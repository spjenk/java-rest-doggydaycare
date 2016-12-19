package com.litereaction.pawspassport.util;

import java.time.LocalDate;

public class ModelUtil {

    public static String getAvailabilityId(LocalDate date, long tenantId) {
        return "" + date.getYear() + date.getMonthValue() + date.getDayOfMonth() + tenantId;
    }
}
