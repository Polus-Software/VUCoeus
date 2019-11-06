/*
 * @(#)CoeusReportMenu.java 
 *
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 *
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 *
 * All rights reserved.
 *
 * 
 * Author: Romerl Elizes
 *
 * Description: Report Menu object for Reporting capabilities in COEUS
 * 
 */
package edu.umdnj.coeus.gui.menu;

import edu.mit.coeus.centraladmin.gui.EndOfMonthProcessForm;
import edu.mit.coeus.centraladmin.gui.FeedMaintenanceController;
import edu.mit.coeus.centraladmin.gui.FeedMaintenanceForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.centraladmin.gui.NewPublicMessageForm;
import edu.mit.coeus.centraladmin.gui.GenerateMasterDataFeedForm;
import edu.mit.coeus.centraladmin.gui.GenerateRolodexFeedForm;
import edu.mit.coeus.centraladmin.gui.GenerateSponsorFeedForm;
import edu.mit.coeus.centraladmin.gui.MassChangeForm;
import edu.mit.coeus.centraladmin.gui.PopulateSubcontractExpenseDataForm;
import edu.mit.coeus.exception.CoeusClientException;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.s2s.controller.S2SSubmissionListController;
import edu.mit.coeus.utils.ParameterMaintenanceForm;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.unit.gui.UnitHierarchyBaseWindow;
import edu.umdnj.coeus.reporting.controller.CannedReportsController;
import edu.umdnj.coeus.reporting.controller.GrantsBySchoolController;
import edu.umdnj.coeus.reporting.controller.ProposalsBySchoolController;
import edu.umdnj.coeus.reporting.controller.PendingProposalsController;
import edu.umdnj.coeus.reporting.controller.ClosedReportController;
import edu.umdnj.coeus.reporting.controller.GrantsBySponsorController;
import edu.umdnj.coeus.reporting.controller.GrantsByActivityTypeController;
import edu.umdnj.coeus.reporting.controller.GrantsByAwardTypeController;
import edu.umdnj.coeus.reporting.controller.SummaryGrantsController;
import edu.umdnj.coeus.reporting.controller.GraphsGrantsSponsorController;
import edu.umdnj.coeus.reporting.controller.GraphsGrantsActivityController;
import edu.umdnj.coeus.reporting.controller.GraphsGrantsAwardController;
import edu.umdnj.coeus.reporting.controller.GrantsByPIController;
import edu.umdnj.coeus.reporting.controller.GraphsGrantsPIController;
import edu.umdnj.coeus.reporting.controller.SpecReviewProposalsController;
import edu.umdnj.coeus.reporting.controller.SummaryPIGrantsController;
import edu.umdnj.coeus.reporting.controller.CurrentActiveGrantsController;
import edu.umdnj.coeus.reporting.controller.SpecReviewGrantsController;
import edu.umdnj.coeus.reporting.controller.AnnualReportsController;
import edu.umdnj.coeus.reporting.controller.PendingStatusReportController;
import java.awt.Cursor;

/**
 * Class that encapsulates Report Menu in COEUS Application
 * @author Romerl Elizes
 */
public class CoeusReportMenu extends JMenu implements ActionListener
{
   private UnitHierarchyBaseWindow unitwindow = null;
   private String unitNumber = "";
   private String unitName = "";
    /*
     * report menu menu items
     */
    /**
     * Menu item for Canned Reports
     */
    public CoeusMenuItem reportItem;

    // Reports sub menu items
    /**
     * Sub-menu item for Grants by Department
     */
    public CoeusMenuItem grantsItem;
    /**
     * Sub-menu item for Proposals
     */
    public CoeusMenuItem proposalsItem;
    /**
     * Sub-menu item for Pending Proposals
     */
    public CoeusMenuItem pendingProposalsItem;
    /**
     * Sub-menu item for Special Review proposals
     */
    public CoeusMenuItem specreviewProposalsItem;
    /**
     * Sub-menu item for Pending Status Report of Proposals
     */
    public CoeusMenuItem pendingStatusReportItem;
    /**
     * Sub-menu item for Closed Reports
     */
    public CoeusMenuItem closedreportItem;
    /**
     * Sub-menu item for Grants by Sponsor Type Reports
     */
    public CoeusMenuItem grantsBySponsorItem;
    /**
     * Sub-menu item for Grants by Activity Type Reports
     */
    public CoeusMenuItem grantsByActivityTypeItem;
    /**
     * Sub-menu item for Grants by Award Type Reports
     */
    public CoeusMenuItem grantsByAwardTypeItem;
    /**
     * Sub-menu item for Grants by Primary Investigator Reports
     */
    public CoeusMenuItem grantsByPIItem;
    /**
     * Sub-menu item for Summary Grants Reports
     */
    public CoeusMenuItem summaryGrantsItem;
    /**
     * Sub-menu item for Summary Grants of Primary Investigators
     */
    public CoeusMenuItem summaryPIGrantsItem;
    /**
     * Sub-menu item for Current Active Grants
     */
    public CoeusMenuItem currentActiveGrantsItem;
    /**
     * Sub-menu item for Current Special Review Grants
     */
    public CoeusMenuItem currentSpecReviewGrantsItem;
    
    /**
     * Sub-menu item for Annual Reports
     */
    public CoeusMenuItem annualReportsItem;
   

    // Graphs sub menu items
    /**
     * Sub-menu item for Grants by Sponsor Type Graphs
     */
    public CoeusMenuItem graphGrantsBySponsorItem;
    /**
     * Sub-menu item for Grants by Activity Type graphs
     */
    public CoeusMenuItem graphGrantsByActivityItem;
    /**
     * Sub-menu item for Grants by Award Type graphs
     */
    public CoeusMenuItem graphGrantsByAwardItem;
    /**
     * Sub-menu items for Grants by Primary Investigators graphs
     */
    public CoeusMenuItem graphGrantsByPIItem;

    private Vector fileChildren;
    public  Vector mainChild;


    private final String SEPERATOR="seperator";

    private CoeusMenu coeusMenu;

    private CoeusAppletMDIForm mdiForm;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** Default constructor which constructs the report menu for coeus

     * application.
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CoeusReportMenu(CoeusAppletMDIForm mdiForm){

        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createMenu();
    }

    /** Default constructor which constructs the report menu for coeus

     * application.
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CoeusReportMenu(CoeusAppletMDIForm mdiForm,UnitHierarchyBaseWindow unitwindow){
        this(mdiForm);
        this.unitwindow = unitwindow;
    }

    /**
     * This method is used to get the central admin menu
     *
     * @return JMenu coeus central admin menu
     */
    public JMenu getMenu(){
        return coeusMenu;
    }

    /**
     * This method is used to create central admin menu for coeus application.
     */
    private void createMenu(){
	fileChildren = new Vector();
        mainChild = new Vector();

        /****
         * Per instructions from Therese, removing Sample Reports dialog
        reportItem = new CoeusMenuItem("Sample Reports",null,true,true);

	fileChildren.add(reportItem);
        
        fileChildren.add(SEPERATOR);
        ****/

        Vector reportVector = new Vector();

        proposalsItem = new CoeusMenuItem("Submitted Proposals By Unit",null,true,true);
        
        proposalsItem.setMnemonic('S');
        
        reportVector.add(proposalsItem);

        pendingProposalsItem = new CoeusMenuItem("Pending Proposals By Unit",null,true,true);
                
        reportVector.add(pendingProposalsItem);
        
        specreviewProposalsItem = new CoeusMenuItem("Proposals for Pending Special Reviews By Unit",null,true,true);
                
        reportVector.add(specreviewProposalsItem);

	pendingStatusReportItem = new CoeusMenuItem("Pending Status Report of Proposals By Unit",null,true,true);

        reportVector.add(pendingStatusReportItem);

        reportVector.add(SEPERATOR);

        grantsItem = new CoeusMenuItem("Active Grants By Unit",null,true,true);
        
        grantsItem.setMnemonic('G');
        
        reportVector.add(grantsItem);

        closedreportItem = new CoeusMenuItem("Closed Awards By Unit/Date",null,true,true);

        closedreportItem.setMnemonic('A');

        reportVector.add(closedreportItem);

        grantsBySponsorItem = new CoeusMenuItem("Active Grants By Sponsor Type",null,true,true);

        grantsBySponsorItem.setMnemonic('N');

        reportVector.add(grantsBySponsorItem);

        grantsByActivityTypeItem = new CoeusMenuItem("Active Grants By Activity Type",null,true,true);

        grantsByActivityTypeItem.setMnemonic('T');

        reportVector.add(grantsByActivityTypeItem);

        grantsByAwardTypeItem = new CoeusMenuItem("Active Grants By Award Type",null,true,true);

        reportVector.add(grantsByAwardTypeItem);

        grantsByPIItem = new CoeusMenuItem("Active Grants By Principal Investigator",null,true,true);

        reportVector.add(grantsByPIItem);

        summaryGrantsItem = new CoeusMenuItem("Summary of Active Grants by Unit",null,true,true);

        reportVector.add(summaryGrantsItem);

        summaryPIGrantsItem = new CoeusMenuItem("Summary of Active Grants by Investigator",null,true,true);

        reportVector.add(summaryPIGrantsItem);

        currentActiveGrantsItem = new CoeusMenuItem("Current Active Grants by Unit",null,true,true);

        reportVector.add(currentActiveGrantsItem);
        
        currentSpecReviewGrantsItem = new CoeusMenuItem("Current Active Grants by Special Review by Unit",null,true,true);
        
        reportVector.add(currentSpecReviewGrantsItem);

        reportVector.add(SEPERATOR);

        annualReportsItem = new CoeusMenuItem("Annual Reports",null,true,true);
        
        reportVector.add(annualReportsItem);

        CoeusMenu reportsMenu = new CoeusMenu("Reports",null,reportVector,true,true);

     

        Vector graphVector = new Vector();

        graphGrantsBySponsorItem = new CoeusMenuItem("Active Grants by Sponsor Type", null, true,true);

        graphVector.add(graphGrantsBySponsorItem);

        graphGrantsByActivityItem = new CoeusMenuItem("Active Grants by Activity Type", null, true,true);

        graphVector.add(graphGrantsByActivityItem);

        graphGrantsByAwardItem = new CoeusMenuItem("Active Grants by Award Type", null, true,true);

        graphVector.add(graphGrantsByAwardItem);

        graphGrantsByPIItem = new CoeusMenuItem("Active Grants by Primary Investigator", null, true,true);

        graphVector.add(graphGrantsByPIItem);

        CoeusMenu graphsMenu = new CoeusMenu("Graphs",null,graphVector,true,true);

        fileChildren.add(reportsMenu);
        fileChildren.add(graphsMenu);
  

        coeusMenu = new CoeusMenu("Reporting",null,fileChildren,true,true);

        coeusMenu.setMnemonic('R');

//JM        mainChild.add(coeusMenu); //JM 5-25-2011 removed reporting menu selection per 4.4.2

        //add listener
        //reportItem.addActionListener(this);
        grantsItem.addActionListener(this);
        proposalsItem.addActionListener(this);
        pendingProposalsItem.addActionListener(this);
	pendingStatusReportItem.addActionListener(this);
        specreviewProposalsItem.addActionListener(this);
        closedreportItem.addActionListener(this);
        grantsBySponsorItem.addActionListener(this);
        grantsByActivityTypeItem.addActionListener(this);
        grantsByAwardTypeItem.addActionListener(this);
        grantsByPIItem.addActionListener(this);
        summaryGrantsItem.addActionListener(this);
        summaryPIGrantsItem.addActionListener(this);
	currentActiveGrantsItem.addActionListener(this);
	currentSpecReviewGrantsItem.addActionListener(this);
        annualReportsItem.addActionListener(this);
        graphGrantsBySponsorItem.addActionListener(this);
	graphGrantsByActivityItem.addActionListener(this);
	graphGrantsByAwardItem.addActionListener(this);
	graphGrantsByPIItem.addActionListener(this);
    }

    /** This method is used to handle the action event for the central admin menu items.
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae){
        Object source = ae.getSource();
        try{
            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            this.unitNumber = unitwindow.getSelectedUnitNumber();
            this.unitName = unitwindow.getSelectedUnitName();
            if( source.equals(reportItem) ){
                showReportDialog();
            }
            else if (source.equals(grantsItem)) {
                    showGrantsDialog();
            }
            else if (source.equals(proposalsItem)) {
                    showProposalsDialog();
            }
            else if (source.equals(specreviewProposalsItem)) {
                    showSpecReviewProposalsDialog();
            }
            else if (source.equals(pendingProposalsItem)) {
                    showPendingProposalsDialog();
            }
            else if (source.equals(pendingStatusReportItem)) {
                    showPendingStatusReportDialog();
            }
            else if (source.equals(closedreportItem)) {
                   showClosedReportsDialog();
            }
            else if (source.equals(grantsBySponsorItem)) {
                    showGrantsBySponsorDialog();
            }
            else if (source.equals(grantsByActivityTypeItem)) {
                    showGrantsByActivityTypeDialog();
            }
            else if (source.equals(grantsByAwardTypeItem)) {
                    showGrantsByAwardTypeDialog();
            }
            else if (source.equals(grantsByPIItem)) {
                    showGrantsByPIDialog();
            }
            else if (source.equals(summaryGrantsItem)) {
                    showSummaryGrantsDialog();
            }
            else if (source.equals(summaryPIGrantsItem)) {
                    showSummaryPIGrantsDialog();
            }
            else if (source.equals(graphGrantsBySponsorItem)) {
                    showGraphsGrantsSponsorDialog();
            }
            else if (source.equals(graphGrantsByActivityItem)) {
                    showGraphsGrantsActivityDialog();
            }
            else if (source.equals(graphGrantsByAwardItem)) {
                    showGraphsGrantsAwardDialog();
            }
            else if (source.equals(graphGrantsByPIItem)) {
                    showGraphsGrantsPIDialog();
            }
            else if (source.equals(currentActiveGrantsItem)) {
                    showCurrentActiveGrantsDialog();
            }
            else if (source.equals(currentSpecReviewGrantsItem)) {
                showSpecReviewGrantsDialog();
            }
            else if (source.equals(annualReportsItem)) {
                showAnnualReportsDialog();
            }
            /*else {
                log("Something went wrong: "+coeusMessageResources.parseMessageKey(
                                                    "funcNotImpl_exceptionCode.1100"));
            }*/
        }catch (edu.mit.coeus.exception.CoeusException  coeusException){
            CoeusOptionPane.showErrorDialog("CoeusReportMenu - CoeusException: "+ coeusException.getMessage());
        }catch (Exception  coeusException){
            CoeusOptionPane.showErrorDialog("CoeusReportMenu - Exception: "+ coeusException.getMessage());
        }finally{
            mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }

    private void showReportDialog() throws edu.mit.coeus.exception.CoeusException
    {
	CannedReportsController cannedReportsController = new CannedReportsController(mdiForm,true,unitNumber,unitName);
	cannedReportsController.display();    
    }
    
    private void showGrantsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GrantsBySchoolController grantsBySchoolController = new GrantsBySchoolController(mdiForm,true,unitNumber,unitName);
	grantsBySchoolController.display();    
    }

    private void showProposalsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	ProposalsBySchoolController proposalsBySchoolController = new ProposalsBySchoolController(mdiForm,true,unitNumber,unitName);
	proposalsBySchoolController.display();    
    }

    private void showPendingProposalsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	PendingProposalsController pendingProposalsController = new PendingProposalsController(mdiForm,true,unitNumber,unitName);
	pendingProposalsController.display();    
    }

    private void showSpecReviewProposalsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	SpecReviewProposalsController specReviewProposalsController = new SpecReviewProposalsController(mdiForm,true,unitNumber,unitName);
	specReviewProposalsController.display();    
    }

    private void showClosedReportsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	ClosedReportController closedReportController = new ClosedReportController(mdiForm,true,unitNumber,unitName);
	closedReportController.display();    
    }
    
    private void showGrantsBySponsorDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GrantsBySponsorController grantsBySponsorController = new GrantsBySponsorController(mdiForm,true,unitNumber,unitName);
	grantsBySponsorController.display();    
    }

    private void showGrantsByActivityTypeDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GrantsByActivityTypeController grantsByActivityTypeController = new GrantsByActivityTypeController(mdiForm,true,unitNumber,unitName);
	grantsByActivityTypeController.display();    
    }

    private void showGrantsByPIDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GrantsByPIController grantsByPIController = new GrantsByPIController(mdiForm,true,unitNumber,unitName);
	grantsByPIController.display();    
    }

    private void showSummaryGrantsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	SummaryGrantsController summaryGrantsController = new SummaryGrantsController(mdiForm,true,unitNumber,unitName);
	summaryGrantsController.display();    
    }

    private void showGrantsByAwardTypeDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GrantsByAwardTypeController grantsByAwardTypeController = new GrantsByAwardTypeController(mdiForm,true,unitNumber,unitName);
	grantsByAwardTypeController.display();    
    }

    private void showGraphsGrantsSponsorDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GraphsGrantsSponsorController graphsGrantsSponsorController = new GraphsGrantsSponsorController(mdiForm,true,unitNumber,unitName);
	graphsGrantsSponsorController.display();    
    }

    private void showGraphsGrantsActivityDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GraphsGrantsActivityController graphsGrantsActivityController = new GraphsGrantsActivityController(mdiForm,true,unitNumber,unitName);
	graphsGrantsActivityController.display();    
    }

    private void showGraphsGrantsAwardDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GraphsGrantsAwardController graphsGrantsAwardController = new GraphsGrantsAwardController(mdiForm,true,unitNumber,unitName);
	graphsGrantsAwardController.display();    
    }

    private void showGraphsGrantsPIDialog() throws edu.mit.coeus.exception.CoeusException
    {
	GraphsGrantsPIController graphsGrantsPIController = new GraphsGrantsPIController(mdiForm,true,unitNumber,unitName);
	graphsGrantsPIController.display();    
    }

    private void showSummaryPIGrantsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	SummaryPIGrantsController summaryPIGrantsController = new SummaryPIGrantsController(mdiForm,true,unitNumber,unitName);
	summaryPIGrantsController.display();    
    }

    private void showCurrentActiveGrantsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	CurrentActiveGrantsController currentActiveGrantsController = new CurrentActiveGrantsController(mdiForm,true,unitNumber,unitName);
	currentActiveGrantsController.display();    
    }
    
    private void showSpecReviewGrantsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	SpecReviewGrantsController specReviewGrantsController = new SpecReviewGrantsController(mdiForm,true,unitNumber,unitName);
	specReviewGrantsController.display();    
    }

    private void showAnnualReportsDialog() throws edu.mit.coeus.exception.CoeusException
    {
	AnnualReportsController annualReportsController = new AnnualReportsController(mdiForm,true,unitNumber,unitName);
	annualReportsController.display();    
    }
    
    private void showPendingStatusReportDialog() throws edu.mit.coeus.exception.CoeusException
    {
	PendingStatusReportController pendingStatusReportController = new PendingStatusReportController(mdiForm,true,unitNumber,unitName);
	pendingStatusReportController.display();    
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }
}
