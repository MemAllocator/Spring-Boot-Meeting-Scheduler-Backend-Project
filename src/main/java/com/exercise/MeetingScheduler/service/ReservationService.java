package com.exercise.MeetingScheduler.service;

import com.exercise.MeetingScheduler.excpertions.DateValidationException;
import com.exercise.MeetingScheduler.excpertions.ResourceNotFoundException;
import com.exercise.MeetingScheduler.model.Manager;
import com.exercise.MeetingScheduler.model.Reservation;
import com.exercise.MeetingScheduler.repository.ManagerRepository;
import com.exercise.MeetingScheduler.repository.ReservationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReservationService {
    final static Logger logger = LogManager.getLogger(ReservationService.class);
    public static final int HOUR_IN_MILLIS = 3600000;
    private final ReservationRepository reservationRepository;
    private final ManagerRepository managerRepository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String OFFICE_START_HOUR = "9:00:00";
    private static final String OFFICE_END_HOUR = "18:00:00";

    private final ElasticsearchRestTemplate client;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ManagerRepository managerRepository, ElasticsearchRestTemplate client) {
        super();
        this.reservationRepository = reservationRepository;
        this.managerRepository = managerRepository;
        this.client = client;
    }

    public Reservation deleteReservation(int id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservationRepository.deleteById(id);
            return reservation.get();
        } else throw new ResourceNotFoundException("Reservation", "Id", id);

    }

    public Reservation updateReservation(Reservation reservation) throws Exception {
        Optional<Reservation> newReservation = reservationRepository.findById(reservation.getReservationId());
        if (newReservation.isPresent()) {
            Reservation updatedReservation = newReservation.get();
            validateManagers(updatedReservation);
            updatedReservation.setDateBegin(reservation.getDateBegin());
            updatedReservation.setDateEnd(reservation.getDateEnd());
            updatedReservation.setAttendedManagers(reservation.getAttendedManagers());
            isHoursValid(updatedReservation);
            checkOverlappingQuery(reservation);
            validateManagers(reservation);
            reservationRepository.save(reservation);
            return reservation;

        } else throw new ResourceNotFoundException("Reservation", "Id", reservation.getReservationId());

    }

    public Reservation createReservation(Reservation reservation) throws Exception {
        Optional<Reservation> reservation1 = reservationRepository.findById(reservation.getReservationId());
        if (reservation1.isPresent())
            throw new Exception("reservation Id " + reservation.getReservationId() + " already exists");
        isHoursValid(reservation);
        checkOverlappingQuery(reservation);
        validateManagers(reservation);
        reservationRepository.save(reservation);
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    private void validateManagers(Reservation reservation) throws Exception {
        for (Manager manager : reservation.getAttendedManagers()) {
            boolean isRegistered = isManagerRegistered(manager);
            if (!isRegistered) {
                reservation.getAttendedManagers().remove(manager);
            }
        }
    }

    private void isHoursValid(Reservation reservation) throws ParseException, DateValidationException {
        long startTime = reservation.getDateBegin().getTime();
        long endTime = reservation.getDateEnd().getTime();
        long rangeOfMeeting = endTime - startTime;
        logger.info("rangeOfMeeting in millis: " + rangeOfMeeting);
        if (rangeOfMeeting < HOUR_IN_MILLIS)
            throw new DateValidationException("cannot Schedule a Meeting that is less then one hour");
        scheduleRangeCheck(reservation);
    }

    private void scheduleRangeCheck(Reservation reservation) throws ParseException, DateValidationException {
        String date = getDateOnly();
        logger.info("begin date: " + reservation.getDateBegin());
        logger.info("end date: " + reservation.getDateEnd());
        TimeZone zone = TimeZone.getDefault();
        logger.info(zone.getDisplayName());
        logger.info(zone.getID());
        Date startTime = dateFormat.parse(date + " " + OFFICE_START_HOUR);
        Date endTime = dateFormat.parse(date + " " + OFFICE_END_HOUR);

        if (reservation.getDateBegin().after(startTime) && reservation.getDateEnd().before(endTime)) {
            logger.info("Meeting hours range is Ok");
        } else {
            throw new DateValidationException("Wrong date hours range, meeting hours must be between " + OFFICE_START_HOUR + " - " + OFFICE_END_HOUR);
        }

    }

    private String getDateOnly() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        logger.info(dateOnly.format(cal.getTime()));
        return dateOnly.format(cal.getTime());

    }

    private void checkOverlappingQuery(Reservation reservation) throws DateValidationException {

        //all this extra variables used its because i didnt yet figure out out to merge all cases into one query
        // i would really love to learn it though!
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder boolBuilder2 = QueryBuilders.boolQuery();
        BoolQueryBuilder boolBuilder3 = QueryBuilders.boolQuery();
        RangeQueryBuilder fromRangeQuery = QueryBuilders.
                rangeQuery("dateBegin")
                .lte(reservation.getDateEnd().getTime());

        RangeQueryBuilder toRangeQuery = QueryBuilders.
                rangeQuery("dateEnd")
                .gte(reservation.getDateEnd().getTime());

        RangeQueryBuilder fromRangeQuery2 = QueryBuilders.
                rangeQuery("dateBegin")
                .lte(reservation.getDateBegin().getTime());

        RangeQueryBuilder toRangeQuery2 = QueryBuilders.
                rangeQuery("dateEnd")
                .gte(reservation.getDateBegin().getTime());

        RangeQueryBuilder fromRangeQuery3 = QueryBuilders.
                rangeQuery("dateBegin")
                .gte(reservation.getDateBegin().getTime());
        RangeQueryBuilder toRangeQuery3 = QueryBuilders.
                rangeQuery("dateEnd")
                .lte(reservation.getDateEnd().getTime());
        BoolQueryBuilder boolBuilder4 = QueryBuilders.boolQuery().should(boolBuilder.
                filter(fromRangeQuery).
                filter(toRangeQuery)).should(boolBuilder2.
                filter(fromRangeQuery2).
                filter(toRangeQuery2)).should(boolBuilder3.
                filter(fromRangeQuery3).
                filter(toRangeQuery3));
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolBuilder4)
                .build();
        SearchHits<Reservation> results = client.search(searchQuery, Reservation.class, IndexCoordinates.of("reservations"));
        if (results.hasSearchHits()) {
            throw new DateValidationException("cannot Schedule a Meeting that is overlapping with another meeting");
        }

    }

    private boolean isManagerRegistered(Manager manager) throws Exception {
        logger.info("Checking if Manager: " + manager.getManagerName() + " is registered");
        Optional<Manager> man = managerRepository.findByManagerId(manager.getManagerId());
        if (man.isPresent())
            return true;
        else throw new Exception("Manager " + manager.getManagerName() + " is not registered in Company");

    }
}
