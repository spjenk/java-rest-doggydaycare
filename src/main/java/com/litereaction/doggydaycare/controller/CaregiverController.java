package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Caregiver;
import com.litereaction.doggydaycare.Model.Paws;
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
    public Caregiver get(@RequestParam(value = "id", required = true, defaultValue = "0") long id) {
        return caregiverRepository.findOne(id);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register a new paws caregiver")
    public ResponseEntity save(@RequestBody Caregiver caregiver) {

        try {
            caregiverRepository.save(caregiver);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a paws caregiver from the repository")
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
