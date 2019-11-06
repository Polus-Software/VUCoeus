/**
 * @(#)AddQuestionnaireQuestionController.java  1.0 September 21, 2006
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AddQuestionnaireQuestionController.java
 *
 * Created on September 21, 2006, 3:56 PM
 */
/* PMD check performed, and commented unused imports and variables on 03-JUL-2007
 * by Leena
 */
package edu.mit.coeus.questionnaire.controller;

//import edu.mit.coeus.admin.gui.QuestionMaintainanceForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.controller.RulesSelectionController;
import edu.mit.coeus.questionnaire.bean.QuestionBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean;
import edu.mit.coeus.questionnaire.bean.QuestionsMaintainanceBean;
import edu.mit.coeus.questionnaire.gui.AddQuestionnaireQuestionForm;
import edu.mit.coeus.subcontract.gui.AmountInfoCommentsForm;
//import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CommentsForm;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Cursor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
//import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

/**
 *
 * @author  vinayks
 */
public class AddQuestionnaireQuestionController extends QuestionnaireController implements ActionListener {
    
    /**
     * Instance of the view
     */
    public AddQuestionnaireQuestionForm addQuestionnaireQuestionForm ;
    /**
     * Instance of the dialog
     */
    public CoeusDlgWindow dlgAddQuestionsForm ;
    /**
     * Instance of MDI form
     */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    /**
     * Holds the Window title
     */
    private static final String ADD_WINDOW_TITLE = "Add Question to Questionnaire";
    private static final String MODIFY_WINDOW_TITLE = "Modify Question";
    //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
    private static final String VIEW_WINDOW_TITLE = "View Question";
    private boolean isDisplayMode = false;
    //COEUSDEV-206 : END
    //Modified for Coeus 4.3 enhancement - Questionnaire Enhancement - start
    /**
     * Holds the Width of the dialog
     */
    //Increased width from 567 to 575
    private static final int WIDTH = 750;
    /**
     * holdsthe height of the dialog
     */
    //Changed the height from 378 to 200
    private static final int HEIGHT = 400;
    //Modified for Coeus 4.3 enhancement - Questionnaire Enhancement - end
    
    /**
     * holds the servlet to communicate with Transaction beans
     */
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final String EMPTY_STRING = "" ;
    private static final String SAVE_CHANGES = "award_exceptionCode.1004";
    private CoeusMessageResources coeusMessageResources;
    /**
     *This is to checck whether ok is clicked or not
     */
    public boolean okClicked ;
    private QuestionBaseBean baseBean;
    //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946
    //to handle the parent question data
    private String parentQuestion;
    private boolean isShown = false;
    private QuestionnaireBean questionnaireBean;
    // 4272: Maintain history of Questionnaires - Start
    private int questionVersion;
    private boolean questionStatus;
    // 4272: Maintain history of Questionnaires - End
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    private int questionnareId, conditionalRuleID;
    private boolean disableRuleCondtion, isConditionalRuleSelected;
    private QuestionnaireUsageBean questionnaireUsageBean;
    private CoeusVector cvRuleData;
    private String conditionalRuleDesc;
    private final String RULE_DESC_POP_WIND_TITLE= "Rule Description" ;
    private final String SELECT_RULE_CONDITION = "questionnaire_exceptionCode.1032";
    private final String SELECT_ATLEAST_ONE_CONDITION = "questionnaire_exceptionCode.1033";
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
    /** Creates a new instance of AddQuestionnaireQuestionController */
    public AddQuestionnaireQuestionController() {
        registerComponents();
        formatFields();
        coeusMessageResources = CoeusMessageResources.getInstance();
        postInitComponents();
    }
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgAddQuestionsForm = new CoeusDlgWindow(mdiForm);
        dlgAddQuestionsForm.setResizable(false);
        dlgAddQuestionsForm.setModal(true);
        dlgAddQuestionsForm.getContentPane().add(addQuestionnaireQuestionForm);
        dlgAddQuestionsForm.setFont(CoeusFontFactory.getLabelFont());
        dlgAddQuestionsForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddQuestionsForm.getSize();
        dlgAddQuestionsForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgAddQuestionsForm.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        dlgAddQuestionsForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                //For validating the question id
                //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
                //When window is opened in display mode no validation is deon
//                isShown = false;
//                if(validateQuestionId()){
//                    performCancelAction();
//                }
                if(isDisplayMode){
                    dlgAddQuestionsForm.dispose();
                    setOkClicked(false);
                }else{
                    isShown = false;
                    if(validateQuestionId()){
                        performCancelAction();
                    }
                    
                }//COEUSDEV-206 : END
            }
        });
        
        dlgAddQuestionsForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAddQuestionsForm.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
            }
            public void windowClosing(WindowEvent we){
                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                //For validating the question id
                //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
                //When window is opened in display mode no validation is deon
//                 isShown = false;
//                 if(validateQuestionId()){
//                     performCancelAction();
//                 }
                if(isDisplayMode){
                    dlgAddQuestionsForm.dispose();
                    setOkClicked(false);
                }else{
                    isShown = false;
                    if(validateQuestionId()){
                        performCancelAction();
                    }
                }//COEUSDEV-206 : END
            }
        });
    }
    
    private void requestDefaultFocus(){
        //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
        //When window is opened in display mode focus is set to question id text field, otherwise focus is set to cancel button
        if(isDisplayMode){
            addQuestionnaireQuestionForm.btnCancel.requestFocusInWindow();
        }else{
            addQuestionnaireQuestionForm.txtQuestionId.requestFocusInWindow();
        }
        
    }
    
    
    /**
     * This method is to show the view
     */
    public void display() {
        dlgAddQuestionsForm.setVisible(true);
    }
    
    /**
     *This method is to perform necessary field format based on mode
     */
    public void formatFields() {
        //addQuestionnaireQuestionForm.txtQuestionId.setDocument(new LimitedPlainDocument(10));
        addQuestionnaireQuestionForm.txtArQuestion.setEnabled(true);
        addQuestionnaireQuestionForm.txtArQuestion.setEditable(false);
        addQuestionnaireQuestionForm.txtArQuestion.setWrapStyleWord(true);
        addQuestionnaireQuestionForm.txtArQuestion.setLineWrap(true);
        addQuestionnaireQuestionForm.txtArQuestion.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        addQuestionnaireQuestionForm.txtArQuestion.setDocument(new LimitedPlainDocument(2000));
        addQuestionnaireQuestionForm.txtArQuestion.setFont(new CoeusFontFactory().getNormalFont());
        //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
        //For Parent Question Link
        addQuestionnaireQuestionForm.lblPreviousQuestion.addMouseListener(new MouseAdapter() {
            final Color COLOR_NORMAL    = Color.BLUE;
            final Color COLOR_HOVER     = Color.RED;
            final Color COLOR_ACTIVE    = COLOR_NORMAL;
            final Color COLOR_BG_NORMAL = Color.LIGHT_GRAY;
            final Color COLOR_BG_ACTIVE = Color.LIGHT_GRAY;
            
            public void mouseClicked(MouseEvent e) {
                if(addQuestionnaireQuestionForm.lblPreviousQuestion.isEnabled()){
                    AmountInfoCommentsForm descForm
                            = new AmountInfoCommentsForm("Question");
                    descForm.setData(parentQuestion);
                    descForm.display();
                }
            }
            
            public void mouseEntered(MouseEvent e) {
                if(addQuestionnaireQuestionForm.lblPreviousQuestion.isEnabled()){
                    addQuestionnaireQuestionForm.lblPreviousQuestion.setForeground(COLOR_HOVER);
                    addQuestionnaireQuestionForm.lblPreviousQuestion.setBackground(COLOR_BG_ACTIVE);
                    Cursor cursor = addQuestionnaireQuestionForm.lblPreviousQuestion.getCursor();
                    addQuestionnaireQuestionForm.lblPreviousQuestion.setCursor(cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            public void mouseExited(MouseEvent me) {
                if(addQuestionnaireQuestionForm.lblPreviousQuestion.isEnabled()){
                    addQuestionnaireQuestionForm.lblPreviousQuestion.setForeground(COLOR_NORMAL);
                    addQuestionnaireQuestionForm.lblPreviousQuestion.setBackground(COLOR_BG_NORMAL);
                    Cursor cursor = addQuestionnaireQuestionForm.lblPreviousQuestion.getCursor();
                    addQuestionnaireQuestionForm.lblPreviousQuestion.setCursor(cursor.getDefaultCursor());
                }
            }
        });
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        addQuestionnaireQuestionForm.chkPreviousSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(addQuestionnaireQuestionForm.chkPreviousSelect.isSelected()){
                    addQuestionnaireQuestionForm.txtConditionValue.setEnabled(true);
                    addQuestionnaireQuestionForm.cmbCondition.setEnabled(true);
                }else{
                    addQuestionnaireQuestionForm.txtConditionValue.setEnabled(false);
                    addQuestionnaireQuestionForm.txtConditionValue.setText(CoeusGuiConstants.EMPTY_STRING);
                    addQuestionnaireQuestionForm.cmbCondition.setEnabled(false);
                }
            }
        });
        addQuestionnaireQuestionForm.chkRuleSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(addQuestionnaireQuestionForm.chkRuleSelect.isSelected() && !isDisplayMode){
                    addQuestionnaireQuestionForm.lblSelectRule.setEnabled(true);
                    addQuestionnaireQuestionForm.lblSelectRule.setText("<html><U>Select a Rule</U></html>");
                }else{
                    conditionalRuleID = 0;
                    conditionalRuleDesc = CoeusGuiConstants.EMPTY_STRING;
                    addQuestionnaireQuestionForm.lblSelectRule.setEnabled(false);
                    addQuestionnaireQuestionForm.lblSelectRule.setText("Select a Rule");
                    addQuestionnaireQuestionForm.txtRuleDesc.setText(CoeusGuiConstants.EMPTY_STRING);
                }
            }
        });
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        final CommentsForm commentsForm = new CommentsForm(RULE_DESC_POP_WIND_TITLE);
        addQuestionnaireQuestionForm.btnRuleDesc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commentsForm.setData(addQuestionnaireQuestionForm.txtRuleDesc.getText());
                commentsForm.display();
            }
        });
        
        //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        if(!isDisplayMode){
            addQuestionnaireQuestionForm.lblSelectRule.setEnabled(false);
            addQuestionnaireQuestionForm.lblSelectRule.setText("Select a Rule");
            addQuestionnaireQuestionForm.lblSelectRule.addMouseListener(new MouseAdapter() {
                
                final Color COLOR_NORMAL    = Color.BLUE;
                final Color COLOR_HOVER     = Color.RED;
                final Color COLOR_ACTIVE    = COLOR_NORMAL;
                final Color COLOR_BG_NORMAL = Color.LIGHT_GRAY;
                final Color COLOR_BG_ACTIVE = Color.LIGHT_GRAY;
                
                public void mouseClicked(MouseEvent e) {
                    if(addQuestionnaireQuestionForm.lblSelectRule.isEnabled()){
                        try{
                            Equals eqModuleCode = new Equals("moduleCode", ""+getQuestionnaireUsageBean().getModuleItemCode());
                            Equals eqSubModuleCode = new Equals("submoduleCode", ""+getQuestionnaireUsageBean().getModuleSubItemCode());
                            And andData = new And(eqModuleCode, eqSubModuleCode);
                            CoeusVector filteredVector = cvRuleData.filter(andData);
                            
                            RulesSelectionController rulesSelectionController = new RulesSelectionController();
                            rulesSelectionController.setFormDataForQuestion(filteredVector);
                            rulesSelectionController.display();
                            CoeusVector cvData = (CoeusVector)rulesSelectionController.getFormData();
                            if(cvData != null && !cvData.isEmpty()){
                                BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvData.get(1);
                                conditionalRuleID = Integer.parseInt(businessRuleBean.getRuleId());
                                conditionalRuleDesc = businessRuleBean.getDescription();
                                addQuestionnaireQuestionForm.txtRuleDesc.setText(conditionalRuleDesc);
                                addQuestionnaireQuestionForm.txtRuleDesc.setCaretPosition(0);
                                isConditionalRuleSelected = true;
                            }
                        }catch(Exception ex){
                            
                        }
                    }
                }
                
                public void mouseEntered(MouseEvent e) {
                    if(addQuestionnaireQuestionForm.lblSelectRule.isEnabled()){
                        addQuestionnaireQuestionForm.lblSelectRule.setForeground(COLOR_HOVER);
                        addQuestionnaireQuestionForm.lblSelectRule.setBackground(COLOR_BG_ACTIVE);
                        Cursor cursor = addQuestionnaireQuestionForm.lblSelectRule.getCursor();
                        addQuestionnaireQuestionForm.lblSelectRule.setCursor(cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                }
                public void mouseExited(MouseEvent me) {
                    if(addQuestionnaireQuestionForm.lblSelectRule.isEnabled()){
                        addQuestionnaireQuestionForm.lblSelectRule.setForeground(COLOR_NORMAL);
                        addQuestionnaireQuestionForm.lblSelectRule.setBackground(COLOR_BG_NORMAL);
                        Cursor cursor = addQuestionnaireQuestionForm.lblSelectRule.getCursor();
                        addQuestionnaireQuestionForm.lblSelectRule.setCursor(cursor.getDefaultCursor());
                    }
                }
            });
        }else{
            addQuestionnaireQuestionForm.lblSelectRule.setEnabled(false);
            addQuestionnaireQuestionForm.lblSelectRule.setText("Select a Rule");
        }
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - end
        addQuestionnaireQuestionForm.btnOK.setMnemonic('O');
        addQuestionnaireQuestionForm.btnCancel.setMnemonic('C');
        addQuestionnaireQuestionForm.btnSearch.setMnemonic('S');
    }
    
    /** This method returns the instance of view
     * @return
     */
    public java.awt.Component getControlledUI() {
        return addQuestionnaireQuestionForm;
    }
    
    /** Check whether data is changed in the screen or not. If changed then
     *set the flag to true and ask for the save confirmation
     */
    private boolean isDataChanged(){
        boolean dataModified = false;
        String qId  = addQuestionnaireQuestionForm.txtQuestionId.getText();
        boolean isSelected = addQuestionnaireQuestionForm.rdBtnCondition.isSelected();
        boolean isPreviousSelected = addQuestionnaireQuestionForm.chkPreviousSelect.isSelected();
        boolean isRuleCondSelected = addQuestionnaireQuestionForm.chkRuleSelect.isSelected();
        if(conditionalRuleID != questionnaireBean.getConditionRuleId()){
            isRuleCondSelected = true;
        }else{
            isRuleCondSelected = false;
        }
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        //For save confirmation
//        if(baseBean!= null && !qId.equals(EMPTY_STRING)){
//            int questionId = ((Integer)baseBean.getQuestionId()).intValue();
        if(questionnaireBean!= null && !qId.equals(EMPTY_STRING)){
            if(questionnaireBean.getAcType() != null
                    && questionnaireBean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    && dlgAddQuestionsForm.getTitle().equals(ADD_WINDOW_TITLE)){
                dataModified = true;
            }
            int questionId = ((Integer)questionnaireBean.getQuestionId()).intValue();
            int formQuestionId = Integer.parseInt(qId);
            if(questionId != formQuestionId || isSelected != questionnaireBean.isConditionalFlag() ||
                    isPreviousSelected != questionnaireBean.isPreviousQuestionFlag() || isRuleCondSelected != questionnaireBean.isRuleSelectionFlag()){
                dataModified = true;
            }
        }else{
            if(!qId.equals(EMPTY_STRING) || isSelected){
                dataModified = true;
            }
        }
        return dataModified;
    }
    /**
     * To get the user entered values
     * @return Object
     */
    public Object getFormData() {
        //Code commented and modified for coeus4.3 enhancement - starts
//        QuestionBaseBean questionBaseBean = new QuestionBaseBean();
//        String questionId = addQuestionnaireQuestionForm.txtQuestionId.getText();
//        questionBaseBean.setQuestionId(new Integer(questionId));
//        questionBaseBean.setDescription(addQuestionnaireQuestionForm.txtArQuestion.getText());
        String questionId = addQuestionnaireQuestionForm.txtQuestionId.getText();
        int ruleId = questionnaireBean.getConditionRuleId();
        String ruleDesc = questionnaireBean.getConditionRuleDesc();
        QuestionnaireBean questionnaireBean = new QuestionnaireBean();
        questionnaireBean.setQuestionId(new Integer(questionId));
        // 4272: Maintain history of Questionnaires - Start
        questionnaireBean.setQuestionVersionNumber(new Integer(questionVersion));
        questionnaireBean.setQuestionStatus(questionStatus);
        // 4272: Maintain history of Questionnaires - End
        questionnaireBean.setDescription(addQuestionnaireQuestionForm.txtArQuestion.getText());
        questionnaireBean.setConditionalFlag(addQuestionnaireQuestionForm.rdBtnCondition.isSelected());
        if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected() &&
                addQuestionnaireQuestionForm.chkPreviousSelect.isSelected()){
            questionnaireBean.setCondition((String) addQuestionnaireQuestionForm.cmbCondition.getSelectedItem());
            questionnaireBean.setConditionValue(addQuestionnaireQuestionForm.txtConditionValue.getText());
        } else {
            questionnaireBean.setCondition(EMPTY_STRING);
            questionnaireBean.setConditionValue(EMPTY_STRING);
        }
        if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected() && addQuestionnaireQuestionForm.chkRuleSelect.isSelected()){
            if(isConditionalRuleSelected){
                questionnaireBean.setConditionRuleId(conditionalRuleID);
                questionnaireBean.setConditionRuleDesc(addQuestionnaireQuestionForm.txtRuleDesc.getText());
                isConditionalRuleSelected = false;
            }else{
                questionnaireBean.setConditionRuleId(ruleId);
                questionnaireBean.setConditionRuleDesc(ruleDesc);
                
            }
        }else{
            questionnaireBean.setConditionRuleId(0);
        }
//        return questionBaseBean;
        return questionnaireBean;
        //Code commented and modified for coeus4.3 enhancement - starts
    }
    
    /**
     * In this method the Listeners are registered withe the components
     */
    public void registerComponents() {
        addQuestionnaireQuestionForm = new AddQuestionnaireQuestionForm();
        // Added for Coeus 4.3 enhancement - Questionnaire enhancement - start
        // Initially loads the data without the condition components
        enableConditionComponents(false);
        //Add action listener to the condition checkbox
        addQuestionnaireQuestionForm.btnCondition.add(addQuestionnaireQuestionForm.rdBtnAlways);
        addQuestionnaireQuestionForm.btnCondition.add(addQuestionnaireQuestionForm.rdBtnCondition);
        addQuestionnaireQuestionForm.rdBtnAlways.addActionListener(this);
        addQuestionnaireQuestionForm.rdBtnCondition.addActionListener(this);
        addQuestionnaireQuestionForm.txtQuestionId.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 10));
        //Added for Coeus 4.3 enhancement - Questionnaire enhancement - end
        addQuestionnaireQuestionForm.btnOK.addActionListener(this);
        addQuestionnaireQuestionForm.btnCancel.addActionListener(this);
        addQuestionnaireQuestionForm.btnSearch.addActionListener(this);
        //Code commented for coeus4.3 Questionnaire Maintenance enhancement.
        addQuestionnaireQuestionForm.txtQuestionId.addFocusListener( new CustomFocusAdapter());
        /** Code for focus traversal - start */
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        java.awt.Component[] components = {addQuestionnaireQuestionForm.txtQuestionId ,
        addQuestionnaireQuestionForm.btnSearch, addQuestionnaireQuestionForm.rdBtnAlways,
        addQuestionnaireQuestionForm.rdBtnCondition, addQuestionnaireQuestionForm.cmbCondition,
        addQuestionnaireQuestionForm.txtConditionValue, addQuestionnaireQuestionForm.btnOK ,
        addQuestionnaireQuestionForm.btnCancel};

        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        addQuestionnaireQuestionForm.setFocusTraversalPolicy(traversePolicy);
        addQuestionnaireQuestionForm.setFocusCycleRoot(true);
    }
    
    // Added for Coeus 4.3 enhancement - Questionnaire enhancement - start
    /**
     * Enable or disable the condition combobox, condition label and condition
     * textfield according to the value enable
     * @param visible - true to make visible, else false
     */
    public void enableConditionComponents(boolean visible){
        addQuestionnaireQuestionForm.chkPreviousSelect.setEnabled(visible);
        addQuestionnaireQuestionForm.chkRuleSelect.setEnabled(visible);
        
        addQuestionnaireQuestionForm.chkPreviousSelect.setSelected(visible);
        if(!visible){
            addQuestionnaireQuestionForm.chkRuleSelect.setSelected(visible);
        }
        addQuestionnaireQuestionForm.cmbCondition.setEnabled(visible);
        addQuestionnaireQuestionForm.txtConditionValue.setEnabled(visible);
        
        //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window - Start
        if(!visible){
            setComponentBackGroundToDisabled(addQuestionnaireQuestionForm.txtConditionValue);
        }
        //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window - End
    }
    //Added for Coeus 4.3 enhancement - Questionnaire enhancement - end
    
    /**
     *
     * @throws CoeusException
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**This method is to set the data to form components
     * @param data
     * @throws CoeusException
     */
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        QuestionBaseBean questionBaseBean = (QuestionBaseBean)data;
        this.baseBean = questionBaseBean;
        if(questionBaseBean!=null){
            dlgAddQuestionsForm.setTitle(MODIFY_WINDOW_TITLE);
            addQuestionnaireQuestionForm.txtQuestionId.setText(EMPTY_STRING+questionBaseBean.getQuestionId());
            addQuestionnaireQuestionForm.txtArQuestion.setText(questionBaseBean.getDescription());
        }else{
            dlgAddQuestionsForm.setTitle(ADD_WINDOW_TITLE);
            addQuestionnaireQuestionForm.txtQuestionId.setText(EMPTY_STRING);
            addQuestionnaireQuestionForm.txtArQuestion.setText(EMPTY_STRING);
        }
    }
    
    
    /**This method is to set the data to form components
     * @param questionnaireBean
     * @throws CoeusException
     */
    public void setFormData(QuestionnaireBean questionnaireBean) throws edu.mit.coeus.exception.CoeusException {
        if(questionnaireBean!=null){
            //Coe commented and added for coeus4.3 Questionnaire Maintenance enhancement case#2946
            //To display the window title
//            dlgAddQuestionsForm.setTitle(MODIFY_WINDOW_TITLE);
            //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
            //Window is displayed in display mode
            addQuestionnaireQuestionForm.rdBtnCondition.setText(coeusMessageResources.parseLabelKey("questionnaireConditional.1000"));
            if(questionnaireBean!= null && isDisplayMode == true){
                dlgAddQuestionsForm.setTitle(VIEW_WINDOW_TITLE);
                addQuestionnaireQuestionForm.txtQuestionId.setEnabled(false);
                addQuestionnaireQuestionForm.txtQuestionId.setEditable(false);
                addQuestionnaireQuestionForm.txtArQuestion.setEnabled(false);
                addQuestionnaireQuestionForm.txtArQuestion.setEditable(false);
                addQuestionnaireQuestionForm.btnSearch.setEnabled(false);
                addQuestionnaireQuestionForm.rdBtnAlways.setEnabled(false);
                addQuestionnaireQuestionForm.rdBtnCondition.setEnabled(false);
                addQuestionnaireQuestionForm.btnOK.setEnabled(false);
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                addQuestionnaireQuestionForm.lblSelectRule.setEnabled(false);
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - end
            }else//COEUSDEV-206 : END
                if(questionnaireBean.getAcType() != null
                    && questionnaireBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                dlgAddQuestionsForm.setTitle(MODIFY_WINDOW_TITLE);
                }else if(questionnaireBean.getAcType() != null
                    && questionnaireBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                dlgAddQuestionsForm.setTitle(ADD_WINDOW_TITLE);
                }
            addQuestionnaireQuestionForm.txtQuestionId.setText(EMPTY_STRING+questionnaireBean.getQuestionId());
            addQuestionnaireQuestionForm.txtArQuestion.setText(questionnaireBean.getDescription());
            //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
            if(questionnaireBean.isConditionalFlag()){
                addQuestionnaireQuestionForm.rdBtnCondition.setSelected(true);
                addQuestionnaireQuestionForm.rdBtnAlways.setSelected(false);
            } else {
                addQuestionnaireQuestionForm.rdBtnCondition.setSelected(false);
                addQuestionnaireQuestionForm.rdBtnAlways.setSelected(true);
            }
            addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("CONTAINS");
            if(questionnaireBean.getCondition() != null){
                if(questionnaireBean.getCondition().equals(("="))){
                    addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("EQUAL TO");
                } else if(questionnaireBean.getCondition().equals((">"))){
                    addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("GREATER THAN");
                } else if(questionnaireBean.getCondition().equals(("<"))){
                    addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("LESS THAN");
                } else if(questionnaireBean.getCondition().equals((">="))){
                    addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("GREATER THAN EQUAL");
                } else if(questionnaireBean.getCondition().equals(("<="))){
                    addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("LESS THAN EQUAL");
                } else if(questionnaireBean.getCondition().equals(("!="))){
                    addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("NOT EQUAL");
                } else {
                    addQuestionnaireQuestionForm.cmbCondition.setSelectedItem(questionnaireBean.getCondition());
                }
            }
            addQuestionnaireQuestionForm.txtConditionValue.setText(questionnaireBean.getConditionValue());
            addQuestionnaireQuestionForm.txtRuleDesc.setText(questionnaireBean.getConditionRuleDesc());
            addQuestionnaireQuestionForm.txtRuleDesc.setCaretPosition(0);
            //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
            //Disable condition combo-box and condition value text field is window is opened in display mode
            if(questionnaireBean!= null && isDisplayMode){
//                    enableConditionComponents(false);
                if(questionnaireBean.getCondition() != null &&
                        questionnaireBean.getConditionValue() != null &&
                        !CoeusGuiConstants.EMPTY_STRING.equals(questionnaireBean.getConditionValue())){
                    addQuestionnaireQuestionForm.chkPreviousSelect.setEnabled(false);
                    addQuestionnaireQuestionForm.chkPreviousSelect.setSelected(true);
                    addQuestionnaireQuestionForm.cmbCondition.setEnabled(false);
                    addQuestionnaireQuestionForm.txtConditionValue.setEnabled(false);
                }
                if(questionnaireBean.getConditionRuleId() != 0){
                    addQuestionnaireQuestionForm.chkRuleSelect.setEnabled(false);
                    addQuestionnaireQuestionForm.chkRuleSelect.setSelected(true);
                    addQuestionnaireQuestionForm.txtRuleDesc.setText(questionnaireBean.getConditionRuleDesc());
                }
            }else if(!addQuestionnaireQuestionForm.rdBtnCondition.isSelected()){
                enableConditionComponents(addQuestionnaireQuestionForm.rdBtnCondition.isSelected());
                
            }else if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected()){
                if(questionnaireBean.getCondition() != null &&
                        questionnaireBean.getConditionValue() != null &&
                        !CoeusGuiConstants.EMPTY_STRING.equals(questionnaireBean.getConditionValue())){
                    addQuestionnaireQuestionForm.chkPreviousSelect.setSelected(true);
                    addQuestionnaireQuestionForm.cmbCondition.setEnabled(true);
                    addQuestionnaireQuestionForm.txtConditionValue.setEnabled(true);
                }
                addQuestionnaireQuestionForm.chkRuleSelect.setEnabled(true);
                if(questionnaireBean.getConditionRuleId() != 0){
                    addQuestionnaireQuestionForm.chkRuleSelect.setSelected(true);
                }else{
                    conditionalRuleDesc = CoeusGuiConstants.EMPTY_STRING;
                    addQuestionnaireQuestionForm.lblSelectRule.setEnabled(false);
                    addQuestionnaireQuestionForm.lblSelectRule.setText("Select a Rule");
                }
                addQuestionnaireQuestionForm.chkPreviousSelect.setEnabled(true);
                
            }
            if(getQuestionnaireUsageBean() == null){
                addQuestionnaireQuestionForm.chkPreviousSelect.setEnabled(false);
                if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected()){
                    addQuestionnaireQuestionForm.chkPreviousSelect.setSelected(true);
                }
                addQuestionnaireQuestionForm.pnlRulePanel.setVisible(false);
//                addQuestionnaireQuestionForm.chkRuleSelect.setVisible(false);
//                addQuestionnaireQuestionForm.lblSelectRule.setVisible(false);
            }
            this.questionnaireBean = questionnaireBean;
            //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
            baseBean = new QuestionBaseBean();
            baseBean.setQuestionId(questionnaireBean.getQuestionId());
        }else{
            dlgAddQuestionsForm.setTitle(ADD_WINDOW_TITLE);
            addQuestionnaireQuestionForm.txtQuestionId.setText(EMPTY_STRING);
            addQuestionnaireQuestionForm.txtArQuestion.setText(EMPTY_STRING);
            //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
            addQuestionnaireQuestionForm.rdBtnCondition.setSelected(false);
            addQuestionnaireQuestionForm.rdBtnAlways.setSelected(true);
            addQuestionnaireQuestionForm.cmbCondition.setSelectedItem("CONTAINS");
            addQuestionnaireQuestionForm.txtConditionValue.setText(EMPTY_STRING);
            enableConditionComponents(false);
            questionnaireBean = new QuestionnaireBean();
            //Added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        }
    }
    
    /**This method is to perform neccessary form validations
     * @throws CoeusUIException
     * @return boolean
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false ;
    }
    
    /**This method performs neccessary action on the action event
     * @param ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent event) {
        Object source = event.getSource();
        try{
            if(source.equals(addQuestionnaireQuestionForm.btnOK)){
                performOKAction();
            }else if(source.equals(addQuestionnaireQuestionForm.btnCancel)){
                //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires	                //COEUSDEV-206
//                performCancelAction();
                if(isDisplayMode){
                    dlgAddQuestionsForm.dispose();
                    setOkClicked(false);
                }else{
                    performCancelAction();
                }//COEUSDEV-206 : END
            }else if(source.equals(addQuestionnaireQuestionForm.btnSearch)){
                showQuestionnaireSearch();
            }
            // Added for Coeus 4.3 enhancement - Questionnaire enhancement - start
            //Show the condition components only if the conditional checkbox is checked.
            else if(source.equals(addQuestionnaireQuestionForm.rdBtnCondition)){
                if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected()){
                    enableConditionComponents(true);
                    if(questionnaireUsageBean == null){
                        addQuestionnaireQuestionForm.chkPreviousSelect.setSelected(true);
                        addQuestionnaireQuestionForm.chkPreviousSelect.setEnabled(false);
                    }
                }
            } else if(source.equals(addQuestionnaireQuestionForm.rdBtnAlways)){
                if(addQuestionnaireQuestionForm.rdBtnAlways.isSelected()){
                    addQuestionnaireQuestionForm.txtConditionValue.setText(EMPTY_STRING);
                    enableConditionComponents(false);
                }
            }
            // Added for Coeus 4.3 enhancement - Questionnaire enhancement - end
            
        }catch (Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    
    public void performOKAction(){
        String questionId = addQuestionnaireQuestionForm.txtQuestionId.getText();
        if(questionId == null || questionId.equals(EMPTY_STRING)){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1010"));
            //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
            //To set the focus to the questionnaire id field
            setRequestFocusInThread(addQuestionnaireQuestionForm.txtQuestionId);
            return ;
            // Code added for coeus4.3 Questionnaire Maintenance enhancement.
        } else if(!validateQuestionId()) {
            return;
        }
        //Code added for coeus4.3 enhancement - starts
        //To validate the entered text is number or not,
        //if the selected condition is other than = and !=
        if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected() && addQuestionnaireQuestionForm.chkPreviousSelect.isSelected()){
            String conditionValue = addQuestionnaireQuestionForm.txtConditionValue.getText().trim();
            if(conditionValue == null || conditionValue.equals(EMPTY_STRING)){
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1011"));
                return;
            }
        }
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected() && addQuestionnaireQuestionForm.chkRuleSelect.isSelected()){
            if(conditionalRuleID == 0 && questionnaireBean.getConditionRuleId() == 0){
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(SELECT_RULE_CONDITION));
                return;
            }
        }
        if(addQuestionnaireQuestionForm.rdBtnCondition.isSelected() &&
                !addQuestionnaireQuestionForm.chkRuleSelect.isSelected() &&
                !addQuestionnaireQuestionForm.chkPreviousSelect.isSelected()){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(SELECT_ATLEAST_ONE_CONDITION));
            return;
        }
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        //Code added for coeus4.3 enhancement - ends
        dlgAddQuestionsForm.dispose();
        setOkClicked(true);
    }
    
    /**
     * This window performs cancel action
     */
    public void performCancelAction(){
        if(isDataChanged()){
            int selection = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                    SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                performOKAction();
            }else if(selection == CoeusOptionPane.SELECTION_CANCEL){
                // Don't do anything. Remain the window
            }else{
                dlgAddQuestionsForm.dispose();
                setOkClicked(false);
            }
        }else{
            dlgAddQuestionsForm.dispose();
            setOkClicked(false);
        }
    }
    
    private void showQuestionnaireSearch() throws Exception {
//        CoeusSearch questionSearch =  new CoeusSearch( mdiForm, "QUESTION_SEARCH",
//                CoeusSearch.TWO_TABS );
//        questionSearch.showSearchWindow();
//        Vector vSelectedPersons = questionSearch.getMultipleSelectedRows();
        
        
        
        QuestionMaintenanceController questionMaintenanceController =
                new QuestionMaintenanceController("Questions",mdiForm.getUnitNumber(),true);
        questionMaintenanceController.setFormData(null);
        questionMaintenanceController.display();
        QuestionBaseBean questBaseBean = questionMaintenanceController.getQuestionForQuestionnaire();
        if(questionMaintenanceController.isOkClicked()){
            setFormData(questBaseBean);
        }
    }
    
    
//        QuestionBaseBean questBaseBean = prepareFormData(vSelectedPersons);
//        setFormData(questBaseBean);
//    }
    
    /**This Class is to implement the methods of focus listener
     */
    private class CustomFocusAdapter extends FocusAdapter{
        
        /**
         * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
         * Method to implement actions on components after focus gained
         * @param fe
         */
        public void focusGained(FocusEvent fe){
            isShown = false;
        }
        /**
         * Method to implement actions on components after focus lost
         */
        public void focusLost(FocusEvent fe){
            //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
            //if the error message is shown then to skip the validation
            if(isShown){
                return;
            }
            String questionId = addQuestionnaireQuestionForm.txtQuestionId.getText();
            if (fe.getSource() instanceof CoeusTextField){
                try{
                    if(questionId!=null && !questionId.trim().equals(EMPTY_STRING)){
                        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                        //For save confirmation
                        isDataChanged();
                        int questId = Integer.parseInt(questionId.trim());
                        // 4272: Maintain history of Questionnaires - Start
//                        QuestionBaseBean qBaseBean = getQuestionsData(questId);
                        QuestionsMaintainanceBean qBaseBean = (QuestionsMaintainanceBean) getQuestionsData(questId);
                        // 4272: Maintain history of Questionnaires - End
                        if(qBaseBean!=null){
                            addQuestionnaireQuestionForm.txtArQuestion.setText(qBaseBean.getDescription());
                            // 4272: Maintain history of Questionnaires - Start
                            questionVersion = qBaseBean.getVersionNumber();
                            String strStatus = qBaseBean.getStatus();
                            if("A".equalsIgnoreCase(strStatus)){
                                questionStatus = true;
                            } else {
                                questionStatus = false;
                            }
                            // 4272: Maintain history of Questionnaires - End
                        }else{
                            CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1014"));
                            addQuestionnaireQuestionForm.txtArQuestion.setText(EMPTY_STRING);
                            setRequestFocusInThread(addQuestionnaireQuestionForm.txtQuestionId);
                        }
                    } else {
                        addQuestionnaireQuestionForm.txtArQuestion.setText(EMPTY_STRING);
                    }
                }catch(NumberFormatException numFormatException){
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1014"));
                    setRequestFocusInThread(addQuestionnaireQuestionForm.txtQuestionId);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
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
    
    /**This method return the question details for particular questionId
     * @param questionId
     * @throws CoeusException
     * @return QuestionBaseBean questBaseBean
     */
    public QuestionBaseBean getQuestionsData(int questionId) throws CoeusException{
        Vector dataObjects = new Vector();
        CoeusVector cvQuestionsData = null ;
        RequesterBean requester = new RequesterBean();
        dataObjects.add(0, new Integer(questionId));
        requester.setDataObjects(dataObjects);
        requester.setFunctionType('B');
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            cvQuestionsData  = (CoeusVector)response.getDataObject();
        }
        QuestionBaseBean questBaseBean = null ;
        if(cvQuestionsData!=null && cvQuestionsData.size() > 0){
            questBaseBean = (QuestionBaseBean)cvQuestionsData.get(0);
        }
        return questBaseBean;
    }
    
    /**This method prepares QuestionBaseBean from the Search results
     * @param vecQuestionData
     * @return QuestionBaseBean qBaseBean
     */
    public QuestionBaseBean prepareFormData(Vector vecQuestionData){
        QuestionBaseBean qBaseBean = null ;
        if(vecQuestionData!=null && vecQuestionData.size() > 0) {
            qBaseBean = new QuestionBaseBean();
            HashMap hmQuestionData = (HashMap) vecQuestionData.get(0);
            String questionId = hmQuestionData.get("QUESTION_ID").toString();
            String question =  hmQuestionData.get("QUESTION").toString();
            qBaseBean.setQuestionId(new Integer(Integer.parseInt(questionId)));
            qBaseBean.setDescription(question);
        }
        return qBaseBean;
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
    
    /**
     * Method to validate the QuestionId
     * @return boolean Gives QuestionId valid or not.
     */
    public boolean validateQuestionId(){
        boolean isValid = true;
        String questionId = addQuestionnaireQuestionForm.txtQuestionId.getText();
        try{
            if(questionId!=null && !questionId.trim().equals(EMPTY_STRING)){
                int questId = Integer.parseInt(questionId.trim());
                // 4272: Maintain history of Questionnaires - Start
//                        QuestionBaseBean qBaseBean = getQuestionsData(questId);
                QuestionsMaintainanceBean qBaseBean = (QuestionsMaintainanceBean) getQuestionsData(questId);
                // 4272: Maintain history of Questionnaires - End
                if(qBaseBean!=null){
                    addQuestionnaireQuestionForm.txtArQuestion.setText(qBaseBean.getDescription());
                    // 4272: Maintain history of Questionnaires - Start
                    questionVersion = qBaseBean.getVersionNumber();
                    String strStatus = qBaseBean.getStatus();
                    if("A".equalsIgnoreCase(strStatus)){
                        questionStatus = true;
                    } else {
                        questionStatus = false;
                    }
                    // 4272: Maintain history of Questionnaires - End
                }else{
                    //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                    isShown = true;
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1014"));
                    addQuestionnaireQuestionForm.txtArQuestion.setText(EMPTY_STRING);
                    setRequestFocusInThread(addQuestionnaireQuestionForm.txtQuestionId);
                    isValid = false;
                }
            }
        }catch(NumberFormatException numFormatException){
            //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
            isShown = true;
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1014"));
            setRequestFocusInThread(addQuestionnaireQuestionForm.txtQuestionId);
            isValid = false;
        }catch(Exception ex){
            ex.printStackTrace();
            isValid = false;
        }
        return isValid;
    }
    
    /**
     * Getter for property parentQuestion.
     * @return Value of property parentQuestion.
     */
    public String getParentQuestion() {
        return parentQuestion;
    }
    
    /**
     * Setter for property parentQuestion.
     * @param parentQuestion New value of property parentQuestion.
     */
    public void setParentQuestion(String parentQuestion) {
        this.parentQuestion = parentQuestion;
    }
    
    //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
    /*
     * Method to set window mode either display or edit
     * @param isDisplayMode
     */
    public void setIsDisplayMode(boolean isDisplayMode){
        this.isDisplayMode = isDisplayMode;
    }
    //COEUSDEV-206 : END
    //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window - Start
    private void setComponentBackGroundToDisabled(JComponent component){
        Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
                getDefaults().get("Panel.background");
        component.setBackground(disabledBackground);
    }
    //COEUSDEV-231 - Answer Length column is editable when question details window is opened in display mode from questionnaire window - End
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /**
     * Method to get questionnaire id
     * @return questionnareId
     */
    public int getQuestionnareId() {
        return questionnareId;
    }
    
    /**
     * Method to set questionnaire id
     * @param questionnareId
     */
    public void setQuestionnareId(int questionnareId) {
        this.questionnareId = questionnareId;
    }
    
    /**
     * Method to get rule condition is disabled based on the questionnaire usage
     * @return disableRuleCondtion
     */
    public boolean isDisableRuleCondtion() {
        return disableRuleCondtion;
    }
    
    /**
     * Method to disable rule condition based on the questionnaire usage
     * @param disableRuleCondtion
     */
    public void setDisableRuleCondtion(boolean disableRuleCondtion) {
        this.disableRuleCondtion = disableRuleCondtion;
    }
    
    
    /**
     * Method to get the questionnaire bean for selecting a rule
     * @return questionnaireUsageBean
     */
    public QuestionnaireUsageBean getQuestionnaireUsageBean() {
        return questionnaireUsageBean;
    }
    
    /**
     * Method to set the questionnaire bean for selecting a rule
     * @param questionnaireUsageBean
     */
    public void setQuestionnaireUsageBean(QuestionnaireUsageBean questionnaireUsageBean) {
        this.questionnaireUsageBean = questionnaireUsageBean;
    }
    
    /**
     * Method to get the rule data
     * @return cvRuleData
     */
    public CoeusVector getRuleData() {
        return cvRuleData;
    }
    
    /**
     * Method to get the rule data
     * @param cvRuleData
     */
    public void setRuleData(CoeusVector cvRuleData) {
        this.cvRuleData = cvRuleData;
    }
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
}
