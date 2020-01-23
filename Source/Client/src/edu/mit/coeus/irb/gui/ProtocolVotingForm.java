/**
 * @(#)ProtocolVotingForm.java  1.0  March 13, 2003
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.util.Enumeration;

import edu.mit.coeus.utils.*;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

import edu.mit.coeus.irb.bean.ProtocolVoteAbsFormBean;
import edu.mit.coeus.irb.bean.AttendanceInfoBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionVoteFormBean;
import java.sql.Timestamp;
import edu.mit.coeus.irb.bean.ProtocolActionsBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
//Added for Review Comments - start
import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.irb.bean.MinuteEntryInfoBean;
//Added for Review Comments - end

/**
 * <CODE>ProtocolVotingForm </CODE>is a form object which display
 * the Protocol Voting details and it is used to <CODE> display/Modify </CODE> the Voting details.
 * This class will be instantiated from <CODE>ScheduleDetailsForm</CODE>.
 * @version 1.0 March 13, 2003
 * @author Raghunath P.V.
 */
public class ProtocolVotingForm extends javax.swing.JComponent
implements TypeConstants, ActionListener,
FocusListener, ListSelectionListener{
    
    private CoeusDlgWindow dlgParentComponent;
    private char functionType;
    private boolean saveRequired;
    private String protocolId;
    private String scheduleId;
    private int sequenceNumber;
    private String personId;
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private Vector vecAttendees;
    private Vector vecAbstainees;
    private Vector deletedAbstainees;
    private AttendanceInfoBean attendanceInfoBean;
    //private ProtocolVoteAbsFormBean protocolVoteAbsFormBean;
    private final char GET_ABSTAINEES = 'G';
    private final char GET_ATTENDEES = 'X';
    private final char UPDATE_VOTING_DETAILS = 'V';
    private ProtocolSubmissionVoteFormBean protocolSubmissionVoteFormBean;
    private int yesCount;
    private int noCount;
    private int abstained ; //prps added this line
    private String comments ;  //prps added this line
    private ProtocolVoteAbsFormBean oldProtocolVoteAbsFormBean;
    private ProtocolVoteAbsFormBean newProtocolVoteAbsFormBean;
    //holds the zero count value
    private static final int ZERO_COUNT = 0;
    private boolean firstEntry = false;
    
    //holds the last selected Row
    private int lastSelectedRow = 0;
    //holds the previous comment
    private String prvComments = "";
    
    //holds the  comment
    private String absComments = "";
    //holds the so far saved info
    private boolean isFormDataUpdated = false;
    
    // prps start june 30th 2003
    private int actionCode ;
    private HashMap hashActions ;
    private Vector vecVotingForm ;
    private Timestamp updTimestamp ;
    //prps end
    
    //prps start jul 17 2003
    private int submissionNumber ;
    private final String PROTOCOL_ACTION_SERVLET = "/protocolActionServlet";
    private final static char VALID_STATUS_CHANGE = 'T' ;
    //prps end
    //Added for Review Comments - start
    //private ReviewCommentsForm reviewCommentsForm = new ReviewCommentsForm(true);
    private ReviewCommentsForm reviewCommentsForm;
    private ProtocolSubmissionInfoBean protocolSubmissionInfoBean;
    private Vector reviewComments ;
    //Added for Review Comments - end
    
    /** Creates new form ProtocolVotingForm */
    public ProtocolVotingForm() {
        
    }
    /** Creates new form <CODE>ProtocolVotingForm</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param personBean DepartmentPersonFormBean
     * @param protocolId is the protocol number for which the voting details are to be displayed
     * @param scheduleId is the schedule number for which the voting details are to be displayed
     * @param seqNo is the seqNo number for which the voting details are to be displayed
     * 'D' specifies that the form is in Display Mode
     */
    public ProtocolVotingForm(ProtocolActionsBean actionBean, char functionType, Vector vecVotingForm, HashMap hashActions) {
        //prpr start - jul 17 2003
        this.protocolId = actionBean.getProtocolNumber() ;
        this.scheduleId = actionBean.getScheduleId() ;
        this.functionType = functionType;
        this.sequenceNumber = actionBean.getSequenceNumber() ;
        
        this.submissionNumber = actionBean.getSubmissionNumber() ;
        this.updTimestamp = actionBean.getUpdateTimestamp() ;
        this.vecVotingForm = vecVotingForm ;
        this.hashActions = hashActions ;
        this.actionCode = 0 ;
        
        //prps end
        
        deletedAbstainees = new Vector();
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
        "Protocol Vote Count - "+protocolId, true);
        dlgParentComponent.getContentPane().add(createVotingDetails(
        CoeusGuiConstants.getMDIForm()));
        dlgParentComponent.setResizable(false);
        //dlgParentComponent.pack();
        dlgParentComponent.setSize(647,427);
        
        
        Dimension screenSize
        = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgParentComponent.addEscapeKeyListener(
            new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                actionCode = 0;
                performWindowClosing();
            }
        });
        
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                cmbAction.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
                actionCode = 0;
                performWindowClosing();
                return;
            }
        });
        
        dlgParentComponent.show();
        
    }
    
    /*
     * Creates a GUI for protocol vote form and set the editors and add listeners to the components
     **/
    private JComponent createVotingDetails(CoeusAppletMDIForm mdiform){
        
        initComponents();
        java.awt.Component[] components = {cmbAction,txtYesVotes,txtNoVotes,txtAbstained,btnAdd,btnDelete,txtAreaComments,btnOk,btnCancel};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(components);
        pnlActions.setFocusTraversalPolicy(traversal);
        pnlActions.setFocusCycleRoot(true);
        
        addListenersForComponents();
        postInitComponents();
        setAcTypes();
        setEditors();
        
        populateActions() ; //prps added this line
        
        if( tblAbstainees != null && tblAbstainees.getRowCount() > ZERO_COUNT ){
            
            tblAbstainees.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
            tblAbstainees.setColumnSelectionInterval(1,1);
            
            ProtocolVoteAbsFormBean firstBean =
            (ProtocolVoteAbsFormBean)vecAbstainees.get( ZERO_COUNT );
            
            if(firstBean != null){
                //  txtAreaComments.setText( firstBean.getComments() );
            }
            
            if(functionType != DISPLAY_MODE){
                btnDelete.setEnabled(true);
            }
            
        } else {
            
            btnDelete.setEnabled(false);
            // txtAreaComments.setEnabled(false);
        }
        
        return this;
    }
    
    private void setAcTypes(){
        
        if(vecAbstainees != null){
            
            int size = vecAbstainees.size();
            ProtocolVoteAbsFormBean pVoteAbsFormBean = null;
            for(int index = 0; index < size; index++){
                
                pVoteAbsFormBean = (ProtocolVoteAbsFormBean)vecAbstainees.get(index);
                
                if(pVoteAbsFormBean != null){
                    
                    String acTye = pVoteAbsFormBean.getAcType();
                    if(acTye == null){
                        pVoteAbsFormBean.setAcType("null");
                    }
                }
            }
        }
    }
    /**
     * This method is used to validate the form components.
     * @ returns true if the validation succeeds else return false.
     **/
    private boolean validateData() throws Exception{
        
        String txtFieldYesValue = txtYesVotes.getText();
        
        int yesCountValue;
        int noCountValue = 0;
        int abstainerCountValue = 0;
        int attendeesCount = getAttendeesCount();
        if( txtFieldYesValue != null && txtFieldYesValue.trim().length() > 0){
            
            yesCountValue = new Integer(txtFieldYesValue).intValue();
            // added by manoj to check whether yescount is greater than or 1 30/08/2
            if(yesCountValue <= 0){
                errorMessage(coeusMessageResources.parseMessageKey(
                    "protocolVotedetailFrm_exceptionCode.1211"));
                txtYesVotes.requestFocus();
                return false;
            }else if( yesCountValue > attendeesCount ) {
                errorMessage(coeusMessageResources.parseMessageKey(
                    "protocolVotedetailFrm_exceptionCode.1212"));
                txtYesVotes.requestFocus();
                return false;
            }
            
        }else{
            errorMessage(coeusMessageResources.parseMessageKey(
                "protocolVotedetailFrm_exceptionCode.1211"));
            txtYesVotes.requestFocus();
            return false;
        }
        
        String txtFieldNoValue = txtNoVotes.getText();
        
        if( txtFieldNoValue != null && txtFieldNoValue.trim().length() > 0){
            
            noCountValue = new Integer(txtFieldNoValue).intValue();
            if( noCountValue > attendeesCount ) {
                errorMessage(coeusMessageResources.parseMessageKey(
                    "protocolVotedetailFrm_exceptionCode.1213"));
                txtNoVotes.requestFocus();
                return false;
            }
        }
        
        String txtFieldAbstainerValue = txtAbstained.getText();
        
        if( txtFieldAbstainerValue != null && txtFieldAbstainerValue.trim().length() > 0){
            
            abstainerCountValue = new Integer(txtFieldAbstainerValue).intValue();
            if( abstainerCountValue > attendeesCount ) {
                errorMessage(coeusMessageResources.parseMessageKey(
                    "protocolVotedetailFrm_exceptionCode.1214"));
                txtYesVotes.requestFocus();
                return false;
            }
        }
        
        if( (yesCountValue + noCountValue + abstainerCountValue) > attendeesCount ) {
            errorMessage(coeusMessageResources.parseMessageKey(
                "protocolVotedetailFrm_exceptionCode.1215"));
            txtYesVotes.requestFocus();
            return false;
        }
        return true;
    }
    
    private int getAttendeesCount() {
        int attendeesCount = 0;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
        char GET_ATTENDEES_COUNT = 'G';
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_ATTENDEES_COUNT);
        request.setDataObject(scheduleId);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                attendeesCount = ((Integer)response.getDataObject()).intValue();
            }
        }
        return attendeesCount;
    }
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     */
    
    private void errorMessage(String mesg){
        CoeusOptionPane.showInfoDialog(mesg);
    }
    
    /**
     * This method is used to set the editors for the JTable.
     * And is used to disable the components in the form based on the function type.
     */
    private void setEditors(){
        
        tblAbstainees.getTableHeader().setReorderingAllowed(false);
        tblAbstainees.getTableHeader().setResizingAllowed(false);
        tblAbstainees.setFont(CoeusFontFactory.getNormalFont());
        tblAbstainees.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblAbstainees.getTableHeader().setVisible(false);
        
        TableColumn column = tblAbstainees.getColumnModel().getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        scrPnCommentsContainer.setViewportView(txtAreaComments);
        
        if(functionType == DISPLAY_MODE){
            
            tblAbstainees.setEnabled(false);
            
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
            btnOk.setEnabled(false);
            
            txtNoVotes.setEnabled(false);
            txtYesVotes.setEnabled(false);
            txtNoVotes.setEditable(false);
            txtYesVotes.setEditable(false);
            txtAreaComments.setEnabled(false);
        }
    }
    /**
     * This method is used to add listeners to all the buttons and the jtable model.
     */
    private void addListenersForComponents(){
        
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        
        ListSelectionModel abstaineeSelectionModel = tblAbstainees.getSelectionModel();
        abstaineeSelectionModel.addListSelectionListener( this );
        abstaineeSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        
        txtAreaComments.addFocusListener( this );
    }
    /**
     * This method is used to get all the values and populate in the Form.
     */
    private void postInitComponents(){
        
        ProtocolSubmissionVoteFormBean locProtocolSubmissionVoteFormBean = null;
        vecAttendees = getAttendenceList();
        
        locProtocolSubmissionVoteFormBean = getAbstaineesList();
        
        if(locProtocolSubmissionVoteFormBean != null){
            
            vecAbstainees = (Vector)locProtocolSubmissionVoteFormBean.getProtocolVoteAbstainee();
            
            yesCount = locProtocolSubmissionVoteFormBean.getYesVoteCount()<=0? 0 :locProtocolSubmissionVoteFormBean.getYesVoteCount() ;
            noCount = locProtocolSubmissionVoteFormBean.getNoVoteCount() <=0? 0 : locProtocolSubmissionVoteFormBean.getNoVoteCount() ;
            abstained = locProtocolSubmissionVoteFormBean.getAbstainerCount() <=0? 0 : locProtocolSubmissionVoteFormBean.getAbstainerCount() ;
            comments = locProtocolSubmissionVoteFormBean.getVotingComments() == null ? "" : locProtocolSubmissionVoteFormBean.getVotingComments() ;
        }
        
       
        setFormData();
    }
    
    /**
     * This method is used to set all the values in the Vote form
     */
    private void setFormData(){
        
        ProtocolVoteAbsFormBean pVoteAbsFormBean = null;
        
        if(yesCount != 0){
            
            txtYesVotes.setText(new Integer(yesCount).toString());
            
        }else{
            txtYesVotes.setText(new Integer(0).toString());
            
        }
        
        if(noCount != 0){
            
            txtNoVotes.setText(new Integer(noCount).toString());
            
        }else{
            
            txtNoVotes.setText(new Integer(0).toString());
            
        }
        
        //prps start
        if(abstained != 0){
            
            txtAbstained.setText(new Integer(abstained).toString());
            
        }else{
            
            txtAbstained.setText(new Integer(0).toString());
            
        }
        //prps end
        txtAreaComments.setText( comments );
        if(vecAbstainees != null){
            
            Vector vcDataPopulate = new Vector();
            Vector vcData;
            int size = vecAbstainees.size();
            
            for(int index = 0; index < size; index++){
                
                pVoteAbsFormBean = (ProtocolVoteAbsFormBean)vecAbstainees.get(index);
                
                if(pVoteAbsFormBean != null){
                    
                    String abstaineeId = pVoteAbsFormBean.getPersonId();
                    String abstaineeName = pVoteAbsFormBean.getPersonName();
                    
                    vcData = new Vector();
                    vcData.addElement(abstaineeId == null ? "" : abstaineeId);
                    vcData.addElement(abstaineeName == null ? "" : abstaineeName);
                    
                    vcDataPopulate.addElement(vcData);
                    
                }
            }
            
            ((DefaultTableModel)tblAbstainees.getModel()).
            setDataVector(vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblAbstainees.getModel()).
            fireTableDataChanged();
            
        }
        
    }
    /**
     * This helper method is used to get all the column names of the JTable.
     */
    private Vector getColumnNames(){
        
        Enumeration enumColNames = tblAbstainees.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        
        while(enumColNames.hasMoreElements()){
            
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
            
        }
        
        return vecColNames;
        
    }
    
    /**
     * This method contains logic needed to update the values in the database.
     */
    private void updateVotingDetails(ProtocolSubmissionVoteFormBean voteBeanToServer) throws Exception{
        
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL
        + "/protocolMntServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(UPDATE_VOTING_DETAILS);
        //request.setId(personId);
        //Added for Review Comments - start
        //request.setDataObject(voteBeanToServer);
        Vector dataObjects = new Vector();
        dataObjects.addElement(voteBeanToServer);
        dataObjects.addElement(reviewComments);
        request.setDataObjects(dataObjects);
       //Added for Review Comments - end
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                //Get Review Comments data back after updating
                reviewComments = response.getDataObjects();
                dlgParentComponent.dispose();
            }
        }
    }
    
    /**
     * This method is used to get the ProtocolSubmissionVoteFormBean which is
     * populated taking the values from the form.
     */
    private ProtocolSubmissionVoteFormBean getProtocolVoteDetails(){
        
        int yesCountValue;
        int noCountValue;
        int abstainedValue ; //prps added this line
        String commentsValue ; //prps added this line
        
        String txtFieldYesValue = txtYesVotes.getText();
        
        if( txtFieldYesValue != null && txtFieldYesValue.trim().length() > 0){
            yesCountValue = new Integer(txtFieldYesValue).intValue();
        }else{
            yesCountValue = 0;
        }
        
        if(yesCount != yesCountValue){
            saveRequired = true;
            protocolSubmissionVoteFormBean.setAcType(UPDATE_RECORD);
        }
        
        String txtFieldNoValue = txtNoVotes.getText();
        
        if(txtFieldNoValue != null && txtFieldNoValue.trim().length() > 0){
            noCountValue = new Integer(txtFieldNoValue).intValue();
        }else{
            noCountValue = 0;
        }
        
        if(noCount != noCountValue){
            saveRequired = true;
            protocolSubmissionVoteFormBean.setAcType(UPDATE_RECORD);
        }
        
        //prps start jul 2nd 2003
        String txtFieldAbstainedValue = txtAbstained.getText();
        
        if(txtFieldAbstainedValue != null && txtFieldAbstainedValue.trim().length() > 0){
            abstainedValue = new Integer(txtFieldAbstainedValue).intValue();
        }else{
            abstainedValue = 0;
        }
        
        if(abstained != abstainedValue){
            saveRequired = true;
            protocolSubmissionVoteFormBean.setAcType(UPDATE_RECORD);
        }
        
        String txtFieldAreaCommentsValue = txtAreaComments.getText(); 
        
        if(txtFieldAreaCommentsValue != null && txtFieldAreaCommentsValue.trim().length() > 0){
            commentsValue = txtFieldAreaCommentsValue;
        }else{
            commentsValue = "";
        }
        
        if(comments != commentsValue){
            saveRequired = true;
            protocolSubmissionVoteFormBean.setAcType(UPDATE_RECORD);
        }
        
        protocolSubmissionVoteFormBean.setAbstainerCount(abstainedValue) ;
        protocolSubmissionVoteFormBean.setVotingComments(commentsValue) ;
        //prps end
        protocolSubmissionVoteFormBean.setProtocolNumber(protocolId);
        protocolSubmissionVoteFormBean.setYesVoteCount(yesCountValue);
        protocolSubmissionVoteFormBean.setNoVoteCount(noCountValue);
        
        //prps start jul 15 2003
        protocolSubmissionVoteFormBean.setScheduleId(scheduleId) ;
        protocolSubmissionVoteFormBean.setSequenceNumber(sequenceNumber) ;
        //Modified for COEUSQA-2502 : When multiple submissions for the same protocol are submitted vote counts are not getting saved - Start
//        protocolSubmissionVoteFormBean.setSubmissionNumber(getMaxSubmissionNumber(protocolId)) ;
        protocolSubmissionVoteFormBean.setSubmissionNumber(submissionNumber);
        //COEUSQA-2502 : End
        protocolSubmissionVoteFormBean.setUpdateTimestamp(updTimestamp) ;
        //prps end
        
        if(deletedAbstainees != null){
            
            int delSize = deletedAbstainees.size();
            ProtocolVoteAbsFormBean prVoteAbsFormBean = null;
            
            for(int index = 0; index < delSize; index++){
                
                prVoteAbsFormBean = (ProtocolVoteAbsFormBean)deletedAbstainees.get(index);
                
                if(prVoteAbsFormBean != null){
                    
                    vecAbstainees.insertElementAt(prVoteAbsFormBean,index);
                    
                }
            }
        }
        protocolSubmissionVoteFormBean.setProtocolVoteAbstainee(vecAbstainees);
        
        return protocolSubmissionVoteFormBean;
    }
    
    /**
     * This method is used to get all the attendees based on the schdule id.
     * And the values are populated to the AttendeesLookupWindow.
     */
    private Vector getAttendenceList(){
        
        Vector attendeesList = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
        
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType(GET_ATTENDEES);
        request.setId(scheduleId);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                attendeesList = (Vector)response.getDataObject();
            }
        }
        
        return attendeesList;
        
    }
    
    /**
     * This method is used to get all the abstainees based on the protocolid.
     * And the values are populated to the Abstainee JTable.
     */
    private ProtocolSubmissionVoteFormBean getAbstaineesList(){
        
        Vector abstaineesList = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_ABSTAINEES);
        request.setId(protocolId);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                
                protocolSubmissionVoteFormBean = (ProtocolSubmissionVoteFormBean)response.getDataObject();
            }
        }
        return protocolSubmissionVoteFormBean;
    }
    
    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    public boolean isSaveRequired(){
        
        return saveRequired;
        
    }
    /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean saveRequired){
        
        this.saveRequired = saveRequired;
        
    }
    
    /** Method to get the functionType
     * @return a <CODE>Char</CODE> representation of functionType.
     */
    public char getFunctionType(){
        
        return this.functionType;
        
    }
    /** Method to set the functionType
     * @param fType is functionType to be set like 'D', 'I', 'M'
     */
    public void setFunctionType(char fType){
        
        this.functionType = fType;
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlButton = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnReviewComments = new javax.swing.JButton();
        pnlActions = new javax.swing.JPanel();
        lblYesVotes = new javax.swing.JLabel();
        lblNoVotes = new javax.swing.JLabel();
        txtYesVotes = new edu.mit.coeus.utils.CoeusTextField();
        txtNoVotes = new edu.mit.coeus.utils.CoeusTextField();
        lblAbstained = new javax.swing.JLabel();
        txtAbstained = new edu.mit.coeus.utils.CoeusTextField();
        lblAction = new javax.swing.JLabel();
        cmbAction = new edu.mit.coeus.utils.CoeusComboBox();
        scrPnAbstainees = new javax.swing.JScrollPane();
        tblAbstainees = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        scrPnCommentsContainer = new javax.swing.JScrollPane();
        txtAreaComments = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), "Voting Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        setPreferredSize(new java.awt.Dimension(575, 400));
        pnlButton.setLayout(new java.awt.GridBagLayout());

        pnlButton.setMinimumSize(new java.awt.Dimension(160, 86));
        pnlButton.setPreferredSize(new java.awt.Dimension(160, 86));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        pnlButton.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 2);
        pnlButton.add(btnCancel, gridBagConstraints);

        btnReviewComments.setFont(CoeusFontFactory.getLabelFont());
        btnReviewComments.setMnemonic('R');
        btnReviewComments.setText("Review Comments");
        btnReviewComments.setToolTipText("");
        btnReviewComments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReviewCommentsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 3);
        pnlButton.add(btnReviewComments, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 17;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlButton, gridBagConstraints);

        pnlActions.setLayout(new java.awt.GridBagLayout());

        pnlActions.setPreferredSize(new java.awt.Dimension(342, 25));
        lblYesVotes.setFont(CoeusFontFactory.getLabelFont());
        lblYesVotes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblYesVotes.setText("Yes Votes :");
        lblYesVotes.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblYesVotes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 4, 4);
        pnlActions.add(lblYesVotes, gridBagConstraints);

        lblNoVotes.setFont(CoeusFontFactory.getLabelFont());
        lblNoVotes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoVotes.setText("No Votes :");
        lblNoVotes.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 59, 0, 0);
        pnlActions.add(lblNoVotes, gridBagConstraints);

        txtYesVotes.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtYesVotes.setFont(CoeusFontFactory.getNormalFont());
        txtYesVotes.setMaximumSize(new java.awt.Dimension(50, 23));
        txtYesVotes.setMinimumSize(new java.awt.Dimension(50, 23));
        txtYesVotes.setPreferredSize(new java.awt.Dimension(50, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 3, 0);
        pnlActions.add(txtYesVotes, gridBagConstraints);

        txtNoVotes.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtNoVotes.setFont(CoeusFontFactory.getNormalFont());
        txtNoVotes.setMaximumSize(new java.awt.Dimension(50, 23));
        txtNoVotes.setMinimumSize(new java.awt.Dimension(50, 23));
        txtNoVotes.setPreferredSize(new java.awt.Dimension(50, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 3, 4);
        pnlActions.add(txtNoVotes, gridBagConstraints);

        lblAbstained.setFont(CoeusFontFactory.getLabelFont());
        lblAbstained.setText("Abstained :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 2, 0, 3);
        pnlActions.add(lblAbstained, gridBagConstraints);

        txtAbstained.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAbstained.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        pnlActions.add(txtAbstained, gridBagConstraints);

        lblAction.setFont(CoeusFontFactory.getLabelFont());
        lblAction.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAction.setText(coeusMessageResources.parseMessageKey("scheduleDetFrm_exceptionCode.2207")+" :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 5);
        pnlActions.add(lblAction, gridBagConstraints);

        cmbAction.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbAction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbActionItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 84;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        pnlActions.add(cmbAction, gridBagConstraints);

        scrPnAbstainees.setMaximumSize(new java.awt.Dimension(350, 200));
        scrPnAbstainees.setMinimumSize(new java.awt.Dimension(350, 200));
        scrPnAbstainees.setPreferredSize(new java.awt.Dimension(350, 130));
        tblAbstainees.setFont(CoeusFontFactory.getNormalFont());
        tblAbstainees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
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
        tblAbstainees.setCellSelectionEnabled(true);
        scrPnAbstainees.setViewportView(tblAbstainees);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 28;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlActions.add(scrPnAbstainees, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 5, 0, 2);
        pnlActions.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 2);
        pnlActions.add(btnDelete, gridBagConstraints);

        jLabel1.setFont(CoeusFontFactory.getLabelFont());
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Abstainers :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 4);
        pnlActions.add(jLabel1, gridBagConstraints);

        txtAreaComments.setDocument(new LimitedPlainDocument( 2000 ));
        txtAreaComments.setFont(CoeusFontFactory.getNormalFont());
        txtAreaComments.setLineWrap(true);
        txtAreaComments.setWrapStyleWord(true);
        txtAreaComments.setPreferredSize(new java.awt.Dimension(420, 100));
        scrPnCommentsContainer.setViewportView(txtAreaComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlActions.add(scrPnCommentsContainer, gridBagConstraints);

        jLabel2.setFont(CoeusFontFactory.getLabelFont());
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(" Voting Comments :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlActions.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 122;
        gridBagConstraints.ipady = 345;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pnlActions, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnReviewCommentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReviewCommentsActionPerformed
        try{
            if( reviewCommentsForm == null ) {
                reviewCommentsForm = new ReviewCommentsForm(true);
                reviewCommentsForm.setSaveToDatabase(false);
                reviewCommentsForm.setLockSchedule(false);
                RequesterBean requesterBean = new RequesterBean();  
                
                protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
                protocolSubmissionInfoBean.setProtocolNumber(protocolId);
                protocolSubmissionInfoBean.setSequenceNumber(sequenceNumber);
                protocolSubmissionInfoBean.setSubmissionNumber(submissionNumber);
                
                requesterBean.setDataObject(protocolSubmissionInfoBean);
                requesterBean.setFunctionType('T');
                AppletServletCommunicator comm = new AppletServletCommunicator(
                    CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt", 
                    requesterBean);
                comm.send();
                ResponderBean responderBean = comm.getResponse();
                if(! responderBean.isSuccessfulResponse()){
                   reviewCommentsForm.setFunctionType(TypeConstants.DISPLAY_MODE); 
                }else {
                   reviewCommentsForm.setFunctionType(TypeConstants.MODIFY_MODE); 
                }
                Vector dataObjects = responderBean.getDataObjects();
                protocolSubmissionInfoBean = ( ProtocolSubmissionInfoBean ) dataObjects.get(0);
            }
            if( reviewComments == null ) {
                reviewCommentsForm.setFormData(protocolId, submissionNumber, sequenceNumber);
            }else{
                reviewCommentsForm.setFormData(protocolSubmissionInfoBean, 
                    (Vector)ObjectCloner.deepCopy(reviewComments));
            }          
            reviewCommentsForm.display();            
            if( reviewCommentsForm.isSaveRequired() ) {
                reviewComments = reviewCommentsForm.getData();
                setSaveRequired(true);
            }            
        }catch(Exception ex) {
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }            
            /*            
        try{
            reviewCommentsForm.setLockSchedule(false);
            reviewCommentsForm.setSaveToDatabase(false);
            //reviewCommentsForm.setFormData(protocolSubmissionInfoBean.getProtocolNumber(), protocolSubmissionInfoBean.getSubmissionNumber(), protocolSubmissionInfoBean.getSequenceNumber());
            reviewCommentsForm.setFormData(protocolSubmissionInfoBean, 
                (Vector)ObjectCloner.deepCopy(protocolSubmissionInfoBean.getProtocolReviewComments()));            
            reviewCommentsForm.display();           
            if(reviewCommentsForm.isSaveRequired()){
                protocolSubmissionInfoBean.setProtocolReviewComments(reviewCommentsForm.getData());
                setSaveRequired(true);
            }else{
                protocolSubmissionInfoBean.setProtocolReviewComments(null);
            }
        }catch(Exception ex) {
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }*/
    }//GEN-LAST:event_btnReviewCommentsActionPerformed

    private void cmbActionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbActionItemStateChanged
        // Add your handling code here:
        ComboBoxBean comboBoxBean = (ComboBoxBean)cmbAction.getSelectedItem() ;
        String selectedItem = comboBoxBean.getCode();
        if(selectedItem != null && !selectedItem.equals("0")){        
            /*if(selectedItem.equals("202") || selectedItem.equals("203")){
                btnReviewComments.setEnabled(true);
            }else{
                btnReviewComments.setEnabled(false);
            }*/
            btnReviewComments.setEnabled(true);
        }else{
            btnReviewComments.setEnabled(false);
        }
    }//GEN-LAST:event_cmbActionItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnReviewComments;
    private edu.mit.coeus.utils.CoeusComboBox cmbAction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAbstained;
    private javax.swing.JLabel lblAction;
    private javax.swing.JLabel lblNoVotes;
    private javax.swing.JLabel lblYesVotes;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JScrollPane scrPnAbstainees;
    private javax.swing.JScrollPane scrPnCommentsContainer;
    private javax.swing.JTable tblAbstainees;
    private edu.mit.coeus.utils.CoeusTextField txtAbstained;
    private javax.swing.JTextArea txtAreaComments;
    private edu.mit.coeus.utils.CoeusTextField txtNoVotes;
    private edu.mit.coeus.utils.CoeusTextField txtYesVotes;
    // End of variables declaration//GEN-END:variables
    
    /**
     * This is the method which is used to show the AttendeesLookupWindow
     * and get the results which are selected in the AttendeesLookupWindow
     */
    private void showAttendeesLookUpWindow(Vector vecAdees, int ind){
        
        Vector vecDataFromLookupWindow = null;
        
        AttendeesLookupWindow attendeesLookupWindow =
        new AttendeesLookupWindow(vecAdees);
        
        vecDataFromLookupWindow = attendeesLookupWindow.getSelectedAttendees();
        
        ProtocolVoteAbsFormBean prVoteBean = null;
        
        if(vecDataFromLookupWindow != null){
            
            AttendanceInfoBean attendanceBean = null;
            
            int atdSize = vecDataFromLookupWindow.size();
            
            for(int index = 0; index < atdSize; index++){
                
                attendanceBean = (AttendanceInfoBean)vecDataFromLookupWindow.elementAt(index);
                
                if( attendanceBean != null ){
                    
                    String attId = attendanceBean.getPersonId();
                    String attName = attendanceBean.getPersonName();
                    boolean nonEmployeeFlag = attendanceBean.getNonEmployeeFlag();
                    
                    if( tblAbstainees.getRowCount() == 0 ){
                        
                        vecAbstainees = new Vector();
                        Vector newAttendeeEntry = new Vector();
                        prVoteBean = new ProtocolVoteAbsFormBean();
                        
                        newAttendeeEntry.addElement( attId );
                        newAttendeeEntry.addElement( attName );
                        newAttendeeEntry.addElement( new Boolean(nonEmployeeFlag) );
                        
                        prVoteBean = new ProtocolVoteAbsFormBean();
                        
                        prVoteBean.setPersonId(attId);
                        prVoteBean.setPersonName(attName);
                        prVoteBean.setProtocolNumber(protocolId);
                        prVoteBean.setSequenceNumber(sequenceNumber);
                        prVoteBean.setScheduleId(scheduleId);
                        prVoteBean.setNonEmployeeFlag(nonEmployeeFlag);
                        prVoteBean.setAcType(INSERT_RECORD);
                        
                        vecAbstainees.addElement(prVoteBean);
                        
                        ((DefaultTableModel)tblAbstainees.getModel()
                        ).addRow( newAttendeeEntry );
                        ((DefaultTableModel)tblAbstainees.getModel()).
                        fireTableDataChanged();
                        
                        
                        int rowCnt = tblAbstainees.getRowCount();
                        
                        if(rowCnt > 0){
                            btnDelete.setEnabled(true);
                            //txtAreaComments.setEnabled(true);
                        }else{
                            btnDelete.setEnabled(false);
                            //txtAreaComments.setEnabled(false);
                        }
                        
                        int newRowAdded = vecAbstainees.size() - 1;
                        lastSelectedRow = newRowAdded;
                        tblAbstainees.scrollRectToVisible( tblAbstainees.getCellRect(
                        newRowAdded ,0, true));
                        
                        saveRequired=true;
                        ind = 0;
                        continue;
                    }
                    
                    boolean duplicate = checkDuplicatePerson(attId);
                    Vector vecAttInfo = null;
                    
                    if(!duplicate){
                        
                        vecAttInfo = new Vector();
                        
                        vecAttInfo.addElement( attId );
                        vecAttInfo.addElement( attName );
                        ProtocolVoteAbsFormBean proVoteBean = null;
                        
                        if(vecAbstainees !=  null){
                            
                            proVoteBean = new ProtocolVoteAbsFormBean();
                            proVoteBean.setPersonId(attId);
                            proVoteBean.setPersonName(attName);
                            proVoteBean.setNonEmployeeFlag(nonEmployeeFlag);
                            proVoteBean.setProtocolNumber(protocolId);
                            proVoteBean.setSequenceNumber(sequenceNumber);
                            proVoteBean.setScheduleId(scheduleId);
                            proVoteBean.setAcType(INSERT_RECORD);
                            
                            vecAbstainees.addElement(proVoteBean);
                        }
                        
                        ((DefaultTableModel)tblAbstainees.getModel()).
                        addRow(vecAttInfo);
                        ((DefaultTableModel)tblAbstainees.getModel()).
                        fireTableDataChanged();
                        
                        int newRowCount = tblAbstainees.getRowCount();
                        int rowCnt = tblAbstainees.getRowCount();
                        
                        tblAbstainees.setRowSelectionInterval(  rowCnt -1, rowCnt -1 );
                        tblAbstainees.setColumnSelectionInterval(1,1);
                        
                        if(rowCnt > 0){
                            btnDelete.setEnabled(true);
                            //txtAreaComments.setEnabled(true);
                        }else{
                            btnDelete.setEnabled(false);
                            //txtAreaComments.setText("");
                            //txtAreaComments.setEnabled(false);
                        }
                        
                        tblAbstainees.getSelectionModel().setSelectionInterval(
                        newRowCount - 1, newRowCount - 1);
                        saveRequired = true;
                        
                    }
                    else{
                        //System.out.println("Is it here in TWO");
                        CoeusOptionPane.showErrorDialog("' " + attName + "' " +
                        coeusMessageResources.parseMessageKey(
                        "general_duplicateNameCode.2277"));
                    }
                }
            }
            //prps code start - Apr 5 2004
           // Case 667
                int rowsAdded = tblAbstainees.getRowCount();
                if(rowsAdded > 0)
                {
                   txtAbstained.setText(String.valueOf(rowsAdded)) ;
                   txtAbstained.setEnabled(false) ;
                 }
                 else
                 {
                    txtAbstained.setEnabled(true) ;
                 }
            //prps code end - Apr 5 2004     
        }
    }
    /**
     * This is the method which is used to delete the Abstainee from the Abstainee table.
     */
    private void deleteFromAbstaineeTable(){
        
        int totalRows = tblAbstainees.getRowCount();
        /* If there are more than one row in table then delete it */
        if (totalRows > 0) {
            /* get the selected row */
            int selectedRow = tblAbstainees.getSelectedRow();
            
            if (selectedRow != -1) {
                int selectedOption = CoeusOptionPane.
                showQuestionDialog(
                coeusMessageResources.parseMessageKey(
                "protocolVotedetailFrm_delConfirmCode.1050"),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                // if Yes then selectedOption is 0
                // if No then selectedOption is 1
                if (0 == selectedOption) {
                    ProtocolVoteAbsFormBean prVoAbsFormBean = null;
                    if(vecAbstainees != null){
                        prVoAbsFormBean =
                        (ProtocolVoteAbsFormBean) vecAbstainees.get( selectedRow );
                    }
                    
                    if( (prVoAbsFormBean.getAcType() != null ) &&
                    ( ! prVoAbsFormBean.getAcType().equalsIgnoreCase(INSERT_RECORD) )) {
                        
                        deletedAbstainees.addElement( prVoAbsFormBean );
                    }
                    
                    if( prVoAbsFormBean != null ){
                        prVoAbsFormBean.setAcType( DELETE_RECORD );
                        saveRequired = true;
                    }
                    
                    vecAbstainees.removeElementAt( selectedRow );
                    
                    ((DefaultTableModel)
                    tblAbstainees.getModel()).removeRow(selectedRow);
                    
                    ((DefaultTableModel)
                    tblAbstainees.getModel()).fireTableDataChanged();
                    
                    saveRequired = true;
                    
                    if( tblAbstainees.getRowCount() <= 0 ){
                        btnDelete.setEnabled( false );
                        //prps code start - Apr 5 2004
                            // Case 667
                            txtAbstained.setText("0") ;
                            txtAbstained.setEnabled(true) ;
                         //prps code end - Apr 5 2004
                            
                        //txtAreaComments.setText("");
                        //txtAreaComments.setEnabled(false);
                    }else{
                        btnDelete.setEnabled( true );
                        //prps code start - Apr 5 2004
                            // Case 667
                            txtAbstained.setText(String.valueOf(tblAbstainees.getRowCount())) ;
                            txtAbstained.setEnabled(false) ;
                            
                        //prps code end - Apr 5 2004
                            
                        //txtAreaComments.setEnabled(true);
                        tblAbstainees.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
                        tblAbstainees.setColumnSelectionInterval(1,1);
                        ProtocolVoteAbsFormBean firstBean =
                        (ProtocolVoteAbsFormBean)vecAbstainees.get( 0 );
                        if(firstBean != null){
                            //  txtAreaComments.setText( firstBean.getComments() );
                        }
                    }
                }
                
            }else{
                // if total rows >0 and row is not selected
                CoeusOptionPane.
                showErrorDialog(
                coeusMessageResources.parseMessageKey(
                "protoFndSrcFrm_exceptionCode.1057"));
            }
        }
        
    }
    
    /**
     *  Method used to validate whether the abstainee is duplicate or not
     */
    private boolean checkDuplicatePerson(String Id){
        
        boolean duplicate = false;
        
        String oldId = "";
        
        int size = tblAbstainees.getRowCount();
        
        for(int rowIndex = 0; rowIndex < size; rowIndex++){
            
            oldId = (String)tblAbstainees.getValueAt(rowIndex,0);
            
            if(oldId != null){
                if(oldId.equalsIgnoreCase(Id)){
                    duplicate = true;
                    break;
                }
            }
        }
        
        return duplicate;
    }
    
    /**
     * This method is used to perform the Window closing operation
     */
    private void performWindowClosing(){
        
        ProtocolSubmissionVoteFormBean voteBeanToServer = getProtocolVoteDetails();
        
        int option = JOptionPane.NO_OPTION;
        
        if(functionType != DISPLAY_MODE ){
            
            if(isSaveRequired()){
                
                option
                = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                
            }
            
            if(option == JOptionPane.YES_OPTION){
                
                try{
                    boolean succ = validateData();
                    
                    if(succ){
                        updateVotingDetails(voteBeanToServer);
                        dlgParentComponent.dispose();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
                
            }else if(option == JOptionPane.NO_OPTION){
                saveRequired = false;
                dlgParentComponent.dispose();
                
            }else if(option == JOptionPane.CANCEL_OPTION){
                
                //saveRequired = false;
                //dlgParentComponent.dispose();
                
                txtYesVotes.requestFocus();
                return;
            }
        }else{
            
            dlgParentComponent.dispose();
            
        }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object actSource = actionEvent.getSource();
        if(actSource.equals(btnAdd)){
            
            int inIndex = tblAbstainees.getSelectedRow();
            showAttendeesLookUpWindow(vecAttendees, inIndex);
            
        }else if(actSource.equals(btnDelete)){
            
            deleteFromAbstaineeTable();
            
        }else if(actSource.equals(btnOk)){
            
            try{
                if (isActionSelected()) //prps added this if condition to make action mandatory
                {
                    // added by manoj to validate yes count greater than 0 on 30/08/2003
                    if(validateData()){
                        /* Case 651
                         * Saving voting details will be performed after validation - prahalad Mar 12 2004
                         * 
                        */
                        ProtocolSubmissionVoteFormBean voteBeanToServer = getProtocolVoteDetails();
                        if(saveRequired){
//                            boolean succ = validateData();
//                            if(succ){
                                //prps added this if condtn - jul 17 2003
                                if (!serverSideValidation(actionCode)) {
                                    return ;
                                }
                                else {
                                    voteBeanToServer = getProtocolVoteDetails();
                                    updateVotingDetails(voteBeanToServer);
                                    dlgParentComponent.dispose();
                                }
                                
//                            }
                        }
                        dlgParentComponent.dispose();
                    }
                }
                
            }catch(Exception e){
                e.printStackTrace();
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
            
        }else if(actSource.equals(btnCancel)){
            actionCode = 0; //prps added this line
            performWindowClosing();
        }
    }
    
    //supporting method to show the warning message
    private void showWarningMessage(){
        if( functionType != DISPLAY_MODE ) {
            CoeusOptionPane.showWarningDialog(
            coeusMessageResources.parseMessageKey(
            "protocolVoteDetailFrm_exceptionCode.1053"));
        }
    }
    
    //prps commenting start - (only code inside the method is commented) Each row in the abstainers table will not have comments
    //                        there will be only one comment for the protocol
    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        ////        Object source = focusEvent.getSource();
        ////        int selectedRow = tblAbstainees.getSelectedRow();
        ////        if( source.equals( txtAreaComments )){
        ////            if( selectedRow == -1 && tblAbstainees.getRowCount() > 0 ){
        ////                showWarningMessage();
        ////                tblAbstainees.requestFocus();
        ////            }
        ////        }
        ////        prvComments = txtAreaComments.getText();
    }
    
    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        
        ////        if ( !focusEvent.isTemporary()) {
        ////
        ////            Object source = focusEvent.getSource();
        ////            int selectedRow = lastSelectedRow;
        ////            tblAbstainees.getSelectedRow();
        ////
        ////            /*if( source.equals( txtArComments ) &&
        ////                selectedRow != -1  ){                */
        ////            if( source.equals( txtAreaComments ) &&
        ////                        lastSelectedRow <= tblAbstainees.getRowCount()  ){
        ////
        ////                ProtocolVoteAbsFormBean prBean = null;
        ////
        ////                if(vecAbstainees != null){
        ////                    prBean = (ProtocolVoteAbsFormBean )vecAbstainees.get(selectedRow);
        ////                }
        ////
        ////                if(prBean != null){
        ////                    prBean.setComments( txtAreaComments.getText() );
        ////                    //Changes made check from equals to not equals
        ////                    if( ( prBean.getAcType() != null ) ||
        ////                                    ( !prBean.getAcType().equalsIgnoreCase(INSERT_RECORD) )){
        ////
        ////                        if(prvComments != null && prvComments.trim().length() > 0){
        ////
        ////                            if( !prvComments.equals( txtAreaComments.getText() ) ) {
        ////                                prBean.setAcType( UPDATE_RECORD );
        ////                                saveRequired = true;
        ////                            }
        ////                        }
        ////                    }
        ////
        ////                    if(vecAbstainees != null){
        ////                        vecAbstainees.setElementAt(prBean,selectedRow);
        ////                    }
        ////                }
        ////
        ////            }
        ////        }
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        
        ////        int selectedRow = tblAbstainees.getSelectedRow();
        ////        String comment = null;
        ////        int rowCount =  tblAbstainees.getRowCount();
        ////
        ////        if( selectedRow >= 0  && selectedRow <= rowCount &&
        ////            firstEntry && vecAbstainees != null){
        ////
        ////                ProtocolVoteAbsFormBean curBean = (ProtocolVoteAbsFormBean)
        ////                                                    vecAbstainees.get( selectedRow );
        ////                if( txtAreaComments.hasFocus() ){
        ////                     curBean = (ProtocolVoteAbsFormBean)
        ////                                        vecAbstainees.get(  lastSelectedRow );
        ////                     curBean.setComments( txtAreaComments.getText() );
        ////                     return;
        ////
        ////               }else{
        ////                    lastSelectedRow = selectedRow;
        ////               }
        ////
        ////               if( curBean != null ){
        ////                    comment = curBean.getComments();
        ////                    if( curBean.getAcType() == null ){
        ////                        //curBean.setAcType(UPDATE_RECORD);
        ////                    }
        ////                    if( comment == null){
        ////                        comment = "";
        ////                    }
        ////                    txtAreaComments.setText( comment );
        ////                }
        ////            }
        ////        firstEntry = true ;
    }
    
    //prps commenting end
    
    // prps start june 30th 2003
    public int getActionCode() {
        return this.actionCode ;
    }
    
    public void setActionCode(int actionCode) {
        this.actionCode = actionCode ;
    }
    
    
    public void setActionMenuItems(HashMap hashActions, Vector vecVotingForm) {
        this.hashActions = hashActions ;
        this.vecVotingForm = vecVotingForm ;
    }
    
    private boolean isActionSelected() {
        ComboBoxBean comboBoxBean = (ComboBoxBean)cmbAction.getSelectedItem() ;
        if (comboBoxBean.getCode().equals("0")) {
            CoeusOptionPane.showErrorDialog("Action is not selected") ;
            return false ;
        }
        else {
            actionCode = Integer.parseInt(comboBoxBean.getCode()) ;
        }
        return true ;
    }
    
    
    private void populateActions() {
        // Add a blank item
        ComboBoxBean comboBoxBean = new ComboBoxBean("0","         ");
        cmbAction.addItem(comboBoxBean) ;
        
        for (int i=0; i< vecVotingForm.size() ; i++) {
            String code = vecVotingForm.get(i).toString() ;
            comboBoxBean = new ComboBoxBean(code, hashActions.get(code).toString().trim()) ;
            cmbAction.addItem(comboBoxBean) ;
        }
    }
    
    //prps end
    
    //prps start jul 15 2003
    private static final char PROTO_MAX_SUB_NUM = 'X' ;
    
    
    /**
     * This method is used to get the max submission number for a particular protocol
     * from OSP$Protocol_submission table, as every submission needs to increment the
     * submission_number field in this table.
     *
     * @param protocolId
     *
     * @return max submission number
     */
    private int getMaxSubmissionNumber( String protocolId ) {
        String connectTo = CoeusGuiConstants.CONNECTION_URL
        + "/protocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROTO_MAX_SUB_NUM);
        request.setId(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        Integer count = new Integer(0);
        if (response.isSuccessfulResponse()) {
            count = (Integer)((Vector) response.getDataObjects()).elementAt(0);
        }
        if(count != null){
            return count.intValue();
        }
        return 0;
    }
    
    //prps end
    
    //prps start - jul 17 2003
    
    
    // This function will perform the validation at server side before an action is performed
    private boolean serverSideValidation(int actionCode) {
        //Check if review comments has been entered here - start
        if(protocolSubmissionInfoBean!=null){
            boolean valid = false;
            if(actionCode==203 || actionCode==202){
                if((protocolSubmissionInfoBean.getSubmissionStatusCode()==100 || protocolSubmissionInfoBean.getSubmissionStatusCode() == 101)
                    && reviewComments!=null && reviewComments.size() > 0){
                        for(int row = 0; row < reviewComments.size() ; row++){
                            MinuteEntryInfoBean minuteEntryInfoBean = (MinuteEntryInfoBean)reviewComments.elementAt(row);
                            if(minuteEntryInfoBean.getAcType()!=null && !minuteEntryInfoBean.getAcType().equalsIgnoreCase("D")){
                                valid = true;
                                break;
                            }
                        }
                }
            }
            if(valid){
                return true;
            }
        }
        //Check if review comments has been entered here - end
        
        System.out.println(" ** System side validation in progress ** ") ;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_ACTION_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType(VALID_STATUS_CHANGE);
        
        ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
        actionBean.setActionTypeCode(actionCode) ;
        actionBean.setScheduleId(scheduleId) ;
        actionBean.setSequenceNumber(sequenceNumber) ;
        actionBean.setProtocolNumber(protocolId) ;
        actionBean.setSubmissionNumber(submissionNumber) ;
        
        request.setDataObject(actionBean) ;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (!response.isSuccessfulResponse()) {
            System.out.println(" ** System side validation returned false ** ") ;
            CoeusOptionPane.showErrorDialog(this, response.getMessage()) ; // msg will be sent from the server
            return false ;
        }
        
        System.out.println(" ** System side validation returned true ** ") ;
        return true ;
    }
    
    
    //prps end        
    
    /** Getter for property reviewComments.
     * @return Value of property reviewComments.
     */
    public java.util.Vector getReviewComments() {
        return reviewComments;
    }        
    
    /*  Bug #669 - In Some XP machines, Committee actions window opens up distorted. 
     *  There is some kind of layout problem. 
     *  This is resolved by Prahalad on feb 25 2004
     */
    
    
}