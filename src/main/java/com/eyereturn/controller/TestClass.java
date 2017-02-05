package com.eyereturn.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chemcee. M. C on 03-02-2017.
 */
public class TestClass {

    public static void main(String args[]) throws ParseException
    {
        /*System.out.println("hello world");
        String[] dates = getDate("coming from the 25-13-2009 to the 30/03/2016");

        System.out.println(dates[0]);
        System.out.println(dates[1]);*/

        convertToDate("25/03/1990");
        convertToDate("19.05.1990");
        convertToDate("30-02-2016");
    }

    private static String[] getDate(String desc) {
        int count=0;
        String[] allMatches = new String[2];
        Matcher m = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d").matcher(desc);
        while (m.find()) {
            allMatches[count] = m.group();
            count++;
        }
        return allMatches;
    }

    public static Date convertToDate(String date) throws ParseException
    {
        date = date.replaceAll("[./]","-");
        //regex to check the date is in dd/MM/yyyy format
        String datePattern = "(0[1-9]|[12][0-9]|3[01])[-](0[1-9]|1[012])[-](19|20)\\d\\d";
        Matcher match = Pattern.compile(datePattern).matcher(date);
        if(match.matches())
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date javaDate = dateFormat.parse(date);
            Date finalDate = new Date(javaDate.getTime());
            System.out.println("final Date "+ finalDate);
            return finalDate;
        } else {
            System.out.println("no match");
            return null;
        }
    }
}
