/*
 * @(#)SponsorMaintenanceForm.java 1.0 8/16/02 12:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-APR-2011
 * by Bharati
 */
package edu.mit.coeus.sponsormaint.gui;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.sql.Timestamp;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
//import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.sponsormaint.bean.SponsorContactBean;

//import edu.mit.coeus.utils.AppletServletCommunicator;
//import edu.mit.coeus.utils.query.Equals;
import java.util.HashMap;
//import java.util.Hashtable;

import java.util.Observer;
//import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * This class provides the GUI for the Sponsor maintenance Dialog for
 * different options like Add,Modify and Display,
 * This class will loaded on the menu selection, it has different methods
 * different events on specific sponsor maintenance.
 * In <CODE>AppletServletCommunicator</CODE> send method is used communicate with
 * the <CODE>SponsorMaintenanceDataTxnBean</CODE> <code>DBEngineImpl</code>
 * singleton instance for the database interaction.
 * This class calls <CODE>SponsorMaintemnceServlet</CODE> for database access and
 * sets the data in <CODE>SponsorMaintenanceFormBean</CODE>.
 *
 * @version 1.0 August 16, 2002, 12:17 PM
 * @author Mukundan C
 */
public class SponsorMaintenanceForm extends JComponent implements ActionListener{
    
    // Variables declaration - do not modify
    private boolean dataChanged =false;
    private char functionType;
    
    private boolean generateSponsorCode = false; //generate sponsor code flag
    
    // this vector hold the states for country USA
    private Vector comboState;
    // this vector holds row data
    private Vector rowData;
    private CoeusAppletMDIForm mdiForm;
    private SponsorDetailForm sponsorDetailForm;
    private CoeusFontFactory fontFactory;
    private CoeusDlgWindow dlgWindow;
    private SponsorMaintenanceFormBean spBean;
    private RolodexDetailsBean rldxBean;
//    private ResponderBean response;
    private RequesterBean requester;
    private String rolodexID ="";
    private Timestamp sponsorTime;
    private Timestamp rolodexTime;
    private String referenceId="";
    private String sponsorUpdateUser = "";
    private String rolodexUpdateUser = "";
    private String selectState = "";
    private String sponsorCode ="";
    private CoeusMessageResources coeusMessageResources;
//    private final String SERVLET = "/SponsorMaintenanceServlet";
    private final String TITLE_SPONSOR = "Sponsor Maintenance";
    private final String CONNECT_TO =
            CoeusGuiConstants.CONNECTION_URL + "/spMntServlet";
    public static final int SPONSOR_DETAIL_TAB = 0;
    public static final int INSTITUTIONAL_CONTACT_TAB = 1;
    private JPanel pnlMain;
    private JPanel pnlControl;
//    private JButton btnOK;
//    private JButton btnCancel;
    private JPanel pnlSponsorHeader;
    private JLabel lblSponsorCode;
    private CoeusTextField txtSponsorCode;
    private JLabel lblName;
    private CoeusTextField txtName;
    private JLabel lblType;
    private JLabel lblDuns;
    private CoeusTextField txtDuns;
    private JLabel lblDuns4;
    private CoeusTextField txtDuns4;
    private JLabel lblDodc;
    private CoeusTextField txtDodc;
    private JLabel lblCage;
    private CoeusTextField txtCage;
    private CoeusTextField txtAudit;
    private JLabel lblAcronym;
    private JLabel lblAudit;
    private JLabel lblVisualCompliance;
    private JLabel lblVisualComplianceExpl;
    private CoeusTextField txtAcronym;
    private JLabel lblUnit;
    private CoeusTextField txtUnit;
    private CoeusComboBox cmbType;
    private JLabel lblCity;
    private CoeusTextField txtAddress3;
    private JLabel lblState;
    private CoeusTextField txtCity;
    private JLabel lblCounty;
    private CoeusTextField txtCounty;
    private JLabel lblCountry;
    private CoeusTextField txtAddress2;
    private CoeusTextField txtAddress1;
    private JLabel lblAddress;
    private JLabel lblEMail;
    private JLabel lblPostalCode;
    private JLabel lblPhone;
    private JLabel lblLastUpdate;
    private JLabel lblUpdateUser;
    private JLabel lblFax;
    private CoeusTextField txtEMail;
    private CoeusTextField txtLastUpdate;
    private CoeusTextField txtPostalCode;
    private CoeusTextField txtPhone;
    private CoeusTextField txtFax;
    private CoeusTextField txtUpdateUser;
    
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    private JRadioButton radioActiveStatus;
    private JRadioButton radioInActiveStatus;
    private final String ACTIVE_STATUS = "A";
    private final String INACTIVE_STATUS = "I";
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    
    //
    private JRadioButton visualComplianceYes;
    private JRadioButton visualComplianceNo;
    private final String VISUAL_COMPLIANCE_YES = "Y";
    private final String VISUAL_COMPLIANCE_NO = "N";
    public javax.swing.JScrollPane scrpnVisualComp;
    private javax.swing.JTextArea txtVisualComplianceExpl;
    //
    //Bug Fix 1149:Start 1
    private AutoCompleteCoeusCombo cmbState;
    private CoeusVector cvStateData = new CoeusVector();
    //Bug Fix 1149:End 1
    
    //Added for Case#4248 -  State Provinces changes 
    private HashMap hmComboStateWithCountry;
    //Case#4248 - End
    
    private CoeusComboBox cmbCountry;
    private BaseWindowObservable observable = new BaseWindowObservable();
    // End of variables declaration
    
    /** Creates new form <CODE>SponsorMaintenanceForm</CODE>
     *
     * @param functionType this will open the different mode like Add/Modify
     * @param sponsorCode String
     * 'I' specifies that the form is in Insert Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     */
    public SponsorMaintenanceForm(char functionType,String sponsorCode) {
        this.functionType = functionType;
        this.sponsorCode = sponsorCode;
        initComponents();        
        if (functionType == 'D'){         
            displaySponsorDetails(sponsorCode);
            setControls(functionType);
            /*btnCancel.requestFocusInWindow();
            lblSponsorCode.requestFocusInWindow();
            lblSponsorCode.requestFocus();*/
        }else if (functionType == 'U'){
            displaySponsorDetails(sponsorCode);
            txtSponsorCode.setEditable(false);
        }else if (functionType == 'I'){
            loadComboBox();
        }
    }
    
    /** Creates new form <CODE>SponsorMaintenanceForm</CODE>
     *
     * @param functionType this will open the different mode like Add/Modify
     * @param sponsorFormBean SponsorMaintenanceFormBean
     * @param rolDetailBean RolodexDetailsBean
     * 'I' specifies that the form is in Insert Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     */
    public SponsorMaintenanceForm(char functionType,
            SponsorMaintenanceFormBean sponsorFormBean,RolodexDetailsBean rolDetailBean) {
        this.functionType = functionType;
        this.sponsorCode = sponsorFormBean.getSponsorCode();
        initComponents();
        String strSponsorCode = sponsorFormBean.getSponsorCode();
        if (functionType == 'D'){
            displaySponsorDetails(strSponsorCode);
            setControls(functionType);
            
            
        }else if (functionType == 'U'){
            displaySponsorDetails(sponsorFormBean,rolDetailBean);
            txtSponsorCode.setEditable(false);
        }else if (functionType == 'I'){
            loadComboBox();
        }
    }
    
    /** default constructor*/
    public SponsorMaintenanceForm(){
    }
    
    public void display(char functionType,
            SponsorMaintenanceFormBean sponsorFormBean,RolodexDetailsBean rolDetailBean){
        this.functionType = functionType;
        String sponsorCode = sponsorFormBean.getSponsorCode();
        if (functionType == 'D'){
            displaySponsorDetails(sponsorCode);
            setControls(functionType);
            
            
        }else if (functionType == 'U'){
            displaySponsorDetails(sponsorFormBean,rolDetailBean);
            txtSponsorCode.setEditable(false);
        }else if (functionType == 'I'){
            loadComboBox();
        }
    }
    /**
     * This panel contains the sponsormaitenance screen
     *
     * @return pnlMain main panel
     */
    public JPanel getSponsorDetail(){
        return pnlMain;
    }
    
    /**
     * This method is called from within the constructor to
     * initialize the form.
     *
     * @returns JPanel
     */
    public JPanel initComponents() {
                
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        pnlMain = new JPanel();
        pnlControl = new JPanel();
        pnlMain.setMinimumSize(new Dimension(650,400)); // JM 6-10-2015 was 560,325
        pnlMain.setPreferredSize(new Dimension(650,400)); // JM 6-10-2015 was 560,325
        
//        btnOK = new JButton();
//        btnOK.setFont(fontFactory.getLabelFont());
//        btnCancel = new JButton();
//        btnCancel.setFont(fontFactory.getLabelFont());
        
        pnlSponsorHeader = new JPanel();
        
        // JM 5-21-2015 set control area size
        pnlSponsorHeader.setMinimumSize(new Dimension(630,80)); 
        pnlSponsorHeader.setPreferredSize(new Dimension(630,80));
        // JM END
        
        lblSponsorCode = new JLabel();
        lblSponsorCode.setFont(fontFactory.getLabelFont());
        txtSponsorCode = new CoeusTextField();
        txtSponsorCode.addKeyListener(new CoeusTextListener());
        txtSponsorCode.setFont(fontFactory.getNormalFont());
        txtSponsorCode.setDocument(new JTextFieldFilter(
                JTextFieldFilter.ALPHA_NUMERIC,6));
        lblName = new JLabel();
        lblName.setFont(fontFactory.getLabelFont());
        txtName = new CoeusTextField();
        txtName.setFont(fontFactory.getNormalFont());
        // JM 7-22-2011 highlighting for required field
        txtName.setRequired(true);
        // END
        //Modified for Coeus 4.3 enhancement: Sponsor name size change - start
        //Changed the max. length of txtName component from 60 to 200
        txtName.setDocument(new LimitedPlainDocument(200));
        //Modified for Coeus 4.3 enhancement: Sponsor name size change - end
        lblType = new JLabel();
        lblType.setFont(fontFactory.getLabelFont());
        lblDuns = new JLabel();
        lblDuns.setFont(fontFactory.getLabelFont());
        txtDuns = new CoeusTextField();
        txtDuns.setDocument(new LimitedPlainDocument(20));
        txtDuns.setFont(fontFactory.getNormalFont());
        lblDuns4 = new JLabel();
        lblDuns4.setFont(fontFactory.getLabelFont());
        txtDuns4 = new CoeusTextField();
        txtDuns4.setDocument(new LimitedPlainDocument(20));
        txtDuns4.setFont(fontFactory.getNormalFont());
        lblDodc = new JLabel();
        lblDodc.setFont(fontFactory.getLabelFont());
        txtDodc = new CoeusTextField();
        txtDodc.setDocument(new LimitedPlainDocument(20));
        txtDodc.setFont(fontFactory.getNormalFont());
        lblCage = new JLabel();
        lblCage.setFont(fontFactory.getLabelFont());
        txtCage = new CoeusTextField();
        txtCage.setDocument(new LimitedPlainDocument(20));
        txtCage.setFont(fontFactory.getNormalFont());
        txtAudit = new CoeusTextField();
        txtAudit.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        txtAudit.setFont(fontFactory.getNormalFont());
        lblAcronym = new JLabel();
        lblAcronym.setFont(fontFactory.getLabelFont());
        lblAudit = new JLabel();
        lblAudit.setFont(fontFactory.getLabelFont());
        scrpnVisualComp = new javax.swing.JScrollPane();
        txtVisualComplianceExpl = new javax.swing.JTextArea();
        txtVisualComplianceExpl.setFont(fontFactory.getNormalFont());
        //txtVisualComplianceExpl.setRequired(true);
        txtVisualComplianceExpl.setBorder(new javax.swing.border.LineBorder(Color.GRAY));
        lblVisualCompliance = new JLabel();
        lblVisualCompliance.setFont(fontFactory.getLabelFont());
        lblVisualComplianceExpl = new JLabel();
        lblVisualComplianceExpl.setFont(fontFactory.getLabelFont());
        txtAcronym = new CoeusTextField();
        txtAcronym.setDocument(new LimitedPlainDocument(10));
        txtAcronym.setFont(fontFactory.getNormalFont());
        lblUnit = new JLabel();
        lblUnit.setFont(fontFactory.getLabelFont());
        txtUnit = new CoeusTextField();
        txtUnit.setFont(fontFactory.getNormalFont());
        cmbType = new CoeusComboBox();
        cmbType.setFont(fontFactory.getNormalFont());
        lblCity = new JLabel();
        lblCity.setFont(fontFactory.getLabelFont());
        txtAddress3 = new CoeusTextField();
        txtAddress3.setDocument(new LimitedPlainDocument(80));
        txtAddress3.setFont(fontFactory.getNormalFont());
        lblState = new JLabel();
        lblState.setFont(fontFactory.getLabelFont());
        txtCity = new CoeusTextField();
        txtCity.setDocument(new LimitedPlainDocument(30));
        txtCity.setFont(fontFactory.getNormalFont());
        lblCounty = new JLabel();
        lblCounty.setFont(fontFactory.getLabelFont());
        txtCounty = new CoeusTextField();
        txtCounty.setDocument(new LimitedPlainDocument(30));
        txtCounty.setFont(fontFactory.getNormalFont());
        lblCountry = new JLabel();
        lblCountry.setFont(fontFactory.getLabelFont());
        txtAddress2 = new CoeusTextField();
        txtAddress2.setDocument(new LimitedPlainDocument(80));
        txtAddress2.setFont(fontFactory.getNormalFont());
        txtAddress1 = new CoeusTextField();
        txtAddress1.setDocument(new LimitedPlainDocument(80));
        txtAddress1.setFont(fontFactory.getNormalFont());
        lblAddress = new JLabel();
        lblAddress.setFont(fontFactory.getLabelFont());
        lblEMail = new JLabel();
        lblEMail.setFont(fontFactory.getLabelFont());
        lblPostalCode = new JLabel();
        lblPostalCode.setFont(fontFactory.getLabelFont());
        lblPhone = new JLabel();
        lblPhone.setFont(fontFactory.getLabelFont());
        lblLastUpdate = new JLabel();
        lblLastUpdate.setFont(fontFactory.getLabelFont());
        lblUpdateUser = new JLabel();
        lblUpdateUser.setFont(fontFactory.getLabelFont());
        lblFax = new JLabel();
        lblFax.setFont(fontFactory.getLabelFont());
        txtEMail = new CoeusTextField();
        txtEMail.setDocument(new LimitedPlainDocument(60));
        txtEMail.setFont(fontFactory.getNormalFont());
        txtLastUpdate = new CoeusTextField();
        txtLastUpdate.setFont(fontFactory.getNormalFont());
        txtPostalCode = new CoeusTextField();
        txtPostalCode.setDocument(new LimitedPlainDocument(15));
        txtPostalCode.setFont(fontFactory.getNormalFont());
        txtPhone = new CoeusTextField();
        txtPhone.setDocument(new LimitedPlainDocument(20));
        txtPhone.setFont(fontFactory.getNormalFont());
        txtFax = new CoeusTextField();
        txtFax.setDocument(new LimitedPlainDocument(20));
        txtFax.setFont(fontFactory.getNormalFont());
        txtUpdateUser = new CoeusTextField();
        txtUpdateUser.setFont(fontFactory.getNormalFont());
        
        //Bug Fix 1149:Start 2
        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
        cmbState = new AutoCompleteCoeusCombo(defaultComboBoxModel);
        cmbState.setAutoCompleteOnFocusLost(false);
        //Bug Fix 1149:End 2
        
        cmbCountry = new CoeusComboBox();
        
        //Commented for bug fix 1149
        //cmbState.setFont(fontFactory.getNormalFont());
        
        cmbCountry.setFont(fontFactory.getNormalFont());
//      pnlMain.setLayout(new BorderLayout(5,5));
        pnlMain.setLayout(new BorderLayout(1,1));
        
        GridBagConstraints gridBagConstraints1;
        
        pnlControl.setLayout(new GridBagLayout());
//        GridBagConstraints gridBagConstraints2;
        
//        btnOK.setText("OK");
//        btnOK.setMnemonic('O');
////        btnOK.setNextFocusableComponent(btnCancel);
//        btnOK.addActionListener(this);
//        gridBagConstraints2 = new GridBagConstraints();
//        gridBagConstraints2.gridx = 0;
//        gridBagConstraints2.gridy = 0;
//        gridBagConstraints2.ipadx = 20;
//        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 10, 0);
//        pnlControl.add(btnOK, gridBagConstraints2);
//
//        btnCancel.setText("Cancel");
//        btnCancel.setMnemonic('C');
//        btnCancel.addActionListener(this);
////        btnCancel.setNextFocusableComponent(txtSponsorCode);
//        gridBagConstraints2 = new GridBagConstraints();
//        gridBagConstraints2.gridx = 0;
//        gridBagConstraints2.gridy = 1;
//        pnlControl.add(btnCancel, gridBagConstraints2);
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.ipadx = 2;
        gridBagConstraints1.ipady = 30;
        gridBagConstraints1.insets = new Insets(0, 7, 7, 1);
        gridBagConstraints1.anchor = GridBagConstraints.NORTH;
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlControls.add(pnlControl);
        pnlMain.add(pnlControls,BorderLayout.EAST);
        
        pnlSponsorHeader.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints3;
        pnlSponsorHeader.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
        lblSponsorCode.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSponsorCode.setText("Sponsor Code: ");
        JPanel pnlNewGrid = new JPanel();
        pnlNewGrid.setLayout(new GridBagLayout());         
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.ipadx = 32;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
//        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
//        pnlSponsorHeader.add(lblSponsorCode, gridBagConstraints3);
        gridBagConstraints3.insets = new Insets(0, 47, 0, 0);
        pnlNewGrid.add(lblSponsorCode, gridBagConstraints3);
        
//        txtSponsorCode.setNextFocusableComponent(txtAcronym);                    
        
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 0;
//        gridBagConstraints3.ipadx = 60;
        gridBagConstraints3.ipadx = 50;
        gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST; // JM 6-5-2015 changed from WEST
//        pnlSponsorHeader.add(txtSponsorCode, gridBagConstraints3);
        pnlNewGrid.add(txtSponsorCode, gridBagConstraints3);
        
        lblName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblName.setText("Name: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 48;
//        gridBagConstraints3.anchor = GridBagConstraints.EAST;
//        gridBagConstraints3.insets = new Insets(0, 0, 0, 4);
        //Case#4254 - End
        pnlSponsorHeader.add(lblName, gridBagConstraints3);
        
//        txtName.setNextFocusableComponent(cmbType);
        txtName.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        txtName.setPreferredSize(new Dimension(392,20));
        txtName.setMaximumSize(new Dimension(392,20));
        txtName.setMinimumSize(new Dimension(392,20));
        pnlSponsorHeader.add(txtName, gridBagConstraints3);
        
        lblType.setHorizontalAlignment(SwingConstants.RIGHT);
        lblType.setText("Type: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        //Modified Case#4254 - change "State" Label to "State/ Province"         
//        gridBagConstraints3.ipadx = 52;
//        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //Case#4254 - End
        
        pnlSponsorHeader.add(lblType, gridBagConstraints3);
        
        lblDuns.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDuns.setText("Duns: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 5;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 52;
//        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        //Case#4254 - End
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        pnlSponsorHeader.add(lblDuns, gridBagConstraints3);
        
//        txtDuns.setNextFocusableComponent(txtDuns4);
        txtDuns.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints(); 
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        txtDuns.setPreferredSize(new Dimension(145,20));
        txtDuns.setMaximumSize(new Dimension(145,20));
        txtDuns.setMinimumSize(new Dimension(145,20));
        pnlSponsorHeader.add(txtDuns, gridBagConstraints3);
        
        lblDuns4.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDuns4.setText("Duns+4:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.ipadx = 28;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblDuns4, gridBagConstraints3);
        
//        txtDuns4.setNextFocusableComponent(txtDodc);
        txtDuns4.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 138;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        pnlSponsorHeader.add(txtDuns4, gridBagConstraints3);
        
        lblDodc.setHorizontalAlignment(SwingConstants.RIGHT);
//JM        lblDodc.setText("Dodc: ");
        lblDodc.setText("GL Fund Source: "); //JM 5-25-2011 updated text per 4.4.2
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 6;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 52;
//        gridBagConstraints3.anchor = GridBagConstraints.WEST;
//        gridBagConstraints3.insets = new Insets(0, 1, 0, 0);
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //Case#4254 - End
        pnlSponsorHeader.add(lblDodc, gridBagConstraints3);
        
//        txtDodc.setNextFocusableComponent(txtCage);
        txtDodc.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        txtDodc.setPreferredSize(new Dimension(145,20));
        txtDodc.setMaximumSize(new Dimension(145,20));
        txtDodc.setMinimumSize(new Dimension(145,20));
        pnlSponsorHeader.add(txtDodc, gridBagConstraints3);
        
        lblCage.setHorizontalAlignment(SwingConstants.RIGHT);
//JM        lblCage.setText("Cage:");
        lblCage.setText("GL Agency:"); //JM 5-25-2011 updated text per 4.4.2
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.ipadx = 42;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblCage, gridBagConstraints3);
        
//        txtCage.setNextFocusableComponent(txtAddress1);
        txtCage.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 138;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        pnlSponsorHeader.add(txtCage, gridBagConstraints3);
        
//        txtAudit.setNextFocusableComponent(txtDuns);
        txtAudit.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5; 
        gridBagConstraints3.gridy = 2;
//        gridBagConstraints3.ipadx = 84;
        gridBagConstraints3.ipadx = 138; 
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        pnlSponsorHeader.add(txtAudit, gridBagConstraints3);
        
        lblAcronym.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAcronym.setText("Acronym: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.ipadx = 1;      
//        gridBagConstraints3.anchor = GridBagConstraints.EAST;
//        pnlSponsorHeader.add(lblAcronym, gridBagConstraints3);
        gridBagConstraints3.insets = new Insets(0,6,0,0);
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        pnlNewGrid.add(lblAcronym, gridBagConstraints3);
        
        lblAudit.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAudit.setText("Audit Report Sent For Fy:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 2;
//        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 2;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblAudit, gridBagConstraints3);
        
        lblVisualCompliance.setHorizontalAlignment(SwingConstants.RIGHT);
        lblVisualCompliance.setText("Visual Compliance Checked?");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.ipadx = 2;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblVisualCompliance, gridBagConstraints3);
        
        
        ButtonGroup buttonGroupVisualCompliance  = new ButtonGroup ();     
        visualComplianceYes = new JRadioButton("Yes");
        visualComplianceYes.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                if (spBean!=null) {                    
                    if (ie.getStateChange()==1) {
                        dataChanged = true;
                    }else {
                        dataChanged = false;
                    }
                }
            }
        });        
	visualComplianceYes.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        buttonGroupVisualCompliance.add(visualComplianceYes);
        pnlSponsorHeader.add(visualComplianceYes, gridBagConstraints3);
        
        visualComplianceNo = new JRadioButton("No");
        visualComplianceNo.addItemListener(new ItemListener() {
             public void itemStateChanged(ItemEvent ie){
                if (spBean!=null) {                    
                    if (ie.getStateChange()==1) {
                        dataChanged = true;
                    }else {
                        dataChanged = false;
                    }
                }
            }
        });
        visualComplianceNo.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        buttonGroupVisualCompliance.add(visualComplianceNo);
        pnlSponsorHeader.add(visualComplianceNo, gridBagConstraints3); 
        //visualComplianceNo.setSelected(true);
        
        lblVisualComplianceExpl.setHorizontalAlignment(SwingConstants.RIGHT);
        lblVisualComplianceExpl.setText("Explanation: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.ipadx = 1;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblVisualComplianceExpl, gridBagConstraints3);
        
        
                
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.ipadx = 1;
        gridBagConstraints3.insets = new Insets(4, 0, 0, 0);
        gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST; 
        txtVisualComplianceExpl.setFont(CoeusFontFactory.getNormalFont());
        pnlSponsorHeader.add(txtVisualComplianceExpl, gridBagConstraints3); 
        
        scrpnVisualComp.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrpnVisualComp.setMinimumSize(new java.awt.Dimension(100, 50));
        scrpnVisualComp.setPreferredSize(new java.awt.Dimension(100, 50));
        scrpnVisualComp.setEnabled(true);
        txtVisualComplianceExpl.setOpaque(true);
        scrpnVisualComp.setViewportView(txtVisualComplianceExpl);
        
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSponsorHeader.add(scrpnVisualComp, gridBagConstraints3); 

//        txtAcronym.setNextFocusableComponent(txtName);
        txtAcronym.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 0;
//        gridBagConstraints3.ipadx = 90;
        gridBagConstraints3.ipadx = 70;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 4);
        //gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        pnlNewGrid.add(txtAcronym, gridBagConstraints3);
        
        lblUnit.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUnit.setText("Unit: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.ipadx = 1;
        //gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        pnlNewGrid.add(lblUnit, gridBagConstraints3);
        
        txtUnit.addKeyListener(new CoeusTextListener());
        txtUnit.setEditable(false);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.ipadx = 60;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        pnlNewGrid.add(txtUnit, gridBagConstraints3);
        
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        //Adding Status radioButtons as Active and Inactive
        ButtonGroup buttonGroup  = new ButtonGroup ();
        GridBagConstraints statusGridBagConstraints;       
        radioActiveStatus = new JRadioButton("Active");
        radioActiveStatus.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                if (spBean!=null) {                    
                    if (ie.getStateChange()==1) {
                        dataChanged = true;
                    }else {
                        dataChanged = false;
                    }
                }
            }
        });
        
        statusGridBagConstraints = new GridBagConstraints();
        statusGridBagConstraints.gridx = 6;
        statusGridBagConstraints.gridy = 0;
        statusGridBagConstraints.insets = new Insets(0,14,0,0);
        statusGridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;   
        buttonGroup.add(radioActiveStatus);
        pnlNewGrid.add(radioActiveStatus, statusGridBagConstraints);
        radioActiveStatus.setSelected(true);
        radioInActiveStatus = new JRadioButton("Inactive");
        radioInActiveStatus.addItemListener(new ItemListener() {
             public void itemStateChanged(ItemEvent ie){
                if (spBean!=null) {                    
                    if (ie.getStateChange()==1) {
                        dataChanged = true;
                    }else {
                        dataChanged = false;
                    }
                }
            }
        });
        buttonGroup.add(radioInActiveStatus);  
        statusGridBagConstraints = new GridBagConstraints();
        statusGridBagConstraints.gridx = 7;
        statusGridBagConstraints.gridy = 0;
        statusGridBagConstraints.insets = new Insets(0, 10, 0, 0);
        statusGridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlNewGrid.add(radioInActiveStatus, statusGridBagConstraints);   
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        
//        cmbType.setNextFocusableComponent(txtAudit);
        cmbType.setMaximumSize(new java.awt.Dimension(44, 20));
        cmbType.setMinimumSize(new java.awt.Dimension(42, 20));
        cmbType.setPreferredSize(new java.awt.Dimension(42,20));
        cmbType.addKeyListener(new CoeusTextListener());
        cmbType.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                dataChanged = true;
            }
        });
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 116;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        pnlSponsorHeader.add(cmbType, gridBagConstraints3);
        
        lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCity.setText("City: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 10;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 57;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //Case#4254 - End
        pnlSponsorHeader.add(lblCity, gridBagConstraints3);
        
//        txtAddress3.setNextFocusableComponent(txtCity);
        txtAddress3.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 9;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        txtAddress3.setPreferredSize(new Dimension(392,20));
        txtAddress3.setMaximumSize(new Dimension(392,20));
        txtAddress3.setMinimumSize(new Dimension(392,20));
        pnlSponsorHeader.add(txtAddress3, gridBagConstraints3);
        
        lblState.setHorizontalAlignment(SwingConstants.RIGHT);
        lblState.setText("State / Province Name: "); // JM 2-21-2013 added space after State
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 11;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 50;
        //Case#4254 - End
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        
        pnlSponsorHeader.add(lblState, gridBagConstraints3);
        
//        txtCity.setNextFocusableComponent(txtCounty);
        txtCity.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 10;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        txtCity.setPreferredSize(new Dimension(145,20));
        txtCity.setMaximumSize(new Dimension(145,20));
        txtCity.setMinimumSize(new Dimension(145,20));
        pnlSponsorHeader.add(txtCity, gridBagConstraints3);
        
        lblCounty.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCounty.setText("County:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 10;
        gridBagConstraints3.ipadx = 40;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblCounty, gridBagConstraints3);
        
//        txtCounty.setNextFocusableComponent(cmbState);
        txtCounty.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 10;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 138;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(txtCounty, gridBagConstraints3);
        
        lblCountry.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCountry.setText("Country: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 12;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 36;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //Case#4254 - End
        pnlSponsorHeader.add(lblCountry, gridBagConstraints3);
        
//        txtAddress2.setNextFocusableComponent(txtAddress3);
        txtAddress2.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 8;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        txtAddress2.setPreferredSize(new Dimension(392,20));
        txtAddress2.setMaximumSize(new Dimension(392,20));
        txtAddress2.setMinimumSize(new Dimension(392,20));
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        pnlSponsorHeader.add(txtAddress2, gridBagConstraints3);
        
//        txtAddress1.setNextFocusableComponent(txtAddress2);
        txtAddress1.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 7;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        txtAddress1.setPreferredSize(new Dimension(392,20));
        txtAddress1.setMaximumSize(new Dimension(392,20));
        txtAddress1.setMinimumSize(new Dimension(392,20));
        pnlSponsorHeader.add(txtAddress1, gridBagConstraints3);
        
        lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAddress.setText("Address: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 7;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 30;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        pnlSponsorHeader.add(lblAddress, gridBagConstraints3);
        //Case#4254 - End
        lblEMail.setHorizontalAlignment(SwingConstants.RIGHT);
        lblEMail.setText("Web / E-Mail: "); // JM 2-21-2013 changed from just E-Mail
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 13;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 46;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //Case#4254 - End

        pnlSponsorHeader.add(lblEMail, gridBagConstraints3);
        
        lblPostalCode.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPostalCode.setText("Postal Code:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.ipadx = 4;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblPostalCode, gridBagConstraints3);
        
        lblPhone.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPhone.setText("Phone:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 12;
        gridBagConstraints3.ipadx = 40;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblPhone, gridBagConstraints3);
        
        lblLastUpdate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLastUpdate.setText("Last Update: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 14;
        //Modified Case#4254 - change "State" Label to "State/ Province" 
//        gridBagConstraints3.ipadx = 14;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        //Case#4254 - End
        pnlSponsorHeader.add(lblLastUpdate, gridBagConstraints3);
        
        lblUpdateUser.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUpdateUser.setText("Update User:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 14;
        gridBagConstraints3.ipadx = 1;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblUpdateUser, gridBagConstraints3);
        
        lblFax.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFax.setText("Fax:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 13;
        gridBagConstraints3.ipadx = 46;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(lblFax, gridBagConstraints3);
        
//        txtEMail.setNextFocusableComponent(txtFax);
        txtEMail.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 13;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        txtEMail.setPreferredSize(new Dimension(145,20));
        txtEMail.setMaximumSize(new Dimension(145,20));
        txtEMail.setMinimumSize(new Dimension(145,20));
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        pnlSponsorHeader.add(txtEMail, gridBagConstraints3);
        
        txtLastUpdate.setEditable(false);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 14;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        txtLastUpdate.setPreferredSize(new Dimension(145,20));
        txtLastUpdate.setMaximumSize(new Dimension(145,20));
        txtLastUpdate.setMinimumSize(new Dimension(145,20));
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4,0,0,0);
        
        pnlSponsorHeader.add(txtLastUpdate, gridBagConstraints3);
        
//        txtPostalCode.setNextFocusableComponent(cmbCountry);
        txtPostalCode.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 138;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4, 1, 0, 0);
        
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(txtPostalCode, gridBagConstraints3);
        
//        txtPhone.setNextFocusableComponent(txtEMail);
        txtPhone.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 12;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 138;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4, 1, 0, 0);
        
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(txtPhone, gridBagConstraints3);
        
//        txtFax.setNextFocusableComponent(btnOK);
        txtFax.addKeyListener(new CoeusTextListener());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 13;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 138;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4, 1, 0, 0);
        
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        pnlSponsorHeader.add(txtFax, gridBagConstraints3);
        
        txtUpdateUser.addKeyListener(new CoeusTextListener());
        txtUpdateUser.setEditable(false);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 14;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 138;
        gridBagConstraints3.anchor = GridBagConstraints.EAST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4, 1, 0, 0);
        
        pnlSponsorHeader.add(txtUpdateUser, gridBagConstraints3);
        
        //Commented for Case#4248 -  State Provinces changes         
//        cmbState.setEditable(true);
//        cmbState.setShowCode(true);
        //Case#4248 - End
        cmbState.setMaximumSize(new java.awt.Dimension(135, 20));
        cmbState.setMinimumSize(new java.awt.Dimension(128, 20));
        cmbState.setPreferredSize(new java.awt.Dimension(128,20));
        //Commmented for Case#4248
        //Bug Fix 1149:Start 3
        //Commented for Case#4248 -  State Provinces changes - Start    
//        final JTextField txtState =(JTextField) cmbState.getEditor().getEditorComponent();
//        txtState.addKeyListener(new CoeusTextListener());
//        
//        txtState.addFocusListener( new FocusAdapter(){
//            public void focusGained( FocusEvent focusEvt ){
//                if( !focusEvt.isTemporary()){
//                    txtState.selectAll();
//                }
//            }
//            public void focusLost( FocusEvent focusEvt ){
//                if( !focusEvt.isTemporary()){
//                    String txtEntered = cmbState.getEditor().getItem().toString();
//                    checkForCode(txtEntered);
//                }
//            }
//        });
        //Case#4248 - End
        
        //Bug Fix 1149:End 3
        
        //BUG FIX -- Bug Id:1138 Start
        
//        ((JTextField)cmbState.getEditor().getEditorComponent()).setNextFocusableComponent(txtPostalCode);
        
        //BUG FIX -- End
        //cmbState.setNextFocusableComponent(txtPostalCode);
        txtPostalCode.addFocusListener( new FocusAdapter(){
            public void focusGained( FocusEvent focusEvt ){
                if( !focusEvt.isTemporary()&& cmbState.isPopupVisible()){
                    cmbState.hidePopup();
                }
            }
        });
        
        cmbState.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                //Modified for Case#4248 -  State Provinces changes - Start
//                selectState=
//                        ((JTextField)cmbState.getEditor().getEditorComponent()).getText();
                selectState = ((ComboBoxBean)cmbState.getSelectedItem()) == null ? CoeusGuiConstants.EMPTY_STRING : ((ComboBoxBean)cmbState.getSelectedItem()).getCode();
                //Case#4248 - End
                if (spBean!=null) {
                    if ( selectState.equalsIgnoreCase(spBean.getState()) ) {
                        dataChanged = false;
                    }else {
                        dataChanged = true;
                    }
                }
            }
        });
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 30;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4, 0, 0, 0);
        
        pnlSponsorHeader.add(cmbState, gridBagConstraints3);
        
//        cmbCountry.setNextFocusableComponent(txtPhone);
        cmbCountry.setMaximumSize(new java.awt.Dimension(61, 20));
        cmbCountry.setMinimumSize(new java.awt.Dimension(55, 20));
        cmbCountry.setPreferredSize(new java.awt.Dimension(55,20));
        cmbCountry.addKeyListener(new CoeusTextListener());
        //Added for Case#4248 -  State Provinces changes 
        hmComboStateWithCountry = fetchStateWithCountry();
        //Case#4248 - End
        
        /* check for the country USA */
        cmbCountry.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                //Modified by shiji for fixing bug id : 1843 : step 1 - start
                ComboBoxBean cmbBeanCountry = new ComboBoxBean("","");
                cmbBeanCountry = (
                        ComboBoxBean)cmbCountry.getSelectedItem();
                //bug id : 1843 : step 1 - end
                //Modified for Case#4248 -  State Provinces changes 
//                if (cmbBeanCountry.getCode().trim().equalsIgnoreCase("USA")){
//                    if(functionType != 'D'){
//                        cmbState.setShowCode(true);
//                    }
//                    
//                    setStateInfo();
//                    dataChanged = true;
//                }else {
//                    
//                    cmbState.setShowCode(false);
//                    /* if the country is not USA remove all from state combo box */
//                    dataChanged = true;
//                    clearStateInfo();
//                }
                
                clearStateInfo();
                cvStateData.removeAllElements();
                if(cmbBeanCountry != null && !cmbBeanCountry.getCode().equals("")){
                     comboState = (Vector)hmComboStateWithCountry.get(cmbBeanCountry.getCode().trim());
                     int sizeCheck = cvStateData.size();
                     if(comboState != null && comboState.size() > 0){
                         int comboLength = comboState.size();
                         for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
                             ComboBoxBean listBox =(ComboBoxBean)comboState.elementAt(comboIndex);
                             cmbState.addItem(listBox);
                         }
                         cvStateData.addAll(comboState);
                         dataChanged = true;
                     }
                }
                //Case#4248 - End
            }
        });
        
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 12;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.ipadx = 103;
        
        //Added the insets
        gridBagConstraints3.insets = new Insets(4, 0, 0, 0);
        
        pnlSponsorHeader. add(cmbCountry, gridBagConstraints3);
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST; // JM 6-5-2015 fix for Java 1.8 smushing

        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        gridBagConstraints1.gridwidth = 6;
        pnlSponsorHeader.add(pnlNewGrid,gridBagConstraints1);  
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.anchor = GridBagConstraints.NORTHEAST;
        pnlMain.add(pnlSponsorHeader,BorderLayout.CENTER); 
        
        return pnlMain;
    }
    
    /**
     * This method loads the combo box like sponsor Type,States and Countries
     * for first time when the user wants to insert data into the database.
     */
    private void loadComboBox(){
        requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setRequestedForm("Sponsor Details");
        ResponderBean response = sendToServer("/spMntServlet",requester);
        Vector vecComboObjects = (Vector)response.getDataObjects();
        Vector comboList = (Vector)vecComboObjects.elementAt(0);
        int comboLength = comboList.size();
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
            cmbType.addItem(listBox);
        }
        
        //Commented for Case#4248 -  State Provinces changes - Start
//        comboList = (Vector)vecComboObjects.elementAt(1);
//        comboLength = comboList.size();
        
        //Bug Fix 1149:Start 4
//        cvStateData.addAll(comboList);
        //Bug Fix 1149:End 4
        
//        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
//            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
//            cmbState.addItem(listBox);
//        }
        //Case#4248 - End
        
        //Modified by shiji for fixing bug id : 1843 : step 2 - start
        ComboBoxBean comboBean = new ComboBoxBean(" "," ");
        comboList = (Vector)vecComboObjects.elementAt(2);
        comboList.add(0, comboBean);
        //bug id : 1843 : step 2 - end
        comboLength = comboList.size();
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
            cmbCountry.addItem(listBox);
        }
        /* Default the country USA should be selected in the country combo box */
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            ComboBoxBean cmbBox = (ComboBoxBean)comboList.elementAt(comboIndex);
            if(cmbBox.getCode().equals("USA")){
                cmbCountry.setSelectedItem(cmbBox.getCode());
            }
        }
        /* parameter flag to indicate whether we need to generate sponsor code */
        generateSponsorCode = ((String)vecComboObjects.elementAt(3)).equalsIgnoreCase("TRUE");
        //System.out.println("*** auto generate sponsor code return value " + generateSponsorCode);
    }
    
    /**
     * This method is used to check dupliaction of sponsor code whether the user
     * entered sponsor code is duplicate or not the method will return true
     * if the sponsor code is already present else false.
     *
     * @param sponsorCode String
     * @return boolean true if sponsor code duplicated
     */
    private boolean getSposnorCount(String sponsorCode) {
        requester = new RequesterBean();
        /*
         * function type "R" is set to check for duplicate of sponsorcode
         * in the servlet
         */
        requester.setFunctionType('R');
        requester.setRequestedForm("Sponsor Maintenance");
        requester.setDataObject(sponsorCode);
        ResponderBean response = sendToServer("/spMntServlet",requester);
        return response.isSuccessfulResponse();
    }
    
    /**
     * This method returns the bean with rolodex details
     *
     * @return RolodexDetailsBean
     */
    private RolodexDetailsBean getRolodexDetails(){
        rldxBean = new RolodexDetailsBean();
        rldxBean.setRolodexId(rolodexID);
        rldxBean.setLastUpdateUser(txtUpdateUser.getText());
        rldxBean.setLastName(null);
        rldxBean.setMiddleName(null);
        rldxBean.setFirstName(null);
        rldxBean.setSuffix(null);
        rldxBean.setPrefix(null);
        rldxBean.setOwnedByUnit(txtUnit.getText());
        rldxBean.setSponsorCode(txtSponsorCode.getText());
        rldxBean.setOrganization(txtName.getText());
        rldxBean.setAddress1(txtAddress1.getText());
        rldxBean.setAddress2(txtAddress2.getText());
        rldxBean.setAddress3(txtAddress3.getText());
        rldxBean.setCity(txtCity.getText());
        rldxBean.setCounty(txtCounty.getText());
        rldxBean.setDeleteFlag("");
        rldxBean.setSponsorAddressFlag("Y");
        if ( ((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().equals("USA") ) {
            if (cmbState.getSelectedItem() != null)
                rldxBean.setState(
                        ((ComboBoxBean)cmbState.getSelectedItem()).getCode());
            else
                rldxBean.setState("");
        }else
            rldxBean.setState(selectState);
        
        rldxBean.setCountry(
                ((ComboBoxBean)cmbCountry.getSelectedItem()).getCode());
        rldxBean.setPostalCode(txtPostalCode.getText());
        rldxBean.setPhone(txtPhone.getText());
        rldxBean.setFax(txtFax.getText());
        rldxBean.setEMail(txtEMail.getText());
        rldxBean.setComments("Sponsor Base Address");
        return rldxBean;
    }
    
    /**
     * This method returns the bean with sponsor details
     *
     * @return SponsorMaintenanceFormBean
     */
    private SponsorMaintenanceFormBean getSponsorDetails(){
        spBean = new SponsorMaintenanceFormBean();
        spBean.setSponsorCode(txtSponsorCode.getText());
        spBean.setAcronym(txtAcronym.getText());
        spBean.setOwnedBy(txtUnit.getText());
        spBean.setName(txtName.getText());
        spBean.setType(((ComboBoxBean)cmbType.getSelectedItem()).getCode());
        spBean.setTypeDescription(((ComboBoxBean)cmbType.getSelectedItem(
                )).getDescription());
        spBean.setAuditReport(txtAudit.getText());
        spBean.setVisualComplianceExpl(txtVisualComplianceExpl.getText());
        spBean.setDuns(txtDuns.getText());
        spBean.setDuns4(txtDuns4.getText());
        spBean.setCage(txtCage.getText());
        spBean.setDodc(txtDodc.getText());
        //Modified for Case#4248 -  State Provinces changes - Start
//        if ( ((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().equals("USA") ) {
        if (cmbCountry.getSelectedItem() != null) {//Case#4248 - End
            if (cmbState.getSelectedItem() != null) {
                spBean.setState(
                        ((ComboBoxBean)cmbState.getSelectedItem()).getCode());
                spBean.setStateDescription(((ComboBoxBean)cmbState.getSelectedItem(
                        )).getDescription());
            } else{
                spBean.setState("");
                spBean.setStateDescription("");
            }
        }else{
            spBean.setState(selectState);
            spBean.setStateDescription("");
        }
        spBean.setCountry(
                ((ComboBoxBean)cmbCountry.getSelectedItem()).getCode());
        spBean.setCountryName(((ComboBoxBean)cmbCountry.getSelectedItem(
                )).getDescription());
        spBean.setRolodexID(rolodexID);
        spBean.setCountry(((ComboBoxBean)cmbCountry.getSelectedItem(
                )).getCode());
        spBean.setPostalCode(txtPostalCode.getText());
        spBean.setLastUpdateUser(txtUpdateUser.getText());
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        if(radioActiveStatus.isSelected())
            spBean.setStatus(ACTIVE_STATUS);
        else if(radioInActiveStatus.isSelected())
            spBean.setStatus(INACTIVE_STATUS);
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        if(visualComplianceYes.isSelected())
            spBean.setVisualCompliance(VISUAL_COMPLIANCE_YES);
        else if(visualComplianceNo.isSelected())
            spBean.setVisualCompliance(VISUAL_COMPLIANCE_NO);
        return spBean;
    }
    
    private void performSponsorContactSaveAction() throws CoeusException{
        try{
            sponsorDetailForm.contactListEditor.stopCellEditing();
            CoeusVector cvDeletedData = sponsorDetailForm.getCvDeletedData();
            CoeusVector cvContactsData = sponsorDetailForm.getCvContactsData();
            if  (functionType == 'I' && cvContactsData != null)  {
                for(int i=0;i<cvContactsData.size();i++){
                    SponsorContactBean bean = (SponsorContactBean)cvContactsData.get(i);
                    bean.setSponsorCode(txtSponsorCode.getText().trim());
                }
                if(cvDeletedData != null){
                    cvDeletedData.removeAllElements();
                }
            }
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setRequestedForm(TITLE_SPONSOR);
            requesterBean.setFunctionType('B');
            HashMap hmSponsorContactData = new HashMap();
            hmSponsorContactData.put("DELETE_DATA", cvDeletedData);
            hmSponsorContactData.put("UPDATE_DATA", cvContactsData);
            requesterBean.setDataObject(hmSponsorContactData);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CONNECT_TO, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }
            }
        }catch(CoeusException exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    /**
     * This method invoked when ever the user press the OK button in the Form
     * the method sends sponsor bean and rolodex bean in the vector objects to
     * the servlet,the AcType is set on the user wants to Add or update the data.
     */
    public void btnOKActionPerformed(){
        
        Vector vecBeans = new Vector();
        RequesterBean requester = new RequesterBean();
        spBean = getSponsorDetails();
        rldxBean = getRolodexDetails();
        /* function type "I" for insert data */
        if  (functionType == 'I')  {
            spBean.setAcType("I");
            rldxBean.setAcType("I");
            spBean.setLastUpdateTime(null);
            rldxBean.setLastUpdateTime(null);
            
        }else {
            /* function type "U" for update data */
            spBean.setAcType("U");
            rldxBean.setAcType("U");
            spBean.setLastUpdateTime(sponsorTime);
            rldxBean.setLastUpdateTime(rolodexTime);
            spBean.setLastUpdateUser(sponsorUpdateUser);
            rldxBean.setLastUpdateUser(rolodexUpdateUser);
            spBean.setRefId(referenceId);
        }
        vecBeans.add(spBean);
        vecBeans.add(rldxBean);
        requester.setFunctionType(functionType);
        requester.setRequestedForm("Sponsor Maintenance");
        requester.setDataObjects(vecBeans);
        /* sending the requester to the sponsor servlet */
        ResponderBean response = sendToServer("/spMntServlet",requester);
        if ((response != null )|| (response.isSuccessfulResponse())) {
            //added bu Jinu 01-02-2005
            try{
                if(sponsorDetailForm != null && sponsorDetailForm.modified){
                    if( (sponsorDetailForm.getCvContactsData() != null &&
                            sponsorDetailForm.getCvContactsData().size()>0)  ||
                            (sponsorDetailForm.getCvDeletedData() != null &&
                            sponsorDetailForm.getCvDeletedData().size()>0) ){
                        performSponsorContactSaveAction();
                    }
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
            //end Jinu
            
            //modified by ravi
            //setDataToUpdate();
//            System.out.println("notifying with func Type:"+functionType);
            
            /* get return data object - should exist for generate sponsor code */
            Vector vecSpBean = (Vector)response.getDataObjects();
//            System.out.println("vector size *** " + vecSpBean.size());
            if(vecSpBean != null && vecSpBean.size() > 0){
                this.spBean = (SponsorMaintenanceFormBean)vecSpBean.elementAt(0);
            }
            //System.out.println("sponsor code - client side *** " + spBean.getSponsorCode());
            
            observable.setFunctionType(functionType);
            observable.notifyObservers(spBean);
            //modified by ravi - end
            dlgWindow.dispose();
        }else {
            CoeusOptionPane.showErrorDialog(response.getMessage());
            releaseUpdateLock();
            if (response.isCloseRequired()) {
                dlgWindow.dispose();
            }else {
                return;
            }
        }
        
    }
    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /**
     * This method is invoked when the user clicks on display or update the data
     * the sponsorCode is sent for which the data should be displayed or update,
     * once the data is retrieved the data is set to the components like
     * Textfield and ComboBox
     *
     * @param spBean as SponsorMaintenanceFormBean and rldxBean as RolodexDetailsBean
     */
    private void displaySponsorDetails(SponsorMaintenanceFormBean spBean,
            RolodexDetailsBean rldxBean){
        
        loadDataToForm(spBean,rldxBean);
    }
    
    /**
     * This method is invoked when the user clicks on display or update the data
     * the sponsorCode is sent for which the data should be displayed or update,
     * once the data is retrieved the data is set to the components like
     * Textfield and ComboBox
     *
     * @param sponsorCode String
     */
    private void displaySponsorDetails(String sponsorCode){
        requester = new RequesterBean();
        /* function type "D" for display data */
        requester.setFunctionType('D');
        requester.setRequestedForm("Sponsor Details");
        requester.setDataObject(sponsorCode);
        ResponderBean response = sendToServer("/spMntServlet",requester);
        Vector vecBeans = new Vector();
        if (response != null) {
            vecBeans = (Vector)response.getDataObjects();
        }
        spBean =
                (SponsorMaintenanceFormBean)vecBeans.elementAt(0);
        RolodexDetailsBean rldxBean = (RolodexDetailsBean)vecBeans.elementAt(1);
        
        loadDataToForm(spBean,rldxBean);
        
    }
    
    /**
     * This method is invoked when the data should be displayed in the form
     * parameter SponsorMaintenanceFormBean and RolodexDetailsBean
     *
     * @param spBean as SponsorMaintenanceFormBean and rldxBean as RolodexDetailsBean
     */
    private void loadDataToForm(SponsorMaintenanceFormBean spBean,
            RolodexDetailsBean rldxBean){
        
        rolodexID = spBean.getRolodexID();
        sponsorTime = spBean.getLastUpdateTime();
        rolodexTime = rldxBean.getLastUpdateTime();
        sponsorUpdateUser = spBean.getLastUpdateUser();
        rolodexUpdateUser = rldxBean.getLastUpdateUser();
        referenceId = spBean.getRefId();
        
        txtSponsorCode.setText(spBean.getSponsorCode() == null ? ""
                : spBean.getSponsorCode());
        txtAcronym.setText(spBean.getAcronym()== null ? "" : spBean.getAcronym());
        txtUnit.setText(spBean.getOwnedBy()==null ? "" : spBean.getOwnedBy());
        txtName.setText(spBean.getName()== null ? "" : spBean.getName());
        Vector comboList =(spBean.getTypes() == null ? new Vector()
        : spBean.getTypes());
        int comboLength = comboList.size();
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
            cmbType.addItem(listBox);
        }
        cmbType.setSelectedItem(spBean.getType() == null ? "" : spBean.getType());
        txtAudit.setText(spBean.getAuditReport()== null ? ""
                : spBean.getAuditReport());
        txtVisualComplianceExpl.setText((String) (spBean.getVisualComplianceExpl()== null ? ""
                : spBean.getVisualComplianceExpl()));
        txtDuns.setText(spBean.getDuns()== null ? "" : spBean.getDuns());
        txtDuns4.setText(spBean.getDuns4()== null ? "" : spBean.getDuns4());
        txtCage.setText(spBean.getCage()== null ? "" : spBean.getCage());
        txtDodc.setText(spBean.getDodc()== null ? "" : spBean.getDodc());
        comboList =(spBean.getCountries() == null ? new Vector()
        : spBean.getCountries());
        //Modified by shiji for fixing bug id : 1843 : step 3 - start
        ComboBoxBean comboBean = new ComboBoxBean(" "," ");
        comboList.add(0, comboBean);
        //bug id : 1843 : step 3 - end
        comboLength = comboList.size();
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
            cmbCountry.addItem(listBox);
        }
        cmbCountry.setSelectedItem(spBean.getCountry() == null ? ""
                : spBean.getCountry());
        
        /* if the country is USA load the state combobox else remove items
         * from the state combobox
         */
        //Commented for Case#4248 -  State Provinces changes  - Start      
//        if (spBean.getCountry().equals("USA")){
//            comboList =(spBean.getStates() == null ? new Vector()
//            : spBean.getStates());
//            comboLength = comboList.size();
//            for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
//                ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
//                cmbState.addItem(listBox);
//            }
//            
//            //Bug Fix:1149 Start 7
//            cvStateData.addAll(comboList);
            //Bug Fix:1149 End 7
            
//        }else{
//            cmbState.removeAllItems();
//            ComboBoxBean cmbBean = spBean.getState() != null ?
//                new ComboBoxBean(spBean.getState(),spBean.getState())
//                : new ComboBoxBean("","");
//            cmbState.addItem(cmbBean);
//        }
        //Case#4248 - End
        cmbState.setSelectedItem(spBean.getState() == null ? ""
                : spBean.getState());
        txtPostalCode.setText(spBean.getPostalCode()== null ? ""
                : spBean.getPostalCode());
        String lastUpdateTime = spBean.getLastUpdateTime().toString();
        try {
            txtLastUpdate.setText(CoeusDateFormat.format(lastUpdateTime));
        }catch(Exception e){
            CoeusOptionPane.showInfoDialog(e.getMessage());
        }
//        txtUpdateUser.setText(spBean.getLastUpdateUser());
        /*
         * UserID to UserName Enhancement - Start
         * Added UserUtils class to change userid to username
         */
        txtUpdateUser.setText(UserUtils.getDisplayName(spBean.getLastUpdateUser()));
        // UserId to UserName Enhancement - End
        
        txtAddress1.setText(rldxBean.getAddress1() == null ? ""
                : rldxBean.getAddress1());
        txtAddress2.setText(rldxBean.getAddress2() == null ? ""
                : rldxBean.getAddress2());
        txtAddress3.setText(rldxBean.getAddress3() == null ? ""
                : rldxBean.getAddress3());
        txtCity.setText(rldxBean.getCity() == null ? "" : rldxBean.getCity());
        txtCounty.setText(rldxBean.getCounty() == null ? ""
                : rldxBean.getCounty());
        txtPhone.setText(rldxBean.getPhone() == null ? "" : rldxBean.getPhone());
        txtEMail.setText(rldxBean.getEMail() == null ? "" : rldxBean.getEMail());
        txtFax.setText(rldxBean.getFax() == null ? "" : rldxBean.getFax());
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        if(ACTIVE_STATUS.equalsIgnoreCase(spBean.getStatus().trim())) {
            radioActiveStatus.setSelected(true);            
        } else if(INACTIVE_STATUS.equalsIgnoreCase(spBean.getStatus().trim())) {
            radioInActiveStatus.setSelected(true);
        }
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        try{
        if(VISUAL_COMPLIANCE_YES.equalsIgnoreCase(spBean.getVisualCompliance().trim())) {
            visualComplianceYes.setSelected(true);            
        } else if(VISUAL_COMPLIANCE_NO.equalsIgnoreCase(spBean.getVisualCompliance().trim())) {
            visualComplianceNo.setSelected(true);
        }
        }catch(Exception e){
            System.out.println("Please Check Visual Compliance");
        }
    }
    
    //Added for Case#4248 -  State Provinces changes  - Start
    /*
     * Method to fetch all the state with their country
     * @return hmComboStateWithCountry
     */
    private HashMap fetchStateWithCountry(){
        requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setRequestedForm("Sponsor Details");
        ResponderBean response = sendToServer("/spMntServlet",requester);
        Vector vecComboObjects = (Vector)response.getDataObjects();
        hmComboStateWithCountry = (HashMap)vecComboObjects.elementAt(1);
        return hmComboStateWithCountry;
    }
    //Case#4248 - End
    /**
     * This methods loads the state combobox with state data related to country
     * USA.
     */
    private void setStateInfo(){
        requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setRequestedForm("Sponsor Details");
        ResponderBean response = sendToServer("/spMntServlet",requester);
        Vector vecComboObjects = (Vector)response.getDataObjects();
        comboState = (Vector)vecComboObjects.elementAt(1);
        int comboLength = comboState.size();
        cmbState.removeAllItems();
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            //cmbState.setSelectedItem(comboState.elementAt(0));
            ComboBoxBean listBox =(ComboBoxBean)comboState.elementAt(comboIndex);
            cmbState.addItem(listBox);
        }
        
        //Bug fix:1149 Start 8
        cvStateData.addAll(comboState);
        //Bug Dix:1149 End 8
    }
    
    /**
     * This methods clears the state combobox if country is not USA.
     */
    private void clearStateInfo(){
        cmbState.removeAllItems();
    }
    
    /**
     * This method connection between the servlet and form using response and
     * requester it takes the parameter servlet name and request sent to the servlet
     *
     * @param srvComponentName String
     * @param requester RequesterBean
     * @return ResponderBean
     */
    public ResponderBean sendToServer(String srvComponentName,
            RequesterBean requester) {
        String connectTo =CoeusGuiConstants.CONNECTION_URL+srvComponentName;
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, requester);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        if( dlgWindow != null ){
            dlgWindow.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        }
        comm.send();
        ResponderBean response = comm.getResponse();
        if( dlgWindow != null ){
            dlgWindow.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }
        return response;
    }
    
    /**
     * This method is called for front end validations
     * if the OK or Cancel button is pressed.
     */
    private boolean validateFields()throws Exception{
        boolean process_OK=false;
        boolean duplicateSponsor = false;
        /* check for sponsor duplicate only if we are not generating sponsor code */
        if (functionType == 'I' && !generateSponsorCode){
            duplicateSponsor = getSposnorCount(txtSponsorCode.getText());
        }
        /* check if sponsor code is entered only if we are not generating sponsor code */
        if (txtSponsorCode.getText().trim().length() == 0 && !generateSponsorCode){
            log(coeusMessageResources.parseMessageKey("spnrMntFrm_exceptionCode.1119"));
        }else if (txtSponsorCode.getText().trim().length() < 6 && !generateSponsorCode){
            log(coeusMessageResources.parseMessageKey("spnrMntFrm_exceptionCode.1120"));
        }else if (duplicateSponsor){
            log("Sponsor Code "+txtSponsorCode.getText()+" is duplicate. "+
                    "Please enter a different Code");
        }else if ( txtName.getText().trim().length() <= 0){
            log(coeusMessageResources.parseMessageKey(
                    "protoKeyStPsnlFrm_exceptionCode.1069"));
            //Visual Compliance  
        }else if ( visualComplianceYes.isSelected() == false && visualComplianceNo.isSelected() == false ){
           log(coeusMessageResources.parseMessageKey(
                   "spnrMntFrm_exceptionCode.1126"));    
        }else if ( txtVisualComplianceExpl.getText().trim().length() <= 0){
           log(coeusMessageResources.parseMessageKey(
                   "spnrMntFrm_exceptionCode.1125"));
            //Visual Compliance txt
        }else if (((ComboBoxBean)cmbType.getSelectedItem()).getCode().equals("")){
            log(coeusMessageResources.parseMessageKey(
                    "spnrMntFrm_exceptionCode.1121"));
        }else if ( (((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().equals("USA")) &&
                (((ComboBoxBean)cmbState.getSelectedItem()).getCode().equals("")) ){
            log(coeusMessageResources.parseMessageKey(
                    "roldxMntDetFrm_exceptionCode.1105"));
        }else if ( selectState.trim().length() > 30) {
            log(coeusMessageResources.parseMessageKey(
                    "spnrMntFrm_exceptionCode.1122"));
        }else if ( (((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().equals("USA"))) {
            if ( (((ComboBoxBean)cmbState.getSelectedItem()).getCode().equals("")) )  {
                log(coeusMessageResources.parseMessageKey(
                        "roldxMntDetFrm_exceptionCode.1105"));
                process_OK =false;
            }else {
                boolean exists = false;
                int comboLength = comboState.size();
                String selectedState=
                        ((ComboBoxBean)cmbState.getSelectedItem()).getCode();
                for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
                    ComboBoxBean cmbBox = (
                            ComboBoxBean)comboState.elementAt(comboIndex);
                    if (cmbBox.getCode().toString().trim()
                    .equalsIgnoreCase(selectedState)) {
                        exists = true;
                    }
                }
                if (!exists) {
                    log(coeusMessageResources.parseMessageKey(
                            "spnrMntFrm_exceptionCode.1123"));
                    cmbState.requestFocus();
                    process_OK =false;
                }else {
                    process_OK =true;
                }
            }
        }else{
            process_OK=true;
        }
        return process_OK;
    }
    
    /**
     * This method called on press of OK or Cancel button
     * it validates the user entered data by calling another method validateFields().
     *
     * @param evt ActionEvent
     */
    public void actionPerformed(ActionEvent evt) {
//        boolean saveData=false;
//        if ( evt.getSource()==btnOK ) {
//            try{
//                saveData = validateFields();
//                if (saveData){
//                    btnOKActionPerformed();
//                    dlgWindow.dispose();
//                }
//            }catch(Exception ex){
//                log(ex.getMessage());
//            }
//        }else{
//            performCancelAction();
//        }
    }
    public void performOKAction(){
        boolean saveData=false;
        try{
            saveData = validateFields();
            if (saveData){
                btnOKActionPerformed();
                dlgWindow.dispose();
            }
        }catch(Exception ex){
            log(ex.getMessage());
        }
    }
    public void performCancelAction(){
        boolean saveData=false;
        if (!dataChanged && sponsorDetailForm!=null){
            dataChanged = sponsorDetailForm.modified;
        }
        if (dataChanged){
            int resultConfirm = confirmLog(coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002"));
            try{
                if (resultConfirm == 0) {
                    if(sponsorDetailForm!=null){
                        SponsorContactBean bean = sponsorDetailForm.checkForDuplicate();
                        if(bean != null){
                            int index = sponsorDetailForm.searchIndex(
                                    sponsorDetailForm.getCvContactsData(), bean);
                            if(index != -1){
                                sponsorDetailForm.tbdPnSponsorForm.setSelectedIndex(1);
                                sponsorDetailForm.tblContactList.requestFocus();
                                sponsorDetailForm.tblContactList.setRowSelectionInterval(index, index);
                            }
                            return;
                        }
                    }
                    saveData = validateFields();
                }else if (resultConfirm == 1){
                    releaseUpdateLock();
                    dlgWindow.dispose();
                }else {
                    return ;
                }
            }catch(Exception ex){
                log(ex.getMessage());
            }
        }else{
            releaseUpdateLock();
            dlgWindow.dispose();
        }
        if (saveData){
            btnOKActionPerformed();
            dlgWindow.dispose();
        }
        
    }
    /**
     * This method is called on window closing if the user have changed the data
     * without saving this method is called for front validatio before saving.
     */
    private void validateWindow(){
        boolean saveData=false;
        if (!dataChanged && sponsorDetailForm!=null){
            dataChanged = sponsorDetailForm.modified;
        }
        if (dataChanged){
            int resultConfirm = confirmLog(coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002"));
            try{
                if (resultConfirm == 0) {
                    saveData = validateFields();
                }else if (resultConfirm == 1){
                    releaseUpdateLock();
                    dlgWindow.dispose();
                }else{
                    return;
                }
            }catch(Exception ex){
                log(ex.getMessage());
            }
        }else{
            releaseUpdateLock();
            dlgWindow.dispose();
        }
        if (saveData){
            btnOKActionPerformed();
            dlgWindow.dispose();
        }
    }
    
    /**
     * displays the message,it gives the error message.
     * @param mesg String
     */
    public void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    /**
     * displays the message,it gives the Question message.
     * @param confirmMesg String
     * @return integer zero or one
     */
    public int confirmLog(String confirmMesg){
        int confirm = CoeusOptionPane.showQuestionDialog(confirmMesg,
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
        return confirm;
        
    }
    
    /**
     * This is main method which is called by the listener to load the form
     * for Add,Display and Update.
     *
     * @param inFrame JFrame
     * @param name the title of the Frame String
     * @param isModal boolean
     */
    public void showForm(JFrame inFrame,String name,boolean isModal){
        this.mdiForm = (CoeusAppletMDIForm)inFrame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        /*check for ESC button if the user presses the button the dialog should close*/
        dlgWindow = new CoeusDlgWindow(inFrame,name,isModal){
            protected JRootPane createRootPane() {
                ActionListener actionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        try{
                            validateWindow();
                        }catch(Exception ex){
                            log(ex.getMessage());
                        }
                    }
                };
                JRootPane rootPane = new JRootPane();
                KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
                        0);
                rootPane.registerKeyboardAction(actionListener, stroke,
                        JComponent.WHEN_IN_FOCUSED_WINDOW);
                return rootPane;
            }
        };
        
        sponsorDetailForm = new SponsorDetailForm(sponsorCode, mdiForm, this);
        sponsorDetailForm.setFunctionType(functionType);
        if (functionType == 'D'){
            sponsorDetailForm.setControls(false);
        }
        // JM 6-5-2015 this is the interior panel with the sponsor details
        JPanel dlgPanel  = getSponsorDetail();
        // JM 6-12-2015 commented out as causing Java 1.8 display issues
        //dlgPanel.setMinimumSize(new Dimension(620,360)); 
        //dlgPanel.setPreferredSize(new Dimension(660,360)); 
        // JM END
        
        sponsorDetailForm.scrPnDetails.setViewportView(dlgPanel);
        sponsorDetailForm.setVisible(true);
        dlgWindow.getContentPane().add(sponsorDetailForm);
        dlgWindow.setResizable(false);
        
        //Increased height
        // JM 6-5-2015 this is the pop-up window
        //dlgWindow.setSize(700,388);// 650,355
        dlgWindow.setSize(780,420); // JM 5-22-2015 increase size a bit more for Mac
        
        Dimension dlgSize = dlgWindow.getSize();
        dlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;
        dlgWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgWindow.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                dataChanged = false;
                if (functionType == 'U') {
                    txtAcronym.requestFocus();
                }else if (functionType == 'I'){
                    /* autogenerate sponsor code - check parameter config */
                    if(generateSponsorCode){
                        txtSponsorCode.setEditable(false);
                        txtAcronym.requestFocus();
                        //System.out.println("show form generate sponsor - focus acronym");
                    }else {
                        txtSponsorCode.requestFocus();
                    }
                }
            }
            public void windowClosing(WindowEvent we){
                validateWindow();
            }
        });
        dlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                validateWindow();
            }
        });
        
        setFocusTraversal(SPONSOR_DETAIL_TAB);
        dlgWindow.show();
        
    }
    public void setFocusTraversal(int tab){
        if(functionType == 'D'){
            sponsorDetailForm.setDefaultFocus();
        }else if(tab == SPONSOR_DETAIL_TAB){
            //Bug Fix :1149 Start 5 COMMENTED NEXT FOCUSSABLE COMPONENTS ON TOP FOR THIS BUG FIX
            java.awt.Component[] components = new java.awt.Component[]{ txtAcronym,
            txtName,cmbType,txtAudit,txtVisualComplianceExpl,txtDuns,txtDuns4,txtDodc,txtCage,txtAddress1,
            //Modified for Case#4248 -  State Provinces changes  - Start
//            txtAddress2,txtAddress3,txtCity,txtCounty,cmbState.getEditor().getEditorComponent(),
            txtAddress2,txtAddress3,txtCity,txtCounty,cmbState,//Case#4248 - End
            txtPostalCode,cmbCountry,txtPhone,txtEMail,txtFax,sponsorDetailForm.btnOK,
            sponsorDetailForm.btnCancel};
            if (functionType == 'U') {
                txtAcronym.requestFocus();
            }else if (functionType == 'I'){
                /* autogenerate sponsor code - check parameter config */
                if(generateSponsorCode){
                    txtSponsorCode.setEditable(false);
                    txtAcronym.requestFocus();
                }else {
                    components = new java.awt.Component[]{ txtSponsorCode,txtAcronym,txtName,cmbType,txtAudit, txtVisualComplianceExpl,
                    txtDuns,txtDuns4,txtDodc,txtCage,txtAddress1,txtAddress2,txtAddress3,
                    //Modified for Case#4248 -  State Provinces changes  - Start
//                    txtCity,txtCounty,cmbState.getEditor().getEditorComponent(),
                    txtCity,txtCounty,cmbState,//Case#4248 - End
                    txtPostalCode,cmbCountry,txtPhone,txtEMail,txtFax,sponsorDetailForm.btnOK,
                    sponsorDetailForm.btnCancel};
                    
                    txtSponsorCode.requestFocus();
                }
            }
            
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            sponsorDetailForm.setFocusTraversalPolicy(traversePolicy);
            sponsorDetailForm.setFocusCycleRoot(true);
            //Bug Fix :1149 End 5
        }else if(tab == INSTITUTIONAL_CONTACT_TAB){
            java.awt.Component[] components = new java.awt.Component[]{ sponsorDetailForm.btnAdd,sponsorDetailForm.btnModify,
            sponsorDetailForm.btnDelete,sponsorDetailForm.btnOK,sponsorDetailForm.btnCancel};
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            //            dlgWindow.setFocusTraversalPolicy(traversePolicy);
            //            dlgWindow.setFocusCycleRoot(true);
            sponsorDetailForm.btnAdd.requestFocus();
            sponsorDetailForm.setFocusTraversalPolicy(traversePolicy);
            sponsorDetailForm.setFocusCycleRoot(true);
        }
    }
    
    public void setDefaultFocus(){
        if(functionType == 'D'){
            sponsorDetailForm.btnCancel.requestFocus();
        }
        
        if (functionType == 'U') {
            txtAcronym.requestFocusInWindow();
        }else if (functionType == 'I'){
            txtSponsorCode.requestFocusInWindow();
        }
        
    }
    /**
     * This method is called for different functionType for setting the field
     * controls like enable or disable ,set the background color.
     *
     * @param functionType char
     */
    private void setControls(char functionType){
        if (functionType == 'D'){
            setControlsEnabled(false);
            
        }
    }
    
    /**
     * This method is called for setting the controls of the text and combobox
     * component like enable or disable .
     *
     * @param value boolean ,if true enable else disable
     */
    private void setControlsEnabled(boolean value){
        txtSponsorCode.setEditable(value);
        txtAcronym.setEditable(value);
        txtUnit.setEditable(value);
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        radioActiveStatus.setEnabled(value);
        radioInActiveStatus.setEnabled(value);
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        txtName.setEditable(value);
        cmbType.setEnabled(value);
        txtAudit.setEditable(value);
        txtVisualComplianceExpl.setEditable(value);
        visualComplianceYes.setEnabled(value);
        visualComplianceNo.setEnabled(value);
        txtDuns.setEditable(value);
        txtDuns4.setEditable(value);
        txtCage.setEditable(value);
        txtDodc.setEditable(value);
        txtAddress1.setEditable(value);
        txtAddress2.setEditable(value);
        txtAddress3.setEditable(value);
        txtCity.setEditable(value);
        txtCounty.setEditable(value);
        //Modified for Case#4248 -  State Provinces changes  - Start
//        cmbState.setEditable(value);
        //Case#4248 - End
        cmbState.setEnabled(value);
        cmbState.setForeground(Color.black);
        cmbState.setOpaque(value);
        cmbCountry.setEnabled(value);
        txtPostalCode.setEditable(value);
        txtPhone.setEditable(value);
        txtEMail.setEditable(value);
        txtFax.setEditable(value);
        txtLastUpdate.setEditable(value);
        txtUpdateUser.setEditable(value);
//        btnOK.setEnabled(false);
        
        //added by raghuSV
//        btnCancel.setFocusable(true);
        
    }
    
//    /**
//     * This method is called for setting the background color of the text and
//     * combobox component light grey is the color if the component is disabled.
//     *
//     * @param value boolean .
//     */
//    private void setControlsBgColour(Color value){
//        txtSponsorCode.setBackground(value);
//        txtAcronym.setBackground(value);
//        txtUnit.setBackground(value);
//        txtName.setBackground(value);
//        cmbType.setBackground(value);
//        txtAudit.setBackground(value);
//        txtDuns.setBackground(value);
//        txtDuns4.setBackground(value);
//        txtCage.setBackground(value);
//        txtDodc.setBackground(value);
//        txtAddress1.setBackground(value);
//        txtAddress2.setBackground(value);
//        txtAddress3.setBackground(value);
//        txtCity.setBackground(value);
//        txtCounty.setBackground(value);
//        cmbState.setBackground(value);
//        cmbCountry.setBackground(value);
//        txtPostalCode.setBackground(value);
//        txtPhone.setBackground(value);
//        txtEMail.setBackground(value);
//        txtFax.setBackground(value);
//        txtLastUpdate.setBackground(value);
//        txtUpdateUser.setBackground(value);
//    }
    
    /**
     * This method is invoked when the user has changed the data without saving
     */
    private void userDataChanged() {
        /* if the data is changed */
        if (functionType != 'D') {
            dataChanged = true;
        }
        
    }
    
    /**
     * This class is to fire the key events in all the text boxes to check
     * whether user has changed any information before closing the window.
     */
    class CoeusTextListener extends KeyAdapter {
        /** Default method
         * @param kt KeyEvent
         */
        public void keyTyped(KeyEvent kt){
        }
        /** Default method
         *  @param kp KeyEvent
         */
        public void keyPressed(KeyEvent kp){
        }
        /**
         * When any data changes in the from this method will be called
         * @param kr KeyEvent
         */
        public void keyReleased(KeyEvent kr){
            userDataChanged();
        }
    }
    
    /**
     *  Method used to refresh the sponsor base window
     *  after adding a new sponsor details or modifying an existing sponsor
     */
    
    public void setDataToUpdate() {
        rowData = new Vector();
        rowData.add(spBean.getSponsorCode());
        rowData.add(spBean.getName());
        rowData.add(spBean.getAcronym());
        rowData.add(spBean.getTypeDescription());
        rowData.add(spBean.getPostalCode());
        rowData.add(spBean.getState());
        rowData.add(spBean.getStateDescription());
        rowData.add(spBean.getCountryName());
        rowData.add(spBean.getAuditReport());
        rowData.add(spBean.getDuns());
        rowData.add(spBean.getDuns4());
        rowData.add(spBean.getDodc());
        rowData.add(spBean.getCage());
        rowData.add(spBean.getOwnedBy());
    }
    
    
    //update for RowLocking. Subramanya
    private void releaseUpdateLock(){
        try{
            if ( functionType == 'U' ) {
                String rowId = this.txtSponsorCode.getText().trim();
                RequesterBean requester = new RequesterBean();
                requester.setDataObject(rowId);
                requester.setFunctionType('Z');
                String connectTo =CoeusGuiConstants.CONNECTION_URL+"/spMntServlet";
                AppletServletCommunicator comm =
                        new AppletServletCommunicator(connectTo,requester);
                /**
                 * Updated for REF ID :0003  Feb'21 2003.
                 * Hour Glass implementation while DB Trsactions Wait
                 * by Subramanya Feb' 21 2003
                 */
                if( dlgWindow != null ){
                    dlgWindow.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
                }
                comm.send();
                ResponderBean res = comm.getResponse();
                if( dlgWindow != null ){
                    dlgWindow.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                }
                
                if (res != null && !res.isSuccessfulResponse()){
                    CoeusOptionPane.showErrorDialog(res.getMessage());
                    return;
                }
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    
    /**
     * gets the row of data for the sponsor added or modified
     * @return rowData Vector of sponsor deatils
     */
    public Vector getDataToUpdate() {
        return rowData;
    }
    
    //Bug Fix 1149:Start 6
    private void checkForCode(String txtEntered){
        boolean inside = false;
        if(cvStateData != null && cvStateData.size()>0){
            for(int index = 0; index < cvStateData.size();index++){
                ComboBoxBean stateComboBean = (ComboBoxBean)cvStateData.get(index);
                inside = true;
                if(txtEntered.equalsIgnoreCase(stateComboBean.getCode()) ||
                        txtEntered.equalsIgnoreCase(stateComboBean.getDescription())){
                    //cmbState.getEditor().setItem(stateComboBean.getDescription());
                    cmbState.setSelectedItem(stateComboBean);
                    return ;
                }
            }
        }//End of if
        
        if(inside){
            ComboBoxBean comboBean = new ComboBoxBean("",txtEntered);
            cmbState.setSelectedItem(comboBean);
            
        }
    }
    //End of checkForCode
    //Bug Fix 1149:End 6
    
}//end of class

