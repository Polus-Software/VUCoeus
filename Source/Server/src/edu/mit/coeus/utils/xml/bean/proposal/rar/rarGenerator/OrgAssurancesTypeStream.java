/*
 * OrgAssurancesTypeStream.java
 *
 * Created on March 15, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */



import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.utils.xml.bean.proposal.rar.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.*;
import java.util.Vector;


public class OrgAssurancesTypeStream {
    
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
    OrgAssurancesType orgAssurances;
    OrganizationMaintenanceDataTxnBean orgTxnBean;
    OrganizationYNQBean[] arrayYNQ;
    
    
    /** Creates a new instance of OrgAssurancesTypeStream */
    public OrgAssurancesTypeStream(ObjectFactory objFactory, 
                  edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory) 
    {
        this.objFactory = objFactory ;
        this.commonObjFactory = commonObjFactory;
        
        orgTxnBean = new OrganizationMaintenanceDataTxnBean();
    }
    
   
    public OrgAssurancesType getOrgAssurances(OrganizationMaintenanceFormBean orgBean)
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
         orgAssurances = objFactory.createOrgAssurances();    
         AssuranceTypeStream assuranceTypeStream = new AssuranceTypeStream(commonObjFactory);
        
        
         arrayYNQ = orgTxnBean.getOrganizationYNQ(orgBean.getOrganizationId());
         //arrayYNQ is array of organizationYNQBeans
         
        
         orgAssurances.setDebarmentAndSuspension(assuranceTypeStream.getAssuranceTypeInfo(arrayYNQ,"I8"));
         orgAssurances.setDrugFreeWorkplace(assuranceTypeStream.getAssuranceTypeInfo(arrayYNQ ,"H5"));
       
          if (arrayYNQ != null) {
               
                for (int i = 0; i < arrayYNQ.length  ; i++) {
                   OrganizationYNQBean orgYNQBean = (OrganizationYNQBean) arrayYNQ[i];
                   if (orgYNQBean.getQuestionId().equals("H0")) {
                      orgAssurances.setLobbyingQuestion(orgYNQBean.getAnswer().equals("Y") ? true : false);                   
                   } else if (orgYNQBean.getQuestionId().equals("H6")) {
                      orgAssurances.setGeneralCertificationQuestion(orgYNQBean.getAnswer().equals("Y") ? true : false); 
                   }
                } 
          }
      
            
         return orgAssurances;
    }
    
    
}
