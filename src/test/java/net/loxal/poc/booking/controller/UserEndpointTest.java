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
