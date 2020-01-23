/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;

/**
 * AwardCopyController.java
 * Created on March 18, 2004, 11:51 AM
 * @author  Vyjayanthi
 */
public class AwardCopyController extends AwardController
implements ActionListener{
    
    /** Holds an instance of <CODE>AwardCopyForm</CODE> */
    private AwardCopyForm awardCopyForm;
    
    /** Holds the selected mit award number */
    private String selectedMitAwardNumber;
    
    /** Holds data to be send to server to perform copy operation */
    private AwardCopyBean awardCopyBean;
    
    /** Holds the data required to build the tree hierarchy */
    private CoeusVector cvAwardHierarchy;
    
    /** Holds all the child award numbers for the selected award number */
    private CoeusVector cvDescendents;
    
    /** Holds true if any award is copied, false otherwise */
    private boolean awardCopied;
    
    private static final String WINDOW_TITLE = "Award Copy";
    private static final String MIT_AWARD_NUMBER_FIELD = "mitAwardNumber";
    private static final String SELECT_MIT_AWARD= "award_exceptionCode.1003";
    
    private static final String CONNECTION_STRING = 
        CoeusGuiConstants.CONNECTION_URL + "/AwardMaintenanceServlet";
    
    private static final String COPY_AS_NEW = "N";
    private static final String COPY_AS_CHILD = "C";
    private static final char COPY_AWARD = 'F';
    
    private static final String EMPTY_STRING = "";
    private static final int WIDTH = 500;
    private static final int ORIGINAL_HEIGHT = 115;
    private static final int NEW_HEIGHT = 375;
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** Creates a new instance of AwardCopyController */
    public AwardCopyController() {
        awardCopyForm = new AwardCopyForm(CoeusGuiConstants.getMDIForm(), true);
        registerComponents();
    }
    
    /** Creates a new instance of AwardHierarchyController
     * @param mitAwardNumber
     */
    public AwardCopyController(String mitAwardNumber, CoeusVector cvAwardHierarchy) {
        this();
        this.cvAwardHierarchy = cvAwardHierarchy;
        setFormData(mitAwardNumber);
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        awardCopyForm.dlgAwardCopy.show();
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return awardCopyForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return awardCopyForm;
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
        java.awt.Component[] components = { awardCopyForm.chkCopyDescendents,
        awardCopyForm.rdBtnNew,
        awardCopyForm.rdBtnChild,
        awardCopyForm.btnOK,
        awardCopyForm.btnCancel
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardCopyForm.setFocusTraversalPolicy(traversePolicy);
        awardCopyForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        //Add listeners to all the buttons
        awardCopyForm.btnOK.addActionListener(this);
        awardCopyForm.btnCancel.addActionListener(this);
        awardCopyForm.btnFind.addActionListener(this);
        
        //Add listener for the New and Child radio buttons
        awardCopyForm.rdBtnNew.addActionListener(this);
        awardCopyForm.rdBtnChild.addActionListener(this);
        
        awardCopyForm.dlgAwardCopy.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
    }
    
    /** To set the default focus for the component
     */
    private void requestDefaultFocus(){
        awardCopyForm.btnCancel.requestFocusInWindow();
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
        
        awardCopyForm.dlgAwardCopy.setTitle(WINDOW_TITLE);
        awardCopyForm.lblCopyFromValue.setText(selectedMitAwardNumber);
        
        //Set the tree data
        awardCopyForm.pnlAwardHierarchyTree.construct(selectedMitAwardNumber, cvAwardHierarchy);
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
        if( source.equals(awardCopyForm.btnOK)) {
            try{
                performCopyAward();
            }catch (CoeusClientException coeusClientException){
                CoeusOptionPane.showInfoDialog(coeusClientException.getMessage());
                awardCopyForm.dlgAwardCopy.dispose();
                awardCopyForm.dlgAwardCopy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                awardCopyForm.dlgAwardCopy.getGlassPane().setVisible(false);
            }
        }else if(source.equals(awardCopyForm.btnCancel)) {
            //Release all locks before closing
            
            awardCopyForm.dlgAwardCopy.dispose();
        }else if(source.equals(awardCopyForm.btnFind)) {
            showAwardSearch();
        }else if(source.equals(awardCopyForm.rdBtnNew)) {
            
            //Hide the award hierarchy if it is visible
            if( awardCopyForm.pnlAwardHierarchy.isVisible() ){

                /** Code for focus traversal - start */
                java.awt.Component[] components = { awardCopyForm.chkCopyDescendents,
                awardCopyForm.rdBtnNew,
                awardCopyForm.rdBtnChild,
                awardCopyForm.btnOK,
                awardCopyForm.btnCancel
                };
                ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
                awardCopyForm.setFocusTraversalPolicy(traversePolicy);
                awardCopyForm.setFocusCycleRoot(true);

                /** Code for focus traversal - end */
                
                awardCopyForm.pnlAwardHierarchy.setVisible(false);
                awardCopyForm.dlgAwardCopy.setSize(WIDTH, ORIGINAL_HEIGHT);
            }
            
        }else if(source.equals(awardCopyForm.rdBtnChild)) {
            
            //Show the award hierarchy if it is not visible
            if( !awardCopyForm.pnlAwardHierarchy.isVisible() ){
                
                /** Code for focus traversal - start */
                java.awt.Component[] components = { awardCopyForm.chkCopyDescendents,
                awardCopyForm.rdBtnNew,
                awardCopyForm.rdBtnChild,
                awardCopyForm.pnlAwardHierarchyTree.treeAwardHierarchy,
                awardCopyForm.btnOK,
                awardCopyForm.btnCancel,
                awardCopyForm.btnFind
                };
                ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
                awardCopyForm.setFocusTraversalPolicy(traversePolicy);
                awardCopyForm.setFocusCycleRoot(true);

                /** Code for focus traversal - end */
                
                awardCopyForm.pnlAwardHierarchyTree.setSelectedObject(selectedMitAwardNumber);
                awardCopyForm.pnlAwardHierarchy.setVisible(true);
                awardCopyForm.dlgAwardCopy.setSize(WIDTH, NEW_HEIGHT);
            }
            
        }
    }
    
    /** Copy the awards depending on the options selected
     * @throws CoeusClientException
     */
    private void performCopyAward() throws CoeusClientException {
        if( awardCopyForm.rdBtnChild.isSelected() ){
            AwardHierarchyBean selectedBean = awardCopyForm.pnlAwardHierarchyTree.getSelectedObject();
            if( selectedBean == null ) {
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_MIT_AWARD));
                return ;
            }
        }
        
        //Create a new AwardCopyBean to send data to the server
        awardCopyBean= new AwardCopyBean();

        awardCopyBean.setSourceAwardNumber(selectedMitAwardNumber);
        if( awardCopyForm.rdBtnNew.isSelected() ){
            awardCopyBean.setCopyAsNewOrChild(COPY_AS_NEW);
        }else{
            awardCopyBean.setCopyAsNewOrChild(COPY_AS_CHILD);
            awardCopyBean.setTargetAwardNumber(
                awardCopyForm.pnlAwardHierarchyTree.getSelectedObject().getMitAwardNumber());
        }
        awardCopyBean.setCopyDescendents(awardCopyForm.chkCopyDescendents.isSelected());
        awardCopyBean.setDescendents(cvDescendents);
        
        awardCopyForm.dlgAwardCopy.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardCopyForm.dlgAwardCopy.getGlassPane().setVisible(true);
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(COPY_AWARD);
        requester.setDataObject(awardCopyBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            Vector vecData = response.getDataObjects();
            awardCopyBean.setDescendents((CoeusVector)vecData.get(0));
            awardCopyBean.setTargetAwardNumber(vecData.get(1).toString());
        }else {
            throw new CoeusClientException(response.getMessage());
        }
        setAwardCopied(true);
        awardCopyForm.dlgAwardCopy.dispose();
        awardCopyForm.dlgAwardCopy.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        awardCopyForm.dlgAwardCopy.getGlassPane().setVisible(false);
    }
    
    /** To display the award search screen */
    private void showAwardSearch(){
        String searchedAwardNumber;
        try {
            CoeusSearch coeusSearch = new CoeusSearch(
            CoeusGuiConstants.getMDIForm(), "AWARDSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap awardSelected = coeusSearch.getSelectedRow();
            if (awardSelected != null && !awardSelected.isEmpty() ) {
                searchedAwardNumber = Utils.convertNull(awardSelected.get(
                "MIT_AWARD_NUMBER"));
                if( !searchedAwardNumber.equals(EMPTY_STRING) ){
                    /** If the searched mit award number exists in the tree hierarchy,
                     * set the selection to the corresponding mit award number in the tree
                     * else construct the tree for the selected mit award number
                     */
                    Equals eqMitAwdNumber = new Equals(MIT_AWARD_NUMBER_FIELD, searchedAwardNumber);
                    CoeusVector cvData = cvAwardHierarchy.filter(eqMitAwdNumber);
                    if( cvData != null && cvData.size() > 0 ){
                        
                        //Tree hierarchy data exists
                        if( !(searchedAwardNumber.startsWith(
                        awardCopyForm.pnlAwardHierarchyTree.getSelectedMitAwardNumber().substring(0, 6) )) ){
                            
                            //Tree hierarchy for the selected mit award number is not displayed
                            //Construct the tree passing the data vector
                            awardCopyForm.pnlAwardHierarchyTree.construct(searchedAwardNumber, cvAwardHierarchy);
                        }else{
                            
                            //Tree hierarchy for the selected mit award number is displayed
                            if( searchedAwardNumber.equals(awardCopyForm.pnlAwardHierarchyTree.getSelectedObject().getMitAwardNumber()) ){
                                //Do nothing since searched award is same as the selected award in the tree
                                return ;
                            }

                            //Set the selection
                            awardCopyForm.pnlAwardHierarchyTree.setSelectedObject(searchedAwardNumber);
                        }
                    }else{

                        if( searchedAwardNumber.startsWith(
                        awardCopyForm.pnlAwardHierarchyTree.getSelectedMitAwardNumber().substring(0, 6)) ){
                            //Tree hierarchy data exists
                            //Set the selection
                            awardCopyForm.pnlAwardHierarchyTree.setSelectedObject(searchedAwardNumber);
                        }else{
                            //Tree hierarchy data does not exist
                            //Construct the tree by passing the mit award number to retrieve data
                            awardCopyForm.pnlAwardHierarchyTree.construct(searchedAwardNumber);
                        }
                    }
                }
            }
        } catch (Exception e) {
            CoeusOptionPane.showErrorDialog("Award Search is not available.." + e.getMessage());
        }
    }
    
    /** Getter for property awardCopied.
     * @return Value of property awardCopied.
     *
     */
    public boolean isAwardCopied() {
        return awardCopied;
    }
    
    /** Setter for property awardCopied.
     * @param awardCopied New value of property awardCopied.
     *
     */
    private void setAwardCopied(boolean awardCopied) {
        this.awardCopied = awardCopied;
    }
    
    /** Getter for property cvDescendents.
     * @return Value of property cvDescendents.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getDescendents() {
        return cvDescendents;
    }
    
    /** Setter for property cvDescendents.
     * @param cvDescendents New value of property cvDescendents.
     *
     */
    public void setDescendents(edu.mit.coeus.utils.CoeusVector cvDescendents) {
        this.cvDescendents = cvDescendents;
        //Remove the first element which is the source award
        cvDescendents.removeElementAt(0);
    }
    
    /** Getter for property awardCopyBean.
     * @return Value of property awardCopyBean.
     *
     */
    public edu.mit.coeus.award.bean.AwardCopyBean getAwardCopyBean() {
        return awardCopyBean;
    }
    
    /** Method to clean all objects */
    public void cleanUp() {
        awardCopyForm = null;
        selectedMitAwardNumber = null;
        awardCopyBean = null;
        cvAwardHierarchy = null;
        cvDescendents = null;
        coeusMessageResources = null;
    }
}
