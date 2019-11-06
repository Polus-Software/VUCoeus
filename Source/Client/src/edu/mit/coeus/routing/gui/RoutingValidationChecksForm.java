/*
 * @(#)RoutingValidationChecksForm.java 1.0 11/21/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.routing.gui;

import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleConditionsBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;
import java.awt.event.*;

/**
 * This class is used to show the rules which failed during the validation checks
 * The user message of the business rules will be displayed
 *
 * @author  leenababu
 */
public class RoutingValidationChecksForm extends javax.swing.JComponent implements ActionListener {
    
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusDlgWindow dlgValidationChks;
    
    private CoeusMessageResources coeusMessageResources;
    
    private int WIDTH = 600;
    
    private int HEIGHT = 550;

    int moduleCode = 0;
    
    String moduleItemKey = null;
    
    private ImageIcon iIcnError = new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON));
    
    private ImageIcon iIcnWarning = new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.HISTORY_MODIFIED_ICON));
    
    private static final String ERROR = "E";
    private static final String WARNING = "W";
    private boolean error;
    //2158
    private final String PROPOSAL_MODULE = "30";
    private final String PROP_BUDGET_SUB_MODULE = "31";
    //Added for the case#-COEUSQA-1403 Implement validation based on rules in protocols-start
    private final String PROTOCOL_MODULE = "70";
    private static final String IRB_PROTOCOL = "The IRB Protocol ";
    //Added for the case#-COEUSQA-1403 Implement validation based on rules in protocols-end
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    private final String IACUC_PROTOCOL_MODULE = "90";
    private static final String IACUC_PROTOCOL = "The IACUC Protocol ";
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    //2158 End
    /** Creates a new instance of RoutingValidationChecksForm */
    public RoutingValidationChecksForm(CoeusAppletMDIForm mdiForm,
            Vector vecBusinessRules, int moduleCode, String moduleItemKey) {
        this.mdiForm = mdiForm;
        this.moduleCode = moduleCode;
        this.moduleItemKey = moduleItemKey;
        initComponents();
        postInitComponents();
        setFormData(vecBusinessRules,moduleItemKey);
    }
    
    //New Constructor added with COEUSDEV -198 : Validation Checks for Proposal Hierarchy Syncing
    /* This constructor can be called if the validation checks are performed on
     * a set of moduleItemKeys.
     * The validation check results should be in a HashMap where each moduleItemKey constitute
     * the key of the map and validation results in the form of a vector against the key.
     */
    public RoutingValidationChecksForm(CoeusAppletMDIForm mdiForm,
            HashMap hmBusinessRules, int moduleCode, String moduleItemKey) {
        this.mdiForm = mdiForm;
        this.moduleCode = moduleCode;
        this.moduleItemKey = moduleItemKey;
        initComponents();
        postInitComponents();
        setFormData(hmBusinessRules,moduleItemKey);
    }
    
    private void setFormData( HashMap hmBusinessRules, String moduleItemKey ){
        if(hmBusinessRules!=null && !hmBusinessRules.isEmpty()){
            Iterator it = hmBusinessRules.keySet().iterator();
            Vector vecRules = null;
            while(it.hasNext()){
                this.moduleItemKey = (String)it.next();
                vecRules = (Vector)hmBusinessRules.get(this.moduleItemKey);
                setFormData(vecRules,moduleItemKey);
            }
        }
    }
    //COEUSDEV 198 End
    /**
     * Sets the form component properties
     */
    public void postInitComponents(){
        btnOk.addActionListener(this);
        btnPrint.addActionListener(this);
        btnPrint.setVisible(false);
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.Component[] components = {btnOk,btnPrint};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }
    
    /**
     * Initializes the dialog and shows the dialog window
     */
    public void display(){
        dlgValidationChks =new CoeusDlgWindow(mdiForm,"Validation Rules",true);
        dlgValidationChks.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                btnOk.requestFocusInWindow();
                btnOk.setFocusable(true);
                btnOk.requestFocus();
            }
        });
        dlgValidationChks.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                try{
                    dlgValidationChks.dispose();
                }catch(Exception exc){
                    exc.printStackTrace();
                }
            }
        });
        try{
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dlgValidationChks.setSize(WIDTH, HEIGHT);
            Dimension dlgSize = dlgValidationChks.getSize();
            dlgValidationChks.setLocation(screenSize.width/2 - (dlgSize.width/2),
                    screenSize.height/2 - (dlgSize.height/2));
            dlgValidationChks.getContentPane().add( this );
            dlgValidationChks.setResizable(false);
            dlgValidationChks.setVisible(true);
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    /**
     * Populates the form with the error/warning business rules
     * 
     * @param vecBusinessRules Vector containing the failed business rules
     * @param moduleItemKey 
     */
    private void setFormData( Vector vecBusinessRules, String moduleItemKey ){
        
       
        if(vecBusinessRules!=null){
            //Populate the error rules
            int currentSubmoduleCode = -1;
             int submoduleCode;
            BusinessRuleBean businessRuleBean;
            
            for(int i=0; i<vecBusinessRules.size();i++){
                businessRuleBean = (BusinessRuleBean)vecBusinessRules.get(i);
                submoduleCode = Integer.parseInt(businessRuleBean.getSubmoduleCode());
                if(businessRuleBean.getRuleCategory().equals(ERROR)){
                    if( submoduleCode != currentSubmoduleCode){
                        addErrorHeader(businessRuleBean.getModuleCode()+businessRuleBean.getSubmoduleCode());
                        currentSubmoduleCode = submoduleCode;
                    }
                    addComponent(businessRuleBean);
                }
            }
            
            //Populate the warning rules
            currentSubmoduleCode = -1;
            for(int i=0; i<vecBusinessRules.size();i++){
                businessRuleBean = (BusinessRuleBean)vecBusinessRules.get(i);
                submoduleCode = Integer.parseInt(businessRuleBean.getSubmoduleCode());
                if(businessRuleBean.getRuleCategory().equals(WARNING)){
                   if( submoduleCode != currentSubmoduleCode){
                        addWarningHeader(businessRuleBean.getModuleCode()+businessRuleBean.getSubmoduleCode());
                        currentSubmoduleCode = submoduleCode;
                    }
                    addComponent(businessRuleBean);
                }
            }
        }
    }
    
    /**
     * Creates the header for Warning business rules and adds to the main panel
     */
    private void addWarningHeader(String warningModule){
        JPanel pnlErrorHeader = new JPanel();
        pnlErrorHeader.setLayout(new java.awt.BorderLayout());
        pnlErrorHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnlErrorHeader.setMaximumSize(new java.awt.Dimension(480, 20));
        pnlErrorHeader.setMinimumSize(new java.awt.Dimension(480, 20));
        pnlErrorHeader.setPreferredSize(new java.awt.Dimension(480, 20));
        
        JLabel lblErrorHeader = new JLabel();
        lblErrorHeader.setSize(new Dimension(480,20));
        lblErrorHeader.setFont(CoeusFontFactory.getLabelFont());
        lblErrorHeader.setForeground(Color.BLUE);
        
        String labelText = "";
         if(warningModule.equals(PROPOSAL_MODULE)){
            labelText = " The Proposal ";
        }else if(warningModule.equals(PROP_BUDGET_SUB_MODULE)){
            labelText = "The budget for Proposal ";
        }
        //Added for the case#-COEUSQA-1403 Implement validation based on rules in protocols-start
        else if(warningModule.equals(PROTOCOL_MODULE)){
            labelText = IRB_PROTOCOL;
        }
        //Added for the case#-COEUSQA-1403 Implement validation based on rules in protocols-end
        // COEUSQA-1724_ Implement validation based on rules in protocols_Start
        else if(warningModule.equals(IACUC_PROTOCOL_MODULE)){
            labelText = IACUC_PROTOCOL;
        }
        // COEUSQA-1724_ Implement validation based on rules in protocols_End
        labelText = labelText + moduleItemKey;
        labelText = labelText + " validated with the following Warnings";
        lblErrorHeader.setText(labelText);
        
        pnlErrorHeader.add(lblErrorHeader);
        pnlErrorHeader.setAlignmentX((float) 0.0);
        pnlErrorHeader.setAlignmentY((float) 0.0);
        pnlMainValidationCheck.add(pnlErrorHeader);
    }
    
    /**
     * Creates the header for Error business rules and adds to the main panel
     */
    private void addErrorHeader(String errorModule){
        JPanel pnlErrorHeader = new JPanel();
        pnlErrorHeader.setLayout(new java.awt.BorderLayout());
        pnlErrorHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnlErrorHeader.setMaximumSize(new java.awt.Dimension(480, 20));
        pnlErrorHeader.setMinimumSize(new java.awt.Dimension(480, 20));
        pnlErrorHeader.setPreferredSize(new java.awt.Dimension(480, 20));
        error = true;
        JLabel lblErrorHeader = new JLabel();
//        lblErrorHeader.setSize(new Dimension(500,20));
        lblErrorHeader.setFont(CoeusFontFactory.getLabelFont());
//        lblErrorHeader.setText(" Fix the following errors before submission");
        String labelText = "";
         if(errorModule.equals(PROPOSAL_MODULE)){
            labelText = " The Proposal ";
        }else if(errorModule.equals(PROP_BUDGET_SUB_MODULE)){
            labelText = "The budget for Proposal ";
        }
        //Added for the case#-COEUSQA-1403 Implement validation based on rules in protocols-start
        else if(errorModule.equals(PROTOCOL_MODULE)){
            labelText = IRB_PROTOCOL;
        }
        //Added for the case#-COEUSQA-1403 Implement validation based on rules in protocols-end
        // COEUSQA-1724_ Implement validation based on rules in protocols_Start
        else if(errorModule.equals(IACUC_PROTOCOL_MODULE)){
            labelText = IACUC_PROTOCOL;
        }
        // COEUSQA-1724_ Implement validation based on rules in protocols_End
        labelText = labelText + moduleItemKey;
        labelText = labelText + " validated with following Errors. " +
                "Please fix them";
        lblErrorHeader.setText(labelText);
        lblErrorHeader.setForeground(Color.RED);
        
        pnlErrorHeader.add(lblErrorHeader);
        pnlErrorHeader.setAlignmentX((float) 0.0);
        pnlErrorHeader.setAlignmentY((float) 0.0);
        pnlMainValidationCheck.add(pnlErrorHeader);
    }
    
    /**
     * Creates and sets data for the components, for displaying a business rule
     * 
     * @param businessRuleBean instance of businessRuleBean having 
     *        the error/warning details
     */
    private void addComponent(BusinessRuleBean businessRuleBean){
        
        final int SIZE_X=140;//, SIZE_Y= 16;
        javax.swing.JScrollPane scrPnUserMessage;
        javax.swing.JTextArea txtArUserMessage;
        scrPnUserMessage = new javax.swing.JScrollPane();
        txtArUserMessage = new javax.swing.JTextArea();
        JPanel pnlValidationComponent;
        java.awt.GridBagConstraints gridBagConstraints;
        pnlValidationComponent = new JPanel();
        
//        JSeparator jSeparator1 = new JSeparator();
        JLabel lblUnit =new JLabel();
        JLabel lblUnitValue =new JLabel();
        JLabel lblIcon = new JLabel();
        
        String userMessage = "";
        if(businessRuleBean.getBusinessRuleConditions()!=null &&
                businessRuleBean.getBusinessRuleConditions().size()>0 &&
                businessRuleBean.getBusinessRuleConditions().get(0)!=null){
            userMessage = ((BusinessRuleConditionsBean)businessRuleBean.
                    getBusinessRuleConditions().get(0)).getUserMessage();
        }
        String unitName = businessRuleBean.getUnitName();
        if((businessRuleBean.getRuleCategory()!=null && 
                businessRuleBean.getRuleCategory().equalsIgnoreCase(WARNING))){
            lblIcon.setIcon(iIcnWarning);
        }else{
            lblIcon.setIcon(iIcnError);
        }
        unitName= (unitName == null?"":unitName);
        
        pnlValidationComponent.setLayout(new java.awt.GridBagLayout());
        pnlValidationComponent.setBackground(new java.awt.Color(255, 255, 255));
        pnlValidationComponent.setMaximumSize(new java.awt.Dimension(480, 80));
        pnlValidationComponent.setMinimumSize(new java.awt.Dimension(480, 80));
        pnlValidationComponent.setPreferredSize(new java.awt.Dimension(480, 80));
        
        //Add the Department label
        lblUnit.setFont(CoeusFontFactory.getLabelFont());
        lblUnit.setText("Department :");
        lblUnit.setMaximumSize(new java.awt.Dimension(75, 16));
        lblUnit.setMinimumSize(new java.awt.Dimension(75, 16));
        lblUnit.setPreferredSize(new java.awt.Dimension(75, 16));
        lblUnit.setHorizontalAlignment(JLabel.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlValidationComponent.add(lblUnit, gridBagConstraints);
        
        //Add Deparment name value
        lblUnitValue.setText(unitName);
        lblUnitValue.setFont(CoeusFontFactory.getLabelFont());
        lblUnitValue.setMaximumSize(new java.awt.Dimension(250, 16));
        lblUnitValue.setMinimumSize(new java.awt.Dimension(250, 16));
        lblUnitValue.setPreferredSize(new java.awt.Dimension(250, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlValidationComponent.add(lblUnitValue, gridBagConstraints);
        
        //Add the error/warning icon
        lblIcon.setMaximumSize(new java.awt.Dimension(30, 16));
        lblIcon.setMinimumSize(new java.awt.Dimension(30, 16));
        lblIcon.setPreferredSize(new java.awt.Dimension(30, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        pnlValidationComponent.add(lblIcon, gridBagConstraints);
        
        //Add the user message
        //Modified for COEUSQA-2276 : Validation error message window does not display the complete error message - Start
//        scrPnUserMessage.setMaximumSize(new java.awt.Dimension(SIZE_X, 30));
//        scrPnUserMessage.setMinimumSize(new java.awt.Dimension(SIZE_X, 30));
//        scrPnUserMessage.setPreferredSize(new java.awt.Dimension(SIZE_X, 30));
        scrPnUserMessage.setMaximumSize(new java.awt.Dimension(SIZE_X, 45));
        scrPnUserMessage.setMinimumSize(new java.awt.Dimension(SIZE_X, 45));
        scrPnUserMessage.setPreferredSize(new java.awt.Dimension(SIZE_X, 45));
        //COEUSQA-2276 : End
        scrPnUserMessage.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        txtArUserMessage.setBackground(java.awt.Color.WHITE);
        //Modified for COEUSQA-2276 : Validation error message window does not display the complete error message - Start
//        txtArUserMessage.setDocument(new LimitedPlainDocument(150));
        txtArUserMessage.setDocument(new LimitedPlainDocument(4000));
        //COEUSQA-2276 : End
        txtArUserMessage.setEditable(false);
        txtArUserMessage.setFont(CoeusFontFactory.getNormalFont());
        txtArUserMessage.setLineWrap(true);
        txtArUserMessage.setWrapStyleWord(true);
        txtArUserMessage.setBorder(null);
        txtArUserMessage.setText(userMessage);
        txtArUserMessage.setCaretPosition(0);
        
        scrPnUserMessage.setViewportView(txtArUserMessage);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlValidationComponent.add(scrPnUserMessage, gridBagConstraints);
        
//        jSeparator1.setForeground(new java.awt.Color(255, 0, 51));
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 2;
//        gridBagConstraints.gridwidth = 3;
//        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
//        pnlValidationComponent.add(jSeparator1, gridBagConstraints);
        
        pnlValidationComponent.setAlignmentX((float) 0.0);
        pnlValidationComponent.setAlignmentY((float) 0.0);
        pnlMainValidationCheck.add(pnlValidationComponent);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        pnlMainValidationCheck = new javax.swing.JPanel();
        btnPrint = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(500, 400));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 400));
        pnlMainValidationCheck.setLayout(new javax.swing.BoxLayout(pnlMainValidationCheck, javax.swing.BoxLayout.Y_AXIS));

        pnlMainValidationCheck.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(pnlMainValidationCheck);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jScrollPane1, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setMnemonic('P');
        btnPrint.setText("Print");
        btnPrint.setMinimumSize(new java.awt.Dimension(75, 26));
        btnPrint.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 8, 4);
        add(btnPrint, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(85, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(85, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(85, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 7, 4);
        add(btnOk, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if(src.equals(btnOk) ){
            dlgValidationChks.dispose();
        }else if (src.equals(btnPrint)){
            //need to be implemented
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "funcNotImpl_exceptionCode.1100"));
        }
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnPrint;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JPanel pnlMainValidationCheck;
    // End of variables declaration//GEN-END:variables
    
}
