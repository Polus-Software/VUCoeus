/*
* @(#)Parameter.java 1.0 03/14/2002
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/
package edu.mit.coeus.utils.dbengine;

import java.util.Vector;
/**
 * Class used to define parameters for the procedures
 *
 * @version 1.0 March 14, 2002, 12:23 PM
 * @author  Geo Thomas
 */
public class Parameter {
    
    static final int TYPE_TIME = 0;
    static final int TYPE_DATE = 1;
    static final int TYPE_TIMESTAMP = 2;
    private final String kStrDirIn = DBEngineConstants.DIRECTION_IN;
    private final String kStrDirInOut = DBEngineConstants.DIRECTION_IN_OUT;
    private final String kStrDirOut = DBEngineConstants.DIRECTION_OUT;
    private final String kStrTypeString = DBEngineConstants.TYPE_STRING;
    private final String kStrTypeTimestamp = DBEngineConstants.TYPE_TIMESTAMP;
    private final String kStrTypeDate = DBEngineConstants.TYPE_DATE;
    private final String kStrTypeTime = DBEngineConstants.TYPE_TIME;
    private final String kStrTypeInt = DBEngineConstants.TYPE_INT;
    private final String kStrTypeFloat = DBEngineConstants.TYPE_FLOAT;
    private final String kStrTypeFloatObj = DBEngineConstants.TYPE_FLOAT_OBJ;
    private final String kStrTypeDouble = DBEngineConstants.TYPE_DOUBLE;
    private final String kStrTypeDoubleObj = DBEngineConstants.TYPE_DOUBLE_OBJ;
    private final String kStrTypeInteger = DBEngineConstants.TYPE_INTEGER;
    private final String kStrTypeSTRING = DBEngineConstants.TYPE_STRING_OUT;
    private final String kStrTypeResSet = DBEngineConstants.TYPE_RESULTSET;
    private final String kStrTypeBlob = DBEngineConstants.TYPE_BLOB;
    private final String kStrTypeBinary = DBEngineConstants.TYPE_BINARY;
    private final String kStrTypeClob = DBEngineConstants.TYPE_CLOB;
    private final String kStrTypeLong = DBEngineConstants.TYPE_LONG;
    
    private Vector vectType;
    private Vector vectDir;
    private String name;
    private String type;
    private Object value;
    private String direction;
    private int dateType;
    
    private void init(){
        vectType = new Vector(14,1);
        vectDir = new Vector(3,0);

        vectType.addElement(kStrTypeString);
        vectType.addElement(kStrTypeDate);
        vectType.addElement(kStrTypeTimestamp);
        vectType.addElement(kStrTypeTime);
        vectType.addElement(kStrTypeInt);
        vectType.addElement(kStrTypeFloat);
        vectType.addElement(kStrTypeFloatObj);
        vectType.addElement(kStrTypeDouble);
        vectType.addElement(kStrTypeDoubleObj);
        vectType.addElement(kStrTypeBlob);
        vectType.addElement(kStrTypeBinary);
        vectType.addElement(kStrTypeSTRING);
        vectType.addElement(kStrTypeResSet);
        vectType.addElement(kStrTypeInteger);
        vectType.addElement(kStrTypeLong);
        vectType.addElement(kStrTypeClob);

        vectDir.addElement(kStrDirIn);
        vectDir.addElement(kStrDirInOut);
        vectDir.addElement(kStrDirOut);
    }
    /**
     *  Four argument Constructor for creating a Parameter object
     *  @param String Name
     *  @param String Type
     *  @param Object Value
     *  @param String direction
     *  @exception DBException
     */
    public Parameter(String nm, String typ, Object val,String dirn) throws DBException{
        name = nm;
        value = val;
        init();
        if (vectDir.contains(dirn.toLowerCase())){
            direction=dirn.toLowerCase();
        }else{
            throw new DBException("Error: Parameter Class does not allow direction other than in, inout,out ");
        }
        if (vectType.contains(typ)){
            type = typ;
            setType();
        }else{
            throw new DBException("Error: Parameter Class does not allow types other than String, int, float, Double & Binary");
        }
    }
    /**
     *  Three argument Constructor for creating a Parameter object
     *  @param String Name
     *  @param String Type
     *  @param Object Value
     *  @exception DBException
     */
    public Parameter(String nm, String typ, Object val) throws DBException{
        name = nm;
        value = val;
        direction = kStrDirIn;
        init();
        if (vectType.contains(typ)){
            type = typ;
            setType();
        }else{
            throw new DBException("Error: Parameter Class does not allow types other than String, Date, int, float & Double");
        }
    }
    private void setType(){
        if (type.equals(kStrTypeDate)){
            if(value instanceof java.sql.Timestamp){
                this.dateType = TYPE_TIMESTAMP;
            }else if(value instanceof java.sql.Date){
                this.dateType = TYPE_DATE;
            }else if(value instanceof java.sql.Time){
                this.dateType = TYPE_TIME;
            }
        }
    }
    /**
     *  Get the direction
     *  @return String Direction
     */
    public String getDirection() {
        return direction;
    }
    /**
     *  Get the Name
     *  @return String Name
     */
    public String getName() {
        return name;
    }
    /**
     *  Get the type
     *  @return String Type
     */
    public String getType() {
        return type;
    }
    /**
     *  Get the date type
     *  @return String date type
     */
    public int getDateType() {
        return dateType;
    }
    /**
     *  Get the long value if the type is "Long"
     *  @return double Long value
     */
    public double getLongValue() {
        if (value!=null && value instanceof String && type.equals(kStrTypeLong)){
            return (Long.valueOf((String)value)).longValue();
        }
        else
            return -1;
    }
    /**
     *  Get the String value if the type is "String"
     *  @return String value
     */
    public String getStrValue() {
        if (value!=null && value instanceof String && type.equals(kStrTypeString))
            return (String)value;
        else
            return null;
        
    }
    /**
     *  Get the <code>java.sql.Date</code> value if the type is "Date"
     *  @return Date value
     */
    public java.sql.Date getDateValue() throws DBException{
        if (value!=null && (type.equals(kStrTypeDate))){
            /*if(value instanceof java.sql.Timestamp){
                return (java.sql.Timestamp)value;
            }else
             */
            if(value instanceof java.sql.Date){
                return (java.sql.Date)value;
            }else {
                String exMsg = "Wrong type:\n Expected type of the "+value+
                                "is : java.sql.Date\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the <code>java.sql.Timestamp</code> value if the type is "Timestamp"
     *  @return Timestamp Timestamp value
     */
    public java.sql.Timestamp getTimestampValue() throws DBException{
        if (value!=null && (type.equals(kStrTypeTimestamp) || type.equals(kStrTypeDate))){
            if(value instanceof java.sql.Timestamp){
                return (java.sql.Timestamp)value;
            }else {
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                "is : java.sql.TimeStamp\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the <code>java.sql.Time</code> value if the type is "Time"
     *  @return Time Time value
     */
    public java.sql.Time getTimeValue() throws DBException{
        if (value!=null && (type.equals(kStrTypeTime) || type.equals(kStrTypeDate))){
            if(value instanceof java.sql.Time){
                return (java.sql.Time)value;
            }else {
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                "is : java.sql.Time\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the int value if the type is "int"
     *  @return int value
     */
    public int getIntValue() throws DBException{
        if(value==null)
            return -1;
        if ((value instanceof Integer || value instanceof String) && type.equals(kStrTypeInt)){
            return Integer.parseInt(value.toString());
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.String or java.lang.Integer\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }   
//        return -1;
    }
    /**
     *  Get the float value if the type is "float"
     *  @return float value
     */
    public float getFloatValue() throws DBException{
        if(value==null)
            return -1;
        if ((value instanceof Float  || value instanceof String) && type.equals(kStrTypeFloat)){
            return (Float.valueOf(value.toString())).floatValue();
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.String or java.lang.Float\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }
//        return -1;
    }
    /**
     *  Get the Float value if the type is "Float"
     *  @return Float value
     */
    public Float getFloatObjValue() throws DBException{
        if(value==null)
            return null;
        if (value instanceof Float && type.equals(kStrTypeFloatObj)){
            return (Float)value;
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.Float\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }
    }
    /**
     *  Get the double value if the type is "double"
     *  @return double value
     */
    public double getDoubleValue() throws DBException{
        if(value==null)
            return -1;
        if ((value instanceof Double || value instanceof String )&& type.equals(kStrTypeDouble)){
            return (Double.valueOf(value.toString())).doubleValue();
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.String or java.lang.Double\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Double value if the type is "Double"
     *  @return Double value
     */
    public Double getDoubleObjValue() throws DBException{
        if(value==null)
            return null;
        if (value instanceof Double && type.equals(kStrTypeDoubleObj)){
            return (Double)value;
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.Double\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Integer value if the type is "Integer"
     *  @return Integer value
     */
    public Integer getIntegerValue() throws DBException{
        if(value==null)
            return null;
        if (value instanceof Integer && type.equals(kStrTypeInteger)){
            return (Integer)value;
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.Integer\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Object if the type is "Blob"
     *  @return Object value
     */
    public Object  getBlobValue() {
        if (value!=null && !(value instanceof String)  && type.equals(kStrTypeBlob) ){
            return value;
        }
        else
            return null;
    }
    /**
     *  Get the Object if the type is "Clob"
     *  @return Object value
     */
    public String  getClobValue() {
        if (value!=null && type.equals(kStrTypeClob) ){
            return value.toString();
        }else
            return null;
    }
    /**
     *  Get the Object if the type is "Binary"
     *  @return Object value
     */
    public Object  getBinaryValue() {
        if (value!=null && !(value instanceof String)  && type.equals(kStrTypeBinary) ){
            return value;
        }
        else
            return null;
    }
    /**
     *  Get the Object
     *  @return Object value
     */
    public Object  getValue() {
        return value;
    }
}