/*
 * @(#)ProtocolUpdateTxnBean.java 1.0 10/25/02 2:20 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 29-NOV-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.SequenceLogic;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.NotepadTxnBean;

//Coeus Enhancement Case #1799 step 1 start
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.instprop.bean.InstituteProposalUpdateTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalSpecialReviewBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.award.bean.AwardUpdateTxnBean;
import edu.mit.coeus.award.bean.AwardSpecialReviewBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.bean.ProposalActionUpdateTxnBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.sql.Blob;
//Coeus Enhancement Case #1799 step 1 end

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
//import java.sql.SQLException;

/**
 * This class provides the methods for performing modify/insert and delete
 * procedure executions for a Protocol functionality. Various methods are used
 * to modify/insert the data for "ProtocolDetailsForm" from the Database.
 * All methods are used <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 October 25, 2002, 2:20 PM
 * @author  Mukundan C
 */

public class ProtocolUpdateTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";

    // holds the userId for the logged in user
    private String userId;

    private TransactionMonitor transMon;


    private static final String rowLockStr = "osp$Protocol_";

 private static final String routingLockStr = "osp$Protocol Routing_";
    private Timestamp dbTimestamp;

    private final char SAVE_NEW_AMENDMENT = 'n';

    private final char SAVE_NEW_REVISION = 'r';

    private char functionType;

    // holds the unit number for the logged in user
    private String unitNumber;
    //Coeus Enhancement Case #1799 step 2 start
    private Vector lockedBeans = null;

    private CoeusVector notepadBeans = null;
    //To store already locked modules
    private String moduleName = "";

    //Coeus Enhancement Case #1799 step 2 end

    //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-Start
    boolean isIndicatorUpdated = false;
    //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-End

   //COEUSQA 1457 STARTS
  private Vector removedPersonIDs=new Vector();
  private Vector removedPersonRoles=new Vector();
  private static final int PERSON_REMOVAL_MAIL_ACTION_ID=900; 
   //COEUSQA 1457 ENDS  
    /** Creates a new instance of ProtocolUpdateTxnBean */
    public ProtocolUpdateTxnBean() {

    }

    /**
     * Creates new ProtocolUpdateTxnBean and initializes PersonID.
     * @param userId String which the Loggedin userid
     */

    public ProtocolUpdateTxnBean(String userId) throws DBException {
        this.userId = userId;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    /**
     * Creates new ProtocolUpdateTxnBean and initializes PersonID.
     * @param userId String which the Loggedin userid
     */
    //Coeus Enhancement Case #1799 step 3 start
    public ProtocolUpdateTxnBean(String userId,String unitNumber) {
        this.userId = userId;
        this.unitNumber = unitNumber;
        //Coeus Enhancement Case #1799 step 3 end
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    //Coeus Enhancement Case #1799 step 3 end
    /**
     * This method creates next Protocol number for the protocol detail form.
     * <li>To fetch the data, it uses the function fn_generate_protocol_number.
     *
     * @return String  protocolNumber as key for protocol detail form.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public String getNextProtocolNumber() throws  DBException{
        String protocolNumber = null;
        Vector param= new Vector();
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER SEQNUMBER>> = call fn_generate_protocol_number()}",
                    param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            protocolNumber = nextNumRow.get("SEQNUMBER").toString();
        }
        return protocolNumber;
    }

    /**
     * Overridden method for implementing rowlocking by using transaction monitor.
     *
     * @param protocolBean this bean contains data for
     * insert/modify for protocol Info.
     * @param functionType to check for modify before retreive
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProtocolInfo(
            ProtocolInfoBean protocolBean,
            char functionType) throws CoeusException,DBException{
        this.functionType = functionType;
        boolean success=addUpdProtocolInfo(protocolBean);
        String rowId = rowLockStr;
        /*if((success==true)&&(functionType=='U'))
        {
              String rowId
                = rowLockStr + protocolBean.getProtocolNumber();
              transMon.releaseEdit(rowId);
              return true;
          }*/
        if( success ) {
            String protocolNo = protocolBean.getProtocolNumber();
            /* as the protocol is locked for amend/ revision with protocol no +
             * 'A' or 'R' respectively when request came to create a new amendment
             * so after saving we have to release the lock with the same row lock
             * string. But in modify mode of amend/ revision as we are locking with
             * the entire protocol number like 0000001828A002 this checking is not
             * required.
             */
            if( (functionType == SAVE_NEW_AMENDMENT  ) ||
                    ( functionType == SAVE_NEW_REVISION ) ){
                if( protocolNo.length() > 10 ) {
                    rowId +=protocolNo.substring(0,11);
                }

            }else if( functionType == 'U' ) {
                rowId+= protocolNo;
            }
            transMon.releaseEdit( rowId );
        }
        return success;

    }

    // Code added by Shivakumar for locking enhancement - BEGIN

    public boolean addUpdProtocolInfo(
            ProtocolInfoBean protocolBean,
            char functionType,String userId) throws CoeusException,DBException{
        this.functionType = functionType;
        boolean success=addUpdProtocolInfo(protocolBean);
        String rowId = rowLockStr;
        /*if((success==true)&&(functionType=='U'))
        {
              String rowId
                = rowLockStr + protocolBean.getProtocolNumber();
              transMon.releaseEdit(rowId);
              return true;
          }*/
        if( success ) {
            String protocolNo = protocolBean.getProtocolNumber();
            /* as the protocol is locked for amend/ revision with protocol no +
             * 'A' or 'R' respectively when request came to create a new amendment
             * so after saving we have to release the lock with the same row lock
             * string. But in modify mode of amend/ revision as we are locking with
             * the entire protocol number like 0000001828A002 this checking is not
             * required.
             */
            if( (functionType == SAVE_NEW_AMENDMENT  ) ||
                    ( functionType == SAVE_NEW_REVISION ) ){
                if( protocolNo.length() > 10 ) {
                    rowId +=protocolNo.substring(0,11);
                }

            }else if( functionType == 'U' ) {
                rowId+= protocolNo;
            }
            //            transMon.releaseEdit(rowId,userId);
            // Calling releaseLock method for bug fixing
            boolean lockCheck = transMon.lockAvailabilityCheck(rowId,userId);
            if(!lockCheck){
                LockingBean lockingBean = transMon.releaseLock(rowId,userId);
            }
        }
        return success;
    }
    public Vector getUserRoles()throws CoeusException, DBException{
        String unitNumber=null;
        boolean success=false;
        Vector unitDetails=new Vector();
        Vector result = new Vector(3,2);
         HashMap hasUnitnum=new HashMap();
        Vector param= new Vector();
          param.add(new Parameter("userId",
                DBEngineConstants.TYPE_STRING,userId));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_USER_UNIT_NUMBER (<<userId>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
            success=true;
        }else{
            throw new DBException("DB instance is not available");
        }
          int typesCount = result.size();
        if (typesCount >0){
              for(int types=0;types<typesCount;types++){
                hasUnitnum = (HashMap)result.elementAt(types);
                unitDetails.addElement(hasUnitnum.get("UNIT_NUMBER"));
              }
        }
        unitDetails.add(success);

        return unitDetails;

    }
    // Code added by Shivakumar for locking enhancement - END
    /**
     * Method used to update/insert/delete all the details of a Protocol Details
     * related ProtocolKeyStatus,VulnerableSubject,Locations,Correspondent,
     * Investigators,AreaofResearch and FundSources.
     * <li>To fetch the data, it uses upd_protocol procedure.
     *
     * @param protocolInfoBean this bean contains data for
     * insert/modify for protocol Info.
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector saveProposalDetails(ProtocolInfoBean protocolInfoBean,String unitNumber)throws CoeusException, DBException{
        Vector retVector=saveProposal(protocolInfoBean,unitNumber);
        return retVector;
    }
        public Vector saveProposal(ProtocolInfoBean protocolInfoBean,String unitNumber) throws CoeusException, DBException{
        boolean success=false;
        Vector returnVector=new Vector();
         CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
       String title= protocolInfoBean.getTitle();
       String createUser=protocolInfoBean.getCreateUser();
       String proposalNumber=getNextProposalNumber();
       Vector propdetail=new Vector();
       propdetail.add(proposalNumber);
       returnVector.add(proposalNumber);
     //  protocolInfoBean.getInvestigators().
         Vector procedures = new Vector(5,3);
         Vector paramProposal= new Vector();
        paramProposal.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        paramProposal.addElement(new Parameter("PROPOSAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+"1"));
        paramProposal.addElement(new Parameter("STATUS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+"1"));
        paramProposal.addElement(new Parameter("CREATION_STATUS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+"1"));
        paramProposal.addElement(new Parameter("BASE_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("CONTINUED_FROM",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("TEMPLATE_FLAG",
                DBEngineConstants.TYPE_STRING,
                "N") );
        paramProposal.addElement(new Parameter("CURRENT_ACCOUNT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("CURRENT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("TITLE",
                DBEngineConstants.TYPE_STRING,
                title));
        paramProposal.addElement(new Parameter("CFDA_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("PRIME_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("SPONSOR_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("INTR_COOP_ACTIVITIES_FLAG",
                DBEngineConstants.TYPE_STRING,
                null) );
        paramProposal.addElement(new Parameter("INTR_COUNTRY_LIST",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("OTHER_AGENCY_FLAG",
                DBEngineConstants.TYPE_STRING,
                null) );
        paramProposal.addElement(new Parameter("NOTICE_OF_OPPORTUNITY_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("PROGRAM_ANNOUNCEMENT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("PROGRAM_ANNOUNCEMENT_TITLE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("ACTIVITY_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                "1"));
        paramProposal.addElement(new Parameter("REQUESTED_START_DATE_INIT",
                DBEngineConstants.TYPE_DATE,
                protocolInfoBean.getApplicationDate()));
        paramProposal.addElement(new Parameter("REQUESTED_START_DATE_TOTAL",
                DBEngineConstants.TYPE_DATE,
                null));
        paramProposal.addElement(new Parameter("REQUESTED_END_DATE_INIT",
                DBEngineConstants.TYPE_DATE,
                null));
        paramProposal.addElement(new Parameter("REQUESTED_END_DATE_TOTAL",
                DBEngineConstants.TYPE_DATE,
                null));
        paramProposal.addElement(new Parameter("DURATION_MONTHS",
                DBEngineConstants.TYPE_INT,
                null));
        paramProposal.addElement(new Parameter("NUMBER_OF_COPIES",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("DEADLINE_DATE",
                DBEngineConstants.TYPE_DATE,
                null));
        paramProposal.addElement(new Parameter("DEADLINE_TYPE",
                DBEngineConstants.TYPE_STRING,
                null));


            paramProposal.addElement(new Parameter("MAILING_ADDRESS_ID",
                    DBEngineConstants.TYPE_INT,
                    null));

            paramProposal.addElement(new Parameter("MAILING_ADDRESS_ID",
                    DBEngineConstants.TYPE_STRING, null));


        paramProposal.addElement(new Parameter("MAIL_BY",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("MAIL_TYPE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("CARRIER_CODE_TYPE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("CARRIER_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("MAIL_DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("MAIL_ACCOUNT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("SUBCONTRACT_FLAG",
                DBEngineConstants.TYPE_STRING,
                "N"));
        paramProposal.addElement(new Parameter("OWNED_BY_UNIT",
                DBEngineConstants.TYPE_STRING,
                unitNumber));
        paramProposal.addElement(new Parameter("CREATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        paramProposal.addElement(new Parameter("NSF_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("AGENCY_PROGRAM_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("AGENCY_DIVISION_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("AWARD_TYPE_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramProposal.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProposal.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramProposal.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramProposal.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                "I"));

        StringBuffer sqlProposal = new StringBuffer(
                "call UPDATE_PROPOSAL(");
        sqlProposal.append(" <<PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<PROPOSAL_TYPE_CODE>> , ");
        sqlProposal.append(" <<STATUS_CODE>> , ");
        sqlProposal.append(" <<CREATION_STATUS_CODE>> , ");
        sqlProposal.append(" <<BASE_PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<CONTINUED_FROM>> , ");
        sqlProposal.append(" <<TEMPLATE_FLAG>> , ");
        sqlProposal.append(" <<CURRENT_ACCOUNT_NUMBER>> , ");
        sqlProposal.append(" <<CURRENT_AWARD_NUMBER>> , ");
        sqlProposal.append(" <<TITLE>> , ");
        sqlProposal.append(" <<CFDA_NUMBER>> , ");
        sqlProposal.append(" <<SPONSOR_CODE>> , ");
        sqlProposal.append(" <<PRIME_SPONSOR_CODE>> , ");
        sqlProposal.append(" <<SPONSOR_PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<INTR_COOP_ACTIVITIES_FLAG>> , ");
        sqlProposal.append(" <<INTR_COUNTRY_LIST>> , ");
        sqlProposal.append(" <<OTHER_AGENCY_FLAG>> , ");
        sqlProposal.append(" <<NOTICE_OF_OPPORTUNITY_CODE>> , ");
        sqlProposal.append(" <<PROGRAM_ANNOUNCEMENT_NUMBER>> , ");
        sqlProposal.append(" <<PROGRAM_ANNOUNCEMENT_TITLE>> , ");
        sqlProposal.append(" <<ACTIVITY_TYPE_CODE>> , ");
        sqlProposal.append(" <<REQUESTED_START_DATE_INIT>> , ");
        sqlProposal.append(" <<REQUESTED_START_DATE_TOTAL>> , ");
        sqlProposal.append(" <<REQUESTED_END_DATE_INIT>> , ");
        sqlProposal.append(" <<REQUESTED_END_DATE_TOTAL>> , ");
        sqlProposal.append(" <<DURATION_MONTHS>> , ");
        sqlProposal.append(" <<NUMBER_OF_COPIES>> , ");
        sqlProposal.append(" <<DEADLINE_DATE>> , ");
        sqlProposal.append(" <<DEADLINE_TYPE>> , ");
        sqlProposal.append(" <<MAILING_ADDRESS_ID>> , ");
        sqlProposal.append(" <<MAIL_BY>> , ");
        sqlProposal.append(" <<MAIL_TYPE>> , ");
        sqlProposal.append(" <<CARRIER_CODE_TYPE>> , ");
        sqlProposal.append(" <<CARRIER_CODE>> , ");
        sqlProposal.append(" <<MAIL_DESCRIPTION>> , ");
        sqlProposal.append(" <<MAIL_ACCOUNT_NUMBER>> , ");
        sqlProposal.append(" <<SUBCONTRACT_FLAG>> , ");
        sqlProposal.append(" <<OWNED_BY_UNIT>> , ");
        sqlProposal.append(" <<CREATE_USER>> , ");
        sqlProposal.append(" <<UPDATE_USER>> , ");
        sqlProposal.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProposal.append(" <<NSF_CODE>> , ");
        sqlProposal.append(" <<AGENCY_PROGRAM_CODE>> , ");
        sqlProposal.append(" <<AGENCY_DIVISION_CODE>> , ");
        sqlProposal.append(" <<AWARD_TYPE_CODE>> , ");
        sqlProposal.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProposal.append(" <<AC_TYPE>> )");
            ProcReqParameter procInfo  = new ProcReqParameter();
            procInfo.setDSN(DSN);
            procInfo.setParameterInfo(paramProposal);
            procInfo.setSqlCommand(sqlProposal.toString());

            procedures.add(procInfo);


        ProtocolDetailChangeFlags protocolDetailChangeFlags;
        Vector vecInvestigators = protocolInfoBean.getInvestigators();
        protocolDetailChangeFlags = protocolInfoBean.getProtocolDetailChangeFlags();
        String protocolNumber=protocolInfoBean.getProtocolNumber();
        SequenceLogic sequenceLogic = null;
        sequenceLogic = new SequenceLogic(protocolInfoBean, generateSequence(protocolNumber));
        if(protocolDetailChangeFlags.getIsInvestigatorChanged()){
            vecInvestigators = sequenceLogic.processDetails(vecInvestigators);
        }
        if ((vecInvestigators != null) && (vecInvestigators.size() >0)){
            int length = vecInvestigators.size();
            for(int index=0;index<length;index++){
                ProtocolInvestigatorsBean protocolInvestigatorsBean =
                        (ProtocolInvestigatorsBean)vecInvestigators.elementAt(index);
                    protocolInvestigatorsBean.setProtocolNumber(protocolNumber);
                    procedures.add(addUpdPrposalInvestigator(protocolInvestigatorsBean,propdetail));

                Vector vecInvestigatorsUnits =
                        protocolInvestigatorsBean.getInvestigatorUnits();
               if(protocolDetailChangeFlags.getIsInvestigatorChanged()){
                    vecInvestigatorsUnits = sequenceLogic.processDetails(vecInvestigatorsUnits);
                }
                if ((vecInvestigatorsUnits != null) &&
                        (vecInvestigatorsUnits.size() >0)){
                   int unitslength = vecInvestigatorsUnits.size();


                    for(int unitIndex=0;unitIndex<unitslength;unitIndex++){
                        ProtocolInvestigatorUnitsBean
                                protocolInvestigatorUnitsBean =
                                (ProtocolInvestigatorUnitsBean)
                                vecInvestigatorsUnits.elementAt(unitIndex);
                      //  if (protocolInvestigatorUnitsBean.getAcType() != null) {
                             protocolInvestigatorUnitsBean.setProtocolNumber(protocolNumber);
                              procedures.add(
                                    addUpdProposalInvestigatorUnits(protocolInvestigatorUnitsBean,propdetail,unitNumber));
                     //   }
                    }
                }
            }
        }

        Vector vecLocations = protocolInfoBean.getLocationLists();
        if(protocolDetailChangeFlags.getIsLocationChanged()){
            vecLocations = sequenceLogic.processDetails(vecLocations);
        }
        if ((vecLocations != null) && (vecLocations.size() >0)){
            int length = vecLocations.size();
            Integer siteNumber=1;
            for(int index=0;index<length;index++){
                ProtocolLocationListBean protocolLocationListBean =
                        (ProtocolLocationListBean)vecLocations.elementAt(index);
                if(protocolLocationListBean.getAcType() != null) {
                    protocolLocationListBean.setProtocolNumber(protocolNumber);
                    procedures.add(addUpdProposalLocations(protocolLocationListBean,propdetail,siteNumber));
                    siteNumber++;
                }
            }
        }


        Vector vecSpecialReview = protocolInfoBean.getSpecialReviews();
        if(protocolDetailChangeFlags.getIsSpecialRevChanged()){
            vecSpecialReview = sequenceLogic.processDetails(vecSpecialReview);
        }
        if ((vecSpecialReview != null) && (vecSpecialReview.size() >0)){
            int length = vecSpecialReview.size();
            Integer specialRev_Num=1;
            for(int index=0;index<length;index++){
                ProtocolSpecialReviewFormBean protocolSpecialReviewFormBean =
                        (ProtocolSpecialReviewFormBean)vecSpecialReview.elementAt(index);
                    protocolSpecialReviewFormBean.setProtocolNumber(protocolNumber);
                    procedures.add(addUpdProposalSpecialReview(protocolSpecialReviewFormBean,propdetail,specialRev_Num));
                    specialRev_Num++;
             }
        }
        ProtocolRolesFormBean protocolRolesFormBean = new ProtocolRolesFormBean();
        Vector vUserRoles = null;
        Vector userDet =new Vector();
        Vector vecUserRoles = protocolInfoBean.getUserRoles();
               if(protocolDetailChangeFlags.getIsRolesChanged()){
            vecUserRoles = sequenceLogic.processDetails(vecUserRoles);
        }
        if ((vecUserRoles != null) && (vecUserRoles.size() >0)){
            int length = vecUserRoles.size();
            for(int index=0;index<length;index++){
                protocolRolesFormBean =
                        (ProtocolRolesFormBean)vecUserRoles.elementAt(index);

                if(protocolRolesFormBean.getUserId()!=null){
                   userDet.add(protocolRolesFormBean.getUserId());
                    if  (protocolRolesFormBean.getAcType() != null) {
                    protocolRolesFormBean.setProtocolNumber(protocolNumber);
                    procedures.add(addDeleteProposalRolesForUser(protocolRolesFormBean,propdetail));
                  }
                }
            }
          }else if (protocolInfoBean.getAcType()!=null &&
                protocolInfoBean.getAcType().equals("I")){
            vUserRoles = new Vector();
            protocolRolesFormBean.setProtocolNumber(protocolNumber);
            protocolRolesFormBean.setSequenceNumber(1);
            protocolRolesFormBean.setRoleId(CoeusConstants.PROTOCOL_COORDINATOR_ID);
            protocolRolesFormBean.setUserId(userId);
            protocolRolesFormBean.setAcType("I");
            procedures.add(addDeleteProposalRolesForUser(protocolRolesFormBean,propdetail));
        }

       if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
            success=true;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        returnVector.add(success);
        return returnVector;
    }
        public ProcReqParameter addDeleteProposalRolesForUser( ProtocolRolesFormBean
            protocolUserRoleFormBean,Vector propDetails)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramRolesUser= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        String proposalNumber=(String)propDetails.get(0);
        paramRolesUser.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        paramRolesUser.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolUserRoleFormBean.getUserId()));
        paramRolesUser.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolUserRoleFormBean.getRoleId()) );
        paramRolesUser.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramRolesUser.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRolesUser.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNumber));
        paramRolesUser.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolUserRoleFormBean.getUserId()));
        paramRolesUser.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolUserRoleFormBean.getRoleId()) );
        paramRolesUser.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolUserRoleFormBean.getUpdateTimestamp()));
        paramRolesUser.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
              "I"));

        StringBuffer sqlRolesUser = new StringBuffer(
                "call UPD_PROPOSAL_USER_ROLES(");
        sqlRolesUser.append(" <<PROPOSAL_NUMBER>> , ");
        sqlRolesUser.append(" <<USER_ID>> , ");
        sqlRolesUser.append(" <<ROLE_ID>> , ");
        sqlRolesUser.append(" <<UPDATE_USER>> , ");
        sqlRolesUser.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRolesUser.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlRolesUser.append(" <<AW_USER_ID>> , ");
        sqlRolesUser.append(" <<AW_ROLE_ID>> , ");
        sqlRolesUser.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRolesUser.append(" <<AC_TYPE>> )");

        ProcReqParameter procRolesUser  = new ProcReqParameter();
        procRolesUser.setDSN(DSN);
        procRolesUser.setParameterInfo(paramRolesUser);
        procRolesUser.setSqlCommand(sqlRolesUser.toString());

        return procRolesUser;
    }
        public ProcReqParameter addUpdProposalSpecialReview(ProtocolSpecialReviewFormBean
            protocolSpecialReviewFormBean,Vector propdetail,Integer specialRev_Num)  throws DBException{
        Vector paramPropSR = new Vector();
        String proposalNumber=(String)propdetail.get(0);

        paramPropSR.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNumber));
        paramPropSR.addElement(new Parameter("SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+specialRev_Num));
        paramPropSR.addElement(new Parameter("SPECIAL_REVIEW_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewCode()));
        paramPropSR.addElement(new Parameter("APPROVAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getApprovalCode()));
        paramPropSR.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolSPRevNumber()));
        paramPropSR.addElement(new Parameter("APPLICATION_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApplicationDate()));
        paramPropSR.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApprovalDate()));
        paramPropSR.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getComments()));
        paramPropSR.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPropSR.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPropSR.addElement(new Parameter("EXISTING_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramPropSR.addElement(new Parameter("EXISTING_SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                null));
        paramPropSR.addElement(new Parameter("EXISTING_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, null));
        paramPropSR.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, "I"));

        StringBuffer sqlNarrative = new StringBuffer(
                "call DW_UPDATE_PRO_SPREV(");
        sqlNarrative.append(" <<PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
        sqlNarrative.append(" <<SPECIAL_REVIEW_CODE>> , ");
        sqlNarrative.append(" <<APPROVAL_TYPE_CODE>> , ");
        sqlNarrative.append(" <<PROTOCOL_NUMBER>> , ");
        sqlNarrative.append(" <<APPLICATION_DATE>> , ");
        sqlNarrative.append(" <<APPROVAL_DATE>> , ");
        sqlNarrative.append(" <<COMMENTS>> , ");
        sqlNarrative.append(" <<UPDATE_USER>> , ");
        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<EXISTING_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<EXISTING_SPECIAL_REVIEW_NUMBER>> , ");
        sqlNarrative.append(" <<EXISTING_UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<AC_TYPE>> )");

        ProcReqParameter procSpecialReview  = new ProcReqParameter();
        procSpecialReview.setDSN(DSN);
        procSpecialReview.setParameterInfo(paramPropSR);
        procSpecialReview.setSqlCommand(sqlNarrative.toString());

        return procSpecialReview;

    }
        public ProcReqParameter addUpdProposalLocations( ProtocolLocationListBean
            protocolLocationListBean,Vector proposalDetail,Integer siteNumber)  throws DBException{
        Vector param = new Vector();
        String proposalNumber=(String)proposalDetail.get(0);
         param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalNumber));
        param.addElement(new Parameter("SITE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+siteNumber));
        param.addElement(new Parameter("LOCATION_NAME",
            DBEngineConstants.TYPE_STRING,
            ""+protocolLocationListBean.getOrganizationName()));
        param.addElement(new Parameter("LOCATION_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+protocolLocationListBean.getOrganizationTypeId()));
        param.addElement(new Parameter("ORGANIZATION_ID",
            DBEngineConstants.TYPE_STRING,
            protocolLocationListBean.getOrganizationId()));
        String rolodexId = null;
        if((String.valueOf(protocolLocationListBean.getRolodexId()) != null)){
            rolodexId =String.valueOf(protocolLocationListBean.getRolodexId());
        }
        if(rolodexId == null || rolodexId.trim().length() == 0){
            rolodexId = "0";
        }
        param.addElement(new Parameter("ROLODEX_ID",
            DBEngineConstants.TYPE_STRING,rolodexId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,protocolLocationListBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, "I"));
       StringBuffer sql = new StringBuffer(
               "call UPDATE_PROPOSAL_SITE(");
       sql.append(" <<PROPOSAL_NUMBER>>, ");
       sql.append(" <<SITE_NUMBER>>, ");
       sql.append(" <<LOCATION_NAME>>, ");
       sql.append(" <<LOCATION_TYPE_CODE>>, ");
       sql.append(" <<ORGANIZATION_ID>>, ");
       sql.append(" <<ROLODEX_ID>>, ");
       sql.append(" <<UPDATE_TIMESTAMP>>, ");
       sql.append(" <<UPDATE_USER>>, ");
       sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
       sql.append(" <<AC_TYPE>> ) ");

        ProcReqParameter procLocations  = new ProcReqParameter();
        procLocations.setDSN(DSN);
        procLocations.setParameterInfo(param);
        procLocations.setSqlCommand(sql.toString());

        return procLocations;
    }
     public ProcReqParameter addUpdProposalInvestigatorUnits(
            ProtocolInvestigatorUnitsBean protocolInvestigatorUnitsBean,Vector proposalDetail,String unitNumber)
            throws DBException{
        Vector paramLeadUnit = new Vector();
        String proposalNumber=(String)proposalDetail.get(0);
        paramLeadUnit.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNumber));
        paramLeadUnit.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getPersonId()));
        paramLeadUnit.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getUnitNumber()));
        paramLeadUnit.addElement(new Parameter("LEAD_UNIT_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.isLeadUnitFlag()? "Y": "N"));
        paramLeadUnit.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramLeadUnit.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramLeadUnit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramLeadUnit.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                null));
        paramLeadUnit.addElement(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                unitNumber));
        //Modified on 09th Feb 2004 - start
        //paramLeadUnit.addElement(new Parameter("AW_UPDATE_USER",
        //            DBEngineConstants.TYPE_STRING, userId));
        paramLeadUnit.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, null));
        //Modified on 09th Feb 2004 - end
        paramLeadUnit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                null));
        paramLeadUnit.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                "I"));

        StringBuffer sqlLeadUnit = new StringBuffer(
                "call DW_UPDATE_PROPOSAL_UNITS(");
        sqlLeadUnit.append(" <<PROPOSAL_NUMBER>> , ");
        sqlLeadUnit.append(" <<PERSON_ID>> , ");
        sqlLeadUnit.append(" <<UNIT_NUMBER>> , ");
        sqlLeadUnit.append(" <<LEAD_UNIT_FLAG>> , ");
        sqlLeadUnit.append(" <<UPDATE_USER>> , ");
        sqlLeadUnit.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlLeadUnit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlLeadUnit.append(" <<AW_PERSON_ID>> , ");
        sqlLeadUnit.append(" <<AW_UNIT_NUMBER>> , ");
        sqlLeadUnit.append(" <<AW_UPDATE_USER>> , ");
        sqlLeadUnit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlLeadUnit.append(" <<AC_TYPE>> )");

        ProcReqParameter procInvestigatorUnit  = new ProcReqParameter();
        procInvestigatorUnit.setDSN(DSN);
        procInvestigatorUnit.setParameterInfo(paramLeadUnit);
        procInvestigatorUnit.setSqlCommand(sqlLeadUnit.toString());

        return procInvestigatorUnit;
    }
    public ProcReqParameter addUpdPrposalInvestigator(ProtocolInvestigatorsBean
            protocolInvestigatorsBean,Vector proposalDetail)  throws DBException{
        Vector paramInves = new Vector();
        String proposalNumber=(String)proposalDetail.get(0);
        paramInves.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNumber));
        paramInves.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonId()));
        paramInves.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonName()));
        paramInves.addElement(new Parameter("PI_INVESTIGATOR_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.isPrincipalInvestigatorFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("FACULTY_FLAG",
                DBEngineConstants.TYPE_STRING,
                null) );
        paramInves.addElement(new Parameter("NON_MIT_PERSON_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.isNonEmployeeFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("CONFLICT_OF_INTEREST_FLAG",
                DBEngineConstants.TYPE_STRING,
                null) );
        paramInves.addElement(new Parameter("PERCENTAGE_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(0)));
        paramInves.addElement(new Parameter("FEDR_DEBR_FLAG",
                DBEngineConstants.TYPE_STRING,
                null) );
        paramInves.addElement(new Parameter("FEDR_DELQ_FLAG",
                DBEngineConstants.TYPE_STRING,
                null) );
        paramInves.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInves.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramInves.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                null));
        paramInves.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonId()));

        paramInves.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, protocolInvestigatorsBean.getUpdateUser()));

        paramInves.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolInvestigatorsBean.getUpdateTimestamp()));
        paramInves.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                "I"));
        paramInves.addElement(new Parameter("MULTI_PI_FLAG",
                DBEngineConstants.TYPE_STRING,
                "N") );


        paramInves.addElement(new Parameter("ACADEMIC_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(0)));
        paramInves.addElement(new Parameter("SUMMER_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(0)));
        paramInves.addElement(new Parameter("CALENDAR_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(0)));
        StringBuffer sqlInvestigator = new StringBuffer(
                "call UPDATE_EPS_PROP_INVESTIGATORS(");
        sqlInvestigator.append(" <<PROPOSAL_NUMBER>> , ");
        sqlInvestigator.append(" <<PERSON_ID>> , ");
        sqlInvestigator.append(" <<PERSON_NAME>> , ");
        sqlInvestigator.append(" <<PI_INVESTIGATOR_FLAG>> , ");
        sqlInvestigator.append(" <<FACULTY_FLAG>> , ");
        sqlInvestigator.append(" <<NON_MIT_PERSON_FLAG>> , ");
        sqlInvestigator.append(" <<CONFLICT_OF_INTEREST_FLAG>> , ");
        sqlInvestigator.append(" <<PERCENTAGE_EFFORT>> , ");
        sqlInvestigator.append(" <<FEDR_DEBR_FLAG>> , ");
        sqlInvestigator.append(" <<FEDR_DELQ_FLAG>> , ");
        sqlInvestigator.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInvestigator.append(" <<UPDATE_USER>> , ");


        sqlInvestigator.append(" <<MULTI_PI_FLAG>> , ");


        sqlInvestigator.append(" <<ACADEMIC_YEAR_EFFORT>> , ");
        sqlInvestigator.append(" <<SUMMER_YEAR_EFFORT>> , ");
        sqlInvestigator.append(" <<CALENDAR_YEAR_EFFORT>> , ");
        sqlInvestigator.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlInvestigator.append(" <<AW_PERSON_ID>> , ");
        sqlInvestigator.append(" <<AW_UPDATE_USER>> , ");
        sqlInvestigator.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvestigator.append(" <<AC_TYPE>> )");

        ProcReqParameter procInvestigator  = new ProcReqParameter();
        procInvestigator.setDSN(DSN);
        procInvestigator.setParameterInfo(paramInves);
        procInvestigator.setSqlCommand(sqlInvestigator.toString());
        return procInvestigator;
    }
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

    public boolean addUpdProtocolInfo( ProtocolInfoBean protocolInfoBean)
    throws CoeusException, DBException{


        //prps start - this flag will make sure that the logging is done only when
        // a new protocol is created
        boolean actionLogging = false ;
        //prps end
        Vector paramInfo= new Vector();
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();

        ProtocolDetailChangeFlags protocolDetailChangeFlags;
        SequenceLogic sequenceLogic = null;

        String protocolNumber ;
        boolean success = false;
        //int seqNumber = protocolInfoBean.getSequenceNumber()+1;

        protocolNumber  = protocolInfoBean.getProtocolNumber();
        int seqNumber = protocolInfoBean.getSequenceNumber();
               /*
                * Following code has been commented to implement Sequence Number Enhancements
                * Added the following code to implement the enhancements
                * Commented By : Prasanna Kumar
                *
                /*
                if (protocolInfoBean.getAcType() != null){
                    if  (protocolInfoBean.getAcType().equals("U")) {
                        //protocolInfoBean.setAcType("I");
                    }
                    //protocolInfoBean.setUpdateTimestamp(dbTimestamp);
                    if (protocolInfoBean.getAcType().equals("I")) {
                        seqNumber = protocolInfoBean.getSequenceNumber()+1;
                        protocolInfoBean.setCreateTimestamp(dbTimestamp);
                        //prps start
                            actionLogging = true ;
                        //prps end
                        //protocolInfoBean.setUpdateTimestamp(dbTimestamp);
                    }
                }
                */

        //Seq No.Enhancements - Start
        protocolDetailChangeFlags = protocolInfoBean.getProtocolDetailChangeFlags();
        //Need not call Generate Sequence Logic if new Protocol - start - Prasanna - 10 Oct, 2003
        if(protocolInfoBean.getSequenceNumber()!=0){
            if(isProtocolChanged(protocolDetailChangeFlags)){
                int oldSeqNum = protocolInfoBean.getSequenceNumber();
                sequenceLogic = new SequenceLogic(protocolInfoBean, generateSequence(protocolNumber));
                protocolInfoBean = (ProtocolInfoBean)sequenceLogic.getParentDataBean();
                //Code added for coeus4.3 enhancements for implementing indicator logic
                //If the new sequence is generated, then the P1 indicator should be modified to P0
                //and the N1 indicator to be modified to N0
                if(protocolInfoBean.getSequenceNumber() > oldSeqNum){
                    protocolInfoBean.setKeyStudyIndicator(
                            indicatorValue(protocolInfoBean.getKeyStudyIndicator()));

                    protocolInfoBean.setCorrespondenceIndicator(
                            indicatorValue(protocolInfoBean.getCorrespondenceIndicator()));

                    protocolInfoBean.setFundingSourceIndicator(
                            indicatorValue(protocolInfoBean.getFundingSourceIndicator()));

                    protocolInfoBean.setVulnerableSubjectIndicator(
                            indicatorValue(protocolInfoBean.getVulnerableSubjectIndicator()));

                    protocolInfoBean.setSpecialReviewIndicator(
                            indicatorValue(protocolInfoBean.getSpecialReviewIndicator()));

                    protocolInfoBean.setReferenceIndicator(
                            indicatorValue(protocolInfoBean.getReferenceIndicator()));
                }
            }
        }else{
            sequenceLogic = new SequenceLogic(protocolInfoBean, false);
            protocolInfoBean = (ProtocolInfoBean)sequenceLogic.getParentDataBean();
        }
        //Need not call Generate Sequence Logic if new Protocol - end

        //Protocol Enhancement -  Administrative Correction Start 1
        if (protocolInfoBean.getAcType() != null){
            //Protocol Enhancement -  Administrative Correction End 1
            if (protocolInfoBean.getAcType().equals("I")) {

                //Check if no sequence number is assigned.
                //If yes set sequence number as 1
                if(protocolInfoBean.getSequenceNumber()==0){
                    protocolInfoBean.setSequenceNumber(1);
                    seqNumber = 1;
                }
                //Added to log actions only if new Protocol is created - start -Prasanna -10 Oct, 2003
                if(seqNumber!=protocolInfoBean.getSequenceNumber()){
                    actionLogging = false ;
                }else{
                    actionLogging = true ;
                }
                //Added to log actions only if new Protocol is created - end
                seqNumber = protocolInfoBean.getSequenceNumber();
                protocolInfoBean.setCreateTimestamp(dbTimestamp);
                //actionLogging = true ;
            }
            //Seq No.Enhancements - End

            paramInfo.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProtocolNumber()));
            paramInfo.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+seqNumber));
            paramInfo.addElement(new Parameter("PROTOCOL_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getProtocolTypeCode()));
            paramInfo.addElement(new Parameter("PROTOCOL_STATUS_CODE",
                    DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getProtocolStatusCode()));
            paramInfo.addElement(new Parameter("TITLE",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getTitle()));
            paramInfo.addElement(new Parameter("DESCRIPTION",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getDescription()));
            paramInfo.addElement(new Parameter("SPECIAL_REVIEW_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getSpecialReviewIndicator()));
            paramInfo.addElement(new Parameter("VUL_SUBJECT_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getVulnerableSubjectIndicator()));
            paramInfo.addElement(new Parameter("APPLICATION_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getApplicationDate()));
            paramInfo.addElement(new Parameter("APPROVAL_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getApprovalDate()));
            paramInfo.addElement(new Parameter("EXPIRATION_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getExpirationDate()));
            paramInfo.addElement(new Parameter("FDA_APPLICATION_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getFDAApplicationNumber()));
            paramInfo.addElement(new Parameter("IS_BILLABLE",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.isBillableFlag()? "Y": "N") );
            paramInfo.addElement(new Parameter("REFERENCE_NUMBER_1",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getRefNum_1()));
            paramInfo.addElement(new Parameter("REFERENCE_NUMBER_2",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getRefNum_2()));
            paramInfo.addElement(new Parameter("KEY_STUDY_PERSON_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getKeyStudyIndicator()));
            paramInfo.addElement(new Parameter("FUNDING_SOURCE_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getFundingSourceIndicator()));
            paramInfo.addElement(new Parameter("CORRESPONDENT_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getCorrespondenceIndicator()));
            paramInfo.addElement(new Parameter("REFERENCE_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getReferenceIndicator()));
            if(protocolInfoBean.getProjectsIndicator()==null){
                protocolInfoBean.setProjectsIndicator("NO");
            }
            paramInfo.addElement(new Parameter("RELATED_PROJECTS_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProjectsIndicator()));
            paramInfo.addElement(new Parameter("CREATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramInfo.addElement(new Parameter("CREATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    protocolInfoBean.getCreateTimestamp()));
            paramInfo.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramInfo.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    dbTimestamp));
            // Added for case # 3090 - start
            paramInfo.addElement(new Parameter("LAST_APPROVAL_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getLastApprovalDate()));
            // Added for case # 3090 - end
            paramInfo.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProtocolNumber()));
            paramInfo.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getSequenceNumber()));
            paramInfo.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramInfo.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    protocolInfoBean.getUpdateTimestamp()));
            paramInfo.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getAcType()));

            StringBuffer sqlInfo = new StringBuffer(
                    "call upd_protocol(");
            sqlInfo.append(" <<PROTOCOL_NUMBER>> , ");
            sqlInfo.append(" <<SEQUENCE_NUMBER>> , ");
            sqlInfo.append(" <<PROTOCOL_TYPE_CODE>> , ");
            sqlInfo.append(" <<PROTOCOL_STATUS_CODE>> , ");
            sqlInfo.append(" <<TITLE>> , ");
            sqlInfo.append(" <<DESCRIPTION>> , ");
            sqlInfo.append(" <<SPECIAL_REVIEW_INDICATOR>> , ");
            sqlInfo.append(" <<VUL_SUBJECT_INDICATOR>> , ");
            sqlInfo.append(" <<APPLICATION_DATE>> , ");
            sqlInfo.append(" <<APPROVAL_DATE>> , ");
            sqlInfo.append(" <<EXPIRATION_DATE>> , ");
            sqlInfo.append(" <<FDA_APPLICATION_NUMBER>> , ");
            sqlInfo.append(" <<IS_BILLABLE>> , ");
            sqlInfo.append(" <<REFERENCE_NUMBER_1>> , ");
            sqlInfo.append(" <<REFERENCE_NUMBER_2>> , ");
            sqlInfo.append(" <<KEY_STUDY_PERSON_INDICATOR>> , ");
            sqlInfo.append(" <<FUNDING_SOURCE_INDICATOR>> , ");
            sqlInfo.append(" <<CORRESPONDENT_INDICATOR>> , ");
            sqlInfo.append(" <<REFERENCE_INDICATOR>> , ");
            sqlInfo.append(" <<RELATED_PROJECTS_INDICATOR>> , ");

            sqlInfo.append(" <<CREATE_USER>> , ");
            sqlInfo.append(" <<CREATE_TIMESTAMP>> , ");
            sqlInfo.append(" <<UPDATE_USER>> , ");
            sqlInfo.append(" <<UPDATE_TIMESTAMP>> , ");
             // Added for case # 3090 - start
            sqlInfo.append(" <<LAST_APPROVAL_DATE>> , ");
            // Added for case # 3090 - end
            sqlInfo.append(" <<AW_PROTOCOL_NUMBER>> , ");
            sqlInfo.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sqlInfo.append(" <<AW_UPDATE_USER>> , ");
            sqlInfo.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlInfo.append(" <<AC_TYPE>> )");

            ProcReqParameter procInfo  = new ProcReqParameter();
            procInfo.setDSN(DSN);
            procInfo.setParameterInfo(paramInfo);
            procInfo.setSqlCommand(sqlInfo.toString());

            procedures.add(procInfo);
        }

        // inserting new key Person
        Vector vecKeyPerson =
                protocolInfoBean.getKeyStudyPersonnel();

        //Seq No. Enhamcements - Start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsKeyStudyChanged()){
            vecKeyPerson = sequenceLogic.processDetails(vecKeyPerson);
        }
        //Seq No. Enhamcements - End

        if ((vecKeyPerson != null) && (vecKeyPerson.size() >0)){
            int keyLength = vecKeyPerson.size();
            for(int keyIndex=0;keyIndex<keyLength;keyIndex++){
                ProtocolKeyPersonnelBean protocolKeyPersonnelBean =
                        (ProtocolKeyPersonnelBean)vecKeyPerson.elementAt(keyIndex);

                        /*if  (protocolKeyPersonnelBean.getAcType() == null ||
                        protocolKeyPersonnelBean.getAcType().equals("U")) {
                            protocolKeyPersonnelBean.setAcType("I");
                        }*/
                if  (protocolKeyPersonnelBean.getAcType() != null) {
                    //protocolKeyPersonnelBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolKeyPersonnelBean.getAcType().equals("I")) {
                                //protocolKeyPersonnelBean.setUpdateTimestamp(
                                //dbTimestamp);
                                protocolKeyPersonnelBean.setProtocolNumber(
                                protocolNumber);
                                protocolKeyPersonnelBean.setSequenceNumber(seqNumber);
                            }*/

                    protocolKeyPersonnelBean.setProtocolNumber(protocolNumber);
                    //if ( !protocolKeyPersonnelBean.getAcType().equalsIgnoreCase("D")) {
                    procedures.add(addUpdProtocolKeyPerson(protocolKeyPersonnelBean));
                    //}
                }
            }
        }
        // inserting new locations
        Vector vecLocations = protocolInfoBean.getLocationLists();

        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsLocationChanged()){
            vecLocations = sequenceLogic.processDetails(vecLocations);
        }
        //Seq No. Enhamcements - End

        if ((vecLocations != null) && (vecLocations.size() >0)){
            int length = vecLocations.size();
            for(int index=0;index<length;index++){
                ProtocolLocationListBean protocolLocationListBean =
                        (ProtocolLocationListBean)vecLocations.elementAt(index);
                        /*if  (protocolLocationListBean.getAcType() == null ||
                        protocolLocationListBean.getAcType().equals("U")) {
                            protocolLocationListBean.setAcType("I");
                        }*/
                if(protocolLocationListBean.getAcType() != null) {
                    //protocolLocationListBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolLocationListBean.getAcType().equals("I")) {
                                protocolLocationListBean.setProtocolNumber(
                                protocolNumber);
                                protocolLocationListBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolLocationListBean.setProtocolNumber(protocolNumber);
                    //if (!protocolLocationListBean.getAcType().equalsIgnoreCase("D")) {
                    procedures.add(addUpdProtocolLocations(protocolLocationListBean));
                    //}
                }
            }
        }
        // inserting new vulnerable
        Vector vecVulnerable = protocolInfoBean.getVulnerableSubjectLists();

        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsSubjectsChanged()){
            vecVulnerable = sequenceLogic.processDetails(vecVulnerable);
        }
        //Seq No. Enhamcements - End

        if ((vecVulnerable != null) && (vecVulnerable.size() >0)){
            int length = vecVulnerable.size();
            for(int index=0;index<length;index++){
                ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean =
                        (ProtocolVulnerableSubListsBean)vecVulnerable.elementAt(index);
                        /*if (protocolVulnerableSubListsBean.getAcType() == null ||
                        protocolVulnerableSubListsBean.getAcType().equals("U")){
                            protocolVulnerableSubListsBean.setAcType("I");
                        }*/
                if (protocolVulnerableSubListsBean.getAcType() != null) {
                    //protocolVulnerableSubListsBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolVulnerableSubListsBean.getAcType().equals("I")){
                                protocolVulnerableSubListsBean.setProtocolNumber(
                                protocolNumber);
                                protocolVulnerableSubListsBean.setSequenceNumber(
                                seqNumber);
                            }*/
                    protocolVulnerableSubListsBean.setProtocolNumber(protocolNumber);
                    //if (!protocolVulnerableSubListsBean.getAcType().equalsIgnoreCase("D") ) {
                    procedures.add(addUpdProtocolVulnerable(protocolVulnerableSubListsBean));
                    //}
                }
            }
        }
        // inserting new Investigators
        Vector vecInvestigators = protocolInfoBean.getInvestigators();
        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsInvestigatorChanged()){
            vecInvestigators = sequenceLogic.processDetails(vecInvestigators);
        }
        //Seq No. Enhamcements - End
        if ((vecInvestigators != null) && (vecInvestigators.size() >0)){
            int length = vecInvestigators.size();
            for(int index=0;index<length;index++){
                ProtocolInvestigatorsBean protocolInvestigatorsBean =
                        (ProtocolInvestigatorsBean)vecInvestigators.elementAt(index);
                        /*if  (protocolInvestigatorsBean.getAcType() == null ||
                        protocolInvestigatorsBean.getAcType().equals("U")) {
                            protocolInvestigatorsBean.setAcType("I");
                        }*/
                if (protocolInvestigatorsBean.getAcType() != null) {
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                    //protocolInvestigatorsBean.setUpdateTimestamp(dbTimestamp);
                            /*if  (protocolInvestigatorsBean.getAcType().equals("I")) {
                                protocolInvestigatorsBean.setProtocolNumber(
                                protocolNumber);
                                protocolInvestigatorsBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolInvestigatorsBean.setProtocolNumber(protocolNumber);
                    //if (!protocolInvestigatorsBean.getAcType().equalsIgnoreCase("D") ) {
                    procedures.add(addUpdProtocolInvestigator(protocolInvestigatorsBean));
                    //}
                }
                Vector vecInvestigatorsUnits =
                        protocolInvestigatorsBean.getInvestigatorUnits();
                //Seq No. Enhamcements - start
                //Modifiied By  : Prasanna Kumar
                if(protocolDetailChangeFlags.getIsInvestigatorChanged()){
                    vecInvestigatorsUnits = sequenceLogic.processDetails(vecInvestigatorsUnits);
                }
                //Seq No. Enhamcements - End

                if ((vecInvestigatorsUnits != null) &&
                        (vecInvestigatorsUnits.size() >0)){
                    // modified by ravi, changed the vector from
                    // vecInvestigators to vecInvestigatorsUnits because here
                    // we are dealing with investigator units.
                    int unitslength = vecInvestigatorsUnits.size();

                    // modified by ravi, changed the loop varaible
                    for(int unitIndex=0;unitIndex<unitslength;unitIndex++){
                        ProtocolInvestigatorUnitsBean
                                protocolInvestigatorUnitsBean =
                                (ProtocolInvestigatorUnitsBean)
                                vecInvestigatorsUnits.elementAt(unitIndex);
                                /*if  (protocolInvestigatorUnitsBean.getAcType() == null
                                    || protocolInvestigatorUnitsBean.getAcType().equals("U")) {
                                    protocolInvestigatorUnitsBean.setAcType("I");
                                }*/
                        if (protocolInvestigatorUnitsBean.getAcType() != null) {
                            //protocolInvestigatorUnitsBean.setUpdateTimestamp(dbTimestamp);
                                    /*
                                     * Following code has been commented to implement Sequence Number Enhancements
                                     * Commented By : Prasanna Kumar
                                     *
                                     */
                                    /*if (protocolInvestigatorUnitsBean.getAcType()
                                    .equals("I")) {
                                        protocolInvestigatorUnitsBean
                                        .setProtocolNumber(protocolNumber);
                                        protocolInvestigatorUnitsBean
                                        .setSequenceNumber(seqNumber);
                                    }*/

                            protocolInvestigatorUnitsBean.setProtocolNumber(protocolNumber);

                            //if (!protocolInvestigatorUnitsBean.getAcType().equalsIgnoreCase("D")) {
                            procedures.add(
                                    addUpdProtocolInvestigatorUnits(protocolInvestigatorUnitsBean));
                            //}
                        }
                    }
                }
            }
        }
        // inserting new Correspondents
        //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-Start
        Vector vecCorrespondents = addDefaultIRBCorrespondence(protocolInfoBean.getCorrespondetns(),protocolInfoBean);
        if(vecCorrespondents != null && vecCorrespondents.size()>0){
           if(isIndicatorUpdated){
             procedures.add(updateIRBProtocolIndicator(protocolInfoBean));
            }
        }
        //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-End

        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsCorrespondentsChanged()){
            vecCorrespondents = sequenceLogic.processDetails(vecCorrespondents);
        }
        //Seq No. Enhamcements - End

        if ((vecCorrespondents != null) && (vecCorrespondents.size() >0)){
            int length = vecCorrespondents.size();
            for(int index=0;index<length;index++){
                ProtocolCorrespondentsBean protocolCorrespondentsBean =
                        (ProtocolCorrespondentsBean)vecCorrespondents.elementAt(index);

                        /*if  (protocolCorrespondentsBean.getAcType() == null ||
                            protocolCorrespondentsBean.getAcType().equals("U")) {
                            protocolCorrespondentsBean.setAcType("I");
                        }*/

                if  (protocolCorrespondentsBean.getAcType() != null) {
                    //protocolCorrespondentsBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */

                            /*if (protocolCorrespondentsBean.getAcType().equals("I")) {
                                protocolCorrespondentsBean.setProtocolNumber(
                                protocolNumber);
                                protocolCorrespondentsBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolCorrespondentsBean.setProtocolNumber(protocolNumber);
                    //if (!protocolCorrespondentsBean.getAcType().equalsIgnoreCase("D")) {
                    procedures.add(addUpdProtocolCorrespondents(protocolCorrespondentsBean));
                    //}
                }
            }
        }
        // inserting new notes
        Vector vecNotes = protocolInfoBean.getVecNotepad();
        //COEUSQA-2796 - IACUC and IRB - notes being duplicated in withdrawn protocols in Premium
        /*
         * New sequence number is creating only for newly added notes (sequence number = 0),
         * Not copying the previous sequence notes to the new sequence.
         */
        Vector modifiedNotes = new Vector();
        if ((vecNotes != null) && (vecNotes.size() > 0)){
            ProtocolNotepadBean notepadBean =  null;
            for(int index=0, length = vecNotes.size(); index<length; index++){
                notepadBean = (ProtocolNotepadBean)vecNotes.elementAt(index);
                if(notepadBean.getSequenceNumber() == 0) {
                    modifiedNotes.add(notepadBean);
                }
            }
        }
        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsNotesChanged()){
            vecNotes = sequenceLogic.processDetails(modifiedNotes);
        }
        //Seq No. Enhamcements - End
        if ((vecNotes != null) && (vecNotes.size() > 0)){
            int length = vecNotes.size();
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            int maxEntryNumber = protocolDataTxnBean.getMaxProtocolNotesEntryNumber(protocolNumber);
            for(int index=0;index<length;index++){
                ProtocolNotepadBean protocolNotepadBean =
                        (ProtocolNotepadBean)vecNotes.elementAt(index);
                        /*
                         * Following code has been commented and added new code
                         * to implement Sequence Number Enhancements.
                         * Commented By : Prasanna Kumar
                         *
                         */
                        /*if ((protocolNotepadBean.getAcType() != null) &&
                            ("I".equalsIgnoreCase(protocolNotepadBean.getAcType()))) {
                            protocolNotepadBean.setProtocolNumber(protocolNumber);
                            protocolNotepadBean.setSequenceNumber(seqNumber);
                            procedures.add(addUpdProtocolNotepad(protocolNotepadBean));
                        }*/
                if (protocolNotepadBean.getAcType() != null){
                    protocolNotepadBean.setProtocolNumber(protocolNumber);
                            /* Added by Shivakumar for bug fixing
                             * while saving notes when protocol has been opened in Display
                             * mode - 12/10/2004
                             */
                    protocolNotepadBean.setSequenceNumber(seqNumber);
                    //End - Shivakumar
                    maxEntryNumber = maxEntryNumber + 1;
                    protocolNotepadBean.setEntryNumber(maxEntryNumber);
                    procedures.add(addUpdProtocolNotepad(protocolNotepadBean));
                }
            }
        }
        // inserting new Arearesearch
        Vector vecAreaResearch = protocolInfoBean.getAreaOfResearch();
        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsAORChanged()){
            vecAreaResearch = sequenceLogic.processDetails(vecAreaResearch);
        }
        //Seq No. Enhamcements - End
        if ((vecAreaResearch != null) && (vecAreaResearch.size() >0)){
            int length = vecAreaResearch.size();
            for(int index=0;index<length;index++){
                ProtocolReasearchAreasBean protocolReasearchAreasBean =
                        (ProtocolReasearchAreasBean)vecAreaResearch.elementAt(index);
                        /*if  (protocolReasearchAreasBean.getAcType() == null ||
                            protocolReasearchAreasBean.getAcType().equals("U")) {
                            protocolReasearchAreasBean.setAcType("I");
                        }*/
                if  (protocolReasearchAreasBean.getAcType() != null) {
                    //protocolReasearchAreasBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolReasearchAreasBean.getAcType().equals("I")) {
                                protocolReasearchAreasBean.setProtocolNumber(
                                protocolNumber);
                                protocolReasearchAreasBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolReasearchAreasBean.setProtocolNumber(protocolNumber);
                    //if (!protocolReasearchAreasBean.getAcType().equalsIgnoreCase("D") ) {
                    procedures.add(addUpdProtocolResearchArea(protocolReasearchAreasBean));
                    //}
                }
            }
        }

        // inserting new FundingSource
        Vector vecFundSource = protocolInfoBean.getFundingSources();
        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        //Commented with case 4379:Funding removed in an approved amendment still appears in protocol
//        if(protocolDetailChangeFlags.getIsFundingSourceChanged()){
//            vecFundSource = sequenceLogic.processDetails(vecFundSource);
//        }
        //Seq No. Enhamcements - End
        if ((vecFundSource != null) && (vecFundSource.size() >0)){
            int length = vecFundSource.size();

            notepadBeans = new CoeusVector();
            lockedBeans = new Vector();
            for(int index=0;index<length;index++){
                ProtocolFundingSourceBean protocolFundingSourceBean =
                        (ProtocolFundingSourceBean)vecFundSource.elementAt(index);
                        /*if  (protocolFundingSourceBean.getAcType() == null ||
                            protocolFundingSourceBean.getAcType().equals("U")) {
                            protocolFundingSourceBean.setAcType("I");
                        }*/
                if (protocolFundingSourceBean.getAcType() != null) {
                    procedures.addAll(
                            updateFundingSourceLink(protocolFundingSourceBean,protocolNumber, seqNumber,protocolInfoBean.getAcType(),new Vector(),lockedBeans));
                    //protocolFundingSourceBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolFundingSourceBean.getAcType().equals("I")) {
                                protocolFundingSourceBean.setProtocolNumber(
                                protocolNumber);
                                protocolFundingSourceBean.setSequenceNumber(seqNumber);
                            }*/



                    //Coeus Enhancement: Case #1799 step 4 start
//                    CoeusFunctions functions= new CoeusFunctions();
//                    String linkAward=functions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK);
//                    String linkProposal=functions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK);
//                    String indicator="P1";
//
//                    CoeusMessageResourcesBean coeusMessageResourcesBean
//                            =new CoeusMessageResourcesBean();
//                    if(protocolFundingSourceBean.getFundingSourceTypeCode()== 5){
//                        if(linkProposal.equals("1")) {
//
//                            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
//                            boolean getLock = instituteProposalTxnBean.lockCheck(protocolFundingSourceBean.getFundingSource(),userId);
//                            if(getLock){
//                                InstituteProposalBean instituteProposalBean = instituteProposalTxnBean.getInstituteProposalDetails(protocolFundingSourceBean.getFundingSource());
//                                int maxSpecialRevNumber = instituteProposalTxnBean.getMaxInstituteProposalSpecialReviewNumber(instituteProposalBean.getProposalNumber(),instituteProposalBean.getSequenceNumber());
//
//                                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(userId);
//                                InstituteProposalSpecialReviewBean insituteProposalSpReviewBean = null;
//                                try {
//                                    LockingBean lockingBean = instituteProposalTxnBean.getInstituteProposalLock(protocolFundingSourceBean.getFundingSource(),userId,unitNumber);
//                                } catch(Exception e) {
//                                    unLockBeans();
//                                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
//                                    // String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource();
//                                    throw new LockingException(msg);
//                                }
//                                if(protocolFundingSourceBean.getAcType().equals("I")) {
//                                    insituteProposalSpReviewBean = new InstituteProposalSpecialReviewBean();
//                                    insituteProposalSpReviewBean.setProposalNumber(protocolFundingSourceBean.getFundingSource());
//                                    insituteProposalSpReviewBean.setSpecialReviewCode(new Integer(functions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN)).intValue());
//                                    insituteProposalSpReviewBean.setApprovalCode(new Integer(functions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE)).intValue());
//                                    insituteProposalSpReviewBean.setSpecialReviewNumber(maxSpecialRevNumber+1);
//                                    insituteProposalSpReviewBean.setProtocolSPRevNumber(protocolNumber);
//                                    insituteProposalSpReviewBean.setAcType("I");
//                                    insituteProposalSpReviewBean.setComments(coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IRBSpecialReview.1000"));
//
//                                    insituteProposalSpReviewBean.setSequenceNumber(instituteProposalBean.getSequenceNumber());
//                                    // Code modified for Case#3070 - Ability to modify funding source - starts
//                                    lockedBeans.add(insituteProposalSpReviewBean);
//                                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                    boolean canLink = protocolDataTxnBean.canAddProtocolLinks(protocolNumber, "2",
//                                            protocolFundingSourceBean.getFundingSource());
//                                    if(protocolNumber != null && protocolNumber.length() == 10 && canLink){
//                                        procedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(insituteProposalSpReviewBean));
////                                        lockedBeans.add(insituteProposalSpReviewBean);
//                                        updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"2");
////                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                        //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                                        try {
//                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
//                                                    protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);
//
//                                        } catch(Exception e) {
//                                        }
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,insituteProposalSpReviewBean));
//                                        updateSpecialReviewNotePad(protocolFundingSourceBean);
//                                        Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,protocolFundingSourceBean.getAcType());
//                                        if(inboxData != null)
//                                            procedures.addAll(inboxData);
//                                    } else if((protocolInfoBean.getAcType() == null || protocolInfoBean.getAcType().equals("U")) && canLink){
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,insituteProposalSpReviewBean));
//                                    }
//                                    // Code modified for Case#3070 - Ability to modify funding source - ends
//                                } else if(protocolFundingSourceBean.getAcType().equals("D")) {
//                                    Vector specRevCount =  instituteProposalTxnBean.getInstituteProposalSpecialReview(protocolFundingSourceBean.getFundingSource());
//                                    specRevCount = (specRevCount == null)? new CoeusVector() : specRevCount;
//                                    int size = specRevCount.size();
//                                    for(int i = 0;i<specRevCount.size();i++){
//                                        insituteProposalSpReviewBean =(InstituteProposalSpecialReviewBean)specRevCount.get(i);
//                                        String specialRevNum = insituteProposalSpReviewBean.getProtocolSPRevNumber();
//                                        specialRevNum = (specialRevNum == null)? "" : specialRevNum;
//                                        if(specialRevNum.equals(protocolNumber.substring(0, 10))){
//                                            insituteProposalSpReviewBean.setAcType("D");
//                                            break;
//                                        } else
//                                            insituteProposalSpReviewBean = null;
//                                    }
//                                    // Code modified for Case#4379 - Funding sources not inserting "D" records to links table for amendment/renewal
//                                    /*
//                                     *  This is for looging into OSP$PROTOCOL_LINKS
//                                     */
//                                    if(insituteProposalSpReviewBean==null){
//                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                        Vector vecProtocolLinks = protocolDataTxnBean.getProtocolLinksData(protocolNumber);
//                                        if(vecProtocolLinks!=null && !vecProtocolLinks.isEmpty()){
//                                            String fundingSrcToDelete = protocolFundingSourceBean.getFundingSource();
//                                            for(int i=0;i<vecProtocolLinks.size();i++){
//                                                ProtocolLinkBean protocolLinkBean = (ProtocolLinkBean)vecProtocolLinks.elementAt(i);
//                                                if(protocolLinkBean.getModuleItemKey().equals(fundingSrcToDelete)
//                                                            && "I".equals(protocolLinkBean.getActionType())){
//                                                     insituteProposalSpReviewBean = new InstituteProposalSpecialReviewBean();
//                                                     insituteProposalSpReviewBean.setProposalNumber(fundingSrcToDelete);
//                                                     insituteProposalSpReviewBean.setSequenceNumber(protocolLinkBean.getModuleItemSeqNumber());
//                                                     break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                    // Case#4379 end
//                                    // Code modified for Case#3070 - Ability to modify funding source - starts
//                                    lockedBeans.add(insituteProposalSpReviewBean);
////                                    if(insituteProposalSpReviewBean != null) {
//                                    if(insituteProposalSpReviewBean != null && protocolNumber != null && protocolNumber.length() == 10 && insituteProposalSpReviewBean.getAcType()!=null) {
//                                        procedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(insituteProposalSpReviewBean));
////                                        lockedBeans.add(insituteProposalSpReviewBean);
//                                    // Code modified for Case#3070 - Ability to modify funding source - ends
//                                        size--;
//                                        if(size==0)
//                                            indicator = "N1";
//
//
//                                        updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"2");
//                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                        //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                                        try {
//                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
//                                                    protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);
//                                        } catch(Exception e) {
//                                        }
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,insituteProposalSpReviewBean));
//                                        updateSpecialReviewNotePad(protocolFundingSourceBean);
//                                        Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,protocolFundingSourceBean.getAcType());
//                                        if(inboxData != null)
//                                            procedures.addAll(inboxData);
//                                    // Code modified for Case#3070 - Ability to modify funding source - starts
//                                    } else if(insituteProposalSpReviewBean != null){
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,insituteProposalSpReviewBean));
//                                    }
//                                    // Code modified for Case#3070 - Ability to modify funding source - ends
//                                }
//                            } else {
//                                unLockBeans();
//                                String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
//                                // String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource();
//
//                                throw new LockingException(msg);
//                            }
//                        }
//                    } else if(protocolFundingSourceBean.getFundingSourceTypeCode()== 6) {
//
//                        if(linkAward.equals("1")) {
//                            AwardTxnBean awardTxnBean = new AwardTxnBean();
//                            boolean getLock = awardTxnBean.lockCheck(protocolFundingSourceBean.getFundingSource(),userId);
//                            if(getLock){
//
//                                AwardBean awardBean = awardTxnBean.getAward(protocolFundingSourceBean.getFundingSource());
//                                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
//                                int maxSpecialRevNumber = awardLookUpDataTxnBean.getMaxAwardSpecialReviewNumber(awardBean.getMitAwardNumber(),awardBean.getSequenceNumber());
//                                AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(userId);
//                                AwardSpecialReviewBean awardSpReviewBean = null;
//                                try {
//                                    LockingBean lockingBean = awardTxnBean.getAwardLock(protocolFundingSourceBean.getFundingSource(),userId,unitNumber);
//                                } catch(Exception e) {
//                                    unLockBeans();
//                                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
//                                    //String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource();
//                                    throw new LockingException(msg);
//                                }
//
//                                if(protocolFundingSourceBean.getAcType().equals("I")) {
//                                    awardSpReviewBean = new AwardSpecialReviewBean();
//                                    awardSpReviewBean.setMitAwardNumber(protocolFundingSourceBean.getFundingSource());
//                                    awardSpReviewBean.setSpecialReviewCode(new Integer(functions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN)).intValue());
//                                    awardSpReviewBean.setApprovalCode(new Integer(functions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE)).intValue());
//                                    awardSpReviewBean.setSpecialReviewNumber(maxSpecialRevNumber+1);
//
//                                    awardSpReviewBean.setProtocolSPRevNumber(protocolNumber);
//                                    awardSpReviewBean.setAcType("I");
//                                    awardSpReviewBean.setComments(coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IRBSpecialReview.1000"));
//                                    awardSpReviewBean.setSequenceNumber(awardBean.getSequenceNumber());
//                                    // Code modified for Case#3070 - Ability to modify funding source - starts
//                                    lockedBeans.add(awardSpReviewBean);
//                                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                    boolean canLink = protocolDataTxnBean.canAddProtocolLinks(protocolNumber, "2",
//                                            protocolFundingSourceBean.getFundingSource());
//                                    if(protocolNumber != null && protocolNumber.length() == 10 && canLink){
//                                        procedures.add(awardUpdateTxnBean.addUpdAwardSpecialReview(awardSpReviewBean));
////                                        lockedBeans.add(awardSpReviewBean);
//                                        updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"1");
////                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                        //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                                        try {
//                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
//                                                    protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE);
//                                        } catch(Exception e) {
//                                        }
//
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,awardSpReviewBean));
//                                        updateSpecialReviewNotePad(protocolFundingSourceBean);
//                                        Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE,protocolFundingSourceBean.getAcType());
//                                        if(inboxData != null)
//                                            procedures.addAll(inboxData);
//                                    } else if((protocolInfoBean.getAcType() == null || protocolInfoBean.getAcType().equals("U")) && canLink){
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,awardSpReviewBean));
//                                    }
//                                    // Code modified for Case#3070 - Ability to modify funding source - ends
//                                } else if(protocolFundingSourceBean.getAcType().equals("D")) {
//                                    Vector specRevCount =  awardTxnBean.getAwardSpecialReview(protocolFundingSourceBean.getFundingSource());
//                                    specRevCount = (specRevCount == null)? new CoeusVector() : specRevCount;
//                                    int size = specRevCount.size();
//
//                                    for(int i = 0;i<specRevCount.size();i++){
//                                        awardSpReviewBean =(AwardSpecialReviewBean)specRevCount.get(i);
//                                        String specialRevNum = awardSpReviewBean.getProtocolSPRevNumber();
//                                        specialRevNum = (specialRevNum == null)? "" : specialRevNum;
//                                        if(specialRevNum.equals(protocolNumber.substring(0, 10))){
////                                        if(protocolNumber.equals(awardSpReviewBean.getProtocolSPRevNumber())){
//                                            awardSpReviewBean.setAcType("D");
//                                            break;
//                                        } else
//                                            awardSpReviewBean = null;
//                                    }
//                                    // Code modified for Case#4379 - Funding sources not inserting "D" records to links table for amendment/renewal
//                                    /*
//                                     * This is for looging into OSP$PROTOCOL_LINKS
//                                     */
//                                    if(awardSpReviewBean==null){
//                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                        Vector vecProtocolLinks = protocolDataTxnBean.getProtocolLinksData(protocolNumber);
//                                        if(vecProtocolLinks!=null && !vecProtocolLinks.isEmpty()){
//                                            String fundingSrcToDelete = protocolFundingSourceBean.getFundingSource();
//                                            for(int i=0;i<vecProtocolLinks.size();i++){
//                                                ProtocolLinkBean protocolLinkBean = (ProtocolLinkBean)vecProtocolLinks.elementAt(i);
//                                                if(protocolLinkBean.getModuleItemKey().equals(fundingSrcToDelete)
//                                                            && "I".equals(protocolLinkBean.getActionType())){
//                                                     awardSpReviewBean = new AwardSpecialReviewBean();
//                                                     awardSpReviewBean.setMitAwardNumber(fundingSrcToDelete);
//                                                     awardSpReviewBean.setSequenceNumber(protocolLinkBean.getModuleItemSeqNumber());
//                                                     break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                    // Case#4379 end
//                                    // Code modified for Case#3070 - Ability to modify funding source - starts
//                                    lockedBeans.add(awardSpReviewBean);
////                                    if(awardSpReviewBean != null) {
//                                    if(awardSpReviewBean != null && protocolNumber != null && protocolNumber.length() == 10 && awardSpReviewBean.getAcType()!=null) {
//                                        procedures.add(awardUpdateTxnBean.addUpdAwardSpecialReview(awardSpReviewBean));
////                                        lockedBeans.add(awardSpReviewBean);
//                                    // Code modified for Case#3070 - Ability to modify funding source - ends
//                                        size--;
//
//                                        if(size==0)
//                                            indicator = "N1";
//
//
//                                        updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"1");
//                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
//                                        //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                                        try {
//                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
//                                                    protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE);
//                                        } catch(Exception e) {
//                                        }
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,awardSpReviewBean));
//                                        updateSpecialReviewNotePad(protocolFundingSourceBean);
//                                        Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE,protocolFundingSourceBean.getAcType());
//                                        if(inboxData != null)
//                                            procedures.addAll(inboxData);
//                                    // Added for Case#3070 - Ability to modify funding source - starts
//                                    } else if(awardSpReviewBean != null){
//                                        procedures.add(updateSpecialReviewLink(protocolInfoBean,awardSpReviewBean));
//                                    }
//                                    // Added for Case#3070 - Ability to modify funding source - ends
//
//                                }
//
//                            }
//
//
//                            else {
//                                unLockBeans();
//                                String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
//                                //  String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+protocolFundingSourceBean.getFundingSource();
//                                throw new LockingException(msg);
//                            }
//
//
//                        }
//                    }
//                    if(notepadBeans != null && notepadBeans.size()>0) {
//                        NotepadTxnBean notepadTxnBean = new NotepadTxnBean(userId);
//                        notepadTxnBean.addUpdNotepad(notepadBeans);
//                        notepadBeans = new CoeusVector();
//                    }
//                    //Coeus Enhancement: Case #1799 step 4 end
//                    //Commented with case 4379:Funding removed in an approved amendment still appears in protocol
////                    protocolFundingSourceBean.setProtocolNumber(protocolNumber);
//                    //if (!protocolFundingSourceBean.getAcType().equalsIgnoreCase("D") ) {
////                    procedures.add(addUpdProtocolFundSource(protocolFundingSourceBean));
//                    //}
                }
            }
        }
         //Added with case 4379:Funding removed in an approved amendment still appears in protocol - Start
        // inserting new funding sources
        if(protocolDetailChangeFlags.getIsFundingSourceChanged()){
            vecFundSource = sequenceLogic.processDetails(vecFundSource);
        }
        if ((vecFundSource != null) && (vecFundSource.size() >0)){
            for(int index=0;index<vecFundSource.size();index++){
                ProtocolFundingSourceBean protocolFundingSourceBean =
                        (ProtocolFundingSourceBean)vecFundSource.elementAt(index);
                if (protocolFundingSourceBean.getAcType() != null) {
                    protocolFundingSourceBean.setProtocolNumber(protocolNumber);
                    procedures.add(addUpdProtocolFundSource(protocolFundingSourceBean));
                }
            }
        }
        //Case 4379:Funding removed in an approved amendment still appears in protocol - End

        // inserting new special review
        Vector vecSpecialReview = protocolInfoBean.getSpecialReviews();
        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsSpecialRevChanged()){
            vecSpecialReview = sequenceLogic.processDetails(vecSpecialReview);
        }
        //Seq No. Enhamcements - End
        if ((vecSpecialReview != null) && (vecSpecialReview.size() >0)){
            int length = vecSpecialReview.size();
            for(int index=0;index<length;index++){
                ProtocolSpecialReviewFormBean protocolSpecialReviewFormBean =
                        (ProtocolSpecialReviewFormBean)vecSpecialReview.elementAt(index);
                        /*if  (protocolSpecialReviewFormBean.getAcType() == null ||
                            protocolSpecialReviewFormBean.getAcType().equals("U")) {
                            protocolSpecialReviewFormBean.setAcType("I");
                        }*/
                if  (protocolSpecialReviewFormBean.getAcType() != null) {
                    //protocolSpecialReviewFormBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolSpecialReviewFormBean.getAcType().equals("I")) {
                                protocolSpecialReviewFormBean.setProtocolNumber(
                                protocolNumber);
                                protocolSpecialReviewFormBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolSpecialReviewFormBean.setProtocolNumber(protocolNumber);
                    //if (!protocolSpecialReviewFormBean.getAcType().equalsIgnoreCase("D") ) {
                    procedures.add(addUpdProtocolSpecialReview(protocolSpecialReviewFormBean));
                    //}
                }
            }
        }
        // to insert protocol user roles
        ProtocolRolesFormBean protocolRolesFormBean = new ProtocolRolesFormBean();
        Vector vUserRoles = null;
        Vector vecUserRoles = protocolInfoBean.getUserRoles();

        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsRolesChanged()){
            vecUserRoles = sequenceLogic.processDetails(vecUserRoles);
        }
        //Seq No. Enhamcements - End

               /* when protocol is generated the user is not allowed to enter the user role
                * for the protocol
                */
        if ((vecUserRoles != null) && (vecUserRoles.size() >0)){
            int length = vecUserRoles.size();
            for(int index=0;index<length;index++){
                protocolRolesFormBean =
                        (ProtocolRolesFormBean)vecUserRoles.elementAt(index);
                        /*if  (protocolRolesFormBean.getAcType() == null ||
                            protocolRolesFormBean.getAcType().equals("U")) {
                            protocolRolesFormBean.setAcType("I");
                        }*/
                if  (protocolRolesFormBean.getAcType() != null) {
                    //protocolRolesFormBean.setUpdateTimestamp(dbTimestamp);
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolRolesFormBean.getAcType().equals("I")) {
                                protocolRolesFormBean.setProtocolNumber(
                                protocolNumber);
                                protocolRolesFormBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolRolesFormBean.setProtocolNumber(protocolNumber);
                    //if (!protocolRolesFormBean.getAcType().equalsIgnoreCase("D") ) {
                    procedures.add(addProtocolRoles(protocolRolesFormBean));
                    //}
                }
            }
        //null check added
//        }else if (seqNumber==1 && protocolInfoBean.getAcType().equals("I")){
        }else if (seqNumber==1 && protocolInfoBean.getAcType()!=null &&
                protocolInfoBean.getAcType().equals("I")){
            vUserRoles = new Vector();
            protocolRolesFormBean.setProtocolNumber(protocolNumber);
            protocolRolesFormBean.setSequenceNumber(seqNumber);
            protocolRolesFormBean.setRoleId(CoeusConstants.PROTOCOL_COORDINATOR_ID);
            protocolRolesFormBean.setUserId(userId);
            protocolRolesFormBean.setAcType("I");
            //protocolRolesFormBean.setUpdateTimestamp(dbTimestamp);
            procedures.add(addProtocolRoles(protocolRolesFormBean));
        }

        // inserting new References
        Vector vecReferences = protocolInfoBean.getReferences();
        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsReferencesChanged()){
            vecReferences = sequenceLogic.processDetails(vecReferences);
        }
        //Seq No. Enhamcements - End
        if ((vecReferences != null) && (vecReferences.size() >0)){
            int length = vecReferences.size();
            for(int index=0;index<length;index++){
                ProtocolReferencesBean protocolReferencesBean =
                        (ProtocolReferencesBean)vecReferences.elementAt(index);
                if  (protocolReferencesBean.getAcType() != null) {
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */

                            /*if (protocolReferencesBean.getAcType().equals("I")) {
                                protocolReferencesBean.setProtocolNumber(
                                protocolNumber);
                                protocolReferencesBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolReferencesBean.setProtocolNumber(protocolNumber);
                    procedures.add(addUpdProtocolReferences(protocolReferencesBean));
                }
            }
        }

        //code added for coeus4.3 enhancements - start
        // inserting Amendments Renewal Data
        boolean insertCustomData = true;
        Vector vecAmendRenewals = protocolInfoBean.getAmendmentRenewal();
        if ((vecAmendRenewals != null) && (vecAmendRenewals.size() >0)){
            int length = vecAmendRenewals.size();
            for(int index=0;index<length;index++){
                ProtocolAmendRenewalBean protocolAmendRenewalBean =
                        (ProtocolAmendRenewalBean)vecAmendRenewals.elementAt(index);
                if  (protocolAmendRenewalBean.getAcType() != null) {
                    protocolAmendRenewalBean.setProtocolAmendRenewalNumber(protocolInfoBean.getProtocolNumber());
                    procedures.add(addUpdProtocolAmendRenewals(protocolAmendRenewalBean));
                    Vector vecModuleData = protocolAmendRenewalBean.getVecModuleData();
                    if(vecModuleData!=null && vecModuleData.size()>0){
                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                        //get editable modules for paticular protocolnumber
                        Vector vecEditableModules =
                                protocolDataTxnBean.getAmendRenewEditableData(protocolInfoBean.getProtocolNumber());
                        HashMap hmEditableModele = getFormatedModuleData(vecEditableModules);
                        HashMap hmModule =(HashMap) protocolDataTxnBean.getAmendRenewEditableModules().get(0);
                        for(int count=0 ; count<vecModuleData.size() ; count++){
                            ProtocolModuleBean protocolModuleBean = (ProtocolModuleBean)vecModuleData.get(count);
                            if(protocolModuleBean!=null){
                                //check this module is already having lock.
                                if(!hmEditableModele.containsKey(protocolModuleBean.getProtocolModuleCode())
                                        || protocolModuleBean.getAcType().equals("D")){
                                    protocolModuleBean.setProtocolAmendRenewalNumber(protocolInfoBean.getProtocolNumber());
                                    procedures.add(addUpdAmendRenewModules(protocolModuleBean));
                                    //Syncing the current protocol module with original protocol module data
                                    if(protocolModuleBean.getAcType().equals("D")){
                                        procedures.add(addUpdAmendRenewSyncData(protocolModuleBean));
                                    }
                                } else {
                                    //if module is already locked then module name is noted to throw error message.
//                                    if(protocolModuleBean.getProtocolModuleCode().equals(IrbWindowConstants.OTHERS)){
//                                        insertCustomData = false;
//                                    }
                                    if(moduleName.length()>0){
                                        this.moduleName += ", "+(String) hmModule.get(protocolModuleBean.getProtocolModuleCode());
                                    } else {
                                        this.moduleName += (String) hmModule.get(protocolModuleBean.getProtocolModuleCode());
                                    }
                                }
                            }
                        }
                    }
                    Vector vecQuestionnaireData = protocolAmendRenewalBean.getVecSelectedOrigProtoQnr();
                    if(vecQuestionnaireData!=null && !vecQuestionnaireData.isEmpty()){
                        ProtocolModuleBean protocolModuleBean;
                        for(Object obj : vecQuestionnaireData){
                            protocolModuleBean = (ProtocolModuleBean)obj;
                            if(protocolModuleBean!=null && protocolModuleBean.getAcType() != null){
                                    protocolModuleBean.setProtocolAmendRenewalNumber(protocolInfoBean.getProtocolNumber());
                                    protocolModuleBean.setProtocolAmendRenewSequenceNumber(protocolInfoBean.getSequenceNumber());
                                    procedures.add(addUpdAmendRenewQuestionnaires(protocolModuleBean));
                                    procedures.add(updateQuestionnaireInfo(protocolModuleBean));
                            }
                        }
                    }
                    // CoeusQA2313: Completion of Questionnaire for Submission - End
                }
            }
        }
        //code added for coeus4.3 enhancements - ends

        // inserting new Other Details/Custom elements
        Vector vecCustomElements = protocolInfoBean.getCustomElements();
        //Seq No. Enhamcements - start
        //Modifiied By  : Prasanna Kumar
        if(protocolDetailChangeFlags.getIsCustomDataChanged()){
            vecCustomElements = sequenceLogic.processDetails(vecCustomElements);
        }
        //Seq No. Enhamcements - End
        //code modified for coeus4.3 enhancements
//        if ((vecCustomElements != null) && (vecCustomElements.size() >0)){
        if ((vecCustomElements != null) && (vecCustomElements.size() >0) && insertCustomData){
            int length = vecCustomElements.size();
            for(int index=0;index<length;index++){
                ProtocolCustomElementsInfoBean protocolCustomElementsInfoBean =
                        (ProtocolCustomElementsInfoBean)vecCustomElements.elementAt(index);
                if  (protocolCustomElementsInfoBean.getAcType() != null) {
                            /*
                             * Following code has been commented to implement Sequence Number Enhancements
                             * Commented By : Prasanna Kumar
                             *
                             */
                            /*if (protocolCustomElementsInfoBean.getAcType().equals("I")) {
                                protocolCustomElementsInfoBean.setProtocolNumber(
                                protocolNumber);
                                protocolCustomElementsInfoBean.setSequenceNumber(seqNumber);
                            }*/
                    protocolCustomElementsInfoBean.setProtocolNumber(protocolNumber);
                    procedures.add(addUpdProtocolOtherDetails(protocolCustomElementsInfoBean));
                }
            }
        }

        // inserting new Protocol Related Projects
        Vector vecProtocolProjects = protocolInfoBean.getRelatedProjects();
        if(protocolDetailChangeFlags.getIsRelatedProjectsChanged()){
            vecProtocolProjects = sequenceLogic.processDetails(vecProtocolProjects);
        }
        if ((vecProtocolProjects != null) && (vecProtocolProjects.size() >0)){
            int length = vecProtocolProjects.size();
            for(int index=0;index<length;index++){
                ProtocolRelatedProjectsBean protocolRelatedProjectsBean =
                        (ProtocolRelatedProjectsBean)vecProtocolProjects.elementAt(index);
                if  (protocolRelatedProjectsBean.getAcType() != null) {
                    protocolRelatedProjectsBean.setProtocolNumber(protocolNumber);
                    procedures.add(addUpdProtocolRelatedProjects(protocolRelatedProjectsBean));
                }
            }
        }

        //if(this.functionType == SAVE_NEW_AMENDMENT ||
        //         this.functionType ==  SAVE_NEW_REVISION){
        // code commented for coeus4.3 enhancement - starts
//        Vector vecAmendRenewals = protocolInfoBean.getAmendmentRenewal();
//        if ((vecAmendRenewals != null) && (vecAmendRenewals.size() >0)){
//            int length = vecAmendRenewals.size();
//            for(int index=0;index<length;index++){
//                ProtocolAmendRenewalBean protocolAmendRenewalBean =
//                        (ProtocolAmendRenewalBean)vecAmendRenewals.elementAt(index);
//                if  (protocolAmendRenewalBean.getAcType() != null) {
//                    protocolAmendRenewalBean.setProtocolAmendRenewalNumber(protocolInfoBean.getProtocolNumber());
//                    procedures.add(addUpdProtocolAmendRenewals(protocolAmendRenewalBean));
//                }
//            }
//        }
        // code commented for coeus4.3 enhancement - ends
        //}

        //Protocol Enhancement -  Administrative Correction Start 2
        boolean adminCorrection = false;
        Vector vecProtoActions = protocolInfoBean.getActions();
        String strComments = "";
        if(vecProtoActions != null && vecProtoActions.size()>0){
            for(int index = 0; index < vecProtoActions.size(); index++){
                ProtocolActionsBean protocolActionsBean
                        = (ProtocolActionsBean)vecProtoActions.get(index);
                if(protocolActionsBean.getAcType() == null){
                    continue;
                }else{
                    if(protocolActionsBean.getAcType().equals("I") &&
                            protocolActionsBean.getActionId() == 113){
                        strComments = protocolActionsBean.getComments();
                        actionLogging = true;
                        adminCorrection = true;

                    }else if(protocolActionsBean.getAcType().equals("U")){
                        updProtoActionComments(protocolActionsBean);
                    }
                }//end outer else
            }//End for
        }//end outer if
        //Protocol Enhancement -  Administrative Correction End 2

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
            //Code suhana

            unLockBeans();
            //ends
            //prps code starts
            // this code will make sure that there is a entry in the actions table
            // when the protocol gets created
            // this code will make sure that there is an entry created in the protocol actions table when there is initial submission
            if (actionLogging) {
                System.out.println(" *** Instantiating Action Transaction ***") ;
                int actionCode;
                //If new Amendment/Renewal is created
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();

                if( functionType == SAVE_NEW_AMENDMENT) {
                    actionCode = 103;
                    protocolNumber = protocolInfoBean.getProtocolNumber().substring(0,10);
                    protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                    seqNumber = protocolInfoBean.getSequenceNumber();
                }else if( functionType == SAVE_NEW_REVISION ){
                    //If new Renewal is created
                    //Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am-start
                    //checking for Renewal and New Amendment/Renwal
                    if(protocolInfoBean.getProtocolStatusDesc().equalsIgnoreCase("Renewal in Progress")){
                    actionCode = 102;
                    } else{
                        //actionCode for New Amendment/Renewl
                        actionCode = 117;
                    }
                    //Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am-end
                    protocolNumber = protocolInfoBean.getProtocolNumber().substring(0,10);
                    protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                    seqNumber = protocolInfoBean.getSequenceNumber();
                }
                //Protocol Enhancement -  Administrative Correction Start 3
                else if(adminCorrection){
                    actionCode = 113;
                    protocolNumber = protocolInfoBean.getProtocolNumber();
                }
                //Protocol Enhancement -  Administrative Correction End 3

                else{//Normal Protocol
                    actionCode = 100;
                    protocolNumber = protocolInfoBean.getProtocolNumber();
                }

                ActionTransaction actionTxn = new ActionTransaction(actionCode) ; //actioncode =1 => created
                System.out.println(" *** Start logging  ***") ;

                //Protocol Enhancement -  Administrative Correction Start 4
                if(adminCorrection){
                    actionTxn.setStrComments(strComments);
                }
                //Protocol Enhancement -  Administrative Correction End 4

                if ( actionTxn.logStatusChangeToProtocolAction(protocolNumber,
                        seqNumber, null, userId ) != -1 ) {
                    success = true ;
                } else {
                    success = false ;
                }
                System.out.println(" *** End logging ***") ;
            }
            //prps code ends

            //COEUSQA 1457 STARTS
            if(removedPersonIDs.size()>0){
                sendRemovalEmailToPropPersons(protocolNumber,removedPersonIDs,removedPersonRoles,userId);
            }
            //COEUSQA 1457 ENDS
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        success = true;
        return success;


    }

    /**
     *  Method used to update/insert all the details of a Protocol Key person.
     *  <li>To fetch the data, it uses upd_proto_key_person procedure.
     *
     *  @param protocolKeyPersonnelBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolKeyPerson( ProtocolKeyPersonnelBean
            protocolKeyPersonnelBean)  throws DBException{
        Vector paramKeyPerson= new Vector();

        paramKeyPerson.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.getProtocolNumber()));
        paramKeyPerson.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolKeyPersonnelBean.getSequenceNumber()));
        paramKeyPerson.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.getPersonId()));
        paramKeyPerson.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.getPersonName()));
        paramKeyPerson.addElement(new Parameter("PERSON_ROLE",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.getPersonRole()));
        paramKeyPerson.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.isNonEmployeeFlag()? "Y": "N") );

        /*paramKeyPerson.addElement(new Parameter("FACULTY_FLAG",
        DBEngineConstants.TYPE_STRING,
        protocolKeyPersonnelBean.isFacultyFlag()? "Y": "N") );*/

        paramKeyPerson.addElement(new Parameter("AFFILIATION_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+protocolKeyPersonnelBean.getAffiliationTypeCode()));

        paramKeyPerson.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramKeyPerson.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramKeyPerson.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.getProtocolNumber()));
        paramKeyPerson.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolKeyPersonnelBean.getSequenceNumber()));
        paramKeyPerson.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.getPersonId()));
        paramKeyPerson.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramKeyPerson.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolKeyPersonnelBean.getUpdateTimestamp()));
        paramKeyPerson.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolKeyPersonnelBean.getAcType()));

        StringBuffer sqlKeyPerson = new StringBuffer(
                "call upd_proto_key_person(");
        sqlKeyPerson.append(" <<PROTOCOL_NUMBER>> , ");
        sqlKeyPerson.append(" <<SEQUENCE_NUMBER>> , ");
        sqlKeyPerson.append(" <<PERSON_ID>> , ");
        sqlKeyPerson.append(" <<PERSON_NAME>> , ");
        sqlKeyPerson.append(" <<PERSON_ROLE>> , ");
        sqlKeyPerson.append(" <<NON_EMPLOYEE_FLAG>> , ");

        //sqlKeyPerson.append(" <<FACULTY_FLAG>> , ");

        sqlKeyPerson.append(" <<AFFILIATION_TYPE_CODE>>, ");
        sqlKeyPerson.append(" <<UPDATE_USER>> , ");
        sqlKeyPerson.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlKeyPerson.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlKeyPerson.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlKeyPerson.append(" <<AW_PERSON_ID>> , ");
        sqlKeyPerson.append(" <<AW_UPDATE_USER>> , ");
        sqlKeyPerson.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlKeyPerson.append(" <<AC_TYPE>> )");

        ProcReqParameter procKeyPerson  = new ProcReqParameter();
        procKeyPerson.setDSN(DSN);
        procKeyPerson.setParameterInfo(paramKeyPerson);
        procKeyPerson.setSqlCommand(sqlKeyPerson.toString());
        //COEUSQA 1457 STARTS
        String acType=protocolKeyPersonnelBean.getAcType();
        if(acType.equalsIgnoreCase("D")){
            removedPersonIDs.add(protocolKeyPersonnelBean.getPersonId());
            acType="Study Person";
            if(protocolKeyPersonnelBean.getPersonRole()!=null){
                acType=protocolKeyPersonnelBean.getPersonRole();
            }
            removedPersonRoles.add(acType);
        }
        //COEUSQA 1457 ENDS

        return procKeyPerson;
    }

    /**
     *  Method used to update/insert all the details of a Protocol Location.
     *  <li>To fetch the data, it uses upd_proto_location procedure.
     *
     *  @param protocolLocationListBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolLocations( ProtocolLocationListBean
            protocolLocationListBean)  throws DBException{
        Vector paramLocations = new Vector();

        paramLocations.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getProtocolNumber()));
        paramLocations.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolLocationListBean.getSequenceNumber()));
        paramLocations.addElement(new Parameter("ORGANIZATION_ID",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getOrganizationId()));
        paramLocations.addElement(new Parameter("ROLODEX_ID",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getRolodexId() == 0 ? null :
                    ""+protocolLocationListBean.getRolodexId()));
        paramLocations.addElement(new Parameter("ORGANIZATION_TYPE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolLocationListBean.getOrganizationTypeId()));
        paramLocations.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        paramLocations.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramLocations.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getProtocolNumber()));
        paramLocations.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolLocationListBean.getSequenceNumber()));
        paramLocations.addElement(new Parameter("AW_ORGANIZATION_ID",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getOrganizationId()));
        paramLocations.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramLocations.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolLocationListBean.getUpdateTimestamp()));
        paramLocations.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getAcType()));

        StringBuffer sqlLocations = new StringBuffer(
                "call upd_proto_location(");
        sqlLocations.append(" <<PROTOCOL_NUMBER>> , ");
        sqlLocations.append(" <<SEQUENCE_NUMBER>> , ");
        sqlLocations.append(" <<ORGANIZATION_ID>> , ");
        sqlLocations.append(" <<ROLODEX_ID>> , ");
        sqlLocations.append(" <<ORGANIZATION_TYPE_ID>> , ");
        sqlLocations.append(" <<UPDATE_USER>> , ");
        sqlLocations.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlLocations.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlLocations.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlLocations.append(" <<AW_ORGANIZATION_ID>> , ");
        sqlLocations.append(" <<AW_UPDATE_USER>> , ");
        sqlLocations.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlLocations.append(" <<AC_TYPE>> )");

        ProcReqParameter procLocations  = new ProcReqParameter();
        procLocations.setDSN(DSN);
        procLocations.setParameterInfo(paramLocations);
        procLocations.setSqlCommand(sqlLocations.toString());

        return procLocations;
    }

    /**
     *  Method used to update/insert all the details of a Protocol Vulnerable.
     *  <li>To fetch the data, it uses upd_proto_vulnerable_sub procedure.
     *
     *  @param protocolVulnerableSubListsBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolVulnerable(
            ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean)
            throws DBException{
        Vector paramVulnerable = new Vector();

        paramVulnerable.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolVulnerableSubListsBean.getProtocolNumber()));
        paramVulnerable.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getSequenceNumber()));
        paramVulnerable.addElement(new Parameter("VULNERABLE_SUB_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getVulnerableSubjectTypeCode()));

        //Protocol Enhancement - Saving null in db Start 1

        /*paramVulnerable.addElement(new Parameter("SUBJECT_COUNT",
        DBEngineConstants.TYPE_INT,
        ""+protocolVulnerableSubListsBean.getSubjectCount()));*/

        paramVulnerable.addElement(new Parameter("SUBJECT_COUNT",
                DBEngineConstants.TYPE_INTEGER,protocolVulnerableSubListsBean.getSubjectCount()));

        //Protocol Enhancement - Saving null in db End 1

        paramVulnerable.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramVulnerable.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramVulnerable.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolVulnerableSubListsBean.getProtocolNumber()));
        paramVulnerable.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getSequenceNumber()));
        paramVulnerable.addElement(new Parameter("AW_VULNERABLE_SUB_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getVulnerableSubjectTypeCode()));
        paramVulnerable.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramVulnerable.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolVulnerableSubListsBean.getUpdateTimestamp()));
        paramVulnerable.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolVulnerableSubListsBean.getAcType()));

        StringBuffer sqlVulnerable = new StringBuffer(
                "call upd_proto_vulnerable_sub(");
        sqlVulnerable.append(" <<PROTOCOL_NUMBER>> , ");
        sqlVulnerable.append(" <<SEQUENCE_NUMBER>> , ");
        sqlVulnerable.append(" <<VULNERABLE_SUB_TYPE_CODE>> , ");
        sqlVulnerable.append(" <<SUBJECT_COUNT>> , ");
        sqlVulnerable.append(" <<UPDATE_USER>> , ");
        sqlVulnerable.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlVulnerable.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlVulnerable.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlVulnerable.append(" <<AW_VULNERABLE_SUB_TYPE_CODE>> , ");
        sqlVulnerable.append(" <<AW_UPDATE_USER>> , ");
        sqlVulnerable.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlVulnerable.append(" <<AC_TYPE>> )");

        ProcReqParameter procVulnerable  = new ProcReqParameter();
        procVulnerable.setDSN(DSN);
        procVulnerable.setParameterInfo(paramVulnerable);
        procVulnerable.setSqlCommand(sqlVulnerable.toString());

        return procVulnerable;
    }

    /**
     * Method used to update/insert all the details of a Protocol Investigator.
     *  <li>To fetch the data, it uses upd_proto_investigator procedure.
     *
     *  @param protocolInvestigatorsBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolInvestigator(ProtocolInvestigatorsBean
            protocolInvestigatorsBean)  throws DBException{
        Vector paramInvestigator = new Vector();

        paramInvestigator.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getProtocolNumber()));
        paramInvestigator.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolInvestigatorsBean.getSequenceNumber()));
        paramInvestigator.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonId()));
        paramInvestigator.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonName()));
        paramInvestigator.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.isNonEmployeeFlag()? "Y": "N") );
        paramInvestigator.addElement(new Parameter("PRINCIPAL_INVESTIGATOR_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.isPrincipalInvestigatorFlag()? "Y": "N") );

        /*paramInvestigator.addElement(new Parameter("FACULTY_FLAG",
        DBEngineConstants.TYPE_STRING,
        protocolInvestigatorsBean.isFacultyFlag()? "Y": "N") );*/

        paramInvestigator.addElement(new Parameter("AFFILIATION_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolInvestigatorsBean.getAffiliationTypeCode()));
        paramInvestigator.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigator.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramInvestigator.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getProtocolNumber()));
        paramInvestigator.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolInvestigatorsBean.getSequenceNumber()));
        paramInvestigator.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonId()));
        paramInvestigator.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigator.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolInvestigatorsBean.getUpdateTimestamp()));
        paramInvestigator.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getAcType()));

        StringBuffer sqlInvestigators = new StringBuffer(
                "call upd_proto_investigator(");
        sqlInvestigators.append(" <<PROTOCOL_NUMBER>> , ");
        sqlInvestigators.append(" <<SEQUENCE_NUMBER>> , ");
        sqlInvestigators.append(" <<PERSON_ID>> , ");
        sqlInvestigators.append(" <<PERSON_NAME>> , ");
        sqlInvestigators.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlInvestigators.append(" <<PRINCIPAL_INVESTIGATOR_FLAG>> , ");

        //sqlInvestigators.append(" <<FACULTY_FLAG>> , ");

        sqlInvestigators.append(" <<AFFILIATION_TYPE_CODE>> , ");
        sqlInvestigators.append(" <<UPDATE_USER>> , ");
        sqlInvestigators.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInvestigators.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlInvestigators.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlInvestigators.append(" <<AW_PERSON_ID>> , ");
        sqlInvestigators.append(" <<AW_UPDATE_USER>> , ");
        sqlInvestigators.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvestigators.append(" <<AC_TYPE>> )");

        ProcReqParameter procInvestigator  = new ProcReqParameter();
        procInvestigator.setDSN(DSN);
        procInvestigator.setParameterInfo(paramInvestigator);
        procInvestigator.setSqlCommand(sqlInvestigators.toString());
        //COEUSQA 1457 STARTS
        String acType=protocolInvestigatorsBean.getAcType();
        if(acType.equalsIgnoreCase("D")){
            removedPersonIDs.add(protocolInvestigatorsBean.getPersonId());
            removedPersonRoles.add("Investigator");
        }
        //COEUSQA 1457 ENDS
        return procInvestigator;
    }

    /**
     * Method used to update/insert all the details of a Protocol
     * InvestigatorUnits.
     * <li>To fetch the data, it uses upd_proto_investigator_unit procedure.
     *
     *  @param protocolInvestigatorUnitsBean  this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolInvestigatorUnits(
            ProtocolInvestigatorUnitsBean protocolInvestigatorUnitsBean)
            throws DBException{
        Vector paramInvestigatorUnit = new Vector();

        paramInvestigatorUnit.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getProtocolNumber()));
        paramInvestigatorUnit.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolInvestigatorUnitsBean.getSequenceNumber()));
        paramInvestigatorUnit.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getUnitNumber()));
        paramInvestigatorUnit.addElement(new Parameter("LEAD_UNIT_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.isLeadUnitFlag()? "Y": "N") );
        paramInvestigatorUnit.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getPersonId()));
        paramInvestigatorUnit.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigatorUnit.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramInvestigatorUnit.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getProtocolNumber()));
        paramInvestigatorUnit.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolInvestigatorUnitsBean.getSequenceNumber()));
        paramInvestigatorUnit.addElement(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getUnitNumber()));
        paramInvestigatorUnit.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getPersonId()));
        paramInvestigatorUnit.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigatorUnit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolInvestigatorUnitsBean.getUpdateTimestamp()));
        paramInvestigatorUnit.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getAcType()));

        StringBuffer sqlInvestigatorsUnit = new StringBuffer(
                "call upd_proto_investigator_unit(");
        sqlInvestigatorsUnit.append(" <<PROTOCOL_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<SEQUENCE_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<UNIT_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<LEAD_UNIT_FLAG>> , ");
        sqlInvestigatorsUnit.append(" <<PERSON_ID>> , ");
        sqlInvestigatorsUnit.append(" <<UPDATE_USER>> , ");
        sqlInvestigatorsUnit.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInvestigatorsUnit.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_UNIT_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_PERSON_ID>> , ");
        sqlInvestigatorsUnit.append(" <<AW_UPDATE_USER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvestigatorsUnit.append(" <<AC_TYPE>> )");

        ProcReqParameter procInvestigatorUnit  = new ProcReqParameter();
        procInvestigatorUnit.setDSN(DSN);
        procInvestigatorUnit.setParameterInfo(paramInvestigatorUnit);
        procInvestigatorUnit.setSqlCommand(sqlInvestigatorsUnit.toString());

        return procInvestigatorUnit;
    }

    /**
     *  Method used to update/insert all the details of a Protocol Correspondent.
     *  <li>To fetch the data, it uses upd_proto_correspondent procedure.
     *
     *  @param protocolCorrespondentsBean  this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolCorrespondents(
            ProtocolCorrespondentsBean  protocolCorrespondentsBean)
            throws DBException{

        Vector paramCorrespondents = new Vector();

        paramCorrespondents.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.getProtocolNumber()));
        paramCorrespondents.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolCorrespondentsBean.getSequenceNumber()));
        paramCorrespondents.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.getPersonId()));
        paramCorrespondents.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.getPersonName()));
        paramCorrespondents.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.isNonEmployeeFlag()? "Y": "N") );
        paramCorrespondents.addElement(new Parameter("CORRESPONDENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolCorrespondentsBean.getCorrespondentTypeCode()));

        paramCorrespondents.addElement(new Parameter("AW_CORRESPONDENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolCorrespondentsBean.getAwCorrespondentTypeCode()));

        paramCorrespondents.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.getComments()));
        paramCorrespondents.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramCorrespondents.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramCorrespondents.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.getProtocolNumber()));
        paramCorrespondents.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolCorrespondentsBean.getSequenceNumber()));
        // Modified to fix the updation issue - Start
//        paramCorrespondents.addElement(new Parameter("AW_PERSON_ID",
//                DBEngineConstants.TYPE_STRING,
//                protocolCorrespondentsBean.getPersonId()));
        paramCorrespondents.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.getAwPersonId()));
        // Modified to fix the updation issue - End
        paramCorrespondents.addElement(new Parameter(
                "AW_CORRESPONDENT_TYPE_CODE", DBEngineConstants.TYPE_INT,
                ""+protocolCorrespondentsBean.getCorrespondentTypeCode()));
        paramCorrespondents.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramCorrespondents.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolCorrespondentsBean.getUpdateTimestamp()));
        paramCorrespondents.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolCorrespondentsBean.getAcType()));

        StringBuffer sqlCorrespondents = new StringBuffer(
                "call upd_proto_correspondent(");
        sqlCorrespondents.append(" <<PROTOCOL_NUMBER>> , ");
        sqlCorrespondents.append(" <<SEQUENCE_NUMBER>> , ");
        sqlCorrespondents.append(" <<PERSON_ID>> , ");
        sqlCorrespondents.append(" <<PERSON_NAME>> , ");
        sqlCorrespondents.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlCorrespondents.append(" <<CORRESPONDENT_TYPE_CODE>> , ");
        sqlCorrespondents.append(" <<COMMENTS>> , ");
        sqlCorrespondents.append(" <<UPDATE_USER>> , ");
        sqlCorrespondents.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCorrespondents.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlCorrespondents.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlCorrespondents.append(" <<AW_PERSON_ID>> , ");
        sqlCorrespondents.append(" <<AW_CORRESPONDENT_TYPE_CODE>> , ");
        sqlCorrespondents.append(" <<AW_UPDATE_USER>> , ");
        sqlCorrespondents.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCorrespondents.append(" <<AC_TYPE>> )");

        ProcReqParameter procCorrespondents  = new ProcReqParameter();
        procCorrespondents.setDSN(DSN);
        procCorrespondents.setParameterInfo(paramCorrespondents);
        procCorrespondents.setSqlCommand(sqlCorrespondents.toString());

        return procCorrespondents;
    }

    /**
     *  Method used to update/insert all the NOTES of a Protocol.
     *  To update the data, it uses upd_proto_notes procedure.
     *
     *  @param protocolNotepadBean  this bean contains data for insert or update.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolNotepad(
            ProtocolNotepadBean  protocolNotepadBean)
            throws DBException{

        Vector paramNotepad = new Vector();

        paramNotepad.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.getProtocolNumber()));
        paramNotepad.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolNotepadBean.getSequenceNumber()));
        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolNotepadBean.getEntryNumber()));
        paramNotepad.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.getComments()));
        paramNotepad.addElement(new Parameter("RESTRICTED_VIEW",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.isRestrictedFlag()? "Y": "N") );
        paramNotepad.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramNotepad.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolNotepadBean.getUpdateTimestamp()));
        paramNotepad.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.getAcType()));

        StringBuffer sqlNotepad = new StringBuffer(
                "call upd_proto_Notes(");
        sqlNotepad.append(" <<PROTOCOL_NUMBER>> , ");
        sqlNotepad.append(" <<SEQUENCE_NUMBER>> , ");
        sqlNotepad.append(" <<ENTRY_NUMBER>> , ");
        sqlNotepad.append(" <<COMMENTS>> , ");
        sqlNotepad.append(" <<RESTRICTED_VIEW>> , ");
        sqlNotepad.append(" <<UPDATE_USER>> , ");
        sqlNotepad.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNotepad.append(" <<AC_TYPE>> )");

        ProcReqParameter procNotepad  = new ProcReqParameter();
        procNotepad.setDSN(DSN);
        procNotepad.setParameterInfo(paramNotepad);
        procNotepad.setSqlCommand(sqlNotepad.toString());

        return procNotepad;
    }
    /**
     * Method used to update/insert all the details of a Protocol ResearchArea.
     *  <li>To fetch the data, it uses upd_proto_research_area procedure.
     *
     *  @param protocolReasearchAreasBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolResearchArea(
            ProtocolReasearchAreasBean protocolReasearchAreasBean)  throws DBException{
        Vector paramResearchArea = new Vector();

        paramResearchArea.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolReasearchAreasBean.getProtocolNumber()));
        paramResearchArea.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolReasearchAreasBean.getSequenceNumber()));
        paramResearchArea.addElement(new Parameter("RESEARCH_AREA_CODE",
                DBEngineConstants.TYPE_STRING,
                protocolReasearchAreasBean.getResearchAreaCode()));
        paramResearchArea.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramResearchArea.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramResearchArea.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolReasearchAreasBean.getProtocolNumber()));
        paramResearchArea.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolReasearchAreasBean.getSequenceNumber()));
        paramResearchArea.addElement(new Parameter("AW_RESEARCH_AREA_CODE",
                DBEngineConstants.TYPE_STRING,
                protocolReasearchAreasBean.getResearchAreaCode()));
        paramResearchArea.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramResearchArea.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolReasearchAreasBean.getUpdateTimestamp()));
        paramResearchArea.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolReasearchAreasBean.getAcType()));

        StringBuffer sqlReasearchAreas = new StringBuffer(
                "call upd_proto_research_area(");
        sqlReasearchAreas.append(" <<PROTOCOL_NUMBER>> , ");
        sqlReasearchAreas.append(" <<SEQUENCE_NUMBER>> , ");
        sqlReasearchAreas.append(" <<RESEARCH_AREA_CODE>> , ");
        sqlReasearchAreas.append(" <<UPDATE_USER>> , ");
        sqlReasearchAreas.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlReasearchAreas.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlReasearchAreas.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlReasearchAreas.append(" <<AW_RESEARCH_AREA_CODE>> , ");
        sqlReasearchAreas.append(" <<AW_UPDATE_USER>> , ");
        sqlReasearchAreas.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlReasearchAreas.append(" <<AC_TYPE>> )");

        ProcReqParameter procReasearchAreas  = new ProcReqParameter();
        procReasearchAreas.setDSN(DSN);
        procReasearchAreas.setParameterInfo(paramResearchArea);
        procReasearchAreas.setSqlCommand(sqlReasearchAreas.toString());

        return procReasearchAreas;
    }

    /**
     *  Method used to update/insert all the details of a Protocol FundSource.
     *  <li>To fetch the data, it uses upd_proto_funding_source procedure.
     *
     *  @param protocolFundingSourceBean  this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolFundSource(ProtocolFundingSourceBean
            protocolFundingSourceBean)  throws DBException{
        //Added for the Coeus Enhancement case:#1799 star step:1
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        //End Coeus Enhancement case:#1799 step:1
        Vector paramFundSource = new Vector();

        paramFundSource.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getProtocolNumber()));
        paramFundSource.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getSequenceNumber()));
        paramFundSource.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getFundingSourceTypeCode()));

        paramFundSource.addElement(new Parameter("FUNDING_SOURCE",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getFundingSource()));
        paramFundSource.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramFundSource.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramFundSource.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getProtocolNumber()));
        paramFundSource.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getSequenceNumber()));

        paramFundSource.addElement(new Parameter("AW_FUNDING_SOURCE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getAwFundingSourceTypeCode()));

        paramFundSource.addElement(new Parameter("AW_FUNDING_SOURCE",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getFundingSource()));
        paramFundSource.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramFundSource.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolFundingSourceBean.getUpdateTimestamp()));
        paramFundSource.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getAcType()));

        StringBuffer sqlFundSource = new StringBuffer(
                "call upd_proto_funding_source(");
        sqlFundSource.append(" <<PROTOCOL_NUMBER>> , ");
        sqlFundSource.append(" <<SEQUENCE_NUMBER>> , ");
        sqlFundSource.append(" <<FUNDING_SOURCE_TYPE_CODE>> , ");
        sqlFundSource.append(" <<FUNDING_SOURCE>> , ");
        sqlFundSource.append(" <<UPDATE_USER>> , ");
        sqlFundSource.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlFundSource.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlFundSource.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlFundSource.append(" <<AW_FUNDING_SOURCE_TYPE_CODE>> , ");
        sqlFundSource.append(" <<AW_FUNDING_SOURCE>> , ");
        sqlFundSource.append(" <<AW_UPDATE_USER>> , ");
        sqlFundSource.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlFundSource.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqFund  = new ProcReqParameter();
        procReqFund.setDSN(DSN);
        procReqFund.setParameterInfo(paramFundSource);
        procReqFund.setSqlCommand(sqlFundSource.toString());

        return procReqFund;
    }

    /**
     *  Method used to update/insert all the details of a Protocol special review.
     *  <li>To fetch the data, it uses update_protocol_sprev procedure.
     *
     *  @param ProtocolSpecialReviewFormBean  this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolSpecialReview(ProtocolSpecialReviewFormBean
            protocolSpecialReviewFormBean)  throws DBException{
        Vector paramSpecialReview = new Vector();

        paramSpecialReview.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolNumber()));
        paramSpecialReview.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSequenceNumber()));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewCode()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getApprovalCode()));
        paramSpecialReview.addElement(new Parameter("SP_REV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolSPRevNumber()));
        paramSpecialReview.addElement(new Parameter("APPLICATION_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApplicationDate()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApprovalDate()));
        paramSpecialReview.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getComments()));
        paramSpecialReview.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramSpecialReview.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramSpecialReview.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolNumber()));
        paramSpecialReview.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSequenceNumber()));
        paramSpecialReview.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolSpecialReviewFormBean.getUpdateTimestamp()));
        paramSpecialReview.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getAcType()));

        StringBuffer sqlSpecialReview = new StringBuffer(
                "call update_protocol_sprev(");
        sqlSpecialReview.append(" <<PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_CODE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_TYPE_CODE>> , ");
        sqlSpecialReview.append(" <<SP_REV_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<APPLICATION_DATE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_DATE>> , ");
        sqlSpecialReview.append(" <<COMMENTS>> , ");
        sqlSpecialReview.append(" <<UPDATE_USER>> , ");
        sqlSpecialReview.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AC_TYPE>> )");

        ProcReqParameter procSpecialReview  = new ProcReqParameter();
        procSpecialReview.setDSN(DSN);
        procSpecialReview.setParameterInfo(paramSpecialReview);
        procSpecialReview.setSqlCommand(sqlSpecialReview.toString());

        return procSpecialReview;

    }

    /** case Id #  2002
     * This method used check whether the protocolNumber,
     *Funding source type and funding source
     *  if it is valid it will return 1 else -1
     *  <li>To fetch the data, it uses the function FN_CHECK_PROTO_IN_FUND_SOURCE.
     *
     * @return int count a number for the existence of record
     * @param protocolNumberm, fundigSourceType, fundingSource Number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     **/
    public int checkProtocolInFundingSource(String protocolNumber, int fundigSourceType, String fundingSource)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.add(new Parameter("AS_FUNDING_SOURCE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,""+fundigSourceType));
        param.add(new Parameter("AS_FUNDING_SOURCE",
                DBEngineConstants.TYPE_STRING,fundingSource));

        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_CAN_INSERT_PROTO_FUND_SRC(<< AS_PROTOCOL_NUMBER >>,<< AS_FUNDING_SOURCE_TYPE_CODE >>,<< AS_FUNDING_SOURCE >> ) }", param);
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
     *  Method used to update all the details of a Protocol Vulnerable.
     *  <li>To fetch the data, it uses upd_proto_vulnerable_sub procedure.
     *
     *  @param protocolVulnerableSubListsBean this bean contains data for insert.
     *  @return boolean will return true on successful updation
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updProtocolVulnerable(
            ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean)
            throws CoeusException , DBException{
        Vector paramVulnerable = new Vector();
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;

        //Commented for COEUSLITE-->SUBJECTS - start - by nandakumar sn
        //The following lines are commented because this method is invoked only
        //if acType = 'U', so this piece of code is redudant
        //        if (protocolVulnerableSubListsBean.getAcType() != null){
        //            if  (protocolVulnerableSubListsBean.getAcType().equals("I")) {
        //                protocolVulnerableSubListsBean.setAcType("U");
        //            }
        //        }
        //Commented for COEUSLITE-->SUBJECTS - end

        paramVulnerable.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolVulnerableSubListsBean.getProtocolNumber()));
        paramVulnerable.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getSequenceNumber()));
        paramVulnerable.addElement(new Parameter("VULNERABLE_SUB_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getVulnerableSubjectTypeCode()));

        //Protocol Enhancement - Saving null in db Start 2
        /*paramVulnerable.addElement(new Parameter("SUBJECT_COUNT",
        DBEngineConstants.TYPE_INT,
        ""+protocolVulnerableSubListsBean.getSubjectCount()));*/

        paramVulnerable.addElement(new Parameter("SUBJECT_COUNT",
                DBEngineConstants.TYPE_INTEGER,protocolVulnerableSubListsBean.getSubjectCount()));
        //Protocol Enhancement - Saving null in db End 2

        paramVulnerable.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramVulnerable.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramVulnerable.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolVulnerableSubListsBean.getProtocolNumber()));
        paramVulnerable.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getSequenceNumber()));
        paramVulnerable.addElement(new Parameter("AW_VULNERABLE_SUB_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolVulnerableSubListsBean.getVulnerableSubjectTypeCode()));
        paramVulnerable.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramVulnerable.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolVulnerableSubListsBean.getUpdateTimestamp()));
        paramVulnerable.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolVulnerableSubListsBean.getAcType()));

        StringBuffer sqlVulnerable = new StringBuffer(
                "call upd_proto_vulnerable_sub(");
        sqlVulnerable.append(" <<PROTOCOL_NUMBER>> , ");
        sqlVulnerable.append(" <<SEQUENCE_NUMBER>> , ");
        sqlVulnerable.append(" <<VULNERABLE_SUB_TYPE_CODE>> , ");
        sqlVulnerable.append(" <<SUBJECT_COUNT>> , ");
        sqlVulnerable.append(" <<UPDATE_USER>> , ");
        sqlVulnerable.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlVulnerable.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlVulnerable.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlVulnerable.append(" <<AW_VULNERABLE_SUB_TYPE_CODE>> , ");
        sqlVulnerable.append(" <<AW_UPDATE_USER>> , ");
        sqlVulnerable.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlVulnerable.append(" <<AC_TYPE>> )");

        ProcReqParameter procVulnerable  = new ProcReqParameter();
        procVulnerable.setDSN(DSN);
        procVulnerable.setParameterInfo(paramVulnerable);
        procVulnerable.setSqlCommand(sqlVulnerable.toString());

        procedures.add(procVulnerable);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        success = true;
        return success;
    }

    /**
     *  Method used to update all the details of a Protocol user Roles.
     *  <li>To fetch the data, it uses update_protocol_user_roles procedure.
     *
     *  @param ProtocolRolesFormBean this bean contains data for insert.
     *  @return ProcReqParameter will return .
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addProtocolRoles(ProtocolRolesFormBean protocolRolesFormBean)
    throws CoeusException , DBException{
        Vector paramRoles = new Vector();
//        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = new CoeusFunctions().getDBTimestamp();

        paramRoles.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getProtocolNumber()));
        paramRoles.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getSequenceNumber()));
        paramRoles.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getRoleId()));
        paramRoles.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getUserId()));
        paramRoles.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRoles.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getProtocolNumber()));
        paramRoles.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getSequenceNumber()));
        paramRoles.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getRoleId()));
        paramRoles.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getUserId()));
        paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolRolesFormBean.getUpdateTimestamp()));
        paramRoles.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getAcType()));

        StringBuffer sqlRoles = new StringBuffer(
                "call update_protocol_user_roles(");
        sqlRoles.append(" <<PROTOCOL_NUMBER>> , ");
        sqlRoles.append(" <<SEQUENCE_NUMBER>> , ");
        sqlRoles.append(" <<USER_ID>> , ");
        sqlRoles.append(" <<ROLE_ID>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlRoles.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlRoles.append(" <<AW_USER_ID>> , ");
        sqlRoles.append(" <<AW_ROLE_ID>> , ");
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AC_TYPE>> )");

        ProcReqParameter procRoles  = new ProcReqParameter();
        procRoles.setDSN(DSN);
        procRoles.setParameterInfo(paramRoles);
        procRoles.setSqlCommand(sqlRoles.toString());

        return procRoles;
    }



    public boolean updProtocolRoles(ProtocolRolesFormBean protocolRolesFormBean)
    throws CoeusException , DBException{
        Vector paramRoles = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;

        System.out.println("User Id  "+protocolRolesFormBean.getUserId());
        System.out.println("Role Id "+protocolRolesFormBean.getRoleId());
        System.out.println("Timestamp "+protocolRolesFormBean.getUpdateTimestamp());

        paramRoles.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getProtocolNumber()));
        paramRoles.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getSequenceNumber()));
        paramRoles.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getRoleId()));
        paramRoles.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getUserId()));
        paramRoles.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRoles.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getProtocolNumber()));
        paramRoles.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getSequenceNumber()));
        paramRoles.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getRoleId()));
        paramRoles.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getUserId()));
        paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolRolesFormBean.getUpdateTimestamp()));
        paramRoles.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getAcType()));

        StringBuffer sqlRoles = new StringBuffer(
                "call update_protocol_user_roles(");
        sqlRoles.append(" <<PROTOCOL_NUMBER>> , ");
        sqlRoles.append(" <<SEQUENCE_NUMBER>> , ");
        sqlRoles.append(" <<USER_ID>> , ");
        sqlRoles.append(" <<ROLE_ID>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlRoles.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlRoles.append(" <<AW_USER_ID>> , ");
        sqlRoles.append(" <<AW_ROLE_ID>> , ");
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AC_TYPE>> )");

        ProcReqParameter procRoles  = new ProcReqParameter();
        procRoles.setDSN(DSN);
        procRoles.setParameterInfo(paramRoles);
        procRoles.setSqlCommand(sqlRoles.toString());

        procedures.add(procRoles);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }





    /**
     *  The method used to release the lock of a particular schedule
     *  @param rowId the id for lock to be released
     */
    public void releaseEdit(String rowId){
         /*if(rowId.length()>10){
             rowId = rowId.substring(0,11);
         }*/
        transMon.releaseEdit(this.rowLockStr+rowId);
    }

    // Code added by Shivakumar for locking enhancement - BEGIN
    //     public void releaseEdit(String rowId, String userId) throws
    //         CoeusException, DBException{
    //        transMon.releaseEdit(this.rowLockStr+rowId,userId);
    //
    //     }
    // Calling releaseLock method for bug fixing
    public LockingBean releaseLock(String rowId, String userId) throws
            CoeusException, DBException{
        LockingBean lockingBean = transMon.releaseLock(this.rowLockStr+rowId,userId);
        return lockingBean;
    }

    //for releasing lock in routing
        public LockingBean releaseRoutingLock(String rowId, String loggedinUser) throws
        CoeusException, DBException{
        LockingBean lockingBean = new LockingBean();
         transMon = TransactionMonitor.getInstance();
       lockingBean = transMon.releaseLock(this.routingLockStr+rowId,loggedinUser);
        return lockingBean;
    }
    // Code added by Shivakumar for locking enhancement - END
    /**
     *  Method used to update/insert all the details of a Protocol References.
     *  <li>To fetch the data, it uses UPD_PROTOCOL_REFERENCE procedure.
     *
     *  @param ProtocolSpecialReviewFormBean  this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolReferences(ProtocolReferencesBean
            protocolReferencesBean)  throws CoeusException, DBException{
        Vector paramReferences = new Vector();

        paramReferences.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolReferencesBean.getProtocolNumber()));
        paramReferences.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolReferencesBean.getSequenceNumber()));
        paramReferences.addElement(new Parameter("PROTOCOL_REFERENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolReferencesBean.getReferenceNumber()));
        paramReferences.addElement(new Parameter("PROTOCOL_REFERENCE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolReferencesBean.getReferenceTypeCode()));
        paramReferences.addElement(new Parameter("REFERENCE_KEY",
                DBEngineConstants.TYPE_STRING,
                protocolReferencesBean.getReferenceKey()));
        paramReferences.addElement(new Parameter("APPLICATION_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolReferencesBean.getApplicationDate()));
        paramReferences.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolReferencesBean.getApprovalDate()));
        paramReferences.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolReferencesBean.getComments()));
        paramReferences.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramReferences.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramReferences.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolReferencesBean.getProtocolNumber()));
        paramReferences.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolReferencesBean.getSequenceNumber()));
        paramReferences.addElement(new Parameter("AW_PROTOCOL_REFERENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolReferencesBean.getReferenceNumber()));
        paramReferences.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolReferencesBean.getUpdateTimestamp()));
        paramReferences.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolReferencesBean.getAcType()));

        StringBuffer sqlReferences = new StringBuffer(
                "call UPD_PROTOCOL_REFERENCE(");
        sqlReferences.append(" <<PROTOCOL_NUMBER>> , ");
        sqlReferences.append(" <<SEQUENCE_NUMBER>> , ");
        sqlReferences.append(" <<PROTOCOL_REFERENCE_NUMBER>> , ");
        sqlReferences.append(" <<PROTOCOL_REFERENCE_TYPE_CODE>> , ");
        sqlReferences.append(" <<REFERENCE_KEY>> , ");
        sqlReferences.append(" <<APPLICATION_DATE>> , ");
        sqlReferences.append(" <<APPROVAL_DATE>> , ");
        sqlReferences.append(" <<COMMENTS>> , ");
        sqlReferences.append(" <<UPDATE_USER>> , ");
        sqlReferences.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlReferences.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlReferences.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlReferences.append(" <<AW_PROTOCOL_REFERENCE_NUMBER>> , ");
        sqlReferences.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlReferences.append(" <<AC_TYPE>> )");

        ProcReqParameter procReferences = new ProcReqParameter();
        procReferences.setDSN(DSN);
        procReferences.setParameterInfo(paramReferences);
        procReferences.setSqlCommand(sqlReferences.toString());

        return procReferences;
    }

    /**
     *  Method used to update/insert all the details of a Protocol others
     *  details.
     *
     *  @param ProtocolCustomElementsInfoBean details of others details.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdProtocolOtherDetails( ProtocolCustomElementsInfoBean
            protocolCustomElementsInfoBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramProposalOther= new Vector();

        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramProposalOther.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolCustomElementsInfoBean.getProtocolNumber()));
        paramProposalOther.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolCustomElementsInfoBean.getSequenceNumber()));
        paramProposalOther.addElement(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,
                protocolCustomElementsInfoBean.getColumnName()));
        paramProposalOther.addElement(new Parameter("COLUMN_VALUE",
                DBEngineConstants.TYPE_STRING,
                protocolCustomElementsInfoBean.getColumnValue()));
        paramProposalOther.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProposalOther.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramProposalOther.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolCustomElementsInfoBean.getProtocolNumber()));
        paramProposalOther.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolCustomElementsInfoBean.getSequenceNumber()));
        paramProposalOther.addElement(new Parameter("AW_COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,
                protocolCustomElementsInfoBean.getColumnName()));
        paramProposalOther.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolCustomElementsInfoBean.getUpdateTimestamp()));
        paramProposalOther.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolCustomElementsInfoBean.getAcType()));

        StringBuffer sqlProposalOther = new StringBuffer(
                "call UPD_PROTOCOL_CUSTOM_DATA(");
        sqlProposalOther.append(" <<PROTOCOL_NUMBER>> , ");
        sqlProposalOther.append(" <<SEQUENCE_NUMBER>> , ");
        sqlProposalOther.append(" <<COLUMN_NAME>> , ");
        sqlProposalOther.append(" <<COLUMN_VALUE>> , ");
        sqlProposalOther.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProposalOther.append(" <<UPDATE_USER>> , ");
        sqlProposalOther.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlProposalOther.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlProposalOther.append(" <<AW_COLUMN_NAME>> , ");
        sqlProposalOther.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProposalOther.append(" <<AC_TYPE>> )");

        ProcReqParameter procProposalOther  = new ProcReqParameter();
        procProposalOther.setDSN(DSN);
        procProposalOther.setParameterInfo(paramProposalOther);
        procProposalOther.setSqlCommand(sqlProposalOther.toString());

        return procProposalOther;
        /*procedures.add(procProposalOther);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
    }

    /*
     *  This method is used to check whether new sequence number
     *  has to generated for this Protocol.
     *  @param protocolNumber Protocol Number.
     *  @return boolean indicating whether new sequence number has to be generated.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean generateSequence(
            String protocolNumber)
            throws CoeusException , DBException{
        Vector param= new Vector();

        HashMap canGenerate = null;
        Vector result = new Vector();
        int intIsGenerate = 0;
        param= new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        try {
            if(dbEngine!=null){
                result = dbEngine.executeFunctions(DSN,
                        "{<<OUT INTEGER SUCCESS>> = call FN_IS_GENERATE_NEW_SEQUENCE( "
                        + " << AS_PROTOCOL_NUMBER >> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()){
                canGenerate = (HashMap)result.elementAt(0);
                intIsGenerate = Integer.parseInt(canGenerate.get("SUCCESS").toString());
            }
        }catch(Exception ex) {
            ex.printStackTrace() ;
        }
        if(intIsGenerate==1){
            return true;
        }else{
            return false;
        }
    }

    public boolean isProtocolChanged(ProtocolDetailChangeFlags protocolDetailChangeFlags){
        if(protocolDetailChangeFlags.getIsProtocolMaintChanged() ||
                protocolDetailChangeFlags.getIsLocationChanged() ||
                protocolDetailChangeFlags.getIsInvestigatorChanged() ||
                protocolDetailChangeFlags.getIsKeyStudyChanged() ||
                protocolDetailChangeFlags.getIsCorrespondentsChanged() ||
                protocolDetailChangeFlags.getIsAORChanged() ||
                protocolDetailChangeFlags.getIsFundingSourceChanged() ||
                protocolDetailChangeFlags.getIsSubjectsChanged() ||
                protocolDetailChangeFlags.getIsSpecialRevChanged() ||
                protocolDetailChangeFlags.getIsNotesChanged() ||
                protocolDetailChangeFlags.getIsCustomDataChanged() ||
                protocolDetailChangeFlags.getIsRolesChanged() ||
                protocolDetailChangeFlags.getIsReferencesChanged() ||
                protocolDetailChangeFlags.getIsRelatedProjectsChanged()){
            return true;
        }else{
            return false;
        }
    }

    /**
     *  Method used to update/insert all the details of a Protocol Related Projects.
     *
     *  @param ProtocolRelatedProjectsBean details of Project details.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdProtocolRelatedProjects( ProtocolRelatedProjectsBean
            protocolRelatedProjectsBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramProjects= new Vector();

        paramProjects.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRelatedProjectsBean.getProtocolNumber()));
        paramProjects.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolRelatedProjectsBean.getSequenceNumber()));
        paramProjects.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolRelatedProjectsBean.getModuleCode()));
        paramProjects.addElement(new Parameter("PROJECT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRelatedProjectsBean.getProjectNumber()));
        paramProjects.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramProjects.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProjects.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRelatedProjectsBean.getProtocolNumber()));
        paramProjects.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolRelatedProjectsBean.getSequenceNumber()));
        paramProjects.addElement(new Parameter("AW_MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolRelatedProjectsBean.getAwModuleCode()));
        paramProjects.addElement(new Parameter("AW_PROJECT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRelatedProjectsBean.getAwProjectNumber()));
        paramProjects.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, protocolRelatedProjectsBean.getUpdateUser()));
        paramProjects.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolRelatedProjectsBean.getUpdateTimestamp()));
        paramProjects.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolRelatedProjectsBean.getAcType()));

        StringBuffer sqlProtocolProjects = new StringBuffer(
                "call UPD_PROTO_RELATED_PROJECTS(");
        sqlProtocolProjects.append(" <<PROTOCOL_NUMBER>> , ");
        sqlProtocolProjects.append(" <<SEQUENCE_NUMBER>> , ");
        sqlProtocolProjects.append(" <<MODULE_CODE>> , ");
        sqlProtocolProjects.append(" <<PROJECT_NUMBER>> , ");
        sqlProtocolProjects.append(" <<UPDATE_USER>> , ");
        sqlProtocolProjects.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProtocolProjects.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlProtocolProjects.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlProtocolProjects.append(" <<AW_MODULE_CODE>> , ");
        sqlProtocolProjects.append(" <<AW_PROJECT_NUMBER>> , ");
        sqlProtocolProjects.append(" <<AW_UPDATE_USER>> , ");
        sqlProtocolProjects.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProtocolProjects.append(" <<AC_TYPE>> )");

        ProcReqParameter procProtocolProjects  = new ProcReqParameter();
        procProtocolProjects.setDSN(DSN);
        procProtocolProjects.setParameterInfo(paramProjects);
        procProtocolProjects.setSqlCommand(sqlProtocolProjects.toString());

        return procProtocolProjects;

        /*procedures.add(procProtocolProjects);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
    }



    /**
     *  Method used to update/insert Amendments/Renewals when new Amendments/Renewals are created.
     *
     *  @param protocolInfoBean ProtocolInfoBean
     *  @return ProcReqParameter ProcReqParameter
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdProtocolAmendRenewals(ProtocolAmendRenewalBean protocolAmendRenewalBean)
    throws CoeusException,DBException{
//        boolean success = false;
        /*Vector procedures = new Vector(5,3);

        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        */

        Vector param = new Vector();

        param.addElement(new Parameter("PROTO_AMEND_REN_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolAmendRenewalBean.getProtocolAmendRenewalNumber()));
        param.addElement(new Parameter("DATE_CREATED",
                DBEngineConstants.TYPE_DATE,
                new Date(dbTimestamp.getTime())));
        param.addElement(new Parameter("SUMMARY",
                DBEngineConstants.TYPE_STRING,
                protocolAmendRenewalBean.getSummary()));
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolAmendRenewalBean.getProtocolNumber()));
        //Code modified for coeus4.3 enhancement
        //To avoid storing 0 to the DB for the sequence number
//        param.addElement(new Parameter("SEQUENCE_NUMBER",
//                DBEngineConstants.TYPE_INT,
//                ""+protocolAmendRenewalBean.getSequenceNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_STRING,
                (protocolAmendRenewalBean.getSequenceNumber() == 0) ? "" :
                    protocolAmendRenewalBean.getSequenceNumber()+""));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AW_PROTO_AMEND_REN_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolAmendRenewalBean.getProtocolAmendRenewalNumber()));
        param.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                protocolAmendRenewalBean.getUpdateUser()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolAmendRenewalBean.getUpdateTimestamp()));
        //AcType is hard coded as "I"
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolAmendRenewalBean.getAcType()));

        StringBuffer sqlProtoAmendRen = new StringBuffer(
                "call UPD_PROTO_AMENDMENT_RENEWAL(");
        sqlProtoAmendRen.append(" <<PROTO_AMEND_REN_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<DATE_CREATED>> , ");
        sqlProtoAmendRen.append(" <<SUMMARY>> , ");
        sqlProtoAmendRen.append(" <<PROTOCOL_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<SEQUENCE_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_USER>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProtoAmendRen.append(" <<AW_PROTO_AMEND_REN_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<AW_UPDATE_USER>> , ");
        sqlProtoAmendRen.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProtoAmendRen.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlProtoAmendRen.toString());

        return procReqParameter;

        /*procedures.add(procProtocolProjects);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
    }

    /* Code added by Shivakumar for saving protocol notes data only - 10/11/2004
     *
     */


    /** This method has been written to save protocol notepad data only
     * @param protocolNotepadBean is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean
     */
    public boolean updProtocolNotepad(ProtocolNotepadBean  protocolNotepadBean)
    throws CoeusException, DBException{

        Vector paramNotepad = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
//        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramNotepad.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.getProtocolNumber()));
        paramNotepad.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolNotepadBean.getSequenceNumber()));
        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                DBEngineConstants.TYPE_INT,""+protocolNotepadBean.getEntryNumber()));
        paramNotepad.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.getComments()));
        paramNotepad.addElement(new Parameter("RESTRICTED_VIEW",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.isRestrictedFlag()? "Y": "N") );
        paramNotepad.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramNotepad.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolNotepadBean.getUpdateTimestamp()));
        paramNotepad.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolNotepadBean.getAcType()));

        StringBuffer sqlNotepad = new StringBuffer(
                "call upd_proto_Notes(");
        sqlNotepad.append(" <<PROTOCOL_NUMBER>> , ");
        sqlNotepad.append(" <<SEQUENCE_NUMBER>> , ");
        sqlNotepad.append(" <<ENTRY_NUMBER>> , ");
        sqlNotepad.append(" <<COMMENTS>> , ");
        sqlNotepad.append(" <<RESTRICTED_VIEW>> , ");
        sqlNotepad.append(" <<UPDATE_USER>> , ");
        sqlNotepad.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNotepad.append(" <<AC_TYPE>> )");

        ProcReqParameter procNotepad  = new ProcReqParameter();
        procNotepad.setDSN(DSN);
        procNotepad.setParameterInfo(paramNotepad);
        procNotepad.setSqlCommand(sqlNotepad.toString());

        procedures.add(procNotepad);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;


    }

    //Protocol Enhancement -  Administrative Correction Start 5
    /** This method has been written to save Protocol Actions data only
     * @param protocolActionsBean is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is
     */
    public void updProtoActionComments(ProtocolActionsBean protocolActionsBean)
    throws CoeusException, DBException{

        Vector parmAction = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);

        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();


        parmAction.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolActionsBean.getProtocolNumber()));

        parmAction.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getSequenceNumber()));

        parmAction.addElement(new Parameter("ACTION_ID",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getActionId()));

        parmAction.addElement(new Parameter("PROTOCOL_ACTION_TYPE_CODE",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getActionTypeCode()));

        parmAction.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getSubmissionNumber()));

        parmAction.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,protocolActionsBean.getComments()));

        parmAction.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        parmAction.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));

        parmAction.addElement(new Parameter("ACTION_DATE",
                DBEngineConstants.TYPE_DATE,protocolActionsBean.getActionDate()));

        StringBuffer sqlProtoAction = new StringBuffer(
                "call UPD_PROTOCOL_ACTION_COMMENTS(");
        sqlProtoAction.append(" <<PROTOCOL_NUMBER>> , ");
        sqlProtoAction.append(" <<SEQUENCE_NUMBER>> , ");
        sqlProtoAction.append(" <<ACTION_ID>> , ");
        sqlProtoAction.append(" <<PROTOCOL_ACTION_TYPE_CODE>> , ");
        sqlProtoAction.append(" <<SUBMISSION_NUMBER>> , ");
        sqlProtoAction.append(" <<COMMENTS>> , ");
        sqlProtoAction.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProtoAction.append(" <<UPDATE_USER>> , ");
        sqlProtoAction.append(" <<ACTION_DATE>> )");

        ProcReqParameter procAction = new ProcReqParameter();
        procAction.setDSN(DSN);
        procAction.setParameterInfo(parmAction);
        procAction.setSqlCommand(sqlProtoAction.toString());

        procedures.add(procAction);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    //Protocol Enhancement -  Administrative Correction End 5

    /**
     * Getter for property dbTimestamp.
     * @return Value of property dbTimestamp.
     */
    public java.sql.Timestamp getDbTimestamp() {
        return dbTimestamp;
    }

    /**
     * Setter for property dbTimestamp.
     * @param dbTimestamp New value of property dbTimestamp.
     */
    public void setDbTimestamp(java.sql.Timestamp dbTimestamp) {
        this.dbTimestamp = dbTimestamp;
    }

    /**
     *  Method used to update/insert/delete Upload Document for IRB WEB.
     *
     *  @param CoeusVector uploadDocumentBean
     *  @return ProcReqParameter ProcReqParameter
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdUploadDocument(UploadDocumentBean uploadDocumentBean)
    throws CoeusException,DBException{

//        Vector procedures = new Vector(5,3);
        Vector param = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,uploadDocumentBean.getProtocolNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getSequenceNumber())));
        param.addElement(new Parameter("DOCUMENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getDocCode())));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,uploadDocumentBean.getDescription()));
        param.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING,uploadDocumentBean.getFileName()));

        if(uploadDocumentBean.getDocument() != null){
            byte data[] = uploadDocumentBean.getDocument();
            param.addElement(new Parameter("DOCUMENT",
                    DBEngineConstants.TYPE_BLOB, data ));
        }

        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        //       param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
        //                   DBEngineConstants.TYPE_STRING,uploadDocumentBean.getAw_protocolNumber()));
        //       param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
        //                   DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_sequenceNumber())));


        param.addElement(new Parameter("DOCUMENT_STATUS_CODE",
                DBEngineConstants.TYPE_INT, new Integer(uploadDocumentBean.getStatusCode())));
        StringBuffer sqlUploadDocument = new StringBuffer("") ;
        if(uploadDocumentBean.getAcType().trim().equals("I")){
            //Commented for Protocol Upload Document enhancement start 1
//            int uploadId = getNextUploadID(uploadDocumentBean.getProtocolNumber() , uploadDocumentBean.getSequenceNumber());
//            param.addElement(new Parameter("DOC_ID",
//            DBEngineConstants.TYPE_INT,new Integer(uploadId)));
            //Commented for Protocol Upload Document enhancement end 1
//            int versionNumber = getNextVersionNumber(uploadDocumentBean.getProtocolNumber() , uploadDocumentBean.getSequenceNumber() , uploadDocumentBean.getDocCode());
//            param.addElement(new Parameter("VERSION_NUMBER",
//            DBEngineConstants.TYPE_INT,new Integer(versionNumber)));
            //Get the next document Id to be set
            if(!uploadDocumentBean.isAmended()){
                int documentId = getNextUploadID(uploadDocumentBean.getProtocolNumber(),
                        uploadDocumentBean.getSequenceNumber());
                uploadDocumentBean.setDocumentId(documentId);
            }

            param.addElement(new Parameter("VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getVersionNumber())));

            param.addElement(new Parameter("DOCUMENT_ID",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getDocumentId())));

            sqlUploadDocument.append("insert into OSP$PROTOCOL_DOCUMENTS(");
            sqlUploadDocument.append(" PROTOCOL_NUMBER , ");
            sqlUploadDocument.append(" SEQUENCE_NUMBER , ");
            //Commented for Protocol Upload Document enhancement start 2
            //sqlUploadDocument.append(" DOC_ID , ");
            //Commented for Protocol Upload Document enhancement end 2
            sqlUploadDocument.append(" DOCUMENT_TYPE_CODE , ");
            sqlUploadDocument.append(" DESCRIPTION , ");
            sqlUploadDocument.append(" FILE_NAME , ");
            sqlUploadDocument.append(" DOCUMENT , ");
            sqlUploadDocument.append(" UPDATE_TIMESTAMP , ");
            sqlUploadDocument.append(" UPDATE_USER , ");
            sqlUploadDocument.append(" VERSION_NUMBER , ");
            sqlUploadDocument.append(" DOCUMENT_STATUS_CODE , ");
            sqlUploadDocument.append(" DOCUMENT_ID ) ");
            sqlUploadDocument.append(" VALUES (");
            sqlUploadDocument.append(" <<PROTOCOL_NUMBER>> , ");
            sqlUploadDocument.append(" <<SEQUENCE_NUMBER>> , ");
            //Commented for Protocol Upload Document enhancement start 3
            //sqlUploadDocument.append(" <<DOC_ID>> , ");
            //Commented for Protocol Upload Document enhancement end 3
            sqlUploadDocument.append(" <<DOCUMENT_TYPE_CODE>> , ");
            sqlUploadDocument.append(" <<DESCRIPTION>> , ");
            sqlUploadDocument.append(" <<FILE_NAME>> , ");
            sqlUploadDocument.append(" <<DOCUMENT>> , ");
            sqlUploadDocument.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlUploadDocument.append(" <<UPDATE_USER>> , ");
            sqlUploadDocument.append(" <<VERSION_NUMBER>> , ");
            sqlUploadDocument.append(" <<DOCUMENT_STATUS_CODE>> , ");
            sqlUploadDocument.append(" <<DOCUMENT_ID>> ) ");
        }else {
            //Commented for Protocol Upload Document enhancement start 4
//            param.addElement(new Parameter("DOC_ID",
//            DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getUploadId())));
            //Commented for Protocol Upload Document enhancement end 4
            //Added for Protocol Upload Document enhancement start 5
            param.addElement(new Parameter("VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getVersionNumber())));
            param.addElement(new Parameter("DOCUMENT_ID",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getDocumentId())));
            param.addElement(new Parameter("AW_VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_versionNumber())));
            param.addElement(new Parameter("AW_DOC_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_docCode())));
            //Added for Protocol Upload Document enhancement end 5
            param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,uploadDocumentBean.getAw_protocolNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_sequenceNumber())));
            //Commented for Protocol Upload Document enhancement start 6
//            param.addElement(new Parameter("AW_DOC_ID",
//            DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_uploadID())));
            //Commented for Protocol Upload Document enhancement end 6
            //Added for Coeus4.3 enhancement start 6.1
            //Introduced document id to identify a document
            param.addElement(new Parameter("AW_DOCUMENT_ID",
                    DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_documentId())));
            //Added for Coeus4.3 enhancement end 6.1
            if(uploadDocumentBean.getAcType().trim().equals("U")){
                sqlUploadDocument.append("update OSP$PROTOCOL_DOCUMENTS set");
                if(uploadDocumentBean.isChangeStatus()){
                    sqlUploadDocument.append(" DOCUMENT_STATUS_CODE = ");
                    sqlUploadDocument.append(" <<DOCUMENT_STATUS_CODE>> ");
                    sqlUploadDocument.append(" where ");
                    sqlUploadDocument.append(" PROTOCOL_NUMBER = ");
                    sqlUploadDocument.append(" <<AW_PROTOCOL_NUMBER>> ");
                    sqlUploadDocument.append(" and SEQUENCE_NUMBER = ");
                    sqlUploadDocument.append(" <<AW_SEQUENCE_NUMBER>> ");
                    sqlUploadDocument.append(" and DOCUMENT_TYPE_CODE = ");
                    sqlUploadDocument.append(" <<AW_DOC_TYPE_CODE>> ");
                    sqlUploadDocument.append(" and VERSION_NUMBER = ");
                    sqlUploadDocument.append(" <<AW_VERSION_NUMBER>> ");
                    //Added for Coeus4.3 enhancement start 6.2
                    //Introduced document id to identify a document
                    sqlUploadDocument.append(" and DOCUMENT_ID = ");
                    sqlUploadDocument.append(" <<AW_DOCUMENT_ID>> ");
                    //Added for Coeus4.3 enhancement end 6.1
                }else{
                    //                sqlUploadDocument.append(" DOCUMENT_TYPE_CODE =  ");
                    //                sqlUploadDocument.append(" <<DOCUMENT_TYPE_CODE>> , ");
                    sqlUploadDocument.append(" DESCRIPTION =  ");
                    sqlUploadDocument.append(" <<DESCRIPTION>> , ");
                    sqlUploadDocument.append(" FILE_NAME =  ");
                    sqlUploadDocument.append(" <<FILE_NAME>> , ");
                    // Protocol Performance issues  beacuse of large Documents - Start
                    if(uploadDocumentBean.getDocument() != null){
                        sqlUploadDocument.append(" DOCUMENT =  ");
                        sqlUploadDocument.append(" <<DOCUMENT>> , ");
                    }
//                    sqlUploadDocument.append(" DOCUMENT =  ");
//                    sqlUploadDocument.append(" <<DOCUMENT>> , ");
                    // Protocol Performance issues  beacuse of large Documents - End
                    sqlUploadDocument.append(" UPDATE_TIMESTAMP =  ");
                    sqlUploadDocument.append(" <<UPDATE_TIMESTAMP>> , ");
                    sqlUploadDocument.append(" UPDATE_USER = ");
                    sqlUploadDocument.append(" <<UPDATE_USER>> ,");
                    sqlUploadDocument.append(" DOCUMENT_STATUS_CODE = ");
                    sqlUploadDocument.append(" <<DOCUMENT_STATUS_CODE>> , ");
                    sqlUploadDocument.append(" DOCUMENT_ID = ");
                    sqlUploadDocument.append(" <<DOCUMENT_ID>> ");
                    sqlUploadDocument.append(" where ");
                    sqlUploadDocument.append(" PROTOCOL_NUMBER = ");
                    sqlUploadDocument.append(" <<AW_PROTOCOL_NUMBER>> ");
                    sqlUploadDocument.append(" and SEQUENCE_NUMBER = ");
                    sqlUploadDocument.append(" <<AW_SEQUENCE_NUMBER>> ");
                    //Commented for Protocol Upload Document enhancement start 7
                    ///sqlUploadDocument.append(" and DOC_ID = ");
                    //sqlUploadDocument.append(" <<AW_DOC_ID>> ");
                    //Commented for Protocol Upload Document enhancement end 7
                    //Added for Protocol Upload Document enhancement end 8
                    sqlUploadDocument.append(" and DOCUMENT_TYPE_CODE = ");
                    sqlUploadDocument.append(" <<AW_DOC_TYPE_CODE>> ");
                    sqlUploadDocument.append(" and VERSION_NUMBER = ");
                    sqlUploadDocument.append(" <<AW_VERSION_NUMBER>> ");
                    //Added for Protocol Upload Document enhancement end 8
                    //Added for Coeus4.3 enhancement start 8.1
                     //Introduced document id to identify a document
                    sqlUploadDocument.append(" and DOCUMENT_ID = ");
                    sqlUploadDocument.append(" <<AW_DOCUMENT_ID>> ");
                    //Added for Coeus4.3 enhancement end 8.1
                }
            }else if(uploadDocumentBean.getAcType().trim().equals("D")){
                //Commented for Protocol Upload Document enhancement start 9
                //param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                // DBEngineConstants.TYPE_STRING,uploadDocumentBean.getAw_protocolNumber()));
                // param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                // DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_sequenceNumber())));

                //param.addElement(new Parameter("AW_DOC_ID",
                //DBEngineConstants.TYPE_INT,new Integer(uploadDocumentBean.getAw_uploadID())));
                //Commented for Protocol Upload Document enhancement end 9
                sqlUploadDocument.append(" delete from OSP$PROTOCOL_DOCUMENTS where ");
                sqlUploadDocument.append(" PROTOCOL_NUMBER = ");
                sqlUploadDocument.append(" <<AW_PROTOCOL_NUMBER>> ");
                sqlUploadDocument.append(" and SEQUENCE_NUMBER = ");
                sqlUploadDocument.append(" <<AW_SEQUENCE_NUMBER>> ");
                //Commented for Protocol Upload Document enhancement start 10
                //sqlUploadDocument.append(" and DOC_ID = ");
                //sqlUploadDocument.append(" <<AW_DOC_ID>> ");
                //Commented for Protocol Upload Document enhancement end 10
                //Added for Protocol Upload Document enhancement start 11
                sqlUploadDocument.append(" and DOCUMENT_TYPE_CODE = ");
                sqlUploadDocument.append(" <<AW_DOC_TYPE_CODE>> ");
                sqlUploadDocument.append(" and VERSION_NUMBER = ");
                sqlUploadDocument.append(" <<AW_VERSION_NUMBER>> ");
                //Added for Protocol Upload Document enhancement end 11
                //Added for Coeus4.3 enhancement start 12
                 //Introduced document id to identify a document
                sqlUploadDocument.append(" and DOCUMENT_ID = ");
                sqlUploadDocument.append(" <<AW_DOCUMENT_ID>> ");
                //Added for Coeus4.3 enhancement end 12
            }
        }

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlUploadDocument.toString());

        return procReqParameter;
    }

    //Added for Protocol Upload Document enhancement start 12
    /**
     *  Method to committed dB in coeus premium
     *  @param uploadDocumentBean uploadDocumentBean
     *  @return boolean data is saved or not
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProtocolUpload(UploadDocumentBean uploadDocumentBean)
    throws CoeusException, DBException{
        boolean isSuccess = false;
        java.util.List lstData = new Vector();
        ProcReqParameter procReqParameter
                = addUpdUploadDocument(uploadDocumentBean);
        if(procReqParameter != null){
            lstData.add(procReqParameter);
        }
        if(dbEngine!=null) {
            java.sql.Connection conn = null;
            conn = dbEngine.beginTxn();
            if(lstData != null && lstData.size() > 0){
                dbEngine.batchSQLUpdate((Vector)lstData,conn);
            }
            dbEngine.commit(conn);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        isSuccess = true;
        return isSuccess;
    }
    /**
     *  Method to copy data from parent protocol in renewal
     *  @param ProtocolInfoBean contain data
     *  @return string contain parent protocol number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int copyProtocolUploadDoc(ProtocolInfoBean newInfoBean,
            String parentProtoNo) throws CoeusException, DBException{
        int copydata = 0;
        Vector param= new Vector();
        HashMap hmCopyData = null;
        Vector result = new Vector();
        param.add(new Parameter("AS_SOURCE_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, parentProtoNo)) ;
        param.add(new Parameter("AS_DESTINATION_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, newInfoBean.getProtocolNumber()));
        param.add(new Parameter("AS_DESTINATION_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, new Integer(newInfoBean.getSequenceNumber()))) ;
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId )) ;

        if(dbEngine!=null){
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER COPY_DATA>> = call FN_COPY_PROTOCOL_DOCUMENTS("
                    +"<< AS_SOURCE_PROTOCOL_NUMBER >> , << AS_DESTINATION_PROTOCOL_NUMBER >>, << AS_DESTINATION_SEQUENCE_NUMBER >>," +
                    "<< UPDATE_USER >>)}",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmCopyData = (HashMap)result.elementAt(0);
            copydata = Integer.parseInt(hmCopyData.get("COPY_DATA").toString());
        }
        return copydata;
    }
    /**
     * This method creates next Version Number for the Upload Document page.
     * <li>To fetch the data, it uses the function .
     *
     * @return int version number .
     * @exception DBException if the instance of a dbEngine is null.
     */
    public int getNextVersionNumber(String protocolNumber , int seqNumber , int docType,
            int docId) throws  DBException{
        int versionNumber = 0;
        Vector param= new Vector();
        HashMap hmNextUploadId = null;
        Vector result = new Vector();

        param= new Vector();

        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
        param.add(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, new Integer(seqNumber))) ;
        param.add(new Parameter("DOCUMENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, new Integer(docType))) ;
        //Added for Coeus4.3 enhancement - starts
         //Introduced document id to identify a document
        param.add(new Parameter("DOCUMENT_ID",
                DBEngineConstants.TYPE_INT, new Integer(docId))) ;
        //Added for Coeus4.3 enhancement - ends
        if(dbEngine!=null){
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER VERSION_NUMBER>> = call FN_GET_VER_NUM_FOR_DOC_TYPE ("
                    +"<< PROTOCOL_NUMBER >> , << SEQUENCE_NUMBER >> , << DOCUMENT_TYPE_CODE >> , "
                    +"<< DOCUMENT_ID >>)}",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmNextUploadId = (HashMap)result.elementAt(0);
            versionNumber = Integer.parseInt(hmNextUploadId.get("VERSION_NUMBER").toString());
        }
        return versionNumber;
    }
//Added for Protocol Upload Document enhancement end 12
    /**
     * This method creates next Upload Id for the Upload Document page.
     * <li>To fetch the data, it uses the function .
     *
     * @return int uploadId .
     * @exception DBException if the instance of a dbEngine is null.
     */
    public int getNextUploadID(String protocolNumber , int seqNumber) throws  DBException{
        int uploadId = 0;
        Vector param= new Vector();
        HashMap hmNextUploadId = null;
        Vector result = new Vector();

        param= new Vector();

        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber )) ;
//        param.add(new Parameter("SEQUENCE_NUMBER",
//                DBEngineConstants.TYPE_INT, new Integer(seqNumber))) ;
//
        if(dbEngine!=null){
            //Commented for Coeus 4.3 enhancement - start
            //Sequence id is not required for generating the document id
//            result = dbEngine.executeFunctions(DSN,
//                    "{ <<OUT INTEGER DOC_ID>> = call FN_GENERATE_PROTOCOL_DOC_ID("
//                    +"<< PROTOCOL_NUMBER >> , << SEQUENCE_NUMBER >>)}",param);
            //Commented for Coeus 4.3 enhancement - end
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER DOC_ID>> = call FN_GENERATE_PROTOCOL_DOC_ID("
                    +"<< PROTOCOL_NUMBER >> )}",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmNextUploadId = (HashMap)result.elementAt(0);
            uploadId = Integer.parseInt(hmNextUploadId.get("DOC_ID").toString());
        }
        return uploadId;
    }

    //Main method for testing the bean
    /*public static void main(String args[]) {
        ProtocolUpdateTxnBean txnUser = new ProtocolUpdateTxnBean(DSN);
        ProtocolDataTxnBean txn = new ProtocolDataTxnBean();

        try{
            boolean success = false;
            Vector vct = new Vector(3,2);
            ProtocolInfoBean  protocolInfoBean;
            protocolInfoBean = (ProtocolInfoBean)txn.getProtocolInfo("0000000528");
            protocolInfoBean.setAcType("I");
            success = txnUser.addUpdProtocolAmendRenewals(protocolInfoBean);


            System.out.println("Generate : "+success);
     */
            /*
            vct = (Vector) txn.getOthersDetails("0000000528",1);
            ProtocolCustomElementsInfoBean protocolCustomElementsInfoBean = (ProtocolCustomElementsInfoBean)vct.elementAt(0);
            System.out.println("Column Name : "+protocolCustomElementsInfoBean.getColumnName());
            protocolCustomElementsInfoBean.setColumnValue("No");
            protocolCustomElementsInfoBean.setAcType("U");
            success = txnUser.addUpdProtocolOtherDetails(protocolCustomElementsInfoBean);

            System.out.println("Success : "+success);*/

            /*System.out.println("Size : "+vct.size());
            protocolReferencesBean = (ProtocolReferencesBean)vct.elementAt(0);
            System.out.println("Update Time Stamp :"+protocolReferencesBean.getUpdateTimestamp());
            protocolReferencesBean.setAcType("U");
            protocolReferencesBean.setReferenceKey("Updated Key");
            success = txnUser.addUpdProtocolReferences(protocolReferencesBean);
            System.out.println("Updated success : " +success);*/

           /*Timestamp ts1 = Timestamp.valueOf("2002-07-10 12:11:14");
            naBean.setProtocolNumber("0000000928");
            naBean.setSequenceNumber(2);
            naBean.setSpecialReviewCode(1);
            naBean.setApprovalCode(1);
            naBean.setUpdateTimestamp(ts1);
            naBean.setAcType("I");
            boolean success = txnUser.addUpdProtocolSpecialReview(naBean);*/

            /*raBean.setProtocolNumber("0000000928");
            raBean.setSequenceNumber(1);
            raBean.setRoleId(101);
            raBean.setUserId("sdowdy");
            raBean.setAcType("I");
            boolean success = txnUser.addProtocolRoles(raBean);

            if(success)
                System.out.println("successfully inserted");
            else
                System.out.println("exception while insert");*/
     /*
     }catch(Exception e){
            e.printStackTrace();
        }
    }*/
    //Coeus Enhancement Case #1799 step 5 start
    public LockingBean unLockBeans() throws CoeusException,DBException{

        LockingBean lockingBean = null;
        if(lockedBeans != null && lockedBeans.size()>0) {
            for(int index=0;index<lockedBeans.size();index++) {
                Object bean = lockedBeans.get(index);
                if(bean instanceof  ProposalSpecialReviewFormBean) {
                    ProposalSpecialReviewFormBean proposalSpecialReviewFormBean = (ProposalSpecialReviewFormBean)bean;
                    lockingBean = transMon.releaseLock("osp$Development Proposal_"+proposalSpecialReviewFormBean.getProposalNumber(),userId);
                } else if(bean instanceof InstituteProposalSpecialReviewBean){
                    InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean = (InstituteProposalSpecialReviewBean)bean;
                    lockingBean = transMon.releaseLock("osp$Proposal_"+instituteProposalSpecialReviewBean.getProposalNumber(),userId);

                } else if(bean instanceof AwardSpecialReviewBean){
                    AwardSpecialReviewBean awardSpecialReviewBean = (AwardSpecialReviewBean)bean;
                    AwardTxnBean awTxnBean = new AwardTxnBean();
                    String rootAwardNumber = awTxnBean.getRootAward(awardSpecialReviewBean.getMitAwardNumber());
                    lockingBean = transMon.releaseLock("osp$Award_"+rootAwardNumber,userId);
                }
            }
        }
        return lockingBean;
    }


    public boolean updateFundingSourceIndicator(String protocolNum,String indicator,String moduleId) throws DBException,CoeusException{
        Vector procedures = new Vector(5,3);
        Vector param = new Vector();
        boolean success = false;
        param.addElement(new Parameter("AV_SP_REV_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNum));
        param.addElement(new Parameter("AV_SP_REV_INDICATOR",
                DBEngineConstants.TYPE_STRING,indicator));
        param.addElement(new Parameter("AV_MODULE_CODE",
                DBEngineConstants.TYPE_INT,moduleId));
        StringBuffer sqlIndicator = new StringBuffer(
                "call UPD_SP_REV_IND_FOR_PROTO_LINKS(");
        sqlIndicator.append(" <<AV_SP_REV_NUMBER>> , ");
        sqlIndicator.append(" <<AV_SP_REV_INDICATOR>> , ");
        sqlIndicator.append(" <<AV_MODULE_CODE>>  )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlIndicator.toString());
        procedures.add(procReqParameter);
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }



    public void  updateSpecialReviewNotePad(ProtocolFundingSourceBean bean) throws CoeusException,DBException{

        NotepadTxnBean notepadTxnBean = new NotepadTxnBean();
        NotepadBean notepadBean = new NotepadBean();
        if(bean.getAcType() != null && bean.getAcType().equals("I"))
            notepadBean.setComments(new CoeusMessageResourcesBean().parseMessageKey("comment_exceptionCode_IRBSpecialReview.1000")+bean.getFundingSourceTypeDesc());
        else if(bean.getAcType().equals("D"))
            notepadBean.setComments(new CoeusMessageResourcesBean().parseMessageKey("comment_exceptionCode_IRBSpecialReview.1001")+bean.getFundingSourceTypeDesc());
        int maxEntryNumber = notepadTxnBean.getMaxProposalNotesEntryNumber(bean.getFundingSource());
        maxEntryNumber = maxEntryNumber + 1;
        notepadBean.setProposalAwardNumber(bean.getFundingSource());
        notepadBean.setNotePadType(new Integer(bean.getFundingSourceTypeCode()).toString());
        notepadBean.setEntryNumber(maxEntryNumber);
        notepadBean.setRestrictedView(false);
        notepadBean.setAcType("I");
        notepadBean.setUpdateTimestamp(dbTimestamp);
        notepadBean.setUpdateUser(userId);
        notepadBean.setUserName(userId);
        if(notepadBeans == null){
            notepadBeans = new CoeusVector();
        }
        notepadBeans.add(notepadBean);

    }

    /** Method used to insert protocol linking  details
     *  <li> it uses upd_protocol_links procedure.
     *
     * @return boolean true for successful insert
     * @param protocolLinkBean ProtocolLinkBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdProtocolLinks(ProtocolLinkBean protocolLinkBean)
    throws CoeusException ,DBException{

        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        if(protocolLinkBean!= null){
            param = new Vector();

            param.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolLinkBean.getProtocolNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+protocolLinkBean.getSequenceNumber()));
            param.addElement(new Parameter("MODULE_CODE",
                    DBEngineConstants.TYPE_INT, ""+protocolLinkBean.getModuleCode()));
            param.addElement(new Parameter("MODULE_ITEM_KEY",
                    DBEngineConstants.TYPE_STRING, protocolLinkBean.getModuleItemKey()));
            param.addElement(new Parameter("MODULE_ITEM_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+protocolLinkBean.getModuleItemSeqNumber()));
            param.addElement(new Parameter("ACTION_TYPE",
                    DBEngineConstants.TYPE_STRING, protocolLinkBean.getActionType()));
            param.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING, protocolLinkBean.getComments()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));

            StringBuffer sql = new StringBuffer( "call upd_protocol_links(");
            sql.append(" <<PROTOCOL_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<MODULE_CODE>> , ");
            sql.append(" <<MODULE_ITEM_KEY>> , ");
            sql.append(" <<MODULE_ITEM_SEQUENCE_NUMBER>> , ");
            sql.append(" <<ACTION_TYPE>> , ");
            sql.append(" <<COMMENTS>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>>  )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }
        return procReqParameter;
    }

    //to insert to the protocol links table when the protocol link is added/deleted in an award/proposal special review tables
//    public ProcReqParameter updateSpecialReviewLink(ProtocolInfoBean protocolInfoBean,SpecialReviewFormBean specialReviewBean) throws CoeusException,DBException{
    public ProcReqParameter updateSpecialReviewLink(String protocolNumber, int sequenceNumber,SpecialReviewFormBean specialReviewBean) throws CoeusException,DBException{
        ProtocolLinkBean protocolLinkBean = new ProtocolLinkBean();
//        protocolLinkBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
//        protocolLinkBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
        protocolLinkBean.setProtocolNumber(protocolNumber);
        protocolLinkBean.setSequenceNumber(sequenceNumber);

        if(specialReviewBean instanceof InstituteProposalSpecialReviewBean) {
            protocolLinkBean.setModuleCode(2);
            protocolLinkBean.setModuleItemKey(((InstituteProposalSpecialReviewBean)specialReviewBean).getProposalNumber());
        } else if(specialReviewBean instanceof AwardSpecialReviewBean) {
            protocolLinkBean.setModuleCode(1);
            protocolLinkBean.setModuleItemKey(((AwardSpecialReviewBean)specialReviewBean).getMitAwardNumber());

        }

        protocolLinkBean.setModuleItemSeqNumber(specialReviewBean.getSequenceNumber());
        if(specialReviewBean.getAcType() != null && specialReviewBean.getAcType().equals("I")){
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_exceptionCode.6000");
            protocolLinkBean.setComments(insertComments);
            protocolLinkBean.setActionType("I");
        }else{
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_exceptionCode.6001");
            protocolLinkBean.setComments(deleteComments);
            protocolLinkBean.setActionType("D");
        }
        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
        return addUpdProtocolLinks(protocolLinkBean);

    }
    //Coeus Enhancement Case #1799  end

    //For the Coeus Enhancement case:#1799
    //Modified datatype of modulecode with COEUSDEV-75 - Email Engine Redesign
    public Vector updateInboxTable(String moduleItemKey,int moduleCode,String acType)
    throws CoeusException,DBException{
        InboxBean inboxBean = null;
        String message = null;
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
        Vector vecData = protocolDataTxnBean.getRecipients(moduleItemKey, moduleCode);
        Vector inboxProcedurs =null;
        ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(userId);
        if(vecData != null) {
            inboxProcedurs  =  new Vector(5,3);
            for(int i=0;i<vecData.size();i++) {
                PersonRecipientBean personBean = (PersonRecipientBean)vecData.get(i);

                //loggedIn UserID is added for to setToUser method because GET_NOTIF_RECIP_FOR_PROTO_LINK procedure
                //returns fullName, it has more than eight characters size in db.
//                personBean.setUserName(userId.toUpperCase());

                if(personBean != null && personBean.getUserId()!=null && !"".equals(personBean.getUserId())) {
                    inboxBean = new InboxBean();
                    MessageBean messageBean = new MessageBean();
                    if(acType.equals("I")){
                        message = coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IRBSpecialReview.1000");
                    }else{
                        message = coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IRBSpecialReview.1001");
                    }
                    messageBean.setAcType("I");
                    messageBean.setMessage(message);

                    inboxBean.setMessageBean(messageBean);
                    inboxBean.setAcType("I");
                    inboxBean.setToUser(personBean.getUserId());
                    inboxBean.setFromUser(userId);
                    inboxBean.setArrivalDate(dbTimestamp);
                    inboxBean.setSubjectType('N');
                    inboxBean.setOpenedFlag('N');
                    inboxBean.setModuleCode(moduleCode);
                    inboxBean.setProposalNumber(moduleItemKey);
                    inboxProcedurs.addAll(proposalActionUpdateTxnBean.addUpdDeleteInbox(inboxBean));
                }
            }
        }
        return inboxProcedurs;
    }

    /**
     * Added for to update the Special Review Notepad
     *
     */
    public void  updateSplReviewNotePad(ProtocolFundingSourceBean bean) throws CoeusException,DBException{

        NotepadTxnBean notepadTxnBean = new NotepadTxnBean(userId);
        NotepadBean notepadBean = new NotepadBean();
        CoeusVector notepadBeans = new CoeusVector();
        if(bean.getAcType() != null && bean.getAcType().equals("I"))
            notepadBean.setComments(new CoeusMessageResourcesBean().parseMessageKey("comment_exceptionCode_IRBSpecialReview.1000")+bean.getFundingSourceTypeDesc());
        else if(bean.getAcType().equals("D"))
            notepadBean.setComments(new CoeusMessageResourcesBean().parseMessageKey("comment_exceptionCode_IRBSpecialReview.1001")+bean.getFundingSourceTypeDesc());
        int maxEntryNumber = notepadTxnBean.getMaxProposalNotesEntryNumber(bean.getFundingSource());
        maxEntryNumber = maxEntryNumber + 1;
        notepadBean.setProposalAwardNumber(bean.getFundingSource());
        notepadBean.setNotePadType(new Integer(bean.getFundingSourceTypeCode()).toString());
        notepadBean.setEntryNumber(maxEntryNumber);
        notepadBean.setRestrictedView(false);
        notepadBean.setAcType("I");
        notepadBean.setUpdateTimestamp(dbTimestamp);
        notepadBean.setUpdateUser(userId);
        notepadBean.setUserName(userId);
        notepadBeans.add(notepadBean);
        if(notepadBeans != null && notepadBeans.size()>0) { //NotepadTxnBean notepadTxnBean = new NotepadTxnBean(userId);
            notepadTxnBean.addUpdNotepad(notepadBeans);
        }

    }

    /**
     * Code added for 4.3 Enhancement
     * To update the final flag to the database.
     * @param protoCorrespRecipientsBean
     * @throws CoeusException
     * @throws DBException
     */
    public void updateCorrespondenceList(ProtoCorrespRecipientsBean protoCorrespRecipientsBean)
    throws CoeusException,DBException{

        Vector param = new Vector();
//        Vector procedures = new Vector();
//        ProcReqParameter procReqParameter = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        if(protoCorrespRecipientsBean!= null){
            param = new Vector();

            param.addElement(new Parameter("AV_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protoCorrespRecipientsBean.getProtocolNumber()));
            param.addElement(new Parameter("AV_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+protoCorrespRecipientsBean.getSequenceNumber()));
            param.addElement(new Parameter("AV_ACTION_ID",
                    DBEngineConstants.TYPE_INT, ""+protoCorrespRecipientsBean.getActionId()));
            param.addElement(new Parameter("AV_PROTO_CORRESP_TYPE_CODE",
                    DBEngineConstants.TYPE_INT, ""+protoCorrespRecipientsBean.getProtoCorrespTypeCode()));
            //Commented for case#3072 - Documents Premium - Final flag is not sticking
//            param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
//                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("AV_FINAL_FLAG",
                    DBEngineConstants.TYPE_STRING, protoCorrespRecipientsBean.isFinalFlag()== true ? "Y" : "N"));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, protoCorrespRecipientsBean.getUpdateTimestamp()));
            if(dbEngine!=null){
                //Modified for case#3072 - Documents Premium - Final flag is not sticking
                dbEngine.executeFunctions(DSN,
                        "{ <<OUT INTEGER IS_UPDATE>> = call FN_UPD_PROTO_CORRES_FINAL_FLAG( "
                        +" <<AV_PROTOCOL_NUMBER>>, <<AV_SEQUENCE_NUMBER>>, <<AV_ACTION_ID>>, "
                        +"<<AV_PROTO_CORRESP_TYPE_CODE>>, <<AV_UPDATE_USER>>, <<AV_FINAL_FLAG>>, <<AW_UPDATE_TIMESTAMP>>) }", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
    }

    /**
     * Code added for 4.3 Enhancement
     * To update the locked modules by particular Amendments/Renewals to the DB
     * @param protocolModuleBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public ProcReqParameter addUpdAmendRenewModules(ProtocolModuleBean protocolModuleBean)
        throws CoeusException,DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("PROTO_AMEND_RENEWAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolAmendRenewalNumber()));
        param.addElement(new Parameter("PROTOCOL_MODULE_CODE",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolModuleCode()));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AW_PROTO_AMEND_RENEWAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolAmendRenewalNumber()));
        param.addElement(new Parameter("AW_PROTOCOL_MODULE_CODE",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolModuleCode()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolModuleBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getAcType()));

        StringBuffer sqlProtoAmendRen = new StringBuffer(
                "call UPD_PROTO_AMEND_RENEW_MODULES(");
        sqlProtoAmendRen.append(" <<PROTO_AMEND_RENEWAL_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<PROTOCOL_MODULE_CODE>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_USER>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProtoAmendRen.append(" <<AW_PROTO_AMEND_RENEWAL_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<AW_PROTOCOL_MODULE_CODE>> , ");
        sqlProtoAmendRen.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProtoAmendRen.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlProtoAmendRen.toString());

        return procReqParameter;
    }

    /**
     * Code added for CoeusQA2313: Completion of Questionnaire for Submission
     * To update the locked questionnaires by particular Amendments/Renewals to the DB
     * @param protocolModuleBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public ProcReqParameter addUpdAmendRenewQuestionnaires(ProtocolModuleBean protocolModuleBean)
    throws CoeusException,DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                ModuleConstants.PROTOCOL_MODULE_CODE));
        param.addElement(new Parameter("PROTO_AMEND_RENEWAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolAmendRenewalNumber()));
        param.addElement(new Parameter("QUESTIONNAIRE_ID",
                DBEngineConstants.TYPE_INT,
                Integer.valueOf(protocolModuleBean.getProtocolModuleCode())));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getAcType()));

        StringBuffer sqlProtoAmendRen = new StringBuffer(
                "call UPD_AMEND_RENEW_QUESTIONNAIRE(");
        sqlProtoAmendRen.append(" <<MODULE_CODE>> , ");
        sqlProtoAmendRen.append(" <<PROTO_AMEND_RENEWAL_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<QUESTIONNAIRE_ID>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_USER>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProtoAmendRen.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlProtoAmendRen.toString());

        return procReqParameter;
    }

    public ProcReqParameter updateQuestionnaireInfo(ProtocolModuleBean protocolModuleBean)
    throws CoeusException,DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                ModuleConstants.PROTOCOL_MODULE_CODE));
        param.addElement(new Parameter("PROTO_AMEND_RENEWAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolAmendRenewalNumber()));
        param.addElement(new Parameter("AMEND_RENEW_SEQ_NUM",
                DBEngineConstants.TYPE_INT,
                protocolModuleBean.getProtocolAmendRenewSequenceNumber()));
        param.addElement(new Parameter("QUESTIONNAIRE_ID",
                DBEngineConstants.TYPE_INT,
                Integer.valueOf(protocolModuleBean.getProtocolModuleCode())));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getAcType()));

        StringBuffer sqlProtoAmendRen = new StringBuffer(
                "call UPD_AMEND_RENEW_QNR_ANS_HEADER(");
        sqlProtoAmendRen.append(" <<MODULE_CODE>> , ");
        sqlProtoAmendRen.append(" <<PROTO_AMEND_RENEWAL_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<AMEND_RENEW_SEQ_NUM>> , ");
        sqlProtoAmendRen.append(" <<QUESTIONNAIRE_ID>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_USER>> , ");
        sqlProtoAmendRen.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProtoAmendRen.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlProtoAmendRen.toString());

        return procReqParameter;
    }

    /**
     * Code added for 4.3 Enhancement
     * To get the modules data in modulecode as key and protocolNumber as value format
     * @param vecModuleData
     * @return
     */
    public HashMap getFormatedModuleData(Vector vecModuleData){
        HashMap hmModuleData = new HashMap();
        ProtocolModuleBean protocolModuleBean = null;
        if(vecModuleData!=null && vecModuleData.size() > 0){
            for(int index = 0 ; index < vecModuleData.size() ; index++){
                protocolModuleBean = (ProtocolModuleBean) vecModuleData.get(index);
                if(protocolModuleBean!=null){
                    hmModuleData.put(protocolModuleBean.getProtocolModuleCode(),
                            protocolModuleBean.getProtocolAmendRenewalNumber());
                }
            }
        }
        return hmModuleData;
    }

    /**
     * To get the module name
     * @return String
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * To set the module name
     * @param aModuleName
     */
    public void setModuleName(String aModuleName) {
        moduleName = aModuleName;
    }

    /**
     * Code added for 4.3 Enhancement
     * While deleting the locked modules syncing for that module
     * should happen.(copying the original protocol details to this
     * Amendment/Renewal protocol module)
     * @param protocolModuleBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return ProcReqParameter
     */
    public ProcReqParameter addUpdAmendRenewSyncData(ProtocolModuleBean protocolModuleBean)
        throws CoeusException,DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("AS_PROTO_AMEND_RENEW_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolAmendRenewalNumber()));
        param.addElement(new Parameter("AS_MODULE_CODE",
                DBEngineConstants.TYPE_STRING,
                protocolModuleBean.getProtocolModuleCode()));
        param.addElement(new Parameter("AS_LEVEL_TYPE",
                DBEngineConstants.TYPE_STRING,
                "M"));
        param.addElement(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        StringBuffer sqlProtoAmendRen = new StringBuffer(
                "<<OUT INTEGER IS_UPDATE>> = call "
                +"PKG_PROTOCOL_ACTIONS.FN_SYNC_DATA_WITH_ORG_PROTO(");
        sqlProtoAmendRen.append(" <<AS_PROTO_AMEND_RENEW_NUMBER>> , ");
        sqlProtoAmendRen.append(" <<AS_MODULE_CODE>> , ");
        sqlProtoAmendRen.append(" <<AS_LEVEL_TYPE>> , ");
        sqlProtoAmendRen.append(" <<AS_UPDATE_USER>> ) ");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlProtoAmendRen.toString());

        return procReqParameter;
    }

    /**
     * To get the appropriate indicator for given count of datas
     * @param oldCount original data count for module
     * @param newCount modified data count for module
     * @return String indicator
     */
    private String indicatorValue(String indicator){
        if(indicator.equals("P1")){
            indicator = "P0";
        } else if(indicator.equals("N1")){
            indicator = "N0";
        }
        return indicator;
    }

    /*Added for Case# 3018 -create ability to delete pending studies - Start*/
    /**
     * This method is used for deleting the protocol
     * @param  String protocolNumber
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return int success
     */
    public int deletePendingProtocol(String protocolNumber) throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        HashMap nextNumRow = null;
        int success = -1 ;
        try {
            param.add(new Parameter("AV_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolNumber ));
            param.add(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId ));

            if(dbEngine!=null){
                result = dbEngine.executeFunctions(DSN,
                        "{<<OUT INTEGER SUCCESS>> = call FN_DELETE_PROTOCOL( "
                        + " << AV_PROTOCOL_NUMBER >> , <<AV_UPDATE_USER>> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
        } catch (DBException ex) {
            ex.printStackTrace();
        }
        if(!result.isEmpty()) {
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
        }
        return success;
    }
    /*Added for Case# 3018 -create ability to delete pending studies - End*/

    //Added for case 3552 - IRB Attachments - start
    /**
     * Add/Update/Delete Protocol other attachment according to the values in
     * the uploadDocumentBean
     *
     * @param uploadDocumentBean
     * @return boolean
     */
    public boolean addUpdDelProtoOtherAttachment(UploadDocumentBean uploadDocumentBean)
    throws CoeusException, DBException{
        if(uploadDocumentBean != null){
            Vector param = new Vector();
            Vector procedures = new Vector();
            dbTimestamp = new CoeusFunctions().getDBTimestamp();

            if(uploadDocumentBean.getAcType().equals("I")){
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                uploadDocumentBean.setDocumentId(protocolDataTxnBean.getNextProtOtherDocumentId(
                        uploadDocumentBean.getProtocolNumber()));
            }
            param.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, uploadDocumentBean.getProtocolNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, Integer.toString(uploadDocumentBean.getSequenceNumber())));
            param.addElement(new Parameter("DOCUMENT_ID",
                    DBEngineConstants.TYPE_INT, Integer.toString(uploadDocumentBean.getDocumentId())));
            param.addElement(new Parameter("DOCUMENT_TYPE_CODE",
                    DBEngineConstants.TYPE_INT, Integer.toString(uploadDocumentBean.getDocCode())));
            param.addElement(new Parameter("DESCRIPTION",
                    DBEngineConstants.TYPE_STRING, uploadDocumentBean.getDescription()));
            param.addElement(new Parameter("FILE_NAME",
                    DBEngineConstants.TYPE_STRING, uploadDocumentBean.getFileName()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, uploadDocumentBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, uploadDocumentBean.getAcType()));

            StringBuffer sql = new StringBuffer("call UPD_PROTOCOL_OTHER_DOCUMENT( ");
            sql.append(" <<PROTOCOL_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<DOCUMENT_ID>>, ");
            sql.append(" <<DOCUMENT_TYPE_CODE>>, ");
            sql.append(" <<DESCRIPTION>>, ");
            sql.append(" <<FILE_NAME>>, ");
            sql.append(" <<UPDATE_TIMESTAMP>>, ");
            sql.append(" <<UPDATE_USER>>, ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
            sql.append(" <<AC_TYPE>> ) ");

            ProcReqParameter procInfo = new ProcReqParameter();
            procInfo.setDSN(DSN);
            procInfo.setParameterInfo(param);
            procInfo.setSqlCommand(sql.toString());

            procedures.add(procInfo);
            if(!uploadDocumentBean.getAcType().equals("D")){
                if(uploadDocumentBean.getDocument() != null){
                    procedures.add(updateProtoOtherAttachmentBlob(uploadDocumentBean));
                }
            }

            if(dbEngine!=null){
                try{
                    dbEngine.executeStoreProcs(procedures);
                }catch (DBException dbEx){
                    throw new CoeusException(dbEx.getMessage());
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        return true;
    }

    /**
     * Updates the blob data for the given document
     *
     * @param uploadDocumentBean
     * @return ProcReqParameter
     */
    private ProcReqParameter updateProtoOtherAttachmentBlob(UploadDocumentBean uploadDocumentBean)
    throws CoeusException, DBException{

        Vector param = new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, uploadDocumentBean.getProtocolNumber()));
        param.addElement(new Parameter("DOCUMENT_ID",
                DBEngineConstants.TYPE_INT, Integer.toString(uploadDocumentBean.getDocumentId())));
        param.addElement(new Parameter("DOCUMENT",
                    DBEngineConstants.TYPE_BLOB, uploadDocumentBean.getDocument()));

        StringBuffer sql = new StringBuffer("UPDATE OSP$PROTOCOL_OTHER_DOCUMENTS ");
        sql.append(" SET DOCUMENT = <<DOCUMENT>> ");
        sql.append(" WHERE PROTOCOL_NUMBER = <<PROTOCOL_NUMBER>>");
        sql.append(" AND DOCUMENT_ID = <<DOCUMENT_ID>>");

        ProcReqParameter procInfo = new ProcReqParameter();
        procInfo.setDSN(DSN);
        procInfo.setParameterInfo(param);
        procInfo.setSqlCommand(sql.toString());

        return procInfo;
    }
    //Added for case 3552 - IRB Attachments - end

    /** This method used to check if Protocol Linking Information is
     *  there in osp$protocol_funding_source for a funding source
     *  @return true if the Link Exists
     *  @param ProtocolFundingSourceBean ProtocolFundingSourceBean
     *  @exception DBException
     *  @exception  CoeusException
     **/
    public boolean isProtocolLinkExists (ProtocolFundingSourceBean ProtocolFundingSourceBean) throws  DBException, CoeusException {
        int retValue = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,ProtocolFundingSourceBean.getProtocolNumber()));
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+ProtocolFundingSourceBean.getSequenceNumber()));
        param.add(new Parameter("AS_FUNDING_SOURCE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,""+ProtocolFundingSourceBean.getFundingSourceTypeCode()));
        param.add(new Parameter("AS_FUNDING_SOURCE",
                DBEngineConstants.TYPE_STRING,ProtocolFundingSourceBean.getFundingSource()));

        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_CHECK_IRB_LINK_EXISTS(<< AS_PROTOCOL_NUMBER >>,<< AS_SEQUENCE_NUMBER >>,<< AS_FUNDING_SOURCE_TYPE_CODE >>,<< AS_FUNDING_SOURCE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            retValue = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        if(retValue == 1) {
            // If link exists
            return true;
        }
        return false;
    }

    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    /**
     * Update the protocol status
     *
     * @param protocolNumber - String
     * @param sequenceNumber - int
     * @param statusCode - int
     */
    public boolean updateProtocolStatus(String protocolNumber,
            int sequenceNumber, int statusCode )throws CoeusException, DBException {
        boolean success = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.add(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+sequenceNumber));
        param.add(new Parameter("PROTOCOL_STATUS_CODE",
                DBEngineConstants.TYPE_INT,""+statusCode));
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call fn_update_protocol_status(");
        sql.append(" <<PROTOCOL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<PROTOCOL_STATUS_CODE>> , ");
        sql.append(" <<UPDATE_USER>> )} ");

        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions(DSN, sql.toString(), param);
                success = true;
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;

    }
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End

     //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-Start
     /**
     * This method is used to get the default correspondence for organization and unit
     *
     * @param correspondenceDetail - Vector
     * @param protocolInfoBean - Protocol information beand
     * @return vecNewIRBCorrToSave - Vector
     */
     public Vector addDefaultIRBCorrespondence(Vector correspondenceDetail,ProtocolInfoBean protocolInfoBean)
        throws CoeusException, DBException{
            Vector vecNewIRBCorrToSave = new Vector();
            Vector vecDefaultOrgID = new Vector();
            Vector vecDefaultUnitID = new Vector();
            isIndicatorUpdated = false;
            CoeusVector correspondenceDetails = new CoeusVector();
            ProtocolCorrespondentsBean currentCorrespondenceBean ;
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
              //Getting the default organization correspondence
            Vector allLocationList = protocolInfoBean.getLocationLists();
            if(allLocationList != null && allLocationList.size()>0){
                for(Object obj: allLocationList){
                    ProtocolLocationListBean protocolLocationListBean = (ProtocolLocationListBean)obj;
                    if(protocolLocationListBean.isCanLoadCorrepondents()
                    || TypeConstants.INSERT_RECORD.equals(protocolLocationListBean.getAcType())
                    || TypeConstants.UPDATE_RECORD.equals(protocolLocationListBean.getAcType())){
                        vecDefaultOrgID.add(protocolLocationListBean.getOrganizationId());
                    }
                }
            }
            Vector vecIRBCorrForOrg = protocolDataTxnBean.getIRBCorrespForOrg(vecDefaultOrgID);
            //Getting the default unit correspondence
            ProtocolInvestigatorsBean protocolInvestigatorsBean = null;
            Vector vecAllInvestigators = protocolInfoBean.getInvestigators();
            if(vecAllInvestigators !=null && vecAllInvestigators.size()>0){
                for(Object investigatorObj : vecAllInvestigators){
                    protocolInvestigatorsBean = (ProtocolInvestigatorsBean) investigatorObj;
                    Vector allUnitList = protocolInvestigatorsBean.getInvestigatorUnits();
                    if(allUnitList != null && allUnitList.size()>0){
                        for(Object obj: allUnitList){
                            ProtocolInvestigatorUnitsBean protocolInvestigatorUnitsBean = (ProtocolInvestigatorUnitsBean)obj;
                            if(protocolInvestigatorUnitsBean.isCanLoadCorrepondents()
                            || TypeConstants.INSERT_RECORD.equals(protocolInvestigatorUnitsBean.getAcType())
                            || TypeConstants.UPDATE_RECORD.equals(protocolInvestigatorUnitsBean.getAcType())){
                                 vecDefaultUnitID.add(protocolInvestigatorUnitsBean.getUnitNumber());
                            }
                        }
                    }
                }
            }
            Vector vecIRBCorrForUnit = protocolDataTxnBean.getIRBCorrespForUnit(vecDefaultUnitID);
            if(correspondenceDetail != null && correspondenceDetail.size()>0){
                correspondenceDetails.addAll(correspondenceDetail);
            }
            //Adding the organization correspondence to correspondenceDetail
            if(vecIRBCorrForOrg !=null && vecIRBCorrForOrg.size()>0){
                Equals eqCorr;
                Equals eqPerson;
                And andCorrPerson;
                And andCorrPersonActive;
                for(Object obj: vecIRBCorrForOrg){
                    currentCorrespondenceBean =    (ProtocolCorrespondentsBean)obj;
                    eqCorr= new Equals("correspondentTypeCode",currentCorrespondenceBean.getCorrespondentTypeCode());
                    eqPerson = new Equals("personId",currentCorrespondenceBean.getPersonId());
                    andCorrPerson = new And(eqCorr,eqPerson);
                    andCorrPersonActive = new And(andCorrPerson,CoeusVector.FILTER_ACTIVE_BEANS);
                    CoeusVector filteredResult = correspondenceDetails.filter(andCorrPersonActive);
                    if(filteredResult.size()==0){
                        currentCorrespondenceBean.setAcType(TypeConstants.INSERT_RECORD);
                        correspondenceDetails.add(currentCorrespondenceBean);
                    }
                }
            }

            //Adding the unit correspondence to correspondenceDetail
            if(vecIRBCorrForUnit !=null && vecIRBCorrForUnit.size()>0){
                Equals eqCorr;
                Equals eqPerson;
                And andCorrPerson;
                And andCorrPersonActive;
                for(Object obj: vecIRBCorrForUnit){
                    currentCorrespondenceBean =    (ProtocolCorrespondentsBean)obj;
                    eqCorr= new Equals("correspondentTypeCode",currentCorrespondenceBean.getCorrespondentTypeCode());
                    eqPerson = new Equals("personId",currentCorrespondenceBean.getPersonId());
                    andCorrPerson = new And(eqCorr,eqPerson);
                    andCorrPersonActive = new And(andCorrPerson,CoeusVector.FILTER_ACTIVE_BEANS);
                    CoeusVector filteredResult = correspondenceDetails.filter(andCorrPersonActive);
                    if(filteredResult.size()==0){
                        currentCorrespondenceBean.setAcType(TypeConstants.INSERT_RECORD);
                        correspondenceDetails.add(currentCorrespondenceBean);
                    }
                }
            }
            //Filtering the duplicate correspondence
             if(correspondenceDetails != null && correspondenceDetails.size() > 0){
                for(Object obj: correspondenceDetails){
                    ProtocolCorrespondentsBean protocolCorrespondenceBean = (ProtocolCorrespondentsBean) obj;
                    protocolCorrespondenceBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                    protocolCorrespondenceBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());

                    vecNewIRBCorrToSave.add(protocolCorrespondenceBean);
                    if( TypeConstants.INSERT_RECORD.equals(protocolCorrespondenceBean.getAcType())
                    ||TypeConstants.UPDATE_RECORD.equals(protocolCorrespondenceBean.getAcType())){
                        isIndicatorUpdated = true;
                    }
                }
             }

         return vecNewIRBCorrToSave;
    }

    /**
     * This method is used to update the protocol correspondence indicator
     * @param   protocolInfoBean - Protocol information beand
     * @return  procInfo - ProcReqParameter procedure parameter avs values.
     */
    private ProcReqParameter updateIRBProtocolIndicator(ProtocolInfoBean protocolInfoBean) throws DBException, CoeusException{
        boolean success = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolInfoBean.getProtocolNumber()));
        param.add(new Parameter("AV_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+protocolInfoBean.getSequenceNumber()));
        param.add(new Parameter("AV_FIELD_NAME",
                DBEngineConstants.TYPE_STRING,"CORRESPONDENT_INDICATOR"));
        param.add(new Parameter("AV_INDICATOR",
                DBEngineConstants.TYPE_STRING,"P1"));
        param.add(new Parameter("AV_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.add(new Parameter("AV_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        StringBuffer sql = new StringBuffer("call UPDATE_PROTOCOL_INDICATOR( ");

        sql.append(" <<AV_PROTOCOL_NUMBER>> , ");
        sql.append(" <<AV_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AV_FIELD_NAME>> , ");
        sql.append(" <<AV_INDICATOR>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>>)");
        ProcReqParameter procInfo = new ProcReqParameter();
        procInfo.setDSN(DSN);
        procInfo.setParameterInfo(param);
        procInfo.setSqlCommand(sql.toString());

        return procInfo;
    }
    //Added for-COEUSQA-2879 IRB - Correspondents do not auto-populate for protocols created in Premium-End

    //Added for COEUSQA-3119  - Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    public Vector updateFundingSourceLink(ProtocolFundingSourceBean protocolFundingSourceBean,String protocolNumber, int seqNumber, String protocolAcType,
             Vector procedures, Vector lockedBeans)throws CoeusException, DBException{
        CoeusFunctions functions= new CoeusFunctions();
        String linkAward=functions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK);
        String linkProposal=functions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK);
        String indicator="P1";

        CoeusMessageResourcesBean coeusMessageResourcesBean
                =new CoeusMessageResourcesBean();
        if(protocolFundingSourceBean.getFundingSourceTypeCode()== 5){
            if(linkProposal.equals("1")) {

                InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                boolean getLock = instituteProposalTxnBean.lockCheck(protocolFundingSourceBean.getFundingSource(),userId);
                if(getLock){
                    InstituteProposalBean instituteProposalBean = instituteProposalTxnBean.getInstituteProposalDetails(protocolFundingSourceBean.getFundingSource());
                    int maxSpecialRevNumber = instituteProposalTxnBean.getMaxInstituteProposalSpecialReviewNumber(instituteProposalBean.getProposalNumber(),instituteProposalBean.getSequenceNumber());

                    InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(userId);
                    InstituteProposalSpecialReviewBean insituteProposalSpReviewBean = null;
                    try {
                        LockingBean lockingBean = instituteProposalTxnBean.getInstituteProposalLock(protocolFundingSourceBean.getFundingSource(),userId,unitNumber);
                    } catch(Exception e) {
                        unLockBeans();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                        // String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource();
                        throw new LockingException(msg);
                    }
                    if(protocolFundingSourceBean.getAcType().equals("I")) {
                        insituteProposalSpReviewBean = new InstituteProposalSpecialReviewBean();
                        insituteProposalSpReviewBean.setProposalNumber(protocolFundingSourceBean.getFundingSource());
                        insituteProposalSpReviewBean.setSpecialReviewCode(new Integer(functions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN)).intValue());
                        insituteProposalSpReviewBean.setApprovalCode(new Integer(functions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE)).intValue());
                        insituteProposalSpReviewBean.setSpecialReviewNumber(maxSpecialRevNumber+1);
                        insituteProposalSpReviewBean.setProtocolSPRevNumber(protocolNumber);
                        insituteProposalSpReviewBean.setAcType("I");
                        insituteProposalSpReviewBean.setComments(coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IRBSpecialReview.1000"));

                        insituteProposalSpReviewBean.setSequenceNumber(instituteProposalBean.getSequenceNumber());
                        // Code modified for Case#3070 - Ability to modify funding source - starts
                        lockedBeans.add(insituteProposalSpReviewBean);
                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                        boolean canLink = protocolDataTxnBean.canAddProtocolLinks(protocolNumber, "2",
                                protocolFundingSourceBean.getFundingSource());
                        if(protocolNumber != null && protocolNumber.length() == 10 && canLink){
                            procedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(insituteProposalSpReviewBean));
//                                        lockedBeans.add(insituteProposalSpReviewBean);
                            updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"2");
//                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                            try {
                                protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
                                        protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);

                            } catch(Exception e) {
                            }
                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,insituteProposalSpReviewBean));
                            updateSpecialReviewNotePad(protocolFundingSourceBean);
                            Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,protocolFundingSourceBean.getAcType());
                            if(inboxData != null)
                                procedures.addAll(inboxData);
                        } else if((protocolAcType == null || protocolAcType.length() == 0  || TypeConstants.UPDATE_RECORD.equals(protocolAcType)) && canLink){
                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,insituteProposalSpReviewBean));
                        }
                        // Code modified for Case#3070 - Ability to modify funding source - ends
                    } else if(protocolFundingSourceBean.getAcType().equals("D")) {
                        Vector specRevCount =  instituteProposalTxnBean.getInstituteProposalSpecialReview(protocolFundingSourceBean.getFundingSource());
                        specRevCount = (specRevCount == null)? new CoeusVector() : specRevCount;
                        int size = specRevCount.size();
                        for(int i = 0;i<specRevCount.size();i++){
                            insituteProposalSpReviewBean =(InstituteProposalSpecialReviewBean)specRevCount.get(i);
                            String specialRevNum = insituteProposalSpReviewBean.getProtocolSPRevNumber();
                            specialRevNum = (specialRevNum == null)? "" : specialRevNum;
                            if(specialRevNum.equals(protocolNumber.substring(0, 10))){
                                insituteProposalSpReviewBean.setAcType("D");
                                break;
                            } else
                                insituteProposalSpReviewBean = null;
                        }
                        // Code modified for Case#4379 - Funding sources not inserting "D" records to links table for amendment/renewal
                                    /*
                                     *  This is for looging into OSP$PROTOCOL_LINKS
                                     */
                        if(insituteProposalSpReviewBean==null){
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            Vector vecProtocolLinks = protocolDataTxnBean.getProtocolLinksData(protocolNumber);
                            if(vecProtocolLinks!=null && !vecProtocolLinks.isEmpty()){
                                String fundingSrcToDelete = protocolFundingSourceBean.getFundingSource();
                                for(int i=0;i<vecProtocolLinks.size();i++){
                                    ProtocolLinkBean protocolLinkBean = (ProtocolLinkBean)vecProtocolLinks.elementAt(i);
                                    if(protocolLinkBean.getModuleItemKey().equals(fundingSrcToDelete)
                                    && "I".equals(protocolLinkBean.getActionType())){
                                        insituteProposalSpReviewBean = new InstituteProposalSpecialReviewBean();
                                        insituteProposalSpReviewBean.setProposalNumber(fundingSrcToDelete);
                                        insituteProposalSpReviewBean.setSequenceNumber(protocolLinkBean.getModuleItemSeqNumber());
                                        break;
                                    }
                                }
                            }
                        }
                        // Case#4379 end
                        // Code modified for Case#3070 - Ability to modify funding source - starts
                        lockedBeans.add(insituteProposalSpReviewBean);
//                                    if(insituteProposalSpReviewBean != null) {
                        if(insituteProposalSpReviewBean != null && protocolNumber != null && protocolNumber.length() == 10 && insituteProposalSpReviewBean.getAcType()!=null) {
                            procedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(insituteProposalSpReviewBean));
//                                        lockedBeans.add(insituteProposalSpReviewBean);
                            // Code modified for Case#3070 - Ability to modify funding source - ends
                            size--;
                            if(size==0)
                                indicator = "N1";


                            updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"2");
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                            try {
                                protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
                                        protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);
                            } catch(Exception e) {
                            }
                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,insituteProposalSpReviewBean));
                            updateSpecialReviewNotePad(protocolFundingSourceBean);
                            Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,protocolFundingSourceBean.getAcType());
                            if(inboxData != null)
                                procedures.addAll(inboxData);
                            // Code modified for Case#3070 - Ability to modify funding source - starts
                        } else if(insituteProposalSpReviewBean != null){
                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,insituteProposalSpReviewBean));
                        }
                        // Code modified for Case#3070 - Ability to modify funding source - ends
                    }
                } else {
                    unLockBeans();
                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                    // String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource();

                    throw new LockingException(msg);
                }
            }
        } else if(protocolFundingSourceBean.getFundingSourceTypeCode()== 6) {

            if(linkAward.equals("1")) {
                AwardTxnBean awardTxnBean = new AwardTxnBean();
                boolean getLock = awardTxnBean.lockCheck(protocolFundingSourceBean.getFundingSource(),userId);
                if(getLock){

                    AwardBean awardBean = awardTxnBean.getAward(protocolFundingSourceBean.getFundingSource());
                    AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
                    int maxSpecialRevNumber = awardLookUpDataTxnBean.getMaxAwardSpecialReviewNumber(awardBean.getMitAwardNumber(),awardBean.getSequenceNumber());
                    AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(userId);
                    AwardSpecialReviewBean awardSpReviewBean = null;
                    try {
                        LockingBean lockingBean = awardTxnBean.getAwardLock(protocolFundingSourceBean.getFundingSource(),userId,unitNumber);
                    } catch(Exception e) {
                        unLockBeans();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                        //String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+protocolFundingSourceBean.getFundingSource();
                        throw new LockingException(msg);
                    }

                    if(protocolFundingSourceBean.getAcType().equals("I")) {
                        awardSpReviewBean = new AwardSpecialReviewBean();
                        awardSpReviewBean.setMitAwardNumber(protocolFundingSourceBean.getFundingSource());
                        awardSpReviewBean.setSpecialReviewCode(new Integer(functions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN)).intValue());
                        awardSpReviewBean.setApprovalCode(new Integer(functions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE)).intValue());
                        awardSpReviewBean.setSpecialReviewNumber(maxSpecialRevNumber+1);

                        awardSpReviewBean.setProtocolSPRevNumber(protocolNumber);
                        awardSpReviewBean.setAcType("I");
                        awardSpReviewBean.setComments(coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IRBSpecialReview.1000"));
                        awardSpReviewBean.setSequenceNumber(awardBean.getSequenceNumber());
                        // Code modified for Case#3070 - Ability to modify funding source - starts
                        lockedBeans.add(awardSpReviewBean);
                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                        boolean canLink = protocolDataTxnBean.canAddProtocolLinks(protocolNumber, "2",
                                protocolFundingSourceBean.getFundingSource());
                        if(protocolNumber != null && protocolNumber.length() == 10 && canLink){
                            procedures.add(awardUpdateTxnBean.addUpdAwardSpecialReview(awardSpReviewBean));
//                                        lockedBeans.add(awardSpReviewBean);
                            updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"1");
//                                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                            try {
                                protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
                                        protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE);
                            } catch(Exception e) {
                            }

                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,awardSpReviewBean));
                            updateSpecialReviewNotePad(protocolFundingSourceBean);
                            Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE,protocolFundingSourceBean.getAcType());
                            if(inboxData != null)
                                procedures.addAll(inboxData);
                        } else if((protocolAcType == null || protocolAcType.length() == 0  || TypeConstants.UPDATE_RECORD.equals(protocolAcType)) && canLink){
                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,awardSpReviewBean));
                        }
                         unLockBeans();
                        // Code modified for Case#3070 - Ability to modify funding source - ends
                    } else if(protocolFundingSourceBean.getAcType().equals("D")) {
                        Vector specRevCount =  awardTxnBean.getAwardSpecialReview(protocolFundingSourceBean.getFundingSource());
                        specRevCount = (specRevCount == null)? new CoeusVector() : specRevCount;
                        int size = specRevCount.size();

                        for(int i = 0;i<specRevCount.size();i++){
                            awardSpReviewBean =(AwardSpecialReviewBean)specRevCount.get(i);
                            String specialRevNum = awardSpReviewBean.getProtocolSPRevNumber();
                            specialRevNum = (specialRevNum == null)? "" : specialRevNum;
                            if(specialRevNum.equals(protocolNumber.substring(0, 10))){
//                                        if(protocolNumber.equals(awardSpReviewBean.getProtocolSPRevNumber())){
                                awardSpReviewBean.setAcType("D");
                                break;
                            } else
                                awardSpReviewBean = null;
                        }
                        // Code modified for Case#4379 - Funding sources not inserting "D" records to links table for amendment/renewal
                                    /*
                                     * This is for looging into OSP$PROTOCOL_LINKS
                                     */
                        if(awardSpReviewBean==null){
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            Vector vecProtocolLinks = protocolDataTxnBean.getProtocolLinksData(protocolNumber);
                            if(vecProtocolLinks!=null && !vecProtocolLinks.isEmpty()){
                                String fundingSrcToDelete = protocolFundingSourceBean.getFundingSource();
                                for(int i=0;i<vecProtocolLinks.size();i++){
                                    ProtocolLinkBean protocolLinkBean = (ProtocolLinkBean)vecProtocolLinks.elementAt(i);
                                    if(protocolLinkBean.getModuleItemKey().equals(fundingSrcToDelete)
                                    && "I".equals(protocolLinkBean.getActionType())){
                                        awardSpReviewBean = new AwardSpecialReviewBean();
                                        awardSpReviewBean.setMitAwardNumber(fundingSrcToDelete);
                                        awardSpReviewBean.setSequenceNumber(protocolLinkBean.getModuleItemSeqNumber());
                                        break;
                                    }
                                }
                            }
                        }
                        // Case#4379 end
                        // Code modified for Case#3070 - Ability to modify funding source - starts
                        lockedBeans.add(awardSpReviewBean);
//                                    if(awardSpReviewBean != null) {
                        if(awardSpReviewBean != null && protocolNumber != null && protocolNumber.length() == 10 && awardSpReviewBean.getAcType()!=null) {
                            procedures.add(awardUpdateTxnBean.addUpdAwardSpecialReview(awardSpReviewBean));
//                                        lockedBeans.add(awardSpReviewBean);
                            // Code modified for Case#3070 - Ability to modify funding source - ends
                            size--;

                            if(size==0)
                                indicator = "N1";


                            updateFundingSourceIndicator(protocolFundingSourceBean.getFundingSource(),indicator,"1");
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                            try {
                                protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
                                        protocolNumber,seqNumber,protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE);
                            } catch(Exception e) {
                            }
                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,awardSpReviewBean));
                            updateSpecialReviewNotePad(protocolFundingSourceBean);
                            Vector inboxData = updateInboxTable(protocolFundingSourceBean.getFundingSource(),ModuleConstants.AWARD_MODULE_CODE,protocolFundingSourceBean.getAcType());
                            if(inboxData != null)
                                procedures.addAll(inboxData);
                            // Added for Case#3070 - Ability to modify funding source - starts
                        } else if(awardSpReviewBean != null){
                            procedures.add(updateSpecialReviewLink(protocolNumber,seqNumber,awardSpReviewBean));
                        }
                        // Added for Case#3070 - Ability to modify funding source - ends
                         unLockBeans();

                    }

                }

                else {
                    unLockBeans();
                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+protocolFundingSourceBean.getFundingSource()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                    //  String msg = userId+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+protocolFundingSourceBean.getFundingSource();
                    throw new LockingException(msg);
                }


            }
        }
        if(notepadBeans != null && notepadBeans.size()>0) {
            NotepadTxnBean notepadTxnBean = new NotepadTxnBean(userId);
            notepadTxnBean.addUpdNotepad(notepadBeans);
            notepadBeans = new CoeusVector();
        }
        return procedures;
    }
       //Added for COEUSQA-3119  - Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end

    //COEUSQA:2653 - Add Protocols to Medusa - Start
    /**
     * Method to Add Update the Notes for Protocol
     * @param protocolNotepadBean
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public boolean  addUpdProtocolNotes(ProtocolNotepadBean  protocolNotepadBean)
    throws DBException, CoeusException{
        Vector paramNotepad = new Vector();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean success = false;

        if(protocolNotepadBean!=null && protocolNotepadBean.getAcType()!=null){
            paramNotepad = new Vector();
            paramNotepad.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolNotepadBean.getProtocolNumber()));
            paramNotepad.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    ""+protocolNotepadBean.getSequenceNumber()));
            paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    ""+protocolNotepadBean.getEntryNumber()));
            paramNotepad.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,protocolNotepadBean.getComments()));
            paramNotepad.addElement(new Parameter("RESTRICTED_VIEW",
                    DBEngineConstants.TYPE_STRING, protocolNotepadBean.isRestrictedFlag() ? "Y": "N"));
            paramNotepad.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramNotepad.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,protocolNotepadBean.getUpdateTimestamp()));
            paramNotepad.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, protocolNotepadBean.getAcType()));

            StringBuffer sqlNotepad = new StringBuffer(
                    "call UPD_PROTO_NOTES(");
            sqlNotepad.append(" <<PROTOCOL_NUMBER>> , ");
            sqlNotepad.append(" <<SEQUENCE_NUMBER>> , ");
            sqlNotepad.append(" <<ENTRY_NUMBER>> , ");
            sqlNotepad.append(" <<COMMENTS>> , ");
            sqlNotepad.append(" <<RESTRICTED_VIEW>> , ");
            sqlNotepad.append(" <<UPDATE_USER>> , ");
            sqlNotepad.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlNotepad.append(" <<AC_TYPE>> )");

            ProcReqParameter procNotepad  = new ProcReqParameter();
            procNotepad.setDSN(DSN);
            procNotepad.setParameterInfo(paramNotepad);
            procNotepad.setSqlCommand(sqlNotepad.toString());

            procedures.add(procNotepad);
        }
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    //COEUSQA:2653 - End
//COEUSQA 1457 STARTS
  public boolean sendRemovalEmailToPropPersons(String protocolNumber,Vector toPersonList,Vector toPersonRoles, String loginnedPerson ){
     boolean mailSend=true;
     Vector vecRecipientsdata=new Vector();
     PersonRecipientBean personRecipientBean=new PersonRecipientBean();
     vecRecipientsdata.add(personRecipientBean);
     if(toPersonList!=null&&toPersonList.size()>0){
         try{
         MailHandler mailHandler=new MailHandler();
         ProtocolSubmissionTxnBean protocolSubmissionTxnBean=new ProtocolSubmissionTxnBean();
         HashMap protDetails=protocolSubmissionTxnBean.getProtoPersonDetailsForMail(protocolNumber);
         String protocolNumberData="Protocol Number: "+protocolNumber;
         String piData = "PI:" +protDetails.get("PI_NAME").toString();
         String unitData = "Lead Unit:"+protDetails.get("UNIT_NUMBER").toString() +":"+protDetails.get("UNIT_NAME").toString();
         String applicationDate = "Application Date:"+protDetails.get("APPLICATION_DATE").toString();
         String approvalDate = "Approval Date: "+((protDetails.get("APPROVAL_DATE")==null)?" ":protDetails.get("APPROVAL_DATE").toString());
         String title = "Title: "+protDetails.get("TITLE").toString();
         String protocolType = "Type: "+protDetails.get("PROTOCOL_TYPE_DESC").toString();
         String protocolStatus = "Protocol Status: "+protDetails.get("PROTOCOL_STATUS_DESC").toString();
         String personId=null;
         String emailId=null;
         String role = "";
         MailMessageInfoBean mailMsgInfoBean;
                  for(int i=0;i<toPersonList.size();i++){
             if(toPersonList.get(i) !=null){
                 personId=toPersonList.get(i).toString();
                 role=toPersonRoles.get(i).toString();
                 emailId=getEmailIdForPerson(personId);
                 if(emailId!=null&&emailId.trim().length()>0){
                     personRecipientBean.setEmailId(emailId);
                     personRecipientBean.setPersonId(personId);
                     mailMsgInfoBean = mailHandler.getNotification(ModuleConstants.PROTOCOL_MODULE_CODE,PERSON_REMOVAL_MAIL_ACTION_ID);
                     if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                         mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
                                           // mailMsgInfoBean.setSubject("Removal from IRB Protocol");
                                            mailMsgInfoBean.appendMessage(piData, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(unitData, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolNumberData, "\n") ;
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(applicationDate, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(approvalDate, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(title, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolType, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolStatus, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage("You have been removed as "+role+ " for the above referenced project by "+loginnedPerson+" on "+dbTimestamp.toString()+". ", "\n\n") ;
                                            mailHandler.sendMail(ModuleConstants.PROTOCOL_MODULE_CODE,PERSON_REMOVAL_MAIL_ACTION_ID, mailMsgInfoBean);
                                          
                     }}}}}
         catch(Exception ex){
             mailSend=false;
         }
     }
     return mailSend;
  }
   public String getEmailIdForPerson(String personId)throws CoeusException, DBException{
        String emailId=null;
        Vector result = null;
        Vector param= new Vector();
        HashMap protocolDevRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_PERSON_EMAIL_ID( <<PERSON_ID>>, <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            protocolDevRow = (HashMap)result.elementAt(0);
            emailId=(String) protocolDevRow.get("EMAIL_ADDRESS");
        }
        return emailId;
    }
//COEUSQA 1457 ENDS
   
   //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
   /**
    * method to copy the Questionnaires
    *
    * @param sourceProtocolNumber
    * @param targetProtocolNumber
    * @param sequenceNumber
    * @throws edu.mit.coeus.exception.CoeusException
    * @throws edu.mit.coeus.utils.dbengine.DBException
    */
   public void copyProtocolQuestionnaire(String sourceProtocolNumber, String targetProtocolNumber,
           String sequenceNumber)throws CoeusException, DBException {
       
       Vector param= new Vector();
       Vector moduleResult = null;
       param.add(new Parameter("AS_MODULE_CODE",
               DBEngineConstants.TYPE_INT, ModuleConstants.PROTOCOL_MODULE_CODE));
       param.add(new Parameter("AS_SOURCE_MODULE_ITEM_KEY",
               DBEngineConstants.TYPE_STRING,sourceProtocolNumber));
       param.add(new Parameter("AS_MODULE_SUB_ITEM_CODE",
               DBEngineConstants.TYPE_INT,0));
       param.add(new Parameter("AS_SOURCE_MODULE_SUB_ITEM_KEY",
               DBEngineConstants.TYPE_STRING,sequenceNumber));
       param.add(new Parameter("AS_TARGET_MODULE_ITEM_KEY",
               DBEngineConstants.TYPE_STRING, targetProtocolNumber));
       param.add(new Parameter("AS_TARGET_MODULE_SUB_ITEM_KEY",
               DBEngineConstants.TYPE_STRING, sequenceNumber ));
       param.add(new Parameter("AS_UPDATE_USER",
               DBEngineConstants.TYPE_STRING,userId));
       if(dbEngine !=null){
           moduleResult = new Vector(3,2);
           
           moduleResult = dbEngine.executeFunctions("Coeus",
                   "{<<OUT INTEGER SUCCESS>> = call FN_COPY_MODULE_QNR_ANSWERS( "
                   + "<<AS_MODULE_CODE>>, <<AS_SOURCE_MODULE_ITEM_KEY>>, <<AS_MODULE_SUB_ITEM_CODE>>," +
                   "<<AS_SOURCE_MODULE_SUB_ITEM_KEY>>,<<AS_TARGET_MODULE_ITEM_KEY>>,<<AS_TARGET_MODULE_SUB_ITEM_KEY>>," +
                   "<<AS_UPDATE_USER>>)}", param) ;
       }else{
           throw new CoeusException("db_exceptionCode.1000");
       }
   }
   //COEUSQA:3503 - End
}
