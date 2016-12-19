package com.litereaction.pawspassport.controller;

import com.litereaction.pawspassport.exceptions.NotFoundException;
import com.litereaction.pawspassport.model.Availability;
import com.litereaction.pawspassport.model.Owner;
import com.litereaction.pawspassport.model.Tenant;
import com.litereaction.pawspassport.repository.AvailabilityRepository;
import com.litereaction.pawspassport.repository.OwnerRepository;
import com.litereaction.pawspassport.repository.TenantRepository;
import com.litereaction.pawspassport.util.httpUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/tenants")
@CrossOrigin(origins = "*")
public class TenantsController {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    AvailabilityRepository availabilityRepository;


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get Tenants")
    public List<Tenant> get() {
        return tenantRepository.findAll();
    }

    @RequestMapping(value = "/{tenantId}/owners", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Register a owner")
    public ResponseEntity<Owner> save(@PathVariable Long tenantId, @RequestBody Owner owner) {

        Tenant tenant = validateTenant(tenantId);

        try {
            owner.setTenant(tenant);
            Owner result = ownerRepository.save(owner);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<Owner>(result, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<Owner>(owner, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{tenantId}/availability", method = RequestMethod.GET)
    @ApiOperation(value = "Get availability")
    public List<Availability> get(@PathVariable Long tenantId,
                                  @ApiParam(value = "year") Integer year,
                                  @ApiParam(value = "month (1-12)") Integer month,
                                  @ApiParam(value = "day (1-31)") Integer day) {

        Tenant tenant = validateTenant(tenantId);

        List<Availability> availabilities = availabilityRepository.findByBookingDateAndTenant(LocalDate.of(year, month, day), tenant);
        return availabilities;
    }

    private Tenant validateTenant(long id) {
        Tenant tenant = this.tenantRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        return tenant;
    }
}
