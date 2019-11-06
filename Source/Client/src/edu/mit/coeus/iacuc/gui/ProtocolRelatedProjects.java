/*
 * @(#)ProtocolRelatedProjects.java 1.0 13/08/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.table.*;
import java.awt.Component;
import java.util.Vector;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DefaultCellEditor;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.iacuc.bean.ProtocolRelatedProjectsBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.*;


/** This class  links protocols to the proposals and awards
 *
 * @version :1.0 August 13, 2003, 10:35 AM
 * @author Manoj Kumar .A
 */


public class ProtocolRelatedProjects extends CoeusDlgWindow implements ActionListener{
    
    
    /** this is a constant string to tell search as Award Search
     */
    private static final String AWARD_SEARCH = "AWARDSEARCH";
    /** this is constant string to tell search is Institutional Proposal
     */
    private static final String INS_PROPOSAL_SEARCH = "PROPOSALSEARCH";
    /** this is a constant string to tell Search is proposal search
     */
    private static final String PROPOSAL_SEARCH = "PROPOSALDEVSEARCHNOROLES";
    /** this string used to identify the type search
     */
    private String searchIdentifier = null;
    /** This holds the reference to the base window
     */
    
    private CoeusAppletMDIForm mdiForm; /* Parent window to current dialog window */
    /** This contains Column Names in the Protorelated projects table
     */
    private Vector colNames;
    
    /** This tells whether linkage is enabled or disabled from protocol to awards or proposal
     */
    private boolean linkEnabled;
    /** this variable tells whether this window modified or not
     */
    private boolean modified = false;
    /** this contains the servlet name to connect
     */
    private final String PROTO_SERVLET = "/IacucProtocolServlet";
    /** This tells the servlet that all module codes to be return
     */
    private final char GET_MODULE_CODE = 'Q';
    private final char VALID_MODULE_CODE = 'W';
    /** this gives the value to connec to servlet
     */
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTO_SERVLET;
    //private final String UPDATE_RECORD = "U";
    //private final String DELETE_RECORD = "D";
    //private final String INSERT_RECORD = "I";
    /** this contains all related projects for a protocol and it contains null for new protocol
     */
    private Vector protoRelatedProjects;
    /** this contains all available module codes
     */
    private Vector availableLinks;
    /** this gives the display mode of the window
     */
    private char displayMode;
    /** this tells that this screen is in display mode
     */
    private final char DISPLAY = 'D';
    /** this tells that window is in Add mode
     */
    private final char ADD_MODE = 'A';
    /** this tells that window displayed in modify mode
     */
    private final char MODIFY_MODE = 'M';
    
    /** this flag gives whether save is required or not for this window
     */
    private boolean saveRequired = false;
    /** This contains ProtoRelatedProjectsBean instance to hold related projects
     */
    private ProtocolRelatedProjectsBean relatedBean = null;
    //private boolean isAllProjectsDelete;
    /** this contains the Coues message resources instance for parsing the messages
     */
    CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    /** This constructor creates a instance of reference window to
     * to link protocol to awards and proposals
     * @param mdiForm this holds the reference to base window
     * @param protocolId this tells to which protocol related projects are
     * linking
     * @param modal
     */
    public ProtocolRelatedProjects(CoeusAppletMDIForm mdiForm, String title,
    boolean modal, Vector protoRelated, char mode)
    throws Exception {
        super(mdiForm,title,modal);
        this.mdiForm = mdiForm;
        this.displayMode = mode;
        protoRelatedProjects = protoRelated;
        initComponents();       // Initilizes the components on Window
        java.awt.Component[] components = {tblProtoRelated,btnOk,btnCancel,btnAdd,btnDelete,btnFind};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);  

        //Added by Amit 11/19/2003
        if(displayMode  == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblProtoRelated.setBackground(bgListColor);    
            tblProtoRelated.setSelectionBackground(bgListColor );
            tblProtoRelated.setSelectionForeground(java.awt.Color.black);              
        }
        else{
            tblProtoRelated.setBackground(java.awt.Color.white);            
            tblProtoRelated.setSelectionBackground(java.awt.Color.white);
            tblProtoRelated.setSelectionForeground(java.awt.Color.black);            
        }
        //end Amit         
        
        setListeners();         // Sets the Listeners to components
        setColNames();          // sets the column names to table
        setTableModel();        // sets the Table Model
        setTableEditors();      // Sets the Table Editors
        setFormData();          // sets the form data and table initial values
        setInitialValues();     // sets find button enable or disable based on linkEnabled flag
        if( tblProtoRelated.getRowCount() == 0 ){
            btnDelete.setEnabled(false);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        scrPnRelated = new javax.swing.JScrollPane();
        tblProtoRelated = new javax.swing.JTable();

        getContentPane().setLayout(new java.awt.FlowLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setFont(new java.awt.Font("SansSerif", 0, 11));
        setName("protocolRelatedDialog");
        setResizable(false);
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setFont(CoeusFontFactory.getLabelFont());
        pnlMain.setMinimumSize(new java.awt.Dimension(650, 225));
        pnlMain.setPreferredSize(new java.awt.Dimension(650, 225));
        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlButtons.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlButtons.add(btnDelete, gridBagConstraints);

        btnFind.setFont(CoeusFontFactory.getLabelFont());
        btnFind.setMnemonic('F');
        btnFind.setText("Find");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlButtons.add(btnFind, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 14, 0);
        pnlMain.add(pnlButtons, gridBagConstraints);

        scrPnRelated.setFont(CoeusFontFactory.getLabelFont());
        scrPnRelated.setMinimumSize(new java.awt.Dimension(400, 175));
        scrPnRelated.setPreferredSize(new java.awt.Dimension(500, 175));
        tblProtoRelated.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrPnRelated.setViewportView(tblProtoRelated);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        pnlMain.add(scrPnRelated, gridBagConstraints);

        getContentPane().add(pnlMain);

        pack();
    }//GEN-END:initComponents
   
    /** This method shows the Protocol related projects window
     */
    public void showDialog() {
        this.setLocationRelativeTo(mdiForm);
        this.setResizable(false);
        if(tblProtoRelated.getRowCount() >0){
            tblProtoRelated.setRowSelectionInterval(0,0);
            //tblProtoRelated.setEditingRow(0);
            tblProtoRelated.setEditingColumn(0);
        }
        this.show();
    }
    /** this method Closes this window
     */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }
    /** This method adds the required Listeners to components
     */
    /* Adding listeners to required components*/
    private void setListeners() {
        /* this listener is to close window when Esc pressed */
        this.setDefaultCloseOperation(javax.swing.JDialog.DO_NOTHING_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
        ActionListener actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(modified) {
                    int index = tblProtoRelated.getSelectedRow();
                    if(index != -1 && index >= 0){
                        tblProtoRelated.getCellEditor(index,1).stopCellEditing();
                    }
                    confirmClosing();
                }
                else {
                    closeDialog();
                }
            }
        };
        this.getRootPane().registerKeyboardAction(actionListener,stroke,
        javax.swing.JComponent.WHEN_FOCUSED);
        this.getRootPane().registerKeyboardAction(actionListener,stroke,
        javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowActivated(java.awt.event.WindowEvent we){
                requestDefaultFocusForComponent();
            }
            public void windowClosing(java.awt.event.WindowEvent we) {
                int index = tblProtoRelated.getSelectedRow();
                if(index != -1 && index >= 0){
                    if( tblProtoRelated.isEditing() ){
                        tblProtoRelated.getCellEditor(index,2).stopCellEditing();
                    }
                }
                if(modified){
                    confirmClosing();
                }
                else {
                    closeDialog();
                }
            }
        });
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnFind.addActionListener(this);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
    }
    private void requestDefaultFocusForComponent(){
        if( displayMode == DISPLAY ) {
            btnCancel.requestFocusInWindow();
        }else if( tblProtoRelated.getRowCount() > 0 ){
            tblProtoRelated.requestFocusInWindow();
        }else{
            btnAdd.requestFocusInWindow();
        }
    }
    /** This method captures the events generated by the Components
     */
    /* Receiving the events*/
    public void actionPerformed(ActionEvent actionEvent){
        int index = tblProtoRelated.getSelectedRow();
        if(index != -1 && index >= 0){
            tblProtoRelated.getCellEditor(index,1).stopCellEditing();
        }
        Object source = actionEvent.getSource();
        if(source == btnAdd){
            modified= true;
            Vector newRowData = new Vector();
            newRowData.addElement("");          /* Adding new Empty column if Add Button clicked*/
            if(availableLinks != null && availableLinks.size() > 0){
                newRowData.addElement( ((ComboBoxBean)availableLinks.get(0)).
                getDescription());
            }else{
                newRowData.addElement("");
            }
            newRowData.addElement("");
            newRowData.addElement("");
            ((DefaultTableModel)tblProtoRelated.getModel()).
            addRow(newRowData);
            ((DefaultTableModel)tblProtoRelated.getModel()).fireTableDataChanged();
            int lastRow = tblProtoRelated.getRowCount() - 1;
            if(lastRow >= 0){
                btnDelete.setEnabled(true);
                tblProtoRelated.setRowSelectionInterval( lastRow, lastRow );
                tblProtoRelated.scrollRectToVisible(tblProtoRelated.getCellRect(
                lastRow ,0, true));
            }
            tblProtoRelated.editCellAt(lastRow,2);
            tblProtoRelated.getEditorComponent().requestFocusInWindow();
        }else if(source == btnFind){
            try{
                /*  Calling corresponding Search  when find button clicked*/
                if(tblProtoRelated.getRowCount()>0){
                if(index != -1){
                    String  stSearch=(String)((DefaultTableModel)tblProtoRelated.
                    getModel()).getValueAt(index,1);
                    if(stSearch != null && !stSearch.equals("") && !stSearch.trim().equals("")){
                        CoeusSearch coeusSearch;
                        int noOfTabs = 0;
                        HashMap relatedInfo;
                        int moduleCode = getModuleCode(stSearch);
                        if(moduleCode == 1){
                            noOfTabs = 1;
                            searchIdentifier = AWARD_SEARCH;
                        }else if(moduleCode == 2){
                            noOfTabs = 1;
                            searchIdentifier = INS_PROPOSAL_SEARCH;
                        }else if(moduleCode ==3){
                            noOfTabs = 1;
                            searchIdentifier = PROPOSAL_SEARCH;
                        }
                        coeusSearch = new CoeusSearch(mdiForm, searchIdentifier, noOfTabs);
                        coeusSearch.showSearchWindow();
                        relatedInfo = coeusSearch.getSelectedRow();
                        if(relatedInfo != null){
                            String title = "";
                            title = (String)relatedInfo.get("TITLE");
                            String projNo = "";
                            if(moduleCode ==1){
                                projNo = (String)relatedInfo.get("MIT_AWARD_NUMBER");
                            }else if(moduleCode ==2){
                                projNo = (String)relatedInfo.get("PROPOSAL_NUMBER");
                            }else if(moduleCode ==3){
                                projNo = (String)relatedInfo.get("PROPOSAL_NUMBER");
                            }
                            if(!checkDuplicate(stSearch,projNo,-1)){
                                ((DefaultTableModel)tblProtoRelated.getModel()).setValueAt(projNo,index,2);
                                ((DefaultTableModel)tblProtoRelated.getModel()).
                                setValueAt(title,index,3);
                                modified = true;
                            }else{
                                CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey("protoRelProj_exceptionCode.1001"));
                            }
                        }
                    }
                }
                
            }
            }catch(Exception ex){               /* Catching the exceptions to debug*/
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
            }
        }else if(source == btnDelete){
            int rowIndex = tblProtoRelated.getSelectedRow();
            if(rowIndex != -1 && rowIndex >= 0){
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("protoRelProj_exceptionCode.1004"),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES){
                    ((DefaultTableModel)tblProtoRelated.getModel()).removeRow(rowIndex);
                    modified = true;
                    if(rowIndex >0){
                        tblProtoRelated.setRowSelectionInterval(rowIndex-1,rowIndex-1);
                    }else{
                        if(tblProtoRelated.getRowCount()>0){
                            tblProtoRelated.setRowSelectionInterval(0,0);
                        }else{
                            btnAdd.requestFocusInWindow();
                            btnDelete.setEnabled(false);
                        }
                    }
                }
            }
        }else if(source == btnCancel){ /* Closing when Calcel Button Clicked*/
            if(tblProtoRelated.getRowCount()>0){
            int selRow = tblProtoRelated.getSelectedRow();
            if(selRow != -1 && selRow >= 0){
                tblProtoRelated.getCellEditor(selRow,2).stopCellEditing();
            }
            }
            if(modified){ 
                confirmClosing();
            }else{
                this.dispose();
            }
        }else if(source == btnOk){       /* Functionality to be added when ok clicked*/
            int selRow = tblProtoRelated.getSelectedRow();
            if(tblProtoRelated.getRowCount()>0){
            if(selRow !=-1 && selRow>=0){
                tblProtoRelated.getCellEditor(selRow,2).stopCellEditing();
            }
        }
            if(modified){
                if(isValidTableData()){
                    setSaveRequired(true);
                    closeDialog();
                }else{
                    CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.
                    parseMessageKey("protoRelProj_exceptionCode.1005"));
                }
            }else{
                closeDialog();
            }
        }
    } // end actionPerformed
    /** this method validates the table data
     *
     * @return
     */
    private boolean isValidTableData(){
        
        int rowCount = tblProtoRelated.getRowCount();
        
        if(rowCount >0){
            for(int rowIndex =0;rowIndex<rowCount;rowIndex++){
                String projNo = (String)tblProtoRelated.getValueAt(rowIndex,2);
                if(projNo == null || projNo.equals("") || projNo.trim().equals("")){
                    return false;
                }
            }
        }
        return true;
    }
    /** this sets the form data. it populates all available projects into table
     */
    private void setFormData() {
        if(protoRelatedProjects != null && protoRelatedProjects.size() > 0){
            for(int index = 0; index < protoRelatedProjects.size(); index++){
                relatedBean = (ProtocolRelatedProjectsBean)protoRelatedProjects.elementAt(index);
                Vector rowData = new Vector();
                rowData.addElement("");
                rowData.addElement(getDescription(relatedBean.getModuleCode()));
                rowData.addElement(relatedBean.getProjectNumber());
                rowData.addElement(relatedBean.getTitle());
                ((DefaultTableModel)tblProtoRelated.getModel()).addRow(rowData);
            }
        }
    }
    /** this returns all table values in the form a vaector
     * @return
     */
    private Vector getTableValues(){
        Vector tableData = null;
        int rowCount = tblProtoRelated.getRowCount();
        if(rowCount != -1 && rowCount > 0){
            tableData = new Vector();
            for(int rowIndex = 0;rowIndex<rowCount;rowIndex++){
                ProtocolRelatedProjectsBean relatedBean = new ProtocolRelatedProjectsBean();
                relatedBean.setModuleCode(getModuleCode((String)tblProtoRelated.getValueAt(rowIndex,1)));
                String projNo = (String)tblProtoRelated.getValueAt(rowIndex,2);
                relatedBean.setProjectNumber(projNo.trim());
                relatedBean.setTitle((String)tblProtoRelated.getValueAt(rowIndex,3));
                tableData.addElement(relatedBean);
            }
        }
        return tableData;
    }
    /** this sets the initial values to the components. this also sets the state of each component based on isLineEnabled flag
     */
    private void setInitialValues() {
        scrPnRelated.setBorder(new javax.swing.border.TitledBorder(
        new javax.swing.border.EtchedBorder(),
        "",
        javax.swing.border.TitledBorder.LEFT,
        javax.swing.border.TitledBorder.TOP,
        CoeusFontFactory.getLabelFont()));
        tblProtoRelated.setShowHorizontalLines(false);
        tblProtoRelated.setShowVerticalLines(false);
        tblProtoRelated.setOpaque(false);
        tblProtoRelated.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        /* Setting Button Find to Disable if ENABLE_PROTOCOL_TO_PROJECTS_LINK value is false ie 0 */
        if(!isLinkEnabled()) {
            btnFind.setEnabled(false);
        }
        if(displayMode == DISPLAY){
            btnOk.setEnabled(false);
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
            btnFind.setEnabled(false);
            //  tblProtoRelated.setEnabled(false);
        }
    }// end initial values
    /** this sets the IsLinkEnabled flag values
     * @param value
     */
    private void setLinkEnabled(boolean value){
        linkEnabled = value;
    }
    /** this returns whether protocol linkage to projects enabled or not
     * @return
     */
    private boolean isLinkEnabled(){
        return linkEnabled;
    }
    /** this sets save required flag
     * @param value
     */
    public void setSaveRequired(boolean value){
        saveRequired = value;
    }
    /** this metohd returns value whether save is required or not
     * @return
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    /** this method confirms before closing the window
     */
    private void confirmClosing(){
        int option = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
        CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
        if(option == CoeusOptionPane.SELECTION_YES){
            if(isValidTableData()){
                setSaveRequired(true);
                closeDialog();
            }else{
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.
                parseMessageKey("protoRelProj_exceptionCode.1005"));
            }
        }else if(option == CoeusOptionPane.SELECTION_NO){
            setSaveRequired(false);
            closeDialog();
        }
    }
    /** this method returns all related projects to this protocol
     * @return
     */
    public Vector getFormData(){
        Vector newData = getTableValues();
        return newData;
    }
    
    /** this method sets ths colnames for the table
     */
    private void setColNames() {
        colNames = new Vector();
        colNames.add("");
        colNames.add("Module Code");
        colNames.add("Project Number");
        colNames.add("Title");
    }
    /** this method returns the column names
     * @return
     */
    private Vector getColNames() {
        return this.colNames;
    }
    /** This sets the table Model for the table
     */
    private void setTableModel(){
        tblProtoRelated.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "", "Module Code", "Project Number", "Title "
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if(displayMode == DISPLAY){
                    return false;
                }
                return canEdit [columnIndex];
            }
        });
    }
    /** this method validates the project number entered by user
     * @return
     */
    private Vector validateProjectNo(int moduleCode,String projNo){
        
        ProtocolRelatedProjectsBean protoRelatedBean = new ProtocolRelatedProjectsBean();
        protoRelatedBean.setModuleCode(moduleCode);
        protoRelatedBean.setProjectNumber(projNo);
        RequesterBean request = new RequesterBean();    /* Bean to connect to server*/
        request.setDataObject(protoRelatedBean);
        request.setFunctionType(VALID_MODULE_CODE);              /*  Setting funtion type to get mode */
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);    /* Applet servlet communicator to
                                                                 to conect to servlet */
        comm.send();
        ResponderBean response = comm.getResponse();            /* Getting Server Respnse*/
        Vector result = null;
        if(response.isSuccessfulResponse()){
            result = (Vector)response.getDataObjects();
            System.out.println(result);
        }
        return result;
    }
        /* This method is used to set the cell editors to the columns,
       set the column width to each individual column, disable the column
       resizable feature to the JTable, setting single selection mode to the
       JTable */
    /** This method sets the Table Editors
     */
    private void setTableEditors()throws Exception{
        
        TableColumn column = tblProtoRelated.getColumnModel().getColumn(0);
        column.setMinWidth(25);
        column.setMaxWidth(25);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(25);
        
        //tblProtoRelated.getTableHeader().setReorderingAllowed(false);
        JTableHeader header = tblProtoRelated.getTableHeader();
        //header.setResizingAllowed(true);
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        tblProtoRelated.setRowHeight(24);
        
        TableColumn clmType = tblProtoRelated.getColumnModel().getColumn(1);
        clmType.setMinWidth(25);
        //clmType.setMaxWidth(135);
        clmType.setPreferredWidth(135);
        
        JComboBox  coeusCombo = new JComboBox();
        coeusCombo.setFont(CoeusFontFactory.getNormalFont());
        availableLinks = getRelatedProjectTypes();
        Vector availableLinkDesc = getDescriptions( availableLinks );
        coeusCombo.setModel(new CoeusComboBox(availableLinkDesc,false).getModel());
        
        clmType.setCellEditor(new DefaultCellEditor(coeusCombo){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().toString();
            }
            public int getClickCountToStart(){
                return 2;
            }
        });
        coeusCombo.addItemListener( new ItemListener(){
            public void itemStateChanged( ItemEvent item ){
                int sRow = tblProtoRelated.getEditingRow();
                if(sRow != -1){
                    ((DefaultTableModel)tblProtoRelated.getModel()).
                    setValueAt("",sRow,2);
                    ((DefaultTableModel)tblProtoRelated.getModel()).
                    setValueAt("",sRow,3);
                    modified = true;
                }
            }
            
        });
        column = tblProtoRelated.getColumnModel().getColumn(2);
        column.setMinWidth(25);
        //column.setMaxWidth(450);
        column.setPreferredWidth(150);
        //column.setResizable(true);
        
        column.setCellEditor(new ProjectNoCellEditor(50));
        column = tblProtoRelated.getColumnModel().getColumn(3);
        column.setMinWidth(50);
        //column.setMaxWidth(450);
        //column.setResizable(true);
        column.setPreferredWidth(175);
        tblProtoRelated.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        //tblProtoRelated.setSelectionBackground(java.awt.Color.white);
        //tblProtoRelated.setSelectionForeground(java.awt.Color.black);
    }//End setTableEditor
    /** this method returns module code for the description
     * @param desc contains project no
     * @return
     */
    private int getModuleCode(String desc){
        int code = 0;
        if(availableLinks != null && availableLinks.size() >0){
            for(int index = 0;index<availableLinks.size();index++){
                ComboBoxBean bean = (ComboBoxBean)availableLinks.elementAt(index);
                if(bean.getDescription().equals(desc)){
                    return (Integer.parseInt(bean.getCode()));
                }
            }
        }
        return code;
    }
    /** this method returns descrption for the given module code
     * @param code contains the value of module code
     * @return
     */
    private String getDescription(int code){
        String strCode = null;
        if(availableLinks != null && availableLinks.size() >0){
            strCode = ""+code;
            for(int index = 0;index<availableLinks.size();index++){
                ComboBoxBean bean = (ComboBoxBean)availableLinks.elementAt(index);
                if(bean.getCode().equals(strCode)){
                    return bean.getDescription();
                }
            }
        }
        return strCode;
    }
    
    /** this method returns descriptions for available module codes
     * @param beanData takes the all module codes as beans
     * @return
     */
    private Vector getDescriptions( Vector beanData ) {
        Vector data = null;
        if( beanData != null && beanData.size()>0) {
            data = new Vector();
            for(int index = 0;index<beanData.size();index++){
                ComboBoxBean bean = (ComboBoxBean)beanData.elementAt(index);
                data.addElement(bean.getDescription());
            }
        }
        return data;
    }
    /** This Method Gives the existing related project types
     * @return
     */
    private Vector getRelatedProjectTypes()throws Exception {
        RequesterBean request = new RequesterBean();    /* Bean to connect to server*/
        request.setFunctionType(GET_MODULE_CODE);              /*  Setting funtion type to get mode */
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);    /* Applet servlet communicator to
                                                                 to conect to servlet */
        comm.send();
        ResponderBean response = comm.getResponse();            /* Getting Server Respnse*/
        if(response.isSuccessfulResponse()){
            Vector dataObjects = (Vector)response.getDataObjects();
            Vector moduleCodes = null;
            if(dataObjects != null && dataObjects.size() > 0 ){
                moduleCodes = (Vector)dataObjects.elementAt(0);
                String linkValue = (String)dataObjects.elementAt(1);
                if(linkValue != null && !linkValue.equals("") && !linkValue.trim().equals("")){
                    System.out.println(linkValue);
                    if(linkValue.equals("1")){
                        setLinkEnabled(true);
                    }else if(linkValue.equals("0")){
                        setLinkEnabled(false);
                    }
                }else{
                    setLinkEnabled(false);
                }
            }else{
                moduleCodes = new Vector();
                moduleCodes.add(" ");
            }
            return moduleCodes;
        }else {
            throw new CoeusClientException(response.getMessage());
        }
    }
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    static class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        /** The Overridden method of TableCellRenderer which is called for every
         * cell when a component
         * is going to be rendered in its cell.
         * Returns the component used for drawing the cell.
         * This method is used to configure the renderer appropriately before
         * drawing
         *
         * @param table  the JTable that is asking the renderer to draw; can be
         * null
         * @param value  the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered as
         * a string or it could be rendered as a check box that is checked.
         * null is a valid value
         * @param isSelected  true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited,
         * render in the color used to indicate editing
         * @param row the row index of the cell being drawn.
         * When drawing the header, the value of row is -1
         * @param column the column index of the cell being drawn
         * @return Component which is to be rendered.
         */
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }//End EmptyHeaderRenderer Inner Class
    
    
    /** This is Iconrendere to display HAND icon for the selected row in the table
     */
    static class IconRenderer  extends DefaultTableCellRenderer {
        
        
        /** This holds the Image Icon of Hand Icon
         */
        private final ImageIcon HAND_ICON =
        new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND_ICON));
        private final ImageIcon EMPTY_ICON = null;
        /** Default Constructor*/
        IconRenderer() {
        }
        /**
         * An overridden method to render the component(icon) in cell.
         * foreground/background for this cell and Font too.
         *
         * @param table  the JTable that is asking the renderer to draw;
         * can be null
         * @param value  the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value. For example,
         * if value is the string "true", it could be rendered as a string or
         * it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected  true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited, render
         * in the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the
         * header, the value of row is -1
         * @param column  the column index of the cell being drawn
         * @return Component
         *
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
         * boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
            setText((String)value);
            setOpaque(false);
            /* if row is selected the place the icon in this cell wherever this
               renderer is used. */
            if( isSelected ){
                setIcon(HAND_ICON);
            }else{
                setIcon(EMPTY_ICON);
            }
            return this;
        }
        
    }//End Icon Rendering inner class
    /** this classs instance sets the cell editor for project number
     */
    class ProjectNoCellEditor extends DefaultCellEditor implements TableCellEditor {
        /** conatins the project number value
         */
        String projValue = null;
        /** contains selected row
         */
        int selRow = 0;
        /** textfield instance for holding the project number
         */
        private JTextField txtProjectNo;
        /** this is constructor for project number cell editor and takes max length allowed for project number
         */
        ProjectNoCellEditor(int len){
            super( new JTextField() );
            txtProjectNo = new JTextField();
            txtProjectNo.setDocument(new LimitedPlainDocument(len));
            txtProjectNo.setFont(CoeusFontFactory.getNormalFont());
            txtProjectNo.addFocusListener(new java.awt.event.FocusAdapter(){
                
                public void focusLost(java.awt.event.FocusEvent fe){
                    if( !fe.isTemporary() ) {
                        stopCellEditing();
                    }
                }
            });
        }
        /** this method returns cell editor for the project numer column
         * @param component contians the table object
         * @param values contains the valued to set
         * @param isselected contains true if this cell selected
         * @param row contains selected row
         * @param col
         */
        public Component getTableCellEditorComponent(JTable table,
        Object value, boolean isSelected,int row, int column){
            projValue = (String)tblProtoRelated.getValueAt(row,2);
            String newValue = ( String ) value ;
            selRow = row;
            if( newValue != null && newValue.length() > 0 ){
                txtProjectNo.setText( (String)value );
            }else{
                txtProjectNo.setText("");
            }
            return txtProjectNo;
        }
        /** this method stops the cell editing
         */
        public boolean stopCellEditing() {
            
            int selRow = tblProtoRelated.getSelectedRow();
            if(selRow != -1 && selRow >= 0 ){
//                modified = true;
                String oldValue = (String)tblProtoRelated.getValueAt(selRow,2);
                String projVal = txtProjectNo.getText();
                if(projVal != null && !projVal.trim().equals("")){
                    oldValue = oldValue==null?"":oldValue;
                    if(!oldValue.equals(projVal)){
                        modified = true;
                    }
                }
                if(isLinkEnabled()){
                   // String projVal = txtProjectNo.getText();
                    String selValue = (String)tblProtoRelated.getValueAt(selRow,1);
                    if(projVal != null && !projVal.equals("") && !projVal.trim().equals("")){
                        int moduleCode = 0;
                        moduleCode = getModuleCode(selValue);
                        projVal = projVal.trim().toUpperCase();
                        Vector result = validateProjectNo(moduleCode,projVal);
                        if(result != null && result.size() > 0 ){
                            Boolean valid = (Boolean)result.elementAt(0);
                            if(valid.booleanValue()==true){
                                String title = (String)result.elementAt(1);
                                String newProjNo  = txtProjectNo.getText();
                                String module=(String)tblProtoRelated.getValueAt(selRow,1);
                                if(checkDuplicate(module,newProjNo,selRow)){
                                    txtProjectNo.setText(oldValue);
                                    CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey("protoRelProj_exceptionCode.1001"));
                                }else{
                                    ((DefaultTableModel)tblProtoRelated.getModel()).setValueAt(title,selRow,3);
                                }
                            }else{
                                CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey("protoRelProj_exceptionCode.1003"));
                                txtProjectNo.setText(oldValue);
                            }
                        }
                    }else{
                        txtProjectNo.setText(oldValue);
                    }
                }else{
                    String newProjNo  = txtProjectNo.getText();
                    String module=(String)tblProtoRelated.getValueAt(selRow,1);
                    if(checkDuplicate(module,newProjNo,selRow)){
                        txtProjectNo.setText(oldValue);
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protoRelProj_exceptionCode.1001"));
                    }
                    tblProtoRelated.setValueAt("",selRow,3);
                }
            }
            return super.stopCellEditing();
        }
        /** this method returns the valued in the cell
         * @return
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtProjectNo).getText();
        }
        /** this method is to tell click count to start for selecting a cell
         * @return returns number as interger
         *
         */
        public int getClickCountToStart(){
            return 1;
        }
    } //end Name Editor Class
    /** this method validates the table date for duplicate rows prsent or not
     * @param projno contains the project number
     * @param modulecode contains the module code
     * @param selRow
     */
    private boolean checkDuplicate(String module,String projNo,int selRow){
        int rowCount = tblProtoRelated.getRowCount();
        if(rowCount !=-1 && rowCount >0){
            for(int rowIndex = 0;rowIndex<rowCount;rowIndex++){
                String exModule = (String)tblProtoRelated.getValueAt(rowIndex,1);
                String exProjNo = (String)tblProtoRelated.getValueAt(rowIndex,2);
                if(module.equals(exModule)){
                    if(projNo.equals(exProjNo)){
                        if(rowIndex != selRow)
                            return true;
                    }
                }
            }
        }
        return false;
    }
/*    /** Getter for property isAllProjectsDelete.
 * @return Value of property isAllProjectsDelete.
     //
    public boolean isAllProjectsDelete() {
        return isAllProjectsDelete;
    }
 
    /** Setter for property isAllProjectsDelete.
 * @param isAllProjectsDelete New value of property isAllProjectsDelete.
     //
    public void setIsAllProjectsDelete(boolean isAllProjectsDelete) {
        this.isAllProjectsDelete = isAllProjectsDelete;
    } */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrPnRelated;
    private javax.swing.JTable tblProtoRelated;
    // End of variables declaration//GEN-END:variables
}
