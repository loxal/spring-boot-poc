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
