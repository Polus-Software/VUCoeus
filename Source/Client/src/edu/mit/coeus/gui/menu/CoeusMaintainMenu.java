/*
 * @(#)CoeusMaintainMenu.java 1.0 10/18/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.irb.gui.CommitteeBaseWindow;
import edu.mit.coeus.irb.gui.ScheduleBaseWindow;
//import edu.mit.coeus.irb.gui.AreaOfResearchBaseWindow;
import edu.mit.coeus.irb.gui.ProtocolBaseWindow;
import edu.mit.coeus.irb.gui.ProtoCorrespTypeBaseWindow;
import edu.mit.coeus.propdev.gui.ProposalBaseWindow;
import edu.mit.coeus.sponsormaint.gui.SponsorBaseWindow;
import edu.mit.coeus.rolodexmaint.gui.RolodexBaseWindow;
import edu.mit.coeus.irb.gui.SubmissionBaseWindow;
import edu.ucsd.coeus.personalization.controller.AbstractController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.award.controller.AwardListController;
import edu.mit.coeus.award.gui.AwardList;
import edu.mit.coeus.negotiation.controller.NegotiationListController;
import edu.mit.coeus.award.controller.AwardReportingReqController;
import edu.mit.coeus.sponsorhierarchy.controller.SponsorHierarchyListController;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.admin.controller.SubcontractReportListController;
import edu.mit.coeus.admin.gui.SubcontractReportListForm;

import edu.mit.coeus.instprop.controller.InstituteProposalListController;
import edu.mit.coeus.instprop.gui.InstituteProposalListForm;
import edu.mit.coeus.negotiation.gui.NegotiationListForm;
import edu.mit.coeus.instprop.controller.ProposalLogBaseWindowController;
import edu.mit.coeus.instprop.gui.ProposalLogBaseWindow;
import edu.mit.coeus.subcontract.controller.SubcontractListController;
import edu.mit.coeus.subcontract.gui.SubcontractListForm;
import edu.mit.coeus.sponsorhierarchy.gui.SponsorHierarchyListForm;
import java.beans.PropertyVetoException;



/**
 * This class creates Maintain menu for the coeus application.
 *
 * @version :1.0 October 18, 2002, 3:11 PM
 * @author Guptha
 */

public class CoeusMaintainMenu extends JMenu implements ActionListener{
    
    /*
     * maintain menu items used by CoeusMaintainListener
     */
    public CoeusMenuItem awards, proposals, proposalsDevelopment, proposalsLog,
    rolodex, sponsor, sponsorHierarchy, subcontract, negotiations,
    financialInterestDisclosure, awardReportingRequirements,
    subContractingReports,irb;
    
    /*
     *irb menu items
     */
    /* JM 05-02-2013
    public CoeusMenuItem committee,areaOfResearch,
    	schedule, protocol,
            // COEUSQA-1724: IACUC module
            iacucProtocol, iacucProtocolSubmissions,iacucCorrespondence;
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    private CoeusMenu coeusMenu;
    
    private SubcontractReportListController controller;
    
    private CoeusAppletMDIForm mdiForm;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private AwardReportingReqController awardReportingReqController = null;
    
    /** Default constructor which constructs the maintain menu for coeus application.
     * @param mdiForm CoeusAppletMDIForm
     */
    public CoeusMaintainMenu(CoeusAppletMDIForm mdiForm){
        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createMenu();
    }
    
    /**
     * This method gets the maintain menu
     *
     * @return JMenu coeus maintain menu
     */
    public JMenu getMenu(){
        return coeusMenu;
    }
    
    /**
     * This method is used to create maintain menu for coeus application.
     */
    private void createMenu(){
        Vector fileChildren = new Vector();
        awards = new CoeusMenuItem("Awards",null,true,true);
        awards.setMnemonic('A');
        proposals = new CoeusMenuItem("Institute Proposals",null,true,true);
        proposals.setMnemonic('P');
        proposalsDevelopment = new CoeusMenuItem(
        "Proposal Development ...",null,true,true);
        proposalsDevelopment.setMnemonic('D');
        proposalsLog = new CoeusMenuItem("Proposal Log...",null,true,true);
        proposalsLog.setMnemonic('L');
        rolodex = new CoeusMenuItem("Rolodex...",null,true,true);
        rolodex.setMnemonic('x');
        sponsor = new CoeusMenuItem("Sponsor...",null,true,true);
        sponsor.setMnemonic('n');
        sponsorHierarchy = new CoeusMenuItem("Sponsor Hierarchy...",null,true,true);
        sponsorHierarchy.setMnemonic('H');
        
        subcontract = new CoeusMenuItem("Subcontract...",null,true,true);
        subcontract.setMnemonic('U');
        negotiations = new CoeusMenuItem("Negotiations...",null,true,true);
        negotiations.setMnemonic('G');
        financialInterestDisclosure = new CoeusMenuItem(
        "Financial Interest Disclosure...",null,true,true);
        financialInterestDisclosure.setMnemonic('F');
        awardReportingRequirements = new CoeusMenuItem(
        "Award Reporting Requirements...",null,true,true);
        awardReportingRequirements.setMnemonic('q');
        subContractingReports = new CoeusMenuItem(
        "Subcontracting Reports",null,true,true);
        subContractingReports.setMnemonic('B');
        
        
        // Vector irbChildren = new Vector();
        /* JM 05-02-2013
        committee = new CoeusMenuItem("Committee...",null,true,true);
        committee.setMnemonic('O'); */
        
        //Adding AreaOfResearch Menu Item
  /*      areaOfResearch = new CoeusMenuItem("Area Of Research", null, true, true);
        areaOfResearch.setMnemonic('E');*/  //Moved to CoeusAdminMenu.java
        
        /* JM 05-02-2013
        //Modified for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium_Start
        protocol = new CoeusMenuItem("IRB Protocol", null, true, true);
        //Modified for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium_End
        protocol.setMnemonic('T');
        /*
        iacucProtocol = new CoeusMenuItem("IACUC Protocol", null, true, true);
        iacucProtocol.setMnemonic('I'); */
         //Modified for COEUSQA-2588 -Reorder IACUC and IRB items from maintain menu in Premium -start
//        schedule = new CoeusMenuItem("Schedules...",null,true,true);
        /* JM 05-02-2013
        schedule = new CoeusMenuItem("Schedule...",null,true,true);//COEUSQA-2588 : End
        schedule.setMnemonic('S'); */
        
        /* JM 05-02-2013
        irbCorrespondence = new CoeusMenuItem("IRB Correspondence...",null,true,true);
        irbCorrespondence.setMnemonic('r');
        //Modified for COEUSQA-2588 -Reorder IACUC and IRB items from maintain menu in Premium -start
//        irbProtocolSubmission = new CoeusMenuItem("IRB Protocol Submissions",null,true,true);
        irbProtocolSubmission = new CoeusMenuItem("IRB Protocol Submission",null,true,true);//COEUSQA-2588 : End
        irbProtocolSubmission.setMnemonic('m');
        //Modified for COEUSQA-2588 -Reorder IACUC and IRB items from maintain menu in Premium -start
//        iacucProtocolSubmissions = new CoeusMenuItem("IACUC Protocol Submissions",null,true,true);
        iacucProtocolSubmissions = new CoeusMenuItem("IACUC Protocol Submission",null,true,true);//COEUSQA-2588 : End
        iacucProtocolSubmissions.setMnemonic('c');
        
        iacucCorrespondence = new CoeusMenuItem("IACUC Correspondence...",null,true,true);
        iacucCorrespondence.setMnemonic('r'); */

        /*irbChildren.add( areaOfResearch );
        irbChildren.add( protocol );
        /*irbChildren.add(committee);
        irbChildren.add(schedule);
        CoeusMenu irb = new CoeusMenu("IRB...",null,irbChildren,true,true);
        irb.setMnemonic('I');*/
        
        fileChildren.add(awards);
        fileChildren.add(SEPERATOR);
        fileChildren.add(proposals);
        fileChildren.add(proposalsDevelopment);
        fileChildren.add(SEPERATOR);
        
        //fileChildren.add(areaOfResearch);  moved to CoeusAdminMenu.java      
        //Added for COEUSQA-2588 -Reorder IACUC and IRB items from maintain menu in Premium -start
        /* JM 05-02-2013
        fileChildren.add(committee);
        fileChildren.add(schedule);
        fileChildren.add(protocol);
        fileChildren.add(irbCorrespondence);
        fileChildren.add(irbProtocolSubmission);
        fileChildren.add(iacucProtocol);             
        fileChildren.add(iacucCorrespondence);        
        fileChildren.add(iacucProtocolSubmissions);
        fileChildren.add(SEPERATOR);
        */
        //Added for COEUSQA-2588 -Reorder IACUC and IRB items from maintain menu in Premium -end
        
        fileChildren.add(proposalsLog);
        fileChildren.add(SEPERATOR);
        fileChildren.add(rolodex);
        fileChildren.add(sponsor);
        fileChildren.add(sponsorHierarchy);
        //fileChildren.add(irb);
        //fileChildren.add(SEPERATOR);
        fileChildren.add(subcontract);
        fileChildren.add(negotiations);
        //bug fix done by shiji - bug id:1846 - start
        //fileChildren.add(financialInterestDisclosure);
        //bug id : 1846 - end
        fileChildren.add(awardReportingRequirements );
        fileChildren.add(SEPERATOR);
        fileChildren.add(subContractingReports);
        
        
        
        coeusMenu = new CoeusMenu("Maintain",null,fileChildren,true,true);
        coeusMenu.setMnemonic('M');
        
        //add listeners
        awards.addActionListener(this);
        proposals.addActionListener(this);
        proposalsDevelopment.addActionListener(this);
        proposalsLog.addActionListener(this);
        rolodex.addActionListener(this);
        sponsor.addActionListener(this);
        sponsorHierarchy.addActionListener(this);
        subcontract.addActionListener(this);
        negotiations.addActionListener(this);
        financialInterestDisclosure.addActionListener(this);
        awardReportingRequirements.addActionListener(this);
        subContractingReports.addActionListener(this);
        //areaOfResearch.addActionListener(this);
        /* JM 05-02-2013
        committee.addActionListener(this);*/
        //protocol.addActionListener(this);
        /*iacucProtocol.addActionListener(this);
        schedule.addActionListener(this);
        irbCorrespondence.addActionListener(this);
        iacucCorrespondence.addActionListener(this);
        irbProtocolSubmission.addActionListener(this);
        iacucProtocolSubmissions.addActionListener(this);
        */
    }
    
    /** This method is used to handle the action event for the maintain menu items.
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae){
        try{
            //BUG FIX: 1063 Hour glass implementation 
            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            if /* JM 05-02-2013 (ae.getSource().equals(committee)){
                showCommittee();
            }else if */(ae.getSource().equals(rolodex)){
                showRolodex();
            }else if (ae.getSource().equals(sponsor)){
                showSponsor();
            /*else if (ae.getSource().equals(areaOfResearch)){
                showAreaOfResearch();*/
            /* JM 05-02-2013
            }else if (ae.getSource().equals(protocol)){
                showProtocol();
            }else if (ae.getSource().equals(iacucProtocol)){
                showIacucProtocol();
            */
            }else if (ae.getSource().equals(proposalsDevelopment)){
                showProposalDev();
            /* JM 05-02-2013
            }else if (ae.getSource().equals(schedule)){
                showSchedule();
            }else if (ae.getSource().equals(irbCorrespondence)){
                showIRBCorrespondenceTypes();
            }else if (ae.getSource().equals(iacucCorrespondence)){
                showIACUCCorrespondenceTypes();
            }else if( ae.getSource().equals(irbProtocolSubmission)){
                showSubmissionDetails();
            } else if(ae.getSource().equals(iacucProtocolSubmissions)){
                showIacucProtocolSubmissionDetails();
            */
            }else if(ae.getSource().equals(awards)) {
                showAwards();
            }else if(ae.getSource().equals(proposals)){
                showProposal();
            }else if( ae.getSource().equals(proposalsLog)){
                showProposalLog();
            }else if( ae.getSource().equals(negotiations)){
                showNegotiation();
            } else if (ae.getSource().equals(awardReportingRequirements)) {
                showAwardReportingRequirements();
            }else if (ae.getSource().equals(subcontract)) {
                showSubcontract();
            }else if(ae.getSource().equals(sponsorHierarchy)) {
                showSponsorHierarchy();
            }else if(ae.getSource().equals(subContractingReports)){
                showSubcontractionReports();
            }else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                "funcNotImpl_exceptionCode.1100"));
            }
            // added by manoj to display the authorization messages for
            //irbCorrespondence as information messages
        }catch(CoeusException ex){
            ex.printStackTrace();
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch(Exception ex){
            ex.printStackTrace();
            
            //Commented by Ajay - Modification done for Exception Handling 
            //Since the Code was changed in AppletServletCommunicator to 
            //show Server down message, the below code is commented.
            
            //CoeusOptionPane.showInfoDialog(ex.getMessage());
        }
        mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }
    // Added by chandra for the subcontracting Reports
    private void showSubcontractionReports() throws Exception{
        SubcontractReportListForm subcontractReportListForm= null;
        if( ( subcontractReportListForm = (SubcontractReportListForm)mdiForm.getFrame(
        CoeusGuiConstants.SUBCONTRACTING_REPORTS))!= null ){
            if( subcontractReportListForm.isIcon() ){
                subcontractReportListForm.setIcon(false);
            }
            subcontractReportListForm.setSelected( true );
            return;
        }
        controller = new SubcontractReportListController(mdiForm, CoeusGuiConstants.SUBCONTRACTING_REPORTS);
        controller.initComponents();
        controller.registerComponents();
        controller.setFormData();
        controller.display();
    }
    
    private void showAwards()throws Exception {
        AwardList awardList= null;
        if( ( awardList = (AwardList)mdiForm.getFrame(
        CoeusGuiConstants.AWARD_FRAME_TITLE))!= null ){
            if( awardList.isIcon() ){
                awardList.setIcon(false);
            }
            awardList.setSelected( true );
            return;
        }
        AwardListController awardListController = new AwardListController();
        awardListController.display();
    }
    
    /** Displays the Proposal Log List
     * @throws Exception
     */
    private void showProposalLog() throws Exception {
        ProposalLogBaseWindow proposalLogBaseWindow = null;
        if( ( proposalLogBaseWindow = (ProposalLogBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.PROPOSAL_LOG_FRAME_TITLE))!= null ){
            if( proposalLogBaseWindow.isIcon() ){
                proposalLogBaseWindow.setIcon(false);
            }
            proposalLogBaseWindow.setSelected( true );
            return;
        }
        ProposalLogBaseWindowController proposalLogBaseWindowController = new ProposalLogBaseWindowController();
        
        // rdias UCSD - Coeus personalization impl
        AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_module(proposalLogBaseWindowController.getControlledUI(),
        		proposalLogBaseWindowController.getControlledUI(),this,"GENERIC");
        // rdias UCSD - Coeus personalization impl
        
        proposalLogBaseWindowController.display();
    }
	
	private void showSubcontract()throws Exception {
       SubcontractListForm subcontractListForm= null;
       if( ( subcontractListForm = (SubcontractListForm)mdiForm.getFrame(
        CoeusGuiConstants.SUBCONTRACT_FRAME_TITLE))!= null ){
            if( subcontractListForm.isIcon() ){
                subcontractListForm.setIcon(false);
            }
            subcontractListForm.setSelected( true );
            return;
        }
        SubcontractListController subcontractController= new SubcontractListController();
        subcontractController.display();
    }
    
    private void showProposal()throws Exception {
        InstituteProposalListForm instituteProposalListForm= null;
        if( ( instituteProposalListForm = (InstituteProposalListForm)mdiForm.getFrame(
        CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE))!= null ){
            if( instituteProposalListForm.isIcon() ){
                instituteProposalListForm.setIcon(false);
            }
            instituteProposalListForm.setSelected( true );
            return;
        }
        InstituteProposalListController proposalController= new InstituteProposalListController();
        
        // rdias UCSD - Coeus personalization impl
        AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_module(proposalController.getControlledUI(),
        		proposalController.getControlledUI(),this,"GENERIC");
        // rdias UCSD - Coeus personalization impl
        
        proposalController.display();
    }
     
       private void showCommittee() throws Exception{
        CommitteeBaseWindow committeeBaseWindow = null;
        if( ( committeeBaseWindow = (CommitteeBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE))!= null ){
            if( committeeBaseWindow.isIcon() ){
                committeeBaseWindow.setIcon(false);
            }
            committeeBaseWindow.setSelected( true );
            return;
        }
        committeeBaseWindow = new CommitteeBaseWindow(mdiForm);
        committeeBaseWindow.setVisible(true);
    }
    
    /*private void showAreaOfResearch() throws Exception{
     
        AreaOfResearchBaseWindow aorFrame = null;
        if ( (aorFrame = (AreaOfResearchBaseWindow)mdiForm.getFrame(
                CoeusGuiConstants.TITLE_AREA_OF_RESEARCH))!= null )  {
            if( aorFrame.isIcon() ){
                aorFrame.setIcon(false);
            }
            aorFrame.setSelected(true);
            return;
        }
        aorFrame = new AreaOfResearchBaseWindow(mdiForm);
        aorFrame.setVisible( true );
    }*/
    
    private void showSponsor() throws Exception{
        SponsorBaseWindow sponsorBaseWindow = null;
        if ( (sponsorBaseWindow = (SponsorBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.SPONSOR_FRAME_TITLE ))!= null )  {
            if( sponsorBaseWindow.isIcon() ){
                sponsorBaseWindow.setIcon(false);
            }
            sponsorBaseWindow.setSelected(true);
            return;
        }
        sponsorBaseWindow = new SponsorBaseWindow(mdiForm);
        sponsorBaseWindow.setVisible(true);
    }
    
    private void showRolodex() throws Exception{
        RolodexBaseWindow rolodexBaseWindow = null;
        if ( (rolodexBaseWindow = (RolodexBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.TITLE_ROLODEX ))!= null )  {
            if( rolodexBaseWindow.isIcon() ){
                rolodexBaseWindow.setIcon(false);
            }
            rolodexBaseWindow.setSelected(true);
            return;
        }
        rolodexBaseWindow = new RolodexBaseWindow(mdiForm);
        rolodexBaseWindow.setVisible(true);
    }
    /* JM 05-02-2013
    private void showProtocol() throws Exception{
        ProtocolBaseWindow protFrame = null;
        if ( (protFrame = (ProtocolBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.PROTOCOLBASE_FRAME_TITLE))!= null )  {
            if( protFrame.isIcon() ){
                protFrame.setIcon(false);
            }
            protFrame.setSelected(true);
            return;
        }
        protFrame = new ProtocolBaseWindow(mdiForm );
        protFrame.setVisible( true );
    }
    
    private void showIacucProtocol() throws Exception{
        edu.mit.coeus.iacuc.gui.ProtocolBaseWindow protFrame = null;
        if ( (protFrame = (edu.mit.coeus.iacuc.gui.ProtocolBaseWindow)mdiForm.getFrame(
        "IACUC Protocol"))!= null )  {
            if( protFrame.isIcon() ){
                protFrame.setIcon(false);
            }
            protFrame.setSelected(true);
            return;
        }
        protFrame = new edu.mit.coeus.iacuc.gui.ProtocolBaseWindow(mdiForm );
        protFrame.setVisible( true );
    }
    */
    private void showSubmissionDetails() throws Exception{
        SubmissionBaseWindow subDetails = null;
        if ( (subDetails = (SubmissionBaseWindow)mdiForm.getFrame(
        "Submission List"))!= null )  {
            if( subDetails.isIcon() ){
                subDetails.setIcon(false);
            }
            subDetails.setSelected(true);
            return;
        }
        subDetails = new SubmissionBaseWindow( mdiForm );
        subDetails.setVisible( true );
    }
    
    private void showProposalDev() throws Exception{
        ProposalBaseWindow propFrame = null;
        if ( (propFrame = (ProposalBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE))!= null )  {
            if( propFrame.isIcon() ){
                propFrame.setIcon(false);
            }
            propFrame.setSelected(true);
            return;
        }
        propFrame = new ProposalBaseWindow(mdiForm );
        propFrame.setVisible( true );
    }
    
    private void showSchedule() throws Exception{
        ScheduleBaseWindow schFrame = null;
        if ( (schFrame = (ScheduleBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.SCHEDULE_BASE_FRAME_TITLE))!= null )  {
            if( schFrame.isIcon() ){
                schFrame.setIcon(false);
            }
            schFrame.setSelected(true);
            return;
        }
        schFrame = new ScheduleBaseWindow(mdiForm );
        schFrame.setVisible( true );
    }
   //Changed for case id COEUSQA-1724 iacuc protocol stream generation
     /**
     *  It will identifies all the correspondencetype object for the irb protocol and list it in the
     *  base window.
     *  @throws Exception.
     */
    /* JM 05-02-2013
    private void showIRBCorrespondenceTypes() throws Exception{
        ProtoCorrespTypeBaseWindow correspTypeBaseWindow = null;
        
        if( ( correspTypeBaseWindow = (ProtoCorrespTypeBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.PROTO_CORRESP_TYPE_BASE_FRAME_TITLE))!= null ){
            if( correspTypeBaseWindow.isIcon() ){
                correspTypeBaseWindow.setIcon(false);
            }
            correspTypeBaseWindow.setSelected( true );
            return;
        }
        correspTypeBaseWindow = new ProtoCorrespTypeBaseWindow(mdiForm);
        correspTypeBaseWindow.setVisible(true);
    } */
    //Added for case id COEUSQA-1724 iacuc protocol stream generation start

    /**
     *  It will identifies all the correspondencetype object for the iacuc protocol and list it in the
     *  base window.
     *  @throws Exception.
     */
    /* JM 05-02-2013
    private void showIACUCCorrespondenceTypes() throws Exception{
        edu.mit.coeus.iacuc.gui.ProtoCorrespTypeBaseWindow correspTypeBaseWindow = null;
        if( ( correspTypeBaseWindow = (edu.mit.coeus.iacuc.gui.ProtoCorrespTypeBaseWindow)mdiForm.getFrame(
                CoeusGuiConstants.IACUC_CORRESP_TYPE_BASE_FRAME_TITLE))!= null ){
            if( correspTypeBaseWindow.isIcon() ){
                correspTypeBaseWindow.setIcon(false);
            }
            correspTypeBaseWindow.setSelected( true );
            return;
        }
        correspTypeBaseWindow = new edu.mit.coeus.iacuc.gui.ProtoCorrespTypeBaseWindow(mdiForm);
        correspTypeBaseWindow.setVisible(true);
    } */
  //Added for case id COEUSQA-1724 iacuc protocol stream generation end
    
     private void showNegotiation()throws Exception {
       NegotiationListForm negotiationListForm= null;
       if( ( negotiationListForm = (NegotiationListForm)mdiForm.getFrame(
        CoeusGuiConstants.NEGOTIATION_DETAILS_FRAME_TITLE))!= null ){
            if( negotiationListForm.isIcon() ){
                negotiationListForm.setIcon(false);
            }
            negotiationListForm.setSelected( true );
            return;
        }
        NegotiationListController negotiationController= new NegotiationListController();
        negotiationController.display();
    }
     
     private void showSponsorHierarchy() throws Exception {
         
         SponsorHierarchyListController controller = new SponsorHierarchyListController();
         controller.display();
     }
     
     private void showAwardReportingRequirements() {
         //ReportingRequirementsController reportingRequirementsController =null;
         Object obj = (Object)mdiForm.getSelectedFrame();
         CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.AWARD_REPORTING_REQ);
         if (frame==null) {
             awardReportingReqController = new AwardReportingReqController();
             awardReportingReqController.display();
         } else if (obj instanceof edu.mit.coeus.award.gui.AwardReportingReqBaseWindow)  {
             // display for that award
             //get award
             String awardNumber = awardReportingReqController.getSelectedAwardNumber();
             //chek whether reporting req is open for this award
             CoeusInternalFrame awardFrame = (CoeusInternalFrame)mdiForm.getFrame(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW,awardNumber);
             //if yes bring that up
             if (awardFrame!=null) {
                 try {
                     awardFrame.setSelected(true);
                 } catch (PropertyVetoException pve) {
                     pve.printStackTrace();
                 }
             } else {  //else  open the window
                 awardReportingReqController.showReportingReqmts(awardNumber);
             }
         } else if (obj instanceof edu.mit.coeus.award.gui.ReportingReqBaseWindow) {
             //bring up maintain window
             try {
                 frame.setSelected(true);
             } catch (PropertyVetoException pve) {
                 pve.printStackTrace();
             }
         }
         
     }
     
    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }
    
    /**
     * 
     *
     */
    // rdias UCSD - Coeus personalization impl
    public void notifyPersonalizationController() {
	    AbstractController persnref = AbstractController.getPersonalizationControllerRef();	    
	    persnref.customize_module(this, mdiForm, this, "MAINFORM");
	}    

    /* JM 05-02-2013
    private void showIacucProtocolSubmissionDetails() throws PropertyVetoException {
        edu.mit.coeus.iacuc.gui.SubmissionBaseWindow subDetails = null;
        if ( (subDetails = (edu.mit.coeus.iacuc.gui.SubmissionBaseWindow)mdiForm.getFrame(
                "IACUC Protocol Submission List"))!= null )  {
            if( subDetails.isIcon() ){
                subDetails.setIcon(false);
            }
            subDetails.setSelected(true);
            return;
        }
        subDetails = new edu.mit.coeus.iacuc.gui.SubmissionBaseWindow( mdiForm );
        subDetails.setVisible( true );
    } */
    
   
}