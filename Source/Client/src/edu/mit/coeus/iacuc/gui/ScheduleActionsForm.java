/**
 * @(#)ScheduleActionsForm.java  1.0  18/11/2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */ 

package edu.mit.coeus.iacuc.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import edu.mit.coeus.iacuc.bean.OtherActionInfoBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;
import java.util.*;

/** <CODE>ScheduleActionsForm </CODE>is a form object which display
 * all the schedule other action details and it is used to <CODE> add/delete/modify </CODE> the schedule actions.
 * This class will be instantiated from <CODE>ScheduleDetailsForm</CODE>.
 * @author Raghunath P.V.
 * @version 1.0 November 18, 2002
 */

public class ScheduleActionsForm extends javax.swing.JComponent {
    
    /* This is used to hold the mode D for Display, I for Add, U for Modify */
    private char functionType;
    /* This is used to hold vector of data beans */
    private Vector otherActionsData;
    /* This is used to hold the reference of MDIFrame */
    private CoeusAppletMDIForm mdiReference;
    /* This is used to notify whether the Save is required */
    private boolean saveRequired = false;
    /* Boolean variable used to find the deleted record */
    private boolean found;
    /* Used to hold the table data as beans */
    private OtherActionInfoBean newOtherActionsBean;
    /* Used to hold the database data as beans */
    private OtherActionInfoBean oldOtherActionsBean;
    /* This is used to get all the available types in combobox */
    private Vector availableTypes;
    /* Holds CoeusMessageResources used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources;
    /* This member variable maintains the reference of scheduleID */
    private String scheduleID;   
    /* This member variable is used to hold the maximum item number in 
     * the client side */
    private int maxItemNumber;
    //holds the zero count value
    private static final int ZERO_COUNT = 0;
    // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - Start
    private static final String ERR_NO_ACTION_TYPE_EXIST = "iacucSchdActFrm_exceptionCode.1100";
    // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - End

    /** Creates a new <CODE>ScheduleActionsForm</CODE> <p>
     * <I> Default Constructor</I>
     */
    public ScheduleActionsForm() {
    }
    
    /** Constructor that instantiate <CODE>ScheduleActionsForm</CODE> and populate the components with the specified
     * data. It sets the enable status for the components depending on the functionType.
     * @param functionType is a <CODE>Char</CODE> variable which specify the mode, in which the
     * form object is to be displayed.
     * <B>'A'</B> specify that the form is in Add Mode
     * <B>'M'</B> specify that the form is in Modify Mode
     * <B>'D'</B> specify that the form is in Display Mode
     * @param vecActionsData is a <CODE>Vector</CODE> which consist the <CODE>OtherActionInfoBeans</CODE>
     */

    public ScheduleActionsForm(char functionType, 
                                            java.util.Vector vecActionsData) {
        this.otherActionsData = vecActionsData;
        this.functionType = functionType;
    }
    
    /** This method set the values of vecActionTypes to availableTypes Vector.
     * @param vecActionTypes is the Vector of <CODE>ComboBoxBean</CODE> instances.
     */
    public void setScheduleActionsType(Vector vecActionTypes){
        this.availableTypes = vecActionTypes;
    }
    
    /** This method set the vecActionsData to otherActionsData Vector.
     * @param vecActionsData is the Vector of <CODE>OtherActionInfoBean</CODE> data beans.
     */
    public void setScheduleActionsFormData(Vector vecActionsData){
        this.otherActionsData = vecActionsData;
    }
    
    /** This method set the schID to the scheduleID.
     * @param schID is a String to set to the scheduleId
     */
    public void setScheduleID(String schID){
        this.scheduleID = schID;
    }
    
    /** This method gets the scheduleId for the other actions.
     * @return a string representation of scheduleID.
     */
    public String getScheduleID(){
        return this.scheduleID;
    }
    
    /** This method is used to determine whether the data to be saved or not.
     * @return true if the modifications done, false otherwise.
     */
    
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
            if(tblOtherActions.getRowCount() > 0 ) {
                int rowNum = tblOtherActions.getSelectedRow();
                tblOtherActions.requestFocusInWindow();
                if(rowNum > 0){
                    tblOtherActions.setRowSelectionInterval(rowNum, rowNum);
                }
                tblOtherActions.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }                  
    }    
    //end Amit       
    
    /** This method is used to set the saveRequired variable.
     * @param save is a boolean variable to be set to saveRequired variable.
     */

    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }

    /** Method to get the functionType
     * @return a <CODE>Char</CODE> representation of functionType.
     */
    
    public char getFunctionType(){
        return functionType;
    }

    /** Method to set the functionType
     * @param fType is functionType to be set like 'D', 'I', 'M'
     */

    public void setFunctionType(char fType){ 
        this.functionType = fType;
    }
    
    /** This method is used for client side validations.
     * It validates the incomplete submission of <CODE>JTable</CODE> values.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception a exception if the validation fails.
     */
    
    public boolean validateData() throws Exception, CoeusUIException{
        boolean valid=true;
        if(tblOtherActions.isEditing()){
            int selRow = tblOtherActions.getSelectedRow();
            //Modified for Internal issue#86_1724-Schedule-Error when Other Actions deleted_start
            if(selRow != -1){
                String value =  "";                        
                 if(tblOtherActions.getEditorComponent() instanceof javax.swing.text.JTextComponent ){
                    value = ((javax.swing.text.JTextComponent)
                            tblOtherActions.getEditorComponent()).
                            getText();
                    tblOtherActions.setValueAt(value,selRow,2);
                 }     
             
                ((DefaultTableModel)tblOtherActions.getModel()).
                                                    fireTableDataChanged();
                tblOtherActions.getCellEditor().cancelCellEditing();
            }
            //Modified for Internal issue#86_1724-Schedule-Error when Other Actions deleted_end   
            
        }
        int rowCount = tblOtherActions.getRowCount();
        if(rowCount > 0){
            for(int inInd=0; inInd < rowCount ;inInd++){
                String stDescription=(String)((DefaultTableModel)
                    tblOtherActions.getModel()).getValueAt(inInd,2);
                if((stDescription == null) || 
                   (stDescription.trim().length() == 0)){                     
                    valid=false;
                    break;
                }
            }
            if(!valid){
                errorMessage(coeusMessageResources.parseMessageKey(
                                        "schdActFrm_exceptionCode.1152"));
                tblOtherActions.requestFocus();
                return false;
            }
        }
        return true;
    }
    
    /** This method is used to get all the JTable data.
     * @return a Vector of <CODE>OtherActionInfoBean</CODE> beans.
     */
    public Vector getScheduleActionsData(){

        if((otherActionsData!=null) &&
           (otherActionsData.size()>0) &&
           (tblOtherActions.getRowCount()<=0)){
               /*IF ALL THE ROWS ARE DELETED IN THE JTABLE THEN THE MAXIMUM 
                 ITEMNUMBER WILL BE ZERO.SINCE THERE WILL BE NO RECORDS IN 
                 THE TABLE */
               maxItemNumber=0;
                for(int oldIndex = 0;
                        oldIndex < otherActionsData.size();
                        oldIndex++){
                            oldOtherActionsBean = (OtherActionInfoBean)
                                        otherActionsData.elementAt(oldIndex);
                            oldOtherActionsBean.setAcType("D");
                            setSaveRequired(true);
                            otherActionsData.setElementAt(oldOtherActionsBean,
                                oldIndex);
                }
               return otherActionsData;
        }
        
        Vector newData = getTableValues();
        if((newData != null) && (newData.size() > 0)){

            for(int newLocIndex = 0; newLocIndex < newData.size();
                newLocIndex++){
                    int foundIndex = -1;
                    found = false;
                    newOtherActionsBean = (OtherActionInfoBean)newData.
                                                    elementAt(newLocIndex);
                    if(otherActionsData != null &&
                            otherActionsData.size() > 0){
                            for(int oldLocIndex = 0;
                                oldLocIndex < otherActionsData.size();
                                oldLocIndex++){
                                    oldOtherActionsBean = (OtherActionInfoBean)
                                    otherActionsData.elementAt(oldLocIndex);

                                oldOtherActionsBean.addPropertyChangeListener(
                                    new PropertyChangeListener(){
                                    public void propertyChange(
                                                    PropertyChangeEvent pce){
                                        oldOtherActionsBean.setAcType("U");
                                        setSaveRequired(true);
                                        
                                    }
                                }); 
                                if(newOtherActionsBean.getActionItemNumber() == 
                                    oldOtherActionsBean.getActionItemNumber()){
                                    found = true;
                                    foundIndex = oldLocIndex;
                                    break;
                                }
                            }
                    }else{
                        otherActionsData = new Vector();
                    }
                      if(!found){
                        /* if location is new set AcType to INSERT_RECORD */
                        newOtherActionsBean.setAcType("I");
                        setSaveRequired(true);
                        otherActionsData.addElement(newOtherActionsBean);
                      }else{
                        /* if present set the values to the bean. if modified,
                        bean will fire property change event */
                        if(oldOtherActionsBean != null){
                            oldOtherActionsBean.
                                setScheduleActTypeDesc(newOtherActionsBean.
                                    getScheduleActTypeDesc());
                            oldOtherActionsBean.
                                setScheduleActTypeCode(newOtherActionsBean.
                                    getScheduleActTypeCode());
                            oldOtherActionsBean.
                                setItemDescription(newOtherActionsBean.
                                    getItemDescription());
                            oldOtherActionsBean.setScheduleId(scheduleID);
                            if(foundIndex != -1){
                                otherActionsData.
                                   setElementAt(oldOtherActionsBean,foundIndex);
                            }
                        }
                    }
            }
            if(otherActionsData != null &&
                otherActionsData.size() > 0){
                    for(int oldLocIndex = 0;
                        oldLocIndex < otherActionsData.size();
                        oldLocIndex++){
                            found = false;
                            oldOtherActionsBean = (OtherActionInfoBean)
                                        otherActionsData.elementAt(oldLocIndex);
                            for(int newLocIndex = 0;
                            newLocIndex < newData.size();
                            newLocIndex++){
                                newOtherActionsBean = (OtherActionInfoBean)
                                                newData.elementAt(newLocIndex);
                                if(oldOtherActionsBean.getActionItemNumber() == 
                                    newOtherActionsBean.getActionItemNumber()){
                                    found=true;
                                    break;
                                }
                            }
                            if(!found){
                            /* if existing location has been deleted set acType
                               to DELETE_RECORD */
                                oldOtherActionsBean.setAcType("D");
                                setSaveRequired(true);
                                otherActionsData.
                                    setElementAt(oldOtherActionsBean,
                                            oldLocIndex);
                            }
                    }
            }
        }
        return otherActionsData;
    }
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */
    
    public void errorMessage(String mesg) throws Exception, CoeusUIException {
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(2);
        throw coeusUIException;
    }

     /** This method will be invoked from <CODE>ScheduleDetailsForm</CODE>. This method
      * is used to initialize the components with the specified data.
      * @param mdiForm is a reference of <CODE>CoeusAppletMDIForm</CODE>.
      * @return a JComponent containing all the components
      * with the data.
      */
    public JComponent showScheduleActionsForm(CoeusAppletMDIForm
     mdiForm){
        this.mdiReference = mdiForm;
        initComponents();

        // Added by Amit 11/21/2003
        java.awt.Component[] components = {tblOtherActions,btnAdd,btnDelete};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        // End chandra        
        
        formatFields();
        setFormData();
        setTableEditors();
        if( tblOtherActions.getRowCount() > ZERO_COUNT ){
            tblOtherActions.setRowSelectionInterval(ZERO_COUNT, ZERO_COUNT);
        }else{
            btnDelete.setEnabled(false);
        }
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }

    /* This method is used to set the cell editors to the columns, 
       set the column width to each individual column, disable the column 
       resizable feature to the JTable, setting single selection mode to the 
       JTable */
    private void setTableEditors(){
        
        tblOtherActions.getTableHeader().
                            setFont(CoeusFontFactory.getLabelFont());
        tblOtherActions.getTableHeader().setReorderingAllowed(false);
        tblOtherActions.getTableHeader().setResizingAllowed(true);
        tblOtherActions.setSelectionMode(
                                DefaultListSelectionModel.SINGLE_SELECTION);
        /* This logic is used to select the first row in the list of available 
           rows in JTable*/
        if( tblOtherActions.getRowCount() > ZERO_COUNT ){
           tblOtherActions.addRowSelectionInterval( ZERO_COUNT, ZERO_COUNT );
        }
        tblOtherActions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblOtherActions.getColumnModel().getColumn(0);
        //column.setMinWidth(25);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        
        column = tblOtherActions.getColumnModel().getColumn(1);
        column.setMinWidth(150);
        //column.setMaxWidth(190);
        //column.setPreferredWidth(190);
        JComboBox  coeusCombo = new JComboBox();
        coeusCombo.setFont(CoeusFontFactory.getNormalFont());
        coeusCombo.setModel(new CoeusComboBox(
                                getAvailableActionTypes(),
                                false).getModel()); 
        column.setCellEditor(new DefaultCellEditor(coeusCombo ){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().
                        toString();
            }
            public int getClickCountToStart(){
                return 1;
            }
        });
        /* This block of code contains logic for clearing the contents in 
           the actions if the action type is changed in the 
           JComboBox*/
       
        
        
            
        //comeHere
       coeusCombo.addItemListener( new ItemListener(){
            public void itemStateChanged( ItemEvent item ){
                int sRow=tblOtherActions.getEditingRow();
                if(sRow != -1){
                    ((DefaultTableModel)tblOtherActions.getModel()).
                        setValueAt("",sRow,2);
                        saveRequired=true;
                }
              
            }
             
       });
      
        
        DescriptionEditor editDescription = 
                                    new DescriptionEditor(2000);
        column = tblOtherActions.getColumnModel().getColumn(2);
        column.setCellEditor(editDescription);
        column.setMinWidth(450);
        //column.setMaxWidth(190);
        //column.setPreferredWidth(190);

        column = tblOtherActions.getColumnModel().getColumn(3);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
    }
    
     /*
        This method is used to Enable or Disable the Buttons
        depending on the function Type. If the functionType is Display 
        then it disables the Add, Delete, Find JButtons.
    */
    private void formatFields(){
        boolean enabled = functionType !='D' ? true : false;
        btnAdd.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        if( functionType=='D' )
        {
            tblOtherActions.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
            tblOtherActions.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
            tblOtherActions.setSelectionForeground(Color.black);
        }
    }
    
    /* Method to set the data in the JTable.
       This method sets the data which is available in otherActionsData 
       Vector into JTable. */
    private void setFormData(){
        
        Vector vcDataPopulate = new Vector();
        Vector vcData=null;
        OtherActionInfoBean otherActionsInfoBean = null;
        
        if((otherActionsData!= null) &&
            (otherActionsData.size()>0)){
                int[] itemNumbersArray = new int[otherActionsData.size()];
                for(int inCtrdata=0;
                inCtrdata < otherActionsData.size();
                inCtrdata++){
                    
                    otherActionsInfoBean=(OtherActionInfoBean)
                            otherActionsData.get(inCtrdata);
                    /*int typeCode =
                        otherActionsInfoBean.getScheduleActTypeCode();*/
                    String typeDesc =
                        otherActionsInfoBean.getScheduleActTypeDesc();
                    String itemDesc = 
                        otherActionsInfoBean.getItemDescription();
                    int itemNumber = 
                        otherActionsInfoBean.getActionItemNumber();
                    itemNumbersArray[inCtrdata] = itemNumber;

                    vcData= new Vector();
                    vcData.addElement("");// for icon
                    vcData.addElement(typeDesc == null ? "" : typeDesc );
                    vcData.addElement(itemDesc == null ? "" : itemDesc);
                    vcData.addElement(new Integer(itemNumber));
                    
                    vcDataPopulate.addElement(vcData); 
                }
                    ((DefaultTableModel)tblOtherActions.getModel()).
                            setDataVector(vcDataPopulate,getColumnNames());
                    ((DefaultTableModel)tblOtherActions.getModel()).
                            fireTableDataChanged();
                    if( tblOtherActions.getRowCount() > ZERO_COUNT ){
                        tblOtherActions.getSelectionModel().
                            setSelectionInterval(1, 
                                tblOtherActions.getColumnCount() );
                        tblOtherActions.setRowSelectionInterval(ZERO_COUNT, ZERO_COUNT);
                    }else{
                        btnDelete.setEnabled(false);
                    }
                    for(int ind=0 ; ind < itemNumbersArray.length; ind++){
                        if(itemNumbersArray[ind] >= maxItemNumber){
                            maxItemNumber=itemNumbersArray[ind];
                        }
                    }
            }
    }
    
    /* Method to get all the column names of JTable*/
    private Vector getColumnNames(){
        Enumeration enumColNames =tblOtherActions.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
   
    /* Method to get all the table data from JTable
       @return Vector, a Vector which consists of OtherActionInfoBean's */
     private Vector getTableValues(){
         Vector keyValues = new Vector();
         if(tblOtherActions.getRowCount() > 0){
            if(tblOtherActions.isEditing()){
                int selRow = tblOtherActions.getSelectedRow();
                tblOtherActions.getCellEditor().stopCellEditing();
            }
            int maxItemNumberInSendData=0;
            int rowCount = tblOtherActions.getRowCount();
            OtherActionInfoBean otBean;
            int itemNumber=0;
            int inSend_MaxItemNumber[] = new int[tblOtherActions.getRowCount()];
            for(int inInd=0; inInd<rowCount ;inInd++){
                
                if((tblOtherActions.getValueAt(inInd,2)!= null)
                      && (tblOtherActions.getValueAt(inInd,
                      2).toString().trim().length()>0)){

                        otBean= new OtherActionInfoBean();
                        String stItemTypeDesc=(String)((DefaultTableModel)
                            tblOtherActions.getModel()).getValueAt(inInd,1);
                        
                        String stTypeCode = getIDForName(stItemTypeDesc);
                        int typeCode = Integer.parseInt(stTypeCode);

                        String stItemDesc = (String)((DefaultTableModel)
                            tblOtherActions.getModel()).getValueAt(inInd,2);
                        
                        itemNumber=((Integer)((DefaultTableModel)
                            tblOtherActions.getModel()).getValueAt(inInd,3)).
                                                                intValue();
                        if(itemNumber != 0){
                            otBean.setActionItemNumber(itemNumber);
                        }
                        inSend_MaxItemNumber[inInd] = itemNumber;

                        otBean.setScheduleActTypeDesc(stItemTypeDesc);
                        otBean.setItemDescription(stItemDesc);
                        otBean.setScheduleActTypeCode(typeCode);

                        otBean.setActionItemNumber(itemNumber);
                        otBean.setScheduleId(scheduleID);

                        keyValues.addElement(otBean);
        }else{

             }
            }//END FOR
            for(int ind=0 ; ind < inSend_MaxItemNumber.length; ind++){
                if(inSend_MaxItemNumber[ind] >= maxItemNumberInSendData){
                    maxItemNumberInSendData=inSend_MaxItemNumber[ind];
                }
            }
            maxItemNumber = maxItemNumberInSendData;
        }//END ROW COUNT
        return keyValues;
     }
    
    /* Helper method which gives vector of ActionTypeDescriptions */
    
    private Vector getAvailableActionTypes() {
        
        ComboBoxBean comboEntry = null;
        Vector actionType = new Vector();
        if( availableTypes == null ){
            return actionType;
        }
        int availableTypesSize = availableTypes.size();
        for( int indx = 0; indx < availableTypesSize; indx++ ){
            
            comboEntry = ( ComboBoxBean ) availableTypes.get( indx );
            actionType.addElement( comboEntry.getDescription() );
        }
        return actionType;
    }
    
    /* Helper method which gives available type Code value for the 
     * available type description selected in JComboBox
     */
    private String getIDForName( String selType ){
        
        String stTypeID = "1";
        ComboBoxBean comboEntry = null;
        if( availableTypes == null || selType == null ){
            return stTypeID;
        }
        for( int indx = 0; indx < availableTypes.size(); indx++ ){
            
            comboEntry = ( ComboBoxBean ) availableTypes.get( indx );
            if( ((String)comboEntry.getDescription()).equalsIgnoreCase(
                                                      selType)  ){
                stTypeID = comboEntry.getCode();
                break;
            }
        }        
        return stTypeID;
    }

    
    /**
     * This method is used to check whether the given action entry number has
     * any minute entry.
     * 
     * @param actionEntryNum String representing Action Entry Number.
     *
     * @return boolean true if the given action item is not used anywhere else false.
     *
     * @throws Exception with custom message specifying where the given action
     * item has been used.
     */
    private boolean canDelete(String actionEntryNum) throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL 
                + "/scheduleMaintSrvlt";
        // connect to the database 
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType('C');
        request.setId(scheduleID);
        request.setDataObject(actionEntryNum);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                // action entry can be deleted. so return true.
                return true;
            }else{
                /* action entry has been used somewhere so throw exception with
                  the message sent by response which specifies where it has been
                  used */
                throw new Exception(response.getMessage());
            }
        }else{
            throw new Exception(coeusMessageResources.parseMessageKey(
                "server_exceptionCode.1000"));
        }
    }
    
    
     /**
     * Inner Class used to provide textField as cell editor. 
     * It extends DefaulCellEditor and implements TableCellEditor interface.
     * This class overides getTableCellEditorComponent method which returns the 
     * editor component to the JTable Column.
     */
    class DescriptionEditor extends DefaultCellEditor 
                                                implements TableCellEditor {
        private JTextField txtDesc;
        int selectRow=0;
        String actualActionDescription = null;
        
        /**
         * Constructor for DescriptionEditor 
         * @len length of the editor field.
         */
        DescriptionEditor(int len ){
            super(new JTextField());
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            
            txtDesc.addFocusListener(new FocusAdapter(){
                 public void focusLost(FocusEvent fe){
                    if (!fe.isTemporary()){
                        setDescriptionInfo();
                    }
                 }
            });
            txtDesc.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent actionEvent){
                    setDescriptionInfo();
                }
            });
        }//End Constructor

        /** The Overridden method of TableCellRenderer which is
         * called for every cell when a component
         * is going to be rendered in its cell.
         * @param table is a JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row is a selected row index
         * @param column is a selected column index
         * @return a Component used for rendering the cell
         */

        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){
            saveRequired=true;
            selectRow = row;
            actualActionDescription = 
                                    (String)tblOtherActions.getValueAt(row,2);
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            return txtDesc;
        }
       
        /** Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped, false otherwise
         */
        public boolean stopCellEditing() {
            setDescriptionInfo();
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return a Object contained in the editor component
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtDesc).getText();
        }

        
        /** This method returns the mouse click counts after which the editor
         * should be invoked
         * @return a <CODE>int</CODE> value.
         */
        public int getClickCountToStart(){
            return 2;     
        }
        
        // Helper method to set the Description in the textfield editor
        private void setDescriptionInfo(){

            String stActionDescription=txtDesc.getText().trim();
            if(!(stActionDescription ==null && 
                stActionDescription.
                equalsIgnoreCase(actualActionDescription))){
                tblOtherActions.getModel().
                setValueAt(stActionDescription,selectRow,2);

                tblOtherActions.getSelectionModel().
                    setLeadSelectionIndex(selectRow);
                if(tblOtherActions.getCellEditor() != null){
                    tblOtherActions.getCellEditor().cancelCellEditing();
                }
                saveRequired=true;
            }
        }
 }// End DescriptionEditor InnerClass
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnOtherActions = new javax.swing.JScrollPane();
        tblOtherActions = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(750, 420));
        setPreferredSize(new java.awt.Dimension(750, 420));
        scrPnOtherActions.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Actions", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        scrPnOtherActions.setMinimumSize(new java.awt.Dimension(650, 405));
        scrPnOtherActions.setPreferredSize(new java.awt.Dimension(650, 405));
        tblOtherActions.setFont(CoeusFontFactory.getNormalFont());
        tblOtherActions.setModel(new DefaultTableModel(new String[][]{},
            new String [] {"Icon", "Type", "Description", "ActionNumber"}
        ){
            public boolean isCellEditable(int row, int col){
                /* in display mode editing of  table fields will be disabled */
                if((functionType == 'D') || (col==3) ){
                    return false;
                }else{
                    return true;
                }
            }
        }

    );
    tblOtherActions.setPreferredScrollableViewportSize(new java.awt.Dimension(650, 150));
    tblOtherActions.setRowHeight(20);
    tblOtherActions.setSelectionBackground(new java.awt.Color(255, 255, 255));
    tblOtherActions.setSelectionForeground(java.awt.Color.black);
    tblOtherActions.setShowHorizontalLines(false);
    tblOtherActions.setShowVerticalLines(false);
    tblOtherActions.setOpaque(false);
    scrPnOtherActions.setViewportView(tblOtherActions);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 9;
    gridBagConstraints.gridheight = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
    add(scrPnOtherActions, gridBagConstraints);

    btnAdd.setFont(CoeusFontFactory.getLabelFont());
    btnAdd.setMnemonic('A');
    btnAdd.setText("Add");
    btnAdd.setName("btnAdd");
    btnAdd.setPreferredSize(new java.awt.Dimension(81, 27));
    btnAdd.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAdd_actionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 9;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 3);
    add(btnAdd, gridBagConstraints);

    btnDelete.setFont(CoeusFontFactory.getLabelFont());
    btnDelete.setMnemonic('D');
    btnDelete.setText("Delete");
    btnDelete.setMaximumSize(new java.awt.Dimension(27, 21));
    btnDelete.setMinimumSize(new java.awt.Dimension(27, 21));
    btnDelete.setName("btnAdd");
    btnDelete.setPreferredSize(new java.awt.Dimension(81, 27));
    btnDelete.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnDelete_actionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 9;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 3);
    add(btnDelete, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void btnDelete_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_actionPerformed
        // Add your handling code here:
        int totalRows = tblOtherActions.getRowCount();
        /* If there are more than one row in table then delete it */
        if (totalRows > 0) {
            /* get the selected row */
            int selectedRow = tblOtherActions.getSelectedRow();
            if (selectedRow != -1) {
                int selectedOption = CoeusOptionPane.
                 showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                                "schdActFrm_delConfirmCode.1153"),
                    CoeusOptionPane.OPTION_YES_NO, 
                    CoeusOptionPane.DEFAULT_YES);
                // if Yes then selectedOption is 0
                // if No then selectedOption is 1
                if (0 == selectedOption) {
                    try{
                         
                        String actionEntryNum 
                            = (tblOtherActions.getValueAt(selectedRow,3)).toString();                                                 
                        if(canDelete(actionEntryNum)){
                            ((DefaultTableModel)
                            tblOtherActions.getModel()).removeRow(selectedRow);

                            saveRequired = true;
                            tblOtherActions.clearSelection();

                            // find out again row count in table
                            int newRowCount = tblOtherActions.getRowCount();

                            if(newRowCount == 0){
                               btnAdd.requestFocusInWindow(); 
                               btnDelete.setEnabled(false);
                            }else if (newRowCount > selectedRow) {
                                (tblOtherActions.getSelectionModel())
                                    .setSelectionInterval(selectedRow,
                                        selectedRow);
                            } else {
                                tblOtherActions.setRowSelectionInterval( 
                                    newRowCount - 1, newRowCount -1 ); 
                                tblOtherActions.scrollRectToVisible( 
                                tblOtherActions.getCellRect(
                                                newRowCount - 1 ,
                                                ZERO_COUNT, true));
                            }
                        }
                    }catch (Exception e){
                        CoeusOptionPane.showInfoDialog(e.getMessage());
                    }
                }

            } // if total rows >0 and row is selected
            else{
                // if total rows >0 and row is not selected
                CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                "schdActFrm_exceptionCode.1151"));
           }//End Else block
        }
        
    }//GEN-LAST:event_btnDelete_actionPerformed

    private void btnAdd_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd_actionPerformed
        // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - Start
        if(availableTypes == null || availableTypes.size() < 1){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                    ERR_NO_ACTION_TYPE_EXIST));
            return;
        }
	// Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - Start
        // Add your handling code here:
        Vector newRowData = new Vector();
        newRowData.addElement( "" );// Icon data
        if(availableTypes != null && availableTypes.size() > 0){
            newRowData.addElement(((ComboBoxBean)availableTypes.get(0 )).
                getDescription());
        }// Type data setting
        //COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release-Start
        else{
        newRowData.addElement("");
        }
        //COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release-End
        newRowData.addElement("");//Item description data setting
        maxItemNumber++;
        newRowData.addElement(new Integer(maxItemNumber));//item number setting
        
        ((DefaultTableModel)tblOtherActions.getModel()).addRow( newRowData );
        ((DefaultTableModel)tblOtherActions.getModel()).fireTableDataChanged();
        int lastRow = tblOtherActions.getRowCount() - 1;
        if(lastRow >= 0){
            btnDelete.setEnabled(true);
            tblOtherActions.setRowSelectionInterval( lastRow, lastRow ); 
            tblOtherActions.scrollRectToVisible(tblOtherActions.getCellRect(
                    lastRow ,ZERO_COUNT, true));
            tblOtherActions.editCellAt(lastRow,2);
            tblOtherActions.getEditorComponent().requestFocusInWindow();
        }
            saveRequired=true;
    }//GEN-LAST:event_btnAdd_actionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblOtherActions;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JScrollPane scrPnOtherActions;
    // End of variables declaration//GEN-END:variables
}
