package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Availability;
import com.litereaction.doggydaycare.Model.Booking;
import com.litereaction.doggydaycare.Model.Pet;
import com.litereaction.doggydaycare.repository.AvailabilityRepository;
import com.litereaction.doggydaycare.repository.BookingRepository;
import com.litereaction.doggydaycare.repository.PetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger log = LoggerFactory.getLogger(AvailabilityControllerTest.class);

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

        this.availabilityRepository.save(new Availability("20161116", 5, 5));
        this.availabilityRepository.save(new Availability("20161117", 5, 5));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/availability", String.class);
        log.info("Found Availability:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"date\":\"20161116\",\"max\":5,\"available\":5"));
        assertThat(response.getBody(), containsString("\"date\":\"20161117\",\"max\":5,\"available\":5"));
    }

    @Test
    public void findBookingWrongDateTest() throws Exception {

        this.availabilityRepository.save(new Availability("20161116", 5, 5));
        this.availabilityRepository.save(new Availability("20161117", 5, 5));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/availability?date=20161116", String.class);
        log.info("Found Availability:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("\"date\":\"20161116\",\"max\":5,\"available\":5"));
        assertThat(response.getBody(), not(containsString("date\":\"20161117")));
    }

}
