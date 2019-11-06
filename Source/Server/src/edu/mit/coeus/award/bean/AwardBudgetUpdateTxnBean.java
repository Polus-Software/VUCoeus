/*
 * AwardBudgeteUpdateTxnBean.java
 *
 * Created on August 4, 2005, 4:08 PM
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  ajaygm
 */
public class AwardBudgetUpdateTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the userId for the logged in user
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    
    /** Creates a new instance of AwardBudgetUpdateTxnBean */
    public AwardBudgetUpdateTxnBean() {
    }
    
    /**
     * Creates new AwardBudgetUpdateTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public AwardBudgetUpdateTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /** Method used to copy Award Budget details
     *  <li>To copy data, it uses FN_COPY_PROP_BUDGET_TO_AWARD procedure.
     *
     * @return the new version no
     * @param cvCopyData CoeusVector
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available.
     */
    public int copyAwardBudget(CoeusVector cvCopyData)
    throws CoeusException, DBException{
        int newVersionNo = -1;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        String mitAwardNumber = (String)cvCopyData.get(0);
        int sequenceNumber = Integer.parseInt((String)(cvCopyData.get(1)));
        int amountSequenceNumber = Integer.parseInt((String)(cvCopyData.get(2)));
        String proposalNumber = (String)cvCopyData.get(3);
        String versionNumber = (String)cvCopyData.get(4);
        String budgetPeriod = (String)cvCopyData.get(5);
        //int proposalCount = Integer.parseInt((String)(cvCopyData.get(6)));
        
        Vector result = new Vector();
        Vector param = new Vector();
        
        param.addElement( new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber ));
        param.addElement( new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+sequenceNumber ));
        param.addElement( new Parameter("AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+amountSequenceNumber ));
        param.addElement( new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber ));
        param.addElement( new Parameter("PROPOSAL_VERSION_NUMBER",
            DBEngineConstants.TYPE_STRING, versionNumber ));
        param.addElement( new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_STRING, budgetPeriod ));
        
        param.addElement( new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_DATE, dbTimestamp));
        param.addElement( new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId ));
        
        /*if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER VERSION_NUMBER>> = "
            +" call FN_COPY_BUDGET_TO_AWARD(" +
            "<< MIT_AWARD_NUMBER >>, " +
            "<< SEQUENCE_NUMBER >>, " +
            "<< AMOUNT_SEQUENCE_NUMBER >>, " +
            "<< PROPOSAL_NUMBER >>, " +
            "<< VERSION_NUMBER >>, " +
            "<< BUDGET_PERIOD >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }*/
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER VERSION_NUMBER>> = "
            +" call FN_COPY_PROP_BUDGET_TO_AWARD(" +
            "<< MIT_AWARD_NUMBER >>, " +
            "<< SEQUENCE_NUMBER >>, " +
            "<< AMOUNT_SEQUENCE_NUMBER >>, " +
            "<< PROPOSAL_NUMBER >>, " +
            "<< PROPOSAL_VERSION_NUMBER >>, " +
            "<< BUDGET_PERIOD >>, " +
            "<< UPDATE_TIMESTAMP >>, " +
            "<< UPDATE_USER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if (!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            newVersionNo = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        
        return newVersionNo;
    }//End copyAwardBudget
    
    /** Method used to Insert/Update/Delete Award Budget details
     *
     * @return true for successful Insert/Update/Delete
     * @param cvAwdBudData CoeusVector 
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available.
     */
    public boolean addUpdAwardBudget(CoeusVector cvAwdBudData) throws
    CoeusException, DBException{
        boolean success = false;
        final int TO_BE_POSTED = 10;
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        AwardBudgetHeaderBean awardBudgetHeaderBean =
                                (AwardBudgetHeaderBean)cvAwdBudData.get(0);
        char mode = ((Character)cvAwdBudData.get(2)).charValue();
        procedures.add(addUpdAwardHeader(awardBudgetHeaderBean, mode));
        
        
        CoeusVector cvAwdBudDetails = (CoeusVector)cvAwdBudData.get(1);
        if(cvAwdBudDetails != null && cvAwdBudDetails.size()>0){
            for (int index = 0 ; index < cvAwdBudDetails.size(); index++){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                (AwardBudgetDetailBean)cvAwdBudDetails.get(index);
                if(awardBudgetDetailBean.getAcType() != null){
                    procedures.add(addUpdAwardDetails(awardBudgetDetailBean));
                }
            }//end if
        }//end for
        int budgetStatusCode  = awardBudgetHeaderBean.getBudgetStatusCode();
         
        if(budgetStatusCode == TO_BE_POSTED){
            procedures.add(insertSapFeedDetails(awardBudgetHeaderBean));
        }
        
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                    success = true;
                }
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;
    }
    
    
    //Supporting method for addUpdAwardBudget
    public ProcReqParameter addUpdAwardHeader(AwardBudgetHeaderBean awardBudgetHeaderBean, char mode)
    throws CoeusException ,DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getMitAwardNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getSequenceNumber()));
        param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getAmountSequenceNo()));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getVersionNo()));
        param.addElement(new Parameter("OH_RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getOhRateClassCode()));
        param.addElement(new Parameter("OH_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getOhRateTypeCode()));
        param.addElement(new Parameter("TOTAL_COST",
            DBEngineConstants.TYPE_DOUBLE, ""+awardBudgetHeaderBean.getTotalCost()));
        param.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE, awardBudgetHeaderBean.getStartDate()));            
        param.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_DATE, awardBudgetHeaderBean.getEndDate()));
        param.addElement(new Parameter("STATUS_CODE",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getBudgetStatusCode()));
        param.addElement(new Parameter("DESCRIPTION",
            DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getDescription()));
        param.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getComments()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        /*If the mode is New, Rebudget, Copy the logged in user is the BUDGET_INITIATOR*/
        if(mode == TypeConstants.NEW_MODE || mode == TypeConstants.REBUDGET_MODE ){
            param.addElement(new Parameter("BUDGET_INITIATOR",        
                DBEngineConstants.TYPE_STRING, userId));
        }else{
            param.addElement(new Parameter("BUDGET_INITIATOR",        
                DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getBudgetInitiator()));
        }
        
        param.addElement(new Parameter("AWARD_BUDGET_TYPE_CODE",
            DBEngineConstants.TYPE_STRING, ""+awardBudgetHeaderBean.getAwardBudgetTypeCode()));
        
        if(awardBudgetHeaderBean.isOnOffCampusFlag()){        
            param.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
                DBEngineConstants.TYPE_STRING, "Y"));
        }else{
            param.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
                DBEngineConstants.TYPE_STRING, "N"));
        }
         
        param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getMitAwardNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getAmountSequenceNo()));
        param.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getVersionNo()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, awardBudgetHeaderBean.getUpdateTimestamp()));
        
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getAcType()));
        
         StringBuffer sql = new StringBuffer("call UPDATE_AWARD_BUDGET_INFO(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<AMOUNT_SEQUENCE_NUMBER>> , ");
            sql.append(" <<VERSION_NUMBER>> , ");
            sql.append(" <<OH_RATE_CLASS_CODE>> , ");
            sql.append(" <<OH_RATE_TYPE_CODE>> , ");
            
            sql.append(" <<TOTAL_COST>> , ");
            sql.append(" <<START_DATE>> , ");
            sql.append(" <<END_DATE>> , ");
            sql.append(" <<STATUS_CODE>> , ");
            sql.append(" <<DESCRIPTION>> , ");
            sql.append(" <<COMMENTS>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");        
            sql.append(" <<UPDATE_USER>> , ");
            
            sql.append(" <<BUDGET_INITIATOR>> , ");        
            sql.append(" <<AWARD_BUDGET_TYPE_CODE>> , ");      
            sql.append(" <<ON_OFF_CAMPUS_FLAG>> , ");      
            
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_AMOUNT_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_VERSION_NUMBER>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
            
            return procReqParameter;
    }//End addUpdAwardHeader
    
    //Supporting method for addUpdAwardBudget
    public ProcReqParameter addUpdAwardDetails(AwardBudgetDetailBean awardBudgetDetailBean)
    throws CoeusException ,DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetDetailBean.getMitAwardNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getSequenceNumber()));
        param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getAmountSequenceNo()));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getVersionNo()));
        param.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getLineItemNo()));
        
        param.addElement(new Parameter("COST_ELEMENT",
            DBEngineConstants.TYPE_STRING, awardBudgetDetailBean.getCostElement()));
        
        param.addElement(new Parameter("OBLIGATED_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE, ""+awardBudgetDetailBean.getOblAmount()));
        param.addElement(new Parameter("OBLIGATED_CHANGE_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE, ""+awardBudgetDetailBean.getOblChangeAmount()));
        
        String modifiedByuser = awardBudgetDetailBean.isModifiedByUser() ? 
                                    "Y" : "N";
        param.addElement(new Parameter("MODIFIED_BY_USER",
            DBEngineConstants.TYPE_STRING, modifiedByuser));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        
        
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetDetailBean.getMitAwardNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getAmountSequenceNo()));
        param.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getVersionNo()));
        param.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetDetailBean.getAwLineItemNo()));
        
        //System.out.println("AW_UPDATE_TIMESTAMP:"+awardBudgetDetailBean.getUpdateTimestamp());
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,awardBudgetDetailBean.getUpdateTimestamp()));
         
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, awardBudgetDetailBean.getAcType()));
        
        StringBuffer sql = new StringBuffer("call UPDATE_AWARD_BUDGET_DETAIL(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<AMOUNT_SEQUENCE_NUMBER>> , ");
            sql.append(" <<VERSION_NUMBER>> , ");
            sql.append(" <<LINE_ITEM_NUMBER>> , ");
            sql.append(" <<COST_ELEMENT>> , ");
            
            sql.append(" <<OBLIGATED_AMOUNT>> , ");
            sql.append(" <<OBLIGATED_CHANGE_AMOUNT>> , ");
            sql.append(" <<MODIFIED_BY_USER>> , ");
          
            sql.append(" <<UPDATE_TIMESTAMP>> , ");        
            sql.append(" <<UPDATE_USER>> , ");
            
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
//            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
//            sql.append(" <<AW_AMOUNT_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_VERSION_NUMBER>> , ");
            sql.append(" <<AW_LINE_ITEM_NUMBER>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
            
            return procReqParameter;
        
    }//end addUpdAwardDetails
    
    /** Method used to Update/Delete Award Budget Summary
     *  <li>To fetch the data, it uses UPDATE_AWARD_BUDGET_SUMMARY procedure.
     *
     * @return true for successful Update/Delete
     * @param awardBudgetSummaryBean AwardBudgetSummaryBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available.
     */
    public boolean updDeleteSummaryData(AwardBudgetSummaryBean awardBudgetSummaryBean)
    throws CoeusException ,DBException{
        boolean success = false;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        Vector procedures = new Vector(5,3);
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetSummaryBean.getMitAwardNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetSummaryBean.getSequenceNumber()));
        param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetSummaryBean.getAmountSequenceNumber()));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetSummaryBean.getBudgetVersion()));
        param.addElement(new Parameter("STATUS_CODE",
            DBEngineConstants.TYPE_INT, ""+awardBudgetSummaryBean.getBudgetStatusCode()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetSummaryBean.getMitAwardNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetSummaryBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetSummaryBean.getAmountSequenceNumber()));
        param.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetSummaryBean.getBudgetVersion()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, awardBudgetSummaryBean.getAcType()));
        
        StringBuffer sql = new StringBuffer("call UPDATE_AWARD_BUDGET_SUMMARY(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
//            sql.append(" <<SEQUENCE_NUMBER>> , ");
//            sql.append(" <<AMOUNT_SEQUENCE_NUMBER>> , ");
            sql.append(" <<VERSION_NUMBER>> , ");
            sql.append(" <<STATUS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");        
            sql.append(" <<UPDATE_USER>> , ");
            
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
//            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
//            sql.append(" <<AW_AMOUNT_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_VERSION_NUMBER>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());

            procedures.add(procReqParameter);
            
            if(dbEngine!=null){
                java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                    success = true;
                }
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;
        
    }//End addUpdSummaryData
    
    //Supporting method for addUpdAwardBudget
    public ProcReqParameter insertSapFeedDetails(AwardBudgetHeaderBean awardBudgetHeaderBean)
    throws CoeusException ,DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getMitAwardNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getSequenceNumber()));
        param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getAmountSequenceNo()));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getVersionNo()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        
         StringBuffer sql = new StringBuffer("call UPDATE_AWARD_BUDGET_FEED(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<AMOUNT_SEQUENCE_NUMBER>> , ");
            sql.append(" <<VERSION_NUMBER>> , ");
          
            sql.append(" <<UPDATE_TIMESTAMP>> , ");        
            sql.append(" <<UPDATE_USER>> )");
            
          
            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
            
            return procReqParameter;
    }
}
