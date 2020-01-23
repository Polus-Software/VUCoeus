/*
 * @(#)SF424Stream.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
 
import edu.mit.coeus.budget.bean.BudgetInfoBean;

import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.s2s.bean.SF424V2TxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.organization.bean.OrganizationYNQBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
 
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationListBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import gov.grants.apply.forms.sf424_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  coeus dev team
 * @Created on October 19, 2004, 10:12 AM
 * Class for generating the object stream for grants.gov SF424. It uses jaxb classes
 * which have been created under gov.grants.atapply package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * SF424 schema.
 */


 public class SF424Stream extends S2SBaseStream{
    private gov.grants.apply.forms.sf424_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalv1ObjFactory;

    //txn beans
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalPrintingTxnBean propPrintingTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private RolodexMaintenanceDataTxnBean rolodexMaintDataTxnBean;
    private DepartmentPersonTxnBean deptPersonTxnBean;
    private BudgetDataTxnBean budgetDataTxnBean;
    private S2STxnBean s2sTxnBean;
    
    /** propDevFormBean holds proposal master info   */
    private ProposalDevelopmentFormBean propDevFormBean;
    /** orgMaintFormBean holds organization master info   */
    private OrganizationMaintenanceFormBean orgMaintFormBean;
    /** orgMaintFormBean holds performing organization master info   */
    private OrganizationMaintenanceFormBean perfOrgFormBean;
    /** rolodexDetailsBean holds rolodex information for orgContact   */
    private RolodexDetailsBean orgContactRolodexDetailsBean;
    /** rolodexDetailsBean holds rolodex information for performing orgContact */
    private RolodexDetailsBean perfOrgContactRolodexDetailsBean;
    private HashMap hmCFDA;
    private String pIPersonID;
    private boolean isNonMITPerson = true;
    /** aorBean holds information for AOR */
    private DepartmentPersonFormBean aorBean;
    /** budgetInfoBean holds budget info */
    private BudgetInfoBean budgetInfoBean;
    
    private Date stateReviewDate = null;
    //start case 2406
    private String organizationID;
    private String perfOrganizationID;
    //end case 2406
    
    private Hashtable propData;
    private String propNumber;
    private Calendar calendar;
    
    private final static String ORG_FED_DEBT_YNQ = "I7";
   
    
   
    /** Creates a new instance of SF424Stream */
    public SF424Stream(){
        objFactory = new gov.grants.apply.forms.sf424_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        globalv1ObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        propPrintingTxnBean = new ProposalPrintingTxnBean();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
        deptPersonTxnBean = new DepartmentPersonTxnBean();
        budgetDataTxnBean = new BudgetDataTxnBean();
        hmCFDA = new HashMap();
        s2sTxnBean = new S2STxnBean();
    } 
   
    private GrantApplicationType getGrantApplication() throws CoeusXMLException,CoeusException,DBException, CoeusException{
        GrantApplicationType grantApplication = null;
        try{
           grantApplication = objFactory.createGrantApplication();
            
            //get proposal master info
            propDevFormBean = getPropDevData();
            //get supplementary info
            hmCFDA = getCFDA();
            //get applicant organization info
            //start case 2406
//            orgMaintFormBean = getOrgData(propDevFormBean.getOrganizationId());
            //get performing organization info
//            perfOrgFormBean = getOrgData(propDevFormBean.getPerformingOrganizationId());
            orgMaintFormBean = getOrgData();
            perfOrgFormBean = getPerfOrgData();
            //end case 2406
            //get rolodex information for applicant org contact
            orgContactRolodexDetailsBean = getRolodexData(orgMaintFormBean.getContactAddressId());
            //get rolodex information for performing org contact
            perfOrgContactRolodexDetailsBean = getRolodexData(perfOrgFormBean.getContactAddressId());
            //get budget info
            budgetInfoBean = getBudgetInfo();
            //get AOR
            aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber);   
            
            String unitNumber,unitName;
            unitNumber = deptPersonTxnBean.getHomeUnit(aorBean.getPersonId());
            if(unitNumber != null) {
                unitName = deptPersonTxnBean.getUnitName(unitNumber);
                if(unitName.length()>30){
                    unitName = deptPersonTxnBean.getUnitName(unitNumber).substring(0,30);
                }
            }else unitName = "Unknown";
               
            aorBean.setUnitName (unitName);
            aorBean.setDirDept(unitName);
            
            grantApplication.setSubmissionTypeCode(getSF424SubmissionType());
            
            grantApplication.setSubmittedDate(getTodayDate());
            grantApplication.setApplicationTypeCode(getSF424ApplicationTypeCode());
        
            HashMap hmRevision = getRevisionCodes();
            RevisionType  revision = null;
         
             
            String revision1 = null;
            String revision2 = null;
          //  String revDesc = null;
            
            if (hmRevision != null) {
                if (hmRevision.get("revCode1") != null){
                   revision = objFactory.createRevision();
                   revision1 = hmRevision.get("revCode1").toString();
                   revision.setRevisionCode1(revision1);
               
                   if (hmRevision.get("revCode2") != null) {
                       revision2 = hmRevision.get("revCode2").toString();
                        revision.setRevisionCode2(revision2);
                    }
                   
//                    if (hmRevision.get("revDesc") != null){
//                        revDesc = hmRevision.get("revDesc").toString();
//                        revision.setRevisionOtherExplanation(revDesc);
//                    }
                    
                   
                }
                grantApplication.setRevision(revision);
            }
          
           grantApplication.setAgencyName(propDevFormBean.getSponsorName()); 
           
           //federal id
            String federalId = getFederalID();
            if (!federalId.equals( "-1")) {
                grantApplication.setFederalID(federalId);
            }
            grantApplication.setActivityTitle(hmCFDA.get("ACTIVITY_TITLE").toString());      
            grantApplication.setSubmittingOrganization(getSubmittingOrganization());
            grantApplication.setProject(getProject());
            IndividualType individual = objFactory.createIndividual();
            individual.setAuthorizedRepresentative(getAuthorizedRepresentative(aorBean));
            individual.setContact(getContact(getPIPersonId())); 
            grantApplication.setIndividual(individual);            
            grantApplication.setAuthorizedRepresentativeSignature(aorBean.getFullName());
            grantApplication.setCFDANumber((String)hmCFDA.get("CFDA"));
            if(budgetInfoBean != null){
                grantApplication.setBudget(getBudget(budgetInfoBean));
            }
            grantApplication.setSignedDate(getTodayDate());
            grantApplication.setStateReviewCode(getStateReviewCode());
            if(stateReviewDate != null){
                grantApplication.setStateReviewDate(this.getCal(stateReviewDate));
            }
            grantApplication.setCoreSchemaVersion("1.0"); //hard coding
            grantApplication.setFormVersionIdentifier("1.0"); //hard coding
                     
         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"SF424Stream","getSF424()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return grantApplication;
    }
   
  
   
     private HashMap getCFDA() throws DBException, CoeusException {
         if (propNumber ==null)
              throw new CoeusXMLException("Proposal Number is Null");
         HashMap hmCFDA = s2sTxnBean.getCFDA(propNumber);
         
         return hmCFDA;
     }
   
     private String getFederalID() throws DBException, CoeusException {
         String federalId = null;
         if (propNumber ==null)
             throw new CoeusXMLException("Proposal Number is Null");
         
         federalId = s2sTxnBean.getFederalId(propNumber);
           
         return federalId;
         
     }
    
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
    //start case 2406 
//    private OrganizationMaintenanceFormBean getOrgData(String orgID)
//       throws CoeusXMLException,CoeusException,DBException{
//        if(orgID==null) 
//            throw new CoeusXMLException("Organization id is Null");   
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(orgID);
//    }
     private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = s2sTxnBean.getOrganizationID(propNumber,"O");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
            //start case 3110
        SF424V2TxnBean sf424V2TxnBean = new SF424V2TxnBean();
        OrganizationMaintenanceFormBean  orgBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
        //get congressional district
        HashMap hmCongDist = sf424V2TxnBean.getCongDistrict(propNumber, orgBean.getOrganizationId(),1);
        String congDist = (String) hmCongDist.get("CONGDIST").toString();
        orgBean.setCongressionalDistrict(congDist);
        return orgBean;
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }
    
      private OrganizationMaintenanceFormBean getPerfOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmPerfOrg = s2sTxnBean.getOrganizationID(propNumber, "P");
        if (hmPerfOrg!= null && hmPerfOrg.get("ORGANIZATION_ID") != null){
               perfOrganizationID = hmPerfOrg.get("ORGANIZATION_ID").toString();
        }
                //start case 3110
        SF424V2TxnBean sf424V2TxnBean = new SF424V2TxnBean();
        OrganizationMaintenanceFormBean  orgBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
        //get congressional district
        HashMap hmCongDist = sf424V2TxnBean.getCongDistrict(propNumber, orgBean.getOrganizationId(),2);
        String congDist = (String) hmCongDist.get("CONGDIST").toString();
        orgBean.setCongressionalDistrict(congDist);
        return orgBean;
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
    }
    
    /*end case 2406*/
      
     private RolodexDetailsBean getRolodexData(int rolodexId)
       throws CoeusXMLException,CoeusException,DBException{
        if(rolodexId == 0 )
            throw new CoeusXMLException("Rolodex id is zero");   
        return rolodexMaintDataTxnBean.getRolodexMaintenanceDetails(Integer.toString(rolodexId ));  
    }
     

   /////////////////////
   // getSF424SubmissionType
   /////////////////////
   private String getSF424SubmissionType()
                throws CoeusException, DBException{
   HashMap hmSubmission = new HashMap();
   HashMap hmResult = s2sTxnBean.getSF424SubmissionType(propNumber);
  
   String subType = hmResult.get("SUBMISSION_TYPE_CODE").toString();
  
  
   return subType;
   }
   
    private String getPIPersonId()
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
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
        return personID;
    }
    
    private BudgetInfoBean getBudgetInfo()
       throws JAXBException,  CoeusException,DBException { 
       int version = s2sTxnBean.getVersion(propNumber);
       
      return budgetDataTxnBean.getBudgetForProposal(propNumber,version);
     
   }     

    
    private String getSF424ApplicationTypeCode()
       throws CoeusException, DBException{
       
        HashMap hmSubmission = new HashMap();
        HashMap hmResult = s2sTxnBean.getSF424ApplicationType(propNumber);
  
        String applicationTypeCode = hmResult.get("APPLICATIONTYPE").toString();
  
        
        return applicationTypeCode;
    }
    
    private HashMap getRevisionCodes()
        throws CoeusException, DBException{
       
        String revisionCode = null;
    //    String revisionDesc = null;
        String revCode1 = null;
        String revCode2 = null;
        
        HashMap hmRevisionCodes = new HashMap();
        HashMap hmResult = s2sTxnBean.getSF424RevisionType(propNumber);
  
        if ( hmResult.get("REVISION_CODE") != null) {
            revisionCode = hmResult.get("REVISION_CODE").toString();
            revCode1 = revisionCode.substring(0,1);
            if (revisionCode.length() > 1)
                
               revCode2 = revisionCode.substring(1,2);
        }
        
 //       if ( hmResult.get("REVISION_DESC") != null)
 //           revisionDesc = hmResult.get("REVISION_DESC").toString();
        
        hmRevisionCodes.put("revCode1",revCode1);
        hmRevisionCodes.put("revCode2",revCode2);
 //       hmRevisionCodes.put("revDesc",revisionDesc);
        
        
        return hmRevisionCodes;
    }
    
    private ProjectType getProject() throws JAXBException{
        ProjectType projectType = objFactory.createProjectType();
        projectType.setProposedStartDate
            (getCal(propDevFormBean.getRequestStartDateInitial()));
        projectType.setProposedEndDate
            (getCal(propDevFormBean.getRequestEndDateInitial()));
        projectType.setCongressionalDistrict
            (UtilFactory.setNullToUnknown(perfOrgFormBean.getCongressionalDistrict()));
        projectType.setProjectTitle(propDevFormBean.getTitle());
        projectType.setLocation(perfOrgContactRolodexDetailsBean.getState());
        return projectType;
    }

    private SubmittingOrganizationType getSubmittingOrganization()
        throws JAXBException, CoeusException, DBException{
        SubmittingOrganizationType submittingOrg = objFactory.createSubmittingOrganization();
        submittingOrg.setCongressionalDistrict(orgMaintFormBean.getCongressionalDistrict());
        submittingOrg.setAddress(getAddress(orgContactRolodexDetailsBean, null)); 
        submittingOrg.setDelinquentFederalDebtIndicator
       //case 2406   (getFederalDebtAnswer(propDevFormBean.getOrganizationId()));
                     (getFederalDebtAnswer(organizationID));
        
        OrganizationType organization = objFactory.createOrganization();
        organization.setDUNSID(orgMaintFormBean.getDunsNumber());
        organization.setOrganizationName(orgMaintFormBean.getOrganizationName());
        organization.setEmployerID(orgMaintFormBean.getFederalEmployerID());
        String deptName = propDevFormBean.getOwnedByDesc();
        if(deptName != null && deptName.length() > 30){
            organization.setDepartmentName(deptName.substring(0,30));
        }
        else{
            organization.setDepartmentName(deptName);
        }
        String divisionName = s2sTxnBean.fn_get_division(propDevFormBean.getOwnedBy());
        if (divisionName != null && divisionName.length() > 30){
            organization.setDivisionName(divisionName.substring(0,30));
        }
        else{
            organization.setDivisionName(divisionName);
        }
        OrganizationIdentifyingInformationType orgIDInfoType = 
                    objFactory.createOrganizationIdentifyingInformation();
        orgIDInfoType.setApplicantID(propNumber);
        orgIDInfoType.setApplicantTypeCode(getApplicantType());
        orgIDInfoType.setOrganization(organization); 

        submittingOrg.setOrganizationIdentifyingInformation(orgIDInfoType);         
        return submittingOrg;
    }
    
    private AddressType getAddress(RolodexDetailsBean rolodexDetailsBean,
                                        DepartmentPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
    AddressType address = objFactory.createAddressType();
    if (rolodexDetailsBean != null) {
        //rolodex
        address.setStreet1(rolodexDetailsBean.getAddress1());
        if (rolodexDetailsBean.getAddress2() != null)
            address.setStreet2(rolodexDetailsBean.getAddress2());
        address.setCity(rolodexDetailsBean.getCity());
        if (rolodexDetailsBean.getState() != null)
             address.setStateCode(rolodexDetailsBean.getState());
        if (rolodexDetailsBean.getPostalCode() != null)
            address.setZipCode(UtilFactory.convertNull(rolodexDetailsBean.getPostalCode()));
        address.setCountry(rolodexDetailsBean.getCountry());
    } else {
        //department person - use office address and org contact for complete address
         address.setStreet1(personBean.getOfficeLocation());
         if (orgContactRolodexDetailsBean.getAddress2() != null)
            address.setStreet2(orgContactRolodexDetailsBean.getAddress2());
         address.setCity(orgContactRolodexDetailsBean.getCity());
         if (orgContactRolodexDetailsBean.getState() != null)
             address.setStateCode(orgContactRolodexDetailsBean.getState());
         if (orgContactRolodexDetailsBean.getPostalCode() != null)
            address.setZipCode(orgContactRolodexDetailsBean.getPostalCode());
         address.setCountry(orgContactRolodexDetailsBean.getCountry());
    }
    return address;
   }
    
   private ContactType getContact(String pIPersonID) 
                            throws JAXBException, CoeusException, DBException{
       ContactType contact = objFactory.createContact();
       if(pIPersonID == null){
           return null;
       }
       ProposalPersonFormBean pIBean = 
            propDevTxnBean.getProposalPersonDetails(propNumber, pIPersonID);
       contact.setGivenName1(pIBean.getFirstName());
       contact.setGivenName2(pIBean.getMiddleName());
       contact.setFamilyName(pIBean.getLastName());
       contact.setElectronicMailAddress(pIBean.getEmailAddress());
       contact.setTelephoneNumber(pIBean.getOfficePhone());
       contact.setFaxNumber(pIBean.getFaxNumber());
       return contact;
   }
   
   private AuthorizedRepresentativeType getAuthorizedRepresentative
                    (DepartmentPersonFormBean aorBean) throws JAXBException{
       AuthorizedRepresentativeType authorizedRep = objFactory.createAuthorizedRepresentative();
       authorizedRep.setGivenName1(aorBean.getFirstName());
       authorizedRep.setGivenName2(aorBean.getMiddleName());
       authorizedRep.setFamilyName(aorBean.getLastName());
       authorizedRep.setElectronicMailAddress(aorBean.getEmailAddress());
       if(aorBean.getPrimaryTitle() !=null && aorBean.getPrimaryTitle().length()>45)
         authorizedRep.setRepresentativeTitle(aorBean.getPrimaryTitle().substring(0,45));
       else
         authorizedRep.setRepresentativeTitle(aorBean.getPrimaryTitle());
   
     //  authorizedRep.setRepresentativeTitle(aorBean.getPrimaryTitle());
       authorizedRep.setTelephoneNumber(aorBean.getOfficePhone());
       authorizedRep.setFaxNumber(aorBean.getFaxNumber());
       return authorizedRep;
   }
      
   private BudgetType getBudget(BudgetInfoBean budgetInfoBean) 
                            throws JAXBException, CoeusException, DBException{
        BudgetType budget = objFactory.createBudget();
        BigDecimal programIncome = s2sTxnBean.getProjectIncome(propNumber);
        budget.setCurrencyCode("USD");//hard code to US Dollar
        budget.setFederalEstimatedAmount(convDoubleToBigDec(budgetInfoBean.getTotalCost()));
        budget.setApplicantEstimatedAmount(
                    convDoubleToBigDec(budgetInfoBean.getCostSharingAmount()));
        BigDecimal zero = new BigDecimal(0.00);
        budget.setStateEstimatedAmount(zero);
        budget.setLocalEstimatedAmount(zero);
        budget.setOtherEstimatedAmount(zero);
        budget.setProgramIncomeEstimatedAmount(programIncome);
        BigDecimal totalEstimated = convDoubleToBigDec(budgetInfoBean.getTotalCost() 
            + budgetInfoBean.getCostSharingAmount()).add(programIncome);
        budget.setTotalEstimatedAmount(totalEstimated);
        return budget;
   }
   
   private String getFederalDebtAnswer(String orgID)
                        throws CoeusXMLException, CoeusException, DBException{
        if(orgID==null) 
            throw new CoeusXMLException("Organization id is Null");          
        OrganizationYNQBean[] ynqArray = 
                                orgMaintDataTxnBean.getOrganizationYNQ(orgID);
        OrganizationYNQBean orgYNQ = null;
        for(int cnt=0; cnt<ynqArray.length; cnt++){
            orgYNQ = ynqArray[cnt];
            if(orgYNQ.getQuestionId().equals(ORG_FED_DEBT_YNQ)){
                return orgYNQ.getAnswer();
            }
        }
        return "N";
   }
  
    
  private String getApplicantType() throws CoeusException,DBException{
   
    String applicantType = "";
    boolean smallBusflag = false;    
    OrganizationListBean[] orgTypes = orgMaintDataTxnBean.
        getSelectedOrganizationList(orgMaintFormBean.getOrganizationId());    
    //use first org type in list for now. schema allows for only one type
    int orgTypeCode = orgTypes[0].getOrganizationTypeCode();
    switch (orgTypeCode){
       case 1: {
           //local
           applicantType = "City or Township Government";
           break;
       }case 2: {
           //state
            applicantType =  "State Government";
            break;
       }case 3: {
           //federal
           applicantType = "Other";
           break;
       }case 4: {
           //Private non-profit
           applicantType = "Nonprofit Organization (Other than Institution of Higher Education)Other (Specify) ";
            break;
       }case 5: {
           //Non-Profit
           applicantType = "Nonprofit Organization (Other than Institution of Higher Education)Other (Specify) ";
           break;

       }case 6: {
           //For-profit
           applicantType = "For-profit Organization (other than small business)";
           break;

       }case 7: {
          //Other
           applicantType = "Other (specify)";
           break;

       }case 8: {
           //Indian Tribal Government
           applicantType = "Native American Tribal Government (Federally Recognized)"; 
           break;

       }case 9: {
           //Individual
           applicantType = "Individual";
           break;

       }case 10: {
           //Inst of higher learning
           applicantType = "Private Institution of Higher Education";
           break;

       }case 11: {
           //Small Business
           applicantType = "Small Business";
           break;

       }case 21: {
           applicantType = "Public/State-Controlled Institution of Higher Education";
           break;

        }case 22: {         
           applicantType = "County Government";
           break;

        }case 23: {  
           applicantType = "Special District";
           break;

        }case 24: {         
           applicantType = "Independent School District";
           break;

        }case 25: {          
           applicantType = "Public/Indian Housing Authority";
           break;

        }case 26: {          
           applicantType = "Native American Tribal Organization (other than Federally recognized)";
           break;

        }default: {
            applicantType = "Other";
            break;
        }
    }
    return applicantType;
  }
  
  private String getStateReviewCode() throws DBException, CoeusException{
      HashMap hmStateReview = s2sTxnBean.getEOStateReview(propNumber);
      String answer = (String)hmStateReview.get("ANSWER");
      System.out.println("answer: "+answer);
      if (answer == null || answer.equals("X")){
          return "Not Covered";
      }
      else if(answer.equals("N")){
          return "Not Reviewed";
      }
      else if(answer.equals("Y")){
          this.stateReviewDate = hmStateReview.get("REVIEW_DATE") == null ? null : 
              new Date( ((Timestamp) hmStateReview.get("REVIEW_DATE")).getTime());
          return "Yes";
      }

      else {
        return "Not Reviewed";
      }
  }
   

    private static BigDecimal convDoubleToBigDec(double dblCost){
    
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         return bdCost; 
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
    
    public Object getStream(HashMap hm) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hm.get("PROPOSAL_NUMBER");
        return getGrantApplication();
    }    
    
   
}
