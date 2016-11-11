package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Caregiver;
import com.litereaction.doggydaycare.repository.CaregiverRepository;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/caregiver")
@CrossOrigin(origins = "*")
public class CaregiverController {

    private Logger log = LoggerFactory.getLogger(CaregiverController.class);

    @Autowired
    CaregiverRepository caregiverRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all Caregivers")
    public Iterable<Caregiver> get() {
        return caregiverRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Caregiver by id")
    public Caregiver get(@RequestParam(value = "id", required = true, defaultValue = "0") long id) {
        return caregiverRepository.findOne(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register a caregiver")
    public ResponseEntity save(@RequestBody Caregiver caregiver) {

        try {
            caregiverRepository.save(caregiver);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register/update a caregiver")
    public ResponseEntity save(@RequestParam(value = "id", required = true, defaultValue = "0") long id, @RequestBody Caregiver caregiver) {

        try {
            caregiverRepository.save(caregiver);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a caregiver")
    public ResponseEntity delete(@RequestParam(value = "id", required = true) long id) {

        try {
            caregiverRepository.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
