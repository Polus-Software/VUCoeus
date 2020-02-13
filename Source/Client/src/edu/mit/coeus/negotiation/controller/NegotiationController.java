/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.negotiation.controller;

import java.beans.PropertyVetoException;

import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.negotiation.bean.NegotiationBaseBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.query.QueryEngine;

/**
 * NegotiationController.java
 * Created on July 12, 2004, 3:39 PM
 * @author  Vyjayanthi
 */
public abstract class NegotiationController extends Controller {
    
    private QueryEngine queryEngine;
    protected String queryKey;
    public NegotiationBaseBean negotiationBaseBean;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    public final static String EMPTY = "";
    
    //To identify the Negotiation related data in the Query Engine
    protected static final String NEGOTIATION = "Negotiation";
    
    protected static final int SHOW_PREV_NEGOTIATION = 1;
    protected static final int SHOW_NEXT_NEGOTIATION = 2;
    
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    
    /** Creates a new instance of NegotiationController. */
    public NegotiationController() {        
    }
    
    /** Creates a new instance of NegotiationController.
     * creates the Key for the query engine from the negotiation base bean.
     * @param negotiationBaseBean negotiation Base Bean.
     */    
    public NegotiationController(NegotiationBaseBean negotiationBaseBean) {
        if(negotiationBaseBean != null && negotiationBaseBean.getNegotiationNumber() != null) {
            this.negotiationBaseBean = negotiationBaseBean;
            queryKey = NEGOTIATION + negotiationBaseBean.getNegotiationNumber();
        }
        queryEngine = QueryEngine.getInstance();
    }
    
    /** Getter for property queryKey.
     * @return Value of property queryKey.
     *
     */
    public String getQueryKey() {
        return queryKey;
    }

    public void prepareQueryKey(NegotiationBaseBean negotiationBaseBean) {
        queryKey = NEGOTIATION + negotiationBaseBean.getNegotiationNumber();
    }

    
    /** This method is used to check whether the given Negotiation number is already
     * open in the given mode or not.
     * @param refId refId - for negotiation its Negotiation Number.
     * @param mode mode of Form open.
     * @param displayMessage if true displays error messages else doesn't.
     * @return true if Negotiation window is already open else returns false.
     */
    boolean isNegotiationOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.NEGOTIATION_DETAILS, refId, mode );
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
            CoeusGuiConstants.NEGOTIATION_DETAILS,refId);
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

    /** This method is used to check whether the given Negotiation number is already
     * open in the given mode or not and displays message if the Negotiation is open
     * @param refId refId - for Negotiation its Negotiation Number.
     * @param mode mode of Form open.
     * @return true if Negotiation window is already open else returns false.
     */
    boolean isNegotiationOpen(String refId, char mode){
        return isNegotiationOpen(refId, mode, true);
    }
    
    public void cleanUp(){}
    
}
