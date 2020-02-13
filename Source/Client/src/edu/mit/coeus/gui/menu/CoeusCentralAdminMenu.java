/*
 * @(#)CoeusCentralAdminMenu.java 1.0 10/18/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import edu.mit.coeus.centraladmin.gui.EndOfMonthProcessForm;

import edu.mit.coeus.centraladmin.gui.FeedMaintenanceController;
import edu.mit.coeus.centraladmin.gui.FeedMaintenanceForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
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
import java.awt.Cursor;

/**
 * This class creates central admin menu for the coeus application.
 *
 * @version :1.0 October 18, 2002, 3:11 PM
 * @author Guptha
 */

public class CoeusCentralAdminMenu extends JMenu implements ActionListener{

    /*
     * central admin menu items
     */
    public CoeusMenuItem parameter, newPublicMessage, generateMasterDataFeed,
                  generateSponsorFeed,GenerateRolodexFeed,
                  populateSubcontractExpenseData,feedMaintenance,s2sSubList,
                  endOfMonthProcess,investigatorMaskChange,
                  subcontractForms,currentLock;;
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";

    private CoeusMenu coeusMenu;

    private CoeusAppletMDIForm mdiForm;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** Default constructor which constructs the central admin menu for coeus
     * application.
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CoeusCentralAdminMenu(CoeusAppletMDIForm mdiForm){
        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createMenu();
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
        java.util.Vector fileChildren = new java.util.Vector();
        parameter = new CoeusMenuItem("Parameter Maintenance...",null,true,true);
        parameter.setMnemonic('P');
        newPublicMessage = new CoeusMenuItem("New Public Message...",null,true,
                                                                        true);
        newPublicMessage.setMnemonic('L');
        generateMasterDataFeed = new CoeusMenuItem(
                            "Generate Master Data Feed......",null,true,true);
        generateMasterDataFeed.setMnemonic('G');
        generateSponsorFeed = new CoeusMenuItem("Generate Sponsor Feed...",
                                                                null,true,true);
        generateSponsorFeed.setMnemonic('S');
        GenerateRolodexFeed = new CoeusMenuItem("Generate Rolodex Feed...",
                                                                null,true,true);
        GenerateRolodexFeed.setMnemonic('R');
        populateSubcontractExpenseData = new CoeusMenuItem(
                        "Populate Subcontract Expense Data...",null,true,true);
        populateSubcontractExpenseData.setMnemonic('C');
        feedMaintenance = new CoeusMenuItem("Feed Maintenance...",null,true,true);
        feedMaintenance.setMnemonic('M');

        /*
         *Added by Geo for S2S Submission List
         */
        s2sSubList = new CoeusMenuItem("S2S Submission List...",null,true,true);
        s2sSubList.setMnemonic('U');
        
        endOfMonthProcess= new CoeusMenuItem("End of Month Process...",
                                                                null,true,true);
        endOfMonthProcess.setMnemonic('E');
        investigatorMaskChange = new CoeusMenuItem(
                                "Person Mass Change...",null,true,true);
        investigatorMaskChange.setMnemonic('N');
        subcontractForms = new CoeusMenuItem("Subcontract Forms...",null,true,true);
        subcontractForms.setMnemonic('F');
        
         // Added by chandra for the Locking
        
        currentLock = new CoeusMenuItem("Current Locks...",null,true,true);
        currentLock.setMnemonic('L');

        fileChildren.add(parameter);
        fileChildren.add(newPublicMessage);
        fileChildren.add(SEPERATOR);
        fileChildren.add(generateMasterDataFeed);
        fileChildren.add(generateSponsorFeed);
        fileChildren.add(GenerateRolodexFeed);
        fileChildren.add(populateSubcontractExpenseData);
        fileChildren.add(SEPERATOR);
        fileChildren.add(feedMaintenance);
        fileChildren.add(SEPERATOR);
        fileChildren.add(s2sSubList);
        fileChildren.add(SEPERATOR);
        fileChildren.add(endOfMonthProcess);
        fileChildren.add(SEPERATOR);
        fileChildren.add(investigatorMaskChange);
        fileChildren.add(SEPERATOR);
        fileChildren.add(subcontractForms);
        fileChildren.add(SEPERATOR);
        fileChildren.add(currentLock);
        coeusMenu = new CoeusMenu("Central Admin",null,fileChildren,true,true);
        coeusMenu.setMnemonic('C');

        //add listener
        parameter.addActionListener(this);
        newPublicMessage.addActionListener(this);
        generateMasterDataFeed.addActionListener(this);
        generateSponsorFeed.addActionListener(this);
        GenerateRolodexFeed.addActionListener(this);
        populateSubcontractExpenseData.addActionListener(this);
        feedMaintenance.addActionListener(this);
        endOfMonthProcess.addActionListener(this);
        investigatorMaskChange.addActionListener(this);
        subcontractForms.addActionListener(this);
        currentLock.addActionListener(this);
        s2sSubList.addActionListener(this);
    }

    /** This method is used to handle the action event for the central admin menu items.
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae){
        Object source = ae.getSource();
        try{
            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            if( source.equals(parameter) ){
                showParameterMaintenance();
            }else if(source.equals(currentLock)){
                   showLocksForm();
            }else if(source.equals(newPublicMessage)){
                  showNewPublicMessageForm();
            }else if(source.equals(generateMasterDataFeed)){
                  showGenerateMasterDataFeed();
            }else if(source.equals(generateSponsorFeed)){
                  showGenerateSponsorFeed();
            }else if(source.equals(GenerateRolodexFeed)){
                  showGenerateRolodoxFeed();
            }else if(source.equals(subcontractForms)){
                showSubcontractForm();
            }else if(source.equals(endOfMonthProcess)){
                showEndOfMonth();
            }else if(source.equals(populateSubcontractExpenseData)){
                showPopulateSubcontractExpenseData();
            }else if(source.equals(feedMaintenance)){
                showFeedMaintenance();
            }else if(source.equals(investigatorMaskChange)){
                showMasschange();
            }else if(source.equals(s2sSubList)){
                showS2SSubList();
            }else {
                log(coeusMessageResources.parseMessageKey(
                                                    "funcNotImpl_exceptionCode.1100"));
            }
        }catch (edu.mit.coeus.exception.CoeusException  coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (Exception  exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    // Added by chandra to show the Mass Change data
    private void showMasschange() throws edu.mit.coeus.exception.CoeusException{
        MassChangeForm massChangeForm = new MassChangeForm(mdiForm);
        massChangeForm.display();
        
    }
    
    /*to show the feed maintenance*/
    private void showFeedMaintenance()throws Exception{
            FeedMaintenanceForm feedMaintenanceForm= null;
            if((feedMaintenanceForm = (FeedMaintenanceForm)mdiForm.getFrame("Feed Maintenance")) != null ){
                if( feedMaintenanceForm.isIcon() ){
                    feedMaintenanceForm.setIcon(false);
                }
                feedMaintenanceForm.setSelected( true );
                return;
            }
            FeedMaintenanceController feedAMaintenanceController = new FeedMaintenanceController(mdiForm);
            feedAMaintenanceController.display();
    }
    
    /*to show the populate subcontract*/
    private void showPopulateSubcontractExpenseData(){
        PopulateSubcontractExpenseDataForm form = new PopulateSubcontractExpenseDataForm();
    }
    
    /*to show end of month*/
    private void showEndOfMonth(){
        EndOfMonthProcessForm form = new EndOfMonthProcessForm();
    }
    
    private void showSubcontractForm() throws CoeusClientException{
        edu.mit.coeus.centraladmin.gui.SubcontractForm form 
            = new edu.mit.coeus.centraladmin.gui.SubcontractForm(mdiForm);
        form.display();
    }
    
    private  void showNewPublicMessageForm() {
        NewPublicMessageForm newPublicMessageForm = new NewPublicMessageForm(mdiForm);
        newPublicMessageForm.display();
    }
    
    private  void showGenerateSponsorFeed() {
        GenerateSponsorFeedForm generateSponsorFeedForm = new GenerateSponsorFeedForm(mdiForm);
        generateSponsorFeedForm.display();
    }
    
    private  void showGenerateRolodoxFeed() {
        GenerateRolodexFeedForm generateRolodexFeedForm = new GenerateRolodexFeedForm(mdiForm);
        generateRolodexFeedForm.display();
    }
    
     private void showGenerateMasterDataFeed() {
        GenerateMasterDataFeedForm generateMasterDataFeedForm = new GenerateMasterDataFeedForm(mdiForm);
        generateMasterDataFeedForm.display();
    }
     
    // Added by chandra to get the lock details for the form.
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,false);
        currentLockForm.display();
    }
    
    /** Displays the Parameter Maintenance screen
     */
    private void showParameterMaintenance() {
        ParameterMaintenanceForm parameterForm = new ParameterMaintenanceForm();
        parameterForm.display();
    }

    /** Displays the Parameter Maintenance screen
     */
    private void showS2SSubList()throws Exception {
        edu.mit.coeus.s2s.gui.S2SSubmissionList s2SSubmissionList;
        if((s2SSubmissionList = (edu.mit.coeus.s2s.gui.S2SSubmissionList)mdiForm.getFrame("S2S Submission List")) != null ){
            if( s2SSubmissionList.isIcon() ){
                s2SSubmissionList.setIcon(false);
            }
            s2SSubmissionList.setSelected( true );
            return;
        }
        S2SSubmissionListController s2sSubListCntrl = new S2SSubmissionListController();
        s2sSubListCntrl.display();
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