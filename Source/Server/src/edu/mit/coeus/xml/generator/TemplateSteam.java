/*
 * templateSteam.java
 *
 * Created on April 19, 2005, 3:47 PM
 */

package edu.mit.coeus.xml.generator;


import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.admin.bean.*;
import edu.mit.coeus.award.AwardLabelConstants;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.xml.bean.template.*;
import java.util.*;
import javax.xml.bind.JAXBException;


/**
 * this file is created for case 1632 -- awardTemplateReport
 * @author  jenlu
 */
public class TemplateSteam extends ReportBaseStream {
    private AwardTemplateBean awardTemplateBean;
    private CoeusVector cvTemplComments;
    private CoeusVector cvComments;
    private CoeusVector cvTemplContacts;
    private CoeusVector cvReportClass;
    private CoeusVector cvTemplReport;
    private CoeusVector cvTemplateDocumentTerms;
    private CoeusVector cvTemplateEquipmentTerms;
    private CoeusVector cvTemplateInventionTerms;
    private CoeusVector cvTemplateApprovalTerms;
    private CoeusVector cvTemplatePropertyTerms;
    private CoeusVector cvTemplatePublicationTerms;
    private CoeusVector cvTemplateRightsTerms;
    private CoeusVector cvTemplateSubcontractTerms;
    private CoeusVector cvTemplateTravelTerms;
    private CoeusVector cvFrequency;
    private CoeusVector cvContactTypes;
    private CoeusVector cvBasisPmt;
    private CoeusVector cvMethodOfPmt;
    private CoeusVector cvFreqDesc;
    private String sponsorName = "";
    
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private int templateCode;
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.template";
    
    /** Creates a new instance of templateSteam */
    public TemplateSteam() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    public org.w3c.dom.Document getStream(int templateCode) throws DBException,CoeusException {
        this.templateCode = templateCode;
        getTemplateData();  
        
        TemplateType templateType = getTemplate();
        return xmlGenerator.marshelObject(templateType,packageName);
     }
    
     public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
         int templateCode = Integer.parseInt(params.get("TEMPLATE_CODE").toString());
         this.templateCode = templateCode;
         getTemplateData();
         TemplateType templateType = getTemplate();
         return templateType;
     }
    
    private void getTemplateData()throws CoeusException, DBException{
        TemplateTxnBean templateTxnBean = new TemplateTxnBean();;
    	AwardTxnBean awardTxnBean = new AwardTxnBean();
        AdminTxnBean adminTxnBean = new AdminTxnBean();
        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();        
        AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
        
        awardTemplateBean = templateTxnBean.getTemplateDataForTemplateCode(templateCode);
        
        // Getting the frequency
        cvFrequency = adminTxnBean.getFrequency();
        
       // Getting the method of payment
        Vector veMethodOfPmt = departmentPersonTxnBean.getArgumentCodeDescription("METHOD OF PAYMENT");
        cvMethodOfPmt =new CoeusVector();
        cvMethodOfPmt.addAll(veMethodOfPmt);
       
        // Getting the basis of payment
        Vector veBasisPmt = departmentPersonTxnBean.getArgumentCodeDescription("BASIS OF PAYMENT");
        cvBasisPmt = new CoeusVector();
        cvBasisPmt.addAll(veBasisPmt);
        
        //Getting Pmt/Inv Freq
        Vector vefreqDesc = departmentPersonTxnBean.getArgumentCodeDescription("FREQUENCY");
        cvFreqDesc = new CoeusVector();
        cvFreqDesc.addAll(vefreqDesc);            
            
        // Getting the asponsor name        
        if(awardTemplateBean.getPrimeSponsorCode() != null){
            sponsorName = awardTxnBean.getSponsorName(awardTemplateBean.getPrimeSponsorCode());
        }
        
        // Getting the comments for template (with out Comment code description)
        cvTemplComments = templateTxnBean.getAwardTemplateComments(templateCode);
        // Getting the comments (with Comment code description)
        cvComments = awardLookUpDataTxnBean.getCommentType();
        
        // Getting the template contacts
        cvTemplContacts = templateTxnBean.getAwardTemplateContactsForTemplateCode(templateCode);
        // Getting contact types 
        cvContactTypes = awardLookUpDataTxnBean.getContactTypes();

        // Getting the template equipment terms for template code 
        cvTemplateEquipmentTerms = templateTxnBean.getTemplateEquipmentTermsForTemplateCode(templateCode);
       
        // Getting the template invention terms for template code
        cvTemplateInventionTerms = templateTxnBean.getTemplateInventionTermsForTemplateCode(templateCode);
        
        // Getting the template approval terms for template code
        cvTemplateApprovalTerms = templateTxnBean.getTemplatePriorApprTermsForTemplateCode(templateCode);
        
        // Getting the template property terms
        cvTemplatePropertyTerms = templateTxnBean.getTemplatePropertyTermsForTemplateCode(templateCode);
        
        // Getting the template publication terms
        cvTemplatePublicationTerms = templateTxnBean.getTemplatePublicationTermsForTemplateCode(templateCode);
        
       // Getting the document terms for template code 
        cvTemplateDocumentTerms = templateTxnBean.getTemplateDocumentTermsForTemplateCode(templateCode);
        
        // Getting the template rights in data terms
        cvTemplateRightsTerms = templateTxnBean.getTemplateRightsInDataTermsForTemplateCode(templateCode);
        
        // Getting the template subcontract terms
        cvTemplateSubcontractTerms = templateTxnBean.getTemplateSubcontractTermsForTemplateCode(templateCode);
        
        // Getting the travel terms for template 
        cvTemplateTravelTerms = templateTxnBean.getTemplateTravelTermsForTemplateCode(templateCode);
        
        // Getting the report class 
        cvReportClass = awardLookUpDataTxnBean.getReportClass();
        
        // Getting the report terms
        cvTemplReport = templateTxnBean.getAwardTemplateReportTerms(templateCode);
        
     }
      
    private TemplateType getTemplate() throws CoeusXMLException,CoeusException,DBException{
        TemplateType template = null;
        try{
              template = objFactory.createTemplate();
              template.setTemplateMaster(getTemplateMaster());
              template.getContact().addAll(getContacts());
              template.getComment().addAll(getComments());
              template.getTerm().addAll(getTerms());
              template.getReport().addAll(getReports());
              template.setSchoolInfo(getSchoolInfoType());
              
        }catch(JAXBException jaxbEx){
              
        }
        return template;
    }
      
    private TemplateMasterData getTemplateMaster()throws JAXBException,CoeusException,DBException{
          TemplateMasterData templateMasterData = objFactory.createTemplateMasterData();
          templateMasterData.setTemplateCode(awardTemplateBean.getTemplateCode());
          templateMasterData.setDescription(UtilFactory.convertNull(awardTemplateBean.getDescription()));
          Calendar currentDate = Calendar.getInstance();
          currentDate.setTime(new Date());
          templateMasterData.setCurrentDate(currentDate);
          //set status
          if (awardTemplateBean.getStatusCode() != 0){
              TemplateStatusType templateStatusType = objFactory.createTemplateStatusType();
              templateStatusType.setStatusCode(awardTemplateBean.getStatusCode());
              templateStatusType.setStatusDesc(UtilFactory.convertNull(awardTemplateBean.getStatusDescription()));
              templateMasterData.setTemplateStatus(templateStatusType);              
          }
         
          //set prime sponsor
          if (awardTemplateBean.getPrimeSponsorCode()!= null){
              SponsorType sponsorType = objFactory.createSponsorType();
              sponsorType.setSponsorCode(awardTemplateBean.getPrimeSponsorCode());
              sponsorType.setSponsorName(sponsorName);
              templateMasterData.setPrimeSponsor(sponsorType);
          }
          
          //Set Non-Competing
          if(awardTemplateBean.getNonCompetingContPrpslDue() != 0 ){
                NonCompetingContType nonCompetingContType = objFactory.createNonCompetingContType();
                nonCompetingContType.setNonCompetingContCode(""+awardTemplateBean.getNonCompetingContPrpslDue());
		Equals eqNoncompeting = new Equals("code",""+awardTemplateBean.getNonCompetingContPrpslDue());
            	CoeusVector cvNonCompeting = cvFrequency.filter(eqNoncompeting);
                if (cvNonCompeting!=null && cvNonCompeting.size()>0) {
                    ComboBoxBean nonCompetingBean = (ComboBoxBean)cvNonCompeting.get(0);
                    nonCompetingContType.setNonCompetingContDesc(nonCompetingBean.getDescription());
                }
                templateMasterData.setNonCompetingCont(nonCompetingContType);
              
          }
          
          //Set Competing Renewal
          if (awardTemplateBean.getCompetingRenewalPrpslDue()!= 0 ){
              CompetingRenewalType competingRenewalType = objFactory.createCompetingRenewalType();
              competingRenewalType.setCompetingRenewalCode(""+awardTemplateBean.getCompetingRenewalPrpslDue());
              Equals eqCompeting = new Equals("code",""+awardTemplateBean.getCompetingRenewalPrpslDue());
              CoeusVector cvCompeting = cvFrequency.filter(eqCompeting);
              if (cvCompeting!=null && cvCompeting.size()>0) {
                  ComboBoxBean competingBean = (ComboBoxBean)cvCompeting.get(0);
                  competingRenewalType.setCompetingRenewalDesc(competingBean.getDescription());
              }              
              templateMasterData.setCompetingRenewal(competingRenewalType);
            
          }
          
          //Set the Payment Basis  
          if (awardTemplateBean.getBasisOfPaymentCode()!= 0){
              BasisPaymentType basisPaymentType = objFactory.createBasisPaymentType();
              basisPaymentType.setBasisPaymentCode(""+awardTemplateBean.getBasisOfPaymentCode());
              Equals eqBasis = new Equals("code",""+awardTemplateBean.getBasisOfPaymentCode());
              CoeusVector cvBasis = cvBasisPmt.filter(eqBasis);
              if (cvBasis!=null && cvBasis.size()>0) {
                  ComboBoxBean basisBean = (ComboBoxBean)cvBasis.get(0);
                  basisPaymentType.setBasisPaymentDesc(basisBean.getDescription());
              }              
              templateMasterData.setBasisPayment(basisPaymentType);
          }
          
          //Set the Payment Method.
          if (awardTemplateBean.getMethodOfPaymentCode()!= 0){
              PaymentMethodType paymentMethodType = objFactory.createPaymentMethodType();
              paymentMethodType.setPaymentMethodCode(""+awardTemplateBean.getMethodOfPaymentCode());
              Equals eqMethod = new Equals("code",""+awardTemplateBean.getMethodOfPaymentCode());
              CoeusVector cvMethod = cvMethodOfPmt.filter(eqMethod);
              if (cvMethod!=null && cvMethod.size()>0) {
                  ComboBoxBean methodBean = (ComboBoxBean)cvMethod.get(0);
                  paymentMethodType.setPaymentMethodDesc(methodBean.getDescription());
              }
              templateMasterData.setPaymentMethod(paymentMethodType);
              
          }
          
          //Set the Payment/Invoice Freq
          if (awardTemplateBean.getPaymentInvoiceFreqCode()!= 0){
              PaymentFreqType paymentFreqType =objFactory.createPaymentFreqType();
              paymentFreqType.setPaymentFreqCode(""+awardTemplateBean.getPaymentInvoiceFreqCode());
              Equals eqFreq = new Equals("code",""+awardTemplateBean.getPaymentInvoiceFreqCode());
              CoeusVector cvFreq = cvFreqDesc.filter(eqFreq);
              if (cvFreq!=null && cvFreq.size()>0) {
                  ComboBoxBean freqBean = (ComboBoxBean)cvFreq.get(0);
                  paymentFreqType.setPaymentFreqDesc(freqBean.getDescription());
              }
              templateMasterData.setPaymentFreq(paymentFreqType);
          }
          
          if (awardTemplateBean.getInvoiceNoOfCopies()!= 0){
              templateMasterData.setInvoiceCopies(awardTemplateBean.getInvoiceNoOfCopies());              
          }
          
          if (awardTemplateBean.getFinalInvoiceDue()!= null ){
              templateMasterData.setFinalInvoiceDue(awardTemplateBean.getFinalInvoiceDue().intValue());  
          }
          
          //set InvoiceEnstructions
//           if (awardTemplateBean.getInvoiceInstructions()!= null ){
//              templateMasterData.setInvoiceInstructions(awardTemplateBean.getInvoiceInstructions());  
//          }
          if (cvTemplComments != null && cvTemplComments.size() > 0 ){
              Equals eqComment = new Equals("commentCode",new Integer(1));
              CoeusVector cvInvoice = cvTemplComments.filter(eqComment);
              if (cvInvoice != null && cvInvoice.size() > 0 ){
                  AwardTemplateCommentsBean awardTemplateCommentsBean = (AwardTemplateCommentsBean)cvInvoice.get(0);
                  templateMasterData.setInvoiceInstructions(awardTemplateCommentsBean.getComments());             
              }
          }
          
          
          
          
          return templateMasterData ;
          
      }
    private Vector getContacts()throws JAXBException,CoeusException,DBException{
        Vector vcContact = new Vector();
        AwardTemplateContactsBean awardTemplateContactsBean;
        int contactSize = cvTemplContacts==null?0:cvTemplContacts.size();
        for (int contactIndex = 0; contactIndex < contactSize; contactIndex++){
            ContactType contactType = objFactory.createContactType();
            RolodexDetailsType rolodexDetailsType = objFactory.createRolodexDetailsType();
            awardTemplateContactsBean = (AwardTemplateContactsBean)cvTemplContacts.elementAt(contactIndex);
            if(awardTemplateContactsBean.getRolodexId() != 0){
                rolodexDetailsType.setRolodexId(""+awardTemplateContactsBean.getRolodexId());
            }
            if(awardTemplateContactsBean.getLastName() != null){
                rolodexDetailsType.setLastName(awardTemplateContactsBean.getLastName());
            }
            if(awardTemplateContactsBean.getFirstName() != null){
                rolodexDetailsType.setFirstName(awardTemplateContactsBean.getFirstName());
            }
            if(awardTemplateContactsBean.getMiddleName() != null){
                rolodexDetailsType.setMiddleName(awardTemplateContactsBean.getMiddleName());
            }
            if(awardTemplateContactsBean.getSuffix() != null){
                rolodexDetailsType.setSuffix(awardTemplateContactsBean.getSuffix());
            }
            if(awardTemplateContactsBean.getPrefix() != null){
                rolodexDetailsType.setPrefix(awardTemplateContactsBean.getPrefix());
            }
            if(awardTemplateContactsBean.getTitle() != null){
                rolodexDetailsType.setTitle(awardTemplateContactsBean.getTitle());
            }
            if(awardTemplateContactsBean.getOrganization() != null){
                rolodexDetailsType.setOrganization(awardTemplateContactsBean.getOrganization());
            }
            if(awardTemplateContactsBean.getAddress1() != null){
                rolodexDetailsType.setAddress1(awardTemplateContactsBean.getAddress1());
            }
             if(awardTemplateContactsBean.getAddress2() != null){
                rolodexDetailsType.setAddress2(awardTemplateContactsBean.getAddress2());
            }
             if(awardTemplateContactsBean.getAddress3() != null){
                rolodexDetailsType.setAddress3(awardTemplateContactsBean.getAddress3());
            }
            if(awardTemplateContactsBean.getFaxNumber() != null){
                rolodexDetailsType.setFax(awardTemplateContactsBean.getFaxNumber());
            }
            if(awardTemplateContactsBean.getEmailAddress() != null){
                rolodexDetailsType.setEmail(awardTemplateContactsBean.getEmailAddress());
            }
            if(awardTemplateContactsBean.getCity() != null){
                rolodexDetailsType.setCity(awardTemplateContactsBean.getCity());
            }
            if(awardTemplateContactsBean.getCounty() != null){
                rolodexDetailsType.setCounty(awardTemplateContactsBean.getCounty());
            }
            if(awardTemplateContactsBean.getState() != null){
                rolodexDetailsType.setStateCode(awardTemplateContactsBean.getState());
            }
            if(awardTemplateContactsBean.getStateName() != null){
                rolodexDetailsType.setStateDescription(awardTemplateContactsBean.getStateName());
            }
            if(awardTemplateContactsBean.getPostalCode() != null){
                rolodexDetailsType.setPostalCode(awardTemplateContactsBean.getPostalCode());
            }
            if(awardTemplateContactsBean.getComments() != null){
                rolodexDetailsType.setComments(awardTemplateContactsBean.getComments());
            }
            if(awardTemplateContactsBean.getPhoneNumber() != null){
                rolodexDetailsType.setPhoneNumber(awardTemplateContactsBean.getPhoneNumber());
            }
            if(awardTemplateContactsBean.getCountryCode() != null){
                rolodexDetailsType.setCountryCode(awardTemplateContactsBean.getCountryCode());
            }
            if(awardTemplateContactsBean.getContactTypeDescription() != null){
                rolodexDetailsType.setCountryDescription(awardTemplateContactsBean.getContactTypeDescription());
            }
            if(awardTemplateContactsBean.getSponsorCode() != null){
                rolodexDetailsType.setSponsorCode(awardTemplateContactsBean.getSponsorCode());
            }
            if(awardTemplateContactsBean.getSponsorName() != null){
                rolodexDetailsType.setSponsorName(awardTemplateContactsBean.getSponsorName());
            }
            if (rolodexDetailsType != null){
                contactType.setRolodexDetails(rolodexDetailsType);
            }
            if(awardTemplateContactsBean.getContactTypeCode()!= 0){
                contactType.setContactTypeCode(awardTemplateContactsBean.getContactTypeCode());
                Equals eqType = new Equals("Code",""+awardTemplateContactsBean.getContactTypeCode());
                CoeusVector cvType = cvContactTypes.filter(eqType);
                if (cvType != null && cvType.size() >0){
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvType.get(0);
                    contactType.setContactTypeDesc(comboBoxBean.getDescription());
                }                               
            }
//            if(awardTemplateContactsBean.getContactTypeDescription()!= null){
//                contactType.setContactTypeDesc(awardTemplateContactsBean.getContactTypeDescription());
//            }
            vcContact.addElement(contactType);
        }
        return vcContact;
           
    }
    
    private Vector getComments()throws JAXBException,CoeusException,DBException{
        Vector vcComment = new Vector();
        AwardTemplateCommentsBean awardTemplateCommentsBean;
        int commentSize = cvTemplComments==null?0:cvTemplComments.size();
        for (int contactIndex = 0; contactIndex < commentSize; contactIndex++){
            CommentType commentType = objFactory.createCommentType();
            awardTemplateCommentsBean = (AwardTemplateCommentsBean)cvTemplComments.get(contactIndex);
            if (awardTemplateCommentsBean.getCommentCode() != 0 && awardTemplateCommentsBean.getCommentCode() != 1 ){
                commentType.setCommentCode(awardTemplateCommentsBean.getCommentCode()); 
                Equals eqComment = new Equals("commentCode",new Integer(awardTemplateCommentsBean.getCommentCode()));
                CoeusVector cvComm = cvComments.filter(eqComment);
                if (cvComm!=null && cvComm.size()>0) {
                    CommentTypeBean  commentTypeBean = (CommentTypeBean)cvComm.get(0);
                    commentType.setDescription(commentTypeBean.getDescription());
                }
            }
            if (awardTemplateCommentsBean.getComments() != null && awardTemplateCommentsBean.getCommentCode() != 1 ){
                commentType.setComments(awardTemplateCommentsBean.getComments()); 
            }
            vcComment.addElement(commentType);
        }
        return vcComment;
    }
    
    private Vector getTerms()throws JAXBException,CoeusException,DBException{
        Vector vcTerm = new Vector();               
        
        // Setting the template equipment terms
        if (cvTemplateEquipmentTerms != null && cvTemplateEquipmentTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.EQUIPMENT_APPROVAL);
            termType.getTermDetails().addAll(getTermDetail(cvTemplateEquipmentTerms));
            vcTerm.addElement(termType);
        }
        
        // Setting the template invention terms
        if (cvTemplateInventionTerms != null && cvTemplateInventionTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.INVENTION);
            termType.getTermDetails().addAll(getTermDetail(cvTemplateInventionTerms));
            vcTerm.addElement(termType);
        }
         
        // Setting the template Other approval... terms
        if (cvTemplateApprovalTerms != null && cvTemplateApprovalTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.OTHER_REQUIREMENT);
            termType.getTermDetails().addAll(getTermDetail(cvTemplateApprovalTerms));
            vcTerm.addElement(termType);
        }
         // Setting the template property terms
        if (cvTemplatePropertyTerms != null && cvTemplatePropertyTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.PROPERTY);
            termType.getTermDetails().addAll(getTermDetail(cvTemplatePropertyTerms));
            vcTerm.addElement(termType);
                    
        }
        // Setting the template publication terms
        if (cvTemplatePublicationTerms != null && cvTemplatePublicationTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.PUBLICATION);
            termType.getTermDetails().addAll(getTermDetail(cvTemplatePublicationTerms));
            vcTerm.addElement(termType);
        }
        // Setting the template referenced documents terms
        if (cvTemplateDocumentTerms != null && cvTemplateDocumentTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.REFERENCED_DOCUMENTS);
            termType.getTermDetails().addAll(getTermDetail(cvTemplateDocumentTerms));
            vcTerm.addElement(termType);
        }
        // Setting the template right in data terms
        if (cvTemplateRightsTerms != null && cvTemplateRightsTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.RIGHTS_IN_DATA);
            termType.getTermDetails().addAll(getTermDetail(cvTemplateRightsTerms));
            vcTerm.addElement(termType);
        }
        // Setting the template subcontract terms
        if (cvTemplateSubcontractTerms != null && cvTemplateSubcontractTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.SUBCONTRACT_APPROVAL);
            termType.getTermDetails().addAll(getTermDetail(cvTemplateSubcontractTerms));
            vcTerm.addElement(termType);
        }
        // Setting the template travel terms
        if (cvTemplateTravelTerms != null && cvTemplateTravelTerms.size() > 0 ){
            TermType termType = objFactory.createTermType();
            termType.setDescription(AwardLabelConstants.TRAVEL);
            termType.getTermDetails().addAll(getTermDetail(cvTemplateTravelTerms));
            vcTerm.addElement(termType);
        }
        return vcTerm;
    }
    
    private Vector getTermDetail(CoeusVector cvTemplTerm)throws JAXBException,CoeusException,DBException{
        int termSize = cvTemplTerm==null?0:cvTemplTerm.size();
        Vector vcTermDetail = new Vector();
        TemplateTermsBean templateTermsBean;
        for (int termIndex = 0; termIndex < termSize; termIndex++){
            TermDetailsType termDetailsType = objFactory.createTermDetailsType();
            templateTermsBean = (TemplateTermsBean)cvTemplTerm.get(termIndex);
            termDetailsType.setTermCode(templateTermsBean.getTemplateCode());
            termDetailsType.setTermDescription(templateTermsBean.getTermsDescription());
            vcTermDetail.addElement(termDetailsType);
        }
        return vcTermDetail;
    }
    
     private Vector getReports()throws JAXBException,CoeusException,DBException{
        Vector vcReport = new Vector();
        int reportClassSize = cvReportClass==null?0:cvReportClass.size();
        int reportSize = cvTemplReport==null?0:cvTemplReport.size();
        if (reportSize > 0) {
            cvTemplReport.sort("reportClassCode");
            for (int reportClassIndex = 0; reportClassIndex < reportClassSize ; reportClassIndex++){
               ComboBoxBean classBean = (ComboBoxBean)cvReportClass.get(reportClassIndex);
               Equals eqReportClass = new Equals("reportClassCode",new Integer(classBean.getCode())); 
               CoeusVector cvFilteredReport = cvTemplReport.filter(eqReportClass);
               int filteredReportSize =cvFilteredReport==null? 0:cvFilteredReport.size();
               AwdTemplateRepTermsBean awdTemplateRepTermsBean ;
               if (filteredReportSize > 0){
                 Vector veRepTermDetails = new Vector();
                 //start csae 2139
//                 for (int filteredRepIdx = 0 ; filteredRepIdx < cvFilteredReport.size(); filteredRepIdx ++){
                 for (int filteredRepIdx = 0 ; filteredRepIdx < cvFilteredReport.size(); ){
                //end case 2139
                   ReportTermDetailsType reportTermDetailsType = objFactory.createReportTermDetailsType();
                   awdTemplateRepTermsBean = (AwdTemplateRepTermsBean)cvFilteredReport.get(filteredRepIdx);
                   if (awdTemplateRepTermsBean.getDueDate()!= null){
                       Calendar dueDate = Calendar.getInstance();
                       dueDate.setTime(awdTemplateRepTermsBean.getDueDate());
                       reportTermDetailsType.setDueDate(dueDate);
                   }
                   reportTermDetailsType.setFrequencyBaseCode(awdTemplateRepTermsBean.getFrequencyBaseCode());
                   reportTermDetailsType.setFrequencyBaseDesc(awdTemplateRepTermsBean.getFrequencyBaseDescription());
                   reportTermDetailsType.setFrequencyCode(awdTemplateRepTermsBean.getFrequencyCode());
                   reportTermDetailsType.setFrequencyCodeDesc(awdTemplateRepTermsBean.getFrequencyDescription());
                   reportTermDetailsType.setOSPDistributionCode(awdTemplateRepTermsBean.getOspDistributionCode());
                   reportTermDetailsType.setOSPDistributionDesc(awdTemplateRepTermsBean.getOspDistributionDescription());
                   reportTermDetailsType.setReportCode(awdTemplateRepTermsBean.getReportCode());
                   reportTermDetailsType.setReportCodeDesc(awdTemplateRepTermsBean.getReportDescription());
                   reportTermDetailsType.setReportClassCode(awdTemplateRepTermsBean.getReportClassCode());
                   reportTermDetailsType.setReportClassDesc(classBean.getDescription());
                   // grop mailCopiesType.
                   Equals eqReportCode = new Equals("aw_ReportCode", new Integer(awdTemplateRepTermsBean.getReportCode()));
                   Equals eqFrequencyCode = new Equals("aw_FrequencyCode",new Integer(awdTemplateRepTermsBean.getFrequencyCode()));
                   Equals eqFrequencyBaseCode = new Equals("aw_FrequencyBaseCode",new Integer(awdTemplateRepTermsBean.getFrequencyBaseCode()));
                   Equals eqOspDistributionCode = new Equals("aw_OspDistributionCode", new Integer(awdTemplateRepTermsBean.getOspDistributionCode()));
                   Equals eqDueDate = new Equals("dueDate", awdTemplateRepTermsBean.getDueDate());
//                   Equals eqReportClassCode = new Equals("aw_ReportClassCode", new Integer(awdTemplateRepTermsBean.getReportClassCode()));
                   And eqAll = new And(new And(new And(eqReportCode, eqReportClass),new And(eqFrequencyBaseCode, eqOspDistributionCode)), new And(eqDueDate,eqFrequencyCode));
                   CoeusVector cvMailCopyType;
                   cvMailCopyType = cvFilteredReport.filter(eqAll);
                   if (cvMailCopyType !=null && cvMailCopyType.size()>0){
                       for (int mailIndex = 0; mailIndex < cvMailCopyType.size(); mailIndex++ ){
                           ReportTermDetailsType.MailCopiesType mailCopiesType = objFactory.createReportTermDetailsTypeMailCopiesType();
                           awdTemplateRepTermsBean =(AwdTemplateRepTermsBean)cvMailCopyType.get(mailIndex);
                           mailCopiesType.setNumberOfCopies(""+awdTemplateRepTermsBean.getNumberOfCopies());
                           mailCopiesType.setRolodexId(""+awdTemplateRepTermsBean.getRolodexId());
                           mailCopiesType.setContactTypeCode(awdTemplateRepTermsBean.getContactTypeCode());
                           mailCopiesType.setContactTypeDesc(awdTemplateRepTermsBean.getContactTypeDescription());
                           reportTermDetailsType.getMailCopies().add(mailCopiesType);
                       }
                       
                       NotEquals notReportCode = new NotEquals("aw_ReportCode", new Integer(awdTemplateRepTermsBean.getReportCode()));
                       NotEquals notFrequencyCode = new NotEquals("aw_FrequencyCode",new Integer(awdTemplateRepTermsBean.getFrequencyCode()));
                       NotEquals notFrequencyBaseCode = new NotEquals("aw_FrequencyBaseCode",new Integer(awdTemplateRepTermsBean.getFrequencyBaseCode()));
                       NotEquals notOspDistributionCode = new NotEquals("aw_OspDistributionCode", new Integer(awdTemplateRepTermsBean.getOspDistributionCode()));
                       NotEquals notDueDate = new NotEquals("dueDate", awdTemplateRepTermsBean.getDueDate());
                       NotEquals notReportClassCode = new NotEquals("aw_ReportClassCode", new Integer(awdTemplateRepTermsBean.getReportClassCode()));
                       Or notAll = new Or(new Or(new Or(notReportCode, notReportClassCode),new Or(notFrequencyBaseCode, notOspDistributionCode)), new Or(notDueDate,notFrequencyCode));
                       cvFilteredReport = cvFilteredReport.filter(notAll);
                       filteredRepIdx = 0;
                       cvFilteredReport.sort("reportClassCode");
                   }
                   veRepTermDetails.add(reportTermDetailsType);
                 }
                 ReportTermType reportTermType = objFactory.createReportTermType();
                 reportTermType.setDescription(classBean.getDescription());
                 reportTermType.getReportTermDetails().addAll(veRepTermDetails);
                 vcReport.add(reportTermType);
               }// end if filteredReportSize > 0
             
            }
         
        }
        return vcReport;
     }
        
  
    private SchoolInfoType getSchoolInfoType() throws JAXBException,
                                        CoeusException,DBException{
        try{
        SchoolInfoType schoolInfoType = objFactory.createSchoolInfoType();
        String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
        String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
        schoolInfoType.setSchoolName(schoolName);
        schoolInfoType.setAcronym(schoolAcronym);
        return schoolInfoType;
        }catch (Exception ex) {
            UtilFactory.log(ex.getMessage(),ex,"TemplateStream","getSchoolInfoType()");
            throw new CoeusException(ex.getMessage());
        }
    }
}
