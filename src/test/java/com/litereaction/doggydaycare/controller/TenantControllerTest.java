package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Tenant;
import com.litereaction.doggydaycare.repository.OwnerRepository;
import com.litereaction.doggydaycare.repository.TenantRepository;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TenantControllerTest {

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

    @Test
    public void findAllTest() {
        Tenant t1 = tenantRepository.save(new Tenant("T1"));
        Tenant t2 = tenantRepository.save(new Tenant("T2"));

        String url = "/tenants/";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":" + t1.getId() + ",\"name\":\"T1\""));
        assertThat(response.getBody(), containsString("\"id\":" + t2.getId() + ",\"name\":\"T2\""));
    }

    @Test
    public void createOwnerTest() throws Exception {

        String ownerName = "Jack";
        String ownerEmail = "abc@edf.com";
        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner owner = new Owner(ownerName, ownerEmail, tenant);

        String url = "/tenants/" + tenant.getId() + "/owners";
        ResponseEntity<Owner> response =
                this.restTemplate.postForEntity(url, owner, Owner.class);

        assertThat(response.getStatusCode() , equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Owner ownerCreated = response.getBody();
        assertThat(ownerCreated.getName(), equalTo(ownerName));
        assertThat(ownerCreated.getEmail(), equalTo(ownerEmail));
    }
}