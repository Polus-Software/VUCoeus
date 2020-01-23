/*
 * PersonFullNameTypeStream.java
 *
 * Created on MAr 3, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;   
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.xml.bean.proposal.bean.*;

  
public class PersonFullNameTypeStream
{
    ObjectFactory objFactory ;
    PersonFullNameType personFullNameType; 
   

    /** Creates a new instance of PersonFullNameTypeStream */
    public PersonFullNameTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
    }
     
  
     public PersonFullNameType getPersonFullNameTypeInfo(ProposalPersonFormBean propPersonBean)
               throws CoeusException,  javax.xml.bind.JAXBException
    {  
       personFullNameType = objFactory.createPersonFullNameType();
         
       //handle case of no investigator
       if (propPersonBean.getPersonId().equals("XXXXXXXXX")){
           personFullNameType.setLastName("Unknown");
           personFullNameType.setFirstName("Unknown");
           personFullNameType.setMiddleName("Unknown");
       } else {
            personFullNameType.setLastName(UtilFactory.convertNull(propPersonBean.getLastName()));
            personFullNameType.setFirstName(UtilFactory.convertNull(propPersonBean.getFirstName()));
            personFullNameType.setMiddleName(UtilFactory.convertNull(propPersonBean.getMiddleName()));
       }
       return personFullNameType;
    }
     
      public PersonFullNameType getPersonFullNameTypeInfo(RolodexDetailsBean  rolodexBean)
               throws CoeusException,  javax.xml.bind.JAXBException
    {  
           
       personFullNameType = objFactory.createPersonFullNameType();
          
       personFullNameType.setLastName(UtilFactory.convertNull(rolodexBean.getLastName()));
       personFullNameType.setFirstName(UtilFactory.convertNull(rolodexBean.getFirstName()));
       personFullNameType.setMiddleName(UtilFactory.convertNull(rolodexBean.getMiddleName()));
      
       return personFullNameType;
    }
      
      public PersonFullNameType getPersonFullNameTypeInfo(SalaryAndWagesBean salaryAndWagesBean)
               throws CoeusException,  javax.xml.bind.JAXBException
    {  
           
       personFullNameType = objFactory.createPersonFullNameType();
          
       personFullNameType.setLastName(UtilFactory.convertNull(salaryAndWagesBean.getLastName()));
       personFullNameType.setFirstName(UtilFactory.convertNull(salaryAndWagesBean.getFirstName()));
       personFullNameType.setMiddleName(UtilFactory.convertNull(salaryAndWagesBean.getMiddleName()));
      
       return personFullNameType;
    }
        
       public PersonFullNameType getPersonFullNameTypeInfo(ProposalPersonBean proposalPersonBean)
               throws CoeusException,  javax.xml.bind.JAXBException
    {  
           
       personFullNameType = objFactory.createPersonFullNameType();
          
       personFullNameType.setLastName(UtilFactory.convertNull(proposalPersonBean.getLastName()));
       personFullNameType.setFirstName(UtilFactory.convertNull(proposalPersonBean.getFirstName()));
       personFullNameType.setMiddleName(UtilFactory.convertNull(proposalPersonBean.getMiddleName()));
      
       return personFullNameType;
    }
           
      public PersonFullNameType getPersonFullNameTypeInfo(DepartmentPersonFormBean deptPersonBean)
               throws CoeusException,  javax.xml.bind.JAXBException
    {  
           
       personFullNameType = objFactory.createPersonFullNameType();
          
       personFullNameType.setLastName(UtilFactory.convertNull(deptPersonBean.getLastName()));
       personFullNameType.setFirstName(UtilFactory.convertNull(deptPersonBean.getFirstName()));
       personFullNameType.setMiddleName(UtilFactory.convertNull(deptPersonBean.getMiddleName()));
     
       return personFullNameType;
    }
   
}
