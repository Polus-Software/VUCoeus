/*
 * PHS398CoverPageSupplementV1_3Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.PHS398CoverPageSupplementTxnBean;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.phs398_coverpagesupplement_1_3_v1_3.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 *
 * @author  jenlu
 */
public class PHS398CoverPageSupplementV1_3Stream extends S2SBaseStream{ 
    private gov.grants.apply.forms.phs398_coverpagesupplement_1_3_v1_3.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    //txn bean
    private PHS398CoverPageSupplementTxnBean pns398CoverPageSupplementTxnBean;
     
    private String propNumber;
    private UtilFactory utilFactory;
    private Vector vctAnswerList;
    private final static String YES = "Y: Yes";
    private final static String NO = "N: No";
    
    /** Creates a new instance of PHS398CoverPageSupplementV1_1Stream */
    public PHS398CoverPageSupplementV1_3Stream() {
        objFactory = new gov.grants.apply.forms.phs398_coverpagesupplement_1_3_v1_3.ObjectFactory();
        globallibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();   
    }
    
    private PHS398CoverPageSupplement13Type getPHS398CoverPageSupplement()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
        PHS398CoverPageSupplement13Type phs398CoverPageSupplementType = objFactory.createPHS398CoverPageSupplement13();
    
        pns398CoverPageSupplementTxnBean = new PHS398CoverPageSupplementTxnBean();
               
        try{
          /**
            *FormVersion
           *
          */
            //JIRA COEUSQA-3660 - START
            ProposalDevelopmentTxnBean proposalTxnBean = new ProposalDevelopmentTxnBean();
            vctAnswerList = proposalTxnBean.getProposalQuestionnaire(propNumber);
            //JIRA COEUSQA-3660 - END
            phs398CoverPageSupplementType.setFormVersion("1.3");
            phs398CoverPageSupplementType.setPDPI(getPDPIType());
            phs398CoverPageSupplementType.setClinicalTrial(getClinicalTrialType());
            phs398CoverPageSupplementType.setContactPersonInfo(getContactPersonInfoType());
            PHS398CoverPageSupplement13Type.StemCellsType stemCells = getStemCellsType();
            if (stemCells != null)
                phs398CoverPageSupplementType.setStemCells(stemCells);
             return phs398CoverPageSupplementType;
            
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398CoverPageSupplementV1_1Stream","getPHS398CoverPageSupplement()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }           
    }   
    
    private PHS398CoverPageSupplement13Type.PDPIType getPDPIType() throws JAXBException,DBException,CoeusException{
        
        PHS398CoverPageSupplement13Type.PDPIType pdpiType = objFactory.createPHS398CoverPageSupplement13TypePDPIType();
        HashMap hmInfo = new HashMap();
        hmInfo = pns398CoverPageSupplementTxnBean.getPI(propNumber);
        if (hmInfo != null){
            HumanNameDataType pdpiName
                        = globallibObjFactory.createHumanNameDataType();
            if (hmInfo.get("FIRST_NAME") != null){
                pdpiName.setFirstName(hmInfo.get("FIRST_NAME").toString());
            }
            if (hmInfo.get("MIDDLE_NAME") != null){
                pdpiName.setMiddleName(hmInfo.get("MIDDLE_NAME").toString());
            }
            if (hmInfo.get("LAST_NAME") != null){
                pdpiName.setLastName(hmInfo.get("LAST_NAME").toString());
            }
            pdpiType.setPDPIName(pdpiName);
        }
        
        hmInfo = null;
        hmInfo = pns398CoverPageSupplementTxnBean.getIsNewInvestigator(propNumber);
        if (hmInfo != null && hmInfo.get("IS_NEW_INVESTIGATOR") != null ){
            //v1.1 changed enumeration. (from v1.0 "Yes/No" to  V1.1 "Y Yes/N No")
            if (hmInfo.get("IS_NEW_INVESTIGATOR").toString().startsWith("Y"))
                pdpiType.setIsNewInvestigator("Y: " + hmInfo.get("IS_NEW_INVESTIGATOR").toString());
            else pdpiType.setIsNewInvestigator("N: " + hmInfo.get("IS_NEW_INVESTIGATOR").toString());
        }
//        Vector vctDgrees = null;
//        vctDgrees = pns398CoverPageSupplementTxnBean.getPIDegrees(propNumber);
//      
//        int size=vctDgrees==null?0:vctDgrees.size();
//        if (size > 4) size = 4; //Degrees: 0...3
//        for (int row=0; row < size; row++) {
//            hmInfo = (HashMap)vctDgrees.get(row);
//            pdpiType.getDegrees().add(hmInfo.get("DEGREE_CODE").toString());
//        }
        return pdpiType;
    }
    
     private PHS398CoverPageSupplement13Type.ClinicalTrialType getClinicalTrialType() throws JAXBException,DBException,CoeusException{
        PHS398CoverPageSupplement13Type.ClinicalTrialType clinicalTrialType = objFactory.createPHS398CoverPageSupplement13TypeClinicalTrialType();
        //JIRA COEUSQA-3660 - START
        ProposalYNQBean propYNQBean = getQuestionnaireAnswer("2", vctAnswerList);//Does it include clinical trial
         if (propYNQBean != null) {
             String answer = propYNQBean.getAnswer();
             if (answer.equals("Y")) {
                 clinicalTrialType.setIsClinicalTrial(YES);
                 propYNQBean = getQuestionnaireAnswer("3", vctAnswerList);//Phase 3 clinical trial
                 answer = propYNQBean.getAnswer();
                 if (answer.equals("Y")) {
                     clinicalTrialType.setIsPhaseIIIClinicalTrial(YES);
                 } else {
                     clinicalTrialType.setIsPhaseIIIClinicalTrial(NO);
                 }
             } else {
                 clinicalTrialType.setIsClinicalTrial(NO);
             }
         }
        //JIRA COEUSQA-3660 - END
        return clinicalTrialType;
     }
     //JIRA COEUSQA-3660 - START
     private List<ProposalYNQBean> getQuestionnaireAnswers(String questionID, Vector vecYNQ) throws CoeusException, DBException {
         ProposalYNQBean tempBean;
         List<ProposalYNQBean> ynqBeanList = new ArrayList();
         String question;
         if (vecYNQ != null) {
             for (int vecCount = 0; vecCount < vecYNQ.size(); vecCount++) {
                 tempBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                 question = tempBean.getQuestionId();
                 if (question.equals(questionID)) {
                     ynqBeanList.add(tempBean);
                 }
             }
         }
         return ynqBeanList;
     }

     private ProposalYNQBean getQuestionnaireAnswer(String questionID, Vector vecYNQ)throws CoeusException, DBException{
         ProposalYNQBean tempBean;
         ProposalYNQBean proposalYNQBean = null;
         List ynqlist = new ArrayList();
         String question;
         if (vecYNQ != null) {
             for (int vecCount = 0; vecCount < vecYNQ.size(); vecCount++) {
                 tempBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                 question = tempBean.getQuestionId();

                 if (question.equals(questionID)) {
                     proposalYNQBean = tempBean;
                     break;
                 }
             }
         }
         return  proposalYNQBean;
     }
     //JIRA COEUSQA-3660 - END

     private PHS398CoverPageSupplement13Type.ContactPersonInfoType getContactPersonInfoType() throws JAXBException,DBException,CoeusException{
        PHS398CoverPageSupplement13Type.ContactPersonInfoType contactPersonInfoType = objFactory.createPHS398CoverPageSupplement13TypeContactPersonInfoType();
        HashMap hmName = new HashMap();
        HashMap hmInfo = new HashMap();
        hmInfo = pns398CoverPageSupplementTxnBean.getContactPersonInfo(propNumber);
        if (hmInfo != null){
            HumanNameDataType contactName 
                        = globallibObjFactory.createHumanNameDataType();
            if (hmInfo.get("FIRST_NAME") != null){
                contactName.setFirstName(hmInfo.get("FIRST_NAME").toString());
            }
            if (hmInfo.get("MIDDLE_NAME") != null){
                contactName.setMiddleName(hmInfo.get("MIDDLE_NAME").toString());
            }
            if (hmInfo.get("LAST_NAME") != null){
                contactName.setLastName(hmInfo.get("LAST_NAME").toString());
            }
            contactPersonInfoType.setContactName(contactName);
            
            if (hmInfo.get("OFFICE_PHONE") != null){
                contactPersonInfoType.setContactPhone(hmInfo.get("OFFICE_PHONE").toString());
            }
            if (hmInfo.get("FAX_NUMBER") != null){
                contactPersonInfoType.setContactFax(hmInfo.get("FAX_NUMBER").toString());
            }
            if (hmInfo.get("EMAIL_ADDRESS") != null){
                contactPersonInfoType.setContactEmail(hmInfo.get("EMAIL_ADDRESS").toString());
            }
            if (hmInfo.get("PRIMARY_TITLE") != null){
                contactPersonInfoType.setContactTitle(hmInfo.get("PRIMARY_TITLE").toString());
            }
            
            AddressDataType contactAddress
                        = globallibObjFactory.createAddressDataType();
            if (hmInfo.get("ADDRESS_LINE_1") != null){
                contactAddress.setStreet1(hmInfo.get("ADDRESS_LINE_1").toString());
            }
            if (hmInfo.get("ADDRESS_LINE_2") != null){
                contactAddress.setStreet2(hmInfo.get("ADDRESS_LINE_2").toString());
            }
            if (hmInfo.get("CITY") != null){
                contactAddress.setCity(hmInfo.get("CITY").toString());
            }
            if (hmInfo.get("COUNTY") != null){
                contactAddress.setCounty(hmInfo.get("COUNTY").toString());
            }
            if (hmInfo.get("STATE") != null){
                hmName = null;
                hmName = pns398CoverPageSupplementTxnBean.getStateName(hmInfo.get("STATE").toString());
                // case 2660 start
//                if (hmName != null)
                if (hmName != null && hmName.get("STATE_NAME") != null)
                // case 2660 end  
                contactAddress.setState(hmName.get("STATE_NAME").toString());
            }
            if (hmInfo.get("POSTAL_CODE") != null){
                contactAddress.setZipPostalCode(hmInfo.get("POSTAL_CODE").toString());
            }
            if (hmInfo.get("COUNTRY_CODE") != null){
                hmName = null;
                hmName = pns398CoverPageSupplementTxnBean.getCountryName(hmInfo.get("COUNTRY_CODE").toString());
                // case 2660 start
//                if (hmName != null)
                if (hmName != null && hmName.get("COUNTRY_NAME") != null)
                // case 2660 end     
                contactAddress.setCountry(hmName.get("COUNTRY_NAME").toString());
            }
            contactPersonInfoType.setContactAddress(contactAddress);
        }
        return contactPersonInfoType;
     }
     
      private PHS398CoverPageSupplement13Type.StemCellsType getStemCellsType() throws JAXBException,DBException,CoeusException{
        PHS398CoverPageSupplement13Type.StemCellsType stemCellsType = null;
        //JIRA COEUSQA-3660 - START
        ProposalYNQBean propYNQBean = getQuestionnaireAnswer("5", vctAnswerList);//Stem Cells
          if (propYNQBean != null) {
              stemCellsType = objFactory.createPHS398CoverPageSupplement13TypeStemCellsType();
              String answer = propYNQBean.getAnswer();
              if (answer.equals("Y")) {
                  stemCellsType.setIsHumanStemCellsInvolved(YES);
                  propYNQBean = getQuestionnaireAnswer("6", vctAnswerList);//Stem Cell Reference
                  answer = propYNQBean.getAnswer();
                  if (answer.equals("Y")) {
                      stemCellsType.setStemCellsIndicator(YES);
                      List<ProposalYNQBean> ynqList = getQuestionnaireAnswers("7", vctAnswerList);//List of registration Number
                      if (ynqList.size() > 0) {
                          for (int index = 0; index < ynqList.size(); index++) {
                              propYNQBean = ynqList.get(index);
                              stemCellsType.getCellLines().add(propYNQBean.getAnswer());
                          }
                          stemCellsType.setStemCellsIndicator(NO);//If Cell Line Data present, Then un-Check
                      }else{
                        stemCellsType.setStemCellsIndicator(YES);//If no Cell Line Data, Then Check
                      }
                  } else {
                      stemCellsType.setStemCellsIndicator(YES);
                  }
              } else {
                  stemCellsType.setIsHumanStemCellsInvolved(NO);
              }
          }
        //JIRA COEUSQA-3660 - END
        return stemCellsType;
     }
    
     public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398CoverPageSupplement();
    }  
}
