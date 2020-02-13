/*
 * CERatesController.java
 *
 * Created on August 17, 2007, 11:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.rates.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.rates.bean.CERatesBean;
import edu.mit.coeus.rates.gui.ValidCERatesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.FormattedDocument;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author  talarianand
 */
public class CERatesController implements ActionListener, ItemListener {
    
    public CoeusDlgWindow dlgCERates;
    public ValidCERatesForm ceRatesForm;
    private static final int WIDTH = 700;
    private static final int HEIGHT = 380;
    private static char VALID_CE_RATES = 'C';
    private static char COST_ELEMENT_LIST = 'E';
    private static char UPDATE_CE_RATES = 'U';
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/RatesMaintenanceServlet";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private CoeusVector cvRatesData = null;
    private CoeusVector cvRatesList;
    private CoeusVector cvCEList = null;
    private CoeusVector cvDeletedItem;
    private static final int HAND_ICON_COLUMN = 0;
    private static final int COST_ELEMENT = 1;
    private static final int DESCRIPTION = 2;
    private static final int FISCAL_YEAR = 3;
    private static final int START_DATE = 4;
    private static final int MONTHLY_RATE = 5;
    private static final String EMPTY_STRING = "";
    private boolean ZERO_ENTERED = false;
    private RatesInfoTableModel ratesTblModel;
    private CERatesEditor ceRatesEditor;
    private CERatesRenderer ceRatesRenderer;
    /*to set the back ground color*/
    private static final java.awt.Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    private String selUnitNumber = EMPTY_STRING;
    private String selUnitName = EMPTY_STRING;
    /*Are u sure u want to delete this row*/
    private static final String DELETE_CONFIRMATION = "ceRate_exceptionCode.1117";
    /*coeusMessage resource instance*/
    private CoeusMessageResources coeusMessageResources;
    private boolean modified = false;
    private boolean hasRights = false;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private DateUtils dtUtils;
    /** To specify the date format*/
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String INVALID_START_DATE = "ceRate_Date_exceptionCode.1114";
    private static final String DUPLICATE_INFORMATION = "ceRateDuplicate_exceptionCode.1116";
    
    /** Creates a new instance of CERatesController */
    public CERatesController(String selUnitNumber, String selUnitName) {
        this.selUnitNumber = selUnitNumber;
        this.selUnitName = selUnitName;
        dtUtils = new DateUtils();
        initComponents();
        ceRatesEditor = new CERatesEditor();
        ceRatesRenderer = new CERatesRenderer();
        cvDeletedItem = new CoeusVector();
        registerComponents();
        setTableEditors();
        setTableKeyTraversal();
        setFormData(null);
    }
    
    /**
     * Initializes the Form.
     */
    public void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        ceRatesForm = new ValidCERatesForm();
        
        dlgCERates = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgCERates.getContentPane().add(ceRatesForm);
        dlgCERates.setResizable(false);
        dlgCERates.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgCERates.setFont(CoeusFontFactory.getLabelFont());
        dlgCERates.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgCERates.getSize();
        dlgCERates.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        if(ceRatesForm.tblRatesList.getRowCount() > 0) {
            ceRatesForm.tblRatesList.setRowSelectionInterval(0, 0);
        } else {
            ceRatesForm.btnDelete.setEnabled(false);
        }
        dlgCERates.show();
    }
    
    
    /** 
     * This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        ceRatesForm.btnOk.addActionListener(this);
        ceRatesForm.btnCancel.addActionListener(this);
        ceRatesForm.btnDelete.addActionListener(this);
        ceRatesForm.btnAdd.addActionListener(this);
        ceRatesForm.cmbCostElement.addItemListener(this);
        ratesTblModel = new RatesInfoTableModel();
        ceRatesForm.tblRatesList.setModel(ratesTblModel);
        
        java.awt.Component[] components = {ceRatesForm.btnOk, ceRatesForm.btnCancel,
        ceRatesForm.btnAdd, ceRatesForm.btnDelete, ceRatesForm.tblRatesList, ceRatesForm.cmbCostElement
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        ceRatesForm.setFocusTraversalPolicy(traversePolicy);
        ceRatesForm.setFocusCycleRoot(true);
        
        dlgCERates.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                performCancelAction();
            }
        });
        
        dlgCERates.addWindowListener(new java.awt.event.WindowAdapter(){
            
            public void windowOpened(java.awt.event.WindowEvent we) {
                requestDefaultFocus();
            }
            
            public void windowClosing(java.awt.event.WindowEvent we){
                performCancelAction();
                return;
            }
        });
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        Vector vecCEList = fetchCostElements();
        cvCEList = (CoeusVector)vecCEList.get(0);
        Boolean objBoolean = (Boolean) vecCEList.get(1);
        hasRights = objBoolean.booleanValue();
        if(!hasRights) {
            ceRatesForm.btnAdd.setEnabled(false);
            ceRatesForm.btnDelete.setEnabled(false);
            ceRatesForm.btnOk.setEnabled(false);
            ceRatesForm.cmbCostElement.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
        ComboBoxBean comboBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
        ceRatesForm.cmbCostElement.addItem(comboBean);
        if(cvCEList != null && cvCEList.size() > 0) {
            CERatesBean dataBean = new CERatesBean();
            for(int index = 0; index < cvCEList.size(); index++) {
                dataBean = (CERatesBean) cvCEList.get(index);
                comboBean = new ComboBoxBean(dataBean.getCostElement(), dataBean.getDescription());
                ceRatesForm.cmbCostElement.addItem(comboBean);
            }
        }
        cvRatesData = fetchRatesData();
        if(cvRatesData != null && cvRatesData.size() > 0) {
            ratesTblModel.setData(cvRatesData);
        } else {
            cvRatesData = new CoeusVector();
        }
    }
    
    /**
     * Is used to fetch the cost elements from data base
     * @return Vector
     */
    
    private Vector fetchCostElements() {
        Vector vecCEList = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(COST_ELEMENT_LIST);
        requesterBean.setDataObject(selUnitNumber);
        
        AppletServletCommunicator appletServletCommunicator = new
        AppletServletCommunicator(CONNECTION_STRING, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return vecCEList;
        }
        
        if(responderBean.isSuccessfulResponse()) {
            vecCEList = (Vector) responderBean.getDataObject();
            return vecCEList;
        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return vecCEList;
        }
    }
    
    /**
     * Is used to fetch the valid ce rates from database
     * @return CoeusVector
     */
    
    private CoeusVector fetchRatesData() {
        CoeusVector cvData = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(VALID_CE_RATES);
        CERatesBean ceRatesBean = new CERatesBean();
        ceRatesBean.setUnitNumber(selUnitNumber);
        requesterBean.setDataObject(ceRatesBean);
        
        AppletServletCommunicator appletServletCommunicator = new
        AppletServletCommunicator(CONNECTION_STRING, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return cvData;
        }
        
        if(responderBean.isSuccessfulResponse()) {
            cvData = (CoeusVector) responderBean.getDataObject();
            dlgCERates.setTitle("Valid CE Rates for " +selUnitNumber+":"+selUnitName);
            return cvData;
        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return cvData;
        }
    }
    
    /** To set the default focus for the components depending
     * on the the function type */
    private void requestDefaultFocus() {
        ceRatesForm.cmbCostElement.requestFocus();
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            //blockEvents(true);
            dlgCERates.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if( source.equals(ceRatesForm.btnOk) ) {
                performOkAction();
            }else if( source.equals(ceRatesForm.btnCancel) ) {
                performCancelAction();
            } else if(source.equals(ceRatesForm.btnDelete)) {
                performDeleteAction();
            } else if(source.equals(ceRatesForm.btnAdd)) {
                performAddAction();
            }
        }finally{
            dlgCERates.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * Is used to perform the necessary operation when cancel button is pressed
     */
    
    private void performCancelAction() {
        ceRatesEditor.stopCellEditing();
        if(modified) {
            confirmClosing();
        } else {
            dlgCERates.dispose();
        }
    }
    
    /**
     * Is used to do the necessary operation bifore closing the form
     */
    
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                performOkAction();
            }else if(option == CoeusOptionPane.SELECTION_NO){
                dlgCERates.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    /**
     * Is used to do the necessary operations when add button is pressed.
     */
    
    private void performAddAction() {
        String costElement = ((ComboBoxBean) ceRatesForm.cmbCostElement.getSelectedItem()).getCode();
        String description = ((ComboBoxBean) ceRatesForm.cmbCostElement.getSelectedItem()).getDescription();
        HashMap hmData = new HashMap();
        int rowCount = ceRatesForm.tblRatesList.getRowCount();
        hmData.put("CostElement", costElement);
        hmData.put("Description", description);
        hmData.put("UnitNumber", selUnitNumber);
        hmData.put("UnitName", selUnitName);
        AddValidRatesController addRatesController = new AddValidRatesController(hmData, cvRatesData); 
        CERatesBean ceRatesBean = addRatesController.display();
        if(ceRatesBean == null) {
            return;
        }
        cvDeletedItem.addElement(ceRatesBean);
        cvRatesData.addElement(ceRatesBean);
        CoeusVector cvTempData = new CoeusVector();
        cvTempData = ratesTblModel.getData();
        cvTempData.addElement(ceRatesBean);
        ratesTblModel.setData(cvTempData);
        modified = true;
        
        if(rowCount==0){
            ratesTblModel.fireTableRowsInserted(rowCount, rowCount);
            ceRatesForm.btnDelete.setEnabled(true);
        }else{
            ratesTblModel.fireTableRowsInserted(rowCount, rowCount+1);
        }
        
        int lastRow = ceRatesForm.tblRatesList.getRowCount()-1;
        if(lastRow >= 0){
            ceRatesForm.tblRatesList.setRowSelectionInterval( lastRow, lastRow );
            ceRatesForm.tblRatesList.scrollRectToVisible(
            ceRatesForm.tblRatesList.getCellRect(lastRow ,0, true));
        }
        ceRatesForm.tblRatesList.editCellAt(lastRow, MONTHLY_RATE);
        ceRatesEditor.txtRate.requestFocus();
    }
    
    /**
     * Is used to save the ce rates data to the database
     */
    
    private void saveRatesData() {
        CoeusVector cvDataObject = new CoeusVector();
        if(cvDeletedItem != null && cvDeletedItem.size() > 0) {
            cvDataObject.addAll(cvDeletedItem);
//            cvDataObject.addAll(cvRatesData);
        } else {
            if(cvRatesData != null && cvRatesData.size() > 0) {
                cvDataObject.addAll(cvRatesData);
            } else {
                cvRatesData = new CoeusVector();
                cvDataObject.addAll(cvRatesData);
            }
        }
        cvDataObject = cvDataObject.filter(new NotEquals("acType", null));
        Hashtable htModified = new Hashtable();
        htModified.put(CERatesBean.class, cvDataObject);
        
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(UPDATE_CE_RATES);
        requesterBean.setDataObject(htModified);
        
        AppletServletCommunicator appletServletCommunicator = new
        AppletServletCommunicator(CONNECTION_STRING, requesterBean);
        
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
    }
    
    /*the action performed on the click of the delete cammand button*/
    private void performDeleteAction(){
        ceRatesEditor.stopCellEditing();
        CoeusVector cvDeleteData = new CoeusVector();
        cvDeleteData = ratesTblModel.getData();
        if(ceRatesForm.tblRatesList.getRowCount() == 0){
            return;
        }
        int selectedRow = ceRatesForm.tblRatesList.getSelectedRow();
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
                CERatesBean deletedBean = (CERatesBean)cvDeleteData.get(selectedRow);
                
                if(cvDeleteData!=null && cvDeleteData.size() > 0){
                    cvDeleteData.remove(selectedRow);
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedItem.addElement(deletedBean);
                    //                    cvRatesData.remove(selectedRow);
                    ratesTblModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    modified = true;
                }
                
            }
        }
        
        if(selectedRow >0){
            ceRatesForm.tblRatesList.setRowSelectionInterval(
            selectedRow-1,selectedRow-1);
            ceRatesForm.tblRatesList.scrollRectToVisible(
            ceRatesForm.tblRatesList.getCellRect(
            selectedRow -1 ,0, true));
        }else{
            if(ceRatesForm.tblRatesList.getRowCount()>0){
                ceRatesForm.tblRatesList.setRowSelectionInterval(0,0);
            } else {
                ceRatesForm.btnDelete.setEnabled(false);
            }
        }
        
    }
    
    /**
     * Is used to validate the form data
     * @return boolean value
     */
    
    private boolean validateData() {
        ceRatesEditor.stopCellEditing();
        CERatesBean ceRatesBean;
        if(cvDeletedItem == null || cvDeletedItem.size() == 0) {
            cvDeletedItem.addAll(cvRatesData);
        }
        CoeusVector cvData = new CoeusVector();
        Equals eqNull = new Equals("acType", null);
        NotEquals eqD = new NotEquals("acType", "D");
        Or eqNullAndD = new Or(eqNull, eqD);
        cvData = cvDeletedItem.filter(eqNullAndD);
        if(cvData != null && cvData.size() > 0) {
            for(int index = 0; index < cvData.size(); index++) {
                ceRatesBean = (CERatesBean) cvData.get(index);
                if(ceRatesBean.getMonthlyRate() == 0.00 ||
                ceRatesBean.getMonthlyRate() == .00 || ceRatesBean.getMonthlyRate() == 0.0
                || ceRatesBean.getMonthlyRate() == .0){
                    
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("ceRate_rate_exceptionCode.1112"));
                    ceRatesForm.tblRatesList.editCellAt(index,MONTHLY_RATE);
                    ceRatesEditor.txtRate.requestFocus();
                    ceRatesForm.tblRatesList.setRowSelectionInterval(index ,index );
                    ceRatesForm.tblRatesList.scrollRectToVisible(
                    ceRatesForm.tblRatesList.getCellRect(index, MONTHLY_RATE, true));
                    return false;
                } else {
                    String costElement = ceRatesBean.getCostElement();
                    String fiscalYear = ceRatesBean.getFiscalYear();
                    String unitNumber = ceRatesBean.getUnitNumber();
                    java.sql.Date modDate = ceRatesBean.getStartDate();
//                    if(isDuplicate(costElement, fiscalYear, unitNumber, modDate)) {
////                        ceRatesForm.tblRatesList.editCellAt(index,MONTHLY_RATE);
////                        ceRatesEditor.txtRate.requestFocus();
////                        ceRatesForm.tblRatesList.setRowSelectionInterval(index ,index );
////                        ceRatesForm.tblRatesList.scrollRectToVisible(
////                        ceRatesForm.tblRatesList.getCellRect(index, MONTHLY_RATE, true));
//                        return false;
//                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Is used to do the necessary operations when ok button is pressed
     */
    
    private void performOkAction() {
        ceRatesEditor.stopCellEditing();
        if(validateData()) {
            if(modified) {
                saveRatesData();
                dlgCERates.dispose();
            } else {
                dlgCERates.dispose();
            }
        }
    }
    
    /**
     * Is used to do the operations when an item is changed in the form
     * @param ItemEvent
     */
    
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        if(source.equals(ceRatesForm.cmbCostElement)) {
            String costElement = ((ComboBoxBean) ceRatesForm.cmbCostElement.getSelectedItem()).getCode();
            if(costElement != null && costElement.length() > 0) {
                if(hasRights) {
                    ceRatesForm.btnAdd.setEnabled(true);
                }
                Equals operator = new Equals("costElement", costElement);
                CoeusVector cvData = cvRatesData.filter(operator);
                if(cvData != null && cvData.size() > 0) {
                    ratesTblModel.setData(cvData);
                } else {
                    ratesTblModel.setData(new CoeusVector());
                }
            } else {
                CoeusVector cvRates = fetchRatesData();
                ratesTblModel.setData(cvRates);
                ceRatesForm.btnAdd.setEnabled(false);
            }
            if(ceRatesForm.tblRatesList.getRowCount() > 0) {
                ceRatesForm.tblRatesList.setRowSelectionInterval(0,0);
                ceRatesForm.btnDelete.setEnabled(true);
            } else {
                ceRatesForm.btnDelete.setEnabled(false);
            }
        }
    }
    
    /*to set the renderers and editors to the table*/
    private void setTableEditors(){
        ceRatesForm.tblRatesList.setRowHeight(22);
        ceRatesForm.tblRatesList.setShowHorizontalLines(false);
        ceRatesForm.tblRatesList.setShowVerticalLines(false);
        JTableHeader tableHeader = ceRatesForm.tblRatesList.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        ceRatesForm.tblRatesList.setOpaque(false);
        ceRatesForm.tblRatesList.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = ceRatesForm.tblRatesList.getColumnModel().getColumn(HAND_ICON_COLUMN);
        column.setPreferredWidth(10);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = ceRatesForm.tblRatesList.getColumnModel().getColumn(COST_ELEMENT);
        column.setPreferredWidth(75);
        column.setResizable(true);
        column.setCellEditor(ceRatesEditor);
        column.setCellRenderer(ceRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = ceRatesForm.tblRatesList.getColumnModel().getColumn(DESCRIPTION);
        column.setPreferredWidth(180);
        column.setResizable(true);
        column.setCellEditor(ceRatesEditor);
        column.setCellRenderer(ceRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = ceRatesForm.tblRatesList.getColumnModel().getColumn(FISCAL_YEAR);
        column.setPreferredWidth(75);
        column.setResizable(true);
        column.setCellEditor(ceRatesEditor);
        column.setCellRenderer(ceRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = ceRatesForm.tblRatesList.getColumnModel().getColumn(START_DATE);
        column.setPreferredWidth(90);
        column.setResizable(true);
        column.setCellEditor(ceRatesEditor);
        column.setCellRenderer(ceRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = ceRatesForm.tblRatesList.getColumnModel().getColumn(MONTHLY_RATE);
        column.setPreferredWidth(90);
        column.setResizable(true);
        column.setCellEditor(ceRatesEditor);
        column.setCellRenderer(ceRatesRenderer);
        tableHeader.setReorderingAllowed(false);
        
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","" },
            {"1","costElement" },
            {"2","description" },
            {"3","fiscalYear" },
            {"4","startDate" },
            {"5","monthlyRate"},
            
        };
        boolean sort = false;
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
                if(scr.equals(ceRatesForm.tblRatesList.getTableHeader())){
                    CoeusVector cvData = ratesTblModel.getData();
                    if(cvData != null && cvData.size()>0 &&
                    nameBeanId [vColIndex][1].length() >1 ){
                        ((CoeusVector)cvData).sort(nameBeanId [vColIndex][1],sort);
                        if(sort)
                            sort = false;
                        else
                            sort = true;
                        ratesTblModel.fireTableRowsUpdated(0, ratesTblModel.getRowCount());
                    }
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
            
        }
    }
    
    class RatesInfoTableModel extends AbstractTableModel {
        String colNames[] = {"", "Cost Element", "Description", "Fiscal Year", "Effective Date", "Monthly Rate" };
        Class[] colTypes = new Class[] {String.class, String.class, String.class, String.class, String.class, Integer.class};
        
        RatesInfoTableModel() {
        }
        
        public boolean isCellEditable(int row, int column) {
            if(!hasRights) {
                return false;
            }
            switch(column){
                case HAND_ICON_COLUMN:
                    return false;
                case COST_ELEMENT:
                    return false;
                case DESCRIPTION:
                    return false;
                case FISCAL_YEAR:
                    return false;
                case START_DATE:
                    return true;
                case MONTHLY_RATE:
                    return true;
            }
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }
        
        public void setData(CoeusVector cvRates) {
            cvRatesList = cvRates;
            fireTableDataChanged();
        }
        
        public CoeusVector getData() {
            return cvRatesList;
        }
        
        public int getRowCount() {
            if(cvRatesList != null && cvRatesList.size() > 0) {
                return cvRatesList.size();
            } else {
                return 0;
            }
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            //MailActionInfoBean mailBean = (MailActionInfoBean) vecPersonInfo.get(row);
            CERatesBean ratesBean = (CERatesBean) cvRatesList.get(row);
            switch(column) {
                case HAND_ICON_COLUMN:
                    return EMPTY_STRING;
                case COST_ELEMENT:
                    return ratesBean.getCostElement();
                case DESCRIPTION:
                    return ratesBean.getDescription();
                case FISCAL_YEAR:
                    return ratesBean.getFiscalYear();
                case START_DATE:
                    return ratesBean.getStartDate();
                case MONTHLY_RATE:
                    return new Double(ratesBean.getMonthlyRate());
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
        }
        
        public void setValueAt(Object value,int row,int column) {
            double rate;
            Date date = null;
            String message = null;
            String strDate = null;
            if(cvRatesList == null || cvRatesList.size() == 0) {
                return;
            }
            CERatesBean ratesBean = (CERatesBean) cvRatesList.get(row);
            switch(column) {
                case COST_ELEMENT:
                    ratesBean.setCostElement(value.toString());
                    break;
                case DESCRIPTION:
                    ratesBean.setDescription(value.toString());
                    break;
                case FISCAL_YEAR:
                    ratesBean.setFiscalYear(value.toString());
                    break;
                case START_DATE:
                    try{
                        strDate = dtUtils.formatDate(
                        value.toString(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
//                        return ;
                    }
                    catch (CoeusException coeusException) {
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
//                        return ;
                    }
                    if(ratesBean.getStartDate() == date) {
                        return;
                    }
                    ratesBean.setStartDate(new java.sql.Date(date.getTime()));
                    modified = true;
                    break;
                case MONTHLY_RATE:
                    rate = Double.parseDouble(value.toString());
                    if(ratesBean.getMonthlyRate() == rate)break;
                    ratesBean.setMonthlyRate(Double.parseDouble(value.toString()));
                    modified = true;
                    break;
            }
            if(modified) {
                if(ratesBean.getAcType() == null) {
                    ratesBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        
    }
    /*Editor for the institute rates table*/
    class CERatesEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusTextField txtComponent;
        private JTextField txtDateComponent;
        private CoeusTextField txtRate;
        private int column;
        String message;
        private DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        
        public CERatesEditor(){
            txtComponent = new CoeusTextField();
            txtDateComponent = new JTextField();
            txtRate = new CoeusTextField();
            
            txtDateComponent.setDocument(new LimitedPlainDocument(15));
            txtDateComponent.setHorizontalAlignment(JLabel.LEFT);
            
            txtComponent.setDocument(new LimitedPlainDocument(12));
            txtComponent.setHorizontalAlignment(JLabel.LEFT);
            
            decimalFormat.setMinimumIntegerDigits(0);
            decimalFormat.setMaximumIntegerDigits(10);

            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setDecimalSeparatorAlwaysShown(true);


            CoeusTextField textField = txtRate;
            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,textField);
            formattedDocument.setNegativeAllowed(true);
            textField.setDocument(formattedDocument);
            textField.setText(".00");
            textField.setHorizontalAlignment(JFormattedTextField.RIGHT);
            
            txtRate.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent kEvent){
                    if( kEvent.getKeyCode() == KeyEvent.VK_0 || kEvent.getKeyCode() == KeyEvent.VK_NUMPAD0 ){
                        int selectedRow = ceRatesForm.tblRatesList.getSelectedRow();
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
                    return txtDateComponent.getText();
                case MONTHLY_RATE:
                    String rate = txtRate.getText();
                    rate = rate.replaceAll(",", "");
                    return new Double(Double.parseDouble(rate));
            }
            return txtComponent;
        }
        
        /*returns celleditor component*/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case START_DATE:
                    txtDateComponent.setText(dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT));
                    return txtDateComponent;
                case MONTHLY_RATE:
                    if(value != null){
                        if(((Double)value).doubleValue() == -1){
                            txtRate.setText("0.00");
                        }else{
//                            txtRate.setText(value.toString());
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
    class CERatesRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        
        private CoeusTextField txtComponent;
        //        private CurrencyField txtRate;
        private CoeusTextField txtRate;
        private JLabel lblText;
        private JTextField txtDateComponent;
        private DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        
        public CERatesRenderer(){
            //            txtRate = new CurrencyField();
            txtRate = new CoeusTextField();
            txtDateComponent = new JTextField();
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new CoeusTextField();
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
            lblText.setBorder(bevelBorder);
            
            decimalFormat.setMinimumIntegerDigits(0);
            decimalFormat.setMaximumIntegerDigits(10);

            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setDecimalSeparatorAlwaysShown(true);


            CoeusTextField textField = txtRate;
            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,textField);
            formattedDocument.setNegativeAllowed(true);
            textField.setDocument(formattedDocument);
            textField.setText(".00");
            textField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        }
        
        /* returns renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            switch(column) {
                case HAND_ICON_COLUMN:
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case COST_ELEMENT:
                    setHorizontalAlignment(JLabel.LEFT);
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case DESCRIPTION:
                    setHorizontalAlignment(JLabel.LEFT);
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case FISCAL_YEAR:
                    setHorizontalAlignment(JLabel.LEFT);
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case START_DATE:
                    lblText.setHorizontalAlignment(JLabel.LEFT);
                    value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                    txtDateComponent.setText(value.toString());
                    lblText.setText(txtDateComponent.getText());
                    lblText.setBackground(Color.WHITE);
                    return lblText;
                case MONTHLY_RATE:
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
                    
                    lblText.setBackground(Color.WHITE);
                    return lblText;
            }
            return null;
        }
    }
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = ceRatesForm.tblRatesList.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = ceRatesForm.tblRatesList.getActionMap().get(im.get(tab));
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
        ceRatesForm.tblRatesList.getActionMap().put(im.get(tab), tabAction);
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = ceRatesForm.tblRatesList.getActionMap().get(im.get(shiftTab));
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
        ceRatesForm.tblRatesList.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    private boolean isDuplicate(String costElement, String fiscalYear, String unitNumber, java.sql.Date startDate) {
        boolean isDuplicate = false;
        Equals eqCostElement;
        Equals eqFiscalYear;
        Equals eqRate;
        Equals eqStartDate;
        And fiscalYearAndCostElement;
        And fiscalYearCostElementRate;
        And fisYearCEStrDate;
        CoeusVector cvDuplicates = new CoeusVector();
        if(cvRatesData != null && cvRatesData.size() > 0) {
            eqCostElement = new Equals("costElement", costElement);
            eqFiscalYear = new Equals("fiscalYear", fiscalYear);
            eqRate = new Equals("unitNumber", unitNumber);
            eqStartDate = new Equals("startDate", startDate);
            fiscalYearAndCostElement = new And(eqRate, eqCostElement);
            fiscalYearCostElementRate = new And(fiscalYearAndCostElement, eqFiscalYear);
            fisYearCEStrDate = new And(fiscalYearCostElementRate, eqStartDate);
            cvDuplicates = cvRatesData.filter(fisYearCEStrDate);
        }
        if(cvDuplicates != null && cvDuplicates.size() > 0) {
            isDuplicate = true;
            CoeusOptionPane.showInfoDialog(
                  coeusMessageResources.parseMessageKey(DUPLICATE_INFORMATION));
        }
        return isDuplicate;
    }
    
}
