package edu.mit.coeus.s2s.bean;
import java.util.* ;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.s2s.generator.stream.bean.ExAttQueryParams;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.lang.Integer;
import java.math.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
  
public class S2SPHS398ChecklistTxnBean 

{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    String propNumber;
    int    version;
    BigDecimal bdZero = new BigDecimal("0");

    
    /** Creates a new instance of S2SPHS398ChecklistTxnBean */
    public S2SPHS398ChecklistTxnBean()
    {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
       
    }
    
 
          
  
  public HashMap getChecklistInfo (String propNumber)
     throws CoeusException, DBException{
         
     
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
           "call s2sPHS398ChecklistPkg.get_checklist_info( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){        
         for (int i=0; i<listSize; i++){
            row = (HashMap)result.elementAt(i);
     
         }
        }

   return row;
 
 }
    
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
         
 public HashMap getProgramIncome (String propNumber,int version, int period)
     throws CoeusException, DBException{
         
     
     if(propNumber==null) 
         throw new CoeusXMLException("Proposal Number is Null");   
   
    Vector result = null;
    Vector param= new Vector();
    
    HashMap row = null;
    param.addElement(new Parameter("PROPOSAL_NUMBER",
                   DBEngineConstants.TYPE_STRING,propNumber));
    param.addElement(new Parameter("VERSION_NUMBER",
                   DBEngineConstants.TYPE_INT, Integer.toString(version)));
    param.addElement(new Parameter("BUDGET_PERIOD",
                   DBEngineConstants.TYPE_INT, Integer.toString(period)));
                    
    if(dbEngine !=null){
       result = new Vector(3,2);
       result = dbEngine.executeRequest("Coeus",
           "call s2sPHS398ChecklistPkg.get_program_income( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>," +
                            " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){        
         for (int i=0; i<listSize; i++){
            row = (HashMap)result.elementAt(i);
     
         }
        }

   return row;
 
 }
    
//QUESTIONNAIRE ENHANCEMENT STARTS
 
 public Vector getChecklistQuestionnaire (String propNumber)
     throws CoeusException, DBException{
         
     
     if(propNumber==null) 
         throw new CoeusXMLException("Proposal Number is Null");   
   
    Vector result = null;
    Vector param= new Vector();
    
    param.addElement(new Parameter("PROPOSAL_NUMBER",
                   DBEngineConstants.TYPE_STRING,propNumber));
    if(dbEngine !=null){
       result = new Vector(3,2);
       result = dbEngine.executeRequest("Coeus",
           "call s2sPHS398ChecklistPkg.get_checklist_questionnaires( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       

   return result;
 
 }
 
 
  public String getRolodexName(int rolodexId)
    throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();

        String rolodexName = null;
        param.addElement(new Parameter("ROLODEX_ID",
                DBEngineConstants.TYPE_INT,new Integer(rolodexId).toString()));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_name_from_rolodex( << ROLODEX_ID >> , << OUT STRING ROLODEX_NAME >> ) ",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowHome = (HashMap) result.elementAt(0);
            if ( rowHome.get("ROLODEX_NAME") != null){
                rolodexName = rowHome.get("ROLODEX_NAME").toString();
            }
        }
        return rolodexName;
    }
//QUESTIONNAIRE ENHANCEMENT ENDS
}
       