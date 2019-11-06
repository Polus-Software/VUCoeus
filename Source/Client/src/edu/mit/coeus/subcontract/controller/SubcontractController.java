/*
 * SubcontractController.java
 *
 * Created on August 31, 2004, 8:23 PM
 */

package edu.mit.coeus.subcontract.controller;


import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.subcontract.bean.*;

import java.beans.*;
import javax.swing.JTabbedPane;

/**
 *
 * @author  nadhgj
 */
public abstract class SubcontractController extends Controller {
    
    public String queryKey;
    public final static String EMPTY_STRING = "";
    public SubContractBean subContractBean;
    public QueryEngine queryEngine;
     //subcontract Modes.
    public static final char NEW_SUBCONTRACT = 'N';
    public static final char CORRECT_SUBCONTRACT = 'M';
    public static final char NEW_ENTRY_SUBCONTRACT = 'E';
    public static final char DISPLAY_SUBCONTRACT = 'D';
    
    protected static final int SHOW_PREV_SUBCONTRACT = 1;
    protected static final int SHOW_NEXT_SUBCONTRACT = 2;
    
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /** Creates a new instance of SubcontractController */
    public SubcontractController() {
    }
    
    public SubcontractController(SubContractBean subContractBean) {
       if(subContractBean != null && subContractBean.getSubContractCode() != null) {
           this.subContractBean= subContractBean;
           prepareQueryKey(subContractBean);
       }
       
        queryEngine = QueryEngine.getInstance(); 
    }
    
    public final void prepareQueryKey(SubContractBean subContractBean) {
        queryKey = subContractBean.getSubContractCode() + subContractBean.getSequenceNumber();
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
    
    /** This method is used to check whether the given Subcontract number is already
      * opened in the given mode or not.
      * @param refId refId - for  Subcontract its Subcontract Number.
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if Subcontract window is already open else returns false.
      */
     boolean isSubcontractOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, refId, mode );
        }catch(Exception exception){
            duplicate = true;
            if( displayMessage ){
                if(exception.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(exception.getMessage());
                }
            }
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW,refId);
            
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
     
     /** This method is used to check whether the given Subcontract number is already
      * opened in the given mode or not and displays message if the Subcontract is open
      * @param refId refId - for Subcontract its Subcontract Number.
      * @param mode mode of Form open.
      * @return true if Subcontract window is already open else returns false.
      */
     boolean isSubcontractOpen(String refId, char mode){
         return isSubcontractOpen(refId, mode, true);
     }
     
     public abstract void cleanUp();
     
}


