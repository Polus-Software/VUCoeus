/*
 * XMLPDFTxnBean.java
 *
 * Created on October 28, 2003, 12:27 PM
 */

package edu.mit.coeus.utils.pdf.generator;

import java.util.* ;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.exception.CoeusException;

public class XMLPDFTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    private TransactionMonitor transMon;
    // holds the userId for the logged in user
    private String userId;
    
    /** Creates a new instance of XMLPDFTxnBean */
    public XMLPDFTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    //method getCorrespondenceTemplate is removed and created two specifc functions for irb and iacuc
  
   //Added for case id COEUSQA-1724 iacuc protocol stream generation start
     /**
     *  This method used to get all Correspondence Template details
     *  from OSP$PROTO_CORRESP_TEMPL.
     *  <li>To fetch the data, it uses the procedure get_proto_corresp_types.
     *
     *  @return Vector collection of all Protocol correspondence types
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    
    public byte[] getIRBCorrespondenceTemplate(final String committeeId, final int protoCorrespTypeCode)
                                                throws CoeusException,DBException {
        Vector result = null;
        Vector param =null;
        Vector correspTypes = new Vector();
        HashMap resultRow = null;
        if(dbEngine != null) {
            param = new Vector();
            param.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespTypeCode));
            param.addElement(new Parameter("COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,
            committeeId));
            // try with committee id of the selected committee template,
            // if that returns null then get the default committee correspondence template
            
            String selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$PROTO_CORRESP_TEMPL "
            + " WHERE PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> "
            + " AND COMMITTEE_ID =  <<COMMITTEE_ID>>" ;
            
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",param);
            if( !result.isEmpty()) {
                resultRow = (HashMap)result.get(0);
                java.io.ByteArrayOutputStream template =
                (java.io.ByteArrayOutputStream)resultRow.get("CORESPONDENCE_TEMPLATE");
                return template.toByteArray();
            }
            
            param = new Vector();
            param.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+protoCorrespTypeCode));
            
            param.addElement(new Parameter("COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,
            "DEFAULT"));
            
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", param);
            if( !result.isEmpty()) {
                resultRow = (HashMap)result.get(0);
                java.io.ByteArrayOutputStream template =
                (java.io.ByteArrayOutputStream)resultRow.get("CORESPONDENCE_TEMPLATE");
                return template.toByteArray();
            }
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    
     /**
     *  This method used to get all Correspondence Template details
     *  from OSP$AC_PROTOCOL_CORRESP_TEMPL.
     *  <li>To fetch the data, it uses the procedure get_proto_corresp_types.
     *
     *  @return Vector collection of all Protocol correspondence types
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    
   
    public byte[] getIACUCCorrespondenceTemplate(final String committeeId, final int protoCorrespTypeCode) throws CoeusException,DBException {
        Vector result = null;
        Vector param = null;
        Vector correspTypes = new Vector();
        HashMap resultRow = null;
        if (dbEngine != null) {
            param = new Vector();
            param.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                    "" + protoCorrespTypeCode));
            param.addElement(new Parameter("COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING,
                    committeeId));
            // try with committee id of the selected committee template,
            // if that returns null then get the default committee correspondence template

            String selectQuery = "SELECT CORESPONDENCE_TEMPLATE FROM OSP$AC_PROTOCOL_CORRESP_TEMPL "
                    + " WHERE PROTO_CORRESP_TYPE_CODE =  <<PROTO_CORRESP_TYPE_CODE>> "
                    + " AND COMMITTEE_ID =  <<COMMITTEE_ID>>";

            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", param);
            if (!result.isEmpty()) {
                resultRow = (HashMap) result.get(0);
                java.io.ByteArrayOutputStream template =
                        (java.io.ByteArrayOutputStream) resultRow.get("CORESPONDENCE_TEMPLATE");
                return template.toByteArray();
            }

            param = new Vector();
            param.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                    "" + protoCorrespTypeCode));

            param.addElement(new Parameter("COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING,
                    "DEFAULT"));

            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", param);
            if (!result.isEmpty()) {
                resultRow = (HashMap) result.get(0);
                java.io.ByteArrayOutputStream template =
                        (java.io.ByteArrayOutputStream) resultRow.get("CORESPONDENCE_TEMPLATE");
                return template.toByteArray();
            }
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
     //Added for case id COEUSQA-1724 iacuc protocol stream end
    
    public int getProtoCoresspondenceCode(String correspondenceType) throws CoeusException, DBException {
        Vector param = new Vector() ;
        Vector result = null;
        HashMap paramResult ;
        int corrType = -1 ;
        
        param.add(new Parameter("PARAM_NAME",
        DBEngineConstants.TYPE_STRING, correspondenceType));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT STRING CORR_TYP>>=call get_parameter_value ( "
        + " << PARAM_NAME >>)}", param);
        if(!result.isEmpty()){
            paramResult =(HashMap)result.elementAt(0);
            corrType = (new Integer(paramResult.get("CORR_TYP").toString())).intValue() ;
        }
        
        return corrType ;
    }
    
    /** Get the protcol Correspondence Type Description
     *@param protocolCorresTypeCode
     *@ returns protoCorrespDescription
     *Added by chandra
     */
    public String getIRBProtocolcorrespondenceDesc(int corresType) throws CoeusException, DBException{
        Vector param = new Vector();
        Vector result = null;
        HashMap paramResult;
        String corresDesc = "";

        param.add(new Parameter("PROTO_CORRESP_TYPE_CODE",
                DBEngineConstants.TYPE_INT, "" + corresType));
        result = dbEngine.executeRequest("Coeus",
                "call GET_PROTO_CORRESP_TYPES_DESCR ( <<PROTO_CORRESP_TYPE_CODE>> , <<OUT RESULTSET rset>> )",
                "Coeus", param);
        if (!result.isEmpty()) {
            paramResult = (HashMap) result.elementAt(0);
            corresDesc = (String) paramResult.get("DESCRIPTION");
        }
        return corresDesc;
    }

    public String getIACUCProtocolcorrespondenceDesc(int corresType) throws CoeusException, DBException {
        Vector param = new Vector();
        Vector result = null;
        HashMap paramResult;
        String corresDesc = "";

        param.add(new Parameter("PROTO_CORRESP_TYPE_CODE",
                DBEngineConstants.TYPE_INT, "" + corresType));
        result = dbEngine.executeRequest("Coeus",
                "call GET_AC_PROTO_CORRESP_TYPE_DESC ( <<PROTO_CORRESP_TYPE_CODE>> , <<OUT RESULTSET rset>> )",
                "Coeus", param);
        if (!result.isEmpty()) {
            paramResult = (HashMap) result.elementAt(0);
            corresDesc = (String) paramResult.get("DESCRIPTION");
        }
        return corresDesc;
    }
    
}
