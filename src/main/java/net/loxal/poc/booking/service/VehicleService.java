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

import net.loxal.poc.booking.jpa.domain.Vehicle;
import net.loxal.poc.booking.jpa.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle registerVehicle(final Vehicle vehicle) {
        Assert.notNull(vehicle);
        return vehicleRepository.saveAndFlush(vehicle);
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle findOne(final Long id) {
        Assert.notNull(id);
        return vehicleRepository.findOne(id);
    }

    public Vehicle findByLicensePlate(final String licensePlate) {
        Assert.hasLength(licensePlate);
        return vehicleRepository.findByLicensePlate(licensePlate);
    }
}
