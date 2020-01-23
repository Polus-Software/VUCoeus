/*
 * @(#)CoeusFileMenu.java 1.0 10/18/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.*;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CurrentLockForm;


/**
 * This class creates File menu for the coeus application.
 *
 * @version :1.0 October 18, 2002, 3:11 PM
 * @author Guptha
 */

public class CoeusFileMenu extends CoeusMenu implements ActionListener{

    /*
     * file menu items
     */
    private CoeusMenuItem inbox,close,save,saveAs,/*printSetup,*/ changePassword,preferences,exit,
    /*Case 2110 Start*/currentLock/*Case 2110 End*/
            /*Added for Case#3682 - Enhancements related to Delegations - Start*/
            ,delegations
            /*Added for Case#3682 - Enhancements related to Delegations  - End*/;

    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";

    private CoeusMenu coeusMenu;

    private CoeusAppletMDIForm mdiForm;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    private CoeusInternalFrame internalFrame;
    private UserPreferencesForm userPreferencesForm;
    private ChangePassword changePassWord;
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** Default constructor which constructs the file menu for coeus application.
     * @param mdiForm  CoeusAppletMDIForm
     */

    public CoeusFileMenu(CoeusAppletMDIForm mdiForm){
        super("File");
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createMenu();
    }

    /**
     * This method is used to get the file menu
     *
     * @return JMenu coeus file menu
     */
    public JMenu getMenu(){
        return this;
    }

    /**
     * This method is used to create file menu for coeus application.
     */
    private void createMenu(){
        java.util.Vector fileChildren = new java.util.Vector();
        inbox = new CoeusMenuItem("Inbox",null,true,true);
        inbox.setMnemonic('I');
        close = new CoeusMenuItem("Close",null,true,true);
        close.setMnemonic('C');
        save = new CoeusMenuItem("Save",null,true,true);
        save.setMnemonic('S');
        // Bug Fix 948 - start
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        // Bug Fix 948 - End
        saveAs = new CoeusMenuItem("Save As",null,true,true);
        saveAs.setMnemonic('A');
        
        //Commented since we are not using it in Coeus 4.0
        //printSetup = new CoeusMenuItem("Print Setup..",null,true,true);
        //printSetup.setMnemonic('P');
        //Commented since we are not using it in Coeus 4.0 End
        
        changePassword = new CoeusMenuItem("Change Password",null,true,true);
        changePassword.setMnemonic('h');
        preferences = new CoeusMenuItem("Preferences",null,true,true);
        preferences.setMnemonic('R');
        
        //Added for Case#3682 - Enhancements related to Delegations - Start
        delegations = new CoeusMenuItem("Delegations",null,true,true);
        delegations.setMnemonic('D');
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        exit = new CoeusMenuItem("Exit",null,true,true);
        exit.setMnemonic('X');
        //Case 2110 Start
        currentLock = new CoeusMenuItem("Current Locks",null,true,true);
        currentLock.setMnemonic('L');
        //Case 2110 End
        fileChildren.add(inbox);
        fileChildren.add(SEPERATOR);
        fileChildren.add(save);
        fileChildren.add(saveAs);
        fileChildren.add(SEPERATOR);
        fileChildren.add(close);
        
        //Commented since we are not using it in Coeus 4.0
        //fileChildren.add(printSetup);
        //Commented since we are not using it in Coeus 4.0 End 
        
        fileChildren.add(SEPERATOR);
        fileChildren.add(changePassword);
        //Case 2110 Start
        fileChildren.add(SEPERATOR);
        fileChildren.add(currentLock);
        fileChildren.add(SEPERATOR);
        //Case 2110 End
        
        //Added for Case#3682 - Enhancements related to Delegations - Start
        fileChildren.add(delegations);
        fileChildren.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        fileChildren.add(preferences);
        fileChildren.add(SEPERATOR);
        fileChildren.add(exit);
        setIcon(null);
        setChildren(fileChildren); 
        setVisible(true);
        setEnabled(true);
        setMnemonic('F');
//        coeusMenu = new CoeusMenu("File",null,fileChildren,true,true);
//        coeusMenu.setMnemonic('F');
        // add listener
        inbox.addActionListener(this);
        close.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        //printSetup.addActionListener(this);
        //Commented since we are not using it in Coeus 4.0 End
        
        changePassword.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        delegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        preferences.addActionListener(this);
        exit.addActionListener(this);
        //Case 2110 Start
        currentLock.addActionListener(this);
        //Case 2110 End
        
        if(!CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID)) {
            changePassword.setEnabled(false);
        }
    }

    /** This method is used to handle the action event for the file menu items.
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae) {
        Object actSource = ae.getSource();
        try{
        if( actSource.equals( close ) ) {
            closeInternalFrame( mdiForm.getSelectedFrame() );            
        }else if( actSource.equals( exit ) ) { 
            exitApplication();
        } else if( actSource.equals( save ) ) {
            saveDetails();
        } else if( actSource.equals( saveAs ) ) {
            saveAsDetails();
        } else if( actSource.equals( inbox ) ) {
            showInboxDetails();
        }else if(actSource.equals(preferences)){
            showPreference();
        }else if(actSource.equals(changePassword)){
            showChangePassword();
        }//Case 2110 Start
        else if(actSource.equals(currentLock)){
            showLocksForm();
        }//Case 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - Start
        else if (actSource.equals(delegations)){
            displayUserDelegation();
        }
        //Added for Case#3682 - Enhancements related to Delegations - End
        else {        
            log(coeusMessageResources.parseMessageKey(
                                            "funcNotImpl_exceptionCode.1100"));
        }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    // Added by chandra to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End chandra
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Displays Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassWord == null) {
            changePassWord = new ChangePassword();
        }
        changePassWord.display();
    }// End Nadh
    
    //Case 2110 Start To get the Current Locks of user 
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }//Case 2110 End
    
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
                mdiForm.logout();
                mdiForm.dispose();
                //Fix for JIRA COEUSQA-2586 - START
                System.exit(0);
                //Fix for JIRA COEUSQA-2586 - END
            }
        }
    }
    
    /**
     * Method used to save the detail form
     */
    
    public void saveDetails(){
        //closeInternalFrame( mdiForm.getSelectedFrame() );
        internalFrame = (CoeusInternalFrame) mdiForm.getSelectedFrame();
        if( internalFrame != null ){                               
            internalFrame.saveActiveSheet();
        }
    }
    
    /* Method to save the table contents to the opted locations
     *
     */
     public void saveAsDetails(){
        //closeInternalFrame( mdiForm.getSelectedFrame() );
        internalFrame = (CoeusInternalFrame) mdiForm.getSelectedFrame();
        if( internalFrame != null ){                               
            internalFrame.saveAsActiveSheet();
        }
     }
    /**
     * Method used to close the given internal frame.
     *
     * @param frame JInternalFrame which is to be closed.
     */
    public void closeInternalFrame( JInternalFrame frame ){
        if( frame != null ){                               
                frame.doDefaultCloseAction();
        }else{
            //if non of the internal frame are open it will close the mdi form
            mdiForm.dispose();
            //Fix for JIRA COEUSQA-2586 - START
            System.exit(0);
            //Fix for JIRA COEUSQA-2586 - END
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
    
    //Method to enable and disable the "Save" option...
    public void setSaveEnabled(boolean b){
	save.setEnabled(b);
    }
    
    public void formatMenuItems(String mode){
        if( CoeusInternalFrame.LIST_MODE.equals(mode)){
            save.setEnabled(false);
            saveAs.setEnabled(true);
        }else if( CoeusInternalFrame.DETAILS_MODE.equals(mode)){
            save.setEnabled(true);
            saveAs.setEnabled(false);
        }else if( CoeusInternalFrame.OTHER_MODE.equals(mode)){
            save.setEnabled(false);
            saveAs.setEnabled(false);
        }
    }
}