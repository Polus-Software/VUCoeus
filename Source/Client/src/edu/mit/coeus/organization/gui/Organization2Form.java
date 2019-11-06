/*
 * @(#)Organization2Form.java 1.0 9/1/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.organization.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.utils.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class constructs the Organization detail's Organization2Form
 *
 * @version :1.0 September 1, 2002, 1:35 PM
 * @author Guptha K
 */
public class Organization2Form extends JPanel implements KeyListener {

    private JPanel mainPanel;
    private JPanel pnlForm;
    private JLabel lblIncorporatedIn;
    private JLabel lblIncorporatedDate;
    private JLabel lblIrsTaxExemption;
    private JLabel lblMassTaxExemptNum;
    private JLabel lblComGovEntityCode;
    private JLabel lblMassEmployeeClaim;
    private JLabel lblDuns;
    private JLabel lblDodac;
    private JLabel lblNoOfEmployees;
    private JLabel lblFederalEmployer;
    private JLabel lblAgency;
    private JLabel lblVendor;
    private JLabel lblDuns4;
    private JLabel lblCage;
    private CoeusTextField txtIncorporatedIn;
    private CoeusTextField txtIncorporatedDate;
    private CoeusTextField txtIrsTaxExemption;
    private CoeusTextField txtMassTaxExemptNum;
    private CoeusTextField txtComGovEntityCode;
    private CoeusTextField txtMassEmployeeClaim;
    private CoeusTextField txtDuns;
    private CoeusTextField txtDodac;
    private CoeusTextField txtNoOfEmployees;
    private CoeusTextField txtCage;
    private CoeusTextField txtFederalEmployer;
    private CoeusTextField txtAgency;
    private CoeusTextField txtVendor;
    private CoeusTextField txtDuns4;

    private OrganizationMaintenanceFormBean formData;
    private CoeusAppletMDIForm mdiForm;
    private char functionType;
    private boolean dataChanged = false;
    private DateUtils dateUtils;
    private String focusDate;
    private boolean saveRequired;

    /**
     * Constructor which instantiates Organization2Form and populates them with data
     * specified, in Organization Module. And sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param formData, a OrganizationMaintenanceFormBean which consists of all the details
     * of a Organization2Form.
     */
    public Organization2Form(char functionType, OrganizationMaintenanceFormBean formData) {
        this.functionType = functionType;
        this.formData = formData;
        initComponents();
        
        java.awt.Component[] component={txtIncorporatedIn,txtIncorporatedDate,txtNoOfEmployees,
        txtIrsTaxExemption,txtFederalEmployer, txtMassTaxExemptNum, txtAgency, txtComGovEntityCode, 
        txtVendor, txtMassEmployeeClaim, txtDuns, txtDuns4, txtDodac, txtCage};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        
        setValues();
    }

    /**
     * initialize the form controls.
     */
    private void initComponents() {
        pnlForm = new JPanel();
        lblIncorporatedIn = new JLabel();
        lblIncorporatedDate = new JLabel();
        lblIrsTaxExemption = new JLabel();
        lblMassTaxExemptNum = new JLabel();
        lblComGovEntityCode = new JLabel();
        lblMassEmployeeClaim = new JLabel();
        lblDuns = new JLabel();
        lblDodac = new JLabel();
        lblNoOfEmployees = new JLabel();
        lblFederalEmployer = new JLabel();
        lblAgency = new JLabel();
        lblVendor = new JLabel();
        lblDuns4 = new JLabel();
        lblCage = new JLabel();
        txtIncorporatedIn = new CoeusTextField();
        txtIncorporatedIn.setDocument( new LimitedPlainDocument(50) );
        txtIncorporatedDate = new CoeusTextField();
        //date format MM/DD/YYYY
        txtIncorporatedDate.setDocument( new LimitedPlainDocument(11) );
        dateUtils = new DateUtils();
        txtIncorporatedDate.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if (!fe.isTemporary()) {
                    if ((txtIncorporatedDate.getText() != null) && 
                        (!txtIncorporatedDate.getText().trim().equals("")) && 
                        (functionType != 'D')) {
                        txtIncorporatedDate.setText(focusDate);
                    }
                }
            }

            public void focusLost(FocusEvent fe) {
                String editingValue = null;
                if (!fe.isTemporary()) {
                    if ((txtIncorporatedDate.getText() != null) && 
                        (!txtIncorporatedDate.getText().trim().equals("")) && 
                        (functionType != 'D') ) {
                        String convertedDate = dateUtils.formatDate(txtIncorporatedDate.getText(), "/-:,", "dd-MMM-yyyy");
                        if (convertedDate == null) {
                            log("Please enter valid date");
                            txtIncorporatedDate.requestFocus();
                        } else {
                            focusDate = txtIncorporatedDate.getText();
                            txtIncorporatedDate.setText(convertedDate);
                        }
                    }
                }
            }
        });

        txtIrsTaxExemption = new CoeusTextField();
        txtIrsTaxExemption.setDocument(new LimitedPlainDocument(30));
        txtIrsTaxExemption.getDocument().addDocumentListener(new CoeusTextListener());

        txtMassTaxExemptNum = new CoeusTextField();
        txtMassTaxExemptNum.setDocument(new LimitedPlainDocument(30));
        txtMassTaxExemptNum.getDocument().addDocumentListener(new CoeusTextListener());

        txtComGovEntityCode = new CoeusTextField();
        txtComGovEntityCode.setDocument(new LimitedPlainDocument(30));
        txtComGovEntityCode.getDocument().addDocumentListener(new CoeusTextListener());

        txtMassEmployeeClaim = new CoeusTextField();
        txtMassEmployeeClaim.setDocument(new LimitedPlainDocument(30));
        txtMassEmployeeClaim.getDocument().addDocumentListener(new CoeusTextListener());

        txtDuns = new CoeusTextField();
        txtDuns.setDocument(new LimitedPlainDocument(20));
        txtDuns.getDocument().addDocumentListener(new CoeusTextListener());

        txtDodac = new CoeusTextField();
        txtDodac.setDocument(new LimitedPlainDocument(20));
        txtDodac.getDocument().addDocumentListener(new CoeusTextListener());

        txtNoOfEmployees = new CoeusTextField();
        txtNoOfEmployees.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 6));
        txtNoOfEmployees.getDocument().addDocumentListener(new CoeusTextListener());

        txtCage = new CoeusTextField();
        txtCage.setDocument(new LimitedPlainDocument(20));
        txtCage.getDocument().addDocumentListener(new CoeusTextListener());

        txtFederalEmployer = new CoeusTextField();
        txtFederalEmployer.setDocument(new LimitedPlainDocument(15));
        txtFederalEmployer.getDocument().addDocumentListener(new CoeusTextListener());

        txtAgency = new CoeusTextField();
        txtAgency.setDocument(new LimitedPlainDocument(30));
        txtAgency.getDocument().addDocumentListener(new CoeusTextListener());

        txtVendor = new CoeusTextField();
        txtVendor.setDocument(new LimitedPlainDocument(30));
        txtVendor.getDocument().addDocumentListener(new CoeusTextListener());

        txtDuns4 = new CoeusTextField();
        txtDuns4.setDocument(new LimitedPlainDocument(20));
        txtDuns4.getDocument().addDocumentListener(new CoeusTextListener());

        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        pnlForm.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        lblIncorporatedIn.setText("Incorporated In:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblIncorporatedIn, gridBagConstraints2);

        lblIncorporatedDate.setText("Incorporated Date:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblIncorporatedDate, gridBagConstraints2);

        lblIrsTaxExemption.setText("IRS Tax Exemption:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblIrsTaxExemption, gridBagConstraints2);

        lblMassTaxExemptNum.setText("Mass Tax Exempt Num:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblMassTaxExemptNum, gridBagConstraints2);

        lblComGovEntityCode.setText("Com Gov Entity Code:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblComGovEntityCode, gridBagConstraints2);

        lblMassEmployeeClaim.setText("Mass Employee Claim:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 5;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblMassEmployeeClaim, gridBagConstraints2);

        lblDuns.setText("Duns:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblDuns, gridBagConstraints2);

        lblDodac.setText("Dodac:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblDodac, gridBagConstraints2);

        lblNoOfEmployees.setText("Number Of Employees:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = 3;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblNoOfEmployees, gridBagConstraints2);

        lblFederalEmployer.setText("Federal Employer:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblFederalEmployer, gridBagConstraints2);

        lblAgency.setText("Agency:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblAgency, gridBagConstraints2);

        lblVendor.setText("Vendor:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblVendor, gridBagConstraints2);

        lblDuns4.setText("Duns+4:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblDuns4, gridBagConstraints2);

        lblCage.setText("Cage:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblCage, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridwidth = 6;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtIncorporatedIn.setPreferredSize(new Dimension(370,20));
        txtIncorporatedIn.setMaximumSize(new Dimension(370,20));
        txtIncorporatedIn.setMinimumSize(new Dimension(370,20));
        pnlForm.add(txtIncorporatedIn, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtIncorporatedDate.setPreferredSize(new Dimension(75,20));
        txtIncorporatedDate.setMaximumSize(new Dimension(75,20));
        txtIncorporatedDate.setMinimumSize(new Dimension(75,20));
        pnlForm.add(txtIncorporatedDate, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtIrsTaxExemption.setPreferredSize(new Dimension(150,20));
        txtIrsTaxExemption.setMaximumSize(new Dimension(150,20));
        txtIrsTaxExemption.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtIrsTaxExemption, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtMassTaxExemptNum.setPreferredSize(new Dimension(150,20));
        txtMassTaxExemptNum.setMaximumSize(new Dimension(150,20));
        txtMassTaxExemptNum.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtMassTaxExemptNum, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtComGovEntityCode.setPreferredSize(new Dimension(150,20));
        txtComGovEntityCode.setMaximumSize(new Dimension(150,20));
        txtComGovEntityCode.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtComGovEntityCode, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 5;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtMassEmployeeClaim.setPreferredSize(new Dimension(150,20));
        txtMassEmployeeClaim.setMaximumSize(new Dimension(150,20));
        txtMassEmployeeClaim.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtMassEmployeeClaim, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtDuns.setPreferredSize(new Dimension(150,20));
        txtDuns.setMaximumSize(new Dimension(150,20));
        txtDuns.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtDuns, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtDodac.setPreferredSize(new Dimension(150,20));
        txtDodac.setMaximumSize(new Dimension(150,20));
        txtDodac.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtDodac, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 5;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtNoOfEmployees.setPreferredSize(new Dimension(90,20));
        txtNoOfEmployees.setMaximumSize(new Dimension(90,20));
        txtNoOfEmployees.setMinimumSize(new Dimension(90,20));
        pnlForm.add(txtNoOfEmployees, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 4;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtCage.setPreferredSize(new Dimension(150,20));
        txtCage.setMaximumSize(new Dimension(150,20));
        txtCage.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtCage, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 5;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtNoOfEmployees.setPreferredSize(new Dimension(96,20));
        txtNoOfEmployees.setMaximumSize(new Dimension(96,20));
        txtNoOfEmployees.setMinimumSize(new Dimension(96,20));
        pnlForm.add(txtFederalEmployer, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 4;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtAgency.setPreferredSize(new Dimension(150,20));
        txtAgency.setMaximumSize(new Dimension(150,20));
        txtAgency.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtAgency, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 4;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtNoOfEmployees.setPreferredSize(new Dimension(90,20));
        txtNoOfEmployees.setMaximumSize(new Dimension(90,20));
        txtNoOfEmployees.setMinimumSize(new Dimension(90,20));
        pnlForm.add(txtVendor, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 4;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtDuns4.setPreferredSize(new Dimension(150,20));
        txtDuns4.setMaximumSize(new Dimension(150,20));
        txtDuns4.setMinimumSize(new Dimension(150,20));
        pnlForm.add(txtDuns4, gridBagConstraints2);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        this.setLayout(flowLayout);
        add(pnlForm);
        setLabelFont();

        txtIncorporatedIn.addKeyListener(this);
        txtIncorporatedDate.addKeyListener(this);
        txtNoOfEmployees.addKeyListener(this);
        txtIrsTaxExemption.addKeyListener(this);
        txtFederalEmployer.addKeyListener(this);
        txtMassTaxExemptNum.addKeyListener(this);
        txtAgency.addKeyListener(this);
        txtComGovEntityCode.addKeyListener(this);
        txtVendor.addKeyListener(this);
        txtMassEmployeeClaim.addKeyListener(this);
        txtDuns.addKeyListener(this);
        txtDuns4.addKeyListener(this);
        txtDodac.addKeyListener(this);
        txtCage.addKeyListener(this);

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
                txtIncorporatedIn.requestFocusInWindow();            
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

    /**
     * set font for all labels
     */
    private void setLabelFont() {
        lblIncorporatedIn.setFont(CoeusFontFactory.getLabelFont());
        lblIncorporatedDate.setFont(CoeusFontFactory.getLabelFont());
        lblIrsTaxExemption.setFont(CoeusFontFactory.getLabelFont());
        lblMassTaxExemptNum.setFont(CoeusFontFactory.getLabelFont());
        lblComGovEntityCode.setFont(CoeusFontFactory.getLabelFont());
        lblMassEmployeeClaim.setFont(CoeusFontFactory.getLabelFont());
        lblDuns.setFont(CoeusFontFactory.getLabelFont());
        lblDodac.setFont(CoeusFontFactory.getLabelFont());
        lblNoOfEmployees.setFont(CoeusFontFactory.getLabelFont());
        lblFederalEmployer.setFont(CoeusFontFactory.getLabelFont());
        lblAgency.setFont(CoeusFontFactory.getLabelFont());
        lblVendor.setFont(CoeusFontFactory.getLabelFont());
        lblDuns4.setFont(CoeusFontFactory.getLabelFont());
        lblCage.setFont(CoeusFontFactory.getLabelFont());

    }

    /**
     * Set values for form controls which is in formData Vector
     */
    public void setValues() {
        if (formData != null) {

            txtIncorporatedIn.setText(formData.getIncorporatedIn() == null ? "" : formData.getIncorporatedIn());
            txtIncorporatedIn.setCaretPosition(0);

            txtIncorporatedDate.setText(formData.getIncorporatedDate() == null ? "" : dateUtils.formatDate(formData.getIncorporatedDate(), "dd-MMM-yyyy"));
            focusDate = formData.getIncorporatedDate() == null ? "" : dateUtils.formatDate(formData.getIncorporatedDate(), "MM/dd/yyyy");
            txtNoOfEmployees.setText(new Integer(formData.getNumberOfExmployees()).toString());
            txtIrsTaxExemption.setText(formData.getIrsTaxExcemption() == null ? "" : formData.getIrsTaxExcemption());
            txtFederalEmployer.setText(formData.getFederalEmployerID() == null ? "" : formData.getFederalEmployerID());
            txtMassTaxExemptNum.setText(formData.getMassTaxExcemptNum() == null ? "" : formData.getMassTaxExcemptNum());
            txtAgency.setText(formData.getAgencySymbol() == null ? "" : formData.getAgencySymbol());
            txtComGovEntityCode.setText(formData.getComGovEntityCode() == null ? "" : formData.getComGovEntityCode());
            txtVendor.setText(formData.getVendorCode() == null ? "" : formData.getVendorCode());
            txtMassEmployeeClaim.setText(formData.getMassEmployeeClaim() == null ? "" : formData.getMassEmployeeClaim());
            txtDuns.setText(formData.getDunsNumber() == null ? "" : formData.getDunsNumber());
            txtDuns4.setText(formData.getDunsPlusFourNumber() == null ? "" : formData.getDunsPlusFourNumber());
            txtDodac.setText(formData.getDodacNumber() == null ? "" : formData.getDodacNumber());
            txtCage.setText(formData.getCageNumber() == null ? "" : formData.getCageNumber());
            formatFields();
        }
    }
    /**
     * This method is used to check whether the bean data is modified or not in
     * modify mode.
     * @returns true if any modifications are done else return false.
     */
    public boolean isDataChanged() {
        return dataChanged;
    }

    /**
     * Set the bean with the components values.
     */
    public void setData() {
        formData.setIncorporatedIn(txtIncorporatedIn.getText());
        formData.setIncorporatedDate(txtIncorporatedDate.getText());
        //parse the date into time format
        formData.setNumberOfExmployees(new Integer((txtNoOfEmployees.getText().equals("") ? "0" : txtNoOfEmployees.getText())).intValue());
        formData.setIrsTaxExcemption(txtIrsTaxExemption.getText());
        formData.setFederalEmployerID(txtFederalEmployer.getText());
        formData.setMassTaxExcemptNum(txtMassTaxExemptNum.getText());
        formData.setAgencySymbol(txtAgency.getText());
        formData.setComGovEntityCode(txtComGovEntityCode.getText());
        formData.setVendorCode(txtVendor.getText());
        formData.setMassEmployeeClaim(txtMassEmployeeClaim.getText());
        formData.setDunsNumber(txtDuns.getText());
        formData.setDunsPlusFourNumber(txtDuns4.getText());
        formData.setDodacNumber(txtDodac.getText());
        formData.setCageNumber(txtCage.getText());
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
        if (functionType == 'D') {
            enableStatus = false;
        } else {
            enableStatus = true;
        }
        txtIncorporatedIn.setEditable(enableStatus);
        txtIncorporatedDate.setEditable(enableStatus);
        txtNoOfEmployees.setEditable(enableStatus);
        txtIrsTaxExemption.setEditable(enableStatus);
        txtFederalEmployer.setEditable(enableStatus);
        txtMassTaxExemptNum.setEditable(enableStatus);
        txtAgency.setEditable(enableStatus);
        txtComGovEntityCode.setEditable(enableStatus);
        txtVendor.setEditable(enableStatus);
        txtMassEmployeeClaim.setEditable(enableStatus);
        txtDuns.setEditable(enableStatus);
        txtDuns4.setEditable(enableStatus);
        txtDodac.setEditable(enableStatus);
        txtCage.setEditable(enableStatus);

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
     * Method used to set the dataChanged to true if any user changes data in any field
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


}

