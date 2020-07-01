package com.exercise.MeetingScheduler.controller;

import com.exercise.MeetingScheduler.model.MeetingRoom;
import com.exercise.MeetingScheduler.service.MeetingRoomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meetingroom")
public class MeetingRoomController {
    final static Logger logger = LogManager.getLogger(MeetingRoomController.class);
    private final MeetingRoomService meetingRoomServiceImp;

    @Autowired
    public MeetingRoomController(MeetingRoomService service) {

        this.meetingRoomServiceImp = service;
    }

    //do not expose meeting Room Api to clients, instead bootstrap only one meeting room instance as requested
    @PostMapping(value="/create",headers="Accept=application/json", produces = "application/json")
    public ResponseEntity<String> createMeetingRoom(@RequestBody MeetingRoom meetingroom){
        List<MeetingRoom> meetingRooms = meetingRoomServiceImp.getMeetingRooms();
        if(meetingRooms.size() > 0){
            return new ResponseEntity<>("There is Already A Meeting Room", HttpStatus.FORBIDDEN);
        }
        logger.info("Creating Meeting Room "+ meetingroom.getName());
        meetingRoomServiceImp.createMeetingRoom(meetingroom);
        return new ResponseEntity<>("Meeting Room Created Successfully", HttpStatus.CREATED);
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<MeetingRoom> getMeetingRoomById(@PathVariable("id") int id){
        logger.info("Fetching Meeting Room with id " + id);
        MeetingRoom meetingroom = meetingRoomServiceImp.getMeetingRoom(id);
        if (meetingroom == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meetingroom, HttpStatus.OK);
    }
}
