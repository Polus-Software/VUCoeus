/*
 * AwardValidationController.java
 *
 * Created on November 10, 2011, 8:27 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller; 

import edu.mit.coeus.award.gui.AwardValidationForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author satheeshkumarkn
 */
public class AwardValidationController extends javax.swing.JComponent implements ActionListener{
    
    private AwardValidationForm awardValidationForm;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgValidationwarnings;
    private int WIDTH = 650;
    private int HEIGHT = 525;
    private String mitAwardNumber;
    private String HEADER_MESSAGE= "award_validation_header_exceptionCode.1000";
    private boolean canChangeStatusToHold;
    private static final String VALIDATION_TITLE = "Validation";
    
    /** Creates a new instance of AwardValidationController */
    public AwardValidationController(CoeusAppletMDIForm mdiForm, String mitAwardNumber) {
        this.mdiForm = mdiForm;
        this.mitAwardNumber = mitAwardNumber;
        coeusMessageResources = CoeusMessageResources.getInstance();
        awardValidationForm = new AwardValidationForm(coeusMessageResources.parseMessageKey(HEADER_MESSAGE));
        registerComponents();
    }
    
    /**
     * Method to get the AwardValidationForm instance
     * @return Component
     */
    public Component getControlledUI() {
        return awardValidationForm;
    }
    
    /**
     * Method to set data to the validation form
     * @param data 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    // JM 10-7-2013 updated to allows errors and alerts
    public void setFormData(Object data) throws CoeusException {
    	Vector validations = (Vector) data;
        Vector vecErrorMsg = (Vector) validations.get(0);
        if(vecErrorMsg != null && !vecErrorMsg.isEmpty()){
            for(Object errorMessage : vecErrorMsg){
                awardValidationForm.addWarningMessageComponent((String)errorMessage,true);
            }
        }
        Vector vecAlertMsg = (Vector) validations.get(1);
        if(vecAlertMsg != null && !vecAlertMsg.isEmpty()){
            for(Object alertMessage : vecAlertMsg){
                awardValidationForm.addWarningMessageComponent((String)alertMessage,false);
            }
        }
    	/*	Vector vecWarningMsg = (Vector)data;
        if(vecWarningMsg != null && !vecWarningMsg.isEmpty()){
            for(Object warningMessage : vecWarningMsg){
                awardValidationForm.addWarningMessageComponent((String)warningMessage);
            }
        }
    */ 
    // JM END
    }
    
    /**
     * Method to now whether to change the status of award to hold. This will return true when user press 'Yes'
     * @return boolean - canChangeStatusToHold
     */
    public boolean canChangeStatusToHold(){
        return canChangeStatusToHold;
    }
    
    public void registerComponents() {
        awardValidationForm.btnYes.addActionListener(this);
        awardValidationForm.btnNo.addActionListener(this);
        java.awt.Component[] components={awardValidationForm.btnYes,awardValidationForm.btnNo};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardValidationForm.setFocusTraversalPolicy(traversePolicy);
        awardValidationForm.setFocusCycleRoot(true);
    }
    
    /**
     * Method to display the validation dialog
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    public void display()throws CoeusException{
        dlgValidationwarnings =new CoeusDlgWindow(mdiForm, VALIDATION_TITLE, true);
        dlgValidationwarnings.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                awardValidationForm.btnYes.requestFocusInWindow();
                awardValidationForm.btnYes.setFocusable(true);
                awardValidationForm.btnYes.requestFocus();
            }
        });
        dlgValidationwarnings.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                dlgValidationwarnings.dispose();
            }
        });
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dlgValidationwarnings.setSize(WIDTH, HEIGHT);
        Dimension dlgSize = dlgValidationwarnings.getSize();
        dlgValidationwarnings.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgValidationwarnings.getContentPane().add( awardValidationForm );
        dlgValidationwarnings.setResizable(false);
        dlgValidationwarnings.setVisible(true);
    }
    
    /**
     * Method to perfome action event
     * @param e - ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(awardValidationForm.btnYes)){
            canChangeStatusToHold = true;
        }else  if(source.equals(awardValidationForm.btnYes)){
            canChangeStatusToHold = false;
        }
        dlgValidationwarnings.dispose();
    }
    
    
    
    
    
}
