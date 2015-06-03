package com.unitedforhealth_softwaredevelopment.testdbapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Serge on 13/05/2015.
 */
public class StringUtil {
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isEmpty(String s) {

        return (s == null || s.trim().length() == 0);
    }

    public static boolean isNotEmpty(String s) {

        return (s != null && s.trim().length() > 0);
    }
}
