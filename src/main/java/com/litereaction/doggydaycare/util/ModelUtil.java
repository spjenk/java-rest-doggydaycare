package com.litereaction.doggydaycare.util;

import java.time.LocalDate;

public class ModelUtil {

    public static String getId(LocalDate date) {
        return "" + date.getYear() + date.getMonthValue() + date.getDayOfMonth();
    }
}
