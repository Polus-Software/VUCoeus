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
  
public class S2SAttachmentsV11TxnBean 

{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    String propNumber;
    int    version;
    BigDecimal bdZero = new BigDecimal("0");

    
    /** Creates a new instance of S2SAttachmentsV11TxnBean */
    public S2SAttachmentsV11TxnBean()
    {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
       
    }
    
 
          
  
public HashMap getNumberOtherAttachments (String propNumber)
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
           "call s2sAttachmentsV11Pkg.get_count_attachments ( <<PROPOSAL_NUMBER>> , "+
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
         
 
    
  
}
       