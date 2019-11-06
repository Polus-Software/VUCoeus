/*
 * Ed524V11TxnBean.java
 *
 * Created on Oct 23, 2006
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
 * @author  ele
 */
public class Ed524BudgetV11TxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    String propNumber;
    
    /** Creates a new instance of Ed524BudgetV11TxnBean */
    public Ed524BudgetV11TxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
     public HashMap getSimpleInfo (String propNumber)
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
               "call s2sED524V11Pkg.getSimpleInfo( <<PROPOSAL_NUMBER>> , "
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
     
     /*
      * getPeriodAmts returns a coeusVector of hashMaps for all budget periods
      */
     public CoeusVector getPeriodAmts (String propNumber, int version, int numPeriods)
      throws CoeusException, DBException{
          
      if (propNumber==null)
          throw new CoeusXMLException("Proposal Number is Null");
      
      CoeusVector cvAmts = new CoeusVector();
      
      HashMap hmPeriod = new HashMap();
      
      for (int period=1; period<= numPeriods; period++){
          hmPeriod = new HashMap();
          hmPeriod = getAmts(propNumber, version, period);
          cvAmts.add(hmPeriod);
      }
          
      return cvAmts;
     }
     
  private HashMap getAmts(String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getBudgetYear( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
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
      * getCategoryAmts returns a coeusVector of hashMaps for all budget periods
   
     public CoeusVector getCategoryAmts (String propNumber, int version, int numPeriods)
      throws CoeusException, DBException{
          
      if (propNumber==null)
          throw new CoeusXMLException("Proposal Number is Null");
      
      CoeusVector cvAmts = new CoeusVector();
      
      HashMap hmPeriodAmts = new HashMap();
      
      for (int period=1; period<= numPeriods; period++){
          hmPeriodAmts = new HashMap();
          hmPeriodAmts = getCategoryAmtsForPeriod(propNumber, version, period);
          cvAmts.add(hmPeriodAmts);
      }
          
      return cvAmts;
     }
     */
     /*
     private HashMap getCategoryAmtsForPeriod (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getCategoryAmts( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
    }
     */
public HashMap  get_total_cost (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.get_total_cost ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }
	

public HashMap  getIDCInfo (String propNumber)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
    
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getIDCInfo ( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }
	

public HashMap  getIDCCostSharing (String propNumber, int period, int version)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
      
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
    
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.get_idc_cost_Sharing ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }
 

public HashMap  get_fringe (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.get_fringe ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }

public HashMap  getPersonnelCosts (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getPersonnelCosts ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }

public HashMap  getOther (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getOther ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }

public HashMap  getContractual (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getContractual ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }



public HashMap  getEquipment (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getEquipment ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }

public HashMap  getConstruction (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getConstruction ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }


public HashMap  getTraining (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getTraining ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }

                                                               
public HashMap  getSupplies (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getSupplies ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
      }


 public HashMap  getTravel (String propNumber, int version, int period)
         throws CoeusException, DBException{
           
      Vector result = new Vector(3,2);
      Vector param= new Vector();
        
      HashMap row = null;
      param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    
      param.addElement(new Parameter("VERSION_NUMBER",
                       DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
      param.addElement(new Parameter("BUDGET_PERIOD",
                       DBEngineConstants.TYPE_INT,Integer.toString(period)));
      
      if(dbEngine !=null){
           
           result = dbEngine.executeRequest("Coeus",
               "call s2sED524V11Pkg.getTravel ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                                + " <<BUDGET_PERIOD>>, <<OUT RESULTSET rset>> )", "Coeus", param);
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
       
    
   
    
   
