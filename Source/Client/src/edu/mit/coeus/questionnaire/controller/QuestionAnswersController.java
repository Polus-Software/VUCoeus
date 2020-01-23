/*
 * @(#)QuestionAnswersController.java September 29, 2006, 4:43 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionAnswersController.java
 *
 * Created on September 29, 2006, 4:43 PM
 */

/* PMD check performed, and commented unused imports and variables on 08-JULY-2010
 * by Satheesh Kumar
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.gui.LookUpWindowConstants;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusTableWindow;
import edu.mit.coeus.gui.CostElementsLookupWindow;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.questionnaire.bean.QuestionExplanationBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.gui.QuestionAnswersForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.OtherLookupBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * constructor for answer
 * @author tarique
 */
public class QuestionAnswersController extends QuestionnaireController implements LookUpWindowConstants,
        ActionListener{
    private QuestionAnswersForm questionAnswersForm;
    private QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean;
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final char GET_QUESTIONNAIRE_QUESTIONS = 'J';
    private static final char GET_QUESTIONNAIRE_ANSWERS = 'K';
//    private static final char QUESTION_NUMBER = 0;
//    private static final char QUESTION_DESC_COLUMN = 1;
//    private static final char ANSWER_COLUMN = 2;
//    private static final char SEARCH_COLUMN = 3;
//    private static final char SEARCH_DESC = 4;
    private static final char QUESTION_NUMBER = 0;
    private static final char QUESTION_DESC_COLUMN = 1;
    private static final char MORE_COLUMN = 2;
    private static final String EMPTY_STRING = "";
//    private static final int DEFAULT_SIZE = 2000;
    private boolean lookupAvailable[];
    private CoeusVector cvData;
    private QuestionAnswersEditor questionAnswersEditor;
    private QuestionAnswersRenderer questionAnswersRenderer;
   // private SearchButtonCellEditor searchButtonCellEditor;
   // private SearchButtonRenderer searchButtonRenderer;
    private QuestionAnswersTableModel questionAnswersTableModel;
    private CoeusVector cvAnswersData;
//    private  MultiLineCellRenderer multiLineCellRenderer;
    //Code added for coeus4.3 enhancements - starts
    private String moduleItemKey;
    private static final char GET_PREVIOUS_QUESTIONS = 'P';
    private static final char NEXT = 'O';
    private static final char SAVE = 'R';
    private static final char RESTART = 'S';
    private static final char MODIFY = 'T';
    private CoeusMessageResources coeusMessageResources;
    private QuestionnaireMenuTableRenderer questionnaireMenuTableRenderer;
    public QuestionnaireMenusTableModel questionnaireMenusTableModel;    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
        getDefaults().get("Panel.background");
    private CoeusVector cvTableData;
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET;
    private static final char GET_QUESTIONS_MODE = 'I';   
    private String page = EMPTY_STRING;
    private char originalFunctionType;
    private QuestionnaireAnswersBaseController questionnaireAnswersBaseController;
    //Added for Case#2946 Questionnaire Printing enhancement
    private static final String connect = CoeusGuiConstants.CONNECTION_URL+ "/ReportConfigServlet";
    //Code added for coeus4.3 enhancements - ends
    private static final String ERRKEY_PRINT_TEMPLATE    = "questionnaire_exceptionCode.1019";//Case 4287
    // 4272: Maintain history of Questionnaires - Start
    int answeredQuestionnaireVersion;
    int latestQuestionnaireVersion;
    private static final char GET_QUESTIONNAIRE_VERSION_DETAILS = 'Z';
    // 4272: Maintain history of Questionnaires - End
    
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    private boolean isModulelSubmission = false;
    //COEUSDEV-86 : End
    // Added for IACUC Questionnaire implementation - Start
    private static final String ERROR_IRB_PROTOCOL_QUESTIONNAIRE_NOT_COMPLETED = "questionnaire_exceptionCode.1015";
    private static final String ERROR_IACUC_PROTOCOL_QUESTIONNAIRE_NOT_COMPLETED = "questionnaire_exceptionCode.1015";
    private static final String ERROR_PROP_DEV_QUESTIONNAIRE_NOT_COMPLETED = "questionnaire_exceptionCode.1016";
    private static final String WARNING_QNR_NOT_ANSWERED = "questionnaireNotAnswered_exceptionCode.1000";
    // Added for IACUC Questionnaire implementation - End
    // Added for CoeusQA2313: Completion of Questionnaire for Submission
    private String originalModuleItemKey;
    private String originalModuleItemKeySequence;
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    private static final char SAVE_AND_COMPLETE = 'z';
    private final String ANSWER_CONDITIONAL_BRANCH_QUESTION = "questionnaire_exceptionCode.1020";
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
    
    
    /** Creates a new instance of QuestionAnswersController */
    public QuestionAnswersController(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
        registerComponents();
        //Code added for coeus4.3 enhancements - starts
        setColumnData();
        coeusMessageResources = CoeusMessageResources.getInstance();
        //Code added for coeus4.3 enhancements - ends
    }
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    /* Creates a new instance of QuestionAnswersController for module submission
     */
    public QuestionAnswersController(String moduleItemKey,boolean isProtocolSubmission ){
        this.isModulelSubmission = isProtocolSubmission;
        this.moduleItemKey = moduleItemKey;
        registerComponents();
        setColumnData();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    //COEUSDEV-86 : End
    /**
     * method to display dialog
     */    
    public void display() {
    }
    
    /**
     * method to format components
     */    
    public void formatFields() {
        if(questionAnswersForm.tblQuestions.getRowCount() > 0) {
                questionAnswersForm.tblQuestions.setRowSelectionInterval(0,0);
        }
        
    }
    
    /**
     * method to get gui form object
     * @return form object
     */    
    public java.awt.Component getControlledUI() {
        return questionAnswersForm;
    }
    
    /**
     * Method to get the form data
     * @return form data object
     */    
    public Object getFormData() {
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        //The actual form data is the table data
//        return cvData;
        return cvTableData;
        //Added for COEUSDEV-86 : Questionnaire for a Submission - End
    }
    
    /**
     * method to register components
     */    
    public void registerComponents() {
        questionAnswersForm = new QuestionAnswersForm();
        questionAnswersTableModel = new QuestionAnswersTableModel();
        questionAnswersEditor = new QuestionAnswersEditor();
        questionAnswersRenderer = new QuestionAnswersRenderer();
       // searchButtonCellEditor = new SearchButtonCellEditor();
       // searchButtonRenderer = new SearchButtonRenderer();
      //  multiLineCellRenderer = new MultiLineCellRenderer();
      //  multiLineCellRenderer.setDocument(new LimitedPlainDocument(2000));
        questionAnswersForm.tblQuestions.setModel(questionAnswersTableModel);
        //Code added for coeus4.3 enhancements - starts
        questionAnswersForm.btnGoBack.addActionListener(this);
        questionAnswersForm.btnSaveAndProceed.addActionListener(this);
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
        questionAnswersForm.btnSaveAndComplete.addActionListener(this);
        // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
        if(!isModulelSubmission){
            questionAnswersForm.btnModify.setVisible(false);
            questionAnswersForm.btnStartOver.setVisible(false);
            questionAnswersForm.btnClose.setVisible(false);
        }
        //COEUSDEV-86 : End
        questionnaireMenuTableRenderer = new QuestionnaireMenuTableRenderer();
        questionnaireMenusTableModel = new QuestionnaireMenusTableModel();
        questionAnswersForm.splitPaneQuestionnaireAnswers.setDividerLocation(219);
        questionAnswersForm.splitPaneQuestionnaireAnswers.setDividerSize(1);
        questionAnswersForm.splitPaneQuestionnaireAnswers.setEnabled(false);
        //Modified with COEUSDEV230: Answered questionnaire says it is not Answered in Approval in Progress Proposal
        questionAnswersForm.tblMenus.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN
                            || e.getKeyCode() == KeyEvent.VK_TAB){
                    loadQuestions();
                }
            }
        });
        questionAnswersForm.tblMenus.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                //Moved the code to loadQuestions method.
                loadQuestions();
            }
        });
        //COEUSDEV:230 End
        questionAnswersForm.tblMenus.setBackground(disabledBackground);
        questionAnswersForm.tblMenus.setShowHorizontalLines(false);
        questionAnswersForm.tblMenus.setShowVerticalLines(false);
        questionAnswersForm.tblMenus.setModel(questionnaireMenusTableModel);
        questionAnswersForm.btnGoBack.setEnabled(false);
        questionAnswersForm.btnSaveAndProceed.setEnabled(false);
        // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
        questionAnswersForm.btnSaveAndComplete.setEnabled(false);
        // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
        //Code added for coeus4.3 enhancements - ends
        setTableEditors();
        
    }
    private void setTableEditors() {
        JTableHeader tableHeader = questionAnswersForm.tblQuestions.getTableHeader();
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));
       // tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(true);
        questionAnswersForm.tblQuestions.getTableHeader().setReorderingAllowed(false);
        questionAnswersForm.tblQuestions.setSelectionMode(
                                DefaultListSelectionModel.SINGLE_SELECTION);
        // row selection true , needs selected row in color
        questionAnswersForm.tblQuestions.setRowSelectionAllowed(true);
        questionAnswersForm.tblQuestions.setCellSelectionEnabled( false );
        questionAnswersForm.tblQuestions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        questionAnswersForm.tblQuestions.setOpaque(true);
        int size[] = {50,675, 25};
        TableColumn column;
        
        for(int index=0; index<size.length; index++){
            column = questionAnswersForm.tblQuestions.getColumnModel().getColumn(index);
            column.setResizable(true);
            column.setPreferredWidth(size[index]);
            switch(index) {
                case QUESTION_NUMBER:
//                    column.setMinWidth(size[index]);
                    column.setMinWidth(50);
                    column.setCellRenderer(questionAnswersRenderer);
                    break;
                case QUESTION_DESC_COLUMN :
                    column.setMinWidth(100);   
                    // COEUSDEV-247: Premium - Questionnaire window, buttons are not displayed correctly
                    column.setMaxWidth(550);   
                    column.setCellRenderer(questionAnswersRenderer);
                    column.setCellEditor(questionAnswersEditor);
                    break;
                case MORE_COLUMN:
                   // COEUSDEV-247: Premium - Questionnaire window, buttons are not displayed correctly
                   // column.setHeaderRenderer(new EmptyHeaderRenderer());
                    column.setCellRenderer(questionAnswersRenderer);
                    column.setCellEditor(questionAnswersEditor);
            }
        }//COEUSDEV:187-Questionnaire question presentation text blocks limited to 1 line in Premium
//        questionAnswersForm.tblQuestions.setRowHeight(35);
        questionAnswersForm.tblQuestions.setRowHeight(30);
        //COEUSDEV:187-End
        questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
    }
    
    /**
     * method to save the form data
     * @throws CoeusException if any exception occurs
     */    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * method to set form data
     * @param data data object
     * @throws CoeusException if any exception occurs
     */    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        // Added for CoeusQA2313: Completion of Questionnaire for Submission - Start
        questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)data;
        originalModuleItemKey = questionnaireAnswerHeaderBean.getModuleItemKey();
        originalModuleItemKeySequence = questionnaireAnswerHeaderBean.getModuleSubItemKey();
        // CoeusQA2313: Completion of Questionnaire for Submission - End
        //Code commented and added for coeus4.3 enhancements - starts
//        CoeusVector cvQuestionsData = (CoeusVector)getServerData(GET_QUESTIONNAIRE_QUESTIONS);
////        cvAnswersData = (CoeusVector)getServerData(GET_QUESTIONNAIRE_ANSWERS);
//        if(cvQuestionsData != null && !cvQuestionsData.isEmpty()) {
////            if(cvAnswersData != null && !cvAnswersData.isEmpty()) {
////                cvData = (CoeusVector)getQuestionsData(cvQuestionsData, cvAnswersData);
////            }else {
//                cvData = (CoeusVector)getQuestionsData(cvQuestionsData);
////            }
////            
//        }
//        if(cvData != null) {
//            try{
//                cvData.sort("questionNumber");
//            }catch(Exception ce) {
//                ce.printStackTrace();
//            }
//        }
////        questionAnswersTableModel.setData(cvData);
////        questionAnswersTableModel.fireTableDataChanged();
        originalFunctionType = getFunctionType();
        cvTableData = getMenuDataFromServer();
        questionnaireMenusTableModel.setData(cvTableData);
        questionnaireMenusTableModel.fireTableDataChanged();
        //Code commented and added for coeus4.3 enhancements - ends
    }
    
    // Commented for PMD Check
//    private List getQuestionsData(CoeusVector cvQuestions, CoeusVector cvAnswers) {
//        QuestionnaireQuestionsBean bean = null;
//        lookupAvailable = new boolean[cvQuestions.size()];
//        for(int index = 0; index < cvQuestions.size(); index ++) {
//            bean = (QuestionnaireQuestionsBean)cvQuestions.get(index);
//            for(int jIndex = 0; jIndex < cvAnswers.size(); jIndex ++) {
//                QuestionAnswerBean ansBean = (QuestionAnswerBean)cvAnswers.get(jIndex);
//                if(bean.getQuestionnaireId() == ansBean.getQuestionnaireId()) {
//                    if((bean.getQuestionId().intValue() == ansBean.getQuestionId().intValue())
//                        &&(bean.getQuestionNumber().intValue() == ansBean.getQuestionNumber())){
//                            bean.setAnswer(ansBean.getAnswer());
//                            bean.setAnswerNumber(ansBean.getAnswerNumber());
//                            bean.setAwQuestionnaireCompletionId(ansBean.getQuestionnaireCompletionId());
//                            bean.setSearchName(ansBean.getDescription());
//                            bean.setAcType(TypeConstants.UPDATE_RECORD);
//                            bean.setUpdateTimestamp(ansBean.getUpdateTimestamp());
//                            bean.setUpdateUser(ansBean.getUpdateUser());    
//                    }
//                }
//            }
//            if(bean.getLookUpGui() != null && !bean.getLookUpGui().equals(EMPTY_STRING)){
//                lookupAvailable[index] = true;
//            }else {
//                lookupAvailable[index] = false;
//            }
//        }
//        return cvQuestions;
//    }
    
     private List getQuestionsData(CoeusVector cvQuestions) {
        QuestionnaireQuestionsBean bean = null;
        lookupAvailable = new boolean[cvQuestions.size()];
        for(int index = 0; index < cvQuestions.size(); index ++) {
            bean = (QuestionnaireQuestionsBean)cvQuestions.get(index);
            if(bean.getLookUpGui() != null && !bean.getLookUpGui().equals(EMPTY_STRING)){
                lookupAvailable[index] = true;
            }else {
                lookupAvailable[index] = false;
            }
        }
        return cvQuestions;
    }
    private List getServerData(char statusType) throws CoeusException {
        CoeusVector cvMainData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        if(statusType == GET_QUESTIONNAIRE_QUESTIONS) {
            request.setFunctionType(GET_QUESTIONNAIRE_QUESTIONS);
        }else if(statusType == GET_QUESTIONNAIRE_ANSWERS) {
            request.setFunctionType(GET_QUESTIONNAIRE_ANSWERS);
        }
        request.setDataObject(questionnaireAnswerHeaderBean);
        request.setRequestedForm(new Character(getFunctionType()).toString());
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET;
        request.setId(moduleItemKey);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                cvMainData = (CoeusVector)response.getDataObject();
                //Code added for coeus4.3 enhancements - starts
                page = response.getId();
                page = (page == null)? EMPTY_STRING : page;
                if(response.getMessage() != null 
                        && response.getMessage().equals("COMPLETED")){
                    enableDisableButtons(false);
                    
                    if(getFunctionType() != TypeConstants.DISPLAY_MODE ){
                        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
                        //If questionnaire for submission then modify button in questionnaireAnswersForm is enabled
                        //Modify button and menuitem are diabled in questionnaireanswerBaseWindowContoller
//                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(true);
//                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(true);
                        if(isModulelSubmission){
                            questionAnswersForm.btnModify.setEnabled(true);
                        }else{
                            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(true);
                            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(true);
                        }
                        //COEUSDEV-86 : End
                        setFunctionType(TypeConstants.DISPLAY_MODE);
                    }
                } else {
                   //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                    questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(false);
//                    questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(false);
                    if(isModulelSubmission){
                        questionAnswersForm.btnModify.setEnabled(false);
                    }else{
                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(false);
                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(false);
                    }
                       
                    //COEUSDEV-86 : End
                }
                //Code added for coeus4.3 enhancements - ends
            }else{
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvMainData;
        
    }
    /**
     * method for validate data
     * @throws CoeusUIException if any exception occurs
     * @return true if data is valid else false
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        questionAnswersEditor.stopCellEditing();
        //Code added for coeus4.3 enhancements
        int questionNumber = 0;
        LinkedHashMap lhmAnsweredQuestions = new LinkedHashMap();
        LinkedHashMap lhmQuestions = new LinkedHashMap();
        if(cvData != null && !cvData.isEmpty()) {
            for(int index = 0; index < cvData.size(); index ++) {
                QuestionnaireQuestionsBean quesBean
                    = (QuestionnaireQuestionsBean)cvData.get(index);
                //Condition checking modified for coeus4.3 enhancements
//                if(quesBean.getAnswer() != null) {
                String key = ""+questionNumber;
                if(quesBean.getAnswerNumber() == 0){
                    questionNumber++;
                    key = ""+questionNumber;
                    if(lhmQuestions.get(key) == null){
                        lhmQuestions.put(key, quesBean.getDescription());
                    }
                }
                if(quesBean.getAnswer() != null && !quesBean.getAnswer().equals(EMPTY_STRING)) {
                    lhmAnsweredQuestions.put(key, "Answered");
                    if(quesBean.getAcType() != null
                    && quesBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        //checking for empty also after user make his answer empty
                        if(quesBean.getAnswerDataType().equalsIgnoreCase("NUMBER")) {
                            try{
                                Integer.parseInt(quesBean.getAnswer());
                            }catch(NumberFormatException nfe){
                                //Code modified for coeus4.3 enhancements
//                                CoeusOptionPane.showErrorDialog("Answer for Question "
//                                    +quesBean.getQuestionNumber()+" should be numeric.");
                                CoeusOptionPane.showErrorDialog("Answer for Question "+questionNumber+"\n"
                                        +quesBean.getDescription()+"\n"
                                        +"should be numeric.");                                
                                return false;
                            }
                        }else if(quesBean.getAnswerDataType().equalsIgnoreCase("DATE")){
                            DateUtils dateUtils = new DateUtils();
                            String resultDate = dateUtils.formatDate(quesBean.getAnswer(),"/-,:","MM/dd/yyyy");
                            //Modified for COEUSDEV-226 : Questionnaire - question answer type Date not validating as expected - Start 
                            //Forcing user to enter 10 char's valid date
//                            if(resultDate == null){
                            if(resultDate == null || (quesBean.getAnswer() != null && quesBean.getAnswer().trim().length() < 10 )){//COEUSDEV-226 : END
                                //Code modified for coeus4.3 enhancements
//                                CoeusOptionPane.showErrorDialog("Answer for Question "+quesBean.getQuestionNumber() + " should be valid date. ");
                                CoeusOptionPane.showErrorDialog("Answer for Question "+questionNumber+"\n"
                                        +quesBean.getDescription()+"\n"
                                        +"should be a valid date.");                                  
                                questionAnswersForm.tblQuestions.setRowSelectionInterval(cvData.size()-1,cvData.size()-1);
                                questionAnswersForm.tblQuestions.setColumnSelectionInterval(2, 2);
                                questionAnswersForm.tblQuestions.scrollRectToVisible(
                                questionAnswersForm.tblQuestions.getCellRect(
                                        cvData.size()-1 ,2, true));
                                
                                questionAnswersForm.tblQuestions.editCellAt(cvData.size()-1, 2);
                                return false;
                            }else{
                                // Added to update the formated date to the UI
                                quesBean.setAnswer(resultDate);
                                questionAnswersForm.tblQuestions.getModel().setValueAt(quesBean,cvData.size()-1,2);
                                questionAnswersForm.tblQuestions.repaint();
                             
                            }
                        }
                        
                    }else {
                        if(quesBean.getAnswer().length() > 0) {
                            if(quesBean.getAnswerDataType().equalsIgnoreCase("NUMBER")) {
                                try{
                                    Integer.parseInt(quesBean.getAnswer());
                                }catch(NumberFormatException nfe){
                                    //Code modified for coeus4.3 enhancements
//                                    CoeusOptionPane.showErrorDialog("Answer for Question "
//                                    +quesBean.getQuestionNumber()+" should be numeric.");
                                    CoeusOptionPane.showErrorDialog("Answer for Question "+key+"\n"
                                            +quesBean.getDescription()+"\n"
                                            +"should be numeric.");                                      
                                    return false;
                                }
                            }else if(quesBean.getAnswerDataType().equalsIgnoreCase("DATE")){
                                DateUtils dateUtils = new DateUtils();
                                String resultDate = dateUtils.formatDate(quesBean.getAnswer(),"/-,:","MM/dd/yyyy");
                                //Modified for COEUSDEV-226 : Questionnaire - question answer type Date not validating as expected - Start
                                //Forcing user to enter 10 char's valid date
//                                if(resultDate == null){
                                if(resultDate == null || (quesBean.getAnswer() != null && quesBean.getAnswer().trim().length() < 10 )){//COEUSDEV-226 : END
                                    //Code modified for coeus4.3 enhancements
//                                    CoeusOptionPane.showErrorDialog("Answer for Question "+quesBean.getQuestionNumber() + " should be valid date. ");
                                    CoeusOptionPane.showErrorDialog("Answer for Question "+key+"\n"
                                            +quesBean.getDescription()+"\n"
                                            +"should be a valid date.");                                     
                                    questionAnswersForm.tblQuestions.setRowSelectionInterval(index,index);
                                    questionAnswersForm.tblQuestions.setColumnSelectionInterval(2, 2);
                                    questionAnswersForm.tblQuestions.scrollRectToVisible(
                                    questionAnswersForm.tblQuestions.getCellRect(
                                    index ,2, true));
                                    return false;
                                }else{
                                    // Added to update the formated date to the UI
                                    quesBean.setAnswer(resultDate);
                                    questionAnswersForm.tblQuestions.getModel().setValueAt(quesBean,index,2);
                                    questionAnswersForm.tblQuestions.repaint();
                                    
                                }
                            }//end if
                        }//end if
                    }//end else
                }//end if
                //Code added for coeus4.3 enhancements - starts
                else {
                    if(lhmAnsweredQuestions.get(key) == null){
                        lhmAnsweredQuestions.put(key, null);
                    }
                }
            }
            if(lhmAnsweredQuestions.size() > 0){
                java.util.Set keySet = lhmAnsweredQuestions.keySet();
                Object[] objQuestions = keySet.toArray();
                for(int index = 0; index < objQuestions.length; index++){
                    if(lhmAnsweredQuestions.get(objQuestions[index]) == null){
                        //Code modified for coeus4.3 enhancements
//                        CoeusOptionPane.showErrorDialog("No answer found for Question "
//                                +objQuestions[index]);
                        CoeusOptionPane.showErrorDialog("Answer for Question "+objQuestions[index]+"\n"
                                +lhmQuestions.get(objQuestions[index])+"\n"
                                +"should not be empty.");                         
                        return false;                         
                    }
                }
            }
            //Code added for coeus4.3 enhancements - ends
        }
        return true;
    }
    /**
     * method to check data is modified or not.
     * @return true if data is modified
     */    
    public boolean checkDataModified(){
        questionAnswersEditor.stopCellEditing();
        //first compare with answer
        //Condition added for coeus4.3 enhancements
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            return false;
        }
        boolean isdataChanged = false;
        if(cvAnswersData != null && !cvAnswersData.isEmpty()) {
            QuestionnaireQuestionsBean bean = null;
            for(int index = 0; index < cvData.size(); index ++) {
                bean = (QuestionnaireQuestionsBean)cvData.get(index);
                boolean isModified = false;
                for(int jIndex = 0; jIndex < cvAnswersData.size(); jIndex ++) {
                    //Code modified for coeus4.3 enhancements - starts
//                    QuestionAnswerBean ansBean = (QuestionAnswerBean)cvAnswersData.get(jIndex);
                    QuestionnaireQuestionsBean ansBean = (QuestionnaireQuestionsBean)cvAnswersData.get(jIndex);
                    if(bean.getQuestionnaireId() == ansBean.getQuestionnaireId()) {
                        if((bean.getQuestionId().intValue() == ansBean.getQuestionId().intValue())
                        &&(bean.getQuestionNumber().intValue() == ansBean.getQuestionNumber().intValue())
                        &&(bean.getAnswerNumber() == ansBean.getAnswerNumber())){
                            if(bean.getAnswerDataType().equalsIgnoreCase("Date")
                            || bean.getAnswerDataType().equalsIgnoreCase("String")) {
                                if(bean.getAnswer() != null && !bean.getAnswer().equals(ansBean.getAnswer())) {
                                    isModified = true;
                                    break;
                                }
                            }else {
                                if(bean.getAnswer() != null && !bean.getAnswer().equals(ansBean.getAnswer())) {
                                    isModified = true;
                                    break;
                                }
                            }
                        }
                    }
                    //Code modified for coeus4.3 enhancements - ends
                  }//end for
                if(isModified) {
                        isdataChanged = true;
                        break;
                 }
            }//end for
        }//end if
//        if(!isdataChanged) {
//            //new questions
//            if(cvData != null && !cvData.isEmpty() ){
//                for(int index = 0; index < cvData.size() ; index ++) {
//                    QuestionnaireQuestionsBean quesBean
//                    = (QuestionnaireQuestionsBean)cvData.get(index);
//                    if(quesBean.getAcType() == null) {
//                        if(quesBean.getAnswer() != null && quesBean.getAnswer().length() > 0 ){
//                            isdataChanged = true;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
        return isdataChanged;
    }
    /**
     * Helper method to display the Lookup Window when the Lookup button is Pressed
     */
    private void showLookupSearchWindow(OtherLookupBean otherLookupBean,
                String lookUpWindow, Vector vecLookupdata, String columnValue, 
                                                                int selectedRow){
            ComboBoxBean cBean = null;
            if(otherLookupBean != null){
                if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)) {
                    CostElementsLookupWindow costElementsLookupWindow =
                                        new CostElementsLookupWindow(otherLookupBean);
                } else {
                    CoeusTableWindow coeusTableWindow =
                                        new CoeusTableWindow(otherLookupBean);                    
                }
            }
            int selRow = otherLookupBean.getSelectedInd();
            if(vecLookupdata != null && selRow >= 0){
                cBean = (ComboBoxBean)vecLookupdata.elementAt(selRow);
                if(cBean != null){
                    String code = (String)cBean.getCode();
                    String desc = (String)cBean.getDescription();
                    if(!code.equalsIgnoreCase(columnValue)){
                        //saveRequired = true;
                        QuestionnaireQuestionsBean bean 
                            = (QuestionnaireQuestionsBean)cvData.get(selectedRow);
                        bean.setAnswer(code.trim());
                        bean.setSearchName(desc);
                        questionAnswersTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
                    }
            }
        }
    }
        // Helper method which is used to get the lookup data from the database based on the lookup argument
        private Vector getLookupValuesFromDB(String lookUpArgument, String lookUpWindow){
            
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
            RequesterBean request = new RequesterBean();
            Vector serverLookUpDataObject = null;
            
            if( lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW) ){
                
                request.setDataObject("DW_GET_ARG_VALUE_DESC_TBL_NEW");
            }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                
                request.setDataObject("DW_GET_COST_ELEMENTS");
            }else{
                
                request.setDataObject("DW_GET_ARG_CODE_TBL_NEW");
            }
            request.setId(lookUpArgument);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    serverLookUpDataObject = (Vector)response.getDataObject();
                }
            }
            return serverLookUpDataObject;
        }

        //Helper Method to show the Search Window 
    
        private void showSearchWindow(String searchType, String colValue, int selectedRow){
            try{

                CoeusSearch coeusSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), searchType, 1);
                coeusSearch.showSearchWindow();
                HashMap hmResult = coeusSearch.getSelectedRow();
                String pID = null;
                String name = null;
                String lastName =null;
                String firstName= null;
                String middleName= null;
                if(hmResult!=null){
                    // No value displayed for Organization search in qiestionnaire window
                    if("ORGANIZATIONSEARCH".equalsIgnoreCase(searchType)){
                        pID = Utils.convertNull(hmResult.get( "ORGANIZATION_ID" ));
                        name = Utils.convertNull(hmResult.get( "ORGANIZATION_NAME" ));
                    }else if("W_Rolodex_Select".equalsIgnoreCase(searchType)){
                        pID = Utils.convertNull(hmResult.get( "ROLODEX_ID" ));
                        lastName= Utils.convertNull(hmResult.get( "LAST_NAME" ));
                        firstName= Utils.convertNull(hmResult.get( "FIRST_NAME" ));
                        middleName= Utils.convertNull(hmResult.get( "MIDDLE_NAME" ));
                        if(lastName != null && lastName.length()>0){
                            name = lastName+", ";
                        }
                        if(firstName != null && firstName.length()>0){
                            name+= firstName+" " ;
                        }
                        if(middleName != null && middleName.length()>0){
                            name +=middleName;
                        }
                    }else {
                        pID = Utils.convertNull(hmResult.get( "ID" ));
                        name = Utils.convertNull(hmResult.get( "NAME" ));
                    }
                    if((pID!= null) && (!pID.equalsIgnoreCase(colValue))){
                        //saveRequired = true;
                       QuestionnaireQuestionsBean bean 
                            = (QuestionnaireQuestionsBean)cvData.get(selectedRow);
                        bean.setAnswer(pID.trim());
                        bean.setSearchName(name);
                    }
                    questionAnswersTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
                    
                }
            }catch(Exception exp){
              exp.printStackTrace();
            }
        }

    /**
     * Code added for coeus4.3 enhancements
     * For Go Back and Save and Proceed operations
     * @param btnAction 
     */
        public void actionPerformed(ActionEvent btnAction) {
            CoeusVector cvQuestionsData = null;
            CoeusVector cvMenuData = null;
            try {
                Object sourceAction = btnAction.getSource();
                if(sourceAction.equals(questionAnswersForm.btnGoBack)){
                    CoeusVector cvQuesData = saveAndGetQuestions(GET_PREVIOUS_QUESTIONS);
                    cvQuestionsData = (CoeusVector) cvQuesData.get(0);
                } else if(sourceAction.equals(questionAnswersForm.btnSaveAndProceed)){
                    if(!validate()){
                        return;
                    }
                    CoeusVector cvQuesData = saveAndGetQuestions(NEXT);

                    cvQuestionsData = (CoeusVector) cvQuesData.get(0);
                    cvTableData = (CoeusVector) cvQuesData.get(1);
                } else if(sourceAction.equals(questionAnswersForm.btnSaveAndComplete)){
                    if(!validate()){
                        return;
                    }

                    CoeusVector cvQuesData = saveAndGetQuestions(SAVE_AND_COMPLETE);

                    cvQuestionsData = (CoeusVector) cvQuesData.get(0);
                    cvTableData = (CoeusVector) cvQuesData.get(1);
                }
                
                if(cvQuestionsData != null && !cvQuestionsData.isEmpty()) {
                    cvData = (CoeusVector)getQuestionsData(cvQuestionsData);
                } else {
                    cvData = new CoeusVector();
                }
                questionAnswersTableModel = new QuestionAnswersTableModel();
                questionAnswersEditor = new QuestionAnswersEditor();
                questionAnswersRenderer = new QuestionAnswersRenderer();
                questionAnswersForm.tblQuestions.setModel(questionAnswersTableModel);
                setTableEditors();
                if(page.equals("1")){
                    questionAnswersForm.btnGoBack.setEnabled(false);
                } else {
                    questionAnswersForm.btnGoBack.setEnabled(true);
                }
                int selectedRow = questionAnswersForm.tblMenus.getSelectedRow();
                questionnaireMenusTableModel.setData(cvTableData);
                questionnaireMenusTableModel.fireTableDataChanged();
                if(selectedRow != -1 && cvTableData != null && selectedRow < cvTableData.size()){
                    questionAnswersForm.tblMenus.setRowSelectionInterval(selectedRow, selectedRow);
                }
                cvAnswersData = (CoeusVector) ObjectCloner.deepCopy(cvData);
                questionAnswersTableModel.setData(cvData);
                questionAnswersTableModel.fireTableDataChanged();              
            } catch(Exception e) {
                CoeusOptionPane.showWarningDialog(e.getMessage());
            }
        }
      /**This is an inner class which behaves like a model for the JTable */
    
    public class QuestionAnswersTableModel extends AbstractTableModel{
        //Code modified for coeus4.3 enhancements
        // COEUSDEV-247: Premium - Questionnaire window, buttons are not displayed correctly - Start
//        private String []colNames={" ","Question", "."};
        private String []colNames={" ","Question", " "};
        // COEUSDEV-247: Premium - Questionnaire window, buttons are not displayed correctly - end
        private Class colClass[]={Integer.class,String.class, Boolean.class};
        private CoeusVector cvData ;
        /**
         * method to make cell editable
         * @param row for which row
         * @param col which column
         * @return true if specify row and column is editable else return false
         */        
        public boolean isCellEditable(int row, int col){
            if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                if( col == MORE_COLUMN) {
                    return true;
                }
                return false;
            }else {
                if(col == QUESTION_DESC_COLUMN || col == MORE_COLUMN) {
                    return true;
                }
                return false;
            }
            
        }
        /**
         * method to get column name
         * @param col for which column
         * @return return the name of the column
         */        
        public String getColumnName(int col){
            return colNames[col];
        }
        /**
         * method to return the column count
         * @return return the total number of column
         */        
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * method to return total row in table
         * @return total row in table
         */        
        public int getRowCount() {
            if(cvData == null||  cvData.size() == 0 ) {
                return 0;
            }
            return cvData.size();
        }
        /**
         * method to set data in table
         * @param cvData vector contain data to set in table.
         */        
        public void setData(CoeusVector cvData){
            this.cvData = cvData;
        }
        
        /**
         * method to get the column class
         * @param col for which column
         * @return column class
         */        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * method to get the value at specified row and column
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object contain value at specified row and column
         */        
        public Object getValueAt(int rowIndex, int columnIndex) {
            QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(rowIndex);
            switch(columnIndex){
                case QUESTION_NUMBER :
                    return bean.getQuestionNumber().toString();
                case QUESTION_DESC_COLUMN:
                    if(bean.getAnswer() == null) {
                        return EMPTY_STRING;
                    }
                   return bean.getAnswer();
//                case QUESTION_DESC_COLUMN:
//                    return bean.getDescription();
//                case ANSWER_COLUMN :
//                    if(bean.getAnswer() == null) {
//                        return EMPTY_STRING;
//                    }
//                    return bean.getAnswer();
//                case SEARCH_DESC :
//                    if(bean.getSearchName() == null) {
//                        return EMPTY_STRING;
//                    }
//                    return bean.getSearchName();
                     
            }
            return EMPTY_STRING;
        }
        
        /**
         * method to set value to which row and column.
         * @param value value to be set
         * @param row which row
         * @param col which col
         */        
        public void setValueAt(Object value,int row,int col){
            if(cvData == null || cvData.size()==0){
                return;
            }
            QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(row);
            switch(col){
              //  case ANSWER_COLUMN:
                case QUESTION_DESC_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)) {
                        bean.setAnswer(EMPTY_STRING);
                        
                    }
                    else{                                         
                        
                        if(bean.getAnswer().equals(value.toString().trim())) {
                          return ;
                        }
                        bean.setAnswer(value.toString().trim());
                        fireTableDataChanged();
                    }
                    break;
            }
        }
    }
     /*** Table editor for AwardBudgetOverheadTable  */
     class QuestionAnswersEditor extends DefaultCellEditor {
        
        private int row;
        private JLabel lblQuestion, lblSearchName;
        private CoeusTextField txtCustomElement;
        private JTextArea txtArAnswer,txtQuesDesc;
        private JScrollPane scrpnAnswer;
        private JButton btnSearch, btnMore;
        public JPanel pnl;
        public JPanel pnlText, pnlBtn;
        public JPanel pnlMain;
        public ButtonGroup group1;
        public JRadioButton btnOne;
        public JRadioButton btnTwo;
        public JRadioButton btnThree;
        public JRadioButton btnFour;
        private int column;
        private int dataLength;
        private GridBagConstraints gridBagConstraints;
        private ImageIcon imgIcnDesc, imgMore;
        private int selRow;
        private int questionNumber = 1;
        private HashMap hmQuestionNumber = new HashMap();
        QuestionAnswersEditor() {
           super(new JComboBox());
            pnlText = new JPanel();
            pnlText.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnlText.setLayout(new java.awt.GridBagLayout());
            pnlText.setMinimumSize(new Dimension(600,50));
            pnlText.setPreferredSize(new Dimension(600,50));
            
            pnlMain = new JPanel();
            pnlMain.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnlMain.setLayout(new java.awt.GridBagLayout());

            lblQuestion=new JLabel("");
            lblQuestion.setOpaque(false);
            
            lblSearchName = new JLabel("");
            lblSearchName.setOpaque(false);
            //COEUSDEV:187-Questionnaire question presentation text blocks limited to 1 line in Premium
//            lblQuesDesc = new JLabel("");
//            lblQuesDesc.setOpaque(false);
           // lblQuesDesc.setMinimumSize(new Dimension(600, 20));
          //  lblQuesDesc.setPreferredSize(new Dimension(600, 20));
          //  lblQuesDesc.setMaximumSize(new Dimension(600, 20));
            txtQuesDesc = new JTextArea();
            txtQuesDesc.setOpaque(false);
            txtQuesDesc.setLineWrap(true);
            txtQuesDesc.setWrapStyleWord(true);
            txtQuesDesc.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            txtQuesDesc.setFont(CoeusFontFactory.getNormalFont());
            txtQuesDesc.setMargin(new Insets(2,2,5,5));
            txtQuesDesc.setEditable(false);
            //COEUSDEV187 End
            imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON));
            
            btnSearch = new JButton();
            btnSearch.setIcon(imgIcnDesc);
            btnSearch.setMinimumSize(new Dimension(25, 25));
            btnSearch.setPreferredSize(new Dimension(25, 25));
            btnSearch.setMaximumSize(new Dimension(25, 25));
            btnSearch.setOpaque(true);
            btnSearch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                questionAnswersEditor.stopCellEditing();
                String windowTitle;
                selRow = questionAnswersForm.tblQuestions.getSelectedRow();
                if(selRow != -1 ){
                QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(selRow);
                if(bean.getLookUpGui() != null && bean.getLookUpGui().trim().length() > 0 ){
                    String answerValue = bean.getAnswer() == null ? EMPTY_STRING : bean.getAnswer();
                    String lookUpWindow = bean.getLookUpGui();
                    String lookUpArgument = bean.getLookUpName();
                    if(lookUpArgument == null) {
                        lookUpArgument = EMPTY_STRING;
                    }
                    if(lookUpWindow.equalsIgnoreCase("CodeTable")) {
                        lookUpWindow = CODE_LOOKUP_WINDOW;
                    }else  if(lookUpWindow.equalsIgnoreCase("ValueList")) {
                        lookUpWindow = VALUE_LOOKUP_WINDOW;
                    }else  if(lookUpWindow.equalsIgnoreCase("SelectCostElement")) {
                        lookUpWindow = COST_ELEMENT_LOOKUP_WINDOW;
                    }else  if(lookUpWindow.equalsIgnoreCase("PersonSearch")) {
                        lookUpWindow = PERSON_LOOKUP_WINDOW;
                    }else  if(lookUpWindow.equalsIgnoreCase("UnitSearch")) {
                        lookUpWindow = UNIT_LOOKUP_WINDOW;
                    }else  if(lookUpWindow.equalsIgnoreCase("RolodexSearch")) {
                        lookUpWindow = ROLODEX_LOOKUP_WINDOW;
                    }
                    if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
                        if(lookUpArgument != null){
                            windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                            Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                            OtherLookupBean otherLookupBean =
                            new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                            showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, answerValue, selRow);
                        }
                    }else if(lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW)){
                        windowTitle = "Lookup Values";
                        if(lookUpArgument != null){
                            Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                            OtherLookupBean otherLookupBean =
                            new OtherLookupBean(windowTitle, vecLookupdata, VALUE_COLUMN_NAMES);
                            showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, answerValue, selRow);
                        }
                    }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                        
                        windowTitle = "Cost Elements";
                        if(lookUpArgument != null){
                            if( lookUpArgument.trim().length() > 0){
                                windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                            }
                            Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                            OtherLookupBean otherLookupBean =
                            new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                            showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, answerValue, selRow);
                        }
                    }else{
                        showSearchWindow(lookUpWindow, answerValue, selRow);
                    }
                }
                }
                
            }
          });
          imgMore = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            btnMore = new JButton();
            btnMore.setIcon(imgMore);
            btnMore.setMinimumSize(new Dimension(20, 20));
            btnMore.setPreferredSize(new Dimension(20, 20));
            btnMore.setMaximumSize(new Dimension(20, 20));
            btnMore.setOpaque(true);
            btnMore.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    questionAnswersEditor.stopCellEditing();
                    selRow = questionAnswersForm.tblQuestions.getSelectedRow();
                    if(selRow != -1 ){
                        try {
                            // Case# 3524: Add Explanation field to Questions -Start
                        QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(selRow);
//                        AmountInfoCommentsForm descForm
//                            = new AmountInfoCommentsForm("Question Description");
//                        descForm.dlgWindow.setTitle("Question Description");
//                        descForm.setData(bean.getDescription());
//                        descForm.display();
//                        descForm = null;
                            Integer questionId = bean.getQuestionId();
                            String description = bean.getDescription();
                            QuestionExplanationController explanationController = 
                                    new QuestionExplanationController(CoeusGuiConstants.getMDIForm(), TypeConstants.DISPLAY_MODE);
                            explanationController.setQuestionId(questionId.intValue());
                            CoeusVector cvExplanation = null;
                            cvExplanation = getQuestionExplanation(String.valueOf(questionId));
                            if(!description.equals(EMPTY_STRING)){
                                explanationController.setDescription(description);
                            }else{
                                explanationController.setDescription(EMPTY_STRING);
                            }
                            explanationController.setFormData(cvExplanation);
                            explanationController.display();
                            
                            //Case# 3524: Add Explanation field to Questions -Start
                            
                        } catch (CoeusException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            );
            txtCustomElement = new CoeusTextField();
            txtCustomElement.setMinimumSize(new Dimension(160, 25));
            txtCustomElement.setPreferredSize(new Dimension(160, 25));
            
            //added by vinay
            scrpnAnswer = new JScrollPane();
            scrpnAnswer.setMinimumSize(new Dimension(550,50));
            scrpnAnswer.setPreferredSize(new Dimension(550,50));
            txtArAnswer = new JTextArea();                                
            txtArAnswer.setEditable(true);                               
            txtArAnswer.setLineWrap(true);
            txtArAnswer.setEnabled(true);
            txtArAnswer.setOpaque(true);
            txtArAnswer.setFont(CoeusFontFactory.getNormalFont());
            txtArAnswer.setWrapStyleWord(true); 
            scrpnAnswer.setViewportView(txtArAnswer);
            scrpnAnswer.getViewport().add(txtArAnswer);         
            scrpnAnswer.setOpaque(true);
            //added by vinay
            
            btnOne = new JRadioButton("Yes");
            btnTwo = new JRadioButton("No");
            btnThree = new JRadioButton("N/A");
            btnFour = new JRadioButton("");
            btnFour.setVisible(false);
            btnOne.setFont(CoeusFontFactory.getNormalFont());
            btnTwo.setFont(CoeusFontFactory.getNormalFont());
            btnThree.setFont(CoeusFontFactory.getNormalFont());
            btnOne.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            btnTwo.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            btnThree.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnl = new JPanel();
            pnl.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnl.setLayout(new GridBagLayout());
        }
       public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            this.row = row;
            //Code added for coeus4.3 enhancements
            QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(row);
            switch (column) {
                //COEUSDEV:187-Questionnaire question presentation text blocks limited to 1 line in Premium
                case QUESTION_NUMBER:
                    //Condition added for coeus4.3 enhancements - starts
                    if(bean.getAnswerNumber() == 0){
//                        lblQuestion.setVerticalAlignment(JLabel.TOP);
//                        lblQuestion.setHorizontalAlignment(JLabel.CENTER);
                        String key = ""+bean.getQuestionId().intValue()+bean.getQuestionNumber().intValue();
                        if(hmQuestionNumber.get(key) == null){
                            hmQuestionNumber.put(key, ""+questionNumber);
                            questionNumber++;
                        }
                        lblQuestion.setText(hmQuestionNumber.get(key).toString());
                    } else {
                        lblQuestion.setText(EMPTY_STRING);
                    }
                    pnlBtn = new JPanel(new java.awt.GridBagLayout());
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.weightx = 0.0;
                    gridBagConstraints.weighty = 1.0;
                    gridBagConstraints.insets  = new Insets(5,0,0,0);
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                    pnlBtn.add(lblQuestion, gridBagConstraints);
                    return pnlBtn;
                    //Condition added for coeus4.3 enhancements - ends
                case QUESTION_DESC_COLUMN:
                    //Code commented for coeus4.3 enhancements
//                    QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(row);
                    txtQuesDesc.setText(bean.getDescription() == null ?  EMPTY_STRING : bean.getDescription().trim());
//                    lblQuesDesc.setVerticalAlignment(JLabel.TOP);
                    pnlMain.removeAll();
                    //Condition added for coeus4.3 enhancements - starts
                    if(bean.getAnswerNumber() == 0){
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.fill = gridBagConstraints.HORIZONTAL;
                        txtQuesDesc.setSize(table.getColumnModel().getColumn(column).getWidth(),
                                txtQuesDesc.getPreferredSize().height);
                        int desiredHeight = (int) txtQuesDesc.getPreferredSize().getHeight();
                        if (desiredHeight>table.getRowHeight(row)) {
                            table.setRowHeight(row, desiredHeight);
                        }else{
                            gridBagConstraints.insets = new Insets(2,0,0,0);
                        }
                        pnlMain.add(txtQuesDesc, gridBagConstraints);
//                        pnlMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                        return pnlMain;
                    }
                    //COEUSDEV:187-End
                    //Condition added for coeus4.3 enhancements - ends
                    if(bean.getValidAnswer().equalsIgnoreCase("YN")
                        || bean.getValidAnswer().equalsIgnoreCase("YNX") ){
                        pnl.removeAll();
                        RadActionListener radListener = new RadActionListener();    
                        btnOne.addActionListener(radListener);
                        btnTwo.addActionListener(radListener);
                        btnThree.addActionListener(radListener);
                        group1 = new ButtonGroup();   
                        group1.add(btnOne);
                        group1.add(btnTwo);
                        group1.add(btnFour);
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        pnl.add(btnOne, gridBagConstraints);
                        
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 1;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                         pnl.add(btnTwo, gridBagConstraints);
                       
                        btnOne.setSelected(false);
                        btnTwo.setSelected(false);
                        
                        btnOne.setFocusPainted(false);
                        btnTwo.setFocusPainted(false);
                        
                        
                        group1.add(btnThree);
                        
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 2;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                         pnl.add(btnThree, gridBagConstraints);
                        btnThree.setSelected(false);
                        btnThree.setFocusPainted(false);
                        btnThree.setVisible(true);
                        if(bean.getValidAnswer().equalsIgnoreCase("YN")) {
                            btnThree.setVisible(false);
                        }
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 3;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                         pnl.add(btnFour, gridBagConstraints);
                        btnFour.setSelected(false);
                        btnFour.setFocusPainted(false);
                        if(bean.getAnswer() != null && !bean.getAnswer().equals(EMPTY_STRING)){
                            if(bean.getAnswer().equalsIgnoreCase("Y")){
                                btnOne.setSelected(true);
                            }else if(bean.getAnswer().equalsIgnoreCase("N")){
                                btnTwo.setSelected(true);
                            }else if(bean.getAnswer().equalsIgnoreCase("X")){
                                btnThree.setSelected(true);
                            }
                        }else {
                            btnFour.setSelected(true);
                        }
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 0;
                        //Code added for coeus4.3 enhancements - starts
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;                        
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        if(bean.getAnswerNumber() == 0){
                            gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
//                            questionAnswersForm.tblQuestions.setRowHeight(row, 45);
                        } else {
                            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);                           
//                            questionAnswersForm.tblQuestions.setRowHeight(row, 30);
                        }
//                        questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                        //Code added for coeus4.3 enhancements - ends
                        pnlMain.add(pnl, gridBagConstraints);
                        return pnlMain;
                    }else {
                        pnlText.removeAll();
                        dataLength = bean.getAnswerMaxLength();
                        // COEUSDEV-181:Coeus premium is not validating based on answer length of a question - Start	 
//                        if(dataLength <= 0) {
//                            dataLength = DEFAULT_SIZE;
//                        }
                        // COEUSDEV-181:Coeus premium is not validating based on answer length of a question - End
                        if( bean.getAnswerDataType().equalsIgnoreCase("NUMBER") ){
                            txtCustomElement.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,dataLength));
                            txtArAnswer.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,dataLength));
                        }else if( bean.getAnswerDataType().equalsIgnoreCase("DATE") ){
                            txtCustomElement.setDocument(new LimitedPlainDocument(11));
                            txtArAnswer.setDocument(new LimitedPlainDocument(11));
                        }else {
                            txtCustomElement.setDocument(new LimitedPlainDocument(dataLength));
                            txtArAnswer.setDocument(new LimitedPlainDocument(dataLength));
                        }
                        
                        String newValue = ( String ) value ;
                        if( newValue != null && newValue.length() > 0 ){
                            txtCustomElement.setText( (String)newValue );
                            txtArAnswer.setText( (String)newValue );
                        }else{
                            txtCustomElement.setText(EMPTY_STRING);
                            txtArAnswer.setText(EMPTY_STRING);
                        }
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 0;
//                        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
                        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        txtCustomElement.setEditable(true);
                        txtCustomElement.setBackground(Color.WHITE);
                        btnSearch.setVisible(false);
                        lblSearchName.setVisible(false);
                        if(lookupAvailable[row]) {
                            txtCustomElement.setMinimumSize(new Dimension(160, 25));
                            txtCustomElement.setPreferredSize(new Dimension(160, 25));
                            txtCustomElement.setEditable(false);
                            txtCustomElement.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            pnlText.add(txtCustomElement, gridBagConstraints);

                            btnSearch.setVisible(true);
                            lblSearchName.setVisible(true);
                            gridBagConstraints = new java.awt.GridBagConstraints();
//                            gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
                            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                            pnlText.add(btnSearch, gridBagConstraints);

                            lblSearchName.setText(bean.getSearchName() == null ? EMPTY_STRING : bean.getSearchName());
                            gridBagConstraints = new java.awt.GridBagConstraints();
//                            gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
                            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                            //Code added for coeus4.3 enhancements - starts
                            gridBagConstraints.weightx = 1.0;
                            gridBagConstraints.weighty = 1.0;
//                            if(bean.getAnswerNumber() == 1){
//                                questionAnswersForm.tblQuestions.setRowHeight(row, 63);
//                            } else {
//                                questionAnswersForm.tblQuestions.setRowHeight(row, 45);
//                            }
//                            questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                            pnlText.setMinimumSize(new Dimension(600,32));
                            pnlText.setPreferredSize(new Dimension(600,32));
                            gridBagConstraints.weightx = 1.0;
                            gridBagConstraints.weighty = 1.0;                             
                            //Code added for coeus4.3 enhancements - ends
                            pnlText.add(lblSearchName, gridBagConstraints);
                        }else {
                            gridBagConstraints.weightx = 1.0;
                            gridBagConstraints.weighty = 1.0;
                            //Code modified for coeus4.3 enhancements - starts
                            if(dataLength<=80){
                                if(dataLength <=20 ){
                                  txtCustomElement.setMinimumSize(new Dimension(160, 25));
                                  txtCustomElement.setPreferredSize(new Dimension(160, 25));   
                                }else if(dataLength <=80){
                                  txtCustomElement.setMinimumSize(new Dimension(550, 25));
                                  txtCustomElement.setPreferredSize(new Dimension(550, 25));     
                                }
//                                if(bean.getAnswerNumber() == 1){
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 55);
//                                } else {
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 35);
//                                }
//                                questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                                pnlText.setMinimumSize(new Dimension(600,30));
                                pnlText.setPreferredSize(new Dimension(600,30));
                                //Code added for coeus4.3 enhancements
                                gridBagConstraints.weightx = 1.0;
                                gridBagConstraints.weighty = 1.0;                                  
                                pnlText.add(txtCustomElement, gridBagConstraints);
                            }else{  
//                                if(bean.getAnswerNumber() == 1){
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 70);
//                                } else {
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 55);
//                                }
//                                questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                                pnlText.setMinimumSize(new Dimension(600,50));
                                pnlText.setPreferredSize(new Dimension(600,50));
                                //Code added for coeus4.3 enhancements
                                gridBagConstraints.weightx = 1.0;
                                gridBagConstraints.weighty = 1.0;                                 
                                pnlText.add(scrpnAnswer, gridBagConstraints);
                            }
                        }
                        //Code modified for coeus4.3 enhancements - ends
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 1;
                        //Code added for coeus4.3 enhancements
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;                        
//                        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
                        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
//                        pnlText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));                                
                        pnlMain.add(pnlText, gridBagConstraints);
                        return pnlMain;
                    }
                case MORE_COLUMN:
                    //Condition added for coeus4.3 enhancements - starts
                    if(bean.getAnswerNumber() == 0){
                        pnlBtn = new JPanel(new java.awt.GridBagLayout());
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        //COEUSDEV:187-Questionnaire question presentation text blocks limited to 1 line in Premium
                        gridBagConstraints.insets  = new Insets(5,3,0,0);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        //COEUSDEV:187 End
                        pnlBtn.add(btnMore, gridBagConstraints);
                        return pnlBtn;
                    } else {
                        lblQuestion.setText(EMPTY_STRING);
                    }
                    //Condition added for coeus4.3 enhancements - ends
                
            }
            
            return lblQuestion;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case QUESTION_NUMBER:
                    return lblQuestion.getText();
                case QUESTION_DESC_COLUMN:
                    QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(row);
                    if(bean.getValidAnswer().equalsIgnoreCase("YN")) {
                        if(bean.getAnswer() == null) {
                            return EMPTY_STRING;
                        }
                        return bean.getAnswer();
                    }else if(bean.getValidAnswer().equalsIgnoreCase("YNX") ){
                        if(bean.getAnswer() == null) {
                            return EMPTY_STRING;
                        }
                        return bean.getAnswer();
                        
                    }else{
                        if(dataLength<=80){
                            bean.setAnswer(txtCustomElement.getText().trim());
                            return txtCustomElement.getText().trim();
                        }else{
                            bean.setAnswer(txtArAnswer.getText().trim());                            
                            return txtArAnswer.getText().trim();
                        }
                    }
               
            }
            return lblQuestion;
        }
         class RadActionListener implements ActionListener {
            public void actionPerformed(ActionEvent ie) {
                String ans = "";
                Object source = ie.getSource();
                if (source.equals(btnOne)) {
                    ans = "Y";
                } else if (source.equals(btnTwo)) {
                    ans = "N";
                } else if (source.equals(btnThree)) {
                    ans = "X";
                } else if (source.equals(btnFour)) {
                    ans = EMPTY_STRING;
                }
                row = questionAnswersForm.tblQuestions.getSelectedRow();
                QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(row);
                bean.setAnswer(ans);
                questionAnswersTableModel.fireTableRowsUpdated(row, row);
            }
        }
     }
     /*** Table editor for AwardBudgetOverheadTable*/
     class QuestionAnswersRenderer extends DefaultTableCellRenderer {
        
       private int row;
        private JLabel lblQuestion, lblSearchName;
        private CoeusTextField txtCustomElement;
        private JTextArea txtArAnswer,txtQuesDesc;//COEUSDEV 187
        private JScrollPane scrpnAnswer;
        private JButton btnSearch, btnMore;
        public JPanel pnl;
        public JPanel pnlText, pnlBtn;
        public JPanel pnlMain;
        public ButtonGroup group1;
        public JRadioButton btnOne;
        public JRadioButton btnTwo;
        public JRadioButton btnThree;
        public JRadioButton btnFour;
        private int column;
        private int dataLength;
        private GridBagConstraints gridBagConstraints;
        private ImageIcon imgIcnDesc, imgMore;
        private HashMap hmQuestionNumber = new HashMap();
        private int questionNumber = 1;
        QuestionAnswersRenderer() {
            pnlText = new JPanel();
            pnlText.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnlText.setLayout(new java.awt.GridBagLayout());
            pnlText.setMinimumSize(new Dimension(600,50));
            pnlText.setPreferredSize(new Dimension(600,50));
            
            pnlMain = new JPanel();
            pnlMain.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnlMain.setLayout(new java.awt.GridBagLayout());
           
            lblQuestion=new JLabel("");
            lblQuestion.setOpaque(false);
            
            lblSearchName = new JLabel("");
            lblSearchName.setOpaque(false);
           //COEUSDEV:187-Questionnaire question presentation text blocks limited to 1 line in Premium 
//            lblQuesDesc = new JLabel("");
//            lblQuesDesc.setOpaque(false);
          //  lblQuesDesc.setMinimumSize(new Dimension(600, 20));
           // lblQuesDesc.setPreferredSize(new Dimension(600, 20));
           // lblQuesDesc.setMaximumSize(new Dimension(600, 20));
            txtQuesDesc = new JTextArea();
            txtQuesDesc.setOpaque(false);
            txtQuesDesc.setLineWrap(true);
            txtQuesDesc.setWrapStyleWord(true);
            txtQuesDesc.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            txtQuesDesc.setFont(CoeusFontFactory.getNormalFont());
            txtQuesDesc.setMargin(new Insets(2,2,5,5));
            //COEUSDEV:187-End        
            imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON));
            btnSearch = new JButton();
            btnSearch.setIcon(imgIcnDesc);
            btnSearch.setMinimumSize(new Dimension(25, 25));
            btnSearch.setPreferredSize(new Dimension(25, 25));
            btnSearch.setMaximumSize(new Dimension(25, 25));
            btnSearch.setOpaque(true);
            
            imgMore = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            btnMore = new JButton();
            btnMore.setIcon(imgMore);
            btnMore.setMinimumSize(new Dimension(20, 20));
            btnMore.setPreferredSize(new Dimension(20, 20));
            btnMore.setMaximumSize(new Dimension(20, 20));
            btnMore.setOpaque(true);
            
            txtCustomElement = new CoeusTextField();
            txtCustomElement.setMinimumSize(new Dimension(160, 25));
            txtCustomElement.setPreferredSize(new Dimension(160, 25));
            
            //Added by vinay
            scrpnAnswer = new JScrollPane();
            scrpnAnswer.setMinimumSize(new Dimension(550,50));            
            scrpnAnswer.setPreferredSize(new Dimension(550,50));
            txtArAnswer = new JTextArea();                                
            txtArAnswer.setEditable(true);
            txtArAnswer.setEnabled(true);
            txtArAnswer.setOpaque(true);
            txtArAnswer.setLineWrap(true);
            txtArAnswer.setWrapStyleWord(true);
            txtArAnswer.setFont(CoeusFontFactory.getNormalFont());
            
            scrpnAnswer.setViewportView(txtArAnswer);
            scrpnAnswer.getViewport().add(txtArAnswer);   
            scrpnAnswer.setVisible(true);           
           // scrpnAnswer.add(txtArAnswer); 
            scrpnAnswer.setOpaque(true); 
            //added by vinay
            
            btnOne = new JRadioButton("Yes");
            btnTwo = new JRadioButton("No");
            btnThree = new JRadioButton("N/A");
            btnFour = new JRadioButton("");
            btnFour.setVisible(false);
            btnOne.setFont(CoeusFontFactory.getNormalFont());
            btnTwo.setFont(CoeusFontFactory.getNormalFont());
            btnThree.setFont(CoeusFontFactory.getNormalFont());
            btnOne.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            btnTwo.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            btnThree.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnl = new JPanel();
            pnl.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            pnl.setLayout(new GridBagLayout());
        }
       public Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
           this.column = column;
            this.row = row;
            //Code added for coeus4.3 enhancements
            QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(row);
            switch (column) {
                //COEUSDEV:187-Questionnaire question presentation text blocks limited to 1 line in Premium
                case QUESTION_NUMBER:
                    if(bean.getAnswerNumber() == 0){
//                        lblQuestion.setVerticalAlignment(JLabel.CENTER);
//                        lblQuestion.setHorizontalAlignment(JLabel.CENTER);
                        String key = ""+bean.getQuestionId().intValue()+bean.getQuestionNumber().intValue();
                        if(hmQuestionNumber.get(key) == null){
                            hmQuestionNumber.put(key, ""+questionNumber);
                            questionNumber++;
                        }
                        lblQuestion.setText(hmQuestionNumber.get(key).toString());
                    } else {
                        lblQuestion.setText(EMPTY_STRING);
                    }
                    pnlBtn = new JPanel(new java.awt.GridBagLayout());
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.weightx = 0.0;
                    gridBagConstraints.weighty = 1.0;
                    gridBagConstraints.insets  = new Insets(5,0,0,0);
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                    pnlBtn.add(lblQuestion, gridBagConstraints);
                    return pnlBtn;
                case QUESTION_DESC_COLUMN:
                    //Code commented for coeus4.3 enhancements
//                    QuestionnaireQuestionsBean bean = (QuestionnaireQuestionsBean)cvData.get(row);
                    txtQuesDesc.setText(bean.getDescription() == null ?  EMPTY_STRING : bean.getDescription().trim());
//                    lblQuesDesc.setVerticalAlignment(JLabel.TOP);
                    pnlMain.removeAll();
                    //condition added for coeus4.3 enhancements
                    if(bean.getAnswerNumber() == 0){
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.fill = gridBagConstraints.HORIZONTAL;
                        txtQuesDesc.setSize(table.getColumnModel().getColumn(column).getWidth(),
                                txtQuesDesc.getPreferredSize().height);
                        int desiredHeight = (int) txtQuesDesc.getPreferredSize().getHeight();
                        if (desiredHeight>table.getRowHeight(row)) {
                            table.setRowHeight(row, desiredHeight);
                        }else{
                            gridBagConstraints.insets = new Insets(2,0,0,0);
                        }
                        pnlMain.add(txtQuesDesc, gridBagConstraints);
//                        pnlMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));                        
                        return pnlMain;
                    }
                    //COEUSDEV:187-End
                    if(bean.getValidAnswer().equalsIgnoreCase("YN")
                        || bean.getValidAnswer().equalsIgnoreCase("YNX") ){
                        pnl.removeAll();
                        group1 = new ButtonGroup();   
                        group1.add(btnOne);
                        group1.add(btnTwo);
                        group1.add(btnFour);
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        pnl.add(btnOne, gridBagConstraints);
                        
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 1;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        pnl.add(btnTwo, gridBagConstraints);
                       
                        btnOne.setSelected(false);
                        btnTwo.setSelected(false);
                        
                        btnOne.setFocusPainted(false);
                        btnTwo.setFocusPainted(false);
                        
                        
                        group1.add(btnThree);
                        
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 2;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                         pnl.add(btnThree, gridBagConstraints);
                        btnThree.setSelected(false);
                        btnThree.setFocusPainted(false);
                        btnThree.setVisible(true);
                        if(bean.getValidAnswer().equalsIgnoreCase("YN")) {
                            btnThree.setVisible(false);
                        }
                        gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.gridx = 3;
                        gridBagConstraints.gridy = 2;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                         pnl.add(btnFour, gridBagConstraints);
                        btnFour.setSelected(false);
                        btnFour.setFocusPainted(false);
                        if(bean.getAnswer() != null && !bean.getAnswer().equals(EMPTY_STRING)){
                            if(bean.getAnswer().equalsIgnoreCase("Y")){
                                btnOne.setSelected(true);
                            }else if(bean.getAnswer().equalsIgnoreCase("N")){
                                btnTwo.setSelected(true);
                            }else if(bean.getAnswer().equalsIgnoreCase("X")){
                                btnThree.setSelected(true);
                            }
                        }else {
                            btnFour.setSelected(true);
                        }
                        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
                            btnOne.setEnabled(false);
                            btnTwo.setEnabled(false);
                            btnThree.setEnabled(false);
                            btnFour.setEnabled(false);
                        }
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 0;
                        //Code added for coeus4.3 enhancements - starts
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;                        
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        if(bean.getAnswerNumber() == 0){
                            gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
//                            questionAnswersForm.tblQuestions.setRowHeight(row, 45);
                        } else {
                            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);                           
//                            questionAnswersForm.tblQuestions.setRowHeight(row, 30);
                        }
//                        questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                        pnlMain.add(pnl, gridBagConstraints);
                        //Code added for coeus4.3 enhancements - ends
                        return pnlMain;
                    }else {
                        pnlText.removeAll();
                        dataLength = bean.getAnswerMaxLength();
                        // // COEUSDEV-181:Coeus premium is not validating based on answer length of a question - Start
//                        if(dataLength <= 0) {
//                            dataLength = DEFAULT_SIZE;
//                        }
                        // // COEUSDEV-181:Coeus premium is not validating based on answer length of a question - End
                        if( bean.getAnswerDataType().equalsIgnoreCase("NUMBER") ){
                            txtCustomElement.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,dataLength));
                            txtArAnswer.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,dataLength));
                        }else if( bean.getAnswerDataType().equalsIgnoreCase("DATE") ){
                            txtCustomElement.setDocument(new LimitedPlainDocument(11));
                            txtArAnswer.setDocument(new LimitedPlainDocument(11));
                        }else {
                            txtCustomElement.setDocument(new LimitedPlainDocument(dataLength));
                            txtArAnswer.setDocument(new LimitedPlainDocument(dataLength));
                        }
                        
                        String newValue = ( String ) value ;
                        if( newValue != null && newValue.length() > 0 ){                            
                            txtCustomElement.setText( (String)newValue );                          
                            txtArAnswer.setText( (String)newValue );                           
                        }else{
                            txtCustomElement.setText(EMPTY_STRING);
                            txtArAnswer.setText(EMPTY_STRING);
                        }
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 0;
//                        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
                        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        txtCustomElement.setEditable(true);
                        txtCustomElement.setBackground(Color.WHITE);
                        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
                            txtCustomElement.setEditable(false);
                            txtCustomElement.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            txtArAnswer.setEditable(false);
                            //`txtArAnswer.setEnabled(false);
                            txtArAnswer.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        }
                        btnSearch.setVisible(false);
                        lblSearchName.setVisible(false);
                        if(lookupAvailable[row]) {
                            txtCustomElement.setMinimumSize(new Dimension(160, 25));
                            txtCustomElement.setPreferredSize(new Dimension(160, 25));
                            txtCustomElement.setEditable(false);
                            txtCustomElement.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            pnlText.add(txtCustomElement, gridBagConstraints);

                            btnSearch.setVisible(true);
                            btnSearch.setEnabled(true);
                            lblSearchName.setVisible(true);
                            gridBagConstraints = new java.awt.GridBagConstraints();
//                            gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
                            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                            pnlText.add(btnSearch, gridBagConstraints);

                            lblSearchName.setText(bean.getSearchName() == null ? EMPTY_STRING : bean.getSearchName());
                            gridBagConstraints = new java.awt.GridBagConstraints();
//                            gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
                            gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                            //Code added for coeus4.3 enhancements - starts
                            gridBagConstraints.weightx = 1.0;
                            gridBagConstraints.weighty = 1.0;
//                            if(bean.getAnswerNumber() == 1){
//                                questionAnswersForm.tblQuestions.setRowHeight(row, 63);
//                            } else {
//                                questionAnswersForm.tblQuestions.setRowHeight(row, 45);
//                            }
//                            questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                            pnlText.setMinimumSize(new Dimension(600,32));
                            pnlText.setPreferredSize(new Dimension(600,32)); 
                            //Code added for coeus4.3 enhancements - ends
                            pnlText.add(lblSearchName, gridBagConstraints);
                            if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
                                btnSearch.setEnabled(false);
                            }
                        }else {
                            gridBagConstraints.weightx = 1.0;
                            gridBagConstraints.weighty = 1.0;
                            if(dataLength<=80){
                                if(dataLength <=20 ){
                                  txtCustomElement.setMinimumSize(new Dimension(160, 25));
                                  txtCustomElement.setPreferredSize(new Dimension(160, 25));   
                                }else if(dataLength <=80){
                                  txtCustomElement.setMinimumSize(new Dimension(550, 25));
                                  txtCustomElement.setPreferredSize(new Dimension(550, 25));     
                                }
                                //Code added for coeus4.3 enhancements - starts
//                                if(bean.getAnswerNumber() == 1){
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 55);
//                                } else {
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 35);
//                                }
//                                questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                                pnlText.setMinimumSize(new Dimension(600,30));
                                pnlText.setPreferredSize(new Dimension(600,30));
                                pnlText.add(txtCustomElement, gridBagConstraints);
                                //Code added for coeus4.3 enhancements - ends
                            }else{  
//                                if(bean.getAnswerNumber() == 1){
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 70);
//                                } else {
//                                    questionAnswersForm.tblQuestions.setRowHeight(row, 55);
//                                }
//                                questionAnswersForm.scrPnQuestions.setIgnoreRepaint(false);
                                pnlText.setMinimumSize(new Dimension(600,50));
                                pnlText.setPreferredSize(new Dimension(600,50));
                                pnlText.add(scrpnAnswer, gridBagConstraints);
                            }
                        }
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 1;
                        //Code added for coeus4.3 enhancements
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;                        
//                        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
                        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        pnlMain.add(pnlText, gridBagConstraints);
                        return pnlMain;
                    }
                case MORE_COLUMN :
                    //Code added for coeus4.3 enhancements - starts
                    if(bean.getAnswerNumber() == 0){
                        pnlBtn = new JPanel(new java.awt.GridBagLayout());
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        //COEUSDEV:187-Questionnaire question presentation text blocks limited to 1 line in Premium
                        gridBagConstraints.insets  = new Insets(5,3,0,0);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        //COEUSDEV:187-End
                        pnlBtn.add(btnMore, gridBagConstraints);
                        return pnlBtn;
                    } else {
                        lblQuestion.setText(EMPTY_STRING);
                    }
                    //Code added for coeus4.3 enhancements - ends
            }
            return lblQuestion;
        }
      }   
      
    /**
     * Code added for coeus4.3 enhancements
     * For saving the answers and getting the questions for next page
     * @param statusType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return CoeusVector
     */
     public CoeusVector saveAndGetQuestions(char statusType) throws edu.mit.coeus.exception.CoeusException{
        CoeusVector cvMainData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(statusType);
        CoeusVector cvQuestionnaireData = new CoeusVector();
        cvQuestionnaireData.add(questionnaireAnswerHeaderBean);
        cvQuestionnaireData.add(cvData);
        //Code added for Case#2785 - Routing Enhancements
        cvQuestionnaireData.add(questionnaireMenusTableModel.getData());
        request.setDataObject(cvQuestionnaireData);
        request.setId(moduleItemKey);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                cvMainData = (CoeusVector)response.getDataObject();
                page = response.getId();
                page = (page == null)? EMPTY_STRING : page;
                if(response.getMessage() != null 
                        && response.getMessage().equals("COMPLETED")){
                    // Modified for IACUC Questionnaire implementation - Start
//                    if(questionnaireAnswerHeaderBean.getModuleItemCode() == 7){
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
//                                "questionnaire_exceptionCode.1015")+moduleItemKey+"  ");
//                    } else if(questionnaireAnswerHeaderBean.getModuleItemCode() == 3){
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
//                                "questionnaire_exceptionCode.1016")+moduleItemKey+"  ");
//                    }
                    if(questionnaireAnswerHeaderBean.getModuleItemCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                ERROR_IRB_PROTOCOL_QUESTIONNAIRE_NOT_COMPLETED)+moduleItemKey+"  ");
                    } else if(questionnaireAnswerHeaderBean.getModuleItemCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                ERROR_PROP_DEV_QUESTIONNAIRE_NOT_COMPLETED)+moduleItemKey+"  ");
                    } else if(questionnaireAnswerHeaderBean.getModuleItemCode() == ModuleConstants.IACUC_MODULE_CODE){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                ERROR_IACUC_PROTOCOL_QUESTIONNAIRE_NOT_COMPLETED)+moduleItemKey+"  ");
                    }
                    // Modified for IACUC Questionnaire implementation - End

                    enableDisableButtons(false);
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
                    if(statusType == SAVE_AND_COMPLETE){
                        questionAnswersForm.btnSaveAndComplete.setEnabled(false);
                    }
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
                    if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(true);
//                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(true);
                        if(isModulelSubmission){
                            questionAnswersForm.btnModify.setEnabled(true);
                        }else{
                            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(true);
                            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(true);
                        }
                        //COEUSDEV-86 : End
                        setFunctionType(TypeConstants.DISPLAY_MODE);
                    }
                }else{
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
                    if(statusType == SAVE_AND_COMPLETE){
                        CoeusOptionPane.showInfoDialog(response.getMessage());
                    }
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
                    if(isModulelSubmission){
                        questionAnswersForm.btnModify.setEnabled(false);
                    }else{
                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(false);
                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(false);
                    }
                }
            }else{
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        return cvMainData;         
     }

    /**
     * This is an inner class specify the tableRenderer for questionnaire menu data
     */     
    class QuestionnaireMenuTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JLabel lblText;
        private ImageIcon imgIcnDesc;
        
        /**
         * Default Constructor
         */
        public QuestionnaireMenuTableRenderer(){
            lblText = new JLabel();
            lblText.setBorder(new EmptyBorder(0,0,0,0));
            lblText.setOpaque(true);
            imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON));
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
            Component returnComponent = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
            QuestionnaireAnswerHeaderBean menuHeaderBean = (QuestionnaireAnswerHeaderBean) cvTableData.get(row);
            switch(col){
                case 0:
                    JLabel lblMenu = (JLabel)returnComponent;
                    if(menuHeaderBean.getQuestionnaireCompletionFlag() != null
                            && menuHeaderBean.getQuestionnaireCompletionFlag().equals("Y")){
                        lblMenu.setIcon(imgIcnDesc);
                    } else {
                        lblMenu.setIcon(null);
                    }
                    return lblMenu;
                case 1:
                    if (value != null && !value.toString().trim().equals(EMPTY_STRING)) {
                        lblText.setText(value.toString());
                        lblText.setToolTipText(value.toString());
                    } else {
                        lblText.setText(EMPTY_STRING);
                        lblText.setToolTipText(EMPTY_STRING);
                    }
                    lblText.setBackground(returnComponent.getBackground());
                    lblText.setForeground(returnComponent.getForeground());
                    lblText.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                    return lblText;
            }
            return lblText;
        } // End of  getTableCellRendererComponent
    }
    
    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * setting the column data
     */
    private void setColumnData(){
        
        JTableHeader tableHeader = questionAnswersForm.tblMenus.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));
        
        questionAnswersForm.tblMenus.setRowHeight(22);
        questionAnswersForm.tblMenus.setRowHeight(22);
        questionAnswersForm.tblMenus.setFont(CoeusFontFactory.getLabelFont());
        questionAnswersForm.tblMenus.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        questionAnswersForm.tblMenus.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        TableColumn column = questionAnswersForm.tblMenus.getColumnModel().getColumn(0);
        column.setPreferredWidth(35);
        column.setResizable(false);
        column.setCellRenderer(questionnaireMenuTableRenderer);
        column = questionAnswersForm.tblMenus.getColumnModel().getColumn(1);
        column.setPreferredWidth(165);
        column.setResizable(false);
        column.setCellRenderer(questionnaireMenuTableRenderer);
    }
    
    /**
     * This is an inner class specify the table model for questionnaire menu data
     */
    class QuestionnaireMenusTableModel extends AbstractTableModel {
        
        // QuestionnaireMenusTableModel()   
        String colNames[] = {"", "Questionnaire"};
        Class[] colTypes = new Class [] {String.class, String.class};
        
        /**
         * This method will check whether the given field is ediatble or not
         * @param row int
         * @param col int
         * @return boolean
         */
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * Thie mthod will return the column count
         * @return int
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * This method will return the row count
         * @return int
         */
        public int getRowCount() {
            if( cvTableData  == null )
                return 0;
            else
                return cvTableData .size();
            
        }
        
        /**
         * This method will return the column class
         * @param columnIndex int
         * @return Class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /**
         * To get the column name
         * @param column int
         * @return String
         */
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /**
         * settig the data to the vector
         * @param cvTableData CoeusVector
         */
        public void setData(CoeusVector cvTableData){
            cvTableData = cvTableData;
        }
        
        /**
         * settig the data to the vector
         * @param cvTableData CoeusVector
         */
        public CoeusVector getData(){
            return cvTableData;
        }        
        
        /**
         * To get the value depends on the row and column
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         */
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            QuestionnaireAnswerHeaderBean QuestionnaireAnswerHeaderBean =
                    (QuestionnaireAnswerHeaderBean) cvTableData.get(rowIndex);
            switch(columnIndex) {
                
                case 0:
                    return EMPTY_STRING;
                case 1:
                    return QuestionnaireAnswerHeaderBean.getLabel();
            }
            return EMPTY_STRING;
            
        } // end of getValueAt()
    } // end of QuestionnaireMenusTableModel
    
    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * method to get the Questionnaire Menu data
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return CoeusVector
     */
    private CoeusVector getMenuDataFromServer() throws CoeusException{
        CoeusVector cvMainData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_QUESTIONS_MODE);
        CoeusVector cvQuestionnaireData = new CoeusVector();
        cvQuestionnaireData.add(questionnaireAnswerHeaderBean);
        request.setDataObject(cvQuestionnaireData);        
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
    
    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * To enable or disable the buttons
     * @param enable value to enable or diable buttons
     */
    private void enableDisableButtons(boolean enable){
        questionAnswersForm.btnGoBack.setEnabled(enable);
        questionAnswersForm.btnSaveAndProceed.setEnabled(enable);
        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
        //If module submission, then statover button in questionAnswersForm is enabled or diabled
//        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnSave.setEnabled(enable);
//        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.saveMenuItem.setEnabled(enable);
//        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnStartOver.setEnabled(enable);
//        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.startOverMenuItem.setEnabled(enable);
        if(isModulelSubmission){
            questionAnswersForm.btnStartOver.setEnabled(enable);
        }else{
            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnSave.setEnabled(enable);
            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.saveMenuItem.setEnabled(enable);
            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnStartOver.setEnabled(enable);
            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.startOverMenuItem.setEnabled(enable);
        }
        //COEUSDEV-86 : End
    }

    /**
     * Getter for property originalFunctionType.
     * @return Value of property originalFunctionType.
     */
    public char getOriginalFunctionType() {
        return originalFunctionType;
    }

    /**
     * Setter for property originalFunctionType.
     * @param originalFunctionType New value of property originalFunctionType.
     */
    public void setOriginalFunctionType(char originalFunctionType) {
        this.originalFunctionType = originalFunctionType;
    }

    /**
     * Getter for property questionnaireAnswersBaseController.
     * @return Value of property questionnaireAnswersBaseController.
     */
    public QuestionnaireAnswersBaseController getQuestionnaireAnswersBaseController() {
        return questionnaireAnswersBaseController;
    }

    /**
     * Setter for property questionnaireAnswersBaseController.
     * @param questionnaireAnswersBaseController New value of property questionnaireAnswersBaseController.
     */
    public void setQuestionnaireAnswersBaseController(QuestionnaireAnswersBaseController questionnaireAnswersBaseController) {
        this.questionnaireAnswersBaseController = questionnaireAnswersBaseController;
    }
    
    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * To save the data to the DB
     * @param operation (mode of operation)
     */
    public void processData(char operation) {
        CoeusVector cvQuestionsData = null;
//        CoeusVector cvMenuData = null;
        try {
            questionAnswersForm.btnSaveAndComplete.setEnabled(false);
            CoeusVector cvQuesData = saveAndGetQuestions(operation);
            cvQuestionsData = (CoeusVector) cvQuesData.get(0);
            if(operation == RESTART || operation == SAVE){
                cvTableData = (CoeusVector) cvQuesData.get(1);
                
            } else if(operation == MODIFY){
                questionAnswersForm.btnSaveAndComplete.setEnabled(true);
                cvTableData = (CoeusVector) cvQuesData.get(1);
                setFunctionType(originalFunctionType);
                //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(false);
//                questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(false);
                if(isModulelSubmission){
                    questionAnswersForm.btnModify.setEnabled(false);
                }else{
                    questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(false);
                    questionnaireAnswersBaseController.questionnaireAnswersBaseForm.modifyMenuItem.setEnabled(false);
                }
                //COEUSDEV-86 : End
                if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                    enableDisableButtons(true);
                }
            }
            
            if(cvQuestionsData != null && !cvQuestionsData.isEmpty()) {
                cvData = (CoeusVector)getQuestionsData(cvQuestionsData);
            } else {
                cvData = new CoeusVector();
            }
            questionAnswersTableModel = new QuestionAnswersTableModel();
            questionAnswersEditor = new QuestionAnswersEditor();
            questionAnswersRenderer = new QuestionAnswersRenderer();
            questionAnswersForm.tblQuestions.setModel(questionAnswersTableModel);
            setTableEditors();
            if(page.equals("1")){
                questionAnswersForm.btnGoBack.setEnabled(false);
            } else {
                questionAnswersForm.btnGoBack.setEnabled(true);
            }
            if(cvQuesData != null && cvQuesData.size() > 2){
                String totalPages = (String) cvQuesData.get(2);
                //Modified for Case#3769 - Questionnaire issues - starts
//                if(totalPages != null && totalPages.equals("1")){
                if(getFunctionType() == TypeConstants.DISPLAY_MODE &&
                        totalPages != null && totalPages.equals("1")){
                    questionAnswersForm.btnSaveAndProceed.setEnabled(false);
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
                    questionAnswersForm.btnSaveAndComplete.setEnabled(false);
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
                //Modified for Case#3769 - Questionnaire issues - ends
                } else {
                    questionAnswersForm.btnSaveAndProceed.setEnabled(true);
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
                    if(operation == RESTART || operation == SAVE){
                        questionAnswersForm.btnSaveAndComplete.setEnabled(false);
                    }else{
                        questionAnswersForm.btnSaveAndComplete.setEnabled(true);
                    }
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
                }                
            }
            int selectedRow = questionAnswersForm.tblMenus.getSelectedRow();
            questionnaireMenusTableModel.setData(cvTableData);
            questionnaireMenusTableModel.fireTableDataChanged();
            if(selectedRow != -1 && cvTableData != null && selectedRow < cvTableData.size()){
                questionAnswersForm.tblMenus.setRowSelectionInterval(selectedRow, selectedRow);
            }
            cvAnswersData = (CoeusVector) ObjectCloner.deepCopy(cvData);
            questionAnswersTableModel.setData(cvData);
            questionAnswersTableModel.fireTableDataChanged();
        } catch(Exception e) {
            CoeusOptionPane.showWarningDialog(e.getMessage());
        }
    }  
    //Commented for Routing Release - Start
    //Case#2946 Coeus43 enhancement - start
    //Added for Questionnaire Printing enhancement
    /**
     * Is used to communicate with the serverside components when print 
     * functionality is invoked
     * @param printAnswers Boolean option to specify to print answers or not
     * @param printAll Boolean option to specify to print all the answered and 
     * unanswered questions (with answers for answered questions)
     * @param printAnsweredOnly Boolean option to specify to print only answered
     * questions with answers
     * @throws CoeusException
     */
    public void printQuestionnaire(boolean printAnswers, boolean printAll, boolean printAnsweredOnly)throws CoeusException{       
        RequesterBean requester = new RequesterBean();
        Hashtable htParams = new Hashtable();
        questionnaireAnswerHeaderBean.setPrintAnswers(printAnswers);
        questionnaireAnswerHeaderBean.setPrintAll(printAll);
        questionnaireAnswerHeaderBean.setPrintOnlyAnswered(printAnsweredOnly);
        
        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
        int row = questionAnswersForm.tblMenus.getSelectedRow();
        // Added for the case# COEUSQA-2266 -allow user to print a questionnaire that was created with prior version of questionnaire -start
	// in order to get Latest VersionNumber we called fetchQuestionnaireVersion number
        fetchQuestionnaireVersionDetails();
        if(cvTableData != null && cvTableData.size() > 0){
            QuestionnaireAnswerHeaderBean answerHeadrBean = (QuestionnaireAnswerHeaderBean) cvTableData.get(row);
            questionnaireAnswerHeaderBean.setQuestionnaireCompletionFlag(answerHeadrBean.getQuestionnaireCompletionFlag());
        }
        // Added for the case# COEUSQA-2266 -allow user to print a questionnaire that was created with prior version of questionnaire -start
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(answeredQuestionnaireVersion);
        }else{
            questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(latestQuestionnaireVersion);
        }
        // Added for the case# COEUSQA-2266 -allow user to print a questionnaire that was created with prior version of questionnaire -End
        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
        htParams.put(QuestionnaireAnswerHeaderBean.class, questionnaireAnswerHeaderBean);
        requester.setDataObject(htParams);
        
        //For Streaming
        requester.setId("Questionnaire/QuestionnaireReport");
        requester.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
        comm.send();
        ResponderBean responder = comm.getResponse();
        String fileName = "";
        if(responder.isSuccessfulResponse()){
             fileName = (String)responder.getDataObject();
             try{
                 URL url = new URL(fileName);
                 URLOpener.openUrl(url);
             }catch (MalformedURLException malformedURLException) {
                 throw new CoeusException(malformedURLException.getMessage());
             }
         }else{
             //Modified with case 4287:Show proper error message if printing failed
             throw new CoeusException(coeusMessageResources.parseMessageKey(ERRKEY_PRINT_TEMPLATE));
         }               
    }
    //Case#2946 Coeus43 enhancement - end
    //Commented for Routing Release - End
    // Case# 3524: Add Explanation field to Questions - Start
    
    /**
     * This Method is used for getting the Explanation, Regulation,Policy information for a Question
     * @param questionId String
     * @return cvQuestionExplanation CoeusVector 
     */
    
    private CoeusVector getQuestionExplanation(String questionId) {
        CoeusVector cvQuestionExplanation = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/questionnaireServlet";
        QuestionExplanationBean questionExplanationBean = new QuestionExplanationBean();
        questionExplanationBean.setQuestionId(new Integer(questionId));
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
    // 4272: Maintain history of Questionnaires - Start
    private CoeusVector fetchQuestionnaireVersionDetails() throws CoeusException{
        CoeusVector cvVersionData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_QUESTIONNAIRE_VERSION_DETAILS);
        request.setDataObject(questionnaireAnswerHeaderBean);
        // Added for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - Start
        request.setId(getFunctionType()+CoeusGuiConstants.EMPTY_STRING);
        // Added for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - End
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                cvVersionData = (CoeusVector)response.getDataObjects();
                answeredQuestionnaireVersion = ((Integer) cvVersionData.get(0)).intValue();
                latestQuestionnaireVersion = ((Integer) cvVersionData.get(1)).intValue();
            }else{
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvVersionData;
    }
    // 4272: Maintain history of Questionnaires - End
    
    //Added with COEUSDEV230: Answered questionnaire says it is not Answered in Approval in Progress Proposal
    /* This function loads the questions on selecting 
     * an item in the questionnaire list menu.
     */
    private void loadQuestions(){
        try {
            questionAnswersForm.btnSaveAndComplete.setEnabled(false);
            int row = questionAnswersForm.tblMenus.getSelectedRow();
            if(row != -1 && cvTableData != null
                    && cvTableData.size() > row){
                QuestionnaireAnswerHeaderBean answerBean =
                        (QuestionnaireAnswerHeaderBean)cvTableData.get(row);
                //setting for getting answer
                if(cvAnswersData != null && !cvAnswersData.isEmpty()){
                    if(checkDataModified()){
                        int selection = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey("questions_exceptionCode.1005"),
                                CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                        if(selection == CoeusOptionPane.SELECTION_YES) {
                            if(validate()){
                                processData(SAVE);
                            } else {
                                return;
                            }
                        } else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                            return;
                        }
                    }
                }
                questionnaireAnswerHeaderBean.setQuestionnaireId(answerBean.getQuestionnaireId());
                questionnaireAnswerHeaderBean.setName(answerBean.getName());
                questionnaireAnswerHeaderBean.setDescription(answerBean.getDescription());
                questionnaireAnswerHeaderBean.setLabel(answerBean.getLabel());
                questionnaireAnswerHeaderBean.setRuleId(answerBean.getRuleId());
                // Added with CoeusQA2313: Completion of Questionnaire for Submission
                questionnaireAnswerHeaderBean.setApplicableSubmoduleCode(answerBean.getApplicableSubmoduleCode());
                questionnaireAnswerHeaderBean.setQuestionnaireCompletionFlag(answerBean.getQuestionnaireCompletionFlag());
                if("Y".equals(answerBean.getQuestionnaireCompletionFlag())){
                    questionnaireAnswerHeaderBean.setApplicableModuleItemKey(answerBean.getApplicableModuleItemKey());
                    questionnaireAnswerHeaderBean.setApplicableModuleSubItemKey(answerBean.getApplicableModuleSubItemKey());
                }else{
                    questionnaireAnswerHeaderBean.setApplicableModuleItemKey(originalModuleItemKey);
                    questionnaireAnswerHeaderBean.setApplicableModuleSubItemKey(Integer.parseInt(originalModuleItemKeySequence));
                }
                if(isAmendmentRenewal()){
                    setAmendmentRenewalQuestionnaireProperties(answerBean);
                }else{
                    setFunctionType(originalFunctionType);
                }
                // CoeusQA2313: Completion of Questionnaire for Submission - End
                // 4272: Maintain history of Questionnaires - Start
                fetchQuestionnaireVersionDetails();
                // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal
                if(latestQuestionnaireVersion != 0 && getFunctionType() != TypeConstants.DISPLAY_MODE){
                    if(answeredQuestionnaireVersion != latestQuestionnaireVersion && latestQuestionnaireVersion != 1
                            && latestQuestionnaireVersion > answeredQuestionnaireVersion
                            // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire
                            && answeredQuestionnaireVersion != 0 ){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1025"));
                        questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(latestQuestionnaireVersion);                        
                    } else {
                        questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(latestQuestionnaireVersion);
                    }
                } else{
                    questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(answeredQuestionnaireVersion);
                }
                // 4272: Maintain history of Questionnaires - End
                // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - Start
                CoeusVector cvQuestionsData = new CoeusVector();
                String totalPages = "";
                if(questionnaireAnswerHeaderBean.getQuestionnaireVersionNumber() > 0){
                    CoeusVector cvQuesData = (CoeusVector)getServerData(GET_QUESTIONNAIRE_QUESTIONS);
//                            CoeusVector cvQuestionsData = (CoeusVector) cvQuesData.get(0);
                    cvQuestionsData = (CoeusVector) cvQuesData.get(0);
                    cvTableData = (CoeusVector) cvQuesData.get(1);
//                            String totalPages = (String) cvQuesData.get(2);
                    totalPages = (String) cvQuesData.get(2);
                }
                // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - End
                if(cvQuestionsData != null && !cvQuestionsData.isEmpty()) {
                    cvData = (CoeusVector)getQuestionsData(cvQuestionsData);
                } else {
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                        // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - Start
                        cvData = new CoeusVector();  
                        questionAnswersTableModel = new QuestionAnswersTableModel();
                        questionAnswersForm.tblQuestions.setModel(questionAnswersTableModel);
                        questionnaireMenusTableModel.setData(new CoeusVector());
                        questionnaireMenusTableModel.fireTableDataChanged();
                        setTableEditors();
                        // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - End
                        // Modified for IACUC Questionnaire implementation - Start
//                        if(questionnaireAnswerHeaderBean.getModuleItemCode() == 7){
//                            CoeusOptionPane.showInfoDialog("This form "
//                                    +answerBean.getLabel()+" is not answered for this protocol ");
//                        } else if(questionnaireAnswerHeaderBean.getModuleItemCode() == 3){
//                            CoeusOptionPane.showInfoDialog("This form "
//                                    +answerBean.getLabel()+" is not answered for this proposal ");
//                        }
                        String warningMessage = CoeusGuiConstants.EMPTY_STRING;
                        String[] qnrLabelAndModuleName = new String[2];
                        qnrLabelAndModuleName[0] = answerBean.getLabel();
                        if(questionnaireAnswerHeaderBean.getModuleItemCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                            qnrLabelAndModuleName[1] = "protocol";
                            warningMessage = getMsgForNotAnsweredModule(qnrLabelAndModuleName);                            
                        } else if(questionnaireAnswerHeaderBean.getModuleItemCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            qnrLabelAndModuleName[1] = "proposal";
                            warningMessage = getMsgForNotAnsweredModule(qnrLabelAndModuleName);
                        }else if(questionnaireAnswerHeaderBean.getModuleItemCode() == ModuleConstants.IACUC_MODULE_CODE){
                            qnrLabelAndModuleName[1] = "protocol";
                            warningMessage = getMsgForNotAnsweredModule(qnrLabelAndModuleName);
                        }
                        CoeusOptionPane.showInfoDialog(warningMessage);
                        // Modified for IACUC Questionnaire implementation - End
                        // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal  
                        if(row != -1 &&  cvTableData != null && row < cvTableData.size()){
                            questionAnswersForm.tblMenus.setRowSelectionInterval(row, row);
                        }
                        
                        //Case#2946 Coeus43 enhancement - start
                        //Added for Questionnaire Printing enhancement
                        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnPrint.setEnabled(true);
//                        questionnaireAnswersBaseController.questionnaireAnswersBaseForm.printMenuItem.setEnabled(true);              
                        if(!isModulelSubmission){
                            
                            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnPrint.setEnabled(true);
                            questionnaireAnswersBaseController.questionnaireAnswersBaseForm.printMenuItem.setEnabled(true);
                        }
                        //COEUSDEV-86 : End
                        //Case#2946 Coeus43 enhancement - end     
                        return;
                    }
                    cvData = new CoeusVector();
                }
                if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                    enableDisableButtons(true);
                } else {
                    enableDisableButtons(false);
                }
                if(page.equals("1")){
                    questionAnswersForm.btnGoBack.setEnabled(false);
                } else if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                    questionAnswersForm.btnGoBack.setEnabled(true);
                }
                if(totalPages != null && totalPages.equals("1")){
//                            questionAnswersForm.btnSaveAndProceed.setEnabled(false);
                    questionAnswersForm.btnSaveAndProceed.setText("Save");
//                        } else if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                } else {
//                            questionAnswersForm.btnSaveAndProceed.setEnabled(true);
                    questionAnswersForm.btnSaveAndProceed.setText("Save & Proceed");
                }
                //Commented for Routing Release - Start
//                        //Case#2946 Coeus43 enhancement - start
//                        //Added for Questionnaire Printing enhancement
                //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnPrint.setEnabled(true);
//                questionnaireAnswersBaseController.questionnaireAnswersBaseForm.printMenuItem.setEnabled(true);
                if(!isModulelSubmission){
                    questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnPrint.setEnabled(true);
                    questionnaireAnswersBaseController.questionnaireAnswersBaseForm.printMenuItem.setEnabled(true);
                }
                //COEUSDEV-86 : End
//                        //Case#2946 Coeus43 enhancement - end
                //Commented for Routing Release - End
                questionAnswersTableModel = new QuestionAnswersTableModel();
                questionAnswersEditor = new QuestionAnswersEditor();
                questionAnswersRenderer = new QuestionAnswersRenderer();
                questionAnswersForm.tblQuestions.setModel(questionAnswersTableModel);
                setTableEditors();
//                int selectedRow = questionAnswersForm.tblMenus.getSelectedRow();
                questionnaireMenusTableModel.setData(cvTableData);
                questionnaireMenusTableModel.fireTableDataChanged();
                // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - Start
//                if(selectedRow != -1 && cvTableData != null && selectedRow < cvTableData.size()){
//                    questionAnswersForm.tblMenus.setRowSelectionInterval(selectedRow, selectedRow);
//                }
                if(row != -1 && cvTableData != null && row < cvTableData.size()){
                    questionAnswersForm.tblMenus.setRowSelectionInterval(row, row);
                }
                
                // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - End
                cvAnswersData = (CoeusVector) ObjectCloner.deepCopy(cvData);
                questionAnswersTableModel.setData(cvData);
                questionAnswersTableModel.fireTableDataChanged();
            }
        } catch (CoeusException ex) {
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        } catch (Exception ex) {
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }
    }
    //COEUSDEV:230 End
    
    // Added for IACUC Questionnaire implementation - Start
    
    /*
     * Method to get message from the property file for the not answered questionnaire for a module
     * @param String[] - qnrLabelAndModuleName(label and module description)
     * @return String - message
     */
    private String getMsgForNotAnsweredModule(String[] qnrLabelAndModuleName){
        MessageFormat formatter = new MessageFormat("");
        String message = formatter.format(
                coeusMessageResources.parseMessageKey(WARNING_QNR_NOT_ANSWERED),qnrLabelAndModuleName);
       return message;
    }
    // Added for IACUC Questionnaire implementation - End
    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * Method to check if an original protocol qnr is editable in amendment/renewal or not
     */
    private boolean isQuestionnaireEditable(){
        boolean editable = true;
        if(isQuestionnairePopulatedFromOriginalProtocol()){
            String questionnaireId = String.valueOf(questionnaireAnswerHeaderBean.getQuestionnaireId());
            HashMap selectedOriginalProtocolQnr = null;
            if(questionnaireAnswersBaseController != null ){
                selectedOriginalProtocolQnr = questionnaireAnswersBaseController.getAllSelectedOriginalProtocolQnr();
            }
            if(selectedOriginalProtocolQnr!=null 
                    && selectedOriginalProtocolQnr.containsKey(questionnaireId)
                    && !(Boolean)selectedOriginalProtocolQnr.get(questionnaireId)){
                editable = false;
                questionnaireAnswersBaseController.questionnaireAnswersBaseForm.btnModify.setEnabled(false);
            }
        }
        return editable;
    }
    
    /**
     * Method to check if the module is irb/iacuc protocol or not.
     */
    private boolean isProtocol(){
        boolean isProtocol = false;
        int moduleCode = questionnaireAnswerHeaderBean.getModuleItemCode();
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE || moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            isProtocol = true;
        }
        return isProtocol;
    }
    
    /**
     * Method to check is the questionnaire is for amendment/renewal in irb/iacuc or not.
     */
    private boolean isAmendmentRenewal(){
        boolean isAmendmentRenewal = false;
        int submoduleCode = questionnaireAnswerHeaderBean.getModuleSubItemCode();
        if(isProtocol() && submoduleCode == 1){
            isAmendmentRenewal = true;
        }
        return isAmendmentRenewal;
    }
    
    /**
     * Method to check if the current questionnaire in amendment/renewal
     * is populated from original protocol or not
     */
    private boolean isQuestionnairePopulatedFromOriginalProtocol(){
        boolean originalProtoQnr = false;
        if(isAmendmentRenewal()){
            int submoduleCode = questionnaireAnswerHeaderBean.getModuleSubItemCode();
            int answeredSubModuleCode = questionnaireAnswerHeaderBean.getApplicableSubmoduleCode();
            if(submoduleCode == 1 && answeredSubModuleCode == 0){
                originalProtoQnr = true;
            }
        }
        return originalProtoQnr;
    }
    
    /**
     * Method to set the mode and header info for amendment/renewal questionnaires
     */
    private void setAmendmentRenewalQuestionnaireProperties(QuestionnaireAnswerHeaderBean answerBean) {
        //If answer is present and submodulecode is 0, its an original protocol questionnaire
        if(answerBean.getApplicableSubmoduleCode() == 0){
            questionAnswersForm.scrPnQuestions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Original Protocol Questionnaire", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        }else{
            questionAnswersForm.scrPnQuestions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Amendment/Renewal Questionnaire", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        }
        if(isQuestionnaireEditable()){
            setFunctionType(originalFunctionType);
        }else{
            setFunctionType(TypeConstants.DISPLAY_MODE);
        }
    }
    // CoeusQA2313: Completion of Questionnaire for Submission - End

  }
