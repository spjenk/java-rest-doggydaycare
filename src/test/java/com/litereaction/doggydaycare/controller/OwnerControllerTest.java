package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerControllerTest {

    final String BASE_URL = "/owners/";

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
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("Jack"));
        assertThat(response.getBody(), containsString("Jill"));
    }

    @Test
    public void findOwnerByNameTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?name=Jack", String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"name\":\"Jack\""));
        assertThat(response.getBody(), not(containsString("Jill")));
    }

    @Test
    public void findOwnerByEmailTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?email=Jill@Hill.com", String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"name\":\"Jill\""));
        assertThat(response.getBody(), containsString("\"email\":\"Jill@Hill.com\""));
        assertThat(response.getBody(), not(containsString("Jack")));
    }

    @Test
    public void getOwnerByIdTest() throws Exception {

        Owner owner = this.ownerRepository.save(new Owner("Bill", "abc@edf.com"));

        String url = BASE_URL + owner.getId();

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"name\":\"Bill\""));
        assertThat(response.getBody(), containsString("\"email\":\"abc@edf.com\""));
        assertThat(response.getBody(), not(containsString("Jack")));
    }

    @Test
    public void createOwnerTest() throws Exception {

        String ownerName = "Jack";
        String ownerEmail = "abc@edf.com";
        Owner owner = new Owner(ownerName, ownerEmail);

        ResponseEntity<Owner> response =
                this.restTemplate.postForEntity(BASE_URL, owner, Owner.class, Collections.EMPTY_MAP);

        assertThat(response.getStatusCode() , equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Owner ownerCreated = response.getBody();
        assertThat(ownerCreated.getName(), equalTo(ownerName));
        assertThat(ownerCreated.getEmail(), equalTo(ownerEmail));
    }

    @Test
    public void updateOwnerTest() throws Exception {

        String ownerName = "Jack";
        String ownerEmail = "abc@edf.com";
        Owner owner = this.ownerRepository.save(new Owner(ownerName, ownerEmail));

        String url = BASE_URL + owner.getId();

        owner.setDisplayName("JJ");

        this.restTemplate.put(url, owner);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"displayName\":\"JJ\""));

    }

    @Test
    public void deleteOwnerTest() throws Exception {

        String ownerName = "Jack";
        String ownerEmail = "abc@edf.com";
        Owner owner = this.ownerRepository.save(new Owner(ownerName, ownerEmail));

        String url = BASE_URL + owner.getId();

        this.restTemplate.delete(url);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));

    }

}