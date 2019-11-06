/*
 * CoeusDateFormat.java
 *
 * Created on August 28, 2002, 8:53 PM
 */

package edu.mit.coeus.utils;

import java.text.SimpleDateFormat;

/**
 * This class is used to construct the Date format like DD-MMM-YYYY HH:MM,
 * Custom string.
 *
 * @author  geo
 * @version 1.0
 */
public class CoeusDateFormat {
    
    /**
     * This method will Date format like DD-MMM-YYYY HH:MM with date String.
     * @param dateStr date string to be parsed.
     * @return String date value
     */
    public static String format(String dateStr) {
        //dd-MMM-yyyyy hh:mm a
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        return (dateStr!=null?dateFormat.format(java.sql.Timestamp.valueOf(dateStr)):null);
        
    }   
    
    /**
     * This method will Date format like custom date String.
     * @param dateStr date string to be parsed.
     * @param formatStr represent the format type.
     * @return String date value
     */
    public static String format(String dateStr, String formatStr) {
        if(dateStr==null || formatStr==null)
            return null;
        else{
            //format as per the param formatStr
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
            return dateFormat.format(java.sql.Timestamp.valueOf(dateStr));
        }

    }
}
