package com.litereaction.doggydaycare.model;

import org.junit.Assert;
import org.junit.Test;

public class AvailabilityTest {

    @Test
    public void createAvailabilityFromConstructorTest() {

        //when
        String date = "19991231";
        int max = 5;
        Availability availability = new Availability(date, max);

        //then
        Assert.assertEquals(date, availability.getDate());
        Assert.assertEquals(max, availability.getMax());
        Assert.assertEquals(max, availability.getAvailable());
    }

    @Test
    public void createAvailabilityUsingSettersTest() {

        String date = "19991231";
        int max = 5;
        int available = 5;

        //when
        Availability availability = new Availability();
        availability.setDate(date);
        availability.setMax(max);
        availability.setAvailable(available);

        //then
        Assert.assertEquals(date, availability.getDate());
        Assert.assertEquals(max, availability.getMax());
        Assert.assertEquals(available, availability.getAvailable());
    }
}
