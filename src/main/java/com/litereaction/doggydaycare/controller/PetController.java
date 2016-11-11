package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Pet;
import com.litereaction.doggydaycare.repository.PetRepository;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pet")
@CrossOrigin(origins = "*")
public class PetController {

    private Logger log = LoggerFactory.getLogger(PetController.class);

    @Autowired
    PetRepository petRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all pets")
    public Iterable<Pet> get() {

        Iterable<Pet> paws = petRepository.findAll();
        logPawsRetrieved(paws);
        return paws;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get pet by id")
    public Pet get(@RequestParam(value = "id", required = true, defaultValue = "0") long id) {
        return petRepository.findOne(id);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ApiOperation(value = "Get all pets with a given name")
    public Iterable<Pet> get(@RequestParam(value = "name", required = true) String name) {

        Iterable<Pet> paws = petRepository.findByName(name);
        logPawsRetrieved(paws);

        return paws;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register/update a new pet")
    public ResponseEntity save(@RequestBody Pet pet) {

        try {
            petRepository.save(pet);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        log.info("New pet registered:" + pet.toString());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Register a new pet")
    public ResponseEntity save(@RequestParam(value = "id", required = true) long id, @RequestBody Pet pet) {

        try {
            petRepository.save(pet);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        log.info("New pet registered:" + pet.toString());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a pet from the repository")
    public ResponseEntity delete(@RequestParam(value = "id", required = true) long id) {

        try {
            petRepository.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    private void logPawsRetrieved(Iterable<Pet> paws) {
        log.info("Retrieving Pet");
        paws.forEach(paw -> log.debug("Found: " + paw.getName()));
    }

}
