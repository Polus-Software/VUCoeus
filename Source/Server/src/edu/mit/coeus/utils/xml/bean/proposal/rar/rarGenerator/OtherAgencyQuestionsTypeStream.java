/*
 * OtherAgencyQuestionsTypeStream.java
 *
 * Created on March 2, 2004, 6:07 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import java.util.Vector;

public class OtherAgencyQuestionsTypeStream {
    
    ObjectFactory objFactory ;
    OtherAgencyQuestionsType otherAgencyQuestionsType;
    ProposalYNQBean proposalYNQBean;
    
    /** Creates a new instance of OtherAgenciesQuestionsTypeStream */
    public OtherAgencyQuestionsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
      public OtherAgencyQuestionsType getOtherAgencyQuestions(ProposalDevelopmentFormBean proposalBean) 
         throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
       boolean answer;
       String  strAnswer;
       String questionId ;
       String explanation;
       
       otherAgencyQuestionsType = objFactory.createOtherAgencyQuestionsType();
       otherAgencyQuestionsType.setOtherAgencyIndicator(false); //init
       
       Vector vecYNQ = proposalBean.getPropYNQAnswerList();
       if (vecYNQ != null) {  
          
           for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
           
              proposalYNQBean = (ProposalYNQBean) vecYNQ.get(vecCount);
              questionId = proposalYNQBean.getQuestionId();
              strAnswer = proposalYNQBean.getAnswer();
              explanation = (proposalYNQBean.getExplanation()== null ? " " : proposalYNQBean.getExplanation());
              answer = (strAnswer.equals("Y") ? true : false);
       
              if (questionId.equals( "15")) { 
                  otherAgencyQuestionsType.setOtherAgencyIndicator(answer);
                  otherAgencyQuestionsType.setOtherAgencyNames(explanation);
                  break;
              }
           }
       }
        
     
        return otherAgencyQuestionsType;
      }
}
