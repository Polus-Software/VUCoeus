/**
 * @(#)CustomElementsForm.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 09-AUG-2010
 * by keerthyjayaraj
 */
package edu.mit.coeus.utils.customelements;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.customelements.bean.*;
import edu.mit.coeus.gui.*;
import java.util.*;
import javax.swing.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
/**
 * <CODE>CustomElementsForm </CODE>is a form object which display
 * all the Other column values and it is used to <CODE> add/modify </CODE> the other elements.
 * This class will be instantiated from <CODE>CustomElementsForm</CODE>.
 * @version 1.0 March 13, 2003
 * @author Raghunath P.V.
 */
public class CustomElementsForm extends javax.swing.JComponent implements LookUpWindowConstants, TypeConstants, MouseListener{
    
    private char functionType;
    //    private boolean canMaintain;
    private Vector vecColumnValues;
    private edu.mit.coeus.utils.Utils Utils;
    private boolean saveRequired;
//    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    
    private boolean canMaintainProposal;
    private boolean fired;
//    private final static int TWO_TABS = 1;
    
    //private boolean hasLookUp;
    
    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    private InputMap inputMap;
    private Action oldTabAction,tabAction;
    private KeyStroke tab;
    // Variable to check TextFiled and Button is Editable or not
    private int editableTableField;
    
    // constant assigned for visible button in editor
    private final int VISIBLE_BUTTON = 0;
    
    //constant assigned for in-visible button in editor
    private final int INVISIBLE_BUTTON = 1;
    
    //constant assigned for editable text field in editor
    private final int EDITABLE_TEXT_FIELD = 2;
    
    //constant assigned for non-editable text field in editor
    private final int NON_EDITABLE_TEXT_FIELD = 3;
    private boolean negotiateFocus;
    private int previousRow;
    private int previousCol;
    private int mandatoryRow;
    private int mandatoryColumn;
    private boolean OtherTabMandatory = false;
    private int row ;
    private int column;
    private boolean saveDone = false;
    //Case :#3149 - End
    private boolean lookupAvailable[];
    
    /** Default Constructor */
    public CustomElementsForm() {
        this.mdiForm = CoeusGuiConstants.getMDIForm();
//        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        scrPnCustomElements.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
        tblCustomElements.addMouseListener(this);
        //Case :#3149 - End

    }
  
    /** Creates new form <CODE>CustomElementsForm</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param maintainOther is a boolean variable which specifies that the others tab is maintainaable or not
     * @param columnModules is a Vector of Custom values for a module
     * @param columnValues is a Vector of Custom values  
     * 'D' specifies that the form is in Display Mode
     */
    public CustomElementsForm(char functionType, boolean maintainOther, Vector columnValues) {
        
        this.functionType = functionType;
        this.canMaintainProposal = maintainOther;
        this.vecColumnValues = columnValues;
        this.mdiForm = CoeusGuiConstants.getMDIForm();
//        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        scrPnCustomElements.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        if((!canMaintainProposal) || (functionType == DISPLAY_MODE) ||
                (functionType == CoeusGuiConstants.AMEND_MODE)){
            setNotToMaintain();
        }
        setFormData();
        setEditors();
        //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
        tblCustomElements.addMouseListener(this);
        //Case :#3149 - End
    }
    
    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        int currentColumn = tblCustomElements.getSelectedColumn();
        int selectedColumn = tblCustomElements.getSelectedColumn();
        if((editableTableField == INVISIBLE_BUTTON) || (selectedColumn <1 || selectedColumn >2)){
            setSaveAction(true);
            tblCustomElements.setRowSelectionInterval(getRow(),getRow());
            tblCustomElements.setColumnSelectionInterval(getColumn(),getColumn());
            tblCustomElements.editCellAt(getRow(),getColumn());
        }
    }
    public void mouseClicked(MouseEvent e){}
    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e){}
    
    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e){}
    
    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e){}
    
    /**
     * Method to set tabbing between the fields in the table
     *
     **/
    public void setTabFocus(){
        tblCustomElements.setFocusable(true);
        tblCustomElements.setEnabled(true);
        tblCustomElements.resetKeyboardActions();
        // Assigning no action when shift+tab keys are pressed inside table
        inputMap = tblCustomElements.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke shiftTab=KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.SHIFT_MASK);
        inputMap.put(shiftTab,"NoAction");
        
        // Assigning no action when enter key is pressed inside table
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        inputMap.put(enter,"NoAction");
//        final Action  enterAction = tblCustomElements.getActionMap().get(inputMap.get(enter));
        
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
        oldTabAction = tblCustomElements.getActionMap().get(inputMap.get(tab));
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
        tblCustomElements.getActionMap().put(inputMap.get(tab), tabAction);
        
        
    }
    
    
    /**
     * Method to set focus to a cell in table when the other tabbed pane is selected
     */
    public void setTableFocus(){
        if(!negotiateFocus){
            tblCustomElements.setRowSelectionInterval(0,0);
        }
        tblCustomElements.setColumnSelectionInterval(2,2);
        
        boolean buttonFocus = tblCustomElements.editCellAt(0,2);
        if(!negotiateFocus){
            tblCustomElements.setRowSelectionInterval(0,0);
        }
        tblCustomElements.setColumnSelectionInterval(2,2);
        previousCol = 3;
        previousRow = 0;
        if(!buttonFocus){
            tblCustomElements.editCellAt(0,1);
            if(!negotiateFocus){
                tblCustomElements.setRowSelectionInterval(0,0);
            }
            tblCustomElements.setColumnSelectionInterval(1,1);
            previousCol = 3;
            previousRow = 0;
            
        }
        negotiateFocus = false;
        
    }
    /**
     * Method to set focus to a negoatiation cusotom data table
     */
    public void setFocusToNegotiation(boolean focus){
        this.negotiateFocus = focus;
    }
    
    //Case :#3149 - End
    
    /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
        //System.out.println("resetted saveRequired to:"+save);
    }
    
    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    
    public boolean isSaveRequired(){
        if(tblCustomElements.isEditing()){
            fired = true;
            tblCustomElements.getCellEditor().stopCellEditing();
        }
        return this.saveRequired;
    }
    
    /** This method is used for Validations.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception is a exception to be thrown in the client side.
     */
    public boolean validateData() throws Exception, CoeusUIException{
        
        if(tblCustomElements.isEditing()){
            tblCustomElements.getCellEditor().stopCellEditing();
        }
        
        int rowCount = tblCustomElements.getRowCount();
        if(rowCount > 0){
            
            for(int inInd = 0; inInd < rowCount ;inInd++){
                
                String cLabel = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,0);
                String cVal = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,1);
                boolean  isReq = ((Boolean)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,6)).booleanValue();
                //                System.out.println("isReq is "+isReq);
                String dType = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,7);
                int dLen = ((Integer)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,8)).intValue();
                boolean  hLookUp = ((Boolean)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,2)).booleanValue();
                
                if(isReq){
                    if(cVal == null){
                        tblCustomElements.requestFocus();
                        tblCustomElements.setRowSelectionInterval(inInd,inInd);
                        if( ! hLookUp && functionType != CoeusGuiConstants.DISPLAY_MODE) {
                            tblCustomElements.editCellAt(inInd,1);
                            tblCustomElements.getEditorComponent().requestFocusInWindow();
                        }
                        //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                        setMandatoryField(inInd,1);
                        setOtherTabMandatory(true);
                        //Case :#3149 - End
                        errorMessage(cLabel + " is mandatory. Please enter a value for "+cLabel);
                        return false;
                    }else if( cVal.trim().length() <= 0 ){
                        tblCustomElements.requestFocus();
                        tblCustomElements.setRowSelectionInterval(inInd,inInd);
                        if( ! hLookUp && functionType != CoeusGuiConstants.DISPLAY_MODE) {
                            tblCustomElements.editCellAt(inInd,1);
                            tblCustomElements.getEditorComponent().requestFocusInWindow();
                        }
                        //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                        setMandatoryField(inInd,1);
                        setOtherTabMandatory(true);
                        //Case :#3149 - End
                        errorMessage(cLabel + " is mandatory. Please enter a value for "+cLabel);
                        return false;
                    }
                    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                    else{
                        setOtherTabMandatory(false);
                    }
                    //Case :#3149 - End
                }
                if(dType != null && cVal != null && cVal.trim().length() > 0){
                    if(dType.equalsIgnoreCase("NUMBER")){
                        try{
                            Integer.parseInt(cVal);
                        }catch(NumberFormatException nfe){
                            tblCustomElements.requestFocus();
                            tblCustomElements.setRowSelectionInterval(inInd,inInd);
                            if( ! hLookUp && functionType != CoeusGuiConstants.DISPLAY_MODE) {
                                tblCustomElements.editCellAt(inInd,1);
                                tblCustomElements.getEditorComponent().requestFocusInWindow();
                            }
                            //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                            setMandatoryField(inInd,1);
                            setOtherTabMandatory(true);
                            //Case :#3149 - End
                            
                            // modfied by manoj to fix the bug id# 24 IRB-SystemTestingDl-01.xls
                            String mesg ="Value for "+cLabel + " should be numeric " +" maximum value is "+Integer.MAX_VALUE;
                            errorMessage(mesg);
                            return false;
                        }
                    }else if(dType.equalsIgnoreCase("DATE")){
                        DateUtils dateUtils = new DateUtils();
                        
                        String resultDate = dateUtils.formatDate(cVal,"/-,:.","MM/dd/yyyy");
                        if(resultDate == null){
                            tblCustomElements.requestFocus();
                            tblCustomElements.setRowSelectionInterval(inInd,inInd);
                            if( ! hLookUp && functionType != CoeusGuiConstants.DISPLAY_MODE) {
                                tblCustomElements.editCellAt(inInd,1);
                                tblCustomElements.getEditorComponent().requestFocusInWindow();
                            }
                            //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                            setMandatoryField(inInd,1);
                            setOtherTabMandatory(true);
                            //Case :#3149 - End
                            setMandatoryField(inInd,1);
                            setOtherTabMandatory(true);
                            errorMessage("Value for "+cLabel + " should be valid date. ");
                            return false;
                        }
                    }
                    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                    else{
                        setOtherTabMandatory(false);
                    }
                    //Case :#3149 - End
                }
                
                int len = cVal.trim().length();
                if((dLen > 0) && (len > dLen)){
                    tblCustomElements.requestFocus();
                    tblCustomElements.setRowSelectionInterval(inInd,inInd);
                    if( ! hLookUp && functionType != CoeusGuiConstants.DISPLAY_MODE) {
                        tblCustomElements.editCellAt(inInd,1);
                        tblCustomElements.getEditorComponent().requestFocusInWindow();
                    }
                    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                    setMandatoryField(inInd,1);
                    setOtherTabMandatory(true);
                    //Case :#3149 - End
                    errorMessage("Value for "+cLabel + " cannot be more than "+ dLen +" characters long.");
                    return false;
                    //break;
                }
                //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                else{
                    setOtherTabMandatory(false);
                }
                //Case :#3149 - End
                //}
            }
        }
        return true;
    }
    
    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
    /**
     * Method to set mandatory field row and column
     */
    private void setMandatoryField(int row, int column){
        mandatoryRow = row;
        mandatoryColumn = column;
    }
    
    /**
     * Method to get mandatory row
     */
    public int getmandatoryRow(){
        return mandatoryRow;
    }
    
    /**
     * Method to get mandatory column
     */
    public int getmandatoryColumn(){
        return mandatoryColumn;
    }
    
    /**
     * Method to get table
     */
    public JTable getTable(){
        return tblCustomElements;
    }
    
    /**
     * Method to get lookupavailable boolean array
     */
    public boolean[] getLookUpAvailable(){
        return lookupAvailable;
    }
    /**
     * Method to set boolean whether error message dialog box is displayed
     */
    private void setOtherTabMandatory(boolean mandatory){
        this.OtherTabMandatory = mandatory;
    }
    
    /**
     * Method to get boolean value 
     */
    public boolean getOtherTabMandatory(){
        return OtherTabMandatory;
    }
    //Case :#3149 - End
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */
    
    public void errorMessage(String mesg) throws CoeusUIException{
        //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        //Modified for case 3552 - IRB attachments - start
        //Modified for case 2176 - Risk Level Category - start
        
        coeusUIException.setTabIndex(13);
        //Modified for case 2176 - Risk Level Category - end
        //Modified for case 3552 - IRB attachments - end
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
    }
    
    /**
     * This method is used to get the Data from the Others Form.
     * @returns Vector containing all the table data. i.e. collection of CustomElementsInfoBean's
     */
    
    public Vector getOtherColumnElementData(){
        return getTableData();
    }
    
    /*
       Method to get all the table data from JTable
       @return Vector, a Vector which consists of CustomElementsInfoBean's
     */
    
    private Vector getTableData(){
        
        Vector otherTableData = new Vector();
        if(tblCustomElements.getRowCount() > 0){
            if(tblCustomElements.isEditing()){
                int selRow = tblCustomElements.getSelectedRow();
                //Condition Added for Case#4494 - In Protocol, Error on Other tab when custom elements are not defined - Start
                if(tblCustomElements.getEditorComponent() instanceof javax.swing.text.JTextComponent){ //Case#4494 - End
                    String txtvalue = ((javax.swing.text.JTextComponent)
                    tblCustomElements.getEditorComponent()).
                            getText();
                    tblCustomElements.setValueAt(txtvalue,selRow,1);
                    ((DefaultTableModel)tblCustomElements.getModel()).
                            fireTableDataChanged();
                    tblCustomElements.getCellEditor().cancelCellEditing();
                }
            }
            int rowCount = tblCustomElements.getRowCount();
            CustomElementsInfoBean customElementsInfoBean;
            
            for(int inInd = 0 ; inInd < rowCount ; inInd++){
                
                //customElementsInfoBean = new CustomElementsInfoBean();
                
                customElementsInfoBean = (CustomElementsInfoBean)vecColumnValues.elementAt(inInd);
                
                String cLabel = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,0);
                String cVal = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,1);
                boolean  hLookUp = ((Boolean)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,2)).booleanValue();
                String description = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,3);
                String lookWindow = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,4);
                String lookArgument = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,5);
                boolean  isReq = ((Boolean)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,6)).booleanValue();
                String dType = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,7);
                int dLen = ((Integer)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,8)).intValue();
                String colName = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,9);
                String acType = (String)((DefaultTableModel)
                tblCustomElements.getModel()).getValueAt(inInd,10);
                
                customElementsInfoBean.setColumnName(colName);
                customElementsInfoBean.setColumnValue(cVal);
                customElementsInfoBean.setColumnLabel(cLabel);
                customElementsInfoBean.setDataType(dType);
                customElementsInfoBean.setDataLength(dLen);
                customElementsInfoBean.setHasLookUp(hLookUp);
                customElementsInfoBean.setRequired(isReq);
                customElementsInfoBean.setLookUpWindow(lookWindow);
                customElementsInfoBean.setLookUpArgument(lookArgument);
                customElementsInfoBean.setAcType(acType);
                //System.out.println("bean in getTableData:"+customElementsInfoBean);
                otherTableData.addElement(customElementsInfoBean);
            }//END FOR
        }//END ROW COUNT
        //System.out.println("save req in getTableData:"+saveRequired);
        return otherTableData;
    }
    
    // This method does not allow to edit the JTable. This method is invoked
    // based on the loggedin user validation
    
    private void setNotToMaintain(){
        tblCustomElements.setEnabled(false);
    }
    
      /* This method is used to set the cell editors to the columns,
       set the column width to each individual column, disable the column
       resizable feature to the JTable, setting single selection mode to the
       JTable */
    
    private void setEditors(){
        
        tblCustomElements.getTableHeader().setReorderingAllowed(false);
        tblCustomElements.getTableHeader().setResizingAllowed(false);
        tblCustomElements.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = tblCustomElements.getColumnModel().getColumn(0);
        column.setMinWidth(170);
        column.setMaxWidth(170);
        column.setPreferredWidth(170);
        column.setResizable(false);
        column.setCellRenderer(new ColumnFontRenderer());
        
        // For this Editor should be Text Field.. And Should have Key Listener.
        
        column = tblCustomElements.getColumnModel().getColumn(1);
        column.setMinWidth(150);
        column.setMaxWidth(150);
        column.setPreferredWidth(150);
        column.setCellRenderer(new ColumnValueRenderer());
        ColumnValueEditor columnValueEditor =
        new ColumnValueEditor(100);
        column.setCellEditor(columnValueEditor);
        
        column = tblCustomElements.getColumnModel().getColumn(2);
        column.setMinWidth(23);
        column.setMaxWidth(23);
        column.setPreferredWidth(23);
        
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));
        
        column = tblCustomElements.getColumnModel().getColumn(3);
        column.setMinWidth(300);
        //        column.setMaxWidth(120);
        column.setPreferredWidth(300);
        column.setCellRenderer(new ColumnFontRenderer());
        
        column = tblCustomElements.getColumnModel().getColumn(4);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        column = tblCustomElements.getColumnModel().getColumn(5);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        column = tblCustomElements.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setResizable(false);
        column.setPreferredWidth(0);
        
        column = tblCustomElements.getColumnModel().getColumn(7);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        column = tblCustomElements.getColumnModel().getColumn(8);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        column = tblCustomElements.getColumnModel().getColumn(9);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        column = tblCustomElements.getColumnModel().getColumn(10);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        tblCustomElements.getTableHeader().setVisible(false);
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
        
        try{
            if(vecColumnValues != null){
                
                int size = vecColumnValues.size();
                Vector vcDataPopulate = new Vector();
                lookupAvailable = new boolean[size];
                CustomElementsInfoBean custElementsInfoBean = null;
                for(int index = 0; index < size; index++){
                    custElementsInfoBean = (CustomElementsInfoBean)vecColumnValues.get(index);
                    if(custElementsInfoBean != null){
                        
                        String columnName = custElementsInfoBean.getColumnName();
                        String columnValue = custElementsInfoBean.getColumnValue();
                        String coulmnLabel = custElementsInfoBean.getColumnLabel();
                        //Added for case#3148 - Right align of custom element label
                        if(coulmnLabel!=null && !coulmnLabel.endsWith(":")){
                            coulmnLabel = coulmnLabel +":";
                        }
                        String dataType = custElementsInfoBean.getDataType();
                        int dataLength = custElementsInfoBean.getDataLength();
                        String defaultValue = custElementsInfoBean.getDefaultValue();
                        boolean hasLookUp = custElementsInfoBean.isHasLookUp();
//                        lookupAvailable[index] = custElementsInfoBean.isHasLookUp();
                        boolean isRequired = custElementsInfoBean.isRequired();
                        String lookUpWindow = custElementsInfoBean.getLookUpWindow();
                        lookupAvailable[index] = hasLookUp && (lookUpWindow!=null) && !("".equals(lookUpWindow));
                        String lookUpArgument = custElementsInfoBean.getLookUpArgument();
                        String acType = custElementsInfoBean.getAcType();
                        String description = custElementsInfoBean.getDescription();
                        
                        Vector vcData = new Vector();
                        vcData.addElement(coulmnLabel == null ? "" : coulmnLabel);
                        if(acType != null){
                            if( !acType.equalsIgnoreCase(INSERT_RECORD) ){
                                vcData.addElement(columnValue == null ? "" : columnValue);
                            }else{
                                vcData.addElement(defaultValue == null ? "" : defaultValue);
                            }
                        }else{
                            vcData.addElement(columnValue == null ? "" : columnValue);
                        }
                        //                    if(acType != null && !acType.equalsIgnoreCase(INSERT_RECORD)){
                        //                        vcData.addElement(columnValue == null ? "" : columnValue);
                        //                    }else{
                        //                        vcData.addElement(defaultValue == null ? "" : defaultValue);
                        //                        if(!saveRequired){
                        //                            //saveRequired = true;
                        //                        }
                        //                    }
                        vcData.addElement(new Boolean(hasLookUp));
                        vcData.addElement(description == null ? "" : description);
                        vcData.addElement(lookUpWindow == null ? "" : lookUpWindow);
                        //                    System.out.println("In SetFormData isRequired value is "+isRequired);
                        //                    System.out.println("In SetFormData coulmnLabel value is "+coulmnLabel);
                        //                    System.out.println("****");
                        vcData.addElement(lookUpArgument == null ? "" : lookUpArgument);
                        vcData.addElement(new Boolean(isRequired));
                        vcData.addElement(dataType == null ? "" : dataType);
                        vcData.addElement(new Integer(dataLength));
                        vcData.addElement(columnName == null ? "" : columnName);
                        //vcData.addElement(acType == null ? "" : acType);
                        
// JM 7-5-2011 exclude NIH MECHANISM from custom elements page (Other tab)                        
                        if (columnName.contentEquals("NIH MECHANISM")) {
                        	// exclude
                        } else {                        
                        vcData.addElement(acType);
                        vcDataPopulate.addElement(vcData);
                        } // END
                    }
                }
                ((DefaultTableModel)tblCustomElements.getModel()).
                setDataVector(vcDataPopulate,getColumnNames());
                ((DefaultTableModel)tblCustomElements.getModel()).
                fireTableDataChanged();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /* Method to get all the column names of JTable*/
    
    private Vector getColumnNames(){
        Enumeration enumColNames = tblCustomElements.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /**
     * This method sets the Vector to the vecColumnValues which contains
     * the custom elements. i.e Collection of CustomElementsInfoBean
     **/
    public void setPersonColumnValues(Vector columnValues){
        this.vecColumnValues = columnValues;
        setFormData();
        setEditors();
        //Added for coeus4.3 enhancement
        if(functionType == CoeusGuiConstants.DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE){
            setNotToMaintain();
        } else {
            tblCustomElements.setEnabled(true);
        }
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
                    //System.out.println("setting saveReq to true...in showLookup");
                    String acType = UPDATE_RECORD;
                    String prevAcType = (String)((DefaultTableModel)tblCustomElements.getModel()
                    ).getValueAt(selectedRow,10);
                    if(prevAcType != null){
                        if(!prevAcType.equalsIgnoreCase(INSERT_RECORD)){
                            ((DefaultTableModel)tblCustomElements.getModel()
                            ).setValueAt(acType,selectedRow,10);
                        }
                    }else{
                        ((DefaultTableModel)
                        tblCustomElements.getModel()).setValueAt(acType,selectedRow,10);
                    }
                }
                ((DefaultTableModel)tblCustomElements.getModel()
                ).setValueAt(code,selectedRow,1);
                ((DefaultTableModel)tblCustomElements.getModel()
                ).setValueAt(desc,selectedRow,3);
                ((DefaultTableModel)tblCustomElements.getModel()).fireTableRowsUpdated(selectedRow, selectedRow);
                tblCustomElements.getSelectionModel().
                setLeadSelectionIndex(selectedRow);
                
                if(tblCustomElements.getCellEditor()!=null){
                    tblCustomElements.getCellEditor().cancelCellEditing();
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
                pID = Utils.
                convertNull(personInfo.get( "ID" ));
                name = Utils.
                convertNull(personInfo.get( "NAME" ));
                if((pID!= null) && (!pID.equalsIgnoreCase(colValue))){
                    saveRequired = true;
                    //System.out.println("setting saveReq to true...in showSearch");
                    String acType = UPDATE_RECORD;
                    String prevAcT = (String)((DefaultTableModel)tblCustomElements.getModel()
                    ).getValueAt(selectedRow,10);
                    if(prevAcT != null){
                        if(!prevAcT.equalsIgnoreCase(INSERT_RECORD)){
                            ((DefaultTableModel)tblCustomElements.getModel()
                            ).setValueAt(acType,selectedRow,10);
                        }
                    }else{
                        ((DefaultTableModel)
                        tblCustomElements.getModel()).setValueAt(acType,selectedRow,10);
                    }
                }
                ((DefaultTableModel)tblCustomElements.getModel()
                ).setValueAt(pID,selectedRow,1);
                ((DefaultTableModel)tblCustomElements.getModel()
                ).setValueAt(name,selectedRow,3);
                ((DefaultTableModel)tblCustomElements.getModel()).fireTableRowsUpdated(selectedRow, selectedRow);
                tblCustomElements.getSelectionModel().
                setLeadSelectionIndex(selectedRow);
                if(tblCustomElements.getCellEditor()!=null){
                    tblCustomElements.getCellEditor().cancelCellEditing();
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
        scrPnCustomElements = new javax.swing.JScrollPane();
        tblCustomElements = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        scrPnCustomElements.setMinimumSize(new java.awt.Dimension(700, 550));
        scrPnCustomElements.setPreferredSize(new java.awt.Dimension(700, 550));
        tblCustomElements.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblCustomElements.setFont(CoeusFontFactory.getLabelFont());
        tblCustomElements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ColumnLabel", "ColumnValue", "LookupButton", "CodeDescription", "LookupWindow", "LookupArgument", "isRequired", "DataType", "DataLength", "ColumnName", "AcType"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCustomElements.setGridColor(new java.awt.Color(204, 204, 204));
        tblCustomElements.setRowHeight(25);
        tblCustomElements.setRowMargin(3);
        tblCustomElements.setRowSelectionAllowed(false);
        tblCustomElements.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblCustomElements.setSelectionForeground(new java.awt.Color(204, 204, 204));
        tblCustomElements.setShowHorizontalLines(false);
        tblCustomElements.setShowVerticalLines(false);
        tblCustomElements.setOpaque(false);
        scrPnCustomElements.setViewportView(tblCustomElements);

        add(scrPnCustomElements, java.awt.BorderLayout.NORTH);

    }//GEN-END:initComponents
    
    /** Getter for property canMaintainProposal.
     * @return Value of property canMaintainProposal.
     */
    public boolean isCanMaintainProposal() {
        return canMaintainProposal;
    }
    
    /** Setter for property canMaintainProposal.
     * @param canMaintainProposal New value of property canMaintainProposal.
     */
    public void setCanMaintainProposal(boolean canMaintainProposal) {
        this.canMaintainProposal = canMaintainProposal;
        if((!canMaintainProposal) || (functionType == DISPLAY_MODE)
        || functionType == CoeusGuiConstants.AMEND_MODE){
            setNotToMaintain();
        }
        
    }
    
    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane scrPnCustomElements;
    public javax.swing.JTable tblCustomElements;
    // End of variables declaration//GEN-END:variables
     
   //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    /**
     *Method to set selected row and column
     */
    private void setRowColumn(int row, int column){
        this.row = row;
        this.column = column;
    }
    /**
     *Method to get the selected row
     *@return the Row number
     */
    public int getRow(){
        return row;
    }
    /**
     *Method to get selected Column
     *@return the column number
     */
    public int getColumn(){
        return column;
    }
    /**
     *Method to set boolean value when save action is performed
     *@param value save
     *              - boolean value
     */
    public void setSaveAction(boolean save){
        this.saveDone = save;
    }
    //Case :#3149 – End
    //Button Cell Editor
    class ColumnValueEditor extends AbstractCellEditor//DefaultCellEditor
    implements TableCellEditor, Runnable {
        private JTextField txtDesc;
        String actualActionDescription = null;
        
        private int selectedRow ;
        
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
//        private CoeusSearch coeusSearch;
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        private int editorRow;
        private int editorColumn;
        private int tempRow = -1;
        private int tempColumn = -1;
        private JTable otherTabTable;
        //Case :#3149 - End
        //private final String PERSON_SEARCH = "personSearch";
        
        
        ColumnValueEditor(int len ){
            //super(new JTextField());
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
                    if (!fe.isTemporary() ){
                        //Case :#3149 Tabbing between fields does not work on Others tabs - Start
                        editableTableField = NON_EDITABLE_TEXT_FIELD;
                        //Case :#3149 - End
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
//                        if(lookUpWindow != null && lookUpWindow.trim().length() >0 ){//Bug fix : Ajay Added lookUpWindow.trim().length() >0 since it was throwing excp for dbl click
                        if(lookupAvailable[selectedRow]){
                            //                            if(lookUpWindow.equalsIgnoreCase(PERSON_LOOKUP_WINDOW)){
                            //                                showSearchWindow(PERSON_SEARCH, colValue, selectedRow);
                            //                            }else if(lookUpWindow.equalsIgnoreCase(UNIT_LOOKUP_WINDOW)){
                            //                                showSearchWindow(UNIT_SEARCH, colValue, selectedRow);
                            //                            }else if(lookUpWindow.equalsIgnoreCase(ROLODEX_LOOKUP_WINDOW)){
                            //                                showSearchWindow(ROLODEX_SEARCH, colValue, selectedRow);
                            //                            }
                            if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
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
                                    //                                    if(lookUpArgument.equalsIgnoreCase("Special review type")){
                                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument,lookUpWindow);
                                    OtherLookupBean otherLookupBean =
                                    new OtherLookupBean(windowTitle, vecLookupdata, VALUE_COLUMN_NAMES);
                                    showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, colValue, selectedRow);
                                    //                                    }
                                }
                            }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                                windowTitle = "Cost Elements";
                                if(lookUpArgument != null){
                                    if( lookUpArgument.trim().length()>0 ) {
                                        windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                                    }
                                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument,lookUpWindow);
                                    OtherLookupBean otherLookupBean =
                                    new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                                    showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, colValue, selectedRow);
                                }
                            }else{
                                showSearchWindow(lookUpWindow, colValue, selectedRow);
                            }
                            //                            ((DefaultTableModel)tblCustomElements.getModel()).
                            //                                                                    fireTableDataChanged();
                        }
                        
                    }
                }
            });
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column){
            
            if(colValue == null){
                colValue = "";
            }
            this.selectedRow = row;
            this.coulmnLabel = (String)tblCustomElements.getValueAt(row,0);
            this.colValue = (String)tblCustomElements.getValueAt(row,1);
            this.hasLookUp = ((Boolean)tblCustomElements.getValueAt(row,2)).booleanValue();
            this.defaultValue = (String)tblCustomElements.getValueAt(row,3);
            this.lookUpWindow = (String)tblCustomElements.getValueAt(row,4);
            this.lookUpArgument = (String)tblCustomElements.getValueAt(row,5);
            this.isRequired = ((Boolean)tblCustomElements.getValueAt(row,6)).booleanValue();
            this.dataType = (String)tblCustomElements.getValueAt(row,7);
            this.dataLength = ((Integer)tblCustomElements.getValueAt(row,8)).intValue();
            //Added by Nadh Bug Fix - 1460
            if(dataLength <= 0)
                dataLength = 2000;
            //End
            if( dataType.equalsIgnoreCase("NUMBER") ){
                txtDesc.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,dataLength));
            }else if( dataType.equalsIgnoreCase("DATE") ){
                txtDesc.setDocument(new LimitedPlainDocument(11));
            }else {
                txtDesc.setDocument(new LimitedPlainDocument(dataLength));
            }
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            if(lookupAvailable[row]){
                //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                editableTableField = NON_EDITABLE_TEXT_FIELD;
                //Case :#3149 - End
                txtDesc.setEnabled(false);
                return txtDesc;
            }else{
                txtDesc.setEnabled(true);
                //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
                editableTableField = EDITABLE_TEXT_FIELD;
                editorRow = row;
                editorColumn = column;
                otherTabTable = table;
                SwingUtilities.invokeLater(this);
                //Case :#3149 - End
                
                //Bug Fix (Defect Id : 386)
                //                setSaveRequired(true);
                //System.out.println("setting saveReq to true...in getTableCellEditorComp");
                
                //End Bug Fix (Defect Id : 386)
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
                    txtDesc.requestFocusInWindow();
                    previousRow = editorRow;
                    previousCol = editorColumn;
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
            //Bug Fix 1427
            fired = true;
            //Bug Fix 1427
            setEditorValueToTable(editingValue);
            //Commented by sharath for Bug Fix(Defect Id : 386)
            //return super.stopCellEditing();
            //Bug Fix(Defect Id : 386)
            return true;
            //Bug Fix(Defect Id : 386)
        }
        
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
        
        private void setEditorValueToTable(String editingValue){
            if (!fired) return; // Added by Jobin to stop the firing on 4th Dec 2004 - start
            fired = false; //end
            if( (editingValue == null )){
                editingValue = (editingValue == null) ? "" : editingValue;
                ((JTextField)txtDesc).setText( editingValue);
                ((DefaultTableModel)tblCustomElements.getModel()).setValueAt(editingValue,selectedRow,1); // Handle
                tblCustomElements.getSelectionModel().
                setLeadSelectionIndex(selectedRow);
            }else{
                editingValue = editingValue.trim();
                //colValue = (colValue == null) ? "" : colValue;
                if(!editingValue.equalsIgnoreCase(colValue)){
                    //System.out.println("setting saveRequired...1");
                    saveRequired = true;
                    //System.out.println("setting saveReq to true...in setEditorValueToTable - 1");
                    String acType = UPDATE_RECORD;
                    String prevAcType = (String)((DefaultTableModel)tblCustomElements.getModel()
                    ).getValueAt(selectedRow,10);
                    if(prevAcType == null || !prevAcType.equalsIgnoreCase(INSERT_RECORD)){
                        ((DefaultTableModel)tblCustomElements.getModel()
                        ).setValueAt(acType,selectedRow,10);
                    }
                }
            }
            //Bug fix by bijosh :
            // made colValue.trim().length()>=0 to colValue.trim().length()>0 in if condition
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) &&
            (colValue != null) && (colValue.trim().length()>0 )){
                saveRequired = true;
                //System.out.println("setting saveReq to true...in setEditorValueToTable - 2");
                //System.out.println("setting savereq 2");
                String acType = UPDATE_RECORD;
                String prevAcType = (String)((DefaultTableModel)tblCustomElements.getModel()
                ).getValueAt(selectedRow,10);
                if(prevAcType == null || !prevAcType.equalsIgnoreCase(INSERT_RECORD)){
                    ((DefaultTableModel)tblCustomElements.getModel()
                    ).setValueAt(acType,selectedRow,10);
                }
            }
            ((DefaultTableModel)tblCustomElements.getModel()
            ).setValueAt(editingValue,selectedRow,1);
            ((JTextField)txtDesc).setText( editingValue);
            tblCustomElements.getSelectionModel().
            setLeadSelectionIndex(selectedRow);
            fireEditingStopped();
        }
        
    }
    //TextField Cell Renderer
    public class ColumnValueRenderer extends JTextField
    implements TableCellRenderer {
      
        private int selRow;
        private int selCol;
        
        public ColumnValueRenderer() {
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            if(functionType == DISPLAY_MODE){
                //Bug fix:1032 start for the grayed out fields in the display mode..
                setOpaque(false);
                //Bug fix end....
                
                //setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                setEnabled(false);
                setDisabledTextColor(Color.black);
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
            if( column == 3) {
                lblDesc.setFont(CoeusFontFactory.getNormalFont());
                //Added for case# 3148 - Inconsistent UI for custom element tabs
                lblDesc.setBorder(new CompoundBorder(new EmptyBorder(new Insets(1,4,1,4)),
                    lblDesc.getBorder()));                                
            }else if( column == 0 ) {
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
        
//        private CoeusSearch coeusSearch;
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        private int editorRow;
        private int editorColumn;
        private int tempRow = -1;
        private int tempColumn = -1;
        private JTable otherTabTable;
        //Case :#3149 - End
        CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
        
        public ButtonEditor(JCheckBox cf ) {
            super(cf);
            button = new JButton();
            
            //Bug Fix:1077 Start 1
            button.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
            //Bug Fix:1077 End 1
            
            button.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                    showDialog();
                    //Case :#3149 - End
                }
            });
        }
        
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        /**
         * Methos to display the dialog window
         */
        private void showDialog(){
            fireEditingStopped();
            String windowTitle;
//            if(lookUpWindow != null){
            if(lookupAvailable[selRow]){
                //                    if(lookUpWindow.equalsIgnoreCase(PERSON_LOOKUP_WINDOW)){
                //                        showSearchWindow(PERSON_SEARCH, cValue, selRow);
                //                    }else if(lookUpWindow.equalsIgnoreCase(UNIT_LOOKUP_WINDOW)){
                //                        showSearchWindow(UNIT_SEARCH, cValue, selRow);
                //                    }else if(lookUpWindow.equalsIgnoreCase(ROLODEX_LOOKUP_WINDOW)){
                //                        showSearchWindow(ROLODEX_SEARCH, cValue, selRow);
                //                    }
                if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
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
                        //if(lookUpArgument.equalsIgnoreCase("Special review type")){
                        Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                        OtherLookupBean otherLookupBean =
                                new OtherLookupBean(windowTitle, vecLookupdata, VALUE_COLUMN_NAMES);
                        showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow);
                        //}
                    }
                }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                    windowTitle = "Cost Elements";
                    if(lookUpArgument != null ){
                        if( lookUpArgument.trim().length()>0 ) {
                            windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                        }
                        Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                        OtherLookupBean otherLookupBean =
                                new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                        showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow);
                    }
                }else{
                    showSearchWindow(lookUpWindow, cValue, selRow);
                }
            }
            //Case :#3149 – Tabbing between fields does not work on others tabs - Start
            final JTable otherTabTable = tblCustomElements;
            final int selectedRow = getRow();
            final int selectedtColumn = getColumn();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    otherTabTable.editCellAt(selectedRow,selectedtColumn);
                    button.requestFocusInWindow();
                }
            });
            //Case :#3149 - End
            fireEditingStopped();
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
            }
            return serverLookUpDataObject;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
            
            this.selRow = row;
            this.coulmnLabel = (String)tblCustomElements.getValueAt(row,0);
            this.cValue = (String)tblCustomElements.getValueAt(row,1);
            this.hasLookUp = ((Boolean)tblCustomElements.getValueAt(row,2)).booleanValue();
            this.defaultValue = (String)tblCustomElements.getValueAt(row,3);
            this.lookUpWindow = (String)tblCustomElements.getValueAt(row,4);
            this.lookUpArgument = (String)tblCustomElements.getValueAt(row,5);
            this.isRequired = ((Boolean)tblCustomElements.getValueAt(row,6)).booleanValue();
            this.dataType = (String)tblCustomElements.getValueAt(row,7);
            this.dataLength = ((Integer)tblCustomElements.getValueAt(row,8)).intValue();
            if(lookupAvailable[row]){
                //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
                tblCustomElements.setCellSelectionEnabled(true);
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
            
            //Bug Fix:1077 Start 2
            setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
            //Bug Fix:1077 End 2
            
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
