package com.litereaction.pawspassport.helper;

import com.litereaction.pawspassport.model.Owner;
import com.litereaction.pawspassport.model.Tenant;

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
