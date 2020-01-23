/*
 * ProposalPersonTypeStream.java
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.ContactInfoTypeStream;

import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPersonBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.PersonFullNameTypeStream;
import java.util.* ;
//cowusqa-2420 start
import edu.mit.coeus.propdev.bean.* ;
//cowusqa-2420 end
public class ProposalPersonTypeStream {
    
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
 
    ProposalPersonType proposalPersonType;
 
    /** Creates a new instance of ProposalPersonTypeStream */
    public ProposalPersonTypeStream(ObjectFactory objFactory,
                edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory)  
    {
        this.objFactory = objFactory ;
        this.commonObjFactory = commonObjFactory;
    }

  //cowusqa-2420 start
  //public ProposalPersonType getProposalPerson(ProposalPersonBean proposalPersonBean)
  public ProposalPersonType getProposalPerson(String proposalNumber, ProposalPersonBean proposalPersonBean)
  //cowusqa-2420 end
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        
         proposalPersonType = objFactory.createProposalPersonType();
         
         PersonFullNameTypeStream personFullNameTypeStream
            = new PersonFullNameTypeStream(objFactory);    
        
         proposalPersonType.setName(
            personFullNameTypeStream.getPersonFullNameTypeInfo(proposalPersonBean));
        
         proposalPersonType.setDOB(proposalPersonBean.getDob());
         //coeusqa-2420 start
//         proposalPersonType.setDegree(proposalPersonBean.getDegree());
         ProposalPerDegreeFormBean proposalPerDegreeForm = new ProposalPerDegreeFormBean();
        ProposalPersonTxnBean proposalPersonTxnBean;
        proposalPersonTxnBean = new ProposalPersonTxnBean();
        proposalPerDegreeForm =
            proposalPersonTxnBean.getProposalPersonDegreeMax(proposalNumber, proposalPersonBean.getPersonId());
        if(proposalPerDegreeForm != null){
            if (proposalPerDegreeForm.getDegreeDescription() != null)
                proposalPersonType.setDegree(proposalPerDegreeForm.getDegreeDescription());
         }
         //coeusqa-2420 end
         proposalPersonType.setEmail(proposalPersonBean.getEmail());
         proposalPersonType.setFundingMonths(proposalPersonBean.getFundingMonths());
         proposalPersonType.setAcademicFundingMonths(proposalPersonBean.getAcademicFundingMonths());
         proposalPersonType.setSummerFundingMonths(proposalPersonBean.getSummerFundingMonths());
         proposalPersonType.setPercentEffort(proposalPersonBean.getPercentEffort());
         proposalPersonType.setPhone(proposalPersonBean.getPhone());
         proposalPersonType.setProjectRole(proposalPersonBean.getRole());
         proposalPersonType.setRequestedCost(proposalPersonBean.getRequestedCost());
         proposalPersonType.setSSN(proposalPersonBean.getSsn());

         //cowusqa-2420 start
         ProposalDevelopmentTxnBean proposalDevTxnBean ;
         ProposalPersonFormBean propPersonBean;
         proposalDevTxnBean = new ProposalDevelopmentTxnBean() ;
         propPersonBean = proposalDevTxnBean.getProposalPersonDetails(proposalNumber,proposalPersonBean.getPersonId());
         if(propPersonBean.getEraCommonsUsrName() != null)
         proposalPersonType.setAccountIdentifier(propPersonBean.getEraCommonsUsrName());
         //cowusqa-2420 end
         
       return proposalPersonType;
    }
    
      

    public Calendar convertDateStringToCalendar(String dateStr)
    {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null)
        {    
            if (dateStr.indexOf('-')!= -1)
            { // if the format obtd is YYYY-MM-DD
              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }    
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                        Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
     }   
}
