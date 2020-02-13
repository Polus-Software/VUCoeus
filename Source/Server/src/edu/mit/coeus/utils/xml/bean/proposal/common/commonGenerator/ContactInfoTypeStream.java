/*
 * ContactInfoTypeStream.java
 *
 * Created on MAr 3, 2004
 */
 
package edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator;

import java.util.* ;
import java.math.BigInteger;

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException; 
import edu.mit.coeus.utils.xml.bean.proposal.common.*;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;

  
public class ContactInfoTypeStream
{
    ObjectFactory objFactory ;
    ContactInfoType contactInfoType;   
    RolodexDetailsBean orgRolodexBean;
    
    /** Creates a new instance of ContactInfoTypeStream */
    public ContactInfoTypeStream(ObjectFactory objFactory)
    {
        
        this.objFactory = objFactory ;
       
    }
    
  
      public ContactInfoType getContactInfo( ProposalPersonFormBean propPersonBean,
                     RolodexDetailsBean orgRolodexBean  ) 
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        
        contactInfoType = objFactory.createContactInfoType();
        PostalAddressTypeStream postalAddressStream = new PostalAddressTypeStream(objFactory);
    
        //handle case if no investigator
        if (propPersonBean.getPersonId().equals("XXXXXXXXX")) {
            contactInfoType.setEmail("Unknown");
            contactInfoType.setFaxNumber("Unknown");
            contactInfoType.setPhoneNumber("Unknown");
            contactInfoType.setPostalAddress
                ( postalAddressStream.getPostalAddressInfo(propPersonBean,orgRolodexBean));
        } else {
          
            contactInfoType.setEmail(propPersonBean.getEmailAddress());
            //changes made to include new address fields from proposal_person
            if (propPersonBean.getFaxNumber() != null)
               contactInfoType.setFaxNumber(propPersonBean.getFaxNumber());           
            contactInfoType.setPhoneNumber(propPersonBean.getOfficePhone());
            contactInfoType.setPostalAddress
                  ( postalAddressStream.getPostalAddressInfo(propPersonBean, orgRolodexBean));
  
        }
        
        return contactInfoType ;
    }
      
      //overloaded method for rolodex
        public ContactInfoType getContactInfo( RolodexDetailsBean rolodexBean) 
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        
        contactInfoType = objFactory.createContactInfoType();
                                                                             
        PostalAddressTypeStream postalAddressStream = new PostalAddressTypeStream(objFactory);
       
        contactInfoType.setEmail(rolodexBean.getEMail());
        contactInfoType.setFaxNumber( rolodexBean.getFax());
        contactInfoType.setPhoneNumber(rolodexBean.getPhone());
        contactInfoType.setPostalAddress(postalAddressStream.getPostalAddressInfo(rolodexBean));
  
        return contactInfoType ;
    }
        
       //overloaded method for DepartmentPersonFormBean
        public ContactInfoType getContactInfo( DepartmentPersonFormBean orgContactBean,
                       RolodexDetailsBean orgRolodexBean ) 
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        
        contactInfoType = objFactory.createContactInfoType();
                                                                             
        PostalAddressTypeStream postalAddressStream = new PostalAddressTypeStream(objFactory);
       
        contactInfoType.setEmail(orgContactBean.getEmailAddress());
        //changes made to include new address fields from proposal_person
        if (orgContactBean.getFaxNumber() != null)
            contactInfoType.setFaxNumber(orgContactBean.getFaxNumber());
        contactInfoType.setPhoneNumber(orgContactBean.getOfficePhone());
        contactInfoType.setPostalAddress(postalAddressStream.getPostalAddressInfo
                  (orgContactBean, orgRolodexBean));
   
        return contactInfoType ;
    }
       
  
    
}
