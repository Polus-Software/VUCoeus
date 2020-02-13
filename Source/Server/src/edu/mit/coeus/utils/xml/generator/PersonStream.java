/*
 * PersonStream.java
 *
 * Created on November 20, 2003, 3:26 PM
 */

package edu.mit.coeus.utils.xml.generator;


import edu.mit.coeus.utils.xml.bean.schedule.CommitteeMasterDataType ;
import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.utils.xml.bean.schedule.ObjectFactory;
import java.math.BigInteger;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import java.util.Vector;
import edu.mit.coeus.utils.xml.bean.schedule.PersonType;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.utils.xml.bean.schedule.CorrespondentType;
import edu.mit.coeus.utils.xml.bean.schedule.KeyStudyPersonType;
import edu.mit.coeus.utils.xml.bean.schedule.InvestigatorType;
import edu.mit.coeus.utils.xml.bean.schedule.ProtocolReviewerType;
import edu.mit.coeus.utils.xml.bean.schedule.UnitType;
import edu.mit.coeus.utils.xml.bean.schedule.CommitteeMemberType;
import edu.mit.coeus.utils.xml.bean.schedule.CommitteeMemberRoleType;
import edu.mit.coeus.utils.xml.bean.schedule.ResearchAreaType;
import edu.mit.coeus.utils.xml.bean.schedule.Unit;

/**
 *
 * @author  prahalad
 */
public class PersonStream
{
    ObjectFactory objFactory ;
    PersonInfoTxnBean personTxnBean ;
    RolodexMaintenanceDataTxnBean rolodexTxnBean ;
    
    /** Creates a new instance of PersonStream */
    public PersonStream(ObjectFactory objFactory)
    {
        this.objFactory = objFactory ;
        personTxnBean = new PersonInfoTxnBean() ;
        rolodexTxnBean = new RolodexMaintenanceDataTxnBean();
    }
    
    public PersonType getPerson(Object dataBean) throws CoeusException, DBException, javax.xml.bind.JAXBException 
    {
       PersonType person = objFactory.createPersonType() ;
       String personId = null ; 
       Boolean facultyFlag = new Boolean(false) ;
       boolean nonEmployeeFlag = false ;
       
      // this if will be true when called from correspondents
       if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.irb.bean.ProtocolCorrespondentsBean"))
       {    
            ProtocolCorrespondentsBean protocolCorrespondentsBean = 
                (ProtocolCorrespondentsBean) dataBean ;
           try
           {
            facultyFlag = new Boolean(!protocolCorrespondentsBean.isNonEmployeeFlag()) ; //prps check protocolCorrespondentsBean doesnt have isFacultyFlag method
           }
           catch(Exception ex)
           {
            // just let faculty flag be false
           }
            nonEmployeeFlag = protocolCorrespondentsBean.isNonEmployeeFlag() ;
            personId = protocolCorrespondentsBean.getPersonId() ;
       }  // this if will be true when called from KeyStudyPersonnel
       else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.irb.bean.ProtocolKeyPersonnelBean"))
       {
             ProtocolKeyPersonnelBean protocolKeyStudyPersonBean = 
                   (ProtocolKeyPersonnelBean) dataBean ;
              try
              {
                facultyFlag = new Boolean(protocolKeyStudyPersonBean.isFacultyFlag()) ;
              }
              catch(Exception ex)
              {
                // just let faculty flag be false
              }
              nonEmployeeFlag = protocolKeyStudyPersonBean.isNonEmployeeFlag() ;
              personId = protocolKeyStudyPersonBean.getPersonId() ;
       } //this if will be true when called from Investigators
       else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.irb.bean.ProtocolInvestigatorsBean"))
       {
           ProtocolInvestigatorsBean protocolInvestigatorsBean = 
                            (ProtocolInvestigatorsBean) dataBean ;
           try
           {
                facultyFlag = new Boolean(protocolInvestigatorsBean.isFacultyFlag()) ;
           }
           catch(Exception ex)
           {
            // just let faculty flag be false
           }
           nonEmployeeFlag = protocolInvestigatorsBean.isNonEmployeeFlag() ;
           personId = protocolInvestigatorsBean.getPersonId() ;
       }    
       else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.irb.bean.ProtocolReviewerInfoBean"))
       {
         ProtocolReviewerInfoBean protocolReviewerInfoBean = 
                                    (ProtocolReviewerInfoBean) dataBean ;
        
         nonEmployeeFlag = protocolReviewerInfoBean.isNonEmployee() ;
         personId = protocolReviewerInfoBean.getPersonId() ;
       } 
       else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.irb.bean.CommitteeMembershipDetailsBean"))
       {
            CommitteeMembershipDetailsBean membershipBean =
                                           (CommitteeMembershipDetailsBean) dataBean ;
            
            nonEmployeeFlag = membershipBean.getNonEmployeeFlag()=='N'? false:true ;
            personId = membershipBean.getPersonId() ;
       }    
       
        person.setFacultyFlag(facultyFlag) ; 
        person.setEmployeeFlag(nonEmployeeFlag) ;

         if (!nonEmployeeFlag )
         { // get details from person table
             PersonInfoFormBean personBean = personTxnBean.getPersonInfo(personId) ;
             person.setPersonID(personBean.getPersonID()) ;
             person.setFullname(personBean.getFullName()) ;
             person.setLastName(personBean.getLastName()) ;
             person.setFirstname(personBean.getFirstName()) ;
             //case 1646 start
             person.setDegree(personBean.getDegree() != null?personBean.getDegree() : null);
             person.setSalutation(personBean.getSaluation() != null? personBean.getSaluation() : null);
             //case 1646 end
             person.setEmail(personBean.getEmail()!= null?personBean.getEmail() : null) ;
             person.setOfficeLocation(personBean.getOffLocation()!= null?personBean.getOffLocation(): null) ;
             person.setOfficePhone(personBean.getOffPhone()!= null?personBean.getOffPhone() : null) ;
             person.setDirectoryTitle(personBean.getDirTitle()!= null?personBean.getDirTitle() : null) ;
             //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - Start
             person.setAddressLine1(personBean.getAddressLine1()!= null? personBean.getAddressLine1() : null);  
             person.setAddressLine2(personBean.getAddressLine2()!= null? personBean.getAddressLine2() : null); 
             person.setAddressLine3(personBean.getAddressLine3()!= null? personBean.getAddressLine3() : null); 
             person.setCity(personBean.getCity()!= null? personBean.getCity() : null); 
             person.setState(personBean.getState()!= null? personBean.getState() : null); 
             person.setCountry(personBean.getCountry()!= null? personBean.getCountry() : null);              
             person.setCountryCode(personBean.getCountryCode()!= null? personBean.getCountryCode() : null); 
             person.setPostalCode(personBean.getPostalCode()!= null? personBean.getPostalCode() : null); 
             person.setFaxNumber(personBean.getFaxNumber()!= null? personBean.getFaxNumber() : null); 
             person.setPagerNumber(personBean.getPagerNumber()!= null? personBean.getPagerNumber() : null); 
             person.setMobilePhoneNumber(personBean.getMobilePhoneNumber()!= null? personBean.getMobilePhoneNumber() : null); 
             //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - End
//                         if (personBean.getHomeUnit() != null)
//                         {    
//                             PersonType.HomeUnitType homeUnit = objFactory.createPersonTypeHomeUnitType() ;
//                             homeUnit.setUnitName(personBean.getUnitName()) ;
//                             homeUnit.setUnitNumber(personBean.getHomeUnit()) ;
//                             person.setHomeUnit(homeUnit) ; 
//                         }   
             person.setDepartmentOrganization(personBean.getDirDept()!= null?personBean.getDirDept() : null) ;
         }    
         else
         { // get details from rolodex table
            RolodexDetailsBean rolodexBean = rolodexTxnBean.getRolodexMaintenanceDetails(personId) ;
            person.setPersonID(rolodexBean.getRolodexId()) ;
            String fullName = rolodexBean.getMiddleName()!= null ? rolodexBean.getLastName() 
                                                                 + "," 
                                                                 + rolodexBean.getFirstName()
                                                                 + rolodexBean.getMiddleName(): rolodexBean.getLastName() + ","
                                                                 + rolodexBean.getFirstName() ;
            person.setFullname(fullName) ;
            person.setLastName(rolodexBean.getLastName()) ;
            person.setFirstname(rolodexBean.getFirstName()) ;
            person.setEmail(rolodexBean.getEMail()!= null? rolodexBean.getEMail(): null) ;
            //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - Start
             person.setAddressLine1(rolodexBean.getAddress1()!= null? rolodexBean.getAddress1() : null);  
             person.setAddressLine2(rolodexBean.getAddress2()!= null? rolodexBean.getAddress2() : null);  
             person.setAddressLine3(rolodexBean.getAddress3()!= null? rolodexBean.getAddress3() : null);  
             person.setCity(rolodexBean.getCity()!= null? rolodexBean.getCity() : null); 
             person.setState(rolodexBean.getState()!= null? rolodexBean.getState() : null); 
             person.setCountry(rolodexBean.getCountryName()!= null? rolodexBean.getCountryName() : null);              
             person.setCountryCode(rolodexBean.getCountry()!= null? rolodexBean.getCountry() : null);              
             person.setPostalCode(rolodexBean.getPostalCode()!= null? rolodexBean.getPostalCode() : null); 
             person.setFaxNumber(rolodexBean.getFax()!= null? rolodexBean.getFax() : null); 
             person.setMobilePhoneNumber(rolodexBean.getPhone()!= null? rolodexBean.getPhone() : null); 
             //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - End
//                        if (rolodexBean.getOwnedByUnit() != null)
//                        {    
//                            PersonType.HomeUnitType homeUnit = objFactory.createPersonTypeHomeUnitType() ;
//                            homeUnit.setUnitNumber(rolodexBean.getOwnedByUnit()) ;
//                            homeUnit.setUnitName(rolodexBean.getOrganization()) ;
//                            person.setHomeUnit(homeUnit) ; 
//                        }    
            person.setDepartmentOrganization(rolodexBean.getOrganization()!= null?rolodexBean.getOrganization(): null) ;
         }    

           return person ;
    }
      
   public ProtocolReviewerType getReviewer(ProtocolReviewerInfoBean protocolReviewerInfoBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
   {
        ProtocolReviewerType protocolReviewer = objFactory.createProtocolReviewerType();
        protocolReviewer.setPerson(getPerson(protocolReviewerInfoBean)) ;
        
        protocolReviewer.setReviewerTypeDesc(protocolReviewerInfoBean.getReviewerTypeDesc());
        try
        {
            protocolReviewer.setReviewerTypeCode(new BigInteger(String.valueOf(protocolReviewerInfoBean.getReviewerTypeCode()))) ;
        }
        catch(Exception afc)
        {
            // do nothing so that this tag doesnt get included in the xml generated
        }
    
        return protocolReviewer ;
   }
    
   public InvestigatorType getInvestigator(ProtocolInvestigatorsBean protocolInvestigatorsBean) throws CoeusException, DBException, javax.xml.bind.JAXBException 
   {
       InvestigatorType protocolInvestigator = objFactory.createInvestigatorType() ;
       protocolInvestigator.setPerson(getPerson(protocolInvestigatorsBean)) ;
        try
        {
            protocolInvestigator.setAffiliationCode(new BigInteger(String.valueOf(protocolInvestigatorsBean.getAffiliationTypeCode()))) ;
        }
         catch(Exception afc)
        {
        // do nothing so that this tag doesnt get included in the xml generated
        }
        protocolInvestigator.setAffiliationDesc(protocolInvestigatorsBean.getAffiliationTypeDescription()) ;
        try
        {
            protocolInvestigator.setPIFlag(protocolInvestigatorsBean.isPrincipalInvestigatorFlag()) ;
        }
        catch(Exception afc)
        {
        // do nothing so that this tag doesnt get included in the xml generated
        } 
        
        
        Vector vecUnits = protocolInvestigatorsBean.getInvestigatorUnits() ;
        if (vecUnits != null)
        {    
            for (int unitCount = 0; unitCount < vecUnits.size() ; unitCount++)
            {    
                ProtocolInvestigatorUnitsBean protocolInvestigatorUnitsBean 
                                = (ProtocolInvestigatorUnitsBean) vecUnits.get(unitCount) ;

          //    UnitType unitType = objFactory.createUnitType() ;
                Unit unitType = objFactory.createUnit();  //case 1671
                unitType.setUnitName(protocolInvestigatorUnitsBean.getUnitName()) ;
                unitType.setUnitNumber(protocolInvestigatorUnitsBean.getUnitNumber()) ;
              
                InvestigatorType.LeadUnitFlag leadUnitFlag = objFactory.createInvestigatorTypeLeadUnitFlag() ;
                leadUnitFlag.setValue(protocolInvestigatorUnitsBean.isLeadUnitFlag()) ;
                protocolInvestigator.getUnitAndLeadUnitFlag().add(unitType) ;
                protocolInvestigator.getUnitAndLeadUnitFlag().add(leadUnitFlag) ;

            }// end for 

        } // end if vecUnits
                    
        
        return protocolInvestigator ;
   }
    
    
    public KeyStudyPersonType getKeyStudyPersonnel(ProtocolKeyPersonnelBean protocolKeyStudyPersonBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        KeyStudyPersonType keyStudyPerson = objFactory.createKeyStudyPersonType() ;
        keyStudyPerson.setPerson(getPerson(protocolKeyStudyPersonBean)) ;
        keyStudyPerson.setAffiliation(protocolKeyStudyPersonBean.getAffiliationTypeDescription()) ;
        keyStudyPerson.setRole(protocolKeyStudyPersonBean.getPersonRole()) ;
                    
        return keyStudyPerson ;
    }
    
    public CorrespondentType getCorrespondent(ProtocolCorrespondentsBean protocolCorrespondentsBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
       CorrespondentType correspondent = objFactory.createCorrespondentType() ;
       correspondent.setPerson(getPerson(protocolCorrespondentsBean)) ;
       correspondent.setCorrespondentComments(protocolCorrespondentsBean.getComments()) ;
       correspondent.setTypeOfCorrespondent(protocolCorrespondentsBean.getCorrespondentTypeDesc()) ;
       return correspondent ;
    }
    
       
    
}
