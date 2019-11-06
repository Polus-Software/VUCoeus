package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.rr_otherprojectinfo_1_4_v1.*;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType;
import gov.grants.apply.system.global_v1.HashValueType;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import edu.vanderbilt.coeus.s2s.bean.S2SFormsETxnBean;

public class RR_OtherProjectInfoStream_V1_4 extends S2SBaseStream {
	private gov.grants.apply.forms.rr_otherprojectinfo_1_4_v1.ObjectFactory objFactory;
	private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
	private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
	private CoeusXMLGenrator xmlGenerator;
	private String propNumber;
	private ProposalDevelopmentFormBean proposalBean;
	private ProposalDevelopmentTxnBean proposalDevTxnBean;
	private OrganizationMaintenanceFormBean orgBean;
	private OrganizationMaintenanceDataTxnBean orgTxnBean;
	private S2SFormsETxnBean s2sFormsETxnBean;
	private S2STxnBean s2sTxnBean;
	private String organizationID;
	AttachedFileDataType attachedFileType;
	
	private HashMap questionnaireData;
	private HashMap<Integer, String> instanceData;
	
	// questionnaire questions
    public static final int PROPRIETARY = 122;
    public static final int ENVIRONMENTAL = 123;
    public static final int ENVIRONMENTAL_EXPLANATION = 136;
    public static final int ENVIRONMENTAL_EA = 124;
    public static final int ENVIRONMENTAL_EA_EXPLANATION = 137;
    public static final int HISTORIC = 125;
    public static final int HISTORIC_EXPLANATION = 135;
    public static final int INTERNATIONAL = 126;
    public static final int INTERNATIONAL_COUNTRIES = 127;
    public static final int INTERNATIONAL_EXPLANATION = 138;
    
    // narratives
    public static final int PROJECT_SUMMARY = 5;
    public static final int PROJECT_NARRATIVE = 1;
    public static final int BIBLIOGRAPHY = 4;
    public static final int FACILITIES = 2;
    public static final int EQUIPMENT = 3;
    public static final int OTHER = 8;
    
    // questionnaire variables
	private String proprietaryInfo, environmentalImpact, environmentalImpactExplanation, environmentalImpactEA, environmentalImpactEAExplanation,
		historicSite, historicSiteExplanation, internationalCollaboration, internationalCollaborationCountries, internationalCollaborationExplanation = null;
    
	// Instantiate a new instance
	public RR_OtherProjectInfoStream_V1_4()   {
	    objFactory = new gov.grants.apply.forms.rr_otherprojectinfo_1_4_v1.ObjectFactory();
	    attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
	    globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
	    
	    proposalDevTxnBean = new ProposalDevelopmentTxnBean();
	    orgTxnBean = new OrganizationMaintenanceDataTxnBean();
	    s2sTxnBean = new S2STxnBean();
	    xmlGenerator = new CoeusXMLGenrator();
	}
  

	private RROtherProjectInfo14Type getRROtherProjectInfo() throws CoeusXMLException, CoeusException, DBException, JAXBException {
		RROtherProjectInfo14Type rrOtherInfo = objFactory.createRROtherProjectInfo14();
		Attachment attachment = null;
    
		try {
			proposalBean = proposalDevTxnBean.getProposalDevelopmentDetails(propNumber);
 			orgBean = getOrgData();
 			
 			rrOtherInfo.setFormVersion("1.4");

 			Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
			
			int approvalCode = 0;
			String protocolNumber = null;
			  
			rrOtherInfo.setHumanSubjectsIndicator("N: No");
			rrOtherInfo.setVertebrateAnimalsIndicator("N: No");
			
	        questionnaireData = processQuestionnaireAnswers();
      
			String description;
			if (vecSpecialReview != null) {
				for (int vecCount = 0; vecCount < vecSpecialReview.size(); vecCount++) {
					ProposalSpecialReviewFormBean specialReviewBean = (ProposalSpecialReviewFormBean)vecSpecialReview.get(vecCount);
					HashMap hmAppCode;
					switch (specialReviewBean.getSpecialReviewCode()) {
						case 1: 
							rrOtherInfo.setHumanSubjectsIndicator("Y: Yes");
				            RROtherProjectInfo14Type.HumanSubjectsSupplementType humSubjectsSuppType = objFactory.createRROtherProjectInfo14TypeHumanSubjectsSupplementType();
				            RROtherProjectInfo14Type.HumanSubjectsSupplementType.ExemptionNumbersType humSubjectsSuppExemptType = objFactory.createRROtherProjectInfo14TypeHumanSubjectsSupplementTypeExemptionNumbersType();
				            description = specialReviewBean.getComments();
            
				            humSubjectsSuppType.setExemptFedReg("N: No");
				            
				            hmAppCode = new HashMap();
				            hmAppCode = s2sTxnBean.get_specRev_app_code(propNumber, 1, specialReviewBean.getApprovalCode());
				            
				            approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
				            String linkEnabled = hmAppCode.get("LINK_ENABLED").toString();
				            if ((linkEnabled.equals("1")) && (hmAppCode.get("PROTOCOL_NUMBER") != null)) {
				              protocolNumber = hmAppCode.get("PROTOCOL_NUMBER").toString();
				            }
            
				            if (approvalCode == 4) {
				            	if (linkEnabled.equals("1")) {
				            		String exemptionNumbers = null;
				            		HashMap hmExempt = new HashMap();
				            		Vector vExempt = s2sTxnBean.getExemptionNumbers(protocolNumber);
				            		int listSize = vExempt.size();
				                
				            		if (listSize > 0) {
				            			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				            				hmExempt = (HashMap)vExempt.elementAt(rowIndex);
				            				exemptionNumbers = hmExempt.get("EXEMPTION_NUMBERS").toString();
				            				humSubjectsSuppExemptType.getExemptionNumber().add(exemptionNumbers);
				            			}
				                  
				            			humSubjectsSuppType.setExemptionNumbers(humSubjectsSuppExemptType);
				            		}
				            	}
				            	else if (description != null) {
				            		String[] exemptions = description.split(",");
				                
				            		for (int i = 0; i < exemptions.length; i++) {
				            			String exemptionNumber = exemptions[i].trim();
				            			humSubjectsSuppExemptType.getExemptionNumber().add(exemptionNumber);
				            		}
				            		humSubjectsSuppType.setExemptionNumbers(humSubjectsSuppExemptType);
				            	}
				
				            	humSubjectsSuppType.setExemptFedReg("Y: Yes");
			            	}
				            
				            if (approvalCode == 1) {
				            	humSubjectsSuppType.setHumanSubjectIRBReviewIndicator("Y: Yes");
				            } 
				            else {
				            	humSubjectsSuppType.setHumanSubjectIRBReviewIndicator("N: No");
				              
				            	if (specialReviewBean.getApprovalDate() != null) {
				            		humSubjectsSuppType.setHumanSubjectIRBReviewDate(getCal(specialReviewBean.getApprovalDate()));
				            	}
				            }
				            
				            if (orgBean.getHumanSubAssurance() != null) {
				            	humSubjectsSuppType.setHumanSubjectAssuranceNumber(orgBean.getHumanSubAssurance().substring(3));
				            }
				            if (humSubjectsSuppType != null) {
				            	rrOtherInfo.setHumanSubjectsSupplement(humSubjectsSuppType);
				            }
				            
				            break;
						case 2: 
							rrOtherInfo.setVertebrateAnimalsIndicator("Y: Yes");
				            RROtherProjectInfo14Type.VertebrateAnimalsSupplementType vertAnimalSuppType = objFactory.createRROtherProjectInfo14TypeVertebrateAnimalsSupplementType();
				            
				            hmAppCode = new HashMap();
				            hmAppCode = s2sTxnBean.get_specRev_app_code(propNumber, 2, specialReviewBean.getApprovalCode());
				            approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
				            
				            if (approvalCode == 1) {
				              vertAnimalSuppType.setVertebrateAnimalsIACUCReviewIndicator("Y: Yes");
				            } 
				            else {
				            	vertAnimalSuppType.setVertebrateAnimalsIACUCReviewIndicator("N: No");
				              
				            	if (specialReviewBean.getApprovalDate() != null) {
				            		vertAnimalSuppType.setVertebrateAnimalsIACUCApprovalDateReviewDate(getCal(specialReviewBean.getApprovalDate()));
				            	}
				            }
				            
				            if (orgBean.getAnimalWelfareAssurance() != null) {
				            	vertAnimalSuppType.setAssuranceNumber(orgBean.getAnimalWelfareAssurance());
				            }
				            
				            if (vertAnimalSuppType != null) {
				            	rrOtherInfo.setVertebrateAnimalsSupplement(vertAnimalSuppType);
				            }

				            break;
					}
				}
			}

		    // Proprietary information
			if (questionnaireData.get(PROPRIETARY) != null) {
				proprietaryInfo = questionnaireData.get(PROPRIETARY).equals("Y") ? "Y: Yes" : "N: No";
			}
			rrOtherInfo.setProprietaryInformationIndicator(proprietaryInfo); 
	      
		    // Environmental impact
			RROtherProjectInfo14Type.EnvironmentalImpactType environmentalImpactType = objFactory.createRROtherProjectInfo14TypeEnvironmentalImpactType();
			if (questionnaireData.get(ENVIRONMENTAL) != null) {
				environmentalImpact = questionnaireData.get(ENVIRONMENTAL).equals("Y") ? "Y: Yes" : "N: No";
				environmentalImpactType.setEnvironmentalImpactIndicator(environmentalImpact);
				
				if (questionnaireData.get(ENVIRONMENTAL).equals("Y")) {
					environmentalImpactExplanation = (String) questionnaireData.get(ENVIRONMENTAL_EXPLANATION);
					environmentalImpactType.setEnvironmentalImpactExplanation(environmentalImpactExplanation);
					
					RROtherProjectInfo14Type.EnvironmentalImpactType.EnvironmentalExemptionType environmentalExemptionType = 
							objFactory.createRROtherProjectInfo14TypeEnvironmentalImpactTypeEnvironmentalExemptionType();
					environmentalImpactEA = questionnaireData.get(ENVIRONMENTAL_EA).equals("Y") ? "Y: Yes" : "N: No";
					environmentalExemptionType.setEnvironmentalExemptionIndicator(environmentalImpactEA);
					
					if (questionnaireData.get(ENVIRONMENTAL_EA).equals("Y")) {
						environmentalImpactEAExplanation = (String) questionnaireData.get(ENVIRONMENTAL_EA_EXPLANATION);
						environmentalExemptionType.setEnvironmentalExemptionExplanation(environmentalImpactEAExplanation);
					}
					
					environmentalImpactType.setEnvironmentalExemption(environmentalExemptionType);
	
				}
			}
			rrOtherInfo.setEnvironmentalImpact(environmentalImpactType);
	
		    // Historic site
			if (questionnaireData.get(HISTORIC) != null) {
				historicSite = questionnaireData.get(HISTORIC).equals("Y") ? "Y: Yes" : "N: No";
				
				if (questionnaireData.get(HISTORIC).equals("Y")) {
					historicSiteExplanation = (String) questionnaireData.get(HISTORIC_EXPLANATION);
					rrOtherInfo.setHistoricDesignationExplanation(historicSiteExplanation);
				}
			}
			rrOtherInfo.setHistoricDesignation(historicSite);
		
			// International collaboration
			RROtherProjectInfo14Type.InternationalActivitiesType internationalActivitiesType = 
					objFactory.createRROtherProjectInfo14TypeInternationalActivitiesType();
			if (questionnaireData.get(INTERNATIONAL) != null) {
				internationalCollaboration = questionnaireData.get(INTERNATIONAL).equals("Y") ? "Y: Yes" : "N: No";
				internationalActivitiesType.setInternationalActivitiesIndicator(internationalCollaboration);
				
				if (questionnaireData.get(INTERNATIONAL).equals("Y")) {
					internationalCollaborationCountries = (String) questionnaireData.get(INTERNATIONAL_COUNTRIES);
					internationalActivitiesType.setActivitiesPartnershipsCountries(internationalCollaborationCountries);
	
					internationalCollaborationExplanation = (String) questionnaireData.get(INTERNATIONAL_EXPLANATION);
					internationalActivitiesType.setInternationalActivitiesExplanation(internationalCollaborationExplanation);
	
				}
			}
			rrOtherInfo.setInternationalActivities(internationalActivitiesType);

			// Narratives
			ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
			RROtherProjectInfo14Type.OtherAttachmentsType rrOtherAttachments = objFactory.createRROtherProjectInfo14TypeOtherAttachmentsType();
			Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
			s2sTxnBean = new S2STxnBean();
			LinkedHashMap hmArg = new LinkedHashMap();
	      
			HashMap hmNarrative = new HashMap();
			boolean otherAttachments = false;
		      
			int size = vctNarrative == null ? 0 : vctNarrative.size();
			for (int row = 0; row < size; row++) {
				ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)vctNarrative.elementAt(row);
				int moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
				String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
	
				hmNarrative = new HashMap();
				hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber, moduleNum);
				int narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
				description = hmNarrative.get("DESCRIPTION").toString();
	 
				hmArg.put("M", Integer.toString(moduleNum));
				hmArg.put("DESCRIPTION", description);
	        
				attachment = getAttachment(hmArg);
    
				if (narrativeType == PROJECT_SUMMARY) {
					if (attachment == null) {
						proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
						Attachment abstractAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
						if (abstractAttachment.getContent() != null) {
							RROtherProjectInfo14Type.AbstractAttachmentsType rrAbstract = objFactory.createRROtherProjectInfo14TypeAbstractAttachmentsType();
							attachedFileType = getAttachedFileType(abstractAttachment);
							rrAbstract.setAbstractAttachment(attachedFileType);
							rrOtherInfo.setAbstractAttachments(rrAbstract);
						}
					}
				}
				else if (narrativeType == PROJECT_NARRATIVE) {
					if (attachment == null) {
						proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
						Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
						if (narrativeAttachment.getContent() != null) {
							RROtherProjectInfo14Type.ProjectNarrativeAttachmentsType rrProjNarrative = objFactory.createRROtherProjectInfo14TypeProjectNarrativeAttachmentsType();
							attachedFileType = getAttachedFileType(narrativeAttachment);
							rrProjNarrative.setProjectNarrativeAttachment(attachedFileType);
							rrOtherInfo.setProjectNarrativeAttachments(rrProjNarrative);
						}
		          	}
		        }
		        else if (narrativeType == FACILITIES) {
		        	if (attachment == null) {
		        		proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
		        		Attachment facilitiesAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		        		if (facilitiesAttachment.getContent() != null) {
		        			RROtherProjectInfo14Type.FacilitiesAttachmentsType rrFacilities = objFactory.createRROtherProjectInfo14TypeFacilitiesAttachmentsType();
		        			attachedFileType = getAttachedFileType(facilitiesAttachment);
		        			rrFacilities.setFacilitiesAttachment(attachedFileType);
		        			rrOtherInfo.setFacilitiesAttachments(rrFacilities);
		        		}
		        	}
		        }
		        else if (narrativeType == EQUIPMENT) {
		        	if (attachment == null) {
		        		proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
		        		Attachment equipmentAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		        		if (equipmentAttachment.getContent() != null) {
		        			RROtherProjectInfo14Type.EquipmentAttachmentsType rrEquipment = objFactory.createRROtherProjectInfo14TypeEquipmentAttachmentsType();
		        			attachedFileType = getAttachedFileType(equipmentAttachment);
		        			rrEquipment.setEquipmentAttachment(attachedFileType);
		        			rrOtherInfo.setEquipmentAttachments(rrEquipment);
		        		}
		        	}
		        }
		        else if (narrativeType == BIBLIOGRAPHY) {
		        	if (attachment == null) {
		        		proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
		        		Attachment biblioAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		        		if (biblioAttachment.getContent() != null) {
		        			RROtherProjectInfo14Type.BibliographyAttachmentsType rrBibliography = objFactory.createRROtherProjectInfo14TypeBibliographyAttachmentsType();
		        			attachedFileType = getAttachedFileType(biblioAttachment);
		        			rrBibliography.setBibliographyAttachment(attachedFileType);
		        			rrOtherInfo.setBibliographyAttachments(rrBibliography);
		        		}
		        	}
		        }
		        else if (narrativeType == OTHER) {
		        	if (attachment == null) {
		        		proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
		        		Attachment otherAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		        		if (otherAttachment.getContent() != null) {
		        			otherAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
		        			attachedFileType = getAttachedFileType(otherAttachment);
		        			rrOtherAttachments.getOtherAttachment().add(attachedFileType);
		        			otherAttachments = true;
		    			}
		  			}
				}
			}
			
			if (otherAttachments) { 
				rrOtherInfo.setOtherAttachments(rrOtherAttachments);
      		}
			
      		return rrOtherInfo;
		}
		catch (JAXBException jaxbEx) {
			UtilFactory.log(jaxbEx.getMessage(), jaxbEx, "RROtherProjectInfoStreamV12", "getRROtherProjectInfo()");
			throw new CoeusXMLException(jaxbEx.getMessage());
		}
	}
  

	// Get organization data for human subject assurance number
	private OrganizationMaintenanceFormBean getOrgData() throws CoeusXMLException, CoeusException, DBException {
		HashMap hmOrg = s2sTxnBean.getOrganizationID(propNumber, "O");
		if ((hmOrg != null) && (hmOrg.get("ORGANIZATION_ID") != null)) {
			organizationID = hmOrg.get("ORGANIZATION_ID").toString();
		}
		return orgTxnBean.getOrganizationMaintenanceDetails(organizationID);
	}
  
	private AttachedFileDataType getAttachedFileType(Attachment attachment) throws JAXBException {
		AttachedFileDataType attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
		AttachedFileDataType.FileLocationType fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
		HashValueType hashValueType = globalObjFactory.createHashValueType();

		fileLocation.setHref(attachment.getContentId());
		attachedFileType.setFileLocation(fileLocation);
		attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
		attachedFileType.setMimeType("application/octet-stream");
		try {
			attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			UtilFactory.log(ex.getMessage(), ex, "RR_OtherProjectInfoStream_V1_4", "getAttachedFile");
			throw new JAXBException(ex);
		}

		return attachedFileType;
	}

  	private Calendar getCal(Date date) {
  		return DateUtils.getCal(date);
  	}
  
  	public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException {
  		propNumber = ((String)ht.get("PROPOSAL_NUMBER"));
  		return getRROtherProjectInfo();
  	}
  	
  	
    /* Gets questionnaire answers from the database */
    private CoeusVector getQuestionnaireAnswers() throws CoeusException, DBException {
    	CoeusVector cvAnswers = new CoeusVector();
        
    	s2sFormsETxnBean = new S2SFormsETxnBean();
    	cvAnswers = s2sFormsETxnBean.getQuestionnaireAnswers(propNumber);

    	return cvAnswers;
    }
    
    /* Take questionnaire answers and put in CoeusVector for easy access by question ID */
    private HashMap processQuestionnaireAnswers() {
    	CoeusVector cvForm = new CoeusVector();
	   
    	try {
    		cvForm = getQuestionnaireAnswers();
    	} catch (CoeusException e) {
    		UtilFactory.log("CoeusException:  Unable to retrieve questionnaire answers for R&R Other Project Info V1-4.");
    		e.printStackTrace();
    	} catch (DBException e) {
    		UtilFactory.log("DBException:  Unable to retrieve questionnaire answers for R&R Other Project Info V1-4.");
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
  	
}