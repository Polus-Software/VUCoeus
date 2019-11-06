/*
 * @(#)UnitHierarchyForm.java  August 27, 2002, 11:03 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 26-JAN-2011
 * by Bharati
 */

package edu.mit.coeus.unit.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.beans.*;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.unit.bean.UnitHierarchyFormBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.tree.DnDJTree;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.organization.gui.DetailForm;
import edu.mit.coeus.unit.bean.UnitAdministratorBean;
import edu.mit.coeus.utils.CoeusUtils;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;

import java.util.Observer;


/**
 *
 * Created on August 27, 2002, 11:03 AM
 * @author  Geo Thomas
 * @version 1.0
 */
public class UnitDetailForm extends JComponent
implements ActionListener, KeyListener, MouseListener, FocusListener{
    //Holds UnitDetailFormBean instance
    private UnitDetailFormBean unitDetail;
    //holds admin officer id
    private String adminOfficerId;
    //hods dean vp id
    private String deanVpId;
    //holds the person id of other individual
    private String otherIndToNotifyId;
    //holds the person id of unit head
    private String unitHeadId;
    //holds the person id of osp admin
    private String ospAdminId;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user
    private String updateUser;
    //holds functionality type
    private char functionalityType;
    
    //added for unit detail - organization search - 1
    //holds Organization Id
    private String organizationId;
    //added for unit detail - organization search - 1
    
    //Holds the Function Type Constant
    private final char DISPALY_MODE = 'G' ;
    
    //holds unithierarchy
    private UnitHierarchyFormBean unitHierarchy;
    //holds unithierarchy tree
    private DnDJTree unitHierarchyTree;
    //holds connection string to unitservlet
    private final String UNIT_CONNECTION_URL =
                    CoeusGuiConstants.CONNECTION_URL+"/unitServlet";
    //holds normal font
    private Font normalFont = CoeusFontFactory.getNormalFont();
    //holds label font
    private Font labelFont = CoeusFontFactory.getLabelFont();
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    //Bug Fix: For validating the data on focus lost Start 1
    private final char PERSON_VALIDATE = 'J';
    private String oldAdminOffName = "";
    private String oldUnitHeadName = "";
    private String oldDeanVPName = "";
    private String oldOtherIndvName = "";
    private String oldOSPAdminName = "";
    
    //added for unit detail - organization search - 2
    private String oldOrganization = "";
    //added for unit detail - organization search - 2
    /*Added for unit hierarchy enhancement*/
    public static final int UNIT_DETAIL_TAB = 0;
    public static final int ADMINISTRATOR_TAB = 1;
    private UnitAdminTypeForm unitAdminTypeForm;
    private static final char UPDATE_ADMIN_TYPE = 'L'; 
    private CoeusVector adminDataObjects;
    private CoeusVector cvAdminData = null;//new CoeusVector();
    public CoeusVector cvDataToServer = new CoeusVector();
    public boolean unitAdminTypeFlag = false;
    /** END HERE*/
    //Bug Fix: For validating the data on focus lost End 1
    
    //added for unit detail - organization search - 3
    private final char ORGANIZATION_VALIDATE = 'N';
    //added for unit detail - organization search - 3
    
    // JM 7-17-2015 is user application administrator
    private boolean isAdmin = false;
    
    /** Creates new form UnitDetailForm */
    public UnitDetailForm() {
        initComponents();
        attachValues();
    }
    /**
     *  Creates new form UnitDetailForm with unit number and functionality type
     *  @param unitNumber unit number
     *  @param functionalityType funictionality type
     *  @throws Exception while constructing this form.
     */
    public UnitDetailForm(String unitNumber, char functionalityType)
    throws Exception{
        this.functionalityType = functionalityType;
        this.unitDetail = getResponseData(unitNumber);
        initComponents();
        attachValues();
        //Added for unit hierarchy enhancement start 1 by tarique
        getAdministratorType(unitNumber);
        //Added for unit hierarchy enhancement end 1 by tarique
    }
    /**
     *  Creates new form with UnitDetailFormBean instance and functioanlity type
     *  @param unitDetail UnitDetailFormBean
     *  @param functionalityType functionality type
     *  @throws Exception while constructing this form.
     */
    public UnitDetailForm(UnitDetailFormBean unitDetail, char functionalityType)
    throws Exception{
        this.unitDetail = unitDetail;
        this.functionalityType = functionalityType;
        initComponents();
        attachValues();
        //Added for unit hierarchy enhancement start 2 by tarique
        getAdministratorType(unitDetail.getUnitNumber());
        //Added for unit hierarchy enhancement end 2 by tarique
        
    }
    /**
     *  Creates new form with UnitHierarchyFormBean and functionality type
     *  @param unitHierarchy UnitHierarchyFormBean
     *  @param functionalityType functionality type
     *  @throws Exception while constructing this form.
     */
    public UnitDetailForm(UnitHierarchyFormBean unitHierarchy,
    char functionalityType) throws Exception{
        this.unitHierarchy = unitHierarchy;
        this.functionalityType = functionalityType;
        
        switch(functionalityType){
            case('U'):
            case('G'):
                if((unitDetail = unitHierarchy.getUnitDetail())==null){
                    unitDetail = getResponseData(unitHierarchy.getNodeID());
                }
                unitDetail.setUnitNumber(unitHierarchy.getNodeID());
                //Added for unit hierarchy enhancement start 3 by tarique
                getAdministratorType(unitDetail.getUnitNumber());
                
                //Added for unit hierarchy enhancement end 3 by tarique
                unitDetail.setUnitName(unitHierarchy.getNodeName());
                unitDetail.setParentUnitNumber(unitHierarchy.getParentNodeID());
                unitDetail.setParentUnitName(unitHierarchy.getParentNodeName());
                break;
            case('I'):
                unitDetail = new UnitDetailFormBean();
                unitDetail.setParentUnitNumber(unitHierarchy.getNodeID());
                unitDetail.setParentUnitName(unitHierarchy.getNodeName());
                //Added for unit hierarchy enhancement start 4 by tarique
                getAdministratorType(null);
                
                //Added for unit hierarchy enhancement end 4 by tarique
                break;
        }
        
        checkIsAdmin(); // JM 7-17-2015 check if unit has application administrator role
        initComponents();
        attachValues();
    }
    
    /**
     *  Set <code>UnitHierarchyFormBean</code> instance.
     *  @param unitHierarchy UnitHiearchy node data
     */
    public void setUnitHierarchyBean(UnitHierarchyFormBean unitHierarchy){
        this.unitHierarchy = unitHierarchy;
    }
    
    /**
     *  Get UnitHierarchyFormBean
     *  @return UnitHierarchyFormBean unit hierarchy node data
     */
    public UnitHierarchyFormBean getUnitHierarchyBean(){
        return unitHierarchy;
    }
    
    /**
     *  Set <code>DnDJTree component</code> instance.
     *  @param unitHierarchyTree UnitHiearchy tree
     */
    public void setUnitHierarchyTree(DnDJTree unitHierarchyTree){
        this.unitHierarchyTree = unitHierarchyTree;
    }
    
    /**
     *  Get UnitHierarchy tree
     *  @return unit hierarchy tree
     */
    public DnDJTree getUnitHierarchyTree(){
        return unitHierarchyTree;
    }
    
    /**
     *  Set administarator officer id
     *  @param adminOfficerId person id of administrator
     */
    public void setAdminOfficerId(String adminOfficerId){
        this.adminOfficerId = adminOfficerId;
    }
    /**
     *  Set <code>dean vp id</id>
     *  @param deanVpId person id of dean vp
     */
    public void setDeanVpId(String deanVpId){
        this.deanVpId = deanVpId;
    }
    /**
     *  Set <code> OtherIndividualToNotify </code> person id
     *  @param otherIndToNotifyId person id of other individual to notify
     */
    public void setOtherIndToNotifyId(String otherIndToNotifyId){
        this.otherIndToNotifyId = otherIndToNotifyId;
    }
    /**
     *  Set unit head id
     *  @param unitHeadId person id of unit head
     */
    public void setUnitHeadId(String unitHeadId){
        this.unitHeadId = unitHeadId;
    }
    /**
     *  Set OSP admin id
     *  @param ospAdminId person id of OSP administrator
     */
    public void setOspAdminId(String ospAdminId){
        this.ospAdminId = ospAdminId;
    }
    
    /**
     * Set Organization Id
     */
    public void setOrganizationId(String organizationId){
        this.organizationId =  organizationId;        
    }
    
    /*
     *  Method to attach values to the form. It will take the unit detail bean
     *  and attach the data to the corresponding fields
     */
    private void attachValues(){
        if(unitDetail==null)
            return;
        String parent = (unitDetail.getParentUnitNumber()+
        " : "+ unitDetail.getParentUnitName());
        this.txtParentUnit.setText(parent);
        this.txtParentUnit.setCaretPosition(0);
        this.txtUnitNumber.setText(unitDetail.getUnitNumber());
        this.txtAdminOfficer.setText(unitDetail.getAdminOfficerName());
        setAdminOfficerId(unitDetail.getAdminOfficerId());
        this.txtDeanVp.setText(unitDetail.getDeanVpName());
        setDeanVpId(unitDetail.getDeanVpId());
        this.txtOtherIndToNotify.setText(unitDetail.getOtherIndToNotifyName());
        setOtherIndToNotifyId(unitDetail.getOtherIndToNotifyId());
        //this.txtParentUnit.setText(unitDetail.getParentUnitNumber());
        this.txtUnitHead.setText(unitDetail.getUnitHeadName());
        setUnitHeadId(unitDetail.getUnitHeadId());
        this.txtUnitName.setText(unitDetail.getUnitName());
        this.txtOspAdmin.setText(unitDetail.getOspAdminName());
        this.txtOrganization.setText(unitDetail.getOrganizationName());
        setOspAdminId(unitDetail.getOspAdminId());
        this.updateTimestamp = unitDetail.getUpdateTimestamp();
        this.updateUser = unitDetail.getUpdateUser();
        setOrganizationId(unitDetail.getOrganizationId());
        //adding listeners
        this.txtUnitNumber.addKeyListener(this);
        this.txtUnitName.addKeyListener(this);
        this.txtAdminOfficer.addKeyListener(this);
        this.txtDeanVp.addKeyListener(this);
        this.txtOtherIndToNotify.addKeyListener(this);
        this.txtUnitHead.addKeyListener(this);
        this.txtOspAdmin.addKeyListener(this);
        
        //added for unit detail - organization search - 4
        this.txtOrganization.addKeyListener(this);
        //added for unit detail - organization search - 4
        
        // Added by chandra 17/04/2004 - Start
        this.txtAdminOfficer.addMouseListener(this);
        this.txtDeanVp.addMouseListener(this);
        this.txtOtherIndToNotify.addMouseListener(this);
        this.txtUnitHead.addMouseListener(this);
        this.txtOspAdmin.addMouseListener(this);
        // Added by chandra 17/04/2004 - End
        
        this.btnSearch1.addActionListener(this);
        this.btnSearch2.addActionListener(this);
        this.btnSearch3.addActionListener(this);
        this.btnSearch4.addActionListener(this);
        this.btnSearch5.addActionListener(this);
        
        //added for unit detail - organization search - 5
        this.btnOrganizationSearch.addActionListener(this);
        this.txtOrganization.addMouseListener(this);
        //added for unit detail - organization search - 5
        
        /** commented for unit hierarchy enhancement start*/
        //this.btnOK.addActionListener(this);
        //this.btnCancel.addActionListener(this);
        //commented for unit hierarchy enhancement end by tarique
        
        //Bug Fix: For validating the data on focus lost Start 2
        if(txtAdminOfficer.getText() !=null && !txtAdminOfficer.getText().trim().equals("")){
            oldAdminOffName = txtAdminOfficer.getText().trim();
        }
        if(txtUnitHead.getText() !=null && !txtUnitHead.getText().trim().equals("")){
            oldUnitHeadName = txtUnitHead.getText().trim();
        }
        if(txtDeanVp.getText() !=null && !txtDeanVp.getText().trim().equals("")){
            oldDeanVPName = txtDeanVp.getText().trim();
        }
        if(txtOtherIndToNotify.getText() !=null && !txtOtherIndToNotify.getText().trim().equals("")){
            oldOtherIndvName = txtOtherIndToNotify.getText().trim();
        }
        if(txtOspAdmin.getText() !=null && !txtOspAdmin.getText().trim().equals("")){
            oldOSPAdminName = txtOspAdmin.getText().trim();
        }
        
        //added for unit detail - organization search - 6
        if(txtOrganization.getText() !=null && !txtOrganization.getText().trim().equals("")){
            oldOrganization = txtOrganization.getText().trim();
        }
        //added for unit detail - organization search - 6
        
        txtAdminOfficer.addFocusListener(this);
        txtUnitHead.addFocusListener(this);
        txtDeanVp.addFocusListener(this);
        txtOtherIndToNotify.addFocusListener(this);
        txtOspAdmin.addFocusListener(this);
        
        //added for unit detail - organization search - 7
        txtOrganization.addFocusListener(this);
        //added for unit detail - organization search - 7
        
        //Bug Fix: For validating the data on focus lost End 2
        
        // JM 7-16-2015 set combobox status as brought in from db
        setComboBoxSelection(unitDetail.getStatus(),cmbStatus);
        cmbStatus.addActionListener(this);
        // JM END
    }
    
    // JM 7-16-2015 method to set combobox value
    public void setComboBoxSelection(String code, JComboBox comboBox){
        ComboBoxBean comboBoxBean = null;
        for(int i=0; i<comboBox.getModel().getSize(); i++){
            comboBoxBean = (ComboBoxBean) comboBox.getItemAt(i);
            if(comboBoxBean.getCode().equals(code)){
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }
    // JM END
    
    // JM 7-16-2015 method to get combobox value
    public String getComboBoxSelection(int index, JComboBox comboBox){
        ComboBoxBean comboBoxBean = null;
        comboBoxBean = (ComboBoxBean) comboBox.getItemAt(index);
        return comboBoxBean.getCode();
    }
    // JM END
    
    // JM 7-17-2015 check if user is application administrator and if so show status
    public void checkIsAdmin() {
	    edu.vanderbilt.coeus.utils.UserPermissions perm = new edu.vanderbilt.coeus.utils.UserPermissions(unitDetail.getUpdateUser());
	    isAdmin = false;
	    try {
			isAdmin = perm.hasRole(1); // role_id = 1 is the Application Administrator role
		} catch (CoeusClientException e) {
			System.out.println("Unable to determine administrator rights for user");
			e.printStackTrace();
		}
    }
    // JM END
    
    /**
     *  Get the <code>UnitDetailFormBean</code> instance, which holds the data
     *  for the form.
     *  @return UnitDetailFormBean Unit detail form data
     *  @throws Exception while gettting data for this form.
     */
    public UnitDetailFormBean getUnitDetail() throws Exception{
        UnitDetailFormBean unitDetail = new UnitDetailFormBean();
        unitDetail.setUnitNumber(this.txtUnitNumber.getText());
        unitDetail.setUnitName(this.txtUnitName.getText().trim());
        unitDetail.setAdminOfficerName(this.txtAdminOfficer.getText());
        unitDetail.setAdminOfficerId(this.adminOfficerId);
        unitDetail.setDeanVpName(this.txtDeanVp.getText());
        unitDetail.setDeanVpId(this.deanVpId);
        unitDetail.setOtherIndToNotifyName(this.txtOtherIndToNotify.getText());
        unitDetail.setOtherIndToNotifyId(this.otherIndToNotifyId);
        unitDetail.setParentUnitNumber(this.txtParentUnit.getText().toString().substring(
        		0,this.txtParentUnit.getText().toString().indexOf(':')-1));
        unitDetail.setUnitHeadName(this.txtUnitHead.getText());
        unitDetail.setUnitHeadId(this.unitHeadId);
        unitDetail.setOspAdminName(this.txtOspAdmin.getText());
        unitDetail.setOspAdminId(this.ospAdminId);
        unitDetail.setUpdateUser(this.updateUser);
        unitDetail.setUpdateTimestamp(this.updateTimestamp);
        
        //added for unit detail - organization search - 8
        unitDetail.setOrganizationName(this.txtOrganization.getText());
        unitDetail.setOrganizationId(this.organizationId);
        //added for unit detail - organization search - 8
        
        /* JM 7-14-2015 added status */
        unitDetail.setStatus(getComboBoxSelection(cmbStatus.getSelectedIndex(),cmbStatus));
        /* JM END */
        
        return unitDetail;
    }
    
    /**
     * This method is called for setting the controls of the text and combobox component
     * like enable or disable .
     *
     * @param value boolean ,if true enable the controls else disable it
     */
    public void setControlsEnabled(boolean value){
        txtParentUnit.setEditable(value);
        txtUnitNumber.setEditable(value);
        txtUnitName.setEditable(value);
        txtAdminOfficer.setEditable(value);
        txtUnitHead.setEditable(value);
        txtDeanVp.setEditable(value);
        txtOtherIndToNotify.setEditable(value);
        txtOspAdmin.setEditable(value);
        btnSearch1.setEnabled(value);
        btnSearch2.setEnabled(value);
        btnSearch3.setEnabled(value);
        btnSearch4.setEnabled(value);
        btnSearch5.setEnabled(value);
        //added for unit detail - organization search - 9
        btnOrganizationSearch.setEnabled(value);
        txtOrganization.setEditable(value);
        //added for unit detail - organization search - 10
        if(unitAdminTypeForm!=null){
            if(!value){
                unitAdminTypeForm.btnCancel.requestFocus();
            }
        }
        //Commented for unit hierarchy enhancement start by tarique
       // btnOK.setEnabled(value);
        //Commented for unit hierarchy enhancement end by tarique
        
        /* JM 7-14-2015 status toggle only shows for administrators */
        edu.vanderbilt.coeus.utils.UserPermissions perm = new edu.vanderbilt.coeus.utils.UserPermissions(unitDetail.getUpdateUser());
        isAdmin = false;
        try {
			isAdmin = perm.hasRole(1); // role_id = 1 is the Application Administrator role
		} catch (CoeusClientException e) {
			System.out.println("Unable to determine administrator rights for user");
			e.printStackTrace();
		}
        cmbStatus.setEnabled(value);
        if (!isAdmin) {
        	cmbStatus.setEditable(false);
        }
        /* JM END */
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        pnlUnitDetail = new JPanel(new GridBagLayout());
        pnlUnitDetail.setBorder(new javax.swing.border.EtchedBorder());
        lblParentUnit = new JLabel();
        txtParentUnit = new CoeusTextField();
        lblUnitNumber = new JLabel();
        txtUnitNumber = new CoeusTextField();
        //txtUnitNumber.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,8));
        /**
         * Updated For : REF ID 145 Feb' 14 2003
         * Org Unit entries appear to be limited to numerical entries. Can
         * this be modified to include alphanumeric as well.
         *
         * Updated by Subramanya Feb' 19 2003
         */
        /*
         *Modified by Geo
         *It should accept any chars. so commenting the line to set the filter document
         */
//        txtUnitNumber.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,8));
        txtUnitNumber.setDocument(new LimitedPlainDocument(8));
        lblUnitName = new JLabel();
        txtUnitName = new CoeusTextField();
        txtUnitName.setDocument(new LimitedPlainDocument(60));
        lblAdminOfficer = new JLabel();
        txtAdminOfficer = new CoeusTextField();
        lblUnitHead = new JLabel();
        txtUnitHead = new CoeusTextField();
        lblDeanVp = new JLabel();
        txtDeanVp = new CoeusTextField();
        lblOtherIndToNotify = new JLabel();
        txtOtherIndToNotify = new CoeusTextField();
        lblOSPAdmin = new JLabel();
        txtOspAdmin = new CoeusTextField();
        
        //added for unit detail - organization search - 11
        lblOrganization = new JLabel();
        txtOrganization = new CoeusTextField();
        //added for unit detail - organization search - 11
        
        /* JM 7-14-2015 added status */
        lblStatus = new JLabel();
        /* JM END */        
        
        btnSearch1 = new JButton();
        btnSearch2 = new JButton();
        btnSearch3 = new JButton();
        btnSearch4 = new JButton();
        btnSearch5 = new JButton();
        btnOrganizationSearch = new JButton();
        
        btnPanel = new JPanel();

        /* JM 7-14-2015 added status combobox */
        cmbStatus = new JComboBox();
        /* JM END */
        
        java.awt.GridBagConstraints gridBagConstraints1;
        
        lblParentUnit.setText("Parent Unit:");
        lblParentUnit.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.weightx = 0.5;
        gridBagConstraints1.insets = new java.awt.Insets(13, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblParentUnit, gridBagConstraints1);
        
        txtParentUnit.setColumns(25);
        txtParentUnit.setFont(normalFont);
        txtParentUnit.setEditable(false);
        txtParentUnit.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(13, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtParentUnit, gridBagConstraints1);
        
        lblUnitNumber.setText("Unit Number:");
        lblUnitNumber.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblUnitNumber, gridBagConstraints1);
        
        txtUnitNumber.setColumns(8);
        txtUnitNumber.setFont(normalFont);
        txtUnitNumber.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtUnitNumber, gridBagConstraints1);
        
        lblUnitName.setText("Unit Name:");
        lblUnitName.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblUnitName, gridBagConstraints1);
        
        txtUnitName.setColumns(25);
        txtUnitName.setFont(normalFont);
        txtUnitName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtUnitName, gridBagConstraints1);
        
        lblAdminOfficer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAdminOfficer.setText("Administrative Officer:");
        lblAdminOfficer.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblAdminOfficer, gridBagConstraints1);
        
        txtAdminOfficer.setColumns(23);
        txtAdminOfficer.setFont(normalFont);
        txtAdminOfficer.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtAdminOfficer, gridBagConstraints1);
        
        lblUnitHead.setText("Unit Head:");
        lblUnitHead.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblUnitHead, gridBagConstraints1);
        
        txtUnitHead.setColumns(23);
        txtUnitHead.setFont(normalFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtUnitHead, gridBagConstraints1);
        
        lblDeanVp.setText("Dean Vp:");
        lblDeanVp.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblDeanVp, gridBagConstraints1);
        
        txtDeanVp.setColumns(23);
        txtDeanVp.setFont(normalFont);
        txtDeanVp.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtDeanVp, gridBagConstraints1);
        
        lblOtherIndToNotify.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOtherIndToNotify.setText("Other Individual To Notify:");
        lblOtherIndToNotify.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblOtherIndToNotify, gridBagConstraints1);
        
        txtOtherIndToNotify.setColumns(23);
        txtOtherIndToNotify.setFont(normalFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtOtherIndToNotify, gridBagConstraints1);
        
        lblOSPAdmin.setText("Osp administrator:");
        lblOSPAdmin.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblOSPAdmin, gridBagConstraints1);
        
        txtOspAdmin.setColumns(23);
        txtOspAdmin.setFont(normalFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtOspAdmin, gridBagConstraints1);
        
        
        //added for unit detail - organization search - 12
        lblOrganization.setText("Organization:");
        lblOrganization.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        pnlUnitDetail.add(lblOrganization, gridBagConstraints1);
        
        txtOrganization.setColumns(23);
        txtOrganization.setFont(normalFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(txtOrganization, gridBagConstraints1);
        //added for unit detail - organization search - end
        
        /* JM 7-14-2015 added status combobox */
        lblStatus.setText("Status:");
        lblStatus.setFont(labelFont);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        // only show if application administrator
        if (isAdmin) {
        	pnlUnitDetail.add(lblStatus, gridBagConstraints1);
        }
        
        //Populate the status combobox
        ComboBoxBean comboBoxBean = new ComboBoxBean("","");
        cmbStatus.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean("A", "Active");
        cmbStatus.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean("I", "Inactive");
        cmbStatus.addItem(comboBoxBean);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        // only show if application administrator
        if (isAdmin) {
        	pnlUnitDetail.add(cmbStatus, gridBagConstraints1);
        }
        /* JM END */
        
        //Bug Fix:1077 Start 1
        ImageIcon searchIcon = new javax.swing.ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON));
        Dimension icnSize = new Dimension(23,23);
        //Bug Fix:1077 End 1
        
        btnSearch1.setName("Search1");
        btnSearch1.setPreferredSize(icnSize);
        btnSearch1.setIcon(searchIcon);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(3, 0, 3, 3);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(btnSearch1, gridBagConstraints1);
        
        btnSearch2.setName("Search2");
        //        btnSearch2.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("/images/find.gif")));
        btnSearch2.setPreferredSize(icnSize);
        btnSearch2.setIcon(searchIcon);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.insets = new java.awt.Insets(3, 0, 3, 3);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(btnSearch2, gridBagConstraints1);
        
        btnSearch3.setName("Search3");
        //btnSearch3.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("/images/find.gif")));
        btnSearch3.setPreferredSize(icnSize);
        btnSearch3.setIcon(searchIcon);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(3, 0, 3, 3);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(btnSearch3, gridBagConstraints1);
        
        btnSearch4.setName("Search4");
        //btnSearch4.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("/images/find.gif")));
        btnSearch4.setPreferredSize(icnSize);
        btnSearch4.setIcon(searchIcon);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.insets = new java.awt.Insets(3, 0, 3, 3);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(btnSearch4, gridBagConstraints1);
        
        btnSearch5.setName("Search5");
        btnSearch5.setPreferredSize(icnSize);
        btnSearch5.setIcon(searchIcon);
       // btnSearch5.setNextFocusableComponent(btnOK);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(3, 0, 3, 3);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(btnSearch5, gridBagConstraints1);
        
        //added for unit detail - organization search - 13
        btnOrganizationSearch.setName("Search6");
        btnOrganizationSearch.setPreferredSize(icnSize);
        btnOrganizationSearch.setIcon(searchIcon);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.insets = new java.awt.Insets(3, 0, 3, 3);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        pnlUnitDetail.add(btnOrganizationSearch, gridBagConstraints1);     
        //added for unit detail - organization search - end
        
        
        btnPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridheight = 8;
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.weightx = 0.5;
        gridBagConstraints1.weighty = 0.5;
        
        gridBagConstraints1.insets = new java.awt.Insets(13, 9, 16, 9);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
        pnlUnitDetail.add(btnPanel, gridBagConstraints1);
        
        switch (functionalityType){
            case('U'):
                txtUnitNumber.setEditable(false);
                txtUnitName.requestFocus();
               // btnCancel.setNextFocusableComponent(txtUnitName);
                break;
            case('G'):
               // this.btnCancel.requestFocus();
                setControlsEnabled(false);
                break;
            case('I'):
                txtUnitNumber.requestFocus();
              //  btnCancel.setNextFocusableComponent(txtUnitNumber);
                break;
        }
        this.add(pnlUnitDetail);
    }
    /**  The method used to display the unit detail form as a dialog window.
     *  It is using CoeusDlgWindow component to display the form. It accepts
     *  the parent component as <code>JDialog</code> or <code>JFrame</code>
     * @param parent  parent component
     */
    public void showUnitForm(Component parent){
        if( unitDetail != null ) {
            if(parent instanceof JFrame){
                dlgUnitDetail = new CoeusDlgWindow((JFrame)parent, "Unit Detail", true);
            }
            
            dlgUnitDetail.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            //Commented for unit hierarchy enhancement start by tarique
            //dlgUnitDetail.getContentPane().add(pnlUnitDetail);
            //Commented for unit hierarchy enhancement start by tarique
            unitAdminTypeForm.scrPnUnitDetails.setViewportView(pnlUnitDetail);
            unitAdminTypeForm.setVisible(true);
            dlgUnitDetail.getContentPane().add(unitAdminTypeForm);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dlgUnitDetail.setResizable(false);
            dlgUnitDetail.setSize(568,335);
            //Commented for unit hierarchy enhancement start by tarique
            //dlgUnitDetail.pack();
            //Commented for unit hierarchy enhancement end by tarique
            //setting the location at the middile of the screen
            Dimension dlgSize = dlgUnitDetail.getSize();
            dlgUnitDetail.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));
            dlgUnitDetail.addKeyListener(this);
            //Commented for unit hierarchy enhancement start by tarique
          //  dlgUnitDetail.getRootPane().setDefaultButton(btnOK);
            //Commented for unit hierarchy enhancement end by tarique
            
            //Setting the focus depends on the functionality type
            dlgUnitDetail.addWindowListener(new WindowAdapter(){
                public void windowOpened(WindowEvent we){
                    dataChanged = false;
                    switch (functionalityType){
                        case('U'):
                            txtUnitNumber.setEditable(false);
                            txtUnitName.requestFocus();
                            break;
                        case('G'):
                            //Commented for unit hierarchy enhancement start by tarique
                          //  btnCancel.requestFocus();
                            //Commented for unit hierarchy enhancement end by tarique
                            setControlsEnabled(false);
                            unitAdminTypeForm.setControls(false);
                            break;
                        case('I'):
                            txtUnitNumber.requestFocus();
                            break;
                    }
                }
                public void windowClosing(WindowEvent we){
                    try{
                        validateWindow();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        CoeusOptionPane.showErrorDialog(ex.getMessage());
                        
                    }
                }
            });
            dlgUnitDetail.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae) {
                    try{
                        validateWindow();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        CoeusOptionPane.showErrorDialog(ex.getMessage());
                        
                    }
                }
            });
            if(functionalityType=='G'){
                dlgUnitDetail.setForeground(Color.black);
            }
            setFocusTraversal(UNIT_DETAIL_TAB);
            dlgUnitDetail.show();
        }
    }
    /**
     * get all the form data from the database
     */
    
    private void doDBValidations() throws Exception{
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionalityType);
        request.setDataObject(getUnitDetail());
        request.setFunctionType('A');
        request.setId(txtUnitNumber.getText());
        AppletServletCommunicator comm = new AppletServletCommunicator(
        UNIT_CONNECTION_URL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (!response.isSuccessfulResponse()){
            dlgUnitDetail.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            throw new Exception(response.getMessage());
        }
        this.unitDetail = (UnitDetailFormBean)response.getDataObject();
        this.attachValues();
    }
    
    /*
     *  Method to validate the field contents. This will be invoked before
     *  saving the data.
     */
    private boolean validateFields() throws Exception {
        boolean validateFlag=false;
        String unitNumber = txtUnitNumber.getText().trim();
        
        if (unitNumber.length() == 0){
            //Modified for setting text EMPTY if user give blank space start 1 case #2104
            txtUnitNumber.setText("");
            //Modified for setting text EMPTY if user give blank space end 1 case #2104
            throw new Exception(coeusMessageResources.parseMessageKey(
            "unitDetFrm_exceptionCode.1114"));
        }
        //Commented for not to check validation less than 6 start 2 case #2104
//        else if ( unitNumber.length() < 6){
//            throw new Exception(coeusMessageResources.parseMessageKey(
//            "unitDetFrm_exceptionCode.1115"));
//        }
        //Commented for not to check validation less than 6 end 2 case #2104
        else if (txtUnitName.getText().trim().length() <= 0 ){
            throw new Exception(coeusMessageResources.parseMessageKey(
            "unitDetFrm_exceptionCode.1116"));
        }else if (functionalityType=='I' && unitHierarchyTree!=null &&
        unitHierarchyTree.findByName(
        (this.txtUnitNumber.getText()+" ")) != null){
            throw new Exception("Unit Number "+ unitNumber +
            " is already existing. Please enter a different Unit Number");
        }else{
            validateFlag = true;
        }
        return validateFlag;
    }
    
    /*
     * method to add or update the node to the tree
     */
    private void addUpdateData(char funcType) throws Exception{
        dlgUnitDetail.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        /* updated by ravi on 18-02-03 for ignoring mouse clicks
           when the system is busy with some operation */
        CoeusGuiConstants.getMDIForm().getGlassPane().setVisible(true);
        validateFields();
        doDBValidations();
        switch(funcType){
            case('I'):
                unitHierarchy = new UnitHierarchyFormBean();
                unitHierarchy.setChildrenFlag("N");
                //break;
            case('U'):
                unitHierarchy.setUnitDetail(this.getUnitDetail());
                break;
        }
        String parentText = txtParentUnit.getText();
        unitHierarchy.setParentNodeID(parentText.substring(0,parentText.indexOf(' ')));
        unitHierarchy.setNodeID(txtUnitNumber.getText());
        unitHierarchy.setNodeName(txtUnitName.getText());
        if(unitAdminTypeForm!=null&&unitAdminTypeForm.modified){
            //Commented for COEUSDEV-170 : Array index out of range error - Unit Administrators - Start
            //When modification is done unitAdminTypeFlag is set to True
            //Added for unit hierarchy enhancement start by tarique
//            if((unitAdminTypeForm.getCvAdminType()!=null
//                    &&unitAdminTypeForm.getCvAdminType().size()>0)||
//                    (unitAdminTypeForm.getCvDeletedData()!=null
//                        &&unitAdminTypeForm.getCvDeletedData().size()>0)){
            //COEUSDEV-170 : END
                unitAdminTypeFlag = unitAdminTypeForm.modified;
                performAdministratorSaveAction();
//            }
            //Added for unit hierarchy enhancement end by tarique
            dlgUnitDetail.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            CoeusGuiConstants.getMDIForm().getGlassPane().setVisible(false);
        }
    }
    
    /**
     * get all the form data from the database
     */
    private UnitDetailFormBean getResponseData(String unitNumber) throws Exception{
        UnitDetailFormBean unitDetail = new UnitDetailFormBean();
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        request.setFunctionType('G');
        request.setId(unitNumber);
        request.setDataObject(unitDetail);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        UNIT_CONNECTION_URL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()){
            unitDetail = (UnitDetailFormBean)response.getDataObject();
        }else{
            throw new Exception(response.getMessage());
        }
        return unitDetail;
    }
    //Added for unit hierarchy enhancement start by tarique
    private void getAdministratorType(String unitNumber) throws Exception{
        try{
            unitAdminTypeForm = new UnitAdminTypeForm(this);
            unitAdminTypeForm.setFunctionType(functionalityType);
            unitAdminTypeForm.setUnitNumber(unitNumber);
            if(unitHierarchy!=null){
                if((unitHierarchy.getCvAdminType()) == null ){
                    cvAdminData  = getDataForAdmin(unitNumber);
                }else{
                    CoeusVector cvData = getFilterUnitNumber(
                                unitHierarchy.getCvAdminType(),unitNumber);
                    cvAdminData = new CoeusVector();
                    cvAdminData.add(cvData);
                    CoeusVector cvTypeCode = unitHierarchy.getCvAdminTypeCode();
                    cvAdminData.add(cvTypeCode);
                }
            }else{
                cvAdminData  = getDataForAdmin(unitNumber);
            }
            unitAdminTypeForm.setAdminData(cvAdminData);
            unitAdminTypeForm.setFormData();
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
            return;
            
        }
        
    }
    private CoeusVector getFilterUnitNumber(CoeusVector dataToFilter,String unitNumber){
        CoeusVector dataObject = null;
        if(dataToFilter!=null&&dataToFilter.size()>0){
            Equals equals = new Equals("unitNumber",unitNumber);
            CoeusVector filterData = dataToFilter.filter(equals);
            if(filterData!= null && filterData.size() > 0){
                dataObject = new CoeusVector();
                dataObject.addAll(filterData);
            }
        }
        return dataObject;
    }
    
    private CoeusVector getDataForAdmin(String unitNumber) throws Exception{
        RequesterBean request = new RequesterBean();
        CoeusVector cvData = null;
        request.setFunctionType('K');
        request.setDataObject(unitNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        UNIT_CONNECTION_URL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()){
            cvData = (CoeusVector)response.getDataObject();
        }else{
            throw new Exception(response.getMessage());
        }
        return cvData;
    }
    //Added for unit hierarchy enhancement end by tarique
    /**  Overridden method for the KeyListener
     * @param ke  KeyEvent
     */
    public void keyTyped(KeyEvent ke){
    }
    /**
     *  Overridden method for the KeyListener
     * @param kp  KeyEvent
     */
    public void keyPressed(KeyEvent kp){
    }
    /**
     *  Overridden method for the KeyListener
     * @param kt  KeyEvent
     */
    public void keyReleased(KeyEvent kt){
        dataChanged = true;
    }
    /*
     *  Method to validate the window before closing. If any data has been changed
     *  by the user, it will pop up an option dialog window asking the
     *  save confirmation message
     */
    //Modify private to public for unit hierarchy enhancement by tarique 
    public void validateWindow() throws Exception{
        //Added for unit hierarchy enhancement start by tarique
        if (!dataChanged && unitAdminTypeForm!=null){
            dataChanged = unitAdminTypeForm.modified;
            unitAdminTypeFlag = unitAdminTypeForm.modified;
        }
        //Added for unit hierarchy enhancement end by tarique
        if (dataChanged && functionalityType != DISPALY_MODE ){
            int resultConfirm = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(
            "saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(resultConfirm){
                case(JOptionPane.YES_OPTION):
                    if(unitAdminTypeForm!=null){
                        UnitAdministratorBean bean = unitAdminTypeForm.checkForDuplicate();
                        if(bean != null){
                            int index = unitAdminTypeForm.searchIndex(
                                unitAdminTypeForm.getCvAdminType(), bean);
                            if(index != -1){
                                unitAdminTypeForm.tbdPnUnitForm.setSelectedIndex(1);
                                unitAdminTypeForm.tblUnitAdmin.requestFocus();
                                unitAdminTypeForm.tblUnitAdmin.setRowSelectionInterval(index, index);
                            }
                            return;
                        }
                    }
                    if(validateFields()){
                        addUpdateData(functionalityType);
                    }
                    dlgUnitDetail.dispose();
                    break;
                case(JOptionPane.NO_OPTION):
                    unitHierarchy = null;
                    dlgUnitDetail.dispose();
                    break;
                default:
                    return;
            }
        }else{
            unitHierarchy = null;
            dlgUnitDetail.dispose();
        }
    }
    //Added for unit hierarchy enhancement start by tarique
    public void performAdministratorSaveAction() throws CoeusException{
      
            unitAdminTypeForm.unitAdminTableCellEditor.stopCellEditing();
            CoeusVector cvDeletedData = unitAdminTypeForm.getCvDeletedData();
            CoeusVector cvAdminType = unitAdminTypeForm.getCvAdminType();
            CoeusVector cvAdminTypeCode = unitAdminTypeForm.getVecAdminTypeCode();
            if  (functionalityType == 'I' && cvAdminType != null)  {
                for(int i=0;i<cvAdminType.size();i++){
                    UnitAdministratorBean bean = (UnitAdministratorBean)cvAdminType.get(i);
                    bean.setUnitNumber(txtUnitNumber.getText().trim());
                }
                if(cvDeletedData != null){
                    cvDeletedData.removeAllElements();
                }
            }
            adminDataObjects = new CoeusVector();
            
            if(cvDeletedData!= null && cvDeletedData.size() > 0){
                adminDataObjects.addAll(cvDeletedData);
            }
            
            if(cvAdminType!= null && cvAdminType.size() > 0){
                adminDataObjects.addAll(cvAdminType);
            }

            unitHierarchy.setCvAdminType(adminDataObjects);
            unitHierarchy.setCvAdminTypeCode(cvAdminTypeCode);
    }
     
    //End here unit hierarchy enhancement 
    private boolean dataChanged;
    /**
     *  Overridden method for the ActionListener
     * @param actionEvent ActionEvent
     */
    public void actionPerformed(ActionEvent actionEvent){
        //Hashtable selectedRow = null;
        HashMap selectedRow = null;
        try{
            Object eventSource = actionEvent.getSource();

            if (eventSource.equals(btnSearch1)){
                CoeusSearch coeusSearch = new CoeusSearch(dlgUnitDetail,"personsearch",1);
                coeusSearch.showSearchWindow();
                selectedRow = coeusSearch.getSelectedRow();
                if (selectedRow != null){
                    setAdminOfficerId(selectedRow.get("PERSON_ID").toString());
                    txtAdminOfficer.setText(selectedRow.get("FULL_NAME").toString());
                    //Bug Fix: For validating the data on focus lost Start 3
                    oldAdminOffName = txtAdminOfficer.getText().trim();
                    //Bug Fix: For validating the data on focus lost End 3
                }
            }else if (eventSource.equals(btnSearch2)){
                CoeusSearch coeusSearch = new CoeusSearch(dlgUnitDetail,"personsearch",1);
                coeusSearch.showSearchWindow();
                selectedRow = coeusSearch.getSelectedRow();
                if (selectedRow != null){
                    setUnitHeadId(selectedRow.get("PERSON_ID").toString());
                    txtUnitHead.setText(selectedRow.get("FULL_NAME").toString());
                    //Bug Fix: For validating the data on focus lost Start 4
                    oldUnitHeadName = txtUnitHead.getText().trim();
                    //Bug Fix: For validating the data on focus lost End 4
                }
            }else if (eventSource.equals(btnSearch3)){
                CoeusSearch coeusSearch = new CoeusSearch(dlgUnitDetail,"personsearch",1);
                coeusSearch.showSearchWindow();
                selectedRow = coeusSearch.getSelectedRow();
                if (selectedRow != null){
                    setDeanVpId(selectedRow.get("PERSON_ID").toString());
                    txtDeanVp.setText(selectedRow.get("FULL_NAME").toString());
                    //Bug Fix: For validating the data on focus lost Start 5
                    oldDeanVPName = txtDeanVp.getText().trim();
                    //Bug Fix: For validating the data on focus lost End 5
                }
            }else if (eventSource.equals(btnSearch4)){
                CoeusSearch coeusSearch = new CoeusSearch(dlgUnitDetail,"personsearch",1);
                coeusSearch.showSearchWindow();
                selectedRow = coeusSearch.getSelectedRow();
                if (selectedRow != null){
                    setOtherIndToNotifyId(selectedRow.get("PERSON_ID").toString());
                    txtOtherIndToNotify.setText(selectedRow.get("FULL_NAME").toString());
                    //Bug Fix: For validating the data on focus lost Start 6
                    oldOtherIndvName = txtOtherIndToNotify.getText().trim();
                    //Bug Fix: For validating the data on focus lost End 6
                }
            }else if (eventSource.equals(btnSearch5)){
                CoeusSearch coeusSearch = new CoeusSearch(dlgUnitDetail,"personsearch",1);
                coeusSearch.showSearchWindow();
                selectedRow = coeusSearch.getSelectedRow();
                if (selectedRow != null){
                    setOspAdminId(selectedRow.get("PERSON_ID").toString());
                    txtOspAdmin.setText(selectedRow.get("FULL_NAME").toString());
                    //Bug Fix: For validating the data on focus lost Start 7
                    oldOSPAdminName = txtOspAdmin.getText().trim();
                    //Bug Fix: For validating the data on focus lost End 7
                }
            //added for unit detail - organization search - 14
            }else if (eventSource.equals(btnOrganizationSearch)){
                CoeusSearch coeusSearch = new CoeusSearch(dlgUnitDetail,"organizationsearch",1);
                coeusSearch.showSearchWindow();
                selectedRow = coeusSearch.getSelectedRow();
                if (selectedRow != null){
                    setOrganizationId(selectedRow.get("ORGANIZATION_ID").toString());
                    txtOrganization.setText(selectedRow.get("ORGANIZATION_NAME").toString());
                    oldOrganization = txtOrganization.getText().trim();
                }
            }
            //added for unit detail - organization search - end
            
        }catch (Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    //added for unit hierarchy enhancement start by tarique
    public void performOkAction() throws Exception{
        if(validateFields()){
                addUpdateData(functionalityType);
        }
        this.dlgUnitDetail.dispose();
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()!=2 ) return ;
        Object source = mouseEvent.getSource();
        String personName = "";
        String loginName = CoeusGuiConstants.getMDIForm().getUserName();
        
        if(((JTextField)source).getText() != null
        && !((JTextField)source).getText().trim().equals("")){
            personName  = ((JTextField)source).getText().trim();
            try{
                ((JTextField)source).setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                dlgUnitDetail.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                
                if(source.equals(txtAdminOfficer)){
                    PersonDetailForm personDetailForm =
                    new PersonDetailForm(adminOfficerId,loginName,TypeConstants.DISPLAY_MODE);
                }else if(source.equals(txtUnitHead)){
                    PersonDetailForm personDetailForm =
                    new PersonDetailForm(unitHeadId,loginName,TypeConstants.DISPLAY_MODE);
                }else if(source.equals(txtDeanVp)){
                    PersonDetailForm personDetailForm =
                    new PersonDetailForm(deanVpId,loginName,TypeConstants.DISPLAY_MODE);
                }else if(source.equals(txtOspAdmin)){
                    PersonDetailForm personDetailForm =
                    new PersonDetailForm(ospAdminId,loginName,TypeConstants.DISPLAY_MODE);
                }else if(source.equals(txtOtherIndToNotify)){
                    PersonDetailForm personDetailForm =
                    new PersonDetailForm(otherIndToNotifyId,loginName,TypeConstants.DISPLAY_MODE);
                }
                //Added for Organization Enhancement start
                else if(source.equals(txtOrganization)){
                    if (mouseEvent.getClickCount() == 2) {
                        
                        //invoke Organization display
                        try {
                            DetailForm frmOrgDetailForm = null;
                            if (!txtOrganization.getText().trim().equals("")) {
                                frmOrgDetailForm = new DetailForm(
                                                TypeConstants.DISPLAY_MODE, organizationId);
                            } 
                            if (frmOrgDetailForm != null) {
                                frmOrgDetailForm.showDialogForm(CoeusGuiConstants.getMDIForm());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            
                        }
                    }
                }
                //Added for Organization Enhancement end
            }catch ( Exception e) {
                CoeusOptionPane.showInfoDialog( e.getMessage());
            }finally{
                ((JTextField)source).setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                dlgUnitDetail.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }
    //Bug Fix: Pass the person id to get the person details End
   
    //Bug Fix: For validating the data on focus lost Start 8
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    
    
    private boolean validatePerson(String personName , Object source){
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(PERSON_VALIDATE);
        requesterBean.setDataObject(personName);
        
        AppletServletCommunicator comm = new AppletServletCommunicator
        (UNIT_CONNECTION_URL, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()) {
                PersonInfoFormBean personInfoFormBean  =
                (PersonInfoFormBean)responderBean.getDataObject();
                 
                if(personInfoFormBean.getPersonID() == null){
                    ((JTextField)source).setText("");
                    ((JTextField)source).requestFocusInWindow();
                    CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("investigator_exceptionCode.1007"));
                    return false;
                }else if(personInfoFormBean.getPersonID().equalsIgnoreCase("TOO_MANY")){
                    ((JTextField)source).setText("");
                    ((JTextField)source).requestFocusInWindow();
                    CoeusOptionPane.showErrorDialog
                        (personName+" " +coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1055"));
                    return false;
                }else{
                    setData(personInfoFormBean , source);
                    return true;
                }
            }else{
                Exception ex = responderBean.getException();
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    //added for unit detail - organization search - 15
    private boolean validateOrganizationName(String organizationName){
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(ORGANIZATION_VALIDATE);
        requesterBean.setDataObject(organizationName);
        String strOrganizationId = null;
        AppletServletCommunicator comm = new AppletServletCommunicator
        (UNIT_CONNECTION_URL, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(!responderBean.isSuccessfulResponse()) {
                Exception exception = responderBean.getException();
                exception.printStackTrace();
                return false;
            }
            strOrganizationId = (String)responderBean.getDataObject();
            if(strOrganizationId == null){
                txtOrganization.setText("");
                txtOrganization.requestFocusInWindow();
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("unitDetFrmOrg_exceptionCode.1000"));
                return false;
            }
            setOrganizationId(strOrganizationId);
        }
        return true;
    }
    //added for unit detail - organization search - end

    
    private void setData(PersonInfoFormBean personInfoFormBean, Object source){
        if(source.equals(txtAdminOfficer)){
            setAdminOfficerId(personInfoFormBean.getPersonID());
        }else if(source.equals(txtUnitHead)){
            setUnitHeadId(personInfoFormBean.getPersonID());
        }else if(source.equals(txtDeanVp)){
            setDeanVpId(personInfoFormBean.getPersonID());
        }else if(source.equals(txtOspAdmin)){
            setOspAdminId(personInfoFormBean.getPersonID());
        }else if(source.equals(txtOtherIndToNotify)){
            setOtherIndToNotifyId(personInfoFormBean.getPersonID());
        }
    }
    
    public void focusGained(FocusEvent e) {
    }
    
    public void focusLost(FocusEvent e) {
        if(e.isTemporary()){
            return ;
        }
        
        Object source = e.getSource();
        if(source.equals(txtOrganization)){
            //validate Organization Name
            //added for unit detail - organization search - 16
            if(txtOrganization.getText() == null || txtOrganization.getText().trim().equals("")){
                return;
            }
            String organizationName = txtOrganization.getText().trim();
            validateOrganizationName(organizationName);
            //added for unit detail - organization search - end
            
        }else{
            String personName = "";
            if(((JTextField)source).getText() != null
            && !((JTextField)source).getText().trim().equals("")){
                personName = ((JTextField)source).getText().trim();

                if(source.equals(txtAdminOfficer)){
                    if(oldAdminOffName.equals(personName)){
                        return ;
                    }else{
                        oldAdminOffName = personName;
                    }
                }else if(source.equals(txtUnitHead)){
                    if(oldUnitHeadName.equals(personName)){
                        return ;
                    }else{
                        oldUnitHeadName = personName;
                    }
                }else if(source.equals(txtDeanVp)){
                    if(oldDeanVPName.equals(personName)){
                        return ;
                    }else{
                        oldDeanVPName = personName;
                    }
                }else if(source.equals(txtOspAdmin)){
                    if(oldOSPAdminName.equals(personName)){
                        return ;
                    }else{
                        oldOSPAdminName = personName;
                    }
                }else if(source.equals(txtOtherIndToNotify)){
                    if(oldOtherIndvName.equals(personName)){
                        return ;
                    }else{
                        oldOtherIndvName = personName;
                    }
                }

                validatePerson(personName, source);
            }
        }
    }
    //Bug Fix: For validating the data on focus lost End 8
    
    //Added for unit hierarchy enhancement start added by tarique
    public void setFocusTraversal(int tab){
        
        if(tab == UNIT_DETAIL_TAB){
           
           //case id - 2593 - Premium - Maintain Unit - Organization Entry - start 
           java.awt.Component[] components = new java.awt.Component[]{ 
                    txtUnitNumber, txtUnitName,txtAdminOfficer,btnSearch1,txtUnitHead,
                    btnSearch2,txtDeanVp,btnSearch3,txtOtherIndToNotify,btnSearch4,
                    txtOspAdmin,btnSearch5,txtOrganization,btnOrganizationSearch,
                    unitAdminTypeForm.btnOk,unitAdminTypeForm.btnCancel,txtUnitName,cmbStatus}; // JM 7-14-2015 added status
           //case id - 2593 - Premium - Maintain Unit - Organization Entry - end
                    
            switch (functionalityType){
                case('D'):
                    unitAdminTypeForm.setControls(false);
                    break;
                case('M'):
                    unitAdminTypeForm.setControls(false);
                    break;
                case('U'):
                    txtUnitNumber.setEditable(false);
                    txtUnitName.requestFocus();
                    break;
                case('G'):
                    setControlsEnabled(false);
                    break;
                case('I'):
                    txtUnitNumber.requestFocus();
                    break;
            }            
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            unitAdminTypeForm.setFocusTraversalPolicy(traversePolicy);
            unitAdminTypeForm.setFocusCycleRoot(true);
           
        }else if(tab == ADMINISTRATOR_TAB){
                java.awt.Component[] components = new java.awt.Component[]{ 
                    unitAdminTypeForm.btnAdd,unitAdminTypeForm.btnModify,
                    unitAdminTypeForm.btnDelete,unitAdminTypeForm.btnOk,
                    unitAdminTypeForm.btnCancel, unitAdminTypeForm.scrPnAdministrator};
                ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
                unitAdminTypeForm.btnAdd.requestFocus();
                unitAdminTypeForm.setFocusTraversalPolicy(traversePolicy);
                unitAdminTypeForm.setFocusCycleRoot(true);
                if(functionalityType == 'D' || functionalityType == 'M'){
                    unitAdminTypeForm.setControls(false);
                }
        }
    }
    /**
     * Getter for property adminDataObjects.
     * @return Value of property adminDataObjects.
     */
    public edu.mit.coeus.utils.CoeusVector getAdminDataObjects() {
        return adminDataObjects;
    }
    
    /**
     * Setter for property adminDataObjects.
     * @param adminDataObjects New value of property adminDataObjects.
     */
    public void setAdminDataObjects(edu.mit.coeus.utils.CoeusVector adminDataObjects) {
        this.adminDataObjects = adminDataObjects;
    }
    
    //Added for unit hierarchy enhancement end added by tarique
    // Variables declaration
    private CoeusDlgWindow dlgUnitDetail;
    private JPanel pnlUnitDetail;
    private JLabel lblParentUnit;
    private CoeusTextField txtParentUnit;
    private JLabel lblUnitNumber;
    public CoeusTextField txtUnitNumber;
    private JLabel lblUnitName;
    private CoeusTextField txtUnitName;
    private JLabel lblAdminOfficer;
    private CoeusTextField txtAdminOfficer;
    private JLabel lblUnitHead;
    private CoeusTextField txtUnitHead;
    private JLabel lblDeanVp;
    private CoeusTextField txtDeanVp;
    private JLabel lblOtherIndToNotify;
    private CoeusTextField txtOtherIndToNotify;
    private JLabel lblOSPAdmin;
    private CoeusTextField txtOspAdmin;
    
    //added for unit detail - organization search - 17
    private JLabel lblOrganization;
    private CoeusTextField txtOrganization;
    //added for unit detail - organization search - 17
    
    private JButton btnSearch1;
    private JButton btnSearch2;
    private JButton btnSearch3;
    private JButton btnSearch4;
    private JButton btnSearch5;
    
    //added for unit detail - organization search - 18
    private JButton btnOrganizationSearch;
    //added for unit detail - organization search - 18
    
    private JPanel btnPanel;
    //Commented for unit hierarchy enhancement start by tarique
    //private JButton btnOK;
    //private JButton btnCancel;
    //End of variables declaration
    //Commented for unit hierarchy enhancement end by tarique
    
    /* JM 7-14-2015 added status */
    private JLabel lblStatus;
    private JComboBox cmbStatus;
    /* JM END */
}
