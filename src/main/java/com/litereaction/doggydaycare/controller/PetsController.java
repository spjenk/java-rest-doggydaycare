package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Pet;
import com.litereaction.doggydaycare.repository.PetRepository;
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

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register/update a new pet")
    public ResponseEntity save(@RequestBody Pet pet) {

        try {
            Pet result = petRepository.save(pet);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get pet by id")
    public Pet get(@RequestParam(value = "id", required = true, defaultValue = "0") long id) {
        return petRepository.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update pet details")
    public ResponseEntity save(@RequestParam(value = "id", required = true) long id, @RequestBody Pet pet) {

        try {
            pet = petRepository.save(pet);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a pet from the repository")
    public ResponseEntity delete(@RequestParam(value = "id", required = true) long id) {

        try {
            petRepository.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
