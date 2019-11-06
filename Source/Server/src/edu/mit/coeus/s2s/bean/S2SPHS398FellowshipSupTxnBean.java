package edu.mit.coeus.s2s.bean;
import java.util.* ;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.math.*;

  
public class S2SPHS398FellowshipSupTxnBean 

{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    String propNumber;
    int    version;
    BigDecimal bdZero = new BigDecimal("0");

    
    /** Creates a new instance of S2SPHS398ChecklistTxnBean */
    public S2SPHS398FellowshipSupTxnBean()
    {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
       
    }
   
/** getApplicationType
*/

public HashMap getApplicationType (String propNumber)
     throws CoeusException, DBException{
         
     HashMap hmInfo = new HashMap();
     
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
           "call s2sPHS398ChecklistPkg.get_application_type( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

   return row;
 
 }

/** getAlternatePhone
 */
 public HashMap getAdditionalInfo(String propNumber)
     throws CoeusException , DBException{
     HashMap hmInfo = new HashMap();

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
           "call s2sphsFellowshipPkg.get_additional_info( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

   return row;

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
            "call  s2sphsFellowshipPkg.get_questionnaire_answers" +
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

  //get citizenship info of PI
             //"U.S. Citizen or noncitizen national";
            //"Permanent Resident of U.S."
            //"Non-U.S. Citizen with temporary visa"
       public HashMap getCitizenship (String propNumber)
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
               "call s2sphsFellowshipPkg.getCitizenship( <<PROPOSAL_NUMBER>> , "
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



public HashMap getBudgetInfo(String proposalNumber)
     throws CoeusException , DBException{

         Vector param = new Vector();

         param.addElement( new Parameter("PROPOSAL_NUMBER",
               DBEngineConstants.TYPE_STRING, proposalNumber));

         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call s2sphsFellowshipPkg.GET_BUDGET_INFO( <<PROPOSAL_NUMBER>> ," +
                    " <<OUT RESULTSET rset>> )","Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

       return row;


 }


  
}
       