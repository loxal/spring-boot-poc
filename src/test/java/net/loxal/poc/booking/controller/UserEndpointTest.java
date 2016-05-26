/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.controller;

import net.loxal.poc.booking.jpa.domain.User;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserEndpointTest extends Common {
    private static final String EXISTING_USER_LAST_NAME = "Bond";
    private static final String LAST_NAME_PATH_PARAM = "lastName";

    private static final WebTarget USER_ENDPOINT = API_ENDPOINT.path("users");

    @Test
    public void fetchByName() {
        final String existingLastName = EXISTING_USER_LAST_NAME;

        final Response response = USER_ENDPOINT
                .queryParam(LAST_NAME_PATH_PARAM, existingLastName)
                .request()
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        final User user = response.readEntity(User.class);
        assertEquals(existingLastName, user.getLastName());
    }

    @Test
    public void fetchByNonExistingName() {
        final String nonExistingLastName = EXISTING_USER_LAST_NAME + "missing";

        final Response response = USER_ENDPOINT
                .queryParam(LAST_NAME_PATH_PARAM, nonExistingLastName)
                .request()
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.readEntity(String.class).contains(Response.Status.NOT_FOUND.getReasonPhrase()));
    }

}
