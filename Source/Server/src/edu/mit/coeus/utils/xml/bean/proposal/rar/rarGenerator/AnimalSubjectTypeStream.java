/*
 * AnimalSubjectTypeStream.java
 ********************* NOT IMPLEMENTED YET
 * Created on March 10,2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import edu.mit.coeus.utils.DateUtils;
import java.util.* ;

public class AnimalSubjectTypeStream {
    
    ObjectFactory objFactory ;
    AnimalSubjectType animalSubjectType;
 
    /** Creates a new instance of AnimalSubjectTypeStream */
    public AnimalSubjectTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public AnimalSubjectType getAnimalSubjInfo(ProposalDevelopmentFormBean proposalBean,
         OrganizationMaintenanceFormBean orgBean)
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
         ProposalSpecialReviewFormBean specialReviewBean;
         animalSubjectType = objFactory.createAnimalSubject();
         animalSubjectType.setVertebrateAnimalsUsedQuestion(false);
         
        //if there is more than one animal protocol, using first one for now.
          if (vecSpecialReview != null) {    
              for (int vecCount = 0 ; vecCount < vecSpecialReview.size() ; vecCount++) {
                  specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);
                  if (specialReviewBean.getSpecialReviewCode() == 2) {
                    animalSubjectType.setVertebrateAnimalsUsedQuestion(true);
                   
                    animalSubjectType.setAssuranceNumber(orgBean.getAnimalWelfareAssurance());
                    //schema allows for either an approval date or "pending"
                    if (specialReviewBean.getApprovalDate() != null) {
                              animalSubjectType.setIACUCApprovalDate(convertDateStringToCalendar
                                               (specialReviewBean.getApprovalDate().toString()));
                    } else {
             
                        animalSubjectType.setIACUCApprovalPending("Pending");
                    }
          
                     
                    break;
                  }//end if
              }//end for
          }
       return animalSubjectType;
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
