/*
 * SubmissionChekListStream.java
 *
 * Created on February 13, 2004, 4:35 PM
 */

package edu.mit.coeus.utils.xml.generator;

import java.util.* ;
import java.math.BigInteger;

import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.utils.xml.bean.schedule.ObjectFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.schedule.* ; 
import edu.mit.coeus.utils.DateUtils;


public class SubmissionCheckListStream {
    
    ObjectFactory objFactory ;
    ProtocolSubmissionTxnBean protocolSubmissionTxnBean ;
    
    public SubmissionCheckListStream(ObjectFactory objFactory)
    {
        this.objFactory = objFactory ;
        protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean() ;
    }
    
    
   public SubmissionDetailsType.SubmissionChecklistInfoType getSubmissionCheckList(String protocolNumber, 
                                            int sequenceNumber, int submissionNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException
   {

       SubmissionDetailsType.SubmissionChecklistInfoType submissionChecklistInfo = objFactory.createSubmissionDetailsTypeSubmissionChecklistInfoType() ;
       String formattedCode = new String() ;
       
       Vector vecExemptCheckList = protocolSubmissionTxnBean.getProtocolExemptCheckList(protocolNumber, sequenceNumber, submissionNumber) ;
       if (vecExemptCheckList != null)
       {
            for (int checkCount = 0 ; checkCount < vecExemptCheckList.size() ; checkCount++)
            {
                SubmissionDetailsType.SubmissionChecklistInfoType.ChecklistsType checkListItem = objFactory.createSubmissionDetailsTypeSubmissionChecklistInfoTypeChecklistsType() ;
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean = (ProtocolReviewTypeCheckListBean) vecExemptCheckList.get(checkCount) ;
                checkListItem.setChecklistCode(protocolReviewTypeCheckListBean.getCheckListCode()) ;
                checkListItem.setChecklistDescription(protocolReviewTypeCheckListBean.getDescription()) ;
                formattedCode = formattedCode + "(" + protocolReviewTypeCheckListBean.getCheckListCode()  + ") " ;
                submissionChecklistInfo.getChecklists().add(checkListItem) ;
            }
            
            if (formattedCode.length() > 0)
            {   
               submissionChecklistInfo.setChecklistCodesFormatted( formattedCode ) ; // this will have format (3) (7) like that...
            }
       }    
       
              
       Vector vecExpeditedCheckList = protocolSubmissionTxnBean.getProtocolExpeditedCheckList(protocolNumber, sequenceNumber, submissionNumber) ;
       if (vecExpeditedCheckList != null)
       {
            for (int checkCount = 0 ; checkCount < vecExpeditedCheckList.size() ; checkCount++)
            {
                SubmissionDetailsType.SubmissionChecklistInfoType.ChecklistsType checkListItem = objFactory.createSubmissionDetailsTypeSubmissionChecklistInfoTypeChecklistsType() ;
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean = (ProtocolReviewTypeCheckListBean) vecExpeditedCheckList.get(checkCount) ;
                checkListItem.setChecklistCode(protocolReviewTypeCheckListBean.getCheckListCode()) ;
                checkListItem.setChecklistDescription(protocolReviewTypeCheckListBean.getDescription()) ;
                formattedCode = formattedCode + "(" + protocolReviewTypeCheckListBean.getCheckListCode()  + ") " ;
                submissionChecklistInfo.getChecklists().add(checkListItem) ;
            } 
             
           if (formattedCode.length() > 0)
           {   
               submissionChecklistInfo.setChecklistCodesFormatted( formattedCode ) ; // this will have format (3) (7) like that...
           }    
       }
       
       
       if (vecExemptCheckList == null && vecExpeditedCheckList == null)
       {
            return null ;
       }    
       
       
       
       return submissionChecklistInfo ;
       
       
   }
    
}
