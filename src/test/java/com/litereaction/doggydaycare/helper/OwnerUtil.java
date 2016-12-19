package com.litereaction.doggydaycare.helper;

import com.litereaction.doggydaycare.model.Owner;
import com.litereaction.doggydaycare.model.Tenant;
import com.litereaction.doggydaycare.repository.OwnerRepository;

import java.util.UUID;

public class OwnerUtil {

    public static String getRandomEmail() {
        String email = UUID.randomUUID().toString();
        email = email.replaceFirst("-", "@");
        email += ".com";
        return email;
    }

    public static Owner getRandomOwner(Tenant tenant) {
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        return new Owner(name, getRandomEmail(), tenant);
    }

}
