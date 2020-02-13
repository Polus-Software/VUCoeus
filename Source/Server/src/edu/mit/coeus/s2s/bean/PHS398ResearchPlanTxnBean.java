/*
 * PHS398ResearchPlanTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author  jenlu
 * PHS398ResearchPlanStream calls this object.
 */
public class PHS398ResearchPlanTxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    String propNumber;
    
    /** Creates a new instance of PHS398ResearchPlanTxnBean */
    public PHS398ResearchPlanTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
        
    }
    
     public HashMap getApplicationType (String propNumber)
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
               "call s2sPackage.getApplicationType( <<PROPOSAL_NUMBER>> , "
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

