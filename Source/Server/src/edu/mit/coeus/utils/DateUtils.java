package edu.mit.coeus.utils;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
/*
 * @(#)DateUtils.java 1.0	10,Sep.2002 10:59 PM
 */

import java.util.*;
import java.text.*;
/**
 * This is a utility class to handle the date operations including the validate and
 * formatting the date in desired ouptut.
 * @author  RaYaKu
 * @date Sep,10,2002 10:59 PM
 * @version 1.0
 * @since 1.0
 */
public class DateUtils{

    //COEUSQA-2476 - START -1
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //COEUSQA-2476 - END -1
    
    //COEUSQA-1477 Dates in Search Results - Start
    private static final String DATE_FORMAT_DELIMITER = "/";
    private static final String DATE_FORMAT_USER_DELIMITER = "-";
    //COEUSQA-1477 Dates in Search Results - End

    /**
     * This is method is used to give Months of Year as key/value ( Name/Number of
     * the month)
     * @return Hashtable collection of month values
     */
    public static Hashtable getMonths(){
        Hashtable MONTHS = new Hashtable();

        MONTHS.put("January", "01");
        MONTHS.put("February", "02");
        MONTHS.put("March", "03");
        MONTHS.put("April", "04");
        MONTHS.put("May", "05");
        MONTHS.put("June", "06");
        MONTHS.put("July", "07");
        MONTHS.put("August", "08");
        MONTHS.put("September", "09");
        MONTHS.put("October", "10");
        MONTHS.put("November", "11");
        MONTHS.put("December", "12");
        return MONTHS;
    }

    /** Creates a new instance of DateUtils*/
    public DateUtils() {
    }


        /*
        public boolean validate( String enteredDate, String dateSeparaters){

                                final String dateDigit="0123456789";
                                if( ( dateSeparaters == null ) || ( dateSeparaters.trim().length()<1) ){
                                        return false;
                                }

                                String dateChars = dateSeparaters+dateDigit;

                                if( ( enteredDate == null ) || ( enteredDate.trim().length() < 6 ) ){
                                        return false;
                                }

                                // Filter unnecessary date separators
                                for (int i=0; i<enteredDate.length(); i++){
                                         if (dateChars.indexOf( enteredDate.valueOf( enteredDate.charAt(i) ) )  ==  -1 ){
                                        return false;
                                                }// if ends
                                }// for ends

                                // As the separators are defined above, tokenize the dd,month and yyyy using String tokenizer and validate them
                                java.util.StringTokenizer tokenizer = new StringTokenizer( enteredDate,dateSeparaters);
                                int totalTokens = tokenizer.countTokens();
                                // usually the token count is 3 ( Month,day and year)
                                if( totalTokens != 3){
                                        return false;
                                }

                           // get all tokens
                           String monthToken=null;
                           int month = 0;
                           String dayToken=null;
                           int day=0;
                           String yearToken = null;
                           int year=0;
                           // take all tokens and parse them to into " int "
                           try{
                                           // assume the first token is Month
                                          monthToken = tokenizer.nextToken();
                                          month = Integer.parseInt(monthToken);

                                         // assume the first token is Day
                                         dayToken = tokenizer.nextToken();
                                         day= Integer.parseInt(dayToken);

                                    // assume the first token is Year
                                        yearToken = tokenizer.nextToken();
                                        year = Integer.parseInt(yearToken);

                                }catch(Exception formatEx){
                                        return false;
                                }



                                //Calendar calendar = Calendar.getInstance();
                           //calendar.set(year+1900,month,day);


                                // date particulars should not be less than 1
                                if( (month < 1) ||(month>12) || (day < 1 ) || (day>31) || (year < 1) ){
                                                return false;
                                }

                                // validate the number of days of months
                                if (	(month == 1) || (month== 3) || (month==5) || (month==7) ||
                                                        (month==8) || (month==10) || (month==12)	){

                                                        if (day > 31)
                                                                return false;
                                }

                                if (	(month == 4) || (month== 6) || (month==9) || (month==11) ) {
                                                        if (day > 30)
                                                                return false;
                                }
                                // check the 2nd month days in both leap year and non leap years.
                                if (month == 2)		{

                                        if (yearToken.endsWith("00")) {

                                                if ((year % 400) != 0) {

                                                        if (day > 28)
                                                                return false;
                                                }else{
                                                        if (day > 29)
                                                                return false;
                                                }
                                        }else{

                                                if ((year % 4) != 0) {
                                                        if (day > 28)
                                                        return false;
                                                }else{
                                                        if (day > 29)
                                                                return false;
                                                }
                                        }// else ends
                                }//if ends


        return true;
        }
         */


    /**
     *  validates the string representation of date.
     * @param enteredDate The String representation of date
     * @param dateSeparaters  The separaters used in enteredDate to classify the month,day and years.
     * @return boolean true If the enteredDate is valid.
     */
    public boolean validateDate( String enteredDate, String dateSeparaters){

        String formattedDate =  formatDate( enteredDate,dateSeparaters,"MM/dd/yyyy");

        if( formattedDate !=null ){
            return true;
        }
        return false;
    }

    /**
     * Converts the date in required format.
     * @param enteredDate String, The date that is to be formatted/conveted in required format
     * @param dateSeparaters  String,  separaters used in enteredDate to classify the day,month and year
     * @param requiredFormat	String, the ouput of this method is string representation of a date in this format .
     * @return String representation of the date that is formatted.
     *			  returns null if enteredDate or requiredFormat are not valid
     * @see  java.text.SimpleDateFormat for date and time pattern strings that are used in requiredFormat.
     */
    public String formatDate(String enteredDate, String dateSeparaters, String requiredFormat) {


        final String dateDigit="0123456789";
        if( ( dateSeparaters == null ) || ( dateSeparaters.trim().length()<1) ){
            return null;
        }

        if((  requiredFormat == null) || (requiredFormat.trim().length() == 0 ) ){
            requiredFormat = "MM/dd/yyyy";
        }

        String dateChars = dateSeparaters+dateDigit;

        if( ( enteredDate == null ) || ( enteredDate.trim().length() < 6 ) ){
            return null;
        }

        // Filter unnecessary date separators
        for (int i=0; i<enteredDate.length(); i++){
            if (dateChars.indexOf( enteredDate.valueOf( enteredDate.charAt(i) ) )  ==  -1 ){
                return null;
            }// if ends
        }// for ends

        // As the separators are defined above, tokenize the dd,month and yyyy using String tokenizer and validate them
        java.util.StringTokenizer tokenizer = new StringTokenizer( enteredDate,dateSeparaters);
        int totalTokens = tokenizer.countTokens();
        // usually the token count is 3 ( Month,day and year)
        if( totalTokens != 3){
            return null;
        }

        // get all tokens
        String monthToken=null;
        int month = 0;
        String dayToken=null;
        int day=0;
        String yearToken = null;
        int year=0;
        // take all tokens and parse them to into " int "
        try{
            // assume the first token is Month
            monthToken = tokenizer.nextToken();
            month = Integer.parseInt(monthToken);
            if(month > 12){
                return null;
            }

            // assume the second token is Day
            dayToken = tokenizer.nextToken();
            day= Integer.parseInt(dayToken);
            if( day <1 || day>31){
                return  null;
            }

            // assume the 3rd token is Year
            yearToken = tokenizer.nextToken();
            if( (yearToken.trim().length() <= 1) || (yearToken.trim().length() == 3) || (yearToken.trim().length() > 4) )  {
                return null ;
            }
            year = Integer.parseInt(yearToken);

            if(year < 100){
                //Commented By Sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -18
                //year = year+2000;
                //Added By Sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -18
                year = getYear(year);
                //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -18
            }


        }catch(Exception formatEx){
            return null;
        }
        //construct a string that can be understood by java.util.Date valueOf method
        String newString = year+"-"+month+"-"+day;
        GregorianCalendar cal = new GregorianCalendar();
        if (! cal.isLeapYear(year)){
            if (month==2 && day>28){
                cal=null;
                return null;
            }
        }

        String converted = null;
         //COEUSQA-2476 - START - 2
        //java.util.Date utilDate = java.sql.Date.valueOf(newString);
        java.util.Date utilDate;
        try{
            utilDate = dateFormat.parse(newString);
            //COEUSQA-2476 - END - 2
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(requiredFormat);        
            converted = simpleDateFormat.format(utilDate);
        }catch(Exception ex){
            return null;
        }
        return converted;

    }// end of validateDate method

    /**
     * Restores the user entered actual date.
     * @param oldDate String representation of a date (Ex. 01-Dec-2002 )
     * @param dateSeparaters String representation of separaters that classify the
     *  date into day, month and Year.Ex( '/', '-', ',' etc..)
     * @return a String representation of date in MM/DD/YYYY format
     */
    public String restoreDate(String oldDate, String dateSeparaters){


        String monthToken=null;
        int month = 0;
        String dayToken=null;
        String yearToken = null;
        boolean found = false;

        try{
            StringTokenizer tokenizer = new StringTokenizer( oldDate,dateSeparaters);
            int totalTokens = tokenizer.countTokens();
            // usually the token count is 3 ( Month,day and year)
            if( totalTokens != 3){
                return oldDate;
            }

            // take all tokens and parse them to into " int "
            dayToken = tokenizer.nextToken();
            monthToken = tokenizer.nextToken();
            yearToken = tokenizer.nextToken();
            // find out the month number
            Enumeration monthNames =  getMonths().keys();
            while( monthNames.hasMoreElements() ){
                String monthName = (String) monthNames.nextElement();
                if( monthName.startsWith(monthToken) ){
                    String monthNumber = (String) getMonths().get(monthName);
                    month = Integer.parseInt(monthNumber);
                    found = true;
                    break;
                }
            }
            if(!found){
                return oldDate;
            }
        }catch(Exception ex){
            return oldDate;
        }
        // format a string
        return ( month+"/"+dayToken+"/"+yearToken );

    }

    // Added By Sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -18
    //When a two Digit year is Entered. This method calculates the year to be
    //as four digit.
    private int getYear(int year) {

        java.util.Date now = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int yearNow = thisYear % 1000;

        //System.out.println("yearNow "+yearNow);
        if((yearNow >= 0 && yearNow < 50) && year >=50) {
            return (thisYear - (thisYear % 100)) - 100 + year;
        }
        else if(yearNow >= 50 && (year > 0 && year < 50)) {
            return (thisYear - (thisYear % 100)) + year;
        }
        else {
            return thisYear - (thisYear % 100) + year;
        }
    }
    //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -18

    /**
     * Converts timestamp string to required date format.
     * @param timeStr String The time stamp that is to be formatted/conveted in required format
     * @param formatStr	String, the ouput of this method is string representation of a date in this format .
     * @return String representation of the date that is formatted.
     *			  returns null if enteredDate or requiredFormat are not valid
     * @see  java.text.SimpleDateFormat for date and time pattern strings that are used in requiredFormat.
     */
    public String formatDate(String timeStr, String formatStr) {
        int year=0;
        int month=0;
        int day=0;
        if(timeStr==null || formatStr==null)
            return null;
        else{
            GregorianCalendar cal = new GregorianCalendar();
            year  = Integer.parseInt(timeStr.substring(0,4));
            month  = Integer.parseInt(timeStr.substring(5,7));
            day  = Integer.parseInt(timeStr.substring(8,10));
        }
        return formatDate(""+month+"/"+day+"/"+year, "/-:," , formatStr);
    }

    /**
     * Main method used to construct the the Date Utils.
     * @param args command line arguments
     */
    public static void main(String args[]){
        DateUtils test = new DateUtils();
        String[] ids=TimeZone.getAvailableIDs(0);
        for (int i = 0; i < ids.length; i++) {
            String string = ids[i];
            System.out.println(string);
        }
        
        try{
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        java.util.Date dt = null;
        String dateStr = test.formatDate("2001-12-12 00:00:00.0", "dd-MMM-yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        dt =dateFormat.parse(dateStr);
        System.out.println(formatCalendar(getCal(dt)));
        //test.test5();

        }catch(Exception e){
            e.printStackTrace();
        }
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        //DateUtils dt = new DateUtils();
        //System.out.println(dt.getCal(new Date()));
        //         TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        //         System.out.println(dt.getCurrentDateTime());
        //        System.out.println(dt.getYear(99));
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(((1440l-24l)*60l*60l*1000l));
//        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//        DateFormat f = DateFormat.getTimeInstance();
////        System.out.println(f.format(new Time((1440l*60l*60l*1000l))));
//        System.out.println(dt.formatCalendar(cal,"D hh:mm"));
    }


    /**
     *  validates the string representation of time.
     * @param enteredTime The String representation of Time
     * @return boolean true, If the enteredTime is valid.
     */
    public boolean validateTime( String enteredTime){

        String formattedTime =  formatTime( enteredTime);

        if( formattedTime !=null ){
            return true;
        }
        return false;
    }

    /**
     * Converts the Time in required format.
     * @param enteredTime String, The time that is to be formatted/conveted in required format
     * @return String representation of the time that is formatted.
     *			  returns null if enteredTime is not valid
     * @see  java.text.SimpleDateFormat for date and time pattern strings that are used in requiredFormat.
     */
    public String formatTime(String enteredTime) {

        String timeSeparaters = ":";
        final String timeDigit="0123456789";

        String timeChars = timeSeparaters+timeDigit;

        if( ( enteredTime == null ) || ( enteredTime.trim().length() < 4 ) ){
            return null;
        }

        // Filter unnecessary date separators
        for (int i=0; i<enteredTime.length(); i++){
            if (timeChars.indexOf( enteredTime.valueOf( enteredTime.charAt(i) ) )  ==  -1 ){
                return null;
            }// if ends
        }// for ends

        // As the separators are defined above, tokenize the dd,month and yyyy using String tokenizer and validate them
        java.util.StringTokenizer tokenizer = new StringTokenizer( enteredTime,timeSeparaters);
        int totalTokens = tokenizer.countTokens();
        // usually the token count is 3 ( Month,day and year)
        if( totalTokens != 2){
            return null;
        }

        // get all tokens
        String hourToken=null;
        int hour = 0;
        String minutesToken=null;
        int minutes=0;

        String strMinutes = null;

        // take all tokens and parse them to into " int "
        try{
            // assume the first token is Hour
            hourToken = tokenizer.nextToken();
            hour = Integer.parseInt(hourToken);
            if(hour > 23 || hour <0){
                return null;
            }

            // assume the second token is Minutes
            minutesToken = tokenizer.nextToken();
            minutes = Integer.parseInt(minutesToken);
            if( minutes <0 || minutes > 59){
                return  null;
            }

        }catch(Exception formatEx){
            return null;
        }

        String newString = hourToken+":"+minutesToken;

        return newString;

    }// end of validateTime method

    /*
     *Format Timestamp to dd-MMM-yyyy format
     */
    public static String formatDate(Timestamp dateTime){
        return formatDate(dateTime,"dd-MMM-yyyy");
    }
    /*
     *Format Timestamp to given format
     */
    public static String formatDate(Timestamp dateTime,String frmtStr){
        if(dateTime==null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(frmtStr);
        return dateFormat.format(dateTime);
    }
    public synchronized static Calendar getLocalCalendar(){
        Calendar cal;
        try{
            cal = Calendar.getInstance(
                            TimeZone.getTimeZone(CoeusProperties.getProperty(
                                        CoeusPropertyKeys.LOCAL_TIMEZONE_ID)));
        }catch(IOException ex){
            UtilFactory.log("Warning ::",ex,"DateUtils", "getCurrentTime");
            return Calendar.getInstance();
        }
        return cal;
    }

    private static long getActualCurrentTimeInMillis(){
        try{
            Calendar cal = Calendar.getInstance(
                            TimeZone.getTimeZone(CoeusProperties.getProperty(
                                        CoeusPropertyKeys.LOCAL_TIMEZONE_ID)));
            TimeZone defTZ = TimeZone.getDefault();
            TimeZone tz = cal.getTimeZone();
            return defTZ.getRawOffset()!=0?cal.getTimeInMillis():
                (cal.getTimeInMillis()+(tz.getOffset(cal.getTimeInMillis())));
        }catch(IOException ex){
            UtilFactory.log("Warning ::",ex,"DateUtils", "getCurrentTime");
            return System.currentTimeMillis();
        }
    }
    /*
     *Convert Calendar object to Timestamp
     */
    public static Timestamp convertCal2Timestamp(Calendar cal){
        TimeZone defTZ = TimeZone.getDefault();
        TimeZone tz = cal.getTimeZone();
        return defTZ.getRawOffset()!=0?new Timestamp(cal.getTimeInMillis()):
                new Timestamp(cal.getTimeInMillis()+(tz.getOffset(cal.getTimeInMillis())));
//        return cal==null?null:Timestamp.valueOf(cal.get(Calendar.YEAR)+
//        "-"+(cal.get(Calendar.MONTH)+1)+
//        "-"+cal.get(Calendar.DATE)+
//        " "+cal.get(Calendar.HOUR_OF_DAY)+
//        ":"+cal.get(Calendar.MINUTE)+
//        ":"+cal.get(Calendar.SECOND)+
//        "."+cal.get(Calendar.MILLISECOND));
    }
    /*
     *Format Calendar to given format. If format is null, it formats to dd-MON-yyyy
     */
    public static String formatCalendar(Calendar cal){
        return formatCalendar(cal,null);
    }
    public static String formatCalendar(Calendar cal,String formatStr){
        DateFormat dateFormat = formatStr==null?
        DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,Locale.US):
            new SimpleDateFormat(formatStr);
            return dateFormat.format(convertCal2Timestamp(cal));
    }
    public static java.util.Date getCurrentDate(){
//        try{
//            Calendar cal = Calendar.getInstance(
//                            TimeZone.getTimeZone(CoeusProperties.getProperty(
//                                        CoeusPropertyKeys.LOCAL_TIMEZONE_ID)));
//            return new Date(cal.getTimeInMillis());
            return new Date(getActualCurrentTimeInMillis());
//        }catch(IOException ex){
//            UtilFactory.log("Warning ::",ex,"DateUtils", "getCurrentTime");
//            return new Date(System.currentTimeMillis());
//        }
    }
    public static java.sql.Timestamp getCurrentDateTime(){
//        try{
//            Calendar cal = Calendar.getInstance(
//            TimeZone.getTimeZone(CoeusProperties.getProperty(
//            CoeusPropertyKeys.LOCAL_TIMEZONE_ID)));
            //            return new Timestamp(cal.getTimeInMillis());
//            return convertCal2Timestamp(cal);
            return new Timestamp(getActualCurrentTimeInMillis());
//        }catch(IOException ex){
//            UtilFactory.log("Warning ::",ex,"DateUtils", "getCurrentTime");
//            return new Timestamp(System.currentTimeMillis());
//        }
    }
    public static java.sql.Time getCurrentTime(){
//        try{
//            Calendar cal = Calendar.getInstance(
//            TimeZone.getTimeZone(CoeusProperties.getProperty(
//                                        CoeusPropertyKeys.LOCAL_TIMEZONE_ID)));
//            return new Time(cal.getTimeInMillis());
            return new Time(getActualCurrentTimeInMillis());
//        }catch(IOException ex){
//            UtilFactory.log("Warning ::",ex,"DateUtils", "getCurrentTime");
//            return new Time(System.currentTimeMillis());
//        }
    }
    //Added for Case #3185 Generating Future Periods - start
    /**
     * This method calculates the date difference between two dates
     * @param field int the field of the date (Year/Month/Date) used for calculating the difference
     * @param fromDate Date start or from date
     * @param toDate Date to or end date
     * @returns integer value of the difference between two dates based on the field setting
     */
    public static int calculateDateDiff(int field, Date fromDate, Date toDate) {
        int diffMonths = 0;
        int duration =0;
        GregorianCalendar fromCal = new GregorianCalendar(TimeZone.getDefault());
        GregorianCalendar toCal = new GregorianCalendar(TimeZone.getDefault());
        //Code modified for case#3121
        fromCal.setTime(fromDate);
        toCal.setTime(toDate);

        switch(field){

            case Calendar.YEAR:
                return (toCal.get(Calendar.YEAR) - fromCal.get(Calendar.YEAR));
            case Calendar.MONTH:
                int months;
                // first calculate the year difference.
                months = (toCal.get(Calendar.YEAR) - fromCal.get(Calendar.YEAR))*12;
                // Add the difference in months
                months += toCal.get(Calendar.MONTH) - fromCal.get(Calendar.MONTH);
                return months;

            case Calendar.DATE:
            default:    //by default give the difference in days
                long timeInMillis;
                timeInMillis = toCal.getTimeInMillis() - fromCal.getTimeInMillis();
                return (int) (timeInMillis/(1000*60*60*24));

        }
    }
    //Added for Case #3185 Generating Future Periods - end
    /**
     * Adds the specified amount of time to the given time field.
     *
     * @param field the time field.
     * @param date
     * @param amount the amount of date or time to be added to the field.
     */
    public static Date dateAdd(int field, Date date, int amount ) {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTime(date);
        calendar.add(field,amount);
        return calendar.getTime();
    }

    /**
     * Adds the specified amount of time to the given time field. If checkEndOfMonth
     * is set to true, check whether the given date is a month end. If its a month end
     * add the given amount to the given dates field and set the day of month to the
     * last day of the resulting date
     *
     * @param field the time field.
     * @param date
     * @param amount the amount of date or time to be added to the field.
     * @param checkEndOfMonth if true, does the month end manipulation
     */
    public static Date dateAdd(int field, Date date, int amount , boolean checkEndOfMonth) {
        Date newDate = dateAdd(field, date, amount);
        if(checkEndOfMonth && (field == Calendar.YEAR || field == Calendar.MONTH)){
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
            calendar.setTime(date);
            int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            if(calendar.get(Calendar.DAY_OF_MONTH) == lastDayOfMonth){
                GregorianCalendar newCalendar = new GregorianCalendar(TimeZone.getDefault());
                newCalendar.setTime(newDate);
                newCalendar.set(Calendar.DATE, newCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                newDate = newCalendar.getTime();
            }
        }
         return newDate;
    }
    /* Returns a Calendar Object initialized with the given date.
     * If Date arguement is null, this method returns null.
     * @param date - The Date Object
     * @return The Calendar Object
     */

    public static Calendar getCalendar(Date date){
        if(date==null)
            return null;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        return calendar;
    }


      //case 3590 start
	    /**
	     * returns the Date difference between two dates in number of days.
	     * @param date1 date 1
	     * @param date2 date 2
	     * @return the number of days between date1 and date2
	     */
	    public int dateDifference(Date date1, Date date2) {
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	        return dateDifference(cal1, cal2);
	    }

	    /**
	     * returns the Date difference between two dates in number of days.
	     * @param d1 calendar date 1
	     * @param d2 calendar date 2
	     * @return the number of days between date1 and date2
	     */
	    public int dateDifference(Calendar d1, Calendar d2) {
	        if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
	        java.util.Calendar swap = d1;
	        d1 = d2;
	        d2 = swap;
	    }
	    int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
	    int y2 = d2.get(java.util.Calendar.YEAR);
	    if (d1.get(java.util.Calendar.YEAR) != y2) {
	        d1 = (java.util.Calendar) d1.clone();
	        do {
	            days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
	            d1.add(java.util.Calendar.YEAR, 1);
	        } while (d1.get(java.util.Calendar.YEAR) != y2);
	    }
	    return days;

	    }
    //case 3590 end
    
    //COEUSQA-1477 Dates in Search Results - Start
    /**
     * This method parser the date formats in the 
     * search result window to the format defined in property file.
     * @param date
     * @param dateFormat
     * @return dateValue
     */                             
    public String parseDateForSearchResults(String date, String dateFormat){
        String oldDateFormat = dateFormat;
        //to load the valid date formats
        HashMap hmDateFormat = loadSearchResultDateFormats();
        if(hmDateFormat.get(dateFormat)!=null){
            dateFormat = hmDateFormat.get(dateFormat).toString();
        }else{
            dateFormat = CoeusConstants.JAVA_DATE_YYYY_MM_DD_SLASH;
        }
        Format simpleDateFormat = new SimpleDateFormat(dateFormat);
        String dateValue = date.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
        if(dateValue.contains(" ")){
            dateValue = dateValue.substring(0,dateValue.indexOf(" "));
        }
        Date dateVal = new Date(dateValue);
        dateValue = simpleDateFormat.format(dateVal);
        if(!oldDateFormat.contains(" ")){
            dateValue = dateValue.substring(0,dateValue.indexOf(" "));
        }
        return dateValue;
    }
    
    /**
     * This method will Date format like custom date String.
     * @param dateStr date string to be parsed.
     * @param formatStr represent the format type.
     * @return String date value
     */
    public String formatTimeForSearchResults(String dateStr, String formatStr) {
        if(dateStr==null || formatStr==null)
            return null;
        else{
            //to load the valid date formats
            HashMap hmDateFormat = loadSearchResultDateFormats();
            if(hmDateFormat.get(formatStr)!=null){
                formatStr = hmDateFormat.get(formatStr).toString();
            }else{
                formatStr = CoeusConstants.JAVA_DATE_YYYY_MM_DD_SLASH;
            }
            //format as per the param formatStr
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
            return dateFormat.format(java.sql.Timestamp.valueOf(dateStr));
        }

    }
    
    /**
     * This method loads the default formats supported by 
     * the application in to the collection object
     * @return hmDateFormats
     */
    public HashMap loadFormatsForSearchResults() {
        HashMap hmDateFormats = new HashMap(20);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_SLASH,CoeusConstants.EXCEL_DATE_DD_MM_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_SLASH,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_SLASH,CoeusConstants.EXCEL_DATE_DD_MON_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_SLASH,CoeusConstants.EXCEL_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH,CoeusConstants.EXCEL_DATE_YYYY_MM_DD_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HYPHEN,CoeusConstants.EXCEL_DATE_DD_MM_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HYPHEN,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HYPHEN,CoeusConstants.EXCEL_DATE_DD_MON_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HYPHEN,CoeusConstants.EXCEL_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HYPHEN,CoeusConstants.EXCEL_DATE_YYYY_MM_DD_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HOUR_SLASH,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HOUR_SLASH,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HOUR_SLASH,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HOUR_SLASH,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HOUR_SLASH,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HOUR_HYPHEN,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HOUR_HYPHEN,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HOUR_HYPHEN,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HOUR_HYPHEN,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HOUR_HYPHEN,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_SLASH,CoeusConstants.EXCEL_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HYPHEN,CoeusConstants.EXCEL_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_SLASH,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_HYPHEN,CoeusConstants.EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH);
        return hmDateFormats;
    }
    
    /**
     * This method loads the default formats supported by 
     * the application in to the collection object
     * @return hmDateFormats
     */
    public HashMap loadSearchResultDateFormats() {
        HashMap hmDateFormats = new HashMap(20);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MM_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_SLASH,CoeusConstants.JAVA_DATE_MM_DD_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MON_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH,CoeusConstants.JAVA_DATE_YYYY_MM_DD_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MM_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_MM_DD_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MON_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HYPHEN,CoeusConstants.JAVA_DATE_YYYY_MM_DD_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MM_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_MM_DD_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MON_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HOUR_SLASH,CoeusConstants.JAVA_DATE_YYYY_MM_DD_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MM_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_MM_DD_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MON_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_YYYY_MM_DD_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        return hmDateFormats;
    }
    //COEUSQA-1477 Dates in Search Results - End
    public static Calendar getTodayDate() {
      String localTimeZone; 
         try {
             localTimeZone = CoeusProperties.getProperty(CoeusPropertyKeys.LOCAL_TIMEZONE_ID,"America/New_York");
         } catch (IOException ex) {
             localTimeZone = "America/New_York";
         }
      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(localTimeZone)); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }

   public static Calendar getCal(Date date){
        if(date==null)
            return null;
        /*String localTimeZone;
         try {
             localTimeZone = CoeusProperties.getProperty(CoeusPropertyKeys.LOCAL_TIMEZONE_ID,"America/New_York");
         } catch (IOException ex) {
             localTimeZone = "America/New_York";
         }*/
      String defaultTimeZone = "GMT+00:00";
      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(defaultTimeZone));
      cal.setTime(date);
      return cal;
    }

}
