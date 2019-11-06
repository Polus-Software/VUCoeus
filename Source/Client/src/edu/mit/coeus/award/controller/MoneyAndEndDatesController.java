/*
 * MoneyAndEndDatesController.java
 *
 * Created on May 27, 2004, 8:44 PM
 */

package edu.mit.coeus.award.controller;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  sharathk
 */

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
//import javax.swing.tree.TreePath;

import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.bean.UserInfoBean;
import javax.swing.JScrollPane;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.applet.AppletContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;


public class MoneyAndEndDatesController extends AwardController
implements ActionListener, BeanUpdatedListener, FocusListener{
    
    private MoneyAndEndDatesForm moneyAndEndDatesForm = new MoneyAndEndDatesForm();
    
    private QueryEngine queryEngine = QueryEngine.getInstance();
    
    private CoeusVector cvAwardAmount;
    
    private CoeusMessageResources coeusMessageResources;
    
    private int saveCount;
    
    /** Final Expiration Date is mandatory. Please enter Final Expiration Date. Award - 
     */
    private static final String ENTER_FINAL_EXP_DATE = "awardMoneyAndEndDates_exceptionCode.1165";
    
    /** Obligation Effective Date should be same or prior to Final Expiration Date. Award - 
     */
    private static final String OBL_EFF_DATE_LT_FINAL_EXP_DATE = "awardMoneyAndEndDates_exceptionCode.1166";
    
    /** Obligation Expiration Date should be between Final Expiration Date and Obligation Effective Date. Award - 
     */
    private static final String OBL_EXP_DT_BWN_FI_EXP_DT_OBL_EFF_DT = "awardMoneyAndEndDates_exceptionCode.1167";
    
    /** Obligated Amount cannot be more than Anticipated Total. Award - 
     */
    private static final String OBL_AMT_MORE_THN_ANT_TOT = "awardMoneyAndEndDates_exceptionCode.1168";
    
    /** Total obligated amount in Award Budget is more than Total Award obligated amount
     */
    private static final String OBL_AMT_MORE_THN_TOT_OBL_AMT = "awardMoneyAndEndDates_exceptionCode.1169";
    
    /** Total anticipated amount in Award Budget is more than Total Award anticipated amount
     */
    private static final String ANT_AMT_MORE_THN_TOT_ANT_AMT = "awardMoneyAndEndDates_exceptionCode.1170";
    
    /** There is no summary information available for this award.
     */
    private static final String NO_SUMMARY = "awardMoneyAndEndDates_exceptionCode.1171";
    
    /** Please select an Award to view history.
     */
    private static final String SELECT_AWARD = "awardMoneyAndEndDates_exceptionCode.1172";
    
    private static final String NO_AWARD_AMOUNT = "awardMoneyAndEndDates_exceptionCode.1173";
    private static final char PRINT_BUDGET_HIERARCHY = 'b';
    private static final char PRINT_BUDGET_MODIFICATION = 'f';
    private static final String SERVLET = "/AwardMaintenanceServlet";
    private static final String PRINT_SERVLET = "/ReportConfigServlet";
    
    private static final String AWARD_BUDGET_FLAG = "Award Budget";

    //Award Budget Enhancment Start 1
    /*private static final String BRIEF_AWARD_BUDGET_SELECTED = "BRIEF_AWARD_BUDGET_SELECTED";
    private static final String DETAIL_AWARD_BUDGET_SELECTED = "DETAIL_AWARD_BUDGET_SELECTED";*/
    private static final String NO_PRIVILEGE = "awardBudgetSummary_exceptionCode.2031";
    //Award Budget Enhancment End 1
    
    private static final String ROOT_MIT_NUMBER_NOT_AVAILABLE = "awardMoneyAndEndDates_exceptionCode.1176";
    
    // Case 1822 Award Amount FNA Distribution Start
    private static final char GET_PARAM_FNA_DATA='l';
    private static final String SELECT_ROW ="selectRow_exceptionCode.2007";
    private static final String FNA_DIST_MANDATORY = "M";
    private static final String FNA_DIST_DISABLED = "D";
    private static final String TOTAL_FNA_NOT_EQUAL="awardF&A_exceptionCode.2009";
    private double fnaTotalAmount;
    private boolean validationRequired = false;   
    // Case 1822 Award Amount FNA Distribution End
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrpn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String EMPTY_STRING = "";
    private DateUtils dateUtils;
    private SimpleDateFormat simpleDateFormat;
    private static final String ENTER_TRANSACTION_TYPE="award_moneyenddates_exceptionCode.1001";
    private static final String ENTER_NOTICE_DATE="award_moneyenddates_exceptionCode.1002";
    private static final String ENTER_COMMENTS="award_moneyenddates_exceptionCode.1003";
    private static final String ENTER_VALID_NOTICE_DATE="award_moneyenddates_exceptionCode.1004";
    private Color disabledBackground;
    private AwardBaseBean selectedAwardBaseBean;
    private boolean parameterExist;
    private static final char GET_PARAMETER_VALUE='r';
    private CoeusAppletMDIForm mdiForm;
             static final String connect = CoeusGuiConstants.CONNECTION_URL +
    "/AwardMaintenanceServlet";
    private final char CREATE_AWARD_BUDGET_FOR_RAFT ='i';
    private final char MODIFY_AWARD_BUDGET_FOR_RAFT ='j';
    private final char VIEW_AWARD_BUDGET_FOR_RAFT ='s';
        private boolean createAwardBudget;
        private boolean modifyAwardBudget;
        private boolean viewAwardBudget;
        private boolean p;
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    /** Creates a new instance of MoneyAndEndDatesController */
    public MoneyAndEndDatesController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrpn = new JScrollPane(moneyAndEndDatesForm);
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrpn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrpn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        selectedAwardBaseBean = awardBaseBean;
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        setFunctionType(functionType);
        initComponents();
        registerComponents();
    }
    
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        dateUtils = new DateUtils();
        Color disabledBackground = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    }
    
    /**
     * saves the Form Data.
     */
    public void saveFormData() {
        CoeusVector cvAwardAmount = moneyAndEndDatesForm.awardAmountTreeTable.getBeans();
        if(cvAwardAmount == null || cvAwardAmount.size() == 0) return ;

        int size = cvAwardAmount.size();
        AwardAmountInfoBean awardAmountInfoBean;
        String acType;
        try{
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        populateAmtTransactionDetails(cvAwardAmount);
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        for(int index = 0; index < size; index++) {
            awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(index);
            acType = awardAmountInfoBean.getAcType();
            if(acType != null && (  
            acType.equals(AwardAmountTreeTable.UPDATE_ANT_CHANGE) ||
            acType.equals(AwardAmountTreeTable.UPDATE_OBL_CHANGE) ||
            acType.equals(AwardAmountTreeTable.UPDATE_DATE) ||
            acType.equals(AwardAmountTreeTable.UPDATE_INDIRECT) ||
            acType.equals(TypeConstants.UPDATE_RECORD)
            )) {
                //bean has been modified. update to query Engine
                //awardAmountInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                //queryEngine.update(queryKey, awardAmountInfoBean);

                awardAmountInfoBean.setAcType(TypeConstants.INSERT_RECORD);
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                if(awardAmountInfoBean.getAwardAmountTransaction() !=null){
                    awardAmountInfoBean.getAwardAmountTransaction().setAcType(TypeConstants.INSERT_RECORD);
                }
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
                //queryEngine.insert(queryKey, awardAmountInfoBean);

                //Bug Fix : 1179 - START
                AwardAmountInfoBean awdAmtInfoBean = (AwardAmountInfoBean)ObjectCloner.deepCopy(awardAmountInfoBean);
                awdAmtInfoBean.setEomProcessFlag(false);  // ** ASU
                queryEngine.setData(queryKey, AwardAmountInfoBean.class, awdAmtInfoBean);
                //Bug Fix : 1179 - END

            }//End if acType
            //bug fix : in new award, new child copied. save confirmation was displayed all the time.
            else if(acType != null && acType.startsWith(AwardAmountTreeTable.UPDATED_LAST_TIME)
            ) {
                //check if really modified before saving to query engine
                Equals eqMitNum = new Equals("mitAwardNumber", awardAmountInfoBean.getMitAwardNumber());
                CoeusVector cvQueryBean = queryEngine.executeQuery(queryKey, AwardAmountInfoBean.class, eqMitNum);
                AwardAmountInfoBean queryBean = (AwardAmountInfoBean)cvQueryBean.get(0);
                StrictEquals strictEquals = new StrictEquals();
                //will have to check without considering the ac type.
                queryBean.setAcType(awardAmountInfoBean.getAcType());
                queryBean.setChildren(awardAmountInfoBean.getChildren());
                queryBean.setChildCount(awardAmountInfoBean.getChildCount());
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                queryBean.setAwardAmountTransaction(awardAmountInfoBean.getAwardAmountTransaction());
               //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
                if(!strictEquals.compare(queryBean, awardAmountInfoBean)) {
                    awardAmountInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                    if(awardAmountInfoBean.getAwardAmountTransaction() !=null){
                        awardAmountInfoBean.getAwardAmountTransaction().setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
                    awardAmountInfoBean.setEomProcessFlag(false);  // ** ASU
                    queryEngine.update(queryKey, awardAmountInfoBean);
                }
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                else if(awardAmountInfoBean.getAwardAmountTransaction() !=null 
                    && awardAmountInfoBean.getAwardAmountTransaction().getAcType()!=null
                    && !awardAmountInfoBean.getAwardAmountTransaction().getAcType().startsWith(AwardAmountTreeTable.UPDATED_LAST_TIME)){
                    queryEngine.update(queryKey, awardAmountInfoBean);
                 }
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            }else if(acType != null && acType.equals(TypeConstants.INSERT_RECORD)) {
                //New Award Mode. Insert Record.
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                 if(awardAmountInfoBean.getAwardAmountTransaction() !=null){
                    awardAmountInfoBean.getAwardAmountTransaction().setAcType(TypeConstants.INSERT_RECORD);
                }
                 //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
                awardAmountInfoBean.setEomProcessFlag(false);  // ** ASU
                queryEngine.insert(queryKey, awardAmountInfoBean);
            }
            
            //set back the old Ac Type
            awardAmountInfoBean.setAcType(acType);
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            if(awardAmountInfoBean.getAwardAmountTransaction() != null){
                awardAmountInfoBean.setAcType(acType);
            }
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        }//End for award amount
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void setSaveCount(int saveCount) {
        this.saveCount = saveCount;
        moneyAndEndDatesForm.awardAmountTreeTable.setSaveCount(saveCount);
    }
    
    /**
     * displays the Form which is being controlled.
     */
    public void display() {
    }
    
    /**
     * perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
 
        if(getFunctionType() == DISPLAY_MODE) {
            moneyAndEndDatesForm.btnPrintMod.setVisible(false);
        }
        //Case Id 1822 Start
        try{
            if(getParameterData().trim().equalsIgnoreCase("D")){
                moneyAndEndDatesForm.btnFNA.setVisible(false);
            }
        }catch (Exception ex) {
           ex.printStackTrace();
           CoeusOptionPane.showErrorDialog(ex.getMessage());
            
        }//Case Id 1822 End
    }

    /** Case 1822 Award Amount FNA Distribution  Start
     */
  
    
     private String getParameterData() throws CoeusClientException{
        String data = null;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_PARAM_FNA_DATA);
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response== null){
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }
        if(response.isSuccessfulResponse()){
            data = (String)response.getDataObject();
        }else {
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            
        }
        return data ;
    }
    /** Case 1822 Award Amount FNA Distribution  End
     */  
    
    /**
     * validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() {
        //Case 1822 start to get the award_fna_parameter value
        String paramData =null;
        if(!validationRequired){
            try{
                paramData=getParameterData();
            }catch(Exception ex){
                ex.printStackTrace();
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
        }
        //Case 1822 end
        
        CoeusVector cvAmountInfo = moneyAndEndDatesForm.awardAmountTreeTable.getBeans();
        if(cvAmountInfo == null || cvAmountInfo.size() == 0) {
            CoeusOptionPane.showErrorDialog("No money and end Dates. \n May be corrupt Data.");
            return false;
        }
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        if(isTransactionModified(cvAmountInfo) || getFunctionType() == NEW_AWARD){
            ComboBoxBean comboBoxBean = (ComboBoxBean)moneyAndEndDatesForm.cmbTransactionType.getSelectedItem();
            if(comboBoxBean.getCode().equals("")){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_TRANSACTION_TYPE));
                setRequestFocusInThread(moneyAndEndDatesForm.cmbTransactionType);
                return false;
            }
        }
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        AwardAmountInfoBean selectedBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
//        if(!validateBeginDate(selectedBean)){
//            return false;
//        }
        
        if(!moneyAndEndDatesForm.awardAmountTreeTable.isValueSet()) { 
            //some error occured while setting data.
            moneyAndEndDatesForm.awardAmountTreeTable.resetValue();
            return false;
        }
        
        AwardAmountInfoBean awardAmountInfoBean;
        int size = cvAmountInfo.size();
        for(int index = 0; index < size; index++) {
            awardAmountInfoBean = (AwardAmountInfoBean)cvAmountInfo.get(index);
            //validation for final expiration date
            if(awardAmountInfoBean.getFinalExpirationDate() == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_FINAL_EXP_DATE) + awardAmountInfoBean.getMitAwardNumber() );
                
                //Bug Fix:1108 Start 1
                moneyAndEndDatesForm.awardAmountTreeTable.gotoMITAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
                moneyAndEndDatesForm.awardAmountTreeTable.setRequestFocusInThread(selectedRow, AwardAmountTreeTable.FINAL_EXP_COL);
                //Bug Fix:1108 End 1
                
                //Commented for bug fix 1108
//                if(selectedRow != -1){
//                    moneyAndEndDatesForm.awardAmountTreeTable.editCellFor(awardAmountInfoBean, AwardAmountTreeTable.FINAL_EXP_COL);
//                }
                
                return false;
            }
            //validation for obligation effective date
            
            if(awardAmountInfoBean.getCurrentFundEffectiveDate() != null &&
            awardAmountInfoBean.getCurrentFundEffectiveDate().after(awardAmountInfoBean.getFinalExpirationDate())) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OBL_EFF_DATE_LT_FINAL_EXP_DATE) + awardAmountInfoBean.getMitAwardNumber() );
                
                //Bug Fix:1108 Start 2
                moneyAndEndDatesForm.awardAmountTreeTable.gotoMITAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
                moneyAndEndDatesForm.awardAmountTreeTable.setRequestFocusInThread(selectedRow, AwardAmountTreeTable.OBL_EFF_DATE_COL);
                //Bug Fix:1108 End 2
                
                return false;
            }
           
            //validation for obligation expiration date
            /*Bug Fix,for avoiding the exception*/
              if((awardAmountInfoBean.getObligationExpirationDate() != null && 
                awardAmountInfoBean.getObligationExpirationDate().after(awardAmountInfoBean.getFinalExpirationDate())) ||
                (awardAmountInfoBean.getCurrentFundEffectiveDate() != null && awardAmountInfoBean.getObligationExpirationDate() != null && 
                awardAmountInfoBean.getObligationExpirationDate().before(awardAmountInfoBean.getCurrentFundEffectiveDate())) ) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OBL_EXP_DT_BWN_FI_EXP_DT_OBL_EFF_DT) + awardAmountInfoBean.getMitAwardNumber() );
                    
                    //Bug Fix:1108 Start 3
                    moneyAndEndDatesForm.awardAmountTreeTable.gotoMITAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                    int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
                    moneyAndEndDatesForm.awardAmountTreeTable.setRequestFocusInThread(selectedRow, AwardAmountTreeTable.OBL_EXP_DATE_COL);
                    //Bug Fix:1108 End 3
                    
                    return false;
                }
            
            //validation for obligated total
            if(awardAmountInfoBean.getAmountObligatedToDate() > awardAmountInfoBean.getAnticipatedTotalAmount()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OBL_AMT_MORE_THN_ANT_TOT) + awardAmountInfoBean.getMitAwardNumber() );
                
                //Bug Fix:1108 Start 4
                moneyAndEndDatesForm.awardAmountTreeTable.gotoMITAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
                moneyAndEndDatesForm.awardAmountTreeTable.setRequestFocusInThread(selectedRow, AwardAmountTreeTable.OBL_CHANGE_COL);
                //Bug Fix:1108 End 4
                
                return false;
            }
            
            if(syncAwardBudget(awardAmountInfoBean, EMPTY) == false) {
                return false;
            }
            
            //Case 1822 Award FNA Start
            
            /**This validation checks for the award_fna_distribution parameter
             *value if the value is 'M' and if the anticipate change column 
             *is non-zero value and if the total anticipated is same as total distributed. 
             */  
            
                if(!validationRequired){
                    if(FNA_DIST_MANDATORY.equals(paramData.trim())){
                        //if(awardAmountInfoBean.getAnticipatedChange() > 0.00){
                        
                        //Case 1859 Start
                        if(awardAmountInfoBean.getAcType()!=null && awardAmountInfoBean.getAnticipatedChange() > 0.00){
                        //Case 1859 End    
                            if(awardAmountInfoBean.getAnticipatedTotalAmount()!=awardAmountInfoBean.getTotalFNACost()){
                               moneyAndEndDatesForm.awardAmountTreeTable.gotoMITAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(TOTAL_FNA_NOT_EQUAL)+awardAmountInfoBean.getMitAwardNumber());                               
                                return true;
                            }
                        }

                    }
                }
            //Case Award FNA End
            
        }//End for

        
        return true;
    }
    
    private boolean syncAwardBudget(AwardAmountInfoBean awardAmountInfoBean, String flag) {
		
        //sync award budget only if total obligated or total anticipated is modified.
        if(awardAmountInfoBean.getAcType() != null &&
        (awardAmountInfoBean.getAcType().equals(AwardAmountTreeTable.UPDATE_ANT_CHANGE) ||
        awardAmountInfoBean.getAcType().equals(AwardAmountTreeTable.UPDATE_OBL_CHANGE) ||
        awardAmountInfoBean.getAcType().equals(TypeConstants.UPDATE_RECORD)
        //Bug Fix : 1109 - START
        || awardAmountInfoBean.getAcType().equals(TypeConstants.INSERT_RECORD)
        //Bug Fix : 1109 - END
        )) {
            
            double oblTotal, antTotal;
            String awardNum;
            
            oblTotal = awardAmountInfoBean.getAmountObligatedToDate();
            antTotal = awardAmountInfoBean.getAnticipatedTotalAmount();
            awardNum = awardAmountInfoBean.getMitAwardNumber();
            
            
            //if(awardAmountInfoBean.getSequenceNumber() != awardBaseBean.getSequenceNumber()) {
            //    awardAmountInfoBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            //}
            
            
            Equals eqMitAwrdNum = new Equals("mitAwardNumber", awardAmountInfoBean.getMitAwardNumber());
            Equals eqLINum = new Equals("lineItemNumber", new Integer(1));
            CoeusVector cvAwardBudget;
            try{
                //cvAwardBudget = queryEngine.executeQuery(queryKey, AwardBudgetBean.class, eqMitAwrdNum);
                cvAwardBudget = queryEngine.executeQuery(queryKey, AwardBudgetBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvAwardBudget == null || cvAwardBudget.size() == 0) {
                    //not retreived earlier. retrieve now.
                    //0th element is CoeusVector, 1st is String containing default cost element.
                    cvAwardBudget = (CoeusVector)getAwardBudget(awardAmountInfoBean.getMitAwardNumber()).get(0);
                }
                
                if(cvAwardBudget != null && cvAwardBudget.size() > 0) {
                    AwardBudgetBean awardBudgetBean = (AwardBudgetBean)cvAwardBudget.get(0);
                    if(awardAmountInfoBean.getSequenceNumber() != awardBudgetBean.getSequenceNumber()) {
                        int seqNum = awardAmountInfoBean.getSequenceNumber();
                        for(int awrdBgtIndx = 0; awrdBgtIndx < cvAwardBudget.size(); awrdBgtIndx++) {
                            awardBudgetBean = (AwardBudgetBean)cvAwardBudget.get(awrdBgtIndx);
                            awardBudgetBean.setSequenceNumber(seqNum);
                            queryEngine.update(queryKey, awardBudgetBean);
                        }//end for set sequence number
                    }//End if sequence number
                    
                    CoeusVector cvAwardBgt = cvAwardBudget.filter(eqLINum);
                    AwardBudgetBean awardBudgetBeanLI = (AwardBudgetBean)cvAwardBgt.get(0);
                    
                    awardBudgetBean = (AwardBudgetBean)cvAwardBudget.get(0);
                    //checking for modification already done above.
                    //check if obligated is modified.
                    double sumObligated = 0;
                    sumObligated = cvAwardBudget.sum("obligatedAmount");
                    if(! awardAmountInfoBean.getAcType().equals(AwardAmountTreeTable.UPDATE_ANT_CHANGE) &&
                    oblTotal != sumObligated) {
                        if(awardBudgetBeanLI.getObligatedAmount() < (sumObligated - oblTotal) &&
                        cvAwardBudget.size() != 1) {
                            //display msg only if called from awrd budget
                            if(!flag.equals(AWARD_BUDGET_FLAG)) {
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OBL_AMT_MORE_THN_TOT_OBL_AMT));
                                
                                //Bug Fix:1108 Start 5
                                int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
                                moneyAndEndDatesForm.awardAmountTreeTable.setRequestFocusInThread(selectedRow, AwardAmountTreeTable.OBL_CHANGE_COL);
                                //Bug Fix:1108 End 5
                                
                                return false;
                            }
                        }else {
                            awardBudgetBeanLI.setObligatedAmount(awardBudgetBeanLI.getObligatedAmount() + (oblTotal - sumObligated));
                        }//end if - else
                    }//end if obligated change
                    
                    double sumAnticipated = 0;
                    sumAnticipated = cvAwardBudget.sum("anticipatedAmount");
                    if(! awardAmountInfoBean.getAcType().equals(AwardAmountTreeTable.UPDATE_OBL_CHANGE) &&
                    antTotal != sumAnticipated) {
                        if(awardBudgetBeanLI.getAnticipatedAmount() < (sumAnticipated - antTotal) &&
                        cvAwardBudget.size() != 1) {
                            //display msg only if called from awrd budget
                            if(!flag.equals(AWARD_BUDGET_FLAG)) {
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ANT_AMT_MORE_THN_TOT_ANT_AMT));
                                
                                //Bug Fix:1108 Start 6
                                int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
                                moneyAndEndDatesForm.awardAmountTreeTable.setRequestFocusInThread(selectedRow, AwardAmountTreeTable.ANT_CHANGE_COL);
                                //Bug Fix:1108 End 6
                                
                                return false;
                            }
                        }else {
                            awardBudgetBeanLI.setAnticipatedAmount(awardBudgetBeanLI.getAnticipatedAmount() + (antTotal - sumAnticipated));
                        }//end if - else
                    }//end if Anticipated Change
                    
                }//End if cvAwardBudget size > 0
                //End Sync Award Budget
                
            }catch (Exception coeusException) {
                coeusException.printStackTrace();
                return false;
            }
        }//End if award amount updated
        //Every thing went well
        return true;
    }

    /**
     * This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     */
    public void setFormData(Object data) {
        if(! (data instanceof AwardBaseBean)) return ;
        
        try{
            cvAwardAmount = queryEngine.executeQuery(queryKey, AwardAmountInfoBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvAwardAmount == null || cvAwardAmount.size() < 1){
                return ;
            }
            //Equals eqAwardNum = new Equals("mitAwardNumber", awardBaseBean.getMitAwardNumber());
            Equals eqSeqNum = new Equals("sequenceNumber", new Integer(awardBaseBean.getSequenceNumber()));
            //And awardNumAndSeqNum = new And(eqAwardNum, eqSeqNum);
            
            Equals eqParent = new Equals("parentMitAwardNumber", "000000-000");
            //And awardNumAndSeqNum = new And(eqParent, eqSeqNum);
            //Added for Case 4031 - Unknown error while scrolling the awards list -Start
            CoeusVector cvRootAmountInfo = cvAwardAmount.filter(eqParent);
            if(cvRootAmountInfo == null || cvRootAmountInfo.size() == 0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ROOT_MIT_NUMBER_NOT_AVAILABLE));
                return;
            }
            //Added for Case 4031 - Unknown error while scrolling the awards list -End
            AwardAmountInfoBean root = (AwardAmountInfoBean)cvAwardAmount.filter(eqParent).get(0);//should always return a vector with 1 item
            
            //Bug Fix: 1410 Start
            //Changed the signature of set beans for the bug fix
            //moneyAndEndDatesForm.awardAmountTreeTable.setBeans(cvAwardAmount, root);
            //JIRA COEUSQA-2871 - START
            CoeusVector cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvParameters != null && cvParameters.size()>0){
                int accountNumberMaxLength;
                CoeusVector cvFiltered = null;
                //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
                cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                if(cvFiltered != null && cvFiltered.size() > 0){
                    CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
                    accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                    moneyAndEndDatesForm.awardAmountTreeTable.setMaxAccountNumberLength(accountNumberMaxLength);
                }
            }
            //JIRA COEUSQA-2871 - END
            moneyAndEndDatesForm.awardAmountTreeTable.setBeans(cvAwardAmount, root, getFunctionType());
            //Bug Fix: 1410 End
            
            moneyAndEndDatesForm.awardAmountTreeTable.setAwardColor(awardBaseBean.getMitAwardNumber(), Color.blue);
            moneyAndEndDatesForm.awardAmountTreeTable.setEditable(!(getFunctionType() == DISPLAY_MODE));
            
            //Bug Fix: 1136 Start.
            moneyAndEndDatesForm.awardAmountTreeTable.gotoMITAwardNumber(awardBaseBean.getMitAwardNumber());
            //Bug Fix: 1136 End.
            
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            CoeusVector cvTransactionTypes = queryEngine.getDetails(queryKey,KeyConstants.AWARD_TRANSACTION_TYPES); 
            ComboBoxBean comboBoxBean = new ComboBoxBean("","");
            cvTransactionTypes.add(0, comboBoxBean);
            moneyAndEndDatesForm.cmbTransactionType.setModel(new DefaultComboBoxModel(cvTransactionTypes));
            
            AwardAmountInfoBean awardAmountInfoBean = null;
            if(getFunctionType() == DISPLAY_MODE){
                if(cvAwardAmount != null && selectedAwardBaseBean!=null){
                    for(int i=0; i<cvAwardAmount.size(); i++){
                        awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(i);
                        if(awardAmountInfoBean.getMitAwardNumber().equals(selectedAwardBaseBean.getMitAwardNumber())){
                            break;
                        }
                    }
                }
            }else{
                awardAmountInfoBean = getModifiedAmountInfoBean(cvAwardAmount);
            }
            
            if((getFunctionType() == CORRECT_AWARD || getFunctionType() == DISPLAY_MODE)
                &&  awardAmountInfoBean!=null && awardAmountInfoBean.getAwardAmountTransaction()!=null ){
                AwardAmountTransactionBean awardAmountTransactionBean =
                        awardAmountInfoBean.getAwardAmountTransaction();
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(Integer.toString(awardAmountTransactionBean.getTransactionTypeCode()));
                comboBoxBean.setDescription(awardAmountTransactionBean.getTransactionTypeDescription());
                moneyAndEndDatesForm.cmbTransactionType.setSelectedItem(comboBoxBean);
                moneyAndEndDatesForm.txtArComments.setText(awardAmountTransactionBean.getComments());
                if ( awardAmountTransactionBean.getNoticeDate() != null ){
                    moneyAndEndDatesForm.txtNoticeDate.setText(dateUtils.formatDate(
                             awardAmountTransactionBean.getNoticeDate().toString(),"dd-MMM-yyyy"));
                }
            }else{
                moneyAndEndDatesForm.txtNoticeDate.setText(EMPTY_STRING);
                moneyAndEndDatesForm.txtArComments.setText(EMPTY_STRING);
            }
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            
            if(getFunctionType() == DISPLAY_MODE) {
                moneyAndEndDatesForm.awardAmountTreeTable.setChangeVisible(false);
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                moneyAndEndDatesForm.cmbTransactionType.setEnabled(false);
                moneyAndEndDatesForm.txtNoticeDate.setBackground(disabledBackground);
                moneyAndEndDatesForm.txtNoticeDate.setDisabledTextColor(Color.black);
                moneyAndEndDatesForm.txtNoticeDate.setEnabled(false);
                
                moneyAndEndDatesForm.txtArComments.setBackground(disabledBackground);
                moneyAndEndDatesForm.txtArComments.setDisabledTextColor(Color.black);
                moneyAndEndDatesForm.txtArComments.setEnabled(false);
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /**
     * returns the form data
     * @return the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /**
     * returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public Component getControlledUI() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 2
        //return moneyAndEndDatesForm;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrpn = new JScrollPane(moneyAndEndDatesForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrpn;
        //Bug Fix:Performance Issue (Out of memory) End 2
    }
    
    /**
     * registers GUI Components with event Listeners.
     */
    public void registerComponents() {
        Component[] components = {moneyAndEndDatesForm.cmbTransactionType,
            moneyAndEndDatesForm.txtNoticeDate, moneyAndEndDatesForm.txtArComments};
        ScreenFocusTraversalPolicy screenFocusTraversalPolicy =
                new ScreenFocusTraversalPolicy(components);
        moneyAndEndDatesForm.setFocusTraversalPolicy(screenFocusTraversalPolicy);
        moneyAndEndDatesForm.setFocusCycleRoot(true);
        
        moneyAndEndDatesForm.btnBudget.addActionListener(this);
        try {
            if(parameterExist){

            createAwardBudget=getCreateAwardBudgetRight();
            modifyAwardBudget=getModifyAwardBudgetRight();
            viewAwardBudget=getViewAwardBudgetRight();

            if(createAwardBudget || modifyAwardBudget ||viewAwardBudget)
            {
              moneyAndEndDatesForm.btnBudget.setEnabled(true);
            }
             }}
        catch (Exception ex) {
            Logger.getLogger(MoneyAndEndDatesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        moneyAndEndDatesForm.btnExpandAll.addActionListener(this);
        moneyAndEndDatesForm.btnGoTo.addActionListener(this);
        moneyAndEndDatesForm.btnHistory.addActionListener(this);
        moneyAndEndDatesForm.btnMedusa.addActionListener(this);
        moneyAndEndDatesForm.btnPrint.addActionListener(this);
        moneyAndEndDatesForm.btnPrintMod.addActionListener(this);
        moneyAndEndDatesForm.btnSummary.addActionListener(this);
        
        //#3857 -- start
        moneyAndEndDatesForm.btnAwardActionSummary.addActionListener(this);
       //#3857 -- end
        
        /** Case 1822 Award Amount FNA Distribution  Start
         */
        moneyAndEndDatesForm.btnFNA.addActionListener(this);
        /** Case 1822 Award Amount FNA Distribution End
        */
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        moneyAndEndDatesForm.txtNoticeDate.addFocusListener(this);
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        addBeanUpdatedListener(this, AwardDetailsBean.class);
        
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            blockEvents(true);
            
            Object source = actionEvent.getSource();
            if(source.equals(moneyAndEndDatesForm.btnBudget)) {
 // for Raft parameter checking Start
             parameterExist=fetchParameterValue();
            if(parameterExist){
                 openUrlCertify();
                }
                //Award Budget Enhancment Start 2
            else{
                    showAwardBudget();
                }
                //showBudget();
                //Award Budget Enhancment End 2
                 
            }
            // Case 1822 Award Amount FNA Start
            else if(source.equals(moneyAndEndDatesForm.btnFNA)) {
                showFNA();
            // Case 1822 Award Amount FNA End
            }else if(source.equals(moneyAndEndDatesForm.btnExpandAll)) {
                expandAll();
            }else if(source.equals(moneyAndEndDatesForm.btnGoTo)) {
                showGoto();
            }else if(source.equals(moneyAndEndDatesForm.btnHistory)) {
                showHistory();
            }else if(source.equals(moneyAndEndDatesForm.btnMedusa)) {
                showMedusa();
            }else if(source.equals(moneyAndEndDatesForm.btnSummary)) {
                showSummary();
            }else if(source.equals(moneyAndEndDatesForm.btnPrint)) {
                createReport(REPORT_BUDGET_HIERARCHY);
                
            }else if (source.equals(moneyAndEndDatesForm.btnPrintMod)){
                createReport(REPORT_MODIFICATION);
//#3857 -- start
            } else if(source.equals(moneyAndEndDatesForm.btnAwardActionSummary)) {
                showAwardActionSummary();
            }
       //#3857 -- ends
        } catch (Exception ex) {
            Logger.getLogger(MoneyAndEndDatesController.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            blockEvents(false);
        }
    }
    
    //Award Budget Enhancment Start 3
    private void showAwardBudget(){
        // Popup the option form and select the option. Depending upon the option,
        // select the type of Budget
        try{
            /*edu.mit.coeus.award.gui.AwardBudgetOptionForm form = new edu.mit.coeus.award.gui.AwardBudgetOptionForm();
            form.display();
            String value = form.getSelectedItem();*/
            validationRequired = true;
            moneyAndEndDatesForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            
            int parameterForBudget = (getParameterForBudget()).intValue();
            /*if(value.equals(BRIEF_AWARD_BUDGET_SELECTED)){
                showBudget();
            }else if(value.equals(DETAIL_AWARD_BUDGET_SELECTED)){
                showDetailBudget();
            }*/
            if(parameterForBudget == 0){
                moneyAndEndDatesForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                showDetailBudget();
            }else if(parameterForBudget == 1){
                moneyAndEndDatesForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                showBudget();
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            moneyAndEndDatesForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    /** Case 1822 Award Amount FNA Distribution  Start
     */    
    private void showFNA() {
        validationRequired = false;
        AwardAmountInfoBean selectedBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
        if(selectedBean==null){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW));
            return ;
        }
        BeanEvent beanEvent = new BeanEvent();
        CoeusVector cvData = new CoeusVector();
        beanEvent.setBean(selectedBean);
        beanEvent.setSource(this);
        beanEvent.setMessageId(SHOW_AWARD_FNA);
        moneyAndEndDatesForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fireBeanUpdated(beanEvent);
    }
  /** Case 1822 Award Amount FNA Distribution  End
     */       
    
    private void showDetailBudget() throws CoeusException{
        moneyAndEndDatesForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        
        AwardAmountInfoBean selectedBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
        if(selectedBean == null) {
            CoeusOptionPane.showInfoDialog("Select Row");
            return;
        }
        
        /**validate and sync the Award budget - This functionality is 
         *same for descriptive and detail Award Budget
         */
        if (validate()) {
            CoeusVector cvRights = checkUserRights();
            boolean canModify = ((Boolean)cvRights.get(0)).booleanValue();
            
            if(canModify){
                syncAwardBudget(selectedBean, AWARD_BUDGET_FLAG);
                BeanEvent beanEvent = new BeanEvent();
                CoeusVector cvData = new CoeusVector();
                
                //Fix for locking bug Start 1
                cvData.add(cvRights.get(1));
                cvData.add(awardBaseBean.getMitAwardNumber());
                //Fix for locking bug End 1
                
                beanEvent.setBean(selectedBean);
                
                //Fix for locking bug Start 2
                //beanEvent.setObject(cvRights.get(1));
                beanEvent.setObject(cvData);
                //Fix for locking bug End 2
                
                beanEvent.setSource(this);
                beanEvent.setMessageId(SHOW_AWARD_BUDGET);
                moneyAndEndDatesForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                fireBeanUpdated(beanEvent);
            }else{
                CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey(NO_PRIVILEGE));
            }
        }
    }
    
    private CoeusVector checkUserRights() throws CoeusException{
        boolean canModify = false;
        CoeusVector cvData = new CoeusVector();
        Hashtable userRights = new Hashtable();
        
        userRights = getUserRightsData();
        
        /**
         * Get the Unit level right from the hashtable and update to the base class
         */
        Hashtable unitLevelRight = (Hashtable)userRights.get(KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT);
        /**
         * Get the OSP level right from the hashtable and update to the base class
         */
        Hashtable ospLevelRight = (Hashtable)userRights.get(KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT);
         
         
        boolean isAwardBudgetViewer = ((Boolean)unitLevelRight.get(KeyConstants.VIEW_AWARD_BUDGET)).booleanValue();
        boolean isAwardBudgetModifier = ((Boolean)unitLevelRight.get(KeyConstants.MODIFY_AWARD_BUDGET)).booleanValue();
        boolean isAwardBudgetSubmitter = ((Boolean)unitLevelRight.get(KeyConstants.SUBMIT_AWARD_BUDGET)).booleanValue();
        
//        boolean isAwardBudgetAggregator = ((Boolean)unitLevelRight.get(KeyConstants.MAINTAIN_AWARD_BUDGET)).booleanValue();
//        boolean isAwardBudgetAggregator = true;
        //Modified with case 3587: MultiCampus Enhancement
        boolean isAwardBudgetCreator = ((Boolean)unitLevelRight.get(KeyConstants.CREATE_AWARD_BUDGET)).booleanValue();
        boolean isAnyAwardBudgetSubmitter = ((Boolean)unitLevelRight.get(KeyConstants.SUBMIT_ANY_AWARD_BUDGET)).booleanValue();
        boolean isAwardBudgetApprover = ((Boolean)unitLevelRight.get(KeyConstants.APPROVE_AWARD_BUDGET)).booleanValue();
        boolean isAwardBudgetAdmin = ((Boolean)unitLevelRight.get(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING)).booleanValue();
        boolean isPostAwardBudget = ((Boolean)unitLevelRight.get(KeyConstants.POST_AWARD_BUDGET)).booleanValue();
        //3587 End
        //Added for COEUSQA-2521 - User who has both view depts awards (or view award) and view award budget roles can't view award budget. - start
        if(getFunctionType() == TypeConstants.DISPLAY_MODE && isAwardBudgetViewer) {
            canModify = true;
        } 
        //Added for COEUSQA-2521 - User who has both view depts awards (or view award) and view award budget roles can't view award budget. - end        
        else if(isAwardBudgetViewer && !isAwardBudgetCreator && !isAnyAwardBudgetSubmitter &&
           !isAwardBudgetApprover && !isAwardBudgetSubmitter &&
           !isAwardBudgetModifier && 
//           !isAwardBudgetAggregator &&
           !isPostAwardBudget){
               canModify = false;
        }else{
            canModify = true;
        }
        cvData.add(new Boolean(canModify));
        cvData.add(userRights);
        return cvData;
    }
    
    private String getLeadUnit() throws CoeusException{
        
        CoeusVector cvUnit = queryEngine.executeQuery(queryKey,AwardUnitBean.class, 
                                CoeusVector.FILTER_ACTIVE_BEANS); 
        for(int i=0;i<cvUnit.size();i++){
            if(((AwardUnitBean)cvUnit.get(i)).isLeadUnitFlag()){
                return ((AwardUnitBean)cvUnit.get(i)).getUnitNumber();
            }
        }
        return "";
    }
    
    private Hashtable getUserRightsData() throws CoeusException{
        Hashtable userRights = null;
        Hashtable awardBudgetRigts = new Hashtable();
        char GET_AWARD_BUDGET_RIGHTS = 'C';
        String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                    AWARD_BUDGET_SERVLET;
    
       
        
        awardBudgetRigts.put(KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT, getOspLevelRight());
        awardBudgetRigts.put(KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT, getUnitLevelRight());
        awardBudgetRigts.put("AWARD_BUDGET_LEAD_UNIT", getLeadUnit());
        
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setDataObject(awardBudgetRigts);
        request.setFunctionType(GET_AWARD_BUDGET_RIGHTS);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                userRights = (Hashtable)response.getDataObject();
            }else{
                Exception ex = response.getException();
                ex.printStackTrace();
                throw new CoeusException(ex.getMessage());
            }
        }
        return userRights;
    }
    
    private Integer getParameterForBudget(){
        Hashtable userRights = null;
        Hashtable awardBudgetRigts = new Hashtable();
        char GET_PARAMETER_FOR_BUDGET = 'J';
        Integer parameterForBudget = new Integer(1);
        
        String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                    AWARD_BUDGET_SERVLET;
       
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        
        request.setFunctionType(GET_PARAMETER_FOR_BUDGET);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                parameterForBudget = (Integer)response.getDataObject();
            }else{
                Exception ex = response.getException();
                ex.printStackTrace();
            }
        }
        return parameterForBudget;
    }
    
    /**
     * Getter for property unitLevelRight.
     * @return Value of property unitLevelRight.
     */
    public java.util.Hashtable getUnitLevelRight() {
        Hashtable unitLevelRight = new Hashtable();
        unitLevelRight.put(KeyConstants.VIEW_AWARD_BUDGET,"VIEW_AWARD_BUDGET");
        unitLevelRight.put(KeyConstants.MODIFY_AWARD_BUDGET,"MODIFY_AWARD_BUDGET");
        unitLevelRight.put(KeyConstants.SUBMIT_AWARD_BUDGET,"SUBMIT_AWARD_BUDGET");
        unitLevelRight.put(KeyConstants.MAINTAIN_AWARD_BUDGET,"MAINTAIN_AWARD_BUDGET");
        //Added With Case 3587: MultiCampus enhancement
        unitLevelRight.put(KeyConstants.CREATE_AWARD_BUDGET,"CREATE_AWARD_BUDGET");
        unitLevelRight.put(KeyConstants.APPROVE_AWARD_BUDGET,"APPROVE_AWARD_BUDGET");
        unitLevelRight.put(KeyConstants.SUBMIT_ANY_AWARD_BUDGET,"SUBMIT_ANY_AWARD_BUDGET");
        unitLevelRight.put(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING,"MAINTAIN_AWARD_BUDGET_ROUTING");
        unitLevelRight.put(KeyConstants.POST_AWARD_BUDGET,"POST_AWARD_BUDGET");
        //3587 End
        return unitLevelRight;
    }
    
    /**
     * Getter for property ospLevelRight.
     * @return Value of property ospLevelRight.
     */
    public java.util.Hashtable getOspLevelRight() {
        Hashtable ospLevelRight = new Hashtable();
        //Commented With Case 3587: MultiCampus enhancement
//        ospLevelRight.put(KeyConstants.CREATE_AWARD_BUDGET,"CREATE_AWARD_BUDGET");
//        ospLevelRight.put(KeyConstants.APPROVE_AWARD_BUDGET,"APPROVE_AWARD_BUDGET");
//        ospLevelRight.put(KeyConstants.SUBMIT_ANY_AWARD_BUDGET,"SUBMIT_ANY_AWARD_BUDGET");
//        ospLevelRight.put(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING,"MAINTAIN_AWARD_BUDGET_ROUTING");
//        ospLevelRight.put(KeyConstants.POST_AWARD_BUDGET,"POST_AWARD_BUDGET");
        //3587 End
        return ospLevelRight;
    }
    //Award Budget Enhancment End 3
    
    
    /**Displays budget for selected Award
     */
    private void showBudget() {
		
        AwardAmountInfoBean selectedBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
        if(selectedBean == null) {
            CoeusOptionPane.showInfoDialog("Select Row");
			return;
        }
        /*Bug:1416*/
        /*these lines have been commented as the validations here are wrong and these are taken care in the code*/
//		if (selectedBean.getAnticipatedChange() < 0.0) {
//			CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("awardMoneyAndEndDates_exceptionCode.1163"));
//		}
//		if (selectedBean.getObligatedChange() < 0.0) {
//			CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("awardMoneyAndEndDates_exceptionCode.1160"));
//			return;
//		}
        
		if (validate()) {//Bug Fix: 1416 (should validate the money and end dates screen before opening Budget window.
			syncAwardBudget(selectedBean, AWARD_BUDGET_FLAG);
        
			BudgetForAwardController budgetForAwardController =
			new BudgetForAwardController(selectedBean, getFunctionType());
			budgetForAwardController.display();
		}
    }
    
    /**expands all tree child nodes.
     */
    private void expandAll() {
        moneyAndEndDatesForm.awardAmountTreeTable.expandAll(true);
    }
    
    /**Displays goto screen 
     */
    private void showGoto() {
        AwardGoToForm awardGoToForm = new AwardGoToForm();
        int option = awardGoToForm.display();
        if(option == AwardGoToForm.CANCEL) {
            //Cancel clicked. do nothing
        }else {
            //OK Clicked.
            String value = awardGoToForm.getValue();
            if(option ==  AwardGoToForm.MIT_NUMBER) {
                moneyAndEndDatesForm.awardAmountTreeTable.gotoMITAwardNumber(value);
            }else if(option == AwardGoToForm.ACCOUNT_NUMBER) {
                moneyAndEndDatesForm.awardAmountTreeTable.gotoAccountNumber(value);
            }
        }//End if - else cancel/OK clicked
    }
    
    /**Displays history information for selected Award
     */
    private void showHistory() {
        int count = moneyAndEndDatesForm.awardAmountTreeTable.getRowCount();
        AwardAmountInfoBean selectedBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
        int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
        
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_AWARD));
            return ;
        }
        
        CoeusVector cvTraversalBeans = new CoeusVector();
        
        AwardAmountInfoBean awardAmountInfoBean;
        for(int index = 0; index < count; index++) {
            awardAmountInfoBean = moneyAndEndDatesForm.awardAmountTreeTable.getBeanForRow(index);
            cvTraversalBeans.add(awardAmountInfoBean);
        }
        
        AwardMoneyAndEndDatesHistoryController awardMoneyAndEndDatesHistoryController = 
        new AwardMoneyAndEndDatesHistoryController(awardBaseBean);
        
        awardMoneyAndEndDatesHistoryController.setTraversalData(cvTraversalBeans, selectedBean, selectedRow);
        awardMoneyAndEndDatesHistoryController.display();
    }
    
    //#3857 -- start
    /*Displays award action summary for the selected award.     */
    private void  showAwardActionSummary() {
         int count = moneyAndEndDatesForm.awardAmountTreeTable.getRowCount();
         AwardAmountInfoBean selectedBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
         int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
         
         if(selectedRow == -1){
             CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_AWARD));
             return ;
         }
         
         CoeusVector cvTraversalBeans = new CoeusVector();
         
         AwardAmountInfoBean awardAmountInfoBean;
         for(int index = 0; index < count; index++) {
             awardAmountInfoBean = moneyAndEndDatesForm.awardAmountTreeTable.getBeanForRow(index);
             cvTraversalBeans.add(awardAmountInfoBean);
         }
         
         AwardMoneyAndEndDatesActionSummaryController awardMoneyAndEndDatesActionSummaryController =
                 new AwardMoneyAndEndDatesActionSummaryController(awardBaseBean);
         
         awardMoneyAndEndDatesActionSummaryController.setTraversalData(cvTraversalBeans, selectedBean, selectedRow);
         awardMoneyAndEndDatesActionSummaryController.display();
        
        
    }
    //#3857 -- end
    
    /**Displays medusa
     */
    private void showMedusa() {
        BeanEvent beanEvent = new BeanEvent();
        //bug Fix : START 1048
        AwardAmountInfoBean awardAmountInfoBean;
        awardAmountInfoBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
        beanEvent.setBean(awardAmountInfoBean);
        //Bug Fix : END 1048
        
        beanEvent.setSource(this);
        beanEvent.setMessageId(SHOW_MEDUSA);
        fireBeanUpdated(beanEvent);
    }
    
    /**Displays summary
     */
    private void showSummary() {
        if(saveCount == 0 && (getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD ||
        getFunctionType() == NEW_CHILD_COPIED)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_SUMMARY));
            return ;
        }
        
        int selectedRow = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedRow();
        if(selectedRow == -1) {
            CoeusOptionPane.showInfoDialog("Select Row");
            return ;
        }
        //Commented the code for case #2054  by tarique start 1
        //AwardSummaryController awardSummaryController = new AwardSummaryController(cvAwardAmount, selectedRow);
        //Commented the code for case #2054  by tarique end 1
        
        //Added for case #2054 by tarique start 2
        int count = moneyAndEndDatesForm.awardAmountTreeTable.getRowCount();
        CoeusVector cvTraversalBeans = new CoeusVector();
        AwardAmountInfoBean awardAmountInfoBean;
        for(int index = 0; index < count; index++) {
            awardAmountInfoBean = moneyAndEndDatesForm.awardAmountTreeTable.getBeanForRow(index);
            cvTraversalBeans.add(awardAmountInfoBean);
        }
        awardAmountInfoBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
        AwardSummaryController awardSummaryController 
            = new AwardSummaryController(cvAwardAmount, selectedRow, awardAmountInfoBean.getMitAwardNumber(), cvTraversalBeans);
        //Added for case #2054 by tarique end 2
        awardSummaryController.display();
        //Added for case #2054 by tarique start 3
        awardAmountInfoBean = null;
        cvTraversalBeans = null;
        //Added for case #2054 by tarique end 3
        
    }
    
    /**
     * Use this method to refresh the GUI with new Data
     */
    public void refresh() {
        if(isRefreshRequired()) {
            setFormData(awardBaseBean);
        }
    }
    
    /**
     * invoked when bean is Updated
     * @param beanEvent encapsulates event information.
     */
    public void beanUpdated(BeanEvent beanEvent) {
        Controller source = beanEvent.getSource();
        BaseBean baseBean = beanEvent.getBean();
        if(source.getClass().equals(AwardDetailController.class) && 
        baseBean.getClass().equals(AwardDetailsBean.class)) {
            //Award account number has updated. update in award amounts also.
            Equals eqMitNum = new Equals("mitAwardNumber", awardBaseBean.getMitAwardNumber());
//            CoeusVector cvAward = cvAwardAmount.filter(eqMitNum);
//            AwardAmountInfoBean  awardAmountInfoBean = (AwardAmountInfoBean)cvAward.get(0);
            AwardDetailsBean awardDetailsBean = (AwardDetailsBean)baseBean;
            //Commented for COEUSDEV-653 Award - Account number and status display bug-Start
            //awardAmountInfoBean.setAccountNumber(awardDetailsBean.getAccountNumber());
            //awardAmountInfoBean.setStatusCode(awardDetailsBean.getStatusCode());
            //Commented for COEUSDEV-653 Award - Account number and status display bug-End
            //setRefreshRequired(true);
            // Added for COEUSDEV-850 : Money and end dates screen doesn't show new child account numbers until after a save - start
            // Added for COEUSDEV-843 : Flag color on the award money and date screen would not change until account is saved - Start
            // Added for COEUSQA-3394 : unable to save award transaction type when certain changes are made to award detail tab - Start
            // Gets the AwardAmountTreeTableModel data and upadte the account number and status code and update the model
            try{
                 CoeusVector cvAwardAmountInfo = moneyAndEndDatesForm.awardAmountTreeTable.getModelData();
                 if(cvAwardAmountInfo != null && !cvAwardAmountInfo.isEmpty()){
                     CoeusVector cvFilteredAmountInfo = cvAwardAmountInfo.filter(eqMitNum);
                     if(cvFilteredAmountInfo != null && !cvFilteredAmountInfo.isEmpty()){
                          AwardAmountInfoBean awardAmountBean = (AwardAmountInfoBean)cvFilteredAmountInfo.get(0);
                          awardAmountBean.setAccountNumber(awardDetailsBean.getAccountNumber());
                          awardAmountBean.setStatusCode(awardDetailsBean.getStatusCode());
                          // update the details in Query Engine
                          CoeusVector cvAwardAmountDetails = queryEngine.executeQuery(queryKey, AwardAmountInfoBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                          if(cvAwardAmountDetails != null && !cvAwardAmountDetails.isEmpty()){
                            Equals eqAwardNum = new Equals("mitAwardNumber", awardBaseBean.getMitAwardNumber());    
                            cvAwardAmountDetails = cvAwardAmountDetails.filter(eqAwardNum);
                            if(cvAwardAmountDetails != null && !cvAwardAmountDetails.isEmpty()){
                                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmountDetails.get(0);
                                // Modified for COEUSDEV-1152 : Award creating duplicated transaction IDs - Start
                                // Since queryEngine update method will change the acType to "U", 
                                // here only the status and account number need to be update to the query engine
//                                awardAmountInfoBean.setAccountNumber(awardDetailsBean.getAccountNumber());
//                                awardAmountInfoBean.setStatusCode(awardDetailsBean.getStatusCode());
//                                queryEngine.update(queryKey,awardAmountInfoBean);
                                 queryEngine.setUpdate(queryKey,AwardAmountInfoBean.class,
                                         "accountNumber",String.class,awardDetailsBean.getAccountNumber(),eqMitNum);
                                 // JM 4-11-2013 statement sending status code as string, should be int
                                 //queryEngine.setUpdate(queryKey,AwardAmountInfoBean.class,
                                 //        "statusCode",String.class,awardDetailsBean.getStatusCode(),eqMitNum);
                                 queryEngine.setUpdate(queryKey,AwardAmountInfoBean.class,
                                         "statusCode",int.class,awardDetailsBean.getStatusCode(),eqMitNum);
                                 // JM END
                                // Modified for COEUSDEV-1152 : Award creating duplicated transaction IDs - End
                            }
                          }
                          moneyAndEndDatesForm.awardAmountTreeTable.updateModelData(cvAwardAmountInfo,awardBaseBean.getMitAwardNumber());
                          
                     }
                 }
            }catch(Exception exp){
                exp.printStackTrace();
            }
            // Added for COEUSQA-3394 : unable to save award transaction type when certain changes are made to award detail tab - End
            // Added for COEUSDEV-850 : Money and end dates screen doesn't show new child account numbers until after a save - End
            // Added for COEUSDEV-843 : Flag color on the award money and date screen would not change until account is saved - End
        }

    }
         private void createReport(int indicator) {
        BeanEvent beanEvent = new BeanEvent();
        AwardAmountInfoBean awardAmountInfoBean;
        //start case 1816 
//        awardAmountInfoBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
        awardAmountInfoBean = moneyAndEndDatesForm.awardAmountTreeTable.getBeanForRow(0);
        //end case 1816
        beanEvent.setBean(awardAmountInfoBean);
        
        beanEvent.setSource(this);
        beanEvent.setMessageId(indicator);
        fireBeanUpdated(beanEvent);
    }
     public void printBudgetHierarchy()throws CoeusException{
        CoeusVector cvAwardAmountInfo = moneyAndEndDatesForm.awardAmountTreeTable.getBeans();
        if(cvAwardAmountInfo == null || cvAwardAmountInfo.size() == 0) return ;
        
        Equals eqParent = new Equals("parentMitAwardNumber", "000000-000");
            
        AwardAmountInfoBean root = (AwardAmountInfoBean)cvAwardAmountInfo.filter(eqParent).get(0);//should always return a vector with 1 item
        if (root != null){
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(PRINT_BUDGET_HIERARCHY);
            Hashtable htPrintParams = new Hashtable();
            htPrintParams.put("MIT_AWARD_NUMBER",root.getMitAwardNumber());
            requesterBean.setDataObject(htPrintParams);
            
            //For Streaming
            requesterBean.setId("Award/BudgetHierarchy");
            requesterBean.setFunctionType('R');
            //For Streaming
        
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + PRINT_SERVLET, requesterBean);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            String fileName = "";
            if(responder.isSuccessfulResponse()){
//                 AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();

                 fileName = (String)responder.getDataObject();
//                 System.out.println("Report Filename is=>"+fileName);

                 fileName.replace('\\', '/') ; // this is fix for Mac
//                 URL reportUrl = null;
                 try{
//                    reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//
//
//                 if (coeusContxt != null) {
//                     coeusContxt.showDocument( reportUrl, "_blank" );
//                 }else {
//                     javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                     bs.showDocument( reportUrl );
//                 }
                   URL urlObj = new URL(fileName);
                   URLOpener.openUrl(urlObj);
                 }catch(MalformedURLException muEx){
                     throw new CoeusException(muEx.getMessage());
                 }catch(Exception uaEx){
                     throw new CoeusException(uaEx.getMessage());
                 }

            }else{
                 throw new CoeusException(responder.getMessage());
            }
        }
        cvAwardAmountInfo = null;
     }
     
  //For print Current Budget Modification report
    public  void printCurrentBudgetModification(String mitAwardnum, String transactinId )throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(PRINT_BUDGET_MODIFICATION);
        Hashtable htPrintParams = new Hashtable();
//       make sure use root number
        htPrintParams.put("MIT_AWARD_NUMBER",(mitAwardnum.substring(0,7)+"001"));
        htPrintParams.put("TRANSACTION_ID",transactinId);
        requesterBean.setDataObject(htPrintParams);
        
        //For Streaming
        requesterBean.setId("Award/BudgetHistoryTransactionDetail");
        requesterBean.setFunctionType('R');
        //For Streaming
            
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + PRINT_SERVLET, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();            
        if(responderBean.isSuccessfulResponse()){
             String fileName = "";  
             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
             
             fileName = (String)responderBean.getDataObject();
//             System.out.println("Report Filename is=>"+fileName);
             
             fileName.replace('\\', '/') ; // this is fix for Mac
//             URL reportUrl = null;
             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
               URL urlObj = new URL(fileName);
               URLOpener.openUrl(urlObj);
             }catch(MalformedURLException muEx){
                 throw new CoeusException(muEx.getMessage());
             }catch(Exception uaEx){
                 throw new CoeusException(uaEx.getMessage());
             }
             
        }else{
            //Modified for the case# COEUSDEV-270 - Formatting of dollars in NOA and MoneyAndEndaDates
            if (responderBean.getDataObject() == null || responderBean.getDataObject().equals("NoAwardAmountInfo")){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_AWARD_AMOUNT) + transactinId + ". ");
            }else{
                 throw new CoeusException(responderBean.getMessage());
            }
        }
    
    }
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    /**
     * Returns true if any of the beans in the vector cvAmountInfo has acType not null
     *
     * @param cvAmountInfo Vector with AwardAmountInfoBean beans
     * @return boolean
     *
     */
    private boolean isTransactionModified(CoeusVector cvAmountInfo){
        boolean modified = false;
        AwardAmountInfoBean awardAmountInfoBean;
        boolean acTypeSet = false;
        if(cvAmountInfo!=null){
            for(int i=0; i<cvAmountInfo.size(); i++){
                awardAmountInfoBean = (AwardAmountInfoBean)cvAmountInfo.get(i);
                if(awardAmountInfoBean.getAcType()!=null){
                    acTypeSet = true;
                    break;
                }
            }
        }
        
        if(saveCount == 0 && acTypeSet){
            modified = true;
        }
        
        if(!modified &&  !acTypeSet){
            if(moneyAndEndDatesForm.txtArComments.getText().trim().length() > 0){
                modified = true;
            }else if(moneyAndEndDatesForm.txtNoticeDate.getText().trim().length() > 0){
                modified = true;
            }else{
                ComboBoxBean comboBoxBean = (ComboBoxBean)moneyAndEndDatesForm.cmbTransactionType.getSelectedItem();
                if(comboBoxBean != null && comboBoxBean.getCode()!=null && comboBoxBean.getCode().length() >0){
                    modified = true;
                }
            }
        }else if(!modified && saveCount >0 && acTypeSet){
            String comments = moneyAndEndDatesForm.txtArComments.getText();
            ComboBoxBean comboBoxBean = (ComboBoxBean)moneyAndEndDatesForm.cmbTransactionType.getSelectedItem();
            String noticeDate = moneyAndEndDatesForm.txtNoticeDate.getText();
            java.sql.Date convertedDate = null;
             if(!noticeDate.equals(EMPTY_STRING)){
                java.util.Date enteredDate = null;
                try {
                    enteredDate = simpleDateFormat.parse(dateUtils.restoreDate(noticeDate, DATE_SEPARATERS));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                if(enteredDate!=null){
                    convertedDate = new java.sql.Date(enteredDate.getTime());
                }
            }
            CoeusVector cvAmountInfoSorted = new CoeusVector();
            cvAmountInfoSorted.addAll(cvAmountInfo);
            if(cvAmountInfo!=null){
                cvAmountInfoSorted.sort("transactionId", false);
            }

            if(cvAmountInfoSorted!=null && cvAmountInfoSorted.size() >0){
                awardAmountInfoBean = (AwardAmountInfoBean)cvAmountInfoSorted.get(0);
                if(awardAmountInfoBean != null && awardAmountInfoBean.getAwardAmountTransaction() !=null){
                    AwardAmountTransactionBean awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
                    if(awardAmountTransactionBean != null){
                        if(!comments.equals(awardAmountTransactionBean.getComments())){
                            modified = true;
                        }
                    }
                    if(!modified){
                        if(comboBoxBean!=null && comboBoxBean.getCode() != null 
                                && !comboBoxBean.getCode().equals(Integer.toString(awardAmountTransactionBean.getTransactionTypeCode()))){
                                modified = true;
                        }
                    }

                    if(!modified){
                        if((convertedDate == null && awardAmountTransactionBean.getNoticeDate()!=null)
                                || (convertedDate!=null && awardAmountTransactionBean.getNoticeDate()==null)){
                            modified = true;
                        }
                    }
                    if (!modified){
                        if(convertedDate != null && awardAmountTransactionBean.getNoticeDate() != null
                                && !convertedDate.equals(awardAmountTransactionBean.getNoticeDate())){
                            modified = true;
                        }
                    }
                }
            }
        }
        return modified;
    }
    
    /**
     * Checks whether any bean has actype not null and returns the first bean with 
     * this criteria. Returns null if all the beans acType is null
     *
     * @param cvAmountInfo Vector with AwardAmountInfoBean beans
     * @return AwardAmountInfoBean
     */
    private AwardAmountInfoBean getModifiedAmountInfoBean(CoeusVector cvAmountInfo){
        AwardAmountInfoBean modifiedAwardAmountInfoBean = null;
        if(cvAmountInfo!=null){
            AwardAmountInfoBean awardAmountInfoBean = null;
            for(int i=0; i<cvAmountInfo.size(); i++){
                awardAmountInfoBean = (AwardAmountInfoBean)cvAmountInfo.get(i);
                if(awardAmountInfoBean.getAcType()!=null){
                    modifiedAwardAmountInfoBean = awardAmountInfoBean;
                    break;
                }
            }
        }
        return modifiedAwardAmountInfoBean;
    }
    
    /**
     * Populate the AwardAmountTransactionBean of the beans AwardAmountInfo in the
     * vector with data entered in the form
     *
     * @param cvAmountInfo Vector with AwardAmountInfoBean beans
     */
    private void populateAmtTransactionDetails(CoeusVector cvAmountInfoBeans){
        boolean applyHeaderChanges = false;
        ComboBoxBean comboBoxBean;
        String noticeDate = moneyAndEndDatesForm.txtNoticeDate.getText();
        java.sql.Date convertedDate = null;
        if(!noticeDate.equals(EMPTY_STRING)){
            java.util.Date enteredDate = null;
            try {
                enteredDate = simpleDateFormat.parse(dateUtils.restoreDate(noticeDate, DATE_SEPARATERS));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            if(enteredDate!=null){
                convertedDate = new java.sql.Date(enteredDate.getTime());
            }
        }
        String comments = moneyAndEndDatesForm.txtArComments.getText().trim();

        boolean changesApplied = false;
        AwardAmountInfoBean awardAmountInfoBean = null;

        applyHeaderChanges = isTransactionModified(cvAmountInfoBeans);
        if(applyHeaderChanges){
            comboBoxBean = (ComboBoxBean)moneyAndEndDatesForm.cmbTransactionType.getSelectedItem();
            AwardAmountTransactionBean awardAmountTransactionBean = null;
            for(int i=0; i<cvAmountInfoBeans.size(); i++){
                awardAmountInfoBean = (AwardAmountInfoBean)cvAmountInfoBeans.get(i);
                if(awardAmountInfoBean != null && awardAmountInfoBean.getAcType()!=null ){
                    changesApplied = true;
                    if(awardAmountInfoBean.getAwardAmountTransaction() == null){
                        awardAmountInfoBean.setAwardAmountTransaction(new AwardAmountTransactionBean());
                    }
                    awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
                    awardAmountTransactionBean.setComments(comments);
                    awardAmountTransactionBean.setNoticeDate(convertedDate);
                    try {
                        awardAmountTransactionBean.setTransactionTypeCode(Integer.parseInt(comboBoxBean.getCode()));
                    } catch (NumberFormatException ex) {
                        awardAmountTransactionBean.setTransactionTypeCode(0);
                    }
                    if(getFunctionType() == CORRECT_AWARD){
                        awardAmountTransactionBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
            if(!changesApplied){
                if(cvAmountInfoBeans != null && selectedAwardBaseBean!=null){
                    for(int i=0; i<cvAwardAmount.size(); i++){
                        awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(i);
                        if(awardAmountInfoBean.getMitAwardNumber().equals(selectedAwardBaseBean.getMitAwardNumber())){
                            awardAmountInfoBean.setAcType(TypeConstants.INSERT_RECORD);
                            if(awardAmountInfoBean.getAwardAmountTransaction() == null){
                                awardAmountInfoBean.setAwardAmountTransaction(new AwardAmountTransactionBean());
                            }
                            awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
                            awardAmountTransactionBean.setComments(comments);
                            awardAmountTransactionBean.setNoticeDate(convertedDate);
                            try {
                                awardAmountTransactionBean.setTransactionTypeCode(Integer.parseInt(comboBoxBean.getCode()));
                            } catch (NumberFormatException ex) {
                                awardAmountTransactionBean.setTransactionTypeCode(0);
                            }
                            awardAmountTransactionBean.setAcType(TypeConstants.INSERT_RECORD);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public void focusLost(FocusEvent fe){
        if(fe.isTemporary()) return ;
        Object source = fe.getSource();
         if(source.equals(moneyAndEndDatesForm.txtNoticeDate)) {
            String noticeDate = moneyAndEndDatesForm.txtNoticeDate.getText().trim();
            
            if(noticeDate.equals(EMPTY)) return ;
            noticeDate = dateUtils.formatDate(noticeDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            
            if(noticeDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_VALID_NOTICE_DATE));
                setRequestFocusInThread(moneyAndEndDatesForm.txtNoticeDate);
            }else {
                moneyAndEndDatesForm.txtNoticeDate.setText(noticeDate);
            }
        }//End Effective date validation.
    }
    
    public void focusGained(FocusEvent fe){
        if(fe.isTemporary()) return ;
        Object source = fe.getSource();
        // 4499: Unable to print the Money and End date in Display Mode - Start
        // Provided Null Check.
        if(moneyAndEndDatesForm!= null){
            if(source.equals(moneyAndEndDatesForm.txtNoticeDate)) {
                if(moneyAndEndDatesForm.txtNoticeDate != null) {
                    String noticeDate = moneyAndEndDatesForm.txtNoticeDate.getText();
                    if(noticeDate != null){
                        noticeDate = dateUtils.restoreDate(noticeDate, DATE_SEPARATERS);
                        moneyAndEndDatesForm.txtNoticeDate.setText(noticeDate);
                    }
                }
            }
        }
        // 4499: Unable to print the Money and End date in Display Mode - End
    }
    /**
     * Sets the request to the given component
     * 
     * @param component
     */
     private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    public void cleanUp() {
//        Equals eqMITNo = new Equals("mitAwardNumber",awardBaseBean.getMitAwardNumber());
//        queryEngine.removeData(queryKey, AwardAmountInfoBean.class,eqMITNo);
        
        //Bug Fix:Performance Issue (Out of memory) Start 3
        //System.out.println("Clean Up");
        jscrpn.remove(moneyAndEndDatesForm);
        moneyAndEndDatesForm.awardAmountTreeTable.cleanUp();
        moneyAndEndDatesForm = null;
        cvAwardAmount = null;
        jscrpn = null;
        removeBeanUpdatedListener(this, AwardDetailsBean.class);
        //Bug Fix:Performance Issue (Out of memory) End 3
    }
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    public AwardBaseBean getSelectedAwardBaseBean() {
        return selectedAwardBaseBean;
    }

    public void setSelectedAwardBaseBean(AwardBaseBean selectedAwardBaseBean) {
        this.selectedAwardBaseBean = selectedAwardBaseBean;
    }
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end

    //~*~**~**~*~*~**~*~*~*~**~*~
  private boolean fetchParameterValue()throws Exception{
        HashMap mpParameterName = new HashMap();
        boolean parameterFlag = false;
        RequesterBean  requesterB = new RequesterBean();
        requesterB.setFunctionType(GET_PARAMETER_VALUE);
        requesterB.setParameterValue("ENABLE_RAFT_AWARD_BUDGET");
        String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                    AWARD_BUDGET_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, requesterB);
        comm.send();
           ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                parameterFlag = ((Boolean)response.getParameterValue()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
         return parameterFlag ;
    }
private boolean getCreateAwardBudgetRight()throws Exception{
        String awdNumber = awardBaseBean.getMitAwardNumber();
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CREATE_AWARD_BUDGET_FOR_RAFT);
        requesterBean.setId(awdNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }

private boolean getModifyAwardBudgetRight()throws Exception{
        String awdNumber = awardBaseBean.getMitAwardNumber();
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(MODIFY_AWARD_BUDGET_FOR_RAFT);
        requesterBean.setId(awdNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }
private boolean getViewAwardBudgetRight()throws Exception{
        String awdNumber = awardBaseBean.getMitAwardNumber();
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(VIEW_AWARD_BUDGET_FOR_RAFT);
        requesterBean.setId(awdNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }
  private void openUrlCertify() throws IOException
    {
        /*commented is in the case of selected user*/
//    int selectedRow = tblPerson.getSelectedRow();
//    String personId = (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);
        /*this is in the case of login user*/      
    String  awardNum=awardBaseBean.getMitAwardNumber();     
    String urlSend=null;
    StringBuffer url =null;
                            url=new StringBuffer();
                            url.append("/raftBudget.do?awardNumber=");
                            url.append(awardNum);
                            urlSend=url.toString();
        try{
            URLOpener.openUrl(urlSend);
            }catch(Exception ex){
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }

    }
    
  //Added for COEUSDEV-1152_LOG_ Award creating duplicated transaction IDs - Start
    /**
     * Method to check transaction type is changed
     * @return boolean
     */
  public boolean isTransactionTypeChanged(){
      ComboBoxBean comboBoxBean = (ComboBoxBean)moneyAndEndDatesForm.cmbTransactionType.getSelectedItem();
      if(comboBoxBean != null && comboBoxBean.getCode()!=null && comboBoxBean.getCode().length() >0){
          return true;
      }
      return false;
  }
  //COEUSDEV-1152_LOG_ Award creating duplicated transaction IDs - End
}
