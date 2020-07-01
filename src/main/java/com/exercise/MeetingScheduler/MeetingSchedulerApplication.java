package com.exercise.MeetingScheduler;

import com.exercise.MeetingScheduler.model.Manager;
import com.exercise.MeetingScheduler.repository.ManagerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MeetingSchedulerApplication {
	final static Logger logger = LogManager.getLogger(MeetingSchedulerApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(MeetingSchedulerApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ManagerRepository repo) {
		return args -> {
			repo.save(new Manager(1, "Steve"));
			repo.save(new Manager(2, "Mike"));
			repo.save(new Manager(3, "Shay"));
			logger.info("Managers added: " + repo.findByManagerId(1).get().toString());
			//repo.save(new MeetingRoom(1, "meeting room"));
		};
	}
}
