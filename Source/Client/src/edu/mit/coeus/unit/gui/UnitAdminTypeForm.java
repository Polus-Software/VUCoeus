/*
 * UnitAdminTypeForm.java
 *
 * Created on December 16, 2005, 6:43 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 26-JAN-2011
 * by Bharati
 */

package edu.mit.coeus.unit.gui;

import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.unit.bean.UnitAdministratorBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Operator;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 * Class which provide the ability to associate multiple administrators
 * @author Tarique
 */
public class UnitAdminTypeForm extends javax.swing.JPanel implements ActionListener
                                                            , ChangeListener{
   /** Vector for getting Admininstator Data */                                                                
    private CoeusVector adminData;
    /** Object for unit number */    
    private String unitNumber;
    /** Vector which has admin Data  */    
    private CoeusVector cvAdminType;
    /** Vector which has admin type code */    
    private CoeusVector vecAdminTypeCode;
    /** hand icon column number */    
    private static final int HAND_ICON_COLUMN = 0;
    /** admin type code column number */    
    private static final int ADMIN_TYPE = 1;
    /** Administrator name column number */    
    private static final int NAME_COLUMN = 2;
    /** empty string */    
    private static final String EMPTY_STRING = "";
    /** return boolean to tell form is modified or not */    
    public boolean modified;
    /** function type   */    
    private char functionType;
    /**panel background color in disable mode  */    
    private static final Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    /** Cell Renderer object */    
    private UnitAdminTableCellRenderer unitAdminTableCellRenderer;
    /** Table mode object  */    
    private UnitAdminTableModel unitAdminTableModel;
    /**Class for Table Editor */    
    public UnitAdminTableCellEditor unitAdminTableCellEditor;
    /** Unit detail form object */    
    private UnitDetailForm unitDetailForm;
    /**Message Resouces object */    
    private CoeusMessageResources coeusMessageResources;
    /** Vector which has deleted data */    
    private CoeusVector cvDeletedData;
    /** Duplicate string object which pass in message resource    */    
    private static final String DUPLICATE_INFO = "unitAdmTypeForm_exceptionCode.1006";
    /** Unit Number string object which pass in message resource */    
    private static final String ENTER_UNIT_NUMBER = "unitDetFrm_exceptionCode.1114";
    /** No Admin type Code object which pass in message resource   */    
    private static final String NO_ADMIN_TYPE = "unitAdmTypeForm_exceptionCode.1001";
    /** No row to modify string which pass in message resource */    
    private static final String NO_ROW_TO_MODIFY = "unitAdmTypeForm_exceptionCode.1002";
    /**select for modify string which pass in message resource */    
    private static final String SELECT_TO_MODIFY = "unitAdmTypeForm_exceptionCode.1003";
    /** select for delete string which pass in message resource */    
    private static final String SELECT_TO_DELETE = "unitAdmTypeForm_exceptionCode.1004";
    /** no row to delete string which pass in message resource */    
    private static final String NO_ROW_DELETE = "unitAdmTypeForm_exceptionCode.1005";
    /** Creates new form UnitAdminTypeForm
     * @param unitDetailForm object of unit Detail form
     */
    public UnitAdminTypeForm(UnitDetailForm unitDetailForm) {
        this.unitDetailForm = unitDetailForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        setTableKeyTraversal();
    }
    /** 
     * Method which register components 
     **/
    private  void registerComponents(){
        cvDeletedData = new CoeusVector();
        unitAdminTableModel = new UnitAdminTableModel();
        unitAdminTableCellRenderer = new UnitAdminTableCellRenderer();
        unitAdminTableCellEditor = new UnitAdminTableCellEditor();
        tblUnitAdmin.setModel(unitAdminTableModel);
        tblUnitAdmin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        btnAdd.addActionListener(this);
        btnModify.addActionListener(this);
        btnDelete.addActionListener(this);
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        tblUnitAdmin.addMouseListener( new PersonDisplayAdapter());
        tbdPnUnitForm.addChangeListener(this);
        setTableEditors();
    }
    /**
     * Method to set form data
     */    
    public void setFormData(){
        if(getAdminData() != null){
            cvAdminType = (CoeusVector)(getAdminData().get(0));
            Operator op = cvAdminType.FILTER_ACTIVE_BEANS;
 
            if(cvAdminType!= null && cvAdminType.size() > 0){
                cvAdminType = cvAdminType.filter(op);
             
            }
            if(cvAdminType == null){
                cvAdminType = new CoeusVector();
            }
            vecAdminTypeCode = (CoeusVector)(getAdminData().get(1));
            if(vecAdminTypeCode == null){
                vecAdminTypeCode = new CoeusVector();
            }
            unitAdminTableModel.setData(cvAdminType);
            unitAdminTableModel.fireTableDataChanged();
            if( cvAdminType != null && cvAdminType.size() > 0){
                tblUnitAdmin.setRowSelectionInterval(0,0);
            }
        }
        
    }
     /**
     * Method to set Enable and disable buttons based on function type
     * @param value boolean based on function type
     */    
    public void setControls(boolean value){
        btnAdd.setEnabled(value);
        btnModify.setEnabled(value);
        btnDelete.setEnabled(value);
        btnOk.setEnabled(value);
        
    }
    /**
     *Method for setting table
     */
    private void setTableEditors(){
        tblUnitAdmin.setRowHeight(22);
        tblUnitAdmin.setShowHorizontalLines(false);
        tblUnitAdmin.setShowVerticalLines(false);
        tblUnitAdmin.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = tblUnitAdmin.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        
        tblUnitAdmin.setOpaque(false);
        tblUnitAdmin.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = tblUnitAdmin.getColumnModel().getColumn(HAND_ICON_COLUMN);
        column.setMaxWidth(25);
        column.setMinWidth(25);
        column.setPreferredWidth(25);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = tblUnitAdmin.getColumnModel().getColumn(1);
        column.setPreferredWidth(170);
        column.setCellRenderer(unitAdminTableCellRenderer);
        column.setCellEditor(unitAdminTableCellEditor);
        header.setReorderingAllowed(false);
        
        column = tblUnitAdmin.getColumnModel().getColumn(2);
        column.setPreferredWidth(175);
        column.setCellRenderer(unitAdminTableCellRenderer);
        header.setReorderingAllowed(false);
    }
    // supporting class to display PersonDetails on
    // double clicking of any Administrator row.
    /**
     * Class for Mouse Clicked
     */    
    class PersonDisplayAdapter extends MouseAdapter {
        /**
         * Method for mouse clicked
         * @param me Mouse Event
         */        
        public void mouseClicked( MouseEvent me ) {
            int selRow = tblUnitAdmin.getSelectedRow();
            if ( me.getClickCount() == 2) {
                if(selRow != -1){
                    String personName =
                    (String)tblUnitAdmin.getModel().getValueAt(selRow,2);
                    UnitAdministratorBean bean = search(personName);
                    if(bean != null){
                        String personID = bean.getAdministrator();
                        String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                        try{
                            PersonDetailForm personDetailForm =
                            new PersonDetailForm(personID,loginUserName,TypeConstants.DISPLAY_MODE);
                        }catch ( Exception e) {
                            e.printStackTrace();
                            CoeusOptionPane.showInfoDialog( e.getMessage() );
                        }
                    }
                }
            }
        }
    }
    /**
     * Method to search UnitAdministrator bean
     * @param personName String
     * @return UnitAdministrator
     **/
    private UnitAdministratorBean search(String personName){
        if(cvAdminType != null && personName != null){
            UnitAdministratorBean vectorBean = null;
            for(int i=0;i<cvAdminType.size();i++){
                vectorBean = (UnitAdministratorBean)cvAdminType.elementAt(i);
                if(vectorBean.getPersonName().equals(personName.trim())){
                    return vectorBean;
                }
            }
        }
        return null;
    }
    
    /**
     * Class for table model
     */    
    class UnitAdminTableModel extends AbstractTableModel{
        /** String array for column names*/        
        String colNames[] = {"","Admin Type", "Name"};
        /** Class column Type arrays */        
        Class[] colTypes = new Class [] {Object.class , String.class, String.class};
        
        /**
         * method to set data in table model
         * @param cvAdminType vector object
         */        
        public void setData(CoeusVector cvAdminType) {
            cvAdminType = cvAdminType;
        }
        
        /**
         * Method to get value
         * @param rowIndex row
         * @param columnIndex column
         * @return Object
         */        
        public Object getValueAt(int rowIndex, int columnIndex) {
            UnitAdministratorBean unitAdministratorBean = (UnitAdministratorBean)cvAdminType.get(rowIndex);
            switch(columnIndex) {
                case HAND_ICON_COLUMN:
                    return EMPTY_STRING;
                case ADMIN_TYPE:
                    int adminTypeCode = unitAdministratorBean.getUnitAdminTypeCode();
                    CoeusVector filteredVector = vecAdminTypeCode.filter(
                    new Equals("code", ""+adminTypeCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }
                case NAME_COLUMN:
                    String personName = unitAdministratorBean.getPersonName() == null ?EMPTY_STRING:unitAdministratorBean.getPersonName();
                    return personName;
            }
            return EMPTY_STRING;
            
        }
        /**
         * Method to set value in table model
         * @param value which has to set
         * @param row specify the row
         * @param column specify the column
         */        
        public void setValueAt(Object value, int row, int column){
            //Modified for COEUSDEV-170 : Array index out of range error - Unit Administrators - Start
            //Checks cvAdminType has the bean collection and the current row number is greater than the cvAdminType vector size 
            //if(cvAdminType == null){
            if(cvAdminType == null || (cvAdminType.size() < 1 || cvAdminType.size() <= row)) return;//Case#(COEUSDEV-123) - End
                
            
            UnitAdministratorBean unitAdministratorBean = (UnitAdministratorBean)cvAdminType.get(row);
            switch(column){
                case ADMIN_TYPE:
                    if(value==null || value.toString().equals(EMPTY_STRING)) {
                        return ;
                    }
                    ComboBoxBean comboBoxBean = (ComboBoxBean)vecAdminTypeCode.filter(new Equals("description", value.toString())).get(0);
                    int contactTypeCode = -1;
                    if(comboBoxBean != null){
                        contactTypeCode = Integer.parseInt(comboBoxBean.getCode());
                    }
                    if(unitAdministratorBean != null){
                        if(unitAdministratorBean.getUnitAdminTypeCode() != contactTypeCode){
                            if( contactTypeCode != unitAdministratorBean.getUnitAdminTypeCode() ){
                                unitAdministratorBean.setUnitAdminTypeCode(contactTypeCode);
                                if( unitAdministratorBean.getAcType() == null ){
                                    unitAdministratorBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                                modified = true;
                            }
                        }
                    }
                    break;
                case NAME_COLUMN:
                    unitAdministratorBean.setPersonName(value.toString().trim());
                    if( unitAdministratorBean.getAcType() == null ){
                        unitAdministratorBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    modified = true;
            }
            
            
        }
        
        /**
         * Method to check cell is editable or not
         * @param row specify the row
         * @param column specify in which column
         * @return boolean
         */        
        public boolean isCellEditable(int row, int column) {
            if(getFunctionType() == 'G' || getFunctionType() == 'D' || getFunctionType() == 'M'){
                return false;
            }else {
                boolean type = (column == ADMIN_TYPE)?true:false;
                return type;
            }
        }
        
        /**
         * Method which count the number of column
         * @return integer value
         */        
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * Method which return column class type
         * @param columnIndex specify which column
         * @return Class
         */        
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /**
         * Method to get column name
         * @param column specify which column
         * @return String
         */        
        public String getColumnName(int column) {
            return colNames[column];
        }
        /**
         * Method which return row count
         * @return integer value for count
         */        
        public int getRowCount() {
            if( cvAdminType == null ) {
                return 0;
            }
            return cvAdminType.size();
            
        }
    }
    
    /*Renderer for the table columns*/
    /**
     * Class for cell renderer
     */    
    class UnitAdminTableCellRenderer extends DefaultTableCellRenderer{
        /**
         * Constructor for cell renderer
         */        
        public UnitAdminTableCellRenderer() {
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
        }
        
        /**
         * Method which return the component
         * @param table object
         * @param value of the cell
         * @param isSelected cell is selected or not
         * @param hasFocus cell has got focus or not
         * @param row of the table
         * @param column of the column
         * @return Component
         */        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            
            switch(column) {
                case HAND_ICON_COLUMN:
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case ADMIN_TYPE:
                    setText(value.toString());
                    if( getFunctionType() == 'G' || getFunctionType() == 'D' || getFunctionType() == 'M'){
                        setBackground(PANEL_BACKGROUND_COLOR);
                    }else{
                        setBackground(Color.WHITE);
                    }
                    return this;
                case NAME_COLUMN:
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                    
            }
            return null;
        }
    }
    /** Clas for Table Cell editor */
    class UnitAdminTableCellEditor extends AbstractCellEditor implements 
        TableCellEditor, ActionListener{
            /** Combo box object which display in editor */            
        private JComboBox cmbContactType;
        /** boolean value for populate combo box */        
        private boolean populated = false;
        /** column value  */        
        private int column;
        /**
         * Constructor for cell editor
         */        
        UnitAdminTableCellEditor() {
            cmbContactType = new JComboBox();
            cmbContactType.addActionListener(this);
            if (functionType == 'D'){
                cmbContactType.setEditable(false);
            }
        }
        /**
         * Method to populate combo box
         */        
        private void populateCombo() {
            cmbContactType.setModel(new DefaultComboBoxModel(vecAdminTypeCode));
        }
        /**
         * Method to get cell editor component
         * @param table object
         * @param value table value
         * @param isSelected editor is selected or not
         * @param row which row
         * @param column which column for component
         * @return Component object
         */        
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case ADMIN_TYPE:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                    if(comboBoxBean != null){
                        if(comboBoxBean.getDescription() == null || comboBoxBean.getDescription().equals(EMPTY_STRING)) {
                            ComboBoxBean selBean = (ComboBoxBean)vecAdminTypeCode.get(0);
                            cmbContactType.setSelectedItem(selBean);
                            return cmbContactType;
                        }
                        cmbContactType.setSelectedItem(value);
                    }
                    return cmbContactType;
            }
            return null;
        }
        
        /**
         * Method to get Editor value
         * @return Object
         */        
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case ADMIN_TYPE:
                    return cmbContactType.getSelectedItem();
            }
            return cmbContactType;
        }
        /**
         * Method to count clik on cell
         * @return total click on cell
         */        
        public int getClickCountToStart(){
            return 1;
        }
        /**
         * Method to stop cell editing
         * @return boolean value
         */        
        public boolean stopCellEditing(){
            return super.stopCellEditing();
        }
        
        /**
         * Method to action performed
         * @param e action event
         */        
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
        
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    //supporting method to check for null value
    /**
     * Method to check null string
     * @param value string
     * @return String value
     */    
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
    }
    /**
     * Method to take action on components
     * @param actionEvent Event generator
     */    
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            Object eventSource = actionEvent.getSource();
            if(eventSource == btnCancel){
                unitDetailForm.validateWindow();
            }else if(eventSource == btnOk){
                performOKAction();
            }else if(eventSource == btnAdd){
                performFindPerson();
                
            }else if(eventSource == btnModify){
                performModifyAction();
                
            }else if(eventSource == btnDelete){
                performDeleteAction();
            }
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
            return;
        }
    }
    /**
     *Method to check ok action
     *@throws Exception if error occurs
     */
    private void performOKAction() throws Exception{
        UnitAdministratorBean bean = checkForDuplicate();
        if(bean != null){
            int index = searchIndex(cvAdminType, bean);
            if(index != -1){
                tbdPnUnitForm.setSelectedIndex(1);
                tblUnitAdmin.requestFocus();
                tblUnitAdmin.setRowSelectionInterval(index, index);
            }
            return;
        }
        if(unitDetailForm != null){
            unitDetailForm.performOkAction();
        }
    }
     /**
     * Method to show person detail window
     */    
    private void performFindPerson(){
        try{
            if(getFunctionType() == 'I'){
                   if(unitDetailForm.txtUnitNumber.getText()!=null
                    &&unitDetailForm.txtUnitNumber.getText().trim().length()>0){
                        setUnitNumber(unitDetailForm.txtUnitNumber.getText());
                   }else{
                       CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(ENTER_UNIT_NUMBER));
                       tbdPnUnitForm.setSelectedIndex(0);
                       return;
                   }
            }
            if(vecAdminTypeCode != null && vecAdminTypeCode.size()>0){
                unitAdminTableCellEditor.stopCellEditing();
                CoeusSearch proposalSearch = null;
                proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(), 
                    "PERSONSEARCH", CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
                proposalSearch.showSearchWindow();
                Vector vSelectedPersons = proposalSearch.getMultipleSelectedRows();
                if ( vSelectedPersons != null ){
                    HashMap singlePersonData = null;
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                        
                        singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                        
                        if( singlePersonData == null || singlePersonData.isEmpty() ){
                            continue;
                        }
                        String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));
                        String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));
                        
                        ComboBoxBean selBean = (ComboBoxBean)vecAdminTypeCode.get(0);
                        int contactTypeCode = -1;
                        if(selBean != null){
                            contactTypeCode = Integer.parseInt(selBean.getCode());
                        }
                        UnitAdministratorBean unitAdministratorBean = new UnitAdministratorBean();
                        unitAdministratorBean.setUnitNumber(getUnitNumber());
                        
                        unitAdministratorBean.setUnitAdminTypeCode(contactTypeCode);
                        unitAdministratorBean.setAdministrator(personId);
                        unitAdministratorBean.setPersonName(personName);
                        unitAdministratorBean.setAcType(TypeConstants.INSERT_RECORD);
                        cvAdminType.add(unitAdministratorBean);
                        modified = true;
                        unitAdminTableModel.fireTableDataChanged();
                        int index = searchIndex(cvAdminType, unitAdministratorBean);
                        if(index != -1){
                            tblUnitAdmin.setRowSelectionInterval(index, index);
                            tblUnitAdmin.scrollRectToVisible(tblUnitAdmin.getCellRect(
                                                index ,0, true));
                        }
                        unitAdminTableCellEditor.stopCellEditing();
                    } //end of for loop
                }//end of vSelectedPersons != null
            }else{
                String msg = coeusMessageResources.parseMessageKey(NO_ADMIN_TYPE);
                CoeusOptionPane.showErrorDialog(msg);
            }
          }catch( Exception err ){
            err.printStackTrace();
        }
    }
    /** Method to Modify Administrator name */
    private void performModifyAction(){
        try{
            int rowCount = tblUnitAdmin.getRowCount();
            if(rowCount == 0){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_ROW_TO_MODIFY));
                return;
            }
            int selRow = tblUnitAdmin.getSelectedRow();
            if(selRow != -1){
                UnitAdministratorBean unitAdministratorBean =
                (UnitAdministratorBean)cvAdminType.get(selRow);
                CoeusSearch proposalSearch = null;
                proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(), 
                    "PERSONSEARCH",CoeusSearch.TWO_TABS );
                proposalSearch.showSearchWindow();
                Vector vSelectedPersons = proposalSearch.getMultipleSelectedRows();
                if ( vSelectedPersons != null ){
                    HashMap singlePersonData = null;
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                        
                        singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                        
                        if( singlePersonData == null || singlePersonData.isEmpty() ){
                            continue;
                        }
                        String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));
                        String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));
                        if(unitAdministratorBean != null){
                            if(!unitAdministratorBean.getAdministrator().equals(personId)){
                                unitAdministratorBean.setAdministrator(personId);
                                unitAdministratorBean.setPersonName(personName);
                                //COEUSDEV-170 : Array index out of range error - Unit Administrators - Start
                                //Administrator who is get modfied is deleted and new administrator is inserted
//                                if(unitAdministratorBean.getAcType() == null ){
//                                    unitAdministratorBean.setAcType(TypeConstants.UPDATE_RECORD);
//                                }
                                UnitAdministratorBean unitAdminBean = new UnitAdministratorBean();
                                if(unitAdministratorBean.getAcType() == null ){
                                    unitAdministratorBean.setAcType(TypeConstants.DELETE_RECORD);
                                    cvDeletedData.addElement(unitAdministratorBean);
                                    cvAdminType.removeElementAt(selRow);
                                    unitAdminBean.setAcType(TypeConstants.INSERT_RECORD);
                                    unitAdminBean.setAdministrator(unitAdministratorBean.getAdministrator());
                                    unitAdminBean.setUnitAdminTypeCode(unitAdministratorBean.getAwUnitAdminTypeCode());
                                    unitAdminBean.setUnitNumber(unitAdministratorBean.getUnitNumber());
                                    unitAdminBean.setPersonName(unitAdministratorBean.getPersonName());
                                    cvAdminType.addElement(unitAdminBean);
                                    //COEUSDEV-170 : END
                                }
                                modified = true;
                                unitAdminTableModel.fireTableDataChanged();
                                int index = searchIndex(cvAdminType, unitAdministratorBean);
                                if(index != -1){
                                    tblUnitAdmin.setRowSelectionInterval(index, index);
                                }
                                break;
                            }
                        }
                    } //end of for loop
                }//end of 
            }else{
                String msg = coeusMessageResources.parseMessageKey(SELECT_TO_MODIFY);
                CoeusOptionPane.showInfoDialog(msg);
                return;
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
    }
    /** Method for Delete Administrator for the department */
    private void performDeleteAction(){
        int rowCount = tblUnitAdmin.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(NO_ROW_DELETE));
            return;
        }
        int selRow = tblUnitAdmin.getSelectedRow();
        if( selRow == -1) {
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(SELECT_TO_DELETE));
            return;
        }else{
            String msg = coeusMessageResources.parseMessageKey("generalDelConfirm_exceptionCode.2100");
            int selectedOption = CoeusOptionPane.showQuestionDialog(msg+" row? ",
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if( selectedOption == CoeusOptionPane.SELECTION_YES ){
                UnitAdministratorBean unitAdministratorBean = 
                    (UnitAdministratorBean)cvAdminType.get(selRow);
                if(unitAdministratorBean.getAcType()== null ||
                unitAdministratorBean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                    unitAdministratorBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedData.addElement(unitAdministratorBean);
                    cvAdminType.removeElementAt(selRow);
                }else if(unitAdministratorBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    cvAdminType.removeElementAt(selRow);
                }
                modified = true;
                unitAdminTableModel.fireTableDataChanged();
                if(cvAdminType.size()>0){
                    if(selRow-1 != -1){
                         tblUnitAdmin.setRowSelectionInterval(selRow -1 , selRow -1);
                         tblUnitAdmin.scrollRectToVisible(tblUnitAdmin.getCellRect(
                                                    selRow -1 ,0, true));
                    }else{
                        tblUnitAdmin.setRowSelectionInterval(0 , 0);
                        tblUnitAdmin.scrollRectToVisible(tblUnitAdmin.getCellRect(
                                                    0 ,0, true));
                    }
                }
            }
        }
    }
    /**
     * Method to check For Duplicate SponsorContactBean
     * @return bean
     **/
    public UnitAdministratorBean checkForDuplicate(){
        if(cvAdminType!= null && cvAdminType.size() > 0){
            Equals contactType;
            Equals personId;
            for(int index = 0;index <cvAdminType.size();index++){
                UnitAdministratorBean adminBean = (UnitAdministratorBean)cvAdminType.get(index);
                contactType =
                new Equals("unitAdminTypeCode",new Integer(adminBean.getUnitAdminTypeCode()));
                personId = new Equals("administrator", adminBean.getAdministrator());
                And AwContact = new And(contactType , personId);
                CoeusVector cvFilterd = (CoeusVector)cvAdminType.filter(AwContact);
                if(cvFilterd!= null && cvFilterd.size() > 1) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_INFO));
                    return (UnitAdministratorBean)cvFilterd.get(0);
                }
            }
            
        }
        return null;
    }
    /**
     * Method to search index of an object
     * @param coeusVector CoeusVector
     * @param object Object
     * @return int
     **/
    public int searchIndex(CoeusVector coeusVector, Object object){
        if(object != null && coeusVector != null){
            UnitAdministratorBean bean = (UnitAdministratorBean)object;
            UnitAdministratorBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (UnitAdministratorBean)coeusVector.elementAt(i);
                if(vectorBean.getUnitNumber().equals(bean.getUnitNumber()) &&
                vectorBean.getAdministrator().equals(bean.getAdministrator()) &&
                vectorBean.getUnitAdminTypeCode() == bean.getUnitAdminTypeCode() ){
                    return i;
                }
            }
        }
        return -1;
    }
    /**
     * Method to check state of Tab Pane
     * @param changeEvent Event for Tab Pane
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        int rowCount = tblUnitAdmin.getRowCount();
        if(getFunctionType() == 'G'){
            if(rowCount > 0){
                tblUnitAdmin.setRowSelectionInterval(0,0);
            }
            setDefaultFocus();
            
        }else{
            if(changeEvent.getSource() == tbdPnUnitForm){
                int selTab = 0;
                selTab = tbdPnUnitForm.getSelectedIndex();
                if(unitDetailForm != null){
                    if(selTab == 0){
                        unitDetailForm.setFocusTraversal(UnitDetailForm.UNIT_DETAIL_TAB);
                        
                    }else if(selTab == 1){
                        if(rowCount > 0){
                            tblUnitAdmin.setRowSelectionInterval(0,0);
                        }
                        unitDetailForm.setFocusTraversal(UnitDetailForm.ADMINISTRATOR_TAB);
                        
                    }
                }
            }
        }
    }
    /**
     * Default Focus on opening the form
     */
    public void setDefaultFocus(){
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                btnCancel.requestFocus();
            }
        });
    }
    
    /**
     * Getter for property cvDeletedData.
     * @return Value of property cvDeletedData.
     */
    public edu.mit.coeus.utils.CoeusVector getCvDeletedData() {
        return cvDeletedData;
    }
    
    /**
     * Setter for property cvDeletedData.
     * @param cvDeletedData New value of property cvDeletedData.
     */
    public void setCvDeletedData(edu.mit.coeus.utils.CoeusVector cvDeletedData) {
        this.cvDeletedData = cvDeletedData;
    }
    /**
     * Getter for property cvAdminType.
     * @return Value of property cvAdminType.
     */
    public edu.mit.coeus.utils.CoeusVector getCvAdminType() {
        return cvAdminType;
    }
    
    /**
     * Setter for property cvAdminType.
     * @param cvAdminType New value of property cvAdminType.
     */
    public void setCvAdminType(edu.mit.coeus.utils.CoeusVector cvAdminType) {
        this.cvAdminType = cvAdminType;
    }
    
    /**
     * Getter for property adminData.
     * @return Value of property adminData.
     */
    public edu.mit.coeus.utils.CoeusVector getAdminData() {
        return adminData;
    }
    
    /**
     * Setter for property adminData.
     * @param adminData New value of property adminData.
     */
    public void setAdminData(edu.mit.coeus.utils.CoeusVector adminData) {
        this.adminData = adminData;
    }
    
    /**
     * Getter for property vecAdminTypeCode.
     * @return Value of property vecAdminTypeCode.
     */
    public edu.mit.coeus.utils.CoeusVector getVecAdminTypeCode() {
        return vecAdminTypeCode;
    }
    
    /**
     * Setter for property vecAdminTypeCode.
     * @param vecAdminTypeCode New value of property vecAdminTypeCode.
     */
    public void setVecAdminTypeCode(edu.mit.coeus.utils.CoeusVector vecAdminTypeCode) {
        this.vecAdminTypeCode = vecAdminTypeCode;
    }
    /**
     *Method for traverse table to components
     **/
    private void setTableKeyTraversal(){
        javax.swing.InputMap im = tblUnitAdmin.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        final Action oldTabAction = tblUnitAdmin.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            int row = 0;
            int column =0;
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                boolean selectionOut=false;
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                if((rowCount-1)==row && column==(columnCount-1)){
                    if(getFunctionType()!='G'){
                        selectionOut=true;
                        btnAdd.requestFocusInWindow();
                        tblUnitAdmin.setRowSelectionInterval(0,0);
                        tblUnitAdmin.scrollRectToVisible(tblUnitAdmin.getCellRect(
                        0 ,0, true));
                    }
                    else{
                        btnCancel.requestFocusInWindow();
                    }
                }
                if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column=0;
                    btnAdd.requestFocusInWindow();
                    return ;
                }
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    if (row == rowCount) {
                        row = 0;
                    }
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                if(!selectionOut){
                    table.changeSelection(row, column, false, false);
                }
            }
        };
        tblUnitAdmin.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction = tblUnitAdmin.getActionMap().get(im.get(shiftTab));
        Action tabShiftAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                while (! table.isCellEditable(row, column) ) {
                    column -= 1;
                    if (column <= 0) {
                        column = ADMIN_TYPE;
                        row -=1;
                    }
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                table.changeSelection(row, column, false, false);
            }
        };
        tblUnitAdmin.getActionMap().put(im.get(shiftTab), tabShiftAction);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        tbdPnUnitForm = new edu.mit.coeus.utils.CoeusTabbedPane();
        scrPnUnitDetails = new javax.swing.JScrollPane();
        scrPnAdministrator = new javax.swing.JScrollPane();
        pnlAdministrator = new javax.swing.JPanel();
        scrPnAdminType = new javax.swing.JScrollPane();
        tblUnitAdmin = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                if (editCellAt(row, column)){
                    setRowSelectionInterval(row,row);
                    setColumnSelectionInterval(column,column);
                    getEditorComponent().requestFocusInWindow();
                }
            }};
            btnAdd = new javax.swing.JButton();
            btnModify = new javax.swing.JButton();
            btnDelete = new javax.swing.JButton();

            setLayout(new java.awt.GridBagLayout());

            setMinimumSize(new java.awt.Dimension(525, 300));
            setPreferredSize(new java.awt.Dimension(525, 300));
            btnOk.setFont(CoeusFontFactory.getLabelFont());
            btnOk.setMnemonic('O');
            btnOk.setText("OK");
            btnOk.setMaximumSize(new java.awt.Dimension(65, 23));
            btnOk.setMinimumSize(new java.awt.Dimension(65, 23));
            btnOk.setPreferredSize(new java.awt.Dimension(65, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            add(btnOk, gridBagConstraints);

            btnCancel.setFont(CoeusFontFactory.getLabelFont());
            btnCancel.setMnemonic('C');
            btnCancel.setText("Cancel");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            add(btnCancel, gridBagConstraints);

            tbdPnUnitForm.setMinimumSize(new java.awt.Dimension(480, 304));
            tbdPnUnitForm.setPreferredSize(new java.awt.Dimension(480, 304));
            scrPnUnitDetails.setMaximumSize(new java.awt.Dimension(430, 240));
            scrPnUnitDetails.setMinimumSize(new java.awt.Dimension(430, 235));
            scrPnUnitDetails.setPreferredSize(new java.awt.Dimension(430, 235));
            tbdPnUnitForm.addTab("Unit Details", scrPnUnitDetails);

            scrPnAdministrator.setMinimumSize(new java.awt.Dimension(423, 205));
            scrPnAdministrator.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    scrPnAdministratorFocusGained(evt);
                }
            });

            pnlAdministrator.setLayout(new java.awt.GridBagLayout());

            pnlAdministrator.setMinimumSize(new java.awt.Dimension(430, 240));
            pnlAdministrator.setPreferredSize(new java.awt.Dimension(430, 240));
            scrPnAdminType.setMinimumSize(new java.awt.Dimension(390, 265));
            scrPnAdminType.setPreferredSize(new java.awt.Dimension(390, 265));
            tblUnitAdmin.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {},
                    {},
                    {},
                    {}
                },
                new String [] {

                }
            ));
            scrPnAdminType.setViewportView(tblUnitAdmin);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridheight = 3;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlAdministrator.add(scrPnAdminType, gridBagConstraints);

            btnAdd.setFont(CoeusFontFactory.getLabelFont());
            btnAdd.setMnemonic('A');
            btnAdd.setText("Add");
            btnAdd.setMaximumSize(new java.awt.Dimension(73, 23));
            btnAdd.setMinimumSize(new java.awt.Dimension(73, 23));
            btnAdd.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlAdministrator.add(btnAdd, gridBagConstraints);

            btnModify.setFont(CoeusFontFactory.getLabelFont());
            btnModify.setMnemonic('M');
            btnModify.setText("Modify");
            btnModify.setMargin(new java.awt.Insets(2, 12, 2, 12));
            btnModify.setMaximumSize(new java.awt.Dimension(73, 23));
            btnModify.setMinimumSize(new java.awt.Dimension(73, 23));
            btnModify.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlAdministrator.add(btnModify, gridBagConstraints);

            btnDelete.setFont(CoeusFontFactory.getLabelFont());
            btnDelete.setMnemonic('D');
            btnDelete.setText("Delete");
            btnDelete.setMargin(new java.awt.Insets(2, 12, 2, 12));
            btnDelete.setMaximumSize(new java.awt.Dimension(73, 23));
            btnDelete.setMinimumSize(new java.awt.Dimension(73, 23));
            btnDelete.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlAdministrator.add(btnDelete, gridBagConstraints);

            scrPnAdministrator.setViewportView(pnlAdministrator);

            tbdPnUnitForm.addTab("Administrators", scrPnAdministrator);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridheight = 2;
            add(tbdPnUnitForm, gridBagConstraints);

        }//GEN-END:initComponents
    /**
     * Method to set focus in table
     * @param evt Event for scroll pane
     */  
    private void scrPnAdministratorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_scrPnAdministratorFocusGained
        if(tblUnitAdmin.getRowCount() > 0){
            tblUnitAdmin.setRowSelectionInterval(0,0);
            tblUnitAdmin.setColumnSelectionInterval(ADMIN_TYPE,ADMIN_TYPE);
            tblUnitAdmin.scrollRectToVisible(
                            tblUnitAdmin.getCellRect(0 ,ADMIN_TYPE, true));
            tblUnitAdmin.editCellAt(0, ADMIN_TYPE);
            if(tblUnitAdmin.getEditorComponent()!=null){
                tblUnitAdmin.getEditorComponent().requestFocusInWindow();
            }else{
                btnCancel.requestFocusInWindow();
            }
        }else{
            if(getFunctionType()=='G'){
                btnCancel.requestFocusInWindow();
            }else{
                btnAdd.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_scrPnAdministratorFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnModify;
    public javax.swing.JButton btnOk;
    public javax.swing.JPanel pnlAdministrator;
    public javax.swing.JScrollPane scrPnAdminType;
    public javax.swing.JScrollPane scrPnAdministrator;
    public javax.swing.JScrollPane scrPnUnitDetails;
    public edu.mit.coeus.utils.CoeusTabbedPane tbdPnUnitForm;
    public javax.swing.JTable tblUnitAdmin;
    // End of variables declaration//GEN-END:variables
    
}
