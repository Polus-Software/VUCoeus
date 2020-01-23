/*
 * @(#)ProposalKeyStudyPersonnelAdminForm.java  1.0  April 03, 2003, 10:06 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.bean.KeyPersonBean;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.exception.*;

/** <CODE>ProposalKeyStudyPersonnelAdminForm</CODE> is a form object which display
 * all the key person for the selected proposal and can be used to
 * <CODE>add/modify/display</CODE> the key personl details.
 * This class is instantiated in <CODE>ProposalDetailForm</CODE>.
 *
 * @author  Raghunath P.V.
 * @version: 1.0 Created on April 03, 2003, 10:06 AM
 */
public class ProposalKeyStudyPersonnelAdminForm extends javax.swing.JComponent
                    implements TypeConstants, ActionListener{

    /* Data Beans to hold Key Study Person Details */
    private ProposalKeyPersonFormBean proposalDataBean;
    private ProposalKeyPersonFormBean proposalNewDataBean;
    private ProposalKeyPersonFormBean proposalOldDataBean;
   // private KeyPersonUnitTableModel keyPersonUnitTableModel;
   

    /* Character variable to hold the function type which may be add,delete,
     modify */
    private char functionType;
    /* Vector to hold all the Data Beans */
    /* Stores the proposal id */
    private String proposalId;
    private Vector proposalKeyStudyPersonnelData;
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;
    /* Specifies the proposal information is modified or not */
    private boolean proposalInfoModified = false;
    /* boolean variable to check the condition whether it is found or not */
    private boolean found;
    /* Url for the servlet */
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    /* used in searching for rolodex */
    private static final String ROLODEX_SEARCH = "rolodexSearch";
    /* used to specify to search in person table */
    private static final String PERSON_SEARCH = "personSearch";
    /* used to display title in the search window */
    private static final String DISPLAY_TITLE = "DISPLAY ROLODEX";
    /** This is used to verify the vaues which come from
        the database are null or not
     */
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //holds the zero count value
    private static final int ZERO_COUNT = 0;

    private Vector persons = new Vector();
    private CoeusVector cvUnit;
    private ProposalDetailForm detailForm;
    
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    private boolean parentProposal;
    private static final int UNIT_HAND_COLUMN = 0;
    private static final int UNIT_LEAD_FLAG_COLUMN = 3;
    private static final int UNIT_NUMBER_COLUMN = 1;
    private static final int UNIT_NAME_COLUMN = 2;
    private static final int UNIT_OSP_ADMIN_COLUMN = 4;
    /** Creates new form <CODE>ProposalKeyStudyPersonnelAdminForm</CODE>. <p>
     * <I> Default Constructor.</I>
     */

    public ProposalKeyStudyPersonnelAdminForm() {
        //initComponents();
    }

    /** Constructor that instantiate ProposalKeyStudyPersonnelAdminForm and populate the component with specified data.
     * And sets the enabled status for all components depending on the functionType.
     * @param keyStudyPersonnelData a Vector which consists of all the KeyStudyPersonnel details
     *
     * @param functionType is a Char variable which specifies the mode in which the
     * form will be displayed.
     * <B>'A'</B> specifies that the form is in Add Mode
     * <B>'M'</B> specifies that the form is in Modify Mode
     * <B>'D'</B> specifies that the form is in Display Mode
     */

    public ProposalKeyStudyPersonnelAdminForm(char functionType,
        java.util.Vector keyStudyPersonnelData, String propId) {
            proposalKeyStudyPersonnelData = new Vector();
            this.proposalKeyStudyPersonnelData = keyStudyPersonnelData;
            this.functionType = functionType;
            this.proposalId = propId;
    }

    /** This Method is used to get the functionType
     * @return a fuctionType like 'A','D','M'.
     */

    public char getFunctionType(){
        return functionType;
    }

    /** This Method is used to set the functionType
     * @param fType is a Char data like 'A','D','M'.
     */

    public void setFunctionType(char fType){
        this.functionType = fType;
    }

    /** This Method to set the Form data.
     * @param KeyStudyPersonnelData is a Vector KeyStudyPersonnelBeans
     */

    public void setKeyStudyPersonnelData(Vector keyStudyPersonnelData){
        
        int selectedRow=0;
        if(tblKeyStPer.getRowCount()>0)
        {
            selectedRow=tblKeyStPer.getSelectedRow();
        }
        this.proposalKeyStudyPersonnelData = keyStudyPersonnelData;
        if(proposalKeyStudyPersonnelData != null){
            setFormData();
//            if(tblKeyStPer.getRowCount()>0){
//                tblKeyStPer.setRowSelectionInterval(selectedRow, selectedRow);
//            }
        }else{
            ((DefaultTableModel)tblKeyStPer.getModel()).setDataVector(
                new Object[][]{},getColumnNames().toArray());
        }
        setTableEditors();
        if(tblKeyStPer.getRowCount()>0 && selectedRow != -1 ){
            tblKeyStPer.setRowSelectionInterval(selectedRow, selectedRow);
        }
        // Handle
        
    }

     /** This method is used to initialize the components, set the data in the components.
      * This method is invoked in the <CODE>ProposalDetailForm</CODE>.
      * @param mdiForm is a reference of CoeusAppletMDIForm
      * @return a JPanel containing all the components with the data populated.
      */

    public JComponent showProposalKeyPersonForm(CoeusAppletMDIForm
    mdiForm){

        this.mdiReference = mdiForm;
        initComponents();
        setListenersForButtons();
        // This method enable or disable the JButtons depending on the functionType
        formatFields();
        setFormData();
        setTableEditors();
        /* This logic is used to select the first row in the list of available
           rows in JTable*/
        if( tblKeyStPer!=null && tblKeyStPer.getRowCount() > ZERO_COUNT ){
            tblKeyStPer.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
        }else{
            btnDelete.setEnabled(false);
        }
        // setting bold property for table header values
        tblKeyStPer.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        coeusMessageResources = CoeusMessageResources.getInstance();
        Component[] comp = {tblKeyStPer,btnAdd,btnDelete,btnFindPerson,btnFindRolodex};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        
        return this;
    }

    // This method adds listeners to the all the buttons.

    private void setListenersForButtons(){

        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnFindPerson.addActionListener(this);
        btnFindRolodex.addActionListener(this);
        tblKeyStPer.addMouseListener( new PersonDisplayAdapter() );
    }

    /** Method to set the data in the JTable.
     * This method sets the data which is available in proposalKeyStudyPersonnelData
     * Vector to JTable.
     */

    public void setFormData(){

        Vector vcDataPopulate = new Vector();
        Vector vcData = null;
        if((proposalKeyStudyPersonnelData != null) &&
                        (proposalKeyStudyPersonnelData.size()>0)){

            if( persons == null ) {
                persons = new Vector();
            }

            int keyPersonnelSize = proposalKeyStudyPersonnelData.size();
            for(int inCtrdata=0;inCtrdata < keyPersonnelSize;inCtrdata++){

                proposalDataBean=(ProposalKeyPersonFormBean)
                                      proposalKeyStudyPersonnelData.get(inCtrdata);

                String personname = proposalDataBean.getPersonName();
                String role = proposalDataBean.getProjectRole();
                float effort = proposalDataBean.getPercentageEffort();
                boolean faculty = proposalDataBean.isFacultyFlag();
                String slno = proposalDataBean.getPersonId();
                boolean employee = proposalDataBean.isNonMITPersonFlag();

                persons.addElement( slno );

                vcData= new Vector();
                vcData.addElement("");
                vcData.addElement(personname);
                vcData.addElement(role == null ? "" : role);
                if(effort == 0.0){
                    vcData.addElement(".00");
                }else{
                    vcData.addElement(new Float(effort));
                }
                vcData.addElement(new Boolean(faculty));
                vcData.addElement(slno);
                vcData.addElement(new Boolean(employee));

                vcDataPopulate.addElement(vcData);
            }
                ((DefaultTableModel)tblKeyStPer.getModel()).setDataVector(
                                            vcDataPopulate,getColumnNames());
        }
    }

    /**
     * Method used to set the reference of the ProposalDetailForm to this screen.
     *
     * @param detForm reference to the ProposalDetailForm
     */
    public void setDetailForm( ProposalDetailForm detForm) {
        this.detailForm = detForm;
    }

    /* Method to get all the table data from JTable
       @return Vector, a Vector which consists of ProposalKeyPersonFormBean's */

    private Vector getTableValues(){

        Vector keyValues = new Vector();
        int rowCount = tblKeyStPer.getRowCount();
        ProposalKeyPersonFormBean pkBean;

        for(int inInd=0; inInd < rowCount ;inInd++){

            String pId=(String)((DefaultTableModel)tblKeyStPer.getModel()).
                                                            getValueAt(inInd,5);
            if(pId != null && pId.trim().length()>0){

                String pName = (String)((DefaultTableModel)
                                    tblKeyStPer.getModel()).getValueAt(inInd,1);
                String pRole=(String)((DefaultTableModel)
                                    tblKeyStPer.getModel()).getValueAt(inInd,2);
                float effortPercentage = tblKeyStPer.getValueAt(inInd,3) == null ? 0 :
                                            new Float(tblKeyStPer.getValueAt(inInd,3).toString().trim()).floatValue();
                boolean flag = ((Boolean)((DefaultTableModel)
                                    tblKeyStPer.getModel()).
                                            getValueAt(inInd,4)).booleanValue();
                boolean empFlag = ((Boolean)((DefaultTableModel)
                                    tblKeyStPer.getModel()).
                                            getValueAt(inInd,6)).booleanValue();
                pkBean= new ProposalKeyPersonFormBean();

                pkBean.setPersonName(pName);
                pkBean.setProjectRole(pRole);
                pkBean.setFacultyFlag(flag);
                pkBean.setNonMITPersonFlag(empFlag);
                pkBean.setPercentageEffort(effortPercentage);
                pkBean.setPersonId(pId);
                pkBean.setProposalNumber(proposalId);

                keyValues.addElement(pkBean);

            }else{
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                        "protoKeyStPsnlFrm_exceptionCode.1067"));
                tblKeyStPer.requestFocus();
            }
        }
        return keyValues;
    }

    /** Method to get the Key personnel data in a Vector
     * which consists of ProposalKeyPersonFormBeans.
     * It sets the AcType as 'U' to the bean if any bean data is modified.
     * It sets the AcType to 'i' if any data is inserted into JTable by the user.
     * It sets the AcType to 'D' if any data is deleted from JTable by the user.
     * @return Vector of bean data.
     */

    public java.util.Vector getKeyStudyPersonnelData(){
        /* This block of code is used to set AcType as D to all the beans
           if all the rows are deleted in the JTable and it sets the
           proposalInfoModified flag to true stating that the save is required
           for the user */
        if((proposalKeyStudyPersonnelData != null) &&
           (proposalKeyStudyPersonnelData.size()>0) &&
           (tblKeyStPer.getRowCount()<=0)){

            int noOfKeyPersons = proposalKeyStudyPersonnelData.size();

            for(int oldIndex = 0;oldIndex < noOfKeyPersons;oldIndex++){

                proposalOldDataBean = (ProposalKeyPersonFormBean)
                proposalKeyStudyPersonnelData.elementAt(oldIndex);
                proposalOldDataBean.setAcType(DELETE_RECORD);
                setSaveRequired(true);
                if(functionType == MODIFY_MODE){
                    proposalInfoModified = true;
                }
                proposalKeyStudyPersonnelData.
                    setElementAt(proposalOldDataBean,oldIndex);
            }
            return proposalKeyStudyPersonnelData;
        }
        /* This gets all the data from the JTable*/
        Vector newData = getTableValues();
        if((newData != null) && (newData.size() > 0)){
            int dataSize = newData.size();
            for(int newLocIndex = 0; newLocIndex < dataSize;newLocIndex++){
                int foundIndex = -1;
                found = false;
                proposalNewDataBean = (ProposalKeyPersonFormBean)newData.
                                                        elementAt(newLocIndex);
                if(proposalKeyStudyPersonnelData != null &&
                    proposalKeyStudyPersonnelData.size() > 0){
                      int noOfKeyPersons = proposalKeyStudyPersonnelData.size();
                    for(int oldLocIndex = 0;oldLocIndex < noOfKeyPersons;
                     oldLocIndex++){
                        proposalOldDataBean = (ProposalKeyPersonFormBean)
                        proposalKeyStudyPersonnelData.elementAt(oldLocIndex);
//                        proposalOldDataBean.addPropertyChangeListener(
//
//                        new PropertyChangeListener(){
//                            public void propertyChange(
//                            PropertyChangeEvent pce){
//                                proposalOldDataBean.setAcType(UPDATE_RECORD);
//                                setSaveRequired(true);
//                                if(functionType == MODIFY_MODE){
//                                    proposalInfoModified = true;
//                                }
//                            }
//                        });
                        if(proposalNewDataBean.getPersonName().equals(
                                    proposalOldDataBean.getPersonName())){

                            found = true;
                            foundIndex = oldLocIndex;
                            break;
                        }
                    }
                }else{
                    proposalKeyStudyPersonnelData = new Vector();
                }
                if(!found){

                    //if location is new set AcType to INSERT_RECORD
                    proposalNewDataBean.setAcType(INSERT_RECORD);
                    setSaveRequired(true);
                    if(functionType == MODIFY_MODE){
                        proposalInfoModified = true;
                    }
                    proposalKeyStudyPersonnelData.addElement(proposalNewDataBean);
                }else{
                        /* if present set the values to the bean. if modified,
                           bean will fire property change event */
                    if(proposalOldDataBean != null){

                        proposalOldDataBean.setPersonId(
                                        proposalNewDataBean.getPersonId());
                        proposalOldDataBean.setPersonName(
                                        proposalNewDataBean.getPersonName());
                        proposalOldDataBean.setProjectRole(
                                        proposalNewDataBean.getProjectRole());
                        proposalOldDataBean.setFacultyFlag(
                                        proposalNewDataBean.isFacultyFlag());
                        proposalOldDataBean.setNonMITPersonFlag(
                                        proposalNewDataBean.isNonMITPersonFlag());
                        proposalOldDataBean.setPercentageEffort(
                                        proposalNewDataBean.getPercentageEffort());

                        if(foundIndex != -1){
                            proposalKeyStudyPersonnelData.setElementAt(
                                               proposalOldDataBean,foundIndex);
                        }
                    }
                }
            }
            if(proposalKeyStudyPersonnelData != null
                && proposalKeyStudyPersonnelData.size() > 0){

                int noOfKeyPersons = proposalKeyStudyPersonnelData.size();
                for(int oldLocIndex = 0; oldLocIndex <noOfKeyPersons;
                        oldLocIndex++){

                    found = false;
                    proposalOldDataBean = (ProposalKeyPersonFormBean)
                    proposalKeyStudyPersonnelData.elementAt(oldLocIndex);
                    int newDataSize = newData.size();
                    for(int newLocIndex = 0; newLocIndex < newDataSize;
                    newLocIndex++){
                        proposalNewDataBean = (ProposalKeyPersonFormBean)newData.
                                                        elementAt(newLocIndex);
                        if(proposalOldDataBean.getPersonName().equals(
                                        proposalNewDataBean.getPersonName())){

                            found = true;
                            break;
                        }
                    }
                    if(!found){

                        proposalOldDataBean.setAcType(DELETE_RECORD);
                        setSaveRequired(true);
                        if(functionType == MODIFY_MODE){
                            proposalInfoModified = true;
                        }
                        proposalKeyStudyPersonnelData.setElementAt(
                                            proposalOldDataBean,oldLocIndex);
                    }
                }
            }
        }
        //printBean(proposalKeyStudyPersonnelData);
        return proposalKeyStudyPersonnelData;
    }
    // Test Bed method
    private void printBean(Vector keyData){
        ProposalKeyPersonFormBean pBean = null;
        if(keyData != null){
            for(int index = 0; index < keyData.size(); index++){
                pBean = (ProposalKeyPersonFormBean)keyData.elementAt(index);
                if (pBean != null){

                    String personname = pBean.getPersonName();
                    String role = pBean.getProjectRole();
                    float effort = pBean.getPercentageEffort();
                    boolean faculty = pBean.isFacultyFlag();
                    String slno = pBean.getPersonId();
                    boolean employee = pBean.isNonMITPersonFlag();
                    String AcType = pBean.getAcType();

                }
            }
        }
    }

    /**
     * This method is used to get the Column Names of Key study personal
     * table data.
     * @return Vector collection of column names of key study personnel table.
     */

    private Vector getColumnNames(){
        Enumeration enumColNames = tblKeyStPer.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    /*
        This method is used to Enable or Disable the Buttons
        depending on the function Type. If the functionType is Display
        then it sets the Add, Delete, FindPerson, FindRolodex JButtons to disable.
    */

    private void formatFields(){
        
        if(isParentProposal()){
            functionType= DISPLAY_MODE;
        }
        boolean enabled = functionType != DISPLAY_MODE ? true : false;
        btnAdd.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        btnFindPerson.setEnabled(enabled);
        btnFindRolodex.setEnabled(enabled);
        
         //Added by Amit 11/19/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblKeyStPer.setBackground(bgListColor);    
            tblKeyStPer.setSelectionBackground(bgListColor );
            tblKeyStPer.setSelectionForeground(java.awt.Color.BLACK);            
        }
        else{
            tblKeyStPer.setBackground(Color.white);            
            tblKeyStPer.setSelectionBackground(Color.white);
            tblKeyStPer.setSelectionForeground(java.awt.Color.black);                        
        }
        //end Amit           
        
//        if(functionType!= DISPLAY_MODE && isParentProposal()){
//            btnAdd.setEnabled(false);
//            btnDelete.setEnabled(false);
//            btnFindPerson.setEnabled(false);
//            btnFindRolodex.setEnabled(false);
//            functionType = DISPLAY_MODE;
//        }
    }

    /** This method is used for validations.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception a exception to be thrown in the client side.
     */

    public boolean validateFormData() throws Exception,CoeusUIException{
        boolean valid=true;
        if(tblKeyStPer.getRowCount() <= 0){
            //Validation not required
        }else{
            int rowCount = tblKeyStPer.getRowCount();
            for(int inInd=0; inInd < rowCount ;inInd++){
                String stPersonId=(String)((DefaultTableModel)
                                            tblKeyStPer.getModel()).
                                                    getValueAt(inInd,5);
                String stRole=(String)((DefaultTableModel)
                                            tblKeyStPer.getModel()).
                                                    getValueAt(inInd,2);
                if((stPersonId == null) || (stPersonId.trim().length() <= 0)){
                    valid=false;
                    break;
                }
                if((stRole == null) || (stRole.trim().length() <= 0)){
                    // added by chandra 06/02/04 -  start
                tblKeyStPer.setRowSelectionInterval(inInd,inInd);
                tblKeyStPer.scrollRectToVisible(tblKeyStPer.getCellRect(inInd ,0, true));
                // added by chandra 06/02/04 - End
                    errorMessage(coeusMessageResources.parseMessageKey(
                            "proposal_KeyStPsnlFrm_exceptionCode.7101"));
                    tblKeyStPer.requestFocus();
                    return false;
                    //break;
                }
            }
            if(!valid){
                // added by chandra 06/02/04 -  start
                tblKeyStPer.setRowSelectionInterval(rowCount-1,rowCount-1);
                tblKeyStPer.scrollRectToVisible(tblKeyStPer.getCellRect(rowCount-1 ,0, true));
                // added by chandra 06/02/04 - End
                errorMessage(coeusMessageResources.parseMessageKey(
                        "protoKeyStPsnlFrm_exceptionCode.1140"));
                tblKeyStPer.requestFocus();
                return false;
            }
        }
        return true;
    }

    /* This method is used to set the cell editors to the columns,
       set the column width to each individual column, disable the column
       resizable feature to the JTable, setting single selection mode to the
       JTable */

    private void setTableEditors(){

//        ColumnValueRenderer columnValueRenderer = new ColumnValueRenderer();

        TableColumn column = tblKeyStPer.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());

        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);

        JTableHeader header = tblKeyStPer.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        tblKeyStPer.setRowHeight(24);
        tblKeyStPer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);    
        tblKeyStPer.setOpaque(false);
        tblKeyStPer.setShowVerticalLines(false);
        tblKeyStPer.setShowHorizontalLines(false);
        tblKeyStPer.setFont(CoeusFontFactory.getNormalFont());
        tblKeyStPer.setSelectionMode(
                            DefaultListSelectionModel.SINGLE_SELECTION);

        column = tblKeyStPer.getColumnModel().getColumn(5);
        column.setMinWidth(0);
        //column.setMaxWidth(0);
        column.setPreferredWidth(0);

        column = tblKeyStPer.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        //column.setMaxWidth(0);
        column.setPreferredWidth(0);

        tblKeyStPer.setRowHeight(20);

        NameEditor nmEdtName = new NameEditor( "Name", 60 );

        column = tblKeyStPer.getColumnModel().getColumn(1);
        column.setMinWidth(300);
        //column.setMaxWidth(245);
        column.setPreferredWidth(300);
        //column.setResizable(false);
        column.setCellEditor( nmEdtName );
//        column.setCellRenderer( columnValueRenderer );

        NameEditor nmEdtRole = new NameEditor( "Role", 60 );
        column = tblKeyStPer.getColumnModel().getColumn(2);
        
        //column.setMinWidth(200);
        //Added by chandra 06/02/2004 - start
        column.setMinWidth(180);
        column.setPreferredWidth(180);
        //Added by chandra 06/02/2004 - end
        //column.setPreferredWidth(200);
        //column.setResizable(false);
        
        column.setCellEditor( nmEdtRole );
//        column.setCellRenderer( columnValueRenderer );

        //CurrencyField effortField = new CurrencyField();
        column = tblKeyStPer.getColumnModel().getColumn(3);
        column.setPreferredWidth(65);
        //column.setMaxWidth(65);
        column.setMinWidth(65);

        TableCellEditor currencyCellEditor =
                            new CurrencyEditor("%Effort");

//        column.setCellRenderer( columnValueRenderer );
        column.setCellEditor(currencyCellEditor);

        /*column.setCellRenderer( columnValueRenderer );
        column.setCellEditor( new DefaultCellEditor( effortField ) {
            public Object getCellEditorValue(){
               return (((CurrencyField)getComponent()).getText());
            }
        });*/

       column = tblKeyStPer.getColumnModel().getColumn(4);
       column.setMinWidth(60);
       column.setMaxWidth(60);
       column.setPreferredWidth(60);
       column.setResizable(false);
    }

    /** This method is used to determine whether the data to be saved or not.
     * @return true if the modifications done, false otherwise.
     */

    public boolean isSaveRequired(){
        if( tblKeyStPer.isEditing() && tblKeyStPer.getCellEditor()!= null ){
            tblKeyStPer.getCellEditor().stopCellEditing();
        }
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
            if(tblKeyStPer.getRowCount() > 0 ) {
                tblKeyStPer.requestFocusInWindow();
                int rowNum = tblKeyStPer.getSelectedRow();
                if(rowNum > 0){
                    tblKeyStPer.setRowSelectionInterval(rowNum,rowNum);
                }
                tblKeyStPer.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }                
    }    
    //end Amit       

    /** This method is used to determine whether the proposal information is
     * modified.
     * @return true if the modifications done, false otherwise.
     */

    public boolean isProposalInfoModified(){
        return proposalInfoModified;
    }

    /** This method is used to set true or false to the saveRequired member variable
     * @param save is a boolean variable to be set to saveRequired variable.
     */

    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }

    /** This method is used to show the alert messages to the user.
      * @param mesg a string message to the user.
      * @throws Exception a exception thrown in the client side.
      */

    public static void errorMessage(String mesg) throws CoeusUIException {
        
        //raghuSV:  below code to set TabIndex
        //starts...
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(4);
        throw coeusUIException;
        //ends
        
        //throw new Exception(mesg);
        
    }

    /**
     *  Method used to validate whether the keyStudyPerson is duplicate or not
     */

    private boolean checkDuplicatePerson(String personId){

        boolean duplicate = false;
        String oldId = "";
        int keyStudyPersonnelSize = tblKeyStPer.getRowCount();
        for(int rowIndex = 0; rowIndex < keyStudyPersonnelSize;
            rowIndex++){
                oldId = (String)tblKeyStPer.getValueAt(rowIndex,5);
                if(oldId != null){
                    if(oldId.equals(personId)){
                        duplicate = true;
                        break;
                    }
                }
        }
        return duplicate;
    }

     /**
     *  Method used to validate whether the keyStudyPerson is duplicate or not
     */
    private boolean checkDuplicatePerson(String personId, int selectedRow){

        boolean duplicate = false;
        String oldId = "";
        int keyStudyPersonnelSize = tblKeyStPer.getRowCount();
        for(int rowIndex = 0; rowIndex < keyStudyPersonnelSize;
            rowIndex++){
            if(rowIndex != selectedRow){

                oldId = (String)tblKeyStPer.getValueAt(rowIndex,5);
                if(oldId != null){

                    if(oldId.equals(personId)){
                        duplicate = true;
                        break;
                    }
                }
            }//
        }
        return duplicate;
    }

        /**
     * This method is used to get the collection of PersonInfoFormBean.
     * @return Vector of Bean elements
     */

    private Vector getPersonInfo(String name){

        Vector vsearchData=null;
        RequesterBean requester = new RequesterBean();
        if(name!=null && name.trim().length() > 0){
        
            //Bug Fix: Validation on focus lost Start 1
            /*requester.setDataObject("GET_PERSONINFO");
            requester.setId(name);*/
            requester.setFunctionType('J');
            requester.setDataObject(name);
            //Bug Fix: Validation on focus lost End 1
        }
        
        //Bug Fix: Validation on focus lost Start 2
        /*String connectTo = connectionURL + "/coeusFunctionsServlet";*/
        String connectTo = connectionURL + "/unitServlet";
        //Bug Fix: Validation on focus lost End 2
        
        AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){

            vsearchData = new Vector();
            PersonInfoFormBean personInfoFormBean =
                                (PersonInfoFormBean) response.getDataObject();
            
            //Bug Fix: Validation on focus lost Start 3
            if(personInfoFormBean.getPersonID() == null){
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("investigator_exceptionCode.1007"));
                vsearchData = new Vector();
            }else if(personInfoFormBean.getPersonID().equalsIgnoreCase("TOO_MANY")){
                CoeusOptionPane.showErrorDialog
                    ("\""+name+"\""+" " +coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1055"));
                vsearchData = new Vector();
            }else{
                String stName = personInfoFormBean.getFullName();
                
                String stRole= personInfoFormBean.getDirTitle();
                String stId= personInfoFormBean.getPersonID();
                boolean flag = personInfoFormBean.getFacFlag() == null ? false
                    :(personInfoFormBean.getFacFlag().toString().
                        equalsIgnoreCase("y")? true :false);

                vsearchData.addElement("");
                vsearchData.addElement(stName);
                vsearchData.addElement(stRole);
                vsearchData.addElement("");//
                vsearchData.addElement(new Boolean(flag));
                vsearchData.addElement(stId);
                vsearchData.addElement(new Boolean(false));
            }
        }
        //Bug Fix: Validation on focus lost End 3
        return vsearchData;
    }

    public boolean isPersonPresent ( String personId ) {
        if( persons == null ) {
            persons = new Vector();
        }
        int size = persons.size();
        for( int indx = 0 ; indx < size ; indx++ ) {
            if( persons.elementAt( indx ).equals( personId ) ) {
                return true;
            }
        }
        return false;
    }

    //TextField Cell Renderer

    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {

        public ColumnValueRenderer() {
          setOpaque(true);
          setBorder(new EmptyBorder(0,0,0,0));
          setFont(CoeusFontFactory.getNormalFont());
        }

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {

              setText( (value ==null) ? "" : value.toString() );
              return this;
        }
     }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTableContainer = new javax.swing.JPanel();
        scrPnPane1 = new javax.swing.JScrollPane();
        tblKeyStPer = new javax.swing.JTable();
        pnlButtonsContainer = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFindPerson = new javax.swing.JButton();
        btnFindRolodex = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlTableContainer.setLayout(new java.awt.BorderLayout());

        pnlTableContainer.setMaximumSize(new java.awt.Dimension(660, 360));
        pnlTableContainer.setMinimumSize(new java.awt.Dimension(660, 360));
        pnlTableContainer.setPreferredSize(new java.awt.Dimension(660, 360));
        scrPnPane1.setMaximumSize(new java.awt.Dimension(640, 350));
        scrPnPane1.setMinimumSize(new java.awt.Dimension(640, 350));
        scrPnPane1.setPreferredSize(new java.awt.Dimension(640, 350));
        tblKeyStPer.setFont(CoeusFontFactory.getLabelFont());
        tblKeyStPer.setModel(new CustomTableModel());
        tblKeyStPer.setSelectionBackground(new java.awt.Color(255, 255, 255));
        scrPnPane1.setViewportView(tblKeyStPer);

        pnlTableContainer.add(scrPnPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
        add(pnlTableContainer, gridBagConstraints);

        pnlButtonsContainer.setLayout(new java.awt.GridBagLayout());

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(106, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(106, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(106, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlButtonsContainer.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(106, 26));
        btnDelete.setMinimumSize(new java.awt.Dimension(106, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(106, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlButtonsContainer.add(btnDelete, gridBagConstraints);

        btnFindPerson.setFont(CoeusFontFactory.getLabelFont());
        btnFindPerson.setMnemonic('P');
        btnFindPerson.setText("Find Person");
        btnFindPerson.setMaximumSize(new java.awt.Dimension(106, 26));
        btnFindPerson.setMinimumSize(new java.awt.Dimension(106, 26));
        btnFindPerson.setPreferredSize(new java.awt.Dimension(106, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlButtonsContainer.add(btnFindPerson, gridBagConstraints);

        btnFindRolodex.setFont(CoeusFontFactory.getLabelFont());
        btnFindRolodex.setMnemonic('R');
        btnFindRolodex.setText("Find Rolodex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlButtonsContainer.add(btnFindRolodex, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 2, 2);
        add(pnlButtonsContainer, gridBagConstraints);

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFindPerson;
    private javax.swing.JButton btnFindRolodex;
    private javax.swing.JPanel pnlButtonsContainer;
    private javax.swing.JPanel pnlTableContainer;
    private javax.swing.JScrollPane scrPnPane1;
    private javax.swing.JTable tblKeyStPer;
    // End of variables declaration//GEN-END:variables

    // This builds an custom default table model
    public class CustomTableModel extends DefaultTableModel{

        public CustomTableModel(){
            super(new Object[][]{}, new Object []
                {"Icon", "Name", "Role", "% Effort", "Faculty", "Id", "EmployeeFlag"});
        }
        public boolean isCellEditable(int row, int col){
            /** If the proposal is parent then don't edit any of the
             *cells in the table
             */
//            if(functionType!= DISPLAY_MODE && isParentProposal()){
//                return false;
//            }

                /** In display mode editing of  table fields will be
                 * disabled
                 */

                if(functionType == DISPLAY_MODE || col == 0){
                    return false;
                }else{
                    return true;
                }
        }
        /* This method is invoked when ever the user changes the
               contents in the table cell */
//        public void fireTableCellUpdated(int row,int column){
//            setSaveRequired(true);
//        }

        /* This is Overridden method and is invoked when ever the
               data changes in table cell */
        public void setValueAt(Object value, int row, int col) {
                super.setValueAt(value,row,col);
                fireTableCellUpdated(row,col);
        }

        public Class getColumnClass(int col){
            if( (col == 4 ) || (col == 6)){
                return Boolean.class;
            } /*else if (col == 3) {
                return Float.class;
            }*/
            return Object.class;
        }
    }

    /**
     * Inner Class used to provide textField as cell editor.
     * It extends DefaulCellEditor and implements TableCellEditor interface.
     * This class overides getTableCellEditorComponent method which returns the
     * editor component to the JTable Column.
     */

    class NameEditor extends AbstractCellEditor implements TableCellEditor{

        private JTextField txtName;
        private JTextField txtRole;
        int selRow;
        int selCol;
        boolean temporary=false;
        String stTempPersonName=null;
        String stTempRole=null;
        boolean stTempFaculty=false;
        // Newly Added Handle with care
        float flEffortPercentage;
        String stTempPersonId = null;
        boolean stTempNonEmployee = false;

        /**
         * Constructor for NameEditor
         * @colName Column Name
         * @len length of the editor field.
         */
        NameEditor( String colName, int len ){
            
          //  super( new JTextField() );
            
            if(colName.equals("Name")){
                
                txtName = new JTextField();
                txtName.setDocument( new LimitedPlainDocument( len ));
                txtName.addFocusListener(new FocusAdapter(){
                    

                    public void focusLost(FocusEvent fe){

                        if (!fe.isTemporary()){
                           validatePersonName();
                        }
                    }
                });

                txtName.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        validatePersonName();
                    }
                });
            }else if( colName.equals("Role") ){
                
                txtRole = new JTextField();
                
                txtRole.setDocument(new LimitedPlainDocument(len));
                txtRole.addFocusListener(new FocusAdapter(){
                    public void focusLost(FocusEvent fe){
                        if (!fe.isTemporary()){
                            String stRoleCompare = txtRole.getText().trim();
                            int index=tblKeyStPer.getSelectedRow();
                            if(stRoleCompare != null &&
                                 !stRoleCompare.equalsIgnoreCase(stTempRole)){
                                     /* These line commented because the row indicated is not proper
                                      commented by chandra  06/02/04
                                     tblKeyStPer.getModel().
                                            setValueAt(stRoleCompare,index,2);
                                     */
                                     // Adde by chandra 06/02/2004. Throwing NullPointerException
                                    tblKeyStPer.getModel().
                                            setValueAt(stRoleCompare,selRow,2);
                                    saveRequired=true;
                            }
                            if(tblKeyStPer.getCellEditor()!=null){
                                tblKeyStPer.getCellEditor().cancelCellEditing();
                            }
                        }
                    }
                });
            }
            if( txtName != null ) {
                txtName.addMouseListener( new PersonDisplayAdapter() );
            }
            if( txtRole != null ) {
                txtRole.addMouseListener( new PersonDisplayAdapter() );
            }
            
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
        Object value,
        boolean isSelected,
        int row,
        int column){

            selRow = row;
            selCol = column;
            stTempPersonName=(String)tblKeyStPer.getValueAt(row,1);
            stTempRole=(String)tblKeyStPer.getValueAt(row,2);
            stTempFaculty=((Boolean)tblKeyStPer.
                                        getValueAt(row,4)).booleanValue();

            stTempPersonId=(String)tblKeyStPer.getValueAt(row,5);

            flEffortPercentage = tblKeyStPer.getValueAt(row,3) == null ? 0 :
                new Float(tblKeyStPer.getValueAt(row,3).toString().trim()).floatValue();

            stTempNonEmployee = ((Boolean)tblKeyStPer.
                                        getValueAt(row,6)).booleanValue();
            String newValue = ( String ) value ;

            if (column == 1) {
//                if( newValue != null && newValue.length() > 0 ){
                    txtName.setText( (String)value );
//                }else{
//                    txtName.setText("");
//                }
                return txtName;
            } else if (column == 2) {
//                if( newValue != null && newValue.length() > 0 ){
                    txtRole.setText( (String)value );
//                }else{
//                    txtRole.setText("");
//                }
                return txtRole;
            }
            return txtName;
        }
        
         // Added by chandra 06/04/1004 - start
         public int getClickCountToStart(){
            return 1;
        }
         // End chandra 06/04/1004

        /**
        * Forwards the message from the CellEditor to the delegate. Tell the
        * editor to stop editing and accept any partially edited value as the
        * value of the editor.
        * @return true if editing was stopped; false otherwise
        */

        public boolean stopCellEditing() {

            /*if(tblKeyStPer.getCellEditor() != null){
                tblKeyStPer.getCellEditor().cancelCellEditing();
            }*/
            String editingValue = (String)getCellEditorValue();
            //if(editingValue != null && editingValue.trim().length()>0){
              //  txtName.setText( editingValue );
            //}
            if (selCol == 1) {
                validatePersonName();
            }else if(selCol == 2){
                setRoleValueToTable(editingValue);
            }
            return super.stopCellEditing();
        }

        private void setRoleValueToTable(String editingValue){

            if( (editingValue == null )){
                editingValue = (editingValue == null) ? "" : editingValue;
                ((JTextField)txtRole).setText( editingValue);
                ((DefaultTableModel)tblKeyStPer.getModel()).setValueAt(editingValue,selRow,2); // Handle
                tblKeyStPer.getSelectionModel().
                                            setLeadSelectionIndex(selRow);
            }else{
                editingValue = editingValue.trim();
                if(!editingValue.equalsIgnoreCase(stTempRole)){
                    saveRequired = true;
                }
            }
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) &&
                        (stTempRole != null) && (stTempRole.trim().length()>= 0 )){
                saveRequired = true;
            }
            ((DefaultTableModel)tblKeyStPer.getModel()
                            ).setValueAt(editingValue,selRow,2);
            ((JTextField)txtRole).setText( editingValue);
             tblKeyStPer.getSelectionModel().setLeadSelectionIndex(selRow);
        }
        /** Returns the value contained in the editor.
         * @return a value contained in the editor
         */

        public Object getCellEditorValue() {
            if (tblKeyStPer.getSelectedColumn() == 2) {
                return ((JTextField)txtRole).getText();
            } else {
                return ((JTextField)txtName).getText();
            }
        }

        /** This Overridden is used to handle the item state changed events.
         * @param e ItemEvent
         */

        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }

        /**
         * Supporting method used to validate person Name for its correctness
         * existance with db data.
        */

        private void validatePersonName(){

            Vector vecData=new Vector();
            String stTxtNameValue = txtName.getText();
            if (stTxtNameValue.trim().length() <= 0) {

                tblKeyStPer.getModel().
                            setValueAt(stTempPersonName == null ?
                                            "":stTempPersonName ,selRow,1);
                tblKeyStPer.getModel().
                            setValueAt(stTempRole == null ?
                                            "":stTempRole,selRow,2);
                tblKeyStPer.getModel().
                            setValueAt(new Float(flEffortPercentage),selRow,3);

                tblKeyStPer.getModel().
                            setValueAt(new Boolean(stTempFaculty),selRow,4);

                tblKeyStPer.getModel().
                            setValueAt(stTempPersonId == null ?
                                            "":stTempPersonId,selRow,5);
                tblKeyStPer.getModel().
                            setValueAt(new Boolean(stTempNonEmployee),selRow,6);

                if(tblKeyStPer.getCellEditor() != null){
                    tblKeyStPer.getCellEditor().cancelCellEditing();
                }

            } else {
                if(!(stTempPersonName.equalsIgnoreCase(stTxtNameValue))){


                    vecData = getPersonInfo(txtName.getText().trim());

                    if(vecData != null && vecData.size()>0){

                        String stNm=(String)vecData.get(1);
                        String stRo=(String)vecData.get(2);
                        //float stEffPercentage = ((Float)vecData.get(3)).floatValue();
                        Boolean bFlag = (Boolean)vecData.get(4);
                        String stId=(String)vecData.get(5);
                        Boolean empFlag = (Boolean)vecData.get(6);

                        boolean duplicate = checkDuplicatePerson(stId,selRow);
                        if(!duplicate){
                            for( int indx = 0 ; indx < persons.size() ; indx++ ) {
                                String existingPersonId = (String) persons.elementAt( indx );
                                if( existingPersonId.equals( stTempPersonName ) ) {
                                    persons.removeElementAt(indx );
                                    break;
                                }
                            }
                            detailForm.deletePropPerson(stTempPersonName, false );

                            persons.addElement( stId );
                            //Commented for bug fix Case #2071 by tarique start 1
                            //detailForm.addPropPerson( stId , !bFlag.booleanValue() );
                            //Commented for bug fix Case #2071 by tarique end 1
                            //Modified code for bug fix Case #2071 by tarique start 2
                            detailForm.addPropPerson( stId , true );
                            //Modified code for bug fix Case #2071 by tarique end 2
                            tblKeyStPer.getModel().setValueAt(stId,selRow,5);
                            tblKeyStPer.getModel().setValueAt(empFlag,selRow,6);
                            tblKeyStPer.getModel().setValueAt(stNm,selRow,1);
                            tblKeyStPer.getModel().setValueAt(stRo,selRow,2);
                            tblKeyStPer.getModel().setValueAt(new Float(0),selRow,3);
                            tblKeyStPer.getModel().setValueAt(bFlag,selRow,4);
                            if(tblKeyStPer.getCellEditor() !=null){
                                tblKeyStPer.getCellEditor().cancelCellEditing();
                            }
                            saveRequired = true;
                        }else{
                            CoeusOptionPane.showErrorDialog("'" + stNm +"' "+
                                coeusMessageResources.parseMessageKey(
                                   "general_duplicateNameCode.2277"));
                            tblKeyStPer.getModel().setValueAt("",selRow,1);
                            if(tblKeyStPer.getCellEditor() !=null){
                                tblKeyStPer.getCellEditor().cancelCellEditing();
                                
                                
                            }
                        }
                    }else{
                        //Bug Fix: Validation on focus lost Start 4
                        /*CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                    "protoCorroFrm_exceptionCode.1054"));*/
                        //Bug Fix: Validation on focus lost End 4
                        tblKeyStPer.getModel().
                        setValueAt(stTempPersonName,selRow,1);

                        tblKeyStPer.getModel().
                        setValueAt(stTempRole,selRow,2);

                        tblKeyStPer.getModel().setValueAt(new Float(0),selRow,3);

                        tblKeyStPer.getModel().setValueAt(new
                        Boolean(stTempFaculty),selRow,4);

                        tblKeyStPer.getModel().
                        setValueAt(stTempPersonId,selRow,5);

                        tblKeyStPer.getModel().setValueAt(new
                        Boolean(stTempNonEmployee),selRow,6);

                        if(tblKeyStPer.getCellEditor() !=null){
                          tblKeyStPer.getCellEditor().cancelCellEditing();
                        }
                    }
                    tblKeyStPer.getSelectionModel().setLeadSelectionIndex(selRow);
                }else{
                    if(tblKeyStPer.getCellEditor() !=null){
                          tblKeyStPer.getCellEditor().cancelCellEditing();
                    }
                }
            }//Else part handle
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {

        Object actionSource = actionEvent.getSource();
        if(tblKeyStPer.isEditing() && tblKeyStPer.getCellEditor() != null){
            tblKeyStPer.getCellEditor().stopCellEditing();
        }

        if(actionSource.equals( btnAdd )){
            
            int rowCount = tblKeyStPer.getRowCount();
            if (tblKeyStPer.getModel() instanceof DefaultTableModel) {
                /* add a new row in table and make it as selected row */
                // change needed by raghu
                ((DefaultTableModel)
                tblKeyStPer.getModel()).addRow(
                new Object[]{"","","",".00",new Boolean(false),"",
                                                       new Boolean(false)});
                ((DefaultTableModel)
                tblKeyStPer.getModel()).fireTableDataChanged();

                int lastRow = tblKeyStPer.getRowCount() - 1;
                if(lastRow >= 0){
                    btnDelete.setEnabled(true);
                    tblKeyStPer.setRowSelectionInterval(lastRow,lastRow );
                    tblKeyStPer.scrollRectToVisible(
                        tblKeyStPer.getCellRect(lastRow,ZERO_COUNT, true));
                }
                setSaveRequired(true);
                scrPnPane1.setViewportView(tblKeyStPer);
            }
            
        //Added by Amit 11/20/2003 for IRB_DEF_Gen_6
        tblKeyStPer.requestFocusInWindow();
        tblKeyStPer.editCellAt(rowCount,1);
        tblKeyStPer.getEditorComponent().requestFocusInWindow();
        //End Amit              

        }else if(actionSource.equals( btnDelete )){

             int totalRows = tblKeyStPer.getRowCount();
                /* If there are more than one row in table then delete it */
                if (totalRows > 0) {
                    /* get the selected row */
                    int selectedRow = tblKeyStPer.getSelectedRow();
                    if (selectedRow != -1) {
                        String name = (String)((DefaultTableModel)
                            tblKeyStPer.getModel()).getValueAt(selectedRow,1);
                        int selectedOption = CoeusOptionPane.
                                            showQuestionDialog(
                                            "Are you sure you want to remove "+name.trim()+"?",
                                            CoeusOptionPane.OPTION_YES_NO,
                                            CoeusOptionPane.DEFAULT_YES);
                        // if Yes then selectedOption is 0
                        // if No then selectedOption is 1
                        if (0 == selectedOption) {
                            String personId = (String)tblKeyStPer.getValueAt(selectedRow,5 );
                            if( personId != null && personId.length() > 0 ){
                                for( int indx = 0 ; indx < persons.size() ; indx++ ) {
                                    String existingPersonId = (String) persons.elementAt( indx );

                                    if( existingPersonId.equals( personId ) ) {
                                        persons.removeElementAt(indx );
                                        break;
                                    }
                                }
                                detailForm.deletePropPerson(personId, false );
                            }

                            ((DefaultTableModel)
                            tblKeyStPer.getModel()).removeRow(selectedRow);
                            ((DefaultTableModel)
                            tblKeyStPer.getModel()).fireTableDataChanged();
                            saveRequired = true;
                            tblKeyStPer.clearSelection();
                            // find out again row count in table
                            int newRowCount = tblKeyStPer.getRowCount();
                            if(newRowCount == 0){
                                btnDelete.setEnabled(false);
                            }else{
                                // select the next row if exists
                                if (newRowCount > selectedRow) {
                                    (tblKeyStPer.getSelectionModel())
                                    .setSelectionInterval(selectedRow,
                                    selectedRow);
                                } else {
                                    tblKeyStPer.setRowSelectionInterval(
                                        newRowCount - 1, newRowCount -1 );
                                    tblKeyStPer.scrollRectToVisible(
                                    tblKeyStPer.getCellRect(
                                                    newRowCount - 1 ,
                                                    ZERO_COUNT, true));
                                }
                            }
                        }

                    } // if total rows >0 and row is selected
                    else{
                        // if total rows >0 and row is not selected
                        CoeusOptionPane.
                                    showErrorDialog(
                                        coeusMessageResources.parseMessageKey(
                                            "protoFndSrcFrm_exceptionCode.1057"));
                    }
                }

        }else if(actionSource.equals( btnFindPerson )){

            try{
                    /**
                     * Updated For : REF ID 149 Feb' 14/18 2003
                     * Person Search allows for multiple entries, however,
                     * the user can only add 1 at a time
                     *
                     * Updated by Subramanya Feb' 19 2003
                     */
                    int inIndex=tblKeyStPer.getSelectedRow();
//
//                    if(tblKeyStPer.isEditing()){
//                        String value = ((javax.swing.text.JTextComponent)
//                        tblKeyStPer.getEditorComponent()).getText();
//                        if( (value != null)){
//                            tblKeyStPer.setValueAt(value,inIndex,1);
//                        }
//                        tblKeyStPer.getCellEditor().cancelCellEditing();
//                    }

                    CoeusSearch coeusSearch =
                    new CoeusSearch(mdiReference, PERSON_SEARCH,
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ); //1);
                    coeusSearch.showSearchWindow();

                    Vector vSelectedPersons = coeusSearch.getMultipleSelectedRows();
                    if( vSelectedPersons != null ){
                    HashMap singlePersonData = null;
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){

                    singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;

                    //HashMap personInfo = coeusSearch.getSelectedRow();
                    //if(personInfo!=null){
                    if( singlePersonData !=null ){
                        /* construct the full name of person */
                        String personID = Utils.
                            convertNull(singlePersonData.get( "PERSON_ID" ));//personInfo.get("PERSON_ID"));
                        String role = Utils.
                            convertNull(singlePersonData.get( "DIRECTORY_TITLE" ));//personInfo.get("DIRECTORY_TITLE"));
                        String name = Utils.
                            convertNull(singlePersonData.get( "FULL_NAME" ));//personInfo.get("FULL_NAME"));
                        String tmpFaculty = Utils.
                            convertNull(singlePersonData.get( "IS_FACULTY" ));//personInfo.get("IS_FACULTY")).trim();
                        boolean faculty = tmpFaculty.
                            equalsIgnoreCase("y") ? true : false;

                        boolean duplicate = checkDuplicatePerson(personID);

                        if( tblKeyStPer.getSelectedRow() == -1 ){

                                Vector newKeyStudyEntry = new Vector();

                                newKeyStudyEntry.addElement( "" );
                                newKeyStudyEntry.addElement( name );
                                newKeyStudyEntry.addElement( role );
                                newKeyStudyEntry.addElement( ".00" );// For Effort Percentage
                                newKeyStudyEntry.addElement( new Boolean(faculty));
                                newKeyStudyEntry.addElement( personID );
                                newKeyStudyEntry.addElement( new Boolean(false) );
                                persons.addElement( personID );
                                detailForm.addPropPerson( personID , true );

                                ((DefaultTableModel)tblKeyStPer.getModel()
                                ).addRow( newKeyStudyEntry );
                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).
                                                    fireTableDataChanged();
                                tblKeyStPer.setRowSelectionInterval(0,0);
                                btnDelete.setEnabled(true);
                                saveRequired=true;
                                inIndex = 0;
                                continue;
                        }

                        String stPersonId = ( String)
                                ((DefaultTableModel)tblKeyStPer.
                                        getModel()).getValueAt(inIndex,5);

                        Vector vFundInfo=null;
                        if(stPersonId != null && stPersonId.trim().length() >0){
                            if(!duplicate){
                                vFundInfo = new Vector();

                                vFundInfo.addElement( "" );
                                vFundInfo.addElement( name );
                                vFundInfo.addElement( role );
                                vFundInfo.addElement( ".00" );
                                vFundInfo.addElement( new Boolean(faculty));
                                vFundInfo.addElement( personID );
                                vFundInfo.addElement( new Boolean(false) );
                                persons.addElement( personID );
                                detailForm.addPropPerson( personID , true );

                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    addRow(vFundInfo);
                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    fireTableDataChanged();

                                int newRowCount = tblKeyStPer.getRowCount();
                                tblKeyStPer.getSelectionModel().
                                    setSelectionInterval(
                                        newRowCount - 1, newRowCount - 1);
                                saveRequired = true;
                            }else{
                                CoeusOptionPane.showErrorDialog("'" + name +"' "+
                                    coeusMessageResources.parseMessageKey(
                                        "general_duplicateNameCode.2277"));
                            }
                         }else{
                             /* If the entry is not a duplicate then It adds
                                to the selected row */
                                if(!duplicate){

                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(name,inIndex,1);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(role,inIndex,2);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(".00",inIndex,3);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(faculty),inIndex,4);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(personID,inIndex,5);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(false),inIndex,6);
                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).
                                                    fireTableDataChanged();
                                tblKeyStPer.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                persons.addElement( personID );
                                detailForm.addPropPerson( personID , true );
                                saveRequired = true;
                                }else{
                                    CoeusOptionPane.showErrorDialog("'" + name +"' "+
                                        coeusMessageResources.parseMessageKey(
                                            "general_duplicateNameCode.2277"));
                                }
                            }
                    }// singlePersonData != null //personInfo !=null
                    }
                    }// end of vSelectedPerson != null
                }catch(Exception e){
                    e.printStackTrace();
                }

        }else if(actionSource.equals( btnFindRolodex )){

            try{
                    int inIndex=tblKeyStPer.getSelectedRow();
//                    if(tblKeyStPer.isEditing()){
//                        String value = ((javax.swing.text.JTextComponent)
//                        tblKeyStPer.getEditorComponent()).getText();
//                        if( (value != null)){
//                            tblKeyStPer.setValueAt(value,inIndex,1);
//                        }
//                        tblKeyStPer.getCellEditor().cancelCellEditing();
//                    }
                /**
                 * Updated For : REF ID 149 Feb' 14/19 2003
                 * Person/Rolodex Search allows for multiple entries, however,
                 * the user can only add 1 at a time
                 *
                 * Updated by Subramanya Feb' 20 2003
                 */
                    CoeusSearch coeusSearch =
                    new CoeusSearch(mdiReference, ROLODEX_SEARCH,
                     CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ; //1);
                    coeusSearch.showSearchWindow();
                    Vector vSelectedRolodex = coeusSearch.getMultipleSelectedRows();
                    if ( vSelectedRolodex != null ){

                    HashMap singleRolodexData = null;
                    for(int indx = 0; indx < vSelectedRolodex.size(); indx++ ){

                        singleRolodexData = (HashMap)vSelectedRolodex.get( indx ) ;

                    //HashMap rolodexInfo = coeusSearch.getSelectedRow();
                    //if(rolodexInfo !=null){
                    if( singleRolodexData !=null){

                        /* construct the full name of person */


                        String rolodexId = Utils.
                            convertNull(singleRolodexData.get("ROLODEX_ID"));//rolodexInfo.get("ROLODEX_ID"));
                        String role = Utils.
                            convertNull(singleRolodexData.get("TITLE"));//rolodexInfo.get("TITLE"));
                        String lastName = Utils.
                            convertNull(singleRolodexData.get("LAST_NAME"));//rolodexInfo.get("LAST_NAME"));
                        String firstName = Utils.
                            convertNull(singleRolodexData.get("FIRST_NAME"));//rolodexInfo.get("FIRST_NAME"));
                        String middleName = Utils.
                            convertNull(singleRolodexData.get("MIDDLE_NAME"));//rolodexInfo.get("MIDDLE_NAME"));
                        String suffix = Utils.
                            convertNull(singleRolodexData.get("SUFFIX"));//rolodexInfo.get("SUFFIX"));
                        String prefix = Utils.
                            convertNull(singleRolodexData.get("PREFIX"));//rolodexInfo.get("PREFIX"));
                        String name = lastName+" "+suffix+", "+
                            prefix+" "+firstName+" "+middleName;

                        if ( lastName.length() > 0) {
                            name = ( lastName +", " + suffix + " " +
                                    prefix + " " +
                                    firstName + " " + middleName ).trim();
                        } else {
                            name = Utils.convertNull(
                                            singleRolodexData.get("ORGANIZATION") );//rolodexInfo.get("ORGANIZATION") );
                        }

                        // use this name variable to display in Jtable
                        boolean duplicate = checkDuplicatePerson(rolodexId);

                        if( tblKeyStPer.getSelectedRow() == -1 ){
                                Vector newKeyStudyEntry = new Vector();
                                newKeyStudyEntry.addElement( "" );
                                newKeyStudyEntry.addElement( name );
                                newKeyStudyEntry.addElement( role );
                                newKeyStudyEntry.addElement( ".00" );
                                newKeyStudyEntry.addElement(new Boolean(false));
                                newKeyStudyEntry.addElement( rolodexId );
                                newKeyStudyEntry.addElement(new Boolean(true) );
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                ).addRow( newKeyStudyEntry );
                                persons.addElement( rolodexId );
                                detailForm.addPropPerson( rolodexId , false );

                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).fireTableDataChanged();
                                tblKeyStPer.setRowSelectionInterval(0,0);
                                btnDelete.setEnabled(true);
                                saveRequired = true;
                                inIndex = 0;
                                continue;
                        }
                        String stPersonId=( String)
                                ((DefaultTableModel)tblKeyStPer.
                                        getModel()).getValueAt(inIndex,5);
                        Vector vFundInfo=null;
                        if(stPersonId != null && stPersonId.trim().length() >0){
                            if(!duplicate){
                                vFundInfo = new Vector();
                                vFundInfo.addElement( "" );
                                vFundInfo.addElement( name );
                                vFundInfo.addElement( role );
                                vFundInfo.addElement( ".00" );
                                vFundInfo.addElement( new Boolean(false) );
                                vFundInfo.addElement( rolodexId );
                                vFundInfo.addElement( new Boolean(true) );
                                persons.addElement( rolodexId );
                                detailForm.addPropPerson( rolodexId , false );

                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    addRow(vFundInfo);
                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    fireTableDataChanged();

                                int newRowCount = tblKeyStPer.getRowCount();
                                tblKeyStPer.getSelectionModel().
                                    setSelectionInterval(
                                        newRowCount - 1, newRowCount - 1);
                                saveRequired = true;
                            }else{
                                CoeusOptionPane.showErrorDialog("' " + name + "' " +
                                    coeusMessageResources.parseMessageKey(
                                        "general_duplicateNameCode.2277"));
                            }
                         }else{
                                if(!duplicate){
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(name,inIndex,1);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(role,inIndex,2);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt( ".00" ,inIndex,3);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(false),inIndex,4);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(rolodexId,inIndex,5);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(true),inIndex,6);
                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).
                                                    fireTableDataChanged();
                                tblKeyStPer.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                persons.addElement( rolodexId );
                                detailForm.addPropPerson( rolodexId , false );
                                saveRequired = true;
                                }else{
                                    CoeusOptionPane.showErrorDialog("' " + name + "' " +
                                        coeusMessageResources.parseMessageKey(
                                           "general_duplicateNameCode.2277"));
                                }
                            }
                       }// singleRolodexData != null //personInfo !=null
                    }
                    }// end of vSelectedPerson != null
                }catch(Exception e){
                }
        }
    }

    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }
    
    // Custom Editor for Effort Field
    public class CurrencyEditor extends AbstractCellEditor implements TableCellEditor {

        private String colName;
        private float effortPercentage;
        private JComponent currencyComponent = null;
        private int selRow;

        public CurrencyEditor(String colName) {
            this.colName = colName;
        }

        /**
         * An overridden method to set the editor component in a cell.
         * @param table - the JTable that is asking the editor to edit; can be null
         * @param value - the value of the cell to be edited; it is up to the
         * specific editor to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered as a
         * string or it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected - true if the cell is to be rendered with highlighting
         * @param row - the row of the cell being edited
         * @param column - the column of the cell being edited
         * @return the component for editing
         */
        public Component getTableCellEditorComponent(JTable table,Object value,
            boolean isSelected,
        int row,int column){

            selRow = row;
            effortPercentage = tblKeyStPer.getValueAt(selRow,3) == null ? 0 :
                                            new Float(tblKeyStPer.getValueAt(selRow,3).toString().trim()).floatValue();
            String currencyValue ="";
            if (value != null) {
                currencyValue = value.toString();
            }
            currencyComponent = new CurrencyField(currencyValue);
            return currencyComponent;
        }
        // Added by chandra 06/04/1004
         public int getClickCountToStart(){
            return 1;
        }
        // End Chandra
        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {

            try {
                String editingValue = (String)getCellEditorValue();
                setEditorValueToTable(editingValue);
            }
            catch(ClassCastException exception) {
                return false;
            }
            return super.stopCellEditing();
        }

        private void setEditorValueToTable(String editingValue){
            float newEffortPercentage;

            if( (editingValue == null )){
                editingValue = (editingValue == null) ? "" : editingValue;
                ((DefaultTableModel)tblKeyStPer.getModel()).setValueAt(editingValue,selRow,3);
                tblKeyStPer.getSelectionModel().setLeadSelectionIndex(selRow);
            }else{
                editingValue = editingValue.trim();
                newEffortPercentage = new Float(editingValue).floatValue();
                if(effortPercentage != newEffortPercentage){
                    saveRequired = true;
                }
                ((DefaultTableModel)tblKeyStPer.getModel()).setValueAt(new Float(newEffortPercentage),selRow,3);
            }
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) &&
                            (effortPercentage > 0 )){
                saveRequired = true;
            }
            tblKeyStPer.getSelectionModel().setLeadSelectionIndex(selRow);
        }

        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)currencyComponent).getText();
        }

        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need to
         * occur when an cell is selected (or deselected).
         * @param e an ItemEvent.
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
}
 class PersonDisplayAdapter extends MouseAdapter {
    public void mouseClicked( MouseEvent me ) {
        int selRow = tblKeyStPer.getSelectedRow();
        if(me.getClickCount() == 2 ) {
             
              boolean nonEmployee=((Boolean)tblKeyStPer.getModel().
                               getValueAt(selRow,6)).booleanValue();
              String stId=(String)tblKeyStPer.
                                getModel().getValueAt(selRow,5);
              if (stId == null || stId.trim().length() == 0){
                  CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                            "protoKeyStPsnlFrm_exceptionCode.1069"));
                  
              }else if((nonEmployee) && (stId!=null)){

                  RolodexMaintenanceDetailForm frmRolodex =
                         new RolodexMaintenanceDetailForm('V',stId);
                  frmRolodex.showForm(mdiReference,DISPLAY_TITLE,true);

              }else if(!nonEmployee){
                  //Implement to display person Details
                  try{
                      String loginUserName = mdiReference.getUserName();
                      
                      //Changed the constructor for Case #1602 - Person Enhancement Start 
                      //String personName=(String)tblKeyStPer.getValueAt(selRow,1);
                      //PersonInfoFormBean personInfoFormBean = coeusUtils.getPersonInfoID(personName);
                      //PersonDetailForm personDetailForm = new PersonDetailForm(stId ,loginUserName,DISPLAY_MODE);
                      char MODULE_CODE = 'K';
                      PersonDetailForm personDetailForm = new PersonDetailForm
                            (stId ,loginUserName,DISPLAY_MODE, MODULE_CODE, proposalId);
                      //Changed the constructor for Case #1602 - Person Enhancement end
                      
                  }catch(Exception exception){
                      exception.printStackTrace();
                  }

                }        
        }
            
        
    }
 }
   









}

