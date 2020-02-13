/*
 * AssuranceTypeStream.java
 *
 * Created on MAr 3, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator;

import java.util.* ;



import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.* ; 

 
  
public class AssuranceTypeStream
{
    ObjectFactory objFactory ;
    AssuranceType assuranceType;
    OrganizationYNQBean orgYNQBean;
   
   
  
    public AssuranceTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
    }
    
 
    public AssuranceType getAssuranceTypeInfo( OrganizationYNQBean[] arrayYNQ ,
        String questionId) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  

        assuranceType = objFactory.createAssuranceType();  // always use Createxxx not createxxxType method - createxxxType
                                                           // method will not add <xxx> tag to the o/p       
        
        //arrayYNQ is an array of organizationYNQBeans
        //we need to search the array for a specific question
        
         if (arrayYNQ != null) {
               
                for (int i = 0; i < arrayYNQ.length  ; i++) {
                   orgYNQBean = (OrganizationYNQBean) arrayYNQ[i];
                   if (orgYNQBean.getQuestionId().equals(questionId)) {
                       assuranceType.setYesNoAnswer((orgYNQBean.getAnswer().equals("Y") ? true : false));
                       assuranceType.setExplanation(orgYNQBean.getExplanation()== null ? " " : orgYNQBean.getExplanation());
                   }
                   
                } //end for
          }
  
       return assuranceType;
    }
  
   
}
