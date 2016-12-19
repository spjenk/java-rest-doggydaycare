package com.litereaction.pawspassport.controller;

import com.litereaction.pawspassport.helper.OwnerUtil;
import com.litereaction.pawspassport.model.Owner;
import com.litereaction.pawspassport.model.Tenant;
import com.litereaction.pawspassport.repository.OwnerRepository;
import com.litereaction.pawspassport.repository.TenantRepository;
import com.litereaction.pawspassport.types.Status;
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
public class OwnerControllerTest {

    private static final String BASE_URL = "/owners/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    private Tenant tenant;

    private Owner owner1;

    private Owner owner2;

    @Before
    public void setup() {
        this.tenant = this.tenantRepository.save(new Tenant("PnR"));
        this.owner1 = this.ownerRepository.save(OwnerUtil.getRandomOwner(tenant));
        this.owner2 = this.ownerRepository.save(OwnerUtil.getRandomOwner(tenant));
    }

    @Test
    public void findAllOwnersTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString(owner1.getName()));
        assertThat(response.getBody(), containsString(owner2.getName()));
    }

    @Test
    public void findOwnerByNameTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?name=" + owner1.getName(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString(owner1.getName()));
        assertThat(response.getBody(), not(containsString(owner2.getName())));
    }

    @Test
    public void findOwnerByEmailTest() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?email=" + owner2.getEmail(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString(owner2.getName()));
        assertThat(response.getBody(), containsString(owner2.getEmail()));
        assertThat(response.getBody(), not(containsString(owner1.getName())));
    }

    @Test
    public void getOwnerByIdTest() throws Exception {

        String url = BASE_URL + owner1.getId();

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString(owner1.getName()));
        assertThat(response.getBody(), containsString(owner1.getEmail()));
        assertThat(response.getBody(), not(containsString(owner2.getName())));
    }

    @Test
    public void getOwnerByWrongIdTest() throws Exception {

        String url = BASE_URL + "9999";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void updateOwnerTest() throws Exception {

        owner1.setDisplayName("JJ");

        this.restTemplate.put(BASE_URL + owner1.getId(), owner1);

        ResponseEntity<String> response = this.restTemplate.getForEntity(BASE_URL + owner1.getId(), String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"displayName\":\"JJ\""));
    }

    @Test
    public void deleteOwnerTest() throws Exception {

        this.restTemplate.delete(BASE_URL + owner1.getId());

        ResponseEntity<String> response = this.restTemplate.getForEntity(BASE_URL + owner1.getId(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

        Owner deletedOwner = this.ownerRepository.findOne(owner1.getId());
        assertThat(deletedOwner.getName(), equalTo(owner1.getName()));
        assertThat(deletedOwner.getStatus(), equalTo(Status.DELETED));
    }
}