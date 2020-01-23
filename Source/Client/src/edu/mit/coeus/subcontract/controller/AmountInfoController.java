/*
 * SubContractAmountInfoController.java
 *
 * Created on September 14, 2004, 3:21 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.subcontract.bean.SubcontractContactDetailsBean;
import edu.mit.coeus.subcontract.controller.SubcontractController;
import edu.mit.coeus.subcontract.controller.AmountInfoChangeController;
import edu.mit.coeus.subcontract.gui.AmountInfoForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.subcontract.bean.SubContractAmountInfoBean;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.subcontract.gui.AmountInfoCommentsForm;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.awt.event.*;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class AmountInfoController extends SubcontractController implements ActionListener {
	
	private AmountInfoForm amountInfoForm;
	private HistoryOfChangesTableModel historyOfChangesTableModel;
	private HistoryOfChangesTableRenderer historyOfChangesTableRenderer;
	private AmountTableModel amountTableModel;
	private AmountTableCellRenderer amountTableCellRenderer;
	
	private CoeusVector cvTotalData;
	
	// table columns for the Total Table field
	private static final int TOTAL_COLUMN = 0;
	private static final int OBLIGATED_TOTAL_COLUMN = 1;
	private static final int ANTICIPATED_TOTAL_COLUMN = 2;
	
	private CoeusVector cvTableData;
	private static final int LINE_NUMBER_COLUMN = 1;
	private static final int EFF_DATE_COLUMN = 2;
	private static final int OBLIGATED_AMOUNT_COLUMN = 3;
	private static final int ANTICIPATED_AMOUNT_COLUMN = 4;
        
        // Modified for COEUSQA-1412 Subcontract Module changes - Start
//        private static final int COMMENTS_COLUMN = 5;
        private static final int PERF_START_DATE_COLUMN = 5;
        private static final int PERF_END_DATE_COLUMN = 6;
        private static final int MODIFICATION_NUM_COLUMN = 7;
        private static final int MODIFI_EFFECTIVE_DATE_COLUMN = 8;
        private static final int COMMENTS_COLUMN = 9;
        // Modified for COEUSQA-1412 Subcontract Module changes - End
	
	private static final String EMPTY_STRING = "";
	private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
	private DateUtils dtUtils;
	private SimpleDateFormat dtFormat  = new SimpleDateFormat("MM/dd/yyyy");
	private CoeusMessageResources coeusMessageResources;
	private QueryEngine queryEngine;
	private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
	getDefaults().get("Panel.background");
	private AmountInfoChangeController amountInfoChangeController;
	private SubContractBean subContractBean;
        //Added for Case #2090 start 1
	private AmountInfoCommentsForm amountInfoCommentsForm;
        // Modified for COEUSQA-1412 Subcontract Module changes - Start
//        private static final int MORE_COMMENTS = 6;
        private static final int MORE_COMMENTS = 10;
        // Modified for COEUSQA-1412 Subcontract Module changes - End
        private static final int PDF_DOCUMENT_COLUMN = 0;
        private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
        private HistoryOfChangesTableMoreEditor historyOfChangesTableMoreEditor;
        private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
        //Added for Case #2090 end 1
        private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
        private DateUtils dateUtils;
        
        /* JM 2-27-2015 modifications to allow tab access if user has only this right */
        private boolean userHasModify = false;
        private boolean userHasCreate = false;
        /* JM END */
        
	/**
	 * Creates a new instance of SubContractAmountInfoController
	 * @param subContractBean SubContractBean
	 * @param functionType char
	 */
	public AmountInfoController(SubContractBean subContractBean,char functionType) {
		super(subContractBean);
		this.subContractBean = subContractBean;
		coeusMessageResources = CoeusMessageResources.getInstance();
		queryEngine = QueryEngine.getInstance();
                dateUtils = new DateUtils();
		registerComponents();
		formatFields();
		setFormData(subContractBean);
		
		/* JM 2-27-2015 no access if not modifier */
        userHasModify = subContractBean.getHasModify();
        userHasCreate = subContractBean.getHasCreate();
		if (!userHasModify && !userHasCreate) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */  
		
		setFunctionType(functionType);
		setColumnData();
		
	}
	
	/**
	 * display the form
	 */
	public void display() {
	}
	
	/**
	 * setting the fields
	 */
	public void formatFields() {
		amountInfoForm.txtAnticipated.setEditable(false);
		amountInfoForm.txtObligated.setEditable(false);
		amountInfoForm.txtAvailable.setEditable(false);
		amountInfoForm.txtReleased.setEditable(false);
                amountInfoForm.btnModifyAgreement.setEnabled(false);
                amountInfoForm.btnDeleteAgreement.setEnabled(false);
                // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
                amountInfoForm.txtPerformStartDate.setEditable(false);
                amountInfoForm.txtPerformEndDate.setEditable(false);
                amountInfoForm.txtModificationNum.setEditable(false);
                amountInfoForm.txtModificationEffecDate.setEditable(false);
                // Added for COEUSQA-1412 Subcontract Module changes - Change - End

		if (getFunctionType() == DISPLAY_SUBCONTRACT) {
			amountInfoForm.btnChange.setEnabled(false);
		}
	}
	
	/**
	 * getting the form UI
	 * @return Component
	 */
	public java.awt.Component getControlledUI() {
		return amountInfoForm;
	}
	
	/**
	 * getting the form data
	 * @return Object
	 */
	public Object getFormData() {
		return null;
	}
	
	/**
	 * setting up the amount field
	 * @param cvData CoeusVector
	 */
        public void setAmounts(CoeusVector cvData ) {
            amountInfoForm.txtObligated.setText(cvData.get(0).toString());
            amountInfoForm.txtAnticipated.setText(cvData.get(1).toString());
            amountInfoForm.txtAvailable.setText(cvData.get(3).toString());
            amountInfoForm.txtReleased.setText(cvData.get(2).toString());
            // Modified for COEUSQA-1412 Subcontract Module changes - Start
            if(cvTableData != null && !cvTableData.isEmpty()){
                SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvTableData.get(cvTableData.size()-1);
                if(subContractAmountInfoBean.getPerformanceStartDate() != null){
                    amountInfoForm.txtPerformStartDate.setText(
                            dateUtils.formatDate(subContractAmountInfoBean.getPerformanceStartDate().toString(),DATE_FORMAT_DISPLAY));
                }
                if(subContractAmountInfoBean.getPerformanceEndDate() != null){
                    amountInfoForm.txtPerformEndDate.setText(
                            dateUtils.formatDate(subContractAmountInfoBean.getPerformanceEndDate().toString(),DATE_FORMAT_DISPLAY));
                }
                amountInfoForm.txtModificationNum.setText(subContractAmountInfoBean.getModificationNumber());
                
                if(subContractAmountInfoBean.getModificationEffectiveDate() != null){
                    amountInfoForm.txtModificationEffecDate.setText(
                            dateUtils.formatDate(subContractAmountInfoBean.getModificationEffectiveDate().toString(),DATE_FORMAT_DISPLAY));
                }
            }
            // Modified for COEUSQA-1412 Subcontract Module changes - End
        }
	
	/**
	 * registering the components
	 */
	public void registerComponents() {
		amountInfoForm = new AmountInfoForm();
		amountInfoChangeController = new AmountInfoChangeController(subContractBean, getFunctionType());
		cvTableData = new CoeusVector();
		dtUtils = new DateUtils();
		historyOfChangesTableModel = new HistoryOfChangesTableModel();
		historyOfChangesTableRenderer = new HistoryOfChangesTableRenderer();
		amountInfoForm.tblHistoryOfChange.setModel(historyOfChangesTableModel);
		amountInfoForm.btnChange.addActionListener(this);
		amountInfoForm.tblHistoryOfChange.setBackground(disabledBackground);
		amountInfoForm.tblTotal.setBackground(disabledBackground);
		amountTableModel = new AmountTableModel();
		amountTableCellRenderer = new AmountTableCellRenderer();
		amountInfoForm.tblTotal.setModel(amountTableModel);
		amountInfoForm.btnModifyAgreement.addActionListener(this);
                amountInfoForm.btnDeleteAgreement.addActionListener(this);
                //Code added for princeton enhancement case#2802 - starts
                amountInfoForm.tblHistoryOfChange.addMouseListener(new MouseAdapter(){
                   public void mouseClicked(MouseEvent e){
                       int row = amountInfoForm.tblHistoryOfChange.getSelectedRow();
                       if(row != -1){
                            SubContractAmountInfoBean subContractAmountInfoBean =
                                    (SubContractAmountInfoBean)cvTableData.elementAt(row);
                            if(subContractAmountInfoBean.getFileName() != null 
                                    && !subContractAmountInfoBean.getFileName().equals("")
                                    && getFunctionType()!= DISPLAY_SUBCONTRACT){
                                amountInfoForm.btnDeleteAgreement.setEnabled(true);
                            } else {
                                amountInfoForm.btnDeleteAgreement.setEnabled(false);
                            }
                            if (getFunctionType() != DISPLAY_SUBCONTRACT) {
                                amountInfoForm.btnModifyAgreement.setEnabled(true);
                            }                            
                            
                       }
                   } 
                });                
                //Code added for princeton enhancement case#2802 - ends
	}
	/**
	 * Setting up the table column values
	 */
	private void setColumnData() {
		JTableHeader tableHeader = amountInfoForm.tblHistoryOfChange.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setFont(CoeusFontFactory.getLabelFont());
		
                
		amountInfoForm.tblHistoryOfChange.setRowHeight(22);
		amountInfoForm.tblHistoryOfChange.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		amountInfoForm.tblTotal.setRowHeight(22);
		amountInfoForm.tblTotal.setFont(CoeusFontFactory.getLabelFont());
		amountInfoForm.tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		amountInfoForm.tblHistoryOfChange.setSelectionMode(
		DefaultListSelectionModel.SINGLE_SELECTION);
		
		TableColumn column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(PDF_DOCUMENT_COLUMN);
                // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                column.setHeaderRenderer(new EmptyHeaderRenderer());
                SelectedAmountInfoTableHeaderRenderer selectedAmountInfoTableHeaderRenderer = new SelectedAmountInfoTableHeaderRenderer();
                column.setHeaderRenderer(selectedAmountInfoTableHeaderRenderer);
                // Modified for COEUSQA-1412 Subcontract Module changes - End
		column.setPreferredWidth(20);
		column.setResizable(false);
		column.setCellRenderer(historyOfChangesTableRenderer);
                historyOfChangesTableMoreEditor
                                = new HistoryOfChangesTableMoreEditor();
                column.setCellEditor(historyOfChangesTableMoreEditor); 
                
                column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(LINE_NUMBER_COLUMN);
		column.setPreferredWidth(70);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
		
		column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(EFF_DATE_COLUMN);
//		column.setPreferredWidth(90);
                column.setPreferredWidth(80);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
		
		column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(OBLIGATED_AMOUNT_COLUMN);
//		column.setPreferredWidth(195);
                column.setPreferredWidth(100);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
		
		column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(ANTICIPATED_AMOUNT_COLUMN);
//		column.setPreferredWidth(195);
                column.setPreferredWidth(100);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
		
		column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(COMMENTS_COLUMN);
		column.setPreferredWidth(225);
		column.setResizable(false);
		column.setCellRenderer(historyOfChangesTableRenderer);
                
                // Added for COEUSQA-1412 Subcontract Module changes - Start
                column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(PERF_START_DATE_COLUMN);
                column.setPreferredWidth(150);
                column.setResizable(true);
                column.setCellRenderer(historyOfChangesTableRenderer);
                
                column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(PERF_END_DATE_COLUMN);
                column.setPreferredWidth(150);
                column.setResizable(true);
                column.setCellRenderer(historyOfChangesTableRenderer);
                
                column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(MODIFICATION_NUM_COLUMN);
                column.setPreferredWidth(130);
                column.setResizable(true);
                column.setCellRenderer(historyOfChangesTableRenderer);
                
                column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(MODIFI_EFFECTIVE_DATE_COLUMN);
                column.setPreferredWidth(125);
                column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
                // Added for COEUSQA-1412 Subcontract Module changes - Start
                
                
		//Added for Case #2090 start 2
                column = amountInfoForm.tblHistoryOfChange.getColumnModel().getColumn(MORE_COMMENTS);
                column.setHeaderRenderer(new EmptyHeaderRenderer());
		column.setPreferredWidth(20);
		column.setResizable(false);
		column.setCellRenderer(historyOfChangesTableRenderer);
                historyOfChangesTableMoreEditor
                                = new HistoryOfChangesTableMoreEditor();
                column.setCellEditor(historyOfChangesTableMoreEditor);
                //Added for Case #2090 end 2               
		//for the Total table
		JTableHeader totalTableHeader = amountInfoForm.tblTotal.getTableHeader();
		totalTableHeader.setVisible(false);
		column = amountInfoForm.tblTotal.getColumnModel().getColumn(TOTAL_COLUMN);
		
		column.setPreferredWidth(210);
		column.setResizable(true);
		column.setCellRenderer(amountTableCellRenderer);
		
		
		column = amountInfoForm.tblTotal.getColumnModel().getColumn(OBLIGATED_TOTAL_COLUMN);
		column.setPreferredWidth(220);
		column.setResizable(true);
		column.setCellRenderer(amountTableCellRenderer);
		
		column = amountInfoForm.tblTotal.getColumnModel().getColumn(ANTICIPATED_TOTAL_COLUMN);
		column.setPreferredWidth(200);
		column.setResizable(true);
		column.setCellRenderer(amountTableCellRenderer);
		
	}
	/**
	 * saving the form data
	 * @throws CoeusException CoeusException
	 */
	public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
		
	}
	
	/**
	 * setting up the form data in the fields
	 * @param data Object
	 */
	public void setFormData(Object data) {
		try {
			cvTableData = queryEngine.getDetails(queryKey,SubContractAmountInfoBean.class);
			if (cvTableData != null && cvTableData.size() > 0) {
				cvTableData.sort("lineNumber");
			}
		} catch (CoeusException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * validate method
	 * @return boolean
	 */
	public boolean validate() {
		return true;
	}
	
	/**
	 * cleaning up the fields
	 */
	public void cleanUp() {
                //Added for Case #2090 start 3
                amountInfoCommentsForm = null;
                historyOfChangesTableMoreEditor = null;
                //Added for Case #2090 end 3
		amountInfoForm = null;
		historyOfChangesTableModel = null;
		historyOfChangesTableRenderer = null;
		amountTableModel = null;
		amountTableCellRenderer = null;
		cvTotalData = null;
		cvTableData = null;
		dtUtils = null;
		dtFormat  = null;
		coeusMessageResources = null;
		subContractBean = null;
		amountInfoChangeController.cleanUp();
		amountInfoChangeController = null;
	}
	
        /**
         * action performed
         * @param actionEvent ActionEvent
         */
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            int row = amountInfoForm.tblHistoryOfChange.getSelectedRow();
            if (actionEvent.getSource().equals(amountInfoForm.btnChange)) {
                //call the amount change tab and get the new values and set to the table
                setAmountData();
            } 
            //Code added for princeton enhancement case#2802 - starts
            else if (row != -1){
                SubContractAmountInfoBean subContractAmountInfoBean =
                        (SubContractAmountInfoBean)cvTableData.elementAt(row);
                if(actionEvent.getSource().equals(amountInfoForm.btnModifyAgreement)) {
                    String[] fileExtension = {"pdf"};
                    CoeusFileChooser fileChooser = new CoeusFileChooser(mdiForm);
                    fileChooser.setAcceptAllFileFilterUsed(true);
                    //Code commented for Case#3648 - Uploading non-pdf files
                    //fileChooser.setSelectedFileExtension(fileExtension);
                    fileChooser.showFileChooser();
                    if(fileChooser.isFileSelected()){
                        String fileName = fileChooser.getSelectedFile();
                        if(fileName != null && !fileName.trim().equals("")){
                            int index = fileName.lastIndexOf('.');
                            if(index != -1 && index != fileName.length()){
                                subContractAmountInfoBean.setFileName(fileChooser.getFileName().getName());
                                subContractAmountInfoBean.setDocument(fileChooser.getFile());
                                //Added with case 4007: Icon based on attachment type
                                CoeusDocumentUtils docTypeUtils = CoeusDocumentUtils.getInstance();
                                CoeusAttachmentBean attachmentBean = new CoeusAttachmentBean(fileName,fileChooser.getFile());
                                subContractAmountInfoBean.setMimeType(docTypeUtils.getDocumentMimeType(attachmentBean));
                                //4007:End
                                subContractAmountInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                                queryEngine.insert(queryKey,subContractAmountInfoBean);
                            }else{
                                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                        "correspType_exceptionCode.1012"));
                            }
                        }
                    }
                } else if(actionEvent.getSource().equals(amountInfoForm.btnDeleteAgreement)) {
                    int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1217"),
                        CoeusOptionPane.OPTION_YES_NO, JOptionPane.YES_OPTION);
                    if(option == JOptionPane.YES_OPTION){
                        subContractAmountInfoBean.setFileName(null);
                        subContractAmountInfoBean.setMimeType(null);
                        subContractAmountInfoBean.setDocument(new byte[0]);
                        subContractAmountInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.insert(queryKey,subContractAmountInfoBean);
                    }
                }
                historyOfChangesTableModel.fireTableDataChanged();
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setBean(subContractAmountInfoBean);
                fireBeanUpdated(beanEvent);                
            } else {
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1209"));                
            }
            //Code added for princeton enhancement case#2802 - ends
        }
	
	/**
	 * Getting and setting the History of Changes values
	 */
	private void setAmountData() {
		//calling the change dialog window and get the new values as a coeusvector
            SubContractAmountInfoBean subContractAmountInfoBean = new SubContractAmountInfoBean();
            int rowCount = historyOfChangesTableModel.getRowCount();
            subContractAmountInfoBean.setLineNumber(rowCount + 1);
            subContractAmountInfoBean.setSubContractCode(subContractBean.getSubContractCode());
            subContractAmountInfoBean.setSequenceNumber(subContractBean.getSequenceNumber());
		CoeusVector cvChangedData = amountInfoChangeController.showChangeInfo(subContractAmountInfoBean);
		
		if (cvChangedData != null && cvChangedData.size() > 0) {
			
			// get the bean and set the values to the text fields.
//			SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvChangedData.get(0);
			subContractAmountInfoBean = (SubContractAmountInfoBean)cvChangedData.get(0);                        
			if (subContractAmountInfoBean != null) {
				subContractAmountInfoBean.setAcType(TypeConstants.INSERT_RECORD);
				rowCount = historyOfChangesTableModel.getRowCount();
				subContractAmountInfoBean.setLineNumber(rowCount + 1);
				subContractAmountInfoBean.setSubContractCode(subContractBean.getSubContractCode());
				subContractAmountInfoBean.setSequenceNumber(subContractBean.getSequenceNumber());
				
				if (cvTableData != null) {
					cvTableData.addAll(cvChangedData);
					historyOfChangesTableModel.setData(cvTableData);
					double anticipatedAmount = cvTableData.sum("anticipatedChange");
					double obligatedAmount = cvTableData.sum("obligatedChange");
					amountInfoForm.txtAnticipated.setValue(anticipatedAmount);
					subContractAmountInfoBean.setAnticipatedAmount(anticipatedAmount);
					amountInfoForm.txtObligated.setValue(obligatedAmount);
					subContractAmountInfoBean.setObligatedAmount(obligatedAmount);
                                        // Added for COEUSQA-1412 Subcontract Module changes - Start
                                        if(subContractAmountInfoBean.getPerformanceStartDate() != null){
                                            amountInfoForm.txtPerformStartDate.setText(
                                                    dateUtils.formatDate(subContractAmountInfoBean.getPerformanceStartDate().toString(),DATE_FORMAT_DISPLAY));
                                        }
                                        if(subContractAmountInfoBean.getPerformanceEndDate() != null){
                                            amountInfoForm.txtPerformEndDate.setText(
                                                    dateUtils.formatDate(subContractAmountInfoBean.getPerformanceEndDate().toString(),DATE_FORMAT_DISPLAY));
                                        }
                                        amountInfoForm.txtModificationNum.setText(subContractAmountInfoBean.getModificationNumber());
                                        
                                        if(subContractAmountInfoBean.getModificationEffectiveDate() != null){
                                            amountInfoForm.txtModificationEffecDate.setText(
                                                    dateUtils.formatDate(subContractAmountInfoBean.getModificationEffectiveDate().toString(),DATE_FORMAT_DISPLAY));
                                        }
					// Added for COEUSQA-1412 Subcontract Module changes - End
					//available amount = obligated - released
					double amountAvailable = Double.parseDouble(amountInfoForm.txtObligated.getValue()) - Double.parseDouble(amountInfoForm.txtReleased.getValue());
					amountInfoForm.txtAvailable.setValue(amountAvailable);
					
					double amountReleased = Double.parseDouble(amountInfoForm.txtReleased.getValue());
					amountInfoForm.txtReleased.setValue(amountReleased);
					historyOfChangesTableModel.fireTableDataChanged();
					amountTableModel.fireTableDataChanged();
				}
				//insert into the query engine
				queryEngine.insert(queryKey,subContractAmountInfoBean);
				//Fire other tabs with Modified event
				BeanEvent beanEvent = new BeanEvent();
				beanEvent.setSource(this);
				beanEvent.setBean(subContractAmountInfoBean);
				fireBeanUpdated(beanEvent);
                                amountInfoForm.btnModifyAgreement.setEnabled(false);
                                amountInfoForm.btnDeleteAgreement.setEnabled(false);
			}
		}
		
	}
	/**
	 * Setter for property refreshRequired of super
	 * @param refreshRequired boolean
	 */
	public void setRefreshRequired(boolean refreshRequired) {
		super.setRefreshRequired(refreshRequired);
	}
	
	/**
	 * Getter for property refreshRequired
	 * @return boolean
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
	
	/**
	 * This class will specify the model for displaying
	 * Total Amount for both Obligated and Anticipated fields
	 */
	public class AmountTableModel extends AbstractTableModel {
		private String colName[] = {"Total: ", "",""};
		private Class colClass[] = {String.class, Double.class,Double.class};
		
		/**
		 * This method will check whether the given field is ediatble or not
		 * @param row int
		 * @param col int
		 * @return boolean
		 */
		public boolean isCellEditable(int row, int col){
			return false;
		}
		/**
		 * Thie mthod will return the column count
		 * @return int
		 */
		public int getColumnCount(){
			return colName.length;
		}
		
		/**
		 * This method will return the column class
		 * @param colIndex int
		 * @return Class
		 */
		public Class getColumnClass(int colIndex){
			return colClass[colIndex];
		}
		/**
		 * This method will return the row count
		 * @return int
		 */
		public int getRowCount(){
			return 1;
		}
		
		/**
		 * This method is to set the data in the table model
		 * @param cvTotalData CoeusVector
		 */
		public void setData(CoeusVector cvTotalData){
			cvTotalData = cvTotalData;
		}
		/**
		 * To get the column name
		 * @param column int
		 * @return String
		 */
		public String getColumnName(int column){
			return colName[column];
		}
		
		/**
		 * To get the value depends on the row and column
		 * @param row int
		 * @param col int
		 * @return Object
		 */
		public Object getValueAt(int row, int col) {
			String name = "Total: ";
			double totalAmount = 0.00;
			
			switch (col) {
				case TOTAL_COLUMN:
					return name;
				case OBLIGATED_TOTAL_COLUMN:
					totalAmount = cvTableData.sum("obligatedChange");
					return new Double(totalAmount);
				case ANTICIPATED_TOTAL_COLUMN:
					totalAmount = cvTableData.sum("anticipatedChange");
					return new Double(totalAmount);
			}
			return EMPTY_STRING;
		}
	}
	
	/**
	 * This is an inner class will specify the renderer
	 */
	public class AmountTableCellRenderer extends DefaultTableCellRenderer
	implements TableCellRenderer {
		private JLabel lblText;
		
		private DollarCurrencyTextField txtCurrencyField;
		/**
		 * Default constructor for the renderer class
		 */
		public AmountTableCellRenderer(){
			
			lblText = new JLabel();
			lblText.setBorder(new EmptyBorder(0,0,0,0));
			lblText.setOpaque(true);
			txtCurrencyField=new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			txtCurrencyField.setHorizontalAlignment(txtCurrencyField.RIGHT);
			
		}
		/**
		 * This will return the rendered component depends on the row and column
		 * @param table javax.swing.JTable
		 * @param value Object
		 * @param isSelected boolean
		 * @param hasFocus boolean
		 * @param row int
		 * @param col int
		 * @return java.awt.Component
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			
			Component returnComponent = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			switch(col){
				case TOTAL_COLUMN:
					JLabel lblTotal = (JLabel)returnComponent;
					lblTotal.setFont(CoeusFontFactory.getLabelFont());
					lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					return lblTotal;
					
				case OBLIGATED_TOTAL_COLUMN:
					if (value != null && !EMPTY_STRING.equals(value)) {
						txtCurrencyField.setValue(new Double(value.toString()).doubleValue());
						txtCurrencyField.setText(txtCurrencyField.getText());
					} else {
						txtCurrencyField.setValue(0.00);
						txtCurrencyField.setText(txtCurrencyField.getText());
					}
					lblText.setText(txtCurrencyField.getText());
					lblText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					lblText.setBackground(returnComponent.getBackground());
					lblText.setForeground(returnComponent.getForeground());
					return lblText;
					
				case ANTICIPATED_TOTAL_COLUMN:
					if (value != null && !EMPTY_STRING.equals(value)) {
						txtCurrencyField.setValue(new Double(value.toString()).doubleValue());
						txtCurrencyField.setText(txtCurrencyField.getText());
					} else {
						txtCurrencyField.setValue(0.00);
						txtCurrencyField.setText(txtCurrencyField.getText());
					}
					lblText.setText(txtCurrencyField.getText());
					lblText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					lblText.setBackground(returnComponent.getBackground());
					lblText.setForeground(returnComponent.getForeground());
					return lblText;
			}
			return returnComponent;
		}
		
	} // end of AmountTableCellRenderer
	
	/**
	 * This is an inner class specify the table model for the indirect cost
	 */
	class HistoryOfChangesTableModel extends AbstractTableModel{
		
		// IndirectCostTableModel()
                //Added for Case #2090 start 4
            
                String LINE_NUMBER = "<html>Line No</html>";
                String EFF_DATE = "<html>Eff Date</html>";
                String OBLIGATED = "<html>Obligated</html>";
                String ANTICIPATED = "<html>Anticipated</html>";
                String PERIOD_OF_PERF_START_DATE = "<html>Period of Performance<br><center>Start Date</center></html>";
                String PERIOD_OF_PERF_END_DATE = "<html>Period of Performance<br><center>End Date</center></html>";
                String MODIFICATION_NUMBER = "<html>Modification<br><center>Number</center></html>";
                String MODIFICATION_EFF_DATE = "<html>Modification<br><center>Effective Date</center></html>";
                String COMMENTS = "<html>Comments</html>";
                
		String colNames[] = {".",LINE_NUMBER,EFF_DATE, OBLIGATED,ANTICIPATED,PERIOD_OF_PERF_START_DATE,
                PERIOD_OF_PERF_END_DATE,MODIFICATION_NUMBER,MODIFICATION_EFF_DATE,COMMENTS,"."};
		Class[] colTypes = new Class [] {Boolean.class,String.class,DateUtils.class,Double.class,
                Double.class,DateUtils.class,DateUtils.class, String.class,DateUtils.class,String.class,Boolean.class};
		//Added for Case #2090 end 4
		/**
		 * This method will check whether the given field is ediatble or not
		 * @param row int
		 * @param col int
		 * @return boolean
		 */
		public boolean isCellEditable(int row, int col){
                        //Added for Case #2090 start 5
                        if(col == MORE_COMMENTS || col == PDF_DOCUMENT_COLUMN){
                            return true;
                        }
                        //Added for Case #2090 end 5
			return false;
		}
		/**
		 * Thie mthod will return the column count
		 * @return int
		 */
		public int getColumnCount() {
			return colNames.length;
		}
		
		/**
		 * This method will return the row count
		 * @return int
		 */
		public int getRowCount() {
			if( cvTableData  == null )
				return 0;
			else
				return cvTableData .size();
			
		}
		
		/**
		 * This method will return the column class
		 * @param columnIndex int
		 * @return Class
		 */
		public Class getColumnClass(int columnIndex) {
			return colTypes [columnIndex];
		}
		
		/**
		 * To get the column name
		 * @param column int
		 * @return String
		 */
		public String getColumnName(int column) {
			return colNames[column];
		}
		
		/**
		 * setting up the table data
		 * @param cvTableData CoeusVector
		 */
		public void setData(CoeusVector cvTableData){
			cvTableData = cvTableData;
		}
		
		/**
		 * To get the value depends on the row and column
		 * @param rowIndex int
		 * @param columnIndex int
		 * @return Object
		 */
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvTableData.elementAt(rowIndex);;
			
			switch(columnIndex) {
				case LINE_NUMBER_COLUMN:
					return new Integer(subContractAmountInfoBean.getLineNumber());
				case EFF_DATE_COLUMN:
					return subContractAmountInfoBean.getEffectiveDate();
					
				case OBLIGATED_AMOUNT_COLUMN:
					return new Double(subContractAmountInfoBean.getObligatedChange());
				case ANTICIPATED_AMOUNT_COLUMN:
					return new Double(subContractAmountInfoBean.getAnticipatedChange());
				case COMMENTS_COLUMN:
					return subContractAmountInfoBean.getComments();
                                // Added for COEUSQA-1412 Subcontract Module changes - Start
                                case PERF_START_DATE_COLUMN:
                                    return subContractAmountInfoBean.getPerformanceStartDate();
                                case PERF_END_DATE_COLUMN:
                                    return subContractAmountInfoBean.getPerformanceEndDate();
                                case MODIFICATION_NUM_COLUMN:
                                    return subContractAmountInfoBean.getModificationNumber();
                                case MODIFI_EFFECTIVE_DATE_COLUMN:
                                    return subContractAmountInfoBean.getModificationEffectiveDate();
                                // Added for COEUSQA-1412 Subcontract Module changes - End
			}
			return EMPTY_STRING;
			
		} // end of getValueAt()
	} // end of IndirectCostTableModel
	
	/**
	 * Inner class for the Amount table field
	 */
	class HistoryOfChangesTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
		
		private JLabel lblText;
		
		private DollarCurrencyTextField txtCurrencyField;
		//case #2090
                private JButton btnMoreComments, btnDocument;
                private ImageIcon imgIcnDesc, imgIcnDocument;
                //case #2090
		/**
		 * Default constructor
		 */
		public HistoryOfChangesTableRenderer(){
			
			lblText = new JLabel();
			lblText.setBorder(new EmptyBorder(0,0,0,0));
			lblText.setOpaque(true);
			lblText.setFont(CoeusFontFactory.getNormalFont());
			txtCurrencyField=new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			txtCurrencyField.setHorizontalAlignment(txtCurrencyField.RIGHT);
			txtCurrencyField.setFont(CoeusFontFactory.getNormalFont());
                        //case #2090
                        imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
                        btnMoreComments  = new JButton();
                        btnMoreComments.setIcon(imgIcnDesc);
                        //case #2090
                        //Code added for Princeton enhancements case#2802 - starts
                        // For PDF documentbutton to view the uploaded document
                        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//                        imgIcnDocument = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
                        imgIcnDocument = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
                        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
                        btnDocument = new JButton();
                        // # Commented for case 3855 need to add attachment specific icons.
                     //   btnDocument.setIcon(imgIcnDocument);
                        //Code added for Princeton enhancements case#2802 - ends                        
		}
		
		/**
		 * getting the renderer for each column
		 * @param table JTable
		 * @param value Object
		 * @param isSelected boolean
		 * @param hasFocus boolean
		 * @param row int
		 * @param col int
		 * @return Component
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			Component returnComponent = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
                        //Code added for Princeton enhancements case#2802 - starts
                        //To display the rejected invoice in red color.
                        SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvTableData.elementAt(row);                        
			switch(col){
				case LINE_NUMBER_COLUMN :
					
					return returnComponent;
				case EFF_DATE_COLUMN:
                                case PERF_START_DATE_COLUMN:
                                case PERF_END_DATE_COLUMN:
                                case MODIFI_EFFECTIVE_DATE_COLUMN:
                                	if (value != null && !value.toString().trim().equals(EMPTY_STRING)) {
						value = dtUtils.formatDate(value.toString(), REQUIRED_DATEFORMAT);
						lblText.setText(value.toString());
					} else {
						lblText.setText(EMPTY_STRING);
					}
					lblText.setBackground(returnComponent.getBackground());
					lblText.setForeground(returnComponent.getForeground());
					lblText.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					return lblText;
					
				case OBLIGATED_AMOUNT_COLUMN:
					
					if (value != null && !EMPTY_STRING.equals(value)) {
						txtCurrencyField.setValue(new Double(value.toString()).doubleValue());
						txtCurrencyField.setText(txtCurrencyField.getText());
					} else {
						txtCurrencyField.setValue(0.00);
						txtCurrencyField.setText(txtCurrencyField.getText());
					}
					lblText.setText(txtCurrencyField.getText());
					lblText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					lblText.setBackground(returnComponent.getBackground());
					lblText.setForeground(returnComponent.getForeground());
					return lblText;
				case ANTICIPATED_AMOUNT_COLUMN:
					if (value != null && !EMPTY_STRING.equals(value)) {
						txtCurrencyField.setValue(new Double(value.toString()).doubleValue());
						txtCurrencyField.setText(txtCurrencyField.getText());
					} else {
						txtCurrencyField.setValue(0.00);
						txtCurrencyField.setText(txtCurrencyField.getText());
					}
					lblText.setText(txtCurrencyField.getText());
					lblText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					lblText.setBackground(returnComponent.getBackground());
					lblText.setForeground(returnComponent.getForeground());
					return lblText;
				case COMMENTS_COLUMN:
				case MODIFICATION_NUM_COLUMN:	
					return returnComponent;
                                
                            //Added for Case #2090 start 6
                            case MORE_COMMENTS:
                                return btnMoreComments;
                            //Added for Case #2090 end 6
                            //Code added for princeton enhancement case#2802 - starts    
                            case PDF_DOCUMENT_COLUMN:
                                if(subContractAmountInfoBean.getFileName() != null
                                        && !subContractAmountInfoBean.getFileName().equals(EMPTY_STRING)){
                                    //Modified with Case 4007: Icon based on mime Type
                                    // #Case 3855 start added attachment specific icons for the button
//                                     String fileExtension = UserUtils.getFileExtension(subContractAmountInfoBean.getFileName());
//                                     btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                                    // Case 3855 end
                                    CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                                    btnDocument.setIcon(docTypeUtils.getAttachmentIcon(subContractAmountInfoBean));
                                    //4007 End
                                    return btnDocument;
                                }
                            //Code added for princeton enhancement case#2802 - ends
			}
			
			return returnComponent;
			
		} // End of  getTableCellRendererComponent
		
	}
    //Added for Case #2090 start 7
     /* Table cell editor for comments button 
     *@ author tarique
     */
    class HistoryOfChangesTableMoreEditor extends DefaultCellEditor implements ActionListener{
        
        private JButton btnComments, btnDocument;
        int row;
        private ImageIcon imgIcnDesc, pdfIcon;
        HistoryOfChangesTableMoreEditor() {
            super(new JComboBox());
            imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            btnComments = new JButton();
            btnComments.setIcon(imgIcnDesc);
            btnComments.setOpaque(true);
            btnComments.addActionListener(this);
            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
         //#Case 3855 - start commented to add document specific icon
            //  btnDocument = new JButton(pdfIcon);
            btnDocument = new JButton();
            btnDocument.addActionListener(this);             
          }
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
            //Code added for princeton enhancement case#2802 - starts
            this.row = row;
            SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvTableData.elementAt(row);
            switch(column){
                case PDF_DOCUMENT_COLUMN:
                    if(subContractAmountInfoBean.getFileName() != null
                            && !subContractAmountInfoBean.getFileName().equals(EMPTY_STRING)) {
                        //Modified with Case 4007: Icon based on mime Type
                        // #Case 3855 start added attachment specific icons for the button
//                         String fileExtension = UserUtils.getFileExtension(subContractAmountInfoBean.getFileName());
//                         btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                        // #Case 3855 end
                        CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                        btnDocument.setIcon(docTypeUtils.getAttachmentIcon(subContractAmountInfoBean));
                        //4007 End
                        btnDocument.setEnabled(true);
                    }else{
                        btnDocument.setIcon( null );
                        btnDocument.setEnabled(false);
                    }
                    return btnDocument;
            }
            //Code added for princeton enhancement case#2802 - ends
            return btnComments;
        }
        public void actionPerformed(ActionEvent actionEvent) {
            if( btnComments.equals(actionEvent.getSource())){
                this.stopCellEditing();
                showComments();
                
            } 
            //Code added for princeton enhancement case#2802 - starts
            else if(actionEvent.getSource().equals(btnDocument)){
                try{
                    viewPdfDocument();
                    stopCellEditing();
                }catch (Exception exception){
                    exception.printStackTrace();
                    if(!( exception.getMessage().equals(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130")) )){
                        CoeusOptionPane.showInfoDialog(exception.getMessage());
                    }
                }
            }
            ////Code added for princeton enhancement case#2802 - ends
        }
    }
     public void showComments(){
         int row = amountInfoForm.tblHistoryOfChange.getSelectedRow();
         if(row != -1){
             SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvTableData.elementAt(row);;
             amountInfoCommentsForm = new AmountInfoCommentsForm("Amount Info Comments");
             amountInfoCommentsForm.setData(subContractAmountInfoBean.getComments());
             amountInfoCommentsForm.display();
             amountInfoForm.tblHistoryOfChange.scrollRectToVisible(
             amountInfoForm.tblHistoryOfChange.getCellRect(
                                cvTableData.size()-1 ,1, true));
             amountInfoForm.tblHistoryOfChange.setRowSelectionInterval(row,row);
             amountInfoForm.tblHistoryOfChange.editCellAt(row,5);
             setRequestFocusInThread(historyOfChangesTableMoreEditor.btnComments);
         }
     }
     /* 
     ** Method to set focus in any component
     **/
     private void setRequestFocusInThread(final Component component){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            component.requestFocusInWindow();

        }
        });
    }
    //Added for Case #2090 end 7
     
    /** 
     * Code added for Princeton enhancements case#2802
     * Allows to view the PDF document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewPdfDocument() throws Exception{
        int selectedRow = amountInfoForm.tblHistoryOfChange.getSelectedRow();
        if( selectedRow != -1 ){
            SubContractAmountInfoBean subContractAmountInfoBean =
                    (SubContractAmountInfoBean)cvTableData.elementAt(selectedRow);
            CoeusVector cvDataObject = new CoeusVector();
            Map map = new HashMap();
            if(subContractAmountInfoBean.getAcType() != null 
                    && !subContractAmountInfoBean.getAcType().equals("")){
                HashMap hmDocumentDetails = new HashMap();
                hmDocumentDetails.put("subContractCode", subContractAmountInfoBean.getSubContractCode());
                hmDocumentDetails.put("sequenceNumber", ""+subContractAmountInfoBean.getSequenceNumber());
                hmDocumentDetails.put("lineNumber", ""+subContractAmountInfoBean.getLineNumber());
                hmDocumentDetails.put("fileName", subContractAmountInfoBean.getFileName());
                hmDocumentDetails.put("document", subContractAmountInfoBean.getDocument());
                cvDataObject.add(hmDocumentDetails);
                map.put("MODULE_NAME", "VIEW_DOCUMENT");
            } else {
                cvDataObject.add(subContractAmountInfoBean);
                map.put("MODULE_NAME", "AMOUNT_INFO");
            }
            RequesterBean requesterBean = new RequesterBean();
            DocumentBean documentBean = new DocumentBean();
            map.put("DATA", cvDataObject);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.subcontract.SubcontractDocumentReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            
            documentBean.setParameterMap(map);
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator appletServletCommunicator = new
                    AppletServletCommunicator(STREMING_SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            
            if(!responder.isSuccessfulResponse()){
                throw new CoeusException(responder.getMessage(),0);
            }
            map = (Map)responder.getDataObject();
            String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
            if(url == null || url.trim().length() == 0 ) {
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1009"));
                return;
            }
            url = url.replace('\\', '/') ;
            try{
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }catch( Exception ue) {
                ue.printStackTrace() ;
            }
        }
    }
    
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    private class SelectedAmountInfoTableHeaderRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setFont(table.getTableHeader().getFont());
            setText((value == null) ? "" : value.toString());
            setPreferredSize(new Dimension(50,35));
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
}
