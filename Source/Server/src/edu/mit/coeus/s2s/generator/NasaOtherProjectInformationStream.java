/*
 * NasaOtherProjectInformationStream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.s2s.bean.KeyPersonBean;
import edu.mit.coeus.s2s.bean.NasaOtherProjectInformationTxnBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.utils.CoeusVector;
import gov.grants.apply.forms.nasa_otherprojectinformation_v1.*;
import java.util.*;
import javax.xml.bind.JAXBException;


/**
 *
 * @author  jenlu
 */
public class NasaOtherProjectInformationStream extends S2SBaseStream{
     private gov.grants.apply.forms.nasa_otherprojectinformation_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;    
    //txn bean
    private NasaOtherProjectInformationTxnBean nasaOtherProjectInformationTxnBean;
    private S2STxnBean s2sTxnBean; 
    private HashMap hmInfo;
    private String propNumber;
    private UtilFactory utilFactory;
    
    private static final String COPI = "Co-PD/PI";
    private static final String PDPI = "PD/PI";
    //JIRA COEUSQA-3663 - START
    private final static String YES = "Y: Yes";
    private final static String NO = "N: No";
    private Vector vctAnswerList;
    //JIRA COEUSQA-3663 - END
    
    NASAOtherProjectInformationType  nasaOtherProjectInformation;
    
    /** Creates a new instance of NasaOtherProjectInformationStream */
    public NasaOtherProjectInformationStream() {
        objFactory = new gov.grants.apply.forms.nasa_otherprojectinformation_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator(); 
        
    }
    
    private NASAOtherProjectInformationType getNasaOtherProjectInformation()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
        nasaOtherProjectInformation = objFactory.createNASAOtherProjectInformation();  
        nasaOtherProjectInformationTxnBean = new NasaOtherProjectInformationTxnBean();
        s2sTxnBean = new S2STxnBean();
        try{
            NASAOtherProjectInformationType.NASACivilServicePersonnelType 
                    nasaCivilServicePersonnel = objFactory.createNASAOtherProjectInformationTypeNASACivilServicePersonnelType();
            
            /**
            *FormVersion
            *
            */
            //JIRA COEUSQA-3663 - START
            ProposalDevelopmentTxnBean proposalTxnBean = new ProposalDevelopmentTxnBean();
            vctAnswerList = proposalTxnBean.getProposalQuestionnaire(propNumber);
            nasaOtherProjectInformation.setFormVersion("1.0");
            //hard coding "No" for question "Will NASA civil service personnel work on this project?"
            //nasaCivilServicePersonnel.setCivilServicePersonnel("N: No");
            ProposalYNQBean propYNQBean = getQuestionnaireAnswer("101", vctAnswerList);
            if(propYNQBean!=null){
                if(propYNQBean.getAnswer().equalsIgnoreCase("Y")){
                    nasaCivilServicePersonnel.setCivilServicePersonnel(YES);
                }else {
                    nasaCivilServicePersonnel.setCivilServicePersonnel(NO);
                }
            }
            //JIRA COEUSQA-3663 - END

            nasaOtherProjectInformation.setNASACivilServicePersonnel(nasaCivilServicePersonnel);
            nasaOtherProjectInformation.setHistoricImpact(getHistoricImpact());
            nasaOtherProjectInformation.setInternationalParticipation(getInternationalParticipation());
            setFormsAttachments();
            
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NasaOtherProjectInformationStream","getNasaOtherProjectInformation()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        } 
        return nasaOtherProjectInformation;
    }
            
    private NASAOtherProjectInformationType.HistoricImpactType getHistoricImpact()
                                        throws JAXBException,DBException,CoeusException{
        NASAOtherProjectInformationType.HistoricImpactType historicImpact = objFactory.createNASAOtherProjectInformationTypeHistoricImpactType();
        //JIRA COEUSQA-3663 - START
        ProposalYNQBean propYNQBean = getQuestionnaireAnswer("106", vctAnswerList);//Affect Historic etc
        if (propYNQBean != null) {
            if (propYNQBean.getAnswer().equalsIgnoreCase("Y")) {
                historicImpact.setHistoricImpactQ(YES);
                propYNQBean = getQuestionnaireAnswer("139", vctAnswerList);
                String explanation = propYNQBean.getAnswer();
                if (explanation != null && explanation.length() > 2000) {
                    explanation = explanation.substring(0, 2000);
                }
                historicImpact.setHistoricImpactEx(explanation);
            }else{
                historicImpact.setHistoricImpactQ(NO);
            }
        }
        //JIRA COEUSQA-3663 - END
        return historicImpact;
    }

    //case 3135 - CHANGED LOGIC
    
    private NASAOtherProjectInformationType.InternationalParticipationType getInternationalParticipation()
                    throws JAXBException,DBException,CoeusException{
        NASAOtherProjectInformationType.InternationalParticipationType 
            internationalParticipation = objFactory.createNASAOtherProjectInformationTypeInternationalParticipationType();
        //JIRA COEUSQA-3663 - START
        ProposalYNQBean propYNQBean = getQuestionnaireAnswer("108", vctAnswerList);
        if (propYNQBean != null) {
            if (propYNQBean.getAnswer().equalsIgnoreCase("Y")) {
                internationalParticipation.setInternationalParticipationQ(YES);
                List<ProposalYNQBean> ynqList = getQuestionnaireAnswers("109", vctAnswerList);
                if(ynqList.size() > 0){
                    for (int index = 0; index < ynqList.size(); index++) {
                        propYNQBean = ynqList.get(index);
                        if (propYNQBean.getAnswer().equals("Co-I")) {
                            internationalParticipation.setInternationalParticipationCoI(YES);
                        }else if (propYNQBean.getAnswer().equals("Collaborator")) {
                            internationalParticipation.setInternationalParticipationCollaborator(YES);
                        }else if (propYNQBean.getAnswer().equals("Equipment")) {
                            internationalParticipation.setInternationalParticipationEquipment(YES);
                        }  else if (propYNQBean.getAnswer().equals("Facility")) {
                            internationalParticipation.setInternationalParticipationFacility(YES);
                        } else if (propYNQBean.getAnswer().equals("PI")) {
                            internationalParticipation.setInternationalParticipationPI(YES);
                        }
                        //stemCellsType.getCellLines().add(propYNQBean.getAnswer());
                    }
                }
                // Because the answer of this question is "Yes",
                // if none of PI, Co-I and Collaborator was checked, then check Facility,
                if (internationalParticipation.getInternationalParticipationPI() == null
                        && internationalParticipation.getInternationalParticipationCoI() == null
                        && internationalParticipation.getInternationalParticipationCollaborator() == null) {
                    internationalParticipation.setInternationalParticipationFacility(YES);
                }

                propYNQBean = getQuestionnaireAnswer("140", vctAnswerList);
                if(propYNQBean != null) {
                    String explanation = propYNQBean.getAnswer();
                    if(explanation != null && explanation.length() > 2000) {
                        explanation = explanation.substring(0,2000);
                    }
                    internationalParticipation.setInternationalParticipatioEx(explanation);
                }
            }else {
                internationalParticipation.setInternationalParticipationQ(NO);
            }
        }
        //JIRA COEUSQA-3663 - END
           return internationalParticipation;
    }
        
    private void setFormsAttachments()throws JAXBException,DBException,CoeusException{
        String description;
        int narrativeType;
        int moduleNum;
        Attachment attachment = null;
        gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attGroupAppendices 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attGroupEndorsement 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attGroupLetters 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
        Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);   
        
        LinkedHashMap hmArg = new LinkedHashMap();
                     
        HashMap hmNarrative = new HashMap();
             
        int size=vctNarrative==null?0:vctNarrative.size();
        for (int row=0; row < size;row++) {
            proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
            moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   
  
//          String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
            hmNarrative = new HashMap();
            hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
            narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
            description = hmNarrative.get("DESCRIPTION").toString();
      
            hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
            hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
            attachment = getAttachment(hmArg);
            
            if (narrativeType == 47) {//Program Specific Data
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        nasaOtherProjectInformation.setPSDataAttach(attachedFileType);                       
                     }
                  }                  
            }
            
            if (narrativeType == 48) {//Appendices
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        attGroupAppendices.getAttachedFile().add(attachedFileType);
                     }
                  }
            }
            
            if (narrativeType == 49) {//Non-U.S. Organization Letters of Endorsement
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        attGroupEndorsement.getAttachedFile().add(attachedFileType);
                     }
                  }
            }
            if (narrativeType == 50) {// IRB & ACUC Letters
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        attGroupLetters.getAttachedFile().add(attachedFileType);
                     }
                  }
            }           
        }//end for loop
        if (attGroupAppendices != null && attGroupAppendices.getAttachedFile() != null)
            nasaOtherProjectInformation.setAppendAttach(attGroupAppendices);
        if (attGroupEndorsement != null && attGroupEndorsement.getAttachedFile() != null)
            nasaOtherProjectInformation.setLetterEndorsAttach(attGroupEndorsement);
        if (attGroupLetters != null && attGroupLetters.getAttachedFile() != null)
            nasaOtherProjectInformation.setIRBACUCLettersAttach(attGroupLetters);
    }
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getNasaOtherProjectInformation();
    }

    //JIRA COEUSQA-3663 - START
     private ProposalYNQBean getQuestionnaireAnswer(String questionID, Vector vecYNQ)throws CoeusException, DBException{
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
         return  proposalYNQBean;
     }

     private List<ProposalYNQBean> getQuestionnaireAnswers(String questionID, Vector vecYNQ) throws CoeusException, DBException {
         ProposalYNQBean tempBean;
         List<ProposalYNQBean> ynqBeanList = new ArrayList();
         String question;
         if (vecYNQ != null) {
             for (int vecCount = 0; vecCount < vecYNQ.size(); vecCount++) {
                 tempBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                 question = tempBean.getQuestionId();
                 if (question.equals(questionID)) {
                     ynqBeanList.add(tempBean);
                 }
             }
         }
         return ynqBeanList;
     }
     //JIRA COEUSQA-3663 - END
    }
