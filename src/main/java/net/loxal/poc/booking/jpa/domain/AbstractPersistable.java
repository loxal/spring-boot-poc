/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
abstract class AbstractPersistable implements Serializable, Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        final AbstractPersistable that = (AbstractPersistable) obj;

        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

}