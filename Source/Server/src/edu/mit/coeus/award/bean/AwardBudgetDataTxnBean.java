/*
 * AwardBudgetDataTxnBean.java
 *
 * Created on July 14, 2005, 7:42 PM
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.bean.RateClassBean;
import edu.mit.coeus.departmental.bean.DepartmentBudgetFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Connection;
import java.util.TreeSet;
import java.util.Comparator;
/**
 *
 * @author  ajaygm
 */
public class AwardBudgetDataTxnBean {
      // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    private Connection conn = null;
    private static final String DSN = "Coeus";
    private TransactionMonitor transactionMonitor;
    
    /** Creates a new instance of AwardBudgetDataTxnBean */
    public AwardBudgetDataTxnBean() {
        dbEngine = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();
    }
    
    
    /** Method used to get Award Budget Summary Data for the given Award Number.
     * <li>To fetch the data, it uses GET_AWARD_BUDGET_SUMMARY.
     *
     * @return CoeusVector of AwardBudgetSummaryBean
     * @param mitAwardNumber is used to get AwardBudgetSummarybean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardBudgetSummary(String mitAwardNumber)
    throws CoeusException, DBException{
        CoeusVector cvAwardBudgetSummary = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardBudgetSummaryBean awardBudgetSummaryBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BUDGET_SUMMARY( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector messageList = null;
        if(listSize > 0){
            cvAwardBudgetSummary = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardBudgetSummaryBean = new AwardBudgetSummaryBean();
                row = (HashMap)result.elementAt(index);
                awardBudgetSummaryBean.setBudgetVersion(
                    row.get("VERSION_NUMBER") == null ? 0 : Integer.parseInt(row.get("VERSION_NUMBER").toString()));
                awardBudgetSummaryBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardBudgetSummaryBean.setStartDate(
                    row.get("START_DATE") == null ?null : new Date(((Timestamp) row.get("START_DATE")).getTime())); 
                awardBudgetSummaryBean.setExpirationDate(
                    row.get("END_DATE") == null ?null : new Date(((Timestamp) row.get("END_DATE")).getTime())); 
                awardBudgetSummaryBean.setBudgetStatusCode(
                    row.get("BUDGET_STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("BUDGET_STATUS_CODE").toString()));
                awardBudgetSummaryBean.setBudgetStatusDescription((String)row.get("BUDGET_STATUS_DESC"));
                
                awardBudgetSummaryBean.setAwardBudgetTypeCode(
                    row.get("AWARD_BUDGET_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("AWARD_BUDGET_TYPE_CODE").toString()));
                
                awardBudgetSummaryBean.setAwardBudgetTypeDesc(
                (String)row.get("BUDGET_TYPE_DESC"));
                
                awardBudgetSummaryBean.setBudgetAmount(
                    row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));
                
                awardBudgetSummaryBean.setUpdateUser((String)row.get("UPDATE_USER"));
                awardBudgetSummaryBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardBudgetSummaryBean.setAmountSequenceNumber(
                    row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
                cvAwardBudgetSummary.addElement(awardBudgetSummaryBean);
            }
        }
        return cvAwardBudgetSummary;
    }
    
    /** Method used to get Award Budget Copy Data for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_PROPOSAL_BUDGET_LIST.
     *
     * @return CoeusVector of AwardBudgetCopyBean
     * @param mitAwardNumber is used to get AwardBudgetSummarybean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardBudgetCopyData(String awardNumber) 
    throws CoeusException, DBException{
        CoeusVector cvAwardBudgetCopy = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardBudgetCopyBean awardBudgetCopyBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_PROPOSAL_BUDGET_LIST( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector messageList = null;
        if(listSize > 0){
            cvAwardBudgetCopy = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardBudgetCopyBean = new AwardBudgetCopyBean();
                row = (HashMap)result.elementAt(index);
                awardBudgetCopyBean.setProposalNumber((String)row.get("PROPOSAL_NUMBER"));
                    awardBudgetCopyBean.setVersionNumber(
                    row.get("VERSION_NUMBER") == null ? 0 : Integer.parseInt(row.get("VERSION_NUMBER").toString()));
                awardBudgetCopyBean.setBudgetPeriod(
                    row.get("BUDGET_PERIOD") == null ? 0 : Integer.parseInt(row.get("BUDGET_PERIOD").toString()));
                awardBudgetCopyBean.setStartDate(
                    row.get("START_DATE") == null ?null : new Date(((Timestamp) row.get("START_DATE")).getTime()));
                awardBudgetCopyBean.setEndDate(
                    row.get("END_DATE") == null ?null : new Date(((Timestamp) row.get("END_DATE")).getTime())); 
                awardBudgetCopyBean.setTotalCost(
                    row.get("TOTAL_COST") == null ? 0 : Double.parseDouble(row.get("TOTAL_COST").toString()));
                cvAwardBudgetCopy.addElement(awardBudgetCopyBean);
            }
        }
        return cvAwardBudgetCopy;
    }
    
    
    /** Method used to get Award Budget Header Info.
     * <li>To fetch the data, it uses DW_GET_AWARD_BUDGET_HEADER.
     *
     * @return CoeusVector of AwardBudgetHeaderBean
     * @param mitAwardNumber is used to get AwardBudgetSummarybean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public AwardBudgetHeaderBean getAwardBudgetHeader(AwardBudgetSummaryBean awardBudgetSummaryBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardBudgetHeaderBean awardBudgetHeaderBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,awardBudgetSummaryBean.getMitAwardNumber() ));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,""+ awardBudgetSummaryBean.getSequenceNumber()));
        param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,""+ awardBudgetSummaryBean.getAmountSequenceNumber()));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,""+awardBudgetSummaryBean.getBudgetVersion()));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BUDGET_HEADER( <<MIT_AWARD_NUMBER>>, " +
            "<<SEQUENCE_NUMBER>>, " +
            "<<AMOUNT_SEQUENCE_NUMBER>>, " +
            "<<VERSION_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        //Vector messageList = null;
        if(listSize > 0){
            awardBudgetHeaderBean = new AwardBudgetHeaderBean();
            row = (HashMap)result.elementAt(0);
            awardBudgetHeaderBean.setAccountNumber(
                //Modified for Case #2100 - start
                row.get("ACCOUNT_NUMBER") == null ? "" : row.get("ACCOUNT_NUMBER").toString() );
                //Modified for Case #2100 - end
            awardBudgetHeaderBean.setOblDisributableAmount(
                row.get("OBLIGATED_AMOUNT") == null ? 0 : Double.parseDouble(row.get("OBLIGATED_AMOUNT").toString()));    
            awardBudgetHeaderBean.setTotalCost(
                row.get("TOTAL_COST") == null ? 0 : Double.parseDouble(row.get("TOTAL_COST").toString()));    
                
            awardBudgetHeaderBean.setBudgetStatusCode(
                Integer.parseInt(row.get("BUDGET_STATUS").toString()));
            awardBudgetHeaderBean.setBudgetStatusDesc(
                row.get("BUDGET_STATUS_DESCRIPTION") == null ? "" : row.get("BUDGET_STATUS_DESCRIPTION").toString());
                
            if(row.get("AWARD_BUDGET_TYPE_CODE") == null){
                awardBudgetHeaderBean.setAwardBudgetTypeCode(0);
            }else{
                awardBudgetHeaderBean.setAwardBudgetTypeCode(
                    Integer.parseInt(row.get("AWARD_BUDGET_TYPE_CODE").toString()));
            }
            
            awardBudgetHeaderBean.setStartDate(
                row.get("START_DATE") == null ?null : new Date(((Timestamp) row.get("START_DATE")).getTime())); 
            awardBudgetHeaderBean.setEndDate(
                row.get("END_DATE") == null ?null : new Date(((Timestamp) row.get("END_DATE")).getTime())); 
            awardBudgetHeaderBean.setComments(
                row.get("COMMENTS") == null ? "" : row.get("COMMENTS").toString());
            awardBudgetHeaderBean.setDescription((String)row.get("DESCRIPTION"));
            awardBudgetHeaderBean.setMitAwardNumber((String)
                row.get("MIT_AWARD_NUMBER"));                
            awardBudgetHeaderBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
            awardBudgetHeaderBean.setVersionNo(
                 Integer.parseInt(row.get("VERSION_NUMBER").toString()));
            awardBudgetHeaderBean.setAmountSequenceNo(
                 Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
            awardBudgetHeaderBean.setOhRateClassCode(
                 Integer.parseInt(row.get("OH_RATE_CLASS_CODE").toString()));
            awardBudgetHeaderBean.setOhRateTypeCode(
                 Integer.parseInt(row.get("OH_RATE_TYPE_CODE").toString()));
            awardBudgetHeaderBean.setOblChangeAmount(
                row.get("OBLIGATED_CHANGE_AMOUNT") == null ? 0 : Double.parseDouble(row.get("OBLIGATED_CHANGE_AMOUNT").toString())); 
            
            if(row.get("ON_OFF_CAMPUS_FLAG") == null 
               || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("")
               || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("N")){
                   awardBudgetHeaderBean.setOnOffCampusFlag(false);
            }else{
                awardBudgetHeaderBean.setOnOffCampusFlag(true);
            }
                
            awardBudgetHeaderBean.setBudgetInitiator(
                row.get("BUDGET_INITIATOR") == null ? null : row.get("BUDGET_INITIATOR").toString());
            
            awardBudgetHeaderBean.setUpdateTimestamp((Timestamp)
                row.get("UPDATE_TIMESTAMP"));
            awardBudgetHeaderBean.setUpdateUser((String)
                row.get("UPDATE_USER"));
        }
        return awardBudgetHeaderBean;
    }
    
    /** Method used to get Award Budget Details.
     * <li>To fetch the data, it uses GET_AWARD_BUDGET_DETAIL.
     *
     * @return CoeusVector of AwardBudgetSummarybean
     * @param mitAwardNumber is used to get AwardBudgetSummarybean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardBudgetDetails(
                    AwardBudgetSummaryBean awardBudgetSummaryBean, char mode)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        String budgetMode = null;
        CoeusVector cvBudgetDetails = null; 
        AwardBudgetDetailBean awardBudgetDetailBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING,awardBudgetSummaryBean.getMitAwardNumber() ));
        //System.out.println(awardBudgetSummaryBean.getMitAwardNumber());
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,""+ awardBudgetSummaryBean.getSequenceNumber()));
        //system.out.println(awardBudgetSummaryBean.getSequenceNumber());
        param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,""+ awardBudgetSummaryBean.getAmountSequenceNumber()));
        //system.out.println(awardBudgetSummaryBean.getAmountSequenceNumber());
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,""+awardBudgetSummaryBean.getBudgetVersion()));
        //system.out.println(awardBudgetSummaryBean.getBudgetVersion());
        if(mode == TypeConstants.NEW_MODE || mode == TypeConstants.REBUDGET_MODE){
            /*if New/Rebudget set the status as "Y"*/
            budgetMode = "Y";
        }else{
            budgetMode = "N";
        }
        
        param.addElement(new Parameter("IS_NEW_BUDGET",
            DBEngineConstants.TYPE_STRING,""+ budgetMode));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BUDGET_DETAIL( <<MIT_AWARD_NUMBER>>, " +
//            "<<SEQUENCE_NUMBER>>, " +
//            "<<AMOUNT_SEQUENCE_NUMBER>>, " +
            "<<VERSION_NUMBER>>," +
            "<<IS_NEW_BUDGET>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvBudgetDetails = new CoeusVector();
            for(int index = 0 ; index < listSize; index++){
                awardBudgetDetailBean = new AwardBudgetDetailBean();
                row = (HashMap)result.elementAt(index);
                awardBudgetDetailBean.setLineItemNo(
                    Integer.parseInt(row.get("LINE_NUMBER").toString()));
                awardBudgetDetailBean.setCostElement(
                    row.get("COST_ELEMENT") == null ? "" : row.get("COST_ELEMENT").toString());
                awardBudgetDetailBean.setCostElementDescription(
                    row.get("COST_ELEMENT_DESCRIPTION") == null ? "" : row.get("COST_ELEMENT_DESCRIPTION").toString());
                awardBudgetDetailBean.setOblChangeAmount(
                    row.get("CHANGE_AMOUNT") == null ? 0 : Double.parseDouble(row.get("CHANGE_AMOUNT").toString()));    
                awardBudgetDetailBean.setOblAmount(
                    row.get("OBLIGATED_AMOUNT") == null ? 0 : Double.parseDouble(row.get("OBLIGATED_AMOUNT").toString()));    
                awardBudgetDetailBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));                
//                awardBudgetDetailBean.setSequenceNumber(
//                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
//                awardBudgetDetailBean.setAmountSequenceNo(
//                    Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));   
                awardBudgetDetailBean.setVersionNo(
                    Integer.parseInt(row.get("VERSION_NUMBER").toString()));
                awardBudgetDetailBean.setModifiedByUser(
                    row.get("MODIFIED_BY_USER") == "Y" ? true : false);
                
                awardBudgetDetailBean.setUpdateTimestamp((Timestamp)
                    row.get("UPDATE_TIMESTAMP"));
                awardBudgetDetailBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
            
                awardBudgetDetailBean.setAwLineItemNo(
                    Integer.parseInt(row.get("LINE_NUMBER").toString()));
                
                cvBudgetDetails.addElement(awardBudgetDetailBean);
            }
        }
        return cvBudgetDetails;
    }
    
    /**
     *  This method used to get Award Budget Type
     *  <li>To fetch the data, it uses the procedure GET_AWARD_BUDGET_TYPE
     *
     *  @return CoeusVector of all ComboBox beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardBudgetType() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BUDGET_TYPE( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector cvData = null;
        if (listSize > 0){
            cvData = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                row = (HashMap)result.elementAt(rowIndex);
                cvData.addElement(new ComboBoxBean(
                row.get("AWARD_BUDGET_TYPE_CODE").toString(),
                row.get("DESCRIPTION").toString()));
            }
        }
        return cvData;
    }
    
     /**
     *  This method used to get Award Budget Calculated Cost Elements
     *  <li>To fetch the data, it uses the procedure GET_AWARD_BGT_CAL_CE_DESC
     *
     *  @return CoeusVector of Calculated Cost Elements
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardBudgetCalCE() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BGT_CAL_CE_DESC( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector cvData = null;
        if (listSize > 0){
            cvData = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                AwardBudgetDetailBean awardBudgetDetailBean = new AwardBudgetDetailBean();
                row = (HashMap)result.elementAt(rowIndex);
                awardBudgetDetailBean.setCostElement(
                    row.get("COST_ELEMENT") == null ? "" : row.get("COST_ELEMENT").toString());
                
                awardBudgetDetailBean.setCostElementDescription(
                    row.get("DESCRIPTION") == null ? "" : row.get("DESCRIPTION").toString());
                awardBudgetDetailBean.setRateClassType(
                    row.get("RATE_CLASS_TYPE") == null ? "" : row.get("RATE_CLASS_TYPE").toString());
                    
                 if(row.get("ON_OFF_CAMPUS_FLAG") == null 
                    || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("")
                    || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("N")){
                        awardBudgetDetailBean.setOnOffCampusFlag(false);
                 }else{
                     awardBudgetDetailBean.setOnOffCampusFlag(true);
                 }                    
                cvData.addElement(awardBudgetDetailBean);
            }
        }
        return cvData;
    }
    
     /**
     * Gets all Award Budget Cost Elements for the Lookup . The
     * return Vector(Collection) contains cost element,description and etc
     * from OSP$COST_ELEMENT table.
     * <li>To fetch the data, it uses the procedure GET_AWARD_BUDGET_CE_LIST.
     *
     * @return DepartmentBudgetFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardBudgetCEList() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        CostElementsBean costElementsBean = null;
        HashMap row = null;

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BUDGET_CE_LIST( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector ceList = null;
        if (listSize > 0){
            ceList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                costElementsBean = new CostElementsBean();
                row = (HashMap)result.elementAt(rowIndex);
                costElementsBean.setCostElement(
                    (String)row.get("COST_ELEMENT"));
                costElementsBean.setDescription(
                    (String)row.get("DESCRIPTION"));
                costElementsBean.setBudgetCategoryCode(
                    row.get("BUDGET_CATEGORY_CODE")==null ? 0 : Integer.parseInt(row.get("BUDGET_CATEGORY_CODE").toString()));
                costElementsBean.setOnOffCampusFlag(
                    (String)row.get("ON_OFF_CAMPUS_FLAG") == null ? false : row.get("ON_OFF_CAMPUS_FLAG").toString().trim().equalsIgnoreCase("N") ? true : false);
                ceList.add(costElementsBean);
            }//end for  
        }//end if
        return ceList;
    }//End getAwardBudgetCEList
   
     
     /**
     * To fetch the data, it uses the procedure GET_AWARD_BUDGET_CE_DETAILS.
     *
     * @return CostElementsBean CostElementsBean
     * @param costElement String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.  
     */
    public CostElementsBean getAwardBudgetCEDetails(String costElement) 
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        Vector vctResultSet = null;
        Vector param= new Vector();
        param.addElement(new Parameter("COST_ELEMENT",
            DBEngineConstants.TYPE_STRING, costElement));        
        
        if(dbEngine!=null){
           result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BUDGET_CE_DETAILS( <<COST_ELEMENT>>,"
            +" <<OUT STRING DESCRIPTION>>, <<OUT STRING ON_OFF_CAMPUS_FLAG>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        CostElementsBean costElementsBean = null;
        if (recCount >0){
            vctResultSet = new Vector();
            costElementsBean = new CostElementsBean();
            budgetRow = (HashMap) result.elementAt(0);
            costElementsBean.setDescription(
                (String)budgetRow.get("DESCRIPTION"));
            costElementsBean.setOnOffCampusFlag(
                (String)budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().trim().equalsIgnoreCase("N") ? true : false);
        }
       return costElementsBean;
    }                
    
     /**
     * <li>To fetch the data, it uses the procedure GET_AWARD_BUDGET_STATUS.
     *
     * @return CoeusVector map of all Doc Code as key
     * and Doc Type description as value.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public CoeusVector getAwardBudgetStatus() throws DBException{
        Vector result = new Vector(3,2);
        CoeusVector cvStatus = null;
        HashMap hmStatus = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_BUDGET_STATUS ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new DBException("db_exceptionCode.1000");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            cvStatus = new CoeusVector();
            for(int types=0;types<ctypesCount;types++){
                hmStatus = (HashMap)result.elementAt(types);
                cvStatus.addElement(new ComboBoxBean(
                                        hmStatus.get("STATUS_CODE").toString(),
                                        hmStatus.get("DESCRIPTION").toString()));
            }//end for
        }
        return cvStatus;
    }
    
    /**
     *  This method populates combo box with type code and description.
     *
     *  To fetch the data, it uses the procedure GET_OH_RATE_CLASS_LIST.
     *
     *  @return Vector collection of ComboBox beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getOHRateClassList() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_OH_RATE_CLASS_LIST ( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector cvList = null;
        if (listSize > 0){
            cvList = new CoeusVector();
            RateClassBean rateClassBean = null;
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                row = (HashMap)result.elementAt(rowIndex);
                rateClassBean = new RateClassBean();
                rateClassBean.setCode(row.get("RATE_CLASS_CODE") == null ? 
                        "" : row.get("RATE_CLASS_CODE").toString());
                rateClassBean.setDescription(row.get("DESCRIPTION") == null ? 
                        "" : row.get("DESCRIPTION").toString());   
                cvList.addElement(rateClassBean);
            }
        }
        return cvList;
    }        
    
    public CoeusVector getRates(CoeusVector cvData) 
    throws CoeusException, DBException{
        Vector result = null;
        Vector procedures = new Vector(5,3);
        HashMap row = null;
        
        
        
        AwardBudgetHeaderBean awardBudgetHeaderBean = (AwardBudgetHeaderBean)cvData.get(0);
        CoeusVector cvCalcData = (CoeusVector)cvData.get(1);
        int noOfCostElements = cvCalcData.size();
        
        for(int index = 0; index< cvCalcData.size() ;index++){
            AwardBudgetDetailBean awardBudgetDetailBean = 
                                          (AwardBudgetDetailBean)cvCalcData.get(index);
            String costElement = awardBudgetDetailBean.getCostElement();
            procedures.add(getRatesForCE(awardBudgetHeaderBean,costElement));
        }
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    result = dbEngine.executeStoreProcs(procedures,conn);
                    //dbEngine.commit(conn);
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
        
        CoeusVector cvList = null;
        CoeusVector cvEB = null;
        CoeusVector cvOH = null;
        if (result.size() > 0){
            //System.out.println(result);
            cvList = new CoeusVector();
            cvEB = new CoeusVector();
            cvOH = new CoeusVector();
            if(noOfCostElements == 1){
                AwardBudgetRatesBean awardBudgetRatesBean = null;
                
                for(int i = 0 ; i<result.size(); i++){
                    row = (HashMap)result.elementAt(i);

                    awardBudgetRatesBean= new AwardBudgetRatesBean();

                    awardBudgetRatesBean.setCostElement(row.get("COST_ELEMENT") == null ? 
                            "" : row.get("COST_ELEMENT").toString());

                    awardBudgetRatesBean.setOhType(row.get("OH_TYPE") == null ? 
                            "" : row.get("OH_TYPE").toString());   


                    if(row.get("ON_OFF_CAMPUS_FLAG") == null 
                       || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("")
                       || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("N")){
                           awardBudgetRatesBean.setOnOffCampusFlag(false);
                    }else{
                        awardBudgetRatesBean.setOnOffCampusFlag(true);
                    }

                    awardBudgetRatesBean.setRate(
                            row.get("RATE") == null ? 0 : Double.parseDouble(row.get("RATE").toString()) );   


                    if(awardBudgetRatesBean.getOhType().equals("O")){
                        cvOH.addElement(awardBudgetRatesBean);
                    }else if(awardBudgetRatesBean.getOhType().equals("E")){
                        cvEB.addElement(awardBudgetRatesBean);
                    }
                }
            }else {
                for(int rowIndex=0; rowIndex<result.size() ; rowIndex++){
                    Vector vecData = (Vector)result.elementAt(rowIndex);

                    if (vecData.size() > 0){
                        AwardBudgetRatesBean awardBudgetRatesBean = null;

                        for(int i = 0 ; i < vecData.size(); i++){
                            row = (HashMap)vecData.elementAt(i);

                            awardBudgetRatesBean= new AwardBudgetRatesBean();

                            awardBudgetRatesBean.setCostElement(row.get("COST_ELEMENT") == null ? 
                                    "" : row.get("COST_ELEMENT").toString());

                            awardBudgetRatesBean.setOhType(row.get("OH_TYPE") == null ? 
                                    "" : row.get("OH_TYPE").toString());   


                            if(row.get("ON_OFF_CAMPUS_FLAG") == null 
                               || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("")
                               || row.get("ON_OFF_CAMPUS_FLAG").toString().equals("N")){
                                   awardBudgetRatesBean.setOnOffCampusFlag(false);
                            }else{
                                awardBudgetRatesBean.setOnOffCampusFlag(true);
                            }

                            awardBudgetRatesBean.setRate(
                                    row.get("RATE") == null ? 0 : Double.parseDouble(row.get("RATE").toString()) );   


                            if(awardBudgetRatesBean.getOhType().equals("O")){
                                cvOH.addElement(awardBudgetRatesBean);
                            }else if(awardBudgetRatesBean.getOhType().equals("E")){
                                cvEB.addElement(awardBudgetRatesBean);
                            }

                        }//end inner for
                    }//end inner if
                }//end outer for
            }
        }//end outer if
        cvList.add(cvOH);
        cvList.add(cvEB);
        cvList.add(getParameterValue("AWARD_BUDGET_CAMPUS_BASED_ON_CE"));
        return cvList;
    }
    
    
     /**
     *  This method is used to get rates for a CE
     *
     *  To fetch the data, it uses the procedure GET_AWARD_OH_CE_RATES.
     *
     *  @return ProcReqParameter 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getRatesForCE(
    AwardBudgetHeaderBean awardBudgetHeaderBean , String costElement)throws CoeusException, DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
       
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardBudgetHeaderBean.getMitAwardNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getSequenceNumber()));
        //system.out.println(awardBudgetHeaderBean.getStartDate());
        /*param.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_TIMESTAMP, new java.sql.Timestamp(awardBudgetHeaderBean.getStartDate().getTime())));*/
        param.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE, awardBudgetHeaderBean.getStartDate()));
            
        //system.out.println(awardBudgetHeaderBean.getEndDate());
        /*param.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_TIMESTAMP, new java.sql.Timestamp(awardBudgetHeaderBean.getEndDate().getTime())));*/
        param.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_DATE, awardBudgetHeaderBean.getEndDate()));
            
        param.addElement(new Parameter("COST_ELEMENT",
            DBEngineConstants.TYPE_STRING,costElement));
        param.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT, ""+awardBudgetHeaderBean.getOhRateClassCode()));
        /*param.addElement(new Parameter("RATE_CLASS_TYPE",
            DBEngineConstants.TYPE_STRING, ""));*/
        
        if(awardBudgetHeaderBean.isOnOffCampusFlag()){        
            param.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
                DBEngineConstants.TYPE_STRING, "Y"));
        }else{
            param.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
                DBEngineConstants.TYPE_STRING, "N"));
        }
        
         StringBuffer sql = new StringBuffer("call GET_AWARD_OH_CE_RATES(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<START_DATE>> , ");
            sql.append(" <<END_DATE>> , ");
            sql.append(" <<COST_ELEMENT>> , ");
            sql.append(" <<RATE_CLASS_CODE>> , ");
            //sql.append(" <<RATE_CLASS_TYPE>> , ");
            sql.append(" <<ON_OFF_CAMPUS_FLAG>>,");
            sql.append(" <<OUT RESULTSET rset>> )");
          
            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
            
            return procReqParameter;
        
    }        
    
     /**
     * This method is used to get the parameter value for the given module class code
     * @param String parameterName
     * @return String moduleId
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getParameterValue(String parameterName)   
    throws DBException, CoeusException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap modId = null;
        String moduleId = "";
        param.add(new Parameter("PARAM_NAME",
        DBEngineConstants.TYPE_STRING, parameterName));
        if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT STRING MOD_NAME>> = call get_parameter_value (<<PARAM_NAME>>)}",param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }

        if(!result.isEmpty()) {
                modId = (HashMap)result.elementAt(0);
                moduleId = modId.get("MOD_NAME").toString();
        }
        return moduleId;
    }

     /**
     * This method is used to get the lead unit of the award
     * @param String award number
     * @param String sequence number
     * @return String lead unit number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getLeadUnit(String awardNumber,int sequenceNumber)   
    throws DBException, CoeusException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap leadUnitRow = null;
        String leadUnit = "";
        param.add(new Parameter("AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, awardNumber));
        param.add(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+sequenceNumber));
        if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT STRING LEAD_UNIT>> = call fn_get_award_lead_unit (<<AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>)}",param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }

        if(!result.isEmpty()) {
                leadUnitRow = (HashMap)result.elementAt(0);
                leadUnit = leadUnitRow.get("LEAD_UNIT").toString();
        }
        return leadUnit;
    }
    
    public Hashtable checkAwardBudgetRights(Hashtable data){
        Hashtable valuedata = new Hashtable();
        return valuedata;
    }
    //COEUSQA-3937
    public Integer UpdSeqInBugInfo(String awardNumber)   
    throws DBException, CoeusException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap modId = null;
        Integer moduleId = -1;
        param.add(new Parameter("AS_MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, awardNumber));
        if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER RETURN_VALUE>> = call fn_upd_seq_in_awd_bud_info (<<AS_MIT_AWARD_NUMBER>>)}",param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }

        if(!result.isEmpty()) {
                modId = (HashMap)result.elementAt(0);
                moduleId = Integer.parseInt(modId.get("RETURN_VALUE").toString());
        }
        return moduleId;
    }
    //COEUSQA-3937
}
