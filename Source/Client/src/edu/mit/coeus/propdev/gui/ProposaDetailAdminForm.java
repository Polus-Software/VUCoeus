/*
 * @(#)ProposaDetailAdminForm.java  1.0  April 07, 2003, 10:06 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import edu.mit.coeus.propdev.bean.*;

/** <CODE>ProposaDetailAdminForm</CODE> is a form object which display
 * the proposal details for the selected proposal and can be used to
 * <CODE>add/modify/display</CODE> the key personl details.
 * This class is instantiated in <CODE>ProposalDetailForm</CODE>.
 *
 * @author  Raghunath P.V.
 * @version: 1.0 Created on April 07, 2003, 10:06 AM
 */

public class ProposaDetailAdminForm extends javax.swing.JPanel 
                    implements ItemListener, TypeConstants, ActionListener{
    
    private char functionType;
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    private boolean saveRequired;
    private Vector vecProposalTypes;
    private Vector vecNSFCodes;
    private Vector vecNoticeOfOpportunities;
    private Vector vecActivityCodes;
    private DateUtils dtUtils;
    
        //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    /* holds the reference of parent object*/
    private CoeusAppletMDIForm mdiForm;

    /** Creates new form ProposalDetail */
    public ProposaDetailAdminForm() {
        initComponents();
    }
    
    /** Creates new form <CODE>ProposaDetailAdminForm</CODE> with the given Protocol
     * details and specified <CODE>functionType</CODE>.
     * @param functionType which specifies the mode in which the form will be
     * opened.
     * @param infoBean <CODE>ProposalDevelopmentFormBean</CODE> with all the details regarding to
     * proposal.
     */
    public ProposaDetailAdminForm( char functionType,
                                                ProposalDevelopmentFormBean propDevelopmentFormBean){
        this.functionType = functionType;
        this.proposalDevelopmentFormBean = propDevelopmentFormBean;
    }
    
     /** This Method is used to get the functionType
     * @return a fuctionType like 'A','D','M'.
     */
    
    public char getFunctionType(){
        return functionType;
    }

    /** This Method is used to set the functionType
     * @param fType is a Char data like 'A','D','M'.
     */
    
    public void setFunctionType(char fType){
        this.functionType = fType;
    }

    /** This method is used to initialize the form components, set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     * @param mdiForm reference to the <CODE>CoeusAppletMDIForm</CODE>.
     * @return JComponent reference to the <CODE>ProposaDetailAdminForm</CODE> component
     * after initializing and setting the data.
     */
    public JComponent showProposalDetailAdminForm(CoeusAppletMDIForm mdiForm){
        
        this.mdiForm = mdiForm;
        dtUtils = new DateUtils();
        initComponents();
        postInitComponents();
        addListenersToComponents();
        setFormData();
        formatFields();
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }

    private void postInitComponents(){
        
        txtProposalNum.setEditable(false);
        txtProposalNum.setOpaque(false);
        txtStatus.setEditable(false);
        txtStatus.setOpaque(false);
        txtLeadUnit.setEditable(false);
        txtLeadUnit.setOpaque(false);
        
    }
    
    private void addListenersToComponents(){
        
        if(functionType != 'D'){
            
            txtEndDate.addFocusListener(new CustomFocusAdapter());
            txtStartDate.addFocusListener(new CustomFocusAdapter());
            
            cmbActivityCode.addItemListener( this );
            cmbNSFCode.addItemListener( this );
            cmbNoticeOfOpportunity.addItemListener( this );
            cmbProposalType.addItemListener( this );
            
            btnAwardSearch.addActionListener(this);
            btnPrimeSponsor.addActionListener(this);
            btnSponsorSearch.addActionListener(this);
            
        }
    }
    
    private void setFormData(){
        
        if( ( vecActivityCodes != null ) && ( vecActivityCodes.size() > 0 ) 
                && ( cmbActivityCode.getItemCount() == 0 ) ) {
                    
            int activityCodesSize = vecActivityCodes.size();
            for(int index = 0 ; index < activityCodesSize ; index++){
                cmbActivityCode.addItem((ComboBoxBean)vecActivityCodes.elementAt(index));
            }
        }
        
        if( ( vecNSFCodes != null ) && ( vecNSFCodes.size() > 0 ) 
                && ( cmbNSFCode.getItemCount() == 0 ) ) {
                    
            int nsfCodesSize = vecNSFCodes.size();
            for(int index = 0 ; index < nsfCodesSize ; index++){
                cmbNSFCode.addItem((ComboBoxBean)vecNSFCodes.elementAt(index));
            }
        }
        
        if( ( vecNoticeOfOpportunities != null ) && ( vecNoticeOfOpportunities.size() > 0 ) 
                && ( cmbNoticeOfOpportunity.getItemCount() == 0 ) ) {
                    
            int noticeSize = vecNoticeOfOpportunities.size();
            for(int index = 0 ; index < noticeSize ; index++){
                cmbNoticeOfOpportunity.addItem((ComboBoxBean)vecNoticeOfOpportunities.elementAt(index));
            }
        }
        
        if( ( vecProposalTypes != null ) && ( vecProposalTypes.size() > 0 ) 
                && ( cmbProposalType.getItemCount() == 0 ) ) {
                    
            int proposalTypeSize = vecProposalTypes.size();
            for(int index = 0 ; index < proposalTypeSize ; index++){
                cmbProposalType.addItem((ComboBoxBean)vecProposalTypes.elementAt(index));
            }
        }
        
        if(proposalDevelopmentFormBean != null){
            
            String stProposalNumber = proposalDevelopmentFormBean.getProposalNumber();
            String stProposalTypeDesc = proposalDevelopmentFormBean.getProposalTypeDesc();

            int statusCode = proposalDevelopmentFormBean.getStatusCode();
            
            int activityCode = proposalDevelopmentFormBean.getProposalActivityTypeCode();
            int noticeOfOpportunityCode = proposalDevelopmentFormBean.getNoticeOfOpportunitycode();
            int proposalTypeCode = proposalDevelopmentFormBean.getProposalTypeCode();
            String nsfCode = proposalDevelopmentFormBean.getNsfCode();
            
            String stBaseProposalNumber = proposalDevelopmentFormBean.getBaseProposalNumber();
            String stTitle = proposalDevelopmentFormBean.getTitle();
            
            String stSponsorCode = proposalDevelopmentFormBean.getSponsorCode();
            String stPrimeSponsorCode = proposalDevelopmentFormBean.getPrimeSponsorCode();
            String stCurrentAwardNumber = proposalDevelopmentFormBean.getCurrentAwardNumber();
            
            String stProgramNo = proposalDevelopmentFormBean.getProgramAnnouncementNumber();
            String stProgramTitle = proposalDevelopmentFormBean.getProgramAnnouncementTitle();

            String stNarrativeStatus = proposalDevelopmentFormBean.getNarrativeStatus();
            String stBudgetStatus = proposalDevelopmentFormBean.getBudgetStatus();
            String stUpdateUser = proposalDevelopmentFormBean.getUpdateUser();
            String stLastUpdate = proposalDevelopmentFormBean.getUpdateTimestamp().toString();
            
            Date stStartDate = proposalDevelopmentFormBean.getRequestStartDateInitial();
            Date stEndDate = proposalDevelopmentFormBean.getRequestEndDateInitial();

            /*
            txtAwardNo
            txtBudget
            txtEndDate
            txtLastUpdate
            txtLeadUnit
            txtNarrative
            txtPrimeSponsor
            txtProgramNo
            txtProposalNum
            txtSponsorCode
            txtSponsorProposalNo
            txtStartDate
            txtStatus
            txtUpdateUser
            txtArProgramTitle
            txtArTitle
            cmbActivityCode
            cmbNSFCode
            cmbNoticeOfOpportunity
            cmbProposalType
            */
        }
        
        
        
        
        
    }
    
    private void formatFields(){
        
        boolean enabled = functionType != 'D' ? true : false ;
        
        txtAwardNo.setEditable(enabled);
        txtSponsorCode.setEditable(enabled);
        txtPrimeSponsor.setEditable(enabled);
        
        cmbActivityCode.setEnabled(enabled);
        cmbNSFCode.setEnabled(enabled);
        cmbNoticeOfOpportunity.setEnabled(enabled);
        cmbProposalType.setEnabled(enabled);
        

        txtAwardNo.setOpaque(enabled);
        txtSponsorCode.setOpaque(false);
        cmbActivityCode.setOpaque(enabled);
        cmbNSFCode.setOpaque(enabled);
        cmbNoticeOfOpportunity.setOpaque(enabled);
        cmbProposalType.setOpaque(enabled);

        txtEndDate.setEditable(enabled);
        txtStartDate.setEditable(enabled);
        
        txtArTitle.setEditable(enabled);
        txtArProgramTitle.setEditable(enabled);
        txtSponsorProposalNo.setEditable(enabled);
        txtProgramNo.setEditable(enabled);

        txtNarrative.setEnabled(enabled);
        txtBudget.setEnabled(enabled);
        txtUpdateUser.setEnabled(enabled);
        txtLastUpdate.setEnabled(enabled);
        
    }

        /** This method is used to check whether any unsaved modifications are
     * available.
     * @return true if there are any modifications that are not saved,
     * else false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }

    /** This method is used to set the saveRequired flag.
     * @param save boolean value which specifies any unsaved changes are present
     * or not.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    /** This method is used to set the <CODE>ProtocolInfoBean</CODE> with all the details
     * of the Protocol.
     * @param infoBean <CODE>ProtocolInfoBean</CODE> which consists of all the details of
     * Protocol like Vulnerable Subjects, Locations, Investigator details etc.
     */
    public void setValues(ProposalDevelopmentFormBean infoBean){
        this.proposalDevelopmentFormBean = infoBean;
        setFormData();
        formatFields();
    }
    
    /**
     * This method is used to set the available proposal Types.
     * @param propTypes collection of available Proposal Types
     */
    public void setProposalTypes(Vector propTypes){
        this.vecProposalTypes = propTypes;
    }

     /** This method is used to the set the collection of Available NSF Codes
      * information.
      * @param nsfCodes Collection of available NSF Codes Status details.
      */
    public void setNSFCodes(Vector nsfCodes){
        this.vecNSFCodes = nsfCodes;
    }
    
     /** This method is used to the set the collection of Available Notice Of Opportuniies
      * information.
      * @param opportunities Collection of available Available Notice Of Opportuniies
      */
    public void setNoticeOfOpportunities(Vector opportunities){
        this.vecNoticeOfOpportunities = opportunities;
    }
    
    /** This method is used to the set the collection of Available Activity code
      * information.
      * @param activityCodes Collection of available Activity Code details.
      */
    public void setActivityCodes(Vector activityCodes){
        this.vecActivityCodes = activityCodes;
    }
    
     /** This method is used throw the exception with the given message.
     * @param mesg String which will describe the exception
     * @throws Exception with the given custom message.
     */
    public void log(String mesg) throws Exception {
        throw new Exception(mesg);
    }
    
    /** Validate the form data before sending to the database.
     *
     * @return true if all form controls have valid data, else false.
     * @throws Exception with the custom message if any form control doesn't
     * have valid data.
     */
    public boolean validateData() throws Exception{
        return false;
    }
    
    /** This method is used to get the <CODE>ProposaDetailAdminForm</CODE> data.
     * @return <CODE>ProposalDevelopmentFormBean</CODE> which consists of all the details given by the
     * user in the form.
     * @throws Exception with custom if fetching of the details from the form
     * fails.
     */
    public ProposalDevelopmentFormBean getFormData() throws Exception{
        return null;
    }
    
    /**
     * This method is used to set the proposal number which will be used through
     * out the program for reference.
     *
     * @param proposalId String representing proposal number.
     */
   /*public void setProtocolNumber( String proposalID ){
        txtProtocolId.setText( protocolID );
    }*/
    
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblProposalNum = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblLeadUnit = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblProposalType = new javax.swing.JLabel();
        lblAwardNo = new javax.swing.JLabel();
        btnAwardSearch = new javax.swing.JButton();
        lblNSFCode = new javax.swing.JLabel();
        chkOtherAgency = new javax.swing.JCheckBox();
        chkSubcontract = new javax.swing.JCheckBox();
        cmbProposalType = new edu.mit.coeus.utils.CoeusComboBox();
        cmbNSFCode = new edu.mit.coeus.utils.CoeusComboBox();
        lblSponsorCode = new javax.swing.JLabel();
        btnSponsorSearch = new javax.swing.JButton();
        lblSponsorName = new javax.swing.JLabel();
        lblPrimeSponsor = new javax.swing.JLabel();
        btnPrimeSponsor = new javax.swing.JButton();
        lblPrimeSponsorName = new javax.swing.JLabel();
        lblSponsorPropNum = new javax.swing.JLabel();
        lblActivityCode = new javax.swing.JLabel();
        cmbActivityCode = new edu.mit.coeus.utils.CoeusComboBox();
        lblProgramTitle = new javax.swing.JLabel();
        scrPnProgramTitle = new javax.swing.JScrollPane();
        txtArProgramTitle = new javax.swing.JTextArea();
        lblNoticeOfOpportunity = new javax.swing.JLabel();
        cmbNoticeOfOpportunity = new edu.mit.coeus.utils.CoeusComboBox();
        lblProgramNum = new javax.swing.JLabel();
        lblNarrative = new javax.swing.JLabel();
        lblBudget = new javax.swing.JLabel();
        lblLastUpdate = new javax.swing.JLabel();
        lblUpdateUser = new javax.swing.JLabel();
        lblOtherAgency = new javax.swing.JLabel();
        txtProposalNum = new edu.mit.coeus.utils.CoeusTextField();
        txtStatus = new edu.mit.coeus.utils.CoeusTextField();
        txtStartDate = new edu.mit.coeus.utils.CoeusTextField();
        txtEndDate = new edu.mit.coeus.utils.CoeusTextField();
        txtAwardNo = new edu.mit.coeus.utils.CoeusTextField();
        txtSponsorCode = new edu.mit.coeus.utils.CoeusTextField();
        txtPrimeSponsor = new edu.mit.coeus.utils.CoeusTextField();
        txtSponsorProposalNo = new edu.mit.coeus.utils.CoeusTextField();
        txtNarrative = new edu.mit.coeus.utils.CoeusTextField();
        txtLastUpdate = new edu.mit.coeus.utils.CoeusTextField();
        txtProgramNo = new edu.mit.coeus.utils.CoeusTextField();
        txtBudget = new edu.mit.coeus.utils.CoeusTextField();
        txtUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        txtLeadUnit = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        lblProposalNum.setText("Proposal No:");
        lblProposalNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalNum.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        add(lblProposalNum, gridBagConstraints);

        lblStatus.setText("Status:");
        lblStatus.setToolTipText("Status");
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        add(lblStatus, gridBagConstraints);

        lblLeadUnit.setText("Lead Unit:");
        lblLeadUnit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLeadUnit.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblLeadUnit, gridBagConstraints);

        lblStartDate.setText("Start Date:");
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblStartDate, gridBagConstraints);

        lblEndDate.setText("End Date:");
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblEndDate, gridBagConstraints);

        lblTitle.setText("Title:");
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblTitle, gridBagConstraints);

        scrPnTitle.setPreferredSize(new java.awt.Dimension(468, 43));
        scrPnTitle.setMinimumSize(new java.awt.Dimension(468, 43));
        scrPnTitle.setMaximumSize(new java.awt.Dimension(468, 43));
        txtArTitle.setDocument(new LimitedPlainDocument(150));
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setPreferredSize(new java.awt.Dimension(392, 40));
        txtArTitle.setMaximumSize(new java.awt.Dimension(392, 40));
        txtArTitle.setMinimumSize(new java.awt.Dimension(392, 40));
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(scrPnTitle, gridBagConstraints);

        lblProposalType.setText("Proposal Type:");
        lblProposalType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalType.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProposalType, gridBagConstraints);

        lblAwardNo.setText("Award No:");
        lblAwardNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAwardNo.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblAwardNo, gridBagConstraints);

        btnAwardSearch.setIcon(new javax.swing.ImageIcon( getClass().getResource(CoeusGuiConstants.FIND_ICON)));
        btnAwardSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        btnAwardSearch.setMaximumSize(new java.awt.Dimension(23, 23));
        btnAwardSearch.setMinimumSize(new java.awt.Dimension(23, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        add(btnAwardSearch, gridBagConstraints);

        lblNSFCode.setText("NSF Code:");
        lblNSFCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNSFCode.setFont(CoeusFontFactory.getLabelFont()
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblNSFCode, gridBagConstraints);

        chkOtherAgency.setMnemonic('O');
        chkOtherAgency.setFont(CoeusFontFactory.getLabelFont());
        chkOtherAgency.setPreferredSize(new java.awt.Dimension(20, 20));
        chkOtherAgency.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chkOtherAgency.setMaximumSize(new java.awt.Dimension(20, 20));
        chkOtherAgency.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkOtherAgency.setMinimumSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        add(chkOtherAgency, gridBagConstraints);

        chkSubcontract.setMnemonic('S');
        chkSubcontract.setFont(CoeusFontFactory.getLabelFont());
        chkSubcontract.setText("Subcontract");
        chkSubcontract.setPreferredSize(new java.awt.Dimension(95, 20));
        chkSubcontract.setMaximumSize(new java.awt.Dimension(95, 20));
        chkSubcontract.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkSubcontract.setMinimumSize(new java.awt.Dimension(95, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 2);
        add(chkSubcontract, gridBagConstraints);

        cmbProposalType.setPreferredSize(new java.awt.Dimension(175, 20));
        cmbProposalType.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbProposalType.setMaximumSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(cmbProposalType, gridBagConstraints);

        cmbNSFCode.setPreferredSize(new java.awt.Dimension(175, 20));
        cmbNSFCode.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbNSFCode.setMaximumSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(cmbNSFCode, gridBagConstraints);

        lblSponsorCode.setText("Sponsor:");
        lblSponsorCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorCode.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        add(lblSponsorCode, gridBagConstraints);

        btnSponsorSearch.setIcon(new javax.swing.ImageIcon( getClass().getResource(CoeusGuiConstants.FIND_ICON)));
        btnSponsorSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        btnSponsorSearch.setMaximumSize(new java.awt.Dimension(23, 23));
        btnSponsorSearch.setMinimumSize(new java.awt.Dimension(23, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 5);
        add(btnSponsorSearch, gridBagConstraints);

        lblSponsorName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 2);
        add(lblSponsorName, gridBagConstraints);

        lblPrimeSponsor.setText("Prime Sponsor:");
        lblPrimeSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrimeSponsor.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblPrimeSponsor, gridBagConstraints);

        btnPrimeSponsor.setIcon(new javax.swing.ImageIcon( getClass().getResource(CoeusGuiConstants.FIND_ICON)));
        btnPrimeSponsor.setPreferredSize(new java.awt.Dimension(23, 23));
        btnPrimeSponsor.setMaximumSize(new java.awt.Dimension(23, 23));
        btnPrimeSponsor.setMinimumSize(new java.awt.Dimension(23, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        add(btnPrimeSponsor, gridBagConstraints);

        lblPrimeSponsorName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 2);
        add(lblPrimeSponsorName, gridBagConstraints);

        lblSponsorPropNum.setText("Sponsor Proposal No:");
        lblSponsorPropNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorPropNum.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblSponsorPropNum, gridBagConstraints);

        lblActivityCode.setText("Activity Code:");
        lblActivityCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblActivityCode.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblActivityCode, gridBagConstraints);

        cmbActivityCode.setPreferredSize(new java.awt.Dimension(175, 20));
        cmbActivityCode.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbActivityCode.setMaximumSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(cmbActivityCode, gridBagConstraints);

        lblProgramTitle.setText("Program Title:");
        lblProgramTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProgramTitle.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProgramTitle, gridBagConstraints);

        scrPnProgramTitle.setPreferredSize(new java.awt.Dimension(468, 43));
        scrPnProgramTitle.setMinimumSize(new java.awt.Dimension(468, 43));
        scrPnProgramTitle.setMaximumSize(new java.awt.Dimension(468, 43));
        txtArProgramTitle.setDocument(new LimitedPlainDocument(150));
        txtArProgramTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArProgramTitle.setPreferredSize(new java.awt.Dimension(392, 40));
        txtArProgramTitle.setMaximumSize(new java.awt.Dimension(392, 40));
        txtArProgramTitle.setMinimumSize(new java.awt.Dimension(392, 40));
        scrPnProgramTitle.setViewportView(txtArProgramTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(scrPnProgramTitle, gridBagConstraints);

        lblNoticeOfOpportunity.setText("Notice Of Opportunity:");
        lblNoticeOfOpportunity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoticeOfOpportunity.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblNoticeOfOpportunity, gridBagConstraints);

        cmbNoticeOfOpportunity.setPreferredSize(new java.awt.Dimension(175, 20));
        cmbNoticeOfOpportunity.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbNoticeOfOpportunity.setMaximumSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(cmbNoticeOfOpportunity, gridBagConstraints);

        lblProgramNum.setText("Program No:");
        lblProgramNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProgramNum.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProgramNum, gridBagConstraints);

        lblNarrative.setText("Narrative:");
        lblNarrative.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNarrative.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblNarrative, gridBagConstraints);

        lblBudget.setText("Budget:");
        lblBudget.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBudget.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblBudget, gridBagConstraints);

        lblLastUpdate.setText("Last Update:");
        lblLastUpdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastUpdate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(lblLastUpdate, gridBagConstraints);

        lblUpdateUser.setText("Update User:");
        lblUpdateUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdateUser.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(lblUpdateUser, gridBagConstraints);

        lblOtherAgency.setText("Other Agency:");
        lblOtherAgency.setFont(CoeusFontFactory.getLabelFont());
        lblOtherAgency.setPreferredSize(new java.awt.Dimension(80, 20));
        lblOtherAgency.setMinimumSize(new java.awt.Dimension(80, 20));
        lblOtherAgency.setMaximumSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 6);
        add(lblOtherAgency, gridBagConstraints);

        txtProposalNum.setDocument(new LimitedPlainDocument(8));
        txtProposalNum.setPreferredSize(new java.awt.Dimension(175, 23));
        txtProposalNum.setMaximumSize(new java.awt.Dimension(175, 23));
        txtProposalNum.setMinimumSize(new java.awt.Dimension(175, 23));
        txtProposalNum.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 0);
        add(txtProposalNum, gridBagConstraints);

        txtStatus.setPreferredSize(new java.awt.Dimension(175, 23));
        txtStatus.setMaximumSize(new java.awt.Dimension(175, 23));
        txtStatus.setMinimumSize(new java.awt.Dimension(175, 23));
        txtStatus.setRequestFocusEnabled(false);
        txtStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStatusActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 2);
        add(txtStatus, gridBagConstraints);

        txtStartDate.setDocument(new LimitedPlainDocument(11));
        txtStartDate.setPreferredSize(new java.awt.Dimension(175, 23));
        txtStartDate.setMaximumSize(new java.awt.Dimension(175, 23));
        txtStartDate.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtStartDate, gridBagConstraints);

        txtEndDate.setDocument(new LimitedPlainDocument(11));
        txtEndDate.setPreferredSize(new java.awt.Dimension(175, 23));
        txtEndDate.setMaximumSize(new java.awt.Dimension(175, 23));
        txtEndDate.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtEndDate, gridBagConstraints);

        txtAwardNo.setDocument(new LimitedPlainDocument(10));
        txtAwardNo.setPreferredSize(new java.awt.Dimension(175, 23));
        txtAwardNo.setMaximumSize(new java.awt.Dimension(175, 23));
        txtAwardNo.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtAwardNo, gridBagConstraints);

        txtSponsorCode.setPreferredSize(new java.awt.Dimension(175, 23));
        txtSponsorCode.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSponsorCode.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        add(txtSponsorCode, gridBagConstraints);

        txtPrimeSponsor.setPreferredSize(new java.awt.Dimension(175, 23));
        txtPrimeSponsor.setMaximumSize(new java.awt.Dimension(175, 23));
        txtPrimeSponsor.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtPrimeSponsor, gridBagConstraints);

        txtSponsorProposalNo.setDocument(new LimitedPlainDocument(70));
        txtSponsorProposalNo.setPreferredSize(new java.awt.Dimension(175, 23));
        txtSponsorProposalNo.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSponsorProposalNo.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtSponsorProposalNo, gridBagConstraints);

        txtNarrative.setPreferredSize(new java.awt.Dimension(175, 23));
        txtNarrative.setMaximumSize(new java.awt.Dimension(175, 23));
        txtNarrative.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtNarrative, gridBagConstraints);

        txtLastUpdate.setPreferredSize(new java.awt.Dimension(175, 23));
        txtLastUpdate.setMaximumSize(new java.awt.Dimension(175, 23));
        txtLastUpdate.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(txtLastUpdate, gridBagConstraints);

        txtProgramNo.setDocument(new LimitedPlainDocument(15));
        txtProgramNo.setPreferredSize(new java.awt.Dimension(175, 23));
        txtProgramNo.setMaximumSize(new java.awt.Dimension(175, 23));
        txtProgramNo.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtProgramNo, gridBagConstraints);

        txtBudget.setPreferredSize(new java.awt.Dimension(175, 23));
        txtBudget.setMaximumSize(new java.awt.Dimension(175, 23));
        txtBudget.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtBudget, gridBagConstraints);

        txtUpdateUser.setPreferredSize(new java.awt.Dimension(175, 23));
        txtUpdateUser.setMaximumSize(new java.awt.Dimension(175, 23));
        txtUpdateUser.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 2);
        add(txtUpdateUser, gridBagConstraints);

        txtLeadUnit.setPreferredSize(new java.awt.Dimension(468, 23));
        txtLeadUnit.setMaximumSize(new java.awt.Dimension(468, 23));
        txtLeadUnit.setMinimumSize(new java.awt.Dimension(468, 23));
        txtLeadUnit.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtLeadUnit, gridBagConstraints);

    }//GEN-END:initComponents

    private void txtStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStatusActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_txtStatusActionPerformed

    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
    }    
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.mit.coeus.utils.CoeusComboBox cmbActivityCode;
    private javax.swing.JTextArea txtArProgramTitle;
    private javax.swing.JLabel lblUpdateUser;
    private edu.mit.coeus.utils.CoeusTextField txtLeadUnit;
    private edu.mit.coeus.utils.CoeusComboBox cmbProposalType;
    private javax.swing.JLabel lblPrimeSponsorName;
    private javax.swing.JLabel lblAwardNo;
    private javax.swing.JCheckBox chkOtherAgency;
    private javax.swing.JLabel lblProgramNum;
    private edu.mit.coeus.utils.CoeusComboBox cmbNoticeOfOpportunity;
    private edu.mit.coeus.utils.CoeusTextField txtUpdateUser;
    private edu.mit.coeus.utils.CoeusTextField txtSponsorProposalNo;
    private edu.mit.coeus.utils.CoeusTextField txtStartDate;
    private javax.swing.JLabel lblNSFCode;
    private edu.mit.coeus.utils.CoeusTextField txtPrimeSponsor;
    private javax.swing.JTextArea txtArTitle;
    private edu.mit.coeus.utils.CoeusTextField txtProgramNo;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JLabel lblSponsorName;
    private edu.mit.coeus.utils.CoeusComboBox cmbNSFCode;
    private javax.swing.JLabel lblLeadUnit;
    private javax.swing.JLabel lblNarrative;
    private javax.swing.JLabel lblProgramTitle;
    private javax.swing.JLabel lblSponsorCode;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblBudget;
    private javax.swing.JCheckBox chkSubcontract;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JButton btnSponsorSearch;
    private javax.swing.JLabel lblLastUpdate;
    private edu.mit.coeus.utils.CoeusTextField txtAwardNo;
    private javax.swing.JScrollPane scrPnProgramTitle;
    private edu.mit.coeus.utils.CoeusTextField txtEndDate;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblNoticeOfOpportunity;
    private edu.mit.coeus.utils.CoeusTextField txtProposalNum;
    private edu.mit.coeus.utils.CoeusTextField txtBudget;
    private edu.mit.coeus.utils.CoeusTextField txtLastUpdate;
    private edu.mit.coeus.utils.CoeusTextField txtSponsorCode;
    private javax.swing.JLabel lblProposalNum;
    private javax.swing.JLabel lblPrimeSponsor;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JButton btnAwardSearch;
    private edu.mit.coeus.utils.CoeusTextField txtStatus;
    private javax.swing.JButton btnPrimeSponsor;
    private edu.mit.coeus.utils.CoeusTextField txtNarrative;
    private javax.swing.JLabel lblActivityCode;
    private javax.swing.JLabel lblOtherAgency;
    private javax.swing.JLabel lblSponsorPropNum;
    private javax.swing.JLabel lblProposalType;
    // End of variables declaration//GEN-END:variables
    
    /**
    * Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field.
    */
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;
        String strDate = "";
        String oldData = "";
        boolean temporary = false;

        public void focusGained (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))) {
                    
                    oldData = dateField.getText();
                    String focusDate = dtUtils.restoreDate(
                            dateField.getText(),"/-:,");
                    dateField.setText(focusDate);
                    saveRequired = true;
                }
            }
        }

        public void focusLost (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
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
                            CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                    "memMntFrm_exceptionCode.1048"));
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
    
    /*public static void main( String[] arg ){
        javax.swing.JFrame frm = new javax.swing.JFrame("Proposal Development");
        frm.getContentPane().add( new ProposalDetail() );
        frm.pack();
        frm.show(); 
        
    }*/
}
