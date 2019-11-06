/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * InstituteRatesController.java
 *
 * Created on August 17, 2004, 12:09 PM
 */

package edu.mit.coeus.rates.controller;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Date;
import edu.mit.coeus.rates.bean.InstituteRatesBean;
import edu.mit.coeus.rates.gui.InstituteRatesForm;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.rates.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import java.util.Vector;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;

/**
 *
 * @author  surekhan
 */
public class InstituteRatesController extends RatesController implements ActionListener , ItemListener 
//Bug Fix: 1742: Performance Fix - Start 1
/*, ListSelectionListener */ 
//Bug Fix: 1742: Performance Fix - End 1
,BeanUpdatedListener  {
    
    /*institute rates form which this controls*/
    private InstituteRatesForm instituteRatesForm = new InstituteRatesForm();
    
    /*the dialog window*/
    private CoeusDlgWindow dlgInstituteRates;
    
    /*mdiForm instance*/
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /*width of the window*/
    private static final int WIDTH = 550;
    
    /*Height of the window*/
    private static final int HEIGHT = 400;
    
    /*function type declaration*/
    private char functionType;
    
    /*coeusMessage resource instance*/
    private CoeusMessageResources coeusMessageResources;
    
    /*vector that holds the rate class data*/
    private CoeusVector cvRateClass;
    
    /*vector that holds the table data*/
    private CoeusVector cvTableData;
    
    /*vector that hold the after filtering the reqd rate class*/
    private CoeusVector cvFilteredRateClass;
    
    /*vector that holds the rate type data*/
    private CoeusVector cvRateType;
    
    /*vector that holda the deleted data*/
    private CoeusVector cvDeletedItem;
    
    /*Query engine instance*/
    private QueryEngine queryEngine;
    
    /*table model for the institute rates table*/
    private InstituteRatesTableModel instituteRatesTableModel;
    
    /*the editor for the table*/
    private InstituteRatesEditor instituteRatesEditor;
    
    /*the renderer for the table*/
    private InstituteRatesRenderer instituteRatesRenderer;
    
    /*setting the column numbers for the table*/
    private static final int HAND_ICON_COLUMN = 0;
    private static final int ACTIVITY_TYPE = 1;
    private static final int FISCAL_YEAR = 2;
    private static final int CAMPUS_FLAG = 3;
    private static final int START_DATE = 4;
    private static final int RATE = 5;
    //Added code for case #1748 by tarique start 1
    private static final int UPDATE_TIME = 6;
    private static final int UPDATE_USER = 7;
    //Added code for case #1748 by tarique end 1
    
    private static final String EMPTY_STRING = "";
    
    /*to set the back ground color*/
    private static final java.awt.Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    
    /*the date format*/
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    /*the date format while displaying*/
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    
    /*date saperators*/
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    /*date utils*/
    private DateUtils dateUtils;
    private DateUtils dtUtils = new DateUtils();
    
    /*the date format on editing*/
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    /*item does not pass validation test*/
    private static final String INVALID_DATE = "instRates_exceptionCode.1003";
    
    /*enter the start date*/
    private static final String DATE_MSG = "instRates_exceptionCode.1001";
    
    /*enter the rate*/
    private static final String RATE_MSG = "instRates_exceptionCode.1002";
    
    /*Are u sure u want to delete this row*/
    private static final String DELETE_CONFIRMATION = "award_exceptionCode.1009";
    
    /*flags used to set in the campus flag field */
    private static final String ON_FLAG = "On";
    private static final String OFF_FLAG = "Off";
    
    /*the fields which has to get sorted For the Bug Fix:1742 Start step:1*/
    private String[] fieldNames = {"activityCode", "fiscalYear","onOffCampusFlag"};
    
    private ListSelectionModel instituteSelectionModel;
    
    /* the flag is set when any of the table data is modified*/
    private boolean modified ;
    
    /*the flag is set for the validation purpose*/
    private boolean flag;
    
    private boolean showWindow;
    
    private boolean ZERO_ENTERED = false;
    
    private double DEFAULT_INST_RATE = .00;
    
    //added for the Bug Fix:1724 start step:1
    private Vector vecSortedData;
    private static final int OK_CLICKED = 0;
    private String beanFieldNames [][] ={
                    {"0","" },
                    {"1","activityCode" },
                    {"2","fiscalYear"},
                    {"3","onOffCampusFlag" },
                    {"4","startDate"},
                    {"5","rate" },
                    {"6","updateTimestamp" },
                    {"7","updateUser" },
                };
     //Bug Fix:1724 End step:1
 
    
    /** Creates a new instance of InstituteRatesController */
    public InstituteRatesController(InstituteRatesBean instituteRatesBean ,char functionType) {
        super(instituteRatesBean);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
        dateUtils = new DateUtils();
        cvDeletedItem = new CoeusVector();
        instituteRatesTableModel = new InstituteRatesTableModel();
        instituteRatesEditor = new InstituteRatesEditor();
        postInitComponents();
        registerComponents();
        instituteRatesRenderer = new InstituteRatesRenderer();
        setTableEditors();
        setTableKeyTraversal();
        setFormData(null);
        formatFields();
        
    }
    
    /*Instantiates instance objects*/
    private void postInitComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        dlgInstituteRates = new CoeusDlgWindow(mdiForm);
        dlgInstituteRates.setResizable(false);
        dlgInstituteRates.setModal(true);
        dlgInstituteRates.getContentPane().add(instituteRatesForm);
        dlgInstituteRates.setFont(CoeusFontFactory.getLabelFont());
        dlgInstituteRates.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgInstituteRates.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgInstituteRates.getSize();
        dlgInstituteRates.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgInstituteRates.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                dlgInstituteRates.dispose();
            }
        });
    }
    
    /*to diaplay the dialog window*/
    public void display() {
        if(instituteRatesForm.tblInstituteRate.getRowCount() > 0){
            instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
        }
        dlgInstituteRates.show();
    }
    
    /*formats Components*/
    public void formatFields() {
        if(functionType == TypeConstants.DISPLAY_MODE){
            instituteRatesForm.btnAdd.setEnabled(false);
            instituteRatesForm.btnDelete.setEnabled(false);
            instituteRatesForm.cmbRateClass.setEnabled(false);
            instituteRatesForm.cmbRateType.setEnabled(false);
            instituteRatesForm.tblInstituteRate.setEnabled(false);
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            instituteRatesForm.tblInstituteRate.setBackground(bgColor);
            instituteRatesForm.tblInstituteRate.setSelectionBackground(bgColor);
            
        }
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            instituteRatesForm.tblInstituteRate.setBackground(bgColor);
            instituteRatesForm.tblInstituteRate.setSelectionBackground(bgColor);
        }
    }
    
    /** returns controlled UI.
     * @return Controlled UI
     */
    public java.awt.Component getControlledUI() {
        return instituteRatesForm;
    }
    
    /** returns form data.
     * @return returns form data.
     */
    public Object getFormData() {
        return instituteRatesForm;
    }
    
    /*registers the components with the listeners*/
    public void registerComponents() {
        java.awt.Component[] components = {instituteRatesForm.cmbRateClass,
        instituteRatesForm.cmbRateType,instituteRatesForm.tblInstituteRate,
        instituteRatesForm.btnAdd,instituteRatesForm.btnDelete,instituteRatesForm.btnSort};
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        instituteRatesForm.setFocusTraversalPolicy(traversePolicy);
        instituteRatesForm.setFocusCycleRoot(true);
        
        instituteRatesForm.btnAdd.addActionListener(this);
        instituteRatesForm.btnDelete.addActionListener(this);
        instituteRatesForm.btnSort.addActionListener(this);
        
        instituteRatesTableModel = new InstituteRatesTableModel();
        instituteRatesForm.tblInstituteRate.setModel(instituteRatesTableModel);
        
        //Bug Fix: 1742: Performance Fix - Start 2
        /*instituteRatesForm.tblInstituteRate.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instituteSelectionModel = instituteRatesForm.tblInstituteRate.getSelectionModel();
        instituteSelectionModel.addListSelectionListener(this);
        instituteRatesForm.tblInstituteRate.setSelectionModel(instituteSelectionModel);
         */
        //Bug Fix: 1742: Performance Fix - End 2
        
        instituteRatesForm.tblInstituteRate.setAutoResizeMode(
        instituteRatesForm.tblInstituteRate.AUTO_RESIZE_OFF);
        
        instituteRatesForm.cmbRateType.addItemListener(this);
        instituteRatesForm.cmbRateClass.addItemListener(this);
        addBeanUpdatedListener(this, InstituteRatesBean.class);
    }
    
    /*tp update the bean to the table*/
    public void beanUpdated(BeanEvent beanEvent) {
        if( beanEvent.getSource().getClass().equals(AddInstituteRatesController.class) ){
            if( beanEvent.getBean().getClass().equals(InstituteRatesBean.class)){
                setData(null);
            }
        }
    }
    
    
    /*saves the form data */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        if(!isSaveRequired()) return;
        instituteRatesEditor.stopCellEditing();
        
        //Bug Fix:Saving:If a rate type is changed and edited then 
        //the data was not updaeted since in setTableData they are filtering for 
        //the particular selected rate type and rate class: Start

        //CoeusVector cvData = instituteRatesTableModel.getData();
        CoeusVector cvData = cvTableData;
        
        //Bug Fix:Saving: End 
        
        if( cvData == null ) return ;
        //Delete all the deleted beans from the query engine
        
        //Bug Fix:Delete Rates Deleting the in performDeleteAction Start 4
//        for( int index = 0; index < cvDeletedItem.size(); index++ ){
//            InstituteRatesBean bean = (InstituteRatesBean)cvDeletedItem.get(index);
//            queryEngine.delete(queryKey, bean);
//        }
        //Bug Fix:Delete Rates End 4
        
        //Bug Fix: 1742: Performance Fix - Start 3
        NotEquals neNull = new NotEquals("acType",null);
        cvData = cvData.filter(neNull);
        int maxId  = getExistingMaxId();
        //Bug Fix: 1742: Performance Fix - End 3
        
        for( int index = 0; index < cvData.size(); index++ ){
            InstituteRatesBean bean = (InstituteRatesBean)cvData.get(index);
            if( bean.getAcType() != null ){
                if( bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                    //Delete the existing bean in the query engine
                    //got from the database and insert it with new one
                    InstituteRatesBean existingBean = bean;
                    queryEngine.delete(queryKey, bean);
                    existingBean.setAcType(TypeConstants.INSERT_RECORD);
                    
                    //Bug Fix: 1742: Performance Fix - Start 4
                    //existingBean.setRowId(getExistingMaxId() + index + 1);
                    maxId = maxId + index + 1;
                    existingBean.setRowId(maxId);
                    //Bug Fix: 1742: Performance Fix - End 4
                    
                    queryEngine.insert(queryKey, existingBean);
                }else if( bean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
                    //Set the rowId of the bean if not already set
                    if( bean.getRowId() == 0 ){
                        
                        //Bug Fix: 1742: Performance Fix - Start 5
                        //bean.setRowId(getExistingMaxId() + index + 1);
                        maxId = maxId + index + 1;
                        bean.setRowId(maxId);
                        //Bug Fix: 1742: Performance Fix - End 5
                    }
                    
                    //Bug Fix:1603 Start 1
                    queryEngine.update(queryKey,bean);
                    //Bug Fix:1603 End 1
                }
            }
        }
        
    }
    
    /*to get the maximum rowId of the vector*/
    private int getExistingMaxId() {
        CoeusVector cvExistingRecords = new CoeusVector();
        int maxRowId = 0;
        try{
            cvExistingRecords = queryEngine.getDetails(queryKey, InstituteRatesBean.class);
            cvExistingRecords.sort("rowId",false);
            if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                InstituteRatesBean bean = (InstituteRatesBean)cvExistingRecords.get(0);
                maxRowId = bean.getRowId();
            }else{
                maxRowId = 0;
            }
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        return maxRowId;
    }
    
    
    /** sets data to form.
     * @param data data to be set.
     */
    public void setFormData(Object data){
        try{
            CoeusVector cvFilter = new CoeusVector();
            cvRateClass = new CoeusVector();
            cvTableData = new CoeusVector();
            cvFilteredRateClass = new CoeusVector();
            cvRateType = new CoeusVector();
            instituteRatesForm.tblInstituteRate.setBackground(PANEL_BACKGROUND_COLOR);
            
            cvRateClass = queryEngine.getDetails(queryKey,KeyConstants.RATE_CLASS_DATA);
            /*tofilter for the rateclasstype not equal to "L" or "Y" */
            NotEquals rtClass = new NotEquals("rateClassType" , "L");
            NotEquals rateclss = new NotEquals("rateClassType" , "Y");
            And rtclss = new And(rtClass , rateclss);
            cvRateClass = cvRateClass.filter(rtclss);
            instituteRatesForm.cmbRateClass.setModel(new DefaultComboBoxModel(cvRateClass));
            
            
            cvRateType = queryEngine.getDetails(queryKey , KeyConstants.RATE_TYPE_DATA);
            if(cvRateType != null && cvRateType.size() > 0){
                RateClassBean rateClassBean = (RateClassBean)cvRateClass.get(0);
                int code = Integer.parseInt(rateClassBean.getCode());
                Equals rt = new Equals( "rateClassCode" , new Integer(code) );
                cvFilteredRateClass = cvRateType.filter(rt);
                instituteRatesForm.cmbRateType.setModel(new DefaultComboBoxModel(cvFilteredRateClass));
            }
            
            cvTableData = queryEngine.executeQuery(queryKey , InstituteRatesBean.class , CoeusVector.FILTER_ACTIVE_BEANS);
            
            if(cvTableData != null && cvTableData.size() > 0){
                RateClassBean bean = (RateClassBean)cvRateClass.get(0);
                int rateCodes = Integer.parseInt(bean.getCode());
                Equals rateCode = new Equals("rateClassCode" , new Integer(rateCodes));
                // filter = cvRateClass.filter(rateCode);
                
                RateTypeBean typeBean = (RateTypeBean)cvFilteredRateClass.get(0);
                int rate = Integer.parseInt(typeBean.getCode());
                Equals rateTypeCode = new Equals("rateTypeCode" , new Integer(rate));
                And tableData = new And(rateCode , rateTypeCode);
                cvFilter = cvTableData.filter(tableData);
                cvFilter.sort(fieldNames, true);
                //Added for the Bug Fix:1742 Sort Buttton Implementation start
                prepareSortColumns();
                //End Bug Fix:1742
                instituteRatesTableModel.setData(cvFilter);
                if(cvFilter != null && cvFilter.size() > 0){
                    instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
                }
            }
            
            
            //Bug Fix:1603 Start 2
//            if(cvTableData != null && cvTableData.size() > 0){
//                instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
//            }
            //Bug Fix:1603 End 2
            
            if(cvTableData == null){
                instituteRatesForm.btnDelete.setEnabled(false);
            }else{
                instituteRatesForm.btnDelete.setEnabled(true);
            }
            
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    
    //Added for the Bug Fix:1742 Sort Buttton Implementation start
    private void prepareSortColumns() {
        vecSortedData = new Vector();
        for(int index=0; index<fieldNames.length;index++) {
            Vector vecData = getSortColumnData(fieldNames[index]);
            if(vecData != null)
                vecSortedData.addElement(vecData);
        }
    }//End Bug Fix:1742
    
    //Added for the Bug Fix:1742 Sort Buttton Implementation start
    private Vector getSortColumnData(String colName) {
         Vector colData = null;
        for(int index=0; index<beanFieldNames.length;index++) {
            if(beanFieldNames[index][1].equals(colName)) {
                colData = new Vector();
                colData.addElement(instituteRatesForm.tblInstituteRate.getColumnName(index));
                colData.addElement(new Integer(beanFieldNames[index][0]));
                colData.addElement(new Boolean(true));
                break;
            }
        }
        return colData;
    }//End Bug Fix:1742
    
    /* to set the rate class and rate type to the respective fields in the
     * add institute rates window*/
    public void setData(Object data){
        CoeusVector cvTemp = new CoeusVector();
        ComboBoxBean cmbBean = new ComboBoxBean();
        
        cmbBean = (ComboBoxBean)instituteRatesForm.cmbRateClass.getSelectedItem();
        int code = Integer.parseInt(cmbBean.getCode());
        Equals rate = new Equals("rateClassCode" , new Integer(code));
        
        
        ComboBoxBean comboBean = new ComboBoxBean();
        comboBean = (ComboBoxBean)instituteRatesForm.cmbRateType.getSelectedItem();
        int clssCode = Integer.parseInt(comboBean.getCode());
        Equals classCode = new Equals("rateTypeCode" , new Integer(clssCode));
        And instData = new And(rate , classCode);
        try{
            CoeusVector cvUpdatedData = queryEngine.executeQuery(queryKey, InstituteRatesBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            cvTemp = cvUpdatedData.filter(instData);
            cvTemp.sort(fieldNames, true);
            instituteRatesTableModel.setData(cvTemp);
            instituteRatesTableModel.fireTableDataChanged();
            
            //Bug Fix:1603 Start 3
            cvTableData.addAll(cvTemp);
            //Bug Fix:1603 End 3
            
            if(cvTemp != null && cvTemp.size() > 0){
                instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
            }
            if(cvTemp == null || cvTemp.size() == 0 ){
                instituteRatesForm.btnDelete.setEnabled(false);
            }else{
                instituteRatesForm.btnDelete.setEnabled(true);
            }
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
    }
    
    
    public void itemStateChanged(ItemEvent itemEvent){
        if(itemEvent.getStateChange() == itemEvent.DESELECTED){
            return ;
        }
        Object source = itemEvent.getSource();
        if(source.equals(instituteRatesForm.cmbRateClass)){
            //Bug Fix:1603 Start 4
            instituteRatesEditor.stopCellEditing();
            //Bug Fix:1603 End 4
            setRateTypeData();
        }
        
        if(source.equals(instituteRatesForm.cmbRateType)){
            //Bug Fix:1603 Start 5
            instituteRatesEditor.stopCellEditing();
            //Bug Fix:1603 End 5
            setTableData();
        }
    }
    
    /* to set the table data when the rate class is changed*/
    public void setRateTypeData(){
        CoeusVector cvTemp = new CoeusVector();
        CoeusVector cvTableTemp = new CoeusVector();
        ComboBoxBean comboBean = new ComboBoxBean();
        
        comboBean = (ComboBoxBean)instituteRatesForm.cmbRateClass.getSelectedItem();
        int code = Integer.parseInt(comboBean.getCode());
        Equals rate = new Equals("rateClassCode" , new Integer(code));
        cvTemp = cvRateType.filter(rate);
        instituteRatesForm.cmbRateType.setModel(new DefaultComboBoxModel(cvTemp));
        
        ComboBoxBean cmbBean = new ComboBoxBean();
        cmbBean = (ComboBoxBean)instituteRatesForm.cmbRateClass.getSelectedItem();
        int rateCode = Integer.parseInt(cmbBean.getCode());
        Equals ratesCode = new Equals("rateClassCode" , new Integer(code));
        
        ComboBoxBean bean = new ComboBoxBean();
        bean = (ComboBoxBean)instituteRatesForm.cmbRateType.getSelectedItem();
        if(bean == null){
            instituteRatesTableModel.setData(null);
            instituteRatesTableModel.fireTableDataChanged();
        }
        if(bean != null){
            int typeRate = Integer.parseInt(bean.getCode());
            Equals typeCode = new Equals("rateTypeCode" , new Integer(typeRate));
            And instData = new And(ratesCode , typeCode);
            cvTableTemp = cvTableData.filter(instData);
            cvTableTemp.sort(fieldNames , true);
            instituteRatesTableModel.setData(cvTableTemp);
            instituteRatesTableModel.fireTableDataChanged();
        }
        if(cvTableTemp != null && cvTableTemp.size() > 0){
            instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
        }
        if(cvTableTemp == null || cvTableTemp.size() == 0){
            instituteRatesForm.btnDelete.setEnabled(false);
        }else{
            instituteRatesForm.btnDelete.setEnabled(true);
        }
        
    }
    
    /* to set the table data when the rate type  is changed*/
    public void setTableData(){
        CoeusVector cvData = new CoeusVector();
        ComboBoxBean cmbBean = new ComboBoxBean();
        cmbBean = (ComboBoxBean)instituteRatesForm.cmbRateClass.getSelectedItem();
        int code = Integer.parseInt(cmbBean.getCode());
        Equals rateCode = new Equals("rateClassCode" , new Integer(code));
        
        ComboBoxBean bean = new ComboBoxBean();
        bean = (ComboBoxBean)instituteRatesForm.cmbRateType.getSelectedItem();
        int rate = Integer.parseInt(bean.getCode());
        Equals rateTypeCode = new Equals("rateTypeCode" , new Integer(rate));
        And data = new And(rateCode , rateTypeCode);
        cvData = cvTableData.filter(data);
        cvData.sort(fieldNames, true);
        instituteRatesTableModel.setData(cvData);
        instituteRatesTableModel.fireTableDataChanged();
        if(cvData != null && cvData.size() > 0){
            instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
        }
        if(cvData == null || cvData.size() == 0 ){
            instituteRatesForm.btnDelete.setEnabled(false);
        }else{
            instituteRatesForm.btnDelete.setEnabled(true);
        }
            
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
        //instituteRatesEditor.stopCellEditing();
        CoeusVector cvTable = instituteRatesTableModel.getData();
        int row = instituteRatesForm.tblInstituteRate.getSelectedRow();
        if(row !=  -1){
            String message;
            InstituteRatesBean bean  = (InstituteRatesBean)cvTable.get(row);
            if(instituteRatesForm.tblInstituteRate.isEditing()){
                if(instituteRatesForm.tblInstituteRate.getEditingColumn() == START_DATE ){
                    try{
                        String startDate;
                        Date date;
                        String value = instituteRatesEditor.getCellEditorValue().toString();
                        if(value != null && !value.toString().trim().equals(EMPTY_STRING )){
                            startDate = dtUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS,DATE_FORMAT);
                            if(startDate == null){
                                if( !value.toString().trim().equals(EMPTY_STRING)){
                                    CoeusOptionPane.showInfoDialog(" Item  '"+value+ "' does not pass validation test.");
                                    showWindow = true;
                                    setRequestFocusInThread(row , START_DATE);
                                    return false;
                                    
                                }else{
                                    bean.setStartDate(null);
                                    
                                }
                            }
                            
                            date = dtFormat.parse(dateUtils.restoreDate(startDate, DATE_SEPARATERS));
                        }
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_DATE);
                        CoeusOptionPane.showInfoDialog(message);
                        setRequestFocusInThread(row , START_DATE);
                        return false;
                    }
                }
            }
        }
        
        instituteRatesEditor.stopCellEditing();
        CoeusVector cvData = instituteRatesTableModel.getData();
        if( cvData == null ) return true;
        for(int index=0 ; index < cvData.size() ; index++){
            InstituteRatesBean ratesBean = (InstituteRatesBean)cvData.elementAt(index);
            if(ratesBean.getStartDate() == null){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_MSG));
                instituteRatesForm.tblInstituteRate.setRowSelectionInterval(index,index);
                instituteRatesForm.tblInstituteRate.setColumnSelectionInterval(0,0);
                instituteRatesForm.tblInstituteRate.scrollRectToVisible(
                instituteRatesForm.tblInstituteRate.getCellRect(
                index ,0, true));
                instituteRatesForm.tblInstituteRate.editCellAt(index,START_DATE);
                setRequestFocusInThread(index , START_DATE);
                return false;
            }
            
            //Bug Fix:If a number with 0 was entered the bean value will be set to 0
            //To avoid this making an extra check.Start
            //if(ZERO_ENTERED){
            if(ZERO_ENTERED && ratesBean.getRate() == .00){
                ratesBean.setRate(0.0);
                return true;
            }
            //Bug Fix: End
            //Commented for case 3632 - Data Error in rates maintenance when current rate is 0 - start
            //After the bugfix for 3632 rate is never saved to datatbase as -1. So no need
            //for this validation
//            if(!modified){
//                if(ratesBean.getRate() == -1){
//                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RATE_MSG));
//                    instituteRatesForm.tblInstituteRate.setRowSelectionInterval(index,index);
//                    instituteRatesForm.tblInstituteRate.setColumnSelectionInterval(0,0);
//                    instituteRatesForm.tblInstituteRate.scrollRectToVisible(
//                    instituteRatesForm.tblInstituteRate.getCellRect(
//                    index ,0, true));
//                    instituteRatesForm.tblInstituteRate.editCellAt(index,RATE);
//                    setRequestFocusInThread(index , RATE);
//                    return false;
//                    
//                }
//            }
            //Commented for case 3632 - Data Error in rates maintenance when current rate is 0 - end
            
      }
        return true;
    }
    
    /*to set the focus in their respective fields*/
    private void setRequestFocusInThread(final int selrow , final int selcol) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                // component.requestFocusInWindow();
                instituteRatesForm.tblInstituteRate.requestFocusInWindow();
                instituteRatesForm.tblInstituteRate.changeSelection( selrow, selcol, true, false);
                instituteRatesForm.tblInstituteRate.setRowSelectionInterval(selrow , selrow);
            }
        });
        
    }
    
    /** listens to action events.
     * @param actionEvent actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        //Modified code to show hour glass during Adding or Deleting by tarique start 2
        try{
            instituteRatesForm.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
            //Modified code to show hour glass during Adding or Deleting by tarique end 2
            Object source = actionEvent.getSource();
            
            InstituteRatesBean newBean = new InstituteRatesBean();
            ComboBoxBean bean = new ComboBoxBean();
            ComboBoxBean typeBean = new ComboBoxBean();
            bean = (ComboBoxBean)instituteRatesForm.cmbRateClass.getSelectedItem();
            typeBean = (ComboBoxBean)instituteRatesForm.cmbRateType.getSelectedItem();
            
            if(typeBean != null){
                newBean.setRateClassCode(Integer.parseInt(bean.getCode()));
                newBean.setRateClassDescription(bean.getDescription());
                newBean.setRateTypeCode(Integer.parseInt(typeBean.getCode()));
                newBean.setRateTypeDescription(typeBean.getDescription());
                newBean.setUnitNumber(instituteRatesBean.getUnitNumber());
            }
            if(source.equals(instituteRatesForm.btnAdd)){
                if(typeBean == null){
                    CoeusOptionPane.showInfoDialog("No rates types available for this rate class.");
                    return;
                }
                AddInstituteRatesController addController = new AddInstituteRatesController(newBean , functionType);
                
                addController.setBaseData(instituteRatesTableModel.getData());
                addController.display();
                
                //Bug Fix: 1742: Performance Fix - Start 6
                addController.cleanUp();
                //Bug Fix: 1742: Performance Fix - End 6
                
            }else if(source.equals(instituteRatesForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(instituteRatesForm.btnSort)){
                //For the Bug Fix:1724 Sort Button implementation start step:2
                //            CoeusOptionPane.showInfoDialog("Functionality not implemented");
                //            return;
                showSort();
                //Bug Fix End :1724 step:2
                
            }
        //Modified code to show hour glass during Adding or Deleting by tarique start 3
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            instituteRatesForm.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
        }
        //Modified code to show hour glass during Adding or Deleting by tarique end 3
    }
    
    
    /* Added for Bug Fix:1724 Sort Buttton Implementation start step:3
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(instituteRatesForm.tblInstituteRate,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
         CoeusVector cvData = new CoeusVector();
         cvData = instituteRatesTableModel.getData();
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED){
            
            sortByColumns(cvData,vecSortedData);
          
        }else{
            return;
        }
    }//End Bug Fix:1724 step:3
    
    
    /** For the Bug Fix:1724 Sorting multilple columns start step:4
     * This will sort by column index
     * @param sourceTable source Table
     * @param columns type vector
     */
    public void sortByColumns(CoeusVector cvData,Vector columns) {
        String colNames[] = new String[columns.size()];
        boolean[] asce = new boolean[columns.size()];
        
        for(int index=0; index<columns.size();index++) {
            int colIndex = ((Integer)((Vector)columns.get(index)).get(1)).intValue();
            colNames[index] = beanFieldNames[colIndex][1];
            asce[index] = ((Boolean)((Vector)columns.get(index)).get(2)).booleanValue();
        }
        //Modified code for case #1748 by tarique start 
        if(colNames.length != 0){
            cvData.sort(colNames,asce);
        }
        //Modified Code for case #1748 by tarique end 
        instituteRatesTableModel.fireTableDataChanged();
        instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
    }//End Bug fix:1724 End step:4

    
    /*the action performed on the click of the delete cammand button*/
    private void performDeleteAction(){
        instituteRatesEditor.stopCellEditing();
        CoeusVector cvData = instituteRatesTableModel.getData();
        if(instituteRatesForm.tblInstituteRate.getRowCount() == 0){
            return;
        }
        int selectedRow = instituteRatesForm.tblInstituteRate.getSelectedRow();
        if(selectedRow == -1){
            return;
        }
        if(selectedRow != -1 && selectedRow >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                InstituteRatesBean deletedBean = (InstituteRatesBean)cvData.get(selectedRow);
                
                //Bug Fix:Delete Rates: If rate was deleted and new rate was added 
                //The deleted rate would also be displayed in the table 
                //Since the bean ACTYPE in the query engine would be the 
                //the old Actype itself : Start 1
                
//                cvDeletedItem.add(deletedBean);
//                if (deletedBean.getAcType() == null ||
//                deletedBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
//                    cvDeletedItem.add(deletedBean);
//                }
                //Bug Fix: End 1
                
                if(cvData!=null && cvData.size() > 0){
                    cvData.remove(selectedRow);
                    //Bug Fix:Delete Rates Start 2
                    cvTableData.remove(selectedRow);
                    //Bug Fix:Delete Rates End 2
                    instituteRatesTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    modified = true;
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    
                }
                
                //Bug Fix:Delete Rates Start 3
                try{
                    queryEngine.delete(queryKey,deletedBean);
                }catch (CoeusException ce){
                    ce.printStackTrace();
                }
                //Bug Fix:Delete Rates End 3
            }
        }
        
        if(selectedRow >0){
            instituteRatesForm.tblInstituteRate.setRowSelectionInterval(
            selectedRow-1,selectedRow-1);
            instituteRatesForm.tblInstituteRate.scrollRectToVisible(
            instituteRatesForm.tblInstituteRate.getCellRect(
            selectedRow -1 ,0, true));
        }else{
            if(instituteRatesForm.tblInstituteRate.getRowCount()>0){
                instituteRatesForm.tblInstituteRate.setRowSelectionInterval(0,0);
            }
        }
        
    }
    
    /*to set the renderers and editors to the table*/
    private void setTableEditors(){
        instituteRatesForm.tblInstituteRate.setRowHeight(22);
        instituteRatesForm.tblInstituteRate.setShowHorizontalLines(false);
        instituteRatesForm.tblInstituteRate.setShowVerticalLines(false);
        JTableHeader tableHeader = instituteRatesForm.tblInstituteRate.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        instituteRatesForm.tblInstituteRate.setOpaque(false);
        instituteRatesForm.tblInstituteRate.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(HAND_ICON_COLUMN);
        column.setPreferredWidth(30);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(ACTIVITY_TYPE);
        column.setPreferredWidth(125);
        column.setResizable(true);
        column.setCellEditor(instituteRatesEditor);
        column.setCellRenderer(instituteRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(FISCAL_YEAR);
        column.setPreferredWidth(85);
        column.setResizable(true);
        column.setCellEditor(instituteRatesEditor);
        column.setCellRenderer(instituteRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(CAMPUS_FLAG);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellEditor(instituteRatesEditor);
        column.setCellRenderer(instituteRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(START_DATE);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellEditor(instituteRatesEditor);
        column.setCellRenderer(instituteRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(RATE);
        column.setPreferredWidth(60);
        column.setResizable(true);
        column.setCellEditor(instituteRatesEditor);
        column.setCellRenderer(instituteRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        //Added for case #1748 by tarique start 4
        column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(UPDATE_TIME);
        column.setPreferredWidth(135);
        column.setResizable(true);
        column.setCellEditor(instituteRatesEditor);
        column.setCellRenderer(instituteRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = instituteRatesForm.tblInstituteRate.getColumnModel().getColumn(UPDATE_USER);
        column.setPreferredWidth(85);
        column.setResizable(true);
        column.setCellEditor(instituteRatesEditor);
        column.setCellRenderer(instituteRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        //Added for case #1748 by tarique end 4
        
    }
    
    //Bug Fix: 1742: Performance Fix - Start 7
    /*public void valueChanged(ListSelectionEvent e) {
    }*/
    //Bug Fix: 1742: Performance Fix - End 7
    
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","" },
            {"1","activityCode" },
            {"2","fiscalYear"},
            {"3","onOffCampusFlag" },
            {"4","startDate"},
            {"5","rate" },
        };
        //For the Bug Fix:1724 Column Header sorting start 
//        boolean sort = true;
        boolean sort = false;
        //ENd Bug Fix:1724 Column Header sorting
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                Object scr = evt.getSource();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                int column = colModel.getColumn(vColIndex).getModelIndex();
                if(scr.equals(instituteRatesForm.tblInstituteRate.getTableHeader())){
                    CoeusVector cvData = instituteRatesTableModel.getData();
                    if(cvData != null && cvData.size()>0 &&
                    nameBeanId [vColIndex][1].length() >1 ){
                        ((CoeusVector)cvData).sort(nameBeanId [vColIndex][1],sort);
                        //Added for the Bug Fix:1742 Sort Button Implementation start 
                        vecSortedData.removeAllElements();
                        Vector newSortVec = new Vector();
                        newSortVec.addElement(instituteRatesForm.tblInstituteRate.getColumnName(column));
                        newSortVec.addElement(new Integer(column));
                        newSortVec.addElement(new Boolean(sort));
                        vecSortedData.addElement(newSortVec);
                        //End Bug Fix:1742
                        if(sort)
                            sort = false;
                        else
                            sort = true;
                        instituteRatesTableModel.fireTableRowsUpdated(0, instituteRatesTableModel.getRowCount());
                    }
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
            
        }
    }
    
    /*table model for the institute rates table*/
    class InstituteRatesTableModel  extends AbstractTableModel{
        private CoeusVector cvTableData = new CoeusVector();
        //Modified code for case #1748 by tarique start 5
        String colNames[] = {" " , "Activity Type" , "Fiscal Year" , "Campus Flag" 
            , "Start Date" ,"Rate","Update Timestamp" , "Update User"};
        Class[] colTypes = new Class [] {String.class , String.class , String.class 
            , String.class ,String.class ,  Integer.class, String.class, String.class };
        //Modified code for case #1748 by tarique end 5
        public void setData(CoeusVector cvTableData) {
            this.cvTableData = cvTableData;
        }
        
        public boolean isCellEditable(int row, int column){
            switch(column){
                case HAND_ICON_COLUMN:
                    return false;
                case ACTIVITY_TYPE:
                    return false;
                case FISCAL_YEAR:
                    return false;
                case CAMPUS_FLAG:
                    return false;
                case START_DATE:
                    return true;
                case RATE:
                    return true;
                //Modified code for case #1748 by tarique start 6
                case UPDATE_TIME :
                case UPDATE_USER :
                    return false;
                //Modified code for case #1748 by tarique end 6
            }
            return false;
        }
        
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /* returns the number of rows*/
        public int getRowCount() {
            if(cvTableData == null){
                return 0;
            }else{
                return cvTableData.size();
            }
        }
        
        
        public CoeusVector getData(){
            return cvTableData;
        }
        
        /* gets the value at a particular cell*/
        public Object getValueAt(int row, int column) {
            InstituteRatesBean ratesBean = (InstituteRatesBean)cvTableData.get(row);
            switch(column){
                case HAND_ICON_COLUMN:
                    return EMPTY_STRING;
                case ACTIVITY_TYPE:
                    return ratesBean.getActivityTypeDescription();
                case FISCAL_YEAR:
                    return ratesBean.getFiscalYear();
                case CAMPUS_FLAG:
                    if(ratesBean.isOnOffCampusFlag()){
                        return ON_FLAG;
                    }else{
                        return OFF_FLAG;
                    }
                    
                case START_DATE:
                    return ratesBean.getStartDate();
                case RATE:
                    return new Double(ratesBean.getRate());
                //Modified code for case #1748 by tarique start 7
                case UPDATE_TIME :
                    if(ratesBean.getUpdateTimestamp() == null){
                        return EMPTY_STRING;
                    }
                    return ratesBean.getUpdateTimestamp().toString();
                case UPDATE_USER :
                    if(ratesBean.getUpdateUser() == null){
                        return EMPTY_STRING;
                    }
                    return ratesBean.getUpdateUser();
                //Modified code for case #1748 by tarique end  7 
            }
            return EMPTY_STRING;
        }
        
        /*sets the value to the particular cell*/
        public void setValueAt(Object value, int row , int column) {
            InstituteRatesBean instRatesBean = (InstituteRatesBean)cvTableData.get(row);
            String startDate;
            Date dt;
            //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - start
            //valueChanged will be set to true if there is change in the data value while modification
            boolean valueChanged = false;
            switch(column){
                case ACTIVITY_TYPE:
                    if(!value.toString().trim().equals(instRatesBean.getActivityTypeDescription())){
                        instRatesBean.setActivityTypeDescription(value.toString());
                        modified = true;
                        valueChanged = true;
                    }
                    break;
                case FISCAL_YEAR:
                    if(!value.toString().trim().equals(instRatesBean.getFiscalYear())){
                        instRatesBean.setFiscalYear(value.toString());
                        modified = true;
                        valueChanged = true;
                    }
                    break;
                case CAMPUS_FLAG:
                    if(value != null){
                        boolean campus = ((Boolean)value).booleanValue();
                        if( instRatesBean.isOnOffCampusFlag() != campus ){
                            instRatesBean.setOnOffCampusFlag(campus);
                            modified = true;
                            valueChanged = true;
                        }
                    }
                    
                    break;
                case START_DATE:
                    if(showWindow) {
                        showWindow = false;
                        return;
                    }
                    try{
                        startDate = dtUtils.formatDate(value.toString().trim(), DATE_SEPARATERS, DATE_FORMAT);
                        if(startDate == null) {
                            if( !value.toString().trim().equals(EMPTY_STRING)){
                                CoeusOptionPane.showInfoDialog(" Item  '"+value+ "' does not pass validation test.");
                                instituteRatesEditor.txtComponent.setText(null);
                                setRequestFocusInThread(row , column);
                            }else{
                                instRatesBean.setStartDate(null);
                                modified = true;
                                valueChanged = true;
                            }
                            return;
                        }
                        dt = dtFormat.parse(dateUtils.restoreDate(startDate, DATE_SEPARATERS));
                        java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
                        if( !sqlDate.equals(instRatesBean.getStartDate())){
                            modified = true;
                            valueChanged = true;
                        }
                    }catch (ParseException parseException) {
                        CoeusOptionPane.showInfoDialog(" Item  '"+value+ "' does not pass validation test.");
                        setRequestFocusInThread(row , column);
                        return ;
                    }
                    instRatesBean.setStartDate(new java.sql.Date(dt.getTime()));
                    break;
                case RATE:
                    double rate;
                    try{
                        //Commented the code for 3632 - Data Error in rates maintenance when current rate is 0
                        //If the user enter deletes the data in one row without entering 0, value saved to db was -1.
                        //Now if the value is null or .00 or empty string 0 will be stored to the db.
//                        if( ZERO_ENTERED &&  Double.parseDouble(value.toString()) == .00) {
//                            instRatesBean.setRate(0.0);
//                            ZERO_ENTERED = false;
//                            modified = true;
//                        }else if(!ZERO_ENTERED && Double.parseDouble(value.toString()) == .00){
//                            instRatesBean.setRate(-1);
                        if(value == null || value.toString().trim().equals(EMPTY_STRING) ||
                            Double.parseDouble(value.toString()) == .00){
                                rate = 0.0;
                                if(rate != instRatesBean.getRate()){
                                    instRatesBean.setRate(0.0);
                                    modified = true;
                                    valueChanged = true;
                                }    
                        }else if(value != null   && !value.toString().trim().equals(EMPTY_STRING)) {
                            rate = new Double(value.toString()).doubleValue();
                            if(rate != instRatesBean.getRate()){
                                instRatesBean.setRate(rate);
                                modified = true;
                                valueChanged = true;
                            }
                        }
                    }catch(NumberFormatException numberFormatException){
                        if(Double.parseDouble(value.toString()) ==  DEFAULT_INST_RATE){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RATE_MSG));
                            flag = true;
                            setRequestFocusInThread(row , column);
                            return;
                        }else{
                            CoeusOptionPane.showInfoDialog(" Item  '"+value+ "' does not pass validation test.");
                            flag = true;
                            setRequestFocusInThread(row , column);
                            return;
                        }
                    }
                    break;
                    
            }
            //Set the actype to U only if there is change in the data value
            if(instRatesBean.getAcType() == null && valueChanged){
                instRatesBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
             valueChanged = false;
            //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - end
        }
        
    }
    
    /*Editor for the institute rates table*/
    class InstituteRatesEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusTextField txtComponent;
        private CurrencyField txtRate;
        private JComboBox cmbRate;
        private int column;
        String message;
        
        public InstituteRatesEditor(){
            txtComponent = new CoeusTextField();
            txtRate = new CurrencyField();
            cmbRate = new JComboBox();
            
            txtRate.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent kEvent){
                    if( kEvent.getKeyCode() == KeyEvent.VK_0 || kEvent.getKeyCode() == KeyEvent.VK_NUMPAD0 ){
                        int selectedRow = instituteRatesForm.tblInstituteRate.getSelectedRow();
                        ZERO_ENTERED = true;
                        kEvent.consume();
                    }
                }
            });
        }
        
        /*returns cell editor value*/
        public Object getCellEditorValue() {
            switch(column){
                case START_DATE:
                    return txtComponent.getText();
                case RATE:
                    return txtRate.getText();
            }
            return txtComponent;
        }
        
        /*returns celleditor component*/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case START_DATE:
                    txtComponent.setDocument(new LimitedPlainDocument(12));
                    txtComponent.setHorizontalAlignment(JLabel.LEFT);
                    if(value != null){
                        txtComponent.setText(dateUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT));
                    }else{
                        txtComponent.setText(EMPTY_STRING);
                    }
                    return txtComponent;
                case RATE:
                    if(value != null){
                        if(((Double)value).doubleValue() == -1){
                            txtRate.setText("0.00");
                        }else{
                            txtRate.setText(value.toString());
                        }
                    }else{
                        txtRate.setText(EMPTY_STRING);
                    }
                    return txtRate;
            }
            return txtComponent;
        }
        
    }
    
    /*renderer for the institute rates table*/
    class InstituteRatesRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        
        private CoeusTextField txtComponent;
        private CurrencyField txtRate;
        private JLabel lblText;
        
        public InstituteRatesRenderer(){
            txtRate = new CurrencyField();
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new CoeusTextField();
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
            lblText.setBorder(bevelBorder);
            
        }
        
        /* returns renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            switch(column) {
                case HAND_ICON_COLUMN:
                    
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case ACTIVITY_TYPE:
                    setHorizontalAlignment(JLabel.LEFT);
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case FISCAL_YEAR:
                    setHorizontalAlignment(JLabel.LEFT);
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case CAMPUS_FLAG:
                    setHorizontalAlignment(JLabel.LEFT);
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case START_DATE:
                    lblText.setHorizontalAlignment(JLabel.LEFT);
                    if(functionType == TypeConstants.DISPLAY_MODE){
                        lblText.setBackground(PANEL_BACKGROUND_COLOR);
                    }else{
                        lblText.setBackground(Color.WHITE);
                    }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        value = dateUtils.formatDate(value.toString(), DATE_FORMAT);
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    
                    return lblText;
                case RATE:
                    lblText.setHorizontalAlignment(JLabel.RIGHT);
                    if(value != null) {
                        if(((Double)value).doubleValue() == -1 ){
                            String strZero = ".00";
                            txtRate.setText(strZero);
                            lblText.setText(strZero);
                        }else{
                             txtRate.setText(value.toString());
                             lblText.setText(txtRate.getText());
                        }
                    }

                    if(functionType == TypeConstants.DISPLAY_MODE){
                        setBackground(PANEL_BACKGROUND_COLOR);
                    }else{
                        setBackground(Color.WHITE);
                    }
                    return lblText;
                //Modified code for case #1748 by tarique start 8
                case UPDATE_TIME :
                    lblText.setHorizontalAlignment(JLabel.LEFT);
                    lblText.setBackground(PANEL_BACKGROUND_COLOR);
                    if(value.toString().equals(EMPTY_STRING)){
                        lblText.setText(value.toString());
                        return lblText;
                    }
                    lblText.setText(CoeusDateFormat.format(value.toString()));
                    return lblText;
                case UPDATE_USER :
                    setHorizontalAlignment(JLabel.LEFT);
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                //Modified code for case #1748 by tarique end 8
            }
            return null;
        }
    }
    
    /*to check whether any data is modified in the table*/
    public boolean isSaveRequired(){
        instituteRatesEditor.stopCellEditing();
        return modified;
    }
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = instituteRatesForm.tblInstituteRate.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = instituteRatesForm.tblInstituteRate.getActionMap().get(im.get(tab));
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
        instituteRatesForm.tblInstituteRate.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = instituteRatesForm.tblInstituteRate.getActionMap().get(im.get(shiftTab));
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
        instituteRatesForm.tblInstituteRate.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
    
    /**
     * To remove the instances and the listenrs
     */
    public void cleanUp(){
        instituteRatesForm = null;
        dlgInstituteRates = null;
        instituteRatesTableModel = null;
        instituteRatesEditor = null;
        instituteRatesRenderer = null;
        cvTableData = null;
        cvFilteredRateClass = null;
        cvRateClass = null;
        cvDeletedItem = null;
        cvRateType = null;
        instituteRatesBean = null;
        removeBeanUpdatedListener(this, InstituteRatesBean.class);

    }
    
}
