/*
 * UserRolesMaintenanceForm.java
 *
 * Created on June 26, 2003, 11:03 AM
 */

package edu.mit.coeus.user.gui;

import java.util.Vector;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.utils.tree.UserRoleNode;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.tree.UserRoleTree;

import edu.mit.coeus.user.bean.UserDetailsController;

/**
 *
 * @author  senthilar
 */
public class UserRolesMaintenanceForm extends JComponent 
                            implements TypeConstants {
    
    private Vector users, userRoleDetails;
    private CoeusDlgWindow parent;
    private DropTarget dropTarget;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    private boolean saveRequired;    
    private ImageIcon trashIcon,animatedTrashIcon;
    private Vector userRolesData;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusInternalFrame parentIFrame;
    private Vector vecListeners;
    private UserInfoBean userInfoBean;
    private Vector vecRemovedUsers = new Vector();
    
    /** Creates new form UserRolesMaintenanceForm */
    public UserRolesMaintenanceForm(Vector userAndRoleDetails) {
        if( userAndRoleDetails != null && userAndRoleDetails.size() == 2 ) {
            this.users = ( Vector ) userAndRoleDetails.elementAt( 0 );
            this.userRoleDetails = ( Vector ) userAndRoleDetails.elementAt( 1 );
        }        
    }
    
    /**
     * Method which initializes all the form components and sets the default data
     * to the components.
     * @param parentComponent reference to the calling dialog window.
     * @return JComponent reference to this component.
     */
    public JComponent showUserRolesMaintenance(CoeusDlgWindow parentComponent ) throws Exception{
        initComponents();
        this.parent = parentComponent;
        //lblModuleLabel.setText( moduleLabelName );
        trashIcon = new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.TRASH_ICON ));
        
        animatedTrashIcon = new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.ANIMATED_TRASH_ICON ));
        userRoleTree.setDropable( true );
        userRoleTree.setMoveNode( true );
        
        lblTrash.setIcon(trashIcon);
        dropTarget = new DropTarget(lblTrash,new LabelDropTargetListener());
        userRolesTable.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        setFormData();
        //formatFields();
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        //userRoleTree.setAdminRoleId( adminRoleId );
        //userRoleTree.setMandatoryRoleId( mandatoryRoleId );
        return pnlMain;
    }
    
    /**
     * Method which initializes all the form components and sets the default data
     * to the components.
     * @param parentComponent reference to the calling dialog window.
     * @return JComponent reference to this component.
     */
    public JComponent showUserRolesMaintenance(CoeusInternalFrame parentIFrame ) throws Exception{
        initComponents();
        
        //Bug Fix:User Rights Check before drag to Roles/Trash - (16-Jan-2004) - Start
        UserDetailsController userDetailsController = new UserDetailsController();
        String MAINTAIN_USER_ACCESS = "MAINTAIN_USER_ACCESS";
        Vector data = (Vector)userDetailsController.readRolesAndAuthorizations(mdiForm.getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, mdiForm.getUnitNumber());
        HashMap authorizations = (HashMap)data.get(0);
        boolean canMaintainUser = ((Boolean)authorizations.get(MAINTAIN_USER_ACCESS)).booleanValue();
        //userRolesTable.getDropTarget().setActive(canMaintainUser);
        userRoleTree.setDropable(canMaintainUser);
        if(canMaintainUser) {
            dropTarget = new DropTarget(lblTrash,new LabelDropTargetListener());
        }
        //Bug Fix:User Rights Check before drag to Roles/Trash - (16-Jan-2004) - End
        
        //paneUser.setHorizontalScrollBarPolicy();
        userRolesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.parentIFrame = parentIFrame;
        //lblModuleLabel.setText( moduleLabelName );
        trashIcon = new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.TRASH_ICON ));
        //trashIcon.getIconHeight()
        
        animatedTrashIcon = new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.ANIMATED_TRASH_ICON ));
        //userRoleTree.setDropable( true ); //Moved Top Bug Fix:User Rights Check
        userRoleTree.setMoveNode( true );
        userRoleTree.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        
        lblTrash.setIcon(trashIcon);
        //dropTarget = new DropTarget(lblTrash,new LabelDropTargetListener());//Moved Top Bug Fix:User Rights Check
        
        userRolesTable.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        setFormData();
        //formatFields();
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        //userRoleTree.setAdminRoleId( adminRoleId );
        //userRoleTree.setMandatoryRoleId( mandatoryRoleId );
        return pnlMain;
    }
    
    public void setFormData(Vector userAndRoleDetails) {
        if( userAndRoleDetails != null && userAndRoleDetails.size() == 2 ) {
            this.users = ( Vector ) userAndRoleDetails.elementAt( 0 );
            this.userRoleDetails = ( Vector ) userAndRoleDetails.elementAt( 1 );
        }
    }
    
    /* supporting method which  sets the data to all of the form components */
    
    private void setFormData(){
        
        if( users != null && users.size() > 0 ) {
            userRolesTable.setUserTableData( users, getTableColumnNames());
            userRolesTable.clearSelection();
        }
        
        if( userRoleDetails != null && userRoleDetails.size() > 0 ) {
            //existingUsers = getUserRoleInfo ( userRoleDetails );
            userRoleTree.createTreeData( userRoleDetails );
            // addUserInfo ( userRoleDetails );
        }
    }
    
    /* supporting method which returns the column names to be used for UserRolesTable
     */
    private Vector getTableColumnNames(){
        Vector colNames = new Vector();
        colNames.addElement("Role");
        colNames.addElement("User ID");
        colNames.addElement("User Name");
        colNames.addElement("Unit Number");
        colNames.addElement("Unit Name");
        colNames.addElement("Index");
        return colNames;
    }
    
    /**
     * This method will return the added users to the tree.
     */
    public Vector getAddedUsers(){
        return userRoleTree.getAddedUsers();
    }
    
    public Vector getRemovedUsers(){
        return userRoleTree.getRemovedUsers();
    }
    
    /* supporting method which sets all the user details to the user table */
    
    private void addUserInfo( Vector userRoleDetails ) {
        int rolesSize = userRoleDetails.size();
        Vector extractedUsers = new Vector();
        UserRolesInfoBean bean,childBean;
        RoleInfoBean roleBean;
        UserInfoBean userBean;
        Vector children;
        int childCount;
        for(int index = 0 ; index < rolesSize ; index++ ) {
            bean = (UserRolesInfoBean) userRoleDetails.elementAt( index );
            if( bean.isRole() ) {
                roleBean = bean.getRoleBean();
                if( bean.hasChildren() ){
                    children = bean.getUsers();
                    childCount = children.size();
                    for(int childIndex = 0; childIndex < childCount ; childIndex++ ) {
                        childBean = (UserRolesInfoBean) children.elementAt(childIndex);
                        userBean = childBean.getUserBean();
                        userRolesTable.addRow( userBean ) ;
                    }
                }
            }
        }
    }
    
    /*
     * This method will return the Selected user in the UserRolesTable.
     */
    public UserInfoBean getSelectedUser(){
        return userRolesTable.getSelectedUser();
    }
    
    /**
     * This method will return the TableModel of the UserRolesTable.
     */
    public int getSelectedNumberOfRows(){
        int[] noOfRows = userRolesTable.getSelectedRows();
        return noOfRows.length;
    }
    
    /** Getter for property rolesTree.
     * @return Value of property rolesTree.
     */
    public edu.mit.coeus.utils.tree.UserRoleTree getRolesTree() {
        return userRoleTree;
    }
    
    /** Returns whether data is to be saved or not.
     * @return true if data is to be saved else false.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /**
     * Sets the saveRequired flag with the given parameter value.
     * @param save boolean value to be set to the saveRequired flag.
     */
    public void setSaveRequired( boolean save ) {
        saveRequired = save;
    }
    
    /** Method which returns collection of UserRolesInfoBean
     * @return Collection of UserRolesInfoBean from roles tree.
     */
    public java.util.Vector getUserRolesData() {
        userRolesData = userRoleTree.getUserRoles();
        return userRolesData;
    }
    
    class LabelDropTargetListener implements DropTargetListener {
        
        public void dragEnter(DropTargetDragEvent dtde){
        }
        
        public void dragOver(DropTargetDragEvent dtde){
            
        }
        public void dropActionChanged(DropTargetDragEvent dtde){
        }
        public void dragExit(DropTargetEvent dte){
        }
        public void drop(DropTargetDropEvent dtde){
            Transferable tr = dtde.getTransferable();
            
            UserRolesInfoBean userRolesBean = new UserRolesInfoBean();
            Object data = new Object();
            try {
                data = tr.getTransferData(
                TransferableUserRoleData.ROLE_FLAVOR );
            } catch ( Exception e){
                dtde.getDropTargetContext().dropComplete(false);
            }
            if( data instanceof UserRolesInfoBean ) {
                //dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                lblTrash.setIcon( animatedTrashIcon );
                AnimateIcon animate = new AnimateIcon();
                
                lblTrash.setIcon( animatedTrashIcon );
                animate.start();
                dtde.getDropTargetContext().dropComplete(true);
            }
        }
    }
    
    /* Class used to animate the trash icom by starting it in a new thread */
    class AnimateIcon extends Thread {
        Runnable runThread;
        AnimateIcon( ) {
            runThread = new Runnable() {
                public void run() {
                    lblTrash.setIcon( trashIcon );
                }
            };
        }
        
        public void run() {
            try{
                Thread.sleep(500);
                SwingUtilities.invokeLater( runThread );
            } catch ( Exception e) {
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        paneUser = new javax.swing.JScrollPane();
        userRolesTable = new edu.mit.coeus.utils.UserRolesTable();
        paneRoles = new javax.swing.JScrollPane();
        userRoleTree = new edu.mit.coeus.utils.tree.UserRoleTree();
        pnlTrash = new javax.swing.JPanel();
        lblTrash = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout(5, 5));

        pnlMain.setLayout(new java.awt.BorderLayout(5, 5));

        paneUser.setBorder(new javax.swing.border.TitledBorder(""));
        paneUser.setViewportView(userRolesTable);

        pnlMain.add(paneUser, java.awt.BorderLayout.WEST);

        paneRoles.setViewportView(userRoleTree);

        pnlMain.add(paneRoles, java.awt.BorderLayout.CENTER);

        pnlTrash.setLayout(new java.awt.BorderLayout(5, 5));

        lblTrash.setPreferredSize(new java.awt.Dimension(80, 80));
        lblTrash.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        lblTrash.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlTrash.add(lblTrash, java.awt.BorderLayout.SOUTH);

        pnlMain.add(pnlTrash, java.awt.BorderLayout.EAST);

        add(pnlMain, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    /** Getter for property userRolesTable.
     * @return Value of property userRolesTable.
     */
    public edu.mit.coeus.utils.UserRolesTable getUserRolesTable() {
        return userRolesTable;
    }    

    public boolean userExists(String userId) {
        int count = users.size();
        for(int index = 0; index < count; index++) {
            if(userId.equals(((UserInfoBean)users.get(index)).getUserId())) {
                return true;
            }
        }
        return false;
    }
    
    public void addUser(UserInfoBean userInfoBean) {
        userRolesTable.addRow(userInfoBean);        
    }
    
    public void modifyUser(UserInfoBean userInfoBean) {
        userRolesTable.modifyRow(userInfoBean);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.mit.coeus.utils.UserRolesTable userRolesTable;
    private javax.swing.JPanel pnlTrash;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane paneRoles;
    private javax.swing.JScrollPane paneUser;
    private edu.mit.coeus.utils.tree.UserRoleTree userRoleTree;
    private javax.swing.JLabel lblTrash;
    // End of variables declaration//GEN-END:variables
    
}
