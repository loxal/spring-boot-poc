/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.controller;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

abstract class Common {
    private static final Client CLIENT = ClientBuilder.newClient();

    static final WebTarget API_ENDPOINT = CLIENT
            .target("http://localhost:8080")
            .path("/api/v1");
}
