/*
 * InstituteProposalDetailWindowController.java
 *
 * Created on April 26, 2004, 10:25 AM
 */

package edu.mit.coeus.instprop.controller;

// JM 05-01-2013 import Vanderbilt customizations 
import edu.vanderbilt.coeus.instprop.controller.*;
// JM END
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.mail.controller.ActionValidityChecking;
import edu.mit.coeus.mail.controller.MailController;
import edu.mit.coeus.mailaction.bean.MailActionInfoBean;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import javax.swing.*;
import java.util.Hashtable;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.beans.*;
import java.util.Enumeration;
import java.awt.*;
import javax.swing.event.ChangeListener;
import java.util.HashMap;
import java.util.Observer;
import javax.swing.JOptionPane;

import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.instprop.gui.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.instprop.controller.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.search.gui.NewInstituteProposalSearch;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyBean;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.propdev.gui.ProposalNotepadForm;
import edu.mit.coeus.instprop.gui.InvestigaorListForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.negotiation.bean.NegotiationInfoBean;
import edu.mit.coeus.utils.investigator.InvestigatorForm;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvCreditSplitObject;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvestigatorCreditSplitController;
import edu.mit.coeus.utils.investigator.invUnitAdminType.InvestigatorUnitAdminTypeController;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import edu.ucsd.coeus.personalization.controller.AbstractController;

/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author chandru
 */
public class InstituteProposalBaseWindowController extends InstituteProposalController
implements ActionListener, VetoableChangeListener, ChangeListener{
    
    private InstituteProposalBaseWindow instituteProposalBaseWindow;
    private static final char GET_PROPOSAL_DATA = 'A';
    private static final char SAVE_PROPOSAL = 'B';
    private static final char CHECK_PARAMETER = 'C';
    private static final char RELEASE_LOCK = 'O';
    private static final char INST_PROPOSAL_PRINTING = 'I';
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private QueryEngine queryEngine;
    private static final String EMPTY_STRING = "";
    private JTabbedPane tbdPnInstProposal;
    private char functionType;
    private String title = EMPTY_STRING;
    public String queryKeys = EMPTY_STRING;
    public InstituteProposalBean instituteProposalBean ;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private Hashtable htInstituteProposalData;
    private String proposalNumber;
    private char mode;
    private CoeusMessageResources coeusMessageResources;
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private boolean closed = false;
    private int selectedTabIndex;
    private MedusaDetailForm medusaDetailform = null;
    // private CoeusVector validateAwardAccountData;
    private boolean windowOpen = false;
    private boolean isLock = false;
    private boolean proposalModified = false;
    private javax.swing.JTable tblResult;
    private static final String NEXT = "NEXT_PROPOSAL";
    private static final String PREVIOUS = "PREVIOUS_PROPOSAL";
    private static final String FUNCTIONALITY_NOT_IMPLEMENTED = "Functionality not implemented ";
    /** Holds the selected row in the Proposal List table */
    private int selectedRow = -1;
    //Listerner for updating to base window
    private BaseWindowObservable observable  = new BaseWindowObservable();
    public boolean ipRight= false;
    
    private String newProposalNumber = EMPTY_STRING;
    
    private CoeusVector negotiationRightData = null;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** Holds the total number of rows in the proposal List table */
    private int totalProposals = 0;
    
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    public static final String SERVER_ERROR = "Server Error";
    /** Do you want to save changes ? */
    private static final String SAVE_CHANGES = "award_exceptionCode.1004";
    
    private static final String PARAMETER_VALUE = "parameterName";
    private static final String MISSING_SUMMARY_COMMENT = "instPropBase_exceptionCode.1003";
    private static final String MISSING_PROPOSAL_COMMENT= "instPropBase_exceptionCode.1004";
    private static final String MISSING_COST_SHARING = "instPropBase_exceptionCode.1005";
    private static final String MISSING_IDC_COMMENT = "instPropBase_exceptionCode.1006";
    private static final String MISSING_IP_REVIEW_COMMENT = "instPropBase_exceptionCode.1007";
    private static final String MISSING_IP_REVIEWER_COMMENT = "instPropBase_exceptionCode.1008";
    /**
     * Holds Investigator Sequence Numbers from Server so as to modify again when its sent back to server.
     */
    private int invgtrSeqNoFrmSrv;
    private boolean hasNegoModifyRight;
    private boolean hasNegoViewData;
    private boolean negoMode;
    private String unitNumber = EMPTY_STRING;
    
    //specifies the tab and tab index for the inst proposal
    
    private static final String PROPOSAL_DETAIL = "Proposal";
    private static final String MAILING_INFO = "Mailing Info";
    private static final String INVESTIGATOR = "Investigators";
    private static final String COST_SHARING = "Cost Sharing";
    private static final String IDC_RATES = "IDC rates";
    private static final String SPECIAL_REVIEW = "Special Review";
    private static final String SCIENCE_CODE= "Science Codes";
    private static final String IP_REVIEW= "IP Review";
    // JM 5-01-2012 added centers subcontracts tabs
    private static final String CENTERS = "Center Numbers";
    private static final String SUBCONTRACTS = "Subcontracts";
    // JM END
    private static final String IP_CUSTOM="Others";
    // 3823: Key Person Records Needed in Inst Proposal and Award
    private static final String KEY_PERSONS = "Key Person";  
    
    
    private static final int PROPOSAL_DETAIL_TAB_INDEX = 0;
    private static final int MAINLINGINFO_TAB_INDEX = 1;
    private static final int INVESTIGATOR_TAB_INDEX = 2;
    // 3823: Key Person Records Needed in Inst Proposal and Award - Start
//    private static final int COST_SHARING_TAB_INDEX = 3;
//    private static final int IDC_RATES_TAB_INDEX = 4;
//    private static final int SPECIAL_REVIEW_INDEX = 5;
//    private static final int SCIENCE_CODE_TAB_INDEX = 6;
//    private static final int IP_REVIEW_TAB_INDEX = 7;
//    private static final int IP_CUSTOM_TAB_INDEX = 8;
    // JM 8-12-2013 don't want key persons tab; renumbered other tabs
    private static final int KEY_PERSONS_TAB_INDEX = -1;
    private static final int COST_SHARING_TAB_INDEX = 3;
    private static final int IDC_RATES_TAB_INDEX = 4;
    private static final int SPECIAL_REVIEW_INDEX = 5;
    private static final int SCIENCE_CODE_TAB_INDEX = 6;
    private static final int IP_REVIEW_TAB_INDEX = 7;
    // JM END
    // JM 05-01-2013 added centers / subcontracts tabs and incremented others tab
    private static final int SUBCONTRACTS_TAB_INDEX = 8;
    private static final int CENTERS_TAB_INDEX = 9;
    private static final int IP_CUSTOM_TAB_INDEX = 10;
    // JM END
    // 3823: Key Person Records Needed in Inst Proposal and Award - End
    
    
    private boolean valid = true;
    
    private InstituteProposalDetailController instituteProposalDetailController;
    private IDCRatesController iDCRatesController;
    private IPMailingInfoController iPMailingInfoController;
    private IPScienceCodeController iPScienceCodeController;
    private CostSharingController costSharingController;
    private IPReviewController reviewController;
    private IPInvestigatorController investigatorController;
    private SpecialReviewController specialReviewController;
    // JM 05-01-2013 added centers / subcontracts controller
    private ProposalCentersController proposalCentersForm;
    private SubcontractController subcontractController;
    // JM END
    private IPCustomElementsController ipCustomElementsController;
    // 3823: Key Person Records Needed in Inst Proposal and Award
    private IPKeyPersonController ipKeyPersonController;
    //String used in get and putFrame to maintain same number for both methods especially
    // for new mode.
    private String referenceId;
    
    private ChangePassword changePassword;
    private CoeusVector cvCommentParameter = null;
    //Bug Fix for case #1877 start 1
    private String primeSponsorCode="";
    private InstituteProposalDetailsForm detailForm;
    //Bug Fix for case #1877 end 1
    //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
    private int row = 0 ;
    private int column =0;
    private JTable customTable = null;
    private int count = 1;
    private CustomElementsForm customElementsForm;
    //Case :#3149 ? End
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    private static final char GET_ATTACHMENT_RIGHTS = 'a';
    private boolean userCanMaintainAttachment;
    private boolean userCanViewAttachment;
    private static final String CANNOT_OPEN_ATTACHMENT = "instPropAttachment_exceptionCode.1600";
    private static final String SAVE_PROPOSAL_TO_OPEN_ATTACHMENT = "instPropAttachment_exceptionCode.1603";
    //COEUSQA-1525 : End
    
    /** Creates a new instance of InstituteProposalDetailWindowController */
    public InstituteProposalBaseWindowController(String title, char functionType,
    InstituteProposalBaseBean instituteProposalBaseBean) {
        super(instituteProposalBaseBean);
        
        if(instituteProposalBaseBean!= null){
            this.instituteProposalBaseBean = instituteProposalBaseBean;
            //            instituteProposalBean = (InstituteProposalBean)instituteProposalBaseBean;
        }
        // this.title = title;
        this.functionType = functionType;
        //System.out.println("The function type in const is : "+functionType);
        proposalNumber = instituteProposalBaseBean.getProposalNumber();
        this.referenceId = (functionType == NEW_ENTRY_INST_PROPOSAL ? "" :proposalNumber);
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        tbdPnInstProposal = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        setFunctionType(functionType);
        instituteProposalBaseWindow = new InstituteProposalBaseWindow(title,mdiForm);
        instituteProposalBaseWindow.addVetoableChangeListener(this);
        // JM 4-11-2012 allow mouse wheel to scroll JScrollPane
        instituteProposalBaseWindow.addMouseWheelListener( new MouseWheelListener() {
            public void mouseWheelMoved( MouseWheelEvent e ) {
            }
        });
    }
    
    private void showProposalLog() {
        try{
            NewInstituteProposalSearch proposalLogSearch =
            new NewInstituteProposalSearch(mdiForm, "PROPOSALLOGSEARCH" ,
            CoeusSearch.TWO_TABS);
            proposalLogSearch.showSearchWindow();
            HashMap selectedRow = proposalLogSearch.getSelectedRow();
            if(selectedRow == null || selectedRow.isEmpty()) {
                windowOpen = true;
                return ;
            }
            String logProposalNumber = selectedRow.get("PROPOSAL_NUMBER").toString();
            unitNumber =  selectedRow.get("LEAD_UNIT").toString();
            
            proposalNumber = logProposalNumber;
            if(proposalNumber.equals(EMPTY_STRING)) return ;
            setProposalNumber(proposalNumber);
            
        }catch (Exception exception ){
            exception.printStackTrace();
        }
    }
    
    private void initComponents(){
        instituteProposalBaseWindow.setFunctionType(getFunctionType());
        Container proposalBaseContainer = instituteProposalBaseWindow.getContentPane();
        
        
        //If in display Mode disable Save
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            instituteProposalBaseWindow.btnSave.setEnabled(false);
            instituteProposalBaseWindow.mnuItmSave.setEnabled(false);
        }else {
            instituteProposalBaseWindow.btnPrevious.setEnabled(false);
            instituteProposalBaseWindow.btnNext.setEnabled(false);
            instituteProposalBaseWindow.mnuItmPrevious.setEnabled(false);
            instituteProposalBaseWindow.mnuItmNext.setEnabled(false);
        }
        
        //        if(functionType!= NEW_INST_PROPOSAL){
        //            enableDisableNegotiation();
        //        }
        
        JPanel basePanel = new JPanel();
        
        basePanel.setLayout(new BorderLayout());
        
        if(instituteProposalDetailController == null){
            instituteProposalDetailController =
            new InstituteProposalDetailController(instituteProposalBaseBean,getFunctionType());
        }
        //Bug Fix for case #1877 start 2
        detailForm = (InstituteProposalDetailsForm)instituteProposalDetailController.getControlledUI();
        if(detailForm.txtPrimeSponsor.getText()!=null){
            primeSponsorCode=detailForm.txtPrimeSponsor.getText().trim();
        }
        //Bug Fix for case #1877 end 2
        iDCRatesController = new IDCRatesController(instituteProposalBaseBean, getFunctionType());
        
        iPMailingInfoController = new IPMailingInfoController(instituteProposalBaseBean,getFunctionType());
        
        iPScienceCodeController = new IPScienceCodeController(instituteProposalBaseBean,getFunctionType());
        costSharingController = new CostSharingController(instituteProposalBaseBean, getFunctionType());
        
        reviewController = new IPReviewController(instituteProposalBaseBean, getFunctionType());
        reviewController.setCheckRight(isIpRight());
        
        if(functionType != NEW_INST_PROPOSAL){
            investigatorController = new IPInvestigatorController(instituteProposalBaseBean, queryKeys, getFunctionType());
        }else{
            investigatorController = new IPInvestigatorController(instituteProposalBaseBean, EMPTY_STRING, getFunctionType());
        }
        
        specialReviewController = new SpecialReviewController(instituteProposalBaseBean, getFunctionType(), cvCommentParameter);
        // 3823: Key Person Records Needed in Inst Proposal and Award
        ipKeyPersonController =  new IPKeyPersonController(instituteProposalBaseBean, queryKeys, getFunctionType());

        // JM 05-01-2013 added for centers / subcontracts tabs
        proposalCentersForm =  new ProposalCentersController(instituteProposalBaseBean);
        subcontractController =  new SubcontractController(instituteProposalBaseBean,getFunctionType());
        //JM END
        
         //Case :#4176 ? non-uniform Background color in tabs - Start
        FlowLayout layout       = new FlowLayout(FlowLayout.LEFT);
       
        JPanel pnlDetail        = new JPanel(layout);
        pnlDetail.add(instituteProposalDetailController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrPnDetail = new JScrollPane(pnlDetail);
        jscrPnDetail.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnDetail.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(PROPOSAL_DETAIL,jscrPnDetail);  
     
        JPanel pnlMailingInfo  = new JPanel(layout);
        pnlMailingInfo.add(iPMailingInfoController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrPnMail = new JScrollPane(pnlMailingInfo);
        jscrPnMail.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnMail.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(MAILING_INFO,jscrPnMail);  
        
        JPanel pnlInvestigator  = new JPanel(new BorderLayout());
        pnlInvestigator.add(investigatorController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrPnInv = new JScrollPane(pnlInvestigator);
        jscrPnInv.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnInv.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(INVESTIGATOR,jscrPnInv);  
        
        // 3823: Key Person Records Needed in Inst Proposal and Award - Start
        JPanel pnlKeyPerson = new JPanel(layout);
        pnlKeyPerson.add(ipKeyPersonController.getControlledUI());
        JScrollPane jscrPnKeyPerson = new JScrollPane(pnlKeyPerson);
        //JM 5-25-2011 Do not want Key Person tab per 4.4.2
//JM        tbdPnInstProposal.addTab(KEY_PERSONS, jscrPnKeyPerson);
        // 3823: Key Person Records Needed in Inst Proposal and Award - End
        
        JPanel pnlCostSharing   = new JPanel(layout);
        pnlCostSharing.add(costSharingController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrPnCostShare = new JScrollPane(pnlCostSharing);
        jscrPnCostShare.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnCostShare.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(COST_SHARING,jscrPnCostShare);  
        
        JPanel pnlIDCRates      = new JPanel(layout);
        pnlIDCRates.add(iDCRatesController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrPnIDCRate = new JScrollPane(pnlIDCRates);
        jscrPnIDCRate.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnIDCRate.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(IDC_RATES,jscrPnIDCRate);  
        
        JPanel pnlSplRvw = new JPanel(layout);
        pnlSplRvw.add(specialReviewController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrPnSplRvw = new JScrollPane(pnlSplRvw);
        jscrPnSplRvw.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnSplRvw.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(SPECIAL_REVIEW,jscrPnSplRvw);  
        
        JPanel pnlScienceCode = new JPanel(layout);
        pnlScienceCode.add(iPScienceCodeController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrIPScienceCode = new JScrollPane(pnlScienceCode);
        jscrIPScienceCode.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrIPScienceCode.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(SCIENCE_CODE,jscrIPScienceCode);  
        
        JPanel pnlIPreview      = new JPanel(layout);
        pnlIPreview.add(reviewController.getControlledUI());
        // JM 4-11-2012 add listener to pass control to outer pane for scrolling
        final JScrollPane jscrPnIPRvw = new JScrollPane(pnlIPreview);
        jscrPnIPRvw.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnIPRvw.getParent().dispatchEvent(e);
            }
        });
        // JM END
        tbdPnInstProposal.addTab(IP_REVIEW,jscrPnIPRvw);  
 
        // JM subcontracts
        JPanel pnlSubcontracts = new JPanel(layout);
        //GridLayout gl = new GridLayout(0,1);
        pnlSubcontracts.add(subcontractController.getControlledUI());
        final JScrollPane jscrPnSubcontracts = new JScrollPane(pnlSubcontracts);
//        final JScrollPane jscrPnSubcontracts = (JScrollPane) subcontractController.getControlledUI();
        jscrPnSubcontracts.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnSubcontracts.getParent().dispatchEvent(e);
            }
        });
        tbdPnInstProposal.addTab(SUBCONTRACTS,jscrPnSubcontracts);  
        // JM END
        
        // JM 4-12-2012 added for centers tab
        JPanel pnlIPCenters = new JPanel(layout);
        pnlIPCenters.add(proposalCentersForm.getControlledUI());
        final JScrollPane jscrPnIPCenters = new JScrollPane(pnlIPCenters);
        jscrPnIPCenters.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPnIPCenters.getParent().dispatchEvent(e);
            }
        });
        tbdPnInstProposal.addTab(CENTERS,jscrPnIPCenters);  
        // JM END
 
        if( functionType != NEW_INST_PROPOSAL ) {
            try{
                CoeusVector cvCustomData = queryEngine.executeQuery(queryKey,InstituteProposalCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                if( cvCustomData != null && cvCustomData.size() > 0 ) {
                    ipCustomElementsController = new IPCustomElementsController(instituteProposalBaseBean, getFunctionType());
                    //                    tbdPnInstProposal.setComponentAt(IP_CUSTOM_TAB_INDEX, ipCustomElementsController.getControlledUI());
                    JPanel pnlIPCustom      = new JPanel(layout);
                    pnlIPCustom.add(ipCustomElementsController.getControlledUI());
                    // JM 4-11-2012 add listener to pass control to outer pane for scrolling
                    final JScrollPane jscrPnIPCustom = new JScrollPane(pnlIPCustom);
                    jscrPnIPCustom.addMouseWheelListener(new MouseWheelListener() {
                        public void mouseWheelMoved(MouseWheelEvent e) {
                        	jscrPnIPCustom.getParent().dispatchEvent(e);
                        }
                    });
                    // JM END
                    tbdPnInstProposal.addTab(IP_CUSTOM,jscrPnIPCustom);  
                     //Case :#4176 - End

                    ipCustomElementsController.setFormData(instituteProposalBaseBean);
                }
            }catch ( CoeusException ce ){
                ce.printStackTrace();
            }
        }

        basePanel.add(tbdPnInstProposal);
        JScrollPane jScrollPane = new JScrollPane(basePanel);
        proposalBaseContainer.add(jScrollPane);
        
        if(functionType==NEW_ENTRY_INST_PROPOSAL){
            if(instituteProposalBean.getStatusCode()== 2){
                instituteProposalBaseWindow.mnuItmUnlockProposal.setEnabled(true);
            }
        }
    }
    
    public String[] displayProposal() throws CoeusException{
        try{
            /** If the proposal is opened in new mode, don't get data from the
             *the server
             */
            
            //CoeusVector cvCommentParameter = null;
            if(functionType == NEW_INST_PROPOSAL){
                cvCommentParameter = getParameterData();
                
                if(cvCommentParameter!= null && cvCommentParameter.size() >= 6){
                    initComponents();
                    mdiForm.putFrame(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW, referenceId, NEW_INST_PROPOSAL, instituteProposalBaseWindow);
                    mdiForm.getDeskTopPane().add(instituteProposalBaseWindow);
                    instituteProposalBaseWindow.setSelected(true);
                    instituteProposalBaseWindow.setVisible(true);
                    registerComponents();
                    showProposalLog();
                    if(!windowOpen){
                        Hashtable htData = getProposalData();
                        allFormData(htData);
                        instituteProposalDetailController.setDefaultFocusForComponent();
                    }else{
                        //closeProposal();
                        instituteProposalBaseWindow.doDefaultCloseAction();
                    }
                }else{
                    showProposalLog();
                    if(!windowOpen){
                        //Messages
                        CoeusVector cvFiltered = null;
                        cvFiltered = cvCommentParameter.filter(new Equals(PARAMETER_VALUE, CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE));
                        if(cvFiltered==null || cvFiltered.size()==0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_SUMMARY_COMMENT));
                        }
                        
                        cvFiltered = cvCommentParameter.filter(new Equals(PARAMETER_VALUE, CoeusConstants.PROPOSAL_COMMENT_CODE));
                        if(cvFiltered==null || cvFiltered.size()==0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_PROPOSAL_COMMENT));
                        }
                        
                        cvFiltered = cvCommentParameter.filter(new Equals(PARAMETER_VALUE, CoeusConstants.COST_SHARING_COMMENT_CODE));
                        if(cvFiltered==null || cvFiltered.size()==0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_COST_SHARING));
                        }
                        
                        cvFiltered = cvCommentParameter.filter(new Equals(PARAMETER_VALUE, CoeusConstants.INDIRECT_COST_COMMENT_CODE));
                        if(cvFiltered==null || cvFiltered.size()==0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_IDC_COMMENT));
                        }
                        
                        cvFiltered = cvCommentParameter.filter(new Equals(PARAMETER_VALUE, CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE));
                        if(cvFiltered==null || cvFiltered.size()==0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_IP_REVIEW_COMMENT));
                        }
                        
                        cvFiltered = cvCommentParameter.filter(new Equals(PARAMETER_VALUE, CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE));
                        if(cvFiltered==null || cvFiltered.size()==0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_IP_REVIEWER_COMMENT));
                        }
                        initComponents();
                        mdiForm.putFrame(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW, referenceId, NEW_INST_PROPOSAL, instituteProposalBaseWindow);
                        mdiForm.getDeskTopPane().add(instituteProposalBaseWindow);
                        instituteProposalBaseWindow.setSelected(true);
                        instituteProposalBaseWindow.setVisible(true);
                        registerComponents();
                        Hashtable htData = getProposalData();
                        allFormData(htData);
                        instituteProposalDetailController.setDefaultFocusForComponent();
                    }else{
                        //closeProposal();
                        instituteProposalBaseWindow.doDefaultCloseAction();
                    }
                		// rdias UCSD - Coeus personalization impl
            				customizeProposal();
            				// rdias UCSD                                          
                }
            }else{
                setFormData(instituteProposalBaseBean);
                if(htInstituteProposalData!= null){
                    initComponents();
                    mdiForm.putFrame(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW, referenceId, functionType, instituteProposalBaseWindow);
                    mdiForm.getDeskTopPane().add(instituteProposalBaseWindow);
                    instituteProposalBaseWindow.setSelected(true);
                    instituteProposalBaseWindow.setVisible(true);
                    registerComponents();
                    instituteProposalDetailController.setDefaultFocusForComponent();
                }
            }
             // rdias UCSD - Coeus personalization impl
            if(htInstituteProposalData!= null) {
            	customizeProposal();
            }
            // rdias UCSD
        }catch (PropertyVetoException propertyVetoException){
            propertyVetoException.printStackTrace();
        }
        String propAndUnitNumber[] = {proposalNumber,unitNumber};
        return propAndUnitNumber;
    }
    
    private CoeusVector getParameterData(){
        CoeusVector cvParameterData = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(CHECK_PARAMETER);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            cvParameterData = (CoeusVector)responderBean.getDataObject();
        }else{
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
        }
        return cvParameterData;
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return instituteProposalBaseWindow;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        //        instituteProposalBaseWindow.addVetoableChangeListener(this);
        instituteProposalBaseWindow.btnSave.addActionListener(this);
        instituteProposalBaseWindow.mnuItmSave.addActionListener(this);
        
        instituteProposalBaseWindow.mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        
        instituteProposalBaseWindow.btnClose.addActionListener(this);
        instituteProposalBaseWindow.mnuItmClose.addActionListener(this);
        instituteProposalBaseWindow.mnuItmUnlockProposal.addActionListener(this);
        instituteProposalBaseWindow.btnMedusa.addActionListener(this);
        instituteProposalBaseWindow.mnuItmMedusa.addActionListener(this);
        instituteProposalBaseWindow.btnNotepad.addActionListener(this);
        instituteProposalBaseWindow.mnuItmNotepad.addActionListener(this);
        //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
        instituteProposalBaseWindow.btnAttachments.addActionListener(this);
        instituteProposalBaseWindow.mnuItmAttachments.addActionListener(this);
        //COEUSQA-1525 : End
        instituteProposalBaseWindow.mnuItmPrevious.addActionListener(this);
        instituteProposalBaseWindow.btnPrevious.addActionListener(this);
        instituteProposalBaseWindow.mnuItmNext.addActionListener(this);
        instituteProposalBaseWindow.btnNext.addActionListener(this);
        instituteProposalBaseWindow.mnuItmExit.addActionListener(this);
        
        //Added for Case#3682 - Enhancements related to Delegations - Start
        instituteProposalBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        instituteProposalBaseWindow.mnuItmPreferences.addActionListener(this);
        instituteProposalBaseWindow.mnuItmPrintProposalNotice.addActionListener(this);
        instituteProposalBaseWindow.btnPrint.addActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        //instituteProposalBaseWindow.mnuItmPrintSetup.addActionListener(this);
        
        instituteProposalBaseWindow.mnuItmCurrentPendingreport.addActionListener(this);
        instituteProposalBaseWindow.mnuItmChangePassword.addActionListener(this);
        instituteProposalBaseWindow.mnuItmNegotiation.addActionListener(this);
        //Case 2110 Start
        instituteProposalBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        //Added for Case#2214 email enhancement
        instituteProposalBaseWindow.btnSendEmail.addActionListener(this);
        
        
        tbdPnInstProposal.addChangeListener(this);
        //        if(getFunctionType()!= DISPLAY_PROPOSAL) {
        //            //addBeanUpdatedListener(this, InstituteProposalBean.class);
        //            //tbdPnInstProposal.addChangeListener(this);
        //        }
        
        //Case 2106 1
        ((InvestigatorForm)investigatorController.getControlledUI()).btnCreditSplit.addActionListener(this);
        //Case 2106 1
        //Added for case#2136 enhancement start 1
        ((InvestigatorForm)investigatorController.getControlledUI()).btnAdminType.addActionListener(this);
        //Added for case#2136 enhancement end 1
    }
    
    public void saveFormData() {
        //Save Form Data of all Tab Pages.
        iDCRatesController.saveFormData();
        instituteProposalDetailController.saveFormData();
        iPScienceCodeController.saveFormData();
        iPMailingInfoController.saveFormData();
        costSharingController.saveFormData();
        reviewController.saveFormData();
        investigatorController.saveFormData();
        specialReviewController.saveFormData();
        // 3823: Key Person Records Needed in Inst Proposal and Award
        ipKeyPersonController.saveFormData();
        
        // JM 5-3-2013 save subcontracts data though I don't think this ever gets called here
        subcontractController.saveFormData();
        // JM END
    }
    
    public void setFormData(Object instituteProposalBaseBean) throws CoeusException {
        CoeusVector cvData = null;
        this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean;
        htInstituteProposalData = new Hashtable();
        htInstituteProposalData = getProposalData();
        if(htInstituteProposalData!= null){
            cvData = (CoeusVector)htInstituteProposalData.get(InstituteProposalBean.class);
            this.instituteProposalBean = (InstituteProposalBean)cvData.elementAt(0);
        }
        enableDisableNegotiation();
    }
    
    private Hashtable getProposalData() throws CoeusException {
        // To get the return values for the insti proposal
        Hashtable htProposalData = null;
        // To insert the bean and send to the server.
        Hashtable htData = new Hashtable();
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_PROPOSAL_DATA);
        InstituteProposalBean bean = new InstituteProposalBean();
        bean.setProposalNumber(proposalNumber);
        //System.out.println("The Function Type is :"+functionType);
        bean.setMode(functionType);
        htData.put(InstituteProposalBean.class, bean);
        requesterBean.setDataObject(htData);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            htProposalData = (Hashtable)responderBean.getDataObject();
            CoeusVector cvData = (CoeusVector)htProposalData.get(InstituteProposalBean.class);
            //prepare query key
            instituteProposalBean = (InstituteProposalBean)cvData.elementAt(0);
            this.instituteProposalBean = (InstituteProposalBean)instituteProposalBean;
            this.instituteProposalBaseBean = (InstituteProposalBaseBean )instituteProposalBean;
            queryKeys = this.instituteProposalBaseBean.getProposalNumber() + this.instituteProposalBaseBean.getSequenceNumber();
            
            prepareQueryKey(this.instituteProposalBaseBean);
            //Set Sequence Number for Inst Prop Custom Data Beans - start
            CoeusVector cvInstCustomData = (CoeusVector)htProposalData.get(InstituteProposalCustomDataBean.class);
            if(cvInstCustomData!=null && cvInstCustomData.size() > 0){
                InstituteProposalCustomDataBean instituteProposalCustomDataBean = null;
                for(int custRow = 0; custRow < cvInstCustomData.size(); custRow++){
                    instituteProposalCustomDataBean = (InstituteProposalCustomDataBean)cvInstCustomData.elementAt(custRow);
                    if(instituteProposalCustomDataBean.getAcType()!=null && instituteProposalCustomDataBean.getAcType().equalsIgnoreCase("I")){
                        instituteProposalCustomDataBean.setSequenceNumber(instituteProposalBaseBean.getSequenceNumber());
                    }
                }
            }
            
            htProposalData.put(InstituteProposalCustomDataBean.class, cvInstCustomData);
            //Set Sequence Number for Inst Prop Custom Data Beans - end
            
            // Add the server data to the queryEngine
            queryEngine.addDataCollection(queryKey, htProposalData);
            //Since Investigators encapsulates Units managing A/C types has to be
            //done explicitly. so set all Units beans to Query engine. and then update
            //before sending to server(Save Award).
            extractInvestigatorUnits();
            
            //Set title.
            title = title + this.instituteProposalBaseBean.getProposalNumber() +"   Sequence  "+this.instituteProposalBaseBean.getSequenceNumber();
            if(getFunctionType()!= NEW_INST_PROPOSAL){
                instituteProposalBaseWindow.setTitle(instituteProposalBaseWindow.getTitle() + title);
            }
            //rdias - UCSD's coeus personalization - Begin
//            AbstractController persnref = AbstractController.getPersonalizationControllerRef();
//            persnref.setFormSecurity(responderBean.getPersnXMLObject());
//            if(functionType != TypeConstants.DISPLAY_MODE && persnref != null &&
//            		instituteProposalBean != null) {
//            	boolean allowaccess = persnref.formSecurity("proposal",instituteProposalBean.getProposalNumber());
//            	if (!allowaccess) {
//                	CoeusOptionPane.showErrorDialog("Coeus Proposal Form Security: Edit disabled");
//                	releaseLock(instituteProposalBean.getProposalNumber());
//                	return null;
//                }
//            }        
            //rdias - UCSD's coeus personalization - End        
            
        }else{
            // 2930: Auto-delete Current Locks based on new parameter - Start
//            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
//            /*
//             * Bugfixing for Case#2748 Start
//             * Commented two lines to avoid the unknown error which is displaying 
//             * when trying to access a Institute proposal which is locked bu a user
//             */
////            instituteProposalBaseWindow.doDefaultCloseAction();
////            throw new CoeusException();
//            //Bugfixing for Case#2748 End
//            // return null;            
            throw new CoeusException(responderBean.getMessage());
            // 2930: Auto-delete Current Locks based on new parameter - End
        }
        return htProposalData;
    }
    
    
    /**
     * extracts all investigator units into a vector and stores it in Query Engine.
     */
    private void extractInvestigatorUnits() {
        CoeusVector cvInvestigators, cvInvestigatorUnits, cvTempUnits;
        try{
            cvInvestigators = queryEngine.getDetails(queryKey, InstituteProposalInvestigatorBean.class);
            if(cvInvestigators == null || cvInvestigators.size() == 0) return ;
            
            int size = cvInvestigators.size();
            cvInvestigatorUnits = new CoeusVector();
            InstituteProposalInvestigatorBean instituteProposalInvestigatorBean;
            instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)cvInvestigators.get(0);
            
            for(int index = 0; index < size; index++) {
                instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)cvInvestigators.get(index);
                cvTempUnits = instituteProposalInvestigatorBean.getInvestigatorUnits();
                if(cvTempUnits != null && cvTempUnits.size() > 0) {
                    cvInvestigatorUnits.addAll(cvTempUnits);
                }
            }
            queryEngine.addCollection(queryKey, InstituteProposalUnitBean.class, cvInvestigatorUnits);
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    
    /**
     * Integrate all investigator units into a vector and stores it in Query Engine.
     */
    private void integrateInvestigatorUnits(Hashtable htProposalData) {
        CoeusVector cvInvestigators, cvUnits, cvTempUnits;
        cvInvestigators = (CoeusVector)htProposalData.get(InstituteProposalInvestigatorBean.class);
        cvUnits = (CoeusVector)htProposalData.get(InstituteProposalUnitBean.class);
        
        if(cvInvestigators == null || cvInvestigators.size() == 0) return ;
        int size = cvInvestigators.size();
        InstituteProposalInvestigatorBean investigatorBean;
        InstituteProposalUnitBean unitBean;
        for(int index = 0; index < size; index++) {
            investigatorBean = (InstituteProposalInvestigatorBean)cvInvestigators.get(index);
            Equals eqPersonId = new Equals("personId", investigatorBean.getPersonId());
            cvTempUnits = cvUnits.filter(eqPersonId);
            investigatorBean.setInvestigatorUnits(cvTempUnits);
        }
    }
    
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        //Validate All Tab Pages
        //boolean valid = false;
        if(instituteProposalDetailController.validate() == false){
            tbdPnInstProposal.setSelectedIndex(PROPOSAL_DETAIL_TAB_INDEX);
            valid = false;
        }else if(iPMailingInfoController.validate()== false){
            tbdPnInstProposal.setSelectedIndex(MAINLINGINFO_TAB_INDEX);
            valid = false;
        }else if(investigatorController.validate() == false){
            tbdPnInstProposal.setSelectedIndex(INVESTIGATOR_TAB_INDEX);
            valid = false;
            // 3823: Key Person Records Needed in Inst Proposal and Award - Start
        }else if(ipKeyPersonController.validate() == false){
            tbdPnInstProposal.setSelectedIndex(KEY_PERSONS_TAB_INDEX);
            valid = false;
            // 3823: Key Person Records Needed in Inst Proposal and Award - End
        }else if(costSharingController.validate() == false) {
            tbdPnInstProposal.setSelectedIndex(COST_SHARING_TAB_INDEX);
            valid = false;
        }else if(iDCRatesController.validate() == false) {
            tbdPnInstProposal.setSelectedIndex(IDC_RATES_TAB_INDEX);
            tbdPnInstProposal.getSelectedComponent().requestFocus();
            valid = false;
        }else if(specialReviewController.validate() == false){
            tbdPnInstProposal.setSelectedIndex(SPECIAL_REVIEW_INDEX);
            valid = false;
        } else if(reviewController.validate() == false){
            tbdPnInstProposal.setSelectedIndex(IP_REVIEW_TAB_INDEX);
            valid = false;
        // JM 5-14-2013 for subcontracts
	    } else if(subcontractController.validate() == false){
	        tbdPnInstProposal.setSelectedIndex(SUBCONTRACTS_TAB_INDEX);
	        valid = false;
        }
        else {
            if( ipCustomElementsController != null ) {
                ipCustomElementsController.validate();
                //tbdPnInstProposal.setSelectedIndex(IP_CUSTOM_TAB_INDEX);
            }
            valid = true;
        }
        return valid;
    }
    
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
        if(source.equals(instituteProposalBaseWindow.btnSave) ||
        source.equals(instituteProposalBaseWindow.mnuItmSave)){
            //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
            if(getFunctionType() != DISPLAY_PROPOSAL && ipCustomElementsController != null){
                ipCustomElementsController.getCustomElementsForm().setSaveAction(true);
            }
            //Case :#3149 - End
            try{
                if(isSaveRequired()){
                    if(getFunctionType()== CORRECT_INST_PROPOSAL){
                        instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, instituteProposalBean);
                        subcontractController.saveFormData(); // JM 05-10-2013 call to save subcontracts
                    }
                    //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
                    if(ipCustomElementsController != null && getFunctionType() != DISPLAY_PROPOSAL){
                        customElementsForm = ipCustomElementsController.getCustomElementsForm();
                        customElementsForm.setSaveAction(true);
                        customTable =customElementsForm.getTable();
                        row = customElementsForm.getRow();
                        column = customElementsForm.getColumn();
                        count = 0;
                    }
                    //Case :#3149 ? End
                    saveProposal(false);
                    //Bug Fix for case #1877 start 3
                    primeSponsorCode=detailForm.txtPrimeSponsor.getText().trim();
                    //Bug Fix for case #1877 end 3
                  
                    
                }
            }catch (CoeusUIException coeusUIException) {
                if(!coeusUIException.getMessage().equals("null.")) {
                    CoeusOptionPane.showDialog(coeusUIException);
                    tbdPnInstProposal.setSelectedIndex(coeusUIException.getTabIndex());
                }
                //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
                if(ipCustomElementsController != null && getFunctionType() != DISPLAY_PROPOSAL){
                    customElementsForm = ipCustomElementsController.getCustomElementsForm();
                    customTable = customElementsForm.getTable();
                    row = customTable.getSelectedColumn();
                    column = customTable.getSelectedColumn();
                    if(customElementsForm.getOtherTabMandatory()){
                        boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                        column = customElementsForm.getmandatoryColumn();
                        row = customElementsForm.getmandatoryRow();
                        count = 0;
                        if(lookUpAvailable[row]){
                            customTable.editCellAt(row,column+1);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column+1,column+1);
                            
                        }else if(customTable != null) {
                            customTable.editCellAt(row,column);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column,column);
                        }
                        customTable.setEnabled(true);
                    }
                }
                //Case :#3149 ? End
            }catch (Exception exception) {
                exception.printStackTrace();
            }
            //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
                if(ipCustomElementsController != null){
                customElementsForm = ipCustomElementsController.getCustomElementsForm();
                if(tbdPnInstProposal.getSelectedIndex() == IP_CUSTOM_TAB_INDEX && !customElementsForm.getOtherTabMandatory() && getFunctionType() != DISPLAY_PROPOSAL){
                    customElementsForm.setSaveAction(true);
                    if(ipCustomElementsController != null){
                        boolean[] lookUpAvailable =ipCustomElementsController.getCustomElementsForm().getLookUpAvailable();
                        customElementsForm = ipCustomElementsController.getCustomElementsForm();
                        customTable = ipCustomElementsController.getCustomElementsForm().getTable();
                        
                        row = customElementsForm.getRow();
                        column = customElementsForm.getColumn();
                        count = 0;
                        if(row != -1 && column != -1){
                            if(lookUpAvailable[row]){
                                if(column == 1){
                                    column++;
                                }
                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);
                            }else{
                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);
                            }
                        }
                        customTable.setEnabled(true);
                    }
                }
                }
            //Case :#3149 ? End
        }else if(source.equals(instituteProposalBaseWindow.btnClose) ||
        source.equals(instituteProposalBaseWindow.mnuItmClose)){
            try{
                instituteProposalBaseWindow.doDefaultCloseAction();
                //                   closeProposal();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }else if(source.equals(instituteProposalBaseWindow.btnNext) ||
        source.equals(instituteProposalBaseWindow.mnuItmNext)){
            showProposalInDisplay(NEXT);
        }else if(source.equals(instituteProposalBaseWindow.btnPrevious) ||
        source.equals(instituteProposalBaseWindow.mnuItmPrevious)){
            showProposalInDisplay(PREVIOUS);
        }else if(source.equals(instituteProposalBaseWindow.mnuItmUnlockProposal)){
            unLockProposal();
        }else if(source.equals(instituteProposalBaseWindow.btnMedusa)||
        source.equals(instituteProposalBaseWindow.mnuItmMedusa)){
            showMedusa();
        }else if(source.equals(instituteProposalBaseWindow.btnNotepad)||
        source.equals(instituteProposalBaseWindow.mnuItmNotepad)){
            showNotepad();
        }else if(source.equals(instituteProposalBaseWindow.mnuItmExit)){
            exitApplication();
        }else if(source.equals(instituteProposalBaseWindow.mnuItmCurrentPendingreport)){
            try{
                saveFormData();
                showCurrentAndPendingDetails();
            }catch (CoeusException coeusException){
                CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            }
        //Added for Case#3682 - Enhancements related to Delegations - Start
         }else if(source.equals(instituteProposalBaseWindow.mnuItmDelegations)) {
            displayUserDelegation();
        //Added for Case#3682 - Enhancements related to Delegations - End
        }else if(source.equals(instituteProposalBaseWindow.mnuItmPreferences)) {
            showPreference();
        }else if(source.equals(instituteProposalBaseWindow.mnuItmNegotiation)){
            showNegotiation();
        }else if(source.equals(instituteProposalBaseWindow.mnuItmChangePassword)){
            showChangePassword();
        }else if(source.equals(instituteProposalBaseWindow.btnPrint) ||
        source.equals(instituteProposalBaseWindow.mnuItmPrintProposalNotice)){
            try{
                if( functionType!= DISPLAY_PROPOSAL && isSaveRequired()){
                    //                    getFunctionType()!= TypeConstants.DISPLAY_MODE
                    //case 2061 start
                    if(getFunctionType()== CORRECT_INST_PROPOSAL){
                        instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, instituteProposalBean);
                    }
                    //case 2061 end 
                    saveProposal(false);
                }
                printProposalNotice();
            }catch(Exception e){
                //                if(!e.getMessage().equals("null.")) {
                //                    CoeusOptionPane.showErrorDialog(e.getMessage());
                //                    return;
                //                }
                e.printStackTrace();
//                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }//Case 2110 Start
        else if(source.equals(instituteProposalBaseWindow.mnuItmCurrentLocks)){
            showLocksForm();
        }//Case 2110 End
        
        //Case 2106 Start 2
        else if(source.equals(
        ((InvestigatorForm)investigatorController.getControlledUI()).btnCreditSplit)){
            performCreditSplit();
        }
        //Case 2106 End 2
        //Added for case#2136 Enhancement start 2
        else if(source.equals(
        ((InvestigatorForm)investigatorController.getControlledUI()).btnAdminType)){
            performAdminType();
        }
        //Added for case#2136 Enhancement end 2
        //Added for Case#2214 Email enhancement - start
        else if(source.equals(instituteProposalBaseWindow.btnSendEmail)) {
            ActionValidityChecking checkValid = new ActionValidityChecking();
            synchronized(checkValid) {
                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
                checkValid.sendMail(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, 0, proposalNumber, instituteProposalBaseBean.getSequenceNumber());
            }
        }
        //Added for Case#2214 Email enhancement - end
        //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
        else if(source.equals(instituteProposalBaseWindow.btnAttachments) ||
                source.equals(instituteProposalBaseWindow.mnuItmAttachments)){
            if(functionType == NEW_ENTRY_INST_PROPOSAL || (functionType!= DISPLAY_PROPOSAL &&  isSaveRequired())){
                int confirm = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(SAVE_PROPOSAL_TO_OPEN_ATTACHMENT),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                switch(confirm){
                    case JOptionPane.YES_OPTION:
                        try{
                            if(validate()){
                                if(getFunctionType()== CORRECT_INST_PROPOSAL){
                                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    queryEngine.update(queryKey, instituteProposalBean);
                                }
                                saveProposal(false);
                                showProposalAttachment();
                            }
                        }catch (CoeusUIException exception){
                            CoeusOptionPane.showDialog(exception);
                            tbdPnInstProposal.setSelectedIndex(exception.getTabIndex());
                        } catch(Exception exception){
                            exception.printStackTrace();
                            CoeusOptionPane.showErrorDialog(exception.getMessage());
                        }
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                }
            }else{
                showProposalAttachment();
            }
        
        }
        //COEUSQA-1525 : End
        }catch(CoeusException coeusException){
           coeusException.printStackTrace();
           CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
           exception.printStackTrace();
           CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    
    /** Added by chandra to show the current and pending details
     */
    private void showCurrentAndPendingDetails() throws CoeusException{
        InvestigaorListForm investigaorListForm = new InvestigaorListForm(mdiForm, queryKey);
        //Modified for Case #2371 start 1
        investigaorListForm.setOpenFromPropDev(false);
        investigaorListForm.setFormData();
        investigaorListForm.display();
        //Modified for Case #2371 end 1
    }
    
    /*
     *  Added by Geo to activate the print
     */
    /**
     *  Code to go for printing
     */
    private String printProposalNotice() throws CoeusException{
        String printServletURL = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
        
        Hashtable htPrintParams = new Hashtable();
        htPrintParams.put("PROPOSAL_NUMBER",this.getProposalNumber());
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(INST_PROPOSAL_PRINTING);
        requester.setDataObject(htPrintParams);
        
        //For Streaming
        htPrintParams.put("USER_ID", mdiForm.getUserId());
        printServletURL = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
        requester.setId("InstituteProposal/InstituteProposalReport");
        requester.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(printServletURL, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        String fileName = "";
        if(responder.isSuccessfulResponse()){
            AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            
            //             System.out.println("Got the Clob Data");
            fileName = (String)responder.getDataObject();
            /*System.out.println("Report Filename is=>"+fileName);
             
            fileName.replace('\\', '/') ; // this is fix for Mac
            URL reportUrl = null;
            try{
                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
             
             
                if (coeusContxt != null) {
                    coeusContxt.showDocument( reportUrl, "_blank" );
                }else {
                    javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                    bs.showDocument( reportUrl );
                }
            }catch(MalformedURLException muEx){
                throw new CoeusException(muEx.getMessage());
            }catch(Exception uaEx){
                throw new CoeusException(uaEx.getMessage());
            }*/
            try{
                URL url = new URL(fileName);
                URLOpener.openUrl(url);
            }catch (MalformedURLException malformedURLException) {
                throw new CoeusException(malformedURLException.getMessage());
            }
            
        }else{
            throw new CoeusException(responder.getMessage());
        }
        return fileName;
    }
    
    
    // show the negotiation only if the rights presnt else throw the message
    private void showNegotiation(){
        if(negoMode){
            NegotiationInfoBean bean = new NegotiationInfoBean();
            bean.setNegotiationNumber(proposalNumber);
            edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController controller=
            new edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController(CoeusGuiConstants.INSTITUTE_PROPOSAL_LIST,'M',bean);
            controller.display();
        }else{
            NegotiationInfoBean bean = new NegotiationInfoBean();
            bean.setNegotiationNumber(proposalNumber);
            edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController controller=
            new edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController(CoeusGuiConstants.INSTITUTE_PROPOSAL_LIST,'D',bean);
            controller.display();
        }
    }
    
    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    //Case 2110  Start to get the Current Locks for logged in user
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110
    
   //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Displays Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End     
     
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha
    
    
    
    /**
     * Method used to close the application after confirmation.
     */
    public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
        "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
    }
    
    
    public void showProposalInDisplay(String path){
        observable.notifyObservers(path);
    }
    
    
    /** Displays the Proposal Notepad screen */
    private void showNotepad(){
        if (functionType!= DISPLAY_PROPOSAL &&  isSaveRequired() ){
            int confirm = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("instPropNotepad_exceptionCode.1501"),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case JOptionPane.YES_OPTION:
                    try{
                        if(validate()){
                            //case 2061 start
                            if(getFunctionType()== CORRECT_INST_PROPOSAL){
                                instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                                queryEngine.update(queryKey, instituteProposalBean);
                            }
                            //case 2061 end 
                            saveProposal(false);
                            showNotepadWindow();
                        }
                    }catch (CoeusUIException exception){
                        CoeusOptionPane.showDialog(exception);
                        tbdPnInstProposal.setSelectedIndex(exception.getTabIndex());
                    }
                    //case 2061 start
                    catch(Exception e){
                        e.printStackTrace();
//                        CoeusOptionPane.showErrorDialog(e.getMessage());
                    }
                    //case 2061 end
                    break;
                case JOptionPane.NO_OPTION:
                    break;
            }
        }else{
            showNotepadWindow();
        }
    }
    
    /** Supporting method to display the Proposal Notepad screen */
    private void showNotepadWindow(){
        //Check if Notepad is already opened
        CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.NOTEPAD_FRAME_TITLE);
        if(frame == null){
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setInstituteProposalNumber(proposalNumber);
            linkBean.setBaseType(CoeusConstants.INST_PROP);
            
            ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(linkBean, mdiForm);
            proposalNotepadForm.display();
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "proposal_Notepad_exceptionCode.7116"));
        }
    }
    
    
    private void showMedusa() {
        if ( functionType!= DISPLAY_PROPOSAL && isSaveRequired() ){
            int confirm = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("medusaSaveConfirm_exceptionCode.1120"),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case JOptionPane.YES_OPTION:
                    try{
                        if(validate()){
                            //case 2061 start
                            if(getFunctionType()== CORRECT_INST_PROPOSAL){
                                instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                                queryEngine.update(queryKey, instituteProposalBean);
                            }
                            //case 2061 end 
                            saveProposal(false);
                            showMedusaWidnow();
                        }
                    }catch (CoeusUIException exception){
                        CoeusOptionPane.showDialog(exception);
                        tbdPnInstProposal.setSelectedIndex(exception.getTabIndex());
                    }
                    //case 2061 start
                    catch(Exception e){
                        e.printStackTrace();
//                        CoeusOptionPane.showErrorDialog(e.getMessage());
                    }
                    //case 2061 end
                    break;
                case JOptionPane.NO_OPTION:
                    break;
            }
        }else{
            showMedusaWidnow();
        }
    }
    
    
    private void  showMedusaWidnow(){
        try{
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            
            linkBean.setBaseType(CoeusConstants.INST_PROP);
            linkBean.setInstituteProposalNumber(proposalNumber);
            
            if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
            CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                linkBean.setBaseType(CoeusConstants.INST_PROP);
                linkBean.setInstituteProposalNumber(proposalNumber);
                medusaDetailform.setSelectedNodeId(proposalNumber);
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
            medusaDetailform.setVisible(true);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    private void unLockProposal(){
        AwardListForProposal awardListForProposal = new AwardListForProposal(
        instituteProposalBean, queryKey);
        InstituteProposalBean instPropBean = awardListForProposal.display();
        if( instPropBean.getStatusCode()==1){
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(instPropBean);
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
            instituteProposalBaseWindow.mnuItmUnlockProposal.setEnabled(false);
        }
    }
    
    private void saveProposal(boolean isLocked)throws CoeusUIException{
        CoeusVector cvTemp =  null;
        //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
        if(ipCustomElementsController != null){
            customTable =ipCustomElementsController.getCustomElementsForm().getTable();
            if( getFunctionType() != DISPLAY_PROPOSAL) {
                row = ipCustomElementsController.getCustomElementsForm().getRow();
                column = ipCustomElementsController.getCustomElementsForm().getColumn();
            }
        }
        //Case :#3149 ? End
        if(!validate()) {
            throw new CoeusUIException();
        }
        iPMailingInfoController.saveFormData();
        reviewController.saveFormData();
        investigatorController.saveFormData();
        // 3823: Key Person Records Needed in Inst Proposal and Award
        ipKeyPersonController.saveFormData();
        //instituteProposalDetailController.saveFormData();
        
        if(specialReviewController.isFormSaveRequired()){
            // Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            CoeusVector cvSpecialReviewData = (CoeusVector)specialReviewController.getSpecialReviewData();
            // Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            specialReviewController.saveFormData();
            specialReviewController.setFormSaveRequired(false);
        }
        
        if(iPScienceCodeController.isSaveRequied()){
            iPScienceCodeController.saveFormData();
        }
        if( ipCustomElementsController != null ){
            ipCustomElementsController.saveFormData();
        }
        
        // JM 5-14-2013
        if(subcontractController.isSaveRequired()){
        	subcontractController.saveFormData();
        }
        // JM END
        
        Hashtable htProposalDetail;
        
        htProposalDetail = queryEngine.getDataCollection(queryKey);
        // Set the Lock flag whether the window has to be locked or not
        htProposalDetail.put(CoeusConstants.IS_RELEASE_LOCK,new Boolean(isLocked));
        
        htProposalDetail.remove(KeyConstants.ACTIVITY_TYPE);
        htProposalDetail.remove(KeyConstants.NSF_CODE);
        htProposalDetail.remove(KeyConstants.PROPOSAL_TYPE);
        htProposalDetail.remove(KeyConstants.NOTICE_OF_OPPORTUNITY);
        
        integrateInvestigatorUnits(htProposalDetail);
        updateProposalData(htProposalDetail);
        
      
        
    }
    
    
    private void updateProposalData(Hashtable proposalData){
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(SAVE_PROPOSAL);
        requesterBean.setDataObject(proposalData);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return ;
        }
        
        if(responderBean.isSuccessfulResponse()) {
            Hashtable htProposalData = (Hashtable)responderBean.getDataObject();
            
            CoeusVector cvTemp = (CoeusVector)htProposalData.get(InstituteProposalBean.class);
            instituteProposalBean = (InstituteProposalBean)cvTemp.get(0);
            
            instituteProposalBean.setAcType(null);
            
            // bug Fix #1918 - start
            //observable.setFunctionType(functionType)
            observable.setFunctionType(getFunctionType());
            // bug Fix #1918 - End
            observable.notifyObservers(instituteProposalBean);
            
            htProposalData.remove(InstituteProposalBean.class);
            htProposalData.put(InstituteProposalBean.class, cvTemp);
            
            queryEngine.addDataCollection(queryKey, htProposalData);
            extractInvestigatorUnits();
            // Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            CoeusVector cvSpRvData = specialReviewController.getCvSpRvData();
            CoeusVector cvData = new CoeusVector();
            // Code for Mail 
            ActionValidityChecking checkValid = new ActionValidityChecking();
            if(cvSpRvData != null){
                    for(int i =0 ; i < cvSpRvData.size();i++){
                        SpecialReviewFormBean instituteProposalSpRevBean =(SpecialReviewFormBean)cvSpRvData.get(i);
                        if(instituteProposalSpRevBean.getSpecialReviewCode() == 2){
                            if(instituteProposalSpRevBean.getAcType() != null  && instituteProposalSpRevBean.getAcType().equals("I")){
                                synchronized(checkValid){                          
                                    checkValid.sendMail(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.IP_SPECIAL_REVIEW_INSERTED_FOR_IACUC,proposalNumber,0);
                                }
                            }else if(instituteProposalSpRevBean.getAcType() != null  && instituteProposalSpRevBean.getAcType().equals("D")){
                                
                                synchronized(checkValid){                                   
                                    checkValid.sendMail(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.IP_SPECIAL_REVIEW_DELETED_FOR_IACUC,proposalNumber,0);
                                }
                            }
                        }
                    }
                }
            // Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            refresh();
            //System.out.println("The AcType is : "+instituteProposalBean.getAcType());
            proposalModified = false;
            specialReviewController.setFormSaveRequired(false);
            investigatorController.setDataChanged(false);
        }else {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return ;
        }
    }
    
    /** refreshes all tab pages. */
    public void refresh() {
        //refresh all tab pages. set mode as update since
        //after refreshing the data to will sent as update
        //next time saved.
        if(functionType!= DISPLAY_PROPOSAL){
            setFunctionType(CORRECT_INST_PROPOSAL);
            instituteProposalDetailController.setFunctionType(CORRECT_INST_PROPOSAL);
            iDCRatesController.setFunctionType(CORRECT_INST_PROPOSAL);
            iPScienceCodeController.setFunctionType(CORRECT_INST_PROPOSAL);
            iPMailingInfoController.setFunctionType(CORRECT_INST_PROPOSAL);
            costSharingController.setFunctionType(CORRECT_INST_PROPOSAL);
            specialReviewController.setFunctionType(CORRECT_INST_PROPOSAL);
            reviewController.setFunctionType(CORRECT_INST_PROPOSAL);
            if( ipCustomElementsController != null ) {
                ipCustomElementsController.setFunctionType(CORRECT_INST_PROPOSAL);
            }
            investigatorController.setFunctionType(CORRECT_INST_PROPOSAL);
            // 3823: Key Person Records Needed in Inst Proposal and Award
            ipKeyPersonController.setFunctionType(CORRECT_INST_PROPOSAL);
            
            // JM 5-3-2013 refresh subcontracts data
            subcontractController.setFunctionType(CORRECT_INST_PROPOSAL);
            // JM END
        }
        
        // JM 5-3-2013 refresh subcontracts data
        subcontractController.setRefreshRequired(true);
        subcontractController.refresh();
        // JM END
        
        instituteProposalDetailController.setRefreshRequired(true);
        instituteProposalDetailController.refresh();
        
        
        iDCRatesController.setRefreshRequired(true);
        iDCRatesController.refresh();
        
        
        iPScienceCodeController.setRefreshRequired(true);
        iPScienceCodeController.refresh();
        
        iPMailingInfoController.setRefreshRequired(true);
        iPMailingInfoController.refresh();
        
        costSharingController.setRefreshRequired(true);
        costSharingController.refresh();
        // Refresh immediately after saving
        specialReviewController.setRefreshRequired(true);
        specialReviewController.refresh();
        
        reviewController.setRefreshRequired(true);
        reviewController.refresh();
        if( ipCustomElementsController != null ) {
            ipCustomElementsController.setRefreshRequired(true);
            ipCustomElementsController.refresh();
        }
        
        investigatorController.setRefreshRequired(true);
        investigatorController.refresh();
        
        // 3823: Key Person Records Needed in Inst Proposal and Award - Start
        ipKeyPersonController.setDataChanged(true);
        ipKeyPersonController.refresh();
        // 3823: Key Person Records Needed in Inst Proposal and Award - End
    }
    
    
    
    
    
    /** Checks if data in QueryEngine is modifed and needs to be saved.
     * returns false if nothing to save.
     * else returns true.
     * @return false if nothing to save.
     * else returns true.
     */
    public boolean isSaveRequired()  {
        if(windowOpen ){
            if(functionType == NEW_INST_PROPOSAL ){
                return false;
            }
        }
        //Save Data for the currently selected Tab
        int selectedTabIndex = tbdPnInstProposal.getSelectedIndex();
        switch (selectedTabIndex){
            case PROPOSAL_DETAIL_TAB_INDEX:
                instituteProposalDetailController.saveFormData();
                break;
            case IDC_RATES_TAB_INDEX:
                iDCRatesController.saveFormData();
                break;
            case SCIENCE_CODE_TAB_INDEX:
                iPScienceCodeController.saveFormData();
                break;
            case COST_SHARING_TAB_INDEX:
                costSharingController.saveFormData();
                break;
            case IP_REVIEW_TAB_INDEX:
                reviewController.saveFormData();
                break;
            case MAINLINGINFO_TAB_INDEX:
                iPMailingInfoController.saveFormData();
                break;
            // 3823: Key Person Records Needed in Inst Proposal and Award - Start
            case KEY_PERSONS_TAB_INDEX:
                ipKeyPersonController.saveFormData();
                break;
            // 3823: Key Person Records Needed in Inst Proposal and Award - End
            // JM 5-14-2013 for subcontracts
            case SUBCONTRACTS_TAB_INDEX:
                subcontractController.saveFormData();
                break;
            // JM END
            case IP_CUSTOM_TAB_INDEX :
                if( ipCustomElementsController != null ) {
                    ipCustomElementsController.saveFormData();
                }
                break;
        }
        // Added by chandra.
        // It is a fix for the query kwy where its default value is only "0".
        if(queryKey!=null && (!queryKey.equals("0"))){
            //          if(queryKey!=null){
            if(queryEngine.getKeyEnumeration(queryKey)!= null){
                Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
                
                Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
                Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
                Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
                
                NotEquals notEq = new NotEquals("acType", "null");
                
                Or insertOrUpdate = new Or(eqInsert, eqUpdate);
                Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
                
                Or notNull = new Or(insertOrUpdateOrDelete,notEq);
                
                Object key;
                CoeusVector data;
                try{
                    while(enumeration.hasMoreElements()) {
                        key = enumeration.nextElement();
                        if(!(key instanceof Class)) continue;
                        data = queryEngine.executeQuery(queryKey, (Class)key, notNull);
                        if(! proposalModified) {
                            if(data != null && data.size() > 0) {
                                proposalModified = true;
                                break;
                            }
                        }
                    }
                }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                }
            }
        }
        //Bug Fix for case #1877 start 4
        if(detailForm.txtPrimeSponsor.getText()!=null
            &&!detailForm.txtPrimeSponsor.getText().trim().equals("")){
                if(!primeSponsorCode.equals(detailForm.txtPrimeSponsor.getText())){
                    if(detailForm.lblPrimeSponsorName.getText()!=null){
                        if(detailForm.lblPrimeSponsorName.getText().length()==0){
                            proposalModified=true;
                        }
                    }
                }
                    

        }
        //Bug Fix for case #1877 end 4
        return proposalModified || investigatorController.isDataChanged()
        // 3823: Key Person Records Needed in Inst Proposal and Award
        || (ipKeyPersonController != null && ipKeyPersonController.isDataChanged())
        || iPScienceCodeController.isSaveRequied()
        || (ipCustomElementsController != null && ipCustomElementsController.isDataChanged())||specialReviewController.isFormSaveRequired();
    }
    
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        try{
            if(closed) return ;
            boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
            if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
                closeProposal();
                //            closed = true;
            }
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    
    /** closes the Base Window and removes the reference from MDIForm.
     * @throws PropertyVetoException PropertyVetoException
     */
    public void closeProposal() throws PropertyVetoException,CoeusException{
        //        if( closed ) return ;
        if(getFunctionType() != DISPLAY_PROPOSAL ){// && getFunctionType()!= NEW_INST_PROPOSAL) {
            //Check if data modified and display save confirmation.
            if(isSaveRequired()){
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    try{
                        if(getFunctionType()== CORRECT_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                            queryEngine.update(queryKey, instituteProposalBean);
                        }
                        saveProposal(false);
                        
                        releaseLock(proposalNumber);
                    }catch (CoeusUIException coeusUIException) {
                        if(!coeusUIException.getMessage().equals("null.")) {
                            CoeusOptionPane.showDialog(coeusUIException);
                            tbdPnInstProposal.setSelectedIndex(coeusUIException.getTabIndex());
                        }
                        throw new PropertyVetoException(EMPTY_STRING, null);
                    }catch (Exception exception) {
                        exception.printStackTrace();
                        throw new PropertyVetoException(EMPTY_STRING, null);
                    }
                }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                    //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
                    if(tbdPnInstProposal.getSelectedIndex() == IP_CUSTOM_TAB_INDEX && ipCustomElementsController != null && getFunctionType() != DISPLAY_PROPOSAL){
                        if(ipCustomElementsController != null){
                            boolean[] lookUpAvailable =ipCustomElementsController.getCustomElementsForm().getLookUpAvailable();
                            customElementsForm = ipCustomElementsController.getCustomElementsForm();
                            customTable = ipCustomElementsController.getCustomElementsForm().getTable();
                            row = customElementsForm.getRow();
                            column = customElementsForm.getColumn();
                            count = 0;
                            if(row != -1 && column != -1){
                                if(lookUpAvailable[row]){
                                    if(column == 1){
                                        column++;
                                    }
                                    customTable.editCellAt(row,column);
                                    customTable.setRowSelectionInterval(row,row);
                                    customTable.setColumnSelectionInterval(column,column);
                                }else{
                                    customTable.editCellAt(row,column);
                                    customTable.setRowSelectionInterval(row,row);
                                    customTable.changeSelection(row,column,false,false);
                                }
                            }
                            customTable.setEnabled(true);
                        }
                    }
                    //Case :#3149 ? End
                    throw new PropertyVetoException(EMPTY_STRING, null);
                }else if(selection == CoeusOptionPane.SELECTION_NO) {
                    releaseLock(proposalNumber);
                }
            }else{
                releaseLock(proposalNumber);
            }
        }
        mdiForm.removeFrame(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW, referenceId);
        if(getFunctionType()!= NEW_INST_PROPOSAL){
            queryEngine.removeDataCollection(queryKey);
        }
        closed = true;
        cleanUp();
    }
    
    /** cleans up instance variables.
     * i.e. sets instance variables to null.
     */
    protected void cleanUp() {
        //Bug Fix for case #1877 start 5
        queryEngine.removeDataCollection(queryKeys);
        detailForm=null;
        //Bug Fix for case #1877 end 5
        instituteProposalBaseWindow = null;
        instituteProposalBaseBean = null;
        tbdPnInstProposal= null;
        instituteProposalDetailController = null;
        iDCRatesController = null;
        iPScienceCodeController = null;
        iPMailingInfoController= null;
        costSharingController = null;
        reviewController = null;
        investigatorController =  null;
        specialReviewController = null;
        ipCustomElementsController = null;
        // 3823: Key Person Records Needed in Inst Proposal and Award
        ipKeyPersonController = null;
        proposalNumber = null;
        tbdPnInstProposal = null;
    }
    
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        //save selected tabs FormData before Calling Refresh.
        if(functionType!= DISPLAY_PROPOSAL){
            switch (selectedTabIndex) {
                case PROPOSAL_DETAIL_TAB_INDEX:
                    instituteProposalDetailController.saveFormData();
                    break;
                case MAINLINGINFO_TAB_INDEX:
                    iPMailingInfoController.saveFormData();
                    break;
                case COST_SHARING_TAB_INDEX:
                    costSharingController.saveFormData();
                    break;
                case IDC_RATES_TAB_INDEX:
                    iDCRatesController.saveFormData();
                    break;
                case IP_REVIEW_TAB_INDEX:
                    reviewController.saveFormData();
                    break;
            }
        }
        
        //Refresh Data and set the default focus
        selectedTabIndex = tbdPnInstProposal.getSelectedIndex();
        switch (selectedTabIndex) {
            case PROPOSAL_DETAIL_TAB_INDEX:
                if(functionType!= DISPLAY_PROPOSAL){
                    instituteProposalDetailController.refresh();
                }
                if(valid){
                    instituteProposalDetailController.setDefaultFocusForComponent();
                }
                break;
            case COST_SHARING_TAB_INDEX:
                if(functionType!= DISPLAY_PROPOSAL){
                    costSharingController.refresh();
                }
                if(valid){
                    costSharingController.setDefaultFocusForComponent();
                }
                break;
            case IDC_RATES_TAB_INDEX:
                if(functionType!= DISPLAY_PROPOSAL){
                    iDCRatesController.refresh();
                }
                if(valid){
                    iDCRatesController.setDefaultFocusForComponent();
                }
                break;
            case IP_REVIEW_TAB_INDEX:
                if(functionType!= DISPLAY_PROPOSAL){
                    reviewController.refresh();
                }
                if(valid){
                    reviewController.setDefaultFocusForComponent();
                }
                break;
            case SCIENCE_CODE_TAB_INDEX:
                if(functionType!= DISPLAY_PROPOSAL){
                    iPScienceCodeController.refresh();
                }
                iPScienceCodeController.setDefaultFocusForComponent();
                break;
            case SPECIAL_REVIEW_INDEX:
                specialReviewController.setDefaultFocusInWindow();
                break;
            // 3823: Key Person Records Needed in Inst Proposal and Award - Start
            case KEY_PERSONS_TAB_INDEX:
                ipKeyPersonController.setDefaultFocusInWindow();
                break;
            // 3823: Key Person Records Needed in Inst Proposal and Award - End
                //Case :#3149 ? Tabbing between fields does not work on others tabs - Start
            case IP_CUSTOM_TAB_INDEX:
                if(getFunctionType() != DISPLAY_PROPOSAL && ipCustomElementsController != null){
                    ipCustomElementsController.setTabFocus();
                    if(tbdPnInstProposal.getSelectedIndex() == IP_CUSTOM_TAB_INDEX && count != 0 && ipCustomElementsController != null){
                        ipCustomElementsController.setTableFocus();
                    }
                    tbdPnInstProposal.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            count = 1;
                            if(tbdPnInstProposal.getSelectedIndex() == IP_CUSTOM_TAB_INDEX){
                                ipCustomElementsController.setTableFocus();
                            }
                        }
                    });
                }
                break;
                //Case :#3149 - End
        }
        //rdias - UCSD's coeus personalization - Overwrite comp in a tab or a form
//        AbstractController persnref = AbstractController.getPersonalizationControllerRef();
//        persnref.customize_Form(tbdPnInstProposal.getComponentAt(selectedTabIndex),
//        		CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE);
//        // rdias - UCSD
    }

    /** Getter for property proposalLogNumber.
     * @return Value of property proposalLogNumber.
     *
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalLogNumber.
     * @param proposalLogNumber New value of property proposalLogNumber.
     *
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    public void allFormData(Hashtable data){
        if(data!=null){
            CoeusVector proposalDetail = (CoeusVector)data.get(InstituteProposalBean.class);
            if( proposalDetail!=null && proposalDetail.size()>0){
                instituteProposalBean = (InstituteProposalBean)proposalDetail.elementAt(0);
                InstituteProposalBaseBean instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBean;
                
                this.instituteProposalBean = instituteProposalBean;
                
                //generate query keys for tab controllers - START - For new institute Propoosal
                //System.out.println("The Base bean is :" +instituteProposalBaseBean);
                instituteProposalDetailController.prepareQueryKey(instituteProposalBaseBean);
                investigatorController.setQueryKey(getQueryKey());
                investigatorController.setIpBaseBean(instituteProposalBaseBean);
                iDCRatesController.prepareQueryKey(instituteProposalBaseBean);
                costSharingController.prepareQueryKey(instituteProposalBaseBean);
                iPMailingInfoController.prepareQueryKey(instituteProposalBaseBean);
                iPScienceCodeController.prepareQueryKey(instituteProposalBaseBean);
                reviewController.prepareQueryKey(instituteProposalBaseBean);
                specialReviewController.prepareQueryKey(instituteProposalBaseBean);
                // 3823: Key Person Records Needed in Inst Proposal and Award
                ipKeyPersonController.setQueryKey(getQueryKey());
                try{
                    CoeusVector cvCustomData = queryEngine.executeQuery(queryKey,InstituteProposalCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                    if( cvCustomData != null && cvCustomData.size() > 0 ) {
                        if(ipCustomElementsController== null){
                            ipCustomElementsController = new IPCustomElementsController(instituteProposalBaseBean, getFunctionType());
                            tbdPnInstProposal.addTab(IP_CUSTOM, ipCustomElementsController.getControlledUI());
                        }
                        ipCustomElementsController.prepareQueryKey(instituteProposalBaseBean);
                    }
                }catch ( CoeusException ce ){
                    ce.printStackTrace();
                }
                
                //generate query keys for tab controllers - END
                
                //setQueryKey(instituteProposalBean.getProposalNumber()+instituteProposalBean.getSequenceNumber());
                
                instituteProposalDetailController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                investigatorController.setFormData();
                specialReviewController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                costSharingController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                iDCRatesController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                reviewController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                iPScienceCodeController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                iPMailingInfoController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                // 3823: Key Person Records Needed in Inst Proposal and Award - Start
                ipKeyPersonController.setIpBaseBean((InstituteProposalBaseBean)instituteProposalBean);
                ipKeyPersonController.setFormData();
                // 3823: Key Person Records Needed in Inst Proposal and Award - End
                if( ipCustomElementsController != null ){
                    ipCustomElementsController.setFormData((InstituteProposalBaseBean)instituteProposalBean);
                }
            }
        }
    }
    
    /** Getter for property windowOpen.
     * @return Value of property windowOpen.
     *
     */
    public boolean isWindowOpen() {
        return windowOpen;
    }
    
    /** Setter for property windowOpen.
     * @param windowOpen New value of property windowOpen.
     *
     */
    public void setWindowOpen(boolean windowOpen) {
        this.windowOpen = windowOpen;
    }
    
    // If data is not saved, open and close the proposal then, unlock the key
    private void releaseLock(String proposalNumber){
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(RELEASE_LOCK);
        requesterBean.setDataObject(proposalNumber);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            //isLock = true
        }else{
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
        }
        
    }
    
    public void beanUpdated(BeanEvent beanEvent) {
        if(functionType!= DISPLAY_PROPOSAL){
            if(beanEvent.getBean().getClass().equals(InstituteProposalBean.class)) {
                instituteProposalBean = (InstituteProposalBean)beanEvent.getBean();
                setRefreshRequired(true);
            }
        }
    }
    
    //register observer for updating to base window
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /** Getter for property ipRight.
     * @return Value of property ipRight.
     *tbdPnInstProposal
     */
    public boolean isIpRight() {
        return ipRight;
    }
    
    /** Setter for property ipRight.
     * @param ipRight New value of property ipRight.
     *
     */
    public void setIpRight(boolean ipRight) {
        this.ipRight = ipRight;
    }
    
    public void clearOldInstance() {
        mdiForm.removeFrame(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW,referenceId);
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            
            String oldTitle = "Display Institute Proposal " + instituteProposalBaseBean.getProposalNumber() +"   Sequence  "+instituteProposalBaseBean.getSequenceNumber();
            windowMenu = windowMenu.removeMenuItem( oldTitle );
        }
        
    }
    public void performNavigation(String newProposalId) throws CoeusException{
        proposalNumber = newProposalId;
        //        mdiForm.removeFrame(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW,referenceId);
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        //        if( windowMenu != null ){
        //
        //            String oldTitle = "Display Institute Proposal " + instituteProposalBaseBean.getProposalNumber() +"   Sequence  "+instituteProposalBaseBean.getSequenceNumber();
        //            windowMenu = windowMenu.removeMenuItem( oldTitle );
        //        }
        
        Hashtable htData =  getProposalData();
        allFormData(htData);
        title = "Display Institute Proposal " + instituteProposalBaseBean.getProposalNumber() +"   Sequence  "+instituteProposalBaseBean.getSequenceNumber();
        
        instituteProposalBaseWindow.setTitle(title);
        referenceId = newProposalId;
        
        mdiForm.putFrame(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW, referenceId,functionType,instituteProposalBaseWindow);
        //update to handle new window menu item to the existing Window Menu.
        if( windowMenu != null ){
            windowMenu = windowMenu.addNewMenuItem( title, instituteProposalBaseWindow );
            windowMenu.updateCheckBoxMenuItemState( title, true );
        }
        mdiForm.refreshWindowMenu(windowMenu);
    }
    
    public void setMode(char functionType) {
        this.functionType = functionType;
        super.setFunctionType(functionType);
    }
    
    /** Getter for property negotiationRightData.
     * @return Value of property negotiationRightData.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getNegotiationRightData() {
        return negotiationRightData;
    }
    
    /** Setter for property negotiationRightData.
     * @param negotiationRightData New value of property negotiationRightData.
     *
     */
    public void setNegotiationRightData(edu.mit.coeus.utils.CoeusVector negotiationRightData) {
        this.negotiationRightData = negotiationRightData;
    }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     *
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     *
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property newProposalNumber.
     * @return Value of property newProposalNumber.
     *
     */
    public java.lang.String getNewProposalNumber() {
        return newProposalNumber;
    }
    
    /** Setter for property newProposalNumber.
     * @param newProposalNumber New value of property newProposalNumber.
     *
     */
    public void setNewProposalNumber(java.lang.String newProposalNumber) {
        this.newProposalNumber = newProposalNumber;
    }
    
    private void enableDisableNegotiation(){
        CoeusVector rightData = getNegotiationRightData();
        if(rightData!= null && rightData.size()>0){
            hasNegoModifyRight  = ((Boolean)getNegotiationRightData().elementAt(0)).booleanValue();
            hasNegoViewData = ((Boolean)rightData.elementAt(1)).booleanValue();
            boolean checkPi = ((Boolean)rightData.elementAt(2)).booleanValue();
            
            if(hasNegoModifyRight){
                negoMode=true;
            }else{
                if(checkPi){
                    negoMode = false;
                }else{
                    if(getUnitNumber()!= null && !EMPTY_STRING.equals(getUnitNumber())){
                        if(hasNegoViewData){
                            negoMode = true;
                        }else{
                            instituteProposalBaseWindow.mnuItmNegotiation.setEnabled(false);
                        }
                    }else{
                        instituteProposalBaseWindow.mnuItmNegotiation.setEnabled(false);
                    }
                }
            }
        }
    }
    
    public void display(){
    }
    
    //Case 2106 3
    private void performCreditSplit(){
        if(isSaveRequired() && getFunctionType() != TypeConstants.DISPLAY_MODE){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("proposal_investigator_exceptionCode.1000"));
            return ;
        }
        
        InvestigatorCreditSplitController invCreditSplitController = 
                        new InvestigatorCreditSplitController(getFunctionType());
        invCreditSplitController.setFormData(investigatorController.getCreditSplitData());
        invCreditSplitController.display();
        invCreditSplitController.cleanUp();
    }
    //Case 2106 3
    //Added for case#2136 enhancement start 3
    private void performAdminType(){
        if(isSaveRequired() && getFunctionType() != TypeConstants.DISPLAY_MODE){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("proposal_unit_adminType_exceptionCode.1000"));
            return ;
        }
        try{
            
            InvestigatorUnitAdminTypeController adminType
                = new InvestigatorUnitAdminTypeController(getFunctionType(), 
                    (InvCreditSplitObject)investigatorController.getCreditSplitData());
            adminType.setFormData(investigatorController.getLeadUnitNo());
            adminType.setFormBaseBean(instituteProposalBean);
            adminType.display();
            // Added by Noorul for Enhancement 11-01-07 starts
            investigatorController.fetchAdministratorData();
            // Added by Noorul for Enhancement 11-01-07 ends             
            adminType.cleanUp();
        }catch(CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
    }
    //Added for case#2136 enhancement end 3
    
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    /*
     * Gets the proposal attachments rights from server and sets it
     * @param proposalNumber - String
     */
    public void fetchProposalAttachmentRights(String proposalNumber)throws Exception {
        boolean canUserMaintainAttachment = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_ATTACHMENT_RIGHTS);
        request.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            Hashtable htAttacmentRights = (Hashtable)response.getDataObject();
            setUserCanMaintainAttachment(((Boolean)htAttacmentRights.get(Integer.valueOf(0))).booleanValue());
            setUserCanViewAttachment(((Boolean)htAttacmentRights.get(Integer.valueOf(1))).booleanValue());
        }else{
            throw new Exception(response.getMessage());
        }
    }
    
    /*
     * Method to check user can maintain attachment
     * @return userCanMaintainAttachment
     */
    private boolean isUserCanMaintainAttachment() {
        return userCanMaintainAttachment;
    }
    
    /*
     * Method to set whether user can maintain attachment
     * @param userCanMaintainAttachment
     */
    private void setUserCanMaintainAttachment(boolean userCanMaintainAttachment) {
        this.userCanMaintainAttachment = userCanMaintainAttachment;
    }
    
     /*
      * Method to check user view maintain attachment
      * @return userCanViewAttachment
      */
    private boolean isUserCanViewAttachment() {
        return userCanViewAttachment;
    }
    
    /*
     * Method to set whether user can view attachment
     * @param userCanViewAttachment
     */
    private void setUserCanViewAttachment(boolean userCanViewAttachment) {
        this.userCanViewAttachment = userCanViewAttachment;
    }
    
    /*
     * Method to show institute proposal attachment window
     */
    public void showProposalAttachment() throws Exception{
        fetchProposalAttachmentRights(proposalNumber);
        if(isUserCanMaintainAttachment()){
            showAttachment(proposalNumber,instituteProposalBaseBean.getSequenceNumber(),TypeConstants.MODIFY_MODE);
        }else if(isUserCanViewAttachment()){
            showAttachment(proposalNumber,instituteProposalBaseBean.getSequenceNumber(),TypeConstants.DISPLAY_MODE);
        }else{
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    CANNOT_OPEN_ATTACHMENT));
        }
    }
    
    
    public void showAttachment(String proposalNumber, int sequenceNumber,char functionType){
        try{
            mdiForm.checkDuplicate(CoeusGuiConstants.INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE,
                    proposalNumber, functionType );
            
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE,proposalNumber);
            if(frame == null){
                // if no frame opened for the requested record then the
                //   requested mode is edit mode. So get the frame of the
                //   editing record.
                frame = mdiForm.getEditingFrame(
                        CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE );
            }
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            return;
        }
        InstituteProposalAttachmentForm proposalAttachmentForm =
                new InstituteProposalAttachmentForm(mdiForm,proposalNumber, sequenceNumber,functionType);
        proposalAttachmentForm.showAttachmentForm();
        
    }
    //COEUSQA-1525 : End

    // rdias UCSD - Coeus personalization impl
    public IPCustomElementsController getipCustomElementsController() {
        return ipCustomElementsController;
    }
    
  	public void customizeProposal() {
//    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
//    	persnref.customize_tabs(tbdPnInstProposal,
//    			CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE,instituteProposalBaseBean.getProposalNumber(),
//    			functionType);
//    	persnref.customize_module(this.getControlledUI(),this.getControlledUI(),this,
//    			CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE);
//    	persnref.customize_Form(instituteProposalDetailController.getControlledUI(),
//    			CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE);
//    	
    }
   // rdias UCSD - Coeus personalization impl    
   // rdias UCSD - Coeus personalization impl


}

