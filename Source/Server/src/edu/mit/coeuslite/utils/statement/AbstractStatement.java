
/*
 * AbstractStatement.java
 *
 * Created on April 22, 2005, 12:07 PM
 */

package edu.mit.coeuslite.utils.statement ;

import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.lang.reflect.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.struts.action.DynaActionForm;

/**
 * Abstract class which has a helper method to set parameters for the Statement.
 * @author sharathk
 * @version 0.1
 */
public abstract class AbstractStatement implements StatementType{
    
    /**
     * Data Source Name
     */
    protected static final String DSN = "Coeus";
    /**
     * Service name
     */
    protected static final String SERVICE_NAME = "Coeus";
    /**
     * holds SQL parameters
     */
    protected StringBuffer sqlParams;
    
    private Object value;
    
    /**
     * sets the parameter values from the bean.
     * @param listStatementMap List of StatementMap
     * @param bean the values would be extracted from this object.
     * @param userId user Id
     * @throws NoSuchMethodException if the mehod could not be found.
     * @throws InvocationTargetException if the underlying method throws an exception
     * @throws IllegalAccessException if this Method object enforces Java language access control and the underlying method is inaccessible.
     * @throws DBException if DB Engine throws an Exception
     * @return Vector of Parameters
     */
    protected Vector getParams(List listStatementMap, Object bean, String userId)throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, DBException {
        Vector param = new Vector();
        if(listStatementMap == null || listStatementMap.size() == 0 || bean == null) {
            return param;
        }
        
        if(bean instanceof DynaActionForm) {
            DynaActionForm dynaActionForm = (DynaActionForm)bean;
            return getDynaParams(listStatementMap, dynaActionForm.getMap(), userId);
        }else if(bean instanceof Map) {
            return getDynaParams(listStatementMap, (Map)bean, userId);
        }
        
        StatementMap statementMap;
        sqlParams = new StringBuffer();
        
        Method method;
        Field field;
        Class dataClass;
        String methodName, fieldName, type = null;
//        Object value = null;
        
        for(int index = 0; index < listStatementMap.size(); index++) {
            statementMap = (StatementMap)listStatementMap.get(index);
            fieldName =  statementMap.getField();
            
            dataClass = bean.getClass();
            methodName = "get" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
            method = dataClass.getMethod(methodName, null);
            value = method.invoke(bean, null);
            type = getDBEngineParameterType(statementMap.getParameterType());
            
            if(index > 0) {
                sqlParams.append(" , ");
            }
            sqlParams.append(" <<"+statementMap.getParameter()+">> ");
            
            if(fieldName.equals(StatementConstants.PRE_DEFINED)) {
                //As of this implementation(version 0.1) only User Id can be predefined which is sent as parameter.
                param.addElement(new Parameter(statementMap.getParameter(), type, userId));
                continue;
            }
            if(fieldName.equals(StatementConstants.AUTO_GENERATE)) {
                //As of implementation(version 0.1) only Timestamp can be Auto Generated.
                //Timestamp dbTimestamp = new Timestamp(new Date().getTime());
                param.addElement(new Parameter(statementMap.getParameter(), type, edu.mit.coeus.utils.DateUtils.getCurrentDateTime()));
                continue;
            }
            
            //field = dataClass.getField(fieldName);
            
            param.addElement(new Parameter(statementMap.getParameter(), type, value));
            
        }
        return param;
    }
    
    protected Vector getDynaParams(List listStatementMap, Map map, String userId)throws DBException {
        Vector param = new Vector();
        if(listStatementMap == null || listStatementMap.size() == 0) {
            return param;
        }
        StatementMap statementMap;
        sqlParams = new StringBuffer();
        
        String fieldName, type = null;
//        Object value;
        
        for(int index = 0; index < listStatementMap.size(); index++) {
            statementMap = (StatementMap)listStatementMap.get(index);
            fieldName =  statementMap.getField();
            
            value = map.get(fieldName);
            if(fieldName.equals("onOffCampusFlag")){
                type = getDBEngineParameterType(statementMap.getParameterType(), fieldName);
            }else{
                type = getDBEngineParameterType(statementMap.getParameterType());
            }
            
            if(index > 0) {
                sqlParams.append(" , ");
            }
            sqlParams.append(" <<"+statementMap.getParameter()+">> ");
            
            if(fieldName.equals(StatementConstants.PRE_DEFINED)) {
                //As of this implementation(version 0.1) only User Id can be predefined which is sent as parameter.
                param.addElement(new Parameter(statementMap.getParameter(), type, userId));
                continue;
            }
            if(fieldName.equals(StatementConstants.AUTO_GENERATE)) {
                //As of implementation(version 0.1) only Timestamp can be Auto Generated.
                //Timestamp dbTimestamp = new Timestamp(new Date().getTime());
                param.addElement(new Parameter(statementMap.getParameter(), type, edu.mit.coeus.utils.DateUtils.getCurrentDateTime()));
                continue;
            }
            
            param.addElement(new Parameter(statementMap.getParameter(), type, value));
            
        }
        return param;
    }
    
    protected StringBuffer getOutputParams(List statementOutputList) {
        StringBuffer outputParams = new StringBuffer();
        if(statementOutputList == null || statementOutputList.size() == 0) {
            return outputParams;
        }
        StatementOutput statementOutput;
        for(int index = 0; index < statementOutputList.size(); index++) {
            statementOutput = (StatementOutput)statementOutputList.get(index);
            if(index > 0) {
                outputParams.append(" , ");
            }
            outputParams.append(" << OUT "+statementOutput.getType()+" "+statementOutput.getName()+" >>");
        }
        return outputParams;
    }
    
    private String getDBEngineParameterType(String parameterType) {
        String type = null;
        if(parameterType.equals("String")) {
            type = DBEngineConstants.TYPE_STRING;
        }else if(parameterType.equals("Integer")) {
            type = DBEngineConstants.TYPE_INTEGER;
        }else if(parameterType.equals("Timestamp")) {
            value = getTimeStampForString(value);
            type = DBEngineConstants.TYPE_TIMESTAMP;
        }else if(parameterType.equals("int")) {
            if(value != null && !value.equals("")) {
                value = ""+value;
            }
            type = DBEngineConstants.TYPE_INT;
        }else if(parameterType.equals("Date")) {
            value = getDateForString(value);
            type = DBEngineConstants.TYPE_DATE;
        }else if(parameterType.equals("double")) {
            type = DBEngineConstants.TYPE_DOUBLE;
        }else if(parameterType.equals("Double")) {
            type = DBEngineConstants.TYPE_DOUBLE_OBJ;
        }else if(parameterType.equals("float")) {
            type = DBEngineConstants.TYPE_FLOAT;
        }else if(parameterType.equals("Float")) {
            type = DBEngineConstants.TYPE_FLOAT_OBJ;
        }else if(parameterType.equals("Byte")) {
            type = DBEngineConstants.TYPE_BLOB;
        }else if(parameterType.equals("Clob")) {
            type = DBEngineConstants.TYPE_CLOB;
        }else if(parameterType.equals("Long")) {
            type = DBEngineConstants.TYPE_LONG;
        }else if(parameterType.equals("Boolean") || parameterType.equals("boolean")) {
            value = booleanConvertion(value);
            type = DBEngineConstants.TYPE_STRING;
        }
        return type;
        //@todo: Add More Datatype implementation here
    }
    
    /** This overloaded method is used for the boolean conversion
     *for on-off campus flag. If the boolean value is true the flag should be N
     *If the boolean value is false - F. This scenario will occur for on-off campus falg
     */
    private String getDBEngineParameterType(String parameterType, String dataType) {
        String type = null;
        if(parameterType.equals("String")) {
            type = DBEngineConstants.TYPE_STRING;
        }else if(parameterType.equals("Integer")) {
            type = DBEngineConstants.TYPE_INTEGER;
        }else if(parameterType.equals("Timestamp")) {
            value = getTimeStampForString(value);
            type = DBEngineConstants.TYPE_TIMESTAMP;
        }else if(parameterType.equals("int")) {
            value = ""+value;
            type = DBEngineConstants.TYPE_INT;
        }else if(parameterType.equals("Date")) {
            value = getDateForString(value);
            type = DBEngineConstants.TYPE_DATE;
        }else if(parameterType.equals("double")) {
            type = DBEngineConstants.TYPE_DOUBLE;
        }else if(parameterType.equals("Double")) {
            type = DBEngineConstants.TYPE_DOUBLE_OBJ;
        }else if(parameterType.equals("float")) {
            type = DBEngineConstants.TYPE_FLOAT;
        }else if(parameterType.equals("Float")) {
            type = DBEngineConstants.TYPE_FLOAT_OBJ;
        }else if(parameterType.equals("Byte")) {
            type = DBEngineConstants.TYPE_BLOB;
        }else if(parameterType.equals("Clob")) {
            type = DBEngineConstants.TYPE_CLOB;
        }else if(parameterType.equals("Long")) {
            type = DBEngineConstants.TYPE_LONG;
        }else if(parameterType.equals("Boolean") || parameterType.equals("boolean")) {
            if(dataType!= null && dataType.equals("onOffCampusFlag")){
                value = booleanConvertion(value,dataType);
            }else{
                value = booleanConvertion(value);
            }
            type = DBEngineConstants.TYPE_STRING;
        }
        return type;
        //@todo: Add More Datatype implementation here
    }
    
    /**
     * Getter for property sqlParams.
     * @return Value of property sqlParams.
     */
    public java.lang.StringBuffer getSqlParams() {
        return sqlParams;
    }
    
    /**
     * Setter for property sqlParams.
     * @param sqlParams New value of property sqlParams.
     */
    public void setSqlParams(java.lang.StringBuffer sqlParams) {
        this.sqlParams = sqlParams;
    }
    
    /* This method converts a boolean object to a String Object
     * @param value holds boolean value
     * @return string object.
     */
    private String booleanConvertion(Object value) {
        String defaultValue = "N";
        if (value == null) {
            return defaultValue;
        }
        
        String stringValue = value.toString();
        if (stringValue.equalsIgnoreCase("yes") ||
                stringValue.equalsIgnoreCase("y") ||
                stringValue.equalsIgnoreCase("true") ||
                stringValue.equalsIgnoreCase("on") ||
                stringValue.equalsIgnoreCase("1")) {
            defaultValue = "Y";
        }
        return defaultValue;
    }
    
    /* This Overlaoded method converts a boolean object to a String Object
     * @param value holds boolean value
     * @return string object.This is written for on-offCampusFlag
     *If the value is true then set it N
     *If the value is false F
     */
    private String booleanConvertion(Object value, String type) {
        String defaultValue = "F";
        if (value == null) {
            return defaultValue;
        }
        
        if(type!= null && type.equals("onOffCampusFlag")){
            String stringValue = value.toString();
            if (stringValue.equalsIgnoreCase("yes") ||
                    stringValue.equalsIgnoreCase("y") ||
                    stringValue.equalsIgnoreCase("true") ||
                    stringValue.equalsIgnoreCase("on") ||
                    stringValue.equalsIgnoreCase("1")) {
                defaultValue = "N";
            }
        }
        return defaultValue;
    }
    
    /* This method converts a String object to a Timestamp
     * @param strTimeStamp holds TimeStamp string format
     * @return java.sql.Timestamp
     */
    private java.sql.Timestamp getTimeStampForString(Object strTimeStamp) {
        java.sql.Timestamp timeStamp = null;
        //DateUtils dateUtils = new DateUtils();
        
        if(strTimeStamp == null) {
            return timeStamp;
        }
        
        if(strTimeStamp instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp)strTimeStamp;
        }
        
        try {
            // Commnted for the Timestamp value
            //timeStamp = new java.sql.Timestamp(dateUtils.getSQLDate(strTimeStamp.toString()).getTime());
            //Timestamp is stored as String. convert to java.sql.Timestamp
//                String TIME_STAMP = "yyyy-MM-dd";
//                String REQUIRED_DATE_FORMAT = TIME_STAMP +" hh:mm:ss.S";
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
//                Date date = simpleDateFormat.parse(value.toString());
            
            if(strTimeStamp.equals("")){
                timeStamp = null;
            }else{
                timeStamp = Timestamp.valueOf(value.toString());
            }
            
        }catch (Exception exception) {
            timeStamp = null;
        }
        return timeStamp;
    }
    
    /* This method converts a String object to a Date Object
     * @param value holds Date string format
     * @return java.sql.Date
     */
    private java.sql.Date getDateForString(Object objDate) {
        java.sql.Date date = null;
        DateUtils dateUtils = new DateUtils();
        
        if(objDate == null) {
            return date;
        }
        
        if(objDate instanceof java.sql.Date) {
            return (java.sql.Date)objDate;
        }else if(objDate instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date)objDate).getTime());
        }
        
        try {
            date = dateUtils.getSQLDate(objDate.toString());
        }catch (ParseException parseException) {
            date = null;
        }
        return date;
    }
    
    public static void main(String s[]) {
        try{
            String TIME_STAMP = "yyyy-MM-dd";
            String REQUIRED_DATE_FORMAT = TIME_STAMP +" hh:mm:ss.S";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
            Date date = simpleDateFormat.parse("2004-05-03 10:10:56.0");
            Timestamp timeStamp = new Timestamp(date.getTime());
            System.out.println("OK");
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    
}


