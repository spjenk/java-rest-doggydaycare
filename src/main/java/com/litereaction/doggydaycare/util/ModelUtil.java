package com.litereaction.doggydaycare.util;

import java.time.LocalDate;

public class ModelUtil {

    public static String getAvailabilityId(LocalDate date, long tenantId) {
        return "" + date.getYear() + date.getMonthValue() + date.getDayOfMonth() + tenantId;
    }
}
