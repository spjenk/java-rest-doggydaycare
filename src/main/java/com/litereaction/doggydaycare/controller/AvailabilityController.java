package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Availability;
import com.litereaction.doggydaycare.repository.AvailabilityRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/availability")
@CrossOrigin(origins = "*")
public class AvailabilityController {

    private Logger log = LoggerFactory.getLogger(AvailabilityController.class);

    @Autowired
    AvailabilityRepository availabilityRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get availability")
    public List<Availability> get(@ApiParam(value = "name", required = false) String date) {

        List<Availability> availabilities;

        log.info("Date: " + date);

        if (StringUtils.isEmpty(date)) {
            availabilities = availabilityRepository.findAll();
        } else {
            availabilities = new ArrayList<Availability>();
            availabilities.add(availabilityRepository.findOne(date));
        }

        return availabilities;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register new available day")
    public ResponseEntity save(@RequestBody Availability availability) {

        try {
            Availability result = availabilityRepository.save(availability);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getDate()).toUri();
            return ResponseEntity.created(location).build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get availability by date")
    public Availability availability(@RequestParam(value = "date", required = true) String date) {
        return availabilityRepository.findOne(date);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update availability details")
    public ResponseEntity save(@RequestParam(value = "date", required = true) String date, @RequestBody Availability availability) {

        try {
            availability = availabilityRepository.save(availability);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove availability day")
    public ResponseEntity delete(@RequestParam(value = "date", required = true) String date) {

        try {
            availabilityRepository.delete(date);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
