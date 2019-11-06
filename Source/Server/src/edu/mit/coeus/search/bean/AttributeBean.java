/*
 * @(#)AttributeBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 15, 2002, 3:24 PM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.search.bean;
/**
 * This class is used to represent the attribute which user has enetered in each 
 * input field as the search criteria. This has mainly three tokens
 * <li> attribute value
 * <li> attribute type
 * <li> row id
 * While creating an instance of this bean, it will parse these tokens and assign
 * each property.
 */
public class AttributeBean implements java.io.Serializable{

    /*
     *  String constants to represent each type of the attribute
     */
    private static final String LIKE_TYPE_STR = "LIKE";
    private static final String GR_EQUAL_TYPE_STR = ">=";
    private static final String LESS_EQUAL_TYPE_STR = "<=";
    private static final String NOT_EQUAL_TYPE_STR = "<>";
    private static final String LESS_TYPE_STR = "<";
    private static final String GREATER_TYPE_STR = ">";
    private static final String AND_TYPE_STR = "AND";
    private static final String OR_TYPE_STR = "OR";
    private static final String EQUAL_TYPE_STR = "=";
    private static final String IS_NULL = "IS NULL";
    // holds row id
    private String rowId;
    //holds attribute value
    private String attValue;
    //holds attribute type
    private String valueType;
    // Code added for coeus 4.3 search between two dates enhancements - starts
    // Added for searching in between dates.
    private static final String BETWEEN = "BETWEEN";
    // Code added for coeus 4.3 search between two dates enhancements - ends
    
    /** Creates new AttributeBean */
    public AttributeBean(String rowId,String fieldValue) {
        if(fieldValue==null || fieldValue.trim().equals("")){
            return;
        }
        this.rowId = rowId;
        attValue = fieldValue;
        valueType = "=";
        int startIndex = 0;
        //setting the start index of the exact value of the attribute string
        /* modified by ravi on 19-02-03  to fix the bug with id: #153*/ 
        /* for providing checking for multiple values in search like date > 1/1/03 and < 2/1/03 etc...*/
        /* as there may be two operators like 'AND' and  '<=' etc elseif condintions
          are broken to if conditions so that  start index will get incremented if
          there are two operators.*/
        
        /* begin fixID: 190_2 */
        
        /* modified on 3-MAR-03 to consider operators AND, OR and LIKE if they are
           in the starting position of the expression, otherwise consider them as
           part of the value specified by user */
        
        fieldValue = fieldValue.trim();
        
        //Code commented and modified for Coeus4.3 search between two dates
        
//        int indx = fieldValue.toUpperCase().indexOf(AND_TYPE_STR);
//        if( ( indx != -1) && ( indx == 0 ) ) {
//            startIndex = indx;
//            startIndex += AND_TYPE_STR.length();
//            if(fieldValue.length()> startIndex){
//                if(!isOperator(fieldValue.charAt(startIndex))){
//                    startIndex=-1;
//                }
//            }
//        }
//        
//        indx = fieldValue.toUpperCase().indexOf(OR_TYPE_STR);
//        if( ( indx != -1 ) && ( indx == 0 ) ) {
//            startIndex = indx;
//            startIndex += OR_TYPE_STR.length();
//            if(fieldValue.length()> startIndex){
//                if(!isOperator(fieldValue.charAt(startIndex)) ){
//                    startIndex=-1;
//                }else if( Integer.parseInt(rowId) > 0 ) {
//                    fieldValue = fieldValue.substring(startIndex).trim();
//                    startIndex = -1;
//                }
//                
//            }
//        }
//        
//        indx = fieldValue.toUpperCase().indexOf(LIKE_TYPE_STR);
//        if( indx != -1 ) {
//            int tempStartIndex = startIndex;
//            if ( indx == 0 ) {
//                startIndex = indx;
//                startIndex += LIKE_TYPE_STR.length();
//                char likeValue= ' ';
//                if(fieldValue.length()> startIndex){
//                    likeValue = fieldValue.charAt(startIndex);
//                    if( !(( likeValue == '%') || (likeValue == ' ') )){
//                        startIndex = tempStartIndex;
//                    }
//                }
//            }else if(! (fieldValue.charAt(indx-1) == ' ') ){
//                startIndex = tempStartIndex;
//            }else{
//                
//                startIndex = indx;
//                startIndex += LIKE_TYPE_STR.length();
//            }
//            
//        }
//        /* end of fixID: 190_2 */
//        indx = fieldValue.indexOf(GR_EQUAL_TYPE_STR);
//        if( indx != -1 ) {
//            startIndex = indx;
//            startIndex += GR_EQUAL_TYPE_STR.length();
//        }
//        
//        indx = fieldValue.indexOf(LESS_EQUAL_TYPE_STR);
//        if( indx != -1){
//            startIndex = indx;
//            startIndex += LESS_EQUAL_TYPE_STR.length();
//        }
//        
//        indx = fieldValue.indexOf(NOT_EQUAL_TYPE_STR);
//        if( indx != -1){
//            startIndex = indx;
//            startIndex += NOT_EQUAL_TYPE_STR.length();
//        }
//        
//        indx = fieldValue.indexOf(LESS_TYPE_STR,startIndex);
//        if( indx != -1){
//            startIndex = indx;
//            startIndex += LESS_TYPE_STR.length();
//        }
//        
//        indx = fieldValue.indexOf(GREATER_TYPE_STR,startIndex);
//        if( indx != -1){
//            startIndex = indx;
//            startIndex += GREATER_TYPE_STR.length();
//        }
//        /* added on 3-MAR-03 to provide checking for operator '=' also */
//        indx = fieldValue.indexOf(EQUAL_TYPE_STR,startIndex);
//        if( indx != -1){
//            startIndex = indx;
//            startIndex += EQUAL_TYPE_STR.length();
//        }
//
//        if(startIndex!=-1){
//            //calling parse method to split the type and value
//            parse(fieldValue,startIndex);
        
        String fieldValueToUpper = fieldValue.toUpperCase();
        
        //If the search criteria starts with AND then the block below will execute
        if(fieldValueToUpper.startsWith(AND_TYPE_STR)){
            startIndex += AND_TYPE_STR.length();
            if(fieldValueToUpper.length()> startIndex){
                if(!isOperator(fieldValueToUpper.charAt(startIndex))){
                    startIndex=0;
                }
            }            
          //If the search criteria starts with OR then the block below will execute  
        } else if(fieldValueToUpper.startsWith(OR_TYPE_STR)){
            startIndex += OR_TYPE_STR.length();
            if(fieldValueToUpper.length()> startIndex){
                if(!isOperator(fieldValueToUpper.charAt(startIndex)) ){
                    startIndex=0;
                }else if( Integer.parseInt(rowId) > 0 ) {
                    fieldValue = fieldValue.substring(startIndex).trim();
                    fieldValueToUpper = fieldValueToUpper.substring(startIndex).trim();
                    startIndex = 0;
                }
            }            
        }
        startIndex = discardEmptySpace(fieldValueToUpper, startIndex);

        //If the search criteria starts with LIKE then the block below will execute
        if(fieldValueToUpper.startsWith(LIKE_TYPE_STR, startIndex)){
            if(isOperator(fieldValueToUpper.charAt(startIndex+LIKE_TYPE_STR.length()))){
                startIndex += LIKE_TYPE_STR.length();
            }
        //If the search criteria starts with BETWEEN then block below will execute
        } else if(fieldValueToUpper.startsWith(BETWEEN, startIndex)){
            if(isOperator(fieldValueToUpper.charAt(startIndex+BETWEEN.length()))){
                startIndex += BETWEEN.length();
            }
        //If the search criteria starts with >= then block below will execute
        } else if(fieldValueToUpper.startsWith(GR_EQUAL_TYPE_STR, startIndex)){
            startIndex += GR_EQUAL_TYPE_STR.length();
        //If the search criteria starts with <= then block below will execute
        } else if(fieldValueToUpper.startsWith(LESS_EQUAL_TYPE_STR, startIndex)){
            startIndex += LESS_EQUAL_TYPE_STR.length();
        //If the search criteria starts with <> then block below will execute
        } else if(fieldValueToUpper.startsWith(NOT_EQUAL_TYPE_STR, startIndex)){
            startIndex += NOT_EQUAL_TYPE_STR.length();
        //If the search criteria starts with < then block below will execute
        } else if(fieldValueToUpper.startsWith(LESS_TYPE_STR, startIndex)){
            startIndex += LESS_TYPE_STR.length();
            startIndex = discardEmptySpace(fieldValueToUpper, startIndex);
            if(fieldValueToUpper.charAt(startIndex) == '=' || fieldValueToUpper.charAt(startIndex) == '>'){
                startIndex ++;
            }
        //If the search criteria starts with > then block below will execute
        } else if(fieldValueToUpper.startsWith(GREATER_TYPE_STR, startIndex)){
            startIndex += GREATER_TYPE_STR.length();
            startIndex = discardEmptySpace(fieldValueToUpper, startIndex);
            if(fieldValueToUpper.charAt(startIndex) == '='){
                startIndex += EQUAL_TYPE_STR.length();
            }
        //If the search criteria starts with = then block below will execute
        } else if(fieldValueToUpper.startsWith(EQUAL_TYPE_STR, startIndex)){
            startIndex += EQUAL_TYPE_STR.length();
        }

        if(startIndex!=0){
            //calling parse method to split the type and value
            parse(fieldValue,startIndex);
            if( attValue.trim().equalsIgnoreCase("NULL") ){
                valueType = IS_NULL;
                attValue = "";
            }        
        }else{
            /* if user is searching for null convert the value type to IS NULL 
             * and set empty string 
             */
            if( fieldValue.trim().equalsIgnoreCase("NULL") ){
                valueType = IS_NULL;
                attValue = "";
            }else{
                attValue = fieldValue;
            }
        }
    }
    
    private boolean isOperator(char value){
        return ((value == ' ') || (value == '>') || (value == '<') || (value == '='));
    }
    /*
     *  Method to separate value and type
     */
    private void parse(String fieldValue,int startIndex){
        if(startIndex!=-1 && fieldValue.length() > startIndex ){
            valueType = fieldValue.trim().substring(0,startIndex);
            attValue = fieldValue.trim().substring(startIndex);
        }
    }

    /**
     * The method used to get row id
     * @return row id
     */
    String getRowId() {
        return rowId;
    }
    
    /**
     *  The method used to get the attribute value
     *  @retrun attribute value
     */
    public String getAttValue() {
        return attValue;
    }
    
    /**
     *  The method used to get the attribute value type
     *  @return attribute value type
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Row Id=>"+rowId);
        strBffr.append(";");
        strBffr.append("Attribute Value=>"+attValue);
        strBffr.append(";");
        strBffr.append("Attribute Value Type=>"+valueType);
        return strBffr.toString();
    }
    
    /**
     * To discard the empty spaces from the started index to
     * the next non empty space character.
     * @param fieldValue 
     * @param startIndex existing starting index for that fieldValue
     * @return int new starting index for that fieldValue
     */    
    public int discardEmptySpace(String fieldValue, int startIndex){
        for(int index=startIndex; index<fieldValue.length(); index++){
            if(fieldValue.charAt(index) != ' '){
                return index;
            }
        }
        return startIndex;
    }    
    
}
