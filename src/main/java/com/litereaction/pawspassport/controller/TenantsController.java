package com.litereaction.pawspassport.controller;

import com.litereaction.pawspassport.exceptions.NotFoundException;
import com.litereaction.pawspassport.model.Availability;
import com.litereaction.pawspassport.model.Owner;
import com.litereaction.pawspassport.model.Tenant;
import com.litereaction.pawspassport.repository.AvailabilityRepository;
import com.litereaction.pawspassport.repository.OwnerRepository;
import com.litereaction.pawspassport.repository.TenantRepository;
import com.litereaction.pawspassport.types.Status;
import com.litereaction.pawspassport.util.httpUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/tenants")
@CrossOrigin(origins = "*")
public class TenantsController {

    private Logger log = LoggerFactory.getLogger(TenantsController.class);

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

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create Tenant")
    public ResponseEntity<Tenant> create(@RequestBody Tenant tenant) {

        try {
            //default status to active if not set
            if (tenant.getStatus() == null) {
                tenant.setStatus(Status.ACTIVE);
            }
            Tenant result = tenantRepository.save(tenant);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<Tenant>(result, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<Tenant>(tenant, httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove a tenant")
    public ResponseEntity delete(@PathVariable long tenantId) {

        Tenant tenant = validateTenant(tenantId);
        tenant.setStatus(Status.DELETED);

        try {
            tenantRepository.save(tenant);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
                                  @ApiParam(value = "day (1-31)") Integer day,
                                  @ApiParam(value = "limit results") Integer limit) {

        Tenant tenant = validateTenant(tenantId);

        List<Availability> availabilities;
        if (year == null) {
            availabilities = availabilityRepository.findByTenantOrderByBookingDate(tenant);
        } else {
            availabilities = availabilityRepository.findByBookingDateAndTenant(LocalDate.of(year, month, day), tenant);
        }

        if (limit != null && availabilities.size() > limit) {
            availabilities = availabilities.subList(0, limit);
        }

        return availabilities;
    }

    @RequestMapping(value = "/{tenantId}/generateCalendar/{year}", method = RequestMethod.POST)
    @ApiOperation(value = "Generate Calendar of availability")
    public ResponseEntity generateCalendar(@PathVariable Long tenantId,
                                           @PathVariable Integer year,
                                           @RequestBody DayOfWeek[] daysClosed) {

        if (tenantId != null && year != null) {
            Tenant tenant = validateTenant(tenantId);

            LocalDate date = LocalDate.of(year, 01, 01);
            int totalDays = date.isLeapYear() ? 366 : 365;
            for (int i = 0; i < totalDays; i++) {
                log.info("date:" + date.toString());

                Availability availability = new Availability(date.plusDays(i), 10, tenant);
                availabilityRepository.save(availability);
            }
        } else {
            return new ResponseEntity(httpUtil.getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    private Tenant validateTenant(long id) {
        Tenant tenant = this.tenantRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        return tenant;
    }
}
