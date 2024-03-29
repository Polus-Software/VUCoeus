/*
 * NSFCoverPageV1_1Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalPerDegreeFormBean;
import edu.mit.coeus.propdev.bean.ProposalBiographyFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.propdev.bean.ProposalYNQFormBean;

import edu.mit.coeus.s2s.bean.KeyPersonBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.util.S2SHashValue;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.nsf_coverpage_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import javax.xml.bind.JAXBException;
/**
 *
 * @author  jenlu
 */

 public class NSFCoverPageV1_1Stream extends S2SBaseStream{ 
     
    private gov.grants.apply.forms.nsf_coverpage_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalLibObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;

    //txn beans
    private S2STxnBean s2sTxnBean;
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalPersonTxnBean proposalPersonTxnBean;
    
    //data beans
    private ProposalDevelopmentFormBean propDevFormBean;
    
    private HashMap attachmentMap;
    private Vector vecContentIds;
    private Hashtable propData;
    private String propNumber;
    private UtilFactory utilFactory;
    private KeyPersonBean PI;
    private CoeusVector keyPersons;
    
    //Narrative types
    private static final int PERSONAL_DATA = 13;
    private static final int PROPRIETARY_INFORMATION = 14; 
    private static final String COPI = "Co-PD/PI";
    private static final String PDPI = "PD/PI";

   
    /** Creates a new instance of NSFCoverPageV1_1Stream */
    public NSFCoverPageV1_1Stream(){        
        objFactory = new gov.grants.apply.forms.nsf_coverpage_v1_1.ObjectFactory();
        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
     
        s2sTxnBean = new S2STxnBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();        
        proposalPersonTxnBean = new ProposalPersonTxnBean();
    
    } 
   
    private NSFCoverPageType getNSFCoverPage() throws CoeusXMLException,
                                                CoeusException,DBException{
        
        NSFCoverPageType nsfCoverPage = null;
       
        try{
            //get proposal master info
            propDevFormBean = getPropDevData();        
            //Populate PI, keyPersons and extraKeyPersons
            populatePIAndKeyPersons(); 
            //get existing attachment list  
            attachmentMap = getAttachmentMap();
            nsfCoverPage = objFactory.createNSFCoverPage();
            //In V1.1, the length of the FundingOpportunityNumber is changed from 100 to 40.
            if (propDevFormBean != null && propDevFormBean.getProgramAnnouncementNumber() != null){
                if (propDevFormBean.getProgramAnnouncementNumber().length() > 40 )
                     nsfCoverPage.setFundingOpportunityNumber
                    (propDevFormBean.getProgramAnnouncementNumber().substring(0,40));
                else  nsfCoverPage.setFundingOpportunityNumber
                    (propDevFormBean.getProgramAnnouncementNumber());
            }
                        
            nsfCoverPage.setNSFUnitConsideration(getNSFUnitConsideration());
            nsfCoverPage.setPIInfo(getPIInfo(PI));
            
            NSFCoverPageType.CoPIInfoType coPIInfo = 
                    objFactory.createNSFCoverPageTypeCoPIInfoType(); 
            //the form only takes 4 CoPI, we take first 4 coPi
            
            for(int cnt=0; cnt<keyPersons.size(); cnt++){                
                KeyPersonBean keyPersonBean = (KeyPersonBean)keyPersons.get(cnt);
  
                  if(keyPersonBean.getRole().equals(COPI) && coPIInfo.getCoPI().size() < 4 ){
                    coPIInfo.getCoPI().add(getCoPI(keyPersonBean));
                }                
            }
            nsfCoverPage.setCoPIInfo(coPIInfo);
            nsfCoverPage.setOtherInfo(getOtherInfo());
            gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType 
                attachmentGroup = 
                attachmentsObjFactory.createAttachmentGroupMin1Max100DataType();
            List attachmentList = attachmentGroup.getAttachedFile();
            //Add all personal_data pdfs
            attachmentList = getSingleCopyDocuments
                            (attachmentList, PERSONAL_DATA);                   
            attachmentList = getSingleCopyDocuments
                            (attachmentList, PROPRIETARY_INFORMATION); 
            if(attachmentList != null && attachmentList.size() > 0){
                nsfCoverPage.setSingleCopyDocuments(attachmentGroup);
            }
           
           //Set form version
           nsfCoverPage.setFormVersion("1.1");  
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NSFCoverPageV1_1Stream",
                                                            "getNSFCoverPage()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return nsfCoverPage;
    }
   
      
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
     
    private void populatePIAndKeyPersons() throws CoeusException, DBException{
        ArrayList allKeyPersons = s2sTxnBean.getInvestAndKeyPersons(propNumber,8);
        KeyPersonBean keyPerBean;
        if(allKeyPersons != null && !allKeyPersons.isEmpty()){
            keyPersons = (CoeusVector)allKeyPersons.get(0);
            if(keyPersons != null && !keyPersons.isEmpty()){
                for (int index = 0 ; index < keyPersons.size(); index++){
                    keyPerBean = (KeyPersonBean)keyPersons.get(index);
                    if(keyPerBean.getRole().equals(PDPI)){
                        PI = keyPerBean;
                        return;
                    }
                }
//                PI = (KeyPersonBean)keyPersons.get(0);              
            }
            
        }
    }
     
    private NSFCoverPageType.PIInfoType getPIInfo(KeyPersonBean PI) 
        throws JAXBException, DBException, CoeusException{
        NSFCoverPageType.PIInfoType PIInfo = 
                            objFactory.createNSFCoverPageTypePIInfoType();
        if(PI != null){
            //Set degree type and year.
             ProposalPerDegreeFormBean proposalPerDegreeForm = 
               this.getProposalPerDegreeFormBean(PI);
             if(proposalPerDegreeForm != null){
               String degree_code = proposalPerDegreeForm.getDegreeCode();
            
                String nsfDegree = s2sTxnBean.getNSFDegree(degree_code);
                PIInfo.setDegreeType(nsfDegree);
                Date graduationDate = proposalPerDegreeForm.getGraduationDate();
                if(graduationDate != null){
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                    String degreeYear = sdf.format(graduationDate);
                    PIInfo.setDegreeYear(degreeYear);
                }
            }
             
             PIInfo.setIsCurrentPI(this.getYNQAnswer("19"));
             
        }
        return PIInfo;
    }
    
    private NSFCoverPageType.CoPIInfoType.CoPIType getCoPI(KeyPersonBean keyPerson)
                            throws JAXBException, CoeusException, DBException{
        NSFCoverPageType.CoPIInfoType.CoPIType coPI= 
            objFactory.createNSFCoverPageTypeCoPIInfoTypeCoPIType();
        //Set name.
        HumanNameDataType humanNameDataType = 
                        globalLibObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(keyPerson.getFirstName());
        humanNameDataType.setMiddleName(keyPerson.getMiddleName());
        humanNameDataType.setLastName(keyPerson.getLastName());
        coPI.setName(humanNameDataType);
        //Set degree type and year.
        ProposalPerDegreeFormBean proposalPerDegreeForm = 
            this.getProposalPerDegreeFormBean(keyPerson);
        if(proposalPerDegreeForm != null){
            String degree_code = proposalPerDegreeForm.getDegreeCode();
            String nsfDegree = s2sTxnBean.getNSFDegree(degree_code);
            coPI.setDegreeType(nsfDegree);
            Date graduationDate = proposalPerDegreeForm.getGraduationDate();
            if(graduationDate != null){
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                String degreeYear = sdf.format(graduationDate);
                coPI.setDegreeYear(degreeYear);
            }
        }

        return coPI;
    }
    
    private ProposalPerDegreeFormBean getProposalPerDegreeFormBean
    (KeyPersonBean keyPerson)throws DBException, CoeusException{
        ProposalPerDegreeFormBean proposalPerDegreeForm = new ProposalPerDegreeFormBean();
        String personId = keyPerson.getPersonId();
        proposalPerDegreeForm = 
            proposalPersonTxnBean.getProposalPersonDegreeMax(propNumber, personId);
        return proposalPerDegreeForm;                
    }
    
    private NSFCoverPage.NSFUnitConsiderationType getNSFUnitConsideration()
                            throws JAXBException{
        NSFCoverPage.NSFUnitConsiderationType nsfUnitConsideration = 
                objFactory.createNSFCoverPageTypeNSFUnitConsiderationType();
        nsfUnitConsideration.setDivisionCode(propDevFormBean.getAgencyDivCode());        
        nsfUnitConsideration.setProgramCode(propDevFormBean.getAgencyProgramCode());
        return nsfUnitConsideration;
    }
    
    private NSFCoverPage.OtherInfoType getOtherInfo() 
                throws JAXBException, CoeusException, DBException{
        NSFCoverPage.OtherInfoType otherInfo = objFactory.createNSFCoverPageTypeOtherInfoType();
        otherInfo.setIsBeginInvestigator(getYNQAnswer("12"));
        otherInfo.setIsDisclosureLobbyingActivities(getLobbyingAnswer());
        otherInfo.setIsExploratoryResearch(getYNQAnswer("14"));
        otherInfo.setIsHistoricPlaces(getYNQAnswer("G6"));
        //case 4318 start
//        int proposalTypeCode = propDevFormBean.getProposalTypeCode();
////        otherInfo.setIsAccomplishmentRenewal(proposalTypeCode == 8 ? "Y: Yes" : "N: No");
//        otherInfo.setIsAccomplishmentRenewal(proposalTypeCode == 5 ? "Y: Yes" : "N: No");
        otherInfo.setIsAccomplishmentRenewal(getYNQAnswer("31"));
        //case 4318 end 
        otherInfo.setIsHighResolutionGraphics(getYNQAnswer("20"));
        return otherInfo;
    }
    
    // Yes/No questions
    private String getYNQAnswer(String questionID) throws CoeusException,DBException {
        String question;
        String answer = null;
        ProposalYNQBean   proposalYNQBean = null;
        
        Vector vecYNQ =propDevFormBean.getPropYNQAnswerList();
        if (vecYNQ != null) {    
              for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                    proposalYNQBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                    question = proposalYNQBean.getQuestionId();

                    if (question.equals( questionID)){
                         answer = proposalYNQBean.getAnswer();
                         //v1.1 changed enumeration. (from v1.0 "Yes/No" to  V1.1 "Y: Yes/N: No")
                         answer = answer.equals("Y") ? "Y: Yes" : "N: No";
                         break;
                    }
              }
        }
        return answer;
    }  
    
    private String getLobbyingAnswer() throws CoeusException, DBException{
        //Lobbying question H4 is a YNQ of question_type I (Individual).
        String answer = "N: No";
        if(keyPersons != null && !keyPersons.isEmpty()){
            String persId = null;
            for(int persCnt=0; persCnt<keyPersons.size(); persCnt++){
                KeyPersonBean keyPerson = (KeyPersonBean)keyPersons.get(persCnt);
              
                if(keyPerson.getRole().equals(PDPI) || keyPerson.getRole().equals(COPI)) {
                    persId = keyPerson.getPersonId();
                    Vector certifyAnswers = propDevTxnBean.getCertifyAnswers(propNumber, persId);
                    if(certifyAnswers == null){
                        return answer;
                    }
                    for(int questCnt = 0; questCnt<certifyAnswers.size(); questCnt++){
                        ProposalYNQFormBean ynqBean = (ProposalYNQFormBean)certifyAnswers.get(questCnt);
                        if(ynqBean.getQuestionId().equals("H4")){
                            if(ynqBean.getAnswer().equals("Y")){
                                return "Y: Yes";
                            }
                        }
                    }
                }
            }
        }
        return answer;
    }
   
    private List getSingleCopyDocuments(List attachmentList, int attachmentType) 
                throws JAXBException, CoeusException, DBException{
        Attachment pdf = null;
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
        Vector vctNarrative = proposalNarrativeTxnBean.
                                getPropNarrativePDFForProposal(propNumber);
        if(vctNarrative == null){
            return null;
        }

        S2STxnBean s2sTxnBean = new S2STxnBean();
        LinkedHashMap hmArg = new LinkedHashMap();
        int moduleNum = 0;
        int narrativeType = 0;

        for (int row=0; row < vctNarrative.size();row++) {
           proposalNarrativePDFSourceBean = 
                (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);

            moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
            HashMap narrativeInfo = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
            narrativeType = 
                Integer.parseInt((String)narrativeInfo.get("NARRATIVE_TYPE_CODE"));             
            if(narrativeType == attachmentType){
                hmArg.put(ContentIdConstants.MODULE_NUMBER, String.valueOf(moduleNum));                
                hmArg.put(ContentIdConstants.DESCRIPTION, 
                                    (String)narrativeInfo.get("DESCRIPTION"));                

                pdf = getAttachment(hmArg);//check if already attached.

                if(pdf == null ){//Not already attached, so get it.
                    proposalNarrativePDFSourceBean = 
                        proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    String contentId = createContentId(hmArg);
                    pdf = new Attachment();
                    if(pdf != null){
                        pdf.setContent( proposalNarrativePDFSourceBean.getFileBytes());
                        String contentType = "application/octet-stream";
                        pdf.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
                        pdf.setContentId(contentId);
                        pdf.setContentType(contentType);    
                        addAttachment(hmArg, pdf);     
                        gov.grants.apply.system.attachments_v1.AttachedFileDataType 
                            attachedFile = getAttachedFileType(pdf);
                        attachmentList.add(attachedFile);                        
                    }
                }
            }
        }
        return attachmentList;
    }

    
    private gov.grants.apply.system.attachments_v1.AttachedFileDataType 
                                    getAttachedFileType(Attachment attachment) 
         throws JAXBException {

        gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
        gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
        gov.grants.apply.system.global_v1.HashValueType hashValueType;

        attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
        if(attachment == null){
            return attachedFileType;
        }
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
            UtilFactory.log(ex.getMessage(),ex, "NSFCoverPageV1_1Stream", "getAttachedFileType");
            throw new JAXBException(ex);
        }

        return attachedFileType;

    }    

    
    public Object getStream(HashMap hm) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hm.get("PROPOSAL_NUMBER");
        return getNSFCoverPage();
    }    
         
}
