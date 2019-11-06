/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * UserDetailsForm.java
 *
 * Created on July 17, 2003, 11:44 AM
 */

/* PMD check performed, and commented unused imports and variables on 07-MAR-2012
 * by Bharati Umarani
 */

package edu.mit.coeus.user.gui;

import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ListSelectionListener;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
//import edu.mit.coeus.user.bean.UserMaintRoleBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.user.bean.UserDetailsController;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.table.CoeusTableModel;
import edu.mit.coeus.utils.table.CoeusDnDTable;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusMessageResources;

/** Displays User Details or creates Users.
 * @author sharathk
 */
public class UserDetailsForm extends javax.swing.JComponent {
    
    private CoeusDlgWindow userDetailsDlgWindow;
    private UserDetailsMediator userDetailsMediator;
    private UserDetailsController userDetailsController;
    
    private CoeusTableModel rolesAssignedTM;
    private CoeusTableModel rolesNotAssignedTM;
    
    private RolesCellRenderer cellRenderer;
    private RolesAssignedCellEditor cellEditor;
    
    private ImageIcon iIcnProposalRoleActive, iIcnProposalRoleInactive;
    private ImageIcon iIcnProtocolRoleActive, iIcnProtocolRoleInactive;
    private ImageIcon iIcnAdminActive, iIcnAdminInactive;
    private ImageIcon iIcnSystemActive, iIcnSystemInactive;
    private ImageIcon iIcnDescendY, iIcnDescendN;
    //Added for IACUC Changes - Start
    private ImageIcon iIcnIacucRoleActive, iIcnIacucRoleInactive;
    private static final char IACUC_LOWER_CASE = 'i';
    private static final char IACUC_UPPER_CASE = 'I';
    //IACUC Changes - End
    
    private CoeusSearch coeusSearch;
    private Frame owner;
    private boolean modal;
    
    private boolean loading;
    private boolean dirty;
    private boolean userAdded, userModified;
    
    private boolean canAddUser, canModifyUser, canMaintainUser, hasOSPRight;
    private HashMap allRoles;
    private Vector rolesForUnit, vecRolesAssigned;
    
    private String personId;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private CoeusMessageResources coeusMessageResources;
    
    private UserInfoBean userInfoBean;
    
    private static final String DEFAULT_TITLE = "User Maintenence For ";
    private static final String ROLES_WITHIN_UNIT = "Roles for this user within ";
    private static final String PERSON_SEARCH = "personSearch";
    
    private static final String ACTIVE = "Active";
    private static final String INACTIVE = "Inactive";
    private static final String EMPTY = "";
    
    //private static final int WIDTH = 600;
    private static final int WIDTH = 675;
    //private static final int HEIGHT = 325;
    private static final int HEIGHT = 460;
    
    private static final char PROPOSAL_LOWER_CASE = 'p';
    private static final char PROPOSAL_UPPER_CASE = 'P';
    
    private static final char PROTOCOL_LOWER_CASE = 'r';
    private static final char PROTOCOL_UPPER_CASE = 'R';
    
    private static final char ADMIN_LOWER_CASE = 'o';
    private static final char ADMIN_UPPER_CASE = 'O';
    
    private static final char SYSTEM_LOWER_CASE = 's';
    private static final char SYSTEM_UPPER_CASE = 'S';
    
    private static final char ACTIVE_LOWER_CASE = 'a';
    private static final char ACTIVE_UPPER_CASE = 'A';
    
    private static final char INACTIVE_LOWER_CASE = 'i';
    private static final char INACTIVE_UPPER_CASE = 'I';
    
    private static final char DESCEND_YES_LOWER_CASE = 'y';
    private static final char DESCEND_YES_UPPER_CASE = 'Y';
    
    private static final char DESCEND_NO_LOWER_CASE = 'n';
    private static final char DESCEND_NO_UPPER_CASE = 'N';
    
    private static final String DESCEND_YES = "Y";
    private static final String DESCEND_NO = "N";
    
    private static final String YES = "YES";
    private static final String NO = "NO";
    
    private static final int NONE_SELECTED = -1;
    
    private static final String BASE = "user_details_exceptionCode.";
    
    private static final String ENTER_USER_ID = BASE + "2536";
    private static final String ENTER_USER_NAME = BASE + "2537";
    private static final String ENTER_STATUS = BASE + "2538";
    private static final String USER_ID_MUST_BEGIN_WITH_LETTER = BASE + "2539";
    private static final String USER_ID_MUST_BE_ALPHANUMERIC = BASE + "2540";
    private static final String ENTER_PASSWORD = BASE + "2541";
    private static final String RECONFIRM_PASSWORD = BASE + "2542";
    private static final String PASSWORDS_MISMATCH = BASE + "2543";
    private static final String PASSWORD_MUST_BEGIN_WITH_LETTER = BASE + "2544";
    private static final String DESCEND_FLAG_CANNOT_BE_CHANGED = BASE + "2545";
    private static final String OVERWRITE_USER_DATA = BASE + "2546";
    private static final String SAVE_CHANGES = BASE + "2547";
    private static final String USER_ALREADY_EXISTS = BASE + "2548"; //A user already exists with this id. Do you want to select this user and refresh the screen?
    private static final String CANNOT_MODIFY_INSTITUTE_LEVEL_ROLE = BASE+"2550";
    private static final String USER_CREATED = BASE+"2551";
    private static final String USER_UPDATED = BASE+"2552";
    private static final String CANNOT_ADD_USER = BASE+"2553";
    
    //to get Details of Person from Person Search
    private static final String USER_NAME = "USER_NAME";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String UNIT_NUMBER = "HOME_UNIT";
    private static final String PERSON_ID = "PERSON_ID";
    
    //Authorization Function Types
    private static final String ADD_USER = "ADD_USER";
    private static final String MODIFY_USER = "MODIFY_USER";
    private static final String MAINTAIN_USER_ACCESS = "MAINTAIN_USER_ACCESS";
    private static final String GRANT_INSTITUTE_ROLES = "GRANT_INSTITUTE_ROLES";
    
    private static final int ROLE_ID_COLUMN = 0;
    private static final int ROLE_TYPE_COLUMN = 1;
    private static final int ROLE_STATUS_COLUMN = 2;
    private static final int DESCEND_COLUMN = 3;
    private static final int ROLE_NAME_COLUMN = 4;
    
    //AC Types
    public static final String INSERT = "I";
    public static final String UPDATE = "U";
    public static final String DELETE = "D";
    private String acType;
    
    //Colors
    private static final Color COLOR_FOREGROUND = Color.black;
    private static final Color COLOR_BACKGROUND = Color.lightGray;
    
    private String selUnitNumber,selUnitName;
    
    //Used so that focus should not be set to user name whwnever window activated.
    //Focus should be set to user name only when first time window is activated.
    private boolean triggerDisplay = false;
    
    
    /** Creates new form UserDetailsForm
     * @param owner owner
     * @param modal modality
     */
    public UserDetailsForm(Frame owner, boolean modal) {
        this.owner = owner;
        this.modal = modal;
        initComponents();
        postInitComponents();
        selUnitName = mdiForm.getUnitName();
        selUnitNumber = mdiForm.getUnitNumber();
        
        loadRolesAndAuthorizations(mdiForm.getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, selUnitNumber);
        UserDetailsFocusTraversalPolicy focusPolicy = new UserDetailsFocusTraversalPolicy();
        pnlUserInfo.setFocusTraversalPolicy(focusPolicy);
        pnlUserInfo.setFocusCycleRoot(true);
    }
    
    public UserDetailsForm(Frame owner, boolean modal,String unitNumber,String unitName) {
        this.owner = owner;
        this.modal = modal;
        this.selUnitName = unitName;
        this.selUnitNumber = unitNumber;
        initComponents();
        postInitComponents();
        loadRolesAndAuthorizations(mdiForm.getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, selUnitNumber);
        UserDetailsFocusTraversalPolicy focusPolicy = new UserDetailsFocusTraversalPolicy();
        pnlUserInfo.setFocusTraversalPolicy(focusPolicy);
        pnlUserInfo.setFocusCycleRoot(true);
    }

    /** loads roles and authorization details.
     * @param userId userId
     * @param allRolesUnitNumber unit number to get all Roles.
     * @param unitNumber logged in users unit number.
     */
    private void loadRolesAndAuthorizations(String userId, String allRolesUnitNumber, String unitNumber) {
        try{
            //System.out.println("UserId : "+userId+" allRolesUnitNumber : "+allRolesUnitNumber+" unitNumber : "+unitNumber);
            Vector data = (Vector)userDetailsController.readRolesAndAuthorizations(userId, allRolesUnitNumber, unitNumber);
            HashMap authorizations = (HashMap)data.get(0);
            canAddUser = ((Boolean)authorizations.get(ADD_USER)).booleanValue();
            canModifyUser = ((Boolean)authorizations.get(MODIFY_USER)).booleanValue();
            canMaintainUser = ((Boolean)authorizations.get(MAINTAIN_USER_ACCESS)).booleanValue();
            hasOSPRight = ((Boolean)authorizations.get(GRANT_INSTITUTE_ROLES)).booleanValue();
            
            Vector roles;
            roles = (Vector)data.get(1);
            rolesForUnit = (Vector)data.get(2);
            //add to hashmap
            allRoles = new HashMap();
            RoleInfoBean roleInfoBean;
            String strDescend;
            for(int index = 0; index < roles.size(); index++) {
                roleInfoBean = (RoleInfoBean)roles.get(index);
                if(roleInfoBean.isDescend()){
                    strDescend = DESCEND_YES;
                }else {
                    strDescend = DESCEND_NO;
                }
                allRoles.put(new Integer(roleInfoBean.getRoleId()), strDescend);
            }
          //Added for Case 3608 -  User Maintenance - when add/update a user, there is no roles to be assigned in the pop up window -Start
            if(rolesForUnit !=null && rolesForUnit.size() > 0 ){
                 for(int index = 0; index < rolesForUnit.size(); index++) {
                    roleInfoBean = (RoleInfoBean)rolesForUnit.get(index);
                    if(allRoles.get(new Integer(roleInfoBean.getRoleId())) == null){
                           if(roleInfoBean.isDescend()){
                               strDescend = DESCEND_YES;
                           }else{
                               strDescend = DESCEND_NO;
                            }
                            allRoles.put(new Integer(roleInfoBean.getRoleId()), strDescend);
                    }
                 }
           //Added for Case 3608 -  User Maintenance - when add/update a user, there is no roles to be assigned in the pop up window -End
            }
            
            //Enabling / Disabling depend on User Rights/Roles
            if(! hasOSPRight()) {
                chkDoNotCreateDatabaseUser.setSelected(true);
                chkDoNotCreateDatabaseUser.setVisible(false);
            }
            
        }catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    /** checks wheather the logged in user has right to Add User.
     * @return true/false depending on wheather the logged in user
     * has right to Add User.
     */
    public boolean canAddUser() {
        return canAddUser;
    }
    
    /** checks wheather the logged in user has right to Modify User.
     * @return true/false depending on wheather the logged in user
     * has right to Modify User.
     */
    public boolean canModifyUser() {
        return canModifyUser;
    }
    
    /** checks wheather the logged in user has right to Maintain User.
     * @return true/false depending on wheather the logged in user
     * has right to Maintain User.
     */
    public boolean canMaintainUser() {
        return canMaintainUser;
    }
    
    /** checks wheather the logged in user has OSP Right.
     * @return true/false depending on wheather the logged in user
     * has OSP Right.
     */
    public boolean hasOSPRight() {
        return hasOSPRight;
    }
    
    /** creates a CoeusDialog and
     * Sets up the UI for user Interaction.
     * registers components with event handlers.
     */
    private void postInitComponents() {
        //modifiedRoles = new HashMap();
        
        //Setting Colors
        
        //txtUserId.setDisabledTextColor(COLOR_FOREGROUND);
        //txtUserName.setDisabledTextColor(COLOR_FOREGROUND);
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        //setting non mit check non editable
        chkNonEmployee.setSelected(true);
        chkNonEmployee.setEnabled(false);
        
        //populating Combo Box
        cmbStatus.addItem(EMPTY);
        cmbStatus.addItem(ACTIVE);
        cmbStatus.addItem(INACTIVE);
        
        //instantiating Image Icons.
        iIcnProposalRoleActive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON));
        
        iIcnProposalRoleInactive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PROP_INACTIVE_ROLE_ICON));
        
        iIcnAdminActive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADMIN_ACTIVE_ROLE_ICON));
        
        iIcnAdminInactive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADMIN_INACTIVE_ROLE_ICON));
        
        iIcnSystemActive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SYSTEM_ACTIVE_ROLE_ICON));
        
        iIcnSystemInactive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SYSTEM_INACTIVE_ROLE_ICON));
        
        iIcnProtocolRoleActive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ACTIVE_ROLE_ICON));
        
        iIcnProtocolRoleInactive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.INACTIVE_ROLE_ICON));
        
        iIcnDescendN = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DESCEND_NO_ICON));
        
        iIcnDescendY = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DESCEND_YES_ICON));
        
        //Added for IACUC Changes - Start
        iIcnIacucRoleActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.IACUC_ACTIVE_ROLE_ICON));
        
        iIcnIacucRoleInactive = new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.IACUC_IN_ACTIVE_ROLE_ICON));
        //IACUC Changes - End
        
        //text fields....
        txtUserId.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
         txtUserName.setBackground(Color.white);
        //txtUserName.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        //setting up Table
        
        //Designating Selection Listener for Table
        tblRolesAssigned.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        tblRolesNotAssigned.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        tblRolesAssigned.getSelectionModel().addListSelectionListener(new UserDetailsMediator());
        tblRolesNotAssigned.getSelectionModel().addListSelectionListener(new UserDetailsMediator());
        
        //Making Tables Multiple Selection to support Multiple Drag.
        tblRolesAssigned.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblRolesNotAssigned.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        Class colTypes[] = {Integer.class, String.class, String.class, String.class, String.class};
        rolesAssignedTM = new RolesAssignedTableModel(colTypes);
        tblRolesAssigned.setModel(rolesAssignedTM);
        
        rolesNotAssignedTM = new RolesNotAssignedTableModel(colTypes);
        tblRolesNotAssigned.setModel(rolesNotAssignedTM);
        
        int columnWidths[] = {25,25,25,25,200};
        for(int column = 0; column < columnWidths.length; column++) {
            tblRolesNotAssigned.getColumnModel().getColumn(column).setPreferredWidth(columnWidths[column]);
            tblRolesAssigned.getColumnModel().getColumn(column).setPreferredWidth(columnWidths[column]);
        }
        
        
        tblRolesNotAssigned.setColumnVisibility(ROLE_ID_COLUMN,false);
        tblRolesAssigned.setColumnVisibility(ROLE_ID_COLUMN,false);
        
        // since one column is already not visible ( -1 )
        tblRolesAssigned.setColumnVisibility(ROLE_STATUS_COLUMN - 1, false);
        tblRolesNotAssigned.setColumnVisibility(ROLE_STATUS_COLUMN - 1, false);
        
        //Setting Cell Editor and Renderer
        cellRenderer = new RolesCellRenderer();
        
        tblRolesAssigned.getColumnModel().getColumn(ROLE_TYPE_COLUMN - 1).setCellRenderer(cellRenderer);
        tblRolesNotAssigned.getColumnModel().getColumn(ROLE_TYPE_COLUMN - 1).setCellRenderer(cellRenderer);
        
        cellEditor = new RolesAssignedCellEditor();
        
        tblRolesAssigned.getColumnModel().getColumn(DESCEND_COLUMN - 2).setCellEditor(cellEditor);
        tblRolesAssigned.getColumnModel().getColumn(DESCEND_COLUMN - 2).setCellRenderer(cellRenderer);
        tblRolesNotAssigned.getColumnModel().getColumn(DESCEND_COLUMN - 2).setCellRenderer(cellRenderer);
        
        //Don't show table headers
        tblRolesAssigned.setTableHeader(null);
        tblRolesNotAssigned.setTableHeader(null);
        
        tblRolesAssigned.setShowGrid(false);
        tblRolesNotAssigned.setShowGrid(false);
        
        //testing
        /*
        Vector row = new Vector();
        row.add(new ImageIcon("D:/image.gif"));
        row.add(new ImageIcon("D:/image.gif"));
        row.add("Hello");
        rolesNotAssignedTM.addRow(row);
        rolesNotAssignedTM.fireTableDataChanged();
         */
        
        userDetailsController = new UserDetailsController();
        
        //Registering Components with Listener
        userDetailsMediator = new UserDetailsMediator();
        btnOk.addActionListener(userDetailsMediator);
        btnCancel.addActionListener(userDetailsMediator);
        btnFind.addActionListener(userDetailsMediator);
        btnAdd.addActionListener(userDetailsMediator);
        btnRemove.addActionListener(userDetailsMediator);
        txtUserId.addFocusListener(userDetailsMediator);
        
        cmbStatus.addItemListener(userDetailsMediator);
        chkDoNotCreateDatabaseUser.addItemListener(userDetailsMediator);
        
        
        //Setting up Dialog to display UserDelegations
        userDetailsDlgWindow = new CoeusDlgWindow(owner, modal);
        userDetailsDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        userDetailsDlgWindow.addWindowListener(userDetailsMediator);
        userDetailsDlgWindow.getContentPane().add(this);
        setTitle(DEFAULT_TITLE+selUnitNumber+" : "+ selUnitName);
        lblRolesWithinUnit.setText(ROLES_WITHIN_UNIT + selUnitName);
        userDetailsDlgWindow.setSize(WIDTH,HEIGHT);
        userDetailsDlgWindow.setResizable(false);
        userDetailsDlgWindow.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    close();
            }
        });
        
        txtUserId.setRequestFocusEnabled(true);
        txtUserName.setRequestFocusEnabled(true);
        cmbStatus.setRequestFocusEnabled(true);
        pssWdPassword.setRequestFocusEnabled(true);
        pssWdReEnterPassword.setRequestFocusEnabled(true);
        
        if(!CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID) &&
           CoeusGuiConstants.SECONDARY_LOGIN_MODE == null) {
            chkDoNotCreateDatabaseUser.setEnabled(false);
        }
    }
    
    /** loads User Details to GUI.
     * @param userInfoBean loads user details related to this user.
     */
    public void loadUserDetails(UserInfoBean userInfoBean) {
        loadUserDetails(userInfoBean.getUserId());
    }
    
    /** loads Users Details into Use Details Panel.
     * @param userId whose Details has to be loaded.
     */
    public void loadUserDetails(String userId) {
        setAcType(UPDATE);
        setEnabled(false);
        btnFind.setEnabled(false);
        //chkDoNotCreateDatabaseUser.setEnabled(false);
                
        //moved top as instance variable
        //UserInfoBean userInfoBean;
        try{
            
            Vector response = (Vector)userDetailsController.read(userId, selUnitNumber);
            Vector userDetails = (Vector)response.get(0);
            userInfoBean = (UserInfoBean)userDetails.get(0);
            vecRolesAssigned = (Vector)userDetails.get(1);
            Vector vecRolesNotAssigned = (Vector)userDetails.get(2);
            loading = true;
            ///Added for Case 3608 -  User Maintenance - when add/update a user, there is no roles to be assigned in the pop up window -Start
            loadRolesAndAuthorizations(mdiForm.getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, selUnitNumber);   
            //Added for Case 3608 -  User Maintenance - when add/update a user, there is no roles to be assigned in the pop up window -End
            loadRoles(vecRolesAssigned, rolesAssignedTM);
            loadRoles(vecRolesNotAssigned, rolesNotAssignedTM);
            loading = true;
            txtUserId.setText(userInfoBean.getUserId());
            txtUserName.setText(userInfoBean.getUserName());
            char status = userInfoBean.getStatus();
            if(status == ACTIVE_LOWER_CASE || status == ACTIVE_UPPER_CASE) {
                cmbStatus.setSelectedItem(ACTIVE);
            }
            else if(status == INACTIVE_LOWER_CASE || status == INACTIVE_UPPER_CASE) {
                cmbStatus.setSelectedItem(INACTIVE);
            }
            boolean nonEmployee = userInfoBean.isNonEmployee();
            chkNonEmployee.setSelected(nonEmployee);
            
            //Get User Details and Roles.
            if(userInfoBean.getUnitNumber().trim().equals(selUnitNumber.trim())) {
                cmbStatus.setEnabled(true);
            }
            else {
                cmbStatus.setEnabled(false);
            }
            //End Role based Validation.
            
            //Enable/Disable Depends on User Rights.
            //cmbStatus.setEnabled(nonEmployee);
            txtUserName.setEnabled(nonEmployee);
            //chkDoNotCreateDatabaseUser.setEnabled(hasOSPRight());
            
            loading = false;
            
        }catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    /** loads User Roles.
     */
    public void loadUserRoles() {
        
        setAcType(INSERT);
        setEnabled(true);
        cmbStatus.setEnabled(true);
        btnFind.setEnabled(true);
        //Added for Case 3608 -  User Maintenance - when add/update a user, there is no roles to be assigned in the pop up window -Start
        loadRolesAndAuthorizations(mdiForm.getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, selUnitNumber);   
        //Added for Case 3608 -  User Maintenance - when add/update a user, there is no roles to be assigned in the pop up window -End
        //Vector roles;
        RoleInfoBean roleInfoBean;
        int roleId;
        char roleType, status;
        boolean descend;
        String roleName;
        
        Vector data, row;
        /*try{
            roles = (Vector)userDetailsController.getRolesNotAssigned(mdiForm.getUnitNumber());
        }catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
            return ;
        }*/
        //roles = (Vector)roles.get(0);
        loading = true;
        for(int count = 0; count < rolesForUnit.size(); count++) {
            roleInfoBean = (RoleInfoBean)rolesForUnit.get(count);
            row = new Vector();
            roleType = roleInfoBean.getRoleType();
            status = roleInfoBean.getStatus();
            roleId = roleInfoBean.getRoleId();
            descend = roleInfoBean.isDescend();
            roleName = roleInfoBean.getRoleName();
            
            row.add(ROLE_ID_COLUMN, new Integer(roleId));
            
            //addRole(row,roleType,status);
            
            row.add(ROLE_TYPE_COLUMN, EMPTY+roleType);
            row.add(ROLE_STATUS_COLUMN, EMPTY+status);
            
            if(descend) {
                row.add(DESCEND_COLUMN, DESCEND_YES);
            }
            else {
                row.add(DESCEND_COLUMN, DESCEND_NO);
            }
            
            row.add(ROLE_NAME_COLUMN, roleName);
            
            rolesNotAssignedTM.addRow(row);
        }
        
        loading = false;
        rolesNotAssignedTM.fireTableDataChanged();
        
        chkDoNotCreateDatabaseUser.setSelected(!CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID));
        
        //System.out.println("Roles Got");
    }
    
    /** adds role to Table.
     * @param row vector to be added to table as row.
     * @param roleType role Type.
     * @param status status(i.e active = a/A, inactive = i/I)
     */
    private void addRole(Vector row, char roleType, char status) {
        switch(roleType) {
            case ADMIN_LOWER_CASE:
            case ADMIN_UPPER_CASE:
                if(status == ACTIVE_LOWER_CASE || status == ACTIVE_UPPER_CASE) {
                    row.add(iIcnAdminActive);
                }
                else if(status == INACTIVE_LOWER_CASE || status == INACTIVE_UPPER_CASE) {
                    row.add(iIcnAdminInactive);
                }
                break;
                
            case PROPOSAL_LOWER_CASE:
            case PROPOSAL_UPPER_CASE:
                if(status == ACTIVE_LOWER_CASE || status == ACTIVE_UPPER_CASE) {
                    row.add(iIcnProposalRoleActive);
                }
                else if(status == INACTIVE_LOWER_CASE || status == INACTIVE_UPPER_CASE) {
                    row.add(iIcnProposalRoleInactive);
                }
                break;
                
            case PROTOCOL_LOWER_CASE:
            case PROTOCOL_UPPER_CASE:
                if(status == ACTIVE_LOWER_CASE || status == ACTIVE_UPPER_CASE) {
                    row.add(iIcnProtocolRoleActive);
                }
                else if(status == INACTIVE_LOWER_CASE || status == INACTIVE_UPPER_CASE) {
                    row.add(iIcnProtocolRoleInactive);
                }
                break;
                
            case SYSTEM_LOWER_CASE:
            case SYSTEM_UPPER_CASE:
                if(status == ACTIVE_LOWER_CASE || status == ACTIVE_UPPER_CASE) {
                    row.add(iIcnSystemActive);
                }
                else if(status == INACTIVE_LOWER_CASE || status == INACTIVE_UPPER_CASE) {
                    row.add(iIcnSystemInactive);
                }
                break;
            //Added for IACUC Changes - start
            case IACUC_LOWER_CASE:
            case IACUC_UPPER_CASE:
                if(status == ACTIVE_LOWER_CASE || status == ACTIVE_UPPER_CASE) {
                    row.add(iIcnIacucRoleActive);
                } else if(status == INACTIVE_LOWER_CASE || status == INACTIVE_UPPER_CASE) {
                    row.add(iIcnIacucRoleInactive);
                }
                break;
           //IACUC Changes - End
        }
    }
    
    /** validates the form.
     * returns true if necessary entries are valid
     * else returns false.
     * @return true if valid.
     * else returns false.
     */
    public boolean isUserDetailsValid() {
        if(txtUserId.getText().trim().equals(EMPTY)) {
            //CoeusOptionPane.showInfoDialog(ENTER_USER_ID);
            txtUserId.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_USER_ID));
            return false;
        }
        if(txtUserName.getText().trim().equals(EMPTY)) {
            txtUserName.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_USER_NAME));
            return false;
        }
        if(cmbStatus.getSelectedItem().equals(EMPTY)) {
            cmbStatus.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_STATUS));
            return false;
        }
        if(CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID) && ! Character.isLetter(txtUserId.getText().charAt(0))) {
            txtUserId.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(USER_ID_MUST_BEGIN_WITH_LETTER));
            return false;
        }
        //No Need to Check for Alpha Numeric since the Document manages it ;)
        if(chkDoNotCreateDatabaseUser.isSelected() || chkDoNotCreateDatabaseUser.isEnabled()==false) return true;
        
        if(new String(pssWdPassword.getPassword()).trim().equals(EMPTY)) {
            pssWdPassword.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_PASSWORD));
            return false;
        }
        if(new String(pssWdReEnterPassword.getPassword()).trim().equals(EMPTY)) {
            pssWdReEnterPassword.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RECONFIRM_PASSWORD));
            return false;
        }
        if(! new String(pssWdPassword.getPassword()).trim().equals(
        new String(pssWdReEnterPassword.getPassword()).trim())) {
            pssWdPassword.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PASSWORDS_MISMATCH));
            return false;
        }
        if(CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID) && !Character.isLetter(new String(pssWdPassword.getPassword()).trim().charAt(0))) {
            pssWdPassword.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PASSWORD_MUST_BEGIN_WITH_LETTER));
            return false;
        }
        
        return true;
    }
    
    /** displays the GUI.
     */
    public void display() {
        
        userAdded = false;
        userModified = false;
        
        triggerDisplay = true;
        
        if(acType.equals(INSERT)) {
            if(! canAddUser()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_ADD_USER));
                return ;
            }
        }
        else if(acType.equals(UPDATE)) {
            if(! canAddUser()) {
                setEnabled(false);
                cmbStatus.setEnabled(false);
                tblRolesAssigned.setEnabled(false);
                tblRolesNotAssigned.setEnabled(false);
                btnOk.setEnabled(false);
                btnAdd.setEnabled(false);
                btnRemove.setEnabled(false);
            }
            if(! canModifyUser()) {
                setEnabled(false);
                tblRolesAssigned.setEnabled(false);
                tblRolesNotAssigned.setEnabled(false);
            }
            if(! canMaintainUser()) {
                tblRolesAssigned.setEnabled(false);
                tblRolesNotAssigned.setEnabled(false);
            }
        }
        
        //Check for OSp Right
        //chkDoNotCreateDatabaseUser.setEnabled(hasOSPRight());
        
        //Set the location to center Screen
        userDetailsDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        
        userDetailsDlgWindow.setVisible(true);
        
    }
    /** sets the form title.
     * @param title title for form.
     */
    public void setTitle(String title) {
        userDetailsDlgWindow.setTitle(title);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblUserIsEmployee = new javax.swing.JLabel();
        lblUserNotEmployee = new javax.swing.JLabel();
        pnlUserInfo = new javax.swing.JPanel();
        lblUserId = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblEnterPassword = new javax.swing.JLabel();
        lblReEnterPassword = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        pssWdPassword = new javax.swing.JPasswordField();
        pssWdReEnterPassword = new javax.swing.JPasswordField();
        chkNonEmployee = new javax.swing.JCheckBox();
        chkDoNotCreateDatabaseUser = new javax.swing.JCheckBox();
        cmbStatus = new javax.swing.JComboBox();
        txtUserId = new javax.swing.JTextField();
        lblRoles = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        scrPnRolesAssigned = new javax.swing.JScrollPane();
        tblRolesAssigned = new edu.mit.coeus.utils.table.CoeusDnDTable();
        scrPnRolesNotAssigned = new javax.swing.JScrollPane();
        tblRolesNotAssigned = new edu.mit.coeus.utils.table.CoeusDnDTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        lblRolesNotAssigned = new javax.swing.JLabel();
        lblRolesWithinUnit = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(651, 445));
        setPreferredSize(new java.awt.Dimension(651, 445));
        lblUserIsEmployee.setForeground(java.awt.Color.blue);
        lblUserIsEmployee.setText("If new user is an Employee, use the Find button to get details from person table;");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        add(lblUserIsEmployee, gridBagConstraints);

        lblUserNotEmployee.setForeground(java.awt.Color.blue);
        lblUserNotEmployee.setText("Otherwise type user id and user name below.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(lblUserNotEmployee, gridBagConstraints);

        pnlUserInfo.setLayout(new java.awt.GridBagLayout());

        pnlUserInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlUserInfo.setMinimumSize(new java.awt.Dimension(400, 135));
        pnlUserInfo.setPreferredSize(new java.awt.Dimension(500, 135));
        lblUserId.setFont(CoeusFontFactory.getLabelFont());
        lblUserId.setText("User Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlUserInfo.add(lblUserId, gridBagConstraints);

        lblUserName.setFont(CoeusFontFactory.getLabelFont());
        lblUserName.setText("User Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        pnlUserInfo.add(lblUserName, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlUserInfo.add(lblStatus, gridBagConstraints);

        lblEnterPassword.setFont(CoeusFontFactory.getLabelFont());
        lblEnterPassword.setText("Enter Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        pnlUserInfo.add(lblEnterPassword, gridBagConstraints);

        lblReEnterPassword.setFont(CoeusFontFactory.getLabelFont());
        lblReEnterPassword.setText("Re-Enter Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        pnlUserInfo.add(lblReEnterPassword, gridBagConstraints);

        txtUserName.setDocument(new LimitedPlainDocument(30));
        txtUserName.setFont(CoeusFontFactory.getNormalFont());
        txtUserName.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        pnlUserInfo.add(txtUserName, gridBagConstraints);

        JTextFieldFilter passwordFilter = new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,30);
        pssWdPassword.setDocument(passwordFilter);
        pssWdPassword.setFont(CoeusFontFactory.getNormalFont());
        pssWdPassword.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 10);
        pnlUserInfo.add(pssWdPassword, gridBagConstraints);

        JTextFieldFilter reEnterPasswordFilter = new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,30);
        pssWdReEnterPassword.setDocument(reEnterPasswordFilter);
        pssWdReEnterPassword.setFont(CoeusFontFactory.getNormalFont());
        pssWdReEnterPassword.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        pnlUserInfo.add(pssWdReEnterPassword, gridBagConstraints);

        chkNonEmployee.setFont(CoeusFontFactory.getLabelFont());
        chkNonEmployee.setText("Non Employee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlUserInfo.add(chkNonEmployee, gridBagConstraints);

        chkDoNotCreateDatabaseUser.setFont(CoeusFontFactory.getLabelFont());
        chkDoNotCreateDatabaseUser.setText("Do not create new database user");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        pnlUserInfo.add(chkDoNotCreateDatabaseUser, gridBagConstraints);

        cmbStatus.setMinimumSize(new java.awt.Dimension(100, 22));
        cmbStatus.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        pnlUserInfo.add(cmbStatus, gridBagConstraints);

        txtUserId.setDocument(new LimitedPlainDocument(8));
        txtUserId.setMinimumSize(new java.awt.Dimension(100, 22));
        txtUserId.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlUserInfo.add(txtUserId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        add(pnlUserInfo, gridBagConstraints);

        lblRoles.setFont(CoeusFontFactory.getLabelFont());
        lblRoles.setText("Roles Assigned");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        add(lblRoles, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('a');
        btnAdd.setText("< Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(100, 25));
        btnAdd.setMinimumSize(new java.awt.Dimension(100, 25));
        btnAdd.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(btnAdd, gridBagConstraints);

        btnRemove.setFont(CoeusFontFactory.getLabelFont());
        btnRemove.setMnemonic('r');
        btnRemove.setText("Remove >");
        btnRemove.setMaximumSize(new java.awt.Dimension(100, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(100, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 0.5;
        add(btnRemove, gridBagConstraints);

        scrPnRolesAssigned.setMinimumSize(new java.awt.Dimension(235, 230));
        scrPnRolesAssigned.setPreferredSize(new java.awt.Dimension(235, 230));
        scrPnRolesAssigned.setViewportView(tblRolesAssigned);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 0);
        add(scrPnRolesAssigned, gridBagConstraints);

        scrPnRolesNotAssigned.setMinimumSize(new java.awt.Dimension(235, 230));
        scrPnRolesNotAssigned.setPreferredSize(new java.awt.Dimension(235, 230));
        scrPnRolesNotAssigned.setViewportView(tblRolesNotAssigned);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 0);
        add(scrPnRolesNotAssigned, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 2);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnCancel, gridBagConstraints);

        btnFind.setFont(CoeusFontFactory.getLabelFont());
        btnFind.setMnemonic('f');
        btnFind.setText("Find");
        btnFind.setMaximumSize(new java.awt.Dimension(75, 25));
        btnFind.setMinimumSize(new java.awt.Dimension(75, 25));
        btnFind.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnFind, gridBagConstraints);

        lblRolesNotAssigned.setFont(CoeusFontFactory.getLabelFont());
        lblRolesNotAssigned.setText("Roles Not Assigned");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblRolesNotAssigned, gridBagConstraints);

        lblRolesWithinUnit.setForeground(java.awt.Color.blue);
        lblRolesWithinUnit.setText("Roles for this this user within");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        add(lblRolesWithinUnit, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnFind;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnRemove;
    public javax.swing.JCheckBox chkDoNotCreateDatabaseUser;
    public javax.swing.JCheckBox chkNonEmployee;
    public javax.swing.JComboBox cmbStatus;
    public javax.swing.JLabel lblEnterPassword;
    public javax.swing.JLabel lblReEnterPassword;
    public javax.swing.JLabel lblRoles;
    public javax.swing.JLabel lblRolesNotAssigned;
    public javax.swing.JLabel lblRolesWithinUnit;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JLabel lblUserId;
    public javax.swing.JLabel lblUserIsEmployee;
    public javax.swing.JLabel lblUserName;
    public javax.swing.JLabel lblUserNotEmployee;
    public javax.swing.JPanel pnlUserInfo;
    public javax.swing.JPasswordField pssWdPassword;
    public javax.swing.JPasswordField pssWdReEnterPassword;
    public javax.swing.JScrollPane scrPnRolesAssigned;
    public javax.swing.JScrollPane scrPnRolesNotAssigned;
    public edu.mit.coeus.utils.table.CoeusDnDTable tblRolesAssigned;
    public edu.mit.coeus.utils.table.CoeusDnDTable tblRolesNotAssigned;
    public javax.swing.JTextField txtUserId;
    public javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
    
    /** For Testing Purpose Only
     */
    public static void main(String s[]) {
        UserDetailsForm userDetailsForm = new UserDetailsForm(new Frame(),true);
        userDetailsForm.display();
    }
    
    /** closes the User Details Dialog.
     * checks if any modification is done and asks user
     * wheather to save or discard.
     */
    public void close() {
        if(! btnOk.isEnabled()) {
            userDetailsDlgWindow.setVisible(false);
            return ;
        }
        
        //checks for dirty
        if(acType.equals(INSERT)) {
            if(txtUserId.getText().trim().length() > 0 
                || txtUserName.getText().trim().length() > 0
                || cmbStatus.getSelectedIndex() != 0) {
                dirty = true;
            }
        }
        else if(acType.equals(UPDATE)) {
            if(! (txtUserId.getText().trim().equals(userInfoBean.getUserId())
                || txtUserName.getText().trim().equals(userInfoBean.getUserName()))) {
                dirty = true;
            }
                
        }
        
        if(dirty) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES),CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                saveUserDetails();
            }
            else if(selection == CoeusOptionPane.SELECTION_NO) {
                userDetailsDlgWindow.setVisible(false);
            }
            else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
            }
        }
        else {
            userDetailsDlgWindow.setVisible(false);
        }
        
    }
    
    /** Creates New Role Info Bean From the Row Of the RoleAssigned Table
     * @param row row of RoleAssigned table from which the RoleInfoBean has to be created.
     * @param type INSERT, UPDATE or DELETE
     * @return created RoleInfoBean
     */
    private RoleInfoBean createRoleInfoBean(int row, String type) {
        int roleId;
        char descend = 'N';
        RoleInfoBean roleInfoBean;
        if(type == UPDATE || type == DELETE) {
            roleInfoBean = (RoleInfoBean)vecRolesAssigned.get(row);
        }
        else {
            roleInfoBean = new RoleInfoBean();
        }
        //COEUSQA:3445 - Error updating a users role - Start
        if(type == UPDATE || type == DELETE) {
            for(int rowIndex = 0; rowIndex < rolesAssignedTM.getRowCount(); rowIndex++) {
                if( roleInfoBean.getRoleId() == ((Integer)rolesAssignedTM.getValueAt(rowIndex, ROLE_ID_COLUMN)).intValue() ){
                    descend = rolesAssignedTM.getValueAt(rowIndex, DESCEND_COLUMN).toString().trim().charAt(0);
                }
            }
        } else{
            // Bug Fix #1699 - start
            char roleTy = rolesAssignedTM.getValueAt(row, ROLE_TYPE_COLUMN).toString().trim().charAt(0);
            if(roleTy == 'O'){
                roleInfoBean.setUnitNumber(getCampusForDept(selUnitNumber));
            }else{
                roleInfoBean.setUnitNumber(selUnitNumber);
            }// // Bug Fix #1699 - End
            
            roleId = ((Integer)rolesAssignedTM.getValueAt(row, ROLE_ID_COLUMN)).intValue();
            roleInfoBean.setRoleId(roleId);
            descend = rolesAssignedTM.getValueAt(row, DESCEND_COLUMN).toString().trim().charAt(0);
            roleInfoBean.setRoleName(rolesAssignedTM.getValueAt(row, ROLE_NAME_COLUMN).toString().trim());
            roleInfoBean.setRoleType(rolesAssignedTM.getValueAt(row, ROLE_TYPE_COLUMN).toString().trim().charAt(0));
        }
        //COEUSQA:3445 - End
        
        if(descend == DESCEND_YES_LOWER_CASE || descend == DESCEND_YES_UPPER_CASE) {
            roleInfoBean.setDescend(true);
        }
        else if(descend == DESCEND_NO_LOWER_CASE || descend == DESCEND_NO_UPPER_CASE) {
            roleInfoBean.setDescend(false);
        }
        //COEUSQA:3445 - Error updating a users role - Start
//        roleInfoBean.setRoleName(rolesAssignedTM.getValueAt(row, ROLE_NAME_COLUMN).toString().trim());
//        roleInfoBean.setRoleType(rolesAssignedTM.getValueAt(row, ROLE_TYPE_COLUMN).toString().trim().charAt(0));
        //COEUSQA:3445 - End
        roleInfoBean.setUserId(txtUserId.getText().trim());
        roleInfoBean.setUpdateUser(mdiForm.getUserId());
        roleInfoBean.setAcType(acType);
        
        return roleInfoBean;
    }
    
    /** saves user details.
     */
    public void saveUserDetails() {
        if(! isUserDetailsValid()) return ;
        
        if(! dirty){
            if(userInfoBean != null && userInfoBean.getUserName().equals(txtUserName.getText())) {
                userDetailsDlgWindow.setVisible(false);
                return ;
            }
        }
        
        char status;
        UserInfoBean userInfoBean = new UserInfoBean();
        
        if(acType.equals(UPDATE)) {
            userInfoBean = this.userInfoBean;
        }else{
            userInfoBean.setUnitNumber(selUnitNumber);
            userInfoBean.setUnitName(selUnitName);
        }
        
        userInfoBean.setNonEmployee(chkNonEmployee.isSelected());
        userInfoBean.setUserId(txtUserId.getText().trim());
        userInfoBean.setUserName(txtUserName.getText().trim());
        if(cmbStatus.getSelectedItem().toString().trim().equalsIgnoreCase(ACTIVE)) {
            status = ACTIVE_UPPER_CASE;
        }
        else status = INACTIVE_UPPER_CASE;
        userInfoBean.setStatus(status);
        /*userInfoBean.setUnitNumber(mdiForm.getUnitNumber());
        userInfoBean.setUnitName(mdiForm.getUnitName());*/
        userInfoBean.setPersonId(personId);
        userInfoBean.setUpdateUser(mdiForm.getUserId());
        userInfoBean.setAcType(acType);
        
        Vector vecUserRoles  = new Vector();
        //RoleInfoBean roleInfoBean;
        //int roleId;
        //char descend;
        
        if(acType.equals(INSERT)) {
            //create Role Info Beans
            int rowCount = tblRolesAssigned.getRowCount();
            
            for(int row = 0; row < rowCount; row++) {
                vecUserRoles.add(createRoleInfoBean(row, INSERT));
            }//end for
            try{
                Vector vecDbUser = null;
                if(! chkDoNotCreateDatabaseUser.isSelected()) {
                    vecDbUser = new Vector();
                    vecDbUser.add(txtUserId.getText().trim());
                    vecDbUser.add(new String(pssWdPassword.getPassword()));
                }
                userDetailsController.create(userInfoBean, vecUserRoles, vecDbUser);
                this.userInfoBean = userInfoBean;
                userAdded = true;
                //CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(USER_CREATED));
                userDetailsDlgWindow.setVisible(false);
            }catch (CoeusClientException coeusClientException) {
                CoeusOptionPane.showDialog(coeusClientException);
            }
        } //Insert Roles
        else if(acType.equals(UPDATE)) {
            int roleId;
            boolean descend;
            String strDescend;
            boolean dsnd;
            rolesWhenLoaded :for(int index = 0; index < vecRolesAssigned.size(); index++) {
                roleId = ((RoleInfoBean)vecRolesAssigned.get(index)).getRoleId();
                descend = ((RoleInfoBean)vecRolesAssigned.get(index)).isDescend();
                if(descend) strDescend = DESCEND_YES;
                else strDescend = DESCEND_NO;
                for(int row = 0; row < rolesAssignedTM.getRowCount(); row++) {
                    if(roleId == ((Integer)rolesAssignedTM.getValueAt(row, ROLE_ID_COLUMN)).intValue()) {
                        if(strDescend.equalsIgnoreCase(rolesAssignedTM.getValueAt(row, DESCEND_COLUMN).toString().trim())) {
                            //Role Not Modified so start with next role.
                            continue rolesWhenLoaded;
                        }
                        else {
                            //Role Modified store in vecRoles and set acType as M
                            RoleInfoBean roleInfoBean;
                            //COEUSQA:3445 - Error updating a users role - Start
                            //roleInfoBean = createRoleInfoBean(row, UPDATE);
                            roleInfoBean = createRoleInfoBean(index, UPDATE);
                            //COEUSQA:3445 - End
                            roleInfoBean.setAcType(UPDATE);
                            vecUserRoles.add(roleInfoBean);
                            continue rolesWhenLoaded;
                        }
                    }//End If
                    //Check with next role in Role Assigned Table
                }//End For Table Model
                //Role Deleted so store in vecRoles and set acType as U
                
                
                RoleInfoBean roleInfoBean = (RoleInfoBean)vecRolesAssigned.get(index);
                roleInfoBean.setUserId(txtUserId.getText().trim());
                roleInfoBean.setAcType(DELETE);
                vecUserRoles.add(roleInfoBean);
                
            }//End For Vector
            //End of finding roles Modified and Deleted.
            
            //Start Finding Roles Newly Added.
            rolesAfterModification:for(int row = 0; row < rolesAssignedTM.getRowCount(); row++) {
                roleId = ((Integer)rolesAssignedTM.getValueAt(row, ROLE_ID_COLUMN)).intValue();
                for(int index = 0; index < vecRolesAssigned.size(); index++) {
                    if(((RoleInfoBean)vecRolesAssigned.get(index)).getRoleId() == roleId) {
                        //Not New Continue
                        continue rolesAfterModification;
                    }
                }//end For vec Roles Assigned
                //Newly Added so store in vecRoles and set the acType as I
                RoleInfoBean roleInfoBean = createRoleInfoBean(row,INSERT);
                roleInfoBean.setAcType(INSERT);
                vecUserRoles.add(roleInfoBean);
            }//End For Roles Assigned Table Model.
            //End of Finding Roles Newly Added
            try{
                
                UserInfoBean userInfoBeans = new UserInfoBean();
                String userId = txtUserId.getText().trim();
                Vector response = (Vector)userDetailsController.read(userId, selUnitNumber);
                

                Vector userDetails = (Vector)response.get(0);
                userInfoBeans = (UserInfoBean)userDetails.get(0);
                // Bug Fix #1699. checking the condition for chkNonEmployee.isEnabled()- start
                //if(chkNonEmployee.isSelected() && chkNonEmployee.isEnabled()){
                //if(getOpenMode().equals(MOUSE_CLCIKED)){
                    boolean dataChanged = isDataChanged(userInfoBeans);
                    if(dataChanged){
                        // Bug Fix #1699 - End
                        userInfoBeans.setAcType(UPDATE);
                        userInfoBeans.setUserId(txtUserId.getText());
                        userInfoBeans.setUnitNumber(selUnitNumber);
                        userInfoBeans.setUpdateUser(mdiForm.getUserId());
                        userInfoBeans.setUserName(txtUserName.getText().trim());
                        userInfoBeans.setNonEmployee(chkNonEmployee.isSelected());
                        if(cmbStatus.getSelectedItem().toString().trim().equalsIgnoreCase(ACTIVE)) {
                            status = ACTIVE_UPPER_CASE;
                        }
                        else status = INACTIVE_UPPER_CASE;
                        userInfoBeans.setStatus(status);
                    }
                //}
                if((vecUserRoles!= null && vecUserRoles.size() >0) || dataChanged){
                    userDetailsController.update(userInfoBeans, vecUserRoles);
                }
                //this.userInfoBean = userInfoBean;
                userModified = true;
                //CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(USER_UPDATED));
                userDetailsDlgWindow.setVisible(false);
            }catch (CoeusClientException coeusClientException) {
                CoeusOptionPane.showDialog(coeusClientException);
            }catch (Exception exception){
             //  exception.printStackTrace();
                CoeusOptionPane.showErrorDialog(exception.getMessage());
                
            }
        }//Update Roles
      
    }
    
    private boolean isDataChanged(UserInfoBean userInfoBean){
        boolean dataChanged = false;
        String userName = txtUserName.getText().trim();
        String beanUserName = userInfoBean.getUserName();
        Object cmpStatzus = cmbStatus.getSelectedItem();
        char status = ' ';
        char beanStatus = ' ';
        if(cmpStatzus.equals(ACTIVE)){
            status = ACTIVE_UPPER_CASE;
        }else if(cmpStatzus.equals(INACTIVE)){
            status = INACTIVE_UPPER_CASE;
        }
        //Check for the datachage
        beanStatus = userInfoBean.getStatus();
        if(status!=beanStatus){
            dataChanged = true;
        }else if((!userName.trim().equals(beanUserName.trim()))){
            dataChanged = true;
        }
        return dataChanged;
        
    }
    
    
    /** resets the form.
     */
    public void reset() {
        txtUserId.setText(EMPTY);
        txtUserName.setText(EMPTY);
        cmbStatus.setSelectedItem(EMPTY);
        pssWdPassword.setText(EMPTY);
        pssWdReEnterPassword.setText(EMPTY);
        pssWdPassword.setBackground(Color.white);
        pssWdReEnterPassword.setBackground(Color.white);
        chkNonEmployee.setSelected(true);
        chkDoNotCreateDatabaseUser.setSelected(false);
        rolesNotAssignedTM.getDataVector().removeAllElements();
        rolesAssignedTM.getDataVector().removeAllElements();
    }
    
    /** enables or disables the GUI components in the form.
     * @param enabled enabled flag.
     */
    public void setEnabled(boolean enabled) {
        txtUserId.setEnabled(enabled);
        txtUserName.setEnabled(enabled);
        if(enabled) {
            txtUserId.setBackground(Color.white);
            txtUserName.setBackground(Color.white);
        }
        else {
            txtUserId.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
             txtUserName.setBackground(Color.white);
            //txtUserName.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
            pssWdPassword.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
            pssWdReEnterPassword.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        }
        
        //cmbStatus.setEnabled(enabled);
        //chkNonEmployee.setEnabled(enabled);
        pssWdPassword.setEnabled(enabled);
        pssWdReEnterPassword.setEnabled(enabled);
        
        if(enabled) {
            boolean createDBUserFlag = CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID) ||
                                        CoeusGuiConstants.SECONDARY_LOGIN_MODE != null;
            chkDoNotCreateDatabaseUser.setEnabled(createDBUserFlag);
        }else {
            chkDoNotCreateDatabaseUser.setEnabled(enabled);
        }
        
        //Gray out the TextFields when disabled
        /*Color bgColor;
        if(! enabled) {
            bgColor = Color.lightGray;
        }
        else {
            bgColor = Color.white;
        }
        //txtUserId.setBackground(bgColor);
        //txtUserName.setBackground(bgColor);*/
        
        
    }
    
    /** loads the roles to the Table.
     * @param vecRoles vector containing collection of RoleInfoBeans.
     * @param model Table Model of the Table in which these roles has to be displayed.
     */
    private void loadRoles(Vector vecRoles, DefaultTableModel model) {
        RoleInfoBean roleInfoBean;
        Vector vecRowData;
        int roleId;
        String roleName;
        char roleType, status;
        boolean descend;
        loading = true;
        
        for(int index = 0; index < vecRoles.size(); index++) {
            //Load Roles.
            roleInfoBean = (RoleInfoBean)vecRoles.get(index);
            vecRowData = new Vector();
            roleId = roleInfoBean.getRoleId();
            roleType = roleInfoBean.getRoleType();
            status = roleInfoBean.getStatus();
            descend = roleInfoBean.isDescend();
            roleName = roleInfoBean.getRoleName();
                vecRowData.add(ROLE_ID_COLUMN, new Integer(roleId));
                vecRowData.add(ROLE_TYPE_COLUMN, EMPTY+roleType);
                vecRowData.add(ROLE_STATUS_COLUMN, EMPTY + status);

                if(descend) {
                    vecRowData.add(DESCEND_COLUMN, DESCEND_YES);
                }
                else {
                    vecRowData.add(DESCEND_COLUMN, DESCEND_NO);
                }

                vecRowData.add(ROLE_NAME_COLUMN, roleName);

                model.addRow(vecRowData);
                
        }       
        //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
        Vector vecSortRoles = model.getDataVector();
        if(vecSortRoles != null && !vecSortRoles.isEmpty()){
            HashMap hmRoles = new HashMap();
            for(int index=0;index<vecSortRoles.size();index++){
                String roleTypeDesc = (String)((Vector)vecSortRoles.get(index)).get(ROLE_TYPE_COLUMN);
                if(hmRoles.get(roleTypeDesc) != null){
                    Vector vecRoleData = (Vector)hmRoles.get(roleTypeDesc);
                    vecRoleData.add(vecSortRoles.get(index));
                    hmRoles.put(roleTypeDesc,vecRoleData);
                }else{
                    Vector vecRoleData = new Vector();
                    vecRoleData.add(vecSortRoles.get(index));
                    hmRoles.put(roleTypeDesc,vecRoleData);
                }
            }
            Vector vecOrderedColl = new Vector();
            if(!hmRoles.isEmpty()){
                Iterator roleIterator = hmRoles.keySet().iterator();
                while(roleIterator.hasNext()){
                    String roleTypeDesc = (String)roleIterator.next();
                    Vector vecRoleData = (Vector)hmRoles.get(roleTypeDesc);
                    //sorting the collection based on the role name
                    if(vecRoleData != null && vecRoleData.size() > 0){
                        Collections.sort(vecRoleData,new RoleDescComparator());                   
                    }
                    hmRoles.put(roleTypeDesc,vecRoleData);
                }
                //sorting the rolesbased on roletypes
                for (int i = 0; i < CoeusGuiConstants.roleRightSortOrder.length; i++) {
                    String roleTypeDesc = CoeusGuiConstants.roleRightSortOrder[i];
                    if(hmRoles.get(roleTypeDesc) != null){                         
                      vecOrderedColl.addAll((Vector)hmRoles.get(roleTypeDesc));  
                      // model.addRow(vecOrderedColl);  
                     
                    }                      
                }    
            }
            //Assign all the roles to the User maintenance
            if(vecOrderedColl != null && vecOrderedColl.size() > 0){
                //Before assigning the sorted listt o model remove the existing data from datavector
                model.getDataVector().removeAllElements();
                for(int i = vecOrderedColl.size()-1; i >= 0; i--){
                    Vector vecData = (Vector) vecOrderedColl.get(i);
                    model.addRow(vecData);
                }
            }
        }
        //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end 
        loading = false;
        model.fireTableDataChanged();
    }
    
    /** sets the AC Type for the UI.
     * @param acType Values can be INSERT, UPDATE, DELETE.
     */
    public void setAcType(String acType) {
        if(! (acType.equals(INSERT) || acType.equals(UPDATE) || acType.equals(DELETE))) {
            throw new IllegalArgumentException();
        }
        this.acType = acType;
        dirty = false;
    }
    
    /** checks whether the user already exists.
     * @param userId who has to be checked for Duplicacy.
     * @return true if user already exists
     * else returns false.
     */
    public boolean isUserDuplicate(String userId) {
        //If Disabled then it is in modify/View Mode no need to check.
        if(! txtUserId.isEnabled()) return false;
        
        //Check with Base Window First
        
        //Check with Database (Contact the Server)
        try{
            return userDetailsController.isUserDuplicate(userId);
        }catch (CoeusClientException coeusClientException) {
            return true;
        }
        
        //return false;
    }
    
    /** Added by chandra 21 Sept 2004
     *Check if the selected user is present in any unit. If yes then 
     *throw the message
     *@user id which specifies the entered user
     *@returns the unit number for the specified user
     */
        public String getUserForUnit(String userId) {
            if(! txtUserId.isEnabled()) return EMPTY;
            try{
                return userDetailsController.getUserForUnit(userId);
            }catch (CoeusClientException coeusClientException) {
                return EMPTY;
            }
        }// End chandra 21 Sept 2004
        
        /** Pass the unit number to get the top level unit while 
         *updating the system level roles in another unit.
         *@ unit number which is selected from a specific unit
         *@ returns the top level unit say 000001
         *Bug Fix #1699
         */
        public String getCampusForDept(String unitNumber) {
            String topLevelUnit = EMPTY;
            try{
                if(unitNumber!= null && !(unitNumber.equals(EMPTY))){
                    topLevelUnit = userDetailsController.getCampusForDept(unitNumber);
                }
            }catch (CoeusClientException coeusClientException){
                coeusClientException.printStackTrace();
                CoeusOptionPane.showErrorDialog(coeusClientException.getMessage());
            }
            return topLevelUnit;
        }// End Bug Fix #1699
    
    public boolean isUserAdded() {
        return userAdded;
    }
    
    public UserInfoBean getAddedUser() {
        return this.userInfoBean;
    }
    
    public boolean isUserModified() {
        return userModified;
    }
    
    public UserInfoBean getModifiedUser() {
        return this.userInfoBean;
    }
    
    
    
    /** Mediator for User Details
     * Listsns to all events in User Details.
     */
    class UserDetailsMediator extends WindowAdapter
    implements ActionListener, ListSelectionListener, ItemListener, FocusListener {
        Object source;
        /** listens to action performed events.
         * @param actionEvent ActionEvent.
         */
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            source = actionEvent.getSource();
            if(source.equals(btnFind)) {
                
                try{
                    //Create Search Instance
                    try{
                        coeusSearch = new CoeusSearch(owner, PERSON_SEARCH,CoeusSearch.TWO_TABS);
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    coeusSearch.showSearchWindow();
                    HashMap rowData = coeusSearch.getSelectedRow();
                    if(rowData == null) return ;
                    setUser(rowData);
                    txtUserId.setEnabled(txtUserId.getText().trim().equals(EMPTY));
                    txtUserName.setEnabled(txtUserName.getText().trim().equals(EMPTY));
                    
                    if(!txtUserName.isEnabled()) txtUserName.setBackground(COLOR_BACKGROUND);
                    else txtUserName.setBackground(Color.white); 
                    
                    if(! txtUserId.isEnabled()) txtUserId.setBackground(COLOR_BACKGROUND);
                    else txtUserId.setBackground(Color.white);
                    
                    dirty = true;
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }//End Event Handling Code for Find Button
            else if(source.equals(btnOk)) {
                saveUserDetails();
            }//End Event Handling Code for OK Button
            else if(source.equals(btnCancel)) {
                close();
            }//End Event Handling Code for Cancel Button
            else if(source.equals(btnAdd)) {
                moveRows(tblRolesNotAssigned, tblRolesAssigned);
            }//End Event Handling Code for ADD Button
            else if(source.equals(btnRemove)) {
                moveRows(tblRolesAssigned, tblRolesNotAssigned);
            }//End Event Handling Code for Remove Button
        }
        
        /** moves selected rows.
         * @param from from Table
         * @param to to Table.
         */
        private void moveRows(CoeusDnDTable from, CoeusDnDTable to) {
            Vector rowData;
            int selectedRows[] = from.getSelectedRows();
            if(selectedRows.length < 1) return ;
            
            int rowInserted = to.getRowCount();
            
            cellEditor.cancelCellEditing();
            
            for(int row = 0; row < selectedRows.length; row++) {
                rowData = ((CoeusTableModel)from.getModel()).getRow(selectedRows[row] - row);
                ((CoeusTableModel)to.getModel()).addRow(rowData);
                ((CoeusTableModel)from.getModel()).removeRow(selectedRows[row] - row);
            }
            
            ((CoeusTableModel)to.getModel()).fireTableDataChanged();
            ((CoeusTableModel)from.getModel()).fireTableDataChanged();
            
            dirty = true;
            
            btnAdd.setEnabled(false);
            btnRemove.setEnabled(false);
        }
        //when window opens set the focus...UserName textfield
        
        public void windowActivated(WindowEvent evnt)
        {
            if(! triggerDisplay) return ;
            else {
                triggerDisplay = false;
            }
            
            if(txtUserName.isEnabled()) {
                //txtUserName.setRequestFocusEnabled(true);
                txtUserName.requestFocusInWindow();
            }
            else {
                btnCancel.setRequestFocusEnabled(true);
                btnCancel.requestFocusInWindow();
            }
         }
        
        /** listens to window closing event.
         * @param windowEvent WindowEvent.
         */
        public void windowClosing(WindowEvent windowEvent) {
            close();
        }

        /** listens to selection changed event.
         * @param listSelectionEvent ListSelectionEvent.
         */
        public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
            if(!listSelectionEvent.getValueIsAdjusting()) return ;
            
            ListSelectionModel listSelectionModel = (ListSelectionModel)listSelectionEvent.getSource();
            int selectedRow;
            
            if(tblRolesAssigned.getSelectionModel().equals(listSelectionModel)) {
                selectedRow = tblRolesAssigned.getSelectedRow();
                if(selectedRow == -1) return ;
                
                String roleType = (String)tblRolesAssigned.getValueAt(selectedRow, ROLE_TYPE_COLUMN - 1);
                
                //Authorization Check
                if(roleType.equalsIgnoreCase(EMPTY + ADMIN_UPPER_CASE) && (!hasOSPRight())) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_MODIFY_INSTITUTE_LEVEL_ROLE));
                    tblRolesAssigned.clearSelection();
                    return ;
                }
                //End Authorization Check
                btnAdd.setEnabled(false);
                btnRemove.setEnabled(true);
                tblRolesNotAssigned.clearSelection();
                
            }
            else if(tblRolesNotAssigned.getSelectionModel().equals(listSelectionModel)) {
                btnAdd.setEnabled(true);
                btnRemove.setEnabled(false);
                tblRolesAssigned.clearSelection();
            }
            
        }
        
        /** sets the user details into User Details GUI.
         * @param rowData HashMap containing user details.
         */
        private void setUser(HashMap rowData) {
            String userName = EMPTY, fullName = EMPTY, unitNumber = EMPTY;
            
            if(rowData.get(USER_NAME) != null)
                userName = rowData.get(USER_NAME).toString().trim();
            
            if(rowData.get(FULL_NAME) != null)
                fullName = rowData.get(FULL_NAME).toString().trim();
            
            if(rowData.get(UNIT_NUMBER) != null)
            unitNumber = rowData.get(UNIT_NUMBER).toString().trim();
            
            personId = rowData.get(PERSON_ID).toString().trim();
            
            if(! unitNumber.equals(selUnitNumber) && ! unitNumber.equals(EMPTY)) {
                // Case# 3173_Bad message formatting when creating user- Start
//                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("user_details_exceptionCode.2554")+
//                fullName+coeusMessageResources.parseMessageKey("user_details_exceptionCode.2555")+unitNumber+coeusMessageResources.parseMessageKey("user_details_exceptionCode.2556")+
//                selUnitNumber+":"+selUnitName+"?"
//                ,CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                String unitName = "";
                if(rowData.get("UNIT_NAME") != null)
                    unitName = rowData.get("UNIT_NAME").toString().trim();
                String[] msgArgs ={fullName, unitNumber, unitName, selUnitNumber, selUnitName};
                MessageFormat formatter = new MessageFormat("");
                int selection = CoeusOptionPane.showQuestionDialog(
                        formatter.format(coeusMessageResources.parseMessageKey("user_details_exceptionCode.2554"), msgArgs),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_NO);
                // Case# 3173_Bad message formatting when creating user- End
                if(selection == CoeusOptionPane.SELECTION_NO) {
                    return ;
                }
            }
            
            if(txtUserId.getText().trim().length() > 0 ||
            txtUserName.getText().trim().length() > 0 ) {
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(OVERWRITE_USER_DATA), CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(selection == CoeusOptionPane.SELECTION_NO) {
                    return ;
                }
            }
            
            if(isUserDuplicate(userName)) {
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(USER_ALREADY_EXISTS), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    //System.out.println("Yes");
                    reset();
                    loadUserDetails(userName);
                    
                }
                else {
                    //System.out.println("No");
                }
            }
            
            txtUserId.setText(userName);
            txtUserName.setText(fullName);
            
            cmbStatus.setSelectedItem(ACTIVE);
            
            chkNonEmployee.setSelected(false);
        }
        
        /** listens to checkBox state change.
         * @param itemEvent ItemEvent.
         */
        public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
            Object src = itemEvent.getSource();
            
            if(! loading)dirty = true;
            
            if(src.equals(chkDoNotCreateDatabaseUser)) {
                Color bgColor=Color.white;
                //For Checkbox DoNotCreateNewDatabaseUser
                if(itemEvent.getStateChange() == itemEvent.SELECTED) {
                    pssWdPassword.setEnabled(false);
                    pssWdReEnterPassword.setEnabled(false);
                    bgColor = ((Color)UIManager.getDefaults().get("Panel.background"));
                }
                else if(itemEvent.getStateChange() == itemEvent.DESELECTED) {
                    pssWdPassword.setEnabled(true);
                    pssWdReEnterPassword.setEnabled(true);
                    bgColor = Color.white;
                }
                pssWdPassword.setBackground(bgColor);
                pssWdReEnterPassword.setBackground(bgColor);
            }
        }
        
        public void focusGained(FocusEvent focusEvent) {
        }
        
        public void focusLost(FocusEvent focusEvent) {
            //System.out.println("Focus Lost");
            //Don't Check Again Unnessasarily
            String userId = txtUserId.getText().trim();
            if(userId.equals(EMPTY)) return ;
            
            /** Added by chandra 21 sept 2004
             *Check if the entered user presents in any of the unit
             *If presents then throws the message else skip the condition
             */
            String existsUnitNumber= getUserForUnit(userId);
            if(existsUnitNumber!=null && !existsUnitNumber.equals(selUnitNumber)) {
                CoeusOptionPane.showInfoDialog("A user already exists with this id. ");
                txtUserId.setText(EMPTY);
                return ;
            }// End Chandra 21 Sept 2004
            
            if(isUserDuplicate(userId)) {
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(USER_ALREADY_EXISTS), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    reset();
                    loadUserDetails(userId);
                }
                else {
                    txtUserId.setText(EMPTY);
                }
            }
        }
        
    }//End Mediator
    
    //Table Cell Editor
    class RolesAssignedCellEditor extends DefaultCellEditor implements ActionListener {
        private JButton btnCellEditor;
        private String descend;//, descendAfterModification;
        private int row, column;
        
        RolesAssignedCellEditor() {
            super(new JComboBox());
            btnCellEditor = new JButton();
            btnCellEditor.addActionListener(this);
        }
        
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            descend = value.toString();
            this.row = row;
            this.column = column;
            
            if(descend.trim().equals(DESCEND_YES)) {
                btnCellEditor.setIcon(iIcnDescendY);
            }
            else {
                btnCellEditor.setIcon(iIcnDescendN);
            }
            return btnCellEditor;
        }
        
        public boolean stopCellEditing() {
            //This is required since the default implementation
            //removes any partially edited value.
            return true;
        }
        
        /** listens to action events.
         * @param actionEvent ActionEvent.
         */
        public void actionPerformed(ActionEvent actionEvent) {
            String masterDescend = allRoles.get(rolesAssignedTM.getValueAt(row, ROLE_ID_COLUMN)).toString().trim();
            if(masterDescend.equalsIgnoreCase(DESCEND_NO) && descend.equalsIgnoreCase(DESCEND_NO)) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DESCEND_FLAG_CANNOT_BE_CHANGED));
                return ;
            }
            else {
                if(descend.equalsIgnoreCase(DESCEND_YES)) {
                    rolesAssignedTM.setValueAt(DESCEND_NO, row, DESCEND_COLUMN);
                    btnCellEditor.setIcon(iIcnDescendN);
                }
                else {
                    rolesAssignedTM.setValueAt(DESCEND_YES, row, DESCEND_COLUMN);
                    btnCellEditor.setIcon(iIcnDescendY);
                }
                dirty = true;
            }
            cancelCellEditing();
        }//End actionPerformed
        
    }//End Table Cell Editor
    
    //Table Renderer
    class RolesCellRenderer extends DefaultTableCellRenderer{
        private JButton btnImageRenderer;
        private JLabel lblImage;
        private String descend, descendAfterModification;
        
        private char roleType, status;
        private Vector vecRowData;
        
        RolesCellRenderer() {
            btnImageRenderer = new JButton();
            lblImage = new JLabel();
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(column == DESCEND_COLUMN - 2) {
                //For Roles Assigned.
                if(table.equals(tblRolesNotAssigned)) {
                    //Added for Case 3608 - User Maintenance when add/update a user, there is no roles to be assigned in the pop up window
                    if(value != null && value.toString().trim().equals(DESCEND_YES)){
                        lblImage.setIcon(iIcnDescendY);
                    }
                    else {
                        lblImage.setIcon(iIcnDescendN);
                    }
                    return lblImage;
                }
                //for roles not assigned
                else if(table.equals(tblRolesAssigned)) {
                    descend = value.toString().trim();
                    if(descend.equals(DESCEND_NO)) {
                        btnImageRenderer.setIcon(iIcnDescendN);
                    }
                    else {
                        /*
                        if(modifiedRoles.containsKey(new Integer(row))) {
                            descendAfterModification = modifiedRoles.get(new Integer(row)).toString();
                            if(descendAfterModification.equalsIgnoreCase(DESCEND_YES)) {
                                btnImageRenderer.setIcon(iIcnDescendY);
                            }
                            else {
                                btnImageRenderer.setIcon(iIcnDescendN);
                            }
                        }
                        else{
                            //Descend is always YES
                            modifiedRoles.put(new Integer(row),DESCEND_NO);
                            btnImageRenderer.setIcon(iIcnDescendN);
                        }
                         */
                        btnImageRenderer.setIcon(iIcnDescendY);
                    }
                    
                    return btnImageRenderer;
                }
            }//End Descend Column Renderer
            else if(column == ROLE_TYPE_COLUMN - 1){
                vecRowData = (Vector)((DefaultTableModel)table.getModel()).getDataVector().get(row);
                status = vecRowData.get(ROLE_STATUS_COLUMN).toString().toUpperCase().trim().charAt(0);
                roleType = value.toString().toUpperCase().trim().charAt(0);
                switch(roleType) {
                    case ADMIN_UPPER_CASE:
                        if(status == ACTIVE_UPPER_CASE) lblImage.setIcon(iIcnAdminActive);
                        else if(status == INACTIVE_UPPER_CASE) lblImage.setIcon(iIcnAdminInactive);
                        break;
                    case PROPOSAL_UPPER_CASE:
                        if(status == ACTIVE_UPPER_CASE) lblImage.setIcon(iIcnProposalRoleActive);
                        else if(status == INACTIVE_UPPER_CASE) lblImage.setIcon(iIcnProposalRoleInactive);
                        break;
                    case SYSTEM_UPPER_CASE:
                        if(status == ACTIVE_UPPER_CASE) lblImage.setIcon(iIcnSystemActive);
                        else if(status == INACTIVE_UPPER_CASE) lblImage.setIcon(iIcnSystemInactive);
                        break;
                    case PROTOCOL_UPPER_CASE:
                        if(status == ACTIVE_UPPER_CASE) lblImage.setIcon(iIcnProtocolRoleActive);
                        else if(status == INACTIVE_UPPER_CASE) lblImage.setIcon(iIcnProtocolRoleInactive);
                        break;
                    //Added for IACUC Changes - Start
                    case IACUC_UPPER_CASE:
                        if(status == ACTIVE_UPPER_CASE) lblImage.setIcon(iIcnIacucRoleActive);
                        else if(status == INACTIVE_UPPER_CASE) lblImage.setIcon(iIcnIacucRoleInactive);
                        break;
                    //IACUC Changes - End
                }
                return lblImage;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
    }
    //End Table Renderer
    
    //Inner class Table Model
    class RolesAssignedTableModel extends CoeusTableModel {
        
        RolesAssignedTableModel(Class colTypes[]) {
            super(colTypes);
        }
        
        public boolean isCellEditable(int row, int column) {
            if(column == DESCEND_COLUMN) {
                return true;
            }
            return false;
        }
        
        public void addRow(Vector rowData) {
            if(! loading)dirty = true;
            
            if(rowData.get(DESCEND_COLUMN).toString().trim().equals(DESCEND_YES) && !loading) {
                rowData.set(DESCEND_COLUMN, DESCEND_NO);
            }
            //super.addRow(rowData);
            addSorted(rowData, ROLE_TYPE_COLUMN);
        }
        
        private void addSorted(Vector rowData, int columnToCompare) {
            int row=0, compare;
            //System.out.println(rowData.get(columnToCompare).getClass());
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - Start
//            String value, with;
//            value = (String)rowData.get(columnToCompare);
//            for(row = 0; row < getRowCount(); row++) {
//                with = (String)getValueAt(row, columnToCompare);
//                compare = with.compareToIgnoreCase(value);
//                if(compare > 0){
//                    break;
//                }
//            }
            dataVector.add(row, rowData);
            //Commented for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
            //sorting is taken care in loadRoles method part
            //Collections.sort(dataVector,new RoleTypeComparator());
            //Commented for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - End
        }
        
        /*
        private void addSorted(Vector rowData, int columnToCompare[]) {
            int row=0, compare[];
            //System.out.println(rowData.get(columnToCompare).getClass());
            String value[], with[];
            
            for(int col = 0; col < columnToCompare.length; col++) {
                value[col] = (String)rowData.get(columnToCompare[col]);
            }
            
            for(row = 0; row < getRowCount(); row++) {
                for(int col = 0; col < columnToCompare.length; col++) {
                    with[col] = (String)getValueAt(row, columnToCompare[col]);
                    compare[col] = with.compareToIgnoreCase(value[col]);
                }
                
                for(int col = 1; col < columnToCompare.length; col++) {
                    
                }
                
                //if(compare > 0){
                //    break;
                //}
                
            }
            dataVector.add(row, rowData);
        }
        */
    }//End Table Model
    
    //Inner class Table Model for Roles Not Assigned
    class RolesNotAssignedTableModel extends CoeusTableModel {
        RolesNotAssignedTableModel(Class colTypes[]) {
            super(colTypes);
        }
        
        public void addRow(Vector rowData) {
            if(! loading)dirty = true;
            
            rowData.set(DESCEND_COLUMN,allRoles.get((Integer)rowData.get(ROLE_ID_COLUMN)));
            
            if(rowData.get(ROLE_STATUS_COLUMN).toString().trim().toUpperCase().charAt(0) == INACTIVE_UPPER_CASE) {
                return ;
            }
            
            if(!hasOSPRight() && rowData.get(ROLE_TYPE_COLUMN).toString().trim().toUpperCase().charAt(0) == ADMIN_UPPER_CASE) {
                return ;
            }
            
            //super.addRow(rowData);
            addSorted(rowData, ROLE_TYPE_COLUMN);
        }
        
        private void addSorted(Vector rowData, int columnToCompare) {
            int row=0, compare;
            //System.out.println(rowData.get(columnToCompare).getClass());
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - Start
//            String value, with;
//            value = (String)rowData.get(columnToCompare);
//            for(row = 0; row < getRowCount(); row++) {
//                with = (String)getValueAt(row, columnToCompare);
//                compare = with.compareToIgnoreCase(value);
//                if(compare > 0){
//                    break;
//                }
//            }
            dataVector.add(row, rowData);
            //Commented for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
            //sorting is taken care in loadRoles method part
            //Collections.sort(dataVector,new RoleTypeComparator());
            //Commented for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - End
        }
        
        
        
    }
    //End Inner Class Table Model for Roles Not Assigned
    
    
    // Added for COEUSQA-3230 : Move IACUC protocol roles below - start
    class RoleTypeComparator implements Comparator{
        
        public int compare(Object role1, Object role2) {
            if(role1 == null || role2 == null ||
                    ((Vector)role1).isEmpty() || ((Vector)role2).isEmpty()) {
                return 0;
            }
            String roleType1 = (String)((Vector)role1).get(1);
            String roleType2 = (String)((Vector)role2).get(1);
            Integer beanPos1 = getPosition(roleType1);
            Integer beanPos2 = getPosition(roleType2);
            return beanPos1.compareTo(beanPos2);
            
        }
        
        /**
         * Method to get the display poistion
         * @param rightType
         * @return displayPosition
         */
        private Integer getPosition(String rightType) {
            Integer displayPosition = null;
            try {
                for (int i = 0; i < CoeusGuiConstants.roleRightSortOrder.length; i++) {
                    if (CoeusGuiConstants.roleRightSortOrder[i].equalsIgnoreCase(rightType)) {
                        displayPosition = new Integer(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return displayPosition;
        }
        
        
        
    }
    // Added for COEUSQA-3230 : Move IACUC protocol roles below - End    
    
    class UserDetailsFocusTraversalPolicy extends java.awt.FocusTraversalPolicy {
        java.awt.Component components[] = {txtUserId, txtUserName, cmbStatus, chkNonEmployee, pssWdPassword, pssWdReEnterPassword, chkDoNotCreateDatabaseUser};

        public java.awt.Component getComponentAfter(java.awt.Container container, java.awt.Component component) {
            for(int count = 0; count < components.length - 1; count++) {
                /*if(component.equals(components[count])) {
                    if(!components[count].isEnabled())
                        continue;
                    return components[count%components.length];
               }
            }
            return getDefaultComponent(container);
                 */
                if(component.equals(components[count])) {
                    if(components[count+1].isEnabled()) {
                    return components[count+1];
                    }
                    else return getComponentAfter(container, components[count+1]);
                }
            }
            return getDefaultComponent(container);
        }
        
        public java.awt.Component getComponentBefore(java.awt.Container container, java.awt.Component component) {
            for(int count = 1; count < components.length; count++) {
                if(component.equals(components[count])) {
                    if(components[count-1].isEnabled()) {
                        return components[count-1];
                    }
                    else return getComponentBefore(container, components[count-1]);
                }
            }
            return getComponentBefore(container, components[components.length-1]);
        }
        
        public java.awt.Component getDefaultComponent(java.awt.Container container) {
            //if(components[0].isEnabled())  return components[0];
            //return getComponentAfter(container, components[0]);
            return components[0];
        }
        
        public java.awt.Component getFirstComponent(java.awt.Container container) {
            return components[0];
        }
        
        public java.awt.Component getLastComponent(java.awt.Container container) {
            return components[components.length-1];
        }
        
    }
    
    //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
    //To sort the roles description Alphabetically
    class RoleDescComparator implements Comparator{
        
        public int compare(Object role1, Object role2) {
            if(role1 == null || role2 == null ||
                    ((Vector)role1).isEmpty() || ((Vector)role2).isEmpty()) {
                return 0;
            }
            String roleType1 = (String)((Vector)role1).get(4);
            String roleType2 = (String)((Vector)role2).get(4);
            return roleType1.toUpperCase().compareTo(roleType2.toUpperCase());
            
        }
    }
    //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
    
}//End User Details
