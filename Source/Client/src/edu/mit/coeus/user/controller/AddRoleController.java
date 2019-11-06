/*
 * @(#)AddRoleController.java 1.0 7/25/07
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
import edu.mit.coeus.bean.RoleRightInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.user.gui.AddRoleForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.table.CoeusDnDTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
//import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * This class is used as the controller for the form edu.mit.coeus.user.gui.AddRoleForm
 *
 * @author leenababu
 */
public class AddRoleController extends Controller implements ActionListener,
        ListSelectionListener, ItemListener{
    
    private CoeusDlgWindow dlgWindow;
    private AddRoleForm addRoleForm;
    private RoleInfoBean roleInfoBean;
    private String acType = "I";
    
    private String unitNumber;
    private String unitName;
    private String prevRoleType = "";
    private boolean refreshRequired = false;
    
    private Vector vecAvailableRights;
    private Vector vecAssignedRights;
    private Map hmAllRights;
    private Map hmTypeRights;
    private Vector vecFinalAssignedRights;
    private boolean populatedAssignedRighs = false;
    
    private int RIGHT_TYPE_COLUMN = 0;
    private int DESCEND_COLUMN = 1;
    private int RIGHT_ID_COLUMN = 2;
    private Vector vecColumnNames = new Vector();
    
    private static final char PROPOSAL_UPPER_CASE = 'P';
    private static final char PROTOCOL_UPPER_CASE = 'R';
    private static final char ADMIN_UPPER_CASE = 'O';
    private static final char SYSTEM_UPPER_CASE = 'S';
    //Added for IACUC Changes - start
    private static final String IACUC_RIGHT_TYPE = "I";
    private static final char IACUC_UPPER_CASE = 'I';
    private static final char REPORT_UPPER_CASE = 'T';
    private static final String DESCEND_YES = "Y";
    private static final String DESCEND_NO = "N";
    
    private ImageIcon iIcnProposalRoleActive;
    private ImageIcon iIcnProtocolRoleActive;
    private ImageIcon iIcnIacucLevelRight;
    private ImageIcon iIcnAdminActive;
    private ImageIcon iIcnSystemActive;
    private ImageIcon iIcnDescendY, iIcnDescendN;
    
    private String connecto = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.USER_SERVLET;
    private static final char GET_ROLE_DETAILS = 'B';
    private static final char GET_NEXT_ROLE_ID = 'C';
    private static final char SAVE_ROLE_DETAILS = 'D';
    private static final char RIGHT_CHECK = 'E';
    
    private int WIDTH = 790;
    private int HEIGHT = 500;
    private CoeusMessageResources coeusMessageResources;
    
    
    
    /** Creates a new instance of AddRoleController */
    public AddRoleController(String unitNumber, String unitName){
        this.unitNumber = unitNumber;
        this.unitName = unitName;
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    /** Creates a new instance of AddRoleController */
    public AddRoleController(String unitNumber, String unitName, RoleInfoBean roleInfoBean)
    throws CoeusException{
        this.unitNumber = unitNumber;
        this.unitName = unitName;
        this.roleInfoBean = roleInfoBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        postInitComponents();
        registerComponents();
        getRoleRightDetails();
        setFormData(null);
    }
    
    public void postInitComponents(){
        if(roleInfoBean!=null){
            acType = "U";
        }
        vecColumnNames.add(" ");
        vecColumnNames.add(" ");
        vecColumnNames.add("Right Id");
        
        addRoleForm = new AddRoleForm();
        addRoleForm.tblAssignedRights.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        addRoleForm.tblAvailableRights.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        
        addRoleForm.btnAdd.setEnabled(false);
        addRoleForm.btnRemove.setEnabled(false);
        
        JTextFieldFilter descriptionFilter = new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,200 );
        descriptionFilter.AddAcceptedCharacters(" `~!@#$%^&*()_+{}|:<>?;',./\"");
        addRoleForm.txtDescription.setDocument(descriptionFilter);
        
        JTextFieldFilter nameFilter = new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,50);
        nameFilter.AddAcceptedCharacters(" _.:");
        addRoleForm.txtName.setDocument(nameFilter);
        
        Component[] components = {addRoleForm.txtName, addRoleForm.txtDescription,
        addRoleForm.cmbType, addRoleForm.chkDescend, addRoleForm.cmbStatus,addRoleForm.btnOk,
        addRoleForm.btnCancel };
        ScreenFocusTraversalPolicy screenTraversalPolicy = new ScreenFocusTraversalPolicy(components);
        addRoleForm.setFocusTraversalPolicy(screenTraversalPolicy);
        addRoleForm.setFocusCycleRoot(true);
        populateComboBoxes();
        addRoleForm.tblAssignedRights.getTableHeader().setReorderingAllowed(false);
        addRoleForm.tblAvailableRights.getTableHeader().setReorderingAllowed(false);
        
        iIcnProposalRoleActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON));
        
        iIcnAdminActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ADMIN_ACTIVE_ROLE_ICON));
        
        iIcnProtocolRoleActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ACTIVE_ROLE_ICON));
        //Added for IACUC Changes - Start
        iIcnIacucLevelRight =  new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.IACUC_ACTIVE_ROLE_ICON));
        //IACUC Changes - End
        iIcnSystemActive = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SYSTEM_ACTIVE_ROLE_ICON));
        
        iIcnDescendN = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DESCEND_NO_ICON));
        
        iIcnDescendY = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DESCEND_YES_ICON));
    }
    
    /**
     * Set the item selected with the given code for the given combobox
     * @param code the code which need to set selected.
     * @param combobox the combobox in which the selection is to be set
     */
    public void setComboBoxSelection(String code, JComboBox comboBox){
        ComboBoxBean comboBoxBean = null;
        for(int i=0; i<comboBox.getModel().getSize(); i++){
            comboBoxBean = (ComboBoxBean)comboBox.getItemAt(i);
            if(comboBoxBean.getCode().equals(code)){
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }
    
    /**
     * Checks whether the logged in user has the maintain roles rights
     * @return boolean true if the user has rights else false
     */
    public boolean canUserMaintainRole(){
        boolean canMaintainRole = false;
        RequesterBean requesterBean = new RequesterBean();
        Vector serverDataObject = new Vector();
        serverDataObject.add(unitNumber);
        serverDataObject.add(unitName);
        serverDataObject.add("MAINTAIN_ROLES");
        requesterBean.setDataObjects(serverDataObject);
        requesterBean.setFunctionType(RIGHT_CHECK);
        AppletServletCommunicator comm = new AppletServletCommunicator(connecto, requesterBean);
        comm.send();
        
        ResponderBean response = comm.getResponse();
        if(response != null && response.isSuccessfulResponse()){
            canMaintainRole = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canMaintainRole;
    }
    
    /**
     * Populate the type and status combobox
     */
    public void populateComboBoxes(){
        //Populate the type combobox
        ComboBoxBean comboBoxBean = new ComboBoxBean("","");
        addRoleForm.cmbType.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean("O", "OSP");
        addRoleForm.cmbType.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean("S", "Department");
        addRoleForm.cmbType.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean("P", "Proposal");
        addRoleForm.cmbType.addItem(comboBoxBean);
        //Modified for IACUC Changes - Start
//        comboBoxBean = new ComboBoxBean("R", "Protocol");
        comboBoxBean = new ComboBoxBean("R", "IRB Protocol");
        addRoleForm.cmbType.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean(IACUC_RIGHT_TYPE, "IACUC Protocol");
        addRoleForm.cmbType.addItem(comboBoxBean);
        //IACUC Changes - End
        comboBoxBean = new ComboBoxBean("T", "Reports");
        addRoleForm.cmbType.addItem(comboBoxBean);
        
        
        //Populate the status combobox
        comboBoxBean = new ComboBoxBean("", "");
        addRoleForm.cmbStatus.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean("A", "Active");
        addRoleForm.cmbStatus.addItem(comboBoxBean);
        comboBoxBean = new ComboBoxBean("I", "Inactive");
        addRoleForm.cmbStatus.addItem(comboBoxBean);
        
    }
    /**
     * Populate the data in the vector <code>vecAssignedTableRights</code> to
     * the table tblAssignedRights and set the renderers and editors to the columns
     *
     * @param vecAssignedTableRights vector of vector with data to be populated
     */
    public void populateAssignedRightsTable(Vector vecAssignedTableRights){
        
        RightAssignedTableModel assignedRightTableModel = new RightAssignedTableModel(vecAssignedTableRights, vecColumnNames);
        addRoleForm.tblAssignedRights.setModel(assignedRightTableModel);
        RightCellRenderer rightCellRenderer = new RightCellRenderer();
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(0).setCellRenderer(rightCellRenderer);
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(1).setCellRenderer(rightCellRenderer);
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(1).setCellEditor(new RightAssignedCellEditor());
        
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(0).setMinWidth(20);
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(0).setMaxWidth(20);
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(0).setPreferredWidth(20);
        
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(1).setMinWidth(20);
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(1).setMaxWidth(20);
        addRoleForm.tblAssignedRights.getColumnModel().getColumn(1).setPreferredWidth(20);
        
        addRoleForm.tblAssignedRights.setTableHeader(null);
    }
    
    /**
     * Populate the data in the vector <code> vecAvailableTableRights</code> to
     * the table tblAssignedRights and set the renderers to the columns
     *
     * @param vecAvailableTableRights vector of vector with data to be populated
     */
    public void populateAvailableRightsTable(Vector vecAvailableTableRights){
        RightAvailableTableModel availableRightTableModel = new RightAvailableTableModel(vecAvailableTableRights, vecColumnNames);
        addRoleForm.tblAvailableRights.setModel(availableRightTableModel);
        
        RightCellRenderer rightCellRenderer = new RightCellRenderer();
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setCellRenderer(rightCellRenderer);
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setCellRenderer(rightCellRenderer);
        
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setMinWidth(20);
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setMaxWidth(20);
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setPreferredWidth(20);
        
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setMinWidth(20);
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setMaxWidth(20);
        addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setPreferredWidth(20);
        addRoleForm.tblAvailableRights.setTableHeader(null);
    }
    
    /**
     * Get rights assigned for this role and all the available rights from the
     * database.
     */
    public void getRoleRightDetails(){
        int roleId = 0;
        if(acType.equals("I")){
            roleId = getNextRoleId();
        }else{
            roleId = roleInfoBean.getRoleId();
        }
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(Integer.toString(roleId));
        requesterBean.setFunctionType(GET_ROLE_DETAILS);
        AppletServletCommunicator comm = new AppletServletCommunicator(connecto, requesterBean);
        comm.send();
        
        ResponderBean response = comm.getResponse();
        if(response != null && response.isSuccessfulResponse()){
            Vector vecServerObjects = response.getDataObjects();
            Vector vecAllRights = (Vector)vecServerObjects.get(0);
            vecAssignedRights = (Vector)vecServerObjects.get(1);
            roleInfoBean = (RoleInfoBean)vecServerObjects.get(2);
            if(vecAssignedRights==null){
                vecAssignedRights = new Vector();
            }
            filterAvailableRights(vecAllRights, vecAssignedRights);
        }
    }
    
    /**
     * Filters the assigned rights from the vecAllRights and populate it into the
     * vecAvailableRights. Also populates the hashMaps hmAllRights with key the
     * right_id and value the descend flag, and the hashMap hmTypeRights with keys
     * "O", "P", "R", "S" the role types and values the possible rights for each type
     *
     * @param vecAllRights vector with all the rights
     * @param vecAssignedRights vector with the assigned rights for the role
     */
    public void filterAvailableRights(Vector vecAllRights, Vector vecAssignedRights){
        hmAllRights = new HashMap();
        
        hmTypeRights = new HashMap();
        hmTypeRights.put("O", new Vector());
        hmTypeRights.put("P", new Vector());
        hmTypeRights.put("R", new Vector());
        //Added for IACUC Changes - Start
        hmTypeRights.put(IACUC_RIGHT_TYPE, new Vector());
        //IACUC Changes - End
        hmTypeRights.put("S", new Vector());
        hmTypeRights.put("T", new Vector());
        
        /* Filter the assigned rights from the allRights and populate it into the
         * vector vecAvailable rights
         */
        RoleRightInfoBean allRightBean = null;
        if(vecAssignedRights!=null && vecAssignedRights.size() != 0){
            if(vecAllRights!=null){
                vecAvailableRights = new Vector();
                boolean found = false;
                RoleRightInfoBean assignedRightBean = null;
                for(int i = 0; i < vecAllRights.size(); i++){
                    allRightBean = (RoleRightInfoBean)vecAllRights.get(i);
                    hmAllRights.put(allRightBean.getRightId(), allRightBean.isDescendFlag()?"Y":"N");
                    found = false;
                    for(int j = 0; j < vecAssignedRights.size(); j++){
                        assignedRightBean = (RoleRightInfoBean)vecAssignedRights.get(j);
                        if(assignedRightBean.getRightId().equals(allRightBean.getRightId())){
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        vecAvailableRights.add(allRightBean);
                    }
                }
            }
        }else{
            vecAvailableRights = vecAllRights;
            if(vecAllRights!=null){
                for(int i = 0; i < vecAllRights.size(); i++){
                    allRightBean = (RoleRightInfoBean)vecAllRights.get(i);
                    hmAllRights.put(allRightBean.getRightId(), allRightBean.isDescendFlag()?"Y":"N");
                }
            }
        }
        
        /* Sort the available rights in the order of the right type and populate hmTypeRights
         * hmTypeRights will contain the keys as "O", "S", "P", "R", "I"(IACUC) which are the role types
         * and the values will be vectors of rights which are possible accrding the role type
         */
        Collections.sort(vecAvailableRights, new RightComparator());
        for(int i = 0; i < vecAvailableRights.size(); i++){
            allRightBean = (RoleRightInfoBean)vecAvailableRights.get(i);
            ((Vector)hmTypeRights.get("O")).add(allRightBean);
            ((Vector)hmTypeRights.get("S")).add(allRightBean);
            if(allRightBean.getRightType() == 'P'){
                ((Vector)hmTypeRights.get("P")).add(allRightBean);
            }else if(allRightBean.getRightType() == 'R'){
                ((Vector)hmTypeRights.get("R")).add(allRightBean);
            //Added for IACUC Changes - Start
            }else if(allRightBean.getRightType() == IACUC_RIGHT_TYPE.charAt(0)){
                ((Vector)hmTypeRights.get(IACUC_RIGHT_TYPE)).add(allRightBean);
            //IACUC Changes - End
            }else if(allRightBean.getRightType() == 'T'){
                ((Vector)hmTypeRights.get("T")).add(allRightBean);
            }
        }
    }
    /**
     * Converts the given vector with RoleRightInfoBean objects to a vector with
     * vector representing each row of a table
     *
     * @param vecRights the vector with the RoleRightInfoBean objects
     * @return Vector containing the converted values
     */
    public Vector changeToTableVector(Vector vecRights){
        Vector vecTableVector = new Vector();
        if(vecRights!=null){
            Vector vecTableRowVector = null;
            RoleRightInfoBean roleRightInfoBean = null;
            for(int i = 0; i < vecRights.size(); i++){
                roleRightInfoBean = (RoleRightInfoBean)vecRights.get(i);
                vecTableRowVector = new Vector();
                vecTableRowVector.add(Character.toString(roleRightInfoBean.getRightType()));
                vecTableRowVector.add(roleRightInfoBean.isDescendFlag()?"Y":"N");
                vecTableRowVector.add(roleRightInfoBean.getRightId());
                vecTableVector.add(vecTableRowVector);
            }
        }
        return vecTableVector;
    }
    
    /**
     * Action to be performed while performing a close operation
     */
    public void performCloseOperation(){
        if(isSaveRequired()){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
            switch( option ) {
                case (CoeusOptionPane.SELECTION_YES):
                    dlgWindow.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    try {
                        saveFormData();
                    } catch (CoeusException ex) {
                        ex.printStackTrace();
                    }
                    dlgWindow.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    break;
                case(CoeusOptionPane.SELECTION_NO):
                    dlgWindow.dispose();
                    break;
                default:
                    populatedAssignedRighs = false;
                    break;
            }
        }else{
            dlgWindow.dispose();
        }
    }
    
    /**
     * Get the next role id possible from the database
     *
     * @return the next role id
     */
    public int getNextRoleId(){
        int roleId = 0;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_NEXT_ROLE_ID);
        AppletServletCommunicator comm = new AppletServletCommunicator(connecto, requesterBean);
        comm.send();
        
        ResponderBean response = comm.getResponse();
        if(response != null && response.isSuccessfulResponse()){
            Integer iroleId  = (Integer)response.getDataObject();
            roleId = iroleId.intValue();
        }
        return roleId;
    }
    
    /**
     * Gets the rights which are newly added/modified or deleted for the role.
     *
     * @return vector of RoleRightInfoBean objects
     */
    public Vector getAssignedRights(){
        populatedAssignedRighs = true;
        Vector vecCurrentAssignedRights = new Vector();
        Vector assignedTableRights = ((DefaultTableModel)addRoleForm.
                tblAssignedRights.getModel()).getDataVector();
        RoleRightInfoBean roleRightInfoBean = null;
        Vector rowData = null;
        if(acType.equals("I")){//Add mode
            for(int i = 0; i < addRoleForm.tblAssignedRights.getRowCount(); i++){
                roleRightInfoBean = new RoleRightInfoBean();
                rowData = (Vector)assignedTableRights.get(i);
                roleRightInfoBean.setAcType("I");
                roleRightInfoBean.setRoleId(Integer.parseInt(addRoleForm.txtRoleId.getText()));
                roleRightInfoBean.setRightId((String)rowData.get(2));
                if(addRoleForm.chkDescend.isSelected()){
                    roleRightInfoBean.setDescendFlag(((String)rowData.get(1)).equalsIgnoreCase(DESCEND_YES)? true : false);
                }else{
                    roleRightInfoBean.setDescendFlag(false);
                }
                vecCurrentAssignedRights.add(roleRightInfoBean);
            }
        }else{//Modify mode
            boolean found = false;
            //Find the newly added rights
            for(int i=0; i  < addRoleForm.tblAssignedRights.getRowCount(); i++){
                rowData = (Vector)assignedTableRights.get(i);
                found = false;
                
                for(int j = 0; j < vecAssignedRights.size(); j++){
                    roleRightInfoBean = (RoleRightInfoBean)vecAssignedRights.get(j);
                    if(roleRightInfoBean.getRightId().equals(rowData.get(2).toString())){
                        found = true;
                        break;
                    }
                }
                boolean newDescendFlag = ((String)rowData.get(1)).equalsIgnoreCase(DESCEND_YES)? true : false;
                if(!addRoleForm.chkDescend.isSelected()){
                    newDescendFlag = false;
                }
                if(!found){
                    roleRightInfoBean = new RoleRightInfoBean();
                    roleRightInfoBean.setAcType("I");
                    roleRightInfoBean.setRoleId(Integer.parseInt(addRoleForm.txtRoleId.getText()));
                    roleRightInfoBean.setRightId((String)rowData.get(2));
                    roleRightInfoBean.setDescendFlag(newDescendFlag);
                    vecCurrentAssignedRights.add(roleRightInfoBean);
                }else{
                    if(newDescendFlag != roleRightInfoBean.isDescendFlag()){
                        roleRightInfoBean.setDescendFlag(newDescendFlag);
                        roleRightInfoBean.setAcType("U");
                        vecCurrentAssignedRights.add(roleRightInfoBean);
                    }
                }
            }
            
            //Find deleted rights
            for(int i = 0; i < vecAssignedRights.size(); i++){
                found = false;
                roleRightInfoBean = (RoleRightInfoBean)vecAssignedRights.get(i);
                for(int j = 0; j  < addRoleForm.tblAssignedRights.getRowCount(); j++){
                    rowData = (Vector)assignedTableRights.get(j);
                    if(roleRightInfoBean.getRightId().equals(rowData.get(2).toString())){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    roleRightInfoBean.setAcType("D");
                    vecCurrentAssignedRights.add(roleRightInfoBean);
                }
            }
        }
        return vecCurrentAssignedRights;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object actSource = e.getSource();
        try {
            if(actSource.equals(addRoleForm.btnAdd)){
                moveRows(addRoleForm.tblAvailableRights, addRoleForm.tblAssignedRights);
            }else if(actSource.equals(addRoleForm.btnRemove)){
                moveRows(addRoleForm.tblAssignedRights, addRoleForm.tblAvailableRights);
            }else if(actSource.equals(addRoleForm.btnOk)){
                if(acType.equals("I")){
                    saveFormData();
                }else if(isSaveRequired()){
                    saveFormData();
                }else{
                    dlgWindow.dispose();
                }
            }else if(actSource.equals(addRoleForm.btnCancel)){
                performCloseOperation();
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void display() {
        String title = "Create Role";
        if(acType.equals("U")){
            title = "Modify Role";
        }
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), title);
        dlgWindow.setResizable(false);
        dlgWindow.setModal(true);
        dlgWindow.setFont(CoeusFontFactory.getLabelFont());
        dlgWindow.getContentPane().add(addRoleForm);
        dlgWindow.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgWindow.getSize();
        dlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgWindow.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    performCloseOperation();
                }
            }
        });
        dlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                performCloseOperation();
            }
        });
        dlgWindow.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                addRoleForm.txtName.requestFocusInWindow();
            }
        });
        dlgWindow.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    public Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        RoleInfoBean roleInfoBean = new RoleInfoBean();
        roleInfoBean.setRoleId(Integer.parseInt(addRoleForm.txtRoleId.getText().trim()));
        roleInfoBean.setRoleName(addRoleForm.txtName.getText().trim());
        roleInfoBean.setRoleDesc(addRoleForm.txtDescription.getText().trim());
        roleInfoBean.setDescend(addRoleForm.chkDescend.isSelected());
        roleInfoBean.setStatus(((ComboBoxBean)addRoleForm.cmbStatus.getSelectedItem()).getCode().charAt(0));
        roleInfoBean.setRoleType(((ComboBoxBean)addRoleForm.cmbType.getSelectedItem()).getCode().charAt(0));
        roleInfoBean.setUnitNumber(unitNumber);
        
        if(acType.equals("I")){
            roleInfoBean.setAcType("I");
        }else{
            roleInfoBean.setUpdateTimestamp(this.roleInfoBean.getUpdateTimestamp());
            roleInfoBean.setAcType("U");
        }
        return roleInfoBean;
    }
    
    public void registerComponents() {
        addRoleForm.btnAdd.addActionListener(this);
        addRoleForm.btnRemove.addActionListener(this);
        addRoleForm.btnOk.addActionListener(this);
        addRoleForm.btnCancel.addActionListener(this);
        
        addRoleForm.tblAssignedRights.getSelectionModel().addListSelectionListener(this);
        addRoleForm.tblAvailableRights.getSelectionModel().addListSelectionListener(this);
        addRoleForm.cmbType.addItemListener(this);
    }
    
    public void saveFormData() throws CoeusException {
        try {
            boolean success = false;
            if(validate()){
                RoleInfoBean roleInfoBean = (RoleInfoBean)getFormData();
                Vector vecDataObjects = new Vector();
                vecDataObjects.add(roleInfoBean);
                if(populatedAssignedRighs){
                    vecDataObjects.add(vecFinalAssignedRights);
                }else{
                    vecDataObjects.add(getAssignedRights());
                }
                
                RequesterBean requesterBean = new RequesterBean();
                requesterBean.setDataObjects(vecDataObjects);
                requesterBean.setFunctionType(SAVE_ROLE_DETAILS);
                AppletServletCommunicator comm = new AppletServletCommunicator(connecto, requesterBean);
                comm.send();
                
                ResponderBean response = comm.getResponse();
                if(response != null && response.isSuccessfulResponse()){
                    success = true;
                    refreshRequired = true;
                }else{
                    success = false;
                }
                if(success){
                    dlgWindow.dispose();
                }
            }
        } catch (CoeusUIException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setFormData(Object data) throws CoeusException {
        if(acType.equals("U")){//Modify Mode
            addRoleForm.txtRoleId.setText(Integer.toString(roleInfoBean.getRoleId()));
            addRoleForm.txtDescription.setText(roleInfoBean.getRoleDesc());
            addRoleForm.txtName.setText(roleInfoBean.getRoleName());
            
            addRoleForm.cmbType.setEnabled(false);
            setComboBoxSelection(new Character(roleInfoBean.getRoleType()).toString(), addRoleForm.cmbType);
            setComboBoxSelection(new Character(roleInfoBean.getStatus()).toString(), addRoleForm.cmbStatus);
            addRoleForm.chkDescend.setSelected(roleInfoBean.isDescend());
            
            //Populate the available rights table
            Vector vecAvailableTableRights = changeToTableVector((Vector)hmTypeRights.get(Character.toString(roleInfoBean.getRoleType())));
            
            //Commented and Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
            Vector vecOrderedAvailableRights = new Vector();
            vecOrderedAvailableRights = getSortedRightsForRole(vecAvailableTableRights);
            // populateAvailableRightsTable(vecAvailableTableRights);
            populateAvailableRightsTable(vecOrderedAvailableRights);
            //Commented and Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
            
            //Populate the assigned rights table
            if(vecAssignedRights!=null && vecAssignedRights.size()!=0){
                Collections.sort(vecAssignedRights, new RightComparator());
            }
            Vector vecAssignedTableRights = changeToTableVector(vecAssignedRights);
            
            //Commented and Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
            Vector vecOrderedAssignedRights = new Vector();
            vecOrderedAssignedRights = getSortedRightsForRole(vecAssignedTableRights);
            //populateAssignedRightsTable(vecAssignedTableRights);
            populateAssignedRightsTable(vecOrderedAssignedRights);
            //Commented and Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
            //AddedCase#4414 - Disappearing role
            //To set role owned unit number when modified
            unitNumber = roleInfoBean.getUnitNumber();
            //Case#4414 - Endd
            addRoleForm.txtUnitName.setText(roleInfoBean.getUnitNumber()+":"+roleInfoBean.getUnitName());
        }else{//Add mode
            int roleId = getNextRoleId();
            addRoleForm.txtRoleId.setText(Integer.toString(roleId));
            addRoleForm.txtUnitName.setText(unitNumber+":"+unitName);
            populateAvailableRightsTable(new Vector());
            populateAssignedRightsTable(new Vector());
        }
    }
    
    public boolean validate() throws CoeusUIException {
        boolean valid = true;
            if(addRoleForm.txtName.getText().trim().length()==0){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("roleExceptionCode.1001"));
                valid = false;
                setFocusToComponent(addRoleForm.txtName);
            }else if(acType.equals("I") && ((ComboBoxBean)addRoleForm.cmbType.getSelectedItem()).getCode().equals("")){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("roleExceptionCode.1002"));
                valid = false;
                setFocusToComponent(addRoleForm.cmbType);
            }else if(acType.equals("I") && ((ComboBoxBean)addRoleForm.cmbStatus.getSelectedItem()).getCode().equals("")){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("roleExceptionCode.1003"));
                valid = false;
                setFocusToComponent(addRoleForm.cmbStatus);
            }
        return valid;
    }
    
    /** moves selected row from the table<code>from</code> to the table <code>
     * to</code>
     *
     * @param from from Table
     * @param to to Table.
     */
    private void moveRows(CoeusDnDTable from, CoeusDnDTable to) {
        Vector rowData = null;
        int selectedRow = from.getSelectedRow();
        if(selectedRow == -1){
            return ;
        }
        if(from.equals(addRoleForm.tblAssignedRights) || from.equals(addRoleForm.tblAvailableRights)){
            int[] selectedRows = from.getSelectedRows();
            for(int i = selectedRows.length-1; i>= 0; i--){
                selectedRow = selectedRows[i];
                if(from.equals(addRoleForm.tblAssignedRights)){
                    rowData = ((RightAssignedTableModel)from.getModel()).getRow(selectedRow);
                }else if(from.equals(addRoleForm.tblAvailableRights)){
                    rowData = ((RightAvailableTableModel)from.getModel()).getRow(selectedRow);
                }
                ((DefaultTableModel)to.getModel()).addRow(rowData);
                ((DefaultTableModel)from.getModel()).removeRow(selectedRow);
            }
            ((DefaultTableModel)to.getModel()).fireTableDataChanged();
            ((DefaultTableModel)from.getModel()).fireTableDataChanged();
            addRoleForm.btnAdd.setEnabled(false);
            addRoleForm.btnRemove.setEnabled(false);
        }
    }
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if(e.getStateChange() == e.SELECTED){
            if(source.equals(addRoleForm.cmbType)){
                addRoleForm.tblAvailableRights.clearSelection();
                addRoleForm.tblAssignedRights.clearSelection();
                String roleType = ((ComboBoxBean)addRoleForm.cmbType.getSelectedItem()).getCode();
                
                Vector vecAvailableTableRights = changeToTableVector((Vector)hmTypeRights.get(roleType));
                //Commented and Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
                Vector vecOrderedAvailableRights = new Vector();
                vecOrderedAvailableRights = getSortedRightsForRole(vecAvailableTableRights);
                // RightAvailableTableModel availableRightTableModel = new RightAvailableTableModel(vecAvailableTableRights, vecColumnNames);
                RightAvailableTableModel availableRightTableModel = new RightAvailableTableModel(vecOrderedAvailableRights, vecColumnNames);
                //Added for COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
                addRoleForm.tblAvailableRights.setModel(availableRightTableModel);
                
                if(addRoleForm.tblAssignedRights.getRowCount()>0){
                /* Check the previous selected type and new type selected
                 * if the assigned table contains rights which are not valid
                 * according to the current selection of type, remove all such rows
                 */
                    //Modified for IACUC Changes
//                    if(roleType.equals("P") || roleType.equals("R")){
                    if(roleType.equals("P") || roleType.equals("R") || roleType.equals(IACUC_RIGHT_TYPE)){
                        for(int i = addRoleForm.tblAssignedRights.getRowCount()-1;i>=0; i--){
                            if(!addRoleForm.tblAssignedRights.getValueAt(i, RIGHT_TYPE_COLUMN).toString().equalsIgnoreCase(roleType)){
                                ((DefaultTableModel)addRoleForm.tblAssignedRights.getModel()).removeRow(i);
                            }
                        }
                    }
                    
                /*
                 * While changing the type if there is already some rights selected in
                 * the assigned rights table and are also present in the available rights
                 * table, remove all such entries from the available rights table
                 */
                    for(int i = 0; i < addRoleForm.tblAssignedRights.getRowCount(); i++){
                        for(int j=0; j< addRoleForm.tblAvailableRights.getRowCount(); j++){
                            if(addRoleForm.tblAssignedRights.getValueAt(i, RIGHT_ID_COLUMN).toString().
                                    equals(addRoleForm.tblAvailableRights.getValueAt(j, RIGHT_ID_COLUMN).toString())){
                                vecAvailableTableRights.remove(j);
                            }
                        }
                    }
                    availableRightTableModel = new RightAvailableTableModel(vecAvailableTableRights, vecColumnNames);
                    addRoleForm.tblAvailableRights.setModel(availableRightTableModel);
                }
                RightCellRenderer rightCellRenderer = new RightCellRenderer();
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setCellRenderer(rightCellRenderer);
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setCellRenderer(rightCellRenderer);
                
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setMinWidth(20);
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setMaxWidth(20);
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(0).setPreferredWidth(20);
                
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setMinWidth(20);
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setMaxWidth(20);
                addRoleForm.tblAvailableRights.getColumnModel().getColumn(1).setPreferredWidth(20);
                
                prevRoleType = roleType;
            }
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
        Object source = e.getSource();
        if(source.equals(addRoleForm.tblAssignedRights.getSelectionModel())){
            if(addRoleForm.tblAssignedRights.getSelectedRow()!=-1){
                addRoleForm.btnAdd.setEnabled(false);
                addRoleForm.btnRemove.setEnabled(true);
                addRoleForm.tblAvailableRights.clearSelection();
            }
        }else if(source.equals(addRoleForm.tblAvailableRights.getSelectionModel())){
            if(addRoleForm.tblAvailableRights.getSelectedRow()!=-1){
                String typeCode = ((ComboBoxBean)addRoleForm.cmbType.getSelectedItem()).getCode();
                String selTypeCode = (String)addRoleForm.tblAvailableRights.
                        getValueAt(addRoleForm.tblAvailableRights.getSelectedRow(), 0);
                //Modified for IACUC Changes - Start
//                if(typeCode.equals("P") || typeCode.equals("R")){//Proposal
                if(typeCode.equals("P") || typeCode.equals("R") || typeCode.equals(IACUC_RIGHT_TYPE)){//Proposal, IRB Protocol, IACUC Protocol
                    if(selTypeCode.equals(typeCode)){
                        addRoleForm.btnAdd.setEnabled(true);
                    }else{
                        addRoleForm.btnAdd.setEnabled(false);
                    }
                }else if(typeCode.equals("S") || typeCode.equals("O")){//Unit, Department
                    addRoleForm.btnAdd.setEnabled(true);
                }
                addRoleForm.btnRemove.setEnabled(false);
                addRoleForm.tblAssignedRights.clearSelection();
            }
        }
    }
    
    //This class is used as the table Model for the Assinged rights table
    class RightAssignedTableModel extends DefaultTableModel {
        RightAssignedTableModel(Vector vecData, Vector vecColumns){
            super(vecData, vecColumns);
        }
        public void addRow(Vector rowData) {
            addSorted(rowData, RIGHT_TYPE_COLUMN);
        }
        
        private void addSorted(Vector rowData, int columnToCompare) {
            int row=0, compare;
            // Commented for COEUSQA-3230 : Move IACUC protocol roles below - Start            
//            String value, with;
//            value = (String)rowData.get(columnToCompare);
//            for(row = 0; row < getRowCount(); row++) {
//                with = (String)getValueAt(row, columnToCompare);
//                compare = with.compareToIgnoreCase(value);
//                if(compare > 0){
//                    break;
//                }
//            }
            // Commented for COEUSQA-3230 : Move IACUC protocol roles below - End
            if(dataVector.size() == 0){
                Vector tableData = new Vector();
                tableData.add(rowData);
                populateAssignedRightsTable(tableData);
            }else{
                dataVector.add(row, rowData);
                // Added for COEUSQA-3230 : Move IACUC protocol roles below - Start
                // Sort the collection based on the right type defined in the CoeusGuiConstants
                Collections.sort(dataVector,new AssignedRightComparator());
                // Added for COEUSQA-3230 : Move IACUC protocol roles below - End
            }
            
        }
        public Vector getRow(int row){
            return (Vector)dataVector.get(row);
        }
        
        public boolean isCellEditable(int row, int column){
            if(column == DESCEND_COLUMN){
                return true;
            }else{
                return false;
            }
        }
    }
    
    //This class is used as the table Model for the Available rights table
    class RightAvailableTableModel extends DefaultTableModel {
        RightAvailableTableModel(Vector vecData, Vector vecColumns){
            super(vecData, vecColumns);
        }
        
        public void addRow(Vector rowData) {
            // Whiling moving rows to the available rights table, the descend flag should be
            // set as the original one taken from the osp$rights table, incase the user has
            // changed the flag.
            String originalDescendFlag = hmAllRights.get(rowData.get(RIGHT_ID_COLUMN).
                    toString()).toString().toUpperCase();
            rowData.remove(1);
            rowData.add(1, originalDescendFlag);
            addSorted(rowData, RIGHT_TYPE_COLUMN);
        }
        
        private void addSorted(Vector rowData, int columnToCompare) {
            int row=0, compare;
            // Commented for COEUSQA-3230 : Move IACUC protocol roles below - Start
//            String value, with;
//            value = (String)rowData.get(columnToCompare);
//            for(row = 0; row < getRowCount(); row++) {
//                with = (String)getValueAt(row, columnToCompare);
//                compare = with.compareToIgnoreCase(value);
//                if(compare > 0){
//                    break;
//                }
//            }
            // Commented for COEUSQA-3230 : Move IACUC protocol roles below - End
            if(dataVector.size() == 0){
                Vector tableData = new Vector();
                tableData.add(rowData);
                populateAvailableRightsTable(tableData);
            }else{
                dataVector.add(row, rowData);
                // Added for COEUSQA-3230 : Move IACUC protocol roles below - Start
                // Sort the collection based on the right type defined in the CoeusGuiConstants
                Collections.sort(dataVector, new AssignedRightComparator());
                // Added for COEUSQA-3230 : Move IACUC protocol roles below - End
            }
        }
        
        public Vector getRow(int row){
            return (Vector)dataVector.get(row);
        }
        
        public boolean isCellEditable(int row, int column){
            return false;
        }
    }
    
    //This class is used for rendering the cells in the rights table
    class RightCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblImage;
        private JButton btnImage;
        private char roleType;
        private Vector vecRowData;
        
        RightCellRenderer() {
            lblImage = new JLabel();
            btnImage = new JButton();
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            if(column == DESCEND_COLUMN) {
                if(table.equals(addRoleForm.tblAssignedRights)){
                    if(value.toString().trim().equals(DESCEND_YES)){
                        btnImage.setIcon(iIcnDescendY);
                    } else {
                        btnImage.setIcon(iIcnDescendN);
                    }
                    return btnImage;
                }else{
                    if(value.toString().trim().equals(DESCEND_YES)){
                        lblImage.setIcon(iIcnDescendY);
                    } else {
                        lblImage.setIcon(iIcnDescendN);
                    }
                    return lblImage;
                }
            } else if(column == RIGHT_TYPE_COLUMN){
                vecRowData = (Vector)((DefaultTableModel)table.getModel()).getDataVector().get(row);
                roleType = value.toString().toUpperCase().trim().charAt(0);
                switch(roleType) {
                    case ADMIN_UPPER_CASE:
                        lblImage.setIcon(iIcnAdminActive);
                        break;
                    case PROPOSAL_UPPER_CASE:
                        lblImage.setIcon(iIcnProposalRoleActive);
                        break;
                    case SYSTEM_UPPER_CASE:
                        lblImage.setIcon(iIcnSystemActive);
                        break;
                    case PROTOCOL_UPPER_CASE:
                        lblImage.setIcon(iIcnProtocolRoleActive);
                        break;
                    case IACUC_UPPER_CASE:
                        lblImage.setIcon(iIcnIacucLevelRight);
                        break;
                    case REPORT_UPPER_CASE:
                        lblImage.setIcon(iIcnDescendY);
                      

                }
                return lblImage;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    //This class is used as the cell editor for the RightsAssigned Table
    class RightAssignedCellEditor extends DefaultCellEditor implements ActionListener{
        
        private JButton btnCellEditor;
        private String descend;
        private int row, column;
        
        RightAssignedCellEditor(){
            super(new JComboBox());
            btnCellEditor = new JButton();
            btnCellEditor.addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent e){
            String orginalDescendFlag = hmAllRights.get(addRoleForm.tblAssignedRights.getValueAt(row, RIGHT_ID_COLUMN)).toString().toUpperCase();
            if(orginalDescendFlag.equals(DESCEND_NO)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("roleExceptionCode.1004"));
            }else{
                if(descend.equals(DESCEND_YES)){
                    addRoleForm.tblAssignedRights.setValueAt(DESCEND_NO, row, column);
                    btnCellEditor.setIcon(iIcnDescendN);
                }else{
                    addRoleForm.tblAssignedRights.setValueAt(DESCEND_YES, row, column);
                    btnCellEditor.setIcon(iIcnDescendY);
                }
            }
            cancelCellEditing();
        }
        
        public boolean stopCellEditing() {
            //This is required since the default implementation
            //removes any partially edited value.
            return true;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.column = column;
            descend = value.toString().toUpperCase();
            if(descend.equals(DESCEND_YES)){
                btnCellEditor.setIcon(iIcnDescendY);
            }else{
                btnCellEditor.setIcon(iIcnDescendN);
            }
            return btnCellEditor;
        }
    }
    
    //Comparator used to set the order of the RoleRightBeans in the order of the right type
    class RightComparator implements Comparator{
        
        public int compare(Object roleRightBean1, Object roleRightBean2) {
            if(roleRightBean1 == null || roleRightBean2 == null ||
                    !(roleRightBean1 instanceof RoleRightInfoBean) ||
                    !(roleRightBean2 instanceof RoleRightInfoBean)){
                return 0;
            }
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - Start
            // Modified to order the rights in the defined order in CoeusGuiConstants
//            return new Character(((RoleRightInfoBean)roleRightBean1).getRightType()).
//                    compareTo(new Character(((RoleRightInfoBean)roleRightBean2).getRightType()));
            char rightType1 = ((RoleRightInfoBean)roleRightBean1).getRightType();
            char rightType2 = ((RoleRightInfoBean)roleRightBean2).getRightType();
            Integer beanPos1 = getPosition(rightType1+"");
            Integer beanPos2 = getPosition(rightType2+"");
            return beanPos1.compareTo(beanPos2);
            // Modified for COEUSQA-3230 : Move IACUC protocol roles below - Start
        }
        
        // Added for COEUSQA-3230 : Move IACUC protocol roles below - Start
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
        // Added for COEUSQA-3230 : Move IACUC protocol roles below - End
    }
    
    
    // Added for COEUSQA-3230 : Move IACUC protocol roles below - Start
    class AssignedRightComparator implements Comparator{

        public int compare(Object roleRight1, Object roleRight2) {
            if(roleRight1 == null || roleRight2 == null ||
                    ((Vector)roleRight1).isEmpty() || ((Vector)roleRight2).isEmpty()) {
                return 0;
            }
            String rightType1 = (String)((Vector)roleRight1).get(0);
            String rightType2 = (String)((Vector)roleRight2).get(0);
            Integer beanPos1 = getPosition(rightType1);
            Integer beanPos2 = getPosition(rightType2);
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
    
    public boolean isRefreshRequired() {
        return refreshRequired;
    }
    public boolean isSaveRequired(){
        boolean saveRequired = false;
        if(acType.equals("I")){
            if(addRoleForm.txtName.getText().trim().length()>0){
                saveRequired = true;
            }else if(addRoleForm.txtDescription.getText().trim().length()>0){
                saveRequired = true;
            }else if(!((ComboBoxBean)addRoleForm.cmbType.getSelectedItem()).getCode().equals("")){
                saveRequired = true;
            }else if(!((ComboBoxBean)addRoleForm.cmbStatus.getSelectedItem()).getCode().equals("")){
                saveRequired = true;
            }else if(addRoleForm.chkDescend.isSelected()){
                saveRequired = true;
            }else if(addRoleForm.tblAssignedRights.getModel().getRowCount()>0){
                saveRequired = true;
            }else{
                saveRequired = false;
            }
        }else{
            if(!addRoleForm.txtName.getText().trim().equals(roleInfoBean.getRoleName())){
                saveRequired = true;
            }else if(!addRoleForm.txtDescription.getText().trim().equals(roleInfoBean.getRoleDesc())){
                saveRequired = true;
            }else if(!((ComboBoxBean)addRoleForm.cmbType.getSelectedItem()).
                    getCode().equals(Character.toString(roleInfoBean.getRoleType()))){
                saveRequired = true;
            }else if(!((ComboBoxBean)addRoleForm.cmbStatus.getSelectedItem()).
                    getCode().equals(Character.toString(roleInfoBean.getStatus()))){
                saveRequired = true;
            }else if(addRoleForm.chkDescend.isSelected()!=roleInfoBean.isDescend()){
                saveRequired = true;
            }else if((vecFinalAssignedRights = getAssignedRights()).size()>0){
                saveRequired = true;
            }else{
                saveRequired = false;
            }
        }
        return saveRequired;
    }
    
    public RoleInfoBean getRole(){
        RoleInfoBean role = new RoleInfoBean();
        role.setRoleName(addRoleForm.txtName.getText().trim());
        role.setDescend(addRoleForm.chkDescend.isSelected());
        role.setStatus(((ComboBoxBean)addRoleForm.cmbStatus.getSelectedItem()).getCode().charAt(0));
        return role;
    }
    public void setFocusToComponent(final JComponent component){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                component.requestFocusInWindow();
            }
        });
    }
    
    //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - start
    /**
     * Method used to sort the rights in alphabatical order and based on right types
     * @param vecAvailableTableRights  
     */
    private Vector getSortedRightsForRole(Vector vecAvailableTableRights){
        Vector vecOrderedRights = new Vector();
        HashMap hmAvailableRights = new HashMap();
        if(vecAvailableTableRights != null && !vecAvailableTableRights.isEmpty()){
            for(int index=0;index<vecAvailableTableRights.size();index++){
                String rightTypeDesc = (String)((Vector)vecAvailableTableRights.get(index)).get(0);
                if(hmAvailableRights.get(rightTypeDesc) != null){
                    Vector vecRoleData = (Vector)hmAvailableRights.get(rightTypeDesc);
                    vecRoleData.add(vecAvailableTableRights.get(index));
                    hmAvailableRights.put(rightTypeDesc,vecRoleData);
                }else{
                    Vector vecRoleData = new Vector();
                    vecRoleData.add(vecAvailableTableRights.get(index));
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
            String roleType1 = (String)((Vector)role1).get(2);
            String roleType2 = (String)((Vector)role2).get(2);
            return roleType1.toUpperCase().compareTo(roleType2.toUpperCase());
            
        }
    }
    //Added for the COEUSQA-3408 : roles in user maintenance should be in alphabetical order - end
}
