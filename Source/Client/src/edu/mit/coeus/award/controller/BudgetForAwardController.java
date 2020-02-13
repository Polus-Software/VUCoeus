/*
 * AwardForBudgetController.java
 *
 * Created on April 23, 2004, 6:02 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardBudgetBean;
import edu.mit.coeus.award.gui.AwardAmountTreeTable;
import edu.mit.coeus.award.gui.BudgetForAwardForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.controller.BudgetBaseWindowController;
import edu.mit.coeus.departmental.bean.DepartmentBudgetFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusClientException;

import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Cursor;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class BudgetForAwardController extends AwardController implements
                ActionListener,MouseListener {
        // For querying 
        private QueryEngine queryEngine;
        // To check whether the data or screen is modified
        private boolean modified = false;
        // Represents the empty string
        private static final String EMPTY_STRING = "";
        //Represents the dialog box
        private CoeusDlgWindow dlgBudgetForAward;
        //Represents the mdiForm
        private CoeusAppletMDIForm mdiForm;
        private String windowTitle = "";
		//Represents the cost element look up window
		private CostElementsLookupWindow costElementsLookupWindow;
		//For getting the max row count
		private int maxLineItem = 0;
		
		//For the table data
		private CoeusVector cvTableData;
		
		// For storing the deleted bean
		private CoeusVector cvDelData;
		// For the added cost element
		private CoeusVector cvAddCostElements;
		//For the disabled background colour
		private Color disabledBackground = (Color) UIManager.
						getDefaults().get("Panel.background");
		
		//To make the server call
		private static final String AWARD_SERVLET = "/AwardMaintenanceServlet"; 
		private static final String connectTo = CoeusGuiConstants.CONNECTION_URL + 
                                                                AWARD_SERVLET; 
		//private static final char GET_AWARD_BUDGET = 'P';
		
		private AwardBudgetBean awardBudgetBean;
		private boolean isValidCostElement = false;
		
		private boolean dirty = false;
		
		private AwardAmountInfoBean selectedBean;
		
		// For the default cost element
		private String defaultCostElement;
		
		private static final String SELECT_A_ROW="Please select a row to delete";
		private static final String DELETE_CONFIRMATION = "Are you sure you want to remove this row?";
		   
		//Title for the Cost Element Look up window
		private static final String COST_ELEMENT_LOOKUP_TITLE = "Cost Elements";
		
		//Title for the Budget For Award Dialog Window
		private static final String BUDGET_FOR_AWARD_TITLE = "Budget for Award: ";
		
        // Represents the names of the table column
        private static final int LINE_COLUMN = 0;
        private static final int CE_COLUMN = 1;
        private static final int COST_ELEMENT_DESC_COLUMN = 2;
        private static final int LINE_ITEM_DESC_COLUMN = 3;
        private static final int ANT_AMOUNT_COLUMN = 4;
        private static final int OBL_AMOUNT_COLUMN = 5;
		
		//setting up the width and height of the screen
		private static final int WIDTH = 760;
		private static final int HEIGHT = 450;
		
		private AwardAmountTreeTable awardAmountTreeTable;
        // Represents the form for the budget for award screen
        private BudgetForAwardForm budgetForAwardForm;
        // Represents the table model
        private BudgetForAwardTableModel budgetForAwardTableModel;
        // Represents the editor for the table
        private BudgetForAwardTableCellEditor budgetForAwardTableCellEditor;
        // Represents the renderer for the table
        private BudgetForAwardTableCellRenderer budgetForAwardTableCellRenderer;
        
        /*For Bug fix:1436 -  to add the summary budget line item as the default 
         line iten but not in the display mode - Step 1*/
        private char fnType;
        
       /** 
        * this contains the Coues message resources instance for parsing the messages
        **/
    CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();

        /**
		 * Creates a new instance of AwardForBudgetController
		 * @param selectedBean AwardAmountInfoBean
		 * @param functionType char
		 */
        public BudgetForAwardController (AwardAmountInfoBean selectedBean,char functionType) {
			super(selectedBean);
                        /*For Bug fix:1436 -  to add the summary budget line item as the default
                        line iten but not in the display mode - Step 2*/
                        this.fnType = functionType;
                        this.selectedBean = selectedBean;
                        coeusMessageResources = CoeusMessageResources.getInstance();
			queryEngine = QueryEngine.getInstance();
						
			registerComponents();
			formatFields();
			setFormData(selectedBean);
			setTableKeyTraversal();
			setFunctionType(functionType);
			setColumnData();
			postInitComponents();
                        
		}
         /**
		  * Registering the components
		  */
        public  void registerComponents() {
            budgetForAwardForm = new BudgetForAwardForm();
            budgetForAwardTableModel = new BudgetForAwardTableModel();
            budgetForAwardTableCellEditor = new BudgetForAwardTableCellEditor();
            budgetForAwardTableCellRenderer = new  BudgetForAwardTableCellRenderer();
			budgetForAwardForm.tblBudget.setModel(budgetForAwardTableModel);
            budgetForAwardForm.tblBudget.setCellEditor (budgetForAwardTableCellEditor);
			budgetForAwardForm.btnOk.addActionListener(this);
            budgetForAwardForm.btnCancel.addActionListener(this);
            budgetForAwardForm.btnAdd.addActionListener(this);
            budgetForAwardForm.btnDelete.addActionListener(this);
            budgetForAwardForm.btnCESearch.addActionListener(this);
        }
		
		/**
		 * Setting up the column data
		 */
		private void setColumnData(){
        JTableHeader tableHeader = budgetForAwardForm.tblBudget.getTableHeader();
		tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        budgetForAwardForm.tblBudget.setRowHeight(22);
        budgetForAwardForm.tblBudget.setSelectionBackground(Color.yellow);
        budgetForAwardForm.tblBudget.setSelectionForeground(Color.black);
		if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
			budgetForAwardForm.tblBudget.setShowHorizontalLines(false);
			budgetForAwardForm.tblBudget.setShowVerticalLines(false);
		}
        budgetForAwardForm.tblBudget.setOpaque(false);
        budgetForAwardForm.tblBudget.setSelectionMode(
			DefaultListSelectionModel.SINGLE_SELECTION);
		budgetForAwardForm.tblBudget.addMouseListener(this);
        
        TableColumn column = budgetForAwardForm.tblBudget.getColumnModel().getColumn(LINE_COLUMN);
       
        column.setPreferredWidth(50);
        column.setResizable(true);
        column.setCellRenderer(budgetForAwardTableCellRenderer);
        column.setCellEditor(budgetForAwardTableCellEditor);
        
        column = budgetForAwardForm.tblBudget.getColumnModel().getColumn(CE_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellRenderer(budgetForAwardTableCellRenderer);
        column.setCellEditor(budgetForAwardTableCellEditor);        
       
        column = budgetForAwardForm.tblBudget.getColumnModel().getColumn(COST_ELEMENT_DESC_COLUMN);
        column.setPreferredWidth(250);
        column.setResizable(true);
        column.setCellRenderer(budgetForAwardTableCellRenderer);
        column.setCellEditor(budgetForAwardTableCellEditor);
        
        column = budgetForAwardForm.tblBudget.getColumnModel().getColumn(LINE_ITEM_DESC_COLUMN);
        column.setPreferredWidth(250);
        column.setResizable(true);
        column.setCellRenderer(budgetForAwardTableCellRenderer); 
        column.setCellEditor(budgetForAwardTableCellEditor);
        
        column = budgetForAwardForm.tblBudget.getColumnModel().getColumn(ANT_AMOUNT_COLUMN);
        column.setPreferredWidth(170);
        column.setResizable(true);
        column.setCellRenderer(budgetForAwardTableCellRenderer);
        column.setCellEditor(budgetForAwardTableCellEditor);
        
        column = budgetForAwardForm.tblBudget.getColumnModel().getColumn(OBL_AMOUNT_COLUMN);
        column.setPreferredWidth(170);
        column.setResizable(true);
        column.setCellRenderer(budgetForAwardTableCellRenderer);
        column.setCellEditor(budgetForAwardTableCellEditor);
    }
    /**
	 * Specifies the Modal window
	 */    
    private void postInitComponents() {
		mdiForm = CoeusGuiConstants.getMDIForm();
        dlgBudgetForAward = new CoeusDlgWindow(mdiForm);
        dlgBudgetForAward.getContentPane().add(budgetForAwardForm);
        dlgBudgetForAward.setTitle(BUDGET_FOR_AWARD_TITLE);
        dlgBudgetForAward.setFont(CoeusFontFactory.getLabelFont());
        dlgBudgetForAward.setModal(true);
        dlgBudgetForAward.setResizable(false);
        dlgBudgetForAward.setSize(WIDTH,HEIGHT);
        dlgBudgetForAward.setTitle(windowTitle);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgBudgetForAward.getSize();
        dlgBudgetForAward.setLocation(screenSize.width/2 - (dlgSize.width/2),
									   screenSize.height/2 - (dlgSize.height/2));
        
        dlgBudgetForAward.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performCancelAction();
                return;
            }
        });
        dlgBudgetForAward.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgBudgetForAward.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                performCancelAction();
                return;
            }
        });
        
         dlgBudgetForAward.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 requestDefaultFocus();
             }
         });
    }
    
    private void requestDefaultFocus(){
		if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
			if(budgetForAwardForm.tblBudget.getRowCount() > 2){
				budgetForAwardForm.tblBudget.setRowSelectionInterval(1,1);
				budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
				budgetForAwardForm.tblBudget.editCellAt(1,1);
				budgetForAwardForm.tblBudget.getEditorComponent().requestFocusInWindow();
			}else{
				budgetForAwardForm.btnAdd.requestFocusInWindow();
			}
		}
    }
    
    /**
	 * Confirm before closing the BudgetPersons dialog box
	 */    
    private void confirmClosing(){
		dirty = false;
		isValidCostElement = false;
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                if(!isValidCostElement && !dirty) {// If the invalid cost element, make empty the field and stay back.
					if(validate()){
					  saveTableData(); 
					  closeDialog();
					}
				} else {
					return;
				}
			} else if(option == CoeusOptionPane.SELECTION_NO){
                            
                dlgBudgetForAward.setVisible(false);
            } else if(option == CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        } catch (Exception exception) {
            exception.getMessage();
        }
    }
    
    
    /** this method Closes this window
     */
    private void closeDialog() {
        
         dlgBudgetForAward.setVisible(false);
    }
    
        /**
		 * Display
		 */
        public void display () {
			
			//cvTableData will have atleast one record/element
			if(cvTableData == null ) {
				//Only default line item present. can't select that.
			} else if (cvTableData.size() <= 1) {
			
			} else {
				//select first editable element.
				//since cvTableData will have atleast one record and record count != 1
				//it now has atleast 2 records. so can select (2nd element)
				budgetForAwardForm.tblBudget.setRowSelectionInterval(1, 1);
				budgetForAwardForm.tblBudget.setColumnSelectionInterval(1, 1);
				
			}
			dlgBudgetForAward.setVisible(true);
		}        
        
        /**
		 * Format fields
		 */
        public void formatFields () {
			if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
				budgetForAwardForm.btnOk.setEnabled(false);
				budgetForAwardForm.btnAdd.setEnabled(false);
				budgetForAwardForm.btnDelete.setEnabled(false);
				budgetForAwardForm.btnCESearch.setEnabled(false);
				budgetForAwardForm.tblBudget.setShowHorizontalLines(true);
				budgetForAwardForm.tblBudget.setShowVerticalLines(true);
			}
			
		}
		
		public void mouseClicked(MouseEvent mouseEvent) {
			if (mouseEvent.getClickCount() != 2) return ;
			if (cvTableData != null && cvTableData.size() >=0) {
				int xPos = mouseEvent.getX();
				int column = budgetForAwardForm.tblBudget.getColumnModel().getColumnIndexAtX(xPos);
				if(column == COST_ELEMENT_DESC_COLUMN) {
					int selectedRow = budgetForAwardForm.tblBudget.getSelectedRow();
					AwardBudgetBean bean = (AwardBudgetBean)cvTableData.get(selectedRow);
					if(bean.getCostElementDescription() == null || bean.getCostElementDescription().equals(EMPTY)) {
						budgetForAwardForm.btnCESearch.doClick();
					}//end if costElementDesc == empty
				}//end if column == COST_ELEMENT_DESC_COLUMN
				
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
	/** This class will sort the column values in ascending and descending order
     * based on number of clicks. 
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","lineItemNumber" },
            {"1","costElement" },
            {"2","costElementDescription"},
			{"3","lineItemDescription"},
			{"4","anticipatedAmount"},
			{"5","obligatedAmount"}
       };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
				
				budgetForAwardTableCellEditor.stopCellEditing();
				if(dirty) {
					//some validation error occured. so don't sort.
					return ;
				}
				
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                 // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
               // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvTableData != null && cvTableData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvTableData).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    budgetForAwardTableModel.fireTableRowsUpdated(
                                            0, budgetForAwardTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
	
        /**
		 * To get the controlled UI
		 * @return java.awt.Component
		 */
        public Component getControlledUI() {
            return budgetForAwardForm;
        }
        /**
         * To get the form data
         * @return Object
         **/
        public Object getFormData () {
            return budgetForAwardForm;
        }
        /**
		 * save form data
		 */
        public void saveFormData () {
        }
        /**
		 * To set the form data
		 */
        
        public void setFormData (Object budgetData) {
                        
			cvDelData = new CoeusVector();	
			AwardAmountInfoBean awardInfoBean = null;
			if (budgetData != null) {
				// setting up the dialog window title
				awardInfoBean = (AwardAmountInfoBean)budgetData;
				budgetForAwardForm.txtAnticipatedField.setValue(awardInfoBean.
												getAnticipatedTotalAmount());
				budgetForAwardForm.txtObligatedField.setValue(awardInfoBean.
													getAmountObligatedToDate());
				windowTitle = BUDGET_FOR_AWARD_TITLE + awardInfoBean.
					getMitAwardNumber() + " "+ "Sequence " +awardInfoBean.getSequenceNumber();
            }
			
			// get the table data from the vector depends upon the term.
			String awardNumber = awardBaseBean.getMitAwardNumber();
			try{
				cvTableData = queryEngine.executeQuery (
				queryKey,AwardBudgetBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
				//setting up the anticipated total amount and obligated date explicitly - START
				Equals eqLI = new Equals("lineItemNumber", new Integer(1));
				CoeusVector cvLiOne = cvTableData.filter(eqLI);//Guaranteed to return 1 bean
				if (cvLiOne != null && cvLiOne.size() > 0) {
					AwardBudgetBean liOne = (AwardBudgetBean)cvLiOne.get(0);
				
					// if the vector size is one or the anticipated & obligated value is not equal to the 
					//available total value then update the vector with the new values.
					if (cvTableData.size() == 1) {
						liOne.setAnticipatedAmount(awardInfoBean.getAnticipatedTotalAmount());
						liOne.setObligatedAmount(awardInfoBean.getAmountObligatedToDate());
					} else {
						double sumAnt = calculateSum(ANT_AMOUNT_COLUMN);
						double sumObl = calculateSum(OBL_AMOUNT_COLUMN);
						double totalAntValue = awardInfoBean.getAnticipatedTotalAmount();
						double totalOblValue = awardInfoBean.getAmountObligatedToDate();
						liOne.setAnticipatedAmount(totalAntValue - sumAnt);
						liOne.setObligatedAmount(totalOblValue - sumObl);
					}
				}
				//setting up the anticipated total amount and obligated date explicitly - END
			} catch (CoeusException coeusException){
				coeusException.printStackTrace ();
			}
			// To get the whole data from server as well as the default Cost Element.
			CoeusVector cvServerData = getAwardBudget(awardNumber);
			if (cvServerData != null && cvServerData.size() > 0) {
				if (cvTableData == null || cvTableData.size() < 1) {
					cvTableData = (CoeusVector)cvServerData.get(0);
					//cvTableData is sure to return atleast 1 value.Adding the values to the query Engine.
				
					AwardBudgetBean awardBudgetBean;
					if (cvTableData != null) {
						for(int index = 0; index < cvTableData.size(); index++) {
							awardBudgetBean = (AwardBudgetBean)cvTableData.get(index);
							if (cvTableData.size() == 1) {
								awardBudgetBean.setAnticipatedAmount(awardInfoBean.getAnticipatedTotalAmount());
								awardBudgetBean.setObligatedAmount(awardInfoBean.getAmountObligatedToDate());
							}
							queryEngine.addData(queryKey, awardBudgetBean);
						}
					}
				}
				//getting the default cost element value
				defaultCostElement = (String)cvServerData.get(1);
			}
			//To get the max line item number
			maxLineItem = getMaxLineItemNumber();
			
			
                        /*For Bug fix:1436 -  to add the summary budget line item as the default 
                        line iten but not in the display mode - Step 3*/
                        if(cvTableData == null ){
                            if(fnType == TypeConstants.DISPLAY_MODE){
                                budgetForAwardTableModel.setData(cvTableData);
                                budgetForAwardTableModel.fireTableDataChanged();
                            }else{
                                AwardBudgetBean bean = new AwardBudgetBean();
                                CoeusVector cvData = new CoeusVector();
                                bean.setLineItemNumber(1);
                                bean.setCostElement("400000");
                                bean.setCostElementDescription("Summary Budget");
                                bean.setAnticipatedAmount(awardInfoBean.getAnticipatedTotalAmount());
                                bean.setObligatedAmount(awardInfoBean.getAmountObligatedToDate());
                                cvData.add(bean);
                                cvTableData = cvData;
                                budgetForAwardTableModel.setData(cvTableData);
                                budgetForAwardTableModel.fireTableDataChanged();
                            }
                        }else{
                            // setting up the data and update the table model.
                            budgetForAwardTableModel.setData(cvTableData);
                            budgetForAwardTableModel.fireTableDataChanged();
                        }
			if (cvTableData != null && cvTableData.size() < 2) {
				budgetForAwardForm.btnDelete.setEnabled(false);
				budgetForAwardForm.btnCESearch.setEnabled(false);
			}
                        try{
                            getCostElementData();
                        }catch (CoeusClientException ex){
                            CoeusOptionPane.showDialog(ex);
                            dlgBudgetForAward.dispose();
                        }
			// For the Cost Element Look up window values
//			String connectToCost = CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET;
//			RequesterBean request = new RequesterBean();
//                        cvAddCostElements = new CoeusVector();
//			request.setFunctionType('I');
//			AppletServletCommunicator comm = new AppletServletCommunicator(connectToCost, request);
//			comm.send();
//			ResponderBean response = comm.getResponse();
//                        if(response!=null){
//                            Vector vecCostElements = response.getDataObjects();
//                            cvAddCostElements.addAll(vecCostElements);
//                        }
//                        if(! response.isSuccessfulResponse()) {
//                                throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
//                                dlgBudgetForAward.dispose();
//                                //return ;
//                            }
		}
        
        private void getCostElementData() throws CoeusClientException{
            String connectToCost = CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET;
            RequesterBean request = new RequesterBean();
            cvAddCostElements = new CoeusVector();
            request.setFunctionType('I');
            AppletServletCommunicator comm = new AppletServletCommunicator(connectToCost, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response!=null){
                Vector vecCostElements = response.getDataObjects();
                cvAddCostElements.addAll(vecCostElements);
            }
            if(! response.isSuccessfulResponse()) {
                throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
                //return ;
            }
        }
	
		/**returns the max line item number from the contained vector.
		 */
		private int getMaxLineItemNumber(){
			int maxLineItem = 0;
			AwardBudgetBean awardBudgetBean;
			if (cvTableData != null && cvTableData.size() >0) {
				for (int index = 0; index < cvTableData.size(); index++) {
					awardBudgetBean = (AwardBudgetBean)cvTableData.get(index);
					if (awardBudgetBean.getLineItemNumber() > maxLineItem) {
						maxLineItem = awardBudgetBean.getLineItemNumber();
					}
				}
			}
			return maxLineItem;
		}//end getMaxLineItemNumber
		
		/**
		 *  This method will do a server call to get the default cost element
		 * @return String 
		 */
		private String getNewDefaultCostElement() {
			// For the Cost Element Look up window values
			
			RequesterBean request = new RequesterBean();
			request.setFunctionType('T');
			AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
			comm.send();
			ResponderBean response = comm.getResponse();
			if(! response.isSuccessfulResponse()) {
                                CoeusOptionPane.showErrorDialog(response.getMessage());
				return null;
			}
			 defaultCostElement = (String)response.getDataObject();
			 return defaultCostElement;
		}
		
        /**
		 * validate method
		 * @return boolean
		 * @throws edu.mit.coeus.exception.CoeusUIException
		 */
        public boolean validate() throws CoeusUIException {
			
			budgetForAwardTableCellEditor.stopCellEditing();
			AwardBudgetBean currentRowBean = null;
			
			if (cvTableData != null && cvTableData.size() > 0) {
				for (int index=0; index < cvTableData.size(); index++) {
					currentRowBean = (AwardBudgetBean)cvTableData.elementAt(index);
					if ((currentRowBean.getCostElement() == null) ||
					(EMPTY_STRING.equals(currentRowBean.getCostElement()))) {
						//Please enter a cost cost element in row <row number>
						CoeusOptionPane.showInfoDialog(coeusMessageResources.
						parseMessageKey("awardMoneyAndEndDates_exceptionCode.1151"));
						budgetForAwardForm.tblBudget.setRowSelectionInterval(index, index);
						budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
						budgetForAwardForm.tblBudget.editCellAt(index,1);
						setRequestFocusInThread(budgetForAwardTableCellEditor.txtCEField);
						
						
						return false;
					}
					
					if (currentRowBean.getLineItemNumber() == 1 && currentRowBean.getAnticipatedAmount() < 0.0) {
						//Anticipated amount cannot be less than zero
						CoeusOptionPane.showInfoDialog(coeusMessageResources.
						parseMessageKey("awardMoneyAndEndDates_exceptionCode.1152"));
						budgetForAwardForm.tblBudget.setRowSelectionInterval(index, index);
						
						
						return false;
					}
					if (currentRowBean.getLineItemNumber() == 1 && currentRowBean.getObligatedAmount() < 0.0) {
						//Obligated amount cannot be less than zero
						CoeusOptionPane.showInfoDialog(coeusMessageResources.
						parseMessageKey("awardMoneyAndEndDates_exceptionCode.1153"));
						budgetForAwardForm.tblBudget.setRowSelectionInterval(index, index);
						
						return false;
					}
					if (currentRowBean.getCostElement() != null) {
						// Check for the valid cost element.
						Equals eqCostElement = new Equals("costElement",currentRowBean.getCostElement());
						CoeusVector cvFilteredCost = cvAddCostElements.filter(eqCostElement);
						if (cvFilteredCost == null && cvFilteredCost.size() < 1) {
							CoeusOptionPane.showInfoDialog(coeusMessageResources.
							parseMessageKey("awardMoneyAndEndDates_exceptionCode.1154"));
							budgetForAwardForm.tblBudget.setRowSelectionInterval(index,index);
							budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
							budgetForAwardForm.tblBudget.editCellAt(index,1);
							setRequestFocusInThread(budgetForAwardTableCellEditor.txtCEField);
							return false;
						}
					}
				}
			}
		return true;
}
        
        
        
        /** Specifies the cancel action, the validations are as followed earlier */    
        private void performCancelAction(){
			budgetForAwardTableCellEditor.stopCellEditing();
			if(modified){
			  confirmClosing();
			} else {
				dlgBudgetForAward.setVisible(false);
			}
        }
        /**
		 * Action performed method
		 * @param actionEvent ActionEvent
		 */
        public void actionPerformed (ActionEvent actionEvent) {
			Object source = actionEvent.getSource();
			if (source == budgetForAwardForm.btnOk) {
				performOKOperation();
			}
			if (source == budgetForAwardForm.btnCancel) {
				performCancelAction ();
			}
			if (source == budgetForAwardForm.btnAdd) {
				performAddRow(actionEvent);
			}
			if (source == budgetForAwardForm.btnDelete) {
				performDeleteOperation();
			}
			if (source == budgetForAwardForm.btnCESearch) {
				performAddRow(actionEvent);
			}
        }
		/**
		 * This method will be called when OK button is clicked
		 * @ return void
		 */
		private void performOKOperation() {
			dirty = false;
			isValidCostElement = false;
			budgetForAwardTableCellEditor.stopCellEditing();
			try {
				if(!isValidCostElement && !dirty) {// If the invalid cost element, make empty the field and stay back.
					if (validate()) {
						saveTableData();
						dlgBudgetForAward.setVisible(false);
					}
				} else {
					return;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		/**
		 * To add a row if we click on Add Button
		 *   /** displays cost elment lookup dialog.
		 */
		private void performAddRow(ActionEvent actionEvent) {
			AwardBudgetBean newAwardBudgetBean = null;
			AwardBudgetBean prevAwardBudgetBean = null;
			//AwardBudgetBean bean = null;
			int selectedRowBean = 0;
			budgetForAwardTableCellEditor.stopCellEditing();
			//To get the max line item number
		
			maxLineItem = getMaxLineItemNumber();
			if (actionEvent.getSource() == budgetForAwardForm.btnAdd) {
				//For new Row
				newAwardBudgetBean = new AwardBudgetBean();
				newAwardBudgetBean.setAcType(TypeConstants.INSERT_RECORD);
				int rowCount = budgetForAwardTableModel.getRowCount();
				// To get the previous row bean
				prevAwardBudgetBean = (AwardBudgetBean)cvTableData.get(rowCount - 1);
				newAwardBudgetBean.setLineItemNumber(maxLineItem + 1);
				newAwardBudgetBean.setCostElement(EMPTY_STRING);
				newAwardBudgetBean.setCostElementDescription(EMPTY_STRING);
				newAwardBudgetBean.setMitAwardNumber(prevAwardBudgetBean.getMitAwardNumber());
				newAwardBudgetBean.setSequenceNumber(prevAwardBudgetBean.getSequenceNumber());
				cvTableData.add(newAwardBudgetBean);
				budgetForAwardTableModel.fireTableRowsInserted(
					budgetForAwardTableModel.getRowCount() - 1,
						budgetForAwardTableModel.getRowCount() - 1);
				maxLineItem = newAwardBudgetBean.getLineItemNumber();
				budgetForAwardForm.tblBudget.setRowSelectionInterval(
				budgetForAwardTableModel.getRowCount() - 1,
				budgetForAwardTableModel.getRowCount() - 1);
				budgetForAwardForm.tblBudget.setColumnSelectionInterval(3,3);
				budgetForAwardForm.tblBudget.editCellAt(budgetForAwardTableModel.getRowCount() - 1, LINE_ITEM_DESC_COLUMN);
				setRequestFocusInThread(budgetForAwardTableCellEditor.txtLineItemDesc);
				//budgetForAwardForm.tblBudget.getEditorComponent().requestFocusInWindow();
				budgetForAwardForm.tblBudget.scrollRectToVisible(
				budgetForAwardForm.tblBudget.getCellRect(
				budgetForAwardTableModel.getRowCount() - 1 ,0, true));
				modified = true;
				budgetForAwardForm.btnDelete.setEnabled(true);
				budgetForAwardForm.btnCESearch.setEnabled(true);
				
			} else if (actionEvent.getSource() == budgetForAwardForm.btnCESearch) { 
				//take selected bean
				selectedRowBean = budgetForAwardForm.tblBudget.getSelectedRow();
				if (selectedRowBean == -1) 
					return;
				budgetForAwardForm.tblBudget.setRowSelectionInterval(selectedRowBean,
								selectedRowBean);
			
				//budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
				budgetForAwardForm.tblBudget.editCellAt(selectedRowBean,LINE_ITEM_DESC_COLUMN);
				setRequestFocusInThread(budgetForAwardTableCellEditor.txtLineItemDesc);
				//budgetForAwardForm.tblBudget.getEditorComponent().requestFocusInWindow();
				budgetForAwardForm.tblBudget.scrollRectToVisible(
				budgetForAwardForm.tblBudget.getCellRect(selectedRowBean,0, true));
//                                budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
				
				// getting the selected row bean for updation
				newAwardBudgetBean = (AwardBudgetBean)cvTableData.get(selectedRowBean);
			}
			try {
			dlgBudgetForAward.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			DepartmentBudgetFormBean departmentBudgetFormBean;
			ComboBoxBean comboBoxBean;
			CoeusVector vecComboBoxBean = new CoeusVector();

			for(int index = 0; index < cvAddCostElements.size(); index++) {
				departmentBudgetFormBean = (DepartmentBudgetFormBean)cvAddCostElements.get(index);
				comboBoxBean = new ComboBoxBean(departmentBudgetFormBean.getCostElement(), departmentBudgetFormBean.getDescription());
				vecComboBoxBean.add(comboBoxBean);
			}
			String colNames[] = {"Code","Description"};
			OtherLookupBean otherLookupBean = new OtherLookupBean(COST_ELEMENT_LOOKUP_TITLE, vecComboBoxBean, colNames);
			costElementsLookupWindow = new CostElementsLookupWindow(otherLookupBean);
			
			//Check button click - OK or Cancel
			if(otherLookupBean.getSelectedInd() == -1) return ;

			//Get Selected Row for Cost Elements
			int selectedRow = costElementsLookupWindow.getDisplayTable().getSelectedRow();
			if(selectedRow == -1) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.
							parseMessageKey("awardMoneyAndEndDates_exceptionCode.1151"));
				return ;
			}
				
			departmentBudgetFormBean = (DepartmentBudgetFormBean)cvAddCostElements.get(selectedRow);
			String modifiedDefaultCE = getNewDefaultCostElement();
			if (modifiedDefaultCE != null) {
				defaultCostElement = modifiedDefaultCE;
			}
			
			if (newAwardBudgetBean != null) {
				if (!defaultCostElement.equals(departmentBudgetFormBean.getCostElement())) {
					newAwardBudgetBean.setCostElement(departmentBudgetFormBean.getCostElement());
					newAwardBudgetBean.setCostElementDescription(departmentBudgetFormBean.getDescription());
					newAwardBudgetBean.setMitAwardNumber(selectedBean.getMitAwardNumber());
					newAwardBudgetBean.setSequenceNumber(selectedBean.getSequenceNumber());
				} else {
					CoeusOptionPane.showInfoDialog(coeusMessageResources.
							parseMessageKey("awardMoneyAndEndDates_exceptionCode.1156"));
					return ;
				}
				
			}
						
			//setting AC Types
			//set acType as Insert if it was inserted initially.
			//set actype as Update for beans got from server(i.e acType = null/Update)
			if(newAwardBudgetBean.getAcType() == null) {
				newAwardBudgetBean.setAcType(TypeConstants.UPDATE_RECORD);
			}
			
			modified = true;
			budgetForAwardTableModel.fireTableRowsUpdated(selectedRowBean,selectedRowBean);
			if (actionEvent.getSource() == budgetForAwardForm.btnAdd) {
				//budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
				
				//budgetForAwardForm.tblBudget.getEditorComponent().requestFocusInWindow();
			 
				budgetForAwardTableCellEditor.stopCellEditing();

				budgetForAwardForm.tblBudget.setRowSelectionInterval(
				budgetForAwardTableModel.getRowCount() - 1,
				budgetForAwardTableModel.getRowCount() - 1);
				budgetForAwardForm.tblBudget.setColumnSelectionInterval(3,3);
				budgetForAwardForm.tblBudget.editCellAt(budgetForAwardTableModel.getRowCount() - 1, LINE_ITEM_DESC_COLUMN);
				setRequestFocusInThread(budgetForAwardTableCellEditor.txtLineItemDesc);
				//budgetForAwardForm.tblBudget.getEditorComponent().requestFocusInWindow();
				budgetForAwardForm.tblBudget.scrollRectToVisible(
				budgetForAwardForm.tblBudget.getCellRect(
				budgetForAwardTableModel.getRowCount() - 1 ,0, true));
				//budgetForAwardTableCellEditor.requestFocus();
				
			} 
			budgetForAwardForm.btnDelete.setEnabled(true);
			budgetForAwardForm.btnCESearch.setEnabled(true);
			} finally{
				dlgBudgetForAward.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ));
			}
		}
		
		/**
		 * Validating the screen fields and saving the details
		 * to the query engine
		 */
	private void saveTableData() {
		try {
			for (int index=0; index < cvTableData.size(); index++) {
				AwardBudgetBean awardBudgetBean = 
						(AwardBudgetBean)cvTableData.get(index);
				if (awardBudgetBean.getAcType()==null) {
					continue; 
				}
				if (awardBudgetBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
					queryEngine.insert(queryKey,awardBudgetBean);
				}
				if (awardBudgetBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {

					queryEngine.update(queryKey,awardBudgetBean);
				} 
			}
		} catch (CoeusException exception) {
				exception.printStackTrace();
		}
		if (cvDelData != null && cvDelData.size() >= 0) {
			try {
				for (int index=0; index < cvDelData.size(); index++) {
					AwardBudgetBean awardBudgetBean = 
						(AwardBudgetBean)cvDelData.get(index);
					queryEngine.delete(queryKey,awardBudgetBean);
				}
			} catch (CoeusException exception) {
					exception.printStackTrace();
				}
			}
		}
		
		
	/**
	 * This method will specify the actions during Delete operation
	 */
	private void performDeleteOperation() {
		budgetForAwardTableCellEditor.stopCellEditing();
		int rowIndex;
		rowIndex = budgetForAwardForm.tblBudget.getSelectedRow();
        if (rowIndex == -1) {
            CoeusOptionPane.showErrorDialog(SELECT_A_ROW);
            return;
        }
		
		if (rowIndex >= 0) {
			AwardBudgetBean awardBudgetBean = (AwardBudgetBean)cvTableData.get(rowIndex);
			if (awardBudgetBean.getLineItemNumber() != 1) {
				String mesg = DELETE_CONFIRMATION;
				int selectedOption = CoeusOptionPane.showQuestionDialog(
					coeusMessageResources.parseMessageKey(mesg),
						CoeusOptionPane.OPTION_YES_NO,
							CoeusOptionPane.DEFAULT_YES);
				if(selectedOption == CoeusOptionPane.SELECTION_YES) {
					AwardAmountInfoBean infoBean = (AwardAmountInfoBean)selectedBean;

					if(cvTableData!=null && cvTableData.size() > 0) {
						if(awardBudgetBean.getCostElement()!= null && 
								!EMPTY_STRING.equals(awardBudgetBean.getCostElement())) {
							cvDelData.addElement(cvTableData.elementAt(rowIndex));
						}
						cvTableData.removeElementAt(rowIndex);
						//budgetForAwardTableModel.setData(cvTableData);

						// To set the Anticipated & Obligated values after deleting the row
						double sumAntCol = calculateSum(ANT_AMOUNT_COLUMN);
						double sumOblCol = calculateSum(OBL_AMOUNT_COLUMN);
						if (infoBean == null) return;
						double totalAntValue = infoBean.getAnticipatedTotalAmount();
						double totalOblValue = infoBean.getAmountObligatedToDate();

						//filtering done to get the row which has line item number 1
						Equals eqLineNumber = new Equals("lineItemNumber",new Integer(1));
						CoeusVector cvFiltered = cvTableData.filter(eqLineNumber);
						if (cvFiltered != null && cvFiltered.size() >= 0) {
							AwardBudgetBean bean = (AwardBudgetBean)cvFiltered.get(0);
							bean.setAnticipatedAmount(totalAntValue - sumAntCol);
							bean.setObligatedAmount(totalOblValue - sumOblCol);
							bean.setAcType(TypeConstants.UPDATE_RECORD);
						}
						budgetForAwardTableModel.fireTableRowsDeleted(rowIndex,rowIndex);
						//budgetForAwardTableModel.fireTableRowsUpdated(rowIndex,rowIndex);
						//budgetForAwardTableModel.fireTableDataChanged();
												
						if(rowIndex < 1 && budgetForAwardForm.tblBudget.getRowCount() > 0) {
//							budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex, rowIndex);
//							budgetForAwardForm.tblBudget.setColumnSelectionInterval(LINE_ITEM_DESC_COLUMN,LINE_ITEM_DESC_COLUMN);
//							budgetForAwardForm.tblBudget.editCellAt(rowIndex, LINE_ITEM_DESC_COLUMN);
//							setRequestFocusInThread(budgetForAwardTableCellEditor.txtLineItemDesc);
						}else{
							budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex - 1, rowIndex - 1);
							budgetForAwardForm.tblBudget.setColumnSelectionInterval(LINE_ITEM_DESC_COLUMN,LINE_ITEM_DESC_COLUMN);
							budgetForAwardForm.tblBudget.editCellAt(rowIndex - 1, LINE_ITEM_DESC_COLUMN);
							setRequestFocusInThread(budgetForAwardTableCellEditor.txtLineItemDesc);
						}
						modified = true;
						awardBudgetBean.setAcType(TypeConstants.DELETE_RECORD);
						// To get the max line item number

						//if deleted bean had the max row id then set back the max rowid as present maxRowID
						if(awardBudgetBean.getLineItemNumber() == maxLineItem) {
							maxLineItem = getMaxLineItemNumber();
						}
					}
					if (cvTableData != null && cvTableData.size() < 2) {
						budgetForAwardForm.btnDelete.setEnabled(false);
						budgetForAwardForm.btnCESearch.setEnabled(false);
					}
				}
			}
		}
	}
	/**
	 * To calculate the field values
	 * @param col int
	 * @return double
	 */
	private double calculateSum(int col) {
		double sum = 0.0;
		int index = 0;
		if (cvTableData != null && cvTableData.size() > 1) {
			for (index = 0; index < cvTableData.size(); index++) {
				AwardBudgetBean bean = (AwardBudgetBean)cvTableData.get(index);
				if (bean.getLineItemNumber() == 1) {
					continue;
				} 
				if (col == ANT_AMOUNT_COLUMN) {
					sum = sum + bean.getAnticipatedAmount();
				}
				if (col == OBL_AMOUNT_COLUMN) {
					sum = sum + bean.getObligatedAmount();
				}
			}
		}
		return sum;
	}
        
  /**
   * This is an inner class represents the table model for the Award For Budget
   * screen table
   **/
   public class BudgetForAwardTableModel extends AbstractTableModel {
           
        // represents the column names of the table
        private String colName[] = {"Line","CE","Cost Element Description",
                "Line Item Description","Ant Amt","Obl Amt"};
        // represents the column class of the fields of table        
        private Class colClass[] = {Integer.class, String.class, String.class, 
                        String.class,Double.class,Double.class};   
		
		private CoeusVector cvTableData;
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
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
		 */
        public void setData(CoeusVector cvTableData) {
            this.cvTableData = cvTableData;
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
			AwardBudgetBean awardBudgetBean = (AwardBudgetBean)cvTableData.elementAt(rowIndex);
			
			if (awardBudgetBean != null) {
				switch(columnIndex) {
					
					case LINE_COLUMN:
						return new Integer(awardBudgetBean.getLineItemNumber());
					case CE_COLUMN:
						return awardBudgetBean.getCostElement();
					case COST_ELEMENT_DESC_COLUMN:
						return awardBudgetBean.getCostElementDescription();
					case LINE_ITEM_DESC_COLUMN:
						if (awardBudgetBean.getLineItemDescription() == null) {
							return EMPTY_STRING;
						} else
						return awardBudgetBean.getLineItemDescription();
					case ANT_AMOUNT_COLUMN:
						return new Double(awardBudgetBean.getAnticipatedAmount());
					case OBL_AMOUNT_COLUMN:
						return new Double(awardBudgetBean.getObligatedAmount());
				}
			}
			return EMPTY_STRING;
        }
        
        /**
		 * To set the value in the table
		 * @param value Object
		 * @param row int
		 * @param col int
		 */
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
			AwardAmountInfoBean awardInfoBean = null;
			double sum=0.00;
			double totalValue = 0.00;
			Equals eqLineNumber;
			dirty = false;
			if (selectedBean != null) {
				awardInfoBean = (AwardAmountInfoBean)selectedBean;
			}
            //have to set value in bean
			if (cvTableData != null) {
				
				AwardBudgetBean awardBudgetBean = (AwardBudgetBean)cvTableData.elementAt(rowIndex);
				if (value != null) {
					if (awardBudgetBean != null) {
						switch(columnIndex) {

							case LINE_COLUMN:
								if (!value.equals(EMPTY_STRING)) {
									awardBudgetBean.setLineItemNumber(Integer.parseInt(value.toString()));
								}
								break;
							case CE_COLUMN:
								if(value.toString().trim().length() > 0) {
									// Check for the valid cost element.
									Equals eqCostElement = new Equals("costElement",value.toString().trim());
									CoeusVector cvFilteredCost = cvAddCostElements.filter(eqCostElement);
									String modifiedDefaultCE = getNewDefaultCostElement();
									if (modifiedDefaultCE != null) {
										defaultCostElement = modifiedDefaultCE;
									}
									if (cvFilteredCost != null && cvFilteredCost.size() > 0 ) {
										DepartmentBudgetFormBean descBean = (DepartmentBudgetFormBean)cvFilteredCost.get(0);
										if (!defaultCostElement.equals(value.toString().trim())) {
											awardBudgetBean.setCostElement(value.toString().trim());
											awardBudgetBean.setCostElementDescription(descBean.getDescription());
										} else {
											CoeusOptionPane.showInfoDialog(coeusMessageResources.
												parseMessageKey("awardMoneyAndEndDates_exceptionCode.1155"));
											awardBudgetBean.setCostElement(EMPTY_STRING);
											awardBudgetBean.setCostElementDescription(EMPTY_STRING);
											budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex, rowIndex);
											budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
											setRequestFocusInThread(budgetForAwardTableCellEditor.txtCEField);
											return;
										}
									} else {
										CoeusOptionPane.showErrorDialog(coeusMessageResources.
											parseMessageKey("awardMoneyAndEndDates_exceptionCode.1154"));
										isValidCostElement = true;
										budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex, rowIndex);
										budgetForAwardForm.tblBudget.setColumnSelectionInterval(1,1);
										setRequestFocusInThread(budgetForAwardTableCellEditor.txtCEField);
										//awardBudgetBean.setCostElement(EMPTY_STRING);
										//awardBudgetBean.setCostElementDescription(EMPTY_STRING);
										//awardBudgetBean.setCostElement(value.toString());
										//awardBudgetBean.setCostElementDescription(EMPTY_STRING);
										return;
									}
								}
//								String desc = (String)this.getValueAt(rowIndex, COST_ELEMENT_DESC_COLUMN);
//								
//								if(value.toString().trim().equals(EMPTY_STRING)){
//									desc  = EMPTY_STRING;
//								}
								if(awardBudgetBean.getCostElement()!= null &&
								awardBudgetBean.getCostElement().trim().equals(value.toString()))break;
								if (value.toString().trim().equals(EMPTY_STRING)) {
									awardBudgetBean.setCostElement(EMPTY_STRING);
									awardBudgetBean.setCostElementDescription(EMPTY_STRING);
									
								}
								modified = true;
								break;
							case COST_ELEMENT_DESC_COLUMN:
								awardBudgetBean.setCostElementDescription(value.toString());
								break;
							case LINE_ITEM_DESC_COLUMN:
								if(awardBudgetBean.getLineItemDescription()!= null && 
									awardBudgetBean.getLineItemDescription().trim().equals(value.toString().trim())) break;
								awardBudgetBean.setLineItemDescription(value.toString());
								modified = true;
								break;
							case ANT_AMOUNT_COLUMN:
								if(awardBudgetBean.getAnticipatedAmount() == new Double(value.toString()).doubleValue())break;
								if(Double.parseDouble(value.toString()) < 0) {
									CoeusOptionPane.showErrorDialog("Anticipated Amount should not be less then zero");
									dirty = true;
									budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex,rowIndex);
									setRequestFocusInThread(budgetForAwardTableCellEditor.txtAnticipatedAmt);
									return;
								}
								
								awardBudgetBean.setAnticipatedAmount(new Double(value.toString()).doubleValue());

								 sum = calculateSum(columnIndex);

								if (awardInfoBean == null) return;
								 totalValue = awardInfoBean.getAnticipatedTotalAmount();

								 eqLineNumber = new Equals("lineItemNumber",new Integer(1));
								CoeusVector cvFilteredAnt = cvTableData.filter(eqLineNumber);
								if (cvFilteredAnt != null && cvFilteredAnt.size() >= 0) {
									AwardBudgetBean bean = (AwardBudgetBean)cvFilteredAnt.get(0);
									bean.setAnticipatedAmount(totalValue - sum);
									bean.setAcType(TypeConstants.UPDATE_RECORD);
									modified = true;
								}
								
								budgetForAwardTableModel.fireTableDataChanged();
								budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex,rowIndex);
								
								break;
							case OBL_AMOUNT_COLUMN:
								if(awardBudgetBean.getObligatedAmount() == new Double(value.toString()).doubleValue())break;
								if(Double.parseDouble(value.toString()) < 0) {
									CoeusOptionPane.showErrorDialog("Obligated Amount should not be less then zero");
									dirty = true;
									budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex,rowIndex);
									setRequestFocusInThread(budgetForAwardTableCellEditor.txtObligatedField);
									return;
								}
								
								awardBudgetBean.setObligatedAmount(new Double(value.toString()).doubleValue());

								 sum = calculateSum(columnIndex);
								if (awardInfoBean == null) return;
								 totalValue = awardInfoBean.getAmountObligatedToDate();

								 eqLineNumber = new Equals("lineItemNumber",new Integer(1));
								CoeusVector cvFilteredObl = cvTableData.filter(eqLineNumber);
								if (cvFilteredObl != null && cvFilteredObl.size() >= 0) {
									AwardBudgetBean bean = (AwardBudgetBean)cvFilteredObl.get(0);
									bean.setObligatedAmount(totalValue - sum);
									bean.setAcType(TypeConstants.UPDATE_RECORD);
									modified = true;
								}
								
								budgetForAwardTableModel.fireTableDataChanged();
								budgetForAwardForm.tblBudget.setRowSelectionInterval(rowIndex,
														rowIndex);
								break;
							
						}
						if (awardBudgetBean.getAcType() == null) {
							awardBudgetBean.setAcType(TypeConstants.UPDATE_RECORD);
						}
					}
				}
			}
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
		
		/**
		 * return true if cell is editable, else returns false
		 * @return boolean
		 */        
        public boolean isCellEditable(int row, int col) {
			if( getFunctionType() == TypeConstants.DISPLAY_MODE){
                return false;
            } 
			AwardBudgetBean budgetBean = (AwardBudgetBean)cvTableData.get(row);
			if (budgetBean.getLineItemNumber() == 1) {
           		return false;
			} 
			if (budgetBean.getLineItemNumber() != 1 && col == 0) {
				return false;
			}
			if (budgetBean.getLineItemNumber() != 1 && col == 2) {
				return false;
			}
			return true;
        }
     }
    
  /**
   * This is an inner class represents the table cell editor for the Award For Budget
   * screen table
   **/
    public class BudgetForAwardTableCellEditor extends AbstractCellEditor 
                                         implements MouseListener, TableCellEditor {
        
       	private CoeusTextField txtLineField;
        private CoeusTextField txtCEField;
        private CoeusTextField txtCostElementDesc;
        private CoeusTextField txtLineItemDesc;
        private DollarCurrencyTextField txtAnticipatedAmt;
		private DollarCurrencyTextField txtObligatedField;
		
        private int column;        
        
        /**
         * Default Constructor
         **/
        public BudgetForAwardTableCellEditor() {
            
            txtLineField = new CoeusTextField();
			txtCEField = new CoeusTextField();
            txtCostElementDesc = new CoeusTextField();
            txtLineItemDesc = new CoeusTextField();
			txtAnticipatedAmt = new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
            txtObligatedField = new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
            registerComponents();
        }
		/**
		 * To register the components
		 */
		 private void registerComponents(){
            txtCEField.addMouseListener(this);
			txtCostElementDesc.addMouseListener(this);
        }
        /**
         * To get the cell editor value
         * @return Object
         **/
        public Object getCellEditorValue() {
            switch(column) {
				case LINE_COLUMN:
					return txtLineField.getText();
                case CE_COLUMN:
					return txtCEField.getText();
				case COST_ELEMENT_DESC_COLUMN:
					return txtCostElementDesc.getText();
                case LINE_ITEM_DESC_COLUMN:
                    return txtLineItemDesc.getText();
                case ANT_AMOUNT_COLUMN:
                    return txtAnticipatedAmt.getValue();
				case OBL_AMOUNT_COLUMN:
                    return txtObligatedField.getValue();    
              }
            return null;
        }
        
        /**
         * To get the table cell editor component
         * @param table javax.swing.JTable
         * @param value Object
         * @param isSelected boolean
         * @param row int
         * @param column int
         * @return java.awt.Component
         **/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
             this.column = column;
			 if (cvTableData != null) {
				 AwardBudgetBean bean = (AwardBudgetBean)cvTableData.get(row);
				 if (bean != null) {
					 switch(column) {
						case LINE_COLUMN:
							if (bean.getLineItemNumber()!= 1) {
								txtLineField.setText(value.toString());
							}
							return txtLineField;
						case CE_COLUMN:
							if (bean.getLineItemNumber() != 1){
								txtCEField.setText(value.toString());
							}
							return txtCEField;
						case COST_ELEMENT_DESC_COLUMN:
							if (bean.getLineItemNumber() != 1) {
								txtCostElementDesc.setText(value.toString());
							}
							return txtCostElementDesc;
						case LINE_ITEM_DESC_COLUMN:
							if (bean.getLineItemNumber() != 1) {
								txtLineItemDesc.setText(value.toString());
							}
							return txtLineItemDesc;
						case ANT_AMOUNT_COLUMN:
							if (bean.getLineItemNumber() != 1) {
								txtAnticipatedAmt.setValue(new Double(value.toString()).doubleValue());
							}

							return txtAnticipatedAmt;
						case OBL_AMOUNT_COLUMN:
							if (bean.getLineItemNumber() != 1) {
								txtObligatedField.setValue(new Double(value.toString()).doubleValue());   
							}
							return txtObligatedField;
					 }
				}
			}
             return txtLineItemDesc;
        }
        
		public void requestFocus() {
			switch (column) {
				case LINE_ITEM_DESC_COLUMN:
					txtLineItemDesc.requestFocus();
					break;
				case CE_COLUMN:
					txtCEField.requestFocus();
					break;
				case ANT_AMOUNT_COLUMN:
					txtAnticipatedAmt.requestFocus();
					break;
			}
		}
		
		/**
		 * Actions on click of mouse
		 * @param mouseEvent java.awt.event.MouseEvent
		 */
		public void mouseClicked(MouseEvent mouseEvent) {
			if (mouseEvent.getClickCount() != 2) return ;
			if (EMPTY_STRING.equals(txtCEField.getText()) && 
				EMPTY_STRING.equals(txtCostElementDesc.getText())) {
				if (mouseEvent.getSource().equals(txtCEField) ||
					mouseEvent.getSource().equals(txtCostElementDesc)) {
					budgetForAwardForm.btnCESearch.doClick();
				}
			}
		}
		
		public void mouseEntered(MouseEvent e)
		{
		}
		
		public void mouseExited(MouseEvent e)
		{
		}
		
		public void mousePressed(MouseEvent e)
		{
		}
		
		public void mouseReleased(MouseEvent e)
		{
		}
		
    }
    
   /**
    * This is an inner class represents the table cell renderer for the Award For Budget
    * screen table
    **/
    public class BudgetForAwardTableCellRenderer extends DefaultTableCellRenderer {

		private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtAnticipatedAmt;
        private DollarCurrencyTextField txtObligatedAmt;
                
        private JLabel lblValue;
        
        /**
         * Default Constructor
         **/
        public BudgetForAwardTableCellRenderer() {
			txtComponent = new CoeusTextField();
			txtComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtAnticipatedAmt = new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			txtAnticipatedAmt.setBorder(new EmptyBorder(0,0,0,0));
            txtObligatedAmt = new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			txtObligatedAmt.setBorder(new EmptyBorder(0,0,0,0)); 
			// For the display mode 
			            
            lblValue = new JLabel();
            lblValue.setOpaque(true);
            lblValue.setBorder(new EmptyBorder(0,0,0,0));
            
		}
        /**
         * To get the table cell editor component
         * @param table javax.swing.JTable
         * @param value Object
         * @param isSelected boolean
         * @param row int
         * @param column int
         * @return java.awt.Component
         **/
       public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
											
		// If 1st row is selected then set the second row as selected
		
		AwardBudgetBean budgetBean = (AwardBudgetBean)cvTableData.get(row);
		if (isSelected && budgetBean.getLineItemNumber() == 1) {
			if(row > 0) {
				//select prev row
				budgetForAwardForm.tblBudget.setRowSelectionInterval(row - 1, row - 1);
			} else if(cvTableData.size() > 1) {
				budgetForAwardForm.tblBudget.setRowSelectionInterval(row + 1, row + 1);
			} 
		}
		
		if (value != null) {
		   switch (col) {
			   case LINE_COLUMN:
				   if(isSelected && budgetBean.getLineItemNumber() != 1){
						txtComponent.setBackground(Color.yellow);
						lblValue.setBackground(Color.yellow);
					}
					else {
						txtComponent.setBackground(Color.white);
						lblValue.setBackground(Color.white);
					}
				   if (budgetBean.getLineItemNumber() == 1) {
						txtComponent.setForeground(Color.blue);
						txtComponent.setFont(CoeusFontFactory.getLabelFont());
						lblValue.setForeground(Color.blue);
						lblValue.setFont(CoeusFontFactory.getLabelFont());
					} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
					}
				   
				   if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						if (budgetBean.getLineItemNumber() == 1) {
							txtComponent.setForeground(Color.blue);
							txtComponent.setFont(CoeusFontFactory.getLabelFont());
							txtComponent.setBackground(disabledBackground);
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setBackground(disabledBackground);
						} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						txtComponent.setBackground(disabledBackground);
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setBackground(disabledBackground);
						}
                    }
				   txtComponent.setText(value.toString());
				   txtComponent.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
				   lblValue.setText(value.toString());
				   lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
				   return lblValue;
			   case CE_COLUMN:
				   if(isSelected && budgetBean.getLineItemNumber() != 1) {
						txtComponent.setBackground(Color.yellow);
						lblValue.setBackground(Color.yellow);
					}
					else {
						txtComponent.setBackground(Color.white);
						lblValue.setBackground(Color.white);
					}
				   if (budgetBean.getLineItemNumber() == 1) {
						txtComponent.setForeground(Color.blue);
						txtComponent.setFont(CoeusFontFactory.getLabelFont());
						lblValue.setForeground(Color.blue);
						lblValue.setFont(CoeusFontFactory.getLabelFont());
					} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
					}	
				   if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						if (budgetBean.getLineItemNumber() == 1) {
							txtComponent.setForeground(Color.blue);
							txtComponent.setFont(CoeusFontFactory.getLabelFont());
							txtComponent.setBackground(disabledBackground);
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setBackground(disabledBackground);
						} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						txtComponent.setBackground(disabledBackground);
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setBackground(disabledBackground);
						}
                    }
				   txtComponent.setText(value.toString());
				   txtComponent.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				   lblValue.setText(value.toString());
				   lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				   return lblValue;
			   case COST_ELEMENT_DESC_COLUMN:
				   if(isSelected && budgetBean.getLineItemNumber() != 1) {
						txtComponent.setBackground(Color.yellow);
						lblValue.setBackground(Color.yellow);
					}
					else {
						txtComponent.setBackground(Color.white);
						lblValue.setBackground(Color.white);
					}
				   if (budgetBean.getLineItemNumber() == 1) {
						txtComponent.setForeground(Color.blue);
						txtComponent.setFont(CoeusFontFactory.getLabelFont());
						lblValue.setForeground(Color.blue);
						lblValue.setFont(CoeusFontFactory.getLabelFont());
					} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
					}
				   if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						if (budgetBean.getLineItemNumber() == 1) {
							txtComponent.setForeground(Color.blue);
							txtComponent.setFont(CoeusFontFactory.getLabelFont());
							txtComponent.setBackground(disabledBackground);
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setBackground(disabledBackground);
						} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						txtComponent.setBackground(disabledBackground);
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setBackground(disabledBackground);
						}
                    }
				   txtComponent.setText(value.toString());
				   txtComponent.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				   lblValue.setText(value.toString());
				   lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				   return lblValue;

			   case LINE_ITEM_DESC_COLUMN:
					if(isSelected && budgetBean.getLineItemNumber() != 1){
						txtComponent.setBackground(Color.yellow);
						lblValue.setBackground(Color.yellow);
					} else {
						txtComponent.setBackground(Color.white);
						lblValue.setBackground(Color.white);
					}
					if (budgetBean.getLineItemNumber() == 1) {
						txtComponent.setForeground(Color.blue);
						txtComponent.setFont(CoeusFontFactory.getLabelFont());
						lblValue.setForeground(Color.blue);
						lblValue.setFont(CoeusFontFactory.getLabelFont());
					} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
					}
					if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						if (budgetBean.getLineItemNumber() == 1) {
							txtComponent.setForeground(Color.blue);
							txtComponent.setFont(CoeusFontFactory.getLabelFont());
							txtComponent.setBackground(disabledBackground);
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setBackground(disabledBackground);
						} else {
						txtComponent.setForeground(Color.black);
						txtComponent.setFont(CoeusFontFactory.getNormalFont());
						txtComponent.setBackground(disabledBackground);
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setBackground(disabledBackground);
						}
                    }
					txtComponent.setText(value.toString());
					txtComponent.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					lblValue.setText(value.toString());
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				   return lblValue;
			   case ANT_AMOUNT_COLUMN:
				   
				   if(isSelected && budgetBean.getLineItemNumber() != 1){
						txtAnticipatedAmt.setBackground(Color.yellow);
						lblValue.setBackground(Color.yellow);
					}
					else {
						txtAnticipatedAmt.setBackground(Color.white);
						lblValue.setBackground(Color.white);
					}   
				   	if (budgetBean.getLineItemNumber() == 1) {
						double totalValue = budgetBean.getAnticipatedAmount();
						if (totalValue < 0) {
							txtAnticipatedAmt.setForeground(Color.red);
							txtAnticipatedAmt.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setForeground(Color.red);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
						} else {
							txtAnticipatedAmt.setForeground(Color.blue);
							txtAnticipatedAmt.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
						}
					} else {
						txtAnticipatedAmt.setForeground(Color.black);
						txtAnticipatedAmt.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
					}
					txtAnticipatedAmt.setValue(new Double(value.toString()).doubleValue());
					lblValue.setText(txtAnticipatedAmt.getText());
					if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						if (budgetBean.getLineItemNumber() == 1) {
							txtAnticipatedAmt.setForeground(Color.blue);
							txtAnticipatedAmt.setFont(CoeusFontFactory.getLabelFont());
							txtAnticipatedAmt.setBackground(disabledBackground);
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setBackground(disabledBackground);
						} else {
						txtAnticipatedAmt.setForeground(Color.black);
						txtAnticipatedAmt.setFont(CoeusFontFactory.getNormalFont());
						txtAnticipatedAmt.setBackground(disabledBackground);
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setBackground(disabledBackground);
						}
                    }
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
				   return lblValue;

			   case OBL_AMOUNT_COLUMN:
					 if(isSelected && budgetBean.getLineItemNumber() != 1){
						txtObligatedAmt.setBackground(Color.yellow);
						lblValue.setBackground(Color.yellow);
					}
					else {
						txtObligatedAmt.setBackground(Color.white);
						lblValue.setBackground(Color.white);
					} 
					if (budgetBean.getLineItemNumber() == 1) {
						double totalValue = budgetBean.getObligatedAmount();
						if (totalValue < 0) {
							txtObligatedAmt.setForeground(Color.red);
							txtObligatedAmt.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setForeground(Color.red);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
						} else {
							txtObligatedAmt.setForeground(Color.blue);
							txtObligatedAmt.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
						}
					} else {
						txtObligatedAmt.setForeground(Color.black);
						txtObligatedAmt.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
					}
					 if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						if (budgetBean.getLineItemNumber() == 1) {
							txtObligatedAmt.setForeground(Color.blue);
							txtObligatedAmt.setFont(CoeusFontFactory.getLabelFont());
							txtObligatedAmt.setBackground(disabledBackground);
							lblValue.setForeground(Color.blue);
							lblValue.setFont(CoeusFontFactory.getLabelFont());
							lblValue.setBackground(disabledBackground);
						} else {
						txtObligatedAmt.setForeground(Color.black);
						txtObligatedAmt.setFont(CoeusFontFactory.getNormalFont());
						txtObligatedAmt.setBackground(disabledBackground);
						lblValue.setForeground(Color.black);
						lblValue.setFont(CoeusFontFactory.getNormalFont());
						lblValue.setBackground(disabledBackground);
						}
                    }
					txtObligatedAmt.setValue(new Double(value.toString()).doubleValue());  
					lblValue.setText(txtObligatedAmt.getText());
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
				  return lblValue;
				 }
			}

		return lblValue;
		}
	}
	
	/** This method will provide the key travrsal for the table cells
     *	It specifies the tab and shift tab order.
	 */
    public void setTableKeyTraversal(){
        
         javax.swing.InputMap im = budgetForAwardForm.tblBudget.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = budgetForAwardForm.tblBudget.getActionMap().get(im.get(tab));
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
        budgetForAwardForm.tblBudget.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = budgetForAwardForm.tblBudget.getActionMap().get(im.get(shiftTab));
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
        budgetForAwardForm.tblBudget.getActionMap().put(im.get(shiftTab), tabAction1);
    }
	
	
	 private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
	 /**
	  * To set all the instance variable to null.
	  * calling from the base class
	  **/
	 public void cleanUp() {
		 
		dlgBudgetForAward = null;
        mdiForm = null;
        windowTitle = null;
		costElementsLookupWindow = null;
		cvTableData = null;
		cvDelData = null;
		cvAddCostElements = null;
		awardBudgetBean = null;
		selectedBean = null;
		defaultCostElement = null;
		awardAmountTreeTable = null;
		budgetForAwardForm = null;
		budgetForAwardTableModel = null;
		budgetForAwardTableCellEditor = null;
		budgetForAwardTableCellRenderer = null;
	 }
 
}
