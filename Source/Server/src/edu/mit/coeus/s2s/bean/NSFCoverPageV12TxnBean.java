/*
 * NSFCoverPageV12TxnBean.java 
 */

package edu.mit.coeus.s2s.bean;


import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  jenlu
 */
public class NSFCoverPageV12TxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    String propNumber;
    
    /** Creates a new instance of SF424V2TxnBean */
    public NSFCoverPageV12TxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
   
    
    public HashMap getS2sOpportunity (String propNumber)
        throws CoeusException, DBException {
     
        if(propNumber==null) 
                throw new CoeusXMLException("Proposal Number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sSF424V2Pkg.getS2sOpportunity( <<PROPOSAL_NUMBER>> , "
                                + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
    }
 //add for nsfCoverPageV13- coeusdev-370
 /** getQuestionnaireAnswers
 *
 */
 public CoeusVector getQuestionnaireAnswers(String proposalNumber, int questionnaireID, int module_code)
     throws CoeusException , DBException{


         CoeusVector cvQuestions = new CoeusVector();

         Vector param = new Vector();

         param.addElement( new Parameter("MODULE_ITEM_KEY",
               DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement(new Parameter("QUESTIONNAIRE_ID",
               DBEngineConstants.TYPE_INT, ( Integer.toString(questionnaireID))));
         param.addElement(new Parameter("MODULE_ITEM_CODE",
               DBEngineConstants.TYPE_INT, ( Integer.toString(module_code))));
         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call  get_ans_for_a_questionnaire" +
                    " ( <<MODULE_ITEM_KEY>> , <<QUESTIONNAIRE_ID>> , <<MODULE_ITEM_CODE>>, " +
                    " <<OUT RESULTSET rset>> )",  "Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int vecSize = result.size();

        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
               row = (HashMap)result.elementAt(i);
               cvQuestions.add(row);
            }
        }

        return cvQuestions;


 }

}
