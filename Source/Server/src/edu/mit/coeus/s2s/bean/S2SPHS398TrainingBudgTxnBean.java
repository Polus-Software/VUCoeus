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

  
public class S2SPHS398TrainingBudgTxnBean 

{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    String propNumber;
    int    version;
    BigDecimal bdZero = new BigDecimal("0");

   
    
    /** Creates a new instance of S2SPHS398ChecklistTxnBean */
    public S2SPHS398TrainingBudgTxnBean()
    {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
       
    }
   
/** getTrainingBudgInfo
*/

public HashMap getTrainingBudgInfo (String propNumber)
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
           "call s2sphsTrainingBudgPkg.get_training_budg_info( <<PROPOSAL_NUMBER>> , "+
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

 
 /***************************************************************
  * getPostDocAnswers
  * this gets postDoc questionnaire answers for one period
  * postDoctype values : FN, SN, FD, SD
  *  (full term non degree, short term non degree, full term degree, short term degree)
 ******************************************************************/
 public CoeusVector getPostDocAnswers(String proposalNumber, String postDoctype, int period)
     throws CoeusException , DBException{

         HashMap hmQuestions = null;
         CoeusVector cvPostDocAnswers = new CoeusVector();

         Vector param = new Vector();

         param.addElement( new Parameter("MODULE_ITEM_KEY",
               DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement( new Parameter("POSTDOCTYPE",
               DBEngineConstants.TYPE_STRING, postDoctype));
          param.addElement(new Parameter("BUDGET_PERIOD",
               DBEngineConstants.TYPE_INT, Integer.toString(period)));

         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call  s2sphsTrainingBudgPkg.get_postdoc_answers" +
                                     " ( <<MODULE_ITEM_KEY>> , <<POSTDOCTYPE>>, <<BUDGET_PERIOD>>," +
                    " <<OUT RESULTSET rset>> )",  "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int vecSize = result.size();
        hmQuestions = new HashMap();

        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
               row = (HashMap)result.elementAt(i);
               cvPostDocAnswers.add(row);
            }
        }

        return cvPostDocAnswers;


 }

  /***************************************************************
  * getPreDocAnswers
  * this gets preDoc and undergrad questionnaire answers for one period
 ******************************************************************/
 public CoeusVector getPreDocAnswers(String proposalNumber, int period)
     throws CoeusException , DBException{

        
         String description = null;

         HashMap hmQuestions = null;
         CoeusVector cvPreDocAnswers = new CoeusVector();

         Vector param = new Vector();

         param.addElement( new Parameter("MODULE_ITEM_KEY",
               DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement(new Parameter("BUDGET_PERIOD",
               DBEngineConstants.TYPE_INT, Integer.toString(period)));

         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call  s2sphsTrainingBudgPkg.get_predoc_answers" +
                                     " ( <<MODULE_ITEM_KEY>> , <<BUDGET_PERIOD>>, " +
                    " <<OUT RESULTSET rset>> )",  "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int vecSize = result.size();
        hmQuestions = new HashMap();

        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
               row = (HashMap)result.elementAt(i);
               cvPreDocAnswers.add(row);
            }
        }

        return cvPreDocAnswers;


 }



 public CoeusVector getIndirectCosts(String proposalNumber,int version_number, int budget_period)
     throws CoeusException , DBException{

         Vector param = new Vector();

         param.addElement( new Parameter("PROPOSAL_NUMBER",
               DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement(new Parameter("BUDGET_PERIOD",
               DBEngineConstants.TYPE_INT, Integer.toString(budget_period)));
          param.addElement(new Parameter("VERSION_NUMBER",
               DBEngineConstants.TYPE_INT, Integer.toString(version_number)));
         HashMap row = null;
         Vector result = new Vector();

         HashMap hmIndirectCosts = null;
         CoeusVector cvIndirectCosts = new CoeusVector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call pkg_oh_rate_and_base.get_oh_rate_and_base_for_per( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, <<BUDGET_PERIOD>>," +
                    " <<OUT RESULTSET rset>> )","Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int vecSize = result.size();
        hmIndirectCosts = new HashMap();

        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
               row = (HashMap)result.elementAt(i);
               cvIndirectCosts.add(row);
            }
        }

        return cvIndirectCosts;

 }




public HashMap getBudgetInfo(String proposalNumber, int budget_period)
     throws CoeusException , DBException{

         Vector param = new Vector();

         param.addElement( new Parameter("PROPOSAL_NUMBER",
               DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement(new Parameter("BUDGET_PERIOD",
               DBEngineConstants.TYPE_INT, Integer.toString(budget_period)));
         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call s2sphsTrainingBudgPkg.get_budget_info( <<PROPOSAL_NUMBER>> , <<BUDGET_PERIOD>>," +
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

/*
public int getBudgetVersion (String proposalNumber)
     throws CoeusException , DBException{

     int versionNumber = 0;
     Vector param = new Vector();
     param.addElement( new Parameter("PROPOSAL_NUMBER",
               DBEngineConstants.TYPE_STRING, proposalNumber));

      HashMap row = null;
      Vector result = new Vector();

      if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER VERSION_NUMBER>> = call fn_get_version(  <<PROPOSAL_NUMBER>>) }", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          versionNumber = Integer.parseInt(row.get("VERSION_NUMBER").toString());
        }
       return versionNumber;
    }

*/


public int getStipendAmount (String level, int experience,int numPeople, String propNumber)
     throws CoeusException , DBException{
         int stipendAmount = 0;

         Vector param = new Vector();

         param.addElement(new Parameter("STIPEND_AMOUNT",
          DBEngineConstants.TYPE_INT, Integer.toString(stipendAmount),
          DBEngineConstants.DIRECTION_OUT));

         param.addElement( new Parameter("CAREER_LEVEL",
               DBEngineConstants.TYPE_STRING, level));

        param.addElement(new Parameter("EXPERIENCE_LEVEL",
               DBEngineConstants.TYPE_INT, Integer.toString(experience)));

         param.addElement(new Parameter("NUM_PEOPLE",
               DBEngineConstants.TYPE_INT, Integer.toString(numPeople)));

          param.addElement( new Parameter("PROPOSAL_NUMBER",
               DBEngineConstants.TYPE_STRING, propNumber));

         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER STIPEND_AMOUNT>> = call s2sphsTrainingBudgPkg.fn_calc_stipend( <<CAREER_LEVEL>>," +
            " <<EXPERIENCE_LEVEL>>, <<NUM_PEOPLE>> , <<PROPOSAL_NUMBER>>) }", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          stipendAmount = Integer.parseInt(row.get("STIPEND_AMOUNT").toString());
        }
       return stipendAmount;
    }
  
}
       