/*
 * @(#)TrainingInformation.java 1.0 3/8/04 6:41 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 30-MAY-2007
 * by Leena
 */
package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.beans.*;
import java.util.Vector;
import java.util.Date;

/**
 *
 * @author  chandru
 */
public class PersonTrainingInformationForm extends javax.swing.JComponent
        implements ActionListener,ListSelectionListener,FocusListener{
    
    private CoeusDlgWindow dlgTraining;
    
    private TrainingInfoTableModel trainingInfoTableModel;
    private TrainingInfoTableCellEditor trainingInfoTableCellEditor;
    private TrainingInfoTableCellRenderer trainingInfoTableCellRenderer;
    
    private static final int TRAINING_COLUMN = 0;
    private static final int DATE_REQUESTED_COLUMN = 1;
    private static final int DATE_SUBMITTED_COLUMN = 2;
    private static final int DATE_ACKNOWLEDGED_COLUMN = 3;
    private static final int FOLLOWUP_DATE = 4;
    private static final int SCORE_COLUMN = 5;
    
    private static final int WIDTH = 750;
    private static final int HEIGHT = 400;
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + "/personMaintenanceServlet";
    private static final char GET_TRAINING_INFO = 'E';
    private static final char UPDATE_TRAINING_INFO= 'F';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //used to denote the form is taken from the Personnel module of the Maintain Menu
    private static final char PERSONNEL_MODULE_CODE = 'N';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    
    private CoeusAppletMDIForm mdiForm;
    private static final String EMPTY_STRING = "";
    private DateUtils dtUtils = new DateUtils();
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    private SimpleDateFormat dtFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private String personId = "";
    private String personName = "";
    private CoeusVector cvTrainingData = null;
    private CoeusVector cvFormData = null;
    private CoeusVector cvDeletedData = null;
    
    private ListSelectionModel trainingSelectionModel;
    private boolean hasRights = false;
//    private DepartmentPersonTrainingBean departmentPersonTrainingBean;
    private CoeusMessageResources coeusMessageResources;
    private boolean modified = false;
    private static final String INVALID_DATE =
            "Invalid Date. Please enter valid Date";
    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
    private static final String TRAINING_CODE_VALIDATION = "person_trainingCode_excetionCode.1100";
    
    private int lastSelectedRow;
    
    //added by Nadh
    //starts 2 aug 2004
    private Vector personData;
    private char functionType;
    private boolean canEditable = true;
    private char moduleType;
    private int TRAINING_COLUMN_WIDTH = 130;
    private int DATE_REQUESTED_COLUMN_WIDTH = 100;
    private int DATE_SUBMITTED_COLUMN_WIDTH = 100;
    private int DATE_ACKNOWLEDGED_COLUMN_WIDTH = 125;
    private int FOLLOWUP_DATE_WIDTH = 100;
    private int SCORE_COLUMN_WIDTH = 95;
    //nadh end 2 aug 2004
    
    /** Creates new form TrainingInformation */
    public PersonTrainingInformationForm(CoeusAppletMDIForm mdiForm, String personId,String personName) {
        this.mdiForm = mdiForm;
        this.personId = personId;
        this.personName = personName;
        initComponents();
        registerComponents();
        setFormData();
        lblHeader.setText("Training info for " +personName);
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvDeletedData = new CoeusVector();
        setColumnData();
        postInitComponents();
        display();
    }
    
    //Added by Nadh for enhancement to show traing info tab along with other tabs in person details form
    /** Creates new form TrainingInformation */
    //start 2 aug 2004
    public PersonTrainingInformationForm(char functionType,char moduleType, String personName, Vector data) {
        
        personData = new Vector();
        this.functionType = functionType;
        this.personData = data;
        this.canEditable=false;
        this.personName = personName;
        this.moduleType = moduleType;
        initComponents();
        registerComponents();
        setFormData();
        lblHeader.setText("Training info for " +personName);
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvDeletedData = new CoeusVector();
        setFormComponents();
        setColumnData();
        display();
    }
    
    /* this set form component sizes
     *@ returns void
     */
    private void setFormComponents(){
        TRAINING_COLUMN_WIDTH = 85;
        DATE_REQUESTED_COLUMN_WIDTH = 100;
        DATE_SUBMITTED_COLUMN_WIDTH = 100;
        DATE_ACKNOWLEDGED_COLUMN_WIDTH = 120;
        FOLLOWUP_DATE_WIDTH = 90;
        SCORE_COLUMN_WIDTH = 55;
        btnAdd.setVisible(false);
        btnDelete.setVisible(false);
        btnOk.setVisible(false);
        btnCancel.setVisible(false);
        this.setPreferredSize(new java.awt.Dimension(555,260));
        jscrPnTrainingInfo.setMinimumSize(new java.awt.Dimension(555,150));
        jscrPnTrainingInfo.setPreferredSize(new java.awt.Dimension(555,150));
        jscrPnComments.setMinimumSize(new java.awt.Dimension(555,100));
        jscrPnComments.setPreferredSize(new java.awt.Dimension(555,100));
    }
    //Nadh end 2 aug 2004
    
    private void display(){
        int rowCount = tblTrrainingInfo.getRowCount();
        if(rowCount != 0){
            DepartmentPersonTrainingBean bean = (DepartmentPersonTrainingBean)cvFormData.elementAt(0);
            txtArComments.setText( (bean.getComments() == null) ? EMPTY_STRING : bean.getComments());
            tblTrrainingInfo.setRowSelectionInterval(0,0);
            //commented and added for case# 3318 - Delete button enabled even when user does not have MAINTAIN_TRAINING right - Start
            // 'hasRights' has boolean value true if user has MAINTAIN_TRAINING right else it is false
//            btnDelete.setEnabled(true);
            btnDelete.setEnabled(hasRights);
            //commented and added for case# 3318 - Delete button enabled even when user does not have MAINTAIN_TRAINING right - End            
            //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
            //Comments Field is made editable only when user has 'MAINTAIN_TRAINING' right
            //if(canEditable){
            if(canEditable && hasRights){//Case#4346 - End
                txtArComments.setEditable(true);
            }else{
                txtArComments.setEditable(false);
            }
            
        }else{
            // If there are no rows found in the table then disable the entry for the comments
            txtArComments.setEditable(false);
            btnDelete.setEnabled(false);
        }
        modified = false;
        if(canEditable){
            dlgTraining.setVisible(true);
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //To disable the table and the comments text area while in the dispaly mode
        if(moduleType == PERSONNEL_MODULE_CODE && functionType == TypeConstants.DISPLAY_MODE){
            //Commented for Case#4346 -  Person training information not consistently displayed, saved -Start
            //Instead of disabling the table,isCellEditable() is set to false in TrainingInfoTableModel, for 
            //row selection in the table.
//            tblTrrainingInfo.setEnabled(false);
            txtArComments.setEditable(false);
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    }
    private void postInitComponents(){
        
        dlgTraining = new CoeusDlgWindow(mdiForm);
        dlgTraining.getContentPane().add(this);
        dlgTraining.setTitle("Training Information");
        dlgTraining.setFont(CoeusFontFactory.getLabelFont());
        dlgTraining.setModal(true);
        dlgTraining.setResizable(false);
        dlgTraining.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgTraining.getSize();
        dlgTraining.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgTraining.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
        dlgTraining.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgTraining.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                setDefaultFocus();
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
    }
    
    private void setDefaultFocus(){
        if(hasRights){
            if( tblTrrainingInfo.getRowCount() > 0 ) {
                //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
                //Default focus is set to TrainingCode combobox, when form is loaded intially
//                txtArComments.requestFocusInWindow();
                tblTrrainingInfo.setRowSelectionInterval(0,0);
                tblTrrainingInfo.setColumnSelectionInterval(0,0);
                tblTrrainingInfo.editCellAt(0,0);
                tblTrrainingInfo.getEditorComponent().requestFocusInWindow();
                //Case#4346 - End
            }else{
                btnOk.requestFocusInWindow();
            }
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
    
    /** Register all the compoonents and create the instance of the model,editor and
     *rendere and set the model to the JTabel
     */
    private void registerComponents(){
        
        txtArComments.setDocument(new LimitedPlainDocument(3878));
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        txtArComments.addFocusListener(this);
        
        trainingInfoTableCellEditor = new TrainingInfoTableCellEditor();
        trainingInfoTableCellRenderer = new TrainingInfoTableCellRenderer();
        trainingInfoTableModel = new TrainingInfoTableModel();
        
        tblTrrainingInfo.setModel(trainingInfoTableModel);
        
        trainingSelectionModel = tblTrrainingInfo.getSelectionModel();
        trainingSelectionModel.addListSelectionListener(this);
        trainingSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        tblTrrainingInfo.setSelectionModel( trainingSelectionModel );
        
        Component[] comp = {txtArComments,btnOk,btnCancel,btnAdd,btnDelete,tblTrrainingInfo };
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
    }
    //set the form data and do the right checking
    //modified by Nadh for enhancement to show traing info tab in person details form
    private void setFormData(){
        if(canEditable){
            Vector data = getFormData(personId);
            Boolean obj = (Boolean) data.elementAt(0);
            cvTrainingData = (CoeusVector)data.elementAt(1);
            cvFormData = (CoeusVector)data.elementAt(2);
            hasRights = obj.booleanValue();
            // check for the right MODIFY_TRAINING role right
            if(!hasRights){
                btnAdd.setEnabled(hasRights);
                btnDelete.setEnabled(hasRights);
                btnOk.setEnabled(hasRights);
                txtArComments.setEditable(false);
                trainingInfoTableCellRenderer.txtComponent.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                trainingInfoTableCellRenderer.txtDateComponent.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                //Commented for Case#4346 -  Person training information not consistently displayed, saved -Start
                //tblTrrainingInfo.setEnabled(false);
                //Case#4246 - End
                txtArComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                btnCancel.requestFocusInWindow();
            }
        }else{
            Vector data = personData;
            cvTrainingData = (CoeusVector)data.elementAt(0);
            cvFormData = (CoeusVector)data.elementAt(1);
            
        }
        
        trainingInfoTableModel.setData(cvFormData);
        
    }
    //nadh end 2 aug 2004
    /** communicate with the server to get the form data
     */
    private Vector getFormData(String personId){
        Vector data = null;
        
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setFunctionType(GET_TRAINING_INFO);
        requester.setDataObject(personId);
        
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            data = (Vector)responder.getDataObjects();
        }
        return data;
    }
    
    /** Update the form data after the modification
     */
    private void updateFormData(){
        CoeusVector dataObject = new CoeusVector();
        // check whether the delete vector is null
        if(cvDeletedData!=null && cvDeletedData.size() > 0){
            dataObject.addAll(cvDeletedData);
            dataObject.addAll(cvFormData);
        }else{
            // check if the data vector is null or not. If null create a new
            // instance of that.To avoid null pointer, when there is no data
            // while deleting.
            if(cvFormData!=null && cvFormData.size() > 0){
                dataObject.addAll(cvFormData);
            }else{
                cvFormData= new CoeusVector();
                dataObject.addAll(cvFormData);
            }
        }
        
        
        dataObject = dataObject.filter(new NotEquals("acType", null));
        if(dataObject!=null && dataObject.size() > 0  ){
            
            RequesterBean requester;
            //ResponderBean responder;
            
            requester = new RequesterBean();
            requester.setFunctionType(UPDATE_TRAINING_INFO);
            
            requester.setDataObject(dataObject);
            
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, requester);
            
            comm.send();
//            responder = comm.getResponse();
//            if(responder.isSuccessfulResponse()){
//                System.out.println("Update Success !!!");
//            }else {
//                System.out.println("Update Problem!!!!!!");
//            }
        }
    }
    
    public boolean validateData(){
        trainingInfoTableCellEditor.stopCellEditing();
        
        int rowCount = tblTrrainingInfo.getRowCount();
        if(rowCount > 0){
            for(int index = 0; index < rowCount; index++){
                Object selectedValue = trainingInfoTableModel.getValueAt(index,TRAINING_COLUMN);
                String selectedItem = EMPTY_STRING;
                if( selectedValue instanceof ComboBoxBean ) {
                    selectedItem = ((ComboBoxBean)selectedValue).getDescription();
                }else{
                    selectedItem = ( selectedValue == null ) ? EMPTY_STRING : selectedValue.toString();
                }
//                String selectedItem = (String)trainingInfoTableModel.getValueAt(index,TRAINING_COLUMN);
                if(selectedItem == null ||selectedItem.equals(EMPTY_STRING)){
                    trainingInfoTableCellEditor.cmbTraining.requestFocus();
                    tblTrrainingInfo.setRowSelectionInterval(index ,index );
                    tblTrrainingInfo.scrollRectToVisible(
                            tblTrrainingInfo.getCellRect(index, TRAINING_COLUMN, true));
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey( TRAINING_CODE_VALIDATION));
                    return false;
                }
            }
        }
        return true;
    }
    
    
    private void setColumnData(){
        
        JTableHeader tableHeader = tblTrrainingInfo.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        tblTrrainingInfo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblTrrainingInfo.setRowHeight(22);
        tblTrrainingInfo.setSelectionBackground(java.awt.Color.white);
        tblTrrainingInfo.setSelectionForeground(java.awt.Color.black);
        tblTrrainingInfo.setShowHorizontalLines(false);
        tblTrrainingInfo.setShowVerticalLines(false);
        tblTrrainingInfo.setOpaque(false);
        tblTrrainingInfo.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = tblTrrainingInfo.getColumnModel().getColumn(TRAINING_COLUMN);
        column.setMinWidth(70);
        column.setMaxWidth(180);
        column.setPreferredWidth(TRAINING_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellRenderer(trainingInfoTableCellRenderer);
        column.setCellEditor(trainingInfoTableCellEditor);
        
        column = tblTrrainingInfo.getColumnModel().getColumn(DATE_REQUESTED_COLUMN);
        column.setMinWidth(100);
        column.setMaxWidth(150);
        column.setPreferredWidth(DATE_REQUESTED_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellRenderer(trainingInfoTableCellRenderer);
        column.setCellEditor(trainingInfoTableCellEditor);
        
        column = tblTrrainingInfo.getColumnModel().getColumn(DATE_SUBMITTED_COLUMN);
        column.setMinWidth(100);
        column.setMaxWidth(150);
        column.setPreferredWidth(DATE_SUBMITTED_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellRenderer(trainingInfoTableCellRenderer);
        column.setCellEditor(trainingInfoTableCellEditor);
        
        column = tblTrrainingInfo.getColumnModel().getColumn(DATE_ACKNOWLEDGED_COLUMN);
        column.setMinWidth(100);
        column.setMaxWidth(160);
        column.setPreferredWidth(DATE_ACKNOWLEDGED_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellRenderer(trainingInfoTableCellRenderer);
        column.setCellEditor(trainingInfoTableCellEditor);
        
        column = tblTrrainingInfo.getColumnModel().getColumn(FOLLOWUP_DATE);
        column.setMinWidth(90);
        column.setMaxWidth(150);
        column.setPreferredWidth(FOLLOWUP_DATE_WIDTH);
        column.setResizable(true);
        column.setCellRenderer(trainingInfoTableCellRenderer);
        column.setCellEditor(trainingInfoTableCellEditor);
        
        column = tblTrrainingInfo.getColumnModel().getColumn(SCORE_COLUMN);
        column.setMinWidth(55);
        column.setMaxWidth(150);
        column.setPreferredWidth(SCORE_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellRenderer(trainingInfoTableCellRenderer);
        column.setCellEditor(trainingInfoTableCellEditor);
    }
    
    
    
    
    public   void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnAdd)){
            performAddAction();
        }else if(source.equals(btnDelete)){
            performDeleteAction();
        }else if(source.equals(btnCancel)){
            performCancelAction();
        }else if(source.equals(btnOk)){
            performOkAction();
        }
    }
    
    // Add  a new row as an empty row
    private void performAddAction(){
        DepartmentPersonTrainingBean newBean = new DepartmentPersonTrainingBean();
        newBean.setTrainingCode(0);
        newBean.setDateRequested(null);
        newBean.setDateSubmitted(null);
        newBean.setDateAcknowledged(null);
        newBean.setFollowUpDate(null);
        newBean.setScore(EMPTY_STRING);
        newBean.setPersonId(personId);
        newBean.setComments(EMPTY_STRING);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        if(cvFormData!=null){
            cvFormData.add(newBean);
        }else{
            cvFormData = new CoeusVector();
            cvFormData.add(newBean);
        }
        trainingInfoTableModel.fireTableRowsInserted(
                trainingInfoTableModel.getRowCount() + 1, trainingInfoTableModel.getRowCount() + 1);
        
        int lastRow = tblTrrainingInfo.getRowCount()-1;
        if(lastRow >= 0){
            tblTrrainingInfo.setRowSelectionInterval( lastRow, lastRow );
            
            tblTrrainingInfo.scrollRectToVisible(
                    tblTrrainingInfo.getCellRect(lastRow ,0, true));
            tblTrrainingInfo.editCellAt(lastRow,0);
            Component comp = tblTrrainingInfo.getEditorComponent();
            if( comp != null ){
                comp.requestFocusInWindow();
            }
        }
    }
    
    private void performDeleteAction(){
        trainingInfoTableCellEditor.stopCellEditing();
        String mesg = EMPTY_STRING;
        int rowIndex = tblTrrainingInfo.getSelectedRow();
        
        if(rowIndex != -1 && rowIndex >= 0){
            mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(mesg),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                int rowSelect = 0;
                
                if(rowIndex > 0){
                    rowSelect = rowIndex - 1;
                } else {
                    rowSelect = 0;
                }
                
                DepartmentPersonTrainingBean deletedBean =
                        (DepartmentPersonTrainingBean)cvFormData.get(rowIndex);
                if(rowIndex==0){
                    txtArComments.setText(EMPTY_STRING);
                }
                if (deletedBean.getAcType() == null ||
                        deletedBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedData.add(deletedBean);
                    cvFormData.remove(rowIndex);
                    // If the row is added and then deleted, then the Ac Type will be
                    // "I".
                }else if(deletedBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedData.add(deletedBean);
                    cvFormData.remove(rowIndex);
                }
                
                if (cvFormData!= null && cvFormData.size() > 0) {
                    //if (tblTrrainingInfo.getRowCount()!=-1) {
                    lastSelectedRow = rowSelect;
                    DepartmentPersonTrainingBean bean = (DepartmentPersonTrainingBean)cvFormData.get(lastSelectedRow);
                    txtArComments.setText((bean.getComments() == null) ? EMPTY_STRING : bean.getComments());
                    trainingInfoTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    tblTrrainingInfo.setRowSelectionInterval(
                            rowSelect,rowSelect);
                    tblTrrainingInfo.scrollRectToVisible(
                            tblTrrainingInfo.getCellRect(
                            rowSelect ,0, true));
                }else{
                    trainingInfoTableModel.fireTableRowsDeleted(0,0);
//                    tblTrrainingInfo.setRowSelectionInterval(0,0);
//                    tblTrrainingInfo.scrollRectToVisible(
//                    tblTrrainingInfo.getCellRect(0,0, true));
                }
                modified = true;
                //deletedBean.setAw_TrainingNumber(deletedBean.getTrainingNumber());
            }
        }
    }
    
    private void performCancelAction(){
        trainingInfoTableCellEditor.stopCellEditing();
        int selRow = tblTrrainingInfo.getSelectedRow();
        if(selRow!=-1){
            DepartmentPersonTrainingBean bean =
                    (DepartmentPersonTrainingBean)cvFormData.get(selRow);
            if(!(bean.getComments() == null && txtArComments.getText().length() == 0)) {
                
                if((bean.getComments() ==null && txtArComments.getText().length() > 0)
                || (! bean.getComments().equals(txtArComments.getText()))) {
                    modified = true;
                }
            }
        }
        //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
        //if(modified){
        if(modified && hasRights){//Case#4346 - End
            confirmClosing();
        }else{
            dlgTraining.dispose();
        }
    }
    
    private void confirmClosing(){
        trainingInfoTableCellEditor.stopCellEditing();
        try{
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                performOkAction();
            }else if(option == CoeusOptionPane.SELECTION_NO){
                dlgTraining.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    private void performOkAction(){
        trainingInfoTableCellEditor.stopCellEditing();
        if (modified && hasRights) {//COEUSDEV-860
            if (validateData()) {
                int selRow = tblTrrainingInfo.getSelectedRow();
                if (selRow != -1 && cvFormData != null && cvFormData.size() > 0) {
                    DepartmentPersonTrainingBean bean =
                            (DepartmentPersonTrainingBean) cvFormData.get(selRow);
                    if (!(bean.getComments() == null && txtArComments.getText().length() == 0)) {

                        if ((bean.getComments() == null && txtArComments.getText().length() > 0)
                                || (!bean.getComments().equals(txtArComments.getText()))) {
                            bean.setComments(txtArComments.getText());
                            modified = true;
                            // bean.setAw_TrainingNumber(bean.getTrainingNumber());
                            if (bean.getAcType() == null) {
                                bean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                        }
                    }
                }
                //if(modified){
                updateFormData();
                dlgTraining.dispose();
//            }else{
//                dlgTraining.dispose();
//            }
            }
        //COEUSDEV-860 - START
        } else {
            dlgTraining.dispose();
        }
        //COEUSDEV-860 - END
        }
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting() ){
            trainingInfoTableCellEditor.stopCellEditing();
            
            Object source = listSelectionEvent.getSource();
            int selectedRow = tblTrrainingInfo.getSelectedRow();
            if(selectedRow == -1){
                selectedRow = lastSelectedRow;
            }
            
            // If there are no rows found in the table then disable the comments
            if(tblTrrainingInfo.getRowCount()==0){
                txtArComments.setEditable(false);
                btnDelete.setEnabled(false);
            }else{
                //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
                //if(canEditable){
                if(canEditable && hasRights){
                    txtArComments.setEditable(true);
                    btnDelete.setEnabled(true);
                }else{
                    txtArComments.setEditable(false);
                }
            }
            
            if( (source.equals(trainingSelectionModel) )&& (selectedRow >= 0 ) &&
                    cvFormData!=null && cvFormData.size() > 0) {
                DepartmentPersonTrainingBean rowBean = null;
                
                //Setting text to last selected Row - Start
                rowBean = (DepartmentPersonTrainingBean) cvFormData.get(lastSelectedRow);
                // additional checking with bean null value with empty string is
                // required bcoz txtArComments.getText() will return EMPTY_STRING
                // if there are no contents in it but where as its corresponding bean value
                // will return null, which are not equal. If we don't check for this
                // condition it will save the record though nothing has been changed.
                if( !(rowBean.getComments() == null && txtArComments.getText().equals(EMPTY_STRING))){//COEUSDEV-860
                    if(!txtArComments.getText().equals(rowBean.getComments())) {
                        rowBean.setComments(txtArComments.getText());
                        modified = true;
                        //rowBean.setAw_TrainingNumber(rowBean.getTrainingNumber());
                        if(rowBean.getAcType()==null){
                            rowBean.setAcType(TypeConstants.UPDATE_RECORD);
                            
                        }
                    }
                    
                }
                lastSelectedRow = selectedRow;
                //Setting text to last selected Row - End
                rowBean = (DepartmentPersonTrainingBean) cvFormData.get(selectedRow);
                txtArComments.setText( (rowBean.getComments() == null) ? EMPTY_STRING : rowBean.getComments()) ;
                txtArComments.setCaretPosition(0);
                
                //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
                lblSpeciesValue.setText((rowBean.getSpeciesType() == null) ? EMPTY_STRING : rowBean.getSpeciesType());
                lblProcedureValue.setText((rowBean.getProcedureType() == null) ? EMPTY_STRING : rowBean.getProcedureType());
                //COEUSQA:3537 - End
            }
        }
    }
    
    public void focusGained(FocusEvent focusEvent) {
        //Sets the vertical scrollbar to indicate the selection
        jscrPnComments.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    
    public void focusLost(FocusEvent focusEvent) {
        //Removes the vertical scrollbar
        jscrPnComments.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
    
    
    public class TrainingInfoTableModel extends AbstractTableModel implements TableModel{
        
        private String colNames[] = {"Training","Date Requested","Date Submitted",
        "Date Acknowledged","Followup Date","Score"};
        private Class colClass[] = {String.class,String.class, String.class,String.class,
        String.class,String.class};
        TrainingInfoTableModel(){
        }
        
        public boolean isCellEditable(int row, int col){
            //modified by Nadh for enhancement to show traing info tab along with other tabs in person details form
            //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
            //Setting cell editable to false,when the person is opened in display mode.
//            if(moduleType=='D') {
            if(moduleType=='D' || functionType == 'D' || !hasRights) {
                return false;
            }else{
                return true;
            }
            
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public int getRowCount() {
            if(cvFormData==null){
                return 0;
            }else{
                return cvFormData.size();
            }
        }
        
        public void setData(CoeusVector cvFormData){
            cvFormData= cvFormData;
        }
        
        public Object getValueAt(int row, int column) {
            DepartmentPersonTrainingBean departmentPersonTrainingBean= (DepartmentPersonTrainingBean)cvFormData.get(row);
            switch (column) {
                case TRAINING_COLUMN:
                    int trainingCode = departmentPersonTrainingBean.getTrainingCode();
                    //Bug Fix Case Id 1848 Start
                    if(cvTrainingData!=null && cvTrainingData.size()>0){
                        CoeusVector filteredVector = cvTrainingData.filter(new Equals("code",""+trainingCode));
                        if(filteredVector!=null && filteredVector.size() > 0){
                            ComboBoxBean comboBoxBean = null;
                            comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            //                        return comboBoxBean.getDescription();
                            return comboBoxBean;
                        }else{
                            //                        return EMPTY_STRING;
                            return new ComboBoxBean("","");
                        }
                    }else{
                        return new ComboBoxBean("","");
                    }//Bug Fix Case Id 1848 End
                case DATE_REQUESTED_COLUMN:
                    return departmentPersonTrainingBean.getDateRequested();
                case DATE_SUBMITTED_COLUMN:
                    return departmentPersonTrainingBean.getDateSubmitted();
                case DATE_ACKNOWLEDGED_COLUMN:
                    return departmentPersonTrainingBean.getDateAcknowledged();
                case FOLLOWUP_DATE:
                    return departmentPersonTrainingBean.getFollowUpDate();
                case SCORE_COLUMN:
                    return departmentPersonTrainingBean.getScore();
                    
            }
            return EMPTY_STRING;
        }
        
        
        public void setValueAt(Object value, int row, int column){
            DepartmentPersonTrainingBean departmentPersonTrainingBean =
                    (DepartmentPersonTrainingBean)cvFormData.get(row);
            
            String strDate = EMPTY_STRING;
            String message = EMPTY_STRING;
            java.util.Date date = null;
            
            if(column==SCORE_COLUMN ){
                // if(value==null|| value.toString().trim().equals(EMPTY_STRING))return ;
                String oldScore = departmentPersonTrainingBean.getScore();
                if( (oldScore != null && oldScore.equals(value.toString())) ||
                        ( oldScore == null && value.toString().trim().equals(EMPTY_STRING))){
                    return ;
                }
                departmentPersonTrainingBean.setScore(value.toString());
                if(departmentPersonTrainingBean.getAcType()==null)departmentPersonTrainingBean.setAcType(TypeConstants.UPDATE_RECORD);//COEUSDEV-860
                modified = true;
                
            }else if(column == TRAINING_COLUMN){
                if(value==null || value.toString().equals(EMPTY_STRING)){
                    return ;
                }
                ComboBoxBean comboBoxBean = (ComboBoxBean)cvTrainingData.filter(new Equals("description", value.toString())).get(0);
                int trainingCode = Integer.parseInt(comboBoxBean.getCode());
                if( trainingCode != departmentPersonTrainingBean.getTrainingCode() ){
                    departmentPersonTrainingBean.setTrainingCode(trainingCode);
                    if(departmentPersonTrainingBean.getAcType()==null)departmentPersonTrainingBean.setAcType(TypeConstants.UPDATE_RECORD);//COEUSDEV-860
                    modified = true;
                }
            }else{
                if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                    //commented by ravi as modified is the boolean used to check for save
                    //required we should not set it to false once set to true till saving.
                    //                    modified = false;
                    switch(column){
                        case DATE_REQUESTED_COLUMN:
                            departmentPersonTrainingBean.setDateRequested(null);
                            break;
                        case DATE_SUBMITTED_COLUMN:
                            departmentPersonTrainingBean.setDateSubmitted(null);
                            break;
                        case DATE_ACKNOWLEDGED_COLUMN:
                            departmentPersonTrainingBean.setDateAcknowledged(null);
                            break;
                        case FOLLOWUP_DATE:
                            departmentPersonTrainingBean.setFollowUpDate(null);
                            break;
                    }
                    return ;
                }
                try{
                    strDate = dtUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                    strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if(strDate==null) {
                        throw new CoeusException();
                    }
                    date = dtFormat.parse(strDate.trim());
                }catch (ParseException parseException) {
                    parseException.printStackTrace();
                    message = coeusMessageResources.parseMessageKey(
                            INVALID_DATE);
                    CoeusOptionPane.showErrorDialog(message);
                    return ;
                } catch (CoeusException coeusException) {
                    message = coeusMessageResources.parseMessageKey(
                            INVALID_DATE);
                    CoeusOptionPane.showErrorDialog(message);
                    return ;
                }
                //used for COEUSDEV-860
                //When Feeds update training records the Date fields have Time Portion.
                //This sets the updated flag to true while comparing, so use this variable to hold the TimeLess Date values.
                Date trainDate = null;
                switch(column){
                    case DATE_REQUESTED_COLUMN:
                        trainDate = departmentPersonTrainingBean.getDateRequested();
                        trainDate = getDateWithoutTime(trainDate);
                        if(trainDate!=null && trainDate.equals(date)){
                            break;
                        }
                        departmentPersonTrainingBean.setDateRequested(new java.sql.Date(date.getTime()));
                        if(departmentPersonTrainingBean.getAcType()==null)departmentPersonTrainingBean.setAcType(TypeConstants.UPDATE_RECORD);//COEUSDEV-860
                        modified = true;
                        break;
                    case DATE_SUBMITTED_COLUMN:
                        trainDate = departmentPersonTrainingBean.getDateSubmitted();
                        trainDate = getDateWithoutTime(trainDate);
                        if(trainDate!=null && trainDate.equals(date)){
                            break;
                        }
                        departmentPersonTrainingBean.setDateSubmitted(new java.sql.Date(date.getTime()));
                        if(departmentPersonTrainingBean.getAcType()==null)departmentPersonTrainingBean.setAcType(TypeConstants.UPDATE_RECORD);//COEUSDEV-860
                        modified = true;
                        break;
                    case DATE_ACKNOWLEDGED_COLUMN:
                        trainDate = departmentPersonTrainingBean.getDateAcknowledged();
                        trainDate = getDateWithoutTime(trainDate);
                        if(trainDate!=null && trainDate.equals(date)){
                            break;
                        }
                        departmentPersonTrainingBean.setDateAcknowledged(new java.sql.Date(date.getTime()));
                        if(departmentPersonTrainingBean.getAcType()==null)departmentPersonTrainingBean.setAcType(TypeConstants.UPDATE_RECORD);//COEUSDEV-860
                        modified = true;
                        break;
                    case FOLLOWUP_DATE:
                        trainDate = departmentPersonTrainingBean.getFollowUpDate();
                        trainDate = getDateWithoutTime(trainDate);
                        if(trainDate!= null && trainDate.equals(date)){
                            break;
                        }
                        departmentPersonTrainingBean.setFollowUpDate(new java.sql.Date(date.getTime()));
                        if(departmentPersonTrainingBean.getAcType()==null)departmentPersonTrainingBean.setAcType(TypeConstants.UPDATE_RECORD);//COEUSDEV-860
                        modified = true;
                        break;
                }
            }
            //COEUSDEV-860 - START
            //if(departmentPersonTrainingBean.getAcType()==null){
            //    departmentPersonTrainingBean.setAcType(TypeConstants.UPDATE_RECORD);
            //}
            //COEUSDEV-860 - END
        }
    }
    
    
    public class TrainingInfoTableCellEditor extends AbstractCellEditor
            implements TableCellEditor{
        
        private CoeusComboBox cmbTraining;
        private JTextField txtDateComponent;
        private JTextField txtScore;
        private int column;
        private boolean populated = false;
        
        public TrainingInfoTableCellEditor(){
            cmbTraining = new CoeusComboBox();
            txtDateComponent = new JTextField();
            txtScore = new JTextField();
            txtScore.setDocument(new LimitedPlainDocument(9));
        }
        
        private void populateCombo() {
            ComboBoxBean comboBoxBean =null;
            //Bug Fix Case 1848 Start
            if(cvTrainingData!=null && cvTrainingData.size()>0){
                int size = cvTrainingData.size();
                //ComboBoxBean comboBoxBean;
                cmbTraining.addItem(new ComboBoxBean("",""));
                for(int index = 0; index < size; index++) {
                    comboBoxBean = (ComboBoxBean)cvTrainingData.get(index);
                    cmbTraining.addItem(comboBoxBean);
                }
            }else{
                cmbTraining.addItem(new ComboBoxBean("",""));
            }//Bug Fix Case 1848 End
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case DATE_ACKNOWLEDGED_COLUMN:
                case DATE_REQUESTED_COLUMN:
                case DATE_SUBMITTED_COLUMN:
                case FOLLOWUP_DATE:
                    if(value==null || value.toString().trim().equals(EMPTY_STRING)){
                        txtDateComponent.setText(EMPTY_STRING);
                    }else{
                        txtDateComponent.setText(dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT));
                    }
                    return txtDateComponent;
                case SCORE_COLUMN:
                    if(value==null || value.toString().trim().equals(EMPTY_STRING)){
                        txtScore.setText(EMPTY_STRING);
                    }else{
                        txtScore.setText(value.toString());
                    }
                    return txtScore;
                case TRAINING_COLUMN:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                    //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
                    cmbTraining.setSelectedItem(value);
                    //BugFix Case Id 1848 Start
                    //cmbTraining.setSelectedItem(value.toString());
                    //BugFix Case Id 1848 End
                    //Case#4346 - End
                    return cmbTraining;
            }
            return txtDateComponent;
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case DATE_ACKNOWLEDGED_COLUMN:
                case DATE_REQUESTED_COLUMN:
                case DATE_SUBMITTED_COLUMN:
                case FOLLOWUP_DATE:
                    return txtDateComponent.getText();
                case SCORE_COLUMN:
                    return txtScore.getText();
                case TRAINING_COLUMN:
                    return cmbTraining.getSelectedItem();
            }
            return ((JTextField)txtDateComponent).getText();
        }
        
        public int getClickCountToStart(){
            return 1;
        }
    }
    
    public class TrainingInfoTableCellRenderer extends DefaultTableCellRenderer{
        JTextField txtDateComponent;
        JTextField txtComponent;
        
        public TrainingInfoTableCellRenderer(){
            txtDateComponent = new JTextField();
            txtComponent = new JTextField();
            
            txtDateComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
                boolean isSelected, boolean hasFocus, int row, int column){
            switch(column){
                case DATE_ACKNOWLEDGED_COLUMN:
                case DATE_REQUESTED_COLUMN:
                case DATE_SUBMITTED_COLUMN:
                case FOLLOWUP_DATE:
                    if(isSelected ){
                        txtDateComponent.setBackground(Color.YELLOW);
                        txtDateComponent.setForeground(Color.black);
                    }else{
                        txtDateComponent.setBackground(Color.white);
                        txtDateComponent.setForeground(Color.black);
                    }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtDateComponent.setText(EMPTY_STRING);
                    }else{
                        value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                        txtDateComponent.setText(value.toString());
                    }
                    return txtDateComponent;
                case TRAINING_COLUMN:
                case SCORE_COLUMN:
                    if(isSelected){
                        txtComponent.setBackground(Color.YELLOW);
                        txtComponent.setForeground(Color.black);
                    }else{
                        txtComponent.setBackground(Color.white);
                        txtComponent.setForeground(Color.black);
                    }
                    if(value==null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
            }
            
            return txtDateComponent;
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblHeader = new javax.swing.JLabel();
        jscrPnTrainingInfo = new javax.swing.JScrollPane();
        tblTrrainingInfo = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        tblTrrainingInfo .dispatchEvent(new java.awt.event.KeyEvent(
                            tblTrrainingInfo ,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                }
            });
        }
    };
    btnOk = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();
    btnAdd = new javax.swing.JButton();
    btnDelete = new javax.swing.JButton();
    jscrPnComments = new javax.swing.JScrollPane();
    txtArComments = new javax.swing.JTextArea();
    pnlSpeciesProcedure = new javax.swing.JPanel();
    lblSpecies = new javax.swing.JLabel();
    lblSpeciesValue = new javax.swing.JLabel();
    lblProcedure = new javax.swing.JLabel();
    lblProcedureValue = new javax.swing.JLabel();

    setMinimumSize(new java.awt.Dimension(730, 420));
    setPreferredSize(new java.awt.Dimension(730, 420));
    setLayout(new java.awt.GridBagLayout());

    lblHeader.setFont(CoeusFontFactory.getLabelFont());
    lblHeader.setText("Training info for");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
    add(lblHeader, gridBagConstraints);

    jscrPnTrainingInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jscrPnTrainingInfo.setMinimumSize(new java.awt.Dimension(660, 290));
    jscrPnTrainingInfo.setPreferredSize(new java.awt.Dimension(660, 290));

    tblTrrainingInfo.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    tblTrrainingInfo.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tblTrrainingInfoMouseClicked(evt);
        }
    });
    jscrPnTrainingInfo.setViewportView(tblTrrainingInfo);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridheight = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    add(jscrPnTrainingInfo, gridBagConstraints);

    btnOk.setFont(CoeusFontFactory.getLabelFont());
    btnOk.setMnemonic('O');
    btnOk.setText("OK");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
    add(btnOk, gridBagConstraints);

    btnCancel.setFont(CoeusFontFactory.getLabelFont());
    btnCancel.setMnemonic('C');
    btnCancel.setText("Cancel");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
    add(btnCancel, gridBagConstraints);

    btnAdd.setFont(CoeusFontFactory.getLabelFont());
    btnAdd.setMnemonic('A');
    btnAdd.setText("Add");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
    add(btnAdd, gridBagConstraints);

    btnDelete.setFont(CoeusFontFactory.getLabelFont());
    btnDelete.setMnemonic('D');
    btnDelete.setText("Delete");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
    add(btnDelete, gridBagConstraints);

    jscrPnComments.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jscrPnComments.setMinimumSize(new java.awt.Dimension(660, 100));
    jscrPnComments.setPreferredSize(new java.awt.Dimension(660, 100));

    txtArComments.setFont(CoeusFontFactory.getNormalFont());
    txtArComments.setLineWrap(true);
    txtArComments.setWrapStyleWord(true);
    jscrPnComments.setViewportView(txtArComments);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
    add(jscrPnComments, gridBagConstraints);

    pnlSpeciesProcedure.setMinimumSize(new java.awt.Dimension(161, 25));
    pnlSpeciesProcedure.setPreferredSize(new java.awt.Dimension(161, 25));
    pnlSpeciesProcedure.setLayout(new java.awt.GridBagLayout());

    lblSpecies.setFont(CoeusFontFactory.getLabelFont());
    lblSpecies.setText("Species:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.ipadx = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
    pnlSpeciesProcedure.add(lblSpecies, gridBagConstraints);

    lblSpeciesValue.setFont(CoeusFontFactory.getLabelFont());
    lblSpeciesValue.setMaximumSize(new java.awt.Dimension(260, 14));
    lblSpeciesValue.setMinimumSize(new java.awt.Dimension(260, 14));
    lblSpeciesValue.setPreferredSize(new java.awt.Dimension(260, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlSpeciesProcedure.add(lblSpeciesValue, gridBagConstraints);

    lblProcedure.setFont(CoeusFontFactory.getLabelFont());
    lblProcedure.setText("Procedure:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 12;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 3);
    pnlSpeciesProcedure.add(lblProcedure, gridBagConstraints);

    lblProcedureValue.setFont(CoeusFontFactory.getLabelFont());
    lblProcedureValue.setMaximumSize(new java.awt.Dimension(240, 14));
    lblProcedureValue.setMinimumSize(new java.awt.Dimension(240, 14));
    lblProcedureValue.setPreferredSize(new java.awt.Dimension(240, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    pnlSpeciesProcedure.add(lblProcedureValue, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 7;
    // JM 1-15-2013 not using this feature
    //add(pnlSpeciesProcedure, gridBagConstraints);
    // JM END
    }// </editor-fold>//GEN-END:initComponents

    private void tblTrrainingInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTrrainingInfoMouseClicked
        // Add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                tblTrrainingInfo.dispatchEvent(new java.awt.event.KeyEvent(
                        tblTrrainingInfo,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                        java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
    }//GEN-LAST:event_tblTrrainingInfoMouseClicked
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnOk;
    public javax.swing.JScrollPane jscrPnComments;
    public javax.swing.JScrollPane jscrPnTrainingInfo;
    public javax.swing.JLabel lblHeader;
    public javax.swing.JLabel lblProcedure;
    public javax.swing.JLabel lblProcedureValue;
    public javax.swing.JLabel lblSpecies;
    public javax.swing.JLabel lblSpeciesValue;
    public javax.swing.JPanel pnlSpeciesProcedure;
    public javax.swing.JTable tblTrrainingInfo;
    public javax.swing.JTextArea txtArComments;
    // End of variables declaration//GEN-END:variables
 
    private Date getDateWithoutTime(Date date){
        Date modifiedDate = null;
        if (date != null) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(date);
            //Strip Time Portions
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
            modifiedDate = calendar.getTime();
        }
        return modifiedDate;
    }
}
