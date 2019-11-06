
package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.vanderbilt.coeus.s2s.bean.PHSAssignmentRequestFormTxnBean_V1_0;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import java.util.HashMap;
import javax.xml.bind.JAXBException;

import gov.grants.apply.forms.phs_assignmentrequestform_v1_0.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.generator.*;

/**
 *
 * @author Jill McAfee
 */
public class PHSAssignmentRequestFormStream_V1_0 extends S2SBaseStream { 
    private gov.grants.apply.forms.phs_assignmentrequestform_v1_0.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    // Txn bean
    private PHSAssignmentRequestFormTxnBean_V1_0 phsAssignmentRequestFormTxnBean;
    
	private HashMap questionnaireData;
    private String propNumber;
    
    /* Question / answer IDs */
    private final static String AWARDING_COMPONENT_REQUESTS_YN = "593.1";
    private final static String AWARDING_COMPONENT_REQUESTS = "594";
    private final static String DO_NOT_ASSIGN_AWARDING_COMPONENTS_YN = "595.1";
    private final static String DO_NOT_ASSIGN_AWARDING_COMPONENTS = "596";
    private final static String STUDY_SECTION_REQUESTS_YN = "597.1";
    private final static String STUDY_SECTION_REQUESTS = "598";
    private final static String DO_NOT_ASSIGN_STUDY_SECTIONS_YN = "599.1";
    private final static String DO_NOT_ASSIGN_STUDY_SECTIONS = "600";
    private final static String DO_NOT_REVIEW_INDIVIDUALS_YN = "601.1";
    private final static String DO_NOT_REVIEW_INDIVIDUALS = "602.1";
    private final static String EXPERTISE_AREAS_YN = "603.1";
    private final static String EXPERTISE_AREAS = "604";
    
     /** Creates a new instance of PHSAssignmentRequestFormStream_V1_0 */
    public PHSAssignmentRequestFormStream_V1_0(){
    	objFactory = new gov.grants.apply.forms.phs_assignmentrequestform_v1_0.ObjectFactory();
        globallibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
    	globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
      
    	xmlGenerator = new CoeusXMLGenrator();  
    }    

    private PHSAssignmentRequestFormType getPHSAssignmentRequestForm()
		   throws CoeusXMLException,CoeusException,DBException,JAXBException {
     
    	PHSAssignmentRequestFormType phsAssignmentRequestFormType = objFactory.createPHSAssignmentRequestForm();
    	questionnaireData = getQuestionnaireAnswers();
    	
    	phsAssignmentRequestFormType.setFormVersion("1.0");
    	
    	/* Funding Opportunity Info */
    	OpportunityInfoBean fundingOpportunity = getFundingOpportunity();
    	
    	if (fundingOpportunity != null) {
    		phsAssignmentRequestFormType.setFundingOpportunityNumber(fundingOpportunity.getOpportunityId());
    		phsAssignmentRequestFormType.setFundingOpportunityTitle(fundingOpportunity.getOpportunityTitle());
    	}
    	
    	/* Awarding Component Assignments */
		if (questionnaireData.get(AWARDING_COMPONENT_REQUESTS_YN) != null) {
			if (questionnaireData.get(AWARDING_COMPONENT_REQUESTS_YN).equals("Y")) {
				
				if (questionnaireData.get(AWARDING_COMPONENT_REQUESTS + ".1") != null) {
					phsAssignmentRequestFormType.setAssignToAwardingComponent1(
						(String) questionnaireData.get(AWARDING_COMPONENT_REQUESTS + ".1")); 
				}
				
				if (questionnaireData.get(AWARDING_COMPONENT_REQUESTS + ".2") != null) {
					phsAssignmentRequestFormType.setAssignToAwardingComponent2(
						(String) questionnaireData.get(AWARDING_COMPONENT_REQUESTS + ".2")); 
				}
				
				if (questionnaireData.get(AWARDING_COMPONENT_REQUESTS + ".3") != null) {
					phsAssignmentRequestFormType.setAssignToAwardingComponent3(
						(String) questionnaireData.get(AWARDING_COMPONENT_REQUESTS + ".3")); 
				}

			}
		}
  
    	/* Do not assign awarding components */
		if (questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS_YN) != null) {
			if (questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS_YN).equals("Y")) {
				
				if (questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS + ".1") != null) {
					phsAssignmentRequestFormType.setNotAssignToAwardingComponent1(
						(String) questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS + ".1")); 
				}
				
				if (questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS + ".2") != null) {
					phsAssignmentRequestFormType.setNotAssignToAwardingComponent2(
						(String) questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS + ".2")); 
				}
				
				if (questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS + ".3") != null) {
					phsAssignmentRequestFormType.setNotAssignToAwardingComponent3(
						(String) questionnaireData.get(DO_NOT_ASSIGN_AWARDING_COMPONENTS + ".3")); 
				}

			}
		}
        	
    	/* Study Section Assignments */
		if (questionnaireData.get(STUDY_SECTION_REQUESTS_YN) != null) {
			if (questionnaireData.get(STUDY_SECTION_REQUESTS_YN).equals("Y")) {
				
				if (questionnaireData.get(STUDY_SECTION_REQUESTS + ".1") != null) {
					phsAssignmentRequestFormType.setAssignToStudySection1(
						(String) questionnaireData.get(STUDY_SECTION_REQUESTS + ".1")); 
				}
				
				if (questionnaireData.get(STUDY_SECTION_REQUESTS + ".2") != null) {
					phsAssignmentRequestFormType.setAssignToStudySection2(
						(String) questionnaireData.get(STUDY_SECTION_REQUESTS + ".2")); 
				}
				
				if (questionnaireData.get(STUDY_SECTION_REQUESTS + ".3") != null) {
					phsAssignmentRequestFormType.setAssignToStudySection3(
						(String) questionnaireData.get(STUDY_SECTION_REQUESTS + ".3")); 
				}

			}
		}
    	
    	/* Do not assign study sections */
		if (questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS_YN) != null) {
			if (questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS_YN).equals("Y")) {
				
				if (questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS + ".1") != null) {
					phsAssignmentRequestFormType.setNotAssignToStudySection1(
						(String) questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS + ".1")); 
				}
				
				if (questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS + ".2") != null) {
					phsAssignmentRequestFormType.setNotAssignToStudySection2(
						(String) questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS + ".2")); 
				}
				
				if (questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS + ".3") != null) {
					phsAssignmentRequestFormType.setNotAssignToStudySection3(
						(String) questionnaireData.get(DO_NOT_ASSIGN_STUDY_SECTIONS + ".3")); 
				}

			}
		}
    	
    	/* Do not review individuals */
		if (questionnaireData.get(DO_NOT_REVIEW_INDIVIDUALS_YN) != null) {
			if (questionnaireData.get(DO_NOT_REVIEW_INDIVIDUALS_YN).equals("Y")) {
				
				if (questionnaireData.get(DO_NOT_REVIEW_INDIVIDUALS) != null) {
					phsAssignmentRequestFormType.setIndividualsNotToReviewText(
						(String) questionnaireData.get(DO_NOT_REVIEW_INDIVIDUALS)); 
				}
			}
		}
    	
    	/* Areas of expertise */
		if (questionnaireData.get(EXPERTISE_AREAS_YN) != null) {
			if (questionnaireData.get(EXPERTISE_AREAS_YN).equals("Y")) {
				
				if (questionnaireData.get(EXPERTISE_AREAS + ".1") != null) {
					phsAssignmentRequestFormType.setExpertise1(
						(String) questionnaireData.get(EXPERTISE_AREAS + ".1")); 
				}
				
				if (questionnaireData.get(EXPERTISE_AREAS + ".2") != null) {
					phsAssignmentRequestFormType.setExpertise2(
						(String) questionnaireData.get(EXPERTISE_AREAS + ".2")); 
				}
				
				if (questionnaireData.get(EXPERTISE_AREAS + ".3") != null) {
					phsAssignmentRequestFormType.setExpertise3(
						(String) questionnaireData.get(EXPERTISE_AREAS + ".3")); 
				}
				
				if (questionnaireData.get(EXPERTISE_AREAS + ".4") != null) {
					phsAssignmentRequestFormType.setExpertise4(
						(String) questionnaireData.get(EXPERTISE_AREAS + ".4")); 
				}
				
				if (questionnaireData.get(EXPERTISE_AREAS + ".5") != null) {
					phsAssignmentRequestFormType.setExpertise5(
						(String) questionnaireData.get(EXPERTISE_AREAS + ".5")); 
				}
			}
		} 
  
    	return phsAssignmentRequestFormType;
    }
    
    /* Gets questionnaire answers from the database */
    private OpportunityInfoBean getFundingOpportunity() throws CoeusException, DBException {
    	OpportunityInfoBean oppBean = new OpportunityInfoBean();
         
    	PHSAssignmentRequestFormTxnBean_V1_0 txnBean = new PHSAssignmentRequestFormTxnBean_V1_0();
    	oppBean = txnBean.getFundingOpportunity(propNumber);

    	return oppBean;
    }
  
    /* Gets questionnaire answers from the database */
    private HashMap getQuestionnaireAnswers() throws CoeusException, DBException {
    	CoeusVector cvAnswers = new CoeusVector();
        
    	phsAssignmentRequestFormTxnBean = new PHSAssignmentRequestFormTxnBean_V1_0();
    	cvAnswers = phsAssignmentRequestFormTxnBean.getQuestionnaireAnswers(propNumber);
    	return processQuestionnaireAnswers(cvAnswers);
    }
    
    /* Take questionnaire answers and put in CoeusVector for easy access by question ID and answer number */
    private HashMap processQuestionnaireAnswers(CoeusVector dbAnswers) {
    	// Extract the questionnaire answers from the returned data
    	QuestionAnswerBean bean = new QuestionAnswerBean();
    	HashMap<String, String> map = new HashMap<String, String>();
	   
    	if (dbAnswers.size() > 0) {
    		for (int v=0; v < dbAnswers.size(); v++) {
    			bean = (QuestionAnswerBean) dbAnswers.get(v);
    			map.put(bean.getQuestionId() + "." + bean.getAnswerNumber(),bean.getAnswer());
    		}
    	}
    	return map;
    }
    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHSAssignmentRequestForm();
    }
    
}
