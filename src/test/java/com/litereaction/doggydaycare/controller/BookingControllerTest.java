package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.Model.Booking;
import com.litereaction.doggydaycare.Model.Pet;
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

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookingControllerTest {

    private Logger log = LoggerFactory.getLogger(BookingsController.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PetRepository petRepository;

    @Before
    public void setup() {
        this.bookingRepository.deleteAllInBatch();
    }

    @Test
    public void findBookingTest() throws Exception {

        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        Pet rover = this.petRepository.save(new Pet("Rover", 2));

        this.bookingRepository.save(new Booking("19991231", spot));
        this.bookingRepository.save(new Booking("19991231", rover));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/bookings?date=19991231", String.class);
        log.info("Found Booking:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), containsString("Spot"));
        assertThat(response.getBody(), containsString("Rover"));
    }

    @Test
    public void findBookingWrongDateTest() throws Exception {

        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        Pet rover = this.petRepository.save(new Pet("Rover", 2));

        this.bookingRepository.save(new Booking("19991231", spot));
        this.bookingRepository.save(new Booking("19991231", rover));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/bookings?date=19991230", String.class);
        log.info("Found Booking:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), not(containsString("Spot")));
        assertThat(response.getBody(), not(containsString("Rover")));
    }

    @Test
    public void findBookingSingleItemReturnedTest() throws Exception {

        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        Pet rover = this.petRepository.save(new Pet("Rover", 2));

        this.bookingRepository.save(new Booking("19991231", spot));
        this.bookingRepository.save(new Booking("19991230", rover));

        ResponseEntity<String> response = this.restTemplate.getForEntity("/bookings?date=19991230", String.class);
        log.info("Found Booking:" + response.getBody());
        assertNotNull(response.getBody());
        assertThat(response.getBody(), not(containsString("Spot")));
        assertThat(response.getBody(), containsString("Rover"));
    }

}
