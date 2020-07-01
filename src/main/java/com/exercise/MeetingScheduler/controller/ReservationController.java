package com.exercise.MeetingScheduler.controller;

import com.exercise.MeetingScheduler.model.Reservation;
import com.exercise.MeetingScheduler.service.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/reservation")
public class ReservationController {
    final static Logger logger = LogManager.getLogger(ReservationController.class);
    private final ReservationService reservationServiceImp;

    public ReservationController(ReservationService reservationServiceImp) {
        this.reservationServiceImp = reservationServiceImp;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createReservation(@Valid @RequestBody Reservation reservation) {
        logger.info("Creating Reservation " + reservation);
        try {
            Reservation r = reservationServiceImp.createReservation(reservation);
            return new ResponseEntity<>(r, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping(value="/update", headers="Accept=application/json")
    public ResponseEntity<?> updateReservation(@Valid @RequestBody Reservation reservation) {
        try {
            Reservation reservation1 = reservationServiceImp.updateReservation(reservation);
            return new ResponseEntity<>(reservation1, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    @DeleteMapping(value="/{id}", headers ="Accept=application/json")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") int id){
        try {
            Reservation reservation = reservationServiceImp.deleteReservation(id);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(value = "/get", produces = "application/json")
    public List<Reservation> getAllMeetingRoom() {
        List<Reservation> reservationList =reservationServiceImp.getAllReservations();
        return reservationList;
    }
}
