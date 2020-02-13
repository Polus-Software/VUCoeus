/*
* @(#) DBEngineFactory.java 1.0 03/14/2002
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/
package edu.mit.coeus.utils.dbengine;

/**
 * Class used to store all constants and static variables
 *
 * @version 1.0 March 14, 2002, 4:26 PM
 * @author  Geo Thomas
 */
public class DBEngineConstants {


    public static final String DIRECTION_IN = "in";
    public static final String DIRECTION_IN_OUT = "inout";
    public static final String DIRECTION_OUT = "out";

    public static final String TYPE_STRING = "String";
    public static final String TYPE_DATE = "Date";
    public static final String TYPE_TIMESTAMP = "Timestamp";
    public static final String TYPE_TIME = "Time";
    public static final String TYPE_INT = "int";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_FLOAT_OBJ = "FLOAT_OBJ";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_DOUBLE_OBJ = "DOUBLE_OBJ";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_STRING_OUT = "STRING";
    public static final String TYPE_RESULTSET = "RESULTSET";
    public static final String TYPE_BLOB = "Blob";
    public static final String TYPE_CLOB = "Clob";
    public static final String TYPE_BINARY = "Binary";
    public static final String TYPE_LONG = "Long";

    public static final String kStrOut = "OUT";
    public static final String kStrParamlist = "paramlist";
    public static final String kStrSql = "sql";
    public static final String kStrCall = "call";
    public static final String kStrSet = "SET";
    public static final String kStrSqlCommLeftDel = "<<";
    static final String kStrSqlCommRightDel = ">>";

    /*
     *  constantes defined for coeusDB.Properties file.
     */

    public static final String kStrDBDriver = "Driver";
    public static final String kStrDBDataSourceName = "DataSourceName";
    public static final String kStrDBLogFile = "logfile";
    public static final String kStrDBUserName = "UserName";
    public static final String kStrDBPassword = "Password";
    public static final String kStrDBRequestDB = "RquestDB";
    public static final String kStrDBNoOfNodes = "NoOfNodes";
    public static final String kStrDBDriverUrl = "JDBCDriverUrl";
    public static final String kStrLockWaitInterval = "WaitInterval";
    
    private static int lockInterval;
    /** Creates new DBEngineFactory */
    public DBEngineConstants() {
    }
    /**
     *  Method to set the interval for releasing the lock
     *  @param String interval
     */
    static void setLockInterval(String interval){
        try{
            lockInterval = Integer.parseInt(interval);
        }catch(NumberFormatException numEx){
            lockInterval = 15;
        }
    }
    
    /**
     *  Method to get the interval for releasing the lock
     *  @return int interval
     */
    static int getLockInterval(){
        return lockInterval;
    }
}