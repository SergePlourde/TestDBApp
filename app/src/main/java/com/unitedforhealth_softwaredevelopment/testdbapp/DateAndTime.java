package com.unitedforhealth_softwaredevelopment.testdbapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Serge on 21/04/2015.
 */
public class DateAndTime {
    public static final DateFormat sqLiteDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sqLiteDateStampSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * @return String of today, like 2015-01-31
     */
    public static String today() {
        return new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()).substring(0, 10);
    }

    public static String now() {
        return new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()).substring(11);
    }

    public static String calculatedDate(String dateString, int daysBackOrForth) {
        StringBuilder sb = new StringBuilder();

        if (dateString != null && dateString.length() > 0) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                calendar.setTime(dateFormat.parse(dateString));
                calendar.add(Calendar.DATE, daysBackOrForth);
                date = calendar.getTime();
                sb.append(new SimpleDateFormat("yyy-MM-dd").format(date));
            } catch (ParseException e) {
                //
            }
        }

        return sb.toString();
    }


    /**
     * @param timeString a string from 0 4 (as we don't expect users to enter the seconds part of the hhmmss) digits intended to represent hours, minutes, seconds
     * @return a normalized time string in the form of HH:MM:SS. It fills a char array with timeString, from right to left, and the remaining positions are defaulted to '0'.
     * <p/>
     * null,       00:00:00
     * "",         00:00:00
     * "1",        00:01:00
     * "12",       00:12:00
     * "123",      01:23:00
     * "1234",     12:34:00
     * "12345",    12:34:00
     * "123456"    12:34:00
     * <p/>
     * Warning: no validation!
     */
    public static String normalizeTimeForHMInput(String timeString) {
        /*

         */
        if (timeString.indexOf(":") > -1) {
            return timeString;
        }
        if (timeString == null) {
            timeString = "";
        } else {
            if (timeString.length() > 4) {
                timeString = timeString.substring(0, 4);
            }
        }

        char[] hhmmss = {'0', '0', ':', '0', '0'};
        char[] givenTime = (timeString == null ? "" : timeString).toCharArray();
        if (givenTime.length > 0) {
            int rightmostGivenTimeCharToUse = givenTime.length - 1;
            int rightmostHhmmssToFill = 4;
            char currentCharToFill;
            for (int i = rightmostHhmmssToFill; i > -1 && rightmostGivenTimeCharToUse > -1; i--) {
                if (i == 2) {
                    i--;
                }
                currentCharToFill = givenTime[rightmostGivenTimeCharToUse--];
                hhmmss[i] = currentCharToFill;
            }
        }
        if (timeString.indexOf(':') > -1) {
            return timeString;
        }
        return new StringBuilder().append(hhmmss).append(":00").toString();
    }

    public static String getDateFromDateTimeStamp(String dateTimeStamp) {
        StringBuilder sb = new StringBuilder("");
        if (dateTimeStamp != null && dateTimeStamp.trim().length() > 0) {
            sb.append(dateTimeStamp.substring(0, 10));
        }

        return sb.toString();
    }

    /**
     * @param dateTimeStamp SQLite date & time string format like 2015-12-31 23:35:59
     * @return
     */
    public static String getTimeFromDateTimeStamp(String dateTimeStamp) {
        StringBuilder sb = new StringBuilder("");
        if (dateTimeStamp != null && dateTimeStamp.trim().length() > 0) {
            sb.append(dateTimeStamp.substring(11));
        }

        return sb.toString();
    }

    public static String getFormattedDateString(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);

    }

    public static String getDisplayDate(String sqliteDateStamp) {
        StringBuilder sb = new StringBuilder();
        if (sqliteDateStamp != null && sqliteDateStamp.length() > 0) {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            SimpleDateFormat sqliteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                sb.append(displayDateFormat.format(sqliteFormat.parse(sqliteDateStamp)));
            } catch (ParseException e) {
                //
            }
        }
        return sb.toString();
    }

    /**
     * @param dateString
     * @param formatString
     * @return String like: Monday, April 27 2015
     */
    public static String getDisplayDateFromFormat(String dateString, String formatString) {
        StringBuilder sb = new StringBuilder();
        if (dateString != null && dateString.length() > 0) {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("EEEE, MMMM d yyyy");
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
            try {
                sb.append(displayDateFormat.format(dateFormat.parse(dateString)));
            } catch (ParseException e) {
                //
            }
        }
        return sb.toString();
    }

    /**
     * SQLite date format is like 2015-12-31.
     *
     * @param dateString to check if it fits exactly the SQLite date string, and it is valid date.
     * @return stringOfDateGeneratedFromParameter.equals(dateStringParameter)
     */
    public static boolean isSqLiteDateString(String dateString) {
        Date date = null;
        String dateCompare = "";
        if (StringUtil.isEmpty(dateString)) return false;
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sqLiteDateFormat.parse(dateString));
            date = calendar.getTime();
            dateCompare = sqLiteDateFormat.format(date);
        } catch (ParseException e) {
            //
        }
        return dateCompare.equals(dateString);
    }

    public static boolean isSqLiteDateStampString(String dateString, String timeString) {
        Date date = null;
        StringBuilder dateStampOriginal = new StringBuilder().append(dateString).append(" ").append(timeString);
        String dateCompare = "";


        if (dateStampOriginal.length() == 0) return false;

        Calendar calendar = Calendar.getInstance();
        try {
            date = sqLiteDateStampSimpleDateFormat.parse(dateStampOriginal.toString());
            dateCompare = sqLiteDateStampSimpleDateFormat.format(date);
            System.out.println("Resulting date for " + dateStampOriginal + " is: " + dateCompare);

        } catch (ParseException e) {
            return false;
        }
        return dateCompare.equals(dateStampOriginal.toString());
    }

    public static String makeSqLiteDateStampString(String dateString, String timeString) {
        Date date = null;
        String timeStringTampered = (timeString.length() == 4 ? "0" + timeString : timeString);
        StringBuilder dateStampOriginal = new StringBuilder().append(dateString).append(" ").append(timeStringTampered);
        String dateCompare = "";
        String sqLiteDateStampString = null;

        if (dateStampOriginal.length() == 0) return sqLiteDateStampString;

        Calendar calendar = Calendar.getInstance();
        try {
            date = sqLiteDateStampSimpleDateFormat.parse(dateStampOriginal.toString());
            dateCompare = sqLiteDateStampSimpleDateFormat.format(date);
            System.out.println("Resulting date for " + dateStampOriginal + " is: " + dateCompare);

        } catch (ParseException e) {
            return sqLiteDateStampString;
        }

        if (dateCompare.equals(dateStampOriginal.toString())) {
            sqLiteDateStampString = dateCompare;
        }
        return sqLiteDateStampString;
    }


}
