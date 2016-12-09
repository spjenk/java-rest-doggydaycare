package com.litereaction.doggydaycare.model;

import org.junit.Assert;
import org.junit.Test;

public class OwnerTest {

    private static final long DEFAULT_ID = 0;
    private static final String DEFAULT_NAME = "Jill";
    private static final String DEFAULT_EMAIL = "first.last@abc.com";
    private Tenant tenant = new Tenant("PnR");

    @Test
    public void createCaregiverFromConstructorTest() {

        //when
        Owner owner = new Owner(DEFAULT_NAME, DEFAULT_EMAIL, tenant);

        //then
        Assert.assertEquals(DEFAULT_ID, owner.getId());
        Assert.assertEquals(DEFAULT_NAME, owner.getName());
        Assert.assertEquals(DEFAULT_NAME, owner.getDisplayName());
        Assert.assertEquals(DEFAULT_EMAIL, owner.getEmail());
    }

    @Test
    public void createCaregiverUsingSettersTest() {

        //given
        long id = 1;
        Owner owner = new Owner();

        //when
        owner.setId(id);
        owner.setName(DEFAULT_NAME);
        owner.setEmail(DEFAULT_EMAIL);

        //then
        Assert.assertEquals(id, owner.getId());
        Assert.assertEquals(DEFAULT_NAME, owner.getName());
        Assert.assertEquals(DEFAULT_NAME, owner.getDisplayName());
        Assert.assertEquals(DEFAULT_EMAIL, owner.getEmail());
    }

    @Test
    public void setDisplayNameTest() {

        //given
        Owner owner = new Owner(DEFAULT_NAME, DEFAULT_EMAIL, tenant);
        String displayName = "Jack";

        //when
        owner.setDisplayName(displayName);

        //then
        Assert.assertEquals(DEFAULT_ID, owner.getId());
        Assert.assertEquals(DEFAULT_NAME, owner.getName());
        Assert.assertEquals(displayName, owner.getDisplayName());
        Assert.assertEquals(DEFAULT_EMAIL, owner.getEmail());
    }
}
