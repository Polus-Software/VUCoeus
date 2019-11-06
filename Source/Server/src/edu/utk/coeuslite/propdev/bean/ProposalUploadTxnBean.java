/*
 * ProposalUploadTxnBean.java
 *
 * Created on July 19, 2006, 12:37 PM
 */

package edu.utk.coeuslite.propdev.bean;

import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.exception.CoeusException;



import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.sql.*;
import java.sql.Timestamp;
import java.util.*;
/**
 *
 * @author  chandrashekara
 */
public class ProposalUploadTxnBean{
    /**
     * dbEngine instance
     */
    private DBEngineImpl dbEngine;
    /**
     * DSN name
     */
    private static final String DSN = "Coeus";
    
    private Timestamp timestamp;
    
    private final String CONTENT_KEY = "Content-Type";
    private final String CONTENT_VALUE = "application/octet-stream";
    
    private String userId;
    
    
    public ProposalUploadTxnBean(){
        
    }
    /** Creates a new instance of ProposalUploadTxnBean */
    public ProposalUploadTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        this.userId = userId;
    }
    
    
        
    public Object getDocumentData(HashMap hmDataObjects)
    throws CoeusException, DBException {
        return getContents(hmDataObjects);
    }
    
    private Object getContents(HashMap hmDataObjects)
    throws CoeusException, DBException {

        Object object = null;
        String content = null;
        StringBuffer sqlUpload = null;
        ProcReqParameter procNarrative = null;
        Vector result = null;
        Vector param = null;
        
        String proposalNumber = (String)hmDataObjects.get(NarrativeDocumentConstants.PROPOSAL_NUMBER);
        String personId = (String)hmDataObjects.get(NarrativeDocumentConstants.PERSON_ID);
        Object modNumber = hmDataObjects.get(NarrativeDocumentConstants.MODULE_NUMBER);
        Object bioNum = hmDataObjects.get(NarrativeDocumentConstants.BIO_NUMBER);
        int moduleNumber =-1;
        int bioNumber = -1;
        if(modNumber!= null){
            moduleNumber = Integer.parseInt(modNumber.toString());
        }
        if(bioNum!= null){
            bioNumber = Integer.parseInt(bioNum.toString());
        }
        String fileType = (String)hmDataObjects.get(NarrativeDocumentConstants.FILE_TYPE);
        int file = -1;
        if(fileType!= null){
            file = Integer.parseInt(fileType.toString());
        }
       if(file == NarrativeDocumentConstants.NARRATIVE_PDF) {
            content = "NARRATIVE_PDF";
            sqlUpload = new StringBuffer();
            sqlUpload.append("SELECT ");
            sqlUpload.append(content);
            sqlUpload.append(" FROM  OSP$NARRATIVE_PDF WHERE");
            
            sqlUpload.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
            sqlUpload.append("AND MODULE_NUMBER = <<MODULE_NUMBER>> ");
            param = new Vector();
            param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
            param.addElement(new Parameter("MODULE_NUMBER", "int", "" + moduleNumber));
            procNarrative = new ProcReqParameter();
            procNarrative.setDSN("Coeus");
            procNarrative.setParameterInfo(param);
            procNarrative.setSqlCommand(sqlUpload.toString());
            result = new Vector();
            if(dbEngine != null)
                result = dbEngine.executeRequest("Coeus", sqlUpload.toString(), "Coeus", param);
            else
                throw new CoeusException("db_exceptionCode.1000");
            int listSize = result.size();
            if(listSize > 0) {
                HashMap mapRow = (HashMap)result.elementAt(0);
                object = mapRow.get(content);
            }
       }else if(file == NarrativeDocumentConstants.PERSON_PDF) {
            content = "BIO_PDF";
            sqlUpload = new StringBuffer();
            sqlUpload.append("SELECT ");
            sqlUpload.append(content);
            sqlUpload.append(" FROM  OSP$EPS_PROP_PERSON_BIO_PDF WHERE");
            sqlUpload.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
            sqlUpload.append("AND PERSON_ID = <<PERSON_ID>> ");
            sqlUpload.append("AND BIO_NUMBER = <<BIO_NUMBER>> ");
            param = new Vector();
            param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
            param.addElement(new Parameter("PERSON_ID", "String", "" + personId));
            param.addElement(new Parameter("BIO_NUMBER", "int", "" + bioNumber));
            procNarrative = new ProcReqParameter();
            procNarrative.setDSN("Coeus");
            procNarrative.setParameterInfo(param);
            procNarrative.setSqlCommand(sqlUpload.toString());
            result = new Vector();
            if(dbEngine != null)
                result = dbEngine.executeRequest("Coeus", sqlUpload.toString(), "Coeus", param);
            else
                throw new CoeusException("db_exceptionCode.1000");
            int listSize = result.size();
            if(listSize > 0) {
                HashMap mapRow = (HashMap)result.elementAt(0);
                object = mapRow.get(content);
            }
        }
        return object;
    }
    
    
 
}
