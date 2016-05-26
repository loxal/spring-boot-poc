/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.controller;

import com.wordnik.swagger.annotations.Api;
import net.loxal.poc.booking.jpa.domain.User;
import net.loxal.poc.booking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "users", description = "User resource endpoint")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> fetchAll() {
        LOG.debug("Getting all users");
        return userService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User fetchById(@PathVariable("id") final Long id) {
        LOG.debug("Get user {}", id);
        return userService.findOne(id);
    }

    // using an alternative path is not HATEOAS-compliant
    @RequestMapping(params = "lastName", method = RequestMethod.GET)
    public User getByLastName(@RequestParam("lastName") final String lastName) {
        LOG.debug("Get user by last name {}", lastName);
        return userService.findByName(lastName);
    }
}
