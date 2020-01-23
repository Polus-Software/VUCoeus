/*
 * NSFCoverPageV1_3Stream.java
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
//import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalPerDegreeFormBean;
//import edu.mit.coeus.propdev.bean.ProposalBiographyFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
//import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.propdev.bean.ProposalYNQFormBean;

import edu.mit.coeus.s2s.bean.KeyPersonBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.bean.NSFCoverPageV12TxnBean;
import edu.mit.coeus.s2s.Attachment;
//import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.util.S2SHashValue;

//import edu.mit.coeus.utils.CoeusConstants;
//import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
//import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.nsf_coverpage_v1_3.*;
//import gov.grants.apply.system.globallibrary_v2.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.LinkedHashMap;
//import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import javax.xml.bind.JAXBException;
/**
 *
 * @author  jenlu
 */

 public class NSFCoverPageV1_3Stream extends S2SBaseStream{
     
    private gov.grants.apply.forms.nsf_coverpage_v1_3.ObjectFactory objFactory;
//    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalLibObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;

    private NSFCoverPage13Type.PIInfoType PIInfo ;

    //txn beans
    private S2STxnBean s2sTxnBean;
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalPersonTxnBean proposalPersonTxnBean;
    private NSFCoverPageV12TxnBean nsf424TxnBean;
    
    //data beans
    private ProposalDevelopmentFormBean propDevFormBean;
    
    private HashMap attachmentMap;
//    private Vector vecContentIds;
//    private Hashtable propData;
    private String propNumber;
    private UtilFactory utilFactory;
    private KeyPersonBean PI;
    private CoeusVector keyPersons;
    
    //Narrative types
    private static final int PERSONAL_DATA = 13;
    private static final int PROPRIETARY_INFORMATION = 14; 
    private static final int NSF_DOCUMENT = 87; 
    private static final String COPI = "Co-PD/PI";
    private static final String PDPI = "PD/PI";

    //Question ID
    //    private static final int question_1 = 1104;
//    private static final int question_2 = 1105;
//    private static final int question_3 = 1106;
//    private static final int question_4 = 1107;
//    private static final int question_5 = 1108;
//    private static final int question_6 = 1109;

    private static final int question_1 = 52;
    private static final int question_2 = 53;
    //coeusqa-2439 start
//    private static final int question_3 = 54;
//    private static final int question_4 = 55;
    private static final int question_3 = 55;
    private static final int question_4 = 54;
    //coeusqa-2439 end
    private static final int question_5 = 56;
    private static final int question_6 = 57;

//    private static final int QUESTIONNAIRE_ID = 1025;
    private static final int QUESTIONNAIRE_ID = 2;
    private static final int MODULE_ITEM_CODE = 3;

    /** Creates a new instance of NSFCoverPageV1_1Stream */
    public NSFCoverPageV1_3Stream(){
        objFactory = new gov.grants.apply.forms.nsf_coverpage_v1_3.ObjectFactory();
//        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();

     
        s2sTxnBean = new S2STxnBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();        
        proposalPersonTxnBean = new ProposalPersonTxnBean();
        nsf424TxnBean = new NSFCoverPageV12TxnBean();
    
    } 
   
    private NSFCoverPage13Type getNSFCoverPage() throws CoeusXMLException,
                                                CoeusException,DBException{
        
        NSFCoverPage13Type nsfCoverPage = null;
       
        try{
            //get proposal master info
            propDevFormBean = getPropDevData();        
            //Populate PI, keyPersons and extraKeyPersons
            populatePIAndKeyPersons(); 
            //get existing attachment list  
            attachmentMap = getAttachmentMap();
            nsfCoverPage = objFactory.createNSFCoverPage13();
            
            if (propDevFormBean != null && propDevFormBean.getProgramAnnouncementNumber() != null){
                if (propDevFormBean.getProgramAnnouncementNumber().length() > 40 )
                     nsfCoverPage.setFundingOpportunityNumber
                    (propDevFormBean.getProgramAnnouncementNumber().substring(0,40));
                else  nsfCoverPage.setFundingOpportunityNumber
                    (propDevFormBean.getProgramAnnouncementNumber());
            }
                        
            nsfCoverPage.setNSFUnitConsideration(getNSFUnitConsideration());         

            nsfCoverPage.setOtherInfo(getOtherInfo());
            nsfCoverPage.setPIInfo(PIInfo);//did set answer in getOtherInfo(), so this step has to be here after setOtherInfo(getOtherInfo())

            gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType 
                attachmentGroup = 
                attachmentsObjFactory.createAttachmentGroupMin1Max100DataType();
            List attachmentList = attachmentGroup.getAttachedFile();
            //Add all personal_data pdfs
            attachmentList = getSingleCopyDocuments
                            (attachmentList, PERSONAL_DATA);                   
            attachmentList = getSingleCopyDocuments
                            (attachmentList, PROPRIETARY_INFORMATION);
            attachmentList = getSingleCopyDocuments
                            (attachmentList, NSF_DOCUMENT);
            if(attachmentList != null && attachmentList.size() > 0){
                nsfCoverPage.setSingleCopyDocuments(attachmentGroup);
            }
           
           //Set form version
           nsfCoverPage.setFormVersion("1.3");
//           NSFCoverPageV12TxnBean nsf424TxnBean = new NSFCoverPageV12TxnBean();
           HashMap hmInfo = new HashMap();
           hmInfo = nsf424TxnBean.getS2sOpportunity(propNumber);
           if (hmInfo != null && hmInfo.get("CLOSING_DATE") != null){
               nsfCoverPage.setDueDate(getCal(new Date( ((Timestamp) hmInfo.get("CLOSING_DATE")).getTime())));
           }
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NSFCoverPageV1_3Stream",
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

    
    private ProposalPerDegreeFormBean getProposalPerDegreeFormBean
    (KeyPersonBean keyPerson)throws DBException, CoeusException{
        ProposalPerDegreeFormBean proposalPerDegreeForm = new ProposalPerDegreeFormBean();
        String personId = keyPerson.getPersonId();
        proposalPerDegreeForm = 
            proposalPersonTxnBean.getProposalPersonDegreeMax(propNumber, personId);
        return proposalPerDegreeForm;                
    }
    
    private NSFCoverPage13.NSFUnitConsiderationType getNSFUnitConsideration()
                            throws JAXBException{
        NSFCoverPage13.NSFUnitConsiderationType nsfUnitConsideration =
                objFactory.createNSFCoverPage13TypeNSFUnitConsiderationType();
        nsfUnitConsideration.setDivisionCode(propDevFormBean.getAgencyDivCode());        
        nsfUnitConsideration.setProgramCode(propDevFormBean.getAgencyProgramCode());
        return nsfUnitConsideration;
    }
    
    private NSFCoverPage13.OtherInfoType getOtherInfo()
                throws JAXBException, CoeusException, DBException{
        NSFCoverPage13.OtherInfoType otherInfo = objFactory.createNSFCoverPage13TypeOtherInfoType();
        HashMap hmQuestions = new HashMap();
        int questionId = 0;
        String answer = null;
        CoeusVector cvQuestions = nsf424TxnBean.getQuestionnaireAnswers(propNumber, QUESTIONNAIRE_ID, MODULE_ITEM_CODE);

        if (cvQuestions != null && cvQuestions.size() > 0 ){
            for (int i = 0; i < cvQuestions.size(); i++){
                hmQuestions = (HashMap) cvQuestions.get(i);
                if (hmQuestions.get("QUESTION_ID") != null)
                    questionId = Integer.parseInt(hmQuestions.get("QUESTION_ID").toString());
                if (hmQuestions.get("ANSWER") != null)
                    answer = hmQuestions.get("ANSWER").toString();

                switch (questionId) {
                    case question_1:
                        if (answer != null){//set pu
                            PIInfo = objFactory.createNSFCoverPage13TypePIInfoType();
                            if(PI != null)
                                PIInfo.setIsCurrentPI(answer.equals("Y") ? "Y: Yes" : "N: No");
                        }
                        break;
                    case question_2:
                        if (answer != null)
                            otherInfo.setIsBeginInvestigator(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                    case question_3:
                        if (answer != null)
                            otherInfo.setIsEarlyConceptGrant(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                    case question_4:
                        if (answer != null)
                            otherInfo.setIsRapidResponseGrant(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                    case question_5:
                        if (answer != null)
                            otherInfo.setIsAccomplishmentRenewal(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                    case question_6:
                        if (answer != null)
                            otherInfo.setIsHighResolutionGraphics(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                }


            }
        }

        otherInfo.setIsDisclosureLobbyingActivities(getLobbyingAnswer());


        return otherInfo;
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

//        S2STxnBean s2sTxnBean = new S2STxnBean();
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
private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
    public Object getStream(HashMap hm) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hm.get("PROPOSAL_NUMBER");
        return getNSFCoverPage();
    }    
         
}
