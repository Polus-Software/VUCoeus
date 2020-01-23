/*
 * FundingOpportunityDetailsStream.java
 *
 * Created on March 10, 2004, 
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */
import java.util.* ;


import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;


public class FundingOpportunityDetailsStream {
    
    ObjectFactory objFactory ;
    FundingOpportunityDetailsType fundingOpportunityType;
   
    /** Creates a new instance of FundingOpportunityDetailsStream */
    public FundingOpportunityDetailsStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
    }
    
   
    public FundingOpportunityDetailsType getFundingOppInfo(ProposalDevelopmentFormBean propDevFormBean) 
           throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
       
        boolean response;
        
        fundingOpportunityType = objFactory.createFundingOpportunityDetailsType();
         
       //  response = (propDevFormBean.getNoticeOfOpportunitycode() == 1 ? true : false);
        if (propDevFormBean.getProgramAnnouncementNumber() == null ){
            response = false;
        }else {
            response = true;
        }
         fundingOpportunityType.setFundingOpportunityResponseCode(response);
         fundingOpportunityType.setFundingOpportunityTitle
            ((propDevFormBean.getProgramAnnouncementTitle() == null ? " " : propDevFormBean.getProgramAnnouncementTitle()));
         fundingOpportunityType.setFundingOpportunityNumber
            ((propDevFormBean.getProgramAnnouncementNumber() == null ? " " : propDevFormBean.getProgramAnnouncementNumber()));
         return fundingOpportunityType;
    }
    
    
    
}
