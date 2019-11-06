/*
 * MedusaDetailForm.java
 *
 * Created on January 2, 2004, 10:43 AM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusFontFactory;
//import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.gui.ProtocolDetailForm;
//import edu.mit.coeus.propdev.gui.ProposalAwardHierarchyForm;

import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.award.gui.AwardHierarchyInMedusaForm;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.controller.AwardBaseWindowController;
import edu.mit.coeus.instprop.controller.InstituteProposalBaseWindowController;
import edu.mit.coeus.instprop.bean.InstituteProposalBaseBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.negotiation.bean.NegotiationInfoBean;
import edu.mit.coeus.negotiation.controller.NegotiationBaseWindowController;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.subcontract.controller.SubcontractBaseWindowController;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.*;
import java.beans.*;
/**
 *
 * @author  chandrashekara
 */

public class MedusaDetailForm extends edu.mit.coeus.gui.CoeusInternalFrame 
implements ActionListener,TreeSelectionListener{
    /** Holds the Institute, Development, Award and SubContract Numbers
     */
    
    private JPanel rightPanel;
    //private String proposalAwardNumber;
    private Vector vecproposalAwardDetails;
    private Vector vctDpInvestigators;
    private CoeusVector vecAwInvestigators;
    private Vector vecInstInvestigators;
    
    
    private SubContractBean subContractBean;
    private AwardBean awardBean;
    private InstituteProposalBean instituteProposalBean;
   // private ProposalAwardHierarchyBean  proposalAwardHierarchyBean;
   // private ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean;
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    private ProposalAwardHierarchyForm proposalAwardHierarchyForm;
  //  private MedusaProposalDetailForm medusaProposalDetailForm;
    
    
    private static final String MEDUSA_SERVLET = "/ProposalActionServlet";
    private static final char MEDUSA_TYPE =  'O' ;
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL + MEDUSA_SERVLET;
    
    private CoeusMenuItem display,notepad,negotiation,expandAll, collapseAll;
    private JCheckBoxMenuItem awardHierarchy,summary;
    private CoeusToolBarButton btnDisplay, btnNotepad,btnNegotiation,
                               btnExpand, btnCollapse, btnClose;
    private AbstractButton   btnAwardHierarchy,btnSummary;
    
    /** CoeusAppletMDIform to hold the base window
     */
    private CoeusAppletMDIForm mdiForm = null;
    /** Separator between the menu items
     */
    private final String SEPERATOR="seperator";
    
    
    /** Coeuse message resources instance to parse the error message
     */
    private CoeusMessageResources coeusMessageResources;
    
    private String selectedNodeId;
    
    
    private static final String OPEN_DISPLAY_MODE = "Open selected item in display mode";
    private static final String OPEN_NOTEPAD_SELECTED_ITEM = "Open Notepad for selected item";
    private static final String NEGOTIATION_FOR_INSTITUTE_PROPOSAL = 
                    "Open Negotiation window for selected institute proposal";
    private static final String SUMMARY_FOR_SELECTED_ITEM = "Display summary for selected item";
    private static final String DISPLAY_AWARD_HIERARCHY = "Display Award Hierarchy for selected award";
    private static final String EXPAND_MEDUSA_TREE = "Expand Medusa tree";
    private static final String COLLAPSE_MEDUSA_TREE = "Collapse Medusa tree";
    private static final String CLOSE = "Close";
    private static final String NEGOTIATION_CONFIRMATION= "No negotiation exists for this proposal.Do you want to create a new negotiation";
    
    private MedusaProposalDetailForm medusaDpForm = null;    
    private AwardHierarchyInMedusaForm awardHierarchyMedusa= null;
    private MedusaInvestigatorUnitForm medusaInvestigatorUnitForm =  null;
    private MedusaAwardDetailForm medusaAwardDetailForm=null;
    private MedusaInstPropDetailForm  medusaInstPropDetailForm=null;
    private MedusaPropSubContract medusaPropSubContract = null;
    private Hashtable htMedusa;
    private boolean treeDataAvailable;
    private ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean;
    //Code added for Case#3388 - Implementing authorization check at department level - starts
    private static final char CAN_VIEW_AWARD =  'f' ;
    private static final char CAN_VIEW_INST_PROPOSAL = 'w';
    //Code added for Case#3388 - Implementing authorization check at department level - ends
    // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - Start
    private static final char CAN_VIEW_DEV_PROPOSAL = '0';
    private static final char CHECK_CAN_VIEW_SUB_CONTRACT = 'j';
    private static final char CAN_MODIFY_NEGOTIATION = 'K';
    // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - End
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private MedusaIRBProtocolForm medusaIRBProtocolForm = null;
    private ProtocolInfoBean irbProtocolInfoBean;
    private MedusaIACUCProtocolForm medusaIACUCProtocolForm = null;
    private edu.mit.coeus.iacuc.bean.ProtocolInfoBean iacucProtocolInfoBean;
    //COEUSQA:2653 - End
    /** Creates a new instance of MedusaDetailForm
     * @param mdiForm
     * @param selectedNodeId
     * @throws Exception
     */
    
    
    public MedusaDetailForm(CoeusAppletMDIForm mdiForm, ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean) throws Exception{
        super("Medusa - Award, Institute Proposal and Development Proposal Links", mdiForm);
        this.mdiForm = mdiForm;
       // this.selectedNodeId = selectedNodeId;
        this.proposalAwardHierarchyLinkBean = proposalAwardHierarchyLinkBean;
        initComponents();
        proposalAwardHierarchyForm.setDefaultFocusForComponent();
        
        mdiForm.putFrame(CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE, this);
        mdiForm.getDeskTopPane().add(this);
    }

    /**
     * @param mdiForm
     * @param htValues
     * @param selectedNodeId
     * @throws Exception
     */    
    public MedusaDetailForm(CoeusAppletMDIForm mdiForm, Hashtable htValues, String selectedNodeId) throws Exception{
        super("Medusa - Award, Institute Proposal and Development Proposal Links", mdiForm);
        this.mdiForm = mdiForm;
        this.selectedNodeId = selectedNodeId;
        this.htMedusa = htValues;
        this.treeDataAvailable = true;
        initComponents();
        proposalAwardHierarchyForm.setDefaultFocusForComponent();
        
        mdiForm.putFrame(CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE, this);
        mdiForm.getDeskTopPane().add(this);
    }
    
    // Call this constructor when opening from the notepad window
    
    /**
     * @param mdiForm
     * @param htValues
     * @param selectedNodeId
     * @param proposalAwardHierarchyLinkBean
     * @throws Exception
     */    
    public MedusaDetailForm(CoeusAppletMDIForm mdiForm, Hashtable htValues,
    String selectedNodeId, ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean ) throws Exception{
        
        super("Medusa - Award, Institute Proposal and Development Proposal Links", mdiForm);
        this.mdiForm = mdiForm;
        this.selectedNodeId = selectedNodeId;
        this.htMedusa = htValues;
        this.treeDataAvailable = true;
        this.proposalAwardHierarchyLinkBean = proposalAwardHierarchyLinkBean;
        initComponents();
        proposalAwardHierarchyForm.setDefaultFocusForComponent();
        
        mdiForm.putFrame(CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE, this);
        mdiForm.getDeskTopPane().add(this);
    }
   
    
    /** Initialize all the components
     * @throws Exception
     */    
    private void initComponents() throws Exception{
        coeusMessageResources = CoeusMessageResources.getInstance();
        setFrameMenu(buildViewMenu());
        final JToolBar medusaToolBar = buildToolBar();
        this.setFrameToolBar(medusaToolBar);
        this.setFrame(CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE);
        this.setFrameIcon(mdiForm.getCoeusIcon());
        
        medusaInvestigatorUnitForm = new MedusaInvestigatorUnitForm(); 
        medusaDpForm = new MedusaProposalDetailForm(mdiForm); 
//        awardHierarchyMedusa = new AwardHierarchyInMedusaForm(mdiForm);
        
        medusaAwardDetailForm = new MedusaAwardDetailForm(mdiForm); 
        medusaInstPropDetailForm = new MedusaInstPropDetailForm(mdiForm);
        
        medusaPropSubContract = new MedusaPropSubContract(mdiForm);
        
        medusaIRBProtocolForm = new MedusaIRBProtocolForm(mdiForm);
        medusaIACUCProtocolForm = new MedusaIACUCProtocolForm(mdiForm);
        
        
        getContentPane().setLayout(new BorderLayout());
        //JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());
        
        proposalAwardHierarchyForm = new ProposalAwardHierarchyForm();
        proposalAwardHierarchyForm.setTreeSelectionListener(this);
        if( !treeDataAvailable ) {
            //proposalAwardHierarchyForm.construct(selectedNodeId);
            proposalAwardHierarchyForm.construct(proposalAwardHierarchyLinkBean);
        }else{
            proposalAwardHierarchyForm.construct(htMedusa,selectedNodeId,proposalAwardHierarchyLinkBean);
        }
        
        getContentPane().add(proposalAwardHierarchyForm,BorderLayout.WEST);
        getContentPane().add(rightPanel,BorderLayout.CENTER);
        
    }
    
    /** Build the View menu and its menu items */    
    private CoeusMenu buildViewMenu(){
        CoeusMenu mnuMedusa = null;
        Vector fileChildren = new Vector();
        
        // For Display menu item
        display = new CoeusMenuItem("Display...",null,true,true);
        display.setMnemonic('D');
        //display.addActionListener(this);
        
        // for Notepad menu item
        notepad = new CoeusMenuItem("Notepad...",null,true,true);
        notepad.setMnemonic('t');
        //notepad.addActionListener(this);
        
        // for Negotiation menu item
        negotiation = new CoeusMenuItem("Negotiation...",null,true,true);
        negotiation.setMnemonic('g');
        //negotiation.addActionListener(this);
        
        // for Award Hierarchy menu item
        awardHierarchy = new JCheckBoxMenuItem("Award Hierarchy",null,false);
        awardHierarchy.setMnemonic('H');
        //awardHierarchy.addActionListener(this);
        
        // for Summary menu item
        summary = new JCheckBoxMenuItem("Summary",null,true);
        summary.setMnemonic('S');
        //summary.addActionListener(this);
        
        // for Expand All menu item
        expandAll = new CoeusMenuItem("Expand All",null,true,true);
        expandAll.setMnemonic('x');
        //expandAll.addActionListener(this);
        
        // for Collapse All menu item
        collapseAll = new CoeusMenuItem("Collapse All",null,true,true);
        collapseAll.setMnemonic('l');
        //collapseAll.addActionListener(this);

        display.addActionListener(this);
        notepad.addActionListener(this);
        negotiation.addActionListener(this);
        awardHierarchy.addActionListener(this);
        summary.addActionListener(this);
        expandAll.addActionListener(this);
        collapseAll.addActionListener(this);
        
        // Add all the menu items to the vector 
        fileChildren.add(display);
        fileChildren.add(notepad);
        fileChildren.add(negotiation);
        fileChildren.add(SEPERATOR);
        fileChildren.add(awardHierarchy);
        fileChildren.add(summary);
        fileChildren.add(SEPERATOR);
        fileChildren.add(expandAll);
        fileChildren.add(collapseAll);
        
        /* This is the Main View menu which will be added to the main MDIMenu*/
        mnuMedusa = new CoeusMenu("View",null,fileChildren,true,true);
        mnuMedusa.setMnemonic('V');
        return mnuMedusa;
    }
    
    /** build the Tool bar button
     * @return JToolBar
     */    
    private JToolBar buildToolBar() {
        JToolBar toolbar = new JToolBar();
        /* This is the tool bar button to display the selected Item for the medusa*/
        btnDisplay = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),null,
        OPEN_DISPLAY_MODE);
        btnDisplay.setVerticalTextPosition(SwingConstants.TOP);
        btnDisplay.setHorizontalTextPosition(SwingConstants.CENTER);
        btnDisplay.setMargin(new Insets(-1,0,0,0));
        
        /* This is the tool bar button to display the selected Item for the notepad in medusa*/
        btnNotepad = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NOTEPAD_ICON)),null,
        OPEN_NOTEPAD_SELECTED_ITEM);
        
         /* This is the tool bar button to display the negotion window only for the
          proposal*/
        btnNegotiation = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NEGOTIATIONS_ICON)),null,
        NEGOTIATION_FOR_INSTITUTE_PROPOSAL);
        btnNegotiation.setVerticalTextPosition(SwingConstants.TOP);
        btnNegotiation.setHorizontalTextPosition(SwingConstants.CENTER);
        btnNegotiation.setMargin(new Insets(-1,0,0,0));
        
        /* This is the tool bar button to display the tree display mode .i.e., Expand mode*/
        btnExpand = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EXPAND_ALL)),null,
        EXPAND_MEDUSA_TREE);
        
        /* This is the tool bar button to display the tree display mode .i.e., Collapse mode*/
        btnCollapse = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.COLLAPSE_ALL)),null,
        COLLAPSE_MEDUSA_TREE);
         /* This is the tool bar button to display the award hierarchy for the selected award*/
        btnAwardHierarchy = new JToggleButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.AWARD_HIERARCHY)),false);
        btnAwardHierarchy.setToolTipText(DISPLAY_AWARD_HIERARCHY);
        btnAwardHierarchy.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnAwardHierarchy.setHorizontalTextPosition(SwingConstants.CENTER);
        btnAwardHierarchy.setMargin(new Insets(0,0,0,0));
        
        /* This is the tool bar button to display the award hierarchy for the selected award*/
        btnSummary = new JToggleButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_SUMMARY_ICON)),true);
        btnSummary.setToolTipText(SUMMARY_FOR_SELECTED_ITEM);
        btnSummary.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnSummary.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSummary.setMargin(new Insets(0,0,0,0));
        
        /* This is the tool bar button to close the Medusa*/
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),null,
        CLOSE);
        btnClose.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnClose.setHorizontalTextPosition(SwingConstants.CENTER);
        btnClose.setMargin(new Insets(-1,0,0,0));
        
        /** Action Listeners for all the toolbar buttons*/
        btnDisplay.addActionListener(this);
        btnNotepad.addActionListener(this);
        btnNegotiation.addActionListener(this);
        btnAwardHierarchy.addActionListener(this);
        btnSummary.addActionListener(this);
        btnExpand.addActionListener(this);
        btnCollapse.addActionListener(this);
        btnClose.addActionListener(this);
        
        
        // Adding all the toolbar buttons to the toolbar.
        toolbar.add(btnDisplay);
        toolbar.add(btnNotepad);
        toolbar.add(btnNegotiation);
        toolbar.add(btnAwardHierarchy);
        toolbar.add(btnSummary);
        toolbar.add(btnExpand);
        toolbar.add(btnCollapse);
        toolbar.add(btnClose);
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    /** An overriden method of the CoeusInternal frame */    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    /**
     * @param actionEvent
     */    
    public void actionPerformed(ActionEvent actionEvent) {
        Object actSource = actionEvent.getSource();
        if(actSource.equals(display) || actSource.equals(btnDisplay)){
            displayWindows();
        }else if(actSource.equals(notepad) || actSource.equals(btnNotepad)){
            displayNotePad();
        }else if(actSource.equals(negotiation) || actSource.equals(btnNegotiation)){
            displayNegotiation();
        }else if(actSource.equals(awardHierarchy) || actSource.equals(btnAwardHierarchy)){
            if( actSource.equals(awardHierarchy) ){
                btnAwardHierarchy.setSelected(awardHierarchy.getState());
            }else{
                awardHierarchy.setState(btnAwardHierarchy.isSelected());
            }
            displayAwardHierarchy();
        }else if(actSource.equals(summary) || actSource.equals(btnSummary)){
            if( actSource.equals(summary) ){
                btnSummary.setSelected(summary.getState());
            }else{
                summary.setState(btnSummary.isSelected());
            }
            displaySummary();
        }else if(actSource.equals(expandAll) || actSource.equals(btnExpand)){
            expandTree();
        }else if(actSource.equals(collapseAll) || actSource.equals(btnCollapse)){
            collapseTree();
        }else if(actSource.equals(btnClose)){
            close();
        }
    }
    
    /** Display the corresponding windows when selected node is award, Inst Prop or Dev
     * Prop
     */    
    private void displayWindows(){
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
        proposalAwardHierarchyForm.getSelectedObject();
        
        if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
            try{
                checkDuplicateAndShowDevProposal(proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber());
            }catch(Exception e) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
//            try{
//               
//           ProposalDetailForm proposalDetailForm = new ProposalDetailForm('D', proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber(), mdiForm);
//           proposalDetailForm.showDialogForm();
//            }catch(Exception exception){
//                exception.printStackTrace();
//            }
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.SUBCONTRACT)){
            try{
            checkDuplicateAndShowSubcontract(proposalAwardHierarchyLinkBean.getSubcontractNumber());
            }catch (Exception e){
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
            try{
                checkDuplicateAndShowAward(proposalAwardHierarchyLinkBean.getAwardNumber());
            }catch (Exception e){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
            try{
                checkDuplicateAndShowInstProposal(proposalAwardHierarchyLinkBean.getInstituteProposalNumber());
            }catch (Exception e){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
            try{
                checkDuplicateAndShowIrbProto(proposalAwardHierarchyLinkBean.getIrbProtocolNumber());
            }catch (Exception e){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
            try{
                checkDuplicateAndShowIacucProto(proposalAwardHierarchyLinkBean.getIacucProtocolNumber());
            }catch (Exception e){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }
        //COEUSQA:2653 - End
    }
    
     /** This method is used to check whether the given subcontract number is already
     *Added by chandra on 16th Nov 2004.
     * opened in the given mode or not.
     * @param refId
     * @throws Exception
     */
    
    private void checkDuplicateAndShowSubcontract(String refId) throws Exception{
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
        proposalAwardHierarchyForm.getSelectedObject();
        
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, refId, 'D');
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened 
            edu.mit.coeus.gui.CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW,refId);
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - Start
        if(!canViewSubcontract(proposalAwardHierarchyLinkBean.getSubcontractNumber())){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("subcontractBasewindow_exceptionCode.1007"));
            return;
        }
        // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - End
        SubContractBean bean= new SubContractBean();
        bean.setSubContractCode(proposalAwardHierarchyLinkBean.getSubcontractNumber());
        SubcontractBaseWindowController conntroller = new SubcontractBaseWindowController("Display Subcontract ", 'D', bean, null);
        conntroller.display();
    }

    
    /** This method is used to check whether the given Proposal number is already
     * opened in the given mode or not.
     * @param refId
     * @throws Exception
     */
    private void checkDuplicateAndShowDevProposal(String refId)throws Exception {
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
        proposalAwardHierarchyForm.getSelectedObject();
        
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, refId, 'D');
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened 
            edu.mit.coeus.gui.CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,refId);
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - Start
        if(!canViewDevProposal(proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber())){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("proposal_BaseWin_exceptionCode.7104"));
            return;
        }
        // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - End
        
        ProposalDetailForm proposalDetailForm = new ProposalDetailForm('D', proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber(), mdiForm);
        proposalDetailForm.showDialogForm();
    }
    
    /** This method is used to check whether the given Proposal number is already
     * opened in the given mode or not.
     * @param refId
     * @throws Exception
     */
    private void checkDuplicateAndShowAward(String refId)throws Exception {
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
        proposalAwardHierarchyForm.getSelectedObject();
        
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                CoeusGuiConstants.AWARD_BASE_WINDOW, refId, 'D');
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened 
            edu.mit.coeus.gui.CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.AWARD_BASE_WINDOW,refId);
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award
        if(!canViewAward(proposalAwardHierarchyLinkBean.getAwardNumber())){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
            return;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - ends
           AwardBean bean = new AwardBean();
           bean.setMitAwardNumber(proposalAwardHierarchyLinkBean.getAwardNumber());
           AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Display Award : ", 'D' , bean);
           awardBaseWindowController.display();
    }
    
    /** This method is used to check whether the given Proposal number is already
     * opened in the given mode or not.
     * @param refId
     * @throws Exception
     */
    private void checkDuplicateAndShowInstProposal(String refId)throws Exception {
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
        proposalAwardHierarchyForm.getSelectedObject();
        
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW, refId, 'D');
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened 
            edu.mit.coeus.gui.CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW,refId);
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - starts
            if(!canViewProposal(proposalAwardHierarchyLinkBean.getInstituteProposalNumber())){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("proposal_BaseWin_exceptionCode.7104"));
                return;
            }        
        //Code added for Case#3388 - Implementing authorization check at department level - ends
           InstituteProposalBean instituteProposalBean= new InstituteProposalBean();
           instituteProposalBean.setProposalNumber(proposalAwardHierarchyLinkBean.getInstituteProposalNumber());
           instituteProposalBean.setMode('D');
           InstituteProposalBaseWindowController conntroller = new InstituteProposalBaseWindowController("Display Institute Proposal ", 'D', (InstituteProposalBaseBean)instituteProposalBean);
           //This method is changed bug Fix 1674 - start
           //conntroller.display();
           
           conntroller.displayProposal();
           // bug Fix 1674 - End
    }
    
    
    
    
    /** Sspecifies the selected node and corresponding notepad details */    
    private void displayNotePad(){
         ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
         proposalAwardHierarchyForm.getSelectedObject();
        String nodeId = ""; 
        if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
            nodeId = proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber();
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
            nodeId = proposalAwardHierarchyLinkBean.getInstituteProposalNumber();
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
            nodeId = proposalAwardHierarchyLinkBean.getAwardNumber();
        }
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
            nodeId = proposalAwardHierarchyLinkBean.getIrbProtocolNumber();
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
            nodeId = proposalAwardHierarchyLinkBean.getIacucProtocolNumber();
        }
        //COEUSQA:2653 - End
        CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.NOTEPAD_FRAME_TITLE);
        if(frame == null){
            ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(proposalAwardHierarchyForm.getHtMedusa(),nodeId,mdiForm, proposalAwardHierarchyLinkBean);
            //ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(proposalAwardHierarchyLinkBean, mdiForm);
            proposalNotepadForm.display();
        }else{
            ((ProposalNotepadForm)frame).setSelectedNodeId(nodeId);
            try{
                frame.setSelected(true); 
            }catch( java.beans.PropertyVetoException pve) {
                pve.printStackTrace();
            }
        }
    }
    
   /** Open the Negotiation window, depending on the right in Display mode,
    *New Mode, or Modify Mode
    */
    private void displayNegotiation(){
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
        proposalAwardHierarchyForm.getSelectedObject();
        if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
            CoeusVector cvData = new CoeusVector();
            Hashtable data = proposalAwardHierarchyForm.getHtMedusa();
            if(data!= null){
                cvData= (CoeusVector) data.get("INSTITUTE_PROPOSAL");
                CoeusVector cvNegoTiation = cvData.filter(new Equals("instituteProposalNumber",proposalAwardHierarchyLinkBean.getInstituteProposalNumber()));
                if(cvNegoTiation!=null && cvNegoTiation.size() > 0){
                    ProposalAwardHierarchyBean bean = (ProposalAwardHierarchyBean)cvNegoTiation.get(0);
                    String negotiationNumber = proposalAwardHierarchyLinkBean.getInstituteProposalNumber();
                    if(isNegotiationOpen(negotiationNumber, CoeusGuiConstants.MODIFY_MODE)) {
                        return ;
                    }
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - Start
                    char functionType;
                    if(canModifyNegotiation(negotiationNumber)){
                        functionType = CoeusGuiConstants.MODIFY_MODE;
                    } else {
                        functionType = CoeusGuiConstants.DISPLAY_MODE;
                    }
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - End
                    NegotiationInfoBean negotiationInfoBean = new NegotiationInfoBean();
                    negotiationInfoBean.setNegotiationNumber(negotiationNumber);
                    NegotiationBaseWindowController negotiationBaseWindowController =
                        //   new NegotiationBaseWindowController(CoeusGuiConstants.MEDUSA_NEGOTIATION, CoeusGuiConstants.MODIFY_MODE, negotiationInfoBean);
                        new NegotiationBaseWindowController(CoeusGuiConstants.MEDUSA_NEGOTIATION, functionType, negotiationInfoBean);
                    negotiationBaseWindowController.display();
                }
            }
        }
    }
    
    
     /** This method is used to check whether the given Negotiation number is already
     * open in the given mode or not.
     * @param refId refId - for negotiation its Negotiation Number.
     * @param mode mode of Form open.
     * @param displayMessage if true displays error messages else doesn't.
     * @return true if Negotiation window is already open else returns false.
     */
    boolean isNegotiationOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.NEGOTIATION_DETAILS, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            // try to get the requested frame which is already opened
            CoeusInternalFrame frame = mdiForm.getFrame(
            CoeusGuiConstants.NEGOTIATION_DETAILS,refId);
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            }
            return true;
        }
        return false;
    }

    /** This method is used to check whether the given Negotiation number is already
     * open in the given mode or not and displays message if the Negotiation is open
     * @param refId refId - for Negotiation its Negotiation Number.
     * @param mode mode of Form open.
     * @return true if Negotiation window is already open else returns false.
     */
    boolean isNegotiationOpen(String refId, char mode){
        return isNegotiationOpen(refId, mode, true);
    }
    
    
    
    
    
    
    
    private void displayAwardHierarchy(){
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
        proposalAwardHierarchyForm.getSelectedObject();

        btnSummary.setSelected(false);
        summary.setSelected(false);
        
        if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
            if(btnAwardHierarchy.isSelected() || awardHierarchy.isSelected()){
                btnSummary.setSelected(false);
                summary.setSelected(false);
                
                awardHierarchyMedusa = new AwardHierarchyInMedusaForm(mdiForm);
                awardHierarchyMedusa.setAwardNumber(proposalAwardHierarchyLinkBean.getAwardNumber());
                awardHierarchyMedusa.awardHierarchyTreeInMedusa.construct(proposalAwardHierarchyLinkBean.getAwardNumber());
                awardHierarchyMedusa.awardDetailsPanelInMedusa.setFormData(proposalAwardHierarchyLinkBean.getAwardNumber());
                
                rightPanel.add(awardHierarchyMedusa, BorderLayout.NORTH);
                awardHierarchyMedusa.setVisible(true);
                
                medusaInvestigatorUnitForm.setVisible(false);
                medusaDpForm.setVisible(false);
                medusaInvestigatorUnitForm.setVisible(false);
                medusaInstPropDetailForm.setVisible(false);
                medusaAwardDetailForm.setVisible(false);
                medusaInvestigatorUnitForm.setVisible(false);
                medusaPropSubContract.setVisible(false);
                medusaIRBProtocolForm.setVisible(false);
                medusaIACUCProtocolForm.setVisible(false);
                
            }else{
                awardHierarchyMedusa.setVisible(false);
                // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - Start
//                medusaInvestigatorUnitForm.setVisible(false);
//                medusaDpForm.setVisible(false);
//                medusaInvestigatorUnitForm.setVisible(false);
//                medusaInstPropDetailForm.setVisible(false);
//                medusaAwardDetailForm.setVisible(false);
//                medusaInvestigatorUnitForm.setVisible(false);
//                medusaPropSubContract.setVisible(false);
                if(btnSummary.isSelected() || summary.isSelected()){
                    medusaInvestigatorUnitForm.setVisible(true);
                    medusaDpForm.setVisible(true);
                    medusaInvestigatorUnitForm.setVisible(true);
                    medusaInstPropDetailForm.setVisible(true);
                    medusaAwardDetailForm.setVisible(true);
                    medusaInvestigatorUnitForm.setVisible(true);
                    medusaPropSubContract.setVisible(true);
                }
                // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - End
                
            }
        }
        
    }
    
    private void displaySummary(){
//        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
//        proposalAwardHierarchyForm.getSelectedObject();
        
        if(btnSummary.isSelected() || summary.isSelected()) {
            
            btnAwardHierarchy.setSelected(false);
            awardHierarchy.setSelected(false);
            if(awardHierarchyMedusa== null){
                awardHierarchyMedusa = new AwardHierarchyInMedusaForm(mdiForm);
            }
            awardHierarchyMedusa.setVisible(false);
            
            medusaInvestigatorUnitForm.setVisible(true);
            medusaDpForm.setVisible(true);
            medusaInvestigatorUnitForm.setVisible(true);
            medusaInstPropDetailForm.setVisible(true);
            medusaAwardDetailForm.setVisible(true);
            medusaInvestigatorUnitForm.setVisible(true); 
            medusaPropSubContract.setVisible(true);
            medusaIRBProtocolForm.setVisible(true);
            medusaIACUCProtocolForm.setVisible(true);
            
//            if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
//                medusaInvestigatorUnitForm.setVisible(true);
//                medusaDpForm.setVisible(true);
//            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
//                medusaInvestigatorUnitForm.setVisible(true);
//                medusaInstPropDetailForm.setVisible(true);
//            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
//                medusaAwardDetailForm.setVisible(true);
//                medusaInvestigatorUnitForm.setVisible(true);
//            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.SUBCONTRACT)){
//                medusaPropSubContract.setVisible(true);
//            }
        }else {
                
            medusaInvestigatorUnitForm.setVisible(false);
            medusaDpForm.setVisible(false);
            medusaInvestigatorUnitForm.setVisible(false);
            medusaInstPropDetailForm.setVisible(false);
            medusaAwardDetailForm.setVisible(false);
            medusaInvestigatorUnitForm.setVisible(false); 
            medusaPropSubContract.setVisible(false);
            // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
            if(btnAwardHierarchy.isSelected() || awardHierarchy.isSelected()){
                if(awardHierarchyMedusa != null){
                    awardHierarchyMedusa.setVisible(true);
                }
            }
            // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
        }
    }
    
    private void expandTree(){
//        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
//        proposalAwardHierarchyForm.getSelectedObject();
        
       proposalAwardHierarchyForm.expandAll(proposalAwardHierarchyForm.treeProposalAwardHierarchy, true);
    }
    
    
    private void collapseTree(){
        ProposalAwardHierarchyNode  proposalAwardHierarchyNode =
        (ProposalAwardHierarchyNode)proposalAwardHierarchyForm.treeProposalAwardHierarchy.getLastSelectedPathComponent(),
        parentNode = null;
        
        ProposalAwardHierarchyNode node =
        (ProposalAwardHierarchyNode)((ProposalAwardHierarchyNode)
        proposalAwardHierarchyForm.treeProposalAwardHierarchy.getModel().getRoot()).getFirstChild();
        
        /** When tree is in expand state and click on Collapse All button then the 
         *parent of the selected node has to be selected in the collapse All action.
         */
        while(node!=null){
            TreePath nodePath = new TreePath(node);
            proposalAwardHierarchyForm.treeProposalAwardHierarchy.collapsePath(nodePath);
            node = (ProposalAwardHierarchyNode)node.getNextSibling();
        }
        (( DefaultTreeModel )proposalAwardHierarchyForm.treeProposalAwardHierarchy.getModel() ).reload();
        
        TreeNode [] path = proposalAwardHierarchyNode.getPath();
        if(path!=null && path.length > 1){
            parentNode = (ProposalAwardHierarchyNode)path[1];
            proposalAwardHierarchyForm.treeProposalAwardHierarchy.setSelectionPath(new TreePath(parentNode.getPath()));
        }
    }
    
    private void close(){
        this.doDefaultCloseAction();
    }
    
    /** Getter for property selectedNodeId.
     * @return Value of property selectedNodeId.
     *
     */
    public java.lang.String getselectedNodeId() {
        return selectedNodeId;
    }
    
    /** Setter for property selectedNodeId.
     * @param selectedNodeId New value of property selectedNodeId.
     *
     */
    public void setSelectedNodeId(java.lang.String nodeId) {
        this.selectedNodeId = nodeId;
        proposalAwardHierarchyForm.setSelectedNodeId(nodeId);
    }
    
    
 
    /** When the each node is selected, for each node there is a  separate server 
     *side call. For each Inst Prop, Dev Prop, Award and SubContract there is a 
     *call. Here the right checkings are done. If the selected node doesn't 
     *contain the right then the table and details panel will be empty.
     *vec(0) returns the bean details and vec(1) returns the right details
     */
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        try{
            rightPanel.removeAll();
            
            ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =  proposalAwardHierarchyForm.getSelectedObject();
            if( proposalAwardHierarchyLinkBean != null ) {
                if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
                    /* When the subcontract is selected then the button notepad and menu item 
                     *notepad has to be disabled. Else for all other it has to be enabled
                     */
                    btnNotepad.setEnabled(true);
                    notepad.setEnabled(true);
                    
                    vecproposalAwardDetails = getFormData();
                    proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)vecproposalAwardDetails.get(0);
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- Start
                    Boolean hasViewRight =  (Boolean)vecproposalAwardDetails.get(1);
                    if(hasViewRight.booleanValue()){
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- End
                        medusaDpForm.showValues(proposalDevelopmentFormBean);
                        
                        vctDpInvestigators = proposalDevelopmentFormBean.getInvestigators();
                        medusaInvestigatorUnitForm.setDataValues(vctDpInvestigators, proposalAwardHierarchyLinkBean);
                        medusaInvestigatorUnitForm.setFormData();
                        
                        rightPanel.add(medusaDpForm, BorderLayout.CENTER);
                        rightPanel.add(medusaInvestigatorUnitForm,BorderLayout.SOUTH);
                    }
                    
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
                    /* When the subcontract is selected then the button notepad and menu item
                     *notepad has to be disabled. Else for all other it has to be enabled
                     */
                   
                    btnNotepad.setEnabled(true);
                    notepad.setEnabled(true);
                    
                    vecproposalAwardDetails = getFormData();
                    instituteProposalBean = (InstituteProposalBean)vecproposalAwardDetails.get(0);
                    Boolean hasRight = (Boolean)vecproposalAwardDetails.get(1);
                    if(hasRight.booleanValue()){
                        medusaInstPropDetailForm.setDataValues(instituteProposalBean);
                        vecInstInvestigators = instituteProposalBean.getInvestigators();
                        medusaInvestigatorUnitForm.setDataValues(vecInstInvestigators, proposalAwardHierarchyLinkBean);
                        medusaInvestigatorUnitForm.setFormData();
                        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        centerPanel.add(medusaInstPropDetailForm);
                        rightPanel.add(centerPanel, BorderLayout.CENTER);

                        rightPanel.add(centerPanel, BorderLayout.CENTER);
                        rightPanel.add(medusaInvestigatorUnitForm,BorderLayout.SOUTH);
                    }
                    
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                    /* When the subcontract is selected then the button notepad and menu item
                     *notepad has to be disabled. Else for all other it has to be enabled
                     */
                    btnNotepad.setEnabled(true);
                    notepad.setEnabled(true);
                    
                    vecproposalAwardDetails = getFormData();
                    awardBean = (AwardBean)vecproposalAwardDetails.get(0);
                    Boolean hasRight = (Boolean)vecproposalAwardDetails.get(1);
                    if(hasRight.booleanValue()){
                        if((!awardHierarchy.isSelected()) || (!btnAwardHierarchy.isSelected())){
                            medusaAwardDetailForm.showValues(awardBean);
                            vecAwInvestigators = awardBean.getAwardInvestigators();
                            medusaInvestigatorUnitForm.setDataValues(vecAwInvestigators, proposalAwardHierarchyLinkBean);
                            medusaInvestigatorUnitForm.setFormData();
                            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                            centerPanel.add(medusaAwardDetailForm);
                            rightPanel.add(centerPanel, BorderLayout.CENTER);
                            rightPanel.add(medusaInvestigatorUnitForm,BorderLayout.SOUTH);
                        }
                        if(awardHierarchy.isSelected() || btnAwardHierarchy.isSelected()){
                             btnSummary.setSelected(false);
                             summary.setSelected(false);
                             if(awardHierarchyMedusa == null){
                                 awardHierarchyMedusa = new AwardHierarchyInMedusaForm(mdiForm);
                             }
                            awardHierarchyMedusa.setAwardNumber(proposalAwardHierarchyLinkBean.getAwardNumber());
                            awardHierarchyMedusa.awardHierarchyTreeInMedusa.construct(proposalAwardHierarchyLinkBean.getAwardNumber());
                            awardHierarchyMedusa.awardDetailsPanelInMedusa.setFormData(proposalAwardHierarchyLinkBean.getAwardNumber());
                            
                            medusaInvestigatorUnitForm.setVisible(false);
                            medusaAwardDetailForm.setVisible(false);
                            medusaInstPropDetailForm.setVisible(false);
                            
                            rightPanel.add(awardHierarchyMedusa, BorderLayout.NORTH);
                             awardHierarchyMedusa.setVisible(true);
                        }
                    }
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.SUBCONTRACT)){
                    /* When the subcontract is selected then the button notepad and menu item
                     *notepad has to be disabled. Else for all other it has to be enabled
                     */
                    btnNotepad.setEnabled(false);
                    notepad.setEnabled(false);
                    
                    vecproposalAwardDetails = getFormData();
                    subContractBean = (SubContractBean)vecproposalAwardDetails.get(0);
                    Boolean hasRight = (Boolean)vecproposalAwardDetails.get(1);
                    if(hasRight.booleanValue()){
                        medusaPropSubContract.setDataValues(subContractBean);
                        rightPanel.add(medusaPropSubContract, BorderLayout.CENTER);
                    }
                }
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
                    
                    btnNotepad.setEnabled(true);
                    notepad.setEnabled(true);
                    
                    vecproposalAwardDetails = getFormData();
                    irbProtocolInfoBean = (ProtocolInfoBean)vecproposalAwardDetails.get(0);
                    Boolean hasRight = (Boolean)vecproposalAwardDetails.get(1);
                    if(hasRight.booleanValue()){
                        medusaIRBProtocolForm.setDataValues(irbProtocolInfoBean);
                        vecInstInvestigators = irbProtocolInfoBean.getInvestigators();
                        medusaInvestigatorUnitForm.setDataValues(vecInstInvestigators, proposalAwardHierarchyLinkBean);
                        medusaInvestigatorUnitForm.setFormData();
                        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        centerPanel.add(medusaIRBProtocolForm);
                        rightPanel.add(centerPanel, BorderLayout.CENTER);
                        rightPanel.add(centerPanel, BorderLayout.CENTER);
                        rightPanel.add(medusaInvestigatorUnitForm,BorderLayout.SOUTH);
                    }
                    
                } else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
                    
                    btnNotepad.setEnabled(true);
                    notepad.setEnabled(true);
                    
                    vecproposalAwardDetails = getFormData();
                    iacucProtocolInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)vecproposalAwardDetails.get(0);
                    Boolean hasRight = (Boolean)vecproposalAwardDetails.get(1);
                    if(hasRight.booleanValue()){
                        medusaIACUCProtocolForm.setDataValues(iacucProtocolInfoBean);
                        vecInstInvestigators = iacucProtocolInfoBean.getInvestigators();
                        medusaInvestigatorUnitForm.setDataValues(vecInstInvestigators, proposalAwardHierarchyLinkBean);
                        medusaInvestigatorUnitForm.setFormData();
                        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        centerPanel.add(medusaIACUCProtocolForm);
                        rightPanel.add(centerPanel, BorderLayout.CENTER);
                        rightPanel.add(centerPanel, BorderLayout.CENTER);
                        rightPanel.add(medusaInvestigatorUnitForm,BorderLayout.SOUTH);
                    }
                    
                }
                //COEUSQA:2653 - End
            }
        }catch(CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }
    }
    
   
    /** Contact server to get the selected object details. Returns a vector, 
     *which contains selected node details and right details
     *vec(0) returns node details and vec(1) returns right details
     */
    private Vector getFormData() throws CoeusClientException{
        ProposalAwardHierarchyLinkBean propAwardHierarchyLinkBean;
        propAwardHierarchyLinkBean = proposalAwardHierarchyForm.getSelectedObject();
        Vector vecDetails=null;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(MEDUSA_TYPE);
        request.setDataObject(propAwardHierarchyLinkBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            //System.out.println("SuccessFull Response in MedusaDetailsForm");
            vecDetails = response.getDataObjects();
            return vecDetails;
        }else {
            //System.out.println("Error while loading Details in MedusaDetailsForm");
            throw new CoeusClientException(response.getMessage());
        }
    }
    

    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this award
     * @param awardNumber
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewAward(String awardNumber) throws CoeusClientException{
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_VIEW_AWARD);
        request.setDataObject(awardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }else {
            //System.out.println("Error while loading Details in MedusaDetailsForm");
            throw new CoeusClientException(response.getMessage());
        }
        return canView;
    }
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this institute proposal
     * @param institute proposal number
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewProposal(String instituteProposalNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_VIEW_INST_PROPOSAL);
        request.setDataObject(instituteProposalNumber);
        String connect = CoeusGuiConstants.CONNECTION_URL +  "/InstituteProposalMaintenanceServlet" ;        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
    }    
   // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - Start
    /**
     * To check the user has right to view the Subcontract
     * @param String subContractNumber
     * @return boolean
     */    
    private boolean canViewSubcontract(String subContractNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CHECK_CAN_VIEW_SUB_CONTRACT);
        request.setDataObject(subContractNumber);
        String connect = CoeusGuiConstants.CONNECTION_URL +"/SubcontractMaintenenceServlet";        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
        
    }
      /**
     * To check the user has right to view Development proposal
     * @param String devProposalNumber
     * @return boolean
     */    
    private boolean canViewDevProposal(String devProposalNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_VIEW_DEV_PROPOSAL);
        request.setDataObject(devProposalNumber);
        String connect = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
    }
    
    private boolean canModifyNegotiation(String negotiationNumber){
        boolean hasModifyRights = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_MODIFY_NEGOTIATION);
        request.setDataObject(negotiationNumber);
        String connect = CoeusGuiConstants.CONNECTION_URL + "/NegotiationMaintenanceServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            hasModifyRights = ((Boolean)response.getDataObject()).booleanValue();
        }
        return hasModifyRights;
    }
   // COEUSDEV-185	Proposal Development and Subawards are viewable from medusa without appropriate role - End
    
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    /**
     * Method to Check Duplicate window opened and if it is opened, it will show the IRB Protocol window
     * @param refId 
     * @throws java.lang.Exception 
     */
    private void checkDuplicateAndShowIrbProto(String refId)throws Exception {
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
                proposalAwardHierarchyForm.getSelectedObject();
        
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                    CoeusGuiConstants.PROTOCOL_FRAME_TITLE, refId, 'D');
        }catch(Exception e){
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            edu.mit.coeus.gui.CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.PROTOCOL_FRAME_TITLE,refId);
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }             
        mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        ProtocolDetailForm mainProtocol;
        mainProtocol = new ProtocolDetailForm( 'D', proposalAwardHierarchyLinkBean.getIrbProtocolNumber(), mdiForm);
        mainProtocol.showDialogForm();        
    }
    
    /**
     * Method to Check Duplicate window opened and if it is opened, it will show the IACUC Protocol window
     * @param refId 
     * @throws java.lang.Exception 
     */
    private void checkDuplicateAndShowIacucProto(String refId)throws Exception {
        ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean =
                proposalAwardHierarchyForm.getSelectedObject();
        
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                    CoeusGuiConstants.PROTOCOL_FRAME_TITLE, refId, 'D');
        }catch(Exception e){
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            edu.mit.coeus.gui.CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.PROTOCOL_FRAME_TITLE,refId);
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }             
        mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        edu.mit.coeus.iacuc.gui.ProtocolDetailForm mainProtocol;
        mainProtocol = new edu.mit.coeus.iacuc.gui.ProtocolDetailForm( 'D', proposalAwardHierarchyLinkBean.getIacucProtocolNumber(), mdiForm);
        mainProtocol.showDialogForm();        
    }
    //COEUSQA:2653 - End
}

