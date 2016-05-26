/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.poc.booking.controller;

import net.loxal.poc.booking.controller.dto.SystemMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.ws.rs.NotFoundException;

@ControllerAdvice
public class ErrorHandling {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorHandling.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SystemMessageDTO> illegalArgument(final Exception e) {
        LOG.error("Error: ", e);

        return new ResponseEntity<>(new SystemMessageDTO(
                HttpStatus.BAD_REQUEST,
                "invalid_input",
                e.getLocalizedMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SystemMessageDTO> notFound(final Exception e) {
        LOG.info(HttpStatus.NOT_FOUND.toString() + ": ", e);

        return new ResponseEntity<>(new SystemMessageDTO(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getLocalizedMessage()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SystemMessageDTO> generalError(final Exception e) {
        LOG.error("General error: ", e);

        return new ResponseEntity<>(new SystemMessageDTO(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "internal_error",
                e.getLocalizedMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
