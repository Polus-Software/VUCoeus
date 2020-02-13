/*
 * AmountReleasedController.java
 *
 * Created on September 23, 2004, 6:27 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.subcontract.controller.SubcontractController;
import edu.mit.coeus.subcontract.gui.AmountInfoCommentsForm;
import edu.mit.coeus.subcontract.gui.AmountReleasedForm;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.bean.SubContractAmountInfoBean;
import edu.mit.coeus.subcontract.bean.SubContractAmountReleased;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
//import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.DollarCurrencyTextField;
//import edu.mit.coeus.utils.CoeusTextField;
//import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.QueryEngine;
//import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.*;
import java.awt.Component;
//import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;
import javax.swing.DefaultListSelectionModel;
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  shijiv
 */
public class AmountReleasedController extends SubcontractController implements ActionListener {

	private AmountReleasedForm amountReleasedForm;
	private HistoryOfChangesTableModel historyOfChangesTableModel;
	private HistoryOfChangesTableRenderer historyOfChangesTableRenderer;
	private AmountTableModel amountTableModel;
	private AmountTableCellRenderer amountTableCellRenderer;
	private CoeusVector cvTableData;
	private CoeusMessageResources coeusMessageResources;
	//defines the column positions
    private static final int PDF_DOCUMENT_COLUMN = 0;
    private static final int INVOICE_NUMBER_COLUMN = 1;
    private static final int START_DATE_COLUMN = 2;
    private static final int END_DATE_COLUMN = 3;
    private static final int AMOUNT_RELEASED_COLUMN = 4;
     private static final int STATUS_COLUMN = 5;
	private static final int EFF_DATE_COLUMN = 6;
	private static final int COMMENTS_COLUMN = 7;        
	private static final int MORE_COMMENTS = 8;
    private static final int APPROVAL_COMMENTS_COLUMN = 9;
    private static final int APPROVAL_MORE_COMMENTS = 10;
    private HistoryOfChangesTableMoreEditor historyOfChangesTableMoreEditor;
    private AmountInfoCommentsForm amountInfoCommentsForm;
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private HashMap hmModifiedInvoice;
    private static final String SUBCONTRACT_SERVLET = "/SubcontractMaintenenceServlet";
    
	// table columns for the Total Table field
	private static final int TOTAL_COLUMN = 0;
	private static final int RELEASED_TOTAL_COLUMN = 1;
	private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
	private DateUtils dtUtils;
	private Color disabledBackground = (Color) javax.swing.UIManager.getDefaults().get("Panel.background");
	private QueryEngine queryEngine;
	private char functionType;
	
	private CoeusVector cvData;
	
	private AmountReleasedChangeController amountReleasedChangeController;
	
	private SubContractBean subContractBean;
	
	private static final String AMOUNT_INFO = "subcontractAmountRelease_exceptionCode.1204";
        
    private AmountReleasedCommentsController amountReleasedCommentsController;
    
    private SubcontractBaseWindowController subcontractBaseWindowController;
    private String amtReleasedLineItemNumber;

    /* JM 2-13-2015 modifications to allow tab access if user has only this right */
    private boolean userHasDocumentsModify = false;
    /* JM END */

    
    /** Creates a new instance of AmountReleasedController */
	public AmountReleasedController(SubContractBean subContractBean,char functionType) {
		super(subContractBean);
		
		/* JM 2-13-2015 no access if not document maintainer */
		userHasDocumentsModify = subContractBean.getHasModifyDocuments();
		if (!userHasDocumentsModify) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */
		
		coeusMessageResources = CoeusMessageResources.getInstance();
		queryEngine = QueryEngine.getInstance();
		this.subContractBean = subContractBean;
		registerComponents();
		formatFields();
		setFormData(subContractBean);
		setFunctionType(functionType);
		setColumnData();
		
	}
	
	/**
	 * Saving the data
	 */	
	public void saveFormData() {
                hmModifiedInvoice = new HashMap();
	}
	
	/**
	 * displaying the form
	 */	
	public void display() {
	}
	
	/**
	 * set the components enable or disable depends upon the modes
	 */	
	public void formatFields() {
		
		amountReleasedForm.txtAmountAvailable.setEnabled(false);
		amountReleasedForm.txtObligatedAmount.setEnabled(false);
		amountReleasedForm.txtAmountReleased.setEnabled(false);
        amountReleasedForm.btnModify.setEnabled(false);
        amountReleasedForm.btnSubmit.setEnabled(false);
        amountReleasedForm.btnApprove.setEnabled(false);
        amountReleasedForm.btnReject.setEnabled(false);
        amountReleasedForm.txtCreatedBy.setText(EMPTY_STRING);
        amountReleasedForm.txtLastUpdatedBy.setText(EMPTY_STRING);
        amountReleasedForm.txtApprovedBy.setText(EMPTY_STRING);
        amountReleasedForm.lblApprovedBy.setText(EMPTY_STRING);                

		if (getFunctionType() == DISPLAY_SUBCONTRACT) {
			amountReleasedForm.btnNew.setEnabled(false);
		}
	}
	
	/**
	 * validate method
	 * @return boolean
	 * @throws CoeusUIException
	 */	
	public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
		
		return true;
	}
	
	/**
	 * set the form data
	 * @param data Object
	 */	
	public void setFormData(Object data) {
		try {
			hmModifiedInvoice = new HashMap();
			cvTableData = queryEngine.getDetails(queryKey,SubContractAmountReleased.class);
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}
	}
	
	/**
	 * getting the form data
	 * @return Object
	 */	
	public Object getFormData() {
		return cvTableData;
	}
	
	/**
	 * getting the form UI
	 * @return Component
	 */	
	public java.awt.Component getControlledUI() {
		return amountReleasedForm;
	}
	
	/**
	 * registering the components
	 */	
	public void registerComponents() {
		amountReleasedForm = new AmountReleasedForm();
		historyOfChangesTableModel = new HistoryOfChangesTableModel();
		historyOfChangesTableRenderer = new HistoryOfChangesTableRenderer();
		amountTableModel = new AmountTableModel();
		amountTableCellRenderer = new AmountTableCellRenderer();
                //Code modified for Princeton enhancements case#2802 - starts
//		amountReleasedForm.btnChange.addActionListener(this);
                amountReleasedForm.btnNew.addActionListener(this);
                amountReleasedForm.btnModify.addActionListener(this);
                amountReleasedForm.btnSubmit.addActionListener(this);
                amountReleasedForm.btnApprove.addActionListener(this);
                amountReleasedForm.btnReject.addActionListener(this);               
//                amountReleasedForm.tblHistory.addMouseListener(new MouseAdapter() {
//                    public void mouseClicked(MouseEvent e){
//                        selectedRowStatus();
//                    }
//                });
                amountReleasedForm.tblHistory.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        selectedRowStatus();
                    }
                });              
                //Code modified for Princeton enhancements case#2802 - ends
		amountReleasedForm.tblHistory.setBackground(disabledBackground);
//		amountReleasedForm.tblAmounts.setBackground(disabledBackground);
		cvTableData = new CoeusVector();
		cvData = new CoeusVector();
		dtUtils = new DateUtils();
		amountReleasedForm.tblHistory.setModel(historyOfChangesTableModel);
                amountReleasedForm.txtCreatedBy.setText(EMPTY_STRING);
                amountReleasedForm.txtLastUpdatedBy.setText(EMPTY_STRING);
                amountReleasedForm.txtApprovedBy.setText(EMPTY_STRING);
                amountReleasedForm.lblApprovedBy.setText(EMPTY_STRING);
//		amountReleasedForm.tblAmounts.setModel(amountTableModel);
		amountReleasedChangeController = new AmountReleasedChangeController(subContractBean,getFunctionType());
		
	}
	
	private void setColumnData() {
		JTableHeader tableHeader = amountReleasedForm.tblHistory.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setFont(CoeusFontFactory.getLabelFont());
                tableHeader.setPreferredSize(new Dimension(0,22));
                
		amountReleasedForm.tblHistory.setRowHeight(22);
		
//		amountReleasedForm.tblAmounts.setRowHeight(22);
//		amountReleasedForm.tblAmounts.setFont(CoeusFontFactory.getLabelFont());
		amountReleasedForm.tblHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				
		amountReleasedForm.tblHistory.setSelectionMode(
		DefaultListSelectionModel.SINGLE_SELECTION);
        
                TableColumn column = amountReleasedForm.tblHistory.getColumnModel().getColumn(PDF_DOCUMENT_COLUMN);
                //Code modified for Princeton enhancements case#2802 - starts
                column.setMinWidth(20);
                column.setPreferredWidth(20);
                column.setCellRenderer(historyOfChangesTableRenderer);
                column.setCellEditor(new HistoryOfChangesTableMoreEditor());
                column.setHeaderValue(" ");
                
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(INVOICE_NUMBER_COLUMN);
		column.setPreferredWidth(110);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
		
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(START_DATE_COLUMN);
		column.setPreferredWidth(75);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
		
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(END_DATE_COLUMN);
		column.setPreferredWidth(75);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
                
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(AMOUNT_RELEASED_COLUMN);
		column.setPreferredWidth(95);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
                
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(STATUS_COLUMN);
		column.setPreferredWidth(100);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
                
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(EFF_DATE_COLUMN);
		column.setPreferredWidth(75);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer);
		
//		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(UPD_USER_COLUMN);
//		column.setPreferredWidth(100);
//		column.setResizable(true);
//		column.setCellRenderer(historyOfChangesTableRenderer);
//                
//		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(UPD_TIMESTAMP_COLUMN);
//		column.setPreferredWidth(120);
//		column.setResizable(true);
//		column.setCellRenderer(historyOfChangesTableRenderer);
                
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(COMMENTS_COLUMN);
		column.setPreferredWidth(145);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer); 
                
                column = amountReleasedForm.tblHistory.getColumnModel().getColumn(MORE_COMMENTS);
                column.setHeaderRenderer(new EmptyHeaderRenderer());
		column.setPreferredWidth(20);
		column.setResizable(false);
		column.setCellRenderer(historyOfChangesTableRenderer);                
                column.setCellEditor(new HistoryOfChangesTableMoreEditor());
                
		column = amountReleasedForm.tblHistory.getColumnModel().getColumn(APPROVAL_COMMENTS_COLUMN);
		column.setPreferredWidth(145);
		column.setResizable(true);
		column.setCellRenderer(historyOfChangesTableRenderer); 
                
                column = amountReleasedForm.tblHistory.getColumnModel().getColumn(APPROVAL_MORE_COMMENTS);
                column.setHeaderRenderer(new EmptyHeaderRenderer());
		column.setPreferredWidth(20);
		column.setResizable(false);
		column.setCellRenderer(historyOfChangesTableRenderer);                
                column.setCellEditor(new HistoryOfChangesTableMoreEditor());                
                
                //Code modified for Princeton enhancements case#2802 - ends
                //setting up the column values for the Total Table
                //Code commented as per new requirement.
//		JTableHeader totalTableHeader = amountReleasedForm.tblAmounts.getTableHeader();
//		totalTableHeader.setVisible(false);
//		
//		column = amountReleasedForm.tblAmounts.getColumnModel().getColumn(TOTAL_COLUMN);
//		//column.setMinWidth(60);
//		column.setPreferredWidth(170);
//		column.setResizable(true);
//		column.setCellRenderer(amountTableCellRenderer);
//		
//		column = amountReleasedForm.tblAmounts.getColumnModel().getColumn(RELEASED_TOTAL_COLUMN);
//		column.setPreferredWidth(100);
//		column.setResizable(true);
//		column.setCellRenderer(amountTableCellRenderer);
                if (cvTableData != null && cvTableData.size() > 0) {
                    amountReleasedForm.tblHistory.setRowSelectionAllowed(true);
                    amountReleasedForm.tblHistory.setRowSelectionInterval(0, 0);
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
            //Code added for princeton enhancement case#2802
            //To update the components and table with new values
            historyOfChangesTableModel.fireTableDataChanged();
            amountTableModel.fireTableDataChanged();
            double amount = cvTableData.sum("amountReleased");
            // 3566: Amount Available in Subcontract - Anomalous Result - Start
            // Round off amount to 2 decimal values
            amount = Utils.round(amount);
            // 3566: Amount Available in Subcontract - Anomalous Result - End
            amountReleasedForm.txtAmountReleased.setValue(amount);
            double amountAvailable = Double.parseDouble(amountReleasedForm.txtObligatedAmount.getValue()) - amount;
            amountReleasedForm.txtAmountAvailable.setValue(amountAvailable);
            //Code added for Princeton enhancements case#2802 - starts
            amountReleasedForm.btnModify.setEnabled(false);
            amountReleasedForm.btnSubmit.setEnabled(false);
            amountReleasedForm.btnApprove.setEnabled(false);
            amountReleasedForm.btnReject.setEnabled(false);
            if(cvTableData != null && cvTableData.size()>0){
                amountReleasedForm.tblHistory.setRowSelectionInterval(0, 0);
            }
            //Code added for Princeton enhancements case#2802 - ends             
            setRefreshRequired(false);
        }
    }
	
	/**
	 * This is an inner class specify the table model for the indirect cost
	 */
	class HistoryOfChangesTableModel extends AbstractTableModel {
		
		// HistoryOfChangesTableModel()
//		String colNames[] = {".", "<html>Invoice<br> Number</html>", "<html>Start<br> Date</html>", "<html>End<br> Date</html>", 
//                                        "<html>Amount<br> Released</html>", "Status" , "<html>Effective<br> Date</html>", 
//                                        "<html>Update<br> User</html>", "<html>Update<br> Timestamp</html>", 
//                                        "Comments", ".", "<html>Approval<br> Comments</html>", "."};
		String colNames[] = {".", "Invoice Number", "Start Date", "End Date", 
                                        "Amount", "Status" , "Eff Date", 
                                        "Comments", ".", "Approval Comments", "."};            
		Class[] colTypes = new Class [] {Boolean.class, String.class, DateUtils.class, 
                        DateUtils.class, Double.class, String.class, DateUtils.class, 
                        String.class, Boolean.class, String.class, Boolean.class};
		
		/**
		 * This method will check whether the given field is ediatble or not
		 * @param row int
		 * @param col int
		 * @return boolean
		 */
                public boolean isCellEditable(int row, int col){
                    //Code added for Princeton enhancements case#2802 - starts
                    if(col == MORE_COMMENTS
                            || col == APPROVAL_MORE_COMMENTS || col == PDF_DOCUMENT_COLUMN){
                        return true;
                    }
                    //Code added for Princeton enhancements case#2802 - ends
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
		 * settig the data to the vector
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
			SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)cvTableData.elementAt(rowIndex);
                        //Code modified for Princeton enhancements case#2802 - starts
                        switch(columnIndex) {
                            
                            case INVOICE_NUMBER_COLUMN:
                                return subContractAmountReleased.getInvoiceNumber();
                                
                            case START_DATE_COLUMN:
                                return subContractAmountReleased.getStartDate();
                                
                            case END_DATE_COLUMN:
                                return subContractAmountReleased.getEndDate();
                                
                            case AMOUNT_RELEASED_COLUMN:
                                return new Double(subContractAmountReleased.getAmountReleased());
                                
                            case STATUS_COLUMN:
                                String value = subContractAmountReleased.getStatusCode();
                                value = (value == null) ? EMPTY_STRING : value;
                                if(value.equals("P"))
                                    return "In Progress";
                                if(value.equals("A"))
                                    return "Approved";
                                if(value.equals("R"))
                                    return "Rejected";
                                if(value.equals("F"))
                                    return "Sent";
                                if(value.equals("U"))
                                    return "Rejection approved";
                                return value;
                                
                            case EFF_DATE_COLUMN:
                                return subContractAmountReleased.getEffectiveDate();
                                
//                            case UPD_USER_COLUMN:
//                                return subContractAmountReleased.getUpdateUserName();
//                                
//                            case UPD_TIMESTAMP_COLUMN:
//                                return subContractAmountReleased.getUpdateTimestamp();
                                
                            case COMMENTS_COLUMN:
                                return subContractAmountReleased.getComments();
                                
                            case APPROVAL_COMMENTS_COLUMN:
                                return subContractAmountReleased.getApprovalComments();
                                
                        }
                        //Code modified for Princeton enhancements case#2802 - ends
			return EMPTY_STRING;
			
		} // end of getValueAt()
	} // end of HistoryOfChangesTableModel
	
	class HistoryOfChangesTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
		private JLabel lblText;
		private DollarCurrencyTextField txtCurrencyField;
		private JButton btnMoreComments, btnDocument;
                private ImageIcon imgIcnDesc, imgIcnDocument;
		/**
		 * Default Constructor
		 */		
		public HistoryOfChangesTableRenderer(){
			lblText = new JLabel();
			lblText.setBorder(new EmptyBorder(0,0,0,0));
			lblText.setOpaque(true);
                        lblText.setFont(CoeusFontFactory.getNormalFont());
			txtCurrencyField=new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			txtCurrencyField.setHorizontalAlignment(txtCurrencyField.RIGHT);
                        txtCurrencyField.setFont(CoeusFontFactory.getNormalFont());
                        imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
                        btnMoreComments  = new JButton();
                        btnMoreComments.setIcon(imgIcnDesc);
                        //Code added for Princeton enhancements case#2802 - starts
                        // For PDF documentbutton to view the uploaded document
                        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//                        imgIcnDocument = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
                        imgIcnDocument = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
                        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
                        btnDocument = new JButton();
                      //#Case3855 -start commented to add document specific icon
                      //  btnDocument.setIcon(imgIcnDocument);
                        //Code added for Princeton enhancements case#2802 - ends
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			Component returnComponent = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
                        //Code added for Princeton enhancements case#2802 - starts
                        //To display the rejected invoice in red color.
                        SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)cvTableData.elementAt(row);
                        if(subContractAmountReleased.getStatusCode() != null && 
                                (subContractAmountReleased.getStatusCode().equals("R")
                                    || subContractAmountReleased.getStatusCode().equals("U"))){
                            setForeground(Color.RED);
                        } else {
                            setForeground(Color.BLACK);
                        }
                        //Code added for Princeton enhancements case#2802 - ends
                        switch(col){
                            case INVOICE_NUMBER_COLUMN:
                            case STATUS_COLUMN:
                            case COMMENTS_COLUMN:
                            case APPROVAL_COMMENTS_COLUMN:
                            //Code added for Princeton enhancements case#2802
                            //To display the updated userName and time
//                            case UPD_USER_COLUMN:
//                            case UPD_TIMESTAMP_COLUMN:                                
                                return returnComponent;
                                
                            case START_DATE_COLUMN:
                            case END_DATE_COLUMN:
                            case EFF_DATE_COLUMN:
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
                                
                            case AMOUNT_RELEASED_COLUMN:
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
                                
                            case MORE_COMMENTS:
                            case APPROVAL_MORE_COMMENTS:
                                return btnMoreComments;
                                
                            case PDF_DOCUMENT_COLUMN:
                                if(subContractAmountReleased.getFileName() != null 
                                        && !subContractAmountReleased.getFileName().equals(EMPTY_STRING)){
                                    //Modified with Case 4007: Icon based on mime Type:Start
                                    // #Case 3855 start added attachment specific icons for the button
//                                       String fileExtension = UserUtils.getFileExtension(subContractAmountReleased.getFileName());
//                                       btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                                    // #Case 3855 end
                                    CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                                    btnDocument.setIcon(docTypeUtils.getAttachmentIcon(subContractAmountReleased));
                                    //4007 End
                                    return btnDocument;
                                } 
                        }
			return returnComponent;
			
		} // End of  getTableCellRendererComponent
		
	}
	
	/**
	 * This class will specify the model for displaying
	 * Total Amount for both Obligated and Anticipated fields
	 */
	public class AmountTableModel extends AbstractTableModel {
		private String colName[] = {"Total: ", ""};
		private Class colClass[] = {String.class, Double.class};
		
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
			double totalAmount = 0.00;
			String name = "Total: ";
			switch (col) {
				case TOTAL_COLUMN:
					return name;
				case RELEASED_TOTAL_COLUMN:
					totalAmount = cvTableData.sum("amountReleased");
                                        // 3566: Amount Available in Subcontract - Anomalous Result - Start
                                        totalAmount = Utils.round(totalAmount);
                                        // 3566: Amount Available in Subcontract - Anomalous Result - End
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
                        //Code modified for Princeton enhancements case#2802 - starts
                        switch(col){
                            case TOTAL_COLUMN:
                                JLabel lblTotal = (JLabel)returnComponent;
                                lblTotal.setFont(CoeusFontFactory.getLabelFont());
                                lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                                return lblTotal;
                                
                            case RELEASED_TOTAL_COLUMN:
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
                        //Code modified for Princeton enhancements case#2802 - ends
			return lblText;
		}
		
	} // end of AmountTableCellRenderer
	
	/**
	 * clean up method.Set back all the objects to null
	 */	
	public void cleanUp() {
		amountReleasedForm = null;
		historyOfChangesTableModel = null;
		historyOfChangesTableRenderer = null;
		amountTableModel = null;
		amountTableCellRenderer = null;
		cvTableData = null;
		coeusMessageResources = null;
		cvData = null;
		subContractBean = null;
		amountReleasedChangeController.cleanUp();
        amountReleasedChangeController = null; 
	}
	
	/**
	 * action performed method
	 * @param actionEvent ActionEvent
	 */	
	public void actionPerformed(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();
                //Code modified for Princeton enhancements case#2802
            try{                
                if(source.equals(amountReleasedForm.btnNew)){
                    
                    CoeusVector cvTemp = queryEngine.getDetails(queryKey , SubContractAmountInfoBean.class);
                    if(cvTemp.size() == 0){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(AMOUNT_INFO));
                        BeanEvent beanEvent = new BeanEvent();
                        beanEvent.setSource(this);
                        beanEvent.setMessageId(2);
                        beanEvent.setBean(new SubContractAmountReleased());
                        fireBeanUpdated(beanEvent);
                        return;
                    }
                    if (validate()){
                        //Code modified for Princeton enhancements case#2802
                        setAmountReleasedData(null, 0);
                    }
                }
                //Code modified for Princeton enhancements case#2802 - starts
                //For Modify, Submit, Approve, Reject actions
                else {
                    int row = amountReleasedForm.tblHistory.getSelectedRow();
                    if(row != -1){
                            if(source.equals(amountReleasedForm.btnModify)){    
                                SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)cvTableData.elementAt(row);
                                setAmountReleasedData(subContractAmountReleased, row);
                                //Modified for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount Released tab - Start
                                //Only if the subcontract invoice is saved user can do the action
//                            } else if(!hmModifiedInvoice.containsKey(""+(row+1))){
                            }else {
                                boolean isSuccess = true;

                                boolean isSaveRequired = false;
                                 if(cvTableData != null && cvTableData.size() > row){
                                    SubContractAmountReleased subContractAmountReleased =
                                            (SubContractAmountReleased)cvTableData.get(row);
                                    int lineNumber = subContractAmountReleased.getLineNumber();
                                    if(subContractAmountReleased != null && 
                                            hmModifiedInvoice.containsKey(CoeusGuiConstants.EMPTY_STRING+lineNumber)){
                                        isSaveRequired = true;
                                    }
                                 }
                                if(!isSaveRequired){
                                //COEUSDEV-335 : end
                                    if(source.equals(amountReleasedForm.btnSubmit)){
                                        int option = CoeusOptionPane.showQuestionDialog(
                                                coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1212"),
                                                CoeusOptionPane.OPTION_YES_NO, JOptionPane.YES_OPTION);
                                        
                                        if(option == JOptionPane.YES_OPTION){
                                            if(cvTableData != null && cvTableData.size() > row){
                                                SubContractAmountReleased subContractAmountReleased =
                                                        (SubContractAmountReleased)cvTableData.get(row);
                                                /* JM 3-9-2015 flip to approve on submit
                                                submitSelectedInvoice(subContractAmountReleased, "F");*/
                                                submitSelectedInvoice(subContractAmountReleased, "A");
                                                /* JM END */
                                            }
                                        }else {
                                            //Code added for Princeton enhancements case#2802 -Start
                                            // if select option NO, disable the Approve & Reject button
                                            amountReleasedForm.btnApprove.setEnabled(false);
                                            amountReleasedForm.btnReject.setEnabled(false);
                                            isSuccess = false;
                                            //Code added for Princeton enhancements case#2802 - ends
                                        }
                                    } else if(source.equals(amountReleasedForm.btnApprove)){
                                        if(cvTableData != null && cvTableData.size() > row){
                                            SubContractAmountReleased subContractAmountReleased =
                                                    (SubContractAmountReleased)cvTableData.get(row);
                                            String title = "Approve Invoice No."+
                                                    ((subContractAmountReleased.getInvoiceNumber()==null)?"":
                                                        subContractAmountReleased.getInvoiceNumber());
                                            amountReleasedCommentsController = new
                                                    AmountReleasedCommentsController(title,"A");
                                            amountReleasedCommentsController.setCommentsMandatory(false);
                                            amountReleasedCommentsController.showChangeReleased(subContractAmountReleased);
                                            if(amountReleasedCommentsController.isOkClicked()){
                                                subContractAmountReleased.setApprovalComments((String)
                                                amountReleasedCommentsController.getFormData());
                                                submitSelectedInvoice(subContractAmountReleased, "A");
                                            }else{
                                                // Added for for Princeton enhancements case#2802 -Start
                                                // If Cancel is clicked ; set enable Modify & Approve buttons
                                                amountReleasedForm.btnModify.setEnabled(false);
                                                amountReleasedForm.btnSubmit.setEnabled(false);
                                                isSuccess = false;
                                                // Added for for Princeton enhancements case#2802 -End
                                            }
                                        }
                                    } else if(source.equals(amountReleasedForm.btnReject)){
                                        if(cvTableData != null && cvTableData.size() > row){
                                            SubContractAmountReleased subContractAmountReleased =
                                                    (SubContractAmountReleased)cvTableData.get(row);
                                            if(subContractAmountReleased.getAmountReleased() < 0){
                                                CoeusOptionPane.showInfoDialog(
                                                        coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1216"));
                                                return;
                                            }
                                            String title = "Reject Invoice No."+
                                                    ((subContractAmountReleased.getInvoiceNumber()==null)?"":
                                                        subContractAmountReleased.getInvoiceNumber());
                                            amountReleasedCommentsController = new
                                                    AmountReleasedCommentsController(title,"R");
                                            amountReleasedCommentsController.setCommentsMandatory(true);
                                            amountReleasedCommentsController.showChangeReleased(subContractAmountReleased);
                                            if(amountReleasedCommentsController.isOkClicked()){
                                                subContractAmountReleased.setApprovalComments((String)
                                                amountReleasedCommentsController.getFormData());
                                                submitSelectedInvoice(subContractAmountReleased, "R");
//                                            //insert into the query engine
//                                            queryEngine.insert(queryKey,subContractAmountReleased);
                                                //Fire other tabs with Modified event
                                                BeanEvent beanEvent = new BeanEvent();
                                                beanEvent.setSource(this);
                                                beanEvent.setBean(subContractAmountReleased);
                                                fireBeanUpdated(beanEvent);
                                            }else{
                                                // Added for for Princeton enhancements case#2802 -Start
                                                // If Cancel is clicked ; set enable Modify & Approve buttons
                                                amountReleasedForm.btnModify.setEnabled(false);
                                                amountReleasedForm.btnSubmit.setEnabled(false);
                                                isSuccess = false;
                                                // Added for for Princeton enhancements case#2802 -End
                                            }
                                        }
                                    }
                                    //Code added for Princeton enhancements case#2802 - starts
                                    if(isSuccess){
                                        amountReleasedForm.btnModify.setEnabled(false);
                                        amountReleasedForm.btnSubmit.setEnabled(false);
                                        amountReleasedForm.btnApprove.setEnabled(false);
                                        amountReleasedForm.btnReject.setEnabled(false);
                                        amountReleasedForm.txtCreatedBy.setText(EMPTY_STRING);
                                        amountReleasedForm.txtLastUpdatedBy.setText(EMPTY_STRING);
                                        amountReleasedForm.txtApprovedBy.setText(EMPTY_STRING);
                                        amountReleasedForm.lblApprovedBy.setText(EMPTY_STRING);
                                    }
                                    amountReleasedForm.tblHistory.setRowSelectionInterval(row, row);
                                    selectedRowStatus();
                                    //Code added for Princeton enhancements case#2802 - ends
                                }
                                //Added for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount Released tab - Start
                                //Message will be thrown when user tries to do action without saving the invoice
                                else {
                                    CoeusOptionPane.showInfoDialog(
                                            coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1210"));
                                }
                                //COEUSDEV-335 : End
                                
                            }
                            //Commented for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount Released tab - Start
//                            else {
//                                CoeusOptionPane.showInfoDialog(
//                                        coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1210"));
//                            }
                            //COEUSDEV-335 : End
                    } else {
                        CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1209"));
                    }
                    //Code modified for Princeton enhancements case#2802 - ends
                }
            } catch(CoeusUIException exception){
                    exception.printStackTrace();
            } catch(CoeusException exception){
                    exception.printStackTrace();
            }
	}
	
	/**
	 * setting the text field values
	 * @param cvData CoeusVector
	 */	
	public void setAmounts(CoeusVector cvData ){
		this.cvData = cvData;
		amountReleasedForm.txtAmountReleased.setText(cvData.get(2).toString());
		amountReleasedForm.txtAmountAvailable.setText(cvData.get(3).toString());
		amountReleasedForm.txtObligatedAmount.setText(cvData.get(0).toString());
		
	}
        
        /**
         * Code modified for Princeton enhancements case#2802
         * To save the invoice data to query engine.
         * @param subContAmountReleased
         * @param row
         */
        private void setAmountReleasedData(SubContractAmountReleased subContAmountReleased,
                int row) {
            String value = amountReleasedForm.txtAmountAvailable.getValue();
            //Code modified for Princeton enhancements case#2802 - starts
//            CoeusVector cvAmountReleased = amountReleasedChangeController.showChangeReleased(Double.parseDouble(value));
            boolean isUpdate = false;
            CoeusVector cvAmountReleased = null;
            if(subContAmountReleased == null){
                subContAmountReleased = new SubContractAmountReleased();
                int rowCount = historyOfChangesTableModel.getRowCount();
                subContAmountReleased.setLineNumber(rowCount + 1);
                subContAmountReleased.setSubContractCode(subContractBean.getSubContractCode());
                subContAmountReleased.setSequenceNumber(subContractBean.getSequenceNumber());
                cvAmountReleased = amountReleasedChangeController.showChangeReleased(
                        Double.parseDouble(value), subContAmountReleased);
                amountReleasedChangeController.setCvChanges(null);
            } else {
                cvAmountReleased = amountReleasedChangeController.showChangeReleased(
                        Double.parseDouble(value), subContAmountReleased);
                amountReleasedChangeController.setCvChanges(null);
                isUpdate = true;
            }
            //Code modified for Princeton enhancements case#2802 - ends
            if (cvAmountReleased != null && cvAmountReleased.size() > 0) {
                SubContractAmountReleased subContractAmountReleased =
                        (SubContractAmountReleased)cvAmountReleased.get(0);
                if (subContractAmountReleased != null) {
                    //Code modified for Princeton enhancements case#2802 - starts
                    //Invoice data to update code is added
                    if(isUpdate){
                        subContractAmountReleased.setAcType(TypeConstants.UPDATE_RECORD);
                        subContractAmountReleased.setLineNumber(
                                subContAmountReleased.getLineNumber());
                    } else {
                        subContractAmountReleased.setAcType(TypeConstants.INSERT_RECORD);
                        int rowCount = historyOfChangesTableModel.getRowCount();
                        subContractAmountReleased.setLineNumber(rowCount + 1);
                    }
                    hmModifiedInvoice.put(""+subContractAmountReleased.getLineNumber(),
                            ""+subContractAmountReleased.getLineNumber());
                    //Code modified for Princeton enhancements case#2802 - ends
                    subContractAmountReleased.setSubContractCode(subContractBean.getSubContractCode());
                    subContractAmountReleased.setSequenceNumber(subContractBean.getSequenceNumber());
                    //Code modified for Princeton enhancements case#2802
                    //Setting the status to in progress
                    subContractAmountReleased.setStatusCode("P");
                    if (cvTableData != null) {
                        //Code modified for Princeton enhancements case#2802 - starts
                        //Invoice data to update code is added
                        if(isUpdate){
                            cvTableData.remove(row);
                            cvTableData.add(row, cvAmountReleased.get(0));
                        } else {
                            cvTableData.addAll(0, cvAmountReleased);
                            //Modified for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount - Start
                            //Merged from 4.2.3_princeton_release
                            //code modified to displaying invoices in decending order
                            //row = cvTableData.size()-1;
                            row = 0;
                            //COEUSDEV-335 : End
                        }
                        //Code modified for Princeton enhancements case#2802 - ends
                        //Modified for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount - Start
                        //Merged from 4.2.3_princeton_release
                        //To displaying invoices in decending order
                        cvTableData.sort("lineNumber", false);
                        //COEUSDEV-335 : end
                        historyOfChangesTableModel.setData(cvTableData);
                        double amount = cvTableData.sum("amountReleased"); 
                        // 3566: Amount Available in Subcontract - Anomalous Result - Start
                        // Round off amount to 2 decimal values
                        amount = Utils.round(amount);
                        // 3566: Amount Available in Subcontract - Anomalous Result - End
                        amountReleasedForm.txtAmountReleased.setValue(amount);
                        double amountAvailable = Double.parseDouble(amountReleasedForm.txtObligatedAmount.getValue()) - amount;
                        amountReleasedForm.txtAmountAvailable.setValue(amountAvailable);
                        
                        historyOfChangesTableModel.fireTableDataChanged();
                        amountTableModel.fireTableDataChanged();
                    }
                    //insert into the query engine
                    queryEngine.insert(queryKey,subContractAmountReleased);
                    //Fire other tabs with Modified event
                    BeanEvent beanEvent = new BeanEvent();
                    beanEvent.setSource(this);
                    beanEvent.setBean(subContractAmountReleased);
                    fireBeanUpdated(beanEvent);
                    //Code added for Princeton enhancements case#2802 - starts
                    amountReleasedForm.btnModify.setEnabled(false);
                    amountReleasedForm.btnSubmit.setEnabled(false);
                    amountReleasedForm.btnApprove.setEnabled(false);
                    amountReleasedForm.btnReject.setEnabled(false);
                    amountReleasedForm.txtCreatedBy.setText(EMPTY_STRING);
                    amountReleasedForm.txtLastUpdatedBy.setText(EMPTY_STRING);
                    amountReleasedForm.txtApprovedBy.setText(EMPTY_STRING);
                    amountReleasedForm.lblApprovedBy.setText(EMPTY_STRING);
                    amountReleasedForm.tblHistory.setRowSelectionInterval(row, row);
                    //Code added for Princeton enhancements case#2802 - ends                    
                }
            }
            
        }
        
        /**
         * Code added for Princeton enhancements case#2802
         * This is the editor to show the comments, approved comments in a dialogue window
         * And to view the document.
         */
        class HistoryOfChangesTableMoreEditor extends DefaultCellEditor implements ActionListener{
            
            private JButton btnComments, btnDocument;
            private int column, row;
            private ImageIcon imgIcnDesc, pdfIcon;
          
            HistoryOfChangesTableMoreEditor() {
                super(new JComboBox());
                imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
                btnComments = new JButton();
                btnComments.setIcon(imgIcnDesc);
                btnComments.setOpaque(true);
                btnComments.addActionListener(this);
                //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//                pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
                pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
                //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
            // #Case 3855 start commented to add attachment specific icon
               //   btnDocument = new JButton(pdfIcon);                
                btnDocument = new JButton();
             //#Case 3855 -- end
                btnDocument.addActionListener(this);                
            }
            public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
                this.column = column;
                this.row = row;
                SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)cvTableData.elementAt(row);
                
                switch(column){
                    case PDF_DOCUMENT_COLUMN:
                        if(subContractAmountReleased.getFileName() != null
                                && !subContractAmountReleased.getFileName().equals(EMPTY_STRING)) {
                            //Modified with Case 4007: Icon based on mime Type : Start
                            // #Case 3855 start added attachment specific icons for the button
//                            String fileExtension = UserUtils.getFileExtension(subContractAmountReleased.getFileName());
//                            btnDocument.setIcon( UserUtils.getAttachmentIcon(fileExtension));
                            // #Case 3855 end
                            CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                            btnDocument.setIcon(docTypeUtils.getAttachmentIcon(subContractAmountReleased));
                            //4007 End
                            btnDocument.setEnabled(true);
                        }else{
                            btnDocument.setIcon( null );
                            btnDocument.setEnabled(false);
                        }
                        return btnDocument;
                }               
                return btnComments;
            }

            public void actionPerformed(ActionEvent actionEvent) {
                this.stopCellEditing();
                if( btnComments.equals(actionEvent.getSource())){
                    showComments(column);
                } else if(actionEvent.getSource().equals(btnDocument)){
                    //Modified for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount Released tab - Start
                    //Only if the subcontract invoice is saved user can view the document
//                    if(!hmModifiedInvoice.containsKey(""+(row+1))){
                    boolean isSaveRequired = false;
                    if(cvTableData != null && cvTableData.size() > row){
                        SubContractAmountReleased subContractAmountReleased =
                                (SubContractAmountReleased)cvTableData.get(row);
                        int lineNumber = subContractAmountReleased.getLineNumber();
                        if(subContractAmountReleased != null && 
                                hmModifiedInvoice.containsKey(CoeusGuiConstants.EMPTY_STRING+lineNumber)){
                            isSaveRequired = true;
                        }
                    }
                    
                    if(!isSaveRequired){
                    //COEUSDEV-335 : End
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
                    } else {
                        CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1210"));                        
                    }
                }
            }
        }
        
        /**
         * Code added for Princeton enhancements case#2802
         * To show the comments in the dialogue box.
         */        
        public void showComments(int column){
            int row = amountReleasedForm.tblHistory.getSelectedRow();
            if(row != -1){
                SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)cvTableData.elementAt(row);
                switch(column){
                    case MORE_COMMENTS:
                        amountInfoCommentsForm = new AmountInfoCommentsForm("Amount Released Comments");
                        amountInfoCommentsForm.setData(subContractAmountReleased.getComments());
                        break;
                    case APPROVAL_MORE_COMMENTS:
                        amountInfoCommentsForm = new AmountInfoCommentsForm("Approval Comments");
                        amountInfoCommentsForm.setData(subContractAmountReleased.getApprovalComments());
//                        amountInfoCommentsForm.txtDate.setText(
//                                subContractAmountReleased.getApprovalDate() == null ? EMPTY_STRING :
//                                    subContractAmountReleased.getApprovalDate().toString());
//                        amountInfoCommentsForm.txtUserId.setText(
//                                UserUtils.getDisplayName(subContractAmountReleased.getApprovedByUser()));
//                        amountInfoCommentsForm.setComponentStatus(true);
//                        if(subContractAmountReleased.getStatusCode() != null
//                                && subContractAmountReleased.getStatusCode().equals("A")){
//                            amountInfoCommentsForm.lblDate.setText("Approved Date:");
//                            amountInfoCommentsForm.lblUserId.setText("Approved User Name:");
//                            amountInfoCommentsForm.dlgWindow.setSize(new Dimension(410, 240));
//                        } else if(subContractAmountReleased.getStatusCode() != null
//                                && subContractAmountReleased.getStatusCode().equals("R")
//                                || (subContractAmountReleased.getApprovedByUser() != null
//                                    && !subContractAmountReleased.getApprovedByUser().equals(EMPTY_STRING))){
//                            amountInfoCommentsForm.lblDate.setText("Rejected Date:");
//                            amountInfoCommentsForm.lblUserId.setText("Rejected User Name:");
//                            amountInfoCommentsForm.dlgWindow.setSize(new Dimension(410, 240));
//                        } else {
//                            amountInfoCommentsForm.setComponentStatus(false);
//                        }
                        break;                        
                }
                if(getFunctionType() != DISPLAY_SUBCONTRACT){
                    setButtonStatus(subContractAmountReleased.getStatusCode());
                }
                amountInfoCommentsForm.display();
                amountReleasedForm.tblHistory.scrollRectToVisible(
                        amountReleasedForm.tblHistory.getCellRect(
                        cvTableData.size()-1 ,1, true));
                amountReleasedForm.tblHistory.editCellAt(row,5);
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
     
    /**
     * Code added for Princeton enhancements case#2802
     * To enable or disable the buttons, according to the row selected
     * @param e 
     */
     private void selectedRowStatus(){
         int row = amountReleasedForm.tblHistory.getSelectedRow();
         if(row == -1){
             return;
         }
         SubContractAmountReleased subContractAmountReleased = 
                 (SubContractAmountReleased)cvTableData.elementAt(row);         
         if(getFunctionType() != DISPLAY_SUBCONTRACT){
             String status = subContractAmountReleased.getStatusCode();
             status = ( status == null ) ? EMPTY_STRING : status;
             setButtonStatus(status);
         }
         if(subContractAmountReleased.getCreatedBy() != null &&
                 !subContractAmountReleased.getCreatedBy().equals(EMPTY_STRING)){
                 amountReleasedForm.txtCreatedBy.setText(subContractAmountReleased.getCreatedBy()+" at "
                         +CoeusDateFormat.format(subContractAmountReleased.getCreatedDate().toString()));
         } else {
             amountReleasedForm.txtCreatedBy.setText(EMPTY_STRING);
         }
         
         if(subContractAmountReleased.getUpdateUser() != null &&
                 !subContractAmountReleased.getUpdateUser().equals(EMPTY_STRING)){
                 amountReleasedForm.txtLastUpdatedBy.setText(subContractAmountReleased.getUpdateUserName()+" at "
                         +CoeusDateFormat.format(subContractAmountReleased.getUpdateTimestamp().toString()));
         } else {
             amountReleasedForm.txtLastUpdatedBy.setText(EMPTY_STRING);
         }
         
         if(subContractAmountReleased.getApprovedByUser() != null &&
                 !subContractAmountReleased.getApprovedByUser().equals(EMPTY_STRING)){
                 amountReleasedForm.txtApprovedBy.setText(subContractAmountReleased.getApprovedByUser()+" at "
                         +CoeusDateFormat.format(subContractAmountReleased.getApprovalDate().toString()));
                 if(subContractAmountReleased.getStatusCode().equals("A")){
                     amountReleasedForm.lblApprovedBy.setText("Approved By:");
                 } else if(subContractAmountReleased.getStatusCode().equals("R")
                    || subContractAmountReleased.getStatusCode().equals("U")){
                     amountReleasedForm.lblApprovedBy.setText("Rejected By:");
                 }
         } else {
             amountReleasedForm.txtApprovedBy.setText(EMPTY_STRING);
             amountReleasedForm.lblApprovedBy.setText(EMPTY_STRING);
         }
     }
     
    /**
     * Code added for Princeton enhancements case#2802
     * Enable or disable the buttons accoring to the invoice status
     * @param status 
     */
     private void setButtonStatus(String status){
         amountReleasedForm.btnModify.setEnabled(false);
         amountReleasedForm.btnSubmit.setEnabled(false);
         amountReleasedForm.btnApprove.setEnabled(false);
         amountReleasedForm.btnReject.setEnabled(false);
         if(status == null || status.equals("P")){
             amountReleasedForm.btnModify.setEnabled(true);
             amountReleasedForm.btnSubmit.setEnabled(true);
         } else if(status.equals("F")){
             amountReleasedForm.btnApprove.setEnabled(true);
             amountReleasedForm.btnReject.setEnabled(true);
         //Modified for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount - Start
         //Merged from 4.2.3_princeton_release
//         } else if(!status.equals("A") && !status.equals("R")){
         } else if(!status.equals("A") && !status.equals("R")
             && !status.equals("U")){
         //COEUSDEV-335 : End
             amountReleasedForm.btnModify.setEnabled(true);
             amountReleasedForm.btnSubmit.setEnabled(true);
             amountReleasedForm.btnApprove.setEnabled(true);
             amountReleasedForm.btnReject.setEnabled(true);
         }
     }

     
    /**
     * Code added for Princeton enhancements case#2802
     * To save the invoice data to query engine.
     * @param subContractAmountReleased 
     * @param statusFlag 
     */
     private void submitSelectedInvoice(SubContractAmountReleased subContractAmountReleased,
             String statusFlag)throws CoeusException, CoeusUIException {
         if (subContractAmountReleased != null) {

//             subContractAmountReleased.setAcType(TypeConstants.UPDATE_RECORD);
             subContractAmountReleased.setSubContractCode(subContractBean.getSubContractCode());
         // Commented set Sequence Number  becoz this code replace the already existing sequence with the latest sequence. Need to retain the original sequence.
         //    subContractAmountReleased.setSequenceNumber(subContractBean.getSequenceNumber());
             subContractAmountReleased.setStatusCode(statusFlag);
             //To get the Latest amount released. the calculation shoul not include the rejection amount.
             double amount = cvTableData.sum("amountReleased");   
             // 3566: Amount Available in Subcontract - Anomalous Result - Start
             amount = Utils.round(amount);
             // 3566: Amount Available in Subcontract - Anomalous Result - End
             amountReleasedForm.txtAmountReleased.setValue(amount);
             double amountAvailable = Double.parseDouble(amountReleasedForm.txtObligatedAmount.getValue()) - amount;
             amountReleasedForm.txtAmountAvailable.setValue(amountAvailable);
             hmModifiedInvoice.put(""+subContractAmountReleased.getLineNumber(),
                    ""+subContractAmountReleased.getLineNumber());
             historyOfChangesTableModel.fireTableDataChanged();
             amountTableModel.fireTableDataChanged();
             //insert into the query engine
             queryEngine.insert(queryKey,subContractAmountReleased);
             //Fire other tabs with Modified event
             BeanEvent beanEvent = new BeanEvent();
             beanEvent.setSource(this);
             beanEvent.setBean(subContractAmountReleased);
             fireBeanUpdated(beanEvent);
             setButtonStatus(EMPTY_STRING);
             if(subcontractBaseWindowController.validate() && subcontractBaseWindowController.isSaveRequired()) {
                 try {
                     subcontractBaseWindowController.saveSubcontract();
                 }catch(CoeusUIException coeusUIException) {
                     if(!coeusUIException.getMessage().equals("null.")) {
                         CoeusOptionPane.showDialog(coeusUIException);
                         subcontractBaseWindowController.tbdPnSubcontract.setSelectedIndex(coeusUIException.getTabIndex());
                     }
                 }
             }             
             //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
             if(statusFlag !=null && statusFlag.equalsIgnoreCase("A")){
                 setAmtReleasedLineItemNumber(String.valueOf(subContractAmountReleased.getLineNumber()));
             }
             //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
         }
     }     
     
    /** 
     * Code added for Princeton enhancements case#2802
     * Allows to view the PDF document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewPdfDocument() throws Exception{
        int selectedRow = amountReleasedForm.tblHistory.getSelectedRow();
        if( selectedRow != -1 ){
            SubContractAmountReleased subContractAmountReleased =
                    (SubContractAmountReleased)cvTableData.elementAt(selectedRow);
            CoeusVector cvDataObject = new CoeusVector();
            cvDataObject.add(subContractAmountReleased);
            RequesterBean requesterBean = new RequesterBean();
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", cvDataObject);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.subcontract.SubcontractDocumentReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("MODULE_NAME", "AMOUNT_RELEASED");
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

    public SubcontractBaseWindowController getSubcontractBaseWindowController() {
        return subcontractBaseWindowController;
    }

    public void setSubcontractBaseWindowController(SubcontractBaseWindowController subcontractBaseWindowController) {
        this.subcontractBaseWindowController = subcontractBaseWindowController;
    }
    //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
    public String getAmtReleasedLineItemNumber(){
        return this.amtReleasedLineItemNumber;
    }

    public void setAmtReleasedLineItemNumber(String amtReleasedLineItemNumber){
        this.amtReleasedLineItemNumber = amtReleasedLineItemNumber;
    }
    //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
}
