package edu.vanderbilt.coeus.s2s.generator;

import gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.*;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.vanderbilt.coeus.s2s.bean.S2SFormsETxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.s2s.generator.S2SBaseStream;

import gov.grants.apply.forms.humansubjectstudy_v1.*;


public class PHSHumanSubjectsAndClinicalTrialsInfo_V1_0 extends S2SBaseStream {

	private gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.ObjectFactory objFactory;
	private gov.grants.apply.forms.humansubjectstudy_v1.ObjectFactory humanSubjectStudyObjFactory;
	private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
	private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
	
    private String proposalNumber;
	private HashMap questionnaireData;
	private HashMap<Integer, String> instanceData;
	private String humanSpecimens;

    public static final int EXPLANATION = 215;
    public static final int HUMAN_SPECIMENS = 615;
    private static final String NO = "N: No";
    private static final String YES = "Y: Yes";
    private static final String NA = "NA: Not Applicable";
    private static final String VERSION = "1.0";
    private static final String HSS_VERSION = "1.0";
    
    private S2SFormsETxnBean s2sFormsETxnBean;
    private ProposalDevelopmentTxnBean proposalDevTxnBean;
    private ProposalDevelopmentFormBean proposalBean;
    private S2STxnBean s2sTxnBean;
    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType, vaAttachedFileType;
    
    public PHSHumanSubjectsAndClinicalTrialsInfo_V1_0() {
	    objFactory = new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.ObjectFactory();
	    humanSubjectStudyObjFactory = new gov.grants.apply.forms.humansubjectstudy_v1.ObjectFactory();
	    attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
	    globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
	    s2sTxnBean = new S2STxnBean();
		s2sFormsETxnBean = new S2SFormsETxnBean();
		proposalDevTxnBean = new ProposalDevelopmentTxnBean();
		proposalBean = new ProposalDevelopmentFormBean();
    }
    
    private PHSHumanSubjectsAndClinicalTrialsInfoType getHumanSubjectsAndClinicalTrialsInfo() 
    		throws CoeusXMLException, CoeusException, DBException, JAXBException {
    	
    	PHSHumanSubjectsAndClinicalTrialsInfoType phsHumanSubjectsAndClinicalTrialsInfoType = 
    			objFactory.createPHSHumanSubjectsAndClinicalTrialsInfo();
    	
    	gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup = 
    			attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
		
    	// Get questionnaire questions
    	s2sFormsETxnBean = new S2SFormsETxnBean();
        questionnaireData = processQuestionnaireAnswers();
        
    	phsHumanSubjectsAndClinicalTrialsInfoType.setFormVersion(VERSION);
	        
        // Get special review information
		proposalBean = proposalDevTxnBean.getProposalDevelopmentDetails(proposalNumber);
		Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
		
		// Human subjects
		phsHumanSubjectsAndClinicalTrialsInfoType.setHumanSubjectsIndicator(NO);
  
		// Exemptions
		String description;
		int approvalCode = 0;
		if (vecSpecialReview != null) {
			for (int vecCount = 0; vecCount < vecSpecialReview.size(); vecCount++) {
				ProposalSpecialReviewFormBean specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);
				HashMap hmAppCode;
				if (specialReviewBean.getSpecialReviewCode() == 1) {
					phsHumanSubjectsAndClinicalTrialsInfoType.setHumanSubjectsIndicator(YES);
						
		            PHSHumanSubjectsAndClinicalTrialsInfoType.ExemptionNumbersType exemptionTypes = 
		            		objFactory.createPHSHumanSubjectsAndClinicalTrialsInfoTypeExemptionNumbersType();
		            description = specialReviewBean.getComments();
		            
		            phsHumanSubjectsAndClinicalTrialsInfoType.setExemptFedReg(NO);
		            
		            hmAppCode = new HashMap();
		            hmAppCode = s2sTxnBean.get_specRev_app_code(proposalNumber, 1, specialReviewBean.getApprovalCode());
		            
		            approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
		            if (approvalCode == 4) {
		            	String exemptionNumbers = null;
		            	HashMap hmExempt = new HashMap();
		            	Vector vExempt = s2sTxnBean.getExemptionNumbers(proposalNumber);
		            	int listSize = vExempt.size();
		                
	            		if (listSize > 0) {
	            			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
	            				hmExempt = (HashMap)vExempt.elementAt(rowIndex);
	            				exemptionNumbers = hmExempt.get("EXEMPTION_NUMBERS").toString();
	            				exemptionTypes.getExemptionNumber().add(exemptionNumbers);
	            			}
	            			phsHumanSubjectsAndClinicalTrialsInfoType.setExemptionNumbers(exemptionTypes);
		            	}
		            	else if (description != null) {
		            		String[] exemptions = description.split(",");
		                
		            		for (int i = 0; i < exemptions.length; i++) {
		            			String exemptionNumber = exemptions[i].trim();
		            			exemptionTypes.getExemptionNumber().add(exemptionNumber);
		            		}
		            		phsHumanSubjectsAndClinicalTrialsInfoType.setExemptionNumbers(exemptionTypes);
		            	}
	            		phsHumanSubjectsAndClinicalTrialsInfoType.setExemptFedReg(YES);
	            	}
				}
			}
		}
        	
		// Human specimens
    	if (questionnaireData.get(HUMAN_SPECIMENS) != null) {
    		humanSpecimens = questionnaireData.get(HUMAN_SPECIMENS).equals("Y") ? YES : NO;
    		phsHumanSubjectsAndClinicalTrialsInfoType.setInvolveHumanSpecimens(humanSpecimens); 
    	}

    	// Narrative Attachments - START
    	int narrativeType;
    	int moduleNum;
        Attachment attachment = null;
    	ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
    	ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean;
                  
    	Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(proposalNumber);
     
    	S2STxnBean s2sTxnBean = new S2STxnBean();
    	LinkedHashMap hmArg = new LinkedHashMap();
    	HashMap hmNarrative = new HashMap();
         
    	int size=vctNarrative==null?0:vctNarrative.size();
    	for (int row=0; row < size;row++) {
    		proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                       
    		moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   

    		hmNarrative = new HashMap();
    		hmNarrative = s2sTxnBean.getNarrativeInfo(proposalNumber,moduleNum);
    		narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
    		description = hmNarrative.get("DESCRIPTION").toString();
  
    		hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
    		hmArg.put(ContentIdConstants.DESCRIPTION, description);
           
    		attachment = getAttachment(hmArg);
               
    		if (narrativeType == EXPLANATION) { // Explanation of why application does not involve human subjects research
    			if (attachment == null) {
    				proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                 
    				Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                if (narrativeAttachment.getContent() != null) {
	                	attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                	PHSHumanSubjectsAndClinicalTrialsInfoType.ExplanationType explanationType = 
	                			objFactory.createPHSHumanSubjectsAndClinicalTrialsInfoTypeExplanationType();
	                	explanationType.setAttFile(attachedFileType);
	                	phsHumanSubjectsAndClinicalTrialsInfoType.setExplanation(explanationType);                          
                 	}
    			}                  
    		} 
    	}
       	// Narrative Attachments - END
    	
    	return phsHumanSubjectsAndClinicalTrialsInfoType;
    }
    
    /* Gets questionnaire answers from the database */
    private CoeusVector getQuestionnaireAnswers() throws CoeusException, DBException {
    	CoeusVector cvAnswers = new CoeusVector();
    	cvAnswers = s2sFormsETxnBean.getQuestionnaireAnswers(proposalNumber);
    	return cvAnswers;
    }
    
    /* Take questionnaire answers and put in CoeusVector for easy access by question ID */
    private HashMap processQuestionnaireAnswers() {
    	CoeusVector cvForm = new CoeusVector();
	   
    	try {
    		cvForm = getQuestionnaireAnswers();
    	} catch (CoeusException e) {
    		UtilFactory.log("CoeusException:  Unable to retrieve questionnaire answers for PHS Human Subjects and Clinical Trials Info V1-0.");
    		e.printStackTrace();
    	} catch (DBException e) {
    		UtilFactory.log("DBException:  Unable to retrieve questionnaire answers for PHS Human Subjects and Clinical Trials Info V1-0.");
    		e.printStackTrace();
    	}

    	// Extract the questionnaire answers from the returned data
    	QuestionAnswerBean bean = new QuestionAnswerBean();
    	HashMap<Integer, String> map = new HashMap<Integer, String>();
	   
    	if (cvForm.size() > 0) {
    		for (int v=0; v < cvForm.size(); v++) {
    			bean = (QuestionAnswerBean) cvForm.get(v);
    			map.put(bean.getQuestionId(),bean.getAnswer());
    		}
    	}
    	return map;
    }

    /* Return questionnaire answer or 0 given a question ID */
    private Integer getAnswer(int question_id) {
    	Integer answer = 0;
    	
    	if (instanceData.containsKey(question_id)) {
    		answer = Integer.parseInt(instanceData.get(question_id));
    	}
    	
    	return answer;
    }
    
    private Calendar getCal(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
	@Override
	public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException {
        this.proposalNumber = (String) ht.get("PROPOSAL_NUMBER");
        return getHumanSubjectsAndClinicalTrialsInfo();
	}
    
}
