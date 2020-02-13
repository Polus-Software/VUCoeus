/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * SubContractController.java
 *
 * Created on March 22, 2004, 6:08 PM
 */

package edu.mit.coeus.award.controller;

//import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.award.gui.SubContractForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.vanderbilt.coeus.instprop.bean.ProposalApprovedSubcontractBean;
import edu.mit.coeus.award.controller.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.subcontract.bean.SubContractFundingSourceBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.subcontract.controller.*;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.gui.SubcontractBaseWindow;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author chandru
 */
public class SubContractController extends AwardController implements
java.awt.event.ActionListener, java.awt.event.MouseListener,TableColumnModelListener{
    
    public CoeusVector cvEditData; // JM 8-19-2013 made public so could access when adding IP
    private CoeusVector cvDisplayData;
    private CoeusVector cvDeletedData;
    
    private boolean columnsMoved;
    
    
    private static final String EMPTY_STRING = "";
    private SubContractForm subContractForm ;
    private AwardFundedTableModel awardFundedTableModel;
    private AwardFundedTableCellRenderer awardFundedTableCellRenderer;
    // JM 8-14-2013 changed to public so we can refresh from base window
    public SubContractTableModel subContractTableModel;
    private SubcontractTableCellEditor subcontractTableCellEditor;
    private SubContractTableCellRenderer subContractTableCellRenderer;
    private AmountTableCellRenderer amountTableCellRenderer;
    
    private AmountTableModel amountTableModel;
    private AmountCellEditor amountCellEditor;
    private QueryEngine queryEngine ;
    private boolean modified = false;
    private CoeusMessageResources coeusMessageResources;
    // Specifies the column numbers for the Subcontract table.
    // JM 10-8-2013 added sequence number to display; 6-22-2016 added location type
    private static final int SEQ_COLUMN = 0;
    private static final int NAME_COLUMN = 1;
    private static final int TYPE_COLUMN = 2;
    private static final int AMOUNT_COLUMN = 3;
    private static final int TYPE_CODE_COLUMN = 4;
    private int visibleColumns[] = {NAME_COLUMN,TYPE_COLUMN,AMOUNT_COLUMN,TYPE_CODE_COLUMN};
    
    /* JM 6-22-2016 for location type */
    private static final int OUTGOING_SUB_CODE = 3;
    private static final String OUTGOING_SUB_DESC = "Outgoing Sub Organization";
    private static final String AWARD_SERVLET = "/AwardServlet";
    private CoeusVector cvLocationTypes;
    private static final char GET_LOCATION_TYPES = 'E';
    /* JM END */
    
    // Specifies the column count for the Award Funded table
    private static final int AWARD_NAME_COLUMN = 0;
    private static final int AWARD_CODE_COLUMN = 1;
    private static final int AWARD_AMOUNT_COLUMN = 2;
    private static final int AWARD_STATUS_COLUMN = 3;
    
    // Specifies the column numbers for the Amount Table
    
    private static final int TOTAL_COLUMN = 0;
    private static final int TOTAL_AMOUNT_COLUMN = 1;
    
    // Case # 3738: Subcontract status code hard-coded into award subcontracts controller - Start
//    private static final String ACTIVE = "Active";
//    private static final String ARCHIVE = "Archive";
//    private static final String INACTIVE = "Inactive";
    // Case # 3738: Subcontract status code hard-coded into award subcontracts controller - End
    
    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
    private static final String DUPLICATE_INFO = "award_exceptionCode.1451";
    private static final String EMPTY_NAME = "award_exceptionCode.1452";
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    
    private AwardBaseBean  awardBaseBean ;
    private SubContractFundingSourceBean subContractFundingSourceBean;
    // private AwardApprovedSubcontractBean awardApprovedSubcontractBean;
    private char functionType;
    private int rowID = 1; //Used for uniquely identifying AwardApprovedSubcontractBeans
    
    private static final String SERVLET = "/AwardMaintenanceServlet";
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrPn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    
    // JM 10-8-2013 column width variables
    private static final int SEQ_COL_WIDTH = 35;
    private static final int NAME_COL_WIDTH = 400; // previously 550
    private static final int TYPE_COL_WIDTH = 150; // JM location type column width
    private static final int AMOUNT_COL_WIDTH = 150; // previously 210
    
    /** Creates a new instance of SubContractController */
    public SubContractController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
        cvEditData = new CoeusVector();
        cvDisplayData = new CoeusVector();
        cvDeletedData = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        //Set the max rowID
        setMaxRowID();
        setFormData(awardBaseBean);
        formatFields();
        setColumnData();
        setTableKeyTraversal();
        display();
    }
    
    
    public void display() {
        if(subContractForm.tblEditSubContract.getRowCount() > 0){
            subContractForm.tblEditSubContract.setRowSelectionInterval(0,0);
        }
        if(subContractForm.tblDisplaySubcontract.getRowCount() > 0){
            subContractForm.tblDisplaySubcontract.setRowSelectionInterval(0,0);
        }
        
        if(subContractForm.tblEditSubContract.getRowCount() == 0){
            subContractForm.pnlAmount.setVisible(false);
            subContractForm.tblAmount.setVisible(false);
        }else{
            subContractForm.tblAmount.setVisible(true);
            subContractForm.pnlAmount.setVisible(true);
        }
    }
    
    public  void setFormData(Object  awardBaseBean ){
        try{
            this.awardBaseBean = (AwardBaseBean) awardBaseBean;
            cvEditData = queryEngine.executeQuery(queryKey,AwardApprovedSubcontractBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            cvDisplayData  = queryEngine.executeQuery(queryKey,SubContractFundingSourceBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            subContractTableModel.setData(cvEditData);
            awardFundedTableModel.setData(cvDisplayData);
            amountTableModel.setData(cvEditData);
            //Added for the case# 301-Awd List>Scrolling thru List in Display mode-Start
            subContractTableModel.fireTableDataChanged();
            awardFundedTableModel.fireTableDataChanged();
            //Added for the case# 301-Awd List>Scrolling thru List in Display mode-End
            amountTableModel.fireTableDataChanged();
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    // JM
    public void setFormData(Object awardBaseBean, CoeusVector cvData){
        try{
            this.awardBaseBean = (AwardBaseBean) awardBaseBean;
            cvEditData = queryEngine.executeQuery(queryKey,AwardApprovedSubcontractBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            for (int c=0; c>cvData.size(); c++) {
            	cvEditData.add(cvData.get(c));
            }
            
            subContractTableModel.setData(cvEditData);
            subContractTableModel.fireTableDataChanged();
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    // JM END
    
    public void formatFields() {
        if(functionType== TypeConstants.DISPLAY_MODE){
             subContractForm.btnAdd.setEnabled(false);
            subContractForm.btnDelete.setEnabled(false);
        }
    }
    
    public java.awt.Component getControlledUI() {
        return jscrPn;
    }
    
    public Object getFormData(){
        return subContractForm;
    }
    
    
    public void registerComponents() {
        subContractForm = new SubContractForm();
        subContractForm.btnAdd.addActionListener(this);
        subContractForm.btnDelete.addActionListener(this);
        subContractForm.tblDisplaySubcontract.addMouseListener(this);
        subContractForm.tblEditSubContract.getTableHeader().getColumnModel().addColumnModelListener(this);
        
        subContractTableModel = new SubContractTableModel();
        subContractForm.tblEditSubContract.setModel(subContractTableModel);
        subContractTableCellRenderer = new SubContractTableCellRenderer();
        subcontractTableCellEditor = new SubcontractTableCellEditor();
        
        awardFundedTableCellRenderer = new AwardFundedTableCellRenderer();
        awardFundedTableModel = new AwardFundedTableModel();
        subContractForm.tblDisplaySubcontract.setModel(awardFundedTableModel);
        
        amountCellEditor = new AmountCellEditor();
        amountTableCellRenderer = new AmountTableCellRenderer();
        amountTableModel = new AmountTableModel();
        subContractForm.tblAmount.setModel(amountTableModel);
        subContractForm.tblAmount.addMouseListener(this);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(subContractForm);
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
    }
    
    public  void saveFormData(){
        subcontractTableCellEditor.stopCellEditing();
        try{
            if(modified){
                CoeusVector dataObject = new CoeusVector();
                if(cvDeletedData!= null && cvDeletedData.size() > 0){
                    dataObject.addAll(cvDeletedData);
                }
                if(cvEditData!= null && cvEditData.size() > 0){
                    dataObject.addAll(cvEditData);
                }
                
                if(dataObject!=null){
                    for(int index = 0; index < dataObject.size(); index++){
                        AwardApprovedSubcontractBean bean = (AwardApprovedSubcontractBean) dataObject.get(index);
                        if(bean.getAcType()!= null && bean.getSubcontractName() != null &&
                        !bean.getSubcontractName().trim().equals(EMPTY_STRING)){
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                //bean.setAcType(TypeConstants.UPDATE_RECORD);
                                //queryEngine.update(queryKey, bean);
                                
                                //First delete the existing record and then insert the same. This is
                                //required since primary keys can be modified
                            	// JM 11-15-2013 Handled now by updated db procedure
                                /* 
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                                
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                bean.setRowId(rowID++);
                                queryEngine.insert(queryKey, bean);
                                */
                                
                            	// JM 11-15-2013 added
                                bean.setAcType(TypeConstants.UPDATE_RECORD);
                                queryEngine.update(queryKey, bean);
                                
                            }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                //bean.setAw_SubcontractName(bean.getSubcontractName());
                                //bean.setRowId(rowID++);
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, bean);
                            }
                        }
                    }
                }
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        subcontractTableCellEditor.stopCellEditing();
        String name = EMPTY_STRING;
        //Equals nameEq;
        String[] subs = new String[cvEditData.size()];
        AwardApprovedSubcontractBean awardApprovedSubcontractBean;
        //CoeusVector cvFilterdName ;
        // Validate if the any of the row which contains the empty name then show the message
        if(cvEditData!= null){
            for(int index = 0; index < cvEditData.size(); index++){
                name = (String)subContractTableModel.getValueAt(index, NAME_COLUMN);
                if(name==null || name.trim().equals(EMPTY_STRING)){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_NAME));
                    subContractForm.tblEditSubContract.editCellAt(index,NAME_COLUMN);
                    subcontractTableCellEditor.txtComponent.requestFocus();
                    subContractForm.tblEditSubContract.setRowSelectionInterval(index,index);
                    subContractForm.tblEditSubContract.scrollRectToVisible(
                    subContractForm.tblEditSubContract.getCellRect(
                    index ,NAME_COLUMN, true));
                    return false;
                }
            }
        }
        // If duplicate names found then show the message
        // JM 11-13-2013 would like to only throw error if same name AND sequence - still working;
        // in the meantime, removed validation of duplicate name
//        if(cvEditData!= null && cvEditData.size() > 0){
//            for ( int index = 0; index < cvEditData.size(); index++){
//                awardApprovedSubcontractBean= (AwardApprovedSubcontractBean)cvEditData.get(index);
//                nameEq = new Equals("subcontractName",awardApprovedSubcontractBean.getSubcontractName());
//                cvFilterdName = cvEditData.filter(nameEq);
                /*
                if(cvFilterdName!= null && cvFilterdName.size() > 1){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(DUPLICATE_INFO));
                    subContractForm.tblEditSubContract.editCellAt(index,NAME_COLUMN);
                    subcontractTableCellEditor.txtComponent.requestFocus();
                    subContractForm.tblEditSubContract.setRowSelectionInterval(index,index);
                    subContractForm.tblEditSubContract.scrollRectToVisible(
                    subContractForm.tblEditSubContract.getCellRect(
                    index ,NAME_COLUMN, true));
                    return false;
                }*/
//            }
//        }
        
        // If duplicate name, sequence, and location type found, then show the message
        if(cvEditData!= null && cvEditData.size() > 0) {
            for (int index = 0; index < cvEditData.size(); index++){
            	awardApprovedSubcontractBean = (AwardApprovedSubcontractBean) cvEditData.get(index);
                subs[index] = awardApprovedSubcontractBean.getSubcontractName() + " " +
                		awardApprovedSubcontractBean.getSequenceNumber() + " " +
                		awardApprovedSubcontractBean.getLocationTypeCode();
            }
                
        	if (duplicates(subs)) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(DUPLICATE_INFO));
                return false;
            }
        	else {
        		return true;
        	}
        }
        else {
        	return true;
        }
        
        //return true;
    }
    
    /* JM 6-22-2016 method to check for duplicate entries */
    private boolean duplicates(final String[] subs) {
    	Set<String> dups = new HashSet<String>();
    	for (String i : subs) {
    		if (dups.contains(i)) return true;
    		dups.add(i);
    	}
    	return false;
    }
    /* JM END */
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(subContractForm.btnAdd)){
            performAddAction();
        }else if(source.equals(subContractForm.btnDelete)){
            performDeleteAction();
        }
    }
    
    
    private void performAddAction(){
        String orgname = "";
        String organId = "";
        try{
            CoeusSearch organizationSearch = null;
            organizationSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(),"ORGANIZATIONSEARCH" ,1);
            organizationSearch.showSearchWindow();
            HashMap organizationData = organizationSearch.getSelectedRow();
            if(organizationData != null){
                orgname = organizationData.get("ORGANIZATION_NAME").toString();
                organId = organizationData.get("ORGANIZATION_ID").toString();
            }
            
            //Bug Fix:1495 Start
            else if(organizationData == null || organizationData.isEmpty()){
                return ;
            }
            //Bug Fix:1495 End
            
        }catch(Exception err){
            err.printStackTrace();
        }
        
        if(cvEditData!= null && cvEditData.size() > 0){
            subcontractTableCellEditor.stopCellEditing();
        }
        int rowCount =  subContractForm.tblEditSubContract.getRowCount();
        if(rowCount  > 0){
            if(cvEditData!= null && cvEditData.size() > 0){
                for(int index =0 ; index < cvEditData.size(); index++){
                    AwardApprovedSubcontractBean bean = (AwardApprovedSubcontractBean)cvEditData.get(index);
                    // If the Amount or Name columns are empty then don't add the row.If any one of the
                    // field is entered then allow the user to add a new Row.
                    if(bean.getSubcontractName().trim().equals(EMPTY_STRING) && bean.getAmount()==0.00) return ;
                }
            }
        }
        
        AwardApprovedSubcontractBean newBean= new AwardApprovedSubcontractBean();
        newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
        newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        newBean.setRowId(rowID++);
        newBean.setSubcontractName(orgname);
        // JM 7-31-2013 add organization ID
        newBean.setOrganizationId(organId);
        // JM END
        // JM 6-22-2016 added location type
        newBean.setLocationTypeCode(OUTGOING_SUB_CODE);
        newBean.setLocationTypeDescription(OUTGOING_SUB_DESC);
        // JM END
        newBean.setAmount(0.00);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        cvEditData.add(newBean);
        subContractTableModel.fireTableRowsInserted(subContractTableModel.getRowCount()+1,
        subContractTableModel.getRowCount()+1);
        
        int lastRow = subContractForm.tblEditSubContract.getRowCount()-1;
        if(lastRow >= 0){
            subContractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
            subContractForm.tblEditSubContract.setRowSelectionInterval(lastRow,lastRow);
            subContractForm.tblEditSubContract.scrollRectToVisible(
            subContractForm.tblEditSubContract.getCellRect(lastRow, NAME_COLUMN, true));
            
        }
        subContractForm.tblEditSubContract.editCellAt(lastRow,AMOUNT_COLUMN);
        subContractForm.tblEditSubContract.requestFocusInWindow();
        subcontractTableCellEditor.txtComponent.setText(orgname);
        //subcontractTableCellEditor.txtComponent.requestFocus();
        
        if(subContractForm.tblEditSubContract.getRowCount() == 0){
            subContractForm.pnlAmount.setVisible(false);
            subContractForm.tblAmount.setVisible(false);
        }else{
            subContractForm.tblAmount.setVisible(true);
            subContractForm.pnlAmount.setVisible(true);
        }
        
    }
    
    
    private void performDeleteAction(){
        subcontractTableCellEditor.stopCellEditing();
        String mesg = EMPTY_STRING;
        int rowIndex = subContractForm.tblEditSubContract.getSelectedRow();
        if(rowIndex != -1 && rowIndex >= 0){
            mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                AwardApprovedSubcontractBean deletedBean = (AwardApprovedSubcontractBean)cvEditData.get(rowIndex);
                if (deletedBean.getAcType() == null ||
                deletedBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedData.add(deletedBean);
                }
                if(cvEditData!=null && cvEditData.size() > 0){
                    cvEditData.remove(rowIndex);
                    subContractTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    amountTableModel.fireTableDataChanged();
                    modified = true;
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(rowIndex >0){
                    subContractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
                    subContractForm.tblEditSubContract.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    subContractForm.tblEditSubContract.scrollRectToVisible(
                    subContractForm.tblEditSubContract.getCellRect(
                    rowIndex-1 ,0, true));
                }else{
                    if(subContractForm.tblEditSubContract.getRowCount()>0){
                        subContractForm.tblEditSubContract.setRowSelectionInterval(0,0);
                    }
                }
                
                if(subContractForm.tblEditSubContract.getRowCount() == 0){
                    subContractForm.pnlAmount.setVisible(false);
                    subContractForm.tblAmount.setVisible(false);
                }else{
                    subContractForm.tblAmount.setVisible(true);
                    subContractForm.pnlAmount.setVisible(true);
                }
                
                if(rowIndex >0){
                    subContractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
                    subContractForm.tblEditSubContract.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    subContractForm.tblEditSubContract.scrollRectToVisible(
                    subContractForm.tblEditSubContract.getCellRect(rowIndex-1 ,0, true));
                    
                }else{
                    if(subContractForm.tblEditSubContract.getRowCount()>0){
                        subContractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
                        subContractForm.tblEditSubContract.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    
    private void setColumnData(){
        // Setting the table header, column width, renderer and editor for the
        // editing subcontract table
        JTableHeader tableHeader = subContractForm.tblEditSubContract.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(true); // JM
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        subContractForm.tblEditSubContract.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        subContractForm.tblEditSubContract.setRowHeight(22);
        subContractForm.tblEditSubContract.setShowHorizontalLines(true);
        subContractForm.tblEditSubContract.setShowVerticalLines(true);
        subContractForm.tblEditSubContract.setOpaque(false);
        subContractForm.tblEditSubContract.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        // JM 10-8-2013 added sequence number; replace hard-coded column widths with static variables
        TableColumn column = subContractForm.tblEditSubContract.getColumnModel().getColumn(SEQ_COLUMN);
        column.setMaxWidth(SEQ_COL_WIDTH);
        column.setMinWidth(SEQ_COL_WIDTH);
        column.setPreferredWidth(SEQ_COL_WIDTH);
        column.setResizable(false);
        column.setCellEditor(subcontractTableCellEditor);
        column.setCellRenderer(subContractTableCellRenderer);

        column = subContractForm.tblEditSubContract.getColumnModel().getColumn(NAME_COLUMN);
        // JM END
        column.setMaxWidth(NAME_COL_WIDTH);
        column.setMinWidth(NAME_COL_WIDTH);
        column.setPreferredWidth(NAME_COL_WIDTH);
        column.setResizable(false);
        column.setCellEditor(subcontractTableCellEditor);
        column.setCellRenderer(subContractTableCellRenderer);
        
        /* JM 6-22-2016 location type description */
        column = subContractForm.tblEditSubContract.getColumnModel().getColumn(TYPE_COLUMN);
        column.setMaxWidth(TYPE_COL_WIDTH);
        column.setMinWidth(TYPE_COL_WIDTH);
        column.setPreferredWidth(TYPE_COL_WIDTH);
        column.setResizable(false);
        column.setCellEditor(new DefaultCellEditor(getLocationTypeCmb()));
        column.setCellRenderer(subContractTableCellRenderer);
        
        column = subContractForm.tblEditSubContract.getColumnModel().getColumn(AMOUNT_COLUMN);
        column.setMaxWidth(AMOUNT_COL_WIDTH);
        column.setMinWidth(AMOUNT_COL_WIDTH);
        column.setPreferredWidth(AMOUNT_COL_WIDTH);
        column.setResizable(false);
        column.setCellEditor(subcontractTableCellEditor);
        column.setCellRenderer(subContractTableCellRenderer);
        
        /* JM 6-22-2016 location type code */
        column = subContractForm.tblEditSubContract.getColumnModel().getColumn(TYPE_CODE_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        /* JM END */
        
        // Setting the table header, column width, renderer and editor for the
        // display award funded subcontract table
        JTableHeader awardHeader = subContractForm.tblDisplaySubcontract.getTableHeader();
        awardHeader.addMouseListener(new ColumnHeaderListener());
        awardHeader .setReorderingAllowed(false);
        awardHeader .setFont(CoeusFontFactory.getLabelFont());
        
        subContractForm.tblDisplaySubcontract.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        subContractForm.tblDisplaySubcontract.setRowHeight(22);
        
        subContractForm.tblDisplaySubcontract.setShowHorizontalLines(true);
        subContractForm.tblDisplaySubcontract.setShowVerticalLines(true);
        subContractForm.tblDisplaySubcontract.setOpaque(false);
        subContractForm.tblDisplaySubcontract.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn awardColumn = subContractForm.tblDisplaySubcontract.getColumnModel().getColumn(AWARD_NAME_COLUMN);
        awardColumn.setMaxWidth(290);
        awardColumn.setMinWidth(250);
        awardColumn.setPreferredWidth(250);
        awardColumn.setResizable(true);
        awardColumn.setCellRenderer(awardFundedTableCellRenderer);
        
        awardColumn = subContractForm.tblDisplaySubcontract.getColumnModel().getColumn(AWARD_CODE_COLUMN);
        awardColumn.setMinWidth(150);
        awardColumn.setMaxWidth(220);
        awardColumn.setPreferredWidth(160);
        //awardColumn.setPreferrnewBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());edWidth(160);
        awardColumn.setResizable(true);
        awardColumn.setCellRenderer(awardFundedTableCellRenderer);
        
        awardColumn = subContractForm.tblDisplaySubcontract.getColumnModel().getColumn(AWARD_AMOUNT_COLUMN);
        awardColumn.setMaxWidth(250);
        awardColumn.setMinWidth(200);
        awardColumn.setPreferredWidth(210);
        awardColumn.setResizable(true);
        awardColumn.setCellRenderer(awardFundedTableCellRenderer);
        
        awardColumn = subContractForm.tblDisplaySubcontract.getColumnModel().getColumn(AWARD_STATUS_COLUMN);
        awardColumn.setMaxWidth(160);
        awardColumn.setMinWidth(120);
        awardColumn.setPreferredWidth(140);
        awardColumn.setResizable(true);
        awardColumn.setCellRenderer(awardFundedTableCellRenderer);
        
        subContractForm.tblAmount.setRowHeight(22);
        subContractForm.tblAmount.setShowHorizontalLines(true);
        subContractForm.tblAmount.setShowVerticalLines(true);
        
        // Setting renderer and sizes for the amount related columns
        TableColumn amountColumn = subContractForm.tblAmount.getColumnModel().getColumn(TOTAL_COLUMN);
        amountColumn.setMaxWidth(570);
        amountColumn.setMinWidth(570);
        amountColumn.setPreferredWidth(570);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
        
        amountColumn = subContractForm.tblAmount.getColumnModel().getColumn(TOTAL_AMOUNT_COLUMN);
        amountColumn.setMaxWidth(210);
        amountColumn.setMinWidth(210);
        amountColumn.setPreferredWidth(210);
        amountColumn.setResizable(true);
        amountColumn.setCellEditor(amountCellEditor);
        amountColumn.setCellRenderer(amountTableCellRenderer);
    }
    
    private JComboBox getLocationTypeCmb() {
    	JComboBox comboBox = new JComboBox();
    	comboBox.setModel(new DefaultComboBoxModel(getLocationTypes()));
    	return comboBox;
    }
    
    private CoeusVector getLocationTypes() {
        cvLocationTypes = new CoeusVector();
    	RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_LOCATION_TYPES);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + AWARD_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	cvLocationTypes = (CoeusVector) responderBean.getDataObject();
        }
        
		return cvLocationTypes;    	
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()!=2) return ;
        lookupWindow();
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    private void lookupWindow(){
        
        try{
            
            AwardListController controller = new AwardListController();
            boolean value = controller.viewSubcontract();
            if(value){
                SubContractBean subContractBean = new SubContractBean();
                int row = subContractForm.tblDisplaySubcontract.getSelectedRow();
                String subcontractCode = subContractForm.tblDisplaySubcontract.getValueAt(row , AWARD_CODE_COLUMN).toString();
                
                if(isSubcontractOpen(subcontractCode, CoeusGuiConstants.DISPLAY_MODE)) {
                        return ;
                    }
                
                
                subContractBean.setSubContractCode(subcontractCode);
                SubcontractBaseWindowController subcontractBaseWindowController = new SubcontractBaseWindowController("Display Subcontract ", CoeusGuiConstants.DISPLAY_MODE, subContractBean,null,true);
                subcontractBaseWindowController.display();
            }else{
                CoeusOptionPane.showInfoDialog("You do not have the right to view a subcontract");
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    // Bug Fix #1426
    
    /** This method is used to check whether the given Subcontract number is already
      * opened in the given mode or not.
      * @param refId refId - for  Subcontract its Subcontract Number.
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if Subcontract window is already open else returns false.
      */
     boolean isSubcontractOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = CoeusGuiConstants.getMDIForm().checkDuplicate(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, refId, mode );
        }catch(Exception exception){
            duplicate = true;
            if( displayMessage ){
                if(exception.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(exception.getMessage());
                }
            }
            CoeusInternalFrame frame = CoeusGuiConstants.getMDIForm().getFrame(
                    CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW,refId);
            
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            }
            return true;
        }
        return false;
     }// Bug Fix #1426
     
     /** This method is used to check whether the given Subcontract number is already
      * opened in the given mode or not and displays message if the Subcontract is open
      * @param refId refId - for Subcontract its Subcontract Number.
      * @param mode mode of Form open.
      * @return true if Subcontract window is already open else returns false.
      */
     boolean isSubcontractOpen(String refId, char mode){
         return isSubcontractOpen(refId, mode, true);
     }// Bug Fix #1426
    
    public void columnAdded(TableColumnModelEvent e) {
    }
    
    public void columnMarginChanged(ChangeEvent e) {
    }
    
    public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
        int fromIndex = tableColumnModelEvent.getFromIndex();
        int toIndex = tableColumnModelEvent.getToIndex();
        if(fromIndex != toIndex) {
            int from = visibleColumns[fromIndex];
            visibleColumns[fromIndex] = visibleColumns[toIndex];
            visibleColumns[toIndex] = from;
        }
        columnsMoved = true;
    }
    
    public void columnRemoved(TableColumnModelEvent e) {
    }
    
    public void columnSelectionChanged(ListSelectionEvent e) {
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class ColumnHeaderListener extends java.awt.event.MouseAdapter {
        String nameBeanId [][] ={
            {"0","organizationName" },
        };
        boolean sort =true;
        /**
         * @param evt
         */
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvDisplayData!=null && cvDisplayData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvDisplayData).sort(nameBeanId [vColIndex][1],sort,true);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    awardFundedTableModel.fireTableRowsUpdated(0, awardFundedTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    public class SubContractTableModel extends AbstractTableModel{
        
    	// JM 10-8-2013 added sequence number; 6-22-20016 added location type and code
        private String colName[] = {"Seq","Subcontractor Name","Location Type","Amount","Location Type Code"};
        private Class colClass[] = {Integer.class, String.class, String.class, Double.class, Integer.class};
        
        private boolean[] canEdit = new boolean [] {  false, false, true, true };
        
        // JM 11-8-2013 updated so name would not be editable
        public boolean isCellEditable(int row, int col){
            if(functionType == TypeConstants.DISPLAY_MODE){
                return false;
            }
            else {
            	return canEdit[col];
            }
        }
        
        public int getRowCount(){
            if(cvEditData==null ){
                return 0;
            }else{
                return cvEditData.size();
            }
        }
        
        public void setData(CoeusVector cvEditData){
            cvEditData = cvEditData;
        }
        
        public int getColumnCount(){
            return colName.length;
        }
        
        public String getColumnName(int column) {
            return colName[column];
        }
        
        public Class getColumnClass(int colIndex){
            return colClass [colIndex];
        }
        
        // JM 10-8-2013 added sequence number; 6-22-20016 added location type and code
        public Object getValueAt(int row, int col){
            AwardApprovedSubcontractBean awardApprovedSubcontractBean =
            		(AwardApprovedSubcontractBean)cvEditData.get(row);
            
            switch(col){
            	case SEQ_COLUMN:
            		return awardApprovedSubcontractBean.getSequenceNumber();
                case NAME_COLUMN:
                    return awardApprovedSubcontractBean.getSubcontractName();
                case TYPE_COLUMN:
                    return awardApprovedSubcontractBean.getLocationTypeDescription();
                case AMOUNT_COLUMN:
                    return new Double(awardApprovedSubcontractBean.getAmount());
                case TYPE_CODE_COLUMN:
                    return awardApprovedSubcontractBean.getLocationTypeCode();	
                    
            }
            return EMPTY_STRING;
        }
        
        // JM 10-8-2013 added sequence number; 6-22-20016 added location type and code
        public void setValueAt(Object value, int row, int col){
            if (cvEditData == null) return;
        	ComboBoxBean comboBoxBean = new ComboBoxBean();
        	int ix = 0;
            
            AwardApprovedSubcontractBean awardApprovedSubcontractBean = 
            		(AwardApprovedSubcontractBean) cvEditData.get(row);
            double cost = 0.00;
            int seq = 0;
            switch(col){
            	case SEQ_COLUMN:
            		seq = (Integer) value;
	                if (awardApprovedSubcontractBean.getSequenceNumber() == seq) {
	                    break;
	                }
	                awardApprovedSubcontractBean.setSequenceNumber(seq);
	                modified = true;
	                break;            	
                case NAME_COLUMN:
                    if(awardApprovedSubcontractBean.getSubcontractName()!= null &&
                    awardApprovedSubcontractBean.getSubcontractName().equals(value.toString())) {
                        break;
                    }
                    awardApprovedSubcontractBean.setSubcontractName(value.toString().trim());                    
                    if (awardApprovedSubcontractBean.getAcType() != null &&
                    awardApprovedSubcontractBean.getAcType().equals(
                    TypeConstants.INSERT_RECORD)) {
                        awardApprovedSubcontractBean.setAw_SubcontractName(
                        awardApprovedSubcontractBean.getSubcontractName());
                    }
                    modified = true;
                    break;
                case TYPE_COLUMN:
		        	if (value.getClass() == ComboBoxBean.class) {
			        	comboBoxBean = (ComboBoxBean) value;
			        	ix = (Integer) cvLocationTypes.indexOf(comboBoxBean.getCode());
			        	awardApprovedSubcontractBean.setLocationTypeDescription(comboBoxBean.getDescription()); 
			        	awardApprovedSubcontractBean.setLocationTypeCode(Integer.parseInt(comboBoxBean.getCode())); 
		        	}
	                modified = true;
                	break;
                case AMOUNT_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    if(awardApprovedSubcontractBean.getAmount()==cost) {
                        break;
                    }
                    awardApprovedSubcontractBean.setAmount(cost);
                    modified = true;
                    amountTableModel.fireTableDataChanged();
                    break;
                case TYPE_CODE_COLUMN:
                	// set above
                	break;
            }
            if(awardApprovedSubcontractBean.getAcType() == null){
                awardApprovedSubcontractBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
    }
    public class SubcontractTableCellEditor extends AbstractCellEditor implements TableCellEditor{
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        private int column;
        
        public SubcontractTableCellEditor(){
            txtComponent = new JTextField();
            txtComponent.setDocument(new LimitedPlainDocument(50));
            txtCurrencyComponent = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
        }
        
        public java.awt.Component getTableCellEditorComponent(
        javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            this.column  = column;
            switch(column){
                case NAME_COLUMN:
                    if(value==null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                case AMOUNT_COLUMN:
                    if(value== null){
                        txtCurrencyComponent.setValue(0.00);
                    }else{
                        txtCurrencyComponent.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtCurrencyComponent;
            }
            return txtCurrencyComponent;
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case NAME_COLUMN:
                    return txtComponent.getText();
                case AMOUNT_COLUMN:
                    return txtCurrencyComponent.getValue();
                    
            }
            return txtCurrencyComponent;
        }
    }
    
    public class SubContractTableCellRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer {
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        private JLabel lblText,lblCurrency;
        // JM 10-9-2013 added sequence number
        private JLabel lblSeq;
        private JTextField txtSeq;
        // JM END
        
        /* JM 6-22-2016 added location type */
        private JLabel lblType;
        private JTextField txtType;        
        /* JM END */
        
        public SubContractTableCellRenderer(){
        	// JM 10-9-2013 added sequence number
        	lblSeq = new JLabel();
        	lblSeq.setOpaque(true);
        	lblSeq.setHorizontalAlignment(JLabel.CENTER);
        	//lblSeq.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        	txtSeq = new JTextField();
        	txtSeq.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        	// JM END
        	
            /* JM 6-22-2016 added location type */
            lblType = new JLabel();
            lblType.setOpaque(true);
            txtType = new JTextField();
            txtType.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            /* JM END */
        	
            lblText = new JLabel();
            lblCurrency = new JLabel();
            lblText.setOpaque(true);
            lblCurrency.setOpaque(true);
            lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
            //lblText.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
            //lblCurrency.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
            txtComponent = new JTextField();
            txtCurrencyComponent = new DollarCurrencyTextField();
            
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col){
        	// JM 10-9-2013 added sequence number; 6-22-2016 added location type
            case SEQ_COLUMN:
                if(functionType==TypeConstants.DISPLAY_MODE){
	                    lblSeq.setBackground(disabledBackground);
	                    lblSeq.setForeground(java.awt.Color.black);
	                }else if(isSelected){
	                	lblSeq.setBackground(java.awt.Color.YELLOW);
	                	lblSeq.setForeground(java.awt.Color.black);
	                }else{
	                	lblSeq.setBackground(java.awt.Color.white);
	                	lblSeq.setForeground(java.awt.Color.black);
	                }
            
	                if(value == null || value.toString().trim().equals(EMPTY_STRING)){
	                    txtSeq.setText(EMPTY_STRING);
	                    lblSeq.setText(txtSeq.getText());
	                }else{
	                	txtSeq.setText(value.toString());
	                    lblSeq.setText(txtSeq.getText());
	                }
	                return lblSeq;

            	case NAME_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.black);
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

                case TYPE_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        lblType.setBackground(disabledBackground);
                        lblType.setForeground(java.awt.Color.black);
                    }else if(isSelected){
                    	lblType.setBackground(java.awt.Color.YELLOW);
                    	lblType.setForeground(java.awt.Color.black);
                    }else{
                    	lblType.setBackground(java.awt.Color.white);
                    	lblType.setForeground(java.awt.Color.black);
                    }

                    if (value == null || value.toString().trim().equals(EMPTY_STRING)) {
                        txtType.setText(EMPTY_STRING);
                        lblType.setText(txtType.getText());
                    } else {
                    	txtType.setText(value.toString());
                    	lblType.setText(txtType.getText());
                    }
                    return lblType;
                    
                case AMOUNT_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        lblCurrency.setBackground(disabledBackground);
                        lblCurrency.setForeground(java.awt.Color.black);
                    }else if(isSelected){
                        lblCurrency.setBackground(java.awt.Color.YELLOW);
                        lblCurrency.setForeground(java.awt.Color.black);
                    }else{
                        lblCurrency.setBackground(java.awt.Color.white);
                        lblCurrency.setForeground(java.awt.Color.black);
                    }
                    
                    
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
    
    public class AwardFundedTableModel extends AbstractTableModel{
        private String colName[] = {"Subcontractor Name","Subcontract Code","Amount","Status"};
        private Class colClass[] = {String.class, String.class, String.class, String.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount(){
            return colName.length;
        }
        
        public Class getColumnClass(int colIndex){
            return colClass[colIndex];
        }
        
        public String getColumnName(int column){
            return colName[column];
        }
        
        public void setData(CoeusVector cvDisplayData){
            cvDisplayData = cvDisplayData;
        }
        
        public int getRowCount(){
            if(cvDisplayData== null){
                return 0;
            }else{
                return cvDisplayData.size();
            }
        }
        
        public Object getValueAt(int row, int col){
            SubContractFundingSourceBean subContractFundingSourceBean =
            (SubContractFundingSourceBean)cvDisplayData.get(row);
            switch(col){
                case AWARD_NAME_COLUMN:
                    return subContractFundingSourceBean.getOrganizationName();
                case AWARD_CODE_COLUMN:
                    return subContractFundingSourceBean.getSubContractCode();
                case AWARD_AMOUNT_COLUMN:
                    return new Double(subContractFundingSourceBean.getObligatedAmount());
                case AWARD_STATUS_COLUMN:
                    // Case# 3738: Subcontract status code hard-coded into award subcontracts controller - Start
//                    return new Integer(subContractFundingSourceBean.getStatusCode());
                    return subContractFundingSourceBean.getStatusDescription();
                    // Case# 3738: Subcontract status code hard-coded into award subcontracts controller - End
            }
            return EMPTY_STRING;
        }
    }
    
    
    public class AwardFundedTableCellRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer{
        
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyCompoent;
        private JLabel lblText,lblCurrency;
        
        public AwardFundedTableCellRenderer(){
            lblText = new JLabel();
            lblCurrency = new JLabel();
            lblText.setOpaque(true);
            lblCurrency.setOpaque(true);
            lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
            lblText.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
            lblCurrency.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
            txtComponent = new JTextField();
            txtCurrencyCompoent = new DollarCurrencyTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyCompoent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col){
                case AWARD_NAME_COLUMN:
                case AWARD_CODE_COLUMN:
                    if(isSelected ){
                        lblText.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                        lblText.setForeground(java.awt.Color.white);
                    }else{
                        lblText.setBackground(disabledBackground);
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
                    
                case AWARD_AMOUNT_COLUMN:
                    if(isSelected ){
                        lblCurrency.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                        lblCurrency.setForeground(java.awt.Color.white);
                    }else{
                        lblCurrency.setBackground(disabledBackground);
                        lblCurrency.setForeground(java.awt.Color.black);
                    }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrencyCompoent.setText(EMPTY_STRING);
                        lblCurrency.setText(txtCurrencyCompoent.getText());
                    }else{
                        txtCurrencyCompoent.setText(value.toString());
                        lblCurrency.setText(txtCurrencyCompoent.getText());
                    }
                    return lblCurrency;
                    
                case AWARD_STATUS_COLUMN:
                    // Case# 3738: Subcontract status code hard-coded into award subcontracts controller - Start
//                    if(cvDisplayData!= null && cvDisplayData.size() > 0){
//                        subContractFundingSourceBean = (SubContractFundingSourceBean)cvDisplayData.get(row);
//                        if(subContractFundingSourceBean.getStatusCode()== 1){
//                            value = ACTIVE;
//                        }else if(subContractFundingSourceBean.getStatusCode()== 2){
//                            value = INACTIVE;
//                        }else if(subContractFundingSourceBean.getStatusCode()== 3){
//                            value = ARCHIVE;
//                        }
//                    }
                    // Case# 3738: Subcontract status code hard-coded into award subcontracts controller - End
                    if(isSelected ){
                        lblText.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                        lblText.setForeground(java.awt.Color.white);
                    }else{
                        lblText.setBackground(disabledBackground);
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
            }
            return txtComponent;
        }
    }
    
    public class AmountTableModel extends AbstractTableModel implements MouseListener{
        
        private String colName[] = {"Total Amount", ""};
        private Class colClass[] = {String.class, Double.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount(){
            return colName.length;
        }
        
        public Class getColumnClass(int colIndex){
            return colClass[colIndex];
        }
        public int getRowCount(){
            return 1;
        }
        
        public void setData(CoeusVector cvEditData){
            cvEditData = cvEditData;
        }
        public String getColumnName(int column){
            return colName[column];
        }
        
        public Object getValueAt(int row, int col) {
            double totalAmount = 0.00;
            String name = "Total Amount:";
            if(col==TOTAL_COLUMN){
                return name;
            }
            if(col==TOTAL_AMOUNT_COLUMN){
                totalAmount = cvEditData.sum("amount");
                return new Double(totalAmount);
            }
            return EMPTY_STRING;
        }
        
        public void mouseClicked(MouseEvent mouseEvent) {
            int clickCount = mouseEvent.getClickCount();
            if(clickCount == 2){
                CoeusOptionPane.showInfoDialog("waiting .....");
            }
                
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseExited(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
        }
        
        public void mouseReleased(MouseEvent e) {
        }
        
    }
    
    
    public class AmountCellEditor extends AbstractCellEditor implements TableCellEditor{
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        private int column;
        
        public AmountCellEditor(){
            txtComponent = new JTextField();
            txtCurrencyComponent = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
        }
        
        public java.awt.Component getTableCellEditorComponent(
        javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            this.column  = column;
            switch(column){
                case TOTAL_COLUMN:
                    if(value==null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                case TOTAL_AMOUNT_COLUMN:
                    if(value== null){
                        txtCurrencyComponent.setValue(0.00);
                    }else{
                        txtCurrencyComponent.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtCurrencyComponent;
            }
            return txtCurrencyComponent;
        }
        
        // JM changed NAME_COLUMN (now 1) to TOTAL_COLUMN (0)
        public Object getCellEditorValue() {
            switch(column){
                case TOTAL_COLUMN:
                    return txtComponent.getText();
                case TOTAL_AMOUNT_COLUMN:
                    return txtCurrencyComponent.getValue();
                    
            }
            return txtCurrencyComponent;
        }
    }
    
    
    public class AmountTableCellRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer {
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        private JLabel lblText,lblValue;
        
        public AmountTableCellRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            lblValue = new JLabel();
            lblValue.setOpaque(true);
            lblText.setHorizontalAlignment(RIGHT);
            lblValue.setHorizontalAlignment(RIGHT);
            lblText.setFont(CoeusFontFactory.getLabelFont());
            txtComponent = new JTextField();
            txtCurrencyComponent = new DollarCurrencyTextField();
            txtComponent.setBackground(disabledBackground);
            txtComponent.setHorizontalAlignment(JTextField.RIGHT);
            txtComponent.setForeground(java.awt.Color.BLACK);
            txtComponent.setFont(CoeusFontFactory.getLabelFont());
            txtCurrencyComponent.setBackground(disabledBackground);
            txtCurrencyComponent.setForeground(java.awt.Color.BLACK);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
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
                    //return txtComponent;
                    
                case TOTAL_AMOUNT_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrencyComponent.setText(EMPTY_STRING);
                        lblValue.setText(txtCurrencyComponent.getText());
                    }else{
                        txtCurrencyComponent.setValue(new Double(value.toString()).doubleValue());
                        lblValue.setText(txtCurrencyComponent.getText());
                    }
                    return lblValue;
                   // return txtCurrencyComponent;
            }
            return txtComponent;
        }
        
    }
    /* This method sets the maximum Row ID from the vector of Budget Persons beans
     *that is present in queryEngine
     */
    private void setMaxRowID() {
        CoeusVector cvApprovedSubcontracts = new CoeusVector();
        AwardApprovedSubcontractBean approvedSubcontractBean;
        try {
            cvApprovedSubcontracts = queryEngine.getDetails(queryKey,
            AwardApprovedSubcontractBean.class);
            if (cvApprovedSubcontracts != null && cvApprovedSubcontracts.size() > 0) {
                cvApprovedSubcontracts.sort("rowId", false);
                approvedSubcontractBean = (AwardApprovedSubcontractBean) cvApprovedSubcontracts.get(0);
                rowID = approvedSubcontractBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    
    public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    public boolean isRefreshRequired() {
        boolean retValue;
        
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(awardBaseBean);
            cvDeletedData.clear();
            setRefreshRequired(false);
        }
    }
    
    
    
     // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
         javax.swing.InputMap im = subContractForm.tblEditSubContract.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = subContractForm.tblEditSubContract.getActionMap().get(im.get(tab));
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
        subContractForm.tblEditSubContract.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = subContractForm.tblEditSubContract.getActionMap().get(im.get(shiftTab));
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
                        column = 5;
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
        subContractForm.tblEditSubContract.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    
    //Bug Fix:Performance Issue (Out of memory) Start 3
    public void cleanUp(){
        jscrPn.remove(subContractForm);
        jscrPn = null;
        
        cvEditData = null;
        cvDisplayData = null;
        cvDeletedData = null;
        subContractForm = null;
        awardFundedTableModel = null;
        awardFundedTableCellRenderer = null;
        subContractTableModel = null;
        subcontractTableCellEditor = null;
        subContractTableCellRenderer = null;
        amountTableCellRenderer = null;

        amountTableModel = null;
        amountCellEditor = null;
        awardBaseBean = null;
        subContractFundingSourceBean = null;
    }
    //Bug Fix:Performance Issue (Out of memory) End 3
}





