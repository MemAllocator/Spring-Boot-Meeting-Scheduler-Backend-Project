package com.exercise.MeetingScheduler.controller;

import com.exercise.MeetingScheduler.excpertions.ResourceNotFoundException;
import com.exercise.MeetingScheduler.model.Manager;
import com.exercise.MeetingScheduler.service.ManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Validated
@RestController
@RequestMapping("/manager")
public class ManagerController {
    final static Logger logger = LogManager.getLogger(ManagerController.class);
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping(value = "/{managerId}", produces = "application/json")
    public ResponseEntity<Manager> getManager(@PathVariable int managerId) throws Exception {
        logger.info("Fetching Manager with id " + managerId);
        Manager manager = managerService.getManager(managerId);
        if (manager == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(manager, HttpStatus.OK);
    }
    @PostMapping(value="/add",headers="Accept=application/json", consumes = "application/json")
    public ResponseEntity<?> addManager(@Valid @RequestBody Manager manager){
        logger.info("Registering Manager "+ manager);
        try {
            Manager manager1 = managerService.addManager(manager);
            return new ResponseEntity<>(manager1, HttpStatus.OK);
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping(value="/{managerId}", headers ="Accept=application/json")
    public ResponseEntity<?> deleteManager(@PathVariable("managerId") int managerId){
        logger.info("Deleting Manager with Id"+ managerId);
        try {
            Manager man = managerService.deleteManager(managerId);
            return new ResponseEntity<>(man, HttpStatus.OK);
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
