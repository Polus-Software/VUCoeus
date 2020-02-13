/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.exception.CoeusClientException;

/**
 * AwardSummaryController.java
 * Created on March 26, 2004, 10:05 AM
 * @author  Vyjayanthi
 */
public class AwardSummaryController extends AwardController
implements ActionListener {
    
    /** Holds an instance of <CODE>AwardSummaryForm</CODE> */
    private AwardSummaryForm awardSummaryForm;
    
    /** Holds an instance of <CODE>awardBean</CODE> */
    private AwardBean awardBean;
    
    /** Holds an instance of the Award List table */
    private JTable tblAwardList;
    
    /** Holds the selected row's mit award number */
    private String mitAwardNumber;
    
    /** Holds the selected row in the Award List table */
    private int selectedRow = -1;
    
    /** Holds the total number of rows in the Award List table */
    private int totalAwards = 0;
    
    /** Holds true if row selection is required as in Award List, false otherwise */
    private boolean showRowSelection;
    
    /** Holds the hierarchy data */
    private CoeusVector cvAwardHierarchy;
    
    private static final String NEXT = "N";
    private static final String PREVIOUS = "P";
    private static final char GET_AWARD_SUMMARY = 'H';
    private static final String SOURCE_LIST = "SOURCE_LIST";
    
    
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
        "/AwardMaintenanceServlet";
    

    //Bug Fix: 1267 Start 1
    private int awdNoCol;
    //Bug Fix: 1267 End 1
    //Added for case #2054 by tarique start 1
    private CoeusVector cvTraversalBean;
    private String actionValue = "";
    //Added for case #2054 by tarique end 1
    /** Creates a new instance of AwardSummaryController */
    public AwardSummaryController() {
        awardSummaryForm = new AwardSummaryForm(CoeusGuiConstants.getMDIForm(), true);
        registerComponents();
    }
    
    /** Creates a new instance of AwardSummaryController
     * @param tblAwardList
     */
    public AwardSummaryController(JTable tblAwardList) {
        this();
        showRowSelection = true;
        // bug Fix #1706 - start
        awardSummaryForm.pnlAwardDetails.setSource("SOURCE_LIST");
        //bug Fix #1706 - End
        setFormData(tblAwardList);
        formatFields();
    }
    
    /** Creates a new instance of AwardSummaryController
     * @param cvAwardHierarchy
     * @param selectedIndex
     */
    public AwardSummaryController(CoeusVector cvAwardHierarchy, int selectedIndex) {
        this();
        this.cvAwardHierarchy = cvAwardHierarchy;
        setData(selectedIndex);
        formatFields();
    }
    //Added for case #2054 by tarique start 2
    /** Creates a new instance of AwardSummaryController
     * @param cvAwardHierarchy
     * @param selectedIndex
     * @param mitAwardNumber
     */
    public AwardSummaryController(CoeusVector cvAwardHierarchy, int selectedIndex
            ,String mitAwardNumber, CoeusVector cvTraversalBean) {
        this();
        this.cvAwardHierarchy = cvAwardHierarchy;
        this.mitAwardNumber = mitAwardNumber;
        this.cvTraversalBean = cvTraversalBean;
        setData(selectedIndex);
        formatFields();
    }
    //Added for case #2054 by tarique end 2
    /** To set the form data 
     * @param selectedIndex
     */
    private void setData(int selectedIndex){

        showRowSelection = false;
        
        selectedRow = selectedIndex;
        //Added code for case #2054  by tarique start 3
        totalAwards = cvTraversalBean.size();
        //Added code for case #2054  by tarique end 3
        //Commented the code for case #2054  by tarique start 4
       // totalAwards = cvAwardHierarchy.size();
        //Commented the code for case #2054  by tarique end 4
        
        
        

        //To set the data initially
        showAwardDetails(EMPTY);
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        awardSummaryForm.dlgAwardSummary.show();
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {

        //Disable the Next button if the selected award is the last in the Award List
        if( totalAwards - 1 > selectedRow ){
            awardSummaryForm.btnNext.setEnabled(true);
        }else{
            awardSummaryForm.btnNext.setEnabled(false);
        }
        
        //Disable the Prev button if the selected award is the first in the Award List
        if( selectedRow > 0 ){
            awardSummaryForm.btnPrev.setEnabled(true);
        }else{
            awardSummaryForm.btnPrev.setEnabled(false);
        }
    }
    
    /** An overridden method of the controller
     * @return awardSummaryForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return awardSummaryForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        if( showRowSelection ){
            
            //Bug Fix:1267 Start 3 
            //Changed the column index from hard coded value to awdNoCol
            mitAwardNumber = (String)tblAwardList.getValueAt(selectedRow, awdNoCol);
            //Bug Fix:1267 End 3
            
        }else {
            //Commented the code for case #2054  by tarique start 5
            //mitAwardNumber = ((AwardBaseBean)cvAwardHierarchy.get(selectedRow)).getMitAwardNumber();
            //Commented the code for case #2054  by tarique end 5
            //Added for case #2054 by tarique start 6
            if(actionValue.equals(NEXT)){
                AwardBaseBean awardBaseBean = (AwardBaseBean)cvTraversalBean.get(selectedRow);
                mitAwardNumber = awardBaseBean.getMitAwardNumber();
                awardBaseBean = null;
            }else if(actionValue.equals(PREVIOUS)){
                AwardBaseBean awardBaseBean = (AwardBaseBean)cvTraversalBean.get(selectedRow);
                mitAwardNumber = awardBaseBean.getMitAwardNumber();
                awardBaseBean = null;
            }
            //Added for case #2054 by tarique end 6
        }        
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_AWARD_SUMMARY);
        requester.setDataObject(mitAwardNumber);
        awardSummaryForm.dlgAwardSummary.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        awardSummaryForm.dlgAwardSummary.getGlassPane().setVisible(true);
        try{
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                awardBean = (AwardBean)response.getDataObject();
            }else{
                throw new CoeusClientException(response.getMessage());
            }
        }catch (CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }finally {
            awardSummaryForm.dlgAwardSummary.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            awardSummaryForm.dlgAwardSummary.getGlassPane().setVisible(false);
        }
        return awardBean;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { awardSummaryForm.btnClose,
        awardSummaryForm.btnPrev,
        awardSummaryForm.btnNext,
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardSummaryForm.setFocusTraversalPolicy(traversePolicy);
        awardSummaryForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        awardSummaryForm.btnClose.addActionListener(this);
        awardSummaryForm.btnNext.addActionListener(this);
        awardSummaryForm.btnPrev.addActionListener(this);
        
        awardSummaryForm.dlgAwardSummary.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
    }
    
    /** To set the default focus for the component
     */
    private void requestDefaultFocus(){
        awardSummaryForm.btnClose.requestFocusInWindow();
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
        
        if( data == null ) {
            return ;
        }
        tblAwardList = (JTable)data;
        
        //Bug Fix:1267 Start 2
        //JIRA COEUSQA-2460 - START
        //awdNoCol = tblAwardList.getColumnModel().getColumnIndex("Award Number");
        awdNoCol = 0;
        //JIRA COEUSQA-2460 - END
        //Bug Fix:1267 End 2
        
        selectedRow = tblAwardList.getSelectedRow();
        totalAwards = tblAwardList.getRowCount();

        //To set the data initially
        showAwardDetails(EMPTY);
        
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
        //Added code for case #2054 to show hour glass by tarique start 7
        try{
            awardSummaryForm.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
            //Added code for case #2054 to show hour glass by tarique end 7
            if( source.equals(awardSummaryForm.btnClose) ){
                awardSummaryForm.dlgAwardSummary.dispose();
            }else if( source.equals(awardSummaryForm.btnNext) ){
                //Added code for case #2054 to show hour glass by tarique start 8
                actionValue = NEXT;
                //Added code for case #2054 to show hour glass by tarique end 8
                showAwardDetails(NEXT);
            }else if( source.equals(awardSummaryForm.btnPrev) ){
                //Added code for case #2054 to show hour glass by tarique start 9
                actionValue = PREVIOUS;
                //Added code for case #2054 to show hour glass by tarique end 9
                showAwardDetails(PREVIOUS);
            }
        ///Added code for case #2054 to show hour glass by tarique start 10
        }catch(Exception ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }finally{
            awardSummaryForm.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
        }
        //Added code for case #2054 to show hour glass by tarique end 10
    }
    
    /** Displays the award details for the next award in the Award List screen
     * @param navigation holds "N" or "P", indicating Next or Previous, respectively
     */
    private void showAwardDetails(String navigation){
        if( navigation.equals(NEXT) ){
            selectedRow++;
        }else if( navigation.equals(PREVIOUS) ){
            selectedRow--;
        }
        
        getFormData();
        
        
        //Set the award details
        // Bug Fix #1706 - start
        awardSummaryForm.pnlAwardDetails.setIndex(selectedRow);
        // Bug Fix #1706 - End 
        awardSummaryForm.pnlAwardDetails.showValues(awardBean);
        //Set the Investigator details
        ProposalAwardHierarchyLinkBean propAwardHierarchyLinkBean = 
            new ProposalAwardHierarchyLinkBean();
        propAwardHierarchyLinkBean.setBaseType(CoeusConstants.AWARD);
        awardSummaryForm.pnlInvestigatorUnit.setDataValues(
            awardBean.getAwardInvestigators(), propAwardHierarchyLinkBean);
        awardSummaryForm.pnlInvestigatorUnit.setFormData();
        
        formatFields();
        
        if( showRowSelection ){
            tblAwardList.setRowSelectionInterval(selectedRow, selectedRow);
        }        
    }
    
    /** Method to clean all objects */
    public void cleanUp(){
        awardSummaryForm = null;
        awardBean = null;
        tblAwardList = null;
        mitAwardNumber = null;
        cvAwardHierarchy = null;
        //Added for case #2054 by tarique start 11
        actionValue =  null;
        cvTraversalBean = null;
        //Added for case #2054 by tarique end 11
        
    }

}
