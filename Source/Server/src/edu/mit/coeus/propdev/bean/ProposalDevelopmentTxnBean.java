/*
 * @(#)ProposalDevelopmentTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 13-JULY-2011 by Bharati
 */


package edu.mit.coeus.propdev.bean;


import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.rolodexmaint.bean.*;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;

//import edu.mit.coeus.utils.question.bean.YNQBean;
import edu.mit.coeus.utils.question.bean.QuestionListBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.unit.bean.UnitDataTxnBean;
//import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingBean;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Comparator;
import java.util.TreeSet;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Connection;
//import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.businessrules.bean.BusinessRulesBean;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;
import edu.mit.coeus.sponsormaint.bean.SponsorFormsBean;
import edu.mit.coeus.utils.KeyConstants;
//import edu.utk.coeuslite.propdev.bean.rightPersonBean;
//import java.util.StringTokenizer;
/**
 * This class provides the methods for performing all procedure executions for
 * a Proposal development functionality. Various methods are used to fetch
 * the Proposal development details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on March 10, 2003, 9:50 AM
 * @author  Mukundan C
 */

public class ProposalDevelopmentTxnBean implements TypeConstants{
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    //Connection instance added by Shivakumar for locking enhancement
    Connection conn = null;

    RolodexMaintenanceDataTxnBean rolodexDataTxnBean =
            new RolodexMaintenanceDataTxnBean();
    SponsorMaintenanceDataTxnBean spTxnBean =
            new SponsorMaintenanceDataTxnBean();
    DepartmentPersonTxnBean departmentTxnBean = new DepartmentPersonTxnBean();
    private TransactionMonitor transMon;
    private static final String rowLockStr = "osp$Development Proposal_";
    private static final String routingLockStr = "osp$Proposal Routing_";

    //Added for case 2604 - Organizations and Locations - start
    private final int PROPOSAL_ORGANIZATION = 1;
    private final int PERFORMING_ORGANIZATION = 2;
    //Added for case 2604 - Organizations and Locations - end

    //Added for COEUSQA-2984 : Statuses in special review - start
    private static final int PROPOSAL_SUBMITTED_STATUS = 5;
    //Added for COEUSQA-2984 : Statuses in special review - end

    //ppc certify flag for key person starts
    private static final String ENABLE_PROP_PERSON_SELF_CERTIFY="ENABLE_PROP_PERSON_SELF_CERTIFY";
    //ppc certify flag for key person ends

    /** Creates a new instance of ProposalDevelopmentTxnBean */
    public ProposalDevelopmentTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }


    /**
     *  This method used get count for the proposal number
     *  <li>To fetch the data, it uses the function FN_PROPOSAL_OVERRIDES_EXISTS.
     *
     *  @return int count for the proposal number
     *  @param String proposal number to get proposal count
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getProposalCount(String proposalNumber)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_PROPOSAL_OVERRIDES_EXISTS(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }

    /**
     *  This method used get custom data count for the proposal number
     *  <li>To fetch the data, it uses the function FN_DEV_PROP_HAS_CUSTOM_DATA.
     *
     *  @return int count for the proposal number
     *  @param String proposal number to get custom data count
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getProposalCustomCount(String proposalNumber)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_DEV_PROP_HAS_CUSTOM_DATA(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }
    /**
     *  This method used get person bio graphy number for the person id
     *  <li>To fetch the data, it uses the function FN_GET_COUNT_FOR_PERSON_BIO.
     *
     *  @return int bio graphy number for the person id
     *  @param String person id to get biography number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getPersonBiographyNumber(String proposalId, String personId)
    throws CoeusException, DBException {

        int number = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalId));
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NUMBER>> = "
                    +" call GET_MAX_FOR_PROP_PER_BIO( <<PROPOSAL_NUMBER>>, << PERSON_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("NUMBER").toString());
        }
        return number;
    }
    /**
     *  This method used get user has proposal right count for the userid,proposal number
     *  and right id.
     *  <li>To fetch the data, it uses the function FN_USER_HAS_PROP_RIGHT.
     *
     *  @return int count for the user id ,proposal number and right id
     *  @param String user id ,proposal number and right id to get proposal right count
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getUserHasPropRightCount(String userId,String proposalNumber,String rightId)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,userId.trim()));
        param.add(new Parameter("RIGHT_ID",
                DBEngineConstants.TYPE_STRING,rightId.trim()));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_USER_HAS_PROP_RIGHT( "
                    +" <<USER_ID>>,<< PROPOSAL_NUMBER >>,<<RIGHT_ID>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }

    //Added by shiji for right checking - start
    public boolean isUserHasRight(String user,String unitNumber,
            String rightId) throws CoeusException, DBException {
        int userRight = 0;
        boolean hasRight =false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AW_USER_ID",DBEngineConstants.TYPE_STRING,user));
        param.add(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("AW_RIGHT_ID",
                DBEngineConstants.TYPE_STRING,rightId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER RIGHTEXISTS>> = call fn_user_has_right(<< AW_USER_ID >>,"+
                    "<< AW_UNIT_NUMBER >> , << AW_RIGHT_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowRolodexId = (HashMap)result.elementAt(0);
            userRight = Integer.parseInt(
                    rowRolodexId.get("RIGHTEXISTS").toString());
        }
        if ( userRight ==1 )  {
            hasRight = true;
        }else if ( userRight == 0 )    {
            hasRight =false;
        }
        return hasRight;
    }
    //right checking - end

    /**
     *  This method used get custom data element count for the module code
     *  <li>To fetch the data, it uses the function FN_MODULE_HAS_CUSTOM_DATA.
     *
     *  @return int count for the module code
     *  @param integer module code to get custom data element count
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getCustomDataElementCount(int moduleCode)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(moduleCode).toString()));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_MODULE_HAS_CUSTOM_DATA(<< MODULE_CODE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }

    /**
     *  This method used to generate the new proposal number when ever new proposal
     *  is generated
     *  <li>To fetch the data, it uses the function FN_GET_NEXT_PROPOSAL_NUMBER.
     *
     *  @return string proposal number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getNextProposalNumber()
    throws CoeusException, DBException {
        String proposalNumber = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PARAMATER",DBEngineConstants.TYPE_STRING,param));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING PROPOSAL_NUMBER>> = "
                    +" call FN_GET_NEXT_PROPOSAL_NUMBER( ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap proposalNumberrow = (HashMap)result.elementAt(0);
            proposalNumber = proposalNumberrow.get("PROPOSAL_NUMBER").toString();
        }
        return proposalNumber;
    }

    /**
     *  This method used populates combo box with Proposal types item in the
     *  Proposal development screen from OSP$PROPOSAL_TYPE.
     *  <li>To fetch the data, it uses the procedure GET_PROPOSAL_TYPE_LIST_DW.
     *
     *  @return Vector map of all Proposal types with Proposal
     *  item types code as key and proposal item type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector vecProposalTypes = null;
        Vector param= new Vector();
        HashMap hasProposalTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_TYPE_LIST( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            vecProposalTypes = new Vector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hasProposalTypes = (HashMap)result.elementAt(rowIndex);
                vecProposalTypes.addElement(new ComboBoxBean(
                        hasProposalTypes.get(
                        "PROPOSAL_TYPE_CODE").toString(),
                        hasProposalTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecProposalTypes;
    }

    /**
     *  This method used populates combo box with Proposal NSF Code in the
     *  Proposal development screen from OSP$NSF_CODES.
     *  <li>To fetch the data, it uses the procedure DW_GET_NSF_CODES.
     *
     *  @return Vector map of all Proposal NSF code with Proposal NSFCode
     *  code as key and proposal NSFCode description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalNSFCodes() throws CoeusException,DBException{
        Vector result = null;
        Vector vecProposalNSF = null;
        Vector param= new Vector();
        HashMap hasProposalNSF = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_NSF_CODES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int nsfCount = result.size();
        if (nsfCount > 0){
            vecProposalNSF = new Vector();
            for(int rowIndex=0; rowIndex<nsfCount; rowIndex++){
                hasProposalNSF = (HashMap)result.elementAt(rowIndex);
                vecProposalNSF.addElement(new ComboBoxBean(
                        hasProposalNSF.get(
                        "NSF_CODE").toString(),
                        hasProposalNSF.get("DESCRIPTION").toString()));
            }
        }
        return vecProposalNSF;
    }

    /**
     *  This method used populates combo box with Proposal Activity types in the
     *  Proposal development screen from OSP$ACTIVITY_TYPE.
     *  <li>To fetch the data, it uses the procedure DW_GET_ACTIVITY_TYPE.
     *
     *  @return Vector map of all Proposal Activity type with Proposal Activity
     *  type code as key and proposal activity type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalActivityTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector vecProposalActTypes = null;
        Vector param= new Vector();
        HashMap hasProposalActTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_ACTIVITY_TYPE( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actTypeCount = result.size();
        if (actTypeCount > 0){
            vecProposalActTypes = new Vector();
            for(int rowIndex=0; rowIndex<actTypeCount; rowIndex++){
                hasProposalActTypes = (HashMap)result.elementAt(rowIndex);
                vecProposalActTypes.addElement(new ComboBoxBean(
                        hasProposalActTypes.get(
                        "ACTIVITY_TYPE_CODE").toString(),
                        hasProposalActTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecProposalActTypes;
    }

    /**
     *  This method used populates combo box with Proposal Notice of Opportunity
     *  in the Proposal development screen from OSP$NOTICE_OF_OPPORTUNITY.
     *  <li>To fetch the data, it uses the procedure DW_GET_NOTICE_OF_OPPORTUNITY.
     *
     *  @return Vector map of all Proposal Notice of Opportunity with Proposal Notice
     *  of Opportunity code as key and proposal Notice of Opportunity description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalNoticeOpp() throws CoeusException,DBException{
        Vector result = null;
        Vector vecProposalNoticeOpp = null;
        Vector param= new Vector();
        HashMap hasProposalNoticeOpp = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_NOTICE_OF_OPPORTUNITY( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actTypeCount = result.size();
        if (actTypeCount > 0){
            vecProposalNoticeOpp = new Vector();
            for(int rowIndex=0; rowIndex<actTypeCount; rowIndex++){
                hasProposalNoticeOpp = (HashMap)result.elementAt(rowIndex);
                vecProposalNoticeOpp.addElement(new ComboBoxBean(
                        hasProposalNoticeOpp.get(
                        "NOTICE_OF_OPPORTUNITY_CODE").toString(),
                        hasProposalNoticeOpp.get("DESCRIPTION").toString()));
            }
        }
        return vecProposalNoticeOpp;
    }

    /**
     *  This method used get rolodex name for rolodex id passed
     *  <li>To fetch the data, it uses the procedure get_name_from_rolodex.
     *
     *  @return int rolodex Id
     *  @param String rolodex name for the rolodex id passed
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getRolodexName(int rolodexId)
    throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();

        String rolodexName = null;
        param.addElement(new Parameter("ROLODEX_ID",
                DBEngineConstants.TYPE_INT,new Integer(rolodexId).toString()));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_name_from_rolodex( << ROLODEX_ID >> , << OUT STRING ROLODEX_NAME >> ) ",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowHome = (HashMap) result.elementAt(0);
            if ( rowHome.get("ROLODEX_NAME") != null){
                rolodexName = rowHome.get("ROLODEX_NAME").toString();
            }
        }
        return rolodexName;
    }

    /**
     * To get the proposal development details for a particular proposal Number
     * @ return ProposalDevelopmentFormBean that contains the proposal development details
     */
    public ProposalDevelopmentFormBean getProposalDevelopmentDetails(
            String proposalNumber, char functionType )throws CoeusException, DBException{

        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        proposalDevelopmentFormBean = getProposalDevelopmentDetails(proposalNumber);
        String rowId = rowLockStr+proposalDevelopmentFormBean.getProposalNumber();
        if ( functionType == MODIFY_MODE ) {
            if(transMon.canEdit(rowId)){
                return proposalDevelopmentFormBean;
            } else{
                throw new CoeusException("exceptionCode.999999");
            }

        }
        return proposalDevelopmentFormBean;
    }

    // Code added by Shivakumar for locking enhancement-BEGIN

    public ProposalDevelopmentFormBean getProposalDevelopmentDetails(
            String proposalNumber, String loggedinUser, String unitNumber, char functionType )throws CoeusException, DBException,LockingException{

        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        try{
            proposalDevelopmentFormBean = getProposalDevelopmentDetails(proposalNumber);
            String rowId = rowLockStr+proposalDevelopmentFormBean.getProposalNumber();
            if ( functionType == MODIFY_MODE ) {
                dbEngine = new DBEngineImpl();
                if(dbEngine!=null){
                    conn = dbEngine.beginTxn();
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
                LockingBean lockingBean = transMon.canEdit(rowId,loggedinUser, unitNumber,conn);
                boolean gotLock = lockingBean.isGotLock();

                if(gotLock){
                    transactionCommit();
                    return proposalDevelopmentFormBean;
                }
            }
        }catch(DBException dbEx){
            // commented for using UtilFactory.log instead of printStackTrace
            UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalDevelopmentTxnBean", "getProposalDevelopmentDetails");
//            dbEx.printStackTrace();
            transactionRollback();
            throw dbEx;
        }finally{
            endConnection();
        }

//            else{
//                throw new CoeusException("exceptionCode.999999");
//            }

        return proposalDevelopmentFormBean;
    }

    // Method to commit transaction
    public void transactionCommit() throws DBException{
        dbEngine.commit(conn);
    }

    // Method to rollback transaction
    public void transactionRollback() throws DBException{
        dbEngine.rollback(conn);
    }

    public void endConnection() throws DBException{
        dbEngine.endTxn(conn);
    }
    // This method will be called when new Proposals are created.
    // NEW LOCKING METHOD- BEGIN
    public LockingBean getLock(String proposalNumber,String loggedinUser,String unitNumber)
    throws CoeusException, DBException{

        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        proposalDevelopmentFormBean = getProposalDevelopmentDetails(proposalNumber);
        String rowId = rowLockStr+proposalDevelopmentFormBean.getProposalNumber();
        dbEngine=new DBEngineImpl();

        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        LockingBean lockingBean = transMon.newLock(rowId, loggedinUser, unitNumber, conn);
        boolean gotLock = lockingBean.isGotLock();
        try{
            if(gotLock){
                transactionCommit();
            }
        }catch(DBException dbEx){
            // commented for using UtilFactory.log instead of printStackTrace
            UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalDevelopmentTxnBean", "getLock");
//                dbEx.printStackTrace();
            transactionRollback();
            throw dbEx;
        }finally {
            dbEngine.endTxn(conn);
        }

        return lockingBean;
    }

    /** Check the lock for the given propsoal number. If the doesn't exists then lock
     *it and send the notification.
     *@param proposalNumber, loggedIn user, Unit number
     *@ return LockingBean
     *Case #1769  - start
     */
    public  LockingBean checkAndLock(String proposalNumber,String loggedinUser,String unitNumber)
    throws CoeusException, DBException{
        String rowId = rowLockStr+proposalNumber;
        dbEngine=new DBEngineImpl();
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        LockingBean lockingBean = transMon.newLock(rowId, loggedinUser, unitNumber, conn);
        boolean gotLock = lockingBean.isGotLock();
        try{
            if(gotLock){
                transactionCommit();
            }
        }catch(DBException dbEx){
            // commented for using UtilFactory.log instead of printStackTrace
            UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalDevelopmentTxnBean", "checkAndLock");
//                dbEx.printStackTrace();
            transactionRollback();
            throw dbEx;
        }finally {
            dbEngine.endTxn(conn);
        }

        return lockingBean;
    }//Case #1769  - End
    // NEW LOCKING METHOD - END
    // Calling lockavailability method for bug fixing
    public boolean lockCheck(String proposalNumber, String loggedinUser)
    throws CoeusException, DBException{
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        proposalDevelopmentFormBean = getProposalDevelopmentDetails(proposalNumber);
        String rowId = this.rowLockStr+proposalDevelopmentFormBean.getProposalNumber();
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
    }

    // Code added by Shivakumar for locking enhancement-END

    /**
     * Method used to get proposal details from OSP$EPS_PROPOSAL for a given
     * proposal number
     * <li>To fetch the data, it uses DW_GET_PROPOSAL_DETAIL procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return ProposalDevelopmentFormBean  with proposal details for proposalDetails screen.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalDevelopmentFormBean getProposalDevelopmentDetails(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        OrganizationMaintenanceDataTxnBean organizationMaintenanceDataTxnBean;
        OrganizationAddressFormBean organizationAddressFormBean;
        RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean;
        RolodexDetailsBean rolodexDetailsBean;

        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        HashMap proposalDevRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROPOSAL_DETAIL( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            proposalDevRow = (HashMap)result.elementAt(0);
            proposalDevelopmentFormBean = new ProposalDevelopmentFormBean();
            proposalDevelopmentFormBean.setProposalNumber( (String)
            proposalDevRow.get("PROPOSAL_NUMBER"));
            proposalDevelopmentFormBean.setProposalTypeCode(
                    Integer.parseInt(proposalDevRow.get(
                    "PROPOSAL_TYPE_CODE").toString()));
            proposalDevelopmentFormBean.setStatusCode(
                    Integer.parseInt(proposalDevRow.get(
                    "STATUS_CODE").toString()));
            proposalDevelopmentFormBean.setCreationStatusCode(
                    Integer.parseInt(proposalDevRow.get(
                    "CREATION_STATUS_CODE").toString()));
            proposalDevelopmentFormBean.setCreationStatusDescription( (String)
            proposalDevRow.get("CREATION_STATUS_DESC"));
            proposalDevelopmentFormBean.setBaseProposalNumber( (String)
            proposalDevRow.get("BASE_PROPOSAL_NUMBER"));
            proposalDevelopmentFormBean.setContinuedFrom( (String)
            proposalDevRow.get("CONTINUED_FROM"));
            proposalDevelopmentFormBean.setTemplateFlag(
                    (proposalDevRow.get("TEMPLATE_FLAG").toString()
                    .equalsIgnoreCase("y") ? true :false));
            //Commented for case 2406 - Organization and Location - start
//            proposalDevelopmentFormBean.setOrganizationId( (String)
//            proposalDevRow.get("ORGANIZATION_ID"));
//            proposalDevelopmentFormBean.setPerformingOrganizationId( (String)
//            proposalDevRow.get("PERFORMING_ORGANIZATION_ID"));
            //Commented for case 2406 - Organization and Location - end
            proposalDevelopmentFormBean.setCurrentAccountNumber( (String)
            proposalDevRow.get("CURRENT_ACCOUNT_NUMBER"));
            proposalDevelopmentFormBean.setCurrentAwardNumber( (String)
            proposalDevRow.get("CURRENT_AWARD_NUMBER"));
            proposalDevelopmentFormBean.setTitle( (String)
            proposalDevRow.get("TITLE"));
            proposalDevelopmentFormBean.setCfdaNumber( (String)
            proposalDevRow.get("CFDA_NUMBER"));
            proposalDevelopmentFormBean.setSponsorCode( (String)
            proposalDevRow.get("SPONSOR_CODE"));
            proposalDevelopmentFormBean.setSponsorName( (String)
            proposalDevRow.get("SPONSOR_NAME"));
            proposalDevelopmentFormBean.setSponsorProposalNumber( (String)
            proposalDevRow.get("SPONSOR_PROPOSAL_NUMBER"));
            proposalDevelopmentFormBean.setIntrCoopActivitiesFlag(
                    proposalDevRow.get("INTR_COOP_ACTIVITIES_FLAG") == null ? false :
                        (proposalDevRow.get("INTR_COOP_ACTIVITIES_FLAG").toString()
                        .equalsIgnoreCase("y") ? true :false));
            proposalDevelopmentFormBean.setIntrCountrylist( (String)
            proposalDevRow.get("INTR_COUNTRY_LIST"));
            proposalDevelopmentFormBean.setOtherAgencyFlag(
                    proposalDevRow.get("OTHER_AGENCY_FLAG") == null ? false :
                        (proposalDevRow.get("OTHER_AGENCY_FLAG").toString()
                        .equalsIgnoreCase("y") ? true :false));
            proposalDevelopmentFormBean.setNoticeOfOpportunitycode(
                    Integer.parseInt(proposalDevRow.get(
                    "NOTICE_OF_OPPORTUNITY_CODE") == null ? "0" :
                        proposalDevRow.get("NOTICE_OF_OPPORTUNITY_CODE").toString()));
            proposalDevelopmentFormBean.setNoticeOfOpportunityDescription((String)proposalDevRow.get("NOTICE_OF_OPPOR_DESCRIPTION"));
            proposalDevelopmentFormBean.setProgramAnnouncementNumber( (String)
            proposalDevRow.get("PROGRAM_ANNOUNCEMENT_NUMBER"));
            proposalDevelopmentFormBean.setProgramAnnouncementTitle( (String)
            proposalDevRow.get("PROGRAM_ANNOUNCEMENT_TITLE"));
            proposalDevelopmentFormBean.setProposalActivityTypeCode(
                    Integer.parseInt(proposalDevRow.get(
                    "ACTIVITY_TYPE_CODE") == null ? "0" :
                        proposalDevRow.get("ACTIVITY_TYPE_CODE").toString()));
            //start ele addition 5-24-05
            proposalDevelopmentFormBean.setProposalActivityTypeDesc((String)
            proposalDevRow.get("ACTIVITY_TYPE_DESCRIPTION"));
            //Added for coeus enhancement Case#1622
            proposalDevelopmentFormBean.setAgencyProgramCode((String)
            proposalDevRow.get("AGENCY_PROGRAM_CODE"));
            proposalDevelopmentFormBean.setAgencyDivCode((String)
            proposalDevRow.get("AGENCY_DIVISION_CODE"));
            //end ele addition 5-24-05
            
           //COEUSQA-3951  
            proposalDevelopmentFormBean.setAgencyRoutingIdentifier((String)
            proposalDevRow.get("AGENCY_ROUTING_IDENTIFIER"));
            proposalDevelopmentFormBean.setPreviousGGTrackingID((String)
            proposalDevRow.get("PREV_GG_TRACKID"));
           //COEUSQA-3951 
            
            proposalDevelopmentFormBean.setRequestStartDateInitial(
                    proposalDevRow.get("REQUESTED_START_DATE_INITIAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                    "REQUESTED_START_DATE_INITIAL")).getTime()));
            proposalDevelopmentFormBean.setRequestStartDateTotal(
                    proposalDevRow.get("REQUESTED_START_DATE_TOTAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                    "REQUESTED_START_DATE_TOTAL")).getTime()));
            proposalDevelopmentFormBean.setRequestEndDateInitial(
                    proposalDevRow.get("REQUESTED_END_DATE_INITIAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                    "REQUESTED_END_DATE_INITIAL")).getTime()));
            proposalDevelopmentFormBean.setRequestEndDateTotal(
                    proposalDevRow.get("REQUESTED_END_DATE_TOTAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                    "REQUESTED_END_DATE_TOTAL")).getTime()));
            proposalDevelopmentFormBean.setDurationMonth(
                    Integer.parseInt(proposalDevRow.get(
                    "DURATION_MONTHS") == null ? "0" :
                        proposalDevRow.get("DURATION_MONTHS").toString()));
            proposalDevelopmentFormBean.setNumberCopies( (String)
            proposalDevRow.get("NUMBER_OF_COPIES"));
            proposalDevelopmentFormBean.setDeadLineDate(
                    proposalDevRow.get("DEADLINE_DATE") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                    "DEADLINE_DATE")).getTime()));
            proposalDevelopmentFormBean.setDeadLineType( (String)
            proposalDevRow.get("DEADLINE_TYPE"));
            proposalDevelopmentFormBean.setMailingAddressId(
                    Integer.parseInt(proposalDevRow.get(
                    "MAILING_ADDRESS_ID") == null ? "-1" :
                        proposalDevRow.get("MAILING_ADDRESS_ID").toString()));
            proposalDevelopmentFormBean.setMailBy( (String)
            proposalDevRow.get("MAIL_BY"));
            proposalDevelopmentFormBean.setMailType( (String)
            proposalDevRow.get("MAIL_TYPE"));
            proposalDevelopmentFormBean.setMailAccountNumber( (String)
            proposalDevRow.get("MAIL_ACCOUNT_NUMBER"));
            proposalDevelopmentFormBean.setCarrierCodeType( (String)
            proposalDevRow.get("CARRIER_CODE_TYPE"));
            proposalDevelopmentFormBean.setCarrierCode( (String)
            proposalDevRow.get("CARRIER_CODE"));
            proposalDevelopmentFormBean.setMailDescription( (String)
            proposalDevRow.get("MAIL_DESCRIPTION"));
            proposalDevelopmentFormBean.setSubcontractFlag(
                    proposalDevRow.get("SUBCONTRACT_FLAG") == null ? false :
                        (proposalDevRow.get("SUBCONTRACT_FLAG").toString()
                        .equalsIgnoreCase("y") ? true :false));
            proposalDevelopmentFormBean.setNarrativeStatus( (String)
            proposalDevRow.get("NARRATIVE_STATUS"));
            proposalDevelopmentFormBean.setBudgetStatus( (String)
            proposalDevRow.get("BUDGET_STATUS"));
            proposalDevelopmentFormBean.setOwnedBy( (String)
            proposalDevRow.get("OWNED_BY_UNIT"));
            proposalDevelopmentFormBean.setCreateTimeStamp(
                    (Timestamp)proposalDevRow.get("CREATE_TIMESTAMP"));
            proposalDevelopmentFormBean.setCreateUser( (String)
            proposalDevRow.get("CREATE_USER"));
            proposalDevelopmentFormBean.setUpdateTimestamp(
                    (Timestamp)proposalDevRow.get("UPDATE_TIMESTAMP"));
            proposalDevelopmentFormBean.setUpdateUser( (String)
            proposalDevRow.get("UPDATE_USER"));
            proposalDevelopmentFormBean.setNsfCode( (String)
            proposalDevRow.get("NSF_CODE"));
            proposalDevelopmentFormBean.setNsfCodeDescription((String)proposalDevRow.get("NSF_CODES_DESCRIPTION"));
            proposalDevelopmentFormBean.setPrimeSponsorCode( (String)
            proposalDevRow.get("PRIME_SPONSOR_CODE"));
            proposalDevelopmentFormBean.setPrimeSponsorName( (String)
            proposalDevRow.get("PRIME_SPONSOR_NAME"));
            proposalDevelopmentFormBean.setProposalTypeDesc( (String)
            proposalDevRow.get("PROPOSAL_TYPE_DESC"));
            // Added for Case 2162  - adding Award Type - Start
            proposalDevelopmentFormBean.setAwardTypeCode(
                    Integer.parseInt(proposalDevRow.get("AWARD_TYPE_CODE") == null ? "0" :
                        proposalDevRow.get("AWARD_TYPE_CODE").toString()));
            proposalDevelopmentFormBean.setAwardTypeDesc((String)
            proposalDevRow.get("AWARD_TYPE_DESCRIPTION"));
            // Added for Case 2162  - adding Award Type - End
            proposalDevelopmentFormBean.setProposalOverrideExists(
                    getProposalCount(proposalDevelopmentFormBean.getProposalNumber()));
            SponsorMaintenanceFormBean sponsorFormBean =
                    spTxnBean.getSponsorMaintenanceDetails(proposalDevelopmentFormBean.getSponsorCode());

            proposalDevelopmentFormBean.setSponsorName(sponsorFormBean.getName());
            /*StringBuffer strBffr = new StringBuffer();
            strBffr.append((String)proposalDevelopmentFormBean.getOwnedBy()+":");
            strBffr.append(departmentTxnBean.getUnitName(proposalDevelopmentFormBean.getOwnedBy()));
            proposalDevelopmentFormBean.setOwnedBy(strBffr.toString());*/
            proposalDevelopmentFormBean.setOwnedByDesc(
                    departmentTxnBean.getUnitName(proposalDevelopmentFormBean.getOwnedBy()));

            organizationMaintenanceDataTxnBean = new
                    OrganizationMaintenanceDataTxnBean();
            rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
            rolodexDetailsBean = new RolodexDetailsBean();

            //Commented for case 2406 - Organization and Locations - start
//            // setting the organization details for the organization id
//            organizationAddressFormBean =
//                    organizationMaintenanceDataTxnBean.getOrganizationAddress(
//                    proposalDevelopmentFormBean.getOrganizationId());
//            proposalDevelopmentFormBean.setOrganizationAddressFormBean(organizationAddressFormBean);
            //Commented for case 2406 - Organization and Locations - end
            //Added for case 2406 - Organization and Locations - start
            proposalDevelopmentFormBean.setSites(getProposalSites(proposalDevelopmentFormBean.getProposalNumber()));
            organizationAddressFormBean = getSiteAddress("Proposal Organization", proposalDevelopmentFormBean.getSites());
            proposalDevelopmentFormBean.setOrganizationAddressFormBean(organizationAddressFormBean);
            //Added for case 2406 - Organization and Locations - end

            //setting the rolodex name
            int rolodexId = proposalDevelopmentFormBean.getMailingAddressId();
            proposalDevelopmentFormBean.setMailingAddressName(rolodexDataTxnBean.getRolodexName(""+rolodexId));
            //setting the rolodex details
            int contactAddressId = organizationAddressFormBean.getContactAddressId();
            proposalDevelopmentFormBean.setRolodexDetailsBean(
                    rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(Integer.toString(
                    contactAddressId )));


            //settings the organization details for the performing organization id
            //Commented for case 2406 - Organization and Locations - start
//            proposalDevelopmentFormBean.setPerOrganizationAddressFormBean(
//                    organizationMaintenanceDataTxnBean.getOrganizationAddress(
//                    proposalDevelopmentFormBean.getPerformingOrganizationId()));
            //Commented for case 2406 - Organization and Locations - end
            //Added for case 2406 - Organization and Locations - start
            proposalDevelopmentFormBean.setPerOrganizationAddressFormBean(
                    getSiteAddress("Performing Organization",proposalDevelopmentFormBean.getSites()));
            //Added for case 2406 - Organization and Locations - end

            //setting the mailing address
            if(rolodexId == -1){
                proposalDevelopmentFormBean.setMailingAddress(null);
            }else{
                proposalDevelopmentFormBean.setMailingAddress(
                        getRolodexAddress(Integer.toString(proposalDevelopmentFormBean.getMailingAddressId())));
            }

            //getting the protocol investigators and adding to proposalform bean
            ProposalPersonTxnBean  proposalPersonTxnBean = new ProposalPersonTxnBean();
            proposalDevelopmentFormBean.setPropPersons(
                    proposalPersonTxnBean.getAllPersonDetails( proposalDevelopmentFormBean.getProposalNumber() ));
            proposalDevelopmentFormBean.setInvestigators(getProposalInvestigatorDetails(
                    proposalDevelopmentFormBean.getProposalNumber()));
            // getting the proposal key persone and adding to proposal key person form bean
            //new code to add unit section

            //ppc certify flag for key person ends
            proposalDevelopmentFormBean.setKeyPersonCertifyParam(this.getParameterCertification(ENABLE_PROP_PERSON_SELF_CERTIFY));
            //ppc certify flag for key person ends

            //old code..
            proposalDevelopmentFormBean.setKeyStudyPersonnel(getProposalKeyPersonDetails(
                    proposalDevelopmentFormBean.getProposalNumber()));
            //getting the proposal science code and adding the form bean
            proposalDevelopmentFormBean.setScienceCode(getProposalScienceCode(
                    proposalDevelopmentFormBean.getProposalNumber()));
            //getting the proposal others and adding to the form bean
            /*
             proposalDevelopmentFormBean.setOthers(getProposalOthersDetails(
                        proposalDevelopmentFormBean.getProposalNumber()));
             */
            //proposalDevelopmentFormBean.setKeyStudyPersonnel(getProposalKPUnitDetails(
              //      proposalDevelopmentFormBean.getProposalNumber()));

            proposalDevelopmentFormBean.setOthers(getOthersDetails(
                    proposalDevelopmentFormBean.getProposalNumber()));
            //Commented for case 2406 - Organization and Locations - start
            //getting the proposal location list and adding the form bean
//            proposalDevelopmentFormBean.setLocationLists(getProposalLocationList(
//                    proposalDevelopmentFormBean.getProposalNumber()));
            //Commented for case 2406 - Organization and Locations - end

            //getting the proposal lead unit and adding the form bean
            /*proposalDevelopmentFormBean.setLeadUnit(getProposalLeadUnitDetails(
                        proposalDevelopmentFormBean.getProposalNumber()));*/

            //set certify data
            proposalDevelopmentFormBean.setInvestigatorQuestions(
                    getCertifyQuestions("I"));
            // set Certify 'More' data
            // hashtable holds the 'More' data
            // key = question id + explanation type
            // value = explanation
            Hashtable moreExplanation = organizationMaintenanceDataTxnBean.getQuestionExplanationAll();
            proposalDevelopmentFormBean.setMoreExplanation(moreExplanation);

            // set investigator answers
            Vector investigators = proposalDevelopmentFormBean.getInvestigators();
            int size = 	( investigators == null ? 0 : investigators.size() );
            for(int rowIndex=0; rowIndex<size; rowIndex++){
                ProposalInvestigatorFormBean  proposalInvestigatorFormBean =
                        (ProposalInvestigatorFormBean) investigators.elementAt(rowIndex);
                String personId = proposalInvestigatorFormBean.getPersonId();
                //pass the personid and proposal no to get the answers
                Vector answers = getCertifyAnswers(
                        proposalDevelopmentFormBean.getProposalNumber(),personId);

                // hashtable will hold personId as key and vector of ProposalYNQFormBean as value
                //Vector investigatorAnswers = new Vector();
                //investigatorAnswers.add(answers);
                proposalInvestigatorFormBean.setInvestigatorAnswers(answers == null? new Vector(): answers);

            }

            //getting the data for user  tree view for Modify/Display mode
            proposalDevelopmentFormBean.setUserRolesInfoBean(getProposalUserRoles(proposalNumber,proposalDevelopmentFormBean.getOwnedBy(),"M",""));
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            //getting data for User's for this Unit Number
            proposalDevelopmentFormBean.setUserInfo(userDetailsBean.getUserForUnit(proposalDevelopmentFormBean.getOwnedBy()));
            //getting Special Review Data
            //Commented and Added for COEUSQA-2984 : Statuses in special review - start
            //proposalDevelopmentFormBean.setPropSpecialReviewFormBean(getProposalSpecialReview(proposalDevelopmentFormBean.getProposalNumber()));
            Vector vecReviewData = (Vector)getProposalSpecialReview(proposalDevelopmentFormBean.getProposalNumber());
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String specialReviewCode = coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
            String specialReviewCodeForAnimal = coeusFunctions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
            vecReviewData = vecReviewData == null? new Vector() : vecReviewData;
            SpecialReviewFormBean  specialReviewFormBean = null;
            if(vecReviewData!=null && vecReviewData.size() > 0 ){
                if(PROPOSAL_SUBMITTED_STATUS == proposalDevelopmentFormBean.getCreationStatusCode()){
                    for(int index = 0 ; index < vecReviewData.size() ; index++){
                        ProposalSpecialReviewFormBean proposalSpecialReviewFormBean  = (ProposalSpecialReviewFormBean)vecReviewData.get(index);
                        if( Integer.parseInt(specialReviewCode.trim()) == proposalSpecialReviewFormBean.getSpecialReviewCode()){
                            specialReviewFormBean = (SpecialReviewFormBean)getPropProtocolStatus(proposalSpecialReviewFormBean.getProposalNumber(),proposalSpecialReviewFormBean.getProtocolSPRevNumber(),proposalSpecialReviewFormBean.getSpecialReviewNumber());
                            if(specialReviewFormBean!=null){
                                proposalSpecialReviewFormBean.setApprovalDescription(specialReviewFormBean.getProtocolStatusDescription());
                            }
                            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                            //If special review code is 2 then get the details for IACUC protocol
                        }else if(Integer.parseInt(specialReviewCodeForAnimal.trim()) == proposalSpecialReviewFormBean.getSpecialReviewCode()){
                            specialReviewFormBean = (SpecialReviewFormBean)getPropIacucProtocolStatus(proposalSpecialReviewFormBean.getProposalNumber(),proposalSpecialReviewFormBean.getProtocolSPRevNumber(),proposalSpecialReviewFormBean.getSpecialReviewNumber());
                            if(specialReviewFormBean!=null){
                                proposalSpecialReviewFormBean.setApprovalDescription(specialReviewFormBean.getProtocolStatusDescription());
                            }
                        }
                        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                    }
                }
            }
            proposalDevelopmentFormBean.setPropSpecialReviewFormBean(vecReviewData);
            //Added for COEUSQA-2984 : Statuses in special review - end
            //Getting Yes No Questions
            Vector vctQuestionList = getYesNoQuestionList("P",proposalNumber);
            Vector vctAnswerList = null;
            try{
                vctAnswerList = getProposalYNQ(proposalNumber);
            } catch(NullPointerException npe){
                //System.out.println("NullPointerException raised. It means that the Answer List is Null.");
            }

            // Commented the below lines. The initialization will be taken care at the client
            // by the QuestionForm class
            /*
            ProposalYNQBean[] yqnArray = null;
            if (vctAnswerList == null) {
                vctAnswerList = new Vector();
                yqnArray = new ProposalYNQBean[vctQuestionList.size()];
                for (int i = 0; i < vctQuestionList.size(); i++) {
                    yqnArray[i] = new ProposalYNQBean();
                    yqnArray[i].setQuestionId(((QuestionListBean)vctQuestionList.elementAt( i )).getQuestionId());
                    yqnArray[i].setProposalNumber(proposalNumber);
                    yqnArray[i].setAcType("I");
                    yqnArray[i].setAnswer("");
                    vctAnswerList.addElement(yqnArray[i]);
                }
            }
             */
            Hashtable hstExplanation = new Hashtable();
            String questionID = null;

            if(vctQuestionList!= null && vctQuestionList.size() > 0){
                for (int index=0; index < vctQuestionList.size(); index++){
                    questionID = ((QuestionListBean)vctQuestionList.elementAt(index)).getQuestionId();
                    Vector vExp = getProposalYNQExplantion(questionID);
                    if (vExp!=null) {
                        hstExplanation.put(questionID,vExp);
                    }
                }
            }
            proposalDevelopmentFormBean.setPropYNQuestionList(vctQuestionList == null ? new Vector(): vctQuestionList);
            proposalDevelopmentFormBean.setPropYNQAnswerList(vctAnswerList == null ? new Vector():vctAnswerList);
            proposalDevelopmentFormBean.setPropYNQExplanationList(hstExplanation == null ? new Hashtable():hstExplanation);
            //End of Yes No Questions

            //QUESTIONNAIRE SEGMENT MODIFICATION STARTS
            try{vctAnswerList = getProposalQuestionnaire(proposalNumber);}
            catch(NullPointerException npe){}
            proposalDevelopmentFormBean.setPropQuestionnaireAnswers(vctAnswerList == null ? new Vector():vctAnswerList);
            //QUESTIONNAIRE SEGMENT MODIFICATION ENDS


            //Get Approval Maps - start
            //Modified for Routing enhancements case 2785 - start
//            CoeusVector vctApprovalMaps = new CoeusVector();
//            ProposalApprovalMapBean proposalApprovalMapBean = null;
//            ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
//
//            proposalApprovalMapBean = proposalActionTxnBean.getProposalApprovalMapForPK(proposalDevelopmentFormBean.getProposalNumber(), 999);
//            vctApprovalMaps.addElement(proposalApprovalMapBean);
//            proposalDevelopmentFormBean.setApprovalMaps(vctApprovalMaps);
            RoutingTxnBean routingTxnBean = new RoutingTxnBean();
            RoutingBean routingBean = routingTxnBean.getRoutingHeader("3", proposalNumber, "0", 0);

            CoeusVector vctApprovalMaps = new CoeusVector();
            //Modified for case 3612 - Routing Issues - start
            //Get the maps only if the routing is not rejected.
            if(routingBean != null){
                CoeusVector cvRoutingMaps = routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
                if(cvRoutingMaps!=null){
                    Equals eqApprovalStatus = new Equals("approvalStatus", "R");
                    CoeusVector cvRejectedMaps = cvRoutingMaps.filter(eqApprovalStatus);
                    if(cvRejectedMaps==null || cvRejectedMaps.size() == 0){
                        proposalDevelopmentFormBean.setRoutingBean(routingBean);
                        RoutingMapBean routingMapBean = routingTxnBean.getRoutingApprovalMap(routingBean.getRoutingNumber());
                        vctApprovalMaps.addElement(routingMapBean);
                    }
                }
            }
            //Modified for case 3612 - Routing Issues - end
            proposalDevelopmentFormBean.setApprovalMaps(vctApprovalMaps);
            //Modified for Routing enhancements case 2785 - end
            //Get Approval Maps - end

            //Get Narrative All Modules users - start
            ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
            CoeusVector vctNarrative = proposalNarrativeTxnBean.getProposalNarrative(proposalDevelopmentFormBean.getProposalNumber());
            proposalDevelopmentFormBean.setNarrativeUserRights(vctNarrative);

            vctNarrative = proposalNarrativeTxnBean.getProposalNarrativeModules(proposalDevelopmentFormBean.getProposalNumber());
            proposalDevelopmentFormBean.setNarrativeModules(vctNarrative);
            //Get Narrative All Modules users - end

            //check the opportunity already selected for s2s submission and set it to bean

            S2SSubmissionDataTxnBean s2sSubTxnBean  = new S2SSubmissionDataTxnBean();
            proposalDevelopmentFormBean.setS2sOppSelected(null!=s2sSubTxnBean.getLocalOpportunity(
                    proposalDevelopmentFormBean.getProposalNumber()));

            proposalDevelopmentFormBean.setS2sCandidate(s2sSubTxnBean.isS2SCandidate(
                    proposalDevelopmentFormBean.getProposalNumber()));

            // Added for Proposal Hierarchy data - start
//             ProposalHierarchyTxnBean txn = new ProposalHierarchyTxnBean();
//            ProposalHierarchyBean proposalHierarchyBean = null;
//                HashMap data = txn.getParentProposalData(proposalNumber);
//                String rootProposal = (String)data.get("PARENT_PROPOSAL");
//                if(rootProposal != null)
//                    proposalHierarchyBean = txn.getHierarchyData(rootProposal);
//                proposalDevelopmentFormBean.setProposalHierarchyBean(proposalHierarchyBean);
            //Added for Proposal Hierarchy data - End
        }
        return proposalDevelopmentFormBean;
    }

    // Code added by Shivakumar - BEGIN
    public CoeusVector getNarrativeRights(String proposalNumber)
    throws CoeusException, DBException{
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        CoeusVector cvNarrativeRights = new CoeusVector();
        if(proposalNumber != null){
            cvNarrativeRights = proposalNarrativeTxnBean.getProposalNarrative(proposalNumber);
        }
        return cvNarrativeRights;
    }

    public CoeusVector getNarrativeModule(String proposalNumber)
    throws CoeusException, DBException{
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        CoeusVector cvNarrativeModules = new CoeusVector();
        if(proposalNumber != null){
            cvNarrativeModules = proposalNarrativeTxnBean.getProposalNarrativeModules(proposalNumber);
        }
        return cvNarrativeModules;
    }
    // Code added by Shivakumar - END
    /**
     *
     *
     * /**
     * This method gets the proposal details information for the proposal tab in
     * Proposal details screen.
     * @ return ProposalDevelopmentFormBean that contain the proposal infn.
     */
    //Modified by shiji for fixing bug id : 1576 - start
    public ProposalDevelopmentFormBean getNewProposalDetails(String unitNumber, String userId)
    throws CoeusException, DBException, Exception{
        OrganizationAddressFormBean organizationAddressFormBean;
        RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean;
        String organizationId = null;
        RolodexDetailsBean rolodexDetailsBean;
        /** The following lines are commnted. The organization id has to be pulled
         *by executing FN_GET_ORGANIZATION_ID not by getting from parameter table
         *Bug fix #1576
         */
//        CoeusFunctions coeusFunctions = new CoeusFunctions();
//        CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
//        coeusParameterBean.setParameterName(CoeusConstants.DEFAULT_ORGANIZATION_ID);
//        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
//        if(coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")){
//            organizationId = coeusParameterBean.getParameterValue();
//        }



        OrganizationMaintenanceDataTxnBean organizationMaintenanceDataTxnBean
                = new OrganizationMaintenanceDataTxnBean();
        /** Bug Fix #1576.start
         *Get the Organization id by executing FN_GET_ORGANIZATION_ID
         */
        organizationId = organizationMaintenanceDataTxnBean.getOrganizationId(unitNumber);
        /** Bug Fix #1576.End
         */
        ProposalDevelopmentFormBean proposalBean = new ProposalDevelopmentFormBean();
        String newProposalNumber = getNextProposalNumber();
        rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
        rolodexDetailsBean = new RolodexDetailsBean();
        // setting the organization details for the organization id
//        organizationAddressFormBean =
//                organizationMaintenanceDataTxnBean.getOrganizationAddress(
//                            CoeusConstants.INSTITUTE_UNIT_NUMBER);
        organizationAddressFormBean =
                organizationMaintenanceDataTxnBean.getOrganizationAddress(
                organizationId);
        proposalBean.setOrganizationAddressFormBean(organizationAddressFormBean);
        //setting the rolodex details
        int contactAddressId = organizationAddressFormBean.getContactAddressId();
        proposalBean.setRolodexDetailsBean(
                rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(Integer.toString(
                contactAddressId )));

        //settings the organization details for the performing organization id
//        proposalBean.setPerOrganizationAddressFormBean(
//        organizationMaintenanceDataTxnBean.getOrganizationAddress(
//                    CoeusConstants.INSTITUTE_UNIT_NUMBER));
        proposalBean.setPerOrganizationAddressFormBean(
                organizationMaintenanceDataTxnBean.getOrganizationAddress(
                organizationId));
        proposalBean.setProposalNumber( newProposalNumber );
        //proposalBean.setOrganizationId( CoeusConstants.INSTITUTE_UNIT_NUMBER );
        proposalBean.setOrganizationId(organizationId );
        //proposalBean.setPerformingOrganizationId( CoeusConstants.INSTITUTE_UNIT_NUMBER );
        proposalBean.setPerformingOrganizationId(organizationId  );
        proposalBean.setProposalNumber( newProposalNumber );
        proposalBean.setOwnedBy( unitNumber );
        proposalBean.setOthers( getOthersDetails( newProposalNumber ) );
        proposalBean.setInvestigatorQuestions( getCertifyQuestions("I") );
        //Added for case 2406 - Organization and Locations - start
        CoeusVector cvSites = new CoeusVector();
        //Add the proposal organization
        ProposalSiteBean proposalSiteBean = new ProposalSiteBean();
        proposalSiteBean.setProposalNumber(newProposalNumber);
        proposalSiteBean.setLocationName(organizationAddressFormBean.getOrganizationName());
        proposalSiteBean.setLocationTypeCode(PROPOSAL_ORGANIZATION);
        proposalSiteBean.setRolodexDetailsBean(proposalBean.getRolodexDetailsBean());
        proposalSiteBean.setOrganizationId(organizationId);
        proposalSiteBean.setAcType(TypeConstants.INSERT_RECORD);
        if(organizationAddressFormBean != null && organizationAddressFormBean.getCongressionalDistrict()!=null){
            proposalSiteBean.setCongDistricts(new CoeusVector());
            ProposalCongDistrictBean proposalCongDistrictBean = new ProposalCongDistrictBean();
            proposalCongDistrictBean.setProposalNumber(newProposalNumber);
            proposalCongDistrictBean.setCongDistrict(organizationAddressFormBean.getCongressionalDistrict());
            proposalCongDistrictBean.setAw_CongDistrict(organizationAddressFormBean.getCongressionalDistrict());
            proposalCongDistrictBean.setAcType(TypeConstants.INSERT_RECORD);
            proposalSiteBean.getCongDistricts().add(proposalCongDistrictBean);
        }
        cvSites.add(proposalSiteBean);
        ProposalSiteBean perfomingOrgSiteBean = (ProposalSiteBean)ObjectCloner.deepCopy(proposalSiteBean);
        perfomingOrgSiteBean.setLocationTypeCode(PERFORMING_ORGANIZATION);
        cvSites.add(perfomingOrgSiteBean);
        proposalBean.setSites(cvSites);
        //Added for case 2406 - Organization and Locations - end
        // value = explanation
        Hashtable moreExplanation = organizationMaintenanceDataTxnBean.getQuestionExplanationAll();
        proposalBean.setMoreExplanation( moreExplanation );

        //getting the data for user  tree view for Default mode
        proposalBean.setUserRolesInfoBean(getProposalUserRoles(newProposalNumber,proposalBean.getOwnedBy(),"N",userId));
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        //getting data for User's for this Unit Number
        proposalBean.setUserInfo(userDetailsBean.getUserForUnit(proposalBean.getOwnedBy()));

        //Getting Yes No Questions & Explanations - start
        Vector vctQuestionList = getYesNoQuestionList("P",newProposalNumber);
        proposalBean.setPropYNQuestionList(vctQuestionList);

        Hashtable hstExplanation = new Hashtable();
        String questionID = null;
        // bug Fix 2017 - start
        if(vctQuestionList!= null && vctQuestionList.size() > 0){
            // bug Fix 2017 - End
            for (int index=0; index < vctQuestionList.size(); index++){
                questionID = ((QuestionListBean)vctQuestionList.elementAt(index)).getQuestionId();
                Vector vExp = getProposalYNQExplantion(questionID);
                if (vExp!=null) {
                    hstExplanation.put(questionID,vExp);
                }
            }
        }
        proposalBean.setPropYNQExplanationList(hstExplanation);
        //Getting Yes No Questions & Explanations - end

        return proposalBean;
    }
    //bug id : 1576 - end


    /**
     * Method used to get rolodex address details for rolodex id
     * <li>To fetch the data, it uses SELECT_ROLODEX procedure.
     *
     * @param rolodex id this is given as input parameter for the
     * procedure to execute.
     * @return string  rolodex address .
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getRolodexAddress(String rolodexId)
    throws CoeusException , DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap addressListRow = null;
        param.addElement(new Parameter("ROLODEX_ID",
                DBEngineConstants.TYPE_STRING,rolodexId));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call SELECT_ROLODEX( <<ROLODEX_ID>> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        StringBuffer strBffr ;
        String address = "";
        if(listSize >0){
            strBffr = new StringBuffer();
            addressListRow = (HashMap)result.elementAt(0);
            strBffr.append((String)
            addressListRow.get("ORGANIZATION") == null
                    ? "" :addressListRow.get("ORGANIZATION")
                    .toString()+ "$#");
            strBffr.append((String)
            addressListRow.get("ADDRESS_LINE_1") == null
                    ? "" :addressListRow.get("ADDRESS_LINE_1")
                    .toString()+ "$#");
            strBffr.append((String)
            addressListRow.get("ADDRESS_LINE_2") == null
                    ? "" :addressListRow.get("ADDRESS_LINE_2")
                    .toString()+ "$#");
            strBffr.append((String)
            addressListRow.get("ADDRESS_LINE_3") == null
                    ? "" :addressListRow.get("ADDRESS_LINE_3")
                    .toString()+ "$#");
            strBffr.append((String)
            addressListRow.get("CITY") == null
                    ? "" :addressListRow.get("CITY").toString()+ "$#");
            strBffr.append((String)
            addressListRow.get("COUNTY") == null
                    ? "" :addressListRow.get("COUNTY").toString()+ "$#");
            strBffr.append((String)
            addressListRow.get("STATE") == null
                    ? "" :addressListRow.get("STATE").toString()+ " - ");
            strBffr.append((String)
            addressListRow.get("POSTAL_CODE") == null
                    ? "" :addressListRow.get("POSTAL_CODE")
                    .toString()+ "$#");
            strBffr.append((String)
            addressListRow.get("COUNTRY_CODE") == null
                    ? "" :addressListRow.get("COUNTRY_CODE").toString());

            address = (String) strBffr.toString();

        }
        return address;
    }

    /**
     * Method used to get proposal investigator details from OSP$EPS_PROP_INVESTIGATORS
     * for a given proposal number
     * <li>To fetch the data, it uses GET_PROPOSAL_INVESTIGATORS_DW procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return ProposalInvestigatorFormBean  with proposal investigatordetails .
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalInvestigatorDetails(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalInvestigatorFormBean proposalInvestigatorFormBean = null;
        HashMap proposalInvRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* CASE #1667 Comment Begin */
        /*if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROPOSAL_INVESTIGATORS_DW( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }*/
        /* CASE #1667 Comment End */
        /* CASE #1667 Begin*/
        //Call updated stored procedure.  Stored procedure is the same except
        //for an order by clause.
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_INVESTIGATORS( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        /* CASE #1667 End */
        int listSize = result.size();
        Vector proposalInvList = null;
        if (listSize > 0){
            proposalInvList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalInvestigatorFormBean = new ProposalInvestigatorFormBean();
                proposalInvRow = (HashMap)result.elementAt(rowIndex);
                proposalInvestigatorFormBean.setProposalNumber( (String)
                proposalInvRow.get("PROPOSAL_NUMBER"));
                proposalInvestigatorFormBean.setPersonId( (String)
                proposalInvRow.get("PERSON_ID"));
                proposalInvestigatorFormBean.setPersonName((String)
                proposalInvRow.get("PERSON_NAME"));
                proposalInvestigatorFormBean.setPrincipleInvestigatorFlag(
                        proposalInvRow.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? false :
                            (proposalInvRow.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalInvestigatorFormBean.setFacultyFlag(
                        proposalInvRow.get("FACULTY_FLAG") == null ? false :
                            (proposalInvRow.get("FACULTY_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalInvestigatorFormBean.setNonMITPersonFlag(
                        proposalInvRow.get("NON_MIT_PERSON_FLAG") == null ? false :
                            (proposalInvRow.get("NON_MIT_PERSON_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalInvestigatorFormBean.setConflictOfIntersetFlag(
                        proposalInvRow.get("CONFLICT_OF_INTEREST_FLAG") == null ? false :
                            (proposalInvRow.get("CONFLICT_OF_INTEREST_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalInvestigatorFormBean.setPercentageEffort(
                        Float.parseFloat(proposalInvRow.get( "PERCENTAGE_EFFORT") == null ?
                            "0" : proposalInvRow.get( "PERCENTAGE_EFFORT").toString()));
                proposalInvestigatorFormBean.setFedrDebrFlag(
                        proposalInvRow.get("FEDR_DEBR_FLAG") == null ? false :
                            (proposalInvRow.get("FEDR_DEBR_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalInvestigatorFormBean.setFedrDelqFlag(
                        proposalInvRow.get("FEDR_DELQ_FLAG") == null ? false :
                            (proposalInvRow.get("FEDR_DELQ_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalInvestigatorFormBean.setUpdateTimestamp(
                        (Timestamp)proposalInvRow.get("UPDATE_TIMESTAMP"));
                proposalInvestigatorFormBean.setUpdateUser( (String)
                proposalInvRow.get("UPDATE_USER"));

                //Added for Case# 2229 - Multi PI Enhancement
                proposalInvestigatorFormBean.setMultiPIFlag(proposalInvRow.get("MULTI_PI_FLAG") == null ? false :
                    (proposalInvRow.get("MULTI_PI_FLAG").toString()
                    .equalsIgnoreCase("y") ? true :false));

                //Addedd for Case# 2270 - Tracking of Effort - Start
                proposalInvestigatorFormBean.setAcademicYearEffort(
                        Float.parseFloat(proposalInvRow.get("ACADEMIC_YEAR_EFFORT") == null ?
                            "0" : proposalInvRow.get("ACADEMIC_YEAR_EFFORT").toString()));
                proposalInvestigatorFormBean.setSummerYearEffort(
                        Float.parseFloat(proposalInvRow.get("SUMMER_YEAR_EFFORT") == null ?
                            "0" : proposalInvRow.get("SUMMER_YEAR_EFFORT").toString()));
                proposalInvestigatorFormBean.setCalendarYearEffort(
                        Float.parseFloat(proposalInvRow.get("CALENDAR_YEAR_EFFORT") == null ?
                            "0" : proposalInvRow.get("CALENDAR_YEAR_EFFORT").toString()));
                //Addedd for Case# 2270 - Tracking of Effort - End
                proposalInvestigatorFormBean.setCertifyFlag(
                        proposalInvRow.get("CERTIFICATION_FLAG") == null ? false :
                            (proposalInvRow.get("CERTIFICATION_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                // Adding the Lead unit details to the investigator tab
                proposalInvestigatorFormBean.setInvestigatorUnits(
                        getProposalLeadUnitDetails(proposalInvestigatorFormBean.getProposalNumber(),
                        proposalInvestigatorFormBean.getPersonId()));
                proposalInvList.add(proposalInvestigatorFormBean);
            }
        }

        return proposalInvList;
    }

    /**
     * Method used to get proposal lead unit details from OSP$EPS_PROP_UNITS
     * for a given proposal number and personid.
     * <li>To fetch the data, it uses GET_PROP_INVESTIGATOR_UNITS procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @param personid String representing the investigator person id to which
     * units have to be fetched.
     * @return ProposalLeadUnitFormBean  with proposal lead units details .
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalLeadUnitDetails(String proposalNumber, String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalLeadUnitFormBean proposalLeadUnitFormBean = null;
        HashMap proposalLeadRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROP_INVESTIGATOR_UNITS( <<PROPOSAL_NUMBER>> , <<PERSON_ID>>,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector proposalLeadList = null;
        if (listSize > 0){
            proposalLeadList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalLeadUnitFormBean = new ProposalLeadUnitFormBean();
                proposalLeadRow = (HashMap)result.elementAt(rowIndex);
                proposalLeadUnitFormBean.setProposalNumber( (String)
                proposalLeadRow.get("PROPOSAL_NUMBER"));
                proposalLeadUnitFormBean.setPersonId( (String)
                proposalLeadRow.get("PERSON_ID"));
                proposalLeadUnitFormBean.setUnitNumber( (String)
                proposalLeadRow.get("UNIT_NUMBER"));
                proposalLeadUnitFormBean.setUnitName( (String)
                proposalLeadRow.get("UNIT_NAME"));
                proposalLeadUnitFormBean.setLeadUnitFlag(
                        proposalLeadRow.get("LEAD_UNIT_FLAG") == null ? false :
                            (proposalLeadRow.get("LEAD_UNIT_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalLeadUnitFormBean.setUpdateTimestamp(
                        (Timestamp)proposalLeadRow.get("UPDATE_TIMESTAMP"));
                proposalLeadUnitFormBean.setUpdateUser( (String)
                proposalLeadRow.get("UPDATE_USER"));
                proposalLeadList.add(proposalLeadUnitFormBean);
            }
        }
        return proposalLeadList;
    }




    /**
     * Method used to get proposal lead unit details from OSP$EPS_PROP_UNITS
     * for a given proposal number
     * <li>To fetch the data, it uses GET_PROPOSAL_UNITS_DW procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return ProposalLeadUnitFormBean  with proposal lead units details .
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */

     public CoeusVector getProposalKeyPersonDetails(String proposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector cvKeyPersons = null;
        HashMap row = null;
        Vector param= new Vector();

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));

//        param.addElement(new Parameter("SEQUENCE_NUMBER",
//                DBEngineConstants.TYPE_STRING,String.valueOf(sequenceNumber)));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROPOSAL_KEY_PERSONS( <<PROPOSAL_NUMBER>>,"
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        ProposalKeyPersonFormBean proposalKeyPersonFormBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                cvKeyPersons = new CoeusVector();
                int rowId = 0;
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    proposalKeyPersonFormBean = new ProposalKeyPersonFormBean();
                    row = (HashMap) result.elementAt(rowIndex);
                    proposalKeyPersonFormBean.setProposalNumber(
                            (String)row.get("PROPOSAL_NUMBER"));
                    proposalKeyPersonFormBean.setSequenceNumber(
                            Integer.parseInt(row.get(
                            "SEQUENCE_NUMBER") == null ? "0" : row.get(
                            "SEQUENCE_NUMBER").toString()));
                    proposalKeyPersonFormBean.setPersonId((String)row.get("PERSON_ID"));
                    proposalKeyPersonFormBean.setPersonName((String)row.get("PERSON_NAME"));
                    proposalKeyPersonFormBean.setProjectRole((String)row.get("PROJECT_ROLE"));
                    proposalKeyPersonFormBean.setFacultyFlag("Y".equals((String)row.get("FACULTY_FLAG"))? true :false);
                    proposalKeyPersonFormBean.setNonMITPersonFlag("Y".equals((String)row.get("NON_MIT_PERSON_FLAG")) ? true : false);
                    proposalKeyPersonFormBean.setPercentageEffort(
                            Float.parseFloat(row.get( "PERCENTAGE_EFFORT") == null ?
                                "0" : row.get("PERCENTAGE_EFFORT").toString()));
                    proposalKeyPersonFormBean.setAcademicYearEffort(
                            Float.parseFloat(row.get( "ACADEMIC_YEAR_EFFORT") == null ?
                                "0" : row.get("ACADEMIC_YEAR_EFFORT").toString()));
                    proposalKeyPersonFormBean.setSummerYearEffort(
                            Float.parseFloat(row.get( "SUMMER_YEAR_EFFORT") == null ?
                                "0" : row.get("SUMMER_YEAR_EFFORT").toString()));
                    proposalKeyPersonFormBean.setCalendarYearEffort(
                            Float.parseFloat(row.get( "CALENDAR_YEAR_EFFORT") == null ?
                                "0" : row.get("CALENDAR_YEAR_EFFORT").toString()));
                    proposalKeyPersonFormBean.setUpdateTimestamp(
                            (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    proposalKeyPersonFormBean.setUpdateUser( (String)
                    row.get("UPDATE_USER"));
                    proposalKeyPersonFormBean.setKeyPersonsUnits(
                    getProposalKeyPersonUnits(proposalKeyPersonFormBean.getProposalNumber(),
                        proposalKeyPersonFormBean.getPersonId()));
                    //ppc change for keyperson starts
                    proposalKeyPersonFormBean.setPpcCertifyFlag(Integer.parseInt(row.get(
                            "CERTIFICATION_FLAG") == null ? "0" : row.get(
                            "CERTIFICATION_FLAG").toString()));
                        //ppc change for keyperson ends
                cvKeyPersons.add(proposalKeyPersonFormBean);
                }
            }
        }
        return cvKeyPersons;
    }


      //CHANGES FOR THE KEY PERSON UNIT
     public CoeusVector getProposalKeyPersonUnits(String proposalNumber, String personId)
            throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();

        KeyPersonUnitBean keyPersonUnitBean = null;
        HashMap proposalLeadRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERSON_ID",
                                    DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROPOSAL_KP_UNITS( <<PROPOSAL_NUMBER>> , <<PERSON_ID>>,"+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector proposalLeadList = null;
        if (listSize > 0){
            proposalLeadList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                keyPersonUnitBean = new KeyPersonUnitBean();
                proposalLeadRow = (HashMap)result.elementAt(rowIndex);
                keyPersonUnitBean.setProposalNumber( (String)
                                proposalLeadRow.get("PROPOSAL_NUMBER"));
                keyPersonUnitBean.setSequenceNumber(proposalLeadRow.get("SEQUENCE_NUMBER") == null ? 0
                    : Integer.parseInt(proposalLeadRow.get("SEQUENCE_NUMBER").toString()));
                keyPersonUnitBean.setPersonId( (String)
                    proposalLeadRow.get("PERSON_ID"));
                keyPersonUnitBean.setAw_PersonId( (String)
                    proposalLeadRow.get("PERSON_ID"));
                keyPersonUnitBean.setUnitNumber( (String)
                    proposalLeadRow.get("UNIT_NUMBER"));
                keyPersonUnitBean.setAw_UnitNumber( (String)
                    proposalLeadRow.get("UNIT_NUMBER"));
                keyPersonUnitBean.setUnitName( (String)
                    proposalLeadRow.get("UNIT_NAME"));

                keyPersonUnitBean.setUpdateTimestamp(
                    (Timestamp)proposalLeadRow.get("UPDATE_TIMESTAMP"));
                keyPersonUnitBean.setUpdateUser( (String)
                    proposalLeadRow.get("UPDATE_USER"));

                proposalLeadList.add(keyPersonUnitBean);
            }
        }
        return proposalLeadList;
    }




    //CHANGES FOR THE KEY PERSON UNITS ENDS




    //Commented for case 2406  - Proposal and Organization - start
    /**
     * Method used to get proposal location details from OSP$EPS_PROP_LOCATION
     * for a given proposal number
     * <li>To fetch the data, it uses DW_GET_PROP_LOCATION procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return ProposalLocationFormBean  with proposal location details .
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
//    public Vector getProposalLocation(String proposalNumber)
//    throws CoeusException, DBException{
//        Vector result = null;
//        Vector param= new Vector();
//        ProposalLocationFormBean proposalLocationFormBean = null;
//        HashMap proposalLocRow = null;
//        param.addElement(new Parameter("PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING,proposalNumber));
//        if(dbEngine !=null){
//            result = new Vector(3,2);
//            result = dbEngine.executeRequest("Coeus",
//                    "call DW_GET_PROP_LOCATION( <<PROPOSAL_NUMBER>> , "+
//                    "<<OUT RESULTSET rset>> )", "Coeus", param);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        int listSize = result.size();
//        Vector proposalLocList = null;
//        if (listSize > 0){
//            proposalLocList = new Vector(3,2);
//            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
//                proposalLocationFormBean = new ProposalLocationFormBean();
//                proposalLocRow = (HashMap)result.elementAt(rowIndex);
//                proposalLocationFormBean.setProposalNumber( (String)
//                proposalLocRow.get("PROPOSAL_NUMBER"));
//                proposalLocationFormBean.setProposalLocation( (String)
//                proposalLocRow.get("LOCATION"));
//                proposalLocationFormBean.setRolodexId(
//                        Integer.parseInt(proposalLocRow.get(
//                        "ROLODEX_ID") == null ? "0" :
//                            proposalLocRow.get("ROLODEX_ID").toString()));
//                proposalLocationFormBean.setUpdateTimestamp(
//                        (Timestamp)proposalLocRow.get("UPDATE_TIMESTAMP"));
//                proposalLocationFormBean.setUpdateUser( (String)
//                proposalLocRow.get("UPDATE_USER"));
//                proposalLocList.add(proposalLocationFormBean);
//            }
//        }
//        return proposalLocList;
//    }
    //Commented for case 2406  - Proposal and Organization - end

    /**
     * Method used to get proposal science code details from OSP$EPS_PROP_SCIENCE_CODE
     * for a given proposal number
     * <li>To fetch the data, it uses DW_GET_SCIENCE_CODES_FOR_PROP procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProposalScienceCodeFormBean for science code tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalScienceCode(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalScienceCodeFormBean proposalScienceCodeFormBean = null;
        HashMap proposalScienceRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_SCIENCE_CODES_FOR_PROP( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector proposalScienceList = null;
        if (listSize > 0){
            proposalScienceList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalScienceCodeFormBean = new ProposalScienceCodeFormBean();
                proposalScienceRow = (HashMap)result.elementAt(rowIndex);

                proposalScienceCodeFormBean.setProposalNumber( (String)
                proposalScienceRow.get("PROPOSAL_NUMBER"));
                proposalScienceCodeFormBean.setScienceCode( (String)
                proposalScienceRow.get("SCIENCE_CODE"));
                proposalScienceCodeFormBean.setDescription( (String)
                proposalScienceRow.get("DESCRIPTION"));
                proposalScienceCodeFormBean.setUpdateTimestamp(
                        (Timestamp)proposalScienceRow.get("UPDATE_TIMESTAMP"));
                proposalScienceCodeFormBean.setUpdateUser( (String)
                proposalScienceRow.get("UPDATE_USER"));
                proposalScienceList.add(proposalScienceCodeFormBean);
            }
        }
        return proposalScienceList;
    }

    /**
     * Method used to get proposal science code details from OSP$EPS_PROP_SCIENCE_CODE
     * for a given proposal number
     * <li>To fetch the data, it uses DW_GET_SCIENCE_CODES_FOR_PROP procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProposalScienceCodeFormBean for science code tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalBioGraphy(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalBiographyFormBean proposalBiographyFormBean = null;
        HashMap proposalBioRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_PERBIO_FOR_P( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector proposalBioList = null;
        if (listSize > 0){
            proposalBioList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalBiographyFormBean = new ProposalBiographyFormBean();
                proposalBioRow = (HashMap)result.elementAt(rowIndex);
                proposalBiographyFormBean.setProposalNumber( (String)
                proposalBioRow.get("PROPOSAL_NUMBER"));
                proposalBiographyFormBean.setPersonId( (String)
                proposalBioRow.get("PERSON_ID"));
                proposalBiographyFormBean.setBioNumber(
                        Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
                proposalBiographyFormBean.setDescription( (String)
                proposalBioRow.get("DESCRIPTION"));
                proposalBiographyFormBean.setUpdateTimestamp(
                        (Timestamp)proposalBioRow.get("UPDATE_TIMESTAMP"));
                proposalBiographyFormBean.setUpdateUser( (String)
                proposalBioRow.get("UPDATE_USER"));
                proposalBioList.add(proposalBiographyFormBean);
            }
        }
        return proposalBioList;
    }

    /**
     * Method used to get proposal others details from OSP$EPS_PROP_CUSTOM_DATA
     * for a given proposal number
     * <li>To fetch the data, it uses DW_GET_EPS_PROP_CUSTOM_DATA procedure.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProposalCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalOthersDetails(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalCustomElementsInfoBean proposalOthersFormBean = null;
        HashMap othersRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROP_CUSTOM_DATA( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector othersList = null;
        if (listSize > 0){
            othersList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalOthersFormBean = new ProposalCustomElementsInfoBean();
                othersRow = (HashMap)result.elementAt(rowIndex);
                proposalOthersFormBean.setProposalNumber( (String)
                othersRow.get("PROPOSAL_NUMBER"));
                proposalOthersFormBean.setColumnName( (String)
                othersRow.get("COLUMN_NAME"));
                proposalOthersFormBean.setColumnValue( (String)
                othersRow.get("COLUMN_VALUE"));
                proposalOthersFormBean.setColumnLabel( (String)
                othersRow.get("COLUMN_LABEL"));
                proposalOthersFormBean.setDataType( (String)
                othersRow.get("DATA_TYPE"));
                proposalOthersFormBean.setDataLength(
                        Integer.parseInt(othersRow.get("DATA_LENGTH") == null ? "0" :
                            othersRow.get("DATA_LENGTH").toString()));
                proposalOthersFormBean.setDefaultValue( (String)
                othersRow.get("DEFAULT_VALUE"));
                proposalOthersFormBean.setHasLookUp(
                        othersRow.get("HAS_LOOKUP") == null ? false :
                            (othersRow.get("HAS_LOOKUP").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalOthersFormBean.setLookUpWindow( (String)
                othersRow.get("LOOKUP_WINDOW"));
                proposalOthersFormBean.setLookUpArgument( (String)
                othersRow.get("LOOKUP_ARGUMENT"));
                proposalOthersFormBean.setDescription( (String)
                othersRow.get("DESCRIPTION"));
                proposalOthersFormBean.setUpdateTimestamp(
                        (Timestamp)othersRow.get("UPDATE_TIMESTAMP"));
                proposalOthersFormBean.setUpdateUser( (String)
                othersRow.get("UPDATE_USER"));
                othersList.add(proposalOthersFormBean);
            }
        }
        return othersList;
    }

    /**
     * Method used to get proposal others details per person from
     * OSP$EPS_PROP_CUSTOM_DATA and OSP$CUSTOM_DATA_ELEMENTS for a given
     * proposal number and person id.
     * <li>To fetch the data, it uses GET_EPS_PROP_PER_CUST_DATA procedure.
     *
     * @param proposalnumber and person Id this is given as input parameter
     * for the procedure to execute.
     * @return Vector collections of ProposalCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalOthersDetails(String proposalNumber,String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalCustomElementsInfoBean proposalOthersFormBean = null;
        HashMap othersRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROP_PER_CUST_DATA(<<PROPOSAL_NUMBER>>,<<PERSON_ID>> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector othersList = null;
        if (listSize > 0){
            othersList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalOthersFormBean = new ProposalCustomElementsInfoBean();
                othersRow = (HashMap)result.elementAt(rowIndex);
                proposalOthersFormBean.setProposalNumber( (String)
                othersRow.get("PROPOSAL_NUMBER"));
                proposalOthersFormBean.setPersonId( (String)
                othersRow.get("PERSON_ID"));
                proposalOthersFormBean.setColumnName( (String)
                othersRow.get("COLUMN_NAME"));
                proposalOthersFormBean.setColumnValue( (String)
                othersRow.get("COLUMN_VALUE"));
                proposalOthersFormBean.setColumnLabel( (String)
                othersRow.get("COLUMN_LABEL"));
                proposalOthersFormBean.setDataType( (String)
                othersRow.get("DATA_TYPE"));
                proposalOthersFormBean.setDataLength(
                        Integer.parseInt(othersRow.get("DATA_LENGTH") == null ? "0" :
                            othersRow.get("DATA_LENGTH").toString()));
                proposalOthersFormBean.setDefaultValue( (String)
                othersRow.get("DEFAULT_VALUE"));
                proposalOthersFormBean.setHasLookUp(
                        othersRow.get("HAS_LOOKUP") == null ? false :
                            (othersRow.get("HAS_LOOKUP").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalOthersFormBean.setLookUpWindow( (String)
                othersRow.get("LOOKUP_WINDOW"));
                proposalOthersFormBean.setLookUpArgument( (String)
                othersRow.get("LOOKUP_ARGUMENT"));
                proposalOthersFormBean.setUpdateTimestamp(
                        (Timestamp)othersRow.get("UPDATE_TIMESTAMP"));
                proposalOthersFormBean.setUpdateUser( (String)
                othersRow.get("UPDATE_USER"));
                proposalOthersFormBean.setDescription( (String)
                othersRow.get("DESCRIPTION"));
                othersList.add(proposalOthersFormBean);
            }
        }
        return othersList;
    }

    /**
     * Method used to get Questions List for type'I'.
     * <li>To fetch the data, it uses DW_GET_YNQ_FOR_QTYPE procedure.
     *
     * @param questionType
     * @return Vector collections of QuestionListBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCertifyQuestions(String questionType)
    throws CoeusException, DBException{
        Vector questionLists = null;
        if (questionType != null) {
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("QUESTION_TYPE", "String", questionType));
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                        "call DW_GET_YNQ_FOR_QTYPE ( <<QUESTION_TYPE>> , <<OUT RESULTSET rset>> )",
                        "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }

            if (result != null) {
                questionLists = new Vector();
                for (int i = 0; i < result.size(); i++) {
                    HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                    QuestionListBean questionListBean = new QuestionListBean();
                    questionListBean.setQuestionId( (String) orgDetailsRow.get("QUESTION_ID"));
                    questionListBean.setDescription( (String) orgDetailsRow.get("DESCRIPTION"));
                    questionListBean.setNoOfAnswers(Integer.parseInt(
                            orgDetailsRow.get("NO_OF_ANSWERS") == null ?
                                "0" : orgDetailsRow.get("NO_OF_ANSWERS").toString()));
                    questionListBean.setExplanationRequiredFor( (String) orgDetailsRow.get("EXPLANATION_REQUIRED_FOR"));
                    questionListBean.setDateRequiredFor(orgDetailsRow.get("DATE_REQUIRED_FOR") == null ? null : orgDetailsRow.get("DATE_REQUIRED_FOR").toString() );
                    /*
                     *Added by Geo on 01/02/07
                     *updating the status to the bean for fixing #2698
                     */
                    questionListBean.setStatus( (String) orgDetailsRow.get("STATUS"));
                    questionLists.addElement(questionListBean);
                }
            }
        }
        return questionLists;

    }


    /**
     * Method used to get proposal others details per person from
     * OSP$EPS_PROP_PERS_YNQ for a given  proposal number and person id.
     * <li>To fetch the data, it uses DW_GET_PROP_PERS_YNQ_FOR_PP procedure.
     *
     * @param proposalnumber and person Id this is given as input parameter
     * for the procedure to execute.
     * @return Vector collections of ProposalYNQFormBean for proposal menu
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */

    public Vector getCertifyAnswers(String proposalNumber,String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
//        YNQBean ynqBean = null;
        ProposalYNQFormBean proposalYNQFormBean = null;
        HashMap yNQRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_PERS_YNQ_FOR_PP(<<PROPOSAL_NUMBER>>,<<PERSON_ID>> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector yNQList = null;
        if (listSize > 0){
            yNQList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                /*
                ynqBean = new YNQBean();
                yNQRow = (HashMap)result.elementAt(rowIndex);
                ynqBean.setQuestionId( (String) yNQRow.get("QUESTION_ID"));
                ynqBean.setAnswer( (String) yNQRow.get("ANSWER"));
                ynqBean.setUpdateTimeStamp(	(Timestamp) yNQRow.get("UPDATE_TIMESTAMP"));
                ynqBean.setUpdateUser( (String)	yNQRow.get("UPDATE_USER"));
                yNQList.add(ynqBean);
                 */
                proposalYNQFormBean = new ProposalYNQFormBean();
                yNQRow = (HashMap)result.elementAt(rowIndex);
                proposalYNQFormBean.setProposalNumber( (String)
                yNQRow.get("PROPOSAL_NUMBER"));
                proposalYNQFormBean.setPersonId( (String)
                yNQRow.get("PERSON_ID"));
                proposalYNQFormBean.setQuestionId( (String)
                yNQRow.get("QUESTION_ID"));
                proposalYNQFormBean.setAnswer( (String) yNQRow.get("ANSWER") );
                proposalYNQFormBean.setUpdateTimeStamp(
                        (Timestamp)yNQRow.get("UPDATE_TIMESTAMP"));
                proposalYNQFormBean.setUpdateUser( (String)
                yNQRow.get("UPDATE_USER"));
                yNQList.add(proposalYNQFormBean);

            }
        }
        return yNQList;
    }
    //Commented for case 2406 - Organization and Location - start
    /**
     * Method used to get proposallocation list from OSP$EPS_PROP_LOCATION for
     * a given proposalnumber .
     * <li>To fetch the data, it uses get_prop_location_list procedure.
     *
     * @param proposalNumber get list of proposalLoctionList for this id
     * @return Vector map of Proposal Location data is set of
     * proposalLocationFormBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
//    public Vector getProposalLocationList(String proposalNumber)
//    throws CoeusException , DBException{
//        Vector result = new Vector(3,2);
//        Vector param= new Vector();
//        HashMap locationProposalListRow = null;
//        ProposalLocationFormBean proposalLocationFormBean = null;
//        param.addElement(new Parameter("PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING,proposalNumber));
//
//        if(dbEngine!=null){
//            result = dbEngine.executeRequest("Coeus",
//                    "call get_prop_location_list( <<PROPOSAL_NUMBER>> , "
//                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        int listSize = result.size();
//        StringBuffer strBffr ;
//        Vector locationList = null;
//        if(listSize >0){
//            locationList = new Vector(3,2);
//            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
//                strBffr = new StringBuffer();
//                proposalLocationFormBean = new ProposalLocationFormBean();
//                locationProposalListRow = (HashMap)result.elementAt(rowIndex);
//                proposalLocationFormBean.setProposalNumber(
//                        (String)locationProposalListRow.get(
//                        "PROPOSAL_NUMBER"));
//                proposalLocationFormBean.setProposalLocation((String)
//                locationProposalListRow.get("LOCATION"));
//                proposalLocationFormBean.setRolodexId(Integer.parseInt(
//                        locationProposalListRow.get("ROLODEX_ID") == null
//                        ? "0" : locationProposalListRow.get("ROLODEX_ID").toString()));
//                strBffr.append((String)
//                locationProposalListRow.get("ORGANIZATION") == null
//                        ? "" :locationProposalListRow.get("ORGANIZATION")
//                        .toString()+ "$#");
//                strBffr.append((String)
//                locationProposalListRow.get("ADDRESS_LINE_1") == null
//                        ? "" :locationProposalListRow.get("ADDRESS_LINE_1")
//                        .toString()+ "$#");
//                strBffr.append((String)
//                locationProposalListRow.get("ADDRESS_LINE_2") == null
//                        ? "" :locationProposalListRow.get("ADDRESS_LINE_2")
//                        .toString()+ "$#");
//                strBffr.append((String)
//                locationProposalListRow.get("ADDRESS_LINE_3") == null
//                        ? "" :locationProposalListRow.get("ADDRESS_LINE_3")
//                        .toString()+ "$#");
//                strBffr.append((String)
//                locationProposalListRow.get("CITY") == null
//                        ? "" :locationProposalListRow.get("CITY").toString()+ "$#");
//                strBffr.append((String)
//                locationProposalListRow.get("COUNTY") == null
//                        ? "" :locationProposalListRow.get("COUNTY").toString()+ "$#");
//                strBffr.append((String)
//                locationProposalListRow.get("STATE") == null
//                        ? "" :locationProposalListRow.get("STATE").toString()+ " - ");
//                strBffr.append((String)
//                locationProposalListRow.get("POSTAL_CODE") == null
//                        ? "" :locationProposalListRow.get("POSTAL_CODE")
//                        .toString()+ "$#");
//                strBffr.append((String)
//                locationProposalListRow.get("COUNTRY_CODE") == null
//                        ? "" :locationProposalListRow.get("COUNTRY_CODE").toString());
//                proposalLocationFormBean.setAddress( (String) strBffr.toString());
//                proposalLocationFormBean.setUpdateTimestamp(
//                        (Timestamp)locationProposalListRow.get("UPDATE_TIMESTAMP"));
//                proposalLocationFormBean.setUpdateUser((String)
//                locationProposalListRow.get("UPDATE_USER"));
//
//                locationList.add(proposalLocationFormBean);
//            }
//        }
//        return locationList;
//    }
    //Commented for case 2406 - Organization and Location - end

    /**
     * Method used to get proposalyesNoQuestion list from OSP$YNQ_EXPLANATION for
     * a given question Id .
     * <li>To fetch the data, it uses DW_GET_YNQ_EXPLANATION procedure.
     *
     * @param questionId get list of proposalyesNoQuestion for this id
     * @return Vector map of Proposal yesNoQuestion data is set of
     * ProposalYNQExplantionFormBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalYNQExplantion(String questionId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalYNQExplantionFormBean proposalYNQExplantionFormBean = null;
        HashMap yNQExplantionRow = null;
        param.addElement(new Parameter("QUESTION_ID",
                DBEngineConstants.TYPE_STRING,questionId));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_YNQ_EXPLANATION(<<QUESTION_ID>> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector yNQExplantionList = null;
        if (listSize > 0){
            yNQExplantionList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalYNQExplantionFormBean = new ProposalYNQExplantionFormBean();
                yNQExplantionRow = (HashMap)result.elementAt(rowIndex);
                proposalYNQExplantionFormBean.setQuestionId( (String)
                yNQExplantionRow.get("QUESTION_ID"));
                proposalYNQExplantionFormBean.setExplantionType(
                        yNQExplantionRow.get("EXPLANATION_TYPE").toString().charAt(0));
                proposalYNQExplantionFormBean.setExplantion( (String)
                yNQExplantionRow.get("EXPLANATION"));
                // Check This
                //proposalYNQExplantionFormBean.setUpdateTimestamp(
                //(Timestamp)yNQExplantionRow.get("UPDATE_TIMESTAMP"));
                //proposalYNQExplantionFormBean.setUpdateUser( (String)
                //yNQExplantionRow.get("UPDATE_USER"));
                yNQExplantionList.add(proposalYNQExplantionFormBean);
            }
        }
        return yNQExplantionList;
    }


    /**
     * Method used to get proposalyesNoQuestion list from OSP$RIGHTS and
     * OSP$ROLE_RIGHTS for a given role Id .
     * <li>To fetch the data, it uses DW_GET_ROLE_RIGHTS_FOR_ROLE procedure.
     *
     * @param roleId get list of proposalRoles details for this id
     * @return Vector map of Proposal Roles data is set of
     * ProposalRoleRightFormBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalRightRole(int roleId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        RoleRightInfoBean proposalRoleRightFormBean = null;
        HashMap rightRoleRow = null;
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,new Integer(roleId).toString()));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_ROLE_RIGHTS_FOR_ROLE(<<ROLE_ID>> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector rightRoleList = null;
        if (listSize > 0){
            rightRoleList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalRoleRightFormBean = new RoleRightInfoBean();

                rightRoleRow = (HashMap)result.elementAt(rowIndex);

                proposalRoleRightFormBean.setRightId( (String)
                rightRoleRow.get("RIGHT_ID"));
                proposalRoleRightFormBean.setRoleId(
                        Integer.parseInt(rightRoleRow.get("ROLE_ID").toString()));
                proposalRoleRightFormBean.setDescendFlag(
                        rightRoleRow.get("DESCEND_FLAG") == null ? false :
                            (rightRoleRow.get("DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalRoleRightFormBean.setDescription( (String)
                rightRoleRow.get("DESCRIPTION"));
                proposalRoleRightFormBean.setRightType(
                        rightRoleRow.get("RIGHT_TYPE").toString().charAt(0));
                proposalRoleRightFormBean.setRoleDescendFlag(
                        rightRoleRow.get("DESCEND_FLAG_1") == null ? false :
                            (rightRoleRow.get("DESCEND_FLAG_1").toString()
                            .equalsIgnoreCase("y") ? true :false));
                rightRoleList.add(proposalRoleRightFormBean);
            }
        }
        return rightRoleList;
    }


    /**
     * Method used to get proposalyesNoQuestion list from OSP$RIGHTS ,OSP$ROLE_RIGHTS
     * and OSP$EPS_PROP_USER_ROLES for a given proposal number and user Id .
     * <li>To fetch the data, it uses DW_GET_PROP_RIGHTS_FOR_USER procedure.
     *
     * @param proposalnumber and userId get list of proposalRolesRight details
     * @return Vector map of Proposal Roles Right data is set of
     * ProposalRoleRightFormBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalRightForUser(String proposalNumber,String userId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        RoleRightInfoBean proposalRoleRightFormBean = null;
        HashMap rightRoleRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,userId));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_RIGHTS_FOR_USER(<<PROPOSAL_NUMBER>> , << USER_ID >> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector rightRoleList = null;
        if (listSize > 0){
            rightRoleList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalRoleRightFormBean = new RoleRightInfoBean();

                rightRoleRow = (HashMap)result.elementAt(rowIndex);

                proposalRoleRightFormBean.setRightId( (String)
                rightRoleRow.get("RIGHT_ID"));
                proposalRoleRightFormBean.setDescription( (String)
                rightRoleRow.get("DESCRIPTION"));
                proposalRoleRightFormBean.setRightType(
                        rightRoleRow.get("RIGHT_TYPE").toString().charAt(0));
                proposalRoleRightFormBean.setDescendFlag(
                        rightRoleRow.get("DESCEND_FLAG") == null ? false :
                            (rightRoleRow.get("DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));

                rightRoleList.add(proposalRoleRightFormBean);
            }
        }
        return rightRoleList;
    }

    /**
     * Gets the proposal admin Details for the given proposal number.
     * It returns a ProposalAdminFormBean from OSP$PROPOSAL_ADMIN_DETAILS table.
     * <li>To fetch the data, it uses the procedure DW_GET_PROP_ADMIN_DETAILS.
     *
     * @param String proposal Number to get certify details for proposal screen
     * @return ProposalAdminFormBean attribute of the proposal.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalAdminFormBean getProposalAdminDetails(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalAdminFormBean proposalAdminFormBean = null;
        HashMap adminDetailRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_ADMIN_DETAILS( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
//        Vector adminDetailList = null;
        if (listSize > 0){
            //adminDetailList = new Vector(3,2);
            proposalAdminFormBean = new ProposalAdminFormBean();
            adminDetailRow = (HashMap)result.elementAt(0);
            proposalAdminFormBean.setDevProposalNumber( (String)
            adminDetailRow.get("DEV_PROPOSAL_NUMBER"));
            proposalAdminFormBean.setInstProposalNumber( (String)
            adminDetailRow.get("INST_PROPOSAL_NUMBER"));
            proposalAdminFormBean.setInstPropSeqNumber(
                    Integer.parseInt(adminDetailRow.get("INST_PROP_SEQUENCE_NUMBER") == null
                    ? "0" : adminDetailRow.get("INST_PROP_SEQUENCE_NUMBER").toString()));
            proposalAdminFormBean.setDateSubmittedByDept( (Timestamp)
            adminDetailRow.get("DATE_SUBMITTED_BY_DEPT"));
            proposalAdminFormBean.setDateReturnedToDept( (Timestamp)
            adminDetailRow.get("DATE_RETURNED_TO_DEPT"));
            proposalAdminFormBean.setDateApprovedByOsp( (Timestamp)
            adminDetailRow.get("DATE_APPROVED_BY_OSP"));
            proposalAdminFormBean.setDateSubmittedToAgency( (Timestamp)
            adminDetailRow.get("DATE_SUBMITTED_TO_AGENCY"));
            proposalAdminFormBean.setCreatedDate( (Timestamp)
            adminDetailRow.get("INST_PROP_CREATE_DATE"));
            proposalAdminFormBean.setCreatedUser( (String)
            adminDetailRow.get("INST_PROP_CREATE_USER"));
            proposalAdminFormBean.setSignedBy( (String)
            adminDetailRow.get("SIGNED_BY"));
            proposalAdminFormBean.setSubmissionType(
                    adminDetailRow.get("SUBMISSION_TYPE").toString().charAt(0));

            // get the created by user name
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            UserInfoBean userInfoBean = userDetailsBean.getUserInfo((String) adminDetailRow.get("INST_PROP_CREATE_USER"));
            proposalAdminFormBean.setCreatedUserName(userInfoBean.getUserName());

            // get the signed by user name
            userInfoBean = userDetailsBean.getUserInfo((String) adminDetailRow.get("SIGNED_BY"));
            proposalAdminFormBean.setSignedUserName(userInfoBean.getUserName());

            proposalAdminFormBean.setInvCertification( getProposalCertifyDetails(proposalNumber));

            //adminDetailList.add(proposalAdminFormBean);
        }
        return proposalAdminFormBean;
    }

    /**
     * Gets all the proposal admin Details for proposal number . The
     * return Vector(Collection) from OSP$PROPOSAL_INV_CERTIFICATION table.
     * <li>To fetch the data, it uses the procedure DW_GET_ALL_INV_CERT_FOR_PROP.
     *
     * @param String proposal Number to get certify details for proposal screen
     * @return ProposalCertificationFormBean attributes of the proposal.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalCertifyDetails(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalCertificationFormBean proposalCertificationFormBean = null;
        HashMap certifyDetailRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_ALL_INV_CERT_FOR_PROP( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector certifyDetailList = null;
        if (listSize > 0){
            certifyDetailList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalCertificationFormBean = new ProposalCertificationFormBean();
                certifyDetailRow = (HashMap)result.elementAt(rowIndex);
                proposalCertificationFormBean.setProposalNumber( (String)
                certifyDetailRow.get("PROPOSAL_NUMBER"));
                proposalCertificationFormBean.setPersonId( (String)
                certifyDetailRow.get("PERSON_ID"));
                proposalCertificationFormBean.setPersonName( (String)
                certifyDetailRow.get("PERSON_NAME"));
                proposalCertificationFormBean.setCertifyFlag(
                        certifyDetailRow.get("CERTIFIED_FLAG") == null ? false :
                            (certifyDetailRow.get("CERTIFIED_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                proposalCertificationFormBean.setDateCertify(
                        certifyDetailRow.get("DATE_CERTIFIED") == null ?
                            null : new Date(((Timestamp) certifyDetailRow.get(
                        "DATE_CERTIFIED")).getTime()));
                proposalCertificationFormBean.setDateReceivedByOsp(
                        certifyDetailRow.get("DATE_RECEIVED_BY_OSP") == null ?
                            null : new Date(((Timestamp) certifyDetailRow.get(
                        "DATE_RECEIVED_BY_OSP")).getTime()));
                proposalCertificationFormBean.setUpdateTimestamp( (Timestamp)
                certifyDetailRow.get("UPDATE_TIMESTAMP"));
                proposalCertificationFormBean.setUpdateUser( (String)
                certifyDetailRow.get("UPDATE_USER"));
                certifyDetailList.add(proposalCertificationFormBean);
            }
        }
        return certifyDetailList;
    }

    /**
     * Gets all the proposal role id and role name for user id . The
     * return Vector(Collection) from OSP$ROlE table.the methods gets the
     * user id by user id it gets the unit number by passing the unit number
     * role id and role name is got.
     * <li>To fetch the data, it uses the procedure DW_GET_PROPOSAL_ROLES_FOR_UNIT.
     *
     * @param String user id to get role id and role name for proposal screen
     * @return Vector of ComboBox bean whic return role id and role name.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getRoleAndRoleName(String userId) throws CoeusException,DBException{
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        UserInfoBean userInfoBean = userDetailsBean.getUserInfo(userId);
        String unitNumber = userInfoBean.getUnitNumber();

        Vector result = null;
        Vector vecProposalRoles = null;
        Vector param= new Vector();
        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        HashMap hasProposalRoles = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_ROLES_FOR_UNIT(<<UNIT_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            vecProposalRoles = new Vector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hasProposalRoles = (HashMap)result.elementAt(rowIndex);
                vecProposalRoles.addElement(new ComboBoxBean(
                        hasProposalRoles.get("ROLE_ID").toString(),
                        hasProposalRoles.get("ROLE_NAME").toString()));
            }
        }
        return vecProposalRoles;
    }

    /**
     * Gets all the proposal role for user Details for proposal number and role id. The
     * return Vector(Collection) from OSP$EPS_PROP_USER_ROLES and OSP$USER table.
     * <li>To fetch the data, it uses the procedure DW_GET_USERS_FOR_PROP_ROLE.
     *
     * @param String proposal Number and integer role id to get roles for user details.
     * @return ProposalUserRoleFormBean attributes of the proposal.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getUsersForProposalRole(String proposalNumber,int roleId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalUserRoleFormBean proposalUserRoleFormBean = null;
        HashMap rightRoleRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,new Integer(roleId).toString()));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_USERS_FOR_PROP_ROLE(<<PROPOSAL_NUMBER>> , << ROLE_ID >> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector rightRoleList = null;
        if (listSize > 0){
            rightRoleList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalUserRoleFormBean = new ProposalUserRoleFormBean();

                rightRoleRow = (HashMap)result.elementAt(rowIndex);
                proposalUserRoleFormBean.setProposalNumber( (String)
                rightRoleRow.get("PROPOSAL_NUMBER"));
                proposalUserRoleFormBean.setUserId( (String)
                rightRoleRow.get("USER_ID"));
                proposalUserRoleFormBean.setRoleId(
                        Integer.parseInt(rightRoleRow.get("ROLE_ID") == null ? "0" :
                            rightRoleRow.get("ROLE_ID").toString()));
                proposalUserRoleFormBean.setUserName( (String)
                rightRoleRow.get("USER_NAME"));
                proposalUserRoleFormBean.setUnitNumber( (String)
                rightRoleRow.get("UNIT_NUMBER"));
                proposalUserRoleFormBean.setStatus(
                        rightRoleRow.get("STATUS").toString().charAt(0));
                proposalUserRoleFormBean.setUnitName( (String)
                rightRoleRow.get("UNIT_NAME"));
                proposalUserRoleFormBean.setUpdateTimestamp(
                        (Timestamp)rightRoleRow.get("UPDATE_TIMESTAMP"));
                proposalUserRoleFormBean.setUpdateUser( (String)
                rightRoleRow.get("UPDATE_USER"));

                rightRoleList.add(proposalUserRoleFormBean);
            }
        }
        return rightRoleList;
    }

    /**
     * Method used to get proposal others details from OSP$EPS_PROP_CUSTOM_DATA
     * for a given proposal number and others details for the loggedin user module.
     * <li>It uses DW_GET_EPS_PROP_CUSTOM_DATA to fetch others data for given proposal
     * number and DW_GET_CUST_COLUMNS_FOR_MODULE to get the custom data for the given module.
     * <li>fn_dev_prop_has_custom_data is used to check whether custom data is
     * available for the given proposal number
     * <li> fn_module_has_custom_data is used to check whether custom data is
     * available for the given module number.
     *
     * <li> get_parameter_value  is used to get the module number for the proposal
     * module by passing the code COEUS_MODULE_DEV_PROPOSAL.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProposalCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getOthersDetails(String proposalNumber)
    throws CoeusException, DBException{
        Vector param= new Vector();
        //ProposalCustomElementsInfoBean proposalOthersFormBean = null;
        //HashMap othersRow = null;
        Vector proposalOthers = null,moduleOthers = null;
        HashMap modId = null;
        Vector result = new Vector();
        TreeSet othersData = new TreeSet(new BeanComparator());
        int customPropCount = 0, customModCount = 0;
        String moduleId = "";
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER COUNT>>=call fn_dev_prop_has_custom_data ( "
                    + " << PROPOSAL_NUMBER >>)}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap propCount = (HashMap)result.elementAt(0);
            customPropCount = Integer.parseInt(propCount.get("COUNT").toString());
        }
        // get the module number for the proposal
        param.removeAllElements();
        param.add(new Parameter("PARAM_NAME",
                DBEngineConstants.TYPE_STRING,"COEUS_MODULE_DEV_PROPOSAL"));
        result = dbEngine.executeFunctions("Coeus",
                "{<<OUT STRING MOD_NAME>>=call get_parameter_value ( "
                + " << PARAM_NAME >>)}", param);
        if(!result.isEmpty()){
            modId = (HashMap)result.elementAt(0);
            moduleId = modId.get("MOD_NAME").toString();
        }

        // check whether any custom data is present for proposal module
        param.removeAllElements();
        param.add(new Parameter("MOD_NAME",
                DBEngineConstants.TYPE_STRING,moduleId));
        result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER COUNT>>=call fn_module_has_custom_data ( "
                + " << MOD_NAME >>)}", param);
        if(!result.isEmpty()){
            HashMap modCount = (HashMap)result.elementAt(0);
            customModCount = Integer.parseInt(modCount.get("COUNT").toString());
        }
        if ( ( customPropCount > 0 ) || ( customModCount > 0 )){
            // custom data is present for the proposal module. so get the
            // proposal custom data  and module custom data and send unique
            // set of custom data from both.
            if ( customPropCount > 0 ) {
                //get proposal custom data
                proposalOthers = getProposalOthersDetails(proposalNumber);
                othersData.addAll(proposalOthers);
            }
            if( customModCount > 0 ) {
                // get module custom data

                moduleOthers = departmentTxnBean.getPersonColumnModule(moduleId);
                moduleOthers = setAcTypeAsNew(moduleOthers);
                othersData.addAll(moduleOthers);
            }
            //Commmented following code on 4th June, 2004 - Prasanna
            /*else{
                // there is no custom data available for the proposal module.
                return null;
            }*/
            //Set Required property for existing Proposal Custom data
            if(proposalOthers!=null){
                CoeusVector cvModuleOthers = null;
                if(moduleOthers!=null){
                    cvModuleOthers = new CoeusVector();
                    cvModuleOthers.addAll(moduleOthers);
                }
                CustomElementsInfoBean customElementsInfoBean = null;
                CoeusVector cvFilteredData = null;
                for(int row = 0; row < proposalOthers.size(); row++){
                    customElementsInfoBean = (CustomElementsInfoBean)proposalOthers.elementAt(row);
                    if(cvModuleOthers==null){
                        customElementsInfoBean.setRequired(false);
                    }else{
                        cvFilteredData = cvModuleOthers.filter(new Equals("columnName", customElementsInfoBean.getColumnName()));
                        if(cvFilteredData==null || cvFilteredData.size()==0){
                            customElementsInfoBean.setRequired(false);
                        }else{
                            customElementsInfoBean.setRequired(((CustomElementsInfoBean)cvFilteredData.elementAt(0)).isRequired());
                        }
                    }
                }
            }
            CoeusVector cvCustomData = new CoeusVector();
            cvCustomData.addAll(new Vector(othersData));
            cvCustomData.sort("columnLabel", true, true);
            return (Vector)cvCustomData;
        }else{
            // there is no custom data available for the proposal module.
            return null;
        }
    }

    /**
     * sets AcType 'I' for the records copied from the module cost elements
     * to the proposal cost elements.
     * @param modOthers vector contain CustomElementsInfoBean
     */
    private Vector setAcTypeAsNew(Vector modOthers){
        if(modOthers != null && modOthers.size() > 0 ){
            int modCount = modOthers.size();
            CustomElementsInfoBean customBean;
            ProposalCustomElementsInfoBean proposalCustomElementsInfoBean;
            for ( int modIndex = 0; modIndex < modCount; modIndex++ ) {
                customBean = (CustomElementsInfoBean)modOthers.elementAt(modIndex);
                customBean.setAcType(INSERT_RECORD);
                proposalCustomElementsInfoBean = new ProposalCustomElementsInfoBean(customBean);
                modOthers.set(modIndex,proposalCustomElementsInfoBean);
            }
        }
        return modOthers;
    }


    /** This method gets all the proposal abstracts for the given proposal number. The
     * return Vector(Collection) from OSP$EPS_PROP_ABSTRACT table.
     * <li>To fetch the data, it uses the procedure DW_GET_PROP_ABSTRACTS.
     *
     * @return ProposalAbstractFormBean attributes of the proposal.
     * @param proposalNumber is given as input parameter for the procedure.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalAbstract(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalAbstractFormBean proposalAbstractFormBean = null;
        HashMap abstractRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_ABSTRACTS(<<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector propAbstractList = null;
        if (listSize > 0){
            propAbstractList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalAbstractFormBean = new ProposalAbstractFormBean();
                abstractRow = (HashMap)result.elementAt(rowIndex);

                proposalAbstractFormBean.setProposalNumber( (String)
                abstractRow.get("PROPOSAL_NUMBER"));
                proposalAbstractFormBean.setAbstractTypeCode(
                        Integer.parseInt(abstractRow.get("ABSTRACT_TYPE_CODE").toString()));
                proposalAbstractFormBean.setAwAbstractTypeCode(
                        Integer.parseInt(abstractRow.get("ABSTRACT_TYPE_CODE").toString()));
                proposalAbstractFormBean.setAbstractText((String)
                abstractRow.get("ABSTRACT").toString());
                proposalAbstractFormBean.setUpdateTimestamp(
                        (Timestamp)abstractRow.get("UPDATE_TIMESTAMP"));
                proposalAbstractFormBean.setUpdateUser( (String)
                abstractRow.get("UPDATE_USER"));

                propAbstractList.add(proposalAbstractFormBean);
            }
        }
        return propAbstractList;
    }


    /** This method gets all Abstracts Types. The
     * return is a Vector(Collection) from OSP$ABSTRACT_TYPE table.
     * <li>To fetch the data, it uses the procedure DW_GET_ABSTRACT_TYPES.
     *
     * @return Vector collections of AbstractTypeFormBean of the Abstract Types.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getAbstractTypes()
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        AbstractTypeFormBean abstractTypeFormBean = null;
        HashMap abstractRow = null;
        //param.addElement(new Parameter("PROPOSAL_NUMBER",
        //            DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_ABSTRACT_TYPES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector abstractTypeList = null;
        if (listSize > 0){
            abstractTypeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                abstractTypeFormBean = new AbstractTypeFormBean();
                abstractRow = (HashMap)result.elementAt(rowIndex);

                abstractTypeFormBean.setAbstractTypeId(
                        Integer.parseInt(abstractRow.get("ABSTRACT_TYPE_CODE").toString()));
                abstractTypeFormBean.setDescription( (String)
                abstractRow.get("DESCRIPTION"));
                abstractTypeFormBean.setUpdateTimestamp(
                        (Timestamp)abstractRow.get("UPDATE_TIMESTAMP"));
                abstractTypeFormBean.setUpdateUser( (String)
                abstractRow.get("UPDATE_USER"));

                abstractTypeList.add(abstractTypeFormBean);
            }
        }
        return abstractTypeList;
    }

    /** This method gets all the Questions for the given QuestionType. The
     * return Vector(Collection) from OSP$YNQ table.
     * <li>To fetch the data, it uses the procedure DW_GET_YNQ_LIST.
     *
     * @return Vector collections of YNQBean.
     * @param questionType is given as input parameter for the procedure.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getYesNoQuestionList(String questionType,String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        QuestionListBean yNQBean = null;
        HashMap ynqRow = null;
        param.addElement(new Parameter("QUESTION_TYPE",
                DBEngineConstants.TYPE_STRING,questionType));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_YNQ_LIST(<<QUESTION_TYPE>> , <<MODULE_ITEM_KEY>> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector ynqList = null;
        if (listSize > 0){
            ynqList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                yNQBean = new QuestionListBean();
                ynqRow = (HashMap)result.elementAt(rowIndex);

                yNQBean.setQuestionId( (String)
                ynqRow.get("QUESTION_ID"));
                yNQBean.setDescription( (String)
                ynqRow.get("DESCRIPTION"));
                yNQBean.setNoOfAnswers(Integer.parseInt(
                        ynqRow.get("NO_OF_ANSWERS") == null ?
                            "0" : ynqRow.get("NO_OF_ANSWERS").toString()));

                yNQBean.setExplanationRequiredFor( (String)
                ynqRow.get("EXPLANATION_REQUIRED_FOR"));
                yNQBean.setDateRequiredFor(ynqRow.get("DATE_REQUIRED_FOR") == null ? null : ynqRow.get("DATE_REQUIRED_FOR").toString() );
                ynqList.add(yNQBean);
            }
        }
        return ynqList;
    }

    /** This method gets all the Questions for the given Proposal Number. The
     * return Vector(Collection) from OSP$EPS_PROP_YNQ table.
     * <li>To fetch the data, it uses the procedure DW_GET_PROPOSAL_YNQ.
     *
     * @return Vector collections of ProposalYNQBean.
     * @param proposalNumber is given as input parameter for the procedure.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalYNQ(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalYNQBean proposalYNQBean = null;
        HashMap yNQRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROPOSAL_YNQ(<<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector ynqList = null;
        if (listSize > 0){
            ynqList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalYNQBean = new ProposalYNQBean();
                yNQRow = (HashMap)result.elementAt(rowIndex);

                proposalYNQBean.setProposalNumber( (String)
                yNQRow.get("PROPOSAL_NUMBER"));
                proposalYNQBean.setQuestionId( (String)
                yNQRow.get("QUESTION_ID"));
                proposalYNQBean.setAnswer( (String) yNQRow.get("ANSWER") );
                proposalYNQBean.setExplanation( (String) yNQRow.get("EXPLANATION") );
                proposalYNQBean.setReviewDate( yNQRow.get("REVIEW_DATE") == null ?
                    null : yNQRow.get("REVIEW_DATE").toString());
                proposalYNQBean.setUpdateTimeStamp(
                        (Timestamp)yNQRow.get("UPDATE_TIMESTAMP"));
                proposalYNQBean.setUpdateUser( (String)
                yNQRow.get("UPDATE_USER"));
                ynqList.add(proposalYNQBean);
            }
        }
        return ynqList;
    }

    /** This method is used to get whether the given proposal number has any Budget.
     * <li>To fetch the data, it uses the function FN_BUDGET_EXISTS.
     *
     * @return int count for the proposal number.
     * @param proposalNumber is given as input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getProposalHasBudget(String proposalNumber)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_BUDGET_EXISTS(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }

    /** This method is used to check whether it is ok to copy the Proposal Budget for the given Proposal Number and Unit number.
     * <li>To fetch the data, it uses the function FN_IS_OK_TO_COPY_PROPOSAL_BUD.
     *
     * @return int count for the proposal number.
     * @param proposalNumber is given as the first input parameter to the function.
     * @param unitNumber is given as the second input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getIsOkToCopyProposalBudget(String proposalNumber , String unitNumber)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("TARGET_UNIT",
                DBEngineConstants.TYPE_STRING,unitNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_IS_OK_TO_COPY_PROPOSAL_BUD(<< PROPOSAL_NUMBER >> , << TARGET_UNIT >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }

    /**
     *  This method is used to check whether the user has Proposal role.
     *  If the user has rights, it returns 1, else, returns 0.
     *  It uses the stored function FN_USER_HAS_PROPOSAL_ROLE to do the validation
     *  @param userId is given as the input parameter to the function.
     *  @param proposalNumber is given as the input parameter to the function.
     *  @param roleId is given as the input parameter to the function.
     *  @return int hasRight
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public int getUserHasProposalRole(String userId, String proposalNumber, int roleId)
    throws CoeusException,DBException{
        int hasRight = 0;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        param.addElement(new Parameter("USERID", DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("ROLEID", DBEngineConstants.TYPE_INT, new Integer(roleId).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ << OUT INTEGER HAS_RIGHT >> = call FN_USER_HAS_PROPOSAL_ROLE( << USERID >>, << PROPOSAL_NUMBER >> , << ROLEID >>) }",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap userRoleRow = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(userRoleRow.get("HAS_RIGHT").toString());
        }
        return hasRight;
    }

    /**
     *  This method is used to check whether the user has OSP rights.
     *  If the user has rights, it returns 1, else, returns 0.
     *  It uses the stored function FN_USER_HAS_OSP_RIGHT to do the validation
     *  @param userId is given as the input parameter to the function.
     *  @param rightId is given as the input parameter to the function.
     *  @return int hasRight
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public int getUserHasOSPRight(String userId, String rightId)
    throws CoeusException,DBException{
        int hasRight = 0;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        param.addElement(new Parameter("USERID", DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("RIGHTID", DBEngineConstants.TYPE_STRING, rightId));

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ << OUT INTEGER HAS_RIGHT >> = call FN_USER_HAS_OSP_RIGHT( << USERID >> , << RIGHTID >>) }",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap userRoleRow = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(userRoleRow.get("HAS_RIGHT").toString());
        }
        return hasRight;
    }

    /** This method is used to get Person Names, Unit Name and Unit Number for the given Proposal Number.
     * It returns Vector(Collection) from OSP$EPS_PROP_UNITS table.
     * <li>To fetch the data, it uses the procedure GET_PI_LU_NAMES.
     *
     * @param proposalNumber is given as the input parameter to the function.
     * @return Vector collections of UserInfoBean.
     * @exception CoeusException raised if dbEngine is null.
     * @exception DBException raised from the server side.
     */
    public Vector getPersonUnitNumberName(String proposalNumber)
    throws CoeusException,DBException{
//        int hasRight = 0;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        UserInfoBean userInfoBean = null;
        HashMap personRow = null;
        //calling stored function
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PI_LU_NAMES(<<PROPOSAL_NUMBER>> , "
                    +" << OUT STRING PERSON_NAME >> , << OUT STRING UNIT_NAME >> , "
                    +" << OUT STRING UNIT_NUMBER >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        Vector personList = null;
        if (listSize > 0){
            personList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                userInfoBean = new UserInfoBean();
                personRow = (HashMap)result.elementAt(rowIndex);
                userInfoBean.setPersonName(personRow.get("PERSON_NAME").toString());
                userInfoBean.setUnitName(personRow.get("UNIT_NAME").toString());
                userInfoBean.setUnitNumber(personRow.get("UNIT_NUMBER").toString());
                personList.add(userInfoBean);
            }
        }
        return personList;
    }

    /**
     * This method gets Special Review data for the given Proposal Number.
     * It returns  a Vector(Collection) from OSP$EPS_PROP_SPECIAL_REVIEW table.
     * <li>To fetch the data, it uses the procedure GET_PRO_SP_REV.
     *
     * @param proposalNumber is given as the input parameter to the procedure.
     * @return Vector collections of ProposalSpecialReviewFormBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalSpecialReview(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalSpecialReviewFormBean proposalSpecialReviewFormBean = null;
        HashMap specialReviewRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PRO_SP_REV( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        Vector specialReviewList = null;
        if (listSize > 0){
            specialReviewList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalSpecialReviewFormBean = new ProposalSpecialReviewFormBean();
                specialReviewRow = (HashMap)result.elementAt(rowIndex);

                proposalSpecialReviewFormBean.setProposalNumber( (String)
                specialReviewRow.get("PROPOSAL_NUMBER"));
                proposalSpecialReviewFormBean.setSpecialReviewNumber(
                        Integer.parseInt(specialReviewRow.get("SPECIAL_REVIEW_NUMBER").toString()));
                proposalSpecialReviewFormBean.setSpecialReviewCode(
                        Integer.parseInt(specialReviewRow.get("SPECIAL_REVIEW_CODE").toString()));
                proposalSpecialReviewFormBean.setApprovalCode(
                        Integer.parseInt(specialReviewRow.get("APPROVAL_TYPE_CODE").toString()));
                proposalSpecialReviewFormBean.setProtocolSPRevNumber( (String)
                specialReviewRow.get("PROTOCOL_NUMBER"));
                proposalSpecialReviewFormBean.setApplicationDate(specialReviewRow.get("APPLICATION_DATE") == null ?
                    null : new Date(((Timestamp) specialReviewRow.get(
                        "APPLICATION_DATE")).getTime()));
                proposalSpecialReviewFormBean.setApprovalDate(specialReviewRow.get("APPROVAL_DATE") == null ?
                    null : new Date(((Timestamp) specialReviewRow.get(
                        "APPROVAL_DATE")).getTime()));
                proposalSpecialReviewFormBean.setComments( (String)
                specialReviewRow.get("COMMENTS"));
                proposalSpecialReviewFormBean.setUpdateUser( (String)
                specialReviewRow.get("UPDATE_USER"));
                proposalSpecialReviewFormBean.setUpdateTimestamp(
                        (Timestamp)specialReviewRow.get("UPDATE_TIMESTAMP"));
                proposalSpecialReviewFormBean.setSpecialReviewDescription( (String)
                specialReviewRow.get("SPECIAL_REVIEW_DESCRIPTION"));
                proposalSpecialReviewFormBean.setApprovalDescription( (String)
                specialReviewRow.get("APPROVAL_TYPE_DESCRIPTION"));
                //COEUSQA-2984 : Statuses in special review - start
                proposalSpecialReviewFormBean.setProtocolStatusCode(specialReviewRow.get("PROTOCOL_STATUS_CODE") == null ?
                    0 : Integer.parseInt(specialReviewRow.get("PROTOCOL_STATUS_CODE").toString()));
                //COEUSQA-2984 : Statuses in special review - stop
                specialReviewList.add(proposalSpecialReviewFormBean);
            }
        }
        return specialReviewList;
    }

    /**
     * This Method is used to get Person Editable Columns from OSP$PERSON_EDITABLE_COLUMNS table.
     * <li>To fetch the data, it uses DW_GET_PERSON_EDITABLE_COLUMNS procedure.
     *
     * @return Vector collections of PersonEditableColumnsFormBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPersonEditableColumns()
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        PersonEditableColumnsFormBean personEditableColumnsFormBean = null;
        HashMap editColumnRow = null;

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PERSON_EDITABLE_COLUMNS( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        Vector editColumnList = null;
        if (listSize > 0){
            editColumnList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                personEditableColumnsFormBean = new PersonEditableColumnsFormBean();
                editColumnRow = (HashMap)result.elementAt(rowIndex);
                personEditableColumnsFormBean.setColumnName( (String)
                editColumnRow.get("COLUMN_NAME"));
                personEditableColumnsFormBean.setUpdateTimestamp(
                        (Timestamp)editColumnRow.get("UPDATE_TIMESTAMP"));
                personEditableColumnsFormBean.setUpdateUser( (String)
                editColumnRow.get("UPDATE_USER"));
                editColumnList.add(personEditableColumnsFormBean);
            }
        }
        return editColumnList;
    }


    public Vector getParamValue()
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        PersonEditableColumnsFormBean personEditableColumnsFormBean = null;
        HashMap paramValue = null;

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call get_parameter_value( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        Vector parameterValue = null;
        if (listSize > 0){
            parameterValue = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                RequesterBean  requesterB = new RequesterBean();
                paramValue = (HashMap)result.elementAt(rowIndex);
                requesterB.setParameterValue((String)
                paramValue.get("parameter"));

                 //enableProposaltoProtocolLink = Integer.parseInt(coeusParameterBean.getParameterValue());
                parameterValue.add(requesterB);
            }
        }
        return parameterValue;
    }





    /**
     * This Method is used to get Person Editable Columns from OSP$PERSON_EDITABLE_COLUMNS table.
     * <li>To fetch the data, it uses DW_GET_PERSON_EDITABLE_COLUMNS procedure.
     *
     * @param proposalNumber is used to get Users for this Proposal
     * @param unitNumber is used to get Users for this Unit Number
     * @param strMode is used to differentiate the mode like New or Modify/Display
     * @param loggedUserId is the logged in User Id
     * @return Vector collections of PersonEditableColumnsFormBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalUserRoles(String proposalNumber, String unitNumber, String strMode, String loggedUserId)
    throws CoeusException, DBException{
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        Vector vecRolesBean = new Vector();
        vecRolesBean = getProposalRoles(unitNumber);
        Vector roles = new Vector();
        Vector vecUserId = null;
        Hashtable hstRoleId = null;
        if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
            int outerlength = vecRolesBean.size();
            //If new records get all Users for this Unit number
            if(strMode.equals("N")) {
                hstRoleId = getUsersForRole(unitNumber);
            }
            for(int index1=0;index1<outerlength;index1++){
                RoleInfoBean roleInfoBean =
                        (RoleInfoBean)vecRolesBean.elementAt(index1);
                int roleId = roleInfoBean.getRoleId();
                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
                userRolesInfoBean.setIsRole(true);
                userRolesInfoBean.setRoleBean(roleInfoBean);

                //If Modify/Display mode get all Users for this Proposal No. and Role.
                if(strMode.equals("M")) {
                    vecUserId = getUsersForProposalRole(proposalNumber,roleId);
                } else {
                    //Get User Id vector for this Role Id
                    vecUserId = (Vector)hstRoleId.get(new Integer(roleId));

                    if ((vecUserId != null) && (vecUserId.size() >0)){
                        //Check if the Logged in User Id not present in vector
                        //Only in case of Add mode and Aggregator role
                        if(roleId == CoeusConstants.PROPOSAL_ROLE_ID) {
                            boolean blnFoundUserId = false;
                            for(int userIndex = 0;userIndex<vecUserId.size();userIndex++) {
                                String userId = (String)vecUserId.elementAt(userIndex);
                                if(userId.equals(loggedUserId)) {
                                    blnFoundUserId = true;
                                    break;
                                }
                            }
                            if(blnFoundUserId == false) {
                                vecUserId.add(loggedUserId);
                            }
                        }
                    }else{
                        //If new Mode and if the User does not exist,
                        //add Logged in User as Agregator - start - 02/02/2004
                        if(strMode.equals("N") && roleId == CoeusConstants.PROPOSAL_ROLE_ID){
                            vecUserId = new Vector();
                            vecUserId.add(loggedUserId);
                        }
                        //If new Mode and if the User does not exist,
                        //add Logged in User as Agregator - end - - 02/02/2004
                    }
                }
                if ((vecUserId != null) && (vecUserId.size() >0)){
                    userRolesInfoBean.setHasChildren(true);
                    int innerlength = vecUserId.size();
                    Vector users = new Vector();
                    ProposalUserRoleFormBean proposalUserRoleFormBean = null;
                    String userId = "";
                    for(int index2=0;index2<innerlength;index2++){
                        if(strMode.equals("M")) {
                            //In modify/display mode vector contains proposalUserRoleFormBean objects
                            proposalUserRoleFormBean = (ProposalUserRoleFormBean) vecUserId.elementAt(index2);
                            userId = proposalUserRoleFormBean.getUserId();
                        } else {
                            //In new mode vector contains String objects
                            userId = (String)vecUserId.elementAt(index2);
                        }

                        UserInfoBean userInfoBean =
                                userDetailsBean.getUserInfo(userId);
                        UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
                        userRolesInfoBean2.setUserBean(userInfoBean);
                        userRolesInfoBean2.setIsRole(false);
                        userRolesInfoBean2.setHasChildren(false);
                        users.addElement(userRolesInfoBean2);
                    }//end inner for loop
                    userRolesInfoBean.setUsers(users);
                }//end of inner  if loop
                roles.addElement(userRolesInfoBean);
            }//end of outer for loop
        }//end of outer if loop
        return roles;
    }

    /**
     * Method used to get proposal roles from osp$role for the given unit number.
     * <li>To fetch the data, it uses GET_PROPOSAL_ROLES_FOR_UNIT.
     *
     * @param unitNumber is used to get list of RoleInfoBean
     * @return vector
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProposalRoles(String unitNumber)throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap roleRow = null;
        RoleInfoBean roleInfoBean = null;

        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_ROLES_FOR_UNIT ( <<UNIT_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int listSize = result.size();
        Vector actionList = null;
        if(listSize>0){
            actionList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                roleInfoBean = new RoleInfoBean();
                roleRow = (HashMap)result.elementAt(rowIndex);
                roleInfoBean.setRoleId(Integer.parseInt(
                        roleRow.get("ROLE_ID") == null ? "0"
                        : roleRow.get("ROLE_ID").toString()));
                roleInfoBean.setRoleName((String)
                roleRow.get("ROLE_NAME"));
                roleInfoBean.setRoleDesc((String)
                roleRow.get("DESCRIPTION"));
                roleInfoBean.setRoleType(
                        roleRow.get("ROLE_TYPE").toString().charAt(0));
                roleInfoBean.setUnitNumber((String)
                roleRow.get("OWNED_BY_UNIT"));
                roleInfoBean.setDescend(roleRow.get("DESCEND_FLAG") == null ? false :
                    (roleRow.get("DESCEND_FLAG").toString().equalsIgnoreCase("y") ? true :false));
                roleInfoBean.setStatus(roleRow.get("STATUS_FLAG").toString().charAt(0));
                actionList.add(roleInfoBean);
            }
        }
        return actionList;
    }

    /**
     * Method used to get all user ids for the given Unit number
     * from OSP$USER_ROLES, OSP$USER and OSP$ROLE tables
     * <li>To fetch the data, it uses DW_GET_DEFAULT_PROP_USER_ROLES.
     *
     * @param unitNumber is used as input parameter for the procedure.
     * @return Hashtable collections of user id.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Hashtable getUsersForRole(String unitNumber)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProposalUser = null;
        HashMap hasProposalUser=null;
        Vector param= new Vector();
        String userId ="";
        Integer roleId ;

        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_DEFAULT_PROP_USER_ROLES ( <<UNIT_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }

        int typesCount = result.size();
        Hashtable hstRoleId = new Hashtable();
        Vector vctUserIds = null ;
        if (typesCount >0){
            vecProposalUser = new Vector();
            for(int types=0;types<typesCount;types++){
                hasProposalUser = (HashMap)result.elementAt(types);
                userId = hasProposalUser.get("USER_ID").toString();
                roleId = Integer.valueOf(hasProposalUser.get("ROLE_ID").toString());
                if(!hstRoleId.containsKey(roleId)) {
                    vctUserIds = new Vector();
                    vctUserIds.add(userId);
                    hstRoleId.put(roleId,vctUserIds);
                } else {
                    vctUserIds = (Vector)hstRoleId.get(roleId);
                    vctUserIds.add(userId);
                    hstRoleId.put(roleId,vctUserIds);
                }
                //vecProposalUser.add(userId);
            }
        }
        return hstRoleId;
    }

    /**
     * Method used to get user id for combination of proposal number,seqnumber and roleid
     * from OSP$EPS_PROP_USER_ROLES and OSP$USER
     * <li>To fetch the data, it uses DW_GET_USERS_FOR_PROPOSAL_ROLE.
     *
     * @param proposalNumber is used as input parameter for the procedure.
     * @param roleId is used as input parameter for the procedure.
     * @return Vector collections of user id
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getUsersForPropRole(String proposalNumber, int roleId)
    throws DBException{
        Vector result = new Vector(3,2);
        Vector vecProposalUser = null;
        HashMap hasProposalUser=null;
        Vector param= new Vector();
        String userId ="";
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,new Integer(roleId).toString()));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_USERS_FOR_PROPOSAL_ROLE ( <<PROPOSAL_NUMBER>> , "
                    +" <<ROLE_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }

        int typesCount = result.size();
        if (typesCount >0){
            vecProposalUser = new Vector();
            for(int types=0;types<typesCount;types++){
                hasProposalUser = (HashMap)result.elementAt(types);
                userId = hasProposalUser.get("USER_ID").toString();
                vecProposalUser.add(userId);
            }
        }
        return vecProposalUser;
    }

    /**
     * This method is used to check whether the Budget information of the Proposal can be copied.
     * This returns true if the Budget details can be copied else returns false.
     *
     * @param proposalNumber is used as an input parameter to the procedure.
     * @param userId is used as an input parameter to the procedure.
     * @return boolean indicating Budget can be copied or not.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isPropBudgetCopyAllowed(String proposalNumber, String userId, boolean hasRights)
        throws CoeusException, DBException{

        boolean blnFlag = false;

        if(getProposalHasBudget(proposalNumber) > 0)
        {
            //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - start
            if(hasRights){
                blnFlag = true;
            } else{
                if(getUserHasPropRightCount(userId,proposalNumber, CoeusConstants.MODIFY_BUDGET_RIGHT)>0 ||
                getUserHasPropRightCount(userId,proposalNumber, CoeusConstants.VIEW_BUDGET_RIGHT)>0 ) {
                    blnFlag = true;
                }
            } //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - End
        }
        return blnFlag;
    }

     /**
     * This method is used to check whether the Narrative information of the Proposal can be copied.
     * This returns true if the Narrative details can be copied else returns false.
     *
     * @param proposalNumber is used as an input parameter to the procedure.
     * @param userId is used as an input parameter to the procedure.
     * @param hasRights is used as an input parameter to the procedure.
     * @return boolean indicating Narrative can be copied or not.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isPropNarrativeCopyAllowed(String proposalNumber, String userId, boolean hasRights)
        throws CoeusException, DBException{

        boolean blnFlag = false;
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        if(proposalNarrativeTxnBean.getProposalHasNarrative(proposalNumber) > 0)
        {
             //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - start
            if(hasRights){
                blnFlag = true;
            } else{
                if(getUserHasPropRightCount(userId,proposalNumber, CoeusConstants.MODIFY_NARRATIVE_RIGHT) > 0 ||
                getUserHasPropRightCount(userId,proposalNumber, CoeusConstants.VIEW_NARRATIVE_RIGHT) > 0 ) {
                    if(proposalNarrativeTxnBean.getUserHasNarrativeModRights(userId, proposalNumber) > 0) {
                        blnFlag = true;
                    }
                }
            }  //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - End
        }
        return blnFlag;
    }

    /**
     * Gets all the person Details for person id and proposal number . The
     * return Vector(Collection) from OSP$EPS_PROP_PERSON  table.
     * <li>To fetch the data, it uses the procedure DW_GET_PROP_PERSON_FOR_ONE.
     *
     * @param proposalNumber is used as an input parameter for the procedure
     * @param personId is used as an input parameter for the procedure
     * @return ProposalPersonFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalPersonFormBean getProposalPersonDetails(String proposalNumber,
            String personId )throws CoeusException, DBException {

        Vector resultPerson = new Vector(3, 2);
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber ));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId ));

        if (dbEngine != null) {

            //Execute the  DB Procedure and Stores the result in Vector.
            resultPerson = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_PERSON_FOR_ONE ( <<PROPOSAL_NUMBER>>, "+
                    " <<PERSON_ID>>, <<OUT RESULTSET rset>>) " , "Coeus", param );
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        int personSize = resultPerson.size();

        if ( personSize < 1) {
            return null;
        }

//        Vector personResultSet = new Vector(3, 2);
        HashMap personRow = null;
        ProposalPersonFormBean proposalPersonFormBean = new ProposalPersonFormBean();
        personRow = (HashMap) resultPerson.elementAt(0);

        proposalPersonFormBean.setProposalNumber( (String)
        personRow.get("PROPOSAL_NUMBER"));
        proposalPersonFormBean.setPersonId( (String)
        personRow.get("PERSON_ID"));
        proposalPersonFormBean.setAWPersonId( (String)
        personRow.get("PERSON_ID"));
        proposalPersonFormBean.setSsn( (String)
        personRow.get("SSN"));
        proposalPersonFormBean.setLastName( (String)
        personRow.get("LAST_NAME"));
        proposalPersonFormBean.setFirstName( (String)
        personRow.get("FIRST_NAME"));
        proposalPersonFormBean.setMiddleName( (String)
        personRow.get("MIDDLE_NAME"));
        proposalPersonFormBean.setFullName( (String)
        personRow.get("FULL_NAME"));
        proposalPersonFormBean.setPriorName( (String)
        personRow.get("PRIOR_NAME"));
        proposalPersonFormBean.setUserName( (String)
        personRow.get("USER_NAME"));
        proposalPersonFormBean.setEmailAddress( (String)
        personRow.get("EMAIL_ADDRESS"));
        proposalPersonFormBean.setDateOfBirth(
                personRow.get("DATE_OF_BIRTH") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "DATE_OF_BIRTH")).getTime()));
        proposalPersonFormBean.setAge(Integer.parseInt(personRow.get("AGE") == null ? "0" :
            personRow.get("AGE").toString()));
        proposalPersonFormBean.setAgeByFiscalYear(
                Integer.parseInt(personRow.get("AGE_BY_FISCAL_YEAR") == null ? "0" :
                    personRow.get("AGE_BY_FISCAL_YEAR").toString()));
        proposalPersonFormBean.setGender( (String)
        personRow.get("GENDER"));
        proposalPersonFormBean.setRace( (String)
        personRow.get("RACE"));
        proposalPersonFormBean.setEduLevel( (String)
        personRow.get("EDUCATION_LEVEL"));
        proposalPersonFormBean.setDegree( (String)
        personRow.get("DEGREE"));
        proposalPersonFormBean.setMajor( (String)
        personRow.get("MAJOR"));
        proposalPersonFormBean.setHandicap(
                personRow.get("IS_HANDICAPPED") == null ? false :
                    (personRow.get("IS_HANDICAPPED").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setHandiCapType( (String)
        personRow.get("HANDICAP_TYPE"));
        proposalPersonFormBean.setVeteran(
                personRow.get("IS_VETERAN") == null ? false :
                    (personRow.get("IS_VETERAN").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setVeteranType( (String)
        personRow.get("VETERAN_TYPE"));
        proposalPersonFormBean.setVisaCode( (String)
        personRow.get("VISA_CODE"));
        proposalPersonFormBean.setVisaType( (String)
        personRow.get("VISA_TYPE"));
        proposalPersonFormBean.setVisaRenDate(
                personRow.get("VISA_RENEWAL_DATE") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "VISA_RENEWAL_DATE")).getTime()));
        proposalPersonFormBean.setHasVisa(
                personRow.get("HAS_VISA") == null ? false :
                    (personRow.get("HAS_VISA").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setOfficeLocation( (String)
        personRow.get("OFFICE_LOCATION"));
        proposalPersonFormBean.setOfficePhone( (String)
        personRow.get("OFFICE_PHONE"));
        proposalPersonFormBean.setSecOfficeLocation( (String)
        personRow.get("SECONDRY_OFFICE_LOCATION"));
        proposalPersonFormBean.setSecOfficePhone( (String)
        personRow.get("SECONDRY_OFFICE_PHONE"));
        proposalPersonFormBean.setSchool( (String)
        personRow.get("SCHOOL"));
        proposalPersonFormBean.setYearGraduated( (String)
        personRow.get("YEAR_GRADUATED"));
        proposalPersonFormBean.setDirDept( (String)
        personRow.get("DIRECTORY_DEPARTMENT"));
        proposalPersonFormBean.setSaltuation( (String)
        personRow.get("SALUTATION"));
        proposalPersonFormBean.setCountryCitizenship( (String)
        personRow.get("COUNTRY_OF_CITIZENSHIP"));
        proposalPersonFormBean.setPrimaryTitle( (String)
        personRow.get("PRIMARY_TITLE"));
        proposalPersonFormBean.setDirTitle( (String)
        personRow.get("DIRECTORY_TITLE"));
        proposalPersonFormBean.setHomeUnit( (String)
        personRow.get("HOME_UNIT"));
        //Get Unit name from Unit Number
        String unitNumber = (String)personRow.get("HOME_UNIT");
        if(unitNumber!=null){
            proposalPersonFormBean.setUnitName(departmentTxnBean.getUnitName(unitNumber));
        }

        proposalPersonFormBean.setFaculty(
                personRow.get("IS_FACULTY") == null ? false :
                    (personRow.get("IS_FACULTY").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setGraduateStudentStaff(
                personRow.get("IS_GRADUATE_STUDENT_STAFF") == null ? false :
                    (personRow.get("IS_GRADUATE_STUDENT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setResearchStaff(
                personRow.get("IS_RESEARCH_STAFF") == null ? false :
                    (personRow.get("IS_RESEARCH_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setServiceStaff(
                personRow.get("IS_SERVICE_STAFF") == null ? false :
                    (personRow.get("IS_SERVICE_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setSupportStaff(
                personRow.get("IS_SUPPORT_STAFF") == null ? false :
                    (personRow.get("IS_SUPPORT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setOtherAcademicGroup(
                personRow.get("IS_OTHER_ACCADEMIC_GROUP") == null ? false :
                    (personRow.get("IS_OTHER_ACCADEMIC_GROUP").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setMedicalStaff(
                personRow.get("IS_MEDICAL_STAFF") == null ? false :
                    (personRow.get("IS_MEDICAL_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setVacationAccural(
                personRow.get("VACATION_ACCURAL") == null ? false :
                    (personRow.get("VACATION_ACCURAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setOnSabbatical(
                personRow.get("IS_ON_SABBATICAL") == null ? false :
                    (personRow.get("IS_ON_SABBATICAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        proposalPersonFormBean.setProvided( (String)
        personRow.get("ID_PROVIDED"));
        proposalPersonFormBean.setVerified( (String)
        personRow.get("ID_VERIFIED"));
        proposalPersonFormBean.setUpdateTimestamp( (Timestamp)
        personRow.get("UPDATE_TIMESTAMP"));
        proposalPersonFormBean.setUpdateUser( (String)
        personRow.get("UPDATE_USER"));

        //Case #1602 Start
        proposalPersonFormBean.setAddress1((String)
        personRow.get("ADDRESS_LINE_1"));
        proposalPersonFormBean.setAddress2((String)
        personRow.get("ADDRESS_LINE_2"));
        proposalPersonFormBean.setAddress3((String)
        personRow.get("ADDRESS_LINE_3"));
        proposalPersonFormBean.setCity((String)
        personRow.get("CITY"));
        proposalPersonFormBean.setCounty((String)
        personRow.get("COUNTY"));
        proposalPersonFormBean.setState((String)
        personRow.get("STATE"));
        proposalPersonFormBean.setPostalCode((String)
        personRow.get("POSTAL_CODE"));
        proposalPersonFormBean.setCountryCode((String)
        personRow.get("COUNTRY_CODE"));
        proposalPersonFormBean.setFaxNumber((String)
        personRow.get("FAX_NUMBER"));
        proposalPersonFormBean.setPagerNumber((String)
        personRow.get("PAGER_NUMBER"));
        proposalPersonFormBean.setMobilePhNumber((String)
        personRow.get("MOBILE_PHONE_NUMBER"));
        proposalPersonFormBean.setEraCommonsUsrName((String)
        personRow.get("ERA_COMMONS_USER_NAME"));
        //Bug fix for Case #2059 by tarique Start 1
        proposalPersonFormBean.setSortId(personRow.get("SORT_ID") == null ?
            null :new Integer( personRow.get("SORT_ID").toString()));
        //Bug fix for Case #2059 by tarique End 1
        //Case #1602 End
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        proposalPersonFormBean.setDivision((String)
        personRow.get("DIVISION"));
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End

        return proposalPersonFormBean;
    }

    /**
     * Gets all User rights for the given proposal number. The
     * return Vector(Collection) from OSP$EPS_PROP_USER_ROLES, OSP$ROLE_RIGHTS & OSP$USER table.
     * <li>To fetch the data, it uses the procedure DW_GET_USERS_FOR_PROP_RIGHT.
     *
     * @param proposalNumber is used an input parameter to the procedure.
     * @return Vector of ProposalNarrativeModuleUsersFormBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getUsersForProposalRight(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean = null;
        HashMap narrativeRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_USERS_FOR_PROP_RIGHT(<<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector narrativeList = null;
        Hashtable hstUserId = new Hashtable();
        if (listSize > 0){
            narrativeList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalNarrativeModuleUsersFormBean = new ProposalNarrativeModuleUsersFormBean();
                narrativeRow = (HashMap)result.elementAt(rowIndex);
                proposalNarrativeModuleUsersFormBean.setProposalNumber(proposalNumber);
                proposalNarrativeModuleUsersFormBean.setUserId( (String)
                narrativeRow.get("USER_ID"));
                String rightId = (String) narrativeRow.get("RIGHT_ID");
                if(rightId == null || rightId.equals("")) {
                    proposalNarrativeModuleUsersFormBean.setAccessType('N');
                } else if(rightId.equals("MODIFY_NARRATIVE")) {
                    proposalNarrativeModuleUsersFormBean.setAccessType('M');
                } else if(rightId.equals("VIEW_NARRATIVE")) {
                    proposalNarrativeModuleUsersFormBean.setAccessType('R');
                }
                proposalNarrativeModuleUsersFormBean.setUserName( (String)
                narrativeRow.get("USER_NAME").toString());

                if(!hstUserId.containsKey(narrativeRow.get("USER_ID"))){
                    hstUserId.put(narrativeRow.get("USER_ID"),proposalNarrativeModuleUsersFormBean);
                    narrativeList.add(proposalNarrativeModuleUsersFormBean);
                }
            }
        }
        return narrativeList;
    }

    /**
     *  This method used get max specail review number for the given Proposal Number
     *  <li>To fetch the data, it uses the function GET_SPECIAL_REVIEW_NUMBER.
     *
     *  @return int review number for the protocol number and sequence number.
     *  @param proposalNumber is given as the input parameter for the procedure.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getNextPropSpecialReviewNumber(String proposalNumber)
    throws CoeusException, DBException {
        int reviewNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));

        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER REVIEWNUMBER>> = "
                    +" call GET_PROP_SPECIAL_REVIEW_NUMBER(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            reviewNumber = Integer.parseInt(rowParameter.get("REVIEWNUMBER").toString());
        }
        return reviewNumber;
    }

    /**
     * Class used to compare two CustomElementsInfoBean
     */
    class BeanComparator implements Comparator{
        public int compare( Object bean1, Object bean2 ){
            if( (bean1 != null ) && ( bean2 != null ) ){
                if( ( bean1 instanceof CustomElementsInfoBean )
                && ( bean2 instanceof CustomElementsInfoBean ) ) {
                    CustomElementsInfoBean firstBean, secondBean;
                    firstBean = ( CustomElementsInfoBean ) bean1;
                    secondBean = ( CustomElementsInfoBean ) bean2;
                    return firstBean.getColumnName().compareToIgnoreCase(
                            secondBean.getColumnName());
                }
            }
            return 0;
        }
    }

    /**
     * Method used to get Proposal Roles from OSP$EPS_PROP_USER_ROLES for the given Proposal Number.
     * <li>To fetch the data, it uses DW_GET_ROLES_FOR_PROPOSAL.
     *
     * @param proposalNumber is used to get list of RoleInfoBean
     * @return vector
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getRolesForProposal(String proposalNumber)throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap roleRow = null;
        RoleInfoBean roleInfoBean = null;

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_ROLES_FOR_PROPOSAL ( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector rolesList = null;
        if(listSize>0){
            rolesList = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                roleInfoBean = new RoleInfoBean();
                roleRow = (HashMap)result.elementAt(rowIndex);
                roleInfoBean.setRoleId(Integer.parseInt(
                        roleRow.get("ROLE_ID") == null ? "0"
                        : roleRow.get("ROLE_ID").toString()));
                roleInfoBean.setRoleName((String)
                roleRow.get("ROLE_NAME"));
                roleInfoBean.setRoleType(
                        roleRow.get("ROLE_TYPE").toString().charAt(0));
                roleInfoBean.setStatus(roleRow.get("STATUS_FLAG").toString().charAt(0));
                rolesList.add(roleInfoBean);
            }
        }
        return rolesList;
    }

    /** This Method is used to get Person Editable Columns from OSP$PERSON_EDITABLE_COLUMNS table.
     * <li>To fetch the data, it uses DW_GET_PERSON_EDITABLE_COLUMNS procedure.
     *
     * @return Vector collections of PersonEditableColumnsFormBean
     * @param proposalNumber is used to get Users for this Proposal
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalUserRoles(String proposalNumber)
    throws CoeusException, DBException{
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        Vector vecRolesBean = new Vector();
        vecRolesBean = getRolesForProposal(proposalNumber);
        Vector roles = new Vector();
        Vector vecUserId = null;
//        Hashtable hstRoleId = null;
        if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
            int outerlength = vecRolesBean.size();
            for(int index1=0;index1<outerlength;index1++){
                RoleInfoBean roleInfoBean =
                        (RoleInfoBean)vecRolesBean.elementAt(index1);
                int roleId = roleInfoBean.getRoleId();
                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
                userRolesInfoBean.setIsRole(true);
                userRolesInfoBean.setRoleBean(roleInfoBean);

                vecUserId = getUsersForProposalRole(proposalNumber,roleId);

                if ((vecUserId != null) && (vecUserId.size() >0)){
                    userRolesInfoBean.setHasChildren(true);
                    int innerlength = vecUserId.size();
                    Vector users = new Vector();
                    ProposalUserRoleFormBean proposalUserRoleFormBean = null;
                    String userId = "";
                    for(int index2=0;index2<innerlength;index2++){
                        proposalUserRoleFormBean = (ProposalUserRoleFormBean) vecUserId.elementAt(index2);
                        userId = proposalUserRoleFormBean.getUserId();

                        UserInfoBean userInfoBean =
                                userDetailsBean.getUserInfo(userId);
                        UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
                        userRolesInfoBean2.setUserBean(userInfoBean);
                        userRolesInfoBean2.setIsRole(false);
                        userRolesInfoBean2.setHasChildren(false);
                        users.addElement(userRolesInfoBean2);
                    }//end inner for loop
                    userRolesInfoBean.setUsers(users);
                }//end of inner  if loop
                roles.addElement(userRolesInfoBean);
            }//end of outer for loop
        }//end of outer if loop
        return roles;
    }

    /**
     *  This method used get individual question count
     *  <li>To fetch the data, it uses the function FN_GET_INDIV_QUEST_COUNT.
     *
     *  @return int count of Questions
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getIndividualQuestionCount()
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();

        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_GET_INDIV_QUEST_COUNT() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }

    /**
     *  This method used populates combo box with Proposal Status item
     *  <li>To fetch the data, it uses the procedure GET_EPS_PROP_STATUS.
     *
     *  @return Vector map of all Proposal Status with Proposal
     *  item types code as key and proposal item type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalStatus() throws CoeusException,DBException{
        Vector result = null;
        Vector vecProposalStatus = null;
        Vector param= new Vector();
        HashMap hasProposalStatus = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            //Start: Enhancement for ProposalStatus: Added new Procedure for getting Proposal Status
            //from newly created table  OSP$EPS_PROPOSAL_STATUS
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROPOSAL_STATUS( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
            //End: Enhancement for ProposalStatus
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            vecProposalStatus = new Vector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hasProposalStatus = (HashMap)result.elementAt(rowIndex);
                vecProposalStatus.addElement(new ComboBoxBean(
                        hasProposalStatus.get(
                        "STATUS_CODE").toString(),
                        hasProposalStatus.get("DESCRIPTION").toString()));
            }
        }
        return vecProposalStatus;
    }

    /**
     *  This method used gets Sponsor Code and Name for the given Dev. Proposal Number
     *  <li>To fetch the data, it uses the procedure GET_SPONSOR_FOR_PROPOSAL.
     *
     *  @return ComboBoxBean containing Sponsor Code and Name
     *  @param proposalNumber String
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ComboBoxBean getSponsorForProposal(String proposalNumber) throws CoeusException,DBException{
        Vector result = null;
        Vector vecSponsor = null;
        Vector param= new Vector();
        HashMap hasSponsor = null;
        ComboBoxBean comboBoxBean = null;
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        if(dbEngine != null){
            result = new Vector(3,2);

            result = dbEngine.executeRequest("Coeus",
                    "call GET_SPONSOR_FOR_PROPOSAL( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
//        int typeCount = result.size();

        if (!result.isEmpty()){
            vecSponsor = new Vector();
            hasSponsor = (HashMap)result.elementAt(0);
            comboBoxBean = new ComboBoxBean();
            comboBoxBean.setCode(hasSponsor.get("SPONSOR_CODE") == null ? "" : hasSponsor.get("SPONSOR_CODE").toString());
            comboBoxBean.setDescription(hasSponsor.get("SPONSOR_NAME") == null ? "" : hasSponsor.get("SPONSOR_NAME").toString());
        }
        return comboBoxBean;
    }


    /**
     * Method used to get Proposal roles right list from OSP$RIGHTS and
     * OSP$ROLE_RIGHTS.
     * <li>To fetch the data, it uses GET_ALL_PROPOSAL_ROLE_RIGHTS procedure.
     *
     * @return Vector map of Roles data is set of roleRightInfoBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAllProposalRoleRights()
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        RoleRightInfoBean roleRightInfoBean = null;
        HashMap rightRoleRow = null;

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ALL_PROPOSAL_ROLE_RIGHTS("+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector rightRoleList = null;
        if (listSize > 0){
            rightRoleList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                roleRightInfoBean = new RoleRightInfoBean();
                rightRoleRow = (HashMap)result.elementAt(rowIndex);
                roleRightInfoBean.setRightId( (String)
                rightRoleRow.get("RIGHT_ID"));
                roleRightInfoBean.setRoleId(
                        Integer.parseInt(rightRoleRow.get("ROLE_ID").toString()));
                roleRightInfoBean.setDescendFlag(
                        rightRoleRow.get("DESCEND_FLAG") == null ? false :
                            (rightRoleRow.get("DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                roleRightInfoBean.setDescription( (String)
                rightRoleRow.get("DESCRIPTION"));
                roleRightInfoBean.setRightType(
                        rightRoleRow.get("RIGHT_TYPE").toString().charAt(0));
                roleRightInfoBean.setRoleDescendFlag(
                        rightRoleRow.get("ROLE_DESCEND_FLAG") == null ? false :
                            (rightRoleRow.get("ROLE_DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                rightRoleList.add(roleRightInfoBean);
            }
        }
        return rightRoleList;
    }

    /* CASE #748 Begin */
    /* FOR USE IN COEUSWEB ONLY.  TO BE REPLACED WHEN AUTHORIZATION API IS IMPLEMENTED FOR VIEW PROPOSAL. */
    public boolean userCanViewProposal(String userId, String proposalNumber)
    throws DBException, CoeusException{
        boolean hasRightToView = false;
        Vector result = null;
        Vector param = new Vector();
        param.addElement(new Parameter("AW_USER_ID", "String", userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER", "String", proposalNumber));
        if(dbEngine != null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER RET>> = call FN_USER_CAN_VIEW_PROPOSAL ( "+
                    "<<AW_USER_ID>> , <<AW_PROPOSAL_NUMBER>>) }",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && !result.isEmpty()){
            HashMap resultSet = (HashMap)result.get(0);
            int returnValue = Integer.parseInt(resultSet.get("RET").toString());
            if (returnValue == 1) {
                hasRightToView = true;
            }
        }
        return hasRightToView;
    }
    /* CASE #748 End */
    /**
     *  Method to fetch the data for proposal printing.
     *  It uses get_sponsor_print_forms stored procedure to get the data.
     *  It returns a hash table of having two vector. One has got all package
     *  information and another has got page data.
     *
     *  Created on 2-Sep-2004 by geo
     */
    public Hashtable getProposalPrintForms(String userId, String proposalNumber, String sponsCode,String primeSponCode)
    throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING, sponsCode));
        param.addElement(new Parameter("PRIME_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING, primeSponCode));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
                    "call get_sponsor_print_forms(<< USER_ID >>,<< PROPOSAL_NUMBER >>,<< SPONSOR_CODE >>,<< PRIME_SPONSOR_CODE >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize=result.size();
        CoeusVector packageList = new CoeusVector();
        CoeusVector pageList = new CoeusVector();
        Hashtable printData = null;
        SponsorTemplateBean propPrintPageFormBean = null;
        SponsorFormsBean propPrintPackageBean = null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
                propPrintPageFormBean = new SponsorTemplateBean();
                propPrintPackageBean = new SponsorFormsBean();
                invRow=(HashMap)result.elementAt(index);

                String sponsorCode = (String)invRow.get("SPONSOR_CODE");
                int packageNumber = Integer.parseInt(invRow.get("PACKAGE_NUMBER") == null ? "1" :
                    invRow.get("PACKAGE_NUMBER").toString());
                String packageName = (String)invRow.get("PACKAGE_NAME");

                propPrintPackageBean.setSponsorCode(sponsorCode);
                propPrintPackageBean.setPackageNumber(packageNumber);
                propPrintPackageBean.setPackageName(packageName);

                if(!packageList.contains(propPrintPackageBean)){
                    packageList.add(propPrintPackageBean);
                }

                int rowId = index+1;
                propPrintPageFormBean.setRowId(rowId);
                propPrintPageFormBean.setSponsorCode(sponsorCode);
                propPrintPageFormBean.setPackageNumber(packageNumber);
                propPrintPageFormBean.setPageNumber(Integer.parseInt(invRow.get("PAGE_NUMBER") == null ? "1" :
                    invRow.get("PAGE_NUMBER").toString()));
                propPrintPageFormBean.setPageDescription((String)invRow.get("PAGE_DESCRIPTION"));
                pageList.add(propPrintPageFormBean);
            }
            printData = new Hashtable();
            //Case 3894 - START
            pageList.sort("packageNumber");
            packageList.sort("packageNumber");
            //Case 3894 - END
            printData.put(KeyConstants.PACKAGE_DATA, packageList);
            printData.put(KeyConstants.PAGE_DATA, pageList);
        }
        return printData;
    }
    //add for printing certification begin
    public int checkAgencyType(String sponsorCode , String primeSponsorCode)
    throws CoeusException, DBException {
        int sponsorTypeCode = -1;
        Vector param= new Vector();
        Vector result = new Vector();
        param.addElement(new Parameter("SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING, sponsorCode));
        param.addElement(new Parameter("PRIME_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING, primeSponsorCode));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER SPONSOR_TYPE>> = "
                    +" call fn_check_fed_type_for_sponsors(<<SPONSOR_CODE>> , <<PRIME_SPONSOR_CODE>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            sponsorTypeCode = Integer.parseInt(rowParameter.get("SPONSOR_TYPE").toString());
        }
        return sponsorTypeCode;
    }
    //start case 2358
    public HashMap getPropLeadUnitOSPAdminHomeUnit(String propNumber)
    throws CoeusException, DBException {

        if(propNumber==null){
            throw new CoeusException("Proposal Number is Null");
        }
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call getPropLUOspAdminHomeUnit( <<PROPOSAL_NUMBER>> , "
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
    //end case 2358
    //case print old certification start
    public Vector getCertifications(String sponsorCode , String primeSponsorCode)
    throws CoeusException,DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        Vector veCertifications = new Vector();
        String questionId;
        HashMap questionRow = null;

        param.addElement(new Parameter("SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING, sponsorCode));
        param.addElement(new Parameter("PRIME_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING, primeSponsorCode));

        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call get_questions_for_sponsor(<<SPONSOR_CODE>> , <<PRIME_SPONSOR_CODE>>,"
                    +" << OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        if (listSize > 0 ){
            for(int index = 0 ; index < listSize; index++ ) {
                questionRow = (HashMap)result.elementAt(index);
                questionId = questionRow.get("QUESTION_ID").toString();
                veCertifications.add(getStatementForQuestion(questionId));
            }

        }

        return veCertifications;
    }

    public String getStatementForQuestion(String questionId)throws CoeusException, DBException {

        Vector result=null;
        Vector param=new Vector();
//        HashMap invRow=null;
        String resp_stmt ="";

        param.addElement(new Parameter("QUESTION_ID",
                DBEngineConstants.TYPE_STRING, questionId));

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING resp_stmt>> = "
                    +" call fn_get_resp_stmt_for_pi_ques(<< QUESTION_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            resp_stmt = rowParameter.get("resp_stmt")== null ? "" : rowParameter.get("resp_stmt").toString().trim();
        }
        return resp_stmt;
    }
    //case print old certification end 1/04/07
    //add for printing certification end
    //Coeus Enhancement Case #1799 start
    /**
     * Method used to get proposal title from OSP$EPS_PROPOSAL
     * for a given proposal number
     * @param proposalId this is given as input parameter for the
     * procedure to execute.
     * @return String proposalTitle
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getProposalTitle(String proposalId)
    throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        String proposalTitle = "";

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalId));

        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROPOSAL_TITLE(<<AV_PROPOSAL_NUMBER>> , "
                    +" << OUT STRING as_title>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if(!result.isEmpty()){
            HashMap hash = (HashMap)result.elementAt(0);
            //case 2414 start
//            proposalTitle = hash.get("as_title").toString();
            if (hash.get("as_title") != null){
                proposalTitle = hash.get("as_title").toString();
            }
            //end case 2414

        }


        return proposalTitle;
    }

    //Coeus Enhancement Case #1799 end


    //Case 2106 Start
    /**
     * Method used to get Investigator credit type
     * <li>To fetch the data, it uses GET_INV_CREDIT_TYPE procedure.
     *
     * @return CoeusVector containing InvCreditTypeBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInvCreditTypes() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InvCreditTypeBean invCreditTypeBean = null;
        HashMap row = null;

        if(dbEngine !=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                        "{ call GET_INV_CREDIT_TYPE("+
                        "<<OUT RESULTSET rset>> ) }", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        CoeusVector cvInvCreditType = null;
        if (listSize > 0){
            cvInvCreditType = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){

                invCreditTypeBean = new InvCreditTypeBean();

                row = (HashMap)result.elementAt(rowIndex);
                invCreditTypeBean.setInvCreditTypeCode(
                        Integer.parseInt(row.get("INV_CREDIT_TYPE_CODE").toString()));

                invCreditTypeBean.setDescription(
                        row.get("DESCRIPTION") == null ? "" : row.get("DESCRIPTION").toString());

                invCreditTypeBean.setAddsToHundred(
                        row.get("ADDS_TO_HUNDRED").toString().equalsIgnoreCase("Y") ? true :false);

                invCreditTypeBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));

                invCreditTypeBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));

                cvInvCreditType.add(invCreditTypeBean);
            }
        }
        return cvInvCreditType;
    }

    /**
     * Method used to get Investigator Credit data
     * <li>To fetch the data, it uses GET_INV_CREDIT_TYPE procedure.
     *
     * @return CoeusVector containing InvCreditTypeBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getPropPerCreditSplit(String proposalNo) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InvestigatorCreditSplitBean invCreditSplitBean = null;
        HashMap row = null;

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNo));

        if(dbEngine !=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                        "{ call GET_EPS_PROP_PER_CREDIT_SPLIT("+
                        "<<AV_PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> ) }", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        CoeusVector cvPropPerCredit = null;
        if (listSize > 0){
            cvPropPerCredit = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){

                invCreditSplitBean = new InvestigatorCreditSplitBean();

                row = (HashMap)result.elementAt(rowIndex);

                invCreditSplitBean.setModuleNumber(
                        row.get("PROPOSAL_NUMBER").toString());

                invCreditSplitBean.setPersonId(
                        row.get("PERSON_ID").toString());
                invCreditSplitBean.setPersonName(row.get("PERSON_NAME") == null ? "" : row.get("PERSON_NAME").toString());
                invCreditSplitBean.setInvCreditTypeCode(
                        Integer.parseInt(row.get("INV_CREDIT_TYPE_CODE").toString()));

                invCreditSplitBean.setCredit(
                        row.get("CREDIT") == null ? new Double(0.0) : new Double(row.get("CREDIT").toString()));

                invCreditSplitBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));

                invCreditSplitBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));

                cvPropPerCredit.add(invCreditSplitBean);
            }
        }
        return cvPropPerCredit;
    }

    /**
     * Method used to get Unit Credit data
     * <li>To fetch the data, it uses GET_EPS_PROP_UNIT_CREDIT_SPLIT procedure.
     *
     * @return CoeusVector containing InvCreditTypeBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getPropUnitCreditSplit(String proposalNo) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InvestigatorCreditSplitBean invCreditSplitBean = null;
        HashMap row = null;

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNo));

        if(dbEngine !=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                        "{ call GET_EPS_PROP_UNIT_CREDIT_SPLIT("+
                        "<<AV_PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> ) }", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        CoeusVector cvPropUnitCredit = null;
        if (listSize > 0){
            cvPropUnitCredit = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){

                invCreditSplitBean = new InvestigatorCreditSplitBean();

                row = (HashMap)result.elementAt(rowIndex);

                invCreditSplitBean.setModuleNumber(
                        row.get("PROPOSAL_NUMBER").toString());

                invCreditSplitBean.setPersonId(
                        row.get("PERSON_ID").toString());

                invCreditSplitBean.setInvCreditTypeCode(
                        Integer.parseInt(row.get("INV_CREDIT_TYPE_CODE").toString()));

                invCreditSplitBean.setUnitNumber(
                        row.get("UNIT_NUMBER").toString());
                invCreditSplitBean.setDescription(
                        row.get("UNIT_NAME") == null ? "": row.get("UNIT_NAME").toString());
                invCreditSplitBean.setCredit(
                        row.get("CREDIT") == null ? new Double(0.0) : new Double(row.get("CREDIT").toString()));

                invCreditSplitBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));

                invCreditSplitBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));

                cvPropUnitCredit.add(invCreditSplitBean);
            }
        }
        return cvPropUnitCredit;
    }
    //Case 2106 End
    public HashMap getProposalHeader(String proposalId)
    throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();
//        String proposalTitle = "";

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalId));

        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROPOSAL_HEADER(<<AV_PROPOSAL_NUMBER>> , "
                    +" << OUT STRING PROPOSAL_TITLE>>,"
                    +" << OUT STRING PI_NAME>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        return result.isEmpty()?null:(HashMap)result.get(0);
    }

    //Coeus Enhancement Case #1799 end

    /**  This method used get PI User Name for the given Proposal number
     *  <li>To fetch the data, it uses the function fn_get_pi_full_name.
     *
     * @return String User ID for the Proposal number
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public String getPIUserName(String proposalNumber)
    throws CoeusException, DBException {
        String userName = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING PI_NAME>> = "
                    +" call fn_get_eps_prop_pi_name(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            Object value = rowParameter.get("PI_NAME");
            if(value!=null){
                userName = value.toString();
            }else{
                userName = null;
            }
        }
        return userName;
    }
    //Added for case 2604 - Organizations and Locations - start

    /**
     * Get all sites for the give proposal  number
     *
     * @param proposalNumber
     * @return CoeusVector
     */
    public CoeusVector getProposalSites(String proposalNumber)
    throws CoeusException, DBException{
        CoeusVector cvProposalSites = new CoeusVector();
        Vector param = new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "  call GET_EPS_PROP_SITES(<<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if(!result.isEmpty()){
            HashMap hmRow = null;
            ProposalSiteBean proposalSiteBean = null;
            RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean =
                    new RolodexMaintenanceDataTxnBean();
            for(int row = 0; row<result.size(); row++){
                hmRow = (HashMap)result.elementAt(row);
                proposalSiteBean = new ProposalSiteBean();
                proposalSiteBean.setProposalNumber((String)hmRow.get("PROPOSAL_NUMBER"));
                proposalSiteBean.setSiteNumber(Integer.parseInt(hmRow.get("SITE_NUMBER").toString()));
                proposalSiteBean.setLocationName((String)hmRow.get("LOCATION_NAME"));
                proposalSiteBean.setLocationTypeName((String)hmRow.get("LOCATION_TYPE_DESC"));
                proposalSiteBean.setLocationTypeCode(Integer.parseInt(hmRow.get("LOCATION_TYPE_CODE").toString()));
                proposalSiteBean.setOrganizationId((String)hmRow.get("ORGANIZATION_ID"));
                proposalSiteBean.setOrganizationId((String)hmRow.get("ORGANIZATION_ID"));
                proposalSiteBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                proposalSiteBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                proposalSiteBean.setCongDistricts(getPropSiteCongDistricts(proposalSiteBean.getProposalNumber(),
                        proposalSiteBean.getSiteNumber()));
                if(hmRow.get("ROLODEX_ID")!=null){
                    RolodexDetailsBean rolodexDetailsBean = rolodexMaintenanceDataTxnBean.
                            getRolodexMaintenanceDetails(hmRow.get("ROLODEX_ID").toString());
                    proposalSiteBean.setRolodexDetailsBean(rolodexDetailsBean);
                }
                cvProposalSites.add(proposalSiteBean);
            }
        }
        return cvProposalSites;
    }

    /**
     * Get all the congressional districts for the given proposalNumber and siteNumber
     *
     * @param proposalNumber
     * @param siteNumber
     * @return CoeusVector
     */
    public CoeusVector getPropSiteCongDistricts(String proposalNumber, int siteNumber)
    throws CoeusException, DBException{
        CoeusVector cvPropSiteCongDistricts = new CoeusVector();
        Vector param = new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("SITE_NUMBER",
                DBEngineConstants.TYPE_INT,Integer.toString(siteNumber)));

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ call GET_PROP_SITE_CONGR_DISTRICT(<< PROPOSAL_NUMBER >>, " +
                    " <<SITE_NUMBER>>, <<OUT RESULTSET rset>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if(!result.isEmpty()){
            HashMap hmRow = null;
            ProposalCongDistrictBean proposalCongDistrictBean = null;
            for(int row = 0; row<result.size(); row++){
                hmRow = (HashMap)result.elementAt(row);
                proposalCongDistrictBean = new ProposalCongDistrictBean();
                proposalCongDistrictBean.setProposalNumber((String)hmRow.get("PROPOSAL_NUMBER"));
                proposalCongDistrictBean.setSiteNumber(Integer.parseInt(hmRow.get("SITE_NUMBER").toString()));
                proposalCongDistrictBean.setCongDistrict((String)hmRow.get("CONG_DISTRICT"));
                proposalCongDistrictBean.setAw_CongDistrict((String)hmRow.get("CONG_DISTRICT"));
                proposalCongDistrictBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                proposalCongDistrictBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                cvPropSiteCongDistricts.add(proposalCongDistrictBean);
            }
        }
        return cvPropSiteCongDistricts;
    }

    /**
     * Get the organization according to the given organization type from the vector
     * cvSites
     *
     * @param type - type of organization(values either Proposal Organization or Performing Organization)
     * @return OrganizationAddressFormBean
     */
    private OrganizationAddressFormBean getSiteAddress(String type, CoeusVector cvSites){
        OrganizationAddressFormBean organizationAddressFormBean = new OrganizationAddressFormBean();
        int proposalOrganizationCode = 1;
        int performingOrganizationCode = 2;
        Equals eqOrganizationType = null;
        if(type.equals("Proposal Organization")){
            eqOrganizationType = new Equals("locationTypeCode", new Integer(proposalOrganizationCode));
        }else if(type.equals("Performing Organization")){
            eqOrganizationType = new Equals("locationTypeCode", new Integer(performingOrganizationCode));
        }
        if(cvSites != null && eqOrganizationType != null){
            CoeusVector cvFilteredSites = cvSites.filter(eqOrganizationType);
            if(cvFilteredSites!=null && cvFilteredSites.size() > 0){
                ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvFilteredSites.get(0);
                organizationAddressFormBean.setOrganizationName(proposalSiteBean.getLocationName());
                RolodexDetailsBean rolodexDetailsBean = proposalSiteBean.getRolodexDetailsBean();
                if(rolodexDetailsBean !=null && rolodexDetailsBean.getRolodexId() != null){
                    organizationAddressFormBean.setContactAddressId(Integer.parseInt(rolodexDetailsBean.getRolodexId()));
                }
            }
        }
        return organizationAddressFormBean;
    }

    /**
     * Get all the location types
     *
     * @return Vector
     */
    public Vector getLocationTypes() throws CoeusException, DBException{
        Vector result = null;
        Vector vecLocationTypes = null;
        Vector param= new Vector();
        HashMap hmRow = null;
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_LOCATION_TYPES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int locationTypeCount = result.size();
        if (locationTypeCount > 0){
            vecLocationTypes = new Vector();
            for(int rowIndex=0; rowIndex<locationTypeCount; rowIndex++){
                hmRow = (HashMap)result.elementAt(rowIndex);
                vecLocationTypes.addElement(new ComboBoxBean(hmRow.get(
                        "LOCATION_TYPE_CODE").toString(), hmRow.get("LOCATION_TYPE_DESC").toString()));
            }
        }
        return vecLocationTypes;
    }

    /**
     * Gets the next siteNumber for the given proposal number
     *
     * @param proposalNumber
     * @return int next siteNumber
     */
    public int getNextSiteNumber(String proposalNumber)throws DBException, CoeusException{
        Vector result = null;
        int siteNumber = -1;
        Vector param= new Vector();
        HashMap hmRow = null;
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine != null){
             result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER SITE_NUMBER>> = "
                    +" call FN_GET_NEXT_SITE_NUMBER(<< PROPOSAL_NUMBER >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int resultSize = result.size();
        if (resultSize > 0){
                hmRow = (HashMap)result.elementAt(0);
                siteNumber = Integer.parseInt(hmRow.get("SITE_NUMBER").toString());
        }
        return siteNumber;
    }
    //Added for case 2406- Organizations and Locations - end
    // 2930: Auto-delete Current Locks based on new parameter - Start

    /**
     * This method is used for Locking a Development Proposal when it is opened
     * in Edit Mode
     *
     * @param String proposalNumber
     * @param String loggedinUser
     * @param String unitNumber
     * @return LockingBean lockingBean
     * @exception Exception
     */
    public LockingBean lockDevProposal(
            String proposalNumber,String loggedinUser, String unitNumber) throws Exception{
        String rowId = rowLockStr+proposalNumber;
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn);
        boolean gotLock = lockingBean.isGotLock();
        if(gotLock){
            try{
                transactionCommit();
                return lockingBean;
            }catch(DBException dbEx){
//                dbEx.printStackTrace();
                UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalDevelopmentTxnBean", "lockDevProposal");
                transactionRollback();
                throw dbEx;
            } finally {
                endConnection();
            }
        } else{
            throw new CoeusException("exceptionCode.999999");
        }
    }
    // 2930: Auto-delete Current Locks based on new parameter - End

//    public static void main(String args[]){
//        try{
//            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
//            Vector vctCustomData = proposalDevelopmentTxnBean.getOthersDetails("00000186");
//            if(vctCustomData!=null){
//                System.out.println("Size : "+vctCustomData.size());
//            }else{
//                System.out.println("Vector is null");
//            }
//        }catch(Exception ex){
//            // commented for using UtilFactory.log instead of printStackTrace
//            UtilFactory.log(ex.getMessage(),ex,"ProposalDevelopmentTxnBean", "main");
////            ex.printStackTrace();
//        }
//    }
    //Added for case 3587: MultiCampus Enhancement
     /**
      *  This method used get the lead unit of the development proposal
      *  <li>To fetch the data, it uses the function FN_GET_LEAD_UNIT_FOR_DEV_PROP.
      *
      *  @return String leadUnit for the proposal number
      *  @param String proposal number to get the lead unit
      *  @exception DBException if any error during database transaction.
      *  @exception CoeusException if the instance of dbEngine is not available.
      */
    public String getProposalLeadUnit(String proposalNumber)
    throws CoeusException, DBException {
        String leadUnit = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING UNIT_NUMBER>> = "
                    +" call FN_GET_LEAD_UNIT_FOR_DEV_PROP(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            leadUnit = rowParameter.get("UNIT_NUMBER").toString();
        }
        return leadUnit;
    }
    //COEUSDEV-75:Rework email engine so the email body is picked up from one place
    /* This function fetches all relevent information for a proposalNumber into a hashmap.
     *Calls get_eps_prop_details_for_mail
     */
    public HashMap getProposalDetailsForMail(String proposalNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmReturn = new HashMap();
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_eps_prop_details_for_mail( << AW_PROPOSAL_NUMBER >> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmReturn = (HashMap)result.elementAt(0);
        }
        return hmReturn;
    }
    //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
    /*
     * Method to get proposal development status code
     * @param proposalNumber
     * @return proposalStatusCode
     */
    public int getProposalStatusCode(String proposalNumber)throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap proposalDevRow = null;
        int proposalStatusCode = 0;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROPOSAL_DETAIL( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            proposalDevRow = (HashMap)result.elementAt(0);
            proposalStatusCode = Integer.parseInt(proposalDevRow.get("CREATION_STATUS_CODE").toString());
        }
        return proposalStatusCode;
    }
    //COEUSQA-1579 : End
    /*Added for Case# COEUSQA-1675-ability to delete Proposal Development proposals - Start*/
        /**
     * To check if the Proposal can be deleted for the given Proposal number
     * Function FN_CAN_DELETE_Proposal is used to check if Proposal can be deleted
     * @param String ProposalNumber
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return int canDelete
     * canDelete = 0 - Proposal is in one of the 3 required status and is not linked to any module
     * canDelete = 2 - Proposal not in either 'Pending in Progress'
     */
    public int checkCanDeleteProposal(String proposalNumber)  throws CoeusException, DBException {
        int canDelete = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ll_Ret>> = "
                    +" call FN_CAN_DELETE_PROPOSAL(<< PROPOSAL_NUMBER >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            canDelete = Integer.parseInt(rowParameter.get("ll_Ret").toString());
        }
        return canDelete;
    }

//Added for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_Start
    /*To check if the Proposal is in Hierarchy
     * @param String ProposalNumber
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return String isPropInHierarchy
     */
    public String checkIsProposalInHierarchy(String proposalNumber)  throws CoeusException, DBException {
        String isPropInHierarchy = "";
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROP_HEADER_DETAILS(<< AV_PROPOSAL_NUMBER >>,<<OUT RESULTSET rset>>)",
                    "Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isPropInHierarchy = rowParameter.get("IS_PROP_IN_HIERARCHY").toString();
        }
        return isPropInHierarchy;
    }
//Added for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_End
    /*Added for Case# COEUSQA-1675-ability to delete Proposal Development proposals - END*/

    // Added for COEUSQA-2778-Submited Prop to Sponsor-Narrative Incomplete - Start
        /*
         * Method to get proposal development narrative status
         * @param proposalNumber
         * @return proposalStatusCode
         */
    public String getNarrativeStatus(String proposalNumber)throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap proposalDevRow = null;
        String proposalNarrativeStatus = "";
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROPOSAL_DETAIL( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            proposalDevRow = (HashMap)result.elementAt(0);
            proposalNarrativeStatus = proposalDevRow.get("NARRATIVE_STATUS").toString();
        }
        return proposalNarrativeStatus;
    }



     //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
    /*
     * Method to get proposal development status code
     * @param proposalNumber
     * @return proposalStatusCode
     */
    public String getEmailIdForPerson(String proposalNumber,String personId)throws CoeusException, DBException{
        String emailId=null;
        Vector result = null;
        Vector param= new Vector();
        HashMap proposalDevRow = null;
        int proposalStatusCode = 0;

        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           // result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus", " call DW_GET_EMAIL_ID( <<PERSON_ID>>, " +
                    " <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>>) ", "Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            proposalDevRow = (HashMap)result.elementAt(0);
            emailId=(String) proposalDevRow.get("EMAIL_ADDRESS");
            //proposalStatusCode = Integer.parseInt(proposalDevRow.get("CREATION_STATUS_CODE").toString());
        }
        return emailId;
    }

    public void updateLastNotificationDate(String personId,String proposalNumber)throws CoeusException, DBException{

        Vector param= new Vector();
        Vector result = new Vector();
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NUMBER>> = "
                    +" call FN_UPD_LAST_NOTIFIC_DATE( <<PERSON_ID>>, << PROPOSAL_NUMBER >> ) }", param);
         }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }

    
    public HashMap getPropPersonDetailsForMail(String proposalNumber)throws CoeusException, DBException{
        Vector result = null;
        HashMap hmRow = null;
        Vector param= new Vector();
               param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
              
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_PROP_DETAILS_SEND_NOTIF( <<PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && result.size()>0){
            hmRow=(HashMap)result.get(0);
        }
            return hmRow;
    }


public Vector getPropPersonDetailsForNotif(String proposalNumber)throws CoeusException, DBException{
    Vector result = null;
    Vector param= new Vector();
               param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
              
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_PROP_PERSONS_SEND_NOTIF( <<PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       return result;
}




     public Vector getNotificationPersonsDetails(String proposalNumber)throws CoeusException, DBException{
        Vector lproposalPersonsVector= new Vector();
        Vector result = null;
        Vector param= new Vector();
        Vector personid=new Vector();
        HashMap hmRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            //result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SEND_NOTIFICATION( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null){
        int proposalPersonsCertifyCount = result.size();
        if(proposalPersonsCertifyCount>0){
            lproposalPersonsVector=new Vector();
            for(int rowIndex=0;rowIndex<proposalPersonsCertifyCount;rowIndex++){
                hmRow = (HashMap)result.elementAt(rowIndex);
                Vector data= new Vector();
                data.add( false );
                data.add((String)hmRow.get("FULL_NAME"));
                data.add((Timestamp)hmRow.get("LAST_NOTIFICATION_DATE"));
                personid.add((String)hmRow.get("PERSON_ID"));
                lproposalPersonsVector.add(data);

            }
            lproposalPersonsVector.add(personid);
        }
        }

     return lproposalPersonsVector;
    }
//code added for getting proposal disclosure details to displaying DisclosureStatusForm - starts
 public Vector getDisclosureStatusDetails(String proposalNumber)throws CoeusException, DBException{
        Vector proposalPersonsVector= new Vector();
        Vector result = null;
        Vector param= new Vector();
        HashMap hmRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null)
        {
                    result = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_DISCLOSRE_STATUS( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else
        {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int proposalPersonsCount = result.size();
        if(proposalPersonsCount>0)
        {
            proposalPersonsVector=new Vector();
            for(int rowIndex=0;rowIndex<proposalPersonsCount;rowIndex++)
            {
                hmRow = (HashMap)result.elementAt(rowIndex);
                Vector data= new Vector();
                data.add((String)hmRow.get("FULL_NAME"));
//                data.add((String)hmRow.get("UNIT_NAME"));
                data.add((String)hmRow.get("ROLE"));
                if(hmRow.get("DESCRIPTION") !=null)
                {
                    data.add((String)hmRow.get("DESCRIPTION"));
                }
                else
                {
                    data.add("Not Disclosed");
                }
                data.add((String)hmRow.get("TITLE"));
                proposalPersonsVector.add(data);

            }
        }
     return proposalPersonsVector;
    }
 //code added for getting proposal disclosure details to displaying DisclosureStatusForm - ends
 public boolean getMaintanenceCertification(String loggedinUser,String proposalNumber)throws CoeusException, DBException
{
boolean hasRight=false;
boolean hasRight1=false;
Vector param= new Vector();
Vector result = null;
Vector  maintanenceCertificationCountVector=null;
        HashMap hmRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
         param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,loggedinUser));
        if(dbEngine !=null){
            //result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call dw_get_prop_rights_for_user( <<PROPOSAL_NUMBER>> ,<<USER_ID>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int maintanenceCertificationCount = result.size();
        if(maintanenceCertificationCount>0){
            maintanenceCertificationCountVector=new Vector();
            for(int rowIndex=0;rowIndex<maintanenceCertificationCount;rowIndex++){
                hmRow = (HashMap)result.elementAt(rowIndex);
                maintanenceCertificationCountVector.add((String)hmRow.get("RIGHT_ID"));
              }

        }
if(maintanenceCertificationCountVector!=null && maintanenceCertificationCountVector.contains("MAINTAIN_PERSON_CERTIFICATION"))
{
    hasRight=true;
}
else if (maintanenceCertificationCountVector!=null && maintanenceCertificationCountVector.contains("NOTIFY_PROPOSAL_PERSONS"))
{
     hasRight1=true;
}
else if (maintanenceCertificationCountVector!=null && maintanenceCertificationCountVector.contains("MAINTAIN_DEPT_PERSONNEL_CERTIFICATION "))
{
     hasRight=true;
}
     return hasRight;
}

//checking parameter
public boolean getParameterCertification(String parameterName)throws CoeusException, DBException
{
boolean hasRight=false;
Vector param= new Vector();
int count=0;

        HashMap hmRow = null;
        Vector result=null;
         param.addElement(new Parameter("as_parameter",
                DBEngineConstants.TYPE_STRING,parameterName));
        if(dbEngine !=null){
            //result = new Vector(3,2);
           // result = dbEngine.executeFunctions("Coeus","{<<OUT INTEGER COUNT>> = call FN_GET_UPDATEUSER_FOR_PPC(<<AV_MODULE_ITEM_KEY>>)", "Coeus", param);

            result=dbEngine.executeFunctions("Coeus","{<<OUT STRING COUNT>> = call get_parameter_value(<<as_parameter>>)}",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         if(!result.isEmpty())
         {
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
         }


if(count == 1)
{
   hasRight=true;
}
 else{
   hasRight=false;
}
   return hasRight;
}

public boolean getMaintanenceNotification(String loggedinUser,String proposalNumber)throws CoeusException, DBException
{
boolean hasRight=false;
boolean hasNotificRight=false;
Vector param= new Vector();
Vector result = null;
Vector  maintanenceCertificationCountVector=null;
        HashMap hmRow = null;

         param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
         param.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING,loggedinUser));
        if(dbEngine !=null){
            //result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_RIGHTS_FOR_USER(<<AW_PROPOSAL_NUMBER>>,<<AW_USER_ID>>, "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int maintanenceCertificationCount = result.size();
        if(maintanenceCertificationCount>0){
            maintanenceCertificationCountVector=new Vector();
            for(int rowIndex=0;rowIndex<maintanenceCertificationCount;rowIndex++){
                hmRow = (HashMap)result.elementAt(rowIndex);
                maintanenceCertificationCountVector.add((String)hmRow.get("RIGHT_ID"));
              }

if(maintanenceCertificationCountVector!=null &&   maintanenceCertificationCountVector.contains("NOTIFY_PROPOSAL_PERSONS"))
{
   hasNotificRight=true;
}


        }
 return hasNotificRight;
}
public boolean getViewCertifications(String loggedinUser,String proposalNumber)throws CoeusException, DBException {
    boolean hasRight=false;
    boolean hasViewCertRight=false;
    Vector param= new Vector();
    Vector result = null;
    Vector  maintanenceCertificationCountVector=null;
    HashMap hmRow = null;

    param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
    param.addElement(new Parameter("AW_USER_ID",
            DBEngineConstants.TYPE_STRING,loggedinUser));
    if(dbEngine !=null){
        //result = new Vector(3,2);
        result = dbEngine.executeRequest("Coeus",
                "call DW_GET_PROP_RIGHTS_FOR_USER(<<AW_PROPOSAL_NUMBER>>,<<AW_USER_ID>>, "+
                "<<OUT RESULTSET rset>> )", "Coeus", param);
    }else{
        throw new CoeusException("db_exceptionCode.1000");
    }
    int maintanenceCertificationCount = result.size();
    if(maintanenceCertificationCount>0){
        maintanenceCertificationCountVector=new Vector();
        for(int rowIndex=0;rowIndex<maintanenceCertificationCount;rowIndex++){
            hmRow = (HashMap)result.elementAt(rowIndex);
            maintanenceCertificationCountVector.add((String)hmRow.get("RIGHT_ID"));
        }
        if(maintanenceCertificationCountVector!=null &&   maintanenceCertificationCountVector.contains("VIEW_DEPT_PERSNL_CERTIFN")) {
        hasViewCertRight=true;
    }
    }

    return hasViewCertRight;
}

  //COEUSQA-2984 : Statuses in special review - start
  /*
   * This Method gets the  protocol(Special Review) status of the proposal
   * it uses procedure GET_PROP_SPREV_PROTO_STATUS to get the status code
   * and status description for the particular protocol
   *
   * @param proposalNumber
   * @param protocolNumber
   * @param specialReviewNumber
   * returns specialReviewFormBean
   */
public SpecialReviewFormBean getPropProtocolStatus(String proposalNumber,String protocolNumber,int specialReviewNumber)
throws CoeusException, DBException {

    Vector result = new Vector(3,2);
    Vector param= new Vector();
    HashMap infoProtocolRow = null;
    SpecialReviewFormBean specialReviewFormBean = null;
    param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
    param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING,protocolNumber));
    param.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
            DBEngineConstants.TYPE_INT,specialReviewNumber));
    if(dbEngine!=null){
        result = dbEngine.executeRequest("Coeus",
                "call GET_PROP_SPREV_PROTO_STATUS(" +
                "<< AW_PROPOSAL_NUMBER >> , << AW_PROTOCOL_NUMBER >> , << AW_SPECIAL_REVIEW_NUMBER >>,<< OUT RESULTSET rset >>)",
                "Coeus", param);

    }else{
        throw new DBException("DB instance is not available");
    }
    if(!result.isEmpty()){
        specialReviewFormBean = new SpecialReviewFormBean();
        infoProtocolRow = (HashMap)result.elementAt(0);
        specialReviewFormBean.setProtocolStatusCode(Integer.parseInt(
                infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString()));
        specialReviewFormBean.setProtocolStatusDescription((String)
        infoProtocolRow.get("PROTOCOL_STATUS_DESCRIPTION"));
    }

    return specialReviewFormBean;
}
//COEUSQA-2984 : Statuses in special review - end

//Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
/*
   * This Method gets the  IACUC protocol(Special Review) status of the proposal
   * it uses procedure GET_PROP_SPRV_AC_PROTO_STATUS to get the status code
   * and status description for the particular protocol
   *
   * @param proposalNumber
   * @param protocolNumber
   * @param specialReviewNumber
   * returns specialReviewFormBean
   */
public SpecialReviewFormBean getPropIacucProtocolStatus(String proposalNumber,String protocolNumber,int specialReviewNumber)
throws CoeusException, DBException {

    Vector result = new Vector(3,2);
    Vector param= new Vector();
    HashMap infoProtocolRow = null;
    SpecialReviewFormBean specialReviewFormBean = null;
    param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
    param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING,protocolNumber));
    param.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
            DBEngineConstants.TYPE_INT,specialReviewNumber));
    if(dbEngine!=null){
        result = dbEngine.executeRequest("Coeus",
                "call GET_PROP_SPRV_AC_PROTO_STATUS(" +
                "<< AW_PROPOSAL_NUMBER >> , << AW_PROTOCOL_NUMBER >> , << AW_SPECIAL_REVIEW_NUMBER >>,<< OUT RESULTSET rset >>)",
                "Coeus", param);

    }else{
        throw new DBException("DB instance is not available");
    }
    if(!result.isEmpty()){
        specialReviewFormBean = new SpecialReviewFormBean();
        infoProtocolRow = (HashMap)result.elementAt(0);
        specialReviewFormBean.setProtocolStatusCode(Integer.parseInt(
                infoProtocolRow.get("PROTOCOL_STATUS_CODE").toString()));
        specialReviewFormBean.setProtocolStatusDescription((String)
        infoProtocolRow.get("PROTOCOL_STATUS_DESCRIPTION"));
    }

    return specialReviewFormBean;
}
//Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end

 public LockingBean lockProposalRouting(
            String proposalNumber,String loggedinUser, String unitNumber) throws Exception{
        String rowId = routingLockStr+proposalNumber;
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn);
        boolean gotLock = lockingBean.isGotLock();
        if(gotLock){
            try{
                transactionCommit();
                return lockingBean;
            }catch(DBException dbEx){
//                dbEx.printStackTrace();
                UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalDevelopmentTxnBean", "lockDevProposal");
                transactionRollback();
                throw dbEx;
            } finally {
                endConnection();
            }
        } else{
            throw new CoeusException("exceptionCode.999999");
        }
    }

 //QUESTIONNAIRE SEGMENT MODIFICATION STARTS


 public Vector getProposalQuestionnaire(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalYNQBean proposalYNQBean = null;
        HashMap yNQRow = null;
        param.addElement(new Parameter("av_module_item_key",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_QUESTIONNAIRE_ANS_GRANTS(<<av_module_item_key>>,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector ynqList = null;
        if (listSize > 0){
            ynqList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalYNQBean = new ProposalYNQBean();
                yNQRow = (HashMap)result.elementAt(rowIndex);

                proposalYNQBean.setProposalNumber( proposalNumber);
                proposalYNQBean.setQuestionId( (yNQRow.get("QUESTION_ID")).toString());
                proposalYNQBean.setAnswer(  (yNQRow.get("ANSWER")).toString() );
                proposalYNQBean.setExplanation(((yNQRow.get("EXPLANATION"))==null)?null:yNQRow.get("EXPLANATION").toString());
                proposalYNQBean.setReviewDate( yNQRow.get("ANSWER") == null ? null : yNQRow.get("ANSWER").toString());
                proposalYNQBean.setUpdateTimeStamp((Timestamp)yNQRow.get("UPDATE_TIMESTAMP"));
                proposalYNQBean.setUpdateUser( (yNQRow.get("UPDATE_USER")).toString());
                ynqList.add(proposalYNQBean);
            }
        }
        return ynqList;
    }
 //QUESTIONNAIRE SEGMENT MODIFICATION ENDS

 public ComboBoxBean getProposalStatusDetails(String proposalNumber)throws CoeusException, DBException{
     Vector result = null;
     Vector param= new Vector();
     HashMap proposalDevRow = null;
     ComboBoxBean comboBoxBean = null;
     param.addElement(new Parameter("PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,proposalNumber));
     if(dbEngine !=null){
         result = new Vector(3,2);
         result = dbEngine.executeRequest("Coeus",
                 "call GET_EPS_PROPOSAL_DETAIL( <<PROPOSAL_NUMBER>> , "+
                 "<<OUT RESULTSET rset>> )", "Coeus", param);
     }else{
         throw new CoeusException("db_exceptionCode.1000");
     }
     int listSize = result.size();
     if (listSize > 0){
         proposalDevRow = (HashMap)result.elementAt(0);
          comboBoxBean = new ComboBoxBean(
                        proposalDevRow.get("CREATION_STATUS_CODE").toString(),
                        proposalDevRow.get("CREATION_STATUS_DESC").toString());
     }
     return comboBoxBean;
 }
 //COEUSQA-3951 
   public boolean checkS2SSubTyp(String proposalNumber) throws CoeusException, DBException {        
        boolean retValue = true ; 
        int value = 0;
        Vector param= new Vector();
        Vector result = new Vector();        
        param.add(new Parameter("AS_PROPOSAL",DBEngineConstants.TYPE_STRING,proposalNumber));  
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER RESULTS>> = call FN_CHECK_PROP_S2S_SUB_TYP(<< AS_PROPOSAL >>"+
                    " ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowPersonExist = (HashMap)result.elementAt(0);
            value =  Integer.parseInt(rowPersonExist.get("RESULTS").toString());
            if(value == 0){
                retValue = false;
            }
        }
        
        return retValue;
    } 
 //COEUSQA-3951   
//COEUSQA-3964 
    public boolean IsUserPI(String proposalNumber, String userId, int moduleCode) throws CoeusException, DBException {
        boolean retValue = false;
        int value = 0;
        Vector param = new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AV_MODULE_KEY", DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("AV_USER_ID", DBEngineConstants.TYPE_STRING, userId));
        param.add(new Parameter("AV_MODULE_CODE", DBEngineConstants.TYPE_INT, moduleCode));
        /* calling stored function */
        try {
            if (dbEngine != null) {
                result = dbEngine.executeFunctions("Coeus",
                        "{ <<OUT INTEGER RESULTS>> = call FN_USER_IS_PI(<< AV_MODULE_KEY >>,<< AV_USER_ID >>,<< AV_MODULE_CODE >>"
                        + " ) }", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
            if (!result.isEmpty()) {
                HashMap rowPersonExist = (HashMap) result.elementAt(0);
                value = Integer.parseInt(rowPersonExist.get("RESULTS").toString());
                if (value == 1) {
                    retValue = true;
                }
            }
        } catch (Exception e) {
            retValue = false;
        }


        return retValue;
    }
 //COEUSQA-3964      
 //COEUSQA-3956 START
      public boolean ISNeedToShowYNQWhenPPCEnabled(String proposalNumber) throws CoeusException, DBException {
        boolean retValue = false;
        int value = 0;
        Vector param = new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
      
        /* calling stored function */
        try {
            if (dbEngine != null) {
                result = dbEngine.executeFunctions("Coeus",
                        "{ <<OUT INTEGER RESULTS>> = call FN_EPS_SHOW_YNQ_FOR_PPC(<< AS_PROPOSAL_NUMBER >>"
                        + " ) }", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
            if (!result.isEmpty()) {
                HashMap rowPersonExist = (HashMap) result.elementAt(0);
                value = Integer.parseInt(rowPersonExist.get("RESULTS").toString());
                if (value == 1) {
                    retValue = true;
                }
            }
        } catch (Exception e) {
            retValue = false;
        }


        return retValue;
    }      
 //COEUSQA-3956 ENDS   
   public boolean isPHSHumanSubjectCTFormIncluded(String proposalNumber)throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap proposalDevRow = null;
        boolean canInclude = false;
        int retValue = 0;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
       try {
           if (dbEngine != null) {
               result = dbEngine.executeFunctions("Coeus",
                       "{ <<OUT INTEGER RESULTS>> = call FN_IS_PHS_HS_CT_FORM_INCLUDED(<< PROPOSAL_NUMBER >>"
                       + " ) }", param);
           } else {
               throw new CoeusException("db_exceptionCode.1000");
           }
           if (!result.isEmpty()) {
               HashMap rowPersonExist = (HashMap) result.elementAt(0);
               retValue = Integer.parseInt(rowPersonExist.get("RESULTS").toString());
               if (retValue == 1) {
                   canInclude = true;
               }
           }
       } catch (Exception e) {
           canInclude = false;
       }       
     
       return canInclude;
    }   
}//end of class