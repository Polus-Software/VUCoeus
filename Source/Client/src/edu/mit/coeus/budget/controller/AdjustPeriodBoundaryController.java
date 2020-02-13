/*
 * AdjustPeriodBoundaryController.java
 *
 * Created on November 25, 2003, 5:46 PM
 */

/** Copyright (c) Massachusetts Institute of Technology
  * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
  * All rights reserved.
  */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.budget.gui.AdjustPeriodBoundariesForm;
import edu.mit.coeus.budget.controller.Controller;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.event.*;


import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.text.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;

/**
 *
 * @author  chandrashekara
 */
public class AdjustPeriodBoundaryController  extends Controller implements ActionListener{
    
    AdjustPeriodBoundariesForm  adjustPeriodBoundariesForm;
    /** Specifies the parent window */    
    private CoeusAppletMDIForm mdiForm;
    /** Specifies the modal for the window */    
    private boolean modal;
    BudgetInfoBean budgetInfoBean;
    BudgetPeriodBean budgetPeriodBean;
    /** Specifies the key, which holds the proposal number and version number */    
    private String key;
   // private Controller eventSource = this;
    private QueryEngine queryEngine;
    private String proposalNumber;
    private int versionNumber;
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static String EMPTY_STRING = "";
    private SimpleDateFormat dtFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);;
    private CoeusVector vecPeriodDetails;
    private DateUtils dtUtils = new DateUtils();
    private PeriodBoundaryTableModel periodBoundaryTableModel;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private CoeusDlgWindow dlgPeriods;
    private static final int WIDTH = 385;
    private static final int HEIGHT =330;
    /** Holds the details of the deleted beans */  
    private CoeusVector vecDeletedPeriods;
   
    private String mesg;
   // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start 
   private CoeusVector deletedPeriods;
    // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -End 
    private boolean periodAdjusted;
   
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    /** this variable tells whether this window modified or not
     */
    private boolean modified = false;
    
    /** holds the budget start date */
    private String txtPreviousStartDate;
    
    /** holds budget end date */    
    private String  txtPreviousEndDate;
    
    private static final int HAND_ICON_COLUMN = 0;
    private static final int PERIOD_COLUMN = 1;
    private static final int START_DATE_COLUMN = 2;
    private static final int END_DATE_COLUMN = 3;
    
    private PeriodBoundaryEditor periodBoundaryEditor;
    private PeriodBoundaryRenderer periodBoundaryRenderer;
    
    /** specifies the Budget Start Date for a selected proposal */    
    private java.sql.Date budgetStartDate;
    /** specifies the Budget End Date for specified proposal */    
    private java.sql.Date budgetEndDate;
    //Added for modular budget enhancement delete period start 1
    private CoeusVector cvPeriodModBud;
    private int selRowforModBudget;
    //Added for modular budget enhancement delete period end 1
    //Added for Case#2341 - Recalculate Budget if Project dates change - starts
    private boolean saveNeeded;
    private CoeusVector cvOldPeriodDetails;
    //Added for Case#2341 - Recalculate Budget if Project dates change - ends
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    private CoeusVector cvBudgetPeriods;
    private int rowID = 1;
    private static final int DEFAULT_PERIODS = 10;
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
    /** Creates a new instance of AdjustPeriodBoundaryController
     * @param mdiForm
     * @param modal
     * @param budgetInfoBean
     */
    public AdjustPeriodBoundaryController(CoeusAppletMDIForm mdiForm,
    boolean modal, BudgetInfoBean budgetInfoBean) {
        this.mdiForm = mdiForm;
        this.modal = modal;
        this.budgetInfoBean = budgetInfoBean;
        proposalNumber = budgetInfoBean.getProposalNumber();
        versionNumber = budgetInfoBean.getVersionNumber();
        budgetInfoBean.setProposalNumber(proposalNumber);
        budgetInfoBean.setVersionNumber(versionNumber);
        
        budgetStartDate = budgetInfoBean.getStartDate();
        budgetInfoBean.setStartDate(budgetStartDate);
        budgetEndDate = budgetInfoBean.getEndDate();
        budgetInfoBean.setEndDate(budgetEndDate);
        
        adjustPeriodBoundariesForm = new AdjustPeriodBoundariesForm();
        queryEngine = QueryEngine.getInstance();
        key = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //dtFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        registerComponents();
        setTableKeyTraversal();
        periodBoundaryTableModel = new PeriodBoundaryTableModel();
        adjustPeriodBoundariesForm.tblBoundaries.setModel(periodBoundaryTableModel);
        periodBoundaryEditor = new PeriodBoundaryEditor();
        periodBoundaryRenderer = new PeriodBoundaryRenderer();
        adjustPeriodBoundariesForm.tblBoundaries.setSelectionBackground(Color.YELLOW);
        adjustPeriodBoundariesForm.tblBoundaries.setSelectionForeground(Color.BLACK);
        setTableEditors();
        postInitComponents();
    }
    
    /** Initialize and create the JDialog which extends the CoeusDlgWindow */    
    private void postInitComponents() {
        dlgPeriods = new CoeusDlgWindow(mdiForm);
        dlgPeriods.getContentPane().add(adjustPeriodBoundariesForm);
        dlgPeriods.setTitle(TITLE_WINDOW);
        dlgPeriods.setFont(CoeusFontFactory.getLabelFont());
        dlgPeriods.setModal(true);
        dlgPeriods.setResizable(false);
        dlgPeriods.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgPeriods.getSize();
        dlgPeriods.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgPeriods.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgPeriods.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        dlgPeriods.addWindowListener(new WindowAdapter(){
             public void windowActivated(WindowEvent we) {
                requestDefaultFocus();
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
    }
    
    /** register all the components in the form */    
    public void registerComponents(){
        adjustPeriodBoundariesForm.btnAdd.addActionListener(this);
        adjustPeriodBoundariesForm.btnCancel.addActionListener(this);
        adjustPeriodBoundariesForm.btnDefault.addActionListener(this);
        adjustPeriodBoundariesForm.btnDelete.addActionListener(this);
        adjustPeriodBoundariesForm.btnInsert.addActionListener(this);
        adjustPeriodBoundariesForm.btnOk.addActionListener(this);
        
       java.awt.Component[] components = {
                                        adjustPeriodBoundariesForm.tblBoundaries,
                                        adjustPeriodBoundariesForm.btnOk,
                                        adjustPeriodBoundariesForm.btnCancel,
                                        adjustPeriodBoundariesForm.btnDefault,
                                        adjustPeriodBoundariesForm.btnAdd,
                                        adjustPeriodBoundariesForm.btnInsert,
                                        adjustPeriodBoundariesForm.btnDelete
            
                                        };
        
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        adjustPeriodBoundariesForm.setFocusTraversalPolicy(traversePolicy);
        adjustPeriodBoundariesForm.setFocusCycleRoot(true);

         
        
    }
    
    /** To set the default focus for the components depending 
     * on the function type */
    private void requestDefaultFocus(){
        
        if(adjustPeriodBoundariesForm.tblBoundaries.getRowCount() > 0){
             adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                adjustPeriodBoundariesForm.tblBoundaries.setColumnSelectionInterval(0, 2);
                
                adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,2);
                adjustPeriodBoundariesForm.tblBoundaries.getEditorComponent().requestFocusInWindow();
        }else{
            adjustPeriodBoundariesForm.btnOk.requestFocus();
        }
    }
    
    /** sets the form data. All the data queried from base window and set
     * the values to the table and budget start date and end date
     * @param data
     */    
    public void setFormData(Object data){
        if(data == null) return ;
        budgetInfoBean = (BudgetInfoBean)data;
        vecDeletedPeriods = new CoeusVector();
        
        Equals eqPropNo = new Equals("proposalNumber", budgetInfoBean.getProposalNumber());
        Equals eqVersionNo = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
        And eqPropNoAndEqVersionNo = new And(eqPropNo, eqVersionNo);
        And eqPropNoAndEqVersionNoAndActivePeriods = new And(eqPropNoAndEqVersionNo, CoeusVector.FILTER_ACTIVE_BEANS);
        if ( budgetInfoBean.getStartDate() != null ){
            adjustPeriodBoundariesForm.txtBudgetStartDate.setText(dtUtils.formatDate(
            budgetInfoBean.getStartDate().toString(),"dd-MMM-yyyy"));
            txtPreviousStartDate = adjustPeriodBoundariesForm.txtBudgetStartDate.getText();
        }
        
        if ( budgetInfoBean.getEndDate() != null ){
            adjustPeriodBoundariesForm.txtBudgetEndDate.setText(dtUtils.formatDate(
            budgetInfoBean.getEndDate().toString(),"dd-MMM-yyyy"));
            txtPreviousEndDate = adjustPeriodBoundariesForm.txtBudgetEndDate.getText();
        }
        
        try{
            
            vecPeriodDetails = queryEngine.executeQuery(key,BudgetPeriodBean.class, eqPropNoAndEqVersionNoAndActivePeriods);
            if(vecPeriodDetails!=null || vecPeriodDetails.size()>0){
                //Modified for Case#2341 - Recalculate Budget if Project dates change - starts
                periodBoundaryTableModel = new PeriodBoundaryTableModel();
                adjustPeriodBoundariesForm.tblBoundaries.setModel(periodBoundaryTableModel);
                setTableEditors();
                periodBoundaryTableModel.setData(vecPeriodDetails);
                periodBoundaryTableModel.fireTableDataChanged();
                //Modified for Case#2341 - Recalculate Budget if Project dates change - starts
            }
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    
    /** sets the table editors. Specifies the header sizes and their propertices */    
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = adjustPeriodBoundariesForm.tblBoundaries.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            adjustPeriodBoundariesForm.tblBoundaries.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            adjustPeriodBoundariesForm.tblBoundaries.setRowHeight(22);
            adjustPeriodBoundariesForm.tblBoundaries.setSelectionBackground(java.awt.Color.white);
            adjustPeriodBoundariesForm.tblBoundaries.setSelectionForeground(java.awt.Color.black);
            adjustPeriodBoundariesForm.tblBoundaries.setShowHorizontalLines(false);
            adjustPeriodBoundariesForm.tblBoundaries.setShowVerticalLines(true);
            adjustPeriodBoundariesForm.tblBoundaries.setOpaque(false);
            tableHeader.setResizingAllowed(false);
            adjustPeriodBoundariesForm.tblBoundaries.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            
            TableColumn column = adjustPeriodBoundariesForm.tblBoundaries.getColumnModel().getColumn(HAND_ICON_COLUMN);
            column.setMinWidth(30);
            column.setPreferredWidth(30);
            column.setResizable(false);
            column.setCellRenderer(new IconRenderer());
            column.setHeaderRenderer(new EmptyHeaderRenderer());
            
            column =adjustPeriodBoundariesForm.tblBoundaries.getColumnModel().getColumn(PERIOD_COLUMN);
            column.setMinWidth(45);
            column.setPreferredWidth(50);
            column.setCellEditor(periodBoundaryEditor);
            column.setCellRenderer(periodBoundaryRenderer);
            
            
            column = adjustPeriodBoundariesForm.tblBoundaries.getColumnModel().getColumn(START_DATE_COLUMN);
            column.setMinWidth(90);
            column.setPreferredWidth(95);
            column.setCellEditor(periodBoundaryEditor);
            column.setCellRenderer(periodBoundaryRenderer);
            
            column = adjustPeriodBoundariesForm.tblBoundaries.getColumnModel().getColumn(END_DATE_COLUMN);
            column.setMinWidth(90);
            column.setPreferredWidth(90);
            column.setCellEditor(periodBoundaryEditor);
            column.setCellRenderer(periodBoundaryRenderer);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        int index = adjustPeriodBoundariesForm.tblBoundaries.getSelectedRow();
        try {
            if(index != -1 && index >= 0){
                adjustPeriodBoundariesForm.tblBoundaries.getCellEditor(index,1).stopCellEditing();
            }
            Object source  = actionEvent.getSource();
            if(source==adjustPeriodBoundariesForm.btnAdd){
                performAddAction();
            }else if(source==adjustPeriodBoundariesForm.btnCancel){
                performCancelAction();
            }else if(source==adjustPeriodBoundariesForm.btnDefault){
                performDefaultAction();
            }else if(source==adjustPeriodBoundariesForm.btnDelete){
                performDeleteAction();
            }else if(source==adjustPeriodBoundariesForm.btnInsert){
                performInsertAction();
            }else if(source==adjustPeriodBoundariesForm.btnOk){
                //added for bug fix 1654
                dlgPeriods.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                performOkAction();
            }
            //added for bug fix 1654
        }finally {
            dlgPeriods.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
   
    
    /** Performs the delete action when button Delete is triggred.
     * deletes the periods
     */    
    private void performDeleteAction(){
         // int rowCount = periodBoundaryTableModel.getRowCount();
          int deletedRow = periodBoundaryTableModel.getRowCount()-1;
          if(deletedRow < 0) return;
          int selectedRow = adjustPeriodBoundariesForm.tblBoundaries.getSelectedRow();
          if(selectedRow<0)return;
          //Added for modular budget enhancement delete period start 2
              cvPeriodModBud = new CoeusVector();
              cvPeriodModBud.addAll(vecPeriodDetails);
              selRowforModBudget = selectedRow;
          //Added for modular budget enhancement delete period end 2
              vecPeriodDetails.remove(selectedRow);
              periodBoundaryTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
              modified = true;
          // This logic is used to take n-1 period numbers in the budget period
          for(int index = selectedRow; index < vecPeriodDetails.size(); index++) {
              budgetPeriodBean = (BudgetPeriodBean)vecPeriodDetails.get(index);
              budgetPeriodBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod() - 1);
              budgetPeriodBean.setAw_BudgetPeriod(budgetPeriodBean.getBudgetPeriod());
              modified=true;
              if (budgetPeriodBean.getAcType() == null) {
                  budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
              } else {
                  budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
              }
          }
          periodBoundaryTableModel.fireTableRowsUpdated(deletedRow, vecPeriodDetails.size());
          modified = true;
          if(selectedRow >0){
              adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(
              selectedRow-1,selectedRow-1);
              int row = selectedRow-1;
              if(row!= 0){
                  adjustPeriodBoundariesForm.tblBoundaries.setColumnSelectionInterval(START_DATE_COLUMN, START_DATE_COLUMN);
                  adjustPeriodBoundariesForm.tblBoundaries.editCellAt(selectedRow-1,selectedRow-1);
//                  adjustPeriodBoundariesForm.tblBoundaries.getEditorComponent().requestFocusInWindow();
                  adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
                  adjustPeriodBoundariesForm.tblBoundaries.getCellRect(selectedRow-1, HAND_ICON_COLUMN, true));
              }else{
                  adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                  adjustPeriodBoundariesForm.tblBoundaries.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                  adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,0);
                  periodBoundaryEditor.txtDateComponent.requestFocusInWindow();
                  //adjustPeriodBoundariesForm.tblBoundaries.getEditorComponent().requestFocusInWindow();
              }
          }else{
              if(adjustPeriodBoundariesForm.tblBoundaries.getRowCount()>0){
                  adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                  adjustPeriodBoundariesForm.tblBoundaries.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                  periodBoundaryEditor.txtDateComponent.requestFocusInWindow();
                  adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
                  adjustPeriodBoundariesForm.tblBoundaries.getCellRect(0, START_DATE_COLUMN, true));
              }
          }
    }
    
    /** Check whether any periods has to be deleted from base window. If yes then keep
     * the beans to be deleted in a separate vector.
     * @throws CoeusException
     * @return
     */    
    private boolean deletePeriods() throws CoeusException {
        /**
         *Check whether any periods has to be deleted from base window. If yes then keep 
         *the beans to be deleted in a separate vector
         */ 
        CoeusVector vecOldPeriodDetails = queryEngine.executeQuery(key, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        int oldPeriodSize = vecOldPeriodDetails.size();
        int newPeriodSize = vecPeriodDetails.size();
        int budgetPeriod = 0;
        Equals periodEquals;
        CoeusVector cvfilteredLIDetails;
        
        if (oldPeriodSize > newPeriodSize) {
            for(int index = oldPeriodSize -1; index >= newPeriodSize; index--) {
                
                budgetPeriodBean = (BudgetPeriodBean) vecOldPeriodDetails.get(index);
                
                // Check whether the last period has line items. If it has show the message.
                budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                periodEquals = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                cvfilteredLIDetails = queryEngine.getActiveData(key, BudgetDetailBean.class, periodEquals);
                if(cvfilteredLIDetails.size() > 0){
                    CoeusOptionPane.showInfoDialog("Period " +budgetPeriod +" has detail line items. Cannot delete period "+budgetPeriod + " ");
                    return false;
                }
            
                budgetPeriodBean.setAcType(TypeConstants.DELETE_RECORD);
                vecDeletedPeriods.add(budgetPeriodBean);
            }
        }
        
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        //insert, update the records from the base window
        CoeusVector cvBudgetPersons = queryEngine.executeQuery(key, BudgetPersonsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        //Set the max rowID
        setMaxRowID();
        if (oldPeriodSize > newPeriodSize) {
            for(int index = oldPeriodSize -1; index >= newPeriodSize; index--) {
                budgetPeriodBean = (BudgetPeriodBean) vecOldPeriodDetails.get(index);
                if(budgetPeriodBean.getBudgetPeriod() > DEFAULT_PERIODS){
                    continue;
                }
                if (cvBudgetPersons != null && cvBudgetPersons.size() > 0) {
                    int size = cvBudgetPersons.size();
                    int personIndex = 0;
                    for(personIndex = 0; personIndex < size; personIndex++) {
                        BudgetPersonsBean persBean = (BudgetPersonsBean) cvBudgetPersons.get(personIndex);
                        // Check whether the last period has line items. If it has show the message.
                        budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                        //to remove the period base salary of the person
                        persBean = updatePersonPeriodBaseSalary(budgetPeriod, persBean);
                        
                        if(persBean.getAcType()!=null){
                            if (persBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                                //First delete the existing person and then insert the same. This is
                                //required since primary keys can be modified
                                persBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(key, persBean);
                                
                                persBean.setAcType(TypeConstants.INSERT_RECORD);
                                persBean.setRowId(rowID++);
                                queryEngine.insert(key, persBean);
                            }
                        }
                        
                    }
                }
            }
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        return true;
            
    }
    
    /** Default will create the periods based on Budget start date and budget end
     * date.It spanns the start date and end date by one year and break up of year
     * month and days are generated
     */    
    private void performDefaultAction(){
        periodBoundaryEditor.stopCellEditing();
        //CoeusVector vecDefaultDetails;
        Calendar calStart, calEnd, calPeriodStart, calPeriodEnd;
        int startYear, endYear;
        
        calStart = Calendar.getInstance();
        calStart.setTime(budgetInfoBean.getStartDate());
        
        calEnd = Calendar.getInstance();
        calEnd.setTime(budgetInfoBean.getEndDate());
        
        startYear = calStart.get(Calendar.YEAR);
        endYear = calEnd.get(Calendar.YEAR);
        //Remove All exixting budget periods before breaking up.
        vecPeriodDetails.removeAllElements();
        
        if(startYear < endYear) {
            //Period spans more thrn a year. Break up required.
            calPeriodStart = calStart;
            calPeriodEnd = Calendar.getInstance();
            int budgetPeriod = 0;
            while(true) {
                budgetPeriod = budgetPeriod + 1;
                BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
                budgetPeriodBean.setBudgetPeriod(budgetPeriod);
                budgetPeriodBean.setAw_BudgetPeriod(budgetPeriod);
                budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                modified=true;
                budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                budgetPeriodBean.setStartDate(new java.sql.Date(calPeriodStart.getTimeInMillis()));
                
                
                calPeriodStart.add(Calendar.YEAR, 1);
                calPeriodStart.add(Calendar.DATE, -1);
                calPeriodEnd.setTimeInMillis(calPeriodStart.getTimeInMillis());
                calPeriodStart.add(Calendar.DATE, 1);
                
                if(calPeriodEnd.after(calEnd) || calPeriodEnd.equals(calEnd)) {
                    budgetPeriodBean.setEndDate(budgetInfoBean.getEndDate());
                    budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                     modified=true;
                    vecPeriodDetails.add(budgetPeriodBean);
                    break;
                }
                budgetPeriodBean.setEndDate(new java.sql.Date(calPeriodEnd.getTimeInMillis()));
                budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                modified=true;
                vecPeriodDetails.add(budgetPeriodBean);
            }
        }else{
            //Generate 1st Period.
            BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
            budgetPeriodBean.setBudgetPeriod(1);
            budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
            modified=true;
            budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            budgetPeriodBean.setStartDate(budgetInfoBean.getStartDate());
            budgetPeriodBean.setEndDate(budgetInfoBean.getEndDate());
            vecPeriodDetails.add(budgetPeriodBean);
        }//End Else
        periodBoundaryTableModel.fireTableDataChanged();
        adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
        adjustPeriodBoundariesForm.tblBoundaries.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,START_DATE_COLUMN);
        adjustPeriodBoundariesForm.tblBoundaries.getEditorComponent().requestFocusInWindow();
    }//End performDefaultAction
    
    
    /** Clicking on ok button will validates and saves the data to the base window */    
     private void performOkAction(){
        try{
            periodBoundaryEditor.stopCellEditing();
            if(modified){
                if(validate()){
                    if (deletePeriods()) {
                        adjustBudgetDates(); 
                        // calculate after changing the budget periods
                        calculate(key,-1);
                        setSaveRequired(true);
                        dlgPeriods.setVisible(false);
                        setPeriodAdjusted(true);
                    }
                  }
            }else{
                dlgPeriods.setVisible(false);
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    
     /** */     
    private void performCancelAction(){
         periodBoundaryEditor.stopCellEditing();
        if(modified){
            confirmClosing();
        }else{
            dlgPeriods.setVisible(false);
        }
    }// end of performCancelAction() method
    
    /** Confirm before closing the AdjustBoundary dialog box */    
    private void confirmClosing(){
        
        try{
            periodBoundaryEditor.stopCellEditing();
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                    if(validate()){
                        if(deletePeriods()){
                            adjustBudgetDates();
                            setSaveRequired(true);
                            setPeriodAdjusted(true);
                            dlgPeriods.setVisible(false);
                        }
                    }
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setPeriodAdjusted(false);
                dlgPeriods.dispose();
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    
    /** Adds a new empty period.The Mode is INSERT. */    
    private void performAddAction(){
        
        periodBoundaryEditor.stopCellEditing();
        BudgetPeriodBean newBudgetPeriodBean = new BudgetPeriodBean();
        newBudgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        newBudgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        newBudgetPeriodBean.setBudgetPeriod(vecPeriodDetails.size() +1);
        newBudgetPeriodBean.setAw_BudgetPeriod(vecPeriodDetails.size() + 1);
        newBudgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
        modified=true;
        vecPeriodDetails.add(newBudgetPeriodBean);
        periodBoundaryTableModel.fireTableRowsInserted(periodBoundaryTableModel.getRowCount()+1,
        periodBoundaryTableModel.getRowCount()+1);
              
        int lastRow = adjustPeriodBoundariesForm.tblBoundaries.getRowCount()-1;
        if(lastRow >= 0){
            adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(lastRow,lastRow);
            adjustPeriodBoundariesForm.tblBoundaries.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
            
            adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
            adjustPeriodBoundariesForm.tblBoundaries.getCellRect(lastRow, HAND_ICON_COLUMN, true));
            
        }
        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(lastRow,START_DATE_COLUMN);
        adjustPeriodBoundariesForm.tblBoundaries.getEditorComponent().requestFocusInWindow();
    }
    
    /** This method will specifies the  inserting of a new period as row+1 */    
    private void performInsertAction(){
        int row = adjustPeriodBoundariesForm.tblBoundaries.getSelectedRow();
        if(row!=-1){ 
            periodBoundaryEditor.stopCellEditing();
            BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
            budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            budgetPeriodBean.setBudgetPeriod(row+1);
            budgetPeriodBean.setAw_BudgetPeriod(row+1);
            budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
            modified=true;
            vecPeriodDetails.add(row, budgetPeriodBean);
            periodBoundaryTableModel.fireTableRowsInserted(row,row);
            adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(row,row);
            adjustPeriodBoundariesForm.tblBoundaries.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
            adjustPeriodBoundariesForm.tblBoundaries.editCellAt(row,START_DATE_COLUMN);
            
            adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
            adjustPeriodBoundariesForm.tblBoundaries.getCellRect(row, HAND_ICON_COLUMN, true));
            adjustPeriodBoundariesForm.tblBoundaries.getEditorComponent().requestFocusInWindow();
            
            for(int index = row + 1; index < vecPeriodDetails.size(); index++) {
                budgetPeriodBean = (BudgetPeriodBean)vecPeriodDetails.get(index);
                budgetPeriodBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod() + 1);
                budgetPeriodBean.setAw_BudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                modified=true;
            }
            periodBoundaryTableModel.fireTableCellUpdated(row + 1, vecPeriodDetails.size());
        }else{
                performAddAction();
             }
    }
    
    /** displays the form where the details are got from base window */    
    public void display() {
        setPeriodAdjusted(false);
        if(adjustPeriodBoundariesForm.tblBoundaries.getRowCount() >0){
            adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
            adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
            adjustPeriodBoundariesForm.tblBoundaries.getCellRect(0, 0, true));
        }
        //periodBoundaryEditor.stopCellEditing();
        //commented and modified for Case#2341 - Recalculate Budget if Project dates change
        //modified=false;
        modified = saveNeeded;
        dlgPeriods.setVisible(true);
    }
    
    /** An overridden method of the Controller */    
    public void formatFields() {
    }
    
    /** returns the AdjustPeriodBoundaryForm
     * @return
     */    
    public java.awt.Component getControlledUI() {
        return adjustPeriodBoundariesForm;
    }
    
    /** An overidden method of the SuperClass i.e., controller
     * @return
     */    
    public Object getFormData() {
        return adjustPeriodBoundariesForm;
    }
    
    /** An overridden method of the controller */    
    public void saveFormData() {
    }

    
    /** returns true if all the validations of the form is true. Specifies all the form
     * validations, Date validations and period validations
     * @return
     */    
    public boolean validate(){
        try{
           
            periodBoundaryEditor.stopCellEditing();
            BudgetPeriodBean budgetPeriodBean,nextBudgetPeriodBean;
            
            Date txtStartDate = dtFormat.parse(dtUtils.restoreDate(
            adjustPeriodBoundariesForm.txtBudgetStartDate.getText(),DATE_SEPARATERS));
            
            Date txtEndDate = dtFormat.parse(dtUtils.restoreDate(
            adjustPeriodBoundariesForm.txtBudgetEndDate.getText(),DATE_SEPARATERS));
            
            
             // Atleast one period has to be there in the budget. If not show the message
            if(vecPeriodDetails.size()==0){
                 mesg = coeusMessageResources.parseMessageKey(BUDGET_SHOULD_HAVE_ONE_PERIOD);
                CoeusOptionPane.showErrorDialog(mesg);
                return false;
            }
            
            if(periodBoundaryTableModel.getValueAt(0, START_DATE_COLUMN)==null){
                        mesg = coeusMessageResources.parseMessageKey(START_DATE_MANDATORY);
                        CoeusOptionPane.showErrorDialog(mesg);
                        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,START_DATE_COLUMN);
                        periodBoundaryEditor.txtDateComponent.requestFocus();
                        periodBoundaryEditor.txtDateComponent.setCaretPosition(0);
                        adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                        return false;
            }else if(periodBoundaryTableModel.getValueAt(0, END_DATE_COLUMN)==null){
                        mesg = coeusMessageResources.parseMessageKey(END_DATE_MANDATORY);
                        CoeusOptionPane.showErrorDialog(mesg);
                        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,END_DATE_COLUMN);
                        periodBoundaryEditor.txtDateComponent.requestFocus();
                        adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                        return false;                        
            }
            Date tempStDate = new Date(((java.sql.Date)periodBoundaryTableModel.getValueAt(0, START_DATE_COLUMN)).getTime());
            Date tempEnDate = new Date(((java.sql.Date)periodBoundaryTableModel.getValueAt(0, END_DATE_COLUMN)).getTime());
            if(tempStDate.before(txtStartDate)){
                mesg = coeusMessageResources.parseMessageKey(
                START_DATE_NOT_PRIOR_BUDGET_START_DATE);
                CoeusOptionPane.showErrorDialog(mesg);
                adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,START_DATE_COLUMN);
                periodBoundaryEditor.txtDateComponent.requestFocus();
                periodBoundaryEditor.txtDateComponent.setCaretPosition(0);
                adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                return false;
            }else if(tempEnDate.after(txtEndDate)){
                mesg = coeusMessageResources.parseMessageKey(
                END_DATE_NOT_LATER_THAN_BUDGET);
                CoeusOptionPane.showErrorDialog(mesg);
                adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,END_DATE_COLUMN);
                periodBoundaryEditor.txtDateComponent.requestFocus();
                periodBoundaryEditor.txtDateComponent.setCaretPosition(0);
                adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                return false;
            }else if(tempStDate.after(tempEnDate) || tempEnDate.before(tempStDate)){
                mesg =coeusMessageResources.parseMessageKey(
                END_DATE_LATER_THAN_START_DATE);
                CoeusOptionPane.showErrorDialog(mesg);
                adjustPeriodBoundariesForm.tblBoundaries.editCellAt(0,END_DATE_COLUMN);
                periodBoundaryEditor.txtDateComponent.requestFocus();
                periodBoundaryEditor.txtDateComponent.setCaretPosition(0);
                adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(0,0);
                return false;
            }
            //Validating Period Dates
            for(int index = 0; index < vecPeriodDetails.size() - 1; index++) {
                budgetPeriodBean = (BudgetPeriodBean)vecPeriodDetails.get(index);
                nextBudgetPeriodBean = (BudgetPeriodBean)vecPeriodDetails.get(index + 1);
                //Check if start date is null/ Empty
                if(budgetPeriodBean.getStartDate()==null || nextBudgetPeriodBean.getStartDate()==null){
                        mesg = coeusMessageResources.parseMessageKey(
                        START_DATE_MANDATORY);
                        CoeusOptionPane.showErrorDialog(mesg);
                        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(index+1,START_DATE_COLUMN);
                        periodBoundaryEditor.txtDateComponent.requestFocus();
                        adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(index + 1,index + 1);
                        adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
                        adjustPeriodBoundariesForm.tblBoundaries.getCellRect(index+1, START_DATE_COLUMN, true));
                        return false;
                }
                //Check if End date is null / Empty
                else if(budgetPeriodBean.getEndDate() == null || nextBudgetPeriodBean.getEndDate()==null){
                        mesg = coeusMessageResources.parseMessageKey(
                        END_DATE_MANDATORY);
                        CoeusOptionPane.showErrorDialog(mesg);
                        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(index+1,END_DATE_COLUMN);
                        periodBoundaryEditor.txtDateComponent.requestFocus();
                        adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(index+1,index+1);
                        adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
                        adjustPeriodBoundariesForm.tblBoundaries.getCellRect(index+1, END_DATE_COLUMN, true));
                        return false;
                }
                //Check if next period start date < this period end date
                else if(nextBudgetPeriodBean.getEndDate().before(nextBudgetPeriodBean.getStartDate())
                || budgetPeriodBean.getEndDate().before(budgetPeriodBean.getStartDate())){
                    mesg =coeusMessageResources.parseMessageKey(
                        END_DATE_LATER_THAN_START_DATE);
                        CoeusOptionPane.showErrorDialog(mesg);
                        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(index+1,END_DATE_COLUMN);
                        periodBoundaryEditor.txtDateComponent.requestFocus();
                        adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(index+1,index+1);
                        adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
                        adjustPeriodBoundariesForm.tblBoundaries.getCellRect(index+1, END_DATE_COLUMN, true));
                        return false;
                }
                //check if period start date  > period end date
                //Check if next period start date == this period end date
                else if((nextBudgetPeriodBean.getStartDate().compareTo(budgetPeriodBean.getEndDate()) <= 0)){
                        CoeusOptionPane.showErrorDialog("Start Date of Period "+ nextBudgetPeriodBean.getBudgetPeriod() + " should be later than the end date of period " + budgetPeriodBean.getBudgetPeriod() + " ");
                        adjustPeriodBoundariesForm.tblBoundaries.editCellAt(index+1,START_DATE_COLUMN);
                        periodBoundaryEditor.txtDateComponent.requestFocus();
                        adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(index+1,index+1);
                        adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
                        adjustPeriodBoundariesForm.tblBoundaries.getCellRect(index+1, START_DATE_COLUMN, true));
                        return false;
                }
                //Check for last period end date > budget end date
                if(index == vecPeriodDetails.size() - 2 &&
                nextBudgetPeriodBean.getEndDate().after(txtEndDate)) {
                    mesg = coeusMessageResources.parseMessageKey(
                    END_DATE_NOT_LATER_THAN_BUDGET);
                    CoeusOptionPane.showErrorDialog(mesg);
                    adjustPeriodBoundariesForm.tblBoundaries.editCellAt(vecPeriodDetails.size()-1,END_DATE_COLUMN);
                    periodBoundaryEditor.txtDateComponent.requestFocus();
                    periodBoundaryEditor.txtDateComponent.setCaretPosition(0);
                    adjustPeriodBoundariesForm.tblBoundaries.setRowSelectionInterval(vecPeriodDetails.size()-1, vecPeriodDetails.size()-1);
                    adjustPeriodBoundariesForm.tblBoundaries.scrollRectToVisible(
                    adjustPeriodBoundariesForm.tblBoundaries.getCellRect(vecPeriodDetails.size()-1, vecPeriodDetails.size()-1, true));
                    return false;
                }
            }
        }catch(ParseException parseException){
            parseException.printStackTrace();
        }
        return true;
    }
    
    // case #1625 Start 1 
    private void updateProjectIncome(CoeusVector vecPeriodDetails) throws CoeusException{
        Equals eqBudgetPeriod = null;;
        String acType=EMPTY_STRING;
        CoeusVector cvProjectIncodme = new CoeusVector();
        
        int size = 0;
        int incomeSize = 0;        
        if (vecPeriodDetails != null && vecPeriodDetails.size() > 0) {
            size = vecPeriodDetails.size();
            BudgetPeriodBean budgetPeriodBean;
            for (int index = 0; index < size; index++) {
                budgetPeriodBean = (BudgetPeriodBean) vecPeriodDetails.get(index);
                acType = budgetPeriodBean.getAcType();
                // starts income details
                eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                cvProjectIncodme = queryEngine.getActiveData(key,ProjectIncomeBean.class,eqBudgetPeriod);
                if (cvProjectIncodme != null && cvProjectIncodme.size() > 0) {
                    incomeSize = cvProjectIncodme.size();
                    ProjectIncomeBean projectIncomeBean;
                    for (int incomeIndex = 0; incomeIndex  < incomeSize; incomeIndex ++) {
                        projectIncomeBean = (ProjectIncomeBean) cvProjectIncodme.get(incomeIndex);
                        
                        if (acType == null || acType.equals(TypeConstants.UPDATE_RECORD)) {
                            queryEngine.update(key,projectIncomeBean);
                        }else if (acType.equals(TypeConstants.INSERT_RECORD)) {
                            queryEngine.insert(key,projectIncomeBean);
                        } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(key,projectIncomeBean);
                        }
                    }
                }
            }
        }
    }
    //  case #1625 End 1   
    //Case #1626 Start 1
    //Added for modular budget enhancement delete period start 3
     private void updateBudgetModular(CoeusVector vecPeriodDetails, int selRow) throws CoeusException{
         CoeusVector cvBudgetModular = new CoeusVector();
         if(vecPeriodDetails != null && vecPeriodDetails.size() >0 ){
             BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecPeriodDetails.get(selRow);
             
             Equals periodEquals = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
             cvBudgetModular = queryEngine.getActiveData(budgetInfoBean.getProposalNumber()
             + budgetInfoBean.getVersionNumber(), BudgetModularBean.class, periodEquals);
             if(cvBudgetModular != null && cvBudgetModular.size()>0){
                 for(int index = 0 ; index < cvBudgetModular.size(); index ++){
                     BudgetModularBean deleteModularBean =
                     (BudgetModularBean)cvBudgetModular.get(index);
                     deleteModularBean.setAcType(TypeConstants.DELETE_RECORD);
                     queryEngine.delete(budgetInfoBean.getProposalNumber()
                     + budgetInfoBean.getVersionNumber(), deleteModularBean);
                 }
             }
             cvBudgetModular = queryEngine.getActiveData(budgetInfoBean.getProposalNumber()
             + budgetInfoBean.getVersionNumber(), BudgetModularIDCBean.class, periodEquals);
             if(cvBudgetModular != null && cvBudgetModular.size()>0){
                 for(int index = 0 ; index < cvBudgetModular.size(); index ++){
                     BudgetModularIDCBean deleteModularBean =
                     (BudgetModularIDCBean)cvBudgetModular.get(index);
                     deleteModularBean.setAcType(TypeConstants.DELETE_RECORD);
                     queryEngine.delete(budgetInfoBean.getProposalNumber()
                     + budgetInfoBean.getVersionNumber(), deleteModularBean);
                 }
             }
         }
         cvBudgetModular = null;
     }
    private void updateBudgetModularOther(CoeusVector vecPeriodDetails) throws CoeusException{
        Equals eqBudgetPeriod = null;;
        String acType=EMPTY_STRING;
        CoeusVector cvBudgetModular = new CoeusVector();
        
        int size = 0;
        int modularSize = 0;        
        if (vecPeriodDetails != null && vecPeriodDetails.size() > 0) {
            size = vecPeriodDetails.size();
            BudgetPeriodBean budgetPeriodBean;
            for (int index = 0; index < size; index++) {
                budgetPeriodBean = (BudgetPeriodBean) vecPeriodDetails.get(index);
                acType = budgetPeriodBean.getAcType();
                // starts Modular budget details
                eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                cvBudgetModular = queryEngine.getActiveData(key,BudgetModularBean.class,eqBudgetPeriod);
                if (cvBudgetModular != null && cvBudgetModular.size() > 0) {
                    modularSize = cvBudgetModular.size();
                    BudgetModularBean budgetModularBean;
                    for (int modularIndex = 0; modularIndex  < modularSize; modularIndex ++) {
                        budgetModularBean = (BudgetModularBean) cvBudgetModular.get(modularIndex);
                        if (acType == null || acType.equals(TypeConstants.UPDATE_RECORD)) {
                            queryEngine.update(key,budgetModularBean);
                        }else if (acType.equals(TypeConstants.INSERT_RECORD)) {
                            queryEngine.insert(key,budgetModularBean);
                        } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(key,budgetModularBean);
                        }
                    }
                }
                //For Budget Modular Enhancement case #2087 start 1
                cvBudgetModular = queryEngine.getActiveData(key,BudgetModularIDCBean.class,eqBudgetPeriod);
                if (cvBudgetModular != null && cvBudgetModular.size() > 0) {
                    modularSize = cvBudgetModular.size();
                    BudgetModularIDCBean budgetModularIDCBean;
                    for (int modularIndex = 0; modularIndex  < modularSize; modularIndex ++) {
                        budgetModularIDCBean = (BudgetModularIDCBean) cvBudgetModular.get(modularIndex);
                        if (acType == null || acType.equals(TypeConstants.UPDATE_RECORD)) {
                            queryEngine.update(key,budgetModularIDCBean);
                        }else if (acType.equals(TypeConstants.INSERT_RECORD)) {
                            queryEngine.insert(key,budgetModularIDCBean);
                            
                        } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(key,budgetModularIDCBean);
                           
                        }
                    }
                }
                //For Budget Modular Enhancement case #2087 end 1
            }
            
        }
    }
    //Added for modular budget enhancement delete period end 3
    //Case #1626 End 1
     
    
    /** Adjust the dates in BudgetPeriod, Budget Line Ite, Personnel Budget.
     *If the old period changed to new Period, adjust the dates in Line item dates,
     *Personnel Line Iten dates.Delete, Update or insert the dates as dates are
     *changed in the adjust period.
     */
    public void adjustBudgetDates(){
        try{
            CoeusVector cvLineItemDetails = new CoeusVector();
            CoeusVector cvPersonnelDetails = new CoeusVector();
            CoeusVector cvTotalLineItemDetails = new CoeusVector();
            CoeusVector cvTotalPersonnelDetails = new CoeusVector();
            
            
            CoeusVector cvOldPeriodDetails;
            //Modified for Case#2341 - Recalculate Budget if Project dates change - starts
            if(saveNeeded){
                cvOldPeriodDetails = getCvOldPeriodDetails();
            } else {
                cvOldPeriodDetails = queryEngine.executeQuery(key, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            }
            //Modified for Case#2341 - Recalculate Budget if Project dates change - ends
            Calendar calendar = Calendar.getInstance();
            
            long periodAndLineItemStDateDifference;
            long oldLiEndDateDifference;
            
            long liAndPersonnelStDateDifference;
            long personnelStDateEndDateDiff;
            
            int periodSize = vecPeriodDetails.size();
            BudgetPeriodBean newBudgetPeriodBean;
            BudgetPeriodBean oldBudgetPeriodBean;
            BudgetDetailBean budgetDetailBean;
            BudgetPersonnelDetailsBean personnelDetailsBean;
            
            Equals eqBudgetPeriod;
            Equals eqLineItemNo;
            int budgetPeriod = 0;
            Date oldPeriodStDate, oldPeriodEndDate, newPeriodStDate, newPeriodEndDate;
            Date oldLiStDate, oldLiEndDate, newLiStDate=null, newLiEndDate=null;
            Date oldPersonnelStDate, oldPersonnelEndDate, newPersonnelStDate=null, newPersonnelEndDate=null;
            for (int periodIndex = 0; periodIndex < periodSize; periodIndex++) {
                
                //Get the new budget period
                newBudgetPeriodBean = (BudgetPeriodBean) vecPeriodDetails.get(periodIndex);
                newPeriodStDate = newBudgetPeriodBean.getStartDate();
                newPeriodEndDate = newBudgetPeriodBean.getEndDate();
                
                budgetPeriod = newBudgetPeriodBean.getBudgetPeriod();
                eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
                
                //Check whether the period has line items
                cvLineItemDetails = queryEngine.getActiveData(key, BudgetDetailBean.class, eqBudgetPeriod);
                
                if (cvLineItemDetails != null && cvLineItemDetails.size() > 0) {
                    //Get the old budget period
                    oldBudgetPeriodBean = (BudgetPeriodBean) cvOldPeriodDetails.get(periodIndex);
                    oldPeriodStDate = oldBudgetPeriodBean.getStartDate();
                    oldPeriodEndDate = oldBudgetPeriodBean.getEndDate();
                    int liSize = cvLineItemDetails.size();
                    
                    for (int liIndex = 0; liIndex < liSize; liIndex++) {
                        budgetDetailBean = (BudgetDetailBean) cvLineItemDetails.get(liIndex);
                        oldLiStDate = budgetDetailBean.getLineItemStartDate();
                        oldLiEndDate = budgetDetailBean.getLineItemEndDate();
                        
                        /**
                         *If the Line item Start Date equals old Period start date and
                         *if line item End Date equals old Period End Date then set the
                         *new line item Start Date to new Period Start Date and
                         *new line item End Date to new Period End Date
                         */
                        if (oldPeriodStDate.compareTo(oldLiStDate) == 0 &&
                            oldPeriodEndDate.compareTo(oldLiEndDate) == 0) {
                            newLiStDate = newPeriodStDate;
                            newLiEndDate = newPeriodEndDate;
                        } else {
                            /** get the Period and Line item start dates difference and 
                             *add this to the new Period start date and set the new start date
                             *to the new line item start date
                             */
                            
                            //Set Line Item Start Date
                            periodAndLineItemStDateDifference = (oldLiStDate.getTime() - oldPeriodStDate.getTime()) / 86400000;
                            
                            calendar.setTime(newPeriodStDate);
                            calendar.add(Calendar.DATE, (int)periodAndLineItemStDateDifference);
                            newLiStDate = calendar.getTime();
                            
                            if (newLiStDate.compareTo(newPeriodEndDate) > 0) {
                                newLiStDate = newPeriodStDate;
                            }
                            
                            //Set Line Item End Date
                            oldLiEndDateDifference = (oldLiEndDate.getTime() - oldLiStDate.getTime()) / 86400000;
                            calendar.setTime(newLiStDate);
                            calendar.add(Calendar.DATE, (int) oldLiEndDateDifference);
                            newLiEndDate = calendar.getTime();
                            
                            if (newLiEndDate.compareTo(newPeriodEndDate) > 0) {
                                newLiEndDate = newPeriodEndDate;
                            }
                            
                        }
                        
                        //Set the line item with new Start Date & End Dates
                        budgetDetailBean.setLineItemStartDate(new java.sql.Date(newLiStDate.getTime()));
                        budgetDetailBean.setLineItemEndDate(new java.sql.Date(newLiEndDate.getTime()));
                        
                        //Check whether the line item has personnel details
                        //Modified for Case#2341 - Recalculate Budget if Project dates change - starts
                        eqLineItemNo = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
                        And andPeriodAndLineItem = new And(eqBudgetPeriod, eqLineItemNo);
//                        cvPersonnelDetails = queryEngine.getActiveData(key, BudgetPersonnelDetailsBean.class, eqLineItemNo);
                        cvPersonnelDetails = queryEngine.getActiveData(key, BudgetPersonnelDetailsBean.class, andPeriodAndLineItem);
                        //Modified for Case#2341 - Recalculate Budget if Project dates change - ends
                        if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                            int personnelSize = cvPersonnelDetails.size();
                            
                            for (int personnelIndex = 0; personnelIndex < personnelSize; personnelIndex++) {
                                personnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelDetails.get(personnelIndex);
                                oldPersonnelStDate = personnelDetailsBean.getStartDate();
                                oldPersonnelEndDate = personnelDetailsBean.getEndDate();
                                
                                /**
                                 *If the Personnel Line item Start Date equals old Line Item start date and
                                 *if Personnel line item End Date equals old Line Item End Date then set the
                                 *new Personnel line item Start Date to new Line Item Start Date and
                                 *new Personnel line item End Date to new Line Item End Date
                                 */
                                if (oldLiStDate.compareTo(oldPersonnelStDate) == 0 &&
                                oldLiEndDate.compareTo(oldPersonnelEndDate) == 0) {
                                    newPersonnelStDate = newLiStDate;
                                    newPersonnelEndDate = newLiEndDate;
                                } else {
                                    //Set Personnel Line Item Start Date
                                    liAndPersonnelStDateDifference = (oldPersonnelStDate.getTime() - oldLiStDate.getTime()) / 86400000;

                                    calendar.setTime(newLiStDate);
                                    calendar.add(Calendar.DATE, (int)liAndPersonnelStDateDifference);
                                    newPersonnelStDate = calendar.getTime();

                                    if (newPersonnelStDate.compareTo(newLiEndDate) > 0) {
                                        newPersonnelStDate = newLiStDate;
                                    }

                                    //Set Personnel Line Item End Date
                                    personnelStDateEndDateDiff = (oldPersonnelEndDate.getTime() - oldPersonnelStDate.getTime()) / 86400000;
                                    calendar.setTime(newPersonnelStDate);
                                    calendar.add(Calendar.DATE, (int) personnelStDateEndDateDiff);
                                    newPersonnelEndDate = calendar.getTime();

                                    if (newPersonnelEndDate.compareTo(newLiEndDate) > 0) {
                                        newPersonnelEndDate = newLiEndDate;
                                    }
                                }
                                
                                //Set the Personnel line item with new Start Date & End Dates
                                personnelDetailsBean.setStartDate(new java.sql.Date(newPersonnelStDate.getTime()));
                                personnelDetailsBean.setEndDate(new java.sql.Date(newPersonnelEndDate.getTime()));
                                
                            }
                            
                            if (cvPersonnelDetails.size() > 0) {
                                cvTotalPersonnelDetails.addAll(cvPersonnelDetails);
                            }
                        }
                    }
                    if (cvLineItemDetails.size() > 0) {
                        cvTotalLineItemDetails.addAll(cvLineItemDetails);
                    }
                }
            }
           updateBudgetDates(cvTotalLineItemDetails,cvTotalPersonnelDetails);
            
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    /** Update the respective dates, in the respective beans. Update BudgetPeriodBean,
     *budgetDetailBean, and BudgetPersonnelDetailsBean. Update these beans to the base widndow
     *through query engine
     */
    private void updateBudgetDates(CoeusVector cvLineItemDetails, CoeusVector cvPersonnelDetails) 
        throws CoeusException {
        
        //Update Budget Period Details to the base window
        int size = 0;
        int index = 0;
        String acType=EMPTY_STRING;
        //Combine deleted vector with updated vector
        vecPeriodDetails.addAll(vecDeletedPeriods);
        // Start case #1625 2
        updateProjectIncome(vecPeriodDetails);
        // End Start case #1625 2 
        //Case #1626 Start 2
        //updateBudgetModular(vecPeriodDetails);
        //Added for modular budget enhancement delete period start 4
         updateBudgetModular(cvPeriodModBud, selRowforModBudget);
         updateBudgetModularOther(vecPeriodDetails);
         //Added for modular budget enhancement delete period end 4
         
         // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start
         // setting the deleted period data to vector.
         setDeletedPeriods(vecDeletedPeriods);
         // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -End
         
        //Case #1626 End 2
        if (vecPeriodDetails != null && vecPeriodDetails.size() > 0) {
            size = vecPeriodDetails.size();
            BudgetPeriodBean budgetPeriodBean;
            for (index = 0; index < size; index++) {
                budgetPeriodBean = (BudgetPeriodBean) vecPeriodDetails.get(index);
                acType = budgetPeriodBean.getAcType();
                
                if (acType == null || acType.equals(TypeConstants.UPDATE_RECORD)) {
                    queryEngine.update(key,budgetPeriodBean);
                }else if (acType.equals(TypeConstants.INSERT_RECORD)) {
                    queryEngine.insert(key,budgetPeriodBean);
                } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                    queryEngine.delete(key,budgetPeriodBean);
                }
            }
        }
        
        
        
        
       
        
        // Update BudgetDetailBean to the base widndow. update the Line item start date and end date
        if(cvLineItemDetails!=null || cvLineItemDetails.size() > 0){
            for(int liIndex = 0 ; liIndex < cvLineItemDetails.size(); liIndex++){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)cvLineItemDetails.get(liIndex);
                acType = budgetDetailBean.getAcType();
                if(acType==null || acType.equals(TypeConstants.UPDATE_RECORD)){
                    queryEngine.update(key, budgetDetailBean);
                } else if(acType==null || acType.equals(TypeConstants.INSERT_RECORD)){
                    queryEngine.insert(key, budgetDetailBean);
                }
            }
        }
        /* Update BudgetPersonnelDetailsBean to the base window. 
         update the Personnel Line item start date and Personnel Line item end date*/
        if(cvPersonnelDetails!=null || cvPersonnelDetails.size() > 0){
            for(int pIndex = 0 ; pIndex < cvPersonnelDetails.size(); pIndex++){
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = 
                        (BudgetPersonnelDetailsBean)cvPersonnelDetails.get(pIndex);
                acType = budgetPersonnelDetailsBean.getAcType();
                if(acType==null || acType.equals(TypeConstants.UPDATE_RECORD)){
                    queryEngine.update(key, budgetPersonnelDetailsBean);
                }else if(acType==null || acType.equals(TypeConstants.INSERT_RECORD)){
                    queryEngine.insert(key, budgetPersonnelDetailsBean);
                }
            }
        }
    }
    
    /** Getter for property periodAdjusted.
     * @return Value of property periodAdjusted.
     *
     */
    public boolean isPeriodAdjusted() {
        return periodAdjusted;
    }
    
    /** Setter for property periodAdjusted.
     * @param periodAdjusted New value of property periodAdjusted.
     *
     */
    public void setPeriodAdjusted(boolean periodAdjusted) {
        this.periodAdjusted = periodAdjusted;
    }
    
    /** An inner class which specifies the model for the Table
     */
    
    private class PeriodBoundaryTableModel extends AbstractTableModel implements TableModel{
        private CoeusVector vecPeriodDetails;
       // private BudgetInfoBean budgetInfoBean = null;
        private String colNames[] = {"","Period","Start Date","End Date"};
        
        private Class colClass[] = {ImageIcon.class, Integer.class, String.class,
        String.class};
        /** Specifies which columns are editable */        
        boolean[] canEdit = new boolean [] {
            false, false, true, true};
            
            PeriodBoundaryTableModel(){
            }
            
            /** Specifies which row or column are editable
             * @param row
             * @param col
             * @return
             */            
            public boolean isCellEditable(int row, int col){
                if((col==PERIOD_COLUMN)|| (col== HAND_ICON_COLUMN)){// || (functionType==TypeConstants.DISPLAY_MODE)){
                    return false;
                }else{
                    return true;
                }
            }
            /** sets the data to the table model
             * @param vecPeriodDetails
             */            
            public void setData(CoeusVector vecPeriodDetails) {
                this.vecPeriodDetails = vecPeriodDetails;
            }
            /** returns the number of columns in the table
             * @return
             */            
            public int getColumnCount() {
                return colNames.length;
            }
            
            public Class getColumnClass(int columnIndex) {
                return colClass [columnIndex];
            }
            
            /** returns the number of rows in the table
             * @return
             */            
            public int getRowCount() {
                if(vecPeriodDetails==null)return 0;
                return vecPeriodDetails.size();
            }
            
            public String getColumnName(int column) {
                return colNames[column];
            }
            
            /** gets the bean data and set it to the table model
             * @param row
             * @param col
             * @return
             */            
            public Object getValueAt(int row, int col) {
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecPeriodDetails.get(row);
                switch(col){
                    case PERIOD_COLUMN:
                        return new Integer(budgetPeriodBean.getBudgetPeriod());
                    case START_DATE_COLUMN:
                        return budgetPeriodBean.getStartDate();
                    case END_DATE_COLUMN:
                        return budgetPeriodBean.getEndDate();
                }
                return EMPTY_STRING;
            }
            
            /** set the new value to the table model
             * @param value
             * @param row
             * @param column
             */            
            public void setValueAt(Object value, int row, int column) {
                if(vecPeriodDetails==null) return;
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecPeriodDetails.get(row);
                Date date = null;
                String strDate=null;
                String message=null;
                switch(column){
                    case START_DATE_COLUMN:
                        try{
                            if (value.toString().trim().length() > 0) {
                                strDate = dtUtils.formatDate(
                                value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                            } else {
                                return;
                            }
                            strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                            if(strDate==null) {
                                throw new CoeusException();
                            }
                            //date = dtFormat.parse(strDate.toString().trim());
                            date = dtFormat.parse(strDate.trim());
                        }catch (ParseException parseException) {
                            parseException.printStackTrace();
                            message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
                             CoeusOptionPane.showErrorDialog(message);
                            return ;
                        }
                        catch (CoeusException coeusException) {
                             message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
                             CoeusOptionPane.showErrorDialog(message);
                            return ;
                        }
                        
                        if(budgetPeriodBean.getStartDate() != null && budgetPeriodBean.getStartDate().equals(date)) break;
                        budgetPeriodBean.setStartDate(new java.sql.Date(date.getTime()));
                        modified=true;
                        break;
                        
                    case END_DATE_COLUMN:
                        try{
                            if (value.toString().trim().length() > 0) {
                                strDate = dtUtils.formatDate(
                                value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                            } else {
                                return;
                            }
                            
                            strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                            if(strDate==null) {
                                throw new CoeusException();
                            }
                            //date = dtFormat.parse(strDate.toString().trim());
                            date = dtFormat.parse(strDate.trim());
                        }catch (ParseException parseException) {
                            parseException.printStackTrace();
                            message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
                             CoeusOptionPane.showErrorDialog(message);
                            return ;
                        }
                        catch (CoeusException coeusException) {
                            message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
                             CoeusOptionPane.showErrorDialog(message);
                            return ;
                        }
                        
                        if(budgetPeriodBean.getEndDate() != null && budgetPeriodBean.getEndDate().equals(date)) break;
                        budgetPeriodBean.setEndDate(new java.sql.Date(date.getTime()));
                        modified = true;
                        break;
                }
                
                try{
                    if (budgetPeriodBean.getAcType() == null) {
                        budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    periodBoundaryTableModel.fireTableRowsUpdated(row,row);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }
    
    
    
    /** This is Iconrendere to display HAND icon for the selected row in the table
     */
    static class IconRenderer  extends DefaultTableCellRenderer {
        
        /** This holds the Image Icon of Hand Icon
         */
        private final ImageIcon HAND_ICON =
        new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND_ICON));
        private final ImageIcon EMPTY_ICON = null;
        /** Default Constructor*/
        IconRenderer() {
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
            setText((String)value);
            setOpaque(false);
            /* if row is selected the place the icon in this cell wherever this
               renderer is used. */
            if( isSelected ){
                setIcon(HAND_ICON);
            }else{
                setIcon(EMPTY_ICON);
            }
            return this;
        }
        
    }//End Icon Rendering inner class
    
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
            
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
    
    /** An iiner class, specifies the editor for the table component
     */
    public class PeriodBoundaryEditor extends  AbstractCellEditor  implements TableCellEditor {
        
        private CoeusTextField txtDateComponent;
        int column;
        PeriodBoundaryEditor(){
            txtDateComponent = new CoeusTextField();
        }
        /** An overridden method of the Abstarct cell editor.returns the table componets
         * @param table
         * @param value
         * @param isSelected
         * @param row
         * @param column
         * @return
         */        
        public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        txtDateComponent.setText(EMPTY_STRING);
                        return txtDateComponent;
                    }
                    txtDateComponent.setText(dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT));
                    return txtDateComponent;
            }
            return txtDateComponent;
        }
        
        /** returns the Cell editor values for a table component
         * @return
         */        
        public Object getCellEditorValue() {
            switch (column) {
                case  START_DATE_COLUMN:
                case  END_DATE_COLUMN:
                    return txtDateComponent.getText();
            }
            return ((JTextField)txtDateComponent).getText();
        }// End of getCellEditorValue() method
    }// End of class PeriodBoundaryEditor
    
    
    
    /** An Iner class to specify the renderer for the table components
     */
    private class PeriodBoundaryRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer{
        private JTextField txtDateComponent;
        private JTextField txtPeriod;
        private int selRow;
        private int selCol;
        
        PeriodBoundaryRenderer(){
            txtDateComponent = new JTextField();
            txtDateComponent.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            adjustPeriodBoundariesForm.tblBoundaries.editCellAt(selRow,selCol);
            setFont(CoeusFontFactory.getNormalFont());
            
            txtPeriod = new JTextField();
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            txtPeriod.setBorder(new BevelBorder(BevelBorder.LOWERED));
        }
        /** specifies the renderer componensts for the table model
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */        
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            selRow = row;
            selCol = column;
            switch (column){
                case PERIOD_COLUMN:
                    if(isSelected){
                        txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtPeriod.setForeground(Color.black);
                    }
                    //                    else {
                    //                        txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                    //                        txtPeriod.setForeground(Color.black);
                    //                    }
                    txtPeriod.setText(value.toString());
                    txtPeriod.setHorizontalAlignment(JTextField.RIGHT);
                    return txtPeriod;
                    
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    if(isSelected){
                        txtDateComponent.setBackground(Color.YELLOW);
                        txtDateComponent.setForeground(Color.black);
                    }
                    else {
                        txtDateComponent.setBackground(Color.white);
                        txtDateComponent.setForeground(Color.black);
                    }
                    if(value == null || value.toString().equals(EMPTY_STRING)){
                        txtDateComponent.setText(EMPTY_STRING);
                        return txtDateComponent;
                    }else{
                        value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                        txtDateComponent.setText(value.toString());
                        txtDateComponent.setHorizontalAlignment(JTextField.LEFT);
                        return txtDateComponent;
                    }
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    
    
     // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
         javax.swing.InputMap im = adjustPeriodBoundariesForm.tblBoundaries.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = adjustPeriodBoundariesForm.tblBoundaries.getActionMap().get(im.get(tab));
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
        adjustPeriodBoundariesForm.tblBoundaries.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = adjustPeriodBoundariesForm.tblBoundaries.getActionMap().get(im.get(shiftTab));
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
                    if (column <= 0) {
                        column = 3;
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
        adjustPeriodBoundariesForm.tblBoundaries.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
    
    
    
    
    private static final String BUDGET_SHOULD_HAVE_ONE_PERIOD = "adjustPeriod_exceptionCode.1451";
    private static final String START_DATE_MANDATORY = "adjustPeriod_exceptionCode.1452";
    private static final String END_DATE_MANDATORY = "adjustPeriod_exceptionCode.1453";
    private static final String END_DATE_LATER_THAN_START_DATE = "adjustPeriod_exceptionCode.1454";
    private static final String END_DATE_NOT_LATER_THAN_BUDGET ="adjustPeriod_exceptionCode.1455";
    private static final String  START_DATE_NOT_PRIOR_BUDGET_START_DATE= "adjustPeriod_exceptionCode.1456";
    private static final String TITLE_WINDOW = "Define Periods";
    private static final String INVALID_START_DATE = "budget_common_exceptionCode.1001";
    private static final String INVALID_END_DATE = "budget_common_exceptionCode.1002";
    private static final String BUDGET_PERIOD = "budgetPeriod";

    /**
     * Getter for property saveNeeded.
     * @return Value of property saveNeeded.
     */
    public boolean isSaveNeeded() {
        return saveNeeded;
    }

    /**
     * Setter for property saveNeeded.
     * @param saveNeeded New value of property saveNeeded.
     */
    public void setSaveNeeded(boolean saveNeeded) {
        this.saveNeeded = saveNeeded;
    }

    /**
     * Getter for property cvOldPeriodDetails.
     * @return Value of property cvOldPeriodDetails.
     */
    public CoeusVector getCvOldPeriodDetails() {
        return cvOldPeriodDetails;
    }

    /**
     * Setter for property cvOldPeriodDetails.
     * @param cvOldPeriodDetails New value of property cvOldPeriodDetails.
     */    
    public void setCvOldPeriodDetails(CoeusVector cvOldPeriodDetails) {
        this.cvOldPeriodDetails = cvOldPeriodDetails;
    }
 // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start
    public CoeusVector getDeletedPeriods() {
        return deletedPeriods;
    }

    public void setDeletedPeriods(CoeusVector deletedPeriods) {
        this.deletedPeriods = deletedPeriods;
    }
 // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -End
    
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     *     
     * To reset the period base salary of the person for the particular period.
     * @param int
     * @param BudgetPersonsBean
     * @return BudgetPersonsBean     
     */
    private BudgetPersonsBean updatePersonPeriodBaseSalary(int budgetPeriod, BudgetPersonsBean persBean){
        double baseSalary = 0.00;
        boolean modified = false;
        switch(budgetPeriod){
            case 1:
                persBean.setBaseSalaryP1(baseSalary);
                modified = true;
                break;
            case 2:
                persBean.setBaseSalaryP2(baseSalary);
                modified = true;
                break;
            case 3:
                persBean.setBaseSalaryP3(baseSalary);
                modified = true;
                break;
            case 4:
                persBean.setBaseSalaryP4(baseSalary);
                modified = true;
                break;
            case 5:
                persBean.setBaseSalaryP5(baseSalary);
                modified = true;
                break;
            case 6:
                persBean.setBaseSalaryP6(baseSalary);
                modified = true;
                break;
            case 7:
                persBean.setBaseSalaryP7(baseSalary);
                modified = true;
                break;
            case 8:
                persBean.setBaseSalaryP8(baseSalary);
                modified = true;
                break;
            case 9:
                persBean.setBaseSalaryP9(baseSalary);
                modified = true;
                break;
            case 10:
                persBean.setBaseSalaryP10(baseSalary);
                modified = true;
                break;
        }
        //check if value is modified
        if(modified){
            persBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        return persBean;
    }
    
    /* This method sets the maximum Row ID from the vector of Budget Persons beans
     *that is present in queryEngine
     */
    private void setMaxRowID() {
        CoeusVector cvBudgetPersons = new CoeusVector();
        BudgetPersonsBean personsBean;
        try {
            cvBudgetPersons = QueryEngine.getInstance().getDetails(key,BudgetPersonsBean.class);
            if (cvBudgetPersons != null && cvBudgetPersons.size() > 0) {
                cvBudgetPersons.sort("rowId", false);
                personsBean = (BudgetPersonsBean) cvBudgetPersons.get(0);
                rowID = personsBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
}

