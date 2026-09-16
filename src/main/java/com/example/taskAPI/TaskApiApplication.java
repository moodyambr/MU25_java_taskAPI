package com.example.taskAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Startar hela Spring Boot-applikationen när programmet körs.
public class TaskApiApplication {

    public static void main(String[] args) {
        // Skapar Spring-contexten och startar webservern.
        SpringApplication.run(TaskApiApplication.class, args);
    }

}