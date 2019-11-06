/*
 * DateUtils.java
 *
 * Created on November 10, 2004, 8:47 AM
 */

package edu.mit.coeuslite.utils ;

/**
 *
 * @author  sharathk
 */

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.text.ParseException;

public class DateUtils extends edu.mit.coeus.utils.DateUtils{
    
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    
    /** Creates a new instance of DateUtils */
    public DateUtils() {
    }
    
    public Date getSQLDate(String strDate, String dateFormat)throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        java.util.Date date = simpleDateFormat.parse(strDate);
        
        Date sqlDate = new Date(date.getTime());
        
        return sqlDate;
    }
    
    public Date getSQLDate(String strDate)throws ParseException {
        return getSQLDate(strDate, MM_DD_YYYY);
    }
    
    
    public static void main(String s[]) {
        DateUtils du = new DateUtils();
        String strDate = du.formatDate(new java.sql.Timestamp(999999999).toString(), MM_DD_YYYY);
        System.out.println(strDate);
    }
    
}
