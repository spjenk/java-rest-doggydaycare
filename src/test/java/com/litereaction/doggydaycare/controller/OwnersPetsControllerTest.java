package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.model.Tenant;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import com.litereaction.doggydaycare.repository.PetRepository;
import com.litereaction.doggydaycare.repository.TenantRepository;
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

    @Autowired
    private TenantRepository tenantRepository;

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

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));

        Pet spot = new Pet("Spot", 1, jack);
        spot = this.petRepository.save(spot);

        Pet rover = new Pet("Rover", 2, jack);
        rover = this.petRepository.save(rover);

        String url = "/owners/" + jack.getId() + "/pets";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("\"id\":" + spot.getId() + ",\"name\":\"Spot\""));
        assertThat(response.getBody(), containsString("\"id\":" + rover.getId() + ",\"name\":\"Rover\""));
    }

    @Test
    public void onlyThisOwnersPetsAreReturnedTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));
        Owner jill = this.ownerRepository.save(new Owner("Jill", "Jill@Hill.com", tenant));

        Pet spot = new Pet("Spot", 1, jack);
        spot = this.petRepository.save(spot);

        Pet rover = new Pet("Rover", 2, jill);
        rover = this.petRepository.save(rover);

        String url = "/owners/" + jack.getId() + "/pets";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("\"id\":" + spot.getId() + ",\"name\":\"Spot\""));
        assertThat(response.getBody(), not(containsString("\"id\":" + rover.getId() + ",\"name\":\"Rover\"")));
    }

    @Test
    public void createPetTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));

        String petName = "Spot";
        int petAge = 5;
        Pet pet = new Pet(petName, petAge, jack);

        String url = "/owners/" + jack.getId() + "/pets";

        ResponseEntity<Pet> response = this.restTemplate.postForEntity(url, pet, Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getName(), equalTo(petName));
        assertThat(petResponse.getAge(), equalTo(petAge));
    }

    @Test
    public void createPetBadRequestTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));

        String url = "/owners/" + jack.getId() + "/pets";

        ResponseEntity<Pet> response = this.restTemplate.postForEntity(url, new Pet(), Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void updatePetTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner jack = this.ownerRepository.save(new Owner("Jack", "Jack@Hill.com", tenant));

        Pet pet = this.petRepository.save(new Pet("Spot", 5, jack));

        String url = "/owners/" + jack.getId() + "/pets/" + pet.getId();

        pet.setAge(4);

        this.restTemplate.put(url, pet);

        ResponseEntity<Pet> response = this.restTemplate.getForEntity("/pets/" + pet.getId(), Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getId(), equalTo(pet.getId()));
        assertThat(petResponse.getAge(), equalTo(4));
    }
}
