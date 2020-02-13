/**
 * @(#)ProposalScienceCodeAdminForm.java  1.0  
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.gui;

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

import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;

/** <CODE>ProposalScienceCodeAdminForm</CODE> is a form object which display
 * all the science codes for the selected proposal and can be used to
 * <CODE>add/delete/display</CODE> the key personl details.
 * This class is instantiated in <CODE>ProposalDetailForm</CODE>.
 *
 * @author  Raghunath P.V.
 * @version: 1.0 Created on April 03, 2003, 10:06 AM
 */

public class ProposalScienceCodeAdminForm extends javax.swing.JComponent 
                                            implements TypeConstants, ActionListener{
    
    private Vector proposalScienceCodeData;
     
     /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;
    
    /* Character variable to hold the function type which may be add,delete,
     modify */
    private char functionType;
    
     /* Specifies the proposal information is modified or not */
    private boolean proposalInfoModified = false;
    
    /* boolean variable to check the condition whether it is found or not */
    private boolean found;
    
    /* Data Beans to hold Science code Details */
    private ProposalScienceCodeFormBean proposalScienceCodeFormBean;
    
    private ProposalScienceCodeFormBean oldProposalScienceCodeFormBean;
    private ProposalScienceCodeFormBean newProposalScienceCodeFormBean;
    
    
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** This is used to verify the vaues which come from
        the database are null or not
     */
    private edu.mit.coeus.utils.Utils Utils;
    
    //holds the zero count value
    private static final int ZERO_COUNT = 0;
    
    //Added for Case#3404-Outstanding proposal hierarchy changes - Start
    private boolean parent;
    //Added for Case#3404-Outstanding proposal hierarchy changes - End
    
    /* Stores the proposal id */
    private String proposalId;
    TableSorter sorter;
    /** Creates new form <CODE>ProposalScienceCodeAdminForm</CODE>. <p>
     * <I> Default Constructor.</I>
     */
    public ProposalScienceCodeAdminForm() {
    }
    
    /** Constructor that instantiate ProposalScienceCodeAdminForm and populate the component with specified data.
     * And sets the enabled status for all components depending on the functionType.
     *
     * @param functionType is a Char variable which specifies the mode in which the
     * form will be displayed.
     * <B>'A'</B> specifies that the form is in Add Mode
     * <B>'M'</B> specifies that the form is in Modify Mode
     * <B>'D'</B> specifies that the form is in Display Mode
     * @param proposalScienceCodeData a Vector which consists of all the Proposal Science Code details
     */
    
    public ProposalScienceCodeAdminForm(char functionType,
        java.util.Vector proposalScienceCodeData, String propId) {
            this.proposalScienceCodeData = proposalScienceCodeData;
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
     * @param proposalScienceCodeData is a Vector of proposalScienceCodeFormBeans
     */
    
    public void setProposalScienceCodeData(Vector proposalScienceCodeData){
        this.proposalScienceCodeData = proposalScienceCodeData;
        if(proposalScienceCodeData != null){
            setFormData();
        }else{
            ((DefaultTableModel)tblScienceCode.getModel()).setDataVector(
                new Object[][]{},getColumnNames().toArray());
        }
        setTableEditors();
    }
    
    /** This method is used to initialize the components, set the data in the components.
      * This method is invoked in the <CODE>ProposalDetailForm</CODE>.
      * @param mdiForm is a reference of CoeusAppletMDIForm
      * @return a JPanel containing all the components with the data populated.
      */
    
    public JComponent showProposalScienceCodeForm(CoeusAppletMDIForm
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
        if( tblScienceCode!=null && tblScienceCode.getRowCount() > ZERO_COUNT ){
            tblScienceCode.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
        }else{
            btnDelete.setEnabled(false);
        }
        // setting bold property for table header values
        tblScienceCode.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        java.awt.Component[] components = {tblScienceCode,btnAdd,btnDelete};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);        
        
        return this;
    }
        
    /**
     * This method is used to set the listeners for Add & Delete buttons in this form
     */
    private void setListenersForButtons(){
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
    }

    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){
        //Modified for Case#3404-Outstanding proposal hierarchy changes - Start
        boolean enabled = functionType != DISPLAY_MODE && !isParent()  ? true : false;
        //Modified for Case#3404-Outstanding proposal hierarchy changes - End
        btnAdd.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        //tblScienceCode.setEnabled(enabled);
        
         //Added by Amit 11/19/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblScienceCode.setBackground(bgListColor);    
            tblScienceCode.setSelectionBackground(bgListColor );
            tblScienceCode.setSelectionForeground(java.awt.Color.BLACK);            
        }
        else{
            tblScienceCode.setBackground(Color.white);            
            tblScienceCode.setSelectionBackground(Color.white);
            tblScienceCode.setSelectionForeground(java.awt.Color.black);                        
        }
        //end Amit          
    }

    /**
     * This method is used to initialize the Table look and feel.
     */
    private void setTableEditors(){
        sorter = new TableSorter( ((DefaultTableModel)tblScienceCode.getModel()), false );
        tblScienceCode.setModel(sorter);
        sorter.addMouseListenerToHeaderInTable( tblScienceCode );
        
        TableColumn column = tblScienceCode.getColumnModel().getColumn(0);
        column.setMinWidth(100);
        //column.setMaxWidth(100);
        column.setPreferredWidth(100);
        //column.setResizable(false);
        
        column = tblScienceCode.getColumnModel().getColumn(1);
        column.setMinWidth(550);
        //column.setMaxWidth(400);
        column.setPreferredWidth(550);
        //column.setResizable(false);
        
        JTableHeader header = tblScienceCode.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(CoeusFontFactory.getLabelFont());
        //header.setResizingAllowed(true);
        tblScienceCode.setRowHeight(24);
        
        tblScienceCode.setOpaque(false);
        
        tblScienceCode.setShowVerticalLines(false);
        tblScienceCode.setShowHorizontalLines(false);
        
        //tblScienceCode.setCellSelectionEnabled( true );
        // Added by chandra 07/02/04 - start
        tblScienceCode.setSelectionBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
        tblScienceCode.setSelectionForeground(Color.white);
        // Added by chandra - 07/02/04 end
        tblScienceCode.setRowSelectionAllowed( true );
        tblScienceCode.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    
     /**
     * This method is used to set the form data specified in
     * <CODE> ProposalScienceCodeFormBean</CODE> 
     */
    public void setFormData(){
        
        Vector vcDataPopulate = new Vector();
        Vector vcData = null;
        
        if((proposalScienceCodeData != null) &&
                        (proposalScienceCodeData.size() > 0)){
                            
            int scienceCodeSize = proposalScienceCodeData.size();
            
            for(int index = 0; index < scienceCodeSize; index++){
                            
                proposalScienceCodeFormBean = (ProposalScienceCodeFormBean)
                                                    proposalScienceCodeData.get(index);
                
                String code = proposalScienceCodeFormBean.getScienceCode();
                String description = proposalScienceCodeFormBean.getDescription();
                
                vcData= new Vector();
                
                vcData.addElement(code == null ? "" : code);
                vcData.addElement(description == null ? "" : description);
                
                vcDataPopulate.addElement(vcData);
            }
            ((DefaultTableModel)tblScienceCode.getModel()).setDataVector(
                                        vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblScienceCode.getModel()).fireTableDataChanged();
            /*TableSorter sorter = new TableSorter( ((DefaultTableModel)tblScienceCode.getModel()), false );
            tblScienceCode.setModel(sorter);
            sorter.addMouseListenerToHeaderInTable( tblScienceCode );*/
                
        }
    }
    
    /**
     * This method is used to get the Column Names of Science Code
     * table data.
     * @return Vector collection of column names of Science Code table.
     */
    
    private Vector getColumnNames(){
        Enumeration enumColNames = tblScienceCode.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /** This method is used for validations.
     * 
     * @return true if the validation succeed, false otherwise.
     * @throws Exception a exception to be thrown in the client side.
     */
    
    public boolean validateData() throws Exception{
        return true;
    }
    
    /** This method is used to determine whether the proposal information is
     * modified.
     * @return true if the modifications done, false otherwise.
     */
    
    public boolean isProposalInfoModified(){
        return proposalInfoModified;
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
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {            
        
            if(tblScienceCode.getRowCount() > 0 ) {
                tblScienceCode.requestFocusInWindow();
                tblScienceCode.setRowSelectionInterval(0, 0);
                tblScienceCode.setColumnSelectionInterval(0,1);
            }else{
                btnAdd.requestFocusInWindow();
            }            
        }
    }    
    //end Amit     
    
    /** This method is used to set true or false to the saveRequired member variable
     * @param save is a boolean variable to be set to saveRequired variable.
     */
    
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    //Helper method to construct bean from table values.
    private Vector getTableValues(){
        
        Vector scienceCodeValues = new Vector();
        int rowCount = tblScienceCode.getRowCount();
        ProposalScienceCodeFormBean pscFormBean;
        
        for(int index = 0; index < rowCount ; index++){
            
            String code = (String)((DefaultTableModel)tblScienceCode.getModel()).
                                                            getValueAt(index,0);
            if(code != null && code.trim().length() > 0 ){
                
                String desc=(String)((DefaultTableModel)
                                    tblScienceCode.getModel()).getValueAt(index,1);

                pscFormBean= new ProposalScienceCodeFormBean();
                
                pscFormBean.setScienceCode(code);
                pscFormBean.setDescription(desc);
                pscFormBean.setProposalNumber(proposalId);
                
                scienceCodeValues.addElement(pscFormBean);
                
            }else{
                
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                        "protoKeyStPsnlFrm_exceptionCode.1067"));
                tblScienceCode.requestFocus();
            }
        }
        return scienceCodeValues;
    }
    
    /** Method to get the science code data in a Vector
     * which consists of ProposalScienceCodeFormBean.
     * It sets the AcType as 'U' to the bean if any bean data is modified.
     * It sets the AcType to 'i' if any data is inserted into JTable by the user.
     * It sets the AcType to 'D' if any data is deleted from JTable by the user.
     * @return Vector of bean data.
     */    
    public Vector getScienceCodeAdminData(){
        
        /* This block of code is used to set AcType as D to all the beans 
           if all the rows are deleted in the JTable and it sets the 
           proposalInfoModified flag to true stating that the save is required 
           for the user */
        if((proposalScienceCodeData != null) &&
           (proposalScienceCodeData.size()>0) &&
           (tblScienceCode.getRowCount()<=0)){
               
            int noOfScienceCodes = proposalScienceCodeData.size();
            
            for(int oldIndex = 0; oldIndex < noOfScienceCodes; oldIndex++){
                
                oldProposalScienceCodeFormBean = (ProposalScienceCodeFormBean)
                    proposalScienceCodeData.elementAt(oldIndex);
                oldProposalScienceCodeFormBean.setAcType(DELETE_RECORD);
                
                setSaveRequired(true);
                
                if(functionType == MODIFY_MODE){
                    proposalInfoModified = true;
                }
                proposalScienceCodeData.
                    setElementAt(oldProposalScienceCodeFormBean,oldIndex);
            }
            return proposalScienceCodeData;
        }
        /* This gets all the data from the JTable*/
        Vector newData = getTableValues();
        
        if((newData != null) && (newData.size() > 0)){
            int dataSize = newData.size();
            for(int newLocIndex = 0; newLocIndex < dataSize;newLocIndex++){
                int foundIndex = -1;
                found = false;
                newProposalScienceCodeFormBean = (ProposalScienceCodeFormBean)newData.
                                                        elementAt(newLocIndex);
                if(proposalScienceCodeData != null &&
                    proposalScienceCodeData.size() > 0){
                        
                    int noOfScienceCodes = proposalScienceCodeData.size();
                    for(int oldLocIndex = 0;oldLocIndex < noOfScienceCodes;
                     oldLocIndex++){
                         
                        oldProposalScienceCodeFormBean = (ProposalScienceCodeFormBean)
                        proposalScienceCodeData.elementAt(oldLocIndex);
                        // Update Logic
                        if(newProposalScienceCodeFormBean.getScienceCode().equals(
                                    oldProposalScienceCodeFormBean.getScienceCode())){
                                        
                            found = true;
                            foundIndex = oldLocIndex;
                            break;
                        }
                    }
                }else{
                    proposalScienceCodeData = new Vector();
                }
                if(!found){
                    
                    //if location is new set AcType to INSERT_RECORD
                    newProposalScienceCodeFormBean.setAcType(INSERT_RECORD);
                    newProposalScienceCodeFormBean.setProposalNumber(proposalId);
                    setSaveRequired(true);
                    
                    if(functionType == MODIFY_MODE){
                        proposalInfoModified = true;
                    }
                    
                    proposalScienceCodeData.addElement(newProposalScienceCodeFormBean);
                }else{
                        /* if present set the values to the bean. if modified,
                           bean will fire property change event */
                    if(oldProposalScienceCodeFormBean != null){
                        
                        oldProposalScienceCodeFormBean.setScienceCode(
                                        newProposalScienceCodeFormBean.getScienceCode());
                        oldProposalScienceCodeFormBean.setDescription(
                                        newProposalScienceCodeFormBean.getDescription());
                        if(foundIndex != -1){
                            proposalScienceCodeData.setElementAt(
                                               oldProposalScienceCodeFormBean,foundIndex);
                        }
                    }
                }
            }
            if(proposalScienceCodeData != null 
                && proposalScienceCodeData.size() > 0){
                    
                int noOfCodes = proposalScienceCodeData.size();
                for(int oldLocIndex = 0; oldLocIndex < noOfCodes;
                        oldLocIndex++){
                            
                    found = false;
                    oldProposalScienceCodeFormBean = (ProposalScienceCodeFormBean)
                    proposalScienceCodeData.elementAt(oldLocIndex);
                    int newDataSize = newData.size();
                    for(int newLocIndex = 0; newLocIndex < newDataSize;
                    newLocIndex++){
                        newProposalScienceCodeFormBean = (ProposalScienceCodeFormBean)newData.
                                                        elementAt(newLocIndex);
                        if(oldProposalScienceCodeFormBean.getScienceCode().equals(
                                        newProposalScienceCodeFormBean.getScienceCode())){
                                            
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        
                        oldProposalScienceCodeFormBean.setAcType(DELETE_RECORD);
                        setSaveRequired(true);
                        if(functionType == MODIFY_MODE){
                            proposalInfoModified = true;
                        }
                        proposalScienceCodeData.setElementAt(
                                            oldProposalScienceCodeFormBean,oldLocIndex);
                    }
                }
            }
        }
        return proposalScienceCodeData;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnScienceCode = new javax.swing.JScrollPane();
        tblScienceCode = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnScienceCode.setPreferredSize(new java.awt.Dimension(660, 360));
        scrPnScienceCode.setMinimumSize(new java.awt.Dimension(660, 360));
        scrPnScienceCode.setMaximumSize(new java.awt.Dimension(660, 360));
        tblScienceCode.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblScienceCode.setFont(CoeusFontFactory.getNormalFont());
        scrPnScienceCode.setViewportView(tblScienceCode);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 4, 3);
        add(scrPnScienceCode, gridBagConstraints);

        btnAdd.setMnemonic('A');
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("Add");
        btnAdd.setPreferredSize(new java.awt.Dimension(106, 26));
        btnAdd.setMaximumSize(new java.awt.Dimension(106, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(106, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 0, 0);
        add(btnAdd, gridBagConstraints);

        btnDelete.setMnemonic('D');
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        btnDelete.setPreferredSize(new java.awt.Dimension(106, 26));
        btnDelete.setMaximumSize(new java.awt.Dimension(106, 26));
        btnDelete.setMinimumSize(new java.awt.Dimension(106, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(btnDelete, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblScienceCode;
    private javax.swing.JButton btnDelete;
    private javax.swing.JScrollPane scrPnScienceCode;
    private javax.swing.JButton btnAdd;
    // End of variables declaration//GEN-END:variables
    
    // Helper method which populates the Science code
    
    private void showSearchWindow(String strSearchType){
        try{
            CoeusSearch coeusSearch =
                    new CoeusSearch(mdiReference, strSearchType, 
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
            
            coeusSearch.showSearchWindow();
            Vector vecSelectedScienceCodes = coeusSearch.getMultipleSelectedRows(); 
            
            if( vecSelectedScienceCodes != null ){
                
                HashMap singleData = null;
                int size = vecSelectedScienceCodes.size();
                for(int indx = 0; indx < size; indx++ ){
                    singleData = (HashMap)vecSelectedScienceCodes.get( indx ) ;     
                    if(singleData != null){
                        String code = Utils.
                            convertNull(singleData.get( "SCIENCE_CODE" ));
                        String description = Utils.
                            convertNull(singleData.get( "DESCRIPTION" ));
                        
                        boolean duplicate = checkDuplicateScienceCode(code);
                        
                        Vector vecData = null;
                        if(!duplicate){
                            vecData = new Vector();
                            vecData.addElement( code );
                            vecData.addElement( description );
                            addRowInSortOrder ( vecData );
                            int newRowCount = tblScienceCode.getRowCount();
                            tblScienceCode.getSelectionModel().
                                setSelectionInterval(
                                    newRowCount - 1, newRowCount - 1);
                            saveRequired = true;
                            btnDelete.setEnabled(true); 
                        }
                    }
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * Adds the given vector data as row in the table in sorted order.
     * @param newRow Vector which consists of science code row details.
     */
    private void addRowInSortOrder ( Vector newRow ) {
        DefaultTableModel tableModel = (DefaultTableModel )tblScienceCode.getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = tblScienceCode.getRowCount();
        int rowIndex=0;
        for(rowIndex =0;rowIndex<rowCount;rowIndex++){
            String userId = ( String )tblScienceCode.getValueAt( rowIndex, 0 );
            if( userId.compareTo((String)newRow.elementAt(0)) < 0){
                continue;
            }
            else {
                break;
            }

        }
        sorter.insertRow( rowIndex, newRow );
    }
    
    
    
    /** 
     *  Method used to validate whether the Science Code is duplicate or not 
     */
    
    private boolean checkDuplicateScienceCode(String code){
        
        boolean duplicate = false;
        String oldId = "";
        int size = tblScienceCode.getRowCount();
        for(int rowIndex = 0; rowIndex < size; rowIndex++){
            
            oldId = (String)tblScienceCode.getValueAt(rowIndex,0);
            if(oldId != null){
                if(oldId.equals(code)){
                    duplicate = true;
                    break;
                }
            }
        }
        return duplicate;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object actionSource = actionEvent.getSource();
        if(actionSource.equals( btnAdd )){
            showSearchWindow("SCIENCECODESEARCH");
        }else if(actionSource.equals( btnDelete )){
            
             int totalRows = tblScienceCode.getRowCount();
                /* If there are more than one row in table then delete it */
            if (totalRows > 0) {
                /* get the selected row */
                int[] selectedRows = tblScienceCode.getSelectedRows();
                //System.out.println("selectedRows:"+selectedRows);
                if (selectedRows.length > 0) {
                    int selectedOption = -1;
                    if (selectedRows.length == 1) {
                        selectedOption = CoeusOptionPane.
                                        showQuestionDialog(
                                        coeusMessageResources.parseMessageKey(
                                        "generalDelConfirm_exceptionCode.2100") + " row?",
                                        CoeusOptionPane.OPTION_YES_NO, 
                                        CoeusOptionPane.DEFAULT_YES);
                    } else {
                        selectedOption = CoeusOptionPane.
                                        showQuestionDialog(
                                        "Are you sure you want to delete these " 
                                        + selectedRows.length + " rows?",
                                        CoeusOptionPane.OPTION_YES_NO, 
                                        CoeusOptionPane.DEFAULT_YES);
                    }
                    // if Yes then selectedOption is 0
                    // if No then selectedOption is 1
                    if ( selectedOption == 0) {
                        int rowCount = tblScienceCode.getRowCount();
                        for(int rowIndex = selectedRows.length-1; rowIndex >=0 ; rowIndex--) {
                            int row = sorter.getIndexForRow( selectedRows[rowIndex]);
                            //System.out.println("model index:"+row);
                            sorter.removeRow( row );
                            // If the previous row is to be selected uncomment thses codes
                            
                            //((DefaultTableModel)tblScienceCode.getModel()).fireTableRowsDeleted(row,row);
                              //  tblScienceCode.setRowSelectionInterval(row-1, row-1);
                                //tblScienceCode.scrollRectToVisible(tblScienceCode.getCellRect(
                                 //row-1 ,0, true));
                        }
                        // If the Previous need to be selected then comment the below line
                        tblScienceCode.clearSelection();
                        saveRequired = true;
                        // find out again row count in table
                        int newRowCount = tblScienceCode.getRowCount();
                        if(newRowCount == 0){
                            btnDelete.setEnabled(false);
                        }
                    }

                } 
            }
             
        }
    }

    //Added for Case#3404-Outstanding proposal hierarchy changes - Start

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }
    //Added for Case#3404-Outstanding proposal hierarchy changes - End
}
