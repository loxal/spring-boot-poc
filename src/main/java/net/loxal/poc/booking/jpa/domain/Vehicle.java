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

package net.loxal.poc.booking.jpa.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
public class Vehicle extends AbstractPersistable {
    @NotEmpty(message = "Value may not be empty")
    @Column(unique = true)
    private String licensePlate;
    @NotEmpty(message = "Value may not be empty")
    private String vin;
    @NotEmpty(message = "Value may not be empty")
    private String model;
    /**
     * true if vehicle is currently booked, false otherwise
     */
    @NotNull(message = "Value may not be null")
    private Boolean active;
    @NotEmpty(message = "Value may not be empty")
    private String color;
    @NotNull(message = "Value may not be null")
    @Future(message = "Date should be in the future")
    private Date validTill;

    public Vehicle(final Long id) {
        super.setId(id);
    }

    private Vehicle() {
    }

    public Vehicle(final String vin) {
        this.vin = vin;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(final String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(final String vin) {
        this.vin = vin;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public Date getValidTill() {
        return validTill;
    }

    public void setValidTill(final Date validTill) {
        this.validTill = validTill;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final Vehicle vehicle = (Vehicle) o;
        return Objects.equals(licensePlate, vehicle.licensePlate) &&
                Objects.equals(vin, vehicle.vin) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(active, vehicle.active) &&
                Objects.equals(color, vehicle.color) &&
                Objects.equals(validTill, vehicle.validTill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), licensePlate, vin, model, active, color, validTill);
    }
}
