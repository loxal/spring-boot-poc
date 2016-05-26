/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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