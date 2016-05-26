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

package net.loxal.poc.booking.service;

import net.loxal.poc.booking.jpa.domain.Booking;
import net.loxal.poc.booking.jpa.domain.Vehicle;
import net.loxal.poc.booking.jpa.repository.BookingRepository;
import net.loxal.poc.booking.jpa.repository.UserRepository;
import net.loxal.poc.booking.jpa.repository.VehicleRepository;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * BONUS requirement implementation
     */
    private boolean alreadyBooked(final Booking pendingBooking) {
        final Set<Booking> allBookingsForVehiclesWithStatus = bookingRepository.findByVehicleAndStatusIn(pendingBooking.getVehicle(), Arrays.asList(Booking.Status.OPEN, Booking.Status.ACTIVE));
        for (final Booking booking : allBookingsForVehiclesWithStatus) {
            if (booking.getBegin().after(pendingBooking.getBegin()) && booking.getBegin().before(pendingBooking.getEnd())) {
                return true;
            }
        }
        return false;
    }

    private boolean isStatusUpdatePossible(final Booking booking, final Booking.Status status) {
        if (!(status.equals(Booking.Status.ACTIVE) || status.equals(Booking.Status.CANCELLED))) {
            throw new IllegalArgumentException("A status transition is possible to " + Booking.Status.ACTIVE + " & " + Booking.Status.ACTIVE + " status only.");
        }
        return !status.equals(booking.getStatus());
    }

    /**
     * Transitions that *might* not make any sense like ACTIVE->OPEN should be intercepted here.
     *
     * @param id     booking id
     * @param status to transition to
     * @return updated booking
     */
    // @Transactional takes care to avoid race conditions
    // by executing all booking related operation in an atomic (everything or nothing/rollback) fashion.
    @Transactional
    public Booking updateBookingStatus(final Long id, final Booking.Status status) {
        Assert.notNull(id);
        Assert.notNull(status);
        final Booking booking = bookingRepository.findOne(id);
        if (isStatusUpdatePossible(booking, status)) {
            booking.setStatus(status);
            if (Booking.Status.CANCELLED.equals(status)) {
                deactivateVehicle(booking);
            }
            bookingRepository.saveAndFlush(booking);
        }

        return booking;
    }

    private void deactivateVehicle(final Booking booking) {
        final Vehicle vehicleToInactivate = vehicleRepository.findOne(booking.getVehicle().getId());
        vehicleToInactivate.setActive(false);
    }

    // @Transactional takes care to avoid race conditions
    // by executing all booking related operation in an atomic (everything or nothing/rollback) fashion.
    @Transactional
    public Booking book(final Booking booking) {
        Assert.notNull(booking);
        setBookingConstraints(booking);
        if (!isBookable(booking)) {
            throw new IllegalArgumentException("User does not exist or vehicle cannot be booked.");
        }
        activateVehicle(booking);

        return bookingRepository.saveAndFlush(booking);
    }

    private void activateVehicle(final Booking booking) {
        final Vehicle vehicle = vehicleRepository.getOne(booking.getVehicle().getId());
        vehicle.setActive(true);
        vehicleRepository.saveAndFlush(vehicle);
    }

    private boolean isBookable(final Booking booking) {
        final Long bookingId = booking.getUser().getId();
        final Long vehicleId = booking.getVehicle().getId();
        if (userRepository.exists(bookingId) && vehicleRepository.exists(vehicleId)) {
            final Vehicle vehicle = vehicleRepository.findOne(vehicleId);

            return !vehicle.getActive() && !alreadyBooked(booking);
        }

        return false;
    }

    private void setBookingConstraints(final Booking booking) {
        final Date now = new Date();
        booking.setBegin(now);
        booking.setEnd(DateUtils.addHours(now, 24));
        booking.setStatus(Booking.Status.OPEN);

        final Vehicle vehicle = booking.getVehicle();
        vehicle.setActive(true);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findOne(final Long id) {
        Assert.notNull(id);
        return bookingRepository.findOne(id);
    }
}