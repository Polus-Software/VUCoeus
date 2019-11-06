/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * IDCRatesController.java
 *
 * Created on April 13, 2004, 3:45 PM
 */

package edu.mit.coeus.instprop.controller;

/**
 *
 * @author  bijosh
 */

import javax.swing.table.*;
import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import javax.swing.DefaultListSelectionModel;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.instprop.gui.IDCRatesForm;
import edu.mit.coeus.instprop.controller.InstituteProposalController;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;


/**
 * Creates an instance of IDC Rates controller
 */
public class IDCRatesController extends InstituteProposalController implements
ActionListener {
    private static final String EMPTY_STRING = "";
    private static final int RATE_COLUMN = 0;
    private static final int TYPE_COLUMN = 1;
    private static final int FISCAL_YEAR_COLUMN = 2;
    private static final int CAMPUS_COLUMN = 3;
    private static final int UNDERRECOVERY_COLUMN = 4;
    private static final int SOURCE_ACCOUNT_COLUMN = 5;
    private static final int TOTAL_COLUMN=0;
    private static final int TOTAL_AMOUNT_COLUMN=1;
    
    private static final String ON="On";
    private static final String OFF="Off";
    private static final String SELECT_A_ROW="Please select an IDC Row";
    private static final String DELETE_CONFIRMATION="Are you sure you want to delete this IDC Rate row?";
    private boolean isCommentPresent=false;
    private CoeusMessageResources coeusMessageResources;
    private IDCRatesForm iDCRatesForm;
    private IDCRateTableModel iDCRateTableModel;
    private boolean campusComboPopulated = false;
    private boolean typeComboPopulated = false;
    private boolean modified=false;
    private InstituteProposalCommentsBean commentsBean;
    private char functionType;
    
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private QueryEngine queryEngine;
    private CoeusVector cvTableData;
    private CoeusVector cvParameters;
    private CoeusVector cvDeletedData;
    private CoeusVector cvRateType;
    private CoeusVector cvCommentDescription;
    
    private IDCRateTableCellEditor iDCRateTableCellEditor;
    private IDCRateTableCellRenderer iDCRateTableCellRenderer;
    private AmountTabelModel amountTabelModel;
    private AmountTableCellRenderer amountTableCellRenderer;
    private int rowID = 1; //Used for uniquely identifying InstituteProposalIDCRatesBean
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    private int accountNumberMaxLength = 0;
    
    /** Creates a new instance of IDCRatesController */
    public IDCRatesController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType) {
        super(instituteProposalBaseBean);
        this.functionType=functionType;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        iDCRatesForm=new IDCRatesForm();
        registerComponents();
        setMaxRowID();
        setFormData(instituteProposalBaseBean);
        showCommentMissingMessage();
        iDCRateTableCellEditor = new IDCRateTableCellEditor();
        iDCRateTableCellRenderer=new IDCRateTableCellRenderer();
        formatFields();
        setColumnData();     
        setTableKeyTraversal();
    }
    /**
     * Registers the components
     */
    public void registerComponents() {
        iDCRateTableModel = new IDCRateTableModel();
        iDCRatesForm.tblIDCRates.setSelectionBackground(java.awt.Color.yellow);
        iDCRatesForm.tblIDCRates.setSelectionForeground(java.awt.Color.black);
        iDCRatesForm.tblIDCRates.setModel(iDCRateTableModel);
        iDCRatesForm.btnAdd.addActionListener(this);
        iDCRatesForm.btnDelete.addActionListener(this);
        amountTabelModel = new AmountTabelModel();
        amountTableCellRenderer=new AmountTableCellRenderer();
        iDCRatesForm.tblAdd.setModel(amountTabelModel);
        
        Component component[]  = { iDCRatesForm.btnAdd,iDCRatesForm.btnDelete,iDCRatesForm.tblIDCRates,iDCRatesForm.txtArComments};
        ScreenFocusTraversalPolicy  traversal = new ScreenFocusTraversalPolicy(component);
        iDCRatesForm.setFocusTraversalPolicy(traversal);
        iDCRatesForm.setFocusCycleRoot(true);
        
    }
    /**
     * Displays message if comment is missing
     */
    public void showCommentMissingMessage() {
        if (!isCommentPresent) {
            if (functionType!=NEW_INST_PROPOSAL) {
                //Missing Indirect cost comment code in OSP$PARAMETER table
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1006"));
            }
        }
    }
    
    /**
     * Sets the form data
     */
    public void setFormData(Object instituteProposalBaseBean ) {
        this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean ;
        cvTableData = new CoeusVector();
        cvParameters = new CoeusVector();
        cvDeletedData = new CoeusVector();
        cvRateType = new CoeusVector();
        try{
            cvTableData = queryEngine.executeQuery(queryKey, InstituteProposalIDCRateBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvTableData!= null && cvTableData.size() > 0){
                //Case #2412 start
                cvTableData.sort("fiscalYear");
                //Casse #2412 end
                iDCRateTableModel.setData(cvTableData);
                amountTabelModel.setData(cvTableData);
                amountTabelModel.fireTableDataChanged();
            }
            if (cvTableData.size()<1) {
                iDCRatesForm.tblAdd.setVisible(false);
            }
            cvRateType = queryEngine.getDetails(queryKey,KeyConstants.IDC_RATE_TYPES);
            // added by chandra
            cvRateType.sort("description", true);
            // end chandra
            /*****************************************************************************/
            
            cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//            for (int index=0;index<cvParameters.size();index++) {
//                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvComments.elementAt(index);
//                if(CoeusConstants.INDIRECT_COST_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
//                    isCommentPresent=true;
//                    break;
//                }
//            }
            
            //To get the INDIRECT_COST_COMMENT_CODE parameter
            if(cvParameters != null && cvParameters.size()>0){
                CoeusVector cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.INDIRECT_COST_COMMENT_CODE));
                if(cvFiltered != null && cvFiltered.size()>0){
                    isCommentPresent=true;
                }
                //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
                cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                if(cvFiltered != null && cvFiltered.size() > 0){
                    CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
                    accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                }
                //Case#2402 - End
            }
            if (!isCommentPresent) {
                iDCRatesForm.txtArComments.setEditable(false);
                iDCRatesForm.txtArComments.setOpaque(false);
                
            }else {
                if(functionType!=DISPLAY_PROPOSAL) {
                    iDCRatesForm.txtArComments.setEditable(true);
                    iDCRatesForm.txtArComments.setOpaque(true);
                }
                cvCommentDescription=new CoeusVector();
                cvCommentDescription=queryEngine.getDetails(queryKey,InstituteProposalCommentsBean.class);
                if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
                    //CoeusVector return
                    CoeusVector cvIDCRatesCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.INDIRECT_COST_COMMENT_CODE));
                    CoeusParameterBean coeusParameterBean = null;
                    coeusParameterBean = (CoeusParameterBean)cvIDCRatesCommentCode.elementAt(0);
                    
                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                    
                    cvCommentDescription = cvCommentDescription.filter(equals);
                    if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
                        this.commentsBean=(InstituteProposalCommentsBean)cvCommentDescription.elementAt(0);
                        iDCRatesForm.txtArComments.setText(this.commentsBean.getComments());
                        iDCRatesForm.txtArComments.setCaretPosition(0);
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
        CoeusVector cvIdcRates = new CoeusVector();
        InstituteProposalIDCRateBean instituteProposalIDCRateBean;
        try {
            cvIdcRates = queryEngine.getDetails(queryKey,
            InstituteProposalIDCRateBean.class);
            if (cvIdcRates != null && cvIdcRates.size() > 0) {
                cvIdcRates.sort("rowId", false);
                instituteProposalIDCRateBean = (InstituteProposalIDCRateBean) cvIdcRates.get(0);
                rowID = instituteProposalIDCRateBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    /**
     * Sets focus to a component when the tab is opened
     */
    public void setDefaultFocusForComponent(){
        // Bug Fix #935 - 14th June chandra - Start
        
        if(functionType != DISPLAY_PROPOSAL) {
            if(iDCRatesForm.tblIDCRates.getRowCount() > 0 ) {
                iDCRatesForm.tblIDCRates.requestFocusInWindow();
                
                int prevSelectedRow=iDCRatesForm.tblIDCRates.getSelectedRow();
                if(prevSelectedRow!=-1){
                    iDCRatesForm.tblIDCRates.setColumnSelectionInterval(0,0);
                    iDCRatesForm.tblIDCRates.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    iDCRatesForm.tblIDCRates.setColumnSelectionInterval(0,0);
                    iDCRatesForm.tblIDCRates.setRowSelectionInterval(0, 0);
                }
                iDCRatesForm.tblIDCRates.setColumnSelectionInterval(1,1);
            }else{
                iDCRatesForm.btnAdd.requestFocusInWindow();
            }
        }
        // Bug Fix #935 - 14th June chandra - End
    }
    
    
    private void setColumnData(){
        JTableHeader tableHeader = iDCRatesForm.tblIDCRates.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        iDCRatesForm.tblIDCRates.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        iDCRatesForm.tblIDCRates.setRowHeight(22);
        iDCRatesForm.tblIDCRates.setSelectionBackground(java.awt.Color.yellow);
        iDCRatesForm.tblIDCRates.setSelectionForeground(java.awt.Color.black);
        iDCRatesForm.tblIDCRates.setShowHorizontalLines(true);
        iDCRatesForm.tblIDCRates.setShowVerticalLines(true);
        iDCRatesForm.tblIDCRates.setOpaque(false);
        iDCRatesForm.tblIDCRates.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        iDCRatesForm.tblAdd.setOpaque(false);
        iDCRatesForm.tblAdd.setShowHorizontalLines(false);
        iDCRatesForm.tblAdd.setShowVerticalLines(false);
        
        TableColumn column = iDCRatesForm.tblIDCRates.getColumnModel().getColumn(RATE_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        column.setPreferredWidth(80);
        column.setResizable(true);
        column.setCellRenderer(iDCRateTableCellRenderer);
        column.setCellEditor(iDCRateTableCellEditor);
        
        column = iDCRatesForm.tblIDCRates.getColumnModel().getColumn(TYPE_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        column.setPreferredWidth(160);
        column.setResizable(true);
        column.setCellRenderer(iDCRateTableCellRenderer);
        column.setCellEditor(iDCRateTableCellEditor);
        
        column = iDCRatesForm.tblIDCRates.getColumnModel().getColumn(FISCAL_YEAR_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(150);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellRenderer(iDCRateTableCellRenderer);
        column.setCellEditor(iDCRateTableCellEditor);
        
        column = iDCRatesForm.tblIDCRates.getColumnModel().getColumn(CAMPUS_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(160);
        column.setPreferredWidth(80);
        column.setResizable(true);
        column.setCellRenderer(iDCRateTableCellRenderer);
        column.setCellEditor(iDCRateTableCellEditor);
        
        column = iDCRatesForm.tblIDCRates.getColumnModel().getColumn(UNDERRECOVERY_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(150);
        column.setPreferredWidth(125);
        column.setResizable(true);
        column.setCellRenderer(iDCRateTableCellRenderer);
        column.setCellEditor(iDCRateTableCellEditor);
        
        column = iDCRatesForm.tblIDCRates.getColumnModel().getColumn(SOURCE_ACCOUNT_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(150);
        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
        //column.setPreferredWidth(140);
        column.setPreferredWidth(240);
        //Case#2402 - End
        column.setResizable(true);
        column.setCellRenderer(iDCRateTableCellRenderer);
        column.setCellEditor(iDCRateTableCellEditor);
        
        TableColumn amountColumn = iDCRatesForm.tblAdd.getColumnModel().getColumn(TOTAL_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        amountColumn.setPreferredWidth(420);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
        
        
        amountColumn = iDCRatesForm.tblAdd.getColumnModel().getColumn(TOTAL_AMOUNT_COLUMN);
        //column.setMinWidth(100);
        //column.setMaxWidth(180);
        amountColumn.setPreferredWidth(125);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
        
    }
    
    /**
     *
     */
    public void display() {
    }
    
    /**
     * perform field formatting.
     * enabling, disabling components depending on the
     * function type
     */
    public void formatFields() {
        if(functionType== DISPLAY_PROPOSAL){
            iDCRatesForm.btnAdd.setEnabled(false);
            iDCRatesForm.btnDelete.setEnabled(false);
            iDCRatesForm.txtArComments.setEditable(false);
            iDCRatesForm.txtArComments.setOpaque(false);
        }
    }
    
    /**
     * returns the Component which is being controlled by this Controller.
     */
    public java.awt.Component getControlledUI() {
        return iDCRatesForm;
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
        iDCRateTableCellEditor.stopCellEditing();
        try{
            CoeusVector dataObject = new CoeusVector();
            StrictEquals stCommentsEquals = new StrictEquals();
            InstituteProposalCommentsBean queryCommentsBean = new InstituteProposalCommentsBean();
            CoeusVector cvTempComment = queryEngine.getDetails(queryKey, InstituteProposalCommentsBean.class);
            cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvIDCRatesComment = null;
            CoeusParameterBean coeusParameterBean = null;
            CoeusVector cvIDCRatesCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.INDIRECT_COST_COMMENT_CODE));
            if(cvIDCRatesCommentCode!=null && cvIDCRatesCommentCode.size() > 0){
                coeusParameterBean = (CoeusParameterBean)cvIDCRatesCommentCode.elementAt(0);
            }
            if (cvTempComment!= null && cvTempComment.size()>0) {
                if(coeusParameterBean!=null){
                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                    
                    cvIDCRatesComment = cvTempComment.filter(equals);
                    if(cvIDCRatesComment!=null && cvIDCRatesComment.size() > 0){
                        queryCommentsBean = (InstituteProposalCommentsBean)cvIDCRatesComment.elementAt(0);
                    }
                }
            }
            if (coeusParameterBean!=null){
                if(commentsBean!= null){
                    commentsBean.setComments(iDCRatesForm.txtArComments.getText());
                    if(! stCommentsEquals.compare(commentsBean, queryCommentsBean)){
                        //Data Changed. save to query Engine.
                        commentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, commentsBean);
                    }
                }else{
                    if (!EMPTY_STRING.equals(iDCRatesForm.txtArComments.getText())) {
                        commentsBean = new InstituteProposalCommentsBean();
                        commentsBean.setProposalNumber( this.instituteProposalBaseBean.getProposalNumber());
                        commentsBean.setSequenceNumber( this.instituteProposalBaseBean.getSequenceNumber());
                        commentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
                        commentsBean.setComments(iDCRatesForm.txtArComments.getText());
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
                        InstituteProposalIDCRateBean bean = (InstituteProposalIDCRateBean) dataObject.get(index);
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
            // coeusException.printStackTrace();
        }
        finally {
            modified=false;
        }
    }
    
    /** Check for the Duplication . If IDC Rate, Rate Type Code, Fiscal Year
     *and On Off Campus are same then throw the Duplication message. If any one of these are
     *different then accept the row
     */
    
    private boolean checkDuplicateRow(){
        Equals idcRateEquals,idcTypeEquals,fiscalYearEquals,onOffCampusFlagEquals,eqSourceAccount;
        //,onOffCampusEquals;
        And rateAndType, rateAndTypeAndYear,rateAndTypeAndYearAndOnOff;
        // Enhancement 2078 - start-step1
        And rateAndTypeAndYearAndOnOffAndSourceAccount;
        // Enhancement 2078 - End-step1
        if(cvTableData!=null && cvTableData.size() > 0){
            for(int index = 0; index < cvTableData.size(); index++){
                InstituteProposalIDCRateBean instituteProposalIDCRateBean = ( InstituteProposalIDCRateBean )
                cvTableData.get(index);
                
                idcRateEquals = new Equals("applicableIDCRate", new Double(instituteProposalIDCRateBean.getApplicableIDCRate()));
                idcTypeEquals = new Equals("idcRateTypeCode", new Integer(instituteProposalIDCRateBean.getIdcRateTypeCode()));
                fiscalYearEquals = new Equals("fiscalYear", instituteProposalIDCRateBean.getFiscalYear());
                onOffCampusFlagEquals = new Equals("onOffCampusFlag",instituteProposalIDCRateBean.isOnOffCampusFlag());
                // Enhancement 2078-start-Stpe2
                eqSourceAccount = new Equals("sourceAccount",instituteProposalIDCRateBean.getSourceAccount());
                // Enhancement 2078 - End - Step2
                rateAndType = new And(idcRateEquals, idcTypeEquals);
                rateAndTypeAndYear = new And(rateAndType, fiscalYearEquals);
                rateAndTypeAndYearAndOnOff = new And(rateAndTypeAndYear,onOffCampusFlagEquals);
                // Enhancement 2078 -Start - step3
                rateAndTypeAndYearAndOnOffAndSourceAccount = new And(rateAndTypeAndYearAndOnOff,eqSourceAccount);
                // Enhancement 2078 -End - step3
                CoeusVector coeusVector;
                coeusVector = cvTableData.filter(rateAndTypeAndYearAndOnOffAndSourceAccount);
                if(coeusVector.size()==-1) {
                    return false;
                }
                if(coeusVector!=null && coeusVector.size() > 1){
                    //A row duplicates another,\nEnter diffrent IDC rate information
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropIDCRates_exceptionCode.1257"));
                    //iDCRatesForm.tblIDCRates.editCellAt(index,RATE_COLUMN);
                    //iDCRateTableCellEditor.txtRate.requestFocus();
                    iDCRatesForm.tblIDCRates.setColumnSelectionInterval(0,0);
                    iDCRatesForm.tblIDCRates.setRowSelectionInterval(index,index);
                    iDCRatesForm.tblIDCRates.scrollRectToVisible(
                    iDCRatesForm.tblIDCRates.getCellRect(
                    index ,0, true));
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * validate the form data/Form and returns true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        iDCRateTableCellEditor.stopCellEditing();
        for (int index=0;index<cvTableData.size();index++) {
            InstituteProposalIDCRateBean currentRowBean=(InstituteProposalIDCRateBean)cvTableData.elementAt(index);
            //if (currentRowBean.getIdcRateTypeCode() == 0) { Bug fix #2068
            if (currentRowBean.getIdcRateTypeCode() < 0) {
                //Please enter an IDC Rate Type in row <row number>
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropIDCRates_exceptionCode.1252")+(index+1));
                iDCRatesForm.tblIDCRates.setRowSelectionInterval( index, index );
                
                return false;
            }
            if (EMPTY_STRING.equals(currentRowBean.getFiscalYear())) {
                //Please enter a Fiscal Year for IDC Rates row
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropIDCRates_exceptionCode.1253")+(index+1));
                iDCRatesForm.tblIDCRates.setRowSelectionInterval( index, index );
                iDCRatesForm.tblIDCRates.scrollRectToVisible(
                iDCRatesForm.tblIDCRates.getCellRect(
                index ,FISCAL_YEAR_COLUMN, true));
                //iDCRatesForm.tblIDCRates.editCellAt(index,FISCAL_YEAR_COLUMN);
                return false;
            }
            if (Integer.parseInt(currentRowBean.getFiscalYear())<1900 || Integer.parseInt(currentRowBean.getFiscalYear())>2099 ) {
                //Please enter a valid fiscal Year for IDC rates row
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropIDCRates_exceptionCode.1254")+(index+1));
                iDCRatesForm.tblIDCRates.setRowSelectionInterval( index, index );
                iDCRatesForm.tblIDCRates.scrollRectToVisible(
                iDCRatesForm.tblIDCRates.getCellRect(
                index ,FISCAL_YEAR_COLUMN, true));
                //iDCRatesForm.tblIDCRates.editCellAt(index,FISCAL_YEAR_COLUMN);
                return false;
            }
            if (EMPTY_STRING.equals(currentRowBean.getSourceAccount())) {
                //Please enter the Source Account number in row
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropIDCRates_exceptionCode.1255")+(index+1));
                iDCRatesForm.tblIDCRates.setRowSelectionInterval( index, index );
                iDCRatesForm.tblIDCRates.scrollRectToVisible(
                iDCRatesForm.tblIDCRates.getCellRect(
                index ,SOURCE_ACCOUNT_COLUMN, true));
                //iDCRatesForm.tblIDCRates.editCellAt(index,SOURCE_ACCOUNT_COLUMN);
                return false;
            }
        }
        boolean isNotDuplicate=checkDuplicateRow();
        if (isNotDuplicate) {
            return false;
        }
        return true;
    }
    /**
     * Setter for property refreshRequired.
     */
    public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    /**
     * Getter for property refreshRequired.
     */
    public boolean isRefreshRequired() {
        boolean retValue;
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    /**
     * Use this method to refresh the GUI with new Data
     */
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(instituteProposalBaseBean);
            cvDeletedData.clear();
            setRefreshRequired(false);
        }
    }
    
    /**
     * Action handler for Ok and Cencel button
     */
    public void actionPerformed(ActionEvent ae) {
        Object source=ae.getSource();
        if (source.equals(iDCRatesForm.btnAdd)) {
            performAddRowAction();
        }
        if (source.equals(iDCRatesForm.btnDelete)) {
            performDeleteRowAction();
        }
    }
    
    /* To handle Add button actioon
     */
    private void performAddRowAction(){
        iDCRateTableCellEditor.stopCellEditing();
        double cost = 0.0;
        if (cvTableData.size()>0) {
            InstituteProposalIDCRateBean lastRowBean=(InstituteProposalIDCRateBean)cvTableData.elementAt(cvTableData.size()-1);
            if (cost==lastRowBean.getApplicableIDCRate() && (EMPTY_STRING.equals(lastRowBean.getFiscalYear())) && (cost == lastRowBean.getUnderRecoveryIDC()) ) {
                ComboBoxBean comboBean = new ComboBoxBean();
                comboBean.setDescription(EMPTY_STRING);
                lastRowBean.setApplicableIDCRate(cost);
                // Case 2068 - start
                lastRowBean.setIdcRateTypeCode(-1);
                // Case 2068 - End
                //                iDCRateTableCellEditor.cmbRateType.addItem(comboBean);
                lastRowBean.setFiscalYear(EMPTY_STRING);
                lastRowBean.setOnOffCampusFlag(true);
                lastRowBean.setUnderRecoveryIDC(cost);
                lastRowBean.setSourceAccount(EMPTY_STRING);
                iDCRateTableModel.fireTableRowsUpdated(iDCRateTableModel.getRowCount(), iDCRateTableModel.getRowCount());
                if(cvTableData.size()>0) {
                    iDCRatesForm.tblIDCRates.setColumnSelectionInterval(RATE_COLUMN,RATE_COLUMN);
                    iDCRatesForm.tblIDCRates.setRowSelectionInterval(cvTableData.size()-1,cvTableData.size()-1);
                    iDCRatesForm.tblIDCRates.editCellAt(cvTableData.size()-1,RATE_COLUMN);
                    iDCRatesForm.tblIDCRates.getEditorComponent().requestFocusInWindow();
                }
                return;
            }
        }
        InstituteProposalIDCRateBean newBean = new InstituteProposalIDCRateBean();
        newBean.setProposalNumber(instituteProposalBaseBean.getProposalNumber());
        newBean.setSequenceNumber(instituteProposalBaseBean.getSequenceNumber());
        newBean.setApplicableIDCRate(cost);
        // Case 2068 - start
        newBean.setIdcRateTypeCode(-1);
        // Case 2068 - End
        newBean.setFiscalYear(EMPTY_STRING);
        newBean.setOnOffCampusFlag(true);
        newBean.setUnderRecoveryIDC(cost);
        newBean.setSourceAccount(EMPTY_STRING);
        //newBean.setAw_SourceAccount(EMPTY_STRING);
        newBean.setRowId(rowID++);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified= true;
        cvTableData.add(newBean);
        iDCRateTableModel.fireTableRowsInserted(iDCRateTableModel.getRowCount() + 1, iDCRateTableModel.getRowCount() + 1);
        int lastRow = iDCRatesForm.tblIDCRates.getRowCount()-1;
        if(lastRow >= 0){
            iDCRatesForm.tblIDCRates.setRowSelectionInterval( lastRow, lastRow );
            // Bug fix #925 - start
            iDCRatesForm.tblIDCRates.setColumnSelectionInterval(0,0);
            iDCRatesForm.tblIDCRates.editCellAt(lastRow,RATE_COLUMN);
            iDCRatesForm.tblIDCRates.getEditorComponent().requestFocusInWindow();
            // Bug fix #925 - End
            iDCRatesForm.tblIDCRates.scrollRectToVisible(
            iDCRatesForm.tblIDCRates.getCellRect(
            lastRow ,0, true));
            iDCRatesForm.tblAdd.setVisible(true);
        }
        
        //iDCRatesForm.tblIDCRates.editCellAt(lastRow,RATE_COLUMN);
        //iDCRateTableCellEditor.txtRate.requestFocus();
    }
    
    /** To Handle Delete button action
     */
    private void performDeleteRowAction(){
        
        iDCRateTableCellEditor.stopCellEditing();
        int rowIndex = iDCRatesForm.tblIDCRates.getSelectedRow();
        if (rowIndex==-1) {
            CoeusOptionPane.showErrorDialog(SELECT_A_ROW);
            return;
        }
        if(rowIndex != -1 && rowIndex >= 0){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            //Are you sure you want to delete this IDC Rate row?
            coeusMessageResources.parseMessageKey("instPropIDCRates_exceptionCode.1256"),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                InstituteProposalIDCRateBean deletedIDCRateBean = (InstituteProposalIDCRateBean)cvTableData.get(rowIndex);
                cvDeletedData.add(deletedIDCRateBean);
                if (deletedIDCRateBean.getAcType() == null ||
                deletedIDCRateBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedData.add(deletedIDCRateBean);
                }
                if(cvTableData!=null && cvTableData.size() > 0){
                    cvTableData.remove(rowIndex);
                    iDCRateTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    modified = true;
                    deletedIDCRateBean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(rowIndex >0){
                    iDCRatesForm.tblIDCRates.setColumnSelectionInterval(0,0);
                    iDCRatesForm.tblIDCRates.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    iDCRatesForm.tblIDCRates.scrollRectToVisible(
                    iDCRatesForm.tblIDCRates.getCellRect(
                    rowIndex-1 ,0, true));
                }else{
                    if(iDCRatesForm.tblIDCRates.getRowCount()>0){
                        iDCRatesForm.tblIDCRates.setColumnSelectionInterval(0,0);
                        iDCRatesForm.tblIDCRates.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
        if (cvTableData.size()<1) {
            iDCRatesForm.tblAdd.setVisible(false);
        }
    }
    
    
    /**
     * Table model for IDC Rates table
     */
    public class IDCRateTableModel extends AbstractTableModel {
        private String colName[] = {"Rate","Type","Fiscal Year","Campus","Underrecovery","Source Account"};
        private Class colClass[] = {Double.class, String.class, String.class, String.class,Double.class, String.class};
        
        /**
         * returns true if the cell is editable, else returns false
         */
        public boolean isCellEditable(int row, int col) {
            if (functionType==DISPLAY_PROPOSAL) {
                return false;
            } else {
                return true;
            }
            
        }
        /**
         * returns the column class
         */
        public  Class getColumnClass(int col){
            return colClass[col];
        }
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colName.length;
        }
        /**
         * sets the data for the table
         */
        public void setData(CoeusVector data){
            cvTableData = data;
            // Bug Fix #2070
            fireTableDataChanged();
        }
        /**
         * Returns number of rows
         */
        public int getRowCount() {
            if(cvTableData== null){
                return 0;
            }else{
                return cvTableData.size();
            }
            
        }
        /**
         * Returns value at cell
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            InstituteProposalIDCRateBean instituteProposalIDCRateBean= (InstituteProposalIDCRateBean)cvTableData.elementAt(rowIndex);
            
            switch(columnIndex){
                case RATE_COLUMN:
                    return new Double(instituteProposalIDCRateBean.getApplicableIDCRate());
                case TYPE_COLUMN:
                    int typeCode=instituteProposalIDCRateBean.getIdcRateTypeCode();
                    CoeusVector filteredVector = cvRateType.filter(new Equals("code",""+typeCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = null;
                        comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }
                case FISCAL_YEAR_COLUMN:
                    return new String(instituteProposalIDCRateBean.getFiscalYear());
                case CAMPUS_COLUMN:
                    ComboBoxBean comboBoxBean=new ComboBoxBean();
                    if (instituteProposalIDCRateBean.isOnOffCampusFlag()) {
                        comboBoxBean.setCode("0");
                        comboBoxBean.setDescription(ON);
                        return comboBoxBean;
                    }
                    else {
                        comboBoxBean.setCode("1");
                        comboBoxBean.setDescription(OFF);
                        return comboBoxBean;
                    }
                case UNDERRECOVERY_COLUMN:
                    return new Double(instituteProposalIDCRateBean.getUnderRecoveryIDC());
                case SOURCE_ACCOUNT_COLUMN:
                    if(instituteProposalIDCRateBean.getSourceAccount()!= null){
                        return instituteProposalIDCRateBean.getSourceAccount();
                    }
                default:
                    return EMPTY_STRING;
                    
            }
            
        }
        /**
         * sets valus for a table cell
         */
        public void setValueAt(Object value, int row, int col) {
            InstituteProposalIDCRateBean instituteProposalIDCRateBean=
            (InstituteProposalIDCRateBean)cvTableData.elementAt(row);
            double cost = 0.00;
            
            switch(col) {
                case RATE_COLUMN:
                    double oldRate=instituteProposalIDCRateBean.getApplicableIDCRate();
                    if (oldRate!=Double.parseDouble(value.toString())) {
                        instituteProposalIDCRateBean.setApplicableIDCRate(Double.parseDouble(value.toString()));
                        modified=true;
                    }
                    break;
                case TYPE_COLUMN:
                    // Added by chandra to fix #934 - 2nd August 2004
                    int typeCode = -1;
                    if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)cvRateType.filter(new Equals("description", value.toString())).get(0);
                        typeCode = Integer.parseInt(comboBoxBean.getCode());
                        if( typeCode != instituteProposalIDCRateBean.getIdcRateTypeCode()){
                            instituteProposalIDCRateBean.setIdcRateTypeCode(typeCode);
                            modified = true;
                        }
                    }else{
                        instituteProposalIDCRateBean.setIdcRateTypeCode(typeCode);
                    }
                    break;
                case FISCAL_YEAR_COLUMN:
                    if (!value.toString().trim().equals(instituteProposalIDCRateBean.getFiscalYear().trim())) {
                        instituteProposalIDCRateBean.setFiscalYear(value.toString());
                        modified = true;
                    }
                    break;
                case CAMPUS_COLUMN:
                    
                    if (ON.equals(value.toString())){
                        if (!instituteProposalIDCRateBean.isOnOffCampusFlag()) {
                            modified = true;
                            instituteProposalIDCRateBean.setOnOffCampusFlag(true);
                        }
                    }
                    else {
                        if (instituteProposalIDCRateBean.isOnOffCampusFlag()) {
                            instituteProposalIDCRateBean.setOnOffCampusFlag(false);
                            modified=true;
                        }
                    }
                    
                    break;
                case UNDERRECOVERY_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    if (cost!=instituteProposalIDCRateBean.getUnderRecoveryIDC()){
                        instituteProposalIDCRateBean.setUnderRecoveryIDC(cost);
                        modified = true;
                    }
                    amountTabelModel.fireTableDataChanged();
                    break;
                case SOURCE_ACCOUNT_COLUMN:
                    if (value.toString().trim().equals(instituteProposalIDCRateBean.getSourceAccount().trim())) {
                        break;
                    } else {
                        instituteProposalIDCRateBean.setSourceAccount(value.toString());
                        modified = true;
                    }
                    break;
            }
            
            if(instituteProposalIDCRateBean.getAcType()==null){
                instituteProposalIDCRateBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        /**
         * returns the column name
         */
        public String getColumnName(int col) {
            return colName[col];
        }
    }
    
    /**
     * Table cell editor for IDC Rates table
     */
    public class IDCRateTableCellEditor extends AbstractCellEditor
    implements TableCellEditor {
        private CurrencyField txtRate;
        private DollarCurrencyTextField txtUnderrecovery;
        private CoeusComboBox cmbRateType;
        private CoeusComboBox cmbCampus;
        private CoeusTextField txtComponent;
        private int column;
        
        /**
         * Creates a cell editor for IDC Rates table
         */
        public IDCRateTableCellEditor() {
            txtRate = new CurrencyField();
            txtUnderrecovery = new DollarCurrencyTextField(12,JTextField.RIGHT,true);
            cmbRateType = new CoeusComboBox();
            //cmbRateType.setModel(new DefaultComboBoxModel(cvRateType));
            cmbCampus = new CoeusComboBox();
            txtComponent = new CoeusTextField();
        }
        private void populateTypeCombo() {
            int size = cvRateType.size();
            ComboBoxBean comboBoxBean;
            // Added by chandra to fix #934 - Start - 2nd August 2004
            cmbRateType.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            // Added by chandra to fix #934 - End - 2nd August 2004
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)cvRateType.get(index);
                cmbRateType.addItem(comboBoxBean);
            }
            
        }
        
        private void populateCampusCombo() {
            ComboBoxBean comboBoxBean=new ComboBoxBean();
            
            comboBoxBean.setCode("0");
            comboBoxBean.setDescription(ON);
            cmbCampus.addItem(comboBoxBean);
            comboBoxBean=null;
            comboBoxBean=new ComboBoxBean();
            comboBoxBean.setCode("1");
            comboBoxBean.setDescription(OFF);
            cmbCampus.addItem(comboBoxBean);
        }
        /**
         * REturns the cell editor component
         */
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table,
        Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case RATE_COLUMN:
                    txtRate.setText(value.toString());
                    if (isSelected) {
                        txtRate.setBackground(java.awt.Color.yellow);
                    }
                    else {
                        txtRate.setBackground(java.awt.Color.white);
                    }
                    return txtRate;
                case TYPE_COLUMN:
                    if(! typeComboPopulated) {
                        populateTypeCombo();
                        typeComboPopulated = true;
                    }
                    if (isSelected) {
                        cmbRateType.setBackground(java.awt.Color.yellow);
                    }
                    else {
                        cmbRateType.setBackground(java.awt.Color.white);
                    }
                    cmbRateType.setSelectedItem(value);
                    return cmbRateType;
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
                case CAMPUS_COLUMN:
                    if(! campusComboPopulated) {
                        populateCampusCombo();
                        campusComboPopulated = true;
                    }
                    cmbCampus.setSelectedItem(value);
                    if (isSelected) {
                        cmbCampus.setBackground(java.awt.Color.yellow);
                    }
                    else {
                        cmbCampus.setBackground(java.awt.Color.white);
                    }
                    return cmbCampus;
                case UNDERRECOVERY_COLUMN:
                    txtUnderrecovery.setValue(new Double(value.toString()).doubleValue());
                    if (isSelected) {
                        txtUnderrecovery.setBackground(java.awt.Color.yellow);
                    }
                    else {
                        txtUnderrecovery.setBackground(java.awt.Color.white);
                    }
                    return txtUnderrecovery;
                case SOURCE_ACCOUNT_COLUMN:
                    //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
                    //Modified by shiji for bug fix id 1730 - start
//                    txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,7));
                    //bug fix id 1730 - end
//                    txtComponent.setText(value.toString());
                    
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
            return txtUnderrecovery;
        }
        
        /**
         * Returns the cell editor value
         */
        public Object getCellEditorValue() {
            switch(column){
                case RATE_COLUMN:
                    return txtRate.getText();
                case TYPE_COLUMN:
                    return cmbRateType.getSelectedItem();
                case CAMPUS_COLUMN:
                    return cmbCampus.getSelectedItem();
                case FISCAL_YEAR_COLUMN:
                case SOURCE_ACCOUNT_COLUMN:
                    return txtComponent.getText();
                case UNDERRECOVERY_COLUMN:
                    return txtUnderrecovery.getValue();
                    
            }
            return cmbRateType;
        }
    }
    
    /**
     * Table cell renderer for IDC Rates table
     */
    public class IDCRateTableCellRenderer extends DefaultTableCellRenderer {
        private CurrencyField txtRate;
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyField;
        private JLabel lblText,lblCurrency,lblRate;
        
        /**
         * Cretas a table cell renderer for IDC Rates table
         */
        public IDCRateTableCellRenderer(){
            lblText =  new JLabel();
            lblCurrency = new JLabel();
            lblRate = new JLabel();
            lblCurrency.setOpaque(true);
            lblText.setOpaque(true);
            lblRate.setOpaque(true);
            lblRate.setHorizontalAlignment(JLabel.RIGHT);
            lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
            txtRate = new CurrencyField();
            txtRate.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtComponent = new JTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyField=new DollarCurrencyTextField(12,JTextField.RIGHT,true);
            txtCurrencyField.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            
        }
        
        /**
         * Returns table cell renderer component
         */
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,Object value,
        boolean isSelected,boolean hasFocus,int row,int col) {
            switch(col){
                case RATE_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblRate.setBackground(disabledBackground);
                        lblRate.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblRate.setBackground(java.awt.Color.YELLOW);
                        lblRate.setForeground(java.awt.Color.black);
                    }else{
                        lblRate.setBackground(java.awt.Color.white);
                        lblRate.setForeground(java.awt.Color.black);
                    }
                    txtRate.setText(value.toString());
                    lblRate.setText(txtRate.getText());
                    return lblRate;
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
                        
                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                            txtComponent.setText(EMPTY_STRING);
                            lblText.setText(txtComponent.getText());
                        }else{
                            txtComponent.setText(value.toString());
                            lblText.setText(txtComponent.getText());
                        }
                    return lblText;
                case CAMPUS_COLUMN:
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
                        
                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                            txtComponent.setText(EMPTY_STRING);
                            lblText.setText(txtComponent.getText());
                        }else{
                            txtComponent.setText(value.toString());
                            lblText.setText(txtComponent.getText());
                        }
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
                        
                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                            txtComponent.setText(EMPTY_STRING);
                            lblText.setText(txtComponent.getText());
                        }else{
                            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
                            //Checks if SourceAccountNumber is greater than account number length from parameter, 
                            //then SourceAccountNumber is substring to accountNumLength
//                            txtComponent.setText(value.toString());
                            String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                            if(sourceAccountNumber.length() > accountNumberMaxLength){
                                sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                            }
                            txtComponent.setText(sourceAccountNumber);
                            //Case#2402 - End
                            lblText.setText(txtComponent.getText());
                        }
                    return lblText;

                case UNDERRECOVERY_COLUMN:
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
     * Table model for amount table
     */
    public class AmountTabelModel extends AbstractTableModel {
        private String colName[] = {"Total: ", ""};
        private Class colClass[] = {String.class, Double.class};
        
        /**
         * Returns true if the cell is editable, else returns false
         */
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * returns the number of columns
         */
        public int getColumnCount(){
            return colName.length;
        }
        
        /**
         * returns the column class for that column
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
         * sets data for the table
         */
        public void setData(CoeusVector cvTableData){
            cvTableData = cvTableData;
        }
        /**
         * retuns column name for the column
         */
        public String getColumnName(int column){
            return colName[column];
        }
        
        /**
         * Gets the value at a cell
         */
        public Object getValueAt(int row, int col) {
            double totalAmount = 0.00;
            String name = "Total: ";
            if(col==TOTAL_COLUMN){
                return name;
            }
            if(col==TOTAL_AMOUNT_COLUMN){
                totalAmount = cvTableData.sum("underRecoveryIDC");
                return new Double(totalAmount);
            }
            return EMPTY_STRING;
        }
    }
    
    
    /**
     * Table cell renderter for Amount table
     */
    public class AmountTableCellRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer {
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        private JLabel lblText,lblCurrency;
        
        /**
         * creates a renederer for the Amoount table
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
         * returns the table cell renderer component
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
        
        javax.swing.InputMap im = iDCRatesForm.tblIDCRates.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = iDCRatesForm.tblIDCRates.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = iDCRatesForm.tblIDCRates.getRowCount();
                int columnCount = iDCRatesForm.tblIDCRates.getColumnCount();
                 if(row==rowCount-1 && column==columnCount-1){
                     row = 0;
                     column = 0;
                     iDCRateTableCellEditor.stopCellEditing();
                        iDCRatesForm.txtArComments.requestFocusInWindow();
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
        iDCRatesForm.tblIDCRates.getActionMap().put(im.get(tab), tabAction);
        
        
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = iDCRatesForm.tblIDCRates.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                column = iDCRatesForm.tblIDCRates.getSelectedColumn();
                row = iDCRatesForm.tblIDCRates.getSelectedRow();
                int rowCount = iDCRatesForm.tblIDCRates.getRowCount();
                int columnCount = iDCRatesForm.tblIDCRates.getColumnCount();
                 if(row==0 && column==0){
                     row = 0;
                     column = 0;
                     iDCRateTableCellEditor.stopCellEditing();
                        iDCRatesForm.txtArComments.requestFocusInWindow();
                        return;
                    }
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                
//                while (table.isCellEditable(row, column) ) {
//                    if (column <= 0) {
//                        column = 5;
//                        row -=1;
//                    }
//                    
//                    if (row < 0) {
//                        row = rowCount-1;
//                    }
//                    // Back to where we started, get out.
//                    
//                    if (row == table.getSelectedRow()
//                    && column == table.getSelectedColumn()) {
//                        break;
//                    }
//                }
//                table.changeSelection(row, column, false, false);
            }
        };
        iDCRatesForm.tblIDCRates.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
}
