package com.litereaction.pawspassport.model;

import com.litereaction.pawspassport.helper.PetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class BookingTest {

    private Tenant tenant = new Tenant("PnR");
    private Owner owner = new Owner("name", "owner@email.com", tenant);

    @Test
    public void createBookingFromConstructorTest() {

        //when
        LocalDate date = LocalDate.of(1999,12,31);
        Pet pet = PetUtil.getRandomPet(owner);
        Booking booking = new Booking(new Availability(1999, 12, 31, 5, tenant), pet);

        //then
        Assert.assertEquals(0, booking.getId());
        Assert.assertEquals(date, booking.getAvailability().getBookingDate());
        Assert.assertEquals(pet.getName(), booking.getPet().getName());
    }

    @Test
    public void createPawsUsingSettersTest() {

        //when
        Booking booking = new Booking();
        booking.setId(1);
        booking.setAvailability(new Availability(1999, 12, 31, 5, tenant));
        Pet pet = PetUtil.getRandomPet(owner);
        booking.setPet(pet);

        //then
        Assert.assertEquals(1, booking.getId());
        Assert.assertEquals(5, booking.getAvailability().getMax());
        Assert.assertEquals(pet.getName(), booking.getPet().getName());
    }
}
