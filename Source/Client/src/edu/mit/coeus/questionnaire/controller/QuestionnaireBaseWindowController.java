/*
 * @(#)ProtocolDetailsForm.java 1.0 10/17/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 17-SEP-2007
 * by Noorul
 */

/*
 * QuestionnaireBaseWindowController.java
 *
 * Created on September 19, 2006, 7:34 PM
 */

package edu.mit.coeus.questionnaire.controller;


import edu.mit.coeus.questionnaire.gui.QuestionnaireBaseWindow;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireMaintainaceBaseBean;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Hashtable;
import java.util.Map;
//import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireBaseWindowController extends QuestionnaireController
implements ActionListener,VetoableChangeListener{
    private QuestionnaireBaseWindow questionnaireBaseWindow;
    private QuestionnaireDetailController questionnaireDetailController;
    private CoeusAppletMDIForm mdiForm;
    private String title;
    private QueryEngine queryEngine;
    private String unitNumber;
    private ChangePassword changePassword;
//    private UserPreferencesForm userPreferencesForm;
    private CoeusMessageResources coeusMessageResources;
    private boolean isClosed;
    private static final String SAVE_CHANGES = "award_exceptionCode.1004";
    private boolean closed = false;
    // 4272: Maintain history of Questionnaires - Start
//    private QuestionnaireMaintainaceBaseBean baseBean;
    private QuestionnaireBaseBean baseBean;
    // 4272: Maintain history of Questionnaires - End
    private static final char GET_QUESTIONNAIRE_DATA = 'A';
    private static final char SAVE_QUESTIONNAIRE_DATA = 'D';
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private Hashtable questionnaireData;
    private int questionnaireId;
    private BaseWindowObservable observable = null;
    //Added for Questionnaire enhancement case#2946
    private int newQuestionnaireId;
    //Added for case 4287:Questionnaire Templates - Start
    private static final char GET_TEMPLATE = 'W';
    private byte[] templateFileBytes = null;
    //4287 end
    
    // 4272: Maintain history of Questionnaires - Start
    private static final String NEW_VERSION = "NEW_VERSION";
    private static final String CURRENT_VERSION_FINAL = "CURRENT_VERSION_FINAL";
    private static final String CURRENT_VERSION_NON_FINAL = "CURRENT_VERSION_NON_FINAL";
    private static final String CURRENT_VERSION_FINAL_NON_EDITABLE = "CURRENT_VERSION_FINAL_NON_EDITABLE";
    private String versionMode;
    private int versionNumber;
    // To hold the Version of the Questionnaire which is being copied.
    private int copyingQuestionnaireVersion;
    private static final char DELETE_QUESTIONNAIRE_VERSION = 'a';
    // 4272: Maintain history of Questionnaires  - End
    //Added for COEUSDEV-189 :  Can't delete the first question applied to a Questionnaire - Start
    private boolean isValidated = false;
    //COEUSDEV-189 : END
    /** Creates a new instance of QuestionnaireBaseWindowController */
    public QuestionnaireBaseWindowController() {
        
    }
    // 4272: Maintain history of Questionnaires  - Start
//    public QuestionnaireBaseWindowController(String title,CoeusAppletMDIForm mdiForm,String unitNumber, int questionnaireId) {
//        this.title = title;
//        this.mdiForm = mdiForm;
//        this.unitNumber = unitNumber;
//        this.questionnaireId = questionnaireId;
//        questionnaireBaseWindow = new QuestionnaireBaseWindow(title+EMPTY_STRING,mdiForm);
//        registerComponents();
//        queryEngine = QueryEngine.getInstance();
//        coeusMessageResources = CoeusMessageResources.getInstance();
//    }
    
    public QuestionnaireBaseWindowController(String title,CoeusAppletMDIForm mdiForm,String unitNumber, int questionnaireId, int versionNumber) {
        this.title = title;
        this.mdiForm = mdiForm;
        this.unitNumber = unitNumber;
        this.questionnaireId = questionnaireId;
        this.versionNumber = versionNumber;
        questionnaireBaseWindow = new QuestionnaireBaseWindow(title+EMPTY_STRING,mdiForm);
        registerComponents();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    // 4272: Maintain history of Questionnaires - End
    /** Initialize the Questionnaire Details window and corresponding detatils
     */
    public void initComponents() throws CoeusException{
        questionnaireDetailController = new QuestionnaireDetailController(getFunctionType(),baseBean);
        questionnaireDetailController.setQuereyKey(queryKey);
        // 4272: Maintain history of Questionnaires 
        questionnaireDetailController.setVersionMode(versionMode);
        questionnaireDetailController.setQuestionnaireVersionNumber(versionNumber);     
        questionnaireDetailController.setTemplateFileBytes(templateFileBytes);//4287
        questionnaireDetailController.setFormData(null);
        questionnaireDetailController.setDataForVersionMode();
        //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh 
//        questionnaireDetailController.prepapreFormData();
        questionnaireDetailController.prepapreFormData(false);
        //COEUSDEV-227 : End
        observable = new BaseWindowObservable();
    }
    
    /** Display the Questionnaire Details data. Get the data from the server and 
     *then intialize the Questionanire Details and populate the values into it
     */
    public void display() {
        try{
            // 4272: Maintain history of Questionnaires - Start
//            fetchDataFromServer();
            if(NEW_VERSION.equalsIgnoreCase(versionMode)){
                fetchDataFromServer(true);
            } else {
                fetchDataFromServer(false);
            }
            // 4272: Maintain history of Questionnaires - End
            initComponents();
            questionnaireBaseWindow.setMenus();
            enableDisableMenus();
            java.awt.Container container = questionnaireBaseWindow.getContentPane();
            container.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            container.add(questionnaireDetailController.getControlledUI());
            mdiForm.putFrame(title,questionnaireBaseWindow);
            mdiForm.getDeskTopPane().add(questionnaireBaseWindow);
            questionnaireBaseWindow.setVisible(true);
            questionnaireBaseWindow.setSelected(true);
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
            CoeusOptionPane.showErrorDialog(propertyVetoException.getMessage());
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    private void enableDisableMenus(){
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            questionnaireBaseWindow.saveMenuItem.setEnabled(false);
            questionnaireBaseWindow.btnSave.setEnabled(false);
            questionnaireBaseWindow.btnPrint.setEnabled(true);
            questionnaireBaseWindow.printMenuItem.setEnabled(true);
            // 4272: Maintain history of Questionnaires - Star
            questionnaireBaseWindow.btnDeleteVersion.setEnabled(false);
            questionnaireBaseWindow.deleteVersionMenuItem.setEnabled(false);
            // 4272: Maintain history of Questionnaires - End
        } else if(getFunctionType() == TypeConstants.ADD_MODE) {
            questionnaireBaseWindow.btnPrint.setEnabled(false);
            questionnaireBaseWindow.printMenuItem.setEnabled(false);
        }
         // 4272: Maintain history of Questionnaires - Start
        if(baseBean.isFinalFlag()){        
            questionnaireBaseWindow.btnDeleteVersion.setEnabled(false);
            questionnaireBaseWindow.deleteVersionMenuItem.setEnabled(false);
            // 4272: Maintain history of Questionnaires - End
        }
    }
    
    /** Communicate with the server and get the Questionnaire details data
     *Get the Questionnaire, QuestionnaireUsage and Questionanrie Questions data
     */
     // 4272: Maintain history of Questionnaires 
//    private void fetchDataFromServer() throws CoeusException{
    private void fetchDataFromServer(boolean latestQuestionsRequired) throws CoeusException{
        Vector dataObjects = new Vector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_QUESTIONNAIRE_DATA );
        dataObjects.add(0, new Integer(questionnaireId));
        dataObjects.add(1, unitNumber);
        // 4272: Maintain history of Questionnaires - Start
        if(getFunctionType() == TypeConstants.COPY_MODE){
            dataObjects.add(2, new Integer(copyingQuestionnaireVersion));
            // COEUSDEV-251: Copy questionnaire is not using latest version of questions - Start    
            // Get the latest version of the question if the questionnaire is created by copying another questionnaire.
            dataObjects.add(3, new Boolean(true));
        } else {
            dataObjects.add(2, new Integer(versionNumber));
            dataObjects.add(3, new Boolean(latestQuestionsRequired));
        }
//        dataObjects.add(3, new Boolean(latestQuestionsRequired));
        // COEUSDEV-251: Copy questionnaire is not using latest version of questions - End
        // 4272: Maintain history of Questionnaires - End
        requester.setDataObjects(dataObjects);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            questionnaireData  = (Hashtable)response.getDataObject();
            //Added for case 4287:Questionnaire Templates - Start
            if(getFunctionType() == TypeConstants.COPY_MODE){
                fetchTemplate(questionnaireId);
            }
            //4287 End
        }
        //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
        //For copy Questionnaire, if the functionType is Copy mode then replace with
        //new Questionnaire id.
        if(getFunctionType() == TypeConstants.COPY_MODE){
            questionnaireId = newQuestionnaireId;
        }
        //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        queryKey = ""+questionnaireId;
         // 4272: Maintain history of Questionnaires - Start
//        baseBean = new QuestionnaireMaintainaceBaseBean();
         baseBean = new QuestionnaireBaseBean();
         baseBean.setQuestionnaireVersionNumber(versionNumber);
         // 4272: Maintain history of Questionnaires - End
        baseBean.setUnitNumber(unitNumber);
        baseBean.setQuestionnaireNumber(questionnaireId);
         // 4272: Maintain history of Questionnaires - Start
        CoeusVector cvQnrData = (CoeusVector) questionnaireData.get(QuestionnaireBaseBean.class);
        if(cvQnrData != null && cvQnrData.size() > 0){
            QuestionnaireBaseBean questionnaireBaseBean = (QuestionnaireBaseBean) cvQnrData.get(0);
            baseBean.setFinalFlag(questionnaireBaseBean.isFinalFlag());
        }
         // 4272: Maintain history of Questionnaires - End
        queryEngine.addDataCollection(queryKey,questionnaireData);
    }
    
    public void formatFields() {
    }
    
    // returns the base window data
    public java.awt.Component getControlledUI() {
        return questionnaireBaseWindow;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /** Add the form listeners
     */
    public void registerComponents() {
        questionnaireBaseWindow.addVetoableChangeListener(this);
        questionnaireBaseWindow.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        questionnaireBaseWindow.saveMenuItem.addActionListener(this);
        questionnaireBaseWindow.btnSave.addActionListener(this);
        questionnaireBaseWindow.btnClose.addActionListener(this);
        questionnaireBaseWindow.closeMenuItem.addActionListener(this);
        questionnaireBaseWindow.inboxMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        questionnaireBaseWindow.delegationsMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        questionnaireBaseWindow.preferencesMenuItem.addActionListener(this);
        questionnaireBaseWindow.changePasswordMenuItem.addActionListener(this);
        questionnaireBaseWindow.exitMenuItem.addActionListener(this);
        questionnaireBaseWindow.currentLocksMenuItem.addActionListener(this);
        questionnaireBaseWindow.btnPrint.addActionListener(this);
        questionnaireBaseWindow.printMenuItem.addActionListener(this);
       
        // 4272: Maintain history of Questionnaires - Start
        questionnaireBaseWindow.deleteVersionMenuItem.addActionListener(this);
        questionnaireBaseWindow.btnDeleteVersion.addActionListener(this);
        // 4272: Maintain history of Questionnaires - End
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source =  actionEvent.getSource();
        try{
            blockEvents(true);
            if(source.equals(questionnaireBaseWindow.saveMenuItem) || source.equals(questionnaireBaseWindow.btnSave)){
                performSaveAction(true);
            }else if(source.equals(questionnaireBaseWindow.changePasswordMenuItem)){
                showChangePassword();
            }else if(source.equals(questionnaireBaseWindow.preferencesMenuItem)){
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(questionnaireBaseWindow.delegationsMenuItem)){
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(questionnaireBaseWindow.exitMenuItem)){
                exitApplication();
            }else if(source.equals(questionnaireBaseWindow.closeMenuItem) || source.equals(questionnaireBaseWindow.btnClose)){
                try{
                    close();
                }catch (PropertyVetoException propertyVetoException) {
                    //Don't do anything. this exception is thrown to stop window from closing.
                }
            }else if(source.equals(questionnaireBaseWindow.inboxMenuItem)){
                showInbox();
            }
            else if(source.equals(questionnaireBaseWindow.currentLocksMenuItem)){
                showLocksForm();
            }  
            //Case#2946 Coeus43 enhancement - start
            //Added for Questionnaire Printing enhancement
            else if(source.equals(questionnaireBaseWindow.btnPrint) || source.equals(questionnaireBaseWindow.printMenuItem)) {
                printQuestionnaire();
            }
            //Case#2946 Coeus43 enhancement - end
            // 4272: Maintain history of Questionnaires - Start
            else if(source.equals(questionnaireBaseWindow.btnDeleteVersion) || 
                    source.equals(questionnaireBaseWindow.deleteVersionMenuItem)) {
                int confirmation = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1026"), 
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                if(confirmation == CoeusOptionPane.SELECTION_YES){
                    boolean deleted = deleteCurrentVersionOfQuestionnaire();
                    if(deleted){
                        CoeusOptionPane.showInfoDialog("Version "+ baseBean.getQuestionnaireVersionNumber() +"" +
                                " of Questionnaire "+ baseBean.getQuestionnaireNumber()+ " is deleted.");
                        try {
                            close();    
                            
                            baseBean.setQuestionnaireVersionNumber(baseBean.getQuestionnaireVersionNumber() -1);
                            observable.notifyObservers( baseBean );
                        } catch (PropertyVetoException ex) {
                            ex.printStackTrace();
                        }
                        
                    }
                }
            }
            // 4272: Maintain history of Questionnaires - End
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (CoeusUIException coeusUIException){
            coeusUIException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
        }finally{
            blockEvents(false);
        }
    }
    /** close the internal frame based on the operation did. If the data changes
     *then ask for the save confrmation and then close the window
     */
    private void close() throws PropertyVetoException{
        if(questionnaireDetailController.isModified() || questionnaireDetailController.isQuestionnaireModified()) {
            int selection = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                    SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                try{
                    performSaveAction(true);
                    //Modified for COEUSDEV-189 :  Can't delete the first question applied to a Questionnaire - Start
//                    if(isClosed){
                    if(isClosed || !isValidated){//COEUSDEV-189 : END
                        throw new PropertyVetoException("Cancel",null);
                    }
                }catch (CoeusUIException coeusUIException) {
                    //Validation Failed
                    throw new PropertyVetoException(EMPTY_STRING, null);
                }catch (CoeusException coeusException){
                    //Validation Failed
                    throw new PropertyVetoException(EMPTY_STRING, null);
                }
            }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                throw new PropertyVetoException(EMPTY_STRING, null);
            }else if(selection == CoeusOptionPane.SELECTION_NO) {
                isClosed = false;
            }
            
        }
        mdiForm.removeFrame(title);
        closed = true;
        questionnaireBaseWindow.doDefaultCloseAction();
        isClosed = false;
    }

    /** Save the questionnaire details data. Check for the save confirmation,
     *then check for the validation and then go for form saving
     */
    private void performSaveAction(boolean value) throws CoeusException,CoeusUIException{
        if(value){
            if(questionnaireDetailController.isModified() || questionnaireDetailController.isQuestionnaireModified()){
                if(questionnaireDetailController.isValidationSuccessfull()){
                    Map data = questionnaireDetailController.saveQuestionnaireData();
                    boolean isSaved = saveToServer(data);
                    if(isSaved){
                        queryEngine.removeDataCollection(queryKey);
                        observable.setFunctionType( getFunctionType());
                        setFunctionType(TypeConstants.MODIFY_MODE);
                        // 4272: Maintain history of Questionnaires - Start
//                        fetchDataFromServer();
                        fetchDataFromServer(false);
                        // 4272: Maintain history of Questionnaires - End
                        questionnaireDetailController.setFunctionType(getFunctionType());
                        questionnaireDetailController.setQuereyKey(queryKey);
                        questionnaireDetailController.setFormData(null);
                        //Commented for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
                        //Question tree is not build during save
//                        questionnaireDetailController.prepapreFormData();
                        questionnaireDetailController.prepapreFormData(true);
                        //COEUSDEV-27 : End
                        questionnaireDetailController.setModified(false);
                        questionnaireDetailController.setTemplateAcType(EMPTY_STRING);//4287
                        questionnaireBaseWindow.btnPrint.setEnabled(true);
                        questionnaireBaseWindow.printMenuItem.setEnabled(true);
                        
                        
                        CoeusVector cvQuestionnaireData = queryEngine.executeQuery(queryKey , 
                            QuestionnaireBaseBean.class ,CoeusVector.FILTER_ACTIVE_BEANS);
                        if(cvQuestionnaireData != null && cvQuestionnaireData.size() > 0){
                            QuestionnaireBaseBean baseBean = (QuestionnaireBaseBean)cvQuestionnaireData.get(0);
                            observable.notifyObservers( baseBean );
                        }
                    }
                    //Modified for COEUSDEV-189 :  Can't delete the first question applied to a Questionnaire - Start
                    isValidated = true;
                   
                }else{
                    isValidated = false;
                }
            }else{
                isValidated = false;
            } //COEUSDEV-189 : END
        }
    }
    
    /** Communicate with the server to update the Questionnaire Details
     *data
     */
    private boolean saveToServer(Map data) throws CoeusException{
        boolean isDataSaved = false;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( SAVE_QUESTIONNAIRE_DATA );
        requester.setDataObject(data);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            isDataSaved  = ((Boolean)response.getDataObject()).booleanValue();
        }
        return isDataSaved;
    }
    
    /** call Change password screen for changing the password of the
     *logged in user
     */
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Display Delegations window
     */
    private void displayUserDelegation() {
        UserDelegationForm userDelegationForm = new UserDelegationForm(mdiForm,true);        
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End  
    
    /** Call user preference window for the application
     */
    private void showPreference(){
        UserPreferencesForm userPreferencesForm = null;
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
    
    /**
     * Method used to close the application after confirmation.
     */
    public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
        "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
    }
    /** Show the inbox details for the logged in user
     */
    private void showInbox(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame("Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    /** Show the Lock form for the logged in user
     */
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    
    /** window clsoe listener will be fired when clsoing X window
     */
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
     public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
   
    
//    public void update(Observable o, Object arg) {
//        if ( arg != null ){
//            if( arg instanceof QuestionnaireBaseBean ) {
//                QuestionnaireBaseBean questionnaireBaseBean = ( QuestionnaireBaseBean )arg;
//                observable.setFunctionType( getFunctionType() );
//                observable.notifyObservers( questionnaireBaseBean );
//            }
//            
//        }
//    }

    /**
     * Getter for property newQuestionnaireId.
     * @return Value of property newQuestionnaireId.
     */
    public int getNewQuestionnaireId() {
        return newQuestionnaireId;
    }

    /**
     * Setter for property newQuestionnaireId.
     * @param newQuestionnaireId New value of property newQuestionnaireId.
     */
    public void setNewQuestionnaireId(int newQuestionnaireId) {
        this.newQuestionnaireId = newQuestionnaireId;
    }
    
    /**
     * Is used to invoke the printing functionality
     * @throws CoeusException
     */
    private void printQuestionnaire() throws CoeusException {
        QuestionnaireDetailController detailController = new QuestionnaireDetailController(getFunctionType(),baseBean);
        detailController.printQuestionnaire(false, false, false);
    }
    
    //Added for case 4287:Questionnaire Templates - Start
    private void fetchTemplate(int questionnaireId)  throws CoeusException{
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_TEMPLATE );
        // 4272: Maintain history of Questionnaires  - Start
//        requester.setDataObject(new Integer(questionnaireId));
        CoeusVector cvDataObjects = new CoeusVector();
        cvDataObjects.add(0, new Integer(questionnaireId));
        cvDataObjects.add(1, new Integer(versionNumber));
        requester.setDataObjects(cvDataObjects);
        // 4272: Maintain history of Questionnaires  - End
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.hasResponse() ){
            this.templateFileBytes = (byte[])response.getDataObject();
        }
    }
    //4287 : End
    // 4272: Maintain history of Questionnaires - Start
    public String getVersionMode() {
        return versionMode;
    }

    public void setVersionMode(String versionMode) {
        this.versionMode = versionMode;
    }
    
    private boolean deleteCurrentVersionOfQuestionnaire() throws CoeusException {
        boolean deleted;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( DELETE_QUESTIONNAIRE_VERSION );
        requester.setDataObject(baseBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.hasResponse() ){
           deleted  = ((Boolean) response.getDataObject()).booleanValue();
        } else {
            throw  new CoeusException(response.getMessage());
        }
        
        return deleted;
    }
    
    public QuestionnaireBaseWindow getQuestionnaireBaseWindow() {
        return questionnaireBaseWindow;
    }

    public void setCopyingQuestionnaireVersion(int copyingQuestionnaireVersion) {
        this.copyingQuestionnaireVersion = copyingQuestionnaireVersion;
    }
    // 4272: Maintain history of Questionnaires - End
}
