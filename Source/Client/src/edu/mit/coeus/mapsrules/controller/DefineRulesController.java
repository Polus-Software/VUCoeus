/*
 * @(#)DefineRuleController.java 1.0 10/18/05 3:49 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 04-Feb-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleConditionsBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleExpBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleFuncArgsBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.gui.DefineRulesForm;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionsMaintainanceBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  ajaygm
 */
public class DefineRulesController extends RuleController
        implements ActionListener, ItemListener{
    
    /** Holds an instance of <CODE>ApprEquipmentForm</CODE> */
    private DefineRulesForm defineRulesForm;
    
    /**
     * To create an instance of MDIform
     */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of the Dialog
     */
    private CoeusDlgWindow dlgDefineRulesForm;
    
    /**
     * Instance of Coeus Message Resources
     */
    private CoeusMessageResources coeusMessageResources;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
    
    /*Table Model and Renderer for Define Rule*/
    private DefineRulesTableModel defineRulesTableModel;
    private DefineRulesRenderer defineRulesRenderer;
    
    private char functionType;
    
    private RuleBaseBean ruleBaseBean = new RuleBaseBean();
    
    /*For setting the dimentions*/
    //Modified for case# 2784 - Routing enhancements - start
//    private static final String WINDOW_TITLE = "Define Rule";
//    private static final int WIDTH = 625;
//    private static final int HEIGHT = 378;
    private String windowTitle = "Create Business Rule";
    private static final int WIDTH = 720;
    private static final int HEIGHT = 500;
    //Modified for case# 2784 - Routing enhancements - end
    private int ruleId;
    
    private CoeusVector cvMasterData;
    private CoeusVector cvRuleDetails;
    private CoeusVector cvRuleCond;
    private CoeusVector cvRuleExp;
    private CoeusVector cvRuleFuncArgs;
    private CoeusVector cvDeletedExpData;
    private CoeusVector cvDeletedCondData;
    
    private static final char GET_DEFINE_RULE_DATA = 'B';
    private static final char SAVE_RULE_CONDITIONS = 'J';
    
    private static final String RULES_SERVLET = "/RuleMaintenanceServlet";
    
    /*Column indexes for the table*/
    private static final int DUMMY_COLUMN = 0;
    private static final int CONDITION_COLUMN = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int USE_COLUMN  = 3;
    private static final int MAP_COLUMN = 4;
    
    private static char INSERT_ROW = 'I';
    
    private BusinessRuleBean businessRuleBean;
    
    private boolean okClicked = false;
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    private Map hmSubModules = new HashMap();
    private Map hmQuestionnaireQuestions = new HashMap();
    private String initialModuleCode;
    private String initialSubModuleCode;
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    //Validation Messages
    private static final String SEL_ROW_FOR_MODIFY = "defineRules_exceptionCode.1051";
    private static final String SEL_ROW_FOR_VIEW = "defineRules_exceptionCode.1052";
    private static final String NO_ROW_TO_DELETE = "defineRules_exceptionCode.1053";
    private static final String SEL_ROT_TO_DELETE = "defineRules_exceptionCode.1054";
    private static final String DELETE_CONFIRMATION = "defineRules_exceptionCode.1055";
    private static final String ENTER_RULE_DESC = "defineRules_exceptionCode.1056";
    private static final String NO_COND = "defineRules_exceptionCode.1057";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    private static final String SELECT_MODULE = "defineRules_exceptionCode.1058";
    private static final String QUESTION_MESSAGE = "defineRules_exceptionCode.1059";
   //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    /** Creates a new instance of DefineRuleController */
    public DefineRulesController(RuleBaseBean ruleBaseBean , char functionType) {
        this.ruleBaseBean = ruleBaseBean;
        this.functionType = functionType;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        formatFields();
        postInitComponents();
        //setFormData(null);
        
    }
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgDefineRulesForm = new CoeusDlgWindow(mdiForm);
        dlgDefineRulesForm.setResizable(false);
        dlgDefineRulesForm.setModal(true);
        dlgDefineRulesForm.getContentPane().add(defineRulesForm);
        //Modified for case# 2784 - Routing enhancements - start
        if(functionType == TypeConstants.MODIFY_MODE){
            windowTitle = "Modify Business Rule";
        }
        //Modified for case# 2784 - Routing enhancements - end
        dlgDefineRulesForm.setTitle(windowTitle);
        dlgDefineRulesForm.setFont(CoeusFontFactory.getLabelFont());
        dlgDefineRulesForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgDefineRulesForm.getSize();
        dlgDefineRulesForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgDefineRulesForm.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        dlgDefineRulesForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
        
        dlgDefineRulesForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgDefineRulesForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        //code for disposing the window ends
    }
    
    public void display() {
        dlgDefineRulesForm.setVisible(true);
    }
    
    public void formatFields() {
        defineRulesForm.txtArDescription.setDocument(new LimitedPlainDocument(200));
        defineRulesForm.txtId.setEditable(false);
        defineRulesForm.txtId.setEnabled(false);
        if(functionType == TypeConstants.DISPLAY_MODE){
            defineRulesForm.cmbType.setEnabled(false);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            defineRulesForm.cmbModule.setEnabled(false);
            defineRulesForm.cmbSubmodule.setEnabled(false);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            defineRulesForm.txtArDescription.setEditable(false);
            defineRulesForm.txtArDescription.setEnabled(false);
            defineRulesForm.txtArDescription.setDisabledTextColor(Color.BLACK);
            defineRulesForm.txtArDescription.setBackground(disabledBackground);
            
            defineRulesForm.btnOK.setEnabled(false);
            defineRulesForm.btnAdd.setEnabled(false);
            defineRulesForm.btnInsert.setEnabled(false);
            defineRulesForm.btnDelete.setEnabled(false);
            
            defineRulesForm.btnModify.setText("View");
            defineRulesForm.btnModify.setMnemonic('V');
            defineRulesForm.btnModify.setFont(CoeusFontFactory.getLabelFont());
            defineRulesForm.btnModify.setMinimumSize(defineRulesForm.btnDelete.getMinimumSize());
            
        }else if(functionType == TypeConstants.MODIFY_MODE){
            defineRulesForm.cmbType.setEnabled(false);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            defineRulesForm.cmbModule.setEnabled(false);
            defineRulesForm.cmbSubmodule.setEnabled(false);
            
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }else{
            defineRulesForm.cmbType.setEnabled(true);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            defineRulesForm.cmbModule.setEnabled(true);
            defineRulesForm.cmbSubmodule.setEnabled(true);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }
    }
    
    /** To set the default focus for the component
     */
    public void requestDefaultFocus(){
        if(functionType == TypeConstants.DISPLAY_MODE){
            defineRulesForm.btnCancel.requestFocus();
        }else{
            if(defineRulesForm.cmbType.isEnabled()){
                defineRulesForm.cmbType.requestFocusInWindow();
            }else{
                defineRulesForm.txtArDescription.requestFocusInWindow();
            }
        }
    }
    
    public java.awt.Component getControlledUI() {
        return defineRulesForm;
    }
    
    public Object getFormData() {
        return businessRuleBean;
    }
    
    public void registerComponents() {
        defineRulesForm = new DefineRulesForm();
        defineRulesForm.btnOK.addActionListener(this);
        defineRulesForm.btnCancel.addActionListener(this);
        
        defineRulesForm.btnAdd.addActionListener(this);
        defineRulesForm.btnDelete.addActionListener(this);
        defineRulesForm.btnInsert.addActionListener(this);
        defineRulesForm.btnModify.addActionListener(this);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        defineRulesForm.cmbModule.addItemListener(this);
        defineRulesForm.cmbType.addItemListener(this);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        /** Code for focus traversal - start */
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Added the component cmbModule and cmbSubmodule into the components array
        java.awt.Component[] components = { defineRulesForm.cmbType, defineRulesForm.cmbModule,
        defineRulesForm.cmbSubmodule, defineRulesForm.txtArDescription, defineRulesForm.btnOK,
        defineRulesForm.btnCancel, defineRulesForm.btnAdd,defineRulesForm.btnInsert,
        defineRulesForm.btnModify,defineRulesForm.btnDelete};
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        defineRulesForm.setFocusTraversalPolicy(traversePolicy);
        defineRulesForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        defineRulesTableModel = new DefineRulesTableModel();
        defineRulesRenderer = new DefineRulesRenderer();
        defineRulesForm.tblConditions.setModel(defineRulesTableModel);
        
        setTableRenderer();
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(defineRulesForm.btnOK)){
                prepareBeansForSave();
                if(isSaveRequired()){
                    if(validate()){
                        dlgDefineRulesForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        saveFormData();
                        setOkClicked(true);
                        dlgDefineRulesForm.dispose();
                        //cleanUp();
                    }
                }else{
                    dlgDefineRulesForm.dispose();
                    //cleanUp();
                }
            }else if(source.equals(defineRulesForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(defineRulesForm.btnAdd)){
                performAddAction();
            }else if(source.equals(defineRulesForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(defineRulesForm.btnInsert)){
                performInsertAction();
            }else if(source.equals(defineRulesForm.btnModify)){
                performModifyAction();
            }
        }catch (CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }catch (CoeusUIException cuiEx){
            cuiEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cuiEx.getMessage());
        }finally{
            dlgDefineRulesForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
//        String protocolModule = ""+ModuleConstants.PROTOCOL_MODULE_CODE;
        //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
        if(itemEvent.getStateChange()== ItemEvent.SELECTED){
            if(source.equals(defineRulesForm.cmbModule)){
                String moduleId = "";
                if(defineRulesForm.cmbModule.getSelectedItem()!=null){
                    moduleId = ((ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem()).getCode();
                }
                //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
                //Disabling cmbSubmodule if Type is Validation and Module is IRB                
                // Modified for COEUSQA-1724_ Implement validation based on rules in protocols_Start
                if(defineRulesForm.cmbType.getSelectedItem()!= null &&
                        ((ComboBoxBean)defineRulesForm.cmbType.getSelectedItem()).getCode().equals(VALIDATION) &&                        
                        //protocolModule.equals(((ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem()).getCode()) ){
                        checkModule(moduleId)){
                    defineRulesForm.cmbSubmodule.setEnabled(false);
                    defineRulesForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
                }else{
                    //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-Start
                    //Enable the sub module combo box for IACUC and IRB module if bussiness rule is created for Questioniare
                    if(defineRulesForm.cmbType.getSelectedItem()!= null &&
                        QUESTION.equals(((ComboBoxBean)defineRulesForm.cmbType.getSelectedItem()).getCode())){
                        defineRulesForm.cmbSubmodule.setEnabled(true);
                        populateSubModuleComboBox(moduleId);                     
                    //Enable the sub module combo box for Development proposal module if bussiness rule is created for Validation
                    }else if(defineRulesForm.cmbType.getSelectedItem()!= null &&
                        VALIDATION.equals(((ComboBoxBean)defineRulesForm.cmbType.getSelectedItem()).getCode())
                        && !checkModule(moduleId)){
                        defineRulesForm.cmbSubmodule.setEnabled(true);
                        populateSubModuleComboBox(moduleId);
                    //Disabled sub module combo box for rest of the business rules.
                    }else{
                        defineRulesForm.cmbSubmodule.setEnabled(false);
                        defineRulesForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
                    }                    
                    //populateSubModuleComboBox(moduleId);
                    //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-End
                }
                // Modified for COEUSQA-1724_ Implement validation based on rules in protocols_End
                //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end

            }else if(source.equals(defineRulesForm.cmbType)){
                //Modified for case 3649 -Add Label and hide for Error/Warning Radio Buttons - start
                //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
                //Disabling cmbSubmodule if Type is Validation and Module is IRB
                String moduleId = "";
                if(defineRulesForm.cmbModule.getSelectedItem()!=null){
                    moduleId = ((ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem()).getCode();
                }
                //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
                if(defineRulesForm.cmbType.getSelectedItem()!= null &&
                        ((ComboBoxBean)defineRulesForm.cmbType.getSelectedItem()).getCode().equals(VALIDATION)){
                      defineRulesForm.lblValidationType.setVisible(true);
                      defineRulesForm.rbError.setVisible(true);
                      defineRulesForm.rbWarning.setSelected(true);
                      defineRulesForm.rbWarning.setVisible(true);
                      //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start                      
//                      if(protocolModule.equals(moduleId)){                      
                      // Modified for COEUSQA-1724_ Implement validation based on rules in protocols_Start
                      if(checkModule(moduleId)){
                       // Modified for COEUSQA-1724_ Implement validation based on rules in protocols_End
                       //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-Start
                       //Disable the sub module combo box for IACUC/IRB business rule if rule type is validation
                        defineRulesForm.cmbSubmodule.setEnabled(false);
                       //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-End
                        defineRulesForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
                      }else{
                          //Enable the sub module combo box for Develorment Proposal business rule if rule type is validation
                          defineRulesForm.cmbSubmodule.setEnabled(true);
                          populateSubModuleComboBox(moduleId);
                      }
                      //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-End
                }else{
                    defineRulesForm.rbWarning.setSelected(true);
                    defineRulesForm.rbError.setVisible(false);
                    defineRulesForm.rbWarning.setVisible(false);
                    defineRulesForm.lblValidationType.setVisible(false);
                    //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-Start
                    if(defineRulesForm.cmbType.getSelectedItem()!= null &&
                        QUESTION.equals(((ComboBoxBean)defineRulesForm.cmbType.getSelectedItem()).getCode())){
                        defineRulesForm.cmbSubmodule.setEnabled(true);
                        populateSubModuleComboBox(moduleId);
                        
                    }else{
                        defineRulesForm.cmbSubmodule.setEnabled(false);
                        defineRulesForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
                    }
                    //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-End
                    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start   
                    //defineRulesForm.cmbSubmodule.setEnabled(false);
                    populateSubModuleComboBox(moduleId);
                    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
                }
                //Modified for case 3649 -Add Label and hide for Error/Warning Radio Buttons - end
            }
        }
    }
    public void populateSubModuleComboBox(String moduleId){
        CoeusVector cvSubmodule = (CoeusVector)hmSubModules.get(moduleId);
        if(cvSubmodule!=null){
            //cvSubmodule.add(new ComboBoxBean("", ""));
            defineRulesForm.cmbSubmodule.setModel(new DefaultComboBoxModel(cvSubmodule));
        }else{
            defineRulesForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
        }
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    public void setFormData(Object data) throws CoeusException {
        this.businessRuleBean = (BusinessRuleBean)data;
        
        if(functionType != TypeConstants.ADD_MODE){
            if(businessRuleBean != null){
                ruleId = Integer.parseInt(businessRuleBean.getRuleId());
            }//End of inner if
        }//End of inner if
        
        cvMasterData = getRulesData();
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        CoeusVector cvQuestionnaireQns = null;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        if(functionType == TypeConstants.ADD_MODE){
            ruleId = ((Integer)cvMasterData.get(0)).intValue();
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            cvQuestionnaireQns = (CoeusVector)cvMasterData.get(1);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            defineRulesForm.txtId.setText(""+ruleId);
            defineRulesForm.txtArDescription.setText(EMPTY_STRING);
            
        }else{
            //Contains rule details data
            cvRuleDetails = (CoeusVector)cvMasterData.get(0);
            
            //Contains rule conditions data
            cvRuleCond = (CoeusVector)cvMasterData.get(1);
            
            //Contains rule expression data
            cvRuleExp = (CoeusVector)cvMasterData.get(2);
            
            //Contains rule expression data
            cvRuleFuncArgs = (CoeusVector)cvMasterData.get(3);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            cvQuestionnaireQns = (CoeusVector)cvMasterData.get(4);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            businessRuleBean = (BusinessRuleBean)cvRuleDetails.get(0);
            
            defineRulesForm.txtId.setText(businessRuleBean.getRuleId());
            defineRulesForm.txtArDescription.setText(
                    businessRuleBean.getDescription() == null ? EMPTY_STRING : businessRuleBean.getDescription());
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            if(businessRuleBean.getRuleCategory()!=null){
                defineRulesForm.rbError.setSelected(businessRuleBean.getRuleCategory().equals("E")? true:false);
            }
            //Modified for case 3649 -Add Label and hide for Error/Warning Radio Buttons - start
            if(businessRuleBean.getRuleType().equals(VALIDATION)){
                defineRulesForm.rbError.setVisible(true);
                defineRulesForm.rbWarning.setVisible(true);
                defineRulesForm.lblValidationType.setVisible(true);
            }else{
                defineRulesForm.rbError.setVisible(false);
                defineRulesForm.rbWarning.setVisible(false);
                defineRulesForm.lblValidationType.setVisible(false);
            }
            //Modified for case 3649 -Add Label and hide for Error/Warning Radio Buttons - end
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            cvRuleCond.sort("conditionSequence" , true);
            defineRulesTableModel.setData(cvRuleCond);
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        QuestionsMaintainanceBean qnMaintenanceBean = null;
        if (cvQuestionnaireQns != null) {
            for (int i = 0; i < cvQuestionnaireQns.size(); i++) {
                qnMaintenanceBean = (QuestionsMaintainanceBean) cvQuestionnaireQns.get(i);
                hmQuestionnaireQuestions.put(qnMaintenanceBean.getQuestionId().toString(), qnMaintenanceBean);
            }
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        populateCombo(businessRuleBean);
        if(functionType != TypeConstants.DISPLAY_MODE){
            if(cvRuleCond != null && cvRuleCond.size()>0){
                Equals eqNull = new Equals("conditionExp" , null);
                Equals eqEmpty = new Equals("conditionExp" , EMPTY_STRING);
                Or eqNullOReqEmpty = new Or(eqNull , eqEmpty);
                
                CoeusVector cvFilter = cvRuleCond.filter(eqNullOReqEmpty);
                if(cvFilter != null && cvFilter.size() > 0){
                    defineRulesForm.btnAdd.setEnabled(false);
                }
            }//End of inner if
        }//End of outer if
    }//End of setFormData
    
    private CoeusVector getRulesData() throws CoeusException{
        CoeusVector cvDataToServer = new CoeusVector();
        CoeusVector cvData = null;
        
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        
        cvDataToServer.add(new Character(functionType));
        cvDataToServer.add(new Integer(ruleId));
        
        request.setDataObject(cvDataToServer);
        request.setFunctionType(GET_DEFINE_RULE_DATA);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + RULES_SERVLET;
        
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
    
    private void populateCombo(BusinessRuleBean businessRuleBean) throws CoeusException{
        CoeusVector cvComboData = new CoeusVector();
        ComboBoxBean cmbBean = null;
        if(functionType == TypeConstants.ADD_MODE){
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //Added the item Question and changed the order
            cmbBean = new ComboBoxBean("N" , "Notification");
            cvComboData.add(cmbBean);
            cmbBean = new ComboBoxBean("Q" , "Question");
            cvComboData.add(cmbBean);
            cmbBean = new ComboBoxBean("R" , "Routing");
            cvComboData.add(cmbBean);
            cmbBean = new ComboBoxBean("V" , "Validation");
            cvComboData.add(cmbBean);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }else{
            if(businessRuleBean.getRuleType().equals("R")){
                cmbBean = new ComboBoxBean("R" , "Routing");
                cvComboData.add(cmbBean);
            }else if(businessRuleBean.getRuleType().equals("V")){
                cmbBean = new ComboBoxBean("V" , "Validation");
                cvComboData.add(cmbBean);
            }else if(businessRuleBean.getRuleType().equals("N")){
                cmbBean = new ComboBoxBean("N" , "Notification");
                cvComboData.add(cmbBean);
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            else if(businessRuleBean.getRuleType().equals("Q")){
                cmbBean = new ComboBoxBean("Q" , "Question");
                cvComboData.add(cmbBean);
            }
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            //End of else if
        }//End of else
        
        defineRulesForm.cmbType.setModel(new DefaultComboBoxModel(cvComboData));
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        if(functionType == TypeConstants.ADD_MODE){
            defineRulesForm.cmbType.setSelectedIndex(2);
        }
        
        //Populate the module combobox
        CoeusVector cvModule = QueryEngine.getInstance().getDetails(ruleBaseBean.getUnitNumber(), ModuleDataBean.class);
        CoeusVector cvModuleData = new CoeusVector();
        if(cvModule!=null){
            ComboBoxBean comboBoxBean = null;
            ModuleDataBean moduleDataBean = null;
            if(functionType == TypeConstants.ADD_MODE){
                for(int i=0; i<cvModule.size();i++){
                    moduleDataBean = (ModuleDataBean)cvModule.get(i);
                    comboBoxBean = new ComboBoxBean();
                    comboBoxBean.setCode(moduleDataBean.getCode());
                    comboBoxBean.setDescription(moduleDataBean.getDescription());
                    cvModuleData.add(comboBoxBean);
                }
            }else{
                for(int i=0; i<cvModule.size();i++){
                    moduleDataBean = (ModuleDataBean)cvModule.get(i);
                    if(moduleDataBean.getCode().equals(businessRuleBean.getModuleCode())){
                        comboBoxBean = new ComboBoxBean();
                        comboBoxBean.setCode(moduleDataBean.getCode());
                        comboBoxBean.setDescription(moduleDataBean.getDescription());
                        cvModuleData.add(comboBoxBean);
                        break;
                    }
                }
            }
            defineRulesForm.cmbModule.setModel(new DefaultComboBoxModel(cvModuleData));
        }
        
        //Populate the submodule combobox
        CoeusVector cvSubmodule = QueryEngine.getInstance().getDetails(ruleBaseBean.getUnitNumber(), SubModuleDataBean.class);
        CoeusVector cvModulewiseSubModules = null;
        if(cvSubmodule!=null){
            ComboBoxBean comboBoxBean = null;
            SubModuleDataBean submoduleDataBean = null;
            for(int i=0; i<cvSubmodule.size();i++){
                submoduleDataBean = (SubModuleDataBean)cvSubmodule.get(i);
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(submoduleDataBean.getCode());
                comboBoxBean.setDescription(submoduleDataBean.getDescription());
                //Modified with case 2158 : Budget Validations Start
                if(functionType == TypeConstants.ADD_MODE){
                    if(hmSubModules.get(Integer.toString(submoduleDataBean.getModuleCode()))!=null){
                        ((CoeusVector)hmSubModules.get(Integer.toString(submoduleDataBean.getModuleCode()))).add(comboBoxBean);
                    }else{
                        cvModulewiseSubModules = new CoeusVector();
                        cvModulewiseSubModules.add(new ComboBoxBean("", ""));
                        cvModulewiseSubModules.add(comboBoxBean);
                        hmSubModules.put(Integer.toString(submoduleDataBean.getModuleCode()), cvModulewiseSubModules);
                    }
                //Modified for COEUSDEV-86 : Questionnaire for Submission - Start
//                }else if(submoduleDataBean.getCode().equals(businessRuleBean.getSubmoduleCode())){//in modify mode
                }else if(submoduleDataBean.getModuleCode() == (new Integer(businessRuleBean.getModuleCode())).intValue() &&
                        submoduleDataBean.getCode().equals(businessRuleBean.getSubmoduleCode())){//COEUSDEV-86 : End
                    cvModulewiseSubModules = new CoeusVector();
                    cvModulewiseSubModules.add(comboBoxBean);
                    hmSubModules.put(Integer.toString(submoduleDataBean.getModuleCode()), cvModulewiseSubModules);
                    break;
                }
            }
        }
        if(defineRulesForm.cmbModule.getSelectedItem()!=null){
            initialModuleCode = ((ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem()).getCode();
            populateSubModuleComboBox(initialModuleCode);
        }
        //2158 End
        if(defineRulesForm.cmbSubmodule.getSelectedItem()!=null){
            initialSubModuleCode = ((ComboBoxBean)defineRulesForm.cmbSubmodule.getSelectedItem()).getCode();
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    }//end populateCombo
    
    private void setTableRenderer(){
        try{
            JTableHeader tableHeader = defineRulesForm.tblConditions.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            tableHeader.setPreferredSize(new Dimension(78,22));
            defineRulesForm.tblConditions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            defineRulesForm.tblConditions.setRowHeight(22);
            defineRulesForm.tblConditions.setSelectionBackground(java.awt.Color.yellow);
            defineRulesForm.tblConditions.setSelectionForeground(java.awt.Color.white);
            defineRulesForm.tblConditions.setShowHorizontalLines(false);
            defineRulesForm.tblConditions.setShowVerticalLines(false);
            defineRulesForm.tblConditions.setOpaque(false);
            
            defineRulesForm.tblConditions.setSelectionMode(
                    DefaultListSelectionModel.SINGLE_SELECTION);
            
            
            TableColumn column;
            //int minWidth[] = {30, 45, 90, 90, 90, 90, 90, 90};
            //Modified for case# 2785 - Routing enhancement - start
            //int prefWidth[] = {20, 33, 250, 60, 165};
            int prefWidth[] = {20, 33, 250, 60, 230};
            //Modified for case# 2785 - Routing enhancement - end
            for(int index = 0; index < prefWidth.length; index++) {
                column = defineRulesForm.tblConditions.getColumnModel().getColumn(index);
                column.setPreferredWidth(prefWidth[index]);
                column.setCellRenderer(defineRulesRenderer);
            }//End for
        }catch(Exception e){
            e.printStackTrace();
        }//End of catch
    }//End of setTableRenderer
    
    
    private void performAddAction() throws CoeusException{
        showConditionEditor(TypeConstants.ADD_MODE);
    }
    
    private void performInsertAction()throws CoeusException{
        showConditionEditor(INSERT_ROW);
    }
    
    private void performModifyAction()throws CoeusException{
        int selRow = defineRulesForm.tblConditions.getSelectedRow();
        if(selRow ==  -1){
            if(functionType == TypeConstants.DISPLAY_MODE){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SEL_ROW_FOR_VIEW));
                return ;
            }else {
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SEL_ROW_FOR_MODIFY));
                return ;
            }
        }
        
        showConditionEditor(TypeConstants.MODIFY_MODE);
    }
    
    private void performCancelAction(){
        prepareBeansForSave();
        if(isSaveRequired()){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_CHANGES),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    try{
                        if(validate()){
                            saveFormData();
                            setOkClicked(true);
                            dlgDefineRulesForm.dispose();
                            //cleanUp();
                        }
                    }catch (CoeusUIException coeusUIException){
                        coeusUIException.printStackTrace();
                        CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
                    }catch (CoeusException cEx){
                        cEx.printStackTrace();
                        CoeusOptionPane.showErrorDialog(cEx.getMessage());
                    }
                    break;
                    
                case(JOptionPane.NO_OPTION ):
                    dlgDefineRulesForm.dispose();
                    //cleanUp();
                    break;
                    
                default:
                    break;
            }
        }else{
            dlgDefineRulesForm.dispose();
            //cleanUp();
        }
    }//End of performCancelAction
    
    private void performDeleteAction(){
        if(defineRulesForm.tblConditions.getRowCount() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_ROW_TO_DELETE));
            return ;
        }
        
        int selRow = defineRulesForm.tblConditions.getSelectedRow();
        
        if(selRow ==  -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SEL_ROT_TO_DELETE));
            return ;
        }
        
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(DELETE_CONFIRMATION),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        
        if(selectedOption == CoeusOptionPane.SELECTION_YES) {
            BusinessRuleConditionsBean ruleBean =
                    (BusinessRuleConditionsBean)cvRuleCond.get(selRow);
            //Addec for case# 2785 - Routing enhancement - start
            //Remove the condition expressions
            CoeusVector cvFilterExp = null;
            NotEquals eqCondNo = new NotEquals("conditionNumber" , new Integer(ruleBean.getConditionNumber()));
            cvFilterExp = cvRuleExp.filter(eqCondNo);

            cvRuleExp = cvFilterExp;
            //Added for case# 2785 - Routing enhancement - end
            if(ruleBean.getAcType() == null
                    || !ruleBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                
                ruleBean.setAcType(TypeConstants.DELETE_RECORD);
                
                if(cvDeletedCondData == null){
                    cvDeletedCondData = new CoeusVector();
                }
                cvDeletedCondData.add(ruleBean);
            }
            
            cvRuleCond.remove(selRow);
            setSaveRequired(true);
            setCondtionSequenceNo();
            
            defineRulesTableModel.fireTableDataChanged();
            
            if(cvRuleCond.size() > 0){
                defineRulesForm.tblConditions.setRowSelectionInterval(0,0);
            }
            
            
            if(cvRuleCond != null && cvRuleCond.size()>0){
                Equals eqNull = new Equals("conditionExp" , null);
                Equals eqEmpty = new Equals("conditionExp" , EMPTY_STRING);
                Or eqNullOReqEmpty = new Or(eqNull , eqEmpty);
                
                CoeusVector cvFilter = cvRuleCond.filter(eqNullOReqEmpty);
                if(cvFilter != null && cvFilter.size() > 0){
                    defineRulesForm.btnAdd.setEnabled(false);
                }else{
                    defineRulesForm.btnAdd.setEnabled(true);
                }
            }//End of if
        }
    }//End of performDeleteAction
    
    private void showConditionEditor(char actionMode) throws CoeusException{
        //Added for case 2785 - Routing enhancement - start
        if((defineRulesForm.cmbModule.getSelectedItem() == null)){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_MODULE));
            return;
        }
        //Added for case 2785 - Routing enhancement - end
        defineRulesForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        HashMap hmData = new HashMap();
        CoeusVector cvExpData = null;
        CoeusVector cvFilter = null;
        
        BusinessRuleConditionsBean ruleConditionsBean = new BusinessRuleConditionsBean();
        
        if(actionMode == TypeConstants.MODIFY_MODE){
            int selRow = defineRulesForm.tblConditions.getSelectedRow();
            if(selRow != -1){
                ruleConditionsBean  =
                        (BusinessRuleConditionsBean)cvRuleCond.get(selRow);
                Equals eqCondNo = new Equals("conditionNumber" , new Integer(ruleConditionsBean.getConditionNumber()));
                
                cvFilter = cvRuleExp.filter(eqCondNo);
            }
            try{
                if(cvFilter != null){
                    cvExpData = (CoeusVector)ObjectCloner.deepCopy(cvFilter);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            
            hmData.put(BusinessRuleExpBean.class ,
                    cvExpData == null ? new CoeusVector() : cvExpData);
        }else{
            ruleConditionsBean.setConditionNumber(getMaxCondNo() + 1);
        }
        
        ruleConditionsBean.setRuleId(ruleId);
        
        //Add the Condition bean to HashMap
        hmData.put(BusinessRuleConditionsBean.class , ruleConditionsBean);
        
        //Add the Function Argumets to HashMap
        hmData.put(BusinessRuleFuncArgsBean.class ,
                cvRuleFuncArgs == null ? new CoeusVector() : cvRuleFuncArgs);
        
        //Add the type
        ComboBoxBean cmbType = (ComboBoxBean)defineRulesForm.cmbType.getSelectedItem();
        hmData.put(ComboBoxBean.class , cmbType);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        String moduleCode = "0";
        if((ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem()!=null){
            moduleCode = ((ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem()).getCode();
        }
        hmData.put("moduleCode", moduleCode);
        String subModuleCode = "0";
        if((ComboBoxBean)defineRulesForm.cmbSubmodule.getSelectedItem()!=null &&
                !((ComboBoxBean)defineRulesForm.cmbSubmodule.getSelectedItem()).getCode().equals("")){
            subModuleCode = ((ComboBoxBean)defineRulesForm.cmbSubmodule.getSelectedItem()).getCode();
        }
        hmData.put("subModuleCode", subModuleCode);
        hmData.put(QuestionsMaintainanceBean.class, hmQuestionnaireQuestions);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        ConditionEditorController conditionEditorController =
                new ConditionEditorController(ruleBaseBean, functionType, actionMode);
        
        conditionEditorController.setFormData(hmData);
        
        //Set the no of cndition present , this is used for validation in
        //Condition editor window
        int noOfConditions = 0;
        boolean elsePresent = false;
        if(cvRuleCond != null){
            noOfConditions = cvRuleCond.size();
            Equals eqNull = new Equals("conditionExp" , null);
            Equals eqEmpty = new Equals("conditionExp" , EMPTY_STRING);
            Or eqNullOReqEmpty = new Or(eqNull , eqEmpty);
            
            CoeusVector cvFilterData = cvRuleCond.filter(eqNullOReqEmpty);
            if(cvFilterData != null && cvFilterData.size() > 0){
                elsePresent= true;
            }
        }
        conditionEditorController.setNoOfCondtionsPresent(noOfConditions);
        conditionEditorController.setElsePresent(elsePresent);
        defineRulesForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        conditionEditorController.display();
        
        //If OK is clicked in Condtion Editor , get the data for save
        if(conditionEditorController.isOkClicked()){
            hmData = (HashMap)conditionEditorController.getFormData();
            refresh(hmData , actionMode);
            //Modified for case# 2785 - Routing enhancement - start
            //If data was changed in define rule form and data not changed in 
            //condition editor form, it should not overwrite the saveRequired
            // to false
            if(!isSaveRequired()){
                setSaveRequired(conditionEditorController.isModified());
            }
            //Modified for case# 2785 - Routing enhancement - end
        }
        conditionEditorController.cleanUp();
    }
    
    /*Refreshes the screen , updates the bean with the condtion no ,
     *sets the expression nos.
     */
    private void refresh(HashMap hmData , char actionMode){
        HashMap hmCondData = hmData;
        BusinessRuleConditionsBean ruleConditionsBean =
                (BusinessRuleConditionsBean)hmCondData.get(BusinessRuleConditionsBean.class);
        
        CoeusVector cvExpData = (CoeusVector)hmCondData.get(BusinessRuleExpBean.class);
        
        this.cvDeletedExpData = (CoeusVector)hmCondData.get(CoeusVector.class);
        
        if(actionMode == TypeConstants.MODIFY_MODE){
            int selRow = defineRulesForm.tblConditions.getSelectedRow();
            if(selRow != -1){
                ruleConditionsBean = (BusinessRuleConditionsBean)cvRuleCond.get(selRow);
            }
            
            String expression = buildExpression(cvExpData);
            ruleConditionsBean.setConditionExp(expression);
            
            if(ruleConditionsBean.getAcType() == null){
                ruleConditionsBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            
            updateConditionBean(cvExpData , ruleConditionsBean);
            
            NotEquals neCondNo = new NotEquals("conditionNumber" , new Integer(ruleConditionsBean.getConditionNumber()));
            cvRuleExp = cvRuleExp.filter(neCondNo);
            cvRuleExp.addAll(cvExpData);
        }else if(actionMode == TypeConstants.ADD_MODE){
            //int maxCondNo = getMaxCondNo();
            int maxSeqNo = getMaxSeqNo();
            
            //ruleConditionsBean.setConditionNumber(maxCondNo + 1);
            ruleConditionsBean.setConditionSequence(maxSeqNo + 1);
            ruleConditionsBean.setAcType(TypeConstants.INSERT_RECORD);
            
            updateConditionBean(cvExpData , ruleConditionsBean);
            
            String expression = buildExpression(cvExpData);
            ruleConditionsBean.setConditionExp(expression);
            
            if(cvRuleCond == null){
                cvRuleCond = new CoeusVector();
                
            }
            cvRuleCond.add(ruleConditionsBean);
            
            if(cvRuleExp == null){
                cvRuleExp = new CoeusVector();
            }
            cvRuleExp.addAll(cvExpData);
            defineRulesTableModel.fireTableDataChanged();
            
        }else if(actionMode == INSERT_ROW){
            //int maxCondNo = getMaxCondNo();
            
            int selRow = defineRulesForm.tblConditions.getSelectedRow();
            if(selRow == -1){
                selRow = 0;
            }
            
            ruleConditionsBean.setRuleId(ruleId);
            
            //ruleConditionsBean.setConditionNumber(maxCondNo + 1);
            ruleConditionsBean.setAcType(TypeConstants.INSERT_RECORD);
            
            updateConditionBean(cvExpData , ruleConditionsBean);
            
            
            if(cvRuleCond == null){
                cvRuleCond = new CoeusVector();
                cvRuleCond.insertElementAt(ruleConditionsBean , 0);
                defineRulesForm.tblConditions.setRowSelectionInterval(0,0);
            }else{
                cvRuleCond.insertElementAt(ruleConditionsBean , selRow);
                defineRulesForm.tblConditions.setRowSelectionInterval(selRow , selRow);
            }
            
            String expression = buildExpression(cvExpData);
            ruleConditionsBean.setConditionExp(expression);
            
            /*for(int index = 0 ; index < cvRuleCond.size() ; index++){
                BusinessRuleConditionsBean businessRuleConditionsBean =
                            (BusinessRuleConditionsBean)cvRuleCond.get(index);
                businessRuleConditionsBean.setConditionSequence(index+1);
                if(businessRuleConditionsBean.getAcType() == null){
                    businessRuleConditionsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }//End of for*/
            setCondtionSequenceNo();
            
            if(cvRuleExp == null){
                cvRuleExp = new CoeusVector();
            }
            cvRuleExp.addAll(cvExpData);
            
            defineRulesTableModel.fireTableDataChanged();
        }//End of else if
        
        //Update the  func args with the condition no
        this.cvRuleFuncArgs = (CoeusVector)hmCondData.get(BusinessRuleFuncArgsBean.class);
        updateFuncArgBean(cvRuleFuncArgs , ruleConditionsBean);
        
        //To Enable/Disable components
        enableDisableComponents(actionMode);
        
    }
    
    private void setCondtionSequenceNo(){
        for(int index = 0 ; index < cvRuleCond.size() ; index++){
            BusinessRuleConditionsBean businessRuleConditionsBean =
                    (BusinessRuleConditionsBean)cvRuleCond.get(index);
            businessRuleConditionsBean.setConditionSequence(index+1);
            if(businessRuleConditionsBean.getAcType() == null){
                businessRuleConditionsBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }//End of for
    }//End setCondtionSequenceNo
    
    private void updateConditionBean(CoeusVector cvData , BusinessRuleConditionsBean ruleConditionsBean){
        for(int index = 0 ; index < cvData.size(); index++){
            BusinessRuleExpBean ruleExpBean = (BusinessRuleExpBean)cvData.get(index);
            ruleExpBean.setConditionNumber(ruleConditionsBean.getConditionNumber());
        }//End of for
    }//End of updateConditionBean
    
    private void updateFuncArgBean(CoeusVector cvData , BusinessRuleConditionsBean ruleConditionsBean){
        for(int index = 0 ; index < cvData.size(); index++){
            BusinessRuleFuncArgsBean ruleFuncArgsBean =
                    (BusinessRuleFuncArgsBean)cvData.get(index);
            ruleFuncArgsBean.setRuleId(ruleId);
            //Since the codition no will not be set to the new beans check for 0
            if(ruleFuncArgsBean.getConditionNumber() == 0){
                ruleFuncArgsBean.setConditionNumber(ruleConditionsBean.getConditionNumber());
            }//End of if
        }
    }//End of updateFuncArgBean
    
    /* Method to build the expression text to be saved to OSP$BUSSINESS_RULE_CONDITIONS.CONDITION_EXP
     * The expression is build based on the data entered in the condition editor.
     */
    private String buildExpression(CoeusVector cvExpData){
        StringBuffer expression = new StringBuffer();
        int size = cvExpData.size();
        
        for(int index = 0 ; index < size ; index++){
            BusinessRuleExpBean ruleExpBean = (BusinessRuleExpBean)cvExpData.get(index);
            // Modified prefix/suffix with COEUSQA-2458-Make 'and' and 'or' work in business rules
            String prefix = ruleExpBean.getExpressionPrefix();
            String suffix = ruleExpBean.getExpressionSuffix();

            if(size > 1){
                if(prefix != null && !EMPTY_STRING.equals(prefix)){
                    expression.append(prefix);
                    expression.append(" ");
                }
                expression.append("E");
                expression.append(ruleExpBean.getExpressionNumber());
                expression.append("  ");
                if(suffix != null && !EMPTY_STRING.equals(suffix)){
                    expression.append(suffix);
                    expression.append(" ");
                }
                //Bug Fix 2077 Start
                //expression.append(ruleExpBean.getLogicalOperator());
                //expression.append(" ");
                if(index != size-1){
                    expression.append(ruleExpBean.getLogicalOperator());
                    expression.append(" ");
                }
                //Bug Fix 2077 End
                
            }else{
                if(prefix != null && !EMPTY_STRING.equals(prefix)){
                    expression.append(prefix);
                    expression.append("  ");
                }
                expression.append("E");
                expression.append(ruleExpBean.getExpressionNumber());
                //Added for case 2785 -Routing enhancement -start
                //Added the space to evaluate the rule in the pkg_routing_evaluation.fn_evaluate_expressions
                expression.append(" ");
                //Added for case 2785 -Routing enhancement -end
                if(suffix != null && !EMPTY_STRING.equals(suffix)){
                    expression.append(suffix);
                    expression.append(" ");
                }
                // COEUSQA-2458-End
            }
        }//End of for
        
        return expression.toString();
    }//End of buildExpression
    
    private void enableDisableComponents(char actionMode){
        Equals eqNull = new Equals("conditionExp" , null);
        Equals eqEmpty = new Equals("conditionExp" , EMPTY_STRING);
        Or eqNullOReqEmpty = new Or(eqNull , eqEmpty);
        
        CoeusVector cvFilter = cvRuleCond.filter(eqNullOReqEmpty);
        if(cvFilter != null && cvFilter.size() > 0){
            defineRulesForm.btnAdd.setEnabled(false);
        }else{
            defineRulesForm.btnAdd.setEnabled(true);
        }
        
        if(actionMode != TypeConstants.MODIFY_MODE){
            defineRulesForm.cmbType.setEnabled(false);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            defineRulesForm.cmbModule.setEnabled(false);
            defineRulesForm.cmbSubmodule.setEnabled(false);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }
    }//End enableDisableAddButton
    
    private int getMaxCondNo(){
        int maxCondNo = 0;
        if(cvRuleCond != null && cvRuleCond.size()>0){
            CoeusVector cvSort = new CoeusVector();
            cvSort.addAll(cvRuleCond);
            cvSort.sort("conditionNumber" , false);
            
            BusinessRuleConditionsBean businessRuleConditionsBean =
                    (BusinessRuleConditionsBean)cvSort.get(0);
            maxCondNo = businessRuleConditionsBean.getConditionNumber();
        }
        return maxCondNo;
    }//End of getMaxCondNo
    
    private int getMaxSeqNo(){
        int maxSeqNo = 0;
        if(cvRuleCond != null && cvRuleCond.size()>0){
            CoeusVector cvSort = new CoeusVector();
            cvSort.addAll(cvRuleCond);
            cvSort.sort("conditionSequence" , false);
            BusinessRuleConditionsBean ruleConditionsBean
                    =(BusinessRuleConditionsBean)cvSort.get(0);
            maxSeqNo = ruleConditionsBean.getConditionSequence();
        }
        return maxSeqNo;
    }//End of getMaxSeqNo
    
    public void saveFormData() throws CoeusException {
        HashMap hmDataToServer = new HashMap();
        
        //Saving Rule bean
        hmDataToServer.put(BusinessRuleBean.class , businessRuleBean);
        
        //Saving Condition data
        if(cvDeletedCondData != null && cvDeletedCondData.size() > 0){
            cvRuleCond.addAll(cvDeletedCondData);
        }
        
        if(cvRuleCond != null && cvRuleCond.size() > 0){
            cvRuleCond = sortVector(cvRuleCond);
        }
        
        hmDataToServer.put(BusinessRuleConditionsBean.class , cvRuleCond);
        
        //Saving Expression Data
        if(cvDeletedExpData != null && cvDeletedExpData.size()>0){
            cvRuleExp.addAll(cvDeletedExpData);
        }
        //Commented for case 2418 - Business Rule Evaluation Bug - start
        //If any modifications is done in expressions, the entire data is deleted
        //and inserted again
//        if(cvRuleExp != null && cvRuleExp.size() > 0){
//            cvRuleExp = sortVector(cvRuleExp);
//        }
        //Commented for case 2418 - Business Rule Evaluation Bug - end
        
        hmDataToServer.put(BusinessRuleExpBean.class , cvRuleExp);
        
        //Saving Function Arguments data
        hmDataToServer.put(BusinessRuleFuncArgsBean.class , cvRuleFuncArgs);
        
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        
        
        request.setDataObject(hmDataToServer);
        request.setFunctionType(SAVE_RULE_CONDITIONS);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + RULES_SERVLET;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(),0);
            }
        }
    }//End of saveFormData
    
    private CoeusVector sortVector(CoeusVector cvData){
        CoeusVector sortedVector = new CoeusVector();
        Equals eqUpdate = new Equals("acType" , TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals("acType" , TypeConstants.DELETE_RECORD);
        Equals eqInsert = new Equals("acType" , TypeConstants.INSERT_RECORD);
        
        sortedVector.addAll(cvData.filter(eqUpdate));
        sortedVector.addAll(cvData.filter(eqDelete));
        sortedVector.addAll(cvData.filter(eqInsert));
        return sortedVector;
    }
    public boolean validate() throws CoeusUIException {
        String strDesc = defineRulesForm.txtArDescription.getText();
        
        if(strDesc == null || strDesc.trim().equals(EMPTY_STRING)){
            defineRulesForm.txtArDescription.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_RULE_DESC));
            return false;
        }
        
        if(cvRuleCond == null || cvRuleCond.size() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_COND));
            return false;
        }
        return true;
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
    
    
    private void prepareBeansForSave(){
        if(functionType == TypeConstants.ADD_MODE){
            businessRuleBean = new BusinessRuleBean();
            businessRuleBean.setAcType(TypeConstants.INSERT_RECORD);
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        else if(functionType == TypeConstants.MODIFY_MODE){
            if(businessRuleBean!=null){
                businessRuleBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        if(functionType == TypeConstants.ADD_MODE){
            if(!((ComboBoxBean)defineRulesForm.cmbType.getSelectedItem()).getCode().equals(ROUTING)){
                setSaveRequired(true);
            }else if((initialModuleCode==null && defineRulesForm.cmbModule.getSelectedItem()!=null) ||
                    (initialModuleCode!=null && defineRulesForm.cmbModule.getSelectedItem()==null) ||
                    ((defineRulesForm.cmbModule.getSelectedItem()!=null) && (!initialModuleCode.equals(((ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem()).getCode())))){
                setSaveRequired(true);
            }else if((initialSubModuleCode==null && defineRulesForm.cmbSubmodule.getSelectedItem()!=null) ||
                    (initialSubModuleCode!=null && defineRulesForm.cmbSubmodule.getSelectedItem()==null) ||
                    ((defineRulesForm.cmbSubmodule.getSelectedItem()!=null) && !initialSubModuleCode.equals(((ComboBoxBean)defineRulesForm.cmbSubmodule.getSelectedItem()).getCode()))){
                setSaveRequired(true);
            }else if(defineRulesForm.txtArDescription.getText().trim().length()>0){
                setSaveRequired(true);
            }else if(cvRuleCond!=null && cvRuleCond.size()>0){
                setSaveRequired(true);
            }
        }else if(functionType == TypeConstants.MODIFY_MODE){
            if(!businessRuleBean.getDescription().equals(defineRulesForm.txtArDescription.getText())){
                setSaveRequired(true);
            }
            if(businessRuleBean.getRuleType().equals(VALIDATION)){
                String errorOrWaring = (defineRulesForm.rbError.isSelected())? "E":"W";
                if(!errorOrWaring.equals(businessRuleBean.getRuleCategory())){
                    setSaveRequired(true);
                }
            }
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        businessRuleBean.setRuleId(""+ruleId);
        businessRuleBean.setUnitNumber(ruleBaseBean.getUnitNumber());
        
        ComboBoxBean  cmbBean =  (ComboBoxBean)defineRulesForm.cmbType.getSelectedItem();
        businessRuleBean.setRuleType(cmbBean.getCode());
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        cmbBean =  (ComboBoxBean)defineRulesForm.cmbModule.getSelectedItem();
        if(cmbBean!=null){
            businessRuleBean.setModuleCode(cmbBean.getCode());
        }
        
        if(defineRulesForm.cmbSubmodule.getSelectedItem()!=null &&
                ((ComboBoxBean)defineRulesForm.cmbSubmodule.getSelectedItem()).getCode()!=EMPTY_STRING){
            cmbBean =  (ComboBoxBean)defineRulesForm.cmbSubmodule.getSelectedItem();
            businessRuleBean.setSubmoduleCode(cmbBean.getCode());
        }else{
            businessRuleBean.setSubmoduleCode("0");
        }
        if(businessRuleBean.getRuleType().equals(VALIDATION)){
            businessRuleBean.setRuleCategory((defineRulesForm.rbError.isSelected())? "E":"W");
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        String strDesc = defineRulesForm.txtArDescription.getText().trim();
        
        if(businessRuleBean.getDescription() == null){
            businessRuleBean.setDescription(strDesc);
            if(businessRuleBean.getAcType() == null){
                businessRuleBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            //Commented for case 2785 - Routing enhancement
            //saveRequired value is set in the code above
            //    setSaveRequired(true);
        }else if(!businessRuleBean.getDescription().trim().equals(strDesc)){
            businessRuleBean.setDescription(strDesc);
            if(businessRuleBean.getAcType() == null){
                businessRuleBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            //Commented for case 2785 - Routing enhancement
            //saveRequired value is set in the code above
//            setSaveRequired(true);
        }
    }
    
    
    /*Table model for Define Rules*/
    public class DefineRulesTableModel extends AbstractTableModel{
        //column names
        private String ColumnName[] = {"","","Conditions","",""};
        private Class colClass[] = {String.class, String.class,String.class,String.class,String.class};
        
        public int getColumnCount() {
            return ColumnName.length;
        }
        
        public Class getColumnClass(int columnIndex){
            return colClass[columnIndex];
        }
        
        public int getRowCount() {
            if (cvRuleCond == null){
                return 0;
            }else
                return cvRuleCond.size();
        }
        
        public void setData(CoeusVector cvData){
            cvRuleCond = cvData;
        }
        
        public String getColumnName(int col) {
            return ColumnName[col];
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            BusinessRuleConditionsBean businessRuleConditionsBean
                    = (BusinessRuleConditionsBean)cvRuleCond.get(rowIndex);
            
            switch(columnIndex){
                case DESCRIPTION_COLUMN:
                    return businessRuleConditionsBean.getRuleDescription();
                    
                case MAP_COLUMN:
                    ComboBoxBean cmbBean =
                            (ComboBoxBean)defineRulesForm.cmbType.getSelectedItem();
                    if(cmbBean.getCode().equalsIgnoreCase("V")){
                        
                        if(businessRuleConditionsBean.getAction()== 1){
                            return "Valid";
                        }else if(businessRuleConditionsBean.getAction() == -1){
                            return "Invalid";
                        }
                    }else{
                        return businessRuleConditionsBean.getMapDescription();
                    }
            }
            return EMPTY_STRING;
        }
        
        /**
         * Returns true if the cell at <code>rowIndex</code> and
         * <code>columnIndex</code>
         * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
         * change the value of that cell.
         *
         * @param	rowIndex	the row whose value to be queried
         * @param	columnIndex	the column whose value to be queried
         * @return	true if the cell is editable
         * @see #setValueAt
         */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
    }//End Class Table Model
    
    
    /*Table cell renderer for Define Rules*/
    class DefineRulesRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        private CoeusTextField txtComponent;
        private JLabel lblText;
        
        public DefineRulesRenderer(){
            txtComponent = new CoeusTextField();
            lblText = new JLabel();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblText.setOpaque(true);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
            switch(col) {
                case DUMMY_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                        if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.blue);
                    }
                    
                    lblText.setText(EMPTY_STRING);
                    return lblText;
                    
                case CONDITION_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                        if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.blue);
                    }
                    
                    lblText.setText(getTextForRenderer(row));
                    return lblText;
                    
                case DESCRIPTION_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                        if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    
                    return lblText;
                    
                case USE_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                        if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.blue);
                    }
                    
                    ComboBoxBean cmbTypeBean =
                            (ComboBoxBean)defineRulesForm.cmbType.getSelectedItem();
                    
                    if(cmbTypeBean.getCode().trim().equalsIgnoreCase(("V"))){
                        lblText.setText("proposal is");
                    }else{
                        lblText.setText("use");
                    }
                    return lblText;
                    
                case MAP_COLUMN:
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                        if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    cmbTypeBean =
                            (ComboBoxBean)defineRulesForm.cmbType.getSelectedItem();
                    
                   if(cmbTypeBean.getCode().trim().equalsIgnoreCase(QUESTION)){
                        lblText.setText(coeusMessageResources.parseMessageKey(QUESTION_MESSAGE));
                    }else{
                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                            txtComponent.setText(EMPTY_STRING);
                            lblText.setText(txtComponent.getText());
                        }else{
                            txtComponent.setText(value.toString());
                            lblText.setText(txtComponent.getText());
                        }
                    }
                    return lblText;
            }//End of switch
            return txtComponent;
        }//End of getTableCellRendererComponent
        
        private String getTextForRenderer(int row){
            String strCondition = "Else if";
            
            if(cvRuleCond != null && cvRuleCond.size() > 0){
                if( row == 0 ){
                    strCondition = "If";
                }else{
                    BusinessRuleConditionsBean businessRuleConditionsBean  =
                            (BusinessRuleConditionsBean)cvRuleCond.get(row);
                    
                    String strCondExp = businessRuleConditionsBean.getConditionExp();
                    
                    if(strCondExp == null ||
                            strCondExp.trim().equals(EMPTY_STRING)){
                        strCondition = "Else";
                    }//End of inner if
                }//End of else
            }//End of outer if
            
            return strCondition;
            
        }//End of getTextForRenderer
        
    }//End DefineRulesRenderer
    
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    /*
     * Check if module id provided is either IRB protocol or IACUC protocol
     * @param module id
     * @return true if module is IACUC protocol or IRB protocol
     */
    private boolean checkModule(String moduleId ){
        boolean isValid = false;
        // Enabled submodule combobox for module developmet proposal and type question.
        if((moduleId.equals(""+ModuleConstants.PROTOCOL_MODULE_CODE) ||
                             moduleId.equals(""+ModuleConstants.IACUC_MODULE_CODE))){
            isValid = true;
        }
        return isValid;
    }
    // COEUSQA-1724_ Implement validation based on rules in protocols_End
    
    public void cleanUp(){
        ruleBaseBean = null;
        businessRuleBean = null;
        
        cvMasterData = null;
        cvRuleDetails = null;
        cvRuleCond = null;
        cvRuleExp = null;
        cvRuleFuncArgs = null;
        cvDeletedExpData = null;
        cvDeletedCondData = null;
        
        defineRulesTableModel = null;
        defineRulesRenderer = null;
        
        defineRulesForm = null;
        //dlgDefineRulesForm = null;
    }//End cleanUp
}//End of DefineRulesController
