/*
 * SF424_ShortStream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationListBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.s2s.bean.*;
import gov.grants.apply.forms.sf424_short_v1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.*;
import java.sql.Timestamp;
import javax.xml.bind.JAXBException;


public class SF424_ShortStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.sf424_short_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    private String propNumber;
    private UtilFactory utilFactory; 
    
    private HashMap hmInfo = new HashMap();
    private String applicantTypeOtherSpecify = null;
    private SF424ShortTxnBean sf424ShortTxnBean;
    /** orgMaintFormBean holds organization master info   */
    private OrganizationMaintenanceFormBean orgMaintFormBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean; 
    /** Creates a new instance of SF424_ShortStream */
    public SF424_ShortStream() {
        objFactory = new gov.grants.apply.forms.sf424_short_v1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    private SF424ShortType  getSF424Short() throws CoeusXMLException,CoeusException,DBException{
        HashMap hmData = new HashMap();
        SF424ShortType sf424Short = null;
        try{
            sf424Short = objFactory.createSF424Short();
            sf424ShortTxnBean = new SF424ShortTxnBean();
            hmInfo = sf424ShortTxnBean.getEpsInfo(propNumber);
            
            /**
            * FormVersion
            */
           sf424Short.setFormVersion("1.0");  
           
           //set box1: NAME OF FEDERAL AGENCY
           if (hmInfo != null && hmInfo.get("SPONSOR_NAME") != null )
                if (hmInfo.get("SPONSOR_NAME").toString().length() > 60)
                    sf424Short.setAgencyName(hmInfo.get("SPONSOR_NAME").toString().substring(0,60));
                else    
                    sf424Short.setAgencyName(hmInfo.get("SPONSOR_NAME").toString()); 
           else if  (hmInfo != null && hmInfo.get("PRIME_SPONSOR_NAME") != null )
                if (hmInfo.get("PRIME_SPONSOR_NAME").toString().length() > 60)
                    sf424Short.setAgencyName(hmInfo.get("PRIME_SPONSOR_NAME").toString().substring(0,60));
                else    
                    sf424Short.setAgencyName(hmInfo.get("PRIME_SPONSOR_NAME").toString()); 
           //box2: cfda number, program title
           if (hmInfo.get("CFDA_NUMBER") != null )
                if (hmInfo.get("CFDA_NUMBER").toString().length() > 15 )
                   sf424Short.setCFDANumber(hmInfo.get("CFDA_NUMBER").toString().substring(0,15));
                else sf424Short.setCFDANumber(hmInfo.get("CFDA_NUMBER").toString()); 
           if (hmInfo.get("PROGRAM_ANNOUNCEMENT_TITLE") != null )
                if (hmInfo.get("PROGRAM_ANNOUNCEMENT_TITLE").toString().length() > 120 )
                   sf424Short.setCFDAProgramTitle(hmInfo.get("PROGRAM_ANNOUNCEMENT_TITLE").toString().substring(0,120));
                else sf424Short.setCFDAProgramTitle(hmInfo.get("PROGRAM_ANNOUNCEMENT_TITLE").toString()); 
           //box3: today date
           sf424Short.setDateReceived(getTodayDate());
           
           //box4: OPPORTUNITY_ID
           hmData = null;
           hmData = sf424ShortTxnBean.getS2sOpportunity(propNumber);
           if (hmData != null && hmData.get("OPPORTUNITY_ID") != null){
               if (hmData.get("OPPORTUNITY_ID").toString().length() > 40 )
                    sf424Short.setFundingOpportunityNumber(hmData.get("OPPORTUNITY_ID").toString().substring(0,40));
               else sf424Short.setFundingOpportunityNumber(hmData.get("OPPORTUNITY_ID").toString());
               
               if ( hmData.get("OPPORTUNITY_TITLE") != null){                
                    sf424Short.setFundingOpportunityTitle(hmData.get("OPPORTUNITY_TITLE").toString());
               }
           }
           //box5: application info
           /** orgMaintFormBean holds organization master info   */
           orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
           orgMaintFormBean = orgMaintDataTxnBean.getOrganizationMaintenanceDetails(hmInfo.get("ORGANIZATION_ID").toString());
           sf424Short.setOrganizationName(orgMaintFormBean.getOrganizationName());
           
           sf424Short.setAddress(getAddress(Integer.toString(orgMaintFormBean.getContactAddressId())));
           
           //set Type of Applicant
           String appType = null;
           appType = getApplicantType(0);
           if (appType != null && !appType.equals(""))
                sf424Short.setApplicantTypeCode1(appType);
           
           appType = getApplicantType(1);
           if (appType != null && !appType.equals(""))
           sf424Short.setApplicantTypeCode2(appType);
           
           appType = getApplicantType(2);
           if (appType != null && !appType.equals(""))
           sf424Short.setApplicantTypeCode3(appType);
           
           if (applicantTypeOtherSpecify != null){
                sf424Short.setApplicantTypeOtherSpecify(applicantTypeOtherSpecify);
                applicantTypeOtherSpecify = null;
           }
           sf424Short.setEmployerTaxpayerIdentificationNumber(orgMaintFormBean.getFederalEmployerID());
           
           sf424Short.setDUNSNumber(orgMaintFormBean.getDunsNumber());
           //set congressional district of applicant
           String congressionalDistrict = UtilFactory.setNullToUnknown(orgMaintFormBean.getCongressionalDistrict());
           if (congressionalDistrict != null && congressionalDistrict.length() > 6 )
               sf424Short.setCongressionalDistrictApplicant(congressionalDistrict.substring(0, 6));
           else 
               sf424Short.setCongressionalDistrictApplicant(congressionalDistrict);
           
           //box6: Project Information
           if ( hmInfo.get("TITLE") != null)               
                sf424Short.setProjectTitle(hmInfo.get("TITLE").toString());    
           
           //set project description,get data from osp$eps_prop_abstract
           hmData = null;
           hmData = sf424ShortTxnBean.getProjectDescription(propNumber);
           if (hmData != null && hmData.get("PROJECT_DESCRIPTION") != null){
               String projectDesc = hmData.get("PROJECT_DESCRIPTION").toString();
               if (projectDesc.length() > 1000 )
                   sf424Short.setProjectDescription(projectDesc.substring(0,1000));
              else 
                  sf424Short.setProjectDescription(projectDesc);
           }
           
           //set start date and end date
           if ( hmInfo.get("REQUESTED_START_DATE_INITIAL") != null)               
                sf424Short.setProjectStartDate(getCal(new Date( ((Timestamp) hmInfo.get("REQUESTED_START_DATE_INITIAL")).getTime())));
           
           if ( hmInfo.get("REQUESTED_END_DATE_INITIAL") != null)               
                sf424Short.setProjectEndDate(getCal(new Date( ((Timestamp) hmInfo.get("REQUESTED_END_DATE_INITIAL")).getTime())));
           
           //box7: PI

           hmData = null; 
           hmData = sf424ShortTxnBean.getProjectDirectorInfo(propNumber);
           sf424Short.setProjectDirectorGroup(getPersonData(hmData));
           
           //box8: osp admin for the lead unit
           hmData = null; 
           hmData = sf424ShortTxnBean.getContactPersonInfo(propNumber);
           sf424Short.setContactPersonGroup(getPersonData(hmData));
           
           //box9:
           //CertificationAgree is hard coding here, so far we don't have it in the DB.
           sf424Short.setApplicationCertification("Y: Yes");
           //get AOR
           DepartmentPersonFormBean aorBean;
           ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
           aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber); 
           sf424Short.setAuthorizedRepresentative(getName(aorBean));
           
           if(aorBean.getPrimaryTitle() !=null )
               if (aorBean.getPrimaryTitle().length()>45)
                 sf424Short.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle().substring(0,45));
               else
                 sf424Short.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle());
           
           if (aorBean.getEmailAddress() !=null )
               if (aorBean.getEmailAddress().length()>60)
                    sf424Short.setAuthorizedRepresentativeEmail(aorBean.getEmailAddress().substring(0,60));
               else
                   sf424Short.setAuthorizedRepresentativeEmail(aorBean.getEmailAddress());
           
           if (aorBean.getOfficePhone() !=null )
               if (aorBean.getOfficePhone().length()>25)
                    sf424Short.setAuthorizedRepresentativePhoneNumber(aorBean.getOfficePhone().substring(0,25));
               else
                   sf424Short.setAuthorizedRepresentativePhoneNumber(aorBean.getOfficePhone());
           
           if (aorBean.getFaxNumber() !=null )
               if (aorBean.getFaxNumber().length()>25)
                    sf424Short.setAuthorizedRepresentativeFaxNumber(aorBean.getFaxNumber().substring(0,25));
               else
                   sf424Short.setAuthorizedRepresentativeFaxNumber(aorBean.getFaxNumber());
           
           sf424Short.setAuthorizedRepresentativeSignature(aorBean.getFullName());
           sf424Short.setAuthorizedRepresentativeDateSigned(getTodayDate());
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"SF424_ShortStream","getSF424Short()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }    
        
        return sf424Short;
    
    }
   
    private AddressDataType getAddress(String ID)
                  throws JAXBException,  CoeusException,DBException{
    RolodexDetailsBean rolodexDetailsBean;    
    RolodexMaintenanceDataTxnBean rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
    rolodexDetailsBean = rolodexMaintDataTxnBean.getRolodexMaintenanceDetails(ID);                  
    AddressDataType addressType = globallibraryObjFactory.createAddressDataType();
    HashMap hmName;    
    if (rolodexDetailsBean != null) {
        //rolodex
        if (rolodexDetailsBean.getAddress1() != null)
            if (rolodexDetailsBean.getAddress1().length() > 55)
                addressType.setStreet1(rolodexDetailsBean.getAddress1().substring(0, 55));
            else
                addressType.setStreet1(rolodexDetailsBean.getAddress1());
        if (rolodexDetailsBean.getAddress2() != null)
            if (rolodexDetailsBean.getAddress2().length() > 55)
                addressType.setStreet2(rolodexDetailsBean.getAddress2().substring(0, 55));
            else
                addressType.setStreet2(rolodexDetailsBean.getAddress2());
        
        if (rolodexDetailsBean.getCity() != null)
            if (rolodexDetailsBean.getCity().length() > 35)
                addressType.setCity(rolodexDetailsBean.getCity().substring(0, 35));
            else
                addressType.setCity(rolodexDetailsBean.getCity());
        
        if (rolodexDetailsBean.getState() != null){
            hmName = null;
            hmName = sf424ShortTxnBean.getStateName(rolodexDetailsBean.getState());
            if (hmName != null )
              addressType.setState(hmName.get("STATE_NAME").toString());
        }
        if (rolodexDetailsBean.getPostalCode() != null)
            addressType.setZipPostalCode(UtilFactory.convertNull(rolodexDetailsBean.getPostalCode()));
        if (rolodexDetailsBean.getCountry() != null ){
            hmName = null;
            hmName = sf424ShortTxnBean.getCountryName(rolodexDetailsBean.getCountry());
            if (hmName != null )
              addressType.setCountry(hmName.get("COUNTRY_NAME").toString());
        }
    }
    return addressType;
   }
   
   private ContactPersonDataType getPersonData(HashMap hmPerson) throws JAXBException,DBException,CoeusException{
       
        ContactPersonDataType contactPersonData = globallibraryObjFactory.createContactPersonDataType();
       
        HashMap hmName = new HashMap();
//        HashMap hmPerson = new HashMap();
//        hmPerson = sf424ShortTxnBean.getContactPersonInfo(propNumber);
        if (hmPerson != null){
            HumanNameDataType contactName 
                        = globallibraryObjFactory.createHumanNameDataType();
            if (hmPerson.get("FIRST_NAME") != null){
                contactName.setFirstName(hmPerson.get("FIRST_NAME").toString());
            }
            if (hmPerson.get("MIDDLE_NAME") != null){
                contactName.setMiddleName(hmPerson.get("MIDDLE_NAME").toString());
            }
            if (hmPerson.get("LAST_NAME") != null){
                contactName.setLastName(hmPerson.get("LAST_NAME").toString());
            }
            contactPersonData.setName(contactName);
            
            if (hmPerson.get("OFFICE_PHONE") != null){
                contactPersonData.setPhone(hmPerson.get("OFFICE_PHONE").toString());
            }
            if (hmPerson.get("FAX_NUMBER") != null){
                contactPersonData.setFax(hmPerson.get("FAX_NUMBER").toString());
            }
            if (hmPerson.get("EMAIL_ADDRESS") != null){
                contactPersonData.setEmail(hmPerson.get("EMAIL_ADDRESS").toString());
            }
            if (hmPerson.get("PRIMARY_TITLE") != null){
                contactPersonData.setTitle(hmPerson.get("PRIMARY_TITLE").toString());
            }
            
            AddressDataType contactAddress
                        = globallibraryObjFactory.createAddressDataType();
            if (hmPerson.get("ADDRESS_LINE_1") != null){
                contactAddress.setStreet1(hmPerson.get("ADDRESS_LINE_1").toString());
            }
            if (hmPerson.get("ADDRESS_LINE_2") != null){
                contactAddress.setStreet2(hmPerson.get("ADDRESS_LINE_2").toString());
            }
            if (hmPerson.get("CITY") != null){
                contactAddress.setCity(hmPerson.get("CITY").toString());
            }
            if (hmPerson.get("COUNTY") != null){
                contactAddress.setCounty(hmPerson.get("COUNTY").toString());
            }
            if (hmPerson.get("STATE") != null){
                hmName = null;
                hmName = sf424ShortTxnBean.getStateName(hmPerson.get("STATE").toString());

                if (hmName != null && hmName.get("STATE_NAME") != null)              
                    contactAddress.setState(hmName.get("STATE_NAME").toString());
            }
            if (hmPerson.get("POSTAL_CODE") != null){
                contactAddress.setZipPostalCode(hmPerson.get("POSTAL_CODE").toString());
            }
            if (hmPerson.get("COUNTRY_CODE") != null){
                hmName = null;
                hmName = sf424ShortTxnBean.getCountryName(hmPerson.get("COUNTRY_CODE").toString());
                if (hmName != null && hmName.get("COUNTRY_NAME") != null)
                    contactAddress.setCountry(hmName.get("COUNTRY_NAME").toString());
            }
            contactPersonData.setAddress(contactAddress);
        }
        return contactPersonData;
   }
   
    private String getApplicantType(int index)
                  throws JAXBException,  CoeusException,DBException{   
   OrganizationListBean[] orgTypes = orgMaintDataTxnBean.getSelectedOrganizationList(orgMaintFormBean.getOrganizationId());
  
      String applicantType = "";

      if (orgTypes.length > index ){
       int orgTypeCode = orgTypes[index].getOrganizationTypeCode();
       if (orgTypeCode > 0 ){
           HashMap hmAppType = sf424ShortTxnBean.getApplicantType(orgTypeCode);
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
    
private  HumanNameDataType getName(DepartmentPersonFormBean perBean)
	throws CoeusException, JAXBException {
	HumanNameDataType humanNameDataType = globallibraryObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(perBean.getFirstName());
        humanNameDataType.setLastName(perBean.getLastName());
        humanNameDataType.setMiddleName(perBean.getMiddleName());
        
        return humanNameDataType;
  }
    
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
    
    private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
     public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getSF424Short();
    }
}
