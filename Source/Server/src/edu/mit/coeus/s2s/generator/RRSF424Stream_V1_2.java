/*
 * @(#)RRSF424Stream_V1_2.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
 
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;

import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
 
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationListBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.bean.RRSF424V11TxnBean;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import gov.grants.apply.forms.rr_sf424_1_2_v1_2.*;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;
import gov.grants.apply.system.globallibrary_v2.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * @author  jenlu 
 */


 public class RRSF424Stream_V1_2 extends S2SBaseStream{
    private gov.grants.apply.forms.rr_sf424_1_2_v1_2.ObjectFactory objFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalLibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
 
    //txn beans
    
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalPrintingTxnBean propPrintingTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private RolodexMaintenanceDataTxnBean rolodexMaintDataTxnBean;
    private DepartmentPersonTxnBean deptPersonTxnBean;
    private BudgetDataTxnBean budgetDataTxnBean;
    private RRSF424V11TxnBean rrSF424V11TxnBean;
    private S2STxnBean s2sTxnBean;
    
    /** propDevFormBean holds proposal master info   */
    private ProposalDevelopmentFormBean propDevFormBean;
    /** orgMaintFormBean holds organization master info   */
    private OrganizationMaintenanceFormBean orgMaintFormBean;
    /** orgMaintFormBean holds performing organization master info   */
    private OrganizationMaintenanceFormBean perfOrgFormBean;
    /** rolodexDetailsBean holds rolodex information for orgContact   */
    private RolodexDetailsBean orgContactRolodexDetailsBean;
   /* refer to case 2317 - change box 5 contact person depending on parameter */
    private DepartmentPersonFormBean contactPersonBean;
    
    private RolodexDetailsBean perfOrgContactRolodexDetailsBean;
    private HashMap hmCFDA;
    private  HashMap hmSubmission;
    private String pIPersonID;
    private boolean isNonMITPerson = true;
    /** aorBean holds information for AOR */
    private DepartmentPersonFormBean aorBean;
    /** budgetInfoBean holds budget info */
    private BudgetInfoBean budgetInfoBean;
    
    private RRSF42412Type rrSF424 = null;
    
    private Hashtable propData;
    private String propNumber;
    private Calendar calendar;
    
 
    private String organizationID;
    private String perfOrganizationID;
    private String lsSponsor;
   
    
   
    /** Creates a new instance of RSF424Stream_V1_2 */
    public RRSF424Stream_V1_2(){
        objFactory = new gov.grants.apply.forms.rr_sf424_1_2_v1_2.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalLibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        propPrintingTxnBean = new ProposalPrintingTxnBean();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
        deptPersonTxnBean = new DepartmentPersonTxnBean();
        budgetDataTxnBean = new BudgetDataTxnBean();
        rrSF424V11TxnBean = new RRSF424V11TxnBean();
        hmCFDA = new HashMap();
        s2sTxnBean = new S2STxnBean();
        
    } 
   
    private RRSF42412Type getSF424() throws CoeusXMLException,CoeusException,DBException{
      
        try{
           rrSF424 = objFactory.createRRSF42412();
            
            //get proposal master info
            propDevFormBean = getPropDevData();
            //get supplementary info
            hmCFDA = getCFDA();
            //get applicant organization info
            //orgMaintFormBean = getOrgData(propDevFormBean.getOrganizationId());
			orgMaintFormBean = getOrgData();
            
            //get performing organization info
            //perfOrgFormBean = getOrgData(propDevFormBean.getPerformingOrganizationId());
			perfOrgFormBean = getPerfOrgData();
            //get rolodex information for applicant org contact
            orgContactRolodexDetailsBean = getRolodexData(orgMaintFormBean.getContactAddressId());
            //get rolodex information for performing org contact
            perfOrgContactRolodexDetailsBean = getRolodexData(perfOrgFormBean.getContactAddressId());
            // get PI information
            pIPersonID = getPIPersonId();
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
        
            //JM 02-07-2013	hard-coding central office
            //aorBean.setDirDept(unitName);
            aorBean.setDirDept("Office of Sponsored Programs");    
            // JM END       
              
           /*start change for org contact person - refer to case 2317*/
         //   ospContactPersonBean = propPrintingTxnBean.getOrgContactPerson(propNumber);
           /* end change for org contact person - refer to case 2317*/
            
           rrSF424.setSubmittedDate(getTodayDate());
           
           hmSubmission = getSubmissionType();
           
           rrSF424.setSubmissionTypeCode(hmSubmission.get("SubmissionType").toString());
           rrSF424.setFormVersion("1.2"); //hard coding
           
           HashMap hmState;
           
           if (orgContactRolodexDetailsBean.getState() != null) {
            hmState= null;
            hmState = rrSF424V11TxnBean.getStateName(orgContactRolodexDetailsBean.getState());
            if (hmState != null )
               rrSF424.setStateID(hmState.get("STATE_NAME").toString());
         }
           
           rrSF424.setApplicantInfo(getAppInfoType()); 
           //coeusqa-3344 changes
           String ls_is_nih = "0";
           HashMap hmISNIH;
           hmISNIH = null;
           hmISNIH = rrSF424V11TxnBean.getISNIH(propNumber);
           if (hmISNIH != null) {
               ls_is_nih = hmISNIH.get("IS_NIH").toString();
               }
           if (ls_is_nih.equals("1")) {
               rrSF424.setEmployerID(orgMaintFormBean.getPhsAcount());
           }else {
              rrSF424.setEmployerID(orgMaintFormBean.getFederalEmployerID());
            }
           rrSF424.setApplicantType( getApplicantType());
           rrSF424.setApplicationType(getApplicationType());
           rrSF424.setApplicantID(propNumber);
           rrSF424.setFederalAgencyName(propDevFormBean.getSponsorName());  
           String title = propDevFormBean.getTitle();          
           if (title != null && title.length() > 200){
                rrSF424.setProjectTitle(title.substring(0,200));
           }
           else{
                rrSF424.setProjectTitle(title);
           }
//           rrSF424.setProjectTitle(propDevFormBean.getTitle());  
           
           
           rrSF424.setProposedProjectPeriod(getProjectPeriod());
           rrSF424.setCongressionalDistrict(getCongDistrict());
           //case 2254
           if (hmCFDA.get("CFDA") != null) {
               if (!hmCFDA.get("CFDA").toString().equals(""))
               rrSF424.setCFDANumber(hmCFDA.get("CFDA").toString());
           }
           rrSF424.setActivityTitle(hmCFDA.get("ACTIVITY_TITLE").toString());
           //adding federal id 4/6/06
           if (!getFederalID().equals("-1"))
                rrSF424.setFederalID(getFederalID());
           if(pIPersonID != null){
            rrSF424.setPDPIContactInfo(getPDPI());
           }
           rrSF424.setEstimatedProjectFunding(getProjectFunding());
           rrSF424.setTrustAgree("Y: Yes"); //hard coding
           rrSF424.setStateReview(getStateReviewType());
           rrSF424.setAORInfo(getAORInfoType());
           rrSF424.setAORSignature(aorBean.getFullName());
           rrSF424.setAORSignedDate(getTodayDate());

           //coeusqa-2299
           rrSF424.setAgencyRoutingNumber( propDevFormBean.getAgencyProgramCode());
           
      //case 3329      AttachedFileDataType preAppAttachment = getPreAppAttachment();
           getAttachments();
          // AttachedFileDataType preAppAttachment = getAttachments();
         //  if (preAppAttachment.getFileName() != null) rrSF424.setPreApplicationAttachment(preAppAttachment);
            
           //case 3329
        
                     
         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRSF424Stream_V1_2","getSF424()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return rrSF424;
    }
     
    private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = rrSF424V11TxnBean.getOrganizationID(propNumber,"O");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
        
        OrganizationMaintenanceFormBean  orgBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
        //get congressional district
        HashMap hmCongDist = rrSF424V11TxnBean.getCongDistrict(propNumber, orgBean.getOrganizationId(),1);
        String congDist = (String) hmCongDist.get("CONGDIST").toString();
        orgBean.setCongressionalDistrict(congDist);
        return orgBean;
     //   return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }
   
      private OrganizationMaintenanceFormBean getPerfOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmPerfOrg = rrSF424V11TxnBean.getOrganizationID(propNumber, "P");
        if (hmPerfOrg!= null && hmPerfOrg.get("ORGANIZATION_ID") != null){
               perfOrganizationID = hmPerfOrg.get("ORGANIZATION_ID").toString();
        }
        
         OrganizationMaintenanceFormBean  orgBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
        //get congressional district
          HashMap hmCongDist = rrSF424V11TxnBean.getCongDistrict(propNumber, orgBean.getOrganizationId(),2);
        String congDist = (String) hmCongDist.get("CONGDIST").toString();
         orgBean.setCongressionalDistrict(congDist);
         return orgBean;
      //  return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
    }
    
    
    //end case 2406
     private HashMap getCFDA() throws DBException, CoeusException {
         if (propNumber ==null)
              throw new CoeusXMLException("Proposal Number is Null");
         HashMap hmCFDA = s2sTxnBean.getCFDA(propNumber);
         
         return hmCFDA;
     }
   
    
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
//    private OrganizationMaintenanceFormBean getOrgData(String orgID)
//       throws CoeusXMLException,CoeusException,DBException{
//        if(orgID==null) 
//            throw new CoeusXMLException("Organization id is Null");   
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(orgID);
//    }
//    
     private RolodexDetailsBean getRolodexData(int rolodexId)
       throws CoeusXMLException,CoeusException,DBException{
        if(rolodexId == 0 )
            throw new CoeusXMLException("Rolodex id is zero");   
        return rolodexMaintDataTxnBean.getRolodexMaintenanceDetails(Integer.toString(rolodexId ));  
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
       
        BudgetInfoBean budgetInfoBean = budgetDataTxnBean.getBudgetForProposal(propNumber,version);
        if (budgetInfoBean != null )
        {
            if (budgetInfoBean.isBudgetModularFlag() == true)
             { 
            //get modular budget amounts instead of budget detail amounts
            HashMap hmIDC = rrSF424V11TxnBean.getIDCForModBudget(propNumber,version);
            
            budgetInfoBean.setTotalIndirectCost(Double.parseDouble(
                                                hmIDC.get("IDC") == null ? "0" :
                                                hmIDC.get("IDC").toString()));
                                            
            HashMap hmTotCost = rrSF424V11TxnBean.getTotForModBudget(propNumber,version);
            
            budgetInfoBean.setTotalCost(Double.parseDouble(
                                                hmTotCost.get("TOTAL_COST") == null ? "0" :
                                                hmTotCost.get("TOTAL_COST").toString()));
                                                
            HashMap hmCostShare = rrSF424V11TxnBean.getCostShareForModBudget(propNumber,version);
            
            budgetInfoBean.setCostSharingAmount(Double.parseDouble(
                                                hmTotCost.get("COST_SHARE") == null ? "0" :
                                                hmTotCost.get("COST_SHARE").toString()));                                  
        }
            /*coeusqa-2035
             * if submit cost sharing flag is No, then don't show cost sharing amounts
             * if it is Yes, then check line items to see if some line items have the flag off
             */
             if (!budgetInfoBean.isSubmitCostSharingFlag()){
               budgetInfoBean.setCostSharingAmount(0);
            } else {
                //check line items
                Double costshare = getCostSharingAmt();
                budgetInfoBean.setCostSharingAmount(costshare);
            }
            
        }
        return budgetInfoBean;
     
   }
   
   //added for  coeusqa-2035
    private Double getCostSharingAmt()
       throws JAXBException,  CoeusException,DBException {
       int version = s2sTxnBean.getVersion(propNumber);

     Double costShareAmt= Double.parseDouble("0");
       HashMap hmCostShare = new HashMap();
       hmCostShare = rrSF424V11TxnBean.getCostShareAmt(propNumber, version);
       
       if (hmCostShare != null) {
           costShareAmt = Double.parseDouble(hmCostShare.get("COST_SHARE_AMT") == null ? "0" :
                                             hmCostShare.get("COST_SHARE_AMT").toString());
       }

        return costShareAmt;

   }

  
   private gov.grants.apply.forms.rr_sf424_1_2_v1_2.OrganizationContactPersonDataType getPDPI()
                  throws JAXBException,  CoeusException,DBException{
        gov.grants.apply.forms.rr_sf424_1_2_v1_2.OrganizationContactPersonDataType PDPI = 
            objFactory.createOrganizationContactPersonDataType();
            System.out.println("call getProposalPersonDetails with piPersonId: "+pIPersonID);
            ProposalPersonFormBean pIBean = propDevTxnBean.getProposalPersonDetails(propNumber, pIPersonID);                     
            PDPI.setName(getNameType(pIBean));
            PDPI.setPhone(pIBean.getOfficePhone());
            PDPI.setEmail(pIBean.getEmailAddress());
            if (pIBean.getFaxNumber() != null) PDPI.setFax(pIBean.getFaxNumber());
            PDPI.setAddress( getAddressType(null,pIBean)); 
          
            if (pIBean.getDirTitle() != null) {
                if( pIBean.getDirTitle().length()>45)
                    PDPI.setTitle(pIBean.getDirTitle().substring(0,45));
                else
                    PDPI.setTitle(pIBean.getDirTitle());
            }
            
            //change for pi lead unit info
      
            HashMap hmUnit = getLeadUnit();
            String leadUnit = hmUnit.get("UNIT_NUMBER").toString();
            String leadUnitName = hmUnit.get("UNIT_NAME").toString();
           
            PDPI.setDepartmentName(leadUnitName);
               
            
            /*
            if (pIBean.getUnitName() != null) {
                if(pIBean.getUnitName().length()>30)
                    PDPI.setDepartmentName(pIBean.getUnitName().substring(0,30));
                else
                    PDPI.setDepartmentName(pIBean.getUnitName());
            }
            */
            
           // String division = s2sTxnBean.fn_get_division(pIBean.getHomeUnit());
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
            //String division = s2sTxnBean.fn_get_division(leadUnit);
            String division = s2sTxnBean.getPropPersonDivision(pIBean.getProposalNumber()
                            , pIBean.getPersonId());
            if(division == null){
                division = s2sTxnBean.fn_get_division(leadUnit);
            }
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
            if (division != null) {
                if(division.length()>30)
                    division = division.substring(0,30);
            }
               
       PDPI.setDivisionName(division);     
         
        PDPI.setOrganizationName(UtilFactory.convertNull(orgMaintFormBean.getOrganizationName()));
        
        return PDPI;
    } 
   
   //addition for box 5 and box 15 changes
   private HashMap getLeadUnit()
                 throws CoeusException, DBException {
                     
       HashMap hmUnit = rrSF424V11TxnBean.getLeadUnit(propNumber);
      
       if (hmUnit != null){
            String leadUnit = hmUnit.get("UNIT_NUMBER")== null? "000001" :
                              hmUnit.get("UNIT_NUMBER").toString();
                    
            String leadUnitName = hmUnit.get("UNIT_NAME")== null? "UNKNOWN" :
                                  hmUnit.get("UNIT_NAME").toString(); 
                                  
            if (leadUnitName.length() > 30) leadUnitName = leadUnitName.substring(0,30);
          
            hmUnit.put("UNIT_NUMBER",leadUnit);
            hmUnit.put("UNIT_NAME",leadUnitName);
            }    
        return hmUnit;
                           
   }
   
    private RRSF42412Type.EstimatedProjectFundingType getProjectFunding()
                  throws JAXBException,  CoeusException,DBException{
   
      RRSF42412Type.EstimatedProjectFundingType projectFundingType = 
            objFactory.createRRSF42412TypeEstimatedProjectFundingType();
     if (budgetInfoBean != null){
       projectFundingType.setTotalEstimatedAmount(convDoubleToBigDec(budgetInfoBean.getTotalCost()));

       projectFundingType.setTotalNonfedrequested(convDoubleToBigDec(budgetInfoBean.getCostSharingAmount()));

       projectFundingType.setTotalfedNonfedrequested(convDoubleToBigDec(budgetInfoBean.getTotalCost() +
                                                    (budgetInfoBean.getCostSharingAmount())));
       projectFundingType.setEstimatedProgramIncome(s2sTxnBean.getProjectIncome(propNumber));       
   
     }
     return projectFundingType;
    }
     
   private RRSF42412Type.ApplicantInfoType getAppInfoType()
                  throws JAXBException,  CoeusException,DBException{
     RRSF42412Type.ApplicantInfoType appInfoType = objFactory.createRRSF42412TypeApplicantInfoType();
     /* start change for box5 - refer to case 2317 - use parameter to decide source */
        
      HashMap hmContactType = (HashMap) rrSF424V11TxnBean.getContactType(propNumber);
      String contactType = hmContactType.get("CONTACT_TYPE").toString();
      
      if (contactType.equals("I")) {
           // use organization rolodex contact
          appInfoType.setContactPersonInfo(getAppInfoContactType(orgContactRolodexDetailsBean));
      } else {
          //contact will come from osp$unit or osp$unit_administrators
          HashMap hmContactPerson = rrSF424V11TxnBean.getContactPerson(propNumber, contactType);
          if (hmContactPerson != null) {
          DepartmentPersonFormBean contactPersonBean = getUnitContactDetails(hmContactPerson);
         
          appInfoType.setContactPersonInfo(getAppInfoContactType(contactPersonBean));
          }
      }

     appInfoType.setOrganizationInfo(getOrgDataType());
       
     return appInfoType;
    }
   
   private RRSF42412Type.ApplicantInfoType.ContactPersonInfoType
          getAppInfoContactType(DepartmentPersonFormBean deptPersonBean)
                  throws JAXBException,  CoeusException,DBException{
    RRSF42412Type.ApplicantInfoType.ContactPersonInfoType appInfoContact =
        objFactory.createRRSF42412TypeApplicantInfoTypeContactPersonInfoType();
    appInfoContact.setName(getNameType(deptPersonBean));
    appInfoContact.setPhone(deptPersonBean.getOfficePhone()==null?"000-000-0000":
                            deptPersonBean.getOfficePhone());
    appInfoContact.setFax(deptPersonBean.getFaxNumber());
    appInfoContact.setEmail(deptPersonBean.getEmailAddress());
    return appInfoContact;
  
   }
    
   
   private RRSF42412Type.ApplicantInfoType.ContactPersonInfoType
          getAppInfoContactType(RolodexDetailsBean rolodexDetailsBean)
                  throws JAXBException,  CoeusException,DBException{
    RRSF42412Type.ApplicantInfoType.ContactPersonInfoType appInfoContact =
        objFactory.createRRSF42412TypeApplicantInfoTypeContactPersonInfoType();
    appInfoContact.setName(getNameType(rolodexDetailsBean));
    appInfoContact.setPhone(rolodexDetailsBean.getPhone()==null?"000-000-0000":
                            rolodexDetailsBean.getPhone());
    appInfoContact.setFax(rolodexDetailsBean.getFax());
    appInfoContact.setEmail(rolodexDetailsBean.getEMail());
    return appInfoContact;
  
   }
   
   
   private DepartmentPersonFormBean getUnitContactDetails(HashMap hmContact) {
        DepartmentPersonFormBean contactPersonBean = new ProposalPersonFormBean();
      
      
        contactPersonBean.setLastName( (String)
                hmContact.get("LAST_NAME"));
        contactPersonBean.setFirstName( (String)
                hmContact.get("FIRST_NAME"));
        contactPersonBean.setMiddleName( (String)
                hmContact.get("MIDDLE_NAME"));
        contactPersonBean.setEmailAddress( (String)
                hmContact.get("EMAIL_ADDRESS")); 
         contactPersonBean.setOfficePhone( (String)
                hmContact.get("OFFICE_PHONE"));
        contactPersonBean.setFaxNumber((String)
               hmContact.get("FAX_NUMBER"));
      
      
        return contactPersonBean;
   }
  


   private HumanNameDataType  getNameType(RolodexDetailsBean rolodexDetailsBean)
                  throws JAXBException,  CoeusException,DBException{
   
    HumanNameDataType nameType = globalLibraryObjFactory.createHumanNameDataType(); 
   
    
    nameType.setFirstName(rolodexDetailsBean.getFirstName());     
    nameType.setLastName(rolodexDetailsBean.getLastName());
    if(rolodexDetailsBean.getMiddleName() != null)
     nameType.setMiddleName(UtilFactory.convertNull(rolodexDetailsBean.getMiddleName()));
   
    return nameType;
   }
  
   //overloaded for Dept person
   private HumanNameDataType  getNameType(DepartmentPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
  
//    HumanNameDataType nameType = globalObjFactory.createHumanNameDataType(); 
      HumanNameDataType nameType = globalLibraryObjFactory.createHumanNameDataType(); 
      
     nameType.setFirstName(UtilFactory.convertNull(personBean.getFirstName()));
     nameType.setLastName(UtilFactory.convertNull(personBean.getLastName()));
     if(personBean.getMiddleName()!=null)
        nameType.setMiddleName(personBean.getMiddleName());
     return nameType;
    }

   //overloaded for ProposalPersonFormBean person
   private HumanNameDataType  getNameType(ProposalPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
   
//    HumanNameDataType nameType = globalObjFactory.createHumanNameDataType(); 
      HumanNameDataType nameType = globalLibraryObjFactory.createHumanNameDataType(); 
     
     nameType.setFirstName(UtilFactory.convertNull(personBean.getFirstName()));
     nameType.setLastName(UtilFactory.convertNull(personBean.getLastName()));
     if(personBean.getMiddleName()!=null)
        nameType.setMiddleName(personBean.getMiddleName());
     return nameType;
    }

   private String getFederalID()
                throws JAXBException, CoeusException, DBException{
    String federalID = null;
    
    federalID = s2sTxnBean.getFederalId(propNumber);
    
    return federalID;
                    
   }
   
    private gov.grants.apply.system.globallibrary_v2.OrganizationDataType getOrgDataType()
                  throws JAXBException,  CoeusException,DBException{
    OrganizationDataType orgDataType =
        globalLibraryObjFactory.createOrganizationDataType();
    orgDataType.setAddress(getAddressType(orgContactRolodexDetailsBean, null));  
    orgDataType.setDUNSID(orgMaintFormBean.getDunsNumber());
    orgDataType.setOrganizationName(orgMaintFormBean.getOrganizationName());
    // start addition for box 5 changes
     
    HashMap hmUnit = getLeadUnit();
    if (hmUnit != null){
        String leadUnit = hmUnit.get("UNIT_NUMBER").toString();
        String leadUnitName = hmUnit.get("UNIT_NAME").toString();
  
        //JM 7-19-2012 changed department name to OSP
//JM        orgDataType.setDepartmentName(leadUnitName);
    	orgDataType.setDepartmentName("Office of Sponsored Programs");
        //END JM     
     
        String division = s2sTxnBean.fn_get_division(leadUnit);
        if (division != null) {
             if(division.length()>30)
                   division = division.substring(0,30);
        }
        orgDataType.setDivisionName(division);
  
    }
 
    // end addition for box 5 changes
    
    return orgDataType;
  
   }
    
    private gov.grants.apply.system.globallibrary_v2.AddressDataType getAddressType(RolodexDetailsBean rolodexDetailsBean,
                                                         DepartmentPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
     AddressDataType addressType = globalLibraryObjFactory.createAddressDataType();
     HashMap hmState;
     HashMap hmCountry;
     
    if (rolodexDetailsBean != null) {
        //rolodex
         //case 3613
         String address1 = rolodexDetailsBean.getAddress1();
         
         if (address1 != null){
            if (address1.length() > 55)
              address1 = address1.substring(0,55);
         }
         addressType.setStreet1(address1);
         
        String address2 = rolodexDetailsBean.getAddress2();
        
        if (address2 != null){
            if (address2.length() > 55)
                address2 = address2.substring(0,55);
        }
        addressType.setStreet2(address2);
           
        addressType.setCity(rolodexDetailsBean.getCity());
        
        //case 4249 - if country is not US, then use
        //state field to populate province.
        String stateProvince = null;
        
        if (rolodexDetailsBean.getState() != null) {
            hmState= null;
            hmState = rrSF424V11TxnBean.getStateName(rolodexDetailsBean.getState());
            if (hmState != null )
               if (hmState.get("STATE_NAME") != null)
               stateProvince = hmState.get("STATE_NAME").toString();
//               addressType.setState(hmState.get("STATE_NAME").toString());
         }
              
        
        if (rolodexDetailsBean.getPostalCode() != null)
            addressType.setZipPostalCode(UtilFactory.convertNull(rolodexDetailsBean.getPostalCode()));
             
     //   addressType.setCountry(rolodexDetailsBean.getCountry());
 
        if (rolodexDetailsBean.getCountry() != null ){
            hmCountry = null;
            hmCountry = rrSF424V11TxnBean.getCountryName(rolodexDetailsBean.getCountry());
            if (hmCountry != null ){
              if (hmCountry.get("COUNTRY_NAME") != null)
              addressType.setCountry(hmCountry.get("COUNTRY_NAME").toString());
            }
        }
        
         //case 4249 - use state field to populate province.
           if (addressType.getCountry() != null) {
               if (addressType.getCountry().equals ("USA: UNITED STATES"))
                      addressType.setState(stateProvince);
               else
                      addressType.setProvince(stateProvince);
                  
           }else
               //country is null for some reason
               addressType.setState(stateProvince);
            

    
        if(rolodexDetailsBean.getCounty() != null) {
            addressType.setCounty(rolodexDetailsBean.getCounty());
            
        }
    } else {
        //department person 
         //case 3613
         String address1 = personBean.getAddress1();
         if (address1 != null)
            if (address1.length() > 55)
                address1 = address1.substring(0,55);
         
         addressType.setStreet1(address1);

         String address2 = personBean.getAddress2();
         if (address2 != null)
            if (address2.length() > 55)
                address2 = address2.substring(0,55);
         
        addressType.setStreet2(address2);
         
         addressType.setCity(personBean.getCity());
        //case 4249 - if country is not US, then use
        //state field to populate province.
         String stateProvince = null;
         if (personBean.getState() != null) {
            hmState= null;
            hmState = rrSF424V11TxnBean.getStateName(personBean.getState());
            if (hmState != null ){
                if (hmState.get("STATE_NAME") != null)
                stateProvince = (String)hmState.get("STATE_NAME");
              //  addressType.setState((String)hmState.get("STATE_NAME"));

            }
         }
      
         if (personBean.getPostalCode() != null)
             addressType.setZipPostalCode(personBean.getPostalCode());

         if (personBean.getCountryCode() != null ){
            hmCountry = null;
            hmCountry = rrSF424V11TxnBean.getCountryName(personBean.getCountryCode());
            if (hmCountry != null )
              if (hmCountry.get("COUNTRY_NAME")!= null)
              addressType.setCountry(hmCountry.get("COUNTRY_NAME").toString());
        }
        //case 4249 - if country is not US, then use
        //state field to populate province.
           if (addressType.getCountry() != null) {
               if (addressType.getCountry().equals ("USA: UNITED STATES")){
                      addressType.setState(stateProvince);
                  } else{ 
                      addressType.setProvince(stateProvince);
                  }
           }else{
               //country is null for some reason
               addressType.setState(stateProvince);
            }
         
         if (personBean.getCounty() != null)
             addressType.setCounty(personBean.getCounty());

         
    }
    return addressType;
   }
    
    
  private RRSF42412Type.ApplicantTypeType getApplicantType()
                  throws JAXBException,  CoeusException,DBException{
   RRSF42412Type.ApplicantTypeType applicantType = objFactory.createRRSF42412TypeApplicantTypeType();
   OrganizationListBean[] orgTypes = orgMaintDataTxnBean.getSelectedOrganizationList(orgMaintFormBean.getOrganizationId());
   RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType smallBus =
    objFactory.createRRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeType();
   RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsSociallyEconomicallyDisadvantagedType disadv =
    objFactory.createRRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsSociallyEconomicallyDisadvantagedType();
   RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsWomenOwnedType womenType =
        objFactory.createRRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsWomenOwnedType();
   boolean smallBusflag = false;
   String applicantTypeStr = "";
   String applicantTypeOtherSpecifyStr = "";
   
   //use first org type in list . schema allows for only one type
 
   int orgTypeCode = orgTypes[0].getOrganizationTypeCode();
  
   if (orgTypeCode > 0 ){
       HashMap hmAppType = rrSF424V11TxnBean.getApplicantType(orgTypeCode);
           if (hmAppType != null ){
               applicantTypeStr =  hmAppType.get("APPLICANT_TYPE") == null? null : hmAppType.get("APPLICANT_TYPE").toString();
               applicantType.setApplicantTypeCode(applicantTypeStr);
               switch (orgTypeCode){
                    case 3: {
                       applicantTypeOtherSpecifyStr = "Federal Government" ;
                       break;
                    }case 14: {
                        applicantTypeOtherSpecifyStr ="socially and Economically Disadvantaged";
                        break;
                    }case 15: {
                        applicantTypeOtherSpecifyStr ="Women owned";
                        break;
                    }
                }
           }
       }
      



    
//  
//       }case 14: {
//           //disadvantaged
//           applicantType.setApplicantTypeCode("P: Other (specify)");
//           disadv.setValue("Yes");
//           smallBus.setApplicantTypeCode("O: Small Business");
//           smallBus.setIsSociallyEconomicallyDisadvantaged(disadv);
//           smallBusflag = true;
//           break;
//       }case 15: {
//           //women owned
//           applicantType.setApplicantTypeCode("P: Other (specify)");
//           womenType.setValue("Yes");
//           smallBus.setApplicantTypeCode("O: Small Business");
//           smallBus.setIsWomenOwned(womenType);
//           smallBusflag = true;
//           break;
//     
//        }case 21: {
//           applicantType.setApplicantTypeCode("F: State-Controlled Institution of Higher Education");
//           break;
//        }case 22: {         
//           applicantType.setApplicantTypeCode("B: County Government");
//           break;
//        }case 23: {          
//           applicantType.setApplicantTypeCode("D: Special District Governments");
//           break;
//        }case 24: {         
//           applicantType.setApplicantTypeCode("E: Independent School District");
//           break;
//        }case 25: {          
//           applicantType.setApplicantTypeCode("H: Public/Indian Housing Authority");
//           break;
//        }case 26: {          
//           applicantType.setApplicantTypeCode("I: Native American Tribal Organization (other than Federally recognized)");
//           break;
//        }default: {
//            applicantType.setApplicantTypeCode("P: Other (specify)");
//            break;
//        }
//        }
//   
//        if (smallBusflag) {
//           applicantType.setSmallBusinessOrganizationType(smallBus);
//        }
//       
   
   
    
   return applicantType;
    }

   private HashMap getSubmissionType()
                throws CoeusException, DBException{
   HashMap hmSubmission = new HashMap();
   HashMap hmResult = s2sTxnBean.getSubmissionType(propNumber);
  
   String subType = hmResult.get("SUBMISSION_TYPE_DESC").toString();
   String revisionCode=null;
   if (hmResult.get("REVISION_CODE") != null)
        revisionCode = hmResult.get("REVISION_CODE").toString();
   String revisionOtherDesc=null;
   if(hmResult.get("REVISION_DESC") != null)
      revisionOtherDesc = hmResult.get("REVISION_DESC").toString();
     
   hmSubmission.put("SubmissionType",subType);
   hmSubmission.put("RevisionCode",revisionCode);
   if(revisionOtherDesc != null)
     hmSubmission.put("RevisionOther",revisionOtherDesc);
       
   return hmSubmission;
   }
   
   
   private RRSF42412Type.ApplicationTypeType getApplicationType()
                  throws JAXBException,  CoeusException,DBException{
    RRSF42412Type.ApplicationTypeType applicationType = objFactory.createRRSF42412TypeApplicationTypeType();
    String otherAgencySubmissionExplanation;
     
    
    HashMap hmApplicationType = s2sTxnBean.getApplicationType(propNumber);
    String applicationTypeDesc = hmApplicationType.get("APPLICATIONTYPE").toString();
    applicationType.setApplicationTypeCode(applicationTypeDesc);
    
    if (applicationTypeDesc.equals("Revision") ) {
        String revisionCode = null;
        if (hmSubmission.get("RevisionCode") != null){
            revisionCode = hmSubmission.get("RevisionCode").toString();
            applicationType.setRevisionCode(revisionCode);           
        }
     
        String revisionCodeOtherDesc = null;
        if (hmSubmission.get("RevisionOther") != null) {
            revisionCodeOtherDesc = hmSubmission.get("RevisionOther").toString();
            applicationType.setRevisionCodeOtherExplanation(revisionCodeOtherDesc);
           
        }
    }
    
     ProposalYNQBean proposalYNQBean = getAnswer("15");
     String answer = "N: No";
     if (proposalYNQBean != null){
        answer = (proposalYNQBean.getAnswer().equals("Y") ? "Y: Yes" : "N: No");
     }
    
    applicationType.setIsOtherAgencySubmission(answer);
    if (answer.equals("Y: Yes")) {
        String answerExplanation = proposalYNQBean.getExplanation();
        //Get Sponsor name
        if (answerExplanation != null) {
            String sponsorName = deptPersonTxnBean.getDescForLookupCode(answerExplanation, "VALUELIST", "Agency_US GOV");
            if (sponsorName != null) {
                int length = sponsorName.length();
                if(length > 20){
                    //truncate
                    int start, end;
                    if(length > 25){
                        start = 5;
                        end  = 25;
                    }else{
                        end = length;
                        start = end - 20;
                    }
                    sponsorName = sponsorName.substring(start, end);
                }
                answerExplanation = sponsorName;
            }
        }
        applicationType.setOtherAgencySubmissionExplanation(answerExplanation);
    }
     return applicationType;
    }
   
    private RRSF42412Type.ProposedProjectPeriodType  getProjectPeriod()
                  throws JAXBException,  CoeusException,DBException{
     RRSF42412Type.ProposedProjectPeriodType projPeriodType = 
           objFactory.createRRSF42412TypeProposedProjectPeriodType();             
     projPeriodType.setProposedStartDate(getCal(propDevFormBean.getRequestStartDateInitial()));
     projPeriodType.setProposedEndDate(getCal(propDevFormBean.getRequestEndDateInitial()));
     return projPeriodType;
       
   }
     
    private RRSF42412Type.CongressionalDistrictType  getCongDistrict()
                  throws JAXBException,  CoeusException,DBException{
     RRSF42412Type.CongressionalDistrictType congDistrictType = 
           objFactory.createRRSF42412TypeCongressionalDistrictType();             
     congDistrictType.setApplicantCongressionalDistrict(orgMaintFormBean.getCongressionalDistrict());
//     congDistrictType.setProjectCongressionalDistrict(perfOrgFormBean.getCongressionalDistrict());
     return congDistrictType;
       
   }
      
   private RRSF42412Type.StateReviewType  getStateReviewType()
                  throws JAXBException,  CoeusException,DBException{
   
    RRSF42412Type.StateReviewType stateReviewType = objFactory.createRRSF42412TypeStateReviewType();
    //JIRA COEUSQA-3658 - START
       ProposalYNQBean propYNQBean = getQuestionnaireAnswer("129");
       if (propYNQBean != null) {//JIRA COEUSDEV-1094
           String answer = propYNQBean.getAnswer();
           if (answer.equals("Y")) {
                stateReviewType.setStateReviewCodeType("Y: Yes");

              propYNQBean = getQuestionnaireAnswer("130");
               if (propYNQBean != null) {
                   String strDate = propYNQBean.getAnswer();
                   SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                   stateReviewType.setStateReviewCodeType("Y: Yes");
                   try {
                       java.util.Date date = dateFormat.parse(strDate);
                       stateReviewType.setStateReviewDate(getCal(new Date(date.getTime())));
                       } catch (ParseException pex) {
                       //Do Nothing
                   }
               }
              
              
           } else {
               propYNQBean = getQuestionnaireAnswer("131");
               String explanation = propYNQBean.getAnswer();
               if (explanation.equals("Not Selected")) {
                   explanation = "Program has not been selected by state for review";
               } else {
                   explanation = "Program is not covered by E.O. 12372";
               }
               stateReviewType.setStateReviewCodeType(explanation);
           }
       }
    //JIRA COEUSQA-3658 - END
                                           
    return stateReviewType;
   }
  
   private AORInfoType getAORInfoType() throws JAXBException,  CoeusException,DBException{
   
    AORInfoType aorInfoType = objFactory.createAORInfoType();
    
    aorInfoType.setName(getNameType(aorBean));
    
    if (aorBean.getPrimaryTitle() != null){
        if(aorBean.getPrimaryTitle().length()>45)
         aorInfoType.setTitle(aorBean.getPrimaryTitle().substring(0,45));
        else
         aorInfoType.setTitle(aorBean.getPrimaryTitle());
    }
           
    
    aorInfoType.setAddress(getAddressType(null,aorBean));
    aorInfoType.setPhone(aorBean.getOfficePhone());
    if (aorBean.getFaxNumber() != null) aorInfoType.setFax(aorBean.getFaxNumber());           
        
    if (aorBean.getDirDept() != null) aorInfoType.setDepartmentName(aorBean.getDirDept());
    aorInfoType.setEmail(aorBean.getEmailAddress());
    aorInfoType.setOrganizationName(orgMaintFormBean.getOrganizationName());
    if (aorBean.getHomeUnit() != null) aorInfoType.setDivisionName(aorBean.getHomeUnit());
   
    return aorInfoType;
   }
  
   private ProposalYNQBean getQuestionnaireAnswer(String questionID) throws CoeusException, DBException {
         Vector vecYNQ = propDevFormBean.getPropQuestionnaireAnswers();
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

     //ynq questions
   private ProposalYNQBean getAnswer(String questionID) throws CoeusException,DBException {
       
        String question;
        ProposalYNQBean   proposalYNQBean = null;
        //QUESTIONNAIRE ENHANCEMENT STARTS
        String answerId = null;
        String answer = null;
        Vector vecYNQ;
        if(questionID.equals("15")){
            vecYNQ=propDevFormBean.getPropQuestionnaireAnswers();
            questionID="128";
            answerId="111";
            answer = "Y";
        }else{
            vecYNQ= propDevFormBean.getPropYNQAnswerList();
        }
        if (vecYNQ != null) {
            ProposalYNQBean   tempBean;
              for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                tempBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                question = tempBean.getQuestionId();
           
                if (question.equals( questionID)){
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
        return proposalYNQBean;
        //QUESTIONNAIRE ENHANCEMENT ENDS
    }
    
  //case 3329 - changed this method to get two types of attachments
    //private  AttachedFileDataType  getPreAppAttachment() 
  //  private AttachedFileDataType getAttachments()
    private void getAttachments()
    throws JAXBException, CoeusException, DBException {
      
    AttachedFileDataType preAppAttachmentType;
    AttachedFileDataType sflllAttachmentType;
    AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    preAppAttachmentType = attachmentsObjFactory.createAttachedFileDataType();
    sflllAttachmentType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();

     hashValueType = globalObjFactory.createHashValueType();
     ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
     ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
 
     int narrativeType;
     String propNumber = propDevFormBean.getProposalNumber();
     int moduleNum;
     LinkedHashMap hmArg = new LinkedHashMap();

     Attachment attachment = new Attachment();
     
     S2STxnBean s2sTxnBean = new S2STxnBean();
     Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
   
     int size=vctNarrative==null?0:vctNarrative.size();
    
      for (int row=0; row < size;row++) {
        proposalNarrativePDFSourceBean =(ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
        
        moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
        narrativeType = s2sTxnBean.getNarrativeType(propNumber,moduleNum);
        HashMap narrMap = s2sTxnBean.getNarrativeInfo(propNumber, moduleNum);
        
        if (narrativeType == 6){
            ProposalNarrativePDFSourceBean narrPdfBean =  proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
            hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));
            String description = narrMap.get("DESCRIPTION").toString();
            hmArg.put(ContentIdConstants.DESCRIPTION, description);
            attachment = getAndAddNarrative(hmArg, narrPdfBean);
 
            if (attachment.getContent() != null){
                //populate attachment info in schema element
                preAppAttachmentType = getAttachedFileType(attachment, attachmentsObjFactory);
                 if (preAppAttachmentType.getFileName() != null) rrSF424.setPreApplicationAttachment(preAppAttachmentType);
            }   
         } else if (narrativeType == 86){
             ProposalNarrativePDFSourceBean narrPdfBean =  proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
            hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));
            String description = narrMap.get("DESCRIPTION").toString();
            hmArg.put(ContentIdConstants.DESCRIPTION, description);
            attachment = getAndAddNarrative(hmArg, narrPdfBean);
 
            if (attachment.getContent() != null){
                //populate attachment info in schema element
                sflllAttachmentType = getAttachedFileType(attachment, attachmentsObjFactory);
                 if (sflllAttachmentType.getFileName() != null) rrSF424.setSFLLLAttachment(sflllAttachmentType);
            }   
         }  
   
        }
                             
       return;
   }
  
   private String checkNull (String s) {
       
       if (s == null){
           return "Unknown";
       }else {
           return s;
       }
      
   }

    private static BigDecimal convDoubleToBigDec(double dblCost){
    
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         return bdCost; 
    }




   private Calendar getCal(Date date){
        return DateUtils.getCal(date);
    }

        
   
     
//    private Calendar getCal(Date date){
//        if(date==null)
//            return null;
//        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
//        cal.setTime(date);
//        return cal;
//    }
    
    private Calendar getTodayDate() {
        return DateUtils.getTodayDate();
    }
    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getSF424();
    }    
   
   
}
