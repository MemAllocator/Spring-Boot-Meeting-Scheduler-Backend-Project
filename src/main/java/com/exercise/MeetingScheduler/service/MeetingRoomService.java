package com.exercise.MeetingScheduler.service;

import com.exercise.MeetingScheduler.model.MeetingRoom;
import com.exercise.MeetingScheduler.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;

    @Autowired
    public MeetingRoomService(MeetingRoomRepository meetingRoomRepository) {
        super();
        this.meetingRoomRepository = meetingRoomRepository;
    }

    public MeetingRoom createMeetingRoom(MeetingRoom meetingRoom) {
        return meetingRoomRepository.save(meetingRoom);
    }
    public MeetingRoom getMeetingRoom(int id){
        MeetingRoom meetingRoom;
        meetingRoom = meetingRoomRepository.findById(id);
        return meetingRoom;
    }
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRoomRepository.findAll();
    }

}
