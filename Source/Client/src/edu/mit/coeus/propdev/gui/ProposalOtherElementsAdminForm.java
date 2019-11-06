/**
 * @(#)ProposalOtherElementsAdminForm.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.gui;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import edu.mit.coeus.propdev.bean.*;
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
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.departmental.gui.*;


/**
 * <CODE>ProposalOtherElementsAdminForm </CODE>is a form object which display
 * all the Other column values and it is used to <CODE> add/modify </CODE> the other elements.
 * This class will be instantiated from <CODE>ProposalDetailsForm</CODE>.
 * @version 1.0 March 13, 2003
 * @author Raghunath P.V.
 */

public class ProposalOtherElementsAdminForm extends javax.swing.JComponent implements TypeConstants, LookUpWindowConstants{

    //private ProposalCustomElementsInfoBean proposalOthersFormBean;
    //private ProposalCustomElementsInfoBean newProposalOthersFormBean;
    //private ProposalCustomElementsInfoBean oldProposalOthersFormBean;
    private char functionType;
    //private boolean canMaintain;
    //private Vector vecModuleColumns;
    private Vector vecPersonColumnValues;
    private edu.mit.coeus.utils.Utils Utils;
    private boolean saveRequired;
    private String proposalId;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    private static final int DEFAULT__SIZE = 2000;

    //private final static int TWO_TABS = 1;
    //Case :#3149 – Tabbing between fields does not work on Others tabs - Start
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
    private int mandatoryRow;
    private int mandatoryColumn;
    private boolean otherTabMandatory = false;
    private int previousRow;
    private int previousCol;
    private int row ;
    private int column;
    private boolean saveDone = false;
    //Case :#3149 - End
    
    //private boolean hasLookUp;
    private boolean lookupAvailable[];
    /** Creates new form ProposalOtherElementsAdminForm */
    public ProposalOtherElementsAdminForm() {

    }

    /** Creates new form <CODE>ProposalOtherElementsAdminForm</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param maintainOther is a boolean variable which specifies that the others tab is maintainaable or not
     * @param columnModules is a Vector of Custom values for a module
     * @param columnValues is a Vector of Custom values for a selected person
     * @param id is the String representing the selected Person
     * 'D' specifies that the form is in Display Mode
     */

    public ProposalOtherElementsAdminForm(char functionType, Vector columnValues, String propId) {

        this.functionType = functionType;
        this.vecPersonColumnValues = columnValues;
        this.proposalId = propId;
        this.mdiForm = CoeusGuiConstants.getMDIForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        scrPnPersonOtherDetail.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        if(functionType == DISPLAY_MODE){
            setNotToMaintain();
        }
        setFormData();
        setEditors();
        
       //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
        tblProposalOtherDetail.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selectedColumn = tblProposalOtherDetail.getSelectedColumn();
                if((editableTableField == INVISIBLE_BUTTON) || (selectedColumn <2|| selectedColumn >3)){
                    setSaveAction(true);
                    tblProposalOtherDetail.setRowSelectionInterval(getRow(),getRow());
                    tblProposalOtherDetail.setColumnSelectionInterval(getColumn(),getColumn());
                    tblProposalOtherDetail.editCellAt(getRow(),getColumn());
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
        
        tblProposalOtherDetail.setFocusable(true);
        tblProposalOtherDetail.setEnabled(true);
        tblProposalOtherDetail.resetKeyboardActions();
        // Assigning no action when shift+tab keys are pressed inside table
        inputMap = tblProposalOtherDetail.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke shiftTab=KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.SHIFT_MASK);
        inputMap.put(shiftTab,"NoAction");
        
        // Assigning no action when enter key is pressed inside table
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        inputMap.put(enter,"NoAction");
        final Action  enterAction = tblProposalOtherDetail.getActionMap().get(inputMap.get(enter));
        
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
        oldTabAction = tblProposalOtherDetail.getActionMap().get(inputMap.get(tab));
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
        tblProposalOtherDetail.getActionMap().put(inputMap.get(tab), tabAction);
        
        
    }

    /**
     * Method to set focus to a cell in table when the other tabbed pane is selected
     */
    public void setTableFocus(){

        tblProposalOtherDetail.setRowSelectionInterval(0,0);
        tblProposalOtherDetail.setColumnSelectionInterval(3,3);

        boolean buttonFocus = tblProposalOtherDetail.editCellAt(0,3);
        tblProposalOtherDetail.setRowSelectionInterval(0,0);
        tblProposalOtherDetail.setColumnSelectionInterval(3,3);
        previousCol = 3;
        previousRow = 0;
        // When editCellAt method return null 
        // Focus is set to the second column component
            
        if(!buttonFocus){
            tblProposalOtherDetail.editCellAt(0,2);
            tblProposalOtherDetail.setRowSelectionInterval(0,0);
            tblProposalOtherDetail.setColumnSelectionInterval(2,2);
            previousCol = 2;
            previousRow = 0;
        }
    }
    
    /**
     *Method to get JTable object
     */
    public JTable getTable(){
        return tblProposalOtherDetail;
    }
    
    /**
     *Method to get LookUpAvailable boolean array
     */
    public boolean[] getLookUpAvailable(){
        return lookupAvailable;
    }
    //Case :#3149 - End
    
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
        if(tblProposalOtherDetail.isEditing()){
            tblProposalOtherDetail.getCellEditor().stopCellEditing();
        }
       
        return this.saveRequired;
    }

    /** This method is used for Validations.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception is a exception to be thrown in the client side.
     */
    public boolean validateData() throws Exception{
        //boolean valid=true;
        if(tblProposalOtherDetail.isEditing()){
            int selRow = tblProposalOtherDetail.getSelectedRow();
            String value = ((javax.swing.text.JTextComponent)
                            tblProposalOtherDetail.getEditorComponent()).
                            getText();
            tblProposalOtherDetail.setValueAt(value,selRow,2);
            ((DefaultTableModel)tblProposalOtherDetail.getModel()).
                                                fireTableDataChanged();
            tblProposalOtherDetail.getCellEditor().cancelCellEditing();
        }

        int rowCount = tblProposalOtherDetail.getRowCount();
        if(rowCount > 0){
            for(int inInd = 0; inInd < rowCount ;inInd++){

                boolean  isReq = ((Boolean)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,7)).booleanValue();
                String cVal = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,2);
                String dType = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,8);
                int dLen = ((Integer)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,9)).intValue();
                String cLabel = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,1);
                
                boolean  hLookUp = ((Boolean)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,3)).booleanValue();

                if(isReq){
                    if(cVal == null || cVal.trim().length() <= 0){
                        tblProposalOtherDetail.requestFocus();
                        tblProposalOtherDetail.setRowSelectionInterval(inInd,inInd);
                        if( ! hLookUp ) {
                            tblProposalOtherDetail.editCellAt(inInd,2);
                            tblProposalOtherDetail.getEditorComponent().requestFocusInWindow();
                        }
                        //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                        setMandatoryFields(inInd,2);
                        setOtherTabMandatory(true);
                        //Case :#3149 - End
                        errorMessage(cLabel + " is mandatory. Please enter a value for "+cLabel);
                        return false;
                        //break;
                    }
                    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                    else{
                        setOtherTabMandatory(false);
                    }
                    //Case :#3149 - End
                }
                if(dType != null && cVal != null && cVal.trim().length() > 0 ){
                    if(dType.equalsIgnoreCase("NUMBER")){
                        try{
                            Integer.parseInt(cVal);
                        }catch(NumberFormatException nfe){
                            tblProposalOtherDetail.requestFocus();
                            tblProposalOtherDetail.setRowSelectionInterval(inInd,inInd);
                            if( ! hLookUp ) {
                                tblProposalOtherDetail.editCellAt(inInd,2);
                                tblProposalOtherDetail.getEditorComponent().requestFocusInWindow();
                            }
                            //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                            setMandatoryFields(inInd,2);
                            setOtherTabMandatory(true);
                            //Case :#3149 - End
                            errorMessage("Value for "+cLabel + " should be numeric. ");
                            
                            return false;
                        }
                    }else if(dType.equalsIgnoreCase("DATE")){
                        DateUtils dateUtils = new DateUtils();
                        String resultDate = dateUtils.formatDate(cVal,"/-,:","MM/dd/yyyy");
                        if(resultDate == null){
                            tblProposalOtherDetail.requestFocus();
                            tblProposalOtherDetail.setRowSelectionInterval(inInd,inInd);
                            if( ! hLookUp ) {
                                tblProposalOtherDetail.editCellAt(inInd,2);
                                tblProposalOtherDetail.getEditorComponent().requestFocusInWindow();
                            }
                            //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
                            setMandatoryFields(inInd,2);
                            setOtherTabMandatory(true);
                            //Case :#3149 - End
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
                // Bug fix #1460 -start
//                if(len > dLen){
//                    tblProposalOtherDetail.requestFocus();
//                    tblProposalOtherDetail.setRowSelectionInterval(inInd,inInd);
//                    if( ! hLookUp ) {
//                        tblProposalOtherDetail.editCellAt(inInd,2);
//                        tblProposalOtherDetail.getEditorComponent().requestFocusInWindow();
//                    }
//                    errorMessage("Value for "+cLabel + " cannot be more than "+ dLen +" characters long.");
//                    return false;
//                    //break;
//                }// Bug fix #1460 -end
            }
        }
        return true;
    }
    
    //Case :#3149 - Tabbing between fields does not work on Others tabs -Start
    /**
     * Method to get and set the mandatory row and column
     */
    public void setMandatoryFields(int row,int column){
        mandatoryRow = row;
        mandatoryColumn = 2;
    }
    
    /**
     * Method to get mandatory row 
     */
    public int getMandatoryRow(){
        return mandatoryRow;
    }
    /**
     * Method to get  mandatory column
     */
    public int getMandatoryColumn(){
        return mandatoryColumn;
    }
    /**
     * Method to set when manadtory dialog box is displayed
     */
    private void setOtherTabMandatory(boolean mandatory){
        this.otherTabMandatory = mandatory;
    }
    /**
     * Method to get boolean value whether mandatory dialog box is displayed or not
     */
    public boolean getOtherTabMandatory(){
        return otherTabMandatory;
    }
    //Case :#3149 - End
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */

    public static void errorMessage(String mesg) throws Exception {
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        //modified for the Coeus Enhancement case:#1823 as special review tab is added ,the other tab will be the 7th tab
        coeusUIException.setTabIndex(7);
       
        throw coeusUIException;
    }

    /**
     *  This method gives you the Vector of ProposalCustomElementsInfoBeans
     * @return Vector
     */
    public Vector getOtherColumnElementData(){
        //printBeanData(getTableData());
        return getTableData();
    }

    /* Method to get all the table data from JTable
       @return Vector, a Vector which consists of ProposalCustomElementsInfoBean's */

    private Vector getTableData(){

        Vector otherTableData = new Vector();
         if(tblProposalOtherDetail.getRowCount() > 0){
            if(tblProposalOtherDetail.isEditing()){
                int selRow = tblProposalOtherDetail.getSelectedRow();
                String txtvalue = ((javax.swing.text.JTextComponent)
                                tblProposalOtherDetail.getEditorComponent()).
                                getText();
                tblProposalOtherDetail.setValueAt(txtvalue,selRow,2);
                ((DefaultTableModel)tblProposalOtherDetail.getModel()).
                                        fireTableDataChanged();
                tblProposalOtherDetail.getCellEditor().cancelCellEditing();
            }
            int rowCount = tblProposalOtherDetail.getRowCount();
            ProposalCustomElementsInfoBean propOthersBean;

            for(int inInd = 0 ; inInd < rowCount ; inInd++){

                //propOthersBean = new ProposalCustomElementsInfoBean();

                propOthersBean = (ProposalCustomElementsInfoBean)vecPersonColumnValues.elementAt(inInd);

                String pId = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,0);
                String cLabel = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,1);
                String cVal = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,2);
                boolean  hLookUp = ((Boolean)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,3)).booleanValue();
                String description = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,4);
                String lookWindow = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,5);
                String lookArgument = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,6);
                boolean  isReq = ((Boolean)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,7)).booleanValue();
                String dType = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,8);
                int dLen = ((Integer)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,9)).intValue();
                String colName = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,10);
                String acType = (String)((DefaultTableModel)
                    tblProposalOtherDetail.getModel()).getValueAt(inInd,11);

                propOthersBean.setPersonId(pId);
                propOthersBean.setColumnName(colName);
                propOthersBean.setColumnValue(cVal);
                propOthersBean.setColumnLabel(cLabel);
                propOthersBean.setDataType(dType);
                propOthersBean.setDataLength(dLen);
                propOthersBean.setHasLookUp(hLookUp);
                propOthersBean.setRequired(isReq);
                propOthersBean.setLookUpWindow(lookWindow);
                propOthersBean.setLookUpArgument(lookArgument);
                propOthersBean.setAcType(acType);
                propOthersBean.setProposalNumber(proposalId);

                otherTableData.addElement(propOthersBean);
            }//END FOR
        }//END ROW COUNT
        return otherTableData;
    }

    private void setNotToMaintain(){
        tblProposalOtherDetail.setEnabled(false);
    }

      /* This method is used to set the cell editors to the columns,
       set the column width to each individual column, disable the column
       resizable feature to the JTable, setting single selection mode to the
       JTable */

    private void setEditors(){

        //createTableColumns();
        tblProposalOtherDetail.getTableHeader().setReorderingAllowed(false);
        tblProposalOtherDetail.getTableHeader().setResizingAllowed(false);
        tblProposalOtherDetail.setSelectionMode(
                                DefaultListSelectionModel.SINGLE_SELECTION);
        tblProposalOtherDetail.setCellSelectionEnabled( false );
        tblProposalOtherDetail.setOpaque(true);

        TableColumn column = tblProposalOtherDetail.getColumnModel().getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblProposalOtherDetail.getColumnModel().getColumn(1);
        column.setMinWidth(170);
        column.setMaxWidth(170);
        column.setPreferredWidth(170);
        column.setCellRenderer(new ColumnFontRenderer());
        // For this Editor should be Text Field.. And Should have Key Listener.

        column = tblProposalOtherDetail.getColumnModel().getColumn(2);
        column.setMinWidth(150);
        column.setMaxWidth(150);
        column.setPreferredWidth(150);
        column.setCellRenderer(new ColumnValueRenderer());
        ColumnValueEditor columnValueEditor =
                                    new ColumnValueEditor(100);
        column.setCellEditor(columnValueEditor);

        column = tblProposalOtherDetail.getColumnModel().getColumn(3);
        column.setMinWidth(25);
        column.setMaxWidth(25);
        column.setPreferredWidth(25);
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));

        column = tblProposalOtherDetail.getColumnModel().getColumn(4);
        column.setMinWidth(300);
        column.setMaxWidth(300);
        column.setPreferredWidth(300);
        column.setCellRenderer(new ColumnFontRenderer());
        
        column = tblProposalOtherDetail.getColumnModel().getColumn(5);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblProposalOtherDetail.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblProposalOtherDetail.getColumnModel().getColumn(7);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setResizable(false);
        column.setPreferredWidth(0);

        column = tblProposalOtherDetail.getColumnModel().getColumn(8);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblProposalOtherDetail.getColumnModel().getColumn(9);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblProposalOtherDetail.getColumnModel().getColumn(10);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        column = tblProposalOtherDetail.getColumnModel().getColumn(11);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);

        tblProposalOtherDetail.getTableHeader().setVisible(false);
    }
    
    /** Method to set the data in the JTable.
     * This method sets the data which is available in ProposalCostElementsInfoBean's
     * Vector to JTable.
     */
    private void setFormData(){

        ProposalCustomElementsInfoBean proposalOthersFormBean = null;

        if(vecPersonColumnValues != null){

            int size = vecPersonColumnValues.size();
            Vector vcDataPopulate = new Vector();
            Vector vcData;
            lookupAvailable = new boolean[size];
            for(int index = 0; index < size; index++){
                proposalOthersFormBean = (ProposalCustomElementsInfoBean)vecPersonColumnValues.get(index);
                if(proposalOthersFormBean != null){

                    String personId = proposalOthersFormBean.getPersonId();
                    String columnName = proposalOthersFormBean.getColumnName();
                    String columnValue = proposalOthersFormBean.getColumnValue();
                    String coulmnLabel = proposalOthersFormBean.getColumnLabel();
                    //Added for case#3148 - Inconsistent UI for custom element tabs
                    coulmnLabel = coulmnLabel +":";                    
                    String dataType = proposalOthersFormBean.getDataType();
                    int dataLength = proposalOthersFormBean.getDataLength();
                    String defaultValue = proposalOthersFormBean.getDefaultValue();
                    boolean hasLookUp = proposalOthersFormBean.isHasLookUp();
                    lookupAvailable[index] = proposalOthersFormBean.isHasLookUp();
                    boolean isRequired = proposalOthersFormBean.isRequired();
                    String lookUpWindow = proposalOthersFormBean.getLookUpWindow();
                    String lookUpArgument = proposalOthersFormBean.getLookUpArgument();
                    String acType = proposalOthersFormBean.getAcType();
                    String description = proposalOthersFormBean.getDescription();
                    //to fire save required if any of the columns have actype as "I" or
                    // if in modify mode, if any column is required and there is no data
                    if( INSERT_RECORD.equals(acType) || (isRequired && ( columnValue == null || columnValue.trim().length() == 0 ) ) ){
                        saveRequired = true;
                    }
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
            ((DefaultTableModel)tblProposalOtherDetail.getModel()).
                            setDataVector(vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblProposalOtherDetail.getModel()).
                            fireTableDataChanged();
        }
    }

    /* Method to get all the column names of JTable*/

     private Vector getColumnNames(){
        Enumeration enumColNames = tblProposalOtherDetail.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

     /**
     *  This method is used to set the data to the JTable.
     *  @param columnValues is a vector containing ProposalCostElementsInfoBean's
     **/
    public void setPersonColumnValues(Vector columnValues){
        this.vecPersonColumnValues = columnValues;
        if(vecPersonColumnValues != null){
            setFormData();
        }else{
            ((DefaultTableModel)tblProposalOtherDetail.getModel()).setDataVector(
                new Object[][]{},getColumnNames().toArray());
        }
        setEditors();
    }
    
    /**
     * Helper method to display the Lookup Window when the Lookup button is Pressed
     */
    private void showLookupSearchWindow(OtherLookupBean otherLookupBean,
                String lookUpWindow, Vector vecLookupdata, String columnValue, 
                                                                int selectedRow){

            ComboBoxBean cBean = null;
            if(otherLookupBean != null){
                if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)) {
                    CostElementsLookupWindow costElementsLookupWindow =
                                        new CostElementsLookupWindow(otherLookupBean);
                } else {
                /** Sagin **/
                    CoeusTableWindow coeusTableWindow =
                                        new CoeusTableWindow(otherLookupBean);                    
                }
            }
            int selRow = otherLookupBean.getSelectedInd();
            if(vecLookupdata != null && selRow >= 0){
                cBean = (ComboBoxBean)vecLookupdata.elementAt(selRow);
                if(cBean != null){
                    String code = (String)cBean.getCode();
                    String desc = (String)cBean.getDescription();
                    if(!code.equalsIgnoreCase(columnValue)){
                        saveRequired = true;
                        String acType = (String)((DefaultTableModel)tblProposalOtherDetail.getModel()
                                            ).getValueAt(selectedRow,11);
                        if(acType == null || !acType.equalsIgnoreCase(INSERT_RECORD)){
                            acType = UPDATE_RECORD;
                            ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                            ).setValueAt(acType,selectedRow,11);
                        }
                    }
                    ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                    ).setValueAt(code,selectedRow,2);
                    ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                        ).setValueAt(desc,selectedRow,4);
                    tblProposalOtherDetail.getSelectionModel().
                                                    setLeadSelectionIndex(selectedRow);

                    if(tblProposalOtherDetail.getCellEditor()!=null){
                        tblProposalOtherDetail.getCellEditor().cancelCellEditing();
                    }
                }
            }
        }


        //Helper Method to show the Search Window 
    
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
                        String prevAcT = (String)((DefaultTableModel)tblProposalOtherDetail.getModel()
                                        ).getValueAt(selectedRow,11);
                        if(prevAcT != null){
                            if(!prevAcT.equalsIgnoreCase(INSERT_RECORD)){
                                ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                                ).setValueAt(acType,selectedRow,11);
                            }
                        }else{
                            ((DefaultTableModel)
                                tblProposalOtherDetail.getModel()).setValueAt(acType,selectedRow,11);
                        }
                    }
                    ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                        ).setValueAt(pID,selectedRow,2);
                    ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                        ).setValueAt(name,selectedRow,4);
                    ((DefaultTableModel)tblProposalOtherDetail.getModel()).fireTableRowsUpdated(selectedRow, selectedRow);
                    tblProposalOtherDetail.getSelectionModel().setLeadSelectionIndex(selectedRow);
                    if(tblProposalOtherDetail.getCellEditor()!=null){
                        tblProposalOtherDetail.getCellEditor().cancelCellEditing();
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
        tblProposalOtherDetail = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        scrPnPersonOtherDetail.setOpaque(false);
        tblProposalOtherDetail.setModel(new CustomTableModel());
        tblProposalOtherDetail.setFont(CoeusFontFactory.getLabelFont());
        tblProposalOtherDetail.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblProposalOtherDetail.setSelectionForeground(new java.awt.Color(204, 204, 204));
        tblProposalOtherDetail.setRowHeight(25);
        tblProposalOtherDetail.setShowVerticalLines(false);
        tblProposalOtherDetail.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblProposalOtherDetail.setShowHorizontalLines(false);
        tblProposalOtherDetail.setRowSelectionAllowed(false);
        tblProposalOtherDetail.setRowMargin(3);
        tblProposalOtherDetail.setOpaque(false);
        scrPnPersonOtherDetail.setViewportView(tblProposalOtherDetail);

        add(scrPnPersonOtherDetail, java.awt.BorderLayout.NORTH);

    }//GEN-END:initComponents

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
    public javax.swing.JScrollPane scrPnPersonOtherDetail;
    public javax.swing.JTable tblProposalOtherDetail;
    // End of variables declaration//GEN-END:variables
    
    /**
     *  This inner class creates a custom DefaultTableModel with Columns
     */
    public class CustomTableModel extends DefaultTableModel{

        public CustomTableModel(){
            super(new Object [][] {}, new String [] {
                "", "", "", "Button", "", "", "", "", "", "", "", ""});
        }

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
    }
  
    //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
    /**
     *Method toset selected row and column
     */
    private void setRowColumn(int row, int column){
        this.row = row;
        this.column = column;
    }
    
    /**
     *Method to get editing row
     */
    public int getRow(){
        return row;
    }
    /**
     *Method to get editing column
     */
    public int getColumn(){
        return column;
    }
    
    /**
     *Method to save action when save is performed
     */
    public void setSaveAction(boolean save){
        this.saveDone = save;
    }
    //Case :#3149 - End
    /**
     * This inner class is used as a Custom Cell editor for the columnValue column in the JTable.
     */
    class ColumnValueEditor extends AbstractCellEditor
                                                implements TableCellEditor, Runnable {
        private JTextField txtDesc;
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
        private boolean temporary;
        private CoeusSearch coeusSearch;
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        private int editorRow;
        private int editorColumn;
        private int tempRow = -1;
        private int tempColumn = -1;
        private JTable otherTabTable;
        //Case :#3149 - End
        //private final String PERSON_SEARCH = "personSearch";

        // Constructor which sets the length to the columnValue TextField 
        ColumnValueEditor(int len ){
            //super(new JTextField());
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            
            txtDesc.addFocusListener(new FocusAdapter(){
              public void focusGained(FocusEvent fe){
                temporary = false;
              }
                public void focusLost(FocusEvent fe){
                    //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
                    editableTableField = NON_EDITABLE_TEXT_FIELD;
                    //Case :#3149 - End
                    if (!fe.isTemporary() ){
                       if(!temporary){
                            stopCellEditing();
                       }
                    }
                }
            });
            
            txtDesc.addMouseListener(new MouseAdapter(){
                
                    //Modified for COEUSDEV-184 : Need to click twice onb icons - Custom data, YNQ etc - Start
//                public void mouseClicked(MouseEvent mouseEvent){
//                    if(mouseEvent.getClickCount() == 2){
                public void mousePressed(MouseEvent mouseEvent){
                    if(mouseEvent.getClickCount()> 0){//COEUSDEV-184 : END
                        String windowTitle;
                         if(lookUpWindow != null && lookUpWindow.trim().length() > 0 ){
//                            if(lookUpWindow.equalsIgnoreCase(PERSON_LOOKUP_WINDOW)){
//                                showSearchWindow(PERSON_SEARCH, colValue, selectedRow);
//                            }else if(lookUpWindow.equalsIgnoreCase(UNIT_LOOKUP_WINDOW)){
//                                showSearchWindow(UNIT_SEARCH, colValue, selectedRow);
//                            }else if(lookUpWindow.equalsIgnoreCase(ROLODEX_LOOKUP_WINDOW)){
//                                showSearchWindow(ROLODEX_SEARCH, colValue, selectedRow);
//                                
//                            }
                            if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
                                if(lookUpArgument != null){
                                    windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                                    OtherLookupBean otherLookupBean =
                                        new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                                    showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, colValue, selectedRow);
                                }
                            }else if(lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW)){
                                windowTitle = "Lookup Values";
                                if(lookUpArgument != null){
//                                    if(lookUpArgument.equalsIgnoreCase("Special review type")){
                                        Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
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
                                    
                                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                                    OtherLookupBean otherLookupBean =
                                        new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                                    showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, colValue, selectedRow);
                                }
                            }else{
                                showSearchWindow(lookUpWindow, colValue, selectedRow);
                            }
                         }
                        //fireEditingStopped();
                    }
                }
            });
        }
        /** This overridden to get the custom cell component in the
         * JTable.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param column column index
         * @return a Component which is a editor component to the JTable cell.
         */
        public Component getTableCellEditorComponent(JTable table,
            Object value,boolean isSelected,int row,int column){
            //saveRequired=true;
            colValue = (String)tblProposalOtherDetail.getValueAt(row,2);
            if(colValue == null){
                colValue = "";
            }
            this.selectedRow = row;
            this.personId = (String)tblProposalOtherDetail.getValueAt(row,0);
            this.coulmnLabel = (String)tblProposalOtherDetail.getValueAt(row,1);
            this.colValue = (String)tblProposalOtherDetail.getValueAt(row,2);
            this.hasLookUp = ((Boolean)tblProposalOtherDetail.getValueAt(row,3)).booleanValue();
            this.defaultValue = (String)tblProposalOtherDetail.getValueAt(row,4);
            this.lookUpWindow = (String)tblProposalOtherDetail.getValueAt(row,5);
            this.lookUpArgument = (String)tblProposalOtherDetail.getValueAt(row,6);
            this.isRequired = ((Boolean)tblProposalOtherDetail.getValueAt(row,7)).booleanValue();
            this.dataType = (String)tblProposalOtherDetail.getValueAt(row,8);
            this.dataLength = ((Integer)tblProposalOtherDetail.getValueAt(row,9)).intValue();
            // Bug fix #1460 -start
            if(dataLength <= 0)
                dataLength = DEFAULT__SIZE;
            // Bug fix #1460 -end
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
                txtDesc.setEnabled(false);
                //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
                editableTableField = NON_EDITABLE_TEXT_FIELD;
                //Case :#3149 - End
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
      
        //Helper method to set the editing Value to the JTable
        private void setEditorValueToTable(String editingValue){
            
            if( (editingValue == null )){
                editingValue = (editingValue == null) ? "" : editingValue;
                ((JTextField)txtDesc).setText( editingValue);
                ((DefaultTableModel)tblProposalOtherDetail.getModel()).setValueAt(editingValue,selectedRow,2); // Handle
                tblProposalOtherDetail.getSelectionModel().
                                            setLeadSelectionIndex(selectedRow);
            }else{
                editingValue = editingValue.trim();
                //colValue = (colValue == null) ? "" : colValue;
                if(!editingValue.equalsIgnoreCase(colValue)){
                    saveRequired = true;
                    String acType = (String)((DefaultTableModel)tblProposalOtherDetail.getModel()
                                            ).getValueAt(selectedRow,11);
                    if(acType == null || !acType.equalsIgnoreCase(INSERT_RECORD)){
                        acType = UPDATE_RECORD;
                        ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                        ).setValueAt(acType,selectedRow,11);
                    }
                }
            }
            //Bug fix by bijosh :
            // made colValue.trim().length()>=0 to colValue.trim().length()>0 in if condition
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) && 
                        (colValue != null) && (colValue.trim().length()> 0 )){
                saveRequired = true;
                String acType = (String)((DefaultTableModel)tblProposalOtherDetail.getModel()
                                            ).getValueAt(selectedRow,11);
                if(acType == null || !acType.equalsIgnoreCase(INSERT_RECORD)){
                    acType = UPDATE_RECORD;
                    ((DefaultTableModel)tblProposalOtherDetail.getModel()
                                    ).setValueAt(acType,selectedRow,11);
                }
            }
            ((DefaultTableModel)tblProposalOtherDetail.getModel()
                            ).setValueAt(editingValue,selectedRow,2);
            ((JTextField)txtDesc).setText( editingValue);
            tblProposalOtherDetail.getSelectionModel().
                                            setLeadSelectionIndex(selectedRow);
        }
        
        // Helper method which is used to get the lookup data from the database based on the lookup argument
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

        public int getClickCountToStart(){
            return 1;
        }
        
        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtDesc).getText();
        }
        /**
        * Forwards the message from the CellEditor to the delegate. Tell the 
        * editor to stop editing and accept any partially edited value as the 
        * value of the editor.
        * @return true if editing was stopped; false otherwise
        */        
        public boolean stopCellEditing() {
            String editingValue = (String)getCellEditorValue();
            setEditorValueToTable(editingValue);
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }
    //TextField Cell Renderer
    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {

        public ColumnValueRenderer() {
          setOpaque(true);
          setFont(CoeusFontFactory.getNormalFont());
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

              setText( (value ==null) ? "" : value.toString() );
              
              //Added by Amit 11/19/2003
              if(functionType == CoeusGuiConstants.DISPLAY_MODE)                  
              {
                  java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
                    
                    setBackground(bgListColor);    
             }
              else
              {
                    setBackground(Color.white);            
             }
              //End Amit
              
              return this;
        }
     }
     /**
     * This inner class is used as a Custom Cell editor for the hasLookup column in the JTable.
     */
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
          
          //Bug Fix:1077 Start 1
          button.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
          //Bug Fix:1077 End 1
          
          button.setMaximumSize(new java.awt.Dimension(23, 23));
          button.setMinimumSize(new java.awt.Dimension(23, 23));
          button.setPreferredSize(new java.awt.Dimension(23, 23));
          button.setOpaque(true);
          button.addActionListener(new ActionListener() {
              
            public void actionPerformed(ActionEvent e) {
                //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
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
            if(lookUpWindow != null && lookUpWindow.trim().length() > 0 ){
                
//                    if(lookUpWindow.equalsIgnoreCase(PERSON_LOOKUP_WINDOW)){
//
//                        showSearchWindow(PERSON_SEARCH, cValue, selRow);
//                    }else if(lookUpWindow.equalsIgnoreCase(UNIT_LOOKUP_WINDOW)){
//
//                        showSearchWindow(UNIT_SEARCH, cValue, selRow);
//                    }else if(lookUpWindow.equalsIgnoreCase(ROLODEX_LOOKUP_WINDOW)){
//
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
                        
//                            if(lookUpArgument.equalsIgnoreCase("Special review type")){
                        Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                        OtherLookupBean otherLookupBean =
                                new OtherLookupBean(windowTitle, vecLookupdata, VALUE_COLUMN_NAMES);
                        showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow);
//                            }
                    }
                }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                    
                    windowTitle = "Cost Elements";
                    if(lookUpArgument != null){
                        if( lookUpArgument.trim().length() > 0){
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
                 final JTable otherTabTable = tblProposalOtherDetail;
                    final int selectedRow = otherTabTable.getSelectedRow();
                    final int selectedColumn = otherTabTable.getSelectedColumn();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            otherTabTable.editCellAt(selectedRow,selectedColumn);
                            previousCol = selectedColumn;
                            previousRow = selectedRow;
                            button.requestFocusInWindow();
                       }
                    });
                    //Case :#3149 - End
            fireEditingStopped();
            
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
        
        
        
        
        /** This overridden to get the custom cell component in the
         * JTable.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param column column index
         * @return a Component which is a editor component to the JTable cell.
         */
        public Component getTableCellEditorComponent(JTable table, Object value,
                         boolean isSelected, int row, int column) {

          this.selRow = row;
          this.personId = (String)tblProposalOtherDetail.getValueAt(row,0);
          this.coulmnLabel = (String)tblProposalOtherDetail.getValueAt(row,1);
          this.cValue = (String)tblProposalOtherDetail.getValueAt(row,2);
          this.hasLookUp = ((Boolean)tblProposalOtherDetail.getValueAt(row,3)).booleanValue();
          this.defaultValue = (String)tblProposalOtherDetail.getValueAt(row,4);
          this.lookUpWindow = (String)tblProposalOtherDetail.getValueAt(row,5);
          this.lookUpArgument = (String)tblProposalOtherDetail.getValueAt(row,6);
          this.isRequired = ((Boolean)tblProposalOtherDetail.getValueAt(row,7)).booleanValue();
          this.dataType = (String)tblProposalOtherDetail.getValueAt(row,8);
          this.dataLength = ((Integer)tblProposalOtherDetail.getValueAt(row,9)).intValue();
          if(lookupAvailable[row]){
              
           //Case :#3149 - Tabbing between fields does not work on Others tabs - Start
            tblProposalOtherDetail.setCellSelectionEnabled(true);
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
        /**
        * Forwards the message from the CellEditor to the delegate. Tell the 
        * editor to stop editing and accept any partially edited value as the 
        * value of the editor.
        * @return true if editing was stopped; false otherwise
        */
        public boolean stopCellEditing() {
          return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }

     //Button Cell Renderer for haslookup button
    public class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            
            //Bug Fix:1077 Start 2
            setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
            //Bug Fix:1077 End 2
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
              if(lookupAvailable[row]){
                  return this;
              }else{
                  return null;
              }
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

}


