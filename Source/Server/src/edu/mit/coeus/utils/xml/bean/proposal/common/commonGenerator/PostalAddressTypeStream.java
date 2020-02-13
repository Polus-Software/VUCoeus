/*
 * PostalAddressTypeStream.java
 *
 * Created on Mar 3, 2004
 */
 
package edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator;

import java.util.* ;


import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.xml.bean.proposal.common.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.utils.DateUtils;

  
public class PostalAddressTypeStream
{
    ObjectFactory objFactory;
    PostalAddressType postalAddressType;
     
    /** Creates a new instance of PostalAddressTypeStream */
    public PostalAddressTypeStream(ObjectFactory objFactory)
    {
        
        this.objFactory = objFactory ;
              
    }
    
    
  
    
    // overloaded method for rolodexBean
    public PostalAddressType getPostalAddressInfo( RolodexDetailsBean rolodexBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
              
        postalAddressType = objFactory.createPostalAddressType();
     
       if (rolodexBean.getAddress1() != null){
      //    postalAddressType.getStreet().add(rolodexBean.getAddress1());
        //start change for case 3997
          String street = null;
          String street2 = null;
          String street3 = null;
            
          if (rolodexBean.getAddress1() != null){
                street = rolodexBean.getAddress1();
                postalAddressType.getStreet().clear();
                postalAddressType.getStreet().add(street);
                if (rolodexBean.getAddress2() != null) {
                    street2 =  rolodexBean.getAddress2();
                    postalAddressType.getStreet().add(street2);
                    if (rolodexBean.getAddress3() != null) {
                        street3 =  rolodexBean.getAddress3();
                        postalAddressType.getStreet().add(street3);
                    }
                }
            }
       }
            
        
       postalAddressType.setCity((rolodexBean.getCity() == null||
                                  rolodexBean.getCity().trim().equals("")) ? "Unknown" :
                                  rolodexBean.getCity());
       if(rolodexBean.getState() != null) 
          postalAddressType.setState(rolodexBean.getState());
       postalAddressType.setPostalCode((rolodexBean.getPostalCode() == null||
                                          rolodexBean.getPostalCode().trim().equals("")) ? "Unknown" :
                                          rolodexBean.getPostalCode());
       postalAddressType.setCountry(rolodexBean.getCountry() == null ||
                                  rolodexBean.getCountry().trim().equals("")? "Unknown" :
                                  rolodexBean.getCountry());
            
       return postalAddressType ;
    }
    
      // overloaded method for propPerson bean
    public PostalAddressType getPostalAddressInfo(ProposalPersonFormBean propPersonBean,
                                                RolodexDetailsBean rolodexBean        ) 
                throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
              
        
        //changes made on 3/21/06 to add new street address info from person table
   
        postalAddressType = objFactory.createPostalAddressType();
        //handle case of no investigator
        if (propPersonBean.getPersonId().equals("XXXXXXXXX")){

            postalAddressType.getStreet().add("Unknown");
            postalAddressType.setCity("Unknown");
            postalAddressType.setState("Unknown");
            postalAddressType.setPostalCode("Unknown");
            postalAddressType.setCountry("Unknown");
        } else {

            postalAddressType.setCity((propPersonBean.getCity() == null||
                                      propPersonBean.getCity().trim().equals("")) ? "Unknown" :
                                      propPersonBean.getCity());       
            postalAddressType.setPostalCode((propPersonBean.getPostalCode() == null||
                                            propPersonBean.getPostalCode().trim().equals("")) ? "Unknown" :
                                            propPersonBean.getPostalCode());
            postalAddressType.setCountry((propPersonBean.getCountryCode() == null||
                                      propPersonBean.getCountryCode().trim().equals("")) ? "Unknown" :
                                      propPersonBean.getCountryCode());
   
            if (propPersonBean.getState() != null)
                postalAddressType.setState(propPersonBean.getState());
            String street = null;
            String street2 = null;
            String street3 = null;
            
            if (propPersonBean.getAddress1() != null){
                street = propPersonBean.getAddress1();
                postalAddressType.getStreet().clear();
                postalAddressType.getStreet().add(street);
                if (propPersonBean.getAddress2() != null) {
                    street2 =  propPersonBean.getAddress2();
                    postalAddressType.getStreet().add(street2);
                    if (propPersonBean.getAddress3() != null) {
                        street3 =  propPersonBean.getAddress3();
                        postalAddressType.getStreet().add(street3);
                    }
                }
            }
          }
               
        return postalAddressType ;
    }
       
    // overloaded method for DepartmentPersonFormBean
    public PostalAddressType getPostalAddressInfo(DepartmentPersonFormBean orgPersonBean,
                                                 RolodexDetailsBean rolodexBean )
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
              
        postalAddressType = objFactory.createPostalAddressType();
        
        String street = null;
        String street2 = null;
        String street3 = null;
            
        if (orgPersonBean.getAddress1() != null){
            street = orgPersonBean.getAddress1();
            postalAddressType.getStreet().clear();
            postalAddressType.getStreet().add(street);
            if (orgPersonBean.getAddress2() != null) {
                street2 = orgPersonBean.getAddress2();
                postalAddressType.getStreet().add(street2);
                if (orgPersonBean.getAddress3() != null) {
                    street3 = orgPersonBean.getAddress3();
                    postalAddressType.getStreet().add(street3);
                }
            }
        }
        
            postalAddressType.setCity((orgPersonBean.getCity() == null||
                                            orgPersonBean.getCity().trim().equals("")) ? "Unknown" :
                                            orgPersonBean.getCity());
           
            postalAddressType.setPostalCode((orgPersonBean.getPostalCode() == null||
                                            orgPersonBean.getPostalCode().trim().equals("")) ? "Unknown" :
                                            orgPersonBean.getPostalCode());
   
            postalAddressType.setCountry((orgPersonBean.getCountryCode() == null||
                                      orgPersonBean.getCountryCode().trim().equals("")) ? "Unknown" :
                                      orgPersonBean.getCountryCode());
   
            if (orgPersonBean.getState() != null)
                postalAddressType.setState(orgPersonBean.getState());
     
 
        return postalAddressType ;
    }
      
}
