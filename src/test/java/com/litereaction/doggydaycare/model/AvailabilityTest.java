package com.litereaction.doggydaycare.model;

import com.litereaction.doggydaycare.util.ModelUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class AvailabilityTest {

    @Test
    public void createAvailabilityFromConstructorTest() {

        //when
        LocalDate date = LocalDate.of(1999,12,31);
        int max = 5;
        Availability availability = new Availability(1999,12,31, max);

        //then
        Assert.assertEquals(ModelUtil.getId(date), availability.getId());
        Assert.assertEquals(date, availability.getBookingDate());
        Assert.assertEquals(max, availability.getMax());
        Assert.assertEquals(max, availability.getAvailable());
    }

    @Test
    public void createAvailabilityUsingSettersTest() {

        LocalDate date = LocalDate.of(1999,12,31);
        int max = 5;
        int available = 5;

        //when
        Availability availability = new Availability();
        availability.setId(ModelUtil.getId(date));
        availability.setBookingDate(1999,12,31);
        availability.setMax(max);
        availability.setAvailable(available);

        //then
        Assert.assertEquals(ModelUtil.getId(date), availability.getId());
        Assert.assertEquals(date, availability.getBookingDate());
        Assert.assertEquals(max, availability.getMax());
        Assert.assertEquals(available, availability.getAvailable());
    }
}
