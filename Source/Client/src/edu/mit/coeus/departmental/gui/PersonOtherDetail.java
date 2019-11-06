/**
 * @(#)PersonOtherDetail.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.departmental.gui;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.customelements.bean.*;
import edu.mit.coeus.gui.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ComboBoxBean;

import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
/**
 * <CODE>PersonOtherDetail </CODE>is a form object which display
 * all the Other column values and it is used to <CODE> add/modify </CODE> the other elements.
 * This class will be instantiated from <CODE>PersonDetailsForm</CODE>.
  * @version 1.0 March 13, 2003
 * @author Raghunath P.V.
 */
public class PersonOtherDetail extends javax.swing.JComponent implements LookUpWindowConstants, TypeConstants{

    //private PersonCustomElementsInfoBean departmentOthersFormBean;
    //private PersonCustomElementsInfoBean newDepartmentOthersFormBean;
    //private PersonCustomElementsInfoBean oldDepartmentOthersFormBean;
    private char functionType;
    private boolean canMaintain;
    private Vector vecModuleColumns;
    private Vector vecPersonColumnValues;
    private boolean saveRequired;
    private String personId;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;

    private final static int TWO_TABS = 1;
     //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    private InputMap inputMap;
    private Action oldTabAction,tabAction;
    private KeyStroke tab;
     // Variable to check TextFiled and Button is Editable or not
    private int editableTableField;
    
    //constant assigned for visible button in editor
    private final int VISIBLE_BUTTON = 0;
    
    //constant assigned for in-visible button in editor
    private final int INVISIBLE_BUTTON = 1;
    
    //constant assigned for editable text field in editor
    private final int EDITABLE_TEXT_FIELD = 2;
    
    //constant assigned for non-editable text field in editor
    private final int NON_EDITABLE_TEXT_FIELD = 3;
    private int previousRow;
    private int previousCol;
    private int mandatoryRow;
    private int mandatoryColumn;
    private boolean otherTabMandatory;
    private int row ;
    private int column;
    private boolean saveDone = false;
    //Case :#3149 - End

    //private boolean hasLookUp;
    private boolean lookupAvailable[];

    /** Default Constructor */
    public PersonOtherDetail() {
    }

    /** Creates new form <CODE>PersonOtherDetail</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param maintainOther is a boolean variable which specifies that the others tab is maintainaable or not
     * @param columnModules is a Vector of Custom values for a module
     * @param columnValues is a Vector of Custom values for a selected person
     * @param id is the String representing the selected Person
     * 'D' specifies that the form is in Display Mode
     */
    public PersonOtherDetail(char functionType, boolean maintainOther,
            Vector columnModules, Vector columnValues, String id) {
        this.functionType = functionType;
        this.canMaintain = maintainOther;
        this.vecModuleColumns = columnModules;
        this.vecPersonColumnValues = columnValues;
        this.personId = id;
        this.mdiForm = CoeusGuiConstants.getMDIForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        if((!canMaintain) || (functionType == DISPLAY_MODE)){
            setNotToMaintain();
        }
        setFormData();
        setEditors();
        //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
        tblPersonOtherDetail.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedColumn = tblPersonOtherDetail.getSelectedColumn();
                if((editableTableField == INVISIBLE_BUTTON) || (selectedColumn <2 || selectedColumn >3)){
                    setSaveAction(true);
                    tblPersonOtherDetail.setRowSelectionInterval(getRow(),getRow());
                    tblPersonOtherDetail.setColumnSelectionInterval(getColumn(),getColumn());
                    tblPersonOtherDetail.editCellAt(getRow(),getColumn());
                }
            }
        });
        //Case :#3149 - End
        
    }
    
    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
    /**
     * Method to set tabbing between the fields in the table
     *
     **/
    public void setTabFocus(){
        
        tblPersonOtherDetail.setFocusable(true);
        tblPersonOtherDetail.setEnabled(true);
        tblPersonOtherDetail.resetKeyboardActions();
        // Assigning no action when shift+tab keys are pressed inside table
        inputMap = tblPersonOtherDetail.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke shiftTab=KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.SHIFT_MASK);
        inputMap.put(shiftTab,"NoAction");
        
        // Assigning no action when enter key is pressed inside table
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        inputMap.put(enter,"NoAction");
        final Action  enterAction = tblPersonOtherDetail.getActionMap().get(inputMap.get(enter));
        
        tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        inputMap.put(tab, inputMap.get(tab));
        
        if(tabAction == null){
            setTabAction();
        }else{
            tabAction.setEnabled(true);
        }
        
    }
    /**
     *Method to set tab action for table
     */
    private void setTabAction(){
        oldTabAction = tblPersonOtherDetail.getActionMap().get(inputMap.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // Auto single tab action is performed
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                TableCellEditor editor = table.getCellEditor(row,column);
                table.editCellAt(row,column);
                //Checking for empty cell in the button column
                if(editor instanceof ButtonEditor){
                    if(editableTableField == INVISIBLE_BUTTON){
                        oldTabAction.actionPerformed(e);
                        row = table.getSelectedRow();
                        column = table.getSelectedColumn();
                    }
                }
                // To check column non-editable cells and perform auto single tab action
                while(!table.isCellEditable(row,column)){
                    oldTabAction.actionPerformed( e );
                    row = table.getSelectedRow();
                    column = table.getSelectedColumn();
                }
                table.editCellAt(row,column);
                
                // checking non-editable text field and perform auto single tab action
                if(editableTableField == NON_EDITABLE_TEXT_FIELD){
                    oldTabAction.actionPerformed(e);
                    row = table.getSelectedRow();
                    column = table.getSelectedColumn();
                }
                table.editCellAt(row,column);
            }
        };
        tblPersonOtherDetail.getActionMap().put(inputMap.get(tab), tabAction);
        
        
    }

    /**
     * Method to set focus to a cell in table when the other tabbed pane is selected
     */
    public void setTableFocus(){
        tblPersonOtherDetail.clearSelection();
        tblPersonOtherDetail.setRowSelectionInterval(0,0);
        tblPersonOtherDetail.setColumnSelectionInterval(3,3);
                    
        boolean buttonFocus = tblPersonOtherDetail.editCellAt(0,3);
        tblPersonOtherDetail.setRowSelectionInterval(0,0);
        tblPersonOtherDetail.setColumnSelectionInterval(3,3);
        previousCol = 3;
        previousRow = 0;        
         if(!buttonFocus){
             tblPersonOtherDetail.editCellAt(0,2);
             tblPersonOtherDetail.setRowSelectionInterval(0,0);
             tblPersonOtherDetail.setColumnSelectionInterval(2,2);
             previousCol = 3;
             previousRow = 0;
         }
    }
    // Case :#3149 - End
    
    public JTable getTable(){
        return tblPersonOtherDetail;
    }
    public boolean[] getLookUpAvailable(){
        return lookupAvailable;
    }

    /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }

    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */

    public boolean isSaveRequired(){
        return this.saveRequired;
    }

    /** This method is used for Validations.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception is a exception to be thrown in the client side.
     */
    public boolean validateData() throws Exception{
        
        if(tblPersonOtherDetail.isEditing()){
            tblPersonOtherDetail.getCellEditor().stopCellEditing();
        }

        int rowCount = tblPersonOtherDetail.getRowCount();
        if(rowCount > 0){
            
            for(int inInd = 0; inInd < rowCount ;inInd++){

                boolean  isReq = ((Boolean)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,7)).booleanValue();
                String cVal = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,2);
                String dType = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,8);
                int dLen = ((Integer)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,9)).intValue();
                String cLabel = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,1);
                
                if(isReq){
                    if(cVal == null || cVal.trim().length() <= 0){
                        //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                        setMandatoryField(inInd,2);
                        setOtherTabmandatory(true);
                        //Case :#3149 - End
                        errorMessage(cLabel + " is mandatory. Please enter a value for "+cLabel);
                        tblPersonOtherDetail.requestFocus();
                        return false;
                        //break;
                    }
                    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                    else{
                        setOtherTabmandatory(false);
                    }
                    //Case :#3149 - End
                }
                if(dType != null && cVal != null && cVal.trim().length() > 0){
                    if(dType.equalsIgnoreCase("NUMBER")){
                        try{
                            Integer.parseInt(cVal);
                        }catch(NumberFormatException nfe){
                            //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                            setOtherTabmandatory(true);
                            setMandatoryField(inInd,2);
                            //Case :#3149 - End
                            errorMessage("Value for "+cLabel + " should be numeric. ");
                            tblPersonOtherDetail.requestFocus();
                            return false;
                        }
                    }else if(dType.equalsIgnoreCase("DATE")){
                        DateUtils dateUtils = new DateUtils();
                        //Modified for case 3723 - Person Information is not saved. - start
                        //String resultDate = dateUtils.formatDate(cVal,"/","MM/dd/yyyy");
                        String resultDate = dateUtils.formatDate(cVal,"/-,:.","MM/dd/yyyy");
                        //Modified for case 3723 - Person Information is not saved. - start
                        if(resultDate == null){
                            //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                            setOtherTabmandatory(true);
                            setMandatoryField(inInd,2);
                            //Case :#3149 - End
                            errorMessage("Value for "+cLabel + " should be valid date. ");
                            tblPersonOtherDetail.requestFocus();
                            return false;
                        }
                    }
                    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                    else {
                        setOtherTabmandatory(false);
                    }
                    //Case :#3149 - End
                }
                
                int len = cVal.trim().length();
                if((dLen > 0) && (len > dLen)){
                    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                    setMandatoryField(inInd,2);
                    setOtherTabmandatory(true);
                    //Case :#3149 - End
                    errorMessage("Value for "+cLabel + " cannot be more than "+ dLen +" characters long.");
                    tblPersonOtherDetail.requestFocus();
                    return false;
                    //break;
                }
                //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                else{
                    setOtherTabmandatory(false);
                }
                //Case :#3149 - End
                //}
            }
        }
        return true;
    }
    
     //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
    /**
     * Method to get and set the mandatory field row and column
     */
    public void setMandatoryField(int row,int column){
        mandatoryRow = row;
        mandatoryColumn = column;
    }
    /**
     * Method to get  mandatory field row 
     */
    public int getMandatoryRow(){
        return mandatoryRow;
    }
    /**
     * Method to get  mandatory field column
     */
    public int getMandatoryColumn(){
        return mandatoryColumn;
    }
    /**
     * Method to set whether mandatory dialog box is displayed
     */
    private void setOtherTabmandatory(boolean mandatory){
        this.otherTabMandatory = mandatory;
    }
    /**
     * Method to get boolean value whether mandatory dialog box is dispayed
     */
    public boolean getOtherTabMandatory(){
        return this.otherTabMandatory;
    }
    //Case :#3149 - End
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */

    public void errorMessage(String mesg) throws Exception {
        throw new Exception(mesg);
    }
    
    /**
     * This method is used to get the Data from the Others Form.
     * @returns Vector containing all the table data. i.e. collection of PersonCustomElementsInfoBean's
     */

    public Vector getOtherColumnElementData(){
        return getTableData();
    }

    /* 
       Method to get all the table data from JTable
       @return Vector, a Vector which consists of PersonCustomElementsInfoBean's 
     */

    private Vector getTableData(){

        Vector otherTableData = new Vector();
         if(tblPersonOtherDetail.getRowCount() > 0){
            if(tblPersonOtherDetail.isEditing()){
                int selRow = tblPersonOtherDetail.getSelectedRow();
                String txtvalue = ((javax.swing.text.JTextComponent)
                                tblPersonOtherDetail.getEditorComponent()).
                                getText();
                tblPersonOtherDetail.setValueAt(txtvalue,selRow,2);
                ((DefaultTableModel)tblPersonOtherDetail.getModel()).
                                        fireTableDataChanged();
                tblPersonOtherDetail.getCellEditor().cancelCellEditing();
            }
            int rowCount = tblPersonOtherDetail.getRowCount();
            PersonCustomElementsInfoBean depOthersBean;

            for(int inInd = 0 ; inInd < rowCount ; inInd++){

                //depOthersBean = new PersonCustomElementsInfoBean();

                depOthersBean = (PersonCustomElementsInfoBean)vecPersonColumnValues.elementAt(inInd);

                String pId = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,0);
                String cLabel = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,1);
                String cVal = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,2);
                boolean  hLookUp = ((Boolean)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,3)).booleanValue();
                String description = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,4);
                String lookWindow = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,5);
                String lookArgument = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,6);
                boolean  isReq = ((Boolean)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,7)).booleanValue();
                String dType = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,8);
                int dLen = ((Integer)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,9)).intValue();
                String colName = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,10);
                String acType = (String)((DefaultTableModel)
                    tblPersonOtherDetail.getModel()).getValueAt(inInd,11);

                depOthersBean.setPersonId(pId);
                depOthersBean.setColumnName(colName);
                depOthersBean.setColumnValue(cVal);
                depOthersBean.setColumnLabel(cLabel);
                depOthersBean.setDataType(dType);
                depOthersBean.setDataLength(dLen);
                depOthersBean.setHasLookUp(hLookUp);
                depOthersBean.setRequired(isReq);
                depOthersBean.setLookUpWindow(lookWindow);
                depOthersBean.setLookUpArgument(lookArgument);
                depOthersBean.setAcType(acType);

                otherTableData.addElement(depOthersBean);
            }//END FOR
        }//END ROW COUNT
        return otherTableData;
    }
    
    // This method does not allow to edit the JTable. This method is invoked 
    // based on the loggedin user validation
    
    private void setNotToMaintain(){
        tblPersonOtherDetail.setEnabled(false);
    }

      /* This method is used to set the cell editors to the columns,
       set the column width to each individual column, disable the column
       resizable feature to the JTable, setting single selection mode to the
       JTable */

    private void setEditors(){

        //createTableColumns();
        //tblPersonOtherDetail.setOpaque(true);
        tblPersonOtherDetail.getTableHeader().setReorderingAllowed(false);
        tblPersonOtherDetail.getTableHeader().setResizingAllowed(false);
        tblPersonOtherDetail.setSelectionMode(
                                DefaultListSelectionModel.SINGLE_SELECTION);

        TableColumn column = tblPersonOtherDetail.getColumnModel().getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblPersonOtherDetail.getColumnModel().getColumn(1);
        column.setMinWidth(170);
        column.setMaxWidth(170);
        column.setPreferredWidth(170);
        column.setCellRenderer(new ColumnFontRenderer());

        // For this Editor should be Text Field.. And Should have Key Listener.

        column = tblPersonOtherDetail.getColumnModel().getColumn(2);
        column.setMinWidth(150);
        column.setMaxWidth(150);
        column.setPreferredWidth(150);
        column.setCellRenderer(new ColumnValueRenderer());
        ColumnValueEditor columnValueEditor =
                                    new ColumnValueEditor(100);
        column.setCellEditor(columnValueEditor);

        column = tblPersonOtherDetail.getColumnModel().getColumn(3);
        column.setMinWidth(25);
        column.setMaxWidth(25);
        column.setPreferredWidth(25);
        
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));

        column = tblPersonOtherDetail.getColumnModel().getColumn(4);
        column.setMinWidth(120);
        column.setMaxWidth(120);
        column.setPreferredWidth(120);
        column.setCellRenderer(new ColumnFontRenderer());

        column = tblPersonOtherDetail.getColumnModel().getColumn(5);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblPersonOtherDetail.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblPersonOtherDetail.getColumnModel().getColumn(7);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setResizable(false);
        column.setPreferredWidth(0);

        column = tblPersonOtherDetail.getColumnModel().getColumn(8);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblPersonOtherDetail.getColumnModel().getColumn(9);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblPersonOtherDetail.getColumnModel().getColumn(10);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblPersonOtherDetail.getColumnModel().getColumn(11);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        tblPersonOtherDetail.getTableHeader().setVisible(false);
    }

    /*
    private Vector getTableValues(){
        return new Vector();
    }
     */

    /**
     * This method sets the custom elements data to the JTable. 
     * 
     */
    private void setFormData(){

        //PersonCustomElementsInfoBean departmentOthersFormBean = null;
        CustomElementsInfoBean othersFormBeanFromModule = null;
        PersonCustomElementsInfoBean departmentOthersFormBeanFromPerson = null;

        //Vector vecFinal = new Vector();
        if(vecPersonColumnValues != null ){
            if( vecModuleColumns != null){
                String cNameFromModule = null;
                String cNameFromPerson = null;
                int personColumnSize = vecPersonColumnValues.size();
                int modSize = vecModuleColumns.size();
                for(int indexMod = 0; indexMod < modSize; indexMod++){
                    boolean found = false;
                    othersFormBeanFromModule = (CustomElementsInfoBean)vecModuleColumns.get(indexMod);
                    
                    if(othersFormBeanFromModule != null){
                        cNameFromModule = othersFormBeanFromModule.getColumnName();
                    }
                   for(int indexPer = 0; indexPer < personColumnSize; indexPer++){
                       departmentOthersFormBeanFromPerson = (PersonCustomElementsInfoBean)vecPersonColumnValues.get(indexPer);
                        if(departmentOthersFormBeanFromPerson != null){
                            cNameFromPerson = departmentOthersFormBeanFromPerson.getColumnName();
                            
                        }
                        if(cNameFromModule != null && cNameFromPerson != null){
                            if(cNameFromModule.equalsIgnoreCase(cNameFromPerson)){
                                
                                departmentOthersFormBeanFromPerson.setDataType(othersFormBeanFromModule.getDataType());
                                departmentOthersFormBeanFromPerson.setDataLength(othersFormBeanFromModule.getDataLength());
                                //departmentOthersFormBeanFromPerson.setDefaultValue(othersFormBeanFromModule.getDefaultValue());
                                departmentOthersFormBeanFromPerson.setHasLookUp(othersFormBeanFromModule.isHasLookUp());
                                departmentOthersFormBeanFromPerson.setRequired(othersFormBeanFromModule.isRequired());
                                departmentOthersFormBeanFromPerson.setLookUpWindow(othersFormBeanFromModule.getLookUpWindow());
                                departmentOthersFormBeanFromPerson.setLookUpArgument(othersFormBeanFromModule.getLookUpArgument());
                                found = true;
                                break;
                            }
                        }
                   }
                    if( !found ) {
                        PersonCustomElementsInfoBean pCeBean = new PersonCustomElementsInfoBean(othersFormBeanFromModule);
                        pCeBean.setPersonId(personId);
                        pCeBean.setAcType(INSERT_RECORD);
                        vecPersonColumnValues.addElement(pCeBean);
                        saveRequired = true;
                    }
                }
            }
        }else{
            //vecFinal = vecModuleColumns;
            if(vecModuleColumns != null){
                vecPersonColumnValues = new Vector();
                int moduleColSize = vecModuleColumns.size();
                for(int indexMod = 0; indexMod < moduleColSize; indexMod++){

                    othersFormBeanFromModule = (CustomElementsInfoBean)vecModuleColumns.get(indexMod);
                    PersonCustomElementsInfoBean pCeBean = new PersonCustomElementsInfoBean(othersFormBeanFromModule);
                    pCeBean.setPersonId(personId);
                    pCeBean.setColumnValue(othersFormBeanFromModule.getDefaultValue());
                    pCeBean.setAcType(INSERT_RECORD);
                    vecPersonColumnValues.addElement(pCeBean);
                    saveRequired = true;
                }
            }
        }
        

        try{
        if(vecPersonColumnValues != null){

            int size = vecPersonColumnValues.size();
            Vector vcDataPopulate = new Vector();
            Vector vcData;
            lookupAvailable = new boolean[size];
            PersonCustomElementsInfoBean personOthersFormBean = null;
                    
            for(int index = 0; index < size; index++){
                personOthersFormBean = (PersonCustomElementsInfoBean)vecPersonColumnValues.get(index);
                if(personOthersFormBean != null){

                    String personId = personOthersFormBean.getPersonId();
                    String columnName = personOthersFormBean.getColumnName();
                    String columnValue = personOthersFormBean.getColumnValue();
                    String coulmnLabel = personOthersFormBean.getColumnLabel();
                    //Added for case# 3148 - Inconsistent UI for custom element tabs
                    coulmnLabel = coulmnLabel +": ";                    
                    String dataType = personOthersFormBean.getDataType();
                    int dataLength = personOthersFormBean.getDataLength();
                    //String defaultValue = personOthersFormBean.getDefaultValue();
                    boolean hasLookUp = personOthersFormBean.isHasLookUp();
                    lookupAvailable[index] = personOthersFormBean.isHasLookUp();
                    boolean isRequired = personOthersFormBean.isRequired();
                    String lookUpWindow = personOthersFormBean.getLookUpWindow();
                    String lookUpArgument = personOthersFormBean.getLookUpArgument();
                    String acType = personOthersFormBean.getAcType();
                    String description = personOthersFormBean.getDescription();

                    vcData = new Vector();
                    vcData.addElement(personId == null ? "" : personId);
                    vcData.addElement(coulmnLabel == null ? "" : coulmnLabel);
                    vcData.addElement(columnValue == null ? "" : columnValue);
                    vcData.addElement(new Boolean(hasLookUp));
                    vcData.addElement(description == null ? "" : description);
                    vcData.addElement(lookUpWindow == null ? "" : lookUpWindow);
                    vcData.addElement(lookUpArgument == null ? "" : lookUpArgument);
                    vcData.addElement(new Boolean(isRequired));
                    vcData.addElement(dataType == null ? "" : dataType);
                    vcData.addElement(new Integer(dataLength));
                    vcData.addElement(columnName == null ? "" : columnName);
                    vcData.addElement(acType == null ? "" : acType);

                    vcDataPopulate.addElement(vcData);
                }
            }
            ((DefaultTableModel)tblPersonOtherDetail.getModel()).
                            setDataVector(vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblPersonOtherDetail.getModel()).
                            fireTableDataChanged();
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* Method to get all the column names of JTable*/

     private Vector getColumnNames(){
        Enumeration enumColNames = tblPersonOtherDetail.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    /**
     * This method sets the Vector to the vecModuleColumns which contains 
     * the custom elements for a particular module code. i.e Collection of CustomElementsInfoBean
     **/
    public void setModuleColumns(Vector columnModules){
        this.vecModuleColumns = columnModules;
    }

    /**
     * This method sets the Vector to the vecPersonColumnValues which contains 
     * the custom elements for a particular person. i.e Collection of PersonCustomElementsInfoBean
     **/
    public void setPersonColumnValues(Vector columnValues){
        this.vecPersonColumnValues = columnValues;
    }

    // This method shows the lookup search window
    private void showLookupSearchWindow(OtherLookupBean otherLookupBean,String lookUpWindow,
                Vector vecLookupdata, String columnValue, int selectedRow){

            ComboBoxBean cBean = null;
            if(otherLookupBean != null){
                if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)) {
                    CostElementsLookupWindow costElementsLookupWindow =
                                        new CostElementsLookupWindow(otherLookupBean);
                } else {
                    CoeusTableWindow coeusTableWindow =
                                        new CoeusTableWindow(otherLookupBean);                    
                }
            }
            int selRow = otherLookupBean.getSelectedInd();
            if(vecLookupdata != null  && selRow >= 0){
                cBean = (ComboBoxBean)vecLookupdata.elementAt(selRow);
                if(cBean != null){
                    String code = (String)cBean.getCode();
                    String desc = (String)cBean.getDescription();
                    if(!code.equalsIgnoreCase(columnValue)){
                        saveRequired = true;
                        String acType = UPDATE_RECORD;
                        String prevAcType = (String)((DefaultTableModel)tblPersonOtherDetail.getModel()
                                                        ).getValueAt(selectedRow,11);
                        if(prevAcType != null){
                            if(!prevAcType.equalsIgnoreCase(INSERT_RECORD)){
                                ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                            ).setValueAt(acType,selectedRow,11);
                            }
                        }else{
                            //((DefaultTableModel)tblPersonOtherDetail.getModel()
                                       // ).setValueAt(acType,selectedRow,11);
                        }
                    }
                    ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                    ).setValueAt(code,selectedRow,2);
                    ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                        ).setValueAt(desc,selectedRow,4);
                    tblPersonOtherDetail.getSelectionModel().
                                                    setLeadSelectionIndex(selectedRow);

                    if(tblPersonOtherDetail.getCellEditor()!=null){
                        tblPersonOtherDetail.getCellEditor().cancelCellEditing();
                    }
                }
            }
        }


        //yy
        private void showSearchWindow(String searchType, String colValue, int selectedRow){
            try{

                CoeusSearch coeusSearch = new CoeusSearch(mdiForm, searchType, 1);
                coeusSearch.showSearchWindow();
                HashMap personInfo = coeusSearch.getSelectedRow();
                String pID = null;
                String name = null;
                if(personInfo!=null){
                    //Modified for case 3723 - Person Information is not saved. - start
//                     pID = Utils.
//                            convertNull(personInfo.get( "PERSON_ID" ));
//                    name = Utils.
//                            convertNull(personInfo.get( "FULL_NAME" ));
                    pID = Utils.
                            convertNull(personInfo.get( "ID" ));
                    name = Utils.
                            convertNull(personInfo.get( "NAME" ));
                    //Modified for case 3723 - Person Information is not saved. - end
                    if((pID!= null) && (!pID.equalsIgnoreCase(colValue))){
                        saveRequired = true;
                        String acType = UPDATE_RECORD;
                        String prevAcT = (String)((DefaultTableModel)tblPersonOtherDetail.getModel()
                                        ).getValueAt(selectedRow,11);
                        if(prevAcT != null){
                            if(!prevAcT.equalsIgnoreCase(INSERT_RECORD)){
                                ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                                ).setValueAt(acType,selectedRow,11);
                            }
                        }
                    }
                    ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                        ).setValueAt(pID,selectedRow,2);
                    ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                        ).setValueAt(name,selectedRow,4);
                    tblPersonOtherDetail.getSelectionModel().
                                                    setLeadSelectionIndex(selectedRow);
                    if(tblPersonOtherDetail.getCellEditor()!=null){
                        tblPersonOtherDetail.getCellEditor().cancelCellEditing();
                    }
                }

            }catch(Exception exp){
              exp.printStackTrace();
            }
        }

 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        scrPnPersonOtherDetail = new javax.swing.JScrollPane();
        tblPersonOtherDetail = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        scrPnPersonOtherDetail.setPreferredSize(new java.awt.Dimension(500, 235));
        tblPersonOtherDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "", "Button", "", "", "", "", "", "", "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPersonOtherDetail.setFont(CoeusFontFactory.getLabelFont());
        tblPersonOtherDetail.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblPersonOtherDetail.setSelectionForeground(new java.awt.Color(204, 204, 204));
        tblPersonOtherDetail.setRowHeight(23);
        tblPersonOtherDetail.setShowVerticalLines(false);
        tblPersonOtherDetail.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblPersonOtherDetail.setShowHorizontalLines(false);
        tblPersonOtherDetail.setGridColor(new java.awt.Color(204, 204, 204));
        tblPersonOtherDetail.setRowSelectionAllowed(false);
        tblPersonOtherDetail.setRowMargin(3);
        tblPersonOtherDetail.setOpaque(false);
        scrPnPersonOtherDetail.setViewportView(tblPersonOtherDetail);

        add(scrPnPersonOtherDetail, java.awt.BorderLayout.NORTH);

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblPersonOtherDetail;
    private javax.swing.JScrollPane scrPnPersonOtherDetail;
    // End of variables declaration//GEN-END:variables
    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    /**
     *Method to set edited row and column
     */
    private void setRowColumn(int row, int column){
        this.row = row;
        this.column = column;
    }
    /**
     *Method to get edited row
     */
    public int getRow(){
        return row;
    }
    /**
     *Method to get edited column
     */
    public int getColumn(){
        return column;
    }
    /**
     *Method to set action when save action is perfomed
     */
    public void setSaveAction(boolean save){
        this.saveDone = save;
    }
    //Case :#3149 - End
    //Button Cell Editor
    class ColumnValueEditor extends DefaultCellEditor
                                                implements TableCellEditor, Runnable{
        private JTextField txtDesc;
        String actualActionDescription = null;

        private int selectedRow ;

        private String personId;
        private String columnName;
        private String colValue;
        private String coulmnLabel;
        private String dataType;
        private int dataLength;
        private String defaultValue;
        private boolean hasLookUp;
        private boolean isRequired;
        private String lookUpWindow;
        private String lookUpArgument;
        boolean temporary;
        private CoeusSearch coeusSearch;
       //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        private int editorRow;
        private int editorColumn;
        private int tempRow = -1;
        private int tempColumn = -1;
        private JTable otherTabTable;
        //Case :#3149 - End
        //private final String PERSON_SEARCH = "personSearch";


        ColumnValueEditor(int len ){
            super(new JTextField());
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            if(functionType == DISPLAY_MODE){
                txtDesc.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtDesc.setEnabled(false);
            }else{
                txtDesc.setEnabled(true);
            }
            txtDesc.addFocusListener(new FocusAdapter(){
              public void focusGained(FocusEvent fe){
                temporary = false;
              }
                public void focusLost(FocusEvent fe){
                      //Case :#3149 Tabbing between fields does not work on Others tabs - Start
                        editableTableField = NON_EDITABLE_TEXT_FIELD;
                        //Case :#3149 - End
                    if (!fe.isTemporary() ){
                       if(!temporary){
                            stopCellEditing();
                       }
                    }
                }
            });
            
            txtDesc.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent keyEvent){
                }
            });
            
            txtDesc.addMouseListener(new MouseAdapter(){
  
                //Modified for COEUSDEV-184 : Need to click twice onb icons - Custom data, YNQ etc - Start
//                public void mouseClicked(MouseEvent mouseEvent){
//                    if(mouseEvent.getClickCount() == 2){
                public void mousePressed(MouseEvent mouseEvent){
                    if(mouseEvent.getClickCount() > 0){//COEUSDEV-184 : END    
                        String windowTitle;
                         if(lookUpWindow != null){
                            if(lookUpWindow.equalsIgnoreCase(PERSON_LOOKUP_WINDOW)){
                                showSearchWindow(PERSON_SEARCH, colValue, selectedRow);
                            }else if(lookUpWindow.equalsIgnoreCase(UNIT_LOOKUP_WINDOW)){
                                showSearchWindow(UNIT_SEARCH, colValue, selectedRow);
                            }else if(lookUpWindow.equalsIgnoreCase(ROLODEX_LOOKUP_WINDOW)){
                                showSearchWindow(ROLODEX_SEARCH, colValue, selectedRow);
                            // 4580: Add organization and sponsor search in custom elements - Start
                            }else if(lookUpWindow.equalsIgnoreCase(ORGANIZATION_SEARCH)){
                                showSearchWindow(ORGANIZATION_SEARCH, colValue, selectedRow);
                            }else if(lookUpWindow.equalsIgnoreCase(SPONSOR_SEARCH)){
                                showSearchWindow(SPONSOR_SEARCH, colValue, selectedRow); 
                            // 4580: Add organization and sponsor search in custom elements - End
                            }else if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
                                if(lookUpArgument != null){
                                    windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument,lookUpWindow);
                                    OtherLookupBean otherLookupBean =
                                        new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                                    showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, colValue, selectedRow);
                                }
                            }else if(lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW)){
                                windowTitle = "Lookup Values";
                                if(lookUpArgument != null){
                                    if(lookUpArgument.equalsIgnoreCase("Special review type")){
                                        Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument,lookUpWindow);
                                        OtherLookupBean otherLookupBean =
                                            new OtherLookupBean(windowTitle, vecLookupdata, VALUE_COLUMN_NAMES);
                                        showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, colValue, selectedRow);
                                    }
                                }
                            }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                                windowTitle = "Cost Elements";
                                if(lookUpArgument != null){
                                    windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument,lookUpWindow);
                                    OtherLookupBean otherLookupBean =
                                        new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                                    showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, colValue, selectedRow);
                                }
                            }
                        }
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){
            
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            if(colValue == null){
                colValue = "";
            }
            this.selectedRow = row;
            this.personId = (String)tblPersonOtherDetail.getValueAt(row,0);
            this.coulmnLabel = (String)tblPersonOtherDetail.getValueAt(row,1);
            this.colValue = (String)tblPersonOtherDetail.getValueAt(row,2);
            this.hasLookUp = ((Boolean)tblPersonOtherDetail.getValueAt(row,3)).booleanValue();
            this.defaultValue = (String)tblPersonOtherDetail.getValueAt(row,4);
            this.lookUpWindow = (String)tblPersonOtherDetail.getValueAt(row,5);
            this.lookUpArgument = (String)tblPersonOtherDetail.getValueAt(row,6);
            this.isRequired = ((Boolean)tblPersonOtherDetail.getValueAt(row,7)).booleanValue();
            this.dataType = (String)tblPersonOtherDetail.getValueAt(row,8);
            this.dataLength = ((Integer)tblPersonOtherDetail.getValueAt(row,9)).intValue();
            
            if(lookupAvailable[row]){
                //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                txtDesc.setEnabled(false);
                editableTableField = NON_EDITABLE_TEXT_FIELD;
                //Case :#3149 - End
                return txtDesc;
              }else{
                  //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
                  txtDesc.setEnabled(true);
                  editableTableField = EDITABLE_TEXT_FIELD;
                  editorRow = row;
                  editorColumn = column;
                  otherTabTable = table;
                  SwingUtilities.invokeLater(this);
                   //Case :#3149 - End
                  return txtDesc;
              }
        }
        
      //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
       /**
        * Method added to set focus for editable textfield
        */
     public void run() {
            boolean focus = txtDesc.requestFocusInWindow();
            if(!focus){
                if(tempRow != editorRow || saveDone){
                    otherTabTable.editCellAt(editorRow,editorColumn);
                    previousRow = editorRow;
                    previousCol = editorColumn;
                    txtDesc.requestFocusInWindow();
                    tempRow = editorRow;
                    tempColumn = editorColumn;
                    saveDone = false;
                }
                
            }else{
                tempRow = editorRow;
                tempColumn = editorColumn;
            }
            setRowColumn(editorRow,editorColumn);
        }
       //Case :#3149 - End
        
        
        private Vector getLookupValuesFromDB(String lookUpArgument, String lookUpWindow){
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
            RequesterBean request = new RequesterBean();
            Vector serverLookUpDataObject = null;
            if( lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW) ){
                request.setDataObject("DW_GET_ARG_VALUE_DESC_TBL_NEW");
            }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                request.setDataObject("DW_GET_COST_ELEMENTS");
            }else{
                request.setDataObject("DW_GET_ARG_CODE_TBL_NEW");
            }
            request.setId(lookUpArgument);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    serverLookUpDataObject = (Vector)response.getDataObject();
                }
                //Test
                if(serverLookUpDataObject != null){
                }
            }
            return serverLookUpDataObject;
        }
/*
        private Vector getLookupValuesFromDB(String lookUpArgument){
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
            RequesterBean request = new RequesterBean();
            Vector serverLookUpDataObject = null;
            request.setDataObject("DW_GET_ARG_CODE_TBL_NEW");
            request.setId(lookUpArgument);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    serverLookUpDataObject = (Vector)response.getDataObject();
                }
                //Test
                if(serverLookUpDataObject != null){
                }
            }
            return serverLookUpDataObject;
        }
 **/

        public int getClickCountToStart(){
            return 1;
        }
        
        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtDesc).getText();
        }
        
        public boolean stopCellEditing() {
            String editingValue = (String)getCellEditorValue();
            setEditorValueToTable(editingValue);
            return super.stopCellEditing();
        }
        
        private void setEditorValueToTable(String editingValue){
            
            if( (editingValue == null )){
                editingValue = (editingValue == null) ? "" : editingValue;
                ((JTextField)txtDesc).setText( editingValue);
                ((DefaultTableModel)tblPersonOtherDetail.getModel()).setValueAt(editingValue,selectedRow,2); // Handle
                tblPersonOtherDetail.getSelectionModel().
                                            setLeadSelectionIndex(selectedRow);
            }else{
                editingValue = editingValue.trim();
                //colValue = (colValue == null) ? "" : colValue;
                if(!editingValue.equalsIgnoreCase(colValue)){
                    saveRequired = true;
                    String acType = UPDATE_RECORD;
                    String prevAcType = (String)((DefaultTableModel)tblPersonOtherDetail.getModel()
                                    ).getValueAt(selectedRow,11);
                    if(!prevAcType.equalsIgnoreCase(INSERT_RECORD)){
                        ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                    ).setValueAt(acType,selectedRow,11);
                    }
                }
            }
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) && 
                        (colValue != null) && (colValue.trim().length()>= 0 )){
                saveRequired = true;
                String acType = UPDATE_RECORD;
                String prevAcType = (String)((DefaultTableModel)tblPersonOtherDetail.getModel()
                                    ).getValueAt(selectedRow,11);
                if(!prevAcType.equalsIgnoreCase(INSERT_RECORD)){
                    ((DefaultTableModel)tblPersonOtherDetail.getModel()
                                ).setValueAt(acType,selectedRow,11);
                }
            }
            ((DefaultTableModel)tblPersonOtherDetail.getModel()
                            ).setValueAt(editingValue,selectedRow,2);
            ((JTextField)txtDesc).setText( editingValue);
            tblPersonOtherDetail.getSelectionModel().
                                            setLeadSelectionIndex(selectedRow);
        }

        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }
    //TextField Cell Renderer
    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {
        
        private int selRow;
        private int selCol;
        
        public ColumnValueRenderer() {
          setOpaque(true);
          setFont(CoeusFontFactory.getNormalFont());
          if(functionType == DISPLAY_MODE){
                setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                setEnabled(false);
            }else{
                setEnabled(true);
            }
        }

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
              selRow = row;
              selCol = column;
              setText( (value == null) ? "" : value.toString() );
              return this;
        }
     }
    
    // custom renderer for setting custom font for the description column in the JTable.
    
    public class ColumnFontRenderer extends DefaultTableCellRenderer  {
        JLabel lblDesc = new JLabel(){
            public boolean isFocusTraversable() {
                return false;
            }
         };
                
        public ColumnFontRenderer() {
            super();
        }
        /** An overridden method to render the component(icon) in cell.
         * foreground/background for this cell and Font too.
         *
         * @param table the JTable that is asking the renderer to draw;
         * can be null
         * @param value the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value. For example,
         * if value is the string "true", it could be rendered as a string or
         * it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited, render
         * in the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the
         * header, the value of row is -1
         * @param column the column index of the cell being drawn
         * @return Component which is to be rendered.
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
         * boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
              /*Component cell = super.getTableCellRendererComponent
                    (table, value, isSelected, hasFocus, row, column);
              cell.setFont(CoeusFontFactory.getNormalFont());
              return cell;*/
              //Added for case# 3148 - Inconsistent UI for custom element tabs
              lblDesc.setBorder(null);                            
              if( column == 4) {  
                lblDesc.setFont(CoeusFontFactory.getNormalFont());
                //Added for case# 3148 - Inconsistent UI for custom element tabs
                lblDesc.setBorder(new CompoundBorder(new EmptyBorder(new Insets(1,4,1,4)),
                    lblDesc.getBorder()));                                                
              }else if( column == 1 ) {
                lblDesc.setFont(CoeusFontFactory.getLabelFont());
                //Added for case# 3148 - Inconsistent UI for custom element tabs
                lblDesc.setHorizontalAlignment(SwingConstants.RIGHT);                                                                            
              }
              if( value == null ){
                lblDesc.setText( "" );
              }else{
                lblDesc.setText( value.toString() );
              }
              return lblDesc;
        }
     }

    public class ButtonEditor extends DefaultCellEditor implements Runnable{

        protected JButton button;
        private int selRow ;

        private String personId;
        private String columnName;
        private String cValue;
        private String coulmnLabel;
        private String dataType;
        private int dataLength;
        private String defaultValue;
        private boolean isRequired;
        private boolean hasLookUp;
        private String lookUpWindow;
        private String lookUpArgument;
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        private int editorRow;
        private int editorColumn;
        private int tempRow = -1;
        private int tempColumn = -1;
        private JTable otherTabTable;
        //Case :#3149 - End
        private CoeusSearch coeusSearch;
        CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();

        public ButtonEditor(JCheckBox cf ) {
          super(cf);
          button = new JButton();
          button.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.FIND_ICON)));
          button.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
          button.setOpaque(true);
          button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                String windowTitle;
                //String[] codeColumnNames = {"Code", "Description"};
                //String[] valueColumnNames = {"Value", "Description"};
                if(lookUpWindow != null){
                    if(lookUpWindow.equalsIgnoreCase(PERSON_LOOKUP_WINDOW)){
                        showSearchWindow(PERSON_SEARCH, cValue, selRow);
                    }else if(lookUpWindow.equalsIgnoreCase(UNIT_LOOKUP_WINDOW)){
                        showSearchWindow(UNIT_SEARCH, cValue, selRow);
                    }else if(lookUpWindow.equalsIgnoreCase(ROLODEX_LOOKUP_WINDOW)){
                        showSearchWindow(ROLODEX_SEARCH, cValue, selRow);
                        // 4580: Add organization and sponsor search in custom elements - Start
                    }else if(lookUpWindow.equalsIgnoreCase(ORGANIZATION_SEARCH)){
                        showSearchWindow(ORGANIZATION_SEARCH, cValue, selRow);
                    }else if(lookUpWindow.equalsIgnoreCase(SPONSOR_SEARCH)){
                        showSearchWindow(SPONSOR_SEARCH, cValue, selRow);
                        // 4580: Add organization and sponsor search in custom elements - End
                    }else if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
                        if(lookUpArgument != null){
                            windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                            Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                            OtherLookupBean otherLookupBean =
                                new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                            showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow);
                        }
                    }else if(lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW)){
                        windowTitle = "Lookup Values";
                        if(lookUpArgument != null){
                            //Commented to get the search window.
                            //if(lookUpArgument.equalsIgnoreCase("Special review type")){
                                Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                                OtherLookupBean otherLookupBean =
                                    new OtherLookupBean(windowTitle, vecLookupdata, VALUE_COLUMN_NAMES);
                                showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow);
                            //}
                        }
                    }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                        windowTitle = "Cost Elements";
                        if(lookUpArgument != null){
                            windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                            Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                            OtherLookupBean otherLookupBean =
                                new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                            showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow);
                        }
                    }
                }
                  //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                    final JTable otherTabTable = tblPersonOtherDetail;
                    final int selectedRow = otherTabTable.getSelectedRow();
                    final int selectedtColumn = otherTabTable.getSelectedColumn();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            otherTabTable.editCellAt(selectedRow,selectedtColumn);
                            button.requestFocusInWindow();
                       }
                    });
                    //Case :#3149 - End
                fireEditingStopped();
            }
          });
        }

        private Vector getLookupValuesFromDB(String lookUpArgument, String lookUpWindow){
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
            RequesterBean request = new RequesterBean();
            Vector serverLookUpDataObject = null;
            if( lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW) ){
                request.setDataObject("DW_GET_ARG_VALUE_DESC_TBL_NEW");
            }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                request.setDataObject("DW_GET_COST_ELEMENTS");
            }else{
                request.setDataObject("DW_GET_ARG_CODE_TBL_NEW");
            }
            
            request.setId(lookUpArgument);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    serverLookUpDataObject = (Vector)response.getDataObject();
                }
            }
            return serverLookUpDataObject;
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                         boolean isSelected, int row, int column) {

          this.selRow = row;
          this.personId = (String)tblPersonOtherDetail.getValueAt(row,0);
          this.coulmnLabel = (String)tblPersonOtherDetail.getValueAt(row,1);
          this.cValue = (String)tblPersonOtherDetail.getValueAt(row,2);
          this.hasLookUp = ((Boolean)tblPersonOtherDetail.getValueAt(row,3)).booleanValue();
          this.defaultValue = (String)tblPersonOtherDetail.getValueAt(row,4);
          this.lookUpWindow = (String)tblPersonOtherDetail.getValueAt(row,5);
          this.lookUpArgument = (String)tblPersonOtherDetail.getValueAt(row,6);
          this.isRequired = ((Boolean)tblPersonOtherDetail.getValueAt(row,7)).booleanValue();
          this.dataType = (String)tblPersonOtherDetail.getValueAt(row,8);
          this.dataLength = ((Integer)tblPersonOtherDetail.getValueAt(row,9)).intValue();
          if(lookupAvailable[row]){
           //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
            tblPersonOtherDetail.setCellSelectionEnabled(true);
            editorRow = row;
            editorColumn = column;
            previousCol = column;
            previousRow = row;
            otherTabTable = table;
            SwingUtilities.invokeLater(this);
            editableTableField = VISIBLE_BUTTON;
            //Case :#3149 -  End
            return button;
          }else{
              
            //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
            editableTableField = INVISIBLE_BUTTON;
             //Case :#3149 - End
            return null;
          }
        }
        
       //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
       /**
        * Method added to set focus for editable textfield
        */
       public void run() {
           boolean focus = button.requestFocusInWindow();
           if(!focus){
               if(tempRow != editorRow){
                   otherTabTable.editCellAt(editorRow,editorColumn);
                   previousCol = editorColumn;
                   previousRow = editorRow;
                   button.requestFocusInWindow();
                   tempRow = editorRow;
                   tempColumn = editorColumn;
               }
           }else{
               tempRow = editorRow;
               tempColumn = editorColumn;
           }
           setRowColumn(editorRow,editorColumn);
       }
       //Case :#3149 - End
        

        public int getClickCountToStart(){
            return 1;
        }

        public boolean stopCellEditing() {
          return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }

     //Button Cell Renderer
    public class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
          setOpaque(true);
          setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.FIND_ICON)));
          setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
        
        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
              if(lookupAvailable[row]){
                  return this;
              }else{
                  return null;
              }
       }
     }
}
