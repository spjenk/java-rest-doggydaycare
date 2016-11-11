package com.litereaction.doggydaycare;

import com.litereaction.doggydaycare.Model.Caregiver;
import com.litereaction.doggydaycare.Model.Paws;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CaregiverTest {

    private static final long DEFAULT_ID = 0;
    private static final String DEFAULT_NAME = "Jill";
    private static final int DEFAULT_AGE = 1;


    @Test
    public void createCaregiverFromConstructor() {

        //when
        Caregiver caregiver = new Caregiver(DEFAULT_NAME);

        //then
        Assert.assertEquals(DEFAULT_ID, caregiver.getId());
        Assert.assertEquals(DEFAULT_NAME, caregiver.getName());
    }

    @Test
    public void createCaregiverUsingSetters() {

        //given
        long id = 1;

        //when
        Caregiver caregiver = new Caregiver();
        caregiver.setId(id);
        caregiver.setName(DEFAULT_NAME);

        //then
        Assert.assertEquals(id, caregiver.getId());
        Assert.assertEquals(DEFAULT_NAME, caregiver.getName());
    }

    @Test
    public void createCaregiverWithPetsUsingSetters() {

        //given
        Caregiver caregiver = new Caregiver(DEFAULT_NAME);
        String jack = "Jack";
        int age = 1;
        Paws paw = new Paws(jack, age);
        List<Paws> paws = new ArrayList<Paws>();
        paws.add(paw);

        //when
        caregiver.setPets(paws);

        //then
        Assert.assertEquals(jack, caregiver.getPets().get(0).getName());
        Assert.assertEquals(age, caregiver.getPets().get(0).getAge());
    }
}
