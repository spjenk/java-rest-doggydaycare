package com.litereaction.pawspassport.controller;

import com.litereaction.pawspassport.helper.OwnerUtil;
import com.litereaction.pawspassport.helper.PetUtil;
import com.litereaction.pawspassport.model.*;
import com.litereaction.pawspassport.repository.*;
import com.litereaction.pawspassport.util.ModelUtil;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookingControllerTest {

    private static final int MAX = 5;
    private static final String BASE_URL = "/bookings/";
    private static final int BOOKING_YEAR = 1999;
    private static final int BOOKING_MONTH = 12;
    private static final int BOOKING_DAY = 31;
    private LocalDate BOOKING_DATE = LocalDate.of(BOOKING_YEAR, BOOKING_MONTH, BOOKING_DAY);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AvailabilityRepository availabiltyRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private Tenant tenant;
    private Owner owner;
    private Pet spot;
    private Availability availability;

    @Before
    public void setup() {
        tenant = this.tenantRepository.save(new Tenant("PnR"));
        owner = this.ownerRepository.save(OwnerUtil.getRandomOwner(tenant));
        spot = this.petRepository.save(PetUtil.getRandomPet(owner));
        availability = availabiltyRepository.save(new Availability(BOOKING_YEAR, BOOKING_MONTH, BOOKING_DAY, MAX, tenant));
    }

    @Test
    public void createBookingTest() throws Exception {

        Booking booking = new Booking(availability, spot);
        ResponseEntity<Booking> response = this.restTemplate.postForEntity(BASE_URL, booking, Booking.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertNotNull(response.getBody());

        Booking bookingResult = response.getBody();
        assertThat(bookingResult.getAvailability().getId(), equalTo(ModelUtil.getAvailabilityId(BOOKING_DATE, tenant.getId())));
        assertThat(bookingResult.getPet().getId(), equalTo(spot.getId()));
        assertThat(bookingResult.getPet().getName(), equalTo(spot.getName()));

        availability = availabiltyRepository.findOne(ModelUtil.getAvailabilityId(BOOKING_DATE, tenant.getId()));
        assertThat(availability.getAvailable(), equalTo(4));
    }

    @Test
    public void createBookingBadRequestTest() throws Exception {

        ResponseEntity<Booking> response = this.restTemplate.postForEntity(BASE_URL, new Booking(), Booking.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void deleteBookingTest() throws Exception {

        Booking booking = new Booking(availability, spot);
        bookingRepository.save(booking);

        this.restTemplate.delete(getUrl(booking));

        ResponseEntity<String> response = this.restTemplate.getForEntity(getUrl(booking), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deleteBookingEnsureAvailabilityUpdatesTest() throws Exception {

        ResponseEntity<Booking> response =
                this.restTemplate.postForEntity(BASE_URL, new Booking(availability, spot), Booking.class);
        Booking booking = response.getBody();

        availability = availabiltyRepository.findOne(ModelUtil.getAvailabilityId(BOOKING_DATE, tenant.getId()));
        assertThat(availability.getAvailable(), equalTo(MAX-1));

        this.restTemplate.delete(getUrl(booking));

        availability = availabiltyRepository.findOne(ModelUtil.getAvailabilityId(BOOKING_DATE, tenant.getId()));
        assertThat(availability.getAvailable(), equalTo(MAX));
    }

    @Test
    public void createBookingNoAvailabilityTest() throws Exception {

        availability.setAvailable(0);
        availabiltyRepository.save(availability);

        ResponseEntity<Booking> response =
                this.restTemplate.postForEntity(BASE_URL, new Booking(availability, spot), Booking.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getBookingById() throws Exception {

        Booking booking = this.bookingRepository.save(new Booking(availability, spot));

        ResponseEntity<Booking> response = this.restTemplate.getForEntity(getUrl(booking), Booking.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        assertNotNull(response.getBody());

        Booking bookingResult = response.getBody();
        assertThat(bookingResult.getAvailability().getId(), equalTo(ModelUtil.getAvailabilityId(BOOKING_DATE, tenant.getId())));
        assertThat(bookingResult.getPet().getId(), equalTo(spot.getId()));
        assertThat(bookingResult.getPet().getName(), equalTo(spot.getName()));
    }


    private String getUrl(Booking booking) {
        return BASE_URL + booking.getId();
    }

}
