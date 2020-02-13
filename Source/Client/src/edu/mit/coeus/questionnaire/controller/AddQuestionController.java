/*
 * QuestionController.java
 *
 * Created on September 22, 2006, 4:44 PM
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.questionnaire.bean.QuestionExplanationBean;
import edu.mit.coeus.questionnaire.bean.QuestionsMaintainanceBean;
import edu.mit.coeus.questionnaire.gui.AddQuestionForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;

/**
 *
 * @author  noorula
 */
public class AddQuestionController extends QuestionnaireController implements ActionListener, ItemListener, FocusListener {
    
    private AddQuestionForm addQuestionForm;
    private CoeusDlgWindow dlgAddQuestionsForm ;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    // 4272: Maintain history of Questionnaires - Start
//    private static final String ADD_WINDOW_TITLE = "Add Question";
    private static final String ADD_WINDOW_TITLE = "New Question";
    // 4272: Maintain history of Questionnaires - End
    private static final String MODIFY_WINDOW_TITLE = "Modify Question";
    private static final String DISPLAY_WINDOW_TITLE = "Display Question";
    // Case#: 3524: Add Explanation field to Questions -Start
    private static final int WIDTH = 700;
    private static final int HEIGHT = 230;
    // Case#: 3524: Add Explanation field to Questions -End
    private boolean okClicked;
    private QuestionsMaintainanceBean bean;
    private static final char GET_QUES_ID = 'F';
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static CoeusVector cvLookupArgs;
    private static final String CODE_TABLE = "CODE_TABLE";
    private static final String ARG_TABLE = "ARG_TABLE";
    private static final String OTHER = "Other";
    private static final String GET_SERVLET = "/BudgetMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private static final char GET_ARGS_MODULES = 'W';
    private String lookUpGUIValue = EMPTY_STRING;
    private boolean changed;
    private char mode;
    private CoeusMessageResources  coeusMessageResources;
    private static final String  EMPTY_QUESTION = "questions_exceptionCode.1000";
    private static final String  EMPTY_ANSWER_TYPE = "questions_exceptionCode.1001";
    private static final String  EMPTY_ANSWER_LENGTH = "questions_exceptionCode.1002";
    private static final String  EMPTY_MAX_ANSWER = "questions_exceptionCode.1003";
    private static final String  EMPTY_VALID_ANSWER = "questions_exceptionCode.1004";
    private static final String  CONFIRM_WINDOW = "questions_exceptionCode.1005";
    private static final String  EMPTY_LOOKUP_GUI = "questions_exceptionCode.1006";
    //Added for question group - start - 1
    private static final String EMPTY_GROUP = "questions_exceptionCode.1011";
    private Vector cvQuestionGroup;
    //Added for question group - end - 1
    private boolean isNewMode;
    
    //Case 2360 Start 1
    private static final String  ANSWER_LENGTH_EXCEED = "questions_exceptionCode.1010";
    //Case 2360 End 1
    
    //Added for case#3083 - Question Maintenance gives exception if answer length is too long - start
    private boolean formLoadFlag;
    private static final String VALID_ANSWER = "questions_exceptionCode.1012";
    private static final String MAX_QUESTION_DESCRIPTION = "questions_exceptionCode.1013";
    //Added for case#3083 - Question Maintenance gives exception if answer length is too long - end
    // Case#: 3524: Add Explanation field to Questions -Start
    private CoeusVector vecQuestionExplanation;   
    private QuestionExplanationController explanationController;
    // Case#: 3524: Add Explanation field to Questions - End
    // 4272: Maintain history of Questionnaires - Start
    private int questionVersionNumber;
    private boolean isNewVersion;
    private static final String  MAX_ANSWERS_CANT_BE_ZERO = "questions_exceptionCode.1016";
    private static final String  ANS_LENGTH_CANT_BE_ZERO = "questions_exceptionCode.1015";
    // 4272: Maintain history of Questionnaires - End
    /** Creates a new instance of QuestionController */
    public AddQuestionController(char mode, boolean formLoadFlag) {
        this.mode = mode;
        //Added for case#3083 - Question Maintenance gives exception if answer length is too long
        this.formLoadFlag = formLoadFlag;
        registerComponents();
        formatFields();
        postInitComponents();
    }
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgAddQuestionsForm = new CoeusDlgWindow(mdiForm);
        dlgAddQuestionsForm.setResizable(false);
        dlgAddQuestionsForm.setModal(true);
        dlgAddQuestionsForm.getContentPane().add(addQuestionForm);
        dlgAddQuestionsForm.setFont(CoeusFontFactory.getLabelFont());
        dlgAddQuestionsForm.setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddQuestionsForm.getSize();
        dlgAddQuestionsForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgAddQuestionsForm.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                // requestDefaultFocus();
            }
        });
        
        dlgAddQuestionsForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
        
        dlgAddQuestionsForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAddQuestionsForm.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        //code for disposing the window ends
    }
    
    public void actionPerformed(java.awt.event.ActionEvent event) {
        Object source = event.getSource();
        try{
            if(source.equals(addQuestionForm.btnOk)){
                performOKAction();
            }else if(source.equals(addQuestionForm.btnCancel)){
                performCancelAction();
                // Case# 3524: Add Explanation field to Questions - Start
            }else if(source.equals(addQuestionForm.btnMore)){
                performMoreAction();
            }
            // Case# 3524: Add Explanation field to Questions - End
        }catch (Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    
    public void display() {
        changed = false;
        
        if(mode == TypeConstants.DISPLAY_MODE){
            setRequestFocusInThread(addQuestionForm.btnCancel);
        } else {
            setRequestFocusInThread(addQuestionForm.txtArQuestion);
        }
        dlgAddQuestionsForm.setVisible(true);
    }
    
    public void formatFields() {
        Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
                getDefaults().get("Panel.background");
        boolean enabled = true;
        if(mode == TypeConstants.DISPLAY_MODE) {
            enabled = false;
            addQuestionForm.txtArQuestion.setBackground(disabledBackground);
            addQuestionForm.txtAnswerDataLength.setBackground(disabledBackground);
            addQuestionForm.txtMaxAnswer.setBackground(disabledBackground);
            addQuestionForm.txtArQuestion.setEditable(false);
            //Added for question group - start - 2
            addQuestionForm.cmbGroup.setEnabled(enabled);
            //Added for question group - end - 2
        }
        addQuestionForm.txtArQuestion.setEditable(enabled);
        addQuestionForm.txtMaxAnswer.setEnabled(enabled);
        addQuestionForm.cmbAnswerDataType.setEnabled(enabled);
        addQuestionForm.cmbValidAnswer.setEnabled(enabled);
        addQuestionForm.cmbLookupGUI.setEnabled(enabled);
        addQuestionForm.txtAnswerDataLength.setEnabled(enabled);
        addQuestionForm.cmbLookupName.setEnabled(enabled);
        // 4272: Maintain history of Questionnaires 
        addQuestionForm.chkActive.setEnabled(enabled);
        addQuestionForm.btnOk.setEnabled(enabled);
        addQuestionForm.btnOk.setMnemonic('O');
        addQuestionForm.btnCancel.setMnemonic('C');
        // Case# 3524: Add Explanation field to Questions - Start
        addQuestionForm.btnMore.setMnemonic('M');
        // Case# 3524: Add Explanation field to Questions - End
        //Added for question group - start - 3
        addQuestionForm.cmbGroup.setEnabled(enabled);
        //Added for question group - end - 3
        // //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window - Start
        if(!enabled){
            setComponentBackGroundToDisabled(addQuestionForm.txtMaxAnswer);
            setComponentBackGroundToDisabled(addQuestionForm.txtAnswerDataLength);
        }
        // //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window- End
    }
    
    /** This method returns the instance of view
     * @return
     */
    public java.awt.Component getControlledUI() {
        return addQuestionForm;
    }
    
    public Object getFormData() {
        return bean;
    }
    
    public void registerComponents() {
        addQuestionForm = new AddQuestionForm();
        cvLookupArgs = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        // 4272: Maintain History of Questionnaires - Start
//        Component[] component = {addQuestionForm.txtArQuestion, addQuestionForm.cmbAnswerDataType, addQuestionForm.txtAnswerDataLength,
//        addQuestionForm.txtMaxAnswer, addQuestionForm.cmbValidAnswer, addQuestionForm.cmbLookupGUI, addQuestionForm.cmbLookupName,
//        addQuestionForm.cmbGroup, addQuestionForm.btnOk, addQuestionForm.btnCancel};
        
        Component[] component = {addQuestionForm.txtArQuestion, addQuestionForm.cmbAnswerDataType,addQuestionForm.cmbValidAnswer,
        addQuestionForm.txtMaxAnswer, addQuestionForm.txtAnswerDataLength,
        addQuestionForm.cmbLookupGUI, addQuestionForm.cmbLookupName, addQuestionForm.chkActive,
        addQuestionForm.cmbGroup, addQuestionForm.btnOk, addQuestionForm.btnCancel, addQuestionForm.btnMore};
        // 4272: Maintain History of Questionnaires - End
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        addQuestionForm.setFocusTraversalPolicy(policy);
        addQuestionForm.setFocusCycleRoot(true);
        addQuestionForm.txtMaxAnswer.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,2));
        addQuestionForm.txtAnswerDataLength.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        addQuestionForm.btnOk.addActionListener(this);
        addQuestionForm.btnCancel.addActionListener(this);
        // Case# 3524: Add Explanation field to Questions - Start
        addQuestionForm.btnMore.addActionListener(this);
        // Case# 3524: Add Explanation field to Questions - End
        addQuestionForm.cmbLookupGUI.addItemListener(this);
        addQuestionForm.cmbValidAnswer.addItemListener(this);
        addQuestionForm.cmbAnswerDataType.addItemListener(this);
        addQuestionForm.cmbLookupName.addItemListener(this);
        //Added for question group - start - 4
        addQuestionForm.cmbGroup.addItemListener(this);
        //Added for question group - end - 4
        //Added for case#3083 - Question Maintenance gives exception if answer length is too long
        addQuestionForm.cmbAnswerDataType.addFocusListener(this);
        
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        setComboBoxValues();
        if(data!=null) {
            bean = (QuestionsMaintainanceBean) data;
            addQuestionForm.lblQuestionIdValue.setText(bean.getQuestionId().toString());
            addQuestionForm.txtArQuestion.setText(bean.getDescription());
            addQuestionForm.txtMaxAnswer.setText((new Integer(bean.getMaxAnswers())).toString());
            addQuestionForm.cmbValidAnswer.setSelectedItem(bean.getValidAnswers());
            addQuestionForm.cmbAnswerDataType.setSelectedItem(bean.getAnswerDataType());
            addQuestionForm.cmbLookupGUI.setSelectedItem(bean.getLookupGui());
            addQuestionForm.cmbLookupName.setSelectedItem(bean.getLookupName());
            //Added for question group - start - 5
            addQuestionForm.cmbGroup.setSelectedIndex(getSelectedIndex(bean.getGroupTypeCode()));
            //Added for question group - end - 5
            // 4272: Maintain history of Questionnaires - Start
            if("A".equalsIgnoreCase(bean.getStatus())){
                addQuestionForm.chkActive.setSelected(true);
            }
            questionVersionNumber = bean.getVersionNumber();
            if(isNewVersion || questionVersionNumber > 1 && mode != TypeConstants.DISPLAY_MODE){
                formatFieldsForVersion();
            }
            
//            dlgAddQuestionsForm.setTitle(MODIFY_WINDOW_TITLE);
            dlgAddQuestionsForm.setTitle(MODIFY_WINDOW_TITLE + " " + bean.getQuestionId() + " Version "+ questionVersionNumber);
            // 4272: Maintain history of Questionnaires - End
            if(bean.getAnswerMaxLength() != 0) {
                addQuestionForm.txtAnswerDataLength.setText((new Integer(bean.getAnswerMaxLength())).toString());
            }
            // 4272: Maintain history of Questionnaires - Start
            if("YN".equalsIgnoreCase(bean.getValidAnswers())){
                addQuestionForm.txtAnswerDataLength.setText("1");
                addQuestionForm.txtAnswerDataLength.setEnabled(false);
                //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window
                setComponentBackGroundToDisabled(addQuestionForm.txtAnswerDataLength);
            } else if ("YNX".equalsIgnoreCase(bean.getValidAnswers())){
                addQuestionForm.txtAnswerDataLength.setText("1");
                addQuestionForm.txtAnswerDataLength.setEnabled(false);
                //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window
                setComponentBackGroundToDisabled(addQuestionForm.txtAnswerDataLength);
            } else if ("Search".equalsIgnoreCase(bean.getValidAnswers())
                    //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window
                    && mode != TypeConstants.DISPLAY_MODE){
                addQuestionForm.txtAnswerDataLength.setEnabled(true);
            }
            // 4272: Maintain history of Questionnaires - End
        } else {
            isNewMode = true;
            Integer questionId = null;
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType( GET_QUES_ID );
            AppletServletCommunicator comm = new AppletServletCommunicator(
                    CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if ( !response.isSuccessfulResponse() ){
                throw new CoeusException(response.getMessage());
            }else{
                questionId =(Integer) response.getDataObject();
            }
            addQuestionForm.lblQuestionIdValue.setText("  "+questionId.toString());
            // 4272: Maintain history of Questionnaires - Start
            addQuestionForm.chkActive.setSelected(true);
            questionVersionNumber = 1;
           // bean.setVersionNumber(questionVersionNumber);
//            dlgAddQuestionsForm.setTitle(ADD_WINDOW_TITLE);
            dlgAddQuestionsForm.setTitle(ADD_WINDOW_TITLE + " " + questionId.toString() + " Version 1");
            // 4272: Maintain history of Questionnaires - End
        }
        
        if(mode == TypeConstants.DISPLAY_MODE){
            // 4272: Maintain history of Questionnaires - Start
//            dlgAddQuestionsForm.setTitle(DISPLAY_WINDOW_TITLE);
            dlgAddQuestionsForm.setTitle(DISPLAY_WINDOW_TITLE + " " + bean.getQuestionId() + " Version "+ questionVersionNumber);
            // 4272: Maintain history of Questionnaires - End
        }
        
        //Added for case#3083 - Question Maintenance gives exception if answer length is too long
        if(mode != TypeConstants.DISPLAY_MODE){
            formLoadFlag = false;
        }
    }
    
    // Case# 3524: Add Explanation field to Questions - Start
//    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
//        return false;
//    }
    
    /**
     * This method is used for checking if all the mandatory fields in Add Question Form
     * is Properly filled.
     * @throws CoeusUIException   
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
        if(addQuestionForm.txtArQuestion.getText().trim().length() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_QUESTION));
            setRequestFocusInThread(addQuestionForm.txtArQuestion);
            return false;
            
        } else if(addQuestionForm.txtArQuestion.getText().length() > 2000){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MAX_QUESTION_DESCRIPTION));
            setRequestFocusInThread(addQuestionForm.txtArQuestion);
            return false;
            
        } else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals(EMPTY_STRING)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_ANSWER_TYPE));
            setRequestFocusInThread(addQuestionForm.cmbAnswerDataType);
            return false;
        } else if(!addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Date") &&
                addQuestionForm.txtAnswerDataLength.getText().length() == 0 ) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_ANSWER_LENGTH));
            setRequestFocusInThread(addQuestionForm.txtAnswerDataLength);
            return false;
        } else if(addQuestionForm.txtMaxAnswer.getText().trim().length() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_MAX_ANSWER));
            setRequestFocusInThread(addQuestionForm.txtMaxAnswer);
            return false;
        } else if(addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals(EMPTY_STRING)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_VALID_ANSWER));
            setRequestFocusInThread(addQuestionForm.cmbValidAnswer);
            return false;
        // 4272: Maintain History of Questionnaires - Start    
        } else if("0".equals(addQuestionForm.txtMaxAnswer.getText().trim())) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MAX_ANSWERS_CANT_BE_ZERO));
            setRequestFocusInThread(addQuestionForm.txtMaxAnswer);
            return false;
        // 4272: Maintain History of Questionnaires - End
        }else if(addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Search") &&
                addQuestionForm.cmbLookupGUI.getSelectedItem().toString().equals(EMPTY_STRING)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_LOOKUP_GUI));
            setRequestFocusInThread(addQuestionForm.cmbLookupGUI);
            return false;
            //Added for question group - start - 7
        } else if(addQuestionForm.cmbGroup.getSelectedItem().toString().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_GROUP));
            setRequestFocusInThread(addQuestionForm.cmbGroup);
            return false;
        } else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Date") &&
                !addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Text")){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_ANSWER));
            setRequestFocusInThread(addQuestionForm.cmbAnswerDataType);
            return false;
        } else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Number") &&
                !(addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Search") ||
                addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Text"))){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_ANSWER));
            setRequestFocusInThread(addQuestionForm.cmbAnswerDataType);
            return false;
        }
        
        String ansLength = addQuestionForm.txtAnswerDataLength.getText();
        
        if(ansLength !=null && !ansLength.equals(EMPTY_STRING)){
            int answerLength =  Integer.parseInt(ansLength);
            if(answerLength >2000){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ANSWER_LENGTH_EXCEED));
                setRequestFocusInThread(addQuestionForm.txtAnswerDataLength);
                return false;
            // 4272: Maintain History of Questionnaires - Start    
            } else if(answerLength == 0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ANS_LENGTH_CANT_BE_ZERO));
                setRequestFocusInThread(addQuestionForm.txtAnswerDataLength);
                return false;
            }
            // 4272: Maintain History of Questionnaires - End
        }
        return true;
    }
    // Case# 3524: Add Explanation field to Questions - End
    
    private void setComboBoxValues() {
        try {
            ComboBoxBean comboBoxBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            addQuestionForm.cmbAnswerDataType.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("String", "String");
            addQuestionForm.cmbAnswerDataType.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Number", "Number");
            addQuestionForm.cmbAnswerDataType.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Date", "Date");
            addQuestionForm.cmbAnswerDataType.addItem(comboBoxBean);
            
            comboBoxBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Search", "Search");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Text", "Text");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("YN", "YN");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("YNX", "YNX");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            populateLookupWindowCombo();
            //Added for question group - start - 6
            populateQuestionGroups();
            //Added for question group - end - 6
            getLookupArgsAndModules();
        } catch (CoeusException coeusException) {
            coeusException.getMessage();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        
    }
    
    private void populateLookupWindowCombo() {
        // 4272: Maintain history of Questionnaires - Start
//        String[] lookupGUIData = {EMPTY_STRING,"PersonSearch","UnitSearch","RolodexSearch",
//        "SelectCostElement","CodeTable","ValueList"};
        String[] lookupGUIData = {EMPTY_STRING,"PersonSearch","UnitSearch","RolodexSearch",
        "SelectCostElement","CodeTable","ValueList", "OrganizationSearch", "SponsorSearch"};
        // 4272: Maintain history of Questionnaires - End
        ComboBoxBean comboBoxBean;
        addQuestionForm.cmbLookupGUI.removeAllItems();
        for(int index=0;index<lookupGUIData.length;index++) {
            comboBoxBean = new ComboBoxBean(lookupGUIData[index],lookupGUIData[index]);
            addQuestionForm.cmbLookupGUI.addItem(comboBoxBean);
        }
    }
    
    public void performOKAction(){
        // Case# 3524: Add Explanation field to Questions - Start
        try {
            
//        if(addQuestionForm.txtArQuestion.getText().trim().length() == 0) {
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_QUESTION));
//            setRequestFocusInThread(addQuestionForm.txtArQuestion);
//            return ;
//            //Added for case#3083 - Question Maintenance gives exception if answer length is too long - start
//        } else if(addQuestionForm.txtArQuestion.getText().length() > 2000){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MAX_QUESTION_DESCRIPTION));
//            setRequestFocusInThread(addQuestionForm.txtArQuestion);
//            return ;
//            //Added for case#3083 - Question Maintenance gives exception if answer length is too long - end
//        } else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals(EMPTY_STRING)) {
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_ANSWER_TYPE));
//            setRequestFocusInThread(addQuestionForm.cmbAnswerDataType);
//            return ;
//        } else if(!addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Date") &&
//                addQuestionForm.txtAnswerDataLength.getText().length() == 0 ) {
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_ANSWER_LENGTH));
//            setRequestFocusInThread(addQuestionForm.txtAnswerDataLength);
//            return ;
//        } else if(addQuestionForm.txtMaxAnswer.getText().trim().length() == 0) {
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_MAX_ANSWER));
//            setRequestFocusInThread(addQuestionForm.txtMaxAnswer);
//            return ;
//        } else if(addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals(EMPTY_STRING)) {
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_VALID_ANSWER));
//            setRequestFocusInThread(addQuestionForm.cmbValidAnswer);
//            return ;
//        } else if(addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Search") &&
//                addQuestionForm.cmbLookupGUI.getSelectedItem().toString().equals(EMPTY_STRING)) {
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_LOOKUP_GUI));
//            setRequestFocusInThread(addQuestionForm.cmbLookupGUI);
//            return ;
//            //Added for question group - start - 7
//        } else if(addQuestionForm.cmbGroup.getSelectedItem().toString().equals(EMPTY_STRING)){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(EMPTY_GROUP));
//            setRequestFocusInThread(addQuestionForm.cmbGroup);
//            return ;
//            //Added for question group - end - 7
//            //Added for case#3083 - Question Maintenance gives exception if answer length is too long - start
//        } else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Date") &&
//                !addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Text")){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_ANSWER));
//            setRequestFocusInThread(addQuestionForm.cmbAnswerDataType);
//            return;
//        } else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Number") &&
//                !(addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Search") ||
//                addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Text"))){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_ANSWER));
//            setRequestFocusInThread(addQuestionForm.cmbAnswerDataType);
//            return;
//        }
//        //Added for case#3083 - Question Maintenance gives exception if answer length is too long - End
//        //Case 2360 Start 2
//        String ansLength = addQuestionForm.txtAnswerDataLength.getText();
//        
//        if(ansLength !=null && !ansLength.equals(EMPTY_STRING)){
//            int answerLength =  Integer.parseInt(ansLength);
//            if(answerLength >2000){
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ANSWER_LENGTH_EXCEED));
//                setRequestFocusInThread(addQuestionForm.txtAnswerDataLength);
//                return;
//            }
//        }
            //Case 2360 End 2
            
            if(!validate()) return;
        } catch (CoeusUIException ex) {
            ex.printStackTrace();
        }
        // Case# 3524: Add Explanation field to Questions - End
        if (bean == null) {
            bean = new QuestionsMaintainanceBean();
        }
        bean.setQuestionId(new Integer(addQuestionForm.lblQuestionIdValue.getText().trim()));
        // 4272: Maintain history of Questionnaires 
        bean.setVersionNumber(questionVersionNumber);
        bean.setDescription(addQuestionForm.txtArQuestion.getText().trim());
        bean.setMaxAnswers(Integer.parseInt(addQuestionForm.txtMaxAnswer.getText()));
        bean.setValidAnswers(addQuestionForm.cmbValidAnswer.getSelectedItem().toString());
        bean.setAnswerDataType(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString());
        if(addQuestionForm.cmbLookupName.getSelectedItem()!=null) {
            bean.setLookupName(addQuestionForm.cmbLookupName.getSelectedItem().toString());
        }
        if(addQuestionForm.cmbLookupGUI.getSelectedItem()!=null) {
            bean.setLookupGui(addQuestionForm.cmbLookupGUI.getSelectedItem().toString().toUpperCase());
        }
        if(addQuestionForm.txtAnswerDataLength.getText()!=null &&
                addQuestionForm.txtAnswerDataLength.getText().length() > 0 &&
                !addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Date")) {
            bean.setAnswerMaxLength(Integer.parseInt(addQuestionForm.txtAnswerDataLength.getText()));
        } else {
            bean.setAnswerMaxLength(0);
        }
        //Added for question group - start - 8
        if(addQuestionForm.cmbGroup.getSelectedItem() != null){
            String groupTypeCode = ((ComboBoxBean)addQuestionForm.cmbGroup.getSelectedItem()).getCode();
            bean.setGroupTypeCode(Integer.parseInt(groupTypeCode));
        }
        //Added for question group - end - 8     
        // 4272: Maintain history of Questionnaires - Start
        if(addQuestionForm.chkActive.isSelected()){
            bean.setStatus("A");
        } else {
            bean.setStatus("I");
        }
        // 4272: Maintain history of Questionnaires - End
        // Case#: 3524: Add Explanation field to Questions -Start
        if(explanationController != null){
            vecQuestionExplanation = explanationController.getVecExplanation();
        }
        // Case#: 3524: Add Explanation field to Questions -End
        dlgAddQuestionsForm.dispose();
        setOkClicked(true);
        changed = false;
    }
    
    /**
     * Getter for property okClicked.
     * @return Value of property okClicked.
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Setter for property okClicked.
     * @param okClicked New value of property okClicked.
     */
    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
    
    /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final java.awt.Component component) {
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        if(source.equals(addQuestionForm.cmbLookupGUI)) {
            CoeusVector filteredData = null;
            Equals args = null;
            addQuestionForm.cmbLookupName.removeAllItems();
            if(addQuestionForm.cmbLookupGUI.getSelectedItem().toString().equalsIgnoreCase("CodeTable")) {
                args = new Equals("code", CODE_TABLE);
            }else if(addQuestionForm.cmbLookupGUI.getSelectedItem().toString().equals("ValueList")) {
                args = new Equals("code", ARG_TABLE);
            }else {
                args = new Equals("code", OTHER);
            }
            filteredData = cvLookupArgs.filter(args);
            if(filteredData != null && filteredData.size()>0) {
                filteredData.add(0, new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
                ComboBoxBean comboBoxBean;
                for(int indx=0; indx<filteredData.size(); indx++) {
                    comboBoxBean = (ComboBoxBean)filteredData.get(indx);
                    ComboBoxBean cmbBean = new ComboBoxBean(comboBoxBean.getDescription(), comboBoxBean.getDescription());
                    addQuestionForm.cmbLookupName.addItem(cmbBean);
                }
            }
        } else if(source.equals(addQuestionForm.cmbValidAnswer)) {
            if(!addQuestionForm.cmbValidAnswer.getSelectedItem().toString().equals("Search")) {
                lookUpGUIValue = addQuestionForm.cmbLookupGUI.getSelectedItem().toString();
                addQuestionForm.cmbLookupGUI.removeAllItems();
                addQuestionForm.cmbLookupName.removeAllItems();
                addQuestionForm.cmbLookupGUI.setEnabled(false);
                addQuestionForm.cmbLookupName.setEnabled(false);
            } else {
                populateLookupWindowCombo();
                addQuestionForm.cmbLookupGUI.setSelectedItem(lookUpGUIValue);
                if(mode != TypeConstants.DISPLAY_MODE) {
                    addQuestionForm.cmbLookupGUI.setEnabled(true);
                    addQuestionForm.cmbLookupName.setEnabled(true);
                }
            }
            //Added/Modified for case#2606 - Question Enhancement - start
            // 4272: Maintain history of Questionnaires - Start
            if("YN".equalsIgnoreCase(addQuestionForm.cmbValidAnswer.getSelectedItem().toString())){
                addQuestionForm.txtAnswerDataLength.setText("1");
                addQuestionForm.txtAnswerDataLength.setEnabled(false);
                //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window
                setComponentBackGroundToDisabled(addQuestionForm.txtAnswerDataLength);
            } else if ("YNX".equalsIgnoreCase(addQuestionForm.cmbValidAnswer.getSelectedItem().toString())){
                addQuestionForm.txtAnswerDataLength.setText("1");
                addQuestionForm.txtAnswerDataLength.setEnabled(false);
                //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window
                setComponentBackGroundToDisabled(addQuestionForm.txtAnswerDataLength);
            } else if ("Search".equalsIgnoreCase(addQuestionForm.cmbValidAnswer.getSelectedItem().toString())) {
                addQuestionForm.txtAnswerDataLength.setText("");
                addQuestionForm.txtAnswerDataLength.setEnabled(true);
            } else if("Text".equalsIgnoreCase(addQuestionForm.cmbValidAnswer.getSelectedItem().toString())
            && ! "Date".equalsIgnoreCase(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString())){
                addQuestionForm.txtAnswerDataLength.setText("");
                addQuestionForm.txtAnswerDataLength.setEnabled(true);
            }
            // 4272: Maintain history of Questionnaires - End
        } else if(source.equals(addQuestionForm.cmbAnswerDataType)) {
            if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Date")) {
                // 4272: Maintain history of Questionnaires - Start
//                addQuestionForm.txtAnswerDataLength.setText(EMPTY_STRING);
                addQuestionForm.txtAnswerDataLength.setText("10");
                // 4272: Maintain history of Questionnaires - End
                addQuestionForm.txtAnswerDataLength.setEnabled(false);
                //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window
                setComponentBackGroundToDisabled(addQuestionForm.txtAnswerDataLength);
                if(!formLoadFlag){
                    setValidAnswerType("Date");
                }
            }else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("Number")){
                addQuestionForm.txtAnswerDataLength.setEnabled(true);
                if(!formLoadFlag){
                    setValidAnswerType("Number");
                }
                // 4272: Maintain history of Questionnaires - Start
                if(isNewMode){
                    addQuestionForm.txtAnswerDataLength.setText("");
                }
                // 4272: Maintain history of Questionnaires - End
            }else if(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString().equals("String")){
                addQuestionForm.txtAnswerDataLength.setEnabled(true);
                if(!formLoadFlag){
                    setValidAnswerType("String");
                }
                // 4272: Maintain history of Questionnaires - Start
                if(isNewMode){
                    addQuestionForm.txtAnswerDataLength.setText("");
                }
                // 4272: Maintain history of Questionnaires - End
            }else if(mode != TypeConstants.DISPLAY_MODE) {
                addQuestionForm.txtAnswerDataLength.setEnabled(true);
            }
        }
        if(mode == TypeConstants.DISPLAY_MODE) {
            addQuestionForm.txtAnswerDataLength.setEnabled(false);
        }
        //Added/Modified for case#2606 - Question Enhancement - end
        changed = true;
    }
    
    private void getLookupArgsAndModules() throws CoeusException{
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_ARGS_MODULES);
        //requester.setDataObject(bean.getLookupGui());
        AppletServletCommunicator communicator = new AppletServletCommunicator(connect,requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null) {
            if(responder.isSuccessfulResponse()) {
                Hashtable htData = (Hashtable)responder.getDataObject();
                cvLookupArgs = (CoeusVector)htData.get(ComboBoxBean.class);
            }
        }else {
            throw new CoeusException(responder.getMessage());
        }
    }
    
    private void performCancelAction() {
        if(bean!=null){
            String questions = bean.getDescription();
            String maxAnswer = new Integer(bean.getMaxAnswers()).toString();
            if(!addQuestionForm.txtArQuestion.getText().equals(questions)) {
                changed = true;
            } else if(!addQuestionForm.txtMaxAnswer.getText().equals(maxAnswer)) {
                changed = true;
            }
        } else if(addQuestionForm.txtArQuestion.getText().length() > 0 ||
                addQuestionForm.txtMaxAnswer.getText().length() > 0) {
            changed = true;
        }
        if(isChanged() || isNewMode) {
            int selOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(CONFIRM_WINDOW),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selOption == CoeusOptionPane.SELECTION_YES) {
                performOKAction();
//                if(!isChanged()) {
//                    setOkClicked(true);
//                    dlgAddQuestionsForm.dispose();
//                }
                return;
            } else if(selOption == CoeusOptionPane.SELECTION_NO) {
                setOkClicked(false);
                // 4272: Maintain History if Questionnaires - Start
                if(isNewVersion){
                    bean.setVersionNumber(bean.getVersionNumber() -1);
                }
                // 4272: Maintain History if Questionnaires - End
                dlgAddQuestionsForm.dispose();
            } else {
                return;
            }
        }else {
            // 4272: Maintain History if Questionnaires - Start
            if(isNewVersion){
                bean.setVersionNumber(bean.getVersionNumber() -1);
            }
            // 4272: Maintain History if Questionnaires - End
            dlgAddQuestionsForm.dispose();
        }
    }
    
    /**
     * Getter for property changed.
     * @return Value of property changed.
     */
    public boolean isChanged() {
        return changed;
    }
    
    /**
     * Setter for property changed.
     * @param changed New value of property changed.
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }
    
    /**
     * Added for question group - start - 9
     * This method gets the question group and sets
     * the collection to questiongroup combo box
     * @return void
     */
    public void populateQuestionGroups(){
        cvQuestionGroup = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/questionnaireServlet";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('Q');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean res = comm.getResponse();
        if (res != null){
            cvQuestionGroup = (Vector)res.getDataObject();
            if(cvQuestionGroup != null && cvQuestionGroup.size() > 0){
                addQuestionForm.cmbGroup.setModel(new DefaultComboBoxModel(cvQuestionGroup));
            }
        }
    }
    
    /**
     * This method gets the selected index for the given group type code
     * @param theIndex
     * @param selectedIndex
     */
    private int getSelectedIndex(int theIndex){
        ComboBoxBean comboBoxBean = null;
        int selectedIndex = 0;
        for(int index = 0; index < cvQuestionGroup.size(); index++){
            comboBoxBean = (ComboBoxBean)cvQuestionGroup.get(index);
            if(comboBoxBean.getCode().equals(Integer.toString(theIndex))){
                selectedIndex = index;
                break;
            }
        }
        return selectedIndex;
    }
    //Added for question group - end - 9
    
    //Added for case#3083 - Question Maintenance gives exception if answer length is too long - start
    /**
     * This method sets values for valid answer type combobox based on answer type selection
     * @param answerType String
     */
    private void setValidAnswerType(String answerType){
        addQuestionForm.cmbValidAnswer.removeAllItems();
        addQuestionForm.cmbValidAnswer.setEnabled(true);
        if(answerType.equals("Date")){
            ComboBoxBean comboBoxBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Text", "Text");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            addQuestionForm.cmbValidAnswer.setSelectedIndex(1);
            addQuestionForm.cmbValidAnswer.setEnabled(false);
        }else if(answerType.equals("Number")){
            ComboBoxBean comboBoxBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Search", "Search");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Text", "Text");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
        }else if(answerType.equals("String")){
            ComboBoxBean comboBoxBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            comboBoxBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Search", "Search");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("Text", "Text");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("YN", "YN");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
            comboBoxBean = new ComboBoxBean("YNX", "YNX");
            addQuestionForm.cmbValidAnswer.addItem(comboBoxBean);
        }
    }
    
    public void focusLost(FocusEvent focusEvent) {
        Object source = focusEvent.getSource();
        if(source.equals(addQuestionForm.cmbAnswerDataType)) {
            setValidAnswerType(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString());
        }
    }
    
    public void focusGained(FocusEvent focusEvent) {
    }
    //Added for case#3083 - Question Maintenance gives exception if answer length is too long - end
    
    // Case# 3524: Add Explanation field to Questions - Start
    private void performMoreAction() throws CoeusException {
        if(mode != TypeConstants.DISPLAY_MODE){
            try {
                
                if(!validate()) return;
            } catch (CoeusUIException ex) {
                ex.printStackTrace();
            }
        }
        if(explanationController != null ){
            vecQuestionExplanation = explanationController.getVecExplanation();
        }
        String questionId = addQuestionForm.lblQuestionIdValue.getText();
        questionId =  questionId.trim();
        String description = addQuestionForm.txtArQuestion.getText().trim();
        explanationController = new QuestionExplanationController(mdiForm, mode);
        explanationController.setQuestionId( Integer.parseInt(questionId));
        // 4272: Maintain history of Questionnaires 
        explanationController.setQuestionVersionNumber(questionVersionNumber);
        if(mode != TypeConstants.ADD_MODE && vecQuestionExplanation == null){
            // 4272: Maintain history of Questionnaires - Start
//            vecQuestionExplanation = getQuestionExplanation(questionId);
            vecQuestionExplanation = getQuestionExplanation(questionId, questionVersionNumber);
            // 4272: Maintain history of Questionnaires - End
        }

        if(!description.equals(EMPTY_STRING)){
            explanationController.setDescription(description);
        }else{
            explanationController.setDescription(EMPTY_STRING);
        }
        explanationController.setFormData(vecQuestionExplanation);
        explanationController.display();
        
    }
    
    /**
     * This Method is used for getting the Explanation, Regulation,Policy information for a Question
     * @param questionId String
     * @param versionNumber int
     * @return cvQuestionExplanation CoeusVector 
     */
    // 4272: Maintain history of Questionnaire 
//    private CoeusVector getQuestionExplanation(String questionId) {
    private CoeusVector getQuestionExplanation(String questionId, int questionVersionNumber) {
        CoeusVector cvQuestionExplanation = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/questionnaireServlet";
        QuestionExplanationBean questionExplanationBean = new QuestionExplanationBean();
        questionExplanationBean.setQuestionId(new Integer(questionId));
        // 4272: Maintain history of Questionnaire
        questionExplanationBean.setVersionNumber(questionVersionNumber);
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('V');
        requester.setDataObject(questionExplanationBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean res = comm.getResponse();
        if (res != null){
            cvQuestionExplanation = (CoeusVector)res.getDataObject();
        }
        return cvQuestionExplanation ;
    }
    // Case# 3524: Add Explanation field to Questions - End
    
    public CoeusVector getVecQuestionExplanation() {
        return vecQuestionExplanation;
    }
    // 4272: Maintain history of Questionnaire - Start
//    private void setQuestionVersionForExplanation(CoeusVector vecQuestionExplanation) {
//        QuestionExplanationBean questionExplanationBean;
//        if(vecQuestionExplanation != null && vecQuestionExplanation.size() > 0){
//            int explanationCount = vecQuestionExplanation.size();
//            for(int index = 0; index < explanationCount; index ++){
//                questionExplanationBean = (QuestionExplanationBean) vecQuestionExplanation.elementAt(index);
//                questionExplanationBean.setVersionNumber(questionVersionNumber);
//                questionExplanationBean.setAcType("I");
//            }
//        }
//    }

    public void setIsNewVersion(boolean isNewVersion) {
        this.isNewVersion = isNewVersion;
    }

    private void formatFieldsForVersion() {
        
        addQuestionForm.txtArQuestion.setEditable(true);
        addQuestionForm.chkActive.setEnabled(true);
        // COEUSDEV-247: Premium - Questionnaire window, buttons are not displayed correctly - Start
        if( addQuestionForm.cmbAnswerDataType.getSelectedItem() != null &&
                !"Date".equalsIgnoreCase(addQuestionForm.cmbAnswerDataType.getSelectedItem().toString())){
            addQuestionForm.txtAnswerDataLength.setEnabled(true);
        } else {
            addQuestionForm.txtAnswerDataLength.setEnabled(false);
        }
        // COEUSDEV-247: Premium - Questionnaire window, buttons are not displayed correctly - End
        addQuestionForm.txtMaxAnswer.setEnabled(false);
        addQuestionForm.cmbAnswerDataType.setEnabled(false);
        addQuestionForm.cmbValidAnswer.setEnabled(false);
        addQuestionForm.cmbLookupGUI.setEnabled(false);
        addQuestionForm.cmbLookupName.setEnabled(false);
        addQuestionForm.cmbGroup.setEnabled(false);
        // //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window - Start
//        Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
//                getDefaults().get("Panel.background");
//        addQuestionForm.txtMaxAnswer.setBackground(disabledBackground);
        setComponentBackGroundToDisabled(addQuestionForm.txtMaxAnswer);
    }
    // 4272: Maintain history of Questionnaire - End
    private void setComponentBackGroundToDisabled(JComponent component){
        Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
                getDefaults().get("Panel.background");
        component.setBackground(disabledBackground);
    }
    //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window - End
}
