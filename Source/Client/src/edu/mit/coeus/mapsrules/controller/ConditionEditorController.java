/*
 * @(#)ConditionEditorController.java 1.0 3/08/02 October 25, 2005, 2:50 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 08-Feb-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.admin.bean.YNQBean;
import edu.mit.coeus.admin.controller.AddModifyQuestionController;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.gui.LookUpWindowConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusTableWindow;
import edu.mit.coeus.gui.CostElementsLookupWindow;
import edu.mit.coeus.mapsrules.bean.BusinessRuleConditionsBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleExpBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleFuncArgsBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleFunctionBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleVariableBean;
import edu.mit.coeus.mapsrules.bean.MapHeaderBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.gui.ConditionEditorForm;
import edu.mit.coeus.mapsrules.gui.ValidationForm;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.bean.QuestionsMaintainanceBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;

import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.OtherLookupBean;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  ajaygm
 */
public class ConditionEditorController extends RuleController
        implements ActionListener,DropTargetListener, LookUpWindowConstants{
    
    //holds the condition editor form
    private ConditionEditorForm conditionEditorForm;
    
    //holds the dialog in the form
    private CoeusDlgWindow dlgConditionEditor;
    
    //the coeus mdiform
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    
    /**
     * Instance of Coeus Message Resources
     */
    private CoeusMessageResources coeusMessageResources;
    
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    //the width of the dialog
    private static final int WIDTH = 850;
    //the height of the dialog
    //private static final int HEIGHT = 520;
    private static final int HEIGHT = 555;
    
    //the title of the window
    private String windowTitle = "Condition Editor";
     //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
    //the title of the columns tab
    private static final String COLUMNS_TAB = "Columns";
    
    //the title of the questions tab
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    //private static final String QUESTIONS_TAB="Questions";
    private static final String QUESTIONS_TAB="YNQ";
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
    //the title of  the functions tab
    private static final String FUNCTIONS_TAB = "Functions";
    
    //the scrollpane for the columns tab
    private JScrollPane scrPnColumns = null;
    
    //the scrollpane for the questions tab
    private JScrollPane scrPnQuestions = null;
    
    //the scrollpane for the functions tab
    private JScrollPane scrPnFunctions = null;
    
    //the columnscontroller
    private ColumnsController columnsController;
    
    //the questions controller
    private QuestionsController questionsController;
    
    //the functions controller
    private FunctionsController functionsController;
    
    //the tablemodel for the form
    private ConditionEditorTableModel conditionEditorTableModel;
    
    //the renderer for the form
    private ConditionEditorTableRenderer conditionEditorTableRenderer;
    
    //the L_VALUE column index
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    //Added one more column for icon. Incremented the existing columns to 1
    // Column restructured with COEUSQA-2458-Make 'and' and 'or' work in business rules
    //the prefix column index
    private static final int EXPRESSION_PREFIX = 1;
    
    private static final int L_VALUE = 2;
    
    //the operator column index
    private static final int OPERATOR = 3;
    
    //the rvalue column index
    private static final int R_VALUE = 4;
    
    //the suffix column index
    private static final int EXPRESSION_SUFFIX = 5;
    
    //the logial column index
    private static final int LOGICAL = 6;
    // COEUSQA-2458-End
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    //to get the function data
    private static final char GET_FUNCTION_DATA = 'C';
    
    //to check for the arg window
    private static final char CHECK_FOR_ARGS = 'H';
    
    //to get the data type for validation
    private static final char GET_DATATYPE_FOR_VALIDATION = 'L';
    
    //servlet to get the data
    private static final String RULE_SERVLET = "/RuleMaintenanceServlet";
    
    //an empty string
//    private static final String EMPTY_STRING = "";
    
    //to hold the columns tab data
    private CoeusVector cvColumnsData;
    
    //to hold the questions tab data
    private CoeusVector cvQuestionsData;
    
    //to hold the functions tab data
    private CoeusVector cvFunctionsData;
    
    //to hold the expressions tab data
    private CoeusVector cvExpressionData;
    
    //to hold the argument data
    private CoeusVector cvRuleFuncArgs;
    
    //the editor for the form
    private ConditionTableEditor conditionTableEditor;
    
    //holds the logical operators
    private CoeusVector cvLogicalOperators;
    
    //to hold the operators for the operatirs column
    private CoeusVector cvOperators;
    
    //holds the operatirs when it is of functions type
    private CoeusVector cvFunctionsOperators;
    
    //holds the rvalues for questions tab
    private CoeusVector cvQuestionsRValues;
    
    //holds the r values for functions tab
    private CoeusVector cvFunctionsRValues;
    
    //holds the deleted beans
    private CoeusVector cvDeletedData;
    
    //holds the function type
    private char functionType;
    
    //holds the action performed like insert,add
    private char actionMode;
    
    //holds the ruleBaseBean
    private RuleBaseBean ruleBaseBean;
    
    //holds the businessRuleConditionsBean
    BusinessRuleConditionsBean businessRuleConditionsBean  = new BusinessRuleConditionsBean();
    
    //ComboBox used in the table
    private CoeusComboBox cmbOperatorType,cmbLogicalOperator,cmbRValue;
    
    private DateUtils dateUtils = new DateUtils();
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    
    //the target drop of the drag and drop
    private DropTarget targetDrop;
    
    private boolean modified = false;
    
    private boolean typeValidate = false;
    
    //Constants for the Expresstion Type
    private static final String TYPE_COLUMN = "D";
    private static final String TYPE_QUESTION = "Q";
    private static final String TYPE_FUNCTION= "F";
    
    // the background color
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
    
    private static final String ROUTING_LABEL_TEXT = "If this condition is true, the following map will be used for routing: ";
    
    private static final String NOTIFICATION_LABEL_TEXT = "If this condition is true, the following map will be used for Notification: ";
    
    //private static final String VALIDATION_LABEL_TEXT = "If this condition is true the proposal is :                      ";
    
    private boolean okClicked = false;
    
    private ValidationForm validationForm;
    
    private int noOfCondtionsPresent = 0;
    
    private boolean elsePresent = false;
    
    private static char INSERT_ROW = 'I';
    
    //Validation Messages
    private static final String SELECT_MAP = "conditionEditor_exceptionCode.1101";
    private static final String ENTER_DESCRIPTION = "conditionEditor_exceptionCode.1102";
    private static final String DEFINE_COND = "conditionEditor_exceptionCode.1103";
    private static final String COMPLETE_EXP = "conditionEditor_exceptionCode.1104";
    private static final String DATATYPE_MISMATCH = "conditionEditor_exceptionCode.1105";
    private static final String NO_ROWS = "conditionEditor_exceptionCode.1106";
    private static final String SEL_ROW = "conditionEditor_exceptionCode.1107";
    private static final String DELETE_CONFIRMATION = "conditionEditor_exceptionCode.1108";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    //Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
    private static final String ERRKEY_INVALID_PREFIX = "conditionEditor_exceptionCode.1110";
    private static final String ERRKEY_INVALID_SUFFIX = "conditionEditor_exceptionCode.1111";
    private static final String ERRKEY_INVALID_PARENTHESIS = "conditionEditor_exceptionCode.1112";
    // COEUSQA-2458-End
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    //the scrollpane for the questionnaire tab
    private static final String ENTER_USER_MESSAGE = "conditionEditor_exceptionCode.1109";
    private JScrollPane scrPnQuestionnaires = null;
    private static final String TYPE_QUESTIONNAIRE_QN = "R";
    private String moduleCode;
    private String subModuleCode;
    private static final String QUESTIONNAIRE_TAB="Questionnaire";
    private CoeusVector cvQuestionnaireData;
    private Map hmQuestionnaireQns;
    private ImageIcon iIcnDetails;
    private static final int DETAILS_ICON_COLUMN = 0;
    
    public static final String CONTAINS = "CONTAINS";
    public static final String BEGINS_WITH = "BEGINS WITH";
    public static final String ENDS_WITH = "ENDS WITH";
    public static final String EQUAL_TO = "EQUAL TO";
    public static final String LESS_THAN = "LESS THAN";
    public static final String GREATER_THAN = "GREATER THAN";
    public static final String LESS_THAN_EQUAL_TO = "LESS THAN OR EQUAL TO";
    public static final String GREATER_THAN_EQUAL_TO = "GREATER THAN OR EQUAL TO";
    public static final String NOT_EQUAL_TO = "NOT EQUAL TO";
    private static final String QUESTION_LABEL_TEXT = "If this condition is true, the following map will be used for questions: ";
    
    private static final char GET_YNQ_EXPLANATION = 'G';
    private QuestionnaireController questionnaireController;
    private String ruleType;
    private CoeusVector cvQuestionaireOperators;
    private CoeusVector cvProposalQuestions;
    //holds the map selected
    private MapHeaderBean mapHeaderBean;
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    private boolean validatingForm;
    /** Creates a new instance of ConditionEditorController */
    public ConditionEditorController(RuleBaseBean ruleBaseBean,char functionType,char actionMode)
    throws CoeusException{
        this.ruleBaseBean = ruleBaseBean;
        this.mdiForm = mdiForm;
        this.functionType = functionType;
        this.actionMode = actionMode;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        iIcnDetails = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setTableEditors();
        postInitComponents();
    }
    
    
    //to instantiate the form and the dialog
    private void postInitComponents(){
        dlgConditionEditor = new CoeusDlgWindow(mdiForm);
        dlgConditionEditor.setResizable(false);
        dlgConditionEditor.setModal(true);
        dlgConditionEditor.getContentPane().add(conditionEditorForm);
        dlgConditionEditor.setFont(CoeusFontFactory.getLabelFont());
        dlgConditionEditor.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgConditionEditor.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgConditionEditor.getSize();
        dlgConditionEditor.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        //Modifed for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //dlgConditionEditor.setTitle(WINDOW_TITLE);
        dlgConditionEditor.setTitle(windowTitle);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        dlgConditionEditor.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        dlgConditionEditor.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
        
        dlgConditionEditor.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgConditionEditor.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        //code for disposing the window ends
        validationForm.txtDescription.setDocument(new LimitedPlainDocument(200));
        conditionEditorForm.txtDescription.setDocument(new LimitedPlainDocument(200));
    }
    
    /** To set the default focus for the component
     */
    public void requestDefaultFocus(){
        if(functionType == TypeConstants.DISPLAY_MODE){
            conditionEditorForm.btnCancel.requestFocus();
        }else{
            conditionEditorForm.btnOk.requestFocus();
        }
    }
    
    
    /* setting the renderers and sizes for columns of the table*/
    private void setTableEditors(){
        
        conditionEditorForm.tblRules.setRowHeight(22);
        
        JTableHeader tableHeader = conditionEditorForm.tblRules.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        conditionEditorForm.tblRules.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        tableHeader.setResizingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
        TableColumn column = conditionEditorForm.tblRules.getColumnModel().getColumn(EXPRESSION_PREFIX);
        column.setMinWidth(20);
        column.setResizable(true);
        column.setCellEditor(conditionTableEditor);
        column.setCellRenderer(conditionEditorTableRenderer);
        tableHeader.setReorderingAllowed(false);
        // COEUSQA-2458-End
        column = conditionEditorForm.tblRules.getColumnModel().getColumn(L_VALUE);
        column.setMinWidth(200);
        column.setResizable(true);
        column.setCellEditor(conditionTableEditor);
        column.setCellRenderer(conditionEditorTableRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = conditionEditorForm.tblRules.getColumnModel().getColumn(OPERATOR);
        column.setMinWidth(180);
        column.setResizable(true);
        column.setCellEditor(conditionTableEditor);
        column.setCellRenderer(conditionEditorTableRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = conditionEditorForm.tblRules.getColumnModel().getColumn(R_VALUE);
        //COEUSQA-3992
        column.setMinWidth(200);
        //COEUSQA-3992
        column.setResizable(true);
        column.setCellEditor(conditionTableEditor);
        column.setCellRenderer(conditionEditorTableRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = conditionEditorForm.tblRules.getColumnModel().getColumn(LOGICAL);
        column.setMinWidth(60);
        column.setResizable(true);
        column.setCellEditor(conditionTableEditor);
        column.setCellRenderer(conditionEditorTableRenderer);
        tableHeader.setReorderingAllowed(false);
        
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Added one more column for displaying the icon 
        column = conditionEditorForm.tblRules.getColumnModel().getColumn(DETAILS_ICON_COLUMN);
        column.setMinWidth(20);
        column.setMaxWidth((20));
        column.setResizable(true);
        column.setCellEditor(conditionTableEditor);
        column.setCellRenderer(conditionEditorTableRenderer);
        tableHeader.setReorderingAllowed(false);
        
        // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
        column = conditionEditorForm.tblRules.getColumnModel().getColumn(EXPRESSION_SUFFIX);
        column.setMinWidth(35);
        column.setResizable(true);
        column.setCellEditor(conditionTableEditor);
        column.setCellRenderer(conditionEditorTableRenderer);
        tableHeader.setReorderingAllowed(false);
        // COEUSQA-2458-end
        //Removed the table header
        conditionEditorForm.tblRules.setTableHeader(null);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
    }
    
    //to display the form
    public void display() {
        dlgConditionEditor.setVisible(true);
    }
    
    //to format the fields
    public void formatFields() {
        if(isTypeValidate()){
            /*conditionEditorForm.lblConditionValue.setVisible(false);
            conditionEditorForm.lblConditionValue.setEnabled(false);
             
            conditionEditorForm.cmbCondition.setVisible(true);
            conditionEditorForm.cmbCondition.setEnabled(true);*/
            
            conditionEditorForm.btnMap.setVisible(false);
            conditionEditorForm.btnMap.setEnabled(false);
            
            conditionEditorForm.remove(conditionEditorForm.pnlDescription);
            javax.swing.JPanel pnlDesc = new javax.swing.JPanel();
            pnlDesc.setPreferredSize(conditionEditorForm.pnlDescription.getPreferredSize());
            pnlDesc.setMinimumSize(conditionEditorForm.pnlDescription.getMinimumSize());
            pnlDesc.setLayout(conditionEditorForm.pnlDescription.getLayout());
            pnlDesc.setBorder(conditionEditorForm.pnlDescription.getBorder());
            GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridheight = 2;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.fill = gridBagConstraints.HORIZONTAL;
            pnlDesc.add(validationForm,gridBagConstraints);
            conditionEditorForm.add(pnlDesc, gridBagConstraints);
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        else if(ruleType.equals("Q")){
            conditionEditorForm.btnMap.setVisible(false);
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        else{
            conditionEditorForm.lblConditionValue.setForeground(java.awt.Color.blue);
        }
        
        if(functionType == TypeConstants.DISPLAY_MODE){
            conditionEditorForm.btnOk.setEnabled(false);
            conditionEditorForm.btnMap.setEnabled(false);
            conditionEditorForm.btnAdd.setEnabled(false);
            conditionEditorForm.btnInsert.setEnabled(false);
            conditionEditorForm.btnDelete.setEnabled(false);                                     
            if(isTypeValidate()){
                validationForm.txtDescription.setEditable(false);
                validationForm.txtDescription.setEnabled(false);
                validationForm.txtDescription.setDisabledTextColor(Color.BLACK);
                validationForm.cmbCondition.setEnabled(false);
                //Added for GN450 Issue#23 -Start
                //If user doedn't have right to modify the business rule
                validationForm.txtArUserMessage.setEditable(false);
                validationForm.txtArUserMessage.setEnabled(false);
                validationForm.txtArUserMessage.setDisabledTextColor(Color.BLACK);
                validationForm.txtArUserMessage.setBackground(disabledBackground);
                //Added for GN450 Issue#23 -End
            }else{
                conditionEditorForm.txtDescription.setEditable(false);
                conditionEditorForm.txtDescription.setEnabled(false);
                conditionEditorForm.txtDescription.setDisabledTextColor(Color.BLACK);
                //Added for GN450 Issue#23 -Start
                //If user doedn't have right to modify the business rule
                conditionEditorForm.txtArUserMessage.setEditable(false);
                conditionEditorForm.txtArUserMessage.setEnabled(false);
                conditionEditorForm.txtArUserMessage.setDisabledTextColor(Color.BLACK);
                conditionEditorForm.txtArUserMessage.setBackground(disabledBackground);             
                //Added for GN450 Issue#23 -End
            }
        }//End of outer if
    }//End of format fields
    
    //to get the controlled GUI
    public java.awt.Component getControlledUI() {
        return conditionEditorForm;
    }
    
    //to get the form data
    
    public Object getFormData() {
        HashMap hmData = new HashMap();
        hmData.put(BusinessRuleConditionsBean.class , businessRuleConditionsBean);
        hmData.put(BusinessRuleExpBean.class , cvExpressionData);
        hmData.put(BusinessRuleFuncArgsBean.class , cvRuleFuncArgs);
        hmData.put(CoeusVector.class , cvDeletedData);
        return hmData;
    }
    
    //to register the listeners and the table models
    public void registerComponents() {
        conditionEditorForm = new ConditionEditorForm();
        validationForm= new ValidationForm();
        
        cvColumnsData = new CoeusVector();
        cvQuestionsData = new CoeusVector();
        cvFunctionsData = new CoeusVector();
        cvExpressionData = new CoeusVector();
        cvRuleFuncArgs = new CoeusVector();
        cvLogicalOperators = new CoeusVector();
        cvOperators = new CoeusVector();
        cvFunctionsOperators = new CoeusVector();
        cvQuestionsRValues = new CoeusVector();
        cvFunctionsRValues = new CoeusVector();
        cvDeletedData = new CoeusVector();
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        cvQuestionaireOperators = new CoeusVector();
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        conditionEditorTableModel = new ConditionEditorTableModel();
        conditionTableEditor = new ConditionTableEditor();
        conditionEditorTableRenderer = new ConditionEditorTableRenderer();
        conditionEditorForm.tblRules.setModel(conditionEditorTableModel);
        
        conditionEditorForm.btnAdd.addActionListener(this);
        conditionEditorForm.btnCancel.addActionListener(this);
        conditionEditorForm.btnDelete.addActionListener(this);
        conditionEditorForm.btnInsert.addActionListener(this);
        conditionEditorForm.btnMap.addActionListener(this);
        conditionEditorForm.btnOk.addActionListener(this);
        
        
        
        //conditionEditorForm.tbdPnConditions.addChangeListener(this);
        
        targetDrop = new DropTarget(conditionEditorForm.tblRules,this);
        
        conditionEditorForm.tblRules.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(evt.getClickCount()!=2){
                    return;
                }
                try{
                    int selRow = conditionEditorForm.tblRules.getSelectedRow();
                    if(selRow == -1){
                        return ;
                    }
                    
                    BusinessRuleExpBean businessRuleExpBean =
                            (BusinessRuleExpBean)cvExpressionData.get(selRow);
                    
                    int argPresent = (checkForArgs(businessRuleExpBean.getLvalue())).intValue();
                    
                    if(argPresent == 1){
                        showArgumentWindow(selRow, TypeConstants.MODIFY_MODE);
                    }
                }catch (CoeusException ce){
                    ce.printStackTrace();
                }
            }//End of mouseClicked
        });
    }
    
    
    //to set the form data
    public void setFormData(Object data) throws CoeusException {
        
        HashMap hmpExpressionData = new HashMap();
        hmpExpressionData = (HashMap)data;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        moduleCode = (String)hmpExpressionData.get("moduleCode");
        subModuleCode = (String)hmpExpressionData.get("subModuleCode");
        hmQuestionnaireQns = (HashMap)hmpExpressionData.get(QuestionsMaintainanceBean.class);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
         try{
            cvRuleFuncArgs = (CoeusVector)ObjectCloner.deepCopy((CoeusVector)hmpExpressionData.get(BusinessRuleFuncArgsBean.class));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(actionMode == TypeConstants.MODIFY_MODE){
            cvExpressionData = (CoeusVector)hmpExpressionData.get(BusinessRuleExpBean.class);
            resetExpressionNumbers();
            conditionEditorTableModel.setData(cvExpressionData);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //Only when action mode is INSERT_ROW, there is changed in data
//        }else if(actionMode == TypeConstants.ADD_MODE || actionMode == INSERT_ROW){
//            setModified(true);
//        }
        }else if(actionMode == INSERT_ROW){
            setModified(true);
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        this.businessRuleConditionsBean =
                (BusinessRuleConditionsBean)hmpExpressionData.get(BusinessRuleConditionsBean.class);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        if(businessRuleConditionsBean!=null){
            windowTitle = windowTitle + " for Business Rule "+ businessRuleConditionsBean.getRuleId();
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        dlgConditionEditor.setTitle(windowTitle);
        
        //Commented for case 2418 - Business Rule Evaluation Bug - start
        //cvRuleFuncArgs = (CoeusVector)hmpExpressionData.get(BusinessRuleFuncArgsBean.class);
        //Commented for case 2418 - Business Rule Evaluation Bug - end
        ComboBoxBean cmbBean = (ComboBoxBean)hmpExpressionData.get(ComboBoxBean.class);
        
        if(cmbBean.getCode().equalsIgnoreCase("V")){
            populateConditionCombo();
            setTypeValidate(true);
            String strDesc = businessRuleConditionsBean.getRuleDescription();
            validationForm.txtDescription.setText(strDesc == null ? CoeusGuiConstants.EMPTY_STRING : strDesc.trim());
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            validationForm.txtArUserMessage.setText(businessRuleConditionsBean.getUserMessage()==null ?
                CoeusGuiConstants.EMPTY_STRING :businessRuleConditionsBean.getUserMessage());
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            //conditionEditorForm.lblCondtion.setText(VALIDATION_LABEL_TEXT);
        }else{
            String strCond = businessRuleConditionsBean.getMapDescription();
            conditionEditorForm.lblConditionValue.setText(strCond == null ? CoeusGuiConstants.EMPTY_STRING : strCond.trim());
            //Added for case 2785 - Routing enhancement - start
            mapHeaderBean = new MapHeaderBean();
            mapHeaderBean.setMapDescription(strCond);
            mapHeaderBean.setMapId(businessRuleConditionsBean.getAction());
            //Added for case 2785 - Routing enhancement - end
            if(cmbBean.getCode().equalsIgnoreCase("R")){
                conditionEditorForm.lblCondtion.setText(ROUTING_LABEL_TEXT);
            }
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //Added the condition for Question type
            else if(cmbBean.getCode().equalsIgnoreCase("N")){
                conditionEditorForm.lblCondtion.setText(NOTIFICATION_LABEL_TEXT);
            }else if(cmbBean.getCode().equalsIgnoreCase("Q")){
                conditionEditorForm.lblCondtion.setText(QUESTION_LABEL_TEXT);
            }
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }
        String strDesc = businessRuleConditionsBean.getRuleDescription();
        conditionEditorForm.txtDescription.setText(strDesc == null ? CoeusGuiConstants.EMPTY_STRING : strDesc.trim());
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        conditionEditorForm.txtArUserMessage.setText(businessRuleConditionsBean.getUserMessage()== null ?
            CoeusGuiConstants.EMPTY_STRING : businessRuleConditionsBean.getUserMessage());
        ruleType = cmbBean.getCode();
        if(ruleType.equals("Q")){
            conditionEditorForm.lblCondtion.setVisible(false);
            conditionEditorForm.lblConditionValue.setVisible(false);
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        CoeusVector cvData = new CoeusVector();
        
        //to get the data for all tae tabs in the condition editor
        cvData = getDataForTabs();
        
        if(cvData != null && cvData.size() > 0){
            cvColumnsData = (CoeusVector)cvData.get(0);
            cvQuestionsData = (CoeusVector)cvData.get(1);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//            //Function Names
//            cvFunctionsData.addElement((CoeusVector)cvData.get(2));
//
//            //Fuunction Descriptoin
//            cvFunctionsData.addElement((CoeusVector)cvData.get(3));
            cvQuestionnaireData = new CoeusVector();
            cvQuestionnaireData.add(cvData.get(2));
            cvQuestionnaireData.add(cvData.get(3));
            //Function Names
            cvFunctionsData.addElement((CoeusVector)cvData.get(4));
            
            //Fuunction Descriptoin
            cvFunctionsData.addElement((CoeusVector)cvData.get(5));
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }
        
        if(cvExpressionData != null && cvExpressionData.size()>0){
            conditionEditorForm.tblRules.setRowSelectionInterval(0,0);
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //btnMap is not required if the rule type is validation and question
        //if(!isTypeValidate()){
        //if(!ruleType.equals("V") && !ruleType.equals("Q")){
        if(!ruleType.equals("V")){
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            /** Code for focus traversal - start */
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //Added the txtUserMessage component
            java.awt.Component[] components = null;
            if(ruleType.equals("Q")){
                components = new java.awt.Component[]{ conditionEditorForm.txtDescription,
                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - Start
//                conditionEditorForm.txtArUserMessage, conditionEditorForm.btnOk, conditionEditorForm.btnCancel,
                conditionEditorForm.btnOk, conditionEditorForm.btnCancel,
                conditionEditorForm.btnAdd,conditionEditorForm.btnInsert,conditionEditorForm.btnDelete};
                conditionEditorForm.txtArUserMessage.setEditable(false);
                conditionEditorForm.txtArUserMessage.setEnabled(false);
                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - End
            }else{
                components = new java.awt.Component[]{ conditionEditorForm.txtDescription, conditionEditorForm.txtArUserMessage,
                conditionEditorForm.btnOk, conditionEditorForm.btnCancel,conditionEditorForm.btnMap,
                conditionEditorForm.btnAdd,conditionEditorForm.btnInsert,conditionEditorForm.btnDelete};
            }
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            conditionEditorForm.setFocusTraversalPolicy(traversePolicy);
            conditionEditorForm.setFocusCycleRoot(true);
        }else{
            /** Code for focus traversal - start */
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //Added the txtUserMessage component
            java.awt.Component[] components = null;
            components = new java.awt.Component[]{ validationForm.txtDescription,validationForm.txtArUserMessage,
            validationForm.cmbCondition, conditionEditorForm.btnOk,conditionEditorForm.btnCancel,
            conditionEditorForm.btnAdd,conditionEditorForm.btnInsert, conditionEditorForm.btnDelete};
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            conditionEditorForm.setFocusTraversalPolicy(traversePolicy);
            conditionEditorForm.setFocusCycleRoot(true);
        }
        
        
        
        prepareComboBeans();
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Method signature changed
        initTabComponents(ruleType);
        //Modifeid for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        formatFields();
    }
    
    /**
     * To instantiate the tabs and their controllers
     * 
     * @ruleType type of business rule
     */
    //Modified method signature for Coeus 4.3 Routing enhancement -PT ID:2785
    private void initTabComponents(String ruleType) throws CoeusException{
         //private void initTabComponents() throws CoeusException{
        columnsController = new ColumnsController();
        columnsController.setFormData(cvColumnsData);
        scrPnColumns = new JScrollPane(columnsController.getControlledUI());
        conditionEditorForm.tbdPnConditions.addTab(COLUMNS_TAB, scrPnColumns);
        
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Show the question tab only if the module is Proposal Development whose
        //code is 3
        /*questionsController = new QuestionsController();
        questionsController.setFormData(cvQuestionsData);
        scrPnQuestions = new JScrollPane(questionsController.getControlledUI());
        conditionEditorForm.tbdPnConditions.addTab(QUESTIONS_TAB, scrPnQuestions);*/
        
        if(moduleCode.equals("3")){
            questionsController = new QuestionsController();
            filterProposalActiveQuestions();
            //questionsController.setFormData(cvQuestionsData);
            questionsController.setFormData(cvProposalQuestions);
            scrPnQuestions = new JScrollPane(questionsController.getControlledUI());
            conditionEditorForm.tbdPnConditions.addTab(QUESTIONS_TAB, scrPnQuestions);
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //To show the questionnaire tab
        
        if(ruleType!=null && !ruleType.equalsIgnoreCase("Q")){
            questionnaireController = new QuestionnaireController();
            questionnaireController.setFormData(cvQuestionnaireData);
            scrPnQuestionnaires = new JScrollPane(questionnaireController.getControlledUI());
            conditionEditorForm.tbdPnConditions.addTab(QUESTIONNAIRE_TAB, scrPnQuestionnaires);
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        functionsController = new FunctionsController();
        functionsController.setFormData(cvFunctionsData);
        scrPnFunctions = new JScrollPane(functionsController.getControlledUI());
        conditionEditorForm.tbdPnConditions.addTab(FUNCTIONS_TAB, scrPnFunctions);
    }//End of initTabComponents
    
    //to validate the form
    public boolean validate() throws CoeusUIException{
        // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
        setValidatingForm(true);
        conditionTableEditor.stopCellEditing();
        if(!isValidatingForm()){
            return false;
        }
        // COEUSQA-2458-End
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //if(!isTypeValidate()){
        if(!ruleType.equals("V") && !ruleType.equals("Q")){
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            //Modified for case 2785 - Routing enhancement - start
//            if(businessRuleConditionsBean.getMapDescription() == null
//                    || businessRuleConditionsBean.getMapDescription().trim().equals(EMPTY_STRING)){
            if(mapHeaderBean == null){
                //Modified for case 2785 - Routing enhancement - end
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SELECT_MAP));
                return false;
            }//End of inner if
        }//End of if(!isTypeValidate()........
        
        String strDesc = CoeusGuiConstants.EMPTY_STRING;
        if(isTypeValidate()){
            strDesc = validationForm.txtDescription.getText();
        }else{
            strDesc = conditionEditorForm.txtDescription.getText();
        }
        
        if(strDesc == null || strDesc.trim().equals(CoeusGuiConstants.EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_DESCRIPTION));
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //To set the focus to the component
            if(isTypeValidate()){
                setRequestFocusInThread(validationForm.txtDescription);
            }else{
                setRequestFocusInThread(conditionEditorForm.txtDescription);
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            return false;
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        String strUserMessage = CoeusGuiConstants.EMPTY_STRING;
        if(isTypeValidate()){
            strUserMessage = validationForm.txtArUserMessage.getText();
        }else{
            strUserMessage = conditionEditorForm.txtArUserMessage.getText();
        }
        if(ruleType.equals("V") || ruleType.equals("N")){
            if(strUserMessage == null || strUserMessage.trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_USER_MESSAGE));
                if(isTypeValidate()){
                    setRequestFocusInThread(validationForm.txtArUserMessage);
                }else{
                    setRequestFocusInThread(conditionEditorForm.txtArUserMessage);
                }
                return false;
            }
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        //if(getNoOfCondtionsPresent() == 0 && actionMode != INSERT_ROW ){
        if(conditionEditorForm.tblRules.getRowCount()==0){
            if(cvExpressionData == null || cvExpressionData.size() == 0){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(DEFINE_COND));
                return false;
            }
        }else if(isElsePresent()){
            //If the "else" condition is modified
            //validation should not be fired
            boolean codtionIsElse = false;
            if(actionMode == TypeConstants.MODIFY_MODE &&
                    (businessRuleConditionsBean.getConditionExp() == null ||
                    businessRuleConditionsBean.getConditionExp().equals(CoeusGuiConstants.EMPTY_STRING)) ){
                codtionIsElse = true;
            }
            if(!codtionIsElse &&
                    (cvExpressionData == null || cvExpressionData.size() == 0)){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(DEFINE_COND));
                return false;
            }
            
        }
        
        //Validation for the expresions
        if(cvExpressionData != null && cvExpressionData.size() > 0){
            // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
            int openParenthesisCount = 0;
            int closeParenthesisCount = 0;
            for(int index = 0 ; index < cvExpressionData.size() ; index ++){
                BusinessRuleExpBean expBean = (
                        BusinessRuleExpBean)cvExpressionData.get(index);
                
                if(expBean.getLvalue() == null || expBean.getOperator() == null ||
                        expBean.getRvalue() == null || expBean.getExpressionType() == null ||
                        expBean.getLvalue().trim().equals(CoeusGuiConstants.EMPTY_STRING) ||
                        expBean.getOperator().trim().equals(CoeusGuiConstants.EMPTY_STRING) ||
                        expBean.getRvalue().trim().equals(CoeusGuiConstants.EMPTY_STRING) ||
                        expBean.getExpressionType().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(COMPLETE_EXP) + (index+1));
                    
                    conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                    return false;
                }
                
                
                /*if(expBean.getOperator() == null ||
                   expBean.getOperator().trim().equals(EMPTY_STRING)){
                       CoeusOptionPane.showInfoDialog(
                            "Please enter the operator for expression " + (index+1));
                       conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                       conditionEditorForm.tblRules.setColumnSelectionInterval(0,0);
                       conditionEditorForm.tblRules.editCellAt(index,OPERATOR);
                       setRequestFocusInThread(cmbOperatorType);
                       return false;
                }*/
                
                
                
                //fn_verify_qanda is used fot the validation for the no of ans for a ques
                //in pb code. This is not required , since we are populating the
                //entries in the combo based on the no of ans for the ques.
                
                
                //fn_get_rule_datatype is used to get the data type of the Lvalue and Rvalue in pb
                //Since the Rvalue field is disabled in java code no validation required for Rvalue
                
                //fn_function_exists is used for checkin the functio name in pb.
                //Since the Rvalue field is disabled in java code no validation required for Rvalue
                
                if(expBean.getExpressionType().equalsIgnoreCase(TYPE_COLUMN)){
                    String dataType = getDataType(expBean.getLvalue().trim());
                    if(dataType != null && !dataType.trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                        if(dataType.trim().equalsIgnoreCase("NUMBER")){
                            String  strRvalue = expBean.getRvalue();
                            
                            try{
                                Double.parseDouble(strRvalue);
                            }catch (NumberFormatException nEx){
                                CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey(DATATYPE_MISMATCH) +" "+ (index+1));
                                conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                                return false;
                            }
                            //strRvalue.is
                        }else if(dataType.trim().equalsIgnoreCase("DATE")){
                            String strDate = expBean.getRvalue().trim();
                            if(! strDate.equals(CoeusGuiConstants.EMPTY_STRING)) {
                                String date = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                                if(date == null) {
                                    date = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                                    if( date == null || date.equals(strDate)) {
                                        CoeusOptionPane.showInfoDialog(
                                                coeusMessageResources.parseMessageKey(DATATYPE_MISMATCH) +" "+ (index+1));
                                        conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                                        return false;
                                    }
                                }
                            }
                        }//End of Else if
                    }
                }//End of if(expBean.getExpressionType()..........
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                else if(expBean.getExpressionType().equalsIgnoreCase(TYPE_QUESTIONNAIRE_QN)){
                    QuestionsMaintainanceBean questionMaintenanceBean =
                            (QuestionsMaintainanceBean)hmQuestionnaireQns.get(expBean.getLvalue());
                    if(questionMaintenanceBean.getAnswerDataType().equals("String")){
                        
                    }else if(questionMaintenanceBean.getAnswerDataType().equals("Number")){
                        String  strRvalue = expBean.getRvalue();
                        
                        try{
                            Double.parseDouble(strRvalue);
                        }catch (NumberFormatException nEx){
                            CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey(DATATYPE_MISMATCH) +" "+ (index+1));
                            conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                            return false;
                        }
                    }else if(questionMaintenanceBean.getAnswerDataType().equals("Date")){
                        String strDate = expBean.getRvalue().trim();
                        if(! strDate.equals(CoeusGuiConstants.EMPTY_STRING)) {
                            String date = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                            if(date == null) {
                                date = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                                if( date == null || date.equals(strDate)) {
                                    CoeusOptionPane.showInfoDialog(
                                            coeusMessageResources.parseMessageKey(DATATYPE_MISMATCH) +" "+ (index+1));
                                    conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                                    return false;
                                }
                            }
                        }
                    }
                }
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
                if(expBean.getExpressionPrefix()!= null && !CoeusGuiConstants.EMPTY_STRING.equals(expBean.getExpressionPrefix())){
                    String openString = expBean.getExpressionPrefix();
                    for(int i = 0 ; i< openString.length() ; i++){
                        //no characters otherthan '('
                        if(openString.charAt(i) != '('){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_PREFIX));
                            conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                            return false;
                        }
                    }
                    openParenthesisCount += openString.length();
                }
                
                if(expBean.getExpressionSuffix()!= null && !CoeusGuiConstants.EMPTY_STRING.equals(expBean.getExpressionSuffix())){
                    String closeString = expBean.getExpressionSuffix();
                    for(int i = 0 ; i< closeString.length() ; i++){
                        //no characters otherthan ')'
                        if(closeString.charAt(i) != ')'){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_SUFFIX));
                            conditionEditorForm.tblRules.setRowSelectionInterval(index,index);
                            return false;
                        }
                    }
                    closeParenthesisCount += closeString.length();
                    
                }
            }//End of for
            if(closeParenthesisCount != openParenthesisCount){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_PARENTHESIS));
                return false;
            }
            //COEUSQA-2458-end
        }//End of if(cvExpressionData.........
        
        return true;
    }
    
    private String getDataType(String strVarName){
        
        String dataType = CoeusGuiConstants.EMPTY_STRING;
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        
        
        request.setDataObject(strVarName);
        request.setFunctionType(GET_DATATYPE_FOR_VALIDATION);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + RULE_SERVLET;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                dataType = (String)response.getDataObject();
            }else{
                Exception ex = response.getException();
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
        }
        return dataType;
    }
    
    //to save the form data
    public void saveFormData() throws CoeusException {
        conditionTableEditor.stopCellEditing();
        //Added for case 2785 - Routing enhancements - start
        checkDataModified();
        //Added for case 2785 - Routing enhancements - end
        if(isTypeValidate()){
            ComboBoxBean cmbBean = (ComboBoxBean)validationForm.cmbCondition.getSelectedItem();
            int action = Integer.parseInt(cmbBean.getCode());
            businessRuleConditionsBean.setAction(action);
            
            businessRuleConditionsBean.setRuleDescription(
                    validationForm.txtDescription.getText().trim());
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            businessRuleConditionsBean.setUserMessage(
                    validationForm.txtArUserMessage.getText().trim());
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }else{
            //System.out.println(conditionEditorForm.txtDescription.getText());
            //Added for case 2785 - Routing enhancements - start
            if(mapHeaderBean!=null){
                businessRuleConditionsBean.setAction(mapHeaderBean.getMapId());
                businessRuleConditionsBean.setMapDescription(mapHeaderBean.getMapDescription());
            }
            //Added for case 2785 - Routing enhancements - end
            businessRuleConditionsBean.setRuleDescription(
                    conditionEditorForm.txtDescription.getText().trim());
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            businessRuleConditionsBean.setUserMessage(
                    conditionEditorForm.txtArUserMessage.getText().trim());
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }
        
        if(cvExpressionData == null || cvExpressionData .size() == 0){
            return ;
        }
        
        for(int index = 0 ; index < cvExpressionData.size() ; index++){
            BusinessRuleExpBean ruleExpBean =
                    (BusinessRuleExpBean)cvExpressionData.get(index);
            
            //ruleExpBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
            ruleExpBean.setRuleId(businessRuleConditionsBean.getRuleId());
            
            //Commented for case 2418 - Business Rule Evaluation Bug - start
            //Expression numbers are set while adding, inserting and deleting
//            int expNo = index + 1;
//            ruleExpBean.setExpressionNumber(expNo);
            //Commented for case 2418 - Business Rule Evaluation Bug - end
            
            if(index >= 1) {
                BusinessRuleExpBean businessRuleExpBean =
                        (BusinessRuleExpBean)cvExpressionData.get(index-1);
                if(businessRuleExpBean.getLogicalOperator() == null ||
                        businessRuleExpBean.getLogicalOperator().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                    businessRuleExpBean.setLogicalOperator("AND");
                    if(businessRuleExpBean.getAcType() == null){
                        businessRuleExpBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }//End of inner if
            }
        }//End of for
        
        if(cvExpressionData.size() == 1){
            BusinessRuleExpBean businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(0);
            businessRuleExpBean.setLogicalOperator(null);
        }else{
            BusinessRuleExpBean businessRuleExpBean =
                    (BusinessRuleExpBean)cvExpressionData.get((cvExpressionData.size() - 1));
            businessRuleExpBean.setLogicalOperator(null);
        }
        
    }
    
    // the actions to be performed on the various events
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            if(source.equals(conditionEditorForm.btnOk)){
                dlgConditionEditor.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                if(validate()){
                    saveFormData();
                    setOkClicked(true);
                    dlgConditionEditor.dispose();
                }
            }else if(source.equals(conditionEditorForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(conditionEditorForm.btnMap)){
                showMaps();
            }else if(source.equals(conditionEditorForm.btnAdd)){
                performAddAction();
            }else if(source.equals(conditionEditorForm.btnInsert)){
                performInsertAction();
            }else if(source.equals(conditionEditorForm.btnDelete)){
                performDeleteAction();
            }
        }catch (CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }catch (CoeusUIException cUiEx){
            cUiEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cUiEx.getMessage());
        }finally{
            dlgConditionEditor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    //the action performed on the click of the Add Button
    private void performAddAction(){
        conditionTableEditor.stopCellEditing();
        
        //Modified for case 2418 - Business Rule Evaluation Bug - start
        cvExpressionData.add(createNewBean(conditionEditorForm.tblRules.getRowCount()+1));
        //Modified for case 2418 - Business Rule Evaluation Bug - end
        
        conditionEditorTableModel.fireTableRowsInserted(conditionEditorTableModel.getRowCount(),
                conditionEditorTableModel.getRowCount());
        
        setModified(true);
        
        conditionEditorForm.tblRules.setRowSelectionInterval(0,0);
        int lastRow = conditionEditorForm.tblRules.getRowCount()-1;
        if(lastRow >= 0){
            conditionEditorForm.tblRules.setRowSelectionInterval(lastRow,lastRow);
            conditionEditorForm.tblRules.setColumnSelectionInterval(0,0);
            conditionEditorForm.tblRules.scrollRectToVisible(
                    conditionEditorForm.tblRules.getCellRect(lastRow, L_VALUE, true));
        }
    }
    
    private void performCancelAction(){
        conditionTableEditor.stopCellEditing();
        //Added for case 2785 - Routing enhancements - start
        checkDataModified();
        //Added for case 2785 - Routing enhancements - end
        if(isModified()){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_CHANGES),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    try{
                        if(validate()){
                            dlgConditionEditor.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            saveFormData();
                            setOkClicked(true);
                            dlgConditionEditor.dispose();
                        }
                    }catch (CoeusUIException coeusUIException){
                        coeusUIException.printStackTrace();
                        CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
                    }catch (CoeusException cEx){
                        cEx.printStackTrace();
                        CoeusOptionPane.showErrorDialog(cEx.getMessage());
                    }finally{
                        dlgConditionEditor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    break;
                    
                case(JOptionPane.NO_OPTION ):
                    dlgConditionEditor.dispose();
                    break;
                    
                default:
                    break;
            }
        }else{
            dlgConditionEditor.dispose();
        }
    }
    
    private void performInsertAction(){
        conditionTableEditor.stopCellEditing();
        
        int selRow = conditionEditorForm.tblRules.getSelectedRow();
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Set the selRow as 0 if there is no rows in the table
//        if(selRow != -1){
//            selRow = 0;
//        }
        if(selRow == -1){
            selRow = 0;
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        if(cvExpressionData == null || cvExpressionData.size() == 0){
            //BusinessRuleExpBean businessRuleExpBean = new BusinessRuleExpBean();
            //Modified for case 2418 - Business Rule Evaluation Bug - start
            cvExpressionData.insertElementAt(createNewBean(0) , 0);
            //Modified for case 2418 - Business Rule Evaluation Bug - end
            conditionEditorTableModel.fireTableDataChanged();
            conditionEditorForm.tblRules.setRowSelectionInterval(0,0);
        }else{
            //BusinessRuleExpBean businessRuleExpBean = new BusinessRuleExpBean();
            //Modified for case 2418 - Business Rule Evaluation Bug - start
            setNewExpressionNos(TypeConstants.INSERT_RECORD, selRow);
            cvExpressionData.insertElementAt(createNewBean(selRow+1) , selRow);
            //Modified for case 2418 - Business Rule Evaluation Bug - end
            conditionEditorTableModel.fireTableDataChanged();
            conditionEditorForm.tblRules.setRowSelectionInterval(selRow,selRow);
        }
        
        setModified(true);
    }//End of performInsertAction
    
    
    private void performDeleteAction(){
        conditionTableEditor.stopCellEditing();
        
        if(conditionEditorForm.tblRules.getRowCount() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_ROWS));
            return ;
        }
        
        int selRow = conditionEditorForm.tblRules.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SEL_ROW));
            return ;
        }
        
        
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(DELETE_CONFIRMATION),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        
        if(selectedOption == CoeusOptionPane.SELECTION_YES) {
            BusinessRuleExpBean ruleExpBean =
                    (BusinessRuleExpBean)cvExpressionData.get(selRow);
            if(ruleExpBean.getAcType() == null
                    || !ruleExpBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                
                ruleExpBean.setAcType(TypeConstants.DELETE_RECORD);
                cvDeletedData.add(ruleExpBean);
            }
            
            //Modified for case 2418 - Business Rule Evaluation Bug - start
            //Remove the Function Arguments
//            int expNo = selRow +1;
//            //NotEquals neCondNo = new NotEquals("conditionNumber" , new Integer(businessRuleConditionsBean.getConditionNumber()));
//            NotEquals neExpNo = new NotEquals("expressionNumber" ,  new Integer(expNo));
//            //And neCondNoAndneExpNo = new And(neCondNo , neExpNo);
//            cvRuleFuncArgs = cvRuleFuncArgs.filter(neExpNo);
//            
//            resetExpNo(expNo);
            setNewExpressionNos(TypeConstants.DELETE_RECORD, selRow);
            //Modified for case 2418 - Business Rule Evaluation Bug - end
            cvExpressionData.remove(selRow);
            conditionEditorTableModel.fireTableDataChanged();
            
            if(cvExpressionData.size() > 0){
                conditionEditorForm.tblRules.setRowSelectionInterval(0,0);
            }
            setModified(true);
        }//End of outer if
        
    }//End of performDeleteAction
    
//    private void resetExpNo(int expNo){
//        if(cvRuleFuncArgs == null){
//            return ;
//        }
//        
//        for(int index = 0 ; index < cvRuleFuncArgs.size() ; index++){
//            BusinessRuleFuncArgsBean ruleFuncArgsBean =
//                    (BusinessRuleFuncArgsBean)cvRuleFuncArgs.get(index);
//            int beanExpNo = ruleFuncArgsBean.getExpressionNumber();
//            if(beanExpNo > expNo){
//                beanExpNo = beanExpNo -1;
//                ruleFuncArgsBean.setExpressionNumber(beanExpNo);
//            }
//        }
//    }//End of resetExpNo
    
    private void showMaps() throws CoeusException{
        conditionTableEditor.stopCellEditing();
        
        dlgConditionEditor.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        BusinessRulesMapsController businessRulesMapsController =
                new BusinessRulesMapsController(ruleBaseBean.getUnitNumber());
        
        dlgConditionEditor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
        businessRulesMapsController.display();
        
        if(businessRulesMapsController.isOkClicked()){
            MapHeaderBean mapHeaderBean =
                    (MapHeaderBean)businessRulesMapsController.getFormData();
            refresh(mapHeaderBean);
            setModified(true);
        }
    }
    
    
    private void refresh(MapHeaderBean mapHeaderBean){
        //Modified for case 2785 - Routing enhancements - start
        //businessRuleConditionsBean.setMapDescription(mapHeaderBean.getMapDescription());
        conditionEditorForm.lblConditionValue.setText(mapHeaderBean.getMapDescription());
        this.mapHeaderBean = mapHeaderBean;
        //businessRuleConditionsBean.setAction(mapHeaderBean.getMapId());
        //Modified for case 2785 - Routing enhancements - end
    }
    //Modifed for case 2418 - Business Rule Evaluation Bug -start
    /**
     * Creates a new BusinessRuleExpBean object with default values set
     *
     * @param expNumber expression number for the new bean
     * @return BusinessRuleExpBean instance of BusinessRuleExpBean
     */
    private BusinessRuleExpBean createNewBean(int expNumber){
        BusinessRuleExpBean newBean = new BusinessRuleExpBean();
        newBean.setLvalue(CoeusGuiConstants.EMPTY_STRING);
        newBean.setOperator(CoeusGuiConstants.EMPTY_STRING);
        newBean.setRvalue(CoeusGuiConstants.EMPTY_STRING);
        newBean.setLogicalOperator(CoeusGuiConstants.EMPTY_STRING);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        newBean.setExpressionNumber(expNumber);
        return newBean;
    }
    //Modifed for case 2418 - Business Rule Evaluation Bug - end
    
    /*public void stateChanged(javax.swing.event.ChangeEvent e) {
        //         System.out.println("iam in state changed");
        //         if(conditionEditorForm.tbdPnConditions.getSelectedIndex() == COLUMNS){
        //
        //             conditionEditorForm.tbdPnConditions.requestFocusInWindow();
        //         }else if(conditionEditorForm.tbdPnConditions.getSelectedIndex() == QUESTIONS){
        //             conditionEditorForm.tbdPnConditions.requestFocusInWindow();
        //         }
    }*/
    
    /** Get the data from the server for the  Business Rules Columns,Questions and Functions tabs
     *Get the CoeusVector which contains Columns, Questions,Functions
     *and description data
     *@ returns the CoeusVector
     *@ throws CoeusException
     */
    private CoeusVector getDataForTabs() throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_FUNCTION_DATA );
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        Vector serverData = new Vector();
        serverData.add(moduleCode);
        serverData.add(subModuleCode);
        requester.setDataObjects(serverData);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+RULE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response != null){
            if (!response.isSuccessfulResponse() ){
                throw new CoeusException(response.getMessage());
            }else{
                cvData  = (CoeusVector)response.getDataObject();
            }
        }else{
            throw new CoeusException();
        }
        return cvData;
    }
    
    /** Check if the argumet window is present for the selected function
     *@ returns the Integer
     *@ throws CoeusException
     */
    private Integer checkForArgs(String functionName)
    throws CoeusException{
        Integer argPresent = new Integer(-1);
        
        RequesterBean requester = new RequesterBean();
        
        requester.setFunctionType(CHECK_FOR_ARGS);
        requester.setDataObject(functionName);
        
        AppletServletCommunicator comm =
                new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+RULE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response != null){
            if (!response.isSuccessfulResponse() ){
                throw new CoeusException(response.getMessage());
            }else{
                argPresent  = (Integer)response.getDataObject();
            }
        }else{
            throw new CoeusException();
        }
        return argPresent;
    }
    
    public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    
    public void dragExit(java.awt.dnd.DropTargetEvent dte) {
    }
    
    public void dragOver(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    
    //the action performed on the drop on the table row
    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        conditionTableEditor.stopCellEditing();
        if(functionType == TypeConstants.DISPLAY_MODE){
            return ;
        }
        
        try{
            
            //DropTargetContext dtc = dtde.getDropTargetContext();
            //targetDrop =new DropTarget(dtc.getComponent(),this);
            Transferable transferable = dtde.getTransferable();
            dtde.acceptDrop(dtde.getDropAction());
            Object data = transferable.getTransferData(TransferableUserRoleData.MULTIPLE_USERS_FLAVOR );
            CoeusVector cvData = (CoeusVector)data;
            
            Point location  = dtde.getLocation();
            int selRow  = conditionEditorForm.tblRules.rowAtPoint(location);
            
            if(selRow == -1){
                return ;
            }
            
            if( cvData.get(0) instanceof BusinessRuleVariableBean){
                BusinessRuleVariableBean bean = (BusinessRuleVariableBean)cvData.get(0);
                //int selRow = conditionEditorForm.tblRules.getSelectedRow();
                
                BusinessRuleExpBean expBean = (BusinessRuleExpBean)cvExpressionData.get(selRow);
                expBean.setLvalue(bean.getVariableName());
                expBean.setExpressionType(TYPE_COLUMN);
                
                conditionEditorForm.tblRules.setValueAt(bean.getVariableName(), selRow, L_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, OPERATOR);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, R_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, LOGICAL);
                //Added for case 2785 - Routing Enhancement - start
                //Set the AND as the default logical operator if the user has not selected one
               if(selRow > 0 && (conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL)== null ||
                        conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL).equals(CoeusGuiConstants.EMPTY_STRING))){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow-1, LOGICAL);
                }
                //Set the AND as the default logical operator if the row is not inserted at
                //the end of the table
                if(selRow < conditionEditorForm.tblRules.getRowCount()-1){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow, LOGICAL);
                }
                //Added for case 2785 - Routing Enhancement - end
                setModified(true);
                conditionEditorTableModel.fireTableDataChanged();
                conditionEditorForm.tblRules.setRowSelectionInterval(selRow,selRow);
                //row = selRow;
                
            } else if(cvData.get(0) instanceof YNQBean){
                YNQBean bean = (YNQBean)cvData.get(0);
                //int selRow = conditionEditorForm.tblRules.getSelectedRow();
                BusinessRuleExpBean expBean = (BusinessRuleExpBean)cvExpressionData.get(selRow);
                expBean.setLvalue(bean.getQuestionId());
                expBean.setExpressionType(TYPE_QUESTION);
                expBean.setRvalue("Y");
                expBean.setNoOfAnswers(bean.getNoOfAnswers());
                //Added for case 2785 - Routing Enhancement - start
                //Set the AND as the default logical operator if the user has not selected one
                if(selRow > 0 && (conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL)== null ||
                        conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL).equals(CoeusGuiConstants.EMPTY_STRING))){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow-1, LOGICAL);
                }
                //Added for case 2785 - Routing Enhancement - end
                conditionEditorForm.tblRules.setValueAt(bean.getQuestionId(), selRow, L_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, OPERATOR);
                conditionEditorForm.tblRules.setValueAt("Y", selRow, R_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, LOGICAL);
                //Added for case 2785 - Routing Enhancement - start
               //Set the AND as the default logical operator if the row is not inserted at
                //the end of the table
                if(selRow < conditionEditorForm.tblRules.getRowCount()-1){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow, LOGICAL);
                }
                //Added for case 2785 - Routing Enhancement - end
                //row = selRow;
                setModified(true);
                conditionEditorTableModel.fireTableDataChanged();
                conditionEditorForm.tblRules.setRowSelectionInterval(selRow,selRow);
                
            }else if(cvData.get(0) instanceof BusinessRuleFunctionBean){
                // CoeusVector cvData = (CoeusVector)data;
                BusinessRuleFunctionBean bean = (BusinessRuleFunctionBean)cvData.get(0);
                //int selRow = conditionEditorForm.tblRules.getSelectedRow();
                BusinessRuleExpBean expBean = (BusinessRuleExpBean)cvExpressionData.get(selRow);
                expBean.setLvalue(bean.getFunctionName());
                expBean.setExpressionType(TYPE_FUNCTION);
                expBean.setRvalue("1");
                //row = selRow;
                //Added for case 2785 - Routing Enhancement - start
                //Set the AND as the default logical operator if the user has not selected one
                if(selRow > 0 && (conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL)== null ||
                        conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL).equals(CoeusGuiConstants.EMPTY_STRING))){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow-1, LOGICAL);
                }
                //Added for case 2785 - Routing Enhancement - end
                conditionEditorForm.tblRules.setValueAt(bean.getFunctionName(),selRow,L_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, OPERATOR);
                conditionEditorForm.tblRules.setValueAt("1", selRow, R_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, LOGICAL);
                //Added for case 2785 - Routing Enhancement - start
                //Set the AND as the default logical operator if the row is not inserted at
                //the end of the table
                if(selRow < conditionEditorForm.tblRules.getRowCount()-1){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow, LOGICAL);
                }
                //Added for case 2785 - Routing Enhancement - end
                setModified(true);
                conditionEditorTableModel.fireTableDataChanged();
                conditionEditorForm.tblRules.setRowSelectionInterval(selRow,selRow);
                int argPresent = (checkForArgs(bean.getFunctionName())).intValue();
                if(argPresent == 1){
                    showArgumentWindow(selRow , TypeConstants.NEW_MODE);
                }
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            else if(cvData.get(0) instanceof QuestionnaireQuestionsBean){
                QuestionnaireQuestionsBean questionnaireQnBean = (QuestionnaireQuestionsBean)cvData.get(0);
                BusinessRuleExpBean expBean = (BusinessRuleExpBean)cvExpressionData.get(selRow);
                expBean.setLvalue(questionnaireQnBean.getQuestionId().toString());
                expBean.setExpressionType(TYPE_QUESTIONNAIRE_QN);
                expBean.setRvalue("");
                //Added for case 2785 - Routing Enhancement - start
                //Set the AND as the default logical operator if the user has not selected one
                 if(selRow > 0 && (conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL)== null ||
                        conditionEditorForm.tblRules.getValueAt(selRow-1, LOGICAL).equals(CoeusGuiConstants.EMPTY_STRING))){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow-1, LOGICAL);
                }
                //Added for case 2785 - Routing Enhancement - end
                conditionEditorForm.tblRules.setValueAt(questionnaireQnBean.getQuestionId().toString(),selRow,L_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, OPERATOR);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, R_VALUE);
                conditionEditorForm.tblRules.setValueAt(CoeusGuiConstants.EMPTY_STRING, selRow, LOGICAL);
                //Added for case 2785 - Routing Enhancement - start
                //Set the AND as the default logical operator if the row is not inserted at
                //the end of the table
                if(selRow < conditionEditorForm.tblRules.getRowCount()-1){
                    conditionEditorForm.tblRules.setValueAt("AND", selRow, LOGICAL);
                }
                //Added for case 2785 - Routing Enhancement - end
                conditionEditorTableModel.fireTableDataChanged();
                conditionEditorForm.tblRules.setRowSelectionInterval(selRow,selRow);
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            //conditionEditorForm.tblRules.setRowSelectionInterval(row ,row);
            //conditionEditorForm.tblRules.setColumnSelectionInterval(0,0);
            
        }catch(UnsupportedFlavorException ue){
            ue.printStackTrace();
            dtde.getDropTargetContext().dropComplete(false);
            CoeusOptionPane.showErrorDialog(ue.getMessage());
        }catch (IOException ioEx){
            ioEx.printStackTrace();
            dtde.getDropTargetContext().dropComplete(false);
            CoeusOptionPane.showErrorDialog(ioEx.getMessage());
        }catch (CoeusException ce){
            ce.printStackTrace();
            dtde.getDropTargetContext().dropComplete(false);
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
    }
    
    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    
    //the table model for the users table
    class ConditionEditorTableModel extends AbstractTableModel{
        //Modified for case 2785 - Routing enhancements - start
        //Removed the table header and added one column for icon
        // Restructured columns with COEUSQA-2458-Make 'and' and 'or' work in business rules
        String colNames[] = {"", "", "", "", "","",""};
        Class[] colTypes = new Class [] {String.class , String.class,String.class , String.class, String.class, String.class,String.class};
        //Modified for case 2785 - Routing enhancements - end
        
        /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            if(functionType == TypeConstants.DISPLAY_MODE){
                return false;
            }else {
                //Modified for case 2785 - Routing enhancements - start
                //Replaced the value with variable
                //if(col == 0){
                if(col == L_VALUE){
                    //Modified for case 2785 - Routing enhancements - end
                    return false;
                }else{
                    return true;
                }
            }
        }
        
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /* returns the number of rows*/
        public int getRowCount() {
            return cvExpressionData.size();
        }
        
        public void setData(CoeusVector cvData){
            cvExpressionData = cvData;
        }
        
        /* gets the value at a particular cell*/
        public Object getValueAt(int row, int column) {
            BusinessRuleExpBean businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(row);
            switch(column){
                //Modified for case 2785 - Routing enhancement - start
                case L_VALUE:
                    if(businessRuleExpBean.getExpressionType() != null){
                        if(businessRuleExpBean.getExpressionType().equals(TYPE_COLUMN)){
                            return businessRuleExpBean.getLvalue();
                        }else if(businessRuleExpBean.getExpressionType().equals(TYPE_FUNCTION)){
                            return businessRuleExpBean.getLvalue();
                        }else if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTION)){
                            return "YNQ Id "+businessRuleExpBean.getLvalue();
                        }else if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                            return "Question Id "+businessRuleExpBean.getLvalue();
                        }
                    }
                    return  businessRuleExpBean.getLvalue();
                case OPERATOR:
                    if(businessRuleExpBean!=null){
                        if(businessRuleExpBean.getOperator().equals("=")){
                            return EQUAL_TO;
                        }else if(businessRuleExpBean.getOperator().equals("<")){
                            return LESS_THAN;
                        }else if(businessRuleExpBean.getOperator().equals(">")){
                            return GREATER_THAN;
                        }else if(businessRuleExpBean.getOperator().equals("<=")){
                            return LESS_THAN_EQUAL_TO;
                        }else if(businessRuleExpBean.getOperator().equals(">=")){
                            return GREATER_THAN_EQUAL_TO;
                        }else if(businessRuleExpBean.getOperator().equals("<>")){
                            return NOT_EQUAL_TO;
                        }else if(businessRuleExpBean.getOperator().equalsIgnoreCase("CONTAINS")){
                            return CONTAINS;
                        }else if(businessRuleExpBean.getOperator().equalsIgnoreCase("BEGIN_WITH")){
                            return BEGINS_WITH;
                        }else if(businessRuleExpBean.getOperator().equalsIgnoreCase("ENDS_WITH")){
                            return ENDS_WITH;
                        }
                    }
                    return CoeusGuiConstants.EMPTY_STRING;
                    //Modified for case 2785 - Routing enhancement - end
                case R_VALUE:
                    if(businessRuleExpBean.getExpressionType() != null){
                        if(businessRuleExpBean.getExpressionType().equals(TYPE_COLUMN)){
                            return businessRuleExpBean.getRvalue();
                        }else if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTION)){
                            if(businessRuleExpBean.getRvalue().equals("Y")){
                                return "YES";
                            }else if(businessRuleExpBean.getRvalue().equals("N")){
                                return "NO";
                            }else if(businessRuleExpBean.getRvalue().equals("X")){
                                return "N/A";
                            }
                        }else if(businessRuleExpBean.getExpressionType().equals(TYPE_FUNCTION)){
                            if(businessRuleExpBean.getRvalue().equals("1")){
                                return "True";
                            }else if(businessRuleExpBean.getRvalue().equals("-1")){
                                return "False";
                            }
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                        else if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                            if(businessRuleExpBean.getRvalue().equals("Y")){
                                return "YES";
                            }else if(businessRuleExpBean.getRvalue().equals("N")){
                                return "NO";
                            }else if(businessRuleExpBean.getRvalue().equals("X")){
                                return "N/A";
                            }else{
                                return businessRuleExpBean.getRvalue();
                            }
                        }//End of else if
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                    }else{
                        return CoeusGuiConstants.EMPTY_STRING;
                    }
                case LOGICAL:
                    return businessRuleExpBean.getLogicalOperator();
                // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
                case EXPRESSION_PREFIX:
                    return businessRuleExpBean.getExpressionPrefix();
                case EXPRESSION_SUFFIX:
                    return businessRuleExpBean.getExpressionSuffix();
                // COEUSQA-2458-End
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }//End getValueAt
        
        /* Sets the value in the cell*/
        public void setValueAt(Object value, int row, int col){
            if(cvExpressionData == null){
                return;
            }
            
            BusinessRuleExpBean businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(row);
            
            switch(col) {
                case L_VALUE:
                    if (!value.toString().trim().equals(businessRuleExpBean.getLvalue().trim())) {
                        businessRuleExpBean.setLvalue(value.toString());
                        setModified(true);
                    }
                    break;
                    
                case OPERATOR:
                    if(businessRuleExpBean.getExpressionType() != null){
                        ComboBoxBean cmbBean = null;
                        CoeusVector cvFilteredData = null;
                        Equals eqDesc = new Equals("description" , value.toString());
                        if(businessRuleExpBean.getExpressionType().equals(TYPE_COLUMN)){
                            cvFilteredData = cvOperators.filter(eqDesc);
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                        else if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                            cvFilteredData = cvQuestionaireOperators.filter(eqDesc);
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                        else{
                            cvFilteredData = cvFunctionsOperators.filter(eqDesc);
                        }
                        
                        if(cvFilteredData != null && cvFilteredData.size()>0){
                            cmbBean = (ComboBoxBean)cvFilteredData.get(0);
                            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                            //businessRuleExpBean.setOperator(cmbBean.getDescription());
                            businessRuleExpBean.setOperator(getOperatorSymbol(cmbBean.getDescription()));
                            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                        }else{
                            cmbBean = (ComboBoxBean)cmbOperatorType.getSelectedItem();
                            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                            //businessRuleExpBean.setOperator(cmbBean.getDescription());
                            businessRuleExpBean.setOperator(getOperatorSymbol(cmbBean.getDescription()));
                            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                        }
                        setModified(true);
                    }
                    break;
                    
                case R_VALUE:
                    if(businessRuleExpBean.getExpressionType() != null){
                        if(businessRuleExpBean.getExpressionType().equals(TYPE_COLUMN)){
                            if (!value.toString().trim().equals(businessRuleExpBean.getRvalue().trim())) {
                                businessRuleExpBean.setRvalue(value.toString());
                                setModified(true);
                            }
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                        else if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                            QuestionsMaintainanceBean questionMaintenanceBean =
                                    (QuestionsMaintainanceBean)hmQuestionnaireQns.get(businessRuleExpBean.getLvalue());
                            if(questionMaintenanceBean.getValidAnswers().equals("YN") ||
                                    questionMaintenanceBean.getValidAnswers().equals("YNX")){
                                
                                ComboBoxBean comboBoxBean = null;
                                CoeusVector cvFilData = null;
                                Equals eqDescr = new Equals("description" , value.toString());
                                
                                if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTION) ||
                                        businessRuleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                                    cvFilData = cvQuestionsRValues.filter(eqDescr);
                                }else{
                                    cvFilData = cvFunctionsRValues.filter(eqDescr);
                                }
                                
                                if(cvFilData != null && cvFilData.size()>0){
                                    comboBoxBean = (ComboBoxBean)cvFilData.get(0);
                                    setDataToBean(businessRuleExpBean , comboBoxBean);
                                }else{
                                    comboBoxBean = (ComboBoxBean)cmbRValue.getSelectedItem();
                                    setDataToBean(businessRuleExpBean , comboBoxBean);
                                }//End of inner else
                                
                                setModified(true);
                            }else if(questionMaintenanceBean.getValidAnswers().equals("Text")){
                                if (!value.toString().trim().equals(businessRuleExpBean.getRvalue().trim())) {
                                    businessRuleExpBean.setRvalue(value.toString());
                                    setModified(true);
                                }
                            }
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                        else{
                            ComboBoxBean comboBoxBean = null;
                            CoeusVector cvFilData = null;
                            Equals eqDescr = new Equals("description" , value.toString());
                            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                            if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTION) ||
                                    businessRuleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                                cvFilData = cvQuestionsRValues.filter(eqDescr);
                            }else{
                                cvFilData = cvFunctionsRValues.filter(eqDescr);
                            }
                            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                            if(cvFilData != null && cvFilData.size()>0){
                                comboBoxBean = (ComboBoxBean)cvFilData.get(0);
                                setDataToBean(businessRuleExpBean , comboBoxBean);
                            }else{
                                comboBoxBean = (ComboBoxBean)cmbRValue.getSelectedItem();
                                setDataToBean(businessRuleExpBean , comboBoxBean);
                            }//End of inner else
                            
                            setModified(true);
                        }//End of outer else
                    }//End of if
                    break;
                    
                case LOGICAL:
                    ComboBoxBean comboBean = null;
                    Equals eqDescription = new Equals("description" , value.toString());
                    
                    CoeusVector cvData = cvLogicalOperators.filter(eqDescription);
                    if(cvData != null && cvData.size()>0){
                        comboBean = (ComboBoxBean)cvData.get(0);
                        businessRuleExpBean.setLogicalOperator(comboBean.getDescription());
                    }else{
                        comboBean = (ComboBoxBean)cmbLogicalOperator.getSelectedItem();
                        businessRuleExpBean.setLogicalOperator(comboBean.getDescription());
                    }
                    setModified(true);
                    break;
                  // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
                  case EXPRESSION_PREFIX:
                      boolean inValid = false;
                      String strValue = value.toString();
                      for(int i = 0 ; i< strValue.length() ; i++){
                          //no characters otherthan '('
                          if(strValue.charAt(i) != '('){
                              CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_PREFIX));
                              inValid = true;
                              setValidatingForm(false);
                              break;
                          }
                      }
                      if(!inValid){
                          businessRuleExpBean.setExpressionPrefix(strValue);
                      }
                       setModified(true);
                      break;
                case EXPRESSION_SUFFIX:
                    inValid = false;
                    strValue = value.toString();
                    for(int i = 0 ; i< strValue.length() ; i++){
                        //no characters otherthan ')'
                        if(strValue.charAt(i) != ')'){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_SUFFIX));
                            inValid = true;
                            setValidatingForm(false);
                            break;
                        }
                    }
                    if(!inValid){
                        businessRuleExpBean.setExpressionSuffix(strValue);
                    }
                     setModified(true);
                    break;
                // COEUSQA-2458-End
            }//End of switch
            
            if(modified && businessRuleExpBean.getAcType()== null){
                businessRuleExpBean.setAcType(TypeConstants.UPDATE_RECORD);
            }//End of if
            
        }//End of setValueAt
        
        private void setDataToBean(BusinessRuleExpBean ruleExpBean , ComboBoxBean comboBoxBean){
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            if(ruleExpBean.getExpressionType().equals(TYPE_QUESTION) ||
                    ruleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                if(comboBoxBean.getDescription().equalsIgnoreCase("YES")){
                    ruleExpBean.setRvalue("Y");
                }else if(comboBoxBean.getDescription().equalsIgnoreCase("NO")){
                    ruleExpBean.setRvalue("N");
                }else if(comboBoxBean.getDescription().equalsIgnoreCase("N/A")){
                    ruleExpBean.setRvalue("X");
                }
            }else if(ruleExpBean.getExpressionType().equals(TYPE_FUNCTION)){
                if(comboBoxBean.getDescription().equalsIgnoreCase("True")){
                    ruleExpBean.setRvalue("1");
                }else if(comboBoxBean.getDescription().equalsIgnoreCase("False")){
                    ruleExpBean.setRvalue("-1");
                }
            }
        }//End of setDataToBean
    }//End of ConditionEditorTableModel
    
    /* Table Cell Editor for the AwardCostSharingTable*/
    class ConditionTableEditor extends AbstractCellEditor implements TableCellEditor,
            ActionListener{
        private CoeusTextField txtComponent,txtOpenParanthesis, txtCloseParanthesis;
        private int column;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        private SearchTableCellPanel searchTableCellPanel;
        private JButton btnDetails;
        private JLabel lblEmpty;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        //private boolean populated = false;
        
        /* Creates a CostSharing Editor*/
        public ConditionTableEditor() {
            cmbOperatorType = new CoeusComboBox();
            cmbRValue = new CoeusComboBox();
            cmbLogicalOperator = new CoeusComboBox();
            txtComponent = new CoeusTextField();
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            searchTableCellPanel = new SearchTableCellPanel();
            btnDetails = new JButton();
            //btnDetails.setSize(20,20);
            btnDetails.setIcon(iIcnDetails);
            btnDetails.addActionListener(this);
            lblEmpty = new JLabel();
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
            txtOpenParanthesis = new CoeusTextField();
            txtOpenParanthesis.setDocument(new LimitedPlainDocument(10));
            txtOpenParanthesis.setHorizontalAlignment(JTextField.CENTER);
            txtCloseParanthesis = new CoeusTextField();
            txtCloseParanthesis.setDocument(new LimitedPlainDocument(10));
            txtCloseParanthesis.setHorizontalAlignment(JTextField.CENTER);
            // COEUSQA-2458-End
        }
        
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        public void actionPerformed(ActionEvent e){
            if(e.getSource().equals(btnDetails)){
                showQuestionDetails();
                conditionEditorForm.tblRules.getCellEditor().stopCellEditing();
            }
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        /* Returns the CellEditor value*/
        public Object getCellEditorValue() {
            switch(column){
                case L_VALUE:
                    return txtComponent.getText();
                case OPERATOR:
                    return cmbOperatorType.getSelectedItem();
                case R_VALUE:
                    int selRow = conditionEditorForm.tblRules.getSelectedRow();
                    if(selRow != -1){
                        BusinessRuleExpBean expBean =
                                (BusinessRuleExpBean)cvExpressionData.get(selRow);
                        if(expBean.getExpressionType() != null
                                && expBean.getExpressionType().equals(TYPE_COLUMN)){
                            return txtComponent.getText();
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                        else if(expBean.getExpressionType() != null
                                && expBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                            QuestionsMaintainanceBean questionsMaintenance =
                                    (QuestionsMaintainanceBean)hmQuestionnaireQns.get(expBean.getLvalue());
                            if(questionsMaintenance.getValidAnswers().equals("YN") ||
                                    questionsMaintenance.getValidAnswers().equals("YNX")){
                                return cmbRValue.getSelectedItem();
                            }else if(questionsMaintenance.getValidAnswers().equals("Text")) {
                                return txtComponent.getText();
                            }else{
                                return txtComponent.getText();
                            }
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                        else{
                            return cmbRValue.getSelectedItem();
                        }
                    }else{
                        return txtComponent.getText();
                    }
                case LOGICAL:
                    return cmbLogicalOperator.getSelectedItem();
                    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                    //Column added for displaying icon
                case DETAILS_ICON_COLUMN:
                    int row = conditionEditorForm.tblRules.getSelectedRow();
                    if(row != -1){
                        BusinessRuleExpBean expBean =
                                (BusinessRuleExpBean)cvExpressionData.get(row);
                        if(expBean.getExpressionType()!=null &&
                                (expBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN) ||
                                expBean.getExpressionType().equals(TYPE_QUESTION))){
                            return btnDetails;
                        }else{
                            lblEmpty.setBackground(java.awt.Color.YELLOW);
                            lblEmpty.setForeground(java.awt.Color.BLACK);
                            lblEmpty.setText("");
                            return lblEmpty;
                        }
                    }
                    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
                case EXPRESSION_PREFIX:
                    return txtOpenParanthesis.getText();
                case EXPRESSION_SUFFIX:
                    return txtCloseParanthesis.getText();
                // COEUSQA-2458-end
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        
        /* returns the cellEditor component*/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            BusinessRuleExpBean expBean = (BusinessRuleExpBean)cvExpressionData.get(row);
            
            switch(column){
                case L_VALUE:
                    if(value== null){
                        txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                    
                case OPERATOR:
                    if(expBean.getExpressionType() != null &&
                            expBean.getExpressionType() != CoeusGuiConstants.EMPTY_STRING){
                        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                        //populateOperatorCombo(expBean.getExpressionType());
                        populateOperatorCombo(expBean.getExpressionType());
                        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                        return cmbOperatorType;
                    }
                    
                case R_VALUE:
                    if(expBean.getExpressionType() != null
                            && expBean.getExpressionType().equals(TYPE_COLUMN)){
                        txtComponent.setText(value.toString());
                        return txtComponent;
                    }
                    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                    else if(expBean.getExpressionType() != null
                            && expBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                        QuestionsMaintainanceBean questionsMaintenanceBean =
                                (QuestionsMaintainanceBean)hmQuestionnaireQns.get(expBean.getLvalue());
                        if(questionsMaintenanceBean!=null){
                            if(questionsMaintenanceBean.getValidAnswers().equals("Text")){
                                txtComponent.setText(value.toString());
                                return txtComponent;
                            }else if(questionsMaintenanceBean.getValidAnswers().equals("YN") ||
                                    questionsMaintenanceBean.getValidAnswers().equals("YNX") ){
                                populateRValueCombo(expBean);
                            }else if(questionsMaintenanceBean.getValidAnswers().equals("Search")){
                                if(expBean.getRvalue()!=null){
                                    searchTableCellPanel.txtResult.setText(expBean.getRvalue());
                                }
                                return searchTableCellPanel;
                            }
                        }
                        return cmbRValue;
                    }
                    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                    else{
                        if(expBean.getExpressionType() != null
                                && expBean.getExpressionType() != CoeusGuiConstants.EMPTY_STRING){
                            populateRValueCombo(expBean);
                        }
                        return cmbRValue;
                    }//End of else
                    
                case LOGICAL:
                    populateLogicalCombo();
                    return cmbLogicalOperator;
                    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                case DETAILS_ICON_COLUMN:
                    if(expBean.getExpressionType()!=null &&
                            (expBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN) ||
                            expBean.getExpressionType().equals(TYPE_QUESTION))){
                        return btnDetails;
                    }else{
                        lblEmpty.setBackground(java.awt.Color.YELLOW);
                        lblEmpty.setForeground(java.awt.Color.BLACK);
                        lblEmpty.setText("");
                        return lblEmpty;
                    }
                    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
                case EXPRESSION_PREFIX:
                    if(value== null){
                        txtOpenParanthesis.setText(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        txtOpenParanthesis.setText(value.toString());
                    }
                    return txtOpenParanthesis;
                case EXPRESSION_SUFFIX:
                    if(value== null){
                        txtCloseParanthesis.setText(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        txtCloseParanthesis.setText(value.toString());
                    }
                    return txtCloseParanthesis;
                // COEUSQA-2458-End
            }
            return txtComponent;
        }
        
        /* To populate the comboBox*/
        //Modified Method signature for Coeus 4.3 Routing enhancement -PT ID:2785
        //private void populateOperatorCombo(String expType) {
        private void populateOperatorCombo(String expType) {
            cmbOperatorType.removeAllItems();
            cmbLogicalOperator.removeAllItems();
            int size = cvOperators.size();
            ComboBoxBean comboBoxBean;
            if(expType.equals(TYPE_COLUMN)){
                for(int index = 0; index < size; index++) {
                    comboBoxBean = (ComboBoxBean)cvOperators.get(index);
                    cmbOperatorType.addItem(comboBoxBean);
                }
            }else if(expType.equals(TYPE_QUESTION)){
                for(int index = 0; index < cvFunctionsOperators.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvFunctionsOperators.get(index);
                    cmbOperatorType.addItem(comboBoxBean);
                }
            }else if(expType.equals(TYPE_FUNCTION)){
                for(int index = 0; index < cvFunctionsOperators.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvFunctionsOperators.get(index);
                    cmbOperatorType.addItem(comboBoxBean);
                }
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            else if(expType.equals(TYPE_QUESTIONNAIRE_QN)){
                for(int index = 0; index < cvQuestionaireOperators.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvQuestionaireOperators.get(index);
                    cmbOperatorType.addItem(comboBoxBean);
                }
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            
            
        }//End of populateOperatorCombo
        
        private void populateRValueCombo(BusinessRuleExpBean businessRuleExpBean) {
            BusinessRuleExpBean ruleExpBean = businessRuleExpBean;
            cmbRValue.removeAllItems();
            ComboBoxBean comboBoxBean;
            if(ruleExpBean.getExpressionType().equals(TYPE_QUESTION)){
                int size = 0;
                if(ruleExpBean.getNoOfAnswers() == 3){
                    size = cvQuestionsRValues.size();
                }else{
                    size = cvQuestionsRValues.size() - 1;
                }
                
                for(int index = 0; index < size; index++) {
                    comboBoxBean = (ComboBoxBean)cvQuestionsRValues.get(index);
                    cmbRValue.addItem(comboBoxBean);
                }
            }else if(ruleExpBean.getExpressionType().equals(TYPE_FUNCTION)){
                for(int index = 0; index < cvFunctionsRValues.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvFunctionsRValues.get(index);
                    cmbRValue.addItem(comboBoxBean);
                }
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            else if(ruleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                QuestionsMaintainanceBean questionsMaintenanceBean =
                        (QuestionsMaintainanceBean)hmQuestionnaireQns.get(ruleExpBean.getLvalue());
                if(questionsMaintenanceBean.getValidAnswers().equals("YN")){
                    int size = 0;
                    if(ruleExpBean.getNoOfAnswers() == 3){
                        size = cvQuestionsRValues.size();
                    }else{
                        size = cvQuestionsRValues.size() - 1;
                    }
                    
                    for(int index = 0; index < size; index++) {
                        comboBoxBean = (ComboBoxBean)cvQuestionsRValues.get(index);
                        cmbRValue.addItem(comboBoxBean);
                    }
                }else if(questionsMaintenanceBean.getValidAnswers().equals("YNX")){
                    for(int index = 0; index < cvQuestionsRValues.size(); index++) {
                        comboBoxBean = (ComboBoxBean)cvQuestionsRValues.get(index);
                        cmbRValue.addItem(comboBoxBean);
                    }
                }else if(questionsMaintenanceBean.getValidAnswers().equals("Search")){
                    
                }
                
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            
        }//End populateRValueCombo
        
        private void populateLogicalCombo() {
            cmbLogicalOperator.removeAllItems();
            ComboBoxBean comboBoxBean;
            cmbLogicalOperator.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
            for(int index = 0; index < cvLogicalOperators.size(); index++) {
                comboBoxBean = (ComboBoxBean)cvLogicalOperators.get(index);
                cmbLogicalOperator.addItem(comboBoxBean);
            }
            
        }//End populateLogicalCombo
    }//End of ConditionTableEditor
    
    /**
     * Table cell renederer class for Cost sharing table
     */
    class ConditionEditorTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private CoeusTextField txtComponent;
        private JLabel lblText;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        private JButton btnDetails;
//        private JPanel pnlBtn;
        java.awt.GridBagConstraints gridBagConstraints;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        /* Creates a costSharing Renderer*/
        public ConditionEditorTableRenderer(){
            txtComponent = new CoeusTextField();
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            btnDetails = new JButton();
            btnDetails.setIcon(iIcnDetails);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }
        
        /* returns renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
            switch(col) {
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                case DETAILS_ICON_COLUMN:
                    if(table.getValueAt(row,L_VALUE)!=null &&
                            !table.getValueAt(row, L_VALUE).toString().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                        BusinessRuleExpBean expBean =
                                (BusinessRuleExpBean)cvExpressionData.get(row);
                        if(expBean.getExpressionType()!=null &&
                                (expBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN) ||
                                expBean.getExpressionType().equals(TYPE_QUESTION))){
                            return btnDetails;
                        }
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                        if(functionType != TypeConstants.DISPLAY_MODE  && isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }
                        if(value == null || value.toString().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                            txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                            txtComponent.setText(txtComponent.getText());
                            lblText.setText(txtComponent.getText());
                        }else{
                            txtComponent.setText(value.toString());
                            lblText.setText(txtComponent.getText());
                        }
                        return lblText;
                    }
                    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                case L_VALUE:
                    /*if(functionType == TypeConstants.DISPLAY_MODE){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }*/
                    
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.BLACK);
                    if(functionType != TypeConstants.DISPLAY_MODE  && isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(value == null || value.toString().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                        txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                        txtComponent.setText(txtComponent.getText());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case OPERATOR:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                        txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                        txtComponent.setText(txtComponent.getText());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case R_VALUE:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                        txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                        txtComponent.setText(txtComponent.getText());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case LOGICAL:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                        txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                        txtComponent.setText(txtComponent.getText());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                    // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
                    case EXPRESSION_PREFIX:
                    case EXPRESSION_SUFFIX:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(value == null || value.toString().trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                        txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    lblText.setHorizontalAlignment(JLabel.CENTER);
                    return lblText;
                    // COEUSQA-2458-End
            }
            return txtComponent;
        }
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    /**
     * Returns the operator symbol for the given operator name
     * @param operatorName Name of the operator displayed in the gui
     * @return String operator symbol
     */
    public String getOperatorSymbol(String operatorName){
        String operatorSymbol = "";
        if(operatorName.equals(EQUAL_TO)){
            operatorSymbol = "=";
        }else if(operatorName.equals(NOT_EQUAL_TO)){
            operatorSymbol = "<>";
        }else if(operatorName.equals(LESS_THAN)){
            operatorSymbol = "<";
        }else if(operatorName.equals(GREATER_THAN)){
            operatorSymbol = ">";
        }else if(operatorName.equals(LESS_THAN_EQUAL_TO)){
            operatorSymbol = "<=";
        }else if(operatorName.equals(GREATER_THAN_EQUAL_TO)){
            operatorSymbol = ">=";
        }else if(operatorName.equals(BEGINS_WITH)){
            operatorSymbol = "BEGIN_WITH";
        }else if(operatorName.equals(ENDS_WITH)){
            operatorSymbol = "ENDS_WITH";
        }else if(operatorName.equals(CONTAINS)){
            operatorSymbol = "CONTAINS";
        }
        return operatorSymbol;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
    private void prepareComboBeans(){
        ComboBoxBean emptyBean = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING , CoeusGuiConstants.EMPTY_STRING);
        
        ComboBoxBean bean = new ComboBoxBean("0", "AND");
        cvLogicalOperators.addElement(emptyBean);
        cvLogicalOperators.addElement(bean);
        bean = new ComboBoxBean("1" , "OR");
        cvLogicalOperators.addElement(bean);
        
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//        ComboBoxBean comboBoxBean = new ComboBoxBean("0","<=");
//        cvOperators.addElement(emptyBean);
//        cvOperators.addElement(comboBoxBean);
//        comboBoxBean = new ComboBoxBean("1","<>");
//        cvOperators.addElement(comboBoxBean);
//        comboBoxBean = new ComboBoxBean("2","=");
//        cvOperators.addElement(comboBoxBean);
//        comboBoxBean = new ComboBoxBean("3",">=");
//        cvOperators.addElement(comboBoxBean);
        
        ComboBoxBean comboBoxBean = new ComboBoxBean("0",LESS_THAN_EQUAL_TO);
        cvOperators.addElement(emptyBean);
        cvOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("1",NOT_EQUAL_TO);
        cvOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("2",EQUAL_TO);
        cvOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("3",GREATER_THAN_EQUAL_TO);
        cvOperators.addElement(comboBoxBean);
        
        ComboBoxBean cmbBoxBean = new ComboBoxBean("0",EQUAL_TO);
        cvFunctionsOperators.addElement(emptyBean);
        cvFunctionsOperators.addElement(cmbBoxBean);
        cmbBoxBean = new ComboBoxBean("1",NOT_EQUAL_TO);
        cvFunctionsOperators.addElement(cmbBoxBean);
//        ComboBoxBean cmbBoxBean = new ComboBoxBean("0","=");
//        cvFunctionsOperators.addElement(emptyBean);
//        cvFunctionsOperators.addElement(cmbBoxBean);
//        cmbBoxBean = new ComboBoxBean("1","<>");
//        cvFunctionsOperators.addElement(cmbBoxBean);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        ComboBoxBean cmbRValueBean = new ComboBoxBean("0","YES");
        cvQuestionsRValues.addElement(emptyBean);
        cvQuestionsRValues.addElement(cmbRValueBean);
        cmbRValueBean = new ComboBoxBean("1","NO");
        cvQuestionsRValues.addElement(cmbRValueBean);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //cmbRValueBean = new ComboBoxBean("1","N/A");
        cmbRValueBean = new ComboBoxBean("2","N/A");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        cvQuestionsRValues.addElement(cmbRValueBean);
        
        ComboBoxBean cmbFuncRValBean = new ComboBoxBean("0","True");
        cvFunctionsRValues.addElement(emptyBean);
        cvFunctionsRValues.addElement(cmbFuncRValBean);
        cmbFuncRValBean = new ComboBoxBean("1","False");
        cvFunctionsRValues.addElement(cmbFuncRValBean);
        
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        cvQuestionaireOperators.addElement(emptyBean);
        comboBoxBean = new ComboBoxBean("0",CONTAINS);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("1",BEGINS_WITH);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("2",ENDS_WITH);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("3",EQUAL_TO);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("4",NOT_EQUAL_TO);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("5",GREATER_THAN);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("6",LESS_THAN);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("7",GREATER_THAN_EQUAL_TO);
        cvQuestionaireOperators.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("8",LESS_THAN_EQUAL_TO);
        cvQuestionaireOperators.addElement(comboBoxBean);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    }//End of prepareComboBeans
    
    private void showArgumentWindow(int row , char funcType) throws CoeusException{
        conditionTableEditor.stopCellEditing();
        
        conditionEditorForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        ArgumentController argumentController =
                new ArgumentController(ruleBaseBean , funcType);
        
        HashMap hmData = new HashMap();
        
        
        //int selRow = conditionEditorForm.tblRules.getSelectedRow();
        if(row != -1){
            BusinessRuleExpBean ruleExpBean =
                    (BusinessRuleExpBean)cvExpressionData.get(row);
            
            ruleExpBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
            ruleExpBean.setExpressionNumber(row + 1);
            
            hmData.put(BusinessRuleExpBean.class , ruleExpBean);
             //Modified for case id 3351 'Adding more arguements to rule function'- start
            if(cvRuleFuncArgs!=null && funcType == TypeConstants.NEW_MODE){
                Equals eqCondNo = new Equals("conditionNumber",new Integer(ruleExpBean.getConditionNumber()));
                Equals eqExpNo = new Equals("expressionNumber",new Integer(ruleExpBean.getExpressionNumber()));
                And andCond = new And(eqCondNo , eqExpNo);
                CoeusVector cvFilteredData = cvRuleFuncArgs.filter(andCond);
                if(cvFilteredData!=null){
                    for(int i=0; i<cvFilteredData.size(); i++){
                        BusinessRuleFuncArgsBean ruleFuncArgsBean = 
                            (BusinessRuleFuncArgsBean)cvFilteredData.get(i);
                        if(ruleFuncArgsBean.getAcType() == null ||
				(ruleFuncArgsBean.getAcType() != null &&
                                ruleFuncArgsBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) ){
                            ruleFuncArgsBean.setAcType(TypeConstants.DELETE_RECORD);
                        }else if(ruleFuncArgsBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            for(int index =0 ; index < cvRuleFuncArgs.size() ; index++){
                                BusinessRuleFuncArgsBean masterRuleFunArgsBean = 
                                    (BusinessRuleFuncArgsBean)cvRuleFuncArgs.get(index);
                                if(masterRuleFunArgsBean.getConditionNumber() == ruleFuncArgsBean.getConditionNumber() &&
                                   masterRuleFunArgsBean.getExpressionNumber() == ruleFuncArgsBean.getExpressionNumber() &&
                                   masterRuleFunArgsBean.getFunctionName().equals(ruleFuncArgsBean.getFunctionName()) &&
                                   masterRuleFunArgsBean.getArgumentName().equals(ruleFuncArgsBean.getArgumentName())){
                                       cvRuleFuncArgs.remove(index);
                                       break;
                                }
                           }
                        }
                    }
                }
            }
        }
        hmData.put(BusinessRuleFuncArgsBean.class , cvRuleFuncArgs);
        //Modified for case id 3351 'Adding more arguements to rule function'- end
        
        argumentController.setFormData(hmData);
        
        conditionEditorForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
        argumentController.display();
        if(argumentController.isOkPressed() || funcType == TypeConstants.NEW_MODE) {
             //Modified for case id 3351 'Adding more arguements to rule function'- start
            //Takes vector of function arguments and updates it to the vector cvRuleFuncArgs
//            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = 
//                    (BusinessRuleFuncArgsBean)argumentController.getFormData();
//            //updateFuncArgData(funcType , businessRuleFuncArgsBean);
//            updateFuncArgData(businessRuleFunArgsBean); 
            CoeusVector cvNewRuleFuncArgs = (CoeusVector)argumentController.getFormData();
            updateFuncArgData(cvNewRuleFuncArgs); 
            //Modified for Case id 3351 'Adding more arguements to rule function'- end
        }
        setModified(true);
        argumentController.cleanUp();
    }
    
        //Modified for Case id 3351 'Adding more arguements to rule function' - start
    /*Commented and added a new function updateFuncArgData which takes CoeusVector*/
//    private void updateFuncArgData(BusinessRuleFuncArgsBean businessRuleFuncArgsBean){
//        int selRow = conditionEditorForm.tblRules.getSelectedRow();
//        int expNo = selRow + 1;
//        
//        //if(funcType == TypeConstants.MODIFY_MODE){
//        if(cvRuleFuncArgs != null && cvRuleFuncArgs.size()>0){
//            
//                /*NotEquals neCondNo = new NotEquals("conditionNumber" , new Integer(businessRuleFuncArgsBean.getConditionNumber()));
//                NotEquals neExpNo = new NotEquals("expressionNumber" ,  new Integer(businessRuleFuncArgsBean.getExpressionNumber()));
//                And neCondNoAndneExpNo = new And (neCondNo , neExpNo);*/
//            
//            
//            Equals eqCondNo = new Equals("conditionNumber" , new Integer(businessRuleConditionsBean.getConditionNumber()));
//            Equals eqExpNo = new Equals("expressionNumber" ,  new Integer(expNo));
//            And eqCondNoAndeqExpNo = new And(eqCondNo , eqExpNo);
//            
//            CoeusVector cvFilter = cvRuleFuncArgs.filter(eqCondNoAndeqExpNo);
//            if(cvFilter != null && cvFilter.size()>0){
//                BusinessRuleFuncArgsBean ruleFuncArgsBean =
//                        (BusinessRuleFuncArgsBean)cvFilter.get(0);
//                
//                if(ruleFuncArgsBean.getAcType() == null ||
//                        ruleFuncArgsBean.getAcType() == TypeConstants.UPDATE_RECORD){
//                    ruleFuncArgsBean.setArgumentName(businessRuleFuncArgsBean.getArgumentName());
//                    ruleFuncArgsBean.setArgumentType(businessRuleFuncArgsBean.getArgumentType());
//                    ruleFuncArgsBean.setDefaultValue(businessRuleFuncArgsBean.getDefaultValue());
//                    ruleFuncArgsBean.setFunctionName(businessRuleFuncArgsBean.getFunctionName());
//                    ruleFuncArgsBean.setRuleExpDescription(businessRuleFuncArgsBean.getRuleExpDescription());
//                    ruleFuncArgsBean.setRuleFuncDescription(businessRuleFuncArgsBean.getRuleFuncDescription());
//                    ruleFuncArgsBean.setSequenceNumber(businessRuleFuncArgsBean.getSequenceNumber());
//                    ruleFuncArgsBean.setValue(businessRuleFuncArgsBean.getValue());
//                    ruleFuncArgsBean.setWindowName(businessRuleFuncArgsBean.getWindowName());
//                    ruleFuncArgsBean.setExpressionNumber(expNo);
//                    ruleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
//                }else if(ruleFuncArgsBean.getAcType() == TypeConstants.INSERT_RECORD){
//                    
//                    //Case 2271 Start
//                        /*NotEquals neCondNo = new NotEquals("conditionNumber" , new Integer(businessRuleConditionsBean.getConditionNumber()));
//                        NotEquals neExpNo = new NotEquals("expressionNumber" ,  new Integer(expNo));
//                        And neCondNoAndneExpNo = new And(neCondNo , neExpNo);
//                        cvRuleFuncArgs = cvRuleFuncArgs.filter(neCondNoAndneExpNo);*/
//                    
//                    for(int index =0 ; index < cvRuleFuncArgs.size() ; index++){
//                        BusinessRuleFuncArgsBean ruleFunArgsBean =
//                                (BusinessRuleFuncArgsBean)cvRuleFuncArgs.get(index);
//                        
//                        if(ruleFunArgsBean.getConditionNumber() == businessRuleConditionsBean.getConditionNumber() &&
//                                ruleFunArgsBean.getExpressionNumber() == expNo){
//                            cvRuleFuncArgs.remove(index);
//                            break;
//                        }
//                    }//End of for
//                    
//                    //Case 2271 End
//                    
//                    businessRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
//                    businessRuleFuncArgsBean.setExpressionNumber(expNo);
//                    cvRuleFuncArgs.add(businessRuleFuncArgsBean);
//                }
//                //@TODO
//                //ruleFuncArgsBean.setConditionNumber(
//                //ruleFuncArgsBean.setRuleId(
//                //ruleFuncArgsBean.setUnitNumber(
//            }else{
//                businessRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
//                businessRuleFuncArgsBean.setExpressionNumber(expNo);
//                cvRuleFuncArgs.add(businessRuleFuncArgsBean);
//            }
//            
//        }else{
//            businessRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
//            businessRuleFuncArgsBean.setExpressionNumber(expNo);
//            cvRuleFuncArgs.add(businessRuleFuncArgsBean);
//        }
//        
//        /*}else{
//             if(selRow != -1){
//         
//                 int expNo = selRow + 1;
//                 Equals eqCondNo = new Equals("conditionNumber" , new Integer(businessRuleConditionsBean.getConditionNumber()));
//                 Equals eqExpNo = new Equals("expressionNumber" ,  new Integer(expNo));
//                 And eqCondNoAndeqExpNo = new And(eqCondNo , eqExpNo);
//         
//                 CoeusVector cvFilteredData = cvRuleFuncArgs.filter(eqCondNoAndeqExpNo);
//                 if(cvFilteredData != null && cvFilteredData.size() > 0){
//                     BusinessRuleFuncArgsBean ruleFuncArgsBean =
//                                (BusinessRuleFuncArgsBean)cvFilteredData.get(0);
//                     if(ruleFuncArgsBean.getAcType() == null){
//                         ruleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
//                     }else if(ruleFuncArgsBean.getAcType() == TypeConstants.INSERT_RECORD){
//                         NotEquals neCondNo = new NotEquals("conditionNumber" , new Integer(businessRuleConditionsBean.getConditionNumber()));
//                         NotEquals neExpNo = new NotEquals("expressionNumber" ,  new Integer(expNo));
//                         And neCondNoAndneExpNo = new And(neCondNo , neExpNo);
//                         cvRuleFuncArgs = cvRuleFuncArgs.filter(neCondNoAndneExpNo);
//                         businessRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
//                         businessRuleFuncArgsBean.setExpressionNumber(expNo);
//                         cvRuleFuncArgs.add(businessRuleFuncArgsBean);
//                     }
//                 }//End of inner if
//             }//End of if(selRow.......
//        }//End of else */
//    }//End of updateFuncArgData
    
    /**
     * Updates the vector with function arguments with the newly added or 
     * modified function arguments
     *
     * @param CoeusVector with the modified function arguments
     */
    private void updateFuncArgData(CoeusVector cvNewRuleFuncArgs){
        int selRow = conditionEditorForm.tblRules.getSelectedRow();
        int expNo = selRow + 1;
        if(cvNewRuleFuncArgs!=null){
            if(cvRuleFuncArgs != null && cvRuleFuncArgs.size()>0){
                Equals eqCondNo = new Equals("conditionNumber" , new Integer(businessRuleConditionsBean.getConditionNumber()));
                Equals eqExpNo = new Equals("expressionNumber" ,  new Integer(expNo));
                And eqCondNoAndeqExpNo = new And(eqCondNo , eqExpNo);
                
                CoeusVector cvFilter = cvRuleFuncArgs.filter(eqCondNoAndeqExpNo);
                if(cvFilter != null && cvFilter.size()>0){
                    for(int i=0; i<cvNewRuleFuncArgs.size(); i++){
                        BusinessRuleFuncArgsBean newRuleFuncArgsBean = 
                                    (BusinessRuleFuncArgsBean)cvNewRuleFuncArgs.get(i);
                        boolean found = false;
                        for(int j=0; j<cvFilter.size();j++){
                            BusinessRuleFuncArgsBean ruleFuncArgsBean = 
                                (BusinessRuleFuncArgsBean)cvFilter.get(j);
                            if(ruleFuncArgsBean.getFunctionName().equals(newRuleFuncArgsBean.getFunctionName()) &&
                                ruleFuncArgsBean.getArgumentName().equals(newRuleFuncArgsBean.getArgumentName())){
                                    found = true;
                                if(ruleFuncArgsBean.getAcType() == null ||
                                   ruleFuncArgsBean.getAcType() == TypeConstants.UPDATE_RECORD || 
                                   ruleFuncArgsBean.getAcType() == TypeConstants.INSERT_RECORD){
                                       ruleFuncArgsBean.setArgumentName(newRuleFuncArgsBean.getArgumentName());
                                       ruleFuncArgsBean.setArgumentType(newRuleFuncArgsBean.getArgumentType());
                                       ruleFuncArgsBean.setDefaultValue(newRuleFuncArgsBean.getDefaultValue());
                                       ruleFuncArgsBean.setFunctionName(newRuleFuncArgsBean.getFunctionName());
                                       ruleFuncArgsBean.setRuleExpDescription(newRuleFuncArgsBean.getRuleExpDescription());
                                       ruleFuncArgsBean.setRuleFuncDescription(newRuleFuncArgsBean.getRuleFuncDescription());
                                       ruleFuncArgsBean.setSequenceNumber(newRuleFuncArgsBean.getSequenceNumber());
                                       ruleFuncArgsBean.setValue(newRuleFuncArgsBean.getValue());
                                       ruleFuncArgsBean.setWindowName(newRuleFuncArgsBean.getWindowName());
                                       ruleFuncArgsBean.setExpressionNumber(expNo);
                                       if(ruleFuncArgsBean.getAcType() == null){
                                            ruleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                                       }
                                }else if(ruleFuncArgsBean.getAcType() == TypeConstants.DELETE_RECORD){
                                    newRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
                                    newRuleFuncArgsBean.setExpressionNumber(expNo);
                                    newRuleFuncArgsBean.setAcType(TypeConstants.INSERT_RECORD);
                                    cvRuleFuncArgs.add(newRuleFuncArgsBean);
                                }
                            }
                        }//end of for(int j=0; j<cvFilter.size()
                        if(!found){
                            newRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
                            newRuleFuncArgsBean.setExpressionNumber(expNo);
                            newRuleFuncArgsBean.setAcType(TypeConstants.INSERT_RECORD);
                            cvRuleFuncArgs.add(newRuleFuncArgsBean);
                        }
                    }
                    //@TODO
                    //ruleFuncArgsBean.setConditionNumber(
                    //ruleFuncArgsBean.setRuleId(
                    //ruleFuncArgsBean.setUnitNumber(
                }else{
                    BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
                    for(int i=0; i<cvNewRuleFuncArgs.size(); i++){
                        businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvNewRuleFuncArgs.get(i);
                        businessRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
                        businessRuleFuncArgsBean.setExpressionNumber(expNo);
                        cvRuleFuncArgs.add(businessRuleFuncArgsBean);
                    }
                }                
            }else{
                BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
                for(int i=0; i<cvNewRuleFuncArgs.size(); i++){
                    businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvNewRuleFuncArgs.get(i);
                    businessRuleFuncArgsBean.setConditionNumber(businessRuleConditionsBean.getConditionNumber());
                    businessRuleFuncArgsBean.setExpressionNumber(expNo);
                    cvRuleFuncArgs.add(businessRuleFuncArgsBean);
                }
            }
        }
    }//End of updateFuncArgData
    //Modified for Case id 3351 'Adding more arguements to rule function'- end
    private void populateConditionCombo(){
        CoeusVector cvFunc = new CoeusVector();
        
        ComboBoxBean comboBoxBean = new ComboBoxBean("1","Valid");
        cvFunc.addElement(comboBoxBean);
        comboBoxBean = new ComboBoxBean("-1","Invalid");
        cvFunc.addElement(comboBoxBean);
        
        validationForm.cmbCondition.setModel(new DefaultComboBoxModel(cvFunc));
        
        Equals eqActionCode = new Equals("code" , ""+businessRuleConditionsBean.getAction());
        CoeusVector cvFiltered = cvFunc.filter(eqActionCode);
        if(cvFiltered != null && cvFiltered.size() > 0){
            comboBoxBean = (ComboBoxBean)cvFiltered.get(0);
            validationForm.cmbCondition.setSelectedItem(comboBoxBean);
        }
    }
    
    public void cleanUp(){
        //conditionEditorForm.tbdPnConditions.removeChangeListener(this);
        
        scrPnColumns.remove(columnsController.getControlledUI());
        scrPnColumns = null;
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//        scrPnQuestions.remove(questionsController.getControlledUI());
//        scrPnQuestions = null;
        if(moduleCode.equals("3")){
            scrPnQuestions.remove(questionsController.getControlledUI());
            scrPnQuestions = null;
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        scrPnFunctions.remove(functionsController.getControlledUI());
        scrPnFunctions = null;
        
        businessRuleConditionsBean =  null;
        columnsController = null;
        questionsController = null;
        functionsController = null;
        
        conditionEditorTableModel = null;
        conditionEditorTableRenderer = null;
        
        cvColumnsData = null;
        cvQuestionsData = null;
        cvFunctionsData = null;
        cvExpressionData = null;
        cvRuleFuncArgs = null;
        conditionTableEditor = null;
        cvLogicalOperators = null;
        cvOperators = null;
        cvFunctionsOperators = null;
        cvQuestionsRValues = null;
        cvFunctionsRValues = null;
        cvDeletedData = null;
        
        cmbOperatorType = null;
        cmbLogicalOperator = null;
        cmbRValue = null;
        
        targetDrop = null;
        
        validationForm = null;
        conditionEditorForm  = null;
        dlgConditionEditor  = null;
    }
    //Added for case 2785 - Routing enhancement - start
    /**
     * Checks whether there is any modification done to the fields
     * description, user message, map and valid/invalid. If modified set the 
     * variable modified to true else false
     *
     * @return boolean true if modifed else false
     */
    public boolean checkDataModified() {
        String strDesc = "";
        int action = -1;
        String userMessage = "";
    
        if(isTypeValidate()){
            strDesc = validationForm.txtDescription.getText().trim();
            ComboBoxBean cmbBean = (ComboBoxBean)validationForm.cmbCondition.getSelectedItem();
            action = Integer.parseInt(cmbBean.getCode());
            userMessage = validationForm.txtArUserMessage.getText().trim();
        }else{
            strDesc = conditionEditorForm.txtDescription.getText().trim();
            if(mapHeaderBean!=null){
                action = mapHeaderBean.getMapId();
            }
            userMessage = conditionEditorForm.txtArUserMessage.getText().trim();
        }
        
        //Check whether description is modified
        if(businessRuleConditionsBean.getRuleDescription() != null &&
                !businessRuleConditionsBean.getRuleDescription().trim().equals(strDesc)){
            if(modified == false){
                setModified(true);
            }//End of inner if
            if(businessRuleConditionsBean.getAcType() == null){
                businessRuleConditionsBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        //Check whether the user Message is modified
         if(businessRuleConditionsBean.getUserMessage() != null &&
                !businessRuleConditionsBean.getUserMessage().trim().equals(userMessage)){
            if(modified == false){
                setModified(true);
            }//End of inner if
            if(businessRuleConditionsBean.getAcType() == null){
                businessRuleConditionsBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        //Check whether action(map or (valid/invlaid)) is modified
        if(businessRuleConditionsBean.getAction()!= action){
            setModified(true);
        }
        if(businessRuleConditionsBean.getAcType() == null){
            businessRuleConditionsBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        
        return modified;
    }
    //Added for case 2785 - Routing enhancement - end
     /**
     * Getter for property modified.
     * @return Value of property modified.
     */
    public boolean isModified(){
        return modified;
    }
    /**
     * Setter for property modified.
     * @param modified New value of property modified.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    /**
     * Getter for property typeValidate.
     * @return Value of property typeValidate.
     */
    public boolean isTypeValidate() {
        return typeValidate;
    }
    
    /**
     * Setter for property typeValidate.
     * @param typeValidate New value of property typeValidate.
     */
    public void setTypeValidate(boolean typeValidate) {
        this.typeValidate = typeValidate;
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
     * Getter for property noOfCondtionsPresent.
     * @return Value of property noOfCondtionsPresent.
     */
    public int getNoOfCondtionsPresent() {
        return noOfCondtionsPresent;
    }
    
    /**
     * Setter for property noOfCondtionsPresent.
     * @param noOfCondtionsPresent New value of property noOfCondtionsPresent.
     */
    public void setNoOfCondtionsPresent(int noOfCondtionsPresent) {
        this.noOfCondtionsPresent = noOfCondtionsPresent;
    }
    
//    /*For setting the focus inside the table cell*/
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /**
     * Getter for property elsePresent.
     * @return Value of property elsePresent.
     */
    public boolean isElsePresent() {
        return elsePresent;
    }
    
    /**
     * Setter for property elsePresent.
     * @param elsePresent New value of property elsePresent.
     */
    public void setElsePresent(boolean elsePresent) {
        this.elsePresent = elsePresent;
    }
//Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    class SearchTableCellPanel extends JPanel implements ActionListener{
        public JTextField txtResult;
        public JButton btnSearch;
        public SearchTableCellPanel(){
            initComponents();
        }
        
        public void initComponents(){
            java.awt.GridBagConstraints gridBagConstraints;
            
            txtResult = new javax.swing.JTextField();
            btnSearch = new javax.swing.JButton();
            
            setLayout(new java.awt.GridBagLayout());
            txtResult.setEditable(false);
            txtResult.setMinimumSize(new java.awt.Dimension(178, 21));
            txtResult.setMaximumSize(new java.awt.Dimension(178, 21));
            txtResult.setPreferredSize(new java.awt.Dimension(178, 21));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            add(txtResult, gridBagConstraints);
            
            btnSearch.setMinimumSize(new java.awt.Dimension(21, 21));
            btnSearch.setMaximumSize(new java.awt.Dimension(21, 21));
            btnSearch.setPreferredSize(new java.awt.Dimension(21, 21));
            btnSearch.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
            btnSearch.addActionListener(this);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            add(btnSearch, gridBagConstraints);
        }
        
        public void actionPerformed(ActionEvent e) {
            int selectedRow = conditionEditorForm.tblRules.getSelectedRow();
            String windowTitle;
            if(selectedRow!=-1){
                BusinessRuleExpBean businessRuleExpBean =
                        (BusinessRuleExpBean)cvExpressionData.get(selectedRow);
                if(businessRuleExpBean!=null){
                    QuestionsMaintainanceBean questionMaintenanceBean =
                            (QuestionsMaintainanceBean)hmQuestionnaireQns.get(businessRuleExpBean.getLvalue());
                    if(questionMaintenanceBean.getLookupGui()!= null && questionMaintenanceBean.getLookupGui().trim().length() > 0 ){
                        //String answerValue = questionMaintenanceBean.getAnswer() == null ? EMPTY_STRING : questionMaintenanceBean.getAnswer();
                        String answerValue = "";
                        String lookUpWindow = questionMaintenanceBean.getLookupGui();
                        String lookUpArgument = questionMaintenanceBean.getLookupName();
                        if(lookUpArgument == null) {
                            lookUpArgument = CoeusGuiConstants.EMPTY_STRING;
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
                        }else  if(lookUpWindow.equalsIgnoreCase("OrganizationSearch")) {
                            lookUpWindow = ORGANIZATION_SEARCH;
                        }else  if(lookUpWindow.equalsIgnoreCase("SponsorSearch")) {
                            lookUpWindow = SPONSOR_SEARCH;
                        }
                        if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
                            if(lookUpArgument != null){
                                windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                                Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                                OtherLookupBean otherLookupBean =
                                        new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                                String result = showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, selectedRow);
                                txtResult.setText(result);
                            }
                        }else if(lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW)){
                            windowTitle = "Lookup Values";
                            if(lookUpArgument != null){
                                Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                                OtherLookupBean otherLookupBean =
                                        new OtherLookupBean(windowTitle, vecLookupdata, VALUE_COLUMN_NAMES);
                                String result = showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, selectedRow);
                                txtResult.setText(result);
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
                                String result = showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, selectedRow);
                                txtResult.setText(result);
                                }
                            
                        }else{
                            String result = showSearchWindow(lookUpWindow, answerValue, selectedRow);
                            txtResult.setText(result);
                        }
                    }
                }
            }
        }
        
        
    }
    /**
     * Helper method to display the Lookup Window when the Lookup button is Pressed
     */
    private String showLookupSearchWindow(OtherLookupBean otherLookupBean,
            String lookUpWindow, Vector vecLookupdata, int selectedRow){
        String result = CoeusGuiConstants.EMPTY_STRING;
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
                BusinessRuleExpBean businessRuleExpBean =
                        (BusinessRuleExpBean)cvExpressionData.get(selectedRow);
                businessRuleExpBean.setRvalue(code.trim());
                result =code.trim();
            }
        }
        return result;
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
    
    private String showSearchWindow(String searchType, String colValue, int selectedRow){
        String result = CoeusGuiConstants.EMPTY_STRING;
        try{
            CoeusSearch coeusSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), searchType, 1);
            coeusSearch.showSearchWindow();
            HashMap personInfo = coeusSearch.getSelectedRow();
            String pID = null;
            if(personInfo!=null){
                pID = Utils.convertNull(personInfo.get( "ID" ));
                if((pID!= null) && (!pID.equalsIgnoreCase(colValue))){
                    BusinessRuleExpBean businessRuleExpBean =
                            (BusinessRuleExpBean)cvExpressionData.get(selectedRow);
                    businessRuleExpBean.setRvalue(pID.trim());
                    result = pID.trim();
                }
            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
        return result;
    }
    
    /**
     * Show the questionnaire question details window
     */
    public void showQuestionnaireQuestionDetails(){
        YNQBean ynqBean = null;
        int selectedRow = conditionEditorForm.tblRules.getSelectedRow();
        if(selectedRow!=-1){
            BusinessRuleExpBean businessRuleExpBean =
                    (BusinessRuleExpBean)cvExpressionData.get(selectedRow);
            String selectedQuestionId = businessRuleExpBean.getLvalue();
            if(cvQuestionsData!=null){
                for(int i=0;i<cvQuestionsData.size();i++){
                    if(((YNQBean)(cvQuestionsData.get(i))).
                            getQuestionId().equals(selectedQuestionId)){
                        ynqBean = (YNQBean)(cvQuestionsData.get(i));
                        break;
                    }
                }
                try{
                    if(ynqBean!=null){
                        CoeusVector cvYNQExp = getYNQExpData(selectedQuestionId);
                        CoeusVector cvExp = new CoeusVector();
                        if(cvYNQExp!=null){
                            edu.mit.coeus.admin.bean.YNQExplanationBean adminYNQExpBean = null;
                            for(int i=0;i<cvYNQExp.size(); i++){
                                adminYNQExpBean = new edu.mit.coeus.admin.bean.YNQExplanationBean();
                                adminYNQExpBean.setQuestionId((
                                        (edu.mit.coeus.utils.question.bean.YNQExplanationBean)cvYNQExp.get(i)).
                                        getQuestionId());
                                adminYNQExpBean.setExplanation((
                                        (edu.mit.coeus.utils.question.bean.YNQExplanationBean)cvYNQExp.get(i)).
                                        getExplanation());
                                adminYNQExpBean.setExplanationType((
                                        (edu.mit.coeus.utils.question.bean.YNQExplanationBean)cvYNQExp.get(i)).
                                        getExplanationType());
                                cvExp.add(adminYNQExpBean);
                            }
                        }
                        AddModifyQuestionController addModifyQuestionController =
                                new AddModifyQuestionController(CoeusGuiConstants.getMDIForm(),
                                ynqBean, null, cvExp, TypeConstants.DISPLAY_MODE);
                        addModifyQuestionController.displayWindow();
                    }
                    
                }catch(CoeusException e){
                }
            }
        }
    }
    /**
     * Checks the type of the question selected(YNQ or questionnaire) and show
     * the details of the question in a popup window
     */
    public void showQuestionDetails(){
        int selectedRow = conditionEditorForm.tblRules.getSelectedRow();
        if(selectedRow!=-1){
            BusinessRuleExpBean businessRuleExpBean =
                    (BusinessRuleExpBean)cvExpressionData.get(selectedRow);
            String selectedQuestionId = businessRuleExpBean.getLvalue();
            if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTIONNAIRE_QN)){
                QuestionnaireQuestionsBean questionBean = questionnaireController.getQuestion(selectedQuestionId);
                if(questionBean!=null){
                    try {
                        QuestionnaireQuestionFormController questionnaireQnFormController =
                                new QuestionnaireQuestionFormController();
                        questionnaireQnFormController.setFormData(questionBean);
                        questionnaireQnFormController.display();
                    } catch (CoeusException ex) {
                        ex.printStackTrace();
                    }
                }
            }else if(businessRuleExpBean.getExpressionType().equals(TYPE_QUESTION)){
                showQuestionnaireQuestionDetails();
            }
        }
    }
    /**
     * Get the ynq details from the server for the given ynq id
     * 
     * @param questionId YNQ question id
     */
    private CoeusVector getYNQExpData(String questionId) throws CoeusException{
        CoeusVector cvData = null;
        
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        
        request.setDataObject(questionId);
        request.setFunctionType(GET_YNQ_EXPLANATION);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + RULE_SERVLET;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return cvData;
    }
    /**
     * Filter the questions which are of type P (Proposal) and 
     * status is active ('A')
     */
    public void filterProposalActiveQuestions(){
        Equals equalsType = new Equals("questionType", "P");
        Equals equalsStatus = new Equals("status", "A");
        And andStatusType = new And(equalsStatus, equalsType);
        if(cvQuestionsData!=null){
            cvProposalQuestions = cvQuestionsData.filter(andStatusType);
        }
    }
//Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    //Modifed for case 2418 - Business Rule Evaluation Bug -start
    /**
     * Set new expression numbers to the expressions in the table while deleting
     * or inserting a new row
     *
     * @param type -can have values "I"(insert) and "D"(Delete)
     * @param row -table row where the insertion or deletion is done
     */
    public void setNewExpressionNos(String type, int row){
        if(TypeConstants.INSERT_RECORD.equals(type)){
            if(cvExpressionData!=null){
                if(cvExpressionData.size() >= row ){//case 2158: budget validations
                    BusinessRuleExpBean businessRuleExpBean = null; 
                    businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(row);
                    setArgExpsNos(businessRuleExpBean.getExpressionNumber(),
                            businessRuleExpBean.getConditionNumber(), type);
                    for(int index = row; index<cvExpressionData.size(); index++){
                        businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(index);
                        int orgExpressionNo = businessRuleExpBean.getExpressionNumber();
                        businessRuleExpBean.setExpressionNumber(orgExpressionNo+1);
                    }
                }
            }
        }else if(TypeConstants.DELETE_RECORD.equals(type)){
            if(cvExpressionData!=null){
                if(cvExpressionData.size()-1 >= row ){//case 2158: budget validations
                    BusinessRuleExpBean businessRuleExpBean = null; 
                    businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(row);
                    int conditionNumber = businessRuleExpBean.getConditionNumber();
                    int orgExpressionNo = businessRuleExpBean.getExpressionNumber();
                    deleteArgExps(conditionNumber, orgExpressionNo);
                    setArgExpsNos(orgExpressionNo,conditionNumber, type);
                    for(int index = row+1; index<cvExpressionData.size(); index++){
                        businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(index);
                        orgExpressionNo = businessRuleExpBean.getExpressionNumber();
                        businessRuleExpBean.setExpressionNumber(orgExpressionNo-1);
                    }
                }
               
            }
        }
    }
    
    /**
     * Deletes the arguments when a condition expression is deleted from the table
     * 
     * @param conditionNumber 
     * @param orgExpNo the expression number of the argument to be deleted
     */
    public void deleteArgExps(int conditionNumber, int orgExpNo){
        if(cvRuleFuncArgs!=null){
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
            boolean found = true;
            while(found){
                found = false;
                for(int index = 0; index<cvRuleFuncArgs.size(); index++){
                    businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvRuleFuncArgs.get(index);
                    if(businessRuleFuncArgsBean.getConditionNumber() == conditionNumber 
                            && businessRuleFuncArgsBean.getExpressionNumber() == orgExpNo){
                            found = true;
                            cvRuleFuncArgs.remove(index);
                            break;
                    }
                }
            }
        }
    }
    
     /**
     * Set new expression numbers to the arguments of the expressions while deleting
     * or inserting a new row in the table
     *
     * @param orgExpNo -expression number of the arguments from which onwards the 
     *                  it has to be incremeted or decremented according the the type
     * @param conditionNumber 
     * @param type -can have values "I"(insert) and "D"(Delete)
     * 
     */
    public void setArgExpsNos(int orgExpNo, int conditionNumber, String type){
        if(cvRuleFuncArgs!=null){
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
            for(int index = 0; index<cvRuleFuncArgs.size(); index++){
                businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvRuleFuncArgs.get(index);
                int expNo = businessRuleFuncArgsBean.getExpressionNumber();
                if(businessRuleFuncArgsBean.getConditionNumber() == conditionNumber
                        && expNo >= orgExpNo){
                    if(TypeConstants.DELETE_RECORD.equals(type)){
                        businessRuleFuncArgsBean.setExpressionNumber(expNo - 1);
                    }else if(TypeConstants.INSERT_RECORD.equals(type)){
                        businessRuleFuncArgsBean.setExpressionNumber(expNo + 1);
                    }
                }
            }
        }
    }
    /**
     * Set the expression numbers of the expressions and the arguments in 
     * sequence order if they are not in order
     */
    public void resetExpressionNumbers(){
        if(cvExpressionData!=null){
            BusinessRuleExpBean businessRuleExpBean = null;
//            int expressionNumber = 0;
            for(int i=0; i<cvExpressionData.size(); i++){
                businessRuleExpBean = (BusinessRuleExpBean)cvExpressionData.get(i);
                if(businessRuleExpBean.getExpressionNumber() != (i+1)){
                    businessRuleExpBean.setAcType(TypeConstants.UPDATE_RECORD);
                    resetArgExpressionNo(businessRuleExpBean.getExpressionNumber(),
                            i+1, businessRuleExpBean.getConditionNumber());
                    businessRuleExpBean.setExpressionNumber(i+1);
                    
                }
            }
        }
    }
    
    // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
    // Getter Method for field validatingForm
    public boolean isValidatingForm() {
        return validatingForm;
    }
    
    // Setter Method for field validatingForm
    public void setValidatingForm(boolean validating) {
        this.validatingForm = validating;
    }
    // COEUSQA-2458-End

    /**
     * Find the arguments with expression number orgExpNo and set it to the newExpNo
     *
     * @param orgExpNo original expression number
     * @param newExpNo new expression number
     * @param conditionNumber 
     */
    public void resetArgExpressionNo(int orgExpNo, int newExpNo, int conditionNumber){
        if(cvRuleFuncArgs!=null){
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
            for(int index = 0; index<cvRuleFuncArgs.size(); index++){
                businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvRuleFuncArgs.get(index);
                int expNo = businessRuleFuncArgsBean.getExpressionNumber();
                if(businessRuleFuncArgsBean.getConditionNumber() == conditionNumber
                        && expNo == orgExpNo){
                    businessRuleFuncArgsBean.setExpressionNumber(newExpNo);
                 }
            }
        }
    }
    
    //Modifed for case 2418 - Business Rule Evaluation Bug -end

}    
