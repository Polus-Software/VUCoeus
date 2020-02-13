/*
 * NasaPIandAORSupplementalDataSheetStream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * Created on February 1, 2006 
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.NasaPIandAORSupplementalDataSheetTxnBean;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.nasa_piandaorsupplementaldatasheet_v1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class NasaPIandAORSupplementalDataSheetStream extends S2SBaseStream  { 
    private gov.grants.apply.forms.nasa_piandaorsupplementaldatasheet_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    //txn bean
    private NasaPIandAORSupplementalDataSheetTxnBean nasaPIandAORSmtDtShtTxnBean;
     
    private String propNumber;
    private UtilFactory utilFactory;
    //JIRA COEUSQA-3663 - START
    private final static String YES = "Y: Yes";
    private final static String NO = "N: No";
    private Vector vctAnswerList;
    //JIRA COEUSQA-3663 - END
    
    /** Creates a new instance of NasaPIandAORSmtDtShtStream */
    public NasaPIandAORSupplementalDataSheetStream() {        
        objFactory = new gov.grants.apply.forms.nasa_piandaorsupplementaldatasheet_v1.ObjectFactory();
        globallibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();   
    }
    
    private NasaPIandAORSupplementalDataSheetType getNasaPIandAORSupplementalDataSheet()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
        NasaPIandAORSupplementalDataSheetType nasaPIandAORSmtDtSht = objFactory.createNasaPIandAORSupplementalDataSheet();
        nasaPIandAORSmtDtShtTxnBean = new NasaPIandAORSupplementalDataSheetTxnBean();
        try{
          /**
            *FormVersion
           *
          */
            //JIRA COEUSQA-3663 - START
            ProposalDevelopmentTxnBean proposalTxnBean = new ProposalDevelopmentTxnBean();
            vctAnswerList = proposalTxnBean.getProposalQuestionnaire(propNumber);
            //JIRA COEUSQA-3663 - END
            nasaPIandAORSmtDtSht.setFormVersion("1.0");
            nasaPIandAORSmtDtSht.setAuthorizedRepresentativeName(getAuthorizedRep());
            nasaPIandAORSmtDtSht.setPrincipalInvestigatorName(getPrincipalInvestigator());
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NasaPIandAORSupplementalDataSheetStream","getNasaPIandAORSupplementalDataSheet()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        } 
        return nasaPIandAORSmtDtSht;
    }
       
     private NasaPIandAORSupplementalDataSheetType.AuthorizedRepresentativeNameType
                            getAuthorizedRep() throws JAXBException,DBException,CoeusException{
        
        NasaPIandAORSupplementalDataSheetType.AuthorizedRepresentativeNameType
                    authorizedRepName = objFactory.createNasaPIandAORSupplementalDataSheetTypeAuthorizedRepresentativeNameType();
        HashMap hmInfo = new HashMap();
        hmInfo = nasaPIandAORSmtDtShtTxnBean.getAORName(propNumber);
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
            authorizedRepName.setAORName(pdpiName);
            /* case 3463 -using custom element
            if (hmInfo.get("ERA_COMMONS_USER_NAME") != null){
                authorizedRepName.setUserName(hmInfo.get("ERA_COMMONS_USER_NAME").toString());
            }
            */
        }
    /* 
     * case 3463 -using custom element
     */
        hmInfo = null;
        hmInfo = nasaPIandAORSmtDtShtTxnBean.getNSPIRESUserName(propNumber);
        if (hmInfo != null && hmInfo.get("NSPIRES_USER_NAME") != null ){
            authorizedRepName.setUserName(hmInfo.get("NSPIRES_USER_NAME").toString());
        }
     
        
        return authorizedRepName;
    }
    
     private NasaPIandAORSupplementalDataSheetType.PrincipalInvestigatorNameType
                            getPrincipalInvestigator() throws JAXBException,DBException,CoeusException{
        
        NasaPIandAORSupplementalDataSheetType.PrincipalInvestigatorNameType
                    principalInvestigatorName = objFactory.createNasaPIandAORSupplementalDataSheetTypePrincipalInvestigatorNameType();
        HashMap hmInfo = new HashMap();
        hmInfo = nasaPIandAORSmtDtShtTxnBean.getPDPIName(propNumber);
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
            principalInvestigatorName.setPDPIName(pdpiName);
        }
        //JIRA COEUSQA-3663 - START
        ProposalYNQBean propYNQBean = getQuestionnaireAnswer("110", vctAnswerList);
        if (propYNQBean != null) {
            if (propYNQBean.getAnswer().equalsIgnoreCase("Y")) {
                principalInvestigatorName.setUSGovernmentParticipation(YES);
                propYNQBean = getQuestionnaireAnswer("111", vctAnswerList);
                DepartmentPersonTxnBean deptPersonTxnBean = new DepartmentPersonTxnBean();
                String sponsorName = deptPersonTxnBean.getDescForLookupCode(propYNQBean.getAnswer(), "VALUELIST", "Agency_US GOV");
                principalInvestigatorName.setFederalAgency(sponsorName);
                propYNQBean = getQuestionnaireAnswer("113", vctAnswerList);
                BigDecimal bd = new BigDecimal(propYNQBean.getAnswer());
                principalInvestigatorName.setFederalAgencyDollar(bd);
            }else{
                principalInvestigatorName.setUSGovernmentParticipation(NO);
            }
         }
        propYNQBean = getQuestionnaireAnswer("112", vctAnswerList);
        if(propYNQBean != null) {
            if (propYNQBean.getAnswer().equalsIgnoreCase("Y")) {
                principalInvestigatorName.setInternationalParticipation(YES);
            }else {
                principalInvestigatorName.setInternationalParticipation(NO);
            }
        }
        //JIRA COEUSQA-3663 - END
        return principalInvestigatorName;
    }
     
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getNasaPIandAORSupplementalDataSheet();
    }
    //JIRA COEUSQA-3663 - START
    private ProposalYNQBean getQuestionnaireAnswer(String questionID, Vector vecYNQ) throws CoeusException, DBException {
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
        return proposalYNQBean;
    }
    //JIRA COEUSQA-3663 - END

}
