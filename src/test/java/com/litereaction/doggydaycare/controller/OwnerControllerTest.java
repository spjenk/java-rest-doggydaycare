package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Tenant;
import com.litereaction.doggydaycare.repository.OwnerRepository;
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
public class OwnerControllerTest {

    private static final String BASE_URL = "/owners/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Before
    public void setup() {
        this.ownerRepository.deleteAllInBatch();
        this.tenantRepository.deleteAllInBatch();
    }

    @After
    public void teardown() {
        this.ownerRepository.deleteAllInBatch();
        this.tenantRepository.deleteAllInBatch();
    }

    @Test
    public void findAllOwnersTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        this.ownerRepository.save(new Owner("Jack", "aaa@edf.com", tenant));
        this.ownerRepository.save(new Owner("Jill", "bbb@edf.com", tenant));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("Jack"));
        assertThat(response.getBody(), containsString("Jill"));
    }

    @Test
    public void findOwnerByNameTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        this.ownerRepository.save(new Owner("Jack", "aaa@edf.com", tenant));
        this.ownerRepository.save(new Owner("Jill", "bbb@edf.com", tenant));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?name=Jack", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("\"name\":\"Jack\""));
        assertThat(response.getBody(), not(containsString("Jill")));
    }

    @Test
    public void findOwnerByEmailTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        this.ownerRepository.save(new Owner("Jack", "aaa@edf.com", tenant));
        this.ownerRepository.save(new Owner("Jill", "Jill@Hill.com", tenant));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/owners?email=Jill@Hill.com", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("\"name\":\"Jill\""));
        assertThat(response.getBody(), containsString("\"email\":\"Jill@Hill.com\""));
        assertThat(response.getBody(), not(containsString("Jack")));
    }

    @Test
    public void getOwnerByIdTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));

        Owner owner = this.ownerRepository.save(new Owner("Bill", "abc@edf.com", tenant));

        String url = BASE_URL + owner.getId();

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(response.getBody());

        assertThat(response.getBody(), containsString("\"name\":\"Bill\""));
        assertThat(response.getBody(), containsString("\"email\":\"abc@edf.com\""));
        assertThat(response.getBody(), not(containsString("Jack")));
    }

    @Test
    public void getOwnerByWrongIdTest() throws Exception {

        String url = BASE_URL + "9999";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

    }

    @Test
    public void updateOwnerTest() throws Exception {

        String ownerName = "Jack";
        String ownerEmail = "abc@edf.com";
        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner owner = this.ownerRepository.save(new Owner(ownerName, ownerEmail, tenant));

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
        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner owner = this.ownerRepository.save(new Owner(ownerName, ownerEmail, tenant));

        String url = BASE_URL + owner.getId();

        this.restTemplate.delete(url);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

    }

}