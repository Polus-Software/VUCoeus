/*
 * CommentsForm.java
 *
 * Created on October 20, 2003, 5:52 PM
 */

/* PMD check performed, and commented unused imports and variables on 14-NOV-2011
 * by Bharati 
 */

package edu.mit.coeus.iacuc.gui;

/**
 *
 * @author  sharathk
 */

//import edu.mit.coeus.bean.UserInfoBean;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.brokers.*; //Added by Vyjayanthi for IRB Enhancement - 10/08/2004

public class CommentsForm extends javax.swing.JPanel implements ActionListener, WindowListener, FocusListener{
    
    private CoeusDlgWindow coeusDlgWindow;
    private Frame owner = CoeusGuiConstants.getMDIForm();
    private boolean modal;
    
    //private Vector vecMinuteEntryInfoBean;
    private MinuteEntryInfoBean minuteEntryInfoBean;
    private ReviewCommentsForm reviewCommentsForm;
    private ContingencyLookUpWindow contingencyLookUpWindow;
    private CoeusMessageResources messageResources = CoeusMessageResources.getInstance();
    private String contingencyCode;
    private char functionType;
    private static final int HEIGHT = 260;
    private static final int WIDTH = 525;
    private static final String EMPTY_STRING = "";
    private final String ENTER_COMMENTS = 
        messageResources.parseMessageKey("commentsForm_exceptionCode.1119");
    private final String INVALID_CONTINGENCY_CODE = 
        messageResources.parseMessageKey("commentsForm_exceptionCode.1120");
    private final String SAVE_CHANGES = 
        messageResources.parseMessageKey("user_details_exceptionCode.2547");
    
    //Added by Vyjayanthi for IRB Enhancement - 10/08/2004
    private static final String SCHEDULE_MAINTENENCE_SERVLET = "/scheduleMaintSrvlt";
    
    //Added by Vyjayanthi for IRB Enhancement - 10/08/2004
    /** Holds the schedule id used for releasing the schedule lock
     */
    private java.util.HashMap hmScheduleData;
    
    //Added by Vyjayanthi for IRB Enhancement - 10/08/2004
    private static final int MINUTE_ENTRY_TYPE_CODE = 3;
    
    //Added by Vyjayanthi for IRB Enhancement - 10/08/2004 - Start
    private static final String SCHEDULE_ID = "SCHEDULE_ID";
    private static final String PROTOCOL_NUMBER = "PROTOCOL_NUMBER";
    private static final String SUBMISSION_NUMBER = "SUBMISSION_NUMBER";
    private static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
    private boolean dataSaved;
    //Added by Vyjayanthi for IRB Enhancement - 10/08/2004 - End
  //Modified for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
  // 3282: Reviewer view of Protocols
  // private UserInfoBean userInfoBean;
    private String personId;
    //COEUSQA-2291 : End
    
    /** Creates new form CommentsForm */
    public CommentsForm(boolean modal) {
        this.modal = modal;
        initComponents();
        postInitComponents();
        limitCharsInTextArea();
        registerComponents();
    }
    
    public void limitCharsInTextArea(){
        
        LimitedPlainDocument plainDocument = new LimitedPlainDocument(3878);
        txtArEntry.setDocument(plainDocument);
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblEntry = new javax.swing.JLabel();
        scrPnEntry = new javax.swing.JScrollPane();
        txtArEntry = new javax.swing.JTextArea();
        chkPrivate = new javax.swing.JCheckBox();
        btnSaveAndClose = new javax.swing.JButton();
        btnSaveAndNew = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblContingencyCode = new javax.swing.JLabel();
        txtContingencyCode = new javax.swing.JTextField();
        btnContingencyLookup = new javax.swing.JButton();
        chkFinalFlag = new javax.swing.JCheckBox();
        txtUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        txtUpdateTimestamp = new edu.mit.coeus.utils.CoeusTextField();
        lblUpdateUser = new javax.swing.JLabel();
        lblUpdateTimestamp = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(510, 260));
        setMinimumSize(new java.awt.Dimension(510, 260));
        setPreferredSize(new java.awt.Dimension(510, 260));
        lblEntry.setFont(CoeusFontFactory.getLabelFont());
        lblEntry.setText("Entry");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 30);
        add(lblEntry, gridBagConstraints);

        scrPnEntry.setMinimumSize(new java.awt.Dimension(400, 175));
        scrPnEntry.setPreferredSize(new java.awt.Dimension(400, 150));
        txtArEntry.setFont(CoeusFontFactory.getNormalFont());
        txtArEntry.setLineWrap(true);
        txtArEntry.setWrapStyleWord(true);
        scrPnEntry.setViewportView(txtArEntry);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        add(scrPnEntry, gridBagConstraints);

        chkPrivate.setFont(CoeusFontFactory.getLabelFont());
        chkPrivate.setText("Private");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(chkPrivate, gridBagConstraints);

        btnSaveAndClose.setFont(CoeusFontFactory.getLabelFont());
        btnSaveAndClose.setMnemonic('S');
        btnSaveAndClose.setText("Save & Close");
        btnSaveAndClose.setMaximumSize(new java.awt.Dimension(110, 24));
        btnSaveAndClose.setMinimumSize(new java.awt.Dimension(110, 24));
        btnSaveAndClose.setPreferredSize(new java.awt.Dimension(110, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnSaveAndClose, gridBagConstraints);

        btnSaveAndNew.setFont(CoeusFontFactory.getLabelFont());
        btnSaveAndNew.setMnemonic('N');
        btnSaveAndNew.setText("Save & New");
        btnSaveAndNew.setMaximumSize(new java.awt.Dimension(110, 24));
        btnSaveAndNew.setMinimumSize(new java.awt.Dimension(110, 24));
        btnSaveAndNew.setPreferredSize(new java.awt.Dimension(110, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnSaveAndNew, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(110, 24));
        btnCancel.setMinimumSize(new java.awt.Dimension(110, 24));
        btnCancel.setPreferredSize(new java.awt.Dimension(110, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnCancel, gridBagConstraints);

        lblContingencyCode.setFont(CoeusFontFactory.getLabelFont());
        lblContingencyCode.setText("Contingency Code : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(lblContingencyCode, gridBagConstraints);

        txtContingencyCode.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 4));
        txtContingencyCode.setFont(CoeusFontFactory.getNormalFont());
        txtContingencyCode.setMinimumSize(new java.awt.Dimension(50, 22));
        txtContingencyCode.setPreferredSize(new java.awt.Dimension(50, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 10);
        add(txtContingencyCode, gridBagConstraints);

        btnContingencyLookup.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.FIND_ICON)));
        btnContingencyLookup.setMaximumSize(new java.awt.Dimension(34, 24));
        btnContingencyLookup.setMinimumSize(new java.awt.Dimension(25, 22));
        btnContingencyLookup.setPreferredSize(new java.awt.Dimension(25, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 6);
        add(btnContingencyLookup, gridBagConstraints);

        chkFinalFlag.setFont(CoeusFontFactory.getLabelFont());
        chkFinalFlag.setText("Final");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 61);
        add(chkFinalFlag, gridBagConstraints);

        txtUpdateUser.setMinimumSize(new java.awt.Dimension(140, 21));
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtUpdateUser, gridBagConstraints);

        txtUpdateTimestamp.setMinimumSize(new java.awt.Dimension(140, 21));
        txtUpdateTimestamp.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtUpdateTimestamp, gridBagConstraints);

        lblUpdateUser.setFont(new java.awt.Font("Arial", 1, 11));
        lblUpdateUser.setText("Update User: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblUpdateUser, gridBagConstraints);

        lblUpdateTimestamp.setFont(new java.awt.Font("Arial", 1, 11));
        lblUpdateTimestamp.setText("Last Update:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblUpdateTimestamp, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    private void postInitComponents() {
        //Code for focus traversal - start
        java.awt.Component[] components = {txtContingencyCode, btnContingencyLookup, chkFinalFlag, chkPrivate, txtArEntry, btnSaveAndClose, btnSaveAndNew, btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        //Code for focus traversal - end
        
        coeusDlgWindow = new CoeusDlgWindow(owner, modal);
        coeusDlgWindow.getContentPane().add(this);
        coeusDlgWindow.setSize(WIDTH, HEIGHT);
        coeusDlgWindow.setResizable(false);
        coeusDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        coeusDlgWindow.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae ) {
                    performCancelAction();
                }
        });
    }
    
    public void display() {
        if(minuteEntryInfoBean == null){
            minuteEntryInfoBean = new MinuteEntryInfoBean();
            minuteEntryInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            // 3282: Reviewer view of Protocols - Start
            txtUpdateUser.setText("");
            txtUpdateTimestamp.setText("");
            // 3282: Reviewer view of Protocols - End
        } 
        coeusDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        coeusDlgWindow.setVisible(true);
    }
    
    public void setTitle(String title) {
        coeusDlgWindow.setTitle(title);
    }
    
    public void setFormData(MinuteEntryInfoBean minuteEntryInfoBean) {
        this.minuteEntryInfoBean = minuteEntryInfoBean;
        if(minuteEntryInfoBean.getAcType() == null){
            this.minuteEntryInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
        }        
        txtArEntry.setText(minuteEntryInfoBean.getMinuteEntry());
        chkPrivate.setSelected(minuteEntryInfoBean.isPrivateCommentFlag());
        // 3282: Reviewer View of Protocol materials
        chkFinalFlag.setSelected(minuteEntryInfoBean.isFinalFlag());
        
        String code = minuteEntryInfoBean.getProtocolContingencyCode();
        if(code != null && !code.equals(EMPTY_STRING)){
            txtContingencyCode.setText(code);
            if(contingencyLookUpWindow == null) contingencyLookUpWindow = new ContingencyLookUpWindow();
            txtArEntry.setText(contingencyLookUpWindow.getDescription(code));
            txtArEntry.setEditable(false);
        }
        //
        txtUpdateUser.setText(minuteEntryInfoBean.getUpdateUserName());
        java.sql.Timestamp updateTimestamp = minuteEntryInfoBean.getUpdateTimestamp();
        if(updateTimestamp != null){
            txtUpdateTimestamp.setText(CoeusDateFormat.format(
                    updateTimestamp.toString()));
        }
        //

    }
    public void setFunctionType(char fType){
        this.functionType = fType;
        boolean enable = functionType == CoeusGuiConstants.DISPLAY_MODE ? false : true;
            txtContingencyCode.setEnabled(enable);
            txtArEntry.setEnabled(enable);
            chkPrivate.setEnabled(enable);
            // 3282: Reviewer View of Protocol materials
            chkFinalFlag.setEnabled(enable);
            btnContingencyLookup.setEnabled(enable);
            btnSaveAndClose.setEnabled(enable);
            btnSaveAndNew.setEnabled(enable);
            //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - start
            //While adding new comments privateFlag should be checked by default
            if(functionType == CoeusGuiConstants.ADD_MODE){
                chkPrivate.setSelected(true);
            }
            //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - end
        if( !enable ) {
            Color bgColor = (Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            txtContingencyCode.setBackground(bgColor);
            txtContingencyCode.setDisabledTextColor(Color.black);
            txtArEntry.setBackground(bgColor);
            txtArEntry.setDisabledTextColor(Color.black);
        }else{
            txtContingencyCode.setBackground(Color.white);
            txtArEntry.setBackground(Color.white);
        }
            
        // 3282: Reviewer view of Protocols - Start
        txtUpdateTimestamp.setEnabled(false);
        txtUpdateUser.setEnabled(false);
        //  3282: Reviewer view of Protocols - End
    }
    public void reset() {
        minuteEntryInfoBean = null;
        txtArEntry.setEditable(true);
        txtContingencyCode.setText(EMPTY_STRING);
        txtArEntry.setText(EMPTY_STRING);
        chkPrivate.setSelected(false);
        // 3282: Reviewer View of Protocol materials
        chkFinalFlag.setSelected(false);
        requestDefaultFocusForComponent();
    }
    private void requestDefaultFocusForComponent(){
        if( txtContingencyCode.isEnabled() ) {
            txtContingencyCode.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
    private void registerComponents() {
        btnCancel.addActionListener(this);
        btnSaveAndClose.addActionListener(this);
        btnSaveAndNew.addActionListener(this);
        btnContingencyLookup.addActionListener(this);
        
        coeusDlgWindow.addWindowListener(this);
        
        txtContingencyCode.addFocusListener(this);
    }
    
    public void setReviewCommentsForm(ReviewCommentsForm reviewCommentsForm) {
        this.reviewCommentsForm = reviewCommentsForm;
    }
    
    private void performCancelAction(){
        if(! txtArEntry.getText().equals(minuteEntryInfoBean.getMinuteEntry())){
            int selection = CoeusOptionPane.showQuestionDialog(SAVE_CHANGES, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.SELECTION_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                saveAndClose();
                return ;
            }
        }
        minuteEntryInfoBean.setAcType(null);
        coeusDlgWindow.setVisible(false);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnCancel)) {
            performCancelAction();
        }else if(source.equals(btnSaveAndClose)) {
            saveAndClose();
            //Modified by Vyjayanthi for IRB Enhancement - 12/08/2004
            //Display the Review Comments Screen only if data is saved
            if( dataSaved ){
                reviewCommentsForm.display();
            }
        }else if(source.equals(btnSaveAndNew)) {
            if(txtArEntry.getText().trim().equals(EMPTY_STRING)) {
                CoeusOptionPane.showErrorDialog(ENTER_COMMENTS);
                return ;
            }
            minuteEntryInfoBean.setMinuteEntry(txtArEntry.getText());
            minuteEntryInfoBean.setPrivateCommentFlag(chkPrivate.isSelected());
            // 3282: Reviewer View of Protocol materials - Start
            minuteEntryInfoBean.setFinalFlag(chkFinalFlag.isSelected());
               //Commented for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
//          if(userInfoBean != null){
//                minuteEntryInfoBean.setPersonId(userInfoBean.getPersonId());
//          }
            if(TypeConstants.INSERT_RECORD.equals(minuteEntryInfoBean.getAcType()) && personId != null){
                minuteEntryInfoBean.setPersonId(personId);
            }
            //COEUSQA-2291 : End
            // 3282: Reviewer view of Protocol materials - End
            if(! txtContingencyCode.getText().equals(EMPTY_STRING)) {
                minuteEntryInfoBean.setProtocolContingencyCode(txtContingencyCode.getText());
                minuteEntryInfoBean.setProtocolContingencyDesc(txtArEntry.getText());
            }else{
                minuteEntryInfoBean.setProtocolContingencyCode(EMPTY_STRING);
                minuteEntryInfoBean.setProtocolContingencyDesc(EMPTY_STRING);
            }
            
            //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - start
            //While adding new comments privateFlag should be checked by default
            if(TypeConstants.INSERT_RECORD.equals(minuteEntryInfoBean.getAcType()) || TypeConstants.UPDATE_RECORD.equals(minuteEntryInfoBean.getAcType())){
                chkPrivate.setSelected(true);
                chkFinalFlag.setSelected(false);
            }
            //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - end
            
            //Added by Vyjayanthi - 10/08/2004 for IRB Enhancement - Start
            //To save the review comments directly to the database
            saveToDatabase();

            //To refresh the data displayed after saving
            if(reviewCommentsForm != null) {
                try{
                    reviewCommentsForm.setRefreshRequired(true);
                    reviewCommentsForm.setFormData(
                    (String)hmScheduleData.get(PROTOCOL_NUMBER), 
                    ((Integer)hmScheduleData.get(SUBMISSION_NUMBER)).intValue(),
                    ((Integer)hmScheduleData.get(SEQUENCE_NUMBER)).intValue());
                    reviewCommentsForm.setRowSelection();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
            //Added by Vyjayanthi - 10/08/2004 for IRB Enhancement - End
            
            minuteEntryInfoBean = new MinuteEntryInfoBean();
            minuteEntryInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            
            txtContingencyCode.setText(EMPTY_STRING);
            txtArEntry.setText(EMPTY_STRING);
            txtArEntry.setEditable(true);
            txtContingencyCode.requestFocusInWindow();
        }else if(source.equals(btnContingencyLookup)) {
            if(contingencyLookUpWindow == null) contingencyLookUpWindow = new ContingencyLookUpWindow();
            
            if(! txtContingencyCode.getText().equals(EMPTY_STRING)) {
                contingencyLookUpWindow.setSelectedRow(txtContingencyCode.getText());
            }
            
            int selection = contingencyLookUpWindow.showForm(CoeusGuiConstants.getMDIForm(), true);
            if(selection == ContingencyLookUpWindow.OK) {
                ComboBoxBean comboBoxBean = contingencyLookUpWindow.getSelectedBean();
                txtContingencyCode.setText(comboBoxBean.getCode());
                txtArEntry.setText(comboBoxBean.getDescription());
                txtArEntry.setEditable(false);
            }
        }
    }
    
    private void saveAndClose() {
        if(txtArEntry.getText().trim().equals(EMPTY_STRING)) {
            CoeusOptionPane.showErrorDialog(ENTER_COMMENTS);
            //Reset the flag
            dataSaved = false;  //Added by Vyjayanthi for IRB Enhancement - 12/08/2004
            return ;
        }
        minuteEntryInfoBean.setMinuteEntry(txtArEntry.getText());
        minuteEntryInfoBean.setPrivateCommentFlag(chkPrivate.isSelected());
        // 3282: Reviewer View of Protocol materials - Start
        minuteEntryInfoBean.setFinalFlag(chkFinalFlag.isSelected());
         //Commented for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
//        if(userInfoBean != null){
//            minuteEntryInfoBean.setPersonId(userInfoBean.getPersonId());
//        }
        
        if(TypeConstants.INSERT_RECORD.equals(minuteEntryInfoBean.getAcType()) && personId != null){
            minuteEntryInfoBean.setPersonId(personId);
        }
        //COEUSQA-2291 : End
        // 3282: Reviewer View of Protocol materials - End
        if(! txtContingencyCode.getText().equals(EMPTY_STRING)) {
            minuteEntryInfoBean.setProtocolContingencyCode(txtContingencyCode.getText());
            minuteEntryInfoBean.setProtocolContingencyDesc(txtArEntry.getText());
        }else{
            minuteEntryInfoBean.setProtocolContingencyCode(EMPTY_STRING);
            minuteEntryInfoBean.setProtocolContingencyDesc(EMPTY_STRING);
        }
        
        //Added by Vyjayanthi - 10/08/2004 for IRB Enhancement - Start
        //To save the review comments directly to the database
        saveToDatabase();
        
        //To refresh the data displayed after saving
        if(reviewCommentsForm != null && hmScheduleData != null) {
            try{
                reviewCommentsForm.setRefreshRequired(true);
                reviewCommentsForm.setFormData(
                (String)hmScheduleData.get(PROTOCOL_NUMBER), 
                ((Integer)hmScheduleData.get(SUBMISSION_NUMBER)).intValue(),
                ((Integer)hmScheduleData.get(SEQUENCE_NUMBER)).intValue());
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        //Added by Vyjayanthi - 10/08/2004 for IRB Enhancement - End
        
        coeusDlgWindow.setVisible(false);
    }
    
    public char getFunctionType(){
        return functionType;
    }

    //Added by Vyjayanthi - 10/08/2004 for IRB Enhancement - Start
    /** Saves the Review Comments to the database
     */
    private void saveToDatabase(){
        //Commented for COEUSDEV-237 : Investigator cannot see review comments - Start 
//        RequesterBean requesterBean = new RequesterBean();
//        Vector vecToServer = new Vector();
        //COEUSDEV-237 : end
        String acType = minuteEntryInfoBean.getAcType();
        
        if( hmScheduleData != null ){
            //Commented for COEUSDEV-237 : Investigator cannot see review comments - Start 
//                minuteEntryInfoBean.setScheduleId((String)hmScheduleData.get(SCHEDULE_ID));
            //COEUSDEV-237 : End
            minuteEntryInfoBean.setProtocolNumber((String)hmScheduleData.get(PROTOCOL_NUMBER));
            minuteEntryInfoBean.setSubmissionNumber(((Integer)hmScheduleData.get(SUBMISSION_NUMBER)).intValue());
            minuteEntryInfoBean.setSequenceNumber(((Integer)hmScheduleData.get(SEQUENCE_NUMBER)).intValue());
          //Modified for COEUSDEV-237 : Investigator cannot see review comments - Start 
          //To update new scheduleid when a scheduleid is changed in the submission list
//        minuteEntryInfoBean.setMinuteEntryTypeCode(MINUTE_ENTRY_TYPE_CODE);
//        
//        vecToServer.add(minuteEntryInfoBean);
//
//        Vector dataObjects = new Vector();
//        Vector vecData = new Vector();
//        dataObjects.add(vecToServer);
//        dataObjects.add(new Boolean(false));
//        requesterBean.setDataObjects(dataObjects);
//        requesterBean.setFunctionType('E');

////        requesterBean.setId(minuteEntryInfoBean.getScheduleId());
//        requesterBean.setId((String)hmScheduleData.get(SCHEDULE_ID));
//        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
//        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SCHEDULE_MAINTENENCE_SERVLET);
//        appletServletCommunicator.setRequest(requesterBean);
//        appletServletCommunicator.send();
//        
//        ResponderBean responderBean = appletServletCommunicator.getResponse();
//        if( responderBean != null && responderBean.isSuccessfulResponse() ) {
//            vecData = (Vector)responderBean.getDataObject();
//            dataSaved = true;
//        }else if ( !responderBean.isSuccessfulResponse() ){
//            CoeusOptionPane.showInfoDialog(responderBean.getMessage());
//        }
            
            if(TypeConstants.UPDATE_RECORD.equals(minuteEntryInfoBean.getAcType()) &&
                    (minuteEntryInfoBean.getScheduleId()!= null &&
                    !minuteEntryInfoBean.getScheduleId().equals((String)hmScheduleData.get(SCHEDULE_ID)))){
                
                minuteEntryInfoBean.setAcType(TypeConstants.DELETE_RECORD);
                servletCall();
                minuteEntryInfoBean.setScheduleId((String)hmScheduleData.get(SCHEDULE_ID));
                minuteEntryInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            }else{
                minuteEntryInfoBean.setScheduleId((String)hmScheduleData.get(SCHEDULE_ID));
            }
            servletCall();
        }
        //COEUSDEV-237 : End 
    }
    
     //Added for COEUSDEV-237 : Investigator cannot see review comments - Start 
    /**
     * Method to make a call to servlet
     *
     */
    private void servletCall(){
        RequesterBean requesterBean = new RequesterBean();
        Vector vecToServer = new Vector();
        // Modified for COEUSQA-3124 : IRB and IACUC-review comment code should not change - Start
//        minuteEntryInfoBean.setMinuteEntryTypeCode(MINUTE_ENTRY_TYPE_CODE);
        // Only the newly added review comments will have the MINUTE_ENTRY_TYPE_CODE as '3'
        // For modified review comments the minute enty type code remains same
        if(TypeConstants.INSERT_RECORD.equals(minuteEntryInfoBean.getAcType())){
            minuteEntryInfoBean.setMinuteEntryTypeCode(MINUTE_ENTRY_TYPE_CODE);
        }
        // Modified for COEUSQA-3124 : IRB and IACUC-review comment code should not change - End
        
        vecToServer.add(minuteEntryInfoBean);
        
        Vector dataObjects = new Vector();
        Vector vecData = new Vector();
        dataObjects.add(vecToServer);
        dataObjects.add(new Boolean(false));
        requesterBean.setDataObjects(dataObjects);
        requesterBean.setFunctionType('k');
        requesterBean.setId((String)hmScheduleData.get(SCHEDULE_ID));
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SCHEDULE_MAINTENENCE_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if( responderBean != null && responderBean.isSuccessfulResponse() ) {
            vecData = (Vector)responderBean.getDataObject();
            dataSaved = true;
        }else if ( !responderBean.isSuccessfulResponse() ){
            CoeusOptionPane.showInfoDialog(responderBean.getMessage());
        }
    }
    //COEUSDEV-237 : End
    
    //Added by Vyjayanthi - 10/08/2004 for IRB Enhancement - Start
    /**
     * Setter for property hmScheduleData.
     * @param hmScheduleData New value of property hmScheduleData.
     */
    public void setScheduleData(java.util.HashMap hmScheduleData) {
        this.hmScheduleData = hmScheduleData;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnContingencyLookup;
    public javax.swing.JButton btnSaveAndClose;
    public javax.swing.JButton btnSaveAndNew;
    public javax.swing.JCheckBox chkFinalFlag;
    public javax.swing.JCheckBox chkPrivate;
    public javax.swing.JLabel lblContingencyCode;
    public javax.swing.JLabel lblEntry;
    public javax.swing.JLabel lblUpdateTimestamp;
    public javax.swing.JLabel lblUpdateUser;
    public javax.swing.JScrollPane scrPnEntry;
    public javax.swing.JTextArea txtArEntry;
    public javax.swing.JTextField txtContingencyCode;
    public edu.mit.coeus.utils.CoeusTextField txtUpdateTimestamp;
    public edu.mit.coeus.utils.CoeusTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables
    
    //For testing purpose only
    public static void main(String s[]){
        Frame frame = new Frame();
        CommentsForm commentsForm = new CommentsForm(true);
        commentsForm.display();
    }
    
    public void windowActivated(java.awt.event.WindowEvent windowEvent) {
        requestDefaultFocusForComponent();
    }
    
    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
    }
    
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        performCancelAction();
    }
    
    public void windowDeactivated(java.awt.event.WindowEvent windowEvent) {
    }
    
    public void windowDeiconified(java.awt.event.WindowEvent windowEvent) {
    }
    
    public void windowIconified(java.awt.event.WindowEvent windowEvent) {
    }
    
    public void windowOpened(java.awt.event.WindowEvent windowEvent) {
    }
    
    public void focusGained(FocusEvent focusEvent) {
        
    }
    
    public void focusLost(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()) return ;
        if(txtContingencyCode.getText().trim().equals(EMPTY_STRING)) {
            //txtArEntry.setText(EMPTY_STRING);
            txtContingencyCode.setText(EMPTY_STRING);
            txtArEntry.setEditable(true);
            return ;
        }
        
        if(contingencyLookUpWindow == null) contingencyLookUpWindow = new ContingencyLookUpWindow();
        
        String description = contingencyLookUpWindow.getDescription(txtContingencyCode.getText().trim());
        if(description == null){
            CoeusOptionPane.showErrorDialog(INVALID_CONTINGENCY_CODE);
            txtContingencyCode.setText(EMPTY_STRING);
            txtArEntry.setText(EMPTY_STRING);
            txtArEntry.setEditable(true);
            return ;
        }
        contingencyCode = txtContingencyCode.getText();
        txtArEntry.setText(description);
        txtArEntry.setEditable(false);
    }
    //Modified for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
    // 3282: Reviewer View of Protocol materials - Start
//    public void setUserInfoBean(UserInfoBean userInfoBean) {
//        this.userInfoBean = userInfoBean;
//    }
    // 3282: Reviewer View of Protocol materials - End
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    //COEUSQA-2291 : End
}

