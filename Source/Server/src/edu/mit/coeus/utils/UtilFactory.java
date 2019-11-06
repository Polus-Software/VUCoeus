/*
 * @(#) UtilFactory.java	1.0 03/25/2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
/*
 * @(#) UtilFactory.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on
 */

package edu.mit.coeus.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

//Commented - Apr 19, 2004 - start
//Added to implement Log4J - start
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//Added to implement Log4J - end
//Commented - Apr 19, 2004 - end
/**
 * A utility class used to format string arguments. This class has several handy
 * methods to format strings.
 *
 * @version 1.0 March 25, 2002, 12:27 PM
 * @author Geo Thomas
 */
public class UtilFactory extends Utils {

	/**
	 * Static method to pad single quotes on both sides of a string. This method
	 * is used to pad single quotes to a string before doing any database
	 * transaction.
	 * 
	 * @param String
	 *            string
	 * @return String after padding
	 */
	public synchronized static String checkNull(String str) {
		return (str != null ? "'" + convert(str.trim()) + "'" : null);
	}

	/**
	 * Static method to check the null string
	 * 
	 * @param str
	 *            String value
	 * @return String returns null if the parameter is a "null".
	 */
	public synchronized static String checkNullStr(String str) {
		return ("null".equalsIgnoreCase(str) ? null : str);
	}

	/**
	 * Method used to insert single quote as an escape character.
	 * 
	 * @param str
	 *            String to insert
	 * @return String string value
	 */
	public synchronized static String convert(String str) {
		String strToConvert;
		StringBuffer convertedStr = new StringBuffer("");
		strToConvert = " " + str + " ";
		int startIndex = 0;
		for (int charIndex = 0; charIndex < strToConvert.length(); ++charIndex) {
			if (strToConvert.charAt(charIndex) == '\'') {
				convertedStr = convertedStr.append(strToConvert.substring(startIndex, charIndex) + "'");
			} else {
				convertedStr = convertedStr.append(strToConvert.substring(startIndex, charIndex));
			}
			startIndex = charIndex;
		}
		return convertedStr.toString().toUpperCase().trim();

		// return str.toString().toUpperCase().trim();
	}

	/* CASE #1080 Comment Begin */
	/**
	 * Method to return empty string if the parameter is null or "null" This
	 * method is used in jsp pages for displaying empty string when the argument
	 * is null or "null"
	 * 
	 * @param str
	 *            string to check null
	 * @return String empty string.
	 */
	/*
	 * public static String dispEmptyStr(String str){ return ((str==null ||
	 * str.equals("null"))?"&nbsp":str); }
	 */
	/* CASE #1080 Comment End */

	/**
	 * An Utility method for casting and converting an object into empty literal
	 * string ( "" ) if parameter newObject is null.
	 *
	 * <b>Note: </b> Use only If parameter (Object type) is going to convert
	 * into String type
	 *
	 * @param newObject
	 *            Strictly an instance of Object .
	 * @return Returns empty string( "" ), If parameter newObject is null
	 *
	 * @author RYK
	 */
	public synchronized static String convertNull(Object newObject) {

		if (newObject == null) {
			return "";
		} else {
			return newObject.toString();
		}
	}

	/**
	 * An Utility method for converting null (empty reference) String object
	 * into empty literal string ( "" ) if parameter newString is null.
	 *
	 * <b>Note: </b> Use only If parameter (Object type) is going to convert
	 * into String type
	 *
	 * @param newString
	 *            if parameter is only of String type.
	 * @return returns empty string( "" ),If newString is null.
	 *
	 * @author RYK
	 */
	public synchronized static String convertNull(String newString) {

		if (newString == null) {
			return "";
		} else {
			return newString;
		}
	}

	/* CASE #1080 Begin */
	/**
	 * Method used by jsp's to display null or empty String as &nbsp;
	 * 
	 * @param str
	 *            string to check
	 * @return String
	 */
	public synchronized static String dispEmptyStr(String str) {
		return ((str == null || str.equals("")) ? "&nbsp" : str);
	}

	/* CASE #1080 End */
	/* CASE #748 Begin */
	/**
	 * Method used to insert backslash as an escape character.
	 * 
	 * @param String
	 *            string to insert
	 * @return String string
	 */
	public synchronized static String escapeApostropheWithSlash(String str) {
		String strToConvert;
		StringBuffer convertedStr = new StringBuffer("");
		strToConvert = " " + str + " ";
		int startIndex = 0;
		for (int charIndex = 0; charIndex < strToConvert.length(); ++charIndex) {
			if (strToConvert.charAt(charIndex) == '\'') {
				String escape = "\\";
				convertedStr = convertedStr.append(strToConvert.substring(startIndex, charIndex) + escape);
			} else {
				convertedStr = convertedStr.append(strToConvert.substring(startIndex, charIndex));
			}
			startIndex = charIndex;
		}
		return convertedStr.toString().toUpperCase().trim();
	}

	/* CASE #748 End */
	/* CASE #1678 End */
	public static synchronized String getLocalTimeZoneId() {
		try {
			return CoeusProperties.getProperty(CoeusPropertyKeys.LOCAL_TIMEZONE_ID, "America/New_York");
		} catch (IOException ex) {
			return "America/New_York";
		}
	}

	/**
	 * This method is used to log the message on the console.
	 * 
	 * @param str
	 *            message.
	 */
	public synchronized static void log(String str) {
		log(str, null, null, null);
	}

	/**
	 * This method is used to log the message on the console.
	 * 
	 * @param str
	 *            message.
	 */
	public synchronized static void log(String str, String filename) {
		logMessage(str, null, null, null, filename);
	}

	// Commented following code to revert back the changes - April 19, 2004 -
	// start
	/**
	 * A Handy method to write all exception information into a log file to
	 * trace the application behaviour. Uses Log4J API's
	 *
	 * @param msg
	 *            String Message
	 * @param ex
	 *            Exception
	 * @param srcFileName
	 *            String SourceFileName
	 * @param methodName
	 *            String Method Name
	 *
	 */
	/*
	 * public void log(String msg,Exception ex,String srcFileName,String
	 * methodName){ String logFileName = null; if(srcFileName!=null &&
	 * !srcFileName.equals("")){ logger = Logger.getLogger(srcFileName); }else{
	 * logger = Logger.getLogger(UtilFactory.class); } try{ String fileSeparator
	 * = File.separator; java.util.Calendar calToday = Calendar.getInstance();
	 * java.util.Date dtToday = calToday.getTime(); InputStream is =
	 * getClass().getResourceAsStream("/coeus.properties"); Properties
	 * coeusProps = new Properties();
	 * 
	 * Properties log4JProps = new Properties(); InputStream log4JInputStream =
	 * getClass().getResourceAsStream("/log4J.properties");
	 * 
	 * try{ coeusProps.load(is); log4JProps.load(log4JInputStream); }catch
	 * (IOException e) { System.err.println("Can't read the properties file. " +
	 * "Make sure Coeus.properties is in the CLASSPATH or in the classes folder"
	 * ); return; } try{ String coeusHome =
	 * coeusProps.getProperty("COEUS_HOME"); String strLogDir =
	 * coeusProps.getProperty("LOG_HOME"); String strLogFile = "Coeus_log_";
	 * java.util.Date currentDate = new java.util.Date();
	 * java.text.SimpleDateFormat dateFormat = new
	 * java.text.SimpleDateFormat("yyyy-MM-dd"); File logDir = new File(
	 * strLogDir ); if( !logDir.exists() ) { logDir.mkdirs(); } logFileName =
	 * strLogDir+File.separator+strLogFile+dateFormat.format(currentDate)+
	 * ".txt"; log4JProps.setProperty("log4j.appender.R.File", logFileName);
	 * 
	 * PropertyConfigurator.configure(log4JProps); }catch (Exception e) {
	 * System.err.println("Error while configuring Log4J."); return; }
	 * 
	 * if(msg!=null && !"".equals(msg)){ logger.debug("Message=>"+msg); }
	 * if(srcFileName!=null && !"".equals(srcFileName)){ logger.info(
	 * "File name : "+srcFileName); } if(methodName!=null &&
	 * !"".equals(methodName)){ logger.info("Method name=>"+methodName); }
	 * if(ex!=null){ logger.error(
	 * "********************** Begin Exception Block ********************************"
	 * ); logger.error("Exception : ",ex); logger.error(
	 * "********************** End Exception Block ********************************"
	 * ); } }catch(Exception exx){ System.err.println("Exception occured :"
	 * +exx); } }
	 */
	// Commented - April 19, 2004 - end

	/**
	 * Over loaded method to log information with user Id
	 */
	public synchronized static void log(String userId, String msg, Throwable ex, String srcFileName,
			String methodName) {
		String tmpStr = msg;
		if (msg != null && userId != null) {
			tmpStr = "User Id =>" + userId + "\n" + msg;
		}
		log(tmpStr, ex, srcFileName, methodName);
	}

	/**
	 * A Handy method to write all exception information into a log file to
	 * trace the application behaviour.
	 *
	 * @param msg
	 *            String Message
	 * @param ex
	 *            Exception
	 * @param srcFileName
	 *            String SourceFileName
	 * @param methodName
	 *            String Method Name
	 *
	 */
	public synchronized static void log(String msg, Throwable ex, String srcFileName, String methodName) {
		logMessage(msg, ex, srcFileName, methodName, "Coeus_log");
	}

	public static synchronized void logFile(String dir, String fileName, String content) throws IOException {
		java.io.FileOutputStream fos = null;
		try {
			File file = new File(dir, fileName);
			fos = new java.io.FileOutputStream(file);
			fos.write(content.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			fos.flush();
			fos.close();
		}
	}

	/**
	 * A Handy method to write all exception information into a log file to
	 * trace the application behaviour.
	 *
	 * @param msg
	 *            String Message
	 * @param ex
	 *            Exception
	 * @param srcFileName
	 *            String SourceFileName
	 * @param methodName
	 *            String Method Name
	 *
	 */
	public synchronized static void logMessage(String msg, Throwable ex, String srcFileName, String methodName,
			String filename) {
		PrintWriter pwLog = null;
		String logFileName = null;
		String fileSeparator = File.separator;
		// java.util.Calendar calToday =
		// Calendar.getInstance(TimeZone.getTimeZone(getLocalTimeZoneId()));
		java.util.Calendar calToday = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		int offset = TimeZone.getTimeZone(getLocalTimeZoneId()).getOffset(calToday.getTimeInMillis());
		// java.util.Date dtToday = calToday.getTime();
		Timestamp dtToday = new Timestamp(calToday.getTimeInMillis() + offset);
		// Timestamp dtToday =
		// Timestamp.valueOf(calToday.get(Calendar.YEAR)+"-"+
		// (calToday.get(Calendar.MONTH)+1)+"-"+
		// calToday.get(Calendar.DATE)+" "+
		// calToday.get(Calendar.HOUR_OF_DAY)+":"+
		// calToday.get(Calendar.MINUTE)+":"+
		// calToday.get(Calendar.SECOND)+"."+
		// calToday.get(Calendar.MILLISECOND));
		try {
			String strLogDir = CoeusProperties.getProperty(CoeusPropertyKeys.LOG_HOME);
			String strLogFile = filename + "_";// polling?"Coeus_S2S_Polling_":"Coeus_log_";
			// java.util.Date currentDate = new java.util.Date();
			java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.text.DateFormat dateTimeFormat = java.text.DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.SHORT, Locale.US);
			File logDir = new File(strLogDir);
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			logFileName = strLogDir + File.separator + strLogFile + dateFormat.format(dtToday) + ".txt";
			File fLogFile = new File(logFileName);

			if (fLogFile.exists()) {
				pwLog = new PrintWriter(new FileWriter(fLogFile, true), true);
			} else {
				pwLog = new PrintWriter(new FileWriter(fLogFile), true);
			}
			synchronized (pwLog) {
				// pwLog.println("#########Begin Message Block###########");
				pwLog.println();
				pwLog.println("Date Time =>" + dateTimeFormat.format(dtToday));
				if (msg != null && !"".equals(msg)) {
					pwLog.println(msg);
				}
				if (srcFileName != null && !"".equals(srcFileName)) {
					pwLog.println("File name : " + srcFileName);
				}
				if (methodName != null && !"".equals(methodName)) {
					pwLog.println("Method name=>" + methodName);
				}
				if (ex != null) {
					pwLog.println("********* Begin Exception Block *************");
					ex.printStackTrace(pwLog);
					pwLog.println("********* End Exception Block ***************");
				}
				pwLog.println();
				// pwLog.println("#########End Message Block###########");
			}
		} catch (IOException e) {
			System.err.println("Can't open the log file: " + logFileName);
			pwLog = new PrintWriter(System.err);
		} finally {
			if (pwLog != null) {
				pwLog.flush();
				// pwLog.close();
			}
		}
	}

	/* CASE #1678 Begin */
	public synchronized static String setNullToUnknown(String str) {
		if (str == null) {
			str = "Unknown";
		}
		return str;
	}

	// Commented - Apr 19, 2004 - start
	// Added to implement log4j - start
	// static Logger logger = Logger.getLogger(UtilFactory.class);
	// Added to implement log4j - end
	// Commented - Apr 19, 2004 - end
	// private static String LOCAL_TIME_ZONE;
	/** Creates new UtilFactory */
	private UtilFactory() {
	}

	/**
	 * Method to read the corresponding message from the
	 * errorMessages.propertiws file This method will be invoked from the
	 * ErrorPage.jsp to display the error message.
	 * 
	 * @param exceptionCode
	 *            String Exceptioncode
	 * @return String string
	 */
	public synchronized String getErrorMessage(String exceptionCode) {
		String errorFileName = null;
		String errorMsg = "";
		try {
			InputStream is = getClass().getResourceAsStream("/ErrorMessages.properties");
			Properties propErrors = new Properties();
			try {
				propErrors.load(is);
			} catch (IOException e) {
				System.err.println("Can't read the properties file. "
						+ "Make sure ErrorMessages.properties is in the CLASSPATH or in the classes folder");
			}
			errorMsg = propErrors.getProperty(exceptionCode, "exceptionCode.unknown");
		} catch (Exception ex) {
			log("", ex, "UtilFactory.java", "getErrorMessage()");
		}
		return errorMsg;
	}
}
