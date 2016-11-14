package com.litereaction.doggydaycare.model;

import com.litereaction.doggydaycare.Model.Owner;
import com.litereaction.doggydaycare.Model.Pet;
import org.junit.Assert;
import org.junit.Test;

public class PetTest {

    private static final long DEFAULT_ID = 0;
    private static final String DEFAULT_NAME = "Jack";
    private static final int DEFAULT_AGE = 1;
    private static final long DEFAULT_CAREGIVER_ID = 0;

    @Test
    public void createPawsFromConstructorTest() {

        //when
        Pet pet = new Pet(DEFAULT_NAME, DEFAULT_AGE);

        //then
        Assert.assertEquals(DEFAULT_ID, 0);
        Assert.assertEquals(DEFAULT_NAME, pet.getName());
        Assert.assertEquals(DEFAULT_AGE, pet.getAge());
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
        pet.setAge(DEFAULT_AGE);
        pet.setOwner(owner);

        //then
        Assert.assertEquals(id, pet.getId());
        Assert.assertEquals(DEFAULT_NAME, pet.getName());
        Assert.assertEquals(DEFAULT_AGE, pet.getAge());
        Assert.assertEquals(pet.getOwner().getName(), owner.getName());
    }
}
