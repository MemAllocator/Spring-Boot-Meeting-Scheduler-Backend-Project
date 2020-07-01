package com.exercise.MeetingScheduler.repository;

import com.exercise.MeetingScheduler.model.Manager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends ElasticsearchRepository<Manager, Integer> {
    Iterable<Manager> findAll();
    Optional<Manager> findByManagerId(int id);
    Manager deleteById(int id);
    Manager save(Manager manager);
}
