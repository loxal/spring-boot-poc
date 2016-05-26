/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.service;

import net.loxal.poc.booking.jpa.domain.User;
import net.loxal.poc.booking.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(final User user) {
        Assert.notNull(user);
        return userRepository.saveAndFlush(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(final Long id) {
        Assert.notNull(id);
        return userRepository.findOne(id);
    }

    public User findByEmail(final String email) {
        Assert.hasLength(email);
        return userRepository.findByEmail(email);
    }

    public User findByName(final String lastName) {
        Assert.hasLength(lastName);
        final User userWithLastName = userRepository.findByLastName(lastName);
        if (userWithLastName == null) {
            throw new NotFoundException("User not found.");
        } else {
            return userWithLastName;
        }
    }
}