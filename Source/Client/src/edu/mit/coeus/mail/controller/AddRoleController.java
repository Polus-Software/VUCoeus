/*
 * AddRoleController.java
 *
 * Created on June 5, 2007, 2:14 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mail.bean.RoleRecipientBean;
import edu.mit.coeus.mail.gui.AddRoleForm;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author talarianand
 */
public class AddRoleController extends Controller implements ActionListener, ItemListener, MouseListener, ListSelectionListener, KeyListener {
    
    private static final String WINDOW_TITLE = "Role Details";
    
    private static final int WIDTH = 480;
    
    private static final int HEIGHT = 340;
    
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusDlgWindow dlgRole;
    
    private AddRoleForm objAddRole;
    
    private final String PERSON_ROLE_SERVLET ="/personRoleServlet";
    
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ PERSON_ROLE_SERVLET;
    
    private final static char GET_ROLE_LIST = 'R';
    
    private static final int PERSON_NAME = 0;
    
    private static final int MAIL_ID = 1;
    
    private static final String EMPTY_STRING = "";
    
    private static final char GET_QUALIFIER_LIST = 'Q';
    
    private static final char GET_ROLE_PERSON_INFO = 'r';
    
    private CoeusVector cvRoleData;
    
    private CoeusVector cvQualifierList;
    
    private CoeusVector vecPersonInfo;
    
    private RoleTableModel roleTableModel;
    
//    private MailActionInfoBean mailInfoBean;

    private CoeusVector cvQualifierData;
    
    private CoeusVector cvPersonData;
    
    private int moduleCode;
    private String moduleItemKey;
    private int moduleItemKeySequence;
    private RoleRecipientBean selectedRole = null;
    
    //Modified the design of this class with COEUSDEV - 75 : Rework email Engine.
    public AddRoleController(int moduleCode,String moduleItemKey, int moduleItemKeySequence) {
        this.moduleCode = moduleCode;
        this.moduleItemKey = moduleItemKey;
        this.moduleItemKeySequence = moduleItemKeySequence;
        initComponents();
        registerComponents();
        setTableColumn();
        setFormData(null);
        formatFields();
    }
    
    private void initComponents() {
        mdiForm = CoeusGuiConstants.getMDIForm();
        dlgRole = new CoeusDlgWindow(mdiForm, true);
        objAddRole = new AddRoleForm();
        dlgRole.setResizable(false);
        dlgRole.setModal(true);
        dlgRole.getContentPane().add(objAddRole);
        dlgRole.setTitle(WINDOW_TITLE);
        dlgRole.setFont(CoeusFontFactory.getLabelFont());
        dlgRole.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRole.setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRole.getSize();
        dlgRole.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgRole.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent actionEvent){
                performCloseAction();
            }
        });
        dlgRole.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCloseAction();
            }
        });
        
        dlgRole.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    public void display(){
        objAddRole.tblUserList.setRowSelectionAllowed(true);
        if(objAddRole.tblUserList.getRowCount() > 0) {
            objAddRole.tblUserList.setRowSelectionInterval(0, 0);
        }
        dlgRole.setVisible(true);
    }

    public void saveFormData() {
    }
    
    public void registerComponents(){
        roleTableModel = new RoleTableModel();
        Component[] component = {objAddRole.tblUserList, objAddRole.lblRole,
        objAddRole.lblQualifier, objAddRole.cmbRole, objAddRole.cmbQualifier,
        objAddRole.btnOk, objAddRole.btnCancel};
        
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        objAddRole.setFocusTraversalPolicy(policy);
        objAddRole.setFocusCycleRoot(true);
        
        LimitedPlainDocument limitedPlainDocument = new LimitedPlainDocument(30);
        limitedPlainDocument.setFilter(LimitedPlainDocument.FORCE_UPPERCASE);
        
        objAddRole.btnOk.addActionListener(this);
        objAddRole.btnCancel.addActionListener(this);
        objAddRole.cmbRole.addItemListener(this);
        objAddRole.cmbQualifier.addItemListener(this);
        objAddRole.tblUserList.getTableHeader().addMouseListener(this);
        objAddRole.tblUserList.setModel(roleTableModel);
        ListSelectionModel selectionModel = objAddRole.tblUserList.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        objAddRole.tblUserList.setSelectionModel(selectionModel);
        objAddRole.tblUserList.addKeyListener(this);
    }
    
    public boolean validate() {
        return true;
    }
    
    public void formatFields() {
        objAddRole.btnOk.setEnabled(false);
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void setFormData(Object data){
        try {
            cvRoleData = getRoleList();
            cvQualifierList = getQualifierList();
            vecPersonInfo = new CoeusVector();
            ComboBoxBean comboBoxBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            objAddRole.cmbRole.addItem(comboBoxBean);
            
            if(cvRoleData != null && cvRoleData.size() > 0) {
                for(int index = 0; index < cvRoleData.size(); index++) {
                    PersonRoleInfoBean actionInfoBean = (PersonRoleInfoBean) cvRoleData.get(index);
                    comboBoxBean = new ComboBoxBean(actionInfoBean.getRoleCode(), actionInfoBean.getRoleName());
                    objAddRole.cmbRole.addItem(comboBoxBean);
                }
            }
        }catch(CoeusClientException ce) {
            CoeusOptionPane.showDialog(ce);
        }catch(CoeusException ce) {
            ce.printStackTrace();
        }
    }
    
    public CoeusVector getRoleList() throws CoeusClientException, CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable htMailListData = null;
        requesterBean.setDataObject(String.valueOf(moduleCode));
        requesterBean.setFunctionType(GET_ROLE_LIST);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htMailListData = (Hashtable)responderBean.getDataObject();
                cvRoleData = (CoeusVector) htMailListData.get(PersonRoleInfoBean.class);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return cvRoleData;
    }
    
    public CoeusVector getQualifierList() throws CoeusClientException, CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable htMailListData = null;
        
        requesterBean.setFunctionType(GET_QUALIFIER_LIST);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htMailListData = (Hashtable)responderBean.getDataObject();
                cvQualifierList = (CoeusVector) htMailListData.get(PersonRoleInfoBean.class);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return cvQualifierList;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(objAddRole.btnOk)){
            performOkAction();
        }else if(source.equals(objAddRole.btnCancel)){
            performCloseAction();
        }
    }
    
    public Component getControlledUI() {
        return objAddRole;
    }
    
    private void performCloseAction(){
        selectedRole = null;
        dlgRole.setVisible(false);
    }
    
    private void performOkAction() {
        dlgRole.setVisible(false);
    }
    
    public RoleRecipientBean showRoleList() {
        display();
        return selectedRole;
    }
    
    private void setWindowFocus(){
        objAddRole.btnCancel.requestFocusInWindow();
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        
        if(itemEvent.getStateChange()==ItemEvent.SELECTED){
            Object source = itemEvent.getSource();
            String roleId = ((ComboBoxBean) objAddRole.cmbRole.getSelectedItem()).getCode();
            String roleDesc = ((ComboBoxBean) objAddRole.cmbRole.getSelectedItem()).getDescription();
            if(source.equals(objAddRole.cmbRole)) {
                
                objAddRole.cmbQualifier.removeAllItems();
                
                if(roleId != null && roleId != "") {
                    selectedRole = new RoleRecipientBean();
                    selectedRole.setRoleId(Integer.parseInt(roleId));
                    selectedRole.setRoleName(roleDesc);
                    Equals operator = new Equals("roleCode", roleId);
                    cvQualifierData = cvQualifierList.filter(operator);
                }
                
                if(cvQualifierData != null && cvQualifierData.size() > 0) {
                    objAddRole.btnOk.setEnabled(false);
                    objAddRole.cmbQualifier.setEnabled(true);
                    objAddRole.cmbQualifier.addItem(new ComboBoxBean(EMPTY_STRING, EMPTY_STRING));
                    for(int index = 0; index < cvQualifierData.size(); index++) {
                        PersonRoleInfoBean personBean = (PersonRoleInfoBean) cvQualifierData.get(index);
                        ComboBoxBean comboBoxBean = new ComboBoxBean(personBean.getQualifierCode()+"", personBean.getRoleQualifier());
                        objAddRole.cmbQualifier.addItem(comboBoxBean);
                    }
                    roleTableModel.setData(new CoeusVector());
                } else if(roleId != null && roleId != "") {
                    try {
                        objAddRole.cmbQualifier.setEnabled(false);
                        vecPersonInfo = fetchRolePerson(Integer.parseInt(roleId),"0");
                        
                        if(vecPersonInfo == null || vecPersonInfo.size() == 0) {
                            objAddRole.btnOk.setEnabled(false);
                            roleTableModel.setData(new CoeusVector());
                        } else {
                            objAddRole.btnOk.setEnabled(true);
                            roleTableModel.setData(vecPersonInfo);
                        }
                        
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if(source.equals(objAddRole.cmbQualifier)) {
                String qualifierCode = ((ComboBoxBean) objAddRole.cmbQualifier.getSelectedItem()).getCode();
                String qualifier = ((ComboBoxBean) objAddRole.cmbQualifier.getSelectedItem()).getDescription();
                if(qualifierCode != null && qualifierCode != "") {
                    selectedRole.setRoleQualifier(qualifierCode);
                    selectedRole.setRoleQualifierName(roleDesc);
                    try {
                        vecPersonInfo = fetchRolePerson(Integer.parseInt(roleId),qualifierCode);
                        
//                        if(vecPersonInfo != null && vecPersonInfo.size() > 0) {
                            if(vecPersonInfo == null || vecPersonInfo.size() == 0) {
                                objAddRole.btnOk.setEnabled(false);
                                roleTableModel.setData(new CoeusVector());
                            } else {
                                objAddRole.btnOk.setEnabled(true);
                                roleTableModel.setData(vecPersonInfo);
                            }
//                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }else if(selectedRole!=null){
                    selectedRole.setRoleQualifier(EMPTY_STRING);
                    selectedRole.setRoleQualifierName(EMPTY_STRING);
                }
            }
        }
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        int source=e.getKeyCode();
        int selectedRow=objAddRole.tblUserList.getSelectedRow();
        if(source == KeyEvent.VK_DOWN) {
            objAddRole.tblUserList.requestFocusInWindow();
            objAddRole.tblUserList.setRowSelectionInterval(selectedRow ,selectedRow);
        }else if(source == KeyEvent.VK_UP) {
            objAddRole.tblUserList.requestFocusInWindow();
            objAddRole.tblUserList.setRowSelectionInterval(selectedRow ,selectedRow);
        }
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
    class RoleTableModel extends AbstractTableModel {
        String colNames[] = {"Person Name", "Mail Id"};
        Class[] colTypes = new Class[] {String.class,String.class};
        
        RoleTableModel() {
        }
        
        public boolean isCellEditable() {
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }
        
        public void setData(CoeusVector cvCustElements){
            //vecPersonInfo = cvCustElements;
            cvPersonData = cvCustElements;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvPersonData == null ||  cvPersonData.size()== 0){
                return 0;
            }else{
                return cvPersonData.size();
            }
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            PersonRecipientBean personRecipientBean = (PersonRecipientBean) cvPersonData.get(row);
            switch(column) {
                case PERSON_NAME:
                    return personRecipientBean.getPersonName();
                case MAIL_ID:
                    return personRecipientBean.getEmailId();
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(cvRoleData== null) {
                return;
            }
            PersonRecipientBean personRecipientBean = (PersonRecipientBean) cvPersonData.get(row);
            switch(column) {
                case PERSON_NAME:
                   personRecipientBean.setPersonName(value.toString());
                case MAIL_ID:
                    personRecipientBean.setEmailId(value.toString());
            }
        }
        
    }
    
    /**
     * Used to set the columns to table
     */
    private void setTableColumn() {
        JTableHeader tableHeader = objAddRole.tblUserList.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        objAddRole.tblUserList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        objAddRole.tblUserList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        objAddRole.tblUserList.setCellSelectionEnabled(false);
        
        TableColumn column = objAddRole.tblUserList.getColumnModel().getColumn(PERSON_NAME);
        column.setPreferredWidth(160);
        column.setResizable(true);
        
        column = objAddRole.tblUserList.getColumnModel().getColumn(MAIL_ID);
        column.setPreferredWidth(200);
        column.setResizable(true);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        objAddRole.tblUserList.setBackground(bgColor);
    }
    
    /* Return a vector of PersonRecipientBean*/
    private CoeusVector fetchRolePerson(int roleId, String RoleQualifier) {
        CoeusVector vecPersonInfo = null;
        try {
            Vector dataObjects = new Vector();
            dataObjects.add(moduleCode);
            dataObjects.add(moduleItemKey);
            dataObjects.add(moduleItemKeySequence);
            dataObjects.add(roleId);
            dataObjects.add(RoleQualifier);
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(GET_ROLE_PERSON_INFO);
            requester.setDataObjects(dataObjects);
            AppletServletCommunicator communicator = new AppletServletCommunicator(connectTo, requester);
            communicator.send();
            ResponderBean responder = communicator.getResponse();
            if(responder != null) {
                if(responder.hasResponse()) {
                    vecPersonInfo = (CoeusVector) responder.getDataObject();
                }
            } else {
                throw new CoeusException(responder.getMessage());
            }
        } catch(CoeusException ce) {
            ce.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return vecPersonInfo;
    }
}
