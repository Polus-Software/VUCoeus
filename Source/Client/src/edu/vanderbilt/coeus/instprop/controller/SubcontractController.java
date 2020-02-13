/**
 * SubcontractController.java
 *
 * @author mcafeekj
 *
 * Created on May 1, 2013
 * Vanderbilt University Office of Research
 */

package edu.vanderbilt.coeus.instprop.controller;

import edu.vanderbilt.coeus.instprop.gui.SubcontractForm;
import edu.vanderbilt.coeus.instprop.bean.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.instprop.controller.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.search.gui.CoeusSearch;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class SubcontractController extends InstituteProposalController implements
	java.awt.event.ActionListener, java.awt.event.MouseListener,TableColumnModelListener{
    
    private CoeusVector cvEditData;
    private CoeusVector cvDisplayData;
    private CoeusVector cvDeletedData;
    
    private static final char GET_SUBCONTRACTS = 'k';
    private static final char UPDATE_SUBCONTRACTS = 'l';
    private static final char GET_LOCATION_TYPES = 'E';
    
    private static final String EMPTY_STRING = "";
    private SubcontractForm subcontractForm;
    private SubContractTableModel subContractTableModel;
    private SubcontractTableCellEditor subcontractTableCellEditor;
    private SubContractTableCellRenderer subContractTableCellRenderer;
    private AmountTableCellRenderer amountTableCellRenderer;
    
    private AmountTableModel amountTableModel;
    private AmountCellEditor amountCellEditor;
    private QueryEngine queryEngine ;
    private boolean modified = false;
    private CoeusMessageResources coeusMessageResources;
    // Specifies the column numbers for the Subcontract table.
    private static final int NAME_COLUMN = 0;
    private static final int TYPE_COLUMN = 1;
    private static final int AMOUNT_COLUMN = 2;
    private static final int TYPE_CODE_COLUMN = 3;
    private int visibleColumns[] = {NAME_COLUMN,TYPE_COLUMN,AMOUNT_COLUMN,TYPE_CODE_COLUMN};
    private CoeusVector cvLocationTypes;
    
    private static final int TOTAL_COLUMN = 0;
    private static final int TOTAL_AMOUNT_COLUMN = 1;
    
    private static final int OUTGOING_SUB_CODE = 3;
    private static final String OUTGOING_SUB_DESC = "Outgoing Sub Organization";
    
    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
    private static final String DUPLICATE_INFO = "award_exceptionCode.1451";
    private static final String EMPTY_NAME = "award_exceptionCode.1452";
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    	getDefaults().get("Panel.background");
    
    
    private InstituteProposalBaseBean propBaseBean ;
    private char functionType;
    private int rowID = 1; //Used for uniquely identifying ProposalApprovedSubcontractBeans
    
    private static final String SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String AWARD_SERVLET = "/AwardServlet";
    
    private JScrollPane jscrPn;
    
    /** Creates a new instance of SubContractController */
    public SubcontractController(InstituteProposalBaseBean propBaseBean,char functionType) {
    	super(propBaseBean);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
        cvEditData = new CoeusVector();
        cvDisplayData = new CoeusVector();
        cvDeletedData = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        //Set the max rowID
        setMaxRowID();
        setFormData(propBaseBean);
        formatFields();
        setColumnData();
        setTableKeyTraversal();
        display();
    }
    
    
    public void display() {
        if(subcontractForm.tblEditSubContract.getRowCount() > 0){
            subcontractForm.tblEditSubContract.setRowSelectionInterval(0,0);
        }
       
        if(subcontractForm.tblEditSubContract.getRowCount() == 0){
            subcontractForm.pnlAmount.setVisible(false);
            subcontractForm.tblAmount.setVisible(false);
        }else{
            subcontractForm.tblAmount.setVisible(true);
            subcontractForm.pnlAmount.setVisible(true);
        }
    }
    
    public void setFormData(Object propBaseBean ){
        try{
            this.propBaseBean = (InstituteProposalBaseBean) propBaseBean;
			cvEditData = getDataFromServer(this.propBaseBean.getProposalNumber());
		} catch (CoeusClientException e) {
			System.out.println("Unable to retrieve data from server.");
			e.printStackTrace();
		}
            
		subContractTableModel.setData(cvEditData);
		amountTableModel.setData(cvEditData);
		subContractTableModel.fireTableDataChanged();
		amountTableModel.fireTableDataChanged();
    }
    
    public CoeusVector getDataFromServer(String proposalNumber) throws CoeusClientException {
        CoeusVector cvSubcontracts = new CoeusVector();
    	RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_SUBCONTRACTS);
        requesterBean.setDataObject(instituteProposalBaseBean.getProposalNumber());
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	cvSubcontracts = (CoeusVector) responderBean.getDataObject();
        }
        
		return cvSubcontracts;
    }    
    
    public void formatFields() {
        if(functionType== TypeConstants.DISPLAY_MODE){
            subcontractForm.btnAdd.setEnabled(false);
            subcontractForm.btnDelete.setEnabled(false);
        }
    }
    
    public java.awt.Component getControlledUI() {
        return jscrPn;
    }
    
    public Object getFormData(){
        return subcontractForm;
    }
    
    public void registerComponents() {
        subcontractForm = new SubcontractForm();
        subcontractForm.btnAdd.addActionListener(this);
        subcontractForm.btnDelete.addActionListener(this);
        subcontractForm.tblEditSubContract.getTableHeader().getColumnModel().addColumnModelListener(this);
        
        subContractTableModel = new SubContractTableModel();
        subcontractForm.tblEditSubContract.setModel(subContractTableModel);
        subContractTableCellRenderer = new SubContractTableCellRenderer();
        subcontractTableCellEditor = new SubcontractTableCellEditor();
        
        amountCellEditor = new AmountCellEditor();
        amountTableCellRenderer = new AmountTableCellRenderer();
        amountTableModel = new AmountTableModel();
        subcontractForm.tblAmount.setModel(amountTableModel);
        subcontractForm.tblAmount.addMouseListener(this);
        
        jscrPn = new JScrollPane(subcontractForm);
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPn.getParent().dispatchEvent(e);
            }
        });
    }
    
    public void saveFormData() {
    	if (subcontractForm.tblEditSubContract.getCellEditor() != null) {
    		subcontractForm.tblEditSubContract.getCellEditor().stopCellEditing();
    	}
        CoeusVector dataObject = new CoeusVector();
        Double amount = new Double(0.00);
        setSaveRequired(true);
        
        if(isSaveRequired()){
            
            if(cvDeletedData!= null && cvDeletedData.size() > 0){
                dataObject.addAll(cvDeletedData);
            }
            
            if(cvEditData!= null && cvEditData.size() > 0){
                dataObject.addAll(cvEditData);
            }
            
            if(dataObject != null){
                for(int index = 0; index < dataObject.size(); index++){
                	amount = new Double(0.00);
                	ProposalApprovedSubcontractBean bean =
                			(ProposalApprovedSubcontractBean) dataObject.get(index);
                	
                	if (bean.getAcType() == null) {
                		bean.setAcType(TypeConstants.UPDATE_RECORD);
                	}
                	
                    CoeusVector cvSubcontracts = new CoeusVector();
                	RequesterBean requesterBean = new RequesterBean();
                    requesterBean.setFunctionType(UPDATE_SUBCONTRACTS);
                    requesterBean.setDataObject(bean);
                    
                    AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
                    appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
                    appletServletCommunicator.setRequest(requesterBean);
                    appletServletCommunicator.send();
                    ResponderBean responderBean = appletServletCommunicator.getResponse();

                    setSaveRequired(false);
                }
            }
        }
    }
   
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        subcontractTableCellEditor.stopCellEditing();
        String name = EMPTY_STRING;
        String[] subs = new String[cvEditData.size()];
        ProposalApprovedSubcontractBean proposalApprovedSubcontractBean;

        // Validate if the any of the row which contains the empty name then show the message
        if(cvEditData!= null){
            for(int index = 0; index < cvEditData.size(); index++){
                name = (String)subContractTableModel.getValueAt(index, NAME_COLUMN);
                if(name==null || name.trim().equals(EMPTY_STRING)){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_NAME));
                    subcontractTableCellEditor.txtComponent.requestFocus();
                    return false;
                }
            }
        }
        // If duplicate names and location type found, then show the message
        if(cvEditData!= null && cvEditData.size() > 0) {
            for (int index = 0; index < cvEditData.size(); index++){
                proposalApprovedSubcontractBean = (ProposalApprovedSubcontractBean) cvEditData.get(index);
                subs[index] = proposalApprovedSubcontractBean.getSubcontractName() + " " +
                		proposalApprovedSubcontractBean.getLocationTypeCode();
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
    }
    
    private boolean duplicates(final String[] subs) {
    	Set<String> dups = new HashSet<String>();
    	for (String i : subs) {
    		if (dups.contains(i)) return true;
    		dups.add(i);
    	}
    	return false;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(subcontractForm.btnAdd)){
            performAddAction();
        }else if(source.equals(subcontractForm.btnDelete)){
            performDeleteAction();
        }
    }
    
    private void performAddAction(){
        String orgname = "";
        String organId = "";
        try{
            CoeusSearch organizationSearch = null;
            organizationSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(),"ORGANIZATIONSEARCH",1);
            organizationSearch.showSearchWindow();
            HashMap organizationData = organizationSearch.getSelectedRow();
            if(organizationData != null){
                orgname = organizationData.get("ORGANIZATION_NAME").toString();
                organId = organizationData.get("ORGANIZATION_ID").toString();
            }
            else if(organizationData == null || organizationData.isEmpty()){
                return ;
            }
        }catch(Exception err){
            err.printStackTrace();
        }
        
        if(cvEditData!= null && cvEditData.size() > 0){
            subcontractTableCellEditor.stopCellEditing();
        }
        int rowCount =  subcontractForm.tblEditSubContract.getRowCount();
        if(rowCount  > 0){
            if(cvEditData!= null && cvEditData.size() > 0){
                for(int index =0 ; index < cvEditData.size(); index++){
                    ProposalApprovedSubcontractBean bean = (ProposalApprovedSubcontractBean)cvEditData.get(index);
                    // If the Amount or Name columns are empty then don't add the row.If any one of the
                    // field is entered then allow the user to add a new Row.
                    if(bean.getSubcontractName().trim().equals(EMPTY_STRING) && bean.getAmount()==0.00) return ;
                }
            }
        }
        
        ProposalApprovedSubcontractBean newBean = new ProposalApprovedSubcontractBean();
        newBean.setProposalNumber(propBaseBean.getProposalNumber());
        newBean.setSequenceNumber(propBaseBean.getSequenceNumber());
        newBean.setRowId(rowID++);
        newBean.setSubcontractName(orgname);
        newBean.setOrganizationId(organId);
        newBean.setUpdateUser(propBaseBean.getUpdateUser());
        newBean.setUpdateTimestamp(propBaseBean.getUpdateTimestamp());
        newBean.setAmount(0.00);
        newBean.setLocationTypeCode(OUTGOING_SUB_CODE);
        newBean.setLocationTypeDescription(OUTGOING_SUB_DESC);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        cvEditData.add(newBean);
        subContractTableModel.fireTableRowsInserted(subContractTableModel.getRowCount()+1,
        subContractTableModel.getRowCount()+1);
        
        int lastRow = subcontractForm.tblEditSubContract.getRowCount()-1;
        if(lastRow >= 0){
            subcontractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
            subcontractForm.tblEditSubContract.setRowSelectionInterval(lastRow,lastRow);
            subcontractForm.tblEditSubContract.scrollRectToVisible(
            subcontractForm.tblEditSubContract.getCellRect(lastRow, NAME_COLUMN, true));
            
        }
        subcontractForm.tblEditSubContract.editCellAt(lastRow,AMOUNT_COLUMN);
        subcontractForm.tblEditSubContract.requestFocusInWindow();
        subcontractTableCellEditor.txtComponent.setText(orgname);
        //subcontractTableCellEditor.txtComponent.requestFocus();
        
        if(subcontractForm.tblEditSubContract.getRowCount() == 0){
            subcontractForm.pnlAmount.setVisible(false);
            subcontractForm.tblAmount.setVisible(false);
        }else{
            subcontractForm.tblAmount.setVisible(true);
            subcontractForm.pnlAmount.setVisible(true);
        }
    }
    
    private void performDeleteAction(){
        subcontractTableCellEditor.stopCellEditing();
        String mesg = EMPTY_STRING;
        int rowIndex = subcontractForm.tblEditSubContract.getSelectedRow();
        if(rowIndex != -1 && rowIndex >= 0){
            mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                ProposalApprovedSubcontractBean deletedBean = (ProposalApprovedSubcontractBean)cvEditData.get(rowIndex);
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
                    subcontractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
                    subcontractForm.tblEditSubContract.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    subcontractForm.tblEditSubContract.scrollRectToVisible(
                    subcontractForm.tblEditSubContract.getCellRect(
                    rowIndex-1 ,0, true));
                }else{
                    if(subcontractForm.tblEditSubContract.getRowCount()>0){
                        subcontractForm.tblEditSubContract.setRowSelectionInterval(0,0);
                    }
                }
                
                if(subcontractForm.tblEditSubContract.getRowCount() == 0){
                    subcontractForm.pnlAmount.setVisible(false);
                    subcontractForm.tblAmount.setVisible(false);
                }else{
                    subcontractForm.tblAmount.setVisible(true);
                    subcontractForm.pnlAmount.setVisible(true);
                }
                
                if(rowIndex >0){
                    subcontractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
                    subcontractForm.tblEditSubContract.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    subcontractForm.tblEditSubContract.scrollRectToVisible(
                    subcontractForm.tblEditSubContract.getCellRect(rowIndex-1 ,0, true));
                    
                }else{
                    if(subcontractForm.tblEditSubContract.getRowCount()>0){
                        subcontractForm.tblEditSubContract.setColumnSelectionInterval(AMOUNT_COLUMN,AMOUNT_COLUMN);
                        subcontractForm.tblEditSubContract.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    
    private void setColumnData(){
        // Setting the table header, column width, renderer and editor for the
        // editing subcontract table
        JTableHeader tableHeader = subcontractForm.tblEditSubContract.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        subcontractForm.tblEditSubContract.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        subcontractForm.tblEditSubContract.setRowHeight(22);
        subcontractForm.tblEditSubContract.setShowHorizontalLines(true);
        subcontractForm.tblEditSubContract.setShowVerticalLines(true);
        subcontractForm.tblEditSubContract.setOpaque(false);
        subcontractForm.tblEditSubContract.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = subcontractForm.tblEditSubContract.getColumnModel().getColumn(NAME_COLUMN);
        column.setMaxWidth(450);
        column.setMinWidth(450);
        column.setPreferredWidth(450);
        column.setResizable(true);
        column.setCellEditor(subcontractTableCellEditor);
        column.setCellRenderer(subContractTableCellRenderer);

        column = subcontractForm.tblEditSubContract.getColumnModel().getColumn(TYPE_COLUMN);
        column.setMaxWidth(150);
        column.setMinWidth(150);
        column.setPreferredWidth(150);
        column.setResizable(false);
        column.setCellEditor(new DefaultCellEditor(getLocationTypeCmb()));
        column.setCellRenderer(subContractTableCellRenderer);
        
        column = subcontractForm.tblEditSubContract.getColumnModel().getColumn(AMOUNT_COLUMN);
        column.setMaxWidth(150);
        column.setMinWidth(150);
        column.setPreferredWidth(150);
        column.setResizable(true);
        column.setCellEditor(subcontractTableCellEditor);
        column.setCellRenderer(subContractTableCellRenderer);
        
        column = subcontractForm.tblEditSubContract.getColumnModel().getColumn(TYPE_CODE_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        subcontractForm.tblAmount.setRowHeight(22);
        subcontractForm.tblAmount.setShowHorizontalLines(true);
        subcontractForm.tblAmount.setShowVerticalLines(true);
        
        // Setting renderer and sizes for the amount related columns
        TableColumn amountColumn = subcontractForm.tblAmount.getColumnModel().getColumn(TOTAL_COLUMN);
        amountColumn.setMaxWidth(570);
        amountColumn.setMinWidth(570);
        amountColumn.setPreferredWidth(570);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
        
        amountColumn = subcontractForm.tblAmount.getColumnModel().getColumn(TOTAL_AMOUNT_COLUMN);
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
        //lookupWindow();
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    public void columnAdded(TableColumnModelEvent e) {
    }
    
    public void columnMarginChanged(ChangeEvent e) {
    }
    
    public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {

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
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }
    
    public class SubContractTableModel extends AbstractTableModel {
        
        private String colName[] = {"Subcontractor Name","Location Type","Amount","Location Type Code"};
        private Class colClass[] = {String.class, String.class, Double.class, Integer.class};
        
        public boolean isCellEditable(int row, int col){
            if(functionType== TypeConstants.DISPLAY_MODE){
                return false;
            }else if(col == 0){
                return false;
            }else{
                return true;
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
        
        public Object getValueAt(int row, int col){
        	if (row < cvEditData.size()) {
	        	ProposalApprovedSubcontractBean proposalApprovedSubcontractBean =
	        			(ProposalApprovedSubcontractBean) cvEditData.get(row);
	        	
	            switch(col){
	                case NAME_COLUMN:
	                    return proposalApprovedSubcontractBean.getSubcontractName();
	                case TYPE_COLUMN:
	                    return proposalApprovedSubcontractBean.getLocationTypeDescription();
	                case AMOUNT_COLUMN:
	                    return new Double(proposalApprovedSubcontractBean.getAmount());
	                case TYPE_CODE_COLUMN:
	                    return proposalApprovedSubcontractBean.getLocationTypeCode();	                	
	                    
	            }
        	}
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int col){
            if (cvEditData == null) return;
        	ComboBoxBean comboBoxBean = new ComboBoxBean();
        	int ix = 0;
            ProposalApprovedSubcontractBean proposalApprovedSubcontractBean =
            		(ProposalApprovedSubcontractBean) cvEditData.get(row);
            
            double cost = 0.00;
            switch(col){
                case NAME_COLUMN:
                    if(proposalApprovedSubcontractBean.getSubcontractName()!= null &&
                    		proposalApprovedSubcontractBean.getSubcontractName().equals(value.toString())) {
                        break;
                    }
                    proposalApprovedSubcontractBean.setSubcontractName(value.toString().trim());                    
                    if (proposalApprovedSubcontractBean.getAcType() != null &&
                    		proposalApprovedSubcontractBean.getAcType().equals(
                    TypeConstants.INSERT_RECORD)) {
                   			proposalApprovedSubcontractBean.getSubcontractName();
                    }
                    modified = true;
                    break;
                case TYPE_COLUMN:
		        	if (value.getClass() == ComboBoxBean.class) {
			        	comboBoxBean = (ComboBoxBean) value;
			        	ix = (Integer) cvLocationTypes.indexOf(comboBoxBean.getCode());
			        	//proposalApprovedSubcontractBean.setAwLocationTypeCode(proposalApprovedSubcontractBean.getLocationTypeCode());
			        	proposalApprovedSubcontractBean.setLocationTypeDescription(comboBoxBean.getDescription()); 
	                	proposalApprovedSubcontractBean.setLocationTypeCode(Integer.parseInt(comboBoxBean.getCode())); 
		        	}
                	break;
                case AMOUNT_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    if(proposalApprovedSubcontractBean.getAmount()==cost) {
                        break;
                    }
                    proposalApprovedSubcontractBean.setAmount(cost);
                    modified = true;
                    amountTableModel.fireTableDataChanged();
                    break;
                case TYPE_CODE_COLUMN:
                	// set above
                	break;
            }
            if(proposalApprovedSubcontractBean.getAcType() == null){
            	proposalApprovedSubcontractBean.setAcType(TypeConstants.UPDATE_RECORD);
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
                //case TYPE_COLUMN:
                //    return txtComponent.getText();
                case AMOUNT_COLUMN:
                    return txtCurrencyComponent.getValue();
                    
            }
            return txtCurrencyComponent;
        }
    }
    
    public class SubContractTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        private JTextField txtComponent, txtType;
        private DollarCurrencyTextField txtCurrencyComponent;
        private JLabel lblText,lblCurrency,lblType;
        
        public SubContractTableCellRenderer(){
            lblText = new JLabel();
            lblType = new JLabel();
            lblCurrency = new JLabel();
            lblText.setOpaque(true);
            lblCurrency.setOpaque(true);
            lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
            lblType.setOpaque(true);
            txtComponent = new JTextField();
            txtCurrencyComponent = new DollarCurrencyTextField();
            txtType = new JTextField();
            
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtType.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            
            
            switch(col){
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
        
        public Object getCellEditorValue() {
            switch(column){
                case NAME_COLUMN:
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
        ProposalApprovedSubcontractBean approvedSubcontractBean;
        try {
            cvApprovedSubcontracts = queryEngine.getDetails(queryKey,
            ProposalApprovedSubcontractBean.class);
            if (cvApprovedSubcontracts != null && cvApprovedSubcontracts.size() > 0) {
                cvApprovedSubcontracts.sort("rowId", false);
                approvedSubcontractBean = (ProposalApprovedSubcontractBean) cvApprovedSubcontracts.get(0);
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
            setFormData(propBaseBean);
            cvDeletedData.clear();
            setRefreshRequired(false);
        }
    }
    
    
    
     // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
         javax.swing.InputMap im = subcontractForm.tblEditSubContract.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = subcontractForm.tblEditSubContract.getActionMap().get(im.get(tab));
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
        subcontractForm.tblEditSubContract.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = subcontractForm.tblEditSubContract.getActionMap().get(im.get(shiftTab));
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
        subcontractForm.tblEditSubContract.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    
    public void cleanUp(){
        jscrPn.remove(subcontractForm);
        jscrPn = null;
        
        cvEditData = null;
        cvDisplayData = null;
        cvDeletedData = null;
        subcontractForm = null;
        subContractTableModel = null;
        subcontractTableCellEditor = null;
        subContractTableCellRenderer = null;
        amountTableCellRenderer = null;

        amountTableModel = null;
        amountCellEditor = null;
        propBaseBean = null;
    }
}
