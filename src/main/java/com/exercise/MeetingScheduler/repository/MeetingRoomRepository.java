package com.exercise.MeetingScheduler.repository;

import com.exercise.MeetingScheduler.model.MeetingRoom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomRepository extends ElasticsearchRepository<MeetingRoom, Integer> {
    MeetingRoom findById(int id);
    List<MeetingRoom> findAll();
    //boolean checkAvailability(Date db, Date de, int id);
}
