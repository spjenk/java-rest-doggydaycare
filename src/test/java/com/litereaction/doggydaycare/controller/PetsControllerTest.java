package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.repository.PetRepository;
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
public class PetsControllerTest {

    private static final String BASE_URL = "/pets/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PetRepository petRepository;

    @Before
    public void setup() {
        this.petRepository.deleteAllInBatch();
    }

    @Test
    public void findAllPetsTest() throws Exception {

        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        Pet rover = this.petRepository.save(new Pet("Rover", 2));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/pets", String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":" + spot.getId() + ",\"name\":\"Spot\""));
        assertThat(response.getBody(), containsString("\"id\":" + rover.getId() + ",\"name\":\"Rover\""));
    }

    @Test
    public void findPetByNameTest() throws Exception {

        this.petRepository.save(new Pet("Spot", 1));
        this.petRepository.save(new Pet("Rover", 2));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/pets?name=Spot", String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("Spot"));
        assertThat(response.getBody(), not(containsString("Rover")));
    }

    @Test
    public void findPetByIdTest() throws Exception {

        String petName = "Spot";
        int petAge = 5;

        Pet pet = this.petRepository.save(new Pet(petName, petAge));

        String url = BASE_URL + pet.getId();

        ResponseEntity<Pet> response = this.restTemplate.getForEntity(url, Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getId(), equalTo(pet.getId()));
        assertThat(petResponse.getName(), equalTo(petName));
        assertThat(petResponse.getAge(), equalTo(petAge));
    }

    @Test
    public void updatePetTest() throws Exception {

        String petName = "Spot";
        int petAge = 5;
        Pet pet = this.petRepository.save(new Pet(petName, petAge));

        String url = BASE_URL + pet.getId();

        pet.setAge(4);

        this.restTemplate.put(url, pet);

        ResponseEntity<Pet> response = this.restTemplate.getForEntity(url, Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getId(), equalTo(pet.getId()));
        assertThat(petResponse.getAge(), equalTo(4));
    }

    @Test
    public void deleteOwnerTest() throws Exception {

        String petName = "Spot";
        int petAge = 5;
        Pet pet = this.petRepository.save(new Pet(petName, petAge));

        String url = BASE_URL + pet.getId();

        this.restTemplate.delete(url);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));

    }
}
