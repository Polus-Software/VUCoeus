/*
 * PersonStream.java
 *
 * Created on November 20, 2003, 3:26 PM
 */

/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.xml.iacuc.generator;

import edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean;
import edu.mit.coeus.iacuc.bean.PersonInfoFormBean;
import edu.mit.coeus.iacuc.bean.PersonInfoTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolCorrespondentsBean;
import edu.mit.coeus.iacuc.bean.ProtocolInvestigatorUnitsBean;
import edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean;
import edu.mit.coeus.iacuc.bean.ProtocolKeyPersonnelBean;
import edu.mit.coeus.iacuc.bean.ProtocolReviewerInfoBean;
import edu.mit.coeus.xml.iacuc.ObjectFactory;
import edu.mit.coeus.xml.iacuc.impl.UnitTypeImpl;
import java.math.BigInteger;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import edu.mit.coeus.xml.iacuc.PersonType;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.xml.iacuc.CorrespondentType;
import edu.mit.coeus.xml.iacuc.KeyStudyPersonType;
import edu.mit.coeus.xml.iacuc.InvestigatorType;
import edu.mit.coeus.xml.iacuc.ProtocolReviewerType;
import edu.mit.coeus.xml.iacuc.UnitType;

/**
 *
 * @author  prahalad
 */
public class PersonStream {
    ObjectFactory objFactory ;
    PersonInfoTxnBean personTxnBean ;
    RolodexMaintenanceDataTxnBean rolodexTxnBean ;
    
    /** Creates a new instance of PersonStream */
    public PersonStream(ObjectFactory objFactory) {
        this.objFactory = objFactory ;
        personTxnBean = new PersonInfoTxnBean() ;
        rolodexTxnBean = new RolodexMaintenanceDataTxnBean();
    }
    
    public PersonType getPerson(Object dataBean) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        PersonType person = objFactory.createPersonType() ;
        String personId = null ;
        Boolean facultyFlag = new Boolean(false) ;
        boolean nonEmployeeFlag = false ;
        
        // this if will be true when called from correspondents
        if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.iacuc.bean.ProtocolCorrespondentsBean")) {
            ProtocolCorrespondentsBean protocolCorrespondentsBean =
                    (ProtocolCorrespondentsBean) dataBean ;
            try {
                facultyFlag = new Boolean(!protocolCorrespondentsBean.isNonEmployeeFlag()) ; //prps check protocolCorrespondentsBean doesnt have isFacultyFlag method
            } catch(Exception ex) {
                // just let faculty flag be false
            }
            nonEmployeeFlag = protocolCorrespondentsBean.isNonEmployeeFlag() ;
            personId = protocolCorrespondentsBean.getPersonId() ;
        }  // this if will be true when called from KeyStudyPersonnel
        else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.iacuc.bean.ProtocolKeyPersonnelBean")) {
            ProtocolKeyPersonnelBean protocolKeyStudyPersonBean =
                    (ProtocolKeyPersonnelBean) dataBean ;
            try {
                facultyFlag = new Boolean(protocolKeyStudyPersonBean.isFacultyFlag()) ;
            } catch(Exception ex) {
                // just let faculty flag be false
            }
            nonEmployeeFlag = protocolKeyStudyPersonBean.isNonEmployeeFlag() ;
            personId = protocolKeyStudyPersonBean.getPersonId() ;
        } //this if will be true when called from Investigators
        else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean")) {
            ProtocolInvestigatorsBean protocolInvestigatorsBean =
                    (ProtocolInvestigatorsBean) dataBean ;
            try {
                facultyFlag = new Boolean(protocolInvestigatorsBean.isFacultyFlag()) ;
            } catch(Exception ex) {
                // just let faculty flag be false
            }
            nonEmployeeFlag = protocolInvestigatorsBean.isNonEmployeeFlag() ;
            personId = protocolInvestigatorsBean.getPersonId() ;
        } else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.iacuc.bean.ProtocolReviewerInfoBean")) {
            ProtocolReviewerInfoBean protocolReviewerInfoBean =
                    (ProtocolReviewerInfoBean) dataBean ;
            
            nonEmployeeFlag = protocolReviewerInfoBean.isNonEmployee() ;
            personId = protocolReviewerInfoBean.getPersonId() ;
        } else if (dataBean.getClass().getName().equalsIgnoreCase("edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean")) {
            CommitteeMembershipDetailsBean membershipBean =
                    (CommitteeMembershipDetailsBean) dataBean ;
            
            nonEmployeeFlag = membershipBean.getNonEmployeeFlag()=='N'? false:true ;
            personId = membershipBean.getPersonId() ;
        }
        
        person.setFacultyFlag(facultyFlag) ;
        person.setEmployeeFlag(nonEmployeeFlag) ;
        
        if (!nonEmployeeFlag ) { // get details from person table
            PersonInfoFormBean personBean = personTxnBean.getPersonInfo(personId) ;
            person.setPersonID(personBean.getPersonID()) ;
            person.setFullname(personBean.getFullName()) ;
            person.setLastName(personBean.getLastName()) ;
            //person.setMiddlename(personBean.get)
            person.setFirstname(personBean.getFirstName()) ;
            //case 1646 start
            person.setDegree(personBean.getDegree() != null?personBean.getDegree() : null);
            person.setSalutation(personBean.getSaluation() != null? personBean.getSaluation() : null);
            //case 1646 end
            person.setEmail(personBean.getEmail()!= null?personBean.getEmail() : null) ;
            person.setOfficeLocation(personBean.getOffLocation()!= null?personBean.getOffLocation(): null) ;
            person.setOfficePhone(personBean.getOffPhone()!= null?personBean.getOffPhone() : null) ;
            //person.setSchool();
            //person.setYearGraduated(personBean.get);
            //person.setCitizenship();
            person.setDirectoryTitle(personBean.getDirTitle()!= null?personBean.getDirTitle() : null) ;
            //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - Start
            person.setAddressLine1(personBean.getAddressLine1()!= null? personBean.getAddressLine1() : null);
            person.setAddressLine2(personBean.getAddressLine2()!= null? personBean.getAddressLine2() : null);
            person.setAddressLine3(personBean.getAddressLine3()!= null? personBean.getAddressLine3() : null);
            person.setCity(personBean.getCity()!= null? personBean.getCity() : null);
            person.setState(personBean.getState()!= null? personBean.getState() : null);
            
            person.setCounty(personBean.getCounty()!= null? personBean.getCounty() : null);
            person.setCountry(personBean.getCountry()!= null? personBean.getCountry() : null);
            person.setCountryCode(personBean.getCountryCode()!= null? personBean.getCountryCode() : null);
            person.setPostalCode(personBean.getPostalCode()!= null? personBean.getPostalCode() : null);
            person.setFaxNumber(personBean.getFaxNumber()!= null? personBean.getFaxNumber() : null);
            person.setPagerNumber(personBean.getPagerNumber()!= null? personBean.getPagerNumber() : null);
            person.setMobilePhoneNumber(personBean.getMobilePhoneNumber()!= null? personBean.getMobilePhoneNumber() : null);
            
            person.setDepartmentOrganization(personBean.getDirDept()!= null?personBean.getDirDept() : null) ;
        } else { // get details from rolodex table
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
            
            person.setDepartmentOrganization(rolodexBean.getOrganization()!= null?rolodexBean.getOrganization(): null) ;
        }
        
        return person ;
    }
    
    public ProtocolReviewerType getReviewer(ProtocolReviewerInfoBean protocolReviewerInfoBean) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        ProtocolReviewerType protocolReviewer = objFactory.createProtocolReviewerType();
        protocolReviewer.setPerson(getPerson(protocolReviewerInfoBean)) ;
        
        protocolReviewer.setReviewerTypeDesc(protocolReviewerInfoBean.getReviewerTypeDesc());
        
        protocolReviewer.setReviewerTypeCode(new BigInteger(String.valueOf(protocolReviewerInfoBean.getReviewerTypeCode()))) ;
         //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        if(protocolReviewerInfoBean.getUpdateTimestamp() != null){
            protocolReviewer.setUpdateTimestamp(formatDate(protocolReviewerInfoBean.getUpdateTimestamp())); 
        }
         //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        protocolReviewer.setUpdateUser(protocolReviewerInfoBean.getUpdateUser());
        return protocolReviewer ;
    }
    
    public InvestigatorType getInvestigator(ProtocolInvestigatorsBean protocolInvestigatorsBean) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        InvestigatorType protocolInvestigator = objFactory.createInvestigatorType() ;
        protocolInvestigator.setPerson(getPerson(protocolInvestigatorsBean)) ;
        protocolInvestigator.setAffiliationCode(new BigInteger(String.valueOf(protocolInvestigatorsBean.getAffiliationTypeCode()))) ;
        protocolInvestigator.setAffiliationDesc(protocolInvestigatorsBean.getAffiliationTypeDescription()) ;
        protocolInvestigator.setPIFlag(protocolInvestigatorsBean.isPrincipalInvestigatorFlag()) ;
        protocolInvestigator.setTrainingFlag(protocolInvestigatorsBean.isTrainingFlag() ? "Yes" : "No");
        Vector vecUnits = protocolInvestigatorsBean.getInvestigatorUnits() ;
        if (vecUnits != null) {
            for (int unitCount = 0; unitCount < vecUnits.size() ; unitCount++) {
                ProtocolInvestigatorUnitsBean protocolInvestigatorUnitsBean
                        = (ProtocolInvestigatorUnitsBean) vecUnits.get(unitCount) ;
                  //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
                //    UnitType unitType = objFactory.createUnitType() ;
               // UnitType unitType = objFactory.createInvestigatorTypeUnit();  //case 1671
                InvestigatorType.UnitType unitType = objFactory.createInvestigatorTypeUnitType();
                unitType.setLeadUnitFlag(protocolInvestigatorUnitsBean.isLeadUnitFlag());
                unitType.setUnitName(protocolInvestigatorUnitsBean.getUnitName()) ;
                unitType.setUnitNumber(protocolInvestigatorUnitsBean.getUnitNumber()) ;
                
               // todo: add the code for lead unit flag
               // InvestigatorType.LeadUnitFlag leadUnitFlag = objFactory.createInvestigatorTypeLeadUnitFlag() ;
         
//                leadUnitFlag.setValue(protocolInvestigatorUnitsBean.isLeadUnitFlag()) ;
//                protocolInvestigator.getUnitAndLeadUnitFlag().add(unitType) ;
//                protocolInvestigator.getUnitAndLeadUnitFlag().add(leadUnitFlag) ;
                  protocolInvestigator.getUnit().add(unitType) ;
                  
                  if(protocolInvestigatorsBean.getUpdateTimestamp() != null){
                      protocolInvestigator.setUpdateTimestamp(formatDate(protocolInvestigatorsBean.getUpdateTimestamp()));
                  }
                  protocolInvestigatorsBean.setUpdateUser(protocolInvestigatorsBean.getUpdateUser());
                  //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
            }// end for
            
        } // end if vecUnits
        
        
        return protocolInvestigator ;
    }
    
    
    public KeyStudyPersonType getKeyStudyPersonnel(ProtocolKeyPersonnelBean protocolKeyStudyPersonBean) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        KeyStudyPersonType keyStudyPerson = objFactory.createKeyStudyPersonType() ;
        keyStudyPerson.setPerson(getPerson(protocolKeyStudyPersonBean)) ;
        keyStudyPerson.setRole(protocolKeyStudyPersonBean.getPersonRoleDesc());
        keyStudyPerson.setAffiliation(protocolKeyStudyPersonBean.getAffiliationTypeDescription()) ;
        keyStudyPerson.setTrainingFlag(protocolKeyStudyPersonBean.isTrainingFlag()?"Yes":"No");
        //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        keyStudyPerson.setAffiliationTypeCode(protocolKeyStudyPersonBean.getAffiliationTypeCode());
        
        if(protocolKeyStudyPersonBean.getUpdateTimestamp() != null){
            keyStudyPerson.setUpdateTimestamp(formatDate(protocolKeyStudyPersonBean.getUpdateTimestamp()));
        }
        keyStudyPerson.setUpdateUser(protocolKeyStudyPersonBean.getUpdateUser());
        //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        
        return keyStudyPerson ;
    }
    
    public CorrespondentType getCorrespondent(ProtocolCorrespondentsBean protocolCorrespondentsBean) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        
        CorrespondentType correspondent = objFactory.createCorrespondentType() ;
        correspondent.setPerson(getPerson(protocolCorrespondentsBean)) ;
        correspondent.setCorrespondentComments(protocolCorrespondentsBean.getComments()) ;
        correspondent.setTypeOfCorrespondent(protocolCorrespondentsBean.getCorrespondentTypeDesc()) ;
         //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        if(protocolCorrespondentsBean.isNonEmployeeFlag()){
             correspondent.setNonEmployeeFlag("Y");
        }else{
            correspondent.setNonEmployeeFlag("N");
        }
        
        correspondent.setCorrespondentTypeCode(protocolCorrespondentsBean.getCorrespondentTypeCode());
        correspondent.setCorrespondentTypeDesc(protocolCorrespondentsBean.getCorrespondentTypeDesc());
        correspondent.setComments(protocolCorrespondentsBean.getComments());
        if(protocolCorrespondentsBean.getUpdateTimestamp() != null){
            correspondent.setUpdateTimestamp(formatDate(protocolCorrespondentsBean.getUpdateTimestamp()));
        }
        correspondent.setUpdateUser(protocolCorrespondentsBean.getUpdateUser());
        //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        return correspondent ;
    }
     //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
     /**
     *  Returns java.util.Calendar type object. Creates a calendar type object and set it time to date which is 
     *  passed to the method. Return the Calendar type object.
     *  @param date Date.
     *  @return calendar Calendar.
     */
    private Calendar formatDate(final Date date){
        Calendar calendar =null;
        if(date != null){
            calendar = calendar.getInstance();
            calendar.setTime(date);
        }
        
        return calendar;
    }
  //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end   
}
