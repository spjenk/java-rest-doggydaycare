package com.litereaction.pawspassport.util;

import java.time.LocalDate;

public class ModelUtil {

    public static String getAvailabilityId(LocalDate date, long tenantId) {
        String month = date.getMonthValue() <= 9 ? "0" + date.getMonthValue() : "" + date.getMonthValue();
        String day = date.getDayOfMonth() <= 9 ? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();

        return date.getYear() + month + day + tenantId;
    }
}
