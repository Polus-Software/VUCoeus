/*
 * NSFAppChecklistTxnBean.java
 */

package edu.mit.coeus.s2s.bean;


import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;


public class NSFAppChecklistTxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    String propNumber;
    
    /** Creates a new instance of NSFAppChecklistTxnBean */
    public NSFAppChecklistTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
   
    

/** getQuestionnaireAnswers
 *
 */
 public CoeusVector getQuestionnaireAnswers(String proposalNumber)
     throws CoeusException , DBException{

         int moduleItemCode = 3;
         int subModuleItemCode = 0;
         String moduleSubItemKey = "0";
         int questionId = 0;
         int answerNumber=0;
         String answer = null;
         String questionnaireCompletionId = null;
         String description = null;

         HashMap hmQuestions = null;
         CoeusVector cvQuestions = new CoeusVector();

         Vector param = new Vector();

         param.addElement( new Parameter("MODULE_ITEM_KEY",
               DBEngineConstants.TYPE_STRING, proposalNumber));


         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call  s2sNSFChecklistPkg.get_questionnaire_answers" +
                                     " ( <<MODULE_ITEM_KEY>> ," +
                    " <<OUT RESULTSET rset>> )",  "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int vecSize = result.size();
        hmQuestions = new HashMap();

        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
               row = (HashMap)result.elementAt(i);
               cvQuestions.add(row);
            }
        }

        return cvQuestions;


 }

  public String getNSFChecklistAnswer(String propNumber, int questionId)
                                            throws DBException, CoeusException{
       Vector param = new Vector();
       String answer = "Yes";
       Vector result = new Vector();
       HashMap row = null;
       param.addElement(new Parameter("ANSWER",
              DBEngineConstants.TYPE_STRING, answer, DBEngineConstants.DIRECTION_OUT));
       param.addElement( new Parameter("AS_PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement( new Parameter("AS_QUESTION_ID",
          DBEngineConstants.TYPE_INT, Integer.toString(questionId)));
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING ANSWER>> = call fn_get_nsf_checklist_answer( " +
              " <<AS_PROPOSAL_NUMBER>>, <<AS_QUESTION_ID>> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          answer = (String)row.get("ANSWER");
        }
         return answer;

   }

}
