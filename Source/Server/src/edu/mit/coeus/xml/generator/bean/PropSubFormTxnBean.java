/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * PropSubFormTxnBean.java
 *
 * Created on June 15, 2006, 6:16 PM
 */

package edu.mit.coeus.xml.generator.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  geo thomas
 */
public class PropSubFormTxnBean {
    private DBEngineImpl dbEngine;
    
    /** Creates a new instance of PropSubFormTxnBean */
    public PropSubFormTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    /*
     *GET_PROPOSAL_DETAILS
     *GET_SPECIAL_REVIEW_DETAILS
     *GET_INVESTIGATOR_DETAILS
     *GET_INV_CREDIT_DETAILS
     */
    public HashMap getProposalSubmissionDetails(String proposalNumber,String procName) throws DBException,CoeusException{
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        HashMap row = null;
        Vector result = new Vector();
          
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  PKG_PROP_SUBMISSION."+procName+" ( <<PROPOSAL_NUMBER>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
            return result.isEmpty()?null:(HashMap)result.get(0);
    }
    public Vector getProSubDetList(String proposalNumber,String procName) throws DBException,CoeusException{
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        HashMap row = null;
        Vector result = new Vector();
          
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  PKG_PROP_SUBMISSION."+procName+" ( <<PROPOSAL_NUMBER>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
            return result;
    }
}
