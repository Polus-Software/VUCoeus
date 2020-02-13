package edu.mit.coeus.propdev.bean.web;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.Vector;

import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import java.io.*;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;

/**
 * Component for accessing and updating a given user's messages.
 */
public class GetNarrativeDocumentBean implements Serializable
{   /**
     * Singleton instance of DBEngineImpl.
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor
     * @throws DBException
     */
    public GetNarrativeDocumentBean()
    {   dbEngine = new DBEngineImpl();
    }


    public boolean hasRightToViewNarrative
        (String proposalNumber, String moduleNumber, String userId)
        throws DBException
    {
        /* check whether user has right to view narrative */
        Vector result = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("USER_ID", "String", userId));
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("MODULE_NUMBER", "String", moduleNumber));
        result = dbEngine.executeFunctions("Coeus",
          "{ <<OUT STRING VIEW_RIGHTS>> = call fn_user_can_view_narr_module "+
          "( <<USER_ID>> , <<PROPOSAL_NUMBER>> , <<MODULE_NUMBER>> ) }",
          param);
        int viewRights = 0; // set default - no rights to view this proposal
        if(result.size() > 0)
        {
          /* case #748 comment begin */
          //Hashtable proposalRights = (Hashtable)result.get(0);
          //String vRights = proposalRights.get("VIEW_RIGHTS").toString();
          /* case #748 comment end */
          /* case #748 begin */
          HashMap proposalRights = (HashMap)result.get(0);
          String vRights = (String)proposalRights.get("VIEW_RIGHTS");
          /* case #748 end */

          if(vRights != null) {
            viewRights = Integer.parseInt(vRights);
          }
        }
        return (viewRights > 0);
    }



    /**
     * Get narrative document.
     * @param proposalNumber
     * @param moduleNumber
     * @param docType
     * @param userId
     * @return
     * @throws SQLException
     * @throws DBException
     * @throws Exception
     */
     /* CASE #748 Comment Begin */
    /*public Vector GetNarrativeDocument(String proposalNumber, String moduleNumber, String docType, String userId) throws SQLException, DBException, Exception
    {
        //DBEngineImpl.executeRequest takes as parameters String ServiceName, String SQLCommand,
        //String DataSource, Vector paramList.

        Vector results = new Vector();
        /* check whether user has rights to view narrative */
        /*if (hasRightToViewNarrative(proposalNumber, moduleNumber, userId)){
          String SQL = "";
          if (docType.equals("PDF"))
          {
            SQL = "select file_name, narrative_pdf narrative_doc from OSP$NARRATIVE_PDF " +
               "where proposal_number = '" + proposalNumber + "' AND " +
               "module_number = " + moduleNumber;
          }
          else if (docType.equals("WORD"))
          {
            SQL = "select file_name, narrative_source narrative_doc from OSP$NARRATIVE_SOURCE " +
               "where proposal_number = '" + proposalNumber + "' AND " +
               "module_number = " + moduleNumber;
          }
          if(dbEngine!=null)
          {
            results = dbEngine.executeRequest ("Coeus", SQL);
          }
          else
          {
            throw new DBException("error.database.instance.null");
          }
        }
        return results;
    }*/
    /* CASE #748 Comment End */

    /**
     *  The method used to fetch the Narrative PDF's from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     *  @return byte[] PDF Data
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalNarrativePDFSourceBean getNarrativePDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
        throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;

        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));

        selectQuery = "SELECT NARRATIVE_PDF,FILE_NAME,UPDATE_USER,UPDATE_TIMESTAMP FROM OSP$NARRATIVE_PDF "+
            "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND  MODULE_NUMBER =  <<MODULE_NUMBER>>";

        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_PDF"));
                proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                return proposalNarrativePDFSourceBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }

    /**
     *  The method used to fetch the Narrative Source from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean
     *  @return byte[] PDF Data
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalNarrativePDFSourceBean getNarrativeSource(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
        throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;

        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNarrativePDFSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("MODULE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalNarrativePDFSourceBean.getModuleNumber()));

        selectQuery = "SELECT NARRATIVE_SOURCE, FILE_NAME, INPUT_TYPE, PLATFORM_TYPE, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$NARRATIVE_SOURCE "+
            "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND  MODULE_NUMBER =  <<MODULE_NUMBER>>";

        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_SOURCE"));
                proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                proposalNarrativePDFSourceBean.setInputType((String)resultRow.get("INPUT_TYPE"));
                proposalNarrativePDFSourceBean.setPlatFormType((String)resultRow.get("PLATFORM_TYPE"));
                proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                return proposalNarrativePDFSourceBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }


}
