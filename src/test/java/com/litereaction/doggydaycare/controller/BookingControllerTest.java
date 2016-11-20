package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.Availability;
import com.litereaction.doggydaycare.model.Booking;
import com.litereaction.doggydaycare.model.Pet;
import com.litereaction.doggydaycare.repository.AvailabilityRepository;
import com.litereaction.doggydaycare.repository.BookingRepository;
import com.litereaction.doggydaycare.repository.PetRepository;
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
public class BookingControllerTest {

    private static final int MAX = 5;
    private static final String BASE_URL = "/bookings/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AvailabilityRepository availabiltyRepository;

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
        assertNotNull(response.getBody());
        assertThat(response.getBody(), not(containsString("Spot")));
        assertThat(response.getBody(), containsString("Rover"));
    }

    @Test
    public void createBookingTest() throws Exception {

        String bookingDate = "19991231";

        ResponseEntity<Booking> response = getBookingResponseEntity(bookingDate);

        assertThat(response.getStatusCode() , equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Booking createBookingResult = response.getBody();
        assertThat(createBookingResult.getDate(), equalTo(bookingDate));
        assertThat(createBookingResult.getPet().getName(), equalTo("Spot"));

        Availability availability = availabiltyRepository.findOne(bookingDate);
        assertThat(availability.getAvailable(), equalTo(4));
    }

    @Test
    public void deleteBookingTest() throws Exception {

        String bookingDate = "19991231";

        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        availabiltyRepository.save(new Availability(bookingDate, MAX));

        Booking booking = new Booking(bookingDate, spot);
        bookingRepository.save(booking);

        this.restTemplate.delete(getUrl(booking));

        ResponseEntity<String> response = this.restTemplate.getForEntity(getUrl(booking), String.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deleteBookingEnsureAvailabilityUpdatesTest() throws Exception {

        String bookingDate = "19991231";

        Booking booking = getBookingResponseEntity(bookingDate).getBody();

        Availability availability = availabiltyRepository.findOne(bookingDate);
        assertThat(availability.getAvailable(), equalTo(MAX-1));

        this.restTemplate.delete(getUrl(booking));

        ResponseEntity<String> response = this.restTemplate.getForEntity(getUrl(booking), String.class);
        assertThat(response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));

        availability = availabiltyRepository.findOne(bookingDate);
        assertThat(availability.getAvailable(), equalTo(MAX));
    }

    @Test
    public void createBookingNoAvailabilityTest() throws Exception {

        String bookingDate = "19991231";

        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        Booking booking = new Booking(bookingDate, spot);

        availabiltyRepository.save(new Availability(bookingDate, 0));

        ResponseEntity<Booking> response =
                this.restTemplate.postForEntity(BASE_URL, booking, Booking.class);

        assertThat(response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<Booking> getBookingResponseEntity(String bookingDate) {
        Pet spot = this.petRepository.save(new Pet("Spot", 1));
        availabiltyRepository.save(new Availability(bookingDate, MAX));

        Booking booking = new Booking(bookingDate, spot);

        return this.restTemplate.postForEntity(BASE_URL, booking, Booking.class);
    }

    private String getUrl(Booking booking) {
        return BASE_URL + booking.getId();
    }

}
