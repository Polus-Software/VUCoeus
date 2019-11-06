/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * InstituteProposalController.java
 *
 * Created on April 23, 2004, 2:15 PM
 */

package edu.mit.coeus.instprop.controller;


import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.instprop.bean.*;

import java.beans.*;
//import javax.swing.JTabbedPane;
import java.util.Hashtable;

/** 
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
* @author chandru
*/
public abstract class InstituteProposalController extends Controller{
        
    public String queryKey;
    public final static String EMPTY_STRING = "";
    public InstituteProposalBaseBean instituteProposalBaseBean;
    public QueryEngine queryEngine;
    
     //Award Modes.
    public static final char NEW_INST_PROPOSAL = 'N';
    public static final char CORRECT_INST_PROPOSAL = 'M';
    public static final char NEW_ENTRY_INST_PROPOSAL = 'E';
    public static final char DISPLAY_PROPOSAL = 'D';
    
//    protected  static final int PROPOSAL_DETAIL_TAB_INDEX = 0;
//    protected static final int MAINLINGINFO_TAB_INDEX = 1;
//    protected static final int INVESTIGATOR_TAB_INDEX = 2;
//    protected static final int COST_SHARING_TAB_INDEX = 3;
//    protected static final int IDC_RATES_TAB_INDEX = 4;
//    protected static final int SPECIAL_REVIEW_INDEX = 5;
//    protected static final int SCIENCE_CODE_TAB_INDEX = 6;
//    protected static final int IP_REVIEW_TAB_INDEX = 7;
    
    //Added for Case#3587 - Multi Campus enchanment - Start
    private static final char USER_HAS_RIGHT_IN_UNIT_LEVEL = 'R';
    private static final String LEAD_UNIT_NUMBER = "LEAD_UNIT_NUMBER";
    private static final String RIGHT_ID = "RIGHT_ID";
    protected static final String MODIFY_INST_PROPOSAL = "MODIFY_INST_PROPOSAL";
    protected static final String CREATE_INST_PROPOSAL ="CREATE_INST_PROPOSAL";
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    //Case#3587 - End
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
   
    
    /** Creates a new instance of InstituteProposalController */
    public InstituteProposalController() {
        
    }
    
    public InstituteProposalController(InstituteProposalBaseBean instituteProposalBaseBean){
        this.instituteProposalBaseBean= instituteProposalBaseBean;
        prepareQueryKey(instituteProposalBaseBean);
        queryEngine = QueryEngine.getInstance();
    }
    
    public final void prepareQueryKey(InstituteProposalBaseBean instituteProposalBaseBean) {
        queryKey = instituteProposalBaseBean.getProposalNumber() + instituteProposalBaseBean.getSequenceNumber();
    }
    
    /** Getter for property queryKey.
     * @return Value of property queryKey.
     *
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }
    
    /** Setter for property queryKey.
     * @param queryKey New value of property queryKey.
     *
     */
    public void setQueryKey(java.lang.String queryKey) {
        this.queryKey = queryKey;
    }
    
    /** This method is used to check whether the given proposal number is already
      * opened in the given mode or not.
      * @param refId refId - for institute proposal its proposal Number.
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if proposal window is already open else returns false.
      */
     boolean isProposalWindowOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            // try to get the requested frame which is already opened 
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW,refId);
            
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            }
            return true;
        }
        return false;
     }
    
     
     /** This method is used to check whether the given proposal number is already
      * opened in the given mode or not and displays message if the proposal is open
      * @param refId refId - for inst proposal its institute proposal Number.
      * @param mode mode of Form open.
      * @return true if proposal window is already open else returns false.
      */
     boolean isProposalWindowOpen(String refId, char mode){
         return isProposalWindowOpen(refId, mode, true);
     }
     
    //Added for Case#3587 - Multi Campus enchament - Start
    /*
     * Method to check logged in user has 'MODIFY_INST_PROPOSAL' right in proposa lead unit
     * @param leadUnitNumber
     * @return canModify
     */
    protected boolean checkUserHasRightInLeadUnit(String leadUnitNumber, String rightId){
        boolean hasRight = false;
        RequesterBean request = new RequesterBean();
        Hashtable htRightCheck = new Hashtable();
        htRightCheck.put(LEAD_UNIT_NUMBER,leadUnitNumber);
        htRightCheck.put(RIGHT_ID,rightId);
        request.setFunctionType(USER_HAS_RIGHT_IN_UNIT_LEVEL);
        request.setDataObject(htRightCheck);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            hasRight = ((Boolean)response.getDataObject()).booleanValue();
        }
        return hasRight;
    }
    //Case#3587 - End
}
