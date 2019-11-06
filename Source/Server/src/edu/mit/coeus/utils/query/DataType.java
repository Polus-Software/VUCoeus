/*
 * DataType.java
 *
 * Created on November 7, 2003, 1:58 PM
 */

package edu.mit.coeus.utils.query;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import java.lang.reflect.*;

public final class DataType {
    
    private int primitiveInt;
    private char primitiveChar;
    private float primitiveFloat;
    private double primitiveDouble;
    private boolean primitiveBoolean;
    
    public static final String INT = "primitiveInt";
    public static final String CHAR = "primitiveChar";
    public static final String FLOAT = "primitiveFloat";
    public static final String DOUBLE = "primitiveDouble";
    public static final String BOOLEAN = "primitiveBoolean";

    /** Creates a new instance of DataType */
    private DataType() {
    }
    
    public static Class getClass(String type) {
        try{
            return DataType.class.getDeclaredField(type).getType();
        }catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
            return null;
        }
    }
    
    public static void main(String s[]) {
        System.out.println(DataType.getClass(DataType.BOOLEAN));
    }
    
}
