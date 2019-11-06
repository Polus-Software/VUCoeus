/*
 * FundingSourceController.java
 *
 * Created on September 4, 2004, 3:00 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.subcontract.gui.SubcontractGenerateAgreementModification;
import java.awt.Cursor;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.DefaultListSelectionModel;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.subcontract.controller.SubcontractController;
import edu.mit.coeus.subcontract.gui.FundingSourceForm;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.award.controller.AwardBaseWindowController;
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.util.Hashtable;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class FundingSourceController extends SubcontractController implements ActionListener {
	
	private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
	
	private static final int AWARD_NUMBER_COLUMN = 0;
    private static final int ACCOUNT_NUMBER_COLUMN = 1;
	private static final int STATUS_COLUMN = 2;
	private static final int SPONSOR_COLUMN = 3;
    private static final int AMOUNT_COLUMN = 4;
	private static final int FINAL_EXPIRATION_DATE_COLUMN = 5;
	
	private QueryEngine queryEngine;
	
	private String EMPTY_STRING = "";
	
	private CoeusMessageResources  coeusMessageResources;
	
	private CoeusVector cvTableData;
	
	//For the deleted rows
	private CoeusVector cvDelData;
	
	private SubContractFundingSourceBean subContractFundingSourceBean;
	private SubContractBean subContractBean;
		
	private FundingSourceForm fundingSourceForm;
	private FundingSourceTableModel fundingSourceTableModel;
	private FundingSourceRenderer fundingSourceRenderer;
		
	private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
	
	private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
	private DateUtils dtUtils;
	private SimpleDateFormat dtFormat  = new SimpleDateFormat("MM/dd/yyyy");
	
	private static final String SELECT_A_ROW = "subcontractFundingSource_exceptionCode.1102";
	private static final String DELETE_CONFIRMATION = "subcontractFundingSource_exceptionCode.1103";
	private static final String AWARD_SEARCH = "AWARDSEARCH";
        //Code added for Case#3388 - Implementing authorization check at department level
        private static final char CAN_VIEW_AWARD =  'f' ;
        private boolean maintainReporting,isPrintingForm;
        private CoeusDlgWindow dlgGenAgrmtFundingSrcSelection;
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        private final String SELECT_FUNDING_SOURCE_FOR_GENERATE_AGREEMENT = "subcontractGenerate_exceptionCode.1001";
        private final String FUNDING_SOURCE_SELECTION_TITLE_FOR_GENERATE_AGREEMENT = "subcontractGenerateFundingSource_exceptionCode.1000";
        // Added for COEUSQA-1412 Subcontract Module changes - End
                
    /* JM 2-27-2015 modifications to allow tab access if user has only this right */
    private boolean userHasModify = false;
    private boolean userHasCreate = false;
    /* JM END */
                
        
	/**
	 * Constructor of FundingSourceController
	 * @param functionType char
	 * @param subContractBean SubContractBean
         * @param isPrintingForm boolean
	 */
//        public FundingSourceController(SubContractBean subContractBean,char functionType) {
	public FundingSourceController(SubContractBean subContractBean,char functionType, boolean isPrintingForm) {
		super(subContractBean);
                this.isPrintingForm = isPrintingForm;
		coeusMessageResources = CoeusMessageResources.getInstance();
		queryEngine = QueryEngine.getInstance();
		this.subContractBean = subContractBean;
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
	 * display method
	 */	
	public void display() {
	}
	
	/**
	 * Format the fields depends on the mode
	 */	
        public void formatFields() {
            if (getFunctionType() == DISPLAY_SUBCONTRACT) {
                fundingSourceForm.btnAdd.setEnabled(false);
                fundingSourceForm.btnDelete.setEnabled(false);
            }
            // Added for COEUSQA-1412 Subcontract Module changes - Start
            if(isPrintingForm){
                fundingSourceForm.btnAdd.setEnabled(true);
                fundingSourceForm.btnDelete.setEnabled(true);
                fundingSourceForm.btnAdd.setText("OK");
                fundingSourceForm.btnAdd.setMnemonic('O');
                fundingSourceForm.btnDelete.setText("Cancel");
                fundingSourceForm.btnDelete.setMnemonic('C');
                fundingSourceForm.btnMedusa.setVisible(false);
            }
            // Added for COEUSQA-1412 Subcontract Module changes - End
        }
	
	/**
	 * get the User Interface Form
	 * @return Component
	 */	
	public java.awt.Component getControlledUI() {
		return fundingSourceForm;
	}
	
	/**
	 * Getting the form data
	 * @return Object
	 */	
	public Object getFormData() {
		return cvTableData;
	}
	
	/**
	 * Registering the components
	 */	
	public void registerComponents() {
		fundingSourceForm = new FundingSourceForm();
		fundingSourceTableModel = new FundingSourceTableModel();
		fundingSourceRenderer = new FundingSourceRenderer();
		dtUtils = new DateUtils();
		fundingSourceForm.tblFundingSource.setModel(fundingSourceTableModel);
		fundingSourceForm.btnAdd.addActionListener(this);
		fundingSourceForm.btnDelete.addActionListener(this);
		fundingSourceForm.btnMedusa.addActionListener(this);
		fundingSourceForm.tblFundingSource.setBackground(disabledBackground);
                // Modified for COEUSQA-1412 Subcontract Module changes - Start
                // When controller is used for generate agreement / modification mouse listener is deactivated
                if(!isPrintingForm){
                    //Added for case id 3388 - start
                    fundingSourceForm.tblFundingSource.addMouseListener(new MouseAdapter(){
                        public void mouseClicked(MouseEvent e){
                            if(e.getClickCount()==2){
                                displayAward();
                            }
                        }
                    });
                    //Added for case id 3388 - end
                }
                // Modified for COEUSQA-1412 Subcontract Module changes - End
	}
        //Added for case id 3388 - start
        /**
         * Display the award window for the selected award in the table tblFundingSource
         */
	public void displayAward(){
            int selectedRow = fundingSourceForm.tblFundingSource.getSelectedRow();
            if(selectedRow!=-1){
                String awardNumber =fundingSourceForm.tblFundingSource.getValueAt(selectedRow, AWARD_NUMBER_COLUMN).toString();
                //Code added for Case#3388 - Implementing authorization check at department level - starts
                //Check the user is having rights to view this award
                if(!canViewAward(awardNumber)){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
                    return;
                }
                //Code added for Case#3388 - Implementing authorization check at department level - ends                
                if(isAwardWindowOpen(awardNumber, 'D', true)) {
                    return ;
                }
                
                AwardBean awardBean = new AwardBean();
                awardBean.setMitAwardNumber(awardNumber);
                CoeusGuiConstants.getMDIForm().setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Display Award : ", 'D' , awardBean);
                CoeusGuiConstants.getMDIForm().setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));        
                awardBaseWindowController.setMaintainReporting(maintainReporting);/////////???????????
                awardBaseWindowController.display(); 

            }
        }
        
        /** This method is used to check whether the given Award number is already
      * opened in the given mode or not.
      * @param refId refId - for award its Award Number.
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if Award window is already open else returns false.
      */
     boolean isAwardWindowOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.AWARD_BASE_WINDOW, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            // try to get the requested frame which is already opened 
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.AWARD_BASE_WINDOW,refId);
            /*if(frame == null){
                // if no frame opened for the requested record then the 
                //   requested mode is edit mode. So get the frame of the
                //   editing record. 
                frame = mdiForm.getEditingFrame( 
                    CoeusGuiConstants.AWARD_BASE_WINDOW );
            }*/
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
     }
     //Added for case id 3388 - end
	/**
	 * setting up the table column data
	 */	
	public void setColumnData() {
		JTableHeader tableHeader = fundingSourceForm.tblFundingSource.getTableHeader();
		tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
		
        fundingSourceForm.tblFundingSource.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
        fundingSourceForm.tblFundingSource.setRowHeight(22);
        
		fundingSourceForm.tblFundingSource.setSelectionMode(
			DefaultListSelectionModel.SINGLE_SELECTION);
		        
        TableColumn column = fundingSourceForm.tblFundingSource.getColumnModel().getColumn(AWARD_NUMBER_COLUMN);
       
        //column.setPreferredWidth(200);
		column.setPreferredWidth(120);
        column.setResizable(true);
		column.setCellRenderer(fundingSourceRenderer);
        
        column = fundingSourceForm.tblFundingSource.getColumnModel().getColumn(ACCOUNT_NUMBER_COLUMN);
        //column.setPreferredWidth(200);
		column.setPreferredWidth(140);
        column.setResizable(true);
		column.setCellRenderer(fundingSourceRenderer);
       
        column = fundingSourceForm.tblFundingSource.getColumnModel().getColumn(STATUS_COLUMN);
        //column.setPreferredWidth(150);
		column.setPreferredWidth(100);
        column.setResizable(true);
		column.setCellRenderer(fundingSourceRenderer);
        
        column = fundingSourceForm.tblFundingSource.getColumnModel().getColumn(SPONSOR_COLUMN);
        //column.setPreferredWidth(350);
		column.setPreferredWidth(230);
        column.setResizable(true);
		column.setCellRenderer(fundingSourceRenderer);
		
        column = fundingSourceForm.tblFundingSource.getColumnModel().getColumn(AMOUNT_COLUMN);
        //column.setPreferredWidth(170);
		column.setPreferredWidth(140);
        column.setResizable(true);
		column.setCellRenderer(fundingSourceRenderer);
        
		column = fundingSourceForm.tblFundingSource.getColumnModel().getColumn(FINAL_EXPIRATION_DATE_COLUMN);
        //column.setPreferredWidth(270);
		column.setPreferredWidth(170);
		column.setResizable(true);
		column.setCellRenderer(fundingSourceRenderer);
    }
        
    /**
	 * saving the form data
	 */
	public void saveFormData() {
		if (cvDelData != null && cvDelData.size() >= 0) {
			try {
				for (int index=0; index < cvDelData.size(); index++) {
					SubContractFundingSourceBean subContractFundingSourceBean =
					(SubContractFundingSourceBean)cvDelData.get(index);
					queryEngine.delete(queryKey,subContractFundingSourceBean);
				}
			} catch (CoeusException exception) {
				
			} 
		}
		for (int index=0; index < cvTableData.size(); index++) {
			SubContractFundingSourceBean subContractFundingSourceBean =
			(SubContractFundingSourceBean)cvTableData.get(index);
			if (subContractFundingSourceBean.getAcType()==null) {
				continue;
			}
			if (subContractFundingSourceBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
				queryEngine.insert(queryKey,subContractFundingSourceBean);
			}
		}
	}
	
	/**
	 * setting the form data
	 * @param data Object
	 */	
	public void setFormData(Object data)  {
		cvDelData = new CoeusVector();
		try {
			cvTableData = queryEngine.getDetails(queryKey,SubContractFundingSourceBean.class);
		} catch (CoeusException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * if one particular award is selected more than once it displays the duplicate message
	 * @return boolean
	 */
	
	private boolean isDuplicateRow(){
		
		Equals awardNumberEquals,accountNumberEquals,statusEquals,sponsorEquals,amountEquals,
		expDateEquals ;
		
		And awardAndAccount, awardAndAccountAndStatus,awardAndAccountAndStatusAndSponsor,
		awardAndAccountAndStatusAndSponsorAndAmount,awardAndAccountAndStatusAndSponsorAndAmountAndExpDate;
		
		if(cvTableData!=null && cvTableData.size() > 0) {
			for(int index = 0; index < cvTableData.size(); index++){
				SubContractFundingSourceBean subContractFundingSourceBean = ( SubContractFundingSourceBean )cvTableData.get(index);
				awardNumberEquals = new Equals("mitAwardNumber", subContractFundingSourceBean.getMitAwardNumber());
				accountNumberEquals = new Equals("accountNumber", subContractFundingSourceBean.getAccountNumber());
				statusEquals = new Equals("statusCode", new Integer(subContractFundingSourceBean.getStatusCode()));
				sponsorEquals = new Equals("sponsorCode", subContractFundingSourceBean.getSponsorCode());
				amountEquals = new Equals("obligatedAmount",new Double(subContractFundingSourceBean.getObligatedAmount()));
				expDateEquals = new Equals("finalExpirationDate", subContractFundingSourceBean.getFinalExpirationDate());
								
				awardAndAccount = new And(awardNumberEquals, accountNumberEquals);
				awardAndAccountAndStatus = new And(awardAndAccount, statusEquals);
				awardAndAccountAndStatusAndSponsor = new And(awardAndAccountAndStatus,sponsorEquals);
				awardAndAccountAndStatusAndSponsorAndAmount = new And(awardAndAccountAndStatusAndSponsor, amountEquals);
				awardAndAccountAndStatusAndSponsorAndAmountAndExpDate = new And(awardAndAccountAndStatusAndSponsorAndAmount, expDateEquals);
								
				CoeusVector coeusVector;
				coeusVector = cvTableData.filter(awardAndAccountAndStatusAndSponsorAndAmountAndExpDate);
				if (coeusVector.size()==-1) return false;
				if (coeusVector!=null && coeusVector.size() > 1) {
			
					CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("subcontractFundingSource_exceptionCode.1104"));
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Validate method
	 * @return boolean
	 * @throws CoeusUIException
	 */	
	public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
		//check for the duplicate row
		if (isDuplicateRow()) {
			return false;
		}
		return true;
	}
	/** This class will sort the column values in ascending and descending order
     * based on number of clicks. 
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","mitAwardNumber" },
            {"1","accountNumber" },
			{"2","statusDescription" },
			{"3","sponsorCode" },
			{"4","obligatedAmount" },
			{"5","finalExpirationDate" }
            
       };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                 // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
               // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvTableData != null && cvTableData.size() > 0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvTableData).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    fundingSourceTableModel.fireTableRowsUpdated(
                                            0, fundingSourceTableModel.getRowCount());
                }
           } catch(Exception exception) {
                exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
	
	/**
	 * This is an inner class represents the table model for the Funding Source
	 */
	public class FundingSourceTableModel extends AbstractTableModel {
		
		 // represents the column names of the table
//JM        private String colName[] = {"Award Number","Account Number","Status","Sponsor","Amount","Final Expiration Date"};
        private String colName[] = {"Award Number","Center Number","Status","Sponsor","Amount","Final Expiration Date"}; //JM 5-25-2011 changed to Center per 4.4.2
       
		// represents the column class of the fields of table        
        private Class colClass[] = {String.class, String.class,String.class,String.class,Double.class,String.class};   
		
		/**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
		public Class getColumnClass(int col) {
			return colClass[col];
		}
		
		/**
         * To check whether the table cell is editable or not
         * @param row int
		 * @param col int
         * @return boolean
         **/
		 public boolean isCellEditable(int row, int col){
            return false;
        }
		 /**
         * To get the column count of the table
         * @return int
         **/		 
		public int getColumnCount() {
			return colName.length;
		}		
		
		/**
         * To get the row count of the table
         * @return int
         **/
		public int getRowCount() {
			if (cvTableData == null){
                return 0;
            } else {
                return cvTableData.size();
            }
		}
		
		/**
         * To set the data for the model.
         * @param cvBudgetTableData CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvTableData) {
            cvTableData = cvTableData;
        }
		
		/**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
		public Object getValueAt(int rowIndex, int columnIndex) {
			SubContractFundingSourceBean subContractFundingSourceBean = 
				(SubContractFundingSourceBean)cvTableData.elementAt(rowIndex);
			if (subContractFundingSourceBean != null) {
				 switch(columnIndex) {
					case AWARD_NUMBER_COLUMN:
						return subContractFundingSourceBean.getMitAwardNumber();
					case ACCOUNT_NUMBER_COLUMN:
						return subContractFundingSourceBean.getAccountNumber();
					case STATUS_COLUMN:
						return subContractFundingSourceBean.getStatusDescription();	
					case SPONSOR_COLUMN:
					    return subContractFundingSourceBean.getSponsorCode() +" : "+subContractFundingSourceBean.getSponsorName();	
					case AMOUNT_COLUMN:
						 return new Double(subContractFundingSourceBean.getObligatedAmount());
					 case FINAL_EXPIRATION_DATE_COLUMN:
						 return subContractFundingSourceBean.getFinalExpirationDate();
				}
			}
            return EMPTY_STRING;
		}
		/**
         * To set the value in the table 
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            			
		}
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
		
	} // Table Model ends here
	/**
	 * An inner class represents the table renderer for the Funding Source table
	 */
	public class FundingSourceRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
		
		private JLabel lblText;
		
		private DollarCurrencyTextField txtCurrencyField;
		
		/**
		 * Constructor for the renderer
		 */		
		public FundingSourceRenderer() {
			
			lblText = new JLabel();
			lblText.setBorder(new EmptyBorder(0,0,0,0));
			lblText.setOpaque(true);
			txtCurrencyField=new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			txtCurrencyField.setHorizontalAlignment(txtCurrencyField.RIGHT);
						
		}
		
		/**
		 * This method will specify the renderer for each column.
		 * @param table JTable
		 * @param value Object
		 * @param isSelected boolean
		 * @param hasFocus boolean
		 * @param row int
		 * @param col int
		 * @return Component
		 */		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			Component returnComponent=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			switch(col) {	
				case AWARD_NUMBER_COLUMN :
					return returnComponent;
				case ACCOUNT_NUMBER_COLUMN :
					return returnComponent;
				case STATUS_COLUMN :
					return returnComponent;
				case SPONSOR_COLUMN  :
					return returnComponent;
				case AMOUNT_COLUMN :
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
										
				case FINAL_EXPIRATION_DATE_COLUMN :
					
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
				}
			
			return returnComponent;
			
		} // End of  getTableCellRendererComponent
		
	}
	
	 /**
	  * action performed method
	  * @param e ActionEvent
	  */	 
	public void actionPerformed(ActionEvent e) {
            // Modified for COEUSQA-1412 Subcontract Module changes - Start
//            if (e.getSource().equals(fundingSourceForm.btnAdd)) {
//                displayAwardSearch();
//            }
//            if (e.getSource().equals(fundingSourceForm.btnDelete)) {
//                performDeleteOperation();
//            }
//            if (e.getSource().equals(fundingSourceForm.btnMedusa)) {
//                performMedusa();
//            }
            if(!isPrintingForm){
                if (e.getSource().equals(fundingSourceForm.btnAdd)) {
                    displayAwardSearch();
                }
                if (e.getSource().equals(fundingSourceForm.btnDelete)) {
                    performDeleteOperation();
                }
                if (e.getSource().equals(fundingSourceForm.btnMedusa)) {
                    performMedusa();
                }
            }else{
                // When the controller is used for generate agreement / modification
                if (e.getSource().equals(fundingSourceForm.btnAdd)) {
                    int selectedRow = fundingSourceForm.tblFundingSource.getSelectedRow();
                    if(selectedRow > -1){
                        SubContractFundingSourceBean selectedFundingSource = (SubContractFundingSourceBean)cvTableData.get(selectedRow);
                        dlgGenAgrmtFundingSrcSelection.dispose();
                        SubcontractGenerateAgreementModification subcontractGenerateAgreementModification
                                = new SubcontractGenerateAgreementModification(subContractBean,selectedFundingSource);
                        if(subcontractGenerateAgreementModification.isAttachmentExists()){
                            subcontractGenerateAgreementModification.display();
                        }else{
                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(SubcontractBaseWindowController.NO_ATTACHMENT_FOR_GENERATE_AGREEMENT));
                        }
                    }else{
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(SELECT_FUNDING_SOURCE_FOR_GENERATE_AGREEMENT));
                    }
                }else if (e.getSource().equals(fundingSourceForm.btnDelete)) {
                    dlgGenAgrmtFundingSrcSelection.dispose();
                }
            }
            // Modified for COEUSQA-1412 Subcontract Module changes - End
	}
	
	 /** This method will used to get the valid award number from the search */     
    private void displayAwardSearch() {
        try {
            CoeusVector cvAwards = null;
			int rowId = 0;
			//get the max row id to set the row id to the new bean added.
			if (cvTableData != null && cvTableData.size() > 0) {
				cvTableData.sort("rowId",false);
				subContractFundingSourceBean = (SubContractFundingSourceBean)cvTableData.get(0);
				rowId = subContractFundingSourceBean.getRowId();
				cvTableData.sort("rowId");
			}
            SubContractFundingSourceSearch subContractFundingSourceSearch = new SubContractFundingSourceSearch (
            CoeusGuiConstants.getMDIForm(), AWARD_SEARCH, CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION);
            subContractFundingSourceSearch.showSearchWindow();
            if (subContractFundingSourceSearch.isOKClicked()) {
				//getting the selected awards
                cvAwards = subContractFundingSourceSearch.getSelectedAwards();
                if(cvAwards != null && cvAwards.size() >0 ) {
                    for(int index = 0; index < cvAwards.size(); index++) {
						rowId += 1;
                        subContractFundingSourceBean = (SubContractFundingSourceBean) cvAwards.get(index);
                        subContractFundingSourceBean.setRowId(rowId);
                        subContractFundingSourceBean.setSubContractCode(subContractBean.getSubContractCode());
						subContractFundingSourceBean.setSequenceNumber(subContractBean.getSequenceNumber());
						
                    }
                }
                cvTableData.addAll(cvAwards);
                fundingSourceTableModel.fireTableDataChanged();
            }
        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog("Award Search is not available.." + exception.getMessage());
        }
	}
	
	
	/**
	 * Deletion operation
	 */	
	private void performDeleteOperation() {
		int rowIndex = fundingSourceForm.tblFundingSource.getSelectedRow();
        if (rowIndex == -1) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW));
            return;
        }
        if (rowIndex >= 0) {
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
				coeusMessageResources.parseMessageKey(mesg),
					CoeusOptionPane.OPTION_YES_NO,
						CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
				
              	SubContractFundingSourceBean bean = (SubContractFundingSourceBean)cvTableData.get(rowIndex);
				if (bean != null) {
					//set the Ac Type as Delete and remove it from the table data
					bean.setAcType(TypeConstants.DELETE_RECORD);
					cvDelData.addElement(cvTableData.elementAt(rowIndex));
					cvTableData.removeElementAt(rowIndex);
					fundingSourceTableModel.fireTableRowsDeleted(rowIndex,rowIndex);
				}
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
            setFormData(subContractFundingSourceBean);
            setRefreshRequired(false);
        }
    }
	
	/**
	 * This method will perform the medusa operation
	 */	
	private void performMedusa() {
		try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            int selectedRow = fundingSourceForm.tblFundingSource.getSelectedRow();
            if( selectedRow >= 0 ){
                String awardNumber = (String)fundingSourceForm.tblFundingSource.getValueAt(selectedRow, AWARD_NUMBER_COLUMN);
                linkBean = new ProposalAwardHierarchyLinkBean();
                linkBean.setAwardNumber(awardNumber);
                linkBean.setBaseType(CoeusConstants.AWARD);
                if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
                CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                    if( medusaDetailform.isIcon() ){
                        medusaDetailform.setIcon(false);
                    }
                    medusaDetailform.setSelectedNodeId(awardNumber);
                    medusaDetailform.setSelected( true );
                    return;
                }
                medusaDetailform = new MedusaDetailForm(mdiForm, linkBean);
                medusaDetailform.setVisible(true);
            }else{
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("subcontractFundingSource_exceptionCode.1101"));
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
	}
	
	/**
	 * cleaning the objects
	 */	
	public void cleanUp() {
		coeusMessageResources = null;	
		cvTableData = null;
		cvDelData = null;
		subContractFundingSourceBean = null;
		subContractBean = null;
		fundingSourceForm = null;
		fundingSourceTableModel = null;
		fundingSourceRenderer = null;
		dtUtils = null;
		dtFormat  = null;
	}
        
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this award
     * @param awardNumber
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewAward(String awardNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_VIEW_AWARD);
        request.setDataObject(awardNumber);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalActionServlet";       
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
 
        String MAINTAIN_REPORTING = "MAINTAIN_REPORTING";
        Hashtable authorizations = new Hashtable();
        AuthorizationBean authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MAINTAIN_REPORTING);
        // 3587: Multi Campus enhancements - Start
//        authorizationBean.setFunctionType("OSP");
        authorizationBean.setFunctionType("RIGHT_ID");
        // 3587: Multi Campus enhancements - End
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizations.put(MAINTAIN_REPORTING, new AuthorizationOperator(authorizationBean));
        request = new RequesterBean();
        request.setAuthorizationOperators(authorizations);
        request.setIsAuthorizationRequired(true);
        
        comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + "/AuthorizationServlet", request);
        
        comm.send();
        response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            authorizations = response.getAuthorizationOperators();
        }else{
            CoeusOptionPane.showInfoDialog(response.getMessage());
        }
        maintainReporting = ((Boolean)authorizations.get(MAINTAIN_REPORTING)).booleanValue();
        
        return canView;
    }        

    // Added for COEUSQA-1412 Subcontract Module changes - Start
    /**
     * Method to display funding source selection window
     * @throws java.lang.Exception 
     */
    public void displayFundingSourceForGenerateAgreement() throws Exception{
        dlgGenAgrmtFundingSrcSelection = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgGenAgrmtFundingSrcSelection.getContentPane().add(fundingSourceForm);
        dlgGenAgrmtFundingSrcSelection.setTitle(coeusMessageResources.parseLabelKey(FUNDING_SOURCE_SELECTION_TITLE_FOR_GENERATE_AGREEMENT));
        dlgGenAgrmtFundingSrcSelection.setSize(1000, 300);
        dlgGenAgrmtFundingSrcSelection.setResizable(false);
        dlgGenAgrmtFundingSrcSelection.setLocation(CoeusDlgWindow.CENTER);
        dlgGenAgrmtFundingSrcSelection.setVisible(true);
    }
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
}
