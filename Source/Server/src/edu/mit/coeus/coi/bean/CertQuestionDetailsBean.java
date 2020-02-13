/*
 * @(#)CertQuestionDetailsBean.java 1.0 3/27/02
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
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
/**
 * This class is used to hold the details pertaining to a particular question using the
 * <code>QuestionDetailsBean</code> bean.
 *
 * @version 1.0 March 27, 2002, 11:58 AM
 * @author  Anil Nandakumar
 */
public class CertQuestionDetailsBean {
    /*
     *  instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /*
     *  Initialize bean with a particular person id. ie, All methods
     *  in this class always belong to a person.
     */
    private String personId;
    /**
     * Creates new CertQuestionDetailsBean.
     * Contructor with one argument
     * @param String person id
     */
    public CertQuestionDetailsBean(String personId){
        this.personId = personId;
        dbEngine = new DBEngineImpl();
    }
    /**
     *  This method is used to retrieve certificate details of a particular question.
     *  This method will throw an exception if the person id for this bean is null.
     *  To fetch the data, it uses dw_get_ynqexplanation procedure.
     *  @param String Question Number
     *  @return vector
     *  @exception CoeusException
     *  @exception DBException
     */
    public Vector getQuestionDetails(String questionNumber)
            throws CoeusException,DBException{
        if(personId==null){
            throw new CoeusException("exceptionCode.1001");
        }
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector questDetails = new Vector(3,2);
        param.addElement(new Parameter("question_id","String",questionNumber.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_ynqexplanation ( <<question_id>> , <<OUT RESULTSET rset>> ) ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int j=0;j<result.size();j++){
            /* case #748 comment begin */
            //Hashtable questionRow = (Hashtable)result.elementAt(j);
            /* case #748 begin */
            HashMap questionRow = (HashMap)result.elementAt(j);
            /* case #748 end */
            QuestionDetailsBean questionDetails = new QuestionDetailsBean();
            questionDetails.setQuestionID(questionRow.get("QUESTION_ID").toString());
            questionDetails.setExplanation((String)questionRow.get("EXPLANATION"));
            questionDetails.setDescription((String)questionRow.get("DESCRIPTION"));
            questionDetails.setExplanationType((String)questionRow.get("EXPLANATION_TYPE"));
            questDetails.add(questionDetails);
        }
        return questDetails;
    }

    /* CASE #864 Begin */
    /**
     *  Get all info for certification questions that apply to disclosures.  Also
     *  get appropriate answers to the questions for this user, based on user
     *  answers to certification questions for any disclosed financial
     *  entities.
     *  Call get_ann_disc_cert_questions
     * @return certQuestions Vector with all cert questions info
     * @throws CoeusException
     * @throws DBException
     */
    public Vector getAnnDiscCertQuestions() throws CoeusException, DBException{
        if(personId==null){
            throw new CoeusException("exceptionCode.1001");
        }
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector certQuestions= new Vector(3,2);
        param.addElement(new Parameter("AW_PERSON_ID", "String", this.personId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call get_ann_disc_cert_questions ( <<AW_PERSON_ID>> , <<OUT RESULTSET rset>> ) ",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int j=0;j<result.size();j++){
            HashMap questionRow = (HashMap)result.elementAt(j);
            CertificateDetailsBean certificateDetails = new CertificateDetailsBean();
            certificateDetails.setCode(questionRow.get("QUESTION_ID").toString());
            certificateDetails.setQuestion(questionRow.get("DESCRIPTION").toString());
            certificateDetails.setExplReqFor((String)questionRow.get("EXPLANATION_REQUIRED_FOR"));
            certificateDetails.setNumOfAns(questionRow.get("NO_OF_ANSWERS").toString());
            certificateDetails.setAnswer(questionRow.get("ANSWER").toString());
            /* CASE #1393 Begin */
            //Get corresponding entity cert question id and label, if any, 
            //for this disclosure certification question
            String entQuestId = 
                        getCorrespEntQuestId(certificateDetails.getCode());
            String entQuestLabel = getCertQuestLabel(entQuestId);
            certificateDetails.setCorrespEntQuestId(entQuestId);
            certificateDetails.setCorrespEntQuestLabel(entQuestLabel);
            //Get label for this disclosure cert question
            String label = getCertQuestLabel(certificateDetails.getCode());
            certificateDetails.setLabel(label);
            System.out.println("Put in disc cert question: ");
            System.out.println("&&&label: "+label);
            System.out.println("&&&entQuestLabel: "+entQuestLabel);
            /* CASE #1393 End */
            
            
            certQuestions.add(certificateDetails);
        }
        return certQuestions;
    }
    /* CASE #864 End */
    /* CASE #1393 Begin */
    public HashMap getEntitiesCertQuestionYes (String disclQuestionId)
            throws  DBException{
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        HashMap entities = new HashMap();
        //calling stored procedure
        if(dbEngine!=null){
            param.addElement(new Parameter("AW_PERSON_ID","String",personId));
            param.addElement(new Parameter("AS_QUESTION_ID","String",disclQuestionId));
            String sqlCommand = "call get_entities_cert_q_yes( ";
            sqlCommand += "<<AW_PERSON_ID>>, <<AS_QUESTION_ID>>,";
            sqlCommand += "<<OUT RESULTSET rset>> )";
            result = dbEngine.executeRequest("Coeus", sqlCommand, "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            for(int rowCount = 0; rowCount<result.size(); rowCount++){
                HashMap entityRow = (HashMap)result.elementAt(rowCount);
                entities.put((String)entityRow.get("ENTITY_NUMBER"),
                                        (String)entityRow.get("ENTITY_NAME"));
            }
        }
        return entities;
    }
    /* CASE #1393 End */

    /* CASE #1374 Begin */
    /**
     * Get corresponding entity certification question for a given disclosure
     * certification question
     * @return entQuestId
     * @throws DBException
     */
    public String getCorrespEntQuestId (String disclQuestId) throws DBException{
        String entQuestId = null;
        Vector param= new Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            System.out.println("call fn_get_corresp_ent_quest_id");
            param.addElement(new Parameter("AW_QUESTION_ID","String",disclQuestId));
            StringBuffer strBuffSql = new StringBuffer();
            strBuffSql.append("{ << OUT STRING ENT_QUEST_ID >> = ");
            strBuffSql.append(" call fn_get_corresp_ent_quest_id ( ");
            strBuffSql.append("<< AW_QUESTION_ID >> ) }");
            result = 
                dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);

        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            HashMap resultRow = (HashMap)result.elementAt(0);
            entQuestId = (String)resultRow.get("ENT_QUEST_ID");
        }     
        return entQuestId;
    }
    
    /**
     * Get corresponding entity certification question for a given disclosure
     * certification question
     * @return entQuestId
     * @throws DBException
     */
    public String getCertQuestLabel (String disclQuestId) throws DBException{
        String questionLabel = null;
        Vector param= new Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            System.out.println("call fn_get_cert_quest_label");
            param.addElement(new Parameter("AW_QUESTION_ID","String",disclQuestId));
            StringBuffer strBuffSql = new StringBuffer();
            strBuffSql.append("{ << OUT STRING LABEL >> = ");
            strBuffSql.append(" call fn_get_cert_quest_label ( ");
            strBuffSql.append("<< AW_QUESTION_ID >> ) }");
            result = 
                dbEngine.executeFunctions("Coeus", strBuffSql.toString(), param);

        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            HashMap resultRow = (HashMap)result.elementAt(0);
            questionLabel = (String)resultRow.get("LABEL");
        }     
        return questionLabel;
    }     
    /* CASE #1374 End */
}