/*
 * @(#)ProtocolCorrespDialog.java  1.0  08/08/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.event.TextListener;
import java.awt.event.TextEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Vector;
import javax.swing.ImageIcon;
//import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusFileFilter;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.irb.bean.CorrespondenceTypeFormBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusLookAndFeelChooser;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.CoeusConstants;


/**
 * This class Uploads a template to Server.
 * User has to select an XML file(Template) to upload to database
 *
 * @author Manoj Kumar .A
 */

public class ProtocolCorrespDialog extends edu.mit.coeus.gui.CoeusDlgWindow
implements ActionListener{
    
    /** CoeusAppletMDIForm is parent window to current dialog window
     */
    private CoeusAppletMDIForm mdiForm; /* Parent window to current dialog window */
    
    /* Servelet to get All Committee Details*/
    /** servelet name to connect
     */
    private final String CORR_SERVLET = "/comMntServlet";
    /** Connection string to connect to server
     */
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ CORR_SERVLET;
    /** Mode to get all Committee Details
     */
    private final char GET_MODE = 'G';  /* Mode to connect to Servlet (Get Mode) */
    /** Vector to hold all committee details
     */
    private Vector commDetails;
    /** Constant gives max size of descrption
     */
    private final int DESC_MAXSIZE = 200;   /* Max Size Length in the Description Text Area Component*/
    /** file chooser to select a template
     */
    private CoeusFileChooser fileChooser = null; /* File Chooser to get Template File */
    
    /** byte array to hold the file data
     */
    private byte[] templateData = null; /* TO hold to File Data*/
    /** this holds whther the save is required to database or not
     */
    private boolean saveRequired = false; /* To check whether save to databse required*/
    /** this bean contains all the details
     */
    private CorrespondenceTypeFormBean correspBeanData; /* Bean to hold all the details */
    /** checks whether file is selected or not
     */
    private boolean fileSelected = false; /* To check whether file has been selected or not */
    private boolean changed = false;
    
    /** this varaible used to hold Coues Message Resources Instance
     * It will parse take string as error code and parses it as defined in Messages property file
     */
    private CoeusMessageResources coeusMessageResources;
    private boolean modified = false;
    private int[] existingCorrespTypeCodes;
    private Vector correspData;
    private static final char COMMITTEE_LIST_FOR_MODULE = 'z';
    String correspCode;
    public ProtocolCorrespDialog(){
    }
    
    /**Creates a window to Upload a Template file to Database
     *@param pareant takes CouesAppletMdiForm as parent window for current dialog window
     *@param title takes String to display the title of window
     *@param modal takes boolean value to display current dialog window as mode/modeless
     *@param nextCorrespTypeCode takes integer value to display next correspondent type code value
     */
    public ProtocolCorrespDialog(CoeusAppletMDIForm parent, String title,
    boolean modal,Vector correspData,String correspCode)throws Exception {
        super(parent, title,modal);
        mdiForm = parent;
        initComponents();                                    /* to initialize all components on the dialog window*/
        correspBeanData =  new CorrespondenceTypeFormBean();                 /* Creating a bean instance to hold the new value*/
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        /* sets the next correspondent
                  type code in the text code textfield*/
        //this.existingCorrespTypeCodes = existingtCorrespTypeCodes;
        this.correspData = correspData;
        this.correspCode = correspCode;
        addListeners();                                     /* Adds Listeners to components*/
        this.setLocationRelativeTo(parent);                 /* Sets current window relative to parent*/
        getCommitteeDetails();                         /* gets all committe details from the database*/
        lblTitle.setText("Upload template for committee");
        showDialog();                                       /* Displays Dialog Window*/
    }
    /**Creates a window to Upload a Template file to Database
     *@param pareant takes CouesAppletMdiForm as parent window for current dialog window
     *@param title takes String to display the title of window
     *@param modal takes boolean value to display current dialog window as mode/modeless
     *@param correspBean takes CorrepondenceTypeFormBean bean instance and used to update with new
     *  template and description values
     */
  /*  public ProtocolCorrespDialog(CoeusAppletMDIForm parent, String title,
    boolean modal,Vector correspData,String correspCode,String commId)throws Exception {
        
        super(parent, title,modal);
        mdiForm = parent;
        initComponents();                       // to initialize all components on the dialog window
        correspBeanData = new CorrespondenceTypeFormBean();                 // Creating a bean instance to hold the new value
        coeusMessageResources = CoeusMessageResources.getInstance();
        getCommitteeDetails();                  // Gets all Committee Details from Database        
        this.correspData = correspData;
        this.correspCode = correspCode;
        this.commId = commId;
        txtCommittee.setText(commId);
        addListeners();                         // adds Listeners to components
        this.setLocationRelativeTo(parent);     // Setting Current window relative to parent window
        lblTitle.setText("Upload template for committee");
        showDialog();                           // Displays Dialog Box
    }*/
    
    
    /** This method is called from within the constructor to
     * initialize the Dialog window with compents.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblTemplate = new javax.swing.JLabel();
        btnFind = new javax.swing.JButton();
        lblCommittee = new javax.swing.JLabel();
        txtCommittee = new javax.swing.JTextField();
        txtTemplate = new javax.swing.JTextField();
        btnUpload = new javax.swing.JButton();
        lblCommName = new javax.swing.JLabel();
        lblCommVal = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.FlowLayout());

        setDefaultCloseOperation(0);
        setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        setModal(true);
        setName("dialogCorresp");
        setResizable(false);
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        pnlMain.setMinimumSize(new java.awt.Dimension(435, 170));
        pnlMain.setPreferredSize(new java.awt.Dimension(435, 170));
        lblTemplate.setFont(CoeusFontFactory.getLabelFont());
        lblTemplate.setText("Template:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 22, 2);
        pnlMain.add(lblTemplate, gridBagConstraints);

        btnFind.setFont(CoeusFontFactory.getLabelFont());
        btnFind.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
            CoeusGuiConstants.FIND_ICON)));
        btnFind.setIconTextGap(0);
        btnFind.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 1, 0);
        pnlMain.add(btnFind, gridBagConstraints);

        lblCommittee.setFont(CoeusFontFactory.getLabelFont());
        lblCommittee.setText("Committee Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 3, 3, 2);
        pnlMain.add(lblCommittee, gridBagConstraints);

        txtCommittee.setFont(CoeusFontFactory.getNormalFont());
        txtCommittee.setMaximumSize(new java.awt.Dimension(225, 20));
        txtCommittee.setMinimumSize(new java.awt.Dimension(225, 20));
        txtCommittee.setPreferredSize(new java.awt.Dimension(225, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 3, 0);
        pnlMain.add(txtCommittee, gridBagConstraints);

        txtTemplate.setFont(CoeusFontFactory.getNormalFont());
        txtTemplate.setMaximumSize(new java.awt.Dimension(154, 20));
        txtTemplate.setMinimumSize(new java.awt.Dimension(154, 20));
        txtTemplate.setPreferredSize(new java.awt.Dimension(154, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 22, 0);
        pnlMain.add(txtTemplate, gridBagConstraints);

        btnUpload.setFont(CoeusFontFactory.getLabelFont());
        btnUpload.setMnemonic('B');
        btnUpload.setText("Browse");
        btnUpload.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnUpload.setMaximumSize(new java.awt.Dimension(77, 26));
        btnUpload.setMinimumSize(new java.awt.Dimension(70, 26));
        btnUpload.setPreferredSize(new java.awt.Dimension(70, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 22, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnUpload, gridBagConstraints);

        lblCommName.setFont(CoeusFontFactory.getLabelFont());
        lblCommName.setText("CommitteeName:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 2);
        pnlMain.add(lblCommName, gridBagConstraints);

        lblCommVal.setFont(CoeusFontFactory.getLabelFont());
        lblCommVal.setMaximumSize(new java.awt.Dimension(200, 20));
        lblCommVal.setMinimumSize(new java.awt.Dimension(200, 20));
        lblCommVal.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        pnlMain.add(lblCommVal, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnOK.setMaximumSize(new java.awt.Dimension(77, 26));
        btnOK.setMinimumSize(new java.awt.Dimension(70, 26));
        btnOK.setPreferredSize(new java.awt.Dimension(70, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCancel.setMaximumSize(new java.awt.Dimension(77, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(70, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(70, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnCancel, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(lblTitle, gridBagConstraints);

        getContentPane().add(pnlMain);

        pack();
    }//GEN-END:initComponents
    
    
    
    
    /** Closes the dialog */
    
    /** Displays Dialog Box */
    
    private void showDialog()throws Exception {
        lblCommName.grabFocus();
        this.show();
    }
    private void closeDialog() {
        this.dispose();
    }
    /**This Method adds listeners to components */
    private void addListeners() {
        
        /* TO hadnle Close Operation when Escape Pressed*/
        
        //- this.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
        ActionListener actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(modified){
                    try{
                        confirmClosing();       // Asking for confirmatoin if any thing changed
                    }catch(NumberFormatException ex){
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
                    }catch(CoeusClientException ex){
                        ex.printStackTrace();
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
                    }catch(Exception ex){
                        ex.printStackTrace();
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
                    }
                }else{
                    closeDialog();
                }
            }
        };
        getRootPane().registerKeyboardAction(actionListener, stroke,
        javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        /* Handling window closing operations*/
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if(modified){
                    try{
                        confirmClosing();   // Asking for confirmatoin if any thing changed
                    }catch(NumberFormatException ex){
                        ex.printStackTrace();
                    }catch(CoeusClientException ex){
                        ex.printStackTrace();
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
                    }catch(Exception ex){
                        ex.printStackTrace();
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
                    }
                }else{
                    closeDialog();
                }
            }
        });
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        btnFind.addActionListener(this);
        btnUpload.addActionListener(this);
        
        txtCommittee.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
                if(ke.getKeyCode() != KeyEvent.VK_ESCAPE){
                    modified = true;
                    changed = true;
                }
            }});
            txtCommittee.addKeyListener(new KeyAdapter(){
                public void keyReleased(KeyEvent ke){
                    if(ke.getKeyCode() != KeyEvent.VK_ESCAPE){
                        modified = true;
                    }
                }});
                txtTemplate.addKeyListener(new KeyAdapter(){
                    public void keyReleased(KeyEvent ke){
                        if(ke.getKeyCode() != KeyEvent.VK_ESCAPE){
                            modified = true;
                        }
                    }});
                    
          /* This Method adds focus Listener to Committee TextField. It creates
          Anonoums class focus adapter with method focus Lost. This will chech whether committe id
          selected is valid or not */
                    this.txtCommittee.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(FocusEvent focusEvent) {
                            if(changed == true){
                                changed = false;
                                String comm = txtCommittee.getText();
                                if(comm != null){
                                    comm = comm.trim();
                                    if(isValidCommID(comm)){
                                        if(checkDuplicateCommID(comm)){
                                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                            "correspType_exceptionCode.1010"));
                                            txtCommittee.setText("");
                                        }
                                    }else{
                                        txtCommittee.setText("");
                                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey
                                        ("correspType_exceptionCode.1001"));
                                    }
                                }
                            }
                        }
                    });
    }
    /** This Method receives the events getnereted by the registered componnets
     * @param actionEvent takes an instance of ActionEvent class to get the Source of Event */
    
    public void actionPerformed(ActionEvent actionEvent){
        Object comSelected = actionEvent.getSource();    /* Returns component which event generated*/
        try{
            if(comSelected == btnOK){                           /* Event generated is Button OK */
                if(doValidation()){
                    setCorrespBeanData();               /* Setting Updated Values */
                    setSaveRequired(true);                  /* Setting save is required */
                    this.dispose();                     /* disposing the dialog window*/
                }
            }else if(comSelected == btnCancel){      /* Selected Component is Cancel button*/
                if(modified){
                    confirmClosing();
                }else{
                    setSaveRequired(false);               /* Setting save not required */
                    this.dispose();                     /* disposing the Dialog WIndow */
                }
            }else if(comSelected == btnFind){        /* Selected Components is Find Button */
                    /*  Calling protocolCorrespCommitteeDialog Class
                        to display all committee details and to select one */
                ProtocolCorrespCommitteeDialog  committeeDialog =
                new ProtocolCorrespCommitteeDialog(mdiForm,true,commDetails,getColumnNames());
                /*  Returns selected Committee ID */
                String committeeIdSelected = committeeDialog.getSelectedCommitteeId();
                if(committeeIdSelected != null){ /* Checking whether committee Id selected or not */
                    if(checkDuplicateCommID(committeeIdSelected)){
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                        "correspType_exceptionCode.1010"));
                        txtCommittee.setText("");
                    }else{
                        txtCommittee.setText(committeeIdSelected);
                        /* Displaying Committee Name selected */
                        lblCommVal.setText(committeeDialog.getSelectedCommitteeName());
                        modified = true;
                    }
                }
            }else if(comSelected == btnUpload){            /* Selected Component is Button Upload*/
                fileChooser = new CoeusFileChooser(this); /* Creating an instance of fileChooser */
                fileChooser.showFileChooser();             /*   Opening a file Chooser */
                if(fileChooser.isFileSelected()){          /*   Checking whether a file is selected or not*/
                    modified = true;
                    String fileName = fileChooser.getSelectedFile();
                    if(fileName != null && !fileName.trim().equals("")){
                        int index = fileName.lastIndexOf('.');
                        if(index != -1 && index != fileName.length()){
                            String extension = fileName.substring(index+1,fileName.length());
                            if(extension != null && (extension.equalsIgnoreCase("xml")  || extension.equalsIgnoreCase("xsl"))){
                                templateData = fileChooser.getFile();   /*  Getting the Selected File Data*/
                                setFileSelected(true);                      /*  Setting File is Selected */
                                txtTemplate.setText(""+fileChooser.getSelectedFile());  /* Getting the Selected File Name*/
                            }else{
                                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                "correspType_exceptionCode.1011"));
                                templateData = null;        /*  Setting file data as null */
                                setFileSelected(false);       /*  Setting file is not selected */
                            }
                        }else{
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                            "correspType_exceptionCode.1012"));
                            templateData = null;        /*  Setting file data as null */
                            setFileSelected(false);       /*  Setting file is not selected */
                        }
                    }
                }else{ /* File is not selected*/
                    templateData = null;        /*  Setting file data as null */
                    setFileSelected(false);       /*  Setting file is not selected */
                }
            }
        }catch(NumberFormatException ex){
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
        }catch(CoeusClientException ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
        }catch(Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey("exceptionCode.unKnown"));
        }
    }
    private void confirmClosing()throws Exception{
        int option = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
        CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
        if(option == CoeusOptionPane.SELECTION_YES){
            if(doValidation()){
                setCorrespBeanData();               /* Setting Updated Values */
                setSaveRequired(true);                  /* Setting save is required */
                this.dispose();                     /* disposing the dialog window*/
            }
        }else if(option == CoeusOptionPane.SELECTION_NO){
            setSaveRequired(false);
            this.dispose();
        }
    }
    /** this method used to check whether file is selected or not
     * @return
     */
    private boolean isFileSelected() {
        return fileSelected;
    }
    /** This Method sets the variable saveRequired to true/false indicating save to
     *  Database is required or not
     */
    
    private void setSaveRequired(boolean value) {
        saveRequired = value;
    }
    /** This Method retunrs a varaible to check whether save is required or not
     *@return saveRequired  true if save to database required or false if not required
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    public String getCommitteeID(){
        return txtCommittee.getText();
    }
    private boolean doValidation()throws Exception{
        //    if(isValidCode(txtCode.getText())){
        if(isValidCommID(txtCommittee.getText().trim())){   /* Checking whether committee ID is Valid*/
            /* Checking file selected or not  */
            if(isFileSelected() || checkManuallySelected(txtTemplate.getText())){
                return true;
            }else{
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey("correspType_exceptionCode.1002"));
            }                           /* Template is not Selected */
        }else{       /*  committee ID is not Valid */
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey("correspType_exceptionCode.1013"));
        }
        
        return false;
    }
    /** This menthod is used to check whether file is selected manually. It calls
     *  getFileData method to get the file data
     * @param fileName this takes string contains file selected by user
     * @return returns true if file has selected manually by user
     * @throws it throws FileNotFound Exception if the method getfiledata throws the exception
     */
    public boolean checkManuallySelected(String fileName)throws Exception {
        if(!fileName.equals("") && fileName.trim().length()>0){     // checking filename is valid
            try{
                templateData = getFileData(fileName);           // calling to get the file data
                if(templateData != null){                       // returns true if file contains data else fasle
                    return true;
                }else{
                    return false;
                }
            }catch(java.io.FileNotFoundException ex){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                "correspType_exceptionCode.1004"));
            }
        }
        return false;
    }
    /** This Method sets the  varaible fileSelected to
     * true/false to check whether file selected or not
     */
    private void setFileSelected(boolean value) {
        fileSelected = value;
    }
    /** This Method sets the  all values to the bean to save to database
     */
    private void setCorrespBeanData()throws Exception {
        /* Setting all the updated values to the bean insetance */
        correspBeanData.setProtoCorrespTypeCode(Integer.parseInt(correspCode));
        correspBeanData.setCommitteeId(txtCommittee.getText());
        correspBeanData.setFileBytes(templateData);
        correspBeanData.setCommitteeName(lblCommName.getText());
    }
    /** This Method returns Correspondence type form bean
     *@return correpBeanData returns CorrespondenceTypeFormBean instance
     */
    public CorrespondenceTypeFormBean getCorrespBeantData() {
        return this.correspBeanData;
    }
    /** This Method gets the all committee details from the database
     * @throws Exception throws the message received from the server
     *
     */
     private void getCommitteeDetails()throws Exception {
        RequesterBean request = new RequesterBean();    /* Bean to connect to server*/
//        request.setFunctionType(GET_MODE);              /*  Setting funtion type to get mode */
        request.setDataObject(""+CoeusConstants.IRB_COMMITTEE_TYPE_CODE);
        request.setFunctionType(COMMITTEE_LIST_FOR_MODULE);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);    /* Applet servlet communicator to
                                                                 to conect to servlet */
        comm.send();
        ResponderBean response = comm.getResponse();            /* Getting Server Respnse*/
        if (response.isSuccessfulResponse()) {                  /* If success Full response */
            commDetails = new Vector();
            Vector defaultData = new Vector();
            defaultData.addElement("DEFAULT") ; //("INSTITUTE");  //prps changed this on nov 18 2003
            defaultData.addElement("Global Templates"); // prps changed this on nov 18 2003
            commDetails.addElement(defaultData);
            /*  Geeting all the Committee Deails from the Server and adding to Vector */
  //            Vector protoCommitteeData = (Vector)response.getDataObjects();
//            if( protoCommitteeData != null && protoCommitteeData.size() > 0) {
//                Vector commList = (Vector) protoCommitteeData.elementAt ;
                Vector commList =  response.getDataObjects();
                int listSize = commList.size();
                /* Retriveing the Committee Id and Committee Name to display in the Detail Dialog window */
                for(int rowIndex = 0;rowIndex < listSize;rowIndex++){
                    CommitteeMaintenanceFormBean committeeDetailBean =
                    (CommitteeMaintenanceFormBean)commList.elementAt(rowIndex);
                    Vector rowData = new Vector();
                    rowData.addElement((String)committeeDetailBean.getCommitteeId());
                    rowData.addElement((String)committeeDetailBean.getCommitteeName());
                    commDetails.addElement(rowData);
                }
//            }
        }else{
            throw new CoeusClientException(response.getMessage());
        }
    }
    /** checks whether the committee id is valid or not
     *@param String takes string parameter contains committee ID to check valid or not
     *@return returns true if committe id is valid
     *
     */
    private boolean isValidCommID(String commId) {
        int size = commDetails.size();
        commId = commId.toLowerCase();
        if(commId.equals("default")) // ("institute"))// prps changed this on nov 18 2003
            return true;
        else if(commDetails != null && size > 0 ) {
            for(int index = 0; index <  size; index++){
                Vector vecCommIds =(Vector)commDetails.elementAt(index);
                String strCommId = (String)vecCommIds.elementAt(0);
                strCommId = strCommId.toLowerCase();
                if(commId.equals(strCommId.trim())){
                    lblCommVal.setText((String)vecCommIds.elementAt(1));
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkDuplicateCommID(String comm){
        if(correspData != null && correspData.size()>0){
            for(int index=0;index<correspData.size();index++){
                CorrespondenceTypeFormBean correspBeanData =
                (CorrespondenceTypeFormBean)correspData.elementAt(index);
                int code = correspBeanData.getProtoCorrespTypeCode();
                if(correspCode.equals(""+code) && comm.equals(correspBeanData.getCommitteeId())){
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /** This method returns all column names
     *@return vecColNames returns as vector of column names
     */
    private Vector getColumnNames() {
        Vector vecColNames = new Vector();
        vecColNames.addElement("Committee ID");
        vecColNames.addElement("Name");
        return vecColNames;
    }
    
    
    /** this Methos used to get the File Data.
     * @param  this takes string as file name
     * @return  returns file data in byte array
     * @throws  throws File Not Found Exception if file is not available in the spefied path
     */
    private byte[] getFileData(String filePath)throws Exception {
        File file = new File(filePath);
        byte data[] = null;
        if(file.exists()){
            int index = filePath.lastIndexOf('.');
            if(index != -1 && index != filePath.length()){
                String extension = filePath.substring(index+1,filePath.length());
                if(extension != null && (extension.equalsIgnoreCase("xml") ||
                extension.equalsIgnoreCase("xsl") )){  // For bug Fix #1299
                    FileInputStream fileInputStream = new FileInputStream(file);
                    data = new byte[(int)file.length()];
                    fileInputStream.read(data);
                    return data;
                }else{
                    CoeusOptionPane.showErrorDialog("Please select a xml or xsl file"); // For bug Fix #1299
                }
            }
        }
        return data;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpload;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnFind;
    private javax.swing.JTextField txtCommittee;
    private javax.swing.JLabel lblCommittee;
    private javax.swing.JLabel lblCommVal;
    private javax.swing.JLabel lblTemplate;
    private javax.swing.JLabel lblCommName;
    private javax.swing.JTextField txtTemplate;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}

