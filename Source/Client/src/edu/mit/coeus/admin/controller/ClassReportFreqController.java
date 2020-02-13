/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */



package edu.mit.coeus.admin.controller;

import java.awt.event.*;
import edu.mit.coeus.brokers.*;

import edu.mit.coeus.admin.gui.ClassReportFreqForm;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.bean.ReportBean;
import edu.mit.coeus.bean.ValidReportClassReportFrequencyBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/*
 * ClassReportFreqController.java
 * @author  ajaygm
 * Created on November 22, 2004, 10:45 AM
 */

public class ClassReportFreqController extends AdminController
implements ActionListener,ItemListener,ListSelectionListener{
    
    //Creates an instance of Query Engine
    private QueryEngine queryEngine;
    
    //Creates an instance Coeus Message Resources
    private CoeusMessageResources coeusMessageResources;
    
    //Creates an instance of ClassReportFreqForm
    private ClassReportFreqForm classReportFreqForm;
    
    
    //Creates an instance of dialog
    private CoeusDlgWindow dlgClassReportFreqForm;
    
    //Creates an instance of Coeus Appplet MDI From
    private CoeusAppletMDIForm mdiForm;
    
    //Function Type to get valid report Frequency
    private static final char GET_CLASS_REPORT_FREQUENCY = 'D';
    
    //Function Type to save class report Frequency */
    private static final char  UPDATE_CLASS_REPORT_FREQUENCY = 'H';
    
    //Represents the string for conneting to the servlet and getting data from server
    private static final String GET_SERVLET = "/AdminMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    //Holds the rights
    boolean hasRight;
    
    //Vectors for holding the Type, Class, Frequency data
    CoeusVector cvFrequency,cvClass,cvType;
    
    //Vectors which holds the unique data of the type and freq
    CoeusVector cvTempType;
    CoeusVector cvFilteredData;
    
    //Vector which holds the data of all the type for a given class
    CoeusVector cvFreqValuesOfAllRows;
    
    //Vectors which holds the deleted data
    CoeusVector cvDeletedFreq = new CoeusVector();
    CoeusVector cvDeletedType = new CoeusVector();
    
    //Vectors which holds the data to be saved
    CoeusVector cvDataSave = new CoeusVector();
    
    //vector which holds the data to be sent to the Add Report freq
    CoeusVector cvReportTypeAdd;
    
    //Holds the temp data
    Hashtable htTempData = new Hashtable();
    
    //To get the clicked button
    private final int OK_CLICKED = 1;
    
    
    //For Setting the table model for Freq Table
    private ClassReportFreqTableModel classReportFreqTableModel;
    
    //For Setting the table model Type Table
    private ClassReportTypeTableModel classReportTypeTableModel;
    
    //To set the window title
    private static final String WINDOW_TITLE =  "Class Report Frequency";
    
    //For Setting the dimentions of teh dialog
    private static final int WIDTH = 625;
    private static final int HEIGHT = 380;
    
    //Column indexes for the table
    private static final int CODE_INDEX = 0;
    private static final int DESCRIPTION_INDEX = 1;
    
    //Empty string
    private static final String EMPTY_STRING = "";
    
    private MultipleTableColumnSorter sorter;
    
    //Validation Messages.
    private static final String SELECT_TYPE_ROW_TO_DELETE = "classReportFrequency_exceptionCode.1301";
    private static final String TYPE_DELETE_CONFIRMATION = "Do you want to delete Report type";/*"classReportFrequency_exceptionCode.1302";*/
    private static final String SELECT_FREQ_ROW_TO_DELETE = "classReportFrequency_exceptionCode.1303";
    private static final String FREQ_DELETE_CONFIRMATION = "Do you want to delete Frequency";/*"classReportFrequency_exceptionCode.1304";*/
    private static final String SAVE_CHANGES = "classReportFrequency_exceptionCode.1305";
    
    /** Creates a new instance of ClassReportFreqController */
    public ClassReportFreqController(CoeusAppletMDIForm mdiForm) {
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.mdiForm = mdiForm;
        registerComponents();
        try{
            setFormData(null);
        }catch (CoeusException ce){
            ce.printStackTrace();
        }
        postInitComponents();
        formatFields();
        display();
    }
    
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgClassReportFreqForm = new CoeusDlgWindow(mdiForm);
        dlgClassReportFreqForm.setResizable(false);
        dlgClassReportFreqForm.setModal(true);
        dlgClassReportFreqForm.getContentPane().add(classReportFreqForm);
        
        dlgClassReportFreqForm.setTitle(WINDOW_TITLE);
        dlgClassReportFreqForm.setFont(CoeusFontFactory.getLabelFont());
        dlgClassReportFreqForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgClassReportFreqForm.getSize();
        dlgClassReportFreqForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgClassReportFreqForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        //code for disposing the window
        dlgClassReportFreqForm.addEscapeKeyListener(
        new javax.swing.AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                performCancelAction();
            }
        });
        
        //        dlgClassReportFreqForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        dlgClassReportFreqForm.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                performCancelAction();
            }
        });
        //code for disposing the window ends
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display(){
        dlgClassReportFreqForm.setVisible(true);
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        if(!hasRight()){
            classReportFreqForm.btnOK.setEnabled(false);
            classReportFreqForm.btnAdd.setEnabled(false);
            classReportFreqForm.btnTypeDelete.setEnabled(false);
            classReportFreqForm.btnFreqDelete.setEnabled(false);
            
            classReportFreqForm.cmbClass.setBackground(
            (Color) UIManager.getDefaults().get("Panel.background"));
            
            classReportFreqForm.tblType.setBackground(
            (Color) UIManager.getDefaults().get("Panel.background"));
            
            classReportFreqForm.tblFrequency.setBackground(
            (Color) UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    /** An overridden method of the controller
     * @return classReportFreqForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return classReportFreqForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        //Add listeners to all the buttons
        classReportFreqForm = new ClassReportFreqForm();
        classReportFreqForm.btnOK.addActionListener(this);
        classReportFreqForm.btnCancel.addActionListener(this);
        classReportFreqForm.btnAdd.addActionListener(this);
        classReportFreqForm.btnFreqDelete.addActionListener(this);
        classReportFreqForm.btnTypeDelete.addActionListener(this);
        classReportFreqForm.cmbClass.addItemListener(this);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { classReportFreqForm.cmbClass,
        classReportFreqForm.btnOK,classReportFreqForm.btnCancel,
        classReportFreqForm.btnAdd,classReportFreqForm.btnTypeDelete,
        classReportFreqForm.tblType,classReportFreqForm.tblFrequency,
        classReportFreqForm.btnFreqDelete
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        classReportFreqForm.setFocusTraversalPolicy(traversePolicy);
        classReportFreqForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        classReportTypeTableModel = new ClassReportTypeTableModel();
        classReportFreqForm.tblType.setModel(classReportTypeTableModel);
        
        classReportFreqTableModel = new ClassReportFreqTableModel();
        classReportFreqForm.tblFrequency.setModel(classReportFreqTableModel);
        
        ListSelectionModel selectionModel = classReportFreqForm.tblType.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        classReportFreqForm.tblType.setSelectionModel(selectionModel);
        
        /*Performs the OK clicked action if enter is pressed*/
        classReportFreqForm.cmbClass.addKeyListener (new KeyAdapter (){
            public void keyPressed (KeyEvent kEvent){
                if( kEvent.getKeyCode () == KeyEvent.VK_ENTER){
                    classReportFreqForm.btnOK.doClick ();
                    kEvent.consume ();
                }
            }
        });
        
        classReportFreqForm.tblType.addKeyListener (new KeyAdapter (){
            public void keyPressed (KeyEvent kEvent){
                if( kEvent.getKeyCode () == KeyEvent.VK_ENTER){
                    classReportFreqForm.btnOK.doClick ();
                    kEvent.consume ();
                }
            }
        });
        
        classReportFreqForm.tblFrequency.addKeyListener (new KeyAdapter (){
            public void keyPressed (KeyEvent kEvent){
                if( kEvent.getKeyCode () == KeyEvent.VK_ENTER){
                    classReportFreqForm.btnOK.doClick ();
                    kEvent.consume ();
                }
            }
        });
        
        setTableEditors();
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == itemEvent.DESELECTED){
            return ;
        }
        
        Object source = itemEvent.getSource();
        
        if(source.equals(classReportFreqForm.cmbClass)){
            setDataToTypeTable();
        }
    }
    
    /** To set the default focus for the component
     */
    public void requestDefaultFocus(){
        classReportFreqForm.cmbClass.requestFocusInWindow();
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
       
        try{
            dlgClassReportFreqForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
            requesterBean.setFunctionType(UPDATE_CLASS_REPORT_FREQUENCY);
            requesterBean.setDataObject(cvDataSave);

            AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
            comm.setRequest(requesterBean);
            comm.send();
            responderBean = comm.getResponse();

            if(!responderBean.isSuccessfulResponse()) {
                throw new CoeusException();
            }
        }finally{
            dlgClassReportFreqForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        HashMap hmServerData = new HashMap();
        cvFrequency = new CoeusVector();
        cvClass = new CoeusVector();
        cvType = new CoeusVector();
        cvReportTypeAdd = new CoeusVector();
        
        hmServerData = getDataFromServer();
        boolean right = ((Boolean)hmServerData.get(KeyConstants.VIEW_RIGHTS)).booleanValue();
        setHasRight(right);
        
        cvFrequency = (CoeusVector)hmServerData.get(FrequencyBean.class);
        cvClass = (CoeusVector)hmServerData.get(KeyConstants.REPORT_CLASS);
        cvType = (CoeusVector)hmServerData.get(ValidReportClassReportFrequencyBean.class);
        cvReportTypeAdd = (CoeusVector)hmServerData.get(ReportBean.class);
        setDataToClassCombo();
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        
        if(source.equals(classReportFreqForm.btnOK)){
            if(isSaveRequired()){
                performUpdate();
                try{
                    saveFormData();
                }catch (CoeusException ce){
                    ce.printStackTrace();
                }
                dlgClassReportFreqForm.dispose();
            }else{
                dlgClassReportFreqForm.dispose();
            }
            
        }else if(source.equals(classReportFreqForm.btnAdd)){
            showAddReportFreq();
        }else if(source.equals(classReportFreqForm.btnCancel)){
            performCancelAction();
        }else if(source.equals(classReportFreqForm.btnFreqDelete)){
            performFreqDelete();
        }else if(source.equals(classReportFreqForm.btnTypeDelete)){
            performTypeDelete();
        }
    }
    
    //Sets the table properties
    private void setTableEditors(){
        TableColumn column;
        JTableHeader tableHeader;
        tableHeader = classReportFreqForm.tblType.getTableHeader();
        
         if( sorter == null ) {
                sorter = new MultipleTableColumnSorter((TableModel)classReportFreqForm.tblType.getModel());
                sorter.setTableHeader(classReportFreqForm.tblType.getTableHeader());
                classReportFreqForm.tblType.setModel(sorter);
        }
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        classReportFreqForm.tblType.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        classReportFreqForm.tblType.setRowHeight(22);
        classReportFreqForm.tblType.setOpaque(false);
        classReportFreqForm.tblType.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        if(hasRight()){
            classReportFreqForm.tblType.setShowHorizontalLines(false);
            classReportFreqForm.tblType.setShowVerticalLines(false);
        }
        
        int preferWidth[] = {50, 192};
        for(int index = 0; index < preferWidth.length; index++) {
            column = classReportFreqForm.tblType.getColumnModel().getColumn(index);
            column.setPreferredWidth(preferWidth[index]);
        }
        
        tableHeader = classReportFreqForm.tblFrequency.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
       // if( sorter == null ) {
                sorter = new MultipleTableColumnSorter((TableModel)classReportFreqForm.tblFrequency.getModel());
                sorter.setTableHeader(classReportFreqForm.tblFrequency.getTableHeader());
                classReportFreqForm.tblFrequency.setModel(sorter);
       // }
        
        classReportFreqForm.tblFrequency.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        classReportFreqForm.tblFrequency.setRowHeight(22);
        classReportFreqForm.tblFrequency.setOpaque(false);
        
        classReportFreqForm.tblFrequency.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        if(hasRight()){
            classReportFreqForm.tblFrequency.setShowHorizontalLines(false);
            classReportFreqForm.tblFrequency.setShowVerticalLines(false);
        }
        
        TableColumn tblColumn;
        /*Bug Fix:N002*/
        int prefWidth[] = {50, 192};
        for(int index = 0; index < prefWidth.length; index++) {
            tblColumn = classReportFreqForm.tblFrequency.getColumnModel().getColumn(index);
            tblColumn.setPreferredWidth(prefWidth[index]);
        }
        
    }//End setTableEditors
    
    
    //Table model for Type Table
    class ClassReportTypeTableModel extends AbstractTableModel{
        
        private CoeusVector cvTempTypeData = new CoeusVector();
        private String columnName[] = {"Code","Description"};
        private Class colClass[] = {String.class,String.class};
        
        /**
         * Returns the number of columns in the model. A
         * <code>JTable</code> uses this method to determine how many columns it
         * should create and display by default.
         *
         * @return the number of columns in the model
         * @see #getRowCount
         */
        public int getColumnCount() {
            return columnName.length;
        }
        
        /**
         * Returns the most specific superclass for all the cell values
         * in the column.  This is used by the <code>JTable</code> to set up a
         * default renderer and editor for the column.
         *
         * @param columnIndex  the index of the column
         * @return the common ancestor class of the object values in the model.
         */
        public Class getColumnClass(int columnIndex) {
            return colClass[columnIndex];
        }
        
        /**
         * Returns the name of the column at <code>columnIndex</code>.  This is used
         * to initialize the table's column header name.  Note: this name does
         * not need to be unique; two columns in a table can have the same name.
         *
         * @param	columnIndex	the index of the column
         * @return  the name of the column
         */
        public String getColumnName(int columnIndex) {
            return columnName[columnIndex];
        }
        
        public int getRowCount() {
            return cvTempTypeData.size();
        }
        
        public void setData(CoeusVector cvTypeData){
            cvTempTypeData = cvTypeData;
        }
        
        /**
         * Sets the value in the cell at <code>columnIndex</code> and
         * <code>rowIndex</code> to <code>aValue</code>.
         *
         * @param	aValue		 the new value
         * @param	rowIndex	 the row whose value is to be changed
         * @param	columnIndex 	 the column whose value is to be changed
         * @see #getValueAt
         * @see #isCellEditable
         */
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }
        
        /**
         * Returns the value for the cell at <code>columnIndex</code> and
         * <code>rowIndex</code>.
         *
         * @param	rowIndex	the row whose value is to be queried
         * @param	columnIndex 	the column whose value is to be queried
         * @return	the value Object at the specified cell
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(cvTempTypeData.size()<1){
                return null ;
            }
            ValidReportClassReportFrequencyBean reportClassBean =
            (ValidReportClassReportFrequencyBean)cvTempTypeData.get(rowIndex);
            switch(columnIndex){
                case CODE_INDEX:
                    String strRepCode = ""+reportClassBean.getReportCode();
                    return strRepCode;
                    
                case DESCRIPTION_INDEX:
                    return reportClassBean.getReportDescription();
            }
            return EMPTY_STRING;
        }
        
        /**
         * Returns true if the cell at <code>rowIndex</code> and
         * <code>columnIndex</code>
         * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
         * change the value of that cell.
         *
         * @param	rowIndex	the row whose value to be queried
         * @param	columnIndex	the column whose value to be queried
         * @return	true if the cell is editable
         * @see #setValueAt
         */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }//End ClassReportTypeTableModel
    
    //Table model for Freq Table
    class ClassReportFreqTableModel extends AbstractTableModel{
        
        private CoeusVector cvTempFreqData = new CoeusVector();
        private String columnName[] = {"Code","Description"};
        private Class colClass[] = {String.class,String.class};
        
        /**
         * Returns the number of columns in the model. A
         * <code>JTable</code> uses this method to determine how many columns it
         * should create and display by default.
         *
         * @return the number of columns in the model
         * @see #getRowCount
         */
        public int getColumnCount() {
            return columnName.length;
        }
        
        /**
         * Returns the most specific superclass for all the cell values
         * in the column.  This is used by the <code>JTable</code> to set up a
         * default renderer and editor for the column.
         *
         * @param columnIndex  the index of the column
         * @return the common ancestor class of the object values in the model.
         */
        public Class getColumnClass(int columnIndex) {
            return colClass[columnIndex];
        }
        
        /**
         * Returns the name of the column at <code>columnIndex</code>.  This is used
         * to initialize the table's column header name.  Note: this name does
         * not need to be unique; two columns in a table can have the same name.
         *
         * @param	columnIndex	the index of the column
         * @return  the name of the column
         */
        public String getColumnName(int columnIndex) {
            return columnName[columnIndex];
        }
        
        public int getRowCount() {
            return cvTempFreqData.size();
        }
        
        public void setData(CoeusVector cvFreqData){
            cvTempFreqData.removeAllElements();
            cvTempFreqData.addAll(cvFreqData);
        }
        
        /**
         * Sets the value in the cell at <code>columnIndex</code> and
         * <code>rowIndex</code> to <code>aValue</code>.
         *
         * @param	aValue		 the new value
         * @param	rowIndex	 the row whose value is to be changed
         * @param	columnIndex 	 the column whose value is to be changed
         * @see #getValueAt
         * @see #isCellEditable
         */
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }
        
        /**
         * Returns the value for the cell at <code>columnIndex</code> and
         * <code>rowIndex</code>.
         *
         * @param	rowIndex	the row whose value is to be queried
         * @param	columnIndex 	the column whose value is to be queried
         * @return	the value Object at the specified cell
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(cvTempFreqData.size()<1){
                return null ;
            }
            ValidReportClassReportFrequencyBean reportClassBean =
            (ValidReportClassReportFrequencyBean)cvTempFreqData.get(rowIndex);
            
            switch(columnIndex){
                case CODE_INDEX:
                    String strFreqCode = ""+reportClassBean.getFrequencyCode();
                    return strFreqCode;
                    
                case DESCRIPTION_INDEX:
                    return reportClassBean.getFrequencyDescription();
            }
            return EMPTY_STRING;
        }
        
        /**
         * Returns true if the cell at <code>rowIndex</code> and
         * <code>columnIndex</code>
         * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
         * change the value of that cell.
         *
         * @param	rowIndex	the row whose value to be queried
         * @param	columnIndex	the column whose value to be queried
         * @return	true if the cell is editable
         * @see #setValueAt
         */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
        
        
    }//End ClassReportFreqTableModel
    
    //Gets the Class, Type,Freq data from server
    private HashMap getDataFromServer() throws CoeusException{
        HashMap hmData = new HashMap();
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_CLASS_REPORT_FREQUENCY);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            hmData  = (HashMap)responderBean.getDataObject();
        }else{
            throw new CoeusException();
        }
        return hmData ;
    }
    
    /**
     * Getter for property hasRight.
     * @return Value of property hasRight.
     */
    public boolean hasRight() {
        return hasRight;
    }
    
    /**
     * Setter for property hasRight.
     * @param hasRight New value of property hasRight.
     */
    public void setHasRight(boolean hasRight) {
        this.hasRight = hasRight;
    }
    
    //Sets the data to the Class Combobox
    private void setDataToClassCombo(){
        CoeusVector cvTempClass = new CoeusVector();
        cvTempClass.addAll(cvClass);
        classReportFreqForm.cmbClass.setModel(new DefaultComboBoxModel(cvTempClass));
        classReportFreqForm.cmbClass.setShowCode(true);
        setDataToTypeTable();
    }//End of setDataToClassCombo
    
    //Sets the data to the Type Table
    private void setDataToTypeTable(){
        if(!checkTempData()){
            cvTempType = new CoeusVector();
            cvTempType.addAll(cvType);
            ComboBoxBean cmbClass = (ComboBoxBean)classReportFreqForm.cmbClass.getSelectedItem();
            String classCode = cmbClass.getCode();
            
            if(!classCode.equals(EMPTY_STRING)){
                Equals eqFrequency = new Equals("reportClassCode", new Integer(classCode));
                cvTempType = cvType.filter(eqFrequency);
            }
            
            cvFilteredData = new CoeusVector();
            
            for (int index = 0 ;index < cvTempType.size() ;index++){
                ValidReportClassReportFrequencyBean validReportCRFBean =
                (ValidReportClassReportFrequencyBean)cvTempType.get(index);
                Equals eqCode = new Equals("reportCode" ,new Integer(validReportCRFBean.getReportCode()));
                CoeusVector cvData = cvFilteredData.filter(eqCode);
                if(cvData.size()<=0){
                    cvFilteredData.add(validReportCRFBean);
                }
            }
            
            if(cvFilteredData.size()>0){
                classReportTypeTableModel.setData(cvFilteredData);
                classReportTypeTableModel.fireTableDataChanged();
                cvFreqValuesOfAllRows = getFreqValuesOfAllRows(cvFilteredData);
                classReportFreqForm.tblType.setRowSelectionInterval(0,0);
            }else{
                CoeusVector cvDummy = new CoeusVector();
                cvFreqValuesOfAllRows.removeAllElements();
                classReportTypeTableModel.setData(cvDummy);
                classReportTypeTableModel.fireTableDataChanged();
            }
            setDataToFreqTable();
        }else{
            ComboBoxBean cmbClassBean =  (ComboBoxBean)classReportFreqForm.cmbClass.getSelectedItem();
            String classKey = cmbClassBean.getDescription().trim();
            CoeusVector cvData = (CoeusVector)htTempData.get(classKey);
            this.cvFreqValuesOfAllRows = (CoeusVector)cvData.get(0);
            this.cvFilteredData = (CoeusVector)cvData.get(1);
            
            if(cvFilteredData.size()>0){
                classReportTypeTableModel.setData(cvFilteredData);
                classReportTypeTableModel.fireTableDataChanged();
                classReportFreqForm.tblType.setRowSelectionInterval(0,0);
            }
            
            setDataToFreqTable();
        }
    }//End of setDataToTypeTable
    
    //Sets the data to the Freq Table
    private void setDataToFreqTable(){
        if(cvFilteredData.size()>0){
            int row = classReportFreqForm.tblType.getSelectedRow();
            if(row == -1){
                return ;
            }
            
            CoeusVector cvFre = (CoeusVector)cvFreqValuesOfAllRows.get(row);
            classReportFreqTableModel.setData(cvFre);
            classReportFreqTableModel.fireTableDataChanged();
            classReportFreqForm.tblFrequency.setRowSelectionInterval(0,0);
        }else{
            CoeusVector cvDummy = new CoeusVector();
            classReportFreqTableModel.setData(cvDummy);
            classReportFreqTableModel.fireTableDataChanged();
        }
    }//End of setDataToFreqTable
    
    public void showAddReportFreq(){
        CoeusVector cvClassAndAllFreq = new CoeusVector();
        CoeusVector cvSelType = null;
        ComboBoxBean cmbClassBean = (ComboBoxBean)classReportFreqForm.cmbClass.getSelectedItem();
        ComboBoxBean cmbTypeBean = null;
        
        dlgClassReportFreqForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if(cvFreqValuesOfAllRows!=null && cvFreqValuesOfAllRows.size()>0){
            int typeRow = classReportFreqForm.tblType.getSelectedRow();
            cvSelType = (CoeusVector)cvFreqValuesOfAllRows.get(typeRow);
        }
        
        if(cvSelType!=null && cvSelType.size()>0){
            ValidReportClassReportFrequencyBean validReportCRFBean=
            (ValidReportClassReportFrequencyBean)cvSelType.get(0);
            cmbTypeBean = new ComboBoxBean(""+validReportCRFBean.getReportCode(),validReportCRFBean.getReportDescription());
        }
        
        cvClassAndAllFreq.add(0,cmbClassBean);
        cvClassAndAllFreq.add(1,cvFreqValuesOfAllRows);
        cvClassAndAllFreq.add(2,cmbTypeBean);
        
        dlgClassReportFreqForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
        AddReportFreqController addReportFreqController =
        new AddReportFreqController(mdiForm,cvReportTypeAdd,cvFrequency,cvClassAndAllFreq);
        
        int BUTTON_CLICKED = addReportFreqController.getButtonClicked();
        CoeusVector cvSelFreq = new CoeusVector();
        if(BUTTON_CLICKED == OK_CLICKED){
            if(addReportFreqController.isNewTypeAdded()){
                ReportBean newReport =
                (ReportBean)addReportFreqController.getNewSelectedType();
                cvSelFreq = addReportFreqController.getSelectedFreq();
                refreshTypeAndFreq(cvSelFreq,newReport);
            }else{
                ReportBean reportBean =
                (ReportBean)addReportFreqController.getNewSelectedType();
                cvSelFreq = addReportFreqController.getSelectedFreq();
                refreshFreqTable(cvSelFreq,reportBean);
            }
        }
    }//End showAddReportFreq
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        if( !e.getValueIsAdjusting() ){
            int selectedRow = classReportFreqForm.tblType.getSelectedRow();
            if (selectedRow != -1) {
                setDataToFreqTable();
            }
        }
    }
    
    //Gets unique freq values of all the rows the type.
    //This is done since for the add report freq,
    //should filter freq values which pesent and send the.
    private CoeusVector getFreqValuesOfAllRows(CoeusVector cvAllData){
        CoeusVector cvAlldata = cvAllData;
        CoeusVector cvFreqValues = new CoeusVector();
        if(cvAllData!=null && cvAllData.size()>0){
            for(int index = 0 ; index<cvAlldata.size();index++){
                ValidReportClassReportFrequencyBean validReportCRFBean =
                (ValidReportClassReportFrequencyBean)cvAllData.get(index);
                
                int repClassCode = validReportCRFBean.getReportClassCode();
                int reportCode = validReportCRFBean.getReportCode();
                Equals eqRepClass = new Equals("reportClassCode" ,new Integer(repClassCode));
                Equals eqRepCode = new Equals("reportCode" ,new Integer(reportCode));
                And eqRepClassAndeqRepCode = new And(eqRepClass,eqRepCode);
                CoeusVector cvData  = cvTempType.filter(eqRepClassAndeqRepCode);
                if(cvData != null && cvData.size()>0){
                    cvFreqValues.add(index,cvData);
                }
            }
        }
        return cvFreqValues;
    }
    
    //Method to set newly added freq data to the freq table
    private void refreshFreqTable(CoeusVector cvSelFreq,ReportBean reportBean){
        int typeRow = -1;
        for(int index = 0;index < cvFreqValuesOfAllRows.size();index++){
            
            Equals eqRepCode = new Equals("reportCode",new Integer(reportBean.getCode()));
            CoeusVector cvFreqData = (CoeusVector)cvFreqValuesOfAllRows.get(index);
            CoeusVector cvData = cvFreqData.filter(eqRepCode);
            if(cvData!=null && cvData.size()>0){
                typeRow = index;
                break;
            }
        }
        
        if(typeRow == -1){
            return ;
        }
        
        if(cvSelFreq != null && cvSelFreq.size()>0){
            CoeusVector cvFreq = (CoeusVector)cvFreqValuesOfAllRows.get(typeRow);
            ValidReportClassReportFrequencyBean validReportCRFBean =
            (ValidReportClassReportFrequencyBean)cvFreq.get(0);
            CoeusVector cvConvBean =  convertBeanType(cvSelFreq,validReportCRFBean);
            cvFreq.addAll(cvConvBean);
            setSaveRequired(true);
            classReportFreqForm.tblType.setRowSelectionInterval(typeRow,typeRow);
            classReportFreqTableModel.setData(cvFreq);
            classReportFreqTableModel.fireTableDataChanged();
            classReportFreqForm.tblFrequency.setRowSelectionInterval(0,0);
            holdTempData();
        }
    }
    
    //Method to set newly added freq and type data to the freq and type table
    private void refreshTypeAndFreq(CoeusVector cvSelFreq,ReportBean reportBean){
        CoeusVector cvNewFreq = new CoeusVector();
        ComboBoxBean cmbClassBean = new ComboBoxBean();
        cmbClassBean = (ComboBoxBean)classReportFreqForm.cmbClass.getSelectedItem();
        
        int reportClassCode = Integer.parseInt(cmbClassBean.getCode().toString());
        String reportClassDesc = cmbClassBean.getDescription();
        
        int reportCode = Integer.parseInt(reportBean.getCode().toString());
        String reportDesc = reportBean.getDescription();
        
        ValidReportClassReportFrequencyBean newCRFBean = null;
        
        for(int index =0 ;index < cvSelFreq.size(); index++){
            newCRFBean = new ValidReportClassReportFrequencyBean();
            
            FrequencyBean freqBean =  (FrequencyBean)cvSelFreq.get(index);
            
            String freqCode = freqBean.getCode();
            
            newCRFBean.setFrequencyCode(Integer.parseInt(freqCode));
            newCRFBean.setFrequencyDescription(freqBean.getDescription());
            
            newCRFBean.setAcType("I");
            
            newCRFBean.setReportClassCode(reportClassCode);
            newCRFBean.setReportClassDescription(reportClassDesc);
            newCRFBean.setReportCode(reportCode);
            newCRFBean.setReportDescription(reportDesc);
            cvNewFreq.add(newCRFBean);
        }
        
        int sizeOfAllfreq = cvFreqValuesOfAllRows.size();
        int sizeOfFilteredData = cvFilteredData.size();
        cvFreqValuesOfAllRows.add(sizeOfAllfreq, cvNewFreq);
        cvFilteredData.add(sizeOfFilteredData,newCRFBean);
        
        setSaveRequired(true);
        
        classReportTypeTableModel.setData(cvFilteredData);
        classReportTypeTableModel.fireTableDataChanged();
        classReportFreqForm.tblType.setRowSelectionInterval(sizeOfFilteredData,sizeOfFilteredData);
        
        holdTempData();
    }
    
    //Used to convert bean type for newly added frequency for an exsisting type
    //from FerquencyBean to ValidReportClassReportFrequencyBean
    private CoeusVector convertBeanType(CoeusVector cvSelFreq ,
    ValidReportClassReportFrequencyBean validReportCRFBean){
        
        CoeusVector cvFreqReturn = new CoeusVector();
        
        for(int index =0 ;index < cvSelFreq.size(); index++){
            ValidReportClassReportFrequencyBean newCRFBean =
            new ValidReportClassReportFrequencyBean();
            
            FrequencyBean freqBean =  (FrequencyBean)cvSelFreq.get(index);
            
            String freqCode = freqBean.getCode();
            
            newCRFBean.setFrequencyCode(Integer.parseInt(freqCode));
            newCRFBean.setFrequencyDescription(freqBean.getDescription());
            
            newCRFBean.setAcType("I");
            newCRFBean.setReportClassCode(validReportCRFBean.getReportClassCode());
            newCRFBean.setReportClassDescription(validReportCRFBean.getReportClassDescription());
            newCRFBean.setReportCode(validReportCRFBean.getReportCode());
            newCRFBean.setReportDescription(validReportCRFBean.getReportDescription());
            cvFreqReturn.add(newCRFBean);
        }
        return cvFreqReturn;
    }
    
    //For deleting the frequency
    private void performFreqDelete(){
        int freqRow = classReportFreqForm.tblFrequency.getSelectedRow();
        int typeRow = classReportFreqForm.tblType.getSelectedRow();
        String strFreq = "";
        
        if(freqRow == -1){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            (SELECT_FREQ_ROW_TO_DELETE)));
            return ;
        }
        
        if(cvFreqValuesOfAllRows != null && cvFreqValuesOfAllRows.size()>0){
            CoeusVector cvTempData = (CoeusVector)cvFreqValuesOfAllRows.get(typeRow);
            if(cvTempData != null && cvTempData.size()>0){
                ValidReportClassReportFrequencyBean reportFrequencyBean
                = (ValidReportClassReportFrequencyBean)cvTempData.get(freqRow);
                strFreq = reportFrequencyBean.getFrequencyDescription();
            }
        }
        
        if(freqRow != -1 && freqRow >= 0){
            String mesg = FREQ_DELETE_CONFIRMATION+" - "+strFreq;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            mesg,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                CoeusVector cvDelFreq = (CoeusVector)cvFreqValuesOfAllRows.get(typeRow);
                ValidReportClassReportFrequencyBean delCRFBean =
                (ValidReportClassReportFrequencyBean)cvDelFreq.get(freqRow);
                
                if(delCRFBean.getAcType() == null ||
                !TypeConstants.INSERT_RECORD.equals(delCRFBean.getAcType().trim())){
                    
                    delCRFBean.setAcType("D");
                    cvDeletedFreq.add(delCRFBean);
                }
                
                cvDelFreq.removeElementAt(freqRow);
                if(cvDelFreq.size() == 0){
                    cvFilteredData.remove(typeRow);
                    cvFreqValuesOfAllRows.removeElementAt(typeRow);
                    //Added for internal Bug fix start
                    classReportFreqTableModel.setData(cvFreqValuesOfAllRows);
                    classReportFreqTableModel.fireTableDataChanged();
                    //Bug fix End
                    classReportTypeTableModel.fireTableRowsDeleted(typeRow, typeRow);
                    
                    int rowCount = classReportFreqForm.tblType.getRowCount();
                    if(rowCount > 0){
                        classReportFreqForm.tblType.setRowSelectionInterval(0,0);
                    }
                }else{
                    classReportFreqTableModel.setData(cvDelFreq);
                    classReportFreqTableModel.fireTableDataChanged();
                }
                
                setSaveRequired(true);
                holdTempData();
                
                int rowCount = classReportFreqForm.tblFrequency.getRowCount();
                if(rowCount > 0){
                    classReportFreqForm.tblFrequency.setRowSelectionInterval(0,0);
                }
            }
        }
    }//End performFreqDelete
    
    //For deleting the type
    private void performTypeDelete(){
        int typeRow = classReportFreqForm.tblType.getSelectedRow();
        String strType = "";
        
        if(typeRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            (SELECT_TYPE_ROW_TO_DELETE)));
            return ;
        }
        
        if(cvFreqValuesOfAllRows != null && cvFreqValuesOfAllRows.size()>0){
            CoeusVector cvTempData = (CoeusVector)cvFreqValuesOfAllRows.get(typeRow);
            if(cvTempData != null && cvTempData.size()>0){
                ValidReportClassReportFrequencyBean reportFrequencyBean
                = (ValidReportClassReportFrequencyBean)cvTempData.get(0);
                strType = reportFrequencyBean.getReportDescription();
            }
        }
        
        if(typeRow != -1 && typeRow >= 0){
            String mesg = TYPE_DELETE_CONFIRMATION+" - "+strType;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            mesg,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                CoeusVector cvDelType = (CoeusVector)cvFreqValuesOfAllRows.get(typeRow);
                for(int index=0 ; index < cvDelType.size(); index++ ){
                    
                    ValidReportClassReportFrequencyBean delCRFBean =
                    (ValidReportClassReportFrequencyBean)cvDelType.get(index);
                    
                    if(delCRFBean.getAcType() == null ||
                    !TypeConstants.INSERT_RECORD.equals(delCRFBean.getAcType().trim())){
                        delCRFBean.setAcType("D");
                        cvDeletedType.add(delCRFBean);
                    }
                }
                cvFilteredData.removeElementAt(typeRow);
                cvFreqValuesOfAllRows.removeElementAt(typeRow);
                if(cvFilteredData.size() == 0){
                    CoeusVector cvDummyData = new CoeusVector();
                    classReportFreqTableModel.setData(cvDummyData);
                    classReportFreqTableModel.fireTableDataChanged();
                }
                //                classReportTypeTableModel.setData(cvFilteredData);
                classReportTypeTableModel.fireTableRowsDeleted(typeRow,typeRow);
                //                classReportTypeTableModel.fireTableDataChanged();
                int rowCount = classReportFreqForm.tblType.getRowCount();
                
                setSaveRequired(true);
                holdTempData();
                
                if(rowCount > 0){
                    classReportFreqForm.tblType.setRowSelectionInterval(0,0);
                }
            }
        }
    }
    
    //Performed before save.Puts all the data to a Coeusvector
    private void performUpdate(){
        
        //Extracting the data of all deleted freq rows from the vector
        for(int k = 0;k < cvDeletedFreq.size(); k++){
            ValidReportClassReportFrequencyBean validCRFBean =
            (ValidReportClassReportFrequencyBean)cvDeletedFreq.get(k);
            cvDataSave.add(validCRFBean);
        }
        
        //Extracting the data of all deleted Type rows from the vector
        for(int k = 0;k < cvDeletedType.size(); k++){
            ValidReportClassReportFrequencyBean validCRFBean =
            (ValidReportClassReportFrequencyBean)cvDeletedType.get(k);
            cvDataSave.add(validCRFBean);
        }
        
        //Extracting the data for all the Report Class (TYPE) rows from the vector
        for(int i = 0;i < cvFreqValuesOfAllRows.size();i++){
            CoeusVector cvFreqData = (CoeusVector)cvFreqValuesOfAllRows.get(i);
            for(int j = 0;j < cvFreqData.size(); j++){
                ValidReportClassReportFrequencyBean validCRFBean =
                (ValidReportClassReportFrequencyBean)cvFreqData.get(j);
                cvDataSave.add(validCRFBean);
            }//inner for
        }//outer for
    }//End performUpdate
    
    
    //Retains the modified values of the tables while the changing Report Class
    //before saving (clicking OK)
    private void holdTempData(){
        ComboBoxBean cmbClassBean =  (ComboBoxBean)classReportFreqForm.cmbClass.getSelectedItem();
        String classKey = cmbClassBean.getDescription().trim();
        
        CoeusVector cvClassData = new CoeusVector();
        cvClassData.add(0,cvFreqValuesOfAllRows);
        cvClassData.add(1,cvFilteredData);
        if(htTempData.containsKey(classKey)){
            htTempData.remove(classKey);
            htTempData.put(classKey,cvClassData);
        }else{
            htTempData.put(classKey,cvClassData);
        }
    }
    
    //Method to check whehter the data is already there in hashtable.
    //This is done since no need to update the
    //freqvalueofallrows vector if already present
    private boolean checkTempData(){
        ComboBoxBean cmbClassBean =  (ComboBoxBean)classReportFreqForm.cmbClass.getSelectedItem();
        String classKey = cmbClassBean.getDescription().trim();
        if(htTempData.containsKey(classKey)){
            return true;
        }else{
            return false;
        }
        
    }//End checkTempData
    
    //Performs the cancel action
    private void performCancelAction(){
        if(isSaveRequired()){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            
            if(selectedOption == JOptionPane.YES_OPTION){
                performUpdate();
                try{
                    saveFormData();
                    dlgClassReportFreqForm.dispose();
                }catch (CoeusException ce){
                    ce.printStackTrace();
                }
            }else if(selectedOption == JOptionPane.NO_OPTION){
                dlgClassReportFreqForm.dispose();
            }
        }else{
            dlgClassReportFreqForm.dispose();
        }
    }//End performCancelAction
    
}//End of ClassReportFreqController
