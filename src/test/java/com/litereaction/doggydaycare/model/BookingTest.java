package com.litereaction.doggydaycare.model;

import com.litereaction.doggydaycare.util.ModelUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class BookingTest {

    @Test
    public void createBookingFromConstructorTest() {

        //when
        String name = "Spot";
        LocalDate date = LocalDate.of(1999,12,31);
        Booking booking = new Booking(new Availability(1999, 12, 31, 5), new Pet(name, 1));

        //then
        Assert.assertEquals(0, booking.getId());
        Assert.assertEquals(date, booking.getAvailability().getBookingDate());
        Assert.assertEquals(name, booking.getPet().getName());
    }

    @Test
    public void createPawsUsingSettersTest() {

        //when
        Booking booking = new Booking();
        booking.setId(1);
        booking.setAvailability(new Availability(1999, 12, 31, 5));
        booking.setPet(new Pet("Spot", 1));

        //then
        Assert.assertEquals(1, booking.getId());
        Assert.assertEquals(5, booking.getAvailability().getMax());
        Assert.assertEquals("Spot", booking.getPet().getName());
    }
}
