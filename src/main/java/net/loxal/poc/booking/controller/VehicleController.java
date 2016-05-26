/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.controller;

import com.wordnik.swagger.annotations.Api;
import net.loxal.poc.booking.jpa.domain.Vehicle;
import net.loxal.poc.booking.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@Api(value = "vehicles", description = "Vehicle resource endpoint")
public class VehicleController {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Vehicle> fetchAll() {
        LOG.debug("Fetching all vehicles");
        return vehicleService.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Vehicle fetchById(@PathVariable("id") final Long id) {
        LOG.debug("Fetching vehicle");
        return vehicleService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle save(@RequestBody final Vehicle vehicle) {
        LOG.debug("Register vehicle");
        return vehicleService.registerVehicle(vehicle);
    }
}
