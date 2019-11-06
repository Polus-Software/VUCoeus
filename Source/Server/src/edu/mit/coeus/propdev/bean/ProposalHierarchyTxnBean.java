/*
 * ProposalHierarchyTxnBean.java
 *
 * Created on August 11, 2005, 4:47 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;


/**
 *
 * @author  chandrashekara
 */
public class ProposalHierarchyTxnBean implements TypeConstants{
     // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    //Connection instance added by Shivakumar for locking enhancement
    Connection conn = null;
    // Instance of TransactionMonitor for the transactions control
    private TransactionMonitor transMon;
    
    private String userId;
    private static final String DSN = "Coeus";
    
    /** Creates a new instance of ProposalHierarchyTxnBean */
    public ProposalHierarchyTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    public ProposalHierarchyTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    public HashMap getParentProposalData(String proposalNumber) 
        throws CoeusException,DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String propNumber = null;
        HashMap hmPropData = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine!=null){
            try{
                result = dbEngine.executeRequest("Coeus",
                "call GET_PARENT_PROPOSAL ( <<PROPOSAL_NUMBER>> , "+
                "<< OUT RESULTSET rset >>  ) ",
                "Coeus", param);           
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result != null) {
             if(result.size()>0){
                 HashMap hmData = (HashMap)result.get(0);
                 propNumber = (String)hmData.get("PARENT_PROPOSAL_NUMBER");
             }
        }
        
        hmPropData = new HashMap();
        if(propNumber != null) {
            hmPropData.put("IN_HIERARCHY", new Boolean(true));
            hmPropData.put("PARENT_PROPOSAL", propNumber);
            if(proposalNumber.trim().equals(propNumber.trim())) 
                hmPropData.put("IS_PARENT", new Boolean(true));
            else
                hmPropData.put("IS_PARENT", new Boolean(false));
            
        }else {
            hmPropData.put("IN_HIERARCHY", new Boolean(false));
            hmPropData.put("IS_PARENT", new Boolean(false));
        }
        return hmPropData;
    }
    
    
    public ProposalHierarchyBean getHierarchyData(String proposalNumber)
    throws CoeusException, DBException{
        HashMap  hierarchyData = null;
        Vector param= new Vector();
        ProposalHierarchyBean hierarchyBean = null;
        ProposalBudgetVersionBean budgetVersionBean = null;
        ProposalBudgetBean proposalBudgetBean = null;
        Vector result = null;
        param.addElement(new Parameter("PARENT_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine!=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                "call GET_PROPOSAL_HIERARCHY_DATA(<<PARENT_PROPOSAL_NUMBER>> , "
                +" << OUT RESULTSET rset >>)", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null) {
            int size = result.size();
            hierarchyBean = new ProposalHierarchyBean();
            //hierarchyBean.setParentProposalNumber(proposalNumber);
            String oldPropNumber = null;
            String propNumber = null;
            CoeusVector cvBudgetVersions = null;
            CoeusVector cvProposalData = new CoeusVector();;
            for(int i=0;i<size;i++) {
                HashMap hmData = (HashMap)result.get(i);
                hierarchyBean.setParentProposalNumber(
                    (String)hmData.get("PARENT_PROPOSAL_NUMBER"));
                
                propNumber = (String)hmData.get("CHILD_PROPOSAL_NUMBER");
                if(!propNumber.equals(oldPropNumber)) {
                    proposalBudgetBean = new ProposalBudgetBean();
                    proposalBudgetBean.setProposalNumber(propNumber);
                    cvBudgetVersions = new CoeusVector();
                    proposalBudgetBean.setBudgetVersions(cvBudgetVersions);
                    
                    proposalBudgetBean.setBudgetStatus(
                        hmData.get("BUDGET_STATUS").toString().equals("C") ? true : false);
                    
                    proposalBudgetBean.setProposalSynced(
                        hmData.get("IS_PROPOSAL_SYNCED").toString().equals("Y") ? true : false);
                    
                    proposalBudgetBean.setChildTypeDesc(
                        hmData.get("CHILD_TYPE_DESCRIPTION") == null ? "" : hmData.get("CHILD_TYPE_DESCRIPTION").toString().trim());
                    
                    proposalBudgetBean.setUnitNumber(
                        hmData.get("UNIT_NUMBER") == null ? "" : hmData.get("UNIT_NUMBER").toString().trim());
                    
                    cvProposalData.addElement(proposalBudgetBean);
                }

                budgetVersionBean = new ProposalBudgetVersionBean();
                
                budgetVersionBean.setVersionNumber(
                    Integer.parseInt(hmData.get("VERSION_NUMBER").toString()));
                
                budgetVersionBean.setVersionFlag(
                    hmData.get("FINAL_VERSION_FLAG").toString().equals("Y") ? true : false);
                
                budgetVersionBean.setBudgetSynced(
                    hmData.get("IS_BUDGET_SYNCED").toString().equals("Y") ? true : false);
                
                budgetVersionBean.setUnitNumber(
                        hmData.get("UNIT_NUMBER") == null ? "" : hmData.get("UNIT_NUMBER").toString().trim());
                 
                budgetVersionBean.setChildType(hmData.get("CHILD_TYPE_DESCRIPTION") == null ? "" : hmData.get("CHILD_TYPE_DESCRIPTION").toString().trim());
                    
                cvBudgetVersions.addElement(budgetVersionBean);
                oldPropNumber = propNumber;
            }
            hierarchyBean.setProposalData(cvProposalData);
        }
        return hierarchyBean ;
    }
    
    /**
     *  This method used get the code. If the code is returning zero then hierarchy
     *can be built without any validation
     *  <li>To fetch the data, it uses the function FN_CAN_BUILD_PROP_HIERARCHY.
     *
     *  @return int code for the proposal Number and actionType
     *  @param String Proposal Number and actionType to get code
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int canBuildHierarchy(String proposalNumber, String actionType)
    throws CoeusException,DBException{
        
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        Vector data = null;
        int code = 0;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        
        //join - While joining hierarchy , create - While createing hierarchy
        param.addElement(new Parameter("ACTION_TYPE",
            DBEngineConstants.TYPE_STRING,actionType));

        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER CODE>> = "
                +" call FN_CAN_PERFORM_PH_ACTIONS( << PROPOSAL_NUMBER >>, << ACTION_TYPE >> ) }", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            code = Integer.parseInt(rowParameter.get("CODE").toString());
        }
        return code;
    }
    
    
    
     /** This method is used to copy all the details of the given Proposal except Blob data.
      * <li>To copy the data, it uses the function FN_COPY_PROPOSAL.
      *
      * @return int copyFlag indicating whether the copy was successfull.
      * @param sourceProposalNumber is given as the input parameter to the function.
      * @param targetProposalNumber is given as the input parameter to the function.
      * @param budgetFlag is given as the input parameter to the function.
      * @param narrativeFlag is given as the input parameter to the function.
      * @param unitNumber is given as the input parameter to the function.
      * @param userId is given as the input parameter to the function.      
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int createProposal(String sourceProposalNumber , String parentProposalNumber,String userId,String unitNumber)
                            throws CoeusException, DBException {
        int copyFlag = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("SOURCE_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,sourceProposalNumber));
        param.add(new Parameter("PARENT_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,parentProposalNumber));
        param.add(new Parameter("UNIT_NUMBER",
                                DBEngineConstants.TYPE_STRING,unitNumber));
        
        param.add(new Parameter("USER_ID",
                                DBEngineConstants.TYPE_STRING,userId));          
        
        StringBuffer sqlParentProposal = new StringBuffer(
                                        "{ <<OUT INTEGER COPYFLAG>> = call FN_CREATE_PARENT_PROPOSAL(");
        sqlParentProposal.append(" <<SOURCE_PROPOSAL_NUMBER>> , ");
        sqlParentProposal.append(" <<PARENT_PROPOSAL_NUMBER>> , ");
        sqlParentProposal.append(" <<UNIT_NUMBER>> , ");
        sqlParentProposal.append(" <<USER_ID>> ) }");
        
        ProcReqParameter procPropCopy  = new ProcReqParameter();
        procPropCopy.setDSN(DSN);
        procPropCopy.setParameterInfo(param);
        procPropCopy.setSqlCommand(sqlParentProposal.toString());
        procedures.add(procPropCopy);
        //procedures.add(copyProposalBlobs(sourceProposalNumber, parentProposalNumber,unitNumber, userId));
        
        
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                //Code added for Case#3183 proposal hierarchy. - starts
                Vector vecData = dbEngine.executeStoreProcs(procedures, conn);
                if(vecData != null && vecData.size() > 0){
                    HashMap rowParameter = (HashMap)vecData.elementAt(0);
                    int code = Integer.parseInt(rowParameter.get("COPYFLAG").toString());
                    if(code != 0){
                        throw new CoeusException("hierarchy_exceptionCode."+code);
                    }
                }
                //Code added for Case#3183 proposal hierarchy. - ends
                ProposalDevelopmentUpdateTxnBean txnBean = new ProposalDevelopmentUpdateTxnBean();
                // Update the proposal persons Biography blobs data
                txnBean.copyProposalPersonBiographyBlobs(sourceProposalNumber,parentProposalNumber,conn);
                // Update the narrative blob data
                txnBean.copyProposalNarrativeBlobs(sourceProposalNumber, parentProposalNumber, conn);
               // dbEngine.endTxn(conn);
            }catch(Exception ex){
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalHierarchyTxnBean", "createProposal");
                throw new CoeusException(ex.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        copyFlag = 1;
        return copyFlag; 
    }
    
    
    /** This method is used to JOIN all the details of the given Proposal except Blob data.
      * <li>To copy the data, it uses the function FN_COPY_PROPOSAL.
      *
      * @return int joinFlag indicating whether the copy was successfull.
      * @param parentProposalNumber is given as the input parameter to the function.
      * @param childProposalNumber is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int joinProposal(String parentProposalNumber , String childProposalNumber, String userId , int childTypeCode)
                            throws CoeusException, DBException {
        int joinFlag = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("AS_PARENT_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,parentProposalNumber));
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,childProposalNumber));
        param.add(new Parameter("AS_CHILD_TYPE",
                                DBEngineConstants.TYPE_INT, ""+childTypeCode));
        param.add(new Parameter("AS_UPDATE_USER",
                                DBEngineConstants.TYPE_STRING,userId));
        
        
        StringBuffer sqlParentProposal = new StringBuffer(
                                        "{ <<OUT INTEGER JOIN_FLAG>> = call FN_JOIN_PROPOSAL_TO_HIERARCHY(");
        sqlParentProposal.append(" <<AS_PARENT_PROPOSAL_NUMBER>> , ");
        sqlParentProposal.append(" <<AS_PROPOSAL_NUMBER>> , ");
        sqlParentProposal.append(" <<AS_CHILD_TYPE>> , ");
        sqlParentProposal.append(" <<AS_UPDATE_USER>> ) } ");
        
        ProcReqParameter procPropCopy  = new ProcReqParameter();
        procPropCopy.setDSN(DSN);
        procPropCopy.setParameterInfo(param);
        procPropCopy.setSqlCommand(sqlParentProposal.toString());
        
        //Join Porposal parameters
        procedures.add(procPropCopy);
        
        //Join Budget Parameter
        procedures.add(
            joinBudgetToHierarchy(parentProposalNumber , childProposalNumber , childTypeCode , userId));
        Vector vecDataFromDB = null;
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                vecDataFromDB = dbEngine.executeStoreProcs(procedures, conn);
                dbEngine.endTxn(conn);
                if(vecDataFromDB != null && vecDataFromDB.size()>0){
                    Vector vecJoinData = (Vector)vecDataFromDB.get(0);
                    if(vecJoinData != null && vecJoinData.size()>0){
                        HashMap hmReuslt = (HashMap)vecJoinData.get(0);
                        joinFlag = Integer.parseInt(hmReuslt.get("JOIN_FLAG").toString());
                    }
                }
            }catch(Exception ex){
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalHierarchyTxnBean", "joinProposal");
                if(ex.getMessage().indexOf("ORA-20000") != -1){
                    throw new CoeusException(((DBException)ex).getUserMessage());
                } else {
                    throw new CoeusException(ex.getMessage());
                }
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        //Join Blobs Parameter        
        CoeusVector cvJoinBlobData = joinProposalBlobs(parentProposalNumber , childProposalNumber);
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                if(cvJoinBlobData != null && cvJoinBlobData.size()>0){
                    for(int index = 0; index < cvJoinBlobData.size() ; index++){
                       ProcReqParameter procReqParameter = (ProcReqParameter)cvJoinBlobData.get(index);
                       dbEngine.batchSQLUpdate(procReqParameter , conn);
                    }
                }
            }catch(Exception ex){
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalHierarchyTxnBean", "joinProposal");
                throw new CoeusException(ex.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return joinFlag; 
    }
    
    
    /** This method is used to join Blob data of the given Proposal Number.
      * <li>To copy the data, it uses the function FN_JOIN_PROPOSAL_BLOBS.
      *
      * @return ProcReqParameter containing DSN, Stored Procedure name and Parameter info.
      * @param parentPropNo ,childPropNo , proposalNumber is given as the input parameter to the function.
      * @param userId is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public CoeusVector joinProposalBlobs(String parentPropNo , String childPropNo)
                            throws CoeusException, DBException {
                                
        ProcReqParameter procJoinBlob = null;
        
        CoeusVector cvData = new CoeusVector();

        //Bio PDF
        //Modified for Proposal Hierarchy Person Module - case 3183 - start
        //CoeusVector cvPDFData = getProposalPersonBioPDF(parentPropNo , childPropNo);
        CoeusVector cvPDFData = getProposalPersonBioPDF(childPropNo);
        //cvPDFData = getParentBioNumber(cvPDFData, childPropNo, true);
        //Modified for Proposal Hierarchy Person Module - case 3183 - end
        ProposalPersonTxnBean personTxnBean = new ProposalPersonTxnBean();
        if(cvPDFData != null && cvPDFData.size() > 0){
            for(int i = 0 ; i < cvPDFData.size() ; i++){
                ProposalPersonBioPDFBean proposalPersonBioPDFBean =
                                        (ProposalPersonBioPDFBean)cvPDFData.get(i);
                proposalPersonBioPDFBean.setProposalNumber(parentPropNo);
                cvData.add(addProposalPersonBioPDF(proposalPersonBioPDFBean));
            }
        }
        
        
        //Bio Source
        //Modified for Proposal Hierarchy Person Module - case 3183 - start
        //CoeusVector cvSourceData =  getProposalPersonBioSource(parentPropNo , childPropNo);
        CoeusVector cvSourceData =  getProposalPersonBioSource(childPropNo);
        //cvSourceData = getParentBioNumber(cvSourceData, childPropNo, false);
        //Modified for Proposal Hierarchy Person Module - case 3183 - end
        if(cvSourceData != null && cvSourceData.size() > 0){
            for(int j = 0 ; j < cvSourceData.size() ; j++){
                ProposalPersonBioSourceBean proposalPersonBioSourceBean = 
                                (ProposalPersonBioSourceBean)cvSourceData.get(j);
                proposalPersonBioSourceBean.setProposalNumber(parentPropNo);
                cvData.add(addProposalPersonBioSource(proposalPersonBioSourceBean));
            }
        }
        
        // Get and update the Narrative Source
        ProposalNarrativeTxnBean narrativeTxnBean = new ProposalNarrativeTxnBean();
        //Modified for Proposal Hierarchy Person Module - case 3183 - start
         //CoeusVector cvNarrativeSourceData = getProposalNarrativeSource(parentPropNo , childPropNo);
        CoeusVector cvNarrativeSourceData = getProposalNarrativeSource(childPropNo);
         //cvNarrativeSourceData = getModuleNumbersData(cvNarrativeSourceData,parentPropNo,childPropNo,true);
        //Modified for Proposal Hierarchy Person Module - case 3183 - end
        if(cvNarrativeSourceData != null && cvNarrativeSourceData.size() > 0){
            for(int i = 0 ; i < cvNarrativeSourceData.size() ; i++){
                ProposalNarrativePDFSourceBean sourceBean =
                                        (ProposalNarrativePDFSourceBean)cvNarrativeSourceData.get(i);
                    sourceBean.setProposalNumber(parentPropNo);
                    cvData.add(addProposalNarrativeSource(sourceBean));
            }
        }
        // Get and update the Narrative PDF
        //Modified for Proposal Hierarchy Person Module - case 3183 - start
        //CoeusVector cvNarrativePDFData = getProposalNarrativePdf(parentPropNo , childPropNo);
        //cvNarrativePDFData = getModuleNumbersData(cvNarrativePDFData,parentPropNo , childPropNo,false);
        CoeusVector cvNarrativePDFData = getProposalNarrativePdf(childPropNo);
        //Modified for Proposal Hierarchy Person Module - case 3183 - end
        if(cvNarrativePDFData != null && cvNarrativePDFData.size() > 0){
            for(int i = 0 ; i < cvNarrativePDFData.size() ; i++){
                ProposalNarrativePDFSourceBean sourceBean =
                                        (ProposalNarrativePDFSourceBean)cvNarrativePDFData.get(i);
                    sourceBean.setProposalNumber(parentPropNo);
                    cvData.add(addProposalNarrativePdf(sourceBean));
            }
        }
        return cvData;
    }
   
    private CoeusVector getModuleNumbersDataForSource(CoeusVector cvData, String parentPropNo, String childPropNo)
          throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";        
        byte[] fileData = null;
        CoeusVector cvDataObjects = null;
        
        parameter.addElement(new Parameter("AV_PARENT_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, parentPropNo));        
        
        parameter.addElement(new Parameter("AV_CHILD_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, childPropNo));
        
       selectQuery = "SELECT MODULE_NUMBER,FILE_NAME FROM OSP$NARRATIVE_SOURCE "+
            "WHERE PROPOSAL_NUMBER = <<AV_PARENT_PROPOSAL_NUMBER>> AND FILE_NAME IN ( SELECT TO_CHAR(MODULE_NUMBER) FROM OSP$NARRATIVE_SOURCE WHERE PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>>)";
        
        HashMap resultRow = null;
        if(dbEngine!=null){
        try{
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
                if( result!= null &&  result.size() > 0){
                    cvDataObjects = new CoeusVector();
                    for(int index = 0 ; index < cvData.size() ; index++){
                        ProposalNarrativePDFSourceBean sourceBean = (ProposalNarrativePDFSourceBean)cvData.get(index);
                        resultRow = (HashMap)result.get(index);
                        int value = Integer.parseInt(resultRow.get("MODULE_NUMBER").toString());
                        String obj = (String)resultRow.get("FILE_NAME");
                        int actualValue = Integer.parseInt(obj);
                        if(sourceBean.getModuleNumber() == actualValue){
                            sourceBean.setModuleNumber(value);
                            cvDataObjects.addElement(sourceBean);
                        }
                    }
                }
        }catch (DBException ex){
            ex.printStackTrace();
        }
            
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return cvDataObjects;
    }
    
    /** The cild proposal module number has to be compared with the the value, which
     *is stroed in the file Name column then update the moduleNumber in actual
     *queried data. This will read the data from OSP$NARRATIVE_PDF AND OSP$NARRATIVE_SOURCE
     *@param CoeusVector contains the selected data of narrative source or PDF,
     *@param parent proposal, child proposal number. boolean flag specified
     *true to run OSP$NARRATIVE_SOURCE and false OSP$NARRATIVE_PDF
     */ 
     
    private CoeusVector getModuleNumbersData(CoeusVector cvData, String parentPropNo, String childPropNo, boolean flag)
          throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";        
        byte[] fileData = null;
        CoeusVector cvDataObjects = null;
        
        parameter.addElement(new Parameter("AV_PARENT_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, parentPropNo));        
        
        parameter.addElement(new Parameter("AV_CHILD_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, childPropNo));
        
        if(flag){
                selectQuery = "SELECT MODULE_NUMBER,FILE_NAME FROM OSP$NARRATIVE_SOURCE "+
                    "WHERE PROPOSAL_NUMBER = <<AV_PARENT_PROPOSAL_NUMBER>> AND FILE_NAME IN ( SELECT PROPOSAL_NUMBER || '-' || TO_CHAR(MODULE_NUMBER) FROM OSP$NARRATIVE_SOURCE WHERE PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>>)";
        }else{
            selectQuery = "SELECT MODULE_NUMBER,FILE_NAME FROM OSP$NARRATIVE_PDF "+
                    "WHERE PROPOSAL_NUMBER = <<AV_PARENT_PROPOSAL_NUMBER>> AND FILE_NAME IN ( SELECT PROPOSAL_NUMBER || '-' || TO_CHAR(MODULE_NUMBER) FROM OSP$NARRATIVE_PDF WHERE PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>>)";
        }
        
        HashMap resultRow = null;
        if(dbEngine!=null){
        try{
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
                if( result!= null &&  result.size() > 0){
                    cvDataObjects = new CoeusVector();
                    for(int index = 0 ; index < cvData.size() ; index++){
                        ProposalNarrativePDFSourceBean sourceBean = (ProposalNarrativePDFSourceBean)cvData.get(index);
                        //Modified for case id 3183 - Proposal Hierarchy - start
                        //To solve the narrative sync problem a
//                        resultRow = (HashMap)result.get(index);
//                        int value = Integer.parseInt(resultRow.get("MODULE_NUMBER").toString());
//                        String obj = (String)resultRow.get("FILE_NAME");
//                        obj = (obj == null)?"" : obj.substring(obj.indexOf("-")+1).trim();
//                        int actualValue = Integer.parseInt(obj);
//                        if(sourceBean.getModuleNumber() == actualValue){
//                            sourceBean.setModuleNumber(value);
//                            cvDataObjects.addElement(sourceBean);
//                        }
                        
                        for(int i=0; i< result.size(); i++){
                            resultRow = (HashMap)result.get(i);
                            String obj = (String)resultRow.get("FILE_NAME");
                            obj = (obj == null)?"" : obj.substring(obj.indexOf("-")+1).trim();
                            int actualValue = Integer.parseInt(obj);
                            if(sourceBean.getModuleNumber() == actualValue){
                                int value = Integer.parseInt(resultRow.get("MODULE_NUMBER").toString());
                                sourceBean.setModuleNumber(value);
                                cvDataObjects.addElement(sourceBean);
                                break;
                            }
                        }
                        //Modified for case id 3183 - Proposal Hierarchy - end
                    }
                }
        }catch (DBException ex){
            ex.printStackTrace();
        }
            
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return cvDataObjects;
    }
    
     /** This method is used to copy Blob data of the given Proposal Number.
      * <li>To copy the data, it uses the function FN_COPY_PROPOSAL_BLOBS.
      *
      * @return ProcReqParameter containing DSN, Stored Procedure name and Parameter info.
      * @param sourceProposalNumber is given as the input parameter to the function.
      * @param parentProposalNumber is given as the input parameter to the function.
      * @param narrativeFlag is given as the input parameter to the function.
      * @param unitNumber is given as the input parameter to the function.
      * @param userId is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public ProcReqParameter copyProposalBlobs(String sourceProposalNumber , 
                String parentProposalNumber, String unitNumber, String userId)
                            throws CoeusException, DBException {
        int copyFlag = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("SOURCE_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,sourceProposalNumber));
        param.add(new Parameter("TARGET_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,parentProposalNumber)); 
       param.add(new Parameter("NARRATIVE_FLAG",
                                DBEngineConstants.TYPE_STRING,new Character('N').toString()));
        param.add(new Parameter("UNIT_NUMBER",
                                DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("USER_ID",
                                DBEngineConstants.TYPE_STRING,userId));   
        
        StringBuffer sqlNarrative = new StringBuffer(
                                        "{ <<OUT INTEGER COPYFLAG>> = call FN_COPY_PROPOSAL_BLOBS(");
        sqlNarrative.append(" <<SOURCE_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<TARGET_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<NARRATIVE_FLAG>> , ");        
        sqlNarrative.append(" <<UNIT_NUMBER>> , ");
        sqlNarrative.append(" <<USER_ID>> ) }");
        
        ProcReqParameter procPropCopy  = new ProcReqParameter();
        procPropCopy.setDSN(DSN);
        procPropCopy.setParameterInfo(param);
        procPropCopy.setSqlCommand(sqlNarrative.toString());
        
        return procPropCopy;
    }// End of copyProposalBlobs(....)...
    
    /** This method is used to join budget
      * <li>To copy the data, it uses the function  FN_JOIN_BUDGET_TO_HIERARCHY.
      *
      * @return ProcReqParameter containing DSN, Stored Procedure name and Parameter info.
      * @param parentProposalNumber is given as the input parameter to the function.
      * @param childProposalNumber is given as the input parameter to the function.
      * @param childTypeCode is given as the input parameter to the function.
      * @param userId is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public ProcReqParameter joinBudgetToHierarchy(String parentProposalNumber , 
                String childProposalNumber, int childTypeCode, String userId)
                            throws CoeusException, DBException {
        //int copyFlag = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_PARENT_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,parentProposalNumber));
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,childProposalNumber)); 
        param.add(new Parameter("AS_CHILD_TYPE",
                                DBEngineConstants.TYPE_INT, ""+childTypeCode));
        param.add(new Parameter("AS_UPDATE_USER",
                                DBEngineConstants.TYPE_STRING,userId));   
        
        StringBuffer sqlJoinBud = new StringBuffer(
                                        "{ <<OUT INTEGER JOIN_BUD_FLAG>> = call  FN_JOIN_BUDGET_TO_HIERARCHY(");
        sqlJoinBud.append(" <<AS_PARENT_PROPOSAL_NUMBER>> , ");
        sqlJoinBud.append(" <<AS_PROPOSAL_NUMBER>> , ");
        sqlJoinBud.append(" <<AS_CHILD_TYPE>> , ");        
        sqlJoinBud.append(" <<AS_UPDATE_USER>> ) }");
        
        ProcReqParameter procJoinBud  = new ProcReqParameter();
        procJoinBud.setDSN(DSN);
        procJoinBud.setParameterInfo(param);
        procJoinBud.setSqlCommand(sqlJoinBud.toString());
        
        return procJoinBud;
    }// End of joinBudgetToHierarchy
    
    
    /** This method is used to check the locks of childs for the given parent propsoal number.
      * <li>to get locks, it uses the function FN_GET_EPS_PROPOSAL_LOCKS.
      * @return int 
      * @param proposalNumber is given as the input parameter to the function.
      * @isParent specifies whether the proposal is parent or child
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int checkPorposalLocks(String proposalNumber , boolean isParent)
        throws CoeusException,DBException{
            
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int count = -1;
        
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        
        //P - Parent , C - Child
        if(isParent){
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "P")); 
        }else{
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "C"));
        } 
        
        param.addElement(new Parameter("AS_USER",
            DBEngineConstants.TYPE_STRING,userId));

        
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER COUNT>> = "
                + " call FN_GET_PROPOSAL_HIER_LOCKS( << AS_PROPOSAL_NUMBER >> , " 
                + "<< AS_LOCK_TYPE >> , << AS_USER >> ) }", param);
            }catch (DBException dbEx){
               //Code modified for case#3183 - Proposal Hierarchy
               //To display the error message thrown from DB
               //throw new CoeusException(dbEx.getMessage());
               throw new CoeusException(dbEx.getUserMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        
        /*StringBuffer sqlProposalLock = new StringBuffer(
                                        "{ <<OUT INTEGER COUNT>> = call FN_GET_PROPOSAL_HIER_LOCKS(");
        sqlProposalLock.append(" <<AS_PROPOSAL_NUMBER>> , ");
        sqlProposalLock.append(" <<AS_LOCK_TYPE>> , ");
        sqlProposalLock.append(" <<AS_USER>> ) } ");*/
        
        /*ProcReqParameter procPropLock  = new ProcReqParameter();
        procPropLock.setDSN(DSN);
        procPropLock.setParameterInfo(param);
        procPropLock.setSqlCommand(sqlProposalLock.toString());*/
        
        return count;
    }//End of checkLockForPorposal
    
    /** This method is used to release locks of childs for the given parent propsoal number.
      * <li>to release locks, it uses the function FN_REMOVE_PROP_HIER_LOCKS.
      * @return ProcReqParameter.
      * @param proposalNumber is given as the input parameter to the function.
      * @isParent specifies whether the proposal is parent or child
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int releasePorposalLocks(String proposalNumner , boolean isParent)
        throws CoeusException,DBException{
            
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int count = -1;
        
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumner));
        
        //P - Parent , C - Child
        if(isParent){
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "P")); 
        }else{
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "C"));
        }

        param.addElement(new Parameter("AS_USER",
            DBEngineConstants.TYPE_STRING,userId));
        
        
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER COUNT>> = "
                + " call FN_REMOVE_PROP_HIER_LOCKS( << AS_PROPOSAL_NUMBER >> , " 
                + "<< AS_LOCK_TYPE >> , << AS_USER >> ) }", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        
        /*StringBuffer sqlProposalLock = new StringBuffer(
                                        "{ <<OUT INTEGER COUNT>> = call FN_REMOVE_PROP_HIER_LOCKS(");
        sqlProposalLock.append(" <<AS_PROPOSAL_NUMBER>> , ");
        sqlProposalLock.append(" <<AS_LOCK_TYPE>> , ");
        sqlProposalLock.append(" <<AS_USER>> ) } ");
         *
        ProcReqParameter procPropLock  = new ProcReqParameter();
        procPropLock.setDSN(DSN);
        procPropLock.setParameterInfo(param);
        procPropLock.setSqlCommand(sqlProposalLock.toString());*/
        
        return count;
    }//End of releaseLockForPorposal    
    
    /** This method is used to SYNC ALL all the details of the given Proposal except Blob data.
      * <li>To SYNC the data, it uses the function FN_SYNC_PROPOSAL_IN_HIERARCHY.
      *
      * @return int syncFlag indicating whether the copy was successfull.
      * @param parentProposalNumber is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int syncProposal(String parentProposalNumber)
                            throws CoeusException, DBException {
        int syncFlag = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        //Sync proposal
        param.add(new Parameter("AS_PARENT_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,parentProposalNumber));
        param.add(new Parameter("AS_UPDATE_USER",
                                DBEngineConstants.TYPE_STRING,userId));
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER SYNC_FLAG>> = "
                +" call FN_SYNC_PROPOSAL_IN_HIERARCHY(<<AS_PARENT_PROPOSAL_NUMBER>> , " +
                " <<AS_UPDATE_USER>> ) }", param);
                //COEUSDEV-533 - START
                Vector param2 = new Vector();
                param2.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, parentProposalNumber));
                dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER COUNT>> = call FN_REORDER_PROP_PERSON_SORT_ID( <<PROPOSAL_NUMBER>> )}", param2);
                //COEUSDEV-533 - END
            }catch (DBException dbEx){
               throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            syncFlag = Integer.parseInt(rowParameter.get("SYNC_FLAG").toString());
        }
        
        
        /*StringBuffer sqlParentProposal = new StringBuffer(
                                        "{ <<OUT INTEGER SYNC_ALL_FLAG>> = call FN_SYNC_PROPOSAL_IN_HIERARCHY(");
        sqlParentProposal.append(" <<AS_PARENT_PROPOSAL_NUMBER>> , ");
        sqlParentProposal.append(" <<AS_UPDATE_USER>> ) } ");
        
        ProcReqParameter procPropCopy  = new ProcReqParameter();
        procPropCopy.setDSN(DSN);
        procPropCopy.setParameterInfo(param);
        procPropCopy.setSqlCommand(sqlParentProposal.toString());
        procedures.add(procPropCopy);*/
        
        
        /*java.sql.Connection conn = null;
        Vector vecDataFromDB = null;
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                vecDataFromDB = dbEngine.executeStoreProcs(procedures, conn);
                dbEngine.endTxn(conn);
            }catch(Exception ex){
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalHierarchyTxnBean", "syncProposal");
                throw new DBException(ex);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(vecDataFromDB != null && vecDataFromDB.size()>0){
            System.out.println(vecDataFromDB.get(1).getClass());
            
            Vector vecSyncData = (Vector)vecDataFromDB.get(1);
            if(vecSyncData != null && vecSyncData.size()>0){
                HashMap hmReuslt = (HashMap)vecSyncData.get(0);
                syncFlag = Integer.parseInt(hmReuslt.get("SYNC_ALL_FLAG").toString());
            }
        }*/
        
        return syncFlag; 
    }//End of syncProposal
    
    
    /** This method is used to Sync All Blob data of the given Proposal Number.
      * <li>To copy the data, it uses the function FN_SYNC_ALL_CHILD_PROP_BLOBS.
      *
      * @return ProcReqParameter containing DSN, Stored Procedure name and Parameter info.
      * @param parentProposalNumber is given as the input parameter to the function.
      * @param userId is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public CoeusVector syncProposalBlobs(String proposalNumber)
                            throws CoeusException, DBException {
        ProcReqParameter procSyncBlobs = null;
        CoeusVector cvData = new CoeusVector();
        //ProposalPersonBioPDFBean proposalPersonBioPDFBean = getProposalPersonBioPDF(proposalNumber);
        //procSyncBlobs = new ProcReqParameter();
        
        //cvData.add(addProposalPersonBioPDF(proposalPersonBioPDFBean));
        return cvData;
    }// End of syncAllProposalBlobs(..)...
    
    /** Added for prop hierarchy child type start by tarique */
    public CoeusVector getPropHierChildType() throws CoeusException,DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        ProposalHierarchyChildTypeBean proposalHierarchyChildTypeBean;
        CoeusVector types = new CoeusVector();
        if(dbEngine!=null){
            try{
                result = dbEngine.executeRequest("Coeus",
                "call GET_EPS_PROP_HIERCY_CHILD_TYPE  ( <<OUT RESULTSET rset>> )",
                "Coeus", param);
            }catch (DBException dbEx){
               throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap typeRow = (HashMap)result.elementAt(i);
            proposalHierarchyChildTypeBean = new ProposalHierarchyChildTypeBean();
            int typeCode = Integer.parseInt(typeRow.get("CHILD_TYPE_CODE").toString());
            proposalHierarchyChildTypeBean.setProposalHierarchyChildCode(typeCode);
            proposalHierarchyChildTypeBean.setDescription(typeRow.get("DESCRIPTION").toString());
            proposalHierarchyChildTypeBean.setComments((typeRow.get("COMMENTS") == null ? "" :typeRow.get("COMMENTS").toString()));
            types.addElement(proposalHierarchyChildTypeBean);
        }
        return types;
    }
    /** Added for prop hierarchy child type end by tarique */
    
    /** This method is used to update sync flag of proposal/budget when they are saved
      * <li>To copy the data, it uses the function FN_UPDATE_SYNC_FLAGS.
      *
      * @return int count indicating whether the update was successfull.
      * @param proposalNo is given as the input parameter to the function.
      * @param syncFlagType is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int updateSyncFlag(String proposalNo , String syncFlagType)
                            throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("AS_CHILD_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,proposalNo));
        param.add(new Parameter("AS_UPDATE_USER",
                                DBEngineConstants.TYPE_STRING,userId));
        param.add(new Parameter("AS_SYNC_FLAG_TYPE",
                                DBEngineConstants.TYPE_STRING,syncFlagType));
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER count>> = "
                +" call FN_UPDATE_PROP_HIER_SYNC_FLAGS(<<AS_CHILD_PROPOSAL_NUMBER>> , " +
                "<< AS_UPDATE_USER >> ," +
                " <<AS_SYNC_FLAG_TYPE>> ) }", param);
            }catch (DBException dbEx){
               throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("count").toString());
        }
        //return exist;
        /*StringBuffer sqlParentProposal = new StringBuffer(
                                        "{ <<OUT INTEGER JOIN_FLAG>> = call FN_UPDATE_SYNC_FLAGS(");
        sqlParentProposal.append(" <<AS_CHILD_PROPOSAL_NUMBER>> , ");
        sqlParentProposal.append(" <<AS_UPDATE_USER>> , ");
        sqlParentProposal.append(" <<AS_SYNC_FLAG_TYPE>> ) } ");
        
        ProcReqParameter procParameter  = new ProcReqParameter();
        procParameter.setDSN(DSN);
        procParameter.setParameterInfo(param);
        procParameter.setSqlCommand(sqlParentProposal.toString());
        procedures.add(procParameter);
        procedures.add(joinProposalBlobs(parentProposalNumber, childProposalNumber));
        java.sql.Connection conn = null;
        
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                dbEngine.executeStoreProcs(procedures, conn);
                dbEngine.endTxn(conn);
            }catch(Exception ex){
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalHierarchyTxnBean", "joinProposal");
                throw new DBException(ex);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        joinFlag = 1;*/
        return count; 
    }
    
     /** This method is used to check the locks of childs for the given parent propsoal number.
      * <li>To lock, it uses the function FN_GET_PROP_HIER_BUDGET_LOCK.
      * @return a message if anyone child has been locked for the given parent proposal.
      * @param proposalNumner is given as the input parameter to the function.
      * @param isParent specifies whether the proposal is parent or child
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int checkBudgetLocks(String proposalNumber , boolean isParent)
        throws CoeusException,DBException{
            
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int count = -1;
        
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        
        //P - Parent , C - Child
        if(isParent){
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "P")); 
        }else{
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "C"));
        }
        
        param.addElement(new Parameter("AS_USER",
            DBEngineConstants.TYPE_STRING,userId));

        
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER COUNT>> = "
                + " call FN_GET_PROP_HIER_BUDGET_LOCK( << AS_PROPOSAL_NUMBER >> , " 
                + "<< AS_LOCK_TYPE >> , << AS_USER >> ) }", param);
           }catch (DBException dbEx){
               //Code modified for case#3183 - Proposal Hierarchy
               //To display the error message thrown from DB               
               //throw new CoeusException(dbEx.getMessage());
               throw new CoeusException(dbEx.getUserMessage());
           }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        
        /*StringBuffer sqlBudgetLock = new StringBuffer(
                                        "{ <<OUT INTEGER COUNT>> = call FN_GET_PROP_HIER_BUDGET_LOCK(");
        sqlBudgetLock.append(" <<AS_PROPOSAL_NUMBER>> , ");
        sqlBudgetLock.append(" <<AS_LOCK_TYPE>> , ");
        sqlBudgetLock.append(" <<AS_USER>> ) } ");
        
        ProcReqParameter procBudgetLock = new ProcReqParameter();
        procBudgetLock.setDSN(DSN);
        procBudgetLock.setParameterInfo(param);
        procBudgetLock.setSqlCommand(sqlBudgetLock.toString());*/
        
        return count;
    }//End of checkBudgetLocks
    
    /** This method is used to release locks of childs for the given parent propsoal number.
      * <li>To copy the data, it uses the function FN_REMOVE_PROP_HIER_LOCKS.
      * @return a message if anyone child has been locked for the given parent proposal.
      * @param proposalNumber is given as the input parameter to the function.
      * @param isParent specifies whether the proposal is parent or child 
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int releaseBudgetLocks(String proposalNumber , boolean isParent)
        throws CoeusException,DBException{
            
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int count = -1;
        
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        
        //P - Parent , C - Child
        if(isParent){
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "P"));
        }else{
            param.addElement(new Parameter("AS_LOCK_TYPE",
                DBEngineConstants.TYPE_STRING, "C"));
        }
        
        param.addElement(new Parameter("AS_USER",
            DBEngineConstants.TYPE_STRING,userId));

       
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER COUNT>> = "
                + " call FN_REMOVE_BUDGET_HIER_LOCKS( << AS_PROPOSAL_NUMBER >> , " 
                + "<< AS_LOCK_TYPE >> , << AS_USER >> ) }", param);
           }catch (DBException dbEx){
               throw new CoeusException(dbEx.getMessage());
           }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        
        /* StringBuffer sqlBudgetLock = new StringBuffer(
                                        "{ <<OUT INTEGER COUNT>> = call FN_REMOVE_BUDGET_HIER_LOCKS(");
        sqlBudgetLock.append(" <<AS_PROPOSAL_NUMBER>> , ");
        sqlBudgetLock.append(" <<AS_LOCK_TYPE>> , ");
        sqlBudgetLock.append(" <<AS_USER>> ) } ");
        
        ProcReqParameter procBudgetLock = new ProcReqParameter();
        procBudgetLock.setDSN(DSN);
        procBudgetLock.setParameterInfo(param);
        procBudgetLock.setSqlCommand(sqlBudgetLock.toString());*/
        
        return count;
    }//End of releaseBudgetLocks
    
    /** This method is used to SYNC ALL all the details of the given Proposal except Blob data.
      * <li>To SYNC the data, it uses the function FN_SYNC_BUDGET_IN_HIERARCHY.
      *
      * @return int syncFlag indicating whether the copy was successfull.
      * @param parentProposalNumber is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int syncBudget(String parentPropNumber)
                            throws CoeusException, DBException {
        int syncFlag = -1;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        //Check if locks are available , if availble then get the locks.
        /*if(isParent){
            procedures.add(checkBudgetLocks(parentPropNumber , isParent));
        }else{
            procedures.add(checkBudgetLocks(childPropNumber , isParent));
        }*/
        
        //Sync budget
        param.add(new Parameter("AS_PARENT_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,parentPropNumber));
        param.add(new Parameter("AS_UPDATE_USER",
                                DBEngineConstants.TYPE_STRING,userId));
        
        
        /*StringBuffer sqlBudget = new StringBuffer(
                                        "{ <<OUT INTEGER SYNC_ALL_FLAG>> = call FN_SYNC_BUDGET_IN_HIERARCHY(");
        sqlBudget.append(" <<AS_PARENT_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AS_UPDATE_USER>> ) } ");
        
        ProcReqParameter procBudget = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(param);
        procBudget.setSqlCommand(sqlBudget.toString());
        procedures.add(procBudget);
        
        //Release locks
        if(isParent){
            procedures.add(releaseBudgetLocks(parentPropNumber , isParent));
        }else{
            procedures.add(releaseBudgetLocks(childPropNumber , isParent));
        }*/
        
        if(dbEngine!=null){
           try{
               result = dbEngine.executeFunctions("Coeus",
               "{ <<OUT INTEGER COUNT>> = "
               + " call FN_SYNC_BUDGET_IN_HIERARCHY( << AS_PARENT_PROPOSAL_NUMBER >> , " 
               + "<< AS_UPDATE_USER >>) }", param);
           }catch (DBException dbEx){
               throw new CoeusException(dbEx.getMessage());
           }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            syncFlag = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        
        /*java.sql.Connection conn = null;
        
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                dbEngine.executeStoreProcs(procedures, conn);
                dbEngine.endTxn(conn);
            }catch(Exception ex){
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalHierarchyTxnBean", "syncBudget");
                throw new DBException(ex);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }*/
        
        //syncFlag = 1;
        return syncFlag; 
    }
    
    /** This method is used to remove Proposal from Hierarchy
      * <li>To remove child proposal it uses FN_UPDATE_SYNC_FLAGS.
      *
      * @return int count indicating whether the update was successfull.
      * @param parentPropNo is given as the input parameter to the function.
      * @param childPropNo is given as the input parameter to the function.
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public int removePropFromHierarchy(String parentPropNo , String childPropNo)
                            throws CoeusException, DBException {
        int count = -1;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("AS_PARENT_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,parentPropNo));
        param.add(new Parameter("AS_CHILD_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,childPropNo));
        param.add(new Parameter("AS_USER",
                                DBEngineConstants.TYPE_STRING,userId));
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER count>> = "
                +" call FN_REMOVE_CHILD_FROM_HIERARCHY(<<AS_PARENT_PROPOSAL_NUMBER>> , " +
                "<< AS_CHILD_PROPOSAL_NUMBER >> ," +
                " <<AS_USER>> ) }", param);
            }catch (DBException dbEx){
            	//Modified for case# 3183 - Proposal Hierarchy enhancements - start
                //throw new CoeusException(dbEx.getMessage());
                throw new CoeusException(dbEx.getUserMessage());
                //Modified for case# 3183 - Proposal Hierarchy enhancements - end
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("count").toString());
            //Code added for Case#3183 proposal hierarchy. - starts
            if(count == 110006){
                checkLocksAvailable(parentPropNo, false);
            }
            //Code added for Case#3183 proposal hierarchy. - ends
        }
        return count;
    }//End of removePropFromHierarchy
    
    public String getParentPropUnit(String proposalNumber)
    throws CoeusException, DBException{
        
        Vector param= new Vector();
        String unitNumber = null;
        Vector result = null;
        param.addElement(new Parameter("AV_PARENT_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine!=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                "call GET_UNIT_FOR_PARENT_PROPOSAL(<<AV_PARENT_PROPOSAL_NUMBER>> , "
                +" << OUT STRING AV_UNIT_NUMBER >>)", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result != null) {
            int size = result.size();
            for(int i=0;i<size;i++) {
                HashMap hmData = (HashMap)result.get(i);
                unitNumber = (String)hmData.get("OWENED_BY_UNIT");
            }
        }
        return unitNumber ;
    }
    //Modified Method Signature for Proposal Hierarchy Person Module -case 3183
    /**
     *  The method used to fetch the Person Bio PDF's from DB. 
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param String childPropNo child proposalNumber 
     *  @return CoeusVector  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    //public CoeusVector getProposalPersonBioPDF(String parentPropNo , String childPropNo) 
    public CoeusVector getProposalPersonBioPDF(String childPropNo) 
        throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";    
        //Modified for Proposal Hierarchy Person Module -case 3183 - start
        //byte[] fileData = null;
        
        CoeusVector cvData = null;
        
//        parameter.addElement(new Parameter("PARENT_PROPOSAL_NUMBER",
//            DBEngineConstants.TYPE_STRING, parentPropNo));        
        
        parameter.addElement(new Parameter("AV_CHILD_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, childPropNo));
        
        //selectQuery = "SELECT BIO_PDF, BIO_NUMBER , FILE_NAME, PERSON_ID , UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$EPS_PROP_PERSON_BIO_PDF "+
//      "WHERE PROPOSAL_NUMBER = <<CHILD_PROPOSAL_NUMBER>> AND PERSON_ID NOT IN (SELECT PERSON_ID FROM OSP$EPS_PROP_PERSON_BIO_PDF WHERE PROPOSAL_NUMBER = <<PARENT_PROPOSAL_NUMBER>>) ";

        selectQuery = 
        "SELECT BP.BIO_PDF, BP.BIO_NUMBER CHILD_BIO_NUMBER," +
        "       AL.PARENT_MODULE_NUMBER PARENT_BIO_NUMBER, BP.FILE_NAME, BP.PERSON_ID ," +
        " 	BP.UPDATE_USER, BP.UPDATE_TIMESTAMP " +
        "  FROM OSP$EPS_PROP_PERSON_BIO_PDF BP, OSP$EPS_PROP_HIER_ATT_LINKS AL" +
        " WHERE BP.PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>> " +
        " 	AND BP.PROPOSAL_NUMBER = AL.CHILD_PROPOSAL_NUMBER " +
        "   AND AL.LINK_TYPE = 'A' AND AL.CHILD_MODULE_NUMBER = BP.BIO_NUMBER" +
        "   AND AL.PERSON_ID = BP.PERSON_ID "+
        "   AND AL.PARENT_MODULE_NUMBER NOT IN (SELECT BIO_NUMBER" +
        "                                         FROM OSP$EPS_PROP_PERSON_BIO_PDF" +
        "                                        WHERE PROPOSAL_NUMBER = AL.PARENT_PROPOSAL_NUMBER " +
        "                                          AND PERSON_ID = AL.PERSON_ID )";	
        HashMap resultRow = null;
        if(dbEngine!=null){
            try{
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
                if( result!= null &&  result.size() > 0){
                    cvData = new CoeusVector();
                    for(int index = 0 ; index < result.size() ; index++){
                        resultRow = (HashMap)result.get(index);

                        ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
                        proposalPersonBioPDFBean.setPersonId((String)resultRow.get("PERSON_ID"));
                        //Modified for case 3685 - Remove Word icons - start
                        //proposalPersonBioPDFBean.setFileBytes((byte[])resultRow.get("BIO_PDF"));
                        proposalPersonBioPDFBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("BIO_PDF")));
                        //Modifed for case 3685 - Remove Word icons - end
                        proposalPersonBioPDFBean.setFileName((String)resultRow.get("FILE_NAME"));
                        proposalPersonBioPDFBean.setBioNumber(Integer.parseInt(resultRow.get("PARENT_BIO_NUMBER").toString()));
                        proposalPersonBioPDFBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                        proposalPersonBioPDFBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                        cvData.add(proposalPersonBioPDFBean);
                    }
                }
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
            //Modified for Proposal Hierarchy Person Module -case 3183 - end
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return cvData;
    }//End of getProposalPersonBioPDF
    
    
    
    /**
     *  The method used to fetch the Narrative PDF's from DB. 
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param String proposalNumber 
     *  @return CoeusVector
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    //public CoeusVector getProposalNarrativeSource(String parentPropNo , String childPropNo) 
    public CoeusVector getProposalNarrativeSource(String childPropNo) 
        throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";        
        byte[] fileData = null;
        CoeusVector cvData = null;
        
       // parameter.addElement(new Parameter("AV_PARENT_PROPOSAL_NUMBER",
       //     DBEngineConstants.TYPE_STRING, parentPropNo));        
        
        parameter.addElement(new Parameter("AV_CHILD_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, childPropNo));
        
//       selectQuery = "SELECT PROPOSAL_NUMBER, MODULE_NUMBER , NARRATIVE_SOURCE, FILE_NAME , INPUT_TYPE, PLATFORM_TYPE, UPDATE_TIMESTAMP, UPDATE_USER FROM OSP$NARRATIVE_SOURCE "+
//            "WHERE PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>> AND PROPOSAL_NUMBER || '-' || TO_CHAR(MODULE_NUMBER) IN ( SELECT FILE_NAME FROM OSP$NARRATIVE_SOURCE WHERE PROPOSAL_NUMBER = <<AV_PARENT_PROPOSAL_NUMBER>>)";
        
        selectQuery = " SELECT NS.NARRATIVE_SOURCE, NS.MODULE_NUMBER CHILD_MODULE_NUMBER," +
                "               AL.PARENT_MODULE_NUMBER PARENT_MODULE_NUMBER, NS.FILE_NAME, " +
                "               NS.INPUT_TYPE, NS.PLATFORM_TYPE, NS.UPDATE_USER, NS.UPDATE_TIMESTAMP " +
                "       FROM   OSP$NARRATIVE_SOURCE NS, OSP$EPS_PROP_HIER_ATT_LINKS AL " +
                "       WHERE  NS.PROPOSAL_NUMBER = 00003203 " +
                "         AND  NS.PROPOSAL_NUMBER = AL.CHILD_PROPOSAL_NUMBER " +
                "         AND  AL.LINK_TYPE = 'N' AND AL.CHILD_MODULE_NUMBER = NS.MODULE_NUMBER           " +
                "         AND  AL.PARENT_MODULE_NUMBER NOT IN (SELECT MODULE_NUMBER" +
                "                                                FROM   OSP$NARRATIVE_SOURCE" +
                "                                                WHERE PROPOSAL_NUMBER = AL.PARENT_PROPOSAL_NUMBER) " ;
        HashMap resultRow = null;
        if(dbEngine!=null){
        try{
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
                if( result!= null &&  result.size() > 0){
                    cvData = new CoeusVector();
                    for(int index = 0 ; index < result.size() ; index++){
                        resultRow = (HashMap)result.get(index);
                        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
                        //proposalNarrativePDFSourceBean.setProposalNumber((String)resultRow.get("PROPOSAL_NUMBER"));
                        proposalNarrativePDFSourceBean.setModuleNumber(Integer.parseInt(resultRow.get("PARENT_MODULE_NUMBER").toString()));
                        proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_SOURCE"));
                        proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                        proposalNarrativePDFSourceBean.setInputType((String)resultRow.get("INPUT_TYPE"));
                        proposalNarrativePDFSourceBean.setPlatFormType((String)resultRow.get("PLATFORM_TYPE"));
                        proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                        proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                        cvData.add(proposalNarrativePDFSourceBean);
                    }
                }
        }catch (DBException ex){
            //ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
            
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return cvData;
    }//End of getProposalNarrativeSource 
    
/**
     * This method is used to insert Proposal Person Bio PDF Doc into database.
     * @param proposalPersonBioPDFBean ProposalPersonBioPDFBean
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter addProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean) 
        throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        boolean isUpdated = false;
        byte[] fileData = null; 
        Vector procedures = new Vector();
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        ProcReqParameter procPersonBioPDF  = null;
        
        fileData = proposalPersonBioPDFBean.getFileBytes();
        if(fileData!=null){
            String statement = "";
            
            parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getProposalNumber()));
            parameter.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
            parameter.addElement(new Parameter("BIO_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalPersonBioPDFBean.getBioNumber()));
            parameter.addElement(new Parameter("BIO_PDF",
                DBEngineConstants.TYPE_BLOB, proposalPersonBioPDFBean.getFileBytes()));
            parameter.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getFileName()));
            //Modified for Case#3183 - Proposal hierarchy - starts
//            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
//            parameter.addElement(new Parameter("UPDATE_USER",
//                DBEngineConstants.TYPE_STRING, userId));            
            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, proposalPersonBioPDFBean.getUpdateTimestamp()));
            parameter.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getUpdateUser()));
            //Modified for Case#3183 - Proposal hierarchy - ends
            statement = "INSERT INTO OSP$EPS_PROP_PERSON_BIO_PDF (PROPOSAL_NUMBER, PERSON_ID, BIO_NUMBER, BIO_PDF, FILE_NAME, UPDATE_TIMESTAMP, UPDATE_USER ) "+
            " VALUES( <<PROPOSAL_NUMBER>>, <<PERSON_ID>> ,<<BIO_NUMBER>> , <<BIO_PDF>> , <<FILE_NAME>>, <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>> )";
            
            procPersonBioPDF  = new ProcReqParameter();
            procPersonBioPDF.setDSN(DSN);
            procPersonBioPDF.setParameterInfo(parameter);
            procPersonBioPDF.setSqlCommand(statement);
        }

        return procPersonBioPDF;            
    }//End of addProposalPersonBioPDF            
    
    
    
    /**
     * This method is used to insert Proposal Narrative Source into database.
     * @param proposalPersonBioPDFBean ProposalPersonBioPDFBean
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter addProposalNarrativeSource(ProposalNarrativePDFSourceBean sourceBean)
    throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        boolean isUpdated = false;
        byte[] fileData = null;
        Vector procedures = new Vector();
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        ProcReqParameter procPersonBioPDF  = null;
        
        fileData = sourceBean.getFileBytes();
        if(fileData!=null){
            String statement = "";
            
            parameter.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, sourceBean.getProposalNumber()));
            parameter.addElement(new Parameter("MODULE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+sourceBean.getModuleNumber()));
            parameter.addElement(new Parameter("NARRATIVE_SOURCE",
            DBEngineConstants.TYPE_BLOB, sourceBean.getFileBytes()));
            parameter.addElement(new Parameter("FILE_NAME",
            DBEngineConstants.TYPE_STRING, sourceBean.getFileName()));
            parameter.addElement(new Parameter("INPUT_TYPE",
            DBEngineConstants.TYPE_STRING, sourceBean.getInputType()));
            parameter.addElement(new Parameter("PLATFORM_TYPE",
            DBEngineConstants.TYPE_STRING, sourceBean.getPlatFormType()));
            //Modified for Case#3183 - Proposal hierarchy - starts
//            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
//            parameter.addElement(new Parameter("UPDATE_USER",
//            DBEngineConstants.TYPE_STRING, userId));            
            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, sourceBean.getUpdateTimestamp()));
            parameter.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, sourceBean.getUpdateUser()));
//            statement = "UPDATE OSP$NARRATIVE_SOURCE SET FILE_NAME = <<FILE_NAME>>, NARRATIVE_SOURCE = <<NARRATIVE_SOURCE>>, "+
//                " UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>>, UPDATE_USER = <<UPDATE_USER>> "+"WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND MODULE_NUMBER = <<MODULE_NUMBER>>";
            
            statement = "INSERT INTO OSP$NARRATIVE_SOURCE (PROPOSAL_NUMBER, MODULE_NUMBER, FILE_NAME, INPUT_TYPE, PLATFORM_TYPE, UPDATE_TIMESTAMP, UPDATE_USER, NARRATIVE_SOURCE ) "+
                    " VALUES( <<PROPOSAL_NUMBER>>, <<MODULE_NUMBER>> , <<FILE_NAME>> , <<INPUT_TYPE>>, <<PLATFORM_TYPE>>, <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>>, <<NARRATIVE_SOURCE>> )";

            //Modified for Case#3183 - Proposal hierarchy - ends
            procPersonBioPDF  = new ProcReqParameter();
            procPersonBioPDF.setDSN(DSN);
            procPersonBioPDF.setParameterInfo(parameter);
            procPersonBioPDF.setSqlCommand(statement);
        }
        
        return procPersonBioPDF;
    }//End of addProposalNarrativePDF
    
    
    //Modified Method Signature for Proposal Hierarchy Person Module -case 3183
    /**
     *  The method used to fetch the Person Bio PDF's from DB. 
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param String childPropNo child proposal number
     *  @return CoeusVector
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    //public CoeusVector getProposalPersonBioSource(String parentPropNo , String childPropNo) 
    public CoeusVector getProposalPersonBioSource(String childPropNo) 
        throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";  
        //Modified for Proposal Hierarchy Person Module -case 3183 - start
         byte[] fileData = null;
        
        CoeusVector cvData = null;
        
//        parameter.addElement(new Parameter("PARENT_PROPOSAL_NUMBER",
//            DBEngineConstants.TYPE_STRING, parentPropNo));        
        
        parameter.addElement(new Parameter("AV_CHILD_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, childPropNo));
        
//        selectQuery = "SELECT BIO_SOURCE, PERSON_ID , BIO_NUMBER , FILE_NAME, SOURCE_EDITOR, INPUT_TYPE, PLATFORM_TYPE, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$EPS_PROP_PERSON_BIO_SOURCE "+
//            "WHERE PROPOSAL_NUMBER = <<CHILD_PROPOSAL_NUMBER>> AND PERSON_ID NOT IN (SELECT PERSON_ID FROM OSP$EPS_PROP_PERSON_BIO_SOURCE WHERE PROPOSAL_NUMBER = <<PARENT_PROPOSAL_NUMBER>>)";
//  
        selectQuery = 
        " SELECT BP.BIO_SOURCE, BP.BIO_NUMBER CHILD_BIO_NUMBER," +
        "        AL.PARENT_MODULE_NUMBER PARENT_BIO_NUMBER, BP.FILE_NAME, BP.PERSON_ID ," +
        "        BP.SOURCE_EDITOR, BP.INPUT_TYPE, BP.PLATFORM_TYPE, BP.UPDATE_USER, BP.UPDATE_TIMESTAMP " +
        " FROM   OSP$EPS_PROP_PERSON_BIO_SOURCE BP, OSP$EPS_PROP_HIER_ATT_LINKS AL" +
        " WHERE  BP.PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>> " +
        "   AND  BP.PROPOSAL_NUMBER = AL.CHILD_PROPOSAL_NUMBER " +
        "   AND  AL.LINK_TYPE = 'A' AND AL.CHILD_MODULE_NUMBER = BP.BIO_NUMBER" +
        "   AND  AL.PERSON_ID = BP.PERSON_ID " +
        "   AND  AL.PARENT_MODULE_NUMBER NOT IN (SELECT BIO_NUMBER" +
        "                                       FROM   OSP$EPS_PROP_PERSON_BIO_SOURCE" +
        "                                       WHERE PROPOSAL_NUMBER = AL.PARENT_PROPOSAL_NUMBER " +
        "                                         AND PERSON_ID = AL.PERSON_ID )";
        HashMap resultRow = null;
        if(dbEngine!=null){
            try{
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
                if( result!= null &&  result.size() > 0){
                    cvData = new CoeusVector();
                    for (int index = 0 ; index < result.size() ; index++){
                        resultRow = (HashMap)result.get(index);
                        ProposalPersonBioSourceBean proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
                        proposalPersonBioSourceBean.setFileBytes((byte[])resultRow.get("BIO_SOURCE"));
                        proposalPersonBioSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                        proposalPersonBioSourceBean.setPersonId((String)resultRow.get("PERSON_ID"));
                        proposalPersonBioSourceBean.setBioNumber(Integer.parseInt(resultRow.get("PARENT_BIO_NUMBER").toString()));
                        proposalPersonBioSourceBean.setSourceEditor((String)resultRow.get("SOURCE_EDITOR"));
                        proposalPersonBioSourceBean.setInputType(resultRow.get("INPUT_TYPE") == null ? ' ' : resultRow.get("INPUT_TYPE").toString().charAt(0));
                        proposalPersonBioSourceBean.setPlatformType(resultRow.get("PLATFORM_TYPE") == null ? ' ' : resultRow.get("PLATFORM_TYPE").toString().charAt(0));
                        proposalPersonBioSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                        proposalPersonBioSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                        cvData.add(proposalPersonBioSourceBean);
                    }
                }
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
            //Modified for Proposal Hierarchy Person Module -case 3183 - end
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return cvData;
    }//End of getProposalPersonBioSource
    
    /**
     * This method is used to insert Proposal Person Bio Source Doc into database.
     * @param proposalPersonBioSourceBean ProposalPersonBioSourceBean
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter addProposalPersonBioSource(ProposalPersonBioSourceBean proposalPersonBioPDFBean) 
        throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        byte[] fileData = null; 
        Vector procedures = new Vector(3,2);        
        
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        ProcReqParameter procReqParameter = null;
        
        fileData = proposalPersonBioPDFBean.getFileBytes();
        if(fileData!=null){
            String statement = "";

            parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getProposalNumber()));                    
            parameter.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
            parameter.addElement(new Parameter("BIO_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalPersonBioPDFBean.getBioNumber()));
            parameter.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getFileName()));
            parameter.addElement(new Parameter("SOURCE_EDITOR",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getSourceEditor()));
            parameter.addElement(new Parameter("INPUT_TYPE",
                DBEngineConstants.TYPE_STRING, ""+proposalPersonBioPDFBean.getInputType()));
            parameter.addElement(new Parameter("PLATFORM_TYPE",
                DBEngineConstants.TYPE_STRING, ""+proposalPersonBioPDFBean.getPlatformType()));
            //Modified for Case#3183 - Proposal hierarchy - starts
//            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
//            parameter.addElement(new Parameter("UPDATE_USER", 
//                DBEngineConstants.TYPE_STRING, userId));            
            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, proposalPersonBioPDFBean.getUpdateTimestamp()));
            parameter.addElement(new Parameter("UPDATE_USER", 
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getUpdateUser()));
            //Modified for Case#3183 - Proposal hierarchy - ends
            parameter.addElement(new Parameter("BIO_SOURCE", 
                DBEngineConstants.TYPE_BLOB, proposalPersonBioPDFBean.getFileBytes()));                    

            statement = "INSERT INTO OSP$EPS_PROP_PERSON_BIO_SOURCE (PROPOSAL_NUMBER, PERSON_ID, BIO_NUMBER, FILE_NAME, SOURCE_EDITOR, INPUT_TYPE, PLATFORM_TYPE, UPDATE_TIMESTAMP, UPDATE_USER, BIO_SOURCE ) "+
                    " VALUES( <<PROPOSAL_NUMBER>>, <<PERSON_ID>> , <<BIO_NUMBER>> , <<FILE_NAME>> , <<SOURCE_EDITOR>>, <<INPUT_TYPE>>, <<PLATFORM_TYPE>>, <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>>, <<BIO_SOURCE>> )";

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(parameter);
            procReqParameter.setSqlCommand(statement);

        }
        
        return procReqParameter;            
    }
    
    
    /**
     *  The method used to fetch the Narrative PDF's from DB. 
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param String proposalNumber 
     *  @return CoeusVector
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
   // public CoeusVector getProposalNarrativePdf(String parentPropNo , String childPropNo) 
     public CoeusVector getProposalNarrativePdf(String childPropNo) 
        throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";        
        byte[] fileData = null;
        CoeusVector cvData = null; 
        
       // parameter.addElement(new Parameter("AV_PARENT_PROPOSAL_NUMBER",
         //   DBEngineConstants.TYPE_STRING, parentPropNo));        
        
        parameter.addElement(new Parameter("AV_CHILD_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, childPropNo));
        
        
//       selectQuery = "SELECT PROPOSAL_NUMBER, MODULE_NUMBER , NARRATIVE_PDF, FILE_NAME , UPDATE_TIMESTAMP, UPDATE_USER FROM OSP$NARRATIVE_PDF "+
//            "WHERE PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>> AND PROPOSAL_NUMBER || '-' || TO_CHAR(MODULE_NUMBER) IN ( SELECT FILE_NAME FROM OSP$NARRATIVE_PDF WHERE PROPOSAL_NUMBER = <<AV_PARENT_PROPOSAL_NUMBER>>)";
        
        selectQuery = " SELECT NP.NARRATIVE_PDF, NP.MODULE_NUMBER CHILD_MODULE_NUMBER," +
                "               AL.PARENT_MODULE_NUMBER PARENT_MODULE_NUMBER, NP.FILE_NAME, " +
                "               NP.UPDATE_USER, NP.UPDATE_TIMESTAMP " +
                "       FROM   OSP$NARRATIVE_PDF NP, OSP$EPS_PROP_HIER_ATT_LINKS AL " +
                "       WHERE  NP.PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>> " +
                "         AND  NP.PROPOSAL_NUMBER = AL.CHILD_PROPOSAL_NUMBER " +
                "         AND  AL.LINK_TYPE = 'N' AND AL.CHILD_MODULE_NUMBER = NP.MODULE_NUMBER           " +
                "         AND  AL.PARENT_MODULE_NUMBER NOT IN (SELECT MODULE_NUMBER" +
                "                                                FROM   OSP$NARRATIVE_PDF" +
                "                                                WHERE PROPOSAL_NUMBER = AL.PARENT_PROPOSAL_NUMBER) " ;
        HashMap resultRow = null;
        if(dbEngine!=null){
        try{
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
                if( result!= null &&  result.size() > 0){
                    cvData = new CoeusVector();
                    for(int index = 0 ; index < result.size() ; index++){
                        resultRow = (HashMap)result.get(index);
                        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
                        proposalNarrativePDFSourceBean.setProposalNumber((String)resultRow.get("PROPOSAL_NUMBER"));
                        proposalNarrativePDFSourceBean.setModuleNumber(Integer.parseInt(resultRow.get("PARENT_MODULE_NUMBER").toString()));
                        //Commented for case 3685 - Remove Word icons - start
                        //proposalNarrativePDFSourceBean.setFileBytes((byte[])resultRow.get("NARRATIVE_PDF"));
                        proposalNarrativePDFSourceBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("NARRATIVE_PDF")));
                        //Commented for case 3685 - Remove Word icons - end
                        proposalNarrativePDFSourceBean.setFileName((String)resultRow.get("FILE_NAME"));
                        proposalNarrativePDFSourceBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                        proposalNarrativePDFSourceBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                        cvData.add(proposalNarrativePDFSourceBean);
                    }
                }
        }catch (DBException ex){
            //ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
            
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return cvData;
    }//End of getProposalNarrativeSource 
    
    
    
    /**
     * This method is used to insert Proposal Narrative PDF into database.
     * @param proposalPersonBioPDFBean ProposalPersonBioPDFBean
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter addProposalNarrativePdf(ProposalNarrativePDFSourceBean sourceBean)
    throws CoeusException, DBException {
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        boolean isUpdated = false;
        byte[] fileData = null;
        Vector procedures = new Vector();
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        ProcReqParameter procPersonBioPDF  = null;
        
        fileData = sourceBean.getFileBytes();
        if(fileData!=null){
            String statement = "";
            
            parameter.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, sourceBean.getProposalNumber()));
            parameter.addElement(new Parameter("MODULE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+sourceBean.getModuleNumber()));
            parameter.addElement(new Parameter("NARRATIVE_PDF",
            DBEngineConstants.TYPE_BLOB, sourceBean.getFileBytes()));
            parameter.addElement(new Parameter("FILE_NAME",
            DBEngineConstants.TYPE_STRING, sourceBean.getFileName()));
            //Modified for Case#3183 - Proposal hierarchy - starts
//            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
//            parameter.addElement(new Parameter("UPDATE_USER",
//            DBEngineConstants.TYPE_STRING, userId))            
            parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, sourceBean.getUpdateTimestamp()));
            parameter.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, sourceBean.getUpdateUser()));
                     
//            statement = "UPDATE OSP$NARRATIVE_PDF SET FILE_NAME = <<FILE_NAME>>, NARRATIVE_PDF = <<NARRATIVE_PDF>>, "+
//                " UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>>, UPDATE_USER = <<UPDATE_USER>> "+"WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND MODULE_NUMBER = <<MODULE_NUMBER>>";

             statement = "INSERT INTO OSP$NARRATIVE_PDF (PROPOSAL_NUMBER, MODULE_NUMBER, FILE_NAME, UPDATE_TIMESTAMP, UPDATE_USER, NARRATIVE_PDF ) "+
                    " VALUES( <<PROPOSAL_NUMBER>>, <<MODULE_NUMBER>> , <<FILE_NAME>> , <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>>, <<NARRATIVE_PDF>> )";
   
            //Modified for Case#3183 - Proposal hierarchy - ends   
            procPersonBioPDF  = new ProcReqParameter();
            procPersonBioPDF.setDSN(DSN);
            procPersonBioPDF.setParameterInfo(parameter);
            procPersonBioPDF.setSqlCommand(statement);
        }
        
        return procPersonBioPDF;
    }//End of addProposalNarrativePDF
    
    
    public boolean isBudgetComplete(String proposalNumber) throws CoeusException, DBException{
         Vector result = new Vector(3,2);
        Vector param= new Vector();
        Vector data = null;
        boolean isComplete = false;
        int code = 0;
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER IS_COMPLETE>> = "
                +" call FN_IS_BUDGET_STATUS_COMPLETE( << AS_PROPOSAL_NUMBER >> ) }", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            code = Integer.parseInt(rowParameter.get("IS_COMPLETE").toString());
            if(code == 1){
                isComplete = true;
            }
        }
        return isComplete;
    }
    
     /** Added for Proposal Hierarchy Enhancement Case# 3183
     * Update the Proposal Hierarchy link data        
     * @param ProposalHierarchyLinkBean
     * @return ProcReqParameter
     * @throws CoeusException and DBException
     */
    public ProcReqParameter updatePropHierLinkData( ProposalHierarchyLinkBean
    proposalHierarchyLinkBean, String acType)  throws CoeusException ,DBException{
        
        Vector paramProposalHierLink= new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        paramProposalHierLink.addElement(new Parameter("PARENT_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalHierarchyLinkBean.getParentProposalNumber()));
        paramProposalHierLink.addElement(new Parameter("PARENT_MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+proposalHierarchyLinkBean.getParentModuleNumber()));
        paramProposalHierLink.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING, proposalHierarchyLinkBean.getPersonId()));
        paramProposalHierLink.addElement(new Parameter("CHILD_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalHierarchyLinkBean.getChildProposalNumber()));
        paramProposalHierLink.addElement(new Parameter("CHILD_MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+proposalHierarchyLinkBean.getChildModuleNumber()));
        paramProposalHierLink.addElement(new Parameter("DOCUMENT_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+proposalHierarchyLinkBean.getDocumentTypeCode()));
        paramProposalHierLink.addElement(new Parameter("LINK_TYPE",
        DBEngineConstants.TYPE_STRING, proposalHierarchyLinkBean.getLinkType()));
        paramProposalHierLink.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProposalHierLink.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        paramProposalHierLink.addElement(new Parameter("AW_PARENT_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalHierarchyLinkBean.getParentProposalNumber()));
        paramProposalHierLink.addElement(new Parameter("AW_PARENT_MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+proposalHierarchyLinkBean.getParentModuleNumber()));
        paramProposalHierLink.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, acType));
        
        StringBuffer sqlPersonBio = new StringBuffer(
        "call UPD_EPS_PROP_HIER_ATT_LINKS(");
        sqlPersonBio.append(" <<PARENT_PROPOSAL_NUMBER>> , ");
        sqlPersonBio.append(" <<PARENT_MODULE_NUMBER>> , ");
        sqlPersonBio.append(" <<PERSON_ID>> , ");
        sqlPersonBio.append(" <<CHILD_PROPOSAL_NUMBER>> , ");
        sqlPersonBio.append(" <<CHILD_MODULE_NUMBER>> , ");
        sqlPersonBio.append(" <<DOCUMENT_TYPE_CODE>> , ");
        sqlPersonBio.append(" <<LINK_TYPE>> , ");
        sqlPersonBio.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonBio.append(" <<UPDATE_USER>> , ");
        sqlPersonBio.append(" <<AW_PARENT_PROPOSAL_NUMBER>> , ");
        sqlPersonBio.append(" <<AW_PARENT_MODULE_NUMBER>> , ");
        sqlPersonBio.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procPersonBio  = new ProcReqParameter();
        procPersonBio.setDSN(DSN);
        procPersonBio.setParameterInfo(paramProposalHierLink);
        procPersonBio.setSqlCommand(sqlPersonBio.toString());
        
        return procPersonBio;
    } //Added for Proposal Hierarchy Enhancement Case# 3183 - End    
    
    public void syncBlobData(String parentProposalNumber,
        String childProposalNumber)throws CoeusException, DBException{
        //Join Blobs Parameter
        CoeusVector cvJoinBlobData = joinProposalBlobs(parentProposalNumber , childProposalNumber);
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                if(cvJoinBlobData != null && cvJoinBlobData.size()>0){
                    for(int index = 0; index < cvJoinBlobData.size() ; index++){
                        ProcReqParameter procReqParameter = (ProcReqParameter)cvJoinBlobData.get(index);
                        dbEngine.batchSQLUpdate(procReqParameter , conn);
                    }
                }
            }catch(Exception ex){
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalHierarchyTxnBean", "joinProposal");
                throw new CoeusException(ex.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
        //Added for case id :3183 - start
    /** 
     * Checks whether any user is using the parent proposal or its budget or
     * narrative.
     * @return true if no user is using it else throws LockingException
     */
    public boolean checkLocksAvailable(String parentProposalNumber, boolean isParentProposal)
    throws CoeusException, DBException, LockingException{
        boolean locksAvailable = true;
        transMon = TransactionMonitor.getInstance();
        String errorMessage = "";
        String lockId = "";
        LockingBean lockingBean = null;
        
        //Check for proposal lock only if it is a child proposal
        if(!isParentProposal){
            lockId = "osp$Development Proposal_"+parentProposalNumber;
            locksAvailable = transMon.isLockAvailable(lockId);
            if(!locksAvailable){
                lockingBean = getLockDetails(lockId);
            }
        }
        //Check for narrative lock only if proposal is not locked
        if(locksAvailable){
            lockId = "osp$Narrative_"+parentProposalNumber;
            locksAvailable = transMon.isLockAvailable(lockId);
            if(!locksAvailable){
                lockingBean = getLockDetails(lockId);
            }
        }
        //Check for budget lock only if narrtive is not locked
        if(locksAvailable){
            lockId = "osp$Budget_"+parentProposalNumber;
            locksAvailable = transMon.isLockAvailable(lockId);
            if(!locksAvailable){
                lockingBean = getLockDetails(lockId);
            }
        }
        
        if(!locksAvailable){
            CoeusMessageResourcesBean coeusMessageResourcesBean =
            new CoeusMessageResourcesBean();
            String strModName = lockId.substring(4,lockId.indexOf("_"));
            errorMessage = "Cannot perform the action since the " +
                strModName +" for the Proposal " +parentProposalNumber+ " is locked by the User "+
                lockingBean.getUpdateUserName();
            throw new LockingException(errorMessage);
        }
        return locksAvailable;
    }
    /**
     * Gets the lock details 
     * @rowId lockId in the db
     * @return LockingBean
     */
    public LockingBean getLockDetails(String rowId)throws
    CoeusException, DBException{
        HashMap hmRow=null;
        Vector result=null;
        Vector param=new Vector();
        LockingBean lockingBean = null;
        param.addElement(new Parameter("AV_LOCK_ID",
        DBEngineConstants.TYPE_STRING, rowId));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call PKG_LOCK.GET_LOCK_DETAILS (  <<AV_LOCK_ID>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize=result.size();
        if(listSize>0){
            hmRow=(HashMap)result.elementAt(0);
            lockingBean = new LockingBean();
            lockingBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
            lockingBean.setUpdateUserName(getUserName(lockingBean.getUpdateUser()));
        }
        return lockingBean;
    }
    /**
     * Gets the userName from the db given the userId
     * @param userId
     * @return String
     */
    public String getUserName(String userId)
    throws CoeusException, DBException{
        Vector vcParam = new Vector();
        Vector result = null;
        
        vcParam.addElement(new Parameter("USER_ID",
        DBEngineConstants.TYPE_STRING, userId));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT STRING USER_NAME>>=call FN_GET_USER_NAME ( "
            + " << USER_ID >>)}", vcParam);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        String userName = userId;
        if(!result.isEmpty()){
            HashMap hmUserData=(HashMap)result.elementAt(0);
            userName = (String)hmUserData.get("USER_NAME");
            userName = (userName==null || userName.equals(""))? userId :userName ;
        }
        return userName;
    }
    //Added for case 3685 - Remove Word icons - start
    /**
     * Converts ByteArrayOutputStream to array of bytes
     *
     * @param ByteArrayOutputStream
     * @return byte[]
     */
    private byte[] convert(ByteArrayOutputStream baos){
        byte[] byteArray = null;
        try {
          byteArray = baos.toByteArray();                  
        }finally{   
           try {
              baos.close();
           }catch (IOException ioex){}   
        }
        return byteArray;
    }
    //Added for case 3685 - Remove Word icons - end
    
    //Commented for Proposal Hierarchy Person Module enhancement case# 3183 - start
    //Included the logic into the methods getProposalPersonBioPDF() and getProposalPersonBioSource()
    /**
     * The bionumber of the child proposal records are changed to the correct parent
     * bio numbers
     * @param  cvBioData
     * @param childPropNo
     * @param isPDF boolean
     */
    /*public CoeusVector getParentBioNumber(CoeusVector cvBioData, String childPropNo, boolean isPDF)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";        
        byte[] fileData = null;
        CoeusVector cvDataObjects = null;
        cvBioData = (cvBioData==null)? new CoeusVector() : cvBioData;
        parameter.addElement(new Parameter("AV_CHILD_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, childPropNo));
        if(isPDF){
            selectQuery  = "select bp.BIO_NUMBER CHILD_BIO_NUMBER," +
            " AL.PARENT_MODULE_NUMBER PARENT_BIO_NUMBER, bp.FILE_NAME, bp.PERSON_ID ," +
            " bp.UPDATE_USER, bp.UPDATE_TIMESTAMP " +
            " from OSP$EPS_PROP_PERSON_BIO_PDF bp, osp$eps_prop_hier_att_links al" +
            "  where bp.PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>> " +
            " and bp.PROPOSAL_NUMBER = al.CHILD_PROPOSAL_NUMBER " +
            " and al.LINK_TYPE = 'A' and al.CHILD_MODULE_NUMBER = bp.BIO_NUMBER";
        }else{ 
             selectQuery  = "select bp.BIO_NUMBER CHILD_BIO_NUMBER," +
            " AL.PARENT_MODULE_NUMBER PARENT_BIO_NUMBER, bp.FILE_NAME, bp.PERSON_ID ," +
            " bp.UPDATE_USER, bp.UPDATE_TIMESTAMP " +
            " from OSP$EPS_PROP_PERSON_BIO_SOURCE bp, osp$eps_prop_hier_att_links al" +
            "  where bp.PROPOSAL_NUMBER = <<AV_CHILD_PROPOSAL_NUMBER>> " +
            " and bp.PROPOSAL_NUMBER = al.CHILD_PROPOSAL_NUMBER " +
            " and al.LINK_TYPE = 'A' and al.CHILD_MODULE_NUMBER = bp.BIO_NUMBER";
        }
        HashMap resultRow = null;
        if(dbEngine!=null){
        try{
                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
                if( result!= null &&  result.size() > 0){
                    int childBioNumber = 0;
                    for(int index = 0 ; index < cvBioData.size() ; index++){
                        if(isPDF){
                            ProposalPersonBioPDFBean sourceBean = (ProposalPersonBioPDFBean)cvBioData.get(index);
                            childBioNumber = sourceBean.getBioNumber();
                        }else{
                            ProposalPersonBioSourceBean sourceBean = (ProposalPersonBioSourceBean)cvBioData.get(index);
                            childBioNumber = sourceBean.getBioNumber();
                        }
                        
                        for(int i=0; i< result.size(); i++){
                            resultRow = (HashMap)result.get(i);
                            int resultChildBioNumber = new Integer(resultRow.get("CHILD_BIO_NUMBER").toString()).intValue();
                            if(childBioNumber == resultChildBioNumber){
                                int resultParentBioNumber = Integer.parseInt(resultRow.get("PARENT_BIO_NUMBER").toString());
				 if(isPDF){
                                    ((ProposalPersonBioPDFBean)cvBioData.get(index)).setBioNumber(resultParentBioNumber);
                                }else{
                                    ((ProposalPersonBioSourceBean)cvBioData.get(index)).setBioNumber(resultParentBioNumber);
                                }
                                break;
                            }
                        }
                    }
                }
        }catch (DBException ex){
            ex.printStackTrace();
        }
            
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return cvBioData;
    }*/
   //Commented for Proposal Hierarchy Person Module enhancement case# 3183 - end
    //Added for case id :3183 - end
}