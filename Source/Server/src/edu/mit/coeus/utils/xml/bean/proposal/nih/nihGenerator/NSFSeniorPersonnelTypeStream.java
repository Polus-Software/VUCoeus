/*
 * NSFSeniorPersonnelTypeStream.java
 *
 * Created on August 12, 2004
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
import edu.mit.coeus.utils.xml.bean.proposal.bean.NSFSeniorPersonnelBean;
import java.math.*;
import java.lang.String;


public class NSFSeniorPersonnelTypeStream {
    
    ObjectFactory objFactory ;
  
    NSFSeniorPersonnelType nsfSeniorPersonnelType;
  
    public NSFSeniorPersonnelTypeStream(ObjectFactory objFactory)
     
    {
        this.objFactory = objFactory ;  
             
    }
   
  
     public NSFSeniorPersonnelType getNSFSeniorPersonnel(NSFSeniorPersonnelBean seniorPersonnelBean) 
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
         nsfSeniorPersonnelType = objFactory.createNSFSeniorPersonnelType();       
         nsfSeniorPersonnelType.setFullName(seniorPersonnelBean.getName());       
         nsfSeniorPersonnelType.setTitle(seniorPersonnelBean.getTitle());      
         nsfSeniorPersonnelType.setAcademicMonthsFunded( seniorPersonnelBean.getAcademicMonthsFunded());     
         nsfSeniorPersonnelType.setCalendarMonthsFunded( seniorPersonnelBean.getCalendarMonthsFunded());         
         nsfSeniorPersonnelType.setSummerMonthsFunded(seniorPersonnelBean.getSummerMonthsFunded()); 
         nsfSeniorPersonnelType.setFundsRequested(seniorPersonnelBean.getFundsRequested());
         nsfSeniorPersonnelType.setPersonID(seniorPersonnelBean.getPersonId());
         nsfSeniorPersonnelType.setRownumber(new BigInteger(String.valueOf(seniorPersonnelBean.getRowNumber())));
        
         return nsfSeniorPersonnelType;
    }
       
}
    
   