/*
 * @(#)organization3Form.java 1.0 9/1/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.organization.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * This class constructs the Organization detail's Organization3Form
 *
 * @version :1.0 September 1, 2002, 1:35 PM
 * @author Guptha K
 */
public class Organization3Form extends JPanel implements KeyListener {

    private JPanel mainPanel;
    private JPanel pnlForm;
    private JLabel lblHumanSubAssurance;
    private JLabel lblAnimalWelfareAssurance;
    private JLabel lblScienceMisconductComplDate;
    private JLabel lblPhsAccount;
    private JLabel lblNsfInstitutionalCode;
    private JLabel lblIndirectCostRateAgreement;
    private JLabel lblCognizantAuditor;
    private JLabel lblOnrResidentRep;
    private CoeusTextField txtHumanSubAssurance;
    private CoeusTextField txtAnimalWelfareAssurance;
    private CoeusTextField txtScienceMisconductComplDate;
    private CoeusTextField txtPhsAccount;
    private CoeusTextField txtNsfInstitutionalCode;
    private CoeusTextField txtIndirectCostRateAgreement;
    private CoeusTextField txtCognizantAuditor;
    private CoeusTextField txtOnrResidentRep;
    private JButton btnAuditor;
    private JButton btnResidentRep;

    private CoeusAppletMDIForm mdiForm;
    private OrganizationMaintenanceFormBean formData;
    private char functionType;
    private boolean dataChanged = false;

    private String cognizantAuditor = "";
    private int cognizantAuditorId = 0;
    private String onrResidentRep = "";
    private int onrResidentRepId = 0;
    private DateUtils dateUtils;
    private String focusDate;
    private boolean saveRequired;

    // add functionality
    private final char ADD_FUNCTION = 'I';
    // modify functionality
    private final char MODIFY_FUNCTION = 'U';
    // display functionality
    private final char DISPLAY_FUNCTION = 'D';
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

   /**
     * Constructor which instantiates Organization3Form and populates them with data
     * specified, in Organization Module. And sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param formData, a OrganizationMaintenanceFormBean which consists of all the details
     * of a Organization3Form.
     */
    public Organization3Form(char functionType, OrganizationMaintenanceFormBean formData) {
        this.functionType = functionType;
        this.formData = formData;
        initComponents();
        
        java.awt.Component[] component={txtHumanSubAssurance,txtAnimalWelfareAssurance,txtScienceMisconductComplDate,
        txtPhsAccount,txtNsfInstitutionalCode,txtIndirectCostRateAgreement, txtCognizantAuditor,btnAuditor,
        txtOnrResidentRep,btnResidentRep};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        
        setValues();
    }

    /**
     * initialize the form controls.
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        //Bug Fix:1077 Start 1
        ImageIcon searchIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON));
        //Bug Fix:1077 End 1
        
        dateUtils = new DateUtils();

        pnlForm = new javax.swing.JPanel();
        lblHumanSubAssurance = new javax.swing.JLabel();
        lblAnimalWelfareAssurance = new javax.swing.JLabel();
        lblScienceMisconductComplDate = new javax.swing.JLabel();
        lblPhsAccount = new javax.swing.JLabel();
        lblNsfInstitutionalCode = new javax.swing.JLabel();
        lblIndirectCostRateAgreement = new javax.swing.JLabel();
        lblCognizantAuditor = new javax.swing.JLabel();
        lblOnrResidentRep = new javax.swing.JLabel();
        txtHumanSubAssurance = new CoeusTextField();
        txtHumanSubAssurance.setDocument(new LimitedPlainDocument(30));
        txtHumanSubAssurance.getDocument().addDocumentListener(new CoeusTextListener());

        txtAnimalWelfareAssurance = new CoeusTextField();
        txtAnimalWelfareAssurance.setDocument(new LimitedPlainDocument(20));
        txtAnimalWelfareAssurance.getDocument().addDocumentListener(new CoeusTextListener());

        txtScienceMisconductComplDate = new CoeusTextField();
        txtScienceMisconductComplDate.setDocument( new LimitedPlainDocument(11) );
        txtScienceMisconductComplDate.getDocument().addDocumentListener(new CoeusTextListener());

        txtPhsAccount = new CoeusTextField();
        txtPhsAccount.setDocument(new LimitedPlainDocument(30));
        txtPhsAccount.getDocument().addDocumentListener(new CoeusTextListener());

        txtNsfInstitutionalCode = new CoeusTextField();
        txtNsfInstitutionalCode.setDocument(new LimitedPlainDocument(30));
        txtNsfInstitutionalCode.getDocument().addDocumentListener(new CoeusTextListener());

        txtIndirectCostRateAgreement = new CoeusTextField();
        txtIndirectCostRateAgreement.setDocument(new LimitedPlainDocument(50));
        txtIndirectCostRateAgreement.getDocument().addDocumentListener(new CoeusTextListener());

        txtCognizantAuditor = new CoeusTextField();
        txtCognizantAuditor.setDocument(new LimitedPlainDocument(60));
        txtCognizantAuditor.getDocument().addDocumentListener(new CoeusTextListener());
        txtCognizantAuditor.setCaretPosition(0);

        txtOnrResidentRep = new CoeusTextField();
        txtOnrResidentRep.setDocument(new LimitedPlainDocument(60));
        txtOnrResidentRep.getDocument().addDocumentListener(new CoeusTextListener());
        txtOnrResidentRep.setCaretPosition(0);

        btnAuditor = new javax.swing.JButton(searchIcon);
        btnResidentRep = new javax.swing.JButton(searchIcon);

        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        pnlForm.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        lblHumanSubAssurance.setText("Human Sub Assurance:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblHumanSubAssurance, gridBagConstraints2);

        lblAnimalWelfareAssurance.setText("Animal Welfare Assurance:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblAnimalWelfareAssurance, gridBagConstraints2);

        lblScienceMisconductComplDate.setText("Science Misconduct Comp Date:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblScienceMisconductComplDate, gridBagConstraints2);

        lblPhsAccount.setText("PHS Account:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblPhsAccount, gridBagConstraints2);

        lblNsfInstitutionalCode.setText("NSF Institutional Code:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblNsfInstitutionalCode, gridBagConstraints2);

        lblIndirectCostRateAgreement.setText("Indirect Cost Rate Agreement:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 5;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblIndirectCostRateAgreement, gridBagConstraints2);

        lblCognizantAuditor.setText("Cognizant Auditor:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblCognizantAuditor, gridBagConstraints2);

        lblOnrResidentRep.setText("ONR Resident Rep:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOnrResidentRep, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        txtHumanSubAssurance.setPreferredSize(new Dimension(150,20));
        txtHumanSubAssurance.setMaximumSize(new Dimension(150,20));
        txtHumanSubAssurance.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtHumanSubAssurance, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtAnimalWelfareAssurance.setPreferredSize(new Dimension(150,20));
        txtAnimalWelfareAssurance.setMaximumSize(new Dimension(150,20));
        txtAnimalWelfareAssurance.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtAnimalWelfareAssurance, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        txtScienceMisconductComplDate.setPreferredSize(new Dimension(150,20));
        txtScienceMisconductComplDate.setMaximumSize(new Dimension(150,20));
        txtScienceMisconductComplDate.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtScienceMisconductComplDate, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        txtPhsAccount.setPreferredSize(new Dimension(200,20));
        txtPhsAccount.setMaximumSize(new Dimension(200,20));
        txtPhsAccount.setMinimumSize(new Dimension(200,20));
        pnlForm.add(txtPhsAccount, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        txtNsfInstitutionalCode.setPreferredSize(new Dimension(200,20));
        txtNsfInstitutionalCode.setMaximumSize(new Dimension(200,20));
        txtNsfInstitutionalCode.setMinimumSize(new Dimension(200,20));
        pnlForm.add(txtNsfInstitutionalCode, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 5;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        txtIndirectCostRateAgreement.setPreferredSize(new Dimension(300,20));
        txtIndirectCostRateAgreement.setMaximumSize(new Dimension(300,20));
        txtIndirectCostRateAgreement.setMinimumSize(new Dimension(300,20));
        pnlForm.add(txtIndirectCostRateAgreement, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        txtCognizantAuditor.setPreferredSize(new Dimension(200,20));
        txtCognizantAuditor.setMaximumSize(new Dimension(200,20));
        txtCognizantAuditor.setMinimumSize(new Dimension(200,20));
        pnlForm.add(txtCognizantAuditor, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        txtOnrResidentRep.setPreferredSize(new Dimension(200,20));
        txtOnrResidentRep.setMaximumSize(new Dimension(200,20));
        txtOnrResidentRep.setMinimumSize(new Dimension(200,20));
	pnlForm.add(txtOnrResidentRep, gridBagConstraints2);

        btnAuditor.setName("Auditor");
        
        //Bug Fix:1077 Start 2
        btnAuditor.setPreferredSize(new java.awt.Dimension(23, 23));
        //Bug Fix:1077 End 2
        
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.gridwidth = 1;
        
        //Bug Fix:1077 Start 3        
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //Bug Fix:1077 End 3
        
        pnlForm.add(btnAuditor, gridBagConstraints2);
        btnAuditor.addActionListener(new DisplayRolodex());

        btnResidentRep.setName("ResidentRep");
        
        //Bug Fix:1077 Start 4
        btnResidentRep.setPreferredSize(new java.awt.Dimension(23,23));
        //Bug Fix:1077 End 4
        
        btnResidentRep.setMaximumSize( new java.awt.Dimension(10,10));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.gridwidth = 1;
        
        //Bug Fix:1077 Start 5
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //Bug Fix:1077 End 5
        
        pnlForm.add(btnResidentRep, gridBagConstraints2);
        btnResidentRep.addActionListener(new DisplayRolodex());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        this.setLayout(flowLayout);
        add(pnlForm);
        setLabelFont();

        txtCognizantAuditor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (txtCognizantAuditor.getText().trim().length() > 0) {
                    if (!txtCognizantAuditor.getText().trim().equalsIgnoreCase(cognizantAuditor)) {
                        log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
                        txtCognizantAuditor.setText(cognizantAuditor);
                        txtCognizantAuditor.requestFocus();
                    }
                }
            }
        });

        txtCognizantAuditor.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2 &&
                        !txtCognizantAuditor.getText().trim().equals("") ) {
                    //invoke rolodex display
                    txtCognizantAuditor.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));                
                    RolodexMaintenanceDetailForm rolodex = new RolodexMaintenanceDetailForm('V', "" + cognizantAuditorId);
                    rolodex.showForm(mdiForm, "Rolodex", true);
                    txtCognizantAuditor.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));                
                }
            }
        });

        txtOnrResidentRep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (txtOnrResidentRep.getText().trim().length() > 0) {
                    if (!txtOnrResidentRep.getText().trim().equalsIgnoreCase(onrResidentRep)) {
                        log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
                        txtOnrResidentRep.setText(onrResidentRep);
                        txtOnrResidentRep.requestFocus();
                    }
                }
            }
        });

        txtOnrResidentRep.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2 &&
                        !txtOnrResidentRep.getText().trim().equals("") ) {
                    //invoke rolodex display
                    txtOnrResidentRep.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                    RolodexMaintenanceDetailForm rolodex = new RolodexMaintenanceDetailForm('V', "" + onrResidentRepId);
                    rolodex.showForm(mdiForm, "Rolodex", true);
                    txtOnrResidentRep.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));                
                }
            }
        });


        txtCognizantAuditor.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent fe){
                if (!fe.isTemporary()) {
                    if (txtCognizantAuditor.getText().trim().length()>0){
                        if(!txtCognizantAuditor.getText().trim().equalsIgnoreCase(cognizantAuditor)){
                            log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
                            txtCognizantAuditor.setText(cognizantAuditor);
                            setNextFocusableComponent(txtCognizantAuditor);
                        }
                    }
                }
            }
        });

        txtOnrResidentRep.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent fe){
                if (!fe.isTemporary()) {
                    if (txtOnrResidentRep.getText().trim().length()>0){
                        if(!txtOnrResidentRep.getText().trim().equalsIgnoreCase(onrResidentRep)){
                            log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
                            txtOnrResidentRep.setText(onrResidentRep);
                            setNextFocusableComponent(txtOnrResidentRep);
                        }
                    }
                }
            }
        });

        txtCognizantAuditor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ae) {
                if (ae.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    txtCognizantAuditor.setText(cognizantAuditor);
                    setNextFocusableComponent(txtCognizantAuditor);
                }
            }
        });

        txtOnrResidentRep.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ae) {
                if (ae.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    txtOnrResidentRep.setText(onrResidentRep);
                    setNextFocusableComponent(txtOnrResidentRep);
                }
            }
        });

        txtScienceMisconductComplDate.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if (!fe.isTemporary()) {
                    if ((txtScienceMisconductComplDate.getText() != null) && 
                        (!txtScienceMisconductComplDate.getText().trim().equals("")) && 
                        (functionType != 'D')) {
                        txtScienceMisconductComplDate.setText(focusDate);
                    }
                }
            }

            public void focusLost(FocusEvent fe) {
                String editingValue = null;
                if (!fe.isTemporary()) {
                    if ((txtScienceMisconductComplDate.getText() != null) && 
                        (!txtScienceMisconductComplDate.getText().trim().equals("")) && 
                        (functionType != 'D')) {
                        String convertedDate = dateUtils.formatDate(txtScienceMisconductComplDate.getText(), "/-:,", "dd-MMM-yyyy");
                        if (convertedDate == null) {
                            log("Please enter valid date");
                            txtScienceMisconductComplDate.requestFocus();
                        } else {
                            focusDate = txtScienceMisconductComplDate.getText();
                            txtScienceMisconductComplDate.setText(convertedDate);
                        }
                    }
                }
            }
        });

        txtHumanSubAssurance.addKeyListener(this);
        txtAnimalWelfareAssurance.addKeyListener(this);
        txtScienceMisconductComplDate.addKeyListener(this);
        txtPhsAccount.addKeyListener(this);
        txtNsfInstitutionalCode.addKeyListener(this);
        txtIndirectCostRateAgreement.addKeyListener(this);
        txtCognizantAuditor.addKeyListener(this);
        txtOnrResidentRep.addKeyListener(this);

        setLabelFont();
    }
    /**
     * This method is used to determine whether the data is to be saved or not.
     * @returns boolean true if any modifications have been done else it returns false.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }

    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {                        
                txtHumanSubAssurance.requestFocusInWindow();            
        }
    }    
    //end Amit       
    
    public void keyReleased(KeyEvent ae) {
    }

    public void keyTyped(KeyEvent ae) {
        saveRequired = true;
    }

    public void keyPressed(KeyEvent ae) {
    }

    public boolean validateData(){
        boolean dataOK = true;
        if (txtOnrResidentRep.getText().trim().length()>0){
            if(!txtOnrResidentRep.getText().trim().equalsIgnoreCase(onrResidentRep)){
                log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
                txtOnrResidentRep.setText(onrResidentRep);
                txtOnrResidentRep.requestFocus();
                dataOK=false;
            }
        }
        if (txtCognizantAuditor.getText().trim().length()>0){
            if(!txtCognizantAuditor.getText().trim().equalsIgnoreCase(cognizantAuditor)){
                log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
                txtCognizantAuditor.setText(cognizantAuditor);
                txtCognizantAuditor.requestFocus();
                dataOK=false;
            }
        }
        return dataOK;
    }

    /**
     * Set the Font for all labels
     */
    private void setLabelFont() {
        lblHumanSubAssurance.setFont(CoeusFontFactory.getLabelFont());
        lblAnimalWelfareAssurance.setFont(CoeusFontFactory.getLabelFont());
        lblScienceMisconductComplDate.setFont(CoeusFontFactory.getLabelFont());
        lblPhsAccount.setFont(CoeusFontFactory.getLabelFont());
        lblNsfInstitutionalCode.setFont(CoeusFontFactory.getLabelFont());
        lblIndirectCostRateAgreement.setFont(CoeusFontFactory.getLabelFont());
        lblCognizantAuditor.setFont(CoeusFontFactory.getLabelFont());
        lblOnrResidentRep.setFont(CoeusFontFactory.getLabelFont());
    }

     /**
     * Set values for form controls which is in formData Vector
     */
    public void setValues() {
        if (formData != null) {
            txtHumanSubAssurance.setText(formData.getHumanSubAssurance() == null ? "" : formData.getHumanSubAssurance());
            txtHumanSubAssurance.setCaretPosition(0);
            txtAnimalWelfareAssurance.setText(formData.getAnimalWelfareAssurance() == null ? "" : formData.getAnimalWelfareAssurance());
            txtScienceMisconductComplDate.setText(formData.getScienceMisconductComplDate() == null ? "" : dateUtils.formatDate(formData.getScienceMisconductComplDate(), "dd-MMM-yyyy"));
            focusDate = formData.getScienceMisconductComplDate() == null ? "" : dateUtils.formatDate(formData.getScienceMisconductComplDate(), "MM/dd/yyyy");
            txtPhsAccount.setText(formData.getPhsAcount() == null ? "" : formData.getPhsAcount());
            txtNsfInstitutionalCode.setText(formData.getNsfInstitutionalCode() == null ? "" : formData.getNsfInstitutionalCode());
            txtIndirectCostRateAgreement.setText(formData.getIndirectCostRateAgreement() == null ? "" : formData.getIndirectCostRateAgreement());
            txtIndirectCostRateAgreement.setCaretPosition(0);
            txtCognizantAuditor.setText(formData.getCognizantAuditorName() == null ? "" : formData.getCognizantAuditorName());
            txtOnrResidentRep.setText(formData.getOnrResidentRepName() == null ? "" : formData.getOnrResidentRepName());
            txtOnrResidentRep.setCaretPosition(0);
            cognizantAuditor = txtCognizantAuditor.getText().trim();
            cognizantAuditorId = formData.getCognizantAuditor();
            onrResidentRep = txtOnrResidentRep.getText().trim();
            onrResidentRepId = formData.getOnrResidentRep();
            formatFields();
        }
    }

    /**
     * Set the bean with the components values.
     */
    public void setData() {
        formData.setHumanSubAssurance(txtHumanSubAssurance.getText());
        formData.setAnimalWelfareAssurance(txtAnimalWelfareAssurance.getText());
        formData.setScienceMisconductComplDate(txtScienceMisconductComplDate.getText());
        formData.setPhsAcount(txtPhsAccount.getText());
        formData.setNsfInstitutionalCode(txtNsfInstitutionalCode.getText());
        formData.setIndirectCostRateAgreement(txtIndirectCostRateAgreement.getText());
        formData.setCognizantAuditor(cognizantAuditorId);
        formData.setOnrResidentRep(onrResidentRepId);
    }

    /**
     * set enabled or diabled for form controls as per the functionality
     *
     * @param functionType the functionality type
     *                 'D' - display
     *                 'M' - modify
     */
    public void formatFields() {
        boolean enableStatus = true;
        if (functionType == DISPLAY_FUNCTION) {
            enableStatus = false;
        } else {
            enableStatus = true;
        }
        txtHumanSubAssurance.setEditable(enableStatus);
        txtAnimalWelfareAssurance.setEditable(enableStatus);
        txtScienceMisconductComplDate.setEditable(enableStatus);
        txtPhsAccount.setEditable(enableStatus);
        txtNsfInstitutionalCode.setEditable(enableStatus);
        txtIndirectCostRateAgreement.setEditable(enableStatus);
        txtCognizantAuditor.setEditable(enableStatus);
        txtOnrResidentRep.setEditable(enableStatus);
        btnAuditor.setEnabled(enableStatus);
        btnResidentRep.setEnabled(enableStatus);

    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    public void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }

    /**
     *  Method used to set the dataChanged to true if any user changes data in any field
     * in this form.
     */
    private void userDataChanged() {
        // Add your handling code here:
        dataChanged = true;
    }

    /**
     * This class is to fire the key events in all the text boxes to check whether user
     * changed any information before closing the window.
     *
     */

    class CoeusTextListener implements DocumentListener {
        public void insertUpdate(DocumentEvent evt) {
            userDataChanged();
        }

        public void changedUpdate(DocumentEvent evt) {
            userDataChanged();
        }

        public void removeUpdate(DocumentEvent evt) {
            userDataChanged();
        }
    }

    class DisplayRolodex implements ActionListener {

        public void actionPerformed(ActionEvent aevent) {
            try {
                CoeusSearch coeusSearch = new CoeusSearch(
                            CoeusGuiConstants.getMDIForm(), "ROLODEXSEARCH", 1);
                coeusSearch.showSearchWindow();
                HashMap orgRolodexID = coeusSearch.getSelectedRow();
                if (orgRolodexID != null) {

                    String firstName = Utils.convertNull(orgRolodexID.get("FIRST_NAME"));
                    String middleName = Utils.convertNull(orgRolodexID.get("MIDDLE_NAME"));
                    String lastName = Utils.convertNull(orgRolodexID.get("LAST_NAME"));
                    String prefix = Utils.convertNull(orgRolodexID.get("PREFIX"));
                    String suffix = Utils.convertNull(orgRolodexID.get("SUFFIX"));
                    String rolodexName = "";
                    if (lastName.length() > 0) {
                        rolodexName = lastName +" "+ suffix +", "+prefix +" "+ firstName +" "+ middleName;
                    } else {
                        rolodexName = Utils.convertNull(orgRolodexID.get("ORGANIZATION"));
                    }

                    String rolodexID = orgRolodexID.get("ROLODEX_ID").toString();
                    String keyValue = coeusSearch.getSelectedValue();
                    if (aevent.getSource() instanceof JButton) {
                        JButton temp = (JButton) aevent.getSource();
                        if (temp.getName().trim().equalsIgnoreCase("Auditor")) {
                            cognizantAuditor = rolodexName.trim();
                            cognizantAuditorId = Integer.parseInt(rolodexID);
                            txtCognizantAuditor.setText(rolodexName);
                            txtCognizantAuditor.setCaretPosition(0);                            
                        } else if (temp.getName().trim().equalsIgnoreCase("ResidentRep")) {
                            onrResidentRep = rolodexName.trim();
                            onrResidentRepId = Integer.parseInt(rolodexID);
                            txtOnrResidentRep.setText(rolodexName);
                            txtOnrResidentRep.setCaretPosition(0);
                        }
                        saveRequired = true;
                    }
                }
            } catch (Exception e) {
                log("Coeus Search is not available.." + e.getMessage());
            }
        }
    }

}

