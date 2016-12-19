package com.litereaction.pawspassport.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoAvailabilityException extends RuntimeException {

    public NoAvailabilityException(String date) {

        super("No availability for day with id: '" + date + "'.");
    }
}
