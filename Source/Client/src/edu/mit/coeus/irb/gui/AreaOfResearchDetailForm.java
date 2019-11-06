/*
 * AreaOfResearchDetailForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * Created on September 21, 2000, 1:19 PM
 */

/* PMD check performed, and commented unused imports and variables on 18-OCT-2010
 * by Johncy M John
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusUIException;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import javax.swing.table.*;
import java.awt.Color;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
/**
 * This class is used to display all the areas of research for a committee /
 * a member. Areas of research can be added or removed for a committee or a 
 * member using this form. It calls AreaOfResearchHierarchyForm to display the
 * hierarchy tree for areas of research. If any parent area of research already
 * exists children of that node are not allowed to add explicitly.
 * @author  ravikanth
 */
public class AreaOfResearchDetailForm extends JComponent {
    
    // Variables declaration - do not modify
    private JPanel pnlMain;
    private JButton btnDelete;
    private JButton btnAdd;
    private JScrollPane scrPnMemberExpertise;
    private JTable tblMemberExpertise;
    private DefaultTableModel expertiseModel;
    private JLabel lblAreaOfResearch;

    /* variable used to store the column names used to show in the table */
    private Vector colNames=null;
    
    /* consists of the areas of expertise that are to be shown 
       in the expertise table. */
    private Vector expertiseData=null;
    /* specifies the mode in which the form will be displayed */
    private char functionType='D';
    /* reference to parent component */
    private CoeusAppletMDIForm parent=null;
    /* flag used to indicate saving of modifications */
    private boolean saveRequired = false;
    
    //flag to check the dialog window already opened..
    private boolean addClicked=false;
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
    private static final char RESEARCH_AREA_EXISTS = 'E';
    private final static String connURL = CoeusGuiConstants.CONNECTION_URL + "/AreaOfResearchServlet";  
    private String deletedResearchCode;
    
    /** reference object of CoeusMessageResources which will be used to get the
       messages to be displayed to the user */
    CoeusMessageResources coeusMessageResources 
            = CoeusMessageResources.getInstance();
    
    // End of variables declaration
    
    
   /**
     * This method is used to set the functionType for the form
     * @param char functionType which specifies the mode in which the form will
     * be displayed.
     */
    public void setFunctionType(char  functionType){
        this.functionType = functionType;
    }
    
    /**
     * Sets the column names that are required for table
     * @param colNames A Vector consisting of column names.
     */
    public void setColumnNames(Vector colNames){
        this.colNames = colNames;
    }
    
    /**
     * Gets the Column names of the table available in this panel in a Vector
     *
     * @return A Vector of column names
     */
    public Vector getColumnNames(){
        return this.colNames;
    }
    
    /**
     * This method is used to set the areas of expertise
     *
     * @param Vector which consists of areas of expertise data
     */
    public void setExpertiseData(Vector expertiseData){
        this.expertiseData = expertiseData;
    }
    
    /**
     * Gets the default data that is shown in table of this panel in a Vector
     */
    public Vector getExpertiseData(){
        return getSelectedExpertise();
    }
    /**
     * Creates new AreaOfResearchDetailForm
     */
    public AreaOfResearchDetailForm(){
    }
    
    /** 
     * Creates new form AreaOfResearchDetailForm with specified functionType and
     * expertiseData
     *
     * @param char functionType which specifies the mode in which the form is 
     * displayed
     *
     * @param Vector expertiseData which consists of the areas of expertise that
     * are to be shown in the expertise table.
     */
    public AreaOfResearchDetailForm(char functionType,Vector expertiseData) {
        
        this.functionType = functionType;
        this.expertiseData = expertiseData;
        Vector cols = new Vector();
        cols.add("Icon");
        cols.add("Code");
        cols.add("Description");
        setColumnNames(cols);
        
    }

    /**
     * This method is used to set the values to areas of research table for
     * syncronization after saving the details to the database
     *
     * @param Vector expertiseData which consists of the areas of expertise 
     */
    
    public void setValues(Vector expertiseData){
        
        this.expertiseData = expertiseData;
        Vector cols = new Vector();
        cols.add("Icon");
        cols.add("Code");
        cols.add("Description");
        setColumnNames(cols);
        ((DefaultTableModel)tblMemberExpertise.getModel()).setDataVector(
                getExpertiseTableData(),getColumnNames());
         ((DefaultTableModel)
                tblMemberExpertise.getModel()).fireTableDataChanged();
        setIconColumn();
        formatFields();
        if( tblMemberExpertise.getRowCount() > 0 ) {
            tblMemberExpertise.setRowSelectionInterval(0,0);
            tblMemberExpertise.setFocusable(true);
            
        }else{
            btnDelete.setEnabled(false);
            btnAdd.requestFocusInWindow();
            btnAdd.setFocusable(true);
            
        }
         
        saveRequired = false;
        
    }
    /** 
     * This method is used to initialize the form and set the default data to
     * be shown in the form
     *
     * @param CoeusAppletMDIForm reference to the parent dialog.
     */
    
    public JComponent showAreaOfResearch(CoeusAppletMDIForm parent) {
        
        this.parent = parent;
        initComponents();
        tblMemberExpertise.getTableHeader().setFont(
            CoeusFontFactory.getLabelFont());
        ((DefaultTableModel)
                tblMemberExpertise.getModel()).fireTableDataChanged();
        formatFields();
        if( tblMemberExpertise.getRowCount() > 0 ) {
            tblMemberExpertise.setRowSelectionInterval(0,0);
            
            tblMemberExpertise.requestFocusInWindow();
            tblMemberExpertise.setFocusable(true);
        }else{
            btnDelete.setEnabled(false);
             btnAdd.requestFocusInWindow();
             btnAdd.setFocusable(true);
             
        }
        
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblMemberExpertise.setBackground(bgListColor);
        }
        else{
            tblMemberExpertise.setBackground(Color.WHITE);
        }
        //End Amit
        
        return this;

    }
    public void setDefaultFocusToComponent(){
        if( functionType != 'D' ) {
            if( tblMemberExpertise.getRowCount() > 0 ) {
                tblMemberExpertise.requestFocusInWindow();
                tblMemberExpertise.setRowSelectionInterval(0,0);
                tblMemberExpertise.setColumnSelectionInterval(1,1);
                tblMemberExpertise.setFocusable(true);
                
            }else{
                btnAdd.requestFocusInWindow();
                
                btnAdd.setFocusable(true);
                
            }
        }
    }
    
    /** This method is called from within the showAreaOfResearch to
     * initialize the form.
     */
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;
        pnlMain = new JPanel();
        scrPnMemberExpertise = new JScrollPane();
        tblMemberExpertise = new JTable();
        tblMemberExpertise.setFont(CoeusFontFactory.getNormalFont());
        
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblMemberExpertise.setSelectionBackground(bgListColor);
            tblMemberExpertise.setSelectionForeground(Color.BLACK);
        }
        else{
            tblMemberExpertise.setSelectionBackground(Color.WHITE);
            tblMemberExpertise.setSelectionForeground(Color.black);
        }
        //End Amit        
          
        btnAdd = new JButton();
        btnDelete = new JButton();
        
        pnlMain.setLayout(new java.awt.GridBagLayout());
        // Added by chandra 29/8/2003 - To increase the frame size and table size.
        scrPnMemberExpertise.setPreferredSize(new java.awt.Dimension(500, 380));
        expertiseModel = new DefaultTableModel(getExpertiseTableData(),
                getColumnNames()){
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };
        tblMemberExpertise.setModel(expertiseModel);
        scrPnMemberExpertise.setViewportView(tblMemberExpertise);
        setIconColumn();
        lblAreaOfResearch = new JLabel();
        lblAreaOfResearch.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 0);
        pnlMain.add(lblAreaOfResearch, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 0);
        pnlMain.add(scrPnMemberExpertise, gridBagConstraints);
        
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.addActionListener(new ActionListener(){
       
            public void actionPerformed(ActionEvent ae){
             if(addClicked==false){
                 addClicked=true;
             try{
         
                 getSelectedAreaOfExpertise();
                
                 }catch(Exception e){
             }
             
             }//if close
           }
        });

        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 8);
        pnlMain.add(btnAdd, gridBagConstraints);
        btnAdd.requestFocusInWindow();
        
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent ae){
                int selRow = tblMemberExpertise.getSelectedRow();
                if ((selRow != -1) 
                        && (selRow < tblMemberExpertise.getRowCount())) {
                    saveRequired = true;
                    int option = CoeusOptionPane.showQuestionDialog( 
                            coeusMessageResources.parseMessageKey(
                                "protoAORFrm_delConfirmCode.1050"),
                            CoeusOptionPane.OPTION_YES_NO, 
                            CoeusOptionPane.DEFAULT_YES);
                    if(option == CoeusOptionPane.SELECTION_YES){
                        expertiseData.remove(selRow);
                        ((DefaultTableModel)
                                tblMemberExpertise.getModel()).removeRow(selRow);

                        int newRowCount = tblMemberExpertise.getRowCount();
                        if(newRowCount == 0){
                            btnDelete.setEnabled(false);
                             
                            btnAdd.requestFocusInWindow();
                            btnAdd.setFocusable(true);
                            
                        }
                        // select the next row if exists
                        if (newRowCount > selRow) {
                            (tblMemberExpertise.getSelectionModel())
                                .setSelectionInterval(selRow, 
                                                            selRow);
                        } else {
                            (tblMemberExpertise.getSelectionModel())
                                .setSelectionInterval(newRowCount - 1, 
                                    newRowCount - 1);
                        }
                    }
                    
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 8);
        pnlMain.add(btnDelete, gridBagConstraints);
        setLayout(new java.awt.BorderLayout());
        add(pnlMain,java.awt.BorderLayout.CENTER);
        // Added by chandra 12/09/2003
        java.awt.Component[] components = {tblMemberExpertise,btnAdd,btnDelete};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        // Added by Chandra
    }
    
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC - start
    /**
     * This method checks whether research area data are valid or not
     * whether research code exists in table or not.
     * @return boolean (returns true, if data is valid)
     */
     public boolean validateData() throws Exception{
         boolean isValid = true;
         Vector selectedNodes = getExpertiseData();
         if(selectedNodes != null && selectedNodes.size() > 0) {
             if(!isResearchAreaCodeExists(selectedNodes)) {
                log(getDeletedResearchCode() + " " + coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1012"));
                isValid = false;
            }
         }
         return isValid;
     }
     
     /**
     * This method checks whether apecific research_area_code exists in table
     * @return boolean (returns true, if data exists, else false)
     */
     private boolean isResearchAreaCodeExists(Vector vecAreaOfResearch) {
     
        boolean isExists = false;
        int count = -1;
        try {
            RequesterBean requesterBean = new RequesterBean();
            ResponderBean responderBean = new ResponderBean();
            requesterBean.setFunctionType(RESEARCH_AREA_EXISTS);
            Vector dataObjects = new Vector();
            dataObjects.addElement(ModuleConstants.PROTOCOL_MODULE_CODE);
            dataObjects.addElement(vecAreaOfResearch);
            requesterBean.setDataObjects(dataObjects);
            AppletServletCommunicator comm = new AppletServletCommunicator(connURL, requesterBean);
            comm.send();
            if(responderBean!= null){
            responderBean = comm.getResponse();
            if (responderBean.isSuccessfulResponse()) {
                count = Integer.parseInt(responderBean.getId());   
                if(responderBean.getMessage() != null) {
                    setDeletedResearchCode(responderBean.getMessage());
                }
            }
            isExists = (count > 0) ? true : false;
            }
           }catch(Exception ex) {
                ex.printStackTrace();
           }
       return isExists ;
     }
     
     /**
     * Throws the exception with the given message
     * @param mesg String represent the message to be logged.
     * @throws Exception throws new message.
     */
      public void log(String mesg) throws CoeusUIException {
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(2);
        throw coeusUIException;
      }
      
     /**
     * This method get the research area code for the deleted node
     * @return String 
     */
     private String getDeletedResearchCode() {
         return deletedResearchCode;
     }
     
     /**
     * This method set the research area code for the deleted node
     * @param String deleted Resesech Area Code.
     * @return void 
     */
     private void setDeletedResearchCode(String deletedResearchCode) {
         this.deletedResearchCode = deletedResearchCode;
     }
     //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC - end
     
    /**
     * This method is used to set the header for this form
     * @param String header with information regarding the areas of research
     */
    public void setAORHeader(String header){
        lblAreaOfResearch.setText(header==null?"":header);
    }
    
    /**
     * This method is used to set whether any modifications has been done to the
     * areas of research details 
     *
     * @param boolean true if any modifications has been done and not saved,
     * else false
     */ 
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    /**
     * This method is used to specify whether any modifications has been done to 
     * the areas of research details 
     *
     * @return boolean true if any modifications has been done and not saved,
     * else false
     */ 
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    /**
     * This method is used to show the AreaOfResearchHierarchyForm for selecting
     * the areas of research and for storing them in expertiseData vector which
     * will be sent to the server for storing in to the database
     *
     * @throws Exception if it is unable to show AreaOfResearchHierarchyForm
     * it will throw an exception
     */
    private void getSelectedAreaOfExpertise() throws java.lang.Exception{
        
        Hashtable selectedExpertises;
     
        CoeusDlgWindow dlg = new CoeusDlgWindow(parent,"Select AOR",true);
        AreaOfResearchHierarchyForm aor = new AreaOfResearchHierarchyForm(dlg);

        dlg.pack();
        aor.requestDefaultFocusForComponent();
        dlg.show();
        addClicked=false; 
        
        //aor.getSelectedNodes();
        selectedExpertises = aor.getSelectedAORNodes();
        if (selectedExpertises != null) {
        java.util.Iterator nodes = selectedExpertises.keySet().iterator();
        String expertiseDesc = null;
        String expertiseNode = null;
        //Vector data = new Vector();    
        if (expertiseData == null){
            expertiseData = new Vector();
        }
        Vector row = null;
        boolean found = false;
        while( nodes.hasNext() ){
            /* if any nodes have been selected add them to the table after
               checking for duplicates */
            row = new Vector();
            expertiseNode = nodes.next().toString();
            expertiseDesc = selectedExpertises.get(expertiseNode).toString();
            found = false;
            if (tblMemberExpertise.getRowCount() > 0) {
                for (int index =0 ; index < tblMemberExpertise.getRowCount();
                        index++) {
                    if (tblMemberExpertise.getValueAt(index,
                            1).equals(expertiseNode) ){
                        found = true;
                    }
                }
            }
            if (!found){
                saveRequired = true;
                row.add(expertiseNode);
                row.add(expertiseDesc);
                expertiseData.add(row);
            }
        }
        ((DefaultTableModel)tblMemberExpertise.getModel()).setDataVector(
                getExpertiseTableData(),getColumnNames());

        setIconColumn();
        
        int lastRow = tblMemberExpertise.getRowCount() - 1 ;
        if( lastRow >= 0 ) {
            btnDelete.setEnabled(true);
            tblMemberExpertise.setRowSelectionInterval(lastRow,lastRow);
            tblMemberExpertise.scrollRectToVisible(
                tblMemberExpertise.getCellRect(lastRow ,0, true));
            
        }else{
            btnDelete.setEnabled(false);
             //btnAdd.setFocusable(true);
            btnAdd.requestFocusInWindow();
             
        }
        
        }
        
   
    }
    
    /**
     * This method returns the areas of expertise  that are selected
     *
     * @return Vector which consists of selected Areas of Research details
     */
    public Vector getSelectedExpertise(){
        /* variable used to store the selected Expertise details*/
        java.util.Vector selectedExpertise=new Vector();

        Vector vecRow = null;
        int rows = tblMemberExpertise.getRowCount();
        for(int loopIndex=0;loopIndex<rows;loopIndex++){
            vecRow = new Vector();
            vecRow.addElement(
                    tblMemberExpertise.getValueAt(loopIndex,1).toString());
            vecRow.addElement(
                    tblMemberExpertise.getValueAt(loopIndex,2).toString());
            selectedExpertise.addElement(vecRow);
        }
        return selectedExpertise;
        
    }
    
    /**
     * This method returns the expertise data which will be used to display in
     * table.
     * @return Vector which consists of Areas of Research details
     */
    public Vector getExpertiseTableData(){
        Vector tableData = new Vector();
        if(expertiseData != null){
            Vector tableRowData = null;
            Vector expertiseRow = null;
            for(int expIndex = 0; expIndex < expertiseData.size(); expIndex++){
                tableRowData = new Vector();
                expertiseRow = (Vector)expertiseData.elementAt (expIndex);
                tableRowData.addElement("");
                tableRowData.addElement(expertiseRow.elementAt (0));
                tableRowData.addElement(expertiseRow.elementAt (1));
                tableData.addElement (tableRowData);
            }
        }
        return tableData;
    }
    /**
     * This method is used to set the enable status for the form fields 
     * depending on the functionType specified
     */
    private void formatFields(){
        
        boolean enableStatus =false;
        if (functionType == 'D' ){
            enableStatus = false;
        }else {
            enableStatus = true;
        }
        btnAdd.setEnabled(enableStatus);
        btnDelete.setEnabled(enableStatus);
        
    }    
    
    private void setIconColumn(){
        tblMemberExpertise.setShowHorizontalLines(false);
        tblMemberExpertise.setShowVerticalLines(false);
        tblMemberExpertise.setOpaque(false);
        tblMemberExpertise.setRowHeight(22);
        JTableHeader header = tblMemberExpertise.getTableHeader();
        header.setReorderingAllowed(false);
//        header.setResizingAllowed(false);
        tblMemberExpertise.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMemberExpertise.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblMemberExpertise.getColumnModel ().getColumn (0);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        column.setMaxWidth(30);
        column.setMinWidth(30);
        
        column = tblMemberExpertise.getColumnModel ().getColumn (1);
        column.setPreferredWidth(100);
//        column.setMaxWidth(100);
        column.setMinWidth(100);
        
        column = tblMemberExpertise.getColumnModel ().getColumn (2);
        column.setPreferredWidth(365);
//        column.setMaxWidth(365);
        column.setMinWidth(365);        
        
    }
}
