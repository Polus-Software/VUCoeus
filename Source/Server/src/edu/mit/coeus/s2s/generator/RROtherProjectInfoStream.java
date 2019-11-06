/*
 * @(#)RROtherProjectInfoStream.java October 19, 2004, 10:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
 
import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.s2s.validator.S2SValidator;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.rr_otherprojectinfo_v1.RROtherProjectInfoType;
import gov.grants.apply.system.attachments_v1.*;
import gov.grants.apply.system.global_v1.*;
import java.math.BigDecimal;
import java.sql.Date;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
 
/**
 * @author  Eleanor Shavell
 * @Created on March 9, 2005, 11:12 AM
 * Class for generating the object stream for grants.gov RROtherProjectInfo. It uses jaxb classes
 * which have been created under gov.grants.apply package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * RR_OtherProjectInfo.
 */
 
 public class RROtherProjectInfoStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.rr_otherprojectinfo_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory; 
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    private String propNumber;
    private UtilFactory utilFactory;
    
    
    private ProposalDevelopmentFormBean proposalBean;
    private ProposalDevelopmentTxnBean proposalDevTxnBean;
    private OrganizationMaintenanceFormBean orgBean;
    private OrganizationMaintenanceDataTxnBean orgTxnBean;
     //case 2406
    private S2STxnBean s2sTxnBean;
    private String organizationID;
   gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
  
 
   
    /** Creates a new instance of RROtherProjectInfoStream */
    public RROtherProjectInfoStream(){
        objFactory = new gov.grants.apply.forms.rr_otherprojectinfo_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
        proposalDevTxnBean = new ProposalDevelopmentTxnBean() ;
        orgTxnBean = new OrganizationMaintenanceDataTxnBean();
        s2sTxnBean = new S2STxnBean();   //case 2406
        xmlGenerator = new CoeusXMLGenrator();    
     } 
   
  
    private RROtherProjectInfoType getRROtherProjectInfo() 
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        
        RROtherProjectInfoType rrOtherInfo = objFactory.createRROtherProjectInfo();
        RROtherProjectInfoType.ProjectNarrativeAttachmentsType rrProjNarrative;
        RROtherProjectInfoType.AbstractAttachmentsType rrAbstract;
        RROtherProjectInfoType.BibliographyAttachmentsType rrBibliography;
        RROtherProjectInfoType.EquipmentAttachmentsType rrEquipment;
        RROtherProjectInfoType.FacilitiesAttachmentsType rrFacilities;
        RROtherProjectInfoType.OtherAttachmentsType rrOtherAttachments;
        
        
        GetNarrativeDocumentBean narrativeDocBean;
        Attachment attachment = null;
        String contentId;
        String contentType;
        
     
        
        try{
                
           proposalBean = proposalDevTxnBean.getProposalDevelopmentDetails(propNumber);
           // change for case 2406
   //        orgBean = orgTxnBean.getOrganizationMaintenanceDetails(proposalBean.getOrganizationId());      
           orgBean = getOrgData();
   
           /**
            *FormVersion
           */
            
           rrOtherInfo.setFormVersion("1.0");
         
           /**
            *HumanSubjectsIndicator and VertebrateAnimalsIndicator
           */
           Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
           ProposalSpecialReviewFormBean specialReviewBean;
           String description;
           int approvalCode = 0; //case 3110
            String protocolNumber = null; //case 3110
            
           rrOtherInfo.setHumanSubjectsIndicator("No"); 
           rrOtherInfo.setVertebrateAnimalsIndicator("No"); 
           
           if (vecSpecialReview != null) { 
                for (int vecCount = 0 ; vecCount < vecSpecialReview.size() ; vecCount++) {
                  specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);
                 
                  switch(specialReviewBean.getSpecialReviewCode()) {
                      case 1:
                          rrOtherInfo.setHumanSubjectsIndicator("Yes");                          
                          /**
                           *HumanSubjectsSupplement
                           */
                          RROtherProjectInfoType.HumanSubjectsSupplementType humanSupp 
                              = objFactory.createRROtherProjectInfoTypeHumanSubjectsSupplementType();
                          RROtherProjectInfoType.HumanSubjectsSupplementType.ExemptionNumbersType humanExemptionType 
                               = objFactory.createRROtherProjectInfoTypeHumanSubjectsSupplementTypeExemptionNumbersType();
                          description = specialReviewBean.getComments();
                         //case 3110 start - move logic into stored procedure
                         HashMap hmAppCode = new HashMap();
                         hmAppCode = s2sTxnBean.get_specRev_app_code(propNumber, 1, specialReviewBean.getApprovalCode());
                         approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
                         String linkEnabled = (String)hmAppCode.get("LINK_ENABLED").toString();
                         if (linkEnabled.equals("1")) {
                             if (hmAppCode.get("PROTOCOL_NUMBER") != null)  
                              protocolNumber = (String) hmAppCode.get("PROTOCOL_NUMBER").toString();
                         }
                 
                      //   if (specialReviewBean.getApprovalCode() == 4) {
                          if (approvalCode == 4) {
                             // exempt
                             //change for case 3110. if linked to protocol get the exemption checklist
                               if (linkEnabled.equals("1")){
                                    String exemptionNumbers = null;
                                    HashMap hmExempt = new HashMap();
                                    Vector vExempt = s2sTxnBean.getExemptionNumbers(protocolNumber);
                                    int listSize = vExempt.size();
         
                                    if (listSize > 0){
                                        for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                                        hmExempt = (HashMap)vExempt.elementAt(rowIndex);
                                        exemptionNumbers = (String) hmExempt.get("EXEMPTION_NUMBERS").toString();
                                        humanExemptionType.getExemptionNumber().add(exemptionNumbers);
               
                                        }
                                         humanSupp.setExemptionNumbers(humanExemptionType);
                                    }
                             }else {
                             if (description != null){
                                //parse description for exemption numbers separated by commas. 
                                //valid values are E1,E2,E3,E4,E5,E6
                       
                                String[] exemptions = description.split(",");
                                String exemptionNumber;
                                 for (int i = 0; i< exemptions.length; i++){
                                    exemptionNumber = exemptions[i].trim();
                                    humanExemptionType.getExemptionNumber().add(exemptionNumber);
                                 }
                                humanSupp.setExemptionNumbers(humanExemptionType);
                             }
                             }
                          }  
                          RROtherProjectInfoType.HumanSubjectsSupplementType.HumanSubjectIRBReviewDateType humanIRBDate 
                              = objFactory.createRROtherProjectInfoTypeHumanSubjectsSupplementTypeHumanSubjectIRBReviewDateType();
           
                       //case 3110
                          if (approvalCode == 1 ) {
                        //  if (specialReviewBean.getApprovalCode() == 1) {
                              //pending
                            humanSupp.setHumanSubjectIRBReviewIndicator("Yes"); 
                          } else {
                            humanSupp.setHumanSubjectIRBReviewIndicator("No");
                          
                            if (specialReviewBean.getApprovalDate() != null ) {
                                humanIRBDate.setValue(getCal(specialReviewBean.getApprovalDate() ));
                                humanIRBDate.setHumanSubjectIRBReviewIndicator("No");
                                humanSupp.setHumanSubjectIRBReviewDate(humanIRBDate);
                             }
                          }
                                     
                          humanSupp.setHumanSubjectAssuranceNumber(orgBean.getHumanSubAssurance().substring(3));
                          
                          rrOtherInfo.setHumanSubjectsSupplement(humanSupp);
                         break;
                      case 2:
                          rrOtherInfo.setVertebrateAnimalsIndicator("Yes");
                          /** VertebrateAnimalsSupplement
                           *
                           */
                          RROtherProjectInfoType.VertebrateAnimalsSupplementType animalSupp
                            = objFactory.createRROtherProjectInfoTypeVertebrateAnimalsSupplementType();
                          RROtherProjectInfoType.VertebrateAnimalsSupplementType.VertebrateAnimalsIACUCApprovalDateReviewDateType animalDate
                             = objFactory.createRROtherProjectInfoTypeVertebrateAnimalsSupplementTypeVertebrateAnimalsIACUCApprovalDateReviewDateType();
                          
                         //case 3110 start - move logic into stored procedure
                         hmAppCode = new HashMap();
                         hmAppCode = s2sTxnBean.get_specRev_app_code(propNumber, 2, specialReviewBean.getApprovalCode());
                         approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
                      
                         if (approvalCode == 1) {
                     //     if (specialReviewBean.getApprovalCode() == 1) {
                              //pending
                            animalSupp.setVertebrateAnimalsIACUCReviewIndicator("Yes");
                          } else {
                              animalSupp.setVertebrateAnimalsIACUCReviewIndicator("No");
                          
                              if (specialReviewBean.getApprovalDate() != null) {
                              
                                animalDate.setValue(getCal(specialReviewBean.getApprovalDate()));
                                animalDate.setVertebrateAnimalsIACUCReviewIndicator("No");
                                animalSupp.setVertebrateAnimalsIACUCApprovalDateReviewDate(animalDate);
                               
                              }
                          }
                          
                          animalSupp.setAssuranceNumber(orgBean.getAnimalWelfareAssurance());
                          
                          rrOtherInfo.setVertebrateAnimalsSupplement(animalSupp);
                          break;
                      default:
                          break;
                  }   //switch
                          
                }//for
            } //if
        
           ProposalYNQBean proposalYNQBean;
           String answer="No";
           String answerExplanation = " ";
        
           /**
            *ProprietaryInformationIndicator
           */
           
           proposalYNQBean = getAnswer("G8");
           if (proposalYNQBean != null) {
              answer = (proposalYNQBean.getAnswer().equals("Y") ? "Yes" : "No");
              rrOtherInfo.setProprietaryInformationIndicator(answer); 
           }
           
           /**
            *EnvironmentalImpact
            **4a. Does this project have an impact on environment (G9 ynq)
            *4b.   if yes, explain
            *4c....has an exemption been authorized? (27 ynq)
            *4d.   if yes, explain
           */
   
           RROtherProjectInfoType.EnvironmentalImpactType  envType =
               objFactory.createRROtherProjectInfoTypeEnvironmentalImpactType();
           
           proposalYNQBean = getAnswer("G9");
           if (proposalYNQBean != null) {
            answer = (proposalYNQBean.getAnswer().equals("Y") ? "Yes" : "No");
            answerExplanation = proposalYNQBean.getExplanation();
           
           
            envType.setEnvironmentalImpactIndicator(answer); 
          
           
            if (answer.equals("Yes") & answerExplanation !=null) {
                RROtherProjectInfoType.EnvironmentalImpactType.EnvironmentalImpactExplanationType
                  explanation = objFactory.createRROtherProjectInfoTypeEnvironmentalImpactTypeEnvironmentalImpactExplanationType();
                explanation.setValue(answerExplanation);
                explanation.setEnvironmentalImpactIndicator(answer);
                envType.setEnvironmentalImpactExplanation(explanation);
            }   

           }
           
           proposalYNQBean = getAnswer("27");
           if (proposalYNQBean != null) {
                answer = proposalYNQBean.getAnswer();
                answerExplanation = proposalYNQBean.getExplanation();
                
                if (answer.equals("Y") ) answer = "Yes" ;
                if (answer.equals("N") ) answer = "No" ;
                               
                if (!answer.equals("X")) {
                  RROtherProjectInfoType.EnvironmentalImpactType.EnvironmentalExemptionType 
                    envImpactExemptType = objFactory.createRROtherProjectInfoTypeEnvironmentalImpactTypeEnvironmentalExemptionType();  
                  envImpactExemptType.setEnvironmentalExemptionIndicator(answer);
           
                   if (answerExplanation !=null ) {
                       RROtherProjectInfoType.EnvironmentalImpactType.EnvironmentalExemptionType.EnvironmentalExemptionExplanationType 
                         envExemptExplanation = objFactory.createRROtherProjectInfoTypeEnvironmentalImpactTypeEnvironmentalExemptionTypeEnvironmentalExemptionExplanationType();
                       if (!answer.equals("Yes")) S2SValidator.addCustError(propNumber, "Question 27 must be answered 'Yes' if Question G9 is answered 'Yes'");
                       envExemptExplanation.setEnvironmentalExemptionIndicator(answer);
                       envExemptExplanation.setValue(answerExplanation);
                       envImpactExemptType.setEnvironmentalExemptionExplanation(envExemptExplanation);
                   } 
                  
                  envType.setEnvironmentalExemption(envImpactExemptType);
                 }
                
           if (envType != null) rrOtherInfo.setEnvironmentalImpact(envType);
           }
           
           /**
            *InternationalActivities
           */
           proposalYNQBean = getAnswer("H1");
           if (proposalYNQBean != null) {
            answer = (proposalYNQBean.getAnswer().equals("Y") ? "Yes" : "No");
            answerExplanation = proposalYNQBean.getExplanation();
           
            RROtherProjectInfoType.InternationalActivitiesType intType =
              objFactory.createRROtherProjectInfoTypeInternationalActivitiesType();
            intType.setInternationalActivitiesIndicator(answer); 
            if (answerExplanation != null ) {
               RROtherProjectInfoType.InternationalActivitiesType.ActivitiesPartnershipsCountriesType countries =
                 objFactory.createRROtherProjectInfoTypeInternationalActivitiesTypeActivitiesPartnershipsCountriesType();
               countries.setInternationalActivitiesIndicator(answer);
               countries.setValue(answerExplanation);
               intType.setActivitiesPartnershipsCountries(countries);
            }
            rrOtherInfo.setInternationalActivities(intType);
           }
           
           /**
            * Attachments
           */
           
       
        //   String title;
           int narrativeType;
           int moduleNum;
           ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
           ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
      
           rrOtherAttachments = objFactory.createRROtherProjectInfoTypeOtherAttachmentsType();
          
           Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
               
            // case 3110
       //    S2STxnBean s2sTxnBean = new S2STxnBean();
           LinkedHashMap hmArg = new LinkedHashMap();
                     
           HashMap hmNarrative = new HashMap();
           boolean otherAttachments = false;
           
           int size=vctNarrative==null?0:vctNarrative.size();
           for (int row=0; row < size;row++) {
               proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
               moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
               //Modfied by Geo
               //The procedure fetches the module title as file name
               //This will be used for type other, since its a kind of special
               //requirement for some programs that the filename will be used to identfy
               //the attachment if the attachment type is 'OTHER'.
               String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
               
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               description = hmNarrative.get("DESCRIPTION").toString();
     
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
               hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
               attachment = getAttachment(hmArg);
                   
               if (narrativeType == 5){
                   //ABSTRACT - PROJECT SUMMARY
                   if (attachment == null) {
                     //attachment does not already exist - we need to get it and add it
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     Attachment abstractAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                      if (abstractAttachment.getContent() != null){
                        rrAbstract = objFactory.createRROtherProjectInfoTypeAbstractAttachmentsType();        
                        attachedFileType = getAttachedFileType(abstractAttachment);         
                        rrAbstract.setAbstractAttachment(attachedFileType );     
                        rrOtherInfo.setAbstractAttachments(rrAbstract);
                    }
                   }
 
               } else if (narrativeType ==1) { 
                     //NARRATIVE
                  if (attachment == null) {
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (narrativeAttachment.getContent() != null){
                        rrProjNarrative = objFactory.createRROtherProjectInfoTypeProjectNarrativeAttachmentsType();
                        attachedFileType = getAttachedFileType(narrativeAttachment);
                        rrProjNarrative.setProjectNarrativeAttachment(attachedFileType );                         
                        rrOtherInfo.setProjectNarrativeAttachments(rrProjNarrative);
                     }
                  }
  
               } else if (narrativeType ==2) {  
                     //facilities
                   if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment facilitiesAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (facilitiesAttachment.getContent() != null){
                        rrFacilities= objFactory.createRROtherProjectInfoTypeFacilitiesAttachmentsType(); 
                        attachedFileType = getAttachedFileType(facilitiesAttachment);
                        rrFacilities.setFacilitiesAttachment(attachedFileType);
                        rrOtherInfo.setFacilitiesAttachments(rrFacilities);          
                    } 
                  }
         
               } else if (narrativeType == 3) {  
                   //EQUIPMENT
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment equipmentAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (equipmentAttachment.getContent() != null){
                        rrEquipment= objFactory.createRROtherProjectInfoTypeEquipmentAttachmentsType();
                        attachedFileType = getAttachedFileType(equipmentAttachment);
                        rrEquipment.setEquipmentAttachment(attachedFileType );
                        rrOtherInfo.setEquipmentAttachments(rrEquipment);
                    }
                  }
    
               } else if (narrativeType == 4) {  
                //BIBLIOGRAPHY  
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment biblioAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (biblioAttachment.getContent() != null){ 
                        rrBibliography= objFactory.createRROtherProjectInfoTypeBibliographyAttachmentsType();
                        attachedFileType = getAttachedFileType(biblioAttachment);
                        rrBibliography.setBibliographyAttachment(attachedFileType );
                        rrOtherInfo.setBibliographyAttachments(rrBibliography);
                    }
                  }
               }
                else if (narrativeType == 8 || narrativeType == 15) {
                   //OTHER or SUPPLEMENTARY DOCUMENTATION
                    if (attachment == null) {
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                        Attachment otherAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                         if (otherAttachment.getContent() != null){
                             //Modified by Geo to set the module title as file name if the type is OTHER
                            otherAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                            attachedFileType = getAttachedFileType(otherAttachment);
                            rrOtherAttachments.getOtherAttachment().add(attachedFileType);   
                            otherAttachments = true;
                        }
                    }
              }
           }  //end for
           if(otherAttachments) rrOtherInfo.setOtherAttachments(rrOtherAttachments);
             
           return rrOtherInfo;
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RROtherProjectInfoStream","getRROtherProjectInfo()");
            throw new CoeusXMLException(jaxbEx.getMessage());
           }
        
      
    }
      
       
      //start case 2406
    private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = s2sTxnBean.getOrganizationID(propNumber,"O");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
        return orgTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }
    /**
     * getAndAddNarrative. get the narrative content from database and call base method to add attachment
     
    private  Attachment getAndAddNarrative (LinkedHashMap hmArg,ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
 
        String contentId = createContentId(hmArg);
        Attachment attachment = new Attachment();
        attachment.setContent( proposalNarrativePDFSourceBean.getFileBytes());
        String contentType = "application/octet-stream";
        attachment.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
        attachment.setContentId(contentId);
        attachment.setContentType(contentType);
                   
        //no need for this to return an attachment,     
        addAttachment(hmArg, attachment);
                 
     return attachment;
    }
    */
    //ynq questions
    private ProposalYNQBean getAnswer(String questionID) throws CoeusException,DBException {
        
       
        String question;
        ProposalYNQBean   proposalYNQBean = null;
        //QUESTIONNAIRE ENHANCEMENT STARTS
        Vector vecYNQ;
        if(questionID.equals("27")||questionID.equals("G8")||questionID.equals("H1"))
        {
           vecYNQ= proposalBean.getPropQuestionnaireAnswers();
           ProposalYNQBean   proposalYNQBeanFirst = null;
           ProposalYNQBean   proposalYNQBeanSecond = null;
           if(questionID.equals("27")){questionID="123";}
           else if(questionID.equals("G8")){questionID="122";}
           else if(questionID.equals("H1")){questionID="126";}
           
         for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                proposalYNQBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                question = proposalYNQBean.getQuestionId();
           
                if (question.equals( questionID)){
                    if(proposalYNQBeanFirst==null){ 
                    proposalYNQBeanFirst= proposalYNQBean;}
                    else{
                        proposalYNQBeanSecond=proposalYNQBean;
                        proposalYNQBeanFirst.setExplanation(proposalYNQBeanSecond.getAnswer());
                        return proposalYNQBeanFirst;
                    }
                     
                
                if(questionID.equals("123")){questionID="124";}
                else if(questionID.equals("122")){return proposalYNQBeanFirst;}
                else if(questionID.equals("126")){questionID="127";}
        }}
         
        }
        else
        {
        vecYNQ= proposalBean.getPropYNQAnswerList();
        if (vecYNQ != null) {    
              for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                proposalYNQBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                question = proposalYNQBean.getQuestionId();
           
                if (question.equals( questionID)){
                     return proposalYNQBean;
                }
              }
        }
        }
        return null;//return null if there is no specific question
//        return proposalYNQBean;
    }
   
   //QUESTIONNAIRE ENHANCEMENT ENDS
    
      
private gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFileType(Attachment attachment) 
     throws JAXBException {
    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
    hashValueType = globalObjFactory.createHashValueType();
    
    fileLocation.setHref(attachment.getContentId());
    attachedFileType.setFileLocation(fileLocation);
    attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
    attachedFileType.setMimeType("application/octet-stream");
    try{
        attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
    }catch(Exception ex){
        ex.printStackTrace();
        UtilFactory.log(ex.getMessage(),ex, "RROtherProjectInfoStream", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}

   private Calendar getCal(java.sql.Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
 
    

    


    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getRROtherProjectInfo();
    }    
 
 }   
    

