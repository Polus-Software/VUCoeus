/*
 * UnitUserRolesMaintenanceController.java
 *
 * Created on May 12, 2011, 1:55 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.user.controller;

import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.UnitUserRolesMaintenanceFormBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.user.gui.UnitUserRolesMaintenanceForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author divyasusendran
 */
public class UnitUserRolesMaintenanceController extends Controller implements ActionListener,TreeSelectionListener,TreeModelListener{
    
    private String CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL;
    private final String USER_ROLES_SERVLET = "/userMaintenanceServlet";
    private static final char GET_ALL_ROLES_FOR_USER = 'G';
    private static final char CHECK_USER_HAS_RIGHT = 'H';
    private static final char DELETE_USER_ROLES = 'I';
    private static final String MAINTAIN_USER_ROLE_RIGHT = "MAINTAIN_USER_ROLES";
    private CoeusMessageResources coeusMessageResources;
    private String selectedUser;
    private String loggedInUser;
    private UserInfoBean userInfoBean;
    private UnitUserRolesMaintenanceForm unitUserRolesMaintenanceForm;    
    private DefaultMutableTreeNode treeRoot;
    private JTree unitUserRolesTree;
    private CoeusDlgWindow dlgUnitUserRolesForm;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private Vector vecUnitUserRolesData;
    private UnitUserRolesTreeRenderer unitUserRolesTreeRenderer;
    private UnitUserRolesDetailsController unitUserRolesDetailsController;
    private static final int WIDTH = 700;
    private static final int HEIGHT = 640;
    
    
    /**
     * Creates a new instance of UnitUserRolesMaintenanceController
     */
    public UnitUserRolesMaintenanceController() {
    }
    
    /**
     * Creates a new instance of UnitUserRolesMaintenanceController
     */
    public UnitUserRolesMaintenanceController(UserInfoBean userInfoBean,String loggedInUser) {
        this.selectedUser = userInfoBean.getUserId();
        this.loggedInUser = loggedInUser;
        this.userInfoBean = userInfoBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        try {
            setFormData(null);
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        postInitComponents();
        createUserRolesTree();
    }
    
    
    /** Specifies the Modal window 
     */
    private void postInitComponents(){
        
        dlgUnitUserRolesForm = new CoeusDlgWindow(mdiForm);
        dlgUnitUserRolesForm.setTitle(CoeusGuiConstants.UNIT_USER_ROLE_MAINTENENCE_WINDOW_TITLE + userInfoBean.getUserName());
        dlgUnitUserRolesForm.setResizable(false);
        dlgUnitUserRolesForm.setModal(true);
        dlgUnitUserRolesForm.getContentPane().add(unitUserRolesMaintenanceForm);
        dlgUnitUserRolesForm.setFont(CoeusFontFactory.getLabelFont());
        dlgUnitUserRolesForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgUnitUserRolesForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgUnitUserRolesForm.getSize();
        dlgUnitUserRolesForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgUnitUserRolesForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    dlgUnitUserRolesForm.dispose();
                }
            }
        });
        
        dlgUnitUserRolesForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgUnitUserRolesForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                dlgUnitUserRolesForm.dispose();
            }
        });
        
    }
    
    /**
     * Method to create the  nodes
     */
    private void createUserRolesTree(){
        DefaultMutableTreeNode unitUserRoleNode = null;
        if(vecUnitUserRolesData != null && vecUnitUserRolesData.size()>0){
            for(int i=0 ; i < vecUnitUserRolesData.size() ; i++){
                UnitUserRolesMaintenanceFormBean bean = (UnitUserRolesMaintenanceFormBean)vecUnitUserRolesData.get(i);
                unitUserRoleNode = new DefaultMutableTreeNode(bean,true);
                treeRoot.add(unitUserRoleNode);
                Vector vecChild  = bean.getVecRoles();
                if(vecChild == null || vecChild.size() <= 0){
                    continue;
                }else{
                    
                    for(int j = 0;j<vecChild.size();j++){
                        RoleInfoBean roleInfoBean = (RoleInfoBean)vecChild.get(j);
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(roleInfoBean,false);
                        unitUserRoleNode.add(node);
                    }
                }
            }
            for (int i = 0; i < unitUserRolesTree.getRowCount(); i++) {
                unitUserRolesTree.expandRow(i);
            }
            unitUserRolesTree.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
        
    }
    
    /*to register all the components in the form*/
    public void registerComponents() {
        unitUserRolesMaintenanceForm = new UnitUserRolesMaintenanceForm();
        java.awt.Component[] component = {            
            unitUserRolesMaintenanceForm.btnAdd,
            unitUserRolesMaintenanceForm.btnModify,
            unitUserRolesMaintenanceForm.btnRemove,
            unitUserRolesMaintenanceForm.btnClose};
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);        
        unitUserRolesMaintenanceForm.setFocusTraversalPolicy(policy);
        unitUserRolesMaintenanceForm.setFocusCycleRoot(true);        
        
        unitUserRolesMaintenanceForm.btnAdd.addActionListener(this);
        unitUserRolesMaintenanceForm.btnModify.addActionListener(this);
        unitUserRolesMaintenanceForm.btnRemove.addActionListener(this);
        unitUserRolesMaintenanceForm.btnClose.addActionListener(this);
        
        treeRoot = new DefaultMutableTreeNode();
        unitUserRolesTree = new JTree(treeRoot);
        unitUserRolesTree.addTreeSelectionListener(this);
        unitUserRolesTree.putClientProperty("JTree.lineStyle", "None");
        unitUserRolesTree.setOpaque(false);
        unitUserRolesTree.setShowsRootHandles(true);
        unitUserRolesTree.getModel().addTreeModelListener(this);
        unitUserRolesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        unitUserRolesMaintenanceForm.scrPnUnitRoles.setViewportView(unitUserRolesTree);
        unitUserRolesTreeRenderer = new UnitUserRolesTreeRenderer(true);
        unitUserRolesTree.setCellRenderer(unitUserRolesTreeRenderer);
        unitUserRolesTree.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"none");
    }
    
    /**
     * Method used to send the requester Bean to the servlet for database communication.
     * @param the Servlet to be used for communication to the database
     * @param requesterBean a RequesterBean which consist of required details.
     * @return ResponderBean
     */
    private ResponderBean sendToServer(String servlet,RequesterBean requesterBean) throws Exception{
        String connectTo = CONNECTION_URL + servlet;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        return responderBean;
    }
    
    /**
     * Method to get all roles for a given User
     *@param userId
     *@return Vector of UnitUserRolesMaintenanceFormBean containing the details of roles
     *@exception throws Exception
     */
    public Vector getUnitUserRoles(String userId) throws Exception{
        Vector vecResponseData = new Vector();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_ALL_ROLES_FOR_USER);
        requesterBean.setDataObject(userId);
        ResponderBean response = sendToServer(USER_ROLES_SERVLET, requesterBean);
        if(response != null){
            if(response.hasResponse()){
                if(response.getDataObjects() != null) {
                    vecResponseData = (Vector)response.getDataObjects();
                }else{
                    vecResponseData = new Vector();
                }
            }
        }
        return vecResponseData;
    }
    
    /**
     * To set the form data
     *@param Object
     *@exception throws CoeusException
     */
    public void setFormData(Object data) throws CoeusException {        
        vecUnitUserRolesData = new Vector();
        try {
            vecUnitUserRolesData = getUnitUserRoles(selectedUser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        vecUnitUserRolesData = vecUnitUserRolesData == null ? new Vector(): vecUnitUserRolesData;
    }
    
    /**
     * This class is used as renderer component in JTree. Icon of the tree nodes
     * changes depending on the type of the node. Different icons are available for
     * active /inactive role node, active/ inactive user node.
     */
    public class UnitUserRolesTreeRenderer extends DefaultTreeCellRenderer{
        Icon folderNode , hierarchyRoot , costElement;
        ImageIcon activeRoleIcon, inactiveRoleIcon, activeUserIcon, inactiveUserIcon;
        ImageIcon activeAdminRole,inactiveAdminRole,activePropRole,inactivePropRole, activeSysRole, inactiveSysRole;
        ImageIcon activeDescend,inactiveDescend,iacucActiveIcon,iacucInActiveIcon;
        boolean showDescend;
        
         /**
          * Creates new UnitUserRolesTreeRenderer
          */
        public UnitUserRolesTreeRenderer(boolean showDescend){
            this();
            this.showDescend = showDescend;
        }
        
        public UnitUserRolesTreeRenderer(){

            String actRole = "/images/rolepa.gif";
            String inactRole = "/images/rolepi.gif";
            String actUser = "/images/usera.gif";
            String inactUser = "/images/useri.gif";
            
            String actAdminRole = "/adminrolea.gif";
            String inactAdminRole = "/adminrolei.gif";
            String actPropRole = "/proprolepa.gif";
            String inactPropRole = "/proprolepi.gif";
            String actSysRole = "/systemrolea.gif";
            String inactSysRole = "/systemrolei.gif";
            
            String actDescend = "/descendy.gif";
            String inactDescend = "/descendn.gif";
            
            
            java.net.URL aRole = getClass().getClassLoader().getResource( CoeusGuiConstants.ACTIVE_ROLE_ICON );
            java.net.URL inaRole = getClass().getClassLoader().getResource( CoeusGuiConstants.INACTIVE_ROLE_ICON );
            
            java.net.URL aUser = getClass().getClassLoader().getResource( CoeusGuiConstants.ACTIVE_USER_ICON );
            java.net.URL inaUser = getClass().getClassLoader().getResource( CoeusGuiConstants.INACTIVE_USER_ICON );
            
            java.net.URL aAdminRole = getClass().getClassLoader().getResource( CoeusGuiConstants.ADMIN_ACTIVE_ROLE_ICON );
            java.net.URL inaAdminRole = getClass().getClassLoader().getResource( CoeusGuiConstants.ADMIN_INACTIVE_ROLE_ICON );
            
            java.net.URL aPropRole = getClass().getClassLoader().getResource( CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON );
            java.net.URL inaPropRole = getClass().getClassLoader().getResource( CoeusGuiConstants.PROP_INACTIVE_ROLE_ICON );
            
            java.net.URL aSysRole = getClass().getClassLoader().getResource( CoeusGuiConstants.SYSTEM_ACTIVE_ROLE_ICON );
            java.net.URL inaSysRole = getClass().getClassLoader().getResource( CoeusGuiConstants.SYSTEM_INACTIVE_ROLE_ICON );
            
            java.net.URL aDescend = getClass().getClassLoader().getResource( CoeusGuiConstants.DESCEND_YES_ICON );
            java.net.URL inaDescend = getClass().getClassLoader().getResource( CoeusGuiConstants.DESCEND_NO_ICON );
            
            java.net.URL iacucActiveRole = getClass().getClassLoader().getResource( CoeusGuiConstants.IACUC_ACTIVE_ROLE_ICON);
            java.net.URL iacucInACtiveRole = getClass().getClassLoader().getResource( CoeusGuiConstants.IACUC_IN_ACTIVE_ROLE_ICON);
            
            if( aRole != null ) {
                activeRoleIcon = new ImageIcon( aRole );
            }else {
                activeRoleIcon = new ImageIcon( actRole );
            }
            if( inaRole != null ) {
                inactiveRoleIcon = new ImageIcon( inaRole );
            }else {
                inactiveRoleIcon = new ImageIcon( inactRole );
            }
            
            if(aDescend!=null){
                iacucActiveIcon = new ImageIcon(iacucActiveRole);
            }else{
                iacucActiveIcon = new ImageIcon(iacucActiveRole);
            }
            if(inaDescend!=null){
                iacucInActiveIcon = new ImageIcon(iacucInACtiveRole);
            }else{
                iacucInActiveIcon = new ImageIcon(iacucInACtiveRole);
            }
            
            if( aUser != null ) {
                activeUserIcon = new ImageIcon( aUser );
            }else {
                activeUserIcon = new ImageIcon( actUser );
            }
            if( inaUser != null ) {
                inactiveUserIcon = new ImageIcon( inaUser );
            }else {
                inactiveUserIcon = new ImageIcon( inactUser );
            }
            
            if(aAdminRole!=null){
                activeAdminRole = new ImageIcon(aAdminRole);
            }else{
                activeAdminRole = new ImageIcon(actAdminRole);
            }
            if(inaAdminRole!=null){
                inactiveAdminRole = new ImageIcon(inaAdminRole);
            }else{
                inactiveAdminRole = new ImageIcon(inactAdminRole);
            }
            if(aPropRole!=null){
                activePropRole = new ImageIcon(aPropRole);
            }else{
                activePropRole = new ImageIcon(actPropRole);
            }
            if(inaPropRole!=null){
                inactivePropRole = new ImageIcon(inaPropRole);
            }else{
                inactivePropRole = new ImageIcon(inactPropRole);
            }
            if(aSysRole!=null){
                activeSysRole = new ImageIcon(aSysRole);
            }else{
                activeSysRole = new ImageIcon(actSysRole);
            }
            if(inaSysRole!=null){
                inactiveSysRole = new ImageIcon(inaSysRole);
            }else{
                inactiveSysRole = new ImageIcon(inactSysRole);
            }
            if(aDescend!=null){
                activeDescend = new ImageIcon(aDescend);
            }else{
                activeDescend = new ImageIcon(actDescend);
            }
            if(inaDescend!=null){
                inactiveDescend = new ImageIcon(inaDescend);
            }else{
                inactiveDescend = new ImageIcon(inactDescend);
            }
            
        }
        
        /**
         * Overriden method getTreeCellRendererComponent() of DefaultTreeCellRenderer
         *@param JTree tree
         *@param Object value
         *@param boolean sel
         *@param boolean expanded
         *@param boolean leaf
         *@param int row
         *@param boolean hasFocus
         *@return Component
         */
        public Component getTreeCellRendererComponent(JTree tree,Object value, boolean sel, boolean expanded,boolean leaf,int row,boolean hasFocus) {

            JPanel pnlRolesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            JLabel lblDescend = new JLabel();
            JLabel lblRole = new JLabel();
            JLabel lblRoleName =  new JLabel();
            pnlRolesPanel.add(lblRole);
            pnlRolesPanel.add(lblDescend);
            pnlRolesPanel.add(lblRoleName);
            
            super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
            
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)value;
            Object object = selNode.getUserObject();
            if(selNode.isRoot()){
                setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));                
                setFont(new Font("Microsoft Sans Serif",Font.BOLD,11));
                setText("Roles for "+userInfoBean.getUserName());
                setIcon(hierarchyRoot);
                return this;
            }else if( object instanceof UnitUserRolesMaintenanceFormBean ){
                UnitUserRolesMaintenanceFormBean unitUserRolesMaintenanceBean =(UnitUserRolesMaintenanceFormBean)object;
                setFont(new Font("Microsoft Sans Serif",Font.BOLD,11));
                setText("Roles in "+unitUserRolesMaintenanceBean.getUnitNumber()+" : "+unitUserRolesMaintenanceBean.getUnitName());
                setIcon(hierarchyRoot);
                
                return this;
            }else if( object instanceof RoleInfoBean ){
                
                RoleInfoBean roleBean = (RoleInfoBean)object;
                
                switch ( roleBean.getStatus()){
                    case 'A' :  if(!showDescend ){
                        // active role
                        setIcon(activeRoleIcon);
                        break;
                    }else{
                        switch ( roleBean.getRoleType() ) {
                            case 'S' :
                                lblRole.setIcon(activeSysRole);
                                
                                break;
                            case 'R' :
                                lblRole.setIcon(activeRoleIcon);
                                break;
                            case 'P' :
                                lblRole.setIcon(activePropRole);
                                break;
                            case 'O' :
                                lblRole.setIcon(activeAdminRole);
                                break;
                            case 'I':
                                lblRole.setIcon(iacucActiveIcon);
                                break;
                                
                        }
                        if( roleBean.isDescend() ) {
                            lblDescend.setIcon(activeDescend);
                        }else{
                            lblDescend.setIcon(inactiveDescend);
                        }
                        lblRoleName.setText(roleBean.getRoleName());
                    }
                    break;
                    
                    case 'I' :  if(!showDescend ){
                        setIcon(inactiveRoleIcon);
                        break;
                    }else{
                        switch(roleBean.getRoleType()){
                            case 'S' :
                                lblRole.setIcon(inactiveSysRole);
                                break;
                            case 'R' :
                                lblRole.setIcon(inactiveRoleIcon);
                                break;
                            case 'P' :
                                lblRole.setIcon(inactivePropRole);
                                break;
                            case 'O' :
                                lblRole.setIcon(inactiveAdminRole);
                                break;
                            case 'I':
                                lblRole.setIcon(iacucInActiveIcon);
                                break;
                        }
                        if( roleBean.isDescend()) {
                            lblDescend.setIcon(activeDescend);
                        }else{
                            lblDescend.setIcon(inactiveDescend);
                        }
                        lblRoleName.setText(roleBean.getRoleName());
                    }
                    
                    break;
                }
                if( sel ) {
                    pnlRolesPanel.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                    lblDescend.setForeground(Color.white);
                }else   {
                    pnlRolesPanel.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                    
                }
            }
            setBackgroundNonSelectionColor((java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background"));
            if(!showDescend){
                return this;
            }else{
                return pnlRolesPanel;
            }
        }
    }   
    
    /** 
     * Displays the GUI.
     */
    public void display() {
        unitUserRolesMaintenanceForm.btnAdd.requestFocusInWindow();
        dlgUnitUserRolesForm.setVisible(true);
    }
    
    /** 
     * Listens to action performed events.
     *@param actionEvent ActionEvent.
     */
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource().equals(unitUserRolesMaintenanceForm.btnAdd)){
            
            TreePath treePath = unitUserRolesTree.getSelectionPath();
            if( treePath != null){
                DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
                if(selNode.isRoot()){
                    unitUserRolesDetailsController = new UnitUserRolesDetailsController(userInfoBean,loggedInUser,vecUnitUserRolesData);
                    unitUserRolesDetailsController.loadUserRoles();
                    unitUserRolesDetailsController.display();
                    refreshTree();
                    unitUserRolesMaintenanceForm.btnAdd.setEnabled(true);
                    unitUserRolesMaintenanceForm.btnModify.setEnabled(true);
                    unitUserRolesMaintenanceForm.btnRemove.setEnabled(true);
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("unitUserRoleMaintenance_exceptionCode.1002"));//Please select the Root to add
                    return;
                }
            }else{                
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("unitUserRoleMaintenance_exceptionCode.1002"));//Please select the Root to add
                return;
            }
            
        }else if(e.getSource().equals(unitUserRolesMaintenanceForm.btnRemove)){
            
            TreePath treePath = unitUserRolesTree.getSelectionPath();
            DefaultTreeModel model = (DefaultTreeModel)unitUserRolesTree.getModel();
            if(treePath != null){
                DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
                Object selectedDeleteObject = selNode.getUserObject();
                if(selectedDeleteObject instanceof UnitUserRolesMaintenanceFormBean){
                    UnitUserRolesMaintenanceFormBean deleteUnitBean =(UnitUserRolesMaintenanceFormBean)selNode.getUserObject();
                    
                    if(!checkUserHasRight(loggedInUser,deleteUnitBean.getUnitNumber(),MAINTAIN_USER_ROLE_RIGHT)){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1015"));// Selected user has no right
                        return;
                    }else{
                        int option = CoeusOptionPane.showQuestionDialog("Do you want to remove all roles for the user "+userInfoBean.getUserName()+" \nfrom Department "+"'"+deleteUnitBean.getUnitNumber()+" : "+deleteUnitBean.getUnitName()+"' ?" ,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
                        if(option ==CoeusOptionPane.SELECTION_YES){
                            try {
                                removeRolesDept(selNode);
                                refreshTree();
                                unitUserRolesMaintenanceForm.btnAdd.setEnabled(true);
                                unitUserRolesMaintenanceForm.btnModify.setEnabled(true);
                                unitUserRolesMaintenanceForm.btnRemove.setEnabled(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }else if(selectedDeleteObject instanceof RoleInfoBean){
                    RoleInfoBean deleteRoleBean =(RoleInfoBean)selNode.getUserObject();
                    if(!checkUserHasRight(loggedInUser,deleteRoleBean.getUnitNumber(),MAINTAIN_USER_ROLE_RIGHT)){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1015"));// Selected user has no right
                        return;
                    }else{
                        int option = CoeusOptionPane.showQuestionDialog("Do you want to remove the role for the user "+userInfoBean.getUserName()+" \nfrom Department " +"'"+deleteRoleBean.getUnitNumber()+" : "+deleteRoleBean.getUnitName()+"' ?" ,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
                        if(option ==CoeusOptionPane.SELECTION_YES){
                            try {
                                removeRoles(selNode);
                                refreshTree();
                                unitUserRolesMaintenanceForm.btnAdd.setEnabled(true);
                                unitUserRolesMaintenanceForm.btnModify.setEnabled(true);
                                unitUserRolesMaintenanceForm.btnRemove.setEnabled(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            model.removeNodeFromParent(selNode);
                        }
                    }
                }
            }else{                
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("unitUserRoleMaintenance_exceptionCode.1001"));//Please select a Unit Node or a Role Node to remove
                return;
            }
            
        }else if(e.getSource().equals(unitUserRolesMaintenanceForm.btnModify)){
            TreePath treePath = unitUserRolesTree.getSelectionPath();
            if( treePath != null){
                DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
                Object selectedDeleteObject = selNode.getUserObject();
                if(selectedDeleteObject instanceof UnitUserRolesMaintenanceFormBean){
                    UnitUserRolesMaintenanceFormBean modifyingUnitBean =(UnitUserRolesMaintenanceFormBean)selNode.getUserObject();
                    if(!checkUserHasRight(loggedInUser,modifyingUnitBean.getUnitNumber(),MAINTAIN_USER_ROLE_RIGHT)){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1015"));// Selected user has no right
                        return;
                    }else{
                        unitUserRolesDetailsController = new UnitUserRolesDetailsController(userInfoBean,loggedInUser,vecUnitUserRolesData);
                        try {
                            unitUserRolesDetailsController.loadUserDetails(modifyingUnitBean.getUnitNumber());
                            unitUserRolesDetailsController.display();
                            refreshTree();
                            unitUserRolesMaintenanceForm.btnAdd.setEnabled(true);
                            unitUserRolesMaintenanceForm.btnModify.setEnabled(true);
                            unitUserRolesMaintenanceForm.btnRemove.setEnabled(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("unitUserRoleMaintenance_exceptionCode.1000"));//Please select a Unit Node to modify
                return;
            }        
        }else if(e.getSource().equals(unitUserRolesMaintenanceForm.btnClose)){
            dlgUnitUserRolesForm.dispose();
        }
    }
    
    /**
     * Removes roles a particular department along with assigned roles
     *@param DefaultMutableTreeNode
     *@exception throws Exception
     */
    public void removeRolesDept(DefaultMutableTreeNode selNode) throws Exception{
        Object selectedDeleteObject = selNode.getUserObject();
        if(selectedDeleteObject instanceof UnitUserRolesMaintenanceFormBean){
            UnitUserRolesMaintenanceFormBean deleteUnitBean =(UnitUserRolesMaintenanceFormBean)selNode.getUserObject();
            deleteUnitBean.setAcType(TypeConstants.DELETE_RECORD);
            Vector vecRoles = (Vector)deleteUnitBean.getVecRoles();
            if( vecRoles != null && vecRoles.size() > 0){
                for(Object roleObject : vecRoles){
                    RoleInfoBean removeRoleBean = (RoleInfoBean)roleObject;
                    removeRoleBean.setAcType(TypeConstants.DELETE_RECORD);
                }
                RequesterBean requesterBean = new RequesterBean();
                requesterBean.setFunctionType(DELETE_USER_ROLES);
                requesterBean.setDataObject(vecRoles);
                ResponderBean response = sendToServer(USER_ROLES_SERVLET, requesterBean);
                if(!response.isSuccessfulResponse()){
                    throw new Exception(response.getMessage());
                }
            }
        }
    }
    
    /**
     * Removes the selected Role from a department
     *@param DefaultMutableTreeNode
     *@exception throws Exception
     */
    public void removeRoles(DefaultMutableTreeNode selNode) throws Exception{
        Object selectedDeleteObject = selNode.getUserObject();
        if(selectedDeleteObject instanceof RoleInfoBean){
            RoleInfoBean deleteRoleBean =(RoleInfoBean)selNode.getUserObject();
            deleteRoleBean.setAcType(TypeConstants.DELETE_RECORD);
            Vector vecRoles = new Vector();
            vecRoles.add(deleteRoleBean);
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(DELETE_USER_ROLES);
            requesterBean.setDataObject(vecRoles);
            ResponderBean response = sendToServer(USER_ROLES_SERVLET, requesterBean);
            if(!response.isSuccessfulResponse()){
                throw new Exception(response.getMessage());
            }
        }
    }
    
    /**
     * Check if given user has MAINTAIN_USER_ROLE right at the given unit
     *@param loggedInUser
     *@param unitCode
     *@param right
     *@return boolean, is true if the user has right
     */
    private boolean checkUserHasRight(String loggedInUser,String unitCode,String right){
        boolean hasRight = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_USER_HAS_RIGHT);
        Vector vecRequest = new Vector();
        vecRequest.add(loggedInUser);
        vecRequest.add(unitCode);
        vecRequest.add(right);
        requesterBean.setDataObjects(vecRequest);
        ResponderBean response;
        try {
            response = sendToServer(USER_ROLES_SERVLET, requesterBean);
            if(!response.isSuccessfulResponse()){
                throw new Exception(response.getMessage());
            }else{
                if(response.getDataObject() != null){
                    hasRight = new Boolean(response.getDataObject().toString()).booleanValue();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return hasRight;
    }
    
    /**
     * Overridden method for the <code>TreeselectionListener</code>
     * @param e TreeSelectionEvent
     */
    public void valueChanged(TreeSelectionEvent e) {
        TreePath selectedTreePath = e.getNewLeadSelectionPath();
        if (selectedTreePath == null) {
            return;
        }
        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)selectedTreePath.getLastPathComponent();
        Object selectedObject = selNode.getUserObject();
        if(selNode.isRoot()){
            unitUserRolesMaintenanceForm.btnAdd.setEnabled(true);
            unitUserRolesMaintenanceForm.btnModify.setEnabled(false);
            unitUserRolesMaintenanceForm.btnRemove.setEnabled(false);
        }else if( selectedObject instanceof UnitUserRolesMaintenanceFormBean ){
            unitUserRolesMaintenanceForm.btnModify.setEnabled(true);
            unitUserRolesMaintenanceForm.btnRemove.setEnabled(true);
            unitUserRolesMaintenanceForm.btnAdd.setEnabled(false);
        }else if( selectedObject instanceof RoleInfoBean ){
            unitUserRolesMaintenanceForm.btnRemove.setEnabled(true);
            unitUserRolesMaintenanceForm.btnModify.setEnabled(false);
            unitUserRolesMaintenanceForm.btnAdd.setEnabled(false);
        }
    }
    
    /**
     * Refresh the contents of the JTree
     */
    public void refreshTree(){
        try {
            setFormData(null);
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        treeRoot.removeAllChildren();
        createUserRolesTree();
        DefaultTreeModel model = (DefaultTreeModel)unitUserRolesTree.getModel();
        model.reload();
        for (int i = 0; i < unitUserRolesTree.getRowCount(); i++) {
            unitUserRolesTree.expandRow(i);
        }
    }
    
    public Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return new Object();//change
    }
    
    public void formatFields() {
    }
    
    public boolean validate() throws CoeusUIException {
        return true;// change
    }
    
    public void saveFormData() throws CoeusException {
    }
    
    public void treeNodesChanged(TreeModelEvent e) {
    }
    
    public void treeNodesInserted(TreeModelEvent e) {
    }
    
    public void treeNodesRemoved(TreeModelEvent e) {
    }
    
    public void treeStructureChanged(TreeModelEvent e) {
    }
    
}
