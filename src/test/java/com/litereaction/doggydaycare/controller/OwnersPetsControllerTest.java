package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.model.Tenant;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import com.litereaction.doggydaycare.repository.PetRepository;
import org.junit.After;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnersPetsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private Tenant tenant = new Tenant("PnR");


    @Before
    public void setup() {
        this.petRepository.deleteAllInBatch();
        this.ownerRepository.deleteAllInBatch();
    }

    @After
    public void teardown() {
        this.petRepository.deleteAllInBatch();
        this.ownerRepository.deleteAllInBatch();
    }

    @Test
    public void findAllPetsTest() throws Exception {

        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));

        Pet spot = new Pet("Spot", 1);
        spot.setOwner(jack);
        spot = this.petRepository.save(spot);

        Pet rover = new Pet("Rover", 2);
        rover.setOwner(jack);
        rover = this.petRepository.save(rover);

        String url = "/owners/" + jack.getId() + "/pets";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":" + spot.getId() + ",\"name\":\"Spot\""));
        assertThat(response.getBody(), containsString("\"id\":" + rover.getId() + ",\"name\":\"Rover\""));
    }

    @Test
    public void onlyThisOwnersPetsAreReturnedTest() throws Exception {

        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));
        Owner jill = this.ownerRepository.save(new Owner("Jill", "Jill@Hill.com", tenant));

        Pet spot = new Pet("Spot", 1);
        spot.setOwner(jack);
        spot = this.petRepository.save(spot);

        Pet rover = new Pet("Rover", 2);
        rover.setOwner(jill);
        rover = this.petRepository.save(rover);

        String url = "/owners/" + jack.getId() + "/pets";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":" + spot.getId() + ",\"name\":\"Spot\""));
        assertThat(response.getBody(), not(containsString("\"id\":" + rover.getId() + ",\"name\":\"Rover\"")));
    }

    @Test
    public void createPetTest() throws Exception {

        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));

        String petName = "Spot";
        int petAge = 5;
        Pet pet = new Pet(petName, petAge);
        pet.setOwner(jack);

        String url = "/owners/" + jack.getId() + "/pets";

        ResponseEntity<Pet> response = this.restTemplate.postForEntity(url, pet, Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getName(), equalTo(petName));
        assertThat(petResponse.getAge(), equalTo(petAge));
    }
}
