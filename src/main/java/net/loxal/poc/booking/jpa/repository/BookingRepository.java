/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.jpa.repository;

import net.loxal.poc.booking.jpa.domain.Booking;
import net.loxal.poc.booking.jpa.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * For Spring Data JPA query methods see:
 * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 */

@Component
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Set<Booking> findByVehicleAndStatusIn(Vehicle vehicle, Collection<Booking.Status> status);
}