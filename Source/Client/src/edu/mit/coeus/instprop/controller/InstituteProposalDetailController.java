/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * InstituteProposalDetailWindowController.java
 *
 * Created on April 26, 2004, 11:12 AM
 */
/* PMD check performed, commented unused imports on 05-MAY-2011 by Bharati
 */

package edu.mit.coeus.instprop.controller;

import edu.mit.coeus.instprop.bean.TempProposalMergeLogBean;
import edu.mit.coeus.instprop.controller.InstituteProposalController;
import edu.mit.coeus.instprop.gui.*;
import edu.mit.coeus.instprop.bean.InstituteProposalCommentsBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBaseBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
// JM 7-11-2011 added bean for NIH mechanism field   
import edu.mit.coeus.instprop.bean.InstituteProposalCustomDataBean;
// END
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;


import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
//import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author chandru
 */
public class InstituteProposalDetailController extends InstituteProposalController
implements BeanUpdatedListener,ActionListener, ItemListener{
    
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private static final char VALIDATE_BEFORE_SAVE = 'D';
    private InstituteProposalCommentsBean commentsBean;
    private InstituteProposalDetailsForm instituteProposalDetailsForm;
    private InstituteProposalBean instituteProposalBean ;
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private CoeusMessageResources coeusMessageResources;
    private char functionType;
    private QueryEngine queryEngine;
    private Component components[];
    private CoeusSearch sponsorSearch;
    private static final String SPONSOR_SEARCH = "sponsorSearch";
    private static final String SPONSOR_CODE = "SPONSOR_CODE";
    private static final String SPONSOR_NAME = "SPONSOR_NAME";
    private static final String AWARD_SEARCH = "AWARDSEARCH";
    
    /** MDI Form instance */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private static final String DISPLAY_SPONSOR = "Display Sponsor";
    
    private String sponsorName, sponsorCode;
    //Bug fixed for case #1877 start 1
//    private String primeSponsorCode;
    //Bug fixed for case #1877 end 1
    private static final int SPONSOR = 0;
    private static final int PRIME_SPONSOR = 1;
    private Object lastSelected;
    
    
    public static final int CANCEL_CLICKED = 0;
    public static final int OK_CLICKED = 1;
    private static final String FUNDED = "2";
    private boolean isCommentPresent=false;
    private boolean isMessageDisplay = false;
    
    //private String queryKey;
    private CoeusVector cvProposalStatus,cvProposalType,cvActivityType,cvNSFCode,
    cvNoticeOfOppr, cvParameter,cvCommentDescription, cvFormData,
    // Added for Case 2162  - adding Award Type - Start 
            cvAwardType;
    // Added for Case 2162  - adding Award Type - End 
    
    /** Date utils. */
    private DateUtils dateUtils = new DateUtils();
    private static final String EMPTY_STRING = "";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yy";
    private static final String LAST_UPDATE_FORMAT = DATE_FORMAT_DISPLAY + " hh:mm a";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private static final String ROLODEX_SERVLET = "/rolMntServlet";
    private static final char GET_SPONSOR_NAME = 'S';
    private static final char GET_VALID_SPONSOR_CODE = 'P';
    private static final String SERVER_ERROR = "Server Error";
    private static final String COULD_NOT_CONTACT_SERVER = "Could Not Contact Server";
    private static final String ENTER_PROPOSAL_TITLE = "instPropBase_exceptionCode.1051";
    private static final String ENTER_SPONSOR = "instPropBase_exceptionCode.1052";
    private static final String ENTER_ACTIVITY_TYPE = "instPropBase_exceptionCode.1053";
    private static final String MISSING_COMMENT_CODE = "instPropBase_exceptionCode.1003";
    /** Invalid Sponsor Code.Please enter a valid sponsor code. */
    private static final String INVALID_SPONSOR_CODE = "awardDetail_exceptionCode.1051";
    private static final String ENTER_VALID_PRIME_SPONSOR = "instPropBase_exceptionCode.1054";
    private static final String CAN_NOT_CHANGE_FUND = "instPropBase_exceptionCode.1055";
    private static final String RESEARCH = "R";
    private static final String FUND = "F";
    private String initialContractAdminId;
    private String anySponsorCode;
    //Added for case #2364 start 1
    private static final String REQUEST_INITIAL_DATE = "REQUEST_INITIAL_DATE";
    private static final String REQUEST_TOTAL_DATE = "REQUEST_TOTAL_DATE";
    //Added for case #2364 end 1
    private int accountNumberMaxLength = 0;
    //Added for the case# COEUSQA-1690-Copy Notes and Deadlines from Proposal Log into IP-start
    private static final char NEW_MODE = 'N';
    //Added for the case# COEUSQA-1690-Copy Notes and Deadlines from Proposal Log into IP-end

// JM 6-18-2011 added bean for NIH mechanism field    
    /** Custom data bean **/
    private InstituteProposalCustomDataBean instituteProposalCustomDataBean;
// END    
    
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    private CoeusDlgWindow dlgMergeDetailsForm;
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    boolean sponsorChanged = false;
    boolean isSponsorWarningFired = false;
    boolean primeSponsorChanged = false;
    private static final String INACTIVE_STATUS = "I";
    private static final char GET_CODE_FOR_VALID_SPONSOR = 'p';
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    //Added for the case# COEUSQA-1690-Copy Notes and Deadlines from Proposal Log into IP-end
    /** Creates a new instance of InstituteProposalDetailWindowController */
    public InstituteProposalDetailController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType) {
        //this.institute = instituteProposalBean;
        super(instituteProposalBaseBean);
        this.functionType = functionType;
        coeusMessageResources = CoeusMessageResources.getInstance();
        instituteProposalDetailsForm = new InstituteProposalDetailsForm();
        registerComponents();
        setFunctionType(functionType);
        instituteProposalDetailsForm.rdBtnExtra.setSelected(true);
        setFormData(instituteProposalBaseBean);
        if(functionType!= DISPLAY_PROPOSAL){
            instituteProposalDetailsForm.cmbStatus.addItemListener(this);
        }
        showCommentMissingMessage();
        formatFields();
    }
    
    public void display() {
    }
    
    /** Formats the components based on the modes the window is displayed */    
    public void formatFields() {
        // Enabling and Disabling the components
        
        if(functionType==DISPLAY_PROPOSAL){
            instituteProposalDetailsForm.txtAwardNo.setEditable(false);
            instituteProposalDetailsForm.txtArTitle.setEditable(false);
            instituteProposalDetailsForm.txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            instituteProposalDetailsForm.cmbStatus.setEnabled(false);
            instituteProposalDetailsForm.cmbProposalType.setEnabled(false);
            instituteProposalDetailsForm.txtSponsorPrpslNo.setEditable(false);
            instituteProposalDetailsForm.txtAccount.setEditable(false);
            instituteProposalDetailsForm.cmbActivityType.setEnabled(false);
            instituteProposalDetailsForm.cmbNSFCode.setEnabled(false);
            instituteProposalDetailsForm.cmbNoticeOfOpportunity.setEnabled(false);
            instituteProposalDetailsForm.txtSponsor.setEditable(false);
            instituteProposalDetailsForm.txtPrimeSponsor.setEditable(false);
            instituteProposalDetailsForm.txtInitialTotalDirectCost.setEditable(false);
            instituteProposalDetailsForm.txtInitialTotalIndirectCost.setEditable(false);
            instituteProposalDetailsForm.txtInitialRequestStartDate.setEditable(false);
            instituteProposalDetailsForm.txtInitialTotalDirectCost.setBackground((java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background"));
            instituteProposalDetailsForm.txtInitialRequestEndDate.setEditable(false);
            instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setEditable(false);
            instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setEditable(false);
            instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost.setEditable(false);
            instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost.setEditable(false);
            instituteProposalDetailsForm.txtHeadCount.setEditable(false);
            instituteProposalDetailsForm.txtPersonMonths.setEditable(false);
            instituteProposalDetailsForm.rdBtnFund.setEnabled(false);
            instituteProposalDetailsForm.rdBtnResearch.setEnabled(false);
            instituteProposalDetailsForm.btnAwardNumberSearch.setEnabled(false);
            instituteProposalDetailsForm.btnSponsorSearch.setEnabled(false);
            instituteProposalDetailsForm.btnPrimeSponsorSearch.setEnabled(false);
            instituteProposalDetailsForm.chkSubcontract.setEnabled(false);
            instituteProposalDetailsForm.txtArComments.setEditable(false);
            instituteProposalDetailsForm.txtCfda.setEditable(false);
            instituteProposalDetailsForm.txtOpportunity.setEditable(false);
            // Added for Case 2162  - adding Award Type - Start 
            instituteProposalDetailsForm.cmbAnticipatedAwardType.setEnabled(false);
            // Added for Case 2162  - adding Award Type - End
            // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start            
            instituteProposalDetailsForm.txtMergedProposal.setEditable(false);
            instituteProposalDetailsForm.txtMergedProposal.setEnabled(false);
            // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
            instituteProposalDetailsForm.txtArComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    /** returns the form file
     * @return
     */    
    public java.awt.Component getControlledUI() {
        return instituteProposalDetailsForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /** registers all the components and adding the listeners to the components */    
    public void registerComponents() {
        CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
        CustomMouseAdapetr customMouseAdapetr = new CustomMouseAdapetr();
        
        instituteProposalDetailsForm.txtInitialRequestStartDate.setDocument(new LimitedPlainDocument(11));
        instituteProposalDetailsForm.txtInitialRequestEndDate.setDocument(new LimitedPlainDocument(11));
        instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setDocument(new LimitedPlainDocument(11));
        instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setDocument(new LimitedPlainDocument(11));
        instituteProposalDetailsForm.txtSponsorPrpslNo.setDocument(new LimitedPlainDocument(65));
        
        instituteProposalDetailsForm.txtSponsor.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 6));
        instituteProposalDetailsForm.txtPrimeSponsor.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 6));
        // Case# 3822: Increase the length of the proposal title to 200 characters - Start
//        instituteProposalDetailsForm.txtArTitle.setDocument(new LimitedPlainDocument(150));
        instituteProposalDetailsForm.txtArTitle.setDocument(new LimitedPlainDocument(200));
        // Case# 3822: Increase the length of the proposal title to 200 characters - End
        instituteProposalDetailsForm.txtAwardNo.setDocument(new LimitedPlainDocument(16));
        instituteProposalDetailsForm.txtArComments.setDocument(new LimitedPlainDocument(3878));
        
        if(functionType!= DISPLAY_PROPOSAL){
            instituteProposalDetailsForm.txtSponsor.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtPrimeSponsor.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtInitialRequestStartDate.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtInitialRequestEndDate.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtAccount.addFocusListener(customFocusAdapter);


            instituteProposalDetailsForm.txtInitialTotalDirectCost.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtInitialTotalIndirectCost.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost.addFocusListener(customFocusAdapter);
            instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost.addFocusListener(customFocusAdapter);
        
        
            instituteProposalDetailsForm.btnSponsorSearch.addActionListener(this);
            instituteProposalDetailsForm.btnPrimeSponsorSearch.addActionListener(this);
            instituteProposalDetailsForm.btnAwardNumberSearch.addActionListener(this);
            instituteProposalDetailsForm.txtSponsor.addActionListener(this);
            instituteProposalDetailsForm.txtPrimeSponsor.addActionListener(this);
            
             components = new Component[]{
            instituteProposalDetailsForm.txtAwardNo, instituteProposalDetailsForm.cmbStatus,instituteProposalDetailsForm.btnMore,
            instituteProposalDetailsForm.txtArTitle, instituteProposalDetailsForm.cmbProposalType,
            instituteProposalDetailsForm.txtSponsorPrpslNo,instituteProposalDetailsForm.txtAccount,
            instituteProposalDetailsForm.cmbActivityType, instituteProposalDetailsForm.cmbNSFCode,
            instituteProposalDetailsForm.cmbNoticeOfOpportunity,
            // Added for Case 2162  - adding Award Type - Start 
            instituteProposalDetailsForm.cmbAnticipatedAwardType,
            // Added for Case 2162  - adding Award Type - End
            instituteProposalDetailsForm.txtSponsor,
            instituteProposalDetailsForm.txtPrimeSponsor,instituteProposalDetailsForm.txtInitialRequestStartDate,
            instituteProposalDetailsForm.txtTotalPeriodRequestStartDate,instituteProposalDetailsForm.txtInitialRequestEndDate,
            instituteProposalDetailsForm.txtTotalPeriodRequestEndDate,instituteProposalDetailsForm.txtInitialTotalDirectCost,
            instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost, instituteProposalDetailsForm.txtInitialTotalIndirectCost,
            instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost,instituteProposalDetailsForm.txtHeadCount,
            instituteProposalDetailsForm.txtPersonMonths, instituteProposalDetailsForm.rdBtnResearch,instituteProposalDetailsForm.rdBtnFund,
            instituteProposalDetailsForm.chkSubcontract,instituteProposalDetailsForm.txtOpportunity,instituteProposalDetailsForm.txtCfda,instituteProposalDetailsForm.txtArComments
        };
        
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        instituteProposalDetailsForm.setFocusTraversalPolicy(traversePolicy);
        instituteProposalDetailsForm.setFocusCycleRoot(true);
        }
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
        instituteProposalDetailsForm.btnMore.addActionListener(this);
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
        instituteProposalDetailsForm.txtSponsor.addMouseListener(customMouseAdapetr);
        instituteProposalDetailsForm.txtPrimeSponsor.addMouseListener(customMouseAdapetr);
        if(functionType!= DISPLAY_PROPOSAL){
            addBeanUpdatedListener(this, InstituteProposalBean.class);
        }
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
        instituteProposalDetailsForm.txtMergedProposal.setEditable(false);
        instituteProposalDetailsForm.txtMergedProposal.setEnabled(false);
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    }
    
    /** shows the missing comment code */    
    public void showCommentMissingMessage() {
        if (!isCommentPresent) {
            if (functionType!=NEW_INST_PROPOSAL) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_COMMENT_CODE));
            }
        }
    }
    
    /** sets the form data by getting from queryEngine */    
    public void setFormData(Object instituteProposalBaseBean) {
        try{
            this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean;
            queryEngine = QueryEngine.getInstance();
            cvFormData = new CoeusVector();
            cvFormData = queryEngine.executeQuery(queryKey, InstituteProposalBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvFormData!= null && cvFormData.size() > 0){
                instituteProposalBean = (InstituteProposalBean)cvFormData.get(0);
            } else {
                instituteProposalBean = new InstituteProposalBean();
            }
//            //System.out.println("in begining of setFormData , init st Date:"+instituteProposalBean.getRequestStartDateInitial().toString());
            cvActivityType = queryEngine.getDetails(queryKey,KeyConstants.ACTIVITY_TYPE);
            cvNSFCode = queryEngine.getDetails(queryKey,KeyConstants.NSF_CODE);
            cvProposalStatus = queryEngine.getDetails(queryKey,KeyConstants.INSTITUTE_PROPOSAL_STATUS);
            //cvProposalStatus.sort("description");
            cvProposalType = queryEngine.getDetails(queryKey,KeyConstants.PROPOSAL_TYPE);
            cvProposalType.sort("description");
            cvNoticeOfOppr = queryEngine.getDetails(queryKey,KeyConstants.NOTICE_OF_OPPORTUNITY);
            // Added for Case 2162  - adding Award Type - Start 
            cvAwardType = queryEngine.getDetails(queryKey,KeyConstants.AWARD_TYPE);
            // Added for Case 2162  - adding Award Type - End
            cvParameter = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//            for (int index=0;index<cvParameter.size();index++) {
//                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameter.elementAt(index);
//                if(CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
//                    isCommentPresent=true;
//                    break;
//                }
//            }
            
// JM 7-11-2011 added NIH Mechanism field
	         try {
	             CoeusVector columnValues = queryEngine.executeQuery(queryKey, InstituteProposalCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);        
	             for(int index = 0; index < columnValues.size(); index++) {
	                 instituteProposalCustomDataBean = (InstituteProposalCustomDataBean) columnValues.get(index);
	                 if (instituteProposalCustomDataBean.getColumnName().contentEquals("NIH MECHANISM")) {
	                 	instituteProposalDetailsForm.txtMechanism.setText(instituteProposalCustomDataBean.getColumnValue());
	                 }
	             } //end for
	         } catch ( CoeusException ce ) {
	         	ce.printStackTrace();
	         }
//END   
            
            if(cvParameter != null && cvParameter.size() > 0){
                //To get PROPOSAL_SUMMARY_COMMENT_CODE parameter
                CoeusVector cvFiltered = cvParameter.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE));
                if(cvFiltered !=null && cvFiltered.size() > 0){
                    isCommentPresent=true;
                }
                //To get MAX_ACCOUNT_NUMBER_LENGTH parameter
                cvFiltered = cvParameter.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                if(cvFiltered != null
                        && cvFiltered.size() > 0){
                    CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
                    accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                }
                
            }
            //Sets the AccountNumber length based on accountNumLength value and allow the field to
            //accept alphanumeric with comma,hyphen and periods
            instituteProposalDetailsForm.txtAccount.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
            //Case#2402 - End
            if (!isCommentPresent) {
                instituteProposalDetailsForm.txtArComments.setEnabled(false);
            }else {
                instituteProposalDetailsForm.txtArComments.setEnabled(true);
                cvCommentDescription=new CoeusVector();
                cvCommentDescription=queryEngine.getDetails(queryKey,InstituteProposalCommentsBean.class);
                if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
                    //CoeusVector return
                    CoeusVector cvSummaryCommentCode = cvParameter.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE));
                    CoeusParameterBean coeusParameterBean = null;
                    coeusParameterBean = (CoeusParameterBean)cvSummaryCommentCode.elementAt(0);
                    
                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                    //Added for the case# COEUSQA-1690-Copy Notes and Deadlines from Proposal Log into IP-start
                    // In order to copy the Comments from prposal log to IP, the mode is equal to New, so when it is new mode,
                    // we are copying the proposal Log Comment data to IP,else it will take the data from the text input filed
                    if(instituteProposalBean.getMode() == NEW_MODE){
                        InstituteProposalCommentsBean tempCommentBean = new InstituteProposalCommentsBean();
                        tempCommentBean=(InstituteProposalCommentsBean)cvCommentDescription.elementAt(0);
                        if(tempCommentBean != null){
                            String summaryComments = tempCommentBean.getComments();
                            if(summaryComments != null && summaryComments.length()>0){
                                instituteProposalDetailsForm.txtArComments.setText(summaryComments);
                            }
                        }
                    }else{
                     //Added for the case# COEUSQA-1690-Copy Notes and Deadlines from Proposal Log into IP-end   
                        cvCommentDescription = cvCommentDescription.filter(equals);
                        if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
                            this.commentsBean=(InstituteProposalCommentsBean)cvCommentDescription.elementAt(0);
                            instituteProposalDetailsForm.txtArComments.setText(this.commentsBean.getComments());
                            instituteProposalDetailsForm.txtArComments.setCaretPosition(0);
                        }
                    }
                }
            }
            
            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            cvActivityType.add(0, emptyBean);
            cvNSFCode.add(0, emptyBean);
            //cvProposalStatus.add(0, emptyBean);
            cvProposalType.add(0, emptyBean);
            cvNoticeOfOppr.add(0, emptyBean);
            if( instituteProposalDetailsForm.cmbStatus.getItemCount() == 0 ) {
                instituteProposalDetailsForm.cmbStatus.setModel(new DefaultComboBoxModel(cvProposalStatus));
                instituteProposalDetailsForm.cmbProposalType.setModel(new DefaultComboBoxModel(cvProposalType));
                instituteProposalDetailsForm.cmbActivityType.setModel(new DefaultComboBoxModel(cvActivityType));
                instituteProposalDetailsForm.cmbNSFCode.setModel(new DefaultComboBoxModel(cvNSFCode));
                instituteProposalDetailsForm.cmbNSFCode.setShowCode(true);
                // Added for Case 2162  - adding Award Type - Start 
                instituteProposalDetailsForm.cmbAnticipatedAwardType.setModel(new DefaultComboBoxModel(cvAwardType));
                // Added for Case 2162  - adding Award Type - End
                instituteProposalDetailsForm.cmbNoticeOfOpportunity.setModel(new DefaultComboBoxModel(cvNoticeOfOppr));
            }
            
            
            // Setting the data for the proposal status
            ComboBoxBean comboBoxBean = new ComboBoxBean();
            
            if(getFunctionType()!= NEW_INST_PROPOSAL){
                comboBoxBean.setCode(EMPTY_STRING + instituteProposalBean.getStatusCode());
                comboBoxBean.setDescription(instituteProposalBean.getStatusDescription());
                  /* If the selected item is Funded while loading then disable the combo
                   *box else enable the combo box
                   */
                if(comboBoxBean!= null && comboBoxBean.getCode().equals(FUNDED)){
                    instituteProposalDetailsForm.cmbStatus.setEnabled(false);
                    isMessageDisplay = false;
                    instituteProposalDetailsForm.cmbStatus.setBackground(java.awt.Color.WHITE);
                }
                
            }else{
                Equals eqStatus = new Equals("code",EMPTY_STRING + instituteProposalBean.getStatusCode());
                if(cvProposalStatus!= null && cvProposalStatus.size() > 0){
                    cvProposalStatus  = cvProposalStatus.filter(eqStatus);
                    if(cvProposalStatus!= null && cvProposalStatus.size() > 0){
                        comboBoxBean = (ComboBoxBean)cvProposalStatus.elementAt(0);
                    }
                }
            }
            instituteProposalDetailsForm.cmbStatus.setSelectedItem(comboBoxBean);
            lastSelected = comboBoxBean;
            
            // Setting the data for the ProposalActivity combo
            comboBoxBean = new ComboBoxBean();
            comboBoxBean.setCode(EMPTY_STRING + instituteProposalBean.getProposalActivityTypeCode());
            comboBoxBean.setDescription(instituteProposalBean.getProposalActivityTypeDescription());
            instituteProposalDetailsForm.cmbActivityType.setSelectedItem(comboBoxBean);
            
            // Setting the data for the Proposal Type
            comboBoxBean = new ComboBoxBean();
            // If the Proposal is in New mode, then set the description as Pending and code as 1
            if(getFunctionType() == NEW_INST_PROPOSAL) {
                Equals eqProposalType = new Equals("code",EMPTY_STRING + instituteProposalBean.getProposalTypeCode());
                if(cvProposalType!= null && cvProposalType.size() > 0){
                    cvProposalType  = cvProposalType.filter(eqProposalType);
                    if(cvProposalType!= null && cvProposalType.size() > 0){
                        comboBoxBean = (ComboBoxBean)cvProposalType.elementAt(0);
                    }
                }
            }else{
                comboBoxBean.setCode(EMPTY_STRING + instituteProposalBean.getProposalTypeCode());
                comboBoxBean.setDescription(instituteProposalBean.getProposalTypeDescription());
            }
            instituteProposalDetailsForm.cmbProposalType.setSelectedItem(comboBoxBean);
            
            //setting the data for the NSF code.
            comboBoxBean = new ComboBoxBean();
            if(instituteProposalBean.getNsfCode()== null || instituteProposalBean.getNsfCode().equals(EMPTY_STRING)){
                comboBoxBean.setCode(EMPTY_STRING );
                comboBoxBean.setDescription(EMPTY_STRING);
            }else{
                comboBoxBean.setCode(EMPTY_STRING + instituteProposalBean.getNsfCode());
                comboBoxBean.setDescription(instituteProposalBean.getNsfCodeDescription());
            }
            instituteProposalDetailsForm.cmbNSFCode.setSelectedItem(comboBoxBean);
            
            //setting the data for the Notice of oppurtunity code.
            comboBoxBean = new ComboBoxBean();
            if(instituteProposalBean.getNoticeOfOpportunityCode()==0){
                comboBoxBean.setCode(EMPTY_STRING);
                comboBoxBean.setDescription(EMPTY_STRING);
            }else{
                comboBoxBean.setCode(EMPTY_STRING + instituteProposalBean.getNoticeOfOpportunityCode());
                comboBoxBean.setDescription(instituteProposalBean.getNoticeOfOpportunityDescription());
            }
            instituteProposalDetailsForm.cmbNoticeOfOpportunity.setSelectedItem(comboBoxBean);
            
            // Added for Case 2162  - adding Award Type - Start 
            comboBoxBean = new ComboBoxBean();
            if(getFunctionType() == NEW_INST_PROPOSAL) {
                Equals eqProposalType = new Equals("code",EMPTY_STRING + instituteProposalBean.getAwardTypeCode());
                if(cvAwardType!= null && cvAwardType.size() > 0){
                    cvAwardType  = cvAwardType.filter(eqProposalType);
                    if(cvAwardType!= null && cvAwardType.size() > 0){
                        comboBoxBean = (ComboBoxBean)cvAwardType.elementAt(0);
                    }
                }
            }else{
                comboBoxBean.setCode(EMPTY_STRING + instituteProposalBean.getAwardTypeCode());
                comboBoxBean.setDescription(instituteProposalBean.getAwardTypeDesc());
            }
            instituteProposalDetailsForm.cmbAnticipatedAwardType.setSelectedItem(comboBoxBean);
            
            // Added for Case 2162  - adding Award Type - End
            
            instituteProposalDetailsForm.txtProposalNo.setText(instituteProposalBean.getProposalNumber());
            if(functionType!= NEW_INST_PROPOSAL){
                instituteProposalDetailsForm.txtSequenceNo.setText(EMPTY_STRING + instituteProposalBean.getSequenceNumber());
            }else{
                instituteProposalDetailsForm.txtSequenceNo.setText("1");
            }
            instituteProposalDetailsForm.txtAwardNo.setText(instituteProposalBean.getCurrentAwardNumber());
            instituteProposalDetailsForm.txtArTitle.setText(instituteProposalBean.getTitle());
            instituteProposalDetailsForm.txtArTitle.setCaretPosition(0);

            instituteProposalDetailsForm.txtSponsorPrpslNo.setText(instituteProposalBean.getSponsorProposalNumber());
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //Checks if currentAccountNumber is greater than account number length from parameter, 
            //then currentAccountNumber is substring to accountNumLength
            //instituteProposalDetailsForm.txtAccount.setText(instituteProposalBean.getCurrentAccountNumber());
            String currentAccountNumber = instituteProposalBean.getCurrentAccountNumber();
            if(currentAccountNumber == null){
                currentAccountNumber = CoeusGuiConstants.EMPTY_STRING;
            }else if(currentAccountNumber.length() > accountNumberMaxLength){
                currentAccountNumber = currentAccountNumber.substring(0,accountNumberMaxLength);
            }
            instituteProposalDetailsForm.txtAccount.setText(currentAccountNumber);
            //Case#2402 - End
            
            instituteProposalDetailsForm.txtSponsor.setText(instituteProposalBean.getSponsorCode());

            instituteProposalDetailsForm.lblSponsorName.setText(instituteProposalBean.getSponsorName());
            instituteProposalDetailsForm.txtPrimeSponsor.setText(instituteProposalBean.getPrimeSponsorCode());
            instituteProposalDetailsForm.lblPrimeSponsorName.setText(instituteProposalBean.getPrimeSponsorName());
            //Case # 2097
            instituteProposalDetailsForm.txtOpportunity.setText(instituteProposalBean.getOpportunity());
            
            if(instituteProposalBean.getRequestStartDateInitial()!= null){
                String stDate = dateUtils.formatDate(instituteProposalBean.getRequestStartDateInitial().toString(),DATE_FORMAT_DISPLAY);
                ////System.out.println("init start date :"+stDate);
                instituteProposalDetailsForm.txtInitialRequestStartDate.setText( stDate );
              
                
            }else{
                instituteProposalDetailsForm.txtInitialRequestStartDate.setText(null);
            }
            
            if(instituteProposalBean.getRequestStartDateTotal()!= null){
                instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setText(
                dateUtils.formatDate(instituteProposalBean.getRequestStartDateTotal().toString(),DATE_FORMAT_DISPLAY));
            }else{
                instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setText(null);
            }
            
            if(instituteProposalBean.getRequestEndDateInitial()!= null){
                instituteProposalDetailsForm.txtInitialRequestEndDate.setText(
                dateUtils.formatDate(instituteProposalBean.getRequestEndDateInitial().toString(), DATE_FORMAT_DISPLAY));
            }else{
                instituteProposalDetailsForm.txtInitialRequestEndDate.setText(null);
            }
            
            if(instituteProposalBean.getRequestEndDateTotal()!= null){
                instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setText(
                dateUtils.formatDate(instituteProposalBean.getRequestEndDateTotal().toString(), DATE_FORMAT_DISPLAY));
            }else{
                instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setText(null);
            }
            
            if(instituteProposalBean.getCreateTimeStamp()!= null){
                instituteProposalDetailsForm.txtPropCreateDate.setText(
                dateUtils.formatDate(instituteProposalBean.getCreateTimeStamp().toString(), DATE_FORMAT_DISPLAY));
            }
            instituteProposalDetailsForm.txtInitialTotalDirectCost.setText(EMPTY_STRING + instituteProposalBean.getTotalDirectCostInitial());
            instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost.setText(EMPTY_STRING + instituteProposalBean.getTotalDirectCostTotal());
            instituteProposalDetailsForm.txtInitialTotalIndirectCost.setText(EMPTY_STRING + instituteProposalBean.getTotalInDirectCostInitial());
            instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost.setText(EMPTY_STRING + instituteProposalBean.getTotalInDirectCostTotal());
            //  if(instituteProposalDetailsForm.txtHeadCount.getText()!= null){
            instituteProposalDetailsForm.txtHeadCount.setText(EMPTY_STRING + instituteProposalBean.getGradStudHeadCount());
            //}else{
            //  instituteProposalDetailsForm.txtHeadCount.setText(null);
            // }
            //Case # 2097
            instituteProposalDetailsForm.txtCfda.setText(instituteProposalBean.getCfdaNumber());
            instituteProposalDetailsForm.txtPersonMonths.setText(EMPTY_STRING + instituteProposalBean.getGradStudPersonMonths());
            instituteProposalDetailsForm.txtInitialContractAdmin.setText(instituteProposalBean.getInitialContractAdminName());
            initialContractAdminId = instituteProposalBean.getInitialContractAdmin();
            
            
            double totalInitialCost = new Double(instituteProposalDetailsForm.txtInitialTotalDirectCost.getValue()).doubleValue() + new Double(instituteProposalDetailsForm.txtInitialTotalIndirectCost.getValue()).doubleValue();
            instituteProposalDetailsForm.txtInitialTotalAllCost.setText(EMPTY_STRING + totalInitialCost);
            
            double totalInitialToatlCost = new Double(instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost.getValue()).doubleValue() + new Double(instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost.getValue()).doubleValue();
            instituteProposalDetailsForm.txtTotalPeriodTotalAllCost.setText(EMPTY_STRING + totalInitialToatlCost);
            
//            instituteProposalDetailsForm.txtUpdateUser.setText(instituteProposalBean.getUpdateUser());
            /*
             * UserID to UserName Enhancement - Start
             * Added UserUtils class to change userid to username
             */
            instituteProposalDetailsForm.txtUpdateUser.setText(UserUtils.getDisplayName(instituteProposalBean.getUpdateUser()));
            // UserId to UserName Enhancement - End
            
            simpleDateFormat.applyPattern(LAST_UPDATE_FORMAT);
            if(instituteProposalBean.getUpdateTimestamp() != null) {
                instituteProposalDetailsForm.txtLastUpdate.setText(simpleDateFormat.format(instituteProposalBean.getUpdateTimestamp()));
            }
            simpleDateFormat.applyPattern(SIMPLE_DATE_FORMAT);
            
            if(instituteProposalBean.isSubcontractFlag()){
                instituteProposalDetailsForm.chkSubcontract.setSelected(true);
            }else{
                instituteProposalDetailsForm.chkSubcontract.setSelected(false);
            }
            
            // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
            if(instituteProposalBean.getMergedProposalData() != null && !instituteProposalBean.getMergedProposalData().isEmpty()){
                if(instituteProposalBean.getMergedProposalData().size() >= 1){
                    TempProposalMergeLogBean tempProposalMergeLogBean = (TempProposalMergeLogBean)instituteProposalBean.getMergedProposalData().get(0);
                    instituteProposalDetailsForm.txtMergedProposal.setText(tempProposalMergeLogBean.getTempProposalNumber().toString());
                }else{
                    instituteProposalDetailsForm.txtMergedProposal.setText(CoeusGuiConstants.EMPTY_STRING);
                }
            }else{
                instituteProposalDetailsForm.btnMore.setEnabled(false);
            }
             // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
            /** If the account type is R - Research. else if Account Type is F - Fund
             */
            String accountType = instituteProposalBean.getTypeOfAccount();
            if(accountType == null)return ;
            if(accountType.equals(RESEARCH)){
                instituteProposalDetailsForm.rdBtnResearch.setSelected(true);
            }else if(accountType.equals(FUND)){
                instituteProposalDetailsForm.rdBtnFund.setSelected(true);
            }                         
            //  }
        }catch (CoeusException  coeusException){
            coeusException.printStackTrace();
        }
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        sponsorChanged = false;
        primeSponsorChanged = false;
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    }
    
    // saves the form data.
    /** saves the form data */    
    public void saveFormData() {
        
        String strDate;
        Date date;
        int code;
        ComboBoxBean comboBoxBean;
        try{
// JM 8-24-2011 anticipated award type required; moved block to top of method to check first      	
            // Added for Case 2162  - adding Award Type - Start 
            comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbAnticipatedAwardType.getSelectedItem();
            if(comboBoxBean.getCode()== null ||comboBoxBean.getCode().equals(EMPTY_STRING) || Integer.parseInt(comboBoxBean.getCode()) == 0) {
            	CoeusOptionPane.showErrorDialog("Anticipated award type is required.");
            	return;
//             	  instituteProposalBean.setAwardTypeCode(0);
//                instituteProposalBean.setAwardTypeDesc(null);
            }else {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setAwardTypeCode(code);
                instituteProposalBean.setAwardTypeDesc(comboBoxBean.getDescription());
            }
            // Added for Case 2162  - adding Award Type - End
// END JM
            
            // setting combo value for the proposal status combo
            comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbStatus.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setStatusCode(code);
                instituteProposalBean.setStatusDescription(comboBoxBean.getDescription());
            }
            // Setting the Proposal Type combo
            comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbProposalType.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setProposalTypeCode(code);
                instituteProposalBean.setProposalTypeDescription(comboBoxBean.getDescription());
            }
            // Setting the combo values for Activity Type
            comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbActivityType.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setProposalActivityTypeCode(code);
                instituteProposalBean.setProposalActivityTypeDescription(comboBoxBean.getDescription());
            }else {
                instituteProposalBean.setProposalActivityTypeCode(0);
                instituteProposalBean.setProposalActivityTypeDescription(null);
            }
            // Setting the comob value for the NSF Code
            comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbNSFCode.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                //code = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setNsfCode(comboBoxBean.getCode());
                instituteProposalBean.setNsfCodeDescription(comboBoxBean.getDescription());
            }else {
                instituteProposalBean.setNsfCode(null);
                instituteProposalBean.setNsfCodeDescription(null);
            }
            // Setting combo value for the Notice of Opportunity
            comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbNoticeOfOpportunity.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setNoticeOfOpportunityCode(code);
                instituteProposalBean.setNoticeOfOpportunityDescription(comboBoxBean.getDescription());
            }else {
                instituteProposalBean.setNoticeOfOpportunityCode(0 );
                instituteProposalBean.setNoticeOfOpportunityDescription(null);
            }
            
            // Added for Case 2162  - adding Award Type - Start 
            comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbAnticipatedAwardType.getSelectedItem();
            if(comboBoxBean.getCode()== null ||comboBoxBean.getCode().equals(EMPTY_STRING) || Integer.parseInt(comboBoxBean.getCode()) == 0) {
                instituteProposalBean.setAwardTypeCode(0);
                instituteProposalBean.setAwardTypeDesc(null);
            }else {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setAwardTypeCode(code);
                instituteProposalBean.setAwardTypeDesc(comboBoxBean.getDescription());
            }
            // Added for Case 2162  - adding Award Type - End
            
            // Setting the Initial Request Start Date
            strDate = instituteProposalDetailsForm.txtInitialRequestStartDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if(strDate1== null || strDate1.equals(strDate)){
                        if(getFunctionType()== NEW_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                            instituteProposalBean.setRequestStartDateInitial(null);
                        }
                    }else{
                        date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        instituteProposalBean.setRequestStartDateInitial(new java.sql.Date(date.getTime()));
                    }
                }else{
                    //System.out.println("in saveform data:"+strDate);
                    //date = simpleDateFormat.parse(strDate);
//                if(getFunctionType()== NEW_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
//                }
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    instituteProposalBean.setRequestStartDateInitial(new java.sql.Date(date.getTime()));
                }
            }else{
//                if(getFunctionType()== NEW_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
//                }
                instituteProposalBean.setRequestStartDateInitial(null);
            }
            // setting the  Initial Request End date
            strDate = instituteProposalDetailsForm.txtInitialRequestEndDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if(strDate1== null || strDate1.equals(strDate)){
                        if(getFunctionType()== NEW_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        instituteProposalBean.setRequestEndDateInitial(null);
                    }else{
                        date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        instituteProposalBean.setRequestEndDateInitial(new java.sql.Date(date.getTime()));
                    }
                }else{
                    //System.out.println("in saveform data:"+strDate);
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    instituteProposalBean.setRequestEndDateInitial(new java.sql.Date(date.getTime()));
                }
            }else{
//                if(getFunctionType()== NEW_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
//                }
                instituteProposalBean.setRequestEndDateInitial(null);
            }
            
            
            // Setting the Total Period Request Start Date
            strDate = instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1== null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if( strDate1 == null || strDate1.equals(strDate)) {
                        if(getFunctionType()== NEW_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        instituteProposalBean.setRequestStartDateTotal(null);
                    }else{
                        date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        instituteProposalBean.setRequestStartDateTotal(new java.sql.Date(date.getTime()));
                    }
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    instituteProposalBean.setRequestStartDateTotal(new java.sql.Date(date.getTime()));
                }
            }else{
//                if(getFunctionType()== NEW_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
//                }
                instituteProposalBean.setRequestStartDateTotal(null);
            }
            // Setting the Total Period Request End Date.
            strDate = instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1== null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if( strDate1 == null || strDate1.equals(strDate)) {
                        if(getFunctionType()== NEW_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        instituteProposalBean.setRequestEndDateTotal(null);
                    }else{
                        date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        instituteProposalBean.setRequestEndDateTotal(new java.sql.Date(date.getTime()));
                    }
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    instituteProposalBean.setRequestEndDateTotal(new java.sql.Date(date.getTime()));
                }
            }else{
//                if(getFunctionType()== NEW_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()== NEW_ENTRY_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
//                }else if(getFunctionType()==CORRECT_INST_PROPOSAL){
//                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
//                }
                instituteProposalBean.setRequestEndDateTotal(null);
            }
            
             //Case # 2097
             //Remove Period(.) before saving.
            String strCfdaNo = instituteProposalDetailsForm.txtCfda.getText();
            strCfdaNo = strCfdaNo.substring(0,2) + strCfdaNo.substring(3);
            if(strCfdaNo!= EMPTY_STRING){
                instituteProposalBean.setCfdaNumber(strCfdaNo.trim().length() == 0 ? null :strCfdaNo.trim());
            }else{
                instituteProposalBean.setCfdaNumber(null);
            }
            // Case 2097
            if(instituteProposalDetailsForm.txtOpportunity.getText()!= EMPTY_STRING){
                instituteProposalBean.setOpportunity(instituteProposalDetailsForm.txtOpportunity.getText());
            }else{
                instituteProposalBean.setOpportunity(null);
            }
            
            instituteProposalBean.setTitle(instituteProposalDetailsForm.txtArTitle.getText().trim());
            instituteProposalBean.setCurrentAccountNumber(instituteProposalDetailsForm.txtAccount.getText().trim());
            if(instituteProposalDetailsForm.txtSponsorPrpslNo.getText()!= EMPTY_STRING){
                instituteProposalBean.setSponsorProposalNumber(instituteProposalDetailsForm.txtSponsorPrpslNo.getText().trim());
            }else{
                instituteProposalBean.setSponsorProposalNumber(null);
            }
            String headCount = instituteProposalDetailsForm.txtHeadCount.getText();
            if(headCount != null && headCount.trim().length() > 0 ){
                instituteProposalBean.setGradStudHeadCount(new Integer(instituteProposalDetailsForm.txtHeadCount.getText()).intValue());
            }else{
                instituteProposalBean.setGradStudHeadCount(0);
            }
            if(instituteProposalDetailsForm.txtPersonMonths.getText()!= null){
                instituteProposalBean.setGradStudPersonMonths(new Double(instituteProposalDetailsForm.txtPersonMonths.getText()).doubleValue());
            }else{
                instituteProposalBean.setGradStudPersonMonths(0.0);
            }
            if(instituteProposalDetailsForm.txtAwardNo.getText()!= null){
                instituteProposalBean.setCurrentAwardNumber(instituteProposalDetailsForm.txtAwardNo.getText().trim());
            }else{
                instituteProposalBean.setCurrentAwardNumber(null);
            }
             //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
            if(instituteProposalDetailsForm.txtSponsor.getText()!= null){
                sponsorCode =  instituteProposalDetailsForm.txtSponsor.getText().trim();
                if(!sponsorCode.equals(instituteProposalBean.getSponsorCode()) || sponsorChanged || instituteProposalBean.getMode() == NEW_MODE){
                    //if(!sponsorCode.equals(instituteProposalBean.getSponsorCode()) || sponsorChanged){
                    /*modified for the Bug Fix:1666 for alpha numeric sponsor code step:1*/
                    anySponsorCode=getValidSponsorCode(sponsorCode);
                    if(anySponsorCode!=null&&!anySponsorCode.trim().equals(EMPTY_STRING)){
                        instituteProposalBean.setSponsorCode(anySponsorCode);
                    }else{
                        instituteProposalBean.setSponsorCode(EMPTY_STRING);
                    }
                }
                /*end step:1*/
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
            }else{
                instituteProposalBean.setSponsorCode(null);
            }
            
            if(instituteProposalDetailsForm.lblSponsorName.getText()!= null && !(instituteProposalDetailsForm.lblSponsorName.getText().trim().equals(EMPTY_STRING))){
                instituteProposalBean.setSponsorName(instituteProposalDetailsForm.lblSponsorName.getText().trim());
            }else{
                instituteProposalBean.setSponsorName(EMPTY_STRING);
            }
            
            /*modified for the Bug Fix:1666 for alpha numeric prime sponsor code step:2*/
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
            if(instituteProposalDetailsForm.txtPrimeSponsor.getText()!= null){
                String primeSponsorCode = instituteProposalDetailsForm.txtPrimeSponsor.getText().trim();
                if(!primeSponsorCode.equals(instituteProposalBean.getPrimeSponsorCode()) || sponsorChanged){
                    anySponsorCode=getValidSponsorCode(primeSponsorCode);
                    if(anySponsorCode!=null&&!anySponsorCode.trim().equals(EMPTY_STRING)){
                        instituteProposalBean.setPrimeSponsorCode(anySponsorCode);
                    }else{
                        instituteProposalBean.setPrimeSponsorCode(EMPTY_STRING);
                    }
                }
            }
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
            /*end step:2*/
            if(instituteProposalDetailsForm.lblPrimeSponsorName.getText()!= null && !(instituteProposalDetailsForm.lblPrimeSponsorName.getText().trim().equals(EMPTY_STRING))){
                instituteProposalBean.setPrimeSponsorName(instituteProposalDetailsForm.lblPrimeSponsorName.getText().trim());
            }else{
                instituteProposalBean.setPrimeSponsorName(null);
            }

            
            // Setting values for the Accounts
            if(instituteProposalDetailsForm.rdBtnFund.isSelected()){
                instituteProposalBean.setTypeOfAccount(FUND);
            }else if(instituteProposalDetailsForm.rdBtnResearch.isSelected()){
                instituteProposalBean.setTypeOfAccount(RESEARCH);
            }
            
            // Setting the value for the subcontract flag
            if(instituteProposalDetailsForm.chkSubcontract.isSelected()){
                instituteProposalBean.setSubcontractFlag(true);
            }else{
                instituteProposalBean.setSubcontractFlag(false);
            }
            // setting the value for the TotalDirectCostInitial
            instituteProposalBean.setTotalDirectCostInitial(
            new Double(instituteProposalDetailsForm.txtInitialTotalDirectCost.getValue()).doubleValue());
            
            instituteProposalBean.setTotalInDirectCostInitial(
            new Double(instituteProposalDetailsForm.txtInitialTotalIndirectCost.getValue()).doubleValue());
            
            instituteProposalBean.setTotalDirectCostTotal(
            new Double(instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost.getValue()).doubleValue());
            
            instituteProposalBean.setTotalInDirectCostTotal(
            new Double(instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost.getValue()).doubleValue());
            instituteProposalBean.setInitialContractAdminName(instituteProposalDetailsForm.txtInitialContractAdmin.getText().trim());
            if(initialContractAdminId!= null && (!initialContractAdminId.trim().equals(EMPTY_STRING))){
                instituteProposalBean.setInitialContractAdmin(initialContractAdminId);
            }else{
                instituteProposalBean.setInitialContractAdmin(EMPTY_STRING);
            }
            
            /** Update the beans if it is changed. Check whether the data is changed or
             *not. If it is changed make it is as an update else insert
             */
            
            StrictEquals stInstProposalEquals = new StrictEquals();
            StrictEquals stCommentEquals = new StrictEquals();
            
            InstituteProposalCommentsBean queryCommentsBean = new InstituteProposalCommentsBean();
            InstituteProposalBean queryProposalBean = new InstituteProposalBean();
            
            CoeusVector cvTemp = queryEngine.getDetails(queryKey, InstituteProposalBean.class);
            if(cvTemp != null && cvTemp.size() > 0) {
                queryProposalBean  = (InstituteProposalBean)cvTemp.get(0);
            }
            
            // check for the comments bean.
            //Added for the case# COEUSQA-1690-Copy Notes and Deadlines from Proposal Log into IP-start
            // In case of new mode we are cpying comments data from proposal log, at that time the value is not available,
            // so only in modify mode, we are gettting InstituteProposalCommentsBean
            //CoeusVector cvTempComment = queryEngine.getDetails(queryKey, InstituteProposalCommentsBean.class);
            CoeusVector cvTempComment = new CoeusVector();
            if(instituteProposalBean.getMode() != NEW_MODE){
                cvTempComment = queryEngine.getDetails(queryKey, InstituteProposalCommentsBean.class);
            }
            
            cvParameter = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvSummaryComment = null;
            CoeusParameterBean coeusParameterBean = null;
            CoeusVector cvSummaryCommentCode = cvParameter.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE));
            if(cvSummaryCommentCode!=null && cvSummaryCommentCode.size() > 0){
                coeusParameterBean = (CoeusParameterBean)cvSummaryCommentCode.elementAt(0);
            }
            
            if (cvTempComment!= null && cvTempComment.size()>0) {
                //CoeusVector return
                if(coeusParameterBean!=null){
                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                    
                    cvSummaryComment = cvTempComment.filter(equals);
                    if(cvSummaryComment!=null && cvSummaryComment.size() > 0){
                        queryCommentsBean = (InstituteProposalCommentsBean)cvSummaryComment.elementAt(0);
                    }
                }
            }
            if(coeusParameterBean!= null){
                if(commentsBean!= null){
                    commentsBean.setComments(instituteProposalDetailsForm.txtArComments.getText());
                    if(! stCommentEquals.compare(commentsBean, queryCommentsBean)){
                        //Data Changed. save to query Engine.
                        commentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, commentsBean);
                    }              
                }else{
                    if(!EMPTY_STRING.equals(instituteProposalDetailsForm.txtArComments.getText())){
                        commentsBean = new InstituteProposalCommentsBean();
                        commentsBean.setProposalNumber(this.instituteProposalBaseBean.getProposalNumber());
                        commentsBean.setSequenceNumber(this.instituteProposalBaseBean.getSequenceNumber());
                        commentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
                        commentsBean.setComments(instituteProposalDetailsForm.txtArComments.getText());
                        commentsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, commentsBean);
                    }
                }
            }
            
            if(! stInstProposalEquals.compare(instituteProposalBean, queryProposalBean)){
                //Data Changed. save to query Engine.
                if(getFunctionType() == CORRECT_INST_PROPOSAL) {
                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, instituteProposalBean);
                }else {
                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, instituteProposalBean);
                }
                //Fire Proposal Details Modified event
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setBean(instituteProposalBean);
                fireBeanUpdated(beanEvent);
            }
            // }
            
        }catch (ParseException parseException){
            parseException.printStackTrace();
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
    }
    
    /** Validates the forma data
     */
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        //refresh();
        String primeSponsorName = EMPTY_STRING;
        String name = EMPTY_STRING;
        String awardNumber;
        String accountNumber;
        String strDate;
        // Date date;
        CoeusVector cvValidData = new CoeusVector();
        
        // Validate for the proposal title.
        if(instituteProposalDetailsForm.txtArTitle.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_PROPOSAL_TITLE));
            instituteProposalDetailsForm.txtArTitle.requestFocusInWindow();
            return false;
        }
        // If the Activity type is null or empty string
        if(instituteProposalDetailsForm.cmbActivityType.getSelectedItem().toString().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_ACTIVITY_TYPE));
            instituteProposalDetailsForm.cmbActivityType.requestFocusInWindow();
            return false;
        }
        // Validate for the Sponsor field
        
        if(instituteProposalDetailsForm.txtSponsor.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_SPONSOR));
            instituteProposalDetailsForm.txtSponsor.requestFocusInWindow();
            return false;
            
        } 
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        try{
            String sponsorCode = instituteProposalDetailsForm.txtSponsor.getText().trim();
            if(!CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode) &&
                    CoeusGuiConstants.EMPTY_STRING.equals(instituteProposalBean.getSponsorCode())){
                String sponsorName = getSponsorName(sponsorCode,true);
                if(CoeusGuiConstants.EMPTY_STRING.equals(sponsorName)){
                    return false;
                }
            }
            
            if(instituteProposalDetailsForm.txtPrimeSponsor.getText()!= null){
                String primeSponsorCode = instituteProposalDetailsForm.txtPrimeSponsor.getText().trim();
                if(!CoeusGuiConstants.EMPTY_STRING.equals(primeSponsorCode) &&
                        CoeusGuiConstants.EMPTY_STRING.equals(instituteProposalBean.getPrimeSponsorCode())){
                    primeSponsorName = getSponsorName(primeSponsorCode,false);
                    if(CoeusGuiConstants.EMPTY_STRING.equals(primeSponsorName)){
                        return false;
                    }
                }
            }
        }catch(Exception e){
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        }
        // To check whether the award number and account number are valid or not.
        // Make a server side call to evaluate it.
        
        awardNumber = instituteProposalDetailsForm.txtAwardNo.getText().trim();
        accountNumber = instituteProposalDetailsForm.txtAccount.getText().trim();
        
        InstituteProposalBean validateBean = new InstituteProposalBean();
        if(awardNumber!= null && (!awardNumber.equals(EMPTY_STRING))){
            validateBean.setCurrentAwardNumber(awardNumber);
        }else{
            validateBean.setCurrentAwardNumber(null);
        }
        validateBean.setCurrentAccountNumber(accountNumber);
        //Added for Case#2402 :use a parameter to set the length of the account number throughout app - Start
        Object selectItem = instituteProposalDetailsForm.cmbStatus.getSelectedItem();
        if(selectItem instanceof ComboBoxBean){
            ComboBoxBean comboBoxBean = (ComboBoxBean)selectItem;
            if(comboBoxBean != null){
                String statusCode = comboBoxBean.getCode();
                validateBean.setStatusCode(Integer.parseInt(statusCode));
            }
        }
        //Case#2402 - End
        try{
            cvValidData = validateFormData(validateBean);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        if(cvValidData!= null){
            boolean isValidAwardNumber = ((Boolean)cvValidData.elementAt(0)).booleanValue();
            boolean isValidAccountNumber = ((Boolean)cvValidData.elementAt(1)).booleanValue();
            
            if(!isValidAwardNumber){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("The award number " +awardNumber + " does not exist. Please enter a valid award number"));
                instituteProposalDetailsForm.txtAwardNo.requestFocusInWindow();
                return false;
            }else if(!isValidAccountNumber){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("The account number " +accountNumber + " does not exist. Please enter a valid account number"));
                setRequestFocusInThread(instituteProposalDetailsForm.txtAccount);
                return false;
            }
        }
        
        strDate = instituteProposalDetailsForm.txtInitialRequestStartDate.getText().trim();
        if(! strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) { 
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Please enter a valid initial start date"));
                    instituteProposalDetailsForm.txtInitialRequestStartDate.requestFocusInWindow();
                    return false;
                }
            }else {
                    strDate = strDate1;
//                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                instituteProposalDetailsForm.txtInitialRequestStartDate.setText(strDate);
                
            }
        }
        //End initial request start date Validation.
        //added for case #2364 start 2
        if(strDate.trim().length() > 0){
            if(!validateStartDate(strDate, REQUEST_INITIAL_DATE)){
                return false;
            }
        }

        //Added for case #2364 end 2
        
        strDate = instituteProposalDetailsForm.txtInitialRequestEndDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) { 
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Please enter a valid initial end date"));
                    instituteProposalDetailsForm.txtInitialRequestEndDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
//                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                instituteProposalDetailsForm.txtInitialRequestEndDate.setText(strDate);
                
            }
        }
        //added for case #2364 start 3
        if(strDate.trim().length() > 0){
            if(!validateEndDate(strDate, REQUEST_INITIAL_DATE)){
                return false;
            }
        }
        //Added for case #2364 end 3
        //End initial request start date Validation.
        strDate = instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.getText();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
            if(strDate1 == null) {
                strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if( strDate1 == null || strDate1.equals(strDate)) { 
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Please enter a valid total start date"));
                    instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
//                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setText(strDate);
                
            }
        }
        
        //End Total initial request end date Validation.
        //added for case #2364 start 4
        boolean isValid = false;
         if(strDate.trim().length() > 0){
             isValid = validateStartDate(strDate, REQUEST_TOTAL_DATE);
             if(isValid){
                 isValid = validateInitialTotalDate();
             }
             if(!isValid){
                 return false;
             }
         }

         //Added for case #2364 end 4
        strDate = instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if(strDate1== null || strDate1.equals(strDate)){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Please enter a valid total end date"));
                    instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
//                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setText(strDate);
               
            }
        }
        //added for case #2364 start 5
         if(strDate.trim().length() > 0){
             isValid = validateEndDate(strDate, REQUEST_TOTAL_DATE);
             if(isValid){
                 isValid = validateInitialTotalDate();
             }
             if(!isValid){
                 return false;
             }
         }
         //Added for case #2364 end 5
        return true;
    }
    
    
    
    
    public class CustomFocusAdapter extends FocusAdapter{
        
        /**
         * @param focusEvent
         */        
        public void focusGained(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()) return ;
            
            Object source = focusEvent.getSource();
            
            if(source.equals(instituteProposalDetailsForm.txtInitialRequestStartDate)) {
                String initialRequestStartDate;
                initialRequestStartDate = instituteProposalDetailsForm.txtInitialRequestStartDate.getText();
                initialRequestStartDate  = dateUtils.restoreDate(initialRequestStartDate , DATE_SEPARATERS);
                instituteProposalDetailsForm.txtInitialRequestStartDate.setText(initialRequestStartDate );
                
            }else if(source.equals(instituteProposalDetailsForm.txtInitialRequestEndDate)){
                String initialRequestEndDate;
                initialRequestEndDate= instituteProposalDetailsForm.txtInitialRequestEndDate.getText();
                initialRequestEndDate  = dateUtils.restoreDate(initialRequestEndDate , DATE_SEPARATERS);
                instituteProposalDetailsForm.txtInitialRequestEndDate.setText(initialRequestEndDate );
                
            }else if(source.equals(instituteProposalDetailsForm.txtTotalPeriodRequestStartDate)){
                String totalRequestStartDate;
                totalRequestStartDate = instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.getText();
                totalRequestStartDate  = dateUtils.restoreDate(totalRequestStartDate , DATE_SEPARATERS);
                instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setText(totalRequestStartDate);
                
            }else if(source.equals(instituteProposalDetailsForm.txtTotalPeriodRequestEndDate)){
                String totalRequestEndDate;
                totalRequestEndDate = instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText();
                totalRequestEndDate = dateUtils.restoreDate(totalRequestEndDate , DATE_SEPARATERS);
                instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setText(totalRequestEndDate);
            }else if(source.equals(instituteProposalDetailsForm.txtAccount)){
                instituteProposalDetailsForm.txtAccount.setCaretPosition(0);
            }
           }
        
        
        /**
         * @param focusEvent
         */        
        public void focusLost(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()) return ;
            
            Object source = focusEvent.getSource();
             //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
            if(source.equals(instituteProposalDetailsForm.txtSponsor)) {
                String sponsorName = CoeusGuiConstants.EMPTY_STRING;

                try{
                    String sponsorCode = instituteProposalDetailsForm.txtSponsor.getText().trim();
                    
                    if(!sponsorChanged && !sponsorCode.equals(instituteProposalBean.getSponsorCode())){
                        sponsorChanged = true;
                    }
                    
                    if(sponsorChanged && !CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)){
                        sponsorName= getSponsorName(sponsorCode,true);
                    }else if(!CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)){
                        sponsorName= instituteProposalBean.getSponsorName();
                    }
                    
                }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                }
                instituteProposalDetailsForm.lblSponsorName.setText(sponsorName);
                
            }else if(source.equals(instituteProposalDetailsForm.txtPrimeSponsor)) {
                String primeSponsorName = CoeusGuiConstants.EMPTY_STRING;
                
                try{
                    String primeSponsorCode = instituteProposalDetailsForm.txtPrimeSponsor.getText().trim();
                    
                    if(!primeSponsorChanged && !primeSponsorCode.equals(instituteProposalBean.getPrimeSponsorCode())){
                        primeSponsorChanged = true;
                    }
                    
                    if(primeSponsorChanged && !CoeusGuiConstants.EMPTY_STRING.equals(primeSponsorCode)){
                        primeSponsorName= getSponsorName(primeSponsorCode,false);
                    }else if(!CoeusGuiConstants.EMPTY_STRING.equals(primeSponsorCode)){
                        primeSponsorName= instituteProposalBean.getPrimeSponsorName();
                    }
                    
                }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                }
                 //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                instituteProposalDetailsForm.lblPrimeSponsorName.setText(primeSponsorName);
                
            }else if(source.equals(instituteProposalDetailsForm.txtInitialRequestStartDate)) {
                String initialRequestStartDate;
                initialRequestStartDate = instituteProposalDetailsForm.txtInitialRequestStartDate.getText().trim();
                if(!initialRequestStartDate.equals(EMPTY_STRING)) {
                         String strDate1 = dateUtils.formatDate(initialRequestStartDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                         if(strDate1 == null) {
                             strDate1 = dateUtils.restoreDate(initialRequestStartDate, DATE_SEPARATERS);
                             if( strDate1 == null || strDate1.equals(initialRequestStartDate)) {
                                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1056"));
                                 setRequestFocusInThread(instituteProposalDetailsForm.txtInitialRequestStartDate);
                                 return ;
                             }
                         }else {
                             initialRequestStartDate = strDate1;
                             instituteProposalDetailsForm.txtInitialRequestStartDate.setText(initialRequestStartDate);
//                             //added for case #2364 start 6
//                             validateStartDate(initialRequestStartDate, REQUEST_INITIAL_DATE);
//                             //Added for case #2364 end 6
                             
                             
                         }
                     }
               
            }else if(source.equals(instituteProposalDetailsForm.txtInitialRequestEndDate)){
                String initialRequestEndDate;
                initialRequestEndDate = instituteProposalDetailsForm.txtInitialRequestEndDate.getText().trim();
                
               if(!initialRequestEndDate.equals(EMPTY_STRING)) {
                         String strDate1 = dateUtils.formatDate(initialRequestEndDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                         if(strDate1 == null) {
                             strDate1 = dateUtils.restoreDate(initialRequestEndDate, DATE_SEPARATERS);
                             if( strDate1 == null || strDate1.equals(initialRequestEndDate)) {
                                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1057"));
                                 setRequestFocusInThread(instituteProposalDetailsForm.txtInitialRequestEndDate);
                                 return ;
                             }
                         }else {
                             initialRequestEndDate = strDate1;
                             instituteProposalDetailsForm.txtInitialRequestEndDate.setText(initialRequestEndDate);
//                             //added for case #2364 start 7
//                             validateEndDate(initialRequestEndDate, REQUEST_INITIAL_DATE);
//                             //Added for case #2364 end 7
                             
                         }
                     }
            }else if(source.equals(instituteProposalDetailsForm.txtTotalPeriodRequestStartDate)){
                String totalRequestStartDate;
                totalRequestStartDate = instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.getText().trim();
                if(!totalRequestStartDate.equals(EMPTY_STRING)) {
                         String strDate1 = dateUtils.formatDate(totalRequestStartDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                         if(strDate1 == null) {
                             strDate1 = dateUtils.restoreDate(totalRequestStartDate, DATE_SEPARATERS);
                             if( strDate1 == null || strDate1.equals(totalRequestStartDate)) {
                                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1058"));
                                 setRequestFocusInThread(instituteProposalDetailsForm.txtTotalPeriodRequestStartDate);
                                 return ;
                             }
                         }else {
                             totalRequestStartDate = strDate1;
                             instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setText(totalRequestStartDate);
//                             //added for case #2364 start 8
//                             boolean isValid = validateStartDate(totalRequestStartDate, REQUEST_TOTAL_DATE);
//                             if(isValid){
//                                 validateInitialTotalDate();
//                             }
//                             //Added for case #2364 end 8
                         }
                     }
            }else if(source.equals(instituteProposalDetailsForm.txtTotalPeriodRequestEndDate)){
                String totalRequestEndDate;
                totalRequestEndDate = instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText().trim();
                
                if(!totalRequestEndDate.equals(EMPTY_STRING)) {
                     String strDate1 = dateUtils.formatDate(totalRequestEndDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                     if(strDate1 == null) {
                         strDate1 = dateUtils.restoreDate(totalRequestEndDate, DATE_SEPARATERS);
                         if( strDate1 == null || strDate1.equals(totalRequestEndDate)) {
                             CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1059"));
                             setRequestFocusInThread(instituteProposalDetailsForm.txtTotalPeriodRequestEndDate);
                             return ;
                         }
                     }else {
                         totalRequestEndDate = strDate1;
                         instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setText(totalRequestEndDate);
//                         //added for case #2364 start 9
//                         boolean isValid = validateEndDate(totalRequestEndDate, REQUEST_TOTAL_DATE);
//                         if(isValid){
//                             validateInitialTotalDate();
//                         }
//                         //Added for case #2364 end 9
                     }
                }
            }else if(source.equals(instituteProposalDetailsForm.txtInitialTotalDirectCost)){
                double cost = new Double(instituteProposalDetailsForm.txtInitialTotalDirectCost.getValue()).doubleValue();
                cost = cost + new Double(instituteProposalDetailsForm.txtInitialTotalIndirectCost.getValue()).doubleValue();
                instituteProposalDetailsForm.txtInitialTotalAllCost.setValue(cost);
            }else if(source.equals(instituteProposalDetailsForm.txtInitialTotalIndirectCost)){
                double cost = new Double(instituteProposalDetailsForm.txtInitialTotalIndirectCost.getValue()).doubleValue();
                cost = cost + new Double(instituteProposalDetailsForm.txtInitialTotalDirectCost.getValue()).doubleValue();
                instituteProposalDetailsForm.txtInitialTotalAllCost.setValue(cost);
            }else if(source.equals(instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost)){
                double cost = new Double(instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost.getValue()).doubleValue();
                cost = cost + new Double(instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost.getValue()).doubleValue();
                instituteProposalDetailsForm.txtTotalPeriodTotalAllCost.setValue(cost);
            }else if(source.equals(instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost)){
                double cost = new Double(instituteProposalDetailsForm.txtTotalPeriodTotalIndirectCost.getValue()).doubleValue();
                cost = cost + new Double(instituteProposalDetailsForm.txtTotalPeriodTotalDirectCost.getValue()).doubleValue();
                instituteProposalDetailsForm.txtTotalPeriodTotalAllCost.setValue(cost);
            }
        }
    }
    
    /** contacts the server and validates the award number and Account number
     * returns a vector which contains two boolean objects
     * @return coeusVector
     * @param institute proposal bean whci takes account number and award number
     * @throws CoeusException if cannot contact server or if server error occurs.
     */
    private CoeusVector validateFormData(InstituteProposalBean bean) throws CoeusException{
        CoeusVector cvData = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(VALIDATE_BEFORE_SAVE);
        requesterBean.setDataObject(bean);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(connect);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            throw new CoeusException(COULD_NOT_CONTACT_SERVER);
        }else if(!responderBean.isSuccessfulResponse()) {
            cvData = (CoeusVector)responderBean.getDataObject();
        }
        return cvData;
    }
    
    /** contacts the server and fetches the Sponsor name for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor name
     * @param sponsorCode sponsor code for which sponsor name has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */
    //modified for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    //isSponsor returns true if it is sponsor and false if it is for primesponsor
    private  String getSponsorName(String sponsorCode, boolean isSponsor)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_SPONSOR_NAME);
        requesterBean.setDataObject(sponsorCode);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        isSponsorWarningFired = false;
        if(responderBean == null) {
            //Could not contact server.
            throw new CoeusException(COULD_NOT_CONTACT_SERVER);
        }else if(!responderBean.isSuccessfulResponse()) {
            throw new CoeusException(SERVER_ERROR);
        }
        //Got data from server. return sponsor name.
        //sponsor name = EMPTY if not found.
        if(responderBean.getDataObjects() == null || responderBean.getDataObjects().isEmpty()){
            return EMPTY_STRING;
        }
        Vector vecData =  new Vector();
        String sponsorStatus = "";
        sponsorName = "";
        if (responderBean.isSuccessfulResponse()){
            vecData = (Vector)responderBean.getDataObjects();
            if(vecData != null && !vecData.isEmpty()){
                sponsorStatus = (String)vecData.get(1);
                sponsorName = (String)vecData.get(0);
                if((sponsorStatus == null || INACTIVE_STATUS.equals(sponsorStatus) ) && isSponsor){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                    sponsorName = EMPTY_STRING;
                    instituteProposalDetailsForm.txtSponsor.setText(EMPTY_STRING);
                    instituteProposalDetailsForm.lblSponsorName.setText(sponsorName);
                    setRequestFocusInThread(instituteProposalDetailsForm.txtSponsor);
                }else if((sponsorStatus == null || INACTIVE_STATUS.equals(sponsorStatus)) && !isSponsor){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ENTER_VALID_PRIME_SPONSOR));
                    sponsorName = EMPTY_STRING;
                    instituteProposalDetailsForm.txtPrimeSponsor.setText(EMPTY_STRING);
                    instituteProposalDetailsForm.lblPrimeSponsorName.setText(sponsorName);
                    setRequestFocusInThread(instituteProposalDetailsForm.txtPrimeSponsor);
                }
            }
        } else {
            throw new CoeusException(responderBean.getMessage());
        }
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        return sponsorName;
    }
    
    /** added for the Bug Fix:1666 for alpha numeric sponsor code step:3
     * contacts the server and fetches the valid Sponsor code for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor code
     * @param sponsorCode sponsor code for which valid sponsor code has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */
    private  String getValidSponsorCode(String sponsorCode)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_CODE_FOR_VALID_SPONSOR);
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
            throw new CoeusException(SERVER_ERROR);
        }
        //Got data from server. return sponsor name.
        //sponsor name = EMPTY if not found.
        if(responderBean.getDataObject() == null) return EMPTY_STRING;
        String validSponsorCode = responderBean.getDataObject().toString();
        return validSponsorCode;
    }/* Bug Fix:1666 end step:3*/
    
    /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /** Listenes to the bean which is updated in some other screen
     * @param beanEvent
     */    
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getBean().getClass().equals(InstituteProposalBean.class) && 
        !beanEvent.getSource().equals(this)) {
            instituteProposalBean = (InstituteProposalBean)beanEvent.getBean();
            setRefreshRequired(true);
        }
        if(beanEvent.getSource().getClass().equals(InstituteProposalBaseWindowController.class)) {
            ComboBoxBean comboBoxBean = new ComboBoxBean();
            comboBoxBean.setCode(EMPTY_STRING + instituteProposalBean.getStatusCode());
            comboBoxBean.setDescription(instituteProposalBean.getStatusDescription());
            instituteProposalDetailsForm.cmbStatus.setSelectedItem(comboBoxBean);
            instituteProposalDetailsForm.cmbStatus.setEnabled(true);
        }
    }
    
    /**
     * @param actionEvent
     */    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        int sponsorCode = 0;
        int primeSponsorCode = 1;
        
        // Modified for COEUSQA-1471_show institute proposal for merged proposal logs_start
        if(source.equals(instituteProposalDetailsForm.btnSponsorSearch)){
            displaySponsorSearch(sponsorCode);
        }else if(source.equals(instituteProposalDetailsForm.btnPrimeSponsorSearch)){
            displaySponsorSearch(primeSponsorCode);
        }else if(source.equals(instituteProposalDetailsForm.btnAwardNumberSearch)){
            displayAwardSearch();
        }else if(source.equals(instituteProposalDetailsForm.txtSponsor)){
            checkSponsor();
        }else if(source.equals(instituteProposalDetailsForm.txtPrimeSponsor)){
            checkPrimeSponsor();
        }else if(source.equals(instituteProposalDetailsForm.btnMore)){
            displayMergedProposal(instituteProposalBean.getMergedProposalData());
        }
        // Modified for COEUSQA-1471_show institute proposal for merged proposal logs_end
    }
    
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    /**
     * Method to display all merged data
     *@param cvMergedData
     */
    private void displayMergedProposal(CoeusVector cvMergedData){
            
            MergedProposalDetailsForm mergeDetailFrm = new MergedProposalDetailsForm();            
            StringBuffer strBfr = new StringBuffer();
            if(cvMergedData != null && !cvMergedData.isEmpty()){
                for(Object obj : cvMergedData){
                    TempProposalMergeLogBean tempProposalMergeLogBean = (TempProposalMergeLogBean)obj;
                    strBfr.append(tempProposalMergeLogBean.getTempProposalNumber()+"\n");
                }
            }
            mergeDetailFrm.txtArMergedProposals.setText(strBfr.toString());
            mergeDetailFrm.txtArMergedProposals.setCaretPosition(0);
            mergeDetailFrm.txtArMergedProposals.setEditable(false);
            mergeDetailFrm.txtArMergedProposals.setFont(CoeusFontFactory.getNormalFont());
            mergeDetailFrm.btnOk.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dlgMergeDetailsForm.dispose();
                }
            });
            dlgMergeDetailsForm = new CoeusDlgWindow(mdiForm);
            dlgMergeDetailsForm.setSize(570,265);
            dlgMergeDetailsForm.setResizable(false);
            dlgMergeDetailsForm.getContentPane().add(mergeDetailFrm);
            dlgMergeDetailsForm.setTitle("Merged Proposal Details");
            dlgMergeDetailsForm.setFont(CoeusFontFactory.getLabelFont());            
            dlgMergeDetailsForm.setModal(true);            
            dlgMergeDetailsForm.pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgMergeDetailsForm.getSize();
            dlgMergeDetailsForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));
            
            dlgMergeDetailsForm.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    dlgMergeDetailsForm.dispose();
                    return;
                }
            });
            
            dlgMergeDetailsForm.setVisible(true);        
    }
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    
      //for validating the sponsor name when focus is lost
    /** when the user hits ENTER Key, display the sponsor name if it is valid */    
     private  void checkSponsor(){
        String sponsorName;
        sponsorName = instituteProposalDetailsForm.txtSponsor.getText();
        if(sponsorName.trim().equals(EMPTY_STRING)){
            return;
        }else{
            try{
                sponsorName= getSponsorName(instituteProposalDetailsForm.txtSponsor.getText().trim(),true);
                if(sponsorName.trim().equals(EMPTY_STRING)) {
                sponsorName = EMPTY_STRING;
                //instituteProposalDetailsForm.txtSponsor.setText(EMPTY_STRING);
                instituteProposalDetailsForm.lblSponsorName.setText(EMPTY_STRING);
                }else{
                    instituteProposalDetailsForm.lblSponsorName.setText(sponsorName);
                }
             }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                    sponsorName = EMPTY_STRING;
                }
            }
        }
     
     
      //for validating the sponsor name when focus is lost
     /** when the user hits ENTER Key, display the prime sponsor name if it is valid */     
     private  void checkPrimeSponsor(){
        String sponsorName;
        sponsorName = instituteProposalDetailsForm.txtPrimeSponsor.getText();
        if(sponsorName.trim().equals(EMPTY_STRING)){
            return;
        }else{
            try{
                sponsorName= getSponsorName(instituteProposalDetailsForm.txtPrimeSponsor.getText().trim(),false);
                if(sponsorName.trim().equals(EMPTY_STRING)) {
                sponsorName = EMPTY_STRING;
                //instituteProposalDetailsForm.txtPrimeSponsor.setText(EMPTY_STRING);
                instituteProposalDetailsForm.lblPrimeSponsorName.setText(EMPTY_STRING);
                }else{
                    instituteProposalDetailsForm.lblPrimeSponsorName.setText(sponsorName);
                }
             }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                    sponsorName = EMPTY_STRING;
                }
            }
        }
     
    
     /** This method will used to get the valid award number from the search */     
    private void displayAwardSearch() {
        try {
            CoeusSearch coeusSearch = new CoeusSearch(
            CoeusGuiConstants.getMDIForm(), AWARD_SEARCH, 1);
            coeusSearch.showSearchWindow();
            HashMap awardSelected = coeusSearch.getSelectedRow();
            if (awardSelected != null && !awardSelected.isEmpty() ) {
//                String awardNumber = Utils.convertNull(awardSelected.get(
                String awardNumber = edu.mit.coeus.utils.Utils.convertNull(awardSelected.get(
                "MIT_AWARD_NUMBER"));
                instituteProposalDetailsForm.txtAwardNo.setText(awardNumber);
                instituteProposalDetailsForm.txtAwardNo.requestFocusInWindow();
            }
        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog("Award Search is not available.." + exception.getMessage());
        }
    }
    
    /**
     * @param code
     */    
    private void displaySponsorSearch(int code){
        try{
            int click = sponsorSearch();
            if(click == CANCEL_CLICKED) {
                return ;
            }
            switch(code){
                case SPONSOR:
                    instituteProposalDetailsForm.txtSponsor.setText(getSponsorCode());
                    instituteProposalDetailsForm.lblSponsorName.setText(getSponsorName());
                    instituteProposalDetailsForm.txtSponsor.requestFocusInWindow();
                    break;
                case PRIME_SPONSOR:
                    instituteProposalDetailsForm.txtPrimeSponsor.setText(getSponsorCode());
                    instituteProposalDetailsForm.lblPrimeSponsorName.setText(getSponsorName());
                    instituteProposalDetailsForm.txtPrimeSponsor.requestFocusInWindow();
                    break;
            }
            
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    
    
    /** displays sponsor search.
     * returns OK_CLICKED if OK button was Clicked.
     * else returns CANCEL_CLICKED if Cancel button was clicked.
     * @throws Exception if any error occurs.
     * @return OK_CLICKED if OK button was Clicked.
     * else returns CANCEL_CLICKED if Cancel button was clicked.
     */
    public int sponsorSearch()throws Exception {
        //Do Lazy initialization as every subclass of this need not search for Sponsor.
        if(sponsorSearch == null) {
            sponsorSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), SPONSOR_SEARCH, CoeusSearch.TWO_TABS);
        }
        
        sponsorSearch.showSearchWindow();
        HashMap selectedRow = sponsorSearch.getSelectedRow();
        if(selectedRow == null || selectedRow.isEmpty()) {
            return CANCEL_CLICKED;
        }
        sponsorCode = selectedRow.get(SPONSOR_CODE).toString();
        sponsorName = selectedRow.get(SPONSOR_NAME).toString();
        return OK_CLICKED;
        
    }
    
    /** returns searched sponsor code
     * @return searched sponsor code
     */
    public String getSponsorCode() {
        return sponsorCode;
    }
    
    /** returns searched sponsor code
     * @return returns searched sponsor code
     */
    public String getSponsorName() {
        return sponsorName;
    }
    
    /**
     * @param refreshRequired
     */    
    public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    /**
     * @return
     */    
    public boolean isRefreshRequired() {
        boolean retValue;
        
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    
    /** refreshes the GUI controlled by this. */
    public void refresh() {
        try{
            if(isRefreshRequired()) {
                // Get the latest values from the queryEngine and set it to the bean
                CoeusVector dataVector = queryEngine.executeQuery(queryKey, InstituteProposalBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                InstituteProposalBean bean = (InstituteProposalBean)dataVector.get(0);
                setFormData(bean);
                setRefreshRequired(false);
                isMessageDisplay = false;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    /** sets the defauls focus when the window is opened */    
    public void setDefaultFocusForComponent(){
        if( functionType!= DISPLAY_PROPOSAL) {
            instituteProposalDetailsForm.txtAwardNo.requestFocusInWindow();
        }
    }

    /**
     * @param itemEvent
     */    
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
            if (source==instituteProposalDetailsForm.cmbStatus) {
                return;
            }
        }
        if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            if(source ==instituteProposalDetailsForm.cmbStatus){
                if (isMessageDisplay) {
                    isMessageDisplay = false;
                    return;
                }
                ComboBoxBean comboBoxBean = (ComboBoxBean)instituteProposalDetailsForm.cmbStatus.getSelectedItem();
                String code = comboBoxBean.getCode();
                if(code.equals(FUNDED)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CAN_NOT_CHANGE_FUND));
                    instituteProposalDetailsForm.cmbStatus.removeItemListener(this);
                    instituteProposalDetailsForm.cmbStatus.setSelectedItem(lastSelected);
                    instituteProposalDetailsForm.cmbStatus.addItemListener(this);
                }
                lastSelected = instituteProposalDetailsForm.cmbStatus.getSelectedItem();
            }
        }
    }
    
    /** This Inner class provides the Mouse acions and its behaviour while double
     * clicking on the specified components
     */    
    public class CustomMouseAdapetr extends MouseAdapter{
        
        /**
         * @param mouseEvent
         */        
        public void mouseClicked(MouseEvent mouseEvent){
            Object source = mouseEvent.getSource();
            if(source.equals(instituteProposalDetailsForm.txtSponsor)){
                int clickCount = mouseEvent.getClickCount();
                if(clickCount != 2) return ;
                //Double Clicked on Sponsor code validate and display sponsor details.
                String sponsorCode, sponsorName = null;
                sponsorCode = instituteProposalDetailsForm.txtSponsor.getText().trim();
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                if(!sponsorCode.equals(EMPTY_STRING)) {
                    try{
                        if(!sponsorChanged && !sponsorCode.equals(instituteProposalBean.getSponsorCode())){
                            sponsorChanged = true;
                        }
                        if(sponsorChanged && !CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)){
                            sponsorName= getSponsorName(sponsorCode,true);
                        }else if(!CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)){
                            sponsorName= instituteProposalBean.getSponsorName();
                        }
                         //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                        instituteProposalDetailsForm.lblSponsorName.setText(sponsorName);
                    }catch (CoeusException coeusException) {
                        coeusException.printStackTrace();
                    }
                }
                if(sponsorCode.equals(EMPTY_STRING)) {
                    //Sponsor Code not Entered. Do nothing
                    instituteProposalDetailsForm.lblSponsorName.setText(EMPTY_STRING);
                    return ;
                }else if(sponsorName == null || sponsorName.equals(EMPTY_STRING)) {
                    //Wrong Sponsor Code. show Error Message.
                    instituteProposalDetailsForm.lblSponsorName.setText(EMPTY_STRING);
//                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                    return ;
                }
                //valid sponsor code. display sponsor details.
                SponsorMaintenanceForm sponsorForm = new SponsorMaintenanceForm(TypeConstants.DISPLAY_MODE, sponsorCode);
                sponsorForm.showForm(mdiForm, DISPLAY_SPONSOR, true);
            }else if(source.equals(instituteProposalDetailsForm.txtPrimeSponsor)){
                int clickCount = mouseEvent.getClickCount();
                if(clickCount != 2) return ;
                //Double Clicked on Sponsor code validate and display sponsor details.
                String primeSponsorCode, primeSponsorName = null;
                primeSponsorCode = instituteProposalDetailsForm.txtPrimeSponsor.getText().trim();
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                if(!primeSponsorCode.equals(EMPTY_STRING)) {
                    try{
                        if(!primeSponsorChanged && !primeSponsorCode.equals(instituteProposalBean.getPrimeSponsorCode())){
                            primeSponsorChanged = true;
                        }
                        if(primeSponsorChanged && !CoeusGuiConstants.EMPTY_STRING.equals(primeSponsorCode)){
                            primeSponsorName= getSponsorName(primeSponsorCode,false);
                        }else if(!CoeusGuiConstants.EMPTY_STRING.equals(primeSponsorCode)){
                            primeSponsorName= instituteProposalBean.getPrimeSponsorName();
                        }
                         //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                        instituteProposalDetailsForm.lblPrimeSponsorName.setText(primeSponsorName);
                    }catch (CoeusException coeusException) {
                        coeusException.printStackTrace();
                    }
                }
                if(primeSponsorCode.equals(EMPTY_STRING)) {
                    //Sponsor Code not Entered. Do nothing
                    instituteProposalDetailsForm.lblPrimeSponsorName.setText(EMPTY_STRING);
                    return ;
                }else if(primeSponsorName == null || primeSponsorName.equals(EMPTY_STRING)) {
                    //Wrong Sponsor Code. show Error Message.
                    instituteProposalDetailsForm.lblPrimeSponsorName.setText(EMPTY_STRING);
//                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                    return ;
                }
                //valid sponsor code. display sponsor details.
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                SponsorMaintenanceForm sponsorForm = new SponsorMaintenanceForm(TypeConstants.DISPLAY_MODE, primeSponsorCode);
                sponsorForm.showForm(mdiForm, DISPLAY_SPONSOR, true);
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
            }
        }
    }
    //Added for case #2364 start 10
    /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateStartDate(String strDate, String requestDate) {
        
        boolean isValid = true;
        String mesg = null;
        // compare the date
        Date startDate = null;
        Date endDate = null;
        String strStartDate = null;
        String strEndDate = null;
        if(requestDate.equals(REQUEST_INITIAL_DATE)){
            if ( instituteProposalDetailsForm.txtInitialRequestEndDate.getText() !=null
                && instituteProposalDetailsForm.txtInitialRequestEndDate.getText().trim().length() > 0 ) {
                try {
                    strStartDate =  dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    startDate = simpleDateFormat.parse(strStartDate);
                    
                    strEndDate = instituteProposalDetailsForm.txtInitialRequestEndDate.getText().trim();
                    endDate= simpleDateFormat.parse(dateUtils.restoreDate(strEndDate,DATE_SEPARATERS));
                }catch(java.text.ParseException pe){
                    mesg = coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1056");
                }
                if(endDate != null && endDate.compareTo(startDate) < 0 ){
                    //startdate is greater than end date - not ok
                    mesg = coeusMessageResources.parseMessageKey(
                                        "instPropBase_exceptionCode.1060");
                    setRequestFocusInThread(instituteProposalDetailsForm.txtInitialRequestStartDate);
                }//end if
                
            }
        }else if(requestDate.equals(REQUEST_TOTAL_DATE)){
            if ( instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText() !=null
                && instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText().trim().length() > 0 ) {
                try {
                    strStartDate =  dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    startDate = simpleDateFormat.parse(strStartDate);
                    
                    strEndDate = instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText().trim();
                    endDate= simpleDateFormat.parse(dateUtils.restoreDate(strEndDate,DATE_SEPARATERS));
                }catch(java.text.ParseException pe){
                    mesg = coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1056");
                }
                if(endDate != null && endDate.compareTo(startDate) < 0 ){
                    //startdate is greater than end date - not ok
                    mesg = coeusMessageResources.parseMessageKey(
                                        "instPropBase_exceptionCode.1062");
                    setRequestFocusInThread(instituteProposalDetailsForm.txtTotalPeriodRequestStartDate);
                }//end if
                
            }//end if
        }
        
        if (mesg != null){
            isValid = false;
            CoeusOptionPane.showErrorDialog(mesg);
            if(requestDate.equals(REQUEST_TOTAL_DATE)){
                instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.setText(strDate);
            }else{
                instituteProposalDetailsForm.txtInitialRequestStartDate.setText(strDate);
            }//end if
            
        }
        return isValid;
    }
     /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateEndDate(String strDate, String requestDate) {
         boolean isValid = true;
        String mesg = null;
        // compare the date
        Date startDate = null;
        Date endDate = null;
        String strStartDate = null;
        String strEndDate = null;
        if(requestDate.equals(REQUEST_INITIAL_DATE)){
            if ( instituteProposalDetailsForm.txtInitialRequestStartDate.getText() !=null
                && instituteProposalDetailsForm.txtInitialRequestStartDate.getText().trim().length() > 0 ) {
                try {
                    strEndDate =  dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    endDate = simpleDateFormat.parse(strEndDate);
                    
                    strStartDate = instituteProposalDetailsForm.txtInitialRequestStartDate.getText().trim();
                    startDate = simpleDateFormat.parse(dateUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                }catch(java.text.ParseException pe){
                    mesg = coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1058");
                }
                if(startDate != null && startDate.compareTo(endDate) > 0 ){
                    //end is later than start date - not ok
                    mesg = coeusMessageResources.parseMessageKey(
                                        "instPropBase_exceptionCode.1061");
                    setRequestFocusInThread(instituteProposalDetailsForm.txtInitialRequestEndDate);
                }
                
            }
        }else if(requestDate.equals(REQUEST_TOTAL_DATE)){
            if ( instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText() !=null
                && instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText().trim().length() > 0 ) {
                try {
                    strEndDate =  dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    endDate = simpleDateFormat.parse(strEndDate);
                    
                    strStartDate = instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.getText().trim();
                    startDate = simpleDateFormat.parse(dateUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                }catch(java.text.ParseException pe){
                    mesg = coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1056");
                }
                if(startDate != null && startDate.compareTo(endDate) > 0 ){
                    //end is later than start date - not ok
                    mesg = coeusMessageResources.parseMessageKey(
                                        "instPropBase_exceptionCode.1063");
                    setRequestFocusInThread(instituteProposalDetailsForm.txtTotalPeriodRequestEndDate);
                }
                
            }
        }
        
        if (mesg != null){
            isValid = false;
            CoeusOptionPane.showErrorDialog(mesg);
            if(requestDate.equals(REQUEST_TOTAL_DATE)){
                instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.setText(strDate);
            }else{
                instituteProposalDetailsForm.txtInitialRequestEndDate.setText(strDate);
            }
            
        }
        return isValid;
    }
    private boolean validateInitialTotalDate(){
        boolean isValid = true;
        String mesg = null;
        // compare the date
        Date startDate = null;
        Date compareDate = null;
        String strDate = instituteProposalDetailsForm.txtInitialRequestStartDate.getText();
        String strCompareDate = instituteProposalDetailsForm.txtTotalPeriodRequestStartDate.getText();
        if((strDate != null && strDate.trim().length() > 0) 
            && (strCompareDate != null && strCompareDate.trim().length() > 0)){
            try {
                    startDate = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                    compareDate = simpleDateFormat.parse(dateUtils.restoreDate(strCompareDate,DATE_SEPARATERS));
                }catch(java.text.ParseException pe){
                    pe.printStackTrace();
                    return false;
                }
                if(startDate != null && startDate.compareTo(compareDate) < 0 ){
                    mesg = coeusMessageResources.parseMessageKey(
                                        "instPropBase_exceptionCode.1065");
                    setRequestFocusInThread(instituteProposalDetailsForm.txtInitialRequestStartDate);
                }
                if(mesg != null){
                    isValid = false;
                    CoeusOptionPane.showErrorDialog(mesg);
                    return isValid;
                }
        }
        strDate = instituteProposalDetailsForm.txtInitialRequestEndDate.getText();
        strCompareDate = instituteProposalDetailsForm.txtTotalPeriodRequestEndDate.getText();
        if((strDate != null && strDate.trim().length() > 0) 
            && (strCompareDate != null && strCompareDate.trim().length() > 0)){
            try {
                    startDate = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                    compareDate = simpleDateFormat.parse(dateUtils.restoreDate(strCompareDate,DATE_SEPARATERS));
                }catch(java.text.ParseException pe){
                    pe.printStackTrace();
                    return false;
                }
                if(startDate != null && startDate.compareTo(compareDate) > 0 ){
                    mesg = coeusMessageResources.parseMessageKey(
                                        "instPropBase_exceptionCode.1064");
                    setRequestFocusInThread(instituteProposalDetailsForm.txtInitialRequestEndDate);
                   
                }
                if(mesg != null){
                    isValid = false;
                    CoeusOptionPane.showErrorDialog(mesg);
                    return isValid;
                }   
        }
        return isValid;
    }
    //Added for case #2364 end 10
}
