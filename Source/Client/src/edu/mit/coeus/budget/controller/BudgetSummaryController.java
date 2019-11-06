/*
 * BudgetSummaryController.java
 *
 * Created on October 3, 2003, 11:30 AM
 *//** Copyright (c) Massachusetts Institute of Technology
  * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
  * All rights reserved.
  */

package edu.mit.coeus.budget.controller;


import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.budget.controller.*;
import edu.mit.coeus.budget.gui.BudgetSummaryForm;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.propdev.gui.ProposalDetailForm;
import edu.mit.coeus.gui.CoeusInternalFrame;


import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.text.ParseException;

/** Budget Summary Controller.All event handling done here
 * @author chandrashekara
 */
public class BudgetSummaryController extends Controller implements
ActionListener, ItemListener, BeanUpdatedListener{
    
    /** To get the proposal start date, End date and Budget status and update the budget
     * status to the proposal detail form
     */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    /** Flag is used to specify whether data has changed or not */
    public boolean modified = false;
    
    /** flag is used to specify whether refresh is required or not.This will be true
     * when firing the bean event
     */
    private boolean refreshRequired;
    /** This svector is used for the Refreshed Data
     */
    private CoeusVector cvOhRate;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private Controller eventSource = this;
    /** This vector is used to hold the OH Rate type code and Description.
     */
    private Vector vecOHRate;
    
    /** This vector is used to hold the OH Rate type code and Description.
     */
    private Vector vecURRate;
    
    /* Check whether the mouse clicked on the editor or not
     */
    private boolean mouseClicked;
    
    private QueryEngine queryEngine;
    // Setting the UR Rate Type code.
    int urRateType;
    /** This bean contains all the details of the Budget Summary
     */
    private BudgetInfoBean budgetInfoBean;
    
    /** This bean contains the information about the Budget Periods
     */
    private BudgetPeriodBean budgetPeriodBean;
    
    /** This vector holds the Vector of BudgetPeriodBean detaisl
     */
    private Vector vecPeriodDetails;
    
    /** This will hold the Vector of Bean
     */
    private CoeusVector vecBudgetPeriodBean;
    
    /** This vector will hold the BudgetInfoBean details
     */
    private CoeusVector vecBudgetInfoBean;
    
    private DateUtils dtUtils = new DateUtils();
    /** holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources;
    
    /** This bean holds Budget Type, Proposal Number and Version Number
     */
    BudgetBean budgetBean;
    /** holds the changed oh rate type if the selected option is NO
     */
    private Object oldOhRate;
    
    /** holds the changed oh rate type if the selected option is NO
     */
    private Object oldUrRate;
    
    /** To hold the changed budget status
     */
    private Object oldBudgetStatus;
    
    // Case id# 2924 - start
    /** To hold the changed on/off campus flag
     */
    private Object oldOnOffFlag;
    // Case id #2924 - end
    
    /** To specify the date format*/
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    //TO display err message
    private String mesg;
    
    /** To Specify the Model for the tabel which holds the Period details
     */
    BudgetPeriodModel budgetPeriodModel;
    
    /** Instance of the Form Class Budget Summary Form
     */
    //Code commented and added for bug fix case#3183
//    BudgetSummaryForm budgetSummaryForm;
    public BudgetSummaryForm budgetSummaryForm;
    
    /** Holds the instance of the Editor for the table
     */
    BudgetSummaryEditor budgetSummaryEditor;
    
    /** Rendere for the Cost and Date related component in the table
     */
    ColumnPeriodRenderer columnPeriodRenderer;
    
    /** Bean which contains the details of the BudgetInfoBean and Vector Beans
     * of Budget Period Beans*/
    
    Vector vecBeanDetail;
    
    /** holds the Budget start date and budget end date
     */
    private String txtPreviousStartDate, txtPreviousEndDate;
    
    /* Key for the Query engine
     */
    private String queryKey;
    
    /** To specify the FunctionType
     */
    private char functionType;
    
    //holds the proposal ID.
    private String proposalId;
    // Holds the Version Number
    private int versionNumber;
    
    /** holds the details code and description for the OH Rate
     */
    ComboBoxBean comboBoxBean;
    
    /** Bean which contain the Rate Class values for the OHRate.
     */
    RateClassBean rateClassBean;
    /** traversal of the component in the specified order
     */
    ScreenFocusTraversalPolicy  traversePolicy;
    
    /** This bean is used to compare the two beans and check whether the data modified or not for the ACType
     */
    BudgetInfoBean newBudgetInfoBean;
    
    /** This bean is used to compare the two beans and check for the ACType for data has modified or not
     */
    ProposalDevelopmentFormBean newProposalDevelopmentFormBean;
    
    //Flag used to check whether the OH rate type is cancelled to set the old value
    private boolean isOHCancelling = false;
    
    //Added for Case#3404-Outstanding proposal hierarchy changes - Start
    private static final String SELECT_FINAL_VERSION = "budgetSelect_exceptionCode.1051";
    //Added for Case#3404-Outstanding proposal hierarchy changes - End
    
    java.sql.Date proposalStartDate;
    java.sql.Date proposalEndDate;
    private String budgetStatus;
    // Case Id 1626-start
    private static final char GET_BUDGET_MODULAR_DATA = 'h'; 
    // Case Id 1626-End
    
    //    private boolean statusModified = false;
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 1
     */
    private boolean forceUnderRecovery;
    private boolean forceCostSharing;
    private static final char GET_PARAMETER_DATA = 'f';
    
    //Added for case#3654 - Third option 'Default' in the campus dropdown - start
    private final String BUDGET_MAINTENANCE_SERVLET = "/BudgetMaintenanceServlet";
    private static final String FLAG_DEFAULT = "Default";
    //Added for case#3654 - Third option 'Default' in the campus dropdown - end
    //Added for Case#2924-On/Off Campus flag - Personnel budget
    private boolean errMsgShown = true;    
    //Added with case 2158: Budget Validations - Start
    BudgetBaseWindowController parent = null;
     private static final char VALIDATION_CHECKS = 'N';
     private static final String MSGKEY_VALIDATION_WARNINGS = "budgetSelect_exceptionCode.1060";
     //2158 End
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 1
     *
     *
     *
     *
     * /** create a constructor with BudgetBean.BudgetBean returns proposalNo and Vesrion No
     * @param budgetBean
     * @param proposalDevelopmentFormBean
     * @returns the proposalNo, Version no.
     */
    public BudgetSummaryController(BudgetBean budgetBean, ProposalDevelopmentFormBean proposalDevelopmentFormBean,BudgetBaseWindowController parent) {
        this.parent = parent;
        this.budgetBean = budgetBean;
        this.proposalId = budgetBean.getProposalNumber();
        this.versionNumber = budgetBean.getVersionNumber();
        budgetSummaryForm = new BudgetSummaryForm();
        budgetPeriodModel = new BudgetPeriodModel();
        queryKey = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();
        coeusMessageResources = CoeusMessageResources.getInstance();
        setTableKeyTraversal();
        queryEngine = QueryEngine.getInstance();
        budgetInfoBean = new BudgetInfoBean();
        budgetInfoBean.setProposalNumber(proposalId);
        budgetInfoBean.setVersionNumber(versionNumber);
        
        budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(proposalId);
        budgetPeriodBean.setVersionNumber(versionNumber);
        
        try{
            vecBudgetInfoBean = queryEngine.executeQuery(proposalId + versionNumber, budgetInfoBean);
            vecBudgetPeriodBean = queryEngine.executeQuery(proposalId + versionNumber, budgetPeriodBean);
            
            vecBudgetPeriodBean = vecBudgetPeriodBean.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            
            rateClassBean = new RateClassBean();
            vecOHRate = queryEngine.executeQuery(proposalId + versionNumber,rateClassBean);
            vecURRate = queryEngine.executeQuery(proposalId + versionNumber,rateClassBean);
            
            if(vecBudgetInfoBean != null && vecBudgetInfoBean.size() > 0) {
                budgetInfoBean = (BudgetInfoBean)vecBudgetInfoBean.get(0);
                setFormData(getBeanData(budgetInfoBean, vecBudgetPeriodBean));
            }
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }
        
        budgetSummaryForm.tblBudgetSummary.setModel(budgetPeriodModel);
        columnPeriodRenderer = new ColumnPeriodRenderer();
        budgetSummaryEditor = new BudgetSummaryEditor();
        budgetPeriodModel.setData(vecPeriodDetails);
        
        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_Start
        //setting the recalculated value of UnderRecovery Amount in budgetSummaryForm.txtUnderRecovery field
        budgetSummaryForm.txtUnderRecovery.setValue(budgetInfoBean.getUnderRecoveryAmount());
        budgetSummaryForm.txtCostSharing.setValue(budgetInfoBean.getCostSharingAmount());
        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_End
        
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
        proposalStartDate =  proposalDevelopmentFormBean.getRequestStartDateInitial();
        proposalEndDate = proposalDevelopmentFormBean.getRequestEndDateInitial();
        budgetStatus = proposalDevelopmentFormBean.getBudgetStatus();
        proposalDevelopmentFormBean.setBudgetStatus(budgetStatus);
        setTableEditors();
        if(budgetInfoBean!=null){
            registerComponents();
            setSummaryDetails();
            budgetSummaryForm.txtStartDate.requestFocusInWindow();
            budgetSummaryForm.txtStartDate.grabFocus();
            budgetSummaryForm.txtStartDate.setFocusable(true);
            budgetSummaryForm.cmbOHRateType.addItemListener(this);
            budgetSummaryForm.cmbURRateType.addItemListener(this);
            budgetSummaryForm.cmbOnOffFlag.addItemListener(this);    // Case Id# 2924
            
        }else {
            return;
        }
    }
    
    
    /**
     * @param proposalDevelopmentFormBean
     */
    //public void proposalDates(ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
    //        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    //        proposalStartDate =  proposalDevelopmentFormBean.getRequestStartDateInitial();
    //        proposalEndDate = proposalDevelopmentFormBean.getRequestEndDateInitial();
    //        budgetStatus = proposalDevelopmentFormBean.getBudgetStatus();
    //        //System.out.println("the Budget status is "+budgetStatus);
    //        proposalDevelopmentFormBean.setRequestStartDateInitial(proposalStartDate);
    //        proposalDevelopmentFormBean.setRequestEndDateInitial(proposalEndDate);
    //        //proposalDevelopmentFormBean.setBudgetStatus(budgetStatus);
    
    //}
    /**
     * To get the description for the given  OHRateDescription in the ComboBox
     */
    private String getOHRateDesc(String ohRateCode){
        String ohRateDesc="";
        int ohRateSize = vecOHRate.size();
        for(int index = 0 ; index < ohRateSize ; index++){
            rateClassBean = (RateClassBean)vecOHRate.elementAt(index);
            if (rateClassBean.getCode().equals(ohRateCode)){
                ohRateDesc = rateClassBean.getDescription();
                break;
            }
        }
        return  ohRateDesc;
    }
    /**
     * To get the description for the given  URRateDescription in the ComboBox
     */
    //    private String getURRateDesc(String urRateCode){
    //        String urRateDesc=EMPTY_STRING;
    //        int urRateSize = vecURRate.size();
    //        for(int index = 0 ; index < urRateSize ; index++){
    //            rateClassBean = (RateClassBean)vecOHRate.elementAt(index);
    //            if (rateClassBean.getCode().equals(urRateCode)){
    //                urRateDesc = rateClassBean.getDescription();
    //                break;
    //            }
    //        }
    //        return  urRateDesc;
    //    }
    
    /** This method is used to the set the collection of Available OHRateType Codes
     * and Description information.
     * @param ohRateCode
     */
    public void setOHRateCode(Vector ohRateCode){
        this.vecOHRate = ohRateCode;
    }
    
    /** This method is used to the set the collection of Available OHRateType Codes
     * and Description information.
     * @param ohRateCode
     */
    public void setURRateCode(Vector urRateCode){
        this.vecURRate = urRateCode;
    }
    
    /** This method holds two beans in a vector, the beans like budgetInfoBean
     *  and budgetPeriodBean. The BudgetPeriodBean is a Vector of Bean.
     */
    private Vector getBeanData(BudgetInfoBean budgetInfoBean,CoeusVector vecBudgetPeriodBean){
        Vector vecBeanDetail = new Vector();
        vecBeanDetail.addElement(budgetInfoBean);
        vecBeanDetail.addElement(vecBudgetPeriodBean);
        return vecBeanDetail;
    }
    
    /** sets the form data. Holds the data about the budget period and budget summary
     * @param vecBeanDetail
     */
    public void setFormData(Object vecBeanDetail){
        // Extracting the bean. The get(0) returns the BudgetInfoBean details
        budgetInfoBean = (BudgetInfoBean)((Vector)vecBeanDetail).get(0);
        vecPeriodDetails = (Vector)((Vector)vecBeanDetail).get(1);
    }
    
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 2
     */
    private void getParameterForDistribution(){
        try{
            Vector data = getParameterData(); 
            setForceCostSharing(((Boolean)data.get(0)).booleanValue());
            setForceUnderRecovery(((Boolean)data.get(1)).booleanValue());
        }catch (CoeusClientException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
        
    private Vector getParameterData() throws CoeusClientException{
        Vector data = null;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_PARAMETER_DATA);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response== null){
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }
        if(response.isSuccessfulResponse()){
            data = response.getDataObjects();
        }else {
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            
        }
        return data ;
    }
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 2
     */
    
    /** returns the form data. This is a overridden method of the controller
     * @return returns the form data
     */
    public Object getFormData(){
        return budgetSummaryForm;
    }
    /** This method is to get all the related data for the BudgetSummary.Setting the budgetInfoBean
     */
    private void  setSummaryDetails(){
        
        //setting the OhRateType code and Description
        if( ( vecOHRate != null ) && ( vecOHRate.size() > 0 )
        && ( budgetSummaryForm.cmbOHRateType.getItemCount() == 0 ) ) {
            int ohRateSizeSize = vecOHRate.size();
            budgetSummaryForm.cmbOHRateType.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            for(int index = 0 ; index < ohRateSizeSize ; index++){
                budgetSummaryForm.cmbOHRateType.addItem(
                (RateClassBean)vecOHRate.elementAt(index));
            }
        }
        
        //setting the URRateType code and Description
        if( ( vecURRate != null ) && ( vecURRate.size() > 0 )
        && ( budgetSummaryForm.cmbURRateType.getItemCount() == 0 ) ) {
            int urRateSizeSize = vecURRate.size();
            budgetSummaryForm.cmbURRateType.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            for(int index = 0 ; index < urRateSizeSize ; index++){
                budgetSummaryForm.cmbURRateType.addItem(
                (RateClassBean)vecURRate.elementAt(index));
            }
        }
        String versionNumber = EMPTY_STRING +budgetInfoBean.getVersionNumber();
        budgetSummaryForm.txtVersion.setText(versionNumber);
        budgetSummaryForm.txtResidualFunds.setValue(budgetInfoBean.getResidualFunds());
        budgetSummaryForm.chkFinal.setSelected(budgetInfoBean.isFinalVersion());
        budgetSummaryForm.chkBudgetModular.setSelected(budgetInfoBean.isBudgetModularFlag());
        //COEUSQA-1693 - Cost Sharing Submission - start
        budgetSummaryForm.chkSubmitCostSharing.setSelected(budgetInfoBean.isSubmitCostSharingFlag());
        //COEUSQA-1693 - Cost Sharing Submission - end
        /** the status of the budgetStatus and final version flag while form is opened
         */
        if(budgetSummaryForm.chkFinal.isSelected()){
            budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
        }else if(budgetSummaryForm.chkFinal.isSelected() && budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_COMPLETE){
            budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
            budgetSummaryForm.chkFinal.setEnabled(false);
        }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()!=VAL_COMPLETE &&budgetSummaryForm.chkFinal.isEnabled()){
            budgetSummaryForm.cmbBudgetStatus.setEnabled(false);
        }
        if(!budgetSummaryForm.chkFinal.isSelected()){
            budgetSummaryForm.cmbBudgetStatus.setEnabled(false);
        }else{
            budgetSummaryForm.setEnabled(true);
        }
        /** Setting the values for the budget status depending upon the values it has
         */
        //Modified by Vyjayanthi to check budgetStatus for null
        if(!parent.isValidatingBudgetSummary()){//case 2158
            if( budgetStatus != null ) {
                if( budgetStatus.equals(INCOMPLETE) ){
                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                }else if( budgetStatus.equals(COMPLETE) ){
                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_COMPLETE);
                    //Code added for Proposal Hierarchy case#3183
                    budgetSummaryForm.chkFinal.setEnabled(false);
                }else if( budgetStatus.equals(NONE) ){
                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_NONE);
                }
            }else{
                budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_NONE);
            }
        }
        
        //        if (getFunctionType() == TypeConstants.ADD_MODE) {
        //            budgetSummaryForm.cmbOHRateType.setSelectedIndex(1);
        //            budgetSummaryForm.cmbURRateType.setSelectedIndex(1);
        //        }else {
        budgetSummaryForm.cmbOHRateType.setSelectedItem(EMPTY_STRING + budgetInfoBean.getOhRateClassCode());
        budgetSummaryForm.cmbURRateType.setSelectedItem(EMPTY_STRING + budgetInfoBean.getUrRateClassCode());
        //    }
        budgetSummaryForm.txtDirectCost.setValue(budgetInfoBean.getTotalDirectCost());
        
        budgetSummaryForm.txtIndirectCost.setValue(budgetInfoBean.getTotalIndirectCost());
        
        budgetSummaryForm.txtCostSharing.setValue(budgetInfoBean.getCostSharingAmount());
        
        budgetSummaryForm.txtUnderRecovery.setValue(budgetInfoBean.getUnderRecoveryAmount());
        
        if ( budgetInfoBean.getStartDate() != null ){
            budgetSummaryForm.txtStartDate.setText(dtUtils.formatDate(
            budgetInfoBean.getStartDate().toString(), REQUIRED_DATEFORMAT));
            txtPreviousStartDate = budgetSummaryForm.txtStartDate.getText();
        }
        
        if ( budgetInfoBean.getEndDate() != null ){
            budgetSummaryForm.txtEndDate.setText(dtUtils.formatDate(
            budgetInfoBean.getEndDate().toString(),REQUIRED_DATEFORMAT));
            txtPreviousEndDate = budgetSummaryForm.txtEndDate.getText();
        }
        
        budgetSummaryForm.txtTotalCostLimit.setValue(budgetInfoBean.getTotalCostLimit());
        
        budgetSummaryForm.txtTotalCost.setValue(budgetInfoBean.getTotalCost());
        
        budgetSummaryForm.txtArComments.setText(budgetInfoBean.getComments());
        budgetSummaryForm.chkBudgetModular.setSelected(budgetInfoBean.isBudgetModularFlag());
        //Code added for Case#3472 - Sync to Direct Cost Limit
        //For adding total direct cost limit
        budgetSummaryForm.txtTotalDirectCostLimit.setValue(budgetInfoBean.getTotalDirectCostLimit());
     // Case id# 2924 - start
        //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
        if(budgetInfoBean.isDefaultIndicator()){
            budgetSummaryForm.cmbOnOffFlag.setSelectedItem(EMPTY_STRING + FLAG_DEFAULT);
        }else{
            if (budgetInfoBean.isOnOffCampusFlag())
                budgetSummaryForm.cmbOnOffFlag.setSelectedItem(EMPTY_STRING + FLAG_ON);
            else
                budgetSummaryForm.cmbOnOffFlag.setSelectedItem(EMPTY_STRING + FLAG_OFF);
        }        
        //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
     // Case id# 2924 - end 
     //Commented for-COEUSQA-2698-Confirm "Final" Designation Message displays multiple times-start
//        budgetSummaryForm.chkFinal.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent actionEvent){
//                
//                if(budgetSummaryForm.chkFinal.isSelected()){
//                    performCheckedAction();
//                } else {
//                    performUncheckAction();
//                }                
//            }
//            
//        });
      //Commented-Added for-COEUSQA-2698-Confirm "Final" Designation Message displays multiple times-end
    }
    
    /** this method will specifies the action, when final version flag is checked
     */
    private void performCheckedAction(){
        mesg = DESIGNATE_FINAL_VERSION;
        int selectedOption = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(mesg),
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        switch(selectedOption){
            case (JOptionPane.NO_OPTION):
                budgetSummaryForm.chkFinal.setSelected(false);
                break;
            case (JOptionPane.YES_OPTION):
                budgetSummaryForm.chkFinal.setSelected(true);
                budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
                budgetInfoBean.setFinalVersion(true);
                budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                try{
                    queryEngine.update(proposalId+versionNumber, budgetInfoBean);
                    break;
                }catch(CoeusException  coeusException){
                    coeusException.getMessage();
                }
        }
        
    }// End of performCheckAction() method.............
    
    /** this method will specifies the action, when final version flag is unchecked
     */
    private void  performUncheckAction(){
        mesg = MODIFY_FINAL_VERSION;
        int selectedOptions = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(mesg),
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        switch(selectedOptions){
            case (JOptionPane.NO_OPTION):
                budgetSummaryForm.chkFinal.setSelected(true);
                budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
                break;
                
            case (JOptionPane.YES_OPTION):
                budgetSummaryForm.chkFinal.setSelected(false);
                budgetSummaryForm.cmbBudgetStatus.setEnabled(false);
                budgetInfoBean.setFinalVersion(true);
                budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                try{
                    queryEngine.update(proposalId+versionNumber, budgetInfoBean);
                    break;
                }catch(CoeusException  coeusException){
                    coeusException.getMessage();
                }
                
        }
        
    }// End of performUncheckAction() method.............
    
    
    /** To check whether the BudgetInfoBean is modified or not. Compare with the Strict
     * Equals and update the bean
     */
    public boolean isSummaryDetailsModified(){
        
        newBudgetInfoBean = new BudgetInfoBean();
        boolean isDataEqual = false;
        Date modifiedStartDate;
        Date modifiedEndDate;
        String value;
        
        CoeusVector vecNewBudgetInfoBean;
        queryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
        newBudgetInfoBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        newBudgetInfoBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        
        try{
            vecNewBudgetInfoBean = queryEngine.executeQuery(queryKey, budgetInfoBean);
            if( vecNewBudgetInfoBean != null && vecNewBudgetInfoBean.size() > 0 ){
                newBudgetInfoBean = (BudgetInfoBean)vecNewBudgetInfoBean.get(0);
                
                value = dtUtils.restoreDate(budgetSummaryForm.txtStartDate.getText(), DATE_SEPARATERS);
                if( value.trim().length() >0 && value != null ){
                    modifiedStartDate = dtFormat.parse(value);
                    newBudgetInfoBean.setStartDate( new java.sql.Date(modifiedStartDate.getTime()));
                }
                
                value = dtUtils.restoreDate(budgetSummaryForm.txtEndDate.getText(), DATE_SEPARATERS);
                if( value.trim().length() >0 && value != null ){
                    modifiedEndDate = dtFormat.parse(value);
                    newBudgetInfoBean.setEndDate( new java.sql.Date(modifiedEndDate.getTime()));
                }
                newBudgetInfoBean.setFinalVersion(budgetSummaryForm.chkFinal.isSelected());
                newBudgetInfoBean.setBudgetModularFlag(budgetSummaryForm.chkBudgetModular.isSelected());
                newBudgetInfoBean.setResidualFunds(Double.parseDouble(budgetSummaryForm.txtResidualFunds.getValue()));
                newBudgetInfoBean.setTotalCostLimit(Double.parseDouble(budgetSummaryForm.txtTotalCostLimit.getValue()));
                newBudgetInfoBean.setTotalCost(Double.parseDouble(budgetSummaryForm.txtTotalCost.getValue()));
                newBudgetInfoBean.setTotalDirectCost(Double.parseDouble(budgetSummaryForm.txtDirectCost.getValue()));
                newBudgetInfoBean.setTotalIndirectCost(Double.parseDouble(budgetSummaryForm.txtIndirectCost.getValue()));
                newBudgetInfoBean.setUnderRecoveryAmount(Double.parseDouble(budgetSummaryForm.txtUnderRecovery.getValue()));
                newBudgetInfoBean.setCostSharingAmount(Double.parseDouble(budgetSummaryForm.txtCostSharing.getValue()));
                newBudgetInfoBean.setComments(budgetSummaryForm.txtArComments.getText());
                //COEUSQA-1693 - Cost Sharing Submission - start
                newBudgetInfoBean.setSubmitCostSharingFlag(budgetSummaryForm.chkSubmitCostSharing.isSelected());
                //COEUSQA-1693 - Cost Sharing Submission - end
                //Code added for Case#3472 - Sync to Direct Cost Limit
                //For adding total direct cost limit
                newBudgetInfoBean.setTotalDirectCostLimit(Double.parseDouble(budgetSummaryForm.txtTotalDirectCostLimit.getValue()));
                StrictEquals stEquals = new StrictEquals();
                isDataEqual = stEquals.compare(budgetInfoBean, newBudgetInfoBean);
                if(!isDataEqual){
                    if (newBudgetInfoBean.getAcType() == null) {
                        newBudgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
            
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }catch (ParseException parseException){
            parseException.printStackTrace();
        }catch(Exception exception){
            exception.getMessage();
        }
        
        return !isDataEqual;
        
    }// End of isSummaryDetailsModified................
    
    
    
    /** This is a overridden method used to F the form data. */
    public void saveFormData(){
         
            String budgetStatus = EMPTY_STRING;
            if(parent.isValidatingBudgetSummary()){//case 2158
                budgetStatus = INCOMPLETE;
            }else if (budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_COMPLETE){
                budgetStatus = COMPLETE;
            }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_INCOMPLETE){
                budgetStatus = INCOMPLETE;
            }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_NONE){
                budgetStatus = NONE;
            }
            /** save the changes to the proposal detail form as soon as user
             *saves the budget status in the summary tab
             *Added by chandra 8th July 2004
             */
            CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalId);
            if(frame!= null){
                if(frame.isEnabled()){
                    if(budgetStatus!= null){
                        if(parent.isValidatingBudgetSummary()){//case 2158
                            ((ProposalDetailForm)frame).setBudgetStatusCode(INCOMPLETE);
                        }else if (budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_COMPLETE){
                            ((ProposalDetailForm)frame).setBudgetStatusCode(COMPLETE);
                        }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_INCOMPLETE){
                            ((ProposalDetailForm)frame).setBudgetStatusCode(INCOMPLETE);
                        }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_NONE){
                            if(functionType==TypeConstants.ADD_MODE || getFunctionType() == TypeConstants.ADD_MODE){
                                ((ProposalDetailForm)frame).setBudgetStatusCode(INCOMPLETE);
                            }else{
                                ((ProposalDetailForm)frame).setBudgetStatusCode(NONE);
                            }
                        }
                    }else{
                        if(functionType==TypeConstants.ADD_MODE || getFunctionType() == TypeConstants.ADD_MODE){
                            if(budgetStatus.equals(NONE)){
                                ((ProposalDetailForm)frame).setBudgetStatusCode(INCOMPLETE);
                                
                            }
                        }
                    }
                }
            }
            // End chandra 8th July 2004
            
            if (!budgetStatus.equals(proposalDevelopmentFormBean.getBudgetStatus())) {
                if(parent.isValidatingBudgetSummary()){//case 2158
                    proposalDevelopmentFormBean.setBudgetStatus(INCOMPLETE);
                }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_COMPLETE){
                    proposalDevelopmentFormBean.setBudgetStatus(COMPLETE);
                }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_INCOMPLETE){
                    proposalDevelopmentFormBean.setBudgetStatus(INCOMPLETE);
                }else if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_NONE){
                    if(functionType==TypeConstants.ADD_MODE || getFunctionType() == TypeConstants.ADD_MODE){
                        proposalDevelopmentFormBean.setBudgetStatus(INCOMPLETE);
                    }else{
                        proposalDevelopmentFormBean.setBudgetStatus(NONE);
                    }
                }
                proposalDevelopmentFormBean.setAcType(TypeConstants.UPDATE_RECORD);
                try{
                    queryEngine.update(proposalId+versionNumber, proposalDevelopmentFormBean);
                }catch(CoeusException coeusException){
                    coeusException.getMessage();
                }
                /**
                 *Bug Fix #1362 - The Budget status should be be None only if there
                 * is no budget exists otherwise it should be Incomplete or Complete
                 *3 March 2005 - start
                 */
            }else{
                if(functionType==TypeConstants.ADD_MODE || getFunctionType() == TypeConstants.ADD_MODE){
                    if(budgetStatus.equals(NONE)){
                        try{
                            proposalDevelopmentFormBean.setBudgetStatus(INCOMPLETE);
                            proposalDevelopmentFormBean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.update(proposalId+versionNumber, proposalDevelopmentFormBean);
                        }catch (CoeusException coeusException){
                            coeusException.getMessage();
                        }
                    }
                }
            }// end Bug fix #1362
         
        try {
            //If Budget Summary modified then save
            if (isSummaryDetailsModified()) {
                if(newBudgetInfoBean.getAcType()!=null){
                    BeanEvent beanEvent = new BeanEvent();
                    beanEvent.setBean(newBudgetInfoBean);
                    beanEvent.setSource(this);
                    fireBeanUpdated(beanEvent);
                    if (newBudgetInfoBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        queryEngine.insert(proposalId+versionNumber, newBudgetInfoBean);
                        
                    } else if (newBudgetInfoBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        queryEngine.update(proposalId+versionNumber, newBudgetInfoBean);
                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }// End of saveFormData..........................
    
    
    /** Table model for the Budget Summary Periods.It is an inner class sof budgetSummaryController
     */
    
    private class BudgetPeriodModel extends DefaultTableModel implements TableModel{
        //Modified for Case 3197 - Allow for the generation of project period greater than 12 months -start
        Vector vecData = null;
        private String colnames[] = {"Period","Start Date","End Date", "No. of Months" ,"Total Cost",
        "Direct Cost","Indirect Cost","Cost Sharing","Underrecovery"};
        
        private Class colClass[] = {Integer.class, String.class, String.class, Double.class,
        Double.class,Double.class,Double.class,Double.class,Double.class};
        
        boolean[] canEdit = new boolean [] {
            
            false, true, true, false, true,true,true,true, true
        };
        //3197 - end
        BudgetPeriodModel(){
            
        }
        
        public void setData(Vector data) {
            vecData = data;
        }
        
        public boolean isCellEditable(int row, int col){
            boolean flag = false;
            if( row < 0 )return false;
            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(row);//selectedRow
            Equals periodEquals = new Equals(BUDGET_PERIOD, new Integer(budgetPeriodBean.getBudgetPeriod()));
            try{
                // If the Period contains the Line Item details stop the cell editing
                CoeusVector vecLineDetails = queryEngine.getActiveData(proposalId + versionNumber, BudgetDetailBean.class, periodEquals);
                if(vecLineDetails!=null && vecLineDetails.size() > 0){
                    flag = false;
                }else{
                    flag = true;
                    //Added for Case 3197 - Allow for the generation of project period greater than 12 months 
                    //COEUSQA:3423 - Lite Budget Summary manual entry should not be allowed in the Total column - Start
//                    if((col == PERIOD_COLUMN) || (col == TOTAL_PERIOD_MONTHS) || (functionType == TypeConstants.DISPLAY_MODE )){
                    if((col == PERIOD_COLUMN) || (col == TOTAL_PERIOD_MONTHS) || (functionType == TypeConstants.DISPLAY_MODE )
                    || (col == TOTAL_COST_COLUMN ) ){
                    //COEUSQA:3423 - End
                        flag = false;
                    }else{
                        flag = true;
                        if(col ==  TOTAL_COST_COLUMN && functionType != TypeConstants.DISPLAY_MODE ){
                           /* If Direct cost and Indirect cost are 0.0 edit the Total cost column
                            *If any one of the column is having(Direct cost or Indirect Cost) cost
                            *don't allow Total cost column to edit
                            */
                            if(Double.parseDouble(budgetPeriodModel.getValueAt(row, DIRECT_COST_COLUMN).toString()) == 0.00 &&
                            Double.parseDouble(budgetPeriodModel.getValueAt(row, INDIRECT_COST_COLUMN).toString()) == 0.00){
                                flag = true;
                            }else{
                                flag = false;
                            }
                        }else{
                            flag = true;
                        }
                    }
                    
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            return flag;
        }
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public int getColumnCount() {
            return colnames.length;
        }
        
        public String getColumnName(int column) {
            return colnames[column];
        }
        public int getRowCount() {
            if(vecData==null)return 0;
            return vecData.size();
        }
        
        public Object getValueAt(int row, int column) {
         //Added for COEUSDEV-241 : Funky screens in PD Budget tab for Total screen when save is pressed multiple times - Start
         if(vecData != null && vecData.size() > 0){//COEUSDEV-241 : End
            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecData.get(row);
            if(budgetPeriodBean!=null && vecData!=null && vecData.size()>0){
                switch (column) {
                    case PERIOD_COLUMN:
                        return new Integer(budgetPeriodBean.getBudgetPeriod());
                    case START_DATE_COLUNM:
                        return budgetPeriodBean.getStartDate();
                    case END_DATE_COLUMN:
                        return budgetPeriodBean.getEndDate();
                    case TOTAL_COST_COLUMN:
                        return new Double(budgetPeriodBean.getTotalCost());
                    case DIRECT_COST_COLUMN:
                        return new Double(budgetPeriodBean.getTotalDirectCost());
                    case INDIRECT_COST_COLUMN:
                        return new Double(budgetPeriodBean.getTotalIndirectCost());
                    case COST_SHARING_COLUMN:
                        return new Double(budgetPeriodBean.getCostSharingAmount());
                    case UNDER_RECOVERY_COLUMN:
                        return new Double(budgetPeriodBean.getUnderRecoveryAmount());
                    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start    
                    case TOTAL_PERIOD_MONTHS:
                        return new Double(budgetPeriodBean.getNoOfPeriodMonths());  
                    // 3197 - end    
                }
            }
            }return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(vecData==null) return;
            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecData.get(row);
            int rowIndex = budgetSummaryForm.tblBudgetSummary.getRowCount();
            int index = 0;
            double sumTotCost=0.0;
            double cost;
            String message=null;
            Date date = null;
            String strDate=null;
            switch(column){
                
                case START_DATE_COLUNM:
                    try{
                        if (value.toString().trim().length() > 0) {
                            strDate = dtUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                        } else {
                            budgetPeriodBean.setStartDate(null);
                            return;
                        }
                        strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }
                    catch (CoeusException coeusException) {
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        //budgetSummaryForm.tblBudgetSummary.editCellAt(row, START_DATE_COLUNM);
                        //budgetSummaryEditor.txtDateComponent.requestFocus();
                        return ;
                    }
                    budgetPeriodBean.setStartDate(new java.sql.Date(date.getTime()));
                    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
                    budgetPeriodBean.setNoOfPeriodMonths(getNumberOfMonths(budgetPeriodBean.getStartDate(),budgetPeriodBean.getEndDate()));
                    //3197 - End
                    break;
                case END_DATE_COLUMN:
                    try{
                        
                        if (value.toString().trim().length() > 0) {
                            strDate = dtUtils.formatDate(value.toString(), DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                        } else {
                            budgetPeriodBean.setEndDate(null);
                            return;
                        }
                        strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate == null ) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_END_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }catch (CoeusException coeusException) {
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_END_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }
                    budgetPeriodBean.setEndDate(new java.sql.Date(date.getTime()));
                     //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
                    budgetPeriodBean.setNoOfPeriodMonths(getNumberOfMonths(budgetPeriodBean.getStartDate(),budgetPeriodBean.getEndDate()));
                    //3197 - End
                    break;
                case TOTAL_COST_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    budgetPeriodBean.setTotalCost(cost);
                    calculateChangedCost();
                    break;
                case DIRECT_COST_COLUMN:
                    // double sumDirectCost = 0.0;
                    cost = new Double(value.toString()).doubleValue();
                    //                    for(index = 0; index<rowIndex;index++){
                    //                        if(index==row)sumDirectCost = sumDirectCost + cost;
                    //                        else {
                    //                            sumDirectCost = sumDirectCost + Double.parseDouble(
                    //                            budgetPeriodModel.getValueAt(
                    //                            index,DIRECT_COST_COLUMN).toString());
                    //                            sumTotCost = sumTotCost + cost;
                    //                        }
                    //
                    //                    }
                    
                    if (cost > 0 || budgetPeriodBean.getTotalDirectCost() > 0) {
                        budgetPeriodBean.setTotalCost(cost + Double.parseDouble(
                        (budgetPeriodModel.getValueAt(row,INDIRECT_COST_COLUMN).toString())));
                    }
                    budgetPeriodBean.setTotalDirectCost(cost);
                    calculateChangedCost();
                    
                    break;
                case INDIRECT_COST_COLUMN:
                    //double sumIndirectCost = 0.0;
                    cost = new Double(value.toString()).doubleValue();
                    //                    for(index = 0; index<rowIndex;index++){
                    //                        if(index==row){
                    //                            sumIndirectCost = sumIndirectCost + cost;
                    //                        }else {
                    //                            sumIndirectCost = sumIndirectCost + Double.parseDouble(
                    //                            budgetPeriodModel.getValueAt(
                    //                            index,INDIRECT_COST_COLUMN).toString());
                    //                            sumTotCost = sumTotCost + cost;
                    //                        }
                    //                    }
                    if(cost > 0 || budgetPeriodBean.getTotalIndirectCost() > 0){
                        budgetPeriodBean.setTotalCost(cost + Double.parseDouble(
                        (budgetPeriodModel.getValueAt(row,DIRECT_COST_COLUMN).toString())));
                    }
                    budgetPeriodBean.setTotalIndirectCost(cost);
                    calculateChangedCost();
                    break;
                case COST_SHARING_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    budgetPeriodBean.setCostSharingAmount(cost);
                    calculateChangedCost();
                    break;
                case UNDER_RECOVERY_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    budgetPeriodBean.setUnderRecoveryAmount(cost);
                    calculateChangedCost();
                    break;
               
            }
            
            modified = true;
            try{
                budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(proposalId+versionNumber,budgetPeriodBean);
                budgetPeriodModel.fireTableRowsUpdated(row,row);
                /** The data are changed in the budget periods Fire the bean event to update
                 *the values
                 */
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(budgetPeriodBean);
                beanEvent.setSource(eventSource);
                fireBeanUpdated(beanEvent);
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
    }// End of Custom table model class
    
    
    /** This method is to specify the table header and their sizes
     */
    private void  setTableEditors() {
        
        JTableHeader tableHeader = budgetSummaryForm.tblBudgetSummary.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        budgetSummaryForm.tblBudgetSummary.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        budgetSummaryForm.tblBudgetSummary.setRowHeight(22);
        budgetSummaryForm.tblBudgetSummary.setSelectionBackground(java.awt.Color.white);
        budgetSummaryForm.tblBudgetSummary.setSelectionForeground(java.awt.Color.black);
        budgetSummaryForm.tblBudgetSummary.setShowHorizontalLines(true);
        budgetSummaryForm.tblBudgetSummary.setShowVerticalLines(true);
        budgetSummaryForm.tblBudgetSummary.setOpaque(false);
        budgetSummaryForm.tblBudgetSummary.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
         
        
        TableColumn column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(0);
        
        //Added with case 3197: Allow for the generation of Project period greater than 12 months -Start
//        column.setHeaderRenderer(new DefaultTableCellRenderer() {
//            
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                    boolean isSelected, boolean hasFocus, int row, int column) {
//                setFont(table.getTableHeader().getFont());
//                setText((value == null) ? "" : value.toString());
//                setPreferredSize(new Dimension(50,35));
//                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
//                setHorizontalAlignment(JLabel.CENTER);
//                return this;
//            }
//            
//        });
        //Added with case 3197: Allow for the generation of Project period greater than 12 months -End
        column.setMinWidth(55);
        column.setMaxWidth(150);
        column.setPreferredWidth(55);
        column.setResizable(false);
        column.setCellRenderer(columnPeriodRenderer);
        
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(1);
        column.setMinWidth(85);
        column.setMaxWidth(350);
        column.setPreferredWidth(80);
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        column.setResizable(false);
        
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(2);
        column.setMinWidth(85);
        column.setMaxWidth(350);
        column.setPreferredWidth(80);
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        column.setResizable(false);
        
        //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start   
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(3);
        column.setMinWidth(96);
        column.setMaxWidth(350);
        column.setPreferredWidth(96);
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        column.setResizable(false);
        // 3197 - end      
               
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(4);
        column.setMinWidth(100);
        column.setMaxWidth(350);
        column.setPreferredWidth(105);        
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        column.setResizable(false);
        
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(5);
        column.setMinWidth(100);
        column.setMaxWidth(350);
        column.setPreferredWidth(105);
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(6);
        column.setMinWidth(100);
        column.setMaxWidth(350);
        column.setPreferredWidth(105);
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        column.setResizable(false);
        
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(7);
        column.setMinWidth(100);
        column.setMaxWidth(350);
        column.setPreferredWidth(105);
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        
        column = budgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(8);
        column.setMinWidth(108);
        column.setMaxWidth(350);
        column.setPreferredWidth(105);
        column.setCellEditor(budgetSummaryEditor);
        column.setCellRenderer(columnPeriodRenderer);
        //column.setResizable(false);
    }
    
    /** register all the components for actions to be taken
     */
    public void  registerComponents(){
        
        addBeanUpdatedListener(this, BudgetPeriodBean.class);
        addBeanUpdatedListener(this, BudgetInfoBean.class);
        addBeanUpdatedListener(this, BudgetDetailBean.class);
        
        budgetSummaryForm.txtStartDate.addFocusListener(new CustomFocusAdapter());
        budgetSummaryForm.txtEndDate.addFocusListener(new CustomFocusAdapter());
        budgetSummaryForm.cmbBudgetStatus.addItemListener(this);
        budgetSummaryForm.chkFinal.addItemListener(this);
        budgetSummaryForm.chkBudgetModular.addItemListener(this);
        
        // Travel all the components while pressing tab button
        //Code commented and modified for Case#3472 - Sync to Direct Cost Limit - starts
        //For adding total direct cost limit
//        java.awt.Component[] components = {budgetSummaryForm.chkFinal,
//        budgetSummaryForm.txtStartDate,
//        budgetSummaryForm.txtEndDate,
//        budgetSummaryForm.txtResidualFunds,
//        budgetSummaryForm.txtTotalCostLimit,
//        budgetSummaryForm.cmbOHRateType,
//        budgetSummaryForm.cmbBudgetStatus,
//        budgetSummaryForm.txtArComments,
//        budgetSummaryForm.cmbURRateType,
//        budgetSummaryForm.chkBudgetModular,
//        budgetSummaryForm.cmbOnOffFlag,                      // Case id# 2924
//        budgetSummaryForm.tblBudgetSummary};
        java.awt.Component[] components = {budgetSummaryForm.chkFinal,
        budgetSummaryForm.txtStartDate,
        budgetSummaryForm.txtEndDate,
        budgetSummaryForm.txtResidualFunds,
        budgetSummaryForm.txtTotalCostLimit,
        budgetSummaryForm.cmbOHRateType,
        budgetSummaryForm.txtTotalDirectCostLimit,
        budgetSummaryForm.txtArComments,
        budgetSummaryForm.cmbBudgetStatus,
        budgetSummaryForm.cmbURRateType,
        budgetSummaryForm.cmbOnOffFlag,                      // Case id# 2924
        budgetSummaryForm.chkBudgetModular,
        budgetSummaryForm.tblBudgetSummary,
        budgetSummaryForm.chkSubmitCostSharing};  //COEUSQA-1693 - Cost Sharing Submission 
        //Code commented and modified for Case#3472 - Sync to Direct Cost Limit - ends
        traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetSummaryForm.pnlBudgetSummary.setFocusTraversalPolicy(traversePolicy);
        budgetSummaryForm.pnlBudgetSummary.setFocusCycleRoot(true);
             
     //Added for-COEUSQA-2698-Confirm "Final" Designation Message displays multiple times-start
        budgetSummaryForm.chkFinal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                
                if(budgetSummaryForm.chkFinal.isSelected()){
                    performCheckedAction();
                } else {
                    performUncheckAction();
                }                
            }
            
        });
     //Added for-COEUSQA-2698-Confirm "Final" Designation Message displays multiple times-end
    }
    
    
    
    /** perform field formatting.It is a overridden method of the Controller
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            budgetSummaryForm.txtStartDate.setEditable(false);
            budgetSummaryForm.txtEndDate.setEditable(false);
            budgetSummaryForm.txtTotalCost.setEditable(false);
            budgetSummaryForm.txtDirectCost.setEditable(false);
            budgetSummaryForm.txtIndirectCost.setEditable(false);
            budgetSummaryForm.txtCostSharing.setEditable(false);
            budgetSummaryForm.txtUnderRecovery.setEditable(false);
            budgetSummaryForm.txtTotalCostLimit.setEditable(false);
            budgetSummaryForm.txtVersion.setEditable(false);
            budgetSummaryForm.cmbOHRateType.setEditable(false);
            budgetSummaryForm.cmbURRateType.setEditable(false);
            budgetSummaryForm.cmbBudgetStatus.setEnabled(false);
            budgetSummaryForm.chkFinal.setEnabled(false);
            budgetSummaryForm.txtResidualFunds.setEditable(false);
            budgetSummaryForm.tblBudgetSummary.setEnabled(false);
            budgetSummaryForm.txtArComments.setEditable(false);
            budgetSummaryForm.txtArComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            budgetSummaryForm.tblBudgetSummary.setShowHorizontalLines(true);
            budgetSummaryForm.tblBudgetSummary.setShowVerticalLines(true);
            budgetSummaryForm.cmbOHRateType.setEnabled(false);
            budgetSummaryForm.cmbURRateType.setEnabled(false);
            budgetSummaryForm.chkBudgetModular.setEnabled(false);
            budgetSummaryForm.cmbOnOffFlag.setEnabled(false);       // Case id# 2924
            budgetSummaryForm.cmbOnOffFlag.setEditable(false);      // Case id# 2924            
            //Code added for Case#3472 - Sync to Direct Cost Limit
            //For adding total direct cost limit
            budgetSummaryForm.txtTotalDirectCostLimit.setEditable(false);
            budgetSummaryForm.chkSubmitCostSharing.setEnabled(false);  //COEUSQA-1693 - Cost Sharing Submission 
        }
        //Commented with COEUSDEV-203:Premium Budget marked Final & Complete on Summary tab: Complete won't stick.
//        else if(getFunctionType() == TypeConstants.MODIFY_MODE){
            //For future Use
            
//        }else if(getFunctionType() == TypeConstants.ADD_MODE){
//            if(budgetInfoBean.getVersionNumber()==1){
//                budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
//            }
//        }
        //COEUSDEV-203 End
    }
    
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == TypeConstants.DISPLAY_MODE )) {
            budgetSummaryForm.txtStartDate.requestFocusInWindow();
        }
    }
    
    /** Returns the Form component.It is a overridden method of the controller
     */
    public Component getControlledUI() {
        //if in Add Mode update Budget Info bean with ohrate class code.
        if(getFunctionType()==TypeConstants.ADD_MODE && budgetInfoBean.getOhRateClassCode() == -1){
            if(vecOHRate!= null && vecOHRate.size() > 0){
                RateClassBean rateClassBean = (RateClassBean)vecOHRate.get(0);
                budgetInfoBean.setOhRateClassCode(Integer.parseInt(rateClassBean.getCode()));
                budgetInfoBean.setUrRateClassCode(Integer.parseInt(rateClassBean.getCode()));
            }
            //Commented since this value is going to be removed from Budget table
            //budgetInfoBean.setUrRateTypeCode(urRateType);
            isOHCancelling = true;
            try{
                budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(proposalId+versionNumber, budgetInfoBean);
            }catch(CoeusException coeusException){
                coeusException.getMessage();
            }
            isOHCancelling = true;
            if(budgetSummaryForm.cmbOHRateType.getSelectedIndex()!= -1){
                budgetSummaryForm.cmbOHRateType.setSelectedIndex(1);
            }
            //            else{
            //                budgetSummaryForm.cmbOHRateType.setSelectedItem(EMPTY_STRING);
            //            }
            isOHCancelling = true;
            if(budgetSummaryForm.cmbURRateType.getSelectedIndex()!= -1){
                budgetSummaryForm.cmbURRateType.setSelectedIndex(1);
            }
         // Case id# 2924 - start
            isOHCancelling = true;            
            if (budgetSummaryForm.cmbOnOffFlag.getSelectedIndex() != -1) {
                budgetSummaryForm.cmbOnOffFlag.setSelectedItem(FLAG_DEFAULT); 
             }
        // Case id# 2924 - end
            isOHCancelling = false;
        }
        if(!parent.isValidatingBudgetSummary()){//case 2158
            if( budgetStatus != null ) {
                if( budgetStatus.equals(INCOMPLETE) ){
                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                }else if( budgetStatus.equals(COMPLETE) ){
                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_COMPLETE);
                }else if( budgetStatus.equals(NONE) ){
                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_NONE);
                }
            }else{
                budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_NONE);
            }
        }
        return budgetSummaryForm;
    }
    
    
    /** listens to Combo Box Item Selection Changes.
     * @param itemEvent Item Event
     */
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        budgetSummaryEditor.stopCellEditing();
        //Added for Case#2924-On/Off Campus flag - Personnel budget - starts
        if(!errMsgShown){
            return;
        }
        ////Added for Case#2924-On/Off Campus flag - Personnel budget - ends        
        if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
            if (source==budgetSummaryForm.cmbBudgetStatus) {
                oldBudgetStatus = (String) itemEvent.getItem();
                return;
            } else if (source ==budgetSummaryForm.cmbOHRateType) {
                oldOhRate = itemEvent.getItem();
                
            }else if(source==budgetSummaryForm.cmbURRateType){
                oldUrRate=itemEvent.getItem();
            } else if (source==budgetSummaryForm.cmbOnOffFlag) {  // Case id #2924
                oldOnOffFlag=itemEvent.getItem();                 // Case id #2924
            }
        }
        
        if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            if(source ==budgetSummaryForm.cmbOHRateType){
                // Added for Case#3404-Outstanding proposal hierarchy changes - Start
                if(!isParentProposal()){
                // Added for Case#3404-Outstanding proposal hierarchy changes - End
                if (isOHCancelling) {
                    isOHCancelling = false;
                    return;
                }
                mesg = CHANGING_OH_RATE;
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                //statusModified = true;
                if(selectedOption == CoeusOptionPane.SELECTION_YES){
                    int newOhRate = Integer.parseInt(((ComboBoxBean)
                    budgetSummaryForm.cmbOHRateType.getSelectedItem()).getCode());
                    budgetInfoBean.setOhRateClassCode(newOhRate);
                    budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                    try{
                        queryEngine.update(proposalId+versionNumber, budgetInfoBean);
                        changeLineItemOHRate(newOhRate);
                        // Calculate the changed values after changing the Oh Rate class
                        calculate(proposalId+versionNumber, CALCULATE_ALL_PERIODS);
                        refreshRequired = true;
                        isOHCancelling = true;
                        refresh();
                        refreshRequired = false;
                        isOHCancelling = false;
                        //Firing the  bean for changing in the OH Rates
                        BeanEvent beanEvent = new BeanEvent();
                        beanEvent.setBean(budgetInfoBean);
                        beanEvent.setSource(this);
                        fireBeanUpdated(beanEvent);
                    }catch(Exception exception){
                        CoeusOptionPane.showErrorDialog(exception.getMessage());
                        exception.printStackTrace();
                    }
                }else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    isOHCancelling = true;
                    budgetSummaryForm.cmbOHRateType.setSelectedItem(oldOhRate);
                }
                // Added for Case#3404-Outstanding proposal hierarchy changes - Start
            }else{
                    int newOhRate = Integer.parseInt(((ComboBoxBean)
                    budgetSummaryForm.cmbOHRateType.getSelectedItem()).getCode());
                    budgetInfoBean.setOhRateClassCode(newOhRate);
                    budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
                // Added for Case#3404-Outstanding proposal hierarchy changes - End
                
            }else if(source.equals(budgetSummaryForm.cmbBudgetStatus)){
                //Commented for Case#3404-Outstanding proposal hierarchy changes - Start
                if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem().equals(VAL_COMPLETE)) {
                    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 3
                     */
                    //if(budgetInfoBean.getUnderRecoveryAmount() != budgetInfoBean.getTotalIDCRateDistribution())
                    getParameterForDistribution();
                    //Commented for Case#3404-Outstanding proposal hierarchy changes - Start
//                    if(budgetInfoBean.getUnderRecoveryAmount() != budgetInfoBean.getTotalIDCRateDistribution()){
//                        if(budgetInfoBean.getUnderRecoveryAmount() > 0.00){
//                            if(isForceUnderRecovery()){
//                                /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 3
//                                 */
//                                mesg = coeusMessageResources.parseMessageKey(UNDER_RECOVERY_DISTRIBUTION);
//                                CoeusOptionPane.showErrorDialog(mesg);
//                                budgetSummaryForm.cmbBudgetStatus.removeItemListener(this);
//                                budgetSummaryForm.cmbBudgetStatus.setSelectedItem(oldBudgetStatus);
//                                budgetSummaryForm.cmbBudgetStatus.addItemListener(this);
//                            }
//                        }
//                    }/** Case#1801 :Parameterize Under-recovery and cost-sharing distribution Start 4
//                     */
//                    //else if(budgetInfoBean.getCostSharingAmount() != budgetInfoBean.getTotalCostSharingDistribution()
//                    if(budgetInfoBean.getCostSharingAmount() != budgetInfoBean.getTotalCostSharingDistribution()){
//                        if(isForceCostSharing()){
//                            /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 4
//                             */
//                            mesg = coeusMessageResources.parseMessageKey(COST_SHARING_DISTRIBUTION);
//                            CoeusOptionPane.showErrorDialog(mesg);
//                            budgetSummaryForm.cmbBudgetStatus.removeItemListener(this);
//                            budgetSummaryForm.cmbBudgetStatus.setSelectedItem(oldBudgetStatus);
//                            budgetSummaryForm.cmbBudgetStatus.addItemListener(this);
//                        }
//                    }else {
//                        budgetSummaryForm.chkFinal.setEnabled(false);
//                    }
////                    // Case Id 1626 - start
////                    if(budgetSummaryForm.chkBudgetModular.isSelected()){
////                        try{
////                            CoeusVector cvBudgetPeriod = queryEngine.executeQuery(queryKey,BudgetPeriodBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
////                            CoeusVector cvBudgetModular  = queryEngine.executeQuery(queryKey,BudgetModularBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
////                            if(cvBudgetModular!= null && cvBudgetModular.size() > 0){
////                                if(cvBudgetModular.size()!= cvBudgetPeriod.size()){
////                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INCOMPLETE_MODULAR_BUDGET));
////                                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(oldBudgetStatus);
////                                    budgetSummaryForm.chkBudgetModular.setSelected(true);
////                                }else{
////                                    budgetInfoBean.setBudgetModularFlag(true);
////                                }
////                            }else{
////                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_MODULAR_BUDGET));
////                                if(oldBudgetStatus.equals(VAL_COMPLETE)){
////                                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
////                                }else{
////                                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(oldBudgetStatus);
////                                }
////                                budgetSummaryForm.chkBudgetModular.setSelected(true);
////                            }
////                        }catch (CoeusException ex){
////                            ex.printStackTrace();
////                            CoeusOptionPane.showErrorDialog(ex.getMessage());
////                        }
////                    }// Case Id 1626 - End
//                    //Commented for Case#3404-Outstanding proposal hierarchy changes -End  
                    //Added for Case#3404-Outstanding proposal hierarchy changes - Start
                             String message = EMPTY_STRING;                                                                         
                             if(!budgetSummaryForm.chkFinal.isSelected()){
                                 // If no final version is selected, diplay message
                                 message = coeusMessageResources.parseMessageKey(SELECT_FINAL_VERSION);
                                 CoeusOptionPane.showInfoDialog(message);
                                 budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                                 return;
                             }else{                                                             
                                 boolean hasUnderRecovery = budgetInfoBean.isHasIDCRateDistribution();
                                 double underRecoveryAmount = budgetInfoBean.getUnderRecoveryAmount();
                                 if ( hasUnderRecovery ){
                                     if ( underRecoveryAmount != budgetInfoBean.getTotalIDCRateDistribution() ){
                                         message = coeusMessageResources.parseMessageKey("budgetSummary_exceptionCode.1112");
                                         CoeusOptionPane.showInfoDialog(message);
                                         budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                                         return;
                                     }
                                 }else if ( underRecoveryAmount != budgetInfoBean.getTotalIDCRateDistribution() ){
                                     if(isForceUnderRecovery() && underRecoveryAmount > 0.00){
                                         message = coeusMessageResources.parseMessageKey("budgetSummary_messageCode.1123");
                                         CoeusOptionPane.showInfoDialog(message);
                                         budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);                                         
                                         return;
                                     }
                                 }
                                 boolean hasCostSharing = budgetInfoBean.getHasCostSharingDistribution();
                                 double costSharingAmount = budgetInfoBean.getCostSharingAmount();
                                 if (hasCostSharing){
                                     if ( costSharingAmount != budgetInfoBean.getTotalCostSharingDistribution() ){
                                         if(isForceCostSharing()){
                                             message = coeusMessageResources.parseMessageKey("budgetSummary_exceptionCode.1113");
                                             CoeusOptionPane.showInfoDialog(message);
                                             budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                                             return;
                                         }
                                     }
                                 }else if ( costSharingAmount != budgetInfoBean.getTotalCostSharingDistribution() ){
                                     if(isForceCostSharing()){
                                         message = coeusMessageResources.parseMessageKey("budgetSummary_messageCode.1124");
                                         CoeusOptionPane.showInfoDialog(message);
                                         budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                                         return;
                                     }
                                 }
                                 //Code modified for case#2938 - Proposal Hierarchy enhancement
                                 if(!isBudgetComplete()){
                                     if(isParentProposal()){
                                         message = "Please make all child proposal budget status as Complete";
                                         CoeusOptionPane.showInfoDialog(message);
                                         budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                                         return;
                                     }
                                 }
                                 //Added for 2158: Run validation checks on changing the budget status to Complete
                                 //Modified with case 2158: Budget Validations
                                 boolean valid = true;
                                     Thread thread = new Thread(new Runnable() {
                                         public void run() {
                                             if(!parent.validateSummaryStatusChange()){
                                                return;
                                             }
                                         }});
                                         thread.start();
                                 //2158 End
                                 if( budgetSummaryForm.cmbBudgetStatus.getSelectedItem().toString().charAt(0) != budgetStatus.charAt(0) ){
                                     //statusModified = true;
                                     setSaveRequired(true);
                                     proposalDevelopmentFormBean.setAcType("U");
                                     proposalDevelopmentFormBean.setBudgetStatus(COMPLETE );
                                     try {
                                         queryEngine.update(proposalId+versionNumber, proposalDevelopmentFormBean);
                                     } catch (CoeusException ex) {
                                         ex.printStackTrace();
                                     }
                                 }
                                 budgetSummaryForm.chkFinal.setEnabled(false);
                             }
                             
//                             /**Case Id 1626 - Check for the modular budget validation
//                              *Start
//                              */
//                             HashMap modularData = null;
//                             CoeusVector cvBudgetPeriods = null;
//                             CoeusVector cvModularBudget = null;
//                             try{
//                                 if(budgetInfoBean.isBudgetModularFlag()){
//                                     // Get the HashMap of BudgetPeriods and ModularBudget
//                                     modularData = getModularData(budgetInfoBean);
//                                     cvBudgetPeriods = (CoeusVector)modularData.get(BudgetPeriodBean.class);
//                                     cvModularBudget = (CoeusVector)modularData.get(BudgetModularBean.class);
//                                     if(cvModularBudget!= null && cvModularBudget.size() > 0){
//                                         if(cvBudgetPeriods!= null && cvBudgetPeriods.size() >0){
//                                             if(cvModularBudget.size() != cvBudgetPeriods.size()){
//                                                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INCOMPLETE_MODULAR_BUDGET));
//                                                 budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
//                                                 //Code added for case#2938 - Proposal Hierarchy enhancement
//                                                 //To set the budget status data to INCOMPLETE
//                                                 proposalDevelopmentFormBean.setBudgetStatus(INCOMPLETE);
//                                                 return ;
//                                             }
//                                         }
//                                     }else{
//                                         // If no Modular Budget data available
//                                         CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_MODULAR_BUDGET));
//                                         budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
//                                         //Code added for case#2938 - Proposal Hierarchy enhancement
//                                         //To set the budget status data to INCOMPLETE
//                                         proposalDevelopmentFormBean.setBudgetStatus(INCOMPLETE);//
//                                         return ;
//                                     }
//                                 }
//                             }catch (CoeusClientException ex){
//                                 ex.printStackTrace();
//                                 CoeusOptionPane.showErrorDialog(ex.getMessage());
//                             }// End of Case Id 1626
                             //Added for Case#3404-Outstanding proposal hierarchy changes - End
                }else{
                    if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem().equals(VAL_NONE)){
                        //Allow the user to set None status only if it is the first budget; that too prior to saving.
                        //Once any version is saved, user should never be allowed to select None status.
                        //Fix added with COEUSDEV-203:Premium Budget marked Final & Complete on Summary tab: Complete won't stick
                        if(versionNumber>1 ||  (TypeConstants.UPDATE_RECORD.equals(budgetBean.getAcType())
                                            || (getFunctionType()==TypeConstants.MODIFY_MODE))){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_NONE_NOT_ALLOWED));
                            budgetSummaryForm.cmbBudgetStatus.setSelectedItem((VAL_INCOMPLETE));
                            return;
                        }
                        //COEUSDEV-203 End
                    }
                    if( isParentProposal() && !isBudgetComplete()){
                        budgetSummaryForm.chkFinal.setEnabled(false);
                    }else{
                        budgetSummaryForm.chkFinal.setEnabled(true);
                    }                    
                }             
            }
//            else if(source.equals(budgetSummaryForm.chkBudgetModular)){
//                try{// case Id 1626
//                    if(budgetSummaryForm.chkBudgetModular.isSelected()){
//                        Object selectedItem = budgetSummaryForm.cmbBudgetStatus.getSelectedItem();
//                        if(selectedItem.equals(VAL_COMPLETE)){
//                            CoeusVector cvBudgetPeriod = queryEngine.executeQuery(queryKey,BudgetPeriodBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
//                            CoeusVector cvBudgetModular  = queryEngine.executeQuery(queryKey,BudgetModularBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
//                            if(cvBudgetPeriod!=null && cvBudgetPeriod.size() >0){
//                                if(cvBudgetModular!= null && cvBudgetModular.size() >0 ){
//                                    if(cvBudgetModular.size()!= cvBudgetPeriod.size()){
//                                        CoeusOptionPane.showInfoDialog(
//                                        coeusMessageResources.parseMessageKey(INCOMPLETE_MODULAR_BUDGET));
//                                        if(oldBudgetStatus.equals(VAL_COMPLETE)){
//                                            budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
//                                        }else{
//                                            budgetSummaryForm.cmbBudgetStatus.setSelectedItem(oldBudgetStatus);
//                                        }
//                                        
//                                        budgetSummaryForm.chkBudgetModular.setSelected(true);
//                                    }else{
//                                        budgetInfoBean.setBudgetModularFlag(true);
//                                    }
//                                }else{
//                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_MODULAR_BUDGET));
//                                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(oldBudgetStatus);
//                                    budgetSummaryForm.chkBudgetModular.setSelected(true);
//                                }
//                            }
//                        }
//                    }
//                }catch (CoeusException exception){
//                    exception.printStackTrace();
//                    CoeusOptionPane.showErrorDialog(exception.getMessage());
//                }
//            }//case id 1626 end
            else if(source == budgetSummaryForm.cmbURRateType){
                // Added for Case#3404-Outstanding proposal hierarchy changes - Start
                if(!isParentProposal()){
                // Added for Case#3404-Outstanding proposal hierarchy changes - End
                try{
                    if (isOHCancelling) {
                        isOHCancelling = false;
                        return;
                    }
                    mesg = CHANGING_UR_RATE;
                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(mesg),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    
                    if(selectedOption == CoeusOptionPane.SELECTION_YES){
                        int newUrRate = Integer.parseInt(((ComboBoxBean)
                        budgetSummaryForm.cmbURRateType.getSelectedItem()).getCode());
                        int urRateClass = budgetInfoBean.getUrRateClassCode();
                        // Server side call for the UR Rate Type
                        //int urRateType = getRateClassForRateType(urRateClass);
                        urRateType = getRateClassForRateType(urRateClass);
                        
                        budgetInfoBean.setUrRateClassCode(newUrRate);
                        //Commented since this value is going to be removed from Budget table
                        //budgetInfoBean.setUrRateTypeCode(urRateType);
                        
                        budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(proposalId+versionNumber, budgetInfoBean);
                        
                        // Calculate the changed values after changing the UR Rate class
                        calculate(proposalId+versionNumber, CALCULATE_ALL_PERIODS);
                        refreshRequired = true;
                        isOHCancelling = true;
                        refresh();
                        refreshRequired = false;
                        isOHCancelling = false;
                        //Firing the  bean for changing in the OH Rates
                        BeanEvent beanEvent = new BeanEvent();
                        beanEvent.setBean(budgetInfoBean);
                        beanEvent.setSource(this);
                        fireBeanUpdated(beanEvent);
                        
                    }else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                        isOHCancelling = true;
                        budgetSummaryForm.cmbURRateType.setSelectedItem(oldUrRate);
                    }
                }catch(CoeusException coeusException){
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                    coeusException.printStackTrace();
                }catch(CoeusClientException coeusClientException){
                    CoeusOptionPane.showDialog(coeusClientException);
                    coeusClientException.printStackTrace();
                    
                }
                // Added for Case#3404-Outstanding proposal hierarchy changes - Start
            }else{
                    int newUrRate = Integer.parseInt(((ComboBoxBean)
                    budgetSummaryForm.cmbURRateType.getSelectedItem()).getCode());
                    budgetInfoBean.setUrRateClassCode(newUrRate);                        
                    budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
                // Added for Case#3404-Outstanding proposal hierarchy changes - End
            }
        }//else if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
        // Case id# 2924 - start
        else {
            if(source == budgetSummaryForm.cmbOnOffFlag){
                try{
                    if (isOHCancelling) {
                        isOHCancelling = false;
                        return;
                    }
                    mesg = CHANGING_ONOFF;
                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(mesg),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
                    boolean defaultIndicator = false;   
                    budgetInfoBean.setDefaultIndicator(false);
                    if(selectedOption == CoeusOptionPane.SELECTION_YES){
                        String onOff = (String)budgetSummaryForm.cmbOnOffFlag.getSelectedItem();                      
                        if(onOff == FLAG_ON) {
                            budgetInfoBean.setOnOffCampusFlag(true);
                        }else if(onOff == FLAG_OFF){
                            budgetInfoBean.setOnOffCampusFlag(false);
                        }else if(onOff == FLAG_DEFAULT){
                            budgetInfoBean.setDefaultIndicator(true);
                            defaultIndicator = true;
                        }
                        budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(proposalId+versionNumber, budgetInfoBean);
                        
                        changeLineItemOnOff(budgetInfoBean.isOnOffCampusFlag(), defaultIndicator);
                        //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
                        // Calculate the changed values after changing the On/Off Campus Flag
                        calculate(proposalId+versionNumber, CALCULATE_ALL_PERIODS);
                        refreshRequired = true;
                        isOHCancelling = true;
                        refresh();
                        refreshRequired = false;
                        isOHCancelling = false;
                        //Firing the  bean for changing in the OH Rates
                        BeanEvent beanEvent = new BeanEvent();
                        beanEvent.setBean(budgetInfoBean);
                        beanEvent.setSource(this);
                        fireBeanUpdated(beanEvent);
                        
                    }else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                        isOHCancelling = true;
                        budgetSummaryForm.cmbOnOffFlag.setSelectedItem(oldOnOffFlag);
                    }
                }catch(CoeusException coeusException){
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                    coeusException.printStackTrace();                
                }
            }
        }
        // Case id# 2924 - end
    }
   
    /** This method will update, delete the bean values when the OH Rate class is changed
     */
    private void changeLineItemOHRate(int newOhRateClass) throws CoeusClientException{
        try{
            BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
            BudgetPersonnelCalAmountsBean personnelCalAmts = new BudgetPersonnelCalAmountsBean();
            BudgetPersonnelDetailsBean personnelDetails = new BudgetPersonnelDetailsBean();
            BudgetDetailCalAmountsBean detailCalAmts = new BudgetDetailCalAmountsBean();
            
            String key = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();
            String costElement = EMPTY_STRING;
            Equals ohEquals = new Equals(RATE_CLASS_TYPE, RateClassTypeConstants.OVERHEAD);
            
            CoeusVector cvBudgetDetails = queryEngine.executeQuery(key, BudgetDetailBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvDetailCalAmts = queryEngine.getActiveData(key, BudgetDetailCalAmountsBean.class, ohEquals);
            CoeusVector cvPersonnelCalAmts = queryEngine.getActiveData(key, BudgetPersonnelCalAmountsBean.class, ohEquals);
            CoeusVector cvChangedCalAmts = new CoeusVector();
            CoeusVector cvChangedPersonnelCalAmts = new CoeusVector();
            CoeusVector cvOhRates = new CoeusVector();
            
            int lineItemNo = 0;
            int budgetPeriod = 0;
            Equals liEquals = new Equals(LINE_ITEM_NUMBER, new Integer(lineItemNo));
            Equals periodEquals = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
            Equals newOhRcEquals = new Equals(RATE_CLASS_CODE, new Integer(newOhRateClass));
            And liAndPeriod;
            And liAndOh;
            And liAndPeriodAndOh;
            
            Vector vecCostElements = new Vector();
            Hashtable htCeOhRates = null;
            
            //get all the cost elements in a vector
            if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
                int size = cvBudgetDetails.size();
                for(int index = 0; index < size; index++) {
                    budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                    vecCostElements.add(budgetDetailBean.getCostElement());
                }
                
                //now get the OH rates for these cost elements by calling the database
                try {
                    htCeOhRates = (Hashtable) getCostElementOHRates(vecCostElements);
                } catch(CoeusClientException ce) {
                    ce.getMessage();
                }
                
                /**
                 *Loop thru all the line items. Remove old OH Rate. Then check
                 *whether the new OH Rate is present in the hashtable. If present then
                 *add it as a new BudgetCalAmt bean.
                 *If the line item has Personnel line items, then change the OH for
                 *each personnel line item.
                 */
                
                for(int index = 0; index < size; index++) {
                    budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                    budgetPeriod = budgetDetailBean.getBudgetPeriod();
                    lineItemNo = budgetDetailBean.getLineItemNumber();
                    costElement = budgetDetailBean.getCostElement();
                    liEquals = new Equals("lineItemNumber", new Integer(lineItemNo));
                    periodEquals = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    liAndPeriod = new And(periodEquals, liEquals);
                    liAndOh = new And(liEquals, ohEquals);
                    liAndPeriodAndOh = new And(liAndPeriod, ohEquals);
                    
                    CoeusVector cvCalAmtsOHRate = cvDetailCalAmts.filter(liAndPeriodAndOh);
                    CoeusVector cvPersonnelCalAmtsOHRate = cvPersonnelCalAmts.filter(liAndPeriodAndOh);
                    
                    //check whether old OH rate is present, if present delete it
                    if (cvCalAmtsOHRate != null && cvCalAmtsOHRate.size() > 0) {
                        detailCalAmts = (BudgetDetailCalAmountsBean) cvCalAmtsOHRate.get(0);
                        detailCalAmts.setAcType(TypeConstants.DELETE_RECORD);
                        cvChangedCalAmts.add(detailCalAmts);
                        
                        //check whether personnel line item has details, then delete that too
                        if (cvPersonnelCalAmtsOHRate != null && cvPersonnelCalAmtsOHRate.size() > 0) {
                            int pSize = cvPersonnelCalAmtsOHRate.size();
                            for (int ohIndex = 0; ohIndex < pSize; ohIndex++) {
                                personnelCalAmts = (BudgetPersonnelCalAmountsBean)
                                cvPersonnelCalAmtsOHRate.get(ohIndex);
                                personnelCalAmts.setAcType(TypeConstants.DELETE_RECORD);
                                cvChangedPersonnelCalAmts.add(personnelCalAmts);
                            }
                        }
                        
                    }
                    
                    //check whether the new OH rate is present for the Cost Element
                    if (htCeOhRates != null && costElement!=null) {
                        cvOhRates = (CoeusVector) htCeOhRates.get(costElement);
                        //Bug Fix -  Start
                        if(cvOhRates == null || cvOhRates.size() == 0) {
                            cvOhRates = new CoeusVector();
                        }
                        //Bug Fix -  End
                        cvOhRates = cvOhRates.filter(newOhRcEquals);
                        
                        //if present create a new detail cal amt bean and add to cvChangedCalAmts
                        if (cvOhRates != null && cvOhRates.size() > 0) {
                            ValidCERateTypesBean validCERateTypes =
                            (ValidCERateTypesBean) cvOhRates.get(0);
                            int rateTypeCode = validCERateTypes.getRateTypeCode();
                            detailCalAmts = new BudgetDetailCalAmountsBean();
                            detailCalAmts.setProposalNumber(budgetDetailBean.getProposalNumber());
                            detailCalAmts.setVersionNumber(budgetDetailBean.getVersionNumber());
                            detailCalAmts.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                            detailCalAmts.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                            detailCalAmts.setRateClassType(RateClassTypeConstants.OVERHEAD);
                            detailCalAmts.setRateClassCode(newOhRateClass);
                            detailCalAmts.setRateClassDescription(validCERateTypes.getRateClassDescription());
                            detailCalAmts.setRateTypeCode(rateTypeCode);
                            detailCalAmts.setRateTypeDescription(validCERateTypes.getRateTypeDescription());
                            detailCalAmts.setAcType(TypeConstants.INSERT_RECORD);
                            detailCalAmts.setApplyRateFlag(true);
                            cvChangedCalAmts.add(detailCalAmts);
                            
                            /**
                             * check whether personnel line item details are present
                             * if present add the new OH Rate for each personnel
                             * line item
                             */
                            CoeusVector cvPersonnelDetails = queryEngine.getActiveData(key, BudgetPersonnelDetailsBean.class, liAndPeriod);
                            if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                                //if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                                int pSize = cvPersonnelDetails.size();
                                int personNo = 0;
                                for (int pIndex = 0; pIndex < pSize;  pIndex++) {
                                    personnelDetails = (BudgetPersonnelDetailsBean)
                                    cvPersonnelDetails.get(pIndex);
                                    personNo = personnelDetails.getPersonNumber();
                                    personnelCalAmts = new BudgetPersonnelCalAmountsBean();
                                    personnelCalAmts.setProposalNumber(budgetDetailBean.getProposalNumber());
                                    personnelCalAmts.setVersionNumber(budgetDetailBean.getVersionNumber());
                                    personnelCalAmts.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                                    personnelCalAmts.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                                    personnelCalAmts.setPersonNumber(personNo);
                                    personnelCalAmts.setRateClassType(RateClassTypeConstants.OVERHEAD);
                                    personnelCalAmts.setRateClassCode(newOhRateClass);
                                    personnelCalAmts.setRateClassDescription(validCERateTypes.getRateClassDescription());
                                    personnelCalAmts.setRateTypeCode(rateTypeCode);
                                    personnelCalAmts.setRateTypeDescription(validCERateTypes.getRateTypeDescription());
                                    personnelCalAmts.setApplyRateFlag(true);
                                    personnelCalAmts.setAcType(TypeConstants.INSERT_RECORD);
                                    cvChangedPersonnelCalAmts.add(personnelCalAmts);
                                }
                            }
                        }
                        
                    }
                    
                    
                }
                
                updatOhRateChange(cvChangedCalAmts,cvChangedPersonnelCalAmts);
                
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        
    }
    
   // Case id# 2924 - start
    /* This method will update the bean values when the On/Off campus flag is changed
     */
    private void changeLineItemOnOff(boolean isOnOffCampus, boolean defaultIndicator) {
        try{
            BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
            BudgetPersonnelDetailsBean personnelDetails = new BudgetPersonnelDetailsBean();
            
            String key = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();
            CoeusVector cvBudgetDetails = queryEngine.executeQuery(key, BudgetDetailBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            //Added for case#3654 - Third option 'Default' in the campus dropdown - start
            Vector vecCostElements = null;
            if(defaultIndicator){
                vecCostElements = getCostElements();
            }
            //Added for case#3654 - Third option 'Default' in the campus dropdown - end
            HashMap hmPerioddata = new HashMap();
            HashMap hmOnOffCampusdata = new HashMap();
            // loop through all the budget line item details 
            // and set the onOff campus flag for each budget detail and update it
            //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
            if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
                int size = cvBudgetDetails.size();               
                for(int index = 0; index < size; index++) {
                    budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                    if(defaultIndicator){
                        if( budgetDetailBean.getCategoryType() != ' ' && budgetDetailBean.getCategoryType() == 'P' ){
                            hmOnOffCampusdata.put(new Integer(budgetDetailBean.getLineItemNumber()), budgetDetailBean.getCostElement());
                            hmPerioddata.put(new Integer(budgetDetailBean.getBudgetPeriod()),hmOnOffCampusdata);
                        }
                        budgetDetailBean.setOnOffCampusFlag(getDefaultOnOffFlag(vecCostElements, budgetDetailBean.getCostElement()));
                    }else{
                        budgetDetailBean.setOnOffCampusFlag(isOnOffCampus);
                    }                    
                    queryEngine.update(key, budgetDetailBean);
                }          
            }
            // check whether personnel line item details are present
            // If present, then set the onOff Campus flag for each personnel line item detail
            // and update it
            CoeusVector cvPersonnelDetails = queryEngine.executeQuery(key, BudgetPersonnelDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                int pSize = cvPersonnelDetails.size();
                for (int pIndex = 0; pIndex < pSize;  pIndex++) {
                     personnelDetails = (BudgetPersonnelDetailsBean)cvPersonnelDetails.get(pIndex);
                     if(defaultIndicator){
                        if(hmPerioddata !=null && hmPerioddata.size() > 0 ){
                            if( hmPerioddata.containsKey(new Integer(personnelDetails.getBudgetPeriod()))){
                                HashMap hmOnOffCampusFlagdata = (HashMap) hmPerioddata.get(new Integer(personnelDetails.getBudgetPeriod()));
                                if (hmOnOffCampusFlagdata.get(new Integer(personnelDetails.getLineItemNumber())) !=null){
                                    String strCostElement = (String) hmOnOffCampusFlagdata.get(new Integer(personnelDetails.getLineItemNumber()));
                                     personnelDetails.setOnOffCampusFlag(getDefaultOnOffFlag(vecCostElements, strCostElement));
                                } 
                            }  
                        } 
                     }else{                     
                        personnelDetails.setOnOffCampusFlag(isOnOffCampus);
                     }
                     queryEngine.update(key, personnelDetails);
                }
            }
            //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
        } catch(Exception ex) {
            ex.printStackTrace();
        }        
    }
   // Case id# 2924 - end
    
    /* Update the changed values to the respective bean for Line Item and Personnel details
     */
    private void updatOhRateChange(CoeusVector cvChangedCalAmts,
    CoeusVector cvChangedPersonnelCalAmts) throws Exception{
        
        //Update Budget Detail Cal Amts to the base window
        String key = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();
        int size = 0;
        int index = 0;
        String acType=EMPTY_STRING;
        
        if (cvChangedCalAmts != null && cvChangedCalAmts.size() > 0) {
            size = cvChangedCalAmts.size();
            BudgetDetailCalAmountsBean detailCalAmtsBean;
            for (index = 0; index < size; index++) {
                detailCalAmtsBean = (BudgetDetailCalAmountsBean)
                cvChangedCalAmts.get(index);
                acType = detailCalAmtsBean.getAcType();
                if (acType.equals(TypeConstants.INSERT_RECORD)) {
                    queryEngine.insert(key,detailCalAmtsBean);
                } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                    queryEngine.delete(key,detailCalAmtsBean);
                } else if (acType.equals(TypeConstants.UPDATE_RECORD)) {
                    queryEngine.update(key,detailCalAmtsBean);
                }
            }
        }
        
        //Update Budget Personnel Detail Cal Amts to the base window
        if (cvChangedPersonnelCalAmts != null && cvChangedPersonnelCalAmts.size() > 0) {
            size = cvChangedPersonnelCalAmts.size();
            BudgetPersonnelCalAmountsBean personnelCalAmtsBean;
            for (index = 0; index < size; index++) {
                personnelCalAmtsBean = (BudgetPersonnelCalAmountsBean)
                cvChangedPersonnelCalAmts.get(index);
                acType = personnelCalAmtsBean.getAcType();
                if (acType.equals(TypeConstants.INSERT_RECORD)) {
                    queryEngine.insert(key,personnelCalAmtsBean);
                } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                    queryEngine.delete(key,personnelCalAmtsBean);
                } else if (acType.equals(TypeConstants.UPDATE_RECORD)) {
                    queryEngine.update(key,personnelCalAmtsBean);
                    
                }
            }
        }
    }
    
    
    /** It is a server side call to get the valid cost element details
     */
    private Object getCostElementOHRates(Vector vecCostElements) throws CoeusClientException {
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_VALID_OH_RATE_TYPE);
        request.setDataObject(vecCostElements);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response== null){
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }
        if(response.isSuccessfulResponse()){
            return response.getDataObject();
        }else {
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            
        }
    }
    
    private boolean changeOHRateClass(){
        queryEngine = QueryEngine.getInstance();
        return true;
    }
    
    /** It is an overridden method of the controller. */
    public void display(){
    }
    
    /**
     * This method is used to throw exception with given message.
     *
     * @param mesg String representing the description of the exception.
     *
     * @throws Exception with the given message.
     */
    public void log(String mesg) throws Exception{
        throw new Exception(mesg);
    }
    
    
    /** It is a overridden method of the controller.
     *This method is used to validate the form details like date.
     *This will be triggered while saving and calculating.
     */
    public boolean validate(){
        
        budgetSummaryEditor.stopCellEditing();
        
        double totalCostLimit = (new Double(budgetSummaryForm.txtTotalCostLimit.getValue())).doubleValue();
        double totalCost = (new Double(budgetSummaryForm.txtTotalCost.getValue())).doubleValue();
        Date txtStartDate = null;
        Date txtEndDate = null;
        BudgetPeriodBean budgetPeriodBean, nextBudgetPeriodBean;
        String totCost = budgetSummaryForm.txtTotalCost.getText();
        totCost = totCost.replaceAll(",","");
        // Show if the amount is increased its size then show the message it is a enhancement - start Chandra
        if(totCost.length()-1 > 13){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("budget_common_exceptionCode.1011"));
            return false;
        }
        //end Chandra
        try{
          /* Added, to get the latest data while checking for the cost limit of the
           * budget. This validation is done at budgetBasewindowController. Hence,
           * set the latest value to the query engine./ If the data is changed
           * then only the data has to be updated. -  start 15/03/2004
           */
            CoeusVector vData  = queryEngine.executeQuery(proposalId+versionNumber, BudgetInfoBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            BudgetInfoBean qBudgetInfoBean=null;
            
            if(vData!= null && vData.size() > 0){
                qBudgetInfoBean = (BudgetInfoBean)vData.get(0);
            }
            
            
            budgetInfoBean.setTotalCostLimit(totalCostLimit);
            budgetInfoBean.setTotalCost(totalCost);
            //Code added for Case#3472 - Sync to Direct Cost Limit - starts
            //For adding total direct cost limit
            budgetInfoBean.setTotalDirectCostLimit((new Double(budgetSummaryForm.txtTotalDirectCostLimit.getValue())).doubleValue());
            budgetInfoBean.setTotalDirectCost((new Double(budgetSummaryForm.txtDirectCost.getValue())).doubleValue());
            //Code added for Case#3472 - Sync to Direct Cost Limit - ends
            StrictEquals stEq = new StrictEquals();
            if(! stEq.compare(qBudgetInfoBean, budgetInfoBean)){
                budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(proposalId+versionNumber, budgetInfoBean);
                BeanEvent beanevent = new BeanEvent();
                beanevent.setBean(budgetInfoBean);
                beanevent.setSource(this);
                fireBeanUpdated(beanevent);
            }
            
            // End 15/03/2004
            
            txtStartDate = dtFormat.parse(dtUtils.restoreDate(
            budgetSummaryForm.txtStartDate.getText(),DATE_SEPARATERS));
            
            txtEndDate = dtFormat.parse(dtUtils.restoreDate(
            budgetSummaryForm.txtEndDate.getText(),DATE_SEPARATERS));
            
            for ( int index = 0; index < vecBudgetPeriodBean.size()-1; index++){
                budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(index);
                nextBudgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(index + 1);
                
                //Check if start date is null/ Empty
                if(budgetPeriodBean.getStartDate()==null || nextBudgetPeriodBean.getStartDate()==null){
                    mesg = coeusMessageResources.parseMessageKey(
                    ENTER_START_DATE);
                    CoeusOptionPane.showErrorDialog(mesg);
                    budgetSummaryForm.tblBudgetSummary.editCellAt(index+1,START_DATE_COLUNM);
                    budgetSummaryEditor.txtDateComponent.requestFocus();
                    budgetSummaryEditor.txtDateComponent.setCaretPosition(0);
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(index + 1,index + 1);
                    budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    budgetSummaryForm.tblBudgetSummary.getCellRect(index+1, START_DATE_COLUNM, true));
                    return false;
                } //Check if End date is null / Empty
                else if(budgetPeriodBean.getEndDate() == null || nextBudgetPeriodBean.getEndDate()==null){
                    mesg = coeusMessageResources.parseMessageKey(
                    ENTER_END_DATE);
                    CoeusOptionPane.showErrorDialog(mesg);
                    budgetSummaryForm.tblBudgetSummary.editCellAt(index+1,END_DATE_COLUMN);
                    budgetSummaryEditor.txtDateComponent.requestFocus();
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(index+1,index+1);
                    budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    budgetSummaryForm.tblBudgetSummary.getCellRect(index+1, END_DATE_COLUMN, true));
                    return false;
                }else if(budgetPeriodBean.getStartDate().before(txtStartDate)){
                    mesg =coeusMessageResources.parseMessageKey(
                    FIRST_PERIOD_START_DATE_NOT_EARLIER_THAN_BUDGET_START_DATE);
                    CoeusOptionPane.showErrorDialog(mesg);
                    budgetSummaryForm.tblBudgetSummary.editCellAt(index,START_DATE_COLUNM);
                    budgetSummaryEditor.txtDateComponent.requestFocus();
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(index+1,index+1);
                    budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    budgetSummaryForm.tblBudgetSummary.getCellRect(index+1, START_DATE_COLUNM, true));
                    return false;
                }else if(budgetPeriodBean.getStartDate().after(budgetPeriodBean.getEndDate())
                || budgetPeriodBean.getEndDate().before(budgetPeriodBean.getStartDate())){
                    mesg =coeusMessageResources.parseMessageKey(
                    END_DATE_NOT_EARLIER_THAN_START_DATE);
                    CoeusOptionPane.showInfoDialog(mesg);
                    budgetSummaryForm.tblBudgetSummary.editCellAt(index,END_DATE_COLUMN);
                    budgetSummaryEditor.txtDateComponent.requestFocus();
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(index,index);
                    budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    budgetSummaryForm.tblBudgetSummary.getCellRect(index, END_DATE_COLUMN, true));
                    return false;
                }
                else if(nextBudgetPeriodBean.getStartDate().after(nextBudgetPeriodBean.getEndDate())
                || nextBudgetPeriodBean.getEndDate().before(nextBudgetPeriodBean.getStartDate())){
                    mesg =coeusMessageResources.parseMessageKey(
                    END_DATE_NOT_EARLIER_THAN_START_DATE);
                    CoeusOptionPane.showInfoDialog(mesg);
                    budgetSummaryForm.tblBudgetSummary.editCellAt(index+1,END_DATE_COLUMN);
                    budgetSummaryEditor.txtDateComponent.requestFocus();
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(index+1,index+1);
                    budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    budgetSummaryForm.tblBudgetSummary.getCellRect(index+1, END_DATE_COLUMN, true));
                    return false;
                }else if(!nextBudgetPeriodBean.getStartDate().after(budgetPeriodBean.getEndDate())) {
                    mesg =coeusMessageResources.parseMessageKey(
                    START_DATE_NOT_LATER_THAN_END_DATE);
                    CoeusOptionPane.showInfoDialog(mesg);
                    budgetSummaryForm.tblBudgetSummary.editCellAt(index+1,START_DATE_COLUNM);
                    budgetSummaryEditor.txtDateComponent.requestFocus();
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(index+1,index+1);
                    budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    budgetSummaryForm.tblBudgetSummary.getCellRect(index+1, START_DATE_COLUNM, true));
                    return false;
                    // If last period end date is greater than budget end date then show the message
                }else if(index == vecBudgetPeriodBean.size() - 2 &&
                nextBudgetPeriodBean.getEndDate().after(txtEndDate)){
                    mesg = coeusMessageResources.parseMessageKey(
                    LAST_PERIOD_END_DATE_NOT_GREATER_THAN_BUDGET_END_DATE);
                    CoeusOptionPane.showInfoDialog(mesg);
                    budgetSummaryForm.tblBudgetSummary.editCellAt(index+1,END_DATE_COLUMN);
                    budgetSummaryEditor.txtDateComponent.requestFocus();
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(index+1,index+1);
                    budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    budgetSummaryForm.tblBudgetSummary.getCellRect(index+1, END_DATE_COLUMN, true));
                    return false;
                }
            }// End for loop
            // Check if budget start date with in the proposal start date else show the error message
            budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(0);
            if(txtStartDate.before(proposalStartDate)){
                mesg = coeusMessageResources.parseMessageKey(
                BUDGET_START_DATE__BETWEEN_START_END_DATE_PROPOSAL);
                CoeusOptionPane.showInfoDialog(mesg);
                return false;
                // Check if budget start date with in the proposal end date else show the error message
            }else if(txtEndDate.after(proposalEndDate)){
                mesg = coeusMessageResources.parseMessageKey(
                BUDGET_END_DATE__BETWEEN_END_START_DATE_PROPOSAL);
                CoeusOptionPane.showErrorDialog(mesg);
                return false;
                // Check if the first period of the budget is earlier than budget start date
            }else if(budgetPeriodBean.getStartDate().before(txtStartDate)){
                mesg =coeusMessageResources.parseMessageKey(
                FIRST_PERIOD_START_DATE_NOT_EARLIER_THAN_BUDGET_START_DATE);
                CoeusOptionPane.showErrorDialog(mesg);
                budgetSummaryForm.tblBudgetSummary.editCellAt(0,START_DATE_COLUNM);
                budgetSummaryEditor.txtDateComponent.requestFocus();
                budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(0,0);
                return false;
                // Check if the first period end date is later than budget end date
            }else if(budgetPeriodBean.getEndDate().after(txtEndDate)){
                mesg = coeusMessageResources.parseMessageKey(
                LAST_PERIOD_END_DATE_NOT_GREATER_THAN_BUDGET_END_DATE);
                CoeusOptionPane.showInfoDialog(mesg);
                budgetSummaryForm.tblBudgetSummary.editCellAt(0,END_DATE_COLUMN);
                budgetSummaryEditor.txtDateComponent.requestFocus();
                budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(0,0);
                return false;
                // check if the first period start date greater than end date OR
                // check if the first period end date is less than start date
            }else if(budgetPeriodBean.getStartDate().after(budgetPeriodBean.getEndDate())
            || budgetPeriodBean.getEndDate().before(budgetPeriodBean.getStartDate())){
                mesg =coeusMessageResources.parseMessageKey(
                END_DATE_NOT_EARLIER_THAN_START_DATE);
                CoeusOptionPane.showInfoDialog(mesg);
                budgetSummaryForm.tblBudgetSummary.editCellAt(0,END_DATE_COLUMN);
                budgetSummaryEditor.txtDateComponent.requestFocus();
                budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(0,0);
                return false;
            }
            if(budgetSummaryForm.cmbBudgetStatus.getSelectedItem().equals(VAL_COMPLETE)) {
                CoeusVector vecData = queryEngine.executeQuery(proposalId+versionNumber,BudgetInfoBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                budgetInfoBean = (BudgetInfoBean)vecData.get(0);
                
                // Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 6
                getParameterForDistribution();
                //Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 6                
                
                if(budgetInfoBean.getUnderRecoveryAmount() != budgetInfoBean.getTotalIDCRateDistribution()){
                    
                    // Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 7
                    if(budgetInfoBean.getUnderRecoveryAmount() > 0.00){
                        if( isForceUnderRecovery()){
                            // Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 7
                            
                            mesg = coeusMessageResources.parseMessageKey(UNDER_RECOVERY_DISTRIBUTION);
                            CoeusOptionPane.showErrorDialog(mesg);
                            return false;
                        }
                    }//End of inner if
                }else  if(budgetInfoBean.getCostSharingAmount() != budgetInfoBean.getTotalCostSharingDistribution()){
                    
                    // Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 8
                    if(isForceCostSharing()){
                    // Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 8    
                    
                        mesg = coeusMessageResources.parseMessageKey(COST_SHARING_DISTRIBUTION);
                        CoeusOptionPane.showErrorDialog(mesg);
                        return false;
                    }//End of inner if
                }//End of else if
            }
            // For case #1626 Start 
            if(budgetSummaryForm.chkBudgetModular.isSelected() &&
            budgetSummaryForm.cmbBudgetStatus.getSelectedItem().equals(VAL_COMPLETE)){
                CoeusVector cvBudgetPeriod = queryEngine.executeQuery(queryKey,BudgetPeriodBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                CoeusVector cvBudgetModular  = queryEngine.executeQuery(queryKey,BudgetModularBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvBudgetModular!= null && cvBudgetModular.size() > 0){
                    if(cvBudgetModular.size()!= cvBudgetPeriod.size()){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INCOMPLETE_MODULAR_BUDGET));
                        budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                        //budgetSummaryForm.chkBudgetModular.setSelected(true);
                        return false;
                    }
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_MODULAR_BUDGET));
                    budgetSummaryForm.cmbBudgetStatus.setSelectedItem(VAL_INCOMPLETE);
                    //budgetSummaryForm.chkBudgetModular.setSelected(false);
                    return false;
                }
            }
            //For case #1626 end
          }catch(ParseException parseException){
            parseException.printStackTrace();
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        
        return true;
    }
    /** all types of actions is caught here
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
    }
    
    /**
     * Custom focus adapter which is used for text fields which consists of
     * date values. This is mainly used to format and restore the date value
     * during focus gained / focus lost of the text field.
     */
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;
        String strDate = EMPTY_STRING;
        String oldData = EMPTY_STRING;
        boolean temporary = false;
        boolean temporaryFlag;
        public void focusGained(FocusEvent focusEvent){
            //            if (fe.getSource().equals( budgetSummaryForm.txtStartDate ) ||
            //            fe.getSource().equals( budgetSummaryForm.txtEndDate )){
            //                dateField = (CoeusTextField)fe.getSource();
            //                if ( (dateField.getText() != null) &&  (!dateField.getText().trim().equals(EMPTY_STRING))) {
            //                    oldData = dateField.getText();
            //                    String focusDate = dtUtils.restoreDate(
            //                    dateField.getText(),DATE_SEPARATERS);
            //                    dateField.setText(focusDate);
            //                }
            //            }
            
            if(focusEvent.isTemporary()) return ;
            
            Object source = focusEvent.getSource();
            String strDate;
            
            if(source.equals(budgetSummaryForm.txtStartDate)) {
                strDate = dtUtils.restoreDate(budgetSummaryForm.txtStartDate.getText(), DATE_SEPARATERS);
                budgetSummaryForm.txtStartDate.setText(strDate);
            }else if(source.equals(budgetSummaryForm.txtEndDate)) {
                strDate = dtUtils.restoreDate(budgetSummaryForm.txtEndDate.getText(), DATE_SEPARATERS);
                budgetSummaryForm.txtEndDate.setText(strDate);
            }
        }
        
        public void focusLost(FocusEvent fe){
            String formatedStartDate = dtUtils.formatDate(budgetInfoBean.getStartDate().toString(),SIMPLE_DATE_FORMAT);
            String formatedEndDate = dtUtils.formatDate(budgetInfoBean.getEndDate().toString(),SIMPLE_DATE_FORMAT);
            
            String mesg=null;
            temporary = fe.isTemporary();
            if( !temporary ){
                temporary  = true;
                if ( fe.getSource()== budgetSummaryForm.txtStartDate ){
                    if(budgetSummaryForm.txtStartDate==null || budgetSummaryForm.txtStartDate.getText().trim().length()==0 ||
                    budgetSummaryForm.txtStartDate.equals(EMPTY_STRING)){
                        mesg = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(mesg);
                        budgetSummaryForm.txtStartDate.setText(formatedStartDate);
                        txtPreviousStartDate = budgetSummaryForm.txtStartDate.getText();
                        budgetSummaryForm.txtStartDate.requestFocus();
                    }else if ( budgetSummaryForm.txtStartDate.getText().trim().length() > 0 &&
                    !validateStartDate( budgetSummaryForm.txtStartDate.getText().trim()) ) {
                        //budgetSummaryForm.txtStartDate.requestFocus();
                        temporary  = true;
                    }
                    
                }else if (fe.getSource()== budgetSummaryForm.txtEndDate){
                    if(budgetSummaryForm.txtEndDate==null || budgetSummaryForm.txtEndDate.getText().trim().length()==0 ||
                    budgetSummaryForm.txtEndDate.equals(EMPTY_STRING)){
                        temporary  = true;
                        mesg = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
                        CoeusOptionPane.showErrorDialog(mesg);
                        budgetSummaryForm.txtEndDate.setText(formatedEndDate);
                        txtPreviousEndDate = budgetSummaryForm.txtEndDate.getText();
                        budgetSummaryForm.txtEndDate.requestFocus();
                    }else if ( budgetSummaryForm.txtEndDate.getText().trim().length() > 0 &&
                    !validateEndDate( budgetSummaryForm.txtEndDate.getText().trim()) ) {
                        temporary  = true;
                    }
                }
                
            }
            
            // fire the bean data
            //            BeanEvent beanEvent = new BeanEvent();
            //            beanEvent.setBean(budgetInfoBean);
            //            beanEvent.setSource(eventSource);
            //            fireBeanUpdated(beanEvent);
        }
    }
    /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateStartDate(String strDate) {
        
        boolean isValid=true;
        String mesg = null;
        String convertedDate = dtUtils.formatDate(strDate, DATE_SEPARATERS ,REQUIRED_DATEFORMAT);
        if (convertedDate==null ){
            mesg = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
        }else if ( budgetSummaryForm.txtEndDate.getText() !=null
        && budgetSummaryForm.txtEndDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = dtFormat.parse(dtUtils.restoreDate(convertedDate,DATE_SEPARATERS));
                endDate = dtFormat.parse(dtUtils.restoreDate(budgetSummaryForm.txtEndDate.getText(),DATE_SEPARATERS));
            }catch(java.text.ParseException pe){
                mesg = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
            }
            if(endDate!=null && endDate.compareTo(startDate)<0){
                //startdate is greater than end date - not ok
                mesg = coeusMessageResources.parseMessageKey(START_DATE_LATER_THAN_END_DATE);
            }else if(startDate.before(proposalStartDate)){
                mesg = coeusMessageResources.parseMessageKey(BUDGET_START_DATE__BETWEEN_START_END_DATE_PROPOSAL);
                
            }
        }
        if (mesg != null){
            isValid=false;
            CoeusOptionPane.showErrorDialog(mesg);
            budgetSummaryForm.txtStartDate.setText(txtPreviousStartDate);
        }else{
            String focusDate = dtUtils.formatDate(strDate,DATE_SEPARATERS,REQUIRED_DATEFORMAT);
            budgetSummaryForm.txtStartDate.setText(focusDate);
            txtPreviousStartDate = budgetSummaryForm.txtStartDate.getText();
        }
        return isValid;
    }
    
    
    /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateEndDate(String strDate) {
        boolean isValid=true;
        String mesg = null;
        String convertedDate =
        dtUtils.formatDate(strDate, DATE_SEPARATERS,REQUIRED_DATEFORMAT);
        if (convertedDate==null){
            mesg = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
            budgetSummaryForm.txtEndDate.grabFocus();
        }else if ( budgetSummaryForm.txtStartDate.getText() !=null
        && budgetSummaryForm.txtStartDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;
            try {
                endDate = dtFormat.parse(dtUtils.restoreDate(convertedDate,DATE_SEPARATERS));
                // Bug fix #1739  -start
                String convertedStartDate = dtUtils.restoreDate(budgetSummaryForm.txtStartDate.getText().trim(), DATE_SEPARATERS);
                // Bug fix #1739  -End
                startDate = dtFormat.parse(dtUtils.restoreDate(convertedStartDate, DATE_SEPARATERS));
            }catch(java.text.ParseException pe){
                mesg = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
                //budgetSummaryForm.txtEndDate.requestFocus();
            }
            if(startDate !=null && startDate.compareTo(endDate)>0){
                // end date is less than start date - not ok
                mesg = coeusMessageResources.parseMessageKey(END_DATE_EARLIER_THAN_START_DATE);
                //budgetSummaryForm.txtEndDate.requestFocus();
            }
            else if(endDate!= null && endDate.after(proposalEndDate)){
                mesg = coeusMessageResources.parseMessageKey(BUDGET_END_DATE__BETWEEN_END_START_DATE_PROPOSAL);
                budgetSummaryForm.txtEndDate.requestFocus();
            }
        }
        if (mesg != null){
            isValid=false;
            CoeusOptionPane.showErrorDialog(mesg);
            budgetSummaryForm.txtEndDate.setText(txtPreviousEndDate);
        }else{
            String focusDate = dtUtils.formatDate(strDate,DATE_SEPARATERS,REQUIRED_DATEFORMAT);
            budgetSummaryForm.txtEndDate.setText(focusDate);
            txtPreviousEndDate = budgetSummaryForm.txtEndDate.getText();
        }
        return isValid;
    }
    
    // End of Validate End Date Method............................
    
    
    // To get the reference of number of periods for a particular budget.
    /** This method will return the number of periods in the budget. */
    public int getPeriods() {
        return budgetSummaryForm.tblBudgetSummary.getRowCount();
    }
    
    /** Inner class to specify Editor for the budgetSummaryTable
     */
    
    public class BudgetSummaryEditor extends  AbstractCellEditor
    implements TableCellEditor {
        
        private CoeusTextField txtDateComponent;
        private DollarCurrencyTextField txtDollarCurrencyTextField;
        private int column;
        BudgetSummaryEditor(){
            txtDateComponent = new CoeusTextField();
            txtDollarCurrencyTextField = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            
        }
        public Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            
            switch(column){
                case START_DATE_COLUNM:
                case END_DATE_COLUMN:
                    if(isSelected){
                        txtDateComponent.selectAll();
                        //                        txtDateComponent.setForeground(Color.white);
                        //                    }else{
                        //                        txtDateComponent.setBackground(budgetSummaryForm.tblBudgetSummary.getBackground());
                        //                        txtDateComponent.setForeground(Color.black);
                        //                    }
                    }
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        txtDateComponent.setText(EMPTY_STRING);
                        return txtDateComponent;
                    }
                    String strDate = dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT);
                    if(strDate== null){
                        txtDateComponent.setText(value.toString());
                    }else{
                        txtDateComponent.setText(strDate);
                        return txtDateComponent;
                    }
                    
                case TOTAL_COST_COLUMN:
                case DIRECT_COST_COLUMN:
                case INDIRECT_COST_COLUMN:
                case COST_SHARING_COLUMN:
                case UNDER_RECOVERY_COLUMN:
                    if(isSelected){
                        txtDollarCurrencyTextField.selectAll();
                        //                        txtDollarCurrencyTextField.setForeground(Color.white);
                        //                    }else{
                        //                        txtDollarCurrencyTextField.setBackground(budgetSummaryForm.tblBudgetSummary.getBackground());
                        //                        txtDollarCurrencyTextField.setForeground(Color.black);
                        //                    }
                    }
                    
                    txtDollarCurrencyTextField.setValue(new Double(value.toString()).doubleValue());
                    return txtDollarCurrencyTextField;
            }
            return txtDollarCurrencyTextField;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case TOTAL_COST_COLUMN:
                case DIRECT_COST_COLUMN:
                case INDIRECT_COST_COLUMN:
                case COST_SHARING_COLUMN:
                case UNDER_RECOVERY_COLUMN:
                    return txtDollarCurrencyTextField.getValue();
                case START_DATE_COLUNM:
                case END_DATE_COLUMN:
                    return txtDateComponent.getText();
            }
            return ((JTextField)txtDateComponent).getText();
        }
        
        public int getClickCountToStart(){
            return 1;
        }
        
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
        
    }//End of Cell Editor Class
    
    
    /** Inner class specifies the renderers for the each column
     */
    private class ColumnPeriodRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        
        private DollarCurrencyTextField dollarCurrencyTextField;
        private CoeusTextField txtDateComponent;
        private JTextField txtPeriod;
        private JTextField txtPeriodMonths;
        private int selRow;
        private int selCol;
        public ColumnPeriodRenderer() {
            dollarCurrencyTextField = new DollarCurrencyTextField(12,JTextField.RIGHT,true);
            dollarCurrencyTextField.setHorizontalAlignment(DollarCurrencyTextField.RIGHT);
            dollarCurrencyTextField.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            budgetSummaryForm.tblBudgetSummary.editCellAt(selRow,selCol);
            setFont(CoeusFontFactory.getNormalFont());
            
            txtDateComponent = new CoeusTextField();
            txtDateComponent.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            budgetSummaryForm.tblBudgetSummary.editCellAt(selRow,selCol);
            setFont(CoeusFontFactory.getNormalFont());
            
            txtPeriod = new JTextField();
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            txtPeriod.setBorder(new BevelBorder(BevelBorder.LOWERED));
            
            //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
            txtPeriodMonths = new JTextField();
            txtPeriodMonths.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            //3197- end
            
        }
        
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                txtDateComponent.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtPeriod.setBorder(new EmptyBorder(0,0,0,0));
                txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start   
                txtPeriodMonths.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                // 3197 - end
                dollarCurrencyTextField.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            }
            
            selRow = row;
            selCol = column;
            switch (column){
                case PERIOD_COLUMN:
                    if(isSelected){
                        txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtPeriod.setForeground(Color.black);
                    }
                    else {
                        txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtPeriod.setForeground(Color.black);
                    }
                    txtPeriod.setText(value.toString());
                    txtPeriod.setHorizontalAlignment(JTextField.RIGHT);
                    return txtPeriod;
                //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start       
                case TOTAL_PERIOD_MONTHS:
                    txtPeriodMonths.setText(value.toString());
                    txtPeriodMonths.setHorizontalAlignment(JTextField.RIGHT);
                    return txtPeriodMonths;
                //3197 - end    
                case TOTAL_COST_COLUMN:
                case DIRECT_COST_COLUMN:
                case INDIRECT_COST_COLUMN:
                case COST_SHARING_COLUMN:
                case UNDER_RECOVERY_COLUMN:
                    //dollarCurrencyTextField.setText(value.toString());
                    dollarCurrencyTextField.setValue(new Double(value.toString()).doubleValue());
                    //                        dollarCurrencyTextField.setForeground(Color.white);
                    //                    }
                    //                    else {
                    //                        dollarCurrencyTextField.setBackground(budgetSummaryForm.tblBudgetSummary.getBackground());
                    //                        //dollarCurrencyTextField.setForeground(budgetSummaryForm.tblBudgetSummary.getForeground());
                    //                        dollarCurrencyTextField.setForeground(Color.black);
                    //                    }
                    return dollarCurrencyTextField;
                    
                case START_DATE_COLUNM:
                case END_DATE_COLUMN:
                    //                    if(isSelected){
                    //                        txtDateComponent.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                    //                        txtDateComponent.setForeground(Color.white);
                    //                    }
                    //                    else {
                    //                        txtDateComponent.setBackground(budgetSummaryForm.tblBudgetSummary.getBackground());
                    //                        txtDateComponent.setForeground(Color.black);
                    //                    }
                    
                    if(value == null || value.toString().equals(EMPTY_STRING)){
                        txtDateComponent.setText(EMPTY_STRING);
                        return txtDateComponent;
                    }else{
                        value = dtUtils.formatDate(value.toString(),REQUIRED_DATEFORMAT);
                        txtDateComponent.setText(value.toString());
                        txtDateComponent.setHorizontalAlignment(JTextField.LEFT);
                        return txtDateComponent;
                    }
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
    }// End Class ColumnPeriodRenderer
    
    /** Method to add a new Budget Period. */
    public void addBudgetPeriod(){
        
        budgetSummaryEditor.stopCellEditing();
        BudgetPeriodBean newBean = new BudgetPeriodBean();
        newBean.setProposalNumber(budgetBean.getProposalNumber());
        newBean.setVersionNumber(budgetBean.getVersionNumber());
        newBean.setBudgetPeriod(vecBudgetPeriodBean.size() + 1);
        newBean.setAw_BudgetPeriod(vecBudgetPeriodBean.size() + 1);
        newBean.setTotalCost(0.00);
        newBean.setTotalDirectCost(0.00);
        newBean.setTotalIndirectCost(0.00);
        newBean.setCostSharingAmount(0.00);
        newBean.setUnderRecoveryAmount(0.00);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        queryEngine.insert(proposalId + versionNumber, newBean);
        modified= true;
        vecBudgetPeriodBean.add(newBean);
        budgetPeriodModel.fireTableRowsInserted(budgetPeriodModel.getRowCount() + 1, budgetPeriodModel.getRowCount() + 1);
        /** Fire the bean event to listen the inserted row
         */
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(budgetPeriodBean);
        beanEvent.setSource(this);
        fireBeanAdded(beanEvent);
        
        int lastRow = budgetSummaryForm.tblBudgetSummary.getRowCount()-1;
        if(lastRow >= 0){
            budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval( lastRow,lastRow);
            
            budgetSummaryForm.tblBudgetSummary.setColumnSelectionInterval(START_DATE_COLUNM,START_DATE_COLUNM);
            budgetSummaryForm.tblBudgetSummary.editCellAt(lastRow, START_DATE_COLUNM);
            
            budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
            budgetSummaryForm.tblBudgetSummary.getCellRect(lastRow ,START_DATE_COLUNM, true));
            
            budgetSummaryForm.tblBudgetSummary.getEditorComponent().requestFocusInWindow();
        }
        //        budgetSummaryForm.tblBudgetSummary.editCellAt(lastRow, START_DATE_COLUNM);
        //        budgetSummaryForm.tblBudgetSummary.getEditorComponent().requestFocusInWindow();
    }
    
    /** Method to delete the budget Period and update the osp$Budget */
    public void deleteBudgetPeriod(){
        String mesg = null;
        int rowCount = budgetSummaryForm.tblBudgetSummary.getRowCount();
        CoeusVector cvfilteredLIDetails;
        int selectedRow = budgetSummaryForm.tblBudgetSummary.getSelectedRow();
        if(selectedRow < 0 ){
            mesg = coeusMessageResources.parseMessageKey(SELECT_BUDGET_PERIOD);
            CoeusOptionPane.showInfoDialog(mesg);
            return;
        }
        //Check if this period  has Line item Details.If it has then Can't Delete.
        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(selectedRow);
        int budgetPeriod = budgetPeriodBean.getBudgetPeriod();
        try{
            if(rowCount==1){
                mesg = coeusMessageResources.parseMessageKey(CAN_NOT_DELETE_ALL_PERIODS);
                CoeusOptionPane.showInfoDialog(mesg);
                return;
            }
            //check whether the current period has any line items
            Equals periodEquals = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
            cvfilteredLIDetails = queryEngine.getActiveData(budgetBean.getProposalNumber() + budgetBean.getVersionNumber(), BudgetDetailBean.class, periodEquals);
            if (cvfilteredLIDetails.size() > 0) {
                return ;
            }
            //check whether any of the later periods has line items
            GreaterThan periodGreaterThan = new GreaterThan(BUDGET_PERIOD, new Integer(budgetPeriod));
            cvfilteredLIDetails = queryEngine.getActiveData(budgetBean.getProposalNumber() + budgetBean.getVersionNumber(), BudgetDetailBean.class, periodGreaterThan);
            
            if (cvfilteredLIDetails.size() > 0) {
                CoeusOptionPane.showInfoDialog("Period " +budgetPeriodBean.getBudgetPeriod()+ " or one of the later periods have detail budget items. This period can not be deleted. ");
                return;
            }
             //Case #1626 Start 1
            deleteBudgetModularPeriod(periodEquals);
            //Case #1626 End 1
            budgetSummaryEditor.stopCellEditing();
            BudgetPeriodBean deletedPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(selectedRow);
            if(vecBudgetPeriodBean!=null && vecBudgetPeriodBean.size() > 0){
                deletedPeriodBean.setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete(proposalId+versionNumber, deletedPeriodBean);
                vecBudgetPeriodBean.remove(selectedRow);
                budgetPeriodModel.fireTableRowsDeleted(selectedRow, selectedRow);
                modified = true;
                
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(budgetPeriodBean);
                beanEvent.setSource(eventSource);
                fireBeanDeleted(beanEvent);
                
                //Case 1625 Start
                
                CoeusVector cvProjectIncome = queryEngine.getActiveData(budgetBean.getProposalNumber() + budgetBean.getVersionNumber(), ProjectIncomeBean.class, periodEquals);
                if(cvProjectIncome != null && cvProjectIncome.size()>0){
                    for(int index = 0 ; index < cvProjectIncome.size(); index ++){
                        ProjectIncomeBean deleteIncomeBean =
                        (ProjectIncomeBean)cvProjectIncome.get(index);
                        deleteIncomeBean.setAcType(TypeConstants.DELETE_RECORD);
                        queryEngine.delete(proposalId+versionNumber, deleteIncomeBean);
                    }
                }
                //Case 1625 End
            }
            // This logic is used to take n-1 period numbers in the budget period
            for(int index = selectedRow; index < vecBudgetPeriodBean.size(); index++) {
                budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(index);
                budgetPeriodBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod() - 1);
                modified=true;
                if (budgetPeriodBean.getAcType() == null) {
                    budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(proposalId+versionNumber,budgetPeriodBean);
                } else {
                    queryEngine.insert(proposalId+versionNumber,budgetPeriodBean);
                }
                
            }
            budgetPeriodModel.fireTableRowsUpdated(selectedRow, vecBudgetPeriodBean.size());
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(budgetPeriodBean);
            beanEvent.setSource(eventSource);
            fireBeanUpdated(beanEvent);
            
            modified = true;
            if(selectedRow >0){
                budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(
                selectedRow-1,selectedRow-1);
                budgetSummaryForm.tblBudgetSummary.setColumnSelectionInterval(1,1);
                if(budgetSummaryForm.tblBudgetSummary.isCellEditable(selectedRow-1, START_DATE_COLUNM)){
                    budgetSummaryForm.tblBudgetSummary.editCellAt(selectedRow-1, START_DATE_COLUNM);
                    budgetSummaryForm.tblBudgetSummary.getEditorComponent().requestFocusInWindow();
                }else{
                    budgetSummaryForm.chkFinal.requestFocusInWindow();
                }
                budgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                budgetSummaryForm.tblBudgetSummary.getCellRect(selectedRow-1 ,START_DATE_COLUNM, true));
                
                
            }else{
                if(budgetSummaryForm.tblBudgetSummary.getRowCount()>0){
                    budgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(0,0);
                }
            }
            calculateChangedCost();
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }
        
    }
     //Case #1626 Start 2
    /** This methid is used to delete budget period from Budget Modular
     *
     */
    public void deleteBudgetModularPeriod(Equals periodEquals) throws CoeusException{
            CoeusVector cvBudgetModular = queryEngine.getActiveData(budgetBean.getProposalNumber() + budgetBean.getVersionNumber(), BudgetModularBean.class, periodEquals);
                if(cvBudgetModular != null && cvBudgetModular.size()>0){
                    for(int index = 0 ; index < cvBudgetModular.size(); index ++){
                        BudgetModularBean deleteModularBean = 
                            (BudgetModularBean)cvBudgetModular.get(index);
                        deleteModularBean.setAcType(TypeConstants.DELETE_RECORD);
                        queryEngine.delete(proposalId+versionNumber, deleteModularBean);
                    }
                }
            //For Budget Modular Enhancement case #2087 start 1
            cvBudgetModular = queryEngine.getActiveData(budgetBean.getProposalNumber() + budgetBean.getVersionNumber(), BudgetModularIDCBean.class, periodEquals);
            if(cvBudgetModular != null && cvBudgetModular.size()>0){
                for(int index = 0 ; index < cvBudgetModular.size(); index ++){
                    BudgetModularIDCBean deleteModularBean =
                        (BudgetModularIDCBean)cvBudgetModular.get(index);
                    deleteModularBean.setAcType(TypeConstants.DELETE_RECORD);
                    queryEngine.delete(proposalId+versionNumber, deleteModularBean);
                }
            }
            cvBudgetModular = null;
            //For Budget Modular Enhancement case #2087 end 1
       }
      //Case #1626 End 2
    
    /** This method is used to calculate the changed cost while calculating and when the
     * bean value is changed.
     */
    public void calculateChangedCost(){
        double totalCost, directCost,indirectCost,costSharing,underRecovery;
        totalCost = recalculateSummaryCost(TOTAL_COST);
        directCost = recalculateSummaryCost(DIRECT_COST);
        indirectCost = recalculateSummaryCost(INDIRECT_COST);
        costSharing = recalculateSummaryCost(COST_SHARING);
        underRecovery = recalculateSummaryCost(UNDER_RECOVERY);
        //totCost = directCost + indirectCost;
        totalCost = recalculateSummaryCost(TOTAL_COST);
        
        budgetInfoBean.setTotalCost(totalCost);
        budgetInfoBean.setTotalDirectCost(directCost);
        budgetInfoBean.setTotalIndirectCost(indirectCost);
        budgetInfoBean.setCostSharingAmount(costSharing);
        
        budgetInfoBean.setUnderRecoveryAmount(underRecovery);
        
        
        budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
        try{
            queryEngine.update(proposalId+versionNumber, budgetInfoBean);
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }
        
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(budgetInfoBean);
        beanEvent.setSource(eventSource);
        fireBeanUpdated(beanEvent);
        
        budgetSummaryForm.txtDirectCost.setValue(directCost);
        budgetSummaryForm.txtIndirectCost.setValue(indirectCost);
        budgetSummaryForm.txtTotalCost.setValue(totalCost);
        budgetSummaryForm.txtCostSharing.setValue(costSharing);
        budgetSummaryForm.txtUnderRecovery.setValue(underRecovery);
        
    }
    
    /** This method will give the reclaculated cost for the each field */
    public double recalculateSummaryCost(int costType){
        
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        budgetInfoBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
        budgetInfoBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
        double calAmt = 0;
        switch(costType){
            case TOTAL_COST:
                calAmt = vecBudgetPeriodBean.sum(PERIOD_TOTAL_COST);
                break;
            case DIRECT_COST:
                calAmt = vecBudgetPeriodBean.sum(PERIOD_DIRECT_COST);
                break;
            case INDIRECT_COST:
                calAmt = vecBudgetPeriodBean.sum(PERIOD_INDIRECT_COST);
                break;
            case COST_SHARING:
                calAmt = vecBudgetPeriodBean.sum(PERIOD_COST_SHARING);
                break;
            case UNDER_RECOVERY:
                calAmt = vecBudgetPeriodBean.sum(PERIOD_UNDER_RECOVERY);
                break;
        }
        return calAmt;
    }
    
    /** This is a bean listener , whenever the bean value is changed , it will listen to
     * that bean and the values will be updated
     */
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getSource().getClass().equals(BudgetBaseWindowController.class)
        //Bug Fix : 1092 - START
        //Since Budget Personnel Form fires Budget Detail Beans. this change woud've
        //changed the Budget Period. so update Budget Summary also.
        || (beanEvent.getSource().getClass().equals(PersonnelBudgetDetailController.class) &&
        beanEvent.getBean().getClass().equals(BudgetDetailBean.class))
        //Bug Fix : 1092 - END
        ) {
            try{
                //Added for Case#2924-On/Off Campus flag - Personnel budget
                errMsgShown = false;
                vecBudgetPeriodBean = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                budgetPeriodModel.setData(vecBudgetPeriodBean);
                budgetPeriodModel.fireTableDataChanged();
                // Bug fix 1652 - Update the Budget InfoBean if the preceesding beans are changed
                // start 18-May-2005
                vecBudgetInfoBean = queryEngine.executeQuery(proposalId + versionNumber, budgetInfoBean);
                if(vecBudgetInfoBean != null && vecBudgetInfoBean.size() > 0) {
                    budgetInfoBean = (BudgetInfoBean)vecBudgetInfoBean.get(0);
                    setFormData(getBeanData(budgetInfoBean, vecBudgetPeriodBean));
                    setSummaryDetails();
                    // End 18-May-2005
                }
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
            //Added for Case#2924-On/Off Campus flag - Personnel budget
            errMsgShown = true;            
            return ;
        }else if(beanEvent.getSource().getClass().equals(UnderRecoveryDistributionController.class)) {
            budgetInfoBean = (BudgetInfoBean)beanEvent.getBean();
        }else if(beanEvent.getSource().getClass().equals(CostSharingDistributionController.class)) {
            budgetInfoBean = (BudgetInfoBean)beanEvent.getBean();
        }
        refreshRequired = true;
    }
    
    /** It is a overridden method of the controller. This method will refreshes the
     * values
     */
    public void refresh(){
        if(! refreshRequired) return ;
        try{
            vecBudgetPeriodBean = queryEngine.executeQuery(proposalId + versionNumber, BudgetPeriodBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            calculateChangedCost();
            vecBudgetInfoBean = queryEngine.executeQuery(proposalId + versionNumber, budgetInfoBean);
            cvOhRate = queryEngine.executeQuery(proposalId + versionNumber,rateClassBean);
            if(vecBudgetInfoBean != null && vecBudgetInfoBean.size() > 0) {
                budgetInfoBean = (BudgetInfoBean)vecBudgetInfoBean.get(0);
                setFormData(getBeanData(budgetInfoBean, vecBudgetPeriodBean));
            }
            BudgetSummaryForm budgetSummaryForm = new BudgetSummaryForm();
            budgetSummaryForm.tblBudgetSummary.setModel(budgetPeriodModel);
            columnPeriodRenderer = new ColumnPeriodRenderer();
            budgetSummaryEditor = new BudgetSummaryEditor();
            budgetPeriodModel.setData(vecBudgetPeriodBean);
            budgetPeriodModel.fireTableDataChanged();
            setTableEditors();
            refreshRequired = false;
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }
    }
    
    /** This is a overridden method. It will specify whether the data are refreshed or
     * not
     */
    public boolean isRefreshRequired() {
        return refreshRequired;
    }
    /** setting the refreshRequired flag
     * @param refreshRequired
     */
    public void setRefreshRequired(boolean refreshRequired) {
        this.refreshRequired = refreshRequired;
    }
    
    protected void finalize() throws Throwable {
        removeBeanUpdatedListener(this, BudgetPeriodBean.class);
        removeBeanUpdatedListener(this, BudgetInfoBean.class);
        removeBeanUpdatedListener(this, BudgetDetailBean.class);
        //removeBeanDeletedListener(this, BudgetPeriodBean.class);
        super.finalize();
    }
    
    /* Get the Rate Class for Rate Type for UnderRecovery - enhancement-12/19/2003
     *@returns rateType
     *@param rateclass code
     */
    private int getRateClassForRateType(int rateClass) throws CoeusClientException{
        RequesterBean request = new RequesterBean();
        request.setFunctionType(RATE_TYPE_FOR_RATE_CLASS);
        request.setDataObject(new Integer(rateClass));
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response==null){
            throw new CoeusClientException(COULD_NOT_CONTACT_SERVER);
        }
        if(response.isSuccessfulResponse()){
            Integer rateType = (Integer)response.getDataObject();
            return rateType.intValue();
        }else {
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    
    
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = budgetSummaryForm.tblBudgetSummary.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = budgetSummaryForm.tblBudgetSummary.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    
                    if(row==0 && column==1){
                        budgetSummaryForm.chkFinal.requestFocusInWindow();
                    }
                    
                    if (row == rowCount) {
                        row = 0;
                    }
                    
                    
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        budgetSummaryForm.tblBudgetSummary.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = budgetSummaryForm.tblBudgetSummary.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    
                    column -= 1;
                    
                    //                    if(row==0 && column==-1){
                    //                        budgetSummaryForm.chkFinal.requestFocusInWindow();
                    //                    }
                    if (column <= 0) {
                        column = 7;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        budgetSummaryForm.tblBudgetSummary.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 5
     */
    /**
     * Getter for property forceUnderRecovery.
     * @return Value of property forceUnderRecovery.
     */
    public boolean isForceUnderRecovery() {
        return forceUnderRecovery;
    }
    
    /**
     * Setter for property forceUnderRecovery.
     * @param forceUnderRecovery New value of property forceUnderRecovery.
     */
    public void setForceUnderRecovery(boolean forceUnderRecovery) {
        this.forceUnderRecovery = forceUnderRecovery;
    }
    
    /**
     * Getter for property forceCostSharing.
     * @return Value of property forceCostSharing.
     */
    public boolean isForceCostSharing() {
        return forceCostSharing;
    }
    
    /**
     * Setter for property forceCostSharing.
     * @param forceCostSharing New value of property forceCostSharing.
     */
    public void setForceCostSharing(boolean forceCostSharing) {
        this.forceCostSharing = forceCostSharing;
    }
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 5
     */
    /** static Final Variable Declarations
     */
    
    //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
    /**
     * This method gets the cost elements data from table OSP$COST_ELEMENT
     * @return vecCostElements Vector
     * @throws Exception CoeusException
     */
    private Vector getCostElements()throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('Y');
        Vector vecCostElements = null;
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + BUDGET_MAINTENANCE_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean!= null){
            if(!responderBean.isSuccessfulResponse()){
                throw new CoeusException(responderBean.getMessage(), 1);
            }else{
                Hashtable htData = (Hashtable)responderBean.getDataObject();                        
                vecCostElements = (Vector)htData.get(CostElementsBean.class);
            }
        }
        return vecCostElements;
    }  
    
    /**
     * This method gets the default onOff flag for a given cost element
     * @param vecCostElement Vector
     * @param requiredCostElement String
     * @return defaultOnOffFlag boolean
     */
    private boolean getDefaultOnOffFlag(Vector vecCostElement, String requiredCostElement){
        boolean defaultOnOffFlag = false;
        CostElementsBean costElementsBean = null;
        if(vecCostElement != null && vecCostElement.size() > 0){
            for(int index = 0; index < vecCostElement.size(); index++){
                costElementsBean = (CostElementsBean)vecCostElement.get(index);
                String costElement = costElementsBean.getCostElement();
                if(requiredCostElement.equals(costElement)){
                    defaultOnOffFlag = costElementsBean.isOnOffCampusFlag();
                    break;
                }
            }
        }
        return defaultOnOffFlag;
    }
    //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
    
    private static final String ENTER_START_DATE = "budget_enterStartDate_exceptionCode.1101";
    
    private static final String ENTER_END_DATE = "budget_enterEndDate_exceptionCode.1102";
    
    private static final String END_DATE_NOT_EARLIER_THAN_START_DATE =
    "budget_earlierStartDate_exceptionCode.1103";
    
    private static final String START_DATE_NOT_LATER_THAN_END_DATE=
    "budget_earlierEndDate_exceptionCode.1104";
    
    private static final String LAST_PERIOD_END_DATE_NOT_GREATER_THAN_BUDGET_END_DATE =
    "budgetSummary_exceptionCode.1105";
    
    private static final String FIRST_PERIOD_START_DATE_NOT_EARLIER_THAN_BUDGET_START_DATE=
    "budgetSummary_exceptionCode.1106";
    
    private static final String  COST_LIMIT_OF_THE_BUDGET =
    "budgetSummary_exceptionCode.1107";
    
    private static final String BUDGET_START_DATE__BETWEEN_START_END_DATE_PROPOSAL =
    "budgetSummary_exceptionCode.1108";
    
    private static final String BUDGET_END_DATE__BETWEEN_END_START_DATE_PROPOSAL =
    "budgetSummary_exceptionCode.1109";
    
    private static final String INVALID_START_DATE =
    "budget_common_exceptionCode.1001";
    
    private static final String INVALID_END_DATE =
    "budget_common_exceptionCode.1002";
    
    private static final String MODIFY_FINAL_VERSION =
    "budgetSummary_exceptionCode.1110";
    
    private static final String DESIGNATE_FINAL_VERSION =
    "budgetSummary_exceptionCode.1111";
    
    private static final String UNDER_RECOVERY_DISTRIBUTION =
    "budgetSummary_exceptionCode.1112";
    
    private static final String COST_SHARING_DISTRIBUTION =
    "budgetSummary_exceptionCode.1113";
    
    private static final String CHANGING_OH_RATE =
    "budgetSummary_exceptionCode.1114";
    
    private static final String CHANGING_UR_RATE =
    "budgetSummary_messageCode.1119";
   
  // Case id# 2924 - start
    private static final String CHANGING_ONOFF =
    "budgetSummary_messageCode.1120";
  // Case id# 2924 - end
      
    private static final String END_DATE_EARLIER_THAN_START_DATE =
    "budgetSummary_exceptionCode.1115";
    
    private static final String START_DATE_LATER_THAN_END_DATE=
    "budgetSummary_exceptionCode.1116";
    
    private static final String NO_MODULAR_BUDGET = "budget_summary_modular_budget_exceptionCode.1120";
    private static final String INCOMPLETE_MODULAR_BUDGET = "budget_summary_modular_budget_exceptionCode.1121";
    
    private static final String CAN_NOT_DELETE_ALL_PERIODS = "budgetSummary_exceptionCode.1117";
    
    private static final String SELECT_BUDGET_PERIOD = "budgetSummary_messageCode.1118";
    
    private static final String INCOMPLETE = "I";
    private static final String COMPLETE = "C";
    private static final String NONE = "N";
    
    
    private final String BUDGET_OH_RATES = "/BudgetMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_OH_RATES;
    /** Getting and updating the OH Rates. To update  the changed values used a server
     * call, with the function type
     */
    
    private final char GET_VALID_OH_RATE_TYPE = 'H';
    /** To get the rate type for rate class
     */
    private final char RATE_TYPE_FOR_RATE_CLASS = 'M';
    /** This static variable is used for calculation for all the periods */
    private static final int CALCULATE_ALL_PERIODS = -1;
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    /** Specifies the column number
     */
    private static final int PERIOD_COLUMN = 0;
    private static final int START_DATE_COLUNM = 1;
    private static final int END_DATE_COLUMN = 2;
//Added for Case 3197 - Allow for the generation of project period greater than 12 months -start    
//    private static final int TOTAL_COST_COLUMN = 3;
//    private static final int DIRECT_COST_COLUMN = 4;
//    private static final int INDIRECT_COST_COLUMN = 5;
//    private static final int COST_SHARING_COLUMN = 6;
//    private static final int UNDER_RECOVERY_COLUMN = 7;
    
    private static final int TOTAL_PERIOD_MONTHS = 3;
    private static final int TOTAL_COST_COLUMN = 4;
    private static final int DIRECT_COST_COLUMN = 5;
    private static final int INDIRECT_COST_COLUMN = 6;
    private static final int COST_SHARING_COLUMN = 7;
    private static final int UNDER_RECOVERY_COLUMN = 8;
// 3197 - end    
    
    
    /** used for calculate the periods and update the values in the periods
     */
    private static final int TOTAL_COST=1;
    private static final int DIRECT_COST=2;
    private static final int INDIRECT_COST=3;
    private static final int COST_SHARING=4;
    private static final int UNDER_RECOVERY=5;
    
    private static final String EMPTY_STRING = "";
    
    private static final String VAL_INCOMPLETE = "Incomplete";
    private static final String VAL_COMPLETE = "Complete";
    private static final String VAL_NONE = "(None)";
    
// Case id 2924 - start
    private static final String FLAG_ON = "On";
    private static final String FLAG_OFF = "Off";
// Case id 2924 - end    
           
    
    private static final String PERIOD_TOTAL_COST = "totalCost";
    private static final String PERIOD_DIRECT_COST = "totalDirectCost";
    private static final String PERIOD_INDIRECT_COST = "totalIndirectCost";
    private static final String PERIOD_COST_SHARING = "costSharingAmount";
    private static final String PERIOD_UNDER_RECOVERY = "underRecoveryAmount";
    
    private static final String LINE_ITEM_NUMBER = "lineItemNumber";
    private static final String BUDGET_PERIOD = "budgetPeriod";
    private static final String RATE_CLASS_CODE = "rateClassCode";
    private static final String  RATE_CLASS_TYPE = "rateClassType";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final String ERRKEY_NONE_NOT_ALLOWED = "budgetSummary_exceptionCode.1121";
    //Added for Case#3404-Outstanding proposal hierarchy changes - Start
    
    /** Check whether When budget status is set to Complete in a parent proposal,
     *  check if all child budgets have status are ‘Complete’. 
     */
    private boolean isBudgetComplete(){
        final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
        final char IS_COMPLETE = 'Q';
        final String connectToServlet = CoeusGuiConstants.CONNECTION_URL+ HIERARCHY_SERVLET;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        boolean success = false;
        try{
            requester.setFunctionType(IS_COMPLETE);
            requester.setDataObject(proposalId);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectToServlet, requester);
            comm.send();
            responder = comm.getResponse();
            if(responder.hasResponse()){
                success = ((Boolean)responder.getDataObject()).booleanValue();
            }else{
                throw new CoeusException(responder.getMessage());
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return success;
    }
    
    //Added for Case#3404-Outstanding proposal hierarchy changes - End
    
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    /**
     * This method gets the cost elements data from table OSP$COST_ELEMENT
     * @return vecCostElements Vector
     * @throws Exception CoeusException
     */
    private double getNumberOfMonths(Date startDate, Date endDate){
        RequesterBean requesterBean = new RequesterBean();
        double noOfMonths = 0;
         try{
        
        Vector vecCostElements = null;
        Vector vecDates = new Vector();
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + BUDGET_MAINTENANCE_SERVLET);
        requesterBean.setFunctionType('s');
        vecDates.add(startDate);
        vecDates.add(endDate);
        requesterBean.setDataObjects(vecDates);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean!= null){
            if(!responderBean.hasResponse()){
                throw new CoeusException(responderBean.getMessage(), 1);
            }else{                 
                noOfMonths = ((Double)responderBean.getDataObject()).doubleValue();
              
            }
        }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return noOfMonths;
    }  
    
    //3197 - End
    //Added for COEUSDEV-241 : Funky screens in PD Budget tab for Total screen when save is pressed multiple times - Start
    //This function will refresh all beans required for this controller.
    public void refreshBudgetInfo(BudgetBean budgetBean, ProposalDevelopmentFormBean proposalDevelopmentFormBean){
         this.budgetBean = budgetBean;
        this.proposalId = budgetBean.getProposalNumber();
        this.versionNumber = budgetBean.getVersionNumber();
        refreshRequired = true;/*added for COEUSQA-4103*/
        budgetPeriodModel = new BudgetPeriodModel();
        queryKey = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();
        coeusMessageResources = CoeusMessageResources.getInstance();
        setTableKeyTraversal();
        queryEngine = QueryEngine.getInstance();
        budgetInfoBean = new BudgetInfoBean();
        budgetInfoBean.setProposalNumber(proposalId);
        budgetInfoBean.setVersionNumber(versionNumber);
        
        budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(proposalId);
        budgetPeriodBean.setVersionNumber(versionNumber);
        
        try{
            vecBudgetInfoBean = queryEngine.executeQuery(proposalId + versionNumber, budgetInfoBean);
            vecBudgetPeriodBean = queryEngine.executeQuery(proposalId + versionNumber, budgetPeriodBean);
            
            vecBudgetPeriodBean = vecBudgetPeriodBean.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            
            rateClassBean = new RateClassBean();
            vecOHRate = queryEngine.executeQuery(proposalId + versionNumber,rateClassBean);
            vecURRate = queryEngine.executeQuery(proposalId + versionNumber,rateClassBean);
            
            if(vecBudgetInfoBean != null && vecBudgetInfoBean.size() > 0) {
                budgetInfoBean = (BudgetInfoBean)vecBudgetInfoBean.get(0);
                setFormData(getBeanData(budgetInfoBean, vecBudgetPeriodBean));
            }
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }
        
        budgetSummaryForm.tblBudgetSummary.setModel(budgetPeriodModel);
        columnPeriodRenderer = new ColumnPeriodRenderer();
        budgetSummaryEditor = new BudgetSummaryEditor();
        budgetPeriodModel.setData(vecPeriodDetails);
        
        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_Start
        //setting the recalculated value of UnderRecovery Amount in budgetSummaryForm.txtUnderRecovery field
        budgetSummaryForm.txtUnderRecovery.setValue(budgetInfoBean.getUnderRecoveryAmount());
        budgetSummaryForm.txtCostSharing.setValue(budgetInfoBean.getCostSharingAmount());
        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_End
        
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
        proposalStartDate =  proposalDevelopmentFormBean.getRequestStartDateInitial();
        proposalEndDate = proposalDevelopmentFormBean.getRequestEndDateInitial();
        budgetStatus = proposalDevelopmentFormBean.getBudgetStatus();
        proposalDevelopmentFormBean.setBudgetStatus(budgetStatus);
        setTableEditors();
    }
    //COEUSDEV-241 : End
}
