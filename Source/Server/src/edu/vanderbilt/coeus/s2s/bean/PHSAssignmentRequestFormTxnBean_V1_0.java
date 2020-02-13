/*
 * PHSAssignmentRequestFormTxnBean_V1_0.java
 *
 */

package edu.vanderbilt.coeus.s2s.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;


public class PHSAssignmentRequestFormTxnBean_V1_0 {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
    String propNumber;
    
    /** Creates a new instance of PHS398CoverPageSupplementTxnBean */
    public PHSAssignmentRequestFormTxnBean_V1_0() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }

    /* Get funding opportunity information */
    public OpportunityInfoBean getFundingOpportunity(String proposalNumber)
    	throws CoeusException, DBException {

         OpportunityInfoBean bean = new OpportunityInfoBean();

         Vector param = new Vector();
         HashMap row = null;
         Vector result = new Vector(3,2);

         param.addElement(new Parameter("PROPOSAL_NUMBER",
                 DBEngineConstants.TYPE_STRING,proposalNumber));
         
         if (dbEngine != null){
             result = dbEngine.executeRequest("Coeus",
                     "call S2S_FORMSET_D.getFundingOpportunity( <<PROPOSAL_NUMBER>> , "+
                     "<<OUT RESULTSET rset>> )", "Coeus", param);
         }
         else{
             throw new CoeusException("db_exceptionCode.1000");
         }

         if (result.size() > 0) {
       		 row = (HashMap) result.elementAt(0);
        		 
       		 bean = new OpportunityInfoBean();
       		 bean.setOpportunityId((String) row.get("OPPORTUNITY_ID"));
       		 bean.setOpportunityTitle((String) row.get("OPPORTUNITY_TITLE"));

         }

         return bean;
    }
    
    /* Get questionnaire answers */
    public CoeusVector getQuestionnaireAnswers(String proposalNumber)
    	throws CoeusException, DBException {

         CoeusVector cvQuestions = new CoeusVector();
         QuestionAnswerBean bean = new QuestionAnswerBean();

         Vector param = new Vector();
         HashMap row = null;
         Vector result = new Vector(3,2);

         param.addElement(new Parameter("PROPOSAL_NUMBER",
                 DBEngineConstants.TYPE_STRING,proposalNumber));
         
         if (dbEngine != null){
             result = dbEngine.executeRequest("Coeus",
                     "call S2S_FORMSET_D.getAssignmentRequestFormV1_0( <<PROPOSAL_NUMBER>> , "+
                     "<<OUT RESULTSET rset>> )", "Coeus", param);
         }
         else{
             throw new CoeusException("db_exceptionCode.1000");
         }

         if (result.size()> 0) {
        	 for(int i=0; i < result.size(); i++) {
        		 row = (HashMap) result.elementAt(i);
        		 
        		 bean = new QuestionAnswerBean();
        		 bean.setQuestionnaireCompletionId((String) row.get("QUESTIONNAIRE_COMPLETION_ID"));
        		 bean.setQuestionId(row.get("QUESTION_ID") == null ? null :
                     new Integer(row.get("QUESTION_ID").toString()));
        		 bean.setQuestionNumber(row.get("QUESTION_NUMBER") == null ? null :
                     new Integer(row.get("QUESTION_NUMBER").toString()));
        		 bean.setAnswerNumber(row.get("ANSWER_NUMBER") == null ? null :
                     new Integer(row.get("ANSWER_NUMBER").toString()));
        		 bean.setAnswer((String) row.get("ANSWER"));
        		 bean.setQuestionnaireVersionNumber(row.get("QUESTION_VERSION_NUMBER") == null ? null :
                     new Integer(row.get("QUESTION_VERSION_NUMBER").toString()));
        		 
        		 cvQuestions.add(bean);
        	 }
         }

         return cvQuestions;
    }
}
