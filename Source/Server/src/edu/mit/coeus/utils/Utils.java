/*
 * Utils.java
 *
 * Created on November 30, 2004, 1:38 PM
 */

package edu.mit.coeus.utils;

import java.text.DecimalFormat;

/**
 *
 * @author  geot
 */
public class Utils {
    
    protected Utils(){}
    /**
     *  Static method to pad single quotes on both sides of a string.
     *  This method is used to pad single quotes to a string before doing any database transaction.
     *  @param str String string
     *  @return String String after padding
     */
    public static String checkNull(String str){
        return (str!=null?"'"+convert(str.trim())+"'":null);
    }

    /**
     *  Static method to check the null string
     *  @param str String string
     *  @return String returns null if the parameter is a "null".
     */
    public static String checkNullStr(String str){
        return ("null".equalsIgnoreCase(str) ? null:str);
    }

    /**
     *  Method to return empty string if the parameter is null or "null"
     *  This method is used in jsp pages for displaying empty string when the argument is null or "null"
     *  @param str String string to check null
     * @return String display  string value.
     */
    public static String dispEmptyStr(String str){
        return ((str==null || str.equals("null"))?"&nbsp":str);
    }

    /**
     *  Method used to insert single quote as an escape character.
     *  @param str String string to insert
     *  @return String string
     */
    public static String convert(String str){
        String strToConvert;
        StringBuffer convertedStr=new StringBuffer("");
        strToConvert = " "+str+" ";
        int startIndex = 0;
        for(int charIndex=0;charIndex<strToConvert.length();++charIndex){
            if(strToConvert.charAt(charIndex)=='\''){
                convertedStr = convertedStr.append(strToConvert.substring(startIndex,charIndex)+"'");
            }else{
                convertedStr = convertedStr.append(strToConvert.substring(startIndex,charIndex));
            }
            startIndex = charIndex;
        }
        return convertedStr.toString().toUpperCase().trim();


    }

	/**
	 * An Utility method for casting and converting an object into empty literal string ( "" )
	 * if parameter newObject is null.
	 *
	 * <b>Note: </b> Use only If parameter (Object type) is going to convert into String type
	 *
	 * @param newObject   Strictly an instance of Object .
	 * @return  Returns empty string( "" ) If parameter newObject is null
	 
	 */
	public static String convertNull(Object newObject){

		if(newObject == null){
			return "";
		}else{
			return newObject.toString();
		}
	}

	/**
	 * An Utility method for  converting null (empty reference) String object  into empty literal string ( "" )
	 * if parameter newString is null.
	 *	
	 * @param newString   if parameter is only of String type.
	 * @return empty string( "" )If newString is null.
	 *	 
	 */
	public static String convertNull(String newString){

		if(newString == null){
			return "";
		}else{
			return newString;
		}
	}

	/**
	 * replaceStr all `` with ` ` in a string message.
	 * @param sourceStr       String source message
	 * @param searchStr       String string to be searched and replaced
	 * @param replaceStr      String replacement string
	 * @return              String the string with the replaced message
	 */
	public static String replaceString( String sourceStr , String searchStr , String replaceStr ) {
            /*
             *Modified by Geo on 09 Nov 2005
             *Fix for a bug. it was going to infinit loop if the replaceStr starts with searchStr
             */
            StringBuffer srcBuffer = new StringBuffer(sourceStr);
            if( ! searchStr.equalsIgnoreCase( replaceStr ) ) {
                int offset = 0;
                int index = -1;
                int repLength = replaceStr.length();
                while( (index = srcBuffer.indexOf( searchStr,offset )) != -1 ) {
                    offset =index+repLength;
                    srcBuffer.delete(index,index+searchStr.length());
                    srcBuffer.insert(index, replaceStr);
                }
            }
            return srcBuffer.toString();
	}        
        
        public static double round(double d){
            return round(d,2);
        }
        public static double round(double d,int precision){
            return ((double)Math.round(d* Math.pow(10.0, precision) )) / 100;
        }
        public static void main(String args[]){
            System.out.println("1");
            System.out.println(replaceString("12345= 'COEUS' AND 6789 ('COEUSu')  ", "COEUS", "COEUSADM"));
            System.out.println("2");        
        }
        
}

