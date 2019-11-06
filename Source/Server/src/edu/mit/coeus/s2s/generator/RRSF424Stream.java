/*
 * @(#)RRSF424Stream.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 16-SEP-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.s2s.generator;
 
import edu.mit.coeus.budget.bean.BudgetInfoBean;

import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.s2s.bean.RRSF424V11TxnBean;
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
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.mit.coeus.s2s.util.S2SHashValue;
//import edu.mit.coeus.utils.CoeusConstants;
//import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
//import gov.grants.apply.system.metagrantapplication.GrantApplicationType;
import gov.grants.apply.forms.rr_sf424_v1.*;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;
import gov.grants.apply.system.globallibrary_v1.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  Eleanor Shavell
 * @Created on October 19, 2004, 10:12 AM
 * Class for generating the object stream for grants.gov RR_SF424. It uses jaxb classes
 * which have been created under gov.grants.atapply package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * RR_SF424 schema.
 */


 public class RRSF424Stream extends S2SBaseStream{
    private gov.grants.apply.forms.rr_sf424_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalv1ObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;

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
    /* change box 5 contact person depending on parameter */
    private DepartmentPersonFormBean contactPersonBean;

    private RolodexDetailsBean perfOrgContactRolodexDetailsBean;
    private HashMap hmCFDA;
    private  HashMap hmSubmission;
    private String pIPersonID;
    private boolean isNonMITPerson = true;
    /** aorBean holds information for AOR */
    private DepartmentPersonFormBean aorBean;
    /* case 2317 ospAdminBean holds information for osp Administrator */
    private DepartmentPersonFormBean orgContactPersonBean;
    /** budgetInfoBean holds budget info */
    private BudgetInfoBean budgetInfoBean;
       //start case 2406
    private String organizationID;
    private String perfOrganizationID;
    //end case 2406
    
    private Hashtable propData;
    private String propNumber;
    private Calendar calendar;
   
    
   
    /** Creates a new instance of RRSF424Stream */
    public RRSF424Stream(){
        objFactory = new gov.grants.apply.forms.rr_sf424_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalv1ObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        propPrintingTxnBean = new ProposalPrintingTxnBean();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
        deptPersonTxnBean = new DepartmentPersonTxnBean();
        budgetDataTxnBean = new BudgetDataTxnBean();
        hmCFDA = new HashMap();
        s2sTxnBean = new S2STxnBean();
    } 
   
    private RRSF424Type getSF424() throws CoeusXMLException,CoeusException,DBException{
        RRSF424Type rrSF424 = null;
        try{
           rrSF424 = objFactory.createRRSF424();
            
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
            // get PI information
            pIPersonID = getPIPersonId();
            //get budget info
            budgetInfoBean = getBudgetInfo();
            //get AOR
            aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber);   
                
            String unitNumber,unitName=null;
            unitNumber = deptPersonTxnBean.getHomeUnit(aorBean.getPersonId());
            if(unitNumber != null) {
                unitName = deptPersonTxnBean.getUnitName(unitNumber);
                if(unitName.length()>30){
                    unitName = deptPersonTxnBean.getUnitName(unitNumber).substring(0,30);
                }
                  
            } //else unitName = "Unknown";
               
            aorBean.setUnitName (unitName);
        
            //JM 02-07-2013	hard-coding central office
            //aorBean.setDirDept(unitName);
            aorBean.setDirDept("Office of Sponsored Programs");    
            // JM END
        
           /*start case 2317*/
       //     orgContactPersonBean = propPrintingTxnBean.getOrgContactPerson(propNumber);
              
            
           /* end case 2317 */
           rrSF424.setSubmittedDate(getTodayDate());
           
           hmSubmission = getSubmissionType();
           
           rrSF424.setSubmissionTypeCode(hmSubmission.get("SubmissionType").toString());
           rrSF424.setFormVersion("1.0"); //hard coding
           rrSF424.setStateID(orgContactRolodexDetailsBean.getState());
          
           rrSF424.setApplicantInfo(getAppInfoType()); 
           rrSF424.setEmployerID(orgMaintFormBean.getFederalEmployerID());
           rrSF424.setApplicantType( getApplicantType());
           rrSF424.setApplicationType(getApplicationType());
           rrSF424.setFederalAgencyName(propDevFormBean.getSponsorName());      
           rrSF424.setProjectTitle(propDevFormBean.getTitle());  
           rrSF424.setLocation(perfOrgContactRolodexDetailsBean.getState());
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
       //    }else{
        //        throw new CoeusException("exceptionCode.90012");
           }
           rrSF424.setEstimatedProjectFunding(getProjectFunding());
           rrSF424.setTrustAgree("Yes"); //hard coding
           rrSF424.setStateReview(getStateReviewType());
           rrSF424.setAORInfo(getAORInfoType());
           rrSF424.setAORSignature(aorBean.getFullName());
           rrSF424.setAORSignedDate(getTodayDate());
           
           AttachedFileDataType preAppAttachment = getPreAppAttachment();
           if (preAppAttachment.getFileName() != null) rrSF424.setPreApplicationAttachment(preAppAttachment);
            
                     
         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRSF424Stream","getSF424()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return rrSF424;
    }
   
  
   
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
    
     //start case 2406
    private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = s2sTxnBean.getOrganizationID(propNumber,"O");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
         //start case 3110
        RRSF424V11TxnBean rrSF424V11TxnBean = new RRSF424V11TxnBean();
        OrganizationMaintenanceFormBean  orgBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
        //get congressional district
        HashMap hmCongDist = rrSF424V11TxnBean.getCongDistrict(propNumber, orgBean.getOrganizationId(),1);
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
         RRSF424V11TxnBean rrSF424V11TxnBean = new RRSF424V11TxnBean();
         OrganizationMaintenanceFormBean  orgBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
        //get congressional district
          HashMap hmCongDist = rrSF424V11TxnBean.getCongDistrict(propNumber, orgBean.getOrganizationId(),2);
        String congDist = (String) hmCongDist.get("CONGDIST").toString();
         orgBean.setCongressionalDistrict(congDist);
         return orgBean;
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
    }
    
    
    //end case 2406
//    private OrganizationMaintenanceFormBean getOrgData(String orgID)
//       throws CoeusXMLException,CoeusException,DBException{
//        if(orgID==null) 
//            throw new CoeusXMLException("Organization id is Null");   
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(orgID);
//    }
    
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
       if (budgetInfoBean != null ) {
       if (budgetInfoBean.isBudgetModularFlag() == true) {
         //get modular budget amounts instead of budget detail amounts
            HashMap hmIDC = s2sTxnBean.getIDCForModBudget(propNumber,version);
            
            budgetInfoBean.setTotalIndirectCost(Double.parseDouble(
                                                hmIDC.get("IDC") == null ? "0" :
                                                hmIDC.get("IDC").toString()));
                                              
            HashMap hmTotCost = s2sTxnBean.getTotForModBudget(propNumber,version);
            
            budgetInfoBean.setTotalCost(Double.parseDouble(
                                                hmTotCost.get("TOTAL_COST") == null ? "0" :
                                                hmTotCost.get("TOTAL_COST").toString()));
                                                
            HashMap hmCostShare = s2sTxnBean.getCostShareForModBudget(propNumber,version);
            
            budgetInfoBean.setCostSharingAmount(Double.parseDouble(
                                                hmTotCost.get("COST_SHARE") == null ? "0" :
                                                hmTotCost.get("COST_SHARE").toString()));                                  
        }
            
       }
        return budgetInfoBean;

     
   }
   
   
  
   private gov.grants.apply.forms.rr_sf424_v1.OrganizationContactPersonDataType getPDPI()
                  throws JAXBException,  CoeusException,DBException{
        gov.grants.apply.forms.rr_sf424_v1.OrganizationContactPersonDataType PDPI = 
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
             
            String division = s2sTxnBean.fn_get_division(pIBean.getHomeUnit());
            */
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
                     
       HashMap hmUnit = s2sTxnBean.getLeadUnit(propNumber);
      
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
   
    private RRSF424Type.EstimatedProjectFundingType getProjectFunding()
                  throws JAXBException,  CoeusException,DBException{
   
      RRSF424Type.EstimatedProjectFundingType projectFundingType = 
            objFactory.createRRSF424TypeEstimatedProjectFundingType();
     if (budgetInfoBean != null){
       projectFundingType.setTotalEstimatedAmount(convDoubleToBigDec(budgetInfoBean.getTotalCost()));
       projectFundingType.setTotalfedNonfedrequested(convDoubleToBigDec(budgetInfoBean.getTotalCost() +
                                                    (budgetInfoBean.getCostSharingAmount())));
       projectFundingType.setEstimatedProgramIncome(s2sTxnBean.getProjectIncome(propNumber));
   
     }
     return projectFundingType;
    }
     
   private RRSF424Type.ApplicantInfoType getAppInfoType()
                  throws JAXBException,  CoeusException,DBException{
      /* for box5 - refer to case 2317 - use parameter to decide source */  
     RRSF424Type.ApplicantInfoType appInfoType = objFactory.createRRSF424TypeApplicantInfoType();
   
      HashMap hmContactType = (HashMap)s2sTxnBean.getContactType(propNumber);
      String contactType = hmContactType.get("CONTACT_TYPE").toString();
      
      if (contactType.equals("I")) {
           // use organization rolodex contact
          appInfoType.setContactPersonInfo(getAppInfoContactType(orgContactRolodexDetailsBean));
      } else {
          //contact will come from osp$unit or osp$unit_administrators
          HashMap hmContactPerson = s2sTxnBean.getContactPerson(propNumber, contactType);
          if (hmContactPerson != null) {
          DepartmentPersonFormBean contactPersonBean = getUnitContactDetails(hmContactPerson);
         
          appInfoType.setContactPersonInfo(getAppInfoContactType(contactPersonBean));
          }
      }

     appInfoType.setOrganizationInfo(getOrgDataType());
       
     return appInfoType;
    }                
                      


     
    private RRSF424Type.ApplicantInfoType.ContactPersonInfoType
          getAppInfoContactType(DepartmentPersonFormBean deptPersonBean)
                  throws JAXBException,  CoeusException,DBException{
    RRSF424Type.ApplicantInfoType.ContactPersonInfoType appInfoContact =
        objFactory.createRRSF424TypeApplicantInfoTypeContactPersonInfoType();
    appInfoContact.setName(getNameType(deptPersonBean));
    appInfoContact.setPhone(deptPersonBean.getOfficePhone()==null?"000-000-0000":
                            deptPersonBean.getOfficePhone());
    appInfoContact.setFax(deptPersonBean.getFaxNumber());
    appInfoContact.setEmail(deptPersonBean.getEmailAddress());
    return appInfoContact;
  
   }
   
    private RRSF424Type.ApplicantInfoType.ContactPersonInfoType
          getAppInfoContactType(RolodexDetailsBean rolodexDetailsBean)
                  throws JAXBException,  CoeusException,DBException{
    RRSF424Type.ApplicantInfoType.ContactPersonInfoType appInfoContact =
        objFactory.createRRSF424TypeApplicantInfoTypeContactPersonInfoType();
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
   
    HumanNameDataType nameType = globalObjFactory.createHumanNameDataType(); 
   
    nameType.setFirstName(rolodexDetailsBean.getFirstName());     
    nameType.setLastName(rolodexDetailsBean.getLastName());
    if(rolodexDetailsBean.getMiddleName() != null)
     nameType.setMiddleName(UtilFactory.convertNull(rolodexDetailsBean.getMiddleName()));
   
    return nameType;
   }
  
   //overloaded for Dept person
   private HumanNameDataType  getNameType(DepartmentPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
  
    HumanNameDataType nameType = globalObjFactory.createHumanNameDataType(); 
        
     nameType.setFirstName(UtilFactory.convertNull(personBean.getFirstName()));
     nameType.setLastName(UtilFactory.convertNull(personBean.getLastName()));
     if(personBean.getMiddleName()!=null)
        nameType.setMiddleName(personBean.getMiddleName());
     return nameType;
    }

   //overloaded for ProposalPersonFormBean person
   private HumanNameDataType  getNameType(ProposalPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
   
    HumanNameDataType nameType = globalObjFactory.createHumanNameDataType(); 
        
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
   
    private OrganizationDataTypeV2 getOrgDataType()
                  throws JAXBException,  CoeusException,DBException{
    OrganizationDataTypeV2 orgDataType =
        globalObjFactory.createOrganizationDataTypeV2();
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
    
    private AddressRequireCountryDataType getAddressType(RolodexDetailsBean rolodexDetailsBean,
                                                         DepartmentPersonFormBean personBean)
                  throws JAXBException,  CoeusException,DBException{
    AddressRequireCountryDataType addressType =
        globalObjFactory.createAddressRequireCountryDataType();
    if (rolodexDetailsBean != null) {
        //rolodex
        //case 3613- truncate address
        if (rolodexDetailsBean.getAddress1() != null) {
           if (rolodexDetailsBean.getAddress1().length() > 55)
               addressType.setStreet1(rolodexDetailsBean.getAddress1().substring(0,55));
           else
               addressType.setStreet1(rolodexDetailsBean.getAddress1());
         }  
        if (rolodexDetailsBean.getAddress2() != null) {
           if (rolodexDetailsBean.getAddress2().length() > 55)
               addressType.setStreet2(rolodexDetailsBean.getAddress2().substring(0,55));
           else
               addressType.setStreet2(rolodexDetailsBean.getAddress2());
         }      
                
//        addressType.setStreet1(rolodexDetailsBean.getAddress1());
//        if (rolodexDetailsBean.getAddress2() != null)
//            addressType.setStreet2(rolodexDetailsBean.getAddress2());
        addressType.setCity(rolodexDetailsBean.getCity());
        if (rolodexDetailsBean.getState() != null)
             addressType.setState(rolodexDetailsBean.getState());
        if (rolodexDetailsBean.getPostalCode() != null)
            addressType.setZipCode(UtilFactory.convertNull(rolodexDetailsBean.getPostalCode()));
        addressType.setCountry(rolodexDetailsBean.getCountry());
    } else {
        //department person 
        //case 3613- truncate address
        if (personBean.getAddress1() != null) {
           if (personBean.getAddress1().length() > 55)
               addressType.setStreet1(personBean.getAddress1().substring(0,55));
           else
               addressType.setStreet1(personBean.getAddress1());
         }  
         if (personBean.getAddress2() != null){
            if (personBean.getAddress2().length() > 55)
               addressType.setStreet2(personBean.getAddress2().substring(0,55));
            else
               addressType.setStreet2(personBean.getAddress2()); 
         }
        
//         addressType.setStreet1(personBean.getAddress1());

//         if (personBean.getAddress2() != null)
//             addressType.setStreet2(personBean.getAddress2());

         addressType.setCity(personBean.getCity());
         if (personBean.getState() != null) addressType.setState(personBean.getState());

         if (personBean.getPostalCode() != null)
             addressType.setZipCode(personBean.getPostalCode());

         addressType.setCountry(personBean.getCountryCode());
       
    }
    return addressType;
   }
    
    
  private RRSF424Type.ApplicantTypeType getApplicantType()
                  throws JAXBException,  CoeusException,DBException{
   RRSF424Type.ApplicantTypeType applicantType = objFactory.createRRSF424TypeApplicantTypeType();
   OrganizationListBean[] orgTypes = orgMaintDataTxnBean.getSelectedOrganizationList(orgMaintFormBean.getOrganizationId());
   RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType smallBus =
    objFactory.createRRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeType();
   RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsSociallyEconomicallyDisadvantagedType disadv =
    objFactory.createRRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsSociallyEconomicallyDisadvantagedType();
   RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsWomenOwnedType womenType =
        objFactory.createRRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsWomenOwnedType();
   boolean smallBusflag = false;
   
   //use first org type in list for now. schema allows for only one type
   int orgTypeCode = orgTypes[0].getOrganizationTypeCode();
   switch (orgTypeCode){
       case 1: {
           //local
           applicantType.setApplicantTypeCode("C: City or Township Government");
           break;
       }case 2: {
           //state
            applicantType.setApplicantTypeCode("A: State Government");
            break;
       }case 3: {
           //federal
           applicantType.setApplicantTypeCode("P: Other (specify)");
            break;
       }case 4: {
           //Private non-profit
           applicantType.setApplicantTypeCode("J: Nonprofit with 501C3 IRS status (other than Institution of Higher Education)");
            break;
       }case 5: {
           //Non-Profit
           applicantType.setApplicantTypeCode("K: Nonprofit without 501C3 IRS status (other than Institution of Higher Education)");
            break;
       }case 6: {
           //For-profit
           applicantType.setApplicantTypeCode("N: For-profit Organization (other than small business)");
            break;
       }case 7: {
          //Other
           applicantType.setApplicantTypeCode("P: Other (specify)");
            break;
       }case 8: {
           //Indian Tribal Government
           applicantType.setApplicantTypeCode("G: Native American Tribal Government (Federally Recognized)"); 
           break;
       }case 9: {
           //Individual
           applicantType.setApplicantTypeCode("M: Individual");
           break;
       }case 10: {
           //Inst of higher learning
           applicantType.setApplicantTypeCode("L: Private Institution of Higher Education");
           break;
       }case 11: {
           //Small Business
           applicantType.setApplicantTypeCode("O: Small Business");
           break;
       }case 14: {
           //disadvantaged
           applicantType.setApplicantTypeCode("P: Other (specify)");
           disadv.setValue("Yes");
           smallBus.setApplicantTypeCode("O: Small Business");
           smallBus.setIsSociallyEconomicallyDisadvantaged(disadv);
           smallBusflag = true;
           break;
       }case 15: {
           //women owned
           applicantType.setApplicantTypeCode("P: Other (specify)");
           womenType.setValue("Yes");
           smallBus.setApplicantTypeCode("O: Small Business");
           smallBus.setIsWomenOwned(womenType);
           smallBusflag = true;
           break;
     
        }case 21: {
           applicantType.setApplicantTypeCode("F: State-Controlled Institution of Higher Education");
           break;
        }case 22: {         
           applicantType.setApplicantTypeCode("B: County Government");
           break;
        }case 23: {          
           applicantType.setApplicantTypeCode("D: Special District Governments");
           break;
        }case 24: {         
           applicantType.setApplicantTypeCode("E: Independent School District");
           break;
        }case 25: {          
           applicantType.setApplicantTypeCode("H: Public/Indian Housing Authority");
           break;
        }case 26: {          
           applicantType.setApplicantTypeCode("I: Native American Tribal Organization (other than Federally recognized)");
           break;
        }default: {
            applicantType.setApplicantTypeCode("P: Other (specify)");
            break;
        }
        }
   
        if (smallBusflag) {
           applicantType.setSmallBusinessOrganizationType(smallBus);
        }
       
   
   
    
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
   
   
   private RRSF424Type.ApplicationTypeType getApplicationType()
                  throws JAXBException,  CoeusException,DBException{
    RRSF424Type.ApplicationTypeType applicationType = objFactory.createRRSF424TypeApplicationTypeType();
    RRSF424Type.ApplicationTypeType.OtherAgencySubmissionExplanationType explanation =
        objFactory.createRRSF424TypeApplicationTypeTypeOtherAgencySubmissionExplanationType();
   
    
    HashMap hmApplicationType = s2sTxnBean.getApplicationType(propNumber);
    String applicationTypeDesc = hmApplicationType.get("APPLICATIONTYPE").toString();
    applicationType.setApplicationTypeCode(applicationTypeDesc);
    
    if (applicationTypeDesc.equals("Revision") ) {
        String revisionCode = null;
        if (hmSubmission.get("RevisionCode") != null){
            revisionCode = hmSubmission.get("RevisionCode").toString();
            RRSF424Type.ApplicationTypeType.RevisionCodeType revisionCodeType =
                objFactory.createRRSF424TypeApplicationTypeTypeRevisionCodeType();
                revisionCodeType.setValue(revisionCode);
                revisionCodeType.setApplicationTypeCode(applicationTypeDesc);
                applicationType.setRevisionCode(revisionCodeType);
        }
     
    
        String revisionCodeOtherDesc = null;
        if (hmSubmission.get("RevisionOther") != null) {
            revisionCodeOtherDesc = hmSubmission.get("RevisionOther").toString();
            RRSF424Type.ApplicationTypeType.RevisionCodeOtherExplanationType revisionCodeOtherType =
                objFactory.createRRSF424TypeApplicationTypeTypeRevisionCodeOtherExplanationType();
  
            revisionCodeOtherType.setRevisionCode("E");  //fixed
            revisionCodeOtherType.setValue(revisionCodeOtherDesc);
         
            applicationType.setRevisionCodeOtherExplanation(revisionCodeOtherType);
        }
    }
    
     ProposalYNQBean proposalYNQBean = getAnswer("15");
     String answer = "No";
     if (proposalYNQBean != null){
        answer = (proposalYNQBean.getAnswer().equals("Y") ? "Yes" : "No");
     }
     
    applicationType.setIsOtherAgencySubmission(answer);
    if (answer.equals("Yes")) {
        explanation.setIsOtherAgencySubmission("Yes");
        String answerExplanation = proposalYNQBean.getExplanation();
        explanation.setValue(answerExplanation);
        applicationType.setOtherAgencySubmissionExplanation(explanation);
    }
     return applicationType;
    }
   
    private RRSF424Type.ProposedProjectPeriodType  getProjectPeriod()
                  throws JAXBException,  CoeusException,DBException{
     RRSF424Type.ProposedProjectPeriodType projPeriodType = 
           objFactory.createRRSF424TypeProposedProjectPeriodType();             
     projPeriodType.setProposedStartDate(getCal(propDevFormBean.getRequestStartDateInitial()));
     projPeriodType.setProposedEndDate(getCal(propDevFormBean.getRequestEndDateInitial()));
     return projPeriodType;
       
   }
     
    private RRSF424Type.CongressionalDistrictType  getCongDistrict()
                  throws JAXBException,  CoeusException,DBException{
     RRSF424Type.CongressionalDistrictType congDistrictType = 
           objFactory.createRRSF424TypeCongressionalDistrictType();             
     congDistrictType.setApplicantCongressionalDistrict(orgMaintFormBean.getCongressionalDistrict());
     congDistrictType.setProjectCongressionalDistrict(perfOrgFormBean.getCongressionalDistrict());
     return congDistrictType;
       
   }
      
    private RRSF424Type.StateReviewType  getStateReviewType()
                  throws JAXBException,  CoeusException,DBException{
   
    RRSF424Type.StateReviewType stateReviewType = objFactory.createRRSF424TypeStateReviewType();
    //JIRA COEUSQA-3658 - START
       ProposalYNQBean propYNQBean = getQuestionnaireAnswer("129");
        if (propYNQBean != null) { //JIRA COEUSDEV-1094
            String answer = propYNQBean.getAnswer();
            if (answer.equals("Y")) {
                propYNQBean = getQuestionnaireAnswer("130");
                if (propYNQBean != null) {
                    String strDate = propYNQBean.getAnswer();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    stateReviewType.setStateReviewCodeType("Y: Yes");
                    try {
                        java.util.Date date = dateFormat.parse(strDate);
                        RRSF424Type.StateReviewType.StateReviewDateType stateReviewDateType = objFactory.createRRSF424TypeStateReviewTypeStateReviewDateType();
                        stateReviewDateType.setValue(getCal(new Date(date.getTime())));
                        stateReviewType.setStateReviewDate(stateReviewDateType);
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
  //  else
        //title is mandatory in schema
       //  aorInfoType.setTitle(UtilFactory.convertNull(aorBean.getPrimaryTitle()));
          
    
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
        Vector vecYNQ;
        if(questionID.equals("15")){
            vecYNQ=propDevFormBean.getPropQuestionnaireAnswers();
            questionID="128";
        }else{
            vecYNQ= propDevFormBean.getPropYNQAnswerList();
        }
        if (vecYNQ != null) {    
              for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                proposalYNQBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                question = proposalYNQBean.getQuestionId();
           
                if (question.equals( questionID)){
                     return proposalYNQBean;
                }
              }
        } 
        return proposalYNQBean;
        //QUESTIONNAIRE ENHANCEMENT ENDS
    }
    
  private  AttachedFileDataType  getPreAppAttachment() 
    throws JAXBException, CoeusException, DBException {
      
    AttachedFileDataType preAppAttachmentType;
    AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    preAppAttachmentType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
    hashValueType = globalv1ObjFactory.createHashValueType();
  
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
 
      
        if (narrativeType == 6){
       
            hmArg.put(ContentIdConstants.PROPOSAL, propNumber);
            hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));
            
            attachment = getAttachment(hmArg);
             if (attachment == null) {
             //attachment does not already exist - we need to get it and add it
                 String contentId = createContentId(hmArg);
                 
                 proposalNarrativePDFSourceBean = 
                       proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                 attachment = new Attachment();
                 attachment.setContent( proposalNarrativePDFSourceBean.getFileBytes());
                 String contentType = "application/octet-stream";
                 attachment.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
                 attachment.setContentId(contentId);
                 attachment.setContentType(contentType);
                                
                 addAttachment(hmArg, attachment);                
            }
            break;
       }   
    }       
    if (attachment.getContent() != null){
        //populate attachment info in schema element
    
        preAppAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
        try{
         preAppAttachmentType.setHashValue(S2SHashValue.getValue(attachment.getContent())); 
         } catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "RRSF424Stream", "getPreAppAttachment");
            throw new JAXBException(ex);
        }
        preAppAttachmentType.setMimeType("application/octet-stream");
        fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        fileLocation.setHref(attachment.getContentId());
        preAppAttachmentType.setFileLocation(fileLocation);
    }
    
     return preAppAttachmentType;
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
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("00:00")); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getSF424();
    }    
    
   
}
