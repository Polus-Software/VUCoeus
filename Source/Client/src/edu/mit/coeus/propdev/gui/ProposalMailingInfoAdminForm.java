/*
 * @(#)ProposalMailingInfoAdminForm.java 1.0 03/17/03 5:00 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.utils.query.Equals;
/**
 * This class provides the implementation for the proposal mailing info tab
 * in the proposal detail window.
 *
 * @version 1.0 March 17, 2003, 5:00 PM
 * @author  Sagin
 */


public class ProposalMailingInfoAdminForm extends javax.swing.JComponent
                                            implements TypeConstants {

    /* This is used to hold vector of data beans*/
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;

    /* This is used to hold the mode D for Display, I for Add, U for Modify */
    private char functionType;

    /* This is used to notify whether the Save is required */
    private boolean saveRequired = false;

    /* holds the reference of parent object*/
    private CoeusAppletMDIForm mdiForm=null;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    //Used to format date in dd-MMM-yyyy format
    private DateUtils dtUtils = new DateUtils();

    /* holds the Mailing Address ID*/
    private int mailingAddressId = -1; //set as -1. since its nullable in database.

	private final String DATE_FORMAT = "MM/dd/yyyy";

    private java.text.SimpleDateFormat dtFormat;

    /** Creates new form ProposalMailingInfoAdminForm */

    //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
    private int accountNumberMaxLength = 0;
    private CoeusVector cvParameters = null;
    //Case#2402 - End

    public ProposalMailingInfoAdminForm() {
        initComponents();
    }

    /** Creates new form <CODE>ProposalOrganizationAdminForm</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param proposalDevelopmentFormBean ProposalDevelopmentFormBean
     * 'D' specifies that the form is in Display Mode
     */
    public ProposalMailingInfoAdminForm(char functionType,
                    ProposalDevelopmentFormBean proposalDevelopmentFormBean) {

        /* This is used to hold the mode D for Display, I for Add, U for Modify */
        this.functionType = functionType;

         /* Used to hold the table data as beans*/
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }

    /** This method is used to initialize the form components,set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     * @param mdiForm reference to the parent component <CODE>CoeusAppletMDIForm</CODE>
     * @return JComponent reference to the <CODE>ProposalMailingInfoAdminForm</CODE> component
     * after initializing and setting the data.
     */
    //Modified for Case#2402 - use a parameter to set the length of the account number throughout app 
    //public JComponent showMailingInfoAdminForm(CoeusAppletMDIForm mdiForm){
    public JComponent showMailingInfoAdminForm(CoeusAppletMDIForm mdiForm, CoeusVector cvParameters){
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        //Case#2402 - use a parameter to set the length of the account number throughout app - Start
        this.cvParameters = cvParameters;
        initAccountNumberMaxLength();
        //Case#2402 - End
	initComponents();
        setListeners();
        setFormData();
        formatFields();
        Component[] comp = {txtDate,rBtnPostMark,rBtnReciept,rBtnOSP,rBtnDepartment,
            rBtnRegular,rBtnCourier,rBtnElectronic,txtMailAccount,txtNoOfCopies,
            txtCarrierCode,txtCarrierCodeType,btnSearchAddress,txtMailDescription };
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        return this;
    }
    
    //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
    /**
     * Method to set Account Number field size based on MAX_ACCOUNT_NUMBER_LENGTH parameter value
     */
    private void initAccountNumberMaxLength(){
        CoeusVector cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
        if(cvFiltered != null && cvFiltered.size() > 0){
            CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
            accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
        }
    }
    //Case#2402 - End
    
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){
        boolean enabled = functionType != DISPLAY_MODE ? true : false ;
        rBtnPostMark.setEnabled(enabled);
        rBtnReciept.setEnabled(enabled);
        rBtnOSP.setEnabled(enabled);
        rBtnDepartment.setEnabled(enabled);
        rBtnRegular.setEnabled(enabled);
        rBtnCourier.setEnabled(enabled);
        rBtnElectronic.setEnabled(enabled);
        btnSearchAddress.setEnabled(enabled);
        btnClearAddress.setEnabled(enabled);
        txtDate.setEditable(enabled);
        txtMailAccount.setEditable(enabled);
        txtNoOfCopies.setEditable(enabled);
        txtCarrierCode.setEditable(enabled);
        txtCarrierCodeType.setEditable(enabled);
        txtMailingAddress.setEditable(enabled);
        txtMailDescription.setEditable(enabled);
        if (functionType == DISPLAY_MODE) {
            txtArMailAddress.setBackground(new java.awt.Color(204, 204, 204));
            txtArMailAddress.setOpaque(false);
        } else {
            txtArMailAddress.setBackground(new java.awt.Color(255, 255, 255));
            txtArMailAddress.setOpaque(true);
        }
    }

    /**
     * This method is used to set the listeners for various components in this form
     */
    private void setListeners(){
        if(functionType != DISPLAY_MODE){
            txtDate.addFocusListener(new CustomFocusAdapter());
        }
    }

    /**
     * This method is used to set the form data specified in
     * <CODE> proposalDevelopmentFormBean</CODE>
     */
    public void setFormData(){
        if (functionType == DISPLAY_MODE ||
                                functionType == MODIFY_MODE){
            if ( proposalDevelopmentFormBean.getDeadLineDate() != null ){
                txtDate.setText(dtUtils.formatDate(
                    Utils.convertNull(proposalDevelopmentFormBean.
                        getDeadLineDate().toString()), "dd-MMM-yyyy"));
            } else {
                txtDate.setText("");
            }

            if ( proposalDevelopmentFormBean.getDeadLineType() != null ){
                if ( proposalDevelopmentFormBean.getDeadLineType().
                                                equalsIgnoreCase("P") ){
                    rBtnPostMark.setSelected(true);
                } else {
                    rBtnReciept.setSelected(true);
                }
            } else {
                rBtnDeadLine.setSelected(true);
            }

            if ( proposalDevelopmentFormBean.getMailBy() != null ){
                if ( proposalDevelopmentFormBean.getMailBy().
                                                equalsIgnoreCase("O") ){
                    rBtnOSP.setSelected(true);
                } else {
                    rBtnDepartment.setSelected(true);
                }
            } else {
                rBtnmailBy.setSelected(true);
            }

            if ( proposalDevelopmentFormBean.getMailType() != null ){
                if ( proposalDevelopmentFormBean.getMailType().
                                                equalsIgnoreCase("R") ){
                    rBtnRegular.setSelected(true);
                }else if ( proposalDevelopmentFormBean.getMailType().
                                                equalsIgnoreCase("D") ){
                    rBtnCourier.setSelected(true);
                } else {
                    rBtnElectronic.setSelected(true);
                }
            } else {
                rBtnType.setSelected(true);
            }
            
            if (proposalDevelopmentFormBean.getMailAccountNumber() != null ){
                //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                txtMailAccount.setText(proposalDevelopmentFormBean.
//                                                getMailAccountNumber());
                String mailAccountNumber = proposalDevelopmentFormBean.getMailAccountNumber();
                if(mailAccountNumber.length() > accountNumberMaxLength){
                    mailAccountNumber = mailAccountNumber.substring(0,accountNumberMaxLength);
                }
                txtMailAccount.setText(mailAccountNumber);
                //Case#2402 - End
            } else{
                txtMailAccount.setText("");
            }

            if ( proposalDevelopmentFormBean.getNumberCopies() != null ){
                txtNoOfCopies.setText(new Integer(proposalDevelopmentFormBean.
                                                getNumberCopies()).toString());
            } else {
                txtNoOfCopies.setText("");
            }

            if ( proposalDevelopmentFormBean.getCarrierCode() != null ){
                txtCarrierCode.setText(proposalDevelopmentFormBean.
                                                getCarrierCode());
            } else {
                txtCarrierCode.setText("");
            }

            if ( proposalDevelopmentFormBean.getCarrierCodeType() != null ){
                txtCarrierCodeType.setText(proposalDevelopmentFormBean.
                                                getCarrierCodeType());
            } else {
                txtCarrierCodeType.setText("");
            }

            if ( proposalDevelopmentFormBean.getMailingAddressId() > 0 ){
                mailingAddressId = proposalDevelopmentFormBean.getMailingAddressId();
            } else {
                mailingAddressId = -1;
            }
            /** Bug Fix 1996 -start
             */
            if ( proposalDevelopmentFormBean.getMailingAddressName() != null  && mailingAddressId!= 0){
                txtMailingAddress.setText(proposalDevelopmentFormBean.
                                                getMailingAddressName());
            } else {
                txtMailingAddress.setText("");
            }
            
            if ( proposalDevelopmentFormBean.getMailingAddress() != null && mailingAddressId != 0){
                txtArMailAddress.setText(formatMailingAddress(proposalDevelopmentFormBean.
                                                getMailingAddress()));
            } else {
                txtArMailAddress.setText("");
            }/** Bug Fix 1996 -End
             */

            if ( proposalDevelopmentFormBean.getMailDescription() != null ){
                txtMailDescription.setText(proposalDevelopmentFormBean.
                                                getMailDescription());
            } else {
                txtMailDescription.setText("");
            }



        }
    }

    /** This method is used to set the <CODE>ProposalDevelopmentFormBean</CODE> with all the details
     * of the Proposal.
     * @param infoBean <CODE>ProposalDevelopmentFormBean</CODE> which consists of all the details of
     * Proposal for Proposal, Organization & Mailing Info tabpages.
     * This method will be called from Proposal Detail Form when using the
     * Navigations menu items (Next & Previous)
     */
    public void setValues(ProposalDevelopmentFormBean infoBean){
        this.proposalDevelopmentFormBean = infoBean;
        setFormData();
        //formatFields();
    }

    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {
                txtDate.requestFocusInWindow();            
        }
    }    
    //End Amit  
    
     /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }

    /** Method to get the functionType
     * @return a <CODE>Char</CODE> representation of functionType.
     */
    public char getFunctionType(){
        return this.functionType;
    }
     /** Method to set the functionType
     * @param fType is functionType to be set like 'D', 'I', 'M'
     */
    public void setFunctionType(char fType){
        this.functionType = fType;
    }

    /** This method invoked after validation whenever the user presses any of the
     * Save buttons.
     * Sets the data and returns the ProposalDevelopnmentFormBean.
     * @return ProposalDevelopmentFormBean.
     */
    public void getFormData() throws Exception {
            if((txtDate.getText() != null)
                    && (txtDate.getText().trim().length()>0)){
				dtFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
                proposalDevelopmentFormBean.setDeadLineDate(
                        new java.sql.Date(dtFormat.parse(
                                dtUtils.restoreDate(txtDate.getText(),
                                        "/-:,")).getTime()));
            } else {
                 proposalDevelopmentFormBean.setDeadLineDate(null);
            }


            if ( rBtnPostMark.isSelected() ) {
                proposalDevelopmentFormBean.setDeadLineType("P");
            } else if ( rBtnReciept.isSelected() ) {
                proposalDevelopmentFormBean.setDeadLineType("R");
            }

            if ( rBtnOSP.isSelected() ) {
                proposalDevelopmentFormBean.setMailBy("O");
            } else if ( rBtnDepartment.isSelected() ) {
                proposalDevelopmentFormBean.setMailBy("D");
            }

            if ( rBtnRegular.isSelected() ) {
                proposalDevelopmentFormBean.setMailType("R");
            } else if ( rBtnCourier.isSelected() ) {
                proposalDevelopmentFormBean.setMailType("D");
            } else if ( rBtnElectronic.isSelected() ) {
                proposalDevelopmentFormBean.setMailType("E");
            }
            proposalDevelopmentFormBean.setMailAccountNumber(
            txtMailAccount.getText().trim().length() == 0 ? null : txtMailAccount.getText());
            proposalDevelopmentFormBean.setNumberCopies(
            txtNoOfCopies.getText().trim().length() == 0 ? null : txtNoOfCopies.getText());
            proposalDevelopmentFormBean.setCarrierCode(
            txtCarrierCode.getText().trim().length() == 0 ? null : txtCarrierCode.getText());
            proposalDevelopmentFormBean.setCarrierCodeType(
            txtCarrierCodeType.getText().trim().length() == 0 ? null : txtCarrierCodeType.getText());
            proposalDevelopmentFormBean.setMailingAddressId(mailingAddressId);
            proposalDevelopmentFormBean.setMailDescription(
            txtMailDescription.getText().trim().length() == 0 ? null : txtMailDescription.getText());
            /** Bug Fix 1996 -start
             */
            if(mailingAddressId==0 || mailingAddressId == -1){
                proposalDevelopmentFormBean.setMailingAddress(null);
                proposalDevelopmentFormBean.setMailingAddressName(null);
            }
            /** Bug Fix 1996 -End
             */
    }
    /** Validate the deadline date againist the proposal start date.
     *If the Proposal start date is less than deadline date then show the validation message
     *case 2463
     */
    public boolean validateData(){
        boolean isValid = true;
        //Case 3593 - START
        /*Date deadLineDate = proposalDevelopmentFormBean.getDeadLineDate();
        Date proposalstartDate = proposalDevelopmentFormBean.getRequestStartDateInitial();
        if(deadLineDate!=null && deadLineDate.compareTo(proposalstartDate)>0){
            log(coeusMessageResources.parseMessageKey(
            "prop_invalid_sponsor_exceptionCode.2510"));
            isValid =  false;
        }*/
        //Case 3593 - END
        return isValid;
    }


    /** This method is used to format the mailing address which is received from
     *  the ProposalDevelopmentFormBean
     *
     * @param address which is unformatted
     * @return String containing formatted address
     * for this Proposal.
     */
    public String formatMailingAddress(String address){

        String fullAddress = "";
        StringTokenizer stAddress = null;
        stAddress = new StringTokenizer(address,"$#");
        while(stAddress.hasMoreTokens()){
            String addr = stAddress.nextToken();
            fullAddress += addr;
            fullAddress += (addr.length() > 0)? "\n" : "";
        }
        return fullAddress;
    }

   /**
    * Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field.
    */
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        JTextField dateField;
        String strDate = "";
        String oldData = "";
        boolean temporary = false;

        public void focusGained (FocusEvent fe){
            if (fe.getSource() instanceof JTextField){
                dateField = (JTextField)fe.getSource();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))) {
                    saveRequired = true;
                    oldData = dateField.getText();
                    String focusDate = dtUtils.restoreDate(
                            dateField.getText(),"/-:,");
                    dateField.setText(focusDate);
                }
            }
        }

        public void focusLost (FocusEvent fe){
            if (fe.getSource() instanceof JTextField){
                dateField = (JTextField)fe.getSource();
                temporary = fe.isTemporary();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))
                        && (!temporary) ) {
                    strDate = dateField.getText();
                    String convertedDate =
                            dtUtils.formatDate(dateField.getText(), "/-:," ,
                                    "dd-MMM-yyyy");
                    if (convertedDate==null){
                        saveRequired = true;
                            CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey(
                                    "proposal_MailingInfo_exceptionCode.7100"));
                        dateField.setText(oldData);
                        temporary = true;
                    }else {
                        dateField.setText(convertedDate);
                        temporary = false;
                        if(!oldData.equals(convertedDate)){
                            saveRequired = true;
                        }else{
                            saveRequired = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    public void log(String mesg) {

        CoeusOptionPane.showErrorDialog(mesg);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnBgDeadLine = new javax.swing.ButtonGroup();
        btnBgMailBy = new javax.swing.ButtonGroup();
        btnBgMailType = new javax.swing.ButtonGroup();
        rBtnDeadLine = new javax.swing.JRadioButton();
        rBtnmailBy = new javax.swing.JRadioButton();
        rBtnType = new javax.swing.JRadioButton();
        pnlMailAccountInfo = new javax.swing.JPanel();
        lblMailAccount = new javax.swing.JLabel();
        lblCarrierCode = new javax.swing.JLabel();
        lblMailingAddress = new javax.swing.JLabel();
        lblMailDescription = new javax.swing.JLabel();
        lblNoOfCopies = new javax.swing.JLabel();
        lblCarrierCodeType = new javax.swing.JLabel();
        txtArMailAddress = new javax.swing.JTextArea();
        btnSearchAddress = new javax.swing.JButton();
        btnClearAddress = new javax.swing.JButton();
        txtCarrierCodeType = new edu.mit.coeus.utils.CoeusTextField();
        txtMailAccount = new edu.mit.coeus.utils.CoeusTextField();
        txtNoOfCopies = new edu.mit.coeus.utils.CoeusTextField();
        txtCarrierCode = new edu.mit.coeus.utils.CoeusTextField();
        txtMailingAddress = new edu.mit.coeus.utils.CoeusTextField();
        txtMailDescription = new edu.mit.coeus.utils.CoeusTextField();
        pnlMailDeadLine = new javax.swing.JPanel();
        lblDate = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        rBtnPostMark = new javax.swing.JRadioButton();
        rBtnReciept = new javax.swing.JRadioButton();
        txtDate = new edu.mit.coeus.utils.CoeusTextField();
        pnlMailBy = new javax.swing.JPanel();
        rBtnOSP = new javax.swing.JRadioButton();
        rBtnDepartment = new javax.swing.JRadioButton();
        pnlMailType = new javax.swing.JPanel();
        rBtnRegular = new javax.swing.JRadioButton();
        rBtnCourier = new javax.swing.JRadioButton();
        rBtnElectronic = new javax.swing.JRadioButton();
        rBtnDeadLine.setText("jRadioButton1");
        btnBgDeadLine.add(rBtnDeadLine);
        rBtnmailBy.setText("jRadioButton1");
	    btnBgMailBy.add(rBtnmailBy);
        rBtnType.setText("jRadioButton1");
	    btnBgMailType.add(rBtnType);

        setLayout(new java.awt.GridBagLayout());

        pnlMailAccountInfo.setLayout(new java.awt.GridBagLayout());

        lblMailAccount.setFont(CoeusFontFactory.getLabelFont());
        lblMailAccount.setText("Mail Account: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 1);
        pnlMailAccountInfo.add(lblMailAccount, gridBagConstraints);

        lblCarrierCode.setFont(CoeusFontFactory.getLabelFont());
        lblCarrierCode.setText("Carrier Code: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 0);
        pnlMailAccountInfo.add(lblCarrierCode, gridBagConstraints);

        lblMailingAddress.setFont(CoeusFontFactory.getLabelFont());
        lblMailingAddress.setText("Mailing Address: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        pnlMailAccountInfo.add(lblMailingAddress, gridBagConstraints);

        lblMailDescription.setFont(CoeusFontFactory.getLabelFont());
        lblMailDescription.setText("Mail Description: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlMailAccountInfo.add(lblMailDescription, gridBagConstraints);

        lblNoOfCopies.setFont(CoeusFontFactory.getLabelFont());
        lblNoOfCopies.setText("No Of Copies: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 1);
        pnlMailAccountInfo.add(lblNoOfCopies, gridBagConstraints);

        lblCarrierCodeType.setFont(CoeusFontFactory.getLabelFont());
        lblCarrierCodeType.setText("Carrier Code Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 22, 2, 0);
        pnlMailAccountInfo.add(lblCarrierCodeType, gridBagConstraints);

        txtArMailAddress.setEditable(false);
        txtArMailAddress.setFont(CoeusFontFactory.getNormalFont());
		txtArMailAddress.setBorder(new javax.swing.border.EtchedBorder());
        txtArMailAddress.setMaximumSize(new java.awt.Dimension(225, 125));
        txtArMailAddress.setMinimumSize(new java.awt.Dimension(225, 125));
        txtArMailAddress.setPreferredSize(new java.awt.Dimension(225, 125));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
        pnlMailAccountInfo.add(txtArMailAddress, gridBagConstraints);

        btnSearchAddress.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
        btnSearchAddress.setDisabledIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.FIND_ICON)));
        btnSearchAddress.setMaximumSize(new java.awt.Dimension(23, 23));
        btnSearchAddress.setMinimumSize(new java.awt.Dimension(23, 23));
        btnSearchAddress.setPreferredSize(new java.awt.Dimension(23, 23));
        btnSearchAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchAddressActionPerformed();
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlMailAccountInfo.add(btnSearchAddress, gridBagConstraints);

        btnClearAddress.setIcon(new javax.swing.ImageIcon(
            getClass().getClassLoader().getResource("images/delete1.gif"))
    );
    btnClearAddress.setDisabledIcon(new javax.swing.ImageIcon(
        getClass().getClassLoader().getResource("images/delete1.gif"))
    );
    btnClearAddress.setDisabledSelectedIcon(new javax.swing.ImageIcon(
        getClass().getClassLoader().getResource("images/delete1.gif"))
    );
    btnClearAddress.setMaximumSize(new java.awt.Dimension(23, 23));
    btnClearAddress.setMinimumSize(new java.awt.Dimension(23, 23));
    btnClearAddress.setPreferredSize(new java.awt.Dimension(23, 23));
    btnClearAddress.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            clearAddressActionPerformed();
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    pnlMailAccountInfo.add(btnClearAddress, gridBagConstraints);

    txtCarrierCodeType.setDocument(new LimitedPlainDocument(3));
    txtCarrierCodeType.setFont(CoeusFontFactory.getNormalFont());
    txtCarrierCodeType.setPreferredSize(new java.awt.Dimension(31, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 2);
    pnlMailAccountInfo.add(txtCarrierCodeType, gridBagConstraints);

    txtMailAccount.setDocument(//Modified for Case#2402 - use a parameter to set the length of the account number throughout app
        //new LimitedPlainDocument(7)
        new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
    txtMailAccount.setFont(CoeusFontFactory.getNormalFont());
    txtMailAccount.setPreferredSize(new java.awt.Dimension(200, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 1);
    pnlMailAccountInfo.add(txtMailAccount, gridBagConstraints);

    txtNoOfCopies.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 7));
    txtNoOfCopies.setFont(CoeusFontFactory.getNormalFont());
    txtNoOfCopies.setMinimumSize(new java.awt.Dimension(45, 20));
    txtNoOfCopies.setPreferredSize(new java.awt.Dimension(45, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 8;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
    pnlMailAccountInfo.add(txtNoOfCopies, gridBagConstraints);

    txtCarrierCode.setDocument(new LimitedPlainDocument(20));
    txtCarrierCode.setToolTipText("");
    txtCarrierCode.setFont(CoeusFontFactory.getNormalFont());
    txtCarrierCode.setMinimumSize(new java.awt.Dimension(145, 20));
    txtCarrierCode.setPreferredSize(new java.awt.Dimension(145, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(1, 2, 2, 0);
    pnlMailAccountInfo.add(txtCarrierCode, gridBagConstraints);

    txtMailingAddress.setEditable(false);
    txtMailingAddress.setToolTipText("");
    txtMailingAddress.setFont(CoeusFontFactory.getNormalFont());
    txtMailingAddress.setMinimumSize(new java.awt.Dimension(200, 20));
    txtMailingAddress.setPreferredSize(new java.awt.Dimension(200, 20));
    txtMailingAddress.setEnabled(false);
    txtMailingAddress.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            ProposalMailingInfoAdminForm.this.mouseClicked(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
    pnlMailAccountInfo.add(txtMailingAddress, gridBagConstraints);

    txtMailDescription.setDocument(new LimitedPlainDocument(80));
    txtMailDescription.setToolTipText("");
    txtMailDescription.setFont(CoeusFontFactory.getNormalFont());
    txtMailDescription.setMinimumSize(new java.awt.Dimension(145, 20));
    txtMailDescription.setPreferredSize(new java.awt.Dimension(145, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.gridwidth = 8;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
    pnlMailAccountInfo.add(txtMailDescription, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.gridheight = 8;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(1, 3, 3, 2);
    add(pnlMailAccountInfo, gridBagConstraints);

    pnlMailDeadLine.setLayout(new java.awt.GridBagLayout());

	pnlMailDeadLine.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), "Deadline", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11), new java.awt.Color(255, 0, 0)));
    pnlMailDeadLine.setMaximumSize(new java.awt.Dimension(250, 115));
    pnlMailDeadLine.setMinimumSize(new java.awt.Dimension(250, 115));
    pnlMailDeadLine.setPreferredSize(new java.awt.Dimension(250, 115));
    lblDate.setFont(CoeusFontFactory.getLabelFont());
    lblDate.setText("Date: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 3);
    pnlMailDeadLine.add(lblDate, gridBagConstraints);

    lblType.setFont(CoeusFontFactory.getLabelFont());
    lblType.setText("Type: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 3);
    pnlMailDeadLine.add(lblType, gridBagConstraints);

    rBtnPostMark.setFont(CoeusFontFactory.getLabelFont());
    rBtnPostMark.setText("Postmark");
    rBtnPostMark.setToolTipText("");
	btnBgDeadLine.add(rBtnPostMark);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    pnlMailDeadLine.add(rBtnPostMark, gridBagConstraints);

    rBtnReciept.setFont(CoeusFontFactory.getLabelFont());
    rBtnReciept.setText("Receipt");
    btnBgDeadLine.add(rBtnReciept);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    pnlMailDeadLine.add(rBtnReciept, gridBagConstraints);

    txtDate.setFont(CoeusFontFactory.getNormalFont());
    txtDate.setMinimumSize(new java.awt.Dimension(140, 20));
    txtDate.setPreferredSize(new java.awt.Dimension(140, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
    pnlMailDeadLine.add(txtDate, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 6, 0);
    add(pnlMailDeadLine, gridBagConstraints);

    pnlMailBy.setLayout(new java.awt.GridBagLayout());

    pnlMailBy.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Mail By", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11), new java.awt.Color(255, 0, 0)));
    pnlMailBy.setMaximumSize(new java.awt.Dimension(250, 115));
    pnlMailBy.setMinimumSize(new java.awt.Dimension(250, 115));
    pnlMailBy.setPreferredSize(new java.awt.Dimension(182, 115));
    rBtnOSP.setFont(CoeusFontFactory.getLabelFont());
    rBtnOSP.setText("OSP");
    btnBgMailBy.add(rBtnOSP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    pnlMailBy.add(rBtnOSP, gridBagConstraints);

    rBtnDepartment.setFont(CoeusFontFactory.getLabelFont());
    rBtnDepartment.setText("Department");
    btnBgMailBy.add(rBtnDepartment);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    pnlMailBy.add(rBtnDepartment, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 6, 3);
    add(pnlMailBy, gridBagConstraints);

    pnlMailType.setLayout(new java.awt.GridBagLayout());

	pnlMailType.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), "Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11), new java.awt.Color(255, 0, 0)));
// JM 8-29-2011 increased x dimension to allow for longer tags (140 to 240 and 155 to 255)
	pnlMailType.setMaximumSize(new java.awt.Dimension(240, 115));
    pnlMailType.setMinimumSize(new java.awt.Dimension(240, 115));
    pnlMailType.setPreferredSize(new java.awt.Dimension(255, 115));
// END
    rBtnRegular.setFont(CoeusFontFactory.getLabelFont());
    rBtnRegular.setText(coeusMessageResources.parseLabelKey("mailingInfoType.3000"));
    btnBgMailType.add(rBtnRegular);
    rBtnRegular.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    pnlMailType.add(rBtnRegular, gridBagConstraints);

    rBtnCourier.setFont(CoeusFontFactory.getLabelFont());
    rBtnCourier.setText(coeusMessageResources.parseLabelKey("mailingInfoType.3001"));
    btnBgMailType.add(rBtnCourier);
    rBtnCourier.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    rBtnCourier.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    pnlMailType.add(rBtnCourier, gridBagConstraints);

    rBtnElectronic.setFont(CoeusFontFactory.getLabelFont());
    rBtnElectronic.setText(coeusMessageResources.parseLabelKey("mailingInfoType.3002"));
    btnBgMailType.add(rBtnElectronic);
    rBtnElectronic.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    rBtnElectronic.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    pnlMailType.add(rBtnElectronic, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 6, 2);
    add(pnlMailType, gridBagConstraints);

    }//GEN-END:initComponents

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        if ((evt.getSource() == txtMailingAddress) &&
                evt.getClickCount() == 2) {

                //invoke Organization display
                try {
                    if (mailingAddressId > 0) {
                        RolodexMaintenanceDetailForm detForm = new
                            RolodexMaintenanceDetailForm('V', mailingAddressId+"");
                                detForm.showForm(mdiForm,"Rolodex Details", true);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    log("Exception while opening Rolodex Details Window"
                                                            + e.getMessage());
                }
        }
    }//GEN-LAST:event_mouseClicked

    private void clearAddressActionPerformed() {//GEN-FIRST:event_clearAddressActionPerformed
        if (txtMailingAddress.getText() != null && !txtMailingAddress.getText().equals("")) {

            String msg = "Are you sure you want to remove mailing address entry? \n" +
                            txtMailingAddress.getText();
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);

            switch(confirm){
                case(JOptionPane.YES_OPTION):
                    try{
                        txtMailingAddress.setText("");
                        txtArMailAddress.setText("");
                        mailingAddressId = -1;
                    }catch(Exception ex){
                        String exMsg = ex.getMessage();
                        log(exMsg);
                    }
                    setSaveRequired(true);
                    break;
            }
        }
    }//GEN-LAST:event_clearAddressActionPerformed

    private void searchAddressActionPerformed() {//GEN-FIRST:event_searchAddressActionPerformed
                try {
                CoeusSearch coeusSearch = new CoeusSearch(
                            mdiForm, "ROLODEXSEARCH", 1);
                coeusSearch.showSearchWindow();
                String contactAddress = "";
                String mailingAddressName = "";
                HashMap rolodexSelected = coeusSearch.getSelectedRow();
                if (rolodexSelected != null && !rolodexSelected.isEmpty() ) {
                    mailingAddressId = new Integer(Utils.convertNull(rolodexSelected.get(
                                                        "ROLODEX_ID"))).intValue();
                    String firstName = Utils.convertNull(rolodexSelected.get(
                                                            "FIRST_NAME"));
                    String middleName = Utils.convertNull(rolodexSelected.get(
                                                            "MIDDLE_NAME"));
                    String lastName = Utils.convertNull(rolodexSelected.get(
                                                            "LAST_NAME"));
                    String prefix = Utils.convertNull(rolodexSelected.get(
                                                            "PREFIX"));
                    String suffix = Utils.convertNull(rolodexSelected.get(
                                                            "SUFFIX"));
                    if (lastName.length() > 0) {
                        mailingAddressName = (lastName + " "+ suffix +", "+
                            prefix + " "+ firstName + " "+ middleName).trim();
                    } else {
                        mailingAddressName = Utils.convertNull(
                                    rolodexSelected.get("ORGANIZATION"));
                    }
                    txtMailingAddress.setText(Utils.convertNull(mailingAddressName));

                    String temp = Utils.convertNull(rolodexSelected.get(
                                                        "ORGANIZATION"));

                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + "\n";
                    }
                    temp = Utils.convertNull(rolodexSelected.get(
                                                            "ADDRESS_LINE_1"));
                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + "\n";
                    }
                    temp = Utils.convertNull(rolodexSelected.get(
                                                            "ADDRESS_LINE_2"));
                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + "\n";
                    }
                    temp = Utils.convertNull(rolodexSelected.get(
                                                            "ADDRESS_LINE_3"));
                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + "\n";
                    }
                    temp = Utils.convertNull(rolodexSelected.get("CITY"));
                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + "\n";
                    }
                    temp = Utils.convertNull(rolodexSelected.get(
                                                            "STATE_DESCRIPTION"));
                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + " ";
                    }
                    temp = Utils.convertNull(rolodexSelected.get("POSTAL_CODE"));
                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + ", ";
                    }
                    temp = Utils.convertNull(rolodexSelected.get("COUNTRY_NAME"));
                    if (temp != null && temp.length() > 0) {
                        contactAddress = contactAddress + temp + " ";
                    }

                    txtArMailAddress.setText(Utils.convertNull(contactAddress));
                }

                saveRequired = true;
            } catch (Exception e) {
                //e.printStackTrace();
                log("Coeus Search is not available.." + e.getMessage());
            }
    }//GEN-LAST:event_searchAddressActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnBgDeadLine;
    private javax.swing.ButtonGroup btnBgMailBy;
    private javax.swing.ButtonGroup btnBgMailType;
    private javax.swing.JButton btnClearAddress;
    private javax.swing.JButton btnSearchAddress;
    private javax.swing.JLabel lblCarrierCode;
    private javax.swing.JLabel lblCarrierCodeType;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblMailAccount;
    private javax.swing.JLabel lblMailDescription;
    private javax.swing.JLabel lblMailingAddress;
    private javax.swing.JLabel lblNoOfCopies;
    private javax.swing.JLabel lblType;
    private javax.swing.JPanel pnlMailAccountInfo;
    private javax.swing.JPanel pnlMailBy;
    private javax.swing.JPanel pnlMailDeadLine;
    private javax.swing.JPanel pnlMailType;
    private javax.swing.JRadioButton rBtnCourier;
    private javax.swing.JRadioButton rBtnDeadLine;
    private javax.swing.JRadioButton rBtnDepartment;
    private javax.swing.JRadioButton rBtnElectronic;
    private javax.swing.JRadioButton rBtnOSP;
    private javax.swing.JRadioButton rBtnPostMark;
    private javax.swing.JRadioButton rBtnReciept;
    private javax.swing.JRadioButton rBtnRegular;
    private javax.swing.JRadioButton rBtnType;
    private javax.swing.JRadioButton rBtnmailBy;
    private javax.swing.JTextArea txtArMailAddress;
    private edu.mit.coeus.utils.CoeusTextField txtCarrierCode;
    private edu.mit.coeus.utils.CoeusTextField txtCarrierCodeType;
    private edu.mit.coeus.utils.CoeusTextField txtDate;
    private edu.mit.coeus.utils.CoeusTextField txtMailAccount;
    private edu.mit.coeus.utils.CoeusTextField txtMailDescription;
    private edu.mit.coeus.utils.CoeusTextField txtMailingAddress;
    private edu.mit.coeus.utils.CoeusTextField txtNoOfCopies;
    // End of variables declaration//GEN-END:variables

}
