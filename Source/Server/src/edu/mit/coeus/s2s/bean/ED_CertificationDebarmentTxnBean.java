/*
 * ED_CertificationDebarmentTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;

public class ED_CertificationDebarmentTxnBean {
    
    private DBEngineImpl dbEngine;
    
    /** Creates a new instance of ED_CertificationDebarmentTxnBean */
    public ED_CertificationDebarmentTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    public HashMap getOrganizationName (String propNumber)
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
               "call s2sGG_LobbyingFormV11Pkg.get_eps_org_name( <<PROPOSAL_NUMBER>> , "
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
