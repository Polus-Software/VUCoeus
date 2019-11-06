/*
 * @(#)PHS398ChecklistStream.java January 5, 2006, 10:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.phs398_checklist_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;

import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  Eleanor Shavell
 * @Created on January 5, 2006, 10:12 AM
 * Class for generating the object stream for grants.gov PHS398_Checklist. It uses jaxb classes
 * which have been created under gov.grants.apply package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * RR_Budgetschema.
 */

 public class PHS398ChecklistStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.phs398_checklist_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalLibObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
  
    //txn bean
    private S2SPHS398ChecklistTxnBean s2sPHS398ChecklistTxnBean;
 
    private String propNumber;
    private HashMap hmInfo ;
    private UtilFactory utilFactory;
    private Calendar calendar;
   
    private int version;
   
//    Attachment attachment = null;

   
    /** Creates a new instance of PHS398ChecklistStream */
    public PHS398ChecklistStream(){
        objFactory = new gov.grants.apply.forms.phs398_checklist_v1.ObjectFactory();
        globalObjFactory = new  gov.grants.apply.system.global_v1.ObjectFactory ();
        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
      
        xmlGenerator = new CoeusXMLGenrator();
     
        s2sPHS398ChecklistTxnBean = new S2SPHS398ChecklistTxnBean();
        hmInfo = new HashMap();
        
    
    } 
   
    private PHS398ChecklistType getPHS398Checklist() throws CoeusXMLException,CoeusException,DBException{
        PHS398ChecklistType checklistType = null;
        PHS398ChecklistType.IncomeBudgetPeriodType incomeBudgetPeriodType = null;
        
      
        try{
           checklistType = objFactory.createPHS398Checklist();
         
           hmInfo = getSimpleInfo();
           HashMap hmAppType = getApplicationType();
           HashMap hmProgIncome = new HashMap(); 
            Vector<ProposalYNQBean> cvQuestions = null;
             cvQuestions = getQuestionAnswers();
             HashMap hmQuestions = new HashMap();
             HashMap hmQuestionsBranch=new HashMap();
           int numQuestions = (cvQuestions != null) ?cvQuestions.size() : 0;
           int questionId = 0;
          
           String answer = null;
           
           //No need to search again for the branching question to make it order found the question will come in order
           //115 come always after 114.
           //also the vector is used so the order will not loose.
           if (numQuestions >0){
               ProposalYNQBean ynqBean;
            for(int i=0;i<numQuestions;i++){
                //hmQuestions = (HashMap) cvQuestions.get(i);
                //questionId = Integer.parseInt(hmQuestions.get("QUESTION_ID").toString());
                //answer = hmQuestions.get("ANSWER").toString();
                ynqBean = cvQuestions.get(i);
                questionId = Integer.parseInt(ynqBean.getQuestionId());
                answer = ynqBean.getAnswer();
               
                if((answer!=null)&&(answer.trim().length()!=0))
            switch (questionId) {
                //replacement for 22 starts
                  case 114:
                     
                         if(answer.equalsIgnoreCase("Y")){
                            checklistType.setIsChangeOfPDPI("Y: " + "Yes");
                            hmQuestionsBranch.put(114,"Y");}//goto(115)
                         else {
                            checklistType.setIsChangeOfPDPI("N: " + "No");
                            hmQuestionsBranch.put(114,"N");}
                  break;
                  case 115:
                        if(hmQuestionsBranch.get(114).equals("Y")){
                            checklistType.setFormerPDName(getFormerPDNameType(answer));}
                  break;
                //replacement for 22 ends
                
                //replacement for 23 starts
                  case 116:
                        if(answer.equalsIgnoreCase("Y")){
                            checklistType.setIsChangeOfInstitution("Y: " + "Yes");
                            hmQuestionsBranch.put(116,"Y");}
                        else {
                            checklistType.setIsChangeOfInstitution("N: " + "No");
                            hmQuestionsBranch.put(116,"N");}
                  break;
                  case 117:
                        if (hmQuestionsBranch.get(116).equals("Y")){
                            UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                            answer = unitDataTxnBean.getOrganisationName(answer);
                            if(answer != null && answer.length() > 60 ){
                                checklistType.setFormerInstitutionName (answer.substring(0, 60));}
                            else {
                                checklistType.setFormerInstitutionName (answer);}
                        }
                  break;   
                //replacement for 23 ends
                  
                //replacement for 16 starts  
                  case 118:
                        if(answer.equalsIgnoreCase("Y")){
                            hmQuestionsBranch.put(118,"Y");
                            checklistType.setIsInventionsAndPatents("Y: Yes");                            
                            }
                        else{
                            hmQuestionsBranch.put(118,"N");
                            checklistType.setIsInventionsAndPatents("N: No");
                            checklistType.setIsPreviouslyReported("N: No");
                            }
                  break;
                  case 119:
                      if(answer.equalsIgnoreCase("Y")){
                            if(hmQuestionsBranch.get(118).equals("Y")){
                                hmQuestionsBranch.put(119,"Y");                                
                            }
                            
                            }
                      else{
                          if(hmQuestionsBranch.get(118).equals("Y")){
                              hmQuestionsBranch.put(119,"N");
                              checklistType.setIsPreviouslyReported("N: No");
                            }
                            
                            }
                            
                  break;
                  case 120:
                      if(answer.equalsIgnoreCase("Y")){
                            if(hmQuestionsBranch.get(119) != null && hmQuestionsBranch.get(119).equals("Y")){
                                checklistType.setIsPreviouslyReported("Y: Yes");
                            }
                            
                            }
                      else{
                          if(hmQuestionsBranch.get(119) != null && hmQuestionsBranch.get(119).equals("Y")){
                              checklistType.setIsPreviouslyReported("N: No");
                            }
                            
                            }
                            
                  break;
                //replacement for 16 ends
                                   }// end of switch body
            }// end of for loop
         }//end of if condition
           //coeusqa-2568 Ends

///////////////////////////////////////////////////////////////////////////////////////////////////
          
             if (hmInfo.get("FORM_VERSION") != null)
                checklistType.setFormVersion(hmInfo.get("FORM_VERSION").toString());
             if (hmAppType != null)
                checklistType.setApplicationType(hmAppType.get("APPLICATIONTYPE").toString());
             /*commented for questionnaire questions modification starts
             if (hmInfo.get("IS_CHANGE_PI") != null)
                checklistType.setIsChangeOfPDPI( hmInfo.get("IS_CHANGE_PI").toString());   
             if (hmInfo.get("IS_CHANGE_PI").toString().equals("Yes")) {
                  checklistType.setFormerPDName(getFormerPDNameType());
             }
             if (hmInfo.get("IS_INST_CHANGE") != null)
                 checklistType.setIsChangeOfInstitution  (hmInfo.get("IS_INST_CHANGE").toString());    
             if (hmInfo.get("FORMER_INST_NAME") != null)
                 checklistType.setFormerInstitutionName (hmInfo.get("FORMER_INST_NAME").toString());    
             if (hmInfo.get("IS_INVENTIONS") !=  null)
                 checklistType.setIsInventionsAndPatents(hmInfo.get("IS_INVENTIONS").toString());  
             if (hmInfo.get("IS_PREV_REPORTED") != null)
                 checklistType.setIsPreviouslyReported (hmInfo.get("IS_PREV_REPORTED").toString());  
         commented for questionnaire questions modification ends*/
             if (!hmInfo.get("FEDERAL_ID").equals("-1"))
                 checklistType.setFederalID(hmInfo.get("FEDERAL_ID").toString());
         
           PHS398ChecklistType.CertificationExplanationType certAttachment = getCertExplanationType();
           if (certAttachment.getCertifications() != null)              
               checklistType.setCertificationExplanation(certAttachment);
          
            if (hmInfo.get("PROGRAM_INCOME") != null)
                 checklistType.setProgramIncome(hmInfo.get("PROGRAM_INCOME").toString());  
            if (hmInfo.get("PROGRAM_INCOME").toString().equals("Yes")){
                version = Integer.parseInt(hmInfo.get("VERSION").toString());   
                int numPeriods = Integer.parseInt(hmInfo.get("NUM_PERIODS").toString());

                for (int i=1; i<=numPeriods; i++){
                    hmProgIncome = s2sPHS398ChecklistTxnBean.getProgramIncome(propNumber, version, i);
              
                    if (hmProgIncome.get("AMOUNT") != null) {
                      incomeBudgetPeriodType = objFactory.createPHS398ChecklistTypeIncomeBudgetPeriodType();
                      incomeBudgetPeriodType.setAnticipatedAmount(new BigDecimal(hmProgIncome.get("AMOUNT").toString()));
                      incomeBudgetPeriodType.setBudgetPeriod(i);
                      if  (hmProgIncome.get("DESCRIPTION") != null)
                        incomeBudgetPeriodType.setSource(hmProgIncome.get("DESCRIPTION").toString());
                        checklistType.getIncomeBudgetPeriod().add(incomeBudgetPeriodType);
                    }
                }   
            }
                   
         
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398ChecklistStream","getPHS398Checklist()");
            throw new CoeusXMLException(jaxbEx.getMessage());
     
        }
        
        return checklistType;
    }
    
   /*****************************************************
       get simple Info
    *****************************************************/
     public HashMap getSimpleInfo() 
        throws CoeusXMLException, CoeusException, DBException, JAXBException{
            
       HashMap hm2Info = new HashMap();
       hm2Info = s2sPHS398ChecklistTxnBean.getChecklistInfo(propNumber);
 
       return hm2Info;
     }
     
     /*******************************
      getApplicationType
      *******************************/
       public HashMap getApplicationType() 
          throws CoeusXMLException, CoeusException, DBException, JAXBException{
            
       HashMap hmAppType = new HashMap();
       hmAppType= s2sPHS398ChecklistTxnBean.getApplicationType(propNumber);
       return hmAppType;
     }
     
     /*****************************
       getCertExplanationType - attachment
     *****************************/
     private PHS398ChecklistType.CertificationExplanationType getCertExplanationType ()
           throws CoeusXMLException,CoeusException,DBException,JAXBException{
     
     PHS398ChecklistType.CertificationExplanationType certExplanType =
         objFactory.createPHS398ChecklistTypeCertificationExplanationType();
      
     try{
          String description;
          int narrativeType;
          int moduleNum;
          
          Attachment attachment = null;
               
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
  
               String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               description = hmNarrative.get("DESCRIPTION").toString();
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
               hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
               attachment = getAttachment(hmArg);
             
              
               if ( narrativeType == 38 ) {
                   //certification explanation
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment certAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (certAttachment.getContent() != null){
                       
                        attachedFileType = getAttachedFileType(certAttachment);
                        certExplanType.setCertifications(attachedFileType);

                     }
                  } 
               }
           }  //end for
             
            
          return certExplanType;
     
     
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"OtherAttachmentsStream","getOtherAttachments()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }
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
        UtilFactory.log(ex.getMessage(),ex, "PHSModularBudgetStream", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}
     /*****************************
       getFormerPDNameType
     *****************************/
     private HumanNameDataType getFormerPDNameType (String rolodexId)
           throws CoeusXMLException,CoeusException,DBException,JAXBException{

     HumanNameDataType humanNameType = globalLibObjFactory.createHumanNameDataType();
     String lastName = "";
     String firstName = "";
     String rolodexName=s2sPHS398ChecklistTxnBean.getRolodexName(Integer.parseInt(rolodexId));

     if (rolodexName!= null) {
         int commaPos = rolodexName.indexOf(",");
         if (commaPos > 0) {
           lastName = rolodexName.substring(0,commaPos);
           firstName = rolodexName.substring(commaPos + 1);
         }else {
           lastName = rolodexName;
           }

          humanNameType.setLastName(lastName);
          if (firstName != null) humanNameType.setFirstName(firstName);
     }

    return humanNameType;

     }
   
 private Vector getQuestionAnswers()
        throws CoeusXMLException, CoeusException, DBException, JAXBException{
        Vector cvQuestionAnswers=null;
       //HashMap hm2Info = new HashMap();
       //cvQuestionAnswers = (Vector)s2sPHS398ChecklistTxnBean.getChecklistQuestionnaire(propNumber);
       //ProposalDevelopmentTxnBean proposalTxnBean = new ProposalDevelopmentTxnBean();
        ProposalDevelopmentTxnBean proposalTxnBean = new ProposalDevelopmentTxnBean();
       cvQuestionAnswers = proposalTxnBean.getProposalQuestionnaire(propNumber);
       return cvQuestionAnswers;
     }
       
    
    

 
        
    private static BigDecimal convDoubleToBigDec(double d){
        return new BigDecimal(d);
    }
    private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
    
 
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398Checklist();
    }    
     
 }
    

