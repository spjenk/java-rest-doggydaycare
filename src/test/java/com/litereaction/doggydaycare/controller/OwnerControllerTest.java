package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Owner;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.SystemProfileValueSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerControllerTest {

    private Logger log = LoggerFactory.getLogger(OwnersController.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OwnerRepository ownerRepository;

    @Before
    public void setup() {
        this.ownerRepository.deleteAllInBatch();
        this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com"));
        this.ownerRepository.save(new Owner("Jill", "Jill@Hill.com"));
    }

    @Test
    public void findAllOwnersTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners", String.class);
        log.info("Found Owner:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("Jack"));
        assertThat(response.getBody(), containsString("Jill"));
    }

    @Test
    public void findOwnerByNameTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?name=Jack", String.class);
        log.info("Found Owner:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"name\":\"Jack\""));
        assertThat(response.getBody(), not(containsString("Jill")));
    }

    @Test
    public void findOwnerByEmailTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?email=Jill@Hill.com", String.class);
        log.info("Found Owner:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"name\":\"Jill\""));
        assertThat(response.getBody(), containsString("\"email\":\"Jill@Hill.com\""));
        assertThat(response.getBody(), not(containsString("Jack")));
    }

    //@Test
    public void createOwnerTest() throws Exception {

        Owner owner = new Owner("Jack", "abc@edf.com");

        ResponseEntity response = this.restTemplate.postForEntity("/owners", owner, Void.class);
        log.info("Response Code:" + response.getStatusCodeValue());
        log.info("Header:" + response.getHeaders());
        assertTrue(201 == response.getStatusCodeValue());

    }

}
