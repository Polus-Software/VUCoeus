/*
 * NSFOtherPersonnelTypeStream.java
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
import edu.mit.coeus.utils.xml.bean.proposal.bean.*;
import java.math.*;

public class NSFOtherPersonnelTypeStream {
    
    ObjectFactory objFactory ;
  
    NSFOtherPersonnelType nsfOtherPersonnelType;
    
  
    public NSFOtherPersonnelTypeStream(ObjectFactory objFactory)
     {
        this.objFactory = objFactory ;  
             
    }
   
  
     public NSFOtherPersonnelType getNSFOtherPersonnel(NSFOtherPersonnelBean otherPersonnelBean) 
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
         nsfOtherPersonnelType = objFactory.createNSFOtherPersonnelType();
        
         nsfOtherPersonnelType.setClericalCount(otherPersonnelBean.getClericalCount());
         nsfOtherPersonnelType.setClericalFunds(otherPersonnelBean.getClericalFunds());
         nsfOtherPersonnelType.setGradCount(otherPersonnelBean.getGradCount());
         nsfOtherPersonnelType.setGradFunds(otherPersonnelBean.getGradFunds());
         nsfOtherPersonnelType.setOtherCount(otherPersonnelBean.getOtherCount());
         nsfOtherPersonnelType.setOtherFunds(otherPersonnelBean.getOtherFunds());
         nsfOtherPersonnelType.setOtherLAFunds(otherPersonnelBean.getOtherLAFunds());
         nsfOtherPersonnelType.setOtherProfCount(otherPersonnelBean.getOtherProfCount());
         nsfOtherPersonnelType.setOtherProfFunds(otherPersonnelBean.getOtherProfFunds());
         nsfOtherPersonnelType.setPostDocCount(otherPersonnelBean.getPostDocCount());
         nsfOtherPersonnelType.setPostDocFunds(otherPersonnelBean.getPostDocFunds());
         nsfOtherPersonnelType.setUnderGradCount(otherPersonnelBean.getUnderGradCount());
         nsfOtherPersonnelType.setUnderGradFunds(otherPersonnelBean.getUnderGradFunds());
         
         return nsfOtherPersonnelType;
    }
       
}
    
   