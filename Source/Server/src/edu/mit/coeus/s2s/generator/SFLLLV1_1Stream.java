/*
 * SFLLLV1_1Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import javax.xml.bind.JAXBException;
import java.util.*;
import edu.mit.coeus.s2s.bean.SFLLLTxnBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import gov.grants.apply.forms.sflll_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;

public class SFLLLV1_1Stream extends S2SBaseStream{ 
     private gov.grants.apply.forms.sflll_v1_1.ObjectFactory objFactory;
     private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
     private CoeusXMLGenrator xmlGenerator;
     private String propNumber;
     private  HashMap hmInfo = new HashMap();
     private SFLLLTxnBean sflllTxnBean;
     
    
    /** Creates a new instance of SFLLLV1_1Stream */
    public SFLLLV1_1Stream() {
         objFactory = new gov.grants.apply.forms.sflll_v1_1.ObjectFactory();
         globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
         xmlGenerator = new CoeusXMLGenrator();
         sflllTxnBean = new SFLLLTxnBean();
    }
    
    private LobbyingActivitiesDisclosureType  getSFLLL()  throws CoeusXMLException,CoeusException,DBException,JAXBException{
        LobbyingActivitiesDisclosureType lobbyingAD = objFactory.createLobbyingActivitiesDisclosure();
        lobbyingAD.setFormVersion("1.1");
        
       
        //set FederalActionType,FederalActionStatus and ReportType. 
        //those are all hard coding in the procedure s2sSFLLLPkg.get_types()
        hmInfo = sflllTxnBean.getTypes(propNumber);
        if (hmInfo  !=  null){
              if (hmInfo.get("FED_ACTION_TYPE") != null )
                     lobbyingAD.setFederalActionType(hmInfo.get("FED_ACTION_TYPE").toString());
              if (hmInfo.get("FED_ACTION_STATUS") != null )
                     lobbyingAD.setFederalActionStatus(hmInfo.get("FED_ACTION_STATUS").toString());
              if (hmInfo.get("REPORT_TYPE") != null )
                     lobbyingAD.setReportType(hmInfo.get("REPORT_TYPE").toString());
              
              
        }
        
        //box 4.
        lobbyingAD.setReportEntity(getReportEntity());
        
        //box6. set  fed department/agency
        if (hmInfo != null && hmInfo.get("SPONSOR_NAME") != null )
            if (hmInfo.get("SPONSOR_NAME").toString().length() > 40)
                    lobbyingAD.setFederalAgencyDepartment(hmInfo.get("SPONSOR_NAME").toString().substring(0,40));
            else    
                    lobbyingAD.setFederalAgencyDepartment(hmInfo.get("SPONSOR_NAME").toString()); 
        else if  (hmInfo != null && hmInfo.get("PRIME_SPONSOR_NAME") != null )
            if (hmInfo.get("PRIME_SPONSOR_NAME").toString().length() > 40)
                    lobbyingAD.setFederalAgencyDepartment(hmInfo.get("PRIME_SPONSOR_NAME").toString().substring(0,40));
            else    
                    lobbyingAD.setFederalAgencyDepartment(hmInfo.get("PRIME_SPONSOR_NAME").toString()); 
        
        //box7 fed program name, CFDA number
        lobbyingAD.setFederalProgramName(getFedProgramName());
        
        //box10a
        lobbyingAD.setLobbyingRegistrant(getLobbyingRegistrant());
        //box10b
        lobbyingAD.setIndividualsPerformingServices(getIndividualsPerformingServices());
        
        //box Signature
        lobbyingAD.setSignatureBlock(getSignatureBlock());         
        
        return lobbyingAD;
    }
    
    private LobbyingActivitiesDisclosureType.ReportEntityType getReportEntity() throws CoeusXMLException,CoeusException,DBException,JAXBException{
           LobbyingActivitiesDisclosureType.ReportEntityType reportEntity = objFactory.createLobbyingActivitiesDisclosureTypeReportEntityType();
           hmInfo = sflllTxnBean.getInfos(propNumber);
           if (hmInfo  !=  null){
                 if (hmInfo.get("REPORT_ENTITY_TYPE") != null ){
                     reportEntity.setReportEntityType(hmInfo.get("REPORT_ENTITY_TYPE").toString());
                     if (reportEntity.getReportEntityType().equals("Prime")) 
                         reportEntity.setReportEntityIsPrime("Y: Yes");
                     else 
                         reportEntity.setReportEntityIsPrime("N: No");
                 }
                 
                 LobbyingActivitiesDisclosureType.ReportEntityType.PrimeType 
                              prime = objFactory.createLobbyingActivitiesDisclosureTypeReportEntityTypePrimeType();
                 RolodexDetailsBean orgContactRolodexDetailsBean ;
                 RolodexMaintenanceDataTxnBean rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
                  if ( hmInfo.get("ORGANIZATION_NAME") != null ){              
                       if (hmInfo.get("ORGANIZATION_NAME").toString().length() > 60)
                           prime.setOrganizationName(hmInfo.get("ORGANIZATION_NAME").toString().substring(0,60));
                       else    
                           prime.setOrganizationName(hmInfo.get("ORGANIZATION_NAME").toString());
                 }
                 
                  if ( hmInfo.get("CONGRESSIONAL_DISTRICT") != null ){              
                       if (hmInfo.get("CONGRESSIONAL_DISTRICT").toString().length() > 6)
                           prime.setCongressionalDistrict(hmInfo.get("CONGRESSIONAL_DISTRICT").toString().substring(0,6));
                       else    
                           prime.setCongressionalDistrict(hmInfo.get("CONGRESSIONAL_DISTRICT").toString());
                 }
                 
                 prime.setReportEntityType("Prime");
                 
                  if ( hmInfo.get("CONTACT_ADDRESS_ID") != null ){   
                        orgContactRolodexDetailsBean = rolodexMaintDataTxnBean.getRolodexMaintenanceDetails(hmInfo.get("CONTACT_ADDRESS_ID").toString());
                        if (orgContactRolodexDetailsBean != null){
                            AwardeeDataType.AddressType addressType = objFactory.createAwardeeDataTypeAddressType();
//                            AddressDataType addressType = globallibraryObjFactory.createAddressDataType();
                            if (orgContactRolodexDetailsBean.getAddress1() != null )
                                if (orgContactRolodexDetailsBean.getAddress1().length() > 55)
                                    addressType.setStreet1(orgContactRolodexDetailsBean.getAddress1().substring(0, 55));
                                else 
                                     addressType.setStreet1(orgContactRolodexDetailsBean.getAddress1());
                            
                            if (orgContactRolodexDetailsBean.getAddress2() != null )
                                if (orgContactRolodexDetailsBean.getAddress2().length() > 55)
                                    addressType.setStreet2(orgContactRolodexDetailsBean.getAddress2().substring(0, 55));
                                else 
                                     addressType.setStreet2(orgContactRolodexDetailsBean.getAddress2());                            
                            
                            if (orgContactRolodexDetailsBean.getState() != null ){
                                HashMap hmName = new HashMap();
                                hmName = sflllTxnBean.getStateName(orgContactRolodexDetailsBean.getState());
                                if (hmName  !=  null) 
                                    addressType.setState(hmName.get("STATE_NAME").toString());
                            }
                            
                            if (orgContactRolodexDetailsBean.getPostalCode() != null)
                                    addressType.setZipPostalCode(UtilFactory.convertNull(orgContactRolodexDetailsBean.getPostalCode()));
                            
                            if (orgContactRolodexDetailsBean.getCity() != null)
                               if (orgContactRolodexDetailsBean.getCity().length() > 35)
                                    addressType.setCity(orgContactRolodexDetailsBean.getCity().substring(0, 35));
                               else 
                                     addressType.setCity(orgContactRolodexDetailsBean.getCity());   
                            
                            if (orgContactRolodexDetailsBean.getCity() != null)
                               if (orgContactRolodexDetailsBean.getCity().length() > 35)
                                    addressType.setCity(orgContactRolodexDetailsBean.getCity().substring(0, 35));
                               else 
                                     addressType.setCity(orgContactRolodexDetailsBean.getCity());   
                            
                            if (addressType != null ) prime.setAddress(addressType);
                        }
                  }
                reportEntity.setPrime(prime); 
           }
           return reportEntity;
    }
    
    private LobbyingActivitiesDisclosureType.FederalProgramNameType   getFedProgramName()
                                            throws CoeusXMLException,CoeusException,DBException,JAXBException{
           LobbyingActivitiesDisclosureType.FederalProgramNameType  fedProgramName 
                                            = objFactory.createLobbyingActivitiesDisclosureTypeFederalProgramNameType();
           if (hmInfo  !=  null){
               if (hmInfo.get("PROGRAM_ANNOUNCEMENT_TITLE") != null )
                   fedProgramName.setFederalProgramDescription(hmInfo.get("PROGRAM_ANNOUNCEMENT_TITLE").toString());
               
               if (hmInfo.get("CFDA_NUMBER") != null )
                   fedProgramName.setCFDANumber(hmInfo.get("CFDA_NUMBER").toString());               
           }
           return fedProgramName;
    }
    
    private LobbyingActivitiesDisclosureType.LobbyingRegistrantType   getLobbyingRegistrant()
                                            throws CoeusXMLException,CoeusException,DBException,JAXBException{
           LobbyingActivitiesDisclosureType.LobbyingRegistrantType  fedLobRegistrant 
                                            = objFactory.createLobbyingActivitiesDisclosureTypeLobbyingRegistrantType();
           
           //we don't have info for Lobbying Registrant. This is a required field, so we set it to N/A
           
           fedLobRegistrant.setOrganizationName("N/A");
          
           return fedLobRegistrant;
    }
    
    private LobbyingActivitiesDisclosureType.IndividualsPerformingServicesType   getIndividualsPerformingServices()
                                            throws CoeusXMLException,CoeusException,DBException,JAXBException{
           LobbyingActivitiesDisclosureType.IndividualsPerformingServicesType  individualsPerformingService 
                                            = objFactory.createLobbyingActivitiesDisclosureTypeIndividualsPerformingServicesType();
           LobbyingActivitiesDisclosureType.IndividualsPerformingServicesType.IndividualType  individualType 
                                            = objFactory.createLobbyingActivitiesDisclosureTypeIndividualsPerformingServicesTypeIndividualType();
           
           
           //we don't have info for Individuals Performing Services. This is a required field, so we set it to N/A
           HumanNameDataType humanNameData = globallibraryObjFactory.createHumanNameDataType();
           humanNameData.setFirstName("N/A");
           humanNameData.setLastName("N/A");

           individualType.setName(humanNameData);
           individualsPerformingService.getIndividual().add(individualType);
                     
           return individualsPerformingService;
    }
    
    private LobbyingActivitiesDisclosureType.SignatureBlockType getSignatureBlock()
                            throws CoeusXMLException,CoeusException,DBException,JAXBException{
           
           LobbyingActivitiesDisclosureType.SignatureBlockType     signatureBlock 
                                            = objFactory.createLobbyingActivitiesDisclosureTypeSignatureBlockType(); 
        
           ProposalPrintingTxnBean propPrintingTxnBean  = new ProposalPrintingTxnBean();
           //aorBean holds information for AOR 
           DepartmentPersonFormBean aorBean;
           //get AOR
           aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber);             //set NAME AND TITLE OF AUTHORIZED REPRESENTATIVE
           signatureBlock.setName(getName(aorBean));
           if(aorBean.getPrimaryTitle() !=null && aorBean.getPrimaryTitle().length()>45)
                signatureBlock.setTitle(aorBean.getPrimaryTitle().substring(0,45));
           else
                signatureBlock.setTitle(aorBean.getPrimaryTitle());
           
           signatureBlock.setSignature(aorBean.getFullName());
           signatureBlock.setSignedDate(getTodayDate());
          
           return signatureBlock;
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
    
     public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getSFLLL();
    }
}
