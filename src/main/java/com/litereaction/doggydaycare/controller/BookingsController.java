package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Availability;
import com.litereaction.doggydaycare.Model.Booking;
import com.litereaction.doggydaycare.repository.AvailabilityRepository;
import com.litereaction.doggydaycare.repository.BookingRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping(value = "/bookings")
@CrossOrigin(origins = "*")
public class BookingsController {

    private Logger log = LoggerFactory.getLogger(BookingsController.class);

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AvailabilityRepository availabilityRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get bookings")
    public List<Booking> get(@ApiParam(value = "name", required = false) String date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.parse(date);
            return bookingRepository.findByDate(date);
        } catch (Exception e) {
            log.error("Error:" + e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Make a booking")
    public ResponseEntity save(@RequestBody Booking booking) {

        try {

            Availability availability = availabilityRepository.findOne(booking.getDate());

            if (availability.getAvailable() > 0) {
                Booking result = bookingRepository.save(booking);

                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                        .buildAndExpand(result.getId()).toUri();

                availability.setAvailable(availability.getAvailable()-1);
                availabilityRepository.save(availability);

                return ResponseEntity.created(location).build();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get booking by id")
    public Booking get(@RequestParam(value = "id", required = true, defaultValue = "0") long id) {
        return bookingRepository.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Cancel a booking")
    public ResponseEntity delete(@RequestParam(value = "id", required = true) long id) {

        try {

            Booking booking = bookingRepository.findOne(id);
            if (booking != null) {
                bookingRepository.delete(id);

                Availability availability = availabilityRepository.findOne(booking.getDate());
                availability.setAvailable(availability.getAvailable() + 1);
                availabilityRepository.save(availability);

                return ResponseEntity.ok().build();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

}
