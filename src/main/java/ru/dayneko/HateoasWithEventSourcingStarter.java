package ru.dayneko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HateoasWithEventSourcingStarter {

    public static void main(String[] args) {
        SpringApplication.run(HateoasWithEventSourcingStarter.class, args);
    }
}
