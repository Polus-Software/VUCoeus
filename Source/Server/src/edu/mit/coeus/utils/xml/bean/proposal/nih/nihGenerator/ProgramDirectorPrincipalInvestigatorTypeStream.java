/*
 * ProgramDirectorPrincipalInvestigatorTypeStream.java
 *
 * Created on March 2, 2004, 4:48 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.question.bean.YNQBean;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.ContactInfoTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.PersonFullNameTypeStream;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import java.util.Vector;

public class ProgramDirectorPrincipalInvestigatorTypeStream {
    
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    
    ProgramDirectorPrincipalInvestigatorType principalInvestigatorType;
    
    ProposalDevelopmentTxnBean proposalDevTxnBean ;
    ProposalInvestigatorFormBean pIBean; 
    ProposalPersonFormBean pIPersonBean;
   
    public ProgramDirectorPrincipalInvestigatorTypeStream(ObjectFactory objFactory,
                 edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory,
                 edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory) 
    {
        this.objFactory = objFactory ;  
        this.commonObjFactory = commonObjFactory;
        this.rarObjFactory = rarObjFactory;
        
        proposalDevTxnBean = new ProposalDevelopmentTxnBean() ;
    }
    
       public ProgramDirectorPrincipalInvestigatorType getPIInfo(String propNumber,
                                       RolodexDetailsBean orgRolodexBean ,
                                       ProposalDevelopmentFormBean propDevFormBean )
         throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    //PI details
         pIBean = getPI(propNumber);   
        
         //PI person details
         if (pIBean.getPersonId().equals("XXXXXXXXX")) {
           pIPersonBean = new ProposalPersonFormBean();
           pIPersonBean.setPersonId("XXXXXXXXX");
           pIPersonBean.setFirstName("Unknown");
           pIPersonBean.setLastName("Unknown");
           pIPersonBean.setOfficeLocation("Unknown");
           pIPersonBean.setOfficePhone("Unknown");
           
         }else {
           pIPersonBean = proposalDevTxnBean.getProposalPersonDetails(propNumber,pIBean.getPersonId());
         }
         
         principalInvestigatorType = objFactory.createProgramDirectorPrincipalInvestigator();
         
         /** account id
          */     
         //pi account identifier - changed jan 27,2006 - used to be person id, now is era commonsname
 //        principalInvestigatorType.setAccountIdentifier(pIBean.getPersonId());
         if (pIPersonBean.getEraCommonsUsrName() == null) {
             principalInvestigatorType.setAccountIdentifier("Unknown");
         }else {
             principalInvestigatorType.setAccountIdentifier(pIPersonBean.getEraCommonsUsrName());
         }
         /** new invest question
          */
         principalInvestigatorType.setNewInvestigatorQuestion(getNewInvestQuestion(propDevFormBean));
         
         /** contact info
          */
         ContactInfoTypeStream contactInfoTypeStream
            = new ContactInfoTypeStream(commonObjFactory);     
         principalInvestigatorType.setContactInformation(
                 contactInfoTypeStream.getContactInfo(pIPersonBean,orgRolodexBean ));
       
         /** name
          */
         PersonFullNameTypeStream personFullNameTypeStream
            = new PersonFullNameTypeStream(rarObjFactory);      
         principalInvestigatorType.setName(personFullNameTypeStream.getPersonFullNameTypeInfo(pIPersonBean  ));
        
         /** signature
          */
         SignatureTypeStream signatureStream = new SignatureTypeStream(objFactory);
         principalInvestigatorType.setDirectorInvestigatorSignature(signatureStream.getSignatureInfo(pIPersonBean));
          
         return principalInvestigatorType;
    }

     /**
     * get PI info
     */
    private ProposalInvestigatorFormBean getPI(String propNumber)throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        Vector vecInvestigators;
        vecInvestigators = proposalDevTxnBean.getProposalInvestigatorDetails(propNumber);
        
        if (vecInvestigators != null)
        {    
            for (int vecCount = 0 ; vecCount < vecInvestigators.size() ; vecCount++)
               {
                   pIBean = (ProposalInvestigatorFormBean) vecInvestigators.get(vecCount) ;
                   if (pIBean.isPrincipleInvestigatorFlag()) {
                       return pIBean;
                   } else {
                     //need to indicate no pi somehow   
                   }
                                          
               }   
        }  
        else //no investigators
        {
               pIBean = new ProposalInvestigatorFormBean();
               pIBean.setPersonId("XXXXXXXXX");
               
               
        }    
        return pIBean;
    }
    
    
    
     
     private boolean getNewInvestQuestion(ProposalDevelopmentFormBean propDevFormBean)
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        //case 1350 - get question from propDevFormBean , not piBean
        Vector vecYNQQuestions = propDevFormBean.getPropYNQAnswerList();
        //vecYNQQuestions contains vector of investigator answers ie. ProposalYNQBean
                 
        ProposalYNQBean ynqBean;
        
        if ( vecYNQQuestions != null)
        {    
            for (int vecCount = 0 ; vecCount < vecYNQQuestions.size() ; vecCount++)
               {
                   ynqBean = (ProposalYNQBean) vecYNQQuestions.get(vecCount) ;
                   
                   if (ynqBean.getQuestionId().equals("13")) { 
                       
     
             
                       if (ynqBean.getAnswer().equals("Y")) {
                       return true;
                       }                                         
                   }   
                }
        }
        else
        {
               System.out.println("** getNewInvestQuestion : vecYNQ is null **") ;
        }    
        return false;
        }  
   }    
   