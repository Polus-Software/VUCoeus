/*
 * @(#)AgendaTxnBean.java
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
 * This class is used to handle the report generation for Agenda/Roaster Module.
 * The main functionality of this class/component is to obtain the report( BLOB form) from the
 * data base and display in the client browser and saving the newly generated report (.pdf ) back to the
 * data base(in BLOB form). All the Data Access/ Data Manipulation methods in this class
 * are synchronized to ensure the ISOLATION attribute of the Transaction/DB
 * operation.
 *
 * @author  subramanya
 * @version
 * Created on October 15, 2002, 11:08 PM
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class AgendaTxnBean {


    // Singleton instance of a dbEngine
    private DBEngineImpl dbEngine;

    // holds the UserID ( Ex: Logind UserID )
    private String userId;


    /** Creates a new instance of AgendaTxnBean
     * @param usrID userLogin ID
     */
    public AgendaTxnBean(String usrID) {

        dbEngine = new DBEngineImpl();
        userId = usrID;
    }


    /**
     * Gets all the agenda Details of this schedule ID. The return Vector
     * contains the Agenda Version Number and Agenda data created vector as its
     * elements.
     * @param scheduleID Specific Schedule ID.
     * @return Vector data vector of vector elements.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized Vector getAllAgendaDetails(String scheduleID)
            throws CoeusException, DBException {

        Vector result = new Vector(3, 2);
        Vector agendaParameter = new Vector();
        agendaParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                scheduleID));
        if (dbEngine != null) {

            //Execute the  DB Procedure and Stores the result in Vector.
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SCHEDULE_AGENDA_LIST ( <<SCHEDULE_ID>>, " +
                    " <<OUT RESULTSET rset>>) ",
                    "Coeus", agendaParameter);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        int agendaSize = result.size();

        if (agendaSize < 1) {

            return null;

        }
        Vector agendaResultSet = new Vector(3, 2);
        Vector agendaDBData = null;
        HashMap agendaRow = null;

        for (int rowIndex = 0; rowIndex < agendaSize; rowIndex++) {
            agendaRow = (HashMap) result.elementAt(rowIndex);
            agendaDBData = new Vector();

            agendaDBData.addElement( (String)
                    agendaRow.get("COMMITTEE_ID"));
            agendaDBData.addElement( (String)
                    agendaRow.get("COMMITTEE_NAME"));
            agendaDBData.addElement( (String)
                    agendaRow.get("SCHEDULE_ID"));
            agendaDBData.addElement( agendaRow.get("AGENDA_NUMBER").toString() );
            agendaDBData.addElement( (String)
                    agendaRow.get("AGENDA_NAME"));
            agendaDBData.addElement( (Timestamp)
                    agendaRow.get("CREATE_TIMESTAMP"));
            agendaDBData.addElement( (String)
                    agendaRow.get("CREATE_USER"));
            agendaDBData.addElement( (Timestamp)
                    agendaRow.get("UPDATE_TIMESTAMP"));
            agendaDBData.addElement( (String)
                    agendaRow.get("UPDATE_USER"));
            agendaResultSet.addElement(agendaDBData);
        }
        return agendaResultSet;
    }


    /**
     * Gets the sepcific PDF file for display based on specific schedule ID &
     * Agenda Number.
     * @param schdID schedule ID.
     * @param agendaNO Agenda Number
     * @return Vector result vector containing Agenda#, ScheduleID, PDF Blob
     * Object.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized String getSpecificAgendaPDF(String schdID,
                                                    String agendaNo, String strPath) throws CoeusException, DBException {
        String fileName = null ;                                             
        Vector result = new Vector(3, 2);  
        
        
        if (dbEngine != null) 
        {
            if( dbEngine.getPDFBlob("Coeus",schdID,agendaNo, strPath))
            {
                return schdID + "_" + agendaNo + ".pdf" ;
            } 
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }

    /**
     *  @created by Geo
     *  The method used to fetch the specific agenda report which stored in the database and 
     *  write to the Server hard drive. And return the filename with the relative path.
     *  the path
     *  
     */
    public synchronized byte[] getSpecificAgendaPDF(String schdID,
                                                    String agendaNo) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector agendaParameter = new Vector();
        String newAgendaID = null ; 
/*        agendaParameter.addElement(new Parameter("PDF_STORE",
                DBEngineConstants.TYPE_BLOB,
                "PDF_STORE",DBEngineConstants.DIRECTION_OUT));*/
        agendaParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));
        agendaParameter.addElement(new Parameter("AGENDA_NUMBER",
                DBEngineConstants.TYPE_INT,
                agendaNo));

            String selectQuery = "SELECT PDF_STORE FROM OSP$SCHEDULE_AGENDA " +                                 
                                 "WHERE SCHEDULE_ID =  <<SCHEDULE_ID>> " +
                                 " AND AGENDA_NUMBER =  <<AGENDA_NUMBER>>";
        
            HashMap resultRow = null;
           if(dbEngine!=null){
               result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",agendaParameter);
               //System.out.println("result=>"+result);
               if( !result.isEmpty() ){
                    resultRow = (HashMap)result.get(0);
                   java.io.ByteArrayOutputStream pdfStore = 
                        (java.io.ByteArrayOutputStream)resultRow.get("PDF_STORE");
                   System.out.println("size after ret:"+pdfStore.size());
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
    public synchronized boolean insertNewAgendaPDF(String schdID,
                                                   byte[] fileData) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector agendaParameter = new Vector();
        boolean isNewAgendaInserted = false;
        String newAgendaID = null ; 
        Vector agendaParam = new Vector() ;
        
        agendaParam.addElement(new Parameter("AW_SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));

        if (dbEngine != null) {
            //call this function to get the newly inserted agenda-number.
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NEWAGNID >> = call FN_GET_MAX_SCHEDULE_AGENDA_NO ( " +
                    "<<AW_SCHEDULE_ID>> ) }",  agendaParam);
            if (!result.isEmpty()) 
            {
                HashMap agnData = (HashMap) result.elementAt(0);
                newAgendaID =  String.valueOf(1 +  Integer.parseInt(agnData.get("NEWAGNID").toString())) ;
            }
            else // first time creatiion will have the resulset as empty
            {
                newAgendaID = "1" ;
            }    
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        Vector procedures = new Vector();
        agendaParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));
        agendaParameter.addElement(new Parameter("AGENDA_NUMBER",
                DBEngineConstants.TYPE_INT,
                newAgendaID));

        agendaParameter.addElement(new Parameter("PDF_STORE",
                DBEngineConstants.TYPE_BLOB,fileData));
        agendaParameter.addElement(new Parameter("AGENDA_NAME",
                DBEngineConstants.TYPE_STRING,
                "Agenda For Schedule # " + schdID));
        agendaParameter.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        agendaParameter.addElement(new Parameter("CREATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        agendaParameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        agendaParameter.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        agendaParameter.addElement(new Parameter("AW_SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                schdID));
        agendaParameter.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        agendaParameter.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                "I"));
        
        /*String insertStmt = "call UPD_COMM_SCHEDULE_AGENDA( <<SCHEDULE_ID>> , <<AGENDA_NUMBER>> , "+
                        "<<AGENDA_NAME>>, <<CREATE_TIMESTAMP>>, <<CREATE_USER>>, "+
                        "<<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>,"+
                        "<<AW_SCHEDULE_ID>>, <<AW_UPDATE_TIMESTAMP>>, <<AC_TYPE>> )"; 
        
        ProcReqParameter insertProcReqParam = new ProcReqParameter();
        insertProcReqParam.setParameterInfo(agendaParameter);
        insertProcReqParam.setSqlCommand(insertStmt);
         */

//        String insertStmt = "insert into osp$schedule_agenda( SCHEDULE_ID , "+
//                        "AGENDA_NUMBER , AGENDA_NAME, "+
//                        "CREATE_TIMESTAMP, CREATE_USER, UPDATE_TIMESTAMP, UPDATE_USER ) "+
//                        "values ( <<SCHEDULE_ID>> , <<AGENDA_NUMBER>> , "+
//                        "<<AGENDA_NAME>>, <<CREATE_TIMESTAMP>>, <<CREATE_USER>>, "+
//                        "<<UPDATE_TIMESTAMP>>, <<UPDATE_USER>> )"; 
//        
//        
//        
//        /*String selectQry = "SELECT PDF_STORE FROM OSP$SCHEDULE_AGENDA " +                                 
//                                 "WHERE SCHEDULE_ID =  ? " +
//                                 " AND AGENDA_NUMBER = ? " +
//                                 " FOR UPDATE OF PDF_STORE ";
//         */
//        
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - start
//        /*String selectQry = "SELECT PDF_STORE FROM OSP$SCHEDULE_AGENDA " +                                 
//                                 "WHERE UPDATE_TIMESTAMP =  ?"+// +DateUtils.formatTimestamp(dbTimestamp)+
//                                 " FOR UPDATE OF PDF_STORE ";*/
//        String selectQry = "UPDATE OSP$SCHEDULE_AGENDA SET PDF_STORE = ? "+                                 
//                                 " WHERE UPDATE_TIMESTAMP =  ?";
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - end        
//           if(dbEngine!=null)
//            {   
//                boolean status = dbEngine.insertPdfBlob("Coeus", insertStmt, 
//                    agendaParameter,selectQry,fileData,dbTimestamp);
//                 
//                /*boolean status = dbEngine.insertPdfBlob("Coeus", insertProcReqParam,
//                                    selectQry,fileData,dbTimestamp);
//                 */
//                System.out.println("status=>"+status);
//                isNewAgendaInserted = status ;  
//            }
//            else
//            {   
//                throw new CoeusException("db_exceptionCode.1000");
//            }
//                       
        String insertStmt = "insert into osp$schedule_agenda( SCHEDULE_ID , "+
                        "AGENDA_NUMBER , AGENDA_NAME, PDF_STORE, "+
                        "CREATE_TIMESTAMP, CREATE_USER, UPDATE_TIMESTAMP, UPDATE_USER ) "+
                        "values ( <<SCHEDULE_ID>> , <<AGENDA_NUMBER>> , "+
                        "<<AGENDA_NAME>>, <<PDF_STORE>>, <<CREATE_TIMESTAMP>>, <<CREATE_USER>>, "+
                        "<<UPDATE_TIMESTAMP>>, <<UPDATE_USER>> )"; 
           if(dbEngine!=null){   
                dbEngine.executePreparedQuery("Coeus", insertStmt, agendaParameter);
            }else{   
                throw new CoeusException("db_exceptionCode.1000");
            }
        return true;
    }

    /**
     * Gives the Active Members List for Committee, which contain the membership
     * details.
     * @param commID CommitteeID
     * @return Vector containing collection of CommitteeActiveMemberBean.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized Vector getCommitteeActiveMembers(String commID)
            throws CoeusException, DBException {

        Vector result = new Vector(3, 2);
        Vector roasterParameter = new Vector();
        roasterParameter.addElement(new Parameter("COMMITTEE_ID",
                DBEngineConstants.TYPE_STRING,
                commID));
        if (dbEngine != null) {

            //Execute the  DB Procedure and Stores the result in Vector.
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COMM_MEMBER_LIST_CURRENT ( <<COMMITTEE_ID>>, " +
                    "<<OUT RESULTSET rset>>) ",
                    "Coeus", roasterParameter);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int roasterSize = result.size();
        Vector roasterResultSet = new Vector(3, 2);

        if (roasterSize < 1) {

            return null;

        }

        CommitteeActiveMemberBean actMemberBean = null;
        HashMap roasterRow = null;
        for (int rowIndex = 0; rowIndex < roasterSize; rowIndex++) {
            roasterRow = (HashMap) result.elementAt(rowIndex);
            
            actMemberBean = new CommitteeActiveMemberBean(
                    commID,
                    (String) roasterRow.get("MEMBERSHIP_ID"),
                    (String) roasterRow.get("PERSON_ID"),
                    (String) roasterRow.get("PERSON_NAME"),
                    roasterRow.get("NON_EMPLOYEE_FLAG").toString(),
                    (String) roasterRow.get("COMMENTS"));

            roasterResultSet.addElement(actMemberBean);
        }
        return roasterResultSet;
    }

    /**
     * Gives the Active Members List for the given Committee and Schedule Id. 
     * 
     * @param commID CommitteeID
     * @param scheduleId ScheduleID
     * @return Vector containing collection of CommitteeActiveMemberBean.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public Vector getCommitteeActiveMembers(String commID, String scheduleId)
            throws CoeusException, DBException {
                
        System.out.println("Comittee Id :"+commID);
        System.out.println("scheduleId :"+scheduleId);
        Vector result = new Vector(3, 2);
        Vector roasterParameter = new Vector();
        roasterParameter.addElement(new Parameter("COMMITTEE_ID",
                DBEngineConstants.TYPE_STRING,
                commID));
        roasterParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                scheduleId));        
        if (dbEngine != null) {

            //Execute the  DB Procedure and Stores the result in Vector.
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COMM_ACTIVE_MEMBERS ( <<COMMITTEE_ID>>, <<SCHEDULE_ID>>," +
                    "<<OUT RESULTSET rset>>) ",
                    "Coeus", roasterParameter);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int roasterSize = result.size();
        Vector roasterResultSet = new Vector(3, 2);

        if (roasterSize < 1) {

            return null;

        }

        CommitteeActiveMemberBean actMemberBean = null;
        HashMap roasterRow = null;
        for (int rowIndex = 0; rowIndex < roasterSize; rowIndex++) {
            roasterRow = (HashMap) result.elementAt(rowIndex);
            
            actMemberBean = new CommitteeActiveMemberBean(
                    commID,
                    (String) roasterRow.get("MEMBERSHIP_ID"),
                    (String) roasterRow.get("PERSON_ID"),
                    (String) roasterRow.get("PERSON_NAME"),
                    roasterRow.get("NON_EMPLOYEE_FLAG").toString(),
                    (String) roasterRow.get("COMMENTS"),
                    (String) roasterRow.get("IS_ALTERNATE"));

            roasterResultSet.addElement(actMemberBean);
        }
        return roasterResultSet;
    }    
    /**
     * Gives the Recipients for the given Schedule Id.
     * @param scheduleId Schedule Id as String
     * @return Vector containing MailInfoBeans.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public Vector getRecipientForScheduleAgenda(String scheduleId)
            throws CoeusException, DBException {

        Vector result = new Vector(3, 2);
        Vector recipientParameter = new Vector();
        recipientParameter.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                scheduleId));
        if (dbEngine != null) {

            //Execute the  DB Procedure and Stores the result in Vector.
            result = dbEngine.executeRequest("Coeus",
                    "call GET_RECIPIENTS_FOR_SCH_AGENDA ( <<SCHEDULE_ID>>, " +
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
    /*
     * returns MAx AgendaID based on the schedule ID
     */
    //COEUSQA-2292 START Agenda attachment available to reviewers from Lite
    public synchronized String getMaxAgendaID(String scheduleID)
            throws CoeusException, DBException {

        Vector result = new Vector(3, 2);
        Vector agendaParameter = new Vector();
        agendaParameter.addElement(new Parameter("SCHEDULE_ID",DBEngineConstants.TYPE_STRING,scheduleID));
        if (dbEngine != null) {
            //Execute the  DB Procedure and Stores the result in Vector.
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER AGENDA_NUMBER>> = call  FN_GET_MAX_SCHEDULE_AGENDA_NO( "
                    + " << SCHEDULE_ID >> )}", agendaParameter) ;
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int roasterSize = result.size();
        Vector roasterResultSet = new Vector(3, 2);

        if (roasterSize < 1) { 
            return null;
        }

        HashMap roasterRow = null;
        String maxAgendaID = null;
            roasterRow = (HashMap) result.elementAt(0);            
            maxAgendaID =(String) roasterRow.get("AGENDA_NUMBER");

         return maxAgendaID;
    }
    //COEUSQA-2292 END Agenda attachment available to reviewers from Lite
}