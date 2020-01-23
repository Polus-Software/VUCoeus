/*
 * @(#)UserMaintenanceBaseWindow.java 1.0 July 9, 2003, 5:28 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 09-AUG-2007
 * by Leena
 */
package edu.mit.coeus.user.gui;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.user.controller.AddRoleController;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.user.gui.ResetPasswordForm;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.user.gui.UserRolesMaintenanceForm;
import edu.mit.coeus.utils.tree.UserRoleNodeRenderer;
import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintRoleBean;
import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.user.gui.UserPreferencesForm;

//import edu.mit.coeus.bean.RoleInfoBean;

import edu.mit.coeus.gui.CoeusMessageResources;
//import edu.mit.coeus.utils.roles.UserRolesMaintenance;
import edu.mit.coeus.utils.roles.RightsForRoleForm;
import edu.mit.coeus.utils.tree.UserRoleNode;
//import edu.mit.coeus.utils.tree.TransferableUserRoleData;
//import edu.mit.coeus.utils.tree.UserRoleNode;
import edu.mit.coeus.utils.CoeusOptionPane;
//import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.user.gui.UserRole;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.user.bean.UserRolesController;
import edu.mit.coeus.utils.TableSorter;
import java.awt.Cursor;
//import org.okip.service.shared.api.Factory;
//import edu.mit.is.service.authorization.Person;

import javax.swing.JToolBar;
//import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.*;
import java.util.*;
//import javax.swing.tree.TreePath;
import javax.swing.tree.*;
import java.beans.*;
import javax.swing.JInternalFrame;

/**
 *
 * @author  senthilar
 */
public class UserMaintenanceBaseWindow extends CoeusInternalFrame
        implements ActionListener, TypeConstants, DataPositions,MouseListener,
        VetoableChangeListener{
    private CoeusInternalFrame dlgRoles;
    
    private final String SEPERATOR="seperator";
    
    private CoeusAppletMDIForm mdiForm = null;
    
    private CoeusMenu editMenu, viewMenu, toolsMenu;
    
    //Modified for Coeus 4.3 PT ID 2232 - Custom Roles - added the toolbar buttons
    //createRoleToolBar and modifyRoleToolBar
    private CoeusToolBarButton newUserToolBar, userDetailsToolBar,
            roleRightsToolBar, searchUsersToolBar, saveToolBar, closeToolBar,
            createRoleToolBar, modifyRoleToolBar;
    
    private CoeusMenuItem createUserMenuItem, 
            /*delegationsMenuItem, Commented for Case#3682 - Enhancements related to Delegations*/
            moveUserMenuItem, userPreferencesMenuItem;
    
    //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
    private CoeusMenuItem createRoleMenuItem, modifyRoleMenuItem;
    //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
    
    private CoeusMenuItem roleRightsMenuItem, userDetailsMenuItem, assignedRolesMenuItem;
    
    private CoeusMenuItem searchMenuItem;
    
    private CoeusMessageResources coeusMessageResources;
    
    private edu.mit.coeus.utils.tree.UserRoleTree rolesTree;
    
//    private int unitNumber = 0;
    
    private String userID;
    
    private String command = "UserTableData";
    
    private String module = "user";
    
    private char functionType = 'D';
    
    private Vector dataObjects = null;
    
    private Vector userInfoLists = null;
    
    private Vector userRoleInfoLists = null;
    
    private Vector propUserRoles = new Vector();
    
//    private Vector propDefaultUsers = new Vector();
    
    private Vector userRoles = new Vector();
    
    private HashMap hmUsers = new HashMap();
    private TableSorter sorter;
    
    private UserRolesMaintenanceForm rolesForm;
    
    //private static boolean isCalledFromMenu = false;
//    private boolean showDescend;
    
    private UserPreferencesForm userPreferencesForm = null;
    private UserDelegationForm userDelegationForm = null;
    private UserDetailsForm userDetailsForm = null;
//    private RightsForRoleForm rightsForm =null;
    private UserRole userRole = null;
    private UserMovementForm userMovementForm = null;
    
    private CoeusSearch coeusSearch = null;
    
    private Vector vecUserBean = new Vector();
    
    private static final String SELECT_ONE_USER = "Please Select a User";
    private static final String SELECT_ONLY_ONE_USER = "Select One and Only One User";
    private static final String SAVE_CHANGES = "Do you want to save the changes ? ";
    
    private Hashtable hashTable = new Hashtable();
    
    private String selUnitNumber, selUnitName;
    
    //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
    private CoeusMenuItem resetPasswordMenuItem;
    private ResetPasswordForm resetPwd = null;
    private static final String UTILITY_SERVLET = "/UtilityServlet";
    private static final char GET_USER_RIGHTS = 'F';
    private CoeusToolBarButton resetToolBar;
    //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
    
    /** Creates a new instance of UserMaintenanceBaseWindow */
    public UserMaintenanceBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        
        //isCalledFromMenu = true;
        userID = mdiForm.getUserName();
        // moved by ravi from showDetails() - START
        hashTable.put(UNIT_NUMBER, mdiForm.getUnitNumber());
        // moved by ravi - END
        
        selUnitName = mdiForm.getUnitName();
        selUnitNumber = mdiForm.getUnitNumber();
        
        setViewMenu();
        //added by sharath
        //this.setDefaultCloseOperation(CoeusInternalFrame.DO_NOTHING_ON_CLOSE);
        // This will catch the window closing event and
        //  checks any data is modified.If any changes are done by
        // the user the system will ask for confirmation of Saving the info.
        this.addVetoableChangeListener(this);
        //added by sharath
        
        try {
            showUserDetails();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    /** Creates a new instance of UserMaintenanceBaseWindow */
    public UserMaintenanceBaseWindow(String title, String unitNumber, String unitName) {
        super(title, CoeusGuiConstants.getMDIForm());
        this.mdiForm = CoeusGuiConstants.getMDIForm();
        
        //isCalledFromMenu = true;
        userID = mdiForm.getUserName();
        hashTable.put(UNIT_NUMBER, unitNumber);
        this.selUnitNumber = unitNumber;
        this.selUnitName = unitName;
        
        setViewMenu();
        //added by sharath
        //this.setDefaultCloseOperation(CoeusInternalFrame.DO_NOTHING_ON_CLOSE);
        //added by sharath
        
        try {
            showUserDetails();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    private void setViewMenu() {
        roleRightsMenuItem = new CoeusMenuItem(" Rights ", null, true, true);
        roleRightsMenuItem.setMnemonic('r');
        roleRightsMenuItem.addActionListener(this);
        
        userDetailsMenuItem = new CoeusMenuItem(" User details ", null, true, true);
        userDetailsMenuItem.setMnemonic('u');
        userDetailsMenuItem.addActionListener(this);
        
        assignedRolesMenuItem = new CoeusMenuItem(" Assigned Roles ", null, true, true);
        assignedRolesMenuItem.setMnemonic('a');
        assignedRolesMenuItem.addActionListener(this);
        
        viewMenu = new CoeusMenu("View");
        viewMenu.setMnemonic('v');
        viewMenu.add(roleRightsMenuItem);
        viewMenu.add(userDetailsMenuItem);
        viewMenu.add(assignedRolesMenuItem);
        viewMenu.setEnabled(true);
        viewMenu.setVisible(true);
        
        setMenu(viewMenu,2);
        
    }
    public CoeusMenu getToolsMenu() {
        toolsMenu = new CoeusMenu("Tools");
        toolsMenu.setMnemonic('t');
        searchMenuItem = new CoeusMenuItem(" Search ", null, true, true);
        searchMenuItem.addActionListener(this);
        toolsMenu.add(searchMenuItem);
        toolsMenu.setEnabled(true);
        toolsMenu.setVisible(true);
        return toolsMenu;
    }
    
    private boolean connectToServlet(java.lang.String servletName) {
        dataObjects = new Vector();
        //Hashtable hashTable = new Hashtable();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.USER_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setUserName(mdiForm.getUserName());
        //hashTable.put(UNIT_NUMBER, "000001");
        //hashTable.put(COMMAND, command);
        //hashTable.put(MODULE, module);
        //hashTable.put(FUNCTION, new Character(functionType));
        //hashTable.put(PERSON_ID, userID);
        dataObjects.add(hashTable);
        request.setDataObjects(dataObjects);
//        System.out.println("Before calling the Servlet.");
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
//        System.out.println("After calling the Servlet.");
        if (response!=null){
            if (response.isSuccessfulResponse()){
                dataObjects = (Vector)response.getDataObjects();
            }
        }
        
        if (dataObjects == null) {
//            System.out.println("DataObjects from the Server returned null");
            return false;
        } else {
            /*
            userInfoLists = (Vector)dataObjects.get(0);
            userRoleInfoLists = (Vector)dataObjects.get(1);
             */
            return true;
        }
    }
    
    private CoeusMenu getEditMenu() {
        
        Vector editMenuItems = new Vector();
        
        createUserMenuItem = new CoeusMenuItem(" Create New User ", null, true, true);
        createUserMenuItem.addActionListener(this);
        
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
        createRoleMenuItem = new CoeusMenuItem(" Create New Role ", null, true, true);
        createRoleMenuItem.setMnemonic('r');
        createRoleMenuItem.addActionListener(this);
        
        modifyRoleMenuItem = new CoeusMenuItem(" Modify Role ", null, true, true);
        modifyRoleMenuItem.setMnemonic('m');
        modifyRoleMenuItem.addActionListener(this);
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
        
        /*Commented for Case#3682 - Enhancements related to Delegations - Start
        delegationsMenuItem = new CoeusMenuItem(" Delegations... ", null, true, true);
        delegationsMenuItem.addActionListener(this);
         Commented for Case#3682 - Enhancements related to Delegations - end
        */        
        moveUserMenuItem = new CoeusMenuItem(" Move User ", null, true, true);
        moveUserMenuItem.addActionListener(this);
        
        userPreferencesMenuItem = new CoeusMenuItem(" User Preferences... ", null, true, true);
        userPreferencesMenuItem.addActionListener(this);
        
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
        resetPasswordMenuItem = new CoeusMenuItem(" Reset Password ", null, true, true);
        resetPasswordMenuItem.setMnemonic('s');
        resetPasswordMenuItem.addActionListener(this);
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
        
        editMenuItems.add(createUserMenuItem);
        //Commented for Case#3682 - Enhancements related to Delegations - Start
        //editMenuItems.add(delegationsMenuItem);
        //Commented for Case#3682 - Enhancements related to Delegations - End
        editMenuItems.add(moveUserMenuItem);
        editMenuItems.add(userPreferencesMenuItem);
        editMenuItems.add(resetPasswordMenuItem);
        editMenuItems.add(SEPERATOR);
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
        editMenuItems.add(createRoleMenuItem);
        editMenuItems.add(modifyRoleMenuItem);
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
        
        editMenu = new CoeusMenu("Edit", null, editMenuItems, true, true);
        editMenu.setMnemonic('e');
        
        return editMenu;
    }
    
/*    private CoeusMenu getViewMenu() {
        Vector viewMenuItems = new Vector();
 
        roleRightsMenuItem = new CoeusMenuItem(" Rights ", null, true, true);
        roleRightsMenuItem.addActionListener(this);
 
        userDetailsMenuItem = new CoeusMenuItem(" User details ", null, true, true);
        userDetailsMenuItem.addActionListener(this);
 
        assignedRolesMenuItem = new CoeusMenuItem(" Assigned Roles ", null, true, true);
        assignedRolesMenuItem.addActionListener(this);
 
        viewMenuItems.add(roleRightsMenuItem);
        viewMenuItems.add(userDetailsMenuItem);
        viewMenuItems.add(assignedRolesMenuItem);
 
        viewMenu = new CoeusMenu("View", null, viewMenuItems, true, true);
        return viewMenu;
    }*/
    
    private JToolBar getToolBar() {
        JToolBar toolbar = new JToolBar();
        
        newUserToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
                null, "Add a new user");
        newUserToolBar.addActionListener(this);
        
        userDetailsToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DETAILS_ICON)),
                null, "User Details");
        userDetailsToolBar.addActionListener(this);
        
        roleRightsToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ROLE_RIGHTS_ICON)),
                null, "Role Rights");
        roleRightsToolBar.addActionListener(this);
        
        searchUsersToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
                null, "Search for Users");
        searchUsersToolBar.addActionListener(this);
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
        createRoleToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CREATE_ROLE_ICON)),
                null, "Create Role");
        createRoleToolBar.addActionListener(this);
        
        modifyRoleToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.MODIFY_ROLE_ICON)),
                null, "Modify Role");
        modifyRoleToolBar.addActionListener(this);
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
        
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
        resetToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.RESET_ICON)),
                null, "Reset Password");
        resetToolBar.addActionListener(this);
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
        
        saveToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
                null, "Save");
        saveToolBar.addActionListener(this);
        
        closeToolBar = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
                null, "Close");
        closeToolBar.addActionListener(this);
        
        toolbar.add(newUserToolBar);
        toolbar.add(userDetailsToolBar);
        toolbar.add(roleRightsToolBar);
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
        toolbar.add(resetToolBar);
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
        toolbar.addSeparator();
        
        toolbar.add(searchUsersToolBar);
        toolbar.add(saveToolBar);
        toolbar.addSeparator();
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
        toolbar.add(createRoleToolBar);
        toolbar.add(modifyRoleToolBar);
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
        toolbar.addSeparator();
        toolbar.add(closeToolBar);
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    public void showUserDetails() {
        
        //hashTable.put(UNIT_NUMBER, "000001");
        hashTable.put(COMMAND, command);
        hashTable.put(MODULE, module);
        hashTable.put(FUNCTION, new Character(functionType));
        hashTable.put(PERSON_ID, userID);
        
        if (connectToServlet(CoeusGuiConstants.USER_SERVLET)) {
            userInfoLists = (Vector)dataObjects.get(0);
            userRoleInfoLists = (Vector)dataObjects.get(1);
        }else {
//            System.out.println("Problem in the response from the server.");
        }
        
        //added by sharath
        if(userInfoLists != null){
            UserInfoBean userInfoBean;
            for(int index = 0; index < userInfoLists.size(); index++) {
                userInfoBean = (UserInfoBean)userInfoLists.get(index);
                hmUsers.put(userInfoBean.getUserId(), userInfoBean);
            }
        }
        //added by sharath
        
        formatData();
        rolesForm = new UserRolesMaintenanceForm(userRoles);
        
        UserRoleNodeRenderer nodeRenderer = new UserRoleNodeRenderer(true);
        
        //if (isCalledFromMenu) {
        //    isCalledFromMenu = false;
        try{
            setTitle( "User Maintenance for " + selUnitNumber +" : "+ selUnitName );
            setFrameMenu(getEditMenu());
            
            //setFrameMenu(getViewMenu());
            setFrameToolBar(this.getToolBar());
            setFrameIcon(mdiForm.getCoeusIcon());
            setFrame(CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE);
            mdiForm.putFrame(CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE,this );
            getContentPane().add( rolesForm.showUserRolesMaintenance( dlgRoles ) );
            mdiForm.getDeskTopPane().add(this);
            rolesTree = rolesForm.getRolesTree();
            rolesTree.setCellRenderer(nodeRenderer);
            rolesTree.setRowHeight(16);
            UserRoleNode node = (UserRoleNode)((UserRoleNode)rolesTree.getModel().getRoot()).getFirstChild();
            while(node!=null){
                TreePath nodePath = new TreePath(node);
                rolesTree.collapsePath(nodePath);
                node = (UserRoleNode)node.getNextSibling();
            }
            ( ( DefaultTreeModel )rolesTree.getModel() ).reload();
            
            //added by sharath(7-Aug-2003)
            rolesForm.getUserRolesTable().addMouseListener(this);
            if(userDetailsForm == null){
                userDetailsForm = new UserDetailsForm(mdiForm,true,selUnitNumber,selUnitName);
            }
            newUserToolBar.setEnabled(userDetailsForm.canAddUser());
            createUserMenuItem.setEnabled(userDetailsForm.canAddUser());
            
            //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
            AddRoleController addRoleController = new AddRoleController(selUnitNumber, selUnitName);
            boolean canUserMaintainRole = addRoleController.canUserMaintainRole();
            createRoleMenuItem.setEnabled(canUserMaintainRole);
            modifyRoleMenuItem.setEnabled(canUserMaintainRole);
            //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
            // Added for Case 4414 - Disappearing role - Start
            modifyRoleToolBar.setEnabled(canUserMaintainRole);
            createRoleToolBar.setEnabled(canUserMaintainRole);
            // Added for Case 4414 - Disappearing role - End
            
            
            UserRolesController userRolesController = new UserRolesController();
            boolean proposalAccessRights = userRolesController.getProposalAccessRights(mdiForm.getUserId(), selUnitNumber);
            if( rolesForm.getUserRolesTable().getModel() instanceof TableSorter ) {
                sorter = (TableSorter)rolesForm.getUserRolesTable().getModel();
                sorter.sortByColumn(1);
            }
            //added by sharath
            this.setSelected(true);
            this.setVisible(true);
            
            //Added By sharath
            searchMenuItem.setEnabled(userDetailsForm.canAddUser());
            searchUsersToolBar.setEnabled(userDetailsForm.canAddUser());
            //Added By sharath
            
        } catch(java.lang.Exception ex){
            //ex.printStackTrace();
//            System.out.println("Exception raised while trying to show the UserMaintenanceForm");
            //Case 3287 - START
            mdiForm.removeFrame(title);
            super.doDefaultCloseAction();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
            ex.printStackTrace();
            //Case 3287 - END
        }
        //}
    }
    
    public void formatData(){
        Vector vecUserInfoBeans = (Vector)dataObjects.get(0);
        Vector vecUserRoleInfoBeans = (Vector)dataObjects.get(1);
        
        if( vecUserRoleInfoBeans != null ) {
            int size = vecUserRoleInfoBeans.size();
            for( int indx = 0; indx < size; indx++ ) {
                UserRolesInfoBean tempBean = new UserRolesInfoBean();
                UserRolesInfoBean existBean = (UserRolesInfoBean) vecUserRoleInfoBeans.elementAt(indx );
                tempBean.setHasChildren( existBean.hasChildren() );
                tempBean.setIsRole( existBean.isRole() );
                tempBean.setRoleBean( existBean.getRoleBean() );
                tempBean.setUserBean( existBean.getUserBean() );
                tempBean.setUsers( existBean.getUsers() );
                propUserRoles.addElement( tempBean );
            }
        }
        
        if ( functionType == DISPLAY_MODE ) {
            userRoles.removeAllElements();
            userRoles.addElement( vecUserInfoBeans );
            //userRoles.addElement( propUserRoles );
            userRoles.addElement( vecUserRoleInfoBeans );
        }
    }
    
    /** This abstract method must be implemented by all classes which inherits this class.
     * Used for saving the current activesheet when clicked Save from File menu.
     */
    public void saveActiveSheet() {
//        System.out.println("Entering saveActiveSheet....");
        Vector addedUsers = null;
        Vector deletedUsers = null;
        
        try{
            addedUsers = rolesForm.getAddedUsers();
        } catch(NullPointerException npe){
            addedUsers = new Vector();
        }
        try{
            deletedUsers = rolesForm.getRemovedUsers();
        } catch(NullPointerException npe){
            deletedUsers = new Vector();
        }
        
        
        
        //Vector vecUserBean = new Vector();
        int count = addedUsers.size();
//        UserMaintRoleBean roleBean = null;
//        UserRolesInfoBean userRoleInfoBean = null;
//        RoleInfoBean roleInfoBean = null;
//        UserInfoBean userInfoBean = null;
        /*
        for (int index=0; index<count; index++){
            roleBean = new UserMaintRoleBean();
            userRoleInfoBean = (UserRolesInfoBean)addedUsers.get(index);
            roleInfoBean = userRoleInfoBean.getRoleBean();
            userInfoBean = userRoleInfoBean.getUserBean();
            roleBean.setAcType("I");
            roleBean.setUserId(userInfoBean.getUserId());
            roleBean.setDescendFlag(roleInfoBean.isDescend()? 'Y':'N');
            roleBean.setRoleId(roleInfoBean.getRoleId());
            roleBean.setUnitNumber(userInfoBean.getUnitNumber());
            roleBean.setUpdateUser(userID);
            vecUserBean.addElement(roleBean);
        }
         
        count = deletedUsers.size();
        for (int index=0; index<count; index++){
            roleBean = new UserMaintRoleBean();
            userRoleInfoBean = (UserRolesInfoBean)deletedUsers.get(index);
            roleInfoBean = userRoleInfoBean.getRoleBean();
            userInfoBean = userRoleInfoBean.getUserBean();
            roleBean.setAcType("D");
            System.out.println("Deleting the UserMaintRoleBean");
            roleBean.setDescendFlag(roleInfoBean.isDescend()? 'Y':'N');
            roleBean.setRoleId(roleInfoBean.getRoleId());
            roleBean.setUnitNumber(userInfoBean.getUnitNumber());
            roleBean.setUserId(userInfoBean.getUserId());
            roleBean.setUpdateUser(userID);
            System.out.println("userInfoBean.getUpdateTimestamp() = " + userInfoBean.getUpdateTimestamp());
            roleBean.setUpdateTimestamp(userInfoBean.getUpdateTimestamp());
            if (!alreadyAvailable(roleBean)) {
                vecUserBean.addElement(roleBean);
            }
        }
         */
        
        //if (vecUserBean.size() > 0){
        //hashTable.put(UNIT_NUMBER, "000001");
        hashTable.put(UNIT_NUMBER, selUnitNumber);
        hashTable.put(COMMAND, command);
        hashTable.put(MODULE, module);
        hashTable.put(FUNCTION, new Character('U'));
        hashTable.put(PERSON_ID, userID);
        hashTable.put("DATA", vecUserBean);
//        System.out.println("The numner of beans passed = " + vecUserBean.size());
        count = vecUserBean.size();
        UserMaintRoleBean tempBean = null;
//        for (int index = 0 ; index < count; index++) {
//            tempBean = (UserMaintRoleBean)vecUserBean.get(index);
//            System.out.println("UserID = " + tempBean.getUserId());
//            System.out.println("Act Type = " + tempBean.getAcType());
//        }
        
        //Added by sharath
        
        //Removes Duplicate Users in Same Mode(i.e Add or Delete);
        /*
         Below Code is Commented coz it removes users if user is Added - Deleted - Added.
         user exists once in Add and Once in Delete and finally the User is Removed
         from both vectors(Add, Delete) since he is added and Deleted.
         */
        //removeDuplicateUsers(addedUsers);
        //removeDuplicateUsers(deletedUsers);
        
        //If Deleted - Add Then No Need to Add
        //Deleted - Added Data Contains TimeStamp. Remove Them
        UserRolesInfoBean userRolesInfoBean;
        for(int index = 0; index < addedUsers.size(); index++) {
            userRolesInfoBean = (UserRolesInfoBean)addedUsers.get(index);
            if(userRolesInfoBean.getUserBean().getUpdateTimestamp() != null) {
                addedUsers.remove(index);
            }
        }
        
        
        //removes users if same user is added and deleted.
        Vector data = new Vector();
        if(addedUsers != null && deletedUsers != null) {
//            int sizeAdd = addedUsers.size();
//            int sizeDel = deletedUsers.size();
            UserRolesInfoBean rolesBeanAdd, rolesBeanDel;
            UserInfoBean userInfoBeanAdd, userInfoBeanDel;
            
            add:for(int addIndex = 0;addIndex < addedUsers.size(); ) {
                rolesBeanAdd = (UserRolesInfoBean)addedUsers.get(addIndex);
                userInfoBeanAdd = rolesBeanAdd.getUserBean();
                
                delete:for(int delIndex = 0; delIndex < deletedUsers.size(); delIndex++) {
                    rolesBeanDel = (UserRolesInfoBean)deletedUsers.get(delIndex);
                    userInfoBeanDel = rolesBeanDel.getUserBean();
                    if(userInfoBeanAdd.getUserId().equals(userInfoBeanDel.getUserId())) {
                        //Delete From Both
                        addedUsers.remove(addIndex);
                        deletedUsers.remove(delIndex);
                        continue add;
                    }
                }
                addIndex++;
            }
        }
        data.add(addedUsers);
        data.add(deletedUsers);
        hashTable.put(DataPositions.DATA, data);
        //Added By sharath
        
        if((addedUsers == null || addedUsers.size() == 0)
        && (deletedUsers == null || deletedUsers.size() == 0)){
            return ; //Nothing to Save
        }
        
        if (connectToServlet(CoeusGuiConstants.USER_SERVLET)){
//            System.out.println("Servlet connection went fine");
            vecUserBean.clear();
            // COEUSDEV-221	User Maintenance window does not save in one try - Start
//        }else
//            System.out.println("Servlet connection while saving records failed.");
            
            rolesForm.getAddedUsers().removeAllElements();
            rolesForm.getRemovedUsers().removeAllElements();
        }
        // COEUSDEV-221	User Maintenance window does not save in one try - End
        //}
        //This is to refresh the data
        
    }
    
    /*private void removeDuplicateUsers(Vector vecUserRolesInfoBean) {
        UserRolesInfoBean userRolesInfoBean;
        UserInfoBean userInfoBean;
        String userId;
        for(int count = 0; count < vecUserRolesInfoBean.size(); count++) {
            userRolesInfoBean = (UserRolesInfoBean)vecUserRolesInfoBean.get(count);
            userInfoBean = userRolesInfoBean.getUserBean();
            userId = userInfoBean.getUserId();
     
            for(int index = count + 1; index < vecUserRolesInfoBean.size(); ) {
                userRolesInfoBean = (UserRolesInfoBean)vecUserRolesInfoBean.get(index);
                userInfoBean = userRolesInfoBean.getUserBean();
                if(userId.equals(userInfoBean.getUserId())) {
                    vecUserRolesInfoBean.remove(index);
                    continue;
                }
                index++;
            }
        }
    }*///End Delete Duplicate Users
    
    /*
     * If the same record is for to ADD and DELETE then it dont need
     * to goto database. Those records can be ignored at the client
     * side itself.
     */
    public boolean alreadyAvailable(UserMaintRoleBean roleBean){
        int count = vecUserBean.size();
        UserMaintRoleBean tempBean = null;
        for (int index=0; index<count; index++){
            tempBean = (UserMaintRoleBean)vecUserBean.get(index);
            if (( (roleBean.getUserId()).equals(tempBean.getUserId()) ) &
                    ( roleBean.getRoleId()  == tempBean.getRoleId() ) &
                    ( (roleBean.getUnitNumber()).equals(tempBean.getUnitNumber()) ) ) {
//                System.out.println("The Record to add and delete is available. There details are below...");
//                System.out.println("Delete UserId = " + roleBean.getUserId());
//                System.out.println("Delete RoleId = " + roleBean.getRoleId());
//                System.out.println("Delete UnitNumber = " + roleBean.getUnitNumber());
//                System.out.println("");
//                System.out.println("Added UserId = " + tempBean.getUserId());
//                System.out.println("Added RoleId = " + tempBean.getRoleId());
//                System.out.println("Added UnitNumber = " + tempBean.getUnitNumber());
                vecUserBean.remove(index);
                return true;
            }
        }
//        System.out.println("No duplicates available ");
        return false;
    }
    
    /** Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object actSource = actionEvent.getSource();
        if (actSource.equals(userPreferencesMenuItem)) {
            int selectedNoOfRows = rolesForm.getSelectedNumberOfRows();
            if(selectedNoOfRows <1) {
                CoeusOptionPane.showInfoDialog(SELECT_ONE_USER);
                return ;
            }
            if (selectedNoOfRows > 1) {
                CoeusOptionPane.showInfoDialog(SELECT_ONLY_ONE_USER);
                return ;
            } else {
                //UserInfoBean userBean = rolesForm.getSelectedUser();
                //UserInfoBean userBean = (UserInfoBean)userInfoLists.get(rolesForm.getUserRolesTable().getSelectedRow());
                int selectedRow = rolesForm.getUserRolesTable().getSelectedRow();
                UserInfoBean userBean = (UserInfoBean)hmUsers.get(sorter.getValueAt(selectedRow, 1).toString());
                
                if(userPreferencesForm == null) {
                    userPreferencesForm = new UserPreferencesForm(mdiForm,true);
                }
                userPreferencesForm.loadUserPreferences(userBean.getUserId());
                userPreferencesForm.setUserName(userBean.getUserName());
                userPreferencesForm.display();
            }
        }//End Event Handling for Preferences Menu Item
        //Commented for Case#3682 - Enhancements related to Delegations - Start
        /*else if(actSource.equals(delegationsMenuItem)) {
            displayUserDelegation();
        }//End Event Handling for Delegations Menu Item*/
        //Commented for Case#3682 - Enhancements related to Delegations - End        
        else if(actSource.equals(createUserMenuItem) || actSource.equals(newUserToolBar)) {
            displayUserDetails();
        }//End Event Handling for Create User Menu Item
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
        else if(actSource.equals(createRoleMenuItem) || actSource.equals(createRoleToolBar)){
            coeusMessageResources = CoeusMessageResources.getInstance();
            AddRoleController addRoleController;
            try {
                addRoleController = new AddRoleController(selUnitNumber, selUnitName, null);
                addRoleController.display();
                if(addRoleController.isRefreshRequired()){
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    refresh();
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
        }else if(actSource.equals(modifyRoleMenuItem) || actSource.equals(modifyRoleToolBar)) {
            TreePath selectedPath = rolesTree.getSelectionPath();
            coeusMessageResources = CoeusMessageResources.getInstance();
            if( selectedPath != null ) {
                UserRoleNode selectedNode = (UserRoleNode)selectedPath.getLastPathComponent();
                UserRolesInfoBean userRoleBean = selectedNode.getDataObject();
                if( userRoleBean.isRole() ) {
                    RoleInfoBean roleBean = userRoleBean.getRoleBean();
                    AddRoleController addRoleController;
                    try {
                        int selectedRows[] = rolesTree.getSelectionRows();
                        int selectedRow = -1;
                        if(selectedRows.length>0){
                            selectedRow = selectedRows[0];
                        }
                        addRoleController = new AddRoleController(selUnitNumber, selUnitName, roleBean);
                        addRoleController.display();
                        if(addRoleController.isRefreshRequired()){
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            RoleInfoBean modifiedRoleBean = addRoleController.getRole();
                            roleBean.setRoleName(modifiedRoleBean.getRoleName());
                            roleBean.setStatus(modifiedRoleBean.getStatus());
                            roleBean.setDescend(modifiedRoleBean.isDescend());
                            ( ( DefaultTreeModel )rolesTree.getModel() ).reload();
                            rolesTree.setSelectionRow(selectedRow);
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    } catch (CoeusException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoRoles_exceptionCode.2600"));
                }
            }else {
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(
                        "protoRoles_exceptionCode.2600"));
            }
        }
        //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
        
        else if(actSource.equals(userDetailsMenuItem) || actSource.equals(userDetailsToolBar)) {
            displayUserDetails(rolesForm.getUserRolesTable().getSelectedRow());
        }//End Event Handling for UserDetails
        else if(actSource.equals(searchUsersToolBar) || actSource.equals(searchMenuItem)) {
            displayUserSearch();
        }//End Event Handling for User Search
        else if(actSource.equals(saveToolBar)) {
            saveActiveSheet();
        }//End Event Handling for Save
        else if(actSource.equals(closeToolBar)) {
            try{
                close(false);
            }catch (PropertyVetoException propertyVetoException) {
                //Don't need to do anything .
            }
        }//End Event Handling for close
        else if(actSource.equals(roleRightsToolBar) ||
                actSource.equals(roleRightsMenuItem)){
            
//            System.out.println("roleRightsMenuItem");
            TreePath selectedPath = rolesTree.getSelectionPath();
            coeusMessageResources = CoeusMessageResources.getInstance();
            
            if( selectedPath != null ) {
                UserRoleNode selectedNode = (UserRoleNode)selectedPath.getLastPathComponent();
                UserRolesInfoBean userRoleBean = selectedNode.getDataObject();
//                System.out.println("bean:"+userRoleBean);
                if( userRoleBean.isRole() ) {
                    RoleInfoBean roleBean = userRoleBean.getRoleBean();
                    RightsForRoleForm rightsForm
                            = new RightsForRoleForm(""+roleBean.getRoleId(),
                            roleBean.getRoleName());
//                    System.out.println("RightsForRoleForm constructed");
                }else{
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoRoles_exceptionCode.2600"));
                }
            }else {
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(
                        "protoRoles_exceptionCode.2600"));
            }
//            System.out.println("Exit roleRightsMenuItem Event");
            
            
            
        }//End Event Handling for RoleRight Details
        
        else if((actSource.equals(assignedRolesMenuItem))){
            
            int selectedRows = rolesForm.getSelectedNumberOfRows();
            if(selectedRows <1) {
                CoeusOptionPane.showInfoDialog(SELECT_ONE_USER);
                return ;
            }
            if (selectedRows > 1) {
                CoeusOptionPane.showInfoDialog(SELECT_ONLY_ONE_USER);
                return ;
            }
            //int selectedRowOnly = rolesForm.getUserRolesTable().getSelectedRow();
            //UserInfoBean userInfoBean = null;
            //userInfoBean = (UserInfoBean) rolesForm.getSelectedUser();
            //userInfoBean = (UserInfoBean)userInfoLists.get(selectedRowOnly);
            int selectedRow = rolesForm.getUserRolesTable().getSelectedRow();
            UserInfoBean userBean = (UserInfoBean)hmUsers.get(sorter.getValueAt(selectedRow, 1).toString());
            
            String userId = userBean.getUserId();
            String userName = userBean.getUserName();
            userRole = new UserRole(mdiForm,userId,userName,true);
            
            
        }//End Event Handling for User Roles
        else if(actSource.equals(moveUserMenuItem)) {
            displayMoveUser();
        }//End Event Handling for Move User
        
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
        else if(actSource.equals(resetPasswordMenuItem) || actSource.equals(resetToolBar)){
            coeusMessageResources = CoeusMessageResources.getInstance();
            int selectedRow = rolesForm.getUserRolesTable().getSelectedRow();
            int selectedNoOfRows = rolesForm.getSelectedNumberOfRows();
            if(selectedNoOfRows <1) {
                CoeusOptionPane.showInfoDialog(SELECT_ONE_USER);
                return ;
            }
            if (selectedNoOfRows > 1) {
                CoeusOptionPane.showInfoDialog(SELECT_ONLY_ONE_USER);
                return ;
            }
            UserInfoBean userBean = (UserInfoBean)hmUsers.get(sorter.getValueAt(selectedRow, 1).toString());
            String selectedUnitNumber = (String)sorter.getValueAt(selectedRow, 3).toString();
            resetPwd = new ResetPasswordForm();
            resetPwd.setUserId(userBean.getUserId());
            resetPwd.setUserName(userBean.getUserName());
            resetPwd.setMessages();
            if(hasResetPasswordRights(selectedUnitNumber)){
                displayResetPassword();
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("resetPasswordHelpCode.1002"));
                return;
            }
        }
        //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
    }
    
    //Commented for Case#3682 - Enhancements related to Delegations - Start
    /*
    private void displayUserDelegation() {
        if(userDelegationForm == null) {
            userDelegationForm = new UserDelegationForm(mdiForm,true);
        }
        userDelegationForm.display();
    }
    */
    //Commented for Case#3682 - Enhancements related to Delegations - End   
    
    private void displayUserDetails() {
        //Added so as to display recent modifications
        saveActiveSheet();
        
        if(userDetailsForm == null){
            userDetailsForm = new UserDetailsForm(mdiForm,true,selUnitNumber,selUnitName);
        }
        userDetailsForm.reset();
        userDetailsForm.loadUserRoles();
        userDetailsForm.display();
        if(userDetailsForm.isUserAdded()) {
            UserInfoBean userInfoBean = userDetailsForm.getAddedUser();
            addUser(userInfoBean);
            // Added by chandra: Once a new user is added with few roles from
            // Unit Hierarchy, it sholud refresh the table and tree data.
            // The User should visible under the assigned roles - 20 Sept 2004 - start
            refresh();
            // End - 20-Sept-2004
        }
    }
    
    private void displayUserDetails(int selectedRow) {
        //Added so as to display recent modifications
        saveActiveSheet();
        
        int selectedNoOfRows = rolesForm.getSelectedNumberOfRows();
        if(selectedNoOfRows <1) {
            CoeusOptionPane.showInfoDialog(SELECT_ONE_USER);
            return ;
        }
        if (selectedNoOfRows > 1) {
            CoeusOptionPane.showInfoDialog(SELECT_ONLY_ONE_USER);
            return ;
        }
        //int selectedRow = rolesForm.getUserRolesTable().getSelectedRow();
        
        //UserInfoBean userInfoBean = (UserInfoBean)userInfoLists.get(selectedRow);//rolesForm.getSelectedUser();
        selectedRow = rolesForm.getUserRolesTable().getSelectedRow();
        String userId  = "";
        UserInfoBean userInfoBean = null;
        // Bug Fix #1699
        // If the user doesn't belongs to the existing unit, then
        // get the user info through the selected row information - start
        userInfoBean = (UserInfoBean)hmUsers.get(sorter.getValueAt(selectedRow, 1).toString());
        if(userInfoBean==null){
            userInfoBean = new UserInfoBean();
            userId = (String)rolesForm.getUserRolesTable().getValueAt(selectedRow, 1);
            userInfoBean.setUserId(userId);
        }else{
            userId = userInfoBean.getUserId();
        } // Bug Fix #1699 - End
        
        if(userDetailsForm == null){
            userDetailsForm = new UserDetailsForm(mdiForm,true,selUnitNumber,selUnitName);
        }
        userDetailsForm.reset();
        userDetailsForm.loadUserDetails(userInfoBean);
        userDetailsForm.display();
        if(userDetailsForm.isUserModified()) {
            userInfoBean = userDetailsForm.getModifiedUser();
            modifyUser(userInfoBean);
            refresh();
        }
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if(source.equals(rolesForm.getUserRolesTable())) {
            if(mouseEvent.getClickCount() == 2) { //Double Click
                displayUserDetails(rolesForm.getUserRolesTable().getSelectedRow());
            }
        }
    }
    
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    public void mouseExited(MouseEvent mouseEvent) {
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
    }
    
    private void displayUserSearch() {
        try{
            if(coeusSearch == null){
                coeusSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "userSearch",CoeusSearch.TWO_TABS);
            }
            coeusSearch.showSearchWindow();
            //int selectedRow = rolesForm.getUserRolesTable().getSelectedRow();
            //UserInfoBean userInfoBean = (UserInfoBean)userInfoLists.get(selectedRow);//rolesForm.getSelectedUser();
            //String userId = userInfoBean.getUserId();
            HashMap hashMap = coeusSearch.getSelectedRow();
            if(hashMap == null){
                return ;
            }
            UserRolesController userRolesController = new UserRolesController();
            UserInfoBean userInfoBean = userRolesController.getUser(hashMap.get("USER_ID").toString());
            if(userInfoBean.getUserName() == null) {
                userInfoBean.setUserName("");
            }
            if(rolesForm.userExists(userInfoBean.getUserId())) {
                return ;
            } else {
                //Does Not Exists
                addUser(userInfoBean);
            }
//            System.out.println("User Details Got");
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }
    
    public void displayMoveUser() {
        int selectedNoOfRows = rolesForm.getSelectedNumberOfRows();
        if(selectedNoOfRows <1) {
            CoeusOptionPane.showInfoDialog(SELECT_ONE_USER);
            return ;
        }
        if (selectedNoOfRows > 1) {
            CoeusOptionPane.showInfoDialog(SELECT_ONLY_ONE_USER);
            return ;
        }
        
        int selectedRow = rolesForm.getUserRolesTable().getSelectedRow();
        //UserInfoBean userInfoBean = (UserInfoBean)userInfoLists.get(selectedRow);
        UserInfoBean userInfoBean = (UserInfoBean)hmUsers.get(sorter.getValueAt(selectedRow, 1).toString());
        
        
        //Check For User Rights.
        /*if(! (userDetailsForm.canAddUser() && userDetailsForm.canModifyUser())) {
            CoeusOptionPane.showInfoDialog("you can not move this user from unit "+userInfoBean.getUnitNumber()+" to another");
            return ;
        }*/
        
        if(userMovementForm == null) {
            userMovementForm = new UserMovementForm(mdiForm, true);
            userMovementForm.setAddUser(userDetailsForm.canAddUser());
            userMovementForm.setMaintainUser(userDetailsForm.canMaintainUser());
        }
        
        userMovementForm.display(userInfoBean);
        
        if(userMovementForm.isUserMoved()) {
            userInfoBean = userMovementForm.getMovedUser();
            modifyUser(userInfoBean);
        }
    }
    
    private void modifyUser(UserInfoBean userInfoBean) {
        rolesForm.modifyUser(userInfoBean);
        
    }
    
    private void addUser(UserInfoBean userInfoBean) {
        rolesForm.addUser(userInfoBean);
        if( rolesForm.getUserRolesTable().getModel() instanceof TableSorter ) {
            sorter = ( TableSorter ) rolesForm.getUserRolesTable().getModel();
        }
        //int location = rolesForm.getUserRolesTable().getUserRowInTable(userInfoBean);
        //userInfoLists.add(userInfoBean);
        /*int rowIndex, rowCount = userInfoLists.size();
        String userId;
         
        for(rowIndex =0; rowIndex < rowCount; rowIndex++){
            userId = ((UserInfoBean)userInfoLists.get(rowIndex)).getUserId();
            if( userId.compareTo(userInfoBean.getUserId()) < 0){
                continue;
            }
            else {
                break;
            }
        }
        userInfoLists.add(rowIndex, userInfoBean);
         */
        hmUsers.put(userInfoBean.getUserId(), userInfoBean);
    }
    
    public void refresh() {
        hashTable.put(UNIT_NUMBER, selUnitNumber);
        hashTable.put(COMMAND, command);
        hashTable.put(MODULE, module);
        hashTable.put(FUNCTION, new Character(functionType));
        hashTable.put(PERSON_ID, userID);
        
        if (connectToServlet(CoeusGuiConstants.USER_SERVLET)) {
            userInfoLists = (Vector)dataObjects.get(0);
            userRoleInfoLists = (Vector)dataObjects.get(1);
        }else {
//            System.out.println("Problem in the response from the server.");
        }
        
        //added by sharath
        hmUsers.clear();
        if(userInfoLists != null){
            UserInfoBean userInfoBean;
            for(int index = 0; index < userInfoLists.size(); index++) {
                userInfoBean = (UserInfoBean)userInfoLists.get(index);
                hmUsers.put(userInfoBean.getUserId(), userInfoBean);
            }
        }
        //added by sharath
        
        //formatData();
        //rolesForm.setFormData(userRoles);
        rolesForm.getRolesTree().createTreeData(userRoleInfoLists);
        
        
        UserRoleNodeRenderer nodeRenderer = new UserRoleNodeRenderer(true);
        
        rolesTree = rolesForm.getRolesTree();
        rolesTree.setCellRenderer(nodeRenderer);
        rolesTree.setRowHeight(16);
        UserRoleNode node = (UserRoleNode)((UserRoleNode)rolesTree.getModel().getRoot()).getFirstChild();
        while(node!=null){
            TreePath nodePath = new TreePath(node);
            rolesTree.collapsePath(nodePath);
            node = (UserRoleNode)node.getNextSibling();
        }
        ( ( DefaultTreeModel )rolesTree.getModel() ).reload();
    }
    
    private void close(boolean fromInternalFrame)throws PropertyVetoException {
        Vector addedUsers = null;
        Vector deletedUsers = null;
        int added = 0, deleted = 0;
        addedUsers = rolesForm.getAddedUsers();
        deletedUsers = rolesForm.getRemovedUsers();
        
        if(addedUsers != null){
            added = addedUsers.size();
        }
        if(deletedUsers != null){
            deleted = deletedUsers.size();
        }
        
        if(added >0 || deleted > 0) {
            int selection = CoeusOptionPane.showQuestionDialog(SAVE_CHANGES, CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                saveActiveSheet();
            }else if( selection == CoeusOptionPane.SELECTION_NO ){
                rolesForm.getAddedUsers().removeAllElements();
                rolesForm.getRemovedUsers().removeAllElements();
            }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                //to stop window from closing before saving.
                throw new PropertyVetoException("Cancel",null);
                //return ;
            }
        }
        //this.doDefaultCloseAction();
        //dispose();
        mdiForm.removeFrame(title);
        if( !fromInternalFrame ) {
            super.doDefaultCloseAction();
        }
//        CoeusInternalFrame frame = mdiForm.getFrame(
//        CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE);
//        if(frame != null){
//            frame.setSelected(true);
//            frame.setVisible(true);
//        }
        
    }
    
    public void saveAsActiveSheet() {
    }
    
    public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
        //close();This will go in a endless loop
    }
    
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
        boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
        if(pce.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close(true);
        }
    }
    
    //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
    private void displayResetPassword() {
        if(resetPwd == null) {
            resetPwd = new ResetPasswordForm();
        }
        resetPwd.display();
    }
    
    private boolean hasResetPasswordRights(String unitNumber){
        
        boolean hasRight = false;
        RequesterBean requesterBean = new RequesterBean();
        //Set the function type to requestor bean
        requesterBean.setFunctionType(GET_USER_RIGHTS);
        //Create a vector of rights
        Vector vecRights = new Vector();
        vecRights.add("MODIFY_USER");
        vecRights.add("ADD_USER");
        //Add the inputs to the vector
        Vector vecInput = new Vector();
        vecInput.add(unitNumber);
        vecInput.add(mdiForm.getUserId());
        vecInput.add(vecRights);
        //Set the input vector to requestor bean
        requesterBean.setDataObject(vecInput);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+UTILITY_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean != null){
            HashMap hmRights = (HashMap)responderBean.getDataObject();
            if(hmRights != null && hmRights.size() > 0){
                if(((Boolean)hmRights.get("MODIFY_USER")).booleanValue() ||
                        ((Boolean)hmRights.get("ADD_USER")).booleanValue()){
                    hasRight = true;
                }else{
                    hasRight = false;
                }
            }
        }
        return hasRight;
    }
    //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
    
} //End Class
