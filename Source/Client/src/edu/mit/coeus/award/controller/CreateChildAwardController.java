/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * PMD check performed, and commented unused imports and variables on 24-AUG-2011
 * by Bharati
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.awt.*;
import java.awt.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import java.util.Vector;

/**
 * CreateChildAwardController.java
 * Created on March 20, 2004, 1:11 PM
 * @author  Vyjayanthi
 */
public class CreateChildAwardController extends AwardController
implements ActionListener {

    /** Holds an instance of <CODE>CreateChildAwardForm</CODE> */
    private CreateChildAwardForm createChildAwardForm;
    
    /** Holds the selected mit award number */
    private String selectedMitAwardNumber;
    
    /** Holds the status code of the selected award */
    private int statusCode;
    
    /** Holds the data required to build the tree hierarchy */
    private CoeusVector cvAwardHierarchy;
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                        AWARD_BUDGET_SERVLET;
    private static final char GET_INACTIVE_COST_ELEMENT_DETAILS = 'k';
    private static final String BUDGET_HAVE_INACTIVE_COST_ELEMENTS = "budgetSelect_exceptionCode.1061";
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    /** Holds true if OK button is clicked */
    private boolean okClicked;
    
    private static final int WIDTH = 500;
    private static final int ORIGINAL_HEIGHT = 150;
    private static final int NEW_HEIGHT = 350;
    
    private static final String WINDOW_TITLE = "Create Child Award";
    private static final String SELECT_MIT_AWARD= "award_exceptionCode.1003";
    private static final String CHILD_AWARD_SUFFIX = "-001";
    private static final String NEW_CHILD_AWARD_TITLE = "New Child Award : ";
    private static final String CREATE_CHILD_AWARD_TITLE = "Create Child Award : ";
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** Creates a new instance of CreateChildAwardController */
    public CreateChildAwardController() {
        createChildAwardForm = new CreateChildAwardForm(CoeusGuiConstants.getMDIForm(), true);
        registerComponents();
    }
    
    /** Creates a new instance of AwardHierarchyController
     * @param mitAwardNumber
     */
    public CreateChildAwardController(String mitAwardNumber, CoeusVector cvAwardHierarchy) {
        this();
        this.cvAwardHierarchy = cvAwardHierarchy;
        setFormData(mitAwardNumber);
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        createChildAwardForm.dlgCreateChildAward.show();
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return createChildAwardForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return createChildAwardForm;
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
        java.awt.Component[] components = { createChildAwardForm.btnOK, 
        createChildAwardForm.btnCancel,
        createChildAwardForm.rdBtnNew,
        createChildAwardForm.rdBtnCopyFromParent,
        createChildAwardForm.rdBtnSelectAward,
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        createChildAwardForm.setFocusTraversalPolicy(traversePolicy);
        createChildAwardForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        //Add listeners to all the buttons
        createChildAwardForm.btnOK.addActionListener(this);
        createChildAwardForm.btnCancel.addActionListener(this);
        
        //Add listener for the New and Child radio buttons
        createChildAwardForm.rdBtnNew.addActionListener(this);
        createChildAwardForm.rdBtnCopyFromParent.addActionListener(this);
        createChildAwardForm.rdBtnSelectAward.addActionListener(this);
        
        createChildAwardForm.dlgCreateChildAward.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
    }
    
    /** To set the default focus for the component
     */
    private void requestDefaultFocus(){
        createChildAwardForm.btnCancel.requestFocusInWindow();
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
        selectedMitAwardNumber = data.toString();
        
        createChildAwardForm.dlgCreateChildAward.setTitle(WINDOW_TITLE);
        
        createChildAwardForm.lblCopyFrom.setText(
            createChildAwardForm.lblCopyFrom.getText() + selectedMitAwardNumber);
        
        //Set the tree data
        createChildAwardForm.pnlAwardHierarchyTree.construct(selectedMitAwardNumber, cvAwardHierarchy);
        
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
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(createChildAwardForm.btnOK)) {
            showAward();
        }else if( source.equals(createChildAwardForm.btnCancel)) {
            createChildAwardForm.dlgCreateChildAward.dispose();
        }else if(source.equals(createChildAwardForm.rdBtnNew) ||
        source.equals(createChildAwardForm.rdBtnCopyFromParent)) {
            
            //Hide the award hierarchy if it is visible
            if( createChildAwardForm.pnlAwardHierarchy.isVisible() ){
                
                /** Code for focus traversal - start */
                java.awt.Component[] components = { createChildAwardForm.btnOK, 
                createChildAwardForm.btnCancel,
                createChildAwardForm.rdBtnNew,
                createChildAwardForm.rdBtnCopyFromParent,
                createChildAwardForm.rdBtnSelectAward,
                };
                ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
                createChildAwardForm.setFocusTraversalPolicy(traversePolicy);
                createChildAwardForm.setFocusCycleRoot(true);

                /** Code for focus traversal - end */

                createChildAwardForm.pnlAwardHierarchy.setVisible(false);
                createChildAwardForm.dlgCreateChildAward.setSize(WIDTH, ORIGINAL_HEIGHT);
            }
            
        }else if(source.equals(createChildAwardForm.rdBtnSelectAward)) {
            
            //Show the award hierarchy if it is not visible
            if( !createChildAwardForm.pnlAwardHierarchy.isVisible() ){
                
                /** Code for focus traversal - start */
                java.awt.Component[] components = { createChildAwardForm.btnOK, 
                createChildAwardForm.btnCancel,
                createChildAwardForm.rdBtnNew,
                createChildAwardForm.rdBtnCopyFromParent,
                createChildAwardForm.rdBtnSelectAward,
                createChildAwardForm.pnlAwardHierarchyTree.treeAwardHierarchy
                };
                ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
                createChildAwardForm.setFocusTraversalPolicy(traversePolicy);
                createChildAwardForm.setFocusCycleRoot(true);

                /** Code for focus traversal - end */

                createChildAwardForm.pnlAwardHierarchyTree.setSelectedObject(selectedMitAwardNumber);
                createChildAwardForm.pnlAwardHierarchy.setVisible(true);
                createChildAwardForm.dlgCreateChildAward.setSize(WIDTH, NEW_HEIGHT);
            }
        }
    }
    
    /** Open the award detail screen in the specified mode */
    private void showAward(){
        
        AwardBaseWindowController awardBaseWindowController;
        
        String rootMitAwardNumber = selectedMitAwardNumber.substring(0, 6) + CHILD_AWARD_SUFFIX;
        
        AwardHierarchyBean awardHierarchyBean = new AwardHierarchyBean();
        awardHierarchyBean.setParentMitAwardNumber(selectedMitAwardNumber);
        awardHierarchyBean.setRootMitAwardNumber(rootMitAwardNumber);
        awardHierarchyBean.setStatusCode(statusCode);
        
        AwardBean awardBean = new AwardBean();
        //Commented Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        if( createChildAwardForm.rdBtnNew.isSelected() ){
            awardBean.setMode(NEW_CHILD);
            
            createChildAwardForm.dlgCreateChildAward.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            createChildAwardForm.dlgCreateChildAward.getGlassPane().setVisible(true);
            
            awardBaseWindowController = new AwardBaseWindowController(
                    NEW_CHILD_AWARD_TITLE, NEW_CHILD, awardBean, awardHierarchyBean);
            awardBaseWindowController.display();
            if( awardBaseWindowController.isDisplayed() ){
                //Set the flag to dispose the hierarchy screen
                okClicked = true;
            }else {
                okClicked = false;
            }
        }else {
            if( createChildAwardForm.rdBtnCopyFromParent.isSelected() ){
                awardBean.setMitAwardNumber(selectedMitAwardNumber);
            }else{
                AwardHierarchyBean selectedBean = createChildAwardForm.pnlAwardHierarchyTree.getSelectedObject();
                if( selectedBean == null ) {
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(SELECT_MIT_AWARD));
                    return ;
                }
                awardBean.setMitAwardNumber(selectedBean.getMitAwardNumber());
            }
            awardBean.setMode(NEW_CHILD_COPIED);
            
            //get an award number
            String awardNumber = awardBean.getMitAwardNumber();
            //get the award budget details and cost element details
            //if budget holds inactive cost elements then it returns true
            boolean allow_copy = isInactiveCostCEPresent(awardNumber);
            int selection;
            if(allow_copy){
                selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(BUDGET_HAVE_INACTIVE_COST_ELEMENTS),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                //If yes button clicked then allow to copy the award budget
                if(selection ==CoeusOptionPane.SELECTION_YES){
                    allow_copy = true;
                } else if(selection ==CoeusOptionPane.SELECTION_NO){
                    allow_copy = false;
                }
            }else{
                allow_copy = true;
            }
            createChildAwardForm.dlgCreateChildAward.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            createChildAwardForm.dlgCreateChildAward.getGlassPane().setVisible(true);
            
            //if allow_copy is true then allow to create child award
            if(allow_copy){
                awardBaseWindowController = new AwardBaseWindowController(
                        CREATE_CHILD_AWARD_TITLE, NEW_CHILD_COPIED, awardBean, awardHierarchyBean);
                awardBaseWindowController.display();
                if( awardBaseWindowController.isDisplayed() ){
                    //Set the flag to dispose the hierarchy screen
                    okClicked = true;
                }else {
                    okClicked = false;
                }
            }
        }
//        awardBaseWindowController.display();
//        if( awardBaseWindowController.isDisplayed() ){
//            //Set the flag to dispose the hierarchy screen
//            okClicked = true;
//        }else {
//            okClicked = false;
//        }
        //Commented and added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        createChildAwardForm.dlgCreateChildAward.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        createChildAwardForm.dlgCreateChildAward.getGlassPane().setVisible(false);
        
        createChildAwardForm.dlgCreateChildAward.dispose();
    }

    /** Getter for property okClicked.
     * @return Value of property okClicked.
     *
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /** Method to clean all objects */
    public void cleanUp() {
        createChildAwardForm = null;
        selectedMitAwardNumber = null;
        cvAwardHierarchy = null;
        coeusMessageResources = null;
    }
    
    /**
     * Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
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
    //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
}
