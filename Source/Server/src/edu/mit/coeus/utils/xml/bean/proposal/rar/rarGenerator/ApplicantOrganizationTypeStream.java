/*
 * ApplicantOrganizationTypeStream.java
 *
 * Created on March 2, 2004, 3:56 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;


/**
 *
 * @author  ele
 */

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.organization.bean.OrganizationListBean;
import edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType;
import edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType;
import edu.mit.coeus.utils.xml.bean.proposal.rar.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.PostalAddressTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.ContactInfoTypeStream;


public class ApplicantOrganizationTypeStream {
    
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
   
    ApplicantOrganizationType applicantOrganizationType;
   
    
    
    /** Creates a new instance of ApplicantOrganizationTypeStream */
    public ApplicantOrganizationTypeStream(ObjectFactory objFactory,
            edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory ) {
         this.objFactory = objFactory ;
         this.commonObjFactory = commonObjFactory;
    }
    
    
    public ApplicantOrganizationType getApplicantOrgTypeInfo(
        OrganizationMaintenanceFormBean orgBean, RolodexDetailsBean rolodexBean,
        DepartmentPersonFormBean orgContactPersonBean)
        
    throws CoeusException, DBException, javax.xml.bind.JAXBException
   
    {     
       OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
       OrganizationListBean[] orgTypes = orgMaintDataTxnBean.getSelectedOrganizationList(orgBean.getOrganizationId());
      
      
       applicantOrganizationType = objFactory.createApplicantOrganizationType();
       
       if (orgBean.getOrganizationName() == null) {
           applicantOrganizationType.setOrganizationName("Unknown");
       }else  applicantOrganizationType.setOrganizationName(orgBean.getOrganizationName() );
     
       if (orgBean.getDunsNumber() == null) {
           applicantOrganizationType.setOrganizationDUNS("Unknown");
       }else  applicantOrganizationType.setOrganizationDUNS(orgBean.getDunsNumber());
       
       if (orgBean.getFederalEmployerID() == null) {
           applicantOrganizationType.setOrganizationEIN("Unknown");
       }else  applicantOrganizationType.setOrganizationEIN(orgBean.getFederalEmployerID());
          
       //use first org type, since schema allows for only one and coeus allows multiple
        if (orgTypes == null){
           applicantOrganizationType.setOrganizationCategoryCode("Unknown");
           applicantOrganizationType.setOrganizationCategoryDescription("Unknown");
       }
       applicantOrganizationType.setOrganizationCategoryCode(Integer.toString(orgTypes[0].getOrganizationTypeCode()));
       applicantOrganizationType.setOrganizationCategoryDescription(orgTypes[0].getDescription());
       
       if (orgBean.getCongressionalDistrict() == null) {
            applicantOrganizationType.setOrganizationCongressionalDistrict("Unknown");
       } else applicantOrganizationType.setOrganizationCongressionalDistrict(orgBean.getCongressionalDistrict());
    

       PostalAddressTypeStream postalAddressTypeStream = new PostalAddressTypeStream(commonObjFactory);
       applicantOrganizationType.setOrganizationAddress(postalAddressTypeStream.getPostalAddressInfo(rolodexBean));
     
       ApplicantOrganizationType.OrganizationContactPersonType orgContact;
       orgContact = objFactory.createApplicantOrganizationTypeOrganizationContactPersonType();
          
       PersonFullNameTypeStream personFullNameTypeStream 
                    = new PersonFullNameTypeStream(objFactory);
       //handle case of no investigators
        if (orgContactPersonBean == null) {
           PersonFullNameType personFullNameType = objFactory.createPersonFullNameType();
           personFullNameType.setLastName("Unknown");
           personFullNameType.setFirstName("Unknown");
           personFullNameType.setMiddleName("Unknown");
           orgContact.setName(personFullNameType);
           orgContact.setPositionTitle("Unknown");
           
           ContactInfoType contactInfoType = commonObjFactory.createContactInfoType();
           contactInfoType.setEmail("Unknown");
           contactInfoType.setFaxNumber("Unknown");
           contactInfoType.setPhoneNumber("Unknown");
           
           PostalAddressType postalAddressType = commonObjFactory.createPostalAddressType();
           postalAddressType.getStreet().add("Unknown");
           postalAddressType.setCity("Unknown");
           postalAddressType.setState("Unknown");
           postalAddressType.setPostalCode("Unknown");
           postalAddressType.setCountry("Unknown");
           
           contactInfoType.setPostalAddress(postalAddressType);
            
     
       } else {
            orgContact.setName(personFullNameTypeStream.getPersonFullNameTypeInfo(orgContactPersonBean)); 
            orgContact.setPositionTitle(orgContactPersonBean.getPrimaryTitle());
        
            ContactInfoTypeStream contactInfoTypeStream 
                    = new ContactInfoTypeStream(commonObjFactory);
     
            orgContact.setContactInformation(contactInfoTypeStream.getContactInfo(orgContactPersonBean,rolodexBean)); 
       }
       
       applicantOrganizationType.setOrganizationContactPerson(orgContact);
       applicantOrganizationType.setCageNumber(orgBean.getCageNumber());
    return applicantOrganizationType;
    }
    
}
