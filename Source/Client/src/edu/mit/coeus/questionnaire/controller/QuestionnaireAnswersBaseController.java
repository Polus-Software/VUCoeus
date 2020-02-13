/*
 * @(#)QuestionnaireAnswersController.java September 29, 2006, 12:57 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireAnswersController.java
 *
 * Created on September 29, 2006, 12:57 PM
 */

/* PMD check performed, and commented unused imports and variables on 28-SEP-2007
 * by Noorul
 */
package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
//import edu.mit.coeus.questionnaire.gui.PrintQuestionnaireForm;
//import edu.mit.coeus.questionnaire.gui.QuestionAnswersForm;
import edu.mit.coeus.questionnaire.gui.QuestionnaireAnswersBaseForm;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrentLockForm;
//import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JInternalFrame;
//import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;

/**
 * Constructor for Base class
 * @author tarique
 */
public class QuestionnaireAnswersBaseController extends Controller
        implements ActionListener, ChangeListener, VetoableChangeListener {
//    private static final int WIDTH = 950;
//    private static final int HEIGHT = 600;
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final char GET_QUESTIONS_MODE = 'I';   
    final String connectTo = CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET;
    private static final char SAVE_QUESTIONS_ANS_AND_HEADER = 'N';
    private static final String EMPTY_STRING = "";
    private int selTab = 0;
    private boolean closed = false;
    private boolean isClosed;
    private ChangePassword changePassword;
    public QuestionnaireAnswersBaseForm questionnaireAnswersBaseForm;
    private QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean;
    private CoeusVector cvData;
    private List lstModController;
    private CoeusMessageResources coeusMessageResources;
    private String title;
    private CoeusAppletMDIForm mdiForm;
    //Code added for coeus4.3 Questionnaire enhancement case#2946
    private QuestionAnswersController questionAnswersController;
    private static final char SAVE = 'R';
    private static final char RESTART = 'S';
    private static final char MODIFY = 'T';    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    private HashMap allSelectedOriginalProtocolQnrs;
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    /** Creates a new instance of QuestionnaireAnswersController */
    public QuestionnaireAnswersBaseController(String title,CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        this.title = title;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        
    }
    /** Method to set tab data */
    public void setTabData() throws CoeusException{
        //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - starts
//        Vector vecModController = new Vector();
//        QuestionAnswersController questionAnswersController;
//        for(int index = 0; index < cvData.size() ; index ++) {
//            QuestionnaireAnswerHeaderBean answerBean =
//                (QuestionnaireAnswerHeaderBean)cvData.get(index);
//            //setting for getting answer
//            answerBean.setModuleItemCode(questionnaireAnswerHeaderBean.getModuleItemCode());
//            answerBean.setModuleItemKey(questionnaireAnswerHeaderBean.getModuleItemKey());
//            answerBean.setModuleSubItemCode(questionnaireAnswerHeaderBean.getModuleSubItemCode());
//            answerBean.setModuleSubItemKey(questionnaireAnswerHeaderBean.getModuleSubItemKey());
//            questionAnswersController = new QuestionAnswersController();
//            questionAnswersController.setFunctionType(getFunctionType());
//            questionAnswersController.setFormData(answerBean);
//            ((QuestionnaireAnswersBaseForm)getControlledUI()).tbdPnQuesAnswer.
//                addTab(answerBean.getLabel(), questionAnswersController.getControlledUI());
//            vecModController.add(questionAnswersController);
//        }
        //If user add more questionnaire
//        int prevSize = getLstModController() == null ? 0 : getLstModController().size();
//        setLstModController(vecModController);
//        if(prevSize > 0 && getLstModController().size() > 0 ) {
//            if(prevSize == getLstModController().size()) {
//                if(selTab > 0) {
//                    questionnaireAnswersBaseForm.tbdPnQuesAnswer.setSelectedIndex(selTab);
//                    selTab = 0;
//                }
//            }else {
//                questionnaireAnswersBaseForm.tbdPnQuesAnswer.setSelectedIndex(0);
//            }
//        }
        questionAnswersController = new QuestionAnswersController(questionnaireAnswerHeaderBean.getModuleItemKey());
        questionAnswersController.setFunctionType(getFunctionType());
        questionAnswersController.setFormData(questionnaireAnswerHeaderBean);
        questionAnswersController.setQuestionnaireAnswersBaseController(this);
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        ((QuestionnaireAnswersBaseForm)getControlledUI()).pnlQuestionnaire.add(questionAnswersController.getControlledUI(),
                gridBagConstraints);
        //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - ends
    }
    /** displays the Form which is being controlled.
     */
    public void display() {
      try{
            questionnaireAnswersBaseForm.setMenus();
            mdiForm.putFrame(title,questionnaireAnswersBaseForm);
            mdiForm.getDeskTopPane().add(questionnaireAnswersBaseForm);
            questionnaireAnswersBaseForm.setVisible(true);
            questionnaireAnswersBaseForm.setSelected(true);
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
            CoeusOptionPane.showErrorDialog(propertyVetoException.getMessage());
        }catch (Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
     /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
        //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - starts
//        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
//            questionnaireAnswersBaseForm.saveMenuItem.setEnabled(false);
//            questionnaireAnswersBaseForm.btnSave.setEnabled(false);
//        }        
        questionnaireAnswersBaseForm.saveMenuItem.setEnabled(false);
        questionnaireAnswersBaseForm.btnSave.setEnabled(false);
        questionnaireAnswersBaseForm.btnModify.setEnabled(false);
        questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(false);
        questionnaireAnswersBaseForm.btnStartOver.setEnabled(false);
        questionnaireAnswersBaseForm.startOverMenuItem.setEnabled(false);
        questionnaireAnswersBaseForm.btnPrint.setEnabled(false);
        questionnaireAnswersBaseForm.printMenuItem.setEnabled(false);
        //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - ends
    }
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public java.awt.Component getControlledUI() {
        return questionnaireAnswersBaseForm;
    }
   
    /**
     * Method to get data object from the form
     * @return object of data object
     */    
    public Object getFormData() {
        return cvData;
    }
    /** registers GUI Components with event Listeners.
     */
    public void registerComponents() {
        questionnaireAnswersBaseForm = new QuestionnaireAnswersBaseForm(title, mdiForm);
        questionnaireAnswersBaseForm.tbdPnQuesAnswer.addChangeListener(this);
        questionnaireAnswersBaseForm.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        questionnaireAnswersBaseForm.saveMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.btnSave.addActionListener(this);
        questionnaireAnswersBaseForm.btnClose.addActionListener(this);
        questionnaireAnswersBaseForm.closeMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.inboxMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        questionnaireAnswersBaseForm.delegationsMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        questionnaireAnswersBaseForm.preferencesMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.changePasswordMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.exitMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.currentLocksMenuItem.addActionListener(this);
        //Code added for coeus4.3 Questionnaire enhancement case#2946 - starts
        questionnaireAnswersBaseForm.btnModify.addActionListener(this);
        questionnaireAnswersBaseForm.btnStartOver.addActionListener(this);
        questionnaireAnswersBaseForm.btnPrint.addActionListener(this);
        questionnaireAnswersBaseForm.modifyMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.startOverMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.printMenuItem.addActionListener(this);
        questionnaireAnswersBaseForm.addVetoableChangeListener(this);
        //Code added for coeus4.3 Questionnaire enhancement case#2946 - ends
    }
    /**
     * saves the Form Data.
     * @throws CoeusException if any exception occurs
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        //Code added for coeus4.3 Questionnaire enhancement case#2946 - starts
        //For saving the questionnaire answers data
//        CoeusVector  cvDataToServer = null;
//        if(getLstModController() != null && getLstModController().size() >0) {
//            cvDataToServer = new CoeusVector();
//            if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
//                for(int index = 0; index < getLstModController().size(); index ++) {
//                    Controller controller = (Controller)getLstModController().get(index);
//                    CoeusVector cvSaveData = (CoeusVector)controller.getFormData();
//                    cvDataToServer.addAll(cvSaveData);
//                }
//            }
//        }
//        if(cvDataToServer != null) {
//            saveDataToServer(cvDataToServer);
//        }
//        questionAnswersController.saveAndGetQuestions(SAVE);
        questionAnswersController.processData(SAVE);
        //Code added for coeus4.3 Questionnaire enhancement case#2946 - ends
    }
//    private void saveDataToServer(CoeusVector cvDataToServer) throws CoeusException{
//        CoeusVector cvHeaderData = new CoeusVector();
//        CoeusVector cvAnswerData = new CoeusVector();
//        CoeusVector cvTempData = null;
//        if(!cvDataToServer.isEmpty()) {
//            cvTempData = getHeaderData(cvDataToServer);
//            if(cvTempData != null && !cvTempData.isEmpty()) {
//                cvHeaderData.addAll(cvTempData);
//            }
//            cvTempData = getAnswerData(cvDataToServer);
//            if(cvTempData != null && !cvTempData.isEmpty()) {
//                cvAnswerData.addAll(cvTempData);
//            }
//        }
//        Vector vecData = new Vector();
//        vecData.add(0, cvHeaderData);
//        vecData.add(1, cvAnswerData);
//        RequesterBean request = new RequesterBean();
//        ResponderBean response = null;
//        request.setFunctionType(SAVE_QUESTIONS_ANS_AND_HEADER);
//        request.setDataObjects(vecData);
//        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
//        comm.send();
//        response = comm.getResponse();
//        if(response != null){
//            if(!response.isSuccessfulResponse()){
//                throw new CoeusException(response.getMessage());
//            }
//        }else{
//            throw new CoeusException(response.getMessage());
//        }
//        cvHeaderData = null;
//        cvAnswerData = null;
//        cvTempData = null;
//        cvData = null;
//        setFormData(questionnaireAnswerHeaderBean);
//        if(cvData != null && !cvData.isEmpty()) {
//            selTab = questionnaireAnswersBaseForm.tbdPnQuesAnswer.getSelectedIndex();
//            questionnaireAnswersBaseForm.tbdPnQuesAnswer.removeAll();
//            try{
//                setTabData();
//            }catch(CoeusException ce ) {
//                ce.printStackTrace();
//                CoeusOptionPane.showErrorDialog(ce.getMessage());
//            }
//        }
//     }
    private CoeusVector getHeaderData(CoeusVector cvDataToServer) {
        CoeusVector cvHeadData = new CoeusVector();
        QuestionnaireAnswerHeaderBean ansHeaderBean = null;
        //compare with dB data
        for(int mainIndex = 0; mainIndex < cvData.size(); mainIndex ++ ) {
            boolean isExist = false;
            int answerExist = 0;
            QuestionnaireAnswerHeaderBean quesDBBean =
            (QuestionnaireAnswerHeaderBean)cvData.get(mainIndex);
            // Commented for generating new completion id start 1
            //String questionCompId = questionnaireAnswerHeaderBean.getModuleItemKey()
            //  + quesDBBean.getQuestionnaireId();
            // Commented for generating new completion id end 1
            QuestionnaireQuestionsBean quesBean = null;
            for(int index = 0; index < cvDataToServer.size(); index ++ ) {
                quesBean = (QuestionnaireQuestionsBean)cvDataToServer.get(index);
                
                if(quesDBBean.getQuestionnaireId() == quesBean.getQuestionnaireId()) {
                    if(quesBean.getAnswer() != null && quesBean.getAnswer().length() > 0) {
                        answerExist = 1;
                        if(quesBean.getAcType() != null && quesBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                            isExist = true;
                            break;
                        }
                        
                    }
                    
                }
                
            }//end for
            
            ansHeaderBean = new QuestionnaireAnswerHeaderBean();
            ansHeaderBean.setModuleItemCode(questionnaireAnswerHeaderBean.getModuleItemCode());
            ansHeaderBean.setModuleSubItemCode(questionnaireAnswerHeaderBean.getModuleSubItemCode());
            ansHeaderBean.setModuleItemKey(questionnaireAnswerHeaderBean.getModuleItemKey());
            ansHeaderBean.setModuleSubItemKey(questionnaireAnswerHeaderBean.getModuleSubItemKey());
            ansHeaderBean.setQuestionnaireId(quesDBBean.getQuestionnaireId());
            // Commented for generating new completion id start 2
            //ansHeaderBean.setQuestionnaireCompletionId(questionCompId);
            // Commented for generating new completion id end 2
            if(!isExist && answerExist > 0){
                ansHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
            }else{
                if(isExist && answerExist > 0) {
                    ansHeaderBean.setQuestionnaireCompletionId(quesBean.getAwQuestionnaireCompletionId());
                    ansHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
            cvHeadData.addElement(ansHeaderBean);
            
        }//end for
        return cvHeadData;
        
    }
    private CoeusVector getAnswerData(CoeusVector cvDataToServer) {
        CoeusVector cvAnsData = new CoeusVector();
        QuestionAnswerBean ansBean = null;
        for(int index = 0; index < cvDataToServer.size(); index ++ ) {
            QuestionnaireQuestionsBean quesBean =
                (QuestionnaireQuestionsBean)cvDataToServer.get(index);
            // Commented for generating new completion id start 3
           // String questionCompId = questionnaireAnswerHeaderBean.getModuleItemKey()
             //               + quesBean.getQuestionnaireId();
            // Commented for generating new completion id end 3
            if(quesBean.getAcType() == null) {
                if(quesBean.getAnswer() != null && quesBean.getAnswer().length() > 0) {
                    ansBean = new QuestionAnswerBean();
                    // Commented for generating new completion id start 4
                    //ansBean.setQuestionnaireCompletionId(questionCompId);
                    // Commented for generating new completion id end 4
                    ansBean.setQuestionId(quesBean.getQuestionId());
                    ansBean.setQuestionNumber(quesBean.getQuestionNumber().intValue());
                    ansBean.setQuestionnaireId(quesBean.getQuestionnaireId());
                    //answer number setting will be changed in future
                    ansBean.setAnswerNumber(1);
                    ansBean.setAnswer(quesBean.getAnswer());
                    ansBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvAnsData.addElement(ansBean);
                }
            }else if(quesBean.getAcType() != null 
                && quesBean.getAcType().equals(TypeConstants.UPDATE_RECORD) ) {
                    ansBean = new QuestionAnswerBean();
                   // Modified for generating new completion id start 5
                    ansBean.setQuestionnaireCompletionId(quesBean.getAwQuestionnaireCompletionId());
                    // Modified for generating new completion id end 5
                    ansBean.setQuestionId(quesBean.getQuestionId());
                    ansBean.setQuestionNumber(quesBean.getQuestionNumber().intValue());
                    //answer number setting will be changed in future
                    ansBean.setAnswerNumber(quesBean.getAnswerNumber());
                    ansBean.setAnswer(quesBean.getAnswer());
                    ansBean.setAwUpdateTimestamp(quesBean.getUpdateTimestamp());
                    ansBean.setAwQuestionNumber(quesBean.getQuestionNumber().intValue());
                    ansBean.setAwAnswerNumber(quesBean.getAnswerNumber());
                    ansBean.setAwQuestionnaireCompletionId(quesBean.getAwQuestionnaireCompletionId());
                    ansBean.setAcType(TypeConstants.UPDATE_RECORD);
                    cvAnsData.addElement(ansBean); 
                
            }
        }
        return cvAnsData;
    }
     /**
      * This method is used to set the form data specified in
      * <CODE> data </CODE>
      * @param data to set to the form
      * @throws CoeusException if any exception occurs
      */
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)data;
        if(questionnaireAnswerHeaderBean.getModuleItemDescription().equals(CoeusGuiConstants.PROPOSAL_MODULE)) {
            //set the dialog title for subitemcode based on subitem name
          questionnaireAnswersBaseForm.setTitle(title+" "+questionnaireAnswerHeaderBean.getModuleItemKey());
            
        }else if(questionnaireAnswerHeaderBean.getModuleItemDescription().equals(CoeusGuiConstants.AWARD_MODULE)){
            //set the dialog title for subitemcode based on subitem name
          questionnaireAnswersBaseForm.setTitle(title+" "+questionnaireAnswerHeaderBean.getModuleItemKey());
            if(!questionnaireAnswerHeaderBean.getModuleItemDescription().
                equals(questionnaireAnswerHeaderBean.getModuleSubItemDescription())){
                 //change the sub item description and item key  and concatinate
                // dlgQuesAnswer.setTitle("Proposal "+questionnaireAnswerHeaderBean.getModuleItemKey()+" Questionnaire");   
            }
        }else if(questionnaireAnswerHeaderBean.getModuleItemDescription().equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            //set the dialog title for subitemcode based on subitem name
            questionnaireAnswersBaseForm.setTitle(title+" "+questionnaireAnswerHeaderBean.getModuleItemKey());
            if(!questionnaireAnswerHeaderBean.getModuleItemDescription().
                equals(questionnaireAnswerHeaderBean.getModuleSubItemDescription())){
                 //change the sub item description and item key  and concatinate
                // dlgQuesAnswer.setTitle("Proposal "+questionnaireAnswerHeaderBean.getModuleItemKey()+" Questionnaire");   
            }
        }else if(questionnaireAnswerHeaderBean.getModuleItemDescription().equals(CoeusGuiConstants.PROTOCOL_MODULE)){
            //set the dialog title for subitemcode based on subitem name
          questionnaireAnswersBaseForm.setTitle(title+" "+questionnaireAnswerHeaderBean.getModuleItemKey());
            if(!questionnaireAnswerHeaderBean.getModuleItemDescription().
                equals(questionnaireAnswerHeaderBean.getModuleSubItemDescription())){
                 //change the sub item description and item key  and concatinate
                // dlgQuesAnswer.setTitle("Proposal "+questionnaireAnswerHeaderBean.getModuleItemKey()+" Questionnaire");   
            }
        }
        
        cvData = getDataFromServer();
        
    }
    private CoeusVector getDataFromServer() throws CoeusException{
        CoeusVector cvMainData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_QUESTIONS_MODE);
        //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - starts
//        request.setDataObject(questionnaireAnswerHeaderBean);
        CoeusVector cvQuestionnaireData = new CoeusVector();
        cvQuestionnaireData.add(questionnaireAnswerHeaderBean);
        request.setDataObject(cvQuestionnaireData);
        //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - ends
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                cvMainData = (CoeusVector)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvMainData;
    }
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if(getLstModController() != null && getLstModController().size() >0) {
            if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
                for(int index = 0; index < getLstModController().size(); index ++) {
                    Controller controller = (Controller)getLstModController().get(index);
                    if(!controller.validate()) {
                        questionnaireAnswersBaseForm.tbdPnQuesAnswer.setSelectedIndex(index);
                        return false;
                    }
                }
            }
        }
        return true;
    }
     /**
      * Method to get the action on buttons
      * @param e event on action button
      */     
     public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source =  e.getSource();
        try{
            blockEvents(true);
            if(source.equals(questionnaireAnswersBaseForm.saveMenuItem) || source.equals(questionnaireAnswersBaseForm.btnSave)){
                performOkAction();
            }else if(source.equals(questionnaireAnswersBaseForm.changePasswordMenuItem)){
                showChangePassword();
            }else if(source.equals(questionnaireAnswersBaseForm.preferencesMenuItem)){
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(questionnaireAnswersBaseForm.delegationsMenuItem)){
                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(questionnaireAnswersBaseForm.exitMenuItem)){
                exitApplication();
            }else if(source.equals(questionnaireAnswersBaseForm.closeMenuItem) || source.equals(questionnaireAnswersBaseForm.btnClose)){
                try{
                    close();
                }catch (PropertyVetoException propertyVetoException) {
                    //Don't do anything. this exception is thrown to stop window from closing.
                }
            }else if(source.equals(questionnaireAnswersBaseForm.inboxMenuItem)){
                showInbox();
            }
            else if(source.equals(questionnaireAnswersBaseForm.currentLocksMenuItem)){
                showLocksForm();
            }
            //Code added for coeus4.3 Questionnaire enhancement case#2946 - starts
            else if(source.equals(questionnaireAnswersBaseForm.modifyMenuItem) 
                || source.equals(questionnaireAnswersBaseForm.btnModify)){
                questionAnswersController.processData(MODIFY);
            }
            else if(source.equals(questionnaireAnswersBaseForm.startOverMenuItem) 
                || source.equals(questionnaireAnswersBaseForm.btnStartOver)){
                int selection = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1017"),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                if(selection == 0){
                    questionAnswersController.processData(RESTART);
                }
            }
            else if(source.equals(questionnaireAnswersBaseForm.printMenuItem) 
                || source.equals(questionnaireAnswersBaseForm.btnPrint)){
                //Modified with case 4287:Questionnaire Templates.
                //The printing would be ‘Print Questions & Answers’ and ‘PrintAll Questions’ by default.
//                showPrintForm();
                questionAnswersController.printQuestionnaire(true,true, false);
            }
            //Code added for coeus4.3 Questionnaire enhancement case#2946 - ends
            
        }catch (Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        finally{
            blockEvents(false);
        }
  
     }
     /** close the internal frame based on the operation did. If the data changes
      *then ask for the save confrmation and then close the window
      */
     private void close() throws PropertyVetoException{
         if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
             //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - starts
//             boolean isDataChanged = false;
//             if(getLstModController() != null && getLstModController().size() >0) {
//                 for(int index = 0; index < getLstModController().size(); index ++) {
//                     Controller controller = (Controller)getLstModController().get(index);
//                     if(((QuestionAnswersController)controller).checkDataModified()) {
//                         isDataChanged = true;
//                         break;
//                     }
//                 }
//             }
             
//             if(isDataChanged){
             if(questionAnswersController.checkDataModified()) {
             //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - ends
                 int selection = CoeusOptionPane.showQuestionDialog(
                 coeusMessageResources.parseMessageKey("questions_exceptionCode.1005"), 
                 CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                 if(selection == CoeusOptionPane.SELECTION_YES) {
                     try{
                         //Code added for coeus4.3 Questionnaire enhancement case#2946
                         if(!performOkAction()){
                             // 4272: Maintain History of questionnaires
                             //return;
                             throw new PropertyVetoException("Cancel",null);
                         }
                         if(isClosed){
                             throw new PropertyVetoException("Cancel",null);
                         }
                     }catch (Exception coeusUIException) {
                         //Validation Failed
                         throw new PropertyVetoException(EMPTY_STRING, null);
                     }
                 }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                     throw new PropertyVetoException(EMPTY_STRING, null);
                 }else if(selection == CoeusOptionPane.SELECTION_NO) {
                     isClosed = false;
                 }
             }
         }
         mdiForm.removeFrame(title);
         closed = true;
         questionnaireAnswersBaseForm.doDefaultCloseAction();
         cleanUp();
         isClosed = false;
     }
    private boolean performOkAction() {
        boolean isSaved = false;
        try {
            //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - starts
//            if(validate()) {
            if(questionAnswersController.validate()) {                
                saveFormData();
                isSaved = true;
            }
        }catch(Exception ce ) {
            CoeusOptionPane.showWarningDialog(ce.getMessage());
            return isSaved;
        }
        return isSaved;
        //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - ends
    }
   
    /**
     * Getter for property lstModController.
     * @return Value of property lstModController.
     */
    public List getLstModController() {
        return lstModController;
    }
    
    /**
     * Setter for property lstModController.
     * @param lstModController New value of property lstModController.
     */
    public void setLstModController(List lstModController) {
        this.lstModController = lstModController;
    }
    /**
     * Method to clean up the object when window dispose
     */    
    public void cleanUp() {
        cvData = null;
        lstModController = null;
        questionnaireAnswersBaseForm.tbdPnQuesAnswer.removeAll();
        questionnaireAnswersBaseForm.tbdPnQuesAnswer = null;
        questionnaireAnswersBaseForm = null;
        
    }
    
    /**
     * Method to change state when tab is selected
     * @param e change event
     */    
    public void stateChanged(javax.swing.event.ChangeEvent e) {
        if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
            if(getLstModController() != null && getLstModController().size() > 0) {
                int selTab = questionnaireAnswersBaseForm.tbdPnQuesAnswer.getSelectedIndex();
                if(selTab != -1) {
                    Controller controller = (Controller)lstModController.get(selTab);
                    controller.formatFields();
                }
            }
            
        }
    }
    
    public void vetoableChange(java.beans.PropertyChangeEvent evt) throws java.beans.PropertyVetoException {
         if(closed) return ;
        boolean changed = ((Boolean) evt.getNewValue()).booleanValue();
        if(evt.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
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
        UserDelegationForm userDelegationForm  =  new UserDelegationForm(mdiForm,true);        
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
        if (answer == CoeusOptionPane.SELECTION_YES) {
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
    
    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * Method for closing the window
     */    
    public void closeWindow(){
         mdiForm.removeFrame(title);
         closed = true;
         questionnaireAnswersBaseForm.doDefaultCloseAction();
         cleanUp();
         isClosed = false;        
    }
    
    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * Method to save questionnaire answers and close the window
     */
    public void saveAndExit()throws PropertyVetoException{
        try{
            if(!performOkAction()){
                return;
            }
            if(isClosed){
                throw new PropertyVetoException("Cancel",null);
            }
        }catch (Exception coeusUIException) {
            //Validation Failed
            throw new PropertyVetoException(EMPTY_STRING, null);
        }
        closeWindow();
    }
    
    /** Show the printing options form
     */
    //Commented this method with case 4287:Ability to define Questionnaire Templates.
    //PrintQuestionnaireForm is no more required. The default selection would be
    //‘Print Questions & Answers’ and ‘PrintAll Questions’.
    /*private void showPrintForm(){
        PrintQuestionnaireForm printQuestionnaireForm = null;
        printQuestionnaireForm = new PrintQuestionnaireForm(questionnaireAnswerHeaderBean.getModuleItemKey(), questionnaireAnswerHeaderBean);
        printQuestionnaireForm.display();
    }*/
    //4287:end
    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * Setter for property allSelectedOriginalProtocolQnrs
     */
    public void setAllSelectedOriginalProtocolQnr(HashMap originalQnr) {
        this.allSelectedOriginalProtocolQnrs = originalQnr;
    }
    
    /**
     * Getter for property allSelectedOriginalProtocolQnrs
     */
    public HashMap getAllSelectedOriginalProtocolQnr() {
       return allSelectedOriginalProtocolQnrs;
    }
    // CoeusQA2313: Completion of Questionnaire for Submission - End
  }
