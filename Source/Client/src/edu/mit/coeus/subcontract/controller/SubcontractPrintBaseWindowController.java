/*
 * SubcontractPrintBaseWindowController.java
 *
 * Created on December 22, 2004, 12:24 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.subcontract.bean.SubContractBaseBean;
import edu.mit.coeus.subcontract.gui.SubcontractPrintBaseWindow;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.rtf.CoeusRTFForm;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class SubcontractPrintBaseWindowController extends SubcontractController implements ActionListener, VetoableChangeListener{
	
	private SubcontractPrintBaseWindow subcontractPrintBaseWindow;
	private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
	private CoeusMessageResources coeusMessageResources;
	private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    ////Added for Case#3682 - Enhancements related to Delegations - End
	private String winTitle = EMPTY_STRING;
    private String title = EMPTY_STRING;
    public SubContractBaseBean subContractBaseBean;
	private String subContractCode;
	private boolean closed = false;
	private CoeusRTFForm coeusRTFForm;
	private String fileNamePath;
	/** Creates a new instance of SubcontractPrintBaseWindowController */
	public SubcontractPrintBaseWindowController(String title, SubContractBaseBean subContractBaseBean,String fileNamePath) {
		if(subContractBaseBean!= null){
            this.subContractBaseBean = subContractBaseBean;
            subContractCode = subContractBaseBean.getSubContractCode();
        }
        this.winTitle = title;
		this.fileNamePath = fileNamePath;
		coeusMessageResources = CoeusMessageResources.getInstance();
		subcontractPrintBaseWindow = new SubcontractPrintBaseWindow(winTitle,mdiForm);
		registerComponents();
        initComponents();
	}
	
	/** displays inbox details. */
    private void showInboxDetails() {
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
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
	
	/**
	 * To show the Change password 
	 */
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }
	
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegations window
     */
    private void displayUserDelegation() {
      userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
	/**
	 * To show the preference Screen
	 */
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
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
                mdiForm.dispose();
            }
        }
    }
	
	
	public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            try{
                blockEvents(true);
            if(source.equals(subcontractPrintBaseWindow.btnClose) ||
            source.equals(subcontractPrintBaseWindow.mnuItmClose)){
                subcontractPrintBaseWindow.doDefaultCloseAction();
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmInbox)) {
                showInboxDetails();
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmChangePassword)) {
                showChangePassword();
            } else if((source.equals(subcontractPrintBaseWindow.mnuItmPrint)) ||
            source.equals(subcontractPrintBaseWindow.btnPrint)) {
                PrintSubcontract();
            } else if((source.equals(subcontractPrintBaseWindow.mnuItmPrintPreview))) {
                showPrintPreview();
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmExit)) {
                exitApplication();
                
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmPreferences)) {
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmDelegations)) {
                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmUnDo)) {
                
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmCut)) {
                cutAction();
            } else if(source.equals(subcontractPrintBaseWindow.mnuItmCopy)) {
                copyAction();
            }else if(source.equals(subcontractPrintBaseWindow.mnuItmPaste)) {
                pasteAction();
            } else {
                CoeusOptionPane.showInfoDialog("Functionality not implemented");
            }
            }catch (Exception e){
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
            finally{
                blockEvents(false);
            }
        }
        
        private void showPrintPreview(){
            coeusRTFForm.showPrintPreview();
        }
        
	// Added by chandra to perform Cut Action
        private void cutAction(){
            coeusRTFForm.cutAction();
        }
        
        // Added by chandra to perform copy Action
        private void copyAction(){
            coeusRTFForm.copyAction();
        }
        
        
        
        // Added by chandra to perform paste Action
        private void pasteAction(){
            coeusRTFForm.pastAction();
        }
        
	public void display() {
		try{
            if( mdiForm.getFrame(winTitle) == null) {
                mdiForm.putFrame(winTitle,subcontractPrintBaseWindow);
                mdiForm.getDeskTopPane().add(subcontractPrintBaseWindow);
                subcontractPrintBaseWindow.setSelected(true);
                subcontractPrintBaseWindow.setVisible(true);
            }
           
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
	}
        
        private void PrintSubcontract() throws Exception{
            coeusRTFForm.printData();
        }
	
	public void formatFields() {
	}
	
	public java.awt.Component getControlledUI() {
		return subcontractPrintBaseWindow;
	}
	
	public Object getFormData() {
		return null;
	}
	
	public void registerComponents() {
		subcontractPrintBaseWindow.addVetoableChangeListener(this);
		subcontractPrintBaseWindow.mnuEdit.addActionListener(this);
		subcontractPrintBaseWindow.mnuFile.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmChangePassword.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmClose.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmCopy.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmCut.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmExit.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmInbox.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmPaste.addActionListener(this);
                //Added for Case#3682 - Enhancements related to Delegations - Start
                subcontractPrintBaseWindow.mnuItmDelegations.addActionListener(this);
                //Added for Case#3682 - Enhancements related to Delegations - End
		subcontractPrintBaseWindow.mnuItmPreferences.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmPrint.addActionListener(this);
		subcontractPrintBaseWindow.mnuItmUnDo.addActionListener(this);
		subcontractPrintBaseWindow.btnClose.addActionListener(this);
		subcontractPrintBaseWindow.btnPrint.addActionListener(this);
                subcontractPrintBaseWindow.mnuItmPrintPreview.addActionListener(this);
		
	}
	
	private void initComponents() {
		Container subcontractPrintContainer = subcontractPrintBaseWindow.getContentPane();
		JPanel printPanel = new JPanel();
        
        printPanel.setLayout(new BorderLayout());
		
		coeusRTFForm = new CoeusRTFForm(mdiForm, fileNamePath);
		printPanel.add(coeusRTFForm);
		JScrollPane jScrollPane = new JScrollPane(printPanel);
		subcontractPrintContainer.add(jScrollPane);
	}
	public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
	}
	
	public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
		if (subcontractPrintBaseWindow != null) {
			subcontractPrintBaseWindow.setTitle(winTitle);
		}
	}
	
	public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
		return true;
	}
	
	public void cleanUp() {
	}
	
	public void vetoableChange(java.beans.PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
		if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
	}
	private void close() throws PropertyVetoException {
		
		mdiForm.removeFrame(winTitle);
		closed = true;
	}
}