/*
 * MailController.java
 *
 * Created on May 28, 2007, 7:05 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mail.bean.RoleRecipientBean;
import edu.mit.coeus.mail.gui.MailEditorForm;
import edu.mit.coeus.mail.gui.RoleListForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


// JM 6-26-2014 need for user information
import edu.mit.coeus.bean.UserInfoBean;
// JM END


/**
 *
 * @author talarianand
 */
public class MailController extends Controller implements ActionListener, ListSelectionListener, MouseListener {
    
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusDlgWindow mailUIDlgWindow;
    
    private MailEditorForm objMailUI;
    
    private static final int WIDTH = 920;
    
    private static final int HEIGHT = 450;
    
//    private MailActionInfoBean mailInfoBean;
        
    private Vector vecToRecipients;
        
    private RecipientsListCellRenderer renderer;
        
    /* used to specify to search in person table */
    private static final String PERSON_SEARCH = "personSearch";
    
    /* used in searching for rolodex */
    private static final String ROLODEX_SEARCH = "rolodexSearch";
        
    private static final char SEND_MAIL = 'C';
    private boolean mailSent = false;
    private MailMessageInfoBean mailMessageInfoBean = null;
    private int moduleCode;
    private int actionId;
    private String moduleItemKey;
    private int moduleItemKeySequence;
    private static final String mailConnect = CoeusGuiConstants.CONNECTION_URL+"/MailServlet";
    private static final String ERRKEY_SELECT_RECIPIENT = "mailFrm_exceptionCode.2004";
    private static final String MSGKEY_DELETE_CONFIRM = "mailFrm_exceptionCode.2005";
    private static final String ERRKEY_NO_RECIPIENT = "mailFrm_exceptionCode.2007";
    private static final String MSGKEY_NO_BODY_CONFIRM = "mailFrm_exceptionCode.2008";
    private CoeusMessageResources messageResources;
    private static final char GET_ROLE_PERSON_INFO = 'r';
    
    // JM 6-25-2014 added FROM user for mail message
    private String userId;
    private static final String SERVLET = "/userMaintenanceServlet";
    private static final char GET_USER_NAME = 'A';
    // JM END
    
    /** Creates a new instance of MailController */
    public MailController() {
    }
    
    /** Creates a new instance of MailController */
    //Modified the design of this class with COEUSDEV - 75 : Rework email Engine.
    public MailController(int moduleCode,int actionId,String moduleItemKey, int moduleItemKeySequence,MailMessageInfoBean infoBean) {
        this.moduleCode = moduleCode;
        this.actionId   = actionId;
        this.moduleItemKey = moduleItemKey;
        this.moduleItemKeySequence = moduleItemKeySequence;
        this.mailMessageInfoBean = infoBean;
        messageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        renderer = new RecipientsListCellRenderer();
        objMailUI.lstTo.setCellRenderer(renderer);
        setFormData(null);
        
    }
    
    /**
     * Used to initialize components
     */
    public void initComponents() {
        mdiForm = CoeusGuiConstants.getMDIForm();
        // JM 6-25-2014 get the user vunetid
        userId = mdiForm.getUserId();
        // JM END
        mailUIDlgWindow = new CoeusDlgWindow(mdiForm, true);
        objMailUI = new MailEditorForm();
        mailUIDlgWindow.setResizable(false);
        mailUIDlgWindow.getContentPane().add(objMailUI);
        mailUIDlgWindow.setTitle("Send Mail");
        mailUIDlgWindow.setFont(CoeusFontFactory.getLabelFont());
        mailUIDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        mailUIDlgWindow.setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = mailUIDlgWindow.getSize();
        mailUIDlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        mailUIDlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent actionEvent){
                //if(!performCancelAction()) return;
                mailUIDlgWindow.dispose();
            }
        });
        mailUIDlgWindow.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                //if(!performCancelAction()) return;
                mailUIDlgWindow.dispose();
            }
        });
        
        mailUIDlgWindow.addComponentListener(
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
        objMailUI.btnCancel.requestFocusInWindow();
    }
    
    /**
     * Used to display the form
     */
    public void display() {
        mailUIDlgWindow.setVisible(true);
    }
    
    /**
     * Used to save form data
     */
    public void saveFormData() {
    }
    
    /**
     * Used to register components
     */
    public void registerComponents() {
        Component[] component = {objMailUI.txtSubject, objMailUI.txtMessage,
        objMailUI.btnToRole, objMailUI.btnToPerson, objMailUI.btnAddRolodex,
        objMailUI.btnToDelete,  objMailUI.btnSend,
        objMailUI.lstTo, objMailUI.lblTo, objMailUI.lblSubject, objMailUI.lblMessage,
        objMailUI.btnCancel};
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        objMailUI.setFocusTraversalPolicy(policy);
        objMailUI.setFocusCycleRoot(true);
        
        LimitedPlainDocument limitedPlainDocument = new LimitedPlainDocument(30);
        limitedPlainDocument.setFilter(LimitedPlainDocument.FORCE_UPPERCASE);
        
        objMailUI.btnToPerson.addActionListener(this);
        objMailUI.btnToRole.addActionListener(this);
        objMailUI.btnToDelete.addActionListener(this);
        objMailUI.btnSend.addActionListener(this);
        objMailUI.btnAddRolodex.addActionListener(this);
        objMailUI.btnCancel.addActionListener(this);
        objMailUI.txtSubject.addActionListener(this);
        objMailUI.lstTo.addListSelectionListener(this);
        objMailUI.lstTo.addMouseListener(this);
    }
    
    /**
     * Used to do validations
     */
    public boolean validate() {
        return true;
    }
    
    /**
     * Used to format fields
     */
    public void formatFields() {
    }
    
    /**
     * Used to get the form data
     * @return Object
     */
    public Object getFormData() {
        return null;
    }
 
    
    /**
     * Used to initialize the data
     * @param Object
     */
    public void setFormData(Object data) {
        
        setRecipients();
        
        String subject = mailMessageInfoBean.getSubject();
        String message = mailMessageInfoBean.getMessage();
        
        if(subject != null) {
            objMailUI.txtSubject.setText(subject);
        }
        if(message != null) {
            objMailUI.txtMessage.setText(message);
        }
    }
    
    /**
     * Returns the Component
     * @return Component
     */
    public Component getControlledUI() {
        return null;
    }
    
    /**
     * Used to do the validations whenever a value is changed
     * @param ListSelectionEvent
     */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
    }
    
    /**
     * Is used to do any action
     * @param ActionEvent
     */
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        mailUIDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try{
            if(source.equals(objMailUI.btnToRole)) {
                performAddToRoleAction();
            }else if(source.equals(objMailUI.btnSend)) {
                performSendMailAction();
            } else if(source.equals(objMailUI.btnToDelete)) {
                performDeleteAction();
            } else if(source.equals(objMailUI.btnToPerson)) {
                performPersonSearch();
            } else if(source.equals(objMailUI.btnAddRolodex)) {
                performRoledexSearch();
            } else if(source.equals(objMailUI.btnCancel)) {
                mailUIDlgWindow.dispose();
            }
        }catch (CoeusException ex){
            ex.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        mailUIDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Used to do add a role to list
     */
    private void performAddToRoleAction() throws CoeusException{
        AddRoleController addRoleController = new AddRoleController(moduleCode, moduleItemKey,moduleItemKeySequence);
        RoleRecipientBean newRole = addRoleController.showRoleList();
        if(newRole != null ) {
            mailMessageInfoBean.addRecipient(newRole);
        }
        setRecipients();
    }
    
    /**
     * Used for person search
     */
    private void performPersonSearch() throws Exception {
        CoeusSearch coeusSearch = new CoeusSearch(mdiForm, PERSON_SEARCH,
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION);
        coeusSearch.showSearchWindow();
        Vector vSelectedPersons = coeusSearch.getMultipleSelectedRows();
        if(vSelectedPersons != null && vSelectedPersons.size() > 0) {
            PersonRecipientBean personRecipientBean ;
            HashMap hmSinglePerson = null;
            for(int index = 0; index < vSelectedPersons.size(); index++) {
                hmSinglePerson = (HashMap) vSelectedPersons.get(index);
                if(hmSinglePerson != null) {
                    personRecipientBean = new PersonRecipientBean();
                    personRecipientBean.setPersonId(Utils.convertNull((String)hmSinglePerson.get("PERSON_ID")));
                    personRecipientBean.setPersonName(Utils.convertNull((String)hmSinglePerson.get("FULL_NAME")));
                    personRecipientBean.setEmailId(Utils.convertNull((String)hmSinglePerson.get("EMAIL_ADDRESS")));
                    personRecipientBean.setUserId(Utils.convertNull((String)hmSinglePerson.get("USER_NAME")));
                    mailMessageInfoBean.addRecipient(personRecipientBean);
                }
            }
        }
        setRecipients();
    }
    
    /**
     * Used for Rolodex Search
     */
    private void performRoledexSearch() throws Exception {
        CoeusSearch coeusSearch = new CoeusSearch(mdiForm, ROLODEX_SEARCH,
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION);
        coeusSearch.showSearchWindow();
        Vector vSelectedPersons = coeusSearch.getMultipleSelectedRows();
        if(vSelectedPersons != null && vSelectedPersons.size() > 0) {
            PersonRecipientBean personRecipientBean ;
            HashMap hmSinglePerson = null;
            for(int index = 0; index < vSelectedPersons.size(); index++) {
                personRecipientBean = new PersonRecipientBean();
                hmSinglePerson = (HashMap) vSelectedPersons.get(index);
                if(hmSinglePerson != null) {
                    String personId = Utils.convertNull(hmSinglePerson.get("ROLODEX_ID"));
                    String lastName = Utils.convertNull(hmSinglePerson.get("LAST_NAME"));
                    String firstName = Utils.convertNull(hmSinglePerson.get("FIRST_NAME"));
                    String middleName = Utils.convertNull(hmSinglePerson.get("MIDDLE_NAME"));
                    String name = lastName+" "+firstName+" "+middleName;
                    
                    if ( lastName.length() > 0) {
                        name = ( lastName+" "+firstName+" "+middleName ).trim();
                    } else {
                        name = Utils.convertNull(
                                hmSinglePerson.get("ORGANIZATION") );
                    }
                    String mailId = (String)hmSinglePerson.get("EMAIL_ADDRESS");
                   
                personRecipientBean.setPersonId(personId);
                personRecipientBean.setPersonName(name);
                personRecipientBean.setEmailId(mailId);
                mailMessageInfoBean.addRecipient(personRecipientBean);
                }
            }
            setRecipients();
        }
    }
    
    
    
    /**
     * Used to send Mail
     */
    private void performSendMailAction() throws CoeusException, Exception {
        String message = objMailUI.txtMessage.getText();
        if(vecToRecipients == null || vecToRecipients.size() == 0) {
            CoeusOptionPane.showInfoDialog(messageResources.parseMessageKey(ERRKEY_NO_RECIPIENT));
            return;
        }else{
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            if(message==null || "".equals(message.trim())){
                if(CoeusOptionPane.showQuestionDialog(
                        messageResources.parseMessageKey(MSGKEY_NO_BODY_CONFIRM),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_NO)==CoeusOptionPane.SELECTION_NO){
                    return;
                }
            }
            //COEUSDEV-75:End
            sendMail();
            mailUIDlgWindow.setVisible(false);
        }
    }
    
    // JM 6-26-2014
    /**
     * Get user info
     */
    private String getUserInfo(String userId) {
    	String userName = "";
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_USER_NAME);

        requesterBean.setDataObject(userId);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.isSuccessfulResponse() ){
        	userName = (String) responderBean.getDataObject();
        } 
        else {
        	CoeusOptionPane.showErrorDialog("Could not contact server.");
        }
    	
    	return userName;
    	
    }
    // JM END
    
    /**
     * Used to send mail
     * @param MailAttributes
     */
    private void sendMail() {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(SEND_MAIL);
        Vector dataObjects = new Vector();
        mailMessageInfoBean.setSubject(objMailUI.txtSubject.getText());
    	// JM 6-25-2014 add FROM user to message
        String userName = getUserInfo(userId);
        mailMessageInfoBean.setMessage("Message from:  " + userName + "\n\n" + objMailUI.txtMessage.getText());
        // JM END
        dataObjects.add(moduleCode);
        dataObjects.add(actionId);
        dataObjects.add(moduleItemKey);
        dataObjects.add(moduleItemKeySequence);
        dataObjects.add(mailMessageInfoBean);
        requester.setDataObjects(dataObjects);
        AppletServletCommunicator communicator = new AppletServletCommunicator(mailConnect, requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null && responder.isSuccessfulResponse()) {
            setMailSent(((Boolean) responder.getDataObject()).booleanValue());
        }
    }
    
    /**
     * Used to delete a role/person/rolodex
     */
    private void performDeleteAction() {
        int selectedRow = objMailUI.lstTo.getSelectedIndex();
        if(selectedRow == -1) {
            CoeusOptionPane.showInfoDialog(messageResources.parseMessageKey(ERRKEY_SELECT_RECIPIENT));
        } else {
            int option = CoeusOptionPane.showQuestionDialog(messageResources.parseMessageKey(MSGKEY_DELETE_CONFIRM),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES) {
                deleteRow();
            }
        }
    }
    
    /**
     * Used to delete a row from list
     */
    private void deleteRow() {
        Object obj =  objMailUI.lstTo.getSelectedValue();
        if(obj instanceof PersonRecipientBean && mailMessageInfoBean.getPersonRecipientList()!=null){
            mailMessageInfoBean.getPersonRecipientList().remove(obj);
        }else if(obj instanceof RoleRecipientBean && mailMessageInfoBean.getRoleRecipientList()!=null){
            mailMessageInfoBean.getRoleRecipientList().remove(obj);
        }
        setRecipients();
    }
    
    /**
     * Used to set the recipients
     */
    private void setRecipients() {
        try {
            vecToRecipients   = (Vector)ObjectCloner.deepCopy(mailMessageInfoBean.getRoleRecipientList());
            Vector personRecipients = (Vector)ObjectCloner.deepCopy(mailMessageInfoBean.getPersonRecipientList());
            if(vecToRecipients==null){
                vecToRecipients = personRecipients;
            }else if(personRecipients!=null){
                vecToRecipients.addAll(personRecipients);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        vecToRecipients = (vecToRecipients == null)?new Vector():vecToRecipients;
        objMailUI.lstTo.setListData(vecToRecipients);
    }

    /**
     * Used to do mouse click operations
     * @param MouseEvent
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if(source.equals(objMailUI.lstTo)) {
            if(mouseEvent.getClickCount() == 2) {
                Vector vecData = new Vector();
                Object selObj =  objMailUI.lstTo.getSelectedValue();
                if(selObj instanceof RoleRecipientBean){
                    RoleRecipientBean roleBean = (RoleRecipientBean)selObj;
                    vecData = fetchRolePerson(roleBean.getRoleId(),roleBean.getRoleQualifier());
                    if(vecData != null && vecData.size() > 0) {
                    RoleListForm roleList = new RoleListForm(mdiForm, vecData);
                    roleList.display();
                } else {
                    CoeusOptionPane.showInfoDialog("No Persons exists for this role");
                }
                }
            }
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    
    class RecipientsListCellRenderer extends DefaultListCellRenderer {
      
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus){
            if( value != null){
                if(isSelected) {
                    setBackground(list.getSelectionBackground());
                } else {
                    setBackground(Color.WHITE);
                }
                setText( value.toString());
                if(value instanceof RoleRecipientBean){
                    setForeground(Color.RED);
                }else if(value instanceof PersonRecipientBean){
                    setForeground(Color.BLUE);
                }
            }
            setFont(CoeusFontFactory.getNormalFont());
            return this;
        }
    }
    
    public boolean isMailSent() {
        return mailSent;
    }
    
    public void setMailSent(boolean mailSent) {
        this.mailSent = mailSent;
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
            String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/personRoleServlet";
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
