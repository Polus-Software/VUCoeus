/*
 * KeyPersonTypeStream.java
 *
 * Created on March 31, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.ContactInfoTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.PersonFullNameTypeStream;

import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType;
import edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType;
import edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType;

public class KeyPersonTypeStream {
    
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    
    KeyPersonType keyPersonType;
  
    public KeyPersonTypeStream(ObjectFactory objFactory, 
                            edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory, 
                            edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory) 
    {
        this.objFactory = objFactory ;  
        this.commonObjFactory = commonObjFactory;
        this.rarObjFactory = rarObjFactory;
             
    }
   
  
       public KeyPersonType getKeyPerson(ProposalKeyPersonFormBean keyPersonBean,
                    ProposalPersonFormBean propPersonBean,
                    RolodexDetailsBean orgRolodexBean) 
               throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
         keyPersonType = objFactory.createKeyPersonType();
         
         
         /** name
          */
         PersonFullNameTypeStream personFullNameTypeStream
            = new PersonFullNameTypeStream(rarObjFactory);    
         keyPersonType.setName(personFullNameTypeStream.getPersonFullNameTypeInfo(propPersonBean ));
        
         /** account identifier (personid)
          * - change this to be the era commons name instead of person id, jan 27,2006
          */
       //  keyPersonType.setAccountIdentifier(propPersonBean.getPersonId());
         if (propPersonBean.getEraCommonsUsrName() == null) {
            keyPersonType.setAccountIdentifier("Unknown");
         }else {
            keyPersonType.setAccountIdentifier(propPersonBean.getEraCommonsUsrName());
         }
  
             
         
          /** contact info
          */
         ContactInfoTypeStream contactInfoTypeStream
            = new ContactInfoTypeStream(commonObjFactory);  
         keyPersonType.setContactInformation(contactInfoTypeStream.getContactInfo
                (propPersonBean, orgRolodexBean));
        
         /** project role
          */
         keyPersonType.setRoleOnProject(keyPersonBean.getProjectRole());
         
         /**
          *key person flag
          */
        
        KeyPersonType.KeyPersonFlagType flag = rarObjFactory.createKeyPersonTypeKeyPersonFlagType();
         
        if ( keyPersonBean.getPercentageEffort() != 999) {
            flag.setKeyPersonFlagCode("true");
            flag.setKeyPersonFlagDesc("Key Person");
        }
        else {
            flag.setKeyPersonFlagCode("false");
            flag.setKeyPersonFlagDesc("Collaborator/Other");
        }
        keyPersonType.setKeyPersonFlag(flag);
        
         
         /** organization name
          * if person is from MIT, use organization name from cover page applicant org
          * otherwise, use organization from rolodex
          */
         ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
         
         String org = propPrintingTxnBean.getPersonOrg ( 
                                     keyPersonBean.getProposalNumber(), 
                                     keyPersonBean.getPersonId());
         
         keyPersonType.setOrganizationName(org);
         
         /** biosketch 
          */
         KeyPersonBiosketchStream keyPersonBiosketchStream
            = new KeyPersonBiosketchStream(objFactory);
         keyPersonType.setNIHBiographicalSketch(keyPersonBiosketchStream.getKeyBiosketch(keyPersonBean));
         
         //coeusqa-2420 start
//         //case 3405 start                                                                                                                                                    
//         keyPersonType.getDegree().add(propPersonBean.getDegree());
//         //case 3405 end
         setDegreeInfo(keyPersonType,propPersonBean.getProposalNumber(),propPersonBean.getPersonId() );
         //coeusqa-2420 end
         
         return keyPersonType;
    }
       
       //overloaded method used when adding investigator as a key person
   
  
       public KeyPersonType getKeyPerson(ProposalPersonFormBean propPersonBean, 
                String leadUnitNum, RolodexDetailsBean orgRolodexBean, String piFlag)
                 throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
         keyPersonType = objFactory.createKeyPersonType();
   
         
         /** name
          */
         PersonFullNameTypeStream personFullNameTypeStream
            = new PersonFullNameTypeStream(rarObjFactory);    
         keyPersonType.setName(personFullNameTypeStream.getPersonFullNameTypeInfo(propPersonBean ));
        
         /** account identifier (personid)
          */
         //change sept 28
 //        keyPersonType.setAccountIdentifier(propPersonBean.getPersonId());
         if(propPersonBean.getEraCommonsUsrName() != null)
         keyPersonType.setAccountIdentifier(propPersonBean.getEraCommonsUsrName());
    
         
         
    
             
         /** title
          */
         keyPersonType.setPositionTitle(propPersonBean.getDirTitle());
          /** contact info
          */
         ContactInfoTypeStream contactInfoTypeStream
            = new ContactInfoTypeStream(commonObjFactory);  
         keyPersonType.setContactInformation(contactInfoTypeStream.getContactInfo
                   (propPersonBean, orgRolodexBean));
        
         /** project role
          */
         if (piFlag == "Y") {
            keyPersonType.setRoleOnProject("Principal Investigator");}
         else {
            keyPersonType.setRoleOnProject("Co-Investigator");}
        
         
         /** key person flag
          */
         KeyPersonType.KeyPersonFlagType flag = rarObjFactory.createKeyPersonTypeKeyPersonFlagType();
         
         flag.setKeyPersonFlagCode("true");
         //case 2229
         if (piFlag == "Y"){
           flag.setKeyPersonFlagDesc("PI");
         } else {
           flag.setKeyPersonFlagDesc("Co-PD/PI");
          }
          keyPersonType.setKeyPersonFlag(flag);
   
        
         /** organization name
          * if person is from MIT, use organization name from cover page applicant org
          * otherwise, use organization from rolodex
          */
         ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
         
         String org = propPrintingTxnBean.getPersonOrg ( 
                                     propPersonBean.getProposalNumber(), 
                                     propPersonBean.getPersonId());
         keyPersonType.setOrganizationName(org);
       
         /** organization department - this is the lead unit of the proposal
          */
          UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
          
          keyPersonType.setOrganizationDepartment(unitDataTxnBean.getUnitName(leadUnitNum));
         
          /** organizational division
           */
          
        
          UnitDetailFormBean unitBean = unitDataTxnBean.getUnitDetailsWithMajorSubdivision(leadUnitNum);
          String subdivisionUnitName = unitBean.getMajorSubdivisionUnitName();
          System.out.println("in keyperson stream, sub unit name is " + subdivisionUnitName);
          
          keyPersonType.setOrganizationDivision( subdivisionUnitName);
          
         /** biosketch
          */
         KeyPersonBiosketchStream keyPersonBiosketchStream
            = new KeyPersonBiosketchStream(objFactory);
         System.out.println("......before adding to nihbio sketch");
         keyPersonType.setNIHBiographicalSketch(keyPersonBiosketchStream.getKeyBiosketch(propPersonBean));
         System.out.println("....returning keypersontype");
         
           //coeusqa-2420 start
//         //case 3405 start
//         keyPersonType.getDegree().add(propPersonBean.getDegree());
//         //case 3405 end
         setDegreeInfo(keyPersonType,propPersonBean.getProposalNumber(),propPersonBean.getPersonId() );
         //coeusqa-2420 end
         return keyPersonType;
    }
       
      //overloaded method used if there are no investigators
       public KeyPersonType getKeyPerson(String noPersonID ,
                String leadUnitNum)
                 throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
         keyPersonType = objFactory.createKeyPersonType();
   
         
         /** name
          */
         PersonFullNameType personFullNameType = rarObjFactory.createPersonFullNameType();
         personFullNameType.setFirstName("Unknown");
         personFullNameType.setLastName("Unknown");
         keyPersonType.setName(personFullNameType);
        
         /** account identifier (personid)
          */
         keyPersonType.setAccountIdentifier(noPersonID);
         
         /** title
          */
         keyPersonType.setPositionTitle("Unknown");
         
          /** contact info
          */
         
         ContactInfoType contactInfoType = commonObjFactory.createContactInfoType();
         PostalAddressType postalAddressType = commonObjFactory.createPostalAddressType();
         postalAddressType.setCity("Unknown");
         postalAddressType.setPostalCode("Unknown");
         postalAddressType.setCountry("Unknown");
         contactInfoType.setPostalAddress(postalAddressType);
         keyPersonType.setContactInformation(contactInfoType);
        
         /** project role
          */
       
         keyPersonType.setRoleOnProject("Principle Investigator");
      
         
         /** key person flag
          */
         KeyPersonType.KeyPersonFlagType flag = rarObjFactory.createKeyPersonTypeKeyPersonFlagType();
        
         flag.setKeyPersonFlagCode("true");
         flag.setKeyPersonFlagDesc("PI");
         keyPersonType.setKeyPersonFlag(flag);
   
        
         /** organization name
          */         
         keyPersonType.setOrganizationName("Unknown");
         
       
         /** organization department - 
          */
           
          keyPersonType.setOrganizationDepartment("Unknown");
         
          /** organizational division
           */
          
          keyPersonType.setOrganizationDivision( "Unknown");
          
         /** biosketch
          */
         KeyPersonBiosketchStream keyPersonBiosketchStream
            = new KeyPersonBiosketchStream(objFactory);
         KeyPersonBiosketchType keyPersonBiosketchType = objFactory.createKeyPersonBiosketchType();
         keyPersonBiosketchType.setPositionsHonorsCitationsFileIdentifier("Unknown");
         keyPersonBiosketchType.setResearchSupportFileIdentifier("Unknown");
         
         keyPersonType.setNIHBiographicalSketch(keyPersonBiosketchType);
        
         
         return keyPersonType;
    }
       
    //coeusqa-2420 start
     private void setDegreeInfo
    (KeyPersonType keyPersonType, String propNumber, String personId)throws DBException, CoeusException{
        ProposalPerDegreeFormBean proposalPerDegreeForm = new ProposalPerDegreeFormBean();
        ProposalPersonTxnBean proposalPersonTxnBean;
        proposalPersonTxnBean = new ProposalPersonTxnBean();
        proposalPerDegreeForm =
            proposalPersonTxnBean.getProposalPersonDegreeMax(propNumber, personId);
        if(proposalPerDegreeForm != null){
            if (proposalPerDegreeForm.getDegreeDescription() != null)
                keyPersonType.getDegree().add(proposalPerDegreeForm.getDegreeDescription());
//            if (proposalPerDegreeForm.getGraduationDate() != null){
//                Date graduationDate = proposalPerDegreeForm.getGraduationDate();
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
//                String degreeYear = sdf.format(graduationDate);
//                keyPersonType.setDegreeYear(degreeYear);
//            }

         }
    }

  //coeus-2420 end
       
}
    
   