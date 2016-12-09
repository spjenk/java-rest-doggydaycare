package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.exceptions.NotFoundException;
import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Tenant;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import com.litereaction.doggydaycare.repository.TenantRepository;
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
import java.util.List;

@RestController
@RequestMapping(value = "/tenants")
@CrossOrigin(origins = "*")
public class TenantsController {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    OwnerRepository ownerRepository;

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

    private Tenant validateTenant(long id) {
        Tenant tenant = this.tenantRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        return tenant;
    }
}
