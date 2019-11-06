/*
 * KeyPersonExpV11TxnBean.java
 *
 * Created on August 10, 2006
 */

package edu.mit.coeus.s2s.bean;


import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  ele
 */
public class KeyPersonExpV11TxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    String propNumber;
    
    /** Creates a new instance of KeyPersonExpV11TxnBean */
    public KeyPersonExpV11TxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
   
    

     public HashMap getStateName (String stateCode)
        throws CoeusException, DBException {
     
        if(stateCode==null) 
                throw new CoeusXMLException("State code is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("STATE_CODE",
                       DBEngineConstants.TYPE_STRING,stateCode));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sKeyPersonExp_V11Pkg.getStateName( <<STATE_CODE>> , "
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
     
     public HashMap getCountryName (String countryCode)
        throws CoeusException, DBException {
     
        if(countryCode==null) 
                throw new CoeusXMLException("Country code is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("COUNTRY_CODE",
                       DBEngineConstants.TYPE_STRING,countryCode));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sKeyPersonExp_V11Pkg.getCountryName( <<COUNTRY_CODE>> , "
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
}
