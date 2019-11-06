/*
 * @(#)ProtoCorrespTypeTxnBean.java February 26, 2003, 11:51 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusFunctions;
import java.math.BigDecimal;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.util.Hashtable;

import java.io.*;
//import edu.mit.coeus.report.bean.ProcessReportXMLBean;
import edu.mit.coeus.bean.MailInfoBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.mail.*;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
/**
 *
 * @author  Geo Thomas
 */
public class ProtoCorrespTypeTxnBean {
    
    // Singleton instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    // holds the user id who has logged in.
    private String userId;
    //holds timestamp
    private Timestamp dbTimestamp;
    /** Creates a new instance of ProtoCorrespTypeTxnBean */
    public ProtoCorrespTypeTxnBean() {
        this(null);
    }
    /** Creates a new instance of ProtoCorrespTypeTxnBean */
    public ProtoCorrespTypeTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    /**
     *  This method used to get all protocol correspondence types
     *  from OSP$PROTO_CORRESP_TYPE.
     *  <li>To fetch the data, it uses the procedure get_proto_corresp_types.
     *
     *  @return Vector collection of all Protocol correspondence types
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtoCorrespTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        Vector correspTypes = new Vector();
        HashMap correspTypeRow = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_proto_corresp_types( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actionCount = result.size();
        for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
            correspTypeRow = (HashMap)result.elementAt(rowIndex);
            CorrespondenceTypeFormBean correpTypeFormBean =
            new CorrespondenceTypeFormBean();
            correpTypeFormBean.setProtoCorrespTypeCode((
            (BigDecimal)correspTypeRow.get("PROTO_CORRESP_TYPE_CODE")).intValue());
            correpTypeFormBean.setProtoCorrespTypeDesc((String)correspTypeRow.get("DESCRIPTION"));
            correpTypeFormBean.setUpdateTimestamp((Timestamp)correspTypeRow.get("UPDATE_TIMESTAMP"));
            correpTypeFormBean.setUpdateUser((String)correspTypeRow.get("UPDATE_USER"));
            correpTypeFormBean.setFileBytes(getProtoCorrespTemplate(correpTypeFormBean.getCommitteeId(),correpTypeFormBean.getProtoCorrespTypeCode()));

            correspTypes.addElement(correpTypeFormBean);
        }
        return correspTypes;
    }
    
    /**
     *  This method used to get all protocol correspondence types
     *  from OSP$PROTO_CORRESP_TYPE.
     *  <li>To fetch the data, it uses the procedure get_proto_corresp_types.
     *
     *  @return Vector collection of all Protocol correspondence types
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtoCorrespTemplates() throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        Vector correspTypes = new Vector();
        HashMap correspTypeRow = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_proto_corresp_templ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actionCount = result.size();
        for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
            correspTypeRow = (HashMap)result.elementAt(rowIndex);
            CorrespondenceTypeFormBean correpTypeFormBean =
            new CorrespondenceTypeFormBean();
            correpTypeFormBean.setProtoCorrespTypeCode((
            (BigDecimal)correspTypeRow.get("PROTO_CORRESP_TYPE_CODE")).intValue());
            correpTypeFormBean.setProtoCorrespTypeDesc((String)correspTypeRow.get("DESCRIPTION"));
            correpTypeFormBean.setCommitteeId((String)correspTypeRow.get("COMMITTEE_ID"));
            correpTypeFormBean.setCommitteeName((String)correspTypeRow.get("COMMITTEE_NAME"));
            correpTypeFormBean.setUpdateTimestamp((Timestamp)correspTypeRow.get("UPDATE_TIMESTAMP"));
            correpTypeFormBean.setUpdateUser((String)correspTypeRow.get("UPDATE_USER"));
            
            //Commenting by Geo on 01/05/2006. It should not fetch bytes data for all the templates
//            correpTypeFormBean.setFileBytes(getProtoCorrespTemplate(correpTypeFormBean.getCommitteeId(),correpTypeFormBean.getProtoCorrespTypeCode()));

            correspTypes.addElement(correpTypeFormBean);
        }
        return correspTypes;
    }    
    
    /**
     *  This method used to get all Correspondence Template details
     *  from OSP$PROTO_CORRESP_TEMPL.
     *  <li>To fetch the data, it uses the procedure get_proto_corresp_types.
     *
     *  @return Vector collection of all Protocol correspondence types
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public byte[] getProtoCorrespTemplate(String committeeId, int protoCorrespTypeCode) throws CoeusException,DBException{
        Vector result = null;
        Vector param =null;
        Vector correspTypes = new Vector();
        HashMap resultRow = null;
        if(dbEngine != null){
            param = new Vector();
            param.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespTypeCode));
            param.addElement(new Parameter("COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,
            committeeId));

            String selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$PROTO_CORRESP_TEMPL " +
            "WHERE PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> " +
            " AND COMMITTEE_ID =  <<COMMITTEE_ID>>";

            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",param);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                java.io.ByteArrayOutputStream template =
                (java.io.ByteArrayOutputStream)resultRow.get("CORESPONDENCE_TEMPLATE");
                return template.toByteArray();
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }        
    
    public boolean addUpdCorrespTypes(Vector correspBeanList)
    throws CoeusException, DBException{
        
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        int listSize = correspBeanList.size();
        for(int listIndex = 0;listIndex<listSize;listIndex++){
            Vector paramTypes= new Vector();
            CorrespondenceTypeFormBean correpTypeFormBean =
            (CorrespondenceTypeFormBean)correspBeanList.elementAt(listIndex);
            paramTypes.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+correpTypeFormBean.getProtoCorrespTypeCode()));
            paramTypes.addElement(new Parameter("DESCRIPTION",
            DBEngineConstants.TYPE_STRING,
            correpTypeFormBean.getProtoCorrespTypeDesc()));
            paramTypes.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            dbTimestamp));
            paramTypes.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,
            userId));
            paramTypes.addElement(new Parameter("AW_PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+correpTypeFormBean.getProtoCorrespTypeCode()));
            paramTypes.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            correpTypeFormBean.getUpdateTimestamp()));
            paramTypes.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            ""+correpTypeFormBean.getAcType()));
            
            StringBuffer sqlUpdProtoCorrespTypes = new StringBuffer(
            "call UPD_PROTO_CORRESP_TYPE(");
            sqlUpdProtoCorrespTypes.append(" <<PROTO_CORRESP_TYPE_CODE>> , ");
            sqlUpdProtoCorrespTypes.append(" <<DESCRIPTION>> , ");
            sqlUpdProtoCorrespTypes.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlUpdProtoCorrespTypes.append(" <<UPDATE_USER>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AW_PROTO_CORRESP_TYPE_CODE>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AC_TYPE>> )");
            
            ProcReqParameter procCorrespType  = new ProcReqParameter();
            procCorrespType.setDSN("Coeus");
            procCorrespType.setParameterInfo(paramTypes);
            procCorrespType.setSqlCommand(sqlUpdProtoCorrespTypes.toString());
            procedures.addElement(procCorrespType);
            
        }
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    
    public boolean addUpdCorrespTypes(CorrespondenceTypeFormBean correpTypeFormBean)
    throws CoeusException, DBException{
        
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;

        Vector paramTypes= new Vector();
        
        paramTypes.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+correpTypeFormBean.getProtoCorrespTypeCode()));
        paramTypes.addElement(new Parameter("DESCRIPTION",
        DBEngineConstants.TYPE_STRING,
        correpTypeFormBean.getProtoCorrespTypeDesc()));
        paramTypes.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));
        paramTypes.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));
        paramTypes.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        correpTypeFormBean.getCommitteeId()));        
        paramTypes.addElement(new Parameter("AW_PROTO_CORRESP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+correpTypeFormBean.getProtoCorrespTypeCode()));
        paramTypes.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        correpTypeFormBean.getUpdateTimestamp()));
        paramTypes.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        ""+correpTypeFormBean.getAcType()));

        StringBuffer sqlUpdProtoCorrespTypes = new StringBuffer(
        "call UPD_PROTO_CORRESP_TYPE(");
        sqlUpdProtoCorrespTypes.append(" <<PROTO_CORRESP_TYPE_CODE>> , ");
        sqlUpdProtoCorrespTypes.append(" <<DESCRIPTION>> , ");
        sqlUpdProtoCorrespTypes.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlUpdProtoCorrespTypes.append(" <<UPDATE_USER>> , ");
        sqlUpdProtoCorrespTypes.append(" <<COMMITTEE_ID>> , ");        
        sqlUpdProtoCorrespTypes.append(" <<AW_PROTO_CORRESP_TYPE_CODE>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AC_TYPE>> )");

        ProcReqParameter procCorrespType  = new ProcReqParameter();
        procCorrespType.setDSN("Coeus");
        procCorrespType.setParameterInfo(paramTypes);
        procCorrespType.setSqlCommand(sqlUpdProtoCorrespTypes.toString());
        procedures.addElement(procCorrespType);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    
    
    /**
     * This method is used to insert Correspondence Template into database.
     * @param correspondenceTypeFormBean CorrespondenceTypeFormBean
     * @param fileData byte data of the New PDF Template
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized boolean addUpdProtoCorrespTypeTempl(CorrespondenceTypeFormBean correspondenceTypeFormBean) 
    throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        //Vector procedures = new Vector(3,2);
        Vector correspParameter = new Vector();
        boolean isUpdated = false;
        byte[] fileData = null; 
        Vector procedures = new Vector();
        ProcReqParameter procNarrative = new ProcReqParameter();
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        java.sql.Connection conn = null;
        if(correspondenceTypeFormBean!=null && 
            correspondenceTypeFormBean.getAcType()!=' '){
            fileData = correspondenceTypeFormBean.getFileBytes();
            if(fileData!=null){
                String statement = "";
                if(correspondenceTypeFormBean.getAcType()=='I'){
                    correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                        DBEngineConstants.TYPE_INT,
                        ""+correspondenceTypeFormBean.getProtoCorrespTypeCode()));
                    correspParameter.addElement(new Parameter("COMMITTEE_ID",
                        DBEngineConstants.TYPE_STRING,
                        correspondenceTypeFormBean.getCommitteeId()));
                    correspParameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,
                        dbTimestamp));
                    correspParameter.addElement(new Parameter("UPDATE_USER",
                        DBEngineConstants.TYPE_STRING,
                        userId));
                    correspParameter.addElement(new Parameter("AW_PROTO_CORRESP_TYPE_CODE",
                        DBEngineConstants.TYPE_INT,
                        ""+correspondenceTypeFormBean.getProtoCorrespTypeCode()));
                    correspParameter.addElement(new Parameter("AW_COMMITTEE_ID",
                        DBEngineConstants.TYPE_STRING,
                        correspondenceTypeFormBean.getCommitteeId()));
                    correspParameter.addElement(new Parameter("CORESPONDENCE_TEMPLATE",
                        DBEngineConstants.TYPE_BLOB,
                        correspondenceTypeFormBean.getFileBytes()));                    

                    statement = "INSERT INTO OSP$PROTO_CORRESP_TEMPL (PROTO_CORRESP_TYPE_CODE, COMMITTEE_ID, UPDATE_TIMESTAMP, UPDATE_USER, CORESPONDENCE_TEMPLATE ) "+
                            " VALUES( <<PROTO_CORRESP_TYPE_CODE>> , <<COMMITTEE_ID>> , <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>>, <<CORESPONDENCE_TEMPLATE>> )";                    
                    
                    procNarrative = new ProcReqParameter();
                    procNarrative.setDSN("Coeus");
                    procNarrative.setParameterInfo(correspParameter);
                    procNarrative.setSqlCommand(statement);                    
                    
                    procedures.add(procNarrative);
//                    String selectQry = "UPDATE OSP$PROTO_CORRESP_TEMPL SET CORESPONDENCE_TEMPLATE = ? " +
//                    " WHERE UPDATE_TIMESTAMP =  ?";                    
                    //Modified to implement DataSource and 
                    //to use Java CallableStatement instead of Oracle - end

                    if(dbEngine!=null) {                        
                        try{
                            conn = dbEngine.beginTxn();
//                            boolean status = dbEngine.insertPdfBlob("Coeus", statement,
//                                correspParameter,selectQry,fileData,dbTimestamp);
                            boolean status = dbEngine.batchSQLUpdate(procedures, conn);
                            dbEngine.endTxn(conn);
                            isUpdated = status ;
                        }catch(Exception ex){
                            dbEngine.rollback(conn);
                            throw new DBException(ex);
                        }
                    }
                    else {
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                }else if(correspondenceTypeFormBean.getAcType()=='U'){
                    if(dbEngine!=null){
                        try{
                            conn = dbEngine.beginTxn();
                            correspondenceTypeFormBean.setAcType('D');
                            procedures.add(updateCorrepTemplate(correspondenceTypeFormBean));                            
                            dbEngine.executeStoreProcs(procedures, conn);
                            
                            //Now Insert
                            correspParameter = new Vector();
                            correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                                DBEngineConstants.TYPE_INT,
                                ""+correspondenceTypeFormBean.getProtoCorrespTypeCode()));
                            correspParameter.addElement(new Parameter("COMMITTEE_ID",
                                DBEngineConstants.TYPE_STRING,
                                correspondenceTypeFormBean.getCommitteeId()));
                            correspParameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                                DBEngineConstants.TYPE_TIMESTAMP,
                                dbTimestamp));
                            correspParameter.addElement(new Parameter("UPDATE_USER",
                                DBEngineConstants.TYPE_STRING,
                                userId));
                            correspParameter.addElement(new Parameter("AW_PROTO_CORRESP_TYPE_CODE",
                                DBEngineConstants.TYPE_INT,
                                ""+correspondenceTypeFormBean.getProtoCorrespTypeCode()));
                            correspParameter.addElement(new Parameter("AW_COMMITTEE_ID",
                                DBEngineConstants.TYPE_STRING,
                                correspondenceTypeFormBean.getCommitteeId()));
                            correspParameter.addElement(new Parameter("CORESPONDENCE_TEMPLATE",
                                DBEngineConstants.TYPE_BLOB,
                                correspondenceTypeFormBean.getFileBytes()));

                            statement = "INSERT INTO OSP$PROTO_CORRESP_TEMPL (PROTO_CORRESP_TYPE_CODE, COMMITTEE_ID, UPDATE_TIMESTAMP, UPDATE_USER, CORESPONDENCE_TEMPLATE ) "+
                                    " VALUES( <<PROTO_CORRESP_TYPE_CODE>> , <<COMMITTEE_ID>> , <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>>, <<CORESPONDENCE_TEMPLATE>> )";                    

                            procNarrative = new ProcReqParameter();
                            procNarrative.setDSN("Coeus");
                            procNarrative.setParameterInfo(correspParameter);
                            procNarrative.setSqlCommand(statement);                    
                            procedures = new Vector();    
                            procedures.add(procNarrative);                            
                            //Insert new Record
                            boolean status = dbEngine.batchSQLUpdate(procedures, conn);
                            dbEngine.endTxn(conn);
                            isUpdated = status ;
                        }catch(Exception ex){
                            dbEngine.rollback(conn);
                            throw new DBException(ex);
                        }
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }//End else
                }//End if acType = U
                
                //Bug Fix: Start Added by Ajay on 06-10-2004 to delete the template from database 
                else if(correspondenceTypeFormBean.getAcType()=='D'){
                    if(dbEngine!=null){
                        procedures.add(updateCorrepTemplate(correspondenceTypeFormBean));
                        dbEngine.executeStoreProcs(procedures);
                        isUpdated = true;
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                }//End if acType = D
                //Bug Fix: End
                
            }//End fileData != null
        }else if(correspondenceTypeFormBean.getAcType()=='D'){
            if(dbEngine!=null){
                procedures.add(updateCorrepTemplate(correspondenceTypeFormBean));
                dbEngine.executeStoreProcs(procedures);
                isUpdated = true;
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        return isUpdated;            
    }
    
    /**
     *  The method used to fetch the Correspondence Template from DB. 
     *  Template will be stored in the database and written to the Server hard drive.
     *
     */
    //Modified for the case@ COEUSDEV-220- generate Correspondnece manuaaly
    //Added committee Id in parameter
    public synchronized byte[] getCorrespTemplate(int correspondentTypeCode ,
    String scheduleId,String committeeId) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector correspParameter = new Vector();
         //Modified for the case@ COEUSDEV-220- generate Correspondnece manuaaly
        String strCommitteeId = committeeId;
        String selectQuery = "";
        HashMap resultRow = null;
        
        if(scheduleId==null){
            if(strCommitteeId != null  && strCommitteeId.length()>0){
                strCommitteeId = committeeId; // "INSTITUTE"; //prps changed this on nov 18 2003
            }else{
                strCommitteeId = "DEFAULT" ;
            }
            correspParameter = new Vector();
            correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+correspondentTypeCode));
            correspParameter.addElement(new Parameter("COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,
            strCommitteeId));

            selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$PROTO_CORRESP_TEMPL " +
                "WHERE PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> " +
                " AND COMMITTEE_ID =  <<COMMITTEE_ID>>";
            
            
        }else{
            correspParameter.addElement(new Parameter("CORRESPONDENT_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+correspondentTypeCode));
            correspParameter.addElement(new Parameter("SCHEDULE_ID",
            DBEngineConstants.TYPE_STRING,
            scheduleId));

            selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$PROTO_CORRESP_TEMPL, OSP$COMM_SCHEDULE  "+
                "WHERE PROTO_CORRESP_TYPE_CODE =  <<CORRESPONDENT_TYPE_CODE>> AND OSP$PROTO_CORRESP_TEMPL.COMMITTEE_ID = OSP$COMM_SCHEDULE.COMMITTEE_ID "+
                "AND  OSP$COMM_SCHEDULE.SCHEDULE_ID =  <<SCHEDULE_ID>>";            
        }
        
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",correspParameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                java.io.ByteArrayOutputStream template =
                (java.io.ByteArrayOutputStream)resultRow.get("CORESPONDENCE_TEMPLATE");
                return template.toByteArray();
            }else{
                //Modified for the case#coeusdev-229-generate correspondence
                //If template is not present for the corresponding template, then it will
                //Check for default
                if(scheduleId == null){
                    strCommitteeId="DEFAULT";
                    correspParameter = new Vector();
                    correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                            DBEngineConstants.TYPE_INT,
                            ""+correspondentTypeCode));
                    correspParameter.addElement(new Parameter("COMMITTEE_ID",
                            DBEngineConstants.TYPE_STRING,
                            strCommitteeId));
                    
                    selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$PROTO_CORRESP_TEMPL " +
                            "WHERE PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> " +
                            " AND COMMITTEE_ID =  <<COMMITTEE_ID>>";
                     resultRow = null;
                    result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",correspParameter);
                    if( !result.isEmpty() ){
                        resultRow = (HashMap)result.get(0);
                        java.io.ByteArrayOutputStream template =
                        (java.io.ByteArrayOutputStream)resultRow.get("CORESPONDENCE_TEMPLATE");
                        return template.toByteArray();
                    }

                }
                else if(scheduleId!=null){
                    //If no template found get the default Template of INSTITUTE
                    //strCommitteeId = "DEFAULT" ; //"INSTITUTE"; //prps changed this on nov 18 2003
                    //Modified for the case#COEUSDEV-220-Generate Correspondence-start
                    ScheduleTxnBean schTxnBean = new ScheduleTxnBean();
                    //getting the committee details, by passing scheduleId.
                    ScheduleDetailsBean schDetailBean = schTxnBean.getScheduleDetails(scheduleId);
                    if(schDetailBean.getCommitteeId()!=null && schDetailBean.getCommitteeId().length()>0){
                        strCommitteeId= schDetailBean.getCommitteeId();
                    } else{
                        strCommitteeId="DEFAULT";
                    }
                    //Modified for the case#COEUSDEV-220-Generate Correspondence-end
                    correspParameter = new Vector();
                    correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                    ""+correspondentTypeCode));
                    correspParameter.addElement(new Parameter("COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING,
                    strCommitteeId));

                    selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$PROTO_CORRESP_TEMPL " +
                    "WHERE PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> " +
                    " AND COMMITTEE_ID =  <<COMMITTEE_ID>>";
                    
                    resultRow = null;
                    result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",correspParameter);
                    if( !result.isEmpty() ){
                        resultRow = (HashMap)result.get(0);
                        java.io.ByteArrayOutputStream template =
                        (java.io.ByteArrayOutputStream)resultRow.get("CORESPONDENCE_TEMPLATE");
                        return template.toByteArray();
                    }
                    //Modified for the case#coeusdev-229-generate correspondence
                    // If for correponding committee  id, template is not present, then it will check for default template.
                    else{
                          correspParameter = new Vector();
                    correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                    ""+correspondentTypeCode));
                    correspParameter.addElement(new Parameter("COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING,
                    "DEFAULT"));

                         selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$PROTO_CORRESP_TEMPL " +
                    "WHERE PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> " +
                    " AND COMMITTEE_ID =  <<COMMITTEE_ID>>";
                    
                    resultRow = null;
                    result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",correspParameter);
                    if( !result.isEmpty() ){
                        resultRow = (HashMap)result.get(0);
                        java.io.ByteArrayOutputStream template =
                        (java.io.ByteArrayOutputStream)resultRow.get("CORESPONDENCE_TEMPLATE");
                        // returns the template in byte array.
                        return template.toByteArray();
                    }
                    }
                }
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        return null;
    }
    
    
    /**
     *  This method used to get all protocol correspondence types
     *  from OSP$PROTO_CORRESP_TYPE.
     *  <li>To fetch the data, it uses the procedure get_proto_corresp_types.
     *
     *  @return Vector collection of all Protocol correspondence types
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getValidProtoActionCorrespTypes(ProtocolActionsBean protocolActionsBean) throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        Vector correspTypes = new Vector();
        HashMap correspTypeRow = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            
            param.addElement(new Parameter("ACTION_TYPE_CODE",
            DBEngineConstants.TYPE_STRING,
            ""+protocolActionsBean.getActionTypeCode()));            
            
            result = dbEngine.executeRequest("Coeus",
            "call GET_VALID_PROTO_ACTION_CORRESP( << ACTION_TYPE_CODE >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actionCount = result.size();
        for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
            correspTypeRow = (HashMap)result.elementAt(rowIndex);
            CorrespondenceTypeFormBean correpTypeFormBean =
            new CorrespondenceTypeFormBean();
            correpTypeFormBean.setProtoCorrespTypeCode(Integer.parseInt(correspTypeRow.get("PROTO_CORRESP_TYPE_CODE").toString()));
            // Code added for 4.3 Enhancement - starts
            correpTypeFormBean.setFinalFlag((correspTypeRow.get("FINAL_FLAG")==null)? false : 
                    correspTypeRow.get("FINAL_FLAG").equals("Y")? true : false);
            // Code added for 4.3 Enhancement - ends
            correspTypes.addElement(correpTypeFormBean);
        }
        return correspTypes;
    }    
    
    /**
     * This method is used to insert Correspondence Template into database.
     * @param correspondenceTypeFormBean CorrespondenceTypeFormBean
     * @param fileData byte data of the New PDF Template
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized boolean addUpdProtocolCorrespondence(ProtocolActionsBean protocolActionsBean, int protoCorrespTypeCode ,
        byte[] fileData) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector correspParameter = new Vector();
        boolean isUpdated = false;
        String newAgendaID = null ;
        Vector agendaParam = new Vector() ;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Vector procedures = new Vector();
        
        String statement = "";
        correspParameter.addElement(new Parameter("PROTOCOL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        protocolActionsBean.getProtocolNumber()));
        correspParameter.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+protocolActionsBean.getSequenceNumber()));
        correspParameter.addElement(new Parameter("ACTION_ID",
        DBEngineConstants.TYPE_INT,
        ""+protocolActionsBean.getActionId()));            
        correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+protoCorrespTypeCode));            
        correspParameter.addElement(new Parameter("CORRESPONDENCE",
        DBEngineConstants.TYPE_BLOB, fileData));            
        correspParameter.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));
        correspParameter.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));
        // Code added for 4.3 Enhancement - starts
        correspParameter.addElement(new Parameter("FINAL_FLAG",
        DBEngineConstants.TYPE_STRING,
        protocolActionsBean.getFinalFlag()));
        // Code added for 4.3 Enhancement - ends

//        statement = "insert into OSP$PROTOCOL_CORRESPONDENCE( PROTOCOL_NUMBER , "+
//        "SEQUENCE_NUMBER, ACTION_ID, PROTO_CORRESP_TYPE_CODE, CORRESPONDENCE, UPDATE_TIMESTAMP, UPDATE_USER) "+
//        "values ( <<PROTOCOL_NUMBER>> , <<SEQUENCE_NUMBER>> , <<ACTION_ID>>, << PROTO_CORRESP_TYPE_CODE >>, "+
//        "<<CORRESPONDENCE>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>> )";
//        
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - start                                          
//        /*String selectQry = "SELECT CORRESPONDENCE FROM OSP$PROTOCOL_CORRESPONDENCE " +
//        "WHERE UPDATE_TIMESTAMP =  ?"+
//        " FOR UPDATE OF CORRESPONDENCE ";*/
//        String selectQry = "UPDATE OSP$PROTOCOL_CORRESPONDENCE SET CORRESPONDENCE = ? " +
//        " WHERE UPDATE_TIMESTAMP =  ?";
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - end        
//        
//        if(dbEngine!=null) {
//            boolean status = dbEngine.insertPdfBlob("Coeus", statement,
//            correspParameter,selectQry,fileData,dbTimestamp);
//            
//            isUpdated = status ;
//        }
//        else {
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        
        
        // Code modified for 4.3 Enhancement - starts
        statement = "insert into OSP$PROTOCOL_CORRESPONDENCE( PROTOCOL_NUMBER , "+
        "SEQUENCE_NUMBER, ACTION_ID, PROTO_CORRESP_TYPE_CODE, CORRESPONDENCE, UPDATE_TIMESTAMP, UPDATE_USER, FINAL_FLAG) "+
        "values ( <<PROTOCOL_NUMBER>> , <<SEQUENCE_NUMBER>> , <<ACTION_ID>>, << PROTO_CORRESP_TYPE_CODE >>, "+
        "<<CORRESPONDENCE>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<FINAL_FLAG>> )";
        // Code modified for 4.3 Enhancement - ends
        if(dbEngine!=null) {
            dbEngine.executePreparedQuery("Coeus",statement,correspParameter);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }    
    
    
    /**
     * This method is used to update Correspondence Template into database.
     * @param correspondenceTypeFormBean CorrespondenceTypeFormBean
     * @param fileData byte data of the New PDF Template
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public synchronized boolean UpdateProtocolCorrespondence(ProtocolActionsBean protocolActionsBean, int protoCorrespTypeCode ,
        byte[] fileData) throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector correspParameter = new Vector();
        boolean isUpdated = false;
        String newAgendaID = null ;
        Vector agendaParam = new Vector() ;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Vector procedures = new Vector();
        
        String statement = "";

        correspParameter.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));
        correspParameter.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));
        correspParameter.addElement(new Parameter("PROTOCOL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        protocolActionsBean.getProtocolNumber()));
        correspParameter.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+protocolActionsBean.getSequenceNumber()));
        correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+protoCorrespTypeCode));            
        correspParameter.addElement(new Parameter("CORRESPONDENCE",
        DBEngineConstants.TYPE_BLOB, fileData));            
        correspParameter.addElement(new Parameter("ACTION_ID",
        DBEngineConstants.TYPE_INT,
        ""+protocolActionsBean.getActionId())); 
        //Commented for case #1961 start 1
//        statement = "update OSP$PROTOCOL_CORRESPONDENCE "
//        +" set UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>> , " + 
//        " UPDATE_USER = <<UPDATE_USER>> " +
//        " WHERE PROTOCOL_NUMBER = <<PROTOCOL_NUMBER>> and SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>> " +
//        " and PROTO_CORRESP_TYPE_CODE = <<PROTO_CORRESP_TYPE_CODE>>  and ACTION_ID = <<ACTION_ID>> " ;
//        
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - start                                                  
//        /*
//        String selectQry = "SELECT CORRESPONDENCE FROM OSP$PROTOCOL_CORRESPONDENCE " +
//        "WHERE UPDATE_TIMESTAMP =  ?"+
//        " FOR UPDATE OF CORRESPONDENCE ";*/
//        String selectQry = "UPDATE OSP$PROTOCOL_CORRESPONDENCE SET CORRESPONDENCE = ? " +
//        "WHERE UPDATE_TIMESTAMP =  ?";
//        //Modified to implement DataSource and 
//        //to use Java CallableStatement instead of Oracle - end
//        
//        if(dbEngine!=null) {
//            boolean status = dbEngine.insertPdfBlob("Coeus", statement,
//            correspParameter,selectQry,fileData,dbTimestamp);
//            
//            isUpdated = status ;
//        }
//        else { 
//            throw new CoeusException("db_exceptionCode.1000");
//        }
        //Commented for case #1961 end 1
        //Added for case #1961 start 2
        ProtocolDataTxnBean protocolDataTxnBean
            = new ProtocolDataTxnBean();
        
        Vector vctCorrespList = protocolDataTxnBean.getAllCorrespondenceDocuments(
                protocolActionsBean.getProtocolNumber(),
                protocolActionsBean.getSequenceNumber(),
                protocolActionsBean.getActionId());
        if(vctCorrespList!= null && vctCorrespList.size() > 0){
        
            statement = "update OSP$PROTOCOL_CORRESPONDENCE "
            +" set UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>> , " + 
            " UPDATE_USER = <<UPDATE_USER>>, " +
            " CORRESPONDENCE = <<CORRESPONDENCE>> " +
            " WHERE PROTOCOL_NUMBER = <<PROTOCOL_NUMBER>> and SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>> " +
            " and PROTO_CORRESP_TYPE_CODE = <<PROTO_CORRESP_TYPE_CODE>>  and ACTION_ID = <<ACTION_ID>> " ;
        }else{
               statement = "insert into OSP$PROTOCOL_CORRESPONDENCE( PROTOCOL_NUMBER , "+
            "SEQUENCE_NUMBER, ACTION_ID, PROTO_CORRESP_TYPE_CODE, CORRESPONDENCE, UPDATE_TIMESTAMP, UPDATE_USER) "+
            "values ( <<PROTOCOL_NUMBER>> , <<SEQUENCE_NUMBER>> , <<ACTION_ID>>, << PROTO_CORRESP_TYPE_CODE >>, "+
            "<<CORRESPONDENCE>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>> )";
        }
        //Added for case #1961 end 2
        if(dbEngine!=null) {
            dbEngine.executePreparedQuery("Coeus", statement,correspParameter);
        }
        else { 
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    
    
    
    
    /**
     *  This method used to get all recipients for the given Proto_Corrresp_Type_Code, Protocol Number and Sequence Number. 
     *  First it gets all Qualifiers from OSP$CORRESPONDENT_TYPE for the given Proto_Corrresp_Type_Code. 
     *  If the Qualifier is "P" it searches in OSP$PROTOCOL_CORRESPONDENT table to get all recipients.
     *  If the Qualifier is "U" it searches in OSP$UNIT_CORRESPONDENT table to get all recipients.
     *  <li>To fetch the data, it uses GET_CORRESPONDENT_QUALIFIER and GET_CORRESPONDENCE_RECIPIENTS procedures
     *
     *  @return Vector collection of ProtoCorrespRecipientsBean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCorrespondenceReceipients(String protocolNumber, int sequenceNumber, int protoCorrespTypeCode) throws CoeusException,DBException{
        Vector qualifierResult = null;
        Vector recipientResult = null;
        Vector param= new Vector();
        Vector receipients = new Vector();
        HashMap correspTypeRow = null;
        HashMap recepientsRow = null;
        Hashtable hshReceipients = new Hashtable();
        //MailInfoBean mailInfoBean = null;
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
        if(dbEngine != null){
            qualifierResult = new Vector(3,2);
            
            param.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespTypeCode));            
            
            qualifierResult = dbEngine.executeRequest("Coeus",
            "call GET_CORRESPONDENT_QUALIFIER( << PROTO_CORRESP_TYPE_CODE >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actionCount = qualifierResult.size();
        String strQualifier = "";
        String strEmailId = "";
        int correspondentTypeCode ;
        int recipientCount = 0;
        for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
            correspTypeRow = (HashMap)qualifierResult.elementAt(rowIndex);
            strQualifier = (String)correspTypeRow.get("QUALIFIER");
            correspondentTypeCode = Integer.parseInt(correspTypeRow.get("CORRESPONDENT_TYPE_CODE").toString());

            param= new Vector();
            param.addElement(new Parameter("CORRESPONDENT_QUALIFIER",
            DBEngineConstants.TYPE_STRING, strQualifier));            
            param.addElement(new Parameter("PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING, protocolNumber));                        
            param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+sequenceNumber));                                    
            param.addElement(new Parameter("CORRESPONDENT_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+correspondentTypeCode));            
           
            recipientResult = dbEngine.executeRequest("Coeus",
                "call GET_CORRESPONDENCE_RECIPIENTS( << CORRESPONDENT_QUALIFIER >>, "+
                    " << PROTOCOL_NUMBER >>,"+
                    " << SEQUENCE_NUMBER >>,"+
                    " << CORRESPONDENT_TYPE_CODE >>,"+                    
                    " <<OUT RESULTSET rset>> )", "Coeus", param);            

            if(recipientResult!=null){
                for(int intRecp = 0;intRecp<recipientResult.size();intRecp++){
                    recepientsRow = (HashMap)recipientResult.elementAt(intRecp);
                    if(!hshReceipients.containsKey(recepientsRow.get("PERSON_ID"))){
                        strEmailId = (String)recepientsRow.get("EMAIL_ADDRESS");
                        if(strEmailId!=null){
                            hshReceipients.put(recepientsRow.get("PERSON_ID"), recepientsRow.get("EMAIL_ADDRESS"));
                            protoCorrespRecipientsBean = new ProtoCorrespRecipientsBean();
                            protoCorrespRecipientsBean.setProtocolNumber((String)recepientsRow.get("PROTOCOL_NUMBER"));
                            protoCorrespRecipientsBean.setPersonId((String)recepientsRow.get("PERSON_ID"));
                            protoCorrespRecipientsBean.setPersonName((String)recepientsRow.get("FULL_NAME"));
                            protoCorrespRecipientsBean.setMailId((String)recepientsRow.get("EMAIL_ADDRESS"));
                            protoCorrespRecipientsBean.setNonEmployeeFlag(((String)recepientsRow.get("NON_EMPLOYEE_FLAG")).equalsIgnoreCase("y") 
                                ? true : false);
                            protoCorrespRecipientsBean.setComments((String)recepientsRow.get("COMMENTS"));
                            receipients.addElement(protoCorrespRecipientsBean);                            
                        }
                    }
                }
            }
        }
        return receipients;
    }
    
//Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place    
//    public boolean sendMail(String mailIds,Vector attachmentFilePath){
//        boolean isSend = false ;
//        String strEnabled ;
//        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
////        UtilFactory UtilFactory = new UtilFactory();
//        try{
//            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();            
//            //Check if mail service is enabled
//            strEnabled = departmentPersonTxnBean.getParameterValues("CMS_ENABLED");
//            if(!strEnabled.equalsIgnoreCase("1")){
//                return false;
//            }
//            
//            //To get email Id of logged in User
//            CoeusFunctions coeusFunctions = new CoeusFunctions();
//            UserInfoBean userInfoBean;
//            
//            ReadJavaMailProperties readJavaMailProperties = new ReadJavaMailProperties();
//            SetMailAttributes setMailAttributes = new SetMailAttributes();
//            String strMode = departmentPersonTxnBean.getParameterValues("CMS_MODE");
//            String strToAddress;
//            String strFromAddress = departmentPersonTxnBean.getParameterValues("CMS_SENDER_ID");
//            if(strMode.equalsIgnoreCase("T")){
//                strToAddress = departmentPersonTxnBean.getParameterValues("CMS_TEST_MAIL_RECEIVE_ID");
//                setMailAttributes.setTo(strToAddress);
//            }else{
//                setMailAttributes.setTo(mailIds);
//            }
//            
//            String mailMsg = coeusMessageResourcesBean.parseMessageKey(
//                                "mailMessage_exceptionCode.7601");
//            
//            setMailAttributes.setFrom(strFromAddress);
//            setMailAttributes.setSubject("Correspondence document");
//            setMailAttributes.setMessage(mailMsg);            
//            setMailAttributes.setSend("Y");
//            if(attachmentFilePath!=null){
//                setMailAttributes.setAttachmentPresent(true);
//                String filePath = "";
//                filePath = (String) attachmentFilePath.elementAt(0);
//                setMailAttributes.setFileName(filePath);
//                String fileName = "";
//                if(filePath.lastIndexOf("\\") > 0){
//                    fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());    
//                }else{
//                    fileName = "Attachment.pdf";
//                }
//                setMailAttributes.setAttachmentName(fileName);
//            }                
//            SendJavaMail sendJavaMail = new SendJavaMail(setMailAttributes,readJavaMailProperties);
//
//            isSend = sendJavaMail.sendMessage();        
//            if(isSend){
//                UtilFactory.log("The message : \n"+mailMsg +"\n has been sent to the following recipients : "+mailIds +
//                    "\n", null, "ProtoCorrespTypeTxnBean","sendMail");
//            }
//        }catch(Exception ex){
//            //ex.printStackTrace();
//            UtilFactory.log(ex.getMessage(), ex, "ProtoCorrespTypeTxnBean","sendMail");
//        }
//        return isSend;
//    }    
    
    public boolean addUpdCorrespRecipients(Vector vctProtoCorrespRecipientsBean)
    throws CoeusException, DBException{
        
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
        
        
        int intsize = vctProtoCorrespRecipientsBean.size();
        for(int row = 0;row<intsize;row++){
            Vector paramTypes= new Vector();
            protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)vctProtoCorrespRecipientsBean.elementAt(row);

            paramTypes.addElement(new Parameter("PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.getProtocolNumber()));
            
            paramTypes.addElement(new Parameter("ACTION_ID",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespRecipientsBean.getActionId()));
            
            paramTypes.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespRecipientsBean.getProtoCorrespTypeCode()));            
            
            paramTypes.addElement(new Parameter("PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.getPersonId()));        
            
            paramTypes.addElement(new Parameter("PERSON_NAME",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.getPersonName()));        
            
            paramTypes.addElement(new Parameter("NON_EMPLOYEE_FLAG",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.isNonEmployeeFlag() == true ? "Y" : "N"));                    
            
            paramTypes.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.getComments()));        
            
            paramTypes.addElement(new Parameter("NUMBER_OF_COPIES",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespRecipientsBean.getNumberOfCopies()));                        

            paramTypes.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,
            userId));
            
            paramTypes.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            dbTimestamp));
            
            paramTypes.addElement(new Parameter("AW_PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.getProtocolNumber()));
            
            paramTypes.addElement(new Parameter("AW_ACTION_ID",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespRecipientsBean.getActionId()));
            
            paramTypes.addElement(new Parameter("AW_PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespRecipientsBean.getProtoCorrespTypeCode()));            
            
            paramTypes.addElement(new Parameter("AW_PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.getPersonId()));        
            
            paramTypes.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            protoCorrespRecipientsBean.getUpdateTimestamp()));
            
            paramTypes.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            protoCorrespRecipientsBean.getAcType()));

            StringBuffer sqlUpdProtoCorrespTypes = new StringBuffer(
            "call UPD_PROTO_CORRESP_RECIPIENTS( ");

            
            sqlUpdProtoCorrespTypes.append(" <<PROTOCOL_NUMBER>> , ");
            sqlUpdProtoCorrespTypes.append(" <<ACTION_ID>> , ");
            sqlUpdProtoCorrespTypes.append(" <<PROTO_CORRESP_TYPE_CODE>> , ");
            sqlUpdProtoCorrespTypes.append(" <<PERSON_ID>> , ");
            sqlUpdProtoCorrespTypes.append(" <<PERSON_NAME>> , ");
            sqlUpdProtoCorrespTypes.append(" <<NON_EMPLOYEE_FLAG>> , ");
            sqlUpdProtoCorrespTypes.append(" <<COMMENTS>> ,");
            sqlUpdProtoCorrespTypes.append(" <<NUMBER_OF_COPIES>> ,");
            sqlUpdProtoCorrespTypes.append(" <<UPDATE_USER>> ,");            
            sqlUpdProtoCorrespTypes.append(" <<UPDATE_TIMESTAMP>> ,");
            sqlUpdProtoCorrespTypes.append(" <<AW_PROTOCOL_NUMBER>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AW_ACTION_ID>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AW_PROTO_CORRESP_TYPE_CODE>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AW_PERSON_ID>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlUpdProtoCorrespTypes.append(" <<AC_TYPE>> )");
            
            ProcReqParameter procCorrespType  = new ProcReqParameter();
            procCorrespType.setDSN("Coeus");
            procCorrespType.setParameterInfo(paramTypes);
            procCorrespType.setSqlCommand(sqlUpdProtoCorrespTypes.toString());
            procedures.addElement(procCorrespType);
        }
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return true;
    }    
    
//    private boolean generateCorrespondence(ProtocolActionsBean protocolActionsBean)
//        throws CoeusException,DBException,Exception{
//        boolean success = false;
//        Vector vctReceipients = null;
//        byte[] templateFileBytes;
//        Vector vctCorrespTypes = null;
//
//        Hashtable inputParamValues = new Hashtable();
//        String mailIds = "";
//        Vector attachmentFilePath = new Vector();
//        MailInfoBean mailInfoBean = null;
//        vctCorrespTypes = getValidProtoActionCorrespTypes(protocolActionsBean);
//        CorrespondenceTypeFormBean correpTypeFormBean = null;
//        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
//        Vector vctRecp = new Vector();
//        if(vctCorrespTypes!=null){
//            for(int i=0; i<vctCorrespTypes.size() ; i++ ){
//                correpTypeFormBean = (CorrespondenceTypeFormBean)vctCorrespTypes.elementAt(i);
//                templateFileBytes = getCorrespTemplate(correpTypeFormBean.getProtoCorrespTypeCode(), protocolActionsBean.getScheduleId());
//                inputParamValues.put("SCHEDULE_ID", protocolActionsBean.getScheduleId());
//                inputParamValues.put("PROTOCOL_NUMBER", protocolActionsBean.getProtocolNumber());
//                inputParamValues.put("ACTION_ID", new Integer(protocolActionsBean.getActionId()));
//                inputParamValues.put("SEQUENCE_NUMBER", new Integer(protocolActionsBean.getSequenceNumber()));
//                ProcessReportXMLBean bean = new ProcessReportXMLBean("CORRESREPORT", templateFileBytes, inputParamValues);
//                String newfileRelativeName = bean.getPdfFileName();
//                templateFileBytes = bean.getPdfFileBytes();
//                success = addUpdProtocolCorrespondence(protocolActionsBean, correpTypeFormBean.getProtoCorrespTypeCode(), templateFileBytes);
//                
//                vctReceipients = getCorrespondenceReceipients(protocolActionsBean.getProtocolNumber(),protocolActionsBean.getSequenceNumber(),correpTypeFormBean.getProtoCorrespTypeCode());
//                String strMailIds = "";
//                vctRecp = new Vector();
//                if(vctReceipients!=null){
//                    for(int intRecepRow = 0 ; intRecepRow < vctReceipients.size();intRecepRow++){
//                        protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)vctReceipients.elementAt(intRecepRow);
//                        strMailIds =  strMailIds + protoCorrespRecipientsBean.getMailId()+",";
//                        protoCorrespRecipientsBean.setProtocolNumber(protocolActionsBean.getProtocolNumber());
//                        protoCorrespRecipientsBean.setProtoCorrespTypeCode(correpTypeFormBean.getProtoCorrespTypeCode());
//                        protoCorrespRecipientsBean.setActionId(protocolActionsBean.getActionId());
//                        protoCorrespRecipientsBean.setAcType("I");
//                        protoCorrespRecipientsBean.setNumberOfCopies(1);
//                        vctRecp.addElement(protoCorrespRecipientsBean);
//                    }
//                    if(vctRecp.size()>0){
//                        success = addUpdCorrespRecipients(vctRecp);
//                        strMailIds = strMailIds.substring(0,strMailIds.length()-1);
//                        attachmentFilePath.addElement(bean.getPdfAbsolutePath());
//                        //mailIds = "prasannak@nous.soft.net";
//                        success = sendMail(mailIds, attachmentFilePath);
//                    }
//                }
//            }
//        }        
//        return success;
//    }
    
    public ProcReqParameter updateCorrepTemplate(CorrespondenceTypeFormBean correspondenceTypeFormBean)
            throws CoeusException,DBException{
        Vector correspParameter = new Vector();
        
        correspParameter.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+correspondenceTypeFormBean.getProtoCorrespTypeCode()));
        correspParameter.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));
        correspParameter.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));
        correspParameter.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        correspondenceTypeFormBean.getCommitteeId()));        
        correspParameter.addElement(new Parameter("AW_PROTO_CORRESP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+correspondenceTypeFormBean.getProtoCorrespTypeCode()));
        correspParameter.addElement(new Parameter("AW_COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        correspondenceTypeFormBean.getCommitteeId()));        
        correspParameter.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        correspondenceTypeFormBean.getUpdateTimestamp()));
        correspParameter.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        ""+correspondenceTypeFormBean.getAcType()));

        StringBuffer sqlUpdProtoCorrespTypes = new StringBuffer(
        "call UPD_PROTO_CORRESP_TEMPL(");
        sqlUpdProtoCorrespTypes.append(" <<PROTO_CORRESP_TYPE_CODE>> , ");
        sqlUpdProtoCorrespTypes.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlUpdProtoCorrespTypes.append(" <<UPDATE_USER>> , ");
        sqlUpdProtoCorrespTypes.append(" <<COMMITTEE_ID>> , ");        
        sqlUpdProtoCorrespTypes.append(" <<AW_PROTO_CORRESP_TYPE_CODE>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AW_COMMITTEE_ID>> , ");                    
        sqlUpdProtoCorrespTypes.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AC_TYPE>> )");

        ProcReqParameter procCorrespType  = new ProcReqParameter();
        procCorrespType.setDSN("Coeus");
        procCorrespType.setParameterInfo(correspParameter);
        procCorrespType.setSqlCommand(sqlUpdProtoCorrespTypes.toString());
       
        return procCorrespType;
    }
    
    //Main method to test the procedures
     /*public static void main(String args[]) {
        ProtoCorrespTypeTxnBean txnUpd = new ProtoCorrespTypeTxnBean("COEUS");
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        
        
        try{
            boolean success = false;
            Vector vctReceipients = null;
            byte[] templateFileBytes;
            Vector vctCorrespTypes = null;
            ProtocolActionsBean protocolActionsBean;
            Vector vctActionBeans;
            CorrespondenceTypeFormBean correspondenceTypeFormBean = null;
            vctCorrespTypes = txnUpd.getProtoCorrespTypes();
            if(vctCorrespTypes!=null){
                System.out.println("Size : " +vctCorrespTypes.size());    
                correspondenceTypeFormBean = (CorrespondenceTypeFormBean)vctCorrespTypes.elementAt(0);
                System.out.println("File Bytes : "+correspondenceTypeFormBean.getFileBytes());
            }else{
                System.out.println("is null");
            }*/
       
            //vctActionBeans = protocolDataTxnBean.getProtocolActions("0000001801",1);

            //protocolActionsBean = (ProtocolActionsBean)vctActionBeans.elementAt(1);
            
            //success = txnUpd.generateCorrespondence(protocolActionsBean);
            
            //success = txnUpd.addUpdCorrespRecipients();           
           
            /*
            CorrespondenceTypeFormBean correspondenceTypeFormBean = new CorrespondenceTypeFormBean();
            correspondenceTypeFormBean.setProtoCorrespTypeCode(1);
            correspondenceTypeFormBean.setCommitteeId("Test Committee");
            correspondenceTypeFormBean.setAcType('I');
             
             File tempFile = new File(edu.mit.coeus.utils.CoeusConstants.SERVER_HOME_PATH+"Reports"+File.separator+"Test1.pdf");
             FileInputStream fis = new FileInputStream(tempFile);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             byte[] buffer = new byte[100];
             int length = -1;
             while ((length = fis.read(buffer)) != -1)
                 baos.write(buffer, 0, length);
             fis.close();
             baos.flush();
             baos.close();
             byte[] generatedFileBytes = baos.toByteArray();
             
             boolean isFileInsertedIntoDB = txnUpd.addUpdProtoCorrespTypeTempl(correspondenceTypeFormBean, generatedFileBytes );
             */
            /*
            byte[] generatedFileBytes = txnUpd.getCorrespTemplate(1,"Test Committee12");
             
            String filePath = edu.mit.coeus.utils.CoeusConstants.SERVER_HOME_PATH+File.separator+"Reports";
            File reportDir = new File(filePath);
            if(!reportDir.exists()){
             reportDir.mkdirs();
            }
            java.text.SimpleDateFormat dateFormat= new java.text.SimpleDateFormat("MMddyyyy-hhmmss");
            File reportFile = new File(reportDir + File.separator + "Test"+dateFormat.format(new java.util.Date())+".pdf");
            FileOutputStream fos = new FileOutputStream(reportFile);
            fos.write( generatedFileBytes,0,generatedFileBytes.length );
            fos.close();
             */
    
    
    
            /*if(isFileInsertedIntoDB)
                System.out.println("successfully inserted");
            else
                System.out.println("exception while insert");*/
       /*        
       }catch(Exception e){
            e.printStackTrace();
        }
    }  */
}