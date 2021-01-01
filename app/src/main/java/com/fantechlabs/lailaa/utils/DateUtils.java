package com.fantechlabs.lailaa.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import lombok.NonNull;
import lombok.val;

//*******************************************
public class DateUtils
//*******************************************
{

    public static final String LOGS_DATE_FORMAT = "MM-dd-yyyy hh:mm:ss";


    //*******************************************
    public static String getFormattedDeliveryDate(String deliveryDate)
    //*******************************************
    {
        val date = com.fantechlabs.lailaa.utils.DateUtils.getDateFromString(deliveryDate, "yyyy-MM-dd");
        return getDate(date.getTime(), "MM-dd-yyyy");
    }


    //*****************************************************************
    public static Date getDateFromString(@NonNull String dateString, @NonNull String formatter)
    //*****************************************************************
    {
        String dtStart = dateString;
        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.US);
        try {
            Date date = format.parse(dtStart);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //*****************************************************************
    public static Date addOneDayToDate(@NonNull String dateString, @NonNull String formatter)
    //*****************************************************************
    {
        String dtStart = dateString;
        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.US);
        try {
            Date date = format.parse(dtStart);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            date = c.getTime();
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //*****************************************************************
    public static Date getDateFromUTC(String dateTime, String format)
    //*****************************************************************
    {

        String dateToReturn = dateTime;

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date gmt = null;

        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat(format);
        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        try {

            gmt = sdf.parse(dateTime);
            dateToReturn = sdfOutPutToSend.format(gmt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gmt;

    }

    //*************************************************************
    public static String getCurrentDate(String format)
    //*************************************************************
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    //*************************************************************
    public static String getCurrentTime(String format)
    //*************************************************************
    {
        String currentTime = new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
        return currentTime;
    }

    //*************************************************************
    public static String getDate(long timestamp, String format)
    //*************************************************************
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format(format, cal)
                .toString();
        return date;
    }

    //*****************************************************************
    public static long getDateAndTime(String stringDate)
    //*****************************************************************
    {
        val dateTIme = stringDate.split(" ");
        val date = dateTIme[0].split("-");
        val time = dateTIme[1].split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, (Integer.parseInt(time[0]) - 1));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(time[2]));


        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));


        return calendar.getTimeInMillis();

    }

    //*****************************************************************
    public static final Date getDateFromStringFormat(String dtStart, String format)
    //*****************************************************************
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(dtStart);
            System.out.println(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    //***********************************************************
    public static long getNumberOfDays(String startDate, String endDate)
    //***********************************************************
    {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            Date date1 = myFormat.parse(startDate);
            Date date2 = myFormat.parse(endDate);
            long diff = date2.getTime() - date1.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
