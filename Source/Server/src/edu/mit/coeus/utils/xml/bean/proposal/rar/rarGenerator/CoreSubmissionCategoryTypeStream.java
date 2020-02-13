/*
 * CoreSubmissionCategoryTypeStream.java
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

public class CoreSubmissionCategoryTypeStream {
    ObjectFactory objFactory ;
    CoreSubmissionCategoryType coreSubmissionCategoryType;
    
    /** Creates a new instance of CoreSubmissionCategoryTypeStream */
    public CoreSubmissionCategoryTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public CoreSubmissionCategoryType getCoreSubCategory(ProposalDevelopmentFormBean propDevFormBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    { 
     
      coreSubmissionCategoryType = objFactory.createCoreSubmissionCategoryType();
       
      // not hard coding, but not sure if this is the right element for these codes

       
      coreSubmissionCategoryType.setProjectCategory
            ((propDevFormBean.getProposalActivityTypeDesc()== null ? " " : propDevFormBean.getProposalActivityTypeDesc()));
     
   
      coreSubmissionCategoryType.setSubmissionStatus
            ((propDevFormBean.getCreationStatusDescription()== null ? " " : propDevFormBean.getCreationStatusDescription()));
     
     
      return coreSubmissionCategoryType;
    }
    
}
