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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
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

        this.availabilityRepository.save(new Availability(2016,11,16, 5));
        this.availabilityRepository.save(new Availability(2016,11,17, 5));

        ResponseEntity<String> response = this.restTemplate.getForEntity(BASE_URL, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":\"20161116\",\"max\":5,\"available\":5"));
        assertThat(response.getBody(), containsString("\"id\":\"20161117\",\"max\":5,\"available\":5"));
    }

    @Test
    public void findBookingByDateTest() throws Exception {

        this.availabilityRepository.save(new Availability(2016,11,16, 5));
        this.availabilityRepository.save(new Availability(2016,11,17, 5));


        ResponseEntity<String> response = this.restTemplate.getForEntity("/availability?year=2016&month=11&day=16", String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"id\":\"20161116\",\"max\":5,\"available\":5"));
        assertThat(response.getBody(), not(containsString("id\":\"20161117")));
    }

    @Test
    public void updateAvailabilityTest() throws Exception {

        int max = 5;
        Availability availability = new Availability(1999,12,31, max);

        this.availabilityRepository.save(availability);

        String url = BASE_URL + availability.getId();

        availability.setMax(6);
        this.restTemplate.put(url, availability);

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"max\":6"));
    }
}
