package com.exercise.MeetingScheduler.service;

import com.exercise.MeetingScheduler.model.Manager;
import com.exercise.MeetingScheduler.repository.ManagerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManagerService {
    final static Logger logger = LogManager.getLogger(ManagerService.class);
    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerService(ManagerRepository managerRepository) {
        super();
        this.managerRepository = managerRepository;
    }
    public Manager addManager(Manager manager){
        managerRepository.save(manager);
        return manager;
    }
    public Manager deleteManager(int managerId) throws Exception {
        Optional<Manager> man = managerRepository.findByManagerId(managerId);
        if(man.isPresent()) {
            logger.info("deleting Manager with Id: "+ man.get().getManagerId());
            managerRepository.deleteById(managerId);
            return man.get();
        }
        throw new Exception("Manager to delete with id " + managerId+ " not found");
    }
    public Manager getManager(int id) throws Exception {
        Optional<Manager> man = managerRepository.findByManagerId(id);
        if(man.isPresent()){
            return man.get();
        }
        else throw  new Exception("Manager with id "+ id + " not found");
    }

}
