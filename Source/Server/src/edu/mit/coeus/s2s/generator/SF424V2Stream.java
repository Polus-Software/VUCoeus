/*
 * SF424V2Stream.java
 *
 * Created on April 19, 2006
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationListBean;
import edu.mit.coeus.organization.bean.OrganizationYNQBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType;
import gov.grants.apply.coeus.extrakeyperson.ExtraKeyPersonListType;

import gov.grants.apply.forms.sf424_v2.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

/**
 *
 * @author  jenlu
 */
public class SF424V2Stream extends S2SBaseStream{ 
    private gov.grants.apply.forms.sf424_v2.ObjectFactory objFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    
    //txn beans
    private SF424V2TxnBean sf424V2TxnBean;
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
    private  HashMap hmSubmission;
    private String pIPersonID;
    private boolean isNonMITPerson = true;
    /** aorBean holds information for AOR */
    private DepartmentPersonFormBean aorBean;
    /** budgetInfoBean holds budget info */
    private BudgetInfoBean budgetInfoBean;
    
    
    private Hashtable propData;
    private String propNumber;
    private String applicantTypeOtherSpecify = null;
    private String federalDebtExp;
    private Calendar calendar;
    private Date stateReviewDate = null;
    
    private UtilFactory utilFactory;   
    
    private final static String ORG_FED_DEBT_YNQ = "I7";
    //start case 2406
    private String organizationID;
    private String perfOrganizationID;
    //end case 2406
    
    /** Creates a new instance of SF424V2Stream */
    public SF424V2Stream() {
        objFactory = new gov.grants.apply.forms.sf424_v2.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
//        equipObjFactory = new gov.grants.apply.coeus.additionalequipment.ObjectFactory(); 
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
    
    private SF424Type  getSF424V2() throws CoeusXMLException,CoeusException,DBException{
        SF424Type sf424V2 = null;
        sf424V2TxnBean = new SF424V2TxnBean();
        HashMap hmInfo = new HashMap();
        hmInfo = sf424V2TxnBean.getType(propNumber);
        try{
            sf424V2 = objFactory.createSF424();
            //get proposal master info
            propDevFormBean = getPropDevData();
            //get supplementary info
            hmCFDA = getCFDA();
            /*start case 2406 
            //get applicant organization info
            orgMaintFormBean = getOrgData(propDevFormBean.getOrganizationId());
            //get performing organization info
            perfOrgFormBean = getOrgData(propDevFormBean.getPerformingOrganizationId());
            */
            orgMaintFormBean = getOrgData();
            perfOrgFormBean = getPerfOrgData();
            
            /*end case 2406*/
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
            
           /**
            * FormVersion
            */
           sf424V2.setFormVersion("2.0");             
                    
           if (hmInfo != null && hmInfo.get("SUBMISSION_TYPE_DESC") != null){
                sf424V2.setSubmissionType(hmInfo.get("SUBMISSION_TYPE_DESC").toString());
           }
           
           if (hmInfo != null && hmInfo.get("APPLICATIONTYPE") != null){
                sf424V2.setApplicationType(hmInfo.get("APPLICATIONTYPE").toString());
           }
           
           if (hmInfo != null && hmInfo.get("REVISION_TYPE") != null){
                sf424V2.setRevisionType(hmInfo.get("REVISION_TYPE").toString());
                if (sf424V2.getRevisionType().startsWith("E")){
                    sf424V2.setRevisionOtherSpecify(hmInfo.get("REVISION_OTHER_DESC").toString());
                }
           }
           
           sf424V2.setDateReceived(getTodayDate());
           sf424V2.setApplicantID(propNumber);
           
           String federalEntitytId = getFederalID();
           if (federalEntitytId != null && !federalEntitytId.equals( "-1")) {
              // case 3146
              // sf424V2.setFederalEntityIdentifier(federalEntitytId);
                 sf424V2.setFederalAwardIdentifier(federalEntitytId);
           }
           
           //case 3146
            if (hmInfo != null && hmInfo.get("FEDID") != null){
                sf424V2.setFederalEntityIdentifier(hmInfo.get("FEDID").toString());
            }
           //end case 3146
           
           //box 6 and box 7 is state use only, so we don't need to do it      
//           sf424V2.setStateApplicationID(orgContactRolodexDetailsBean.getState());
           
           //Legal Name
//           sf424V2.setOrganizationName(orgContactRolodexDetailsBean.getOrganization());
           sf424V2.setOrganizationName(orgMaintFormBean.getOrganizationName());
           
           sf424V2.setEmployerTaxpayerIdentificationNumber(orgMaintFormBean.getFederalEmployerID());
           
           sf424V2.setDUNSNumber(orgMaintFormBean.getDunsNumber());
           
           sf424V2.setApplicant(getAddress(orgContactRolodexDetailsBean, null));
           String deptName = propDevFormBean.getOwnedByDesc(); //should be a lead unit
//           String deptName = orgContactRolodexDetailsBean.getOwnedByUnit();
           if(deptName != null && deptName.length() > 30){
                sf424V2.setDepartmentName(deptName.substring(0,30));
           }
           else{
                sf424V2.setDepartmentName(deptName);
           }
           String divisionName = s2sTxnBean.fn_get_division(propDevFormBean.getOwnedBy());
//           String divisionName = s2sTxnBean.fn_get_division(orgContactRolodexDetailsBean.getOwnedByUnit());
           if (divisionName != null && divisionName.length() > 30){
                sf424V2.setDivisionName(divisionName.substring(0,30));
           }
           else{
                sf424V2.setDivisionName(divisionName);
           }
           
           ProposalPersonFormBean personInfoBean = getPIPersonBean();
           
           if (personInfoBean != null){
               sf424V2.setContactPerson(getPersonName(personInfoBean));
               sf424V2.setTitle(personInfoBean.getDirTitle());
               
               sf424V2.setPhoneNumber(personInfoBean.getOfficePhone() == null? "000-000-0000":
                                  personInfoBean.getOfficePhone());
               sf424V2.setFax(personInfoBean.getFaxNumber());
               sf424V2.setEmail(personInfoBean.getEmailAddress());       
           }
           
           //set same as Legal Name (OrganizationName) now. may be change later
           sf424V2.setOrganizationAffiliation(orgMaintFormBean.getOrganizationName());
           
//           
//           sf424V2.setTitle(orgContactRolodexDetailsBean.getTitle());
////           sf424V2.setTitle(propDevFormBean.getTitle());
////           sf424V2.setOrganizationAffiliation(propDevFormBean.); 
//           sf424V2.setPhoneNumber(orgContactRolodexDetailsBean.getPhone() == null? "000-000-0000":
//                                  orgContactRolodexDetailsBean.getPhone());
//           sf424V2.setFax(orgContactRolodexDetailsBean.getFax());
//           sf424V2.setEmail(orgContactRolodexDetailsBean.getEMail());
                
           
           String appType = null;
           appType = getApplicantType(0);
           if (appType != null && !appType.equals(""))
                sf424V2.setApplicantTypeCode1(appType);
           
           appType = getApplicantType(1);
           if (appType != null && !appType.equals(""))
           sf424V2.setApplicantTypeCode2(appType);
           
           appType = getApplicantType(2);
           if (appType != null && !appType.equals(""))
           sf424V2.setApplicantTypeCode3(appType);
           
           if (applicantTypeOtherSpecify != null){
                sf424V2.setApplicantTypeOtherSpecify(applicantTypeOtherSpecify);
                applicantTypeOtherSpecify = null;
           }
           
           sf424V2.setAgencyName(propDevFormBean.getSponsorName());
           if (hmCFDA.get("CFDA") != null && !hmCFDA.get("CFDA").toString().equals("")) {
              sf424V2.setCFDANumber(hmCFDA.get("CFDA").toString());
           }
           
           //data from osp$eps_proposal.PROGRAM_ANNOUNCEMENT_TITLE
            //case 4503
           if (hmCFDA.get("ACTIVITY_TITLE") != null){
               int i = hmCFDA.get("ACTIVITY_TITLE").toString().length();
               if (i > 120)
                sf424V2.setCFDAProgramTitle(hmCFDA.get("ACTIVITY_TITLE").toString().substring(0,119));
               else
                sf424V2.setCFDAProgramTitle(hmCFDA.get("ACTIVITY_TITLE").toString());
           }
         //  sf424V2.setCFDAProgramTitle(hmCFDA.get("ACTIVITY_TITLE").toString()); 
           
           //get info from s2s_opportunity table
           hmInfo = null;
           hmInfo = sf424V2TxnBean.getS2sOpportunity(propNumber);
           
           //box12
           if (hmInfo != null && hmInfo.get("OPPORTUNITY_ID") != null){
               sf424V2.setFundingOpportunityNumber(hmInfo.get("OPPORTUNITY_ID").toString());
               if ( hmInfo.get("OPPORTUNITY_TITLE") != null){
                //get data from osp$s2s_opportunity.OPPORTUNITY_TITLE 
                //(this data same as osp$eps_proposal.PROGRAM_ANNOUNCEMENT_TITLE)
                sf424V2.setFundingOpportunityTitle(hmInfo.get("OPPORTUNITY_TITLE").toString());
               }
           }
           
           //box13
           if (hmInfo != null && hmInfo.get("COMPETETION_ID") != null){
               sf424V2.setCompetitionIdentificationNumber(hmInfo.get("COMPETETION_ID").toString());
               // we don't have this title.
//               sf424V2.setCompetitionIdentificationTitle("???");
           }
           
           //box14 
           //get data from osp$eps_prop_abstract
           hmInfo = null;
           hmInfo = sf424V2TxnBean.getAreasAffected(propNumber);
           if (hmInfo != null && hmInfo.get("AREAS_AFFECTED") != null){
               String areasAff = hmInfo.get("AREAS_AFFECTED").toString();
               if (areasAff.length() > 250 )
                   sf424V2.setAffectedAreas(areasAff.substring(0,250));
              else 
                  sf424V2.setAffectedAreas(areasAff);
           }
                      
           //box15
           sf424V2.setProjectTitle(propDevFormBean.getTitle());
           //attach supporting documents as specified in agency instructions
//           sf424V2.setAdditionalProjectTitle("???"); // do this in setAttachment()
           
           //box16 is only allowed 6D
           String congressionalDistrict = UtilFactory.setNullToUnknown(orgMaintFormBean.getCongressionalDistrict());
           if (congressionalDistrict != null && congressionalDistrict.length() > 6 )
               sf424V2.setCongressionalDistrictApplicant(congressionalDistrict.substring(0, 6));
           else 
               sf424V2.setCongressionalDistrictApplicant(congressionalDistrict);
           
           congressionalDistrict = null;           
           congressionalDistrict = UtilFactory.setNullToUnknown(perfOrgFormBean.getCongressionalDistrict());
           if (congressionalDistrict != null && congressionalDistrict.length() > 6 )
               sf424V2.setCongressionalDistrictProgramProject(congressionalDistrict.substring(0, 6));
           else 
               sf424V2.setCongressionalDistrictProgramProject(congressionalDistrict);           
           //attach an additional list op Program/Project Congressional Districts if needed
//           sf424V2.setAdditionalCongressionalDistricts("???"); //do this in setAttachment()
           
         
           
           //box17 
           sf424V2.setProjectStartDate(getCal(propDevFormBean.getRequestStartDateInitial()));
           sf424V2.setProjectEndDate(getCal(propDevFormBean.getRequestEndDateInitial()));
           
           //box18
           BigDecimal zero = new BigDecimal(0.00);
           if (budgetInfoBean != null){
                sf424V2.setFederalEstimatedFunding(convDoubleToBigDec(budgetInfoBean.getTotalCost()));
                sf424V2.setApplicantEstimatedFunding(convDoubleToBigDec(budgetInfoBean.getCostSharingAmount()));                
                sf424V2.setStateEstimatedFunding(zero);
                sf424V2.setLocalEstimatedFunding(zero);
                sf424V2.setOtherEstimatedFunding(zero);
                BigDecimal programIncome = s2sTxnBean.getProjectIncome(propNumber);
                sf424V2.setProgramIncomeEstimatedFunding(programIncome);
                BigDecimal totalEstimated = convDoubleToBigDec(budgetInfoBean.getTotalCost() 
                    + budgetInfoBean.getCostSharingAmount()).add(programIncome);
                sf424V2.setTotalEstimatedFunding(totalEstimated);
           }else{
               sf424V2.setFederalEstimatedFunding(zero);
               sf424V2.setApplicantEstimatedFunding(zero);                
               sf424V2.setStateEstimatedFunding(zero);
               sf424V2.setLocalEstimatedFunding(zero);
               sf424V2.setOtherEstimatedFunding(zero);
               sf424V2.setProgramIncomeEstimatedFunding(zero);
               sf424V2.setTotalEstimatedFunding(zero);
           }
           
           //box19 
           sf424V2.setStateReview(getStateReviewCode());
           if ( stateReviewDate != null){
                sf424V2.setStateReviewAvailableDate(getCal(stateReviewDate));
           }
           
           //box 20     
           //case 2406
       //    sf424V2.setDelinquentFederalDebt(getFederalDebtAnswer(propDevFormBean.getOrganizationId()));
            sf424V2.setDelinquentFederalDebt(getFederalDebtAnswer(organizationID));
           if (sf424V2.getDelinquentFederalDebt().startsWith("Y")) {
               //it is one page on the form after box21 
               sf424V2.setDelinquentFederalDebtExplanation(federalDebtExp);
           }
           
           //box21 CertificationAgree is hard coding here, so far we don't have it in the DB.
           sf424V2.setCertificationAgree("Y: Yes");
           sf424V2.setAuthorizedRepresentative(getName(aorBean));
           if(aorBean.getPrimaryTitle() !=null && aorBean.getPrimaryTitle().length()>45)
                sf424V2.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle().substring(0,45));
           else
                sf424V2.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle());
           
           sf424V2.setAuthorizedRepresentativePhoneNumber(aorBean.getOfficePhone());
           sf424V2.setAuthorizedRepresentativeEmail(aorBean.getEmailAddress());
           sf424V2.setAuthorizedRepresentativeFax(aorBean.getFaxNumber());
           sf424V2.setAORSignature(aorBean.getFullName());
           sf424V2.setDateSigned(getTodayDate());
           
           setAttachment(sf424V2);
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"SF424V2Stream","getSF424V2()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }    
        
        return sf424V2;
    }
    
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
    private HashMap getCFDA() throws DBException, CoeusException {
         if (propNumber ==null)
              throw new CoeusXMLException("Proposal Number is Null");
         HashMap hmCFDA = s2sTxnBean.getCFDA(propNumber);
         
         return hmCFDA;
    }
    
    /*start case 2406 
    private OrganizationMaintenanceFormBean getOrgData(String orgID)
       throws CoeusXMLException,CoeusException,DBException{
        if(orgID==null) 
            throw new CoeusXMLException("Organization id is Null");   
        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(orgID);
    }
    */
    
    private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = sf424V2TxnBean.getOrganizationID(propNumber,"O");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
          //start case 3110
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
        HashMap hmPerfOrg = sf424V2TxnBean.getOrganizationID(propNumber, "P");
        if (hmPerfOrg!= null && hmPerfOrg.get("ORGANIZATION_ID") != null){
               perfOrganizationID = hmPerfOrg.get("ORGANIZATION_ID").toString();
        }
          //start case 3110
         OrganizationMaintenanceFormBean  orgBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
        //get congressional district
          HashMap hmCongDist = sf424V2TxnBean.getCongDistrict(propNumber, orgBean.getOrganizationId(),2);
        String congDist = (String) hmCongDist.get("CONGDIST").toString();
         orgBean.setCongressionalDistrict(congDist);
         return orgBean;
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(perfOrganizationID);
    }
    
    /*end case 2406*/
    private BudgetInfoBean getBudgetInfo()
       throws JAXBException,  CoeusException,DBException { 
       int version = s2sTxnBean.getVersion(propNumber);
       
      return budgetDataTxnBean.getBudgetForProposal(propNumber,version);
     
   }
    
    private RolodexDetailsBean getRolodexData(int rolodexId)
       throws CoeusXMLException,CoeusException,DBException{
        if(rolodexId == 0 )
            throw new CoeusXMLException("Rolodex id is zero");   
        return rolodexMaintDataTxnBean.getRolodexMaintenanceDetails(Integer.toString(rolodexId ));  
    }
     
    private String getFederalID() throws DBException, CoeusException {
         String federalId = null;
         if (propNumber ==null)
             throw new CoeusXMLException("Proposal Number is Null");
         
         federalId = s2sTxnBean.getFederalId(propNumber);
           
         return federalId;
         
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
    
    private static BigDecimal convDoubleToBigDec(double dblCost){
    
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         return bdCost; 
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
    HashMap hmName;    
    if (rolodexDetailsBean != null) {
        //rolodex
        addressType.setStreet1(rolodexDetailsBean.getAddress1());
        if (rolodexDetailsBean.getAddress2() != null)
            addressType.setStreet2(rolodexDetailsBean.getAddress2());
        addressType.setCity(rolodexDetailsBean.getCity());
        if (rolodexDetailsBean.getState() != null){
            hmName = null;
            hmName = sf424V2TxnBean.getStateName(rolodexDetailsBean.getState());
            if (hmName != null )
              addressType.setState(hmName.get("STATE_NAME").toString());
        }
        if (rolodexDetailsBean.getPostalCode() != null)
            addressType.setZipPostalCode(UtilFactory.convertNull(rolodexDetailsBean.getPostalCode()));
        if (rolodexDetailsBean.getCountry() != null ){
            hmName = null;
            hmName = sf424V2TxnBean.getCountryName(rolodexDetailsBean.getCountry());
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
            hmName = sf424V2TxnBean.getStateName(personBean.getState());
            if (hmName != null )
              addressType.setState(hmName.get("STATE_NAME").toString());
         }

         if (personBean.getPostalCode() != null){
             addressType.setZipPostalCode(personBean.getPostalCode());
         }
         
         if (personBean.getCountryCode() != null ){
            hmName = null;
            hmName = sf424V2TxnBean.getCountryName(personBean.getCountryCode());
            if (hmName != null )
              addressType.setCountry(hmName.get("COUNTRY_NAME").toString());
         }              
    }
    return addressType;
   }
    
   
   private String getApplicantType(int index)
                  throws JAXBException,  CoeusException,DBException{   
   OrganizationListBean[] orgTypes = orgMaintDataTxnBean.getSelectedOrganizationList(orgMaintFormBean.getOrganizationId());
  
      String applicantType = "";

      if (orgTypes.length > index ){
       int orgTypeCode = orgTypes[index].getOrganizationTypeCode();
       if (orgTypeCode > 0 ){
           HashMap hmAppType = sf424V2TxnBean.getSF424V2ApplicantType(orgTypeCode);
           if (hmAppType != null ){
               applicantType =  hmAppType.get("APPLICANT_TYPE") == null? null : hmAppType.get("APPLICANT_TYPE").toString();
                switch (orgTypeCode){
                    case 3: {
                       applicantTypeOtherSpecify = "Federal Government" ;
                       break;
                    }case 14: {
                        applicantTypeOtherSpecify ="socially and Economically Disadvantaged";
                        break;
                    }case 15: {
                        applicantTypeOtherSpecify ="Women owned";
                        break;
                    }
                }
           }
       }
      }

      return applicantType;
}
   
   private String getStateReviewCode() throws DBException, CoeusException{
      HashMap hmStateReview = s2sTxnBean.getEOStateReview(propNumber);
      String answer = (String)hmStateReview.get("ANSWER");
      System.out.println("answer: "+answer);
      if (answer == null || answer.equals("X")) {
//          return "b. Program is subject to E.O. 12372 but has not been selected by the State for review.";
            return "c. Program is not covered by E.O. 12372.";
      }
      else if(answer.equals("N")){
//          return "c. Program is not covered by E.O. 12372.";
            return "b. Program is subject to E.O. 12372 but has not been selected by the State for review.";
      }   
      else if(answer.equals("Y")){
          this.stateReviewDate = hmStateReview.get("REVIEW_DATE") == null ? null : 
              new Date( ((Timestamp) hmStateReview.get("REVIEW_DATE")).getTime());
          return "a. This application was made available to the State under the Executive Order 12372 Process for review on";
      }

      else {
        return "b. Program is subject to E.O. 12372 but has not been selected by the State for review.";
      }
     
  }
   private ProposalPersonFormBean getPIPersonBean()
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
        ProposalPersonFormBean pIBean = 
            propDevTxnBean.getProposalPersonDetails(propNumber, personID);
        return pIBean;
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
                if (orgYNQ.getAnswer().equalsIgnoreCase("N")) return "N: No";
                else if (orgYNQ.getAnswer().equalsIgnoreCase("Y")) {
                    federalDebtExp = orgYNQ.getExplanation().toString();
                    return "Y: Yes";
                }
            }
        }
        return "N: No";
   }
   
private  HumanNameDataType getName(DepartmentPersonFormBean perBean)
	throws CoeusException, JAXBException {
	HumanNameDataType humanNameDataType = globallibraryObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(perBean.getFirstName());
        humanNameDataType.setLastName(perBean.getLastName());
        humanNameDataType.setMiddleName(perBean.getMiddleName());
        
        return humanNameDataType;
  }
   
 private  void setAttachment(SF424Type sf424) throws DBException, CoeusException ,JAXBException{
     
         gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup = null;
               
         try{
         String description;
         Attachment attachment = null;
         int narrativeType;
         int moduleNum;
         ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
         ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
         Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
         LinkedHashMap hmArg = new LinkedHashMap();
                     
         HashMap hmNarrative = new HashMap();
         int size=vctNarrative==null?0:vctNarrative.size();
         for (int row=0; row < size;row++) {
            proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
            moduleNum = proposalNarrativePDFSourceBean.getModuleNumber(); 
            hmNarrative = new HashMap();
            hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
            narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
            description = hmNarrative.get("DESCRIPTION").toString();
      
            hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
            hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
            attachment = getAttachment(hmArg);
            if (narrativeType == 41) {//AdditionalProjectTitle
                if (attachment == null) {
                    if (attachmentGroup == null)
                        attachmentGroup = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        attachmentGroup.getAttachedFile().add(attachedFileType);

                     }
                  }
            }
            if (narrativeType == 42) {//AdditionalCongressionalDistricts
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        sf424.setAdditionalCongressionalDistricts(attachedFileType);
                     }
                  }                  
               }
         }
         if (attachmentGroup != null) sf424.setAdditionalProjectTitle(attachmentGroup);
         
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"SF424V2Stream","setAttachment(()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }
    }

    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getSF424V2();
    }
    
    
    
}
