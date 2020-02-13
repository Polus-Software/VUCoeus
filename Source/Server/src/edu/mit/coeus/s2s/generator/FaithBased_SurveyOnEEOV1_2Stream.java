/*
 * FaithBased_SurveyOnEEOv1_2Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */



package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import edu.mit.coeus.s2s.bean.FaithBased_SurveyOnEEOTxnBean;
import gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.*;


public class FaithBased_SurveyOnEEOV1_2Stream extends S2SBaseStream{ 
    
     private gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.ObjectFactory objFactory;
     private CoeusXMLGenrator xmlGenerator;
     private String propNumber;
     
     
    /** Creates a new instance of FaithBased_SurveyOnEEOv1_2Stream */
    public FaithBased_SurveyOnEEOV1_2Stream() {
        objFactory = new gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        
    }
    
    private SurveyOnEEOType    getFaithBased_SurveyOnEEOV() throws CoeusXMLException,CoeusException,DBException,JAXBException{
        SurveyOnEEOType surveyOnEEO = objFactory.createSurveyOnEEO();
        
        surveyOnEEO.setFormVersion("1.2");
        
        //set Applicant's OrganizationName & DUNS number
        FaithBased_SurveyOnEEOTxnBean faithBased_SurveyOnEEOTxnBean = new FaithBased_SurveyOnEEOTxnBean();
        HashMap hmInfo = new HashMap();
        hmInfo =  faithBased_SurveyOnEEOTxnBean.getOrganizationInfo(propNumber);
        if (hmInfo  !=  null){
            if (hmInfo.get("ORGANIZATION_NAME") != null ){
                 if (hmInfo.get("ORGANIZATION_NAME").toString().length() > 60)
                   surveyOnEEO.setOrganizationName(hmInfo.get("ORGANIZATION_NAME").toString().substring(0,60));
                else    
                   surveyOnEEO.setOrganizationName(hmInfo.get("ORGANIZATION_NAME").toString());
            }
            
             if (hmInfo.get("DUNS_NUMBER") != null ){
                  if (hmInfo.get("DUNS_NUMBER").toString().length() > 13)
                   surveyOnEEO.setDUNSID(hmInfo.get("DUNS_NUMBER").toString().substring(0,13));
                  else    
                   surveyOnEEO.setDUNSID(hmInfo.get("DUNS_NUMBER").toString());
             }
        }
        
        hmInfo = null;
        hmInfo =  faithBased_SurveyOnEEOTxnBean.getFedProgram(propNumber);
        if (hmInfo != null){
            if (hmInfo.get("OPPORTUNITY_TITLE") != null ){                 
                surveyOnEEO.setOpportunityTitle(hmInfo.get("OPPORTUNITY_TITLE").toString());
            }
            
            if (hmInfo.get("CFDA_NUMBER") != null ){
                surveyOnEEO.setCFDANumber(hmInfo.get("CFDA_NUMBER").toString());
            }
        }
  
        return surveyOnEEO;
    }
    
     
      public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getFaithBased_SurveyOnEEOV();
    }
}