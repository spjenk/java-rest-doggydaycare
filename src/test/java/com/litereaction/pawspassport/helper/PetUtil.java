package com.litereaction.pawspassport.helper;

import com.litereaction.pawspassport.model.Owner;
import com.litereaction.pawspassport.model.Pet;

import java.util.Random;
import java.util.UUID;

public class PetUtil {

    public static Pet getRandomPet(Owner owner) {
        String name = UUID.randomUUID().toString().replaceAll("-", "");

        Random random = new Random();
        int year = random.nextInt(99);
        int month = random.nextInt(12);
        int day = random.nextInt(27);

        return new Pet(name, "19"+year+month+day, owner);
    }
}
