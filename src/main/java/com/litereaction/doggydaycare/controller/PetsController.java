package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.exceptions.NotFoundException;
import com.litereaction.doggydaycare.util.httpUtil;
import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.repository.PetRepository;
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
@RequestMapping(value = "/pets")
@CrossOrigin(origins = "*")
public class PetsController {

    private Logger log = LoggerFactory.getLogger(PetsController.class);

    @Autowired
    PetRepository petRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all pets")
    public List<Pet> get(@ApiParam(value = "name", required = false) String name) {

        List<Pet> pets;

        if (StringUtils.isEmpty(name)) {
            pets = petRepository.findAll();
        } else {
            pets = petRepository.findByName(name);
        }

        return pets;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get pet by id")
    public ResponseEntity<Pet> get(@PathVariable long id) {
        validatePetExists(id);
        Pet pet = petRepository.findOne(id);
        return new ResponseEntity<Pet>(pet, httpUtil.getHttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register/update a new pet")
    public ResponseEntity<Pet> save(@RequestBody Pet pet) {

        try {
            Pet result = petRepository.save(pet);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);

            return new ResponseEntity<Pet>(result, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<Pet>(pet, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update pet details")
    public ResponseEntity<Pet> save(@PathVariable long id, @RequestBody Pet pet) {

        validatePetExists(id);

        try {
            Pet result = petRepository.save(pet);
            return new ResponseEntity<Pet>(result, httpUtil.getHttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<Pet>(pet, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a pet from the repository")
    public ResponseEntity delete(@PathVariable long id) {

        validatePetExists(id);

        try {
            petRepository.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private void validatePetExists(long id) {
        this.petRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        log.info("Found pet:" + id);
    }

}
