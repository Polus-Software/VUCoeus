/*
 * AwardIndirectCostController.java
 *
 * Created on May 26, 2004, 4:42 PM
 */

/* PMD check performed, and commented unused imports and variables on 25-FEB-2011
 * by Bharati 
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardCommentsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.text.*;

import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.bean.ValidRatesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardIDCRateBean;
import edu.mit.coeus.award.gui.AwardIndirectCostForm;

import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import edu.ucsd.coeus.personalization.controller.AbstractController;



/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  Arun MP
 */
public class AwardIndirectCostController extends AwardController implements ActionListener {
	
	private Date utilStartDate;
	private Date utilEndDate;
	private String startDate;
	private String endDate;
	private int newYear;
	private int newYearMinus;
	private CoeusTextField txtFiscalYear;
	private CoeusTextField txtCompAccount;
	private CurrencyField txtRate;
	private CoeusTextField txtComponent;
	private DollarCurrencyTextField txtCurrencyField;
	private Color disabledBackground = (Color) UIManager.
	getDefaults().get("Panel.background");
	private AwardIndirectCostForm awardIndirectCostForm;
	private CoeusDlgWindow dlgIndirectCost;
	private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
	private QueryEngine queryEngine;
	
	private CoeusVector cvTableData,cvIndirect,cvCampus,cvDataIndirect,
	cvDataCampus,cvInd,cvCamp,cvDeletedData,cvTempComment,cvParameters,
	cvCommentDescription, cvValidRatesData,cvData,cvValidRatesData1,cvParameter;
	private IndirectCostTableModel indirectCostTableModel;
	private IndirectCostRenderer indirectCostRenderer;
	private IndirectCostEditor indirectCostEditor;
	private CoeusMessageResources coeusMessageResources;
	private AmountTableModel amountTableModel;
	private AmountTableCellRenderer amountTableCellRenderer;
	private DateUtils dateUtils;
	private int col;
	private CoeusComboBox cmbRateCode = new CoeusComboBox();
	private CoeusComboBox cmbCamp = new CoeusComboBox();
	private DateUtils dtUtils;
	private SimpleDateFormat dtFormat  = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat dtFormatDisp = new SimpleDateFormat("dd-MMM-yyyy");
	private SimpleDateFormat newdtFormat  = new SimpleDateFormat("dd/MMM/yyyy");
	private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
	private String mesg;
	private String comments;
	private String parameterValue;
	private SimpleDateFormat simpleDateFormat;
	private DollarCurrencyTextField txtAmount;
	private static final String DELETE_CONFIRMATION="Are you sure you want to delete this IDC Rate row?";
	private static final String SELECT_A_ROW="Please select an IDC Row";
	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	private static final String INVALID_START_DATE ="Please enter a valid start date";
	private static final String INVALID_END_DATE ="Please enter a valid end date";
	private static final int PERCENTAGE = 0;
	private static final String WINDOW_TITLE = "Indirect Cost";
	private static final String ON="On";
	private static final String OFF="Off";
	private static final int APPLICABLE_IDC_RATE = 0;
	private static final int IDC_RATE_TYPECODE = 1;
	private static final int FISCAL_YEAR = 2;
	private static final int START_DATE = 3;
	private static final int END_DATE = 4;
	private static final int ON_OFF_CAMPUS_FLAG = 5;
	private static final int UNDER_RECOVERY_OF_IDC = 6;
	private static final int SOURCE_ACCOUNT = 7;
	private static final int DESTINATION_ACCOUNT = 8;
	private static final int TOTAL_COLUMN=0;
	private static final int TOTAL_AMOUNT_COLUMN=1;
	private double cost = 0.0;
	private int rowId;
	
	private static final String DATE_SEPARATERS = ":/.,|-";
        private final String DATE_SEPERATOR = "/-:,";
	private static final String EMPTY_STRING = "";
	private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
    //private static final int WIDTH = 755;
	private static final int WIDTH = 925;
        private int accountNumberMaxLength = 0;
    //Case#2402 - End
	private static final int HEIGHT = 500;
	// Representing the instance of beans of IDC rates,base bean,Details bean,comments bean
	// parameter bean and valid rates bean
	public AwardIDCRateBean awardIDCRateBean;
	public AwardBaseBean awardBaseBean;
	public AwardDetailsBean awardDetailsBean;
	public AwardCommentsBean commentsBean;
	public AwardCommentsBean queryCommentsBean;
	public CoeusParameterBean coeusParameterBean;
	public ValidRatesBean validRatesBean;
	
	private boolean isCommentPresent=false;
	private boolean modified = false;
	private boolean campusComboPopulated = false;
	private static final String SAVE_CHANGES = "saveConfirmCode.1002";
	private boolean checkOnFlag = false;
	private boolean checkOffFlag = false;
	
        //Bug Fix:1124 
        private boolean ZERO_ENTERED = false;
        private double DEFAULT_IDC_RATE = -1;
        
        //added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
        private String fiscalYearStart = "";
        private String fiscalDate = "";
        private int fiscalNewMonth;
        private int fiscalNewDate;
        private static final String SERVLET = "/AwardMaintenanceServlet";
        //added for COEUSQA -1728 : parameter to define the start date of fiscal year - end

	/**
	 * Creates a new instance of AwardIndirectCostController
	 * @param awardBaseBean AwardBaseBean
	 * @param functionType char
	 */
	public AwardIndirectCostController(AwardBaseBean awardBaseBean, char functionType  ) {
		super(awardBaseBean);
		this.mdiForm = mdiForm;
		
		this.awardBaseBean = awardBaseBean;
		
		queryEngine = QueryEngine.getInstance();
		awardIndirectCostForm = new AwardIndirectCostForm();
		coeusMessageResources = CoeusMessageResources.getInstance();
		registerComponents();
		setFormData(awardBaseBean);
		setTableKeyTraversal();
		setFunctionType(functionType);
		postInitComponents();
		setTableEditors();            
		
	}
	private void postInitComponents(){
		dlgIndirectCost = new CoeusDlgWindow(mdiForm);
		dlgIndirectCost.setResizable(false);
		dlgIndirectCost.setModal(true);
		dlgIndirectCost.getContentPane().add( awardIndirectCostForm);
		dlgIndirectCost.setDefaultCloseOperation(dlgIndirectCost.DISPOSE_ON_CLOSE);
		dlgIndirectCost.setSize(WIDTH, HEIGHT);
		dlgIndirectCost.setLocation(dlgIndirectCost.CENTER);
		dlgIndirectCost.setTitle(WINDOW_TITLE);
		awardDetailsBean = new AwardDetailsBean();
		
		try{
			// For getting the details for the headerForm
			CoeusVector cvAwardDetails = queryEngine.executeQuery(queryKey,
			AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
			if( cvAwardDetails != null && cvAwardDetails.size() > 0 ) {
				awardDetailsBean = (AwardDetailsBean)cvAwardDetails.get(0);
				awardIndirectCostForm.awardHeaderForm1.lblSponsorAwardNumberValue.setText(
				awardDetailsBean.getSponsorAwardNumber());
				awardIndirectCostForm.awardHeaderForm1.lblAwardNumberValue.setText(
				awardDetailsBean.getMitAwardNumber());
				//Modified for bug fix for case #2336 start
				//awardIndirectCostForm.awardHeaderForm1.lblSequenceNumberValue.setText(EMPTY+awardDetailsBean.getSequenceNumber());
                awardIndirectCostForm.awardHeaderForm1.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
                //Modified for bug fix for case #2336 start
			}
		}catch (CoeusException coeusException){
			coeusException.printStackTrace();
		}
		
		dlgIndirectCost.setModal(true);
		dlgIndirectCost.addEscapeKeyListener(
		new AbstractAction("escPressed"){
			public void actionPerformed(ActionEvent ae){
				performCancelAction();
				return;
			}
		});
		dlgIndirectCost.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
		dlgIndirectCost.addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent we) {
				focusInWindow();
			}
			public void windowClosing(WindowEvent we){
				performCancelAction();
				return;
			}
		});
		
		dlgIndirectCost.addComponentListener(
		new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				requestDefaultFocus();
			}
		});
		
	} //end of PostinitComponents!!!
	private  void focusInWindow(){
		awardIndirectCostForm.btnAdd.requestFocusInWindow();
	}
	/**
	 * Registering the components
	 */
	public void registerComponents() {
		
		indirectCostRenderer = new IndirectCostRenderer();
		indirectCostEditor = new IndirectCostEditor();
		indirectCostTableModel = new IndirectCostTableModel();
		
		amountTableModel = new AmountTableModel();
		amountTableCellRenderer=new AmountTableCellRenderer();
		
		dateUtils = new DateUtils();
		dtUtils = new DateUtils();
		Date utilStartDate = new Date();
		Date utilEndDate = new Date();
		
		awardIndirectCostForm.tblAwardIndirectCost.setModel(indirectCostTableModel);
		awardIndirectCostForm.tblAwardIndirectCost.setAutoResizeMode(awardIndirectCostForm.tblAwardIndirectCost.AUTO_RESIZE_OFF);
		awardIndirectCostForm.tblTotal.setModel(amountTableModel);
		awardIndirectCostForm.tblTotal.setBackground((Color) UIManager.
		getDefaults().get("Panel.background"));
		
		Component[] components = {awardIndirectCostForm.tblAwardIndirectCost,
		awardIndirectCostForm.tblTotal,awardIndirectCostForm.btnAdd,
		awardIndirectCostForm.btnCancel,
		awardIndirectCostForm.btnDelete,awardIndirectCostForm.btnOk,
		awardIndirectCostForm.btnRates,awardIndirectCostForm.lblComments,
		awardIndirectCostForm.txtArComments,awardIndirectCostForm.awardHeaderForm1};
		
		awardIndirectCostForm.btnAdd.addActionListener(this);
		awardIndirectCostForm.btnCancel.addActionListener(this);
		awardIndirectCostForm.btnDelete.addActionListener(this);
		awardIndirectCostForm.btnOk.addActionListener(this);
		awardIndirectCostForm.btnRates.addActionListener(this);
		
	} // end of  registerComponents()
	
	
	/**
	 * setting up the form data
	 * @param awardBaseBean Object
	 */
	public void setFormData(Object awardBaseBean) {
		awardBaseBean= (AwardBaseBean)awardBaseBean;
		cvTableData = new CoeusVector();
		cvDeletedData = new CoeusVector();
		cvParameters = new CoeusVector();
		cvIndirect = new CoeusVector();
		cvParameter = new CoeusVector();
		
		try{
			
			ValidRatesBean validRatesBean;
			CoeusVector cvData = new CoeusVector();
			try{
				cvValidRatesData = queryEngine.getDetails(queryKey, ValidRatesBean.class);
				cvValidRatesData.sort("onCampusRate");
				
				cvValidRatesData1 = queryEngine.getDetails(queryKey, ValidRatesBean.class);
				cvValidRatesData1.sort("onCampusRate");
				
			}catch (CoeusException coeusException){
				coeusException.printStackTrace();
			}
			
			// for the row values
			cvTableData = queryEngine.executeQuery(queryKey, AwardIDCRateBean.class,
			cvTableData.FILTER_ACTIVE_BEANS);
			
            //Changed the code for getting the max row id.Since the earlier code 
            //while getting the rowid was not considering beans whose ACTYPE was "D".
            CoeusVector cvRowId = queryEngine.getDetails(queryKey,AwardIDCRateBean.class);
			if(cvRowId != null && cvRowId.size() > 0){
				cvRowId.sort("rowId", false);
				rowId = ((AwardIDCRateBean)cvRowId.get(0)).getRowId();
			}
			
			if(cvTableData != null && cvTableData.size() > 0){
				awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(0,0);
				awardIDCRateBean = (AwardIDCRateBean)cvTableData.get(0);
				indirectCostTableModel.setData(cvTableData);
				amountTableModel.setData(cvTableData);
				amountTableModel.fireTableDataChanged();
			}
			if (cvTableData.size()<1) {
				awardIndirectCostForm.tblTotal.setVisible(false);
			}
			
			// for getting the combobox vector values of idc_rate_codes
			cvIndirect = queryEngine.getDetails(queryKey,KeyConstants.IDC_RATE_TYPES);
			
			cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//          for (int index=0;index<cvComments.size();index++) {
//				CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvComments.elementAt(index);
//				if(CoeusConstants.INDIRECT_COST_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
//					isCommentPresent=true;
//                  break;
//              }
//          }
            if(cvParameters != null && cvParameters.size()>0){
                 //To get the INDIRECT_COST_COMMENT_CODE parameter
                 CoeusVector cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.INDIRECT_COST_COMMENT_CODE));
                 if(cvFiltered != null && cvFiltered.size()>0){
					isCommentPresent=true;
                 }
                 //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
                 cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                 if(cvFiltered != null
                       && cvFiltered.size() > 0){
					CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
	                 accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                  }
             }
             //Case#2402 - End

                        
			if (!isCommentPresent) {
				awardIndirectCostForm.txtArComments.setEditable(false);
				awardIndirectCostForm.txtArComments.setOpaque(false);
				
			}else {
				awardIndirectCostForm.txtArComments.setEditable(true);
				awardIndirectCostForm.txtArComments.setOpaque(true);
				cvCommentDescription=new CoeusVector();
				cvCommentDescription=queryEngine.getDetails(queryKey,AwardCommentsBean.class);
				if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
					//CoeusVector return
					CoeusVector cvIDCRatesCommentCode = cvParameters.filter(new Equals("parameterName",
					CoeusConstants.INDIRECT_COST_COMMENT_CODE));
					CoeusParameterBean coeusParameterBean = null;
					coeusParameterBean = (CoeusParameterBean)cvIDCRatesCommentCode.elementAt(0);
					
					Equals equals = new Equals("commentCode", new
					Integer(coeusParameterBean.getParameterValue()));
					
					cvCommentDescription = cvCommentDescription.filter(equals);
					if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
						this.commentsBean=(AwardCommentsBean)cvCommentDescription.elementAt(0);
						comments = this.commentsBean.getComments();
						awardIndirectCostForm.txtArComments.setText(comments);
						awardIndirectCostForm.txtArComments.setCaretPosition(0);
					}
				}
			}
			// to get the parameter value for the parameter name = MIT_IDC_VALIDATION_ENABLED
			CoeusVector cvParameterValue = new CoeusVector();
			cvParameter = queryEngine.getDetails(queryKey,CoeusParameterBean.class);
			if (cvParameter != null) {
				cvParameterValue = cvParameter.filter(new Equals("parameterName",
				"MIT_IDC_VALIDATION_ENABLED"));
				if (cvParameterValue != null && cvParameterValue.size() > 0) {
					CoeusParameterBean parameterBean = (CoeusParameterBean)cvParameterValue.get(0);
					parameterValue = parameterBean.getParameterValue();
				}
			}
			
			
		}catch (CoeusException coeusException){
			coeusException.printStackTrace();
		}
		
	} //end of setForm
	
	/**
	 * To format the fields after setting up the columns in the table
	 */
	public void formatFields() {
	  /*  boolean enabled = getFunctionType() != DISPLAY_MODE ? true : false ;
		awardIndirectCostForm.btnRates.setEnabled(enabled);
		awardIndirectCostForm.btnCancel.setEnabled(enabled);
	   */
		
		if(getFunctionType() == TypeConstants.DISPLAY_MODE){
			
			Color bgColor = (Color)UIManager.getDefaults().get("Panel.background");
			awardIndirectCostForm.tblAwardIndirectCost.setBackground(bgColor);
			awardIndirectCostForm.tblAwardIndirectCost.setSelectionBackground(bgColor);
			
			awardIndirectCostForm.tblAwardIndirectCost.setBackground(bgColor);
			awardIndirectCostForm.tblAwardIndirectCost.setSelectionBackground(bgColor);
			
			awardIndirectCostForm.txtArComments.setBackground(bgColor);
			awardIndirectCostForm.tblTotal.setBackground(bgColor);
			awardIndirectCostForm.tblTotal.setSelectionBackground(bgColor);
			awardIndirectCostForm.btnAdd.setEnabled(false);
			awardIndirectCostForm.btnOk.setEnabled(false);
			awardIndirectCostForm.btnRates.setEnabled(true);
			awardIndirectCostForm.btnDelete.setEnabled(false);
			awardIndirectCostForm.btnCancel.setEnabled(true);
			awardIndirectCostForm.txtArComments.setWrapStyleWord(true);
			awardIndirectCostForm.txtArComments.setEditable(false);
			
		}
	} // end of format fields
	
	
	/**
	 * Display the form
	 */
	public void display() {
    	//    	rdias - UCSD's coeus personalization - Begin
//    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
//        persnref.customize_Form(getControlledUI(),"GENERIC");
//        persnref.customize_Form(awardIndirectCostForm.awardHeaderForm1,"GENERIC");
        //		rdias - UCSD's coeus personalization - End
		if (cvTableData.size() > 0) {
			//If atleast one row is there
			awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(0,0);
		}
		sortOnDisplay();
		dlgIndirectCost.setVisible(true);
	}
	
	/**
	 * To display the fields as a pair on load
	 */
	private void sortOnDisplay() {
		
		String fieldNames[] = {"fiscalYear", "idcRateTypeCode", "startDate"};
		if (cvTableData.size() > 0) {
			cvTableData.sort(fieldNames, true);
		}
		
	}
	
	
	/** This class will sort the column values in ascending and descending order
	 * based on number of clicks.
	 */
	
	public class ColumnHeaderListener extends MouseAdapter {
		String nameBeanId [][] ={
			{"0","applicableIDCRate" },
			{"1","idcRateTypeCode" },
			{"2","fiscalYear"},
			{"3","startDate"},
			{"4","endDate"},
			{"5","onOffCampusFlag"},
			{"6","underRecoveryOfIDC"},
			{"7","sourceAccount"},
			{"8","destinationAccount"}
		};
		boolean sort =true;
		/** Mouse click handler for the table headers to sort upon the headers
		 * @param evt mouse event
		 */
		public void mouseClicked(MouseEvent evt) {
			indirectCostEditor.stopCellEditing();
			try {
				JTable table = ((JTableHeader)evt.getSource()).getTable();
				TableColumnModel colModel = table.getColumnModel();
				// The index of the column whose header was clicked
				int vColIndex = colModel.getColumnIndexAtX(evt.getX());
				// int mColIndex = table.convertColumnIndexToModel(vColIndex);
				if(cvTableData != null && cvTableData.size()>0 &&
				nameBeanId [vColIndex][1].length() >1 ){
					//chek if column clicked is onOffCampus. since boolean is not of Comparable type.
					//sort method cannot be used here. will have to sort these ourselves.
					if(vColIndex == ON_OFF_CAMPUS_FLAG) {
						sortOnOffCampus(sort);
						if (sort) {
							sort = false;
						}
						else {
							sort = true;
						}
					}
					else {
						((CoeusVector)cvTableData).sort(nameBeanId [vColIndex][1],sort);
						if (sort) {
							sort = false;
						}
						else {
							sort = true;
						}
					}
					indirectCostTableModel.fireTableRowsUpdated(
					0, indirectCostTableModel.getRowCount());
				}
			} catch(Exception exception) {
				//exception.printStackTrace();
				exception.getMessage();
			}
		}
	}// End of ColumnHeaderListener.................
	
	private void sortOnOffCampus1(boolean ascending) {
		if (cvTableData != null && cvTableData.size() > 0) {
			for (int index = 0; index < cvTableData.size(); index++ ) {
				for(int nextIndex = index+1; nextIndex <cvTableData.size(); nextIndex++){
					AwardIDCRateBean sortBean = (AwardIDCRateBean)cvTableData.get(index); //0
					
					if (ascending != sortBean.isOnOffCampusFlag()){
						AwardIDCRateBean temp = (AwardIDCRateBean)cvTableData.get(nextIndex);
						if (!temp.isAw_OnOffCampusFlag()) {
							cvTableData.set(index, cvTableData.get(nextIndex));
							cvTableData.set(nextIndex, temp);
						}
					} else {
						AwardIDCRateBean temp = (AwardIDCRateBean)cvTableData.get(index);
						cvTableData.set(index, cvTableData.get(nextIndex));
						cvTableData.set(nextIndex, temp);
					}
				}
			}
		}
		//indirectCostTableModel.fireTableDataChanged();
	}
	
	
	private void sortOnOffCampus(boolean ascending) {
		int compareValue;
		if (ascending) {
			compareValue =-1;
		} else {
			compareValue =1;
		}
		if (cvTableData != null && cvTableData.size() > 0) {
			for (int index = 0; index < cvTableData.size(); index++ ) {
				AwardIDCRateBean sortBean = (AwardIDCRateBean)cvTableData.get(index);
				for(int nextIndex = index+1; nextIndex <cvTableData.size(); nextIndex++){
					AwardIDCRateBean temp = (AwardIDCRateBean)cvTableData.get(nextIndex);
					if (booleanCompare(sortBean.isOnOffCampusFlag(), temp.isOnOffCampusFlag())==compareValue ){
						cvTableData.set(nextIndex, cvTableData.get(index));
						cvTableData.set(index, temp);
					}
				}
			}
		}
	}
	
	private int booleanCompare(boolean compareFrom, boolean compareTo) {
		if (compareFrom) {
			if (compareTo) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (compareTo) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	/**
	 * To get the GUI
	 * @return java.awt.Component
	 */
	public Component getControlledUI() {
		return awardIndirectCostForm;
	}
	
	/**
	 * To get the form data
	 * @return Object
	 */
	public Object getFormData() {
		return awardIndirectCostForm;
	}
	
	// for saving the datas entered
	
	/**
	 * This method will save the form data
	 */
	public void saveFormData() {
		
		indirectCostEditor.stopCellEditing();
		try{
			CoeusVector dataObject = new CoeusVector();
			CoeusVector cvCmt = new CoeusVector();
			StrictEquals stCommentsEquals = new StrictEquals();
			
			if(commentsBean!= null){
				commentsBean.setComments(awardIndirectCostForm.txtArComments.getText());
				
			}else{
				commentsBean = new AwardCommentsBean();
				commentsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
				commentsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
				commentsBean.setComments(awardIndirectCostForm.txtArComments.getText());
				
			}
			AwardCommentsBean queryCommentsBean = new AwardCommentsBean();
			CoeusVector cvTempComment = queryEngine.getDetails(queryKey, AwardCommentsBean.class);
			cvParameters =
			queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
			CoeusVector cvIDCRatesComment = null;
			CoeusParameterBean coeusParameterBean = null;
			CoeusVector cvIDCRatesCommentCode = cvParameters.filter(new Equals("parameterName",
			CoeusConstants.INDIRECT_COST_COMMENT_CODE));
			if(cvIDCRatesCommentCode!=null && cvIDCRatesCommentCode.size() > 0){
				coeusParameterBean = (CoeusParameterBean)cvIDCRatesCommentCode.elementAt(0);
			}
			if (cvTempComment!= null && cvTempComment.size()>0) {
				if(coeusParameterBean!=null){
					Equals equals = new Equals("commentCode", new
					Integer(coeusParameterBean.getParameterValue()));
					
					cvIDCRatesComment = cvTempComment.filter(equals);
					if(cvIDCRatesComment!=null && cvIDCRatesComment.size() > 0){
						queryCommentsBean = (AwardCommentsBean)cvIDCRatesComment.elementAt(0);
					}
				}
			}
			
			if(commentsBean!= null){
				if(! stCommentsEquals.compare(commentsBean, queryCommentsBean)){
					//Data Changed. save to query Engine.
					if(cvIDCRatesComment==null || cvIDCRatesComment.size() == 0){
						if(coeusParameterBean != null){
							commentsBean.setAcType(TypeConstants.INSERT_RECORD);
							commentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
							
							if (!EMPTY_STRING.equals(commentsBean.getComments())) {//For bug fix : 1209 - (if condition) - Jobin
								queryEngine.insert(queryKey, commentsBean);
							}
						}
					}else{
						commentsBean.setAcType(TypeConstants.UPDATE_RECORD);
						queryEngine.update(queryKey, commentsBean);
					}
					BeanEvent beanEvent = new BeanEvent();
					beanEvent.setBean(new AwardCommentsBean());
					beanEvent.setSource(this);
					fireBeanUpdated(beanEvent);
					
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
						AwardIDCRateBean bean = (AwardIDCRateBean) dataObject.get(index);
						if(bean.getAcType()!= null){
							if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
								//First delete the existing data and then insert the same. This is
								//required since primary keys can be modified
								bean.setAcType(TypeConstants.DELETE_RECORD);
								queryEngine.delete(queryKey, bean);
								bean.setAcType(TypeConstants.INSERT_RECORD);
								
								//		rowId = rowId + 1;
								//        bean.setRowId(rowId);
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
			coeusException.printStackTrace();
		}
		
		modified=false;
		close();
	} // End of saveform data
	
	
	/**
	 * To set the table editors
	 */
	private void setTableEditors(){
            awardIndirectCostForm.tblAwardIndirectCost.setRowHeight(22);
            JTableHeader tableHeader = awardIndirectCostForm.tblAwardIndirectCost.getTableHeader();
            JTableHeader header = awardIndirectCostForm.tblAwardIndirectCost.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.addMouseListener(new ColumnHeaderListener());
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            header.setFont(CoeusFontFactory.getLabelFont());
            awardIndirectCostForm.tblAwardIndirectCost.setSelectionBackground(Color.yellow);
            awardIndirectCostForm.tblAwardIndirectCost.setSelectionForeground(Color.black);
            if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
                awardIndirectCostForm.tblAwardIndirectCost.setShowHorizontalLines(false);
                awardIndirectCostForm.tblAwardIndirectCost.setShowVerticalLines(false);
            }
            awardIndirectCostForm.tblAwardIndirectCost.setOpaque(false);
            awardIndirectCostForm.tblAwardIndirectCost.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            awardIndirectCostForm.tblTotal.setOpaque(false);
            awardIndirectCostForm.tblTotal.setShowHorizontalLines(false);
            awardIndirectCostForm.tblTotal.setShowVerticalLines(false);
            
            
            TableColumn column =
            awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(APPLICABLE_IDC_RATE);
            column.setPreferredWidth(47);
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =
            awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(IDC_RATE_TYPECODE);
            column.setPreferredWidth(72);
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =  awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(FISCAL_YEAR );
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(75);
            column.setPreferredWidth(90);
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =  awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(START_DATE);
            column.setPreferredWidth(80);
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =  awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(END_DATE);
            column.setPreferredWidth(80);
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =
            awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(ON_OFF_CAMPUS_FLAG );
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(55);
            column.setPreferredWidth(70);
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =
            awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(UNDER_RECOVERY_OF_IDC );
            column.setPreferredWidth(103);
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =
            awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(SOURCE_ACCOUNT);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(77);
            column.setPreferredWidth(135);
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            column =
            awardIndirectCostForm.tblAwardIndirectCost.getColumnModel().getColumn(DESTINATION_ACCOUNT);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(67);
            column.setPreferredWidth(140);
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(indirectCostRenderer);
            column.setCellEditor(indirectCostEditor);
            tableHeader.setReorderingAllowed(false);
            
            //For the tblTotal
            
            column =
            awardIndirectCostForm.tblTotal.getColumnModel().getColumn(TOTAL_COLUMN);
            awardIndirectCostForm.tblTotal.setRowHeight(22);
            column.setPreferredWidth(266);
            column.setResizable(true);
            column.setCellRenderer(amountTableCellRenderer);
            tableHeader.setReorderingAllowed(false);
            
            column =
            awardIndirectCostForm.tblTotal.getColumnModel().getColumn(TOTAL_AMOUNT_COLUMN);
            column.setPreferredWidth(103);
            column.setResizable(true);
            column.setCellRenderer(amountTableCellRenderer);
            tableHeader.setReorderingAllowed(false);
            
        } // end of settableeditors
	
	/**
	 * This is an inner class specify the table model for the indirect cost
	 */
	class IndirectCostTableModel extends AbstractTableModel{
            
            // IndirectCostTableModel()
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //String colNames[] = {"Rate","Type", "Fiscal Year","Start Date","End Date","Campus","Underrecovery","Source Acct","Dest Acct"};
            String colNames[] = {"Rate","Type", "Fiscal Year","Start Date","End Date","Campus","Underrecovery","Source Account","Destination Account"};
            //Case#2402 - End
            Class[] colTypes = new Class [] {Double.class, CoeusComboBox.class , String.class, DateUtils.class, DateUtils.class,
            ComboBoxBean.class, Double.class,String.class,String.class};
            
            /**
             * This method will check whether the given field is ediatble or not
             * @param row int
             * @param col int
             * @return boolean
             */
            public boolean isCellEditable(int row, int col){
                
                if( getFunctionType() == TypeConstants.DISPLAY_MODE){
                    return false;
                }else{
                    
                    return true;
                }
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
            
            public void setData(CoeusVector cvData){
                cvTableData = cvData;
            }
            
            /**
             * To get the value depends on the row and column
             * @param rowIndex int
             * @param columnIndex int
             * @return Object
             */
            
            public Object getValueAt(int rowIndex, int columnIndex) {
                AwardIDCRateBean awardIDCRateBean = (AwardIDCRateBean)cvTableData.elementAt(rowIndex);;
                
                switch(columnIndex) {
                    case APPLICABLE_IDC_RATE:
                        return new Double(awardIDCRateBean.getApplicableIDCRate());
                    case IDC_RATE_TYPECODE:
                        int typeCode=awardIDCRateBean.getIdcRateTypeCode();
                        CoeusVector filteredVector = cvIndirect.filter(new Equals("code",""+typeCode));
                        if(filteredVector!=null && filteredVector.size() > 0){
                            ComboBoxBean comboBoxBean = null;
                            filteredVector.sort("description");
                            comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            return comboBoxBean;
                        }else{
                            return new ComboBoxBean("","");
                        }
                        
                    case FISCAL_YEAR:
                        return awardIDCRateBean.getFiscalYear();
                    case START_DATE:
                        return awardIDCRateBean.getStartDate();
                    case END_DATE:
                        return awardIDCRateBean.getEndDate();
                    case ON_OFF_CAMPUS_FLAG:
                        ComboBoxBean comboBoxBean=new ComboBoxBean();
                        if (awardIDCRateBean.isOnOffCampusFlag()) {
                            comboBoxBean.setCode("0");
                            comboBoxBean.setDescription(ON);
                            return comboBoxBean;
                        }
                        else {
                            comboBoxBean.setCode("1");
                            comboBoxBean.setDescription(OFF);
                            return comboBoxBean;
                        }
                    case UNDER_RECOVERY_OF_IDC:
                        return new Double(awardIDCRateBean.getUnderRecoveryOfIDC());
                        
                    case SOURCE_ACCOUNT:
                        return awardIDCRateBean.getSourceAccount();
                    case DESTINATION_ACCOUNT:
                        return awardIDCRateBean.getDestinationAccount();
                }
                return EMPTY_STRING;
                
                
            } // end of getValueAt()
            
            
            public void setValueAt(Object value, int row, int col){
                String startDate ;
                String endDate ;
                String message=null;
                Date date = null;
                
                if(cvTableData == null) return;
                AwardIDCRateBean awardIDCRateBean = (AwardIDCRateBean)cvTableData.get(row);;
                
                switch(col) {
                    case APPLICABLE_IDC_RATE:
                        
                        //Bug Fix:1124 Start 1
                        if(awardIDCRateBean.getApplicableIDCRate() == DEFAULT_IDC_RATE  && ZERO_ENTERED &&
                            Double.parseDouble(value.toString()) == .00) {
                            awardIDCRateBean.setApplicableIDCRate(.00);
                            
                        }
                        //Bug Fix:1124 End 1
                        
                        // Commnetd to fix bug # 1187 - Added by chandra 0n 9-Sept-2004
                        //else if(value != null&& Double.parseDouble(value.toString()) != .00) {
                          else if(value != null){//&& Double.parseDouble(value.toString()) != .00) {
                            double percentage = Double.parseDouble(value.toString());
                            if( percentage != awardIDCRateBean.getApplicableIDCRate()) {
                                awardIDCRateBean.setApplicableIDCRate(percentage);
                                modified=true;
                            }
                        }
                      break;
                    case IDC_RATE_TYPECODE:
                        // Added by chandra to fix bug #934 - start - 6-Aug-2004
                        if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                            // Added by chandra to fix bug #934 - End - 6-Aug-2004
                            ComboBoxBean comboBoxBean = (ComboBoxBean)value;//(ComboBoxBean)cvIndirect.filter(new Equals("description",
                            //value.toString())).get(0);
                            int typeCode = Integer.parseInt(comboBoxBean.getCode());
                            if( typeCode != awardIDCRateBean.getIdcRateTypeCode()){
                                awardIDCRateBean.setIdcRateTypeCode(typeCode);
                                modified = true;
                            }
                        }
                        break;
                    case FISCAL_YEAR:
                        if(value != null) {// && value.toString().length() >=4 ) {
                            //commented and added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
                            //if(awardIDCRateBean.getFiscalYear()!= null &&
                            //awardIDCRateBean.getFiscalYear().trim().equals(value.toString().trim()))break;
                            //                        if (awardIDCRateBean.getFiscalYear() != null &&
                            //							(!value.toString().trim().equals(awardIDCRateBean.getFiscalYear().trim()))) {
                            if (awardIDCRateBean.getFiscalYear() != null && ((value.toString().trim().equals(awardIDCRateBean.getFiscalYear().trim()))
                            || (!value.toString().trim().equals(awardIDCRateBean.getFiscalYear().trim())))) {
                                awardIDCRateBean.setFiscalYear(value.toString());
                                if (!value.toString().trim().equals(EMPTY_STRING)) {
                                    try{
                                        // onChange the values of start date and end date columns will be changed for each change in Fiscal year
                                        
                                        newYear = Integer.parseInt(value.toString());
                                        
                                        
                                        //If value of fiscalMonthValue is from 1 to 6 then it should take the current year as fiscal year
                                        //If it is greater than 6 then it should consider from previous year to current year as fiscal year
                                        //newYearMinus = newYear - 1;
                                        //startDate = "01-Jul-" + newYearMinus;// DD-MMM-YYYY
                                        //endDate  = "30-Jun-" + newYear;// DD-MMM-YYYY
                                        String fiscalYearStart = getFiscalYearStartParameterValue();
                                        int commaIndex = 0;
                                        int length = fiscalYearStart.length();
                                        if(length>0){
                                            for(int index=0;index<length;index++){
                                                char ch = fiscalYearStart.charAt(index);
                                                if(','==ch){
                                                    commaIndex = index;
                                                    break;
                                                }else if('/'==ch){
                                                    commaIndex = index;
                                                    break;
                                                }
                                            }
                                        }
                                        String fiscalMonthValue = fiscalYearStart.substring(0,commaIndex);
                                        String fiscalDayValue = fiscalYearStart.substring(commaIndex+1,length);
                                        int fiscalDate = Integer.parseInt(fiscalDayValue);
                                        if(Integer.parseInt(fiscalMonthValue) < 7){
                                            int maxDay = fiscalDate;
                                            if(fiscalDate == 1 ){
                                                Calendar calendar = Calendar.getInstance();
                                                int year = newYear;
                                                int month = Integer.parseInt(fiscalMonthValue) - 2;
                                                //If month value is -1 then it should assign to last month of the calendar
                                                if(month == -1){
                                                    month = 11;
                                                }
                                                int dateForCalendar = 1;
                                                calendar.set(year, month, dateForCalendar);
                                                maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                                if(month == 11){
                                                    startDate = fiscalMonthValue +"-"+ fiscalDate +"-" + newYear;// DD-MMM-YYYY
                                                    endDate  = (month+1) +"-"+ maxDay +"-"+ year;// DD-MMM-YYYY
                                                    startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , DATE_FORMAT);
                                                    endDate = dtUtils.formatDate(endDate, DATE_SEPERATOR , DATE_FORMAT);
                                                    utilStartDate =  dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                                                    utilEndDate =  dtFormat.parse(dtUtils.restoreDate(endDate, DATE_SEPARATERS));
                                                }else{
                                                    startDate = fiscalMonthValue +"-"+ fiscalDate +"-" + newYear;// DD-MMM-YYYY
                                                    year = newYear+1;
                                                    month = Integer.parseInt(fiscalMonthValue) - 2;
                                                    calendar.set(year, month, dateForCalendar);
                                                    maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                                    endDate  = (month+1) +"-"+ maxDay +"-"+ year;// DD-MMM-YYYY
                                                    startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , DATE_FORMAT);
                                                    endDate = dtUtils.formatDate(endDate, DATE_SEPERATOR , DATE_FORMAT);
                                                    utilStartDate =  dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                                                    utilEndDate =  dtFormat.parse(dtUtils.restoreDate(endDate, DATE_SEPARATERS));
                                                }
                                            }else{
                                                startDate = fiscalMonthValue +"-"+ fiscalDate +"-" + newYear;// DD-MMM-YYYY
                                                endDate  = fiscalMonthValue +"-"+ (fiscalDate -1) +"-"+(newYear+1);// DD-MMM-YYYY
                                                startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , DATE_FORMAT);
                                                endDate = dtUtils.formatDate(endDate, DATE_SEPERATOR , DATE_FORMAT);
                                                utilStartDate =  dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                                                utilEndDate =  dtFormat.parse(dtUtils.restoreDate(endDate, DATE_SEPARATERS));
                                            }
                                        }else
                                            
                                            if(fiscalDate == 1){
                                            int maxDay = fiscalDate;
                                            Calendar calendar = Calendar.getInstance();
                                            int year = newYear;
                                            int month = Integer.parseInt(fiscalMonthValue) - 2;
                                            int dateForCalendar = 1;
                                            calendar.set(year, month, dateForCalendar);
                                            maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                            startDate = fiscalMonthValue +"-"+ fiscalDate +"-" + (year-1);// DD-MMM-YYYY
                                            endDate  = (month+1) +"-"+ maxDay +"-"+ year;// DD-MMM-YYYY
                                            startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , DATE_FORMAT);
                                            endDate = dtUtils.formatDate(endDate, DATE_SEPERATOR , DATE_FORMAT);
                                            utilStartDate =  dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                                            utilEndDate =  dtFormat.parse(dtUtils.restoreDate(endDate, DATE_SEPARATERS));
                                            }else{
                                            newYearMinus = newYear - 1;
                                            startDate = fiscalMonthValue +"-"+ fiscalDate +"-" +newYearMinus;// DD-MMM-YYYY
                                            endDate  =  fiscalMonthValue +"-"+ (fiscalDate-1)+"-"+newYear;// DD-MMM-YYYY
                                            startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , DATE_FORMAT);
                                            endDate = dtUtils.formatDate(endDate, DATE_SEPERATOR , DATE_FORMAT);
                                            utilStartDate =  dtFormat.parse(dtUtils.restoreDate(startDate, DATE_SEPARATERS));
                                            utilEndDate =  dtFormat.parse(dtUtils.restoreDate(endDate, DATE_SEPARATERS));
                                            }
                                        
                                        //added for COEUSQA -1728 - end
                                        
                                    } catch (ParseException parseException) {
                                        parseException.printStackTrace();
                                        message = coeusMessageResources.parseMessageKey("Enter a Valid Fiscal Year");
                                        CoeusOptionPane.showInfoDialog(message);
                                        awardIDCRateBean.setFiscalYear(value.toString());
                                        indirectCostEditor.txtFiscalYear.requestFocus();
                                        setRequestFocusInThread(indirectCostEditor.txtFiscalYear);
                                        
                                        return ;
                                    }
                                    
                                    awardIDCRateBean.setStartDate(new java.sql.Date(utilStartDate.getTime()));
                                    awardIDCRateBean.setEndDate(new java.sql.Date(utilEndDate.getTime()));
                                    fireTableRowsUpdated(row, row);
                                    modified = true;
                                    
                                    return;
                                }
                            }
                        }
                        break;
                    case START_DATE:
                        date = null;
                        
                        if (value != null) {
                            try {
                                startDate = dtUtils.formatDate(
                                value.toString().trim(), DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                                if(startDate==null) {
                                    if( !value.toString().trim().equals(EMPTY_STRING)){
                                        message = coeusMessageResources.parseMessageKey(
                                        INVALID_START_DATE);
                                        CoeusOptionPane.showInfoDialog(message);
                                        setRequestFocusInThread(indirectCostEditor.txtComponent);
                                        return ;
                                    }else{
                                        awardIDCRateBean.setStartDate(null);
                                        modified = true;
                                    }
                                    return ;
                                }
                                
                                date =  dtFormat.parse(dateUtils.restoreDate(startDate, DATE_SEPARATERS));
                                //If value is not changed then should not set it again
                                if (new java.sql.Date(date.getTime()).equals(awardIDCRateBean.getStartDate())) {
                                    break;
                                }
                                awardIDCRateBean.setStartDate(new java.sql.Date(date.getTime()));
                                indirectCostTableModel.fireTableCellUpdated(row,col);
                                
                                modified = true;
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                                message = coeusMessageResources.parseMessageKey(
                                INVALID_START_DATE);
                                CoeusOptionPane.showInfoDialog(message);
                                
                                indirectCostEditor.txtComponent.requestFocus();
                                setRequestFocusInThread(indirectCostEditor.txtComponent);
                                return;
                                
                            }
                            //modified = true;
                        }
                        break;
                    case END_DATE:
                        Date edate = null;
                        
                        if (value != null) {
                            try{
                                endDate = dtUtils.formatDate(
                                value.toString().trim(), DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                                if(endDate==null) {
                                    
                                    if( !value.toString().trim().equals(EMPTY_STRING)){
                                        message = coeusMessageResources.parseMessageKey(
                                        INVALID_END_DATE);
                                        CoeusOptionPane.showInfoDialog(message);
                                        indirectCostEditor.txtComponent.requestFocus();
                                        setRequestFocusInThread(indirectCostEditor.txtComponent);
                                        
                                    } else {
                                        awardIDCRateBean.setEndDate(null);
                                        modified = true;
                                    }
                                    return ;
                                }
                                edate = dtFormat.parse(dateUtils.restoreDate(endDate, DATE_SEPARATERS));
                                if (new java.sql.Date(edate.getTime()).equals(awardIDCRateBean.getEndDate())) {
                                    break;
                                }
                                awardIDCRateBean.setEndDate(new java.sql.Date(edate.getTime()));
                                
                                modified = true;
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                                message = coeusMessageResources.parseMessageKey(
                                INVALID_END_DATE);
                                CoeusOptionPane.showInfoDialog(message);
                                
                                indirectCostEditor.txtComponent.requestFocus();
                                setRequestFocusInThread(indirectCostEditor.txtComponent);
                                return ;
                            }
                        }
                        
                        //  modified = true;
                        
                        break;
                    case ON_OFF_CAMPUS_FLAG:
                        if(value != null) {
                            if (ON.equals(value.toString())){
                                if (!awardIDCRateBean.isOnOffCampusFlag()) {
                                    if (true == (awardIDCRateBean.isOnOffCampusFlag())) {
                                        break;
                                    }
                                    modified = true;
                                    awardIDCRateBean.setOnOffCampusFlag(true);
                                }
                            }
                            else {
                                if (awardIDCRateBean.isOnOffCampusFlag()) {
                                    if (false == awardIDCRateBean.isOnOffCampusFlag()) {
                                        break;
                                    }
                                    awardIDCRateBean.setOnOffCampusFlag(false);
                                    modified=true;
                                }
                            }
                        }
                        
                        break;
                    case UNDER_RECOVERY_OF_IDC:
                        if(value != null) {
                            double percentage_Under = Double.parseDouble(value.toString());
                            if( percentage_Under != awardIDCRateBean.getUnderRecoveryOfIDC()) {
                                awardIDCRateBean.setUnderRecoveryOfIDC(percentage_Under);
                                modified=true;
                            }
                        }
                        amountTableModel.fireTableDataChanged();
                        break;
                    case SOURCE_ACCOUNT:
                        if(value != null) {
                            if(awardIDCRateBean.getSourceAccount()!= null &&
                            awardIDCRateBean.getSourceAccount().trim().equals(value.toString().trim()))break;
                            // if (!value.toString().trim().equals(awardIDCRateBean.getSourceAccount().trim())) {
                            awardIDCRateBean.setSourceAccount(value.toString());
                            modified = true;
                            //  }
                        }
                        break;
                    case DESTINATION_ACCOUNT:
                        if(value != null) {
                            if(awardIDCRateBean.getDestinationAccount()!= null &&
                            awardIDCRateBean.getDestinationAccount().trim().equals(value.toString().trim()))break;
                            // if (!value.toString().trim().equals(awardIDCRateBean.getDestinationAccount().trim())) {
                            awardIDCRateBean.setDestinationAccount(value.toString());
                            modified = true;
                            //  }
                        }
                        break;
                } // switch ends here.
                
                if(awardIDCRateBean.getAcType()== null){
                    awardIDCRateBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }// end of setValueAt
        } // end of IndirectCostTableModel
	
	class IndirectCostEditor extends AbstractCellEditor implements TableCellEditor {
		
		private int column;
		private boolean populated = false;
		private CoeusComboBox cmbCampus;
		private CoeusComboBox cmbRateCode;
		CurrencyField txtRate; // = new CurrencyField();
		CoeusTextField txtFiscalYear; // = new CoeusTextField();
		CoeusTextField txtComponent; //= new CoeusTextField() ;
		CoeusTextField txtCompAccount;
		DollarCurrencyTextField txtCurrencyField;// = new DollarCurrencyTextField();
                
                
                /**
		 * Default constructor for the IndirectCost Editor
		 */
		public IndirectCostEditor(){
                        txtRate = new CurrencyField();

			txtComponent = new CoeusTextField(12);
			
			cmbCampus = new CoeusComboBox();
			cmbRateCode = new CoeusComboBox();
			
			txtCurrencyField = new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			
			txtFiscalYear = new CoeusTextField();
			txtFiscalYear.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
			txtFiscalYear.setHorizontalAlignment(txtFiscalYear.RIGHT);
			
			txtCompAccount = new CoeusTextField();
                        //Modified by shiji for bug fix id 1730 - start
			txtCompAccount.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,7));
			//bug fix id 1730 - end
                        txtCompAccount.setHorizontalAlignment(txtCompAccount.RIGHT);
                        
                        //Bug Fix:1124 Start 2 
                        txtRate.addKeyListener(new KeyAdapter(){
                            public void keyPressed(KeyEvent kEvent){
                                if( kEvent.getKeyCode() == KeyEvent.VK_0 || kEvent.getKeyCode() == KeyEvent.VK_NUMPAD0 ){
                                    int selectedRow = awardIndirectCostForm.tblAwardIndirectCost.getSelectedRow();
                                    ZERO_ENTERED = true;
                                    kEvent.consume();
                                }
                            }
                        });
                        //Bug Fix:1124 End 2
                }
		
		/**
		 * To populate the combo
		 */
		private void populateCombo() {
                    // Added by chandra to fix bug #934 - start - 6-Aug-2004
                     ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
                     cvIndirect.add(0, emptyBean);
                     // Added by chandra to fix bug #934 - end - 6-Aug-2004
			if( cvIndirect != null && cvIndirect.size() > 0 ) {
				cvIndirect.sort("description", true);
				cmbRateCode.setModel( new DefaultComboBoxModel(cvIndirect));
			}
			
		} // end of populateCombo
    
		/**
		 * populate the campus flag combo
		 */
		private void populateCampusCombo() {
			ComboBoxBean comboBoxBean=new ComboBoxBean();
			
			comboBoxBean.setCode("0");
			comboBoxBean.setDescription(ON);
			cmbCampus.addItem(comboBoxBean);
			comboBoxBean =null;
			comboBoxBean =new ComboBoxBean();
			comboBoxBean.setCode("1");
			comboBoxBean.setDescription(OFF);
			cmbCampus.addItem(comboBoxBean);
		}
		
		/**
		 * To get the editored value
		 * @return Object
		 */
		public Object getCellEditorValue() {
			switch(column){
				case APPLICABLE_IDC_RATE:
					return txtRate.getText();
				case IDC_RATE_TYPECODE :
					return cmbRateCode.getSelectedItem();
				case FISCAL_YEAR:
					return txtFiscalYear.getText();
				case START_DATE:
					return txtComponent.getText();
				case END_DATE:
					return txtComponent.getText();
				case ON_OFF_CAMPUS_FLAG:
					return cmbCampus.getSelectedItem();
				case UNDER_RECOVERY_OF_IDC:
					return txtCurrencyField.getValue();
				case SOURCE_ACCOUNT:
					return txtCompAccount.getText();
				case DESTINATION_ACCOUNT:
					return txtCompAccount.getText();
					
			}
			return txtComponent;
			
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int
		column) {
			this.column = column;
			txtComponent.setHorizontalAlignment(txtComponent.RIGHT);
			switch(column){
				
				case APPLICABLE_IDC_RATE:
					if(value == null){
						txtRate.setText("0.00");
					}else{
                                                //Bug Fix:1124 Start 3
                                                if(((Double)value).doubleValue() == DEFAULT_IDC_RATE){
                                                    txtRate.setText(".00");
                                                }
                                                //Bug Fix:1124 End 3 
                                                else{
                                                    txtRate.setText(""+ Double.parseDouble(value.toString()));
                                                }
					}
					return txtRate;
					
				case IDC_RATE_TYPECODE:
					
					if(!populated) {
						populateCombo();
						populated = true;
						
					}
					cmbRateCode.setSelectedItem(value);
					return cmbRateCode;
				case FISCAL_YEAR:
					if(value == null){
						txtFiscalYear.setText(EMPTY_STRING);
					}else{
						txtFiscalYear.setText(value.toString());
					}
					return txtFiscalYear;
					
				case START_DATE:
					
					if(value == null){
						txtComponent.setText(EMPTY_STRING);
					}else{
						txtComponent.setText(dtUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT));
					}
					return txtComponent;
				case END_DATE:
					
					if(value == null){
						txtComponent.setText(EMPTY_STRING);
					}else{
						
						txtComponent.setText(dtUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT));
						
					}
					return txtComponent;
					
				case ON_OFF_CAMPUS_FLAG:
					if(! campusComboPopulated) {
						populateCampusCombo();
						campusComboPopulated = true;
					}
					cmbCampus.setSelectedItem(value);
					if (isSelected) {
						cmbCampus.setBackground(Color.yellow);
					}
					else {
						cmbCampus.setBackground(Color.white);
					}
					return cmbCampus;
				case UNDER_RECOVERY_OF_IDC:
					
					if(value == null){
						txtCurrencyField.setValue(0.00);
						
					}else{
						txtCurrencyField.setValue(Double.parseDouble(value.toString()));
					}
					return txtCurrencyField;
					
				case SOURCE_ACCOUNT:
                    //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                  if(value == null){
//	                  txtCompAccount.setText(EMPTY_STRING);
//                  }else{
//                     txtCompAccount.setText(value.toString());
//                  }
                    //Sets the SourceAccountNumber length based on accountNumberMaxLength value and allow the field to
                    //accept alphanumeric with comma,hyphen and periods
                    txtCompAccount.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                    String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING  : value.toString().trim();
                    //Checks if sourceAccountNumber is greater than account number length from parameter,
                    //then sourceAccountNumber is substring to accountNumberMaxLength
                    if(sourceAccountNumber.length() > accountNumberMaxLength){
						sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                    }
                    txtCompAccount.setText(sourceAccountNumber);
                    //Case#2402 - End
                                        
					return txtCompAccount;
					
				case DESTINATION_ACCOUNT:
                    //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                  if(value == null){
//						txtCompAccount.setText(EMPTY_STRING);
//					}else{
//						txtCompAccount.setText(value.toString());
//					}

                    //Sets the destinationAccountNumber length based on accountNumberMaxLength value and allow the field to
                    //accept alphanumeric with comma,hyphen and periods
                    txtCompAccount.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));				
                    String destinationAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING  : value.toString().trim();
                    //Checks if destinationAccountNumber is greater than account number length from parameter,
                    //then destinationAccountNumber is substring to accountNumberMaxLength
                    if(destinationAccountNumber.length() > accountNumberMaxLength){
						destinationAccountNumber = destinationAccountNumber.substring(0,accountNumberMaxLength);
                    }
                    txtCompAccount.setText(destinationAccountNumber);
                    //Case#2402 - End
                                        
					return txtCompAccount;
					
			}
			
			return txtComponent;
			
		} // end of getTableCellEditorComponent
		
	}//End IndirectCostEditor
	
	class IndirectCostRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
		private CurrencyField txtRate;
		private CoeusTextField txtComponent;
		private JLabel lblValue;
		
		public IndirectCostRenderer(){
			txtRate = new CurrencyField();
			txtRate.setBorder(new EmptyBorder(0,0,0,0));
			txtComponent = new CoeusTextField(12);
			txtComponent.setBorder(new EmptyBorder(0,0,0,0));
			txtComponent.setHorizontalAlignment(txtComponent.RIGHT);
			txtCurrencyField=new DollarCurrencyTextField(12,CoeusTextField.RIGHT,true);
			txtCurrencyField.setHorizontalAlignment(txtCurrencyField.RIGHT);
			txtCurrencyField.setBorder(new EmptyBorder(0,0,0,0));
			
			lblValue = new JLabel();
			lblValue.setOpaque(true);
			lblValue.setBorder(new EmptyBorder(0,0,0,0));
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			switch(col){
				case APPLICABLE_IDC_RATE :
					if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtRate.setBackground(disabledBackground);
						txtRate.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtRate.setBackground(Color.YELLOW);
						txtRate.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtRate.setBackground(Color.white);
						txtRate.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
					if(value != null) {
                                            //Bug Fix:1124 Start 4
                                            if(((Double)value).doubleValue() == DEFAULT_IDC_RATE){        
                                                String strZero = ".00";
						txtRate.setText(strZero);
						lblValue.setText(strZero);
                                                
                                            }//Bug Fix:1124 End 4 
                                            
                                            else{
                                                txtRate.setText(value.toString());
						lblValue.setText(value.toString());
                                            }
					} else {
						txtRate.setText(EMPTY_STRING);
						lblValue.setText(EMPTY_STRING);
					}
					//lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					//                    if (isSelected)
					//                        txtRate.setBackground(java.awt.Color.yellow);
					//                    else
					//                        txtRate.setBackground(java.awt.Color.white);
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					return lblValue;
				case IDC_RATE_TYPECODE :
					
					if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						cmbRateCode.setBackground(disabledBackground);
						cmbRateCode.setForeground(Color.BLACK);
					}else if(isSelected){
						cmbRateCode.setBackground(Color.YELLOW);
						cmbRateCode.setForeground(Color.black);
					}else{
						cmbRateCode.setBackground(Color.white);
						cmbRateCode.setForeground(Color.black);
					}
					
				case FISCAL_YEAR :
					
					if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtComponent.setBackground(disabledBackground);
						txtComponent.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtComponent.setBackground(Color.YELLOW);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtComponent.setBackground(Color.white);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
					
					if(value != null && !value.toString().trim().equals(EMPTY_STRING)){
						txtComponent.setText(value.toString());
						lblValue.setText(value.toString());
					}else{
						txtComponent.setText(EMPTY_STRING);
						lblValue.setText(EMPTY_STRING);
					}
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					return lblValue;
					
				case START_DATE:
					
					if(value != null && !value.toString().trim().equals(EMPTY_STRING)) {
						value = dtUtils.formatDate(value.toString(), REQUIRED_DATEFORMAT);
						txtComponent.setText(value.toString());
						lblValue.setText(value.toString());
					} else {
						txtComponent.setText(EMPTY_STRING);
						lblValue.setText(EMPTY_STRING);
					}
					
					if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtComponent.setBackground(disabledBackground);
						txtComponent.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtComponent.setBackground(Color.YELLOW);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtComponent.setBackground(Color.white);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					return lblValue;
				case END_DATE  :
					
					if(value != null && !value.toString().trim().equals(EMPTY_STRING)) {
						value = dtUtils.formatDate(value.toString(), REQUIRED_DATEFORMAT);
						txtComponent.setText(value.toString());
						lblValue.setText(value.toString());
						
					} else {
						txtComponent.setText(EMPTY_STRING);
						lblValue.setText(EMPTY_STRING);
					}
					
					if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtComponent.setBackground(disabledBackground);
						txtComponent.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtComponent.setBackground(Color.YELLOW);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtComponent.setBackground(Color.white);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					return lblValue;
					
					
				case ON_OFF_CAMPUS_FLAG :
					if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtComponent.setBackground(disabledBackground);
						txtComponent.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtComponent.setBackground(Color.YELLOW);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtComponent.setBackground(Color.white);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
					if(value == null || value.toString().trim().equals(EMPTY_STRING)){
						txtComponent.setText(EMPTY_STRING);
						lblValue.setText(EMPTY_STRING);
					}else{
						txtComponent.setText(value.toString());
						lblValue.setText(value.toString());
					}
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					return lblValue;
					
				case UNDER_RECOVERY_OF_IDC :
					if(value!= null) {
						txtCurrencyField.setValue(new Double(value.toString()).doubleValue());
						lblValue.setText(txtCurrencyField.getText());
					} else {
						txtCurrencyField.setValue(0.00);
						lblValue.setText(txtCurrencyField.getText());
					}
					if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtCurrencyField.setBackground(disabledBackground);
						txtCurrencyField.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtCurrencyField.setBackground(Color.YELLOW);
						txtCurrencyField.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtCurrencyField.setBackground(Color.white);
						txtCurrencyField.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					return lblValue;
				case SOURCE_ACCOUNT :
					if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtComponent.setBackground(disabledBackground);
						txtComponent.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtComponent.setBackground(Color.YELLOW);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtComponent.setBackground(Color.white);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
                    //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//					if(value != null) {
//						txtComponent.setText(value.toString());
//						lblValue.setText(value.toString());
//					} else {
//
//						txtComponent.setText(EMPTY_STRING);
//						lblValue.setText(EMPTY_STRING);
//					}
                                        
                    //Sets the SourceAccountNumber length based on accountNumberMaxLength value and allow the field to
                    //accept alphanumeric with comma,hyphen and periods
                    txtComponent.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                    String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING  : value.toString().trim();
                     //Checks if sourceAccountNumber is greater than account number length from parameter, 
                     //then sourceAccountNumber is substring to accountNumberMaxLength
                     if(sourceAccountNumber.length() > accountNumberMaxLength){
						sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                     }
                     txtComponent.setText(sourceAccountNumber);
                     lblValue.setText(sourceAccountNumber);
                     //Case#2402 - End
                                        
					//                    if (isSelected)
					//                        txtComponent.setBackground(java.awt.Color.yellow);
					//                    else
					//                        txtComponent.setBackground(java.awt.Color.white);
					
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					return lblValue;
					
				case DESTINATION_ACCOUNT  :
					if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
						txtComponent.setBackground(disabledBackground);
						txtComponent.setForeground(Color.BLACK);
						lblValue.setBackground(disabledBackground);
						lblValue.setForeground(Color.BLACK);
					}else if(isSelected){
						txtComponent.setBackground(Color.YELLOW);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.YELLOW);
						lblValue.setForeground(Color.black);
					}else{
						txtComponent.setBackground(Color.white);
						txtComponent.setForeground(Color.black);
						lblValue.setBackground(Color.white);
						lblValue.setForeground(Color.black);
					}
                                        
                    //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//					if(value != null) {
//						txtComponent.setText(value.toString());
//						lblValue.setText(value.toString());
//					} else {
//
//						txtComponent.setText(EMPTY_STRING);
//						lblValue.setText(EMPTY_STRING);
//					}
                                        
                    //Sets the destinationAccountNumber length based on accountNumberMaxLength value and allow the field to
                    //accept alphanumeric with comma,hyphen and periods
                    txtComponent.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                    String destinationAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING  : value.toString().trim();
                    //Checks if destinationAccountNumber is greater than account number length from parameter,
                    //then destinationAccountNumber is substring to accountNumberMaxLength
                    if(destinationAccountNumber.length() > accountNumberMaxLength){
                        destinationAccountNumber = destinationAccountNumber.substring(0,accountNumberMaxLength);
                    }
                    txtComponent.setText(destinationAccountNumber);
                    lblValue.setText(destinationAccountNumber);
                    //Case#2402 - End
                                        
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
					return lblValue;
					
			}
			
			return lblValue;
			
		} // End of  getTableCellRendererComponent
		
	}
	
	/**
	 * This class will specify the model for AmountTable
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
		 * @param cvTableData CoeusVector
		 */
		public void setData(CoeusVector cvTableData){
			cvTableData = cvTableData;
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
			if(col==TOTAL_COLUMN){
				return name;
			}
			if(col==TOTAL_AMOUNT_COLUMN){
				totalAmount = cvTableData.sum("underRecoveryOfIDC");
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
		private CoeusTextField txtComponent;
		private DollarCurrencyTextField txtAmount;
		private JLabel lblValue;
		/**
		 * Default constructor for the renderer class
		 */
		public AmountTableCellRenderer(){
			txtComponent = new CoeusTextField();
			txtAmount = new DollarCurrencyTextField();
			txtComponent.setBackground((java.awt.Color)
			javax.swing.UIManager.getDefaults().get("Panel.background"));
			txtComponent.setHorizontalAlignment(txtComponent.RIGHT);
			
			txtComponent.setForeground(Color.BLACK);
			txtComponent.setFont(CoeusFontFactory.getLabelFont());
			txtComponent.setBorder(new EmptyBorder(0,0,0,0));
			txtAmount.setBackground(
			(java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
			txtAmount.setForeground(Color.BLACK);
			txtAmount.setBorder(new EmptyBorder(0,0,0,0));
			
			lblValue = new JLabel();
			lblValue.setOpaque(true);
			lblValue.setBorder(new EmptyBorder(0,0,0,0));
			
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
			
			
			switch(col){
				case TOTAL_COLUMN:
					if(value == null || value.toString().trim().equals(EMPTY_STRING)){
						txtComponent.setText(EMPTY_STRING);
						lblValue.setText(EMPTY_STRING);
					}else{
						txtComponent.setText(value.toString());
						lblValue.setText(value.toString());
					}
					lblValue.setFont(CoeusFontFactory.getLabelFont());
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					return lblValue;
					
				case TOTAL_AMOUNT_COLUMN:
					if(value == null || value.toString().trim().equals(EMPTY_STRING)){
						txtAmount.setText(EMPTY_STRING);
						lblValue.setText(EMPTY_STRING);
					}else{
						txtAmount.setValue(new Double(value.toString()).doubleValue());
						lblValue.setText(txtAmount.getText());
					}
					lblValue.setFont(CoeusFontFactory.getNormalFont());
					lblValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
					return lblValue;
			}
			return lblValue;
		}
		
	} // end of AmountTableCellRenderer
	
	
	/**
	 * To set the default focus for the component
	 */
	private void requestDefaultFocus() {
		if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
			Color bgColor = (Color)UIManager.getDefaults().get("Panel.background");
			awardIndirectCostForm.tblTotal.setBackground(bgColor);
			awardIndirectCostForm.tblTotal.setSelectionBackground(bgColor);
			
			awardIndirectCostForm.btnCancel.requestFocus();
		} else if (awardIndirectCostForm.tblAwardIndirectCost.getRowCount() > 1) {
			awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(0,0);
			awardIndirectCostForm.tblAwardIndirectCost.setColumnSelectionInterval(0,0);
			awardIndirectCostForm.tblAwardIndirectCost.editCellAt(0,0);
			awardIndirectCostForm.tblAwardIndirectCost.getEditorComponent().requestFocusInWindow();
		} else{
			awardIndirectCostForm.btnOk.requestFocus();
		}
	}
	
	
	/**
	 * This method will specify the actions performed
	 * @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//  indirectCostEditor.stopCellEditing();
		if(source.equals(awardIndirectCostForm.btnAdd)){
			performAddAction();
		}else if(source.equals(awardIndirectCostForm.btnDelete)){
			performDeleteRowAction();
		}else if(source.equals(awardIndirectCostForm.btnOk)){
			try {
				if (validate()) {
					saveFormData();
				} else {
					return;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else if(source.equals(awardIndirectCostForm.btnCancel)){
			performCancelAction();
		}
		else if(source.equals(awardIndirectCostForm.btnRates)){
			showValidIDCRates();
		}
		
	}  // end of actionPerformed
	
	/**
	 * This method will show the valid IDC Rates
	 */
	private void showValidIDCRates(){
		ValidIDCRatesPopUpController validIDCRatesPopUpController = new ValidIDCRatesPopUpController(
		awardBaseBean);
		validIDCRatesPopUpController.display();
		
	}
	
	/**
	 * Closing the dialog window
	 */
	public void close(){
		dlgIndirectCost.setVisible(false);
	}
	// Adding for addition and deletion
	
	/**
	 * This method will specify the actions during Add
	 */
	private void performAddAction() {
		
		double cost = 0.0;
		if(cvTableData != null && cvTableData.size() > 0){
			
			indirectCostEditor.stopCellEditing();
		}
		
		AwardIDCRateBean newBean = new AwardIDCRateBean();
		// AwardBaseBean awardBaseBean;
		//newBean.setApplicableIDCRate(0.00);
                
                
                //Bug Fix :1124   START 5
                newBean.setApplicableIDCRate(DEFAULT_IDC_RATE);
                //Bug Fix :1124  END 5
                
		newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
		newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
		newBean.setFiscalYear(EMPTY_STRING);
		newBean.setStartDate(null);
		newBean.setEndDate(null);
		newBean.setUnderRecoveryOfIDC(0.00);
		newBean.setSourceAccount(EMPTY_STRING);
		newBean.setDestinationAccount(EMPTY_STRING);
		rowId = rowId + 1;
		newBean.setRowId(rowId);
		newBean.setAcType(TypeConstants.INSERT_RECORD);
		
		modified = true;
		cvTableData.add(newBean);
		
		indirectCostTableModel.fireTableRowsInserted(indirectCostTableModel.getRowCount(),
		indirectCostTableModel.getRowCount());
		
		int lastRow = awardIndirectCostForm.tblAwardIndirectCost.getRowCount()-1;
		if(lastRow >= 0){
			awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(lastRow,lastRow);
			awardIndirectCostForm.tblAwardIndirectCost.setColumnSelectionInterval(0,0);
			awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
			awardIndirectCostForm.tblAwardIndirectCost.getCellRect(lastRow, PERCENTAGE, true));
			setRequestFocusInThread(indirectCostEditor.txtRate);
		}
		awardIndirectCostForm.tblAwardIndirectCost.editCellAt(lastRow,PERCENTAGE);
		setRequestFocusInThread(indirectCostEditor.txtRate);
		
		if(awardIndirectCostForm.tblAwardIndirectCost.getRowCount() == 0){
			awardIndirectCostForm.tblTotal.setVisible(false);
		}else{
			awardIndirectCostForm.tblTotal.setVisible(true);
		}
		//
	} // End of PerformAddAction
	
	/** To Handle Delete button action
	 */
	private void performDeleteRowAction(){
		
		indirectCostEditor.stopCellEditing();
		int rowIndex = awardIndirectCostForm.tblAwardIndirectCost.getSelectedRow();
		if (rowIndex==-1) {
			CoeusOptionPane.showErrorDialog(SELECT_A_ROW);
			return;
		}
		if(rowIndex != -1 && rowIndex >= 0){
			String mesg = DELETE_CONFIRMATION;
			int selectedOption = CoeusOptionPane.showQuestionDialog(
			DELETE_CONFIRMATION,
			CoeusOptionPane.OPTION_YES_NO,
			CoeusOptionPane.DEFAULT_YES);
			if(selectedOption == CoeusOptionPane.SELECTION_YES) {
				AwardIDCRateBean deletedIDCRateBean = (AwardIDCRateBean)cvTableData.get(rowIndex);
				deletedIDCRateBean.setAcType(TypeConstants.DELETE_RECORD);
				cvDeletedData.add(deletedIDCRateBean);
				//                if (deletedIDCRateBean.getAcType() == null ||
				//                deletedIDCRateBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
				//                    cvDeletedData.add(deletedIDCRateBean);
				//                }
				if(cvTableData!=null && cvTableData.size() > 0){
					cvTableData.remove(rowIndex);
					indirectCostTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
					modified = true;
					
				}
				if(rowIndex >0){
					awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(
					rowIndex-1,rowIndex-1);
					awardIndirectCostForm.tblAwardIndirectCost.setColumnSelectionInterval(0,0);
					awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
					awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
					rowIndex-1 ,0, true));
					awardIndirectCostForm.tblAwardIndirectCost.editCellAt(rowIndex-1,PERCENTAGE);
					setRequestFocusInThread(indirectCostEditor.txtRate);
				}else{
					if(awardIndirectCostForm.tblAwardIndirectCost.getRowCount()>0){
						awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(0,0);
						awardIndirectCostForm.tblAwardIndirectCost.setColumnSelectionInterval(0,0);
						awardIndirectCostForm.tblAwardIndirectCost.editCellAt(0,PERCENTAGE);
						setRequestFocusInThread(indirectCostEditor.txtRate);
					}
				}
			}
		}
		if (cvTableData.size()<1) {
			awardIndirectCostForm.tblTotal.setVisible(false);
		}
	}   // end of perform deletion
	
	/**
	 * Cancel Operation
	 */
	private void performCancelAction(){
		indirectCostEditor.stopCellEditing();
		//Check for the modified and compare the value with the value in the comments field
		String commentValue = null;
		commentValue = awardIndirectCostForm.txtArComments.getText();
		if (comments == null) {
			if (commentValue != null && commentValue.length() > 0) {
				modified = true;
			}
		}
		if(modified || (comments != null && !comments.equals(
		commentValue))) {
			int option = CoeusOptionPane.showQuestionDialog(
			coeusMessageResources.parseMessageKey(SAVE_CHANGES),
			CoeusOptionPane.OPTION_YES_NO_CANCEL,
			JOptionPane.YES_OPTION);
			switch( option ) {
				case (JOptionPane.YES_OPTION ):
					// setSaveRequired(true);
					try{
						if( validate() ){
							saveFormData();
						}
					}catch (Exception exception){
						exception.printStackTrace();
					}
					break;
				case(JOptionPane.NO_OPTION ):
					close();
					break;
				default:
					break;
			}
		}else{
			close();
		}
		
	} // end of performCancelAction
	
	/**
	 * validate the form data/Form and returns true if
	 * validation is through else returns false.
	 * @return boolean
	 * @throws  CoeusUIException
	 */
	
	public boolean validate() throws CoeusUIException {
		
		indirectCostEditor.stopCellEditing();
		
		for(int index = 0;index < cvTableData.size() ;index++){
			AwardIDCRateBean rowBean = (AwardIDCRateBean)cvTableData.elementAt(index);
			
			//To check for empty fields
			
                        //Commented if for Bug Fix 1124
			//if(rowBean.getApplicableIDCRate() == 0.00 || rowBean.getApplicableIDCRate()  == .00 ){
                       
                        //Bug Fix:1124 - 6  changed only the 'if' part
                        if(rowBean.getApplicableIDCRate() == DEFAULT_IDC_RATE){
                                awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval( index, index );
				awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
				awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
				index ,0, true));
				CoeusOptionPane.showInfoDialog("Enter the applicable IDC rate");
				indirectCostEditor.txtRate.requestFocus();
				awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,0);
				setRequestFocusInThread(indirectCostEditor.txtRate);
				//				 	awardIndirectCostForm.tblAwardIndirectCost.setColumnSelectionInterval(0,0);
				//					awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,0);
				//					setRequestFocusInThread(indirectCostEditor.txtRate);
				
				return false;
			}
                        //Case #2068 Start
			//if (rowBean.getIdcRateTypeCode()==0) {
                        if (rowBean.getIdcRateTypeCode() < 0) {
                        //Case #2068 End
				
				awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval( index, index );
				awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
				awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
				index ,0, true));
				
				CoeusOptionPane.showInfoDialog("Enter the IDC rate type");
				awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,1);
				indirectCostEditor.txtRate.requestFocus();
				
				return false;
			}
			if (EMPTY_STRING.equals(rowBean.getFiscalYear())) {
				
				awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval( index, index );
				awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
				awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
				index ,0, true));
				
				CoeusOptionPane.showInfoDialog("Enter a valid fiscal year");
				awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,2);
				indirectCostEditor.txtFiscalYear.requestFocus();
				setRequestFocusInThread(indirectCostEditor.txtFiscalYear);
				
				return false;
			}
			//Validating for Fiscal Year
			try {
				if (Integer.parseInt(rowBean.getFiscalYear())<1900 || Integer.parseInt(rowBean.getFiscalYear())>2499 ) {
					
					awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval( index, index );
					awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
					awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
					index ,0, true));
				
					CoeusOptionPane.showInfoDialog("Enter a valid fiscal year");
					awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,2);
					indirectCostEditor.txtFiscalYear.requestFocus();
					setRequestFocusInThread(indirectCostEditor.txtFiscalYear);
				
					return false;
				}
			} catch (NumberFormatException numberFormatException) {
				//Invalid Fiscal year
				CoeusOptionPane.showInfoDialog("Enter a valid fiscal year");
				indirectCostEditor.txtFiscalYear.requestFocus();
				setRequestFocusInThread(indirectCostEditor.txtFiscalYear);
				return false;
			}
			if(rowBean.getStartDate() == null){
				
				awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(index,index);
				awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
				awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
				index ,0, true));
				
				CoeusOptionPane.showInfoDialog("Enter the Start Date");
				awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,3);
				indirectCostEditor.txtComponent.requestFocus();
				setRequestFocusInThread(indirectCostEditor.txtComponent);
				
				return false;
			}
			
            //Bug Fix : 1025 - START
            /*
            if(rowBean.getEndDate() == null){
				awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(index,index);
				awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
				awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
				index ,0, true));
				
				CoeusOptionPane.showInfoDialog("Enter the End Date");
				indirectCostEditor.txtComponent.requestFocus();
				awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,4);
				setRequestFocusInThread(indirectCostEditor.txtComponent);
				
				return false;
			}
			*/
            if(rowBean.getEndDate() != null) {
            //Bug Fix : 1025 - END
            
			if(rowBean.getEndDate().compareTo(rowBean.getStartDate()) <= 0 ) {
				
				CoeusOptionPane.showInfoDialog("End Date should be later than Start Date");
				setRequestFocusInThread(indirectCostEditor.txtComponent);
				return false;
			}
			
            //Bug Fix : 1025 - START
            }
            //Bug Fix : 1025 - END
            
			if (EMPTY_STRING.equals(rowBean.getSourceAccount())) {
				
				awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval( index, index );
				awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
				awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
				index ,0, true));
				
				CoeusOptionPane.showInfoDialog("Enter the source account number");
				awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,7);
				indirectCostEditor.txtCompAccount.requestFocus();
				setRequestFocusInThread(indirectCostEditor.txtCompAccount);
				
				return false;
			}
			if (EMPTY_STRING.equals(rowBean.getDestinationAccount())) {
				
				CoeusOptionPane.showInfoDialog("Enter the destination account number");
				awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval( index, index );
				awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
				awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
				index ,0, true));
				awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index,8);
				indirectCostEditor.txtCompAccount.requestFocus();
				setRequestFocusInThread(indirectCostEditor.txtCompAccount);
				
				return false;
			}
		}//end for
		
		if(cvTableData!=null && cvTableData.size() > 0) {
			if(checkForEqualSourceAndDest()) return false;
			if(checkDuplicateRow()) return false;
			if (parameterValue != null && parameterValue.trim().equals("1")) {
				if(checkPairOfOnAndOff()) return false;
				if(checkValidRates()) return false;
				if(checkPairOnAndOffValue()) return false;
				if(checkValidApplicablePair()) return false;
			}
		}
		
		//checking for comments - can't have empty spaces.
		String commnts;
		commnts = awardIndirectCostForm.txtArComments.getText();
		
		if(commnts.length() >= 1 && commnts.toString().trim().equals(EMPTY_STRING)) {
			
			CoeusOptionPane.showInfoDialog("Either remove all spaces or enter text");
			setRequestFocusInThread(awardIndirectCostForm.txtArComments);
			
			return false;
		}
		
		return true;
	} // validate method ends here.
	
	/**
	 * this method will check whether the source and destination are equal or not
	 * @ return boolean
	 **/
	private boolean checkForEqualSourceAndDest(){
		
		indirectCostEditor.stopCellEditing();
		//        Equals sourceAccountEquals,destinationAccountEquals;
		//
		//        And sourceAcctAndDestAccount;
		
		if(cvTableData!=null && cvTableData.size() > 0) {
			for(int indx = 0; indx < cvTableData.size(); indx++){
				AwardIDCRateBean awardIDCRateBean = ( AwardIDCRateBean )cvTableData.get(indx);
				
				//double sourceAccount = awardIDCRateBean.getSourceAccount();
				//double destAccount = awardIDCRateBean.getDestinationAccount();
				if (awardIDCRateBean.getSourceAccount().equals(awardIDCRateBean.getDestinationAccount())) {
					CoeusOptionPane.showInfoDialog("Source and destination accounts must differ");
					awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(indx,indx);
					awardIndirectCostForm.tblAwardIndirectCost.editCellAt(indx,7);
					indirectCostEditor.txtCompAccount.requestFocus();
					setRequestFocusInThread(indirectCostEditor.txtCompAccount);
					return true;
				}
			  /*  sourceAccountEquals = new Equals("sourceAccount", awardIDCRateBean.getSourceAccount());
				destinationAccountEquals = new Equals("destinationAccount", awardIDCRateBean.getDestinationAccount());
			   
				sourceAcctAndDestAccount = new And(sourceAccountEquals, destinationAccountEquals);
			   
				CoeusVector coeusVector;
				coeusVector = cvTableData.filter(sourceAcctAndDestAccount);
				if(coeusVector.size()==-1)return false;
				if(coeusVector!=null && coeusVector.size() >= 1) {
					if(isChecked == false) {
						CoeusOptionPane.showInfoDialog("Source and destination accounts must differ");
						awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(indx,indx);
						awardIndirectCostForm.tblAwardIndirectCost.editCellAt(indx,7);
						indirectCostEditor.txtCompAccount.requestFocus();
						setRequestFocusInThread(indirectCostEditor.txtCompAccount);
					}
					awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(indx,indx);
					awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
					awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
						 indx ,0, true));
					return true;
				}*/
			}
		}
		return false;
	}
	// end of the on and off checking
	
	
	/**
	 * if the startdate is same in more than one row,it displays the duplicate message
	 * @return boolean
	 */
	
	private boolean checkDuplicateRow(){
		
		indirectCostEditor.stopCellEditing();
		Equals idcRateEquals,idcTypeEquals,fiscalYearEquals,startDateEquals,onOffCampusFlagEquals,
		sourceAccountEquals,destinationAccountEquals ;
		
		And rateAndType, rateAndTypeAndYear,rateAndTypeAndYearAndstDat,
		rateAndTypeAndYearAndstDatAndonOff,rateAndTypeAndYearAndstDatAndonOffAndsourceAcct,
		rateAndTypeAndYearAndstDatAndonOffAndsourceAcctAndDestAccount;
		
		if(cvTableData!=null && cvTableData.size() > 0){
			for(int index = 0; index < cvTableData.size(); index++){
				AwardIDCRateBean awardIDCRateBean = ( AwardIDCRateBean )cvTableData.get(index);
				idcRateEquals = new Equals("applicableIDCRate", new Double(awardIDCRateBean.getApplicableIDCRate()));
				idcTypeEquals = new Equals("idcRateTypeCode", new Integer(awardIDCRateBean.getIdcRateTypeCode()));
				fiscalYearEquals = new Equals("fiscalYear", awardIDCRateBean.getFiscalYear());
				startDateEquals = new Equals("startDate", awardIDCRateBean.getStartDate());
				onOffCampusFlagEquals = new Equals("onOffCampusFlag",awardIDCRateBean.isOnOffCampusFlag());
				sourceAccountEquals = new Equals("sourceAccount", awardIDCRateBean.getSourceAccount());
				destinationAccountEquals = new Equals("destinationAccount", awardIDCRateBean.getDestinationAccount());
				
				rateAndType = new And(idcRateEquals, idcTypeEquals);
				rateAndTypeAndYear = new And(rateAndType, fiscalYearEquals);
				rateAndTypeAndYearAndstDat = new And(rateAndTypeAndYear,startDateEquals);
				rateAndTypeAndYearAndstDatAndonOff = new And(rateAndTypeAndYearAndstDat, onOffCampusFlagEquals);
				rateAndTypeAndYearAndstDatAndonOffAndsourceAcct = new And(rateAndTypeAndYearAndstDatAndonOff, sourceAccountEquals);
				rateAndTypeAndYearAndstDatAndonOffAndsourceAcctAndDestAccount = new And(rateAndTypeAndYearAndstDatAndonOffAndsourceAcct,destinationAccountEquals);
				
				CoeusVector coeusVector;
				coeusVector = cvTableData.filter(rateAndTypeAndYearAndstDatAndonOffAndsourceAcctAndDestAccount);
				if(coeusVector.size()==-1)return false;
				if(coeusVector!=null && coeusVector.size() > 1){
			
					CoeusOptionPane.showInfoDialog("This is a duplicate row");
					awardIndirectCostForm.tblAwardIndirectCost.editCellAt(index, 0);
					indirectCostEditor.txtRate.requestFocus();
					setRequestFocusInThread(indirectCostEditor.txtRate);
			
					awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(index,index);
					awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
					awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
					index ,0, true));
					return true;
				}
			}
		}
		
		return false;
	}
	/**
	 * Check the pair for the row is there or not
	 * @return boolean
	 */
	private boolean checkPairOfOnAndOff(){
		
		indirectCostEditor.stopCellEditing();
		Equals idcTypeEquals,fiscalYearEquals,startDateEquals,onOffCampusFlagEquals;
		
		And typeAndYear,typeAndYearAndstDat,typeAndYearAndstDatCompOfOnOfCampus ;
		
		if(cvTableData!=null && cvTableData.size() > 0){
			for(int ind = 0; ind < cvTableData.size(); ind++){
				AwardIDCRateBean awardIDCRateBean = ( AwardIDCRateBean )cvTableData.get(ind);
				idcTypeEquals = new Equals("idcRateTypeCode", new Integer(awardIDCRateBean.getIdcRateTypeCode()));
				fiscalYearEquals = new Equals("fiscalYear", awardIDCRateBean.getFiscalYear());
				startDateEquals = new Equals("startDate", awardIDCRateBean.getStartDate());
				// I have to check for the on if it is off so taking the compliment of onoffcampusflag
				if(awardIDCRateBean.isOnOffCampusFlag())
					onOffCampusFlagEquals = new Equals("onOffCampusFlag",false);
				else
					onOffCampusFlagEquals = new Equals("onOffCampusFlag",true);
				
				typeAndYear = new And(idcTypeEquals, fiscalYearEquals);
				typeAndYearAndstDat  = new And(typeAndYear, startDateEquals);
				typeAndYearAndstDatCompOfOnOfCampus =  new And(typeAndYearAndstDat, onOffCampusFlagEquals);
				
				CoeusVector coeusVector;
				coeusVector = cvTableData.filter(typeAndYearAndstDatCompOfOnOfCampus);
				if(coeusVector.size()==-1)return false;
				// I need  atleast one row which is having the compliment of the given onOffCampus
				if(coeusVector!=null && coeusVector.size() == 0){
			
					CoeusOptionPane.showInfoDialog("Both On And Off has to be there for same Rate Type , Fiscal Year & Start Date");
					awardIndirectCostForm.tblAwardIndirectCost.editCellAt(ind , 5);
					indirectCostEditor.txtComponent.requestFocus();
					setRequestFocusInThread(indirectCostEditor.txtComponent);
			
					awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(ind,ind);
					awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
					awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
					ind ,0, true));
					return true;
				}
				
			}
		}
		
		
		return false;
	}
	
	/**
	 * This method will check whether the entered one is valid one
	 * @return boolean
	 */
	public boolean checkValidRates() {
            checkOnFlag = false;
            checkOffFlag = false;
            boolean checkOnOffFlag = false;
            if(cvTableData!=null && cvTableData.size() > 0) {
                for(int ind = 0; ind < cvTableData.size(); ind++) {
                    AwardIDCRateBean awardIDCRateBean = ( AwardIDCRateBean )cvTableData.get(ind);
                    // To get the selected rates type added by Jobin
                    int typeCode=awardIDCRateBean.getIdcRateTypeCode();
                    CoeusVector filteredVector = cvIndirect.filter(new Equals("code",""+typeCode));
                    ComboBoxBean comboBoxBean = null;
                    if (filteredVector != null && filteredVector.size() > 0) {
                        comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                    }
                    // getting the name of the selected type
                    String typeDescription = comboBoxBean.getDescription();
                    if(awardIDCRateBean.isOnOffCampusFlag() ==  true) {
                        if(checkOnFlag(awardIDCRateBean)) {
                            
                            checkOnFlag = true;
                            
                        } else {
                            
                            CoeusOptionPane.showInfoDialog("The On & Off Campus rates for rate type = "+typeDescription+ ", Fiscal year = " +awardIDCRateBean.getFiscalYear() + ", Start Date = " + awardIDCRateBean.getStartDate()+ " is not a valid rate pair");
                            awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(ind,ind);
                            awardIndirectCostForm.tblAwardIndirectCost.editCellAt(ind , 0);
                            indirectCostEditor.txtRate.requestFocus();
                            setRequestFocusInThread(indirectCostEditor.txtRate);
                            
                            //awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(ind,ind);
                            awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
                            awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
                            ind ,0, true));
                            return true;
                        }
                    }
                    if (awardIDCRateBean.isOnOffCampusFlag() ==  false) {
                        
                        if(checkOffFlag(awardIDCRateBean)) {
                            
                            checkOffFlag = true;
                            
                        } else {
                            
                            CoeusOptionPane.showInfoDialog("The On & Off Campus rates for rate type = "+typeDescription+ ", Fiscal year = " +awardIDCRateBean.getFiscalYear() + ", Start Date = " + awardIDCRateBean.getStartDate()+ " is not a valid rate pair");
                            awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(ind,ind);
                            awardIndirectCostForm.tblAwardIndirectCost.editCellAt(ind , 0);
                            indirectCostEditor.txtRate.requestFocus();
                            setRequestFocusInThread(indirectCostEditor.txtRate);
                            
                            //awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(ind,ind);
                            awardIndirectCostForm.tblAwardIndirectCost.scrollRectToVisible(
                            awardIndirectCostForm.tblAwardIndirectCost.getCellRect(
                            ind ,0, true));
                            return true;
                        }
                    }
                    
                }
            }
            if(checkOnFlag && checkOffFlag)
                return false;
            else return true;
        }
	
	
	/**
	 * Check whether the flag is ON flag
	 * @param awardIDCRateBean AwardIDCRateBean
	 * @return boolean
	 */
	public boolean checkOnFlag(AwardIDCRateBean awardIDCRateBean) {
		
		if( cvValidRatesData != null) {
			double awRate = awardIDCRateBean.getApplicableIDCRate();
			
			CoeusVector cvCheckIDC = cvValidRatesData.filter(new Equals("onCampusRate",
			new Double(awRate)));
			if(cvCheckIDC != null && cvCheckIDC.size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check whether the flag is Off flag
	 * @param awardIDCRateBean AwardIDCRateBean
	 * @return boolean
	 */
	public boolean checkOffFlag(AwardIDCRateBean awardIDCRateBean) {
		
		if( cvValidRatesData1 != null) {
			double awRate = awardIDCRateBean.getApplicableIDCRate();
			CoeusVector cvCheckIDC = cvValidRatesData1.filter(new Equals("offCampusRate",
			new Double(awRate)));
			if(cvCheckIDC != null && cvCheckIDC.size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the pair On and Off values
	 * @return boolean
	 */
	public boolean checkPairOnAndOffValue() {
		indirectCostEditor.stopCellEditing();
		if( cvValidRatesData != null) {
			Equals idcTypeEquals,fiscalYearEquals,startDateEquals,onOffCampusFlagEquals;
			
			And typeAndYear,typeAndYearAndstDat,typeAndYearAndstDatCompOfOnOfCampus ;
			
			if(cvTableData!=null && cvTableData.size() > 0) {
				for(int ind = 0; ind < cvTableData.size(); ind++) {
					//getting the pair of on and off
					AwardIDCRateBean awardIDCRatePairBean = ( AwardIDCRateBean )cvTableData.get(ind);
					idcTypeEquals = new Equals("idcRateTypeCode", new Integer(awardIDCRatePairBean.getIdcRateTypeCode()));
					fiscalYearEquals = new Equals("fiscalYear", awardIDCRatePairBean.getFiscalYear());
					startDateEquals = new Equals("startDate", awardIDCRatePairBean.getStartDate());
					
					// I have to check for the on if it is off so taking the compliment of onoffcampusflag
					if(awardIDCRatePairBean.isOnOffCampusFlag())
						onOffCampusFlagEquals = new Equals("onOffCampusFlag",true);
					else
						onOffCampusFlagEquals = new Equals("onOffCampusFlag",false);
					
					typeAndYear = new And(idcTypeEquals, fiscalYearEquals);
					typeAndYearAndstDat  = new And(typeAndYear, startDateEquals);
					typeAndYearAndstDatCompOfOnOfCampus =  new And(typeAndYearAndstDat, onOffCampusFlagEquals);
					
					CoeusVector coeusVector;
					coeusVector = cvTableData.filter(typeAndYearAndstDatCompOfOnOfCampus);
					if(coeusVector != null && coeusVector.size() >0) {
						for (int index =0; index < coeusVector.size(); index++) {
							AwardIDCRateBean bean = (AwardIDCRateBean)coeusVector.get(index);
							//checking with the other pair of the same bean
							if (awardIDCRatePairBean.getApplicableIDCRate() != bean.getApplicableIDCRate()) {
								
								if (awardIDCRatePairBean.isOnOffCampusFlag()) {
									CoeusOptionPane.showInfoDialog("All On Campus rate for a rate type, fiscal year, start date combination should be the same.");
								} else {
									CoeusOptionPane.showInfoDialog("All Off Campus rate for a rate type, fiscal year, start date combination should be the same.");
								}
								awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(ind,ind);
								awardIndirectCostForm.tblAwardIndirectCost.editCellAt(ind , 0);
								indirectCostEditor.txtRate.requestFocus();
								setRequestFocusInThread(indirectCostEditor.txtRate);
								return true;
							}
						}
					}
					
				}
			}
		}
		return false;
	}
	
	/**
	 * check whether the same pair is registered with different applicable IDC rate
	 * @return boolean
	 */
	public boolean checkValidApplicablePair() {
		
		indirectCostEditor.stopCellEditing();
		
		Equals idcTypeEquals,fiscalYearEquals,startDateEquals,onOffCampusFlagEquals;
		
		And typeAndYear,typeAndYearAndstDat,typeAndYearAndstDatCompOfOnOfCampusON,typeAndYearAndstDatCompOfOnOfCampusOFF ;
		
		if(cvTableData!=null && cvTableData.size() > 0) {
			for(int ind = 0; ind < cvTableData.size(); ind++) {
				//getting the pair of on and off
				AwardIDCRateBean awardIDCRatePairBean = ( AwardIDCRateBean )cvTableData.get(ind);
				idcTypeEquals = new Equals("idcRateTypeCode", new Integer(awardIDCRatePairBean.getIdcRateTypeCode()));
				fiscalYearEquals = new Equals("fiscalYear", awardIDCRatePairBean.getFiscalYear());
				startDateEquals = new Equals("startDate", awardIDCRatePairBean.getStartDate());
				
				// I have to check for the on if it is off so taking the compliment of onoffcampusflag
				if(awardIDCRatePairBean.isOnOffCampusFlag())
					onOffCampusFlagEquals = new Equals("onOffCampusFlag",true);
				else
					onOffCampusFlagEquals = new Equals("onOffCampusFlag",false);
				
				typeAndYear = new And(idcTypeEquals, fiscalYearEquals);
				typeAndYearAndstDat  = new And(typeAndYear, startDateEquals);
				
				//typeAndYearAndstDatCompOfOnOfCampusON =  new And(typeAndYearAndstDat, true);
				//typeAndYearAndstDatCompOfOnOfCampusOFF =  new And(typeAndYearAndstDat, false);
				
				CoeusVector cvSameTypeAndYearAndstDat;
				
				cvSameTypeAndYearAndstDat = cvTableData.filter(typeAndYearAndstDat);
				CoeusVector cvONCampus = cvSameTypeAndYearAndstDat.filter(new Equals("onOffCampusFlag", true));
				CoeusVector cvOFFCampus = cvSameTypeAndYearAndstDat.filter(new Equals("onOffCampusFlag", false));
				
				AwardIDCRateBean onBean = (AwardIDCRateBean)cvONCampus.get(0);
				AwardIDCRateBean offBean = (AwardIDCRateBean)cvOFFCampus.get(0);
				
				int typeCode=awardIDCRatePairBean.getIdcRateTypeCode();
				CoeusVector filteredVector = cvIndirect.filter(new Equals("code",""+typeCode));
				ComboBoxBean comboBoxBean = null;
				if (filteredVector != null && filteredVector.size() > 0) {
					comboBoxBean = (ComboBoxBean)filteredVector.get(0);
				}
				// getting the name of the selected type
				String typeDescription = comboBoxBean.getDescription();
				boolean isRatePair = checkRatePair(onBean.getApplicableIDCRate(),offBean.getApplicableIDCRate());
				if (!isRatePair) {
					CoeusOptionPane.showInfoDialog("The On & Off Campus rates for rate type = "+typeDescription+ ", Fiscal year = " +awardIDCRatePairBean.getFiscalYear() + ", Start Date = " + awardIDCRatePairBean.getStartDate()+ " is not a valid rate pair");
					awardIndirectCostForm.tblAwardIndirectCost.setRowSelectionInterval(ind,ind);
					awardIndirectCostForm.tblAwardIndirectCost.editCellAt(ind , 0);
					indirectCostEditor.txtRate.requestFocus();
					setRequestFocusInThread(indirectCostEditor.txtRate);
					return true;
					
				}
			}
		}
		
		return false;
	}
	
	/**
	 * check whether the On rate and Off rate are of same pair as in valid rates
	 * @param onRate double
	 * @param offRate double
	 * @return boolean
	 */
	public boolean checkRatePair(double onRate,double offRate) {
		if( cvValidRatesData == null) {
			return true;
		}
		for (int index=0;index<cvValidRatesData.size();index++) {
			ValidRatesBean validRatesBean = (ValidRatesBean)cvValidRatesData.get(index);
			if ((validRatesBean.getOnCampusRate() == onRate) && (validRatesBean.getOffCampusRate() == offRate)) {
				return true;
			}
		}
		return false;
	}
	
	/** This method will provide the key travrsal for the table cells
	 * It specifies the tab and shift tab order.
	 */
	public void setTableKeyTraversal() {
		
		javax.swing.InputMap im = awardIndirectCostForm.tblAwardIndirectCost.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// Have the enter key work the same as the tab key
		KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
		KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
		
		// Override the default tab behaviour
		// Tab to the next editable cell. When no editable cells goto next cell.
		final Action oldTabAction = awardIndirectCostForm.tblAwardIndirectCost.getActionMap().get(im.get(tab));
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
		awardIndirectCostForm.tblAwardIndirectCost.getActionMap().put(im.get(tab), tabAction);
		
		// for the shift+tab action
		
		// Override the default tab behaviour
		// Tab to the previous editable cell. When no editable cells goto next cell.
		
		final Action oldTabAction1 = awardIndirectCostForm.tblAwardIndirectCost.getActionMap().get(im.get(shiftTab));
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
						column = 8;
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
		awardIndirectCostForm.tblAwardIndirectCost.getActionMap().put(im.get(shiftTab), tabAction1);
	}
	
	
	private void setRequestFocusInThread(final Component component) {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				component.requestFocusInWindow();
			}
		});
	}
	/**
	 * This method will set all the instance variable to null during garbage collection
	 */
	public void cleanUp() {
		
		utilStartDate = null;
		utilEndDate = null;
		startDate = null;
		endDate = null;
		
		txtFiscalYear = null;
		txtCompAccount = null;
		txtRate = null;
		txtComponent = null;
		txtCurrencyField = null;
		awardIndirectCostForm = null;
		dlgIndirectCost = null;
		cvTableData = null;
		cvIndirect = null;
		cvCampus = null;
		cvDataIndirect = null;
		cvDataCampus = null;
		cvInd = null;
		cvCamp = null;
		cvDeletedData = null;
		cvTempComment = null;
		cvParameters = null;
		cvCommentDescription = null;
		cvValidRatesData = null;
		cvData = null;
		cvValidRatesData1 = null;
		indirectCostTableModel = null;
		indirectCostRenderer = null;
		indirectCostEditor = null;
		amountTableModel = null;
		amountTableCellRenderer = null;
		dateUtils = null;
		cmbRateCode = null;
		cmbCamp = null;
		dtUtils = null;
		simpleDateFormat = null;
		txtAmount = null;
		awardIDCRateBean = null;
		awardBaseBean = null;
		awardDetailsBean = null;
		commentsBean = null;
		queryCommentsBean = null;
		coeusParameterBean = null;
		validRatesBean = null;
	}
        
        //added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
        /**
         *This Method gets FISCAL_YEAR_START parameter value
         *@return fiscalYearStart
         */
        private String getFiscalYearStartParameterValue(){
            String connectTo = CoeusGuiConstants.CONNECTION_URL+ SERVLET;
            char GET_FISCAL_YEAR_START = '9';
            RequesterBean request = new RequesterBean();
            request.setFunctionType(GET_FISCAL_YEAR_START);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response == null) {
                response = new ResponderBean();
                response.setResponseStatus(false);
                response.setMessage(coeusMessageResources.parseMessageKey(
                        "server_exceptionCode.1000"));
            }
            
            if (response.isSuccessfulResponse()) {
                String dataObject = (String)response.getDataObject();
                fiscalYearStart = dataObject.toString();
                
            }
            return fiscalYearStart;
        }//added for COEUSQA -1728 : parameter to define the start date of fiscal year - end
              
}                   
                      
            
