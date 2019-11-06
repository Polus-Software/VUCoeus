/*
 * SponsorHierarchyController.java
 *
 * Created on November 22, 2004, 10:44 AM
 */

package edu.mit.coeus.sponsorhierarchy.controller;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.gui.CoeusInternalFrame;

/**Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */

    

public abstract class SponsorHierarchyController extends Controller{
    
    public String queryKey;
    public final static String EMPTY_STRING = "";
    public QueryEngine queryEngine;
    public String hierarchyName;
    
    public static final char VIEW_HIERARCHY = 'D';
    public static final char EDIT_HIERARCHY = 'M';
    public static final char NEW_HIERARCHY = 'N';
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /** Creates a new instance of SponsorHierarchyController */
    public SponsorHierarchyController() {
    }
    
    public SponsorHierarchyController(String hierarchyName) {
        if(hierarchyName != null) {
            this.hierarchyName = hierarchyName;
            
        }
    }
    
     public final void prepareQueryKey(String hierarchyName) {
        queryKey = hierarchyName;
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
    
    /** This method is used to check whether the given Hierarchy is already
      * opened in the given mode or not and displays message if the Subcontract is open
      * @param refId refId - for Hierarchy its Hierarchy Name.
      * @param mode mode of Form open.
      * @return true if Subcontract window is already open else returns false.
      */
     boolean isHierarchyOpen(String refId, char mode){
         return isHierarchyOpen(refId, mode, false);
     }
     
     /** This method is used to check whether the given Hierarchy name is already
      * opened in the given mode or not.
      * @param refId refId - for  Subcontract its Hierarchy Name.
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if Hierarchy window is already open else returns false.
      */
    boolean isHierarchyOpen(String refId, char mode,boolean isMessage){
        boolean duplicate = false;
        try {
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.SPONSORHIERARCHY_BASE_WINDOW, EMPTY_STRING, mode);
        }catch (Exception exception) {
            duplicate = true;
        }
        CoeusInternalFrame hierarchyFrame = mdiForm.getFrame(CoeusGuiConstants.SPONSORHIERARCHY_BASE_WINDOW);
        if(hierarchyFrame != null) {
            try {
                hierarchyFrame.setSelected(true);
                hierarchyFrame.setVisible(true);
            }catch(java.beans.PropertyVetoException propertyVetoException) {
                
            }
        }
        return duplicate;
    }
     
    public abstract void cleanUp();
     
}
