/*
 * @(#)MinuteTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.irb.bean;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.HashMap;
import java.util.Enumeration;
import java.sql.Timestamp;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.tree.*;

import java.text.*;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import oracle.sql.*;

import java.io.*;
import java.sql.Blob;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.MailInfoBean;

/**
 *
 * This class is used to handle the report generation for Minute Module.
 * The main functionality of this class/component is to obtain the report( BLOB form) from the
 * data base and display in the client browser and saving the newly generated report (.pdf ) back to the
 * data base(in BLOB form). All the Data Access/ Data Manipulation methods in this class
 * are synchronized to ensure the ISOLATION attribute of the Transaction/DB
 * operation.
 *
 * @author  Prasanna Kumar K
 * @version
 * Created on July 18, 2003, 3:21 PM
 *
 */
public class MinuteTxnBean {


    // Singleton instance of a dbEngine
    private DBEngineImpl dbEngine;

    // holds the UserID ( Ex: Logind UserID )
    private String userId;


    /** Creates a new instance of MinuteTxnBean
     * @param usrID userLogin ID
     */
    public MinuteTxnBean(String usrID) {

        dbEngine = new DBEngineImpl();
        userId = usrID;
    }

    /**
     *  @created by Geo
     *  The method used to fetch the specific Minute report stored in the database and 
     *  write to the Server hard drive. And return the filename with the relative path.
     *  the path
     *  
     */
    public synchronized byte[] getSpecificMinutePDF(String schdID,
                                                    String MinuteNo) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector minuteParameter = new Vector();
        String newMinuteID = null ; 
        minuteParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));
        minuteParameter.addElement(new Parameter("MINUTE_NUMBER",
                DBEngineConstants.TYPE_INT,
                MinuteNo));

            String selectQuery = "SELECT PDF_STORE FROM OSP$COMM_SCHEDULE_MINUTE_DOC " +                                 
                                 "WHERE SCHEDULE_ID =  <<SCHEDULE_ID>> " +
                                 " AND MINUTE_NUMBER =  <<MINUTE_NUMBER>>";
        
            HashMap resultRow = null;
           if(dbEngine!=null){
               result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",minuteParameter);
               if( !result.isEmpty() ){
                    resultRow = (HashMap)result.get(0);
                   java.io.ByteArrayOutputStream pdfStore = 
                        (java.io.ByteArrayOutputStream)resultRow.get("PDF_STORE");
                   return pdfStore.toByteArray();
               }
               
           }else {
               throw new CoeusException("db_exceptionCode.1000");
           }
           
        return null;
    }
    /**
     * This method inserts a newly generated PDF document into the db.
     * It is two level process where in it insert data with empty_blob() on
     * the first call to the DB and then Updated the sepcific entry from the
     * newly updated blob.
     * @param schdID Schedule ID as string
     * @param fileData byte data of the New PDF generated file.
     * 
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized boolean insertNewMinutePDF(String schdID,
                                                   byte[] fileData) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector minuteParameter = new Vector();
        boolean isNewMinuteInserted = false;
        String newMinuteID = null ; 
        Vector minuteParam = new Vector() ;
        
        minuteParam.addElement(new Parameter("AW_SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));

        if (dbEngine != null) {
            //call this function to get the newly inserted minute-number.
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NEW_MINUTE_NO >> = call FN_GET_MAX_SCHEDULE_MINUTE_NO ( " +
                    "<<AW_SCHEDULE_ID>> ) }",  minuteParam);
            if (!result.isEmpty()) 
            {
                HashMap minuteData = (HashMap) result.elementAt(0);
                newMinuteID =  String.valueOf(1 +  Integer.parseInt(minuteData.get("NEW_MINUTE_NO").toString())) ;
            }
            else // first time creatiion will have the resulset as empty
            {
                newMinuteID = "1" ;
            }    
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        Vector procedures = new Vector();
        minuteParam.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));
        minuteParam.addElement(new Parameter("MINUTE_NUMBER",
                DBEngineConstants.TYPE_INT,
                newMinuteID));

        minuteParam.addElement(new Parameter("MINUTE_NAME",
                DBEngineConstants.TYPE_STRING,"Minute For Schedule # " + schdID));

        minuteParam.addElement(new Parameter("PDF_STORE",
                DBEngineConstants.TYPE_BLOB,
                fileData));
        minuteParam.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        minuteParam.addElement(new Parameter("CREATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        minuteParam.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        minuteParam.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        minuteParam.addElement(new Parameter("AW_SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));
        minuteParam.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        minuteParam.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                "I"));
        
//        String insertStmt = "insert into OSP$COMM_SCHEDULE_MINUTE_DOC( SCHEDULE_ID , "+
//                        "MINUTE_NUMBER , MINUTE_NAME, "+
//                        "CREATE_TIMESTAMP, CREATE_USER, UPDATE_TIMESTAMP, UPDATE_USER ) "+
//                        "values ( <<SCHEDULE_ID>> , <<MINUTE_NUMBER>> , "+
//                        "<<MINUTE_NAME>>, <<CREATE_TIMESTAMP>>, <<CREATE_USER>>, "+
//                        "<<UPDATE_TIMESTAMP>>, <<UPDATE_USER>> )"; 
//        
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - start
//        /*
//        String selectQry = "SELECT PDF_STORE FROM OSP$COMM_SCHEDULE_MINUTE_DOC " +                                 
//                                 "WHERE UPDATE_TIMESTAMP =  ?"+// +DateUtils.formatTimestamp(dbTimestamp)+
//                                 " FOR UPDATE OF PDF_STORE ";*/
//        
//        String selectQry = "UPDATE OSP$COMM_SCHEDULE_MINUTE_DOC SET PDF_STORE = ?" +                                 
//                           "WHERE UPDATE_TIMESTAMP =  ?";
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - end
//
//           if(dbEngine!=null)
//            {   
//                boolean status = dbEngine.insertPdfBlob("Coeus", insertStmt, 
//                    minuteParam,selectQry,fileData,dbTimestamp);
//                 
//                /*boolean status = dbEngine.insertPdfBlob("Coeus", insertProcReqParam,
//                                    selectQry,fileData,dbTimestamp);
//                 */
//                System.out.println("status=>"+status);
//                isNewMinuteInserted = status ;  
//            }
//            else
//            {   
//                throw new CoeusException("db_exceptionCode.1000");
//            }
        String insertStmt = "insert into OSP$COMM_SCHEDULE_MINUTE_DOC( SCHEDULE_ID , "+
            "MINUTE_NUMBER , MINUTE_NAME, PDF_STORE, "+
            "CREATE_TIMESTAMP, CREATE_USER, UPDATE_TIMESTAMP, UPDATE_USER ) "+
            "values ( <<SCHEDULE_ID>> , <<MINUTE_NUMBER>> , "+
            "<<MINUTE_NAME>>, <<PDF_STORE>>, <<CREATE_TIMESTAMP>>, <<CREATE_USER>>, "+
            "<<UPDATE_TIMESTAMP>>, <<UPDATE_USER>> )";
        if(dbEngine!=null){
            dbEngine.executePreparedQuery("Coeus", insertStmt, minuteParam);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    
    /**
     * Gets all the Minutes Details of this schedule ID. The return Vector
     * contains the Minute Version Number and Minute data created vector as its
     * elements.
     * @param scheduleID Specific Schedule ID.
     * @return Vector data vector of vector elements.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized Vector getAllMinuteDetails(String scheduleID)
            throws CoeusException, DBException {

        Vector result = new Vector(3, 2);
        Vector minuteParameter = new Vector();
        minuteParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                scheduleID));
        if (dbEngine != null) {

            //Execute the  DB Procedure and Stores the result in Vector.
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COMM_SCHEDULE_MINUTE_DOC ( <<SCHEDULE_ID>>, " +
                    " <<OUT RESULTSET rset>>) ",
                    "Coeus", minuteParameter);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        int minuteSize = result.size();

        if (minuteSize < 1) {

            return null;

        }
        Vector minuteResultSet = new Vector(3, 2);
        Vector minuteDBData = null;
        HashMap minuteRow = null;

        for (int rowIndex = 0; rowIndex < minuteSize; rowIndex++) {
            minuteRow = (HashMap) result.elementAt(rowIndex);
            minuteDBData = new Vector();

            minuteDBData.addElement( (String)
                    minuteRow.get("COMMITTEE_ID"));
            minuteDBData.addElement( (String)
                    minuteRow.get("COMMITTEE_NAME"));
            minuteDBData.addElement( (String)
                    minuteRow.get("SCHEDULE_ID"));
            minuteDBData.addElement( minuteRow.get("MINUTE_NUMBER").toString() );
            minuteDBData.addElement( (String)
                    minuteRow.get("MINUTE_NAME"));
            minuteDBData.addElement( (Timestamp)
                    minuteRow.get("CREATE_TIMESTAMP"));
            minuteDBData.addElement( (String)
                    minuteRow.get("CREATE_USER"));
            minuteDBData.addElement( (Timestamp)
                    minuteRow.get("UPDATE_TIMESTAMP"));
            minuteDBData.addElement( (String)
                    minuteRow.get("UPDATE_USER"));
            minuteResultSet.addElement(minuteDBData);
        }
        return minuteResultSet;
    }    
    
    /**
     * Gives the Recipients for the given Schedule Id.
     * @param scheduleId Schedule Id as String
     * @return Vector containing MailInfoBeans.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public Vector getRecipientForScheduleMinutes(String scheduleId)
            throws CoeusException, DBException {

        Vector result = new Vector(3, 2);
        Vector recipientParameter = new Vector();
        recipientParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                scheduleId));
        if (dbEngine != null) {

            //Execute the  DB Procedure and Stores the result in Vector.
            result = dbEngine.executeRequest("Coeus",
                    "call GET_RECIPIENTS_FOR_SCH_MINUTES ( <<SCHEDULE_ID>>, " +
                    "<<OUT RESULTSET rset>>) ",
                    "Coeus", recipientParameter);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recipientSize = result.size();
        Vector recipientResultSet = new Vector(3, 2);

        if (recipientSize < 1) {

            return null;

        }

        MailInfoBean mailInfoBean = null;
        HashMap recipientRow = null;
        for (int rowIndex = 0; rowIndex < recipientSize; rowIndex++) {
            recipientRow = (HashMap) result.elementAt(rowIndex);
            mailInfoBean = new MailInfoBean();
            mailInfoBean.setPersonID( (String)
                    recipientRow.get("PERSON_ID"));
            mailInfoBean.setPersonName( (String)
                    recipientRow.get("PERSON_NAME"));
            String strNonEmployeeFlag = recipientRow.get("NON_EMPLOYEE_FLAG").toString();
            boolean isNonEmployee = false;
            if( strNonEmployeeFlag != null && strNonEmployeeFlag.equalsIgnoreCase("Y") ){
                isNonEmployee = true;
            }            
            mailInfoBean.setNonEmployeeFlag(isNonEmployee);            
            mailInfoBean.setRoleName( (String)
                    recipientRow.get("ROLE_NAME"));
            mailInfoBean.setEmailId((String) 
                    recipientRow.get("EMAIL_ADDRESS"));            
            
            recipientResultSet.addElement(mailInfoBean);
        }
        return recipientResultSet;
    }            
}