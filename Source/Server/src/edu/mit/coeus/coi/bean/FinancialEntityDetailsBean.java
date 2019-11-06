/*
 * @(#)FinancialEntityDetailsBean.java 1.0 3/27/02
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
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
/**
 *
 * This class provides the methods for performing all procedure executions for
 * a Financial Entity Disclosure. Various methods are used to fetch
 * the Financial Entity Disclosure details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the databse interaction.
 *
 * @version 1.0 March 27, 2002, 11:59 AM
 * @author  Geo Thomas
 */
public class FinancialEntityDetailsBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /*
     * To hold service name for the database
     */
    private String serviceName;
    /*
     *  To hold data source name for the connection pool
     */
    private String dsn;
    /*
     *  Initialize bean with a particular person id. ie, All methods
     *  in this class always belong to a person.
     */
    private String personId;
    /*
     *  To hold the result after the execution of the dbEngine engine.
     */
    private Vector vectResult;
    /** Creates new FinancialEntityDetailsBean.
     * @param String Person Id
     */
    public FinancialEntityDetailsBean(String personId){
        this.personId = personId;
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }

    /** Contructor with three arguments
     * @param String Datasource name
     * @param String Service name
     * @param String Person Id
     */
    public FinancialEntityDetailsBean(String serviceName,String dsn,String personId) {
        this(personId);
        this.serviceName = serviceName;
        this.dsn = dsn;
    }
    /**
     *  Method used to get all the details of a Financial Entity.
     *  Throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_per_fin_int_disc_det procedure.
     *  @param String Entity Number
     *  @return EntityDetailsBean Entity Details
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public EntityDetailsBean getFinancialEntityDetails(String entityNumber)
            throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector param= new java.util.Vector();
        EntityDetailsBean entityDetails = new EntityDetailsBean();
        param.addElement(new Parameter("ENTITY_NUMBER","String",entityNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_per_fin_int_disc_det ( <<ENTITY_NUMBER>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(!result.isEmpty()){
            /* case #748 comment begin */
            //HashMap financialEntDisclRow = (HashMap)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap financialEntDisclRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            /* CASE #352 Begin */
            entityDetails.setPersonId(
                  (String)financialEntDisclRow.get("PERSON_ID"));
            /* CASE #352 End */
            entityDetails.setNumber(
                  (String)financialEntDisclRow.get("ENTITY_NUMBER"));
            entityDetails.setSeqNumber(
                  financialEntDisclRow.get("SEQUENCE_NUMBER").toString());
            entityDetails.setUpdateUser(
                  (String)financialEntDisclRow.get("UPDATE_USER"));
            entityDetails.setLastUpdate(
                  (Timestamp)financialEntDisclRow.get("UPDATE_TIMESTAMP"));
            entityDetails.setSponsor(
                financialEntDisclRow.get("SPONSOR_CODE")== null ? null :
                financialEntDisclRow.get("SPONSOR_CODE").toString());
            entityDetails.setSponsorName((String)financialEntDisclRow.get("SPONSOR_NAME"));
            entityDetails.setOrgDescription(
                  (String)financialEntDisclRow.get("ORG_RELATION_DESCRIPTION"));
            entityDetails.setShareOwnship(
                  (String)financialEntDisclRow.get("ENTITY_OWNERSHIP_TYPE"));
            entityDetails.setOrgRelationship(
                  financialEntDisclRow.get("RELATED_TO_ORG_FLAG").toString());
            entityDetails.setPersonReDesc(
                  (String)financialEntDisclRow.get("RELATIONSHIP_DESCRIPTION"));
            entityDetails.setPersonReType(
                  (String)financialEntDisclRow.get("DESCRIPTION"));
            entityDetails.setPersonReTypeCode(
                  financialEntDisclRow.get("RELATIONSHIP_TYPE_CODE").toString());
            entityDetails.setEntityDescription(
                  (String)financialEntDisclRow.get("STATUS_DESCRIPTION"));
            entityDetails.setName(
                  (String)financialEntDisclRow.get("ENTITY_NAME"));
            entityDetails.setTypeCode(
                  financialEntDisclRow.get("ENTITY_TYPE_CODE").toString());
            entityDetails.setType((String)financialEntDisclRow.get("ENTITY_TYPE"));
            entityDetails.setStatus(
                  (String)financialEntDisclRow.get("STATUS"));
            entityDetails.setStatusCode(
                  financialEntDisclRow.get("STATUS_CODE").toString());
        }
        return entityDetails;
    }
    /**
     *  Method used to get all history details of a Financial Entity.
     *  Throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_per_fin_int_histdiscdet procedure.
     *  @param String Entity Number
     *  @param String Sequence Number
     *  @return EntityDetailsBean Entity Details
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public EntityDetailsBean getFinancialEntityHistoryDetails(String entityNumber, String seqNumber)
            throws DBException,CoeusException{
        Vector result = new Vector();
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector param= new java.util.Vector();
        EntityDetailsBean entityDetails = new EntityDetailsBean();
        param.addElement(new Parameter("ENTITY_NUMBER","String",entityNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER","String",seqNumber));
        //DBEngineImpl dbEngine = DBEngineImpl.getInstance();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_per_fin_int_histdiscdet ( <<ENTITY_NUMBER>> , <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable financialEntDisclHistoryRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap financialEntDisclHistoryRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            /* CASE #352 Begin */
            entityDetails.setPersonId((String)financialEntDisclHistoryRow.get("PERSON_ID"));
            /* CASE #352 End */
            entityDetails.setSeqNumber
              (financialEntDisclHistoryRow.get("SEQUENCE_NUMBER").toString());
            entityDetails.setUpdateUser
              ((String)financialEntDisclHistoryRow.get("UPDATE_USER"));
            entityDetails.setLastUpdate
              ((Timestamp)financialEntDisclHistoryRow.get("UPDATE_TIMESTAMP"));
            entityDetails.setSponsor
              (financialEntDisclHistoryRow.get("SPONSOR_CODE") == null ? null :
              financialEntDisclHistoryRow.get("SPONSOR_CODE").toString());
            entityDetails.setOrgDescription
              ((String)financialEntDisclHistoryRow.get("ORG_RELATION_DESCRIPTION"));
            entityDetails.setShareOwnship
              (financialEntDisclHistoryRow.get("ENTITY_OWNERSHIP_TYPE") == null ? null :
              financialEntDisclHistoryRow.get("ENTITY_OWNERSHIP_TYPE").toString());
            entityDetails.setOrgRelationship
                  (financialEntDisclHistoryRow.get("RELATED_TO_ORG_FLAG").toString());
            entityDetails.setPersonReDesc
              ((String)financialEntDisclHistoryRow.get("RELATIONSHIP_DESCRIPTION"));
            entityDetails.setPersonReType
              ((String)financialEntDisclHistoryRow.get("DESCRIPTION"));
            entityDetails.setEntityDescription
              ((String)financialEntDisclHistoryRow.get("STATUS_DESCRIPTION"));
            entityDetails.setName
              ((String)financialEntDisclHistoryRow.get("ENTITY_NAME"));
            entityDetails.setType
              ((String)financialEntDisclHistoryRow.get("ENTITY_TYPE"));
            entityDetails.setStatus
              ((String)financialEntDisclHistoryRow.get("STATUS"));
        }
        return entityDetails;
    }
    /**
     *  Method used to get all Financial Entity certificate details for a disclosure.
     *  Throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_fin_int_certdetails_new procedure.
     *  @param String Entity Number
     *  @param String Sequence Number
     *  @return Vector CertificateDetailsBean instances
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public Vector getFECertificateDetails(String entityNumber,String seqNum)
            throws CoeusException,DBException{
        Vector result = new Vector(3,2);
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        System.out.println("call dw_get_fin_int_certdetails_new with ent no: "+entityNumber+", seqno: "+seqNum);
        Vector param= new java.util.Vector();
        Vector financialEntCertDetails = new Vector(3,2);
        param.addElement(new Parameter("PERSON_ID","String",personId));
        param.addElement(new Parameter("ENTITY_NUMBER","String",entityNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER","int",seqNum));
        if(dbEngine!=null){
            result = dbEngine.executeRequest(serviceName,
              "call dw_get_fin_int_certdetails_new ( <<PERSON_ID>> , <<ENTITY_NUMBER>> , <<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )   ",
              dsn, param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int certCount=0;certCount<result.size();certCount++){
            /* case #748 comment begin */
            //Hashtable certficateDetailsRow = (Hashtable)result.elementAt(certCount);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap certficateDetailsRow = (HashMap)result.elementAt(certCount);
            /* case #748 end */
            CertificateDetailsBean certificateDetails = new CertificateDetailsBean();
            certificateDetails.setCode
                  (certficateDetailsRow.get("QUESTION_ID").toString());
            certificateDetails.setQuestion
                  ((String)certficateDetailsRow.get("DESCRIPTION"));
            certificateDetails.setAnswer
                  (certficateDetailsRow.get("ANSWER") == null ? null :
                  certficateDetailsRow.get("ANSWER").toString());
            certificateDetails.setExplanation
                  ((String)certficateDetailsRow.get("EXPLANATION"));
            certificateDetails.setNumOfAns
                  (certficateDetailsRow.get("NO_OF_ANSWERS").toString());
            certificateDetails.setExplReqFor
                  ((String)certficateDetailsRow.get("EXPLANATION_REQUIRED_FOR"));
            certificateDetails.setDateReqFor
                  ((String)certficateDetailsRow.get("DATE_REQUIRED_FOR"));
            certificateDetails.setNumber
                  (certficateDetailsRow.get("ENTITY_NUMBER").toString());
            certificateDetails.setSeqNumber
                  (certficateDetailsRow.get("SEQUENCE_NUMBER").toString());
            certificateDetails.setReviewDate
                  ((Timestamp)certficateDetailsRow.get("REVIEW_DATE"));
            certificateDetails.setLastUpdate
                  ((Timestamp)certficateDetailsRow.get("UPDATE_TIMESTAMP"));
            /* CASE #1393 Begin */
            //Add display label to cert details bean.
            CertQuestionDetailsBean certQuestionDetails = 
                                    new CertQuestionDetailsBean(personId);
            String questionLabel = 
                certQuestionDetails.getCertQuestLabel(certificateDetails.getCode());
            certificateDetails.setLabel(questionLabel);   
            /* CASE #1393 End */            
            financialEntCertDetails.add(certificateDetails);
        }
        return financialEntCertDetails;
    }

    /**
     *  Method used to get all questions and it's details for a particular type.
     *  This returns a vector which contains instances of CertificateDetailsBean class.
     *  <li>To fetch all the data from the server, it uses dw_get_ynq_list procedure.
     *  @param String type
     *  @return Vector vector of CertificateDetailsBean instance
     *  @exception DBException
     *  @exception CoeusException
     */
    public Vector getAllCertDet(String type) throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector param= new java.util.Vector();
        Vector certDetails = new Vector(3,2);
        param.addElement(new Parameter("QUESTION_TYPE","String",type));
          param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,null));
        if(dbEngine!=null){
            result = dbEngine.executeRequest(serviceName,
            "call dw_get_ynq_list( <<QUESTION_TYPE>> ,<<MODULE_ITEM_KEY>>, <<OUT RESULTSET rset>> ) ",
            dsn, param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int certCount=0;certCount<result.size();certCount++){
            /* case #748 comment begin */
            //Hashtable certDetailsRow = (Hashtable)result.elementAt(certCount);
            /* case #748 comment end */
            /* case #748 begin  */
            HashMap certDetailsRow = (HashMap)result.elementAt(certCount);
            /* case #748 end */
            CertificateDetailsBean objCertDet = new CertificateDetailsBean();
            objCertDet.setCode(certDetailsRow.get("QUESTION_ID").toString());
            objCertDet.setQuestion((String)certDetailsRow.get("DESCRIPTION"));
            objCertDet.setNumOfAns(certDetailsRow.get("NO_OF_ANSWERS").toString());
            objCertDet.setExplReqFor((String)certDetailsRow.get("EXPLANATION_REQUIRED_FOR"));
            objCertDet.setDateReqFor((String)certDetailsRow.get("DATE_REQUIRED_FOR"));
            /* CASE #1393 Begin */
            //Add display label to cert details bean.
            CertQuestionDetailsBean certQuestionDetails = 
                                    new CertQuestionDetailsBean(personId);
            String questionLabel = 
                certQuestionDetails.getCertQuestLabel(objCertDet.getCode());
            objCertDet.setLabel(questionLabel);   
            /* CASE #1393 End */            
            certDetails.add(objCertDet);
        }
        return certDetails;
    }
    /**
     *  Method used to get all questions and it's details for a particular financial entity.
     *  This returns a vector which contains instances of CertificateDetailsBean class.
     *  <li>To fetch all the data from the server, it uses dw_get_fin_entity_ynqdetails procedure.
     *  @param String type
     *  @return Vector vector of CertificateDetailsBean instance
     *  @exception DBException
     *  @exception CoeusException
     */
    public Vector getFECertDetails(String strEntNum,String strSeqNum)
            throws DBException,CoeusException{
        //System.out.print("call dw_get_fin_entity_ynqdetails with ent number: "+strEntNum);
        //System.out.println("sequenceNumber: "+strSeqNum);
        Vector result = new Vector(3,2);
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector param= new java.util.Vector();
        Vector certDetails = new Vector(3,2);
        param.addElement(new Parameter("AW_PERSON_ID","String",personId));
        param.addElement(new Parameter("AW_ENTITY_NUMBER","String",strEntNum));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER","int",strSeqNum));
        StringBuffer sqlQry = new StringBuffer("call dw_get_fin_entity_ynqdetails( ");
        sqlQry.append( " <<AW_PERSON_ID>> ,");
        sqlQry.append( " <<AW_ENTITY_NUMBER>> ,");
        sqlQry.append( " <<AW_SEQUENCE_NUMBER>> ,");
        sqlQry.append( " <<OUT RESULTSET rset>> )");
        if(dbEngine!=null){
            result = dbEngine.executeRequest(serviceName, sqlQry.toString() , dsn, param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int certCount=0;certCount<result.size();certCount++){
            /* case #748 comment begin */
            //Hashtable certDetailsRow = (Hashtable)result.elementAt(certCount);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap certDetailsRow = (HashMap)result.elementAt(certCount);
            /* case #748 end */
            CertificateDetailsBean objCertDet = new CertificateDetailsBean();
            objCertDet.setNumber(certDetailsRow.get("ENTITY_NUMBER").toString());
            objCertDet.setSeqNumber(certDetailsRow.get("SEQUENCE_NUMBER").toString());
            objCertDet.setCode(certDetailsRow.get("QUESTION_ID").toString());
            objCertDet.setQuestion((String)certDetailsRow.get("DESCRIPTION"));
            objCertDet.setAnswer(
                  certDetailsRow.get("ANSWER") == null ? null :
                  certDetailsRow.get("ANSWER").toString());
            objCertDet.setReviewDate((Timestamp)certDetailsRow.get("REVIEW_DATE"));
            objCertDet.setLastUpdate((Timestamp)certDetailsRow.get("UPDATE_TIMESTAMP"));
            objCertDet.setNumOfAns(certDetailsRow.get("NO_OF_ANSWERS").toString());
            certDetails.add(objCertDet);
        }
        return certDetails;
    }
    /**
     *  Method used to get all relations.
     *  This returns a LinkedList which holds instances of realtion bean.
     *  <li>To fetch the data, it uses dw_get_fin_int_entity_rel_type procedure.
     *  @return LinkedList list of ComboBoxBean instance
     *  @exception DBException
     *  @exception CoeusException
     */
    public LinkedList getAllRelations() throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        LinkedList relations = new LinkedList();
        if(dbEngine!=null){
            result = dbEngine.executeRequest(serviceName,
                "call dw_get_fin_int_entity_rel_type( <<OUT RESULTSET rset>> )   ",
                dsn, param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int relationCount=0;relationCount<result.size();relationCount++){
            /* case #748 comment begin */
            //Hashtable relationRow = (Hashtable)result.elementAt(relationCount);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap relationRow = (HashMap)result.elementAt(relationCount);
            /* case #748 end */
            ComboBoxBean relation = new ComboBoxBean();
            relation.setCode(relationRow.get("RELATIONSHIP_TYPE_CODE").toString());
            relation.setDescription((String)relationRow.get("DESCRIPTION"));
            relations.add(relation);
        }
        return relations;
    }
    /**
     *  Method used to get all organization types.
     *  This returns a LinkedList which holds instances of ComboBox bean.
     *  <li>To fetch the data, it uses dw_get_organization_type_list procedure.
     *  @return LinkedList list of ComboBoxBean instance
     *  @exception DBException
     *  @exception CoeusException
     */
    public LinkedList getAllOrgTypes() throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        LinkedList orgTypes = new LinkedList();
        if(dbEngine!=null){
            result = dbEngine.executeRequest(serviceName,
                "call dw_get_organization_type_list( <<OUT RESULTSET rset>> )   ",
                dsn, param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int orgTypeCount=0;orgTypeCount<result.size();orgTypeCount++){
            /* case #748 comment begin  */
            //Hashtable orgTypeRow = (Hashtable)result.elementAt(orgTypeCount);
            /* case #748 comment end */
            /* case #748 begin  */
            HashMap orgTypeRow = (HashMap)result.elementAt(orgTypeCount);
            /* case #748 end */
            ComboBoxBean orgType = new ComboBoxBean();
            orgType.setCode(orgTypeRow.get("ORGANIZATION_TYPE_CODE").toString());
            orgType.setDescription((String)orgTypeRow.get("DESCRIPTION"));
            orgTypes.add(orgType);
        }
        return orgTypes;
    }
    /**
     *  Method used to get all entity status.
     *  This returns a LinkedList which holds instances of comboboxbean.
     *  <li>To fetch the data, it uses dw_get_fin_int_entity_status procedure.
     *  @return LinkedList list of ComboBoxBean instance
     *  @exception DBException
     *  @exception CoeusException
     */
    public LinkedList getAllEntStatus() throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        LinkedList financialEntStats = new LinkedList();
        if(dbEngine!=null){
            result = dbEngine.executeRequest(serviceName,
                "call dw_get_fin_int_entity_status( <<OUT RESULTSET rset>> )   ",
                dsn, param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int k=0;k<result.size();k++){
            /* case #748 comment begin  */
            //Hashtable financialEntStatRow = (Hashtable)result.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap financialEntStatRow = (HashMap)result.elementAt(k);
            /* case #748 end */
            ComboBoxBean entStatus = new ComboBoxBean();
            entStatus.setCode(financialEntStatRow.get("STATUS_CODE").toString());
            entStatus.setDescription((String)financialEntStatRow.get("DESCRIPTION"));
            financialEntStats.add(entStatus);
        }
        return financialEntStats;
    }
    /**
     *  Method used to fetch the Next sequence number of Financial Entity.
     *  <li>To fetch the data, it uses fn_get_next_entity_number procedure.
     *  @return String Next Sequence Number
     *  @exception DBException
     */
    public String getNextSeqNum() throws DBException{
        String seqNum = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER SEQNUMBER>> = call fn_get_next_entity_number() }", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(!result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable seqNumRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap seqNumRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            seqNum = seqNumRow.get("SEQNUMBER").toString();
        }
        return seqNum;
    }
    /**
     *  Method used to fetch the Timestamp from the database.
     *  Returns a current timestamp from the database.
     *  <li>To fetch the data, it uses dw_get_cur_sysdate procedure.
     *  @return Timestamp current timestamp from the database
     *  @exception DBException
     */
    public Timestamp getTimestamp() throws DBException{
        Timestamp currDateTime = null;
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
            //Hashtable sysdateRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap sysdateRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            currDateTime = (Timestamp)sysdateRow.get("SYSDATE");
        }
        return currDateTime;
    }
    
    /* CASE #1374*/
    //Modify method to get sequence number from entityDetails bean passed.
    //don't take ac_type as parameter.  
    /**
     *  Method used to Add/Update Financial Entity for a particular person.
     *  To Add or Modify Financial Entity details, it uses the same procedures by passing
     *  different values for ac_type.
     *  <li>If the ac_type is 'I', the request is for adding a new Financial Entity.
     *  <li>If the ac_type is 'U', the request is for updating the Financial Entity.
     *  <li>Two following procedures are being called to perform the Add/Update operation.
     *  <br>1) dw_upd_person_fin_int_disc for FinancialEntity details
     *  <br>2) dw_upd_fin_int_entity_ynq for Question details
     *  @param EntityDetailsBean Entity Details
     *  @param Vector vector of CertificateDetail instance
     *  @param String ac_type
     *  @return boolean Transaction Status. If any one of the procedures is failed to execute,
     *                  it rollsback the entire transaction and returns false.
     *  @exception DBException
     *  @exception CoeusException
     */
    public boolean addUpdateEntityDetails(EntityDetailsBean entityDetails,
            Vector certificates)
                throws DBException,CoeusException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector procedures = new Vector(3,2);
        Vector paramEntityInfo= new java.util.Vector();
        Timestamp currDateTime = this.getTimestamp();
        /*int seqNum = acType.trim().equals("I")?1:
                            Integer.parseInt(entityDetails.getSeqNumber())+1;*/
        paramEntityInfo.addElement(new Parameter
              ("AV_PERSON_ID","String",personId));
        paramEntityInfo.addElement(new Parameter
              ("AV_ENTITY_NUMBER","String",entityDetails.getNumber()));
        paramEntityInfo.addElement(new Parameter
              ("AV_SEQUENCE_NUMBER","int",entityDetails.getSeqNumber()));
        System.out.println("update entity, seq no: "+entityDetails.getSeqNumber());
        paramEntityInfo.addElement(new Parameter
              ("AV_STATUS_CODE","int",entityDetails.getStatusCode()));
        paramEntityInfo.addElement(new Parameter
              ("AV_STATUS_DESCRIPTION","String",entityDetails.getEntityDescription()));
        paramEntityInfo.addElement(new Parameter
              ("AV_ENTITY_NAME","String",entityDetails.getName()));
        paramEntityInfo.addElement(new Parameter
              ("AV_ENTITY_TYPE_CODE","int",entityDetails.getTypeCode()));
        paramEntityInfo.addElement(new Parameter
              ("AV_ENTITY_OWNERSHIP_TYPE","String",entityDetails.getShareOwnship()));
        paramEntityInfo.addElement(new Parameter
              ("AV_RELATIONSHIP_TYPE_CODE","int",entityDetails.getPersonReTypeCode()));
        paramEntityInfo.addElement(new Parameter
              ("AV_RELATIONSHIP_DESCRIPTION","String",entityDetails.getPersonReDesc()));
        paramEntityInfo.addElement(new Parameter
              ("AV_RELATED_TO_ORG_FLAG","String",entityDetails.getOrgRelationship()));
        paramEntityInfo.addElement(new Parameter
              ("AV_ORG_RELATION_DESCRIPTION","String",entityDetails.getOrgDescription()));
        paramEntityInfo.addElement(new Parameter
              ("AV_SPONSOR_CODE","String",entityDetails.getSponsor()));
        paramEntityInfo.addElement(new Parameter
              ("AV_UPDATE_TIMESTAMP","Date",currDateTime));
        paramEntityInfo.addElement(new Parameter
              ("AV_UPDATE_USER","String",entityDetails.getUpdateUser()));
        paramEntityInfo.addElement(new Parameter
              ("AC_TYPE","String","I"));
        StringBuffer sqlQry =  new StringBuffer("call dw_upd_person_fin_int_disc ( ");
        sqlQry.append(" << AV_PERSON_ID >> ,");
        sqlQry.append(" << AV_ENTITY_NUMBER >> ,");
        sqlQry.append(" << AV_SEQUENCE_NUMBER >> ,");
        sqlQry.append(" << AV_STATUS_CODE >> ,");
        sqlQry.append(" << AV_STATUS_DESCRIPTION >> ,");
        sqlQry.append(" << AV_ENTITY_NAME >> ,");
        sqlQry.append(" << AV_ENTITY_TYPE_CODE >> ,");
        sqlQry.append(" << AV_ENTITY_OWNERSHIP_TYPE >> ,");
        sqlQry.append(" << AV_RELATIONSHIP_TYPE_CODE >> ,");
        sqlQry.append(" << AV_RELATIONSHIP_DESCRIPTION >> ,");
        sqlQry.append(" << AV_RELATED_TO_ORG_FLAG >> ,");
        sqlQry.append(" << AV_ORG_RELATION_DESCRIPTION >> ,");
        sqlQry.append(" << AV_SPONSOR_CODE >> ,");
        sqlQry.append(" << AV_UPDATE_TIMESTAMP >> ,");
        sqlQry.append(" << AV_UPDATE_USER >> ,");
        sqlQry.append(" << AC_TYPE >> )");
        ProcReqParameter procReqParamFinEnt = new ProcReqParameter();
        procReqParamFinEnt.setDSN(dsn);
        procReqParamFinEnt.setParameterInfo(paramEntityInfo);
        procReqParamFinEnt.setSqlCommand(sqlQry.toString());
        procedures.addElement(procReqParamFinEnt);
        if(certificates!=null){
            int certDetailsSize = certificates.size();
            Vector paramCertInfo = new Vector(3,2);
            CertificateDetailsBean certificateDetails = null;
            for(int certDetailsCount=0;certDetailsCount<certDetailsSize;certDetailsCount++){
                certificateDetails = (CertificateDetailsBean)certificates.elementAt(certDetailsCount);
                //String strSeqNum = certificateDetails.getSeqNumber();
                //int nextSeqNum = acType.equals("I")?1:(Integer.parseInt(strSeqNum)+1);
                paramCertInfo= new java.util.Vector();
                paramCertInfo.addElement(new Parameter("AV_PERSON_ID","String",personId));
                paramCertInfo.addElement(new Parameter("AV_ENTITY_NUMBER","String",certificateDetails.getNumber()));
                paramCertInfo.addElement(new Parameter("AV_SEQUENCE_NUMBER","int",entityDetails.getSeqNumber()));
                paramCertInfo.addElement(new Parameter("AV_QUESTION_ID","String",certificateDetails.getCode()));
                paramCertInfo.addElement(new Parameter("AV_ANSWER","String",certificateDetails.getAnswer()));
                paramCertInfo.addElement(new Parameter("AV_EXPLANATION","String",certificateDetails.getExplanation()));
                paramCertInfo.addElement(new Parameter("AV_REVIEW_DATE","Date",certificateDetails.getReviewDate()));
                paramCertInfo.addElement(new Parameter("AV_UPDATE_TIMESTAMP","Date",currDateTime));
                paramCertInfo.addElement(new Parameter("AV_UPDATE_USER","String",certificateDetails.getUpdateUser()));
                paramCertInfo.addElement(new Parameter("AW_PERSON_ID","String",personId));
                paramCertInfo.addElement(new Parameter("AW_ENTITY_NUMBER","String",certificateDetails.getNumber()));
                paramCertInfo.addElement(new Parameter("AW_SEQUENCE_NUMBER","int",certificateDetails.getSeqNumber()));
                paramCertInfo.addElement(new Parameter("AW_QUESTION_ID","String",certificateDetails.getCode()));
                paramCertInfo.addElement(new Parameter("AW_UPDATE_TIMESTAMP","Date",certificateDetails.getLastUpdate()));
                paramCertInfo.addElement(new Parameter("AC_TYPE","String","I"));
                StringBuffer strBuffCertSql =  new StringBuffer("call dw_upd_fin_int_entity_ynq ( ");
                strBuffCertSql.append(" << AV_PERSON_ID >> ,");
                strBuffCertSql.append(" << AV_ENTITY_NUMBER >> ,");
                strBuffCertSql.append(" << AV_SEQUENCE_NUMBER >> ,");
                strBuffCertSql.append(" << AV_QUESTION_ID >> ,");
                strBuffCertSql.append(" << AV_ANSWER >> ,");
                strBuffCertSql.append(" << AV_EXPLANATION >> ,");
                strBuffCertSql.append(" << AV_REVIEW_DATE >> ,");
                strBuffCertSql.append(" << AV_UPDATE_TIMESTAMP >> ,");
                strBuffCertSql.append(" << AV_UPDATE_USER >> ,");
                strBuffCertSql.append(" << AW_PERSON_ID >> ,");
                strBuffCertSql.append(" << AW_ENTITY_NUMBER >> ,");
                strBuffCertSql.append(" << AW_SEQUENCE_NUMBER >> ,");
                strBuffCertSql.append(" << AW_QUESTION_ID >> ,");
                strBuffCertSql.append(" << AW_UPDATE_TIMESTAMP >> ,");
                strBuffCertSql.append(" << AC_TYPE >> )");
                ProcReqParameter procReqParamCert = new ProcReqParameter();
                procReqParamCert.setDSN(dsn);
                procReqParamCert.setParameterInfo(paramCertInfo);
                procReqParamCert.setSqlCommand(strBuffCertSql.toString());
                procedures.addElement(procReqParamCert);
            }
        }
        if(dbEngine!=null){
            vectResult = dbEngine.executeStoreProcs(procedures);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        return true;
    }
    
    /*CASE #1402 Begin */
    public boolean checkEntSeqNoExists(String entityNumber, String sequenceNumber)
                                                             throws DBException{
        boolean entSeqNoExists = false;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        //System.out.println("calling fn_check_ent_seq_no_exists");
        if(dbEngine!=null){
            param.addElement(new Parameter("av_person_id","String",personId));
            param.addElement(new Parameter("av_entity_number","String", entityNumber));
            param.addElement(new Parameter("av_sequence_number","String", sequenceNumber));
            StringBuffer strBuffSql = new StringBuffer("{ << OUT INTEGER RSET >> = ");
            /* CASE #570 Comment Begin */
            //strBuffSql.append("call fn_check_person_has_disclosure ( ");
            /* CASE #570 Comment End */
            /* CASE #570 Begin */
            strBuffSql.append("call fn_check_ent_seq_no_exists ( ");
            /* CASE #570 End */
            strBuffSql.append("<< av_person_id >> , ");
            strBuffSql.append(" << av_entity_number >>, << av_sequence_number >> )} ");
            result = dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable disclNumRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap resultRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            String rset = resultRow.get("RSET").toString();
            entSeqNoExists = Integer.parseInt(rset) > 0;
        }        
        
        return entSeqNoExists;
    }
    /* CASE #1402 End */    
}