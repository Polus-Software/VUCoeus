/*
 * ModifyRecipientsController.java
 *
 * Created on October 18, 2007, 12:14 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 15-JULY-2010
 * by Johncy M John
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.AddRecipients;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Or;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author talarianand
 */
public class ModifyRecipientsController extends AdminController implements ActionListener,
        FocusListener {
    
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusAppletMDIForm mdiForm;
    
    private AddRecipients objModifyRoles;
    
    private CoeusDlgWindow mailListDlgWindow;
    
    private static final int WIDTH = 650;
    
    private static final int HEIGHT = 490;
    
    private PersonRoleInfoBean mailListInfoBean;
    
    private String EMPTY_STRING = "";
    
    private static final char GET_MAIL_ACTION_LIST = 'U';
    
    private final String PERSON_ROLE_SERVLET ="/personRoleServlet";
    
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ PERSON_ROLE_SERVLET;
    
    private boolean modified;
    
    private Hashtable htMailListData;
    
    private CoeusVector cvMailActionData;
    
    private static final int ROLE_NAME = 0;
    
    private MailListTableModel mailListTableModel;
    
    private static final char UPDATE_MAIL_ACTION = 'a';
    
    private static final char UPDATE_MAIL_CONTENT = 'P';
    
    private static final char GET_MESSAGE_CONTENT = 'm';
    
    private static final char GET_NOTIFICATION_NUMBER = 'N';
    
    //Commented For PMD Check
    //private static final char GET_ACTION_LIST = 'W';
    
    private CoeusVector cvUpdateData;
    
    private boolean updateContent = false;
    
    private boolean insertRole = false;
    
    boolean userPrompt = false;
    
    /** Creates a new instance of ModifyRecipientsController */
    public ModifyRecipientsController(Hashtable customElementData, char functionType) throws CoeusException{
        htMailListData = customElementData;
        cvMailActionData = new CoeusVector();
        cvUpdateData = new CoeusVector();
        setFunctionType(functionType);
        initComponents();
        registerComponents();
        setFormData(customElementData);
        setTableColumn();
    }
    
    /**
     * Initializes the UI components with all the components
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        mdiForm = CoeusGuiConstants.getMDIForm();
        mailListDlgWindow = new CoeusDlgWindow(mdiForm, true);
        objModifyRoles = new AddRecipients();
        mailListDlgWindow.setResizable(false);
        mailListDlgWindow.getContentPane().add(objModifyRoles);
        mailListDlgWindow.setTitle("Modify Notification Details");
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
//        objModifyRoles.cmbActionName.requestFocusInWindow();
        objModifyRoles.txtSubject.requestFocusInWindow();
        mailListDlgWindow.setVisible(true);
    }
    /**
     * Used to set the columns to table
     */
    private void setTableColumn() {
        JTableHeader tableHeader = objModifyRoles.tblRoles.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0, 0));
        objModifyRoles.tblRoles.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        objModifyRoles.tblRoles.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        objModifyRoles.tblRoles.setCellSelectionEnabled(false);
        
        TableColumn column = objModifyRoles.tblRoles.getColumnModel().getColumn(ROLE_NAME);
        column.setPreferredWidth(460);
        column.setResizable(true);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        objModifyRoles.tblRoles.setBackground(bgColor);
    }
    
    /**
     * Sets the focus when the form is displayed
     */
    private void setRequestFocusInWindow() {
        objModifyRoles.btnCancel.requestFocusInWindow();
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
        objModifyRoles.rdBtnGroup = new javax.swing.ButtonGroup();
        objModifyRoles.rdBtnGroup.add(objModifyRoles.rdBtnPromptUserYes);
        objModifyRoles.rdBtnGroup.add(objModifyRoles.rdBtnPromptUserNo);
        
        objModifyRoles.rdBtnStatusGroup = new javax.swing.ButtonGroup();
        objModifyRoles.rdBtnStatusGroup.add(objModifyRoles.rdStatusActive);
        objModifyRoles.rdBtnStatusGroup.add(objModifyRoles.rdStatusInActive);
        
        mailListTableModel = new MailListTableModel();
        Component[] component = {objModifyRoles.txtActionName,
        objModifyRoles.btnOk, objModifyRoles.btnCancel, objModifyRoles.btnAdd,
        objModifyRoles.btnDelete, objModifyRoles.txtSubject, objModifyRoles.txtMessage, 
        objModifyRoles.txtModule, objModifyRoles.rdBtnPromptUserYes, 
        objModifyRoles.rdBtnPromptUserNo, objModifyRoles.rdStatusActive, 
        objModifyRoles.rdStatusInActive};
        objModifyRoles.tblRoles.setModel(mailListTableModel);
        
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        objModifyRoles.setFocusTraversalPolicy(policy);
        objModifyRoles.setFocusCycleRoot(true);
        
        LimitedPlainDocument limitedPlainDocument = new LimitedPlainDocument(30);
        limitedPlainDocument.setFilter(LimitedPlainDocument.FORCE_UPPERCASE);
        objModifyRoles.btnCancel.addActionListener(this);
        objModifyRoles.btnOk.addActionListener(this);
        objModifyRoles.btnAdd.addActionListener(this);
        objModifyRoles.btnDelete.addActionListener(this);
        objModifyRoles.txtSubject.addFocusListener(this);
        objModifyRoles.txtMessage.addFocusListener(this);
        objModifyRoles.rdBtnPromptUserNo.addFocusListener(this);
        objModifyRoles.rdBtnPromptUserYes.addFocusListener(this);
        objModifyRoles.rdStatusActive.addFocusListener(this);
        objModifyRoles.rdStatusInActive.addFocusListener(this);
        //COEUSDEV:971 - Front end application entry of notification body text is not allowing for 4000 characters - Start
        //Allowing Message body upto 4000 characters
        //Message body is resticted for 500 charactes - start
        objModifyRoles.txtMessage.setDocument( new LimitedPlainDocument(4000));
        //Message body is resticted for 500 charactes - end
        //COEUSDEV:971 - End
        objModifyRoles.tblRoles.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent me) {
               int selectedRow = objModifyRoles.tblRoles.getSelectedRow();
               objModifyRoles.tblRoles.setRowSelectionAllowed(true);
               objModifyRoles.tblRoles.setRowSelectionInterval(selectedRow, selectedRow);
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
        Hashtable mailListData;
        if(data != null) {
            Hashtable elementData = (Hashtable)data;
            mailListInfoBean = (PersonRoleInfoBean)elementData.get(CoeusVector.class);
            elementData = null;
        }else{
            mailListInfoBean = new PersonRoleInfoBean();
        }
        try {
            mailListData = getMailActionList(GET_MAIL_ACTION_LIST);
            cvMailActionData = (CoeusVector)mailListData.get(PersonRoleInfoBean.class);
            CoeusVector cvQualifier = getDataForCombo();
            cvMailActionData = getQualifierList(cvMailActionData, cvQualifier);
            String actionId = mailListInfoBean.getActionCode();
            String moduleCode = mailListInfoBean.getModuleCode();
            Equals eqActionCode = new Equals("actionCode", actionId);
            Equals eqModuleCode = new Equals("moduleCode", moduleCode);
            And eqActionAndModule = new And(eqActionCode, eqModuleCode);
            
            mailListTableModel.setData(cvMailActionData.filter(eqActionAndModule));
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            MailMessageInfoBean contentBean = getMailContent(actionId, moduleCode);
            if(contentBean != null) {
                objModifyRoles.txtSubject.setText(contentBean.getSubject());
                objModifyRoles.txtMessage.setText(contentBean.getMessage());
                if(contentBean.isPromptUser()) {
                    objModifyRoles.rdBtnPromptUserYes.setSelected(true);
                } else {
                    objModifyRoles.rdBtnPromptUserNo.setSelected(true);
                }

                if(contentBean.isActive()) {
                    objModifyRoles.rdStatusActive.setSelected(true);
                } else {
                    objModifyRoles.rdStatusInActive.setSelected(true);
                }

                mailListInfoBean.setNotificationNumber(contentBean.getNotificationNumber());
                mailListInfoBean.setSubject(contentBean.getSubject());
                mailListInfoBean.setMessageBody(contentBean.getMessage());
                mailListInfoBean.setPromptUser(contentBean.isPromptUser());
                mailListInfoBean.setUpdateTimestamp(contentBean.getUpdateTimestamp());
                if(contentBean.isSystemGenerated()){
                    objModifyRoles.rdBtnPromptUserNo.setEnabled(false);
                    objModifyRoles.rdBtnPromptUserYes.setEnabled(false);
                    objModifyRoles.btnAdd.setEnabled(false);
                    objModifyRoles.btnDelete.setEnabled(false);
                    objModifyRoles.lblSysGenerated.setVisible(true);
                }else{
                    objModifyRoles.lblSysGenerated.setVisible(false);
                }
                //COEUSDEV-75 End
            } else {
                setFunctionType('I');
                int notificationNumber = getNotificationNumber();
                mailListInfoBean.setNotificationNumber(notificationNumber);
                objModifyRoles.rdBtnPromptUserNo.setSelected(true);
                objModifyRoles.rdStatusInActive.setSelected(true);
            }
            
        } catch(CoeusClientException ce) {
            ce.printStackTrace();
        }
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        objModifyRoles.txtActionName.setText(mailListInfoBean.getActionName());
        objModifyRoles.txtActionName.setEnabled(false);
        objModifyRoles.txtActionName.setBackground(bgColor);
        objModifyRoles.txtModule.setText(mailListInfoBean.getModuleDescription());
        objModifyRoles.txtModule.setEnabled(false);
        objModifyRoles.txtModule.setBackground(bgColor);
    }
    
    /**
     * Used to get the qualifier data
     * @param MailActionList, QualifierList
     * @return CoeusVector
     */
    private CoeusVector getQualifierList(CoeusVector cvMailAction, CoeusVector cvQualifier) {
        CoeusVector cvModList = new CoeusVector();
        if(cvMailAction != null && cvMailAction.size() > 0) {
            PersonRoleInfoBean personBean = null;
            for(int index = 0; index < cvMailAction.size(); index++) {
                personBean = new PersonRoleInfoBean();
                personBean = (PersonRoleInfoBean) cvMailAction.get(index);
                if(personBean.getRoleQualifier() != null && personBean.getRoleQualifier().length() > 0) {
                    personBean.setQualifierCode(personBean.getRoleQualifier());
                    String qualifier = fetchQualifier(personBean.getRoleCode(), personBean.getRoleQualifier(), cvQualifier);
                    personBean.setRoleQualifier(qualifier);
                }
                cvModList.addElement(personBean);
            }
        }
        return cvModList;
    }
    
    /**
     * Used to get the qualifier name for a role
     * @param roleCode, qualifierCode, QualifierList
     * @return QualifierName
     */
    private String fetchQualifier(String roleCode, String qualifierCode, CoeusVector cvQualifier) {
        String qualifier = "";
        Equals operator = new Equals("roleCode", roleCode);
        CoeusVector cvData = cvQualifier.filter(operator);
        if(cvData != null && cvData.size() > 0) {
            PersonRoleInfoBean personRoleBean = new PersonRoleInfoBean();
            for(int index = 0; index < cvData.size(); index++) {
                personRoleBean = (PersonRoleInfoBean) cvData.get(index);
                if(personRoleBean.getQualifierCode() != null && personRoleBean.getQualifierCode().equals(qualifierCode)) {
                    qualifier = personRoleBean.getRoleQualifier();
                }
            }
        }
        return qualifier;
    }
    
    /**
     * Gets the required data from database to fill the combo boxes
     * based on the required conditions.
     * @param param which specifies about the function type
     * @return CoeusVector which holds list of values
     */
    private CoeusVector getDataForCombo() throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('Q');
        AppletServletCommunicator communicator = new AppletServletCommunicator(connectTo, requester);
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
     * Used to validate the form controls
     */
    public boolean validate() {
        boolean isModified = false;
        if(updateContent) {
            if(objModifyRoles.rdStatusActive.isSelected()) {
                mailListInfoBean.setSendNotification(true);
            } else if(objModifyRoles.rdStatusInActive.isSelected()) {
                mailListInfoBean.setSendNotification(false);
            }
            
            htMailListData.put("0", mailListInfoBean);
            isModified = true;
        }
        Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        cvUpdateData.addAll(cvMailActionData.filter(insertOrUpdate));
        if(cvUpdateData.size() > 0) {
            isModified = true;
        }
        return isModified;
    }
    
    /**
     * Performs the particalar action
     * @param ActionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        mailListDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(objModifyRoles.btnCancel)) {
            if(!performCancelAction()) {
                return;
            }
            mailListDlgWindow.dispose();
        }else if(source.equals(objModifyRoles.btnOk)) {
            performOkAction();
        } else if(source.equals(objModifyRoles.btnAdd)) {
            performAddAction();
        } else if(source.equals(objModifyRoles.btnDelete)) {
            performDeleteAction();
        }
        mailListDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Used to do add the new action.
     */
    public void performAddAction() {
        Hashtable hData= new Hashtable();
        try {
            String actionCode = mailListInfoBean.getActionCode();
            String moduleCode = mailListInfoBean.getModuleCode();
            Equals eqActionCode = new Equals("actionCode", actionCode);
            Equals eqModuleCode = new Equals("moduleCode", moduleCode);
            And eqActionAndModule = new And(eqActionCode, eqModuleCode);
//            Hashtable htMailActionList = getMailActionList(GET_ACTION_LIST);
//            CoeusVector cvMailActionList = (CoeusVector)htMailActionList.get(PersonRoleInfoBean.class);
            CoeusVector cvActionData = cvMailActionData.filter(eqActionAndModule);
            if(cvActionData != null && cvActionData.size() == 0) {
                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                setFunctionType('I');
//                int notificationNumber = getNotificationNumber();
//                mailListInfoBean.setNotificationNumber(notificationNumber);
                //COEUSDEV-75:End
                cvActionData.add(mailListInfoBean);
            }
            Hashtable htData = new Hashtable();
            htData.put(CoeusVector.class, cvActionData);
            AddModifyMailListController addModifyMailListController = new AddModifyMailListController(htData, 'N');
            hData=addModifyMailListController.showMailList();
        }catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        if(hData == null) {
            return;
        }
        CoeusVector cvActionData = (CoeusVector) hData.get(CoeusVector.class);
        if(cvActionData != null && cvActionData.size() > 0) {
            for(int index = 0; index < cvActionData.size(); index++) {
                PersonRoleInfoBean mailInfoBean =(PersonRoleInfoBean)cvActionData.get(index);
                if(mailInfoBean!= null){
                    if(mailInfoBean.getAcType() != null) {
                        insertRole = true;
                        if(getFunctionType() == 'I') {
                            updateContent = true;
                        }
                        cvMailActionData.add(mailInfoBean);
                        mailListTableModel.fireTableRowsInserted(cvMailActionData.size()-1,cvMailActionData.size()-1);
                        objModifyRoles.tblRoles.setRowSelectionAllowed(true);
                        objModifyRoles.tblRoles.setRowSelectionInterval(cvMailActionData.size()-1,cvMailActionData.size()-1);
                        objModifyRoles.tblRoles.scrollRectToVisible(
                                objModifyRoles.tblRoles.getCellRect(cvMailActionData.size()-1 ,0, true));
                    }
                }
            }
        }
    }
    
    /**
     * Is used to insert the values into the parent form.
     */
    private void performOkAction() {
        if(!validate()) {
            mailListDlgWindow.dispose();
            return;
        }
        if(updateContent) {
            udpateMailContent();
        }
        if(insertRole) {
            saveMailAction();
        }
    }
    
    /**
     * Is used to do the operations when the cancel button is pressed
     * @return boolean
     */
    private boolean performCancelAction() {
        if(isModified()) {
        int selOption = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1505"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
        if(selOption == CoeusOptionPane.SELECTION_YES) {
            if(!validate()) {
                return false;
            }
            if(updateContent) {
                udpateMailContent();
            }
            if(insertRole) {
                saveMailAction();
            }
            return true;
        } else if(selOption == CoeusOptionPane.SELECTION_NO){
            htMailListData = null;
            return true;
        } else {
            mailListDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return false;
        }
        }
        return true;
    }
    
    /**
     * Is used to display the form.
     * @return Hashtable which holds the newly inserted value
     */
    public Hashtable showMailList() {
        display();
        return htMailListData;
    }
    
    /**
     * Used to the mail actions list
     * @return MailList
     */
    public Hashtable getMailActionList(char functionType) throws CoeusClientException, CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable htMailListData = null;
        
        requesterBean.setFunctionType(functionType);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htMailListData = (Hashtable)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return htMailListData;
    }
    
    /**
     * Is used to check whether the form values are modified or not
     * @return boolean
     */
    private boolean isModified() {
        Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        CoeusVector cvTempData = cvMailActionData.filter(insertOrUpdate);
        if(cvTempData != null && cvTempData.size() > 0) {
            modified = true;
        } else if(updateContent || insertRole) {
            modified = true;
        }
        return modified;
    }
    
    class MailListTableModel extends AbstractTableModel {
        String colNames[] = {""};
        Class[] colTypes = new Class[] {String.class};
        
        MailListTableModel() {
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
            cvMailActionData=cvCustElements;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            return cvMailActionData.size();
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            PersonRoleInfoBean mailInfoBean = (PersonRoleInfoBean)cvMailActionData.get(row);
            switch(column) {
                case ROLE_NAME:
                    String roleQualifier = EMPTY_STRING;
                    String role = mailInfoBean.getRoleName();
                    if(!(mailInfoBean.getRoleQualifier().equals("0"))) {
                        roleQualifier = mailInfoBean.getRoleQualifier();
                    }
                    if(roleQualifier != null && roleQualifier != "") {
                        role = role + " ( "+roleQualifier+" )";
                    }
                    return role;
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(cvMailActionData== null) {
                return;
            }
            PersonRoleInfoBean mailInfoBean = (PersonRoleInfoBean)cvMailActionData.get(row);
            switch(column) {
                case ROLE_NAME:
                    mailInfoBean.setRoleName(value.toString());
            }
        }
        
    }
    /**
     * Used to update the mail content with subject, message and prompt user information
     */
    private void udpateMailContent() {
        PersonRoleInfoBean newBean = new PersonRoleInfoBean();
        newBean.setUpdateTimestamp(mailListInfoBean.getUpdateTimestamp());
        newBean.setNotificationNumber(mailListInfoBean.getNotificationNumber());
        newBean.setActionCode(mailListInfoBean.getActionCode());
        newBean.setActionName(mailListInfoBean.getActionName());
        newBean.setModuleCode(mailListInfoBean.getModuleCode());
        
        if(objModifyRoles.rdBtnPromptUserNo.isSelected()) {
            newBean.setPromptUser(false);
        } else if(objModifyRoles.rdBtnPromptUserYes.isSelected()) {
            newBean.setPromptUser(true);
        }
        
        if(objModifyRoles.rdStatusActive.isSelected()) {
            newBean.setSendNotification(true);
        } else if(objModifyRoles.rdStatusInActive.isSelected()) {
            newBean.setSendNotification(false);
        }
        
        newBean.setMessageBody(objModifyRoles.txtMessage.getText());
        newBean.setSubject(objModifyRoles.txtSubject.getText());
        if(getFunctionType() == 'I') {
            newBean.setAcType(TypeConstants.INSERT_RECORD);
        } else {
            newBean.setNotificationNumber(mailListInfoBean.getNotificationNumber());
            newBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        try {
            saveModifiedMailContent(newBean);
        } catch(CoeusClientException ce) {
            ce.printStackTrace();
        } catch(CoeusException ce) {
            ce.printStackTrace();
        }
    }
    
    private void saveMailAction() {
        try{
            Hashtable hModified= new Hashtable();
            hModified.put(PersonRoleInfoBean.class,cvUpdateData);
            saveModifiedMailList(hModified);
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /**
     * Used to Save the modified list to database
     * @param HashTable
     */
    public void saveModifiedMailList(Hashtable hData) throws CoeusClientException, CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        
        requesterBean.setFunctionType(UPDATE_MAIL_ACTION);
        requesterBean.setDataObject(hData);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                mailListDlgWindow.setVisible(false);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Used to Save the modified list to database
     * @param HashTable
     */
    public void saveModifiedMailContent(PersonRoleInfoBean personBean) throws CoeusClientException, CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        
        requesterBean.setFunctionType(UPDATE_MAIL_CONTENT);
        requesterBean.setDataObject(personBean);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                mailListDlgWindow.setVisible(false);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Used to get Mail Content from server
     */
    //COEUSDEV-75:Rework email engine so the email body is picked up from one place
    private MailMessageInfoBean getMailContent(String actionCode, String moduleCode) throws CoeusClientException, CoeusException {
        MailMessageInfoBean contentBean = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        
        HashMap hmData = new HashMap();
        hmData.put("ActionCode", actionCode);
        hmData.put("ModuleCode", moduleCode);
        
        requesterBean.setFunctionType(GET_MESSAGE_CONTENT);
        requesterBean.setDataObject(hmData);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                contentBean = (MailMessageInfoBean) responderBean.getDataObject();
            } else {
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return contentBean;
    }
    //COEUSDEV-75:End
    /**
     * Used to delete a role from the selected action
     */
    private void performDeleteAction() {
        try {
            int selectedRow = objModifyRoles.tblRoles.getSelectedRow();
            if(selectedRow == -1){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("mailActionExceptionCode.1002"));
                return;
            }
            PersonRoleInfoBean mailInfoBean =(PersonRoleInfoBean)cvMailActionData.get(selectedRow);

//            int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("mailActionExceptionCode.1001"),
//                    CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_CANCEL);
            int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("Are you sure you want to delete this recipient?"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_CANCEL);

            if(option == CoeusOptionPane.SELECTION_YES){
                deleteRow(mailInfoBean,selectedRow);
            }else if(option == CoeusOptionPane.SELECTION_NO){
                return;
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
            CoeusOptionPane.showDialog(coeusClientException);

        }
    }
    /**
     * Used to delete a row from list
     */
    public void deleteRow(PersonRoleInfoBean mailInfoBean, int selectedRow) throws CoeusClientException {
        mailInfoBean.setAcType(TypeConstants.DELETE_RECORD);
        cvMailActionData.removeElementAt(selectedRow);
        cvUpdateData.addElement(mailInfoBean);
        mailListTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
        mailListTableModel.fireTableDataChanged();
        if(cvMailActionData != null && cvMailActionData.size() > 0) {
            objModifyRoles.tblRoles.setRowSelectionAllowed(true);
            objModifyRoles.tblRoles.setRowSelectionInterval(cvMailActionData.size()-1,cvMailActionData.size()-1);
        }
        insertRole = true;
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent focusEvent) {
        Object source = focusEvent.getSource();
        boolean sendNotification = false;
        if(source.equals(objModifyRoles.txtSubject)) {
            String subjectLine = objModifyRoles.txtSubject.getText();
            if(mailListInfoBean.getSubject() == null ) {
                if(subjectLine != null && subjectLine.length() > 0) {
                    updateContent = true;
                }
            } else if(mailListInfoBean.getSubject() != null || !(subjectLine.equals(mailListInfoBean.getSubject().trim()))) {
                updateContent = true;
            }
        } else if(source.equals(objModifyRoles.txtMessage)) {
            String messageBody = objModifyRoles.txtMessage.getText();
            if(mailListInfoBean.getMessageBody() == null) {
                if(messageBody != null && messageBody.length() > 0) {
                    updateContent = true;
                }
            } else if(mailListInfoBean.getMessageBody() != null || !(mailListInfoBean.getMessageBody().trim().equals(messageBody))) {
                updateContent = true;
            }
        } else if(source.equals(objModifyRoles.rdBtnPromptUserNo)) {
            if(objModifyRoles.rdBtnPromptUserNo.isSelected()) {
                userPrompt = false;
            }
        } else if(source.equals(objModifyRoles.rdBtnPromptUserYes)) {
            if(objModifyRoles.rdBtnPromptUserYes.isSelected()) {
                userPrompt = true;
            }
        } else if(source.equals(objModifyRoles.rdStatusActive)) {
            if(objModifyRoles.rdStatusActive.isSelected()) {
                sendNotification = true;
            }
        } else if(source.equals(objModifyRoles.rdStatusInActive)) {
            if(objModifyRoles.rdStatusInActive.isSelected()) {
                sendNotification = false;
            }
        }
        if(!(userPrompt == mailListInfoBean.getPromptUser())) {
            updateContent = true;
        }
        if(!(sendNotification == mailListInfoBean.getSendNotification())) {
            updateContent = true;
        }
    }
    
    private int getNotificationNumber() throws CoeusClientException, CoeusException {
        int notificationNumber = -1;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        
        requesterBean.setFunctionType(GET_NOTIFICATION_NUMBER);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                notificationNumber = ((Integer) responderBean.getDataObject()).intValue();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return notificationNumber;
    }
    
}
