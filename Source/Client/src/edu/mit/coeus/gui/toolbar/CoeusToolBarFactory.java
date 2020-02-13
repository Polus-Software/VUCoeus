/*
 * @(#)CoeusToolBarFactory.java 1.0 7/28/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * PMD check performed, and commented unused imports and variables on 16-Aug-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.gui.toolbar;


import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusPopupMenu;
import edu.mit.coeus.rolodexmaint.gui.RolodexBaseWindow;
import edu.mit.coeus.sponsormaint.gui.SponsorBaseWindow;
import edu.mit.coeus.unit.gui.UnitHierarchyBaseWindow;

/*Added Icons are Committe,Protocol,Shedule - Chandrashekar*/
import edu.mit.coeus.irb.gui.ProtocolBaseWindow;
import edu.mit.coeus.irb.gui.ScheduleBaseWindow;
import edu.mit.coeus.irb.gui.CommitteeBaseWindow;
import edu.mit.coeus.irb.gui.SubmissionBaseWindow;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.DesktopUtils;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.departmental.gui.PersonBaseWindow;
import edu.mit.coeus.propdev.gui.ProposalBaseWindow;
import edu.mit.coeus.user.gui.UserMaintenanceBaseWindow;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.award.controller.AwardListController;
import edu.mit.coeus.award.gui.AwardList;
import edu.mit.coeus.instprop.gui.InstituteProposalListForm;
import edu.mit.coeus.instprop.controller.InstituteProposalListController;
import edu.mit.coeus.negotiation.gui.NegotiationListForm;
import edu.mit.coeus.mapsrules.controller.MapMaintenanceBaseWindowController;
import edu.mit.coeus.mapsrules.controller.RuleBaseWindowController;
import edu.mit.coeus.mapsrules.gui.MapMaintenanceBaseWindow;
import edu.mit.coeus.mapsrules.gui.RuleBaseWindow;
import edu.mit.coeus.negotiation.controller.NegotiationListController;
import edu.mit.coeus.subcontract.controller.SubcontractListController;
import edu.mit.coeus.subcontract.gui.SubcontractListForm;
import edu.ucsd.coeus.personalization.controller.AbstractController;

/* JM 4-25-2016 added for Coeus toolbar buttons */
import edu.vanderbilt.coeus.gui.toolbar.ToolFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class creates dockable JToolBar with CoeusToolBarButton's,
 *
 * @version :1.0 July 28,2002
 * @author Mukund C
 * @modified by guptha
 */

public class CoeusToolBarFactory extends JToolBar implements ActionListener {
    public CoeusToolBarButton inbox,awards,proposal,proposalDevelopment,rolodex,sponsor,
    subContract,negotiations,buisnessRules,map,personnal,users,unitHierarchy,
    cascade,tileHorizontal,tileVertical,layer,exit,personal,view,trash,createAward,
    displayAward,correctAward,searchAward,sponsorSearch,saveAs,medusa,closed,
    addRolodex,modifyRolodex,displayRolodex,deleteRolodex,copyRolodex,refRolodex,sortRolodex,
    rolodexSearch,addSponsor,modifySponsor,displaySponsor,deleteSponsor,sortSponsor,closedSponsor,
    addUnitHierarchy,modifyUnitHierarchy,displayUnitHierarchy,moveUnitHierarchy;
    
    /* JM 4-25-2016 added Contact Coeus Help button */
    public CoeusToolBarButton contactCoeusHelp;
    /* JM END */
    
    /* JM 05-02-2013 iacucProtocol,schedule,committee;  
    public CoeusToolBarButton irbProtocolSubmission;
    public CoeusToolBarButton iacucProtocolSubmission;
    */
    private CoeusPopupMenu cpm; 
    private JToolBar toolbar;
    
    private CoeusAppletMDIForm mdiForm;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /**  Creates a new CoeusToolBarFactory
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CoeusToolBarFactory(CoeusAppletMDIForm mdiForm) {
        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createToolBar();
        // rdias UCSD - Coeus personalization impl
//	    AbstractController persnref = AbstractController.getPersonalizationControllerRef();
//	    persnref.customize_module(null, getToolBar(),this, "MAINFRAME");
	    //rdias UCSD               
    }
    
    /**
     * getDefaultToolbar returns the JToolbar in the MDI form with the default toolbar
     * with set icons,text and tooltip specified for the Default toolbar,
     * the icon,text and tooltip is added to the toolbar,
     * the toolbar consist of popup menu also.
     *
     */
    private void createToolBar() {
        toolbar = new JToolBar();
        inbox = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.INBOX_ICON)), "Maintain Inbox", "Maintain Inbox");
        awards = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.AWARDS_ICON)), "Maintain Awards", "Maintain Awards");
        proposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROPOSAL_ICON)), "Maintain InstituteProposals", "Maintain Institute Proposals");
        proposalDevelopment = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROPOSAL_DEVELOPMENT_ICON)), "Maintain ProposalDevelopment", "Maintain Proposal Development");
        rolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ROLODEX_ICON)), "Maintain Rolodex", "Maintain Rolodex");
        sponsor = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SPONSOR_ICON)), "Maintain Sponsor", "Maintain Sponsor");
        subContract = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SUBCONTRACT_ICON)), "Maintain SubContract", "Maintain Subcontract");
        negotiations = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NEGOTIATIONS_ICON)), "Maintain Negotiations", "Maintain Negotiations");
        buisnessRules = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.BUSINESS_RULES_ICON)), "Maintain BusinessRules", "Maintain Business Rules");
        map = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MAP_ICON)), "Maintain Map", "Maintain Map");
        personnal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PERSONNAL_ICON)), "Maintain Personal", "Maintain Personnel");
        users = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.USERS_ICON)), "Maintain Users", "Maintain Users");
        unitHierarchy = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.UNIT_HIERARCHY_ICON)), "Maintain UnitHierarchy", "Maintain Unit Hierarchy");
        cascade = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CASCADE_ICON)), "Cascade", "Cascade");
        tileHorizontal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.TILE_HORIZONTAL_ICON)), "Tile Horizontal", "Tile Horizontal");
        tileVertical = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.TILE_VERTICAL_ICON)), "Tile Vertical", "Tile Vertical");
        layer = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.LAYER_ICON)), "Layer", "Layer");
        
        /*Added Icons are Committe,Protocol,Shedule. The Icons are different.Non-availability of standard Icon - Chandrashekar*/
        //Added for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium - Start
        /* JM 05-02-2013
        irbProtocol = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROTOCOL_ICON)), "Protocol", "IRB Protocol");
        
        irbProtocolSubmission = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROTOCOL_SUBMISSION_BASE_ICON)),"Protocol Submission","IRB Protocol Submission");
        */
       //Added for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium - End
        /* JM 05-02-2013
        schedule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SCHEDULE_ICON)), "Schedule", "Schedule");
        committee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.COMMITTEE_ICON)), "Committee", "Committee");
        */
        //Added for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium - Start
        //irbProtocolSubmission = new CoeusToolBarButton(new ImageIcon(
       // getClass().getClassLoader().getResource(CoeusGuiConstants.PROTOCOL_SUBMISSION_BASE_ICON)),"Protocol Submission","IRB Protocol Submission");
        //Added for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium - End
        //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium start
        /* JM 05-02-2013
        iacucProtocol = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.IACUC_PROTOCOL_ICON)), "Protocol", "IACUC Protocol");
        
        iacucProtocolSubmission = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.IACUC_PROTOCOL_SUBMISSION_BASE_ICON)),"Protocol Submission","IACUC Protocol Submission");
        */
        //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium end
        
        /* JM 4-25-2016 adding new Contact Coeus Help button */
        contactCoeusHelp = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.HELP_ICON)), "Contact Coeus Help", "Contact Coeus Help");
        /* JM END */
        
        exit = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EXIT_ICON)), "Exit", "Exit");
        
        
        toolbar.add(inbox);
        toolbar.addSeparator();
        toolbar.add(awards);
        toolbar.add(proposal);
        toolbar.add(proposalDevelopment);
        toolbar.add(rolodex);
        toolbar.add(sponsor);
        toolbar.add(subContract);
        toolbar.add(negotiations);
        toolbar.add(buisnessRules);
        toolbar.add(map);
        toolbar.add(personnal);
        toolbar.add(users);
        toolbar.add(unitHierarchy);
        
        /*Added Icons are Committe,Protocol,Shedule - Chandrashekar*/
        /* JM 05-02-2013
        toolbar.add(irbProtocol);
        toolbar.add(irbProtocolSubmission);
        
        toolbar.add(schedule);
        toolbar.add(committee);
        */
        //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium start
        /* JM 05-02-2013
        toolbar.add(iacucProtocol);
        toolbar.add(iacucProtocolSubmission);
        */
        //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium end
        
        toolbar.addSeparator();
        toolbar.add(cascade);
        toolbar.add(tileHorizontal);
        toolbar.add(tileVertical);
        toolbar.add(layer);
        toolbar.addSeparator();
        
        /* JM 4-25-2016 adding new Contact Coeus Help button */
        toolbar.add(contactCoeusHelp);
        toolbar.addSeparator();
        /* JM END */
        
        toolbar.add(exit);
        
        toolbar.setFloatable(false);
        setTextLabels(false);
        MouseListener pl = new PopupListener();
        cpm = new CoeusPopupMenu();
        toolbar.addMouseListener(pl);
        
        inbox.addActionListener(this);
        awards.addActionListener(this);
        proposal.addActionListener(this);
        proposalDevelopment.addActionListener(this);
        rolodex.addActionListener(this);
        sponsor.addActionListener(this);
        subContract.addActionListener(this);
        negotiations.addActionListener(this);
        buisnessRules.addActionListener(this);
        map.addActionListener(this);
        personnal.addActionListener(this);
        users.addActionListener(this);
        unitHierarchy.addActionListener(this);
        cascade.addActionListener(this);
        tileHorizontal.addActionListener(this);
        tileVertical.addActionListener(this);
        layer.addActionListener(this);
        /*Added Icons are Committe,Protocol,Shedule - Chandrashekar*/
        /* JM 05-02-2013
        irbProtocol.addActionListener(this);
        schedule.addActionListener(this);
        committee.addActionListener(this);
        irbProtocolSubmission.addActionListener(this);
        //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium start
        iacucProtocol.addActionListener(this);
        iacucProtocolSubmission.addActionListener(this); */
        //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium end
        
        /* JM 4-25-2016 adding new Contact Coeus Help button */
        contactCoeusHelp.addActionListener(this);
        /* JM END */
        
        exit.addActionListener(this);
    }
    
    /**
     * This method gets the JToolBar
     *
     * @return JToolBar coeus maintain menu
     */
    public JToolBar getToolBar(){
        return toolbar;
    }
    
    private void showExit() {
        String message = coeusMessageResources.parseMessageKey(
        "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                if (mdiForm.getParentFrame() == null) {
                    mdiForm.logout();
                    mdiForm.dispose();
                }
                else {
                    try {
                        mdiForm.logout();
                        System.exit(0) ;
                    }
                    catch(Exception exp) {
                        //System.out.println(" Exception when dispose is called... ") ;
                        exp.printStackTrace() ;
                    }
                }
            }
        }
    }
    
    private void showUnitHierarchy() {
        UnitHierarchyBaseWindow unitHierarchyBaseWindow =
        (UnitHierarchyBaseWindow) mdiForm.getFrame(
        CoeusGuiConstants.TITLE_UNIT_HIERARCHY);
        if (unitHierarchyBaseWindow != null) {
            try {
                unitHierarchyBaseWindow.setSelected(true);
            } catch (Exception ex) {
            }
            unitHierarchyBaseWindow.setVisible(true);
            return;
        } else {
            unitHierarchyBaseWindow = new UnitHierarchyBaseWindow(mdiForm);
        }
    }
    
    private void showRolodex() {
        try {
            CoeusInternalFrame rldxFrame = mdiForm.getFrame(
            CoeusGuiConstants.TITLE_ROLODEX);
            if (rldxFrame == null) {
                RolodexBaseWindow rolodexBaseWindow =
                new RolodexBaseWindow(mdiForm);
                rolodexBaseWindow.setVisible(true);
            } else {
                rldxFrame.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
            //Commented by Ajay - Modification done for Exception Handling 
            //Since the Code was changed in AppletServletCommunicator to 
            //show Server down message, the below code is commented.
            
            //String errorMsg = e.getMessage();
            //CoeusOptionPane.showErrorDialog(errorMsg);
            
        }
    }
    
    private void showSponsor() {
        try{
            SponsorBaseWindow sponsorBaseWindow = null;
            if ( (sponsorBaseWindow = (SponsorBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.SPONSOR_FRAME_TITLE ))!= null )  {
                sponsorBaseWindow.setSelected(true);
                return;
            }
            sponsorBaseWindow =
            new SponsorBaseWindow(mdiForm);
            sponsorBaseWindow.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method is used to capture the toolbar button action events. for example
     * rolodex, sponsor module menuitem.
     *
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae) {
        JDesktopPane desktop = mdiForm.getDeskTopPane();
        Object menuItem = ae.getSource();
        try {
            if (menuItem.equals(exit)) {
                showExit();
            } else if (menuItem.equals(unitHierarchy)) {
                showUnitHierarchy();
            } else if (menuItem.equals(rolodex)) {
                showRolodex();
            } else if (menuItem.equals(sponsor)) {
                showSponsor();
            } else if (menuItem.equals(proposalDevelopment)) {
                showProposalDevelopment();
            } else if (menuItem.equals(personnal)) {
                showDepartmentalPerson();
                
                /*Added Icons are Committe,Protocol,Shedule - Chandrashekar*/
            /* JM 05-02-2013
            }else if(menuItem.equals(irbProtocol)){
                showIRBProtocol();
            }else if(menuItem.equals(schedule)){
                showSchedule();
            }else if (menuItem.equals(committee)){
                showCommittee();
            */
            }else if( menuItem.equals( cascade ) ){
                DesktopUtils.cascadeAll( desktop );
            }else if( menuItem.equals( tileHorizontal ) ){
                DesktopUtils.tileHorizontal( desktop );
            }else if( menuItem.equals( tileVertical ) ){
                DesktopUtils.tileVertical( desktop );
            }else if( menuItem.equals( layer ) ){
                DesktopUtils.layer( desktop );
            }
            else if(menuItem.equals(users)) {
                showUserMaintenance();
            }
            else if(menuItem.equals(map)) {
                showMaps(); //Added by Shiji to display the Map Maintainance window
            }
            /* JM 05-02-2013
            else if(menuItem.equals(irbProtocolSubmission)){
                showIRBSubmissionDetails();
            }//Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium start
            else if(menuItem.equals(iacucProtocol)){
                showIACUCProtocol();
            }
            else if(menuItem.equals(iacucProtocolSubmission)){
                showIACUCSubmissionDetails();
            } */
            //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium end
            
            else if(menuItem.equals(inbox)){
                showInboxDetails();
            }else if(menuItem.equals(awards)) {
                showAwards();
            }else if(menuItem.equals(proposal)){
                showProposal();
            }else if(menuItem.equals(negotiations)){
                showNegotiation();
            }else if(menuItem.equals(subContract)){
                showSubcontract();
            }else if(menuItem.equals(buisnessRules)){
                showBusinessRules();
            }
            /* JM 4-25-2016 added Contact Coeus Help button */
	        else if(menuItem.equals(contactCoeusHelp)){
	        	ToolFunctions tools = new ToolFunctions();
	        	tools.contactCoeusHelp();
	        }
	        /* JM END */
        }
        catch(Exception e) {
            //Commented by Ajay - Modification done for Exception Handling 
            //Since the Code was changed in AppletServletCommunicator to 
            //show Server down message, the below code is commented.
              
            //CoeusOptionPane.showInfoDialog(e.getMessage());
            
            //Added to display Stack Trace
            e.printStackTrace();
        }
        
    }
    // To show the Business Rules
     private void showBusinessRules() throws Exception{
         String unitNumber = mdiForm.getUnitNumber();
         String title = CoeusGuiConstants.BUSINESS_RULE_FRAME_TITLE;
         RuleBaseWindow ruleBaseWindow = null;
         if( ( ruleBaseWindow = (RuleBaseWindow)mdiForm.getFrame(
         title))!= null ){
             if( ruleBaseWindow.isIcon() ){
                 ruleBaseWindow.setIcon(false);
             }
             ruleBaseWindow.setSelected( true );
             return;
         }
         RuleBaseWindowController ruleBaseWindowController = new RuleBaseWindowController(title,mdiForm,unitNumber);
         ruleBaseWindowController.display();
     }
    
    //To show the Map Maintainance base window
     public void showMaps() throws Exception{
        MapMaintenanceBaseWindow mapMaintenanceBaseWindow = null;
        String unitNumber=mdiForm.getUnitNumber();
       
        if( ( mapMaintenanceBaseWindow = (MapMaintenanceBaseWindow)mdiForm.getFrame(
                CoeusGuiConstants.MAPS_BASE_FRAME_TITLE+" "+unitNumber))!= null ){
              if( mapMaintenanceBaseWindow.isIcon() ){
                mapMaintenanceBaseWindow.setIcon(false);
              }
              mapMaintenanceBaseWindow.setSelected( true );
              return;
        }
       
        MapMaintenanceBaseWindowController mapMaintenanceBaseWindowController = new MapMaintenanceBaseWindowController(unitNumber,false);
        mapMaintenanceBaseWindowController.display();
        
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
//        AbstractController persnref = AbstractController.getPersonalizationControllerRef();
//        persnref.customize_module(proposalController,proposalController,null,
//        		CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE);
        // rdias UCSD - Coeus personalization impl
        
        proposalController.display();
    }
    
    private void showAwards() throws Exception {
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
    //included by raghuSV to show the inbox details form.
    private void showInboxDetails(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            System.out.println("In showInboxDetails function");
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    // Implemented for displaying Departmental Person Base window when personnel tool bar button is fired
    private void showDepartmentalPerson(){
        
        PersonBaseWindow personBaseWindow = null;
        try{
            if( ( personBaseWindow = (PersonBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.PERSON_BASE_FRAME_TITLE))!= null ){
                if( personBaseWindow.isIcon() ){
                    personBaseWindow.setIcon(false);
                }
                personBaseWindow.setSelected( true );
                return;
            }
            personBaseWindow = new PersonBaseWindow(mdiForm);
            personBaseWindow.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    /**
     * This method will be invoked when uers toolbar button is clicked.
     */
    private void showUserMaintenance(){
        UserMaintenanceBaseWindow userMaintBaseWindow = null;
        try{
            userMaintBaseWindow = (UserMaintenanceBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE);
            if( userMaintBaseWindow != null) {
                
                if(userMaintBaseWindow.isIcon()){ 
                    userMaintBaseWindow.setIcon(false);
                }
                userMaintBaseWindow.setSelected(true);
                //userMaintBaseWindow.showUserDetails();
                return ;
            }
            userMaintBaseWindow = new UserMaintenanceBaseWindow(CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE, mdiForm);
            //userMaintBaseWindow.showUserDetails();
        }catch (Exception exception) {
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    // Implemented for displaying ProposalDevelopment Base window when personnel tool bar button is fired
    // Added By Raghunath P.V.
    
    private void showProposalDevelopment(){
        try{
            
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
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    /* JM 05-02-2013
    private void showIRBProtocol() {
        try{
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
        }catch(Exception exception){
            //Commented by Ajay - Modification done for Exception Handling 
            //Since the Code was changed in AppletServletCommunicator to 
            //show Server down message, the below code is commented.
            
            //CoeusOptionPane.showInfoDialog(exception.getMessage());
            
            //Added to display Stack Trace
            exception.printStackTrace();
        }
    } */
    
    //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium start
    
    /**
     *  The method will disyplays the iacuc protocol list window .
     */
    /* JM 05-02-2013
     private void showIACUCProtocol(){
        try{
            edu.mit.coeus.iacuc.gui.ProtocolBaseWindow protFrame = null;
            if ( (protFrame = (edu.mit.coeus.iacuc.gui.ProtocolBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.IACUC_PROTOCOL_BASE_FRAME_TITLE))!= null )  {
                if( protFrame.isIcon() ){
                    protFrame.setIcon(false);
                }
                protFrame.setSelected(true);
                return;
            }
            protFrame = new edu.mit.coeus.iacuc.gui.ProtocolBaseWindow(mdiForm );
            protFrame.setVisible( true );
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    */
    //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium end
    /* JM 05-02-2013 
    private void showCommittee() {
        try{
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
        }catch(Exception exception){
            //Commented by Ajay - Modification done for Exception Handling 
            //Since the Code was changed in AppletServletCommunicator to 
            //show Server down message, the below code is commented.
            
            //CoeusOptionPane.showInfoDialog(exception.getMessage());
            
            //Added to display Stack Trace
            exception.printStackTrace();
        }
    } */
    /* JM 05-02-2013
    private void showSchedule() {
        try{
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
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    */
    /* JM 05-02-2013
    private void showIRBSubmissionDetails() throws Exception{
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
    */
    
    
    //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium start
    
    /**
     *  The method will disyplays the iacuc protocol submission list window.
     *  @throws Exception
     */
    /* JM 05-02-2013
     private void showIACUCSubmissionDetails() throws Exception{
        edu.mit.coeus.iacuc.gui.SubmissionBaseWindow subDetails = null;
        if ( (subDetails = (edu.mit.coeus.iacuc.gui.SubmissionBaseWindow)mdiForm.getFrame(
        CoeusGuiConstants.IACUC_PROTO_SUB_LIST_BASE_FRAME_TITLE))!= null )  {
            if( subDetails.isIcon() ){
                subDetails.setIcon(false);
            }
            subDetails.setSelected(true);
            return;
        }
        subDetails = new edu.mit.coeus.iacuc.gui.SubmissionBaseWindow( mdiForm );
        subDetails.setVisible( true );
    }
    */
    //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium end
     
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
    /*Added Icons are Committe,Protocol,Shedule - Chandrashekar*/
    /**
     * This method sets the text to the toolbar button if the set text is enabled.
     *
     * @param labelsAreEnabled boolean
     */
    public void setTextLabels(boolean labelsAreEnabled) {
        Component c;
        int i = 0;
        while ((c = toolbar.getComponentAtIndex(i++)) != null) {
            if (c instanceof CoeusToolBarButton) {
                CoeusToolBarButton button = (CoeusToolBarButton) c;
                if (labelsAreEnabled)
                    button.setText(button.getText());
                else
                    button.setText(null);
            }
        }
    }
        
    /**
     * This class implemented for the toolbar ,it will popup the menu on click
     * of the mouse on the toolbar,this class extends MouseAdapter.
     *
     */
    class PopupListener extends MouseAdapter {
        /**
         * This method is used to handle the mousePressed event.
         *
         * @param e MouseEvent
         */
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
        
        /**
         * This method is used to handle the mouseReleased event.
         *
         * @param e MouseEvent
         */
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
        
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                cpm.show(e.getComponent(),
                e.getX(), e.getY());
            }
        }
    }
    
}


