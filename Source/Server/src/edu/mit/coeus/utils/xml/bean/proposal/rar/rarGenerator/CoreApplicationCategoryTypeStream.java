/*
 * CoreApplicationCategoryTypeStream.java
 *
 * Created on March 2, 2004, 5:59 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 

public class CoreApplicationCategoryTypeStream {
    ObjectFactory objFactory ;
    CoreApplicationCategoryType coreAppCategoryType;
 
    /** Creates a new instance of CoreApplicationCategoryTypeStream */
    public CoreApplicationCategoryTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
        public CoreApplicationCategoryType getCoreAppCategory(ProposalDevelopmentFormBean propDevFormBean)
           throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
       // Expected list of values is N (new), R (Revision) or C (competing continutation)
       // not hard coding now
        // putting whole description in for now
       // also not sure if this is the right element for  this
        
      
       
       coreAppCategoryType = objFactory.createCoreApplicationCategoryType();
              
       
       
       coreAppCategoryType.setCategoryIdentifier
             ((propDevFormBean.getProposalTypeDesc()== null ? " " : propDevFormBean.getProposalTypeDesc()));
     
       return coreAppCategoryType;
     }
   
    
}
