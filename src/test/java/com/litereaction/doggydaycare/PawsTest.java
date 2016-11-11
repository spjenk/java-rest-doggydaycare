package com.litereaction.doggydaycare;

import com.litereaction.doggydaycare.Model.Paws;
import org.junit.Assert;
import org.junit.Test;

public class PawsTest {

    private static final long DEFAULT_ID = 0;
    private static final String DEFAULT_NAME = "Jack";
    private static final int DEFAULT_AGE = 1;
    private static final long DEFAULT_CAREGIVER_ID = 0;

    @Test
    public void createPawsFromConstructorTest() {

        //when
        Paws paws = new Paws(DEFAULT_NAME, DEFAULT_AGE);

        //then
        Assert.assertEquals(DEFAULT_ID, 0);
        Assert.assertEquals(DEFAULT_NAME, paws.getName());
        Assert.assertEquals(DEFAULT_AGE, paws.getAge());
        Assert.assertEquals(DEFAULT_CAREGIVER_ID, 0);
    }

    @Test
    public void createPawsUsingSettersTest() {

        //given
        long id = 1;
        long caregiver_id = 2;

        //when
        Paws paws = new Paws();
        paws.setId(id);
        paws.setName(DEFAULT_NAME);
        paws.setAge(DEFAULT_AGE);
        paws.setCaregiverId(caregiver_id);

        //then
        Assert.assertEquals(id, paws.getId());
        Assert.assertEquals(DEFAULT_NAME, paws.getName());
        Assert.assertEquals(DEFAULT_AGE, paws.getAge());
        Assert.assertEquals(caregiver_id, paws.getCaregiverId());
    }
}
