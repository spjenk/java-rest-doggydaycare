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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/availability")
@CrossOrigin(origins = "*")
public class AvailabilityController {

    private Logger log = LoggerFactory.getLogger(AvailabilityController.class);

    @Autowired
    AvailabilityRepository availabilityRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get availability by id")
    public ResponseEntity<Availability> availability(@PathVariable String id) {
        validateAvailability(id);
        Availability availability = availabilityRepository.findOne(id);
        return new ResponseEntity<Availability>(availability, httpUtil.getHttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update availability details")
    public ResponseEntity<Availability> save(@PathVariable String id, @RequestBody Availability availability) {

        Availability modifyAvailability = validateAvailability(id);
        modifyAvailability.setAvailable(availability.getAvailable());
        modifyAvailability.setMax(availability.getMax());

        try {
            Availability result = availabilityRepository.save(modifyAvailability);
            return new ResponseEntity<Availability>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<Availability>(availability, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    private Availability validateAvailability(String id) {
        Availability availability = this.availabilityRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        log.info("Found availability for:" + id);
        return availability;
    }

}
