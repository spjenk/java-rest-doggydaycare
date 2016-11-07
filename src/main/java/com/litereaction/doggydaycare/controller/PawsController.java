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
        return pawsRepository.findAll();
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

    @RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Remove a pet from the repository")
    public ResponseEntity delete(@RequestBody Paws paws) {

        try {
            pawsRepository.delete(paws);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        log.info("Pet removed from registry:" + paws.toString());

        return ResponseEntity.ok().build();
    }

}
