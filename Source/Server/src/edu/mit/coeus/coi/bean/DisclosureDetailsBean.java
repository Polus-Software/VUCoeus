/*
 * @(#)DisclosureDetailsBean.java 1.0 3/26/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
import java.util.Vector;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.LinkedList;
import java.sql.Timestamp;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
/**
 *
 * This class provides the methods for performing all the procedure executions for
 * a COIDisclosure. Various methods are used to fetch the COI Disclosure information
 * from the Database. All methods are used DBEngineImpl singleton instance for the database interaction.
 *
 * @version 1.0 March 26, 2002, 10:08 AM
 * @author  Geo Thomas
 */
public class DisclosureDetailsBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /*
     *  Initialize bean with a particular person id. ie, All methods
     *  in this class always belong to a person.
     */
    private String personId;
    /**
     * Creates new DisclosureDetailsBean.
     */
    public DisclosureDetailsBean(String personId){
        this.personId = personId;
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }
    /**
     *  Method used to fetch the next COIDisclosure ID for COI Disclosure.
     *  This method is used when adding a new COIDisclosure .
     *  <li>To fetch the data, it uses fn_get_next_entity_number procedure.
     *  @return String Next Sequence Number
     *  @exception DBException
     */
    public String getNextSeqNum() throws  DBException{
        String seqNum = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER SEQNUMBER>> = call fn_get_next_coi_disclosure_num() }", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(!result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable nextNumRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap nextNumRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            seqNum = nextNumRow.get("SEQNUMBER").toString();
        }
        return seqNum;
    }
    /**
     *  Method used to check whether the person has already a disclosure
     *  for the selected award or proposal.
     *  <li>To check this, it uses fn_check_person_has_disclosure function.
     *  <li>Returns true if the person has disclosure or else, returns false.
     *  @param String ModuleCode
     *  @param String ModuleItemKey
     *  @return boolean Disclosure Status
     *  @exception DBException
     */
    public boolean isPersonHasDisclosure(String strModuleCode,String strModuleItemKey)
            throws  DBException{
        String disclNum = "";
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            param.addElement(new Parameter("PERSON_ID","String",personId));
            param.addElement(new Parameter("MODULE_CODE","String",strModuleCode));
            param.addElement(new Parameter("MODULE_ITEM_KEY","String",strModuleItemKey));
            StringBuffer strBuffSql = new StringBuffer("{ << OUT STRING DISCLOSURE_NUMBER >> = ");
            /* CASE #570 Comment Begin */
            //strBuffSql.append("call fn_check_person_has_disclosure ( ");
            /* CASE #570 Comment End */
            /* CASE #570 Begin */
            strBuffSql.append("call fn_check_person_has_disc_java ( ");
            /* CASE #570 End */
            strBuffSql.append("<< PERSON_ID >> , ");
            strBuffSql.append(" << MODULE_CODE >> , ");
            strBuffSql.append(" << MODULE_ITEM_KEY >> ) }");
            result = dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable disclNumRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclNumRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            disclNum = (String)disclNumRow.get("DISCLOSURE_NUMBER");
            System.out.println("disclNum: "+disclNum);
        }
        if(disclNum == null){
            disclNum = " ";
        }        
        return !disclNum.equals(" ");
    }

    /* CASE # 250 Begin. */
    /**
     *  Overloaded method used to return the disclosure number, if it exists,
     *  for any initial disclosure for a given award or proposal for this
     *  person.  Else, returns a String with one blank space.
     *  <li>To check this, it uses fn_check_person_has_disclosure function.
     *  @param String ModuleCode
     *  @param String ModuleItemKey
     *  @return String Disclosure Number
     *  @exception DBException
     */
    public String isPersonHasDisclosure(String strModuleCode,
        String strModuleItemKey, boolean returnDisclNum)
            throws  DBException{
        String disclNum = "";
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            param.addElement(new Parameter("PERSON_ID","String",personId));
            param.addElement(new Parameter("MODULE_CODE","String",strModuleCode));
            param.addElement(new Parameter("MODULE_ITEM_KEY","String",strModuleItemKey));
            StringBuffer strBuffSql = new StringBuffer("{ << OUT STRING DISCLOSURE_NUMBER >> = ");
            /* CASE #570 Comment Begin */
            //strBuffSql.append("call fn_check_person_has_disclosure ( ");
            /* CASE #570 Comment End */
            /* CASE #570 Begin */
            strBuffSql.append("call fn_check_person_has_disc_java ( ");
            /* CASE #570 End */
            strBuffSql.append("<< PERSON_ID >> , ");
            strBuffSql.append(" << MODULE_CODE >> , ");
            strBuffSql.append(" << MODULE_ITEM_KEY >> ) }");
            result = dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable disclNumRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclNumRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            disclNum = (String)disclNumRow.get("DISCLOSURE_NUMBER");
        }

        /* This stored procedure returns a single space when the user has no disclousures
        for the given proposal or award.  */
        return disclNum;
    }
    /* CASE # 250 End. */

    /**
     *  Method used to get all status.
     *  This returns a LinkedList which holds instances of ComboBox bean.
     *  <li>To fetch the data, it uses dw_get_fin_int_entity_rel_type procedure.
     *  @return HashMap
     *  @exception DBException
     */
    public LinkedList getAllDiscStatus() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        LinkedList disclStatus = new LinkedList();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call dw_get_coi_disclosure_status( <<OUT RESULTSET rset>> )   ",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int k=0;k<result.size();k++){
            /* case #748 comment begin */
            //Hashtable disclStatusRow = (Hashtable)result.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclStatusRow = (HashMap)result.elementAt(k);
            /* case #748 end */
            ComboBoxBean objStatus = new ComboBoxBean();
            objStatus.setCode(disclStatusRow.get("COI_DISCLOSURE_STATUS_CODE").toString());
            objStatus.setDescription((String)disclStatusRow.get("DESCRIPTION"));
            disclStatus.add(objStatus);
        }
        return disclStatus;
    }
    /**
     *  Method used to get all reviewers. This method can be used, whenever application needs
     *  all reviewers as code and it's description. This method returns a LinkedList which holds instances
     *  of ComboBoxBean. The ComboBoxBean has corresponding methods to get the code and the description.
     *  <li>To fetch the data, it uses dw_get_fin_int_entity_rel_type procedure.
     *  @return LinkedList List of ComboBox Instances
     *  @exception DBException
     */
    public LinkedList getAllReviewers() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        LinkedList reviewers = new LinkedList();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call dw_get_coi_reviewer( <<OUT RESULTSET rset>> )   ", "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int k=0;k<result.size();k++){
            /* case #748 comment begin */
            //Hashtable disclReviewerRow = (Hashtable)result.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclReviewerRow = (HashMap)result.elementAt(k);
            /* case #748 end */
            ComboBoxBean reviewer = new ComboBoxBean();
            reviewer.setCode(disclReviewerRow.get("COI_REVIEWER_CODE").toString());
            reviewer.setDescription((String)disclReviewerRow.get("DESCRIPTION"));
            reviewers.add(reviewer);
        }
        return reviewers;
    }

    /**
     *  This method is used to get disclosures details for a particular person.
     *  It will throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_coi_disclheader procedure.
     *  @param String COI DisclosureNumber
     *  @return DisclosureHeaderBean COI Disclosure Header details
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public DisclosureHeaderBean  getCOIDisclosureHeader(String disclosureNumber)
            throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector param= new java.util.Vector();
        DisclosureHeaderBean disclHeader = new DisclosureHeaderBean();
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER","String",
                                                          disclosureNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call dw_get_coi_disclheader ( <<COI_DISCLOSURE_NUMBER>> , <<OUT RESULTSET rset>> ) ",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        /* case #748 comment begin */
        //Hashtable disclRow = (Hashtable)result.elementAt(0);
        /* case #748 comment end */
        /* case #748 begin */
        HashMap disclRow = (HashMap)result.elementAt(0);
        /* case #748 end */
        /* CASE #352 Begin */
        disclHeader.setPersonId((String)disclRow.get("PERSON_ID"));
        /* CASE #352 End */
        disclHeader.setName((String)disclRow.get("FULL_NAME"));
        disclHeader.setDisclosureNo((String)disclRow.get("COI_DISCLOSURE_NUMBER"));
        disclHeader.setDisclStatus((String)disclRow.get("DISCSTAT"));
        disclHeader.setAppliesTo((String)disclRow.get("MODULE"));
        disclHeader.setKeyNumber(disclRow.get("MODULE_ITEM_KEY").toString());
        disclHeader.setDisclType(disclRow.get("DISCLOSURE_TYPE").toString());
        disclHeader.setReviewer((String)disclRow.get("REVIEWER"));
        disclHeader.setStatSeqNumber(disclRow.get("SEQUENCE_NUMBER").toString());
        disclHeader.setUpdatedDate((Timestamp)disclRow.get("UPDATE_TIMESTAMP"));
        disclHeader.setModuleCode(disclRow.get("MODULE_CODE").toString());
        disclHeader.setDisclStatCode(disclRow.get("COI_DISCLOSURE_STATUS_CODE").toString());
        disclHeader.setReviewerCode(disclRow.get("COI_REVIEWER_CODE").toString());
        disclHeader.setUpdateUser((String)disclRow.get("UPDATE_USER"));
        return disclHeader;
    }
    /**
     *  This method is used to get all financial entity details for a particular COI Disclosure.
     *  It will throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_inv_coi_disclosures procedure.
     *  @return DisclosureInfoBean FinancialEntity details for a COI Disclosure
     *  @param String COIDisclosure number
     *  @exception DBException
     *  @exception CoeusException
     */
    public Vector getCOIDisclosureInfo(String disclosureNumber)
            throws DBException,CoeusException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector coiDisclInfo = new Vector(3,2);
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER","String",
                                                        disclosureNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_inv_coi_disclosures ( <<COI_DISCLOSURE_NUMBER>> , <<OUT RESULTSET rset>> ) ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int j=0;j<result.size();j++){
            /* case #748 comment begin */
            //Hashtable disclRow = (Hashtable)result.elementAt(j);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclRow = (HashMap)result.elementAt(j);
            /* case #748 end */
            DisclosureInfoBean disclInfo = new DisclosureInfoBean();
            disclInfo.setEntityName((String)disclRow.get("ENTITY_NAME"));
            disclInfo.setEntityNumber((String)disclRow.get("ENTITY_NUMBER"));
            disclInfo.setEntSeqNumber
                  (disclRow.get("ENTITY_SEQUENCE_NUMBER").toString());
            disclInfo.setSeqNumber(disclRow.get("SEQUENCE_NUMBER").toString());
            disclInfo.setConflictStatus((String)disclRow.get("COI_STATUS"));
            disclInfo.setConflictStatusCode(disclRow.get("COI_STATUS_CODE").toString());
            disclInfo.setReviewer((String)disclRow.get("COI_REVIEWER"));
            disclInfo.setReviewerCode(disclRow.get("COI_REVIEWER_CODE").toString());
            disclInfo.setDesc((String)disclRow.get("DESCRIPTION"));
            disclInfo.setUpdatedBy((String)disclRow.get("UPDATE_USER"));
            disclInfo.setLastUpdated((Timestamp)disclRow.get("UPDATE_TIMESTAMP"));
            coiDisclInfo.add(disclInfo);
        }
        return coiDisclInfo;
    }
    /**
     *  This method is used to get all financial entity details for a particular person.
     *  This method will throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_person_disc_det procedure.
     *  @return Vector vector of COIDisclosureInfo instance
     *
     *  @exception CoeusException
     *  @exception DBException
     */
    public Vector getCOIDisclInfoForPerson() throws CoeusException,DBException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector coiDisclInfo = new Vector(3,2);
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("AW_PERSON_ID","String",personId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_person_disc_det ( <<AW_PERSON_ID>> , <<OUT RESULTSET rset>> ) ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int j=0;j<result.size();j++){
            /* case #748 comment begin */
            //Hashtable disclRow = (Hashtable)result.elementAt(j);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclRow = (HashMap)result.elementAt(j);
            /* case #748 end */
            DisclosureInfoBean disclInfo = new DisclosureInfoBean();
            disclInfo.setEntityName((String)disclRow.get("ENTITY_NAME"));
            disclInfo.setEntityNumber(disclRow.get("ENTITY_NUMBER").toString());
            disclInfo.setEntSeqNumber(disclRow.get("SEQUENCE_NUMBER").toString());
            disclInfo.setConflictStatus((String)disclRow.get("STATUS_DESCRIPTION"));
            disclInfo.setUpdatedBy((String)disclRow.get("UPDATE_USER"));
            disclInfo.setLastUpdated((Timestamp)disclRow.get("UPDATE_TIMESTAMP"));
            coiDisclInfo.add(disclInfo);
        }
        return coiDisclInfo;
    }
    /**
     *  This method is used to get all certificate details for a particular disclosure.
     *  It will return a vector, which contains instances of CertificateDetailsBean.
     *  This method will throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_inv_coi_certdetails_new procedure.
     *  @return Vector Certificate Details
     *  @param String COIDisclosure number
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public Vector getCOICertificateDetails(String disclosureNumber)
            throws DBException,CoeusException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector coiDisclCertDetails = new Vector(3,2);
        /* CASE #1393 Begin*/
        CertQuestionDetailsBean certQuestionDetails = new CertQuestionDetailsBean(personId);
        /* CASE #1393 End */
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER","String",disclosureNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_inv_coi_certdetails_new ( <<COI_DISCLOSURE_NUMBER>> , <<OUT RESULTSET rset>> ) ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int k=0;k<result.size();k++){
            /* case #748 comment begin */
            //Hashtable certDetRow = (Hashtable)result.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap certDetRow = (HashMap)result.elementAt(k);
            /* case #748 end */
            CertificateDetailsBean objCertDet = new CertificateDetailsBean();
            objCertDet.setCode(certDetRow.get("QUESTION_ID").toString());
            objCertDet.setQuestion((String)certDetRow.get("DESCRIPTION"));
            objCertDet.setAnswer(certDetRow.get("ANSWER") == null ? null :
                                        certDetRow.get("ANSWER").toString());
            objCertDet.setExplanation((String)certDetRow.get("EXPLANATION"));
            objCertDet.setNumber((String)certDetRow.get("COI_DISCLOSURE_NUMBER"));
            /* CASE #748 Comment Begin */
            /*Object reviewDate = certDetRow.get("REVIEW_DATE");
            objCertDet.setReviewDate((reviewDate.toString().equals("null"))?null:((Timestamp)reviewDate));
            Object effectiveDate = certDetRow.get("EFFECTIVE_DATE");
            objCertDet.setEffDate((effectiveDate.toString().equals("null"))?null:((Timestamp)effectiveDate));
            Object lastUpdate = certDetRow.get("UPDATE_TIMESTAMP");
            objCertDet.setLastUpdate((lastUpdate.toString().equals("null"))?null:((Timestamp)lastUpdate));*/
            /* CASE #748 Comment End */
            /* CASE #748 Begin */
            objCertDet.setReviewDate((Timestamp)certDetRow.get("REVIEW_DATE"));
            objCertDet.setEffDate((Timestamp)certDetRow.get("EFFECTIVE_DATE"));
            objCertDet.setLastUpdate((Timestamp)certDetRow.get("UPDATE_TIMESTAMP"));
            /* CASE #748 End */
            objCertDet.setNumOfAns(certDetRow.get("NO_OF_ANSWERS").toString());
            objCertDet.setExplReqFor((String)certDetRow.get("EXPLANATION_REQUIRED_FOR"));
            objCertDet.setDateReqFor((String)certDetRow.get("DATE_REQUIRED_FOR"));
            objCertDet.setUpdateUser((String)certDetRow.get("UPDATE_USER"));
            objCertDet.setStatus(certDetRow.get("STATUS").toString());
            objCertDet.setStatus(certDetRow.get("QUESTION_TYPE").toString());
            /* CASE #1393 Begin */
            //Get corresponding entity cert question id, if any, 
            //for this disclosure certification question
            String entQuestId = 
                certQuestionDetails.getCorrespEntQuestId(objCertDet.getCode());
            String entQuestLabel = certQuestionDetails.getCertQuestLabel(entQuestId);
            objCertDet.setCorrespEntQuestId(entQuestId);
            objCertDet.setCorrespEntQuestLabel(entQuestLabel);
            //Get label for this disclosure cert question
            String label = certQuestionDetails.getCertQuestLabel(objCertDet.getCode());
            objCertDet.setLabel(label);
            /* CASE #1393 End */            

            coiDisclCertDetails.add(objCertDet);
        }

        return coiDisclCertDetails;
    }

    /**
     *  This method is used to get award information for a particular award number.
     *  It will return a DisclosureHeaderBean class instance, which has all the details about an award.
     *  This method will throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses get_awardinfo procedure.
     *  @return DisclosureHeaderBean Award Details
     *  @param String Award number
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public DisclosureHeaderBean getCOIAwardInfo(String awardNumber)
            throws DBException,CoeusException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        DisclosureHeaderBean disclHeader = new DisclosureHeaderBean();
        param.addElement(new Parameter("MIT_AWARD_NUMBER","String",awardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call get_awardinfo( <<MIT_AWARD_NUMBER>> , <<OUT RESULTSET rset>> ) ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        /* case #748 comment begin */
        //Hashtable disclRow = new Hashtable();
        /* case #748 comment end */
        /* case #748 begin */
        HashMap disclRow = new HashMap();
        /* case #748 end */
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //disclRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            disclRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            disclHeader.setTitle((String)disclRow.get("AS_TITLE"));
            disclHeader.setAccount(disclRow.get("AS_ACCOUNT") == null ? null :
                                      disclRow.get("AS_ACCOUNT").toString());
            disclHeader.setStatus((String)disclRow.get("AS_NAME"));
            /* CASE #665 Begin */
            disclHeader.setSponsor((String)disclRow.get("SPONSOR_NAME"));
            /* CASE #665 End */
        }
        return disclHeader;
    }

    /**
     *  This method is used to get proposal information for a particular proposal number.
     *  It will return a DisclosureHeaderBean class instance, which has all the details about the proposal.
     *  This method will throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses get_inst_propinfo procedure.
     *  @param String Proposal number
     *  @return DisclosureHeaderBean Proposal Details
     *  @exception DBException
     *  @exception CoeusException
     */
    public DisclosureHeaderBean getCOIProposalInfo(String proposalNumber)
            throws DBException,CoeusException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        DisclosureHeaderBean disclHeader = new DisclosureHeaderBean();
        param.addElement(new Parameter("PROPOSAL_NUMBER","String",proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call get_inst_propinfo ( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>>) ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        /* case #748 comment begin */
        //Hashtable disclHeaderRow = new Hashtable();
        /* case #748 comment end */
        /* case #748 begin */
        HashMap disclHeaderRow = new HashMap();
        /* case #748 end */
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //disclHeaderRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            disclHeaderRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            disclHeader.setTitle((String)disclHeaderRow.get("AS_TITLE"));
            disclHeader.setAccount(
                          disclHeaderRow.get("AS_ACCOUNT") == null ? null :
                          disclHeaderRow.get("AS_ACCOUNT").toString());
            disclHeader.setStatus((String)disclHeaderRow.get("AS_NAME"));
            /* CASE #665 Begin */
            disclHeader.setSponsor((String)disclHeaderRow.get("SPONSOR_NAME"));
            /* CASE #665 End */
        }
        return disclHeader;
    }
    /**
     *  Method used to get all COI status.
     *  This returns a LinkedList which holds instances of ComboBoxBean.
     *  <li>To fetch the data, it uses dw_get_coi_status procedure.
     *  @return LinkedList list of ComboBoxBean Instances
     *  @exception DBException
     */
    public LinkedList getAllCOIStatus() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        LinkedList coiStatus = new LinkedList();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_coi_status( <<OUT RESULTSET rset>> )   ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int k=0;k<result.size();k++){
            /* case #748 comment begin */
            //Hashtable coiStatRow = (Hashtable)result.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap coiStatRow = (HashMap)result.elementAt(k);
            /* case #748 end */
            ComboBoxBean coiStat = new ComboBoxBean();
            coiStat.setCode(coiStatRow.get("COI_STATUS_CODE").toString());
            coiStat.setDescription((String)coiStatRow.get("DESCRIPTION"));
            coiStatus.add(coiStat);
        }
        return coiStatus;
    }

    /**
     *  Method used to fetch the Timestamp from the database.
     *  Returns the current timestamp from the database.
     *  This method will be used to get the current timestamp while adding/updating a COIDisclosure.
     *  To fetch the data, it uses dw_get_cur_sysdate procedure.
     *  @return Timestamp current database timestamp
     *  @exception DBException
     */
    public Timestamp getTimestamp() throws DBException{
        Timestamp currDBdateTime = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                " call dw_get_cur_sysdate( <<OUT RESULTSET rset>> )", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(!result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable sysDateRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap sysDateRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            currDBdateTime = (Timestamp)sysDateRow.get("SYSDATE");
        }
        return currDBdateTime;
    }

    /* CASE #1374 Modify to take additional parameter, boolean disclStatusChanged */
    /**
     *  Method used to add or update COI disclosure details into the database.
     *  For adding/updating a COIDisclosure, it needs to execute four stored procedures.
     *  <li>The procedures are as follows<br>
     *  1) <code>dw_upd_inv_coi_disclosure</code> for adding a new COIDisclosure in the master table.<br>
     *  2) <code>dw_upd_invcoidisc_statuscahnge</code> for updating the status change.<br>
     *  3) <code>dw_upd_inv_coi_disc_details</code> for updating financial entity details.<br>
     *  4) <code>dw_upd_inv_coi_certification</code> for updating certificate details.<br>
     *  <li>Returns true if all procedures successfully executed.
     *  If it fails to execute any one of the procedures in the transaction,
     *  it rollsback entire transaction and returns false.
     *  @param DisclosureHeaderBean DisclosureHeaderBean is to add a new COI disclosure
     *  @param boolean disclStatusChanged Should row be inserted into 
     *          OSP$INV_COI_DISC_STATUS_CHANGE?
     *  @param Vector vector of COIDisclosureInfo beans to add Financial Entity details
                                            for newly added COI Disclosure
     *  @param Vector vector of CertificateDetailsBean to add questions and answers
                                            for the newly added COIDisclosure
     *  @return boolean Add/Update status
     *  @exception DBException
     *  @exception CoeusException
     */
    public boolean addUpdateCOIDisc(DisclosureHeaderBean disclHeader, boolean disclStatusChanged,
        Vector disclInfo, Vector certDetails,String acType) 
                                            throws CoeusException,DBException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        String dsn = "Coeus";
        //Holds all the procedures in an array to send to dbengine as one transaction.
        Vector procedures = new Vector(5,3);
        //Current timestamp from Database
        Timestamp currDate = getTimestamp();
        Vector paramCOIDiscHead= new java.util.Vector();
        paramCOIDiscHead.addElement(new Parameter("AV_COI_DISCLOSURE_NUMBER","String",
          disclHeader.getDisclosureNo()));
        paramCOIDiscHead.addElement(new Parameter("AV_MODULE_ITEM_KEY","String",
          disclHeader.getKeyNumber()));
        paramCOIDiscHead.addElement(new Parameter("AV_MODULE_CODE","String",
          disclHeader.getModuleCode()));
        paramCOIDiscHead.addElement(new Parameter("AV_PERSON_ID","String",
          personId));
        paramCOIDiscHead.addElement(new Parameter("AV_DISCLOSURE_TYPE","String",
          disclHeader.getDisclType()));
        paramCOIDiscHead.addElement(new Parameter("AV_UPDATE_TIMESTAMP","Date",
          currDate));
        paramCOIDiscHead.addElement(new Parameter("AV_UPDATE_USER","String",
          disclHeader.getUpdateUser()));
        paramCOIDiscHead.addElement(new Parameter("AW_COI_DISCLOSURE_NUMBER","String",
          disclHeader.getDisclosureNo()));
        paramCOIDiscHead.addElement(new Parameter("AW_UPDATE_TIMESTAMP","Date",
          disclHeader.getUpdatedDate()));
        paramCOIDiscHead.addElement(new Parameter("AC_TYPE","String",
          acType));
        StringBuffer sqlHeadQry =  new StringBuffer("call dw_upd_inv_coi_disclosure ( ");
        sqlHeadQry.append(" << AV_COI_DISCLOSURE_NUMBER >> ,");
        sqlHeadQry.append(" << AV_MODULE_ITEM_KEY >> ,");
        sqlHeadQry.append(" << AV_MODULE_CODE >> ,");
        sqlHeadQry.append(" << AV_PERSON_ID >> ,");
        sqlHeadQry.append(" << AV_DISCLOSURE_TYPE >> ,");
        sqlHeadQry.append(" << AV_UPDATE_TIMESTAMP >> ,");
        sqlHeadQry.append(" << AV_UPDATE_USER >> ,");
        sqlHeadQry.append(" << AW_COI_DISCLOSURE_NUMBER >> ,");
        sqlHeadQry.append(" << AW_UPDATE_TIMESTAMP >> ,");
        sqlHeadQry.append(" << AC_TYPE >> )");
        //Use ProcReqParameter class instance to hold stored procedure information
        ProcReqParameter procReqParamHeader = new ProcReqParameter();
        //Set attributes to ProcReqParameter class instance
        procReqParamHeader.setDSN(dsn);
        procReqParamHeader.setParameterInfo(paramCOIDiscHead);
        procReqParamHeader.setSqlCommand(sqlHeadQry.toString());
        /*Add ProcReqParameter instance to a vector which holds all the procedures for
         * a transaction, ie, For Adding a new COI disclosure
         */
        procedures.addElement(procReqParamHeader);
        
        if((acType != null && acType.equals("I")) || disclStatusChanged){
            //Adding parameters for the procedure to add/update disclosure status change
            Vector paramCOIDiscStat= new java.util.Vector();
            String tmpStatSeqNum = disclHeader.getStatSeqNumber();
            int statSeqNum = acType.equals("I")?1:(Integer.parseInt(tmpStatSeqNum)+1);
            paramCOIDiscStat.addElement(new Parameter("AV_COI_DISCLOSURE_NUMBER","String",
              disclHeader.getDisclosureNo()));
            paramCOIDiscStat.addElement(new Parameter("AV_SEQUENCE_NUMBER","String",
              ""+statSeqNum));
            paramCOIDiscStat.addElement(new Parameter("AV_COI_DISCLOSURE_STATUS_CODE","String",
              disclHeader.getDisclStatCode()));
            paramCOIDiscStat.addElement(new Parameter("AV_COI_REVIEWER_CODE","String",
              disclHeader.getReviewerCode()));
            paramCOIDiscStat.addElement(new Parameter("AV_UPDATE_TIMESTAMP","Date",
              currDate));
            paramCOIDiscStat.addElement(new Parameter("AV_UPDATE_USER","String",
              disclHeader.getUpdateUser()));
            paramCOIDiscStat.addElement(new Parameter("AW_COI_DISCLOSURE_NUMBER","String",
              disclHeader.getDisclosureNo()));
            paramCOIDiscStat.addElement(new Parameter("AW_SEQUENCE_NUMBER","String",
              disclHeader.getStatSeqNumber()));
            paramCOIDiscStat.addElement(new Parameter("AW_UPDATE_TIMESTAMP","Date",
              disclHeader.getUpdatedDate()));
            paramCOIDiscStat.addElement(new Parameter("AC_TYPE","String","I"));
            StringBuffer SqlStatusQry =  new StringBuffer("call dw_upd_invcoidisc_statuscahnge ( ");
            SqlStatusQry.append(" << AV_COI_DISCLOSURE_NUMBER >> ,");
            SqlStatusQry.append(" << AV_SEQUENCE_NUMBER >> ,");
            SqlStatusQry.append(" << AV_COI_DISCLOSURE_STATUS_CODE >> ,");
            SqlStatusQry.append(" << AV_COI_REVIEWER_CODE >> ,");
            SqlStatusQry.append(" << AV_UPDATE_TIMESTAMP >> ,");
            SqlStatusQry.append(" << AV_UPDATE_USER >> ,");
            SqlStatusQry.append(" << AW_COI_DISCLOSURE_NUMBER >> ,");
            SqlStatusQry.append(" << AW_SEQUENCE_NUMBER >> ,");
            SqlStatusQry.append(" << AW_UPDATE_TIMESTAMP >> ,");
            SqlStatusQry.append(" << AC_TYPE >> )");
            ProcReqParameter procReqParamStatus = new ProcReqParameter();
            procReqParamStatus.setDSN(dsn);
            procReqParamStatus.setParameterInfo(paramCOIDiscStat);
            procReqParamStatus.setSqlCommand(SqlStatusQry.toString());
            procedures.addElement(procReqParamStatus);
        }
        /*
         *  Adding parameters for the procedure to add/update certificate details
         */
        for(int i=0;i<disclInfo.size();i++){
            DisclosureInfoBean objDiscInfo = (DisclosureInfoBean)disclInfo.elementAt(i);
            Vector paramCOIDiscInfo= new java.util.Vector();
            String tmpSeqNum = objDiscInfo.getSeqNumber();
            int seqNum = acType.equals("I")?1:(Integer.parseInt(tmpSeqNum)+1);
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_COI_DISCLOSURE_NUMBER","String",disclHeader.getDisclosureNo()));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_ENTITY_NUMBER","String",objDiscInfo.getEntityNumber()));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_ENTITY_SEQUENCE_NUMBER","String",objDiscInfo.getEntSeqNumber()));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_SEQUENCE_NUMBER","String",""+seqNum));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_COI_STATUS_CODE","String",objDiscInfo.getConflictStatusCode()));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_COI_REVIEWER_CODE","String",objDiscInfo.getReviewerCode()));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_DESCRIPTION","String",objDiscInfo.getDesc()));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_UPDATE_TIMESTAMP","Date",currDate));
            paramCOIDiscInfo.addElement(new Parameter
                ("AV_UPDATE_USER","String",disclHeader.getUpdateUser()));
            paramCOIDiscInfo.addElement(new Parameter
                ("AC_TYPE","String","I"));
            StringBuffer sqlInfoQry =  new StringBuffer("call dw_upd_inv_coi_disc_details ( ");
            sqlInfoQry.append(" << AV_COI_DISCLOSURE_NUMBER >> ,");
            sqlInfoQry.append(" << AV_ENTITY_NUMBER >> ,");
            sqlInfoQry.append(" << AV_ENTITY_SEQUENCE_NUMBER >> ,");
            sqlInfoQry.append(" << AV_SEQUENCE_NUMBER >> ,");
            sqlInfoQry.append(" << AV_COI_STATUS_CODE >> ,");
            sqlInfoQry.append(" << AV_COI_REVIEWER_CODE >> ,");
            sqlInfoQry.append(" << AV_DESCRIPTION >> ,");
            sqlInfoQry.append(" << AV_UPDATE_TIMESTAMP >> ,");
            sqlInfoQry.append(" << AV_UPDATE_USER >> ,");
            sqlInfoQry.append(" << AC_TYPE >> )");
            ProcReqParameter procReqParamInfo = new ProcReqParameter();
            procReqParamInfo.setDSN(dsn);
            procReqParamInfo.setParameterInfo(paramCOIDiscInfo);
            procReqParamInfo.setSqlCommand(sqlInfoQry.toString());
            procedures.addElement(procReqParamInfo);
        }
        /*
         *  Extracting certDetailVector and forming procedure parameters
         *  and add these procedures in the vector for add/update Question details
         */
        if(certDetails!=null){ //checking whether any certificate details are there for add/update
            int certDetailsSize = certDetails.size();
            Vector paramCertInfo = null;
            for(int i=0;i<certDetailsSize;i++){
                CertificateDetailsBean certificateDetails = (CertificateDetailsBean)certDetails.elementAt(i);
                paramCertInfo= new java.util.Vector();
                paramCertInfo.addElement(new Parameter("AV_COI_DISCLOSURE_NUMBER",
                  "String",certificateDetails.getNumber()));
                paramCertInfo.addElement(new Parameter("AV_QUESTION_ID",
                  "String",certificateDetails.getCode()));
                paramCertInfo.addElement(new Parameter("AV_ANSWER",
                  "String",certificateDetails.getAnswer()));
                paramCertInfo.addElement(new Parameter("AV_EXPLANATION",
                  "String",certificateDetails.getExplanation()));
                paramCertInfo.addElement(new Parameter("AV_REVIEW_DATE",
                  "Date",certificateDetails.getReviewDate()));
                paramCertInfo.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
                  "Date",currDate));
                paramCertInfo.addElement(new Parameter("AV_UPDATE_USER",
                  "String",certificateDetails.getUpdateUser()));
                paramCertInfo.addElement(new Parameter("AW_COI_DISCLOSURE_NUMBER",
                  "String",certificateDetails.getNumber()));
                paramCertInfo.addElement(new Parameter("AW_QUESTION_ID",
                  "String",certificateDetails.getCode()));
                paramCertInfo.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                  "Date",certificateDetails.getLastUpdate()));
                String accountType = certificateDetails.getAccountType();
                if(accountType == null) {
                  accountType = acType;
                }

                paramCertInfo.addElement(new Parameter("AC_TYPE","String",accountType));
                //paramCertInfo.addElement(new Parameter("AC_TYPE","String",acType));
                StringBuffer sqlCertQry =  new StringBuffer("call dw_upd_inv_coi_certification ( ");
                sqlCertQry.append(" << AV_COI_DISCLOSURE_NUMBER >> ,");
                sqlCertQry.append(" << AV_QUESTION_ID >> ,");
                sqlCertQry.append(" << AV_ANSWER >> ,");
                sqlCertQry.append(" << AV_EXPLANATION >> ,");
                sqlCertQry.append(" << AV_REVIEW_DATE >> ,");
                sqlCertQry.append(" << AV_UPDATE_TIMESTAMP >> ,");
                sqlCertQry.append(" << AV_UPDATE_USER >> ,");
                sqlCertQry.append(" << AW_COI_DISCLOSURE_NUMBER >> ,");
                sqlCertQry.append(" << AW_QUESTION_ID >> ,");
                sqlCertQry.append(" << AW_UPDATE_TIMESTAMP >> ,");
                sqlCertQry.append(" << AC_TYPE >> )");
                ProcReqParameter procReqParamCert = new ProcReqParameter();
                procReqParamCert.setDSN(dsn);
                procReqParamCert.setParameterInfo(paramCertInfo);
                procReqParamCert.setSqlCommand(sqlCertQry.toString());
                procedures.addElement(procReqParamCert);
            }
        }
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        return true;
    }

    /* CASE #864 Comment Begin */
    /**
     *  Method used to check disclosure question/answer against
     *  entity question/answer
     *  @return boolean - "true" - correct answer, "false" - incorrect answer
     *  @exception DBException
     */
    /*public boolean isDisclAnswerOk(String disclQuestionId, String disclAnswer)
            throws  DBException{
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        String isAnswerOk = null;
        //calling stored function
        if(dbEngine!=null){
            param.addElement(new Parameter("AW_PERSON_ID","String",personId));
            param.addElement(new Parameter("AS_QUESTION_ID","String",disclQuestionId));
            param.addElement(new Parameter("AS_ANSWER","String",disclAnswer));
            StringBuffer strBuffSql = new StringBuffer("{ << OUT INTEGER ANSWER_OK >> = ");
            strBuffSql.append("call fn_check_discl_answer ( ");
            strBuffSql.append("<< AW_PERSON_ID >> , ");
            strBuffSql.append(" << AS_QUESTION_ID >> , ");
            strBuffSql.append(" << AS_ANSWER >> ) }");
            result = dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            Hashtable disclCheck = (Hashtable)result.elementAt(0);
            isAnswerOk = disclCheck.get("ANSWER_OK").toString();
        }
        if(Integer.parseInt(isAnswerOk) == 1) {
          return true;
        }else {
          return false;
        }
    }*/
    /* CASE #864 Comment End */

    /* CASE #864 Begin */
    /**
     *  Method used to check disclosure question/answer against
     *  entity question/answer
     *  @return String "1" for success. entityNumber that failed for failure
     *  "noFinEnt" for failure due to yes answer with no active fin entities disclosed.
     *  @exception DBException
     */
    public String isDisclAnswerOk(String disclQuestionId, String disclAnswer)
            throws  DBException{
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        String isAnswerOk = null;
        //calling stored function
        if(dbEngine!=null){
            param.addElement(new Parameter("AW_PERSON_ID","String",personId));
            param.addElement(new Parameter("AS_QUESTION_ID","String",disclQuestionId));
            param.addElement(new Parameter("AS_ANSWER","String",disclAnswer));
            StringBuffer strBuffSql = new StringBuffer("{ << OUT STRING ENTITY_NUMBER >> = ");
            strBuffSql.append("call fn_check_discl_answer ( ");
            strBuffSql.append("<< AW_PERSON_ID >> , ");
            strBuffSql.append(" << AS_QUESTION_ID >> , ");
            strBuffSql.append(" << AS_ANSWER >> ) }");
            result = dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            HashMap disclCheck = (HashMap)result.elementAt(0);
            isAnswerOk = disclCheck.get("ENTITY_NUMBER").toString();
        }
        return isAnswerOk;
    }
    /* CASE #864 End */
    
    /* CASE #357 Begin */
    public boolean checkDisclosureRequiresSync(String disclosureNo)throws DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        boolean disclosureRequiresSync = false;
        String requiresSync = null;
        if(dbEngine != null){
            param.addElement(new Parameter("AS_DISCLOSURE_NO", "String", disclosureNo));
            StringBuffer sqlQuery = new StringBuffer("{ << OUT INTEGER REQUIRES_SYNC >> = ");
            sqlQuery.append("call fn_disclosure_requires_sync ( ");
            sqlQuery.append("<< AS_DISCLOSURE_NO >> ) }");
            result = dbEngine.executeFunctions("Coeus", sqlQuery.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable entitiesCheck = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap entitiesCheck = (HashMap)result.elementAt(0);
            /* case #748 end */
            requiresSync = entitiesCheck.get("REQUIRES_SYNC").toString();
            //System.out.println("fn_disclosure_requires_sync returned: "+requiresSync);
        }
        if(Integer.parseInt(requiresSync) == 1) {
          disclosureRequiresSync = true;
        }
        return disclosureRequiresSync;
    }
    /* CASE #357 End */

    /* CASE #357 Begin */
    public boolean syncDisclosureWithFE(String disclosureNo, String userID) throws DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        String syncSuccess = null;
        Timestamp currentTS = getTimestamp();
        if(dbEngine != null){
            param.addElement(new Parameter("AS_DISCLOSURE_NO", "String", disclosureNo));
            param.addElement(new Parameter("AV_UPDATE_TIMESTAMP", "Date", currentTS));
            param.addElement(new Parameter("AV_UPDATE_USER", "String", userID));
            StringBuffer sqlQuery = new StringBuffer("{ << OUT INTEGER SYNC_SUCCESS >> = ");
            sqlQuery.append("call fn_sync_disclosure_with_fe ( ");
            sqlQuery.append("<< AS_DISCLOSURE_NO >> , ");
            sqlQuery.append("<< AV_UPDATE_TIMESTAMP >> , ");
            sqlQuery.append("<< AV_UPDATE_USER >> )} ");
            result = dbEngine.executeFunctions("Coeus", sqlQuery.toString(), param);
            System.out.println("sync completed");
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable returnedRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap returnedRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            syncSuccess = returnedRow.get("SYNC_SUCCESS").toString();
            //System.out.println("fn_sync_disclosure_with_fe returned: "+syncSuccess);
        }
        if(Integer.parseInt(syncSuccess) == 1) {
          return true;
        }
        return false;
    }
    /* CASE #357 End */
    
    /*CASE #1402 Begin */
    public boolean checkDiscNoExists(String disclosureNumber)
                                                    throws DBException{
        boolean disclosureExists = false;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        //System.out.println("calling fn_check_disc_no_exists");
        if(dbEngine!=null){
            param.addElement(new Parameter("AV_PERSON_ID","String",personId));
            param.addElement(new Parameter("AV_COI_DISCLOSURE_NUMBER","String",disclosureNumber));
            StringBuffer strBuffSql = new StringBuffer("{ << OUT INTEGER RSET >> = ");
            strBuffSql.append("call fn_check_disc_no_exists ( ");
            strBuffSql.append("<< AV_PERSON_ID >> , ");
            strBuffSql.append(" << AV_COI_DISCLOSURE_NUMBER >>  )} ");
            result = dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        System.out.println("result set size: "+result.size());
        if(result!=null && !result.isEmpty()){
            HashMap resultRow = (HashMap)result.elementAt(0);
            String rset = resultRow.get("RSET").toString();
            disclosureExists = Integer.parseInt(rset) > 0;
        }        
        
        return disclosureExists;
    }
    /* CASE #1402 End */
    
}
