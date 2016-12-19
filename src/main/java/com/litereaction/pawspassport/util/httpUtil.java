package com.litereaction.pawspassport.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class httpUtil {

    public static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
