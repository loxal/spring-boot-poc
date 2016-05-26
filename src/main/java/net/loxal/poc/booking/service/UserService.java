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