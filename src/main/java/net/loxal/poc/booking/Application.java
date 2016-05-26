/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Application class
 */
@SpringBootApplication
@EnableJpaRepositories
@EntityScan("net.loxal.poc.booking.jpa.domain")
public class Application {
    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }
}
