/*
 * @(#)CommitteeScheduleDetailsForm.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.gui;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.iacuc.bean.ScheduleDetailsBean;
import edu.mit.coeus.exception.*;

/**
 * <code> CommitteeScheduleDetailsForm </code> is a class for maintaining list
 * of all schedules. In this form user can generate schedules and add
 * schedules of his selection for a Committee. Maintenance of schedule can be
 * instantiated from this form.
 *
 * @author ravikanth
 * @version: 1.0 September 21, 2002
 */

public class CommitteeScheduleDetailsForm extends JComponent {
    
    // Variables declaration - do not modify
    private JRadioButton rdBtnAll;
    private JPanel pnlFields;
    private JButton btnDelete;
    private JRadioButton rdBtnAfter;
    private JButton btnRefresh;
    private JButton btnGenerate;
    private JButton btnMaintain;
    private JTable tblSchedule;
    private JButton btnAdd;
    private CoeusTextField txtAfter;
    private JScrollPane scrPnTable;
    private ButtonGroup btnGrpShow;
    private JLabel lblCommitteeSchedules;
    private boolean hasRightsForSchedules=true;
    // End of variables declaration
    
    /** used to store date value as string for formatting purpose */
    private String focusDate = new String();
    
    /** vector used to store collection of ScheduleDetailsBean */
    private Vector commScheduleData = new Vector(5,5);
    
    /** vector used to store collection of available status codes */
    private Vector statusCodes = new Vector();
    
    /** character used to specify the mode in which the form is opened */
    private char functionType;
    
    /** instance of DateUtils object which will be used for formatting dates */
    private DateUtils dtUtils = new DateUtils();
    
    /** connection string which specifies the path to server */
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    
    /** used to parse the dates which are represented as strings */
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    /** reference to mdiForm component */
    private CoeusAppletMDIForm mdiForm;
    
    /** used to store the committee id to which these schedules belongs. */
    private String committeeId;
    
    /** flag which specifies save to database is required or not. */
    private boolean saveRequired;
    
    /** integer value which will be used to calculate the protocol submission
     * deadline for a schedule. This value will be specified for a committee */
    private int advSubmissionDays;
    
    /** integer value which specifies the maximum number of protocols reviewed
     * in this schedule. By default this value is same as that of committee */
    private int maximumProtocols;
    
    /** reference variable of CoeusDlgWindow which will be used to show
     * ScheduleGenerateForm */
    private CoeusDlgWindow dlgGenerate ;
    
    /** reference object of CoeusMessageResources which will be used to get the
     * messages to be displayed to the user */
    CoeusMessageResources coeusMessageResources
    = CoeusMessageResources.getInstance();
    
    /**
     * Default Constructor
     */
    public CommitteeScheduleDetailsForm(){
    }
    
    /** Constructor with <CODE>functionType</CODE> and default data
     * @param fType <PRE>character used to specify the form opened mode.
     * 'A' used to inform that the Form is in <CODE>Add Schedules</CODE> mode.
     * 'M' used to inform that the Form is in <CODE>Modify Schedules</CODE> mode.
     * 'D' used to inform that the Form is in <CODE>Display Schedules</CODE> mode.</PRE>
     * @param commSchData Vector of <CODE>ScheduleDetailsBean</CODE> if the form is in
     * <CODE>Modify/Display</CODE> mode, null in <PRE>Add</PRE> mode
     */
    public CommitteeScheduleDetailsForm(char fType, Vector commSchData) {
        this.commScheduleData = commSchData;
        this.functionType = fType;
    }
    
    /** This method is used to initialize the components, returns the
     * reference of <CODE>CommitteeScheduleDetailsForm</CODE>.
     *
     * @param parent reference to <CODE>CoeusAppletMDIForm</CODE> component.
     * @return JComponent reference of <CODE>CommitteeScheduleDetailsForm</CODE> after
     * initializing and setting the data.
     */
    public JComponent getScheduleDetailsForm(CoeusAppletMDIForm parent){
        this.mdiForm = parent;
        initComponents();
        setTableEditors();
        formatFields();
        if( tblSchedule.getRowCount() > 0 ) {
            tblSchedule.setRowSelectionInterval(0,0);
        }else{
            btnDelete.setEnabled(false);
            btnMaintain.setEnabled(false);
        }
        // setting bold property for table header values
        tblSchedule.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            tblSchedule.setBackground(bgListColor);
            
        }
        else{
            tblSchedule.setBackground(Color.WHITE);
        }
        //End Amit
              
        return this;
    }
    
    /** This method is used to set the Committee Id.
     *
     * @param commId String representing Committee to which all these
     * schedules belongs to.
     */
    public void setCommitteeId(String commId){
        this.committeeId = commId;
        
    }
    
    /** This method is used to set the Advance Protocol Submission Days of the
     * Committee, which will be used to calculate the Protocol Submission Deadline
     * based on Schedule Date
     *
     * @param days integer representing Advance Protocol Sumission Days which
     * has been specified in <CODE>CommitteeMaintenanceForm</CODE> screen.
     */
    public void setAdvSubmissionDays(int days){
        this.advSubmissionDays = days;
    }
    
    
    /** This method is used to set the Maximum Protocols Reviewed in this
     * Schedule. By default it will be same as that of Committee.
     *
     * @param count integer representing Maximum Protocols Reviewed which
     * has been specified in <CODE>CommitteeMaintenanceForm</CODE> screen.
     */
    public void setMaximumProtocols(int count){
        this.maximumProtocols = count;
    }
    
    /** This method is used to set the values for  syncronization after saving
     * the information to the database
     *
     * @param schedules Collection of schedules for the selected Committee in
     * the form of <CODE>ScheduleDetailsBean</CODE>.
     */
    public void setValues(Vector schedules){
        
        int preSelRow = 0;
        int countPrev,countAfter;
        countPrev=tblSchedule.getRowCount();
        if( countPrev > 0 ) {
            preSelRow = tblSchedule.getSelectedRow();
        }
        
        formatFields();
        setCommScheduleData(schedules);
        setScheduleTableData();
        rdBtnAfter.setSelected(true);
        txtAfter.setEditable(true);
        
        countAfter= tblSchedule.getRowCount();
        if( countAfter > 0 ){
            if( countAfter > preSelRow ) {
                tblSchedule.setRowSelectionInterval(preSelRow, preSelRow);
            }else{
                tblSchedule.setRowSelectionInterval(0, 0);
            }
        }
    }
    
    /** This method is used to set the <CODE>functionType</CODE> for this form
     *
     * @param fType character which specifies the mode in which the form is
     * displayed.
     */
    public void setFunctionType(char fType){
        this.functionType = fType;
    }
    
    /**
     * This method is used to determine whether the data is to be saved or not.
     * @return boolean true if any modifications have been done and are not
     * saved, else it returns false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    /** This method is used to set whether the data is to be saved or not.
     * @param save boolean true if any modifications have been done and are not
     * saved, else  false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    public void setDefaultFocusForComponent(){
        if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
            if( tblSchedule.getRowCount() > 0 ) {
                int rowNum = tblSchedule.getSelectedRow();                
                tblSchedule.requestFocusInWindow();
                tblSchedule.setRowSelectionInterval(rowNum ,rowNum );
                tblSchedule.setColumnSelectionInterval(1,1);                
            }
            else{
                rdBtnAfter.requestFocus();
            }
        }else{
            if( tblSchedule.getRowCount() > 0 ) {
                int rowNum = tblSchedule.getSelectedRow();                
                tblSchedule.requestFocusInWindow();
                if(rowNum >0)
                    {
                      tblSchedule.setRowSelectionInterval(rowNum ,rowNum );
                }
                tblSchedule.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }
            
        }
    }
    /**
     * This method is used to set different editors to different columns for
     * editing.
     */
    private void setTableEditors(){
        TableColumn column = tblSchedule.getColumnModel().getColumn(0);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setPreferredWidth(30);
        column.setMaxWidth(30);
        column.setMinWidth(30);
        
        tblSchedule.setRowHeight(22);
        tblSchedule.setShowHorizontalLines(false);
        tblSchedule.setShowVerticalLines(false);
        tblSchedule.setOpaque(false);
        tblSchedule.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = tblSchedule.getTableHeader();
        header.setReorderingAllowed(false);
//        header.setResizingAllowed(false);
        
        /* dateEditor for Scheduled Date column */
        String columnName = ((DefaultTableModel)
        tblSchedule.getModel()).getColumnName(1);
        DateEditor dateCellEditor = new DateEditor(columnName);
        column = tblSchedule.getColumnModel().getColumn(1);
        column.setCellEditor(dateCellEditor);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        
        column = tblSchedule.getColumnModel().getColumn(2);
        column.setPreferredWidth(90);
        column.setMinWidth(90);        
        
        /* dateEditor for Protocol Submission deadline */
        columnName = ((DefaultTableModel)tblSchedule.getModel()).
        getColumnName(3);
        dateCellEditor = new DateEditor(columnName);
        column = tblSchedule.getColumnModel().getColumn(3);
        column.setCellEditor(dateCellEditor);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        
        /* setting CoeusComboBox as editor to Status Code column */
        if(statusCodes != null && statusCodes.size()>0){
            JComboBox  coeusCombo = new JComboBox(getStatusCodes());
            coeusCombo.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    saveRequired = true;
                }
            });
            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
            
            column = tblSchedule.getColumnModel().getColumn(4);
            //column.setPreferredWidth(90);
            column.setMinWidth(150);
            column.setCellEditor(new DefaultCellEditor(coeusCombo ));            
        }
        
        /* setting editor for Place Column */
        columnName = ((DefaultTableModel)tblSchedule.getModel()).
        getColumnName(5);
        dateCellEditor = new DateEditor(columnName);
        column = tblSchedule.getColumnModel().getColumn(5);
        column.setCellEditor(dateCellEditor);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        
        /* setting editor to validate time for Scheduled Time coloumn */
        columnName = ((DefaultTableModel)tblSchedule.getModel()).
        getColumnName(6);
        dateCellEditor = new DateEditor(columnName);
        column = tblSchedule.getColumnModel().getColumn(6);
        column.setCellEditor(dateCellEditor);
        //column.setPreferredWidth(150);
        column.setMinWidth(90);
        
        /* making the scheduleId Column as not visible */
        column = tblSchedule.getColumnModel().getColumn(7);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setCellEditor(dateCellEditor);
    }
    
    /** This method is used to get the Schedule Status descriptions
     * @return Collection of status descriptions.
     */
    public Vector getStatusCodes(){
        Vector descVector = new Vector();
        Vector statCodes = statusCodes;
        if(statCodes != null){
            int statusSize = statCodes.size();
            for(int index = 0; index < statusSize; index ++){
                descVector.addElement(((ComboBoxBean)
                statCodes.elementAt(index)).toString());
            }
        }
        return descVector;
    }
    
    /** This method sets the Schedule Status codes.
     * @param sCodes Collection of <CODE>ComboBoxBean</CODE>s which consists of status codes
     * and description.
     */
    
    public void setStatusCodes(Vector sCodes){
        this.statusCodes = sCodes;
    }
    
    /** This method is called from within the getScheduleDetailsForm() to
     * initialize the form.
     */
    private void initComponents() {
        Font labelFont = CoeusFontFactory.getLabelFont();
        Font normalFont = CoeusFontFactory.getNormalFont();
        GridBagConstraints gridBagConstraints;
        lblCommitteeSchedules = new JLabel();
        pnlFields = new JPanel();
        btnAdd = new JButton();
        btnDelete = new JButton();
        btnGenerate = new JButton();
        btnMaintain = new JButton();
        btnRefresh = new JButton();
        rdBtnAll = new JRadioButton();
        rdBtnAfter = new JRadioButton();
        txtAfter = new CoeusTextField();
        scrPnTable = new JScrollPane();
        //case 3517 start
//      tblSchedule = new JTable();
        tblSchedule = new JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                if(column == 4 || column == 7) return;
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        tblSchedule.dispatchEvent(new java.awt.event.KeyEvent(
                            tblSchedule,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    }
                });
            }
        };
        //case 3517 end  
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
        tblSchedule.setSelectionBackground( bgListColor );
        tblSchedule.setSelectionForeground( Color.black );

        }
        else{
        tblSchedule.setSelectionBackground( Color.white );
        tblSchedule.setSelectionForeground( Color.black );

        }
        //End Amit
        
        
        
        tblSchedule.setFont(normalFont);
        
        pnlFields.setLayout(new GridBagLayout());
        
        btnAdd.setFont(labelFont);
        btnAdd.setText("Add");
        btnAdd.setMnemonic('A');
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(authorizedToPerform()){
                    if (tblSchedule.getModel() instanceof DefaultTableModel) {
                        /* add a new row in table and make it as selected row */
                        ((DefaultTableModel) tblSchedule.getModel()).addRow(
                        new Object[]{"","","","",statusCodes.elementAt(0).
                        toString(),"","",""});
                        int newRowCount = tblSchedule.getRowCount();
                        btnDelete.setEnabled(true);
                        if(functionType == 'M'){
                            btnMaintain.setEnabled(true);
                        }
                        tblSchedule.getSelectionModel().setSelectionInterval(
                        newRowCount - 1, newRowCount - 1);
                        // scroll to the newly selected row
                        tblSchedule.scrollRectToVisible(tblSchedule.getCellRect(
                        newRowCount-1 ,0, true));
                        tblSchedule.requestFocusInWindow();
                        tblSchedule.editCellAt(newRowCount-1,1);
                        tblSchedule.getEditorComponent().requestFocusInWindow();
                        saveRequired = true;
                    }
                }
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 8, 0, 0);
        pnlFields.add(btnAdd, gridBagConstraints);
        
        btnDelete.setFont(labelFont);
        btnDelete.setText("Delete");
        btnDelete.setMnemonic('D');
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(authorizedToPerform()){
                int totalRows = tblSchedule.getRowCount();
                int row = tblSchedule.getSelectedRow();
                int col = tblSchedule.getSelectedColumn();
                if(row != -1 && col != -1){
                    /* check whether any cell is editing */
                    if (tblSchedule.isEditing()) {
                        /* If there is any cell in editing while the delete
                         button pressed save the editing value to table and
                         perform the appropriate action. */
                        String text = "";
                        if(col!=4){
                            text = ((javax.swing.text.JTextComponent)
                            tblSchedule.getEditorComponent()).getText();
                        }
                        else{
                            /* if the editing column element is  ComboBoxBean
                             then getValueAt returns ComboBoxBean so toString()
                             returns the descrpition of that particular element */
                            text = tblSchedule.getValueAt(row,col).toString();
                        }
                        tblSchedule.setValueAt(text, row,col);
                        tblSchedule.getCellEditor().cancelCellEditing();
                    }
                }
                
                /* If there are more than one row in table then delete it */
                if (totalRows > 0) {
                    /* get the selected row */
                    int selectedRow = tblSchedule.getSelectedRow();
                    if (selectedRow != -1) {
                        int selectedOption
                        = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                        "commSchdDetFrm_delConfirmCode.1025"),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                        // if Yes then selectedOption is 0
                        // if No then selectedOption is 1
                        if (0 == selectedOption) {
                            String schId = (String)tblSchedule.getValueAt(
                            selectedRow,7);
                            if(schId != null && schId.trim().length() > 0){
                                try{
                                    /* check whether there are any child records
                                       exists for the schedule going to be
                                       deleted */
                                    if(canDelete(schId)){
                                        ((DefaultTableModel)
                                        tblSchedule.getModel()
                                        ).removeRow(selectedRow);
                                        saveRequired = true;
                                    }
                                }catch (Exception e){
                                    CoeusOptionPane.showErrorDialog(e.getMessage());
                                }
                            }else{
                                ((DefaultTableModel)
                                tblSchedule.getModel()).removeRow(selectedRow);
                                saveRequired = true;
                            }
                            // find row count in table
                            int newRowCount = tblSchedule.getRowCount();
                            if(newRowCount == 0){
                                btnAdd.requestFocusInWindow();
                                btnDelete.setEnabled(false);
                                btnMaintain.setEnabled(false);
                            }
                            // select the next row if exists
                            if (newRowCount > selectedRow) {
                                (tblSchedule.getSelectionModel())
                                .setSelectionInterval(selectedRow,
                                selectedRow);
                            } else {
                                (tblSchedule.getSelectionModel())
                                .setSelectionInterval(newRowCount - 1,
                                newRowCount - 1);
                            }
                        }
                        
                    } // if total rows >0 and row is selected
                    else{
                        // if total rows >0 and row is not selected
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "commSchdDetFrm_exceptionCode.1026"));
                    }
                }
              }// end if auth check  
            }
        });
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(4, 8, 0, 0);
        pnlFields.add(btnDelete, gridBagConstraints);
        
        btnGenerate.setFont(labelFont);
        btnGenerate.setText("Generate");
        btnGenerate.setMnemonic('G');
        btnGenerate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(authorizedToPerform()){
                    dlgGenerate = new CoeusDlgWindow(mdiForm,"Generate Schedules",true);
                    
                /*creation and diplay of ScheduleGenerateForm for
                  generating new schedules */
                    ScheduleGenerateForm scheduleGenerateForm
                    = new ScheduleGenerateForm(dlgGenerate, mdiForm);
                    dlgGenerate.getContentPane().add(scheduleGenerateForm);
                  
                    /* displaying the window in center of the screen */
                    Dimension screenSize = Toolkit.getDefaultToolkit().
                    getScreenSize();
                    dlgGenerate.pack();
                    /* CASE# IRBEN00077 Begin */
                    dlgGenerate.setSize(new Dimension(512,200));
                    /* CASE# IRBEN00077 End */
                    /* CASE# IRBEN00077 Comment Begin */
                    /*dlgGenerate.setSize(new Dimension(500,200));*/
                    /* CASE# IRBEN00077 Comment End */
                    Dimension dlgSize = dlgGenerate.getSize();
                    dlgGenerate.setLocation(screenSize.width/2 - (dlgSize.width/2),
                    screenSize.height/2 - (dlgSize.height/2));
                    dlgGenerate.setResizable(false);
                    scheduleGenerateForm.requestDefaultFocusForComponent();
                    dlgGenerate.setVisible(true);
                    
                    /* getting the selected schedules which are generated */
                    Vector selectedSchedules =
                    scheduleGenerateForm.getSelectedSchedules();
                    boolean found = false;
                    if(selectedSchedules.size()>0){
                        
                        Enumeration enumSelectedSchedules =
                        selectedSchedules.elements();
                        while(enumSelectedSchedules.hasMoreElements()){
                            found = false;
                            Vector commSchGenerated =
                            (Vector)enumSelectedSchedules.nextElement();
                            String genScheduleDate
                            = commSchGenerated.elementAt(0).toString();
                            String genDayOfWeek
                            = commSchGenerated.elementAt(1).toString();
                            String genPlace
                            = commSchGenerated.elementAt(2).toString();
                            String genTime
                            = commSchGenerated.elementAt(3).toString();
                            try{
                                String stDate = dtUtils.restoreDate(genScheduleDate,
                                "/:-,");
                                if(!isDuplicateSchedule(genScheduleDate)){
                                    
                                /* selected schedule is not a duplicate schedule
                                   so convert the new schedule details into
                                   vector of objects and add that to the list of
                                   schedules. */
                                    
                                    Vector vecSchedule = new Vector();
                                    // add empty column for icon
                                    vecSchedule.addElement("");
                                    //add schedule date
                                    vecSchedule.addElement(genScheduleDate);
                                    
                                    // add day of week for the schedule date
                                    vecSchedule.addElement(genDayOfWeek);
                                    
                                    // generate protocol submission deadline date
                                    String genDeadline = computeDeadLine(stDate);
                                    genDeadline = dtUtils.formatDate(genDeadline,
                                    "/-:,","dd-MMM-yyyy");
                                    vecSchedule.addElement(genDeadline);
                                    
                                    // add the first schedule status
                                    String genStatusDesc = ((ComboBoxBean)
                                    statusCodes.elementAt(0)).toString();
                                    vecSchedule.addElement(genStatusDesc);
                                    
                                    //add generated schedule place
                                    vecSchedule.addElement(genPlace);
                                    
                                    //add generated schedule time
                                    vecSchedule.addElement(genTime);
                                    
                                /* empty element will be added to vector for
                                   scheduleId */
                                    vecSchedule.addElement("");
                                    
                                /* adding the new schedule to schedule details
                                   table */
                                    ((DefaultTableModel)
                                    tblSchedule.getModel()).addRow(vecSchedule);
                                    saveRequired = true;
                                    ((DefaultTableModel)
                                    tblSchedule.getModel()).
                                    fireTableDataChanged();
                                }
                            }catch(Exception e){
                                CoeusOptionPane.showInfoDialog(e.getMessage());
                            }
                        }
                        tblSchedule.requestFocusInWindow();
                        int newRowCount = tblSchedule.getRowCount();
                        // select the next row if exists
                        if (newRowCount > 0) {
                            btnDelete.setEnabled(true);
                            btnMaintain.setEnabled(true);
                            (tblSchedule.getSelectionModel()).setSelectionInterval(
                            newRowCount - 1, newRowCount - 1);
                            tblSchedule.scrollRectToVisible(tblSchedule.getCellRect(
                            newRowCount-1 ,0, true));
                        }
                        
                    }
                }
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(4, 8, 0, 0);
        pnlFields.add(btnGenerate, gridBagConstraints);
        
        btnMaintain.setFont(labelFont);
        btnMaintain.setText("Maintain");
        btnMaintain.setMnemonic('M');
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(4, 8, 0, 0);
        pnlFields.add(btnMaintain, gridBagConstraints);
        
        btnMaintain.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                int row = tblSchedule.getSelectedRow();
                try{
                    if(row != -1){
                        /* if a schedule is selected check whether it is modified
                           or not. if it is modified and not saved then prompt
                           for save confirmation */
                        if(isSaveRequired()){
                            int option = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            "saveConfirmCode.1002"),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                            if(option == JOptionPane.YES_OPTION){
                                
                            /* if user wishes to save the modified data, it will
                              saved to the database and the table is refreshed */
                                //prps added this if  - feb 6 2004
                                if (validateTableData())
                                {    
                                    saveData();
                                    refreshData();
                                } // end if prps - feb 6 2004
                                else
                                {
                                    CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey("memMntFrm_exceptionCode.1048")) ;
                                }    
                            }
                        }
                        if(!isSaveRequired()){
                            /* ScheduleMaintenanceForm is shown only for saved
                               schedules */
                            //bug fix for case #2051 by tarique start
                            if(tblSchedule.getRowCount() > 0){
                            //bug fix for case #2051 by tarique end
                                String scheduleId
                                = (String)tblSchedule.getValueAt(row,7);
                                if(hasRightsForSchedules){
                                    checkDuplicateAndShow('M', scheduleId);
                                }else{
                                    checkDuplicateAndShow('D', scheduleId);
                                }
                            }
                        }
                    }else{
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "commSchdDetFrm_exceptionCode.1026"));
                    }
                }catch(CoeusException ex){
                    CoeusOptionPane.showDialog(new CoeusClientException(ex));
                }catch (Exception ex){
                    CoeusOptionPane.showErrorDialog(ex.getMessage());
                }
            }
        });
        
        
        btnRefresh.setFont(labelFont);
        btnRefresh.setText("Refresh");
        btnRefresh.setMnemonic('R');
        btnRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                
                //raghuSV : code to stay the focus on the present row depending on modes.
                //starts...
                int selected=0,countBefore,countAfter;
                countBefore=tblSchedule.getRowCount();
                if(tblSchedule.getRowCount()>0){
                    selected=tblSchedule.getSelectedRow();
                }
                //ends
                
                performRefresh();
                
                //raghuSV
                //starts...
                countAfter=tblSchedule.getRowCount();
                if(tblSchedule.getRowCount()>0){
                    if(countBefore!=countAfter){
                        tblSchedule.setRowSelectionInterval(0, 0);
                     }else{
                        tblSchedule.setRowSelectionInterval(selected, selected);
                     }
                }
                //ends
            }   
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(4, 8, 0, 0);
        pnlFields.add(btnRefresh, gridBagConstraints);
        
        rdBtnAll.setFont(labelFont);
        rdBtnAll.setText("Show All");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlFields.add(rdBtnAll, gridBagConstraints);
        rdBtnAll.addActionListener(new RadioListener());
        
        rdBtnAfter.setFont(labelFont);
        rdBtnAfter.setText("Show After");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        pnlFields.add(rdBtnAfter, gridBagConstraints);
        
        rdBtnAfter.addActionListener(new RadioListener());
        rdBtnAfter.setSelected(true);
        btnGrpShow = new ButtonGroup();
        btnGrpShow.add(rdBtnAll);
        btnGrpShow.add(rdBtnAfter);
        txtAfter.setDocument(new LimitedPlainDocument(11));
        txtAfter.setFont(normalFont);
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.set(Calendar.DATE,gcal.get(Calendar.DATE)-60);
        focusDate = dtUtils.formatDate(
        (new java.sql.Timestamp(gcal.getTime().getTime())).toString(),
        "dd-MMM-yyyy");
        txtAfter.setText(focusDate);
        txtAfter.setPreferredSize(new Dimension(100,20));
        
        /* adding listener to textfield so as to maintain the date formats for
           display and editing modes */
        txtAfter.addFocusListener(new FocusAdapter() {
            boolean temporary = false;
            
            public void focusGained(FocusEvent fe){
                if ( (txtAfter.getText() != null)
                &&  (!txtAfter.getText().trim().equals("") &&
                !temporary) ) {
                    focusDate = dtUtils.restoreDate(txtAfter.getText(),"/-:,");
                    txtAfter.setText(focusDate);
                }
            }
            public void focusLost(FocusEvent fe){
                temporary = fe.isTemporary();
                if ( (txtAfter.getText() != null)
                &&  (!txtAfter.getText().trim().equals(""))
                && (!temporary)) {
                    String convertedDate = dtUtils.formatDate(
                    txtAfter.getText(),"/-:," , "dd-MMM-yyyy");
                    if (convertedDate==null){
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "memMntFrm_exceptionCode.1048"));
                        
                        txtAfter.requestFocus();
                        temporary = true;
                    }else {
                        focusDate = txtAfter.getText();
                        txtAfter.setText(convertedDate);
                    }
                }
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        pnlFields.add(txtAfter, gridBagConstraints);
        
        tblSchedule.setModel(new DefaultTableModel(new String[][]{},
        new String [] {
            "Icon","Schedule Date", "Day Of Week", "Deadline", "Status",
            "Place","Time","ScheduleId"
        }
        ){
            public boolean isCellEditable(int row, int col){
                if(functionType == 'D'){
                    
                    /* in display mode editing of  table fields will be disabled */
                    return false;
                }else if((col == 0) || (col == 2)){
                    
                    /* day of week column depends on schedule date,
                       so it is not editable. Icon column also should not
                       be editable */
                    return false;
                }else if(col == 3){
                    /* the protocol submission deadline column should not be
                       editable if the schedule date is not available */
                    String schDate = (String)getValueAt(row,1);
                    if( (schDate == null) || (schDate.length() == 0) ){
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        );
        tblSchedule.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        if(commScheduleData!=null){
            
            /* adding the available schedules to table for display purpose */
            setScheduleTableData();
        }
        scrPnTable.setViewportView(tblSchedule);
        scrPnTable.setPreferredSize(new Dimension(650,300));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        
        pnlFields.add(scrPnTable, gridBagConstraints);
        
        lblCommitteeSchedules.setFont(labelFont);
        lblCommitteeSchedules.setText("Schedules for this committee");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(4, 0, 10, 0);
        pnlFields.add(lblCommitteeSchedules, gridBagConstraints);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(pnlFields);
        // Added by Chandra
        java.awt.Component[] components = {tblSchedule,btnAdd,btnDelete,btnGenerate,btnMaintain,btnRefresh,rdBtnAfter,rdBtnAll};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlFields.setFocusTraversalPolicy(traversePolicy);
        pnlFields.setFocusCycleRoot(true);
        // Added by Chandra
        
        tblSchedule.addMouseListener(new MouseAdapter(){
            public void mouseReleased( MouseEvent mouseEvent ){
                int selectedRow = tblSchedule.getSelectedRow();
                
                if(mouseEvent.getClickCount() == 2 ){
                    String schId = (String)tblSchedule.getValueAt(selectedRow,7);
                    try{
                    if( schId != null  && schId.trim().length() > 0)  {                            
                            checkDuplicateAndShow('D', schId);
                    }
                    }
                    catch(Exception exc){
                    exc.printStackTrace();
                    }
                }
                
            }//end of moude released
        }//end of mouseadapter
        );
        
    }
    
    
    private boolean validateTableData()
    {
       if (tblSchedule != null)
        {
            for (int rowCount= 0 ; rowCount< tblSchedule.getRowCount() ; rowCount++)
            {
                String tempStr = (String) tblSchedule.getValueAt(rowCount, 1) ;
                if (tempStr != null)
                {    
                    if ( tempStr.trim().equals(""))
                    {
                        return false ;
                    }
                } 
                else
                {
                    return false ;
                }
            }   
        }    
        return true ;
    }
    
    
    private boolean authorizedToPerform(){
        String connectTo = connectionURL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setId(committeeId) ; //prps added this jan 16 2004
        request.setFunctionType('H'); 
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return true;
        }else{
            if(response.getDataObject() != null && (response.getDataObject() instanceof CoeusException)){
                CoeusOptionPane.showDialog(new CoeusClientException((CoeusException)response.getDataObject()));
            }else{
                CoeusOptionPane.showInfoDialog(response.getMessage());
            }
        }
        return false;
    }
    /** This method is used to refresh the data depending on the show option
     * selected, after getting user confirmation for saving the modified Schedules.
     */
    protected void performRefresh(){
        try{
            if(((DefaultTableModel)tblSchedule.getModel()).getRowCount()>0){
                
                int option = JOptionPane.NO_OPTION;
                if(functionType == 'M'){
                    if(isSaveRequired()){
                        option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                        if(option == JOptionPane.YES_OPTION){
                        /* if user wishes to save the modified data, it will
                           saved to the database.*/
                            saveData();
                        }else if( option == JOptionPane.NO_OPTION){
                            saveRequired = false;
                        }
                    }
                    else{
                        
                    }
                }
                if( option != JOptionPane.CANCEL_OPTION){
                    /* if option is either yes/no table data will be refreshed. */
                    refreshData();
                }
            }else{
                refreshData();
            }
        }catch(Exception ex){
            CoeusOptionPane.showErrorDialog(
            ex.getMessage());
        }
    }
    
    /**
     * This method sets the schedule tables data by converting the
     * ScheduleDetailsBean vector to vector of objects which table can accept.
     */
    private void setScheduleTableData(){
        Vector vecSchedules = new Vector(5,5);
        Vector vecSchedule = new Vector();
        Enumeration enumSchedules = commScheduleData.elements();
        while(enumSchedules.hasMoreElements()){
            vecSchedule = new Vector();
            
            /* get each schedule from available schedules vector and add to
               table */
            ScheduleDetailsBean schDetailsBean = (ScheduleDetailsBean)
            enumSchedules.nextElement();
            Date schDate = schDetailsBean.getScheduleDate();
            GregorianCalendar gcal = new GregorianCalendar();
            
            gcal.setTime(schDate);
            String dayOfWeek = getDayOfWeek(gcal.get(Calendar.DAY_OF_WEEK));
            vecSchedule.addElement("");
            vecSchedule.addElement(dtUtils.formatDate(
            schDetailsBean.getScheduleDate().toString(),"dd-MMM-yyyy"));
            vecSchedule.addElement(dayOfWeek);
            vecSchedule.addElement(dtUtils.formatDate(
            schDetailsBean.getProtocolSubDeadLine().toString(),
            "dd-MMM-yyyy"));
            vecSchedule.addElement(
            (schDetailsBean.getScheduleStatusDesc()==null)
            ? "" : schDetailsBean.getScheduleStatusDesc().toString());
            if(schDetailsBean.getScheduledPlace()!=null){
                vecSchedule.addElement(schDetailsBean.getScheduledPlace());
            }else{
                
                /* if schedule doesn't have value for scheduledPlace then add
                   empty string to table data */
                vecSchedule.addElement("");
            }
            if(schDetailsBean.getScheduledTime()!= null){
                
                /* convert the Time object to HH:mm format */
                GregorianCalendar gCal = new GregorianCalendar();
                gCal.setTime(schDetailsBean.getScheduledTime());
                String min = "";
                if(gCal.get(Calendar.MINUTE)<=9){
                    min = "0"+gCal.get(Calendar.MINUTE);
                }else{
                    min = ""+gCal.get(Calendar.MINUTE);
                }
                vecSchedule.addElement(gCal.get(Calendar.HOUR_OF_DAY)+":"+min);
            }else{
                
                /* if schedule doesn't have value for scheduledTime then add
                   empty string to table data */
                vecSchedule.addElement("");
            }
            vecSchedule.addElement(schDetailsBean.getScheduleId());
            vecSchedules.addElement(vecSchedule);
        }
        ((DefaultTableModel)tblSchedule.getModel()).setDataVector(vecSchedules,
        getColumnNames());
        setTableEditors();
        ((DefaultTableModel)tblSchedule.getModel()).fireTableDataChanged();
        if( tblSchedule.getRowCount() > 0 ) {
            //comeHere
            tblSchedule.setRowSelectionInterval(0,0);
            
            btnMaintain.setEnabled(true);
            if(functionType!='D'){
                btnDelete.setEnabled(true);
            }
        }else{
            btnMaintain.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }
    
    /**
     * This method is used to get the day of week in String format if it is
     * given the int between 1 - 7 . 1 represents Sunday, 7 represents Saturday.
     *
     * @param dayOfWeek integer which represents the actual day of week
     *
     * @return String Day of Week in String representation correspoding to
     * dayOfWeek.
     */
    private static String getDayOfWeek(int dayOfWeek){
        String day = "";
        switch (dayOfWeek) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day ="Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }
    
    /**
     * This method is used to check whether the given schedule already exists.
     *
     * @param schDate Schedule Date in the form of sql Date Object.
     *
     * @return boolean true if the given schedule is duplicate else false.
     *
     * @throws Exception with custom message specifying that the given schedule
     * is duplicate.
     */
    private boolean isDuplicateSchedule(String schDate) throws Exception{
        
        int row = tblSchedule.getSelectedRow();
        // loop through all the table rows and check
        // for duplicate schedule date
        for(int rowIndex=0; rowIndex < tblSchedule.getRowCount();rowIndex++){
            if(rowIndex != row){
                if(schDate.equals(tblSchedule.getValueAt(rowIndex,1).toString())){
                    return true;
                }
            }
        }
        
        String stDate = dtUtils.restoreDate(schDate,"/:-,");
        java.sql.Date scheduleDate = new java.sql.Date(dtFormat.parse(
        stDate).getTime());
        
        String connectTo = connectionURL + "/comMntServlet";
        // connect to the database and check for duplicate schedule
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType('J');
        request.setId(committeeId);
        request.setDataObject(scheduleDate);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                // schedule is not duplicate so return false.
                return false;
            }else{
                /* schedule is duplicate so return true  */
                return true;
            }
        }else{
            throw new Exception(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
    }
    
    
    
    /**
     * This method is used to check whether the given schedule has been used for
     * ProtocolSubmission or MinuteMaintenance etc.
     *
     * @param schId String representing Schedule Id.
     *
     * @return boolean true if the given schedule is not used anywhere else false.
     *
     * @throws Exception with custom message specifying where the given schedule
     * has been used.
     */
    private boolean canDelete(String schId) throws Exception{
        String connectTo = connectionURL + "/comMntServlet";
        // connect to the database and check for references
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType('C');
        request.setId(schId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                // schedule can be deleted. so return true.
                return true;
            }else{
                /* schedule has been used somewhere so throw exception with
                  the message sent by response which specifies where it has been
                  used */
                throw new Exception(response.getMessage());
            }
        }else{
            throw new Exception(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
    }
    
    /** This method returns the table column names used in Schedules table
     * @return vecColumnNames Collection of column names used in Schedules table
     */
    public Vector getColumnNames(){
        Enumeration enumColNames = tblSchedule.getColumnModel().getColumns();
        Vector vecColNames = new Vector(5,5);
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /**
     * <code> RadioListener </code> is an inner class for listening changes in
     * selection for ShowAll and ShowAfter RadioButtons.
     */
    class RadioListener implements ActionListener {
        /** This method is triggered when the selection changes between <CODE>Show All</CODE>
         * and <CODE>Show After</CODE> radio buttons.
         * @param ae <CODE>ActionEvent</CODE>, a semantic event which indicates that a
         * component-defined action occured.
         */
        public void actionPerformed(ActionEvent ae){
            if(ae.getSource() == rdBtnAll){
                /* if Show All radio button is selected disable the textAfter
                   field. */
                txtAfter.setEnabled(false);
            }
            else if(ae.getSource() == rdBtnAfter){
                /* if Show After radio button is selected enable the textAfter
                   field and change the date format from DD-MMM-YYYY to
                   mm/dd/yyyy */
                txtAfter.setEnabled(true);
                if((txtAfter.getText() == null)
                || (txtAfter.getText().trim().length()==0)){
                    // change the format only when there is data in the text field
                    GregorianCalendar gcal = new GregorianCalendar();
                    gcal.set(Calendar.DATE,gcal.get(Calendar.DATE)-60);
                    txtAfter.setText(dtUtils.formatDate(
                    (new Date(gcal.getTime().getTime())).toString(),
                    "/:-,","dd-MMM-yyyy"));
                }
                txtAfter.requestFocus();
            }
        }
    }
    
    /** This is used to set the default data to be populated to the schedule
     * details table
     * @param commSchData collection of <CODE>ScheduleDetailsBean</CODE> used to show in the
     * schedule details table.
     */
    
    public void setCommScheduleData(Vector commSchData){
        this.commScheduleData = commSchData;
    }
    
    /** This method returns the schedules to be saved in the database for this
     * Committee. This method determines the database operation to be performed
     * to each <CODE>ScheduleDetailsBean</CODE> and sets the <CODE>AcType</CODE> accordingly depending on
     * the <CODE>Add/Modify/Delete</CODE> operations performed on a Schedule.
     *
     * @return Collection of <CODE>ScheduleDetailsBean</CODE>.
     * @throws Exception if unable to get the schedule details
     */
    public Vector getCommScheduleData() throws Exception{
        if(tblSchedule.isEditing()){
            tblSchedule.getCellEditor().stopCellEditing();
        }
        Vector tableData = ((DefaultTableModel)
        tblSchedule.getModel()).getDataVector();
        Enumeration enumTableData = tableData.elements();
        boolean found =false;
        Vector modifiedSchedules = new Vector();
        if(commScheduleData == null){
            
            /* if functionType = 'A' then the commScheduleData will be null
             so create an vector object */
            commScheduleData = new Vector();
        }
        
        /* loop through all the table rows and check whether corresponding
           schedule is available. If available check whether it is actually
           modified then set AcType as 'U'. If corresponding schedule is not
           available in available schedules vector then set AcType to 'I'.
           Afterwards loop through all the available schedules and check whether
           all available schedules are present in schedules displayed table
          if not present set AcType to 'D' */
        while(enumTableData.hasMoreElements()){
            Vector tableRow = (Vector)enumTableData.nextElement();
            found = false;
            
            /* if table row is new one and there is no data in it ignore it. */
            if(tableRow.elementAt(1).toString().length()>0){
                int schedulesSize = commScheduleData.size();
                for(int scheduleIndex=0;scheduleIndex< schedulesSize;
                scheduleIndex++){
                    
                    /* getting ScheduleDetailsBean from selected schedules
                       vector */
                    ScheduleDetailsBean schDetBean = (ScheduleDetailsBean)
                    commScheduleData.elementAt(scheduleIndex);
                    
                    /* checking whether schedule is present or deleted */
                    if(schDetBean.getScheduleId().equals(
                    tableRow.elementAt(7).toString())){
                        found = true;
                        boolean modified = false;
                        
                        /* if present check whether it has been really
                           modified by checking all the fields in the bean  */
                        if(!(dtUtils.formatDate(
                        schDetBean.getScheduleDate().toString(),
                        "dd-MMM-yyyy")).equals(
                        tableRow.elementAt(1).toString())){
                            modified = true;
                        }else if(!(dtUtils.formatDate(
                        schDetBean.getProtocolSubDeadLine().toString(),
                        "dd-MMM-yyyy")).equals(
                        tableRow.elementAt(3).toString())){
                            modified = true;
                        }else if((schDetBean.getScheduledPlace() == null)
                        || (!schDetBean.getScheduledPlace().equals(
                        tableRow.elementAt(5).toString()))){
                            modified = true;
                        }else{
                            String time="";
                            if(schDetBean.getScheduledTime()!=null){
                                GregorianCalendar gCal =new GregorianCalendar();
                                gCal.setTime(schDetBean.getScheduledTime());
                                String min = "";
                                if(gCal.get(Calendar.MINUTE)<=9){
                                    min = "0"+gCal.get(Calendar.MINUTE);
                                }else{
                                    min = ""+gCal.get(Calendar.MINUTE);
                                }
                                time = gCal.get(Calendar.HOUR_OF_DAY)+":"+min;
                            }
                            String desc = "";
                            /* get the status code for the selected status
                               description for the schedule */
                            for(int codeIndex=0;codeIndex<statusCodes.size();
                            codeIndex++){
                                ComboBoxBean sCode = (ComboBoxBean)
                                statusCodes.elementAt(codeIndex);
                                if(Integer.parseInt(sCode.getCode())
                                == schDetBean.getScheduleStatusCode()){
                                    desc = sCode.getDescription();
                                    break;
                                }
                            }
                            // removed null checking for schedule time as it was not necessary to mark as
                            // modified if there is no schedule time.
                            if( (!time.trim().equals(
                            tableRow.elementAt(6).toString().trim()))
                            || (!desc.trim().equals(
                            tableRow.elementAt(4).toString().trim()))){
                                /* if schedule time or schedule place is modified
                                   then mark the schedule is modified. */
                                modified = true;
                            }
                        }
                        if(modified){
                            
                            /* modified, so mark it as to update and send to
                               server for updation */
                            ScheduleDetailsBean schBean
                            = constructBeanFromTableRow(tableRow);
                            schBean.setScheduleId(schDetBean.getScheduleId());
                            schBean.setCommitteeId(schDetBean.getCommitteeId());
                            schBean.setUpdateUser(schDetBean.getUpdateUser());
                            schBean.setUpdateTimestamp(
                            schDetBean.getUpdateTimestamp());
                            schBean.setAcType("U");
                            modifiedSchedules.addElement(schBean);
                        }
                        break;
                    }
                }
            }else{
                found = true;
            }
            if(!found){
                
                /* schedule is new one take the table column values and prepare
                   a ScheudleDetailBean and add it to the schedules list */
                ScheduleDetailsBean schDetailsBean
                = constructBeanFromTableRow(tableRow);
                
                /* setting the AcType to I- which indicates that the schedule
                   is a new one and should be inserted in the database table */
                schDetailsBean.setAcType("I");
                
                schDetailsBean.setCommitteeId(
                (committeeId == null ? "" : committeeId));
                schDetailsBean.setMaxProtocols(maximumProtocols);
                modifiedSchedules.addElement(schDetailsBean);
            }
            
        }
        
        /* loop through the available schedules list and find out the deleted
           schedules and set the AcType to 'D' */
        if(commScheduleData!= null){
            Enumeration enumScheduleData =commScheduleData.elements();
            found =false;
            /* for every element in available schedules list loop through all
               the elements in table and check whether the schedule is present
               or deleted */
            while(enumScheduleData.hasMoreElements()){
                ScheduleDetailsBean schDetBean = new ScheduleDetailsBean();
                schDetBean =(ScheduleDetailsBean)enumScheduleData.nextElement();
                found = false;
                for(int scheduleIndex=0;scheduleIndex<tableData.size();
                scheduleIndex++){
                    Vector schedule =(Vector)tableData.elementAt(scheduleIndex);
                    
                    /* checking whether schedules is present or deleted */
                    if(schDetBean.getScheduleId().equals(
                    schedule.elementAt(7).toString())){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    
                    /* schedule is deleted one */
                    schDetBean.setAcType("D");
                    modifiedSchedules.addElement(schDetBean);
                }
            }
            
        }
        return modifiedSchedules;
    }
    
    /**
     * This method is used to construct ScheduleDeteailsBean from a vector whose
     * elements are used as columns in displayed schedules table of this form
     *
     * @param tableRow Vector consisting of values of all columns of schedule
     * details table.
     *
     * ScheduleListDisplayForm table or of displayed Schedules table of this form.
     * @return ScheduleDetailsBean with all values set according to table data.
     * @throws Exception if unable to construct the bean properly.
     */
    private ScheduleDetailsBean constructBeanFromTableRow(Vector tableRow)
    throws Exception {
        int statusCode = 1;
        String stDate = tableRow.elementAt(1).toString();
        String dDate = tableRow.elementAt(3).toString();
        String status = tableRow.elementAt(4).toString();
        String place = tableRow.elementAt(5).toString();
        String time = tableRow.elementAt(6).toString();
        int statusCodesSize = statusCodes.size();
        Vector tempStatusCodes = statusCodes;
        for(int codeIndex=0; codeIndex < statusCodesSize; codeIndex++){
            ComboBoxBean sCode = (ComboBoxBean)
            tempStatusCodes.elementAt(codeIndex);
            if(sCode.getDescription().equals(status)){
                statusCode = Integer.parseInt(sCode.getCode());
                break;
            }
        }
        
        ScheduleDetailsBean schDetailsBean = new ScheduleDetailsBean();
        stDate = dtUtils.restoreDate(stDate,"/:-,");
        schDetailsBean.setScheduleDate(new java.sql.Date(
        dtFormat.parse(stDate).getTime()));
        
        if( dDate != null && dDate.trim().length() >0 ) {
            /* if deadline date is available in the table values then take
               that value */
            dDate = dtUtils.restoreDate(dDate,"/:-,");
            
            schDetailsBean.setProtocolSubDeadLine(new java.sql.Date(
            dtFormat.parse(dDate).getTime()));
        }else{
            /* if deadline date is not available then generate the protocol
               submission dead line date from schedule date */
            
            schDetailsBean.setProtocolSubDeadLine(
            new java.sql.Date((dtFormat.parse(
            computeDeadLine(stDate))).getTime()));
        }
        schDetailsBean.setScheduleStatusCode(statusCode);
        schDetailsBean.setScheduledPlace(place);
        if(time.length()>0){
            schDetailsBean.setScheduledTime(java.sql.Time.valueOf(time+":00"));
        }
        return schDetailsBean;
        
    }
    /**
     * This method is used to compute the protocol submission deadline if the
     * scheduled date is given.
     * @param startDate String representing start date in MM/dd/yyyy format.
     * @return String Protocol submission deadline in MM/dd/yyyy format.
     */
    private String computeDeadLine(String startDate){
        Date sDate = dtFormat.parse(startDate,new java.text.ParsePosition(0));
        
        /* create a GregorianCalendar object with our start date and
          deduct advSubmissionDays days from it and set it as deadline date */
        GregorianCalendar genDate = new GregorianCalendar();
        genDate.clear();
        genDate.setTime(sDate);
        genDate.set(Calendar.DATE,genDate.get(Calendar.DATE)-advSubmissionDays);
        String deadLine = (genDate.get(Calendar.MONTH)+1)+"/"
        +genDate.get(Calendar.DATE)+"/"+(genDate.get(Calendar.YEAR));
        return deadLine;
    }
    
    /** This method is used to save the displayed schedule details to the
     * database.
     * @throws Exception  with custom message when the response status is false
     * or when the response is null.
     */
    public void saveData() throws Exception{
        commScheduleData = getCommScheduleData();
        String connectTo = connectionURL + "/comMntServlet";
        
        // connect to the database and save the schedules
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType('U');
        request.setId(committeeId);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
        Vector vecParam = new Vector();
        vecParam.add(new Integer(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE)); 
        request.setDataObjects(vecParam);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        if(commScheduleData!=null && commScheduleData.size()>0){
            request.setDataObject(commScheduleData);
            AppletServletCommunicator comm =
            new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    saveRequired = false;
                }else{
                    if(response.getDataObject() != null){
                        throw (CoeusException)response.getDataObject();
                    }else{
                        throw new Exception(response.getMessage());
                    }
                }
            }else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                "server_exceptionCode.1000"));
            }
        }else{
            saveRequired = false;
        }
        
    }
    
    /** Set enabled or disabled for form controls as per the <CODE>functionType</CODE>
     * with which the form is activated.
     */
    public void formatFields() {
        switch (functionType){
            // enable/disable status for components in Add mode.
            case 'A':
                btnAdd.setEnabled(true);
                btnDelete.setEnabled(true);
                btnGenerate.setEnabled(true);
                btnMaintain.setEnabled(false);
                btnRefresh.setEnabled(false);
                rdBtnAll.setEnabled(false);
                rdBtnAfter.setEnabled(false);
                txtAfter.setEditable(false);
                break;
                
            case 'M':
                btnAdd.setEnabled(true);
                btnDelete.setEnabled(true);
                btnGenerate.setEnabled(true);
                btnMaintain.setEnabled(true);
                btnRefresh.setEnabled(true);
                rdBtnAll.setEnabled(true);
                rdBtnAfter.setEnabled(true);
                txtAfter.setEditable(true);
                break;
            case 'D':
                // enable/disable status for components in Display mode.
                btnAdd.setEnabled(false);
                btnDelete.setEnabled(false);
                btnGenerate.setEnabled(false);
                btnMaintain.setEnabled(true);
                btnRefresh.setEnabled(true);
                rdBtnAll.setEnabled(true);
                rdBtnAfter.setEnabled(true);
                txtAfter.setEditable(true);
                if(hasRightsForSchedules){
                    btnMaintain.setText("Maintain");
                    btnMaintain.setMnemonic('M');
                    btnMaintain.setEnabled(true);
                }
                else{
                    btnMaintain.setText("Display");
                    btnMaintain.setMnemonic('D');
                    btnMaintain.setEnabled(true);
                }
                 break;
                }
        }
        
    //}
    /** This function is used to refresh the schedule details list by fetching
     * the values for displayed Schedules from the database.
     * @throws Exception when the response is null or the response status is not
     * successful
     */
    public void refreshData() throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setId(committeeId);
        java.sql.Date dtFrom = null;
        boolean datePresent = true;
        if(rdBtnAll.isSelected()){
            
            /* Show All is selected so set functionType to 'O' so that it
               retrieves all shcedules after refreshing. */
            request.setFunctionType('O');
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
            Vector vecParam = new Vector();
            vecParam.add(new Integer(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE));
            request.setDataObjects(vecParam);
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        }else{
            
            /* show after is selected so check for validity specified date. */
            if((txtAfter.getText()!= null)
            && (txtAfter.getText().trim().length()>0)){
                String stDate = dtUtils.restoreDate(txtAfter.getText(),"/:-,");
                dtFrom = new java.sql.Date((dtFormat.parse(stDate)).getTime());
            }else{
                
                /* If date is not specified, prompt user for valid date entry */
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(
                "commSchdDetFrm_exceptionCode.1027"));
                txtAfter.requestFocus();
                datePresent = false;
            }
            
            /* To get all the schedules for this committee on or after
               specified date */
            request.setFunctionType('Q');
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
            Vector vecParam = new Vector();
            vecParam.add(new Integer(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE));
            request.setDataObjects(vecParam);
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        }
        if(datePresent){
            request.setDataObject(dtFrom);
            AppletServletCommunicator comm =
            new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    commScheduleData = (Vector)response.getDataObject();
                    setScheduleTableData();
                }else{
                    throw new Exception(response.getMessage());
                }
            }else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                "server_exceptionCode.1000"));
            }
        }
        
    }
    /**
     * This method is used to create and show ScheduleDetailsForm for
     * schedule maintenance.
     *
     * @param fType char which specifies the mode in which the form will be
     * opened.
     *
     * @param scheduleId String representing the schedule going to be maintained.
     *
     * @throws Exception with custom message when trying to open a locked
     * schedule row.
     */
    private void checkDuplicateAndShow(char fType, String scheduleId)
    throws Exception{
        
        //this.functionType = fType;
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(
            CoeusGuiConstants.SCHEDULE_DETAILS_TITLE,scheduleId,fType);
        }catch(Exception e){
            /* Exception occured.  Record may be already opened in requested mode
               or if the requested mode is edit mode and application is already
               editing any other record. */
            duplicate = true;
            if(e.getMessage().length() > 0){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            /* try to get the requested frame which is already opened */
            CoeusInternalFrame frame = mdiForm.getFrame(
            CoeusGuiConstants.SCHEDULE_DETAILS_TITLE,scheduleId);
            if(frame == null){
                /* if no frame opened for the requested record then the
                   requested mode is edit mode. So get the frame of the
                   editing record. */
                frame = mdiForm.getEditingFrame(
                CoeusGuiConstants.SCHEDULE_DETAILS_TITLE );
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
            
        }
        ScheduleDetailsForm scheduleDetailsForm =
        new ScheduleDetailsForm(fType,scheduleId,mdiForm);
        scheduleDetailsForm.setScheduleStatus(statusCodes);
        scheduleDetailsForm.setCommitteeScheduleDetailsForm(this);
        try{
            scheduleDetailsForm.showScheduleDetailForm();
        }catch(Exception ex){
            if ((fType=='D' || fType=='M') && scheduleDetailsForm.isLocked() ) {
                String msg = coeusMessageResources.parseMessageKey(
                "schedule_row_clocked_exceptionCode.555555");
                int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if (resultConfirm == JOptionPane.YES_OPTION) {
                    checkDuplicateAndShow('D',scheduleId);
                }
                //ex.printStackTrace();
            }else{
                //ex.printStackTrace();
                throw ex;
            }
        }
    }
    
/*
 * Inner class to set the editor for date columns/cells.
 */
    class DateEditor extends DefaultCellEditor implements TableCellEditor {
        
        /* string which holds the name of the column. It will be used while
           showing error message */
        private String colName;
        
        /* String representation of separaters that classify the
           date into day, month and Year.Ex( '/', '-', ',' etc..) */
        private static final String DATE_SEPARATERS = ":/.,|-";
        
        /* String representing the required date format */
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        
        /* reference of CoeusTextField which will be used for editing */
        private CoeusTextField dateComponent = new CoeusTextField();
        
        /* string which holds the value in the textfield at the time of focus
           gained */
        String oldData = "";
        int col = 1;
        int row = 0;
        boolean temporary;
        
        DateEditor(String colName) {
            super(new CoeusTextField());
            dateComponent.setDocument(new LimitedPlainDocument(200));
            dateComponent.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){
                    /* when the editor component loses the focus then call
                       stopCellEditing if there is any data in the editor */
                    dateComponent = (CoeusTextField)fe.getSource();
                    if ( (dateComponent.getText() != null)
                    &&  (!dateComponent.getText().trim().equals(""))){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }else{
                        TableCellEditor editor =  tblSchedule.getCellEditor();
                        if(editor != null ){
                            editor.cancelCellEditing();
                        }
                    }
                }
            });
            
            this.colName = colName;
        }
        
        /**
         * An overridden method to set the editor component in a cell.
         * @param table - the JTable that is asking the editor to edit; can be
         * null
         * @param value - the value of the cell to be edited; it is up to the
         * specific editor to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered as
         * a string or it could be rendered as a check box that is checked.
         * null is a valid value
         * @param isSelected - true if the cell is to be rendered with
         * highlighting
         * @param selRow - the row of the cell being edited
         * @param column - the column of the cell being edited
         * @return the component for editing
         */
        public Component getTableCellEditorComponent(JTable table,Object value,
        boolean isSelected,int selRow,int column){
            
            JTextField tfield =(JTextField)dateComponent;
            String currentValue = (String)value;
            col = column;
            this.row = selRow;
            if( ( currentValue != null  )
            && (currentValue.trim().length()!= 0) ){
               
                oldData = tfield.getText();
                if((col == 1) || (col == 3)){
                    /* if the editing column is the date column then restore the
                       date for editing. */
                    String newValue = new DateUtils().restoreDate(currentValue,
                    DATE_SEPARATERS) ;
                   tfield.setText(newValue);
                    oldData = newValue;
                    
                    return dateComponent;
                }else{
                    tfield.setText(currentValue);
                }
            }else{
                tfield.setText("");
            }
            
            return dateComponent;
        }
        
        /**
         * This method returns the mouse click counts after which the editor
         * should be invoked
         * @return int mouse click counts after which editor will be invoked
         */
        public int getClickCountToStart(){
            return 2;
        }
        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            /* before showing any custom message if the entered text is not valid
               set the temporary flag to true so that focus lost event won't fire
               when the text field loses focus temporarily */
            try {
                String editingValue = (String)getCellEditorValue();
                if(col == 6){
                    
                    /* validating time column. as data to this column is not
                       mandatory. we will allow him to keep this column blank */
                    if(editingValue!=null){
                        if(editingValue.trim().length()>0){
                            String strTime = dtUtils.formatTime(editingValue);
                            if(null == strTime){
                                /* if the entered time value is not a valid time
                                   show error message to the user */
                                temporary = true;
                                //CoeusOptionPane.showErrorDialog(
                                //"Please enter a valid "+colName);
                                
                                //raghuSV: to show the message rightly
                                CoeusOptionPane.showErrorDialog( coeusMessageResources.parseMessageKey(
                                        "schdGenFrm_exceptionCode.1085"));
                                //end
                                cancelCellEditing();
                                return false;
                            }else{
                                /* if user enters valid time set it to that
                                   column and saveRequired status to true */
                                temporary = true;
                                ((JTextField)dateComponent).setText(strTime);
                                if(!oldData.equals(strTime)){
                                    saveRequired = true;
                                }
                            }
                        }else{
                            /* if there is no data in the time column then
                               check whether user has deleted exising data. If
                               so change the saveRequired status to true */
                            if((oldData!=null) && (oldData.trim().length()>0)){
                                saveRequired = true;
                            }
                            ((JTextField)dateComponent).setText("");
                            
                        }
                        
                    }
                }else if(col == 5){
                    
                    /* if editing column is Place Column check whether user has
                       modified any value and set saveRequired flag accordingly */
                    if((editingValue == null) && (oldData!=null)
                    && (oldData.trim().length()>0)){
                        saveRequired = true;
                        ((JTextField)dateComponent).setText("");
                    }else{
                        if(!oldData.equals(editingValue)){
                            saveRequired = true;
                            ((JTextField)dateComponent).setText(editingValue);
                        }
                    }
                }else{
                    
                    /* editing column is  date column */
                    if( (editingValue != null )
                    && (editingValue.trim().length() > 0 )  ){
                        
                        /* if there is any content in schedule date column check
                           for validity of that date */
                        String formattedDate = new DateUtils().formatDate(
                        editingValue,DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                        
                        
                        if(null == formattedDate ) {
                            temporary = true;
                            CoeusOptionPane.showErrorDialog(
                            "Please enter a Valid "+colName);
                            dateComponent.setText("");
                            cancelCellEditing();
                            
                            int rowNumber = tblSchedule.getSelectedRow();   
                            int colNumber = tblSchedule.getSelectedColumn();
                            
                            if(rowNumber != -1){
                                tblSchedule.requestFocusInWindow();
                                tblSchedule.setRowSelectionInterval(rowNumber ,rowNumber );
                                tblSchedule.setColumnSelectionInterval(colNumber ,colNumber);                                
                            }
                            
                            return false;
                        }else{
                            if(col == 1){
                                try{
                                    
                                    /* after editing check whether new schedule date
                                       already exists. If exists prompt user to
                                       change the schedule date. */
                                    String schDate = dtUtils.restoreDate(
                                    formattedDate,"/:-,");
                                    java.sql.Date newSchDate = new java.sql.Date(
                                    dtFormat.parse(schDate).getTime());
                                    
                                    //raghuSV : to check if the value is modified
                                    //starts...
                                    if( oldData.equalsIgnoreCase(editingValue.toString()) ){
                                            cancelCellEditing();
                                            return false;
                                     }
                                    //ends
                                    
                                    if(!isDuplicateSchedule(formattedDate)){
                                        
                                        /* if entered date is not already present then
                                           change the format to display and compute the
                                           protocol submission deadline and update
                                           deadline column value. */
                                        
                                            
                                        
                                            
                                        temporary = true;
                                        ((JTextField)dateComponent).
                                        setText(formattedDate);
                                        
                                        if(!oldData.equals(formattedDate)){
                                            saveRequired = true;
                                        }
                                        Date scheduleDate =
                                        dtFormat.parse(schDate);
                                        GregorianCalendar gcal =new GregorianCalendar();
                                        gcal.setTime(scheduleDate);
                                        String dayOfWeek = getDayOfWeek(gcal.get(
                                        Calendar.DAY_OF_WEEK));
                                        ((DefaultTableModel)
                                        tblSchedule.getModel()).
                                        setValueAt(dayOfWeek,row,col+1);
                                        String oldDeadline
                                        = (String)tblSchedule.getValueAt(row,3);
                                        if( (oldDeadline== null)
                                        || (oldDeadline.length() == 0)){
                                        /* compute deadline from schedule date if
                                           the user doesn't specify explicitly */
                                            String deadLine =
                                            computeDeadLine(dtUtils.restoreDate(
                                            formattedDate,"/:-,"));
                                            ((DefaultTableModel)
                                            tblSchedule.getModel()).setValueAt(
                                            dtUtils.formatDate(deadLine,
                                            "/:-,","dd-MMM-yyyy"),row,col+2);
                                        }else{
                                            /* if the deadline date is available,
                                               check whether it is on or before
                                               schedule date. If it is above the
                                               schedule date compute deadline date
                                               for that schedule and replace it */
                                            Date sDate = dtFormat.parse(schDate);
                                            String deadLineDate = dtUtils.restoreDate(
                                            oldDeadline,"/:-,");
                                            Date dDate = dtFormat.parse(deadLineDate);
                                            if(dDate.after(sDate)){
                                                String deadLine = computeDeadLine(
                                                dtUtils.restoreDate(
                                                formattedDate,"/:-,"));
                                                ((DefaultTableModel)
                                                tblSchedule.getModel()).setValueAt(
                                                dtUtils.formatDate(deadLine,
                                                "/:-,","dd-MMM-yyyy"),row,
                                                col+2);
                                            }
                                        }
                                    }else{
                                        temporary = true;
                                        
                                        
                                        CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey(
                                        "commSchdDetFrm_exceptionCode.1029"
                                        ));
                                        cancelCellEditing();
                                        return false;
                                    }
                                }catch (Exception e){
                                    temporary = true;
                                    CoeusOptionPane.showInfoDialog(
                                    e.getMessage());
                                    cancelCellEditing();
                                    return false;
                                }
                            }else if(col == 3){
                                /* if the editing column is protocol submission
                                   deadline column parse the entered date and
                                   check whether the entered date is on or
                                   before scheduled date. If not show error
                                   message to the user and restore the old
                                   value by canceling the editing. */
                                String schDate = dtUtils.restoreDate(
                                tblSchedule.getValueAt(row,1).toString(),
                                "/:-,");
                                Date sDate = dtFormat.parse(schDate);
                                String deadLineDate = dtUtils.restoreDate(
                                formattedDate,"/:-,");
                                Date dDate = dtFormat.parse(deadLineDate);
                                if(dDate.after(sDate)){
                                    temporary = true;
                                    CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                    "commSchdDetFrm_exceptionCode.1030"));
                                    cancelCellEditing();
                                    return false;
                                    
                                }
                                ((JTextField)dateComponent).setText( formattedDate);
                                temporary = true;
                                if(!oldData.equals(formattedDate)){
                                    saveRequired = true;
                                }
                            }
                        }
                    }   // end of else of date column
                }
            }
            catch(Exception exception) {
                return false;
            }
            return super.stopCellEditing();
        }
        
        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)dateComponent).getText();
        }
        
        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need
         * to occur when an cell is selected (or deselected).
         * @param ie ItemEvent which specifies the delegated object
         */
        public void itemStateChanged(ItemEvent ie) {
            super.fireEditingStopped();
        }
    }
    
    public void setScheduleRights(boolean hasRights){
        this.hasRightsForSchedules = hasRights;
    }
}