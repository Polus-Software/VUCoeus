/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * CostSharingController.java
 *
 * Created on May 03, 2004, 10:35 PM
 */

package edu.mit.coeus.instprop.controller;

/**
 *
 * @author  bijosh
 */
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import javax.swing.DefaultListSelectionModel;
import java.awt.event.*;
import java.awt.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.instprop.gui.CostSharingForm;
import edu.mit.coeus.instprop.controller.InstituteProposalController;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;


/**
 * Creates an instance of CostSharingController
 */
public class CostSharingController extends InstituteProposalController implements
ActionListener {
    private static final String EMPTY_STRING = "";
    private static final int PERCENTAGE_COLUMN = 0;
    private static final int TYPE_COLUMN = 1;
    private static final int FISCAL_YEAR_COLUMN = 2;
    private static final int AMOUNT_COLUMN = 3;
    private static final int SOURCE_ACCOUNT_COLUMN = 4;
    private static final int TOTAL_COLUMN=0;
    private static final int TOTAL_AMOUNT_COLUMN=1;
    private static final String SELECT_A_ROW="Please select a Cost Sharing Row";
    private static final String DELETE_CONFIRMATION = "Are you sure you want to delete this Cost Sharing row? ";
    private boolean isCommentPresent=false;
    private CoeusMessageResources coeusMessageResources;
    private CostSharingForm costSharingForm;
    private CostSharingTableModel costSharingTableModel;
    private InstituteProposalCommentsBean commentsBean;
    private boolean typeComboPopulated = false;
    private boolean modified=false;
    private char functionType;
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private QueryEngine queryEngine;
    private CoeusVector cvTableData;
    private CoeusVector cvParameters;
    private CoeusVector cvDeletedData;
    private CoeusVector cvCommentDescription;
    private CoeusVector cvType;
    private CostSharingTableCellEditor costSharingTableCellEditor;
    private CostSharingTableCellRenderer costSharingTableCellRenderer;
    private AmountTabelModel amountTabelModel;
    private AmountTableCellRenderer amountTableCellRenderer;
    private int rowID = 1; //Used for uniquely identifying InstituteProposalIDCRatesBean    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    private int accountNumberMaxLength = 0;
    
    /** Creates a new instance of CostSharingController */
    public CostSharingController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType) {
        super(instituteProposalBaseBean);
        this.functionType=functionType;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        costSharingForm=new CostSharingForm();
        registerComponents();
        setMaxRowID();
        setFormData(instituteProposalBaseBean);
        showCommentMissingMessage();
        costSharingTableCellEditor = new CostSharingTableCellEditor();
        costSharingTableCellRenderer=new CostSharingTableCellRenderer();
        formatFields();
        setColumnData();
        setTableKeyTraversal();
        
    }
    /**
     * Registers listener and other components
     */    
    public void registerComponents() {
        costSharingTableModel = new CostSharingTableModel();
        costSharingForm.tblCostSharing.setSelectionBackground(java.awt.Color.yellow);
        costSharingForm.tblCostSharing.setSelectionForeground(java.awt.Color.black);        
        costSharingForm.tblCostSharing.setModel(costSharingTableModel);
        costSharingForm.btnAdd.addActionListener(this);
        costSharingForm.btnDelete.addActionListener(this);
        amountTabelModel = new AmountTabelModel();
        amountTableCellRenderer=new AmountTableCellRenderer();
        costSharingForm.tblTotalDispaly.setModel(amountTabelModel);
        
        Component component[]  = { costSharingForm.btnAdd,costSharingForm.btnDelete,costSharingForm.tblCostSharing,costSharingForm.txtArComments};
        ScreenFocusTraversalPolicy  traversal = new ScreenFocusTraversalPolicy(component);
        costSharingForm.setFocusTraversalPolicy(traversal);
        costSharingForm.setFocusCycleRoot(true);
    }

    
    /**
     * Thisa sets the focus to a component when the tab is opened
     */    
    public void setDefaultFocusForComponent(){
        // Bug Fix #935 - 14th June chandra - Start
        if(functionType != DISPLAY_PROPOSAL) {
            if(costSharingForm.tblCostSharing.getRowCount() > 0 ) {
                costSharingForm.tblCostSharing.requestFocusInWindow();
                
                int prevSelectedRow = costSharingForm.tblCostSharing.getSelectedRow();
                if(prevSelectedRow!=-1){
                    costSharingForm.tblCostSharing.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    costSharingForm.tblCostSharing.setRowSelectionInterval(0, 0);
                }
                costSharingForm.tblCostSharing.setColumnSelectionInterval(1,1);
            }else{
                costSharingForm.btnAdd.requestFocusInWindow();
            }            
        }
        // Bug Fix #935 - 14th June chandra - End
    } 
    
    /**
     * This displays the message if comment is missing
     */    
    public void showCommentMissingMessage() {
        if (!isCommentPresent) {
             if (functionType!=NEW_INST_PROPOSAL) {
                 //Missing cost sharing comment code in OSP$PARAMETER table
                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1005"));
             }
        }
    }
    
    /**
     * Sets the dat
     */    
    public void setFormData(Object instituteProposalBaseBean ) {
        this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean ;
        cvTableData = new CoeusVector();
        cvParameters = new CoeusVector();
        cvDeletedData = new CoeusVector();
        cvType = new CoeusVector();
        try{
            cvTableData = queryEngine.executeQuery(queryKey, InstituteProposalCostSharingBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvTableData!= null && cvTableData.size() > 0){
                costSharingTableModel.setData(cvTableData);
                amountTabelModel.setData(cvTableData);
                amountTabelModel.fireTableDataChanged();
            }
            if(cvTableData.size()<1) {
                costSharingForm.tblTotalDispaly.setVisible(false);
            }
            cvType = queryEngine.getDetails(queryKey,KeyConstants.COST_SHARING_TYPES);
            cvType.sort("description", true);
            cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//            for (int index=0;index<cvParameters.size();index++) {
//                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
//                if(CoeusConstants.COST_SHARING_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
//                    isCommentPresent=true;
//                    break;
//                }
//            }
            if(cvParameters != null && cvParameters.size() > 0){
                //To get the COST_SHARING_COMMENT_CODE parameter
                CoeusVector cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.COST_SHARING_COMMENT_CODE));
                if(cvFiltered != null && cvFiltered.size() > 0){
                    isCommentPresent=true;
                }
                //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
                cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                if(cvFiltered != null && cvFiltered.size() > 0){
                    CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
                    accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                }
            }
            //Case#2402 - End    
            if (!isCommentPresent) {
                costSharingForm.txtArComments.setEditable(false);
                costSharingForm.txtArComments.setOpaque(false);
            }else {
                if (functionType != DISPLAY_PROPOSAL) {
                    costSharingForm.txtArComments.setEditable(true);
                    costSharingForm.txtArComments.setOpaque(true);
                }
                cvCommentDescription=new CoeusVector();
                cvCommentDescription=queryEngine.getDetails(queryKey,InstituteProposalCommentsBean.class);                                
                if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
                    //CoeusVector return
                    CoeusVector cvCostSharingCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.COST_SHARING_COMMENT_CODE));
                    CoeusParameterBean coeusParameterBean = null;
                    coeusParameterBean = (CoeusParameterBean)cvCostSharingCommentCode.elementAt(0);

                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                    
                    cvCommentDescription = cvCommentDescription.filter(equals);
                    if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
                        this.commentsBean=(InstituteProposalCommentsBean)cvCommentDescription.elementAt(0);
                        costSharingForm.txtArComments.setText(this.commentsBean.getComments());
                        costSharingForm.txtArComments.setCaretPosition(0);
                    }
                }
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    
    /* This method sets the maximum Row ID from the vector of Budget Persons beans
     *that is present in queryEngine
     */
    private void setMaxRowID() {
        CoeusVector cvCostSharing = new CoeusVector();
        InstituteProposalCostSharingBean instituteProposalCostSharingBean;
        try {
            cvCostSharing = queryEngine.getDetails(queryKey, 
                InstituteProposalCostSharingBean.class);
            if (cvCostSharing != null && cvCostSharing.size() > 0) {
                    cvCostSharing.sort("rowId", false);
                    instituteProposalCostSharingBean = (InstituteProposalCostSharingBean) cvCostSharing.get(0);
                    rowID = instituteProposalCostSharingBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }    
    
    
    private void setColumnData(){
        JTableHeader tableHeader = costSharingForm.tblCostSharing.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        costSharingForm.tblCostSharing.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        costSharingForm.tblCostSharing.setRowHeight(22);
        costSharingForm.tblCostSharing.setSelectionBackground(java.awt.Color.yellow);
        costSharingForm.tblCostSharing.setSelectionForeground(java.awt.Color.black);
        costSharingForm.tblCostSharing.setShowHorizontalLines(true);
        costSharingForm.tblCostSharing.setShowVerticalLines(true);
        costSharingForm.tblCostSharing.setOpaque(false);
        costSharingForm.tblCostSharing.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        
        costSharingForm.tblTotalDispaly.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        costSharingForm.tblTotalDispaly.setRowHeight(22);
        //costSharingForm.tblCostSharing.setSelectionBackground(java.awt.Color.yellow);
        //costSharingForm.tblCostSharing.setSelectionForeground(java.awt.Color.black);
        costSharingForm.tblTotalDispaly.setShowHorizontalLines(false);
        costSharingForm.tblTotalDispaly.setShowVerticalLines(false);
        costSharingForm.tblTotalDispaly.setOpaque(false);
        costSharingForm.tblTotalDispaly.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = costSharingForm.tblCostSharing.getColumnModel().getColumn(PERCENTAGE_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        column.setPreferredWidth(80);
        column.setResizable(true);
        column.setCellRenderer(costSharingTableCellRenderer);
        column.setCellEditor(costSharingTableCellEditor);
        
        column = costSharingForm.tblCostSharing.getColumnModel().getColumn(TYPE_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        column.setPreferredWidth(210);
        column.setResizable(true);
        column.setCellRenderer(costSharingTableCellRenderer);
        column.setCellEditor(costSharingTableCellEditor);        
       
        column = costSharingForm.tblCostSharing.getColumnModel().getColumn(FISCAL_YEAR_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(150);
        //modified for COEUSQA-1426-Ability to enter data besides YYYY START
        //column.setPreferredWidth(80);        
        column.setPreferredWidth(100);
        //modified for COEUSQA-1426-Ability to enter data besides YYYY END
        column.setResizable(true);
        column.setCellRenderer(costSharingTableCellRenderer);
        column.setCellEditor(costSharingTableCellEditor);
        
        column = costSharingForm.tblCostSharing.getColumnModel().getColumn(AMOUNT_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(150);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellRenderer(costSharingTableCellRenderer);
        column.setCellEditor(costSharingTableCellEditor);
        
        column = costSharingForm.tblCostSharing.getColumnModel().getColumn(SOURCE_ACCOUNT_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(150);
        //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
        //column.setPreferredWidth(140);
        column.setPreferredWidth(240);
        //Case#2402 - End
        column.setResizable(true);
        column.setCellRenderer(costSharingTableCellRenderer);
        column.setCellEditor(costSharingTableCellEditor);
        
        
        TableColumn amountColumn = costSharingForm.tblTotalDispaly.getColumnModel().getColumn(TOTAL_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        amountColumn.setPreferredWidth(370);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
        
        
        amountColumn = costSharingForm.tblTotalDispaly.getColumnModel().getColumn(TOTAL_AMOUNT_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        amountColumn.setPreferredWidth(100);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
       }
    
    /**
     * Got from the controller
     */    
    public void display() {
    }
    
    /**
     * perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */    
    public void formatFields() {
        if(functionType== DISPLAY_PROPOSAL){
            costSharingForm.btnAdd.setEnabled(false);
            costSharingForm.btnDelete.setEnabled(false);
            costSharingForm.txtArComments.setEditable(false);
            costSharingForm.txtArComments.setOpaque(false);
        }
    }
    
    /**
     * returns the Component
     */    
    public java.awt.Component getControlledUI() {
        return costSharingForm;
    }
    
    /**
     * returns the form data
     */    
    public Object getFormData() {
        return null;
    }
    
    /**
     * saves the Form Data.
     */    
    public void saveFormData() {
        costSharingTableCellEditor.stopCellEditing();
        try{
            CoeusVector dataObject = new CoeusVector();
            StrictEquals stCommentsEquals = new StrictEquals();
            InstituteProposalCommentsBean queryCommentsBean = new InstituteProposalCommentsBean();
            CoeusVector cvTempComment = queryEngine.getDetails(queryKey, InstituteProposalCommentsBean.class);
            cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvCostSharingComment = null;
            CoeusParameterBean coeusParameterBean = null;
            CoeusVector cvCostSharingCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.COST_SHARING_COMMENT_CODE));
            if(cvCostSharingCommentCode!=null && cvCostSharingCommentCode.size() > 0){
                coeusParameterBean = (CoeusParameterBean)cvCostSharingCommentCode.elementAt(0);
            }
            if (cvTempComment!= null && cvTempComment.size()>0) {
                if(coeusParameterBean!=null){
                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));

                    cvCostSharingComment = cvTempComment.filter(equals);
                    if(cvCostSharingComment!=null && cvCostSharingComment.size() > 0){
                        queryCommentsBean = (InstituteProposalCommentsBean)cvCostSharingComment.elementAt(0);                            
                    }
                }
            }
            if (coeusParameterBean!=null){
                if(commentsBean!= null){
                    commentsBean.setComments(costSharingForm.txtArComments.getText());
                    if(! stCommentsEquals.compare(commentsBean, queryCommentsBean)){
                        //Data Changed. save to query Engine.
                        commentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, commentsBean);
                    }
                }else{
                    if (!EMPTY_STRING.equals(costSharingForm.txtArComments.getText())) {
                        commentsBean = new InstituteProposalCommentsBean();
                        commentsBean.setProposalNumber( this.instituteProposalBaseBean.getProposalNumber());
                        commentsBean.setSequenceNumber( this.instituteProposalBaseBean.getSequenceNumber());
                        commentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
                        commentsBean.setComments(costSharingForm.txtArComments.getText());
                        commentsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey,commentsBean);
                    }
                }
            }
            if(modified){
                if(cvDeletedData!= null && cvDeletedData.size() >0){
                    dataObject.addAll(cvDeletedData);
                }
                if(cvTableData!= null && cvTableData.size() >0){
                    dataObject.addAll(cvTableData);
                }
                if(dataObject!=null){
                    for(int index = 0; index < dataObject.size(); index++){
                        InstituteProposalCostSharingBean bean = (InstituteProposalCostSharingBean) dataObject.get(index);
                        if(bean.getAcType()!= null){
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                //First delete the existing data and then insert the same. This is
                                //required since primary keys can be modified
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                bean.setRowId(rowID++);
                                queryEngine.insert(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, bean);
                            }
                        }
                    }
                }
            }
        }catch (CoeusException coeusException){
          //coeusException.printStackTrace();
        }   
        finally{
            modified=false;
        }
        
  }
   
    /**
     * validate the form data/Form and returns true if
     * validation is through else returns false.
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        costSharingTableCellEditor.stopCellEditing();
        
        for (int index=0;index<cvTableData.size();index++) {
            InstituteProposalCostSharingBean currentRowBean=(InstituteProposalCostSharingBean)cvTableData.elementAt(index);
            if (currentRowBean.getCostSharingTypeCode()==0) {
                //Please enter a cost sharing type in row <row number>
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropCostSharing_exceptionCode.1203")+(index+1));
                costSharingForm.tblCostSharing.setRowSelectionInterval( index, index );
                return false;
            }
            //modified for COEUSQA-1426-Ability to enter data besides YYYY START
            //if (EMPTY_STRING.equals(currentRowBean.getFiscalYear())) {
            if (EMPTY_STRING.equals(currentRowBean.getFiscalYear().trim()) || Integer.parseInt(currentRowBean.getFiscalYear().trim())==0) {
                //"Please enter a Fiscal Year for cost sharing row <row number> "
                CoeusOptionPane.showInfoDialog("Please enter a valid Project Year for cost sharing row "+(index+1));
                costSharingForm.tblCostSharing.setRowSelectionInterval( index, index );
                costSharingForm.tblCostSharing.scrollRectToVisible(
                costSharingForm.tblCostSharing.getCellRect(
                index ,FISCAL_YEAR_COLUMN, true));
                //costSharingForm.tblCostSharing.editCellAt(index,FISCAL_YEAR_COLUMN);
                return false;
            }
            //No validation is required as user can enter  fiscal year anything between 1-9999
//            if (Integer.parseInt(currentRowBean.getFiscalYear())<1900 || Integer.parseInt(currentRowBean.getFiscalYear())>2099 ) {
//                // Please enter a valid fiscal Year for Cost Sharing row <rownumber>
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropCostSharing_exceptionCode.1202")+(index+1));
//                costSharingForm.tblCostSharing.setRowSelectionInterval( index, index );
//                costSharingForm.tblCostSharing.scrollRectToVisible(
//                costSharingForm.tblCostSharing.getCellRect(
//                index ,FISCAL_YEAR_COLUMN, true));
//                //costSharingForm.tblCostSharing.editCellAt(index,FISCAL_YEAR_COLUMN);
//                return false;
//            }
             //modified for COEUSQA-1426-Ability to enter data besides YYYY START
            if (EMPTY_STRING.equals(currentRowBean.getSourceAccount())) {
                //Please enter a Source Account for cost sharing row <row number>
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropCostSharing_exceptionCode.1204")+(index+1));
                costSharingForm.tblCostSharing.setRowSelectionInterval( index, index );
                costSharingForm.tblCostSharing.scrollRectToVisible(
                costSharingForm.tblCostSharing.getCellRect(
                index ,SOURCE_ACCOUNT_COLUMN, true));
                //costSharingForm.tblCostSharing.editCellAt(index,SOURCE_ACCOUNT_COLUMN);
                return false;
            }      
        }
        boolean isDuplicateRowPresent=checkDuplicateRow();
        if (isDuplicateRowPresent) {
            return false;
        }
        return true;
    }

    /** Check for the Duplication . If  Fiscal Year
     *and Cost sharing type are same then throw the Duplication message. If any one of these are
     *different then accept the row
     */        
    
    private boolean checkDuplicateRow(){
        costSharingTableCellEditor.stopCellEditing();
        Equals costSharingTypeEquals,fiscalYearEquals,sourceAccountEquals;
        CoeusVector coeusVector = null ;
        And costSharingTypeAndFiscalYear,costSharingTypeAndFiscalYearAndSourceAccount;
        
        if(cvTableData!=null && cvTableData.size() > 0){
            for(int index = 0; index < cvTableData.size(); index++){
                InstituteProposalCostSharingBean instituteProposalCostSharingBean = ( InstituteProposalCostSharingBean )
                cvTableData.get(index);
                costSharingTypeEquals = new Equals("costSharingTypeCode", new Integer(instituteProposalCostSharingBean.getCostSharingTypeCode()));
                fiscalYearEquals = new Equals("fiscalYear", instituteProposalCostSharingBean.getFiscalYear().trim());
                sourceAccountEquals = new Equals("sourceAccount", instituteProposalCostSharingBean.getSourceAccount().trim());
                costSharingTypeAndFiscalYear = new And(fiscalYearEquals, costSharingTypeEquals);
                costSharingTypeAndFiscalYearAndSourceAccount =new And(costSharingTypeAndFiscalYear,sourceAccountEquals);
                coeusVector = cvTableData.filter(costSharingTypeAndFiscalYearAndSourceAccount);
                if(coeusVector.size()==-1) {
                    return false;
                }
                if(coeusVector!=null && coeusVector.size() > 1){
                    //A row duplicates another.\nEnter different cost sharing information
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropCostSharing_exceptionCode.1205"));
                    //costSharingForm.tblCostSharing.editCellAt(index,PERCENTAGE_COLUMN);
                    costSharingTableCellEditor.txtPercentage.requestFocus();
                    costSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
                    costSharingForm.tblCostSharing.scrollRectToVisible(
                    costSharingForm.tblCostSharing.getCellRect(
                    index ,0, true));
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * Setter for property refreshRequired of super
     */    
    public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    /**
     * Getter for property refreshRequired
     */    
    public boolean isRefreshRequired() {
        boolean retValue;
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    /**
     * To refresh the GUI with new Data
     */    
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(instituteProposalBaseBean);
            setRefreshRequired(false);
        }
    }
    
    /**
     * Action handler for Ok and cancel button
     */    
    public void actionPerformed(ActionEvent ae) {
        Object source=ae.getSource();
        if (source.equals(costSharingForm.btnAdd)) {
            performAddRowAction();
        }
        if (source.equals(costSharingForm.btnDelete)) {
            performDeleteRowAction();
        }
    }
    
    
    /* To handle Add button actioon
     */
    private void performAddRowAction(){
        costSharingTableCellEditor.stopCellEditing();
        double cost = 0.0;
        if (cvTableData.size()>0) {
            InstituteProposalCostSharingBean lastRowBean=(InstituteProposalCostSharingBean)cvTableData.elementAt(cvTableData.size()-1);
            if (lastRowBean.getCostSharingTypeCode()==0) {
               //ComboBoxBean comboBean = new ComboBoxBean();
                //comboBean.setDescription(EMPTY_STRING);
                lastRowBean.setCostSharingPercentage(cost);
                lastRowBean.setCostSharingTypeCode(0);
                //costSharingTableCellEditor.cmbType.addItem(comboBean);
                lastRowBean.setFiscalYear(EMPTY_STRING);
                lastRowBean.setAmount(cost);
                lastRowBean.setSourceAccount(EMPTY_STRING);
                //cvTableData.remove(cvTableData.size()-1);
                //cvTableData.add(lastRowBean);
                costSharingTableModel.fireTableRowsUpdated(costSharingTableModel.getRowCount(), costSharingTableModel.getRowCount());
                if(cvTableData.size()>0) {
                    // Added by chandra - bug fix
                    costSharingForm.tblCostSharing.setRowSelectionInterval(costSharingForm.tblCostSharing.getRowCount()-1,costSharingForm.tblCostSharing.getRowCount()-1);
                    costSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                    costSharingForm.tblCostSharing.editCellAt(cvTableData.size()-1,PERCENTAGE_COLUMN);
                    costSharingForm.tblCostSharing.getEditorComponent().requestFocusInWindow();
                    costSharingForm.tblCostSharing.scrollRectToVisible(
                    costSharingForm.tblCostSharing.getCellRect(
                    costSharingForm.tblCostSharing.getRowCount()-1 ,0, true));
                }
                return;
            }
        }
        InstituteProposalCostSharingBean newBean = new InstituteProposalCostSharingBean();
        newBean.setProposalNumber(instituteProposalBaseBean.getProposalNumber());
        newBean.setSequenceNumber(instituteProposalBaseBean.getSequenceNumber());
        newBean.setCostSharingPercentage(cost);
        newBean.setCostSharingTypeCode(0);
        newBean.setFiscalYear(EMPTY_STRING);
        newBean.setAmount(cost);
        newBean.setSourceAccount(EMPTY_STRING);
        newBean.setRowId(rowID++);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified= true;
        cvTableData.add(newBean);
        costSharingTableModel.fireTableRowsInserted(costSharingTableModel.getRowCount() + 1, costSharingTableModel.getRowCount() + 1);
        int lastRow = costSharingForm.tblCostSharing.getRowCount()-1;
        if(lastRow >= 0){
            // Added by chandra for bug fix - start
            costSharingForm.tblCostSharing.setRowSelectionInterval( lastRow, lastRow );
             // Bug fix #925 - start
            costSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
            costSharingForm.tblCostSharing.editCellAt(lastRow,PERCENTAGE_COLUMN);
            costSharingForm.tblCostSharing.getEditorComponent().requestFocusInWindow();
            
            costSharingForm.tblCostSharing.scrollRectToVisible(
            costSharingForm.tblCostSharing.getCellRect(
            lastRow ,0, true));
            costSharingForm.tblTotalDispaly.setVisible(true);
            // Bug fix - End chandra
        }
    }
    
    /** To Handle Delete button action
     */    
    private void performDeleteRowAction(){
       costSharingTableCellEditor.stopCellEditing();
        int rowIndex = costSharingForm.tblCostSharing.getSelectedRow();
        if (rowIndex==-1) {
            CoeusOptionPane.showErrorDialog(SELECT_A_ROW);
            return;
        }
        if(rowIndex != -1 && rowIndex >= 0){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            DELETE_CONFIRMATION,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                InstituteProposalCostSharingBean deletedCostSharingBean = (InstituteProposalCostSharingBean)cvTableData.get(rowIndex);
                cvDeletedData.add(deletedCostSharingBean);
                if (deletedCostSharingBean.getAcType() == null ||
                deletedCostSharingBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedData.add(deletedCostSharingBean);
                }
                if(cvTableData!=null && cvTableData.size() > 0){
                    cvTableData.remove(rowIndex);
                    costSharingTableCellEditor.cancelCellEditing();
                    costSharingTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    modified = true;
                    deletedCostSharingBean.setAcType(TypeConstants.DELETE_RECORD);
                    costSharingTableCellEditor.stopCellEditing();
                }
                if(rowIndex >0){
                    costSharingForm.tblCostSharing.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    costSharingForm.tblCostSharing.scrollRectToVisible(
                    costSharingForm.tblCostSharing.getCellRect(
                    rowIndex-1 ,0, true));
                }else{
                    if(costSharingForm.tblCostSharing.getRowCount()>0){
                        costSharingForm.tblCostSharing.setRowSelectionInterval(0,0);
                    }
                }
                
            }
        }
        if (cvTableData.size()<1) {
            costSharingForm.tblTotalDispaly.setVisible(false);
        }
    }    
        
    
    /**
     * Table model for the costsharing table
     */    
    public class CostSharingTableModel extends AbstractTableModel {
        //modified for COEUSQA-1426-Ability to enter data besides YYYY START
        //private String colName[] = {"Percentage","Type","Fiscal Year","Amount","Source Account"};
        private String colName[] = {"Percentage","Type","Project Year","Amount","Source Account"};
         //modified for COEUSQA-1426-Ability to enter data besides YYYY end
        private Class colClass[] = {Double.class, String.class, String.class, Double.class, String.class};
        
        /**
         * If cell editable, returns true, else returns false
         */        
        public boolean isCellEditable(int row, int col) {
            return (functionType == DISPLAY_PROPOSAL ? false : true);
        }
        /**
         * Returns the column class
         */        
        public  Class getColumnClass(int col){
            return colClass[col];
        }
        /**
         * REturns the number of columns
         */        
        public int getColumnCount() {
            return colName.length;
        }
        /**
         * Sets the table data
         */        
        public void setData(CoeusVector data){
            cvTableData = data;
        }
        /**
         * Returns the number of rows
         */        
        public int getRowCount() {
            if(cvTableData== null){
                return 0;
            }else{
                return cvTableData.size();
            }
            
        }
        /**
         * Gets value at a particular cell
         */        
        public Object getValueAt(int rowIndex, int columnIndex) {

           InstituteProposalCostSharingBean instituteProposalCostSharingBean= (InstituteProposalCostSharingBean)cvTableData.elementAt(rowIndex);
           
           switch(columnIndex){
               case PERCENTAGE_COLUMN:
                   return new Double(instituteProposalCostSharingBean.getCostSharingPercentage());
               case TYPE_COLUMN:
                   int typeCode=instituteProposalCostSharingBean.getCostSharingTypeCode();
                   CoeusVector filteredVector = cvType.filter(new Equals("code",""+typeCode));
                   if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = null;
                        comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }                   
               case FISCAL_YEAR_COLUMN:
                   return instituteProposalCostSharingBean.getFiscalYear();
               case AMOUNT_COLUMN:
                   return new Double(instituteProposalCostSharingBean.getAmount());
               case SOURCE_ACCOUNT_COLUMN:
                   return instituteProposalCostSharingBean.getSourceAccount();
               default:
                   return EMPTY_STRING;
                   
           }
           
        }
        /**
         * Sets the value in a cell
         */        
        public void setValueAt(Object value, int row, int col) {
            InstituteProposalCostSharingBean instituteProposalCostSharingBean= 
                    (InstituteProposalCostSharingBean)cvTableData.elementAt(row);
            double cost = 0.00;
            
            switch(col) {
                case PERCENTAGE_COLUMN:
                    double newValue=Double.parseDouble(value.toString());
                    if(newValue!=instituteProposalCostSharingBean.getCostSharingPercentage()) {
                        instituteProposalCostSharingBean.setCostSharingPercentage(newValue);
                        modified = true;
                    }
                    break;
                case TYPE_COLUMN:
                    if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)cvType.filter(new Equals("description", value.toString())).get(0);
                        int typeCode = Integer.parseInt(comboBoxBean.getCode());
                        if( typeCode != instituteProposalCostSharingBean.getCostSharingTypeCode() ){
                            instituteProposalCostSharingBean.setCostSharingTypeCode(typeCode);
                            modified = true;
                        }
                    }
                    break;
               case FISCAL_YEAR_COLUMN: 
                   //Modified for COEUSQA-1426 start
                   if (!value.toString().trim().equals(instituteProposalCostSharingBean.getFiscalYear().trim())) {
                               instituteProposalCostSharingBean.setFiscalYear(value.toString().trim());
                               modified = true;
                           }
                   //Modified for COEUSQA-1426 start
                    break;                    
                case AMOUNT_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    if(cost!=instituteProposalCostSharingBean.getAmount()) {
                        instituteProposalCostSharingBean.setAmount(cost);
                        modified = true;

                    }
                    amountTabelModel.fireTableDataChanged();
                    break;                    
                case SOURCE_ACCOUNT_COLUMN: 
                    if (!value.toString().trim().equals(instituteProposalCostSharingBean.getSourceAccount().trim())){
                        instituteProposalCostSharingBean.setSourceAccount(value.toString().trim());
                        modified = true;
                    }
                    break;
            }
            
            if(instituteProposalCostSharingBean.getAcType()== null){
                instituteProposalCostSharingBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        /**
         * Sets value at a particular cell
         */        
        public String getColumnName(int col) {
            return colName[col];
        }
     }
    
    /**
     * Table cell editor for Cost sharing table
     */    
    public class CostSharingTableCellEditor extends AbstractCellEditor
    implements TableCellEditor {
        private CurrencyField txtPercentage;
        private DollarCurrencyTextField txtAmount;
        private CoeusComboBox cmbType;
        private CoeusTextField txtComponent;
        private int column;
        
        /**
         * Creates a Cost sharing cell editor
         */        
        public CostSharingTableCellEditor() {
            txtPercentage = new CurrencyField();
            txtAmount = new DollarCurrencyTextField(12,JTextField.RIGHT,true);
            cmbType = new CoeusComboBox();
            //cmbRateType.setModel(new DefaultComboBoxModel(cvRateType));
            txtComponent = new CoeusTextField();
        }
       private void populateTypeCombo() {
            int size = cvType.size();
            ComboBoxBean comboBoxBean;
            // Added by chandra to fix #934 2nd August
            cmbType.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)cvType.get(index);
                cmbType.addItem(comboBoxBean);
            }

        }

       /**
        * Returns the cell editor componenet
        */       
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, 
            Object value, boolean isSelected, int row, int column) {
                this.column = column;
                 switch(column){
                    case PERCENTAGE_COLUMN:
                        txtPercentage.setText(value.toString());
                        if (isSelected) {
                            txtPercentage.setBackground(java.awt.Color.yellow);
                        }
                        else {
                            txtPercentage.setBackground(java.awt.Color.white);
                        }
                        return txtPercentage;
                    case TYPE_COLUMN:
                        if(! typeComboPopulated) {
                            populateTypeCombo();
                            typeComboPopulated = true;
                        }
                        if (isSelected) {
                            cmbType.setBackground(java.awt.Color.yellow);
                        }
                        else {
                            cmbType.setBackground(java.awt.Color.white);
                        }                        
                        cmbType.setSelectedItem(value);
                        return cmbType;
                    case FISCAL_YEAR_COLUMN:
                        txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
                        txtComponent.setText(value.toString());
                        if (isSelected) {
                            txtComponent.setBackground(java.awt.Color.yellow);
                        }
                        else {
                            txtComponent.setBackground(java.awt.Color.white);
                        }                        
                        return txtComponent;
                    case AMOUNT_COLUMN:
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                        if (isSelected) {
                            txtAmount.setBackground(java.awt.Color.yellow);
                        }
                        else {
                            txtAmount.setBackground(java.awt.Color.white);
                        }                                                                        
                        return txtAmount;
                    case SOURCE_ACCOUNT_COLUMN:
                        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
                        //Modified by shiji for bug fix id 1730 - start
//                        txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,7));
                        //bug fix id 1730 - end
//                        txtComponent.setText(value.toString());
                        
                        //Sets the SourceAccountNumber length based on accountNumLength value and allow the field to
                        //accept alphanumeric with comma,hyphen and periods
                        txtComponent.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                        String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                        if(sourceAccountNumber.length() > accountNumberMaxLength){
                            sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                        }
                        txtComponent.setText(sourceAccountNumber);
                        //Case#2402 - End
                        
                        if (isSelected) {
                            txtComponent.setBackground(java.awt.Color.yellow);
                        }
                        else {
                            txtComponent.setBackground(java.awt.Color.white);
                        }                                                                                                
                        return txtComponent;
                   }
                return txtComponent;
        }
        
        /**
         * Retiurns the cell editor value
         */        
        public Object getCellEditorValue() {
            switch(column){
                case PERCENTAGE_COLUMN:
                    return txtPercentage.getText();
                case TYPE_COLUMN:
                    return cmbType.getSelectedItem();
                case FISCAL_YEAR_COLUMN:
                case SOURCE_ACCOUNT_COLUMN:
                    return txtComponent.getText();
                case AMOUNT_COLUMN:
                    return txtAmount.getValue();
                    
            }
            return cmbType;
         }
    }
    
    /**
     * Table cell renederer class for Cost sharing table
     */    
    public class CostSharingTableCellRenderer extends DefaultTableCellRenderer {
        private CurrencyField txtPercentage;
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyField;
        private JLabel lblText,lblPercentage,lblCurrency;
        
        /**
         * Creates a Table cell editor for Cost sharing table
         */        
        public CostSharingTableCellRenderer(){
            lblText = new JLabel();
            lblPercentage = new JLabel();
            lblCurrency = new JLabel();
            lblText.setOpaque(true);
            lblPercentage.setOpaque(true);
            lblCurrency.setOpaque(true);
            lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
            lblPercentage.setHorizontalAlignment(JLabel.RIGHT);
            txtPercentage = new CurrencyField();
            txtPercentage.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtComponent = new CoeusTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyField=new DollarCurrencyTextField(12,JTextField.RIGHT,true);
            txtCurrencyField.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            
        }
        
        /**
         * REturns renderer component
         */        
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,Object value,
        boolean isSelected,boolean hasFocus,int row,int col) {
             switch(col){
                case PERCENTAGE_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblPercentage.setBackground(disabledBackground);
                            lblPercentage.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblPercentage.setBackground(java.awt.Color.YELLOW);
                            lblPercentage.setForeground(java.awt.Color.black);
                        }else{
                            lblPercentage.setBackground(java.awt.Color.white);
                            lblPercentage.setForeground(java.awt.Color.black);
                        }
                    txtPercentage.setText(value.toString());
                    lblPercentage.setText(txtPercentage.getText());
                    return lblPercentage;
                case TYPE_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblText.setBackground(disabledBackground);
                            lblText.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }else{
                            lblText.setBackground(java.awt.Color.white);
                            lblText.setForeground(java.awt.Color.black);
                        }
                        
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                        return lblText;
                case FISCAL_YEAR_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblText.setBackground(disabledBackground);
                            lblText.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }else{
                            lblText.setBackground(java.awt.Color.white);
                            lblText.setForeground(java.awt.Color.black);
                        }
                        
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                        return lblText;
                case SOURCE_ACCOUNT_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblText.setBackground(disabledBackground);
                            lblText.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }else{
                            lblText.setBackground(java.awt.Color.white);
                            lblText.setForeground(java.awt.Color.black);
                        }
                        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
                        //Checks if SourceAccountNumber is greater than account number length from parameter, 
                        //then SourceAccountNumber is substring to accountNumLength
                        //txtComponent.setText(value.toString());
                        String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                        if(sourceAccountNumber.length() > accountNumberMaxLength){
                            sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                        }
                        txtComponent.setText(sourceAccountNumber);
                        lblText.setText(txtComponent.getText());
                        //Case#2402 - End
                        
                        return lblText;
                    
                 case AMOUNT_COLUMN: 
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblCurrency.setBackground(disabledBackground);
                            lblCurrency.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblCurrency.setBackground(java.awt.Color.YELLOW);
                            lblCurrency.setForeground(java.awt.Color.black);
                        }else{
                            lblCurrency.setBackground(java.awt.Color.white);
                            lblCurrency.setForeground(java.awt.Color.black);
                        } 
                    txtCurrencyField.setValue(new 
                        Double(value.toString()).doubleValue());
                    lblCurrency.setText(txtCurrencyField.getText());
                    return lblCurrency;
        }
            return this;
     }
    }
    
    

    /**
     * Table model for Amout table
     */    
  public class AmountTabelModel extends AbstractTableModel {
        private String colName[] = {"Total: ", ""};
        private Class colClass[] = {String.class, Double.class};
        
        /**
         * return tru if cell is editable, else returns false
         */        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * returns number of columns
         */        
        public int getColumnCount(){
            return colName.length;
        }
        
        /**
         * returns column class
         */        
        public Class getColumnClass(int colIndex){
            return colClass[colIndex];
        }
        /**
         * returns number of rows
         */        
        public int getRowCount(){
            return 1;
        }
        
        /**
         * sets the data for the table
         */        
        public void setData(CoeusVector cvTableData){
            cvTableData = cvTableData;
        }
        /**
         * returns column name
         */        
        public String getColumnName(int column){
            return colName[column];
        }
        
        /**
         * gets valus at a cell
         */        
        public Object getValueAt(int row, int col) {
           double totalAmount = 0.00;
           String name = "Total: ";
           if(col==TOTAL_COLUMN){
               return name;
           }
           if(col==TOTAL_AMOUNT_COLUMN){
               totalAmount = cvTableData.sum("amount");
               return new Double(totalAmount);
           }
            return EMPTY_STRING;
        }
    }
  
  
  /**
   * Cell renderer for Amount Table
   */  
public class AmountTableCellRenderer extends DefaultTableCellRenderer 
    implements TableCellRenderer {
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        private JLabel lblText,lblCurrency;
        
        /**
         * Creates cell renedere for Amount table
         */        
        public AmountTableCellRenderer(){
            lblText = new JLabel();
            lblCurrency = new JLabel();
            lblText.setOpaque(true);
            lblCurrency.setOpaque(true);
            lblText.setHorizontalAlignment(JLabel.RIGHT);
            lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
            lblText.setFont(CoeusFontFactory.getLabelFont());
            txtComponent = new JTextField();
            txtCurrencyComponent = new DollarCurrencyTextField();
            txtComponent.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtComponent.setHorizontalAlignment(JTextField.RIGHT);
            txtComponent.setForeground(java.awt.Color.BLACK);
            txtComponent.setFont(CoeusFontFactory.getLabelFont());
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyComponent.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtCurrencyComponent.setForeground(java.awt.Color.BLACK);
            txtCurrencyComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        /**
         * returns table cell renderer component
         */        
      public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){


            switch(col){
                case TOTAL_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;

                case TOTAL_AMOUNT_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrencyComponent.setText(EMPTY_STRING);
                        lblCurrency.setText(txtCurrencyComponent.getText());
                    }else{
                        txtCurrencyComponent.setValue(new Double(value.toString()).doubleValue());
                        lblCurrency.setText(txtCurrencyComponent.getText());
                    }
                    return lblCurrency;
            }
            return txtComponent;
        }

    }

    int row;
    int column;
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = costSharingForm.tblCostSharing.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = costSharingForm.tblCostSharing.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = costSharingForm.tblCostSharing.getRowCount();
                int columnCount = costSharingForm.tblCostSharing.getColumnCount();
                if(row==rowCount-1 && column==columnCount-1){
                    row = 0;
                    column = 0;
                    costSharingTableCellEditor.stopCellEditing();
                    costSharingForm.txtArComments.requestFocusInWindow();
                    return;
                }
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                
                while ( table.isCellEditable(row, column) ) {
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
                
            }
        };
        costSharingForm.tblCostSharing.getActionMap().put(im.get(tab), tabAction);
        
        
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = costSharingForm.tblCostSharing.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                column = costSharingForm.tblCostSharing.getSelectedColumn();
                row = costSharingForm.tblCostSharing.getSelectedRow();
                int rowCount = costSharingForm.tblCostSharing.getRowCount();
                int columnCount = costSharingForm.tblCostSharing.getColumnCount();
                if(row==0 && column==0){
                    row = 0;
                    column = 0;
                    costSharingTableCellEditor.stopCellEditing();
                    costSharingForm.txtArComments.requestFocusInWindow();
                    return;
                }
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                
            }
        };
        costSharingForm.tblCostSharing.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
}
