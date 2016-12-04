package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.exceptions.NotFoundException;
import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import com.litereaction.doggydaycare.repository.PetRepository;
import com.litereaction.doggydaycare.util.httpUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/owners/{ownerId}/pets")
public class OwnersPetController {

    private final OwnerRepository ownerRepository;

    private final PetRepository petRepository;

    @Autowired
    OwnersPetController(OwnerRepository ownerRepository, PetRepository petRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Register/update a new pet")
    public ResponseEntity<Pet> save(@PathVariable long ownerId, @RequestBody Pet pet) {

        validateOwner(ownerId);

        try {

            Owner owner =  ownerRepository.findOne(ownerId);
            pet.setOwner(owner);

            Pet result = petRepository.save(pet);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);

            return new ResponseEntity<Pet>(result, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<Pet>(pet, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Pet> get(@PathVariable long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    private void validateOwner(long id) {
        this.ownerRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
    }
}
