package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.exceptions.NotFoundException;
import com.litereaction.doggydaycare.util.httpUtil;
import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.repository.OwnerRepository;
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
import java.util.List;

@RestController
@RequestMapping(value = "/owners")
@CrossOrigin(origins = "*")
public class OwnersController {

    private Logger log = LoggerFactory.getLogger(OwnersController.class);

    @Autowired
    OwnerRepository ownerRepository;

    /**
     * Get all owners if optional parameters not set
     *
     * @param name optional: get all owners with matching name
     * @param email optional: get user with matching email address
     * @return List of Owners
     */
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

    /**
     * Get owner by ID
     *
     * @param id mandatory: id of the owner
     * @return Matching Owner
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Owner by id ")
    public ResponseEntity<Owner> get(@PathVariable long id) {

        validateOwner(id);
        Owner owner = ownerRepository.findOne(id);
        return new ResponseEntity<Owner>(owner, httpUtil.getHttpHeaders(), HttpStatus.OK);
    }

    /**
     * Create a new owner
     *
     * @param owner Owner
     * @return ResponseEntity and Owner in body
     */
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register a owner")
    public ResponseEntity<Owner> save(@RequestBody Owner owner) {

        try {
            Owner result = ownerRepository.save(owner);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<Owner>(result, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<Owner>(owner, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update an existing owner
     *
     * @param id Mandatory: owner id
     * @param owner Owner to update passed in request body
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "update a owner")
    public ResponseEntity<Owner> save(@PathVariable long id, @RequestBody Owner owner) {

        validateOwner(id);

        try {
            Owner result = ownerRepository.save(owner);
            return new ResponseEntity<Owner>(result, httpUtil.getHttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<Owner>(owner, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Remove an owner
     *
     * @param id Mandatory: owner id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a owner")
    public ResponseEntity delete(@PathVariable long id) {

        validateOwner(id);

        try {
            ownerRepository.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    private void validateOwner(long id) {
        this.ownerRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        log.info("Found owner:" + id);
    }

}
