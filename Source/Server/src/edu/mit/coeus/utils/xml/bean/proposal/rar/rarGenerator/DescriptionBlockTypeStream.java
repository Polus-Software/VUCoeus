/*
 * DescriptionBlockTypeStream.java
 *
 * Created on April 8, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
/**
 *
 * @author  ele
 */


import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.bean.*;
import edu.mit.coeus.utils.xml.bean.proposal.rar.*;


public class DescriptionBlockTypeStream {
    
    ObjectFactory objFactory ;
    DescriptionBlockType descriptionBlock;
    String  proposalNumber;
    
    /** Creates a new instance of DescriptionBlockTypeStream */
    public DescriptionBlockTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
   
    public DescriptionBlockType getDescriptionBlock(Object obj,
            String blockType,  boolean isFile)
       throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
        descriptionBlock = objFactory.createDescriptionBlockType();
         System.out.println("in descrBlockType ... before if obj");
        if ( obj instanceof ProposalDevelopmentFormBean) {
             System.out.println("..it is a prop dev bean");
             ProposalDevelopmentFormBean proposalBean = (ProposalDevelopmentFormBean)obj;        
             proposalNumber = proposalBean.getProposalNumber();
        }else if (obj instanceof String) {
            System.out.println("..it is a string");
            proposalNumber  = (String)obj;
          
        }else {
             System.out.println("..in else condition");
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean) obj;
            proposalNumber = budgetInfoBean.getProposalNumber();
        }
                 
        
        if (isFile) {
            System.out.println("is file is true");
             //hard coding fileid as prop number now
             descriptionBlock.setFileIdentifier(proposalNumber + blockType);
             System.out.println("after set file id ");
         } else {
             //get the text
              //hardcoding blockType now. need to get the abstract text
              descriptionBlock.setText(blockType);
              System.out.println("after set text");
         }
          
         return descriptionBlock;
    }
    
    
}
