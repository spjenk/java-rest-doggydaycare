package com.litereaction.doggydaycare.controller;

import com.litereaction.doggydaycare.model.*;
import com.litereaction.doggydaycare.repository.*;
import com.litereaction.doggydaycare.util.ModelUtil;
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
    private static final String BOOKING_URL = "/bookings?year=" + BOOKING_YEAR + "&month=" + BOOKING_MONTH + "&day=" + BOOKING_DAY;
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

    @Before
    public void setup() {
        this.bookingRepository.deleteAllInBatch();
        this.availabiltyRepository.deleteAllInBatch();
        this.petRepository.deleteAllInBatch();
        this.ownerRepository.deleteAllInBatch();
        this.tenantRepository.deleteAllInBatch();
    }

    @After
    public void teardown() {
        this.bookingRepository.deleteAllInBatch();
        this.availabiltyRepository.deleteAllInBatch();
        this.petRepository.deleteAllInBatch();
        this.ownerRepository.deleteAllInBatch();
        this.tenantRepository.deleteAllInBatch();
    }

    @Test
    public void createBookingTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner owner = this.ownerRepository.save(new Owner("Owner Name", "Owner@email.com", tenant));

        Pet spot = this.petRepository.save(new Pet("Spot", 1, owner));

        Availability availability = availabiltyRepository.save(new Availability(BOOKING_YEAR, BOOKING_MONTH, BOOKING_DAY, MAX, tenant));

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

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner owner = this.ownerRepository.save(new Owner("Owner Name", "Owner@email.com", tenant));

        Pet spot = this.petRepository.save(new Pet("Spot", 1, owner));
        Availability availability = availabiltyRepository.save(new Availability(BOOKING_YEAR, BOOKING_MONTH, BOOKING_DAY, MAX, tenant));

        Booking booking = new Booking(availability, spot);
        bookingRepository.save(booking);

        this.restTemplate.delete(getUrl(booking));

        ResponseEntity<String> response = this.restTemplate.getForEntity(getUrl(booking), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deleteBookingEnsureAvailabilityUpdatesTest() throws Exception {

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner owner = this.ownerRepository.save(new Owner("Owner Name", "Owner@email.com", tenant));

        Pet spot = this.petRepository.save(new Pet("Spot", 1, owner));
        Availability availability = availabiltyRepository.save(new Availability(BOOKING_YEAR, BOOKING_MONTH, BOOKING_DAY, MAX, tenant));

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

        Tenant tenant = this.tenantRepository.save(new Tenant("PnR"));
        Owner owner = this.ownerRepository.save(new Owner("Owner Name", "Owner@email.com", tenant));

        Pet spot = this.petRepository.save(new Pet("Spot", 1, owner));
        Availability availability = availabiltyRepository.save(new Availability(BOOKING_YEAR, BOOKING_MONTH, BOOKING_DAY, 0, tenant));

        ResponseEntity<Booking> response =
                this.restTemplate.postForEntity(BASE_URL, new Booking(availability, spot), Booking.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    private String getUrl(Booking booking) {
        return BASE_URL + booking.getId();
    }

}
