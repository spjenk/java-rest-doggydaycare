package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.exceptions.NotFoundException;
import com.litereaction.doggydaycare.model.Availability;
import com.litereaction.doggydaycare.repository.AvailabilityRepository;
import com.litereaction.doggydaycare.util.httpUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Availability> save(@RequestBody Availability availability) {

        try {
            Availability result = availabilityRepository.save(availability);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getDate()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<Availability>(result, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<Availability>(availability, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    @ApiOperation(value = "Get availability by date")
    public ResponseEntity<Availability> availability(@PathVariable String date) {
        validateAvailability(date);
        Availability availability = availabilityRepository.findOne(date);
        return new ResponseEntity<Availability>(availability, httpUtil.getHttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{date}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update availability details")
    public ResponseEntity<Availability> save(@PathVariable String date, @RequestBody Availability availability) {

        validateAvailability(date);

        try {
            Availability result = availabilityRepository.save(availability);
            return new ResponseEntity<Availability>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<Availability>(availability, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{date}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove availability day")
    public ResponseEntity delete(@PathVariable String date) {

        validateAvailability(date);

        try {
            availabilityRepository.delete(date);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private void validateAvailability(String date) {
        this.availabilityRepository.findByDate(date).orElseThrow(
                () -> new NotFoundException(date));
        log.info("Found availability for date:" + date);
    }

}
