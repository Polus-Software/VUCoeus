/*
 * AddInstituteRatesController.java
 *
 * Created on August 17, 2004, 11:30 AM
 */

package edu.mit.coeus.rates.controller;

import java.awt.Component;
import java.awt.event.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.rates.gui.*;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.rates.controller.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.rates.bean.InstituteRatesBean;
import edu.mit.coeus.brokers.*;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class AddInstituteRatesController extends RatesController implements ActionListener,FocusListener {
	
	// For querying
	private QueryEngine queryEngine;
	// To check whether the data or screen is modified
	private boolean modified = false;
	
	private boolean isErrorOccured = false;
	
	// Represents the empty string
	private static final String EMPTY_STRING = "";
	//Represents the dialog box
	private CoeusDlgWindow dlgAddInstituteRates;
	
	//Represents the mdiForm
	private CoeusAppletMDIForm mdiForm;
	
	//For the table data
	private CoeusVector cvTableData;
	
	private CoeusVector cvBaseData;
	
	private static final int WIDTH = 740;
	private static final int HEIGHT = 220;
	
        // Represents the form for the budget for award screen
        private AddInstituteRatesForm addInstituteRatesForm;
        
        // Represents the table model for the ActivityType
        private ActivityTypeTableModel activityTypeTableModel;
        
        //Title for the Add Institute Rates Dialog Window
        private static final String ADD_INSTITUTE_RATES = "Add Institute Rates ";
        
        // Represents the names of the table column
        private static final int CODE_COLUMN = 0;
        private static final int DESCRIPTION_COLUMN = 1;
	
	/*Please enter a valid activity date*/
        private static final String ACTIVITY_TYPE_MSG = "Please Select an activity type";
        
        private static final String FISCAL_YEAR_MSG = "Fiscal Year Field can not be blank.";
        
        private static final String START_DATE_MSG = "Start Date field can not be blank.";
        
        private static final String RATE_FIELD_MSG = "Rate field can not be blank.";
        /*date utils*/
        private DateUtils dateUtils = new DateUtils();
        
        private static final String DATE_SEPARATERS = ":/.,|-";
        
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        
        /*the date format*/
        private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
        
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
	
	private static final String ROW_ID_FIELD = "rowId";	
	
	private InstituteRatesBean instituteRatesBean;
        
        //Bug Fix: 1742: Performance Fix - Start 1
        private ColumnHeaderListener columnHeaderListener;
        private JTableHeader tableHeader;
        //Bug Fix: 1742: Performance Fix - End 1
		
        //added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
        private String fiscalYearStart = "";
        private String fiscalDate = "";
        private int fiscalNewMonth;
        private int fiscalNewDate;
        private static final String SERVLET = "/AwardMaintenanceServlet";
        private final String DATE_SEPERATOR = "/-:,";
        private Date utilStartDate;
        private int newYearMinus;
        private SimpleDateFormat dtFormat  = new SimpleDateFormat("MM/dd/yyyy");
        //added for COEUSQA -1728 : parameter to define the start date of fiscal year - end
        
	/**
	 * this contains the Coues message resources instance for parsing the messages
	 **/
	CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
	/**
	 * Creates a new instance of AddInstituteRatesController
	 * @param instituteRatesBean InstituteRatesBean
	 * @param functionType char
	 */
	public AddInstituteRatesController(InstituteRatesBean instituteRatesBean, char functionType) {
		super(instituteRatesBean);
		coeusMessageResources = CoeusMessageResources.getInstance();
		queryEngine = QueryEngine.getInstance();
		this.instituteRatesBean = instituteRatesBean;
		registerComponents();
		formatFields();
		setFormData(instituteRatesBean);
		setFunctionType(functionType);
		setColumnData();
		postInitComponents();
	}
	
	/**
	 * Display method
	 */	
	public void display() {
		dlgAddInstituteRates.setVisible(true);
	}
	
	/**
	 * overrided method
	 */	
	public void formatFields() {
	}
	
	/**
	 * Overridden method
	 * @return java.awt.Component
	 */	
	public java.awt.Component getControlledUI() {
		return addInstituteRatesForm;
	}
	
	/**
	 * Setting up the column data
	 */
	private void setColumnData(){
                
                //Bug Fix: 1742: Performance Fix - Start 2
		//JTableHeader tableHeader = addInstituteRatesForm.tblActivityType.getTableHeader();
                //tableHeader.addMouseListener(new ColumnHeaderListener());
            
                tableHeader = addInstituteRatesForm.tblActivityType.getTableHeader();
                columnHeaderListener = new ColumnHeaderListener();
                tableHeader.addMouseListener(columnHeaderListener);
                //Bug Fix: 1742: Performance Fix - End 2
                
		tableHeader.setReorderingAllowed(false);
		tableHeader.setFont(CoeusFontFactory.getLabelFont());
		addInstituteRatesForm.tblActivityType.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

		addInstituteRatesForm.tblActivityType.setRowHeight(22);
				
		addInstituteRatesForm.tblActivityType.setOpaque(false);
		TableColumn column = addInstituteRatesForm.tblActivityType.getColumnModel().getColumn(CODE_COLUMN);
		
		column.setPreferredWidth(40);
		column.setResizable(true);
						
		column = addInstituteRatesForm.tblActivityType.getColumnModel().getColumn(DESCRIPTION_COLUMN);
		column.setPreferredWidth(190);
		column.setResizable(true);
				
	}
	
	/**
	 * Specifies the Modal window
	 */
	private void postInitComponents() {
		mdiForm = CoeusGuiConstants.getMDIForm();
		dlgAddInstituteRates = new CoeusDlgWindow(mdiForm);
		dlgAddInstituteRates.getContentPane().add(addInstituteRatesForm);
		dlgAddInstituteRates.setTitle(ADD_INSTITUTE_RATES);
		dlgAddInstituteRates.setFont(CoeusFontFactory.getLabelFont());
		dlgAddInstituteRates.setModal(true);
		dlgAddInstituteRates.setResizable(false);
		dlgAddInstituteRates.setSize(WIDTH,HEIGHT);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = dlgAddInstituteRates.getSize();
		dlgAddInstituteRates.setLocation(screenSize.width/2 - (dlgSize.width/2),
		screenSize.height/2 - (dlgSize.height/2));
		
		dlgAddInstituteRates.addEscapeKeyListener(
		new AbstractAction("escPressed"){
			public void actionPerformed(ActionEvent ae) {
				performCancelAction();
				return;
			}
		});
		dlgAddInstituteRates.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
		dlgAddInstituteRates.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				performCancelAction();
				return;
			}
		});
		
		dlgAddInstituteRates.addComponentListener(
		new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				requestDefaultFocus();
			}
		});
	}
	
	private void requestDefaultFocus() {
		addInstituteRatesForm.txtFiscalYear.requestFocusInWindow();
	}
	
	/**
	 * Confirm before closing the BudgetPersons dialog box
	 */
	private void confirmClosing(){
		try{
			int option = CoeusOptionPane.showQuestionDialog(
			coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
			CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
			if(option == CoeusOptionPane.SELECTION_YES){
				if(validate()) {
					saveFormData();
					if (!isErrorOccured) {
						closeDialog();
					}
				} else {
					return;
				}
			} else if(option == CoeusOptionPane.SELECTION_NO) {
				dlgAddInstituteRates.setVisible(false);
			} else if(option == CoeusOptionPane.SELECTION_CANCEL) {
				return;
			}
		} catch (Exception exception) {
			exception.getMessage();
		}
	}
	
	
	/** this method Closes this window
	 */
	private void closeDialog() {
		dlgAddInstituteRates.setVisible(false);
	}
	
	/**
	 * To get the form data..Overridden method
	 * @return Object
	 */	
	public Object getFormData() {
		return null;
	}
	
	/**
	 * Regidtering all the compoenets
	 */	
	public void registerComponents() {
		addInstituteRatesForm = new AddInstituteRatesForm();
		/** Code for focus traversal - start */
        java.awt.Component[] components = { addInstituteRatesForm.txtFiscalYear,
        addInstituteRatesForm.txtStartDate,addInstituteRatesForm.rdBtnOn,addInstituteRatesForm.rdBtnOff,
        addInstituteRatesForm.rdBtnBoth,addInstituteRatesForm.txtRates,addInstituteRatesForm.btnOK,
		addInstituteRatesForm.btnCancel
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        addInstituteRatesForm.setFocusTraversalPolicy(traversePolicy);
        addInstituteRatesForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
		activityTypeTableModel = new ActivityTypeTableModel();
		addInstituteRatesForm.tblActivityType.setModel(activityTypeTableModel);
		addInstituteRatesForm.btnOK.addActionListener(this);
		addInstituteRatesForm.btnCancel.addActionListener(this);
		addInstituteRatesForm.txtStartDate.addFocusListener(this);
		
		addInstituteRatesForm.txtFiscalYear.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
		addInstituteRatesForm.txtStartDate.setDocument(new LimitedPlainDocument(11));
		
		addInstituteRatesForm.txtRateClass.setEnabled(false);						
		addInstituteRatesForm.txtRateType.setEnabled(false);
                addInstituteRatesForm.txtRateType.setBackground(Color.WHITE);
		addInstituteRatesForm.rdBtnOn.setSelected(true);			
		
	}
	
	/**
	 * Saving the form data and inserting into the query engine
	 * @throws CoeusException Throwing the coeusException
	 */	
	public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
		DateUtils dtUtils =new DateUtils();
		java.util.Date date;
		//Bug Fix: 1742: Performance Fix - Start 3
                boolean inside = false;
                //Bug Fix: 1742: Performance Fix - End 3
		try {
			dlgAddInstituteRates.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			InstituteRatesBean newInstituteRatesBean = null;
			InstituteRatesBean newOffInstituteRatesBean = null;
			int selRow [] = addInstituteRatesForm.tblActivityType.getSelectedRows();
			if (selRow.length == 0) return;
			for (int index = 0; index < selRow.length; index++) {
				newInstituteRatesBean = new InstituteRatesBean();
				ComboBoxBean bean = (ComboBoxBean)cvTableData.get(selRow[index]);
				int code = Integer.parseInt(bean.getCode());
				String description = bean.getDescription();
				newInstituteRatesBean.setActivityCode(code);
				newInstituteRatesBean.setActivityTypeDescription(description);

				/*setting the values for fiscal year*/
				String fiscalYear = addInstituteRatesForm.txtFiscalYear.getText().trim();
				newInstituteRatesBean.setFiscalYear(fiscalYear);
                                				
				/*setting the values for start date*/
				String startDate; 
                                //Commented and added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
                                //Method getStartDateForFiscalYear gives an startDate for the entered fiscal year.
                                //and It takes monthvalue and date from the parameter FISCAL_YEAR_START 
                                String fiscalStartDate = "";
                                if (addInstituteRatesForm.txtStartDate.getText().equals(EMPTY_STRING))
                                    { fiscalStartDate = getStartDateForFiscalYear(fiscalYear); }
                                    else
                                    { fiscalStartDate = addInstituteRatesForm.txtStartDate.getText(); }
                                utilStartDate =  dtFormat.parse(dtUtils.restoreDate(fiscalStartDate, DATE_SEPARATERS));
                                newInstituteRatesBean.setStartDate(new java.sql.Date(utilStartDate.getTime()));
                                //if (!EMPTY_STRING.equals(startDate)) {
                                //	String dateValue =  dtUtils.formatDate(startDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                                //	if(dateValue== null){
                                //		dateValue =dtUtils.restoreDate(startDate, DATE_SEPARATERS);
                                //		if( dateValue == null || dateValue.equals(startDate)) {
                                //			newInstituteRatesBean.setAcType(TypeConstants.INSERT_RECORD);
                                //		} else {
                                
                                //			date = simpleDateFormat.parse(dtUtils.restoreDate(startDate,DATE_SEPARATERS));
                                //			newInstituteRatesBean.setStartDate(new java.sql.Date(date.getTime()));
                                //		}
                                //	} else {
                                //		date = simpleDateFormat.parse(dtUtils.formatDate(startDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                                //		newInstituteRatesBean.setStartDate(new java.sql.Date(date.getTime()));
                                //	}
                                //}
                                
                                //Commented and added for COEUSQA -1728 : parameter to define the start date of fiscal year - end
                                                                   
				/*setting the values for rates*/
                                //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - start
                                //Set the value of rate as 0 if the rate entered is empty string
                                String rateValue = addInstituteRatesForm.txtRates.getText();
                                if(rateValue == null || rateValue.equals(EMPTY_STRING)){
                                    newInstituteRatesBean.setRate(0.0);
                                }else{
                                    double rate = new Double(addInstituteRatesForm.txtRates.getText()).doubleValue();
                                    newInstituteRatesBean.setRate(rate);
                                }
				//Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - end
                                
				// setting the rate class code and rate type code
				
				newInstituteRatesBean.setRateClassCode(instituteRatesBean.getRateClassCode());
				newInstituteRatesBean.setRateTypeCode(instituteRatesBean.getRateTypeCode());
				newInstituteRatesBean.setUnitNumber(instituteRatesBean.getUnitNumber());
								
				boolean isOnFlag = addInstituteRatesForm.rdBtnOn.isSelected();
				boolean isOffFlag = addInstituteRatesForm.rdBtnOff.isSelected();
				boolean isBothFlag = addInstituteRatesForm.rdBtnBoth.isSelected();
				if (isOnFlag) {
					newInstituteRatesBean.setOnOffCampusFlag(true);
					if (!isDuplicateRow(newInstituteRatesBean)) {
						newInstituteRatesBean.setRowId(getMaxRowId());
						newInstituteRatesBean.setAcType(TypeConstants.INSERT_RECORD);
						queryEngine.insert(queryKey,newInstituteRatesBean);
						
                                                //Bug Fix: 1742: Performance Fix - Start 4
                                                inside = true;
                                                /*BeanEvent beanEvent = new BeanEvent();
						beanEvent.setBean(new InstituteRatesBean());
						beanEvent.setSource(this);
						fireBeanUpdated(beanEvent);*/
                                                //Bug Fix: 1742: Performance Fix - End 4
					}
				} else if (isOffFlag) {
					newInstituteRatesBean.setOnOffCampusFlag(false);
					if (!isDuplicateRow(newInstituteRatesBean)) {
						newInstituteRatesBean.setRowId(getMaxRowId());
						newInstituteRatesBean.setAcType(TypeConstants.INSERT_RECORD);
						queryEngine.insert(queryKey,newInstituteRatesBean);
						
                                                //Bug Fix: 1742: Performance Fix - Start 5
                                                inside = true;
                                                /*BeanEvent beanEvent = new BeanEvent();
						beanEvent.setBean(new InstituteRatesBean());
						beanEvent.setSource(this);
						fireBeanUpdated(beanEvent);*/
                                                //Bug Fix: 1742: Performance Fix - End 5
					}
				} else if (isBothFlag) {
					newInstituteRatesBean.setOnOffCampusFlag(true);
					if (!isDuplicateRow(newInstituteRatesBean)) {
						newInstituteRatesBean.setRowId(getMaxRowId());
						newInstituteRatesBean.setAcType(TypeConstants.INSERT_RECORD);
						queryEngine.insert(queryKey,newInstituteRatesBean);
						newOffInstituteRatesBean = new InstituteRatesBean();
						newOffInstituteRatesBean.setOnOffCampusFlag(false);
						newOffInstituteRatesBean.setRateClassCode(instituteRatesBean.getRateClassCode());
						newOffInstituteRatesBean.setRateTypeCode(instituteRatesBean.getRateTypeCode());
						newOffInstituteRatesBean.setActivityCode(newInstituteRatesBean.getActivityCode());
						newOffInstituteRatesBean.setActivityTypeDescription(newInstituteRatesBean.getActivityTypeDescription());
						newOffInstituteRatesBean.setFiscalYear(newInstituteRatesBean.getFiscalYear());
						newOffInstituteRatesBean.setStartDate(newInstituteRatesBean.getStartDate());
						newOffInstituteRatesBean.setRate(newInstituteRatesBean.getRate());
						newOffInstituteRatesBean.setUnitNumber(instituteRatesBean.getUnitNumber());
						newOffInstituteRatesBean.setRowId(getMaxRowId());
						newOffInstituteRatesBean.setAcType(TypeConstants.INSERT_RECORD);
						queryEngine.insert(queryKey,newOffInstituteRatesBean);

                                                //Bug Fix: 1742: Performance Fix - Start 6
                                                inside = true;
                                                /*BeanEvent beanEvent = new BeanEvent();
						beanEvent.setBean(new InstituteRatesBean());
						beanEvent.setSource(this);
						fireBeanUpdated(beanEvent);*/
                                                //Bug Fix: 1742: Performance Fix - End 6
					}
				}
			}
                        //Bug Fix: 1742: Performance Fix - Start 7
                        if(inside){
                            BeanEvent beanEvent = new BeanEvent();
                            beanEvent.setBean(new InstituteRatesBean());
                            beanEvent.setSource(this);
                            fireBeanUpdated(beanEvent);
                        }
			//Bug Fix: 1742: Performance Fix - End 7
		} catch (ParseException parseException) {
			parseException.printStackTrace();
		} finally{
			dlgAddInstituteRates.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ));
                        
		}
	}
	
	/**
	 * if the startdate is same in more than one row,it displays the duplicate message
	 * @return boolean
	 */
	
	private boolean isDuplicateRow(InstituteRatesBean instituteRatesBean) {
		
		Equals activityCodeEquals,activityTypeEquals,fiscalYearEquals,startDateEquals,onOffCampusFlagEquals;
		
		And codeAndFiscal, codeAndFiscalAndstDat,codeAndFiscalAndstDatAndonOff;
		
		activityCodeEquals = new Equals("activityCode", new Integer(instituteRatesBean.getActivityCode()));
		fiscalYearEquals = new Equals("fiscalYear", instituteRatesBean.getFiscalYear());
		startDateEquals = new Equals("startDate", instituteRatesBean.getStartDate());
		onOffCampusFlagEquals = new Equals("onOffCampusFlag",instituteRatesBean.isOnOffCampusFlag());
				
		codeAndFiscal = new And(activityCodeEquals, fiscalYearEquals);
		codeAndFiscalAndstDat = new And(codeAndFiscal,startDateEquals);
		codeAndFiscalAndstDatAndonOff = new And(codeAndFiscalAndstDat, onOffCampusFlagEquals);
			
		CoeusVector coeusVector;
		coeusVector = cvBaseData.filter(codeAndFiscalAndstDatAndonOff);
		if(coeusVector.size()==-1)return false;
		if(coeusVector != null && coeusVector.size() > 0) {
		
			CoeusOptionPane.showInfoDialog("This is a duplicate row");
			isErrorOccured = true;
			return true;
		}
		return false;
	}
	
	/** To get the max row id for AwardReportTermsBean
     * @return maxRowId
     */
    private int getMaxRowId(){
        CoeusVector cvRowData = null;
        int rowId = 1;
        try{
            cvRowData = queryEngine.getDetails(queryKey, InstituteRatesBean.class);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        if( cvRowData != null && cvRowData.size() > 0 ){
            cvRowData.sort(ROW_ID_FIELD, false);
            InstituteRatesBean instituteRatesBean = 
                (InstituteRatesBean)cvRowData.get(0);
            rowId = instituteRatesBean.getRowId() + 1;
        }
        return rowId;
    }
	
	/**
	 * Setting up the form data
	 * @param data Object
	 */	
	public void setFormData(Object data) {
		
		try {
			if (data != null) {
				cvTableData = (CoeusVector)queryEngine.getDetails(queryKey,KeyConstants.ACTIVITY_TYPES);
				activityTypeTableModel.setData(cvTableData);
				InstituteRatesBean instituteRatesBean = (InstituteRatesBean)data;
				addInstituteRatesForm.txtRateClass.setText(instituteRatesBean.getRateClassDescription());
				addInstituteRatesForm.txtRateType.setText(instituteRatesBean.getRateTypeDescription());
				
			}
		} catch (CoeusException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * validate method.It will validate all the fields
	 * @return boolean
	 * @throws CoeusUIException Throwing the UIException
	 */	
	public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
		if (isErrorOccured) {
			isErrorOccured = false;
			//return false;
		}
		
		int selRow [] = addInstituteRatesForm.tblActivityType.getSelectedRows();
		
		if (selRow.length == 0) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ACTIVITY_TYPE_MSG));
			return false;
		}
	
		String fiscalYear = addInstituteRatesForm.txtFiscalYear.getText().trim();
		if (EMPTY_STRING.equals(fiscalYear)) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(FISCAL_YEAR_MSG));
			setRequestFocusInThread(addInstituteRatesForm.txtFiscalYear);
			return false;
		}
		
		//set focus to this component if this field does not have focus so as to 
		//have the required date format for parsing
		String startDate = addInstituteRatesForm.txtStartDate.getText().trim();
		startDate = dateUtils.restoreDate(startDate,DATE_SEPARATERS);
		
		if(!EMPTY_STRING.equals(startDate)) {
			String startFormatDate = dateUtils.formatDate (
			 startDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
			boolean validDate = dateUtils.validateDate(startDate, DATE_SEPARATERS);
			
			if(!validDate) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Item '" +startDate+ "' does not pass validation test."));
				setRequestFocusInThread(addInstituteRatesForm.txtStartDate);
				return false;
			} else {
				modified = true;
			}
		} else {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(START_DATE_MSG));
				setRequestFocusInThread(addInstituteRatesForm.txtStartDate);
				return false;
		}
                
                //Commented for case 3632 - Data Error in rates maintenance when current rate is 0 - start
                //User can enter blank values. It will be saved to db as 0
//		String rates = addInstituteRatesForm.txtRates.getText().trim();
//		if ((".00").equals(rates)) {
//			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RATE_FIELD_MSG));
//			setRequestFocusInThread(addInstituteRatesForm.txtRates);
//			return false;
//		}
                //Commented for case 3632 - Data Error in rates maintenance when current rate is 0 - end
		return true;
	}
	/**
	 * OK Operation
	 */
	private void performOKOperation() {
		try {
			if (validate()) {
				saveFormData();
				if (!isErrorOccured) {
					closeDialog();
				}
			} else {
				return;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/** Specifies the cancel action, the validations are as followed earlier */
	private void performCancelAction(){
		checkFields();
		
		if(modified) {
			confirmClosing();
		} else {
                        dlgAddInstituteRates.setVisible(false);
		}
                
	}
	/**
	 * To check the fields while click on cancel
	 */
	private void checkFields() {
		String fiscalYear = addInstituteRatesForm.txtFiscalYear.getText();
		String startDate = addInstituteRatesForm.txtStartDate.getText();
		String rate = addInstituteRatesForm.txtRates.getText();
		boolean isBoth = addInstituteRatesForm.rdBtnBoth.isSelected();
		boolean isOff = addInstituteRatesForm.rdBtnOff.isSelected();
		if ((!EMPTY_STRING.equals(fiscalYear)) || (!EMPTY_STRING.equals(startDate)) 
							|| (!(".00").equals(rate)) || (isBoth)
								 || (isOff)) {
			modified = true;							
		}
	}
	/** This class will sort the column values in ascending and descending order
	 * based on number of clicks.
	 */
	
	public class ColumnHeaderListener extends MouseAdapter {
		/**
		 * For sorting the table values
		 */		
		String nameBeanId [][] ={
			{"0","code" },
			{"1","description" }
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
				if(cvTableData != null && cvTableData.size()>0 &&
					nameBeanId [vColIndex][1].length() >1 ) {
					((CoeusVector)cvTableData).sort(nameBeanId [vColIndex][1],sort);
					if (sort) {
						sort = false;
					} else {
						sort = true;
					}
					activityTypeTableModel.fireTableRowsUpdated(
					0, activityTypeTableModel.getRowCount());
				}
			} catch(Exception exception) {
				//exception.printStackTrace();
				exception.getMessage();
			}
		}
	}// End of ColumnHeaderListener.................
	
	
	private void setRequestFocusInThread(final Component component) {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				component.requestFocusInWindow();
			}
		});
	}
	
	/**
	 * action performed method
	 * @param e ActionEvent
	 */	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource().equals(addInstituteRatesForm.btnOK)) {
			performOKOperation();
		}
		if (e.getSource().equals(addInstituteRatesForm.btnCancel)) {
			performCancelAction();
		}
	}
	
	public class ActivityTypeTableModel extends AbstractTableModel {
		
		// represents the column names of the table
		private String colName[] = {"Code","Description"};
		// represents the column class of the fields of table
		private Class colClass[] = {Integer.class, String.class};
		
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
		 * @param cvTableData table Data
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
			if (cvTableData != null) {
				ComboBoxBean comboBoxBean = (ComboBoxBean)cvTableData.get(rowIndex);

				switch(columnIndex) {
					case CODE_COLUMN:
						return comboBoxBean.getCode();
					case DESCRIPTION_COLUMN:
						return comboBoxBean.getDescription();
					}
			}
			return EMPTY_STRING;
		}
		
		/**
		 * To get the column name
		 * @param col int
		 * @return String
		 **/
	    public String getColumnName(int col) {
			return colName[col];
		}
		
            //Bug Fix: 1742: Performance Fix - Start 8
            public void cleanUp(){
                colName = null;
		colClass = null;
                cvTableData = null;
            }
            //Bug Fix: 1742: Performance Fix - End 8
            
	}
	
	/** listens to focus gained event.
         * @param focusEvent focusEvent
         */
        public void focusGained(FocusEvent focusEvent) {
            if (focusEvent.isTemporary()) return;
            Object source = focusEvent.getSource();
            if(source.equals(addInstituteRatesForm.txtStartDate)){
                String startDate;
                //Added for COEUSQA-1728 : parameter to define the start date of fiscal year - start
                //Method getStartDateForFiscalYear gives an startDate for the entered fiscal year.
                //and It takes monthvalue and date from the parameter FISCAL_YEAR_START
                String fiscalYear = addInstituteRatesForm.txtFiscalYear.getText().trim();
                String fiscalStartDate = getStartDateForFiscalYear(fiscalYear);
                addInstituteRatesForm.txtStartDate.setText(fiscalStartDate);
                //Added for COEUSQA-1728 : parameter to define the start date of fiscal year - end
                startDate = addInstituteRatesForm.txtStartDate.getText().trim();
                if(startDate.equals(EMPTY_STRING)) return;
                String startFormatDate = dateUtils.restoreDate(startDate,DATE_SEPARATERS);
                if(startFormatDate == null){
                    //isErrorOccured = true;
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Item '"+startDate+ "' does not pass validation test."));
                    setRequestFocusInThread(addInstituteRatesForm.txtStartDate);
                }else{
                    addInstituteRatesForm.txtStartDate.setText(startFormatDate);
                }
            }
            
        }
	/** listens to focus lost event.
         * @param focusEvent focusEvent
         */
        public void focusLost(FocusEvent focusEvent) {
            if (focusEvent.isTemporary()) return;
            Object source = focusEvent.getSource();
            if(source.equals(addInstituteRatesForm.txtStartDate)){
                String startDate;
                startDate = addInstituteRatesForm.txtStartDate.getText().trim();
                if(startDate.equals(EMPTY_STRING)) return;
                String startFormatDate = dateUtils.formatDate(
                        startDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                if(startFormatDate == null){
                    //isErrorOccured = true;
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Item '"+startDate+ "' does not pass validation test."));
                    setRequestFocusInThread(addInstituteRatesForm.txtStartDate);
                }else{
                    addInstituteRatesForm.txtStartDate.setText(startFormatDate);
                    modified = true;
                    
                }
            }
        }
		
	/**
	 * To set all the instance variable to null.
	 * calling from the base class
	 **/
	public void cleanUp() {
            //Bug Fix: 1742: Performance Fix - Start 9
            //addInstituteRatesForm = null;
            //activityTypeTableModel = null;
            //Bug Fix: 1742: Performance Fix - End 9
            instituteRatesBean = null;
            coeusMessageResources = null;
            
            //Bug Fix: 1742: Performance Fix - Start 10
            tableHeader.removeMouseListener(columnHeaderListener);
            tableHeader  = null;
            columnHeaderListener = null;
            activityTypeTableModel.cleanUp();
            activityTypeTableModel = null;
            cvTableData = null;
            cvBaseData = null;
            dlgAddInstituteRates.remove(addInstituteRatesForm);
            addInstituteRatesForm = null;
            //Bug Fix: 1742: Performance Fix - End 10 
        }
	
	/**
	 * Getter for property cvBaseData.
	 * @return Value of property cvBaseData.
	 */
	public edu.mit.coeus.utils.CoeusVector getBaseData() {
		return cvBaseData;
	}
	
	/**
	 * Setter for property cvBaseData.
	 * @param cvBaseData New value of property cvBaseData.
	 */
	public void setBaseData(edu.mit.coeus.utils.CoeusVector cvBaseData) {
		this.cvBaseData = cvBaseData;
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
        }
       
        /**
         *This Method gets start date for the entered fiscal year
         *@return startDate
         */
        private String getStartDateForFiscalYear(String fiscalYear){         
            int newYear = Integer.parseInt(fiscalYear);
            String startDate;
            DateUtils dtUtils =new DateUtils();
            java.util.Date date;
            InstituteRatesBean newInstituteRatesBean = null;
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
            //If value of fiscalMonthValue is from 1 to 6 then it should take the current year as fiscal year
            //If it is greater than 6 then it should consider from previous year to current year as fiscal year
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
                        startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , REQUIRED_DATEFORMAT);                      
                    }else{
                        startDate = fiscalMonthValue +"-"+ fiscalDate +"-" + newYear;// DD-MMM-YYYY
                        year = newYear+1;
                        month = Integer.parseInt(fiscalMonthValue) - 2;
                        calendar.set(year, month, dateForCalendar);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , REQUIRED_DATEFORMAT);
                    }
                }else{
                    startDate = fiscalMonthValue +"-"+ fiscalDate +"-" + newYear;// DD-MMM-YYYY
                    startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , REQUIRED_DATEFORMAT);
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
                startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , REQUIRED_DATEFORMAT);
                }else{
                newYearMinus = newYear - 1;
                startDate = fiscalMonthValue +"-"+ fiscalDate +"-" +newYearMinus;// DD-MMM-YYYY
                startDate = dtUtils.formatDate(startDate, DATE_SEPERATOR , REQUIRED_DATEFORMAT);
                }
               return (startDate.toString());
        }
        //added for COEUSQA -1728 : parameter to define the start date of fiscal year - end
}