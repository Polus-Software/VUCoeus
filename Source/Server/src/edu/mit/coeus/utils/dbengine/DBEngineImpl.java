/*
* @(#)DBEngineImpl.java 1.0 03/14/2002
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/
package edu.mit.coeus.utils.dbengine;

import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.HashMap;

import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.Connection;
import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;

import edu.mit.coeus.utils.UtilFactory;
import java.io.InputStream;
/**
 * The class used to perform all database transactions in a generic manner.
 * It uses a singleton class,<code>DBConnectionManager</code> to get the connection object.
 * The methods in this class will be used for executing statndard SQL query,
 * executing stored functions, executing strored procedures and executing more than one
 * stored procedures in a transaction.
 * @see DBConnectionManager
 *
 * @version 1.0 March 14, 2002, 10:31 PM
 * @author  Geo Thomas
 */
public class DBEngineImpl {
    // This Dictionary stores the RequestMap and its associated DataBase SQL commands.
    private Hashtable requestList;
    // This Dictionary stores all the User Defined Error Messages.
    private Hashtable errorList; 

    // The DBEngineImpl instance
    private DBConnectionManager dbConnectionManager;
    
    private final String kStrDirIn = DBEngineConstants.DIRECTION_IN;
    private final String kStrDirInOut = DBEngineConstants.DIRECTION_IN_OUT;
    private final String kStrDirOut = DBEngineConstants.DIRECTION_OUT;
    private final String kStrTypeString = DBEngineConstants.TYPE_STRING;
    private final String kStrTypeDate = DBEngineConstants.TYPE_DATE;
    private final String kStrTypeTimestamp = DBEngineConstants.TYPE_TIMESTAMP;
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
    private final String kStrTypeLong = DBEngineConstants.TYPE_LONG;

    private final String kStrCall = DBEngineConstants.kStrCall;
    private final String kStrSet = DBEngineConstants.kStrSet;
    private final String kStrSql = DBEngineConstants.kStrSql;
    //Code commented and modified for handling the out text in the procedure and function names
    //and its parameters
    //private final String kStrOut = DBEngineConstants.kStrOut;
    private final String kStrOut = DBEngineConstants.kStrOut+" ";
    private final String kStrParamlist = DBEngineConstants.kStrParamlist;
    private final String kStrSqlCommLeftDel = DBEngineConstants.kStrSqlCommLeftDel;
    private final String kStrSqlCommRightDel = DBEngineConstants.kStrSqlCommRightDel;

//    private UtilFactory UtilFactory;
    /**
     *  No argument private constructor.
     *  Creates DBEngineImpl instance.
     */
     public DBEngineImpl(){
//        UtilFactory = new UtilFactory();
        dbConnectionManager = DBConnectionManager.getInstance();
     }
    /**
     * Method used to execute stored functions in the database
     * It will take the connection from the connection pool, which has created 
     * with the given datasource and executes the function, 
     * which has specified in the SQLcommand.
     *
     * <li>The format for the SQLCommand is as follows
     * <li><code>{ &lt;&lt;OUT datatype out_param_name&gt;&gt; = call funName ( 
     * &lt;&lt;in_parameter_name_1&gt;&gt;,&lt;&lt;in_parameter_name_2&gt;&gt;,
     * ...,&lt;&lt;in_parameter_name_n&gt;&gt;) }</code>
     * for example:- { &lt;&lt;OUT INTEGER rowCount&gt;&gt; = 
     * call fn_is_valid_award_num ( &lt;&lt;MIT_AWARD_NUMBER&gt;&gt;) }
     * The Vector paramList should conatain a list of Parameter class objects,
     * which should have all details about parameters that specified in the SQLCommand.
     * After the execution of the <code>stored function</code>, 
     * the <code>resultset</code> will be populated in a <code>vector</code>
     * as a list <code>hashtable</code>, in which each hashtable instance will 
     * represent a row in the resultset.
     * @param String Data source name
     * @param String SQLCommand
     * @param Vector Parameter list
     * @return Vector result hashtable list
     * @exception DBException
     * @see Parameter
     */
    public Vector executeFunctions(String DataSourceName,String SQLCommand, 
            Vector paramList) throws DBException{
        Vector result = new Vector(5,3);
        Connection conn = null;
        try{
            /*  Properly closing connection in finally block. rollback in Throwable block
                so that both exception and error can be caught.
             *  by Geo on 03-06-2007
             */
            conn = beginTxn();
            result = executeFunctions(DataSourceName, SQLCommand, paramList, conn);
//            endTxn(conn); //#1
        }catch(DBException ex){
            rollback(conn);
            throw ex;
        }catch(Exception ex){
            rollback(conn);
            throwDBError(ex);//#2
        }finally{
            endTxn(conn);
        }
        return result;
    }
    /**
     * Method used to exceute more than one stored procedures in a transaction.
     * This method accepts a <code>vector</code> as the argument, which has
     * the list of <code>ProcReqParameters</code>.
     * Before calling this method, caller has to create ProcReqParameter class
     * with DSN name, SQLCommand and vector of Parameter class for each procedure. 
     * Add ProcReqParameter class instances into a vector and pass this vector 
     * as an argument to execute all the procedures.
     *
     * It executes all procedures in the vector and returns overall resultant as a vector.
     * if input argument vector has more than one procedure to execute,
     * <li> It returns Vector of vectors, in which each inner vector contains a 
     * list of hashtable as the resultant of each stored procedure.
     * If any procedures failed to execute, the whole transaction will be rolledback.
     * Trancation will supply commit only when all the procedures executed successfully.
     *
     * <li>The format for the SQLCommand is as follows
     * <li><code> call procName ( &lt;&lt;in_parameter_name_1&gt;&gt;,&lt;&lt;
     * in_parameter_name_2&gt;&gt;,...,&lt;&lt;in_parameter_name_n&gt;&gt; ) </code>
     * for example:-  &lt;&lt;OUT INTEGER rowCount&gt;&gt; = call dw_get_award_info ( 
     * &lt;&lt;MIT_AWARD_NUMBER&gt;&gt;) 
     * The Vector paramList should conatain a list of Parameter class objects,
     * which should have all details about parameters that specified in the SQLCommand.
     *
     *  @param Vector Procedure list
     *  @return Vector result, hashtable list
     *  @exception DBException
     */
    public Vector executeStoreProcs(Vector vectProcedures) throws DBException{
        Vector result = new Vector(5,3);
        Connection conn = null;
        try{
            /*  Properly closing connection in finally block. rollback in Throwable block
                so that both exception and error can be caught.
             *  by Geo on 03-06-2007
             */
            conn = beginTxn();
            result = executeStoreProcs(vectProcedures, conn);
//            endTxn(conn); #1
        }catch(Throwable ex){//#2
            rollback(conn);
            //Added by Vyjayanthi for IRB Enhancement - 13/08/2004 - Start
            //To throw a DBException
            if( ex instanceof DBException ){
                throw (DBException)ex;
            }
            //Added by Vyjayanthi for IRB Enhancement - 13/08/2004 - End
            throwDBError(ex);//#3
        }finally{
            endTxn(conn);//#4
        }
        return result;
    }
    
    /**
     * Method used to execute the stored procedure.
     * It executes the procedure which is specified in the SQL Command and 
     * returns resultant as a vector.
     * <li> It returns a vector, which contains a list of hashtables as the resultant.
     *
     * <li>The format for the sqlCommand is as follows
     * <li><code> call procName ( &lt;&lt;in_parameter_name_1&gt;&gt;,
     * &lt;&lt;in_parameter_name_2&gt;&gt;,...,&lt;&lt;in_parameter_name_n&gt;&gt;)</code>
     * for example:-  &lt;&lt;OUT INTEGER rowCount&gt;&gt; = call dw_get_award_info (
     * &lt;&lt;MIT_AWARD_NUMBER&gt;&gt;)
     * The Vector paramList should conatain a list of Parameter class objects,
     * which should have all details about parameters that specified in the sqlCommand.
     *
     *  @param String service name
     *  @param String sqlCommand
     *  @param String data source
     *  @param Vector Parameter list
     *  @return Vector hashtable list
     *  @exception DBException
     */
    public Vector executeRequest(String serviceName, String sqlCommand, 
            String dataSource, Vector paramList) throws DBException{
        Vector vectRset = null;          
        Connection conn = null;
        try{
            /*  Properly closing connection in finally block. rollback in Throwable block
                so that both exception and error can be caught.
             *  by Geo on 03-06-2007
             */

            conn = beginTxn();
            vectRset = executeRequest(serviceName , sqlCommand, dataSource, paramList, conn);            
//            endTxn(conn);#1
        }catch(DBException ex){
            rollback(conn);
            throw ex;
        }catch(Throwable ex){//#2
            rollback(conn);
            throwDBError(ex);
        }finally{
            endTxn(conn);//#3
        }
        return vectRset;
    }
    /**
     *  This is an oveloaded method to execute standard sql queries which 
     *  does not have any input parameters.
     *  @param String sqlCommand
     *  @param String data sourceAb
     *  @return Vector hashtable list
     *  @exception DBException
     */
    public Vector executeRequest(String dataSource, String sqlCommand) 
            throws DBException{
        Vector vectRset = null;                
        Connection conn = null;
        vectRset = executeRequest(dataSource, sqlCommand, conn);
        return vectRset;
    }
    
    /**
     * Method used to execute the standard SQL. The std sql
     * are select, insert,update. The format is select col1,col2...
     * from table1 a,table2 b,..
     * where a.colx1=b.coly1 ....
     * and a.colz1= ?param1 and b.colz2= ?param2 ...
     * This always returns result set( Incase of update,insert --- nothing is returned)
     * Again the result is packaged as a Vector.
     *  @param String Data source name
     *  @param String sqlCommand
     *  @param Vector Parameter list
     *  @return Vector hashtable list
     *  @exception DBException
     */
    private Vector executeStdSql(String dataSourceName,String sqlCommand, 
            Vector paramList) throws DBException{
          Connection conn = null;
          Vector result = null;
          try{
            /*  Properly closing connection in finally block. rollback in Throwable block
                so that both exception and error can be caught.
             *  by Geo on 03-06-2007
             */
              
                conn = beginTxn();
                result = executeStdSql(dataSourceName, sqlCommand, paramList, conn);
//                endTxn(conn);//#1
            }catch(DBException ex){
                rollback(conn);
                throw ex;
            }catch(Throwable ex){
                rollback(conn);
                throwDBError(ex);//#2
            }finally{
                endTxn(conn);//#3
            }
          return result;
    }
    /**
     *  Method used to execute prepared queries.
     *  @param String sqlCommand
     *  @param String data source
     *  @param Vector Parameter list 
     *  @return Vector hashtable list
     *  @exception DBException
     */
    public Vector executePreparedQuery(String dataSourceName,String sqlCommand, 
            Vector paramList) throws DBException{
        Connection conn = null;
        Vector result = null;                
        try{
            /*  Properly closing connection in finally block. rollback in Throwable block
                so that both exception and error can be caught.
             *  by Geo on 03-06-2007
             */
            
            conn = beginTxn();
            result = executePreparedQuery(dataSourceName, sqlCommand, paramList, conn);
//            endTxn(conn);//#1
        }catch(DBException ex){
            rollback(conn);
            throw ex;
        }catch(Exception ex){
            rollback(conn);
            throwDBError(ex);//#2
        }finally{
            endTxn(conn);//#3
        }
        return result;
    }
    /**
     *  Method used to execute prepared queries.
     *  @param String sqlCommand
     *  @param String data source
     *  @param Vector Parameter list
     *  @return Vector hashtable list
     *  @exception DBException
     */
//    public boolean insertBlob(String dataSourceName,String sqlCommand, 
//            Vector paramList, String sltLocator, byte[] stream, 
//                java.sql.Timestamp dbStamp) 
//                    throws DBException{
//        Connection conn = null;
//        boolean isUpdated = false;
//        try{
//            conn = beginTxn();
//            isUpdated = insertBlob(dataSourceName, sqlCommand, paramList, sltLocator, stream, dbStamp, conn);
//            endTxn(conn);
//        }catch(DBException ex){
//            rollback(conn);
//            throw ex;
//        }catch(Exception ex){
//            rollback(conn);
//            throwDBException(ex);
//        } 
//        return isUpdated;
//    }
    /**
     *  This function parses a Request String for ?Holders strings and 
     *  replaces with run time values
     *  For example, this function will replace a string of the format
     *  exec getCustomerDetails ?name, ?phone
     *  with values for name and phone as follows
     */
    private String parseAndReplace(String sqlString, Vector paramList)
            throws DBException{
        String tempHolder = new String(sqlString);
        int pos=tempHolder.indexOf("<<");
        if (pos == -1)
            return tempHolder;
        while (!paramList.isEmpty()) {
            Parameter param = (Parameter) paramList.elementAt(0);
            // Removes the first element from the list
            paramList.removeElementAt(0); 
            // Get the parameter name for parsing into the requestList
            String fillNode = new String("<<" + param.getName().trim()+">>"); 
            int strPos = tempHolder.indexOf(fillNode);
            if (strPos == -1)
                throw new DBException("Error:: Unable to find string - <" + 
                            fillNode + "> in RequestString -> " + sqlString);
            if (param.getType().equals(kStrTypeString) ) {
                tempHolder = tempHolder.substring(0,strPos-1) + " '" + 
                    param.getStrValue() +"' " + tempHolder.substring(
                                strPos+fillNode.length(),tempHolder.length());
            }else if (param.getType().equals(kStrTypeInt) ){
                tempHolder = tempHolder.substring(0,strPos-1) + " "+
                    param.getIntValue() +" " + tempHolder.substring(
                                strPos+fillNode.length(),tempHolder.length());
            }else if (param.getType().equals(kStrTypeFloat) ){
                tempHolder = tempHolder.substring(0,strPos-1) + " "+
                    param.getFloatValue() +" " + tempHolder.substring(
                                strPos+fillNode.length(),tempHolder.length());
            }else if (param.getType().equals(kStrTypeDouble) ){
                tempHolder = tempHolder.substring(0,strPos-1) + " "+
                    param.getDoubleValue() +" " + tempHolder.substring(
                                strPos+fillNode.length(),tempHolder.length());
            }
        }
        return tempHolder;
    }
    /**
     *
     */
    private String getKeyForLine(String str){
        int index = str.indexOf("=");
        return str.substring(0,index-1).trim();
    }
    private String getValueForLine(String str){
        int index = str.indexOf("=");
        return str.substring(index+1,str.length()).trim();
    }
    /**
     ** This is a generic function that parses a string and extracts the key and the value
     ** It expects the format to be in key = value
     */
    private void getKeyAndValueForLine(String str, String key, String value) 
            throws DBException{
        int index = str.indexOf("=");
        value= "" +str.substring(index+1,str.length()).trim();
        if ((key.equals("")) || (value.equals("")) )
            throw new DBException("Parse Error inside getKeyAndValueForLine."+
                    "String supplied = " + str);
    }
    /**
     * The result is packaged as a vector.
     * Extract the result set and place each row in a hashtable with key as ColumnName
     * and value as it's column value.
     * @param ResultSet resultset
     * @return Vector list of hashtable.
     */
    private Vector packageResult(ResultSet rset) throws DBException{
        Vector vectResSet = new Vector(5,3);
        try{
            java.sql.ResultSetMetaData metaData= rset.getMetaData();
            int colCount = metaData.getColumnCount();
            String colName = null;
            Object colValue = null;
            while(rset.next()){
                HashMap htRow = new HashMap();
                for(int i=1;i<=colCount;i++){
                    colName = metaData.getColumnName(i);
                    //colValue = rset.getObject(i);
                    int colDatatype = metaData.getColumnType(i); // get column data type
                    colName = metaData.getColumnName(i);
                    switch(colDatatype) {
                        /*
                         *  Commented by geo to work with BLOB objects
                         *  extract the blob object from the resultset,
                         *  get the input stream, convert that to byte stream and
                         *  attach with the resultant hashmap.
                         */
                        //case java.sql.Types.LONGVARCHAR: // data type is LONG RAW
                        case java.sql.Types.BLOB: // data type is BLOB
                            try {
                                ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
                                if(colDatatype == java.sql.Types.LONGVARCHAR) { // data type is LONG RAW
                                    
                                }else { // data type is BLOB
                                    Blob binaryData = null;
                                    binaryData = rset.getBlob(i);
                                    //Null check code added for blob data.
                                    if(binaryData != null){
                                        bytesOut.write(binaryData.getBytes(1, (int) binaryData.length()));
                                    }
                                }
                                bytesOut.flush();
                                bytesOut.close();
                                colValue = bytesOut;
                                break;
                            }catch(Exception Ex){
                                throwDBException(Ex);
                            }
                        case java.sql.Types.CLOB:
                            try {
                                java.sql.Clob characterData = null;
                                characterData = rset.getClob(i);
                                if(characterData != null && characterData.length()>0) {
                                    char charData[] = new char[(int)characterData.length()];
                                    characterData.getCharacterStream().read(charData);
                                    colValue = new String(charData);
                                }else {
                                    colValue = null;
                                }
                                break;
                            }catch(Exception Ex){
                                throwDBException(Ex);
                            }
                            break;
                        case java.sql.Types.DATE:
                            colValue = rset.getTimestamp(i);
                            break;
                        case java.sql.Types.TIMESTAMP:
                            colValue = rset.getTimestamp(i);
                            break;
                        default: // data type OTHERS
                            colValue = rset.getObject(i);
                            break;
                    }
                    htRow.put(colName,colValue);
                }
                vectResSet.add(htRow);
            }
        }catch(SQLException sqlEx){
            throwDBException(sqlEx);
        }finally{
            try{
                if(rset!=null){
                    rset.close();
                }
            }catch(SQLException sqlEx){
                throwDBException(sqlEx);
            }
        }
        return vectResSet;
    }
    /**
     * The hashtable packaged in a vector.
     * @param Hashtable Result
     * @return Vector result
     */
    private Vector packageResult(HashMap htRset){
            Vector vectResult = new Vector(3,2);
            vectResult.add(htRset);
            return vectResult;
    }
    private void throwDBError(Throwable ex) throws DBException{
        UtilFactory.log(ex.getMessage(),ex,"DBEngineImpl","throwDBError");
        throwDBException(new DBException(ex.getMessage()));
    }
    /**
     * Throw the exception , type DBException.serialVersionUID
     * DBException has error id 0 if it is local DBEngine error(especially language error)
     * DBException has errir id 1 if it is a JDBC error
     * and  has error id >1 if it is data base error
     * The user message is a fixed for cases 0,1 and also 
     * as show below for case where oracode is <20000
     */
    private void throwDBException(Exception ex) throws DBException{
        try{
//            Commented for case#4197
//            UtilFactory.log(ex.getMessage(),ex,"DBEngineImpl","throwDBException");
          /*
            if the exception is of type SQLException, there are possibly two sources
            One is jdbc error(error code is 0), second is database error(error code >0)
            if the exception is JDBC related, then DBException has errorId of 1,
            User Message is "Driver error".
            if the exception is database related, error id is Oracode and 
            then these are possible scenarios
              1.ORA code <20000, it is internal datbase error, 
                then userMessage is "Internal database error"
              2.ORA code  >20000, then it is application related and 
                user Message is obtained from user message table for the application
           */
            if (ex instanceof SQLException) {
                int oraCode=((SQLException)ex).getErrorCode();
                //Get the user message from user_error_message table
                if(oraCode ==0){
                    throw new DBException(oraCode,"Contact Administrator, Driver error",ex);
                }
                //Added for case#3183 - Proposal Hierarchy - starts
                //To display the error message that is thrown from DB.
                //If the exception code is set as 20000 with some custom message,
                //that message alone will be displayed to the user.
                else if(oraCode == 20000){
                    String message = ex.getMessage();
                    String code = new Integer(oraCode).toString()+":";
                    message = message.substring(message.indexOf(code)+ code.length()); 
                    String userMessage= message.substring(0, message.indexOf("ORA")).trim();
                    throw new DBException(oraCode,userMessage,ex);
            	}
                //Added for case#3183 - Proposal Hierarchy - ends
                else if(oraCode >= -20100 & oraCode < 20100){
                    //Added by Vyjayanthi for IRB Enhancement - 13/08/2004
                    //To set the error code in case Review Comments are modified by another user
                    throw new DBException(oraCode, ex.getMessage() ,ex);
                }else if (oraCode >20000){
		    String userMessage= errorList==null?ex.getMessage():(String)errorList.get(""+oraCode);
                    throw new DBException(oraCode,userMessage,ex);
                }else if(oraCode==2292) {
                    throw new DBException(oraCode,"dbEngine_intl_error_child_record_exceptionCode.3333",ex);
                }else{
                    throw new DBException(oraCode,"Contact Adminstrator,Internal Database Error",ex);
                }
            }else {
                throw new DBException(0,"Contact Adminstrator,DB Engine Application error",ex);
            }
        }catch(DBException e){
            throw e;
        }catch(Exception e){
            throw new DBException(0,"At DBEngine.throwException.Fatal error,Unknown error \n",e);
        }
    }
    
    /**
     * Throw the exception , type DBException.serialVersionUID
     * DBException has error id 0 if it is local DBEngine error(especially language error)
     * DBException has errir id 1 if it is a JDBC error
     * and  has error id >1 if it is data base error
     * The user message is a fixed for cases 0,1 and also as show below for case 
     * where oracode is <20000
     */
    private void throwDBException(Exception ex,Vector params) throws DBException{
        try{
//            Commented for case#4197
//            UtilFactory.log(ex.getMessage(),ex,"DBEngineImpl","throwDBException");
          /*
            if the exception is of type SQLException, there are possibly two sources
            One is jdbc error(error code is 0), second is database error(error code >0)
            if the exception is JDBC related, then DBException has errorId of 1,
            User Message is "Driver error"
            if the exception is database related, error id is Oracode and 
            then these are possible scenarios
              1.ORA code <20000, it is internal datbase error, 
                then userMessage is "Internal database error"
              2.ORA code  >20000, then it is application related and 
                user Message is obtained from user message table for the application
           */
            String parameters = "";
            if (params != null) {
                for (int i=0; i <params.size(); i++) {
                    Parameter param = (Parameter)params.elementAt(i);
                    String value ="";
                    if(param.getType().equalsIgnoreCase(kStrTypeBinary)) {
                        value=param.getBinaryValue().toString();
                        //value=obj.toString();
                    }else{
                        value = param.getStrValue();
                    }
                    String name = param.getName();
                    parameters = parameters.concat(name+"->"+value +"\n");
                }
            }else{
                throw new DBException(0,
                    "Contact Adminstrator,DB Engine Application error, ParamList is null",ex);
            }
            if (ex instanceof SQLException) {
                int oraCode=((SQLException)ex).getErrorCode();
                //Gets the user message from user_error_message table
                if(oraCode ==0){
                    throw new DBException(oraCode,
                        "Contact Administrator, Driver error"+parameters,ex);
                }else if (oraCode >20000){
                    String userMessage= (String)errorList.get(""+oraCode);
                    throw new DBException(oraCode,userMessage+parameters,ex);
                }else {
                    throw new DBException(oraCode,
                        "Contact Adminstrator,Internal Database Error",ex);
                }
            }else {
                throw new DBException(0,"Contact Adminstrator,DB Engine Application error",ex);
            }
        }catch(DBException e){
            throw e;
        }catch(Exception e){
            throw new DBException(0,"At DBEngine.throwException.Fatal error,Unknown error",e);
        }
    }
    /**
     * Function returns a Parmeter object from the paramlist
     * where the object has the same name as paramName
     * @param String param name
     * @param Vector param list
     * @return Parameter 
     */
    private Parameter getParamFromParamList(String paramName,Vector paramList) 
            throws DBException{
        Parameter param=null;
        for (int j = 0; j < paramList.size() ; j++) {
            param= (Parameter)paramList.elementAt(j);
            if (param.getName().equals(paramName.trim())) {
                break;
            }
        }
        if (param.getName().equals(paramName.trim())){
            return param;
        }else{
            throw new DBException("Did not Supply the required Parameter >>"+ paramName);
        }
    }
    
    /**
     * Parse SQL command and parameter list and build a hashtable which has 
     * the prepared query string to execute and a Vector of having all the parameter values
     * for the prepared query
     * @param String SQLCommand
     * @param Vector paramList
     * @return Hashtable It has two elements, One is for prepared query string
     *                      and another is for Ordered paramter list
     */
    private Hashtable getSqlAndParamList(String sqlCommand,Vector paramList) 
                    throws DBException{
            Vector orderedParamList = new Vector();
            Hashtable core =new Hashtable();
            if (sqlCommand.indexOf(kStrSqlCommLeftDel)== -1 && 
                        sqlCommand.indexOf(kStrOut)== -1)   {
                core.put(kStrSql,sqlCommand);
                core.put(kStrParamlist, orderedParamList) ;
                return core;
            }
            String balCommand = sqlCommand;
            String correctedString="";
            String unparsedString="";
            int currentPos=0;
            int lastPos=0;
            String name="";
            currentPos=balCommand.indexOf(kStrSqlCommLeftDel);
            if (currentPos!= -1){
                if(currentPos==0){
                    //currentPos++;
                }
                correctedString+=balCommand.substring(0,currentPos)+"?";
                lastPos=balCommand.indexOf(kStrSqlCommRightDel);
                name=balCommand.substring(currentPos+2,lastPos);
                if (name.indexOf(kStrOut)!= -1){
                    StringTokenizer parseOut = new StringTokenizer(name);
                    // First token is always "OUT" which can be ignored
                    parseOut.nextToken();
                    String outParamType = parseOut.nextToken();
                    String outParamName = parseOut.nextToken();
                    //If the last token has a parenthesis , remove it
                    orderedParamList.addElement(new Parameter(outParamName,outParamType,"",kStrDirOut));
                }else{
                    Parameter param=getParamFromParamList(name,paramList);
                    orderedParamList.addElement(new Parameter(param.getName(),param.getType(),param.getValue(),"in"));
                }
                lastPos=lastPos+2;
                balCommand=balCommand.substring(lastPos,balCommand.length());
            }
            while (true){
                currentPos=balCommand.indexOf(kStrSqlCommLeftDel);
                if (currentPos== -1){
                    correctedString+=balCommand;
                    break;
                }
                correctedString+=balCommand.substring(0,currentPos)+"?";
                lastPos=balCommand.indexOf(kStrSqlCommRightDel);
                name=balCommand.substring(currentPos+2,lastPos);
                if (name.indexOf(kStrOut)!= -1){
                    StringTokenizer parseOut = new StringTokenizer(name);
                    parseOut.nextToken();
                    String outParamType = parseOut.nextToken();
                    String outParamName = parseOut.nextToken();
                    orderedParamList.addElement(new Parameter(outParamName,outParamType,"",kStrDirOut));
                }else{
                    Parameter param=getParamFromParamList(name,paramList);
                    orderedParamList.addElement(new Parameter(param.getName(),param.getType(),param.getValue(),param.getDirection().toLowerCase()));
                }
                lastPos=lastPos+2;
                balCommand=balCommand.substring(lastPos,balCommand.length());
            }
            core.put(kStrSql,correctedString);
            core.put(kStrParamlist, orderedParamList);
            return core;
    }
    
    public boolean getPDFBlob( String dataSourceName, String schID,
                                    String agendaNo, String strPath )
                                    throws DBException {
        boolean isBolbInserted = false;
        Connection conn = null;
        Vector result = null;
        ResultSet resultSet = null;
        PreparedStatement pstmt = null;
        try{
            //locks the respective pdf_store blob field.
            String selectQuery = "SELECT PDF_STORE FROM OSP$SCHEDULE_AGENDA " +                                 
                                 "WHERE SCHEDULE_ID =  '" + schID.trim() + "' " +
                                 " AND AGENDA_NUMBER =  " +  
                                 Integer.parseInt( agendaNo.trim() ) ;
            // Get a connection from the connection pool
            conn = dbConnectionManager.getConnection( dataSourceName );
            conn.setAutoCommit( false );
            // Create the statement object
            pstmt = conn.prepareStatement( selectQuery );
            resultSet = ( ResultSet )pstmt.executeQuery();
            if( resultSet.next() ){
                //get the Oracle Blob Object 
                Blob blob = (Blob)resultSet.getBlob(1);
                //transfer file binary data to db blob object.
                java.io.InputStream blobStream = blob.getBinaryStream();
                String fileName = strPath + java.io.File.separator + schID + "_" + agendaNo + ".pdf"; 
                // Open a file stream to save the Blob data 
                java.io.FileOutputStream fileOutStream = new java.io.FileOutputStream(fileName);
                // Read from the Blob data input stream, and write to the file output 
                // stream
                byte[] buffer = new byte[10]; 
                // buffer holding bytes to be transferred 
                int nbytes = 0;    
                // Number of bytes read
                while( (nbytes = blobStream.read(buffer)) != -1 ) // Read from Blob stream 
                    fileOutStream.write(buffer, 0, nbytes);      // Write to file stream
                // Flush and close the streams 
                fileOutStream.flush();
                fileOutStream.close();  
                blobStream.close();
                isBolbInserted = true;
            }
        }catch( Exception insertErr ){
            isBolbInserted = false ;
            throw new DBException(0,
                    "Insert Blob Exception-DBEngine.throwException.Fatal error",
                    insertErr );   
        }finally{
            try{
                if ( resultSet != null){
                    resultSet.close();
                }
                if (pstmt != null){
                    pstmt.close();
                }
                if(conn!=null){                    
                    conn.setAutoCommit(true);
                    dbConnectionManager.freeConnection( dataSourceName,conn );
                }
            }catch(SQLException sqlEx){
                throwDBException(sqlEx);
            }
        }
        return isBolbInserted;
    }
    
      /**
     * This Method is used to INsert Blob Object to the Schedule_Agenda.
     * It locks the respective column( blob column ) based on primarykey 
     * (scheduleID + AgendNumber ) and write the Byte data to the 
     * Selected blob field.
     * @param pdfData file data as byte to be inserted into BLOB.
     * @param schID Schedule ID
     * @param agendaNo Agenda Number.
     * @return boolean status of the bolb insertiion. True : Successfule else 
     *  false.
     *
     * @throws  DBException DB error.
     */      
//     public boolean insertPdfBlob(String dataSourceName,String sqlCommand, 
//            Vector paramList, String sltLocator, byte[] stream, 
//                java.sql.Timestamp timestamp) 
//                    throws DBException{
//        Connection conn = null;
//        boolean isUpdated = false;
//        try{
//            conn = beginTxn();
//            isUpdated = insertPdfBlob(dataSourceName, sqlCommand, paramList, sltLocator, stream, timestamp, conn);            
//            endTxn(conn);
//        }catch(DBException ex){
//            rollback(conn);
//            throw ex;
//        }catch(Exception ex){
//            rollback(conn);
//            throwDBException(ex);
//        }
//        return isUpdated;
//    }   
     
    /**
     * This Method is create a new Connection and return the same
     * 
     * @return Connection Database Connection
     *
     * @throws  DBException DB error.
     */      
     private Connection getConnection() throws DBException{
         Connection conn = null;
         try{
            conn = dbConnectionManager.getConnection("Coeus");
         }catch(SQLException sqlEx){
             throwDBException(sqlEx);
         }
         return conn;
     }

    /**
     * This Method is used to start a new transaction
     * This will be usefull when we need to execute a set of procedures 
     * and then decide to commit or rollback the changes based on User decision or Business Logic
     * This accepts a DB Connection object and starts a transaction and returns the same connection
     * 
     * @return Connection Database Connection
     *
     * @throws  DBException DB error.
     */       
     public Connection beginTxn() throws DBException{
         Connection conn = null;
         try{
             conn = getConnection();
             conn.setAutoCommit(false);             
         }catch(SQLException sqlEx){
             throwDBException(sqlEx);
         }
         return conn;
     }
     
    /**
     * This Method is used to commit the changes done to DB through the given Connection Object
     * A transaction has to be started before calling this method.
     * This will be usefull when we need to execute a set of procedures
     * and then decide to commit or rollback the changes based on User decision or Business Logic
     * This accepts a DB Connection object and commits the changes done to DB
     * 
     * @param conn Connection Database Connection
     * 
     * @throws  DBException DB error.
     */
     public void commit(Connection conn) throws DBException{
         try{
             //Added additional condition to check isClosed - March 24, 2004
            if(conn != null && !conn.isClosed()){
                conn.commit();                
                closeConnection(conn);                
            }
         }catch(SQLException sqlEx){
             throwDBException(sqlEx);
         }
     }

    /**
     * This Method is used to rollback the changes done to DB through the given Connection Object
     * A transaction has to be started before calling this method.
     * This will be usefull when we need to execute a set of procedures
     * and then decide to commit or rollback the changes based on User decision or Business Logic
     * This accepts a DB Connection object and rolls back the changes done to DB
     * 
     * @param conn Connection Database Connection
     * 
     * @throws  DBException DB error.
     */     
     public void rollback(Connection conn) throws DBException{
         try{
            if(conn != null && !conn.isClosed()){                         
                conn.rollback();                
                closeConnection(conn);
            }
         }catch(SQLException sqlEx){
             throwDBException(sqlEx);
         }
     }
    
    /**
     * This Method is used to start a new transaction
     * This will be usefull when we need to execute a set of procedures 
     * and then decide to commit or rollback the changes based on User decision or Business Logic
     * This accepts a DB Connection object and starts a transaction and returns the same connection
     * 
     * @param conn Connection Database Connection
     * @throws  DBException DB error.
     */       
     public void endTxn(Connection conn) throws DBException{
         try{
            if(conn != null && !conn.isClosed()){
                 conn.commit();                 
                 closeConnection(conn);
            }
         }catch(SQLException sqlEx){
             throwDBException(sqlEx);
         }
     }
     
    /**
     * This Method is used to close an Open Connection Object
     * This accepts a DB Connection object and closes the connection
     * 
     * @param con Connection Database Connection
     * 
     * @throws  DBException DB error.
     */          
     private void closeConnection(Connection conn) throws DBException{
         try{
            if(conn != null && !conn.isClosed()){             
                conn.setAutoCommit(true);
                dbConnectionManager.freeConnection("Coeus", conn);
             }
         }catch(SQLException sqlEx){
             throwDBException(sqlEx);
         }
     }    
      
    /**
     * Method used to execute stored functions in the database
     * It will take the connection from the connection pool, which has created 
     * with the given datasource and executes the function, 
     * which has specified in the SQLcommand.
     *
     * <li>The format for the SQLCommand is as follows
     * <li><code>{ &lt;&lt;OUT datatype out_param_name&gt;&gt; = call funName ( 
     * &lt;&lt;in_parameter_name_1&gt;&gt;,&lt;&lt;in_parameter_name_2&gt;&gt;,
     * ...,&lt;&lt;in_parameter_name_n&gt;&gt;) }</code>
     * for example:- { &lt;&lt;OUT INTEGER rowCount&gt;&gt; = 
     * call fn_is_valid_award_num ( &lt;&lt;MIT_AWARD_NUMBER&gt;&gt;) }
     * The Vector paramList should conatain a list of Parameter class objects,
     * which should have all details about parameters that specified in the SQLCommand.
     * After the execution of the <code>stored function</code>, 
     * the <code>resultset</code> will be populated in a <code>vector</code>
     * as a list <code>hashtable</code>, in which each hashtable instance will 
     * represent a row in the resultset.
     *
     * This is an overloaded method which accepts Connection as another Parameter. The given 
     * stored function will be executed using this Connection and no connection object will be
     * created in this method.
     *
     * @param DataSourceName Data source name
     * @param SQLCommand SQL Command
     * @param paramList Vector of Parameter list
     * @param conn Connection Database Connection to be used to execute the given stored function
     * @return Vector result hashtable list
     * @exception DBException DB Error
     * 
     */
    public Vector executeFunctions(String DataSourceName,String SQLCommand, 
            Vector paramList, Connection conn) throws DBException{
        ProcReqParameter procReqParam = new ProcReqParameter(DataSourceName,
                    SQLCommand,paramList);
        Vector procReq = new Vector(1,0);
        procReq.addElement(procReqParam);
        return executeStoreProcs(procReq, conn);
        
    }
    public Vector executeStoreProcs(Vector vectProcedures, Connection conn) throws DBException{
        return executeStoreProcs(vectProcedures,conn,-1);
    }
    /**
     * Method used to exceute more than one stored procedures in a transaction.
     * This method accepts a <code>vector</code> as the argument, which has
     * the list of <code>ProcReqParameters</code>.
     * Before calling this method, caller has to create ProcReqParameter class
     * with DSN name, SQLCommand and vector of Parameter class for each procedure. 
     * Add ProcReqParameter class instances into a vector and pass this vector 
     * as an argument to execute all the procedures.
     *
     * It executes all procedures in the vector and returns overall resultant as a vector.
     * if input argument vector has more than one procedure to execute,
     * <li> It returns Vector of vectors, in which each inner vector contains a 
     * list of hashtable as the resultant of each stored procedure.
     * If any procedures failed to execute, the whole transaction will be rolledback.
     * Trancation will supply commit only when all the procedures executed successfully.
     *
     * <li>The format for the SQLCommand is as follows
     * <li><code> call procName ( &lt;&lt;in_parameter_name_1&gt;&gt;,&lt;&lt;
     * in_parameter_name_2&gt;&gt;,...,&lt;&lt;in_parameter_name_n&gt;&gt; ) </code>
     * for example:-  &lt;&lt;OUT INTEGER rowCount&gt;&gt; = call dw_get_award_info ( 
     * &lt;&lt;MIT_AWARD_NUMBER&gt;&gt;) 
     * The Vector paramList should conatain a list of Parameter class objects,
     * which should have all details about parameters that specified in the SQLCommand.
     * 
     * This is an overloaded method which accepts Connection as another Parameter. All the 
     * procedures will be executed using this Connection and no connection object will be
     * created in this method.
     *
     *  @param vectProcedures Vector of Procedure list
     *  @param conn Connection Database Connection to be used to execute these procedures
     *  @return Vector result, hashtable list
     *  @exception DBException DB Error
     */
    public Vector executeStoreProcs(Vector vectProcedures, Connection conn, int retrieveLimit) throws DBException{
        Vector result = new Vector(5,3);
        Vector entireRes = new Vector(3,2);
         ResultSet resultSet = null;
        CallableStatement callStm =null;
        
        CallableStatement callableStmt = null;
        Vector orderedParamList = null;
        ProcReqParameter procReqParam = null;
        String sqlCommand = null;
        String dataSourceName = null;
        Vector paramList = null;
        int sizeOfProcVect = vectProcedures==null?0:vectProcedures.size();
        try {
            dataSourceName = ((ProcReqParameter)vectProcedures.elementAt(0)).getDSN();
          /*
              Get the data base connection and get the call statement
              to execute the sql
           */
            for(int l=0;l<sizeOfProcVect;++l){
                procReqParam = (ProcReqParameter)vectProcedures.elementAt(l);
                sqlCommand = procReqParam.getSqlCommand();
                paramList = procReqParam.getParameterInfo();
              /*
                  The parameter list is ordered according to the order
                  in which parameters are required in the SQL command
               */
                Hashtable core= getSqlAndParamList(sqlCommand,paramList);
                orderedParamList =(Vector)core.get(kStrParamlist);
              /*
                  The sql is checked for OUT as the these parameters have to be
                  registered.
               */
                boolean outPresent = false;
                if (sqlCommand.indexOf(kStrOut) != -1 ) {
                    outPresent = true;
                }
                /*
                    The sqlCommand is connverted to the format as required by the
                    call statement
               */
                sqlCommand=(String)core.get(kStrSql);
                
                //Ajay: Fix for 10g DB/Client Error Start
                       if ((sqlCommand.toLowerCase().indexOf(kStrCall) != -1) ){
                           if(!sqlCommand.trim().startsWith("{")){
                               sqlCommand = "{ "+ sqlCommand + " }";
                           }
                       }
                //Ajay: Fix for 10g DB/Client Error End
                
                
                try{
                    callableStmt = (java.sql.CallableStatement)conn.prepareCall(sqlCommand);
                    int outIndex = 0;
                  /*
                      The parameters for the SQL command is set
                   */
                    for (int i = 0; i < orderedParamList.size(); ++i ) {
                        Parameter param = (Parameter)orderedParamList.elementAt(i);
                        if (param.getDirection().equals(kStrDirIn)) {
                            if (param.getType().equals(kStrTypeString)) {
                                callableStmt.setString(i+1,param.getStrValue());
                            }else if (param.getType().equals(kStrTypeDate)) {
                                switch(param.getDateType()){
                                    case(Parameter.TYPE_TIMESTAMP):
                                        callableStmt.setTimestamp(i+1, param.getTimestampValue());
                                        break;
                                    case(Parameter.TYPE_DATE):
                                        callableStmt.setDate(i+1, param.getDateValue());
                                        break;
                                    case(Parameter.TYPE_TIME):
                                        callableStmt.setTime(i+1,param.getTimeValue());
                                        break;
                                
                                }
                            }else if (param.getType().equals(kStrTypeTimestamp)) {
                                callableStmt.setTimestamp(i+1,param.getTimestampValue());
                            }else if (param.getType().equals(kStrTypeTime)) {
                                callableStmt.setTime(i+1,param.getTimeValue());
                            }else if (param.getType().equals(kStrTypeInt)) {
                                callableStmt.setInt(i+1,param.getIntValue());
                            }else if (param.getType().equals(kStrTypeFloat)) {
                                callableStmt.setFloat(i+1,param.getFloatValue());
                            }else if (param.getType().equals(kStrTypeDouble)) {
                                callableStmt.setDouble(i+1,param.getDoubleValue());
                            }else if (param.getType().equals(kStrTypeInteger)) {
                                if(param.getIntegerValue()==null){
                                    callableStmt.setNull(i+1,java.sql.Types.INTEGER);
                                }else{
                                    callableStmt.setInt(i+1,param.getIntegerValue().intValue());
                                }
                            }else if (param.getType().equals(kStrTypeFloatObj)) {
                                if(param.getFloatObjValue()==null){
                                    callableStmt.setNull(i+1,java.sql.Types.FLOAT);
                                }else{
                                    callableStmt.setFloat(i+1,param.getFloatObjValue().floatValue());
                                }
                            }else if (param.getType().equals(kStrTypeDoubleObj)) {
                                if(param.getDoubleObjValue()==null){
                                    callableStmt.setNull(i+1,java.sql.Types.DOUBLE);
                                }else{
                                    callableStmt.setDouble(i+1,param.getDoubleObjValue().doubleValue());
                                }
                            }else if (param.getType().equals(kStrTypeBinary) ){
                                java.io.ByteArrayOutputStream BytesOut = 
                                        new java.io.ByteArrayOutputStream();
                                java.io.ObjectOutputStream objectOut = 
                                        new java.io.ObjectOutputStream(BytesOut);
                                objectOut.writeObject(param.getBinaryValue());
                                objectOut.flush();
                                objectOut.close();
                                BytesOut.flush();
                                BytesOut.close();
                                byte[] bytesIn= BytesOut.toByteArray();
                                java.io.ByteArrayInputStream objectIn = 
                                        new java.io.ByteArrayInputStream(bytesIn);
                                callableStmt.setBinaryStream(i+1,objectIn,bytesIn.length);
                                objectIn.close();
                            }
                            // Code added to manupulate the blob data while doing sequence of sql commands
                            // in single shot - starts
                            else if(param.getType().equals(kStrTypeBlob)){
                                byte[] buffer = null;
                                buffer = (byte[])param.getBlobValue();
                                InputStream inputStream ;
                                inputStream = new ByteArrayInputStream(buffer);
                                
                                callableStmt.setBinaryStream(i+1,inputStream, inputStream.available());
                                inputStream.close();
                            }
                            // Code added to manupulate the blob data while doing sequence of sql commands
                            // in single shot - ends
                            //Adding Clob Support - START
                            else if (param.getType().equals(DBEngineConstants.TYPE_CLOB)) {
                                String clobData = param.getClobValue();
                                if (clobData != null) {
                                    ByteArrayInputStream bais = new ByteArrayInputStream(clobData.getBytes());
                                    callableStmt.setAsciiStream(i + 1, bais, bais.available());
                                    bais.close();
                                } else {
                                    callableStmt.setClob(i + 1, CLOB.empty_lob());
                                }
                            }
                            //Adding Clob Support - END
                        }else {
                          /*
                            The index of the first OUT param is captured so
                            that the out params can be processes using this
                            It is assumed that all ins are followed by outs
                           */
                            if(outIndex==0){ 
                                outIndex=i+1;
                                outPresent = true;
                            }
                         /*
                            The OUT params are registered in the Call statement,
                            and they are retrieved from the call statement after it
                            the call is executed
                          */
                            if (param.getType().equalsIgnoreCase(kStrTypeString) ){
                                callableStmt.registerOutParameter(i+1,Types.VARCHAR);
                            }else if (param.getType().equalsIgnoreCase(kStrTypeInteger) ) {
                                callableStmt.registerOutParameter(i+1,Types.INTEGER);
                            }else if (param.getType().equalsIgnoreCase(kStrTypeDouble) ) {
                                callableStmt.registerOutParameter(i+1,Types.DOUBLE);
                            }else if (param.getType().equalsIgnoreCase(kStrTypeFloat) ) {
                                callableStmt.registerOutParameter(i+1,Types.FLOAT);
                            }else if (param.getType().equalsIgnoreCase(kStrTypeTimestamp) ) {
                                callableStmt.registerOutParameter(i+1,Types.TIMESTAMP);
                            }else if (param.getType().equalsIgnoreCase(kStrTypeDate) ) {
                                callableStmt.registerOutParameter(i+1,Types.DATE);
                            }else if (param.getType().equalsIgnoreCase(kStrTypeTime) ) {
                                callableStmt.registerOutParameter(i+1,Types.TIME);
                            }else if (param.getType().equalsIgnoreCase(kStrTypeResSet) ) {
                                callableStmt.registerOutParameter(i+1,OracleTypes.CURSOR);
                            }
                        }
                    }
                    callableStmt.execute();
                  /*
                      The result set is packaged in a hashtable
                      The out params and the return values are elements
                      and the key being the name.
                      If the out parameter is of the TYPE cursor, then cursor
                      is converted to a result set and send out
                      The result, a vector, comprising of statement
                       and result set is  then packaged and that is what is
                       returned
                   */
                    HashMap htResultSet = new HashMap();

                    if (!outPresent) {
                        result = new Vector();                        
                    }else{
                        for (int j = outIndex -1; j < orderedParamList.size(); j++) {
                            Parameter param = (Parameter)orderedParamList.elementAt(j);
                            if(!param.getDirection().equalsIgnoreCase(kStrDirIn)){
                                if (param.getType().equalsIgnoreCase(kStrTypeResSet) ) {
                                    resultSet=(ResultSet)callableStmt.getObject(j+1);
                                    result=packageResult(resultSet);
                                    break;
                                }else if (param.getType().equalsIgnoreCase(kStrTypeString) ) {
                                    String tempStr = callableStmt.getString(j+1);
                                    htResultSet.put(param.getName(),tempStr);
                                    result=packageResult(htResultSet);
                                }else if (param.getType().equalsIgnoreCase(kStrTypeInteger) ) {
                                    htResultSet.put(param.getName(),""+callableStmt.getInt(j+1));
                                    result=packageResult(htResultSet);
                                }
                                // Case# 3803: Lite Personnel Budget months not calculating correctly - Start
                                // Added for Supporting the return of 'Double' type. 
                                else if (param.getType().equalsIgnoreCase(kStrTypeDouble) ) {
                                    htResultSet.put(param.getName(),""+callableStmt.getDouble(j+1));
                                    result=packageResult(htResultSet);
                                }
                                // Case# 3803: Lite Personnel Budget months not calculating correctly - End
                            }  
                        }
                    }
                /*Enhancement done on 07-Mar-2005 by Geo.
                 *If the retreivelimit is greater than 
                 * result set size, throw the DB exception
                 * with error code 50000
                 *
                 */
                /*
                 * BEGIN BLOCK
                 */
                    int resultSize = result==null?0:result.size();
                    if(retrieveLimit!=-1 && resultSize>retrieveLimit){
                        String msg = "The current selection criteria will "+
                                        "retrieve "+resultSize+" rows.\n"+
                                        "This is more than the given limit of "+retrieveLimit;
                        //throws DBException
                        throw new DBException(50000,msg,"");
                    }
                /*
                 *  END BLOCK
                 */
                }finally{
                    //To implement DataSource 6/11/2003 - start
                    if (resultSet != null){
                        resultSet.close();
                    }
                    if(callableStmt!=null){
                        callableStmt.close();
                    }
                }
                entireRes.addElement(result);
            }//End for loop
        }catch (SQLException e) {
            if(conn==null){
                throw new DBException(e);
            }
            UtilFactory.log("Failed SQL statement " + sqlCommand);
//          Case#4197 - Displaying Parameter and corresponding values in a single line - Start
            StringBuffer sb;
            for (int i = 0; i < orderedParamList.size(); i++ ) {
                sb = new StringBuffer();
                Parameter param = (Parameter)orderedParamList.elementAt(i);
                sb.append("Parameter Name: ")
                    .append(param.getName())
                    .append('\t');
                if (param.getType().equals(kStrTypeString)) {
                    sb.append("String Value: ")
                    .append( param.getStrValue());
                }
                else if (param.getType().equals(kStrTypeInt)) {
                    sb.append("Integer Value: ")
                    .append(param.getIntValue());
                }
                else if (param.getType().equals(kStrTypeFloat)) {
                    sb.append("Float Value: ")
                    .append(param.getFloatValue());
                }
                else if (param.getType().equals(kStrTypeDate)) {
                    switch(param.getDateType()){
                        case(Parameter.TYPE_TIMESTAMP):
                            sb.append("Timestamp Value: ")
                            .append(param.getTimestampValue());
                            break;
                        case(Parameter.TYPE_DATE):
                            sb.append("Date Value: ")
                            .append( param.getDateValue());
                            break;
                        case(Parameter.TYPE_TIME):
                            sb.append("Time Value: ")
                            .append(param.getTimeValue());
                            break;
                    }
                }
                else if (param.getType().equals(kStrTypeTimestamp)) {
                    sb.append("Timestamp Value: ")
                    .append(param.getTimestampValue());
                }
                else if (param.getType().equals(kStrTypeTime)) {
                    sb.append("Time Value: ")
                    .append(param.getTimeValue());
                }
                else if (param.getType().equals(kStrTypeBinary) ){
                    
                    try{
                        java.io.ByteArrayOutputStream BytesOut= new java.io.ByteArrayOutputStream();
                        java.io.ObjectOutputStream objectOut=new java.io.ObjectOutputStream(BytesOut);
                        objectOut.writeObject(param.getBinaryValue());
                        objectOut.flush();
                        objectOut.close();
                        BytesOut.flush();
                        BytesOut.close();
                        byte[] bytesIn= BytesOut.toByteArray();
                        java.io.ByteArrayInputStream objectIn=new java.io.ByteArrayInputStream(bytesIn);
                        objectIn.close();
                    }
                    catch(Exception e2) {
                    }
                } else{
                    sb.append("Unknown Type ");
                    if(param.getValue() == null)
                        sb.append("<NULL>");
                    else
                        sb.append(param.getValue().toString());
                }
                 UtilFactory.log(sb.toString());
//                 Case#4197 - Displaying Parameter and corresponding values in a single line - End
            }
//            Commented for case#4197
//            UtilFactory.log(e.getMessage(),e,"DBEngineImpl.java","executeStoreProcs()");
            throwDBException(e);
        }catch(Exception e) {
            UtilFactory.log(e.getMessage(),e,"DBEngineImpl.java","executeStoreProcs()");
            throw new DBException(e.getMessage());
        } finally {
            try{
                if (resultSet != null){
                    resultSet.close();
                }
                if(callableStmt!=null){
                    callableStmt.close();
                }
            }catch(SQLException sqlEx){
                throwDBException(sqlEx);
            }
        }        
        return sizeOfProcVect==1?result:entireRes;
    }      
    
    /**
     * Method used to execute the stored procedure.
     * It executes the procedure which is specified in the SQL Command and 
     * returns resultant as a vector.
     * <li> It returns a vector, which contains a list of hashtables as the resultant.
     *
     * <li>The format for the sqlCommand is as follows
     * <li><code> call procName ( &lt;&lt;in_parameter_name_1&gt;&gt;,
     * &lt;&lt;in_parameter_name_2&gt;&gt;,...,&lt;&lt;in_parameter_name_n&gt;&gt;)</code>
     * for example:-  &lt;&lt;OUT INTEGER rowCount&gt;&gt; = call dw_get_award_info (
     * &lt;&lt;MIT_AWARD_NUMBER&gt;&gt;)
     * The Vector paramList should conatain a list of Parameter class objects,
     * which should have all details about parameters that specified in the sqlCommand.
     *
     * This is an overloaded method which accepts Connection as another Parameter. The given
     * procedure will be executed using this Connection and no connection object will be
     * created in this method.
     *
     *  @param serviceName service name
     *  @param sqlCommand SQL Command
     *  @param dataSource Data Source
     *  @param paramList Vector of Parameter list
     *  @param conn Connection Database Connection to be used to execute these procedures
     *  @return Vector hashtable list
     *  @exception DBException DB Error
     */
    public Vector executeRequest(String serviceName, String sqlCommand, 
            String dataSource, Vector paramList, Connection conn) throws DBException{
        Vector vectRset = null;
        ProcReqParameter procReqParam = 
                new ProcReqParameter(dataSource,sqlCommand,paramList);
        try {
          /*
           *  if the sql string contains call,exec etc, then the use 
           *  executeStoreProcs or excecuteFunctions
           */
            if ((sqlCommand.toLowerCase().indexOf(kStrCall) != -1) ){
                Vector vectProcReq = new Vector(1,0);
                vectProcReq.addElement(procReqParam);
                vectRset = executeStoreProcs(vectProcReq, conn);
            }else if (sqlCommand.toLowerCase().indexOf(kStrTypeBlob.toLowerCase()) != -1){
                vectRset= executePreparedQuery(dataSource,sqlCommand,paramList, conn);
               
            }else{
                vectRset=executeStdSql(dataSource,sqlCommand,  paramList, conn);
            }
        }catch(Exception e){
            e.printStackTrace();
            throwDBException(e,paramList);
        }
        return vectRset;
    }    
    
    /**
     *  This is an oveloaded method to execute standard sql queries which 
     *  does not have any input parameters.
     *  @param dataSource data source
     *  @param sqlCommand SQL Command
     *  @param conn Connection Database connection object
     *  @return Vector hashtable list
     *  @exception DBException DB Error
     */
    public Vector executeRequest(String dataSource, String sqlCommand, Connection conn) 
            throws DBException{
        Vector vectRset = null;
        Vector paramList= new Vector();
        try {
          /*
           *  if the sql string contains call,exec etc, then the use 
           *  executeStoreProcs or excecuteFunctions
           */
            if (sqlCommand.toLowerCase().indexOf(kStrSet) != -1){
                vectRset= executePreparedQuery(dataSource,sqlCommand,  paramList, conn);
            }else{
                vectRset=executeStdSql(dataSource,sqlCommand,  paramList, conn);
            }
        }catch(Exception e){
            throw new DBException(0,"Failed to Excecute SQL",e);
        }
        return vectRset;
    }    
    
    /**
     * Method used to execute the standard SQL. The std sql
     * are select, insert,update. The format is select col1,col2...
     * from table1 a,table2 b,..
     * where a.colx1=b.coly1 ....
     * and a.colz1= ?param1 and b.colz2= ?param2 ...
     * This always returns result set( Incase of update,insert --- nothing is returned)
     * Again the result is packaged as a Vector.
     *  @param String Data source name
     *  @param String sqlCommand
     *  @param Vector Parameter list
     *  @param Connection Database connection object
     *  @return Vector hashtable list
     *  @exception DBException
     */
    private Vector executeStdSql(String dataSourceName,String sqlCommand, 
            Vector paramList, Connection conn) throws DBException{
        //Connection conn = null;
        ResultSet rset=null ;
        Statement stmt = null;
        Vector result = null;
        try {
          /*
              The parse request parses the sql string and replaces the
              ?param1..n with values. Name of param11..n is param1..n
              and is used to replace the value from paramlist
           */
            String request= new String(parseAndReplace(sqlCommand, paramList) );
            stmt = conn.createStatement();
            // Send the boot-strap SQL to the database to get the data
            rset = stmt.executeQuery(request);
            result = packageResult(rset);
        }catch(SQLException sqlEx){
            throwDBException(sqlEx);
        }finally{
            try{
                if(rset!=null){
                    rset.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
            }catch(SQLException sqlEx){
                throwDBException(sqlEx);
            }
        }
        return result;
    }    
    
    /**
     *  Method used to execute prepared queries.
     *  @param dataSourceName DataSource Name
     *  @param sqlCommand SQL Command
     *  @param paramList Vector Parameter list 
     *  @param conn Connection Database connection object
     *  @return Vector hashtable list
     *  @exception DBException DB error
     */
    public Vector executePreparedQuery(String dataSourceName,String sqlCommand, 
            Vector paramList, Connection conn) throws DBException{
        //Connection conn = null;
        Vector result = null;
        
        ResultSet resultSet = null;

        PreparedStatement pstmt = null;
        try {
            String request = sqlCommand;
            boolean outPresent = false;
            if (sqlCommand.indexOf(kStrOut) != -1 ) {
                outPresent = true;
            }
            int outIndex = 0;
        /*
          The parameter list is ordered according to the order
          in which parameters are required in the SQL command
         */
            Hashtable core= getSqlAndParamList(sqlCommand,paramList);
            Vector orderedParamList =(Vector)core.get(kStrParamlist);
        /*
                The sqlCommand is connverted to the format as required by the
                call statement
           */
            sqlCommand=(String)core.get(kStrSql);
            // Create the statement object
            pstmt = conn.prepareCall(sqlCommand);
          /*
              The parameters for the SQL command is set
           */
            for (int i = 0; i < orderedParamList.size(); i++ ) {
                Parameter param = (Parameter)orderedParamList.elementAt(i);
                if (param.getDirection().equals(kStrDirIn)) {
                    if (param.getType().equals(kStrTypeString)) {
                        pstmt.setString(i+1,param.getStrValue());
                    }else if (param.getType().equals(kStrTypeDate)) {
                        switch(param.getDateType()){
                            case(Parameter.TYPE_TIME):
                                pstmt.setTime(i+1,param.getTimeValue());
                                break;
                            case(Parameter.TYPE_DATE):
                                pstmt.setDate(i+1,param.getDateValue());
                                break;
                            case(Parameter.TYPE_TIMESTAMP):
                                pstmt.setTimestamp(i+1,param.getTimestampValue());
                                break;
                        }
                    }else if (param.getType().equals(kStrTypeTimestamp)) {
                        pstmt.setTimestamp(i+1,param.getTimestampValue());
                    }else if (param.getType().equals(kStrTypeTime)) {
                        pstmt.setTime(i+1,param.getTimeValue());
                    }else if (param.getType().equals(kStrTypeInt)) {
                        pstmt.setInt(i+1,param.getIntValue());
                    }else if (param.getType().equals(kStrTypeFloat)) {
                        pstmt.setFloat(i+1,param.getFloatValue());
                    }else if (param.getType().equals(kStrTypeDouble)) {
                        pstmt.setDouble(i+1,param.getDoubleValue());
                    }else if (param.getType().equals(kStrTypeBinary) ){
                        byte[] buffer = null;
                        ByteArrayOutputStream bOut = (java.io.ByteArrayOutputStream)param.getBinaryValue();
                        buffer = bOut.toByteArray();
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        pstmt.setBinaryStream(i+1,ois,buffer.length);
                        ois.close();
                        bais.close();
                    }else if(param.getType().equals(kStrTypeBlob)){
                          byte[] buffer = null;
                          buffer = (byte[])param.getBlobValue();
                          InputStream inputStream ;
                          inputStream = new ByteArrayInputStream(buffer);
                          
                          pstmt.setBinaryStream(i+1,inputStream, inputStream.available());
                          inputStream.close();
                    }else if(param.getType().equals(kStrTypeLong)){
                        byte[] buffer = null;
                        String strValue = (String)param.getValue();
                        buffer = strValue.getBytes();
                        InputStream inputStream ;
                        inputStream = new ByteArrayInputStream(buffer);
                        pstmt.setAsciiStream(i+1,inputStream, inputStream.available());
                        inputStream.close();
                    }else if (param.getType().equals(DBEngineConstants.TYPE_CLOB) ){
                        
                        String clobData = param.getClobValue();
                        if(clobData!=null){
                            ByteArrayInputStream bais = new ByteArrayInputStream(clobData.getBytes());
                            
                            //                        java.io.FileOutputStream out = new java.io.FileOutputStream("clobData");
                            //                        ObjectOutputStream s = new ObjectOutputStream(out);
                            //                        s.writeObject(clobData);
                            //                        s.flush();
                            //                        FileInputStream in = new FileInputStream("clobData");
                            pstmt.setAsciiStream(i+1, bais, bais.available());
                            bais.close();
                            //                        in.close();
                        }else{
                            pstmt.setClob(i+1, CLOB.empty_lob());
                        }
                    }
                }else {
                  /*
                    The index of the first OUT param is captured so
                    that the out params can be processes using this
                    It is assumed that all ins are followed by outs
                   */
                    if(outIndex==0) outIndex=i+1;
                 /*
                    The OUT params are registered in the Call statement,
                    and they are retrieved from the call statement after it
                    the call is executed
                  */
                    if (param.getType().equalsIgnoreCase(kStrTypeSTRING) ){
                        ((CallableStatement)pstmt).registerOutParameter(i+1,
                                Types.VARCHAR);
                    }else if (param.getType().equalsIgnoreCase(kStrTypeInteger) ) {
                        ((CallableStatement)pstmt).registerOutParameter(i+1,
                                Types.INTEGER);
                    }else if (param.getType().equalsIgnoreCase(kStrTypeResSet) ) {
                        ((CallableStatement)pstmt).registerOutParameter(i+1,
                                OracleTypes.CURSOR);
                    }
                }
            }
            pstmt.execute();
            HashMap htResultSet = new HashMap();
            if (!outPresent) {
                result=packageResult(htResultSet);
                return result;
            }
            for (int j = outIndex -1; j < orderedParamList.size(); j++) {
                Parameter param = (Parameter)orderedParamList.elementAt(j);
                if (param.getType().equalsIgnoreCase(kStrTypeResSet) ) {
                    //To implement DataSource 6/11/2003 - start
                    resultSet = (ResultSet)((CallableStatement)pstmt).getObject(j+1);
                    //resSet=(OracleResultSet)((CallableStatement)pstmt).getObject(j+1);
                    result=packageResult(resultSet);
                    //result=packageResult(resSet);
                    //To implement DataSource 6/11/2003 - end                  
                    
                    return result;
                }else if (param.getType().equalsIgnoreCase(kStrTypeString) ) {
                    htResultSet.put(param.getName(),((CallableStatement)pstmt).getString(j+1));
                }else if (param.getType().equalsIgnoreCase(kStrTypeInteger) ) {
                    htResultSet.put(param.getName(),""+((CallableStatement)pstmt).getInt(j+1));
                }
            }
            result=packageResult(htResultSet);
        }catch (SQLException e) {
            UtilFactory.log("Failed SQL statement " + sqlCommand);
            throwDBException(e);
        }catch(Exception e) {
            throwDBException(e);
        } finally {
            try{
                if (resultSet != null){
                    resultSet.close();
                }
                if (pstmt != null){
                    pstmt.close();
                }
            }catch(SQLException sqlEx){
                throwDBException(sqlEx);
            }
        }
        return result;
    }    
    
      /**
     * This Method is used to insert blob data.
     * This is an overloaded method which accepts Connection as an additional parameter
     * @param dataSourceName Data Source Name
     * @param sqlCommand SQL Command
     * @param paramList Vector of Parameter List
     * @param sltLocator Select Locator
     * @param stream byte[] array of File Data to be uploaded 
     * @param dbStamp update timestamp
     * @param conn Connection Database connection object
     * @return boolean status of the bolb insertiion. True : Successfule else 
     * false.
     *
     * @throws  DBException DB error.
     */ 
//    public boolean insertBlob(String dataSourceName,String sqlCommand, 
//            Vector paramList, String sltLocator, byte[] stream, 
//                java.sql.Timestamp dbStamp, Connection conn) 
//                    throws DBException{
//        Vector result = null;
//        PreparedStatement pstmt = null;        
//        PreparedStatement locPtmt = null;
//
//        boolean status = false; 
//        try {
//            String request = sqlCommand;
//        /*
//          The parameter list is ordered according to the order
//          in which parameters are required in the SQL command
//         */
//            Hashtable core= getSqlAndParamList(sqlCommand,paramList);
//            Vector orderedParamList =(Vector)core.get(kStrParamlist);
//        /*
//                The sqlCommand is connverted to the format as required by the
//                call statement
//           */
//            sqlCommand=(String)core.get(kStrSql);
//            // Create the statement object
//            pstmt = conn.prepareStatement(sqlCommand);
//          /*
//              The parameters for the SQL command is set
//           */
//            for (int i = 0; i < orderedParamList.size(); i++ ) {
//                Parameter param = (Parameter)orderedParamList.elementAt(i);
//                if (param.getDirection().equals(kStrDirIn)) {
//                    if (param.getType().equals(kStrTypeString)) {
//                        pstmt.setString(i+1,param.getStrValue());
//                    }else if (param.getType().equals(kStrTypeDate)) {
//                        switch(param.getDateType()){
//                            case(Parameter.TYPE_TIME):
//                                pstmt.setTime(i+1,param.getTimeValue());
//                                break;
//                            case(Parameter.TYPE_DATE):
//                                pstmt.setDate(i+1,param.getDateValue());
//                                break;
//                            case(Parameter.TYPE_TIMESTAMP):
//                                pstmt.setTimestamp(i+1,param.getTimestampValue());
//                                break;
//                        }
//                    }else if (param.getType().equals(kStrTypeTimestamp)) {
//                        pstmt.setTimestamp(i+1,param.getTimestampValue());
//                    }else if (param.getType().equals(kStrTypeTime)) {
//                        pstmt.setTime(i+1,param.getTimeValue());
//                    }else if (param.getType().equals(kStrTypeInt)) {
//                        pstmt.setInt(i+1,param.getIntValue());
//                    }else if (param.getType().equals(kStrTypeFloat)) {
//                        pstmt.setFloat(i+1,param.getFloatValue());
//                    }else if (param.getType().equals(kStrTypeDouble)) {
//                        pstmt.setDouble(i+1,param.getDoubleValue());
//                    }else if (param.getType().equals(kStrTypeBinary) ){
//                        byte[] buffer = null;
//                        ByteArrayOutputStream bOut = (java.io.ByteArrayOutputStream)param.getBinaryValue();
//                        buffer = bOut.toByteArray();
//                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
//                        ObjectInputStream ois = new ObjectInputStream(bais);
//                        pstmt.setBinaryStream(i+1,ois,buffer.length);
//                        ois.close();
//                        bais.close();
//
//                    }
//                }
//            }
//            if(pstmt.executeUpdate()>0){
//                System.out.println("insertion successful");
//               
//                locPtmt = (PreparedStatement)conn.prepareStatement(sltLocator);
//
//                locPtmt.setBytes(1,stream);
//                locPtmt.setTimestamp(2,dbStamp);
//                int updated = locPtmt.executeUpdate();
//                status = true;
//            }else{
//                System.out.println("insertion falied");
//            }
//        }catch (SQLException e) {
//            UtilFactory.log("Failed SQL statement " + sqlCommand);
//            throwDBException(e);
//        }catch(Exception e) {
//            throwDBException(e);
//        } finally {
//            try{
//                if(locPtmt!=null){
//                    locPtmt.close();
//                }
//                if (pstmt != null){
//                    pstmt.close();
//                }
//            }catch(SQLException sqlEx){
//                throwDBException(sqlEx);
//            }
//        }
//        return status;
//    }    
    
      /**
     * This Method is used to insert blob data.
     * This is an overloaded method which accepts Connection as an additional parameter
     * @param dataSourceName Data Source Name
     * @param sqlCommand SQL Command
     * @param paramList Vector of Parameter List
     * @param sltLocator Select Locator
     * @param stream byte[] array of File Data to be uploaded 
     * @param timestamp update timestamp
     * @param conn Connection Database connection object
     * @return boolean status of the bolb insertiion. True : Successfule else 
     * false.
     *
     * @throws  DBException DB error.
     */     
//     public boolean insertPdfBlob(String dataSourceName,String sqlCommand, 
//            Vector paramList, String sltLocator, byte[] stream, 
//            java.sql.Timestamp timestamp, Connection conn) throws DBException{
//        Vector result = null;
//        ResultSet resSet = null; 
//        PreparedStatement pstmt = null;                
//
//        PreparedStatement locPtmt = null;
//
//        boolean status = false; 
//        try {
//            String request = sqlCommand;
//        /*
//          The parameter list is ordered according to the order
//          in which parameters are required in the SQL command
//         */
//            Hashtable core= getSqlAndParamList(sqlCommand,paramList);
//            Vector orderedParamList =(Vector)core.get(kStrParamlist);
//        /*
//                The sqlCommand is connverted to the format as required by the
//                call statement
//           */
//            sqlCommand=(String)core.get(kStrSql);
//            // Create the statement object
//            pstmt = conn.prepareStatement(sqlCommand);
//          /*
//              The parameters for the SQL command is set
//           */
//            for (int i = 0; i < orderedParamList.size(); i++ ) {
//                Parameter param = (Parameter)orderedParamList.elementAt(i);
//                System.out.println("paramter string value=>"+param.getStrValue());
//                if (param.getDirection().equals(kStrDirIn)) {
//                    if (param.getType().equals(kStrTypeString)) {
//                        pstmt.setString(i+1,param.getStrValue());
//                    }else if (param.getType().equals(kStrTypeDate)) {
//                        switch(param.getDateType()){
//                            case(Parameter.TYPE_TIME):
//                                pstmt.setTime(i+1,param.getTimeValue());
//                                break;
//                            case(Parameter.TYPE_DATE):
//                                pstmt.setDate(i+1,param.getDateValue());
//                                break;
//                            case(Parameter.TYPE_TIMESTAMP):
//                                pstmt.setTimestamp(i+1,param.getTimestampValue());
//                                break;
//                        }
//                    }else if (param.getType().equals(kStrTypeTimestamp)) {
//                        pstmt.setTimestamp(i+1,param.getTimestampValue());
//                    }else if (param.getType().equals(kStrTypeTime)) {
//                        pstmt.setTime(i+1,param.getTimeValue());
//                    }else if (param.getType().equals(kStrTypeInt)) {
//                        pstmt.setInt(i+1,param.getIntValue());
//                    }else if (param.getType().equals(kStrTypeFloat)) {
//                        pstmt.setFloat(i+1,param.getFloatValue());
//                    }else if (param.getType().equals(kStrTypeDouble)) {
//                        pstmt.setDouble(i+1,param.getDoubleValue());
//                    }else if (param.getType().equals(kStrTypeBinary) ){
//                        byte[] buffer = null;
//                        ByteArrayOutputStream bOut = (java.io.ByteArrayOutputStream)param.getBinaryValue();
//                        buffer = bOut.toByteArray();
//                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
//                        ObjectInputStream ois = new ObjectInputStream(bais);
//                        pstmt.setBinaryStream(i+1,ois,buffer.length);
//                        ois.close();
//                        bais.close();
//                    }
//                }
//            }
//            if(pstmt.executeUpdate()>0){
//                System.out.println("insertion successful");
//                PreparedStatement p;                
//                locPtmt = (PreparedStatement)conn.prepareStatement(sltLocator);
//                
//                InputStream inputStream ;
//                inputStream = new ByteArrayInputStream(stream);
//                
//                locPtmt.setBinaryStream(1,inputStream, inputStream.available());
//                locPtmt.setTimestamp(2,timestamp);
//                int updated = locPtmt.executeUpdate();
//                status = true;
//            }else{
//                System.out.println("insertion falied");
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//            UtilFactory.log("Failed SQL statement " + sqlCommand);
//            throwDBException(e);
//        }catch(Exception e) {
//            throwDBException(e);
//        } finally {
//            try{
//                if(locPtmt!=null){
//                    locPtmt.close();
//                }
//                if (resSet != null){
//                    resSet.close();
//                }
//                if (pstmt != null){
//                    pstmt.close();
//                }               
//            }catch(SQLException sqlEx){
//                throwDBException(sqlEx);
//            }
//        }
//        return status;
//    }       
     
    /**
     * This Method is used to insert batch blob objects into database. 
     *
     * @param ProcReqParameters
     * @param conn Connection Database connection object
     * @return boolean status of the bolb insertiion. True : Successful else false.
     *
     * @throws  DBException DB error.
     */
     public boolean sqlUpdate(ProcReqParameter procReqParameter)
                    throws DBException{
         return batchSQLUpdate(procReqParameter,null);
     }
    /**
     * This Method is used to insert batch blob objects into database. This method does not get
     * a new connection but operates on the connection object supplied
     *
     * @param ProcReqParameters
     * @param conn Connection Database connection object
     * @return boolean status of the bolb insertiion. True : Successful else false.
     *
     * @throws  DBException DB error.
     */
     public boolean batchSQLUpdate(ProcReqParameter procReqParameter, Connection conn)
                    throws DBException{
         boolean connectionCreated = false;
         if(conn==null) {
             conn = getConnection();
             connectionCreated = true;
         }
         Vector reqParams = new Vector(1);
         reqParams.add(procReqParameter);
         boolean boolReturn = batchSQLUpdate(reqParams,conn);
         if(connectionCreated) {
             closeConnection(conn);
         }
         return boolReturn;
     }
    /**
     * This Method is used to insert batch blob objects into database. This method does not get
     * a new connection but operates on the connection object supplied
     *
     * @param vecProcParameters Vector of ProcReqParameters
     * @param conn Connection Database connection object
     * @return boolean status of the bolb insertiion. True : Successful else false.
     *
     * @throws  DBException DB error.
     */
     public boolean batchSQLUpdate(Vector vecProcParameters, Connection conn)
                    throws DBException{
        Vector result = null;
        PreparedStatement pstmt = null;
        
        boolean status = false;
        String dataSourceName = null;
        try {
            Hashtable core = null;
            Vector orderedParamList = null;
            
            int recCount = vecProcParameters.size();
            
            dataSourceName = ((ProcReqParameter)vecProcParameters.elementAt(0)).getDSN();            
            ProcReqParameter procReqParameter = (ProcReqParameter)vecProcParameters.elementAt(0);
            String sqlCommand = procReqParameter.getSqlCommand();
            Vector paramList = procReqParameter.getParameterInfo();
            /*
              The parameter list is ordered according to the order
              in which parameters are required in the SQL command
            */
            core = getSqlAndParamList(sqlCommand,paramList);
            sqlCommand=(String)core.get(kStrSql);
            pstmt = conn.prepareStatement(sqlCommand);

            for(int row=0; row < recCount; row++){
                procReqParameter = (ProcReqParameter)vecProcParameters.elementAt(row);
                
                paramList = procReqParameter.getParameterInfo();
                sqlCommand = procReqParameter.getSqlCommand();
            
                /*
                  The parameter list is ordered according to the order
                  in which parameters are required in the SQL command
                */
                core = getSqlAndParamList(sqlCommand,paramList);
                orderedParamList = (Vector)core.get(kStrParamlist);
                /*
                  The parameters for the SQL command is set
                */
                for (int i = 0; i < orderedParamList.size(); i++ ) {
                    Parameter param = (Parameter)orderedParamList.elementAt(i);
                    if (param.getDirection().equals(kStrDirIn)) {
                        if (param.getType().equals(kStrTypeString)) {
                            pstmt.setString(i+1,param.getStrValue());
                        }else if (param.getType().equals(kStrTypeDate)) {
                            switch(param.getDateType()){
                                case(Parameter.TYPE_TIME):
                                    pstmt.setTime(i+1,param.getTimeValue());
                                    break;
                                case(Parameter.TYPE_DATE):
                                    pstmt.setDate(i+1,param.getDateValue());
                                    break;
                                case(Parameter.TYPE_TIMESTAMP):
                                    pstmt.setTimestamp(i+1,param.getTimestampValue());
                                    break;
                            }
                        }else if (param.getType().equals(kStrTypeTimestamp)) {
                            pstmt.setTimestamp(i+1,param.getTimestampValue());
                        }else if (param.getType().equals(kStrTypeTime)) {
                            pstmt.setTime(i+1,param.getTimeValue());
                        }else if (param.getType().equals(kStrTypeInt)) {
                            pstmt.setInt(i+1,param.getIntValue());
                        }else if (param.getType().equals(kStrTypeFloat)) {
                            pstmt.setFloat(i+1,param.getFloatValue());
                        }else if (param.getType().equals(kStrTypeDouble)) {
                            pstmt.setDouble(i+1,param.getDoubleValue());
                        }else if (param.getType().equals(kStrTypeBinary) ){
                            byte[] buffer = null;
                            ByteArrayOutputStream bOut = (java.io.ByteArrayOutputStream)param.getBinaryValue();
                            buffer = bOut.toByteArray();
                            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                            ObjectInputStream ois = new ObjectInputStream(bais);
                            pstmt.setBinaryStream(i+1,ois,buffer.length);
                            ois.close();
                            bais.close();
                        }else if(param.getType().equals(kStrTypeBlob)){
                            byte[] buffer = null;
                            buffer = (byte[])param.getBlobValue();
                            InputStream inputStream ;
                            inputStream = new ByteArrayInputStream(buffer);                            

                            pstmt.setBinaryStream(i+1,inputStream, inputStream.available());
                            inputStream.close();                            
                        }else if(param.getType().equals(kStrTypeLong)){
                            byte[] buffer = null;
                            String strValue = (String)param.getValue();
                            buffer = strValue.getBytes();
                            InputStream inputStream ;
                            inputStream = new ByteArrayInputStream(buffer);
                            pstmt.setAsciiStream(i+1,inputStream, inputStream.available());
                            inputStream.close();
                        }else if (param.getType().equals(DBEngineConstants.TYPE_CLOB) ){
                            // Commented by Shivakumar - BEGIN
//                            System.out.println("char data"+param.getClobValue());
                            // Commented by Shivakumar - END
                            String clobData = param.getClobValue();
                            if(clobData!=null){
                                // Commented by Shivakumar - BEGIN
//                            System.out.println("clob data=>"+clobData);
                            // Commented by Shivakumar - END
                            ByteArrayInputStream bais = new ByteArrayInputStream(clobData.getBytes());
//                            java.io.FileOutputStream out = new java.io.FileOutputStream("clobData");
//                            ObjectOutputStream ous = new ObjectOutputStream(out);
//                            ous.writeObject(clobData);
//                            ous.flush();
//                            FileInputStream in = new FileInputStream("clobData");
//                            System.out.println("input stream=>"+in.available());
//                            ObjectInputStream ois = new ObjectInputStream(in);
                            
                            pstmt.setAsciiStream(i+1, bais, bais.available());
                            bais.close();
//                            in.close();
//                            ois.close();
                            }else{
                                pstmt.setClob(i+1, CLOB.empty_lob());
                                
                            }
                        }
                        
                    }
                }
                pstmt.executeUpdate();
            }
            pstmt.close();
            pstmt = null;
            status = true;
        }catch (SQLException e) {
//            Commented for case#4197
//            e.printStackTrace(); 
            throwDBException(e);
        }catch(Exception e) {            
            throwDBException(e);
        }finally{
            try{
                if (pstmt != null){
                    pstmt.close();
                }
            }catch(SQLException sqlEx){
                throwDBException(sqlEx);
            }
        }
        return status;
    }
}