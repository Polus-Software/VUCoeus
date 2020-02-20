/*
 * S2SSubmissionDataTxnBean.java
 *
 * Created on January 6, 2005, 4:17 PM
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.validator.OpportunitySchemaParser;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import gov.grants.apply.soap.util.SoapUtils;
import gov.grants.apply.system.footer_v1.GrantSubmissionFooterType;
import gov.grants.apply.system.header_v1.GrantSubmissionHeaderType;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author  geot
 */
public class S2SSubmissionDataTxnBean implements IS2SDataTxn{
    private S2SXMLInfoBean submissionInfo;
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    private CoeusFunctions coeusFunctions;
    private String userId;
    private static final String SCHEMA_VERSION = "1.0";
    /** Creates a new instance of S2SSubmissionDataTxnBean */
    public S2SSubmissionDataTxnBean() {
        submissionInfo = new S2SXMLInfoBean();
        dbEngine = new DBEngineImpl();
        coeusFunctions = new CoeusFunctions();
    }
    
    public IS2SSubmissionData getSubmissionData(S2SHeader headerParam)
    throws S2SValidationException,CoeusException{
        //        S2SHeader headerParam = (S2SHeader)objParams;
        try{
            submissionInfo.setLocalOpportunity(getLocalOpportunity(headerParam));
            submissionInfo.setHeader(getHeader(headerParam));
            submissionInfo.setFooter(getFooter(headerParam));
            submissionInfo.setSelectedOptionalForms(getSelectedOptionalForms(headerParam));
            submissionInfo.setParams(headerParam);
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SSubmissionDataTxnBean", "getSubmissionData");
            throw new S2SValidationException(ex.getMessage());
        }
        return submissionInfo;
    }
    private GrantSubmissionHeaderType getHeader(S2SHeader param) throws Exception{
        ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
        ProposalDevelopmentFormBean propDevFromBean = propTxnBean.getProposalDevelopmentDetails(param.getSubmissionTitle());
        gov.grants.apply.system.header_v1.GrantSubmissionHeaderType header;
        header = new gov.grants.apply.system.header_v1.ObjectFactory().createGrantSubmissionHeader();
            /*
             *truncate the title to 120, since activity title max length is 120
             *Using program announcement title for both activity title and opportuinty title
             */
        String title = propDevFromBean.getProgramAnnouncementTitle();
        header.setActivityTitle(title==null?null:title.length()>120?title.substring(0,120):title);
        header.setOpportunityTitle(title);
        header.setAgencyName(propDevFromBean.getSponsorName());
        if(param.getCfdaNumber()!=null){
            StringBuffer tmpCfda = new StringBuffer(param.getCfdaNumber());
            if(tmpCfda.length() >= 3 && tmpCfda.indexOf(".")==-1){
                tmpCfda.insert(2,'.');
            }
            header.setCFDANumber(tmpCfda.toString());
        }
            /*
             *To fix the Competition id length=0 issue.
             *Get it from db
             */
        DBOpportunityInfoBean opp = (DBOpportunityInfoBean)submissionInfo.getLocalOpportunity();
        header.setCompetitionID(opp.getCompetitionId()); 
        // putting opportunity closing and opening date in application header start
        if(opp.getOpeningDate() != null){
            java.util.Calendar openingDate = Calendar.getInstance();
            openingDate.setTimeInMillis(opp.getOpeningDate().getTime());          
            header.setOpeningDate(openingDate);
        }
        if(opp.getClosingDate() != null){
            java.util.Calendar closingDate = Calendar.getInstance();
            closingDate.setTimeInMillis(opp.getClosingDate().getTime());          
            header.setClosingDate(closingDate);
        }        
      // putting opportunity closing and opening date in application header ends  
        header.setOpportunityID(opp.getOpportunityId());
        header.setSchemaVersion(SCHEMA_VERSION);
        header.setSubmissionTitle(opp.getProposalNumber());
        
        //            header.setCompetitionID(param.getCompetitionId());
        ////            header.setOpportunityID(propDevFromBean.getProgramAnnouncementNumber());
        //            header.setOpportunityID(param.getOpportunityId());
        //            header.setSchemaVersion("1.0");
        //            header.setSubmissionTitle(param.getSubmissionTitle());
        return header;
        
    }
    
    private GrantSubmissionFooterType getFooter(S2SHeader param) throws Exception{
        
        gov.grants.apply.system.footer_v1.GrantSubmissionFooterType footer;
        footer = new gov.grants.apply.system.footer_v1.ObjectFactory().createGrantSubmissionFooter();
        footer.setSchemaVersion("1.0");
        footer.setSubmitterName("MIT");
        return footer;
        
    }
    /**
     *  The method used to fetch the application details which submitted to Grants.gov
     *  Object array holds three objects.
     *  <li>OpportunityInfoBean, selected opportunity, if any, else null
     *  <li>ArrayList of FormInfoBean, selected forms, if any, else null
     *  <li>ApplicationInfoBean, submitted application details, if any, else null
     */
    public Object[] getS2SDetails(S2SHeader params) throws DBException{
        Object [] s2sDetails = {getLocalOpportunity(params),
        getOppFormDetails(params),
        getSubmissionDetails(params)};
        return s2sDetails;
    }
    public ApplicationInfoBean getSubmissionDetails(S2SHeader params) throws DBException {
        return getSubmissionDetails(params.getSubmissionTitle());
    }
    public ApplicationInfoBean getSubmissionDetails(String proposalNumber) throws DBException {
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
        proposalNumber));
        result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_SUB_DETAILS ( <<PROPOSAL_NUMBER>>, " +
        "<<OUT RESULTSET rset>> )",
        "Coeus", param);
        SubmissionDetailInfoBean appInfo = null;
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            appInfo = new SubmissionDetailInfoBean();
            appInfo.setSubmissionTitle((String)rowParameter.get("PROPOSAL_NUMBER"));
            appInfo.setSubmissionNumber(Integer.parseInt(rowParameter.get("SUBMISSION_NUMBER").toString()));
            appInfo.setAgencyTrackingNumber((String)rowParameter.get("AGENCY_TRACKING_ID"));
            appInfo.setGrantsGovTrackingNumber((String)rowParameter.get("GG_TRACKING_ID"));
            appInfo.setComments((String)rowParameter.get("COMMENTS"));
            appInfo.setReceivedDateTime((Timestamp)rowParameter.get("RECEIVED_DATE"));
            appInfo.setStatusDate((Timestamp)rowParameter.get("LAST_MODIFIED_DATE"));
            appInfo.setStatus((String)rowParameter.get("STATUS"));
            appInfo.setUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
            appInfo.setUpdateUser((String)rowParameter.get("UPDATE_USER"));
            appInfo.setAwUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
            appInfo.setAwProposalNumber((String)rowParameter.get("PROPOSAL_NUMBER"));
            appInfo.setAwSubimissionNumber(Integer.parseInt(rowParameter.get("SUBMISSION_NUMBER").toString()));
            appInfo.setAcType('U');
            appInfo.setAttachments(getAttachments(proposalNumber));
        }
        
        return appInfo;
        
    }
    public Vector getSubmissionDetailsForPoll(String statuses,int stopPollInterval) throws DBException {
        Vector param= new Vector();
        Vector result = new Vector();
        Vector subList = new Vector();
        int number = 0;
        param.add(new Parameter("STATUS",DBEngineConstants.TYPE_STRING,
        statuses));
        param.add(new Parameter("STOP_POLL_INTERVAL",DBEngineConstants.TYPE_STRING,
        stopPollInterval==0?null:""+stopPollInterval));
        result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_SUB_DETAILS_FOR_POLL ( <<STATUS>>, <<STOP_POLL_INTERVAL>>, " +
        "<<OUT RESULTSET rset>> )",
        "Coeus", param);
        SubmissionDetailInfoBean appInfo = null;
        //        if(!result.isEmpty()){
        for(int i=0;i<result.size();i++){
            HashMap rowParameter = (HashMap)result.elementAt(i);
            appInfo = new SubmissionDetailInfoBean();
            appInfo.setSubmissionTitle((String)rowParameter.get("PROPOSAL_NUMBER"));
            appInfo.setSubmissionNumber(Integer.parseInt(rowParameter.get("SUBMISSION_NUMBER").toString()));
            appInfo.setAgencyTrackingNumber((String)rowParameter.get("AGENCY_TRACKING_ID"));
            appInfo.setGrantsGovTrackingNumber((String)rowParameter.get("GG_TRACKING_ID"));
            appInfo.setComments((String)rowParameter.get("COMMENTS"));
            appInfo.setCfdaNumber((String)rowParameter.get("CFDA_NUMBER"));
            appInfo.setOpportunityID((String)rowParameter.get("OPPORTUNITY_ID"));
            appInfo.setReceivedDateTime((Timestamp)rowParameter.get("RECEIVED_DATE"));
            appInfo.setStatusDate((Timestamp)rowParameter.get("LAST_MODIFIED_DATE"));
            appInfo.setStatus((String)rowParameter.get("STATUS"));
            appInfo.setUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
            appInfo.setUpdateUser((String)rowParameter.get("UPDATE_USER"));
            appInfo.setAwUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
            appInfo.setAwProposalNumber((String)rowParameter.get("PROPOSAL_NUMBER"));
            appInfo.setAwSubimissionNumber(Integer.parseInt(rowParameter.get("SUBMISSION_NUMBER").toString()));
            appInfo.setLastNotifiedDate((Timestamp)rowParameter.get("LAST_NOTIFIED_DATE"));
            appInfo.setAcType('U');
            subList.addElement(appInfo);
        }
        
        return subList;
        
    }
    public ArrayList getAttachments(S2SHeader params) throws DBException {
        return getAttachments(params.getSubmissionTitle());
    }
    public ArrayList getAttachments(String porposalNumber) throws DBException {
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
        porposalNumber));
        result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_SUB_ATTACHEMNTS ( <<PROPOSAL_NUMBER>>, " +
        "<<OUT RESULTSET rset>> )",
        "Coeus", param);
        SubmissionDetailInfoBean appInfo = null;
        ArrayList attachments = null;
        if(!result.isEmpty()){
            attachments = new ArrayList(result.size());
            for(int i=0;i<result.size();i++){
                HashMap rowParameter = (HashMap)result.elementAt(i);
                DBAttachmentBean attBean = new DBAttachmentBean();
                attBean.setContentId((String)rowParameter.get("CONTENT_ID"));
                attBean.setHashValue((String)rowParameter.get("HASH_CODE"));
                attBean.setContentType((String)rowParameter.get("CONTENT_TYPE"));
                attBean.setUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
                attBean.setUpdateUser((String)rowParameter.get("UPDATE_USER"));
                attachments.add(attBean);
            }
        }
        return attachments;
        
    }
    private ArrayList getOppFormDetails(S2SHeader headerParams) throws DBException {
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
        headerParams.getSubmissionTitle()));
        result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_OPP_FORMS ( <<PROPOSAL_NUMBER>> , " +
        "<<OUT RESULTSET rset>> )",
        "Coeus", param);
        int size = result.size();
        ArrayList formsList = null;
        FormInfoBean formInfo = null;
        if(!result.isEmpty()){
            formsList = new ArrayList(size);
            for(int i=0;i<size;i++){
                HashMap rowParameter = (HashMap)result.elementAt(i);
                formInfo = new FormInfoBean();
                formInfo.setProposalNumber((String)rowParameter.get("PROPOSAL_NUMBER"));
                formInfo.setNs((String)rowParameter.get("OPP_NAME_SPACE"));
                formInfo.setFormName((String)rowParameter.get("FORM_NAME"));
                formInfo.setMandatory(rowParameter.get("MANDATORY").toString().equalsIgnoreCase("Y"));
                boolean available = rowParameter.get("AVAILABLE").toString().equalsIgnoreCase("Y");
                try {
                	if(!available){
                		available = new UserAttachedS2STxnBean().isFormAvailable(formInfo.getProposalNumber(),formInfo.getNs());
                	}
				} catch (CoeusException e) {}
                formInfo.setAvailable(available);
                formInfo.setInclude(rowParameter.get("INCLUDE").toString().equalsIgnoreCase("Y"));
                formInfo.setUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
                formInfo.setUpdateUser((String)rowParameter.get("UPDATE_USER"));
                formInfo.setAwUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
                formInfo.setAwProposalNumber((String)rowParameter.get("PROPOSAL_NUMBER"));
                formInfo.setAcType('U');
                formsList.add(formInfo);
            }
            try{
                Vector srtList = Converter.sortForms(formsList);
                ArrayList srtdFormsList = new ArrayList(srtList.size());
                srtdFormsList.addAll(srtList);
                return srtdFormsList;
            }catch(CoeusException ex){
                UtilFactory.log("Warning:"+ex.getMessage(),ex, "S2SSubmissionDataTxnBean", "getOppFormDetails");
            }
        }
        return formsList;
    }
    
    public ArrayList getSubmissionList(S2SHeader params) throws DBException{
        return null;
    }
    
    /**  Method used to update/insert/delete S2S Submission details
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetJustificationBean BudgetJustificationBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public boolean addUpdDeleteSubmissionDetails(ApplicationInfoBean infoBean)
    throws Exception{
        Vector infoBeanList = new Vector(1);
        infoBeanList.addElement(infoBean);
        return addUpdDeleteSubmissionDetails(infoBeanList);
    }
    /**  Method used to update/insert/delete S2S Submission details
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetJustificationBean BudgetJustificationBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public boolean addUpdDeleteSubmissionDetails(Vector infoBeanList)
    throws Exception{
        if(dbEngine!=null){
            Connection conn = null;
            Vector allProcedures = new Vector(2,1);
            try{
                conn = dbEngine.beginTxn();
                for(int lstIndex = 0;lstIndex<infoBeanList.size();lstIndex++){
                    //        SubmissionDetailInfoBean appInfo = (SubmissionDetailInfoBean)infoBean;
                    SubmissionDetailInfoBean appInfo = (SubmissionDetailInfoBean)infoBeanList.elementAt(lstIndex);
                    //        System.out.println("Action type="+appInfo.getAcType());
                    Vector paramSubsn = new Vector();
                    Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
                    paramSubsn.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    appInfo.getSubmissionTitle()));
                    paramSubsn.addElement(new Parameter("APPLICATION",
                    DBEngineConstants.TYPE_CLOB,
                    appInfo.getApplicationData()));
                    paramSubsn.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    dbTimestamp));
                    paramSubsn.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING,
                    getUserId()));
                    //        System.out.println("application xml before insterting into db=>"+appInfo.getApplicationData());
                    StringBuffer sqlSubsn = new StringBuffer("");
                    switch(appInfo.getAcType()){
                        case('I'):
                            sqlSubsn.append("insert into OSP$S2S_APPLICATION(");
                            sqlSubsn.append(" PROPOSAL_NUMBER , ");
                            sqlSubsn.append(" APPLICATION , ");
                            sqlSubsn.append(" UPDATE_TIMESTAMP , ");
                            sqlSubsn.append(" UPDATE_USER ) ");
                            sqlSubsn.append(" values (");
                            sqlSubsn.append(" <<PROPOSAL_NUMBER>> , ");
                            sqlSubsn.append(" <<APPLICATION>> , ");
                            sqlSubsn.append(" <<UPDATE_TIMESTAMP>> , ");
                            sqlSubsn.append(" <<UPDATE_USER>> ) ");
                            break;
                    }
                    
                    if(appInfo.getAcType()=='I'){
                        StringBuffer sqlDelSubsn = new StringBuffer("{ <<OUT INTEGER DEL_FLAG>> = ");
                        sqlDelSubsn.append("call FN_DEL_S2S_SUB_APPLICATION(");
                        sqlDelSubsn.append(" <<PROPOSAL_NUMBER>> )}");
                        dbEngine.executeFunctions("Coeus",sqlDelSubsn.toString(),paramSubsn,conn);
                        dbEngine.executePreparedQuery("Coeus",sqlSubsn.toString(),paramSubsn,conn);
                        if(appInfo.getAttachments()!=null && appInfo.getAttachments().size()>0){
                            allProcedures.addAll(updAttachmentProcs(appInfo,dbTimestamp));
                        }
                    }
                    
                    ProcReqParameter submProcReq = null;
                    if((submProcReq = updSubmissionProc(appInfo,dbTimestamp)) != null){
                        allProcedures.add(submProcReq);
//Bug Fix : 2981 - START
                        //Uncommented for Case 3449 - START
                        if(appInfo!=null && appInfo.getAgencyTrackingNumber()!=null && appInfo.getAgencyTrackingNumber().length()>0){
                            allProcedures.add(updateSponsorPropNumber(appInfo.getSubmissionTitle(), appInfo.getAgencyTrackingNumber()));
                        }
                        //Uncommented for Case 3449 - END
//Bug Fix : 2981 - END
                    }
                }
                dbEngine.executeStoreProcs(allProcedures,conn);
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                UtilFactory.log(sqlEx.getMessage(),sqlEx,"S2SSubmissionDataTxnBean", "addUpdDeleteSubmissionData");
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }
        
        return true;
    }
    
    private Vector updAttachmentProcs(SubmissionDetailInfoBean appInfo,Timestamp dbTS)
    throws Exception{
        Vector procs = new Vector();
        
        Vector paramAtt = null;;
        Vector procedures = new Vector(2,1);
        boolean success = false;
        //        if(appInfo!=null)
        //            return procedures;
        ArrayList atts = appInfo.getAttachments();
        int size = atts==null?0:atts.size();
        if(size==0) return null;
        for(int i=0;i<size;i++){
            paramAtt= new Vector();
            Attachment att = (Attachment)atts.get(i);
            paramAtt.addElement(new Parameter("CONTENT_ID",
            DBEngineConstants.TYPE_STRING,
            att.getContentId()));
            paramAtt.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            appInfo.getSubmissionTitle()));
            paramAtt.addElement(new Parameter("HASH_CODE",
            DBEngineConstants.TYPE_STRING,
            att.getHashValue()));
            paramAtt.addElement(new Parameter("CONTENT_TYPE",
            DBEngineConstants.TYPE_STRING,
            att.getContentType()));
            paramAtt.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTS));
            paramAtt.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, getUserId()));
            paramAtt.addElement(new Parameter("AW_CONTENT_ID",
            DBEngineConstants.TYPE_STRING, att.getContentId()));
            paramAtt.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, appInfo.getSubmissionTitle()));
            paramAtt.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            appInfo.getAwUpdateTimestamp()));
            paramAtt.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            ""+appInfo.getAcType()));
            
            StringBuffer sqlBudget = new StringBuffer(
            "call UPD_S2S_SUB_ATTACHMENT(");
            sqlBudget.append(" <<CONTENT_ID>> , ");
            sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudget.append(" <<HASH_CODE>> , ");
            sqlBudget.append(" <<CONTENT_TYPE>> , ");
            sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudget.append(" <<UPDATE_USER>> , ");
            sqlBudget.append(" <<AW_CONTENT_ID>> , ");
            sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
            sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlBudget.append(" <<AC_TYPE>> )");
            
            ProcReqParameter procAtt  = new ProcReqParameter();
            procAtt.setDSN("Coeus");
            procAtt.setParameterInfo(paramAtt);
            procAtt.setSqlCommand(sqlBudget.toString());
            procedures.add(procAtt);
        }
        return procedures;
    }
    private ProcReqParameter updSubmissionProc(SubmissionDetailInfoBean appInfo,
    Timestamp dbTS)
    throws Exception{
        Vector procs = new Vector();
        Vector paramSubsn = new Vector();
        Vector procedures = new Vector(2,1);
        int submNumber = appInfo.getAcType()=='I'?
        (getMaxSubmNumber(appInfo.getSubmissionTitle())+1):appInfo.getSubmissionNumber();
        boolean success = false;
        if(dbTS==null)
            dbTS = coeusFunctions.getDBTimestamp();
        if(appInfo==null)
            return null;
        paramSubsn.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,  appInfo.getSubmissionTitle()));
        paramSubsn.addElement(new Parameter("SUBMISSION_NUMBER",
        DBEngineConstants.TYPE_INT, ""+submNumber));
        paramSubsn.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING, appInfo.getComments()));
        paramSubsn.addElement(new Parameter("STATUS",
        DBEngineConstants.TYPE_STRING,
        appInfo.getStatus()));
        paramSubsn.addElement(new Parameter("GG_TRACKING_ID",
        DBEngineConstants.TYPE_STRING, appInfo.getGrantsGovTrackingNumber()));
        paramSubsn.addElement(new Parameter("AGENCY_TRACKING_ID",
        DBEngineConstants.TYPE_STRING,
        appInfo.getAgencyTrackingNumber()));
        //        paramSubsn.addElement(new Parameter("COMPETITION_ID",
        //        DBEngineConstants.TYPE_STRING,
        //        appInfo.getCompetitionID()));
        paramSubsn.addElement(new Parameter("RECEIVED_DATE",
        DBEngineConstants.TYPE_TIMESTAMP,
        appInfo.getReceivedDateTime()));
        paramSubsn.addElement(new Parameter("LAST_MODIFIED_DATE",
        DBEngineConstants.TYPE_TIMESTAMP,
        appInfo.getStatusDate()));
        paramSubsn.addElement(new Parameter("LAST_NOTIFIED_DATE",
        DBEngineConstants.TYPE_TIMESTAMP,
        appInfo.getLastNotifiedDate()));
        paramSubsn.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTS));
        paramSubsn.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, getUserId()));
        paramSubsn.addElement(new Parameter("AW_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, appInfo.getAwProposalNumber()));
        paramSubsn.addElement(new Parameter("AW_SUBMISSION_NUMBER",
        DBEngineConstants.TYPE_INT, ""+ appInfo.getAwSubimissionNumber()));
        paramSubsn.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        appInfo.getAwUpdateTimestamp()));
        paramSubsn.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        ""+appInfo.getAcType()));
        
        StringBuffer sqlSubsn = new StringBuffer(
        "call UPD_S2S_APP_SUBMISSION(");
        sqlSubsn.append(" <<PROPOSAL_NUMBER>> , ");
        sqlSubsn.append(" <<SUBMISSION_NUMBER>> , ");
        sqlSubsn.append(" <<COMMENTS>> , ");
        sqlSubsn.append(" <<STATUS>> , ");
        sqlSubsn.append(" <<GG_TRACKING_ID>> , ");
        sqlSubsn.append(" <<AGENCY_TRACKING_ID>> , ");
        //        sqlSubsn.append(" <<COMPETITION_ID>> , ");
        sqlSubsn.append(" <<RECEIVED_DATE>> , ");
        sqlSubsn.append(" <<LAST_MODIFIED_DATE>> , ");
        sqlSubsn.append(" <<LAST_NOTIFIED_DATE>> , ");
        sqlSubsn.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlSubsn.append(" <<UPDATE_USER>> , ");
        sqlSubsn.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlSubsn.append(" <<AW_SUBMISSION_NUMBER>> , ");
        sqlSubsn.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlSubsn.append(" <<AC_TYPE>> )");
        //        System.out.println("insert statement=>"+sqlSubsn.toString());
            
        ProcReqParameter procSubmn  = new ProcReqParameter();
        procSubmn.setDSN("Coeus");
        procSubmn.setParameterInfo(paramSubsn);
        procSubmn.setSqlCommand(sqlSubsn.toString());
        return procSubmn;
    }
    
    public boolean addUpdDelSubmission(SubmissionDetailInfoBean appInfo)
    throws Exception{
        updSubmissionProc(appInfo,null);
        return true;
    }

    public boolean addOpportunity(OpportunityInfoBean oppInfo) throws Exception {
        Connection conn = null;
        try {
            conn = dbEngine.beginTxn();
            Vector procedures = addOpportunityProcs(oppInfo);
            dbEngine.executeStoreProcs(procedures, conn);
            dbEngine.commit(conn);
        } catch (Exception ex) {
            dbEngine.rollback(conn);
            UtilFactory.log(ex.getMessage(), ex,
                    "S2SSubmissionDataTxnBean", "addOpportunity");
            throw new CoeusException(ex.getMessage());
        } finally {
            dbEngine.endTxn(conn);
        }
        return true;
    }

    /**
     * Checks for Form Availablility, retreives opportunity content by using schema url
     * and returns Vector with all procedures to be executed to add Opportunity For a proposal.
     * @param oppInfo OpportunityInfoBean
     * @return Vector with ProcReqParameters
     * @throws java.lang.Exception
     */
    public Vector addOpportunityProcs(OpportunityInfoBean oppInfo)throws Exception{
        DBOpportunityInfoBean dbOppInfo = (DBOpportunityInfoBean)oppInfo;
        new OpportunitySchemaParser().checkFormsAvailable(dbOppInfo.getProposalNumber(),oppInfo.getSchemaUrl());

        /*
         *Get the opportunity content by using schema url
         */
        String opportunity = null;
        InputStream is  = null;
        BufferedInputStream br = null;
        try{
            URL url = new URL(dbOppInfo.getSchemaUrl());
            is = url.openConnection().getInputStream();
            br = new BufferedInputStream(is);
            byte bufContent[] = new byte[is.available()];
            br.read(bufContent);
            opportunity = new String(bufContent);
        }finally{
            if(is!=null) is.close();
            if(br!=null) br.close();
        }
        return addOpportunityProcs(dbOppInfo, opportunity);
    }

    /**
     * returns Vector with all procedures to be executed to add Opportunity For a proposal.
     * Note: this does not Check for Form Availablility of Schema URL.
     * if you need the Schem URL Check use addOpportunityProcs(OpportunityInfoBean) instead.
     * @param dbOppInfo DBOpportunityInfoBean
     * @param opportunity opportunity XML
     * @return Vector with ProcReqParameters
     * @throws java.lang.Exception
     */
    public Vector addOpportunityProcs(DBOpportunityInfoBean dbOppInfo, String opportunity) throws Exception {
        Vector procedures = new Vector(2);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();

        if (dbOppInfo.getAcType() == 'U') {
            procedures.add(deleteOppForms(dbOppInfo.getProposalNumber()));
        }
        procedures.add(updateProposalS2SDetails(dbOppInfo.getProposalNumber(),
                dbOppInfo.getCfdaNumber(),
                dbOppInfo.getOpportunityId(),
                dbOppInfo.getOpportunityTitle()));

        procedures.add(addS2SOppProc(dbOppInfo, opportunity, dbTimestamp));

        ArrayList formsList = new OpportunitySchemaParser().getFormsList(dbOppInfo.getProposalNumber(),dbOppInfo.getSchemaUrl());
        int formsSize = formsList.size();
        for (int i = 0; i < formsSize; i++) {
            FormInfoBean frmInfo = (FormInfoBean) formsList.get(i);
            frmInfo.setProposalNumber(dbOppInfo.getProposalNumber());
            frmInfo.setUpdateUser(dbOppInfo.getUpdateUser());
            frmInfo.setAcType('I');
            procedures.add(createProcOppForms(frmInfo, dbTimestamp));
        }

        return procedures;
    }

    /**
     * returns ProcReqParameter for adding a oportunity into OSP$S2S_OPPORTUNITY
     * @param dbOppInfo DBOpportunityInfoBean
     * @param opportunity opportunity XML
     * @param dbTimestamp update timestamp
     * @return ProcReqParameter
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public ProcReqParameter addS2SOppProc(DBOpportunityInfoBean dbOppInfo, String opportunity, Timestamp dbTimestamp) throws DBException {
        String compId = (dbOppInfo.getCompetitionId() == null ||
                dbOppInfo.getCompetitionId().trim().length() == 0) ? null : dbOppInfo.getCompetitionId().trim();

        Vector paramOpp = new Vector();
        coeusFunctions = new CoeusFunctions();

        boolean success = false;
        paramOpp.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, dbOppInfo.getProposalNumber()));
        paramOpp.addElement(new Parameter("OPPORTUNITY_TITLE",
                DBEngineConstants.TYPE_STRING, dbOppInfo.getOpportunityTitle()));
        paramOpp.addElement(new Parameter("COMPETETION_ID",
                DBEngineConstants.TYPE_STRING, compId));
        paramOpp.addElement(new Parameter("OPENING_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbOppInfo.getOpeningDate()));
        paramOpp.addElement(new Parameter("CLOSING_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbOppInfo.getClosingDate()));
        paramOpp.addElement(new Parameter("SCHEMA_URL",
                DBEngineConstants.TYPE_STRING,
                dbOppInfo.getSchemaUrl()));
        paramOpp.addElement(new Parameter("INSTRUCTION_URL",
                DBEngineConstants.TYPE_STRING,
                dbOppInfo.getInstructionUrl()));
        paramOpp.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramOpp.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, dbOppInfo.getUpdateUser().toUpperCase()));
        paramOpp.addElement(new Parameter("OPPORTUNITY_ID",
                DBEngineConstants.TYPE_STRING,
                dbOppInfo.getOpportunityId()));
        paramOpp.addElement(new Parameter("OPPORTUNITY",
                DBEngineConstants.TYPE_CLOB, opportunity));
        paramOpp.addElement(new Parameter("CFDA_NUMBER",
                DBEngineConstants.TYPE_STRING, dbOppInfo.getCfdaNumber()));
        paramOpp.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, dbOppInfo.getAwProposalNumber()));
        paramOpp.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbOppInfo.getAwUpdateTimestamp()));
        StringBuffer sqlOpp = new StringBuffer();
        switch (dbOppInfo.getAcType()) {
            case ('I'):
                sqlOpp.append("insert into OSP$S2S_OPPORTUNITY(");
                sqlOpp.append(" PROPOSAL_NUMBER , ");
                sqlOpp.append(" OPPORTUNITY_TITLE, ");
                sqlOpp.append(" COMPETETION_ID , ");
                sqlOpp.append(" OPENING_DATE , ");
                sqlOpp.append(" CLOSING_DATE , ");
                sqlOpp.append(" SCHEMA_URL, ");
                sqlOpp.append(" INSTRUCTION_URL , ");
                sqlOpp.append(" UPDATE_TIMESTAMP , ");
                sqlOpp.append(" UPDATE_USER , ");
                sqlOpp.append(" OPPORTUNITY_ID ,  ");
                sqlOpp.append(" CFDA_NUMBER ,  ");
                sqlOpp.append(" OPPORTUNITY ) ");
                sqlOpp.append(" values (");
                sqlOpp.append(" <<PROPOSAL_NUMBER>> , ");
                sqlOpp.append(" <<OPPORTUNITY_TITLE>> , ");
                sqlOpp.append(" <<COMPETETION_ID>> , ");
                sqlOpp.append(" <<OPENING_DATE>> , ");
                sqlOpp.append(" <<CLOSING_DATE>> , ");
                sqlOpp.append(" <<SCHEMA_URL>> , ");
                sqlOpp.append(" <<INSTRUCTION_URL>> , ");
                sqlOpp.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlOpp.append(" <<UPDATE_USER>> , ");
                sqlOpp.append(" <<OPPORTUNITY_ID>> ,  ");
                sqlOpp.append(" <<CFDA_NUMBER>> ,  ");
                sqlOpp.append(" <<OPPORTUNITY>> ) ");
                break;
            case ('U'):
                sqlOpp.append("update OSP$S2S_OPPORTUNITY set");
                sqlOpp.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> , ");
                sqlOpp.append(" OPPORTUNITY_TITLE = <<OPPORTUNITY_TITLE>> , ");
                sqlOpp.append(" COMPETETION_ID = <<COMPETETION_ID>> , ");
                sqlOpp.append(" OPENING_DATE = <<OPENING_DATE>> , ");
                sqlOpp.append(" CLOSING_DATE = <<CLOSING_DATE>> , ");
                sqlOpp.append(" SCHEMA_URL = <<SCHEMA_URL>> ,");
                sqlOpp.append(" INSTRUCTION_URL = <<INSTRUCTION_URL>> , ");
                sqlOpp.append(" UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>> , ");
                sqlOpp.append(" UPDATE_USER = <<UPDATE_USER>> , ");
                sqlOpp.append(" OPPORTUNITY_ID = <<OPPORTUNITY_ID>> , ");
                sqlOpp.append(" CFDA_NUMBER = <<CFDA_NUMBER>> , ");
                sqlOpp.append(" OPPORTUNITY = <<OPPORTUNITY>> ");
                sqlOpp.append("where ");
                sqlOpp.append(" PROPOSAL_NUMBER  = <<AW_PROPOSAL_NUMBER>> AND ");
                sqlOpp.append(" UPDATE_TIMESTAMP = <<AW_UPDATE_TIMESTAMP>>  ");
                break;
        }
        ProcReqParameter procS2SOpp = new ProcReqParameter();
        procS2SOpp.setDSN("Coeus");
        procS2SOpp.setParameterInfo(paramOpp);
        procS2SOpp.setSqlCommand(sqlOpp.toString());
        return procS2SOpp;
    }

    public boolean addUpdDelOppForms(Vector data) throws DBException{
        
        Vector formList = (Vector)data.get(0);
        int size = formList.size();
        Vector procedures = new Vector(size);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        for(int i=0;i<size;i++){
            procedures.add(createProcOppForms((FormInfoBean)formList.elementAt(i),dbTimestamp));
        }
        if(data.size() > 1 && data.get(1) != null) {
            DBOpportunityInfoBean dbOpportunityInfoBean = (DBOpportunityInfoBean)data.get(1);
            dbOpportunityInfoBean.setUpdateTimestamp(dbTimestamp);
            dbOpportunityInfoBean.setAcType('U');
            dbOpportunityInfoBean.setUpdateUser(userId);
            procedures.add(updS2SOpportunityProcs(dbOpportunityInfoBean));
        }
        dbEngine.executeStoreProcs(procedures);
        return true;
    }
    public boolean updateDelOpportunity(OpportunityInfoBean oppInfo) throws DBException{
        DBOpportunityInfoBean dbOpportunityInfoBean = (DBOpportunityInfoBean)oppInfo;
        Vector procedures = new Vector(1);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        dbOpportunityInfoBean.setUpdateTimestamp(dbTimestamp);
        //        dbOpportunityInfoBean.setUpdateUser(userId);
        procedures.add(updS2SOpportunityProcs(dbOpportunityInfoBean));
        dbEngine.executeStoreProcs(procedures);
        return true;
    }
    public ProcReqParameter createProcOppForms(FormInfoBean formInfo,Timestamp dbTimestamp)throws DBException{
        Vector paramOppForm = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        
        boolean success = false;
        paramOppForm.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,  formInfo.getProposalNumber()));
        paramOppForm.addElement(new Parameter("OPP_NAME_SPACE",
        DBEngineConstants.TYPE_STRING,  formInfo.getNs()));
        paramOppForm.addElement(new Parameter("FORM_NAME",
        DBEngineConstants.TYPE_STRING,formInfo.getFormName()));
        paramOppForm.addElement(new Parameter("MANDATORY",
        DBEngineConstants.TYPE_STRING, formInfo.isMandatory()?"Y":"N"));
        paramOppForm.addElement(new Parameter("AVAILABLE",
        DBEngineConstants.TYPE_STRING, formInfo.isAvailable()?"Y":"N"));
        paramOppForm.addElement(new Parameter("INCLUDE",
        DBEngineConstants.TYPE_STRING, formInfo.isInclude()?"Y":"N"));
        paramOppForm.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramOppForm.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, formInfo.getUpdateUser()));
        paramOppForm.addElement(new Parameter("AW_OPP_NAME_SPACE",
        DBEngineConstants.TYPE_STRING, formInfo.getNs()));
        paramOppForm.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        formInfo.getUpdateTimestamp()));
        paramOppForm.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        ""+formInfo.getAcType()));
        
        StringBuffer sqlOppForm = new StringBuffer(
        "call UPD_S2S_OPP_FORMS(");
        sqlOppForm.append(" <<PROPOSAL_NUMBER>> , ");
        sqlOppForm.append(" <<OPP_NAME_SPACE>> , ");
        sqlOppForm.append(" <<FORM_NAME>> , ");
        sqlOppForm.append(" <<MANDATORY>> , ");
        sqlOppForm.append(" <<AVAILABLE>> , ");
        sqlOppForm.append(" <<INCLUDE>> , ");
        sqlOppForm.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlOppForm.append(" <<UPDATE_USER>> , ");
        sqlOppForm.append(" <<AW_OPP_NAME_SPACE>> , ");
        sqlOppForm.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlOppForm.append(" <<AC_TYPE>> )");
        //        System.out.println("insert statement=>"+sqlOppForm.toString());
        
        ProcReqParameter procSubmn  = new ProcReqParameter();
        procSubmn.setDSN("Coeus");
        procSubmn.setParameterInfo(paramOppForm);
        procSubmn.setSqlCommand(sqlOppForm.toString());
        return procSubmn;
    }
    /**
     *  @param proposalNumber Proposal Number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    private int getMaxSubmNumber(String proposalNumber)
    throws  Exception{
        int submNumber = 0;
        Vector param = new Vector();
        Vector result = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        if(dbEngine != null){
            result = new Vector();
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER SUBM_NUMBER>> = "
            +"call FN_GET_MAX_S2S_SUB_NUM (<<PROPOSAL_NUMBER>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap maxEntryNumber = (HashMap)result.elementAt(0);
            submNumber = Integer.parseInt((String)maxEntryNumber.get("SUBM_NUMBER"));
        }
        return submNumber;
    }
    
    public ArrayList getSelectedOptionalForms(S2SHeader params) throws DBException {
        return getOppFormDetails(params);
    }
    
    
    /**This method has been written to check whether user has logged in first time.
     * @param userId is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean
     */
    public boolean firstTimeSubmitting(String proposalNumber)
    throws Exception{
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        //calling function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER S2S_SUB_CHECK>> = "
            +"call FN_IS_FIRST_S2S_SUBMISSION(  << PROPOSAL_NUMBER >> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("S2S_SUB_CHECK").toString());
        }
        return number==1;
    }
    
    public boolean isS2SCandidate(String proposalNumber)
    throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        //calling function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER S2S_CANDIDATE_CHECK>> = "
            +"call FN_CHECK_S2S_CANDIDATE(  << PROPOSAL_NUMBER >> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("S2S_CANDIDATE_CHECK").toString());
        }
        return number>=1;
    }
    
    public boolean isProposalReadyForS2S(String proposalNumber)
    throws Exception{
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        //calling function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER S2S_READY_CHECK>> = "
            +"call FN_CHECK_PROP_READY_FOR_S2S(  << PROPOSAL_NUMBER >> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("S2S_READY_CHECK").toString());
        }
        return number>=1;
    }
    public boolean isS2SAttrMatch(String proposalNumber)
    throws Exception{
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        //calling function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER S2S_ATTR_CHECK>> = "
            +"call FN_CHECK_S2S_ATTR_MATCH(  << PROPOSAL_NUMBER >> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("S2S_ATTR_CHECK").toString());
        }
        return number>=1;
    }
    
    public String performCoeusSpecificValidations(String proposalNumber)
    throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();
        String errorCodes = "-1";
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        //calling function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING ERROR_CODES>> = "
            +"call S2SPACKAGE.FN_CHECK_ERRORS_FOR_S2S(  << PROPOSAL_NUMBER >> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            errorCodes = rowParameter.get("ERROR_CODES").toString();
        }
        return errorCodes;
    }
    public OpportunityInfoBean getLocalOpportunity(S2SHeader oppParams)
    throws DBException{
        return getLocalOpportunity(oppParams.getSubmissionTitle());
    }
    public OpportunityInfoBean getLocalOpportunity(String proposalNumber)
    throws DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
        proposalNumber));
        result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_OPPORTUNITY ( <<PROPOSAL_NUMBER>>, " +
        "<<OUT RESULTSET rset>> )",
        "Coeus", param);
        DBOpportunityInfoBean dbOppBean = null;
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            dbOppBean = new DBOpportunityInfoBean();
            dbOppBean.setProposalNumber((String)rowParameter.get("PROPOSAL_NUMBER"));
            dbOppBean.setOpportunityId((String)rowParameter.get("OPPORTUNITY_ID"));
            dbOppBean.setCfdaNumber((String)rowParameter.get("CFDA_NUMBER"));
            dbOppBean.setOpportunityTitle((String)rowParameter.get("OPPORTUNITY_TITLE"));
            dbOppBean.setCompetitionId((String)rowParameter.get("COMPETETION_ID"));
            dbOppBean.setClosingDate((Timestamp)rowParameter.get("CLOSING_DATE"));
            dbOppBean.setOpeningDate((Timestamp)rowParameter.get("OPENING_DATE"));
            dbOppBean.setSchemaUrl((String)rowParameter.get("SCHEMA_URL"));
            dbOppBean.setInstructionUrl((String)rowParameter.get("INSTRUCTION_URL"));
            dbOppBean.setUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
            dbOppBean.setUpdateUser((String)rowParameter.get("UPDATE_USER"));
            dbOppBean.setOpportunity((String)rowParameter.get("OPPORTUNITY"));
            dbOppBean.setAwUpdateTimestamp((Timestamp)rowParameter.get("UPDATE_TIMESTAMP"));
            dbOppBean.setAwProposalNumber((String)rowParameter.get("PROPOSAL_NUMBER"));
            //modification for new columns in OSP$S2S_OPPORTUNITY, S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - START
            dbOppBean.setSubmissionTypeCode(((BigDecimal)rowParameter.get("S2S_SUBMISSION_TYPE_CODE")).intValue());
            dbOppBean.setRevisionCode(((String)rowParameter.get("REVISION_CODE")));
            dbOppBean.setRevisionOtherDescription(((String)rowParameter.get("REVISION_OTHER_DESCRIPTION")));
            dbOppBean.setSubmissionEndPoint(((String)rowParameter.get("END_POINT")));
            //modification for new columns in OSP$S2S_OPPORTUNITY, S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - END
        }
        return dbOppBean;
    }
    
    public ApplicationInfoBean getApplicationData(S2SHeader oppParams)
    throws DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        int number = 0;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
        oppParams.getSubmissionTitle()));
        result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_APPLICATION ( <<PROPOSAL_NUMBER>>, " +
        "<<OUT RESULTSET rset>> )",
        "Coeus", param);
        ApplicationInfoBean appInfoBean = null;
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            appInfoBean = new ApplicationInfoBean();
            appInfoBean.setSubmissionTitle((String)rowParameter.get("PROPOSAL_NUMBER"));
            appInfoBean.setApplicationData(rowParameter.get("APPLICATION").toString());
        }
        //        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
        //                        oppParams.getSubmissionTitle()));
        //        result = dbEngine.executeRequest("Coeus",
        //        "call GET_S2S_SUB_ATTACHEMNT_DATA ( <<PROPOSAL_NUMBER>>, " +
        //        "<<OUT RESULTSET rset>> )",
        //        "Coeus", param);
        //        ArrayList attachmentList = null;
        //        if(result!=null)
        //        for(int attIndex = 0;attIndex<result.size();attIndex++){
        //            if(attachmentList == null) attachmentList = new ArrayList(result.size());
        //            HashMap rowParameter = (HashMap)result.elementAt(attIndex);
        //            Attachment attachment = new Attachment();
        //            attachment.setContentId((String)rowParameter.get("CONTENT_ID"));
        //            attachment.setHashValue((String)rowParameter.get("HASH_CODE"));
        //            attachment.setContentType((String)rowParameter.get("CONTENT_TYPE"));
        //            attachment.setContent(((String)rowParameter.get("CONTENT")).getBytes());
        //            attachmentList.add(attachment);
        //        }
        ////        appInfoBean.setAttachments(getAttachments(oppParams));
        //        appInfoBean.setAttachments(attachmentList);
        return appInfoBean;
    }
    
    /**
     *  Method used to Update Proposal S2S details
     *  To update the data, it uses FN_UPD_PROPOSAL_S2S_DETAILS function.
     *  @param proposal number
     *  @param cfda number
     *  @param program number
     *  @param program title
     *
     *  @return ProcReqParameter
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter updateProposalS2SDetails(String proposalNumber,
    String cfdaNumber,
    String programNumber,
    String programTitle)
    throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        
        int index = cfdaNumber==null?-1:cfdaNumber.indexOf('.');
        if(index!=-1){
            cfdaNumber = cfdaNumber.substring(0,index)+cfdaNumber.substring(index+1,cfdaNumber.length());
        }
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("CFDA_NUMBER",
        DBEngineConstants.TYPE_STRING, cfdaNumber));
        param.add(new Parameter("PROGRAM_ANNOUNCEMENT_NUMBER",
        DBEngineConstants.TYPE_STRING, programNumber));
        param.add(new Parameter("PROGRAM_ANNOUNCEMENT_TITLE",
        DBEngineConstants.TYPE_STRING, programTitle));
        
        StringBuffer sql = new StringBuffer(
        "{ <<OUT INTEGER IS_UPDATE>> = "
        +" call FN_UPD_PROPOSAL_S2S_DETAILS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<CFDA_NUMBER>> , ");
        sql.append(" <<PROGRAM_ANNOUNCEMENT_NUMBER>> , ");
        sql.append(" <<PROGRAM_ANNOUNCEMENT_TITLE>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
    /**
     *  Method used to Update Proposal S2S details
     *  To update the data, it uses FN_UPD_SPONSOR_PROP_NUMBER function.
     *  @param proposal number
     *  @param Agency Tracking Id
     *
     *  @return ProcReqParameter
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter updateSponsorPropNumber(String proposalNumber,
    String agencyTrackingId)
    throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("SPONSOR_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, agencyTrackingId));
        
        StringBuffer sql = new StringBuffer(
        "{ <<OUT INTEGER IS_UPDATE>> = "
        +" call FN_UPD_SPONSOR_PROP_NUMBER(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SPONSOR_PROPOSAL_NUMBER>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
    
    private ProcReqParameter deleteOppForms(String proposalNumber)
    throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        
        StringBuffer sql = new StringBuffer(
        "{ <<OUT INTEGER IS_UPDATE>> = "
        +" call FN_DELETE_OPP_FORMS(");
        sql.append(" <<PROPOSAL_NUMBER>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
    
    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    public List getSubmissionTypes() throws CoeusException, DBException{
        ArrayList submissionTypeList = new ArrayList();
        
        Vector result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_SUBMISSION_TYPE_CODES ( <<OUT RESULTSET rset>> )",
        "Coeus", new Vector());
        
        String value;
        if(result!=null)
            for(int attIndex = 0;attIndex < result.size();attIndex++){
                HashMap rowParameter = (HashMap)result.elementAt(attIndex);
                ComboBoxBean comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(rowParameter.get("S2S_SUBMISSION_TYPE_CODE").toString());
                comboBoxBean.setDescription((String)rowParameter.get("DESCRIPTION"));
                submissionTypeList.add(comboBoxBean);
            }
        
        return submissionTypeList;
    }
    
    private ProcReqParameter updS2SOpportunityProcs(DBOpportunityInfoBean dbOpportunityInfoBean)
    throws DBException{
        
        Vector paramAtt = new Vector();
        boolean success = false;
        String compId = (dbOpportunityInfoBean.getCompetitionId()==null||
        dbOpportunityInfoBean.getCompetitionId().trim().length()==0)?
        null:dbOpportunityInfoBean.getCompetitionId().trim();
        
        paramAtt.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getProposalNumber()));
        paramAtt.addElement(new Parameter("OPPORTUNITY_TITLE", DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getOpportunityTitle()));
        paramAtt.addElement(new Parameter("COMPETITION_ID", DBEngineConstants.TYPE_STRING, compId));
        paramAtt.addElement(new Parameter("OPENING_DATE", DBEngineConstants.TYPE_TIMESTAMP, dbOpportunityInfoBean.getOpeningDate()));
        paramAtt.addElement(new Parameter("CLOSING_DATE", DBEngineConstants.TYPE_TIMESTAMP, dbOpportunityInfoBean.getClosingDate()));
        paramAtt.addElement(new Parameter("SCHEMA_URL", DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getSchemaUrl()));
        paramAtt.addElement(new Parameter("INSTRUCTION_URL", DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getInstructionUrl()));
        paramAtt.addElement(new Parameter("S2S_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_INT, new Integer(dbOpportunityInfoBean.getSubmissionTypeCode())));
        paramAtt.addElement(new Parameter("REVISION_CODE",DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getRevisionCode()));
        paramAtt.addElement(new Parameter("REVISION_OTHER_DESCRIPTION",DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getRevisionOtherDescription()));
        paramAtt.addElement(new Parameter("S2S_SUBMISSION_END_POINT", DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getSubmissionEndPoint()));
        paramAtt.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbOpportunityInfoBean.getUpdateTimestamp()));
        paramAtt.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getUpdateUser()));
        paramAtt.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, dbOpportunityInfoBean.getAwProposalNumber()));
        paramAtt.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbOpportunityInfoBean.getAwUpdateTimestamp()));
        paramAtt.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, ""+dbOpportunityInfoBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
        "call UPD_S2S_OPPORTUNITY(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<OPPORTUNITY_TITLE>> , ");
        sqlBudget.append(" <<COMPETITION_ID>> , ");
        sqlBudget.append(" <<OPENING_DATE>> , ");
        sqlBudget.append(" <<CLOSING_DATE>> , ");
        sqlBudget.append(" <<SCHEMA_URL>> , ");
        sqlBudget.append(" <<INSTRUCTION_URL>> , ");
        sqlBudget.append(" <<S2S_SUBMISSION_TYPE_CODE>> , ");
        sqlBudget.append(" <<REVISION_CODE>> , ");
        sqlBudget.append(" <<REVISION_OTHER_DESCRIPTION>> , ");
        sqlBudget.append(" <<S2S_SUBMISSION_END_POINT>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procAtt  = new ProcReqParameter();
        procAtt.setDSN("Coeus");
        procAtt.setParameterInfo(paramAtt);
        procAtt.setSqlCommand(sqlBudget.toString());
        
        return procAtt;
    }
    
    public String getDunsNumber(String proposalNumber)
    throws DBException,CoeusException{
        /*
         * Call function to get the dunsnumber
         */
        Vector param= new Vector();
        Vector result = new Vector();
        String dunsNumber = null;
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        //calling function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING DUNS_NUMBER>> = "
            +"call FN_GET_PROP_DUNS_NUMBER(  << PROPOSAL_NUMBER >> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            dunsNumber = (String)rowParameter.get("DUNS_NUMBER");
        }
        return dunsNumber;
        //        return "localhost";//for testing purpose
        //        return "coeus-test.mit.edu";//for testing purpose
    }
    int subAwdIndex = 0;
    public Attachment getAttachment(String proposalNumber,String contentId) throws CoeusException,DBException {
        Vector param = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
                        proposalNumber));
        param.add(new Parameter("CONTENT_ID",DBEngineConstants.TYPE_STRING,
                        contentId));
        Vector result = dbEngine.executeRequest("Coeus",
        "call GET_S2S_SUB_ATTACHMENT_DATA ( <<PROPOSAL_NUMBER>>,  <<CONTENT_ID>>, " +
        "<<OUT RESULTSET rset>> )",
        "Coeus", param);
        Attachment attachment = null;
        
        if(result!=null && !result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            attachment = new Attachment();
            String contentIdStr = (String)rowParameter.get("CONTENT_ID");
//            System.out.println("contentIdStr=>"+contentIdStr);
            if(contentIdStr.startsWith("Subaward_Budget_")){
                contentIdStr = "Subaward_Budget_"+(++subAwdIndex);
            }
            attachment.setContentId(contentIdStr);
            attachment.setHashValue((String)rowParameter.get("HASH_CODE"));
            attachment.setContentType((String)rowParameter.get("CONTENT_TYPE"));
            if(rowParameter.get("CONTENT") instanceof ByteArrayOutputStream){
                ByteArrayOutputStream cont = (ByteArrayOutputStream)rowParameter.get("CONTENT");
                attachment.setContent(cont.toByteArray());
            }else
                attachment.setContent((byte[])rowParameter.get("CONTENT"));
        }
        return attachment;
    }
    
    public Attachment getAttachmentBean(String proposalNumber,String contentId) throws CoeusException,DBException {
        Vector param = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,
                        proposalNumber));
        param.add(new Parameter("CONTENT_ID",DBEngineConstants.TYPE_STRING,
                        contentId));
        Vector result =new Vector();
        if(dbEngine!=null){
        result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING SELECT_CLAUSE>> = "
            +"call FN_GET_S2S_ATT_SELECT_CLAUSE (  <<PROPOSAL_NUMBER>>,  <<CONTENT_ID>> ) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        String selectClause = null;
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            selectClause = (String)rowParameter.get("SELECT_CLAUSE");
        }
        if(selectClause != null && !selectClause.equals("")){
            String tmpSelectClause = selectClause.replaceAll("\\t"," ");
            tmpSelectClause = tmpSelectClause.replaceAll("\\n"," ");
            Vector paramSltClause = new Vector();
            result = dbEngine.executeRequest("Coeus",tmpSelectClause,"coeus",paramSltClause);
        }else{
            return null;
        }
        Attachment attachment = null;
        if(result!=null && !result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            attachment = new Attachment();
            String contentIdStr = (String)rowParameter.get("CONTENT_ID");
            if(contentIdStr.startsWith("Subaward_Budget_")){
                contentIdStr = "Subaward_Budget_"+(++subAwdIndex);
            }
            attachment.setContentId(contentIdStr);
            attachment.setHashValue((String)rowParameter.get("HASH_CODE"));
            attachment.setContentType((String)rowParameter.get("CONTENT_TYPE"));
            if(rowParameter.get("CONTENT") instanceof ByteArrayOutputStream){
                ByteArrayOutputStream cont = (ByteArrayOutputStream)rowParameter.get("CONTENT");
                attachment.setContent(cont.toByteArray());
            }else
                attachment.setContent((byte[])rowParameter.get("CONTENT"));
        }
        return attachment;
    }

    public List getSubmissionEndPoints() throws IOException{
        ComboBoxBean serverEndPoint2 = new ComboBoxBean(S2SConstants.SOAP_HOST_2,SoapUtils.getProperty(S2SConstants.SOAP_HOST_2_DISPLAY));
        ComboBoxBean serverEndPoint = new ComboBoxBean(S2SConstants.SOAP_HOST,SoapUtils.getProperty(S2SConstants.SOAP_HOST_DISPLAY));
        List serverEndPoints = new ArrayList();
        serverEndPoints.add(serverEndPoint);
        serverEndPoints.add(serverEndPoint2);
        return serverEndPoints;
    }
    
}
