/*
 * ED_SF424_SupplementStream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import gov.grants.apply.forms.ed_sf424_supplement_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
import java.util.*;
import javax.xml.bind.JAXBException;

public class ED_SF424_SupplementStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.ed_sf424_supplement_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globallibraryObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    
    private ProposalDevelopmentTxnBean propDevTxnBean;
    
    private String propNumber;
    private boolean isNonMITPerson = true;
    private UtilFactory utilFactory;   
    private HashMap hmName;    
    private ED_SF424_SupplementTxnBean edSF424SupplementTxnBean;
    
    /** Creates a new instance of ED_SF424_SupplementStream */
    public ED_SF424_SupplementStream() {
        objFactory = new gov.grants.apply.forms.ed_sf424_supplement_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        edSF424SupplementTxnBean = new ED_SF424_SupplementTxnBean();
    }
    
         private  EDSF424SupplementType getED_SF424_Supplement() throws CoeusXMLException,CoeusException,DBException,JAXBException{
         EDSF424SupplementType  edSF424Supplement = objFactory.createEDSF424Supplement();
         try{
              edSF424Supplement.setFormVersion("1.0"); 
              
              // box 1 set project director
             edSF424Supplement.setProjectDirector(getProjectDirector());
             //COEUSQA-3665 - START
             Vector vctAnswerList = propDevTxnBean.getProposalQuestionnaire(propNumber);
             ProposalYNQBean ynqBean = getQuestionnaireAnswer("133", vctAnswerList);
             if (ynqBean != null) {
                 if(ynqBean.getAnswer().equalsIgnoreCase("Y")){
                    edSF424Supplement.setIsNoviceApplicant("Y: Yes");
                 }else if(ynqBean.getAnswer().equalsIgnoreCase("N")){
                     edSF424Supplement.setIsNoviceApplicant("N: No");
                 }else {
                     edSF424Supplement.setIsNoviceApplicant("NA: Not Applicable");
                 }
             }
              //box 2 set Applicant Experience
              /*hmName = null;
              hmName = edSF424SupplementTxnBean.getApplicantExp(propNumber);
              if (hmName != null && hmName.get("APPLICANT_EXPERIENCE") != null){
                  edSF424Supplement.setIsNoviceApplicant(hmName.get("APPLICANT_EXPERIENCE").toString());                  
              }
              */
              //box 3 set Human Subjects Research
              //COEUSQA-3665 - END
              Vector vecSpecialReview = propDevTxnBean.getProposalSpecialReview(propNumber);
              ProposalSpecialReviewFormBean specialReviewBean;
              String comments;                    
        
              if (vecSpecialReview != null) { 
                  for (int vecCount = 0 ; vecCount < vecSpecialReview.size() ; vecCount++) {
                      specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);
                      if (specialReviewBean != null){
                             if (specialReviewBean.getSpecialReviewCode() == 1){
                                    edSF424Supplement.setIsHumanResearch("Yes");
                                    if (specialReviewBean.getApprovalCode() == 4) {
                                         //exempt
                                            edSF424Supplement.setIsHumanResearchExempt("Yes");
            //                             if (specialReviewBean.getComments() != null)
                                             EDSF424SupplementType.ExemptionsNumberType exemptionsNumber 
                                                            = objFactory.createEDSF424SupplementTypeExemptionsNumberType();
                                             exemptionsNumber.setIsHumanResearchExempt("Yes"); 
                                             if (specialReviewBean.getComments() != null)
                                                    exemptionsNumber.setValue(specialReviewBean.getComments());
                                             edSF424Supplement.setExemptionsNumber(exemptionsNumber);
                                     }else{
                                             edSF424Supplement.setIsHumanResearchExempt("No");
                                             //from the form looks like only when there is no exemption, 
                                             //the Assurance number is needed.
                                             hmName = null;
                                             hmName = edSF424SupplementTxnBean.getHumanAssurance(propNumber);
                                             if (hmName != null ){
                                                 EDSF424SupplementType.AssuranceNumberType assuranceNumber 
                                                            = objFactory.createEDSF424SupplementTypeAssuranceNumberType();
                                                 assuranceNumber.setIsHumanResearchExempt("No"); 
                                                 if (hmName.get("HUMAN_SUB_ASSURANCE") != null)
                                                        assuranceNumber.setValue(hmName.get("HUMAN_SUB_ASSURANCE").toString());
                                                 edSF424Supplement.setAssuranceNumber(assuranceNumber);
                                             }
                                     }
                      }else
                                    edSF424Supplement.setIsHumanResearch("No");                                                          
                      }
                  }
              }else{
                  edSF424Supplement.setIsHumanResearch("No");                   
              }
              
              
              //get narraritive file to set attachment
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


                   if ( narrativeType == 54 ) { 
                       //ED_SF424_Supplement_Attachment
                      if (attachment == null) {
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                        Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                        if (narrativeAttachment.getContent() != null){
                            gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
                            attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                            edSF424Supplement.setAttachment(attachedFileType);

                         }
                      } 
                   }
              } 
              
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"ED_SF424_SupplementV1_1Stream","getED_SF424_Supplement()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }    
        
        return edSF424Supplement;
     }
     
     private ContactPersonDataType getProjectDirector() throws CoeusException, DBException, javax.xml.bind.JAXBException  {  
         ContactPersonDataType contactPersonData = globallibraryObjFactory.createContactPersonDataType();
         try{
                ProposalPersonFormBean piPersonBean = getPIPersonBean();
                if (piPersonBean != null){
                    
                    contactPersonData.setName(getPersonName(piPersonBean));
                    
                    if (piPersonBean.getOfficePhone() != null)
                        contactPersonData.setPhone(piPersonBean.getOfficePhone());
                    
                    if (piPersonBean.getEmailAddress() != null)
                        contactPersonData.setEmail(piPersonBean.getEmailAddress());
                    
                    if (piPersonBean.getDirTitle() != null){
                        if (piPersonBean.getDirTitle().length() > 45 )
                            contactPersonData.setTitle(piPersonBean.getDirTitle().substring(0,45));
                        else    
                            contactPersonData.setTitle(piPersonBean.getDirTitle());
                    }
                   
                    contactPersonData.setAddress(getAddress(null,piPersonBean));
                    
                     if (piPersonBean.getFaxNumber() != null)
                        contactPersonData.setFax(piPersonBean.getFaxNumber());
                }
                
         }catch(JAXBException jaxbEx){
                utilFactory.log(jaxbEx.getMessage(),jaxbEx,"ED_SF424_SupplementV1_1Stream","getProjectDirector()");
                throw new CoeusXMLException(jaxbEx.getMessage());
         }
         return contactPersonData;
     }
    
     private ProposalPersonFormBean getPIPersonBean()  throws CoeusException, DBException, javax.xml.bind.JAXBException  {  
            
        ProposalInvestigatorFormBean propInvBean = null;
        Vector vecInvestigators;
        String personID = null;
        vecInvestigators = propDevTxnBean.getProposalInvestigatorDetails(propNumber);
        int vSize = vecInvestigators==null?0:vecInvestigators.size();
    
        for(int vecCount = 0;vecCount<vSize; vecCount++){
            propInvBean = (ProposalInvestigatorFormBean) vecInvestigators.get(vecCount) ;
            if (propInvBean.isPrincipleInvestigatorFlag()) {
                personID = propInvBean.getPersonId();
                isNonMITPerson = propInvBean.isNonMITPersonFlag();
                
                break;
            }                         
         } 
        ProposalPersonFormBean pIBean = 
            propDevTxnBean.getProposalPersonDetails(propNumber, personID);
        return pIBean;
    }
     
      private HumanNameDataType  getPersonName(ProposalPersonFormBean pBean)
                  throws JAXBException,  CoeusException,DBException{
   
    HumanNameDataType nameType = globallibraryObjFactory.createHumanNameDataType(); 
   
    nameType.setFirstName(pBean.getFirstName());  
    nameType.setLastName(pBean.getLastName());
    if(pBean.getMiddleName() != null)
     nameType.setMiddleName(UtilFactory.convertNull(pBean.getMiddleName()));
   
    return nameType;
   }
   
    private AddressDataType getAddress(RolodexDetailsBean rolodexDetailsBean,
                                           DepartmentPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
                      
    AddressDataType addressType = globallibraryObjFactory.createAddressDataType();
    
    if (rolodexDetailsBean != null) {
        //rolodex
        addressType.setStreet1(rolodexDetailsBean.getAddress1());
        if (rolodexDetailsBean.getAddress2() != null)
            addressType.setStreet2(rolodexDetailsBean.getAddress2());
        addressType.setCity(rolodexDetailsBean.getCity());
        if (rolodexDetailsBean.getState() != null){
            hmName = null;
            hmName = edSF424SupplementTxnBean.getStateName(rolodexDetailsBean.getState());
            if (hmName != null )
              addressType.setState(hmName.get("STATE_NAME").toString());
        }
        if (rolodexDetailsBean.getPostalCode() != null)
            addressType.setZipCode(UtilFactory.convertNull(rolodexDetailsBean.getPostalCode()));
        if (rolodexDetailsBean.getCountry() != null ){
            hmName = null;
            hmName = edSF424SupplementTxnBean.getCountryName(rolodexDetailsBean.getCountry());
            if (hmName != null )
              addressType.setCountry(hmName.get("COUNTRY_NAME").toString());
        }
    } else {
        //department person 
         addressType.setStreet1(personBean.getAddress1());

         if (personBean.getAddress2() != null)
             addressType.setStreet2(personBean.getAddress2());

         addressType.setCity(personBean.getCity());
         if (personBean.getState() != null) {
            hmName = null;
            hmName = edSF424SupplementTxnBean.getStateName(personBean.getState());
            if (hmName != null )
              addressType.setState(hmName.get("STATE_NAME").toString());
         }

         if (personBean.getPostalCode() != null){
             addressType.setZipCode(personBean.getPostalCode());
         }
         
         if (personBean.getCountryCode() != null ){
            hmName = null;
            hmName = edSF424SupplementTxnBean.getCountryName(personBean.getCountryCode());
            if (hmName != null )
              addressType.setCountry(hmName.get("COUNTRY_NAME").toString());
         }              
    }
    return addressType;
   }
    
     public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getED_SF424_Supplement();
    }
    //COEUSQA-3665 - START
    private ProposalYNQBean getQuestionnaireAnswer(String questionID, Vector vecYNQ) throws CoeusException, DBException {
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
        return proposalYNQBean;
    }
    //COEUSQA-3665 - END
}
