package com.litereaction.pawspassport.controller;

import com.litereaction.pawspassport.helper.OwnerUtil;
import com.litereaction.pawspassport.helper.PetUtil;
import com.litereaction.pawspassport.model.Owner;
import com.litereaction.pawspassport.model.Pet;
import com.litereaction.pawspassport.model.Tenant;
import com.litereaction.pawspassport.repository.OwnerRepository;
import com.litereaction.pawspassport.repository.PetRepository;
import com.litereaction.pawspassport.repository.TenantRepository;
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

        String url = "/owners/" + owner.getId() + "/pets";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("\"id\":" + spot.getId()));
        assertThat(response.getBody(), containsString(spot.getName()));
        assertThat(response.getBody(), containsString(spot.getBirthDate()));
        assertThat(response.getBody(), containsString("\"id\":" + rover.getId()));
        assertThat(response.getBody(), containsString(rover.getName()));
        assertThat(response.getBody(), containsString(rover.getBirthDate()));
    }

    @Test
    public void onlyThisOwnersPetsAreReturnedTest() throws Exception {

        //change rover to a new owner
        Owner owner2 = this.ownerRepository.save(OwnerUtil.getRandomOwner(tenant));
        rover.setOwner(owner2);
        this.petRepository.save(rover);

        String url = "/owners/" + owner.getId() + "/pets";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("\"id\":" + spot.getId()));
        assertThat(response.getBody(), containsString(spot.getName()));
        assertThat(response.getBody(), containsString(spot.getBirthDate()));

        assertThat(response.getBody(), not(containsString(rover.getName())));
    }

    @Test
    public void createPetTest() throws Exception {

        String url = "/owners/" + owner.getId() + "/pets";

        Pet newPet = PetUtil.getRandomPet(owner);

        ResponseEntity<Pet> response = this.restTemplate.postForEntity(url, newPet, Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getName(), equalTo(newPet.getName()));
        assertThat(petResponse.getBirthDate(), equalTo(newPet.getBirthDate()));
    }

    @Test
    public void createPetBadRequestTest() throws Exception {

        String url = "/owners/" + owner.getId() + "/pets";

        ResponseEntity<Pet> response = this.restTemplate.postForEntity(url, new Pet(), Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void updatePetTest() throws Exception {

        String url = "/owners/" + owner.getId() + "/pets/" + spot.getId();

        spot.setAge("20110303");

        this.restTemplate.put(url, spot);

        ResponseEntity<Pet> response = this.restTemplate.getForEntity("/pets/" + spot.getId(), Pet.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        Pet petResponse = response.getBody();
        assertThat(petResponse.getId(), equalTo(spot.getId()));
        assertThat(petResponse.getBirthDate(), equalTo("20110303"));
    }
}
