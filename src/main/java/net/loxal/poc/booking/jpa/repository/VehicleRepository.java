/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.jpa.repository;

import net.loxal.poc.booking.jpa.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * For Spring Data JPA query methods see:
 * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Vehicle findByLicensePlate(String licensePlate);
}
