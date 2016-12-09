package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.exceptions.NoAvailabilityException;
import com.litereaction.doggydaycare.exceptions.NotFoundException;
import com.litereaction.doggydaycare.model.Availability;
import com.litereaction.doggydaycare.model.Booking;
import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.repository.AvailabilityRepository;
import com.litereaction.doggydaycare.repository.BookingRepository;
import com.litereaction.doggydaycare.repository.PetRepository;
import com.litereaction.doggydaycare.util.httpUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/bookings")
@CrossOrigin(origins = "*")
public class BookingsController {

    private Logger log = LoggerFactory.getLogger(BookingsController.class);

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AvailabilityRepository availabilityRepository;

    @Autowired
    PetRepository petRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Make a booking")
    public ResponseEntity<Booking> save(@RequestBody Booking booking) {

        Availability availability = validateAvailability(booking.getAvailability().getId());
        booking.setAvailability(availability);

        validatePetExists(booking.getPet().getId());


        try {

            Booking result = bookingRepository.save(booking);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();

            availability.setAvailable(availability.getAvailable() - 1);
            availabilityRepository.save(availability);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<Booking>(result, headers, HttpStatus.CREATED);


        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<Booking>(booking, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get booking by id")
    public ResponseEntity<Booking> get(@PathVariable long id) {
        validateBookingExists(id);
        Booking booking = bookingRepository.findOne(id);
        return new ResponseEntity<Booking>(booking, httpUtil.getHttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Cancel a booking")
    public ResponseEntity delete(@PathVariable long id) {

        validateBookingExists(id);

        try {

            Booking booking = bookingRepository.findOne(id);
            bookingRepository.delete(id);

            Availability availability = availabilityRepository.findOne(booking.getAvailability().getId());
            availability.setAvailable(availability.getAvailable() + 1);
            availabilityRepository.save(availability);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    private void validateBookingExists(long id) {
        this.bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        log.info("Found booking:" + id);
    }

    private Availability validateAvailability(String availabilityId) {

        Availability availability = this.availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new NotFoundException(availabilityId));

        if (availability.getAvailable() <= 0) {
            throw new NoAvailabilityException(availabilityId);
        }

        log.info("Has availability for booking on:" + availabilityId);
        return availability;
    }

    private Pet validatePetExists(long id) {
        Pet pet = this.petRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        log.info("Found booking:" + id);
        return pet;
    }

}
