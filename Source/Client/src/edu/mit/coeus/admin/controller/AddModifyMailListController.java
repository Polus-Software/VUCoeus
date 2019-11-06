/*
 * AddModifyMailListController.java
 *
 * Created on May 21, 2007, 4:12 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.AddModifyMailList;
//import edu.mit.coeus.admin.gui.AddRecipients;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
//import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
//import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
//import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
//import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author talarianand
 */
public class AddModifyMailListController extends AdminController implements ActionListener, KeyListener {
    
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusAppletMDIForm mdiForm;
    
    private AddModifyMailList objAddModifyMailList;
    
    private CoeusDlgWindow mailListDlgWindow;
    
    private static final int WIDTH = 650;
    
    private static final int HEIGHT = 380;
    
    private PersonRoleInfoBean mailListInfoBean;
    
    private CoeusVector cvQualifierList;
    
    private String EMPTY_STRING = "";
    
    private CoeusVector cvRoleList;
    
//    private CoeusVector cvModuleList;
    
    private static final String GET_SERVLET = "/personRoleServlet";
    
//    private static final char GET_ACTION_LIST = 'W';
    
    private static final char GET_ROLE_LIST = 'R';
    
    private static final char GET_QUALIFIER_LIST = 'Q';
    
//    private static final char GET_MODULE_DATA = 'M';
    
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    public static final int MODULE_COLUMN = 0;
    
    public static final int REQUIRED_COLUMN = 1;
    
    private boolean modified;
    
//    private boolean comboItemChanged;
    
    private Hashtable htMailListData;
    
    private static final String MAIL_EXCEPTION = "mailActionExceptionCode.";
    
    private RoleTableModel roleTableModel;
    
    private QualifierTableModel qualifierTableModel;
    
    private CoeusVector cvUpdateData;
    
    /** Creates a new instance of AddModifyMailListController */
    public AddModifyMailListController(Hashtable customElementData, char functionType) throws CoeusException{
        htMailListData = customElementData;
        cvUpdateData = new CoeusVector();
        setFunctionType(functionType);
        initComponents();
        registerComponents();
        setFormData(customElementData);
        setColumnData();
    }
    
    /**
     * Initializes the UI components with all the components
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        mdiForm = CoeusGuiConstants.getMDIForm();
        mailListDlgWindow = new CoeusDlgWindow(mdiForm, true);
        objAddModifyMailList = new AddModifyMailList();
        mailListDlgWindow.setResizable(false);
        mailListDlgWindow.getContentPane().add(objAddModifyMailList);
//        mailListDlgWindow.setTitle("COnfigure Person roles for");
        mailListDlgWindow.setFont(CoeusFontFactory.getLabelFont());
        mailListDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        mailListDlgWindow.setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = mailListDlgWindow.getSize();
        mailListDlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        mailListDlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent actionEvent){
                if(!performCancelAction()) {
                    return;
                }
                mailListDlgWindow.dispose();
            }
        });
        mailListDlgWindow.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                if(!performCancelAction()) {
                    return;
                }
                mailListDlgWindow.dispose();
            }
        });
        
        mailListDlgWindow.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setRequestFocusInWindow();
            }
        });
    }
    
    /**
     * Displays the form
     */
    public void display() {
//        if(objAddModifyMailList.tblRoles.getRowCount() > 0) {
//            objAddModifyMailList.tblRoles.setRowSelectionAllowed(true);
//            objAddModifyMailList.tblRoles.setRowSelectionInterval(0,0);
//        }
        mailListDlgWindow.setVisible(true);
    }
    
    /**
     * Sets the focus when the form is displayed
     */
    private void setRequestFocusInWindow() {
        objAddModifyMailList.btnCancel.requestFocusInWindow();
    }
    
    /**
     * Formats the UI fields
     */
    public void formatFields() {
    }
    
    /**
     * Is used to get the UI component
     * @return Component
     */
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    /**
     * Returns the form data
     * @return Object
     */
    public Object getFormData() {
        return null;
    }
    
    /**
     * Registers all the UI components to the form
     */
    public void registerComponents() {
        roleTableModel = new RoleTableModel();
        objAddModifyMailList.tblRoles.setModel(roleTableModel);
        
        qualifierTableModel = new QualifierTableModel();
        objAddModifyMailList.tblQualifiers.setModel(qualifierTableModel);
        
        Component[] component = {objAddModifyMailList.btnOk, objAddModifyMailList.btnCancel};
        
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        objAddModifyMailList.setFocusTraversalPolicy(policy);
        objAddModifyMailList.setFocusCycleRoot(true);
        
        LimitedPlainDocument limitedPlainDocument = new LimitedPlainDocument(30);
        limitedPlainDocument.setFilter(LimitedPlainDocument.FORCE_UPPERCASE);
        objAddModifyMailList.btnCancel.addActionListener(this);
        objAddModifyMailList.btnOk.addActionListener(this);
        
        objAddModifyMailList.tblRoles.addKeyListener(this);
        
        try {
            if(htMailListData != null && htMailListData.size() > 0) {
                CoeusVector cvTempData = (CoeusVector) htMailListData.get(CoeusVector.class);
                if(cvTempData != null && cvTempData.size() > 0) {
                    mailListInfoBean = (PersonRoleInfoBean)cvTempData.get(0);
                }
            }
            cvRoleList = getDataForCombo(GET_ROLE_LIST);
        } catch(CoeusException ce) {
            ce.printStackTrace();
        }
        objAddModifyMailList.tblRoles.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent mouseEvent) {
                    int selectedRow = objAddModifyMailList.tblRoles.getSelectedRow();
                    objAddModifyMailList.tblRoles.setRowSelectionAllowed(true);
                    objAddModifyMailList.tblRoles.setRowSelectionInterval(selectedRow, selectedRow);
                    if(selectedRow != -1 && cvRoleList != null && cvRoleList.size() > selectedRow) {
                        try {
                            PersonRoleInfoBean personInfoBean = (PersonRoleInfoBean)cvRoleList.get(selectedRow);
                            cvQualifierList = getDataForTheTable(personInfoBean);
                            //Adding a new element to provide select All option
                            if(cvQualifierList != null && cvQualifierList.size() > 0) {
                                PersonRoleInfoBean selectAllBean = new PersonRoleInfoBean();
                                selectAllBean.setQualifierCode("-1");
                                selectAllBean.setRoleQualifier("All");
                                cvQualifierList.add(0, selectAllBean);
                            }
                            qualifierTableModel.setData(cvQualifierList);
                            if(objAddModifyMailList.tblQualifiers.getRowCount() > 0) {
                                objAddModifyMailList.tblQualifiers.setRowSelectionAllowed(true);
                                objAddModifyMailList.tblQualifiers.setRowSelectionInterval(0, 0);
                            }
                        } catch(CoeusException ce) {
                            ce.printStackTrace();
                        }
                    }
                }
            });
        
        objAddModifyMailList.tblQualifiers.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent mouseEvent) {
                    int selectedRow = objAddModifyMailList.tblQualifiers.getSelectedRow();
                    objAddModifyMailList.tblQualifiers.setRowSelectionAllowed(true);
                    objAddModifyMailList.tblQualifiers.setRowSelectionInterval(selectedRow, selectedRow);
                }
            });
    }
    
    /**
     * Saves the form Data
     */
    public void saveFormData() {
    }
    
    /**
     * Sets the initial data to form object
     * @param Object which holds data that has to be initialized when the UI component
     * invokes
     */
    public void setFormData(Object data) throws CoeusException {
        String title = EMPTY_STRING;
        if(data != null) {
            Hashtable elementData = (Hashtable)data;
            CoeusVector cvTempData = (CoeusVector) elementData.get(CoeusVector.class);
            if(cvTempData != null && cvTempData.size() > 0) {
                mailListInfoBean = (PersonRoleInfoBean)cvTempData.get(0);
                if(mailListInfoBean.getActionName() != null) {
                    title = "Person roles in Coeus";
                }
            }
            elementData = null;
        }else{
            mailListInfoBean = new PersonRoleInfoBean();
        }
        mailListDlgWindow.setTitle(title);
        
        String moduleCode = mailListInfoBean.getModuleCode();
        cvRoleList = getDataForCombo(GET_ROLE_LIST);
        
        roleTableModel.setData(cvRoleList);
//        objAddModifyMailList.tblRoles.setRowSelectionAllowed(true);
//        objAddModifyMailList.tblRoles.setRowSelectionInterval(0,0);
//        cvQualifierList = getDataForCombo(GET_QUALIFIER_LIST);
//        cvModuleList = getModuleData(GET_MODULE_DATA);
        
    }
    
    /**
     * Used to validate the form controls
     */
    public boolean validate() {
        
        String actionId = mailListInfoBean.getActionCode();
        int selectedRow = objAddModifyMailList.tblRoles.getSelectedRow();
        PersonRoleInfoBean selectedRoleBean = new PersonRoleInfoBean();
        if(selectedRow != -1 && cvRoleList != null && cvRoleList.size() > 0) {
            selectedRoleBean = (PersonRoleInfoBean) cvRoleList.get(selectedRow);
        }
        String roleId = selectedRoleBean.getRoleCode();
        int selRow = objAddModifyMailList.tblQualifiers.getSelectedRow();
        PersonRoleInfoBean selectedQualifierBean = new PersonRoleInfoBean();
        if(selRow != -1 && cvQualifierList != null && cvQualifierList.size() > 0) {
            selectedQualifierBean = (PersonRoleInfoBean) cvQualifierList.get(selRow);
        }
        String qualifierId = selectedQualifierBean.getQualifierCode();
        if(qualifierId != null && qualifierId.equals("-1")) {
            if(selectedQualifierBean.getRoleQualifier() != null 
                    && selectedQualifierBean.getRoleQualifier().equals("All")) {
                selectedQualifierBean = (PersonRoleInfoBean) cvQualifierList.get(selRow + 1);
                qualifierId = selectedQualifierBean.getQualifierCode();
            }
        }
        
        Equals operator = new Equals("roleCode", roleId);
        CoeusVector cvRoleDuplicate = ((CoeusVector) htMailListData.get(CoeusVector.class)).filter(operator);
        operator = new Equals("actionCode", actionId);
        CoeusVector cvActionDuplicate = cvRoleDuplicate.filter(operator);
        operator = new Equals("qualifierCode", qualifierId);
        CoeusVector cvQualDuplicate = new CoeusVector();
        if(qualifierId != null && qualifierId.length() > 0) {
            cvQualDuplicate = cvActionDuplicate.filter(operator);
        }
        if((cvRoleDuplicate != null && cvRoleDuplicate.size() > 0)
        && (cvActionDuplicate != null && cvActionDuplicate.size() > 0)) {
            if(qualifierId != null && qualifierId.length() > 0) {
                if(cvQualDuplicate != null && cvQualDuplicate.size() > 0) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MAIL_EXCEPTION+"1055"));
                    return false;
                }
            } else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MAIL_EXCEPTION+"1055"));
                return false;
            }
        }
        if(actionId == null || actionId == "") {
            String msg = coeusMessageResources.parseMessageKey(MAIL_EXCEPTION+"1052");
            CoeusOptionPane.showInfoDialog(msg + "Action.");
            return false;
        } else if(roleId == null || roleId == "") {
            String msg = coeusMessageResources.parseMessageKey(MAIL_EXCEPTION+"1052");
            CoeusOptionPane.showInfoDialog(msg + "Role.");
            return false;
        }
        return true;
    }
    
    /**
     * Performs the particalar action
     * @param ActionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        mailListDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(objAddModifyMailList.btnCancel)) {
            if(!performCancelAction()) {
                return;
            }
            mailListDlgWindow.dispose();
        }else if(source.equals(objAddModifyMailList.btnOk)) {
            performOkAction();
        }
        mailListDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Used to set the column data
     */
    private void setColumnData() {
        JTableHeader tableHeader = objAddModifyMailList.tblRoles.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(100, 20));
        objAddModifyMailList.tblRoles.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        objAddModifyMailList.tblRoles.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        objAddModifyMailList.tblRoles.setCellSelectionEnabled(false);
        objAddModifyMailList.jSplitPane1.setEnabled(false);
        TableColumn column = objAddModifyMailList.tblRoles.getColumnModel().getColumn(0);
        column.setPreferredWidth(263);
        column.setResizable(false);

        tableHeader = objAddModifyMailList.tblQualifiers.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(100, 20));
        objAddModifyMailList.tblQualifiers.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        objAddModifyMailList.tblQualifiers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        objAddModifyMailList.tblQualifiers.setAutoscrolls(true);
        objAddModifyMailList.tblQualifiers.setCellSelectionEnabled(false);
        
        column = objAddModifyMailList.tblQualifiers.getColumnModel().getColumn(0);
        column.setPreferredWidth(246);
        column.setResizable(false);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        objAddModifyMailList.tblRoles.setBackground(bgColor);
        objAddModifyMailList.tblQualifiers.setBackground(bgColor);
    }
    
    /**
     * This method is used to do the actions whenever a value is changed in UI
     * @param ListSelectinEvent
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
    }
    
    public class PressedEnter extends KeyAdapter {
        
        public void keyPressed(KeyEvent ae) {
            if(ae.getKeyCode() == KeyEvent.VK_ENTER) {
                performOkAction();
            }
        }
    }
    
    /**
     * Gets the required data from database to fill the combo boxes
     * based on the required conditions.
     * @param param which specifies about the function type
     * @return CoeusVector which holds list of values
     */
    private CoeusVector getDataForCombo(char param) throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        
        String moduleCode = EMPTY_STRING;
        if(mailListInfoBean != null) {
            moduleCode = mailListInfoBean.getModuleCode();
        }
        requester.setDataObject(moduleCode);
        requester.setFunctionType(param);
        AppletServletCommunicator communicator = new AppletServletCommunicator(connect,requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null) {
            if(responder.hasResponse()) {
                Hashtable htData = (Hashtable)responder.getDataObject();
                cvData = (CoeusVector)htData.get(PersonRoleInfoBean.class);
            }
        }else {
            //server error
            throw new CoeusException(responder.getMessage());
        }
        return cvData;
    }
    
    /**
     * Gets the required data from database to fill the combo boxes
     * based on the required conditions.
     * @param param which specifies about the function type
     * @return CoeusVector which holds list of values
     *
    private CoeusVector getModuleData(char param) throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(param);
        AppletServletCommunicator communicator = new AppletServletCommunicator(connect,requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null) {
            if(responder.hasResponse()) {
                cvData = (CoeusVector)responder.getDataObject();
            }
        }else {
            //server error
            throw new CoeusException(responder.getMessage());
        }
        return cvData;
    }
    
    /**
     * Gets the data from database 
     * @return CoeusVector which holds list of values
     */
    private CoeusVector getDataForTheTable(PersonRoleInfoBean personInfoBean) throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_QUALIFIER_LIST);
        requester.setDataObject(personInfoBean.getRoleCode());
        AppletServletCommunicator communicator = new AppletServletCommunicator(connect,requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null) {
            if(responder.hasResponse()) {
                Hashtable htData = (Hashtable)responder.getDataObject();
                cvData = (CoeusVector)htData.get(PersonRoleInfoBean.class);
            }
        }else {
            //server error
            throw new CoeusException(responder.getMessage());
        }
        return cvData;
    }
    
    /**
     * Is used to insert the values into the parent form.
     */
    private void performOkAction() {
        if(!validate()) {
            return;
        }
        isModified();
        mailListDlgWindow.dispose();
    }
    
    /**
     * Is used to do the operations when the cancel button is pressed
     * @return boolean
     */
    private boolean performCancelAction() {
//        if(isModified()) {
//            int selOption = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1505"),
//                    CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
//            if(selOption == CoeusOptionPane.SELECTION_YES) {
//                if(!validate()) {
//                    return false;
//                }
//                return true;
//            } else if(selOption == CoeusOptionPane.SELECTION_NO){
//                htMailListData = null;
//                return true;
//            } else {
//                mailListDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//                return false;
//            }
//        }
        return true;
    }
    
    /**
     * Is used to display the form.
     * @return Hashtable which holds the newly inserted value
     */
    public Hashtable showMailList() {
        display();
        htMailListData.put(CoeusVector.class, cvUpdateData);
        return htMailListData;
    }
    
    /**
     * Is used to check whether the form values are modified or not
     * @return boolean
     */
    private boolean isModified() {
        int selectedQualifier = objAddModifyMailList.tblQualifiers.getSelectedRow();
        PersonRoleInfoBean qualifierInfoBean = new PersonRoleInfoBean();
        if(selectedQualifier != -1) {
            qualifierInfoBean = (PersonRoleInfoBean)cvQualifierList.get(selectedQualifier);
        }
        String qualifierCode = qualifierInfoBean.getQualifierCode();
        String roleQualifier = qualifierInfoBean.getRoleQualifier();
        if(qualifierCode != null && qualifierCode.equals("-1") && roleQualifier.equals("All")) {
            NotEquals neAll = new NotEquals("qualifierCode", qualifierCode);
            CoeusVector cvData = cvQualifierList.filter(neAll);
            for(int index = 0; index < cvData.size(); index++) {
                PersonRoleInfoBean qualifierBean = (PersonRoleInfoBean)cvData.get(index);
                insertRoles(qualifierBean);
            }
        } else {
            insertRoles(qualifierInfoBean);
        }
        if (getFunctionType() == 'N') {
            modified = true;
        }
        return modified;
    }
    
    private void insertRoles(PersonRoleInfoBean qualifierBean) {
        PersonRoleInfoBean newBean = new PersonRoleInfoBean();
        if(getFunctionType() != 'N') {
            newBean.setUpdateTimestamp(mailListInfoBean.getUpdateTimestamp());
            newBean.setUpdateUser(mailListInfoBean.getUpdateUser());
        }
        newBean.setNotificationNumber(mailListInfoBean.getNotificationNumber());
        newBean.setActionCode(mailListInfoBean.getActionCode());
        newBean.setActionName(mailListInfoBean.getActionName());
        newBean.setModuleCode(mailListInfoBean.getModuleCode());
        newBean.setModuleDescription(mailListInfoBean.getModuleDescription());
        
        int selectedRole = objAddModifyMailList.tblRoles.getSelectedRow();
        PersonRoleInfoBean personInfoBean = new PersonRoleInfoBean();
        if(selectedRole != -1) {
            personInfoBean = (PersonRoleInfoBean)cvRoleList.get(selectedRole);
        }
        
        String roleId = personInfoBean.getRoleCode();
        newBean.setRoleCode(EMPTY_STRING.equals(roleId) ? "0" : roleId);
        newBean.setRoleName(personInfoBean.getRoleName());
        String qualifierCode = qualifierBean.getQualifierCode();
        String roleQualifier = qualifierBean.getRoleQualifier();
        if(qualifierCode == null || qualifierCode == "") {
            qualifierCode = "0";
            roleQualifier = EMPTY_STRING;
        }
        newBean.setRoleQualifier(roleQualifier);
        newBean.setQualifierCode(qualifierCode);
        if(personInfoBean.getAcType() == null) {
            if(getFunctionType() == 'N') {
                newBean.setAcType(TypeConstants.INSERT_RECORD);
            } else {
                newBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        
//        htMailListData.put(PersonRoleInfoBean.class, newBean);
        cvUpdateData.add(newBean);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent ke) {
        int selectedRow = objAddModifyMailList.tblRoles.getSelectedRow();
        objAddModifyMailList.tblRoles.setRowSelectionAllowed(true);
        objAddModifyMailList.tblRoles.setRowSelectionInterval(selectedRow, selectedRow);
        if(ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyCode() == KeyEvent.VK_TAB) {
            selectedRow = selectedRow + 1;
        } else if(ke.getKeyCode() == KeyEvent.VK_UP) {
            selectedRow = selectedRow - 1;
        }
        if(selectedRow != -1 && cvRoleList != null && cvRoleList.size() > selectedRow) {
            try {
                PersonRoleInfoBean personInfoBean = (PersonRoleInfoBean)cvRoleList.get(selectedRow);
                cvQualifierList = getDataForTheTable(personInfoBean);
                //Adding a new element to provide select All option
                if(cvQualifierList != null && cvQualifierList.size() > 0) {
                    PersonRoleInfoBean selectAllBean = new PersonRoleInfoBean();
                    selectAllBean.setQualifierCode("-1");
                    selectAllBean.setRoleQualifier("All");
                    cvQualifierList.add(0, selectAllBean);
                }
                qualifierTableModel.setData(cvQualifierList);
                if(objAddModifyMailList.tblQualifiers.getRowCount() > 0) {
                    objAddModifyMailList.tblQualifiers.setRowSelectionAllowed(true);
                    objAddModifyMailList.tblQualifiers.setRowSelectionInterval(0, 0);
                }
            } catch(CoeusException ce) {
                ce.printStackTrace();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    class RoleTableModel extends AbstractTableModel {
        String colNames[] = {"Role"};
        Class[] colTypes = new Class[] {String.class};
        CoeusVector cvRoleTableData;
        
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
            cvRoleTableData = cvCustElements;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvRoleTableData == null ||  cvRoleTableData.size()== 0){
                return 0;
            }else{
                return cvRoleTableData.size();
            }
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            PersonRoleInfoBean personBean = (PersonRoleInfoBean) cvRoleTableData.get(row);
            switch(column) {
                case 0:
                    return personBean.getRoleName();
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
        }
    }
    
    class QualifierTableModel extends AbstractTableModel {
        String colNames[] = {"Qualifier"};
        Class[] colTypes = new Class[] {String.class};
        CoeusVector cvQualifierData;
        
        QualifierTableModel() {
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
            cvQualifierData = cvCustElements;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvQualifierData == null ||  cvQualifierData.size()== 0){
                return 0;
            }else{
                return cvQualifierData.size();
            }
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            PersonRoleInfoBean personBean = (PersonRoleInfoBean) cvQualifierData.get(row);
            switch(column) {
                case 0:
                    return personBean.getRoleQualifier();
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
        }
    }
}
