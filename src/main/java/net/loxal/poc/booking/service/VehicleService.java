/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
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
