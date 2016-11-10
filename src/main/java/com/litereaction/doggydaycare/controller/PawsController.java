package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Paws;
import com.litereaction.doggydaycare.repository.PawsRepository;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/paws")
@CrossOrigin(origins = "*")
public class PawsController {

    private Logger log = LoggerFactory.getLogger(PawsController.class);

    @Autowired
    PawsRepository pawsRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all the pets")
    public Iterable<Paws> get() {

        Iterable<Paws> paws = pawsRepository.findAll();
        logPawsRetrieved(paws);
        return paws;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Paws get(@RequestParam(value = "id", required = true, defaultValue = "0") long id) {
        return pawsRepository.findOne(id);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public Iterable<Paws> get(@RequestParam(value = "name", required = true) String name) {

        Iterable<Paws> paws = pawsRepository.findByName(name);
        logPawsRetrieved(paws);

        return paws;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register a new pet")
    public ResponseEntity save(@RequestBody Paws paws) {

        try {
            pawsRepository.save(paws);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        log.info("New pet registered:" + paws.toString());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a pet from the repository")
    public ResponseEntity delete(@RequestParam(value = "id", required = true) long id) {

        try {
            pawsRepository.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    private void logPawsRetrieved(Iterable<Paws> paws) {
        log.info("Retrieving Paws");
        paws.forEach(paw -> log.debug("Found: " + paw.getName()));
    }

}
