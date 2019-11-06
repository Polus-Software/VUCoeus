/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * SubcontractCloseoutController.java
 *
 * Created on September 7, 2004, 9:57 AM
 */

package edu.mit.coeus.subcontract.controller;

/**
 *
 * @author  nadhgj
 */

import javax.swing.JTable;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListSelectionModel;
import java.awt.event.*;
import java.awt.*;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.util.*;
import java.text.ParseException;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.subcontract.gui.SubcontractCloseoutForm;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusMessageResources;


public class SubcontractCloseoutController extends SubcontractController
implements ActionListener , MouseListener, ListSelectionListener , FocusListener{
            
    //Represents the column header names for the Closeout table
    private static final int CLOSEOUT_TYPE_COLUMN = 0;
    private static final int DATE_REQUESTED_COLUMN = 1;
    private static final int DATE_FOLLOWUP_COLUMN = 2;
    private static final int DATE_RECEIVED_COLUMN=3;
    private static final String EMPTY_STRING="";
    private SubcontractCloseoutForm subcontractCloseoutForm;
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private DateUtils dtUtils = new DateUtils();
    private CloseOutTableCellEditor closeOutTableCellEditor =null;
    private CloseOutTableRenderer closeOutTableRenderer = null;
    private CloseoutTableModel closeoutTableModel =null;
    private  static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    /*the date format on editing*/
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    /*date saperators*/
    private static final String DATE_SEPARATERS = ":/.,|-";
    private SubcontractCloseoutBean subcontractCloseoutBean;
    private char functionType;
    
    private CoeusVector cvCloseoutType = new CoeusVector();
    private CoeusVector cvCloseoutData = new CoeusVector();
    private CoeusVector cvDelCloseoutData = new CoeusVector();
    private Date date;
    private Calendar calendar = Calendar.getInstance();
    private boolean modified = false;
    private boolean canGetCloseoutNum=false;
    private int selRow;
    private int lstSelRow;
    private int closeoutNum;
    // message for delete conformation
    private static final String DELETE_CONFRIMATION = "subcontractCloseout_exceptionCode.1302";
    // Please select a closeout row to delete.
    private static final String ROW_SELECTION_CONFRIMATION = "subcontractCloseout_exceptionCode.1301";
    // Please select a closeout type.
    private static final String CLOSEOUT_TYPE_SELECTION_CONFRIMATION = "subcontractCloseout_exceptionCode.1303";
    // Please enter a requested date.
    private static final String REQUESTED_DATE_SELECTION_CONFRIMATION = "subcontractCloseout_exceptionCode.1304";
    // Please enter a valid Requested Date.
    private static final String VALID_REQUESTED_DATE_CONFRIMATION = "subcontractCloseout_exceptionCode.1305";
    // Please enter a valid Followup Date.
    private static final String VALID_FOLLOWUP_DATE_CONFRIMATION = "subcontractCloseout_exceptionCode.1306";
    // Please enter a valid Received Date.
    private static final String VALID_RECEIVED_DATE_CONFRIMATION = "subcontractCloseout_exceptionCode.1307";
    
    private CoeusMessageResources coeusMessageResources;
    private SubContractBean subContractBean;
    private boolean msgDisplay = true;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
    
    /* JM 2-27-2015 modifications to allow tab access if user has only this right */
    private boolean userHasModify = false;
    private boolean userHasCreate = false;
    /* JM END */
    
    /** Creates a new instance of SubcontractCloseoutController */
    public SubcontractCloseoutController(SubContractBean subContractBean, char functionType) {
        super(subContractBean);
        this.subContractBean = subContractBean;
        
		/* JM 2-27-2015 no access if not modifier */
        userHasModify = subContractBean.getHasModify();
        userHasCreate = subContractBean.getHasCreate();
		if (!userHasModify && !userHasCreate) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */ 
        
        this.functionType = functionType;
        subcontractCloseoutForm = new SubcontractCloseoutForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        formatFields();
        registerComponents();
        setColumnData();
        setFormData(null);
    }
    
    
    
    /* Setting the table header, column width, renderer and editor for the tables 
     * returns void
     */
    private void setColumnData() {
        JTableHeader tableHeader = subcontractCloseoutForm.tblCloseout.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        subcontractCloseoutForm.tblCloseout.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        subcontractCloseoutForm.tblCloseout.setRowHeight(22);
        subcontractCloseoutForm.tblCloseout.setOpaque(false);
        subcontractCloseoutForm.tblCloseout.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = subcontractCloseoutForm.tblCloseout.getColumnModel().getColumn(CLOSEOUT_TYPE_COLUMN);
        column.setPreferredWidth(300);
        column.setResizable(true);
        column.setCellRenderer(closeOutTableRenderer);
        column.setCellEditor(closeOutTableCellEditor);
        
        column = subcontractCloseoutForm.tblCloseout.getColumnModel().getColumn(DATE_REQUESTED_COLUMN);
        column.setPreferredWidth(150);
        column.setResizable(true);
        column.setCellRenderer(closeOutTableRenderer);
        column.setCellEditor(closeOutTableCellEditor);
        
        column = subcontractCloseoutForm.tblCloseout.getColumnModel().getColumn(DATE_FOLLOWUP_COLUMN);
        column.setPreferredWidth(150);
        column.setResizable(true);
        column.setCellRenderer(closeOutTableRenderer);
        column.setCellEditor(closeOutTableCellEditor);
        
        column = subcontractCloseoutForm.tblCloseout.getColumnModel().getColumn(DATE_RECEIVED_COLUMN);
        column.setPreferredWidth(150);
        column.setResizable(true);  
        column.setCellRenderer(closeOutTableRenderer);
        column.setCellEditor(closeOutTableCellEditor);
        
    }// end of setting 
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        int getSelRow = subcontractCloseoutForm.tblCloseout.getSelectedRow();
        if(source.equals(subcontractCloseoutForm.btnAdd)) {
            performAddAction();
        }
        if(source.equals(subcontractCloseoutForm.btnDelete)) {
            performDeleteAction(getSelRow);
        }
    }    
    
    public void mouseClicked(MouseEvent e) {
    }    
    
    public void mouseEntered(MouseEvent e) {
    }    
    
    public void mouseExited(MouseEvent e) {
    }    
    
    public void mousePressed(MouseEvent e) {
    }    
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void display() {
    }
    
    public void formatFields() {
        if(functionType == TypeConstants.DISPLAY_MODE){
            subcontractCloseoutForm.txtComments.setBackground(disabledBackground);
        }
    }
    
    
    public Component getControlledUI() {
        return subcontractCloseoutForm;
    }
    
    public Object getFormData() {
        return new Object();
    }
    
    /*this method adds a new row to the table
     *@returns void
     */
    private void performAddAction() {
        closeOutTableCellEditor.stopCellEditing();
        if(cvCloseoutData != null && cvCloseoutData.size() > 0) {
            subcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(subcontractCloseoutForm.tblCloseout.getRowCount()-1);
            if(subcontractCloseoutBean.getCloseoutTypeCode() == 0 ||  subcontractCloseoutBean.getDateRequested() == null ||
                    subcontractCloseoutBean.getDateRequested().toString().trim().equals(EMPTY_STRING) ){
                return;
            }
        }
        modified = true;
        SubcontractCloseoutBean newSubcontractCloseoutBean = new SubcontractCloseoutBean();
        newSubcontractCloseoutBean.setAcType(TypeConstants.INSERT_RECORD);
        newSubcontractCloseoutBean.setCloseoutTypeCode(0);
        newSubcontractCloseoutBean.setComment(EMPTY_STRING);
        newSubcontractCloseoutBean.setSubContractCode(subContractBean.getSubContractCode());
        newSubcontractCloseoutBean.setSequenceNumber(subContractBean.getSequenceNumber());
        closeoutNum = closeoutNum+1;
        newSubcontractCloseoutBean.setCloseoutNumber(closeoutNum);
        try {
            calendar.setTime(new Date());
            newSubcontractCloseoutBean.setDateRequested(new java.sql.Date(dtFormat.parse(dtFormat.format(calendar.getTime())).getTime()));
            calendar.add(Calendar.DATE, 42);
            date = dtFormat.parse(dtFormat.format(calendar.getTime()));
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            newSubcontractCloseoutBean.setDateFollowUp(sqlDate);
            newSubcontractCloseoutBean.setDateReceived(null);
            newSubcontractCloseoutBean.setComment("");
        }catch(ParseException parseException) {
            parseException.printStackTrace();
        }
        cvCloseoutData = closeoutTableModel.getData();
        if(cvCloseoutData == null) {
            cvCloseoutData = new CoeusVector();
        }
        cvCloseoutData.add(newSubcontractCloseoutBean);
        closeoutTableModel.fireTableRowsInserted(closeoutTableModel.getRowCount()+1, closeoutTableModel.getRowCount()+1);
        int lstRow = subcontractCloseoutForm.tblCloseout.getRowCount()-1;
        if(lstRow >=0) {
            subcontractCloseoutForm.tblCloseout.setRowSelectionInterval(lstRow, lstRow);
            subcontractCloseoutForm.tblCloseout.scrollRectToVisible(
            subcontractCloseoutForm.tblCloseout.getCellRect(lstRow, CLOSEOUT_TYPE_COLUMN, true));
        }
        subcontractCloseoutForm.tblCloseout.requestFocusInWindow();
        subcontractCloseoutForm.tblCloseout.editCellAt(lstRow,CLOSEOUT_TYPE_COLUMN);
        subcontractCloseoutForm.tblCloseout.getEditorComponent().requestFocusInWindow();
    }
    
    /*this method removes selected row from the table
     *@returns void
     */
    
    private void performDeleteAction(int getSelRow) {
        closeOutTableCellEditor.cancelCellEditing();
        closeOutTableCellEditor.stopCellEditing();
        SubcontractCloseoutBean delSubcontractCloseoutBean = new SubcontractCloseoutBean();
        
        if(getSelRow == -1) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ROW_SELECTION_CONFRIMATION));
            return;
        }
        int optionSelected = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_CONFRIMATION),
                                            CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
        if(optionSelected == CoeusOptionPane.SELECTION_YES) {
            modified = true;
            delSubcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(getSelRow);
            
            if(delSubcontractCloseoutBean.getAcType() == null ||
                delSubcontractCloseoutBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                delSubcontractCloseoutBean.setAcType(TypeConstants.DELETE_RECORD);
                cvDelCloseoutData.add(delSubcontractCloseoutBean);
            }
            
		lstSelRow = 0;
                subcontractCloseoutForm.txtComments.setText(EMPTY_STRING);
                closeoutTableModel.fireTableRowsDeleted(getSelRow, getSelRow);
                cvCloseoutData.remove(getSelRow);
                if(cvCloseoutData != null && cvCloseoutData.size() >0) {
                    SubcontractCloseoutBean bean = (SubcontractCloseoutBean)cvCloseoutData.get(lstSelRow);
                    subcontractCloseoutForm.txtComments.setText((bean.getComment() == null) ? EMPTY_STRING : bean.getComment());
                    subcontractCloseoutForm.tblCloseout.setRowSelectionInterval(0,0);
                    subcontractCloseoutForm.tblCloseout.scrollRectToVisible(
                    subcontractCloseoutForm.tblCloseout.getCellRect(0,0, true));
                    subcontractCloseoutForm.tblCloseout.requestFocusInWindow();
                    subcontractCloseoutForm.tblCloseout.editCellAt(0,CLOSEOUT_TYPE_COLUMN);
                    subcontractCloseoutForm.tblCloseout.getEditorComponent().requestFocusInWindow();
                    subcontractCloseoutForm.tblCloseout.setRowSelectionInterval(0,0);
                }
        }
    }
    
    /*this method register components and listeners
     *@returns void
     */
    public void registerComponents() {
        subcontractCloseoutForm.tblCloseout.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subcontractCloseoutForm.tblCloseout.getSelectionModel().addListSelectionListener(this);
        closeoutTableModel = new CloseoutTableModel();
        closeOutTableCellEditor =new CloseOutTableCellEditor();
        closeOutTableRenderer = new CloseOutTableRenderer();
        subcontractCloseoutForm.tblCloseout.setModel(closeoutTableModel);
        subcontractCloseoutForm.txtComments.addFocusListener(this);
        subcontractCloseoutForm.tblCloseout.setFont(CoeusFontFactory.getNormalFont());
        if(functionType == DISPLAY_SUBCONTRACT) {
            subcontractCloseoutForm.btnAdd.setEnabled(false);
            subcontractCloseoutForm.btnDelete.setEnabled(false);
        }else{
            subcontractCloseoutForm.btnAdd.addActionListener(this);
            subcontractCloseoutForm.btnDelete.addActionListener(this);
        }
    }
    
    /** saves the Form Data.
     *@ returns void
     */
    
    public void saveFormData() {
        msgDisplay = false;
        closeOutTableCellEditor.stopCellEditing();
        CoeusVector cvSaveCloseout = new CoeusVector();
        int row = subcontractCloseoutForm.tblCloseout.getSelectedRow();
            if(row != -1) {
            subcontractCloseoutBean = (SubcontractCloseoutBean) cvCloseoutData.get(row);
			
            if(subcontractCloseoutForm.txtComments.getText() == null){
                    subcontractCloseoutForm.txtComments.setText(EMPTY_STRING);
            }
            if(subcontractCloseoutBean.getComment() != null && !subcontractCloseoutBean.getComment().equals(subcontractCloseoutForm.txtComments.getText())) {
                subcontractCloseoutBean.setComment(subcontractCloseoutForm.txtComments.getText());
                modified = true;
                if(subcontractCloseoutBean.getAcType() == null) {
                    subcontractCloseoutBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        try {
            if(modified) {
                if(cvDelCloseoutData != null && cvDelCloseoutData.size() > 0) {
                    cvSaveCloseout.addAll(cvDelCloseoutData);
                }
                if(cvCloseoutData != null && cvCloseoutData.size() > 0) {
                    cvSaveCloseout.addAll(cvCloseoutData);
                }

                if(cvSaveCloseout != null && cvSaveCloseout.size() > 0) {
                    for( int index =0; index<cvSaveCloseout.size(); index++) {
                        SubcontractCloseoutBean savingBean = (SubcontractCloseoutBean)cvSaveCloseout.get(index);
                        if(savingBean.getAcType() != null) {
                            if(savingBean.getAcType() == TypeConstants.UPDATE_RECORD) {
                                savingBean.setAcType(TypeConstants.UPDATE_RECORD);
                                queryEngine.update(queryKey,savingBean);
                            }else if(savingBean.getAcType() == TypeConstants.DELETE_RECORD) {
                                savingBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey,savingBean);
                            }else if(savingBean.getAcType() == TypeConstants.INSERT_RECORD) {
                                savingBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey,savingBean);
                            }
                        }
                    }
                }
                modified = false;
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }finally {
            modified = false;
        }
    }
    
    /** This method is used to set the form data specified in
     * @param data to set to the form
     * @returns void
     */
    public void setFormData(Object data) {
        try {
            cvCloseoutData = queryEngine.getDetails(queryKey , SubcontractCloseoutBean.class);
            if(!canGetCloseoutNum) {
                CoeusVector cvCloseoutDataForSort = queryEngine.getDetails(queryKey , SubcontractCloseoutBean.class);
                if(cvCloseoutDataForSort != null && cvCloseoutDataForSort.size() >0) {
                cvCloseoutDataForSort.sort("closeoutNumber",false);
                SubcontractCloseoutBean sortedBean = (SubcontractCloseoutBean)cvCloseoutDataForSort.get(0);
                closeoutNum = sortedBean.getCloseoutNumber();
                canGetCloseoutNum = true;
                }
            }
            cvCloseoutType = queryEngine.getDetails(queryKey,KeyConstants.CLOSEOUT_TYPES);
            lstSelRow = 0;
            closeoutTableModel.setData(cvCloseoutData);
            closeoutTableModel.fireTableDataChanged();
            if(cvCloseoutData != null && cvCloseoutData.size() > 0){
                subcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(0);
                subcontractCloseoutForm.txtComments.setText(subcontractCloseoutBean.getComment());
                subcontractCloseoutForm.tblCloseout.setRowSelectionInterval(0,0);
                subcontractCloseoutForm.tblCloseout.setColumnSelectionInterval(0,0);
                
            }
            
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** validate the form data
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return false if fails else true 
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        closeOutTableCellEditor.stopCellEditing();
        cvCloseoutData = closeoutTableModel.getData();
        for(int index = 0; index < cvCloseoutData.size(); index++) {
            subcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(index);
            if(subcontractCloseoutBean.getDateRequested() == null || subcontractCloseoutBean.getDateRequested().toString() == EMPTY_STRING) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(REQUESTED_DATE_SELECTION_CONFRIMATION));
                setRequestFocusInCloseoutThread(index, DATE_REQUESTED_COLUMN);
                return false;
            }
            if(dtUtils.restoreDate(subcontractCloseoutBean.getDateRequested().toString(), DATE_SEPARATERS) == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_REQUESTED_DATE_CONFRIMATION));
                setRequestFocusInCloseoutThread(index, DATE_REQUESTED_COLUMN);
                return false;
            }
            if(subcontractCloseoutBean.getCloseoutTypeCode() == 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CLOSEOUT_TYPE_SELECTION_CONFRIMATION));
                setRequestFocusInCloseoutThread(index, CLOSEOUT_TYPE_COLUMN);
                return false;
            }
        }
        
        return true;
    }
    
    /** this method cleans up the not reachble resources this hapens on form close
     * @returns void.
     */
    
    public void cleanUp(){
        dtUtils = null;
        disabledBackground = null;
        dtFormat = null;
        date = null;
        subcontractCloseoutBean = null;
        subContractBean =null;
        coeusMessageResources = null;
        closeOutTableCellEditor = null;
        closeOutTableRenderer = null;
        closeoutTableModel = null;
        cvCloseoutData = null;
        cvCloseoutType = null;
        cvDelCloseoutData = null;
        calendar = null;
        subcontractCloseoutForm = null;
    }
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting() ){
            closeOutTableCellEditor.stopCellEditing();
            Object source = listSelectionEvent.getSource();
            selRow = subcontractCloseoutForm.tblCloseout.getSelectedRow();
			if( (source.equals(subcontractCloseoutForm.tblCloseout.getSelectionModel()) )&& (selRow >= 0 ) && 
            cvCloseoutData !=null && cvCloseoutData.size() > 0) {
                subcontractCloseoutBean = null;
                subcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(lstSelRow);
                
  			if (subcontractCloseoutBean.getComment() == null ) {
				   subcontractCloseoutBean.setComment(EMPTY_STRING);
			}
                if(!subcontractCloseoutForm.txtComments.getText().equals(subcontractCloseoutBean.getComment())) {
                    subcontractCloseoutBean.setComment(subcontractCloseoutForm.txtComments.getText());
                    modified = true;
                    if(subcontractCloseoutBean.getAcType()==null){
                        subcontractCloseoutBean.setAcType(TypeConstants.UPDATE_RECORD);

                    }
                }
			lstSelRow = selRow;
			//Setting text to last selected Row - End
			subcontractCloseoutBean = null;
            subcontractCloseoutBean = (SubcontractCloseoutBean) cvCloseoutData.get(selRow);
			if (subcontractCloseoutBean.getComment() == null ) {
				   subcontractCloseoutBean.setComment(EMPTY_STRING);
			}
            subcontractCloseoutForm.txtComments.setText(subcontractCloseoutBean.getComment()) ;
            subcontractCloseoutForm.txtComments.setCaretPosition(0);
                
            }
        }
    }
    public void focusGained(FocusEvent focusEvent) {
        
    }
    
    /** this method sets focus back to component
     * @return void
     */
    private void setRequestFocusInCloseoutThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            subcontractCloseoutForm.tblCloseout.requestFocusInWindow();
            subcontractCloseoutForm.tblCloseout.changeSelection( selrow, selcol, true, false);
            subcontractCloseoutForm.tblCloseout.setRowSelectionInterval(selrow, selrow);
        }
        });
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
            setFormData(subContractBean);
            setRefreshRequired(false);
        }
    }
    
    public void focusLost(FocusEvent focusEvent) {
    }
    
    /**
     * Table model for the closeout table
     */    
    public class CloseoutTableModel extends AbstractTableModel {
       
    private CoeusVector cvCloseoutData = new CoeusVector();
    private String[] colNames = {"Closeout Type", "Date Requested","Date Followup","Date Received"};
    /** Specifies the column class and its data types as objects */
    private Class colClass[] = {String.class, Date.class, Date.class, Date.class};
    boolean msgDisplayed;
    /**
     * Creates a new instance of CloseoutTableModel
     */
    public CloseoutTableModel() {
    }
    /**
     *This method is to check whether the specified cell is editable or not
     *@param int row
     *@param int col
     *@return boolean
     */
    public boolean isCellEditable(int row, int col) {
      return (functionType == TypeConstants.DISPLAY_MODE ? false : true);
    }
    /**
     *This method is to get the column name
     *@param int column
     *@return String
     */
    public String getColumnName(int column) {
        return colNames[column];
    }
    
    /**
     *This method is to get the column class
     *@param int columnIndex
     *@return Class
     */
    public Class getColumnClass(int columnIndex) {
        return colClass [columnIndex];
    }
    
    /**
     *This method is to get the column count
     *@return int
     */
    public int getColumnCount() {
        return colNames.length;
    }
    
    /**
     *This method is to get the row count
     *@return int
     */
    public int getRowCount() {
        if (cvCloseoutData == null) return 0;
        return cvCloseoutData.size();
    }
    
    /**
     *This method will specifies the data for the table model. Depending upon the value
     *passed, it will hold the Award or Institute Proposal Detail vestor
     *@param CoeusVector cvData
     *@return void
     */
    public void setData(CoeusVector cvData){
        cvCloseoutData = cvData;
    }
    
    /**
     *This method is to get the value with respect to the row and column
     *@param int row
     *@param int col
     *@return Object
     */
    public Object getValueAt(int row, int col) {
        subcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(row);
        switch(col) {
            case CLOSEOUT_TYPE_COLUMN:
                int typeCode = subcontractCloseoutBean.getCloseoutTypeCode();
                CoeusVector filteredVector = cvCloseoutType.filter(new Equals("code",""+typeCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = null;
                        comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }
            case DATE_REQUESTED_COLUMN:
                if(subcontractCloseoutBean.getDateRequested() != null)
                    return subcontractCloseoutBean.getDateRequested().toString();
                else
                    return EMPTY_STRING;
            case DATE_FOLLOWUP_COLUMN:
                if(subcontractCloseoutBean.getDateFollowUp() != null)
                    return subcontractCloseoutBean.getDateFollowUp().toString();
                 else
                    return EMPTY_STRING;
            case DATE_RECEIVED_COLUMN:
                if(subcontractCloseoutBean.getDateReceived() != null)
                    return subcontractCloseoutBean.getDateReceived().toString();
                 else
                    return EMPTY_STRING;
        }
        return EMPTY_STRING;
    }
    
    /**
     *This method is to set the value with respect to the row and column
     *@param Object value
     *@param int row
     *@param int col
     *@return void
     */
    public void setValueAt(Object value, int row, int col) {
        SubcontractCloseoutBean newBean = (SubcontractCloseoutBean)cvCloseoutData.get(row);
        String acType = newBean.getAcType();
        switch(col) {
            case CLOSEOUT_TYPE_COLUMN:
                if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvCloseoutType.filter(new Equals("description", value.toString())).get(0);
                    int typeCode = Integer.parseInt(comboBoxBean.getCode());
                    if( typeCode != newBean.getCloseoutTypeCode() ){
                        newBean.setCloseoutTypeCode(typeCode);
                        modified = true;
                    }
                }
                break;
            case DATE_REQUESTED_COLUMN:
                try{
                    if(value.toString().trim().equals(EMPTY_STRING)) {
                        newBean.setDateRequested(null);
                        modified = true;
                        return;
                    }
                    String startDate = dtUtils.formatDate(value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        if( startDate == null) {
                            closeOutTableCellEditor.cancelCellEditing();
                            if(msgDisplay) {
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_REQUESTED_DATE_CONFRIMATION));
                            }
                            newBean.setDateRequested(null);
                            modified = true;
                            setRequestFocusInCloseoutThread(row , col);
                            closeOutTableCellEditor.stopCellEditing();
                            return ;
                            
                        }

                    date = dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    if( !sqlDate.equals(newBean.getDateRequested())){
                        newBean.setDateRequested(sqlDate);
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE, 42);
                        date = dtFormat.parse(dtFormat.format(calendar.getTime()));
                        sqlDate = new java.sql.Date(date.getTime());
                        newBean.setDateFollowUp(sqlDate);
                        modified = true;
                        if(acType == null) {
                            newBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                    }
                }catch (ParseException parseException) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_REQUESTED_DATE_CONFRIMATION));
                    setRequestFocusInCloseoutThread(row , col);
                    return ;
                }
                break;
            case DATE_FOLLOWUP_COLUMN:
                try{
                    if(value.toString().trim().equals(EMPTY_STRING)) {
                        newBean.setDateFollowUp(null);
                        return;
                    }
                    String startDate = dtUtils.formatDate(value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        if( startDate == null){
                            if(msgDisplay) {
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_FOLLOWUP_DATE_CONFRIMATION));
                            }
                            newBean.setDateFollowUp(newBean.getDateFollowUp());
                            setRequestFocusInCloseoutThread(row , col);
                            return ;
                            
                        }else{
                            date = dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                            if( !sqlDate.equals(newBean.getDateFollowUp())){
                                newBean.setDateFollowUp(sqlDate);
                                modified = true;
                                if(acType == null) {
                                    newBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                            }
                        }
                    }catch (ParseException parseException) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_FOLLOWUP_DATE_CONFRIMATION));
                        setRequestFocusInCloseoutThread(row , col);
                        return ;
                    }
               break;
            case DATE_RECEIVED_COLUMN:
                try{
                    if(value.toString().trim().equals(EMPTY_STRING)) {
                        newBean.setDateReceived(null);
                        return;
                    }
                    String startDate = dtUtils.formatDate(value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        if( startDate == null){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_RECEIVED_DATE_CONFRIMATION));
                            newBean.setDateReceived(newBean.getDateReceived());
                            setRequestFocusInCloseoutThread(row , col);
                            return ;
                            
                        }else{
                            date = dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                            if( !sqlDate.equals(newBean.getDateReceived())){
                                newBean.setDateReceived(sqlDate);
                                modified = true;
                                if(acType == null) {
                                    newBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                            }
                        }
                    }catch (ParseException parseException) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_RECEIVED_DATE_CONFRIMATION));
                        setRequestFocusInCloseoutThread(row , col);
                        return ;
                    }
                break; 
        }
        if(acType==null){
            newBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        
    }
    
    public CoeusVector getData(){
        return cvCloseoutData;
    }
    
   }
    
  /**
  * TableCellEditor for the closeout table
  */      
  public class CloseOutTableCellEditor extends AbstractCellEditor 
    implements TableCellEditor  {
        
        private int column;
        private CoeusTextField txtComponent;
        private CoeusComboBox cmbType;
        private boolean populated = false;
    
        public CloseOutTableCellEditor(){
            txtComponent = new CoeusTextField();
            txtComponent.setFont(CoeusFontFactory.getNormalFont());
            cmbType = new CoeusComboBox();
            cmbType.setFont(CoeusFontFactory.getNormalFont());
           
        }
       
       private void populateCombo() {
            int size = cvCloseoutType.size();
            ComboBoxBean comboBoxBean;
           cmbType.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)cvCloseoutType.get(index);
                cmbType.addItem(comboBoxBean);
            }
       }
       public Component getTableCellEditorComponent(
       JTable table, Object value, boolean isSelected, int row, int col) {
           this.column = col;
           switch(col){
               case CLOSEOUT_TYPE_COLUMN:
                   if(!populated) {
                      populateCombo();
                      populated = true;
                   }
                   cmbType.setSelectedItem(value);
                   if(isSelected) {
                       cmbType.setBackground(java.awt.Color.yellow);
                   }else{
                       cmbType.setBackground(java.awt.Color.white);
                   }
                   
                   return cmbType;
               case DATE_REQUESTED_COLUMN:
                   
               case DATE_FOLLOWUP_COLUMN:
               case DATE_RECEIVED_COLUMN:
                  if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(dtUtils.formatDate(value.toString().trim(), SIMPLE_DATE_FORMAT).toString());
                        
                    }
                  if(isSelected) {
                      txtComponent.setBackground(java.awt.Color.yellow);
                  }else{
                      txtComponent.setBackground(java.awt.Color.white);
                  }
                    return txtComponent;
           }
           return cmbType;
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case CLOSEOUT_TYPE_COLUMN:
                    return cmbType.getSelectedItem();
                case DATE_REQUESTED_COLUMN:
                case DATE_FOLLOWUP_COLUMN:
                case DATE_RECEIVED_COLUMN:
                    return txtComponent.getText();
            }
            return cmbType;
        }
  }
  
  /**
  * TableRenderer for the closeout table
  */
  
  public class CloseOutTableRenderer extends DefaultTableCellRenderer 
  implements TableCellRenderer{
      
      private CoeusTextField txtComponent;
      private JLabel lblText;
      
      public CloseOutTableRenderer (){
          txtComponent = new CoeusTextField();
          lblText = new JLabel();
          lblText.setOpaque(true);
          lblText.setFont(CoeusFontFactory.getNormalFont());
          txtComponent.setFont(CoeusFontFactory.getNormalFont());
      }
      
      public java.awt.Component getTableCellRendererComponent(JTable table,
      Object value, boolean hasFocus, boolean isSelected, int row, int col){
         switch(col){
              case CLOSEOUT_TYPE_COLUMN:
                  if(functionType == TypeConstants.DISPLAY_MODE ) {
                      lblText.setBackground(disabledBackground);
                      lblText.setForeground(java.awt.Color.black);
                  }else if(hasFocus || isSelected){
                      lblText.setBackground(java.awt.Color.yellow);
                      lblText.setForeground(java.awt.Color.black);
                  }else{
                      lblText.setBackground(java.awt.Color.white);
                      lblText.setForeground(java.awt.Color.black);
                  }
                  txtComponent.setText(value.toString());
                  lblText.setText(txtComponent.getText());
                  return lblText;
                  
              case DATE_REQUESTED_COLUMN:
                  if(functionType == TypeConstants.DISPLAY_MODE ) {
                      lblText.setBackground(disabledBackground);
                      lblText.setForeground(java.awt.Color.black);
                  }else if(hasFocus || isSelected){
                      lblText.setBackground(java.awt.Color.yellow);
                      lblText.setForeground(java.awt.Color.black);
                  }else{
                      lblText.setBackground(java.awt.Color.white);
                      lblText.setForeground(java.awt.Color.black);
                  }
                  if(value == null || (value.toString().equals(""))){
                      txtComponent.setText("");
                      lblText.setText(txtComponent.getText());
                  }else{
                      value = dtUtils.formatDate(value.toString().trim(), REQUIRED_DATE_FORMAT);
                      txtComponent.setText(value.toString());
                      lblText.setText(txtComponent.getText());
                 }
                  
                  return lblText;
              case DATE_FOLLOWUP_COLUMN:
                  if(functionType == TypeConstants.DISPLAY_MODE ) {
                      lblText.setBackground(disabledBackground);
                      lblText.setForeground(java.awt.Color.black);
                  }else if(hasFocus || isSelected){
                      lblText.setBackground(java.awt.Color.yellow);
                      lblText.setForeground(java.awt.Color.black);
                  }else{
                      lblText.setBackground(java.awt.Color.white);
                      lblText.setForeground(java.awt.Color.black);
                  }
                  if(value == null || (value.toString().equals(""))){
                      txtComponent.setText("");
                      lblText.setText(txtComponent.getText());
                  }else{
                      value = dtUtils.formatDate(value.toString().trim(), REQUIRED_DATE_FORMAT);
                      txtComponent.setText(value.toString());
                      lblText.setText(txtComponent.getText());
                 }
                  return lblText;
              case DATE_RECEIVED_COLUMN:
                  if(functionType == TypeConstants.DISPLAY_MODE ) {
                      lblText.setBackground(disabledBackground);
                      lblText.setForeground(java.awt.Color.black);
                  }else if(hasFocus || isSelected){
                      lblText.setBackground(java.awt.Color.yellow);
                      lblText.setForeground(java.awt.Color.black);
                  }else{
                      lblText.setBackground(java.awt.Color.white);
                      lblText.setForeground(java.awt.Color.black);
                  }
                  if(value == null || (value.toString().equals(""))){
                      txtComponent.setText("");
                      lblText.setText(txtComponent.getText());
                  }else{
                      value = dtUtils.formatDate(value.toString().trim(), REQUIRED_DATE_FORMAT);
                      txtComponent.setText(value.toString());
                      lblText.setText(txtComponent.getText());
                 }
                  return lblText;
          }
          return txtComponent;
      }
  }
    
}
