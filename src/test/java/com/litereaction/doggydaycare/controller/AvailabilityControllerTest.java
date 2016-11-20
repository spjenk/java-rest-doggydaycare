package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Availability;
import com.litereaction.doggydaycare.repository.AvailabilityRepository;
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
public class AvailabilityControllerTest {

    private static final String BASE_URL = "/availability/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Before
    public void setup() {
        this.availabilityRepository.deleteAllInBatch();
    }

    @Test
    public void findAllAvailabilityTest() throws Exception {

        this.availabilityRepository.save(new Availability("20161116", 5));
        this.availabilityRepository.save(new Availability("20161117", 5));

        ResponseEntity<String> response = this.restTemplate.getForEntity(BASE_URL, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"date\":\"20161116\",\"max\":5,\"available\":5"));
        assertThat(response.getBody(), containsString("\"date\":\"20161117\",\"max\":5,\"available\":5"));
    }

    @Test
    public void findBookingByDateTest() throws Exception {

        this.availabilityRepository.save(new Availability("20161116", 5));
        this.availabilityRepository.save(new Availability("20161117", 5));


        ResponseEntity<String> response = this.restTemplate.getForEntity("/availability?date=20161116", String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"date\":\"20161116\",\"max\":5,\"available\":5"));
        assertThat(response.getBody(), not(containsString("date\":\"20161117")));
    }

    @Test
    public void createAvailabilityTest() throws Exception {

        String date = "19991231";
        int max = 5;
        Availability availability = new Availability(date, max);

        ResponseEntity<Availability> response =
                this.restTemplate.postForEntity(BASE_URL, availability, Availability.class);

        assertThat(response.getStatusCode() , equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Availability availabilityCreated = response.getBody();
        assertThat(availabilityCreated.getDate(), equalTo(date));
        assertThat(availabilityCreated.getMax(), equalTo(max));
        assertThat(availabilityCreated.getAvailable(), equalTo(max));
    }

    @Test
    public void updateAvailabilityTest() throws Exception {

        String date = "19991231";
        int max = 5;
        Availability availability = new Availability(date, max);

        this.availabilityRepository.save(availability);

        String url = BASE_URL + availability.getDate();

        availability.setMax(6);
        this.restTemplate.put(url, availability);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"max\":6"));
    }

    @Test
    public void deleteAvailabilityTest() throws Exception {

        String date = "19991231";
        int max = 5;
        Availability availability = new Availability(date, max);

        this.availabilityRepository.save(availability);

        String url = BASE_URL + availability.getDate();

        this.restTemplate.delete(url);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));

    }



}
