/**
 * @(#)RolodexMaintenanceDetailForm.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 29-JUN-2007
 * by Leena
 */
package edu.mit.coeus.rolodexmaint.gui;

import javax.swing.*;
import javax.swing.border.*;
//import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.event.DocumentListener;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.DefaultTableModel;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;

//import javax.swing.event.DocumentListener;
//import javax.swing.event.DocumentEvent;

// JM 7-11-2011 import email address validator
import edu.vanderbilt.coeus.validate.*;
// END

import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintController;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
//import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
//import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusComboBox;
//import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.UserUtils;

//import java.net.URL;
//import java.io.InputStream;
//import java.io.ObjectOutputStream;
//import java.net.URLConnection;
//import java.io.ObjectInputStream;
//import java.io.IOException;
//import java.util.Hashtable;
import java.util.Vector;
import java.util.Observer;
//import java.text.SimpleDateFormat;
//import java.sql.Timestamp;
import java.io.Serializable;
import java.util.HashMap;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;

/**
 * This class constructs the RolodexMaintenanceDetailForm which is used to
 * maintain rolodex details.
 * @author  phani
 * @version 1.0
 * @author Subramanya
 * @version 1.1 Nov'6 2002 Fix for Sponsor Name Display
 */
public class RolodexMaintenanceDetailForm extends JPanel implements Serializable,
        ActionListener {
    
    private char functionType;
    private RolodexDetailsBean rldxBean;
    private RolodexMaintController rldxController;
    private RequesterBean requester;
    private CoeusAppletMDIForm mdiForm;
    private CoeusFontFactory fontFactory;
    private CoeusDlgWindow dlgWindow;
    
//    private String CONNECTION_URL = "";
//    private final String CONTENT_KEY = "Content-Type";
//    private final String CONTENT_VALUE =    "application/octet-stream";
    private boolean dataChanged =false;
    private boolean isSponsorSearchRequired =false;
    private boolean isSponsorInfoRequired =false;
    private final String DISPLAY_TITLE = "Display Sponsor";
//    private final String ROLODEX_SERVLET = "/rolMntServlet";
//    private JDialog dlgRolMaint;
    private JPanel pnlControl;
    private JButton btnOK;
    private JButton btnCancel;
    private JButton btnSponsor;
    private JPanel pnlRolodexDetails;
    private JLabel lblRolodexID;
    private JTextField txtRolodexId;
    private JLabel lblName;
    private CoeusTextField txtLastName;
    private JLabel lblTitle;
    private JLabel lblPrefix;
    private CoeusTextField txtPrefix;
    private JLabel lblMiddleName;
    private CoeusTextField txtFirstName;
    private JLabel lblFirstName;
    private CoeusTextField txtMiddleName;
    private CoeusTextField txtSuffix;
    private JLabel lblLastUpdate;
    private CoeusTextField txtLastUpdate;
    private JLabel lblLastName;
    private CoeusTextField txtSponsorCode;
    private JLabel lblSuffix;
    private JLabel lblOrganization;
    private CoeusTextField txtTitle;
    private JLabel lblSponsor;
    private JLabel lblUpdateUser;
    private CoeusTextField txtOrganization;
    private CoeusTextField txtUpdateUser;
    private JLabel lblSponsorName;
    private JLabel lblAddress;
    private CoeusTextField txtAddress1;
    private CoeusTextField txtAddress2;
    private CoeusTextField txtAddress3;
    private JLabel lblCity;
    private CoeusTextField txtCity;
    private JLabel lblCounty;
    private CoeusTextField txtCounty;
    private JLabel lblState;
    private CoeusComboBox cmbState;
    private JLabel lblPostalCode;
    private CoeusTextField txtPostalCode;
    private JLabel lblCountry;
    private CoeusComboBox cmbCountry;
    private JLabel lblPhone;
    private CoeusTextField txtPhone;
    private JLabel lblEMail;
    private CoeusTextField txtEMail;
    private JLabel lblFax;
    private CoeusTextField txtFax;
    private JLabel lblComments;
    private JTextArea txtComments;
    private JPanel pnlMain;
    private JScrollPane scrlPnComments;
    private JLabel lblActiveStatus;
    private JLabel lblInActiveStatus;
    private JRadioButton radioActiveStatus;
    private JRadioButton radioInActiveStatus;
    private String rolodexId;
    
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    private static final String SPONSOR_INACTIVE_STATUS = "I";
    private String status = CoeusGuiConstants.EMPTY_STRING ;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private boolean dataSaved = false;
    
    private BaseWindowObservable observable = new BaseWindowObservable();

    //Added for Case#4252 - Rolodex state dropdown associated with country - Start
    private static final char GET_STATE_WITH_COUNTRY = 's';
    private HashMap hmComboStateWithCountry;
   //Case#4252 - End
    //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START
    private boolean canModifyStatus;
    private final String ACTIVE_STATUS = "A";
    private final String INACTIVE_STATUS = "I";
    //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry END
    
    /**
     * Constructor which instantiates RolodexMaintenanceDetailForm and populates them with data
     * specified. And sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param rldxBean a RolodexDetailsBean which consists of rolodex details.
     */
    
    public RolodexMaintenanceDetailForm(char functionType,
            RolodexDetailsBean rldxBean) {
        this.functionType = functionType;
        this.rldxBean = rldxBean;
        this.setLayout(new BorderLayout());
        createRolodexComponent();
        canModifyStatus = checkCanModifyStatus();
        setRolodexData();
        formatFields();
         //coeusqa-1528 start
        viewEditRadioBtn(functionType);
         //coeusqa-1528 end
        dataChanged =false;
        rldxController = new RolodexMaintController();
        
    }
    /**
     * Constructor which instantiates RolodexMaintenanceDetailForm.
     * And it sets the enabled statusfor all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param rolodexId a String which consists of rolodexId
     */
    public RolodexMaintenanceDetailForm(char functionType,String rolodexId){
        RolodexMaintController rldxController = new RolodexMaintController();
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        if( dlgWindow != null ){
            dlgWindow.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        }
        RolodexDetailsBean rldxDetails;
        rldxDetails = rldxController.displayRolodexInfo(rolodexId);
        if( dlgWindow != null ){
            dlgWindow.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }
        
        
        this.functionType = functionType;
        this.rldxBean = rldxDetails;
        this.setLayout(new BorderLayout());
        this.rolodexId=rolodexId;
        createRolodexComponent();
        setRolodexData();
        //coeusqa-1528 start
        viewEditRadioBtn(functionType);
         //coeusqa-1528 end
        dataChanged =false;
    }
    // Added by Bijosh
    /**
     * This is used for displaying the rolodex details.
     * This is used in RoldexReferencesPanel and RolodexReplacePanel
     * Constructor which instantiates RolodexMaintenanceDetailForm.
     * This constructor will create a panel which won't add buttons.
     *
     */
    
    public RolodexMaintenanceDetailForm(String rolodexId,RolodexDetailsBean rldxDetails) {
//        RolodexMaintController rldxController = new RolodexMaintController();
        this.functionType = 'N';
        this.rldxBean = rldxDetails;
        this.rolodexId=rolodexId;
        createRolodexComponent();
        setRolodexData();
        viewEditRadioBtn(functionType);
    }
    /**
     * This method will give the RolodexMaintenanceDetailForm.
     * @return JPanel
     */
    public JPanel getRolodexComponent(){
        /* Set the controls enable/disable and background color depending
         * on the function type
         */
        setControls(functionType);
        return this;
    }
    
    /**
     *  Method used to create the components in RolodexDetails window
     */

    private void createRolodexComponent() {
        coeusMessageResources = CoeusMessageResources.getInstance();

        pnlMain = new JPanel();
        pnlControl = new JPanel();
        if(functionType!='N') // bijosh. For rolodex references details buttons not required
        {
            btnOK = new JButton();
            btnOK.setFont(fontFactory.getLabelFont());
            btnCancel = new JButton();
            btnCancel.setFont(fontFactory.getLabelFont());
        }
        
        //Moved for case#3341 - Sponsor Validation and UI realigmnent - Start
        //Comments Text
        txtComments = new JTextArea(6,60);
        txtComments.setDocument(new LimitedPlainDocument(300));
        txtComments.setFont(fontFactory.getNormalFont());
        txtComments.setLineWrap(true);
        txtComments.setWrapStyleWord(true);        
        txtComments.setTabSize(2);
        txtComments.setWrapStyleWord(true);
        txtComments.setLineWrap(true);
        txtComments.addKeyListener(new CoeusTextListener());
        txtComments.setFocusable(true);
        scrlPnComments = new JScrollPane();   
         //coeusqa-1528 start
        Dimension commentsDimension = new Dimension(652, 100);
        scrlPnComments.setMinimumSize(commentsDimension);
        scrlPnComments.setPreferredSize(commentsDimension);
        scrlPnComments.setViewportView(txtComments);
         //coeusqa-1528 end
        scrlPnComments.setBackground(java.awt.Color.white);
        scrlPnComments.setForeground(java.awt.Color.white);    
        //Moved for case#3341 - Sponsor Validation and UI realigmnent - End
        
        pnlRolodexDetails = new JPanel();
        pnlMain.setLayout(new BorderLayout(10,10));
        GridBagConstraints gridBagConstraints1;
        pnlControl.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints2;
        //For Rolodex references
        if (functionType != 'N' ) 
        {
            btnOK.setText("OK");
            gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.ipadx = 28;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints2.insets = new Insets(0, 0, 5, 0);
            pnlControl.add(btnOK, gridBagConstraints2);

            btnOK.setMnemonic('O');
            btnOK.addActionListener( this );
            btnCancel.setText("Cancel");
            btnCancel.addActionListener( this );

            btnCancel.setMnemonic('C');

            gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.ipadx = 8;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
            //gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints2.insets = new Insets(0, 0, 15, 0);
            pnlControl.add(btnCancel, gridBagConstraints2);            
            btnSponsor = new JButton();
            btnSponsor.setFont(fontFactory.getLabelFont());
            btnSponsor.setText("Sponsor");            
            gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
            pnlControl.add(btnSponsor, gridBagConstraints2);
            btnSponsor.addActionListener( this );
            btnSponsor.setMnemonic('S');
            btnSponsor.setNextFocusableComponent(txtLastName);               
            JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnlControls.add(pnlControl);
            pnlMain.add(pnlControls,BorderLayout.EAST);
        }
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.ipadx = 2;
        gridBagConstraints1.ipady = 30;
        gridBagConstraints1.insets = new Insets(0, 7, 7, 1);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
        
        pnlRolodexDetails.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints3;
        if(functionType!='N') { // bijosh. For rolodex details
            pnlRolodexDetails.setBorder(new BevelBorder(BevelBorder.LOWERED));
        }

        //Moved for case#3341 - Sponsor Validation and UI realigmnent - Start
        //Row 1 - Start
        //This row contains the following components in order
        //RolodexID (label, text), LastUpdate(label, text), UpdateUser (label, text)
        //Roloedex ID Label
        lblRolodexID = new JLabel();
        lblRolodexID.setFont(fontFactory.getLabelFont());
        lblRolodexID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRolodexID.setText("Rolodex Id: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblRolodexID, gridBagConstraints3);
        
        //Rolodex ID Text
        txtRolodexId = new CoeusTextField();
        txtRolodexId.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,8));        
        txtRolodexId.setFont(fontFactory.getNormalFont());
        txtRolodexId.setName("txtRolodexId");
        txtRolodexId.setNextFocusableComponent(txtLastUpdate);
        txtRolodexId.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 0;
        //gridBagConstraints3.ipadx = 60;
        txtRolodexId.setPreferredSize(new Dimension(60,20));
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtRolodexId, gridBagConstraints3);
        
        //Last Update Label
        lblLastUpdate = new JLabel();
        lblLastUpdate.setFont(fontFactory.getLabelFont());
        lblLastUpdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastUpdate.setText("Last Update: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;      
        pnlRolodexDetails.add(lblLastUpdate, gridBagConstraints3);
                
        //Last Update Text
        txtLastUpdate = new CoeusTextField();
        txtLastUpdate.setFont(fontFactory.getNormalFont());
        txtLastUpdate.setName("txtLastUpdate");
        txtLastUpdate.setPreferredSize(new Dimension(160,20));
        txtLastUpdate.setNextFocusableComponent(txtUpdateUser);
        txtLastUpdate.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtLastUpdate, gridBagConstraints3);
        
        //Last UpdateUser Label
        lblUpdateUser = new JLabel();
        lblUpdateUser.setFont(fontFactory.getLabelFont());
        lblUpdateUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdateUser.setText("Updated By: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblUpdateUser, gridBagConstraints3);

        //Last UpdateUser Text
        txtUpdateUser = new CoeusTextField();
        txtUpdateUser.setFont(fontFactory.getNormalFont());
        // Bug fix for case #1839 start 13
        /* UserId to UserName Enhancement - Start
         * The following line is commented to avoid the fixed size of the username display field
         */        
        //txtUpdateUser.setDocument(new LimitedPlainDocument(20));
        //UserId to UserName Enhancement - End
        txtUpdateUser.setMinimumSize(new Dimension(160, 20));
        txtUpdateUser.setMaximumSize(new Dimension(160, 20));
        txtUpdateUser.setPreferredSize(new Dimension(160, 20));
        // Bug fix for case #1839 end 13        
        txtUpdateUser.setName("txtUpdateUser");
        txtUpdateUser.setNextFocusableComponent(txtLastName);
        txtUpdateUser.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 0;
//        gridBagConstraints3.ipadx = 50;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtUpdateUser, gridBagConstraints3);
        //Row 1 - End
        
        //Row 2 - Start
        //This row contains the following components in order
        //LastName (label), FirstName(label), MiddleName(label)        
        //Last Name Label
        lblLastName = new JLabel();
        lblLastName.setFont(fontFactory.getLabelFont());
        lblLastName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblLastName.setText("Last:");        
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridwidth = 1;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(lblLastName, gridBagConstraints3);
        
        //First Name label
        lblFirstName = new JLabel();
        lblFirstName.setFont(fontFactory.getLabelFont());
        lblFirstName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFirstName.setText("First:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridwidth = 2;
        //gridBagConstraints3.ipadx = 42;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(lblFirstName, gridBagConstraints3);
                
        //Middle Name Label
        lblMiddleName = new JLabel();
        lblMiddleName.setFont(fontFactory.getLabelFont());
        lblMiddleName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMiddleName.setText("Middle:");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(lblMiddleName, gridBagConstraints3);
        //Row 2 - End
        
        //Row 3 - Start
        //This row contains the following components in order
        //Name (label), LastName(text), FirstName(text) MiddleName(text)             
        //Name Label        
        lblName = new JLabel();
        lblName.setFont(fontFactory.getLabelFont());
        // Bug fix for case #1839 end 0                
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblName.setText("Name: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblName, gridBagConstraints3);
        
        //Last Name Text
        txtLastName = new CoeusTextField();
        txtLastName.setDocument(new LimitedPlainDocument(20));
        txtLastName.setFont(fontFactory.getNormalFont());
        //txtLastName.setColumns(10);
        txtLastName.setName("txtLastName");
        txtLastName.setNextFocusableComponent(txtFirstName);
        txtLastName.addKeyListener(new CoeusTextListener());
        txtLastName.setMinimumSize(new Dimension(160, 20));
        txtLastName.setMaximumSize(new Dimension(160, 20));
        txtLastName.setPreferredSize(new Dimension(160, 20));        
        gridBagConstraints3 = new GridBagConstraints();
       txtLastName.setFocusable(true);
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //gridBagConstraints3.ipadx = 130;        
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtLastName, gridBagConstraints3);
        
        //First Name Text
        txtFirstName = new CoeusTextField();
        txtFirstName.setDocument(new LimitedPlainDocument(20));
        txtFirstName.setFont(fontFactory.getNormalFont());

        txtFirstName.setName("txtFirstName");
        txtFirstName.setNextFocusableComponent(txtMiddleName);
        txtFirstName.addKeyListener(new CoeusTextListener());
        txtFirstName.setMinimumSize(new Dimension(160, 20));
        txtFirstName.setMaximumSize(new Dimension(160, 20));
        txtFirstName.setPreferredSize(new Dimension(160, 20));
       txtFirstName.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //gridBagConstraints3.ipadx = 100;        
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtFirstName, gridBagConstraints3);
                        
        //Middle Name Text
        txtMiddleName = new CoeusTextField();
        // Bug fix for case #1839 start 1
        //txtMiddleName.setMinimumSize(new Dimension(60, 20));
        //txtMiddleName.setMaximumSize(new Dimension(60, 20));
        //txtMiddleName.setPreferredSize(new Dimension(60, 20));
        // Bug fix for case #1839 end 1        
        txtMiddleName.setDocument(new LimitedPlainDocument(20));
        txtMiddleName.setFont(fontFactory.getNormalFont());

        txtMiddleName.setName("txtMiddleName");
        txtMiddleName.setNextFocusableComponent(txtSuffix);
        txtMiddleName.addKeyListener(new CoeusTextListener());
        txtMiddleName.setMinimumSize(new Dimension(160, 20));
        txtMiddleName.setMaximumSize(new Dimension(160, 20));
        txtMiddleName.setPreferredSize(new Dimension(160, 20));
       txtMiddleName.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 2;
        //gridBagConstraints3.ipadx = 80;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtMiddleName, gridBagConstraints3);        
        //Row 3 - End
        
        //Row 4 - Start
        //Suffix Label
        lblSuffix = new JLabel();
        lblSuffix.setFont(fontFactory.getLabelFont());        
        lblSuffix.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSuffix.setText("Suffix: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblSuffix, gridBagConstraints3);
        
        //Suffix Text
        txtSuffix = new CoeusTextField();
        txtSuffix.setDocument(new LimitedPlainDocument(10));
        txtSuffix.setFont(fontFactory.getNormalFont());
        txtSuffix.setName("txtSuffix");
        txtSuffix.setNextFocusableComponent(txtPrefix);
        txtSuffix.addKeyListener(new CoeusTextListener());
       txtSuffix.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 3;
        //gridBagConstraints3.ipadx = 58;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        txtSuffix.setPreferredSize(new Dimension(60, 20));        
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtSuffix, gridBagConstraints3);
        
        //Prefix Label
        lblPrefix = new JLabel();
        lblPrefix.setFont(fontFactory.getLabelFont());        
        lblPrefix.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrefix.setText("Prefix: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 3;
        //gridBagConstraints3.ipady = 2;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        // Bug fix for case #1839 start 11
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        // Bug fix for case #1839 end 11
        pnlRolodexDetails.add(lblPrefix, gridBagConstraints3);
        
        //Prefix Text
        txtPrefix = new CoeusTextField();
        // Bug fix for case #1839 start 2
        txtPrefix.setMinimumSize(new Dimension(60, 20));
        txtPrefix.setMaximumSize(new Dimension(60, 20));
        txtPrefix.setPreferredSize(new Dimension(60, 20));
        // Bug fix for case #1839 end 2        
        txtPrefix.setDocument(new LimitedPlainDocument(10));
        txtPrefix.setFont(fontFactory.getNormalFont());
        txtPrefix.setName("txtPrefix");
        txtPrefix.addKeyListener(new CoeusTextListener());
        txtPrefix.setNextFocusableComponent(txtTitle);
       txtPrefix.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 3;
        gridBagConstraints3.gridy = 3;
        //gridBagConstraints3.ipadx = 50;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtPrefix, gridBagConstraints3);

        //Title Label
        lblTitle = new JLabel();
        lblTitle.setFont(fontFactory.getLabelFont());
        // Bug fix for case #1839 start 0
        lblTitle.setMaximumSize(new Dimension(20,20));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setText("Title: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 3;
        //gridBagConstraints3.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        pnlRolodexDetails.add(lblTitle, gridBagConstraints3);

        //Title Text
        txtTitle = new CoeusTextField();
        // Bug fix for case #1839 start 3
        txtTitle.setMinimumSize(new Dimension(160, 20));
        txtTitle.setMaximumSize(new Dimension(160, 20));
        txtTitle.setPreferredSize(new Dimension(160, 20));
        // Bug fix for case #1839 end 3        
        txtTitle.setDocument(new LimitedPlainDocument(35));
        txtTitle.setFont(fontFactory.getNormalFont());        
        txtTitle.setName("txtTitle");
        txtTitle.setNextFocusableComponent(txtSponsorCode);
        txtTitle.addKeyListener(new CoeusTextListener());
        txtTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
       txtTitle.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 3;
        // gridBagConstraints3.gridwidth = 3;
        // gridBagConstraints3.ipadx = 144;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        // gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtTitle, gridBagConstraints3);
        //Row 4 - End
        
        //Row 5 - Start
        //Sponsor Label
        lblSponsor = new JLabel();
        lblSponsor.setFont(fontFactory.getLabelFont());
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblSponsor, gridBagConstraints3);
        
        //Sponsor Text
        txtSponsorCode = new CoeusTextField();
        //Added by chandra to fix #1346
        txtSponsorCode.setMinimumSize(new Dimension(60, 20));
        txtSponsorCode.setMaximumSize(new Dimension(60, 20));
        txtSponsorCode.setPreferredSize(new Dimension(60, 20));
        //End Chandra #1346
        txtSponsorCode.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,6));
        txtSponsorCode.setRequestFocusEnabled(true);
        txtSponsorCode.setDisabledTextColor(java.awt.Color.white);
        txtSponsorCode.setName("txtSponsor");
        txtSponsorCode.setNextFocusableComponent(txtOrganization);
        txtSponsorCode.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 4;
        //gridBagConstraints3.ipadx = 58;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtSponsorCode, gridBagConstraints3);
        txtSponsorCode.addFocusListener(new FocusListener(){

            /* declare a variable to hold the SponsorCode before getting focus
             * in the txtSponsorCode text box
             */
            String oldSpCode ="";
            public void focusGained(FocusEvent e){
                /*Get the sponsorCode information into the variable*/
                oldSpCode = txtSponsorCode.getText().toString().trim();
            }
            public void focusLost(FocusEvent e){
                String sponsorCode = "";
                sponsorCode = txtSponsorCode.getText().toString().trim();
                
                //Added for Case# 3341 - Sponsor Code Validation - start
                //If the sponsor Code is empty, clear the sponsor name
                if(sponsorCode.equals("")){
                    lblSponsorName.setText("");
                }                
                //Added for Case# 3341 - Sponsor Code Validation - end
                
            /*Check the sponsorCode before user changes and with the latest
             * if any change then call the servlet for sponsor information
             */
                if ((!isSponsorSearchRequired) && !e.isTemporary() &&
                isSponsorInfoRequired && (oldSpCode.equals(sponsorCode))){
                    if (!sponsorCode.equals("") ){
                        //Commented for case#3341 - Sponsor Validation
                        getSponsorInfo(sponsorCode);
                    /* set the isSponsorsearchReuired to false.so that the
                     * varibale will be initialized to the same value for setting
                     * in mouse double click. This variable is allowed to restrict
                     * the focusLost event for txtSponsorcode when showing sponsor
                     * search screen on double clicking in txtSponsorCode.
                     */
                        isSponsorSearchRequired =false;
                    }
                    
                }else if( (!isSponsorSearchRequired)
                && (!oldSpCode.equals(sponsorCode)) && (!e.isTemporary())) {
                    if (!sponsorCode.equals("") ){
                        //Commented for case#3341 - Sponsor Validation
                        getSponsorInfo(sponsorCode);
                    /* set the isSponsorsearchReuired to false.so that the
                     * varibale will be initialized to the same value for setting
                     * in mouse double click. This variable is allowed to restrict
                     * the focusLost event for txtSponsorcode when showing sponsor
                     * search screen on double clicking in txtSponsorCode.
                     */
                        isSponsorSearchRequired =false;
                    }
                }
            }
            
        });

        txtSponsorCode.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                /* If the user double clikcs in the txtSponsorCode call
                 * showsponsorInfo window
                 */
                if (me.getClickCount() == 2) {
                    /*set issponsorSearchRequired to true to avoid firing in
                     *focusLost event
                     */
                    txtSponsorCode.setCursor(new Cursor(Cursor.WAIT_CURSOR) );
                    isSponsorSearchRequired =true;
                    showSponsorInfo();
                    txtSponsorCode.setCursor(new Cursor(Cursor.DEFAULT_CURSOR) );
                }
            }
        });
        txtSponsorCode.addKeyListener( new CoeusTextListener());
        txtSponsorCode.addActionListener( this );
                
        //Sponsor Name Label
        lblSponsorName = new JLabel();
        lblSponsorName.setFont(fontFactory.getLabelFont());
        lblSponsorName.setText("");
        lblSponsorName.setName("lblSponsorName");
        lblSponsorName.setHorizontalTextPosition(JLabel.LEFT);
        Dimension dimension = new Dimension(400,20);
        lblSponsorName.setMinimumSize(dimension);
        lblSponsorName.setMaximumSize(dimension);
        lblSponsorName.setPreferredSize(dimension);
        gridBagConstraints3 = new GridBagConstraints();
//        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.gridwidth = 3;
//        gridBagConstraints3.weightx= 3;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //comment for bug fix 1839
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        //gridBagConstraints3.ipadx = 290;
        /**bug fix for case 1839 start 12 */
        //lblSponsorName.setMinimumSize(new Dimension( 280, 20 ) );
        //lblSponsorName.setPreferredSize( new Dimension( 280, 20 ) );
        //lblSponsorName.setMaximumSize( new Dimension( 280, 20 ) );
        /** bug fix for case 1839 end 12 */
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(lblSponsorName, gridBagConstraints3);        
        //Row 5 - End
        //coeusqa-1528 start SHABARISH
        JPanel pnlStatus = new JPanel();
        pnlStatus.setLayout(new GridBagLayout());
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        GridBagConstraints statusGridBagConstraints;
        
        
        ButtonGroup buttonGroup  = new ButtonGroup ();
        radioActiveStatus = new JRadioButton("Active");
        radioActiveStatus.setFocusable(true);
        radioActiveStatus.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                if (rldxBean!=null) {                    
                    if (ie.getStateChange()==1) {
                        dataChanged = true;
                    }else {
                        dataChanged = false;
                    }
                }
            }
        });
        statusGridBagConstraints = new GridBagConstraints();
        statusGridBagConstraints.gridx = 0;
        statusGridBagConstraints.gridy = 0;
        statusGridBagConstraints.insets = new Insets(2, 0, 2, 2);
        statusGridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        buttonGroup.add(radioActiveStatus);
        pnlStatus.add(radioActiveStatus, statusGridBagConstraints);
        //radioActiveStatus.setSelected(true);
        radioInActiveStatus = new JRadioButton("Inactive");
        radioInActiveStatus.setFocusable(true);
        radioInActiveStatus.addItemListener(new ItemListener() {
             public void itemStateChanged(ItemEvent ie){
                if (rldxBean!=null) {                    
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
        statusGridBagConstraints.gridx = 1;
        statusGridBagConstraints.gridy = 0;
        statusGridBagConstraints.insets = new Insets(2, 0, 2, 2);
        statusGridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlStatus.add(radioInActiveStatus, statusGridBagConstraints);
        pnlRolodexDetails.add(pnlStatus, gridBagConstraints3);
        //coeusqa-1528 end
        //coeusqa-1528 start
        if(rldxBean != null && rldxBean.getStatus()!=null && ACTIVE_STATUS.equalsIgnoreCase(rldxBean.getStatus().trim())) {
            radioActiveStatus.setSelected(true);
        }
        else if(rldxBean != null && INACTIVE_STATUS.equalsIgnoreCase(rldxBean.getStatus().trim())) {
            radioInActiveStatus.setSelected(true);
        }
       //coeusqa-1528 ends
        //Row 6 - Start
        //Organization Label
        lblOrganization = new JLabel();
        lblOrganization.setFont(fontFactory.getLabelFont());
        lblOrganization.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOrganization.setText("Organization: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblOrganization, gridBagConstraints3);
        
        //Organization Text
        txtOrganization = new CoeusTextField();
        txtOrganization.setDocument(new LimitedPlainDocument(80));
        txtOrganization.setFont(fontFactory.getNormalFont());
        txtOrganization.setRequestFocusEnabled(true);
        txtOrganization.setName("txtOrganization");
        txtOrganization.setNextFocusableComponent(txtAddress1);
        // JM 7-22-2011 added required field highlighting
        txtOrganization.setRequired(true);
        // END
        txtOrganization.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //gridBagConstraints3.ipadx = 442;
        //txtOrganization.setPreferredSize(new Dimension(589, 20));
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtOrganization, gridBagConstraints3);
        txtOrganization.addKeyListener(new CoeusTextListener());        
        //Row 6 - End

        //Row 7 - Start
        //Address Label
        lblAddress = new JLabel();
        lblAddress.setFont(fontFactory.getLabelFont());
        lblAddress.setText("Address: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblAddress, gridBagConstraints3);
        
        //Address1 text
        txtAddress1 = new CoeusTextField();
        // Bug fix for case #1839 start 4
        //txtAddress1.setMinimumSize(new Dimension(589, 20));
        //txtAddress1.setMaximumSize(new Dimension(589, 20));
        //txtAddress1.setPreferredSize(new Dimension(589, 20));
        // Bug fix for case #1839 end 4        
        txtAddress1.setDocument(new LimitedPlainDocument(80));
        txtAddress1.setFont(fontFactory.getNormalFont());
        txtAddress1.setNextFocusableComponent(txtAddress2);
        txtAddress1.addKeyListener(new CoeusTextListener());
        txtAddress1.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.gridwidth = 5;
        // gridBagConstraints3.ipadx = 442;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //commented for bug fix 1839
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtAddress1, gridBagConstraints3);        
        //Row 7 - End

        //Row 8 - Start
        //Address2 text
        txtAddress2 = new CoeusTextField();
        // Bug fix for case #1839 start 5
        //txtAddress2.setMinimumSize(new Dimension(589, 20));
        //txtAddress2.setMaximumSize(new Dimension(589, 20));
        //txtAddress2.setPreferredSize(new Dimension(589, 20));
        // Bug fix for case #1839 end 5        
        txtAddress2.setDocument(new LimitedPlainDocument(80));
        txtAddress2.setFont(fontFactory.getNormalFont());
        txtAddress2.setNextFocusableComponent(txtAddress3);
        txtAddress2.addKeyListener(new CoeusTextListener());
        txtAddress2.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 7;
        gridBagConstraints3.gridwidth = 5;
        //gridBagConstraints3.ipadx = 442;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //commented for bug fix 1839
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtAddress2, gridBagConstraints3);        
        //Row 8 - End
        
        //Row 9 Start
        //Address3 text
        txtAddress3 = new CoeusTextField();
        // Bug fix for case #1839 start 6
        //txtAddress3.setMinimumSize(new Dimension(589, 20));
        //txtAddress3.setMaximumSize(new Dimension(589, 20));
        //txtAddress3.setPreferredSize(new Dimension(589, 20));
        // Bug fix for case #1839 end 6        
        txtAddress3.setDocument(new LimitedPlainDocument(80));
        txtAddress3.setFont(fontFactory.getNormalFont());
        txtAddress3.setNextFocusableComponent(txtCity);
        txtAddress3.addKeyListener(new CoeusTextListener());
        txtAddress3.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 8;
        gridBagConstraints3.gridwidth = 5;
        // gridBagConstraints3.ipadx = 442;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //commented for bug fix 1839
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtAddress3, gridBagConstraints3);        
        //Row 9 End

        //Row 10 - Start
        //This row contains the following components in order
        //City (label, text), County(label, text)
        //City Label
        lblCity = new JLabel();
        lblCity.setFont(fontFactory.getLabelFont());
        lblCity.setText("City: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 9;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblCity, gridBagConstraints3);
        
        //City Text
        txtCity = new CoeusTextField();
        txtCity.setDocument(new LimitedPlainDocument(30));
        txtCity.setFont(fontFactory.getNormalFont());
        txtCity.setNextFocusableComponent(txtCounty);
        txtCity.addKeyListener(new CoeusTextListener());
        txtCity.setFocusable(true);

        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 9;
        gridBagConstraints3.gridwidth = 2;
        //gridBagConstraints3.ipadx = 120;
        txtCity.setPreferredSize(new Dimension(130, 20));
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtCity, gridBagConstraints3);
                
        //County Label
        lblCounty = new JLabel();
        lblCounty.setFont(fontFactory.getLabelFont());
        lblCounty.setText("County: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 9;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblCounty, gridBagConstraints3);        
        
        //County Text
        txtCounty = new CoeusTextField();
        // Bug fix for case #1839 start 7
        txtCounty.setMinimumSize(new Dimension(160, 20));
        txtCounty.setMaximumSize(new Dimension(160, 20));
        txtCounty.setPreferredSize(new Dimension(160, 20));
        // Bug fix for case #1839 end 7        
        txtCounty.setDocument(new LimitedPlainDocument(30));
        txtCounty.setFont(fontFactory.getNormalFont());
       txtCounty.setFocusable(true);
        txtCounty.setNextFocusableComponent(cmbState);
        txtCounty.addKeyListener(new CoeusTextListener());

        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 9;
        //gridBagConstraints3.ipadx = 80;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //commented for bug fix 1839
        //gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtCounty, gridBagConstraints3);
        //Row 10 - End
        
        //Row 11 - Start
        //This row contains the following components in order
        //State (label, combo), PostalCode(label, text)        
        //State Label
        lblState = new JLabel();
        lblState.setFont(fontFactory.getLabelFont());
        //Modified for Case#4254 - change "State" Label to "State/ Province" 
//        lblState.setText("State Name: ");
        lblState.setText("State/ Province Name: ");
        //Case#4254 - End
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 10; 
        gridBagConstraints3.insets = new Insets(2, 0, 2,2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblState, gridBagConstraints3);
        
        //State Combo - Start
        //Modified for case#3278 - State Combobox is made non editable
        cmbState = new CoeusComboBox();
        cmbState.setEditable(false);
        //Modified for Case#4252 - Rolodex state dropdown associated with country - Start
//        cmbState.setShowCode(true);       
        //Case#4252 - End
        cmbState.setMaximumSize(new java.awt.Dimension(130, 20));

        cmbState.setMinimumSize(new java.awt.Dimension(130, 20));
        cmbState.setPreferredSize(new java.awt.Dimension(130,20));
        cmbState.setFocusable(true);
        cmbState.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                if (rldxBean!=null) {
                    String bnSelectState=
                    ((ComboBoxBean)cmbState.getSelectedItem()).getCode();
                    if (bnSelectState.equalsIgnoreCase(rldxBean.getState())) {
                        dataChanged = false;
                    }else {
                        dataChanged = true;
                    }
                }
            }
        });
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 10;
        gridBagConstraints3.gridwidth = 2;
        //gridBagConstraints3.ipadx = 15;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(cmbState, gridBagConstraints3);
        
        //Postal Code Label
        lblPostalCode = new JLabel();
        lblPostalCode.setFont(fontFactory.getLabelFont());
        lblPostalCode.setText("Postal Code: ");
        //GridBagConstraints 
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 10;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblPostalCode, gridBagConstraints3);
        
        //Postal Code Text
        txtPostalCode = new CoeusTextField();
        // Bug fix for case #1839 start 8
        txtPostalCode.setMinimumSize(new Dimension(160, 20));
        txtPostalCode.setMaximumSize(new Dimension(160, 20));
        txtPostalCode.setPreferredSize(new Dimension(160, 20));
        txtPostalCode.setFocusable(true);
        // Bug fix for case #1839 end 8        
        txtPostalCode.setDocument(new LimitedPlainDocument(15));
        txtPostalCode.setFont(fontFactory.getNormalFont());
        txtPostalCode.setNextFocusableComponent(cmbCountry);
        txtPostalCode.addKeyListener(new CoeusTextListener());
        txtPostalCode.addFocusListener( new FocusAdapter(){
            public void focusGained( FocusEvent focusEvt ){
                if( !focusEvt.isTemporary() && cmbState.isPopupVisible() ){
                    cmbState.hidePopup();
                }
            }
        });        
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 10;
        //gridBagConstraints3.ipadx = 80;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        //commented for bug fix 1839
        //gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        //gridBagConstraints3.ipady = 1;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtPostalCode, gridBagConstraints3);
        cmbState.setNextFocusableComponent(txtPostalCode);        
        //Row 11 - End

        //Row 12 - Start
        //This row contains the following components in order
        //Country (label, combo), Phone(label, text)                
        //Country Label
        lblCountry = new JLabel();
        lblCountry.setFont(fontFactory.getLabelFont());
        lblCountry.setText("Country: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblCountry, gridBagConstraints3);
        
        //Country Combo
        cmbCountry = new CoeusComboBox();
        cmbCountry.setFont(fontFactory.getNormalFont());
        cmbCountry.setNextFocusableComponent(txtPhone);
        cmbCountry.setMaximumSize(new java.awt.Dimension(130, 20));
        cmbCountry.setMinimumSize(new java.awt.Dimension(130, 20));
        cmbCountry.setPreferredSize(new java.awt.Dimension(130,20));
        cmbCountry.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.gridwidth = 2;
        //gridBagConstraints3.ipadx = 100;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(cmbCountry, gridBagConstraints3);
        
        //Added for Case#4252 - Rolodex state dropdown associated with country - Start
        requester = new RequesterBean();
        requester.setFunctionType(GET_STATE_WITH_COUNTRY);
        RolodexMaintController rldxController = new RolodexMaintController();
        ResponderBean response = rldxController.sendToServer("/rolMntServlet",requester);
        if(response != null && response.isSuccessfulResponse()){
            hmComboStateWithCountry = (HashMap)response.getDataObject();
        }
        //Case#4252 - End
        
        /* added mouse listener to the country combo for handling state
         * combo information
         */
        cmbCountry.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                userDataChanged();
                //Modified by shiji for fixing bug id : 1843 : step 1 - start
                ComboBoxBean cmbBeanCountry = new ComboBoxBean("","");
                cmbBeanCountry = (ComboBoxBean)cmbCountry.getSelectedItem();
                //bug id : 1843 : step 1 - end
                //Modified for Case#4252 - Rolodex state dropdown associated with country - Start
//                if (cmbBeanCountry.getCode().trim().equalsIgnoreCase("USA")){
//                    if(functionType != 'V'){
//                        cmbState.setShowCode(true);
//                    }
//
//                    /* If country selected is USA then add all the state codes
//                     * information to the state combo else clear the state
//                     * combo and allow the user to enter the state information
//                     */
//                    setStateInfo();
//                }else {
//                    cmbState.setShowCode(false);
//
//                    clearStateInfo();
//                }
                //Case#4252 - End
                clearStateInfo();
                if(cmbBeanCountry != null && !cmbBeanCountry.getCode().equals("")){
                    Vector comboList = (Vector)hmComboStateWithCountry.get(cmbBeanCountry.getCode());
                    if(comboList != null && comboList.size() > 0){
                        int comboLength = comboList.size();
                        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
                            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
                            if(listBox!=null)
                                cmbState.addItem(listBox);
                        }
                    }
                }
            }
        });
        
        //Phone Label
        lblPhone = new JLabel();
        lblPhone.setFont(fontFactory.getLabelFont());
        lblPhone.setText("Phone: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblPhone, gridBagConstraints3);
        
        //Phone Text
        txtPhone = new CoeusTextField();
        // Bug fix for case #1839 start 9
        txtPhone.setMinimumSize(new Dimension(160, 20));
        txtPhone.setMaximumSize(new Dimension(160, 20));
        txtPhone.setPreferredSize(new Dimension(160, 20));
        // Bug fix for case #1839 end 9        
        txtPhone.setDocument(new LimitedPlainDocument(20));
        txtPhone.setFont(fontFactory.getNormalFont());
        txtPhone.setNextFocusableComponent(txtEMail);
        txtPhone.addKeyListener(new CoeusTextListener());
        txtPhone.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 11;
        // gridBagConstraints3.ipadx = 80;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        //commented for bug fix 1839
        //gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtPhone, gridBagConstraints3);
        //Row 12 - End

        //Row 13 - Start
        //This row contains the following components in order
        //EMail (label, text), Fax(label, text)                        
        //EMail Label
        lblEMail = new JLabel();
        lblEMail.setFont(fontFactory.getLabelFont());
        lblEMail.setText("E Mail: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 12;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblEMail, gridBagConstraints3);
        
        //EMail Text
        txtEMail = new CoeusTextField();
        txtEMail.setDocument(new LimitedPlainDocument(60));
        txtEMail.setFont(fontFactory.getNormalFont());
        txtEMail.setNextFocusableComponent(txtFax);
        txtEMail.addKeyListener(new CoeusTextListener());
       txtEMail.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 12;
        //Modified by shiji for fixing the bug id 1870 : start
        gridBagConstraints3.gridwidth = 3;
        //gridBagConstraints3.ipadx = 120;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        txtEMail.setPreferredSize(new Dimension(350,20));
        //Bug fix id 1870 : end
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtEMail, gridBagConstraints3);
        
        //Fax Label
        lblFax = new JLabel();
        lblFax.setFont(fontFactory.getLabelFont());
        lblFax.setText("Fax: ");//RolodexMaintenanceDetailForm
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.gridy = 12;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        pnlRolodexDetails.add(lblFax, gridBagConstraints3);
        
        //Fax Text
        txtFax = new CoeusTextField();
        // Bug fix for case #1839 start 10
        txtFax.setMinimumSize(new Dimension(160, 20));
        txtFax.setMaximumSize(new Dimension(160, 20));
        txtFax.setPreferredSize(new Dimension(160, 20));
        // Bug fix for case #1839 end 10        
        txtFax.setDocument(new LimitedPlainDocument(20));
        txtFax.setFont(fontFactory.getNormalFont());
        txtFax.setNextFocusableComponent(txtComments);
        txtFax.addKeyListener(new CoeusTextListener());
     txtFax.setFocusable(true);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 12;
        // commented for bug fix 1839
        // gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        // gridBagConstraints3.ipadx = 80;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(txtFax, gridBagConstraints3);        
        //Row 13 - End
        
        //Row 14 - Start
        //Comments Label
        lblComments = new JLabel();
        lblComments.setFont(fontFactory.getLabelFont());
        lblComments.setText("Comments: ");
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 13;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlRolodexDetails.add(lblComments, gridBagConstraints3);
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 13;
        gridBagConstraints3.gridwidth = 5;
//        // gridBagConstraints3.ipadx = 424;
//        // gridBagConstraints3.ipady = 80;
        gridBagConstraints3.insets = new Insets(2, 0, 2, 2);
//        //commented for bug fix 1839
//        // gridBagConstraints3.fill = java.awt.GridBagConstraints.VERTICAL;        

        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        pnlRolodexDetails.add(scrlPnComments, gridBagConstraints3);
        //scrlPnComments.setPreferredSize(new Dimension(424, 80));        
        //Row 14 - End
        //Moved for case#3341 - Sponsor Validation and UI realigmnent - End
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
//        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        centerPanel.add(pnlRolodexDetails);
//        pnlMain.add(centerPanel,BorderLayout.CENTER);
        pnlMain.add(pnlRolodexDetails,BorderLayout.CENTER);
       // this.setLayout(new BorderLayout(5,5));
        /**bug fix for case 1839 start 13  */
        this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
        /** bug fix for case 1839 end 13 */
        this.add(pnlMain);//,BorderLayout.CENTER);
    }

    /**
     *  Method used to set all the text fields with Roldex details retrieved.
     *
     */
    private void setRolodexData() {
        /* If the rolodexDetailsBean is null get the new instance */
        if (rldxBean == null) {
            rldxBean = new RolodexDetailsBean();
        }
        /* If the functionality is to show a Rolodex form for copying information
         * The SponsorAddressFlag by default should be "N".
         */
        if (functionType == 'C' ) {
            rldxBean.setSponsorAddressFlag("N");
        }
        txtRolodexId.setText(
                rldxBean.getRolodexId() == null ? "" : rldxBean.getRolodexId());
        String lastUpdateTime = rldxBean.getLastUpdateTime() == null ? null :
            rldxBean.getLastUpdateTime().toString();
        try {
            /* format the date retrieved into 10-Aug-2002 12:20 pm*/
            txtLastUpdate.setText(CoeusDateFormat.format(lastUpdateTime));
        }catch(Exception e){
            CoeusOptionPane.showInfoDialog(e.getMessage());
        }
//            txtUpdateUser.setText(
//            rldxBean.getLastUpdateUser() == null ? "" :
//                rldxBean.getLastUpdateUser());
            /*
             * UserID to UserName Enhancement - Start
             * Added UserUtils class to change userid to username
             */
        txtUpdateUser.setText(rldxBean.getLastUpdateUser() == null ? "" : UserUtils.getDisplayName(rldxBean.getLastUpdateUser()));
        // UserId to UserName Enhancement - End
        txtLastName.setText(
                rldxBean.getLastName()== null ? "" : rldxBean.getLastName());
        txtFirstName.setText(
                rldxBean.getFirstName() == null ? "" : rldxBean.getFirstName());
        txtMiddleName.setText(
                rldxBean.getMiddleName() == null ? "" : rldxBean.getMiddleName());
        txtSuffix.setText(
                rldxBean.getSuffix() == null ? "" : rldxBean.getSuffix());
        txtPrefix.setText(
                rldxBean.getPrefix() == null ? "" : rldxBean.getPrefix());
        txtTitle.setText(
                rldxBean.getTitle() == null ? "" : rldxBean.getTitle());
        txtSponsorCode.setText(
                rldxBean.getSponsorCode() == null ? "" : rldxBean.getSponsorCode());
        String sponsorName = rldxBean.getSponsorName() == null ? "" :
            rldxBean.getSponsorName();
        lblSponsorName.setText( trimSponsorName( sponsorName) );
        txtOrganization.setText(
                rldxBean.getOrganization() == null ? "" : rldxBean.getOrganization());
        txtAddress1.setText(
                rldxBean.getAddress1() == null ? "" : rldxBean.getAddress1());
        txtAddress2.setText(
                rldxBean.getAddress2() == null ? "" : rldxBean.getAddress2());
        txtAddress3.setText(
                rldxBean.getAddress3() == null ? "" : rldxBean.getAddress3());
        txtCity.setText(
                rldxBean.getCity() == null ? "" : rldxBean.getCity());
        txtCounty.setText(
                rldxBean.getCounty() == null ? "" : rldxBean.getCounty());
        Vector comboList =(
                rldxBean.getCountries() == null ? new Vector() : rldxBean.getCountries());
        /* Get all the comboBox beans from the Bean and set the
         * list of countries to country combo
         */
        //Modified by shiji for fixing bug id : 1843 : step 2 - start
        ComboBoxBean comboBean = new ComboBoxBean("","");
        comboList.add(0, comboBean);
        //bug id : 1843 : step 2 - end
        int comboLength = comboList.size();
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
            cmbCountry.addItem(listBox);
        }
        cmbCountry.setSelectedItem(
                rldxBean.getCountry() == null ? "" : rldxBean.getCountry());
        //Commented for Case#4252 - Rolodex state dropdown associated with country - Start
//        if (((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().
//                trim().equalsIgnoreCase("USA") ){
//            /* If the country selected is 'USA' then set the state combo with
//             * all the united states information
//             */
///*                        if(functionType != 'V'){
//                            cmbState.setShowCode(true);
//                        }*/
//            setStateInfo();
//        }else{
//            /* If the country selected is other than USA the clear the state
//             * combo with USA states and add the bean state information
//             */
//            //cmbState.setShowCode(false);
//            cmbState.removeAllItems();
//            ComboBoxBean cmbBean = rldxBean.getState() != null ?
//                new ComboBoxBean(rldxBean.getState(),rldxBean.getState())
//                : new ComboBoxBean("","");
//            cmbState.addItem(cmbBean);
//        }
        //Case#4252 - End
        cmbState.setSelectedItem(
                rldxBean.getState() == null ? "" : rldxBean.getState());
        txtPostalCode.setText(
                rldxBean.getPostalCode() == null ? "" : rldxBean.getPostalCode());
        txtPhone.setText(
                rldxBean.getPhone() == null ? "" : rldxBean.getPhone());
        txtEMail.setText(
                rldxBean.getEMail() == null ? "" : rldxBean.getEMail());
        txtFax.setText(rldxBean.getFax() == null ? "" : rldxBean.getFax());
        txtComments.setText(
                rldxBean.getComments() == null ? "" : rldxBean.getComments());
        txtComments.setCaretPosition(0);
        
    }
    
    /**
     *  Method used to set the state combo with all states information.
     * If the country selected is "USA" .
     */
    private void setStateInfo(){
        
        if (rldxBean == null) {
            rldxBean = new RolodexDetailsBean();
        }
        Vector comboList =(
                rldxBean.getStates() == null ? new Vector() : rldxBean.getStates());
        int comboLength = comboList.size();
        cmbState.removeAllItems();
        for(int comboIndex=0;comboIndex<comboLength;comboIndex++){
            ComboBoxBean listBox = (ComboBoxBean)comboList.elementAt(comboIndex);
            if(listBox!=null)
                cmbState.addItem(listBox);
        }
    }//RolodexMaintenanceDetailForm
    
    
    //supporting method to trim sponsor name length
    private String trimSponsorName( String sponsorName  ){
        String dispSponsorName = sponsorName ;
        if( dispSponsorName == null ){
            dispSponsorName = "";
        //Modified for case#3341 - Sponsor Code Validation - start  
        }else if( dispSponsorName.length() > 80 ){
            dispSponsorName = dispSponsorName.substring( 0, 80 ) + "..." ;
        }
        dispSponsorName = "  "+dispSponsorName;
        //Modified for case#3341 - Sponsor Code Validation - end
        return dispSponsorName;
    }
    
    
    /**
     *  Method used to clear the state combo if the country selected is
     * other than "USA" .
     */
    private void clearStateInfo(){
        cmbState.removeAllItems();
    }
    
    /**
     * Method used to set the RolodexDetails bean with the user
     * information entered in Window.
     * @return RolodexDetailsBean
     */
    public RolodexDetailsBean getRolodexInfo(){
        rldxBean.setRolodexId(txtRolodexId.getText());
        /*
         * for Bugfix Case#2745 begin
         * Instead of getting the updated user from the screen which gives updated
         * user name getting it from the RolodexBean which gives the updated user id
         */
        //rldxBean.setLastUpdateUser(txtUpdateUser.getText());
        rldxBean.setLastUpdateUser(rldxBean.getLastUpdateUser());
        //Bugfix Case#2745 end
        rldxBean.setLastName(txtLastName.getText());
        rldxBean.setMiddleName(txtMiddleName.getText());
        rldxBean.setFirstName(txtFirstName.getText());
        rldxBean.setSuffix(txtSuffix.getText());
        rldxBean.setPrefix(txtPrefix.getText());
        rldxBean.setTitle(txtTitle.getText());
        rldxBean.setSponsorCode(txtSponsorCode.getText());
        rldxBean.setOrganization(txtOrganization.getText());
        rldxBean.setAddress1(txtAddress1.getText());
        rldxBean.setAddress2(txtAddress2.getText());
        rldxBean.setAddress3(txtAddress3.getText());
        rldxBean.setCity(txtCity.getText());
        rldxBean.setCounty(txtCounty.getText());
        //Modified for Case#4252 - Rolodex state dropdown associated with country - Start
//        if ( ((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().equals("USA") ) {
//            if (cmbState.getSelectedItem() != null)
//                rldxBean.setState(((ComboBoxBean)cmbState.getSelectedItem()).getCode());
//            else
//                rldxBean.setState("");
//        }else{
//            String selectedState = "";
//            if (cmbState.getEditor().getEditorComponent() instanceof JTextField){
//                selectedState=
//                        ((JTextField)cmbState.getEditor().getEditorComponent()).getText();
//            }
//            rldxBean.setState(selectedState);
//        }
        String stateCode = ((ComboBoxBean)cmbState.getSelectedItem()) == null ? CoeusGuiConstants.EMPTY_STRING :
                            (((ComboBoxBean)cmbState.getSelectedItem()).getCode());
        rldxBean.setState(stateCode);
        //Case#4252 - End
        rldxBean.setCountry(((ComboBoxBean)cmbCountry.getSelectedItem()).getCode());
        rldxBean.setPostalCode(txtPostalCode.getText());
        rldxBean.setPhone(txtPhone.getText());
        rldxBean.setFax(txtFax.getText());
        rldxBean.setEMail(txtEMail.getText());
        rldxBean.setComments(txtComments.getText());
        if(radioActiveStatus.isSelected())
            rldxBean.setStatus(ACTIVE_STATUS);
        else if(radioInActiveStatus.isSelected())
            rldxBean.setStatus(INACTIVE_STATUS);
        return this.rldxBean;
        
    }
    
    /**
     *  Method used to save the Rolodex info to the database.
     */
    private void saveRolodexInfo() throws Exception{
        /* If the function Type is not 'V' then proceed */
        if  (functionType != 'V')  {
        	
        	// JM 7-11-2011 added to validated email format
        	String email = txtEMail.getText().trim();
        	txtEMail.setText(email);
        	if (txtEMail.getText().length() > 0) {
	        	EmailValidator ev = new EmailValidator(email);
	            if (!ev.validateEmail()) {
	                String msg = ev.errorMessage();
	                txtEMail.setText("");
	                txtEMail.requestFocus();
	                throw new Exception(msg);
	            }
        	}
        	// END        	
        	
            String  sponsorName = null;
            String sponsorCode =txtSponsorCode.getText().trim();
            if (!sponsorCode.trim().equals("")) {
                /* Get the sponsor name for the user entered sponsorCode if not
                 * available inform the user with error message
                 */
                
                /**
                 * Updated for REF ID :0003  Feb'21 2003.
                 * Hour Glass implementation while DB Trsactions Wait
                 * by Subramanya Feb' 21 2003
                 */
                dlgWindow.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
                sponsorName = rldxController.getSponsorName(sponsorCode);
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                SponsorMaintenanceFormBean sponsorMaintenanceFormBean = new SponsorMaintenanceFormBean();
                String sponsorStatus = rldxController.getSponsorStatus();
                dlgWindow.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                if ( (sponsorName ==null) || (sponsorName.trim().equals(CoeusGuiConstants.EMPTY_STRING)) && (!(SPONSOR_INACTIVE_STATUS.equals(sponsorStatus)))) {
                    String msg = txtSponsorCode.getText().trim() +
                            "  is invalid sponsor code. Please enter a valid sponsor code." ;
                    lblSponsorName.setText(CoeusGuiConstants.EMPTY_STRING);
                    txtSponsorCode.requestFocus();
                    throw new Exception(msg);
                }
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
            }
            lblSponsorName.setText( trimSponsorName( sponsorName ) );
            if (!txtOrganization.getText().trim().equals("") ) {
                //Modified for Case#4252 - Rolodex state dropdown associated with country - Start
//                boolean saveData =false;
                boolean saveData =true;
                    /* Check if the country is selected USA force the user
                     * to select state info
                     */
                
//                if (((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().equals("USA")) {
//                    if (((ComboBoxBean)cmbState.getSelectedItem()).getCode().equals("")) {
//                        saveData =false;
//                        throw new Exception(
//                                coeusMessageResources.parseMessageKey(
//                                "roldxMntDetFrm_exceptionCode.1105"));
//                        
//                    }else {
//                        boolean exists = false;
//                        Vector comboList = rldxBean.getStates();
//                        int comboLength = comboList.size();
//                        String selectedState=
//                                ((ComboBoxBean)cmbState.getSelectedItem()).getCode();
//                            /* Check the state info entered is in the list if
//                             * not prompt the error message
//                             */
//                        for(int comboIndex=0;comboIndex<comboLength;
//                        comboIndex++){
//                            ComboBoxBean listBox =
//                                    (ComboBoxBean)comboList.elementAt(comboIndex);
//                            if (listBox.getCode().toString().trim().equalsIgnoreCase(selectedState)) {
//                                exists =true;
//                            }
//                        }
//                        if (!exists) {
//                            cmbState.requestFocus();
//                            saveData =false;
//                            throw new Exception(
//                                    coeusMessageResources.parseMessageKey(
//                                    "roldxMntDetFrm_exceptionCode.1106"));
//                        }else {
//                            saveData =true;
//                        }
//                    }
//                }else {
//                    String selectedState = "";
//                    if (cmbState.getEditor().getEditorComponent() instanceof JTextField){
//                        //Modified for Case#4252 - Rolodex state dropdown associated with country - Start
////                        selectedState=
////                                ((JTextField)cmbState.getEditor().getEditorComponent()).getText();
//                         selectedState = ((ComboBoxBean)cmbState.getSelectedItem()) == null ? CoeusGuiConstants.EMPTY_STRING : ((ComboBoxBean)cmbState.getSelectedItem()).toString();
//                         //Case#4252 - End
//                    }
//                    if ( selectedState.trim().length() > 30) {
//                        throw new Exception(coeusMessageResources.parseMessageKey(
//                                "roldxMntDetFrm_exceptionCode.1144"));
//                    }else {
//                        saveData =true;
//                    }
//                }
                //Case#4252 - End
                
                if (saveData){
                    //connect to server and get org detail form
                    requester = new RequesterBean();
                    requester.setRequestedForm("Rolodex Details");
                    RolodexDetailsBean rdBean = getRolodexInfo();
                    if ( (functionType == 'I') || (functionType == 'C') ) {
                            /* for adding and copying the infor set requester
                             * function type as 'I' and set the Ac_Type as 'I'.
                             * This specifies the procedure to insert the record
                             */
                        requester.setFunctionType('I');
                        rdBean.setAcType("I");
                    }else {
                            /* for updating the info set requester function
                             * type as 'U' and set the Ac_Type as 'U'.This
                             * specifies the procedure to update the existing
                             * record.
                             */
                        requester.setFunctionType('U');
                        rdBean.setAcType("U");
                    }
                    requester.setDataObject(rdBean);
                    /**
                     * Updated for REF ID :0003  Feb'21 2003.
                     * Hour Glass implementation while DB Trsactions Wait
                     * by Subramanya Feb' 21 2003
                     */
                    dlgWindow.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
                    ResponderBean res =
                            rldxController.sendToServer("/rolMntServlet",requester);
                    dlgWindow.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                    
                    this.rldxBean = (RolodexDetailsBean)res.getDataObject();
                    //added by ravi for implementing oberserver pattern - START
                    String stateName = "";
                    if (cmbState.getSelectedItem() != null){
                        stateName = (((ComboBoxBean)cmbState.getSelectedItem()).getDescription());
                    }
                    rldxBean.setState( stateName );
                    rldxBean.setCountry( ((ComboBoxBean)cmbCountry.getSelectedItem()).getDescription() );
                    observable.setFunctionType(functionType);
                    observable.notifyObservers(rldxBean);
                    // added by ravi - END
                    //coeusqa-1528 start
                    if(radioInActiveStatus.isSelected()) {
                        rldxBean.setStatus(INACTIVE_STATUS);
                    }
                    else if(radioActiveStatus.isSelected()) {
                        rldxBean.setStatus(ACTIVE_STATUS);
                    }
                    //coeusqa-1528 end
                    if (res != null ) {
                    }
                    if (!res.isSuccessfulResponse()){
                        CoeusOptionPane.showErrorDialog(res.getMessage());
                        if (res.isCloseRequired()) {
                            releaseUpdateLock();
                            dlgWindow.dispose();
                        }else {
                            return;
                        }
                    }
                    dataSaved = true;
                    releaseUpdateLock();
                    dlgWindow.dispose();
                }
            }else {
                txtOrganization.requestFocus();
                throw new Exception(coeusMessageResources.parseMessageKey(
                        "roldxMntDetFrm_exceptionCode.1107"));
            }
            //}
        }else {
            releaseUpdateLock();
            dlgWindow.dispose();
        }
    }
    
    
    
    /**  Method used to the set Rolodex details to the form controls.
     * @param inFrame JFrame
     * @param name title of the frame
     * @param isModal boolean, true if the window is modal window else false
     */
    public void showForm(JFrame inFrame,String name,boolean isModal){
        this.mdiForm = (CoeusAppletMDIForm)inFrame;
        //dlgRolMaint = new JDialog(inFrame,name,isModal);
        dlgWindow = new CoeusDlgWindow(inFrame,name,isModal){
            protected JRootPane createRootPane() {
                ActionListener actionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        try{
                            if (dataChanged) {
                                try{
                                    int result = CoeusOptionPane.showQuestionDialog(
                                            coeusMessageResources.parseMessageKey(
                                            "saveConfirmCode.1002"),
                                            CoeusOptionPane.OPTION_YES_NO_CANCEL,
                                            CoeusOptionPane.DEFAULT_YES);
                                    if (result == JOptionPane.YES_OPTION) {
                                        saveRolodexInfo();
                                    }else if (result == JOptionPane.NO_OPTION){
                                        releaseUpdateLock();
                                        dlgWindow.dispose();
                                    }
                                }catch(Exception e){
                                    CoeusOptionPane.showErrorDialog(e.getMessage());
                                }
                            }else {
                                releaseUpdateLock();
                                dlgWindow.dispose();
                            }
                        }catch(Exception ex){
                            CoeusOptionPane.showErrorDialog(ex.getMessage());
                        }
                    }
                };
                JRootPane rootPane = new JRootPane();
                KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
                        0);
                rootPane.registerKeyboardAction(actionListener, stroke,
                        JComponent.WHEN_IN_FOCUSED_WINDOW);
                return rootPane;
            }//RolodexMaintenanceDetailForm
        };
        JPanel dlgPanel  = (JPanel)this.getRolodexComponent();
        dlgWindow.getRootPane().setDefaultButton(btnOK);
        dlgWindow.getContentPane().add(dlgPanel);
        dlgWindow.pack();
        //dlgWindow.setSize(660,450);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgWindow.getSize();
        dlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;
        /* This will catch the window closing event and checks any data is
         * modified.If any changes are done by the user the system will ask for
         * confirmation of Saving the info.
         */
        dlgWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        dlgWindow.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent evnt){
                dataChanged = false;
                if (functionType == 'V') {
                    btnCancel.setRequestFocusEnabled(true);
                    btnCancel.requestFocus();
                }else {
                    txtLastName.setRequestFocusEnabled(true);
                    txtLastName.requestFocus();
                }
            }
            public void windowClosing(WindowEvent evnt){
                performWindowClosing();
            }
        });
        dlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performWindowClosing();
            }
        });
        dlgWindow.setResizable(false);
        dlgWindow.show();
    }
    
    //This method called when "Esc" or normal window closing event is performed....
    private void performWindowClosing() {
        if (dataChanged) {
            try {
                int result = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                if (result == JOptionPane.YES_OPTION) {
                    saveRolodexInfo();
                }else if (result == JOptionPane.NO_OPTION){
                    releaseUpdateLock();
                    dlgWindow.dispose();
                }
            }catch(Exception e){
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }else {
            releaseUpdateLock();
            dlgWindow.dispose();
        }
    }
    
    
    /**
     * Method used to display the Sponsor info on double clicking in
     * txtsponsorCode text field.
     */
    private void showSponsorInfo(){
        if (txtSponsorCode.getText().equals("") ){
            showSponsorSearch();
        }else {
            SponsorMaintenanceForm frmSponsor = new SponsorMaintenanceForm('D',
                    txtSponsorCode.getText().toString().trim());
            frmSponsor.showForm(mdiForm,DISPLAY_TITLE,true);
            
        }
        isSponsorSearchRequired =false;
    }
    
    /**
     * Method used to show sponsor search screen when user double clicks
     * in txtSponsorCode field.
     * and get the SponsorInformation for the selected sponsorCode.
     */
    private void showSponsorSearch(){
        String sponsorCode;
//        boolean replaceInfo = false;
        try{
            edu.mit.coeus.search.gui.CoeusSearch coeusSearch =
                    new edu.mit.coeus.search.gui.CoeusSearch(dlgWindow,
                    "sponsorSearch",1);
            coeusSearch.showSearchWindow();
            edu.mit.coeus.search.gui.SearchResultWindow resWindow =
                    coeusSearch.getResultWindow();
            if (!coeusSearch.getSelectedValue().equals(null) ){
                txtSponsorCode.setText(coeusSearch.getSelectedValue());
                txtSponsorCode.requestFocusInWindow();
                sponsorCode = txtSponsorCode.getText();
                getSponsorInfo(sponsorCode);
            }
        }catch(Exception e) {
        }
    }
    
    /**
     * Method used to set the controls enabled or disabled based on
     * function type.
     */
    private void setControls(char functionType){
        if (functionType == 'I' ) {
            setControlsEnabled(true);
            txtRolodexId.setEditable(false);
            txtLastUpdate.setEditable(false);
            txtUpdateUser.setEditable(false);
        }else if (functionType == 'M') {
            setControlsEnabled(true);
            txtRolodexId.setEditable(false);
            txtLastUpdate.setEditable(false);
            txtUpdateUser.setEditable(false);
        }else if (functionType == 'V') {
            setControlsEnabled(false);
            btnOK.setEnabled(false);
            btnCancel.setEnabled(true);
            btnSponsor.setEnabled(false);
            txtComments.setOpaque(false);
        }else if (functionType == 'C') {
            setControlsEnabled(true);
            txtRolodexId.setText("");
            txtLastUpdate.setText("");
            txtUpdateUser.setText("");
            txtRolodexId.setEditable(false);
            txtLastUpdate.setEditable(false);
            txtUpdateUser.setEditable(false);
        }else if (functionType == 'N') {
            setControlsEnabled(false);
            txtComments.setOpaque(false);
        }
        
        
    }
    
    /**
     *  Method used to set the all the controls enabled false/true
     */
    private void setControlsEnabled(boolean value){
        txtRolodexId.setEditable(value);
        txtLastUpdate.setEditable(value);
        txtUpdateUser.setEditable(value);
        txtLastName.setEditable(value);
        txtFirstName.setEditable(value);
        txtMiddleName.setEditable(value);
        txtSuffix.setEditable(value);
        txtPrefix.setEditable(value);
        txtTitle.setEditable(value);
        txtSponsorCode.setEditable(value);
        txtOrganization.setEditable(value);
        txtAddress1.setEditable(value);
        txtAddress2.setEditable(value);
        txtAddress3.setEditable(value);
        txtCity.setEditable(value);
        txtCounty.setEditable(value);
        //Modified for case#3278 - State Combobox is made non editable
        cmbState.setEditable(false);
        cmbState.setEnabled(value);
        cmbState.setForeground(Color.black);
        cmbCountry.setEnabled(value);
        txtPostalCode.setEditable(value);
        txtPhone.setEditable(value);
        txtEMail.setEditable(value);
        txtFax.setEditable(value);
        txtComments.setEditable(value);
    }
    
    /**
     *  Method used to set all the controls back groung color to gray.
     */
//    private void setControlsBgColour(Color value){
//        //java.awt.color
//        txtRolodexId.setBackground(value);
//        txtLastUpdate.setBackground(value);
//        txtUpdateUser.setBackground(value);
//        txtLastName.setBackground(value);
//        txtFirstName.setBackground(value);
//        txtMiddleName.setBackground(value);
//        txtSuffix.setBackground(value);
//        txtPrefix.setBackground(value);
//        txtTitle.setBackground(value);
//        txtSponsorCode.setBackground(value);
//        txtOrganization.setBackground(value);
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
//        txtComments.setBackground(value);
//    }
    
    /**
     * Method used to set the dataChanged to true if any user changes data
     * in any field in rolodexDetails screen.
     */
    private void userDataChanged() {
        // Add your handling code here:
        dataChanged = true;
    }
    
    /**
     *  Method used to cancel all the operation on confirming the user
     */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        if (dataChanged) {
            /* Ask for confirmation by the user to save the information entered
             * in the database */
            int resultConfirm = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            if (resultConfirm == 0) {
                try {
                    saveRolodexInfo();
                }catch(Exception e){
                    releaseUpdateLock();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if (resultConfirm == 1) {
                dataSaved = false;
                releaseUpdateLock();
                dlgWindow.dispose();
            }else {
                return;
            }
        }else{
            releaseUpdateLock();
            dlgWindow.dispose();
        }
    }
    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /**  Method used to refresh the rolodex sheet
     *  after adding a new rolodex information or modifying an existing rolodex information
     * @return  Vector containing Rolodex details
     */
    /*protected Vector getRolodexRowData(){
     
        Vector rowData = null;
        if( dataSaved ) {
            String stateName ="";
            if (cmbState.getSelectedItem() != null)
                stateName = (((ComboBoxBean)cmbState.getSelectedItem()).getDescription());
            else
                stateName = "";
            if ( (rldxBean !=null ) && (rldxBean.getRolodexId() != null)
            && !rldxBean.getRolodexId().equals("")) {
                rowData = new Vector();
                rowData.add(rldxBean.getRolodexId());
                rowData.add(rldxBean.getSponsorCode());
                rowData.add(rldxBean.getLastName());
                rowData.add(rldxBean.getFirstName());
                rowData.add(rldxBean.getMiddleName());
                rowData.add(rldxBean.getSuffix());
                rowData.add(rldxBean.getPrefix());
                rowData.add(rldxBean.getTitle());
                rowData.add(rldxBean.getOrganization());
                rowData.add(rldxBean.getAddress1());
                rowData.add(rldxBean.getAddress2());
                rowData.add(rldxBean.getAddress3());
                rowData.add(rldxBean.getFax());
                rowData.add(rldxBean.getEMail());
                rowData.add(rldxBean.getCounty());
                rowData.add(rldxBean.getCity());
                rowData.add(rldxBean.getState());
                rowData.add(stateName);
                rowData.add(rldxBean.getPostalCode());
                rowData.add(rldxBean.getComments());
                rowData.add(rldxBean.getPhone());
                rowData.add(((ComboBoxBean)cmbCountry.getSelectedItem()).getDescription());
     
            }
        }
        return rowData;
    }*/
    
    /**
     * This class is to fire the key events in all the text boxes to check
     * whether user
     * changed any information before closing the window.
     *
     */
    
    class CoeusTextListener extends KeyAdapter {
        public void keyReleased(KeyEvent ke) {
            //****************************************************
            if ((functionType != 'V') && (ke.getKeyCode() != KeyEvent.VK_ESCAPE)) {
                userDataChanged();
            }
            //**************************************************
        }
    }
    
    /**
     * Method used to replace the sponsor info on selecting sponsor code by the
     * user.
     */
    private void getSponsorInfo(String sponsorCode){
        int resultConfirm =0;
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        if( dlgWindow != null){
            dlgWindow.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        }
        String sponsorName = rldxController.getSponsorName(sponsorCode);
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        String sponsorStatus = rldxController.getSponsorStatus();
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        if( dlgWindow != null ){
            dlgWindow.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }
        
        //Commented for case#3341 - Sponsor Code Validation - start
//        lblSponsorName.setText( trimSponsorName( sponsorName ) );
//        txtOrganization.requestFocus();
        //Commented for case#3341 - Sponsor Code Validation - end
        if ((sponsorName != null) && (!sponsorName.trim().equals("")) && ! INACTIVE_STATUS.equalsIgnoreCase(sponsorStatus)){
            //Added for case#3341 - Sponsor Code Validation - start
            lblSponsorName.setText(trimSponsorName(sponsorName));
            txtOrganization.requestFocus();
            //Added for case#3341 - Sponsor Code Validation - end            
            if  ( (!txtOrganization.getText().trim().equals("")) &&
                    ( !sponsorName.equals(txtOrganization.getText().trim()) ) ){
                String msgStr = "Do you want to overwrite the organization: " +
                        txtOrganization.getText().trim() + " with " + sponsorName + "?";
                resultConfirm = CoeusOptionPane.showQuestionDialog(msgStr,
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                if (resultConfirm == 0) {
                    txtOrganization.setText(sponsorName.trim());
                }
                
            }else {
                txtOrganization.setText(sponsorName.trim());
                //do nothing
            }
            if (!txtSponsorCode.getText().trim().equals("")  ) {
                if ( !txtAddress1.getText().trim().equals("") ||
                        !txtAddress2.getText().trim().equals("") ||
                        !txtAddress3.getText().trim().equals("") ||
                        !txtCity.getText().trim().equals("") ||
                        !txtCounty.getText().trim().equals("") ||
                        //Commented for Case#4252 - Rolodex state dropdown associated with country - Start
//                        !((ComboBoxBean)cmbCountry.getItemAt(
//                        cmbCountry.getSelectedIndex())).getCode().trim().equals("USA") ||
                        //Case#4252 - End
                        !txtPostalCode.getText().trim().equals("") ||
                        !txtPhone.getText().trim().equals("") ||
                        !txtEMail.getText().trim().equals("") ||
                        !txtFax.getText().trim().equals("") ) {
                    
                    String msgStr =
                            coeusMessageResources.parseMessageKey(
                            "roldxMntDetFrm_confirmationCode.1145");
                    resultConfirm = CoeusOptionPane.showQuestionDialog(msgStr,
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                    /* prompt the user for replacing base address on
                     * confirmation replace the address
                     */
                        replaceWithSponsorBaseAdress(sponsorCode);
                    }
                }else {
                    replaceWithSponsorBaseAdress(sponsorCode);
                }
            }
            //Added for case#3341 - Sponsor Code Validation
            txtOrganization.requestFocus();  
        //Added for case#3341 - Sponsor Code Validation - start    
        }else{
            lblSponsorName.setText("");
            txtSponsorCode.setText("");
            txtSponsorCode.requestFocus();            
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
            txtOrganization.setText(CoeusGuiConstants.EMPTY_STRING);            
            txtAddress1.setText(CoeusGuiConstants.EMPTY_STRING);
            txtAddress2.setText(CoeusGuiConstants.EMPTY_STRING);
            txtAddress3.setText(CoeusGuiConstants.EMPTY_STRING);
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("roldxMntDetFrm_exceptionCode.1108"));            
        }
        //Added for case#3341 - Sponsor Code Validation - end
        isSponsorInfoRequired =false;
        //Commented for case#3341 - Sponsor Code Validation
        //txtOrganization.requestFocus();        
    }
    
    /**
     *  Method used to replace the rolodex addres with the sponsor base address.
     */
    private void replaceWithSponsorBaseAdress(String sponsorCode){
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        dlgWindow.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        String rldxId = rldxController.getRolodexIdForSponsor(sponsorCode);
        RolodexDetailsBean rolodexBean =
                rldxController.displayRolodexInfo(rldxId);
        dlgWindow.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        txtAddress1.setText(
                rolodexBean.getAddress1() == null ? "" : rolodexBean.getAddress1());
        txtAddress2.setText(
                rolodexBean.getAddress2() == null ? "" : rolodexBean.getAddress2());
        txtAddress3.setText(
                rolodexBean.getAddress3() == null ? "" : rolodexBean.getAddress3());
        txtCity.setText(
                rolodexBean.getCity() == null ? "" : rolodexBean.getCity());
        txtCounty.setText(
                rolodexBean.getCounty() == null ? "" : rolodexBean.getCounty());
        //Modified for Case#4252 - Rolodex state dropdown associated with country - Start
        cmbCountry.setSelectedItem(rolodexBean.getCountry());
//        cmbState.setSelectedItem(rolodexBean.getState());
//        if (((ComboBoxBean)cmbCountry.getSelectedItem()).getCode().trim().equals("USA") ){
////            cmbState.setShowCode(true);
//            setStateInfo();
//        }else{
////            cmbState.setShowCode(false);
//            cmbState.removeAllItems();
//            ComboBoxBean cmbBean = rolodexBean.getState() != null ?
//                new ComboBoxBean(rolodexBean.getState(),rolodexBean.getState())
//                : new ComboBoxBean(" "," ");
//            cmbState.addItem(cmbBean);
//        }
        //Case#4252 - End
        cmbState.setRequestFocusEnabled(false);
        cmbState.setSelectedItem(
                rolodexBean.getState() == null ? " " :rolodexBean.getState().toString());
        txtPostalCode.setText(
                rolodexBean.getPostalCode() == null ? "" : rolodexBean.getPostalCode());
        txtPhone.setText(
                rolodexBean.getPhone() == null ? "" : rolodexBean.getPhone());
        txtEMail.setText(
                rolodexBean.getEMail() == null ? "" : rolodexBean.getEMail());
        txtFax.setText(
                rolodexBean.getFax() == null ? "" : rolodexBean.getFax());
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object srcAction = actionEvent.getSource();
        if( srcAction.equals( btnOK ) ){
            try {
                /*Call the saveRolodexInfo method to save the user
                 * entered/modified
                 * information
                 */
                saveRolodexInfo();
            } catch (Exception e){
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }else if( srcAction.equals( btnCancel ) ){
                /* Call the  btnCancelActionPerformed method to save the user
                 * entered/modified information
                 */
            btnCancelActionPerformed( actionEvent );
        }else if( srcAction.equals( btnSponsor )){
            /* Call showSponsorSearch method to open Sponsor search window*/
            showSponsorSearch();
        }else if( srcAction.equals( txtSponsorCode ) ){
            // Updated by Subramanya Nov' 6 2002
            //updated for Enter Key Action Event Capturing.
            
                /*set issponsorSearchRequired to true to avoid firing in
                 *focusLost event
                 */
            isSponsorSearchRequired =true;
            isSponsorInfoRequired = true;
            showSponsorInfo();
            
        }
        
    }
    
    private void releaseUpdateLock(){
        try{
            //connect to server and get org detail form
//            String refId = null;
            if ( functionType == 'M' ) {
                String rowId = this.txtRolodexId.getText();
                RequesterBean requester = new RequesterBean();
                requester.setDataObject(rowId);
                requester.setFunctionType('Z');
                String connectTo =CoeusGuiConstants.CONNECTION_URL+"/rolMntServlet";
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                comm.send();
                ResponderBean res = comm.getResponse();
                if (res != null && !res.isSuccessfulResponse()){
                    CoeusOptionPane.showErrorDialog(res.getMessage());
                    return;
                }
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    public void setRolodexId(String rolodexId){
        this.rolodexId=rolodexId;
    }
    public String getRolodexId() {
        return rolodexId;
    }
 //COEUSQA-1528 - START Shabarish
 /*
  *
  This function is checks whether the user has rights to Modify,view and Maintain Rolodex
  */
    private boolean checkCanModifyStatus() {
        
            RequesterBean requesterBean = new RequesterBean();
            ResponderBean responderBean = new ResponderBean();
        try{
            requesterBean.setFunctionType('c');
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/rolMntServlet";
            AppletServletCommunicator ascomm = new AppletServletCommunicator(connectTo,requesterBean);
            ascomm.setRequest(requesterBean);
            ascomm.send();
            responderBean = ascomm.getResponse();     
           
        }catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
         return (Boolean)responderBean.getDataObject();
    }
 //COEUSQA-1528 - END   

    private void formatFields() {
        if(canModifyStatus) {
            radioActiveStatus.setEnabled(true);
            radioInActiveStatus.setEnabled(true);
        }else {
            radioActiveStatus.setEnabled(false);
            radioInActiveStatus.setEnabled(false);
        } 
    }
     //coeusqa-1528 start
    private void viewEditRadioBtn(char funcType) {
        if(funcType=='V' || funcType=='N') {
            radioActiveStatus.setEnabled(false);
            radioInActiveStatus.setEnabled(false);
        }         
    }
     //coeusqa-1528 end
}
