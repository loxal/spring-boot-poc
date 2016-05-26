/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.controller;

import net.loxal.poc.booking.Application;
import net.loxal.poc.booking.TestDataInit;
import net.loxal.poc.booking.jpa.domain.Vehicle;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@SpringApplicationConfiguration(classes = Application.class)
public class VehicleEndpointTest extends Common {
    static final WebTarget VEHICLE_ENDPOINT = API_ENDPOINT.path("vehicles");

    @Test
    public void saveVehicle() {
        final Response response = VEHICLE_ENDPOINT
                .request()
                .post(Entity.json(TestDataInit.constructCar(UUID.randomUUID().toString(), "A8", "electric-blue", "42-24", true)));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assureAllFieldsAreSet(response);
    }

    // BONUS sub-task fulfilment validation
    private void assureAllFieldsAreSet(final Response response) {
        final Vehicle createdVehicle = response.readEntity(Vehicle.class);
        assertNotNull(createdVehicle.getActive());
        assertNotNull(createdVehicle.getColor());
        assertNotNull(createdVehicle.getId());
        assertNotNull(createdVehicle.getLicensePlate());
        assertNotNull(createdVehicle.getModel());
        assertNotNull(createdVehicle.getValidTill());
        assertNotNull(createdVehicle.getVin());

        isFutureDate(createdVehicle);
    }

    // BONUS sub-task fulfilment validation
    private void isFutureDate(final Vehicle createdVehicle) {
        createdVehicle.getValidTill().after(new Date());
    }


    @Test
    public void fetchVehicles() {
        final Response response = VEHICLE_ENDPOINT
                .request()
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        final List<Vehicle> vehicles = response.readEntity(new GenericType<List<Vehicle>>() {
        });
        assertNotNull(vehicles);
        assertFalse(vehicles.isEmpty());
    }
}
