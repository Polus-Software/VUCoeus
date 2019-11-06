/*
 * UnitUserRolesDetailsController.java
 *
 * Created on May 18, 2011, 2:55 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 08-MAR-2012
 * by Bharati Umarani
 */

package edu.mit.coeus.user.controller;


import edu.mit.coeus.bean.RoleInfoBean;
//import edu.mit.coeus.bean.RoleRightInfoBean;
import edu.mit.coeus.bean.UnitUserRolesMaintenanceFormBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.user.bean.UserDetailsController;
import edu.mit.coeus.user.gui.UnitUserRolesDetailsForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.table.CoeusDnDTable;
import edu.mit.coeus.utils.table.CoeusTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author divyasusendran
 */
public class UnitUserRolesDetailsController extends Controller implements ActionListener,
        ListSelectionListener, ItemListener,FocusListener{
    
    private UnitUserRolesDetailsForm unitUserRolesDetailsForm;
    private CoeusMessageResources coeusMessageResources;
    private ImageIcon iIcnProposalRoleActive,iIcnProtocolRoleActive,iIcnAdminActive;
    private ImageIcon iIcnProposalRoleInactive,iIcnProtocolRoleInactive,iIcnAdminInactive,iIcnSystemActive;
    private ImageIcon iIcnSystemInactive,iIcnDescendY, iIcnDescendN;
    private ImageIcon iIcnIacucRoleActive, iIcnIacucRoleInactive;
    private CoeusDlgWindow dlgUnitUserRolesDetails;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private String selectedUser;
    private String loggedInUser;
    private String acType;
    private UserInfoBean userInfoBean;
    private String leadUnitName = CoeusGuiConstants.EMPTY_STRING;
    private static final char GET_UNIT_NAME = 'G';// this function type is used in Unit Servlet
    private static final char CHECK_USER_HAS_RIGHT = 'H';
    private static final char CHECK_IS_VALID_UNIT = 'J';
    private static final char GET_ALL_ROLES_FOR_USER = 'G';// this function type is used in User Maintenance Servlet
    private String CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL;
    private static final String USER_ROLES_SERVLET = "/userMaintenanceServlet";
    private static final String UNIT_SERVLET = "/unitServlet";
    private static final String MAINTAIN_USER_ROLE_RIGHT = "MAINTAIN_USER_ROLES";
    private static final String LABEL_DESCRIPTION_TEXT = "Roles for this user within ";
    private UserDetailsController userDetailsController;
    private CoeusTableModel rolesAssignedTM;
    private CoeusTableModel rolesNotAssignedTM;
    private Vector rolesForUnit, vecRolesAssigned,vecRolesNotAssigned,vecUnitUserRolesData;
    private HashMap hmAllRoles;
    private static final String DESCEND_YES = "Y";
    private static final String DESCEND_NO = "N";
    private static final String ASSIGNED_DESCEND_YES = "ASSIGNED_YES";
    private static final int ROLE_ID_COLUMN = 0;
    private static final int ROLE_TYPE_COLUMN = 1;
    private static final int ROLE_STATUS_COLUMN = 2;
    private static final int DESCEND_COLUMN = 3;
    private static final int ROLE_NAME_COLUMN = 4;
    private RolesCellRenderer cellRenderer;
    private RolesAssignedCellEditor cellEditor;
    private boolean dataModified = false;
    private static final char PROPOSAL_UPPER_CASE = 'P';
    private static final char PROTOCOL_UPPER_CASE = 'R';
    private static final char ADMIN_UPPER_CASE = 'O';
    private static final char SYSTEM_UPPER_CASE = 'S';
    private static final char ACTIVE_UPPER_CASE = 'A';
    private static final char IACUC_UPPER_CASE = 'I';
    private static final char INACTIVE_UPPER_CASE = 'I';
    private static final char DESCEND_YES_LOWER_CASE = 'y';
    private static final char DESCEND_YES_UPPER_CASE = 'Y';
    private static final char DESCEND_NO_LOWER_CASE = 'n';
    private static final char DESCEND_NO_UPPER_CASE = 'N';
    private int WIDTH = 790;
    private int HEIGHT = 400;
        
    /** Creates a new instance of UnitUserRolesDetailsController */
    public UnitUserRolesDetailsController(UserInfoBean userInfoBean,String loggedInUser,Vector vecUnitUserRolesData) {
        this.selectedUser = userInfoBean.getUserId();
        this.loggedInUser = loggedInUser;
        this.userInfoBean = userInfoBean;
        this.vecUnitUserRolesData = vecUnitUserRolesData;
        coeusMessageResources = CoeusMessageResources.getInstance();
        postInitComponents();
        registerComponents();
    }
    
    /**
     * To set the components before opening the screen
     * @return void
     */
    public void postInitComponents(){
        unitUserRolesDetailsForm = new UnitUserRolesDetailsForm();
        
        unitUserRolesDetailsForm.btnAdd.setEnabled(false);
        unitUserRolesDetailsForm.btnRemove.setEnabled(false);
        
        Component[] components = {unitUserRolesDetailsForm.btnOk,unitUserRolesDetailsForm.btnCancel,unitUserRolesDetailsForm.txtUnitNumber,unitUserRolesDetailsForm.btnUnitSearch };
        ScreenFocusTraversalPolicy screenTraversalPolicy = new ScreenFocusTraversalPolicy(components);
        unitUserRolesDetailsForm.setFocusTraversalPolicy(screenTraversalPolicy);
        unitUserRolesDetailsForm.setFocusCycleRoot(true);
        
        unitUserRolesDetailsForm.tblRolesAssigned.getTableHeader().setReorderingAllowed(false);
        unitUserRolesDetailsForm.tblRolesNotAssigned.getTableHeader().setReorderingAllowed(false);
        
        iIcnProposalRoleActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON));
        
        iIcnAdminActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ADMIN_ACTIVE_ROLE_ICON));
        
        iIcnProtocolRoleActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ACTIVE_ROLE_ICON));
        
        iIcnIacucRoleActive =  new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.IACUC_ACTIVE_ROLE_ICON));
        
        iIcnSystemActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SYSTEM_ACTIVE_ROLE_ICON));
        
        iIcnDescendN = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DESCEND_NO_ICON));
        
        iIcnDescendY = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DESCEND_YES_ICON));
        
        unitUserRolesDetailsForm.tblRolesAssigned.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        unitUserRolesDetailsForm.tblRolesNotAssigned.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        
        unitUserRolesDetailsForm.tblRolesAssigned.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        unitUserRolesDetailsForm.tblRolesNotAssigned.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        Class colTypes[] = {Integer.class, String.class, String.class, String.class, String.class};
        rolesAssignedTM = new RolesAssignedTableModel(colTypes);
        unitUserRolesDetailsForm.tblRolesAssigned.setModel(rolesAssignedTM);
        
        rolesNotAssignedTM = new RolesNotAssignedTableModel(colTypes);
        unitUserRolesDetailsForm.tblRolesNotAssigned.setModel(rolesNotAssignedTM);
        
        int columnWidths[] = {25,25,25,25,200};
        for(int column = 0; column < columnWidths.length; column++) {
            unitUserRolesDetailsForm.tblRolesNotAssigned.getColumnModel().getColumn(column).setPreferredWidth(columnWidths[column]);
            unitUserRolesDetailsForm.tblRolesAssigned.getColumnModel().getColumn(column).setPreferredWidth(columnWidths[column]);
        }
        unitUserRolesDetailsForm.tblRolesNotAssigned.setColumnVisibility(ROLE_ID_COLUMN,false);
        unitUserRolesDetailsForm.tblRolesAssigned.setColumnVisibility(ROLE_ID_COLUMN,false);
        
        // since one column is already not visible ( -1 )
        unitUserRolesDetailsForm.tblRolesAssigned.setColumnVisibility(ROLE_STATUS_COLUMN - 1, false);
        unitUserRolesDetailsForm.tblRolesNotAssigned.setColumnVisibility(ROLE_STATUS_COLUMN - 1, false);
        
        //Setting Cell Editor and Renderer
        cellRenderer = new RolesCellRenderer();
        
        unitUserRolesDetailsForm.tblRolesAssigned.getColumnModel().getColumn(ROLE_TYPE_COLUMN - 1).setCellRenderer(cellRenderer);
        unitUserRolesDetailsForm.tblRolesNotAssigned.getColumnModel().getColumn(ROLE_TYPE_COLUMN - 1).setCellRenderer(cellRenderer);
        
        cellEditor = new RolesAssignedCellEditor();
        
        unitUserRolesDetailsForm.tblRolesAssigned.getColumnModel().getColumn(DESCEND_COLUMN - 2).setCellEditor(cellEditor);
        unitUserRolesDetailsForm.tblRolesAssigned.getColumnModel().getColumn(DESCEND_COLUMN - 2).setCellRenderer(cellRenderer);
        unitUserRolesDetailsForm.tblRolesNotAssigned.getColumnModel().getColumn(DESCEND_COLUMN - 2).setCellRenderer(cellRenderer);
        
        //Don't show table headers
        unitUserRolesDetailsForm.tblRolesAssigned.setTableHeader(null);
        unitUserRolesDetailsForm.tblRolesNotAssigned.setTableHeader(null);
        
        unitUserRolesDetailsForm.tblRolesAssigned.setShowGrid(false);
        unitUserRolesDetailsForm.tblRolesNotAssigned.setShowGrid(false);
        
        dlgUnitUserRolesDetails = new CoeusDlgWindow(mdiForm);
        dlgUnitUserRolesDetails.setTitle("Roles for "+userInfoBean.getUserName());
        dlgUnitUserRolesDetails.setResizable(false);
        dlgUnitUserRolesDetails.setModal(true);
        dlgUnitUserRolesDetails.getContentPane().add(unitUserRolesDetailsForm);
        dlgUnitUserRolesDetails.setFont(CoeusFontFactory.getLabelFont());        
        dlgUnitUserRolesDetails.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgUnitUserRolesDetails.getSize();
        dlgUnitUserRolesDetails.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));        

        dlgUnitUserRolesDetails.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    close();
            }
        });
        
        dlgUnitUserRolesDetails.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgUnitUserRolesDetails.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                close();
            }
        });
    }
    
    /**
     * Registering the components
     * @return void
     */
    public void registerComponents() {
        unitUserRolesDetailsForm.btnAdd.addActionListener(this);
        unitUserRolesDetailsForm.btnRemove.addActionListener(this);
        unitUserRolesDetailsForm.btnUnitSearch.addActionListener(this);
        unitUserRolesDetailsForm.btnOk.addActionListener(this);
        unitUserRolesDetailsForm.btnCancel.addActionListener(this);
        unitUserRolesDetailsForm.txtUnitNumber.addFocusListener(this);
        
        unitUserRolesDetailsForm.tblRolesAssigned.getSelectionModel().addListSelectionListener(this);
        unitUserRolesDetailsForm.tblRolesNotAssigned.getSelectionModel().addListSelectionListener(this);      
        
    }
    
    /**
     * Method to check if the unit number is a valid unit number
     *@param unitNumber
     *@return boolean, true if the unit number is valid
     *@exception throws Exception
     */
    private boolean checkIfUnitNumberIsValid(String unitNumber) throws Exception{
        boolean isValid = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_IS_VALID_UNIT);
        requesterBean.setDataObject(unitNumber);
        ResponderBean response = sendToServer(USER_ROLES_SERVLET, requesterBean);
        if(!response.isSuccessfulResponse()){
            throw new Exception(response.getMessage());
        }else{
            if(response.getDataObject() != null){
                isValid = new Boolean(response.getDataObject().toString()).booleanValue();
            }
        }
        return isValid;
    }
    
    /**
     * Method to check if the user has MAINTAIN_USER_ROLES right at a given unit number
     *@param loggedInUser , user whom the right is checked
     *@param unitCode, is the unit at which the right is checked
     *@param right
     *@return boolean, true, if the user has the right at the unit
     *@exception throws Exception
     */
    private boolean checkUserHasRight(String loggedInUser,String unitCode,String right)throws Exception{
        boolean hasRight = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_USER_HAS_RIGHT);
        Vector vecRequest = new Vector();
        vecRequest.add(loggedInUser);
        vecRequest.add(unitCode);
        vecRequest.add(right);
        requesterBean.setDataObjects(vecRequest);
        ResponderBean response = sendToServer(USER_ROLES_SERVLET, requesterBean);
        if(!response.isSuccessfulResponse()){
            throw new Exception(response.getMessage());
        }else{
            if(response.getDataObject() != null){
                hasRight = new Boolean(response.getDataObject().toString()).booleanValue();
            }
        }
        return hasRight;
    }
    
    /**
     * Method to check if the unit already exists in the tree
     *@param unitCode, unit to be checked with
     *@return boolean, true if selected unit is an already existing unit
     */
    private boolean checkUnitExists(String unitCode) {
        boolean isUnitAlreadyPresent = false;
        if( vecUnitUserRolesData != null && !vecUnitUserRolesData.isEmpty()){
            for(Object unitObj : vecUnitUserRolesData){
                UnitUserRolesMaintenanceFormBean unitObject = (UnitUserRolesMaintenanceFormBean)unitObj;
                if(unitCode.equals(unitObject.getUnitNumber())){
                    isUnitAlreadyPresent = true;
                    break;
                }
            }
        }
        return isUnitAlreadyPresent;
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
     * Displays the GUI.
     */
    public void display() {
        unitUserRolesDetailsForm.btnOk.requestFocusInWindow();
        dlgUnitUserRolesDetails.setVisible(true);
    }
    
    /** 
     * Listens to action performed events.
     *@param actionEvent ActionEvent.
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(unitUserRolesDetailsForm.btnUnitSearch)){
            CoeusSearch coeusSearch;
            try {
                coeusSearch = new CoeusSearch(mdiForm, "leadunitsearch", CoeusSearch.TWO_TABS);
                coeusSearch.showSearchWindow();
                String unitCode = coeusSearch.getSelectedValue();
                if(unitCode==null)
                    return;
                if(!checkUserHasRight(loggedInUser,unitCode,MAINTAIN_USER_ROLE_RIGHT)){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1015"));// Selected user has no right
                    unitUserRolesDetailsForm.txtUnitNumber.setText(CoeusGuiConstants.EMPTY_STRING);
                    unitUserRolesDetailsForm.lblUnitName.setText(CoeusGuiConstants.EMPTY_STRING);
                    unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT);
                    
                    rolesAssignedTM.getDataVector().removeAllElements();
                    rolesAssignedTM.fireTableDataChanged();
                    rolesNotAssignedTM.getDataVector().removeAllElements();
                    rolesNotAssignedTM.fireTableDataChanged();
                    
                    return;
                }else{
                    // if checkUserHasRight() is true , logged in user has MAINTAIN_USER_ROLES right in the the searched unit
                    // 1) check if this unit number aleady exists
                    // 2) then display Roles add/modify form with the roles the user has in that unit
                    
                    if(checkUnitExists(unitCode)){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protoInvFrm_exceptionCode.1137"));// duplicate Unit
                        unitUserRolesDetailsForm.txtUnitNumber.setText(CoeusGuiConstants.EMPTY_STRING);
                        unitUserRolesDetailsForm.lblUnitName.setText(CoeusGuiConstants.EMPTY_STRING);
                        unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT);
                        
                        rolesAssignedTM.getDataVector().removeAllElements();
                        rolesAssignedTM.fireTableDataChanged();
                        rolesNotAssignedTM.getDataVector().removeAllElements();
                        rolesNotAssignedTM.fireTableDataChanged();
                        return;
                    }else{
                        UnitDetailFormBean unitInfoBean = getLeadUnit( unitCode );
                        if(unitInfoBean != null){
                            leadUnitName = unitInfoBean.getUnitName();
                            unitUserRolesDetailsForm.lblUnitName.setText(leadUnitName);
                            unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT+leadUnitName);
                        }
                        unitUserRolesDetailsForm.txtUnitNumber.setText(unitCode);
                        dataModified = true;
                        getRolesDetails(userInfoBean,unitCode);
                    }
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if(e.getSource().equals(unitUserRolesDetailsForm.btnAdd)){
            moveRows(unitUserRolesDetailsForm.tblRolesNotAssigned, unitUserRolesDetailsForm.tblRolesAssigned);
        }else if(e.getSource().equals(unitUserRolesDetailsForm.btnRemove)){
            moveRows(unitUserRolesDetailsForm.tblRolesAssigned, unitUserRolesDetailsForm.tblRolesNotAssigned);
        }else if(e.getSource().equals(unitUserRolesDetailsForm.btnOk)){
            saveUserDetails();
        }else if(e.getSource().equals(unitUserRolesDetailsForm.btnCancel)){
            close();
        }
    }
    
    /** 
     * Moves selected rows.
     * @param from from Table
     * @param to to Table.
     * @return void
     */
    private void moveRows(CoeusDnDTable from, CoeusDnDTable to) {
        Vector rowData;
        int selectedRows[] = from.getSelectedRows();
        if(selectedRows.length < 1) return ;
        
        dataModified = true;
        cellEditor.cancelCellEditing();
        
        for(int row = 0; row < selectedRows.length; row++) {
            rowData = ((CoeusTableModel)from.getModel()).getRow(selectedRows[row] - row);             
            ((CoeusTableModel)to.getModel()).addRow(rowData);
            ((CoeusTableModel)from.getModel()).removeRow(selectedRows[row] - row);
        }
        //Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
        Vector vecSortedRights = new Vector();
        Vector vecSortedRemovedRghts = new Vector();
        Vector vecSortRoles = ((CoeusTableModel)to.getModel()).getDataVector();
        vecSortedRights = getSortedRights(vecSortRoles);
        if(vecSortedRights != null && vecSortedRights.size() > 0){
            //Before assigning the sorted listt o model remove the existing data from datavector
            ((CoeusTableModel)to.getModel()).getDataVector().removeAllElements();
            for(int i = vecSortedRights.size()-1; i >= 0; i--){
                Vector vecData = (Vector) vecSortedRights.get(i);
                 //fix for COEUSQA-3929
                    if(vecData.get(DESCEND_COLUMN).toString().trim().equals(DESCEND_YES)) {
                        vecData.set(DESCEND_COLUMN, ASSIGNED_DESCEND_YES);
                     }
                  //fix for COEUSQA-3929
                ((CoeusTableModel)to.getModel()).addRow(vecData);
            }
        }
        //Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
        ((CoeusTableModel)to.getModel()).fireTableDataChanged();
        ((CoeusTableModel)from.getModel()).fireTableDataChanged();
        
        unitUserRolesDetailsForm.btnAdd.setEnabled(false);
        unitUserRolesDetailsForm.btnRemove.setEnabled(false);
    }

    
    
    /**
     * Contacts the server and fetches the Unit Details
     *@param unitNumber, for which the unit name has to be fetched
     *@return UnitDetailFormBean conatining the unit details
     *@exception throws Exception
     */
    public UnitDetailFormBean getLeadUnit( String unitNumber ) throws Exception{
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_UNIT_NAME);
        requester.setId( unitNumber );
        ResponderBean response = sendToServer(UNIT_SERVLET, requester);
        UnitDetailFormBean unitInfoBean = null;
        if ( response!=null ){
            unitInfoBean = (UnitDetailFormBean) response.getDataObject();
        }
        String leadUnit = unitUserRolesDetailsForm.txtUnitNumber.getText().trim();
        if( unitInfoBean == null || unitInfoBean.getUnitNumber() == null){
            unitInfoBean = null;
            if(!CoeusGuiConstants.EMPTY_STRING.equals(leadUnit)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protoInvFrm_exceptionCode.1138"));
                unitUserRolesDetailsForm.txtUnitNumber.setText(CoeusGuiConstants.EMPTY_STRING);
            }
        }
        return unitInfoBean;
    }
    
    /**
     * Get details of roles for a selected unit for a user
     *@param UserInfoBean containing user details
     *@param selUnitNumber, roles of the user at the selected unit 
     */
    private void getRolesDetails(UserInfoBean userInfoBean, String selUnitNumber){
        try{
            userDetailsController = new UserDetailsController();
            Vector response = (Vector)userDetailsController.read(userInfoBean.getUserId(), selUnitNumber);
            Vector userDetails = (Vector)response.get(0);
            vecRolesAssigned = new Vector();
            vecRolesAssigned = (Vector)userDetails.get(1);
            vecRolesNotAssigned =  new Vector();
            vecRolesNotAssigned = (Vector)userDetails.get(2);
            loadRolesDetails(userInfoBean.getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, selUnitNumber);
            loadRolesOntoTable(vecRolesAssigned, rolesAssignedTM);
            vecRolesNotAssigned = (Vector)checkOSPRoles(vecRolesNotAssigned);
            loadRolesOntoTable(vecRolesNotAssigned, rolesNotAssignedTM);
        }catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    /** 
     * Loads the roles to the Table.
     * @param vecRoles vector containing collection of RoleInfoBeans.
     * @param model Table Model of the Table in which these roles has to be displayed.
     */
    private void loadRolesOntoTable(Vector vecRoles, DefaultTableModel model) {
        RoleInfoBean roleInfoBean;
        Vector vecRowData ;
        int roleId;
        String roleName;
        char roleType, status;
        boolean descend;
        
        int size = model.getRowCount();
        if(size > 0 ){
            model.getDataVector().removeAllElements();
        }
       
        for(int index = 0; index < vecRoles.size(); index++) {
            //Load Roles.
            vecRowData = new Vector();
            roleInfoBean = (RoleInfoBean)vecRoles.get(index);
            roleId = roleInfoBean.getRoleId();
            roleType = roleInfoBean.getRoleType();
            status = roleInfoBean.getStatus();
            descend = roleInfoBean.isDescend();
            roleName = roleInfoBean.getRoleName();
            vecRowData.add(ROLE_ID_COLUMN, new Integer(roleId));
            vecRowData.add(ROLE_TYPE_COLUMN, CoeusGuiConstants.EMPTY_STRING+roleType);
            vecRowData.add(ROLE_STATUS_COLUMN, CoeusGuiConstants.EMPTY_STRING + status);
            String strDescend = "";
            if(descend) {
                vecRowData.add(DESCEND_COLUMN, ASSIGNED_DESCEND_YES);
                strDescend = DESCEND_YES;
            } else {
                vecRowData.add(DESCEND_COLUMN, DESCEND_NO);
                strDescend = DESCEND_NO;
            }
            // When roleId is not present in the HashMap hmAllRoles, will be added to the map
            if(hmAllRoles.get(new Integer(roleId)) == null){
                hmAllRoles.put(new Integer(roleId), strDescend);
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
                    }
                }
            }
            //Assign all the roles to the User maintenance
            if(vecOrderedColl != null && vecOrderedColl.size() > 0){
                //Before assigning the sorted listt o model remove the existing data from datavector
                model.getDataVector().removeAllElements();
                for(int i = vecOrderedColl.size()-1; i >= 0; i--){
                    Vector vecData = (Vector) vecOrderedColl.get(i);
                         //fix for COEUSQA-3929
                         if(vecData.get(DESCEND_COLUMN).toString().trim().equals(DESCEND_YES)) {
                            vecData.set(DESCEND_COLUMN, ASSIGNED_DESCEND_YES);
                         }
                         //fix for COEUSQA-3929
                         model.addRow(vecData);
                       }
                    }                   
                }                
        //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
        model.fireTableDataChanged();
    }
    
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
    
    /** 
     * loads roles details.
     * @param userId userId
     * @param allRolesUnitNumber unit number to get all Roles.
     * @param unitNumber searched unit number.
     */
    private void loadRolesDetails(String userId, String allRolesUnitNumber, String unitNumber) {
        try{
            userDetailsController = new UserDetailsController();
            Vector data = (Vector)userDetailsController.readRolesAndAuthorizations(userId, allRolesUnitNumber, unitNumber);
            Vector roles = (Vector)data.get(1);
            rolesForUnit = (Vector)data.get(2);
            //add to hashmap
            hmAllRoles = new HashMap();
            RoleInfoBean roleInfoBean;
            String strDescend;
            for(int index = 0; index < roles.size(); index++) {
                roleInfoBean = (RoleInfoBean)roles.get(index);
                if(roleInfoBean.isDescend()){
                    strDescend = DESCEND_YES;
                }else {
                    strDescend = DESCEND_NO;
                }
                hmAllRoles.put(new Integer(roleInfoBean.getRoleId()), strDescend);
            }
            if(rolesForUnit !=null && rolesForUnit.size() > 0 ){
                for(int index = 0; index < rolesForUnit.size(); index++) {
                    roleInfoBean = (RoleInfoBean)rolesForUnit.get(index);
                    if(hmAllRoles.get(new Integer(roleInfoBean.getRoleId())) == null){
                        if(roleInfoBean.isDescend()){
                            strDescend = DESCEND_YES;
                        }else{
                            strDescend = DESCEND_NO;
                        }
                        hmAllRoles.put(new Integer(roleInfoBean.getRoleId()), strDescend);
                    }
                }
            }
        }catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if(!listSelectionEvent.getValueIsAdjusting()) return ;
        ListSelectionModel listSelectionModel = (ListSelectionModel)listSelectionEvent.getSource();
        int selectedRow;
        if(unitUserRolesDetailsForm.tblRolesAssigned.getSelectionModel().equals(listSelectionModel)) {
            selectedRow = unitUserRolesDetailsForm.tblRolesAssigned.getSelectedRow();
            if(selectedRow == -1) return ;
            unitUserRolesDetailsForm.btnAdd.setEnabled(false);
            unitUserRolesDetailsForm.btnRemove.setEnabled(true);
            unitUserRolesDetailsForm.tblRolesNotAssigned.clearSelection();
            
        }else if(unitUserRolesDetailsForm.tblRolesNotAssigned.getSelectionModel().equals(listSelectionModel)) {
            unitUserRolesDetailsForm.btnAdd.setEnabled(true);
            unitUserRolesDetailsForm.btnRemove.setEnabled(false);
            unitUserRolesDetailsForm.tblRolesAssigned.clearSelection();
        }
    }
    
    /**
     * Method called upon Focus Lost
     *@param  FocusEvent
     */
    public void focusLost(FocusEvent e) {
        if(e.getSource().equals(unitUserRolesDetailsForm.txtUnitNumber)){
            
            try{
                
                UnitDetailFormBean unitInfoBean;
                String unitNumber = unitUserRolesDetailsForm.txtUnitNumber.getText().trim();
                // need to check if valid unit unit number
                if(!CoeusGuiConstants.EMPTY_STRING.equals(unitNumber)){
                    if(!checkIfUnitNumberIsValid(unitNumber)){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("commMntFrm_exceptionCode.1009"));// Unit entered is invalid
                        unitUserRolesDetailsForm.txtUnitNumber.setText(CoeusGuiConstants.EMPTY_STRING);
                        unitUserRolesDetailsForm.lblUnitName.setText(CoeusGuiConstants.EMPTY_STRING);
                        unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT);
                        
                        rolesAssignedTM.getDataVector().removeAllElements();
                        rolesAssignedTM.fireTableDataChanged();
                        rolesNotAssignedTM.getDataVector().removeAllElements();
                        rolesNotAssignedTM.fireTableDataChanged();

                        return;
                    }else{
                        
                        if(!checkUserHasRight(loggedInUser,unitNumber,MAINTAIN_USER_ROLE_RIGHT)){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1015"));// Selected user has no right
                            unitUserRolesDetailsForm.txtUnitNumber.setText(CoeusGuiConstants.EMPTY_STRING);
                            unitUserRolesDetailsForm.lblUnitName.setText(CoeusGuiConstants.EMPTY_STRING);
                            unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT);
                            
                            rolesAssignedTM.getDataVector().removeAllElements();
                            rolesAssignedTM.fireTableDataChanged();
                            rolesNotAssignedTM.getDataVector().removeAllElements();
                            rolesNotAssignedTM.fireTableDataChanged();
                            return;
                        }else{
                            
                            if(checkUnitExists(unitNumber)){
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protoInvFrm_exceptionCode.1137"));// duplicate unit
                                unitUserRolesDetailsForm.txtUnitNumber.setText(CoeusGuiConstants.EMPTY_STRING);
                                unitUserRolesDetailsForm.lblUnitName.setText(CoeusGuiConstants.EMPTY_STRING);
                                unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT);
                                
                                rolesAssignedTM.getDataVector().removeAllElements();
                                rolesAssignedTM.fireTableDataChanged();
                                rolesNotAssignedTM.getDataVector().removeAllElements();
                                rolesNotAssignedTM.fireTableDataChanged();
                                return;
                            }else{
                                
                                unitInfoBean = getLeadUnit( unitNumber );
                                if(unitInfoBean != null){
                                    leadUnitName = unitInfoBean.getUnitName();
                                    unitUserRolesDetailsForm.lblUnitName.setText(leadUnitName);
                                    unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT+leadUnitName);
                                }
                                unitUserRolesDetailsForm.txtUnitNumber.setText(unitNumber);
                                dataModified = true;
                                getRolesDetails(userInfoBean,unitNumber);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    //Table Renderer
    /**
     * Inner Class for rendering Roles tables
     */
    class RolesCellRenderer extends DefaultTableCellRenderer{
        private JButton btnImageRenderer;
        private JLabel lblImage;
        private String descend ;
        
        private char roleType, status;
        private Vector vecRowData;
        
        RolesCellRenderer() {
            btnImageRenderer = new JButton();
            lblImage = new JLabel();
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(column == DESCEND_COLUMN - 2) {
                //For Roles Assigned.
                if(table.equals(unitUserRolesDetailsForm.tblRolesNotAssigned)) {
                    
                    if(value != null && value.toString().trim().equals(DESCEND_YES)){                    
                        lblImage.setIcon(iIcnDescendY);
                    } else {
                        lblImage.setIcon(iIcnDescendN);
                    }
                    return lblImage;
                }
                //for roles not assigned
                else if(table.equals(unitUserRolesDetailsForm.tblRolesAssigned)) {
                    descend = value.toString().trim();

                    if(DESCEND_NO.equals(descend)) {
                        btnImageRenderer.setIcon(iIcnDescendN);
                    } else {
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
                    case IACUC_UPPER_CASE:
                        if(status == ACTIVE_UPPER_CASE) lblImage.setIcon(iIcnIacucRoleActive);
                        else if(status == INACTIVE_UPPER_CASE) lblImage.setIcon(iIcnIacucRoleInactive);
                        break;
                        
                }
                return lblImage;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
    }
    //End Table Renderer
    
    
    /**
     * Inner Class for Cell Editor Roles tables
     */
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
            } else {
                btnCellEditor.setIcon(iIcnDescendN);
            }
            return btnCellEditor;
        }
        
        public boolean stopCellEditing() {
            //This is required since the default implementation
            //removes any partially edited value.
            return true;
        }
        
        /**
         * Listens to action performed events.
         *@param actionEvent ActionEvent.
         */
        public void actionPerformed(ActionEvent actionEvent) {
            String masterDescend = hmAllRoles.get(rolesAssignedTM.getValueAt(row, ROLE_ID_COLUMN)).toString().trim();
            if(masterDescend.equalsIgnoreCase(DESCEND_NO) && descend.equalsIgnoreCase(DESCEND_NO)) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("user_details_exceptionCode.2545"));
                return ;
            } else {
                if(descend.equalsIgnoreCase(DESCEND_YES)) {
                    dataModified = true;
                    rolesAssignedTM.setValueAt(DESCEND_NO, row, DESCEND_COLUMN);
                    btnCellEditor.setIcon(iIcnDescendN);
                } else {
                    dataModified = true;
                    rolesAssignedTM.setValueAt(DESCEND_YES, row, DESCEND_COLUMN);
                    btnCellEditor.setIcon(iIcnDescendY);
                }
            }
            cancelCellEditing();
        }//End actionPerformed
        
    }//End Table Cell Editor
    
    //Inner class Table Model
    /**
     * Inner Class for Table Model for Roles Assigned Table
     */
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
            if(rowData.get(DESCEND_COLUMN).toString().trim().equals(DESCEND_YES)) {
                rowData.set(DESCEND_COLUMN, DESCEND_NO);
            }else if(rowData.get(DESCEND_COLUMN).toString().trim().equals(ASSIGNED_DESCEND_YES)){
                rowData.set(DESCEND_COLUMN, DESCEND_YES);
            }
            addSorted(rowData, ROLE_TYPE_COLUMN);
        }
        
        private void addSorted(Vector rowData, int columnToCompare) {
            int row=0, compare;
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
            //sorting is taken care in loadRolesOntoTable method part
            //Collections.sort(dataVector,new RoleTypeComparator());
            //Commented for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - End
        }
    }//End Table Model
    
    //Inner class Table Model for Roles Not Assigned
    /**
     * Inner Class for Table Model for Roles Not Assigned Table
     */
    class RolesNotAssignedTableModel extends CoeusTableModel {
        RolesNotAssignedTableModel(Class colTypes[]) {
            super(colTypes);
        }
        
        public void addRow(Vector rowData) {
            
            rowData.set(DESCEND_COLUMN,hmAllRoles.get((Integer)rowData.get(ROLE_ID_COLUMN)));
            if(rowData.get(ROLE_STATUS_COLUMN).toString().trim().toUpperCase().charAt(0) == INACTIVE_UPPER_CASE) {
                return ;
            }
            addSorted(rowData, ROLE_TYPE_COLUMN);
        }
        
        private void addSorted(Vector rowData, int columnToCompare) {
            int row=0, compare;
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
            //sorting is taken care in loadRolesOntoTable method part
            //Collections.sort(dataVector,new RoleTypeComparator());
            //Commented for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - End
        }

    }
    //End Inner Class Table Model for Roles Not Assigned
    
    /**
     *Load User Roles Details for selected Unit Number
     *@param selUnitNumber
     *@exception throws exception
     */
    public void loadUserDetails(String selUnitNumber) throws Exception {
        setAcType(TypeConstants.UPDATE_RECORD);
        unitUserRolesDetailsForm.btnUnitSearch.setEnabled(false);
        unitUserRolesDetailsForm.txtUnitNumber.setEditable(false);
        unitUserRolesDetailsForm.txtUnitNumber.setEnabled(false);
        unitUserRolesDetailsForm.txtUnitNumber.setText(selUnitNumber);
        UnitDetailFormBean unitInfoBean = getLeadUnit( selUnitNumber );
        if(unitInfoBean != null){
            String unitName = unitInfoBean.getUnitName();
            unitUserRolesDetailsForm.lblUnitName.setText(unitName);
            unitUserRolesDetailsForm.lblRolesWithinUnit.setText(LABEL_DESCRIPTION_TEXT+unitName);
        }
        userDetailsController = new UserDetailsController();
        Vector response = (Vector)userDetailsController.read(userInfoBean.getUserId(), selUnitNumber);
        Vector userDetails = (Vector)response.get(0);
        vecRolesAssigned = new Vector();
        vecRolesAssigned = (Vector)userDetails.get(1);
        vecRolesNotAssigned =  new Vector();
        vecRolesNotAssigned = (Vector)userDetails.get(2);
        loadRolesDetails(userInfoBean.getUserId(), CoeusGuiConstants.INSTITUTE_UNIT_NUMBER, selUnitNumber);
        loadRolesOntoTable(vecRolesAssigned, rolesAssignedTM);
        vecRolesNotAssigned = (Vector)checkOSPRoles(vecRolesNotAssigned);
        loadRolesOntoTable(vecRolesNotAssigned, rolesNotAssignedTM);
    }
    
    /**
     * Load Roles
     */
    public void loadUserRoles() {
        setAcType(TypeConstants.INSERT_RECORD);
        unitUserRolesDetailsForm.btnUnitSearch.setEnabled(true);
        unitUserRolesDetailsForm.txtUnitNumber.setEditable(true);
        unitUserRolesDetailsForm.txtUnitNumber.setEnabled(true);
    }
    
    /**
     * Validate data
     *@return boolean, false if invalid data is found
     */
    public boolean validateData(){
        boolean validData =  true;
        if(CoeusGuiConstants.EMPTY_STRING.equals(unitUserRolesDetailsForm.txtUnitNumber.getText().trim())){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("commMntFrm_exceptionCode.1009"));// Unit entered is invalid
            validData = false;
        }
        return validData;
    }
    
    /**
     * validate and save user role details
     */
    public void saveUserDetails() {
        if(!validateData()) return;
        if(TypeConstants.INSERT_RECORD.equals(acType)) {
            int assignedRowCount = unitUserRolesDetailsForm.tblRolesAssigned.getRowCount();
            if(assignedRowCount <= 0){
                dlgUnitUserRolesDetails.dispose();
                return;
            }
        }
        
        Vector vecUserRoles  = new Vector();
        userDetailsController = new UserDetailsController();
        Vector assignedTableRights = ((DefaultTableModel)unitUserRolesDetailsForm.tblRolesAssigned.getModel()).getDataVector();
        RoleInfoBean roleInfoBean = null;
        Vector rowData = null;
        if(TypeConstants.INSERT_RECORD.equals(acType)) {
            
            for(int i = 0; i < unitUserRolesDetailsForm.tblRolesAssigned.getRowCount(); i++){
                roleInfoBean = new RoleInfoBean();
                rowData = (Vector)assignedTableRights.get(i);
                roleInfoBean.setAcType(TypeConstants.INSERT_RECORD);
                char roleTy = rowData.get(ROLE_TYPE_COLUMN).toString().trim().charAt(0);
                if(roleTy == 'O'){
                    roleInfoBean.setUnitNumber(getCampusForDept(unitUserRolesDetailsForm.txtUnitNumber.getText().trim()));
                }else{
                    roleInfoBean.setUnitNumber(unitUserRolesDetailsForm.txtUnitNumber.getText().trim());
                }
                roleInfoBean.setRoleId(((Integer)rowData.get(ROLE_ID_COLUMN)).intValue());
                char descend = rowData.get(DESCEND_COLUMN).toString().trim().charAt(0);
                if(descend == DESCEND_YES_LOWER_CASE || descend == DESCEND_YES_UPPER_CASE) {
                    roleInfoBean.setDescend(true);
                } else if(descend == DESCEND_NO_LOWER_CASE || descend == DESCEND_NO_UPPER_CASE) {
                    roleInfoBean.setDescend(false);
                }
                roleInfoBean.setRoleName(rowData.get(ROLE_NAME_COLUMN).toString().trim());
                roleInfoBean.setRoleType(rowData.get(ROLE_TYPE_COLUMN).toString().trim().charAt(0));
                roleInfoBean.setUserId(userInfoBean.getUserId());
                roleInfoBean.setUpdateUser(mdiForm.getUserId());
                vecUserRoles.add(roleInfoBean);
            }
            try{
                Vector vecDbUser = null;
                vecUserRoles = (Vector)checkOSPRoles(vecUserRoles);
                userDetailsController.create(userInfoBean, vecUserRoles, vecDbUser);
            }catch (CoeusClientException coeusClientException) {
                CoeusOptionPane.showDialog(coeusClientException);
            }catch (Exception exception) {
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        } //Insert Roles
        else  if(TypeConstants.UPDATE_RECORD.equals(acType)) {
            
            boolean found = false;
            for(int i=0; i  < unitUserRolesDetailsForm.tblRolesAssigned.getRowCount(); i++){
                rowData = (Vector)assignedTableRights.get(i);
                found = false;
                for(int j = 0; j < vecRolesAssigned.size(); j++){
                    roleInfoBean = (RoleInfoBean)vecRolesAssigned.get(j);
                    if(roleInfoBean.getRoleId()==(new Integer(rowData.get(0).toString()).intValue())){
                        found = true;
                        break;
                    }
                }
                boolean newDescendFlag = false;
                if(rowData.get(DESCEND_COLUMN).toString().equalsIgnoreCase(DESCEND_YES)){
                    newDescendFlag = true;
                }
                if(!found){
                    roleInfoBean = new RoleInfoBean();
                    roleInfoBean.setAcType("I");
                    roleInfoBean.setRoleId(Integer.parseInt(rowData.get(ROLE_ID_COLUMN).toString()));
                    roleInfoBean.setRoleName(rowData.get(ROLE_NAME_COLUMN).toString().trim());
                    roleInfoBean.setRoleType(rowData.get(ROLE_TYPE_COLUMN).toString().trim().charAt(0));
                    roleInfoBean.setStatus(rowData.get(ROLE_STATUS_COLUMN).toString().trim().charAt(0));
                    roleInfoBean.setUserId(userInfoBean.getUserId());
                    roleInfoBean.setUpdateUser(mdiForm.getUserId());
                    char descend = rowData.get(DESCEND_COLUMN).toString().charAt(0);
                    if(descend == DESCEND_YES_LOWER_CASE || descend == DESCEND_YES_UPPER_CASE) {
                        roleInfoBean.setDescend(true);
                    } else if(descend == DESCEND_NO_LOWER_CASE || descend == DESCEND_NO_UPPER_CASE) {
                        roleInfoBean.setDescend(false);
                    }
                    char roleTy = rowData.get(ROLE_TYPE_COLUMN).toString().trim().charAt(0);
                    if(roleTy == 'O'){
                        roleInfoBean.setUnitNumber(getCampusForDept(unitUserRolesDetailsForm.txtUnitNumber.getText().trim()));
                    }else{
                        roleInfoBean.setUnitNumber(unitUserRolesDetailsForm.txtUnitNumber.getText().trim());
                    }
                    vecUserRoles.add(roleInfoBean);
                }else{
                    if(newDescendFlag != roleInfoBean.isDescend()){
                        roleInfoBean.setDescend(newDescendFlag);
                        roleInfoBean.setRoleId(Integer.parseInt(rowData.get(ROLE_ID_COLUMN).toString()));
                        roleInfoBean.setRoleName(rowData.get(ROLE_NAME_COLUMN).toString().trim());
                        roleInfoBean.setRoleType(rowData.get(ROLE_TYPE_COLUMN).toString().trim().charAt(0));
                        roleInfoBean.setStatus(rowData.get(ROLE_STATUS_COLUMN).toString().trim().charAt(0));
                        roleInfoBean.setUserId(userInfoBean.getUserId());
                        roleInfoBean.setUpdateUser(mdiForm.getUserId());
                        char roleTy = rowData.get(ROLE_TYPE_COLUMN).toString().trim().charAt(0);
                        if(roleTy == 'O'){
                            roleInfoBean.setUnitNumber(getCampusForDept(unitUserRolesDetailsForm.txtUnitNumber.getText().trim()));
                        }else{
                            roleInfoBean.setUnitNumber(unitUserRolesDetailsForm.txtUnitNumber.getText().trim());
                        }
                        roleInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                        vecUserRoles.add(roleInfoBean);
                    }
                }
            }
            
            for(int i = 0; i < vecRolesAssigned.size(); i++){
                found = false;
                roleInfoBean = (RoleInfoBean)vecRolesAssigned.get(i);
                for(int j = 0; j  < unitUserRolesDetailsForm.tblRolesAssigned.getRowCount(); j++){
                    rowData = (Vector)assignedTableRights.get(j);
                    if(roleInfoBean.getRoleId()==(new Integer(rowData.get(ROLE_ID_COLUMN).toString()).intValue())){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    roleInfoBean.setAcType(TypeConstants.DELETE_RECORD);
                    roleInfoBean.setUserId(userInfoBean.getUserId());
                    roleInfoBean.setUpdateUser(mdiForm.getUserId());
                    vecUserRoles.add(roleInfoBean);
                }
            }
            try{
                if((vecUserRoles!= null && vecUserRoles.size() >0)){
                    userDetailsController.update(userInfoBean, vecUserRoles);
                }
                
            }catch (CoeusClientException coeusClientException) {
                CoeusOptionPane.showDialog(coeusClientException);
            }catch (Exception exception){
                CoeusOptionPane.showErrorDialog(exception.getMessage());
                
            }
        }//Update Roles
        dlgUnitUserRolesDetails.dispose();
    }
    
    /** 
     * Creates New Role Info Bean from the Row Of the RoleAssigned Table
     * @param row row of RoleAssigned table from which the RoleInfoBean has to be created.
     * @param type INSERT, UPDATE or DELETE
     * @return created RoleInfoBean
     */
    private RoleInfoBean createRoleInfoBean(int row, String type) {
        int roleId;
        char descend;
        RoleInfoBean roleInfoBean;
        if(type == TypeConstants.UPDATE_RECORD || type == TypeConstants.DELETE_RECORD) {
            roleInfoBean = (RoleInfoBean)vecRolesAssigned.get(row);
        } else {
            roleInfoBean = new RoleInfoBean();
        }
        char roleTy = rolesAssignedTM.getValueAt(row, ROLE_TYPE_COLUMN).toString().trim().charAt(0);
        if(roleTy == 'O'){
            roleInfoBean.setUnitNumber(getCampusForDept(unitUserRolesDetailsForm.txtUnitNumber.getText().trim()));
        }else{
            roleInfoBean.setUnitNumber(unitUserRolesDetailsForm.txtUnitNumber.getText().trim());
        }
        roleId = ((Integer)rolesAssignedTM.getValueAt(row, ROLE_ID_COLUMN)).intValue();
        roleInfoBean.setRoleId(roleId);
        descend = rolesAssignedTM.getValueAt(row, DESCEND_COLUMN).toString().trim().charAt(0);
        if(descend == DESCEND_YES_LOWER_CASE || descend == DESCEND_YES_UPPER_CASE) {
            roleInfoBean.setDescend(true);
        } else if(descend == DESCEND_NO_LOWER_CASE || descend == DESCEND_NO_UPPER_CASE) {
            roleInfoBean.setDescend(false);
        }
        roleInfoBean.setRoleName(rolesAssignedTM.getValueAt(row, ROLE_NAME_COLUMN).toString().trim());
        roleInfoBean.setRoleType(rolesAssignedTM.getValueAt(row, ROLE_TYPE_COLUMN).toString().trim().charAt(0));
        roleInfoBean.setUserId(userInfoBean.getUserId());
        roleInfoBean.setUpdateUser(mdiForm.getUserId());
        roleInfoBean.setAcType(acType);
        
        return roleInfoBean;
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
     *Check for OSP level Roles which have been added
     *@param Vector of roles assigned in RolesAssignedTable
     *@return Vector of roles after removal of duplicate OSP level roles
     *@exception throws Exception
     */
    public Vector checkOSPRoles(Vector vecUserRoles) throws Exception{
        Vector vecFinalRoles = new Vector();
        Vector vecChkRoles =  new Vector();
        vecChkRoles.addAll(vecUserRoles);
        Vector vecUnitUserRolesData = (Vector)getUnitUserRoles(userInfoBean.getUserId());
        vecUnitUserRolesData = vecUnitUserRolesData == null ? new Vector():vecUnitUserRolesData;
        if(vecUnitUserRolesData != null && vecUnitUserRolesData.size() > 0){
            for(int i = 0 ; i < vecUnitUserRolesData.size() ; i++){
                UnitUserRolesMaintenanceFormBean unitFormBean =
                        (UnitUserRolesMaintenanceFormBean)vecUnitUserRolesData.get(i);
                if(unitFormBean.getVecRoles() != null && unitFormBean.getVecRoles().size()>0){
                    for(int j = 0 ; j < unitFormBean.getVecRoles().size() ; j++){
                        RoleInfoBean unitRoleBean =(RoleInfoBean)unitFormBean.getVecRoles().get(j);
                        if(vecChkRoles != null && vecChkRoles.size() > 0){
                            for(int k = 0 ; k <vecChkRoles.size() ; k++){
                                RoleInfoBean roleInfoBean =(RoleInfoBean)vecChkRoles.get(k);
                                if(roleInfoBean.getRoleType() == 'O'){
                                    if(roleInfoBean.getRoleId() == unitRoleBean.getRoleId()){
                                        vecChkRoles.remove(roleInfoBean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        vecFinalRoles.addAll(vecChkRoles);
        return vecFinalRoles;
    }
    
    /** 
     * Pass the unit number to get the top level unit while
     * updating the system level roles in another unit.
     *@param unit number which is selected from a specific unit
     *@return the top level unit 
     */
    public String getCampusForDept(String unitNumber) {
        String topLevelUnit = CoeusGuiConstants.EMPTY_STRING;
        try{
//            if(unitNumber!= null && !(unitNumber.equals(CoeusGuiConstants.EMPTY_STRING))){
            if(!CoeusGuiConstants.EMPTY_STRING.equals(unitNumber)){
                userDetailsController = new UserDetailsController();
                topLevelUnit = userDetailsController.getCampusForDept(unitNumber);
            }
        }catch (CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusClientException.getMessage());
        }
        return topLevelUnit;
    }
    
    
    /** 
     * Closes the User Details Dialog.
     * checks if any modification is done and asks user
     * whether to save or discard.
     */
    public void close() {
        if(dataModified) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("user_details_exceptionCode.2547"),
                                                               CoeusOptionPane.OPTION_YES_NO_CANCEL,
                                                               CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                saveUserDetails();
            } else{
                dlgUnitUserRolesDetails.dispose();
            }
        } else {
            dlgUnitUserRolesDetails.dispose();
        }
        
    }
    
    /** 
     *Getter for property acType.
     *@return Value of property acType.
     */
    public String getAcType() {
        return acType;
    }
    
    /** 
     *Setter for property acType.
     *@param acType New value of property acType.
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public Component getControlledUI() {
        return null;
    }
    
    public void setFormData(Object data) throws CoeusException {
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void formatFields() {
    }
    
    public boolean validate() throws CoeusUIException {
        return false;//change
    }
    
    public void saveFormData() throws CoeusException {
    }
    
    public void itemStateChanged(ItemEvent e) {
    }
    
    public void focusGained(FocusEvent e) {
    }
    
    //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
    /**
     * Method used to sort the rights in alphabatical order and based on right types
     * @param vecAvailableTableRights  vecSortRoles
     */
    private Vector getSortedRights(Vector vecSortRoles){
        Vector vecOrderedRights = new Vector();
        HashMap hmAvailableRights = new HashMap();
        if(vecSortRoles != null && !vecSortRoles.isEmpty()){
            for(int index=0;index<vecSortRoles.size();index++){
                String rightTypeDesc = (String)((Vector)vecSortRoles.get(index)).get(ROLE_TYPE_COLUMN);
                if(hmAvailableRights.get(rightTypeDesc) != null){
                    Vector vecRoleData = (Vector)hmAvailableRights.get(rightTypeDesc);
                    vecRoleData.add(vecSortRoles.get(index));
                    hmAvailableRights.put(rightTypeDesc,vecRoleData);
                }else{
                    Vector vecRoleData = new Vector();
                    vecRoleData.add(vecSortRoles.get(index));
                    hmAvailableRights.put(rightTypeDesc,vecRoleData);
                }
            }
        }
        if(!hmAvailableRights.isEmpty()){
            Iterator roleIterator = hmAvailableRights.keySet().iterator();
            while(roleIterator.hasNext()){
                String rightTypeDesc = (String)roleIterator.next();
                Vector vecRightData = (Vector)hmAvailableRights.get(rightTypeDesc);
                //sorting the collection based on the role name
                if(vecRightData != null && vecRightData.size() > 0){
                    Collections.sort(vecRightData,new RoleDescComparator());
                }
                hmAvailableRights.put(rightTypeDesc,vecRightData);
            }
            //sorting the right based on righttypes
            for (int i = 0; i < CoeusGuiConstants.roleRightSortOrder.length; i++) {
                String rightTypeDesc = CoeusGuiConstants.roleRightSortOrder[i];
                if(hmAvailableRights.get(rightTypeDesc) != null){
                    vecOrderedRights.addAll((Vector)hmAvailableRights.get(rightTypeDesc));
                }
            }
        }
        return vecOrderedRights;
    }
    
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
}
