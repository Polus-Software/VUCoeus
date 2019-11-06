/*
 * SelectRoutingMapController.java
 *
 * Created on October 31, 2007, 5:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.routing.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.propdev.bean.ProposalApprovalMapBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.gui.ProposalDetailAdminForm;
import edu.mit.coeus.utils.CoeusVector;
import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author leenababu
 */
public class SelectRoutingMapController extends Controller {
    
    private String moduleName ;
    private String moduleCode ;
    private String moduleItemKeySequence;
    
    private String moduleItemKey;
     /** CoeusDlgWindow instance */
    public CoeusDlgWindow dlgApprovalMapsForm;
    /** Title of this Modal Window */
    private String title = "Approval Maps";
    /** static variable for functiontype while server side call */
    private static final char GET_APPROVAL_MAPS = 'C';
    /** static variable for RED color */
    private static final Color RED_COLOR = Color.RED;
    /** static variable for BLUE color */
    private static final Color BLUE_COLOR  = Color.BLUE;
    /** flag to check is any Map Selected */
    private boolean isAnyMapSelected;
    /** holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources;
    
//    /** ProposalDevelopmentFormBean instance to get Sponsor details
//     * and for saving the Approval Maps
//     */
//    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
//    /** ProposalDetailAdminForm for setting save Required flag */
//    private ProposalDetailAdminForm proposalDetailAdminForm;
    
   
    /** unitNumber instance to fetch UnitMaps details */
    private String unitNumber;
    /** current functionType while opening this Form :New/Add/Modify etc */
    private char functionType;
    
    /** Vector holding the parameters passed to Form from ProposalDetailForm */
    private Vector parameters;
    /** Vector holding all unitmaps applicable for this Proposal */
    private Vector allUnitMaps;
    
    /** Vector holding ProposalApprovalMapBean for Panel Rows */
    private Vector vectorOfbean =  new Vector();
    
    /** Vector holding all ProposalApprovalMapBean in the ProposalDevelopmentFormBean instance */
    private CoeusVector vectorProposalApprovalMapBean =  new CoeusVector();
    /** Flag isNewApprovalMapAdded for any New Approval Map selected */
    boolean isNewApprovalMapAdded;
    
    /** Holds Current Selected ProposalApprovalMapBean instance */
    //private ProposalApprovalMapBean proposalApprovalMapBean;
    
    private ProposalApprovalMapBean oldApprovalMapBean;
    /** isRoleRightEnabled Flag indicating RoleRights for RIGHT_ID */
    private boolean isRoleRightEnabled;
    /** isSaveRequired Flag for monitoring any Changes to Form Details */
    private boolean isSaveRequired;
    
    // direct map id always 999
    /** Direct Map ID value */
    private int mapsID = 999;
    
    /** String Constant RIGHT_ID used to checked aganist RoleRights/ProposalRoleRights */
    private final static String RIGHT_ID = "MAINTAIN_PROPOSAL_ACCESS";
    
    /** roleID for the Current login */
    private int roleID;
    
    /** Servlet URL */
    private final String conURL = "/ProposalActionServlet";
    /** String constant APPROVE BY LABEL */
    private static final String APPROVEBY_LABEL ="Approve By";
    /** tring constant OR LABEL */
    private static final String OR_LABEL ="Or       ";
    
    /** Color for Setting Panel background for Disabled case */
    private Color greyBackGround = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
    
    //======== START MESSAGES
    /** MESSAGES flags ADD_A_MAP */
    private static final String ADD_A_MAP = "selectApprovalmap_exceptionCode.1130"; //"Do you want to add a map ?";
    /** MESSAGES flags REPLACE_A_MAP */
    private static final String REPLACE_A_MAP = "selectApprovalmap_exceptionCode.1131"; // "Do you want to replace the current map ?";
    /** MESSAGES flags SAVE_CHANGES */
    private static final String SAVE_CHANGES = "budget_baseWindow_exceptionCode.1402"; //"Do you want to save changes ?";
    
    //======== END MESSAGES
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    //Map that holds the role as key and value another hashMap with qualifierCode
    //as key qualifier description as value
    private Map roleQulaifiersMap;
    private static final char GET_QUALIFIER_LIST = 'Q';
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end 
    /** Creates a new instance of SelectRoutingMapController */
    public SelectRoutingMapController() {
    }

    public void display() {
    }

    public void formatFields() {
    }

    public Component getControlledUI() {
        return null;
    }

    public Object getFormData() {
        return null;
    }

    public void registerComponents() {
    }

    public void saveFormData() throws CoeusException {
    }

    public void setFormData(Object data) throws CoeusException {
    }

    public boolean validate() throws CoeusUIException {
        return false;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

}
