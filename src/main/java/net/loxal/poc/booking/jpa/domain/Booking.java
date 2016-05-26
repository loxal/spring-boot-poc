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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Booking extends AbstractPersistable {

    @NotNull(message = "Value may not be null")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Vehicle vehicle;
    @NotNull(message = "Value may not be null")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private User user;
    private Status status;
    @NotNull(message = "Value may not be null")
    private Date begin;
    @NotNull(message = "Value may not be null")
    private Date end;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(final Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(final Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(final Date end) {
        this.end = end;
    }

    public enum Status {
        OPEN,
        ACTIVE,
        CANCELLED
    }
}
