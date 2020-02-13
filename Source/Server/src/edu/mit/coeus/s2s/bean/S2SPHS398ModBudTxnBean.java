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
  
public class S2SPHS398ModBudTxnBean 

{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    String propNumber;
    int    version;
    BigDecimal bdZero = new BigDecimal("0");

    
    /** Creates a new instance of S2SPHS398ModBudTxnBean */
    public S2SPHS398ModBudTxnBean()
    {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
       
    }
    
 
          
  
  public HashMap getModularBudgetInfo (String propNumber)
     throws CoeusException, DBException{
         
     HashMap hmInfo = new HashMap();
     
     if(propNumber==null) 
         throw new CoeusXMLException("Proposal Number is Null");   
   
     int version = getVersion(propNumber);
     int numPeriods = getNumberOfPeriods(propNumber,version);
       
     hmInfo.put("version", Integer.toString(version));
     hmInfo.put("numPeriods", Integer.toString(numPeriods));
     hmInfo.put("FormVersion","1.0");

   return hmInfo;
 
 }

         
  
 
 private int getNumberOfPeriods (String propNumber, int version)
      throws CoeusException, DBException{
     
      if(propNumber==null) 
         throw new CoeusXMLException("Proposal Number is Null");   
   
     int numPeriods = 0;
       
     Vector param = new Vector();
       
     param.addElement(new Parameter("PERIODS", 
          DBEngineConstants.TYPE_INT, Integer.toString(numPeriods), 
          DBEngineConstants.DIRECTION_OUT));
     param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
     param.addElement( new Parameter ("VERSION",
          DBEngineConstants.TYPE_INT, Integer.toString(version)));
        
     HashMap row = null;
     Vector result = new Vector();
                
     if(dbEngine !=null){
        result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER PERIODS>> = call s2sPHS398ModBud.fn_Get_numPeriods" +
            "( <<PROPOSAL_NUMBER>> , <<VERSION>> ) }", param);  
              
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
         numPeriods = Integer.parseInt(row.get("PERIODS").toString());   
        }                
       return numPeriods;
    }
     
  private int getVersion(String propNumber) 
      throws CoeusException, DBException {
      
      //get final version or max version number if nothing is final
                    
     version = 0;
       
     Vector param = new Vector();
       
     param.addElement(new Parameter("VERSION", 
          DBEngineConstants.TYPE_INT, Integer.toString(version), 
          DBEngineConstants.DIRECTION_OUT));
     param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
        
     HashMap row = null;
     Vector result = new Vector();
                
     if(dbEngine !=null){
        result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER VERSION>> = call s2sPHS398ModBud.fn_Get_Version( <<PROPOSAL_NUMBER>> ) }", param);  
         
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
         version = Integer.parseInt(row.get("VERSION").toString());   
        }                
       return version;
    }
     
  /** getDirectCost
   * returns hashMap with DIRECT_COST_LESS_CONSOR_FNA,
                          CONSORTIUM_FNA,
			  TOTAL_DIRECT_COST
   */
  public HashMap getDirectCost(String propNumber,int budgetPeriod, int version)
    throws CoeusException, DBException
  {
      if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");   

     Vector result = null;
     Vector param= new Vector(); 
      
     HashMap row = null;
     param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,propNumber));
     param.addElement(new Parameter("VERSION_NUMBER",
                                      DBEngineConstants.TYPE_INT, Integer.toString(version)));
     param.addElement(new Parameter ("BUDGET_PERIOD", 
                                     DBEngineConstants.TYPE_INT, Integer.toString(budgetPeriod)));
     
     if(dbEngine !=null){
             result = new Vector(3,2);
             result = dbEngine.executeRequest("Coeus",
                  "call s2sPHS398ModBud.get_Direct_Cost(<<PROPOSAL_NUMBER>>, <<VERSION_NUMBER>>,  " +
                            "<<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
     }else{
             throw new CoeusException("db_exceptionCode.1000");
     }
      int listSize = result.size();
      if (listSize > 0){
           
           for (int i=0; i<listSize; i++){
            row = (HashMap)result.elementAt(0);
            
           }
      }
      
      return row;
        
 }

  /** getTotalCost
   * returns hashMap with TOTAL_COST                          
   */
  public HashMap getTotalCost(String propNumber,int budgetPeriod, int version)
    throws CoeusException, DBException
  {
      if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");   

     Vector result = null;
     Vector param= new Vector(); 
      
     HashMap row = null;
     param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,propNumber));
     param.addElement(new Parameter("VERSION_NUMBER",
                                      DBEngineConstants.TYPE_INT, Integer.toString(version)));
     param.addElement(new Parameter ("BUDGET_PERIOD", 
                                     DBEngineConstants.TYPE_INT, Integer.toString(budgetPeriod)));
     
     if(dbEngine !=null){
             result = new Vector(3,2);
             result = dbEngine.executeRequest("Coeus",
                  "call s2sPHS398ModBud.get_total_Cost(<<PROPOSAL_NUMBER>>, <<VERSION_NUMBER>>,  " +
                            "<<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
     }else{
             throw new CoeusException("db_exceptionCode.1000");
     }
      int listSize = result.size();
      if (listSize > 0){
           
           for (int i=0; i<listSize; i++){
            row = (HashMap)result.elementAt(0);
            
           }
      }
      
      return row;
        
 }

  /** getCognizantAgency
   *
   */ 
  public HashMap getCognizantAgency(String propNumber)
    throws CoeusException, DBException
 
  {
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
                  "call s2sPHS398ModBud.get_cognizant_agency(<<PROPOSAL_NUMBER>>,  " +
                            " <<OUT RESULTSET rset>> )", "Coeus", param);
     }else{
             throw new CoeusException("db_exceptionCode.1000");
     }
      int listSize = result.size();
      if (listSize > 0){
           
           for (int i=0; i<listSize; i++){
            row = (HashMap)result.elementAt(0);
            
           }
      }
      
      return row;
        
 }
     
 /** getDates
   *
   */
    public HashMap getDates(String propNumber, int version, int period)
    throws CoeusException, DBException {
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
                  "call s2sPHS398ModBud.get_dates(<<PROPOSAL_NUMBER>>, <<VERSION_NUMBER>>, <<BUDGET_PERIOD>>,  " +
                            " <<OUT RESULTSET rset>> )", "Coeus", param);
     }else{
             throw new CoeusException("db_exceptionCode.1000");
     }
      int listSize = result.size();
      if (listSize > 0){
           for (int i=0; i<listSize; i++){
            row = (HashMap)result.elementAt(0);
           }
      }
      return row; 
 }
    
    
  
  /** getInDirectCosts 
   *  returns vector of hashmaps - one hashMap for each indirect rate
   *  hashmap contains  RATE_NUMBER,
                        DESCRIPTION,
			IDC_RATE,
			IDC_BASE
			FUNDS_REQUESTED
  */
       public CoeusVector getInDirectCosts(String propNumber, int budgetPeriod, int version)
       throws CoeusException, DBException {
       CoeusVector cvIndCosts = new CoeusVector();       
       BigDecimal totalIndirectCosts = new BigDecimal("0");
       
       Vector result = null;
       Vector param = new Vector();
                               
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("AI_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.addElement(new Parameter("AI_PERIOD",
            DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       
        HashMap resultRow = null;
                 
       if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call s2sPHS398ModBud.get_indirect_cost( <<AS_PROPOSAL_NUMBER>> , <<AI_VERSION_NUMBER>> , "
                + "<<AI_PERIOD>> , " +   "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
               
        if (listSize > 0){
            for (int rowIndex=0;rowIndex<listSize;rowIndex++){
                  resultRow = (HashMap)result.elementAt(rowIndex);
                  cvIndCosts.add(resultRow);        
            }
               
         }
         return cvIndCosts;
       }  
}        
  
 
    
   
      
  
       