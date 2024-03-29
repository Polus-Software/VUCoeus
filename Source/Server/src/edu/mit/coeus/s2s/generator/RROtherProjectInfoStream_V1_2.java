/*
 * @(#)RROtherProjectInfoStream_V1_2.java 
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


import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.rr_otherprojectinfo_1_2_v1_2.RROtherProjectInfo12Type;

import java.util.Calendar;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
 
/**
 * @author  Eleanor Shavell
 */
 
 public class RROtherProjectInfoStream_V1_2 extends S2SBaseStream{ 
    private gov.grants.apply.forms.rr_otherprojectinfo_1_2_v1_2.ObjectFactory objFactory; 
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
  
 
   
    /** Creates a new instance of RROtherProjectInfoStream_V1_1 */
    public RROtherProjectInfoStream_V1_2(){
        objFactory = new gov.grants.apply.forms.rr_otherprojectinfo_1_2_v1_2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
        proposalDevTxnBean = new ProposalDevelopmentTxnBean() ;
        orgTxnBean = new OrganizationMaintenanceDataTxnBean();
        s2sTxnBean = new S2STxnBean();   
        xmlGenerator = new CoeusXMLGenrator();    
     } 
   
  
    private RROtherProjectInfo12Type getRROtherProjectInfo() 
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        
        RROtherProjectInfo12Type rrOtherInfo = objFactory.createRROtherProjectInfo12();
        RROtherProjectInfo12Type.ProjectNarrativeAttachmentsType rrProjNarrative;
        RROtherProjectInfo12Type.AbstractAttachmentsType rrAbstract;
        RROtherProjectInfo12Type.BibliographyAttachmentsType rrBibliography;
        RROtherProjectInfo12Type.EquipmentAttachmentsType rrEquipment;
        RROtherProjectInfo12Type.FacilitiesAttachmentsType rrFacilities;
        RROtherProjectInfo12Type.OtherAttachmentsType rrOtherAttachments;
        RROtherProjectInfo12Type.EnvironmentalImpactType envImpactType;
        RROtherProjectInfo12Type.EnvironmentalImpactType.EnvironmentalExemptionType envImpactExemptType;
        
        RROtherProjectInfo12Type.HumanSubjectsSupplementType humSubjectsSuppType;
        RROtherProjectInfo12Type.HumanSubjectsSupplementType.ExemptionNumbersType humSubjectsSuppExemptType;
        RROtherProjectInfo12Type.InternationalActivitiesType internActivitiesType;
        RROtherProjectInfo12Type.VertebrateAnimalsSupplementType vertAnimalSuppType;
         
        
        GetNarrativeDocumentBean narrativeDocBean;
        Attachment attachment = null;
        String contentId;
        String contentType;
        
     
        
        try{
                
           proposalBean = proposalDevTxnBean.getProposalDevelopmentDetails(propNumber);
                
           orgBean = getOrgData();
          
   
           /**
            *FormVersion
           */
            
           rrOtherInfo.setFormVersion("1.2");
         
           /**
            *HumanSubjectsIndicator and VertebrateAnimalsIndicator
           */
           Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
           ProposalSpecialReviewFormBean specialReviewBean;
           String description;
           int approvalCode = 0; //case 3110
           String protocolNumber = null; //case 3110
           
           rrOtherInfo.setHumanSubjectsIndicator("N: No"); 
           rrOtherInfo.setVertebrateAnimalsIndicator("N: No"); 
          
           
           if (vecSpecialReview != null) { 
                for (int vecCount = 0 ; vecCount < vecSpecialReview.size() ; vecCount++) {
                  specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);
                 
                  switch(specialReviewBean.getSpecialReviewCode()) {
                      case 1:
                          rrOtherInfo.setHumanSubjectsIndicator("Y: Yes");
                        
                          /**
                           *HumanSubjectsSupplement
                           */
                          humSubjectsSuppType = objFactory.createRROtherProjectInfo12TypeHumanSubjectsSupplementType();
                          humSubjectsSuppExemptType = objFactory.createRROtherProjectInfo12TypeHumanSubjectsSupplementTypeExemptionNumbersType();
                          description = specialReviewBean.getComments();

                          //new for version 1.2 - default
                           humSubjectsSuppType.setExemptFedReg("N: No");
                         
                         HashMap hmAppCode = new HashMap();
                         hmAppCode = s2sTxnBean.get_specRev_app_code(propNumber, 1, specialReviewBean.getApprovalCode());
                         
                         approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
                         String linkEnabled = (String)hmAppCode.get("LINK_ENABLED").toString();
                         if (linkEnabled.equals("1")) {
                           if (hmAppCode.get("PROTOCOL_NUMBER") != null)  
                           protocolNumber = (String) hmAppCode.get("PROTOCOL_NUMBER").toString();
                         }
                
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
                                        humSubjectsSuppExemptType.getExemptionNumber().add(exemptionNumbers);
               
                                        }
                                         humSubjectsSuppType.setExemptionNumbers(humSubjectsSuppExemptType);
                                    }
                             }else {
                             if (description != null){
                                //parse description for exemption numbers separated by commas. 
                                //valid values are E1,E2,E3,E4,E5,E6
                       
                                String[] exemptions = description.split(",");
                                String exemptionNumber;
                                 for (int i = 0; i< exemptions.length; i++){
                                    exemptionNumber = exemptions[i].trim();
                                    humSubjectsSuppExemptType.getExemptionNumber().add(exemptionNumber);
                                 }
                                humSubjectsSuppType.setExemptionNumbers(humSubjectsSuppExemptType);
                             }
                             }
                             //new for version 1.2
                              humSubjectsSuppType.setExemptFedReg("Y: Yes");
                          }  
                          
                           if (approvalCode == 1 ) {
                              //pending
                            humSubjectsSuppType.setHumanSubjectIRBReviewIndicator("Y: Yes"); 
                          } else {
                            humSubjectsSuppType.setHumanSubjectIRBReviewIndicator("N: No");
                          
                            if (specialReviewBean.getApprovalDate() != null ) {
                             
                                humSubjectsSuppType.setHumanSubjectIRBReviewDate(getCal(specialReviewBean.getApprovalDate()));
                             }
                          }
                                     
                          if (orgBean.getHumanSubAssurance() != null)
                          humSubjectsSuppType.setHumanSubjectAssuranceNumber(orgBean.getHumanSubAssurance().substring(3));
                          
                          if (humSubjectsSuppType != null)
                          rrOtherInfo.setHumanSubjectsSupplement(humSubjectsSuppType);
                         break;
                         
                      case 2:                            

                          rrOtherInfo.setVertebrateAnimalsIndicator("Y: Yes");
                          /** VertebrateAnimalsSupplement
                           *
                           */
                          vertAnimalSuppType = objFactory.createRROtherProjectInfo12TypeVertebrateAnimalsSupplementType();
                         
                         hmAppCode = new HashMap();
                         hmAppCode = s2sTxnBean.get_specRev_app_code(propNumber, 2, specialReviewBean.getApprovalCode());
                         approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
                 
                         if (approvalCode == 1) {
                              //pending
                              vertAnimalSuppType.setVertebrateAnimalsIACUCReviewIndicator("Y: Yes");
                          } else {
                              vertAnimalSuppType.setVertebrateAnimalsIACUCReviewIndicator("N: No");
                          
                              if (specialReviewBean.getApprovalDate() != null) {
                              
                                vertAnimalSuppType.setVertebrateAnimalsIACUCApprovalDateReviewDate(getCal(specialReviewBean.getApprovalDate()));
                               
                              }
                          }
                          
                          if (orgBean.getAnimalWelfareAssurance() != null)
                          vertAnimalSuppType.setAssuranceNumber(orgBean.getAnimalWelfareAssurance());
                          
                          if (vertAnimalSuppType != null)
                          rrOtherInfo.setVertebrateAnimalsSupplement(vertAnimalSuppType);
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
              answer = (proposalYNQBean.getAnswer().equals("Y") ? "Y: Yes" : "N: No");
              rrOtherInfo.setProprietaryInformationIndicator(answer); 
           } 
           
           /**
            *EnvironmentalImpact - there are two questions on the form:
            *4a. Does this project have an impact on environment (G9 ynq)
            *4b.   if yes, explain
            *4c....has an exemption been authorized? (27 ynq)
            *4d.   if yes, explain
           */
            
           proposalYNQBean = getAnswer("G9");
          envImpactType =  objFactory.createRROtherProjectInfo12TypeEnvironmentalImpactType();
    
           if (proposalYNQBean != null) {
            answer = (proposalYNQBean.getAnswer().equals("Y") ? "Y: Yes" : "N: No");
            answerExplanation = proposalYNQBean.getExplanation();
           
            envImpactType.setEnvironmentalImpactIndicator(answer); 
            if (answerExplanation != null)
              envImpactType.setEnvironmentalImpactExplanation(answerExplanation);
           }
           
            proposalYNQBean = getAnswer("27");
            if (answerExplanation != null && proposalYNQBean != null) {
                answer = proposalYNQBean.getAnswer();
                answerExplanation = proposalYNQBean.getExplanation();
                
                if (answer.equals("Y") ) answer = "Y: Yes" ;
                if (answer.equals("N") ) answer = "N: No" ;
                               
                if (!answer.equals("X")) {
                  envImpactExemptType = objFactory.createRROtherProjectInfo12TypeEnvironmentalImpactTypeEnvironmentalExemptionType();  
                  envImpactExemptType.setEnvironmentalExemptionIndicator(answer);
           
                  if (answerExplanation !=null ) 
                     envImpactExemptType.setEnvironmentalExemptionExplanation(answerExplanation);
                  envImpactType.setEnvironmentalExemption(envImpactExemptType);
                 }
            
           }
           if (envImpactType != null) rrOtherInfo.setEnvironmentalImpact(envImpactType);
           
           /** historic designation question - added for version 1.2
            */
           proposalYNQBean = getAnswer("G6");
           if (proposalYNQBean != null) {
              answer = (proposalYNQBean.getAnswer().equals("Y") ? "Y: Yes" : "N: No");
              answerExplanation = proposalYNQBean.getExplanation();
              rrOtherInfo.setHistoricDesignation(answer); 
              if(answerExplanation != null) 
                  rrOtherInfo.setHistoricDesignationExplanation(answerExplanation);
           } 
         
           
           
           /**
            *InternationalActivities
           */
         
       
           proposalYNQBean = getAnswer("H1");
           if (proposalYNQBean != null) {
            answer = (proposalYNQBean.getAnswer().equals("Y") ? "Y: Yes" : "N: No");
            answerExplanation = proposalYNQBean.getExplanation();
           
           internActivitiesType = objFactory.createRROtherProjectInfo12TypeInternationalActivitiesType();
           internActivitiesType.setInternationalActivitiesIndicator(answer); 
            if (answerExplanation != null ) {

               internActivitiesType.setInternationalActivitiesIndicator(answer);
               internActivitiesType.setActivitiesPartnershipsCountries(answerExplanation);
               proposalYNQBean = getQuestionnaireAnswer("138");
               internActivitiesType.setInternationalActivitiesExplanation(proposalYNQBean.getAnswer());
            }
            rrOtherInfo.setInternationalActivities(internActivitiesType);
           }
           
           /**
            * Attachments
           */
           
       
           int narrativeType;
           int moduleNum;
           ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
           ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
      
           rrOtherAttachments = objFactory.createRROtherProjectInfo12TypeOtherAttachmentsType();
          
           Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
               
   // case 3110
          
           s2sTxnBean = new S2STxnBean();
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
                        rrAbstract = objFactory.createRROtherProjectInfo12TypeAbstractAttachmentsType();        
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
                        rrProjNarrative = objFactory.createRROtherProjectInfo12TypeProjectNarrativeAttachmentsType();
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
                        rrFacilities= objFactory.createRROtherProjectInfo12TypeFacilitiesAttachmentsType(); 
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
                        rrEquipment= objFactory.createRROtherProjectInfo12TypeEquipmentAttachmentsType();
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
                        rrBibliography= objFactory.createRROtherProjectInfo12TypeBibliographyAttachmentsType();
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
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RROtherProjectInfoStreamV12","getRROtherProjectInfo()");
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
        //QUESTIONNAIRE ENHANCEMENT COEUSQA-3655 STARTS
        String answerId = null;
        String answer = null;
        Vector vecYNQ;
        if(questionID.equals("G8")){
            vecYNQ=proposalBean.getPropQuestionnaireAnswers();
            questionID="122";
        }else if(questionID.equals("G9")){
            vecYNQ=proposalBean.getPropQuestionnaireAnswers();
            questionID="123";
            answerId = "136";
            answer = "Y";
        }else if(questionID.equals("27")){
            vecYNQ=proposalBean.getPropQuestionnaireAnswers();
            questionID="124";
            answerId = "137";
            answer = "Y";
        }else if(questionID.equals("G6")){
            vecYNQ=proposalBean.getPropQuestionnaireAnswers();
            questionID="125";
            answerId = "135";
            answer = "Y";
        }else if(questionID.equals("H1")){
            vecYNQ=proposalBean.getPropQuestionnaireAnswers();
            questionID="126";
            answerId="127";
            answer = "Y";
        }else{
            vecYNQ= proposalBean.getPropYNQAnswerList();
        }
        
        if (vecYNQ != null) {
            ProposalYNQBean   tempBean;
              for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                tempBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                question = tempBean.getQuestionId();
           
                if (question.equals(questionID)) {
                    //if answerId != null, get the answer from answerId and set it as explanation.
                    if (answerId != null) {
                        proposalYNQBean = tempBean;
                        if (proposalYNQBean.getAnswer().equalsIgnoreCase(answer)) {
                            questionID = answerId;
                            answerId = null;
                            vecCount = -1;//Start the loop from 0
                            continue;
                        }
                        break;
                    }
                    if (proposalYNQBean != null) { //Answer for QuationId already got, this is for explanation
                        proposalYNQBean.setExplanation(tempBean.getAnswer());
                        break;
                    } else {
                        proposalYNQBean = tempBean;
                        answerId = null;
                        break;
                    }

                }
            }

        }
        //QUESTIONNAIRE ENHANCEMENT COEUSQA-3655 ENDS
   
        return proposalYNQBean;
    }
   
     private ProposalYNQBean getQuestionnaireAnswer(String questionID) throws CoeusException, DBException {
         Vector vecYNQ = proposalBean.getPropQuestionnaireAnswers();
         ProposalYNQBean tempBean;
         ProposalYNQBean proposalYNQBean = null;
         String question;
         for (int vecCount = 0; vecCount < vecYNQ.size(); vecCount++) {
             tempBean = (ProposalYNQBean) vecYNQ.get(vecCount);
             question = tempBean.getQuestionId();

             if (question.equals(questionID)) {
                 proposalYNQBean = tempBean;
                 break;
             }
         }
         return  proposalYNQBean;
     }
    
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
        UtilFactory.log(ex.getMessage(),ex, "RROtherProjectInfoStreamV12", "getAttachedFile");
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
    

