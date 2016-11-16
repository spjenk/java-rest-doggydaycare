package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Pet;
import com.litereaction.doggydaycare.repository.PetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PetsControllerTest {

    private Logger log = LoggerFactory.getLogger(PetsController.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PetRepository petRepository;

    @Before
    public void setup() {
    }

    @Test
    public void findAllPetsTest() throws Exception {

        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        Pet rover = this.petRepository.save(new Pet("Rover", 2));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/pets", String.class);
        log.info("Found Pet:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":" + spot.getId() + ",\"name\":\"Spot\""));
        assertThat(response.getBody(), containsString("\"id\":" + rover.getId() + ",\"name\":\"Rover\""));
    }

    @Test
    public void findPetByNameTest() throws Exception {

        this.petRepository.save(new Pet("Spot", 1));
        this.petRepository.save(new Pet("Rover", 2));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/pets?name=Spot", String.class);
        log.info("Found Pet:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("Spot"));
        assertThat(response.getBody(), not(containsString("Rover")));
    }

}
