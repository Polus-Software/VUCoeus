/*
 * @(#)AreaOfResearchBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/*
 * QuestionnaireListController.java
 *
 * Created on September 20, 2006, 11:52 AM
 */

/* PMD check performed, and commented unused imports and variables on 17-SEP-2007
 * by Noorul
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.utils.saveas.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBaseBean;
import edu.mit.coeus.questionnaire.gui.QuestionnaireBaseWindow;
import edu.mit.coeus.questionnaire.gui.QuestionnaireListWindow;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.search.bean.DisplayBean;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.BaseWindowObservable;
//import edu.mit.coeus.utils.query.*;
//import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.saveas.SaveAsDialog;

import java.beans.*;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Observer;
import javax.swing.table.*;

//import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.user.gui.UserPreferencesForm;
/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireListController extends QuestionnaireController implements 
    MouseListener, ActionListener, VetoableChangeListener, Observer, ItemListener{
    
    private QuestionnaireListWindow questionnaireListWindow;
    
    /** Coeus Serach instance to search Awards. */
    private boolean closed = false;
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private QuestionnaireBaseWindowController questionnaireBaseWindowController;
    private int selectedRow;
    private int baseTableRow;
    private ChangePassword changePassword;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    //holds column status
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final char GET_NEW_QUESTIONNAIRE_ID = 'G';
    private static final char GET_QUESTIONNAIRE_DATAS = 'H'; 
    private QuestionnaireTableModel questionnaireTableModel;
    private CoeusVector cvQuestionsData;
    private static final String SELECT_MESSAGE = "questionnaire_exceptionCode.1006";
    //Case#2908 - Exports from Search Results Do Not Preserve Data Format - Start
    private static final int QUESTIONNAIRE_ID = 0;
    //Case#2908 - End
    
    // 4272: Maintain history of Questionnaire - Start
    private static final String NEW_VERSION = "NEW_VERSION";
    private static final String CURRENT_VERSION_FINAL = "CURRENT_VERSION_FINAL";
    private static final String CURRENT_VERSION_NON_FINAL = "CURRENT_VERSION_NON_FINAL";
    private static final String CURRENT_VERSION_FINAL_NON_EDITABLE = "CURRENT_VERSION_FINAL_NON_EDITABLE";

    private static final char CHECK_QUESTIONNAIRE_ANSWERED = 'Y';
    // 4272: Maintain history of Questionnaire - End
    
    // 4597: Questionaire maintenance is not checking for rights - Start
    private static final String MAINTAIN_QUESTIONNAIRE = "MAINTAIN_QUESTIONNAIRE";
    private static final String FN_USER_HAS_OSP_RIGHT = "FN_USER_HAS_OSP_RIGHT";
    private static final String FUNCTION_SERVLET = "/coeusFunctionsServlet";
    // 4597: Questionaire maintenance is not checking for rights - End
    private CoeusVector cvQuestionnaireGroup;
    /** Creates a new instance of QuestionnaireListController */
    public QuestionnaireListController() {
        initComponents();
        registerComponents();
        // 4597: Questionaire maintenance is not checking for rights
        enableDisableButtons();
    }
    
    /** Initialize all the components with the tab name and necessary data
     */
    private void initComponents() {
        try{
            questionnaireListWindow = new
            QuestionnaireListWindow("Questionnaire List", mdiForm);
            questionnaireTableModel =new QuestionnaireTableModel();
            questionnaireListWindow.initComponents(new JTable());
            questionnaireListWindow.tblResults.setModel(questionnaireTableModel);
             // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
//            cvQuestionsData = getQuestionnaireDatas();
            HashMap hmQuestionnaireData = getQuestionnaireDatas(false);
            cvQuestionsData = (CoeusVector)hmQuestionnaireData.get("QUESTIONNAIRE_DATA");
            cvQuestionnaireGroup = (CoeusVector)hmQuestionnaireData.get("QUESTIONNAIRE_GROUP_TYPES");
            if(cvQuestionnaireGroup != null && !cvQuestionnaireGroup.isEmpty()){
                for(Object questionGroup : cvQuestionnaireGroup){
                    ComboBoxBean groupComboBoxBean = (ComboBoxBean)questionGroup;
                    questionnaireListWindow.cmbGroup.addItem(groupComboBoxBean);
                }
            }
            questionnaireListWindow.cmbGroup.addItemListener(this);
             // Modified for COEUSQA-3287 Questionnaire Maintenance Features - End
            questionnaireTableModel.setData(cvQuestionsData);
            setTableEditors();
        }catch (CoeusException exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            exception.printStackTrace();
        }
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
            if(cvQuestionsData != null && !cvQuestionsData.isEmpty()){
                ComboBoxBean selectedGroup = (ComboBoxBean)questionnaireListWindow.cmbGroup.getSelectedItem();
                int groupTypeCode = 0;
                String groupTypeDesc = null;
                groupTypeDesc = selectedGroup.getDescription();
                if(!CoeusGuiConstants.EMPTY_STRING.equals(selectedGroup.getCode())){
                    groupTypeCode = Integer.parseInt(selectedGroup.getCode());
                }
                //Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected  - start
                //if groupTypeCode is '0' then it should diplay the all questionnaire list
                //if it not '0' then it should display only selected group questionarries.
                if(groupTypeCode != 0){
                    Equals groupEquals = new Equals("groupTypeCode",groupTypeCode);
                    CoeusVector cvFilteredGroup = cvQuestionsData.filter(groupEquals);
                    questionnaireTableModel.setData(cvFilteredGroup);
                    if(cvFilteredGroup != null && !cvFilteredGroup.isEmpty()){
                        questionnaireListWindow.tblResults.setRowSelectionInterval(0,0);
                    }
                }else if(groupTypeDesc == "ALL" || groupTypeCode == 0){
                    questionnaireTableModel.setData(cvQuestionsData);
                }
                //Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected  - end
                
            }
            
        }
    }
     /** Displays Questionnaire List. */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.QUESTIONNAIRE_FRAME_TITLE, questionnaireListWindow);
            mdiForm.getDeskTopPane().add(questionnaireListWindow);
            questionnaireListWindow.setSelected(true);
            questionnaireListWindow.setVisible(true);
            showQuestionnaireSearch();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
      /** displays Award Search Window. */
    private void showQuestionnaireSearch() {
        questionnaireListWindow.tblResults.addMouseListener(this);
        questionnaireListWindow.tblResults.getTableHeader().addMouseListener(this);
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return questionnaireListWindow;
    }
    
    public Object getFormData() {
        return questionnaireListWindow;
    }
    
    public void registerComponents() {
        questionnaireListWindow.addVetoableChangeListener(this);
        questionnaireListWindow.btnClose.addActionListener(this);
        questionnaireListWindow.btnModifyQuestiionnaire.addActionListener(this);
        questionnaireListWindow.btnDisplayQuestionnaire.addActionListener(this);
        questionnaireListWindow.btnNewQuestiionnaire.addActionListener(this);
        questionnaireListWindow.btnSaveas.addActionListener(this);
        questionnaireListWindow.btnSearchQuestionnaire.addActionListener(this);
        questionnaireListWindow.btnSortQuestionnaire.addActionListener(this);
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        questionnaireListWindow.btnCopyQuestionnaire.addActionListener(this);
        questionnaireListWindow.mnuItmCopyQuestiionnaire.addActionListener(this);
        //Setting the listener for the menu items
        
        questionnaireListWindow.mnuItmChangePassword.addActionListener(this);
        questionnaireListWindow.mnuItmClose.addActionListener(this);
        
        questionnaireListWindow.mnuItmNewQuestiionnaire.addActionListener(this);
        questionnaireListWindow.mnuItmModifyQuestiionnaire.addActionListener(this);
        questionnaireListWindow.mnuItmDisplayQuestionnaire.addActionListener(this);
        questionnaireListWindow.mnuItmExit.addActionListener(this);
        questionnaireListWindow.mnuItmInbox.addActionListener(this);
        questionnaireListWindow.mnuItmSaveas.addActionListener(this);
        questionnaireListWindow.mnuItmCurrentLocks.addActionListener(this);
        questionnaireListWindow.tblResults.addMouseListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        questionnaireListWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        questionnaireListWindow.mnuItmPreferences.addActionListener(this);
        
        questionnaireListWindow.tblResults.getTableHeader().addMouseListener(this);
    }
    /** Set the form table editor
     */
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = questionnaireListWindow.tblResults.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.addMouseListener(new ColumnHeaderListener());
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            
            questionnaireListWindow.tblResults.setRowHeight(22);
            questionnaireListWindow.tblResults.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            questionnaireListWindow.tblResults.setShowHorizontalLines(true);
            questionnaireListWindow.tblResults.setShowVerticalLines(true);
            questionnaireListWindow.tblResults.setOpaque(false);
            questionnaireListWindow.tblResults.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            questionnaireListWindow.tblResults.setRowSelectionAllowed(true);
            
            TableColumn columnDetails=questionnaireListWindow.tblResults.getColumnModel().getColumn(0);
            columnDetails.setPreferredWidth(110);
            
            columnDetails=questionnaireListWindow.tblResults.getColumnModel().getColumn(1);
            columnDetails.setPreferredWidth(300);
            //Code modified and added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
            //To display the final flag value
            columnDetails=questionnaireListWindow.tblResults.getColumnModel().getColumn(2);
            columnDetails.setPreferredWidth(860);
            
            columnDetails=questionnaireListWindow.tblResults.getColumnModel().getColumn(3);
            columnDetails.setPreferredWidth(60);
            //Code modified and added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
            
            // 4272: Maintain history of Questionnaire
            columnDetails=questionnaireListWindow.tblResults.getColumnModel().getColumn(4);
            
            columnDetails.setMaxWidth(0);
            columnDetails.setMinWidth(0);
            columnDetails.setPreferredWidth(0);
            // 4272: Maintain history of Questionnaire - End
        } catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public void actionPerformed(ActionEvent actionEvent){
        try{
            Object source = actionEvent.getSource();
            if(source.equals(questionnaireListWindow.mnuItmModifyQuestiionnaire) ||
            source.equals(questionnaireListWindow.btnModifyQuestiionnaire)){
                blockEvents(true);
                modifyQuestionnaire();
                blockEvents(false);
            }else if(source.equals(questionnaireListWindow.mnuItmDisplayQuestionnaire) ||
            source.equals(questionnaireListWindow.btnDisplayQuestionnaire)){
                blockEvents(true);
                displayQuestionnaire();
                blockEvents(false);
            }else if(source.equals(questionnaireListWindow.btnSearchQuestionnaire)
            || source.equals(questionnaireListWindow.mnuItmSearch)){
                showQuestionnaireSearch();
            }else if(source.equals(questionnaireListWindow.btnClose) ||
            source.equals(questionnaireListWindow.mnuItmClose)){
                close();
            }else if(source.equals(questionnaireListWindow.btnNewQuestiionnaire) ||
            source.equals(questionnaireListWindow.mnuItmNewQuestiionnaire)){
                blockEvents(true);
                newQuestionnaire();
                blockEvents(false);
            }else if(source.equals(questionnaireListWindow.btnSaveas)||
            source.equals(questionnaireListWindow.mnuItmSaveas)){
                saveQuestionnaireList();
            }else if(source.equals(questionnaireListWindow.mnuItmInbox)){
                blockEvents(true);
                showInboxDetails();
                blockEvents(false);
            }else if(source.equals(questionnaireListWindow.mnuItmExit)){
                exitApplication();
            }else if(source.equals(questionnaireListWindow.mnuItmChangePassword)){
                showChangePassword();
            }else if(source.equals(questionnaireListWindow.mnuItmPreferences)){
                showPreference();
            }
            //Added for Case#3682 - Enhancements related to Delegations - Start
            else if(source.equals(questionnaireListWindow.mnuItmDelegations)){
                displayUserDelegation();
            }
            //Added for Case#3682 - Enhancements related to Delegations - End
            else if(source.equals(questionnaireListWindow.mnuItmCurrentLocks)){
                showLocksForm();
            }
            //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
            //For Copy questionnaire functionality
            else if(source.equals(questionnaireListWindow.mnuItmCopyQuestiionnaire) ||
            source.equals(questionnaireListWindow.btnCopyQuestionnaire)){
                blockEvents(true);
                copyQuestionnaire();
                blockEvents(false);
            }
            //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (PropertyVetoException ex){
            
        }
    }
    /** Open the questionnaire window in Modify mode
     */
    private void modifyQuestionnaire() throws CoeusClientException,CoeusException, PropertyVetoException{
        int selRow = questionnaireListWindow.tblResults.getSelectedRow();
        if(selRow == -1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_MESSAGE));
            return;
        }        
        Integer questionnaireId = (Integer)questionnaireListWindow.tblResults.getValueAt(selRow, 0);
        int id = questionnaireId.intValue();
//        if(isWindowOpen(EMPTY_STRING+id, TypeConstants.MODIFY_MODE)) {
//          return ;
//        }
        // 4272: Maintain history of Questionnaire
        Integer versionNumber = (Integer)questionnaireListWindow.tblResults.getValueAt(selRow, 4);
        int qnrVersionNumber = versionNumber.intValue();
        String title = CoeusGuiConstants.QUESTIONNAIRE_TITLE;
        QuestionnaireBaseWindow listWindow = null;
        if( ( listWindow = (QuestionnaireBaseWindow)mdiForm.getFrame(
         title))!= null ){
             if( listWindow.isIcon() ){
                 listWindow.setIcon(false);
             }
             listWindow.setSelected( true );
             return;
         }
        
        // 4272: Maintain history of Questionnaire - Start
        String strFinal = (String) questionnaireListWindow.tblResults.getValueAt(selRow, 3);
        String versionMode; 
        if("Yes".equalsIgnoreCase(strFinal)){
            boolean isQuestionnaireAnswered = checkIsQuestionnaireAnswered(questionnaireId);
            if(isQuestionnaireAnswered){
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1024"), CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES){
                    versionMode = NEW_VERSION;
                    qnrVersionNumber += 1;
                    title = title + " "+  questionnaireId + " Version " + qnrVersionNumber;
                }else{
                    title = title + " "+  questionnaireId + " Version " + qnrVersionNumber;
                    versionMode = CURRENT_VERSION_FINAL_NON_EDITABLE;
                }
            }else{
                versionMode = CURRENT_VERSION_FINAL;
                title = title +" "+  questionnaireId + " Version " + qnrVersionNumber;
            }
                
        }else{
            versionMode = CURRENT_VERSION_NON_FINAL;
            title = title + " "+ questionnaireId + " Version " + qnrVersionNumber;
        }
        //
//        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(CoeusGuiConstants.QUESTIONNAIRE_TITLE,mdiForm,mdiForm.getUnitNumber(),id);
        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(title,
                mdiForm,mdiForm.getUnitNumber(),id, qnrVersionNumber);
        // 4272: Maintain history of Questionnaire - End
        setFunctionType(TypeConstants.MODIFY_MODE);
        questionnaireBaseWindowController.setFunctionType(getFunctionType());
        // 4272: Maintain history of Questionnaire
        questionnaireBaseWindowController.setVersionMode(versionMode);
        questionnaireBaseWindowController.display();
        questionnaireBaseWindowController.registerObserver(this);
    }
    /** Open the questionnaire window in Display mode
     */
    private void displayQuestionnaire() throws CoeusClientException,CoeusException, PropertyVetoException{
        int selRow = questionnaireListWindow.tblResults.getSelectedRow();
        if(selRow == -1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_MESSAGE));
            return;
        }
        Integer questionnaireId = (Integer)questionnaireListWindow.tblResults.getValueAt(selRow, 0);
        int id = questionnaireId.intValue();
//        if(isWindowOpen(EMPTY_STRING+id, TypeConstants.DISPLAY_MODE)) {
//          return ;
//       }
        // 4272: Maintain history of Questionnaire
        Integer versionNumber = (Integer)questionnaireListWindow.tblResults.getValueAt(selRow, 4);
        // 4272: Maintain history of Questionnaire - Start
//         String title = CoeusGuiConstants.QUESTIONNAIRE_TITLE;
         String  title = CoeusGuiConstants.QUESTIONNAIRE_TITLE +" "+ questionnaireId + " Version " + versionNumber.intValue();
         // 4272: Maintain history of Questionnaire - End
        QuestionnaireBaseWindow listWindow = null;
        if( ( listWindow = (QuestionnaireBaseWindow)mdiForm.getFrame(
         title))!= null ){
             if( listWindow.isIcon() ){
                 listWindow.setIcon(false);
            }
             listWindow.setSelected( true );
             return;
         }
        // 4272: Maintain history of Questionnaire - Start
//        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(CoeusGuiConstants.QUESTIONNAIRE_TITLE,mdiForm,mdiForm.getUnitNumber(),id);
        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(title,
                mdiForm,mdiForm.getUnitNumber(),id, versionNumber.intValue());
        // 4272: Maintain history of Questionnaire - End
        setFunctionType(TypeConstants.DISPLAY_MODE);
        questionnaireBaseWindowController.setFunctionType(getFunctionType());
        questionnaireBaseWindowController.display();
    }
    
        
   
          /** closes this window. */
    private void close() {
        
        mdiForm.removeFrame("Questionnaire List");
        closed = true;
        questionnaireListWindow.doDefaultCloseAction();
        

    }
    /** Open the questionnaire window in the new mode
     */
    private void newQuestionnaire()throws CoeusClientException,CoeusException, PropertyVetoException{
        int questionnaireId = getNewQuestionnaireId();
//        if(isWindowOpen(EMPTY_STRING+questionnaireId, TypeConstants.ADD_MODE)) {
//          return ;
//        }
        // // 4272: Maintain history of Questionnaire - Start
//         String title = CoeusGuiConstants.QUESTIONNAIRE_TITLE;
         String title = "New Questionnaire "+questionnaireId+" Version 1";
         // 4272: Maintain history of Questionnaire = End
        QuestionnaireBaseWindow listWindow = null;
        if( ( listWindow = (QuestionnaireBaseWindow)mdiForm.getFrame(
         title))!= null ){
             if( listWindow.isIcon() ){
                 listWindow.setIcon(false);
             }
             listWindow.setSelected( true );
             return;
         }
        //
//        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(CoeusGuiConstants.QUESTIONNAIRE_TITLE,mdiForm,mdiForm.getUnitNumber(),questionnaireId);
        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(title,
                mdiForm, mdiForm.getUnitNumber(), questionnaireId, 1);
        setFunctionType(TypeConstants.ADD_MODE);
        questionnaireBaseWindowController.setFunctionType(getFunctionType());
        questionnaireBaseWindowController.display();
        questionnaireBaseWindowController.registerObserver(this);
    }
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * For copy Questionnaire functionality.
     * @throws edu.mit.coeus.exception.CoeusClientException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws java.beans.PropertyVetoException 
     */
    private void copyQuestionnaire()throws CoeusClientException,CoeusException, PropertyVetoException{
        int selRow = questionnaireListWindow.tblResults.getSelectedRow();
        if(selRow == -1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_MESSAGE));
            return;
        }
        Integer questionnaireId = (Integer)questionnaireListWindow.tblResults.getValueAt(selRow, 0);
        int id = questionnaireId.intValue();        
        
        String title = CoeusGuiConstants.QUESTIONNAIRE_TITLE;
        QuestionnaireBaseWindow listWindow = null;
        if( ( listWindow = (QuestionnaireBaseWindow)mdiForm.getFrame(
         title))!= null ){
             if( listWindow.isIcon() ){
                 listWindow.setIcon(false);
             }
             listWindow.setSelected( true );
             return;
         }
        // 4272: Maintain History of Questionnaire - Start
//        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(CoeusGuiConstants.QUESTIONNAIRE_TITLE,mdiForm,mdiForm.getUnitNumber(),id);
        Integer qnrVersion = (Integer)questionnaireListWindow.tblResults.getValueAt(selRow, 4);
        questionnaireBaseWindowController = new QuestionnaireBaseWindowController(CoeusGuiConstants.QUESTIONNAIRE_TITLE,
                mdiForm, mdiForm.getUnitNumber(), id, 1);
        questionnaireBaseWindowController.setCopyingQuestionnaireVersion(qnrVersion.intValue());
        // 4272: Maintain History of Questionnaire - End
        setFunctionType(TypeConstants.COPY_MODE);
        questionnaireBaseWindowController.setFunctionType(getFunctionType());
        questionnaireBaseWindowController.setNewQuestionnaireId(getNewQuestionnaireId());
        questionnaireBaseWindowController.display();
        questionnaireBaseWindowController.registerObserver(this);
    }
    
    /** Get the new questionnaire Id which is auto generated.It will
     *be invoked when create new questionnaire action is triggered
     */
    private int getNewQuestionnaireId() throws CoeusException{
         int questionnaireId = -1;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_NEW_QUESTIONNAIRE_ID );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            questionnaireId  = ((Integer)response.getDataObject()).intValue();
        }
        return questionnaireId;
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End 
    
    /** Show the User Preference form
     */
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
    
    
    /** Show the change password window
     */
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }
    
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
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
    
    /** Display the inbox details
     */
    private void showInboxDetails(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
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
    
     
    
     /** To save the proposal log list
     */
    private void saveQuestionnaireList(){
        selectedRow = questionnaireListWindow.tblResults.getSelectedRow();
        //Code modified for PT ID#2382 - Save As functionality
        if( selectedRow == -1 ){
            //return;
        }
        //Case#2908 - Exports from Search Results Do Not Preserve Data Format - Start
        initDisplayBeanToColumn(questionnaireListWindow.tblResults);
        //Case#2908 - End
        SaveAsDialog saveAsDialog = new SaveAsDialog(questionnaireListWindow.tblResults);
    }
    /**
     * Method to set display bean to table column
     */
    private void initDisplayBeanToColumn(JTable tblInbox){
        DisplayBean displayBean = new DisplayBean("Questionnaire ID","","","","number","");
        tblInbox.getColumnModel().getColumn(QUESTIONNAIRE_ID).setIdentifier(displayBean);
    }
    
    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /** Get the questionnaire related data. Get questionnaire, QuestionnaireUsage 
     *and QuestionnaireQuestions data
     */
//    private CoeusVector getQuestionnaireDatas() throws CoeusException {
//        RequesterBean requester = new RequesterBean();
//        CoeusVector cvQuestionnaire = null ;
//        requester.setFunctionType( GET_QUESTIONNAIRE_DATAS );
//        AppletServletCommunicator comm = new AppletServletCommunicator(
//                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
//        comm.send();
//        ResponderBean response = comm.getResponse();
//        if ( !response.isSuccessfulResponse() ){
//            throw new CoeusException(response.getMessage());
//        }else{
//            cvQuestionnaire  = (CoeusVector)response.getDataObject();
//        }
//        return cvQuestionnaire;
//    }
    
    private HashMap getQuestionnaireDatas(boolean isDuringUpdate) throws CoeusException {
        RequesterBean requester = new RequesterBean();
        HashMap hmQuestionnaireData = null ;
        requester.setFunctionType( GET_QUESTIONNAIRE_DATAS );
        requester.setDataObject(new Boolean(isDuringUpdate));
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            hmQuestionnaireData = (HashMap)response.getDataObject();
        }
        return hmQuestionnaireData;
    }
    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - End
    
    public class QuestionnaireTableModel extends AbstractTableModel{
        //Code modified for coeus4.3 Questionnaire Maintenance enhancement case#2946
//        private String colNames[]={"Questionnaire Id","Name", "Description"};
//        private Class colClass[]={Integer.class,String.class,String.class};
        // // 4272: Maintain history of Questionnaire - Start
//        private String colNames[]={"Questionnaire Id","Name", "Description", "Final"};
//        private Class colClass[]={Integer.class,String.class,String.class,String.class};
        private String colNames[]={"Questionnaire Id","Name", "Description", "Final", "Version"};
        private Class colClass[]={Integer.class,String.class,String.class,String.class, Integer.class};
        private CoeusVector cvQuestionnaireForList;
        // 4272: Maintain history of Questionnaire - End
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public int getRowCount() {
            if(cvQuestionnaireForList==null){
                return 0;
            }else{
                return cvQuestionnaireForList.size();
            }
        }
        
        public void setData(CoeusVector cvQuestionsData){
            cvQuestionnaireForList = cvQuestionsData ;
            fireTableDataChanged();
        }
        
        public Object getValueAt(int row, int col) {
            QuestionnaireBaseBean bean = (QuestionnaireBaseBean)cvQuestionnaireForList.get(row);
            switch(col){
                case 0:
                    return new Integer(bean.getQuestionnaireId());
                case 1:
                    return bean.getName();
                case 2:
                    return bean.getDescription();
                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                //To Display the final flag value.
                case 3:
                    return bean.isFinalFlag()?"Yes" : "No";
                // 4272: Maintain history of Questionnaire - Start
                case 4:
                    return new Integer(bean.getQuestionnaireVersionNumber());    
                // 4272: Maintain history of Questionnaire - End
            }
            return EMPTY_STRING;
        }
    }    
    /** Open the window in the display mode
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        try{
        if(mouseEvent.getSource() instanceof JTable) {
            int clickCount = mouseEvent.getClickCount();
            if(clickCount != 2) return ;
            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            displayQuestionnaire();
            mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            
        }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (CoeusClientException coeusUIException){
            coeusUIException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
        }catch (PropertyVetoException ex){
            
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
    /** This will be notified when BaseWindowController fires the notify action/
     */
    public void update(java.util.Observable o, Object arg) {
        if( arg instanceof QuestionnaireBaseBean) {
            //Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected - start
            ComboBoxBean selectedGroup = (ComboBoxBean)questionnaireListWindow.cmbGroup.getSelectedItem();
            int groupTypeCode = 0;
            //Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected - end
            QuestionnaireBaseBean questionnaireBean =
            (QuestionnaireBaseBean)arg;
            //Code modified for coeus4.3 Questionnaire Maintenance enhancement case#2946
//            if( ((BaseWindowObservable)o).getFunctionType() == TypeConstants.ADD_MODE) {
            if( ((BaseWindowObservable)o).getFunctionType() == TypeConstants.ADD_MODE
                    || ((BaseWindowObservable)o).getFunctionType() == TypeConstants.COPY_MODE) {
                int lastRow = questionnaireListWindow.tblResults.getRowCount();
                cvQuestionsData.addElement(questionnaireBean);
                questionnaireTableModel.setData(cvQuestionsData);
                baseTableRow = lastRow;
                questionnaireListWindow.tblResults.setRowSelectionInterval(baseTableRow,baseTableRow);
            }else{
                // 4272: Maintain history of Questionnaire - Start
//                int selRow = questionnaireListWindow.tblResults.getSelectedRow();
//                if(  selRow != -1 ) {
//                    baseTableRow = selRow;
//                }
//                cvQuestionsData.remove(baseTableRow);
//                // 4272: Maintain history of Questionnaire 
//                if(questionnaireBean.getQuestionnaireVersionNumber() > 0){
//                    cvQuestionsData.add(baseTableRow,questionnaireBean);
//                    questionnaireTableModel.fireTableRowsUpdated(baseTableRow,baseTableRow);
//                }
                if(questionnaireBaseWindowController != null && questionnaireBaseWindowController.getQuestionnaireBaseWindow() != null){
                    if(questionnaireBean.isFinalFlag()){
                        questionnaireBaseWindowController.getQuestionnaireBaseWindow().btnDeleteVersion.setEnabled(false);
                        questionnaireBaseWindowController.getQuestionnaireBaseWindow().deleteVersionMenuItem.setEnabled(false);
                    } else {
                        questionnaireBaseWindowController.getQuestionnaireBaseWindow().btnDeleteVersion.setEnabled(true);
                        questionnaireBaseWindowController.getQuestionnaireBaseWindow().deleteVersionMenuItem.setEnabled(true);
                    }
                }
                try {
                    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
//                    cvQuestionsData = getQuestionnaireDatas();
                    HashMap hmQuestionnaireData = getQuestionnaireDatas(true);
                    cvQuestionsData = (CoeusVector)hmQuestionnaireData.get("QUESTIONNAIRE_DATA");
                    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                }
                //Commented and Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected - start
                // Enabled for COEUSQA-3751 : Coeus4.5.x: Questionnaire Error - Cannot Add Questions to Pre-Existing Quesitonnaires - Start
                // Details updated in the Base window is updated to the list by loading all the list values
                questionnaireTableModel.setData(cvQuestionsData);
                // Enabled for COEUSQA-3751 : Coeus4.5.x: Questionnaire Error - Cannot Add Questions to Pre-Existing Quesitonnaires - End
                // 4272: Maintain history of Questionnaire - End
                groupTypeCode = Integer.parseInt(selectedGroup.getCode());
                if(groupTypeCode != 0){
                    Equals groupEquals = new Equals("groupTypeCode",groupTypeCode);
                    CoeusVector cvFilteredGroup = cvQuestionsData.filter(groupEquals);
                    questionnaireTableModel.setData(cvFilteredGroup);
                    if(cvFilteredGroup != null && !cvFilteredGroup.isEmpty()){
                        questionnaireListWindow.tblResults.setRowSelectionInterval(0,0);
                    }
                }else{
                    questionnaireTableModel.setData(cvQuestionsData);
                }
                //Commented and Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected - end
            }
        }
    }
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
         if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    // 4272: Maintain history of Questionnaire - Start
    private boolean checkIsQuestionnaireAnswered(Integer questionnaireId) throws CoeusException {
        boolean isQuestionAnswered = true;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( CHECK_QUESTIONNAIRE_ANSWERED );
        requester.setDataObject(questionnaireId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            isQuestionAnswered  = ((Boolean) response.getDataObject()).booleanValue();
        }
        return isQuestionAnswered;
  }
    // 4272: Maintain history of Questionnaire - End
    
    // 4597: Questionaire maintenance is not checking for rights - Start
    private void enableDisableButtons() {
        boolean hasRight = fetchQuestionnaireRight();
        
        questionnaireListWindow.mnuItmNewQuestiionnaire.setEnabled(hasRight);
        questionnaireListWindow.mnuItmModifyQuestiionnaire.setEnabled(hasRight);
        questionnaireListWindow.mnuItmCopyQuestiionnaire.setEnabled(hasRight);
        questionnaireListWindow.mnuItmSaveas.setEnabled(hasRight);
                
        questionnaireListWindow.btnModifyQuestiionnaire.setEnabled(hasRight);
        questionnaireListWindow.btnNewQuestiionnaire.setEnabled(hasRight);
        questionnaireListWindow.btnCopyQuestionnaire.setEnabled(hasRight);
        questionnaireListWindow.btnSaveas.setEnabled(hasRight);
    }
    
    
    private boolean fetchQuestionnaireRight() {
        boolean hasQuestionnaireRight = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ FUNCTION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(MAINTAIN_QUESTIONNAIRE);
        request.setDataObject(FN_USER_HAS_OSP_RIGHT);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()) {
            if(response.getDataObject() != null){
                Boolean right = (Boolean) response.getDataObject();
                hasQuestionnaireRight = right.booleanValue();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                hasQuestionnaireRight = false ;
            }
        }
        
        return  hasQuestionnaireRight ;
        
    }
    // 4597: Questionaire maintenance is not checking for rights - End
        
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only questionnaireId, name and description
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","questionnaireId" },
            {"1","name" },
            {"2","description"},
            {"3","finalFlag"},
        };
        boolean sort =true;
        /**
         * @param evt
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                if(cvQuestionsData!=null && cvQuestionsData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvQuestionsData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    questionnaireTableModel.fireTableRowsUpdated(0, questionnaireTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
}
