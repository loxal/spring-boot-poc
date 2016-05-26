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

package net.loxal.poc.booking.controller;

import net.loxal.poc.booking.TestDataInit;
import net.loxal.poc.booking.jpa.domain.Booking;
import net.loxal.poc.booking.jpa.domain.User;
import net.loxal.poc.booking.jpa.domain.Vehicle;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookingEndpointTest extends Common {
    private static final WebTarget BOOKING_ENDPOINT = API_ENDPOINT.path("bookings");
    private static final String BOOKING_TO_UPDATE = "2";
    private static final long INACTIVE_VEHICLE = 4L;

    @Test
    public void fetchBookings() {
        final Response response = BOOKING_ENDPOINT
                .request()
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        final List<Booking> bookings = response.readEntity(new GenericType<List<Booking>>() {
        });
        assertFalse(bookings.isEmpty());
    }

    @Test
    public void fetchBooking() {
        final Response response = BOOKING_ENDPOINT
                .path(BOOKING_TO_UPDATE)
                .request()
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        final Booking booking = response.readEntity(Booking.class);
        assertNotNull(booking.getId());
    }

    @Test
    public void denyBookingForActiveVehicle() {
        final Vehicle activeVehicle = new Vehicle(TestDataInit.ACTIVE_VEHICLE_ID);
        final Booking bookable = constructBooking(new User(1L), activeVehicle);
        final Response create = BOOKING_ENDPOINT
                .request()
                .post(Entity.json(bookable));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), create.getStatus());
        final String deniedBooking = create.readEntity(String.class);
        assertNotNull(deniedBooking);
    }

    /**
     * BONUS test for a bonus task
     */
    @Test
    public void denyBookingForFutureBookedVehicle() {
        final Vehicle activeVehicle = new Vehicle(TestDataInit.FUTURE_BOOKED_VEHICLE);
        final Booking bookable = constructBooking(new User(2L), activeVehicle);
        final Response create = BOOKING_ENDPOINT
                .request()
                .post(Entity.json(bookable));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), create.getStatus());
        final String deniedBooking = create.readEntity(String.class);
        assertNotNull(deniedBooking);
    }

    @Test
    public void createAndCancelBooking() {
        final Booking bookable = constructBooking(new User(1L), new Vehicle(INACTIVE_VEHICLE));
        final Booking createdBooking = assureBookingCreation(bookable);

        final String createdBookingId = createdBooking.getId().toString();
        validateCreatedBooking(bookable, createdBookingId);

        final Response response = BOOKING_ENDPOINT
                .path(createdBookingId)
                .path("status")
                .request()
                .put(Entity.json(Booking.Status.CANCELLED));

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        final String empty = response.readEntity(String.class);
        assertTrue(empty.isEmpty());

        assureVehicleInactivity(createdBookingId);
    }

    private Booking assureBookingCreation(final Booking bookable) {
        final Response create = BOOKING_ENDPOINT
                .request()
                .post(Entity.json(bookable));

        assertEquals(Response.Status.CREATED.getStatusCode(), create.getStatus());
        final Booking createdBooking = create.readEntity(Booking.class);
        assertNotNull(createdBooking.getId());
        return createdBooking;
    }

    private void validateCreatedBooking(final Booking bookable, final String id) {
        final Response created = BOOKING_ENDPOINT
                .path(id)
                .request()
                .get();

        final Booking fetchedBooking = created.readEntity(Booking.class);

        assertEquals(Booking.Status.OPEN, fetchedBooking.getStatus());
        assertEquals(reducePrecisionToTest(bookable.getBegin()), reducePrecisionToTest(fetchedBooking.getBegin()));
        assertEquals(reducePrecisionToTest(bookable.getEnd()), reducePrecisionToTest(fetchedBooking.getEnd()));
        assertEquals(bookable.getUser().getId(), fetchedBooking.getUser().getId());
        assertEquals(bookable.getVehicle().getId(), fetchedBooking.getVehicle().getId());
        assertEquals(true, fetchedBooking.getVehicle().getActive());
    }

    /**
     * Required because the time is set by server (for now).
     * Minute-wise precision is reasonable for testing and avoids further implementation complexity.
     */
    private String reducePrecisionToTest(final Date date) {
        return date.toString().substring(0, 18);
    }

    private Booking constructBooking(final User user, final Vehicle vehicle) {
        // reuse code, the status actually does not matter as it is overridden in the service layer
        return TestDataInit.constructBooking(user, vehicle, Booking.Status.CANCELLED);
    }

    private void assureVehicleInactivity(final String bookingId) {
        final Response bookingToUpdate = BOOKING_ENDPOINT
                .path(bookingId)
                .request()
                .get();
        final Booking booking = bookingToUpdate.readEntity(Booking.class);
        final String vehicleIdOfUpdatedBooking = booking.getVehicle().getId().toString();

        final Response reactivatedVehicle = VehicleEndpointTest.VEHICLE_ENDPOINT
                .path(vehicleIdOfUpdatedBooking)
                .request()
                .get();

        final Vehicle vehicle = reactivatedVehicle.readEntity(Vehicle.class);
        assertEquals(false, vehicle.getActive());
    }

    @Test
    public void updateBookingStatusToActive() {
        final Response response = BOOKING_ENDPOINT
                .path(BOOKING_TO_UPDATE)
                .path("status")
                .request()
                .put(Entity.json(Booking.Status.ACTIVE));

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        final String empty = response.readEntity(String.class);
        assertTrue(empty.isEmpty());
    }

    @Test
    public void denyUpdateBookingStatusToOpen() {
        final Response response = BOOKING_ENDPOINT
                .path(BOOKING_TO_UPDATE)
                .path("status")
                .request()
                .put(Entity.json(Booking.Status.OPEN));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        final String empty = response.readEntity(String.class);
        assertFalse(empty.isEmpty());
    }

}
