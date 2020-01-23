/*
 * @(#)ProposaDetailAdminForm.java  1.0  April 07, 2003, 10:06 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 31-MAY-2007
 * by Leena
 */
/* PMD check performed, and commented unused imports and variables on 19-SEPT-2007
 * by Divya 
 */
package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.Color;
import edu.mit.coeus.propdev.bean.*;
// JM 07-20-2011 added packages to customized window
import edu.vanderbilt.coeus.gui.CoeusHelpGidget;
import edu.vanderbilt.coeus.gui.CoeusToolTip;
// END
import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.Toolkit;

/** <CODE>ProposaDetailAdminForm</CODE> is a form object which display
 * the proposal details for the selected proposal and can be used to
 * <CODE>add/modify/display</CODE> the key personl details.
 * This class is instantiated in <CODE>ProposalDetailForm</CODE>.
 *
 * @author  Raghunath P.V.
 * @modified Mukundan.C
 * @version: 1.0 Created on April 07, 2003, 10:06 AM
 */

public class ProposalDetailAdminForm extends JPanel implements ItemListener,
        TypeConstants, ActionListener{
    
    private char functionType;
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    private boolean saveRequired;
    private Vector vecProposalTypes;
    private Vector vecProposalStatus;
    private Vector vecNSFCodes;
    private Vector vecNoticeOfOpportunities;
    private Vector vecActivityCodes;
    private DateUtils dtUtils;
    private Hashtable narrativeDesc;
    private boolean tempararyFlag;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    /* holds the reference of parent object*/
    private CoeusAppletMDIForm mdiForm;
    private final String DISPLAY_TITLE = "Display Sponsor";
    private String sponsorCode;
    private String primeSponsorCode;
    private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    
    private String txtPreviousStartDate, txtPreviousEndDate;
    
    // Added for Case 2162  - adding Award Type - Start 
    private Vector vecAwardTypes;
    // Added for Case 2162  - adding Award Type - End
    
    public static String SPONSOR_CODE;
    public static String SPONSOR_DESCRIPTION;
    //private final ImageIcon NONE_ICON;
    //private final ImageIcon COMPLETE_ICON;
    //private final ImageIcon INCOMPLETE_ICON;
    
    /*For Bug Fix:1666 start step:1 start*/
    private static final char GET_VALID_SPONSOR_CODE = 'P';
    private static final String ROLODEX_SERVLET = "/rolMntServlet";
    private static final String EMPTY_STRING = "";
//    private static final String SERVER_ERROR = "Server Error";
    private static final String COULD_NOT_CONTACT_SERVER = "Could Not Contact Server";
    private ImageIcon ggLogoIcon;
    /*end step:1*/
    private static final String NONE = "NONE";
    private static final String INCOMPLETE = "Incomplete";
    private static final String COMPLETE = "Complete";
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    boolean sponsorChanged = false;
    boolean invalidPrimeSponsor = false;
    boolean isSponsorWarningFired = false;
    boolean primeSponsorChanged = false;
    private static final String INACTIVE_STATUS = "I";
    private static final char GET_CODE_FOR_VALID_SPONSOR = 'p';
    boolean invalidSponsorCode = false;
    boolean invalidPrimeSponsorCode = false;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end

    private static final char CHECK_S2S_SUBMISSION_TYPE = 'q';
    private boolean isS2SSubTyp_3;  
    static final String connect = CoeusGuiConstants.CONNECTION_URL +
    "/ProposalActionServlet";
    /** Creates new form ProposalDetail */
    public ProposalDetailAdminForm() {
        ggLogoIcon = new javax.swing.ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.GRANTS_GOV_LOGO_ICON));
        initComponents();
        //added for coeus enhancement Case#1622.Added by Shiji
        txtLastUpdate.setEditable(true);
        txtLastUpdate.setBackground(Color.lightGray);
        txtLastUpdate.setRequestFocusEnabled(false);    
        //txtLastUpdate.setEditable(false);
    }
    
    /** Creates new form <CODE>ProposaDetailAdminForm</CODE> with the given Protocol
     * details and specified <CODE>functionType</CODE>.
     * @param functionType which specifies the mode in which the form will be
     * opened.
     * @param infoBean <CODE>ProposalDevelopmentFormBean</CODE> with all the details regarding to
     * proposal.
     */
    public ProposalDetailAdminForm(char functionType, ProposalDevelopmentFormBean propDevelopmentFormBean){
        this.functionType = functionType;
        this.proposalDevelopmentFormBean = propDevelopmentFormBean;
        ggLogoIcon = new javax.swing.ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.GRANTS_GOV_LOGO_ICON));
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
    
    // Added by Vyjayanthi
    //Code Start
    public void setProposalDevelopmentFormBean(ProposalDevelopmentFormBean propDevFormBean){
        proposalDevelopmentFormBean.setBudgetStatus(propDevFormBean.getBudgetStatus());
        proposalDevelopmentFormBean.setUpdateUser(propDevFormBean.getUpdateUser());
        proposalDevelopmentFormBean.setUpdateTimestamp(propDevFormBean.getUpdateTimestamp());
        //txtBudget.setText((String)narrativeDesc.get(
        //proposalDevelopmentFormBean.getBudgetStatus()));
        //  txtUpdateUser.setText(proposalDevelopmentFormBean.getUpdateUser());
        if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getBudgetStatus())).equals(COMPLETE)) {
            lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON)));
        }else if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getBudgetStatus())).equals(INCOMPLETE)) {
            lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.INCOMPLETE_ICON)));
        }else if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getBudgetStatus())).equals(NONE)) {
            lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON)));
        }
        //modified for coeus enhancement Case#1622.Added by Shiji
        txtLastUpdate.setText("Last Update: " + CoeusDateFormat.format(
                proposalDevelopmentFormBean.getUpdateTimestamp().toString())+" :"+UserUtils.getDisplayName(proposalDevelopmentFormBean.getUpdateUser()));
        addListenersToComponents();
    }
    //Code End
    
    
    /** This methods used get the description for the narrative
     * in this method hashtable is created to store the description for
     * narrative in PB this have been done in data window
     */
    private void getNarrativeDesc(){
        narrativeDesc = new Hashtable();
        narrativeDesc.put("N",NONE);
        narrativeDesc.put("I",INCOMPLETE);
        narrativeDesc.put("C",COMPLETE);
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
        /** gnprh Commneted for Proposal Hierarchy start
         */
//        lblPropHiearchy.setVisible(false);
//        lblPropHiearchyIcon.setVisible(false);
         /*End
          */
        txtLastUpdate.setEditable(true);
        txtLastUpdate.setBackground(Color.lightGray);
        txtLastUpdate.setRequestFocusEnabled(false);
        //txtLastUpdate.setEditable(false);
        txtSponsorCode.setInputVerifier(new SponsorVerifier());
        txtPrimeSponsor.setInputVerifier(new SponsorVerifier());
        formatFields();
        getNarrativeDesc();
        addListenersToComponents();
        setFormData();
        coeusMessageResources = CoeusMessageResources.getInstance();
        //modified for coeus enhancement Case#1622.Added by Shiji
        java.awt.Component[] comp = {txtArTitle,txtStartDate,txtEndDate,cmbProposalType,txtAwardNo,
        // Modified for Case 2162  - adding Award Type - Start 
        btnAwardSearch,cmbActivityCode,cmbNSFCode,
        cmbAnticipatedAwd,
        txtSponsorCode,btnSponsorSearch,txtPrimeSponsor,btnPrimeSponsor,
        // Modified for Case 2162  - adding Award Type - End
//        btnAwardSearch,cmbActivityCode,cmbNSFCode,txtSponsorCode,btnSponsorSearch,txtPrimeSponsor,btnPrimeSponsor,
        txtSponsorProposalNo,
        //Modified for case #2346 start
        //cmbNoticeOfOpportunity,chkOtherAgency,chkSubcontract,txtProgramNo,txtCfdaNo,
        txtOriginalProposal,btnProposalSearch,txtArProgramTitle,cmbNoticeOfOpportunity,chkSubcontract,txtProgramNo,txtCfdaNo,
        //Modified for case #2346 end
        txtAgencyProgramCode,txtAgencyDivCode,txtAgencyRoutingIdentifier,txtPrevGGTrackingID};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        cmbNSFCode.setShowCode(true);
        /*
         *COEUSQA-3951 commented because, currently there is no need for S2SSubmissionType checking
        if(!checkS2SSubmissionType(proposalDevelopmentFormBean.getProposalNumber())){
           lblPrevGGTrackingID.setVisible(false);
           txtPrevGGTrackingID.setVisible(false); 
        } 
        */
        return this;
    }
    
    /** This method used to add the listener to the components */
    private void addListenersToComponents(){
        if(functionType != 'D'){
            txtEndDate.addFocusListener(new CustomFocusAdapter());
            txtStartDate.addFocusListener(new CustomFocusAdapter());
            
            cmbActivityCode.addItemListener( this );
            cmbNSFCode.addItemListener( this );
            cmbNoticeOfOpportunity.addItemListener( this );
            cmbProposalType.addItemListener( this );
            //cmbProposalStatus.addItemListener(this);
            
            // Added for Case 2162  - adding Award Type - Start 
            cmbAnticipatedAwd.addItemListener(this);
            // Added for Case 2162  - adding Award Type - End
            
            btnAwardSearch.addActionListener(this);
            btnPrimeSponsor.addActionListener(this);
            btnSponsorSearch.addActionListener(this);
            
            //            txtCfdaNo.addFocusListener(new CustomFocusAdapter());
            
            proposalDevelopmentFormBean.addPropertyChangeListener(
                    new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equals(pce.getOldValue().toString().trim())))  {
                            saveRequired = true;
                        }
                    }
                }
            });
        }
    }
    
    /**
     * to get the description for the given activity code param
     */
    private String getActivityDesc(String activityCode){
        String activityDesc="";
        // Added by chandra to check null value int he vector 13-Oct-2004
        if(vecActivityCodes!= null){
            int activityCodesSize = vecActivityCodes.size();
            for(int index = 0 ; index < activityCodesSize ; index++){
                ComboBoxBean bean = (ComboBoxBean)vecActivityCodes.elementAt(index);
                if (bean.getCode().equals(activityCode)){
                    activityDesc = bean.getDescription();
                    break;
                }
            }
        }
        return activityDesc;
    }
    
    /**
     * to get the description for the given NSF code param
     */
    private String getNSFDesc(String nsfCode){
        String nsfDesc="";
        // Added by chandra to check the vector for null values. 13-Oct-2004
        if(vecNSFCodes!= null){
            int nsfCodesSize = vecNSFCodes.size();
            for(int index = 0 ; index < nsfCodesSize ; index++){
                ComboBoxBean bean = (ComboBoxBean)vecNSFCodes.elementAt(index);
                if (bean.getCode().equals(nsfCode)){
                    nsfDesc = bean.getDescription();
                    break;
                }
            }
        }
        return nsfDesc;
    }
    
    /**
     * to get the description for the given notice opportunities code param
     */
    private String getNoticeDesc(String noticeCode){
        String noticeDesc="";
        // Added by chandra to check for the null value in the vector
        if(vecNoticeOfOpportunities!= null){
            int noticeSize = vecNoticeOfOpportunities.size();
            for(int index = 0 ; index < noticeSize ; index++){
                ComboBoxBean bean = (ComboBoxBean)vecNoticeOfOpportunities.elementAt(index);
                if (bean.getCode().equals(noticeCode)){
                    noticeDesc = bean.getDescription();
                    break;
                }
            }
        }
        return noticeDesc;
    }
    
    /**
     * to get the description for the given proposal type code param
     */
//    private String getProposalTypeDesc(String proposalTypeCode){
//        String typeDesc="";
//        // Added by chandra to check for the null value in the vector
//        if(vecProposalTypes!= null){
//            int proposalTypeSize = vecProposalTypes.size();
//            for(int index = 0 ; index < proposalTypeSize ; index++){
//                ComboBoxBean bean = (ComboBoxBean)vecProposalTypes.elementAt(index);
//                if (bean.getCode().equals(proposalTypeCode)){
//                    typeDesc = bean.getDescription();
//                    break;
//                }
//            }
//        }
//        return typeDesc;
//    }
    
    
    /**
     * To set the form after the getting the data from the database
     */
    private void setFormData(){
        //setting the activity type code look up
        if( ( vecActivityCodes != null ) && ( vecActivityCodes.size() > 0 )
        && ( cmbActivityCode.getItemCount() == 0 ) ) {
            int activityCodesSize = vecActivityCodes.size();
            cmbActivityCode.addItem(new ComboBoxBean("",""));
            for(int index = 0 ; index < activityCodesSize ; index++){
                cmbActivityCode.addItem((ComboBoxBean)vecActivityCodes.elementAt(index));
            }
        }
        //setting the NSF code look up
        if( ( vecNSFCodes != null ) && ( vecNSFCodes.size() > 0 )
        && ( cmbNSFCode.getItemCount() == 0 ) ) {
            int nsfCodesSize = vecNSFCodes.size();
            cmbNSFCode.addItem(new ComboBoxBean("",""));
            for(int index = 0 ; index < nsfCodesSize ; index++){
                cmbNSFCode.addItem((ComboBoxBean)vecNSFCodes.elementAt(index));
            }
        }
        // setting the Notice of opportunities look up
        if( ( vecNoticeOfOpportunities != null ) && ( vecNoticeOfOpportunities.size() > 0 )
        && ( cmbNoticeOfOpportunity.getItemCount() == 0 ) ) {
            int noticeSize = vecNoticeOfOpportunities.size();
            cmbNoticeOfOpportunity.addItem(new ComboBoxBean("",""));
            for(int index = 0 ; index < noticeSize ; index++){
                cmbNoticeOfOpportunity.addItem((ComboBoxBean)vecNoticeOfOpportunities.elementAt(index));
            }
        }
        //setting the proposal type look up
        if( ( vecProposalTypes != null ) && ( vecProposalTypes.size() > 0 )
        && ( cmbProposalType.getItemCount() == 0 ) ) {
            int proposalTypeSize = vecProposalTypes.size();
            cmbProposalType.addItem(new ComboBoxBean("",""));
            for(int index = 0 ; index < proposalTypeSize ; index++){
                cmbProposalType.addItem((ComboBoxBean)vecProposalTypes.elementAt(index));
            }
        }
        
        // Added for Case 2162  - adding Award Type - Start 
        // setting data to the cmbAnticipatedAwd combo box
        if( ( vecAwardTypes != null ) && ( vecAwardTypes.size() > 0 )
        && ( cmbAnticipatedAwd.getItemCount() == 0 ) ) {
            int AwardTypeSize = vecAwardTypes.size();
            for(int index = 0 ; index < AwardTypeSize ; index++){
                cmbAnticipatedAwd.addItem((ComboBoxBean)vecAwardTypes.elementAt(index));
            }
        }
        // Added for Case 2162  - adding Award Type - End
        
        //        //setting the proposal status look up
        //        if( ( vecProposalStatus != null ) && ( vecProposalStatus.size() > 0 )
        //        && ( cmbProposalStatus.getItemCount() == 0 ) ) {
        //            int proposalStatusSize = vecProposalStatus.size();
        //            for(int index = 0 ; index < proposalStatusSize ; index++){
        //                cmbProposalStatus.addItem((ComboBoxBean)vecProposalStatus.elementAt(index));
        //            }
        //        }
        
        //setting the data for other fields
        if(proposalDevelopmentFormBean != null){
            ComboBoxBean comboBean = new ComboBoxBean();
            txtProposalNum.setText(proposalDevelopmentFormBean.getProposalNumber());
            
            //setting the data for other fields
            if(proposalDevelopmentFormBean != null){
                // ComboBoxBean comboBean = new ComboBoxBean();
                txtProposalStatus.setText(proposalDevelopmentFormBean.getCreationStatusDescription());
            }
            //            txtStatus.setText((String)statusDescription.get(
            //                                ""+proposalDevelopmentFormBean.getCreationStatusCode()));
            StringBuffer strBffr = new StringBuffer();
            strBffr.append((String)proposalDevelopmentFormBean.getOwnedBy()+":");
            strBffr.append((String)proposalDevelopmentFormBean.getOwnedByDesc());
            
            txtLeadUnit.setText(strBffr.toString());
            if ( proposalDevelopmentFormBean.getRequestStartDateInitial() != null ){
                txtStartDate.setText(dtUtils.formatDate(
                        proposalDevelopmentFormBean.getRequestStartDateInitial().toString(),DATE_FORMAT_DISPLAY));
                txtPreviousStartDate = txtStartDate.getText();
            }
            if ( proposalDevelopmentFormBean.getRequestEndDateInitial() != null ){
                txtEndDate.setText(dtUtils.formatDate(
                        proposalDevelopmentFormBean.getRequestEndDateInitial().toString(),DATE_FORMAT_DISPLAY));
                txtPreviousEndDate = txtEndDate.getText();
            }
            
            txtArTitle.setText(proposalDevelopmentFormBean.getTitle());
            
            txtCfdaNo.setText(proposalDevelopmentFormBean.getCfdaNumber());
            
            // JM 11-21-2012 added COPIED_FROM_PROP_NUM
            txtCopiedFromPropNum.setText(proposalDevelopmentFormBean.getCopiedFromPropNum());
            // JM END
            
            // to get selected value
            comboBean.setCode(new Integer(
                    proposalDevelopmentFormBean.getProposalTypeCode()).toString());
            comboBean.setDescription(proposalDevelopmentFormBean.getProposalTypeDesc());
            //            comboBean.setDescription(getProposalTypeDesc(
            //                ""+proposalDevelopmentFormBean.getProposalTypeCode()));
            for(int typeRow = 0; typeRow < cmbProposalType.getItemCount();typeRow++){
                if(((ComboBoxBean)cmbProposalType.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbProposalType.setSelectedIndex(typeRow);
                }
            }
            
            // Added by chandra
            //            comboBean.setCode(new Integer(
            //                    proposalDevelopmentFormBean.getCreationStatusCode()).toString());
            //            comboBean.setDescription(proposalDevelopmentFormBean.getCreationStatusDescription());
            //            for(int typeRow = 0; typeRow < cmbProposalStatus.getItemCount();typeRow++){
            //                if(((ComboBoxBean)cmbProposalStatus.getItemAt(
            //                typeRow)).toString().equals(comboBean.toString())){
            //                    cmbProposalStatus.setSelectedIndex(typeRow);
            //                }
            //            }
            
            txtAwardNo.setText(proposalDevelopmentFormBean.getCurrentAwardNumber());
            // to get description for the NSF code
            comboBean.setCode(proposalDevelopmentFormBean.getNsfCode());
            comboBean.setDescription(getNSFDesc(
                    proposalDevelopmentFormBean.getNsfCode()));
            for(int typeRow = 0; typeRow < cmbNSFCode.getItemCount();typeRow++){
                if(((ComboBoxBean)cmbNSFCode.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbNSFCode.setSelectedIndex(typeRow);
                }
            }
            chkOtherAgency.setSelected(proposalDevelopmentFormBean.isOtherAgencyFlag());
            chkSubcontract.setSelected(proposalDevelopmentFormBean.isSubcontractFlag());
            
            txtSponsorCode.setText(proposalDevelopmentFormBean.getSponsorCode());
            String sponsorName = null;
            if (proposalDevelopmentFormBean.getSponsorCode() != null){
                // Modified for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - Start
//                sponsorName = getSponsorDetails(proposalDevelopmentFormBean.getSponsorCode());
//                lblSponsorName.setText(sponsorName);
                lblSponsorName.setText(proposalDevelopmentFormBean.getSponsorName());
                // Modified for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - End
            //Added for case#2770 - start    
            }else{
                lblSponsorName.setText("");
            }
            //Added for case#2770 - end
            
            ProposalDetailAdminForm.SPONSOR_CODE = proposalDevelopmentFormBean.getSponsorCode();
            //Commeneted and added for COEUSQA-3390 : sponsor name is null in proposal roles screen - start
            //ProposalDetailAdminForm.SPONSOR_DESCRIPTION = sponsorName;
            ProposalDetailAdminForm.SPONSOR_DESCRIPTION = proposalDevelopmentFormBean.getSponsorName();
            //Commeneted and added for COEUSQA-3390 : sponsor name is null in proposal roles screen - end
            txtPrimeSponsor.setText(proposalDevelopmentFormBean.getPrimeSponsorCode());
            if (proposalDevelopmentFormBean.getPrimeSponsorCode() != null){
                // Modified for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - Start
//                lblPrimeSponsorName.setText(getSponsorDetails(
//                        proposalDevelopmentFormBean.getPrimeSponsorCode()));
                lblPrimeSponsorName.setText(proposalDevelopmentFormBean.getPrimeSponsorName());
                // Modified for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - End

            }
            //Added for case#2770 - start
            else{
                lblPrimeSponsorName.setText("");
            }
            //Added for case#2770 - end
            if (proposalDevelopmentFormBean.getAgencyProgramCode() != null){
                txtAgencyProgramCode.setText(proposalDevelopmentFormBean.getAgencyProgramCode());
            }else {
                txtAgencyProgramCode.setText(null);
            }
            if (proposalDevelopmentFormBean.getAgencyDivCode() != null){
                txtAgencyDivCode.setText(proposalDevelopmentFormBean.getAgencyDivCode());
            }else {
                //Bug Fix id D019 for Coeus enhancement Case#1622
                txtAgencyDivCode.setText(null);
            }
            
   //COEUSQA-3951 
            if(proposalDevelopmentFormBean.getAgencyRoutingIdentifier() != null){
                txtAgencyRoutingIdentifier.setText(proposalDevelopmentFormBean.getAgencyRoutingIdentifier());
            }else{
                txtAgencyRoutingIdentifier.setText(null);
            }
                    
            if(proposalDevelopmentFormBean.getPreviousGGTrackingID() != null){
                txtPrevGGTrackingID.setText(proposalDevelopmentFormBean.getPreviousGGTrackingID());
            }else{
                txtPrevGGTrackingID.setText(null);
            }
   //COEUSQA-3951          
            txtSponsorProposalNo.setText(proposalDevelopmentFormBean.getSponsorProposalNumber());
            
            comboBean.setCode(new Integer(
                    proposalDevelopmentFormBean.getProposalActivityTypeCode()).toString());
            comboBean.setDescription(getActivityDesc(
                    ""+proposalDevelopmentFormBean.getProposalActivityTypeCode()));
            for(int typeRow = 0; typeRow < cmbActivityCode.getItemCount();typeRow++){
                if(((ComboBoxBean)cmbActivityCode.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbActivityCode.setSelectedIndex(typeRow);
                }
            }
            
            // Added for Case 2162  - adding Award Type - Start 
            // Set selected item
            comboBean.setCode(new Integer(
                    proposalDevelopmentFormBean.getAwardTypeCode()).toString());
            if(proposalDevelopmentFormBean.getAwardTypeDesc() == null){
                comboBean.setDescription("");
            }else{
                comboBean.setDescription(proposalDevelopmentFormBean.getAwardTypeDesc());
            }
            boolean found = false;
            for(int typeRow = 0; typeRow < cmbAnticipatedAwd.getItemCount();typeRow++){
                if(((ComboBoxBean)cmbAnticipatedAwd.getItemAt(typeRow)).toString().equals(comboBean.toString())){
                    cmbAnticipatedAwd.setSelectedIndex(typeRow);
                    found=true;
                    break;
                }
            }
            if(!found){
                cmbAnticipatedAwd.setSelectedIndex(0);
            }
            // Added for Case 2162  - adding Award Type - End
            
            txtArProgramTitle.setText(proposalDevelopmentFormBean.getProgramAnnouncementTitle());
            comboBean.setCode(new Integer(
                    proposalDevelopmentFormBean.getNoticeOfOpportunitycode()).toString());
            comboBean.setDescription(getNoticeDesc(
                    ""+proposalDevelopmentFormBean.getNoticeOfOpportunitycode()));
            for(int typeRow = 0; typeRow < cmbNoticeOfOpportunity.getItemCount();
            typeRow++){
                if(((ComboBoxBean)cmbNoticeOfOpportunity.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbNoticeOfOpportunity.setSelectedIndex(typeRow);
                }
            }
            txtProgramNo.setText( proposalDevelopmentFormBean.getProgramAnnouncementNumber());
            
            //Check for null since narrative and budget status does not get
            //updated while saving proposal
            if( proposalDevelopmentFormBean.getNarrativeStatus() != null ){
                //txtNarrative.setText((String)narrativeDesc.get(
                // proposalDevelopmentFormBean.getNarrativeStatus()));
                //modified for coeus enhancement Case#1622.Added by Shiji
                if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getNarrativeStatus())).equals(COMPLETE)) {
                    lblNarrativeIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON)));
                }else if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getNarrativeStatus())).equals(INCOMPLETE)) {
                    lblNarrativeIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.INCOMPLETE_ICON)));
                }else if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getNarrativeStatus())).equals(NONE)) {
                    lblNarrativeIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON)));
                }
            }else{
                //modified for coeus enhancement Case#1622.Added by Shiji
                lblNarrativeIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON)));
                // txtNarrative.setText("None");
            }
            if( proposalDevelopmentFormBean.getBudgetStatus() != null ){
                // txtBudget.setText((String)narrativeDesc.get(
                //proposalDevelopmentFormBean.getBudgetStatus()));
                //modified for coeus enhancement Case#1622.Added by Shiji
                if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getBudgetStatus())).equals(COMPLETE)) {
                    lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON)));
                }else if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getBudgetStatus())).equals(INCOMPLETE)) {
                    lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.INCOMPLETE_ICON)));
                }else if(((String)narrativeDesc.get(proposalDevelopmentFormBean.getBudgetStatus())).equals(NONE)) {
                    lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON)));
                }
            }else{
                //modified for coeus enhancement Case#1622.Added by Shiji
                lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON)));
                //txtBudget.setText("None");
            }
            
            if (proposalDevelopmentFormBean.getUpdateTimestamp() != null){
                try {
                    //modified for coeus enhancement Case#1622.Added by Shiji
                    txtLastUpdate.setText("Last Update: " + CoeusDateFormat.format(
                            proposalDevelopmentFormBean.getUpdateTimestamp().toString())+" :"+UserUtils.getDisplayName(proposalDevelopmentFormBean.getUpdateUser()));
                }catch(Exception e){
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            // txtUpdateUser.setText(proposalDevelopmentFormBean.getUpdateUser());
            
            if (proposalDevelopmentFormBean.getProposalOverrideExists() > 0){
                txtProposalNum.setBackground(new java.awt.Color(255, 0, 0));
            }else{
                txtProposalNum.setEditable(false);
            }
        }
        /*
         *Added by geo on 11/31/2005 for displaying gg logo
         */
//        if(proposalDevelopmentFormBean.isS2sCandidate()) lblGGLogo.setIcon(ggLogoIcon);
//        else lblGGLogo.setIcon(null);
//        this.lblGGLogo.setEnabled(proposalDevelopmentFormBean.isS2sOppSelected());
        if(proposalDevelopmentFormBean.isS2sOppSelected()) lblGGLogo.setIcon(ggLogoIcon);
        else lblGGLogo.setIcon(null);
        if(functionType == 'M'){
            proposalDevelopmentFormBean.setAcType("U");
        }
        
        txtOriginalProposal.setText(proposalDevelopmentFormBean.getContinuedFrom());
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        sponsorChanged = false;
        primeSponsorChanged = false;
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    }
    
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){
        txtAwardNo.setDocument(new LimitedPlainDocument(10));
        txtSponsorCode.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,6));
        txtPrimeSponsor.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,6));
        txtSponsorProposalNo.setDocument(new LimitedPlainDocument(60));
        txtProgramNo.setDocument(new LimitedPlainDocument(50));//increased size from 15 -> 50
        // Case# 3822: Increase the length of the proposal title to 200 characters -Start
//        txtArTitle.setDocument(new LimitedPlainDocument(150));
        txtArTitle.setDocument(new LimitedPlainDocument(200));
        // Case# 3822: Increase the length of the proposal title to 200 characters - End
        txtArProgramTitle.setDocument(new LimitedPlainDocument(255));
        txtAgencyProgramCode.setDocument(new LimitedPlainDocument(50));
        txtAgencyDivCode.setDocument(new LimitedPlainDocument(50));
  //COEUSQA-3951         
        txtAgencyRoutingIdentifier.setDocument(new LimitedPlainDocument(50));
        txtPrevGGTrackingID.setDocument(new LimitedPlainDocument(15));        
  //COEUSQA-3951         
        boolean enabled = functionType != 'D' ? true : false ;
        chkOtherAgency.setEnabled(enabled);
        chkSubcontract.setEnabled(enabled);
        
        btnAwardSearch.setEnabled(enabled);
        btnPrimeSponsor.setEnabled(enabled);
        btnSponsorSearch.setEnabled(enabled);
        
        cmbActivityCode.setEnabled(enabled);
        cmbNSFCode.setEnabled(enabled);
        // Added for Case 2162  - adding Award Type - Start 
        cmbAnticipatedAwd.setEnabled(enabled);
        // Added for Case 2162  - adding Award Type - End 
        cmbNoticeOfOpportunity.setEnabled(enabled);
        cmbProposalType.setEnabled(enabled);
        //cmbProposalStatus.setEnabled(enabled);
        cmbActivityCode.setOpaque(enabled);
        cmbNSFCode.setOpaque(enabled);
        cmbNoticeOfOpportunity.setOpaque(enabled);
        cmbProposalType.setOpaque(enabled);
        //cmbProposalStatus.setOpaque(enabled);
        
        txtArTitle.setCaretPosition(0);
        txtArTitle.setOpaque(enabled);
        txtArTitle.setEditable(enabled);
        //  txtArTitle.setLineWrap(true);
        //  txtArProgramTitle.setLineWrap(true);
        txtArProgramTitle.setCaretPosition(0);
        txtArProgramTitle.setOpaque(enabled);
        txtArProgramTitle.setEditable(enabled);
        
        txtCfdaNo.setEditable(enabled);
        txtAwardNo.setEditable(enabled);
        txtSponsorCode.setEditable(enabled);
        txtPrimeSponsor.setEditable(enabled);
        txtAwardNo.setOpaque(enabled);
        txtSponsorCode.setOpaque(enabled);
        txtEndDate.setEditable(enabled);
        txtStartDate.setEditable(enabled);
        txtSponsorProposalNo.setEditable(enabled);
        txtProgramNo.setEditable(enabled);
        //added for coeus enhancement Case#1622.Added by Shiji
        txtAgencyProgramCode.setEditable(enabled);
        txtAgencyDivCode.setEditable(enabled);
        //txtNarrative.setEditable(enabled);
        //txtBudget.setEditable(enabled);
     //COEUSQA-3951   
        txtAgencyRoutingIdentifier.setEditable(enabled);
        txtPrevGGTrackingID.setEditable(enabled); 
    //COEUSQA-3951   
        txtOriginalProposal.setEditable(enabled);
        btnProposalSearch.setEnabled(enabled);
        
        // JM 11-21-2012 added COPIED_FROM_PROP_NUM
        txtCopiedFromPropNum.setEnabled(false);
        txtCopiedFromPropNum.setOpaque(false);
        // JM END  
    }
    
    /** This method is used to check whether any unsaved modifications are
     * available.
     * @return true if there are any modifications that are not saved,
     * else false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    
    /**
     * This method is used to the focus to the Protocol Review Type comboBox.
     */
    public  void setFocusToType() {
        txtStartDate.requestFocusInWindow();
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {
            if(!( functionType == CoeusGuiConstants.ADD_MODE )){
                txtArTitle.requestFocusInWindow();
            }else{
                txtStartDate.requestFocusInWindow();
            }
        }
    }
    //End Amit
    
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
        //formatFields();
        addListenersToComponents();
    }
    
    /**
     * This method is used to set the available proposal Types.
     * @param propTypes collection of available Proposal Types
     */
    public void setProposalTypes(Vector propTypes){
        this.vecProposalTypes = propTypes;
    }
    
    /**
     * This method is used to set the available proposal status.
     * @param proposal status collection of available Proposal status
     */
    public void setProposalStatus(Vector propStatus){
        this.vecProposalStatus = propStatus;
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
    private void log(String mesg) throws CoeusUIException{
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(0);
        throw coeusUIException;
        
    }
    
    // Added for Case 2162  - adding Award Type - Start 
    public void setAwardTypes(Vector vecAwdTypes){
        this.vecAwardTypes = vecAwdTypes;
    }
    // Added for Case 2162  - adding Award Type - End
    
    
    /** Validate the form data before sending to the database.
     *
     * @return true if all form controls have valid data, else false.
     * @throws Exception with the custom message if any form control doesn't
     * have valid data.
     */
    public boolean validateData() throws Exception{
//        String strDate = "";
//        String endDate = "";
        if((txtArTitle.getText()== null)
        || (txtArTitle.getText().trim().length() == 0)){
            /* Start Date doesn't have any value */
            txtArTitle.requestFocus();
            log(coeusMessageResources.parseMessageKey(
                    "prop_title_not_given_exceptionCode.2511"));
            return false;
        }
        else if((txtStartDate.getText()== null)
        || (txtEndDate.getText().trim().length() == 0) ){
            log(coeusMessageResources.parseMessageKey(
                    "prop_end_date_exceptionCode.2501"));
            return false;
        }else if ("".equals(((ComboBoxBean)cmbProposalType.getSelectedItem()).getCode())){
            log(coeusMessageResources.parseMessageKey(
                    "prop_type_code_exceptionCode.2502"));
            return false;
        }else if ("".equals(((ComboBoxBean)cmbActivityCode.getSelectedItem()).getCode())){
            log(coeusMessageResources.parseMessageKey(
                    "prop_activity_code_exceptionCode.2503"));
            return false;
        } else if(txtSponsorCode.getText().toString().trim().length() > 0 &&
                CoeusGuiConstants.EMPTY_STRING.equals(proposalDevelopmentFormBean.getSponsorCode())){
            invalidSponsorCode = true;
             txtSponsorCode.setText(CoeusGuiConstants.EMPTY_STRING);
             lblSponsorName.setText(CoeusGuiConstants.EMPTY_STRING);
            log("prop_invalid_sponsor_exceptionCode.2509");
            return false;
        // Modified for COEUSQA-3700 : Coeus4.5: Proposal Development Prime Sponsor Error- Start
//        } else if(txtPrimeSponsor.getText().toString().trim().length() > 0){
        } else if(txtPrimeSponsor.getText().toString().trim().length() > 0 &&
                CoeusGuiConstants.EMPTY_STRING.equals(proposalDevelopmentFormBean.getPrimeSponsorCode())){// Modified for COEUSQA-3700 - End
            invalidPrimeSponsorCode = true;
            txtPrimeSponsor.setText(CoeusGuiConstants.EMPTY_STRING);
            lblPrimeSponsorName.setText(CoeusGuiConstants.EMPTY_STRING);
            log("prop_invalid_prime_sponsor_exceptionCode.2500");
            return false;
        }
	
		else if (txtAwardNo.getText().toString().trim().length() != 0) {
            if(!validateAwardNumber(txtAwardNo.getText().toString().trim())){
                log("The award number " +txtAwardNo.getText().toString().trim()+ " does not exist. Please enter a valid award number");
                return false;
            }
        }else if (!validateStartDate(txtStartDate.getText().trim())) {
            return false;
        } else if (!validateEndDate(
                dtUtils.restoreDate(txtEndDate.getText().trim(),"/:-,")) ) {
            return false;
        }
        // Added on 8th March 2005 - End
        
        else if(txtOriginalProposal.getText().trim().equals(txtProposalNum.getText().trim())) {
            log(coeusMessageResources.parseMessageKey("cannot_be_same_prop_num_exceptionCode.2303"));
            return false;
        }else if(!txtOriginalProposal.getText().trim().equals("") && !isValidProposal(txtOriginalProposal.getText().trim())) {
            log(coeusMessageResources.parseMessageKey("protoFndSrcFrm_exceptionCode.1134"));
            return false;
        }
        invalidSponsorCode = false;
        invalidPrimeSponsorCode = false;
        return true;
    }
    
    private boolean isValidProposal(String proposalNumber){
        boolean valid = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject("VALIDATE_INSTPROP_NUMBER");
        requesterBean.setId(proposalNumber);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response != null && response.isSuccessfulResponse()) {
            valid = ((Boolean)response.getDataObject()).booleanValue();
        }
        
        return valid;
    }
    
    
    /**
     * This method will return the boolean on the conditions the sponsor valid or not
     * @param sponsorCode String
     */
    private boolean isSponsorCodeValid(String sponsorCode, boolean isSponsor){
        SponsorMaintenanceFormBean sponsorDetails = getSponsorDetails(sponsorCode);
        if(sponsorDetails == null){
            return false;
        }
        if(isSponsor){
            if(!sponsorChanged && !sponsorCode.equals(proposalDevelopmentFormBean.getSponsorCode())){
                sponsorChanged = true;
            }
            if(sponsorChanged && INACTIVE_STATUS.equals(sponsorDetails.getStatus())) {
                return false;
            }
        }else{
            if(!primeSponsorChanged && !sponsorCode.equals(proposalDevelopmentFormBean.getPrimeSponsorCode())){
                primeSponsorChanged = true;
            }
             if(primeSponsorChanged && INACTIVE_STATUS.equals(sponsorDetails.getStatus())) {
                 return false;
            }
        }
        return true;
    }
    
    /** This method is used to get the <CODE>ProposaDetailAdminForm</CODE> data.
     * @return <CODE>ProposalDevelopmentFormBean</CODE> which consists of all the details given by the
     * user in the form.
     * @throws Exception with custom if fetching of the details from the form
     * fails.
     */
    public void getFormData() throws Exception{
        Date date = null;
        String strDate = "";
        String strDate1 = "";
        if( functionType != 'D' ){
            proposalDevelopmentFormBean.setProposalNumber(txtProposalNum.getText());
            proposalDevelopmentFormBean.setCreationStatusDescription(txtProposalStatus.getText());
            proposalDevelopmentFormBean.setStatusCode(proposalDevelopmentFormBean.getStatusCode());
            proposalDevelopmentFormBean.setOwnedBy(proposalDevelopmentFormBean.getOwnedBy());
            if( txtStartDate.getText().length() > 0 ) {
                
                strDate = txtStartDate.getText().trim();
                strDate1 =  dtUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1==null){
                    date = dtFormat.parse(dtUtils.restoreDate(strDate,DATE_SEPARATERS));
                    proposalDevelopmentFormBean.setRequestStartDateInitial(new java.sql.Date(date.getTime()));
                }else{
                    date = dtFormat.parse(dtUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    proposalDevelopmentFormBean.setRequestStartDateInitial(new java.sql.Date(date.getTime()));
                }
            }
            
            //                proposalDevelopmentFormBean.setRequestStartDateInitial(
            //                new java.sql.Date(dtFormat.parse(
            //                dtUtils.restoreDate(txtStartDate.getText(),
            //                "/-:,")).getTime()));
            
            if( txtEndDate.getText().length() > 0 ) {
                strDate = txtEndDate.getText().trim();
                strDate1 =  dtUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1==null){
                    date = dtFormat.parse(dtUtils.restoreDate(strDate,DATE_SEPARATERS));
                    proposalDevelopmentFormBean.setRequestEndDateInitial(new java.sql.Date(date.getTime()));
                }else{
                    date = dtFormat.parse(dtUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    proposalDevelopmentFormBean.setRequestEndDateInitial(new java.sql.Date(date.getTime()));
                }
                
                //                proposalDevelopmentFormBean.setRequestEndDateInitial(
                //                new java.sql.Date(dtFormat.parse(
                //                dtUtils.restoreDate(txtEndDate.getText(),
                //                "/-:,")).getTime()));
            }
            
            if (txtArTitle.getText().trim().length() == 0 ){
                proposalDevelopmentFormBean.setTitle(null);
            }else{
                proposalDevelopmentFormBean.setTitle(txtArTitle.getText());
            }
            
            //Remove Period(.) before saving.
            String strCfdaNo = txtCfdaNo.getText();
            strCfdaNo = strCfdaNo.substring(0,2) + strCfdaNo.substring(3);
            proposalDevelopmentFormBean.setCfdaNumber(strCfdaNo.trim().length() == 0 ? null
                    : strCfdaNo.trim());
            
            ComboBoxBean typeBean =
                    (ComboBoxBean)cmbProposalType.getSelectedItem();
            if( !"".equals(typeBean.getCode())) {
                proposalDevelopmentFormBean.setProposalTypeCode(Integer.parseInt(
                        typeBean.getCode()));
            }
            
            //            ComboBoxBean statusBeanBean =
            //            (ComboBoxBean)cmbProposalStatus.getSelectedItem();
            //            proposalDevelopmentFormBean.setCreationStatusCode(Integer.parseInt(
            //            statusBeanBean.getCode()));
            
            proposalDevelopmentFormBean.setProposalTypeDesc( typeBean.getDescription() );
            if (txtAwardNo.getText().trim().length() == 0 ){
                proposalDevelopmentFormBean.setCurrentAwardNumber(null);
            }else{
                proposalDevelopmentFormBean.setCurrentAwardNumber(txtAwardNo.getText());
            }
            //proposalDevelopmentFormBean.setCurrentAwardNumber(txtAwardNo.getText());
            ComboBoxBean typeBean1 =
                    (ComboBoxBean)cmbNSFCode.getSelectedItem();
            proposalDevelopmentFormBean.setNsfCode(typeBean1.getCode().trim().length() == 0 ? null
                    : typeBean1.getCode());
            
            proposalDevelopmentFormBean.setOtherAgencyFlag(chkOtherAgency.isSelected());
            proposalDevelopmentFormBean.setSubcontractFlag(chkSubcontract.isSelected());
            /*modified for the Bug Fix:1666 step:2 atart*/
            if(!EMPTY_STRING.equals(txtSponsorCode.getText().trim())){
//                proposalDevelopmentFormBean.setSponsorCode(getValidSponsorCode(txtSponsorCode.getText().trim().length() ==0 ? null
//                        :txtSponsorCode.getText()));
                String sponsorCode = txtSponsorCode.getText().trim();
                SponsorMaintenanceFormBean sponsorDetails =  getSponsorDetails(sponsorCode);
                if(sponsorDetails == null){
                    proposalDevelopmentFormBean.setSponsorCode(CoeusGuiConstants.EMPTY_STRING);
                }else {
                    if(!sponsorChanged && !sponsorCode.equals(proposalDevelopmentFormBean.getSponsorCode())){
                        sponsorChanged = true;
                    }
                    if(sponsorChanged && INACTIVE_STATUS.equals(sponsorDetails.getStatus())){
                        proposalDevelopmentFormBean.setSponsorCode(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        proposalDevelopmentFormBean.setSponsorCode(sponsorCode);
                        proposalDevelopmentFormBean.setSponsorName(sponsorDetails.getName());
                    }
                }
            }else{
                proposalDevelopmentFormBean.setSponsorCode(null);// bug fix #1731
            }
            /*end step:2*/
//            proposalDevelopmentFormBean.setSponsorName(lblSponsorName.getText().trim().length() ==0 ? null
//                    :lblSponsorName.getText());
            
            /*modified for the Bug Fix:1666 step:2 atart*/
            // bug fix #1731 - start
            if(!EMPTY_STRING.equals(txtPrimeSponsor.getText().trim())){
//                proposalDevelopmentFormBean.setPrimeSponsorCode(getValidSponsorCode(txtPrimeSponsor.getText().trim().length() ==0 ? null
//                        : txtPrimeSponsor.getText()));
                String primeSponsorCode = txtPrimeSponsor.getText().trim();
                // Modified for COEUSQA-3700 : Coeus4.5: Proposal Development Prime Sponsor Error- Start
//                SponsorMaintenanceFormBean primeSponsorDetails =  getSponsorDetails(sponsorCode);
                SponsorMaintenanceFormBean primeSponsorDetails =  getSponsorDetails(primeSponsorCode);
                // Modified for COEUSQA-3700 : Coeus4.5: Proposal Development Prime Sponsor Error- End
                if(primeSponsorDetails == null){
                    proposalDevelopmentFormBean.setPrimeSponsorCode(CoeusGuiConstants.EMPTY_STRING);
                }else{
                    if(!primeSponsorChanged &&
                            !primeSponsorCode.equals(proposalDevelopmentFormBean.getPrimeSponsorCode())){
                        primeSponsorChanged = true;
                    }
                    if(primeSponsorChanged && INACTIVE_STATUS.equals(primeSponsorDetails.getStatus())){
                        proposalDevelopmentFormBean.setPrimeSponsorCode(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        proposalDevelopmentFormBean.setPrimeSponsorCode(primeSponsorCode);
                        proposalDevelopmentFormBean.setPrimeSponsorName(primeSponsorDetails.getName());
                    }
                }
            }else{
                proposalDevelopmentFormBean.setPrimeSponsorCode(null);
            }
            /*end step:3*/
            // bug fix #1731 - End
            proposalDevelopmentFormBean.setSponsorProposalNumber(txtSponsorProposalNo.getText().trim().length() ==0 ? null
                    : txtSponsorProposalNo.getText());
//            
            ComboBoxBean typeBean2 =
                    (ComboBoxBean)cmbActivityCode.getSelectedItem();
            if( !"".equals(typeBean2.getCode())) {
                proposalDevelopmentFormBean.setProposalActivityTypeCode(Integer.parseInt(
                        typeBean2.getCode()));
            }
            
            proposalDevelopmentFormBean.setProgramAnnouncementTitle(txtArProgramTitle.getText().trim().length() ==0? null
                    : txtArProgramTitle.getText());
            ComboBoxBean typeBean3 =
                    (ComboBoxBean)cmbNoticeOfOpportunity.getSelectedItem();
            if ( (typeBean3.getCode() == null) || (typeBean3.getCode() == "") ){
                proposalDevelopmentFormBean.setNoticeOfOpportunitycode(0);
            }else{
                proposalDevelopmentFormBean.setNoticeOfOpportunitycode(Integer.parseInt(
                        typeBean3.getCode()));
            }
            
            // Added for Case 2162  - adding Award Type - Start 
            ComboBoxBean typeAwdBean =
                    (ComboBoxBean)cmbAnticipatedAwd.getSelectedItem();
            if ((typeAwdBean.getCode().equals("") || typeAwdBean.getCode()== null)){
                proposalDevelopmentFormBean.setAwardTypeCode(0);
                proposalDevelopmentFormBean.setAwardTypeDesc("");
            }else{
                proposalDevelopmentFormBean.setAwardTypeCode(Integer.parseInt(typeAwdBean.getCode()));
            }
            // Added for Case 2162  - adding Award Type - End 
            
            proposalDevelopmentFormBean.setProgramAnnouncementNumber(
                    txtProgramNo.getText().toString().length()== 0 ? null :txtProgramNo.getText());
            proposalDevelopmentFormBean.setNarrativeStatus(proposalDevelopmentFormBean.getNarrativeStatus());
            proposalDevelopmentFormBean.setBudgetStatus(proposalDevelopmentFormBean.getBudgetStatus());
            proposalDevelopmentFormBean.setUpdateTimestamp(proposalDevelopmentFormBean.getUpdateTimestamp());
            proposalDevelopmentFormBean.setUpdateUser(proposalDevelopmentFormBean.getUpdateUser());
            if(!txtAgencyProgramCode.getText().trim().equals("")){
                proposalDevelopmentFormBean.setAgencyProgramCode(txtAgencyProgramCode.getText());
            }else{
                proposalDevelopmentFormBean.setAgencyProgramCode(null);
            }
            if(!txtAgencyDivCode.getText().trim().equals("")){
                proposalDevelopmentFormBean.setAgencyDivCode(txtAgencyDivCode.getText());
            }else{
                proposalDevelopmentFormBean.setAgencyDivCode(null);
            }
   //COEUSQA-3951   
            if(!txtAgencyRoutingIdentifier.getText().trim().equals("")){
                proposalDevelopmentFormBean.setAgencyRoutingIdentifier(txtAgencyRoutingIdentifier.getText());
            }else{
                proposalDevelopmentFormBean.setAgencyRoutingIdentifier(null);
            }
            if(!txtPrevGGTrackingID.getText().trim().equals("")){
                proposalDevelopmentFormBean.setPreviousGGTrackingID(txtPrevGGTrackingID.getText());
            }else{
                proposalDevelopmentFormBean.setPreviousGGTrackingID(null);
            }
  //COEUSQA-3951    
            
            if(txtOriginalProposal.getText().trim().equals("")) {
                proposalDevelopmentFormBean.setContinuedFrom(null);
            }else {
                proposalDevelopmentFormBean.setContinuedFrom(txtOriginalProposal.getText().trim());
            }
            
        }
    }
    
    /** added for the Bug Fix:1666 for alpha numeric sponsor code  start step:4
     * contacts the server and fetches the valid Sponsor code for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor code
     * @param sponsorCode sponsor code for which valid sponsor code has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */
    private  String getValidSponsorCode(String sponsorCode)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_VALID_SPONSOR_CODE);
        requesterBean.setDataObject(sponsorCode);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            throw new CoeusException(COULD_NOT_CONTACT_SERVER);
        }else if(!responderBean.isSuccessfulResponse()) {
            throw new CoeusException(responderBean.getMessage());
        }
        //Got data from server. return sponsor name.
        //sponsor name = EMPTY if not found.
        if(responderBean.getDataObject() == null){
            return EMPTY_STRING;
        }
        String validSponsorCode = responderBean.getDataObject().toString();
        return validSponsorCode;
    }/*end step:4*/
    
    
    /**
     * Validates the Title Sponsor code Entry
     */
    public boolean validateOtherDetails() throws Exception {
        
        if((txtArTitle.getText()== null)
        || (txtArTitle.getText().trim().length() == 0)){
            /* Title Field doesn't have any value */
            txtArTitle.requestFocus();
            log(coeusMessageResources.parseMessageKey(
                    "proposalSubmitValidation_exceptionCode.1130"));
            
            return false;
        }
        
        if((txtSponsorCode.getText()== null)
        || (txtSponsorCode.getText().trim().length() == 0)){
            txtSponsorCode.requestFocus();
            /* SponsorCode Field doesn't have any value */
            log(coeusMessageResources.parseMessageKey(
                    "proposalSubmitValidation_exceptionCode.1131"));
            return false;
        }
        
        return true;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
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
        chkOtherAgency.setVisible(false);
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
        txtProposalNum = new edu.mit.coeus.utils.CoeusTextField();
        txtStartDate = new edu.mit.coeus.utils.CoeusTextField();
        txtEndDate = new edu.mit.coeus.utils.CoeusTextField();
        txtAwardNo = new edu.mit.coeus.utils.CoeusTextField();
        txtSponsorCode = new edu.mit.coeus.utils.CoeusTextField();
        txtPrimeSponsor = new edu.mit.coeus.utils.CoeusTextField();
        txtSponsorProposalNo = new edu.mit.coeus.utils.CoeusTextField();
        txtProgramNo = new edu.mit.coeus.utils.CoeusTextField();
        txtLeadUnit = new edu.mit.coeus.utils.CoeusTextField();
        txtProposalStatus = new edu.mit.coeus.utils.CoeusTextField();
        lblCfdaNo = new javax.swing.JLabel();
        javax.swing.text.MaskFormatter cfdaFormat = null;
        try{
            cfdaFormat = new javax.swing.text.MaskFormatter("**.****");
        }catch (java.text.ParseException parseException) {
            parseException.printStackTrace();
        }
        txtCfdaNo = new JFormattedTextField(cfdaFormat);
        
        lblAgencyProgramCode = new javax.swing.JLabel();
        lblAgencyDivCode = new javax.swing.JLabel();
        pnlUpdate = new javax.swing.JPanel();
        txtLastUpdate = new edu.mit.coeus.utils.CoeusTextField();
        txtAgencyProgramCode = new edu.mit.coeus.utils.CoeusTextField();
        txtAgencyDivCode = new edu.mit.coeus.utils.CoeusTextField();
        lblGGLogo = new javax.swing.JLabel();
        lblOriginalProposal = new javax.swing.JLabel();
        txtOriginalProposal = new javax.swing.JTextField();
        btnProposalSearch = new javax.swing.JButton();
        pnlIcons = new javax.swing.JPanel();
        lblNarrative = new javax.swing.JLabel();
        lblBudget = new javax.swing.JLabel();
        lblNarrativeIcon = new javax.swing.JLabel();
        lblBudgetIcon = new javax.swing.JLabel();
        lblPropHiearchy = new edu.mit.coeus.utils.CoeusLabel();
        pnlPropHierachyIcons = new javax.swing.JPanel();
        lblPropHiearchyIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblParentIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblAnticipatedAwd = new javax.swing.JLabel();
        // JM 6-23-2011 changed from JComboBox to CoeusComboBox for consistent look and feel
        cmbAnticipatedAwd = new edu.mit.coeus.utils.CoeusComboBox();
        // END
        lblAgencyRoutingIdentifier = new javax.swing.JLabel();
        txtAgencyRoutingIdentifier = new edu.mit.coeus.utils.CoeusTextField();
        lblPrevGGTrackingID = new javax.swing.JLabel();
        txtPrevGGTrackingID = new edu.mit.coeus.utils.CoeusTextField();
        // JM 11-21-2012 added COPIED_FROM_PROP_NUM
        lblCopiedFromPropNum = new javax.swing.JLabel();
        txtCopiedFromPropNum = new edu.mit.coeus.utils.CoeusTextField();
        // JM END  

        setLayout(new java.awt.GridBagLayout());

        lblProposalNum.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalNum.setText("Proposal No:");
// JM 8-29-2011 added custom tool tip
        lblProposalNum.setToolTipText(CoeusToolTip.getToolTip("proposalNumber_toolTip.1000"));
// END        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        add(lblProposalNum, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Status:");
// JM 8-29-2011 added custom tool tip
//        lblStatus.setToolTipText("Status");
        lblStatus.setToolTipText(CoeusToolTip.getToolTip("proposalStatus_toolTip.1000"));
// END   
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 40, 3, 0);
        add(lblStatus, gridBagConstraints);

        lblLeadUnit.setFont(CoeusFontFactory.getLabelFont());
        lblLeadUnit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLeadUnit.setText("Lead Unit:");
// JM 8-29-2011 added custom tool tip
        lblLeadUnit.setToolTipText(CoeusToolTip.getToolTip("leadUnit_toolTip.1000"));
// END       
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblLeadUnit, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setText("Start Date:");
// JM 8-29-2011 added custom tool tip
        lblStartDate.setToolTipText(CoeusToolTip.getToolTip("startDate_toolTip.1000"));
// END       
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblStartDate, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblEndDate.setText("End Date: ");
// JM 8-29-2011 added custom tool tip
        lblEndDate.setToolTipText(CoeusToolTip.getToolTip("endDate_toolTip.1000"));
// END       
        lblEndDate.setMaximumSize(new java.awt.Dimension(62, 15));
        lblEndDate.setMinimumSize(new java.awt.Dimension(62, 15));
        lblEndDate.setPreferredSize(new java.awt.Dimension(62, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        add(lblEndDate, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setText("Title:");
// JM 8-29-2011 added custom tool tip
        lblTitle.setToolTipText(CoeusToolTip.getToolTip("title_toolTip.1000"));
// END       
        lblTitle.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblTitle, gridBagConstraints);

        scrPnTitle.setMaximumSize(new java.awt.Dimension(732, 43));
        scrPnTitle.setMinimumSize(new java.awt.Dimension(732, 43));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(732, 43));

        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(scrPnTitle, gridBagConstraints);

        lblProposalType.setFont(CoeusFontFactory.getLabelFont());
        lblProposalType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalType.setText("Proposal Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProposalType, gridBagConstraints);

        lblAwardNo.setFont(CoeusFontFactory.getLabelFont());
        lblAwardNo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblAwardNo.setText("Award No: ");
// JM 8-29-2011 added custom tool tip
        lblAwardNo.setToolTipText(CoeusToolTip.getToolTip("awardNumber_toolTip.1000"));
// END       
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        add(lblAwardNo, gridBagConstraints);

        btnAwardSearch.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
        btnAwardSearch.setMaximumSize(new java.awt.Dimension(23, 23));
        btnAwardSearch.setMinimumSize(new java.awt.Dimension(23, 23));
        btnAwardSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        btnAwardSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchAwardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        add(btnAwardSearch, gridBagConstraints);

        lblNSFCode.setFont(CoeusFontFactory.getLabelFont());
        lblNSFCode.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblNSFCode.setText("NSF Code: ");
// JM 8-29-2011 added custom tool tip
        lblNSFCode.setToolTipText(CoeusToolTip.getToolTip("nsfCode_toolTip.1000"));
// END   
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        add(lblNSFCode, gridBagConstraints);

        chkOtherAgency.setFont(CoeusFontFactory.getLabelFont());
        chkOtherAgency.setMnemonic('O');
        chkOtherAgency.setText("Other Agency");
        chkOtherAgency.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkOtherAgency.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkOtherAgency.setMaximumSize(new java.awt.Dimension(105, 17));
        chkOtherAgency.setMinimumSize(new java.awt.Dimension(105, 20));
        chkOtherAgency.setPreferredSize(new java.awt.Dimension(105, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(chkOtherAgency, gridBagConstraints);

        chkSubcontract.setFont(CoeusFontFactory.getLabelFont());
        chkSubcontract.setMnemonic('S');
        chkSubcontract.setText("Incoming Subcontract"); // JM 11-22-2013 updated label
        chkSubcontract.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkSubcontract.setMaximumSize(new java.awt.Dimension(150, 20)); // JM 11-22-2013 was 95,20
        chkSubcontract.setMinimumSize(new java.awt.Dimension(150, 20)); // JM 11-22-2013 was 95,20
        chkSubcontract.setPreferredSize(new java.awt.Dimension(150, 20)); // JM 11-22-2013 was 95,20
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, 0);
        add(chkSubcontract, gridBagConstraints);

        cmbProposalType.setMaximumSize(new java.awt.Dimension(175, 20));
        cmbProposalType.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbProposalType.setPreferredSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(cmbProposalType, gridBagConstraints);

        cmbNSFCode.setMaximumSize(new java.awt.Dimension(185, 20));
        cmbNSFCode.setMinimumSize(new java.awt.Dimension(185, 20));
        cmbNSFCode.setPreferredSize(new java.awt.Dimension(185, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(cmbNSFCode, gridBagConstraints);

        lblSponsorCode.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorCode.setText("Sponsor:");
// JM 8-29-2011 added custom tool tip
        lblSponsorCode.setToolTipText(CoeusToolTip.getToolTip("sponsor_toolTip.1000"));
// END     
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        add(lblSponsorCode, gridBagConstraints);

        btnSponsorSearch.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
        btnSponsorSearch.setMaximumSize(new java.awt.Dimension(23, 23));
        btnSponsorSearch.setMinimumSize(new java.awt.Dimension(23, 23));
        btnSponsorSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        btnSponsorSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSponsorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, 5);
        add(btnSponsorSearch, gridBagConstraints);

        lblSponsorName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 2);
        add(lblSponsorName, gridBagConstraints);

        lblPrimeSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblPrimeSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrimeSponsor.setText("Prime Sponsor:");
// JM 8-29-2011 added custom tool tip
        lblPrimeSponsor.setToolTipText(CoeusToolTip.getToolTip("primeSponsor_toolTip.1000"));
// END  
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblPrimeSponsor, gridBagConstraints);

        btnPrimeSponsor.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON))
        );
        btnPrimeSponsor.setMaximumSize(new java.awt.Dimension(23, 23));
        btnPrimeSponsor.setMinimumSize(new java.awt.Dimension(23, 23));
        btnPrimeSponsor.setPreferredSize(new java.awt.Dimension(23, 23));
        btnPrimeSponsor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPrimeSponsorActPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 5);
        add(btnPrimeSponsor, gridBagConstraints);

        // JM 11-21-2012 added COPIED_FROM_PROP_NUM - hidden for now
        lblCopiedFromPropNum.setText("Copied from Prop Num:");
        lblCopiedFromPropNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCopiedFromPropNum.setFont(CoeusFontFactory.getLabelFont()
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        //add(lblCopiedFromPropNum, gridBagConstraints); // JM 5-16-2016 uncommented
        
        txtCopiedFromPropNum.setMaximumSize(new java.awt.Dimension(185, 23));
        txtCopiedFromPropNum.setMinimumSize(new java.awt.Dimension(185, 23));
        txtCopiedFromPropNum.setPreferredSize(new java.awt.Dimension(185, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        //add(txtCopiedFromPropNum, gridBagConstraints); // JM 5-16-2016 uncommented
        // JM END

        lblPrimeSponsorName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 2);
        add(lblPrimeSponsorName, gridBagConstraints);

        lblSponsorPropNum.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorPropNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorPropNum.setText("Sponsor Proposal No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        
        // JM 9-17-2013 added sponsor proposal number; reorder some commands for layout       
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CoeusHelpGidget sponsorProposalNumberGidget = new CoeusHelpGidget(lblSponsorPropNum,"sponsorProposalNumber_helpCode.1000");
        JButton sponsorProposalNumberHelp = sponsorProposalNumberGidget.getGidget();
        Integer sponsorProposalNumberGidgetOffset = sponsorProposalNumberGidget.getOffset();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, sponsorProposalNumberGidgetOffset);
        add(sponsorProposalNumberHelp, gridBagConstraints);        
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        // JM END
        //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblSponsorPropNum, gridBagConstraints);

        lblActivityCode.setFont(CoeusFontFactory.getLabelFont());
        lblActivityCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblActivityCode.setText("Activity Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        
        // JM 8-10-2011 added activity type gidget; reorder some commands for layout       
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CoeusHelpGidget activityTypeGidget = new CoeusHelpGidget(lblActivityCode,"activityType_helpCode.1000");
        JButton activityTypeHelp = activityTypeGidget.getGidget();
        Integer activityTypeGidgetOffset = activityTypeGidget.getOffset();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, activityTypeGidgetOffset);
        add(activityTypeHelp, gridBagConstraints);        
        //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        // JM END
        add(lblActivityCode, gridBagConstraints);

        cmbActivityCode.setMaximumSize(new java.awt.Dimension(175, 20));
        cmbActivityCode.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbActivityCode.setPreferredSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(cmbActivityCode, gridBagConstraints);

        lblProgramTitle.setFont(CoeusFontFactory.getLabelFont());
        lblProgramTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProgramTitle.setText("Program Title:");
// JM 8-29-2011 added custom tool tip
        lblProgramTitle.setToolTipText(CoeusToolTip.getToolTip("programTitle_toolTip.1000"));
// END  
        lblProgramTitle.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProgramTitle, gridBagConstraints);

        scrPnProgramTitle.setMaximumSize(new java.awt.Dimension(732, 43));
        scrPnProgramTitle.setMinimumSize(new java.awt.Dimension(732, 43));
        scrPnProgramTitle.setPreferredSize(new java.awt.Dimension(732, 43));

        txtArProgramTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArProgramTitle.setLineWrap(true);
        scrPnProgramTitle.setViewportView(txtArProgramTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(scrPnProgramTitle, gridBagConstraints);

        lblNoticeOfOpportunity.setFont(CoeusFontFactory.getLabelFont());
        lblNoticeOfOpportunity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoticeOfOpportunity.setText("Notice Of Opportunity:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblNoticeOfOpportunity, gridBagConstraints);

        cmbNoticeOfOpportunity.setMaximumSize(new java.awt.Dimension(175, 20));
        cmbNoticeOfOpportunity.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbNoticeOfOpportunity.setPreferredSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(cmbNoticeOfOpportunity, gridBagConstraints);

        lblProgramNum.setFont(CoeusFontFactory.getLabelFont());
        lblProgramNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProgramNum.setText("Funding Opportunity Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProgramNum, gridBagConstraints);

        txtProposalNum.setBackground(new java.awt.Color(204, 204, 204));
        txtProposalNum.setDocument(new LimitedPlainDocument(8));
        txtProposalNum.setEnabled(false);
        txtProposalNum.setMaximumSize(new java.awt.Dimension(175, 23));
        txtProposalNum.setMinimumSize(new java.awt.Dimension(175, 23));
        txtProposalNum.setPreferredSize(new java.awt.Dimension(175, 23));
        txtProposalNum.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 0);
        add(txtProposalNum, gridBagConstraints);

        txtStartDate.setDocument(new LimitedPlainDocument(11));
        txtStartDate.setMaximumSize(new java.awt.Dimension(175, 23));
        txtStartDate.setMinimumSize(new java.awt.Dimension(175, 23));
        txtStartDate.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtStartDate, gridBagConstraints);

        txtEndDate.setDocument(new LimitedPlainDocument(11));
        txtEndDate.setMaximumSize(new java.awt.Dimension(185, 23));
        txtEndDate.setMinimumSize(new java.awt.Dimension(185, 23));
        txtEndDate.setPreferredSize(new java.awt.Dimension(185, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtEndDate, gridBagConstraints);

        txtAwardNo.setDocument(new LimitedPlainDocument(10));
        txtAwardNo.setMaximumSize(new java.awt.Dimension(185, 23));
        txtAwardNo.setMinimumSize(new java.awt.Dimension(185, 23));
        txtAwardNo.setPreferredSize(new java.awt.Dimension(185, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtAwardNo, gridBagConstraints);

        txtSponsorCode.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSponsorCode.setMinimumSize(new java.awt.Dimension(175, 23));
        txtSponsorCode.setPreferredSize(new java.awt.Dimension(175, 23));
        txtSponsorCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sponsor_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sponsor_FocusLost(evt);
            }
        });
        txtSponsorCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sponsor_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        add(txtSponsorCode, gridBagConstraints);

        txtPrimeSponsor.setMaximumSize(new java.awt.Dimension(175, 23));
        txtPrimeSponsor.setMinimumSize(new java.awt.Dimension(175, 23));
        txtPrimeSponsor.setPreferredSize(new java.awt.Dimension(175, 23));
        txtPrimeSponsor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                primeSponsor_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                primeSponsor_FocusLost(evt);
            }
        });
        txtPrimeSponsor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sponsor_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtPrimeSponsor, gridBagConstraints);

        txtSponsorProposalNo.setDocument(new LimitedPlainDocument(70));
        txtSponsorProposalNo.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSponsorProposalNo.setMinimumSize(new java.awt.Dimension(175, 23));
        txtSponsorProposalNo.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtSponsorProposalNo, gridBagConstraints);

        txtProgramNo.setDocument(new LimitedPlainDocument(50));
        txtProgramNo.setMaximumSize(new java.awt.Dimension(175, 23));
        txtProgramNo.setMinimumSize(new java.awt.Dimension(175, 23));
        txtProgramNo.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtProgramNo, gridBagConstraints);

        txtLeadUnit.setEditable(false);
        txtLeadUnit.setMaximumSize(new java.awt.Dimension(732, 23));
        txtLeadUnit.setMinimumSize(new java.awt.Dimension(732, 23));
        txtLeadUnit.setPreferredSize(new java.awt.Dimension(732, 23));
        txtLeadUnit.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtLeadUnit, gridBagConstraints);

        txtProposalStatus.setEditable(false);
        txtProposalStatus.setMinimumSize(new java.awt.Dimension(175, 23));
        txtProposalStatus.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(txtProposalStatus, gridBagConstraints);

        lblCfdaNo.setFont(CoeusFontFactory.getLabelFont());
        lblCfdaNo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCfdaNo.setText("CFDA Number: "); // JM space after label for prettiness
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        add(lblCfdaNo, gridBagConstraints);

        txtCfdaNo.setMaximumSize(new java.awt.Dimension(185, 23));
        txtCfdaNo.setMinimumSize(new java.awt.Dimension(185, 23));
        txtCfdaNo.setPreferredSize(new java.awt.Dimension(185, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtCfdaNo, gridBagConstraints);

        lblAgencyProgramCode.setFont(CoeusFontFactory.getLabelFont());
        lblAgencyProgramCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAgencyProgramCode.setText("Agency Program Code: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 4;
        // JM 1-17-2013 added agency program code gidget; reorder some commands for layout       
        //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CoeusHelpGidget agencyProgramCodeGidget = new CoeusHelpGidget(lblAgencyProgramCode,"agencyProgramCode_helpCode.1000");
        JButton agencyProgramCodeHelp = agencyProgramCodeGidget.getGidget();
        Integer agencyProgramCodeOffset = agencyProgramCodeGidget.getOffset();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, agencyProgramCodeOffset);
        add(agencyProgramCodeHelp, gridBagConstraints);        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        // JM END
        add(lblAgencyProgramCode, gridBagConstraints);

        lblAgencyDivCode.setFont(CoeusFontFactory.getLabelFont());
        lblAgencyDivCode.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING); // JM
        lblAgencyDivCode.setText("Agency Div Code: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 13;
        // JM 1-16-2013 added agency div code gidget       
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CoeusHelpGidget agencyDivCodeGidget = new CoeusHelpGidget(lblAgencyDivCode,"agencyDivCode_helpCode.1000");
        JButton agencyDivCodeHelp = agencyDivCodeGidget.getGidget();
        Integer agencyDivCodeGidgetOffset = agencyDivCodeGidget.getOffset();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, agencyDivCodeGidgetOffset);
        add(agencyDivCodeHelp, gridBagConstraints);        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        // JM END  
        add(lblAgencyDivCode, gridBagConstraints);

        pnlUpdate.setMaximumSize(new java.awt.Dimension(980, 150));
        pnlUpdate.setMinimumSize(new java.awt.Dimension(890, 150));
        pnlUpdate.setPreferredSize(new java.awt.Dimension(980, 110));
        pnlUpdate.setLayout(new java.awt.GridBagLayout());

        txtLastUpdate.setBackground(new java.awt.Color(204, 204, 204));
        txtLastUpdate.setEnabled(false);
        txtLastUpdate.setMaximumSize(new java.awt.Dimension(732, 23));
        txtLastUpdate.setMinimumSize(new java.awt.Dimension(890, 23));
        txtLastUpdate.setPreferredSize(new java.awt.Dimension(980, 23));
        txtLastUpdate.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        pnlUpdate.add(txtLastUpdate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 0.8;
        add(pnlUpdate, gridBagConstraints);

        txtAgencyProgramCode.setMaximumSize(new java.awt.Dimension(175, 23));
        txtAgencyProgramCode.setMinimumSize(new java.awt.Dimension(175, 23));
        txtAgencyProgramCode.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        add(txtAgencyProgramCode, gridBagConstraints);

        txtAgencyDivCode.setMaximumSize(new java.awt.Dimension(185, 23));
        txtAgencyDivCode.setMinimumSize(new java.awt.Dimension(185, 23));
        txtAgencyDivCode.setPreferredSize(new java.awt.Dimension(185, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtAgencyDivCode, gridBagConstraints);

        lblGGLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GrantsGovLogo.gif"))); // NOI18N
        lblGGLogo.setMaximumSize(new java.awt.Dimension(145, 30));
        lblGGLogo.setMinimumSize(new java.awt.Dimension(145, 30));
        lblGGLogo.setPreferredSize(new java.awt.Dimension(145, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(lblGGLogo, gridBagConstraints);

        lblOriginalProposal.setFont(CoeusFontFactory.getLabelFont());
        lblOriginalProposal.setText("Original Proposal: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblOriginalProposal, gridBagConstraints);

        // JM 11-21-2012 added for formatting
        txtAwardNo.setMaximumSize(new java.awt.Dimension(185, 23));
        txtAwardNo.setMinimumSize(new java.awt.Dimension(185, 23));
        txtAwardNo.setPreferredSize(new java.awt.Dimension(185, 23));
        // JM END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtOriginalProposal, gridBagConstraints);

        btnProposalSearch.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
        btnProposalSearch.setMaximumSize(new java.awt.Dimension(23, 23));
        btnProposalSearch.setMinimumSize(new java.awt.Dimension(23, 23));
        btnProposalSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        btnProposalSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProposalSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        add(btnProposalSearch, gridBagConstraints);

        pnlIcons.setLayout(new java.awt.GridBagLayout());

        lblNarrative.setFont(CoeusFontFactory.getLabelFont());
        lblNarrative.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNarrative.setText("Narrative:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        pnlIcons.add(lblNarrative, gridBagConstraints);

        lblBudget.setFont(CoeusFontFactory.getLabelFont());
        lblBudget.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBudget.setText("Budget:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlIcons.add(lblBudget, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlIcons.add(lblNarrativeIcon, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlIcons.add(lblBudgetIcon, gridBagConstraints);

        lblPropHiearchy.setText("Proposal Hierarchy:  ");
        lblPropHiearchy.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlIcons.add(lblPropHiearchy, gridBagConstraints);

        pnlPropHierachyIcons.setLayout(new java.awt.GridBagLayout());
        pnlPropHierachyIcons.add(lblPropHiearchyIcon, new java.awt.GridBagConstraints());

        lblParentIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblParentIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblParentIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlPropHierachyIcons.add(lblParentIcon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlIcons.add(pnlPropHierachyIcons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(pnlIcons, gridBagConstraints);

        lblAnticipatedAwd.setFont(CoeusFontFactory.getLabelFont());
        lblAnticipatedAwd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAnticipatedAwd.setText("Anticipated Award Type: ");
// JM 8-29-2011 added custom tool tip
        lblAnticipatedAwd.setToolTipText(CoeusToolTip.getToolTip("anticipatedAwardType_toolTip.1000"));
// END  
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        // JM 8-10-2011 added anticipated award type gidget; reorder some commands for layout       
        //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CoeusHelpGidget anticipatedAwardTypeGidget = new CoeusHelpGidget(lblAnticipatedAwd,"anticipatedAwardType_helpCode.1000");
        JButton anticipatedAwardTypeHelp = anticipatedAwardTypeGidget.getGidget();
        Integer anticipatedAwardTypeGidgetOffset = anticipatedAwardTypeGidget.getOffset();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, anticipatedAwardTypeGidgetOffset);
        add(anticipatedAwardTypeHelp, gridBagConstraints);        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        // JM END
        add(lblAnticipatedAwd, gridBagConstraints);

        cmbAnticipatedAwd.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbAnticipatedAwd.setPreferredSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(cmbAnticipatedAwd, gridBagConstraints);

        lblAgencyRoutingIdentifier.setFont(CoeusFontFactory.getLabelFont());
        lblAgencyRoutingIdentifier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAgencyRoutingIdentifier.setText("Agency Routing Identifier: "); // JM 8-23-2013 space added
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 4;
        // JM 8-23-2013 added agency routing identifier gidget; place anchor at top for layout; added insets          
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CoeusHelpGidget agencyRoutingIdentifierGidget = new CoeusHelpGidget(lblAgencyRoutingIdentifier,"agencyRoutingIdentifier_helpCode.1000");
        JButton agencyRoutingIdentifierCodeHelp = agencyRoutingIdentifierGidget.getGidget();
        Integer agencyRoutingIdentifierOffset = agencyRoutingIdentifierGidget.getOffset();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, agencyRoutingIdentifierOffset);
        add(agencyRoutingIdentifierCodeHelp, gridBagConstraints);     
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        // JM END
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(lblAgencyRoutingIdentifier, gridBagConstraints);

        txtAgencyRoutingIdentifier.setMaximumSize(new java.awt.Dimension(175, 23));
        txtAgencyRoutingIdentifier.setMinimumSize(new java.awt.Dimension(175, 23));
        txtAgencyRoutingIdentifier.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 14;
        add(txtAgencyRoutingIdentifier, gridBagConstraints);

        lblPrevGGTrackingID.setFont(CoeusFontFactory.getLabelFont());
        lblPrevGGTrackingID.setText("Previous Grants.gov Tracking ID: "); // JM 8-23-2013 space added
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 14;
        // JM 8-23-2013 added previous GG tracking ID gidget; place anchor at top for layout          
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CoeusHelpGidget prevGGTrackingIdGidget = new CoeusHelpGidget(lblPrevGGTrackingID,"previousGGTrackingId_helpCode.1000");
        JButton prevGGTrackingIdHelp = prevGGTrackingIdGidget.getGidget();
        Integer prevGGTrackingIdOffset = prevGGTrackingIdGidget.getOffset();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, prevGGTrackingIdOffset);
        add(prevGGTrackingIdHelp, gridBagConstraints);     
        // JM END
        //gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 0);
        add(lblPrevGGTrackingID, gridBagConstraints);

        txtPrevGGTrackingID.setMaximumSize(new java.awt.Dimension(185, 23));
        txtPrevGGTrackingID.setMinimumSize(new java.awt.Dimension(185, 23));
        txtPrevGGTrackingID.setPreferredSize(new java.awt.Dimension(185, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(txtPrevGGTrackingID, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnProposalSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProposalSearchActionPerformed
        try {
            CoeusSearch coeusSearch = new CoeusSearch(
                    CoeusGuiConstants.getMDIForm(), "PROPOSALSEARCH", CoeusSearch.TWO_TABS);
            coeusSearch.showSearchWindow();
            HashMap awardSelected = coeusSearch.getSelectedRow();
            if (awardSelected != null && !awardSelected.isEmpty() ) {
                String proposalNumber=Utils.convertNull(awardSelected.get("PROPOSAL_NUMBER"));
                
                if(proposalNumber.trim().equals(txtProposalNum.getText().trim())) {
                    log(coeusMessageResources.parseMessageKey("cannot_be_same_prop_num_exceptionCode.2303"));
                }else {
                    txtOriginalProposal.setText(proposalNumber);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }//GEN-LAST:event_btnProposalSearchActionPerformed
    
    /**
     * This method used for mouse click on sponsor and prime sponsor code textfiled
     * on double click for valid sponsor code it will pop up the sposor screen
     * on display mode
     */
    private void sponsor_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sponsor_MouseClicked
        // Add your handling code here:
        Object obj = evt.getSource();
        if ((evt.getSource() == txtSponsorCode) || (evt.getSource() == txtPrimeSponsor) ) {
            String value = ((JTextField)obj).getText().trim();
            if( ( value != null) && (value.length() > 0 ) ){
                if (evt.getClickCount() == 2){
                    String  spCode = value;
                    SponsorMaintenanceFormBean sponsorDetails = getSponsorDetails(spCode);
                    boolean openSponsorDetails = true;
                    if(evt.getSource() == txtSponsorCode){
                        if (sponsorDetails == null || (sponsorChanged && INACTIVE_STATUS.equals(sponsorDetails.getStatus()))) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                    "prop_invalid_sponsor_exceptionCode.2509"));
                        }else{
                            openSponsorDetails = true;
                        }
                    }else if(evt.getSource() == txtSponsorCode){
                        if (sponsorDetails == null || (primeSponsorChanged && INACTIVE_STATUS.equals(sponsorDetails.getStatus()))) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                    "prop_invalid_prime_sponsor_exceptionCode.2500"));
                        }else{
                            openSponsorDetails = true;
                        }
                    }
                    
                    if(openSponsorDetails){
                        SponsorMaintenanceForm frmSponsor = new SponsorMaintenanceForm('D',spCode);
                        frmSponsor.showForm(mdiForm,DISPLAY_TITLE,true);
                    }
                }
            }
        }
    }//GEN-LAST:event_sponsor_MouseClicked
    
    /**
     *This method used for focus lost of prime sponsor textfield it will validate
     *the sponsor on focus lost ,if the sponsor is vlaid it will get the
     *sponsor name for the code
     */
    private void primeSponsor_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_primeSponsor_FocusLost
        // Add your handling code here:
//         if (!evt.isTemporary()) {
//            String primeSPCode = txtPrimeSponsor.getText().trim();
//            if (!primeSPCode.equals("")) {
//                String primeSPDesc = validateSponsorCode(
//                                             txtPrimeSponsor.getText().trim());
//                if (primeSPDesc != null ) {
//                    lblPrimeSponsorName.setText(primeSPDesc);
//                } else {
//                    txtPrimeSponsor.setText(primeSponsorCode);
//                }
//            } else {
//                lblPrimeSponsorName.setText("");
//            }
//       }
    }//GEN-LAST:event_primeSponsor_FocusLost
    
    private void primeSponsor_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_primeSponsor_FocusGained
        // Add your handling code here:
        primeSponsorCode = txtPrimeSponsor.getText().trim();
    }//GEN-LAST:event_primeSponsor_FocusGained
    
    /**
     *This method used for focus lost of sponsor textfield it will validate the sponsor
     *on focus lost ,if the sponsor is vlaid it will get the sponsor name for the code
     */
    private void sponsor_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sponsor_FocusLost
        // Add your handling code here:
//        if (!evt.isTemporary()) {
//            String spCode = txtSponsorCode.getText().trim();
//            if (!spCode.equals("")) {
//                String spDesc = validateSponsorCode(
//                                             txtSponsorCode.getText().trim());
//                if (spDesc != null ) {
//                    lblSponsorName.setText(spDesc);
//                    ProposalDetailAdminForm.SPONSOR_CODE = spCode;
//                    ProposalDetailAdminForm.SPONSOR_DESCRIPTION = spDesc;
//                } else {
//                    txtSponsorCode.setText(sponsorCode);
//                }
//            } else {
//                lblSponsorName.setText("");
//            }
//       }
    }//GEN-LAST:event_sponsor_FocusLost
    
    private void sponsor_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sponsor_FocusGained
        // Add your handling code here:
        sponsorCode = txtSponsorCode.getText().trim();
    }//GEN-LAST:event_sponsor_FocusGained
    
    /**
     *This method will pop up the sponsor search screen on click of the
     * prime sponsor search button,it will load the textfield with sponsor code
     */
    private void searchPrimeSponsorActPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPrimeSponsorActPerformed
        // Add your handling code here:
        try {
            CoeusSearch coeusSearch = new CoeusSearch(
                    CoeusGuiConstants.getMDIForm(), "SPONSORSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap sponsorSelected = coeusSearch.getSelectedRow();
            if (sponsorSelected != null && !sponsorSelected.isEmpty() ) {
                
                String sponsorCode = Utils.convertNull(sponsorSelected.get(
                        "SPONSOR_CODE"));
                String sponsorName = Utils.convertNull(sponsorSelected.get(
                        "SPONSOR_NAME"));
                txtPrimeSponsor.setText(sponsorCode);
                lblPrimeSponsorName.setText(sponsorName);
                //saveRequired = true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            CoeusOptionPane.showErrorDialog("Coeus Search is not available.." + e.getMessage());
        }
    }//GEN-LAST:event_searchPrimeSponsorActPerformed
    
    /**
     * This method will pop up the sponsor search screen on click of the
     * sponsor search button,it will load the textfield with sponsor code
     */
    private void searchSponsorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSponsorActionPerformed
        // Add your handling code here:
        try {
            CoeusSearch coeusSearch = new CoeusSearch(
                    CoeusGuiConstants.getMDIForm(), "SPONSORSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap sponsorSelected = coeusSearch.getSelectedRow();
            if (sponsorSelected != null && !sponsorSelected.isEmpty() ) {
                
                String sponsorCode = Utils.convertNull(sponsorSelected.get(
                        "SPONSOR_CODE"));
                String sponsorName = Utils.convertNull(sponsorSelected.get(
                        "SPONSOR_NAME"));
                txtSponsorCode.setText(sponsorCode);
                lblSponsorName.setText(sponsorName);
                ProposalDetailAdminForm.SPONSOR_CODE = sponsorCode;
                ProposalDetailAdminForm.SPONSOR_DESCRIPTION = sponsorName;
                //saveRequired = true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            CoeusOptionPane.showErrorDialog("Coeus Search is not available.." + e.getMessage());
        }
    }//GEN-LAST:event_searchSponsorActionPerformed
    
    /**
     * This method will pop up the award search screen on click of the
     * award search button,it will load the textfield with award number
     */
    private void searchAwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchAwardActionPerformed
        // Add your handling code here:
        try {
            CoeusSearch coeusSearch = new CoeusSearch(
                    CoeusGuiConstants.getMDIForm(), "AWARDSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap awardSelected = coeusSearch.getSelectedRow();
            if (awardSelected != null && !awardSelected.isEmpty() ) {
                
                String awardNumber = Utils.convertNull(awardSelected.get(
                        "MIT_AWARD_NUMBER"));
                txtAwardNo.setText(awardNumber);
                //saveRequired = true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            CoeusOptionPane.showErrorDialog("Award Search is not available.." + e.getMessage());
        }
        
    }//GEN-LAST:event_searchAwardActionPerformed
    
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
    }
    
    /**
     *This method will get the sponsor name for the sponsor passed as the param
     * if the sponsor code is wrong it will throw the message as
     * Invalid sponsor code.
     */
    private SponsorMaintenanceFormBean getSponsorDetails(String sponsorCode){
        
        SponsorMaintenanceFormBean sponsorInfo = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_SPONSORINFO");
        request.setId(sponsorCode);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            sponsorInfo = (SponsorMaintenanceFormBean) response.getDataObject();
            if(sponsorInfo.getSponsorCode() == null){
                sponsorInfo = null;
            }
//            if(sponsorInfo!=null && sponsorInfo.getName() !=null){
//                sponsorDesc = sponsorInfo.getName();
//            }
//            if (sponsorDesc == null ) {
//                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
//                                    "prop_invalid_sponsor_exceptionCode.2509"));
//                //CoeusOptionPane.showErrorDialog(
//                //"Invalid Sponsor Code.Please enter a valid sponsor code");
//            }
        }
        return sponsorInfo;
    }
    
    /**
     * This method will get the count 1 if the award number passed as the param
     * is the valid else it will -1 on invalid ,this will return boolean on this
     * conditions
     */
    private boolean validateAwardNumber(String awardNumber){
        
        int valid = -1;
        boolean isValid = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("VALIDATE_AWARDNUMBER");
        request.setId(awardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            valid = Integer.parseInt(response.getId());
            if (valid == -1 ) {
                isValid = false;
            }else{
                isValid =  true;
            }
        }
        return isValid;
    }
    
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateStartDate(String strDate) {
        
        boolean isValid=true;
        String mesg = null;
        String convertedDate = dtUtils.formatDate(strDate, "/-:," ,DATE_FORMAT_DISPLAY);
        if (convertedDate==null){
            //Modified by Geo on 23-Jun-05
            //BEGIN
            //Send DATE_SEPARATERS instead of format string
//            convertedDate = dtUtils.restoreDate(strDate, "dd-MMM-yyyy");
            convertedDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
            //END
            if( convertedDate == null || convertedDate.equals(strDate)) {
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_start_exceptionCode.2505");
                setRequestFocusInThread(txtStartDate);
                isValid = false;
            }
        }else{
            strDate = convertedDate;
            txtStartDate.setText(strDate);
        }
        if ( txtEndDate.getText() !=null
                && txtEndDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;
            
            try {
                String strStartDate = txtStartDate.getText().trim();
                String strDate1 =  dtUtils.formatDate(strStartDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1==null){
                    startDate = dtFormat.parse(dtUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                }else{
                    startDate = dtFormat.parse(dtUtils.formatDate(strStartDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
                String strEndDate = txtEndDate.getText().trim();
                String strDate11 =  dtUtils.formatDate(strEndDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate11==null){
                    endDate = dtFormat.parse(dtUtils.restoreDate(strEndDate,DATE_SEPARATERS));
                }else{
                    endDate= dtFormat.parse(dtUtils.formatDate(strEndDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
                
                
            }catch(java.text.ParseException pe){
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_start_exceptionCode.2505")+convertedDate;
            }
            if(endDate!=null && endDate.compareTo(startDate)<0){
                //startdate is greater than end date - not ok
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_start_laterdate_exceptionCode.2508");
                setRequestFocusInThread(txtStartDate);
            }
            
            
            
            
//            try {
//                startDate = dtFormat.parse(dtUtils.restoreDate(convertedDate,"/:-,"));
//                endDate = dtFormat.parse(dtUtils.restoreDate(txtEndDate.getText(),"/:-,"));
//            }catch(java.text.ParseException pe){
//                mesg = coeusMessageResources.parseMessageKey(
//                "prop_invalid_start_exceptionCode.2505")+convertedDate;
//            }
//            if(endDate!=null && endDate.compareTo(startDate)<0){
//                //startdate is greater than end date - not ok
//                mesg = coeusMessageResources.parseMessageKey(
//                "prop_start_laterdate_exceptionCode.2508");
//                setRequestFocusInThread(txtStartDate);
//            }
        }
        if (mesg != null){
            isValid=false;
            CoeusOptionPane.showErrorDialog(mesg);
            txtStartDate.setText(txtPreviousStartDate);
        }
        return isValid;
    }
    
    
    
    
    
    /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateEndDate(String strDate) {
        boolean isValid=true;
        String mesg = null;
        String convertedDate = dtUtils.formatDate(strDate, "/-:," ,DATE_FORMAT_DISPLAY);
        if (convertedDate==null){
            convertedDate = dtUtils.restoreDate(strDate, DATE_FORMAT_DISPLAY);
            if( convertedDate == null || convertedDate.equals(strDate)) {
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_end_exceptionCode.2506");
                setRequestFocusInThread(txtEndDate);
                isValid = false;
            }
        }else {
            strDate = convertedDate;
            txtEndDate.setText(strDate);
        }
        if ( txtStartDate.getText() !=null
                && txtStartDate.getText().trim().length() > 0 ) {
            // compare the date
            
            Date startDate = null;
            Date endDate = null;
            
            try {
                String strStartDate = txtStartDate.getText().trim();
                String strDate1 =  dtUtils.formatDate(strStartDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1==null){
                    startDate = dtFormat.parse(dtUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                }else{
                    startDate = dtFormat.parse(dtUtils.formatDate(strStartDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
                String strEndDate = txtEndDate.getText().trim();
                String strDate11 =  dtUtils.formatDate(strEndDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate11==null){
                    endDate = dtFormat.parse(dtUtils.restoreDate(strEndDate,DATE_SEPARATERS));
                }else{
                    endDate= dtFormat.parse(dtUtils.formatDate(strEndDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
                
                
            }catch(java.text.ParseException pe){
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_end_exceptionCode.2506");
            }
            if(startDate !=null && startDate.compareTo(endDate)>0){
                //startdate is greater than end date - not ok
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_end_earlierdate_exceptionCode.2507");
                setRequestFocusInThread(txtStartDate);
            }
            
            
            
            
            
//            Date startDate = null;
//            Date endDate = null;
//            try {
//                startDate = dtFormat.parse(dtUtils.restoreDate(txtStartDate.getText(),"/:-,"));
//                endDate = dtFormat.parse(dtUtils.restoreDate(convertedDate,"/:-,"));
//            }catch(java.text.ParseException pe){
//                mesg = coeusMessageResources.parseMessageKey(
//                "prop_invalid_end_exceptionCode.2506");
//            }
//            if(startDate !=null && startDate.compareTo(endDate)>0){
//                // enddate is less than start date - not ok
//                mesg = coeusMessageResources.parseMessageKey(
//                "prop_end_earlierdate_exceptionCode.2507");
//            }
        }
        
        if (mesg != null){
            isValid=false;
            CoeusOptionPane.showErrorDialog(mesg);
            txtEndDate.setText(txtPreviousEndDate);
        }
        //        else{
        //            String focusDate = dtUtils.formatDate(strDate,"/:-,","dd-MMM-yyyy");
        //            txtEndDate.setText(focusDate);
        //            txtPreviousEndDate = txtEndDate.getText();
        //        }
        return isValid;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAwardSearch;
    public javax.swing.JButton btnPrimeSponsor;
    public javax.swing.JButton btnProposalSearch;
    public javax.swing.JButton btnSponsorSearch;
    public javax.swing.JCheckBox chkOtherAgency;
    public javax.swing.JCheckBox chkSubcontract;
    public edu.mit.coeus.utils.CoeusComboBox cmbActivityCode;
// JM 6-23-2011 changes to from JComboBox to CoeusComboBox for consistent look and feel    
    public edu.mit.coeus.utils.CoeusComboBox cmbAnticipatedAwd;
// END
    public edu.mit.coeus.utils.CoeusComboBox cmbNSFCode;
    public edu.mit.coeus.utils.CoeusComboBox cmbNoticeOfOpportunity;
    public edu.mit.coeus.utils.CoeusComboBox cmbProposalType;
    public javax.swing.JLabel lblActivityCode;
    public javax.swing.JLabel lblAgencyDivCode;
    public javax.swing.JLabel lblAgencyProgramCode;
    public javax.swing.JLabel lblAgencyRoutingIdentifier;
// JM 8-25-2011 changed from private to public
    public javax.swing.JLabel lblAnticipatedAwd;
// END
    public javax.swing.JLabel lblAwardNo;
    public javax.swing.JLabel lblBudget;
    public javax.swing.JLabel lblBudgetIcon;
    public javax.swing.JLabel lblCfdaNo;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblGGLogo;
    public javax.swing.JLabel lblLeadUnit;
    public javax.swing.JLabel lblNSFCode;
    public javax.swing.JLabel lblNarrative;
    public javax.swing.JLabel lblNarrativeIcon;
    public javax.swing.JLabel lblNoticeOfOpportunity;
    public javax.swing.JLabel lblOriginalProposal;
    public edu.mit.coeus.utils.CoeusLabel lblParentIcon;
    public javax.swing.JLabel lblPrevGGTrackingID;
    public javax.swing.JLabel lblPrimeSponsor;
    public javax.swing.JLabel lblPrimeSponsorName;
    public javax.swing.JLabel lblProgramNum;
    public javax.swing.JLabel lblProgramTitle;
    public edu.mit.coeus.utils.CoeusLabel lblPropHiearchy;
    public edu.mit.coeus.utils.CoeusLabel lblPropHiearchyIcon;
    public javax.swing.JLabel lblProposalNum;
    public javax.swing.JLabel lblProposalType;
    public javax.swing.JLabel lblSponsorCode;
    public javax.swing.JLabel lblSponsorName;
    public javax.swing.JLabel lblSponsorPropNum;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JLabel lblTitle;
    public javax.swing.JPanel pnlIcons;
    public javax.swing.JPanel pnlPropHierachyIcons;
    public javax.swing.JPanel pnlUpdate;
    public javax.swing.JScrollPane scrPnProgramTitle;
    public javax.swing.JScrollPane scrPnTitle;
    public edu.mit.coeus.utils.CoeusTextField txtAgencyDivCode;
    public edu.mit.coeus.utils.CoeusTextField txtAgencyProgramCode;
    public edu.mit.coeus.utils.CoeusTextField txtAgencyRoutingIdentifier;
    public javax.swing.JTextArea txtArProgramTitle;
    public javax.swing.JTextArea txtArTitle;
    public edu.mit.coeus.utils.CoeusTextField txtAwardNo;
    public javax.swing.JFormattedTextField txtCfdaNo;
    public edu.mit.coeus.utils.CoeusTextField txtEndDate;
    public edu.mit.coeus.utils.CoeusTextField txtLastUpdate;
    public edu.mit.coeus.utils.CoeusTextField txtLeadUnit;
    public javax.swing.JTextField txtOriginalProposal;
    public edu.mit.coeus.utils.CoeusTextField txtPrevGGTrackingID;
    public edu.mit.coeus.utils.CoeusTextField txtPrimeSponsor;
    public edu.mit.coeus.utils.CoeusTextField txtProgramNo;
    public edu.mit.coeus.utils.CoeusTextField txtProposalNum;
    public edu.mit.coeus.utils.CoeusTextField txtProposalStatus;
    public edu.mit.coeus.utils.CoeusTextField txtSponsorCode;
    public edu.mit.coeus.utils.CoeusTextField txtSponsorProposalNo;
    public edu.mit.coeus.utils.CoeusTextField txtStartDate;
    // JM 11-12-2012 added COPIED_FROM_PROP_NUM field
    public javax.swing.JLabel lblCopiedFromPropNum;
    public edu.mit.coeus.utils.CoeusTextField txtCopiedFromPropNum;
    // JM END
    // End of variables declaration//GEN-END:variables
    
      private class SponsorVerifier extends InputVerifier {
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            String spCode = tf.getText().trim();
            boolean isSponsor = false;
            boolean isPrimeSponsor = false;
            if(tf.equals(txtSponsorCode)){
                isSponsor = true;
            }else if( tf.equals(txtPrimeSponsor ) ) {
                isPrimeSponsor = true;
            }
//            if (!spCode.equals("")) {
                SponsorMaintenanceFormBean sponsorDetails = getSponsorDetails(spCode);
//                if(sponsorDetails == null){
//                    if(!invalidSponsorCode){
//                        CoeusOptionPane.showInfoDialog(
//                                coeusMessageResources.parseMessageKey(
//                                "prop_invalid_sponsor_exceptionCode.2509"));
//                    }
//                    invalidSponsorCode = false;
//                    if(isSponsor) {
//                        lblSponsorName.setText("");
//                    }else if(isPrimeSponsor) {
//                        lblPrimeSponsorName.setText("");
//                    }
//                    return false;
//                }
                
                if(isSponsor){
                    if(CoeusGuiConstants.EMPTY_STRING.equals(spCode)){
                        if(!sponsorChanged && !spCode.equals(proposalDevelopmentFormBean.getSponsorCode())){
                            sponsorChanged = true;
                        }
                        lblSponsorName.setText(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        if(!sponsorChanged && !spCode.equals(proposalDevelopmentFormBean.getSponsorCode())){
                            sponsorChanged = true;
                        }
                        if(sponsorChanged && (sponsorDetails == null || INACTIVE_STATUS.equals(sponsorDetails.getStatus()))) {
                            if(!invalidSponsorCode){
                                CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey(
                                        "prop_invalid_sponsor_exceptionCode.2509"));
                                lblSponsorName.setText(CoeusGuiConstants.EMPTY_STRING);
                                txtSponsorCode.setText(CoeusGuiConstants.EMPTY_STRING);
                                ProposalDetailAdminForm.SPONSOR_CODE = CoeusGuiConstants.EMPTY_STRING;
                                ProposalDetailAdminForm.SPONSOR_DESCRIPTION = CoeusGuiConstants.EMPTY_STRING;
                                txtSponsorCode.requestFocusInWindow();
                                return false;
                            }
                            invalidSponsorCode = false;
                            
                        }
                        lblSponsorName.setText(sponsorDetails.getName());
                        ProposalDetailAdminForm.SPONSOR_CODE = spCode;
                        ProposalDetailAdminForm.SPONSOR_DESCRIPTION = sponsorDetails.getName();
                    }
                }
                if(isPrimeSponsor) {
                    if(CoeusGuiConstants.EMPTY_STRING.equals(spCode)){
                        if(!primeSponsorChanged && !spCode.equals(proposalDevelopmentFormBean.getPrimeSponsorCode())){
                            primeSponsorChanged = true;
                        }
                        lblPrimeSponsorName.setText(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        if(!primeSponsorChanged && !spCode.equals(proposalDevelopmentFormBean.getPrimeSponsorCode())){
                            primeSponsorChanged = true;
                        }
                        if(primeSponsorChanged && (sponsorDetails == null || INACTIVE_STATUS.equals(sponsorDetails.getStatus()))) {
                            if(!invalidPrimeSponsorCode){
                                CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey(
                                        "prop_invalid_prime_sponsor_exceptionCode.2500"));
                                txtPrimeSponsor.setText(CoeusGuiConstants.EMPTY_STRING);
                                lblPrimeSponsorName.setText(CoeusGuiConstants.EMPTY_STRING);
                                txtPrimeSponsor.requestFocusInWindow();
                                return false;
                            }
                            invalidPrimeSponsorCode = false;
                            
                        }
                        lblPrimeSponsorName.setText(sponsorDetails.getName());
                    }
                }
//            } else {
//                if(isSponsor) {
//                    lblSponsorName.setText("");
//                }else if(isPrimeSponsor) {
//                    lblPrimeSponsorName.setText("");
//                }
//            }
            return true;
        }
        
    }
    
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
        
        public void focusGained(FocusEvent fe){
            
            if (fe.getSource().equals( txtStartDate ) ||
                    fe.getSource().equals( txtEndDate )){
                tempararyFlag = false;
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                &&  (!dateField.getText().trim().equals(""))) {
                    
                    oldData = dateField.getText();
                    String focusDate = dtUtils.restoreDate(
                            dateField.getText(),"/-:,");
                    dateField.setText(focusDate);
                    //saveRequired = true;
                    
                }
            }
        }
        
        public void focusLost(FocusEvent fe){
//            String mesg = "";
            temporary = fe.isTemporary();
            if (!fe.isTemporary())  {
                if(!tempararyFlag){
                    tempararyFlag = true;
                    if (fe.getSource()==txtStartDate){
                        // Added by chandra bug fix #686 12/03/2004 - start
                        if(txtStartDate==null || txtStartDate.getText().trim().length()==0 ||
                                txtStartDate.equals("")){
                            if( oldData != null && oldData.length() > 0 ) {
                                saveRequired = true;
                            }
                            txtPreviousStartDate = txtStartDate.getText();
                            // Added by chandra bug fix # 686 12/03/2004 - end
                        }else if (txtStartDate.getText().trim().length() > 0 && !validateStartDate(txtStartDate.getText().trim()) ) {
                            txtStartDate.requestFocus();
                        }
                    }else if (fe.getSource()==txtEndDate){
                        // Added by chandra bug fix 12/03/2004 - start
                        if(txtEndDate==null || txtEndDate.getText().trim().length()==0 ||txtEndDate.equals("")){
                            if( oldData != null && oldData.length() > 0 ) {
                                saveRequired = true;
                            }
                            txtPreviousStartDate = txtStartDate.getText();
                            // Added by chandra bug fix  #686 12/03/2004 - End
                        }else if (txtEndDate.getText().trim().length() > 0 &&  !validateEndDate(txtEndDate.getText().trim()) ) {
                            tempararyFlag = true;
                        }
                    }
                }
                
            }
        }
    }
    
    /** Added by chandra. Get the status of the narrative , if the parent is opened,
     *then update imeediately. else save the data to the database
     * Bug fix #976, #972 - Start chandra 30th June 2004
     */
    public void updateNarrativeStatus(char status){
        if(status=='C'){
            //modified for coeus enhancement Case#1622.Added by Shiji.
            lblNarrativeIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON)));
            //txtNarrative.setText("Complete");
            proposalDevelopmentFormBean.setNarrativeStatus("C");
            saveRequired = false;
        }else if(status == 'I'){
            //modified for coeus enhancement Case#1622.Added by Shiji
            lblNarrativeIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.INCOMPLETE_ICON)));
            //txtNarrative.setText("Incomplete");
            proposalDevelopmentFormBean.setNarrativeStatus("I");
            saveRequired = false;
        }else if(status == 'N'){
            //modified for coeus enhancement Case#1622.Added by Shiji
            lblNarrativeIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON)));
            // txtNarrative.setText("None");
            proposalDevelopmentFormBean.setNarrativeStatus("N");
            saveRequired = false;
        }
    }
    /** Added by chandra. Get the status of the budget , if the parent is opened,
     *then update imeediately. else save the data to the database
     * Bug fix #976, #972 - Start chandra 30th June 2004
     */
    public void updateBudgetStatus(String status){
        if(status.equals("C")){
            // txtBudget.setText("Complete");
            lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON)));
            proposalDevelopmentFormBean.setBudgetStatus("C");
            saveRequired = false;
        }else if(status.equals("I")){
            lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.INCOMPLETE_ICON)));
            // txtBudget.setText("Incomplete");
            proposalDevelopmentFormBean.setBudgetStatus("I");
            saveRequired = false;
        }else if(status.equals("N")){
            lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON)));
            //txtBudget.setText("None");
            proposalDevelopmentFormBean.setBudgetStatus("N");
            saveRequired = false;
        }
    }
    public static void main(String s[]){
        JFrame frame = new JFrame("ProposalDetailAdminForm");
        ProposalDetailAdminForm proposalDetailAdminForm = new ProposalDetailAdminForm();
        frame.getContentPane().add(proposalDetailAdminForm);
        frame.setSize(1000, 550);
        frame.show();
    }
    
    // End chandra 30th June 2004
 //COEUSQA-3951      
     private boolean checkS2SSubmissionType(String proposalNumber){
        boolean isS2SSubTyp_3 = false;
        try{
            isS2SSubTyp_3 =  isS2SSubmissionType(proposalNumber);
        }catch(Exception e){}
        return isS2SSubTyp_3;
    }
  
  public boolean isS2SSubmissionType(String proposalNumber) throws Exception{
        String ProposalNum= proposalNumber;        
        boolean isS2SSubTyp_3 = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_S2S_SUBMISSION_TYPE);
        requesterBean.setParameterValue(ProposalNum);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                isS2SSubTyp_3 = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return isS2SSubTyp_3;
    }  
  
  //COEUSQA-3951   
}

