/*
 * @(#)UserRoleTree.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on April 10, 2003, 8:05 PM
 */

package edu.mit.coeus.utils.tree;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.*;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.RoleBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.ProtocolRolesFormBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ObjectCloner;

import edu.mit.coeus.user.bean.UserDetailsController;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.CoeusVector;

/** Tree component used to show users and their roles in hierarchy. This tree can be
 * customized by setting some properties like drag and drop enabled, to perform
 * copy/ move operation etc. You can also restrict drag and drop functionality
 * disabled to one role ( admin role ) by specifying the id of the admin role
 * using setAdminRoleId(). And also you can specify one role to which atleast one
 * user should be present ( For ex: Proposal's Aggregator role) using setMandatoryRoleId().
 * <p> The dataFlavor used for drag and drop of the tree node
 * is TransferableuserRoleData.
 * @author ravikanth
 */

public class UserRoleTree extends JTree implements DragGestureListener{
    private boolean move;
    private boolean copyAllChildren;
    private boolean dropable;
    private boolean enableDragDrop = true;
    private boolean expandRole;
    private DragSource dragSource = DragSource.getDefaultDragSource();
    protected CoeusVector roleRights;
    //private DropTarget dropTarget;
    protected DropTarget dropTarget;
    //private final DragSourceListener dragSourceListener = new MyDragSourceListener();
    protected  DragSourceListener dragSourceListener = new MyDragSourceListener();
    
    private UserRoleTree selfRef;
    private Cursor rowCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor defaultCursor = new Cursor( Cursor.DEFAULT_CURSOR );
    
    private RoleBean existingRoleBean;
    
    private boolean modified;
    private int adminRoleId = -1;
    //private UserRolesInfoBean deletedBean;
    protected UserRolesInfoBean deletedBean;
    
    /**
     * Added by Senthil AR to have the added objects to the tree in the Tree
     */
//    private Vector vecAddedUsers = new Vector();
    protected Vector vecAddedUsers = new Vector();
    protected Vector vecDelUsers = new Vector();
    //private Vector vecDelUsers = new Vector();
    protected Vector userAndRoles = null;
    /* if atleast one user should be there under any role then user have to
       specify the role id to this field using get/set methods. Currently used
       in Proposal for Aggregator role.
     */
    private int mandatoryRoleId = -1;
    private boolean dragingInside;
    
    private final char INACTIVE_ROLE_STATUS = 'I';
    
    private static final char ORGANISATIONAL_ROLE = 'O';
    private static final String NO_RIGHT_TO_GRANT_INSTITUTE_ROLE = "You do not have the right to grant an institute role.";
    //private static final String NO_RIGHT_TO_DELETE_INSTITUTE_ROLE = "You do not have the right to delete an institute role";
    protected static final String NO_RIGHT_TO_DELETE_INSTITUTE_ROLE = "You do not have the right to delete an institute role";
    private boolean isProposal = false;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //Added for Narrative Users 
    protected CoeusVector narrativeUsers;
    //COEUSQA:2859 - Users marked as inactive should not appear in selection lists for DP and protocol roles - Start
    private static final char USER_INACTIVE_STATUS = 'I';
    //COEUSQA:2859 - End
    
    /** Creates an instance of the UserRoleTree with invisible root node and
     * initializes listeners for drag and drop. <CODE>UserRoleNode</CODE> is used
     * as node component in this tree and <CODE>UserRoleNodeRenderer</CODE> is used
     * as renderer component.
     */
    public UserRoleTree() {
        super(new UserRoleNode());
        setRootVisible( false );
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE,
        this);
        setCellRenderer( new UserRoleNodeRenderer());
        dropTarget = new DropTarget(this,new MyDropTargetListener());
        setExpandsSelectedPaths(true);
        setShowsRootHandles(true);
        selfRef = this;
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    /**
     * This method is used to specify whether the dragged node from the tree should be
     * moved or copied to the drop target.
     * @param moveCon  boolean specifying the action ( move / copy ) to be performed after
     * successful drop.
     */
    public void setMoveNode(boolean moveCon){
        this.move = moveCon;
    }
    /**
     * This method is used to specify the action to be performed when parent level
     * node is used for drag and drop.
     * @param copy  boolean true specifies to copy all the child nodes of the
     * selected node to the drop target and false specifies not to perform any
     * action.
     */
    public void setCopyAllChildren(boolean copy){
        this.copyAllChildren = copy;
    }
    
    /**
     * This method is used to specify whether this tree component should be used
     * as drop target or not.
     *
     * @param drop  boolean value which specifies the tree accepts any data to be
     * dropped on it or not.
     *
     */
    public void setDropable(boolean drop){
        this.dropable = drop;
    }
    
    /**
     * Method which returns the action to be performed after successful drop
     * of the node on any component which accepts <CODE>TransferableUserRoleData</CODE>.
     * @return  boolean true if the node is to be deleted, else false. */
    public boolean isMoveNode(){
        return move;
    }
    
    /**
     * Method which returns the action to be performed when any parent level node is
     * dragged from the tree and dropped on any drop target component.
     * @return  boolean true to copy all the child nodes to the target, false to ignore
     * the action.
     */
    public boolean isCopyAllChildren(){
        return copyAllChildren;
    }
    
    /**
     * Method which specifies whether this component accepts any data to be dropped
     * on it or not.
     * @return  boolean true if this components accepts any data to be dropped on it
     * else false.
     */
    public boolean isDropable(){
        return dropable;
    }
    /**
     * This method creates the tree with the vector given as argument. This vector is
     * a collection of UserRolesInfoBean. Each element in this collection represents
     * a role. And all the users for that role are available in a collection returned
     * by getUsers() method of that bean instance.
     * @param userRoles  Collection of UserRolesInfoBean which represents the
     * first level nodes in the tree structure.
     *
     */
    public void createTreeData(Vector userRoles){
        UserRoleNode root = (UserRoleNode)((DefaultTreeModel)getModel()).getRoot();
        UserRoleNode node;
        UserRolesInfoBean bean;
        RoleInfoBean roleInfoBean;
        UserInfoBean userInfoBean;
        root.removeAllChildren();
        
        if ( userRoles != null && userRoles.size() > 0 ) {
            int rolesSize = userRoles.size();
            for(int index = 0 ; index < rolesSize ; index++ ) {
                bean = (UserRolesInfoBean) userRoles.elementAt( index );
                roleInfoBean = bean.getRoleBean();
                node = new UserRoleNode(bean);
                root.add(node);
                if( bean.isRole() && bean.hasChildren() ) {
                    Vector children = bean.getUsers();
                    int childCount = children.size();
                    UserRoleNode child;
                    UserRolesInfoBean childBean;
                    for(int childIndex = 0; childIndex < childCount ; childIndex++ ) {
                        childBean = (UserRolesInfoBean) children.elementAt(childIndex);
                        userInfoBean = childBean.getUserBean();
                        node.add( new UserRoleNode(childBean) );
                    }
                }
            }
            
            ( ( DefaultTreeModel )getModel() ).reload();
            expandAll(this,true);
            
        }
        // Added by Chandrashekhar
        else{
            expandAll(this,true);
        }
    }
    
    // DragGestureListener
    
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        TreePath path = getSelectionPath();
        UserRolesInfoBean parentBean = null;
        if (path == null) {
            // Nothing selected, nothing to drag
            //getToolkit().beep();
        } else if( enableDragDrop ){
            UserRoleNode selection = (UserRoleNode)path.getLastPathComponent();
            setSelectionPath( path );
            if(selection.getLevel() == 1 ){
                if(copyAllChildren){
                    int childCount = selection.getChildCount();
                    if( childCount > 0 ) {
                        Vector childData = new Vector();
                        for( int indx = 0; indx < childCount; indx++ ) {
                            UserRoleNode childNode = (UserRoleNode)selection.getChildAt(indx);
                            childData.addElement(childNode.getUserObject());
                        }
                        dragingInside = true;
                        TransferableUserRoleData data = new TransferableUserRoleData(childData);
                        dragSource.startDrag(dragGestureEvent,rowCursor,data,dragSourceListener);
                    }
                }
                return;
            }
            UserRoleNode parent = (UserRoleNode)selection.getParent();
            if(parent != null ){
                parentBean = parent.getDataObject();
                if( !parent.isRoot() ){
                    if( parentBean.getRoleBean().getRoleId() == adminRoleId ) {
                        // if the selected role is Protocol's Coordinator or
                        // Proposal's Approver, don't allow to drag user from
                        // that role.
                        return;
                    }else if( (parentBean.getRoleBean().getRoleId() == mandatoryRoleId )
                    && ( parent.getChildCount() == 1 ) ) {
                        // if the role is Proposal's Aggregator role and number of
                        // users currently under that role is one, then don't allow
                        // to drag that user as atleast one user should be there
                        // for that role.
                        return;
                    }
                }
            }
            dragingInside = true;
            UserRolesInfoBean transferingBean = (UserRolesInfoBean)selection.getUserObject();
            if( parentBean != null ) {
                //System.out.println("parent bean not null");
                deletedBean = new UserRolesInfoBean();
                deletedBean.setRoleBean(parentBean.getRoleBean());
                //System.out.println("adding parents role bean :"+parentBean.getRoleBean());
                deletedBean.setUserBean( transferingBean.getUserBean());
            }
            TransferableUserRoleData data = new TransferableUserRoleData(transferingBean);
            dragSource.startDrag(dragGestureEvent,rowCursor,data,dragSourceListener);
        }
    }
    
    /**
     * DragSource Listener
     */
    class MyDragSourceListener implements DragSourceListener {
        /* Invoked after successful drop of the dragged items */
        public void dragDropEnd(
        DragSourceDropEvent dsde) {
            dragingInside = false;
            if (dsde.getDropSuccess() && move && enableDragDrop ) {
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
                            // COEUSDEV-221	User Maintenance window does not save in one try
//                            if(tempUserRolesInfoBean.getUserBean().getUpdateTimestamp() != null) {
                            if(tempUserRolesInfoBean.getUserBean() != null) {
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
                    modified = true;
                }
            }
        }
        
        public void dragEnter(DragSourceDragEvent dsde) {
            TreePath destinationPath = getSelectionPath();
            UserRoleNode node = (UserRoleNode) destinationPath.getParentPath().getLastPathComponent();
            UserRolesInfoBean  nodeBean = ( UserRolesInfoBean ) node.getUserObject();
            if(! hasInstituteRight(nodeBean)) {
                CoeusOptionPane.showErrorDialog(NO_RIGHT_TO_DELETE_INSTITUTE_ROLE);
            }
        }
        
        public void dragExit(DragSourceEvent dse) {
        }
        
        public void dragOver(DragSourceDragEvent dsde) {
            
        }
        
        public void dropActionChanged(DragSourceDragEvent dsde) {
        }
    }
    
    class MyDropTargetListener implements DropTargetListener {
        
        public void dragEnter(DropTargetDragEvent dtde){
        }
        /**
         * Invoked while draging over the drop target
         */
        public void dragOver(DropTargetDragEvent dtde){
            if( enableDragDrop  && !dragingInside ){
                
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
            if( dropable && enableDragDrop  && !dragingInside ){
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
                                }
                                /** Added to have the added object inside the Vector **/
                                //vecAddedUsers.addElement(dataVector.elementAt( indx ));
                            }
                        }else{
                            //COEUSQA:2859 - Users marked as inactive should not appear in selection lists for DP and protocol roles - Start
                            UserInfoBean userBean = ( UserInfoBean ) data;
                            if(USER_INACTIVE_STATUS == userBean.getStatus()) {
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                        "userRoleTree.exceptionCode.2702"));
                            } else{
                                //COEUSQA:2859 - End
                                /* single item dragged, so add the dragged item */
                                addUserNode( data , destinationPath);
                                /** Added to have the added object inside the Vector **/
                                //vecAddedUsers.addElement(data);
                            }
                        }
                        ( ( DefaultTreeModel )getModel() ).reload();
                        if( expandRole ) {
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
    
    /**
     * This method will return the Vector that contains all the object that is added
     * the UserRolesTree by drag and drop.
     */
    public Vector getAddedUsers(){
        return vecAddedUsers;
    }
    
    public Vector getRemovedUsers(){
        return vecDelUsers;
    }
    
    /**
     * Supporting method used to construct the UserNode from the given UserInfoBean
     * and add it to the specified parent node.
     */
    //private void addUserNode( Object userData , TreePath destinationPath ){
    public void addUserNode( Object userData , TreePath destinationPath ){
        //UserRolesInfoBean userRolesBean = new UserRolesInfoBean();
        /**
         * Senthil AR added these object to be used in UserMintenance module
         */
        UserRolesInfoBean userRoleInfoBean = new UserRolesInfoBean();
        RoleInfoBean roleInfoBean = null;
        
        if( userData instanceof UserInfoBean ) {
            
            //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - Start
            if(isDuplicate(destinationPath, ((UserInfoBean)userData))) {
                return;
            }
            //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - End
            
            /*
            UserInfoBean userBean = (UserInfoBean) userData;
            UserRoleNode newParent =
                    (UserRoleNode)destinationPath.getLastPathComponent();
             
            userRolesBean.setUserBean(userBean);
            UserRoleNode newChild = new UserRoleNode(userRolesBean);
            modified = true;
            newParent.add(newChild);
            // Senthil AR - This vector will have all the objects that are added
            // to the tree
             
            userRoleInfoBean = (UserRolesInfoBean)newParent.getUserObject();
            userRoleInfoBean.setRoleBean(userRoleInfoBean.getRoleBean());
            //userRoleInfoBean.setUserBean(userBean);
            userRoleInfoBean.setUserBean(userRolesBean.getUserBean());
            vecAddedUsers.addElement(userRoleInfoBean);
             */
            
            UserInfoBean userBean = new UserInfoBean();
            userBean = (UserInfoBean) userData;
            UserRoleNode newParent = (UserRoleNode)destinationPath.getLastPathComponent();
            //userRolesBean.setUserBean(userBean);
            userRoleInfoBean.setUserBean(userBean);
            // COEUSDEV-221	User Maintenance window does not save in one try - Start
            UserRolesInfoBean  nodeBean = ( UserRolesInfoBean ) newParent.getUserObject();
            userRoleInfoBean.setRoleBean(nodeBean.getRoleBean());
            // COEUSDEV-221	User Maintenance window does not save in one try - End
            //UserRoleNode newChild = new UserRoleNode(userRolesBean);
            UserRoleNode newChild = new UserRoleNode(userRoleInfoBean);
            modified = true;
            newParent.add(newChild);
            //userRoleInfoBean = (UserRolesInfoBean)newParent.getUserObject();
            userRoleInfoBean = null;
            try{
                userRoleInfoBean = (UserRolesInfoBean)ObjectCloner.deepCopy((UserRolesInfoBean)newParent.getUserObject());
                
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            //userRoleInfoBean = new UserRolesInfoBean();
            //Has to be tested. Looks like its working.
            //userRoleInfoBean.setUserBean(userBean) ;
            userRoleInfoBean.setRoleBean(userRoleInfoBean.getRoleBean());
            
            userRoleInfoBean.setUserBean((UserInfoBean) userData);
            //Added to check if this is contained in Deleted Since 
            //If this bean is Added - Deleted - Added. it should finally only do Addition.
            int index = contains(vecDelUsers, userRoleInfoBean);
            if(index != -1) {
                //Set TimeStamp so that Delete - Add - Delete will still hold the Timestamp.
                UserRolesInfoBean dupUserRolesInfoBean = (UserRolesInfoBean)vecDelUsers.get(index);
                userRoleInfoBean.getUserBean().setUpdateTimestamp(dupUserRolesInfoBean.getUserBean().getUpdateTimestamp());
                vecDelUsers.remove(index);
            }
            
            //Bug Fix: Descend Flag has to be set to false (9-Feb-1004) - START
            userRoleInfoBean.getRoleBean().setDescend(false);
            //Bug Fix: Descend Flag has to be set to false (9-Feb-1004) - END
            
            vecAddedUsers.add(userRoleInfoBean);
        }
    }
    
    /**
     * Method used to check whether user has dropped the item(s) on valid location
     * or not.
     *
     * @param destination selected TreePath for dropping
     * @return  boolean true if the user has selected active role node else false.
     */
    public boolean isValidDrop(TreePath destination){
        
        if (destination == null){
            // no treepath available for the selected drop location, so not valid
            // drop location.
            return false;
        }
        
        UserRoleNode node = (UserRoleNode) destination.getLastPathComponent();
        UserRolesInfoBean  nodeBean = ( UserRolesInfoBean ) node.getUserObject();
        
        if ( !nodeBean.isRole() ) {
            // dropped on User node , so not valid drop
            return false;
        }
        if ( nodeBean.getRoleBean().getStatus() == INACTIVE_ROLE_STATUS ){
            // dropped on inactive role
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "userRoleTree.exceptionCode.2700"));
            return false;
            
        }
        if( nodeBean.getRoleBean().getRoleId() == adminRoleId ) {
            // dropped on admin role node
            CoeusOptionPane.showInfoDialog( coeusMessageResources.parseMessageKey(
            "userRoleTree.exceptionCode.2701") + " "+node.toString() + " role " );
            return false;
        }
        
        //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - Start
        try{
            RoleInfoBean roleInfoBean = nodeBean.getRoleBean();
            if(roleInfoBean.getRoleType() == ORGANISATIONAL_ROLE) {
                UserDetailsController userDetailsController = new UserDetailsController();
                
                Vector data = (Vector)userDetailsController.readRolesAndAuthorizations(CoeusGuiConstants.getMDIForm().getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, CoeusGuiConstants.getMDIForm().getUnitNumber());
                HashMap authorizations = (HashMap)data.get(0);
                String GRANT_INSTITUTE_ROLES = "GRANT_INSTITUTE_ROLES";
                boolean hasOSPRight = ((Boolean)authorizations.get(GRANT_INSTITUTE_ROLES)).booleanValue();
                if(!hasOSPRight) {
                    CoeusOptionPane.showErrorDialog(NO_RIGHT_TO_GRANT_INSTITUTE_ROLE);
                    return false;
                }
            }
        }catch (CoeusClientException coeusClientException) {
            coeusClientException.printStackTrace();
            return false;
        }
        //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - End
        
        return true;
    }
    
    //private boolean hasInstituteRight(UserRolesInfoBean  nodeBean) {
    protected boolean hasInstituteRight(UserRolesInfoBean  nodeBean) {
        //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - Start
        try{
            //Bug Fix : NullPOinterException in Send Proposal Notification when Parent node is Dragged - Start
            if(nodeBean == null) return true;
            //Bug Fix : NullPOinterException in Send Proposal Notification when Parent node is Dragged - End
            
            RoleInfoBean roleInfoBean = nodeBean.getRoleBean();
            if(roleInfoBean.getRoleType() == ORGANISATIONAL_ROLE) {
                UserDetailsController userDetailsController = new UserDetailsController();
                
                Vector data = (Vector)userDetailsController.readRolesAndAuthorizations(CoeusGuiConstants.getMDIForm().getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, CoeusGuiConstants.getMDIForm().getUnitNumber());
                HashMap authorizations = (HashMap)data.get(0);
                String GRANT_INSTITUTE_ROLES = "GRANT_INSTITUTE_ROLES";
                boolean hasOSPRight = ((Boolean)authorizations.get(GRANT_INSTITUTE_ROLES)).booleanValue();
                if(!hasOSPRight) {
                    //CoeusOptionPane.showErrorDialog(NO_RIGHT_TO_GRANT_INSTITUTE_ROLE);
                    return false;
                }
            }
        }catch (CoeusClientException coeusClientException) {
            coeusClientException.printStackTrace();
            return false;
        }
        //Bug Fix:User Rights Check before drag to Roles - (16-Jan-2004) - Start
        return true;
    }
    
    /**
     * Checks for Duplicate user in the Node.
     */
    private boolean isDuplicate( TreePath destinationPath, UserInfoBean userInfoBeanToCheck) {
        UserRoleNode node = (UserRoleNode) destinationPath.getLastPathComponent();
        UserRolesInfoBean  nodeBean = ( UserRolesInfoBean ) node.getUserObject();
        
        RoleInfoBean roleInfoBean = nodeBean.getRoleBean();
        
        Vector users = nodeBean.getUsers();
        if( users == null ) {
            users = new Vector();
        }
        if(vecAddedUsers != null) {
            users.addAll(vecAddedUsers);
        }
        UserInfoBean userInfoBean;
        RoleInfoBean roleInfoBeanToCheck;
        if(users != null) {
            for(int index = 0; index < users.size(); index++) {
                userInfoBean = ((UserRolesInfoBean)users.get(index)).getUserBean();
                roleInfoBeanToCheck = ((UserRolesInfoBean)users.get(index)).getRoleBean();
                
                if(userInfoBean.getUserId().equals(userInfoBeanToCheck.getUserId()) &&
                //userInfoBean.getUnitNumber().equals(userInfoBeanToCheck.getUnitNumber()) &&
                (! TypeConstants.DELETE_RECORD.equals(userInfoBean.getAcType())) &&
                      // COEUSDEV-221	User Maintenance window does not save in one try - - Start
//                    (roleInfoBean.equals(roleInfoBeanToCheck))
                        roleInfoBean.getRoleId() == roleInfoBeanToCheck.getRoleId()
                        && roleInfoBean.getRoleType() == roleInfoBeanToCheck.getRoleType()
                        && roleInfoBean.getUnitNumber().equals(roleInfoBeanToCheck.getUnitNumber())
                        && roleInfoBean.getStatus() == roleInfoBeanToCheck.getStatus()
                        // COEUSDEV-221	User Maintenance window does not save in one try - End
                ) {
                    return true;
                }
            }
        }
        
        if(vecAddedUsers != null) {
            users.removeAll(vecAddedUsers);
        }
        
        return false;
    }
    
    /**
     *Checks if this vector contains userRolesInfoBean.
     *If it contains then it returns the first index where it found.
     *else returns -1. 
     */
    //private int contains(Vector vector, UserRolesInfoBean userRolesInfoBean) {
    protected int contains(Vector vector, UserRolesInfoBean userRolesInfoBean) {
        UserInfoBean userInfoBean;
        RoleInfoBean roleInfoBeanToCheck;
        if(vector != null) {
            for(int index = 0; index < vector.size(); index++) {
                userInfoBean = ((UserRolesInfoBean)vector.get(index)).getUserBean();
                roleInfoBeanToCheck = ((UserRolesInfoBean)vector.get(index)).getRoleBean();
                //System.out.println(userInfoBean.getUserId().equals(userRolesInfoBean.getUserBean().getUserId()));
                //System.out.println((roleInfoBeanToCheck.equals(userRolesInfoBean.getRoleBean())));
                if(userInfoBean.getUserId().equals(userRolesInfoBean.getUserBean().getUserId()) &&
                //(! TypeConstants.DELETE_RECORD.equals(userInfoBean.getAcType())) &&
                (roleInfoBeanToCheck.getRoleId() == userRolesInfoBean.getRoleBean().getRoleId())
                ) {
                    return index;
                }
            }
        }
       
        return -1;
    }
    
    /**
     * Method which returns all role nodes as vector of UserRolesInfoBean. All users
     * under each role will be available from getUsers() method of the bean.
     * @return Collection of UserRolesInfoBean which consists of role nodes data
     */
    public Vector getUserRoles(){
        UserRoleNode root =  ( UserRoleNode ) (( DefaultTreeModel )getModel()).getRoot();
        Vector newUserRoles = null;
        UserRolesInfoBean bean,childBean;
        UserInfoBean userBean;
        Vector children;
        int childCount;
        
        if(root != null  &&  root.getChildCount() > 0 ) {
            newUserRoles = new Vector();
            UserRoleNode node = ( UserRoleNode )root.getFirstChild();
            while ( node != null ) {
                bean = (UserRolesInfoBean) node.getUserObject();
                if( bean.isRole() ) {
                    if( node.getChildCount() > 0 ){
                        bean.setHasChildren(true);
                        Enumeration enumChildren = node.children();
                        children = new Vector();
                        while( enumChildren.hasMoreElements() ) {
                            UserRoleNode childNode = ( UserRoleNode )enumChildren.nextElement();
                            childBean = childNode.getDataObject();
                            children.addElement( childBean );
                        }
                        bean.setUsers( children );
                    }else{
                        bean.setHasChildren( false );
                        bean.setUsers( null );
                    }
                }
                newUserRoles.addElement( bean );
                node = ( UserRoleNode ) node.getNextSibling();
            }
        }
        return newUserRoles;
    }
    
    
    /**
     * Method used to expand/ collapse all the nodes in the tree.
     * @param tree JTree whose nodes are to be expanded/ collapsed.
     * @param expand  boolean true to expand all nodes, false to collapse.
     */
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        
        
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    
    //private void expandAll(JTree tree, TreePath parent, boolean expand) {
    public void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
    
    /** Getter for property enableDragDrop.
     * @return Value of property enableDragDrop.
     */
    public boolean isEnableDragDrop() {
        return enableDragDrop;
    }
    
    /** Setter for property enableDragDrop.
     * @param enableDragDrop New value of property enableDragDrop.
     */
    public void setEnableDragDrop(boolean enableDragDrop) {
        this.enableDragDrop = enableDragDrop;
    }
    
    /** Getter for property modified.
     * @return Value of property modified.
     */
    public boolean isModified() {
        return modified;
    }
    
    /** Setter for property modified.
     * @param modified New value of property modified.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    /**
     * Returns the admin role id
     * @return int representing the id used as Admin Role Id
     */
    public int getAdminRoleId() {
        return adminRoleId;
    }
    
    /**
     * Sets the Admin Role Id which is used in drag and drop validations.
     * @param adminRoleId int representing the Admin Role Id.
     */
    public void setAdminRoleId(int adminRoleId) {
        this.adminRoleId = adminRoleId;
    }
    
    
    /** Method used to set the role id to which atleast one user should be present.
     * @param mandatoryRoleId String representing the role id.
     */
    public void setMandatoryRoleId(int mandatoryRoleId) {
        this.mandatoryRoleId = mandatoryRoleId;
    }
    
    /** Getter for property dragingInside.
     * @return Value of property dragingInside.
     *
     */
    public boolean isDragingInside() {
        return dragingInside;
    }
    
    /** Setter for property dragingInside.
     * @param dragingInside New value of property dragingInside.
     *
     */
    public void setDragingInside(boolean dragingInside) {
        this.dragingInside = dragingInside;
    }
    
    /** Getter for property expandRole.
     * @return Value of property expandRole.
     *
     */
    public boolean isExpandRole() {
        return expandRole;
    }
    
    /** Setter for property expandRole.
     * @param expandRole New value of property expandRole.
     *
     */
    public void setExpandRole(boolean expandRole) {
        this.expandRole = expandRole;
    }
    
    /** Getter for property narrativeUsers.
     * @return Value of property narrativeUsers.
     *
     */
    public CoeusVector getNarrativeUsers() {
        return narrativeUsers;
    }
    
    /** Setter for property narrativeUsers.
     * @param narrativeUsers New value of property narrativeUsers.
     *
     */
    public void setNarrativeUsers(CoeusVector narrativeUsers) {
        this.narrativeUsers = narrativeUsers;
    }
    
    /** Getter for property userAndRoles.
     * @return Value of property userAndRoles.
     *
     */
    public java.util.Vector getUserAndRoles() {
        return userAndRoles;
    }
    
    /** Setter for property userAndRoles.
     * @param userAndRoles New value of property userAndRoles.
     *
     */
    public void setUserAndRoles(java.util.Vector userAndRoles) {
        this.userAndRoles = userAndRoles;
    }
    
    /** Getter for property roleRights.
     * @return Value of property roleRights.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getRoleRights() {
        return roleRights;
    }
    
    /** Setter for property roleRights.
     * @param roleRights New value of property roleRights.
     *
     */
    public void setRoleRights(edu.mit.coeus.utils.CoeusVector roleRights) {
        this.roleRights = roleRights;
    }
    
    //   public void setExpandAllRoles(boolean expandRole) {
    //        this.expandRole = expandRole;
    //    }
    
}