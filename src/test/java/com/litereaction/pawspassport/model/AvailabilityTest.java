package com.litereaction.pawspassport.model;

import com.litereaction.pawspassport.util.ModelUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class AvailabilityTest {

    private Tenant tenant = new Tenant("PnR");

    @Test
    public void createAvailabilityFromConstructorTest() {

        //when
        LocalDate date = LocalDate.of(1999,12,31);
        int max = 5;
        tenant.setId(1);
        Availability availability = new Availability(1999,12,31, max, tenant);

        //then
        Assert.assertEquals(ModelUtil.getAvailabilityId(date, tenant.getId()), availability.getId());
        Assert.assertEquals(date, availability.getBookingDate());
        Assert.assertEquals(max, availability.getMax());
        Assert.assertEquals(max, availability.getAvailable());
    }

    @Test
    public void createAvailabilityUsingSettersTest() {

        LocalDate date = LocalDate.of(1999,12,31);
        int max = 5;
        int available = 5;
        tenant.setId(1);

        //when
        Availability availability = new Availability();
        availability.setId(ModelUtil.getAvailabilityId(date, tenant.getId()));
        availability.setBookingDate(1999,12,31);
        availability.setMax(max);
        availability.setAvailable(available);

        //then
        Assert.assertEquals(ModelUtil.getAvailabilityId(date, tenant.getId()), availability.getId());
        Assert.assertEquals(date, availability.getBookingDate());
        Assert.assertEquals(max, availability.getMax());
        Assert.assertEquals(available, availability.getAvailable());
    }
}
