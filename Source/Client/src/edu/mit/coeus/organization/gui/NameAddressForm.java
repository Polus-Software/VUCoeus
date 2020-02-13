/*
 * @(#)NameAddressForm.java 1.0 8/31/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.organization.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.search.gui.SearchResultWindow;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

/**
 * This class constructs the Organization detail's name and address form
 *
 * @version :1.0 August 31, 2002, 1:35 PM
 * @author Guptha K
 */
public class NameAddressForm extends JPanel implements KeyListener {

    private JPanel mainPanel;
    private JPanel pnlForm;
    private JLabel lblOrgid;
    private JLabel lblOrgname;
    private JLabel lblOrgcadd;
    private JLabel lblOrgadd;
    private JLabel lblOrgcaadd;
    private JLabel lblOrgtno;
    private JLabel lblOrgctry;
    private JLabel lblOrgdist;

    private CoeusTextField txtOrgid;
    private CoeusTextField txtOrgname;
    private CoeusTextField txtOrgcadd;
    private CoeusTextField txtOrgadd;
    private CoeusTextField txtOrgcaadd;
    private CoeusTextField txtOrgtno;
    private CoeusTextField txtOrgctry;
    private CoeusTextField txtOrgdist;

    private JButton btnOrgcadd;

    public OrganizationMaintenanceFormBean formData;
    private char functionType;
    private boolean dataChanged = false;
    private boolean saveRequired = false;

    private CoeusAppletMDIForm mdiForm;
    private final String SEARCH_IDENTIFIER = "OrganizationSearch";
    private String contactAddress;
    private int contactAddressId;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    // add functionality
    private final char ADD_FUNCTION = 'I';
    // modify functionality
    private final char MODIFY_FUNCTION = 'U';
    // display functionality
    private final char DISPLAY_FUNCTION = 'D';

    /**
     * Constructor which instantiates NameAddressForm and populates them with data
     * specified, in Organization Module. And sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param formData, a OrganizationMaintenanceFormBean which consists of all the details
     * of a NameAddress.
     */
    
    public NameAddressForm(char functionType, OrganizationMaintenanceFormBean formData) {
        this.functionType = functionType;
        this.formData = formData;
        initComponents();
        
        java.awt.Component[] components={txtOrgid,txtOrgname, txtOrgcadd,btnOrgcadd, txtOrgadd,
        txtOrgcaadd,txtOrgtno,txtOrgctry,txtOrgdist};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(components);
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
        
        contactAddress = "";
        contactAddressId = 0;
        mainPanel = new javax.swing.JPanel();
        pnlForm = new javax.swing.JPanel();
        lblOrgid = new javax.swing.JLabel();

        txtOrgid = new CoeusTextField();
        txtOrgid.setDocument(new LimitedPlainDocument(8));
        //txtOrgid.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 6));
        txtOrgid.getDocument().addDocumentListener(new CoeusTextListener());

        lblOrgname = new javax.swing.JLabel();

        txtOrgname = new CoeusTextField();
        txtOrgname.setDocument(new LimitedPlainDocument(60));
        txtOrgname.getDocument().addDocumentListener(new CoeusTextListener());
// JM 7-22-2011 added highlighting for required field
        txtOrgname.setRequired(true);
// END

        lblOrgcadd = new javax.swing.JLabel();
        lblOrgadd = new javax.swing.JLabel();
        lblOrgcaadd = new javax.swing.JLabel();
        lblOrgtno = new javax.swing.JLabel();
        lblOrgctry = new javax.swing.JLabel();
        lblOrgdist = new javax.swing.JLabel();
        txtOrgcadd = new CoeusTextField();
        txtOrgcadd.setDocument(new LimitedPlainDocument(60));
        txtOrgcadd.getDocument().addDocumentListener(new CoeusTextListener());
// JM 7-22-2011 added highlighting for required field
        txtOrgcadd.setRequired(true);
// END

        txtOrgcadd.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2 &&
                        !txtOrgcadd.getText().trim().equals("") ){  //&&
//                        functionType != DISPLAY_FUNCTION) {   //Commented by Vyjayanthi for bug-fix
                    //invoke rolodex display
                    txtOrgcadd.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));        
                    RolodexMaintenanceDetailForm rolodex = new RolodexMaintenanceDetailForm('V', "" + contactAddressId);
                    rolodex.showForm(mdiForm, "Rolodex", true);
                    txtOrgcadd.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));        
                }
            }
        });
       txtOrgcadd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                validateAddress();
            }
        });
        txtOrgadd = new CoeusTextField();
        txtOrgadd.setDocument(new LimitedPlainDocument(60));
        txtOrgadd.getDocument().addDocumentListener(new CoeusTextListener());

        txtOrgcaadd = new CoeusTextField();
        txtOrgcaadd.setDocument(new LimitedPlainDocument(20));
        txtOrgcaadd.getDocument().addDocumentListener(new CoeusTextListener());

        txtOrgtno = new CoeusTextField();
        txtOrgtno.setDocument(new LimitedPlainDocument(20));
        txtOrgtno.getDocument().addDocumentListener(new CoeusTextListener());

        txtOrgctry = new CoeusTextField();
        txtOrgctry.setDocument(new LimitedPlainDocument(30));
        txtOrgctry.getDocument().addDocumentListener(new CoeusTextListener());

        btnOrgcadd = new javax.swing.JButton(searchIcon);
        txtOrgdist = new CoeusTextField();
        txtOrgdist.setDocument(new LimitedPlainDocument(50));
        txtOrgdist.getDocument().addDocumentListener(new CoeusTextListener());

        java.awt.GridBagConstraints gridBagConstraints1;

        pnlForm.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        lblOrgid.setText("Organization Id:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgid, gridBagConstraints2);
        
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        txtOrgid.setPreferredSize(new Dimension(80,20));
        txtOrgid.setMaximumSize(new Dimension(80,20));
        txtOrgid.setMinimumSize(new Dimension(80,20));
        pnlForm.add(txtOrgid, gridBagConstraints2);

        lblOrgname.setText("Organization Name:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgname, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = 4;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtOrgname.setPreferredSize(new Dimension(200,20));
        txtOrgname.setMaximumSize(new Dimension(200,20));
        txtOrgname.setMinimumSize(new Dimension(200,20));
        pnlForm.add(txtOrgname, gridBagConstraints2);

        lblOrgcadd.setText("Contact Address:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgcadd, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = 2;
        //gridBagConstraints2.ipadx = 100;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 3);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        
        //Bug Fix:1077 Start 2
        txtOrgcadd.setPreferredSize(new Dimension(200,20));
        txtOrgcadd.setMaximumSize(new Dimension(200,20));
        txtOrgcadd.setMinimumSize(new Dimension(200,20));
        //Bug Fix:1077 End 2
        
        pnlForm.add(txtOrgcadd, gridBagConstraints2);

        btnOrgcadd.setName("CADDRESS");
        btnOrgcadd.addActionListener(new DisplayRolodex());
        
        //Bug Fix:1077 Start 3 
        btnOrgcadd.setPreferredSize(new java.awt.Dimension(23,23));
        //Bug Fix:1077 End 3
        
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        pnlForm.add(btnOrgcadd, gridBagConstraints2);


        lblOrgadd.setText("Address:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgadd, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.gridwidth = 4;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtOrgadd.setPreferredSize(new Dimension(200,20));
        txtOrgadd.setMaximumSize(new Dimension(200,20));
        txtOrgadd.setMinimumSize(new Dimension(200,20));
        pnlForm.add(txtOrgadd, gridBagConstraints2);

        lblOrgcaadd.setText("Cable Address:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgcaadd, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtOrgcaadd.setPreferredSize(new Dimension(100,20));
        txtOrgcaadd.setMaximumSize(new Dimension(100,20));
        txtOrgcaadd.setMinimumSize(new Dimension(100,20));
        pnlForm.add(txtOrgcaadd, gridBagConstraints2);

        lblOrgtno.setText("Telex Number:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 5;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgtno, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 5;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtOrgtno.setPreferredSize(new Dimension(100,20));
        txtOrgtno.setMaximumSize(new Dimension(100,20));
        txtOrgtno.setMinimumSize(new Dimension(100,20));
        pnlForm.add(txtOrgtno, gridBagConstraints2);

        lblOrgctry.setText("County:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgctry, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtOrgctry.setPreferredSize(new Dimension(100,20));
        txtOrgctry.setMaximumSize(new Dimension(100,20));
        txtOrgctry.setMinimumSize(new Dimension(100,20));
        pnlForm.add(txtOrgctry, gridBagConstraints2);

        lblOrgdist.setText("Congressional District:");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 20, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        pnlForm.add(lblOrgdist, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.gridwidth = 4;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        txtOrgdist.setPreferredSize(new Dimension(200,20));
        txtOrgdist.setMaximumSize(new Dimension(200,20));
        txtOrgdist.setMinimumSize(new Dimension(200,20));
        pnlForm.add(txtOrgdist, gridBagConstraints2);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        this.setLayout(flowLayout);
        add(pnlForm);
        setLabelFont();

        txtOrgid.addKeyListener(this);
        txtOrgname.addKeyListener(this);
        txtOrgcadd.addKeyListener(this);
        txtOrgadd.addKeyListener(this);
        txtOrgcaadd.addKeyListener(this);
        txtOrgtno.addKeyListener(this);
        txtOrgctry.addKeyListener(this);
        txtOrgdist.addKeyListener(this);
        btnOrgcadd.addKeyListener(this);

        txtOrgname.requestDefaultFocus();

    }
    
    /**
     * This method is used for client side validations.
     * It validates the imcomplete submission of table values.
     * @ return true if the validation succeed or else false.
     */
    
    public void validateAddress(){
        if (txtOrgcadd.getText().trim().length()<=0){
            log(coeusMessageResources.parseMessageKey(
                                            "orgNmAddrFrm_exceptionCode.1098"));
            txtOrgcadd.setText(contactAddress);
            txtOrgcadd.requestFocus();
        }else if(!txtOrgcadd.getText().trim().equalsIgnoreCase(contactAddress)){
            log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
            txtOrgcadd.setText(contactAddress);
            txtOrgcadd.requestFocus();
        }
    }

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
        lblOrgid.setFont(CoeusFontFactory.getLabelFont());
        lblOrgname.setFont(CoeusFontFactory.getLabelFont());
        lblOrgcadd.setFont(CoeusFontFactory.getLabelFont());
        lblOrgadd.setFont(CoeusFontFactory.getLabelFont());
        lblOrgcaadd.setFont(CoeusFontFactory.getLabelFont());
        lblOrgtno.setFont(CoeusFontFactory.getLabelFont());
        lblOrgctry.setFont(CoeusFontFactory.getLabelFont());
        lblOrgdist.setFont(CoeusFontFactory.getLabelFont());
    }

    /**
     * Set values for form controls which is in formData Vector
     */
    public void setValues() {
        if (formData != null) {
            txtOrgid.setText(formData.getOrganizationId() == null ? "" : formData.getOrganizationId());
            txtOrgname.setText(formData.getOrganizationName() == null ? "" : formData.getOrganizationName());
            txtOrgcadd.setText(formData.getContactAddressName() == null ? "" : formData.getContactAddressName());
            txtOrgcadd.setCaretPosition(0);
            txtOrgadd.setText(formData.getAddress() == null ? "" : formData.getAddress());
            txtOrgadd.setCaretPosition(0);
            txtOrgcaadd.setText(formData.getCableAddress() == null ? "" : formData.getCableAddress());
            txtOrgcaadd.setCaretPosition(0);
            txtOrgtno.setText(formData.getTelexNumber() == null ? "" : formData.getTelexNumber());
            txtOrgctry.setText(formData.getCounty() == null ? "" : formData.getCounty());
            txtOrgdist.setText(formData.getCongressionalDistrict() == null ? "" : formData.getCongressionalDistrict());
            txtOrgdist.setCaretPosition(0);
            contactAddress = txtOrgcadd.getText().trim();
            contactAddressId = formData.getContactAddressId();
            formatFields();
        }
    }

    /**
     * set the bean with the components values.
     */
    public void setData() {
        formData.setOrganizationId(txtOrgid.getText());
        formData.setOrganizationName(txtOrgname.getText());
        formData.setContactAddressId(contactAddressId);
        formData.setAddress(txtOrgadd.getText());
        formData.setCableAddress(txtOrgcaadd.getText());
        formData.setTelexNumber(txtOrgtno.getText());
        formData.setCounty(txtOrgctry.getText());
        formData.setCongressionalDistrict(txtOrgdist.getText());
    }
    /**
     * set enabled or diabled for form controls as per the functionality
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
        txtOrgid.setEditable(functionType == ADD_FUNCTION ? true:false);

        txtOrgname.setEditable(enableStatus);
        txtOrgcadd.setEditable(enableStatus);
        txtOrgadd.setEditable(enableStatus);
        txtOrgcaadd.setEditable(enableStatus);
        txtOrgtno.setEditable(enableStatus);
        txtOrgctry.setEditable(enableStatus);
        txtOrgdist.setEditable(enableStatus);
        btnOrgcadd.setEnabled(enableStatus);
    }

    /**
     *  Method used to set the dataChanged to true if any user changes data in any field
     */
    private void userDataChanged() {
        // Add your handling code here:
        dataChanged = true;
    }
    
    /**
     * This method is used to determine whether the data is to be saved or not.
     * @returns boolean true if any modifications have been done and are not
     * saved, else it returns false.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {                        
                txtOrgname.requestFocusInWindow();            
        }
    }    
    //end Amit       

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
            saveRequired = true;
            userDataChanged();
        }
    }

    /**
     * validate the form data
     *
     * @return boolean
     *   true - validation is successful
     *   false - validation is unsuccessful
     *
     */
    public boolean validateData(boolean generateOrganizationID) {
        boolean dataOK = false;
        if (functionType == ADD_FUNCTION && !generateOrganizationID && (txtOrgid.getText() == null || txtOrgid.getText().trim().length() == 0)) {
            log(coeusMessageResources.parseMessageKey(
                                            "orgNmAddrFrm_exceptionCode.1099"));
            txtOrgid.requestFocus();
        } else if (functionType == ADD_FUNCTION && !generateOrganizationID && txtOrgid.getText().trim().length() < 6) {
            log(coeusMessageResources.parseMessageKey(
                                            "orgNmAddrFrm_exceptionCode.1100"));
            txtOrgid.requestFocus();
        } else if (functionType == ADD_FUNCTION && !generateOrganizationID && validateDuplicate(txtOrgid.getText().trim())) {
            log("Organization id '" + txtOrgid.getText().trim() + "' is already existing. Please enter a different one.");
            txtOrgid.requestFocus();
        } else if (txtOrgname.getText() == null || txtOrgname.getText().trim().length() == 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "orgNmAddrFrm_exceptionCode.1101"));
            txtOrgname.requestFocus();
        } else if (txtOrgcadd.getText() == null || txtOrgcadd.getText().trim().length() == 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "orgNmAddrFrm_exceptionCode.1102"));
            txtOrgcadd.requestFocus();
        } else if (txtOrgcadd.getText().trim().length() <= 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "orgNmAddrFrm_exceptionCode.1098"));
            txtOrgcadd.setText(contactAddress);
            txtOrgcadd.requestFocus();
        } else if (!txtOrgcadd.getText().trim().equalsIgnoreCase(contactAddress)) {
            log(coeusMessageResources.parseMessageKey(
                                            "protoCorroFrm_exceptionCode.1054"));
            txtOrgcadd.setText(contactAddress);
            txtOrgcadd.requestFocus();
        } else {
            dataOK = true;
        }
        return dataOK;
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
     * validate the duplication of organization id
     *
     * @param ordId organization id to be check
     * @return boolean
     *  true - organization id is already available
     *  false - organization is is not available
     */
    public boolean validateDuplicate(String orgId) {
        boolean duplicate = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("FN_ORGADDRESS"); //0001 to get the organization address
        request.setId(orgId);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                                            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            OrganizationAddressFormBean organizationAddress = (OrganizationAddressFormBean) response.getDataObject();
            if (organizationAddress.getOrganizationName() != null) {
                duplicate = true;
            }
        } else {
            log(response.getMessage());
        }
        return duplicate;
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
     * set the default focus to name field
     */
    public void setFocusToName(boolean generateOrganizationID) {
        txtOrgname.requestFocus();
        if(generateOrganizationID) {
            txtOrgid.setEnabled(false);
        }
    }

    /**
     * set the default focus to Id field
     */
    public void setFocusToId() {
        txtOrgid.requestFocus();
    }

    class DisplayRolodex implements ActionListener {

        public void actionPerformed(ActionEvent aevent) {
            try {
                CoeusSearch coeusSearch = new CoeusSearch(
                            CoeusGuiConstants.getMDIForm(), "ROLODEXSEARCH", 1);
                coeusSearch.showSearchWindow();
                HashMap orgRolodexID = coeusSearch.getSelectedRow();
                if (orgRolodexID != null && !orgRolodexID.isEmpty() ) {
                    String firstName = Utils.convertNull(orgRolodexID.get("FIRST_NAME"));
                    String middleName = Utils.convertNull(orgRolodexID.get("MIDDLE_NAME"));
                    String lastName = Utils.convertNull(orgRolodexID.get("LAST_NAME"));
                    String prefix = Utils.convertNull(orgRolodexID.get("PREFIX"));
                    String suffix = Utils.convertNull(orgRolodexID.get("SUFFIX"));
                    if (lastName.length() > 0) {
                        contactAddress = (lastName + " "+ suffix +", "+ prefix + " "+ firstName + " "+ middleName).trim();
                    } else {
                        contactAddress = Utils.convertNull(orgRolodexID.get("ORGANIZATION"));
                    }
                    contactAddressId = Integer.parseInt(orgRolodexID.get("ROLODEX_ID").toString());
                    String keyValue = coeusSearch.getSelectedValue();
                    if (aevent.getSource() instanceof JButton) {
                        JButton temp = (JButton) aevent.getSource();
                        if (temp.getName().trim().equals("CADDRESS")) {
                            txtOrgcadd.setText(contactAddress);    
                            txtOrgcadd.setCaretPosition(0);
                            saveRequired = true;
                        }
                    }
                }
            } catch (Exception e) {
                log("Coeus Search is not available.." + e.getMessage());
            }
        }
    }
}

