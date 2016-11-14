package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Owner;
import com.litereaction.doggydaycare.repository.OwnerRepository;
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
import java.util.List;

@RestController
@RequestMapping(value = "/owners")
@CrossOrigin(origins = "*")
public class OwnersController {

    private Logger log = LoggerFactory.getLogger(OwnersController.class);

    @Autowired
    OwnerRepository ownerRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all Caregivers")
    public List<Owner> get(@ApiParam(value = "name", required = false) String name, @ApiParam(value = "email", required = false) String email) {

        List<Owner> owners;

        if (!StringUtils.isEmpty(email)) {
            owners = ownerRepository.findByEmail(email);
        } else if (!StringUtils.isEmpty(name)) {
            owners = ownerRepository.findByName(name);
        } else {
            owners = ownerRepository.findAll();
        }

        return owners;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Owner by id")
    public Owner get(@RequestParam(value = "id", required = true, defaultValue = "0") long id) {
        return ownerRepository.findOne(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register a owner")
    public ResponseEntity save(@RequestBody Owner owner) {

        try {
            Owner result = ownerRepository.save(owner);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register/update a owner")
    public ResponseEntity save(@RequestParam(value = "id", required = true, defaultValue = "0") long id, @RequestBody Owner owner) {

        try {
            ownerRepository.save(owner);
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
            ownerRepository.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
