package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.helper.OwnerUtil;
import com.litereaction.doggydaycare.helper.PetUtil;
import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.model.Tenant;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import com.litereaction.doggydaycare.repository.PetRepository;
import com.litereaction.doggydaycare.repository.TenantRepository;
import com.litereaction.doggydaycare.types.Status;
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

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    TenantRepository tenantRepository;

    private Tenant tenant = new Tenant("PnR");
    private Owner owner;
    private Pet spot;
    private Pet rover;

    @Before
    public void setup() {

        tenant = this.tenantRepository.save(tenant);
        owner = this.ownerRepository.save(OwnerUtil.getRandomOwner(tenant));
        spot = this.petRepository.save(PetUtil.getRandomPet(owner));
        rover = this.petRepository.save(PetUtil.getRandomPet(owner));
    }

    @Test
    public void findAllPetsTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/pets", String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":" + spot.getId()));
        assertThat(response.getBody(), containsString(spot.getName()));
        assertThat(response.getBody(), containsString(spot.getBirthDate()));
        assertThat(response.getBody(), containsString("\"id\":" + rover.getId()));
        assertThat(response.getBody(), containsString(rover.getName()));
        assertThat(response.getBody(), containsString(rover.getBirthDate()));
    }

    @Test
    public void findPetByNameTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/pets?name=" + spot.getName(), String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString(spot.getName()));
        assertThat(response.getBody(), not(containsString(rover.getName())));
    }

    @Test
    public void findPetByIdTest() throws Exception {

        String url = BASE_URL + spot.getId();

        ResponseEntity<Pet> response = this.restTemplate.getForEntity(url, Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getId(), equalTo(spot.getId()));
        assertThat(petResponse.getName(), equalTo(spot.getName()));
        assertThat(petResponse.getBirthDate(), equalTo(spot.getBirthDate()));
        assertThat(petResponse.getStatus(), equalTo(Status.ACTIVE));
    }

    @Test
    public void deleteOwnerTest() throws Exception {

        String url = BASE_URL + spot.getId();

        this.restTemplate.delete(url);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));

        Pet deletedPet = petRepository.findOne(spot.getId());
        assertThat(deletedPet.getId(), equalTo(spot.getId()));
        assertThat(deletedPet.getName(), equalTo(spot.getName()));
        assertThat(deletedPet.getBirthDate(), equalTo(spot.getBirthDate()));
        assertThat(deletedPet.getStatus(), equalTo(Status.DELETED));

    }
}
