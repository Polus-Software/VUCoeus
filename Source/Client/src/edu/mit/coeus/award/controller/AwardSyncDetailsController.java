/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 27-OCT-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.award.AwardLabelConstants;
import edu.mit.coeus.award.gui.AwardSyncDetailsForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;

/*
 * AwardSyncDetailsController.java
 *
 * Created on April 21, 2009, 12:14 PM
 * @author keerthyjayaraj
 */

public class AwardSyncDetailsController implements ActionListener{
    
    private AwardSyncDetailsForm syncDetailsForm;
//    private static final String WINDOW_TITLE = "Select Sync options for award ";
    
    /** Holds the current mit award number */
    private char syncMode;
    
    /** Holds the selected sync mode */
    private HashMap selectedSyncTarget = null;
    
    // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
    // Set this property if a sync action requires a SAP feed.
    private boolean enableSAPFeedValidate = false;
    // Message:Do you want to create SAP Feed for all the child awards?
    private static final String MSGKEY_SAP_FEED_REQUIRED = "awardDetail_exceptionCode.1069";
    public CoeusMessageResources coeusMessageResources;
    // COEUSDEV-563:End
    
    /** Creates a new instance of AwardSyncDetailsController */
    public AwardSyncDetailsController(char syncMode) {
        
        this.syncMode = syncMode;
        syncDetailsForm = new AwardSyncDetailsForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        setFormData();
        registerComponents();
    }
    
    public void setFormData() {
        syncDetailsForm.dlgAwardSyncDetails.getGlassPane().setVisible(true);
        String title = "";
        switch(syncMode){
            case( AwardConstants.ADD_SYNC ):
            title = AwardLabelConstants.ADD_SYNC;
            break;
            case( AwardConstants.MODIFY_SYNC ):
            title = AwardLabelConstants.MODIFY_SYNC;
            break;
            case( AwardConstants.DELETE_SYNC ):
            title = AwardLabelConstants.DELETE_SYNC;
            break;
            default:
            title = AwardLabelConstants.SYNC;
            break;
        }
        syncDetailsForm.dlgAwardSyncDetails.setTitle( title );
    }
    
    public HashMap display() {
        selectedSyncTarget = null;
        syncDetailsForm.dlgAwardSyncDetails.setVisible(true);
        return getSelectedSyncTarget();
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        //Set Focus Traversal Policy
        java.awt.Component[] components = { syncDetailsForm.btnOK,
        syncDetailsForm.btnCancel,
        syncDetailsForm.rbtnActive,
        syncDetailsForm.rbtnAll,
        //COEUSDEV 253: Add FabE and CS accounts to Sync Screen
        syncDetailsForm.chkFabE,
        syncDetailsForm.chkCS
        //COEUSDEV 253 End
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        syncDetailsForm.setFocusTraversalPolicy(traversePolicy);
        syncDetailsForm.setFocusCycleRoot(true);
        
        //Add listeners to all the buttons
        syncDetailsForm.btnOK.addActionListener(this);
        syncDetailsForm.btnCancel.addActionListener(this);
        
        syncDetailsForm.dlgAwardSyncDetails.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
    }
    
    /** To set the default focus for the component*/
    private void requestDefaultFocus(){
        syncDetailsForm.btnOK.requestFocusInWindow();
    }
    
    public void actionPerformed(ActionEvent e) {
        
        Object source = e.getSource();
        //COEUSDEV 253: Add FabE and CS accounts to Sync Screen
        if(source.equals(syncDetailsForm.btnOK)){
            selectedSyncTarget = new HashMap();
            if(syncDetailsForm.rbtnActive.isSelected()){
                selectedSyncTarget.put(KeyConstants.SYNC_TARGET,AwardConstants.SYNC_ACTIVE_CHILDREN);
            }else if(syncDetailsForm.rbtnAll.isSelected()){
                selectedSyncTarget.put(KeyConstants.SYNC_TARGET,AwardConstants.SYNC_ALL_CHILDREN);
            }
            String includeFABE = (syncDetailsForm.chkFabE.isSelected())?"Y":"N";
            String includeCS   = (syncDetailsForm.chkCS.isSelected())?"Y":"N";
            selectedSyncTarget.put(KeyConstants.SYNC_FABE_ACCOUNTS,includeFABE);
            selectedSyncTarget.put(KeyConstants.SYNC_CS_ACCOUNTS,includeCS);
            // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
            if(enableSAPFeedValidate){
                int selectedOption = CoeusOptionPane.showQuestionDialog
                            (coeusMessageResources.parseMessageKey(MSGKEY_SAP_FEED_REQUIRED),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if( selectedOption == CoeusOptionPane.SELECTION_YES ){
                    selectedSyncTarget.put(KeyConstants.ADD_SAP_FEED_FOR_CHILD_AWARDS,"Y");
                }
            }
            // COEUSDEV-563:End
            syncDetailsForm.dlgAwardSyncDetails.dispose();
            //COEUSDEV 253: End
        }else if(source.equals(syncDetailsForm.btnCancel)){
            syncDetailsForm.dlgAwardSyncDetails.dispose();
        }
    }
    
    
    public HashMap getSelectedSyncTarget() {
        return this.selectedSyncTarget;
    }
    
    // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
    /* getter for property enableSAPFeedValidate
     * @return enableSAPFeedValidate
     */
    public boolean isEnableSAPFeedValidate() {
        return enableSAPFeedValidate;
    }
    
    /* Setter for property enableSAPFeedValidate
     * @param enableSAPFeedValidate
     */
    public void setEnableSAPFeedValidate(boolean enableSAPFeedValidate) {
        this.enableSAPFeedValidate = enableSAPFeedValidate;
    }   
    // COEUSDEV-563:End
}
