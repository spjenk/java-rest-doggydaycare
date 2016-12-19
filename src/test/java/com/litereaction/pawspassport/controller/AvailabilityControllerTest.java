package com.litereaction.pawspassport.controller;

import com.litereaction.pawspassport.model.Availability;
import com.litereaction.pawspassport.model.Tenant;
import com.litereaction.pawspassport.repository.AvailabilityRepository;
import com.litereaction.pawspassport.repository.TenantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AvailabilityControllerTest {

    private static final String BASE_URL = "/availability/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Test
    public void findAvailabilityByIdTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Availability availability = this.availabilityRepository.save(new Availability(2016,11,16, 5, tenant));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/availability/" + availability.getId(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString(availability.getId()));
    }

    @Test
    public void findAvailabilityWithWrongIdTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/availability/99999", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));;
    }

    @Test
    public void updateAvailabilityTest() throws Exception {

        int max = 5;
        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Availability availability = new Availability(1999,12,31, max, tenant);

        this.availabilityRepository.save(availability);

        String url = BASE_URL + availability.getId();

        availability.setMax(6);
        this.restTemplate.put(url, availability);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"max\":6"));
    }

}
