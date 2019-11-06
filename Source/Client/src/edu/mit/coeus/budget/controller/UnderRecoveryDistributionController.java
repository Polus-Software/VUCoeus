/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.gui.UnderRecoveryDistributionForm;
import edu.mit.coeus.budget.gui.PeriodCostDistributionDetails;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
//import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.gui.event.BeanEvent;

/** This class controls the <CODE>UnderRecoveryDistributionForm</CODE>
 * UnderRecoveryDistributionController.java
 * @author  Vyjayanthi
 * Created on December 8, 2003, 11:35 AM
 */
public class UnderRecoveryDistributionController extends Controller
implements ActionListener {

    private QueryEngine queryEngine;
    private String queryKey;

    private UnderRecoveryDistributionForm underRecoveryForm;
    private PeriodCostDistributionDetails periodCostDistributionDetails;

    private UnderRecoveryTableModel underRecoveryTableModel;
    private UnderRecoveryRenderer underRecoveryRenderer;
    private UnderRecoveryEditor underRecoveryEditor;
    
    private DollarCurrencyTextField dollarData;
    private double totalUnderRecoveryAmount;
    
    private boolean hasNoUnderRecovery = false;

    private ProposalIDCRateBean proposalIDCRateBean;
    
    private CoeusVector cvBudgetPeriods;
    private CoeusVector cvBudgetUnderrecovery;
    private CoeusVector cvDeletedBeans;
    
    private BudgetPeriodBean budgetPeriodBean;
    private BudgetInfoBean budgetInfoBean;
    
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";
    private static final String UNDER_RECOVERY = "Underrecovery";
    private static final String APPLICABLE_RATE = "applicableIDCRate";
    private static final String FISCAL_YEAR = "fiscalYear";
    private static final String CAMPUS_FLAG = "onOffCampusFlag";
    private static final String SOURCE_ACCOUNT = "sourceAccount";
    private static final String UNDER_RECOVERY_FIELD = "underRecoveryIDC";
    private static final String RATE_CLASS_CODE = "rateClassCode";
    private static final String RATE_TYPE_CODE = "rateTypeCode";
    
    private static final int RATE_COLUMN = 0;
    private static final int FISCAL_YEAR_COLUMN = 1;
    private static final int CAMPUS_COLUMN = 2;
    private static final int UNDER_RECOVERY_COLUMN = 3;
    private static final int SOURCE_ACCOUNT_COLUMN = 4;
    
    private static final String ON_CAMPUS = "On";
    private static final String OFF_CAMPUS = "Off";
    
    private static final String DELETE_ROW = "budget_common_exceptionCode.1006";
    private static final String ENTER_SOURCE_ACCOUNT = "underRecovery_exceptionCode.1501";// <<row index>>
    private static final String ENTER_VALID_FISCAL_YEAR = "underRecovery_exceptionCode.1502";// <<row index>>
    private static final String SELECT_A_ROW_TO_DELETE = "budget_common_exceptionCode.1007";
    private static final String CHECK_UNDER_RECOVERY = "underRecovery_exceptionCode.1503";// <<total amount>>
    //private static final String ENTER_RATE = "underRecovery_exceptionCode.1504";// <<row index>>
    private static final String ENTER_FISCAL_YEAR = "underRecovery_exceptionCode.1505";// <<row index>>
    //private static final String CHECK_CAMPUS_FLAG = "underRecovery_exceptionCode.1506";// <<row index>>
    private static final String ENTER_UNDER_RECOVERY = "underRecovery_exceptionCode.1507";// <<row index>>
    private static final String DUPLICATE_ROW = "underRecovery_exceptionCode.1508";
    private static final String NO_UNDER_RECOVERY = "underRecovery_exceptionCode.1509";
    
    private boolean fromHierarchy;
    private static final char GET_COST_SHARING_DISTRIBUTION = 'h'; 
    private static final char GET_BUDGET_PERIOD = 'i'; 
    private static final char GET_BUDGET_INFO = 'j';
    private static final char GET_PROPOSAL_DATES = 'k';
    private static final char UNDER_RECOVERY_DATA = 'U'; 
    public  static final String SERVLET = "/BudgetMaintenanceServlet";
    private Vector dataObject;
    private int accountNumberMaxLength = 0;
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */    
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** Creates a new instance of UnderRecoveryDistributionController */
    public UnderRecoveryDistributionController() {
        queryEngine = QueryEngine.getInstance();
        dollarData = new DollarCurrencyTextField();
        underRecoveryTableModel = new UnderRecoveryTableModel();
        underRecoveryRenderer = new UnderRecoveryRenderer();
        underRecoveryEditor = new UnderRecoveryEditor();
        underRecoveryForm = new UnderRecoveryDistributionForm(
            CoeusGuiConstants.getMDIForm(), true);
        registerComponents();
        setTableEditors();
       
    }
    
    /** Creates a new instance of UnderRecoveryDistributionController 
     * @param budgetInfoBean takes a budgetInfoBean */
    public UnderRecoveryDistributionController(BudgetInfoBean budgetInfoBean){
        this();
        this.budgetInfoBean = budgetInfoBean;
    }

    /** Set the title of the screen and values of version and totalunderrecovery */
    private void setLabelValues(){
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            underRecoveryForm.dlgUnderRecovery.setTitle(
                "Display Proposal Under Recovery for " + budgetInfoBean.getProposalNumber() + 
                ", Version " + budgetInfoBean.getVersionNumber() );
        }else{
            underRecoveryForm.dlgUnderRecovery.setTitle(
                "Modify Proposal Under Recovery for " + budgetInfoBean.getProposalNumber() + 
                ", Version " + budgetInfoBean.getVersionNumber() );
        }
        underRecoveryForm.txtVersion.setText(EMPTY_STRING + 
                budgetInfoBean.getVersionNumber());
        underRecoveryForm.txtTotalUnderRecovery.setValue(totalUnderRecoveryAmount);
    }
    
    /** Set the title of the screen and values of version and totalunderrecovery */
    private void setLabelValues(String proposalNumber, int versionNumber){
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            underRecoveryForm.dlgUnderRecovery.setTitle(
                "Display Proposal Under Recovery for " + proposalNumber + 
                ", Version " + versionNumber );
        }else{
            underRecoveryForm.dlgUnderRecovery.setTitle(
                "Modify Proposal Under Recovery for " + proposalNumber + 
                ", Version " + versionNumber );
        }
        underRecoveryForm.txtVersion.setText(EMPTY_STRING + 
                versionNumber);
        underRecoveryForm.txtTotalUnderRecovery.setValue(totalUnderRecoveryAmount);
    }
    
    /** Displays the Form which is being controlled. */    
    public void display() {
        if ( hasNoUnderRecovery ){
            hasNoUnderRecovery = true;
            return;
        }
        underRecoveryForm.dlgUnderRecovery.show();
    }

    /** Method to set the Table Editors */    
    private void setTableEditors(){
        JTableHeader header = underRecoveryForm.tblUnderRecoveryDistribution.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(CoeusFontFactory.getLabelFont());
        
        /** Setting Table Column */
        int colSize[] = {55, 70, 60, 110, 100};
        for(int col = 0; col < colSize.length; col++) {
            TableColumn tableColumn = underRecoveryForm.tblUnderRecoveryDistribution.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[col]);
            tableColumn.setMinWidth(colSize[col]);
            tableColumn.setMaxWidth(colSize[col] + 50);
            tableColumn.setCellRenderer(underRecoveryRenderer);
            tableColumn.setCellEditor(underRecoveryEditor);
        }
    }

    /** Perform field formatting.
     * enabling, disabling components depending on the function type. */
    public void formatFields() {
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            underRecoveryForm.btnAdd.setEnabled(false);
            underRecoveryForm.btnOk.setEnabled(false);
            underRecoveryForm.btnDelete.setEnabled(false);
            underRecoveryForm.tblUnderRecoveryDistribution.setEnabled(false);
            underRecoveryForm.tblUnderRecoveryDistribution.setBackground(
                (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    /** An overridden method of the controller
     * @return budgetTotalForm returns the controlled form component */
    public java.awt.Component getControlledUI() {
        return underRecoveryForm;
    }
    
    /** An overridden method of the Controller to return the form object
     * @return returns the form object, since its not used here it returns null */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components. */
    public void registerComponents() {
        
        /** Setting table model for the table */
        underRecoveryForm.tblUnderRecoveryDistribution.setModel(underRecoveryTableModel);
        underRecoveryForm.tblUnderRecoveryDistribution.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        underRecoveryForm.tblUnderRecoveryDistribution.setSelectionBackground(Color.YELLOW);
        underRecoveryForm.tblUnderRecoveryDistribution.setSelectionForeground(Color.BLACK);

        //Code for focus traversal - start
        java.awt.Component[] components = {underRecoveryForm.tblUnderRecoveryDistribution,
        underRecoveryForm.btnOk,
        underRecoveryForm.btnCancel,
        underRecoveryForm.btnAdd,
        underRecoveryForm.btnDelete
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        underRecoveryForm.dlgUnderRecovery.setFocusTraversalPolicy(traversePolicy);
        underRecoveryForm.dlgUnderRecovery.setFocusCycleRoot(true);
        //Code for focus traversal - end
        
        underRecoveryForm.btnAdd.addActionListener(this);
        underRecoveryForm.btnCancel.addActionListener(this);
        underRecoveryForm.btnDelete.addActionListener(this);
        underRecoveryForm.btnOk.addActionListener(this);
        
        underRecoveryForm.dlgUnderRecovery.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
        });
        
        underRecoveryForm.dlgUnderRecovery.addWindowListener(new WindowAdapter(){            
            
            public void windowOpened(WindowEvent we) {
                requestDefaultFocus();
            }
            
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
    }//End Register Components
    
    /** To set the default focus for the components depending 
     * on the function type */
    private void requestDefaultFocus(){
        if( getFunctionType() != TypeConstants.DISPLAY_MODE ){
            if(underRecoveryForm.tblUnderRecoveryDistribution.getRowCount() > 0){
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(0,0);
                underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(0,0);
                underRecoveryForm.tblUnderRecoveryDistribution.getEditorComponent().requestFocusInWindow();
            }else{
                underRecoveryForm.btnAdd.requestFocus();
            }
        }else {
            underRecoveryForm.btnCancel.requestFocus();
        }
    }
    
    /** To get the max rowId of the existing beans
     * @return cvExistingRecords.size() the count of existing beans
     */
    private int getExistingMaxId(){
        CoeusVector cvExistingRecords = new CoeusVector();
        try{
            cvExistingRecords = queryEngine.getDetails(queryKey, ProposalIDCRateBean.class);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        return cvExistingRecords.size();
    }

    /** Saves the Form Data. */
    public void saveFormData() {
        try{
            underRecoveryEditor.stopCellEditing();
            underRecoveryForm.dlgUnderRecovery.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            underRecoveryForm.dlgUnderRecovery.getGlassPane().setVisible(true);
            if( validate() ){
                if( isSaveRequired() ){
                    //Delete all the deleted beans from the query engine
                    for( int index = 0; index < cvDeletedBeans.size(); index++ ){
                        proposalIDCRateBean = (ProposalIDCRateBean)cvDeletedBeans.get(index);
                        queryEngine.delete(queryKey, proposalIDCRateBean);
                    }

                    //Save data to query engine
                    for( int index = 0; index < cvBudgetUnderrecovery.size(); index++ ){
                        proposalIDCRateBean = (ProposalIDCRateBean)cvBudgetUnderrecovery.get(index);
                        if( proposalIDCRateBean.getAcType() != null ){
                            if( proposalIDCRateBean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                                //Delete the existing bean in the query engine 
                                //got from the database and insert it with new rowId
                                ProposalIDCRateBean existingBean = proposalIDCRateBean;
                                queryEngine.delete(queryKey, proposalIDCRateBean);
                                existingBean.setAcType(TypeConstants.INSERT_RECORD);
                                existingBean.setRowId(getExistingMaxId() + index + 1);
                                queryEngine.insert(queryKey, existingBean);
                            }else if( proposalIDCRateBean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
                                //Set the rowId of the bean if not already set
                                if( proposalIDCRateBean.getRowId() == 0 ){
                                    proposalIDCRateBean.setRowId(getExistingMaxId() + index + 1);
                                }
                                queryEngine.insert(queryKey, proposalIDCRateBean);
                            }
                        }
                    }
                    
                    //Bug - fix code - Start
                    //To update total under-recovery amount of budgetInfoBean in query engine
                    double value = ((double)Math.round(cvBudgetUnderrecovery.sum(UNDER_RECOVERY_FIELD) *
                                    Math.pow(10.0, 2) )) / 100;
                    budgetInfoBean.setTotalIDCRateDistribution(value);

                    queryEngine.update(queryKey, budgetInfoBean);

                    BeanEvent beanEvent = new BeanEvent();
                    beanEvent.setBean(budgetInfoBean);
                    beanEvent.setSource(this);
                    fireBeanUpdated(beanEvent);
                    //Bug - fix code - End
                    
                    underRecoveryForm.dlgUnderRecovery.setVisible(false);
                    setSaveRequired(false);
                }else {
                    underRecoveryForm.dlgUnderRecovery.setVisible(false);
                }
            }
            underRecoveryForm.dlgUnderRecovery.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            underRecoveryForm.dlgUnderRecovery.getGlassPane().setVisible(true);
        }catch (CoeusUIException coeusUIException){
            coeusUIException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form */
    public void setFormData(Object data) {
        if ( data == null ) return;
        budgetInfoBean = ( BudgetInfoBean )data;
        cvDeletedBeans = new CoeusVector();
        queryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();

        //Query and get the budgetPeriodBeans
        try{
            cvBudgetPeriods = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class,
                CoeusVector.FILTER_ACTIVE_BEANS);
            for( int index = 0; index < cvBudgetPeriods.size(); index++ ){
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriods.get(index);
                totalUnderRecoveryAmount = totalUnderRecoveryAmount + 
                    ((double)Math.round(budgetPeriodBean.getUnderRecoveryAmount()* 
                    Math.pow(10.0, 2) )) / 100;
            }
            totalUnderRecoveryAmount = ((double)Math.round(
                totalUnderRecoveryAmount * Math.pow(10.0, 2) )) / 100;
             //Case#2402- use a parameter to set the length of the account number throughout app
             initAccountNumberMaxLength();
             //Case#2402 - End
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
//        catch (DBException dBE){
//            dBE.printStackTrace();
//            CoeusOptionPane.showErrorDialog(dBE.getMessage());
//        }

        //If no underrecovery for this version of the budget, display msg
        if ( totalUnderRecoveryAmount <= 0 ){
            hasNoUnderRecovery = true;
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_UNDER_RECOVERY) );
            return;
        }

        /** Underrecovery exists. Proceed further to set the form data */
        
        //Set the screen title and other label values
        setLabelValues();
        
        //Set the form's panel data
        preparePanel(cvBudgetPeriods);
        
        /** Query and get vector of proposalIDCRateBeans */
        try{
            cvBudgetUnderrecovery = queryEngine.executeQuery(queryKey,
                ProposalIDCRateBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            underRecoveryTableModel.setData(cvBudgetUnderrecovery);
            return;
        }
        /** If there is no underrecovery distribution done then prepare the
         * default vector by setting the applicable rates and campus flags
         * for the corresponding fiscal years from the rates table */
        if( cvBudgetUnderrecovery.size() == 0 ){
            ProposalRatesBean proposalRatesBean = new ProposalRatesBean();
            Equals eqRateClassCode = new Equals(RATE_CLASS_CODE, new Integer(budgetInfoBean.getOhRateClassCode() ));
            Equals eqRateTypeCode = new Equals(RATE_TYPE_CODE, new Integer(budgetInfoBean.getOhRateTypeCode() ));
            And eqRCCAndEqRTC = new And(eqRateClassCode, eqRateTypeCode);
            CoeusVector cvRates = new CoeusVector();
            try{
                cvRates = queryEngine.executeQuery(queryKey, ProposalRatesBean.class, eqRCCAndEqRTC);
                cvRates = cvRates.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
                CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            }
            if( cvRates.size() > 0 ){
                for(int index = 0; index < cvRates.size(); index++ ){
                    proposalRatesBean = (ProposalRatesBean)cvRates.get(index);
                    ProposalIDCRateBean proposalIDCRateBean = new ProposalIDCRateBean();
                    proposalIDCRateBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    proposalIDCRateBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    proposalIDCRateBean.setApplicableIDCRate(proposalRatesBean.getApplicableRate());
                    proposalIDCRateBean.setFiscalYear(proposalRatesBean.getFiscalYear());
                    proposalIDCRateBean.setOnOffCampusFlag(proposalRatesBean.isOnOffCampusFlag());
                    proposalIDCRateBean.setSourceAccount(EMPTY_STRING);
                    proposalIDCRateBean.setUnderRecoveryIDC(0);
                    proposalIDCRateBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvBudgetUnderrecovery.addElement(proposalIDCRateBean);
                }
                setSaveRequired(true);
            }            
        }
        
        //Set the form's table data
        underRecoveryTableModel.setData(cvBudgetUnderrecovery);
        
        if( cvBudgetUnderrecovery.size() > 0 ){
            underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(0,0);
        }
    }
    
    /** This method will be invoked when the window is operning for the seletcted node in 
     *the propsoal hierarchy
     */
    public void setDataForHierarcy(){
        
        String proposalNumber = EMPTY_STRING;
        int versionNumber = 0;
        BudgetInfoBean budgetInfoBean = null;
        try{
            proposalNumber = (String)getDataObject().elementAt(0);
            versionNumber = ((Integer)getDataObject().elementAt(1)).intValue();
            cvBudgetPeriods = getPeriodDataFromServer(proposalNumber,versionNumber);
            for( int index = 0; index < cvBudgetPeriods.size(); index++ ){
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriods.get(index);
                totalUnderRecoveryAmount = totalUnderRecoveryAmount +
                ((double)Math.round(budgetPeriodBean.getUnderRecoveryAmount()*
                Math.pow(10.0, 2) )) / 100;
            }
            totalUnderRecoveryAmount = ((double)Math.round(
            totalUnderRecoveryAmount * Math.pow(10.0, 2) )) / 100;
            if ( totalUnderRecoveryAmount <= 0 ){
                hasNoUnderRecovery = true;
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(NO_UNDER_RECOVERY) );
                return;
            }
            //Set the screen title and other label values
            setLabelValues(proposalNumber,versionNumber);
            
            //Set the form's panel data
            preparePanel(cvBudgetPeriods);
            /** Query and get vector of proposalIDCRateBeans */
                cvBudgetUnderrecovery = getUnderrecoveryDataFromServer(proposalNumber,versionNumber);
            if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
                underRecoveryTableModel.setData(cvBudgetUnderrecovery);
                return;
            }
            /** If there is no underrecovery distribution done then prepare the
             * default vector by setting the applicable rates and campus flags
             * for the corresponding fiscal years from the rates table */
            if( cvBudgetUnderrecovery.size() == 0 ){
                ProposalRatesBean proposalRatesBean = new ProposalRatesBean();
                
                CoeusVector cvBudgetInfo = getBudgetInfoDataFromServer(proposalNumber,versionNumber);
                if(cvBudgetInfo!= null && cvBudgetInfo.size() > 0){
                    budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(0);
                }
                
                Equals eqRateClassCode = new Equals(RATE_CLASS_CODE, new Integer(budgetInfoBean.getOhRateClassCode() ));
                Equals eqRateTypeCode = new Equals(RATE_TYPE_CODE, new Integer(budgetInfoBean.getOhRateTypeCode() ));
                And eqRCCAndEqRTC = new And(eqRateClassCode, eqRateTypeCode);
                CoeusVector cvRates = new CoeusVector();
                try{
                    cvRates = getProposalRates(proposalNumber, versionNumber);
                    cvRates = cvRates.filter(eqRCCAndEqRTC);
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                }
                if( cvRates.size() > 0 ){
                    for(int index = 0; index < cvRates.size(); index++ ){
                        proposalRatesBean = (ProposalRatesBean)cvRates.get(index);
                        ProposalIDCRateBean proposalIDCRateBean = new ProposalIDCRateBean();
                        proposalIDCRateBean.setProposalNumber(proposalNumber);
                        proposalIDCRateBean.setVersionNumber(versionNumber);
                        proposalIDCRateBean.setApplicableIDCRate(proposalRatesBean.getApplicableRate());
                        proposalIDCRateBean.setFiscalYear(proposalRatesBean.getFiscalYear());
                        proposalIDCRateBean.setOnOffCampusFlag(proposalRatesBean.isOnOffCampusFlag());
                        proposalIDCRateBean.setSourceAccount(EMPTY_STRING);
                        proposalIDCRateBean.setUnderRecoveryIDC(0);
                        proposalIDCRateBean.setAcType(TypeConstants.INSERT_RECORD);
                        cvBudgetUnderrecovery.addElement(proposalIDCRateBean);
                    }
                    setSaveRequired(false);
                }
            }
            
            //Set the form's table data
            underRecoveryTableModel.setData(cvBudgetUnderrecovery);
            
            if( cvBudgetUnderrecovery.size() > 0 ){
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(0,0);
            }
        }catch (Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
     
     /** Get the Under recovery data from the server when it is opening from the Proposal 
      *Hiearchy.Get the data for the  selected node which contains Propsoal Number
      *and Version number
      *@param Propsoal Number, VersionNumber
      *@returns CoeusVector contains the vector of UnderRecovery details data
      */
      private CoeusVector getUnderrecoveryDataFromServer(String proposalNumber, int versionNumber) throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(proposalNumber);
       cvData.addElement(new Integer(versionNumber));
       cvData.addElement(new Character(UNDER_RECOVERY_DATA));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_COST_SHARING_DISTRIBUTION);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
      
      /** Get the Institute Proposal Rates data from the server when it is opening from the Proposal 
      *Hiearchy.Get the data for the  selected node which contains Propsoal Number
      *and Version number
      *@param Propsoal Number, VersionNumber
      *@returns CoeusVector contains the vector Institute Propsoal Rates data
      */
     private CoeusVector getProposalRates(String proposalNumber, int versionNumber) throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(proposalNumber);
       cvData.addElement(new Integer(versionNumber));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_PROPOSAL_DATES);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
      
     /** Get the Budget Periods  data from the server when it is opening from the Proposal 
      *Hiearchy.Get the data for the  selected node which contains Propsoal Number
      *and Version number
      *@param Propsoal Number, VersionNumber
      *@returns CoeusVector contains the Budget Periods data
      */
    private CoeusVector getPeriodDataFromServer(String proposalNumber, int versionNumber) throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(proposalNumber);
       cvData.addElement(new Integer(versionNumber));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_BUDGET_PERIOD);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
    /** Get the Budget Info data from the server when it is opening from the Proposal 
      *Hiearchy.Get the data for the  selected node which contains Propsoal Number
      *and Version number
      *@param Propsoal Number, VersionNumber
      *@returns CoeusVector contains the one Budget InfoBean Data
      */
    private CoeusVector getBudgetInfoDataFromServer(String proposalNumber, int versionNumber) throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(proposalNumber);
       cvData.addElement(new Integer(versionNumber));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_BUDGET_INFO);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
    
    
    /** This method is used to set the panel data
     * @param cvBudgetPeriods data to set the panel */
    public void preparePanel(Vector cvBudgetPeriods) {
        java.awt.GridBagConstraints gridBagConstraints;
        if ( cvBudgetPeriods.size() <= 4 ){
            underRecoveryForm.pnlMainPanel.setLayout(new GridLayout(4,1,0,0));
        }else{
            underRecoveryForm.pnlMainPanel.setLayout(new GridLayout(cvBudgetPeriods.size(),1,0,0));
        }
        for(int periodIndex=0; periodIndex < cvBudgetPeriods.size(); periodIndex++ ){
            budgetPeriodBean = ( BudgetPeriodBean )cvBudgetPeriods.get(periodIndex);
            /** Set the values to the panel component and add it to pnlMainPanel
             * of the underrecovery form */
            periodCostDistributionDetails = new PeriodCostDistributionDetails(
                    UNDER_RECOVERY, periodIndex + 1, budgetPeriodBean.getUnderRecoveryAmount());
            underRecoveryForm.pnlMainPanel.add(periodCostDistributionDetails.getChildPanel());
            /** Synchronize the display of period and total underrecovery fields
             * with the budget version and budget total underrecovery */
            if( cvBudgetPeriods.size() > 4 ){
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.insets = new java.awt.Insets(4, 17, 0, 5);
                JLabel lblPeriod = periodCostDistributionDetails.lblPeriod;
                periodCostDistributionDetails.pnlDetails.remove(lblPeriod);
                periodCostDistributionDetails.pnlDetails.add(lblPeriod, gridBagConstraints);
            }
        }
        underRecoveryForm.scrPnPeriod.setViewportView(underRecoveryForm.getMainPanel());
    }
   
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if validation is through else returns false.
     * here since the form is only for disply it always returns false */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        underRecoveryEditor.stopCellEditing();
        String fiscalYear;
        CoeusVector cvDuplicateData = new CoeusVector();
        for( int row = 0, nextRow = 1; row < cvBudgetUnderrecovery.size(); row++, nextRow++){
            ProposalIDCRateBean propIDCRateBean = ( ProposalIDCRateBean )
                cvBudgetUnderrecovery.get(row);
//            if( propIDCRateBean.getApplicableIDCRate() == 0 ){
//                CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(ENTER_RATE) + SPACE + nextRow);
//                underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(row, RATE_COLUMN);
//                underRecoveryEditor.txtRate.requestFocus();
//                underRecoveryEditor.txtRate.setCaretPosition(0);
//                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(row,row);
//                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
//                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(row, RATE_COLUMN, true));
//                return false;
//            }
            fiscalYear = propIDCRateBean.getFiscalYear();
            if( fiscalYear == null || fiscalYear.trim().equals(EMPTY_STRING) ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_FISCAL_YEAR) + SPACE + nextRow);
                underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(row,FISCAL_YEAR_COLUMN);
                underRecoveryEditor.txtFiscalYear.requestFocus();
                underRecoveryEditor.txtFiscalYear.setCaretPosition(0);
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(row,row);
                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(row, FISCAL_YEAR_COLUMN, true));
                return false;
            }else if( fiscalYear.length() < 4 || ( !fiscalYear.startsWith("19") &&
            !fiscalYear.startsWith("20")) ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_VALID_FISCAL_YEAR) + SPACE + nextRow);
                underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(row,FISCAL_YEAR_COLUMN);
                underRecoveryEditor.txtFiscalYear.requestFocus();
                underRecoveryEditor.txtFiscalYear.setCaretPosition(0);
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(row,row);
                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(row, FISCAL_YEAR_COLUMN, true));
                return false;
            }
            //no null check for onOffCampus is done coz its value will never be null
            
            //Bug-fix comment by Vyjayanthi to allow zero amount
            /*
            if( propIDCRateBean.getUnderRecoveryIDC() == 0 ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_UNDER_RECOVERY) + SPACE + nextRow);
                underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(row, UNDER_RECOVERY_COLUMN);
                underRecoveryEditor.txtUnderRecovery.requestFocus();
                underRecoveryEditor.txtUnderRecovery.setCaretPosition(0);
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(row,row);
                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(row, UNDER_RECOVERY_COLUMN, true));
                return false;
            }else 
             */
            if( propIDCRateBean.getSourceAccount() == null ||
            propIDCRateBean.getSourceAccount().trim().equals(EMPTY_STRING) ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_SOURCE_ACCOUNT) + SPACE + nextRow);
                underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(row, SOURCE_ACCOUNT_COLUMN);
                underRecoveryEditor.txtSourceAccount.requestFocus();
                underRecoveryEditor.txtSourceAccount.setCaretPosition(0);
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(row,row);
                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(row, SOURCE_ACCOUNT_COLUMN, true));
                return false;
            }
        }
        /** Check if duplicate rows exist */
        for( int row = 0; row < cvBudgetUnderrecovery.size(); row++){
            ProposalIDCRateBean propIDCRateBean = ( ProposalIDCRateBean )
                    cvBudgetUnderrecovery.get(row);
            Equals eqRate = new Equals( APPLICABLE_RATE, new Double(propIDCRateBean.getApplicableIDCRate() ));
            Equals eqFiscalYear = new Equals(FISCAL_YEAR, propIDCRateBean.getFiscalYear() );
            Equals eqOnOffCampus = new Equals(CAMPUS_FLAG, propIDCRateBean.isOnOffCampusFlag() );
            Equals eqSourceAccount = new Equals(SOURCE_ACCOUNT, propIDCRateBean.getSourceAccount().trim() );
            And eqRateAndEqYear = new And(eqRate, eqFiscalYear);
            And eqCampusAndEqAccount = new And(eqOnOffCampus, eqSourceAccount);
            And operator = new And(eqRateAndEqYear, eqCampusAndEqAccount);
            cvDuplicateData = cvBudgetUnderrecovery.filter(operator);
            if( cvDuplicateData.size() > 1 ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(DUPLICATE_ROW));
                underRecoveryEditor.txtRate.requestFocus();
                underRecoveryEditor.txtRate.setCaretPosition(0);
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(row,row);
                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(row, RATE_COLUMN, true));
                return false;
            }
        }
        if( cvBudgetUnderrecovery.size() > 0 ){
            double value = ((double)Math.round(cvBudgetUnderrecovery.sum(UNDER_RECOVERY_FIELD) *
                            Math.pow(10.0, 2) )) / 100;
            
            if( value != totalUnderRecoveryAmount ){
                /** Sum of cost sharing amount from the distribution list should be
                 * equal to the total cost sharing amount for the budget */
                dollarData.setText(EMPTY_STRING + totalUnderRecoveryAmount);
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(CHECK_UNDER_RECOVERY) + 
                    SPACE + dollarData.getText());
                underRecoveryEditor.txtSourceAccount.requestFocus();
                underRecoveryEditor.txtSourceAccount.setCaretPosition(0);
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(0,0);
                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(0, UNDER_RECOVERY_COLUMN, true));
                return false;
            }
        }
        //Passed all validation checks
        return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if ( source.equals(underRecoveryForm.btnOk) ){
            saveFormData();
        }else if ( source.equals(underRecoveryForm.btnCancel) ){
            performWindowClosing();
        }else if ( source.equals(underRecoveryForm.btnAdd) ){
            performAddOperation();
        }else if ( source.equals(underRecoveryForm.btnDelete) ){
            performDeleteOperation();
        }
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing(){        
        int option = JOptionPane.NO_OPTION;
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            if(isSaveRequired()){
                option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                switch( option ){
                    case ( JOptionPane.YES_OPTION ):
                        try{
                            saveFormData();
                        }catch(Exception e){
                            e.printStackTrace();
                            CoeusOptionPane.showErrorDialog(e.getMessage());
                        }
                        break;
                    case ( JOptionPane.NO_OPTION ):
                        underRecoveryForm.dlgUnderRecovery.dispose();
                        break;
                    case ( JOptionPane.CANCEL_OPTION ) :
                        underRecoveryForm.dlgUnderRecovery.setVisible( true );
                        break;
                }
            }else{
                underRecoveryForm.dlgUnderRecovery.dispose();
            }
        }else{
            underRecoveryForm.dlgUnderRecovery.dispose();
        }
    }
    
    /** Adds a row to the end of the underrecovery table. */
    private void performAddOperation(){
        underRecoveryEditor.stopCellEditing();

        int row = cvBudgetUnderrecovery.size() + 1;
        ProposalIDCRateBean proposalIDCRateBean = new ProposalIDCRateBean();
        proposalIDCRateBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        proposalIDCRateBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        proposalIDCRateBean.setApplicableIDCRate(0);
        proposalIDCRateBean.setFiscalYear(EMPTY_STRING);
        proposalIDCRateBean.setOnOffCampusFlag(true);
        proposalIDCRateBean.setUnderRecoveryIDC(0);
        proposalIDCRateBean.setSourceAccount(EMPTY_STRING);
        proposalIDCRateBean.setAcType(TypeConstants.INSERT_RECORD);
        cvBudgetUnderrecovery.addElement(proposalIDCRateBean);
        setSaveRequired(true);

        underRecoveryTableModel.fireTableRowsInserted(row - 1, row - 1);
        underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(row -1, row - 1);
        underRecoveryForm.tblUnderRecoveryDistribution.setColumnSelectionInterval(RATE_COLUMN,RATE_COLUMN);
        underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(row - 1, RATE_COLUMN);
        underRecoveryForm.tblUnderRecoveryDistribution.getEditorComponent().requestFocusInWindow();
        
        underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
            underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(row -1, RATE_COLUMN, true));
    }
    
    /** Deletes the selected row from the underrecovery table. */
    private void performDeleteOperation(){
        underRecoveryEditor.stopCellEditing();
        int selectedRow = underRecoveryForm.tblUnderRecoveryDistribution.getSelectedRow();
        //No row selected to delete, display message
        if(selectedRow < 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            SELECT_A_ROW_TO_DELETE));
            return;
        }
        int selection = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(DELETE_ROW),
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
        if(selection == CoeusOptionPane.SELECTION_NO) return ;
        
        ProposalIDCRateBean deletedBean = (ProposalIDCRateBean)
        cvBudgetUnderrecovery.get(selectedRow);
        cvDeletedBeans.addElement(deletedBean);
        cvBudgetUnderrecovery.remove(selectedRow);
        setSaveRequired(true);
        underRecoveryTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
        if( cvBudgetUnderrecovery.size() > 1 ){
            if( selectedRow > 1 ){
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                underRecoveryForm.tblUnderRecoveryDistribution.setColumnSelectionInterval(RATE_COLUMN,RATE_COLUMN);
                if(selectedRow!=0){
                    underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(selectedRow - 1,RATE_COLUMN);
                    underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                    underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(selectedRow - 1, RATE_COLUMN, true));
                    underRecoveryForm.tblUnderRecoveryDistribution.getEditorComponent().requestFocusInWindow();
                }else{
                    underRecoveryForm.btnAdd.requestFocusInWindow();
                }
            }else{
                underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(selectedRow, selectedRow);
                underRecoveryForm.tblUnderRecoveryDistribution.setColumnSelectionInterval(RATE_COLUMN,RATE_COLUMN);
                underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(selectedRow - 1,RATE_COLUMN);
                underRecoveryForm.tblUnderRecoveryDistribution.getEditorComponent().requestFocusInWindow();
                
                
                underRecoveryForm.tblUnderRecoveryDistribution.scrollRectToVisible(
                underRecoveryForm.tblUnderRecoveryDistribution.getCellRect(selectedRow, RATE_COLUMN, true));
                underRecoveryForm.tblUnderRecoveryDistribution.getEditorComponent().requestFocusInWindow();
            }
        }else if( cvBudgetUnderrecovery.size() == 1){
            underRecoveryForm.tblUnderRecoveryDistribution.setRowSelectionInterval(0, 0);
            underRecoveryForm.tblUnderRecoveryDistribution.setColumnSelectionInterval(RATE_COLUMN,RATE_COLUMN);
            underRecoveryForm.tblUnderRecoveryDistribution.editCellAt(0,RATE_COLUMN);
            underRecoveryForm.tblUnderRecoveryDistribution.getEditorComponent().requestFocusInWindow();
        }
    }

    /**
     * Getter for property fromHierarchy.
     * @return Value of property fromHierarchy.
     */
    public boolean isFromHierarchy() {
        return fromHierarchy;
    }
    
    /**
     * Setter for property fromHierarchy.
     * @param fromHierarchy New value of property fromHierarchy.
     */
    public void setFromHierarchy(boolean fromHierarchy) {
        this.fromHierarchy = fromHierarchy;
    }
    
    /**
     * Getter for property dataObject.
     * @return Value of property dataObject.
     */
    public java.util.Vector getDataObject() {
        return dataObject;
    }
    
    /**
     * Setter for property dataObject.
     * @param dataObject New value of property dataObject.
     */
    public void setDataObject(java.util.Vector dataObject) {
        this.dataObject = dataObject;
    }
    
    //Inner Class Table Model - Start
    class UnderRecoveryTableModel extends AbstractTableModel{
        
        private CurrencyField currencyField;
        private CoeusVector cvUnderRecovery;
        private ProposalIDCRateBean proposalIDCRateBean;
        
        UnderRecoveryTableModel(){
            currencyField = new CurrencyField();
        }
        
//JM        String colNames[] = {"Rate", "Fiscal Year", "Campus", "Underrecovery", "Source Account"};
        String colNames[] = {"Rate", "Fiscal Year", "Campus", "Underrecovery", "Source Center"}; //JM 5-25-2011 changed to Center per 4.4.2
        Class[] colTypes = new Class [] {
            Double.class, String.class, String.class, String.class, String.class};
            
        /** Sets the data to be displayed by the table.
         * @param cvUnderRecovery Data to be displayed.
         */
        public void setData(CoeusVector cvUnderRecovery) {
            this.cvUnderRecovery = cvUnderRecovery;
        }
        
        public boolean isCellEditable(int row, int column) {
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                return false;
            }else {
                return true;
            }
        }

        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Class getColumnClass(int column) {
            return colTypes[column];
        }
        
        /** Returns row count.
         * @return returns row count.
         */
        public int getRowCount() {
            if( cvUnderRecovery == null ) return 0;
            return cvUnderRecovery.size();
        }
        
        /** Returns the value for the cell at column and row.
         * @param row the row whose value is to be queried
         * @param column the column whose value is to be queried
         * @return the value Object at the specified cell */
        public Object getValueAt(int row, int column) {
            proposalIDCRateBean = ( ProposalIDCRateBean ) cvUnderRecovery.get(row);
            boolean campusFlag = false;
            switch( column ){
                case RATE_COLUMN:
                    return new Double(proposalIDCRateBean.getApplicableIDCRate());
                case FISCAL_YEAR_COLUMN:
                    return proposalIDCRateBean.getFiscalYear();
                case CAMPUS_COLUMN:
                    campusFlag = proposalIDCRateBean.isOnOffCampusFlag();
                    if( campusFlag ){
                        return ON_CAMPUS;
                    }else{
                        return OFF_CAMPUS;
                    }
                case UNDER_RECOVERY_COLUMN:
                    dollarData.setText(EMPTY_STRING + proposalIDCRateBean.getUnderRecoveryIDC());
                    return dollarData.getText();
                case SOURCE_ACCOUNT_COLUMN:
                    return proposalIDCRateBean.getSourceAccount().trim();
            }
            return EMPTY_STRING;
        }
        
        /** Sets the value in the cell at column and row to a value.
         * @param value the new value.
         * @param row the row whose value is to be changed
         * @param column the column whose value is to be changed */
        public void setValueAt(Object value, int row, int column) {
            ProposalIDCRateBean proposalIDCRateBean = (ProposalIDCRateBean)
                    cvBudgetUnderrecovery.get(row);
            double amount = 0.0;
            boolean onCampus = false;
            switch(column){
                case RATE_COLUMN:
                    amount = new Double(value.toString()).doubleValue();
                    if(proposalIDCRateBean.getApplicableIDCRate() == amount ) return ;
                    proposalIDCRateBean.setApplicableIDCRate(amount);
                    setSaveRequired(true);
                    break;
                case FISCAL_YEAR_COLUMN:
                    if(proposalIDCRateBean.getFiscalYear().equals(value.toString())) return ;
                    if ( value == null ) value = EMPTY_STRING;
                    proposalIDCRateBean.setFiscalYear(value.toString());
                    setSaveRequired(true);
                    break;
                case CAMPUS_COLUMN:
                    onCampus = value.toString().equals(ON_CAMPUS);
                    if(proposalIDCRateBean.isOnOffCampusFlag() == onCampus ) return ;                    
                    if( onCampus ){
                        proposalIDCRateBean.setOnOffCampusFlag(true);
                    }else{
                        proposalIDCRateBean.setOnOffCampusFlag(false);
                    }
                    setSaveRequired(true);
                    break;
                case UNDER_RECOVERY_COLUMN:
                    amount = new Double(value.toString()).doubleValue();
                    if(proposalIDCRateBean.getUnderRecoveryIDC() == amount ) return ;                    
                    proposalIDCRateBean.setUnderRecoveryIDC(amount);
                    setSaveRequired(true);
                    break;
                case SOURCE_ACCOUNT_COLUMN:
                    if( value == null ) value = EMPTY_STRING;
                    if(proposalIDCRateBean.getSourceAccount().trim().equals(value.toString().trim())) return ;
                    proposalIDCRateBean.setSourceAccount(value.toString().trim());
                    setSaveRequired(true);
                    break;
            }
            if( proposalIDCRateBean.getAcType() == null ){
                proposalIDCRateBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            underRecoveryTableModel.fireTableRowsUpdated(row, column);
        }        
    }
    
    //UnderRecoveryRenderer - Start
    class UnderRecoveryRenderer extends DefaultTableCellRenderer {

        private JTextField txtSourceAccount;
        CurrencyField txtRate;
        //Added for show grey color in display mode by tarique start 1
        JLabel lblComponent;
        //Added for show grey color in display mode by tarique end 1
        UnderRecoveryRenderer() {
            txtRate = new CurrencyField();
            //Added for show grey color in display mode by tarique start 2
            lblComponent = new JLabel();
            //Added for show grey color in display mode by tarique end 2
            txtRate.setBorder(new EmptyBorder(0,0,0,0));
            //Added for show grey color in display mode by tarique start 2
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            //Added for show grey color in display mode by tarique end 2
            //Case#2402- use a parameter to set the length of the account number throughout app - Start
            txtSourceAccount = new JTextField();
            //Case#2402 - End
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if( column == 0 ){
                txtRate.setText(value.toString());
                if( isSelected ){
                    txtRate.setBackground(Color.YELLOW);
                }else{
                    txtRate.setBackground(Color.WHITE);
                }
                //Added for show grey color in display mode by tarique start 2
                lblComponent.setText(txtRate.getText());
                return lblComponent;
                //Added for show grey color in display mode by tarique end 2
                //return txtRate;
            }
            if( column == 3 ) {
                setHorizontalAlignment(JLabel.RIGHT);
            }else{
                setHorizontalAlignment(JLabel.LEFT);
            }
            //Case#2402- use a parameter to set the length of the account number throughout app - Start
            if(column == SOURCE_ACCOUNT_COLUMN){
                String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                if(getFunctionType() != TypeConstants.DISPLAY_MODE && sourceAccountNumber.length() > accountNumberMaxLength){
                    sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                }
                txtSourceAccount.setText(sourceAccountNumber);
                lblComponent.setText(txtSourceAccount.getText());
                return lblComponent;
            }
            //Case#2402 - End
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }//UnderRecoveryRenderer - End
    
    class UnderRecoveryEditor extends AbstractCellEditor
    implements TableCellEditor{
        
        private DollarCurrencyTextField txtUnderRecovery;
        private JTextField txtSourceAccount;
        private JComboBox cmbCampus;
        private CurrencyField txtRate;
        private JTextField txtFiscalYear;
        private int column;
        String[] campusItems = {ON_CAMPUS, OFF_CAMPUS};
        
        UnderRecoveryEditor(){
            txtSourceAccount = new JTextField();
            txtSourceAccount.setCaretPosition(0);
            //Modified by shiji for bug fix id 1730 - start
//JM            txtSourceAccount.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 7));
            txtSourceAccount.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 10)); //JM 5-25-2011 updated per 4.4.2
            //bug fix id 1730 - end
            cmbCampus = new JComboBox(campusItems);
            
            txtFiscalYear = new JTextField();
            txtFiscalYear.setCaretPosition(0);
            txtFiscalYear.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 4));
            
            txtRate = new CurrencyField();
            txtRate.setCaretPosition(0);
            txtUnderRecovery = new DollarCurrencyTextField();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch (column) {
                case RATE_COLUMN :
                    txtRate.setText(value.toString());
                    return txtRate;
                case FISCAL_YEAR_COLUMN:
                    if(value == null) value = EMPTY_STRING;
                    txtFiscalYear.setText(value.toString());
                    return txtFiscalYear;
                case CAMPUS_COLUMN:
                    if( value.toString().equals(ON_CAMPUS)){
                        cmbCampus.setSelectedIndex(0);
                    }else{
                        cmbCampus.setSelectedIndex(1);
                    }                   
                    return cmbCampus;
                case UNDER_RECOVERY_COLUMN:
                    txtUnderRecovery.setText(value.toString());
                    return txtUnderRecovery;
                case SOURCE_ACCOUNT_COLUMN:
                    //Case#2402- use a parameter to set the length of the account number throughout app - Start
//                    if( value == null ) value = EMPTY_STRING;
                   txtSourceAccount.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                    String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                    if(sourceAccountNumber.length() > accountNumberMaxLength){
                        sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                    } 
                    txtSourceAccount.setText(sourceAccountNumber);
                    //Case#2402 - End
                    return txtSourceAccount;
            }
           // return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            return txtSourceAccount;
        }
        
        /** Overridden method of the Editor
         * @return the value corresponding to the respective components */
        public Object getCellEditorValue() {
            switch (column) {
                case RATE_COLUMN:
                    return txtRate.getText();
                case FISCAL_YEAR_COLUMN:
                    return txtFiscalYear.getText();
                case CAMPUS_COLUMN:
                    return cmbCampus.getSelectedItem();
                case UNDER_RECOVERY_COLUMN:
                    return txtUnderRecovery.getValue();
                case SOURCE_ACCOUNT_COLUMN:
                    return txtSourceAccount.getText();
            }
           // return super.getCellEditorValue();
            return txtSourceAccount;
        }
    }
    
    //Case#2402- use a parameter to set the length of the account number throughout app
    /**
     * Method to intialize account number field size
     */
    private void initAccountNumberMaxLength()throws CoeusException{
        CoeusVector cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
        CoeusVector cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
        if(cvFiltered != null && cvFiltered.size() > 0){
            CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
            accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
        }
    }
}
