package ru.dayneko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class HateoasWithEventSourcingStarter {

    public static void main(String[] args) {
        SpringApplication.run(HateoasWithEventSourcingStarter.class, args);
    }
}
