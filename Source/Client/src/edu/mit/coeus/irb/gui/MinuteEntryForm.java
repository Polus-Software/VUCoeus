/*
 * NewMinuteEntry.java
 *
 * Created on November 25, 2002, 5:14 PM
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.irb.gui.ScheduleMinuteMaintenance;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.gui.ContingencyLookUpWindow;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.LimitedPlainDocument;


import java.util.Vector;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * This class is used for Entering/Modifying Minute Entries for the Schedule
 * selected. This class is instantiated from the Minutes Tab in
 * schedule maintainence of IRB Module
 * @author  Sagin
 * @date    Dec 02 2002
 *
 */
public class MinuteEntryForm extends javax.swing.JComponent
    implements ListSelectionListener{

    /* Variables declaration - do not modify */

    /* Flag used to check whether any data is modified or not */
    private boolean saveRequired =false;

    /*Added by Vyjayanthi
    * This is used to hold the mode D for Display, I for Add, U for Modify */
    private char functionType;
    
    /* Holds the scheduleID value */
    private String scheduleID = "";

    /* Holds the Protocol Number value */
    private String protocolNumber = "";

    /* Holds the Private Comment Flag value */
    private boolean privateCommentFlag = false;

    /* Holds the Contingency Code value */
    private String protocolContingencyCode = "";

    /* Holds the Contingency Description value */
    private String protocolContingencyDesc = "";

    /* Holds the Other Action Item Description value */
    private String otherItemDesc = "";

    /* Holds the Max Entry Number value */
    private int maxEntryNumber;

    /* Holds CoeusMessageResources instance used for reading Messages.properties file */
    private CoeusMessageResources coeusMessageResources;

    /* Holds reference of the parent form */
    private ScheduleMinuteMaintenance schedMinMaintenance;

    /* Holds Minute Entry comments */
    private String minuteEntry;

    /* Holds old valid Contingency Code */
    private String prevContingencyCode;

    /* This bean is used for all to and fro data transactions with Minutes List Window */
    private MinuteEntryInfoBean minuteEntryInfoBean;

    /* This vector holds Other Action Item beans */
    private Vector otherActionListData;

    /* This is used to hold all the Minute Entry Type Beans. */
    private Vector minuteEntryTypes = null;

    /* This vector holds Protocol Submission Info beans */
    private Vector protocolInfoBeanData;

    /* This is used to hold all the protocol numbers for the schedule. */
    private Vector protocolNoList = null;

    /* This is used to hold all the Other Action Item Descriptions for the schedule. */
    private Vector otherActionItemList = null;

    /* This Dialog Window is used for displaying this form. */
    private CoeusDlgWindow dlgWindow;

    /* Used in checkings in conditional statements */
    private static final String PROTOCOL = "Protocol";

    /* Used in checkings in conditional statements */
    private static final String OTHER_BUSINESS = "Other Business";
    
    //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
    private static final char GET_PARAMETER_MINUTE_ENTRY_CODE = 'm';
    private int paramMinuteEntryCode = 0;
    //COEUSQA-2290 : End
    

    /* Creates new form NewMinuteEntry */
    public MinuteEntryForm() {
        
        initComponents();
        limitCharsInTextArea();//raghuSV: to limit chars.
        
        /* Populate the Entry Type list box */
        Vector minEntryTypes = getEntryTypes();
        setEntryTypes(minEntryTypes);
        displayMinuteDetails();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    
    

    /**
     * Constructor which builds form fields and populates them with data
     * specified in MinuteEntryInfoBean if opened in modify mode, or if opened
     * for entering new Minute Entry then all the fields are initialized.
     * @param minuteEntryInfoBean contains the form values to be displayed
     * @param protocolInfoBeanData used for listing Protocols in
     * Protocol Submission table for the selected Schedule ID.
     * @param otherActionListData used for listing Other Actions in
     * Comm Schedule Act Items table for the selected Schedule ID.
     */

    //Modified by Vyjayanthi to add the parameter function type
    public MinuteEntryForm(char functionType, MinuteEntryInfoBean minuteEntryInfoBean,
        Vector protocolInfoBeanData, Vector otherActionListData) {
        this.functionType = functionType;
        this.minuteEntryInfoBean = minuteEntryInfoBean;
        this.otherActionListData = otherActionListData;
        this.protocolInfoBeanData = protocolInfoBeanData;
        //setProtocolListData(protocolInfoBeanData);
        //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
        paramMinuteEntryCode = getparaemterMinuteEntryTypeCode();
        //COEUSQA-2290 : End
        protocolNoList = (Vector)getNonDeferredProtocols();//Bug fix #933
        setOtherActionItemListData(otherActionListData);
        initComponents(); 
        limitCharsInTextArea();//raghuSV : to limit chars
        
        // Added by Chandra 12/09/2003
        // 3282: Reviewer view of Protocols - Start      
//        java.awt.Component[] components = {txtContingencyCode,btnLookUp,txtArEntry,
//        chkPrivateFlag,btnGenAttendence,btnSaveClose,btnSaveNew,btnCancel};
        java.awt.Component[] components = {txtContingencyCode,btnLookUp,txtArEntry,chkFinal,
        chkPrivateFlag,btnGenAttendence,btnSaveClose,btnSaveNew,btnCancel};
        // 3282: Reviewer view of Protocols - End
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        // End Chandra
        Vector minEntryTypes = getEntryTypes();
        setEntryTypes(minEntryTypes);
        displayMinuteDetails();
        
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    
    //raghuSV: method to restrict chars going beyond the limit in text area box.
    //starts...
    public void limitCharsInTextArea(){
        
        LimitedPlainDocument plainDocument = new LimitedPlainDocument(3878);
        txtArEntry.setDocument(plainDocument);
    }
    //ends
    
    /**
     * Sets reference to the parent form ie, ScheduleMinuteMaintenance
     * @param ScheduleMinuteMaintenance parent form.
     */
    public void setScheduleMaintenanceForm(ScheduleMinuteMaintenance schMinMaintenance) {
        this.schedMinMaintenance = schMinMaintenance;
    }

    /**
     * Sets the Entry Type Descriptions in the Entry Type List Box.
     * @param vector minEntryTypes which contains list of MinuteEntryBeans.
     */
    private void setEntryTypes(Vector minEntryTypes){
        if (minEntryTypes!=null){
            lstEntryTypes.setModel(new DefaultListModel());
            int maxSize = minEntryTypes.size();
//            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
//                ((DefaultListModel)lstEntryTypes.getModel()).
//                    addElement(((MinuteEntryBean)minEntryTypes.
//                        elementAt(rowIndex)).toString());
//            }
            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
                ((DefaultListModel)lstEntryTypes.getModel()).addElement(
                    (MinuteEntryBean)minEntryTypes.elementAt(rowIndex));
            }
            
        }
    }

    /**
     * Method to return the Minute Entry Type Code corresponding to the Entry Type
     * selected in the list box.
     * @param entryTypeDesc for which the code to be returned
     * @return int Minute Entry Type Code which is used for updating the
     * Minute Entry Info Bean.
     */
    private int getEntryTypeCode(String entryTypeDesc){
        int entryTypeCode=0;
        if (minuteEntryTypes!=null){
            MinuteEntryBean minuteEntryBean;
            int maxSize = minuteEntryTypes.size();
            Vector localMinEntryTypes = minuteEntryTypes;
            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
                minuteEntryBean = (MinuteEntryBean) localMinEntryTypes.
                    elementAt(rowIndex);
                if (minuteEntryBean.getDescription().equals(entryTypeDesc)) {
                    entryTypeCode = Integer.parseInt(minuteEntryBean.getCode());
                    break;
                }
            }
        }
        return entryTypeCode;
    }

    private MinuteEntryBean getEntryType(int entryTypeCode){
        if (minuteEntryTypes!=null){
            MinuteEntryBean minuteEntryBean;
            int maxSize = minuteEntryTypes.size();
            Vector localMinEntryTypes = minuteEntryTypes;
            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
                minuteEntryBean = (MinuteEntryBean) localMinEntryTypes.elementAt(rowIndex);
                if (Integer.parseInt(minuteEntryBean.getCode()) == entryTypeCode) {
                    return minuteEntryBean;
                }
            }
        }
        return null;
    }
    /**
     * Method to return the Action Item Number if Entry Type selected is Other
     * Business. Returns the Action Item Number corresponding to the Item Description
     * selected.
     * @param actionItemDesc for which the Action Item Number to be returned
     * @return Action Item Number which is used for updating the
     * Minute Entry Info Bean.
     */
    private int getActionItemNumber(String actionItemDesc){
        int actionItemNumber=0;
        if (otherActionListData!=null){
            OtherActionInfoBean otherActionInfoBean;
            int maxSize = minuteEntryTypes.size();
            // Modified for COEUSQA-3223 : IRB Schedule : Generate Minutes - Some Minute Entries don't show up in PDF - Start
            // Instead of checkin for the entry list size, check for the action item list
//            Vector localOtherActionList = otherActionListData;
//            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
//                otherActionInfoBean = (OtherActionInfoBean) localOtherActionList.
                    for ( int rowIndex = 0; rowIndex < otherActionListData.size(); rowIndex++) {
                otherActionInfoBean = (OtherActionInfoBean) otherActionListData.elementAt(rowIndex);
                // Modified for COEUSQA-3223 : IRB Schedule : Generate Minutes - Some Minute Entries don't show up in PDF - End              
                if (otherActionInfoBean.getItemDescription().equals(actionItemDesc)) {
                    actionItemNumber =  otherActionInfoBean.getActionItemNumber();
                    break;
                }
            }
        }
        return actionItemNumber;
    }

    /**
     * Method to return the Minute Entry Type list from the database .
     * @return Vector minuteEntryTypes.
     * The vector returned is used for populating the Entry Type list box.
     */
    private Vector getEntryTypes(){
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_MINUTE_ENTRY_TYPES");
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        minuteEntryTypes = null;
        if (response!=null){
            // get the minute entry types data. the vector contains the minute entry beans
            minuteEntryTypes = (Vector) response.getDataObject();
        }
        return minuteEntryTypes;
    }

    //Added by Nadh for bug fix #933
    //start 12 aug 2004
    /**
     * This Method to return the Non Deffered Protocols list from the database .
     * @return Vector .
     * The vector returned is used for populating the Protocols list box.
     */
    private Vector getNonDeferredProtocols() {
        RequesterBean requester = new RequesterBean();
        Vector nonDefProtocols = new Vector();
        requester.setDataObject(minuteEntryInfoBean.getScheduleId());
        requester.setFunctionType('N');       
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()){
            // get the non deferred protocols data. 
            nonDefProtocols = (Vector) response.getDataObject();
        }
        return nonDefProtocols;
    }
    //end Nadh 12 aug 2004
    /**
     * Method returns the Protocol Sequence Number corresponding to the index given
     * as parameter. The index corresponds to the protocol no selected from 
     * ProtocolNo list. 
     * @param protocolNoIndex for which the Protocol Sequence Number to be returned
     * @return Protocol Sequence Number which is used for updating the
     * Minute Entry Info Bean.
     */
    private int getProtocolSequenceNo(int protocolNoIndex){
        int protocolSeqNumber=0;
        if (protocolInfoBeanData!=null){
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean;
            protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)
                protocolInfoBeanData.elementAt(protocolNoIndex);
            protocolSeqNumber =  protocolSubmissionInfoBean.getSequenceNumber();
        }
        return protocolSeqNumber;
    }

    /**
     * Method to set the ProtocolNumber list from the Protocol Submission
     * Info Bean vectors in a vector. This vector is inturn used for setting the
     * Protocol Number list when the Entry Type selected is Protocol.
     * @param Vector of Protocol Submission Info bean data.
     */
    public void setProtocolListData(Vector protoInfoBeanData){
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean;
        if (protoInfoBeanData != null){
            protocolNoList = new Vector();
            int maxSize = protoInfoBeanData.size();
            String protocolNo;
            Vector localProtocolNoList = new Vector();
            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
                protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)
                                        protoInfoBeanData.elementAt(rowIndex);
                if (protocolSubmissionInfoBean != null) {
                    protocolNo = protocolSubmissionInfoBean.getProtocolNumber();
                    localProtocolNoList.add( protocolNo.equals(null) ? "" : protocolNo );
                }
            }
            protocolNoList = localProtocolNoList;
        }

    }

    /**
     * Method to set the Other Action Items list from the Other Action Info Bean
     * vectors in a vector. This vector is inturn used for setting the 
     * Other Action Item list when the Entry Type selected is Other Business.
     * @param Vector of Other Action Info Bean data.
     */
    public void setOtherActionItemListData(Vector otherActionInfoBeanData){
        OtherActionInfoBean otherActionInfoBean;
        if (otherActionInfoBeanData != null){
            otherActionItemList = new Vector();
            int maxSize = otherActionInfoBeanData.size();
            String itemDescription;
            Vector localOtherActionItemList = new Vector();
            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
                otherActionInfoBean = (OtherActionInfoBean)
                    otherActionInfoBeanData.elementAt(rowIndex);
                if (otherActionInfoBean != null ) { //&& otherActionInfoBean.getScheduleActTypeCode() == 1 // commented by prps on jan 06 2003
                    itemDescription = otherActionInfoBean.getItemDescription();
                    localOtherActionItemList.add(
                        itemDescription.equals(null) ? "" : itemDescription );
                }
            }
            otherActionItemList = localOtherActionItemList;
        }
    }

    /**
     * Method to populate Protocol Numbers in the List when Entry Type Protocol
     * is selected.
     */
    public void displayProtocolList(){

        Vector localProtocolNoList = protocolNoList;
        lstItemDetails.setModel(new DefaultListModel());
        if (localProtocolNoList != null){
            int maxSize = localProtocolNoList.size();
            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex=rowIndex+2 ) { // modified for Bug Fix : 1311
                ((DefaultListModel)lstItemDetails.getModel()).
                    addElement((localProtocolNoList.elementAt(rowIndex)));
            }
        }

    }

    /**
     * Method to populate Other Action Item Description in the List when
     * Entry Type Other Business is selected.
     */
    public void displayOtherActionItemList(){

        Vector localOtherActionItemList = otherActionItemList;
        lstItemDetails.setModel(new DefaultListModel());
        if (localOtherActionItemList != null){
            int maxSize = localOtherActionItemList.size();
            for ( int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
                ((DefaultListModel)lstItemDetails.getModel()).
                    addElement((localOtherActionItemList.elementAt(rowIndex)));
            }
        }

    }

    /**
     * Method to set the Max Entry Number, which is required while adding
     * new Minutes Entry.
     * @param int maxEntryNum contains maximum Entry Number.
     */
    public void setMaxEntryNumber(int maxEntryNum){
        maxEntryNumber = maxEntryNum;
    }

    /**
     * Method to get the Max Entry Number.
     * Returns int Max Entry Number.
     */
    public int getMaxEntryNumber(){
        return maxEntryNumber;
    }

    /**
     * Method to set the Contingency Code and Description selected from the
     * Contingency lookup window.
     *
     * @param String contingencyCode, String contingencyDesc.
     */
//    public void setContingencyCodeDesc(String contingencyCode,
//        String contingencyDesc) {
//        txtContingencyCode.setText(contingencyCode);
//        txtArContingencyDesc.setText(contingencyDesc);
//    }
    // Added by chandra - 20/10/2003 - start

    public void setContingencyCodeDesc(String contingencyCode,
        String contingencyDesc) {
        txtContingencyCode.setText(contingencyCode);
        txtArEntry.setText(contingencyDesc);
        /** To display the contingency message without editing when it is loaded for the 
            first time*/
        if(contingencyCode==null){
            txtArEntry.setEditable(true);
        }else{
            txtArEntry.setEditable(false);
        }
        
    }//Added by chandra - 20/10/2003 - End
    
    
    /**
     * Method to set the Schedule ID, which is required while adding
     * new Minutes Entry. Also required while Generating Attendance.
     * @param int contains maximum Entry Number.
     */
    public void setScheduleID(String schedID){
        scheduleID = schedID;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblEntryType = new javax.swing.JLabel();
        scrPnTypes = new javax.swing.JScrollPane();
        lstEntryTypes = new javax.swing.JList();
        lblTypeDisplay = new javax.swing.JLabel();
        scrPnTypeMembers = new javax.swing.JScrollPane();
        lstItemDetails = new javax.swing.JList();
        lblTypeDescLabel = new javax.swing.JLabel();
        lblPrivateFlagLabel = new javax.swing.JLabel();
        chkPrivateFlag = new javax.swing.JCheckBox();
        scrPnTypeDesc = new javax.swing.JScrollPane();
        txtArEntry = new javax.swing.JTextArea();
        lblContingencyLabel = new javax.swing.JLabel();
        txtContingencyCode = new javax.swing.JTextField();
        btnLookUp = new javax.swing.JButton();
        btnSaveClose = new javax.swing.JButton();
        btnSaveNew = new javax.swing.JButton();
        btnGenAttendence = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblFinal = new javax.swing.JLabel();
        chkFinal = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        lblEntryType.setFont(CoeusFontFactory.getLabelFont());
        lblEntryType.setText("Entry Type");
        lblEntryType.setPreferredSize(new java.awt.Dimension(100, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(lblEntryType, gridBagConstraints);

        scrPnTypes.setPreferredSize(new java.awt.Dimension(150, 131));
        lstEntryTypes.setFont(CoeusFontFactory.getNormalFont());
        lstEntryTypes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrPnTypes.setViewportView(lstEntryTypes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(scrPnTypes, gridBagConstraints);

        lblTypeDisplay.setFont(CoeusFontFactory.getLabelFont());
        lblTypeDisplay.setText("Protocols");
        lblTypeDisplay.setPreferredSize(new java.awt.Dimension(100, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblTypeDisplay, gridBagConstraints);

        scrPnTypeMembers.setPreferredSize(new java.awt.Dimension(150, 131));
        lstItemDetails.setFont(CoeusFontFactory.getNormalFont());
        lstItemDetails.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrPnTypeMembers.setViewportView(lstItemDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        add(scrPnTypeMembers, gridBagConstraints);

        lblTypeDescLabel.setFont(CoeusFontFactory.getLabelFont());
        lblTypeDescLabel.setText("Entry");
        lblTypeDescLabel.setPreferredSize(new java.awt.Dimension(50, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 2, 0);
        add(lblTypeDescLabel, gridBagConstraints);

        lblPrivateFlagLabel.setFont(CoeusFontFactory.getLabelFont());
        lblPrivateFlagLabel.setText("Private");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 5, 5);
        add(lblPrivateFlagLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(chkPrivateFlag, gridBagConstraints);

        scrPnTypeDesc.setToolTipText("");
        scrPnTypeDesc.setPreferredSize(new java.awt.Dimension(450, 168));
        txtArEntry.setFont(CoeusFontFactory.getNormalFont());
        txtArEntry.setLineWrap(true);
        txtArEntry.setRows(10);
        txtArEntry.setTabSize(4);
        txtArEntry.setToolTipText("");
        txtArEntry.setWrapStyleWord(true);
        scrPnTypeDesc.setViewportView(txtArEntry);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 17;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
        add(scrPnTypeDesc, gridBagConstraints);

        lblContingencyLabel.setFont(CoeusFontFactory.getLabelFont());
        lblContingencyLabel.setText("Contingency Code:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 17, 5, 0);
        add(lblContingencyLabel, gridBagConstraints);

        txtContingencyCode.setFont(CoeusFontFactory.getNormalFont());
        txtContingencyCode.setMaximumSize(new java.awt.Dimension(2147483647, 17));
        txtContingencyCode.setMinimumSize(new java.awt.Dimension(63, 17));
        txtContingencyCode.setPreferredSize(new java.awt.Dimension(63, 20));
        txtContingencyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContingencyCode_focusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContingencyCode_lostFocus(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 5);
        add(txtContingencyCode, gridBagConstraints);

        btnLookUp.setIcon(new javax.swing.ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.FIND_ICON)));
    btnLookUp.setToolTipText("");
    btnLookUp.setContentAreaFilled(false);
    btnLookUp.setDefaultCapable(false);
    btnLookUp.setMaximumSize(new java.awt.Dimension(27, 27));
    btnLookUp.setMinimumSize(new java.awt.Dimension(27, 27));
    btnLookUp.setPreferredSize(new java.awt.Dimension(27, 23));
    btnLookUp.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnLookUp_actionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 5);
    add(btnLookUp, gridBagConstraints);

    btnSaveClose.setFont(CoeusFontFactory.getLabelFont());
    btnSaveClose.setMnemonic('S');
    btnSaveClose.setText("Save & Close");
    btnSaveClose.setMaximumSize(null);
    btnSaveClose.setMinimumSize(null);
    btnSaveClose.setPreferredSize(null);
    btnSaveClose.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnSaveAndClose_actionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
    add(btnSaveClose, gridBagConstraints);

    btnSaveNew.setFont(CoeusFontFactory.getLabelFont());
    btnSaveNew.setMnemonic('N');
    btnSaveNew.setText("Save & New");
    btnSaveNew.setMaximumSize(null);
    btnSaveNew.setMinimumSize(null);
    btnSaveNew.setPreferredSize(null);
    btnSaveNew.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnSaveAndNew_actionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
    add(btnSaveNew, gridBagConstraints);

    btnGenAttendence.setFont(CoeusFontFactory.getLabelFont());
    btnGenAttendence.setMnemonic('G');
    btnGenAttendence.setText("Gen. Attendance");
    btnGenAttendence.setEnabled(false);
    btnGenAttendence.setMaximumSize(null);
    btnGenAttendence.setMinimumSize(null);
    btnGenAttendence.setPreferredSize(null);
    btnGenAttendence.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnGenAttendence_actionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
    add(btnGenAttendence, gridBagConstraints);

    btnCancel.setFont(CoeusFontFactory.getLabelFont());
    btnCancel.setMnemonic('C');
    btnCancel.setText("Cancel");
    btnCancel.setMaximumSize(null);
    btnCancel.setMinimumSize(null);
    btnCancel.setPreferredSize(null);
    btnCancel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnCancel_actionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
    add(btnCancel, gridBagConstraints);

    lblFinal.setFont(CoeusFontFactory.getLabelFont());
    lblFinal.setText("Final ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
    add(lblFinal, gridBagConstraints);

    chkFinal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    chkFinal.setMargin(new java.awt.Insets(0, 0, 0, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(0, 55, 0, 0);
    add(chkFinal, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void txtContingencyCode_focusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContingencyCode_focusGained
        // Add your handling code here:
        //Store the previous contingency code
        prevContingencyCode = txtContingencyCode.getText().trim();
    }//GEN-LAST:event_txtContingencyCode_focusGained

    private void txtContingencyCode_lostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContingencyCode_lostFocus
        // Add your handling code here:
       if (!evt.isTemporary()) {
            String contingencyCode = txtContingencyCode.getText().trim();
            if (!contingencyCode.equals("")) {
                String contingencyDesc = validateContingencyCode(
                                             txtContingencyCode.getText().trim());
                
                if (contingencyDesc != null ) {
                   // txtArContingencyDesc.setText(contingencyDesc);
                    txtArEntry.setText(contingencyDesc);
                    txtArEntry.setEditable(false);
                } else {
                    txtContingencyCode.setText(prevContingencyCode);
                   
                }                
            } else {
               // txtArContingencyDesc.setText("");
                //txtArEntry.setText("");
                txtArEntry.setEditable(true);
            }
       }
    }//GEN-LAST:event_txtContingencyCode_lostFocus

    /**
     * validate the contingency code entered by the user. This method will send the 
     * code to the server and validate the availability of code. This method will
     * set the contingency desc if the code is valid, otherwise space.
     *
     * @param String contingencyCode the code to be validated
     * @return String contingency description
     */
    private String validateContingencyCode(String contingencyCode){
        String contingencyDesc=null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_CONTINGENCY_DESC");
        request.setId(contingencyCode);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            contingencyDesc = (String) response.getDataObject();

            if (contingencyDesc == null ) {
                log(coeusMessageResources.parseMessageKey(
                        "minuteEntryFrm_exceptionCode.1154"));
            }
        }
        return contingencyDesc;
    }

    /**
     * This method is invoked to initialize the form with data in modify mode
     * or initialize the form with default values when in Insert mode
     *
     */
    private void displayMinuteDetails(){

        //This bean is used for all to and fro data transactions with Minutes List Window
        MinuteEntryInfoBean localMinuteEntryInfoBean = minuteEntryInfoBean;
 
        //Holds the Entry Type Description value
        String minuteEntryTypeDesc = "";
        int entryType = localMinuteEntryInfoBean.getMinuteEntryTypeCode();
        txtContingencyCode.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        lstEntryTypes.getSelectionModel().addListSelectionListener(this);

        //If modify mode then set the values in the various fields
        if (entryType > 0 ) {

            privateCommentFlag = localMinuteEntryInfoBean.isPrivateCommentFlag();
            minuteEntryTypeDesc = localMinuteEntryInfoBean.getMinuteEntryTypeDesc();
            if (entryType == 3) { // protocol
                protocolNumber = localMinuteEntryInfoBean.getProtocolNumber();
            } else if (entryType == 4) { // other business
                otherItemDesc = localMinuteEntryInfoBean.getOtherItemDesc();
            }
            //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
            else if(entryType == paramMinuteEntryCode){
                 protocolNumber = localMinuteEntryInfoBean.getProtocolNumber();
            }
            protocolContingencyCode =
                localMinuteEntryInfoBean.getProtocolContingencyCode();
            protocolContingencyDesc = 
            localMinuteEntryInfoBean.getProtocolContingencyDesc();
            minuteEntry = localMinuteEntryInfoBean.getMinuteEntry();
            lstEntryTypes.setSelectedValue(
                getEntryType(minuteEntryInfoBean.getMinuteEntryTypeCode()),true);
        }else{
            lstEntryTypes.setSelectedValue(getEntryType(1),true);
        }

        chkPrivateFlag.setSelected(privateCommentFlag);
        // 3282: Reviewer view of Protocols
        chkFinal.setSelected(localMinuteEntryInfoBean.isFinalFlag());
        txtContingencyCode.setText(protocolContingencyCode == null ? "" :
            protocolContingencyCode);
        
        if ( entryType == 3 ) {
            lstItemDetails.setSelectedValue(protocolNumber,true);
        }else if (entryType == 4) {
            lstItemDetails.setSelectedValue(otherItemDesc,true);
        }
        //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
        else if(entryType == paramMinuteEntryCode){
            lstItemDetails.setSelectedValue(protocolNumber,true);
        }
        //COEUSQA-2290 : End
        if(protocolContingencyCode==null ||protocolContingencyCode.equals("")){
            txtArEntry.setEditable(true);            
        }else{
            txtArEntry.setEditable(false);
        }
        txtArEntry.setText(minuteEntry == null ? "" : minuteEntry);
        saveRequired=false;

    }

    /**
     * This is main method which is called by the listener to load the form
     * for Add/Modify.
     *
     * @param mdiForm CoeusAppletMDIForm
     * @param isModal boolean
     */
    public void showForm(CoeusAppletMDIForm mdiForm,boolean isModal){
        this.schedMinMaintenance = schedMinMaintenance;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
     /*check for ESC button if the user presses the button the dialog should close*/
        CoeusDlgWindow dialogWindow = new CoeusDlgWindow(mdiForm,"Minute Entry",isModal);
        dialogWindow.getContentPane().add(this);
        dialogWindow.setResizable(false);
        //dialogWindow.setSize(570,340);
        dialogWindow.pack();
        Dimension dlgSize = dialogWindow.getSize();
        dialogWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;
        dialogWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogWindow.addWindowListener(new WindowAdapter(){
            
            public void windowActivated(WindowEvent we){
                txtArEntry.requestFocusInWindow();
            }
            public void windowOpened(WindowEvent we){
                saveRequired=false;
            }
            public void windowClosing(WindowEvent we){
                 isSaveRequired();
            }
        });
        dialogWindow.addEscapeKeyListener(
            new AbstractAction("escPressed") {
                public void actionPerformed(ActionEvent actionEvent) {
                    isSaveRequired();
                }
        });
        dlgWindow = dialogWindow;
        //Added by Vyjayanthi to disable components in the display mode
        //Start
        formatFields();
        //End
        dlgWindow.show();
    }

    //Added by Vyjayanthi to disable components in the display mode
    //Start
    private void formatFields() {
        if (functionType == 'D') {
            btnSaveNew.setEnabled(false);
            btnSaveClose.setEnabled(false);
            btnGenAttendence.setEnabled(false);
            btnLookUp.setEnabled(false);
        //    btnLookUp.setVisible(false);
            lstEntryTypes.setEnabled(false);
            lstItemDetails.setEnabled(false);
            chkPrivateFlag.setEnabled(false);
            // 3282: Reviewer view of Protocols
            chkFinal.setEnabled(false);        
            txtContingencyCode.setEnabled(false);
           // txtArContingencyDesc.setEditable(false);
            txtArEntry.setEditable(false);
            Color bgColor = (Color) UIManager.getDefaults().get("Panel.background");
            lstEntryTypes.setBackground( bgColor );
            lstEntryTypes.setForeground( Color.black);
            lstItemDetails.setBackground( bgColor );
            lstItemDetails.setForeground( Color.black);
            txtArEntry.setBackground( bgColor );
            txtContingencyCode.setEditable(false);
        }
        //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
        if(functionType == TypeConstants.MODIFY_MODE){
            lstEntryTypes.setEnabled(false);
            lstItemDetails.setEnabled(false);
            Color bgColor = (Color) UIManager.getDefaults().get("Panel.background");
            lstEntryTypes.setBackground( bgColor );
            lstEntryTypes.setForeground( Color.black);
            lstItemDetails.setBackground( bgColor );
            lstItemDetails.setForeground( Color.black);
        }
        //COEUSQA-2290 : end
        
        //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - start
        //While adding new comments privateFlag should be checked by default
        if(functionType == 'I' ){
            chkPrivateFlag.setSelected(true);
        }
        //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - end
        
    }
    //End

    /**
     * Displays the message,it gives the error message.
     * @param mesg String
     */
    private static void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }

    /**
     * Action while firing the events when Entry Type List Selection is changed
     * @param e ListSelectionEvent instance fired while changing Entry Type List
     * selction.
     */
    public void valueChanged(ListSelectionEvent e) {
        //String entryType = (String) lstEntryTypes.getSelectedValue();
        MinuteEntryBean entryType = (MinuteEntryBean)lstEntryTypes.getSelectedValue();
        int entryTypeCode = Integer.parseInt(entryType.getCode());
//        if (entryType.equals("General Comments") ||
//                            entryType.equals("Attendance") ) {
//            lstItemDetails.setModel(new DefaultListModel());
//            lstItemDetails.setVisible(false);
//            lblTypeDisplay.setText("");
//            //scrPnTypeMembers.setVisible(false);
//            txtContingencyCode.setEnabled(false);
//            txtContingencyCode.setVisible(false);
//            lblContingencyLabel.setVisible(false);
//            btnLookUp.setEnabled(false);
//            btnLookUp.setVisible(false);
//            txtContingencyCode.setText("");
//           // txtArContingencyDesc.setText("");
//            txtArEntry.setText("");
//               txtArEntry.setEditable(true);
//        } else {
//            lstItemDetails.setVisible(true);
//           // scrPnTypeMembers.setVisible(true);
//
//            if (entryType.equals("Protocol")) {
//                
//                lblTypeDisplay.setText("Protocols");
//                txtContingencyCode.setEnabled(true);
//                txtContingencyCode.setVisible(true);
//                lblContingencyLabel.setVisible(true);
//                //txtArEntry.setEditable(false);
//                       
//                txtArEntry.setEditable(true);
//                btnLookUp.setVisible(true);
//                btnLookUp.setEnabled(true);
//                
//           
//               // txtArEntry.setText("");
//                displayProtocolList();
//            } else {
//                txtContingencyCode.setEnabled(false);
//                lblContingencyLabel.setVisible(false);
//                txtContingencyCode.setVisible(false);
//                btnLookUp.setEnabled(false);
//                btnLookUp.setVisible(false);
//                txtContingencyCode.setText("");
//               // txtArContingencyDesc.setText("");
//                //txtArEntry.setText("");
//                txtArEntry.setEditable(true);
//                lblTypeDisplay.setText("Other Business");
//                displayOtherActionItemList();
//            }
//        }
        if (entryTypeCode == 3) {
            lstItemDetails.setVisible(true);
            lblTypeDisplay.setText("Protocols");
            txtContingencyCode.setEnabled(true);
            txtContingencyCode.setVisible(true);
            lblContingencyLabel.setVisible(true);
            //txtArEntry.setEditable(false);

            txtArEntry.setEditable(true);
            btnLookUp.setVisible(true);
            btnLookUp.setEnabled(true);


           // txtArEntry.setText("");
            displayProtocolList();
        } else if(entryTypeCode == 4){
            lstItemDetails.setVisible(true);
            txtContingencyCode.setEnabled(false);
            lblContingencyLabel.setVisible(false);
            txtContingencyCode.setVisible(false);
            btnLookUp.setEnabled(false);
            btnLookUp.setVisible(false);
            txtContingencyCode.setText("");
           // txtArContingencyDesc.setText("");
            //txtArEntry.setText("");
            txtArEntry.setEditable(true);
            lblTypeDisplay.setText("Other Business");
            displayOtherActionItemList();
        }
        //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
        else if (entryTypeCode == paramMinuteEntryCode) {
            lstItemDetails.setVisible(true);
            lblTypeDisplay.setText("Protocols");
            txtContingencyCode.setEnabled(true);
            txtContingencyCode.setVisible(true);
            lblContingencyLabel.setVisible(true);
            btnLookUp.setVisible(true);
            btnLookUp.setEnabled(true);
            displayProtocolList();
        } 
        //COEUSQA-2290 :End
        else{
            lstItemDetails.setModel(new DefaultListModel());
            lstItemDetails.setVisible(false);
            lblTypeDisplay.setText("");
            //scrPnTypeMembers.setVisible(false);
            txtContingencyCode.setEnabled(false);
            txtContingencyCode.setVisible(false);
            lblContingencyLabel.setVisible(false);
            btnLookUp.setEnabled(false);
            btnLookUp.setVisible(false);
            txtContingencyCode.setText("");
           // txtArContingencyDesc.setText("");
            txtArEntry.setText("");
               txtArEntry.setEditable(true);
        }
        //Enable/Disable the Generate Attendance button
        if (entryTypeCode == 2) {
            btnGenAttendence.setEnabled(true);
        } else {
            btnGenAttendence.setEnabled(false);
        }
        saveRequired=true;
        
        //COEUSQA:3233 - Remove Protocol Review Comment from minutes entry window - Start
        if(entryTypeCode == paramMinuteEntryCode) {
            btnSaveNew.setEnabled(false);
            btnSaveClose.setEnabled(false);
        } else {
            btnSaveNew.setEnabled(true);
            btnSaveClose.setEnabled(true);
        }
        //COEUSQA:3233 - End
    }

    /**
     * This method is called for frontend validations
     * when the Save & Close or Save & New or Cancel operations are performed.
     */
    private boolean validateFields(){
        boolean processOK = false;
        MinuteEntryBean entryType = (MinuteEntryBean)lstEntryTypes.getSelectedValue();
        int entryTypeCode = Integer.parseInt(entryType.getCode());

        if (lstEntryTypes.getSelectedIndex() < 0){
            log(coeusMessageResources.parseMessageKey(
                                        "minuteEntryFrm_exceptionCode.1155"));
        } else if (txtArEntry.getText().trim().length() == 0 &&
                txtContingencyCode.getText().trim().length() == 0){
            log(coeusMessageResources.parseMessageKey(
                                        "minuteEntryFrm_exceptionCode.1156"));
        } else if (entryTypeCode == 3 && lstItemDetails.getModel().getSize() == 0){
            log(coeusMessageResources.parseMessageKey(
                                        "minuteEntryFrm_exceptionCode.1157"));
        } else if (entryTypeCode == 3 && lstItemDetails.getSelectedIndex() < 0){
            log(coeusMessageResources.parseMessageKey(
                                        "minuteEntryFrm_exceptionCode.1158"));
        } else if (entryTypeCode == 4 && lstItemDetails.getModel().getSize() == 0){
            log(coeusMessageResources.parseMessageKey(
                                        "minuteEntryFrm_exceptionCode.1159"));
        } else if (entryTypeCode == 4 && lstItemDetails.getSelectedIndex() < 0){
            log(coeusMessageResources.parseMessageKey(
                                        "minuteEntryFrm_exceptionCode.1160"));
        } 
        //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
        else if (entryTypeCode == paramMinuteEntryCode && lstItemDetails.getModel().getSize() == 0){
            log(coeusMessageResources.parseMessageKey(
                    "minuteEntryFrm_exceptionCode.1157"));
        } else if (entryTypeCode == paramMinuteEntryCode && lstItemDetails.getSelectedIndex() < 0){
            log(coeusMessageResources.parseMessageKey(
                    "minuteEntryFrm_exceptionCode.1158"));
        }
        //COEUSQA-2290 : End
        else {
            processOK = true;
        }
        return processOK;
    }

    /**
     * This method invoked after validation whenever the user presses any of the
     * Save buttons.
     * Sets the data in the MinuteEntryInfoBean and send to MinuteMaintenance
     * window to update/insert(AcType=U/I) records.
     */
    public void sendDataToUpdate(){
        String entryTypeDesc = "";
        int entryTypeCode;
        String actionItemDesc = "";
        String actionItemNumber;
        boolean privateCommentFlag;
        MinuteEntryBean entryType = (MinuteEntryBean)lstEntryTypes.getSelectedValue();
		entryTypeDesc = entryType.getDescription();
        entryTypeCode = Integer.parseInt(entryType.getCode());
        minuteEntryInfoBean.setMinuteEntryTypeCode(entryTypeCode);
        minuteEntryInfoBean.setMinuteEntryTypeDesc(entryTypeDesc);
		privateCommentFlag = chkPrivateFlag.isSelected();
        minuteEntryInfoBean.setPrivateCommentFlag(privateCommentFlag);
        // 3282: Reviewer view of Protocols
        minuteEntryInfoBean.setFinalFlag(chkFinal.isSelected());
        
        if ( entryTypeCode == 3 ) {
            minuteEntryInfoBean.setProtocolNumber(
                            (String) lstItemDetails.getSelectedValue());
            minuteEntryInfoBean.setSequenceNumber(
                getProtocolSequenceNo(lstItemDetails.getSelectedIndex()));
			//Added by Jobin for the Bug Fix : 1311 getting the submission number along with the 
			// protocol number and based on selection of the protocol number set the corresponding
			//submission number - start
			int selectedProtocol = lstItemDetails.getSelectedIndex();
			selectedProtocol = (selectedProtocol * 2) + 1;
			int submissionNumber = Integer.parseInt(protocolNoList.get(selectedProtocol).toString());

			minuteEntryInfoBean.setSubmissionNumber(submissionNumber);//Bug Fix: 1311
        } else if ( entryTypeCode == 4 ) {
            actionItemDesc = (String) lstItemDetails.getSelectedValue();
            actionItemNumber = (String)String.valueOf(
                                    getActionItemNumber(actionItemDesc));
            minuteEntryInfoBean.setProtocolNumber(actionItemNumber);
            minuteEntryInfoBean.setOtherItemDesc(actionItemDesc);
        }
        //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
        if ( entryTypeCode == paramMinuteEntryCode ) {
            minuteEntryInfoBean.setProtocolNumber(
                    (String) lstItemDetails.getSelectedValue());
            minuteEntryInfoBean.setSequenceNumber(
                    getProtocolSequenceNo(lstItemDetails.getSelectedIndex()));
            int selectedProtocol = lstItemDetails.getSelectedIndex();
            selectedProtocol = (selectedProtocol * 2) + 1;
            int submissionNumber = Integer.parseInt(protocolNoList.get(selectedProtocol).toString());
            minuteEntryInfoBean.setSubmissionNumber(submissionNumber);
        }
        //COEUSQA-2290 : END
        minuteEntryInfoBean.setMinuteEntry(txtArEntry.getText());
        minuteEntryInfoBean.setProtocolContingencyCode(
            txtContingencyCode.getText());
        
//        minuteEntryInfoBean.setProtocolContingencyDesc(
//            txtArContingencyDesc.getText());
        minuteEntryInfoBean.setProtocolContingencyDesc(
        txtArEntry.getText());
        schedMinMaintenance.setMinuteEntryTypes(minuteEntryTypes);
        schedMinMaintenance.refreshDataVector(minuteEntryInfoBean);
		
    }

    /**
     * This method invoked when pressed Save & New button.
     * Initializes the Minute Entry Info Bean and clears the Minutes Entry,
     * Private Flag, Contingency Code and Description.
     */
    private void initAll(){
        
        minuteEntryInfoBean = new MinuteEntryInfoBean();
        minuteEntryInfoBean.setAcType("I");
        minuteEntryInfoBean.setScheduleId(scheduleID);
//        setMaxEntryNumber(getMaxEntryNumber()+1);
//        minuteEntryInfoBean.setEntryNumber(getMaxEntryNumber());
        txtArEntry.setText("");
        txtContingencyCode.setText("");
        //txtArContingencyDesc.setText("");
        chkPrivateFlag.setSelected(false);
        // 3282: Reviewer view of Protocols
        chkFinal.setSelected(false);
        txtContingencyCode.requestFocusInWindow();
        //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - start
        //While adding new comments privateFlag should be checked by default
        if(TypeConstants.INSERT_RECORD.equals(minuteEntryInfoBean.getAcType())){
            chkPrivateFlag.setSelected(true);
        }
        //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - end
    }


    /**
     * This method is called before closing the screen to check whether any values
     * are changed and if changed prompt user whether to save the changes or not.
     */
    private void  isSaveRequired() {
        MinuteEntryBean entryType = (MinuteEntryBean)lstEntryTypes.getSelectedValue();
        
        if ( minuteEntryInfoBean.getMinuteEntry() != null && entryType != null &&
            minuteEntryInfoBean.getMinuteEntryTypeCode() != Integer.parseInt(entryType.getCode()) ) {
              saveRequired=true;
        }else if( minuteEntryInfoBean.isPrivateCommentFlag() != chkPrivateFlag.isSelected() ) {
            saveRequired=true;
        // 3282: Reviewer view of Protocols - Start
        }else if( minuteEntryInfoBean.isFinalFlag() != chkFinal.isSelected() ) {
            saveRequired=true;
        // 3282: Reviewer view of Protocols - End
        }else if ( minuteEntryInfoBean.getMinuteEntry() != null &&
               !minuteEntryInfoBean.getMinuteEntry().trim().equalsIgnoreCase(
                txtArEntry.getText().trim()) ) {
              saveRequired=true;
        }else if ( minuteEntryInfoBean.getMinuteEntry() == null &&
                   txtArEntry.getText().trim().length() > 0 ) {
              saveRequired=true;
        }else if ( minuteEntryInfoBean.getProtocolContingencyCode() != null &&
            !minuteEntryInfoBean.getProtocolContingencyCode().trim().equalsIgnoreCase(
                txtContingencyCode.getText().trim()) ) {
              saveRequired=true;
        }else if ( minuteEntryInfoBean.getProtocolContingencyCode() == null &&
                txtContingencyCode.getText().trim().length() > 0 ) {
              saveRequired=true;
        }else if (lstItemDetails != null && lstItemDetails.getSelectedValue() != null &&
            minuteEntryInfoBean.getProtocolNumber() != null &&
            minuteEntryInfoBean.getMinuteEntryTypeCode() == 3 &&
            !minuteEntryInfoBean.getProtocolNumber().equalsIgnoreCase(
            lstItemDetails.getSelectedValue().toString() ) ) {
              saveRequired=true;
        }else if (lstItemDetails != null && lstItemDetails.getSelectedValue() != null &&
            minuteEntryInfoBean.getProtocolNumber() != null &&
            minuteEntryInfoBean.getMinuteEntryTypeCode() == 4 &&
            !minuteEntryInfoBean.getOtherItemDesc().equalsIgnoreCase(
            lstItemDetails.getSelectedValue().toString() ) ) {
              saveRequired=true;
        }
        //COEUSQA:3233 - Remove Protocol Review Comment from minutes entry window - Start
        if(Integer.parseInt(entryType.getCode()) == paramMinuteEntryCode) {
            saveRequired=false;
        }
        //COEUSQA:3233 - End
        if (saveRequired){
            //alert message
            int resultConfirm = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
            try{
                if (resultConfirm == 0) {
                    if (validateFields()){
                        sendDataToUpdate();
                        dlgWindow.dispose();
                    }
                }else if (resultConfirm == 1){
                    dlgWindow.dispose();
                }else {
                    return;
                }
            }catch(Exception ex){
                log(ex.getMessage());
            }
        } else {
            //close window
            dlgWindow.dispose();
        }
    }
    
    //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
     /**
     * Method to get the minuteEntryTypeCode from the 'IRB_MINUTE_TYPE_REVIEWER_COMMENT' parameter
     * @return paramMinuteEntryCode - int
     */
    private int getparaemterMinuteEntryTypeCode() {
        int paramMinuteEntryCode = 0;
        RequesterBean requester = new RequesterBean();
        Vector nonDefProtocols = new Vector();
        requester.setFunctionType(GET_PARAMETER_MINUTE_ENTRY_CODE);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()){
            paramMinuteEntryCode = Integer.parseInt((String) response.getDataObject());
        }
        return paramMinuteEntryCode;
    }
    //COEUSQA-2290 : End
    
    private void btnLookUp_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLookUp_actionPerformed
        if (evt.getSource().equals(btnLookUp)) {
            ContingencyLookUpWindow contingencyLookUp = new
                                                    ContingencyLookUpWindow();
            contingencyLookUp.setMinuteEntryForm(this);
            contingencyLookUp.showForm(CoeusGuiConstants.getMDIForm(),true);
        }
    }//GEN-LAST:event_btnLookUp_actionPerformed

    private void btnCancel_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel_actionPerformed
       if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
            dlgWindow.dispose();
       }else{
            isSaveRequired();
       }
    }//GEN-LAST:event_btnCancel_actionPerformed

    private void btnGenAttendence_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenAttendence_actionPerformed

        String attendees=null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                    "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_ATTENDEES");
        request.setId(scheduleID);
        AppletServletCommunicator comm = new
                                AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            attendees = (String) response.getDataObject();
            if (attendees == null ) {
                log(coeusMessageResources.parseMessageKey(
                                "minuteEntryFrm_exceptionCode.1161"));
            }
        }
        txtArEntry.setText(attendees);
    }//GEN-LAST:event_btnGenAttendence_actionPerformed

    private void btnSaveAndNew_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAndNew_actionPerformed
        if (validateFields()){
            sendDataToUpdate();
            initAll();
        }
    }//GEN-LAST:event_btnSaveAndNew_actionPerformed

    private void btnSaveAndClose_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAndClose_actionPerformed
        if (validateFields()){
            sendDataToUpdate();
            dlgWindow.dispose();
        }
    }//GEN-LAST:event_btnSaveAndClose_actionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnGenAttendence;
    public javax.swing.JButton btnLookUp;
    public javax.swing.JButton btnSaveClose;
    public javax.swing.JButton btnSaveNew;
    public javax.swing.JCheckBox chkFinal;
    public javax.swing.JCheckBox chkPrivateFlag;
    public javax.swing.JLabel lblContingencyLabel;
    public javax.swing.JLabel lblEntryType;
    public javax.swing.JLabel lblFinal;
    public javax.swing.JLabel lblPrivateFlagLabel;
    public javax.swing.JLabel lblTypeDescLabel;
    public javax.swing.JLabel lblTypeDisplay;
    public javax.swing.JList lstEntryTypes;
    public javax.swing.JList lstItemDetails;
    public javax.swing.JScrollPane scrPnTypeDesc;
    public javax.swing.JScrollPane scrPnTypeMembers;
    public javax.swing.JScrollPane scrPnTypes;
    public javax.swing.JTextArea txtArEntry;
    public javax.swing.JTextField txtContingencyCode;
    // End of variables declaration//GEN-END:variables
}
