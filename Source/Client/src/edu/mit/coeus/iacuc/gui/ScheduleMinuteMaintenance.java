/*
 * @(#)ScheduleMinuteMaintenance.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.table.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.*;
import java.util.*;
import javax.swing.event.*;
import edu.mit.coeus.iacuc.bean.MinuteEntryInfoBean;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.iacuc.bean.ScheduleDetailsBean;

/** <CODE>ScheduleMinuteMaintenance</CODE> is a form which display all the schedule
 * minute entries and can be used to <CODE>add/delete/modify</CODE> the schedule minutes information.
 * This class will be instantiated from <CODE>ScheduleDetailsForm</CODE> for Minutes tab
 * in Schedule Maintenance of IRB module.
 * @author Raghunath P.V.
 * @version: 1.0 November 27, 2002
 */

public class ScheduleMinuteMaintenance extends javax.swing.JComponent {
    
    
    /* EntryTypes Minute Data Bean */
    private MinuteEntryBean minuteEntryBean;
    
    /* This is used to hold the mode D for Display, I for Add, U for Modify */
    private char functionType;
    
    /* This is used to hold the reference of MDIFrame */
    private CoeusAppletMDIForm mdiForm;
    
    /* This is used to notify whether the Save is required */
    private boolean saveRequired = false;
    
    /* This is used to hold vector of data beans */
    private Vector minutesData;
    
    /* This is used to hold vector of data beans */
    private Vector minutesEntryTypes;
    
    /* This is used to hold vector of deleted data beans */
    private Vector minutesDeleteData;
    
    /* Holds CoeusMessageResources used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources;
    
    /* This member variable maintains the reference of scheduleID */
    private String scheduleID;
    
    /* This member variable is used to hold the maximum item number in
     * the client side */
    //modified by ravi to "0" because we are incrementing maxEntryNumber on actionPerformed
    // of btnNew.
    private int maxEntryNumber = 0;
    
    /*holds protocol submission list bean */
    private Vector submissionList;
    
    /*holds protocol other actions bean */
    private Vector otherActionsList;
    
    /* holds server system date */
    private java.sql.Timestamp serverSysDate;
    
    /* holds meeting Date */
    private java.sql.Date meetingDate;
    
    /* holds Schedule Date */
    private java.sql.Date scheduleDate;
    
    /* This is used to maintain the index of selected panel.*/
    private int selectedIndex = -1;
    
    private Vector vecProtocolsForScheduleMinutes;
    
    /*Table Model for Minute Details*/
    //Bug Fix (Defect Id : 361)
    private MinuteTableModel minuteTableModel;
    
//    private HashMap hmReviewComments = new HashMap();
     private static final int PROTOCOL_TYPE_CODE = 3;
    private char UPDATE_MINUTE_DETAILS = 'F';
    
    //Added by Vyjayanthi for IRB Enhancement - 03/08/2004
    /** Holds true if user has MAINTAIN_MINUTES right, false otherwise
     */
    private boolean hasMaintainMinutesRight;
    
    /* Cell Editor and Renderer*/
    //private MinuteTableCellRendererEditor minuteTableCellRendererEditor;
    
    //COEUSQA:3233 - Remove Protocol Review Comment from minutes entry window - Start
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
            "/coeusFunctionsServlet";
    private static final String MINUTE_TYPE_REVIEWER_COMMENT_PAR = "IACUC_MINUTE_TYPE_REVIEWER_COMMENT";
    private int minuteTypeReviewerComment;
    //COEUSQA:3233 - End
    
    /** Creates a new <CODE>ScheduleMinuteMaintenance</CODE> form. <p>
     * <I>Default Constructor</I>
     */
    public ScheduleMinuteMaintenance() {
    }
    
    /** Constructor that instantiate <CODE>ScheduleMinutesMaintenance</CODE> and
     * populate the components with the specified data.
     * @param scheduleDetails is a <CODE>ScheduleDetailsBean</CODE> which consists of all the
     * schedule details.
     * @param functionType is a <CODE>Char</CODE> variable which specify the mode, in which the
     * form will be shown.
     * <B>'A'</B> specifies that the form is in Add Mode
     * <B>'M'</B> specifies that the form is in Modify Mode
     * <B>'D'</B> specifies that the form is in Display Mode
     */
    
    public ScheduleMinuteMaintenance(char functionType,
    ScheduleDetailsBean scheduleDetails) {
        
        this.functionType = functionType;
        if(scheduleDetails != null){
            this.minutesData = scheduleDetails.getMinuteList();
            this.submissionList = scheduleDetails.getSubmissionsList();
            this.otherActionsList = scheduleDetails.getOtherActionsList();
            this.serverSysDate = scheduleDetails.getServerSysDate();
            this.meetingDate = scheduleDetails.getMeetingDate();
            this.scheduleDate = scheduleDetails.getScheduleDate();
            this.vecProtocolsForScheduleMinutes = scheduleDetails.getProtocolsForScheduleMinutes();
        }
        if(minutesData == null){
            minutesData = new Vector();
        }
        this.minutesDeleteData = new Vector();
        this.coeusMessageResources = CoeusMessageResources.getInstance();
        
    }
    
    /** This method is used to set the scheduleDetails.
     * @param scheduleDetails is a ScheduleDetailsBean containing all the schedule details.
     */
    public void setScheduleDetailsBean(ScheduleDetailsBean scheduleDetails){
        
        if(scheduleDetails != null){
            this.minutesData = scheduleDetails.getMinuteList();
            this.submissionList = scheduleDetails.getSubmissionsList();
            this.otherActionsList = scheduleDetails.getOtherActionsList();
            this.serverSysDate = scheduleDetails.getServerSysDate();
            this.meetingDate = scheduleDetails.getMeetingDate();
            this.scheduleDate = scheduleDetails.getScheduleDate();
            this.vecProtocolsForScheduleMinutes = scheduleDetails.getProtocolsForScheduleMinutes();
        }
        if(minutesData == null){
            minutesData = new Vector();
        }
        this.minutesDeleteData = new Vector();
//        hmReviewComments = new HashMap();
        setFormData();
        setMinuteValidation();
        setEditors();        
        this.saveRequired = false;
    }
    
    public void setMinutesData( Vector savedData ) {
        this.minutesData = savedData;
        System.out.println("minutesData:"+minutesData);
        if(minutesData == null){
            minutesData = new Vector();
        }
        this.minutesDeleteData = new Vector();
//        hmReviewComments = new HashMap();
        setFormData();
        setMinuteValidation();
        setEditors();        
//        this.saveRequired = false;
    
    }
    /** This member method is used to set the scheduleId.
     * @param schID String that represents scheduleID.
     */
    public void setScheduleID(String schID){
        this.scheduleID = schID;
    }
    
    /** This method is used to get the scheduleId.
     * @return String that contains scheduleID.
     */
    public String getScheduleID(){
        return this.scheduleID;
    }
    
    /** This method is used to set the minuteEntryBean.
     * @param minEntryBean <CODE>MinuteEntryBean</CODE> that contains minuteEntrytype details.
     */
    public void setMinuteEntryBean(MinuteEntryBean minEntryBean){
        this.minuteEntryBean = minEntryBean;
    }
    
    /** This method is used to get the minuteEntryBean.
     * @return MinuteEntryBean that contains minuteEntryType details.
     */
    public MinuteEntryBean getMinuteEntryBean(){
        return this.minuteEntryBean;
    }
    
    /** This method is used to set the minutesEntryTypes.
     * @param minutesTypes Vector that contains ComboBoxBean beans.
     */
    public void setMinuteEntryTypes(Vector minutesTypes){
        this.minutesEntryTypes = minutesTypes;
    }
    
    /** This method is used to get the minutesEntryTypes vector that
     * contains all the <CODE>ComboBoxBean</CODE>'s.
     * @return Vector that contains <CODE>ComboBoxBean</CODE>'s.
     */
    public Vector getMinuteEntryTypes(){
        return this.minutesEntryTypes;
    }
    
    /** This method is used to determine whether the data to be saved or not.
     * @return true if modifications done, false otherwise.
     */
    
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){        
            if(tblMinutesDetail.getRowCount() > 0 ) {
                int rowNum = tblMinutesDetail.getSelectedRow();
                if( !tblMinutesDetail.isFocusOwner()){
                    tblMinutesDetail.requestFocusInWindow();
                }
                if(rowNum > 0){
                    tblMinutesDetail.setRowSelectionInterval(rowNum,rowNum);
                }
                //tblMinutesDetail.setColumnSelectionInterval(0,0);
            }else{
                btnNew.requestFocusInWindow();                        
        }
    }    
    //end Amit        
    
    /** This method is used to set saveRequired variable.
     * @param save boolean variable to be set to saveRequired variable.
     */
    
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    /** Method to get the functionType
     * @return <CODE>char</CODE> data that represents the mode of the form.
     */
    
    public char getFunctionType(){
        return functionType;
    }
    
    /** Method to set the functionType
     * @param functType  char variable to set functionType like 'D', 'I', 'M'
     */
    
    public void setFunctionType(char functType){
        this.functionType=functType;
    }
    
    /** Method to get the schedule minutes data.
     * @return Vector of MinuteEntryInfoBean data.
     */
    
    public java.util.Vector getScheduleMinutesData(){
        
        Vector minDeleteData = minutesDeleteData;
        if (minDeleteData != null && minDeleteData.size() > 0){
            int vecDeleteMinutesSize = minDeleteData.size();
            for(int ind = 0;ind < vecDeleteMinutesSize ; ind++){
                minutesData.addElement((MinuteEntryInfoBean)minDeleteData.
                elementAt(ind));
            }
        }
        return minutesData;
    }
    
    /**
     * This method is used to set the cell editors to the JList
     */
    
    
    private void setListEditors(){
        
        /*
        lstMinutesDetail.setModel(new DefaultListModel());
        MinuteCellRenderer minuteCellRenderer= new MinuteCellRenderer();
        lstMinutesDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstMinutesDetail.setCellRenderer(minuteCellRenderer);
         */
        
        //Bug Fix(Defect Id:361)
        
        //tblMinutesDetail.setRowHeight(150);
        //tblMinutesDetail.setTableHeader(null);
        
        minuteTableModel = new MinuteTableModel(
        ((DefaultTableModel)tblMinutesDetail.getModel()).getDataVector(),getColumnNames());
        tblMinutesDetail.setModel(minuteTableModel);
        
        /*minuteTableCellRendererEditor= new MinuteTableCellRendererEditor();
        tblMinutesDetail.getColumnModel().getColumn(0).setCellRenderer(minuteTableCellRendererEditor);
        tblMinutesDetail.getColumnModel().getColumn(0).setCellEditor(minuteTableCellRendererEditor);*/
        tblMinutesDetail.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Added by Vyjayanthi to display Minute Entry screen
        //Start of Code
        tblMinutesDetail.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                //COEUSQA:3233 - Remove Protocol Review Comment from minutes entry window - Start
                selectedIndex = tblMinutesDetail.getSelectedRow();
                MinuteEntryInfoBean selectedMinuteEntryInfoBean = (MinuteEntryInfoBean)
                minuteTableModel.getMinuteBean(selectedIndex);
                if(selectedMinuteEntryInfoBean != null){
                    if(selectedMinuteEntryInfoBean.getMinuteEntryTypeCode() == minuteTypeReviewerComment){
                        btnModify.setEnabled(false);
                        btnDelete.setEnabled(false);
                    } else {
                        btnModify.setEnabled(true);
                        btnDelete.setEnabled(true);
                    }
                    
                }
                //COEUSQA:3233 - End
                if (me.getClickCount() == 2) {
//                    selectedIndex = tblMinutesDetail.getSelectedRow();
//                    MinuteEntryInfoBean selectedMinuteEntryInfoBean = (MinuteEntryInfoBean)
//                    minuteTableModel.getMinuteBean(selectedIndex);
                    if(selectedMinuteEntryInfoBean != null){
                        
                        MinuteEntryInfoBean displayMinuteEntryInfoBean =
                        new MinuteEntryInfoBean();
                        
                        displayMinuteEntryInfoBean.setScheduleId(
                        selectedMinuteEntryInfoBean.getScheduleId());
                        displayMinuteEntryInfoBean.setEntryNumber(
                        selectedMinuteEntryInfoBean.getEntryNumber());
                        displayMinuteEntryInfoBean.setMinuteEntryTypeCode(
                        selectedMinuteEntryInfoBean.getMinuteEntryTypeCode());
                        displayMinuteEntryInfoBean.setMinuteEntryTypeDesc(
                        selectedMinuteEntryInfoBean.getMinuteEntryTypeDesc());
                        displayMinuteEntryInfoBean.setProtocolNumber(
                        selectedMinuteEntryInfoBean.getProtocolNumber());
                        displayMinuteEntryInfoBean.setOtherItemDesc(
                        selectedMinuteEntryInfoBean.getOtherItemDesc());
                        displayMinuteEntryInfoBean.setSequenceNumber(
                        selectedMinuteEntryInfoBean.getSequenceNumber());
                        displayMinuteEntryInfoBean.setPrivateCommentFlag(
                        selectedMinuteEntryInfoBean.isPrivateCommentFlag());
                        displayMinuteEntryInfoBean.setProtocolContingencyCode(
                        selectedMinuteEntryInfoBean.getProtocolContingencyCode());
                        displayMinuteEntryInfoBean.setProtocolContingencyDesc(
                        selectedMinuteEntryInfoBean.getProtocolContingencyDesc());
                        displayMinuteEntryInfoBean.setMinuteEntry(
                        selectedMinuteEntryInfoBean.getMinuteEntry());
                        displayMinuteEntryInfoBean.setUpdateTimestamp(
                        selectedMinuteEntryInfoBean.getUpdateTimestamp());
                        displayMinuteEntryInfoBean.setUpdateUser(
                        selectedMinuteEntryInfoBean.getUpdateUser());
                        displayMinuteEntryInfoBean.setSubmissionNumber(
                        selectedMinuteEntryInfoBean.getSubmissionNumber());
                        
                        if(selectedMinuteEntryInfoBean.getAcType() == null){
                            displayMinuteEntryInfoBean.setAcType("M");
                        }else {
                            displayMinuteEntryInfoBean.setAcType(selectedMinuteEntryInfoBean.
                            getAcType());
                        }
                        
                        int selectedRow = tblMinutesDetail.getSelectedRow();
                        
                        /*MinuteEntryForm minuteEntryForm =
                        new MinuteEntryForm(displayMinuteEntryInfoBean,
                        submissionList, otherActionsList);*/
                        functionType = 'D';
                        MinuteEntryForm minuteEntryForm =
                        new MinuteEntryForm(functionType, displayMinuteEntryInfoBean,
                        submissionList, otherActionsList);
//                        minuteEntryForm.setMaxEntryNumber(maxEntryNumber);
                        minuteEntryForm.setScheduleID(scheduleID);
                        //minuteEntryForm.setScheduleMaintenanceForm(this);
                        
                        minuteEntryForm.showForm(mdiForm, true);
                        //minuteTableCellRendererEditor.cancelCellEditing();
                        tblMinutesDetail.setRowSelectionInterval(selectedRow, selectedRow);
                    }else {
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "schdMntMinutesFrm_exceptionCode.1303"));
                    }
                }
            }
        });
    }
    //End of code
    
    
    //Bug Fix(Defect Id:361)
    //
    
    /** This method is used to implement client side validations.
     * @return true if form data is valid else false.
     */
    public static boolean validateData(){
        return true;
    }
    
    /** This method is used to initialize the components, set the data to the components.
     * This method will be invoked from <CODE>ScheduleDetailsForm</CODE>.
     * @param coeusMdiForm a reference of <CODE>CoeusAppletMDIForm</CODE>
     * @return JComponent containing all the form components.
     */
    public JComponent showMinutesMaintenanceForm(CoeusAppletMDIForm coeusMdiForm){
        
        this.mdiForm = coeusMdiForm;
        this.coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        
        java.awt.Component[] components = {tblMinutesDetail,btnNew,btnModify,btnDelete};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        
        setFocusCycleRoot(true);
        setListEditors();
        setFormData();
        setMinuteValidation();
        setEditors();        //Added by Vyjayanthi to set table data
        
        
        /*int lstSize = ((ListModel)lstMinutesDetail.getModel()).getSize();
        if(lstSize > 0){
            lstMinutesDetail.setSelectedIndex(0);
        }else{
            btnDelete.setEnabled(false);
            btnModify.setEnabled(false);
        }*/
        
        //Bug Fix(Defect Id:361)
        int size = tblMinutesDetail.getRowCount();
        if(size > 0) {
            tblMinutesDetail.setRowSelectionInterval(0,0);
        } else {
            btnNew.requestFocusInWindow();
            btnDelete.setEnabled(false);
            btnModify.setEnabled(false);
        }
        //Bug Fix(Defect Id:361)
        
        //COEUSQA:3233 - Remove Protocol Review Comment from minutes entry window - Start        
        minuteTypeReviewerComment = getIacucMinuteTypeReviewerComment();        
        if(tblMinutesDetail.getRowCount() > 0) {
            MinuteEntryInfoBean selectedMinuteEntryInfoBean = (MinuteEntryInfoBean)
            minuteTableModel.getMinuteBean(0);
            if(selectedMinuteEntryInfoBean != null && selectedMinuteEntryInfoBean.getMinuteEntryTypeCode() == minuteTypeReviewerComment){
                btnModify.setEnabled(false);
                btnDelete.setEnabled(false);
            } else {
                btnModify.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        }        
        //COEUSQA:3233 - End
        
        return this;
    }
    
    //COEUSQA:3233 - Remove Protocol Review Comment from minutes entry window - Start
    /** Returns the IACUC_MINUTE_TYPE_REVIEWER_COMMENT value
     */
    public int getIacucMinuteTypeReviewerComment(){
        int value = 0;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject("GET_PARAMETER_VALUE");
        Vector vecParameter = new Vector();
        vecParameter.add(MINUTE_TYPE_REVIEWER_COMMENT_PAR);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder !=null && responder.isSuccessfulResponse()){
            value = Integer.parseInt((String) responder.getDataObject());
        }
        return value;
        
    }    
    //COEUSQA:3233 - End
    
    /**
     * This method is used to enable or disable the buttons based on the
     * minute conditions.
     */
    private void setMinuteValidation(){
        btnDelete.setEnabled(true);
        btnModify.setEnabled(true);
        btnNew.setEnabled(true);
        /* If the function type specifies display mode then it disables the
           buttons*/
        if (functionType == CoeusGuiConstants.DISPLAY_MODE) {
            
            //To gray out the Minutes Details table.....
            tblMinutesDetail.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
//            tblMinutesDetail.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
//            tblMinutesDetail.setSelectionForeground(Color.black);
            
            btnDelete.setEnabled(false);
            btnModify.setEnabled(false);
            btnNew.setEnabled(false);
            
            //Added by Vyjayanthi for IRB Enhancement - 04/08/2004 - Start
            //Enable New and Modify buttons in Minutes tab page if 
            //user has MAINTAIN_MINUTES right in Display mode
            if( hasMaintainMinutesRight ){
                btnNew.setEnabled(true);
                btnModify.setEnabled(true);
            }
            //Added by Vyjayanthi for IRB Enhancement - 04/08/2004 - End
            
        } else {
            /* validation commented by ravi for fix id: 194
            // If the meeting date is after the current server date then it
            //   disables the buttons
            if(meetingDate != null){
                if(meetingDate.after(serverSysDate)){
                    btnDelete.setEnabled(false);
                    btnModify.setEnabled(false);
                    btnNew.setEnabled(false);
                }
            }else {
                 // If the meeting date is not specified and schedule date
                 //   is after the current server date then it disables
                 //   the buttons
                if(scheduleDate.after(serverSysDate)){
                    btnDelete.setEnabled(false);
                    btnModify.setEnabled(false);
                    btnNew.setEnabled(false);
                }
            }
            end of fix id: 194 */
        }
    }
    
    //Added by Vyjayanthi - 04/08/2004 for IRB Enhancement
    /** Setter for the property hasMaintainMinutesRight
     * @param hasRight holds true if user has MAINTAIN_MINUTES right, false otherwise
     */
    public void setHasMaintainMinutesRight(boolean hasRight) {
        this.hasMaintainMinutesRight = hasRight;
    }
    
    /** Method to set the Vector data to <CODE>JList</CODE>.
     * This method sets the data which is available in minutesData
     * <CODE>Vector</CODE> into <CODE>JList</CODE>.
     */
    public void setFormData(){
        minuteTableModel.setDataBeans(minutesData);
    }
    
    //Added by Vyjayanthi
    //Start of code
    
    /* Method to get column names */
    private Vector getColumnNames(){
        Enumeration enumColNames = tblMinutesDetail.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /* Method to set the table data */
    private void setEditors(){
        tblMinutesDetail.getTableHeader().setResizingAllowed(true);
        tblMinutesDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblMinutesDetail.getColumnModel().getColumn(0);
        //Modified for COEUSQA-2290 : New Minute entry type for Review Comments - Start
//        column.setMinWidth(150);
        //column.setPreferredWidth(100);
        column.setMinWidth(250);
        //COEUSQA-2290 : End
        // 3282: Reviewer view of Protocols - Start
//        column = tblMinutesDetail.getColumnModel().getColumn(1);
//        column.setMinWidth(300);
//        //column.setPreferredWidth(200);
        column = tblMinutesDetail.getColumnModel().getColumn(1);
        //Modified for COEUSQA-2290 : New Minute entry type for Review Comments - Start
//        column.setMinWidth(258);
//        //column.setPreferredWidth(200);
//        column.setPreferredWidth(258);
        column.setMinWidth(325);
        column.setPreferredWidth(325);
        //COEUSQA-2290 : End
        
        column = tblMinutesDetail.getColumnModel().getColumn(2);
        column.setMinWidth(48);
        column.setPreferredWidth(48);
        // 3282: Reviewer view of Protocols - End
        
        column = tblMinutesDetail.getColumnModel().getColumn(3);
        column.setMinWidth(58);
        //column.setPreferredWidth(45);
        column.setPreferredWidth(58);
        
        tblMinutesDetail.getTableHeader().setReorderingAllowed( false );
        tblMinutesDetail.getTableHeader().setResizingAllowed(true);
        tblMinutesDetail.setFont(CoeusFontFactory.getNormalFont());
        tblMinutesDetail.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblMinutesDetail.setRowSelectionAllowed(true);
    }
    //End of code
    
    /** Method used for refreshing the JList data.
     * The user enters data in <CODE>MinuteEntryForm</CODE>, and invokes this method
     * to refresh the data in JList.
     * @param minuteEntryInfoBean <CODE>MinuteEntryInfoBean</CODE> which is to be added to JList.
     */
    
    public void refreshDataVector(MinuteEntryInfoBean minuteEntryInfoBean){
        
        if(minuteEntryInfoBean != null){
            if (selectedIndex != -1 ) {
                minutesData.setElementAt(minuteEntryInfoBean,selectedIndex);
                minuteTableModel.setDataBeans(minutesData);
                selectedIndex = -1 ;
            }else {
                sortAndRefreshMinuteVector(minuteEntryInfoBean);
            }
        }
        saveRequired=true;
    }
    
    /**
     * Method used for sorting the minutes data.
     * The list entries in JLIst must be sorted in an order specified.
     * First All General Comments entries will be shown, following Attendees,
     * OtherBusiness and then protocol minute entries.
     * @param MinuteEntryInfoBean Bean which is to be added to JList.
     */
    
    private void sortAndRefreshMinuteVector(MinuteEntryInfoBean minuteInfoBean){
        
        //minutesData.addElement(minuteInfoBean);
        int newEntryType = minuteInfoBean.getMinuteEntryTypeCode();
        int currentEntryType = 0;
        Vector vecGeneralComments = new Vector();
        int vecGeneralCommentsSize;
        Vector vecProtocol = new Vector();
        int vecProtocolSize;
        Vector vecOtherBusiness = new Vector();
        int vecOtherBusinessSize;
        Vector vecAttendees = new Vector();
        int vecAttendeesSize;
        Vector mainMinuteData = new Vector();
        int minDataSize = minutesData.size();
        boolean found = false;
        /* Iterating till the last minute entry */
        for(int index = 0; index < minDataSize; index++){
            MinuteEntryInfoBean minuteEntryInfoBean =(MinuteEntryInfoBean)
            minutesData.get(index);
            currentEntryType = minuteEntryInfoBean.getMinuteEntryTypeCode();
            if( currentEntryType > newEntryType ){
                found = true;
                minutesData.insertElementAt(minuteInfoBean,index);
                break;
            }else if( currentEntryType == 3 ) { 
                int oldEntryNumber = minuteEntryInfoBean.getEntryNumber();
                String oldProtocolNumber = minuteEntryInfoBean.getProtocolNumber();
                String oldSortId = oldEntryNumber + oldProtocolNumber;
                int newEntryNumber = minuteInfoBean.getEntryNumber();
                String newProtocolNumber = minuteInfoBean.getProtocolNumber();
                String newSortId = newEntryNumber + newProtocolNumber;

                int condition = oldSortId.compareTo(newSortId);
                if(condition > 0){
                    found = true;
                    minutesData.insertElementAt(minuteInfoBean,index);
                    break;
                }
                
            }
        }
        if( !found ) {
            minutesData.addElement(minuteInfoBean);
        }
            /*  Implementation of sorting. Placing all the beans
                in corresponding vectors. If the bean minute entry description
                is of General Comments then place those corresponding beans in
                vecGeneralComments, If the bean minute entry description
                is of Attendees then place those corresponding beans in
                vecAttendees, If the bean minute entry description
                is of Protocol then place those corresponding beans in
                vecProtocol, or else in vecOtherBusiness. And in vecProtocol
                the entries should be sorted based on entry number plus
                protocol number of the bean. */
            
//            if(minuteEntryInfoBean != null){
//                if(((String)minuteEntryInfoBean.getMinuteEntryTypeDesc()).
//                equalsIgnoreCase("General Comments")){
//                    vecGeneralComments.addElement(minuteEntryInfoBean);
//                }
//                if(((String)minuteEntryInfoBean.getMinuteEntryTypeDesc()).
//                equalsIgnoreCase("Other Business")){
//                    vecOtherBusiness.addElement(minuteEntryInfoBean);
//                }
//                if(((String)minuteEntryInfoBean.getMinuteEntryTypeDesc()).
//                equalsIgnoreCase("Attendance")){
//                    vecAttendees.addElement(minuteEntryInfoBean);
//                }
//                //implement sorting and add the sorted beans in protocol vector.
//                if(((String)minuteEntryInfoBean.getMinuteEntryTypeDesc()).
//                equalsIgnoreCase("Protocol")){
//                    if(vecProtocol.size() <= 0){
//                        vecProtocol.addElement(minuteEntryInfoBean);
//                    }else {
//                        vecProtocol.addElement(minuteEntryInfoBean);
//                        int size = vecProtocol.size();
//                        for(int ind = 0 ; ind < size ; ind ++){
//                            for(int indNext = 0 ; indNext < (size-ind) ; indNext ++){
//                                MinuteEntryInfoBean oldMinuteBean =
//                                (MinuteEntryInfoBean)vecProtocol.get(ind);
//                                MinuteEntryInfoBean newMinuteBean =
//                                (MinuteEntryInfoBean)vecProtocol.
//                                get(indNext);
//                                int oldEntryNumber = oldMinuteBean.
//                                getEntryNumber();
//                                String oldProtocolNumber = oldMinuteBean.
//                                getProtocolNumber();
//                                String oldSortId = oldEntryNumber +
//                                oldProtocolNumber;
//                                int newEntryNumber = newMinuteBean.
//                                getEntryNumber();
//                                String newProtocolNumber = newMinuteBean.
//                                getProtocolNumber();
//                                String newSortId = newEntryNumber +
//                                newProtocolNumber;
//                                
//                                int condition = oldSortId.compareTo(newSortId);
//                                
//                                if(condition > 0){
//                                    vecProtocol.setElementAt(newMinuteBean,ind);
//                                    vecProtocol.setElementAt(oldMinuteBean,indNext);
//                                }
//                            }
//                        }//End For Ind
//                    }//End If
//                }
//            }
//        }
//        
//        vecGeneralCommentsSize = vecGeneralComments.size();
//        vecOtherBusinessSize = vecOtherBusiness.size();
//        vecProtocolSize = vecProtocol.size();
//        vecAttendeesSize = vecAttendees.size();
//        //Adding GeneralComments Minute entries to main data vector.
//        //Next adding Attendees Minute entries to main data vector.
//        //Next adding Protocol Minute entries to main data vector.
//        //Next adding Other actions Minute entries to main data vector.
//        if(vecGeneralComments != null &&  vecGeneralCommentsSize > 0){
//            for(int index = 0; index < vecGeneralCommentsSize; index++){
//                MinuteEntryInfoBean minuteEntryInfoBean =(MinuteEntryInfoBean)
//                vecGeneralComments.get(index);
//                mainMinuteData.addElement(minuteEntryInfoBean);
//            }
//        }
//        //Adding Attendees Minute entries to main data vector.
//        if(vecAttendees != null &&  vecAttendeesSize > 0){
//            for(int index = 0; index < vecAttendeesSize; index++){
//                MinuteEntryInfoBean minuteEntryInfoBean =(MinuteEntryInfoBean)
//                vecAttendees.get(index);
//                mainMinuteData.addElement(minuteEntryInfoBean);
//            }
//        }
//        //Adding Protocol Minute entries to main data vector.
//        if(vecProtocol != null &&  vecProtocolSize > 0){
//            for(int index = 0; index < vecProtocolSize; index++){
//                MinuteEntryInfoBean minuteEntryInfoBean =(MinuteEntryInfoBean)
//                vecProtocol.get(index);
//                mainMinuteData.addElement(minuteEntryInfoBean);
//            }
//        }
//        //Adding Other Business action Minute entries to main data vector.
//        if(vecOtherBusiness != null &&  vecOtherBusinessSize > 0){
//            for(int index = 0; index < vecOtherBusinessSize; index++){
//                MinuteEntryInfoBean minuteEntryInfoBean =(MinuteEntryInfoBean)
//                vecOtherBusiness.get(index);
//                mainMinuteData.addElement(minuteEntryInfoBean);
//            }
//        }
        //minutesData = mainMinuteData;
        
        minuteTableModel.setDataBeans(minutesData);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnMinutesDetail = new javax.swing.JScrollPane();
        tblMinutesDetail = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(800, 400));
        setPreferredSize(new java.awt.Dimension(800, 400));
        scrPnMinutesDetail.setMinimumSize(new java.awt.Dimension(700, 400));
        scrPnMinutesDetail.setPreferredSize(new java.awt.Dimension(700, 400));
        tblMinutesDetail.setFont(CoeusFontFactory.getNormalFont());
        tblMinutesDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Entry Type", "Entry", "Final", "Private"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrPnMinutesDetail.setViewportView(tblMinutesDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(scrPnMinutesDetail, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnNew.setFont(CoeusFontFactory.getLabelFont());
        btnNew.setMnemonic('N');
        btnNew.setText("New");
        btnNew.setMaximumSize(new java.awt.Dimension(90, 27));
        btnNew.setMinimumSize(new java.awt.Dimension(85, 27));
        btnNew.setPreferredSize(new java.awt.Dimension(85, 27));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_actionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 3, 3);
        pnlButtons.add(btnNew, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        btnModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModify_actionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlButtons.add(btnModify, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_actionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlButtons.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlButtons, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    private void btnDelete_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_actionPerformed
        // added by manoj to perform authorization 15/09/2003
        if(authorizedToPerform()){
        
            //Bug Fix (Defect Id : 361)
            int index = tblMinutesDetail.getSelectedRow();
            if(tblMinutesDetail != null &&  index >= 0){
                int selectedOption = CoeusOptionPane.
                showQuestionDialog(
                coeusMessageResources.parseMessageKey(
                "schdMntMinutesFrm_exceptionCode.1301"),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);

                if(0 == selectedOption){

                    MinuteEntryInfoBean selectedMinuteEntryInfoBean =
                    (MinuteEntryInfoBean)minuteTableModel.getMinuteBean(index);
                    if(selectedMinuteEntryInfoBean != null){

                        selectedMinuteEntryInfoBean.setAcType("D");
                        minutesDeleteData.addElement(selectedMinuteEntryInfoBean);
                        minutesData.removeElementAt(index);
                        System.out.println("The deleted row is" +index);
                         saveRequired = true;

                        //setFormData( minutesData );
                        //Chandra
                        minuteTableModel.setDataBeans(minutesData);
                        updateMinuteEntries();
                    }
                }
            }else {
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(
                "schdMntMinutesFrm_exceptionCode.1302"));
            }
            //Bug Fix (Defect Id : 361)
            
        }
        if(btnDelete.isEnabled())
            btnDelete.requestFocusInWindow();
    }//GEN-LAST:event_btnDelete_actionPerformed
    
    private void btnModify_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModify_actionPerformed
        // added by manoj to perform authorization 15/09/2003
        if(authorizedToPerform()){
        //Bug Fix (Defect Id : 361)
        this.selectedIndex = tblMinutesDetail.getSelectedRow();
        MinuteEntryInfoBean selectedMinuteEntryInfoBean = (MinuteEntryInfoBean)
        minuteTableModel.getMinuteBean(selectedIndex);
        if(selectedMinuteEntryInfoBean != null){
            
            MinuteEntryInfoBean modifyMinuteEntryInfoBean =
            new MinuteEntryInfoBean();
            
            modifyMinuteEntryInfoBean.setScheduleId(
            selectedMinuteEntryInfoBean.getScheduleId());
            modifyMinuteEntryInfoBean.setEntryNumber(
            selectedMinuteEntryInfoBean.getEntryNumber());
            modifyMinuteEntryInfoBean.setMinuteEntryTypeCode(
            selectedMinuteEntryInfoBean.getMinuteEntryTypeCode());
            modifyMinuteEntryInfoBean.setMinuteEntryTypeDesc(
            selectedMinuteEntryInfoBean.getMinuteEntryTypeDesc());
            modifyMinuteEntryInfoBean.setProtocolNumber(
            selectedMinuteEntryInfoBean.getProtocolNumber());
            modifyMinuteEntryInfoBean.setOtherItemDesc(
            selectedMinuteEntryInfoBean.getOtherItemDesc());
            modifyMinuteEntryInfoBean.setSequenceNumber(
            selectedMinuteEntryInfoBean.getSequenceNumber());
            modifyMinuteEntryInfoBean.setPrivateCommentFlag(
            selectedMinuteEntryInfoBean.isPrivateCommentFlag());
            modifyMinuteEntryInfoBean.setProtocolContingencyCode(
            selectedMinuteEntryInfoBean.getProtocolContingencyCode());
            modifyMinuteEntryInfoBean.setProtocolContingencyDesc(
            selectedMinuteEntryInfoBean.getProtocolContingencyDesc());
            modifyMinuteEntryInfoBean.setMinuteEntry(
            selectedMinuteEntryInfoBean.getMinuteEntry());
            modifyMinuteEntryInfoBean.setUpdateTimestamp(
            selectedMinuteEntryInfoBean.getUpdateTimestamp());
            modifyMinuteEntryInfoBean.setUpdateUser(
            selectedMinuteEntryInfoBean.getUpdateUser());
            modifyMinuteEntryInfoBean.setSubmissionNumber(
            selectedMinuteEntryInfoBean.getSubmissionNumber());
            // 3282: Reviewer view of Protocols
            modifyMinuteEntryInfoBean.setFinalFlag(selectedMinuteEntryInfoBean.isFinalFlag());
            if(selectedMinuteEntryInfoBean.getAcType() == null){
                modifyMinuteEntryInfoBean.setAcType("U");
            }else {
                modifyMinuteEntryInfoBean.setAcType(selectedMinuteEntryInfoBean.
                getAcType());
            }
            
            int selectedRow = tblMinutesDetail.getSelectedRow();
            
            /*MinuteEntryForm minuteEntryForm =
                new MinuteEntryForm(modifyMinuteEntryInfoBean,
                    submissionList, otherActionsList);*/
            functionType = 'M';  //Added by Vyjayanthi
            MinuteEntryForm minuteEntryForm =
            new MinuteEntryForm(functionType, modifyMinuteEntryInfoBean,
                                submissionList, otherActionsList);
//            // Added by chandra 20/10/2003 - start
//            /** To display contingency description without editable. if contingency code is
//             *present*/
//            if(selectedMinuteEntryInfoBean.getProtocolContingencyCode()==null ||
//                selectedMinuteEntryInfoBean.getProtocolContingencyCode().equals("")){
//               minuteEntryForm.txtArEntry.setEditable(true); 
//              
//            }else{
//                  minuteEntryForm.txtArEntry.setEditable(false); 
//            }// Added by chandra 20/10/2003 - End
          
//          minuteEntryForm.setMaxEntryNumber(maxEntryNumber);
            minuteEntryForm.setScheduleID(scheduleID);
            minuteEntryForm.setScheduleMaintenanceForm(this);
            minuteEntryForm.showForm(mdiForm, true);
            updateMinuteEntries();
            
            //minuteTableCellRendererEditor.cancelCellEditing();
            tblMinutesDetail.setRowSelectionInterval(selectedRow, selectedRow);
            
        }else {
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey(
            "schdMntMinutesFrm_exceptionCode.1303"));
        }
        //Bug Fix (Defect Id : 361)
        }
        //code to keep the focus : raghuSV
        if(btnModify.isEnabled())
            btnModify.requestFocusInWindow();
    }//GEN-LAST:event_btnModify_actionPerformed
    
    private void btnNew_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_actionPerformed
        // added by manoj to perform authorization 15/09/2003        
        // Add your handling code here:
        if(authorizedToPerform()){
        /* Instantiate new MinuteEntryInfoBean, set the Actype to 'i' and
           pass the bean to MinuteEntryForm to add additional details and
           in MinuteentryForm invoke refreshDataVector to refresh the
           minute list entries */
            this.selectedIndex = -1;
            MinuteEntryInfoBean newMinuteEntryInfoBean = new MinuteEntryInfoBean();
            newMinuteEntryInfoBean.setAcType("I");
            newMinuteEntryInfoBean.setScheduleId(scheduleID);
//            newMinuteEntryInfoBean.setEntryNumber(maxEntryNumber + 1);
            
        /*
        MinuteEntryForm minuteEntryForm =
            new MinuteEntryForm(newMinuteEntryInfoBean,
                submissionList, otherActionsList);
         */
            functionType = 'I';  //Added by Vyjayanthi
            MinuteEntryForm minuteEntryForm =
            new MinuteEntryForm(functionType, newMinuteEntryInfoBean,
            submissionList, otherActionsList);
            minuteEntryForm.setScheduleID(scheduleID);
//            minuteEntryForm.setMaxEntryNumber(maxEntryNumber + 1);
            minuteEntryForm.setScheduleMaintenanceForm(this);
            minuteEntryForm.showForm(mdiForm,true);
            updateMinuteEntries();
        }
        if(btnNew.isEnabled())
            btnNew.requestFocusInWindow();
    }//GEN-LAST:event_btnNew_actionPerformed
    
    private boolean authorizedToPerform(){
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
        RequesterBean request = new RequesterBean();
        request.setId(scheduleID) ; //prps added this jan 16 2004
        request.setFunctionType('H');
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return true;
        }else{
            if(response.getDataObject() != null){
                System.out.println("not null");
                CoeusOptionPane.showDialog(new CoeusClientException
                ((CoeusException)response.getDataObject()));
            }else{
                CoeusOptionPane.showWarningDialog(response.getMessage());
            }
        }
        System.out.println(" Server Side Error ");
        return false;
    }
    
    private void updateMinuteEntries() {
        if( isSaveRequired() ) {
//            ((edu.mit.coeus.gui.CoeusInternalFrame)mdiForm.getSelectedFrame()).saveActiveSheet();
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
            RequesterBean request = new RequesterBean();
            request.setFunctionType(UPDATE_MINUTE_DETAILS);
            request.setId(scheduleID);
            request.setDataObjects(getScheduleMinutesData());
            // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
            request.setDataObject(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE);
            // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
            AppletServletCommunicator comm = new AppletServletCommunicator(
            connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                Vector savedData = response.getDataObjects();
                setMinutesData( savedData );
            }else{
                CoeusOptionPane.showInfoDialog(response.getMessage());
            }
        }
    
    }
    
//    /** Getter for property hmReviewComments.
//     * @return Value of property hmReviewComments.
//     *
//     */
//    public Vector getReviewCommentsForProtocol(String protocolNo, int subNo) {
//        if( hmReviewComments.containsKey(protocolNo+subNo)) {
//            return (Vector)hmReviewComments.get(protocolNo+subNo);
//        }
//        return null;
//    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnNew;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JScrollPane scrPnMinutesDetail;
    private javax.swing.JTable tblMinutesDetail;
    // End of variables declaration//GEN-END:variables

    class MinuteTableModel extends DefaultTableModel {
        private Vector dataBeans = new Vector();
        // 3282: Reviewer view of Protocols - Start
//        Class[] types = new Class [] {
//            java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, 
//        };
        
        Class[] types = new Class [] {
            java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class
        };
        // 3282: Reviewer view of Protocols - End
        public MinuteTableModel(Vector data, Vector columnNames){
            super(data, columnNames);
        }
        public void setDataBeans(Vector vecBeans){
            dataBeans = vecBeans;
//            hmReviewComments = new HashMap();
            int preSelRow = tblMinutesDetail.getSelectedRow();
            Vector data = getData(vecBeans);
            setDataVector(data,columnIdentifiers);
            setEditors();   //Added by Vyjayanthi to reset column widths after saving
            fireTableDataChanged();
            int size = tblMinutesDetail.getRowCount();
            if(size > 0) {
                if( size > preSelRow && preSelRow != -1 ) {
                    tblMinutesDetail.setRowSelectionInterval(preSelRow,preSelRow);
                }else{
                    tblMinutesDetail.setRowSelectionInterval(0,0);
                }
                btnDelete.setEnabled(true);
                btnModify.setEnabled(true);
            } else {
                btnNew.requestFocusInWindow();
                btnDelete.setEnabled(false);
                btnModify.setEnabled(false);
            }
            //Bug Fix (Defect Id : 361)
            
        }
        protected Vector getData(Vector data ) {
            Vector tableData = new Vector(); //Added by Vyjayanthi
            if (data != null && data.size() > 0){
                int inSize = data.size();
                MinuteEntryInfoBean minutesBean = null;
//                int[] entryNumbersArray = new int[inSize];
                for( int inCtr = 0; inCtr < inSize ; inCtr++){
                    
                    minutesBean = (MinuteEntryInfoBean)data.elementAt(inCtr);
//                    int entryNumber = minutesBean.getEntryNumber();
//                    entryNumbersArray[inCtr] = entryNumber;
                    //Added by Vyjayanthi
                    //Start of code
                    Vector tableRow = new Vector();
                    if(minutesBean != null){
//                        if( minutesBean.getMinuteEntryTypeCode() == PROTOCOL_TYPE_CODE ) {
//                            String protocolNo = minutesBean.getProtocolNumber();
//                            int subNo = minutesBean.getSubmissionNumber();
//                            if( hmReviewComments.containsKey(protocolNo+subNo) ) {
//                                Vector comments = (Vector) hmReviewComments.get(protocolNo+subNo);
//                                comments.add(minutesBean);
//                                hmReviewComments.put(protocolNo+subNo, comments);
//                            }else{
//                                Vector comments = new Vector();
//                                comments.add(minutesBean);
//                                hmReviewComments.put(protocolNo+subNo, comments);
//                            }
//                        }
                         //Modified for COEUSQA-2290 : New Minute entry type for Review Comments - Start
//                         if( minutesBean.getMinuteEntryTypeCode()== PROTOCOL_TYPE_CODE ) {
//                                    String Number = minutesBean.getProtocolNumber();
//                                    if(Number==null){
//                                        tableRow.addElement( minutesBean.getMinuteEntryTypeDesc() == null ? "" : minutesBean.getMinuteEntryTypeDesc());
//                                    }else{
//                                    String protocolNumber=" - "+Number;    
//                                    tableRow.addElement( minutesBean.getMinuteEntryTypeDesc() == null ? "" : minutesBean.getMinuteEntryTypeDesc()+protocolNumber);
//                                    }
//                         }else{
//                            tableRow.addElement( minutesBean.getMinuteEntryTypeDesc() == null ? "" : minutesBean.getMinuteEntryTypeDesc());
//                         }
                         
                         if(minutesBean.getProtocolNumber() != null){
                             String Number = minutesBean.getProtocolNumber();
                             if(Number==null){
                                 tableRow.addElement( minutesBean.getMinuteEntryTypeDesc() == null ? "" : minutesBean.getMinuteEntryTypeDesc());
                             }else{
                                 String protocolNumber=" - "+Number;
                                 tableRow.addElement( minutesBean.getMinuteEntryTypeDesc() == null ? "" : minutesBean.getMinuteEntryTypeDesc()+protocolNumber);
                             }
                             
                         }else{
                             tableRow.addElement( minutesBean.getMinuteEntryTypeDesc() == null ? "" : minutesBean.getMinuteEntryTypeDesc());
                         }
                         
                         //COEUSQA-2290 : End
                         
                        tableRow.addElement( minutesBean.getMinuteEntry() == null ? "" : minutesBean.getMinuteEntry());
                        // 3282: Reviewer view of Protocols
                        tableRow.addElement( new Boolean(minutesBean.isFinalFlag()));        
                        tableRow.addElement( new Boolean(minutesBean.isPrivateCommentFlag()));
                    }
                    tableData.addElement( tableRow );
                    //End of code
                }
//                for(int ind=0 ; ind < entryNumbersArray.length; ind++){
//                    if(entryNumbersArray[ind] >= maxEntryNumber){
//                        maxEntryNumber=entryNumbersArray[ind];
//                    }
//                }
                
            }
//            else {
//                maxEntryNumber=0;
//            }
            return tableData;
        }
        public MinuteEntryInfoBean getMinuteBean( int row ){
            if( dataBeans != null && dataBeans.size() > 0 ) {
                return ( MinuteEntryInfoBean )dataBeans.elementAt( row );
            }
            return null;
        }
        
        public boolean isCellEditable( int row, int col ) {
            return false;
        }
        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
        
    }
}
