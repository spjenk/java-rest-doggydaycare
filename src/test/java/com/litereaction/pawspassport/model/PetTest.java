package com.litereaction.pawspassport.model;

import com.litereaction.pawspassport.types.Status;
import org.junit.Assert;
import org.junit.Test;

public class PetTest {

    private static final long DEFAULT_ID = 0;
    private static final String DEFAULT_NAME = "Jack";
    private static final String DEFAULT_DOB = "20110303";
    private static final long DEFAULT_CAREGIVER_ID = 0;

    Owner owner = new Owner("name", "owner@email.com", new Tenant("PnR"));

    @Test
    public void createPawsFromConstructorTest() {

        //when
        Pet pet = new Pet(DEFAULT_NAME, DEFAULT_DOB, owner);

        //then
        Assert.assertEquals(DEFAULT_ID, pet.getId());
        Assert.assertEquals(DEFAULT_NAME, pet.getName());
        Assert.assertEquals(DEFAULT_DOB, pet.getBirthDate());
        Assert.assertEquals(Status.ACTIVE, pet.getStatus());
        Assert.assertEquals(DEFAULT_CAREGIVER_ID, 0);
    }

    @Test
    public void createPawsUsingSettersTest() {

        //given
        long id = 1;
        Owner owner = new Owner();
        owner.setName("Jack");

        //when
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(DEFAULT_NAME);
        pet.setAge(DEFAULT_DOB);
        pet.setOwner(owner);

        //then
        Assert.assertEquals(id, pet.getId());
        Assert.assertEquals(DEFAULT_NAME, pet.getName());
        Assert.assertEquals(DEFAULT_DOB, pet.getBirthDate());
        Assert.assertEquals(pet.getOwner().getName(), owner.getName());
    }
}
