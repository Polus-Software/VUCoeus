/*
 * PHS398ResearchPlanV1_1Stream.java
 *
 * Created on June 26, 2006, 2:26 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.PHS398ResearchPlanTxnBean;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.phs398_researchplan_v1_1.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  jenlu 
 *
 */

 public class PHS398ResearchPlanV1_1Stream extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs398_researchplan_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    //txn bean
    private PHS398ResearchPlanTxnBean phs398ResearchPlanTxnBean;
     
    private String propNumber;
    private UtilFactory utilFactory;
   
  
    /** Creates a new instance of PHS398ResearchPlanStream */
    public PHS398ResearchPlanV1_1Stream(){
        objFactory = new gov.grants.apply.forms.phs398_researchplan_v1_1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
      
        xmlGenerator = new CoeusXMLGenrator();  
           
    }    
    private PHS398ResearchPlanType getPHS398ResearchPlanV1_1()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
                    
        PHS398ResearchPlanType phs398ResearchPlanType = objFactory.createPHS398ResearchPlan();  
        PHS398ResearchPlanType.ResearchPlanAttachmentsType researchPlanAttachments = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsType();
        OtherResearchPlanSectionsType otherResearchPlanSections =
                objFactory.createOtherResearchPlanSectionsType();
        HumanSubjectSectionType humanSubjectSection = objFactory.createHumanSubjectSectionType();
     
        PHS398ResearchPlanType.ApplicationTypeType applicationType = objFactory.createPHS398ResearchPlanTypeApplicationTypeType();

        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        Attachment attachment = null;
       
        try{
          /**
            *FormVersion
          */ 
          phs398ResearchPlanType.setFormVersion("1.1");
          
          //applicationType 
          HashMap hmInfo = new HashMap();
          phs398ResearchPlanTxnBean = new PHS398ResearchPlanTxnBean();
          hmInfo = phs398ResearchPlanTxnBean.getApplicationType(propNumber);
          if (hmInfo.get("APPLICATIONTYPE") != null){
            applicationType.setTypeOfApplication(hmInfo.get("APPLICATIONTYPE").toString());
            phs398ResearchPlanType.setApplicationType(applicationType);
          }
          
          String description;
          int narrativeType;
          int moduleNum;
          ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
          ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
                      
          Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
       
         
           S2STxnBean s2sTxnBean = new S2STxnBean();
           LinkedHashMap hmArg = new LinkedHashMap();
                     
           HashMap hmNarrative = new HashMap();
             
           int size=vctNarrative==null?0:vctNarrative.size();
           for (int row=0; row < size;row++) {
               proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
               moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   
  
//               String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               description = hmNarrative.get("DESCRIPTION").toString();
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
               hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
               attachment = getAttachment(hmArg);
                   
               if (narrativeType == 20) {//IntroductionToApplication
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeIntroductionToApplicationType();
                        introductionToApplication.setAttFile(attachedFileType);
                        researchPlanAttachments.setIntroductionToApplication(introductionToApplication);                           
                     }
                  }                  
               } 
               
               if (narrativeType == 21) {//SpecificAims
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                    
                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.SpecificAimsType
                            specificAims = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeSpecificAimsType();
                        specificAims.setAttFile(attachedFileType);
                        researchPlanAttachments.setSpecificAims(specificAims);                          
                     }
                  }                  
               } 
               
               if (narrativeType == 22) {//BackgroundSignificance
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.BackgroundSignificanceType
                            backgroundSignificance = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeBackgroundSignificanceType();
                        backgroundSignificance.setAttFile(attachedFileType);
                        researchPlanAttachments.setBackgroundSignificance(backgroundSignificance);                             
                     }
                  }                  
               } 
               if (narrativeType == 23) {//ProgressReport
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.ProgressReportType
                            progressReport = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeProgressReportType();
                        progressReport.setAttFile(attachedFileType);
                        researchPlanAttachments.setProgressReport(progressReport);                            
                     }
                  }                  
               }
               if (narrativeType == 24) {//ResearchDesignMethods
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.ResearchDesignMethodsType
                            researchDesignMethods = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeResearchDesignMethodsType();
                        researchDesignMethods.setAttFile(attachedFileType);
                        researchPlanAttachments.setResearchDesignMethods(researchDesignMethods);                              
                     }
                  }                  
               }
               if (narrativeType == 43) {//InclusionEnrollmentReport
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.InclusionEnrollmentReportType
                            inclusionEnrollmentReport = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeInclusionEnrollmentReportType();
                        inclusionEnrollmentReport.setAttFile(attachedFileType);
                        researchPlanAttachments.setInclusionEnrollmentReport(inclusionEnrollmentReport);                              
                     }
                  }                  
               }
               if (narrativeType == 44) {//ProgressReportPublicationList
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.ProgressReportPublicationListType  progressReportPublicationList 
                            = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeProgressReportPublicationListType();
                        progressReportPublicationList.setAttFile(attachedFileType);
                        researchPlanAttachments.setProgressReportPublicationList(progressReportPublicationList);                              
                     }
                  }                  
               }
               //start HumanSubjectSetion
               if (narrativeType == 25) {//ProtectionOfHumanSubjects
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        HumanSubjectSectionType.ProtectionOfHumanSubjectsType protectionOfHumanSubjects =
                                objFactory.createHumanSubjectSectionTypeProtectionOfHumanSubjectsType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.HumanSubjectSectionType.ProtectionOfHumanSubjectsType 
//                            protectionOfHumanSubjectsn = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeProtectionOfHumanSubjectsType();
                        protectionOfHumanSubjects.setAttFile(attachedFileType);
                        humanSubjectSection.setProtectionOfHumanSubjects(protectionOfHumanSubjects);                       
                     }
                  }                  
               }               
               if (narrativeType == 26) {//InclusionOfWomenAndMinorities
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType inclusionOfWomenAndMinorities =
                                objFactory.createHumanSubjectSectionTypeInclusionOfWomenAndMinoritiesType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType 
//                            inclusionOfWomenAndMinorities = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeInclusionOfWomenAndMinoritiesType();
                        inclusionOfWomenAndMinorities.setAttFile(attachedFileType);
                        humanSubjectSection.setInclusionOfWomenAndMinorities(inclusionOfWomenAndMinorities);
                     }
                  }                  
               }
               if (narrativeType == 27) {//TargetedPlannedEnrollmentTable
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
//                      
                        HumanSubjectSectionType.TargetedPlannedEnrollmentTableType targetedPlannedEnrollmentTable =
                                objFactory.createHumanSubjectSectionTypeTargetedPlannedEnrollmentTableType();
//                      PHS398ResearchPlanType.ResearchPlanAttachmentsType.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType 
//                            targetedPlannedEnrollmentTable = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeTargetedPlannedEnrollmentTableType();
                        targetedPlannedEnrollmentTable.setAttFile(attachedFileType);
                        humanSubjectSection.setTargetedPlannedEnrollmentTable(targetedPlannedEnrollmentTable);                             
                     }
                  }                  
               }
               if (narrativeType == 28) {//InclusionOfChildren
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        HumanSubjectSectionType.InclusionOfChildrenType inclusionOfChildren = 
                                objFactory.createHumanSubjectSectionTypeInclusionOfChildrenType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.HumanSubjectSectionType.InclusionOfChildrenType 
//                            inclusionOfChildren = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeInclusionOfChildrenType();
                        inclusionOfChildren.setAttFile(attachedFileType);
                        humanSubjectSection.setInclusionOfChildren(inclusionOfChildren); 
                     }
                  }                  
               }               
               //end HumanSubjectSetion
               //start other research plan sections 
               if (narrativeType == 30) {//VertebrateAnimals
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                        
                        OtherResearchPlanSectionsType.VertebrateAnimalsType vertebrateAnimals = 
                                objFactory.createOtherResearchPlanSectionsTypeVertebrateAnimalsType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.VertebrateAnimalsType
//                            vertebrateAnimals = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeVertebrateAnimalsType();
                        vertebrateAnimals.setAttFile(attachedFileType);
                        otherResearchPlanSections.setVertebrateAnimals(vertebrateAnimals); 
                     }
                  }                  
               }              
               if (narrativeType == 45) {//SelectAgentResearch
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        OtherResearchPlanSectionsType.SelectAgentResearchType selectAgentResearch = 
                                objFactory.createOtherResearchPlanSectionsTypeSelectAgentResearchType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.SelectAgentResearchType 
//                            selectAgentResearch = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeSelectAgentResearchType();
                        selectAgentResearch.setAttFile(attachedFileType);
                        otherResearchPlanSections.setSelectAgentResearch(selectAgentResearch);
                     }
                  }                  
               }
               if (narrativeType == 46) {//MultiplePILeadershipPlan
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        OtherResearchPlanSectionsType.MultiplePILeadershipPlanType  multiplePILeadershipPlan =
                                objFactory.createOtherResearchPlanSectionsTypeMultiplePILeadershipPlanType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.MultiplePILeadershipPlanType 
//                            multiplePILeadershipPlan = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeMultiplePILeadershipPlanType();
                        multiplePILeadershipPlan.setAttFile(attachedFileType);
                        otherResearchPlanSections.setMultiplePILeadershipPlan(multiplePILeadershipPlan);
                     }
                  }                  
               }              
               if (narrativeType == 31) {//ConsortiumContractualArrangements
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType
                              consortiumContractualArrangements = objFactory.createOtherResearchPlanSectionsTypeConsortiumContractualArrangementsType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType 
//                            consortiumContractualArrangements = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeConsortiumContractualArrangementsType();
                        consortiumContractualArrangements.setAttFile(attachedFileType);
                        otherResearchPlanSections.setConsortiumContractualArrangements(consortiumContractualArrangements);
                     }
                  }                  
               }
               if (narrativeType == 32) {//LettersOfSupport
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        OtherResearchPlanSectionsType.LettersOfSupportType lettersOfSupport = 
                                objFactory.createOtherResearchPlanSectionsTypeLettersOfSupportType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.LettersOfSupportType 
//                            lettersOfSupport = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeLettersOfSupportType();
                        lettersOfSupport.setAttFile(attachedFileType);
                        otherResearchPlanSections.setLettersOfSupport(lettersOfSupport); 
                     }
                  }                  
               }
               if (narrativeType == 33) {//ResourceSharingPlans
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        OtherResearchPlanSectionsType.ResourceSharingPlansType resourceSharingPlans =
                                objFactory.createOtherResearchPlanSectionsTypeResourceSharingPlansType();
//                        PHS398ResearchPlanType.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.ResourceSharingPlansType 
//                            resourceSharingPlans = objFactory.createPHS398ResearchPlanTypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeResourceSharingPlansType();
                        resourceSharingPlans.setAttFile(attachedFileType);
                        otherResearchPlanSections.setResourceSharingPlans(resourceSharingPlans);                            
                     }
                  }                  
               }
               //end other research plan sections
               // for appendix 
               if (narrativeType == 34) {//Appendix
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        attachmentGroup.getAttachedFile().add(attachedFileType);
//                        researchPlanAttachments.getAppendix().getAttachedFile().add(attachedFileType);

                     }
                  }                  
               } 
               
           }  //end for
           if (humanSubjectSection != null)
               researchPlanAttachments.setHumanSubjectSection(humanSubjectSection);
           
           if (otherResearchPlanSections != null)
               researchPlanAttachments.setOtherResearchPlanSections(otherResearchPlanSections);             
           
           if (attachmentGroup != null)
               researchPlanAttachments.setAppendix(attachmentGroup);
           
            phs398ResearchPlanType.setResearchPlanAttachments(researchPlanAttachments);
            return phs398ResearchPlanType;
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398ResearchPlanV1_1Stream","getPHS398ResearchPlan()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }        
                    
 }    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398ResearchPlanV1_1();
    }     
    
}


