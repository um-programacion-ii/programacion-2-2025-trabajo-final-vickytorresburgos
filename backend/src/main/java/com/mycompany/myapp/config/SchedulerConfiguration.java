package com.mycompany.myapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {
    // Esta clase habilita la anotación @Scheduled en el todo el proyecto
}
