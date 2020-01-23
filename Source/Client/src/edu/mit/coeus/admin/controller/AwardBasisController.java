/*
 * AwardBasisController.java
 *
 * Created on November 18, 2004, 2:37 PM
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.AwardBasisForm;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.award.bean.ValidBasisPaymentBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.MultipleTableColumnSorter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;
import javax.swing.UIManager;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author  jinum
 */
public class AwardBasisController extends AdminController implements
ActionListener, ItemListener{
    
    private AwardBasisForm awardBasisForm;
    public CoeusDlgWindow dlgAwardBasis;
    private CoeusAppletMDIForm mdiForm;
    private AwardBasisTableModel awardBasisTableModel;
    
    private CoeusMessageResources  coeusMessageResources;
    private ValidBasisPaymentBean validBasisPaymentBean;
    
    private static final int WIDTH = 585;
    private static final int HEIGHT = 300;
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final String TITLE = "Valid Award Basis of Payment";
    private static final String EMPTY = "";
    private static final String AWARD_BASIS_SERVLET = "/AdminMaintenanceServlet";
    private static final String SELECT_A_ROW = "adminAward_exceptionCode.1351";
    private static final String DELETE_CONFIRMATION = "adminAward_exceptionCode.1352";
    private static final String CANCEL_CONFIRMATION = "adminAward_exceptionCode.1353";
    
    private CoeusVector cvAwardBasisFinal;
    private CoeusVector cvAwardBasis;
    private CoeusVector cvAddAwardBasis;
    private CoeusVector cvFilteredAwardBasis;
    private CoeusVector cvFilteredAddAwardBasis;
    private CoeusVector cvSelectedData;
    private boolean hasRight = true;
    private boolean dataModified = false;
    private MultipleTableColumnSorter sorter;
    
    /** Creates a new instance of AwardBasisController */
    public AwardBasisController(CoeusAppletMDIForm mdiForm) {
        try{
            this.mdiForm = mdiForm;
            coeusMessageResources = CoeusMessageResources.getInstance();
            registerComponents();
            setFormData(null);
            setColumnData();
            formatFields();
            postInitComponents();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    /**
     * Registering the components
     * @return void
     **/
    public void registerComponents() {
        awardBasisForm = new AwardBasisForm();
        awardBasisTableModel = new AwardBasisTableModel();
        awardBasisForm.btnOK.addActionListener(this);
        awardBasisForm.btnCancel.addActionListener(this);
        awardBasisForm.btnAdd.addActionListener(this);
        awardBasisForm.btnDelete.addActionListener(this);
        awardBasisForm.cmbAwardType.addItemListener(this);
        awardBasisForm.tblBasis.setModel(awardBasisTableModel);
        
        java.awt.Component[] components = {awardBasisForm.cmbAwardType,awardBasisForm.btnOK,awardBasisForm.btnCancel,awardBasisForm.btnAdd,awardBasisForm.btnDelete};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(components);
        awardBasisForm.setFocusTraversalPolicy(traversalPolicy);
        awardBasisForm.setFocusCycleRoot(true);
    }
    
    /**
     * Method after initializing the components
     * @return void
     **/
    private void postInitComponents() {
        dlgAwardBasis = new CoeusDlgWindow(mdiForm);
        dlgAwardBasis.getContentPane().add(awardBasisForm);
        dlgAwardBasis.setResizable(false);
        dlgAwardBasis.setModal(true);
        dlgAwardBasis.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardBasis.setSize(WIDTH, HEIGHT);
        Dimension dlgLocation = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgAwardBasisSize = dlgAwardBasis.getSize();
        dlgAwardBasis.setLocation((dlgLocation.width/2 - dlgAwardBasisSize.width/2), (dlgLocation.height/2 - dlgAwardBasisSize.height/2));
        dlgAwardBasis.setTitle(TITLE);
        
        dlgAwardBasis.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performCancelAction();
                return;
            }
        });
        dlgAwardBasis.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardBasis.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                performCancelAction();
                return;
            }
        });
        dlgAwardBasis.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
    }
    
    /**
     * Display the Dialog
     * @return void
     **/
    public void display() {
        dlgAwardBasis.setVisible(true);
    }
    
    /**
     * Format the Fields or Controls.
     * @return void
     **/
    public void formatFields() {
        
        if(!hasRight){
            awardBasisForm.btnAdd.setEnabled(false);
            awardBasisForm.btnDelete.setEnabled(false);
            awardBasisForm.btnOK.setEnabled(false);
            awardBasisForm.tblBasis.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    /**
     * To get the controlled UI
     * @return java.awt.Component
     **/
    public java.awt.Component getControlledUI() {
        return awardBasisForm;
    }
    
    /**
     * To set the form data
     * @param data Object
     * @return void
     **/
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        
        Hashtable htData  = getAwardBasis();
        if(htData != null){
            cvAwardBasisFinal = new CoeusVector();
            cvAwardBasis = new CoeusVector();
            cvAddAwardBasis = new CoeusVector();
            CoeusVector cvAwardType = new CoeusVector();
            try{
                hasRight = ((Boolean)htData.get(new Integer(2))).booleanValue();
                cvAwardType = (CoeusVector)htData.get(new Integer(1));
                setComboData(cvAwardType);
                awardBasisForm.cmbAwardType.setShowCode(true);
                cvAwardBasis = (CoeusVector)htData.get(new Integer(0));
                cvAwardBasisFinal = (CoeusVector)ObjectCloner.deepCopy(cvAwardBasis);
                cvAddAwardBasis = (CoeusVector)htData.get(new Integer(3));
                performAwardTypeChangeOperation(((ComboBoxBean)awardBasisForm.cmbAwardType.getSelectedItem()).getCode());
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
    /**
     * To set the ComboBox data
     * @param data CoeusVector
     * @return void
     **/
    private void setComboData(CoeusVector data){
        if( ( data != null ) && ( data.size() > 0 )
        && ( awardBasisForm.cmbAwardType.getItemCount() == 0 ) ) {
            int size = data.size();
            for(int index = 0 ; index < size ; index++){
                awardBasisForm.cmbAwardType.addItem(
                (ValidBasisPaymentBean)data.elementAt(index));
            }
            validBasisPaymentBean= (ValidBasisPaymentBean)data.elementAt(0);
            awardBasisForm.cmbAwardType.setSelectedItem(validBasisPaymentBean);
        }
    }
    
    /**
     * To get the form data
     * @return Object
     **/
    public Object getFormData() {
        return null;
    }
    
    /**
     * save form data
     * @return void
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * setting up the default focus
     * @return void
     */
    public void requestDefaultFocus(){
        awardBasisForm.cmbAwardType.requestFocus();
    }
    
    /** This method will specify the state change of an item
     * @param itemEvent ItemEvent
     * @return void
     */
    public void itemStateChanged(ItemEvent itemEvent){
        Object source = itemEvent.getSource();
        if (source != null && source.equals(awardBasisForm.cmbAwardType)) {
            performAwardTypeChangeOperation(((ComboBoxBean)awardBasisForm.cmbAwardType.getSelectedItem()).getCode());
        }
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
            if(source != null){
                if (source.equals(awardBasisForm.btnOK)) {
                    dlgAwardBasis.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    performOKOperation();
                }else if (source.equals(awardBasisForm.btnCancel)) {
                    performCancelAction();
                }else if (source.equals(awardBasisForm.btnAdd)) {
                    dlgAwardBasis.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    performAddRow();
                }else if (source.equals(awardBasisForm.btnDelete)) {
                    performDeleteOperation();
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }finally{
            dlgAwardBasis.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    /** This method will be used when AwardType is changed
     * @param code String
     * @return void
     */
    private void performAwardTypeChangeOperation(String code){
        
        validBasisPaymentBean = (ValidBasisPaymentBean)awardBasisForm.cmbAwardType.getSelectedItem();
        Equals eqAwardType =  new Equals("awardTypeCode" ,new Integer(code));
        cvFilteredAwardBasis = new CoeusVector();
        if(cvAwardBasis != null){
            cvFilteredAwardBasis = cvAwardBasis.filter(eqAwardType);
        }
        if(cvFilteredAwardBasis != null){
            awardBasisTableModel.setData(cvFilteredAwardBasis);
            if (cvFilteredAwardBasis.size() > 0){
                awardBasisForm.tblBasis.setRowSelectionInterval(0,0);
            }
        }
        filterBasisOfPayment();
    }
    
    /** This method will filter BasisOfPayment according to the BasisOfPaymentCode
     * @return void
     */
    private void filterBasisOfPayment(){
        cvFilteredAddAwardBasis = new CoeusVector();
        if(cvFilteredAwardBasis != null && cvAddAwardBasis != null){
            for(int i=0;i<cvAddAwardBasis.size();i++){
                int count=0;
                for(int j=0;j<cvFilteredAwardBasis.size();j++){
                    if(((ValidBasisPaymentBean)cvFilteredAwardBasis.elementAt(j)).getCode().equals(""+((ValidBasisMethodPaymentBean)cvAddAwardBasis.elementAt(i)).getBasisOfPaymentCode())){
                        break;
                    }else{
                        count++;
                    }
                }
                if(count>=cvFilteredAwardBasis.size()){
                    cvFilteredAddAwardBasis.add(cvAddAwardBasis.elementAt(i));
                }
            }
        }
    }
    
    /** This method will be used for Save or OK operation
     * @return void
     */
    private void performOKOperation() throws CoeusException{
        
        try{
            if(dataModified && cvAwardBasisFinal != null){
                RequesterBean requesterBean = new RequesterBean();
                requesterBean.setFunctionType('B');//"UPDATE_VALID_AWARD_BASIS"
                requesterBean.setDataObject(cvAwardBasisFinal);
                AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
                appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + AWARD_BASIS_SERVLET);
                appletServletCommunicator.setRequest(requesterBean);
                appletServletCommunicator.send();
                ResponderBean responderBean = appletServletCommunicator.getResponse();
                if(responderBean!= null){
                    if(!responderBean.isSuccessfulResponse()){
                        throw new CoeusException(responderBean.getMessage(), 1);
                    }else{
                        cvAwardBasis = new CoeusVector();
                        cvAwardBasis = (CoeusVector)responderBean.getDataObject();
                    }
                }
            }
            dlgAwardBasis.setVisible(false);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** This method will be used for Cancel Action
     * @return void
     */
    private void performCancelAction() {
        try{
            if(dataModified){
                String mesg = coeusMessageResources.parseMessageKey(CANCEL_CONFIRMATION);
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg+"  "),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    performOKOperation();
                } else if(selectedOption == CoeusOptionPane.OPTION_OK_CANCEL){
                    dlgAwardBasis.setVisible(false);
                }
            }else{
                dlgAwardBasis.setVisible(false);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** This method will be used for adding a new Row
     * @return void
     */
    private void performAddRow() {
        try{
            if(validBasisPaymentBean != null && cvFilteredAddAwardBasis != null){
                AddAwardBasisController addAwardBasisController = new AddAwardBasisController(mdiForm, validBasisPaymentBean);
                addAwardBasisController.setFormData(cvFilteredAddAwardBasis);
                addAwardBasisController.setColumnData();
                cvSelectedData = new CoeusVector();
                cvSelectedData = addAwardBasisController.display();
                if(cvSelectedData != null && cvSelectedData.size()>0){
                    for(int i=0;i<cvSelectedData.size();i++){
                        ValidBasisMethodPaymentBean methodPaymentBean = (ValidBasisMethodPaymentBean)cvSelectedData.elementAt(i);
                        ValidBasisPaymentBean paymentBean = new ValidBasisPaymentBean();
                        paymentBean.setCode(""+methodPaymentBean.getBasisOfPaymentCode());
                        paymentBean.setDescription(methodPaymentBean.getDescription());
                        paymentBean.setAwardTypeCode(validBasisPaymentBean.getAwardTypeCode());
                        paymentBean.setAwardTypeDescription(validBasisPaymentBean.getAwardTypeDescription());
                        paymentBean.setAcType(TypeConstants.INSERT_RECORD);
                        cvAwardBasis.add(paymentBean);
                        ValidBasisPaymentBean searchedBean = (ValidBasisPaymentBean)search(paymentBean);
                        if(searchedBean != null){
                            searchedBean.setAcType(null);
                        }else{
                            cvAwardBasisFinal.add(paymentBean);
                        }
                    }
                    performAwardTypeChangeOperation(((ComboBoxBean)awardBasisForm.cmbAwardType.getSelectedItem()).getCode());
                    dataModified = true;
                }
            }
        }catch(Exception exception){exception.printStackTrace();}
    }
    
    /** This method will be used to delete a selected row
     * @return void
     */
    private void performDeleteOperation() {
        int rowIndex = awardBasisForm.tblBasis.getSelectedRow();
        if (rowIndex < 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW)+"  ");
            return;
        }
        if (rowIndex >= 0) {
            String mesg = coeusMessageResources.parseMessageKey(DELETE_CONFIRMATION);
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg+"  "),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                
                ValidBasisPaymentBean bean = (ValidBasisPaymentBean)cvFilteredAwardBasis.get(rowIndex);
                if(bean != null){
                    searchAndDelete(bean);
                    awardBasisTableModel.fireTableRowsDeleted(rowIndex,rowIndex);
                    performAwardTypeChangeOperation(((ComboBoxBean)awardBasisForm.cmbAwardType.getSelectedItem()).getCode());
                    dataModified = true;
                }
            }
        }
        
    }
    
    /** This method will search the passed ValidBasisPaymentBean and set it to delete
     * @param object Object
     * @return void
     */
    private void searchAndDelete(Object object){
        if(object != null && object.getClass().getName().equals("edu.mit.coeus.award.bean.ValidBasisPaymentBean")){
            ValidBasisPaymentBean bean = (ValidBasisPaymentBean)object;
            ValidBasisPaymentBean searchedBean = null;
            for(int i=0;i<cvAwardBasis.size();i++){
                if(((ValidBasisPaymentBean)cvAwardBasis.elementAt(i)).getCode().equals(bean.getCode()) && ((ValidBasisPaymentBean)cvAwardBasis.elementAt(i)).getAwardTypeCode()==(bean.getAwardTypeCode())){
                    searchedBean = (ValidBasisPaymentBean)search(bean);
                    if(searchedBean != null){
                        if(searchedBean.getAcType() == null || searchedBean.getAcType().equals(null)){
                            searchedBean.setAcType(TypeConstants.DELETE_RECORD);
                        }else if(searchedBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            cvAwardBasisFinal.removeElementAt(searchIndex(bean));
                        }
                    }
                    cvAwardBasis.removeElementAt(i);
                    break;
                }
            }
        }
    }
    
    /** This method will search the passed ValidBasisPaymentBean and return it
     * @param object Object
     * @return void
     */
    private Object search(Object object){
        if(object != null && object.getClass().getName().equals("edu.mit.coeus.award.bean.ValidBasisPaymentBean")){
            for(int i=0;i<cvAwardBasisFinal.size();i++){
                if(((ValidBasisPaymentBean)cvAwardBasisFinal.elementAt(i)).getCode().equals(((ValidBasisPaymentBean)object).getCode()) && ((ValidBasisPaymentBean)cvAwardBasisFinal.elementAt(i)).getAwardTypeCode()==(((ValidBasisPaymentBean)object).getAwardTypeCode())){
                    return cvAwardBasisFinal.elementAt(i);
                }
            }
        }
        return null;
    }
    
    /** This method will search the passed ValidBasisPaymentBean and return the index of it
     * @param object Object
     * @return index int
     */
    private int searchIndex(Object object){
        if(object != null && object.getClass().getName().equals("edu.mit.coeus.award.bean.ValidBasisPaymentBean")){
            for(int i=0;i<cvAwardBasisFinal.size();i++){
                if(((ValidBasisPaymentBean)cvAwardBasisFinal.elementAt(i)).getCode().equals(((ValidBasisPaymentBean)object).getCode()) && ((ValidBasisPaymentBean)cvAwardBasisFinal.elementAt(i)).getAwardTypeCode()==(((ValidBasisPaymentBean)object).getAwardTypeCode())){
                    return i;
                }
            }
        }
        return -1;
    }
    
    /** This method will get the required data from Server
     * @return data Hashtable
     */
    private Hashtable getAwardBasis()throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('A');//"GET_VALID_AWARD_BASIS"
        Hashtable data=null;
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + AWARD_BASIS_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean!= null){
            if(!responderBean.isSuccessfulResponse()){
                throw new CoeusException(responderBean.getMessage(), 1);
            }else{
                data = (Hashtable)responderBean.getDataObject();
            }
        }
        return data;
    }
    
    /**
     * Setting up the column data
     * @return void
     **/
    private void setColumnData(){
        JTableHeader tableHeader = awardBasisForm.tblBasis.getTableHeader();
        awardBasisForm.tblBasis.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        if( sorter == null ) {
                sorter = new MultipleTableColumnSorter((TableModel)awardBasisForm.tblBasis.getModel());
                sorter.setTableHeader(awardBasisForm.tblBasis.getTableHeader());
                awardBasisForm.tblBasis.setModel(sorter);
                
        }
        
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        //tableHeader.addMouseListener(new ColumnHeaderListener());
        // setting up the table columns
        TableColumn column = awardBasisForm.tblBasis.getColumnModel().getColumn(CODE_COLUMN);
        column.setPreferredWidth(50);
        awardBasisForm.tblBasis.setRowHeight(22);
        
        column = awardBasisForm.tblBasis.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setPreferredWidth(430);
        
        awardBasisForm.tblBasis.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        if (cvFilteredAwardBasis != null && cvFilteredAwardBasis.size() > 0) {
            awardBasisForm.tblBasis.setRowSelectionInterval(0,0);
        }
        
    }
    
    /**
     * validate method
     * @return boolean
     * @throws edu.mit.coeus.exception.CoeusUIException
     **/
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    /** This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    public class ColumnHeaderListener extends MouseAdapter {
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
                if(cvFilteredAwardBasis != null && cvFilteredAwardBasis.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvFilteredAwardBasis).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    awardBasisTableModel.fireTableRowsUpdated(
                    0, awardBasisTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    /**
     * This is an inner class represents the table model for the Award Basis
     * screen table
     **/
    public class AwardBasisTableModel extends AbstractTableModel {
        
        // represents the column names of the table
        private String colName[] = {"Code","Description"};
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class};
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
            if (cvFilteredAwardBasis == null){
                return 0;
            } else {
                return cvFilteredAwardBasis.size();
            }
        }
        
        /**
         * To set the data for the model.
         * @param cvAwardBasis CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvFilterAwardBasis) {
            cvFilteredAwardBasis = cvFilterAwardBasis;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
            ValidBasisPaymentBean validBasisPaymentBean =
            (ValidBasisPaymentBean)cvFilteredAwardBasis.get(rowIndex);
            if (validBasisPaymentBean != null) {
                switch(columnIndex) {
                    case CODE_COLUMN:
                        return validBasisPaymentBean.getCode();
                    case DESCRIPTION_COLUMN:
                        return validBasisPaymentBean.getDescription();
                    default:
                        return validBasisPaymentBean.getCode();
                }
            }
            return EMPTY;
        }
        
        /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
    }
}
