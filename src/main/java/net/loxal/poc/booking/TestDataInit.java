/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking;

import net.loxal.poc.booking.jpa.domain.Booking;
import net.loxal.poc.booking.jpa.domain.User;
import net.loxal.poc.booking.jpa.domain.Vehicle;
import net.loxal.poc.booking.jpa.repository.BookingRepository;
import net.loxal.poc.booking.service.BookingService;
import net.loxal.poc.booking.service.UserService;
import net.loxal.poc.booking.service.VehicleService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;

/**
 * This kind of components should not be part of the implementation code (src/main) but be either moved to src/test
 * or executed as an one-off task by a script outside the regular source tree (src/*).
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TestDataInit {

    // Usually references to src/text from src/main should be avoided
    public static final long ACTIVE_VEHICLE_ID = 3L;
    public static final long FUTURE_BOOKED_VEHICLE = 2L;
    private static final Logger LOG = LoggerFactory.getLogger(TestDataInit.class);
    private static final long UNBOOKED_VEHICLE = 1L;
    private static final Date NOW = new Date();
    @Autowired
    private UserService userService;
    // needs to be used to override business logic and create test data for an optional task
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private VehicleService vehicleService;

    // could be made superfluous by introducing the builder pattern to the Vehicle DTO
    public static Vehicle constructCar(final String licensePlate, final String model, final String color, final String vin, final boolean isActive) {
        final Vehicle vehicle = new Vehicle(vin);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setModel(model);
        vehicle.setColor(color);
        vehicle.setActive(isActive);
        vehicle.setValidTill(DateUtils.addYears(new Date(), 1));
        return vehicle;
    }

    // could be made superfluous by introducing the builder pattern to the Booking DTO
    public static Booking constructBooking(final User user, final Vehicle vehicle, final Booking.Status active) {
        final Booking booking = new Booking();
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setStatus(active);
        booking.setBegin(NOW);
        booking.setEnd(DateUtils.addHours(NOW, 24));
        return booking;
    }

    @PostConstruct
    public void insertTestData() {
        insertTestUsers();
        insertTestVehicles();
        insertBookings();
    }

    private void insertTestUsers() {
        LOG.debug("Inserting test users");
        if (userService.findByEmail("test@user.com") == null) {
            final User maxUser = new User("test@user.com");
            maxUser.setFirstName("Max");
            maxUser.setLastName("Power");
            try {
                maxUser.setBirthday(DateUtils.parseDate("12/12/1980", new String[]{"dd/MM/yyyy"}));
            } catch (final ParseException e) {
                // ignoring
            }

            userService.saveUser(maxUser);
        }

        if (userService.findByEmail("test2@user.com") == null) {
            final User bondUser = new User("test2@user.com");
            bondUser.setFirstName("James");
            bondUser.setLastName("Bond");
            try {
                bondUser.setBirthday(DateUtils.parseDate("12/12/1985", new String[]{"dd/MM/yyyy"}));
            } catch (final ParseException e) {
                // ignoring
            }

            userService.saveUser(bondUser);
        }
    }

    private void insertTestVehicles() {
        LOG.debug("Inserting test vehicles");

        final Vehicle carA = constructCar("INC0", "A", "red", "8765", false);
        vehicleService.registerVehicle(carA);

        final Vehicle carB = constructCar("INC1", "B", "black", "1234", true);
        vehicleService.registerVehicle(carB);

        final Vehicle booked = constructCar("M-5678", "C1", "energy-red", "1234", true);
        vehicleService.registerVehicle(booked);

        final Vehicle inactive = constructCar("M-567", "C2", "energy-blue", "4321", false);
        vehicleService.registerVehicle(inactive);
    }

    private void insertBookings() {
        LOG.debug("Inserting test bookings");
        final Booking plainBooking = constructBooking(new User(1L), new Vehicle(UNBOOKED_VEHICLE), Booking.Status.ACTIVE);

        bookingService.book(plainBooking);

        final Booking futureBooking = constructBooking(new User(2L), new Vehicle(FUTURE_BOOKED_VEHICLE), Booking.Status.OPEN);
        // 15min value for a “future begin” could cause tests to fail when the initialized data is not re-initialized after
        // 15min. This approach should rather be used in the test itself, so the booking is created every time & removed (cleanup).
        final Date futureBegin = DateUtils.addMinutes(NOW, 15);
        futureBooking.setBegin(futureBegin);
        futureBooking.setEnd(DateUtils.addHours(futureBegin, 24));

        bookingRepository.saveAndFlush(futureBooking);
    }
}

