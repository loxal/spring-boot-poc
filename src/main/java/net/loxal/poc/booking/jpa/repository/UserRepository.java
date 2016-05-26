/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.jpa.repository;

import net.loxal.poc.booking.jpa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * For Spring Data JPA query methods see:
 * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByLastName(String lastName);
}