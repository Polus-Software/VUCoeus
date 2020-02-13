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
 * QuestionnaireDetailController.java
 *
 * Created on September 19, 2006, 3:51 PM
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
//import edu.mit.coeus.questionnaire.bean.QuestionBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireMaintainaceBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTemplateBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean;
import edu.mit.coeus.questionnaire.bean.QuestionsMaintainanceBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.questionnaire.gui.QuestionnaireDetailForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Operator;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireDetailController extends QuestionnaireController implements ActionListener{
    
    
    private QuestionnaireDetailForm questionnaireDetailForm;
    private DefaultTreeModel questionnaireTreeModel;
    
    private JTree qTree;
    private char functionType;
    private QueryEngine queryEngine;
    
    private QuestionnaireTreeRenderer questionnaireTreeRenderer;
    private java.awt.Color backGroundColor;
    private QuestionnaireNode selectedNode;
    //holds current tree selection path
    private TreePath selTreePath;
    private boolean modified;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    private CoeusMessageResources  coeusMessageResources;
    
    private CoeusVector cvQuestionnaireUsageData;
    private CoeusVector cvQuestionnaireQuestionData;
    // 4272: Maintain history of Questionnaires - Start
//    private QuestionnaireMaintainaceBaseBean baseBean;
    private QuestionnaireBaseBean baseBean;
    // 4272: Maintain history of Questionnaires - End
    private CoeusVector cvModuleData;
    private CoeusVector cvSubModuleData;
//    private CoeusVector cvFilterHeaderData;
//    private CoeusVector cvFilterQuestionData;
    private CoeusVector cvDeletedUsageData;
    private CoeusVector cvQuestionnaireData;
    private String quereyKey;
    private CoeusVector cvRuleData,cvQuestionnaireGroup;
    private static final String CONFIRM_DELETE= "metaruleDetail_exceptionCode.1151";
    private static final String SELECT_NODE_DELETE ="metaruleDetail_exceptionCode.1153";
    private static final String CHILD_NODE_EXISTS ="metaruleDetail_exceptionCode.1152";
    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
    private static final String ENTER_QUESTIONNAIRE_NAME = "questionnaire_exceptionCode.1000";
    private static final String ENTER_QUESTIONNAIRE_DESCRIPTION = "questionnaire_exceptionCode.1001";
    private static final String DUPLICATE_RECORD = "questionnaire_exceptionCode.1002";
    private static final String SELECT_MODULE_ITEM_CODE = "questionnaire_exceptionCode.1003";
    private static final String ENTER_QUESTIONNAIRE_LABEL = "questionnaire_exceptionCode.1004";
    private static final String SELECT_QUESTIONNNAIRE_QUESTION = "questionnaire_exceptionCode.1005";
    //Added COEUSDEV-189 : Can't delete the first question applied to a Questionnaire - Start
    private static final String QUESTIONNNAIRE_QUESTION_MANDATORY = "questionnaire_exceptionCode.1030";
    //COEUSDEV-189 : END
    private static final String CANNOT_DELETE_QUESTIONNAIRE = "questionnaire_exceptionCode.1007";
    private static final String CANNOT_DELETE_MODULE = "questionnaire_exceptionCode.1008";
    private static final String CANNOT_MOVE_UP = "questionnaire_exceptionCode.1012";
    private static final String CANNOT_MOVE_DOWN = "questionnaire_exceptionCode.1013";
    
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final char CAN_DELETE_QUESTION = 'L';
    private static final char CAN_DELETE_MODULE = 'M'; 
    private static final char GET_QUESTION_DETAILS = 'B'; 
    
    private static final int MODULE_CODE_COLUMN = 0;
    private static final int SUBMODULE_ITEM_CODE = 1;
    private static final int LABEL_COLUMN = 2;
    private static final int RULE_COLUMN = 3;
    private static final int MANDATORY_COLUMN = 4;
    
    private UsageTableModel usageTableModel;
    private UsageTableCellEditor usageTableCellEditor;
    private UsageTableCellRenderer usageTableCellRenderer;
    private CoeusVector cvDeletedData;
    private boolean isModuleCodeSelected;
    // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
    private boolean finalFlagChecked;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    //Added for Case#2946 Questionnaire Printing enhancement
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    //Added for case#3844 - Questionnaire is not modifiable
    private boolean isModulePresent;
    //Added for case 4287: Questionnaire Templates Start
    private CoeusFileChooser fileChooser;
    private static final String MSGKEY_DELETE_CONFIRM    = "questionnaire_exceptionCode.1018";
    private static final String ERRKEY_PRINT_TEMPLATE    = "questionnaire_exceptionCode.1019";
    private static final String TEMPLATE_FILE_FILTER_KEY = "questionnaireTemplate.fileFilter";
    private String templateAcType = EMPTY_STRING;
    private byte[] templateFileBytes = null;
    private boolean hadTemplate   = false;     
    private QuestionnaireTemplateBean questionnaireTemplateBean;
    private  Properties displayProperties;
    //4287 - end
    // 4272: Maintain history of Questionnaires - Start
    private static final String NEW_VERSION = "NEW_VERSION";
    private static final String CURRENT_VERSION_FINAL = "CURRENT_VERSION_FINAL";
    private static final String CURRENT_VERSION_NON_FINAL = "CURRENT_VERSION_NON_FINAL";
    private static final String CURRENT_VERSION_FINAL_NON_EDITABLE = "CURRENT_VERSION_FINAL_NON_EDITABLE";
    private String versionMode;
    private static final String CANNOT_DELETE_QUESTION = "questionnaire_exceptionCode.1027";
    private int questionnaireVersionNumber;
    // 4272: Maintain history of Questionnaires - End
    //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
    private boolean isModifyMode = false;
    private static final String DISPLAY_MODE = "D";
    private static final String VIEW_QUESTION = "View Question";
    private static final String MODIFY_QUESTION = "Modify Question";
    //COEUSDEV-206 : END
    //Added for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
    private boolean isAddOrModifyOrInsertQuestion = false;
    private boolean isModifyQuestion = false;
    private boolean isInsertQuestion = false;
    private QuestionnaireBean questionForSelection;
    private boolean isSaveData = false;
    //COEUSDEV-227 : END
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    private final String QUESTION_USES_CONDITIONAL_BRANCH = "questionnaire_exceptionCode.1031";
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
            
    /** Creates a new instance of QuestionnaireDetailController */
    public QuestionnaireDetailController(char functionType,QuestionnaireMaintainaceBaseBean baseBean) {
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.functionType = functionType;
        setFunctionType(functionType);
        // // 4272: Maintain history of Questionnaires - Start
//        this.baseBean = baseBean;
        this.baseBean = (QuestionnaireBaseBean) baseBean;
        // 4272: Maintain history of Questionnaires - End
        registerComponents();
        queryEngine = QueryEngine.getInstance();
        setTableKeyTraversal();
    }
    
    public void display() {
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return questionnaireDetailForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    private void setWindowFocus(){
        questionnaireDetailForm.txtName.requestFocusInWindow();
    }
    
    public void registerComponents() {
        questionnaireDetailForm = new QuestionnaireDetailForm();
        questionnaireDetailForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
        questionnaireTreeRenderer = new QuestionnaireTreeRenderer();
        questionnaireDetailForm.btnAdd.addActionListener( this);
        questionnaireDetailForm.btnModify.addActionListener( this);
        questionnaireDetailForm.btnDelete.addActionListener(this);
        questionnaireDetailForm.btnAddModule.addActionListener(this);
        questionnaireDetailForm.btnDeleteModule.addActionListener(this);
        questionnaireDetailForm.btnViewQuestion.addActionListener(this);
        // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        questionnaireDetailForm.btnInsert.addActionListener(this);
        questionnaireDetailForm.btnMoveUp.addActionListener(this);
        questionnaireDetailForm.btnMoveDown.addActionListener(this);
        questionnaireDetailForm.chkFinal.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                enableDisableButtons();
            }
            
        });
        questionnaireDetailForm.txtArDescription.setCaretPosition(0);
        questionnaireDetailForm.txtName.setCaretPosition(0);
        //Added with case 4287 : Questionnaire Templates - Start
        questionnaireDetailForm.btnBrowse.addActionListener(this);
        questionnaireDetailForm.btnRemove.addActionListener(this);
        questionnaireDetailForm.txtTemplate.setCaretPosition(0);
        questionnaireDetailForm.txtTemplate.setDocument(new LimitedPlainDocument(1000));
        //4287 -end
        questionnaireDetailForm.txtArDescription.setDocument(new LimitedPlainDocument(2000));
        questionnaireDetailForm.txtName.setDocument(new LimitedPlainDocument(50));
        usageTableModel = new UsageTableModel();
        usageTableCellEditor = new UsageTableCellEditor();
        usageTableCellRenderer = new UsageTableCellRenderer();
            Component component[]  = {
                questionnaireDetailForm.txtName,
                //Added for Questionnaire enhancement case#2946
                //Final checkbox added
                questionnaireDetailForm.chkFinal,
                questionnaireDetailForm.txtArDescription,
                //Added with case 4287:questionnaire templates
                questionnaireDetailForm.txtTemplate,
                questionnaireDetailForm.btnBrowse,
                questionnaireDetailForm.btnRemove,
                //4287 end
                questionnaireDetailForm.scrPnQuestions,
                questionnaireDetailForm.btnAddModule,
                questionnaireDetailForm.btnDeleteModule,
                questionnaireDetailForm.btnAdd,
                questionnaireDetailForm.btnModify,
                questionnaireDetailForm.btnDelete,
                questionnaireDetailForm.btnViewQuestion,
                questionnaireDetailForm.btnInsert,
                questionnaireDetailForm.btnMoveUp,
                questionnaireDetailForm.btnMoveDown
            };
            ScreenFocusTraversalPolicy  traversal = new ScreenFocusTraversalPolicy(component);
            questionnaireDetailForm.setFocusTraversalPolicy(traversal);
            questionnaireDetailForm.setFocusCycleRoot(true);
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            questionnaireDetailForm.btnAdd.setEnabled(false);
            questionnaireDetailForm.btnAddModule.setEnabled(false);
            questionnaireDetailForm.btnDelete.setEnabled(false);
            questionnaireDetailForm.btnDeleteModule.setEnabled(false);
            questionnaireDetailForm.btnModify.setEnabled(false);
            // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
            questionnaireDetailForm.chkFinal.setEnabled(false);
            questionnaireDetailForm.btnInsert.setEnabled(false);
            questionnaireDetailForm.btnMoveUp.setEnabled(false);
            questionnaireDetailForm.btnMoveDown.setEnabled(false);
            questionnaireDetailForm.chkFinal.setEnabled(false);
            // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
            questionnaireDetailForm.txtName.setEnabled(false);
            questionnaireDetailForm.txtArDescription.setEditable(false);
            //Added with case 4287 : Questionnaire Templates - Start
            questionnaireDetailForm.btnBrowse.setEnabled(false);
            questionnaireDetailForm.btnRemove.setEnabled(false);
            questionnaireDetailForm.txtTemplate.setEditable(false);
            //4287 - end
            questionnaireDetailForm.txtArDescription.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            questionnaireDetailForm.cmbGroup.setEnabled(false);
        }
        
        
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws CoeusException {
        cvQuestionnaireUsageData  = new CoeusVector();
        cvQuestionnaireQuestionData = new CoeusVector();
        cvModuleData = new CoeusVector();
        cvSubModuleData = new CoeusVector();
        cvDeletedData = new CoeusVector();
//        cvFilterHeaderData = new CoeusVector();
//        cvFilterQuestionData = new CoeusVector();
        cvDeletedUsageData = new CoeusVector();
        cvQuestionnaireData = new CoeusVector();
        cvRuleData = new CoeusVector();
        
        cvQuestionnaireData = queryEngine.executeQuery(getQuereyKey() , QuestionnaireBaseBean.class ,CoeusVector.FILTER_ACTIVE_BEANS);
        cvQuestionnaireUsageData  = queryEngine.executeQuery(getQuereyKey(),QuestionnaireUsageBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        cvQuestionnaireQuestionData = queryEngine.executeQuery(getQuereyKey(),QuestionnaireBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        cvModuleData = queryEngine.executeQuery(getQuereyKey(),ModuleDataBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        cvSubModuleData = queryEngine.executeQuery(getQuereyKey(),SubModuleDataBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        cvRuleData = queryEngine.executeQuery(getQuereyKey(),BusinessRuleBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        cvQuestionnaireGroup = (CoeusVector)queryEngine.getDataCollection(getQuereyKey()).get("QUESTIONNAIRE_GROUP_TYPES");
        questionnaireDetailForm.cmbGroup.removeAllItems();
        if(cvQuestionnaireGroup != null && !cvQuestionnaireGroup.isEmpty()){
            for(Object questionGroup : cvQuestionnaireGroup){
                ComboBoxBean groupComboBoxBean = (ComboBoxBean)questionGroup;
                questionnaireDetailForm.cmbGroup.addItem(groupComboBoxBean);
            }
            
        }
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        questionnaireDetailForm.tblUsage.setModel(usageTableModel);
        // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        // For copy questionnaire functionality
        if(getFunctionType() == TypeConstants.COPY_MODE){
            copyQuestionnaireDatas(cvQuestionnaireData);
            copyQuestionnaireDatas(cvQuestionnaireUsageData);
            copyQuestionnaireDatas(cvQuestionnaireQuestionData);
        }
        usageTableModel.setData(cvQuestionnaireUsageData);
        setTreeLabel();
    }
    
    private void setTableEditors(){
        JTableHeader tableHeader = questionnaireDetailForm.tblUsage.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        questionnaireDetailForm.tblUsage.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        questionnaireDetailForm.tblUsage.setRowHeight(22);
        questionnaireDetailForm.tblUsage.setSelectionBackground(java.awt.Color.white);
        questionnaireDetailForm.tblUsage.setSelectionForeground(java.awt.Color.black);
        questionnaireDetailForm.tblUsage.setShowHorizontalLines(true);
        questionnaireDetailForm.tblUsage.setShowVerticalLines(true);
        questionnaireDetailForm.tblUsage.setOpaque(false);
        questionnaireDetailForm.tblUsage.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = questionnaireDetailForm.tblUsage.getColumnModel().getColumn(LABEL_COLUMN);
        column.setPreferredWidth(270);
        column.setResizable(true);
        column.setCellEditor(usageTableCellEditor);
        column.setCellRenderer(usageTableCellRenderer);
        
        column = questionnaireDetailForm.tblUsage.getColumnModel().getColumn(MODULE_CODE_COLUMN);
        column.setPreferredWidth(140);
        column.setResizable(true);
        column.setCellEditor(usageTableCellEditor);
        column.setCellRenderer(usageTableCellRenderer);
        
        column = questionnaireDetailForm.tblUsage.getColumnModel().getColumn(SUBMODULE_ITEM_CODE);
        column.setPreferredWidth(152);
        column.setResizable(true);
        column.setCellEditor(usageTableCellEditor);
        column.setCellRenderer(usageTableCellRenderer);
        //Code addded for Case#2785 - Routing Enhancements - starts
        column = questionnaireDetailForm.tblUsage.getColumnModel().getColumn(RULE_COLUMN);
        column.setPreferredWidth(187);
        column.setResizable(true);
        column.setCellEditor(usageTableCellEditor);
        column.setCellRenderer(usageTableCellRenderer);
        //Code addded for Case#2785 - Routing Enhancements - ends
        // 4272: Maintain history of Questionnaires - Start
        column = questionnaireDetailForm.tblUsage.getColumnModel().getColumn(MANDATORY_COLUMN);
        column.setPreferredWidth(80);
        column.setResizable(true);
        column.setCellEditor(usageTableCellEditor);
        column.setCellRenderer(usageTableCellRenderer);
        // 4272: Maintain history of Questionnaires - End
    }
//Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
//    public void  prepapreFormData() throws CoeusException{
    public void  prepapreFormData(boolean isSaveData) throws CoeusException{
        this.isSaveData = isSaveData;
        //COEUSDEV-227 : End
        setTableEditors();
        setQuestionnnaireHeaderData();
        buildQuessionnaireData();
        enableDisableButtons();
        
        //Code added for coeus4.3 Questionnaire Maintenance case#2946 - starts
        //if the selected node in tree is 0, the disable the buttons
        if(qTree != null){
            TreePath selectionPath = qTree.getSelectionPath();
            if(selectionPath!= null){
                int selRow=qTree.getRowForPath(selectionPath); 
                if(selRow == 0){
                    questionnaireDetailForm.btnModify.setEnabled(false);
                    questionnaireDetailForm.btnDelete.setEnabled(false);
                    questionnaireDetailForm.btnViewQuestion.setEnabled(false);
                }
            }
        }
        //Code added for coeus4.3 Questionnaire Maintenance - ends
        if(questionnaireDetailForm.tblUsage.getRowCount() > 0){
            if(getFunctionType()!= TypeConstants.DISPLAY_MODE){
                questionnaireDetailForm.btnDeleteModule.setEnabled(true);
            }else{
                questionnaireDetailForm.btnDeleteModule.setEnabled(false);
            }
        }else{
            questionnaireDetailForm.btnDeleteModule.setEnabled(false);
        }
        
    }
    
    private void setQuestionnnaireHeaderData() throws CoeusException{
        questionnaireDetailForm.txtQuestionnaireId.setText(""+baseBean.getQuestionnaireNumber());

        if(cvQuestionnaireData!=null && cvQuestionnaireData.size() > 0){
            QuestionnaireBaseBean  questionnaireBaseBean =
            (QuestionnaireBaseBean)cvQuestionnaireData.get(0);
            questionnaireDetailForm.txtName.setText(questionnaireBaseBean.getName());
            questionnaireDetailForm.txtArDescription.setText(questionnaireBaseBean.getDescription());
            //Code added for coeus4.3 Questionnaire Maintenance case#2946
            //For finalFlag
            // 4272: Maintain History of Questionnaire - Start
            if(NEW_VERSION.equalsIgnoreCase(versionMode) && !finalFlagChecked){
                questionnaireDetailForm.chkFinal.setSelected(false);
            } else {
                questionnaireDetailForm.chkFinal.setSelected(questionnaireBaseBean.isFinalFlag());
            }
            // 4272: Maintain History of Questionnaire - End
            if(questionnaireBaseBean.isCompletedFlag()
            && getFunctionType() != TypeConstants.COPY_MODE){
                questionnaireDetailForm.chkFinal.setEnabled(false);
//            } else if(getFunctionType() != TypeConstants.DISPLAY_MODE ){
//                questionnaireDetailForm.chkFinal.setEnabled(true);
//            }
            } else if(getFunctionType() == TypeConstants.DISPLAY_MODE
                    || CURRENT_VERSION_FINAL_NON_EDITABLE.equalsIgnoreCase(versionMode)){
                questionnaireDetailForm.chkFinal.setEnabled(false);
            } else{
                questionnaireDetailForm.chkFinal.setEnabled(true);
            }
            //Added with case 4287 : Questionnaire Templates - Start
            hadTemplate = questionnaireBaseBean.getHasTemplate();
            questionnaireTemplateBean = questionnaireBaseBean.getQuestionnaireTemplateBean();
            String templateName = questionnaireBaseBean.getTemplateName();
            if(templateName!=null && !EMPTY_STRING.equals(templateName.trim())){
                questionnaireDetailForm.txtTemplate.setText(templateName);
            }else{
                questionnaireDetailForm.txtTemplate.setText(EMPTY_STRING);
                questionnaireDetailForm.btnRemove.setEnabled(false);
            }
            //4287 end
            // Added for COEUSQA-3287 Questionnaire Maintenance Features -Start
            Equals equalsActive = new Equals("code", questionnaireBaseBean.getGroupTypeCode()+"");
            CoeusVector cvFilteredQuestionnaireGroup = cvQuestionnaireGroup.filter(equalsActive);
            
            if(cvFilteredQuestionnaireGroup != null && !cvFilteredQuestionnaireGroup.isEmpty()){
                ComboBoxBean groupSelection = (ComboBoxBean)cvFilteredQuestionnaireGroup.get(0);
                questionnaireDetailForm.cmbGroup.setSelectedItem(groupSelection);
            }
            String lastUpdate = CoeusDateFormat.format(questionnaireBaseBean.getUpdateTimestamp().toString());
            questionnaireDetailForm.txtLastUpdate.setText(lastUpdate);
            questionnaireDetailForm.txtLastUpdUser.setText(questionnaireBaseBean.getUpdateUserName());
            // Added for COEUSQA-3287 Questionnaire Maintenance Features -End
        }else{
            questionnaireDetailForm.txtName.setText(EMPTY_STRING);
            questionnaireDetailForm.txtArDescription.setText(EMPTY_STRING);
            //Code added for coeus4.3 Questionnaire Maintenance case#2946
            //For finalFlag
            questionnaireDetailForm.chkFinal.setSelected(false);
            //Added with case 4287 : Questionnaire Templates - Start
            questionnaireDetailForm.txtTemplate.setText(EMPTY_STRING);
            questionnaireDetailForm.btnRemove.setEnabled(false);
            //4287 - end
        }

    }
    
    /** Build the questionnaire Tree and set the rendere for the tree
     */
    private void buildQuessionnaireData() throws CoeusException{
        buildTree();
        if(cvQuestionnaireQuestionData!= null && cvQuestionnaireQuestionData.size() > 0){
            qTree.setCellRenderer(questionnaireTreeRenderer);
            questionnaireDetailForm.scrPnQuestions.setViewportView(qTree);
            backGroundColor = questionnaireDetailForm.scrPnQuestions.getBackground();
            qTree.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            qTree.setOpaque(false);
            qTree.setShowsRootHandles(true);
            qTree.putClientProperty("Jtree.lineStyle", "Angled");
            qTree.setSelectionRow(0);
            selTreePath = qTree.getPathForRow(0);
            selectedNode = (QuestionnaireNode)selTreePath.getLastPathComponent();
            qTree.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent event) {
                    if(event.getKeyCode() == 17) {
                        qTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
                    }
                }
            });
            //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
            //Question tree is remain in the same structure when a new question is added
            if(isSaveData){
                this.isSaveData = false;
            }else if(isAddOrModifyOrInsertQuestion){
                isAddOrModifyOrInsertQuestion = false;
            }else {
                expandAll(qTree,true);
            }
            //COEUSDEV-227 : End
        }
    }
    
    /** Build the tree based on the module code and Module sub item code
     */
    private void buildTree() {
        if(cvQuestionnaireQuestionData != null && cvQuestionnaireQuestionData.size() > 0) {
            //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
            //New JTree instance is created only when the questionnaire is opened
//            qTree = new JTree( new QuestionnaireNode(cvQuestionnaireQuestionData.get(0)));
            if(qTree == null){
                qTree = new JTree( new QuestionnaireNode(cvQuestionnaireQuestionData.get(0)));
            }
            //COEUSDEV-227 : END
            //Code added for coeus4.3 Questionnaire Maintenance - starts
            //Added mouseListener to the tree.
            qTree.setSelectionInterval(0,0);
            qTree.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    enableButtonsForSelection();
                }
            });
            //Code added for coeus4.3 Questionnaire Maintenance - starts
            qTree.setShowsRootHandles(true);
            questionnaireTreeModel = (DefaultTreeModel)qTree.getModel();
            TreePath treePath = null;
            //Modified for  COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
            if(isModifyQuestion){
                int data = ((Integer)questionForSelection.getParentQuestionNumber()).intValue();
                treePath = findTreePath(data);
                QuestionnaireNode parentNode = (QuestionnaireNode)treePath.getLastPathComponent();
                questionnaireTreeModel.nodeChanged(parentNode);
                isModifyQuestion = false;
            }if(isInsertQuestion){
                QuestionnaireNode parentNode = (QuestionnaireNode)selectedNode.getParent();
                int index = parentNode.getIndex(selectedNode);
                QuestionnaireNode childNode = new QuestionnaireNode(questionForSelection);
                questionnaireTreeModel.insertNodeInto(childNode, parentNode, index);
                isInsertQuestion = false;
            }else{//COEUSDEV-227 : END
                for(int index=1;index<cvQuestionnaireQuestionData.size();index++) {
                    QuestionnaireBean questionnaireBean = (QuestionnaireBean)cvQuestionnaireQuestionData.get(index);
                    //Modified for  COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
//                    int data  = ((Integer)questionnaireBean.getParentQuestionNumber()).intValue();
                    int data = -1;
                    if(isSaveData){
                        data = ((Integer)questionnaireBean.getQuestionNumber()).intValue();
                    }else{
                        data = ((Integer)questionnaireBean.getParentQuestionNumber()).intValue();
                    }
                    //COEUSDEV-227 : End
                    treePath = findTreePath(data);
                    setChildsForParent(questionnaireBean,treePath);
                }
            }
        }else{
            if(questionnaireTreeModel!= null){
                questionnaireTreeModel.setRoot(null);
            }
        }
    }
    
    /** Set the childs to the parent. Insert all the nodes to the tree
     */    
    private void setChildsForParent(QuestionnaireBean questionnaireBean,TreePath treePath) {
        QuestionnaireNode childNode = null;
        if(treePath!= null){
            QuestionnaireNode parentNode = (QuestionnaireNode)treePath.getLastPathComponent();
            childNode = new QuestionnaireNode(questionnaireBean);
            //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
            //If new question is added into question, no new JTree instance is created so other questions in the tree remains
            //need to only add new question into question tree
           if(isAddOrModifyOrInsertQuestion){
                TreePath path = findTreePath(((Integer)questionnaireBean.getQuestionNumber()).intValue());
                if(path == null){
                      questionnaireTreeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
                }
            }else if(isSaveData){
                parentNode.setDataObject(questionnaireBean);
            }else{
                questionnaireTreeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
            }
            //COEUSDEV-227 : END
        }
    }
   

    /** Finds the path in tree as specified by the array of names.
     * The names array is a sequence of names where names[0]
     * is the root and names[i] is a child of names[i-1].
     * Comparison is done using String.equals().
     * Returns null if not found.
     * @param tree JTree instance
     * @param name name to the searched/find
     * @return  TreePath found for the specific name
     */
    public TreePath findTreePath(int name) {
        TreeNode root = (TreeNode)qTree.getModel().getRoot();
        TreePath result = findTreePath(qTree, new TreePath(root), name);
        return result;
    }
    /** Set the child nodes to the tree using TreeNodes and buld the tree
     *for each parent
     */
    private TreePath findTreePath(JTree tree, TreePath parent, int parentId) {
        //        parentId = parentId+1;
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        QuestionnaireBean bean = ((QuestionnaireNode)node).getDataObject();
        int nodeValue = ((Integer)bean.getQuestionNumber()).intValue();
        if (node != null && nodeValue==parentId) {
            return parent;
        }else{
            
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    TreePath result = findTreePath(tree, path, parentId);
                    if(result!=null)
                        return result;
                    // Found a match
                }
            }
        }
        // No match at this branch
        return null;
    }
    
    /**
     * Method used to expand/ collapse all the nodes in the tree.
     * @param tree JTree whose nodes are to be expanded/ collapsed.
     * @param expand  boolean true to expand all nodes, false to collapse.
     */
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    
    /**
     * @param tree
     * @param parent
     * @param expand
     */
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
    
    
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException{
        usageTableCellEditor.stopCellEditing();
        boolean isValidate = true;
        if(questionnaireDetailForm.txtName.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_QUESTIONNAIRE_NAME));
            questionnaireDetailForm.txtName.requestFocusInWindow();
            isValidate = false;
            return isValidate;
        }else if(questionnaireDetailForm.txtArDescription.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_QUESTIONNAIRE_DESCRIPTION));
            questionnaireDetailForm.txtArDescription.requestFocusInWindow();
            isValidate = false;
            return isValidate;
        }
        return isValidate;
    }
    
    /** check if the Module Item Code value is selected or not.
     */
    private boolean isModuleCodeExists() throws CoeusException{
        boolean isModuleCodeExists = true;
        if(cvQuestionnaireUsageData!= null && cvQuestionnaireUsageData.size() > 0){
            for(int index = 0; index < cvQuestionnaireUsageData.size(); index++){
                QuestionnaireUsageBean usageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(index);
                //Modified for internal issue fix 96 start
                 if(usageBean.getModuleItemCode() == 0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_MODULE_ITEM_CODE));
                    questionnaireDetailForm.tblUsage.editCellAt(index,MODULE_CODE_COLUMN);
                    usageTableCellEditor.txtLabel.requestFocus();
                    questionnaireDetailForm.tblUsage.setRowSelectionInterval(index,index );
                    questionnaireDetailForm.tblUsage.scrollRectToVisible(
                    questionnaireDetailForm.tblUsage.getCellRect(index, MODULE_CODE_COLUMN, true));
                    isModuleCodeExists = false;
                    break;
                }else if(usageBean.getLabel().trim().equals(EMPTY_STRING)){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_QUESTIONNAIRE_LABEL));
                    questionnaireDetailForm.tblUsage.editCellAt(index,LABEL_COLUMN);
                    usageTableCellEditor.cmbSubModule.requestFocus();
                    questionnaireDetailForm.tblUsage.setRowSelectionInterval(index,index );
                    questionnaireDetailForm.tblUsage.scrollRectToVisible(
                    questionnaireDetailForm.tblUsage.getCellRect(index, LABEL_COLUMN, true));
                    isModuleCodeExists = false;
                    break;
                }
                //Modified for internal issue fix 96 end
            }
        }
        //Commented for internal issue fix 96 start
        
//        else{
//            isModuleCodeExists = false;
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_MODULE_ITEM_CODE));
//        }
        
         //Commented for internal issue fix 96 end
        return isModuleCodeExists;
    }
    
    
    
    /** Check if duplicate Module Code and SubModule Code exists
     */
    private boolean isDuplicateUsageData() throws CoeusException{
        boolean isDuplicate = true;
        Equals eqModuleCode = null;
        Equals eqSubModuleCode = null;
        And eqModuleCodeAndeqSubModuleCode = null;
        CoeusVector cvFilterData = null;
        if(cvQuestionnaireUsageData!= null && cvQuestionnaireUsageData.size() > 0){
            for(int index = 0; index < cvQuestionnaireUsageData.size(); index++){
                QuestionnaireUsageBean usageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(index);
                eqModuleCode = new Equals("moduleItemCode", new Integer(usageBean.getModuleItemCode()));
                eqSubModuleCode = new Equals("moduleSubItemCode", new Integer(usageBean.getModuleSubItemCode()));
                eqModuleCodeAndeqSubModuleCode  = new And(eqModuleCode,eqSubModuleCode);
                cvFilterData = cvQuestionnaireUsageData.filter(eqModuleCodeAndeqSubModuleCode);
                if(cvFilterData != null && cvFilterData.size() > 1 ){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_RECORD));
                    isDuplicate = false;
                    questionnaireDetailForm.tblUsage.editCellAt(index,LABEL_COLUMN);
                    questionnaireDetailForm.tblUsage.editCellAt(index,LABEL_COLUMN);
                    usageTableCellEditor.txtLabel.requestFocus();
                    questionnaireDetailForm.tblUsage.setRowSelectionInterval(index,index );
                    questionnaireDetailForm.tblUsage.scrollRectToVisible(
                    questionnaireDetailForm.tblUsage.getCellRect(index, LABEL_COLUMN, true));
                    break;
                }
            }
        }
        //Code modified for coeus4.3 Questionnaire Maintenance - starts
//        if(cvQuestionnaireQuestionData == null || cvQuestionnaireQuestionData.size() == 0){
        if(cvQuestionnaireQuestionData == null || cvQuestionnaireQuestionData.size() <= 1){
            isDuplicate =  false;
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey());
            //Modified for COEUSDEV-189 : Can't delete the first question applied to a Questionnaire - Start
            //Validation message is thrown when no questions exist in the questionnaire
//            throw new CoeusException(coeusMessageResources.parseMessageKey(SELECT_QUESTIONNNAIRE_QUESTION));
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(QUESTIONNNAIRE_QUESTION_MANDATORY));
            //COEUSDEV-189 : END
//            isDuplicate =  false;
        }
        //Code modified for coeus4.3 Questionnaire Maintenance - ends
        return isDuplicate;
    }
    
    /**
     * Getter for property quereyKey.
     * @return Value of property quereyKey.
     */
    public java.lang.String getQuereyKey() {
        return quereyKey;
    }
    
    /**
     * Setter for property quereyKey.
     * @param quereyKey New value of property quereyKey.
     */
    public void setQuereyKey(java.lang.String quereyKey) {
        this.quereyKey = quereyKey;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        usageTableCellEditor.stopCellEditing();
        try{
            int []selectedRow = qTree.getSelectionRows();
            //Added for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
            if(source.equals(questionnaireDetailForm.btnAdd) ||
                    source.equals(questionnaireDetailForm.btnInsert) ||
                    source.equals(questionnaireDetailForm.btnModify)){
                isAddOrModifyOrInsertQuestion = true;
            }else{
                isAddOrModifyOrInsertQuestion = false;
            }
            if(source.equals(questionnaireDetailForm.btnModify) && isModifyMode){
                isModifyQuestion = true;
            }else{
                isModifyQuestion = false;
            }
            
            if(source.equals(questionnaireDetailForm.btnInsert)){
                isInsertQuestion = true;
            }else{
                isInsertQuestion = false;
            }
            
            //COEUSDEV-227 : END
            if(source.equals(questionnaireDetailForm.btnAdd)){
                performQuestions(TypeConstants.INSERT_RECORD);
                qTree.setSelectionRow(selectedRow[0]);
                //Added for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
                //Selected row gets expaned
                qTree.expandRow(selectedRow[0]);
                //COEUSDEV-227 : ENd
            }else if(source.equals(questionnaireDetailForm.btnModify)){
                //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
//                performQuestions(TypeConstants.UPDATE_RECORD);
                
                if(isModifyMode){
                    performQuestions(TypeConstants.UPDATE_RECORD);
                }else{
                    performQuestions(DISPLAY_MODE);
                }
                //COEUSDEV-206 : END
                //Code added for coeus4.3 Questionnaire Maintenance case#2946
                //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
                //Node selection is done based on the selected treepath
//                qTree.setSelectionRow(selectedRow[0]);
                TreePath treePath = findTreePath(((Integer)questionForSelection.getQuestionNumber()).intValue());
                qTree.setSelectionPath(treePath);
                //COEUSDEV-227 : End

                enableDisableButtons();
            }else if(source.equals(questionnaireDetailForm.btnDelete)){
                performDeleteAction();
                //Code added for coeus4.3 Questionnaire Maintenance
                //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
                //Focus is set to the root node (Questions) whenever question is deleted
//                qTree.setSelectionRow(selectedRow[0]);
                qTree.setSelectionRow(0);
                //COEUSDEV-206 : END
                enableDisableButtons();
            }else if(source.equals(questionnaireDetailForm.btnAddModule)){
                // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
                // When rule condition is defined, questionnaire usage cant be addes
//                performAddModuleAction();
                if(hasDefinedRuleCondition()){
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(QUESTION_USES_CONDITIONAL_BRANCH));
                }else{
                    performAddModuleAction();
                }
                // Modified for COEUSQA-3287 Questionnaire Maintenance Features - End
            }else if(source.equals(questionnaireDetailForm.btnDeleteModule)){
                performDeleteModuleAction();
            }else if(source.equals(questionnaireDetailForm.btnViewQuestion)){
                viewQuestionDetails();
                qTree.setSelectionRow(selectedRow[0]);
            }
            //Code added for coeus4.3 Questionnaire Maintenance case#2946 -starts
            else if(source.equals(questionnaireDetailForm.btnInsert)){
                insertQuestionDetails();
                //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
                //Node selection is done based on the selected treepath
//                qTree.setSelectionRow(selectedRow[0]);
                TreePath treePath = findTreePath(((Integer)questionForSelection.getQuestionNumber()).intValue());
                qTree.setSelectionPath(treePath);
                //COEUSDEV-227 : End
            }else if(source.equals(questionnaireDetailForm.btnMoveUp)){
                moveQuestions(true);
                //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
                //Node selection is done based on the selected treepath
//                qTree.setSelectionRow(0);
                TreePath treePath = findTreePath(((Integer)questionForSelection.getQuestionNumber()).intValue());
                qTree.setSelectionPath(treePath);
                //COEUSDEV-227 : END
                enableDisableButtons();
            }else if(source.equals(questionnaireDetailForm.btnMoveDown)){
                moveQuestions(false);
                //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start    
                //Node selection is done based on the selected treepath
//                qTree.setSelectionRow(0);
                TreePath treePath = findTreePath(((Integer)questionForSelection.getQuestionNumber()).intValue());
                qTree.setSelectionPath(treePath);
                //COEUSDEV-227 : End
                enableDisableButtons();
            }
            //Code added for coeus4.3 Questionnaire Maintenance case#2946 - ends
            //Added with case 4287 : Questionnaire Templates - Start
            else if(source.equals(questionnaireDetailForm.btnBrowse)){
                performUploadAction();
            }else if(source.equals(questionnaireDetailForm.btnRemove)){
                performRemoveAction();
            }            
            //4287 - end
        }catch (CoeusException coeusException){
            coeusException.getMessage();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    /** to view the question details for the selected node. Make 
     *server call to get the question details by passing the questionId
     */
    private void viewQuestionDetails() throws CoeusException{
        QuestionnaireBean viewbean = null;
        TreePath selectionPath = qTree.getSelectionPath();
        if(selectionPath!= null){
            QuestionnaireNode selNode = (QuestionnaireNode)selectionPath.getLastPathComponent();
            viewbean = ((QuestionnaireNode)selNode).getDataObject();
            //Modified for case#3083 - Question Enhancement       
            //AddQuestionController addQuestionController = new AddQuestionController(TypeConstants.DISPLAY_MODE);
            AddQuestionController addQuestionController = new AddQuestionController(TypeConstants.DISPLAY_MODE, true);
            QuestionsMaintainanceBean bean;
            // 4272: Maintain history of Questionnaires - Start
//            bean = getQuestionsData(((Integer)viewbean.getQuestionId()).intValue());
            bean = getQuestionsData(((Integer)viewbean.getQuestionId()).intValue(), ((Integer)viewbean.getQuestionVersionNumber()).intValue());
            // 4272: Maintain history of Questionnaires - End
            addQuestionController.setFormData(bean);
            addQuestionController.display();
        }
    }
    
    /**Make server call to view the question details for the given questionnaire
     *@param questionId
     *@param questionVersionNumber
     *@returns QuestionsMaintainanceBean
     */
    // 4272: Maintain history of Questionnaires 
//    private QuestionsMaintainanceBean getQuestionsData(int questionId) throws CoeusException{
    private QuestionsMaintainanceBean getQuestionsData(int questionId, int questionVersionNumber) throws CoeusException{
         Vector dataObjects = new Vector();
        CoeusVector cvQuestionsData = null ;
        RequesterBean requester = new RequesterBean();
        dataObjects.add(0, new Integer(questionId));
        // 4272: Maintain history of Questionnaires 
        dataObjects.add(1, new Integer(questionVersionNumber));
        requester.setDataObjects(dataObjects);
        requester.setFunctionType(GET_QUESTION_DETAILS);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            cvQuestionsData  = (CoeusVector)response.getDataObject();
        }
        QuestionsMaintainanceBean questionsMaintainanceBean = null ;
        if(cvQuestionsData!=null && cvQuestionsData.size() > 0){
           questionsMaintainanceBean = (QuestionsMaintainanceBean)cvQuestionsData.get(0);
        }
        return questionsMaintainanceBean;
    }
    /** to delete the module for selected row
     */
    private void performDeleteModuleAction() throws CoeusException{
        usageTableCellEditor.stopCellEditing();
        int rowIndex = questionnaireDetailForm.tblUsage.getSelectedRow();
        //Added for case#3844 - Questionnaire is not modifiable
        // 4472: Maintain History of questionnaires- Start
//        isModulePresent = false;
        if(questionnaireDetailForm.tblUsage.getRowCount() <= 0){
            isModulePresent = false;
        }
        // 4472: Maintain History of questionnaires- End
        if(rowIndex != -1 && rowIndex >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
                // When rule condition is defined, questionnaire usage cant be addes
                if(hasDefinedRuleCondition()){
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(QUESTION_USES_CONDITIONAL_BRANCH));
                }else{
                    QuestionnaireUsageBean deletedUsageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(rowIndex);
                    //Code commented for case#3844 - Questionnaire is not modifiable - starts
//               if(canDeleteModule(deletedUsageBean)){
//                   CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_DELETE_MODULE));
//                   return ;
//               }
                    //Code commented for case#3844 - Questionnaire is not modifiable - ends
                    if (deletedUsageBean.getAcType() == null ||
                            deletedUsageBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        cvDeletedUsageData.add(deletedUsageBean);
                    }
                    if(cvQuestionnaireUsageData!=null && cvQuestionnaireUsageData.size() > 0){
                        cvQuestionnaireUsageData.remove(rowIndex);
                        usageTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                        modified = true;
                        deletedUsageBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                    if(rowIndex >0){
                        questionnaireDetailForm.tblUsage.setRowSelectionInterval(
                                rowIndex-1,rowIndex-1);
                        questionnaireDetailForm.tblUsage.setColumnSelectionInterval(LABEL_COLUMN, LABEL_COLUMN);
                        questionnaireDetailForm.tblUsage.scrollRectToVisible(
                                questionnaireDetailForm.tblUsage.getCellRect(
                                rowIndex-1 ,0, true));
                    }else{
                        if(questionnaireDetailForm.tblUsage.getRowCount()>0){
                            questionnaireDetailForm.tblUsage.setRowSelectionInterval(0,0);
                            questionnaireDetailForm.tblUsage.setColumnSelectionInterval(LABEL_COLUMN, LABEL_COLUMN);
                        }
                    }
                    
                    if(questionnaireDetailForm.tblUsage.getRowCount() > 0){
                        questionnaireDetailForm.btnDeleteModule.setEnabled(true);
                    }else{
                        questionnaireDetailForm.btnDeleteModule.setEnabled(false);
                    }
                }
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("orgIDCPnl_exceptionCode.1097"));
        }
    }
    /** To perform add a module action
     */
    private void performAddModuleAction() throws CoeusException{
        QuestionnaireUsageBean questionnaireUsageBean = new QuestionnaireUsageBean();
        questionnaireUsageBean.setAcType(TypeConstants.INSERT_RECORD);
        questionnaireUsageBean.setAwModuleItemCode(0);
        questionnaireUsageBean.setAwModuleSubItemCode(0);
        questionnaireUsageBean.setLabel(EMPTY_STRING);
        questionnaireUsageBean.setModuleItemCode(0);
        questionnaireUsageBean.setModuleSubItemCode(0);
        questionnaireUsageBean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
        // 4272: Maintain history of Questionnaires - Start
        questionnaireUsageBean.setQuestionnaireVersionNumber(questionnaireVersionNumber);
        // 4272: Maintain history of Questionnaires - End
        questionnaireUsageBean.setRuleId(0);
        questionnaireUsageBean.setUnitNumber(baseBean.getUnitNumber());
        cvQuestionnaireUsageData.addElement(questionnaireUsageBean);
        modified = true;
        
        usageTableModel.fireTableRowsInserted(usageTableModel.getRowCount() + 1, usageTableModel.getRowCount() + 1);
        int lastRow = questionnaireDetailForm.tblUsage.getRowCount()-1;
        if(lastRow >= 0){
            questionnaireDetailForm.tblUsage.setRowSelectionInterval( lastRow, lastRow );
            questionnaireDetailForm.tblUsage.setColumnSelectionInterval(MODULE_CODE_COLUMN,MODULE_CODE_COLUMN);
            questionnaireDetailForm.tblUsage.scrollRectToVisible(
            questionnaireDetailForm.tblUsage.getCellRect(
            lastRow ,0, true));
        }
        questionnaireDetailForm.tblUsage.editCellAt(lastRow,MODULE_CODE_COLUMN);
        questionnaireDetailForm.tblUsage.getEditorComponent().requestFocusInWindow();
        questionnaireDetailForm.tblUsage.requestFocus();
        
        if(questionnaireDetailForm.tblUsage.getRowCount() > 0){
            questionnaireDetailForm.btnDeleteModule.setEnabled(true);
        }else{
            questionnaireDetailForm.btnDeleteModule.setEnabled(false);
        }
        //Added for case#3844 - Questionnaire is not modifiable
        isModulePresent = true;
    }
    /** To delete the question from the questionnaire
     */
    private void performDeleteAction() throws CoeusException{
        QuestionnaireBean deletedBean = null;
        if(qTree== null){
            qTree = new JTree( new QuestionnaireNode(null));
        }
        TreePath selectionPath = qTree.getSelectionPath();
        if(selectionPath!= null){
            QuestionnaireNode selNode = (QuestionnaireNode)selectionPath.getLastPathComponent();
            int selRow=qTree.getRowForPath(selectionPath);
            
            int childCount = selNode.getChildCount();
            int optionSelected = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(CONFIRM_DELETE)
            ,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            if(optionSelected == CoeusOptionPane.SELECTION_YES){
                if(childCount > 0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CHILD_NODE_EXISTS));
                    return ;
                }
                deletedBean = ((QuestionnaireNode)selNode).getDataObject();
                // 4272: Maintain history of Questionnaires - Start
//                if(canDeleteQuestion(deletedBean)){
//                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_DELETE_QUESTIONNAIRE));
//                    return ;
//                }
                if(deletedBean.isFinalFlag() || questionnaireDetailForm.chkFinal.isSelected()){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_DELETE_QUESTION));
                    return ;
                }
                // 4272: Maintain history of Questionnaires - End
                modified = true;
                if(deletedBean.getAcType()!= null && deletedBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    cvQuestionnaireQuestionData.remove(deletedBean);
                    // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                    //For shanging the Question sequence number data while deleting                    
                    deleteReOrderQuestions(deletedBean.getParentQuestionNumber().intValue(), 
                            deletedBean.getQuestionSequenceNumber());
                    //Commented COEUSDEV-189 : Can't delete the first question applied to a Questionnaire - Start
                    //When user modify's Questionnaire modified is set to true
//                    modified = false;
                    modified = true;
                    //COEUSDEV-189 : END
                    queryEngine.addCollection(queryKey,QuestionnaireBean.class,cvQuestionnaireQuestionData);
                }else{
                    modified = true;
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedData.addElement(deletedBean);
                    cvQuestionnaireQuestionData.remove(deletedBean);
                    // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                    //For shanging the Question sequence number data while deleting
                    deleteReOrderQuestions(deletedBean.getParentQuestionNumber().intValue(), 
                            deletedBean.getQuestionSequenceNumber());
                    queryEngine.addCollection(queryKey,QuestionnaireBean.class,cvQuestionnaireQuestionData);
                }
                updateParentData(deletedBean);
                if(selNode.isRoot()){
                    questionnaireTreeModel.setRoot(null);
                }else{
                    questionnaireTreeModel.removeNodeFromParent(selNode);
                }
                if(selRow < qTree.getRowCount() &&
                ((QuestionnaireNode)qTree.getPathForRow(selRow).getLastPathComponent()).getAllowsChildren() && !selNode.getAllowsChildren())
                    qTree.setSelectionPath(selectionPath.getParentPath());
                else
                    if(selRow >= qTree.getRowCount())
                        selRow = selRow-1;
                qTree.setSelectionRow(selRow);
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_NODE_DELETE));
            return ;
        }
       enableDisableButtons();       
    }
    
    /**Check if the question is answered in the questionnaire answers. If it
     *is answered then can't delete the question from the tree
     */
    
    private boolean canDeleteQuestion(QuestionnaireBean bean) throws CoeusException{
        boolean canDeleteQuestion = false;
        Vector dataObjects = new Vector();
        dataObjects.addElement(bean.getQuestionId());
        dataObjects.addElement(new Integer(bean.getQuestionnaireId()));
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( CAN_DELETE_QUESTION );
        requester.setDataObjects(dataObjects);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            canDeleteQuestion  = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canDeleteQuestion;
    }
    
    /** check if the module can be deleted when it is asked in some modules 
     *where it has been answered
     */
     private boolean canDeleteModule(QuestionnaireUsageBean bean) throws CoeusException{
        boolean canDeleteModule = false;
        Vector dataObjects = new Vector();
        dataObjects.addElement(new Integer(bean.getModuleItemCode()));
        dataObjects.addElement(new Integer(bean.getModuleSubItemCode()));
        dataObjects.addElement(new Integer(bean.getQuestionnaireId()));
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( CAN_DELETE_MODULE );
        requester.setDataObjects(dataObjects);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            canDeleteModule  = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canDeleteModule;
    }
    
     /** Enable and disable the buttons based on tree node exists in the tree
      */
     private void enableDisableButtons(){
         // Code commented for coeus4.3 Questionnaire Maintenance enhancement case#2946
//        if(qTree!= null){
//            if(qTree.getRowCount() == 0){
//                questionnaireDetailForm.btnModify.setEnabled(false);
//                questionnaireDetailForm.btnDelete.setEnabled(false);
//                questionnaireDetailForm.btnViewQuestion.setEnabled(false);
//            }else if(qTree.getRowCount() > 0){
//                if(getFunctionType() == TypeConstants.DISPLAY_MODE){
//                    questionnaireDetailForm.btnModify.setEnabled(false);
//                    questionnaireDetailForm.btnDelete.setEnabled(false);
//                    questionnaireDetailForm.btnViewQuestion.setEnabled(true);
//                }else{
//                    questionnaireDetailForm.btnModify.setEnabled(true);
//                    questionnaireDetailForm.btnDelete.setEnabled(true);
//                    questionnaireDetailForm.btnViewQuestion.setEnabled(true);
//                }
//            }
//        }else{
//            questionnaireDetailForm.btnModify.setEnabled(false);
//            questionnaireDetailForm.btnDelete.setEnabled(false);
//            questionnaireDetailForm.btnViewQuestion.setEnabled(false);
//        }
         // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
         //To enable and disable buttons
         //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
         //         if(qTree!= null){
//             if(questionnaireDetailForm.chkFinal.isSelected()){
//                 questionnaireDetailForm.btnAdd.setEnabled(false);
//                 questionnaireDetailForm.btnModify.setEnabled(false);
//                 questionnaireDetailForm.btnDelete.setEnabled(false);
//                 questionnaireDetailForm.btnInsert.setEnabled(false);
//                 questionnaireDetailForm.btnMoveUp.setEnabled(false);
//                 questionnaireDetailForm.btnMoveDown.setEnabled(false);
//                 finalFlagChecked = true;
//             } else {
//                 finalFlagChecked = false;
//                 if(getFunctionType() == TypeConstants.DISPLAY_MODE){
//                     questionnaireDetailForm.btnAdd.setEnabled(false);
//                     questionnaireDetailForm.btnModify.setEnabled(false);
//                     questionnaireDetailForm.btnDelete.setEnabled(false);
//                     questionnaireDetailForm.btnInsert.setEnabled(false);
//                     questionnaireDetailForm.btnMoveUp.setEnabled(false);
//                     questionnaireDetailForm.btnMoveDown.setEnabled(false);
//                 } else {
//                     enableButtonsForSelection();
//                 }
//             }
//         }else{
//             questionnaireDetailForm.btnModify.setEnabled(false);
//             questionnaireDetailForm.btnDelete.setEnabled(false);
//             questionnaireDetailForm.btnInsert.setEnabled(false);
//             questionnaireDetailForm.btnMoveUp.setEnabled(false);
//             questionnaireDetailForm.btnMoveDown.setEnabled(false);
//         }
         //COEUSDEV-206 : END
         if(qTree != null){
             //When Questionnarie opened in display/Final flag is selected 'Modify Question' button is changed to 'View Question'
             //'View Question' button is enabled, the Question information is displayed in display mode
             if(questionnaireDetailForm.chkFinal.isSelected() || functionType == TypeConstants.DISPLAY_MODE){
                 finalFlagChecked = true;
                 isModifyMode = false;
                 questionnaireDetailForm.btnModify.setText(VIEW_QUESTION);
                 enableButtonsForSelection();
             } else {
                 finalFlagChecked = false;
                 isModifyMode = true;
                 questionnaireDetailForm.btnModify.setText(MODIFY_QUESTION);
                 enableButtonsForSelection();
             }
         }else{
             questionnaireDetailForm.btnModify.setEnabled(false);
             questionnaireDetailForm.btnDelete.setEnabled(false);
             questionnaireDetailForm.btnInsert.setEnabled(false);
             questionnaireDetailForm.btnMoveUp.setEnabled(false);
             questionnaireDetailForm.btnMoveDown.setEnabled(false);
         }


         
         // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
//         // 4272:  Maintain History of Questionnaires - Start
//         if(questionnaireDetailForm.chkFinal.isSelected()) {
//         }
//         // 4272:  Maintain History of Questionnaires - End

     }
    
    /** Update the parent node data when delete operation is performd
     *@param MetaRuleDetailBean deletedBean
     */
    private void updateParentData(QuestionnaireBean deletedBean){
        Equals equals= new Equals("questionNumber",deletedBean.getParentQuestionNumber());
        CoeusVector cvParentData = cvQuestionnaireQuestionData.filter(equals);
        if(cvParentData!= null && cvParentData.size() > 0){
            QuestionnaireBean detailBean = (QuestionnaireBean)cvParentData.get(0);
            //Code modified for coeus4.3 Questionnaire Maintenance.
//            if(deletedBean.getAcType()!= null && !deletedBean.getAcType().equals(TypeConstants.INSERT_RECORD)){            
            if(deletedBean.getAcType()!= null && !deletedBean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    && detailBean.getAcType() != null && !detailBean.getAcType().equals("N")){
                detailBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
    }
    
    /** Do Add and Modify questions to the questionnaire. Based on the type,
     *perform the action.
     *If there is no parent node then create a parent node first
     *@param actionMode, ADD or MODIFY the question
     */
    private void performQuestions(String actionMode) throws CoeusException{
        AddQuestionnaireQuestionController controller = new AddQuestionnaireQuestionController();
        if(cvQuestionnaireQuestionData==null || cvQuestionnaireQuestionData.size()==0){
            createParentNode(controller);
            return ;
        }
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        if(cvQuestionnaireUsageData == null || cvQuestionnaireUsageData.size() > 1){
            controller.setDisableRuleCondtion(true);
        }else if(cvQuestionnaireUsageData.size() == 1){
            QuestionnaireUsageBean questionnaireUsageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(0);
            if(questionnaireUsageBean.getModuleItemCode() != 0){
                controller.setDisableRuleCondtion(true);
                controller.setQuestionnaireUsageBean(questionnaireUsageBean);
                controller.setRuleData(cvRuleData);
            }else{
                controller.setDisableRuleCondtion(false);
            }
            
        }
        controller.setQuestionnareId(this.baseBean.getQuestionnaireNumber());
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        
//        QuestionBaseBean dataBaseBean = null;
        TreePath selTreePath = qTree.getSelectionPath();
        QuestionnaireNode selNode = (QuestionnaireNode)selTreePath.getLastPathComponent();
        QuestionnaireBean selParentBean = ((QuestionnaireNode)selNode).getDataObject();
        // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
        String parentQuestion = selParentBean.getDescription();
        //Modified for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
//        if(selParentBean.getQuestionNumber().intValue() != 0
//                && actionMode.equals(TypeConstants.UPDATE_RECORD))){
        if(selParentBean.getQuestionNumber().intValue() != 0
                && (actionMode.equals(TypeConstants.UPDATE_RECORD) || actionMode.equals(DISPLAY_MODE))){//COEUSDEV-206 : END
            TreePath selParentPath = selTreePath.getParentPath();
            QuestionnaireNode parentNode = (QuestionnaireNode)selParentPath.getLastPathComponent();
            QuestionnaireBean parentarentBean = ((QuestionnaireNode)parentNode).getDataObject();
            if(parentarentBean.getQuestionNumber().intValue() != 0){
                parentQuestion = parentarentBean.getDescription();
            } else {
                parentQuestion = EMPTY_STRING;
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setText("Previous Question");
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setMinimumSize(new java.awt.Dimension(105, 14));
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setPreferredSize(new java.awt.Dimension(105, 14));            
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setEnabled(false);
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setForeground(Color.BLACK);
                controller.addQuestionnaireQuestionForm.rdBtnCondition.setEnabled(false);
                controller.addQuestionnaireQuestionForm.txtConditionValue.setEditable(false);
                controller.addQuestionnaireQuestionForm.cmbCondition.setEnabled(false);
                controller.addQuestionnaireQuestionForm.lblIs.setEnabled(false);
            }
        } else if(selParentBean.getParentQuestionNumber().intValue() == -1){
            parentQuestion = EMPTY_STRING;
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setText("Previous Question");
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setMinimumSize(new java.awt.Dimension(105, 14));
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setPreferredSize(new java.awt.Dimension(105, 14));            
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setEnabled(false);
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setForeground(Color.BLACK);
            controller.addQuestionnaireQuestionForm.rdBtnCondition.setEnabled(false);
            controller.addQuestionnaireQuestionForm.txtConditionValue.setEditable(false);
            controller.addQuestionnaireQuestionForm.cmbCondition.setEnabled(false);
            controller.addQuestionnaireQuestionForm.lblIs.setEnabled(false);
        }
        // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        if(actionMode.equals(TypeConstants.UPDATE_RECORD)){
            // Prepare the base bean and set question Id and description to the child window
            //Code commented and modified for coeus4.3 enhancement - starts
//            QuestionBaseBean qBaseBean = new QuestionBaseBean();
//            qBaseBean.setQuestionId(selParentBean.getQuestionId());
//            qBaseBean.setDescription(selParentBean.getDescription());
//            controller.setFormData(qBaseBean);
            controller.setParentQuestion(parentQuestion);
            if(selParentBean.getAcType() == null || selParentBean.getAcType().equals(EMPTY_STRING)){
                selParentBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires	
            controller.setIsDisplayMode(false);
            //COEUSDEV-206 : END
            controller.setFormData(selParentBean);
            controller.dlgAddQuestionsForm.setTitle("Modify Question");
            //Code commented and modified for coeus4.3 enhancement - ends
            controller.display();
            if(controller.isOkClicked()){
                modified = true;
//                dataBaseBean = (QuestionBaseBean)controller.getFormData();
                QuestionnaireBean dataBean = (QuestionnaireBean)controller.getFormData();
                CoeusVector cvData = null;
                Equals eqParent = null;
//                QuestionnaireBean parentBean = null;
                if(selParentBean.getQuestionNumber().equals(new Integer(1))){
                    eqParent = new Equals("questionNumber",selParentBean.getQuestionNumber());
                    cvData  = cvQuestionnaireQuestionData.filter(eqParent);
                }else{
                    eqParent = new Equals("questionNumber",selParentBean.getParentQuestionNumber());
                    cvData  = cvQuestionnaireQuestionData.filter(eqParent);
                }
                
                if(cvData!=null && cvData.size() > 0){
//                    parentBean = (QuestionnaireBean)cvData.get(0);
                    //Code commented and modified for coeus4.3 enhancement - starts
//                    selParentBean.setQuestionId(dataBaseBean.getQuestionId());
//                    selParentBean.setDescription(dataBaseBean.getDescription());                    
                    selParentBean.setQuestionId(dataBean.getQuestionId());
                    selParentBean.setDescription(dataBean.getDescription());
                    selParentBean.setConditionalFlag(dataBean.isConditionalFlag());
                    selParentBean.setCondition(dataBean.getCondition());
                    selParentBean.setConditionValue(dataBean.getConditionValue()); 
                    selParentBean.setConditionRuleId(dataBean.getConditionRuleId());
                    selParentBean.setConditionRuleDesc(dataBean.getConditionRuleDesc());
                    //Code commented and modified for coeus4.3 enhancement - ends
                    // 4272: Maintain History of Questionnaires
                    selParentBean.setQuestionVersionNumber(dataBean.getQuestionVersionNumber()); 
                    if(selParentBean.getAcType()==null){
                        selParentBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
            
        }else if(actionMode.equals(TypeConstants.INSERT_RECORD)){
            // Get the data from the form
            // Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
            QuestionMaintenanceController questionMaintenanceController =
                    new QuestionMaintenanceController("Questions", mdiForm.getUnitNumber(),true);
            questionMaintenanceController.setFormData(null);
            questionMaintenanceController.display();
            QuestionBaseBean questBaseBean = questionMaintenanceController.getQuestionForQuestionnaire();
            //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
            controller.setIsDisplayMode(false);
            //COEUSDEV-206 : END
            if(questionMaintenanceController.isOkClicked()){
                controller.setFormData(questBaseBean);
            } else {
                return;
            }
            QuestionnaireBean dataBean = new QuestionnaireBean();
            dataBean.setQuestionId(questBaseBean.getQuestionId());
            // 4272: Maintain history of Questionnaires 
            dataBean.setQuestionVersionNumber(new Integer(questBaseBean.getVersionNumber()));
            dataBean.setDescription(questBaseBean.getDescription());
            controller.setParentQuestion(parentQuestion);
            dataBean.setAcType(TypeConstants.INSERT_RECORD);
            controller.setFormData(dataBean);
            
            controller.display();
            if(controller.isOkClicked()){
                modified = true;
                //Code commented and modified for coeus4.3 enhancement - starts
//                dataBaseBean = (QuestionBaseBean)controller.getFormData();                
                dataBean = (QuestionnaireBean)controller.getFormData();
                // Set the default values and other values from the child window
//                QuestionnaireBean dataBean = new QuestionnaireBean();
                dataBean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
                // 4272: Maintain history of Questionnaires - Start
                dataBean.setQuestionnaireVersionNumber(questionnaireVersionNumber);
                // 4272: Maintain history of Questionnaires - End
//                dataBean.setCondition(EMPTY_STRING);
//                dataBean.setConditionValue(EMPTY_STRING);
//                dataBean.setConditionalFlag(false);
//                dataBean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
//                dataBean.setDescription(dataBaseBean.getDescription());
//                dataBean.setQuestionId(dataBaseBean.getQuestionId());
                //Code commented and modified for coeus4.3 enhancement - ends
                dataBean.setQuestionNumber(new Integer(getMaxQuestionNumber()));
                dataBean.setParentQuestionNumber(selParentBean.getQuestionNumber());
                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                //To set the new Question sequence number
                Equals eqParentQuestion = new Equals("parentQuestionNumber", selParentBean.getQuestionNumber());
                dataBean.setQuestionSequenceNumber(cvQuestionnaireQuestionData.filter(eqParentQuestion).size()+1);
                dataBean.setAcType(TypeConstants.INSERT_RECORD);
                cvQuestionnaireQuestionData.addElement(dataBean);
                queryEngine.addCollection(queryKey,QuestionnaireBean.class,cvQuestionnaireQuestionData);
                enableDisableButtons();
            }
        }
        //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
        //Display the question details in display mode
        else if(actionMode.equals(DISPLAY_MODE)){
            controller.setIsDisplayMode(true);
            controller.setParentQuestion(parentQuestion);
            controller.setFormData(selParentBean);
            controller.display();
        }//COEUSDEV-206 : END
        //Added for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
        questionForSelection = selParentBean;
        //COEUSDEV-227 : End
        //Modified for COEUSDEV-227
        if(!actionMode.equals(DISPLAY_MODE)){
            buildQuessionnaireData();
        }
    }
    
    public void stopCellEditing(){
        usageTableCellEditor.stopCellEditing();
    }
   
    /** to get the max question number from the data object
     *@returns max question number
     */
    private int getMaxQuestionNumber() throws CoeusException{
        int questionNum = 0;
        //Code commented and modified for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
        //To get the new Question number
//        int newQuestionNumber = 0;
//        if(cvQuestionnaireQuestionData!= null && cvQuestionnaireQuestionData.size() > 0){
//            for(int index= 0; index < cvQuestionnaireQuestionData.size(); index++){
//                QuestionnaireBean countBean = (QuestionnaireBean)cvQuestionnaireQuestionData.get(index);
//                questtionNum = ((Integer)countBean.getQuestionNumber()).intValue();
//            }
//            newQuestionNumber = questtionNum+1;
//        }
        int newQuestionNumber = 1;
        if(cvQuestionnaireQuestionData!= null && cvQuestionnaireQuestionData.size() > 0){
            for(int index= 0; index < cvQuestionnaireQuestionData.size(); index++){
                QuestionnaireBean countBean = (QuestionnaireBean)cvQuestionnaireQuestionData.get(index);
                if(((Integer)countBean.getQuestionNumber()).intValue() > questionNum){
                    questionNum = ((Integer)countBean.getQuestionNumber()).intValue();
                }
            }
            newQuestionNumber = questionNum+1;
        }
        //Code commented and added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        return newQuestionNumber;
    }
    
    /** to create a parent node. If there are no nodes available then prepare the 
     *parent node and set it as a root to the tree
     */
    private void createParentNode(AddQuestionnaireQuestionController controller) throws CoeusException{
        
        controller.setFormData(null);
        controller.display();
        if(controller.isOkClicked()){
            //Code commented for coeus4.3 enhancement
//            QuestionBaseBean dataBaseBean = (QuestionBaseBean)controller.getFormData();
//            QuestionnaireBean  questionnaireBean = null;
//            questionnaireBean = new QuestionnaireBean();
            //Code added for coeus4.3 enhancement - starts
            QuestionnaireBean questionnaireBean = (QuestionnaireBean)controller.getFormData();
            questionnaireBean.setCondition(EMPTY_STRING);
            questionnaireBean.setConditionValue(EMPTY_STRING);
            questionnaireBean.setConditionalFlag(false);
            questionnaireBean.setParentQuestionNumber(new Integer(0));
            questionnaireBean.setQuestionNumber(new Integer(1));
            questionnaireBean.setAcType(TypeConstants.INSERT_RECORD);
            questionnaireBean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
            //Code added for coeus4.3 enhancement - starts
            //Code commented for coeus4.3 enhancement
//            questionnaireBean.setQuestionId(dataBaseBean.getQuestionId());
//            questionnaireBean.setDescription(dataBaseBean.getDescription());
            cvQuestionnaireQuestionData.addElement(questionnaireBean);
            queryEngine.addCollection(queryKey,QuestionnaireBean.class,cvQuestionnaireQuestionData);
            buildQuessionnaireData();
            enableDisableButtons();
        }
    }
    
    
    
    /** To check whether the  questionnaire details are modified or not
     *@retursn boolean says data is modified or not
     */
    public boolean isQuestionnaireModified(){
        boolean isDataChanged = false;
        if(getFunctionType() == TypeConstants.ADD_MODE
                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                || getFunctionType() == TypeConstants.COPY_MODE){
            isDataChanged = true;
            modified = true;
        }
        String name = questionnaireDetailForm.txtName.getText().trim();
        String description  = questionnaireDetailForm.txtArDescription.getText().trim();
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        boolean finalFlag = questionnaireDetailForm.chkFinal.isSelected();
        if(cvQuestionnaireData!= null && cvQuestionnaireData.size() > 0){
            QuestionnaireBaseBean questionnaireBaseBean = (QuestionnaireBaseBean)cvQuestionnaireData.get(0);
            if(!questionnaireBaseBean.getName().equals(name)){
                isDataChanged = true;
            }else if(!questionnaireBaseBean.getDescription().equals(description)){
                isDataChanged = true;
            }
            //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
            else if(questionnaireBaseBean.isFinalFlag() != finalFlag){
                isDataChanged = true;
            }
            //Added for case 4287: Questionnaire Templates
            else if(!EMPTY_STRING.equals(templateAcType)){
                isDataChanged = true;
            }
            //Added for  COEUSQA-3287 Questionnaire Maintenance Features - Start
            if(!isDataChanged && questionnaireDetailForm.cmbGroup != null){
                ComboBoxBean groupSelection = (ComboBoxBean)questionnaireDetailForm.cmbGroup.getSelectedItem();
                int selectedGroupCode = 0;
                if(groupSelection != null){
                    if(!CoeusGuiConstants.EMPTY_STRING.equals(groupSelection.getCode())){
                        selectedGroupCode = Integer.parseInt(groupSelection.getCode());
                    }
                    
                    if(selectedGroupCode != questionnaireBaseBean.getGroupTypeCode()){
                        isDataChanged = true;
                    }
                }
            }
            //Added for  COEUSQA-3287 Questionnaire Maintenance Features - End
        }
        return isDataChanged;
    }
    /** set the AcTypes and update the data object of the questionnaire details
     *data
     */
    private void saveQuestionnaireDetailsData() throws CoeusException{
        String name        = questionnaireDetailForm.txtName.getText().trim();
        String description = questionnaireDetailForm.txtArDescription.getText().trim();
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        boolean finalFlag = questionnaireDetailForm.chkFinal.isSelected();
        //Added with case 4287 : Questionnaire Templates - Start
        String fileName   = questionnaireDetailForm.txtTemplate.getText().trim();
        if(questionnaireTemplateBean!=null){
            if(!EMPTY_STRING.equals(templateAcType)){
                //For add/modify mode
                questionnaireTemplateBean.setAcType(templateAcType);
            }else if(getFunctionType()==TypeConstants.COPY_MODE && templateFileBytes!=null){
                //For copy mode
                questionnaireTemplateBean.setTemplateFileBytes(templateFileBytes);
                questionnaireTemplateBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        if(cvQuestionnaireData!= null && cvQuestionnaireData.size() > 0){
            QuestionnaireBaseBean questionnaireBaseBean = (QuestionnaireBaseBean)cvQuestionnaireData.get(0);
//            if((!name.equals(questionnaireBaseBean.getName()) ||
//            !description.equals(questionnaireBaseBean.getDescription()) ||
//            //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
//            finalFlag != questionnaireBaseBean.isFinalFlag()) && getFunctionType() != TypeConstants.COPY_MODE){
//                questionnaireBaseBean.setName(questionnaireDetailForm.txtName.getText().trim());
//                questionnaireBaseBean.setDescription(questionnaireDetailForm.txtArDescription.getText().trim());
//                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
//                questionnaireBaseBean.setFinalFlag(questionnaireDetailForm.chkFinal.isSelected());
//                questionnaireBaseBean.setAcType(TypeConstants.UPDATE_RECORD);
//            } else {
//                questionnaireBaseBean.setName(questionnaireDetailForm.txtName.getText().trim());
//                questionnaireBaseBean.setDescription(questionnaireDetailForm.txtArDescription.getText().trim());
//                questionnaireBaseBean.setFinalFlag(questionnaireDetailForm.chkFinal.isSelected());
//            }
            // 4272: Maintain history of Questionnaires - Start
//            if(getFunctionType()==TypeConstants.MODIFY_MODE && isQuestionnaireModified()){
             if(getFunctionType()==TypeConstants.MODIFY_MODE && isQuestionnaireModified() && 
//                    !NEW_VERSION.equalsIgnoreCase(versionMode)){
                    !TypeConstants.INSERT_RECORD.equalsIgnoreCase(questionnaireBaseBean.getAcType())){
            // 4272: Maintain history of Questionnaires - End
                questionnaireBaseBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            questionnaireBaseBean.setName(name);
            questionnaireBaseBean.setDescription(description);
            questionnaireBaseBean.setFinalFlag(finalFlag);
            questionnaireBaseBean.setTemplateName(fileName);
            questionnaireBaseBean.setHasTemplate((fileName.length()>0));
            questionnaireBaseBean.setQuestionnaireTemplateBean(questionnaireTemplateBean);
            //4287 - End
            ComboBoxBean groupSelection = (ComboBoxBean)questionnaireDetailForm.cmbGroup.getSelectedItem();
            if(groupSelection != null && !CoeusGuiConstants.EMPTY_STRING.equals(groupSelection.getCode())){
                questionnaireBaseBean.setGroupTypeCode(Integer.parseInt(groupSelection.getCode()));
            }else{
                questionnaireBaseBean.setGroupTypeCode(0);
            }
          // 4272: Maintain history of Questionnaires - Start
//        }else if(getFunctionType() == TypeConstants.ADD_MODE
//                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
//                || getFunctionType() == TypeConstants.COPY_MODE){
            }else if(getFunctionType() == TypeConstants.ADD_MODE ||  NEW_VERSION.equalsIgnoreCase(versionMode)               
                || getFunctionType() == TypeConstants.COPY_MODE){
            // 4272: Maintain history of Questionnaires - End
            QuestionnaireBaseBean questionnaireBaseBean = new QuestionnaireBaseBean();
            questionnaireBaseBean.setAcType(TypeConstants.INSERT_RECORD);
            questionnaireBaseBean.setDescription(description);
            questionnaireBaseBean.setName(name);
            questionnaireBaseBean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
            // 4272: Maintain history of Questionnaires - Start
            if( !NEW_VERSION.equalsIgnoreCase(versionMode) ){
                questionnaireBaseBean.setQuestionnaireVersionNumber(1);
            }
            // 4272: Maintain history of Questionnaires - End
            questionnaireBaseBean.setUnitNumber(baseBean.getUnitNumber());
            //Code commented and modified for coeus4.3 Questionnaire Maintenance enhancement case#2946
            questionnaireBaseBean.setFinalFlag(finalFlag);
            cvQuestionnaireData.addElement(questionnaireBaseBean);
            //Added with case 4287 : Questionnaire Templates - Start
            questionnaireBaseBean.setTemplateName(fileName);
            questionnaireBaseBean.setHasTemplate((fileName.length()>0));
            questionnaireBaseBean.setQuestionnaireTemplateBean(questionnaireTemplateBean);
            //4287
        }
    }
    /** Make chain of validation.Check for the questionnaire validation,
     *Questionnaire usage validation and questionnaire questions validation
     *If all the validations are successful then save the questionnaire 
     *question data
     */
    public boolean isValidationSuccessfull() throws CoeusException, CoeusUIException{
        boolean isValidated = true;
        usageTableCellEditor.stopCellEditing();
        isValidated = validate();
        if(isValidated){
            //Modified for case#3844 - Questionnaire is not modifiable - start
            if(isModulePresent){
                isValidated = isModuleCodeExists();
            }
            //Modified for case#3844 - Questionnaire is not modifiable - end
            if(isValidated){
                isValidated = isDuplicateUsageData();
            }
        }
        return isValidated;
    }
    
    /** Save the all questionnaire related details
     *Save Questionnaire, questionanire Usage and questionnaire Questions
     *@returns Map containing the data objects
     *@throws CoeusException
     */
    public  Map saveQuestionnaireData() throws CoeusException{
        usageTableCellEditor.stopCellEditing();
        CoeusVector cvQuestionnaireDataObjects = new CoeusVector();
        CoeusVector cvUsageDataObjects = new CoeusVector();
        HashMap hmQuestionnaireData = new HashMap();
        saveQuestionnaireDetailsData();
        if(cvDeletedData!=null && cvDeletedData.size() > 0){
            cvQuestionnaireDataObjects.addAll(cvDeletedData);
        }
        if(cvDeletedUsageData!= null && cvDeletedUsageData.size() > 0){
            cvUsageDataObjects.addAll(cvDeletedUsageData);
        }
        if(cvQuestionnaireQuestionData!= null && cvQuestionnaireQuestionData.size() > 0){
            for(int index = 0; index < cvQuestionnaireQuestionData.size(); index++){
                QuestionnaireBean questionnaireBean = (QuestionnaireBean)cvQuestionnaireQuestionData.get(index);
                if(questionnaireBean!= null && questionnaireBean.getAcType()!= null){
                    cvQuestionnaireDataObjects.addElement(questionnaireBean);
                }
            }
        }
        if(cvQuestionnaireUsageData!= null && cvQuestionnaireUsageData.size() > 0){
            for(int index = 0; index < cvQuestionnaireUsageData.size(); index++){
                QuestionnaireUsageBean usageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(index);
                //Modified for COEUSDEV-86 : questionnaire for Submission - Start
//                if(usageBean!= null && usageBean.getAcType()!= null){
//                    cvUsageDataObjects.addElement(usageBean);
//                }
                cvUsageDataObjects.addElement(usageBean);
                //COEUSDEV-86 : end
            }
        }
        
        hmQuestionnaireData.put(QuestionnaireUsageBean.class,cvUsageDataObjects);
        hmQuestionnaireData.put(QuestionnaireBean.class,cvQuestionnaireDataObjects);
        hmQuestionnaireData.put(QuestionnaireBaseBean.class,cvQuestionnaireData);
        return(Map)hmQuestionnaireData;
    }
    
    
    
    /**
     * Getter for property modified.
     * @return Value of property modified.
     */
    public boolean isModified() {
        usageTableCellEditor.stopCellEditing();
        return modified;
    }
    
    /**
     * Setter for property modified.
     * @param modified New value of property modified.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    /** Set the Image icons for the tree and check for null.Get the Image icons for
     * Questionnaire Tree
     */
    public class QuestionnaireTreeRenderer extends DefaultTreeCellRenderer{
        //Code commented and modified for coeus4.3 Questionnaire Maintenance enhancement case#2946
//        private ImageIcon rootQ,parentQ,childQ;
        private ImageIcon rootQ,conditionalQ,questionQ;
        public QuestionnaireTreeRenderer(){
            super();
            java.net.URL rootNode = getClass().getClassLoader().getResource( CoeusGuiConstants.RULE_PARENT_NODE );
            //Code commented and modified for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
//            java.net.URL questionNode = getClass().getClassLoader().getResource( CoeusGuiConstants.CHILD_PROP_HIE_ICON);
//            java.net.URL parentNode = getClass().getClassLoader().getResource( CoeusGuiConstants.PARENT_PROP_HIE_ICON);
//            rootQ = new ImageIcon(rootNode);
//            parentQ = new ImageIcon(parentNode);
//            childQ = new ImageIcon(questionNode);
            java.net.URL conditionalNode = getClass().getClassLoader().getResource( CoeusGuiConstants.CONDITIONAL_PROP_HIE_ICON);
            java.net.URL questionNode = getClass().getClassLoader().getResource( CoeusGuiConstants.QUESTION_PROP_HIE_ICON);
            rootQ = new ImageIcon(rootNode);
            conditionalQ = new ImageIcon(conditionalNode);
            questionQ = new ImageIcon(questionNode);            
            //Code commented and modified for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        }
        
        /**
         * @param tree
         * @param value
         * @param selected
         * @param expanded
         * @param leaf
         * @param row
         * @param hasFocus
         * @return
         */
        public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf,int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            setBackgroundNonSelectionColor(backGroundColor);
//            Equals eqParent = null;
            QuestionnaireBean questionnaireBean  = null;
//            QuestionnaireNode selNode = (QuestionnaireNode)value;
            Object obj = ((QuestionnaireNode)value).getDataObject();
            if( obj instanceof QuestionnaireBean ){
                questionnaireBean = ( QuestionnaireBean) obj;
                //code modified for coeus4.3 enhancements
                //To add multiple parents
//                if(questionnaireBean.getParentQuestionNumber().equals(new Integer(0))){
                if(questionnaireBean.getAcType() != null && questionnaireBean.getAcType().equals("N")){
                    setIcon(rootQ);
                } else {
                    //Code commented and modified for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
                    // Images changed
//                    eqParent = new Equals("parentQuestionNumber",questionnaireBean.getQuestionNumber());
//                    CoeusVector cvData  = cvQuestionnaireQuestionData.filter(eqParent);
//                    if(cvData!=null && cvData.size() > 0){
//                        setIcon(parentQ);
//                    }else{
//                        setIcon(childQ);
//                    }
                    if(questionnaireBean.isConditionalFlag()){
                        setIcon(conditionalQ);
                    }else{
                        setIcon(questionQ);
                    }
                    //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
                }
            }else{
                setText((String)((DefaultMutableTreeNode)value).getUserObject());
            }
            //code modified for coeus4.3 enhancements
            //To add multiple parents
//            setText(questionnaireBean.getQuestionId()+" : "+questionnaireBean.getDescription());
            if(questionnaireBean.getAcType() != null && questionnaireBean.getAcType().equals("N")){
                setText(questionnaireBean.getDescription());
            } else {
                //  4272: Maintain History of Questionnaires - Start
                String questionStatus = "";
                if((questionnaireBean.getQuestionVersionNumber() != null) && (!questionnaireBean.isQuestionStatus())){
                    questionStatus = "[Inactive]";
                    setForeground(Color.BLUE);
                }
//                setText(questionnaireBean.getQuestionId()+" : "+questionnaireBean.getDescription());
                setText(questionnaireBean.getQuestionId()+" : "+questionnaireBean.getDescription() + " " + questionStatus);
              //   4272: Maintain History of Questionnaires - End
            }
            setComponentOrientation(tree.getComponentOrientation());
            return this;
        }
    }
    
    
    public class UsageTableModel extends AbstractTableModel implements TableModel{
//        String colName[] = {"Questionnaire Label", "Module", "Sub-Module"};
//        Class colClass[] = {String.class, Integer.class, Integer.class};
        // 4272: Maintain history of Questionnaires - Start
//        //Code modified for Case#2785 - Routing Enhancements
//        String colName[] = {"Module", "Sub-Module", "Label", "Rule"};
//        Class colClass[] = {Integer.class, Integer.class, Integer.class, Integer.class};
        
        String colName[] = {"Module", "Sub-Module", "Label", "Rule", "Mandatory"};
        Class colClass[] = {Integer.class, Integer.class, Integer.class, Integer.class, String.class};
        // 4272: Maintain history of Questionnaires - End
        
        private static final String EMPTY_STRING = "";
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public boolean isCellEditable(int row, int col){
            if(getFunctionType()== TypeConstants.DISPLAY_MODE){
                return false;
            }else{
                return true;
            }
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getRowCount() {
            if(cvQuestionnaireUsageData == null || cvQuestionnaireUsageData.size() == 0){
                return 0;
            }else{
                return cvQuestionnaireUsageData.size();
            }
        }
        
        public void setData(CoeusVector cvQuestionnaireUsageData){
            cvQuestionnaireUsageData = cvQuestionnaireUsageData;
            fireTableDataChanged();
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            QuestionnaireUsageBean usageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(rowIndex);
            SubModuleDataBean subModuleDataBean = null;
            ModuleDataBean moduleDataBean = null;
            CoeusVector filteredVector = null;
            BusinessRuleBean businessRuleBean = null;
            int typeCode = 0;
            switch(columnIndex){
                case LABEL_COLUMN:
                    return usageBean.getLabel();
                case MODULE_CODE_COLUMN:
                    typeCode=usageBean.getModuleItemCode();
                    filteredVector = cvModuleData.filter(new Equals("code",""+typeCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        moduleDataBean = (ModuleDataBean)filteredVector.get(0);
                        return moduleDataBean;
                    }else{
                        ModuleDataBean dataBean = new ModuleDataBean();
                        dataBean.setCode(EMPTY_STRING);
                        dataBean.setDescription(EMPTY_STRING);
                        return dataBean;
                    }
                case SUBMODULE_ITEM_CODE:
                    int subModuleItemCode = usageBean.getModuleSubItemCode();
                    typeCode=usageBean.getModuleItemCode();
                    filteredVector = cvSubModuleData.filter(new Equals("moduleCode",new Integer(typeCode)));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        for(int index= 0; index < filteredVector.size(); index++){
                            subModuleDataBean = (SubModuleDataBean)filteredVector.get(index);
                            if(Integer.parseInt(subModuleDataBean.getCode()) == subModuleItemCode){
                                return subModuleDataBean;
                            }
                        }
                    }
                //Code addded for Case#2785 - Routing Enhancements - starts
                    break;
                case RULE_COLUMN:
                    int ruleId = usageBean.getRuleId();
                    int moduleCodeVal = usageBean.getModuleItemCode();
                    int subModuleCodeVal = usageBean.getModuleSubItemCode();
                    Equals eqModuleCode = new Equals("moduleCode", ""+moduleCodeVal);
                    Equals eqSubModuleCode = new Equals("submoduleCode", ""+subModuleCodeVal);
                    And andData = new And(eqModuleCode, eqSubModuleCode);                    
                    filteredVector = cvRuleData.filter(andData);
                    if(filteredVector!=null && filteredVector.size() > 0){
                        for(int index= 0; index < filteredVector.size(); index++){
                            businessRuleBean = (BusinessRuleBean)filteredVector.get(index);
                            if(Integer.parseInt(businessRuleBean.getRuleId()) == ruleId){
                                return businessRuleBean;
                            }
                        }
                    }
                    break;
                 //Code addded for Case#2785 - Routing Enhancements - ends
                // 4272: Maintain history of Questionnaires - Start
                case MANDATORY_COLUMN:
                    if(usageBean.isMandatory()){
                        return new ComboBoxBean("1", "Yes");
                    } else {
                        return new ComboBoxBean("0", "No");
                    }
                // 4272: Maintain history of Questionnaires - End                    
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int column){
            QuestionnaireUsageBean usageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(row);
            int typeCode = 0;
//            ComboBoxBean comboBoxBean = null;
            ModuleDataBean moduleDataBean = null;
            //Code addded for Case#2785 - Routing Enhancements
            SubModuleDataBean subModuleDataBean = null;
            BusinessRuleBean businessRuleBean = null;
            switch(column){
                case LABEL_COLUMN:
                    if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                        if(usageBean.getLabel().equals(value.toString())) break;
                        usageBean.setLabel(value.toString());
                        if(usageBean.getAcType()!= null && usageBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            
                        }else{
                            usageBean.setAcType(TypeConstants.UPDATE_RECORD);
                            modified = true;
                        }
                    }
                    break;
                case  MODULE_CODE_COLUMN:
                    if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                        moduleDataBean = (ModuleDataBean)cvModuleData.filter(new Equals("description", value.toString())).get(0);
                        typeCode = Integer.parseInt(moduleDataBean.getCode());
                        if(usageBean.getModuleItemCode() == typeCode) break;
                        usageBean.setModuleItemCode(typeCode);
                        usageBean.setModuleSubItemCode(0);
                        //Added for COEUSDEV-86 : questionnaire for Submission - Start
                        usageBean.setRuleId(0);
                        //COEUSDEV-86 : End
                        if(usageBean.getAcType()!= null && usageBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            
                        }else{
                            usageBean.setAcType(TypeConstants.UPDATE_RECORD);
                            modified = true;
                        }
                    }else{
                        modified = true;
                        usageBean.setModuleSubItemCode(0);
                        usageBean.setModuleItemCode(0);
                    }
                    break;
                    
                    
                case SUBMODULE_ITEM_CODE:
                    if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                        subModuleDataBean = (SubModuleDataBean)cvSubModuleData.filter(new Equals("description", value.toString())).get(0);
                        typeCode = Integer.parseInt(subModuleDataBean.getCode());
                        if(usageBean.getModuleSubItemCode() == typeCode) break;
                        usageBean.setModuleSubItemCode(typeCode);
                        //Added for COEUSDEV-86 : questionnaire for Submission - Start
                        usageBean.setRuleId(0);
                        //COEUSDEV-86 : END
                        if(usageBean.getAcType()!= null && usageBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            
                        }else{
                            usageBean.setAcType(TypeConstants.UPDATE_RECORD);
                            modified = true;
                        }
                    }else{
                        if(usageBean.getModuleSubItemCode() == typeCode) break;
                        usageBean.setModuleSubItemCode(0);
                        usageBean.setRuleId(0);
                        usageBean.setAcType(TypeConstants.UPDATE_RECORD);
                        modified = true;
                    }
                    break;
                //Code addded for Case#2785 - Routing Enhancements - starts    
                case RULE_COLUMN:
                    if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                        businessRuleBean = (BusinessRuleBean)cvRuleData.filter(new Equals("description", value.toString())).get(0);
                        if(businessRuleBean.getRuleId() == null 
                                || businessRuleBean.getRuleId().equals(EMPTY_STRING)){
                            typeCode = 0;
                        } else {
                            typeCode = Integer.parseInt(businessRuleBean.getRuleId());
                        }
                        if(usageBean.getRuleId() == typeCode) break;
                        usageBean.setRuleId(typeCode);
                        if(usageBean.getAcType()!= null && usageBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            
                        }else{
                            usageBean.setAcType(TypeConstants.UPDATE_RECORD);
                            modified = true;
                        }
                    }else{
                        if(usageBean.getRuleId() == typeCode) break;
                        usageBean.setRuleId(0);
                        //Modified with COEUSDEV-140: Update questionnaire - Error - data changed between retrieve and update
                        if(functionType != 'C' && !TypeConstants.INSERT_RECORD.equals(usageBean.getAcType())){
                            usageBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        modified = true;
                    }
                    break;                    
                //Code addded for Case#2785 - Routing Enhancements - ends    
                // 4272: Maintain history of Questionnaires - Start
                case MANDATORY_COLUMN:
                    if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                        
                        if("Yes".equalsIgnoreCase(value.toString())){
                            usageBean.setMandatory(true);
                        } else {
                            usageBean.setMandatory(false);
                        }
                        
                        if(usageBean.getAcType()!= null && usageBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            
                        }else{
                            usageBean.setAcType(TypeConstants.UPDATE_RECORD);
                            modified = true;
                        }
                    }
                    break;
                // 4272: Maintain history of Questionnaires - End    
            }
            usageTableModel.fireTableRowsUpdated(row,row);
        }
    }
    
    
    
    private class UsageTableCellEditor extends AbstractCellEditor implements TableCellEditor, ItemListener{
        private static final String EMPTY_STRING = "";
        int selRow=0;
        private CoeusTextField txtLabel;
        private CoeusComboBox cmbModule, cmbSubModule;
        private int column;
        private ModuleDataBean moduleDataBean;
        private SubModuleDataBean subModuleDataBean;
        //Code addded for Case#2785 - Routing Enhancements
        private CoeusComboBox cmbRule;
        // 4272: Maintain history of Questionnaires 
        private CoeusComboBox cmbMandatory;
        ComboBoxBean comboBoxBean = new ComboBoxBean();
        /** An Inner class of the BudgetPersoncontroller. Editor for the table component */
        UsageTableCellEditor(){
            
            txtLabel = new CoeusTextField();
            txtLabel.setDocument(new LimitedPlainDocument(50));
            cmbModule = new CoeusComboBox();
            cmbSubModule = new CoeusComboBox();
            cmbModule.addItemListener(this);
            //Code addded for Case#2785 - Routing Enhancements - starts
            cmbSubModule.addItemListener(this);
            cmbRule = new CoeusComboBox();
            comboBoxBean.setCode(EMPTY_STRING);
            comboBoxBean.setDescription(EMPTY_STRING);
            //Code addded for Case#2785 - Routing Enhancements - ends
            // 4272: Maintain history of Questionnaires 
            cmbMandatory = new CoeusComboBox();
        }
        
        /** Populate all the data for the application modules
         */
        private void populateModuleCode() {
            ModuleDataBean comboBean = new ModuleDataBean();
            comboBean.setCode(EMPTY_STRING);
            comboBean.setDescription(EMPTY_STRING);
            cvModuleData.add(0,comboBean);
            if(cvModuleData!= null && cvModuleData.size() > 0){
                for(int index = 0; index <cvModuleData.size(); index++){
                    moduleDataBean = (ModuleDataBean)cvModuleData.get(index);
                    cmbModule.addItem(moduleDataBean);
                }
            }
        }
     
        /** Popup the subModule data based on the module code which is passed
         */
        private void populateSubModuleCode(String code){
            if(code.equals(EMPTY_STRING)){
                return ;
            }
            cmbSubModule.removeAllItems();
            SubModuleDataBean newBean = null;
            int selectedCode = Integer.parseInt(code);
            Equals eqData = new Equals("moduleCode", new Integer(selectedCode));
            CoeusVector cvData = cvSubModuleData.filter(eqData);
            if(cvData!= null && cvData.size() > 0){
                newBean = new SubModuleDataBean();
                newBean.setCode(EMPTY_STRING);
                newBean.setDescription(EMPTY_STRING);
                cvData.add(0,newBean);
                for(int index = 0; index < cvData.size(); index++){
                    subModuleDataBean = (SubModuleDataBean)cvData.get(index);
                    cmbSubModule.addItem(subModuleDataBean);
                }
            }
        }
        
        /** 
         * Code addded for Case#2785 - Routing Enhancements
         * Popup the Rule data based on the module code which is passed
         */
        private void populateRuleData(String moduleCode, String subModuleCode){
            if(moduleCode == null || moduleCode.equals(EMPTY_STRING)){
                moduleCode = "0";
            }
            if(subModuleCode == null || subModuleCode.equals(EMPTY_STRING)){
                subModuleCode = "0";
            }
            cmbRule.removeAllItems();
            BusinessRuleBean newBean = null;
            int moduleCodeVal = Integer.parseInt(moduleCode);
            int subModuleCodeVal = Integer.parseInt(subModuleCode);
//            Equals eqModuleCode = new Equals("moduleCode", new Integer(moduleCodeVal));
//            Equals eqSubModuleCode = new Equals("submoduleCode", new Integer(subModuleCodeVal));
            Equals eqModuleCode = new Equals("moduleCode", ""+moduleCodeVal);
            Equals eqSubModuleCode = new Equals("submoduleCode", ""+subModuleCodeVal);
            And andData = new And(eqModuleCode, eqSubModuleCode);
            CoeusVector cvData = cvRuleData.filter(andData);
            if(cvData!= null && cvData.size() > 0){
                newBean = new BusinessRuleBean();
                newBean.setCode(EMPTY_STRING);
                newBean.setDescription(EMPTY_STRING);
                cvData.add(0,newBean);
                for(int index = 0; index < cvData.size(); index++){
                    BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvData.get(index);
                    cmbRule.addItem(businessRuleBean);
                }
            }
        }        
        
        /** overridden method of the Editor
         * @param table
         * @param value
         * @param isSelected
         * @param row
         * @param column
         * @return
         */
        public Component getTableCellEditorComponent(JTable table,Object value,
        boolean isSelected, int row,int column){
            this.column = column;
            switch(column){
                case LABEL_COLUMN:
                    txtLabel.setText(value.toString());
                    return txtLabel;
                case MODULE_CODE_COLUMN:
                    if(! isModuleCodeSelected) {
                        populateModuleCode();
                        isModuleCodeSelected = true;
                    }
                    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
                    // When rule condition is defined, questionnaire usage change the module
                    if(hasDefinedRuleCondition()){
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(QUESTION_USES_CONDITIONAL_BRANCH));
                        return null;
                    }
                    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
                    cmbModule.setSelectedItem(value);
                    return cmbModule;
                case SUBMODULE_ITEM_CODE:
                    Object data = questionnaireDetailForm.tblUsage.getValueAt(row, MODULE_CODE_COLUMN);
                    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
                    // When rule condition is defined, questionnaire usage change the module
                     if(hasDefinedRuleCondition()){
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(QUESTION_USES_CONDITIONAL_BRANCH));
                        return null;
                     }
                    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - End
                    if(data instanceof ModuleDataBean){
                        populateSubModuleCode(((ModuleDataBean)data).getCode());
                        //        	            cmbSubModule.setSelectedItem(value.toString());
                        cmbSubModule.setSelectedItem(value);
                    }
                    return cmbSubModule;
                //Code addded for Case#2785 - Routing Enhancements - starts
                case RULE_COLUMN:
                    Object moduleData = questionnaireDetailForm.tblUsage.getValueAt(row, MODULE_CODE_COLUMN);
                    Object subModuleData = questionnaireDetailForm.tblUsage.getValueAt(row, SUBMODULE_ITEM_CODE);
                    if(moduleData instanceof ModuleDataBean
                            && subModuleData instanceof SubModuleDataBean){
                        populateRuleData(((ModuleDataBean)moduleData).getCode(), 
                                ((SubModuleDataBean)subModuleData).getCode());
                        if(value != null && value.equals(EMPTY_STRING)){
                            cmbRule.setSelectedItem(comboBoxBean);
                        }else{
                            cmbRule.setSelectedItem(value);
                        }
                    } else if(moduleData instanceof ModuleDataBean
                            && subModuleData.equals(EMPTY_STRING)){
                        populateRuleData(((ModuleDataBean)moduleData).getCode(), 
                                EMPTY_STRING);
                        if(value != null && value.equals(EMPTY_STRING)){
                            cmbRule.setSelectedItem(comboBoxBean);
                        }else{
                            cmbRule.setSelectedItem(value);
                        }
                        
                    }
                    return cmbRule;                    
               //Code addded for Case#2785 - Routing Enhancements - ends
               // 4272: Maintain history of Questionnaires - Start
                case MANDATORY_COLUMN:
                    populateMandatoryData();
  
                    cmbMandatory.setSelectedIndex(1);
                    return cmbMandatory;
                // 4272: Maintain history of Questionnaires - End     
            }
            return txtLabel;
        }
        public Object getCellEditorValue() {
            switch (column) {
                case LABEL_COLUMN:
                    return txtLabel.getText();
                case MODULE_CODE_COLUMN:
                    return cmbModule.getSelectedItem();
                case SUBMODULE_ITEM_CODE:
                    return cmbSubModule.getSelectedItem();
                //Code addded for Case#2785 - Routing Enhancements - starts
                case RULE_COLUMN:
                    return cmbRule.getSelectedItem();
                //Code addded for Case#2785 - Routing Enhancements - ends
                // 4272: Maintain history of Questionnaires - Start
                case MANDATORY_COLUMN:
                    return cmbMandatory.getSelectedItem();
                // 4272: Maintain history of Questionnaires - End
            }
            return ((CoeusComboBox)cmbSubModule).getSelectedItem();
        }
        
        public void itemStateChanged(ItemEvent itemEvent) {
            Object source = itemEvent.getSource();
            
            if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
                if(source.equals(cmbModule)){
                    String code = ((ComboBoxBean)cmbModule.getSelectedItem()).getCode();
                    if(code.equals(EMPTY_STRING)) return ;
                    cmbSubModule.removeAllItems();
                    //Added for COEUSDEV-86 : questionnaire for Submission - Start
                    cmbRule.removeAllItems();
                    //COEUSDEV-86 : End
                    //Code addded for Case#2785 - Routing Enhancements - starts
                    populateSubModuleCode(code);
                    String subModuleCode = ((ComboBoxBean)cmbSubModule.getSelectedItem()).getCode();
                    populateRuleData(code, subModuleCode);
                } else if(source.equals(cmbSubModule)){
                    String moduleCode = ((ComboBoxBean)cmbModule.getSelectedItem()).getCode();
                    String subModuleCode = ((ComboBoxBean)cmbSubModule.getSelectedItem()).getCode();
                    if(moduleCode.equals(EMPTY_STRING)
                    && subModuleCode.equals(EMPTY_STRING)) return ;
                    cmbRule.removeAllItems();
                    populateRuleData(moduleCode, subModuleCode);

                }

                //Code addded for Case#2785 - Routing Enhancements - ends
            }
            
        }
        
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
        // 4272: Maintain history of Questionnaires - Start
        private void populateMandatoryData() {
            cmbMandatory.removeAllItems();
            
            cmbMandatory.addItem(new ComboBoxBean("1","Yes"));
            cmbMandatory.addItem(new ComboBoxBean("0","No"));
        }
        // 4272: Maintain history of Questionnaires - End
    }
    
    public class UsageTableCellRenderer extends DefaultTableCellRenderer{
        private CoeusTextField txtComponent;
        private static final String EMPTY_STRING = "";
        /** */
        UsageTableCellRenderer(){
            txtComponent = new CoeusTextField();
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
        }
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus,int row,int column){
            switch(column){
                case LABEL_COLUMN:
                case MODULE_CODE_COLUMN:
                case SUBMODULE_ITEM_CODE:
                // 4272: Maintain history of Questionnaires 
                case MANDATORY_COLUMN:
                //Code addded for Case#2785 - Routing Enhancements
                case RULE_COLUMN:
                    if(isSelected){
                        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                            txtComponent.setBackground(java.awt.Color.YELLOW);
                            txtComponent.setEnabled(false);
                        }else{
                            txtComponent.setBackground(java.awt.Color.YELLOW);
                            txtComponent.setForeground(java.awt.Color.BLACK);
                        }
                    }else{
                        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                            txtComponent.setBackground(disabledBackground);
                            txtComponent.setEnabled(false);
                        }else{
                            txtComponent.setBackground(java.awt.Color.WHITE);
                        }
                    }
                    if(value!= null && !value.equals(EMPTY_STRING)){
                        txtComponent.setText(value.toString());
                    }else{
                        txtComponent.setText(EMPTY_STRING);
                    }
                    return txtComponent;
            }
            return super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);
        }
    }
    
    int row;
    int column;
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = questionnaireDetailForm.tblUsage.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = questionnaireDetailForm.tblUsage.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = questionnaireDetailForm.tblUsage.getRowCount();
                int columnCount = questionnaireDetailForm.tblUsage.getColumnCount();
                if(row==rowCount-1 && column==columnCount-1){
                    row = 0;
                    column = 0;
                    usageTableCellEditor.stopCellEditing();
                    questionnaireDetailForm.btnAddModule.requestFocusInWindow();
                    return;
                }
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                
                while ( table.isCellEditable(row, column) ) {
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    if (row == rowCount) {
                        row = 0;
                    }
                    // Back to where we started, get out.
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
            }
        };
        questionnaireDetailForm.tblUsage.getActionMap().put(im.get(tab), tabAction);
        
        
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = questionnaireDetailForm.tblUsage.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                column = questionnaireDetailForm.tblUsage.getSelectedColumn();
                row = questionnaireDetailForm.tblUsage.getSelectedRow();
                int rowCount = questionnaireDetailForm.tblUsage.getRowCount();
                int columnCount = questionnaireDetailForm.tblUsage.getColumnCount();
                if(row==0 && column==0){
                    row = 0;
                    column = 0;
                    usageTableCellEditor.stopCellEditing();
                    questionnaireDetailForm.btnAddModule.requestFocusInWindow();
                    return;
                }
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                
            }
        };
        questionnaireDetailForm.tblUsage.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * To set the default root level node in the tree.
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void setTreeLabel() throws CoeusException{
        QuestionnaireBean questionnaireBean = new QuestionnaireBean();
        questionnaireBean.setCondition(EMPTY_STRING);
        questionnaireBean.setConditionValue(EMPTY_STRING);
        questionnaireBean.setConditionalFlag(false);
        questionnaireBean.setParentQuestionNumber(new Integer(-1));
        questionnaireBean.setQuestionNumber(new Integer(0));
        questionnaireBean.setAcType("N");
        questionnaireBean.setQuestionId(new Integer(0));
        questionnaireBean.setDescription("Questions");
        cvQuestionnaireQuestionData.add(0, questionnaireBean);
    }
    
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * Method to insert the new question in the questionnaire tree.
     * @throws edu.mit.coeus.exception.CoeusException
     */
    private void insertQuestionDetails() throws CoeusException{
        AddQuestionnaireQuestionController controller = new AddQuestionnaireQuestionController();
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        if(cvQuestionnaireUsageData == null || cvQuestionnaireUsageData.size() > 1){
            controller.setDisableRuleCondtion(true);
        }else if(cvQuestionnaireUsageData.size() == 1){
            QuestionnaireUsageBean questionnaireUsageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.get(0);
            if(questionnaireUsageBean.getModuleItemCode() != 0){
                controller.setDisableRuleCondtion(true);
                controller.setQuestionnaireUsageBean(questionnaireUsageBean);
                controller.setRuleData(cvRuleData);
            }else{
                controller.setDisableRuleCondtion(false);
            }
            
        }
        controller.setQuestionnareId(this.baseBean.getQuestionnaireNumber());
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        
        TreePath selTreePath = qTree.getSelectionPath();
        //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
//        QuestionnaireNode selNode = (QuestionnaireNode)selTreePath.getLastPathComponent();
//        QuestionnaireBean selParentBean = ((QuestionnaireNode)selNode).getDataObject();
        selectedNode = (QuestionnaireNode)selTreePath.getLastPathComponent();
        QuestionnaireBean selParentBean = ((QuestionnaireNode)selectedNode).getDataObject();
        //COEUSDEV-227 : End
        String parentQuestion = selParentBean.getDescription();
        if(selParentBean.getQuestionNumber().intValue() != 0){
            TreePath selParentPath = selTreePath.getParentPath();
            QuestionnaireNode parentNode = (QuestionnaireNode)selParentPath.getLastPathComponent();
            QuestionnaireBean parentarentBean = ((QuestionnaireNode)parentNode).getDataObject();
            if(parentarentBean.getQuestionNumber().intValue() != 0){
                parentQuestion = parentarentBean.getDescription();
            } else {
                parentQuestion = EMPTY_STRING;
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setText("Parent Question");
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setMinimumSize(new java.awt.Dimension(105, 14));
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setPreferredSize(new java.awt.Dimension(105, 14));            
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setEnabled(false);
                controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setForeground(Color.BLACK);
                controller.addQuestionnaireQuestionForm.rdBtnCondition.setEnabled(false);
                controller.addQuestionnaireQuestionForm.txtConditionValue.setEditable(false);
                controller.addQuestionnaireQuestionForm.cmbCondition.setEnabled(false);
                controller.addQuestionnaireQuestionForm.lblIs.setEnabled(false);                
            }
        }else if(selParentBean.getParentQuestionNumber().intValue() == -1){
            parentQuestion = EMPTY_STRING;
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setText("Parent Question");
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setMinimumSize(new java.awt.Dimension(105, 14));
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setPreferredSize(new java.awt.Dimension(105, 14));            
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setEnabled(false);
            controller.addQuestionnaireQuestionForm.lblPreviousQuestion.setForeground(Color.BLACK);
            controller.addQuestionnaireQuestionForm.rdBtnCondition.setEnabled(false);
            controller.addQuestionnaireQuestionForm.txtConditionValue.setEditable(false);
            controller.addQuestionnaireQuestionForm.cmbCondition.setEnabled(false);
            controller.addQuestionnaireQuestionForm.lblIs.setEnabled(false);
        }
        // Get the data from the form
        QuestionMaintenanceController questionMaintenanceController =
                new QuestionMaintenanceController("Questions", mdiForm.getUnitNumber(),true);
        questionMaintenanceController.setFormData(null);
        questionMaintenanceController.display();
        QuestionBaseBean questBaseBean = questionMaintenanceController.getQuestionForQuestionnaire();
        if(questionMaintenanceController.isOkClicked()){
            controller.setFormData(questBaseBean);
        } else {
            return;
        }
        QuestionnaireBean dataBean = new QuestionnaireBean();
        dataBean.setQuestionId(questBaseBean.getQuestionId());
        // 4272: Maintain history of Questionnaires 
        dataBean.setQuestionVersionNumber(new Integer(questBaseBean.getVersionNumber()));
        dataBean.setDescription(questBaseBean.getDescription());
        controller.setParentQuestion(parentQuestion);
        dataBean.setAcType(TypeConstants.INSERT_RECORD);
        controller.setFormData(dataBean);
        controller.display();
        if(controller.isOkClicked()){
            modified = true;
            dataBean = (QuestionnaireBean)controller.getFormData();
            dataBean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
            // 4272: Maintain history of Questionnaires 
            dataBean.setQuestionnaireVersionNumber(questionnaireVersionNumber);
            dataBean.setQuestionNumber(new Integer(getMaxQuestionNumber()));
            dataBean.setParentQuestionNumber(selParentBean.getParentQuestionNumber());
            dataBean.setQuestionSequenceNumber(selParentBean.getQuestionSequenceNumber());
            int insertPosition = insertReOrderQuestions(selParentBean.getParentQuestionNumber().intValue(),
                    selParentBean.getQuestionSequenceNumber());
            dataBean.setAcType(TypeConstants.INSERT_RECORD);
            cvQuestionnaireQuestionData.add(insertPosition, dataBean);
            queryEngine.addCollection(queryKey,QuestionnaireBean.class,cvQuestionnaireQuestionData);
            enableDisableButtons();
        }
        //Added for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
        questionForSelection = dataBean;
        //COEUSDEV-227 : End
        buildQuessionnaireData();
    }
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * Method to reorder the Question sequence number while inserting a question
     * @param parentQuestionNumber 
     * @param questionSequenceNumber 
     * @return index to insert the new question
     */
    private int insertReOrderQuestions(int parentQuestionNumber, int questionSequenceNumber){
        int insertPosition = 0;
        if(cvQuestionnaireQuestionData != null && cvQuestionnaireQuestionData.size()>0){
            boolean isFirst = true;
            for(int index = 0 ; index < cvQuestionnaireQuestionData.size() ; index++){
                QuestionnaireBean dataBean = (QuestionnaireBean) cvQuestionnaireQuestionData.get(index);
                if(dataBean.getParentQuestionNumber().intValue() == parentQuestionNumber
                        && dataBean.getQuestionSequenceNumber() >= questionSequenceNumber){
                    if(isFirst){
                        isFirst = false;
                        insertPosition = index;
                    }
                    int newQuesSeaNum = dataBean.getQuestionSequenceNumber();
                    dataBean.setQuestionSequenceNumber(++newQuesSeaNum);
                    if(dataBean.getAcType() == null 
                            || !dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        dataBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
        }
        return insertPosition;
    }
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * While deleting a question from the tree, it will reorder the Question Sequence Number.
     * @param parentQuestionNumber 
     * @param questionSequenceNumber 
     */
    private void deleteReOrderQuestions(int parentQuestionNumber, int questionSequenceNumber){
        if(cvQuestionnaireQuestionData != null && cvQuestionnaireQuestionData.size()>0){
            for(int index = 0 ; index < cvQuestionnaireQuestionData.size() ; index++){
                QuestionnaireBean dataBean = (QuestionnaireBean) cvQuestionnaireQuestionData.get(index);
                if(dataBean.getParentQuestionNumber().intValue() == parentQuestionNumber
                        && dataBean.getQuestionSequenceNumber() >= questionSequenceNumber){
                    int newQuesSeaNum = dataBean.getQuestionSequenceNumber();
                    dataBean.setQuestionSequenceNumber(--newQuesSeaNum);
                    if(dataBean.getAcType() == null 
                            || !dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        dataBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
        }
    }
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * Method used to set the Questionnaire id and it's acType as Insert 
     * while copying the questionnaire
     * @param cvData 
     */
    private void copyQuestionnaireDatas(CoeusVector cvData){
        if(cvData != null && cvData.size() > 0){
            for(int index = 0 ; index < cvData.size() ; index++){
                QuestionnaireBaseBean bean = (QuestionnaireBaseBean) cvData.get(index);
                bean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
                // 4272:  Maintain History of Questionnaires 
                bean.setQuestionnaireVersionNumber(1);
                bean.setAcType(TypeConstants.INSERT_RECORD);
            }
        }
    }
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * Method to move the selected question upwards od downwards.
     * @param moveUp 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void moveQuestions(boolean moveUp) throws CoeusException{
        //Modified for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
//        TreePath selTreePath = qTree.getSelectionPath();
//        QuestionnaireNode selNode = (QuestionnaireNode)selTreePath.getLastPathComponent();
//        QuestionnaireBean selParentBean = ((QuestionnaireNode)selNode).getDataObject();
//        int parentQuestionNumber = selParentBean.getParentQuestionNumber().intValue();
//        int questionSequenceNumber = selParentBean.getQuestionSequenceNumber();
//        int indexToAdd = 0;
//        int seqToChange = questionSequenceNumber+1;
//        Equals eqParentQuesNum = new Equals("parentQuestionNumber", selParentBean.getParentQuestionNumber());
//        if(moveUp){
//            seqToChange = questionSequenceNumber-1;
//        }
//        int childCount = cvQuestionnaireQuestionData.filter(eqParentQuesNum).size();
//        if(cvQuestionnaireQuestionData != null && cvQuestionnaireQuestionData.size()>0){
//            if(seqToChange <= 0){           
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_MOVE_UP));
//                return;
//            } else if(childCount <= 1 || childCount < seqToChange){
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_MOVE_DOWN));
//                return;                
//            }
//            for(int questionIndex = 0 ; questionIndex < cvQuestionnaireQuestionData.size() ; questionIndex++){
//                QuestionnaireBean dataBean = (QuestionnaireBean) cvQuestionnaireQuestionData.get(questionIndex);
//                if(dataBean.getParentQuestionNumber().intValue() == parentQuestionNumber
//                        && dataBean.getQuestionSequenceNumber() == seqToChange){
//                    indexToAdd = questionIndex;
//                    dataBean.setQuestionSequenceNumber(questionSequenceNumber);
//                    if(dataBean.getAcType() == null
//                            || !dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
//                        dataBean.setAcType(TypeConstants.UPDATE_RECORD);
//                    }
//                    setModified(true);
//                } else if(dataBean.getParentQuestionNumber().intValue() == parentQuestionNumber
//                        && dataBean.getQuestionSequenceNumber() == questionSequenceNumber){
//                    dataBean.setQuestionSequenceNumber(seqToChange);
//                    if(dataBean.getAcType() == null
//                            || !dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
//                        dataBean.setAcType(TypeConstants.UPDATE_RECORD);
//                    }
//                    setModified(true);
//                    selParentBean = dataBean;
//                    cvQuestionnaireQuestionData.remove(questionIndex--);
//                }
//            }
//            if(moveUp){
//                cvQuestionnaireQuestionData.add(indexToAdd, selParentBean);
//            } else {
//                cvQuestionnaireQuestionData.add(++indexToAdd, selParentBean);
//            }
            //Added for COEUSDEV-227 : Issue with Questionnaire building - defaults to expanded question hierarchy upon refresh - Start
//            questionForSelection = selParentBean;
            //COEUSDEV-227 : End
//            buildQuessionnaireData();
//        }
        TreePath path = qTree.getSelectionPath();
        QuestionnaireNode selNode = (QuestionnaireNode) path.getLastPathComponent();
        QuestionnaireBean dataBean = ((QuestionnaireNode)selNode).getDataObject();
        
        // if no question is selected, show error msg and return
        if(path == null) {
            CoeusOptionPane.showInfoDialog("No question is selected");
            return;
        }
        selectedNode = (QuestionnaireNode)path.getLastPathComponent();
        QuestionnaireNode parentNode = (QuestionnaireNode)selNode.getParent();
        int questionIndex = questionnaireTreeModel.getIndexOfChild(parentNode, selectedNode);
        questionForSelection = dataBean;
        if(moveUp){
            if(questionIndex != 0) {
                questionnaireTreeModel.removeNodeFromParent(selectedNode);
                int selectedSequence = dataBean.getQuestionSequenceNumber();
                Equals eqSequenceNumber = new Equals("questionSequenceNumber", dataBean.getQuestionSequenceNumber()-1);
                Equals eqParentQuestionNumber = new Equals("parentQuestionNumber", dataBean.getParentQuestionNumber());
                And sequenceAndParentQuesNum = new And(eqSequenceNumber , eqParentQuestionNumber);
                CoeusVector cvNextQuestion = cvQuestionnaireQuestionData.filter(sequenceAndParentQuesNum);
                if(cvNextQuestion != null && cvNextQuestion.size() > 0){
                    QuestionnaireBean nextQuestionBean = (QuestionnaireBean)cvNextQuestion.get(0);
                    nextQuestionBean.setQuestionSequenceNumber(selectedSequence);
                }
                dataBean.setQuestionSequenceNumber(dataBean.getQuestionSequenceNumber()-1);
                if(dataBean.getAcType() == null
                        || !dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    dataBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                //Question is moved one level up
                questionnaireTreeModel.insertNodeInto(selNode, parentNode, questionIndex-1);
                setModified(true);
            } else {
                // if selected question is first
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_MOVE_UP));
                return;
            }
        }else{
            if(questionIndex < parentNode.getChildCount()-1){
                questionnaireTreeModel.removeNodeFromParent(selectedNode);
                int selectedSequence = dataBean.getQuestionSequenceNumber();
                Equals eqSequenceNumber = new Equals("questionSequenceNumber", dataBean.getQuestionSequenceNumber()+1);
                Equals eqParentQuestionNumber = new Equals("parentQuestionNumber", dataBean.getParentQuestionNumber());
                And sequenceAndParentQuesNum = new And(eqSequenceNumber , eqParentQuestionNumber);
                CoeusVector cvNextQuestion = cvQuestionnaireQuestionData.filter(sequenceAndParentQuesNum);
                if(cvNextQuestion != null && cvNextQuestion.size() > 0){
                    QuestionnaireBean nextQuestionBean = (QuestionnaireBean)cvNextQuestion.get(0);
                    nextQuestionBean.setQuestionSequenceNumber(selectedSequence);
                }
                dataBean.setQuestionSequenceNumber(dataBean.getQuestionSequenceNumber()+1);
                if(dataBean.getAcType() == null
                        || !dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    dataBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                //question is moved one level down
                questionnaireTreeModel.insertNodeInto(selectedNode, parentNode, questionIndex+1);
                setModified(true);
            }else{
                // if selected question is last
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_MOVE_DOWN));
                return;
            }
        }
        //COEUSDEV-227 : END
    }
    
    /**
     * Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
     * To enable and disable buttons while selecting the question in the tree.
     */    
    private void enableButtonsForSelection(){
        TreePath selectionPath = qTree.getSelectionPath();
        int selRow=qTree.getRowForPath(selectionPath);
        if(selRow == 0 && !finalFlagChecked && getFunctionType() != TypeConstants.DISPLAY_MODE){
            questionnaireDetailForm.btnAdd.setEnabled(true);
            questionnaireDetailForm.btnViewQuestion.setEnabled(false);
            questionnaireDetailForm.btnModify.setEnabled(false);
            questionnaireDetailForm.btnDelete.setEnabled(false);
            questionnaireDetailForm.btnInsert.setEnabled(false);
            questionnaireDetailForm.btnMoveUp.setEnabled(false);
            questionnaireDetailForm.btnMoveDown.setEnabled(false);
        } else if(selRow == 1 && qTree.getRowCount() == 2
                && functionType != TypeConstants.DISPLAY_MODE && !finalFlagChecked){
            questionnaireDetailForm.btnAdd.setEnabled(true);
            questionnaireDetailForm.btnViewQuestion.setEnabled(true);            
            questionnaireDetailForm.btnModify.setEnabled(true);
            questionnaireDetailForm.btnInsert.setEnabled(true);
            questionnaireDetailForm.btnMoveUp.setEnabled(true);
            questionnaireDetailForm.btnMoveDown.setEnabled(true);            
            //Commented COEUSDEV-189 : Can't delete the first question applied to a Questionnaire - Start
            //Question delete button is enabled in editable mode unless questionnaire is final
//            questionnaireDetailForm.btnDelete.setEnabled(false);
            //COEUSDEV-189 : END
            questionnaireDetailForm.btnDelete.setEnabled(true);
        } else if(functionType != TypeConstants.DISPLAY_MODE && !finalFlagChecked){
            questionnaireDetailForm.btnAdd.setEnabled(true);
            questionnaireDetailForm.btnViewQuestion.setEnabled(true);            
            questionnaireDetailForm.btnModify.setEnabled(true);
            questionnaireDetailForm.btnDelete.setEnabled(true);
            questionnaireDetailForm.btnInsert.setEnabled(true);
            questionnaireDetailForm.btnMoveUp.setEnabled(true);
            questionnaireDetailForm.btnMoveDown.setEnabled(true);
        } else if(finalFlagChecked){
            //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires	
            //Disabled 'Add Question', 'Insert Question', 'Move Up', 'Move Down' and 'Delete Question' buttons when final is checked
            questionnaireDetailForm.btnAdd.setEnabled(false);
            questionnaireDetailForm.btnInsert.setEnabled(false);
            questionnaireDetailForm.btnMoveUp.setEnabled(false);
            questionnaireDetailForm.btnMoveDown.setEnabled(false);
            questionnaireDetailForm.btnDelete.setEnabled(false);
            //COEUSDEV-206 : END
            if(selRow == 0){
                //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
                //Button is disabled when no question in the list is selected and final is checked
                questionnaireDetailForm.btnModify.setEnabled(false);
                //COEUSDEV-206 : END
                questionnaireDetailForm.btnViewQuestion.setEnabled(false);
            } else {
                //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
                questionnaireDetailForm.btnModify.setEnabled(true);
                //COEUSDEV-206 : END
                questionnaireDetailForm.btnViewQuestion.setEnabled(true);
            }
        } else {
            if(selRow == 0){
                //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
                //Button is disabled when no question in the list is selected and final is not checked
                questionnaireDetailForm.btnModify.setEnabled(false);
                //COEUSDEV-206 : END
                questionnaireDetailForm.btnViewQuestion.setEnabled(false);
            } else {
                //Added for COEUSDEV-206 : Questionnaire maintenance - need a view-only Modify Question window on final questionnaires
                questionnaireDetailForm.btnModify.setEnabled(true);
                //COEUSDEV-206 : END
                questionnaireDetailForm.btnViewQuestion.setEnabled(true);
            }            
        }
//        // 4272:  Maintain History of Questionnaires - Start
//        if(!finalFlagChecked){
//            questionnaireDetailForm.tblUsage.setEnabled(true);
//            questionnaireDetailForm.btnBrowse.setEnabled(true);
//            questionnaireDetailForm.txtTemplate.setEditable(true);
//            questionnaireDetailForm.btnAddModule.setEnabled(true);
//            if(questionnaireDetailForm.tblUsage.getRowCount() > 0 && getFunctionType()!= TypeConstants.DISPLAY_MODE){
//                questionnaireDetailForm.btnDeleteModule.setEnabled(true);
//            }else{
//                questionnaireDetailForm.btnDeleteModule.setEnabled(false);
//            }
//        } 
//        // 4272:  Maintain History of Questionnaires - End
    }
    //Commented for Routing Release -Start
    //Case#2946 Coeus43 enhancement - start
    //Added for Questionnaire Printing enhancement
    /**
     * Is used to print the questionnaire details
     * @param printAnswers Boolean option to specify to print answers or not
     * @param printAll Boolean option to specify to print all the answered and 
     * unanswered questions (with answers for answered questions)
     * @param printAnsweredOnly Boolean option to specify to print only answered
     * questions with answers
     * @throws CoeusException
     */
    public void printQuestionnaire(boolean printAnswers, boolean printAll, boolean printAnsweredOnly) throws CoeusException {
        RequesterBean requester = new RequesterBean();
        Hashtable htParams = new Hashtable();
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = new QuestionnaireAnswerHeaderBean();
        questionnaireAnswerHeaderBean.setQuestionnaireId(baseBean.getQuestionnaireNumber());
        questionnaireAnswerHeaderBean.setPrintAnswers(printAnswers);
        questionnaireAnswerHeaderBean.setPrintAll(printAll);
        questionnaireAnswerHeaderBean.setPrintOnlyAnswered(printAnsweredOnly);
        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
        questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(baseBean.getQuestionnaireVersionNumber());
        htParams.put("ADMIN_QUESTIONNAIRE","Y");
        //Added for the case# coeusdev-135-Problem printing a questionnaire-end        
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
    //Commented for Routing Release -Start

    //Added for case#3844 - Questionnaire is not modifiable - start
    public boolean isIsModulePresent() {
        return isModulePresent;
    }

    public void setIsModulePresent(boolean isModulePresent) {
        this.isModulePresent = isModulePresent;
    }
    //Added for case#3844 - Questionnaire is not modifiable - end
    //New methods added with case 4287 : Questionnaire Templates - Start
    /* The action handle for Upload Template Button */
    private void performUploadAction(){
        
        fileChooser       = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
        displayProperties = coeusMessageResources.getDisplayProperties();//4287
        String fileFilter = displayProperties.getProperty(TEMPLATE_FILE_FILTER_KEY);
        
        if(fileFilter==null || EMPTY_STRING.equals(fileFilter.trim())){
            fileFilter = "xsl";
        }
        fileChooser.setSelectedFileExtension(fileFilter);
        fileChooser.showFileChooser();
        if(fileChooser.isFileSelected()){
            String fileName = fileChooser.getSelectedFile();//full path
            File file = fileChooser.getFileName();
            if(fileName!=null && file!=null){
                questionnaireTemplateBean = new QuestionnaireTemplateBean();
                questionnaireTemplateBean.setTemplateFileName(fileName);
                questionnaireTemplateBean.setTemplateFileBytes(fileChooser.getFile());
                templateAcType = TypeConstants.UPDATE_RECORD;
                questionnaireDetailForm.txtTemplate.setText(fileName);
                questionnaireDetailForm.btnRemove.setEnabled(true);
                questionnaireDetailForm.txtTemplate.setText(fileName);
            }
        }
    }
    
    /* The action handle for Remove Template Button */
    private void performRemoveAction(){
        int option = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(MSGKEY_DELETE_CONFIRM),
                                CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_NO);
        
        if(option == JOptionPane.YES_OPTION){
            if(hadTemplate){
                templateAcType = TypeConstants.UPDATE_RECORD;
            }else{
                templateAcType = EMPTY_STRING;
            }
            questionnaireTemplateBean.setTemplateFileBytes(null);
            questionnaireDetailForm.txtTemplate.setText(EMPTY_STRING);
            questionnaireDetailForm.btnRemove.setEnabled(false);
            questionnaireDetailForm.btnBrowse.setEnabled(true);
        }
    }
    
    public void setTemplateAcType(String templateAcType) {
        this.templateAcType = templateAcType;
    }
    
    public void setTemplateFileBytes(byte[] templateFileBytes) {
        this.templateFileBytes = templateFileBytes;
    }
    //New methods added with case 4287 - Questionnaire Templates - End
    // 4272: Maintain history of Questionnaires - Start
    public void setVersionMode(String versionMode) {
        this.versionMode = versionMode;
    }

    public void setDataForVersionMode() {
        if(NEW_VERSION.equalsIgnoreCase(versionMode)){
            setModified(true);
            setDatatForNewVersion(cvQuestionnaireData);
            setDatatForNewVersion(cvQuestionnaireUsageData);
            setDatatForNewVersion(cvQuestionnaireQuestionData);
            
            questionnaireDetailForm.chkFinal.setEnabled(true);
            questionnaireDetailForm.chkFinal.setSelected(false);
            
        } else if(CURRENT_VERSION_FINAL.equalsIgnoreCase(versionMode)){
                      
            questionnaireDetailForm.btnAdd.setEnabled(false);
            questionnaireDetailForm.btnDelete.setEnabled(false);
            questionnaireDetailForm.btnModify.setEnabled(false);
            questionnaireDetailForm.btnViewQuestion.setEnabled(false);
            questionnaireDetailForm.btnInsert.setEnabled(false);
            questionnaireDetailForm.btnMoveDown.setEnabled(false);
            questionnaireDetailForm.btnMoveDown.setEnabled(false);
        } 
    }
    
    private void setDatatForNewVersion(CoeusVector cvData){
        if(cvData != null && cvData.size() > 0){
            for(int index = 0 ; index < cvData.size() ; index++){
                QuestionnaireBaseBean bean = (QuestionnaireBaseBean) cvData.get(index);
                bean.setQuestionnaireVersionNumber(questionnaireVersionNumber);
                if((!"N".equalsIgnoreCase(bean.getAcType()) && (bean.getQuestionnaireId() != 0))){
                    bean.setAcType(TypeConstants.INSERT_RECORD);
                }
            }
        }
    }
   
    public void setQuestionnaireVersionNumber(int questionnaireVersionNumber) {
        this.questionnaireVersionNumber = questionnaireVersionNumber;
    }
    // 4272: Maintain history of Questionnaires - End
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /**
     * Method to check whether any rule condition is provided in the questions
     * @return hasDefinedRuleCondition
     */
    public boolean hasDefinedRuleCondition(){
        boolean hasDefinedRuleCondition = false;
        if(cvQuestionnaireQuestionData != null && !cvQuestionnaireQuestionData.isEmpty()){
            for(Object questionData : cvQuestionnaireQuestionData){
                QuestionnaireBean questionnaireBean = (QuestionnaireBean)questionData;
                if(questionnaireBean.getConditionRuleId() > 0){
                    hasDefinedRuleCondition = true;
                    break;
                }
            }
            
        }
        return hasDefinedRuleCondition;
    }
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
}

