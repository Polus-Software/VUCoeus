/*
 * MailActionController.java
 *
 * Created on May 17, 2007, 2:43 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.MailActionListForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Or;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
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
public class MailActionController extends AdminController implements ActionListener, MouseListener,ListSelectionListener,KeyListener {
    
    private static final int WIDTH = 682 ;
    
    private static final int HEIGHT = 500;
    
    private static final int STATUS = 0;
    
    private static final int ACTION_NAME = 1;
    
    private static final int MODULE = 2;
    
    private static final String EMPTY_STRING = "";
    
    private static final String WINDOW_TITLE = "Actions in Coeus";
    
    private static final char UPDATE_MAIL_ACTION = 'a';
    
    private final String PERSON_ROLE_SERVLET ="/personRoleServlet";
    
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ PERSON_ROLE_SERVLET;
    
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusVector cvMailActionData;
    
    private CoeusVector cvNewMailActionData;
    
    private MailActionListForm mailListForm;
    
    private CoeusDlgWindow dlgMailAction;
    
    private boolean isModified = false;
    
    private boolean sortCodeAsc = true;
    
    private MailListTableModel mailListTableModel;
    
    private static final char GET_ACTIONS_FOR_EMAIL_NOTIF = 'G';
    
    private static final char GET_ACTION_LIST = 'W';
    
    // 4598: No rights checking for Admin --> Notification Recipients - Start
    private static final String MAINTAIN_NOTIFICATIONS = "MAINTAIN_NOTIFICATIONS";
    private static final String FN_USER_HAS_OSP_RIGHT = "FN_USER_HAS_OSP_RIGHT";
    private static final String FUNCTION_SERVLET = "/coeusFunctionsServlet";
    // 4598: No rights checking for Admin --> Notification Recipients - End
    
    /** Creates a new instance of MailActionController */
    public MailActionController(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvMailActionData = new CoeusVector();
        cvNewMailActionData = new CoeusVector();
        postInitComponents();
        registerComponents();
        setFormData(null);
        setTableColumn();
        formatFields();
        // 4598: No rights checking for Admin --> Notification Recipients
        enableDisableButtons();
    }
    
    /**
     * Initializes the components
     */
    private void postInitComponents() {
        mailListForm = new MailActionListForm();
        dlgMailAction = new CoeusDlgWindow(mdiForm);
        dlgMailAction.setResizable(false);
        dlgMailAction.setModal(true);
        dlgMailAction.getContentPane().add(mailListForm);
        dlgMailAction.setFont(CoeusFontFactory.getLabelFont());
        dlgMailAction.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgMailAction.setSize(WIDTH, HEIGHT);
        dlgMailAction.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgMailAction.getSize();
        dlgMailAction.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgMailAction.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgMailAction.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        dlgMailAction.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    /**
     * Used to set window focus 
     */
    public void setWindowFocus() {
        mailListForm.scrPnMailAction.requestFocusInWindow();
    }
    
    /**
     * Used to perform the operation when cancel button is pressed
     */
    private void performCancelAction() {
        if(saveRequired()){
            confirmClosing();
        }else{
            dlgMailAction.setVisible(false);
        }
    }
    
    /**
     * Used to do the operation to do before closing the form
     */
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                if(validate()){
                    saveMailAction();
                    dlgMailAction.setVisible(false);
                }
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setSaveRequired(false);
                dlgMailAction.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    /**
     * Used to display the form
     */
    public void display() {
        mailListForm.tblMailAction.setRowSelectionAllowed(true);
        if(mailListForm.tblMailAction.getRowCount() > 0) {
            mailListForm.tblMailAction.setRowSelectionInterval(0,0);
        }
        dlgMailAction.setVisible(true);
    }
    
    /**
     * Used to set the columns to table
     */
    private void setTableColumn() {
        JTableHeader tableHeader = mailListForm.tblMailAction.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        mailListForm.tblMailAction.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        mailListForm.tblMailAction.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mailListForm.tblMailAction.setCellSelectionEnabled(false);
        
        TableColumn column = mailListForm.tblMailAction.getColumnModel().getColumn(STATUS);
        column.setPreferredWidth(100);
        column.setResizable(true);
        
        column = mailListForm.tblMailAction.getColumnModel().getColumn(ACTION_NAME);
        column.setPreferredWidth(260);
        column.setResizable(true);
        
        column = mailListForm.tblMailAction.getColumnModel().getColumn(MODULE);
        column.setPreferredWidth(200);
        column.setResizable(true);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        mailListForm.tblMailAction.setBackground(bgColor);
    }
    
    /**
     * Used to format the fields
     */
    public void formatFields() {
    }
    
    /**
     * Returns the ui component
     * @return java.awt.Component
     */
    public java.awt.Component getControlledUI() {
        return mailListForm;
    }
    
    /**
     * Returns the form data
     * @return Object
     */
    public Object getFormData() {
        return null;
    }
    
    /**
     * Used to register components
     */
    public void registerComponents() {
        mailListTableModel = new MailListTableModel();
        java.awt.Component[] components = {mailListForm.scrPnMailAction,
        mailListForm.btnOk,
        mailListForm.btnCancel,
        mailListForm.btnModify};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        mailListForm.setFocusTraversalPolicy(traversePolicy);
        mailListForm.setFocusCycleRoot(true);
        mailListForm.btnModify.addActionListener(this);
        mailListForm.btnCancel.addActionListener(this);
        mailListForm.btnOk.addActionListener(this);
        mailListForm.tblMailAction.getTableHeader().addMouseListener(this);
        mailListForm.tblMailAction.setModel(mailListTableModel);
        ListSelectionModel selectionModel = mailListForm.tblMailAction.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        mailListForm.tblMailAction.setSelectionModel(selectionModel);
        mailListForm.tblMailAction.addKeyListener(this);
    }
    
    /**
     * Used to set the object to data when form is initialized
     * @param Object
     */
    public void setFormData(Object data) {
        try {
            Hashtable mailListData;
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            mailListData = getMailActionList(GET_ACTIONS_FOR_EMAIL_NOTIF);
            Hashtable htMailActionList = getMailActionList(GET_ACTION_LIST);
//            cvMailActionData = (CoeusVector)mailListData.get(PersonRoleInfoBean.class);
            CoeusVector cvMailActionList = (CoeusVector) htMailActionList.get(PersonRoleInfoBean.class);
//            cvMailActionData = setStatus(cvMailActionData, cvMailActionList);
//            mailListTableModel.setData(cvMailActionData);
            //COEUSDEV-75:End
            mailListTableModel.setData(cvMailActionList);
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }catch(CoeusException ce) {
            ce.printStackTrace();
        }
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
     * Used to save the data
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * Used to do validations
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /**
     * Used to perform any actions
     * @param ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        dlgMailAction.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(mailListForm.btnCancel)) {
            performCancelAction();
        }else if(source.equals(mailListForm.btnModify)) {
            performAddAction();
        }else if(source.equals(mailListForm.btnOk)) {
            performOkAction();
        }
        dlgMailAction.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Used to do add the new action.
     */
    public void performAddAction() {
//        Hashtable hData= null;
        try {
            Hashtable htData = new Hashtable();
            int selectedRow = mailListForm.tblMailAction.getSelectedRow();
            htData.put(CoeusVector.class, cvMailActionData.get(selectedRow));
            ModifyRecipientsController modifyRecipientsController = new ModifyRecipientsController(htData, 'N');
            
            Hashtable hData = modifyRecipientsController.showMailList();
            if(hData == null) {
                return;
            }
            Set keySet = hData.keySet();
            for(Iterator i = keySet.iterator(); i.hasNext(); ) {
                PersonRoleInfoBean mailInfoBean = (PersonRoleInfoBean) hData.get(i.next());
                if(mailInfoBean!= null){
//                    cvMailActionData.add(mailInfoBean);
                    cvMailActionData.removeElementAt(selectedRow);
                    cvMailActionData.add(selectedRow,mailInfoBean);
                    //cvNewMailActionData.add(mailInfoBean);
                    cvMailActionData.set(selectedRow,mailInfoBean);
                    mailListForm.tblMailAction.setRowSelectionInterval(selectedRow ,selectedRow );
                    mailListForm.tblMailAction.scrollRectToVisible(
                            mailListForm.tblMailAction.getCellRect(selectedRow ,0, true));
                    mailListTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
                }
            }
        }catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    /**
     * Used to modify an action
     */
    public void performModifyAction() {
        Hashtable htModify= new Hashtable();
        Hashtable hData=new Hashtable();
        int selectedRow = mailListForm.tblMailAction.getSelectedRow();
//        if(selectedRow == -1){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("mailActionExceptionCode.1051"));
//            return ;
//        }
        PersonRoleInfoBean mailActionInfoBean =(PersonRoleInfoBean)cvMailActionData.get(selectedRow);
        htModify.put(PersonRoleInfoBean.class, mailActionInfoBean);
        htModify.put(CoeusVector.class,cvMailActionData);
        try {
            AddModifyMailListController addModifyMailListController = new AddModifyMailListController(htModify, TypeConstants.MODIFY_MODE);
            hData = addModifyMailListController.showMailList();
        }catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        if(hData == null) {
            return;
        }
        PersonRoleInfoBean mailInfoBean =(PersonRoleInfoBean)hData.get(PersonRoleInfoBean.class);
        if(mailInfoBean!= null){
            if(mailInfoBean.getAcType() != null) {
                isModified= true;
                cvMailActionData.removeElementAt(selectedRow);
                cvMailActionData.add(selectedRow,mailInfoBean);
                //cvNewMailActionData.add(mailInfoBean);
                cvMailActionData.set(selectedRow,mailInfoBean);
                mailListForm.tblMailAction.setRowSelectionInterval(selectedRow ,selectedRow );
                mailListForm.tblMailAction.scrollRectToVisible(
                        mailListForm.tblMailAction.getCellRect(selectedRow ,0, true));
                mailListTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            }
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
                dlgMailAction.setVisible(false);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Used to delete an action
     */
    public void performDeleteAction() {
        try {
            int selectedRow = mailListForm.tblMailAction.getSelectedRow();
            if(selectedRow == -1){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("mailActionExceptionCode.1002"));
                return;
            }
            PersonRoleInfoBean mailInfoBean =(PersonRoleInfoBean)cvMailActionData.get(selectedRow);

            int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("mailActionExceptionCode.1001"),
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
    public void deleteRow(PersonRoleInfoBean mailInfoBean,int selectedRow) throws CoeusClientException {
        mailInfoBean.setAcType(TypeConstants.DELETE_RECORD);
        cvMailActionData.removeElementAt(selectedRow);
        cvNewMailActionData.addElement(mailInfoBean);
        mailListTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
        mailListTableModel.fireTableDataChanged();
    }
    
    /**
     * Used to save
     */
    private boolean saveRequired() {
        Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        cvNewMailActionData.addAll(cvMailActionData.filter(insertOrUpdate));
        if(cvNewMailActionData.size() > 0) {
            isModified = true;
        }
        return isModified;
    }
    
    /**
     * Used to do the action when ok button is pressed
     */
    public void performOkAction() {
        if(saveRequired()) {
            saveMailAction();
        }else {
            dlgMailAction.setVisible(false);
        }
    }
    
    /**
     * Usecd to save the list
     */
    private void saveMailAction() {
        try{
            Hashtable hModified= new Hashtable();
            hModified.put(PersonRoleInfoBean.class,cvNewMailActionData);
            saveModifiedMailList(hModified);
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /**
     * Used to do mouse clicked operations
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        Point clickedPoint = mouseEvent.getPoint();
        int xPosition = (int)clickedPoint.getX();
        int selectedRow = mailListForm.tblMailAction.getSelectedRow();
        PersonRoleInfoBean mailInfoBean=(PersonRoleInfoBean)cvMailActionData.get(selectedRow);
        
        int columnIndex = mailListForm.tblMailAction.getColumnModel().getColumnIndexAtX(xPosition);
        switch (columnIndex) {
            case STATUS:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvMailActionData.sort("sendNotification", false,true);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvMailActionData.sort("sendNotification", true,true);
                    sortCodeAsc = true;
                }
                break;
            case ACTION_NAME:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvMailActionData.sort("actionName", false,true);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvMailActionData.sort("actionName", true,true);
                    sortCodeAsc = true;
                }
                break;
            case MODULE:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvMailActionData.sort("moduleDescription", false);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvMailActionData.sort("moduleDescription", true);
                    sortCodeAsc = true;
                }
                break;
        }//End Switch
        
        mailListTableModel.fireTableDataChanged();
        mailListForm.tblMailAction.setRowSelectionInterval(cvMailActionData.indexOf(mailInfoBean),cvMailActionData.indexOf(mailInfoBean));
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
        int selectedRow=mailListForm.tblMailAction.getSelectedRow();
        if(source == KeyEvent.VK_DOWN) {
            mailListForm.tblMailAction.requestFocusInWindow();
            mailListForm.tblMailAction.setRowSelectionInterval(selectedRow ,selectedRow);
        }else if(source == KeyEvent.VK_UP) {
            mailListForm.tblMailAction.requestFocusInWindow();
            mailListForm.tblMailAction.setRowSelectionInterval(selectedRow ,selectedRow);
        }
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
    class MailListTableModel extends AbstractTableModel {
        String colNames[] = {"Status","Action Name","Module"};
        Class[] colTypes = new Class[] {String.class,String.class,String.class};
        
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
                case STATUS:
                    if(mailInfoBean.getSendNotification()) {
                        return "Active";
                    } else {
                        return "Inactive";
                    }
                case ACTION_NAME:
                    return mailInfoBean.getActionName();
                case MODULE:
                    return mailInfoBean.getModuleDescription();
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
                case STATUS:
                    if(value != null && value.toString().equals("Active")) {
                        mailInfoBean.setSendNotification(true);
                    } else {
                        mailInfoBean.setSendNotification(false);
                    }
                case ACTION_NAME:
                    mailInfoBean.setActionName(value.toString());
                case MODULE:
                    mailInfoBean.setModuleCode(value.toString());
            }
        }
        
    }
    
    private CoeusVector setStatus(CoeusVector cvMailData, CoeusVector cvMailList) {
        CoeusVector cvData = new CoeusVector();
        CoeusVector cvMailListData = null;
        if(cvMailData != null && cvMailData.size() > 0) {
            for(int index = 0; index < cvMailData.size(); index++) {
                PersonRoleInfoBean personInfoBean = (PersonRoleInfoBean) cvMailData.get(index);
                String actionCode = personInfoBean.getActionCode();
                String moduleCode = personInfoBean.getModuleCode();
                Equals eqActionCode = new Equals("actionCode", actionCode);
                Equals eqModuleCode = new Equals("moduleCode", moduleCode);
                And eqActionAndModule = new And(eqActionCode, eqModuleCode);
                
                if(cvMailList != null) {
                    cvMailListData = cvMailList.filter(eqActionAndModule);
                }
                if(cvMailListData != null && cvMailListData.size() > 0) {
                    PersonRoleInfoBean personRoleInfoBean = (PersonRoleInfoBean) cvMailListData.get(0);
                    int notificationNumber = personRoleInfoBean.getNotificationNumber();
                    boolean sendNotification = personRoleInfoBean.getSendNotification();
                    personInfoBean.setNotificationNumber(notificationNumber);
                    personInfoBean.setSendNotification(sendNotification);
                }
                cvData.add(personInfoBean);
            }
        }
        return cvData;
    }

    // 4598: No rights checking for Admin --> Notification Recipients - Start
    private void enableDisableButtons() {
        boolean hasRight = fetchNotificationRight();
        mailListForm.btnModify.setEnabled(hasRight);
    }
    
    
    private boolean fetchNotificationRight() {
        boolean hasNotificationRight = false;
        String connectToServlet = CoeusGuiConstants.CONNECTION_URL+ FUNCTION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(MAINTAIN_NOTIFICATIONS);
        request.setDataObject(FN_USER_HAS_OSP_RIGHT);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectToServlet, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()) {
            if(response.getDataObject() != null){
                Boolean right = (Boolean) response.getDataObject();
                hasNotificationRight = right.booleanValue();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                hasNotificationRight = false ;
            }
        }
        
        return  hasNotificationRight ;
        
    }
    // 4598: No rights checking for Admin --> Notification Recipients - End

}
