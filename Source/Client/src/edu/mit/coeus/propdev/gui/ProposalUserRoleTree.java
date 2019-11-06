/*
 * ProposalUserRoleTree.java
 *
 * Created on February 20, 2004, 3:08 PM
 */

package edu.mit.coeus.propdev.gui;

/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
* @author chandru
*/

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.tree.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import edu.mit.coeus.utils.CoeusVector;
import java.util.Vector;
import edu.mit.coeus.utils.query.Equals;
import java.io.IOException;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.propdev.bean.ProposalNarrativeModuleUsersFormBean;
import edu.mit.coeus.utils.tree.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;

public class ProposalUserRoleTree extends UserRoleTree {
    //private CoeusVector narrativeUsers;
    
    private CoeusVector narrativeModules;
    //Self Reference to the same tree
    private ProposalUserRoleTree selfRef;
    private String proposalNumber;
    private CoeusVector deletedNarrativeUsers;
    private static final String NO_MODULE_USERS = "There are no other users with modify right for the module";
    //COEUSQA:2859 - Users marked as inactive should not appear in selection lists for DP and protocol roles - Start
    private CoeusMessageResources coeusMessageResources;
    private static final char USER_INACTIVE_STATUS = 'I';
    //COEUSQA:2859 - End
    
    
    /** Creates a new instance of ProposalUserRoleTree */
    public ProposalUserRoleTree(CoeusVector narrativeUsers, CoeusVector narrativeModules, String proposalNumber) {
        super();
        this.narrativeUsers = narrativeUsers;
        this.narrativeModules = narrativeModules;
        this.proposalNumber = proposalNumber;
        selfRef = this;
        dropTarget = new DropTarget(this,new MyDropTargetListener());
        dragSourceListener = new MyDragSourceListener();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    /** Getter for property narrativeUsers.
     * @return Value of property narrativeUsers.
     *
     */
    public CoeusVector getNarrativeUsers() {
        return narrativeUsers;
    }
    

    
    class MyDropTargetListener implements DropTargetListener {
        private boolean hasRight = false;
        
        public void dragEnter(DropTargetDragEvent dtde){
        }
        /**
         * Invoked while draging over the drop target
         */
        public void dragOver(DropTargetDragEvent dtde){
            if( isEnableDragDrop()  && !isDragingInside() ){
                
                /* drag and drop functionality is enabled and not drag source is not
                 the same component, so get the node for the cursor position and
                 make the node as selected if it is a role node.  */
                
                Point loc = dtde.getLocation();
                TreePath destinationPath = getPathForLocation(loc.x, loc.y);
                if ( destinationPath != null ) {
                    UserRoleNode newParent =
                    (UserRoleNode)destinationPath.getLastPathComponent();
                    UserRolesInfoBean nodeObject = ( UserRolesInfoBean ) newParent.getDataObject();
                    if ( nodeObject.isRole() ){
                        setSelectionPath( destinationPath );
                    }
                }
            }
        }
        
        public void dropActionChanged(DropTargetDragEvent dtde){
        }
        
        public void dragExit(DropTargetEvent dte){
        }
        /**
         * Invoked when the user drops any dragged item on this component.
         */
        public void drop(DropTargetDropEvent dtde){
            if( isDropable() && isEnableDragDrop()  && !isDragingInside()){
                try{
                    Point loc = dtde.getLocation();

                    TreePath destinationPath = getSelectionPath();

                     if ( isValidDrop( destinationPath ) ) {

                        /* valid drop location, so get the data from the
                        transferable object */
                        Transferable tr = dtde.getTransferable();
                        Object data = tr.getTransferData(
                        TransferableUserRoleData.USER_FLAVOR );
                        /* check whether multiple items have been dragged or only
                           one item.*/
                        if ( data instanceof Vector ){
                            /* mulitiple items dragged, so add all the items in the
                               vector */
                            Vector dataVector = ( Vector ) data ;
                            ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean = null;
                            UserRoleNode node = (UserRoleNode) destinationPath.getLastPathComponent();
                            UserRolesInfoBean  nodeBean = ( UserRolesInfoBean ) node.getUserObject();
                            boolean blnHasNarrativeRight = checkRoleHasRight(nodeBean.getRoleBean().getRoleId());
                            for ( int indx = 0; indx < dataVector.size(); indx++ ){
                                
                                //COEUSQA:2859 - Users marked as inactive should not appear in selection lists for DP and protocol roles - Start
                                UserInfoBean userBean = ( UserInfoBean ) dataVector.get(indx);
                                if(USER_INACTIVE_STATUS == userBean.getStatus()) {
                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                            "userRoleTree.exceptionCode.2702"));
                                }          
                                else {
                                //COEUSQA:2859 - End
                                addUserNode( dataVector.elementAt( indx ), destinationPath );
                                /** Added to have the added object inside the Vector **/
                                //Added for Narrative User Rights - start               
                                if(blnHasNarrativeRight){
                                    UserInfoBean userInfoBean = (UserInfoBean)dataVector.elementAt( indx );
                                    //Check if there is any narrative User Rights
                                    if(narrativeUsers!=null){
                                        CoeusVector existingUsers = narrativeUsers.filter( new Equals("userId", userInfoBean.getUserId().toUpperCase()));
                                        if(existingUsers==null || existingUsers.size()==0){
                                            //If there is narrative
                                            if(narrativeUsers!=null){
                                                //For each module add the User as Narraative User with NONE as right                
                                                Integer moduleNumber = null;
                                                for(int row = 0; row < narrativeModules.size() ; row++){
                                                    moduleNumber = (Integer)narrativeModules.elementAt(row);
                                                    
                                                    //Bug Fix 2084 Start
                                                    Equals eqProposalNo = new Equals("proposalNumber" , proposalNumber);
                                                    Equals eqModuleNo = new Equals("moduleNumber" , moduleNumber);
                                                    Equals eqUserId = new Equals("userId" , userInfoBean.getUserId());
                                                    
                                                    And eqProposalNoAndeqModuleNo = new And(eqProposalNo , eqModuleNo);
                                                    And filterAnd = new And(eqProposalNoAndeqModuleNo , eqUserId);
                                                    CoeusVector cvFilteredData = narrativeUsers.filter(filterAnd);
                                                    if(cvFilteredData != null && cvFilteredData.size()>0){
                                                        continue;
                                                    }
                                                    //Bug Fix 2084 End
                                                    
                                                     /* Commented for Case:2057 Start  */  
                                                  /*  proposalNarrativeModuleUsersFormBean = new ProposalNarrativeModuleUsersFormBean();
                                                    proposalNarrativeModuleUsersFormBean.setUserId(userInfoBean.getUserId());
                                                    proposalNarrativeModuleUsersFormBean.setAccessType('N');
                                                    proposalNarrativeModuleUsersFormBean.setModuleNumber(moduleNumber.intValue());
                                                    proposalNarrativeModuleUsersFormBean.setProposalNumber(proposalNumber);
                                                    proposalNarrativeModuleUsersFormBean.setAcType("I");
                                                    narrativeUsers.addElement(proposalNarrativeModuleUsersFormBean); */   
                                                    /* Commented for Case:2057 End  */   
                                                }
                                            }
                                        }   
                                    }
                                }
                            }
                            }
                        }else{
                                //COEUSQA:2589 - Users marked as inactive should not appear in selection lists for DP and protocol roles - Start
                                UserInfoBean userBean = ( UserInfoBean ) data;
                                if(USER_INACTIVE_STATUS == userBean.getStatus()) {
                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                            "userRoleTree.exceptionCode.2702"));
                                }          
                                else {
                                //COEUSQA:2859 - End
                            /* single item dragged, so add the dragged item */
                            addUserNode( data , destinationPath);
                            /** Added to have the added object inside the Vector **/
                            //vecAddedUsers.addElement(data);
                                }

                        }
                        ( ( DefaultTreeModel )getModel() ).reload();
                        if( isExpandRole() ) {
                            expandAll(selfRef,true);
                        }else{
                            expandAll(selfRef,destinationPath,true);
                        }
                        dtde.getDropTargetContext().dropComplete(true);
                        setSelectionPath( destinationPath );
                    }else{
                        dtde.getDropTargetContext().dropComplete(false);
                    }
                } catch ( UnsupportedFlavorException ex ){
                    dtde.getDropTargetContext().dropComplete(false);
                } catch ( IOException ioe ) {
                    dtde.getDropTargetContext().dropComplete(false);
                }
            }
        }    
    }    
    
    /** Getter for property narrativeModules.
     * @return Value of property narrativeModules.
     *
     */
    public CoeusVector getNarrativeModules() {
        return narrativeModules;
    }
    
    /** Setter for property narrativeModules.
     * @param narrativeModules New value of property narrativeModules.
     *
     */
    public void setNarrativeModules(CoeusVector narrativeModules) {
        this.narrativeModules = narrativeModules;
    }
    
    
        /**
     * DragSource Listener
     */
    
    class MyDragSourceListener implements DragSourceListener {
        /* Invoked after successful drop of the dragged items */
        public void dragDropEnd(
        DragSourceDropEvent dsde) {
            //dragingInside = false;
            setDragingInside(false);
            if (dsde.getDropSuccess() && isMoveNode() && isEnableDragDrop() ) {
                // Only remove the node if the drop was successful.
                if(getSelectionPath() != null ) {
                    DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)getSelectionPath().getLastPathComponent();
                    if(node.isLeaf()){
                        ((DefaultTreeModel)getModel()).removeNodeFromParent(node);
                        try{
                            UserRolesInfoBean tempUserRolesInfoBean = null;
                            
                            //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - Start
                            deletedBean.getUserBean().setAcType(TypeConstants.DELETE_RECORD);
                            //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - Start
                            
                            tempUserRolesInfoBean = (UserRolesInfoBean)ObjectCloner.deepCopy(deletedBean);
                            //Added to check if this is contained in Added vector Since 
                            //If this bean is Deleted - Added - Deleted. it should finally only do Deletion.
                            int dupIndex = contains(vecAddedUsers, tempUserRolesInfoBean);
                            if(dupIndex != -1){
                                vecAddedUsers.remove(dupIndex);
                            }
                            //Check if this is newly Added if Newly Added Then no need
                            //to keep in Deleted Vector 
                            if(tempUserRolesInfoBean.getUserBean().getUpdateTimestamp() != null) {
                                vecDelUsers.addElement( tempUserRolesInfoBean );
                            }
                            
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        // if parent node is selected and move property is true
                        // remove all the children from the particular node
                        node.removeAllChildren();
                        
                    }
                    // flag which specifies the tree data is modified
                    setModified(true);
                }
            }
        }
        
        public void dragEnter(DragSourceDragEvent dsde) {
            //Get all roles from Tree
            Vector roles = userAndRoles;
            CoeusVector roleRights = getRoleRights();
            ProposalNarrativeModuleUsersFormBean narrativeBean;
            CoeusVector updatedUsers = null;
            
            TreePath destinationPath = getSelectionPath();
            UserRoleNode node = (UserRoleNode) destinationPath.getParentPath().getLastPathComponent();
            UserRolesInfoBean  nodeBean = ( UserRolesInfoBean ) node.getUserObject();
            if(! hasInstituteRight(nodeBean)) {
                CoeusOptionPane.showErrorDialog(NO_RIGHT_TO_DELETE_INSTITUTE_ROLE);
            }
            
            UserRolesInfoBean userRolesInfoBean = null;
            UserInfoBean userInfoBean;
            //If Narrative role is being removed delete the corresponding user from Narrative User
            int currentRoleId = nodeBean.getRoleBean().getRoleId();
            UserRoleNode userNode = (UserRoleNode) destinationPath.getLastPathComponent();
            String currentUser = userNode.getDataObject().getUserBean().getUserId().toUpperCase();
            
            if(checkRoleHasRight(currentRoleId)){                
                //UserRolesInfoBean  userNodeBean = ( UserRolesInfoBean ) node.getUserObject();                
                if(roles!=null){
                    CoeusVector cvFileterdRoles ;
                    CoeusVector cvRights = new CoeusVector();
                    for(int row = 0; row < roles.size();row++){
                        userRolesInfoBean = (UserRolesInfoBean) roles.elementAt( row );
                        if(userRolesInfoBean.isRole()){
                            Vector vecUsers = userRolesInfoBean.getUsers();
                            if(vecUsers!= null){
                                /** Filter the users based on the Role Id  and compare with the 
                                 *current user then add it to the cvRights
                                 */
                                for(int userRow = 0;  userRow  < vecUsers.size(); userRow ++){
                                    UserRolesInfoBean usersBean = (UserRolesInfoBean)vecUsers.elementAt(userRow);
                                    String roleUsers = (String)usersBean.getUserBean().getUserId();
                                    if (roleUsers.equalsIgnoreCase(currentUser)){
                                        cvFileterdRoles  = roleRights.filter(new Equals("roleId",new Integer(userRolesInfoBean.getRoleBean().getRoleId())));
                                        cvRights.addAll(cvFileterdRoles);                                        
                                    }
                                }
                            }
                        }
                    }
                    CoeusVector modifyRight = null;
                    CoeusVector viewRight = null;
                    if(cvRights.size() > 0){
                        modifyRight = cvRights.filter(new Equals("rightId", "MODIFY_NARRATIVE"));
                    }
                    //If there are no MODIFY_NARRATIVE right then he has only VIEW_NARRATIVE
                    //and can be removed from Narrative also
                    if(modifyRight.size() == 0){
                        /*Delete User Roles and Narrative for this User
                        set AC Type for Narrative as DELETE*/
                        if(narrativeUsers!=null){
                            updatedUsers = narrativeUsers.filter(new Equals("userId",currentUser));
                        }
                        if(updatedUsers != null){
                            for(int index=0; index < updatedUsers.size(); index++){
                                narrativeBean = (ProposalNarrativeModuleUsersFormBean)updatedUsers.get(index);
                                narrativeBean.setAcType(TypeConstants.DELETE_RECORD);
                            }
                        }
                        
                    }else{
                        //Check for View Right and Current Role that is being removed
                        Equals modifyEquals = new Equals("rightId", "VIEW_NARRATIVE");
                        Equals viewEquals = new Equals("roleId", new Integer(currentRoleId));
                        And readAndMod = new And(modifyEquals, viewEquals);
                        viewRight = cvRights.filter(readAndMod);
                        if(viewRight.size() != 0){
                            //If current role being removed has VIEW_NARRATIVE right only then 
                            //delete User Roles only
                        }else{
                            //Now user has MODIFY_NARRATIVE and no VIEW_NARRATIVE right
                            //and it could be in one role or multiple roles
                            //If more than one role then go ahead and delete the User roles
                            if (modifyRight.size() > 1){
                                //Delete User roles only
                            }
                            //For each module check whether this user is the only user with 
                            //MODIFY right.
                            Equals userIdEquals = new Equals("userId", currentUser);
                            Equals accessTypeEquals = new Equals("accessType", new Character('M'));
                            //Equals moduleNumEquals = new Equals("moduleNumber", (Integer)narrativeModules.elementAt(row));
                            And modifyLHSCheck = new And(userIdEquals, accessTypeEquals);
                            And modifyRHSCheck ;
                            if(narrativeUsers!=null){
                                modifyRight = narrativeUsers.filter(modifyLHSCheck);
                            }else{
                                modifyRight = new CoeusVector();
                            }
                            
                            //If User has modify right
                            if(modifyRight.size() > 0){
                                for(int row = 0 ; row < narrativeModules.size(); row++){                               
                                    // Check if other users have Modify Right
                                    NotEquals userIdNotEquals = new NotEquals("userId", currentUser);
                                    Equals acTypeEquals = new Equals("acType", null);
                                    accessTypeEquals = new Equals("accessType", new Character('M'));
                                    Equals moduleNumEquals = new Equals("moduleNumber", (Integer)narrativeModules.elementAt(row));
                                    modifyLHSCheck = new And(userIdNotEquals, accessTypeEquals);
                                    modifyRHSCheck = new And(modifyLHSCheck, moduleNumEquals);
                                    And acTypeAnd = new And(modifyRHSCheck, acTypeEquals);
                                    //System.out.println(acTypeAnd);
                                    modifyRight = narrativeUsers.filter(acTypeAnd);
                                    if(modifyRight.size() == 0){
                                        CoeusOptionPane.showInfoDialog(NO_MODULE_USERS + " " +(Integer)narrativeModules.elementAt(row) +  " ");
                                        //break;
                                        return;
                                    }
                                }
                            }
                            /* Indicates that the selected user is having Modify_Narrative right at Proposal level
                             *but not at the module level OR there are more than one user with modify right at the
                             *module level. So delete the user
                             */
                            modifyRight = cvRights.filter(new Equals("rightId", "MODIFY_NARRATIVE"));
                            if(modifyRight.size() == 1){
                                //Delete Narrative User Rights
                                if(narrativeUsers!=null){
                                    updatedUsers = narrativeUsers.filter(new Equals("userId",currentUser));
                                }
                                if( updatedUsers != null) {
                                    for(int index=0; index < updatedUsers.size(); index++){
                                        narrativeBean = (ProposalNarrativeModuleUsersFormBean)updatedUsers.get(index);
                                        narrativeBean.setAcType(TypeConstants.DELETE_RECORD);
                                    }
                                }
                            }else if(modifyRight.size() > 1){
                                modifyRight = cvRights.filter(new Equals("rightId", "VIEW_NARRATIVE"));
                                if(modifyRight.size() > 0){
                                    //Check if the User has Modify Narrative in different Role
                                    //If yes Update the User Right to Read only
                                    modifyEquals = new Equals("rightId", "MODIFY_NARRATIVE");
                                    NotEquals roleIdNotEquals = new NotEquals("roleId", new Integer(currentRoleId));
                                    modifyRHSCheck = new And(modifyEquals, roleIdNotEquals);
                                    modifyRight = cvRights.filter(modifyRHSCheck);
                                    if(modifyRight.size() == 0){
                                        //Get Each record and Update the AccessType as 'R' for the selected User
                                        updatedUsers = narrativeUsers.filter(new Equals("userId", currentUser));
                                        if(updatedUsers != null){
                                            for(int index=0; index < updatedUsers.size(); index++){
                                                narrativeBean = (ProposalNarrativeModuleUsersFormBean)updatedUsers.get(index);
                                                narrativeBean.setAcType(TypeConstants.UPDATE_RECORD);
                                                narrativeBean.setAccessType('R');
                                            }
                                        }
                                    }else{
                                        //Delete from User Roles
                                    }
                                }
                            }
                        }
                    }                    
                }
            }
        }
        
        public void dragExit(DragSourceEvent dragSourceEvent) {
        }
        
        public void dragOver(DragSourceDragEvent dsde) {
            
        }
        
        public void dropActionChanged(DragSourceDragEvent dsde) {
        }
    }
    /** Get the Role Rights. If the Role has Right then get it.
     * return the boolean value which specifies whether the Role has Right or Not
     */
    private boolean checkRoleHasRight(int roleId){
         Boolean hasRight = null;
        try{
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            RequesterBean request = new RequesterBean();
            request.setDataObject(new Integer(roleId));
            request.setFunctionType('p');
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                hasRight = (Boolean)response.getDataObject();
            }
        }catch(Exception  exception){
            exception.printStackTrace();
        }
        return hasRight.booleanValue();
    }   
}
