package com.exercise.MeetingScheduler.repository;

import com.exercise.MeetingScheduler.model.Reservation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ReservationRepository extends ElasticsearchRepository<Reservation, Integer> {
    List<Reservation> findAll();
    Reservation save(Reservation reservation);

}
