/*
 * @(#)AddRiskLevelForm.java 1.0 04/18/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.ProtocolRiskLevelBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author  leenababu
 */
public class AddRiskLevelForm extends CoeusDlgWindow implements ActionListener, ItemListener {
    private char functionType;
    private ProtocolRiskLevelBean protocolRiskLevelBean;
    private Vector vecRiskLevels;
    private boolean saveRequired = false;
    
    private CoeusVector cvExistingRiskLevels;
    private static char ADD_MODIFY_MODE = 'B';
    private static String ACTIVE_STATUS = "A";
    private static String INACTIVE_STATUS = "I";
    private static String EMPTY_STRING = "";
    
    private DateUtils dateUtils = new DateUtils();
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static String DATE_FORMAT = "dd-MMM-yyyy";
    private static String DATE_SEPERATORS = "/-:,";
    
    private String ENTER_DATE_UPDATED = "risklevel_exceptionCode.1004";
    private String ENTER_DATE_ASSIGNED = "risklevel_exceptionCode.1003";
    private String RISK_LEVEL_EXISTS = "risklevel_exceptionCode.1002";
    private String SELECT_RISK_LEVEL = "risklevel_exceptionCode.1005";
    
    /** Creates new form AddRiskLevelForm */
    public AddRiskLevelForm(char functionType, ProtocolRiskLevelBean protocolRiskLevelBean, Vector vecRiskLevels) {
        super(CoeusGuiConstants.getMDIForm(), "Add Risk Level", true);
        this.functionType = functionType;
        this.protocolRiskLevelBean = protocolRiskLevelBean;
        this.vecRiskLevels = vecRiskLevels;
        setFunctionType();
        initComponents();
        postInitComponents();
        setFormData();
    }
    
    /**
     * If the risk level is added in this protocol(not a risk level from the parent),
     * while taken for modify the functionType is set to ADD_MODIFY_MODE('B').
     * This helps in differentiating whether the risk level taken to modify is
     * from the parent protocol or current protocol.
     *
     */
    public void setFunctionType(){
        if(functionType == TypeConstants.MODIFY_MODE 
                && protocolRiskLevelBean !=null 
                && TypeConstants.INSERT_RECORD.equals(protocolRiskLevelBean.getAcType())){
            functionType = ADD_MODIFY_MODE;
        }
    }
    
    /**
     * Set the data to the form components
     */
    public void setFormData(){
        CoeusVector cvStatuses = new CoeusVector();
        ComboBoxBean activeComboBean = new ComboBoxBean(ACTIVE_STATUS, "Active");
        ComboBoxBean inactiveComboBean = new ComboBoxBean(INACTIVE_STATUS, "Inactive");
        cvStatuses.add(activeComboBean);
        cvStatuses.add(inactiveComboBean);
        cmbStatus.setModel(new DefaultComboBoxModel(cvStatuses));
        cmbStatus.setSelectedIndex(0);
        
        if(vecRiskLevels!=null){
            cmbRiskLevel.setModel(new DefaultComboBoxModel(vecRiskLevels));
        }
        
        String todayDate = dateUtils.formatDate((new java.sql.Timestamp(
                (new java.util.Date()).getTime())).toString(),
                DATE_FORMAT);
        
        if((functionType == TypeConstants.MODIFY_MODE ||
                functionType == ADD_MODIFY_MODE) && protocolRiskLevelBean!=null){
            
            txtDateAssigned.setText(dateUtils.formatDate(
                    protocolRiskLevelBean.getDateAssigned().toString(),DATE_FORMAT));
            if(!protocolRiskLevelBean.getStatus().equals(ACTIVE_STATUS) &&
                    protocolRiskLevelBean.getDateUpdated()!=null){
                txtDateUpdated.setText(dateUtils.formatDate(
                        protocolRiskLevelBean.getDateUpdated().toString(),DATE_FORMAT));
            }
            txtArComments.setText(protocolRiskLevelBean.getComments());
            
            //Set the risk level
            for(int i = 0; i< vecRiskLevels.size(); i++){
                ComboBoxBean comboBoxBean = (ComboBoxBean)vecRiskLevels.get(i);
                if(protocolRiskLevelBean.getRiskLevelCode().equals(comboBoxBean.getCode())){
                    cmbRiskLevel.setSelectedIndex(i);
                }
            }
            
            //Set the status
            if(protocolRiskLevelBean.getStatus().equals(ACTIVE_STATUS)){
                cmbStatus.setSelectedItem(activeComboBean);
            }else{
                cmbStatus.setSelectedItem(inactiveComboBean);
            }
            
            
        }else if(functionType == TypeConstants.ADD_MODE){
            txtDateAssigned.setText(todayDate);
            if(vecRiskLevels!=null){
                cmbRiskLevel.setModel(new DefaultComboBoxModel(vecRiskLevels));
            }
        }
    }
    
    /**
     * Set the properties, listeners to the gui components
     */
    public void postInitComponents(){
        dateUtils = new DateUtils();
        
        Component[] components = {cmbRiskLevel, cmbStatus, txtDateAssigned,
        txtDateUpdated, txtArComments, btnOk, btnCancel};
        ScreenFocusTraversalPolicy screenFocusTraversalPolicy =
                new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(screenFocusTraversalPolicy);
        setFocusCycleRoot(true);
        
        txtArComments.setDisabledTextColor(Color.black);
        if(functionType == TypeConstants.MODIFY_MODE ){
            setTitle("Modify Risk Level");
            if(protocolRiskLevelBean != null && protocolRiskLevelBean.getStatus().equals(ACTIVE_STATUS)){
                txtDateUpdated.setEnabled(false);
            }
            cmbRiskLevel.setEnabled(false);
            txtDateAssigned.setEnabled(false);
            txtArComments.setEnabled(false);
        }else {
            setTitle("Add Risk Level");
            txtDateUpdated.setEnabled(false);
            cmbStatus.setEnabled(false);
        }
        
        if(functionType == ADD_MODIFY_MODE){
            setTitle("Modify Risk Level");
        }
        txtDateAssigned.addFocusListener(new CustomFocusAdapter());
        txtDateUpdated.addFocusListener(new CustomFocusAdapter());
        
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        cmbStatus.addItemListener(this);
        
        setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
            }
        });
        
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCloseAction();
            }
        });
    }
    
    /**
     * Displays the window
     */
    public void display(){
        setLocation(CoeusDlgWindow.CENTER);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * Check whether the user has modified any data in the window
     *
     * @return boolean true if modified data else false
     */
    public boolean checkSaveRequired(){
        if(functionType == TypeConstants.MODIFY_MODE){
            
            //Check whether the status is changed
            ComboBoxBean selStatusComboBean = (ComboBoxBean)cmbStatus.getSelectedItem();
            if(selStatusComboBean != null){
                if(!protocolRiskLevelBean.getStatus().equals(selStatusComboBean.getCode())){
                    saveRequired = true;
                }
            }
        }else if(functionType == TypeConstants.ADD_MODE){
            saveRequired = true;
        }else if(functionType == ADD_MODIFY_MODE){
            if(cmbRiskLevel.getSelectedItem() != null){
                ComboBoxBean comboBoxBean = (ComboBoxBean)cmbRiskLevel.getSelectedItem();
                if(!comboBoxBean.getCode().equals(protocolRiskLevelBean.getRiskLevelCode())){
                    saveRequired = true;
                }
            }
            try{
                if(!saveRequired){
                    Date newDate = dtFormat.parse(
                            dateUtils.restoreDate(txtDateAssigned.getText(), DATE_SEPERATORS));
                    if(!newDate.equals(protocolRiskLevelBean.getDateAssigned())){
                        saveRequired = true;
                    }
                }
            }catch(ParseException e){
                saveRequired = true;
            }
            
            if(!saveRequired && !txtArComments.getText().trim().equals(protocolRiskLevelBean.getComments())){
                saveRequired = true;
            }
            
        }
        return saveRequired;
    }
    
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source.equals(btnOk)){
            performOkAction();
        }else if(source.equals(btnCancel)){
            performCloseAction();
        }
    }
    
    public void itemStateChanged(ItemEvent itemEvent){
        if(itemEvent.getSource().equals(cmbStatus)){
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                //When status is active, clear the txtDateUpdated field and disable it 
                if(((ComboBoxBean)cmbStatus.getSelectedItem()).getCode().equals("A")){
                    txtDateUpdated.setText("");
                    txtDateUpdated.setEnabled(false);
                }else{
                    //If the status is inactive, if its first time set the dateUpdated to todays date
                    //else take it from the protocolRiskLevelBean bean
                    if(protocolRiskLevelBean != null 
                            && TypeConstants.UPDATE_RECORD.equals(protocolRiskLevelBean.getAcType())){
                        if(protocolRiskLevelBean.getDateUpdated()!=null){
                            txtDateUpdated.setText(dateUtils.formatDate(
                                    protocolRiskLevelBean.getDateUpdated().toString(),DATE_FORMAT));
                            txtDateUpdated.setEnabled(true);
                        }
                    }else{
                        String todayDate = dateUtils.formatDate((new java.sql.Timestamp(
                                (new java.util.Date()).getTime())).toString(),
                                DATE_FORMAT);
                        txtDateUpdated.setText(todayDate);
                        txtDateUpdated.setEnabled(true);
                    }
                }
                
            }
        }
    }
    
    /**
     * Performs the operation while closing the window
     */
    public void performCloseAction(){
        if(checkSaveRequired()){
            int   option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_YES);
            
            if(option == JOptionPane.YES_OPTION){
                performOkAction();
            }else if(option == JOptionPane.NO_OPTION){
                saveRequired = false;
                this.dispose();
            }else{
               
            }
        }else{
            this.dispose();
        }
    }
    
    /**
     * Performs the operation while clicking OK button
     */
    public void performOkAction(){
        if(functionType == TypeConstants.ADD_MODE || functionType == ADD_MODIFY_MODE){
            if(validateData()){
                if(functionType == ADD_MODIFY_MODE){
                    checkSaveRequired();
                }else if(functionType == TypeConstants.ADD_MODE){
                    saveRequired = true;
                }
                if(functionType == TypeConstants.ADD_MODE){
                    protocolRiskLevelBean = new ProtocolRiskLevelBean();
                }
                if(saveRequired){
                    protocolRiskLevelBean.setAcType(TypeConstants.INSERT_RECORD);
                    protocolRiskLevelBean.setRiskLevelCode(((ComboBoxBean)cmbRiskLevel.getSelectedItem()).getCode());
                    protocolRiskLevelBean.setStatus(ACTIVE_STATUS);
                    protocolRiskLevelBean.setComments(txtArComments.getText().trim());
                    try {
                        protocolRiskLevelBean.setDateAssigned(new java.sql.Date(dtFormat.parse(
                                dateUtils.restoreDate(txtDateAssigned.getText(), DATE_SEPERATORS)).getTime()));
                        
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                this.dispose();
            }
        }else if(functionType == TypeConstants.MODIFY_MODE){
            if(validateData()){
                if(checkSaveRequired()){
                    protocolRiskLevelBean.setAcType(TypeConstants.UPDATE_RECORD);
                    protocolRiskLevelBean.setStatus(((ComboBoxBean)cmbStatus.getSelectedItem()).getCode());
                    if(protocolRiskLevelBean.getStatus().equals(ACTIVE_STATUS)){
                        protocolRiskLevelBean.setDateUpdated(null);
                        protocolRiskLevelBean.setAcType(null);
                    }else{
                        try {
                            protocolRiskLevelBean.setDateUpdated(new java.sql.Date(dtFormat.parse(
                                    dateUtils.restoreDate(txtDateUpdated.getText(),DATE_SEPERATORS)).getTime()));
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                this.dispose();
            }
        }
        
    }
    
    /**
     * Validates the form data
     *
     * @return boolean true if data is valid else false
     */
    public boolean validateData(){
        boolean validData = true;
        if(cmbRiskLevel.getSelectedItem() == null ||
                ((ComboBoxBean)cmbRiskLevel.getSelectedItem()).getCode()== null ||
                ((ComboBoxBean)cmbRiskLevel.getSelectedItem()).getCode().equals("")){
            validData = false;
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(SELECT_RISK_LEVEL));
            cmbRiskLevel.requestFocusInWindow();
        }
        
        if(validData && isRiskLevelAlreadyAdded()){
            validData = false;
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(RISK_LEVEL_EXISTS));
            cmbRiskLevel.requestFocusInWindow();
        }
        
        if(validData && (functionType == TypeConstants.ADD_MODE || functionType == ADD_MODIFY_MODE)){
            if(txtDateAssigned.getText().trim().length() == 0){
                validData = false;
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ENTER_DATE_ASSIGNED));
                txtDateAssigned.requestFocusInWindow();
            }
        }
        
        if(validData && functionType == TypeConstants.MODIFY_MODE){
            if(((ComboBoxBean)cmbStatus.getSelectedItem()).getCode().equals(INACTIVE_STATUS) &&
                    txtDateUpdated.getText().trim().length() == 0){
                validData = false;
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ENTER_DATE_UPDATED));
                txtDateUpdated.requestFocusInWindow();
            }
        }
        return validData;
    }
    
    /**
     * Checks whether the selected risk level is already added 
     *
     * @return boolean true if already added else false
     */
    public boolean isRiskLevelAlreadyAdded(){
        boolean riskLevelAlreadyAdded = false;
        if(cvExistingRiskLevels!=null){
            String selectedRiskLevelCode = ((ComboBoxBean)cmbRiskLevel.getSelectedItem()).getCode();
            String selectedStatus = ((ComboBoxBean)cmbStatus.getSelectedItem()).getCode();
            ProtocolRiskLevelBean existingRiskLevelBean = null;
            for(int i=0; i< cvExistingRiskLevels.size(); i++){
                existingRiskLevelBean = (ProtocolRiskLevelBean)cvExistingRiskLevels.get(i);
                if(selectedRiskLevelCode.equals(existingRiskLevelBean.getRiskLevelCode())){
                    if(functionType == ADD_MODIFY_MODE || functionType == TypeConstants.ADD_MODE){
                        if(existingRiskLevelBean != protocolRiskLevelBean &&
                                existingRiskLevelBean.getStatus().equals(ACTIVE_STATUS)){
                            riskLevelAlreadyAdded = true;
                        }
                    }else if(functionType == TypeConstants.MODIFY_MODE){
                        if(existingRiskLevelBean.getStatus().equals(ACTIVE_STATUS) && selectedStatus.equals(ACTIVE_STATUS)){
                            riskLevelAlreadyAdded = true;
                        }
                    }
                }
            }
        }
        return riskLevelAlreadyAdded;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnOk = new edu.mit.coeus.utils.CoeusButton();
        btnCancel = new edu.mit.coeus.utils.CoeusButton();
        jPanel1 = new javax.swing.JPanel();
        txtDateUpdated = new edu.mit.coeus.utils.CoeusTextField();
        lblDateAssigned = new edu.mit.coeus.utils.CoeusLabel();
        lblDateUpdated = new edu.mit.coeus.utils.CoeusLabel();
        lblComments = new edu.mit.coeus.utils.CoeusLabel();
        txtDateAssigned = new edu.mit.coeus.utils.CoeusTextField();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        lblRiskLevel = new edu.mit.coeus.utils.CoeusLabel();
        cmbStatus = new edu.mit.coeus.utils.CoeusComboBox();
        cmbRiskLevel = new edu.mit.coeus.utils.CoeusComboBox();
        lblStatus = new edu.mit.coeus.utils.CoeusLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 3, 0, 3);
        getContentPane().add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        getContentPane().add(btnCancel, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(445, 170));
        jPanel1.setMinimumSize(new java.awt.Dimension(445, 170));
        jPanel1.setPreferredSize(new java.awt.Dimension(445, 170));
        txtDateUpdated.setDocument(new LimitedPlainDocument(12));
        txtDateUpdated.setMaximumSize(new java.awt.Dimension(120, 22));
        txtDateUpdated.setMinimumSize(new java.awt.Dimension(120, 22));
        txtDateUpdated.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        jPanel1.add(txtDateUpdated, gridBagConstraints);

        lblDateAssigned.setText("Date Assigned: ");
        lblDateAssigned.setMaximumSize(new java.awt.Dimension(90, 22));
        lblDateAssigned.setMinimumSize(new java.awt.Dimension(90, 22));
        lblDateAssigned.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        jPanel1.add(lblDateAssigned, gridBagConstraints);

        lblDateUpdated.setText("Date Updated: ");
        lblDateUpdated.setMaximumSize(new java.awt.Dimension(87, 22));
        lblDateUpdated.setMinimumSize(new java.awt.Dimension(87, 22));
        lblDateUpdated.setPreferredSize(new java.awt.Dimension(87, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 0);
        jPanel1.add(lblDateUpdated, gridBagConstraints);

        lblComments.setText("Comments: ");
        lblComments.setMaximumSize(new java.awt.Dimension(65, 22));
        lblComments.setMinimumSize(new java.awt.Dimension(65, 22));
        lblComments.setPreferredSize(new java.awt.Dimension(65, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        jPanel1.add(lblComments, gridBagConstraints);

        txtDateAssigned.setDocument(new LimitedPlainDocument(12));
        txtDateAssigned.setMaximumSize(new java.awt.Dimension(120, 22));
        txtDateAssigned.setMinimumSize(new java.awt.Dimension(120, 22));
        txtDateAssigned.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel1.add(txtDateAssigned, gridBagConstraints);

        txtArComments.setColumns(20);
        txtArComments.setDocument(new LimitedPlainDocument(2000));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setRows(5);
        txtArComments.setWrapStyleWord(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        jPanel1.add(scrPnComments, gridBagConstraints);

        lblRiskLevel.setText("Risk Level: ");
        lblRiskLevel.setMaximumSize(new java.awt.Dimension(68, 22));
        lblRiskLevel.setMinimumSize(new java.awt.Dimension(68, 22));
        lblRiskLevel.setPreferredSize(new java.awt.Dimension(68, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        jPanel1.add(lblRiskLevel, gridBagConstraints);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbStatus.setMaximumSize(new java.awt.Dimension(120, 22));
        cmbStatus.setMinimumSize(new java.awt.Dimension(120, 22));
        cmbStatus.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        jPanel1.add(cmbStatus, gridBagConstraints);

        cmbRiskLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRiskLevel.setMaximumSize(new java.awt.Dimension(120, 22));
        cmbRiskLevel.setMinimumSize(new java.awt.Dimension(120, 22));
        cmbRiskLevel.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel1.add(cmbRiskLevel, gridBagConstraints);

        lblStatus.setText("Status: ");
        lblStatus.setMaximumSize(new java.awt.Dimension(44, 22));
        lblStatus.setMinimumSize(new java.awt.Dimension(44, 22));
        lblStatus.setPreferredSize(new java.awt.Dimension(44, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel1.add(lblStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    public Vector getVecRiskLevels() {
        return vecRiskLevels;
    }
    
    public void setVecRiskLevels(Vector vecRiskLevels) {
        this.vecRiskLevels = vecRiskLevels;
    }
    public class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;
        String strDate = EMPTY_STRING;
        String oldData = EMPTY_STRING;
        boolean temporary = false;
        
        public void focusGained(FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                    &&  (!dateField.getText().trim().equals(EMPTY_STRING))) {
                    oldData = dateField.getText();
                    String focusDate = dateUtils.restoreDate(
                            dateField.getText(),DATE_SEPERATORS);
                    dateField.setText(focusDate);
                }
            }
        }
        
        public void focusLost(FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                temporary = fe.isTemporary();
                if ( (dateField.getText() != null)
                    &&  (!dateField.getText().trim().equals(EMPTY_STRING))
                    && (!temporary) ) {
                    strDate = dateField.getText();
                    String convertedDate =
                            dateUtils.formatDate(dateField.getText(), DATE_SEPERATORS ,
                            DATE_FORMAT);
                    if (convertedDate==null){
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                "memMntFrm_exceptionCode.1048"));
                        dateField.setText(oldData);
                        temporary = true;
                        dateField.requestFocus();
                    }else {
                        dateField.setText(convertedDate);
                        temporary = false;
                        
                    }
                }
            }
        }
    }
    
    public ProtocolRiskLevelBean getProtocolRiskLevelBean() {
        return protocolRiskLevelBean;
    }
    
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    public void setExistingRiskLevels(CoeusVector cvExistingRiskLevels) {
        this.cvExistingRiskLevels = cvExistingRiskLevels;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.utils.CoeusButton btnCancel;
    public edu.mit.coeus.utils.CoeusButton btnOk;
    public edu.mit.coeus.utils.CoeusComboBox cmbRiskLevel;
    public edu.mit.coeus.utils.CoeusComboBox cmbStatus;
    public javax.swing.JPanel jPanel1;
    public edu.mit.coeus.utils.CoeusLabel lblComments;
    public edu.mit.coeus.utils.CoeusLabel lblDateAssigned;
    public edu.mit.coeus.utils.CoeusLabel lblDateUpdated;
    public edu.mit.coeus.utils.CoeusLabel lblRiskLevel;
    public edu.mit.coeus.utils.CoeusLabel lblStatus;
    public javax.swing.JScrollPane scrPnComments;
    public javax.swing.JTextArea txtArComments;
    public edu.mit.coeus.utils.CoeusTextField txtDateAssigned;
    public edu.mit.coeus.utils.CoeusTextField txtDateUpdated;
    // End of variables declaration//GEN-END:variables
    
}
