/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * PMD check performed, and commented unused imports and variables on 24-AUG-2011
 * by Bharati
 */

package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;

/**
 * AwardHierarchyController.java
 * Created on March 16, 2004, 2:57 PM
 * @author  Vyjayanthi
 */
public class AwardHierarchyController extends AwardController
implements ActionListener, TreeSelectionListener {
    
    /** Holds an instance of <CODE>AwardHierarchyForm</CODE> */
    private AwardHierarchyForm awardHierarchyForm;
    
    /** Holds the selected mit award number */
    private String selectedMitAwardNumber;
    
    /** Holds the boolean values if user has rights to create/modify/view awards */
    private CoeusVector cvUserRights = new CoeusVector();
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                        AWARD_BUDGET_SERVLET;
    private static final char GET_INACTIVE_COST_ELEMENT_DETAILS = 'k';
    private static final String BUDGET_HAVE_INACTIVE_COST_ELEMENTS = "budgetSelect_exceptionCode.1061";
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    private static final int CREATE_AWARD = 0;
    private static final int MODIFY_AWARD = 1;
    private static final int VIEW_AWARD = 2;
    private static final String WINDOW_TITLE = "Award Hierarchy for award ";
    private static final String SELECT_MIT_AWARD= "award_exceptionCode.1003";
    private static final String NEW_ENTRY_AWARD_TITLE = "Award New Entry : ";
    private static final String CORRECT_AWARD_TITLE = "Correct Award : ";
    private static final String DISPLAY_AWARD_TITLE = "Display Award : ";
    
    /** Creates a new instance of AwardHierarchyController */    
    public AwardHierarchyController(){
        awardHierarchyForm = new AwardHierarchyForm(CoeusGuiConstants.getMDIForm(), true);
        registerComponents();
    }
    
    /** Creates a new instance of AwardHierarchyController
     * @param mitAwardNumber
     */
    public AwardHierarchyController(String mitAwardNumber) {
        this();
        setFormData(mitAwardNumber);
        formatFields();
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        awardHierarchyForm.dlgAwardHierarchy.show();
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        
        //If user has right to create award, enable Copy and New Child buttons
        if( ((Boolean)cvUserRights.get(CREATE_AWARD)).booleanValue() ){
            awardHierarchyForm.btnCopy.setEnabled(true);
            awardHierarchyForm.btnNewChild.setEnabled(true);
        }
        
        //If user has right to modify award, enable New Entry and Correct buttons
        if( ((Boolean)cvUserRights.get(MODIFY_AWARD)).booleanValue() ){
            awardHierarchyForm.btnNewEntry.setEnabled(true);
            awardHierarchyForm.btnCorrect.setEnabled(true);
        }
        
        //If user has right to view award, enable the Display button
        if( ((Boolean)cvUserRights.get(VIEW_AWARD)).booleanValue() ){
            awardHierarchyForm.btnDisplay.setEnabled(true);
        }
        
        /** Disable the buttons Copy, New Child, New Entry, Correct if any award
         * is open in Modify(M) or New Entry(E) or New Child(C) or New Child Copied(P) modes
         */
        if( isAwardWindowOpen(EMPTY, CORRECT_AWARD, false) ){
            awardHierarchyForm.btnCopy.setEnabled(false);
            awardHierarchyForm.btnNewChild.setEnabled(false);
            awardHierarchyForm.btnNewEntry.setEnabled(false);
            awardHierarchyForm.btnCorrect.setEnabled(false);
        }
        
    }
    
    /** An overridden method of the controller
     * @return awardHierarchyForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return awardHierarchyForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { awardHierarchyForm.btnCorrect,
        awardHierarchyForm.btnNewEntry,
        awardHierarchyForm.btnDisplay,
        awardHierarchyForm.btnCopy,
        awardHierarchyForm.btnNewChild,
        awardHierarchyForm.btnClose,
        awardHierarchyForm.chkDetails,
        awardHierarchyForm.pnlAwardHierarchyTree.treeAwardHierarchy
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardHierarchyForm.setFocusTraversalPolicy(traversePolicy);
        awardHierarchyForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        //Add listeners to all the buttons
        awardHierarchyForm.btnClose.addActionListener(this);
        awardHierarchyForm.btnCopy.addActionListener(this);
        awardHierarchyForm.btnCorrect.addActionListener(this);
        awardHierarchyForm.btnDisplay.addActionListener(this);
        awardHierarchyForm.btnNewChild.addActionListener(this);
        awardHierarchyForm.btnNewEntry.addActionListener(this);
        
        //Add listener for the check box component
        awardHierarchyForm.chkDetails.addActionListener(this);
        
        //Set the tree selection listener for the tree
        awardHierarchyForm.pnlAwardHierarchyTree.setTreeSelectionListener(this);
        
        awardHierarchyForm.dlgAwardHierarchy.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
    }
    
    /** To set the default focus for the component
     */
    private void requestDefaultFocus(){
        awardHierarchyForm.btnClose.requestFocusInWindow();
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() {
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(true);
        
        selectedMitAwardNumber = data.toString();
        
        //Set the window title
        awardHierarchyForm.dlgAwardHierarchy.setTitle( WINDOW_TITLE + selectedMitAwardNumber );
        
        //Construct the tree with the given mit award number
        awardHierarchyForm.pnlAwardHierarchyTree.construct(selectedMitAwardNumber);
        
        //Get the user rights
        cvUserRights = awardHierarchyForm.pnlAwardHierarchyTree.getUserRights();
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(false);
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /** Performs action on selecting different tree nodes
     * @param treeSelectionEvent holds the generated treeSelectionEvent
     */
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        AwardHierarchyBean selectedBean = awardHierarchyForm.pnlAwardHierarchyTree.getSelectedObject();
        if( selectedBean == null ) return ;
        selectedMitAwardNumber = selectedBean.getMitAwardNumber();
        if( awardHierarchyForm.chkDetails.isSelected() ){
            awardHierarchyForm.pnlAwardDetails.setFormData(selectedMitAwardNumber);
        }
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source.equals(awardHierarchyForm.btnCorrect)){
            showCorrectAward();
        }else if(source.equals(awardHierarchyForm.btnClose)){
            awardHierarchyForm.dlgAwardHierarchy.dispose();
        }else if(source.equals(awardHierarchyForm.btnCopy)){
            showCopyAward();
        }else if(source.equals(awardHierarchyForm.btnDisplay)){
            displayAward();
        }else if(source.equals(awardHierarchyForm.btnNewChild)){
            showCreateChild();
        }else if(source.equals(awardHierarchyForm.btnNewEntry)){
            showNewEntry();
        }else if(source.equals(awardHierarchyForm.chkDetails)){
            if( awardHierarchyForm.pnlAwardHierarchyTree.treeAwardHierarchy.isSelectionEmpty() &&
            !(awardHierarchyForm.pnlAwardDetails.isVisible())) {
                awardHierarchyForm.chkDetails.setSelected(false);
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_MIT_AWARD));
                return ;
            }
            showDetails( awardHierarchyForm.chkDetails.isSelected() );
        }
    }
    
    /** Displays the award details if Details checkbox is selected
     * @param showDetails true if Details checkbox is checked, false otherwise
     */
    private void showDetails(boolean showDetails){
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;

        awardHierarchyForm.remove(awardHierarchyForm.pnlAwardHierarchyTree);

        if( showDetails ){
            //Show the award details
            awardHierarchyForm.pnlAwardDetails.setFormData(selectedMitAwardNumber);
            awardHierarchyForm.pnlAwardDetails.setVisible(true);
            gridBagConstraints.gridheight = 7;
            awardHierarchyForm.add(awardHierarchyForm.pnlAwardHierarchyTree, gridBagConstraints);
        }else{
            //Hide the award details
            awardHierarchyForm.pnlAwardDetails.setVisible(false);
            gridBagConstraints.gridheight = 8;
            awardHierarchyForm.add(awardHierarchyForm.pnlAwardHierarchyTree, gridBagConstraints);
        }
    }
    
    /** Opens the Award in New Entry mode
     */
    private void showNewEntry(){
        if( !isAwardSelected() ) return ;
        
        String awardNumber = awardHierarchyForm.pnlAwardHierarchyTree.getSelectedObject().getMitAwardNumber();
        
        if(isAwardWindowOpen(awardNumber, CORRECT_AWARD)) {
            return ;
        }
        
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(true);
        
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //get the award budget dtails
        //if it holds inactive cost elements then it returns true
        boolean allow_copy = isInactiveCostCEPresent(awardNumber);
        int selection;
        if(allow_copy){
            selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(BUDGET_HAVE_INACTIVE_COST_ELEMENTS),
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            //if user selects yes then allow to copy the award budget
            if(selection ==CoeusOptionPane.SELECTION_YES){
                allow_copy = true;
            } else if(selection ==CoeusOptionPane.SELECTION_NO){
                allow_copy = false;
            }
        }else{
            allow_copy = true;
        }
        //if allow_copy is true then allow to create the new entry for award
        if(allow_copy){
            AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController(NEW_ENTRY_AWARD_TITLE, NEW_ENTRY, awardBean);
            awardBaseWindowController.display();
        }
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(false);
        
        awardHierarchyForm.dlgAwardHierarchy.dispose();
    }
    
    /** Opens an Award for modification
     */
    private void showCorrectAward(){
        if( !isAwardSelected() ) return ;
        
        String awardNumber = awardHierarchyForm.pnlAwardHierarchyTree.getSelectedObject().getMitAwardNumber();
        
        if(isAwardWindowOpen(awardNumber, CORRECT_AWARD)) {
            return ;
        }
        
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(true);
        
        AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController(CORRECT_AWARD_TITLE, CORRECT_AWARD, awardBean);
        awardBaseWindowController.display();
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(false);
        
        awardHierarchyForm.dlgAwardHierarchy.dispose();
    }
    
    /** Open an Award for display */
    private void displayAward(){
        if( !isAwardSelected() ) return ;
        
        String awardNumber = awardHierarchyForm.pnlAwardHierarchyTree.getSelectedObject().getMitAwardNumber();
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award
        if(!canViewAward(awardNumber)){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
            return;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - ends
        if(isAwardWindowOpen(awardNumber, DISPLAY_MODE)) {
            return ;
        }
        
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(true);
        
        AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController(DISPLAY_AWARD_TITLE, DISPLAY_MODE , awardBean);
        awardBaseWindowController.display();
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(false);
        
        awardHierarchyForm.dlgAwardHierarchy.dispose();
    }
    
    /** To check if any award is selected in the tree hierarchy
     * @return true if award is selected, false otherwise
     */
    private boolean isAwardSelected(){
        if( awardHierarchyForm.pnlAwardHierarchyTree.treeAwardHierarchy.isSelectionEmpty() ){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(SELECT_MIT_AWARD));
            return false;
        }
        return true;
    }
    
    /** Display the Copy Award screen */
    private void showCopyAward(){
        if( !isAwardSelected() ) return ;
        AwardHierarchyBean selectedBean = awardHierarchyForm.pnlAwardHierarchyTree.getSelectedObject();
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(true);
        
        AwardCopyController awardCopyController = new AwardCopyController(selectedMitAwardNumber, 
            awardHierarchyForm.pnlAwardHierarchyTree.getAwardHierarchyData());
        awardCopyController.setDescendents(awardHierarchyForm.pnlAwardHierarchyTree.getDescendents(selectedBean));
        awardHierarchyForm.pnlAwardHierarchyTree.resetDescendents();
        awardCopyController.display();
        
        if( awardCopyController.isAwardCopied() ){
            //Refresh the tree hierarchy with the copied awards
            AwardCopyBean awardCopyBean = awardCopyController.getAwardCopyBean();
            if( awardCopyBean != null ){
                AwardCopyForm awardCopyForm = (AwardCopyForm)awardCopyController.getControlledUI();
                if(awardCopyForm.rdBtnChild.isSelected()) {
                    selectedBean = awardCopyForm.pnlAwardHierarchyTree.getSelectedObject();
                    awardHierarchyForm.pnlAwardHierarchyTree.construct(
                    awardCopyBean.getTargetAwardNumber(), awardCopyBean.getDescendents(), selectedBean);
                }else {
                    awardHierarchyForm.pnlAwardHierarchyTree.construct(
                    awardCopyBean.getTargetAwardNumber(), awardCopyBean.getDescendents());
                }
            }
        }
        //Added for bug fix #1841 start
        awardHierarchyForm.dlgAwardHierarchy.setTitle( WINDOW_TITLE + selectedBean.getMitAwardNumber());
        //Bug fix end #1841
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(false);
    }
    
    /** Display the Create Child screen */
    private void showCreateChild(){
        if( !isAwardSelected() ) return ;
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(true);
        
        AwardHierarchyBean selectedBean = awardHierarchyForm.pnlAwardHierarchyTree.getSelectedObject();
        CreateChildAwardController createChildAwardController = new CreateChildAwardController(
            selectedMitAwardNumber, awardHierarchyForm.pnlAwardHierarchyTree.getAwardHierarchyData());
        createChildAwardController.setStatusCode(selectedBean.getStatusCode());
        createChildAwardController.display();
        
        awardHierarchyForm.dlgAwardHierarchy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        awardHierarchyForm.dlgAwardHierarchy.getGlassPane().setVisible(false);
        
        if( createChildAwardController.isOkClicked() ){
            awardHierarchyForm.dlgAwardHierarchy.dispose();
        }
    }
    
    /** Method to clean all objects */
    public void cleanUp() {
        awardHierarchyForm = null;
        selectedMitAwardNumber = null;
        cvUserRights = null;
        coeusMessageResources = null;
    }
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this award
     * @param awardNumber
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewAward(String awardNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        char CAN_VIEW_AWARD =  'f' ;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalActionServlet";        
        request.setFunctionType(CAN_VIEW_AWARD);
        request.setDataObject(awardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
    }    
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    /**
     * This method fetches the award budget details with cost element status
     * @param awardNumber
     * @return boolean value
     */
    private boolean isInactiveCostCEPresent(String awardNumber){
        CoeusVector cvCopyData=null;
        RequesterBean request = new RequesterBean();
        boolean inACtive = false;
        ResponderBean response = null;
        request.setId(awardNumber);
        request.setFunctionType(GET_INACTIVE_COST_ELEMENT_DETAILS);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            inACtive =((Boolean) response.getDataObject()).booleanValue();
        }
        return inACtive;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
}
