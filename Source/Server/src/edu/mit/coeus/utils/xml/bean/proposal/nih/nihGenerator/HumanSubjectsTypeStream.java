/*
 * HumanSubjectsTypeStream.java
 * Created on March 10
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */
import java.util.* ;



import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.DateUtils;

public class HumanSubjectsTypeStream {
    
    ObjectFactory objFactory ;
 
    HumanSubjectsType humanSubjType;
    ProposalYNQBean proposalYNQBean;
    boolean answer;
    String  strAnswer, questionId ;
   
    /** Creates a new instance of HumanSubjectsTypeStream */
    public HumanSubjectsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
    }
    
   
    public HumanSubjectsType getHumanSubjInfo(ProposalDevelopmentFormBean proposalBean,
                                              OrganizationMaintenanceFormBean orgBean )
                                   throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        humanSubjType = objFactory.createHumanSubject();
      
        // clinical trial question will come from activity type
        // set phase 3 clinical trial from Y/N question
        humanSubjType.setPhase3ClinicalTrialQuestion(false);  //initialize
        Vector vecYNQ = proposalBean.getPropYNQAnswerList();
        if (vecYNQ != null) {    
              for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                proposalYNQBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                questionId = proposalYNQBean.getQuestionId();
                strAnswer = proposalYNQBean.getAnswer();
                answer = (strAnswer.equals("Y") ? true : false);
                if (questionId.equals( "17")){
                    humanSubjType.setPhase3ClinicalTrialQuestion(answer);                    
                }
              } // end for  
        } 
              
        
        Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
        ProposalSpecialReviewFormBean specialReviewBean;
        // If it is exempt, get exemption number from comment.  
        // If there is more than one special review, if any are NOT exempt, then use first row
        // because schema and form only allow one. 
        humanSubjType.setHumanSubjectsUsedQuestion(false);
        boolean exemptFlag = false;
        int approvalCode;
        String exemptionNumber = "";
      
        if (vecSpecialReview != null) {    
             
              for (int vecCount = 0 ; vecCount < vecSpecialReview.size() ; vecCount++) {
                  specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);
                  if (specialReviewBean.getSpecialReviewCode() == 1) {
                      humanSubjType.setHumanSubjectsUsedQuestion(true);
                      approvalCode = specialReviewBean.getApprovalCode();
                      if (approvalCode != 4) { 
                          //not exempt
                          exemptFlag = false;
                          if (specialReviewBean.getApprovalDate() != null ) {
                                 humanSubjType.setIRBApprovalDate
                                  (convertDateStringToCalendar(specialReviewBean.getApprovalDate().toString() ));
                          }
                          break;
                      } else  if (exemptFlag == false ) {
                              exemptFlag = true;
                              exemptionNumber = specialReviewBean.getComments() == null ? "Unknown": 
                                                specialReviewBean.getComments();
                        //      humanSubjType.getExemptionNumber().add(exemptionNumber);
                      }
                  }  
             }   
         }
        /* start change 7-21-08*/
//        if (exemptFlag == false) {
//            humanSubjType.setAssuranceNumber(orgBean.getHumanSubAssurance());
//        }else {
//            humanSubjType.getExemptionNumber().add(exemptionNumber);
//        }
         if (humanSubjType.isHumanSubjectsUsedQuestion() == true) {
             if (exemptFlag == false) {
                if (orgBean.getHumanSubAssurance() != null)
                    humanSubjType.setAssuranceNumber(orgBean.getHumanSubAssurance());
             } else {
                    humanSubjType.getExemptionNumber().add(exemptionNumber);
             }
         }
         /* end change 7-21-08*/
        return humanSubjType;
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
