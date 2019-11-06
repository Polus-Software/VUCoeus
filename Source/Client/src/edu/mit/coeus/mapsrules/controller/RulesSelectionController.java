/*
 * @(#)RulesSelectionController.java 1.0 10/17/05 11:54 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-AUG-2007
 * by Leena
 */
package edu.mit.coeus.mapsrules.controller;

//import edu.mit.coeus.brokers.RequesterBean;
//import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.gui.RulesSelectionForm;
//import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
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
public class RulesSelectionController extends RuleController
        implements ActionListener{
    
    /** Holds an instance of <CODE>ApprEquipmentForm</CODE> */
    private RulesSelectionForm rulesSelectionForm;
    
    /**
     * To create an instance of MDIform
     */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of the Dialog
     */
    private CoeusDlgWindow dlgRulesSelectionForm;
    
    /**
     * Instance of Query Engine
     */
    private QueryEngine queryEngine;
    
    /**
     * Instance of Coeus Message Resources
     */
    private CoeusMessageResources coeusMessageResources;
    
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
    
    /*Table Model and Renderer for Rule Selection table */
    private RulesSelectionTableModel rulesSelectionTableModel;
    private RulesSelectionRenderer rulesSelectionRenderer;
    
    private char functionType;
    
    /*For setting the dimentions*/
    private static final String WINDOW_TITLE = "Rules Selection Criteria";
    private static final int WIDTH = 567;
    private static final int HEIGHT = 378;
    
    //private static final char GET_RULES_SELECTION_DATA = 'B';
    //private static final String RULES_SERVLET = "/RuleMaintenanceServlet";
    
    private CoeusVector cvRulesSelectionData;
    private CoeusVector cvMasterData;
    private CoeusVector cvTreeData;
    private RuleBaseBean ruleBaseBean = new RuleBaseBean();
    
    // The bean contains only the parent/ sel node data
    private MetaRuleDetailBean selectedNodeBean;
    
    // This the Rule Type which is passed from the parent window
    private String ruleType;
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    private String moduleCode;
    private String subModuleCode;
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    private char actionMode;
    
    //This is to check whether ok is clicked or not
    private boolean isOkClicked;
    
    private String selectedType;
    
    //Error Messages
    private static final String SELECT_RULE_CRITERIA="rulesSelection_exceptionCode.1001";
    private static final String RULE_ALREADY_PRESENT="rulesSelection_exceptionCode.1002";
    private static final String TRUE_OR_FALSE_PRESENT="rulesSelection_exceptionCode.1003";
    
    /** Creates a new instance of RulesSelectionController */
    public RulesSelectionController(RuleBaseBean ruleBaseBean, char functionType,char actionMode) {
        super(ruleBaseBean);
        this.ruleBaseBean = ruleBaseBean;
        this.actionMode = actionMode;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.functionType = functionType;
        registerComponents();
        formatFields();
        postInitComponents();
    }
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    public RulesSelectionController() {
        registerComponents();
        formatFields();
        postInitComponents();
    }
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgRulesSelectionForm = new CoeusDlgWindow(mdiForm);
        dlgRulesSelectionForm.setResizable(false);
        dlgRulesSelectionForm.setModal(true);
        dlgRulesSelectionForm.getContentPane().add(rulesSelectionForm);
        dlgRulesSelectionForm.setTitle(WINDOW_TITLE);
        dlgRulesSelectionForm.setFont(CoeusFontFactory.getLabelFont());
        dlgRulesSelectionForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRulesSelectionForm.getSize();
        dlgRulesSelectionForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgRulesSelectionForm.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        dlgRulesSelectionForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
        
        dlgRulesSelectionForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRulesSelectionForm.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        //code for disposing the window ends
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        dlgRulesSelectionForm.setVisible(true);
    }
    
    
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        
    }
    
    /** An overridden method of the controller
     * @return apprEquipmentForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return rulesSelectionForm;
        
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        int selRow = rulesSelectionForm.tblRules.getSelectedRow();
        CoeusVector cvData = new CoeusVector();
        if(selRow!=-1){
            BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvRulesSelectionData.get(selRow);
            String ruleCriteria  = EMPTY_STRING;
            if(actionMode == TypeConstants.MODIFY_MODE && getSelectedNodeBean().getNodeId().equals("1") ){
                ruleCriteria = EMPTY_STRING;
                cvData.addElement(ruleCriteria);
                cvData.addElement(businessRuleBean);
                return cvData;
            }
            if(rulesSelectionForm.rdBtnNext.isSelected()){
                ruleCriteria = "N";
            }else if(rulesSelectionForm.rdBtnIfFalse.isSelected()){
                ruleCriteria = "F";
            }else if(rulesSelectionForm.rdBtnIfTrue.isSelected()){
                ruleCriteria = "T";
            }
            cvData.addElement(ruleCriteria);
            cvData.addElement(businessRuleBean);
        }
        return cvData;
    }
    
    
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        rulesSelectionForm = new RulesSelectionForm();
        rulesSelectionForm.btnOK.addActionListener(this);
        rulesSelectionForm.btnCancel.addActionListener(this);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { rulesSelectionForm.rdBtnNext,rulesSelectionForm.rdBtnIfTrue,
        rulesSelectionForm.rdBtnIfFalse, rulesSelectionForm.btnOK,rulesSelectionForm.btnCancel};
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        rulesSelectionForm.setFocusTraversalPolicy(traversePolicy);
        rulesSelectionForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
    }
    
    /** To set the default focus for the component
     */
    public void requestDefaultFocus(){
        rulesSelectionForm.btnOK.requestFocusInWindow();
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(rulesSelectionForm.btnOK)){
                performOKAction();
            }else if(source.equals(rulesSelectionForm.btnCancel)){
                performCancelAction();
            }
        }catch (CoeusUIException cuiEx){
            cuiEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cuiEx.getMessage());
        }catch (CoeusException cEx){
            cEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cEx.getMessage());
        }
    }
    
    /*Sets the tabe renderers and width for each column of the table*/
    private void setTableRenderer(){
        try{
            JTableHeader tableHeader = rulesSelectionForm.tblRules.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            rulesSelectionForm.tblRules.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            rulesSelectionForm.tblRules.setRowHeight(22);
            rulesSelectionForm.tblRules.setSelectionBackground(java.awt.Color.yellow);
            rulesSelectionForm.tblRules.setSelectionForeground(java.awt.Color.white);
            rulesSelectionForm.tblRules.setShowHorizontalLines(true);
            rulesSelectionForm.tblRules.setShowVerticalLines(true);
            rulesSelectionForm.tblRules.setOpaque(false);
            
            rulesSelectionForm.tblRules.setSelectionMode(
                    DefaultListSelectionModel.SINGLE_SELECTION);
            
            
            TableColumn  column = rulesSelectionForm.tblRules.getColumnModel().getColumn(0);
            column.setPreferredWidth(460);
            column.setCellRenderer(rulesSelectionRenderer);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//End setTableEditors
    
    private void performOKAction() throws CoeusUIException, CoeusException{
        int selRow = rulesSelectionForm.tblRules.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog("Please select a row");
            return ;
        }
        setIsOkClicked(true);
        
        if(validate()){
            dlgRulesSelectionForm.dispose();
        }
        
        
    }
    
    private void performCancelAction(){
        setIsOkClicked(false);
        dlgRulesSelectionForm.dispose();
    }
    
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) throws CoeusException {
        if(data != null){
            cvTreeData = (CoeusVector)data;
            
            /**If the selected node in the tree is equal to 1 and if
             *it is modified in MODIFY_MODE all the
             *radio buttons are disabled
             */
            if(getSelectedNodeBean().getNodeId().equals("1") && actionMode == TypeConstants.MODIFY_MODE ){
                rulesSelectionForm.rdBtnIfFalse.setEnabled(false);
                rulesSelectionForm.rdBtnIfTrue.setEnabled(false);
                rulesSelectionForm.rdBtnNext.setEnabled(false);
            }//End of If
        }
        /**If there is no node in the tree
         *disable all the radio buttons
         */
        else{
            rulesSelectionForm.rdBtnIfFalse.setEnabled(false);
            rulesSelectionForm.rdBtnIfTrue.setEnabled(false);
            rulesSelectionForm.rdBtnNext.setEnabled(false);
            cvTreeData = new CoeusVector();
        }
        
        
        if(actionMode == TypeConstants.MODIFY_MODE){
            if(getSelectedType()!= null){
                if(getSelectedType().equals("next")){
                    rulesSelectionForm.rdBtnNext.setSelected(true);
                }else if(getSelectedType().equals("false")){
                    rulesSelectionForm.rdBtnIfFalse.setSelected(true);
                }else if(getSelectedType().equals("true")){
                    rulesSelectionForm.rdBtnIfTrue.setSelected(true);
                }
            }else{
                rulesSelectionForm.rdBtnIfTrue.setSelected(true);
            }
        }//End ofif
        
        cvMasterData = new CoeusVector();
        cvRulesSelectionData = new CoeusVector();
        
        rulesSelectionTableModel = new RulesSelectionTableModel();
        rulesSelectionRenderer = new RulesSelectionRenderer();
        rulesSelectionForm.tblRules.setModel(rulesSelectionTableModel);
        
        setTableRenderer();
        
        /*For getting rules*/
        cvMasterData = queryEngine.getDetails(queryKey,BusinessRuleBean.class);
        if(cvMasterData != null && cvMasterData .size()>0){
            Equals eqRuleType = new Equals("ruleType" , getRuleType());
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //Filter cvMasterData using the ruleType and moduleId
            Equals eqModuleId = new Equals("moduleCode", getModuleCode());
            Equals eqSubModuleId = new Equals("submoduleCode", getSubModuleCode());
            System.out.println("inside the rulecontroller::"+getRuleType()+getModuleCode()+getSubModuleCode());
            And andTypeModuleId = new And(eqRuleType, new And(eqModuleId, eqSubModuleId));
            //cvRulesSelectionData =  cvMasterData.filter(eqRuleType);
            cvRulesSelectionData =  cvMasterData.filter(andTypeModuleId);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            if(cvRulesSelectionData != null && cvRulesSelectionData.size()>0){
                rulesSelectionTableModel.setData(cvRulesSelectionData);
                if(getRuleType().equals("V")){
                    rulesSelectionForm.rdBtnIfTrue.setEnabled(false);
                    rulesSelectionForm.rdBtnIfFalse.setEnabled(false);
                }//End of innermostIf
            }//End of inner if
            /** Set the Row Selection based on the rule Id selected from the parent
             *Set the current row which was selected rule Id. this has to be selected
             *based on the actionMode
             */
            if(actionMode== TypeConstants.MODIFY_MODE){
                if(getSelectedNodeBean()!= null ){
                    String ruleId = getSelectedNodeBean().getRuleId();
//                    int count = 0;
                    if(cvRulesSelectionData!= null && cvRulesSelectionData.size() > 0){
                        for(int index=0; index < cvRulesSelectionData.size(); index++){
                            BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvRulesSelectionData.get(index);
                            if(ruleId.equals(businessRuleBean.getRuleId())){
                                rulesSelectionForm.tblRules.setRowSelectionInterval(index,index);
                            }// End If
                        }// End For
                    }// End If
                }// End If
            }else{
                if(rulesSelectionForm.tblRules.getRowCount() > 0){
                    rulesSelectionForm.tblRules.setRowSelectionInterval(0,0);
                }
            }
        }//End of outer if*/
        
        
    }//End of setFormData
    
    
    /** Saves the Form Data.
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        
    }
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
        MetaRuleDetailBean metaRuleDetailBean = getSelectedNodeBean();
        
        if(metaRuleDetailBean == null){
            return true;
        }else{
            
            CoeusVector cvParentNode = null;
            CoeusVector cvParentChildNodes = null;
            CoeusVector cvChildrenNodes = null;
            //To get the parent of the selected node
            Equals eqParentNode = new Equals("nodeId" , metaRuleDetailBean.getParentNodeId());
            cvParentNode = cvTreeData.filter(eqParentNode);
            
            
            //To get all the children of selected nodes's parent.
            Equals eqParentChildNodes = new Equals("parentNodeId" , metaRuleDetailBean.getParentNodeId());
            cvParentChildNodes = cvTreeData.filter(eqParentChildNodes);
            
            //To get all the children of selected node.
            Equals eqChildrenNodes = new Equals("parentNodeId" , metaRuleDetailBean.getNodeId());
            cvChildrenNodes = cvTreeData.filter(eqChildrenNodes);
            
            if(cvTreeData !=null  && cvTreeData.size() >=1){
                if(!rulesSelectionForm.rdBtnNext.isSelected()
                && !rulesSelectionForm.rdBtnIfTrue.isSelected()
                && !rulesSelectionForm.rdBtnIfFalse.isSelected()){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_RULE_CRITERIA));
                    return false;
                }//End of inner if
            }
            
            CoeusVector cvFilterData = new CoeusVector();
            
            if(rulesSelectionForm.rdBtnIfFalse.isSelected()){
                Equals eqIfFalse = new Equals("nodeId" , metaRuleDetailBean.getNodeIfFalse());
                if(actionMode == TypeConstants.MODIFY_MODE){
                    if(cvParentNode != null && cvParentNode.size()>0){
                        MetaRuleDetailBean ruleDetailBean = (MetaRuleDetailBean)cvParentNode.get(0);
                        String strNodeIfFalse = ruleDetailBean.getNodeIfFalse();
                        if(strNodeIfFalse!= null && strNodeIfFalse.equals(EMPTY_STRING)){
                            strNodeIfFalse =null;
                        }
                        if(strNodeIfFalse!= null && !strNodeIfFalse.equals(metaRuleDetailBean.getNodeId())){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RULE_ALREADY_PRESENT));
                            return false;
                        }
                    }
                    
                }else{
                    if(cvChildrenNodes != null && cvChildrenNodes.size()>0){
                        cvFilterData = cvChildrenNodes.filter(eqIfFalse);
                        if(cvFilterData != null && cvFilterData.size()>0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RULE_ALREADY_PRESENT));
                            return false;
                        }//End of  if(cvFilterData ......
                    }
                }//End of else
            }//End of outer if
            
            if(rulesSelectionForm.rdBtnIfTrue.isSelected()){
                Equals eqIfTrue = new Equals("nodeId" , metaRuleDetailBean.getNodeIfTrue());
//                Equals neIfTrue = new Equals("nodeIfTrue",metaRuleDetailBean.getNodeId());
                //And eqIfTrueAndeqIfTrue  = new And(neIfTrue , eqIfTrue);
                if(actionMode == TypeConstants.MODIFY_MODE){
                    if(cvParentNode !=null && cvParentNode.size() >0){
                        MetaRuleDetailBean ruleDetailBean = (MetaRuleDetailBean)cvParentNode.get(0);
                        String strBeanNodeId = metaRuleDetailBean.getNodeId();
                        String strNodeIfTrue = ruleDetailBean.getNodeIfTrue();
                        if(strNodeIfTrue!=null && strNodeIfTrue.equals(EMPTY_STRING)){
                            strNodeIfTrue = null;
                        }
                        if(strNodeIfTrue!= null && strBeanNodeId!= null && !strNodeIfTrue.equals(strBeanNodeId)){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RULE_ALREADY_PRESENT));
                            return false;
                        }
                    }
                    
                }else{
                    if(cvChildrenNodes != null && cvChildrenNodes.size()>0){
                        cvFilterData = cvChildrenNodes.filter(eqIfTrue);
                        
                        if(cvFilterData != null && cvFilterData.size()>0){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RULE_ALREADY_PRESENT));
                            return false;
                        }//End of  if(cvFilterData ......
                    }
                }//End of else
            }//End of outer if
            
            if(!ruleType.equalsIgnoreCase(VALIDATION)){
                if(rulesSelectionForm.rdBtnNext.isSelected()){
//                    Equals eqNext = new Equals("nodeId" , metaRuleDetailBean.getNextNode());
                    Equals eqIfFalse = new Equals("nodeId" , metaRuleDetailBean.getNodeIfFalse());
                    Equals eqIfTrue = new Equals("nodeId" , metaRuleDetailBean.getNodeIfTrue());
//                    And eqIfFalseAndeqIfTrue = new And(eqIfFalse ,eqIfTrue);
                    Or  eqIfFalseOreqIfTrue = new Or(eqIfFalse ,eqIfTrue);
                    
                    if(actionMode == TypeConstants.MODIFY_MODE){
                        if(cvParentNode != null && cvParentNode.size()>0){
                            for(int i=0; i<cvParentNode.size();i++){
                                MetaRuleDetailBean  ruleDetailBean = (MetaRuleDetailBean)cvParentNode.get(i);
                                String trueNode = ruleDetailBean.getNodeIfTrue();
                                String falseNode = ruleDetailBean.getNodeIfFalse();
                                if(trueNode!=null && trueNode.equals(EMPTY_STRING)){
                                    trueNode = null;
                                }
                                if(falseNode!=null && falseNode.equals(EMPTY_STRING)){
                                    falseNode =null;
                                }
                                if(trueNode != null && falseNode!=null){
                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(TRUE_OR_FALSE_PRESENT));
                                    return false;
                                }
                            }
                            
                        }//End if(cvParentChildNodes
                    }else{
                        if(cvChildrenNodes != null && cvChildrenNodes.size()>0){
                            cvFilterData = cvChildrenNodes.filter(eqIfFalseOreqIfTrue);
                            
                            if(cvFilterData != null && cvFilterData.size()>0){
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(TRUE_OR_FALSE_PRESENT));
                                return false;
                            }//End of  if(cvFilterData ......
                        }
                    }//End of else
                }
            }//End of outer if
        }
        return true;
    }
    
    
    /**
     * Getter for property ruleType.
     * @return Value of property ruleType.
     */
    public java.lang.String getRuleType() {
        return ruleType;
    }
    
    /**
     * Setter for property ruleType.
     * @param ruleType New value of property ruleType.
     */
    public void setRuleType(java.lang.String ruleType) {
        this.ruleType = ruleType;
    }
    
    /*Table model for Rules Selection Criteria*/
    public class RulesSelectionTableModel extends AbstractTableModel{
        CoeusVector cvData = null;
        //column names
        
        private String columnName = getRuleTypeDescription()+ " "+ "Rules";
        
        private Class colClass[] = {String.class};
        
        public int getColumnCount() {
            return 1;
        }
        public Class getColumnClass(int columnIndex){
            return colClass[columnIndex];
        }
        public int getRowCount() {
            if (cvData == null){
                return 0;
            }else{
                return cvData.size();
            }
        }
        public void setData(CoeusVector cvData){
            this.cvData = cvData;
        }
        public String getColumnName(int col) {
            return columnName;
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            BusinessRuleBean businessRuleBean= (BusinessRuleBean)cvData.get(rowIndex);
            return businessRuleBean.getDescription();
        }
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }//End of RulesSelcetionTableModel
    
    
    /*Table cell renderer for Rules Selection Criteria*/
    class RulesSelectionRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        private CoeusTextField txtComponent;
        private JLabel lblText;
        
        public RulesSelectionRenderer(){
            txtComponent = new CoeusTextField();
            lblText = new JLabel();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblText.setOpaque(true);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
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
            if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                txtComponent.setText(EMPTY_STRING);
                lblText.setText(txtComponent.getText());
            }else{
                txtComponent.setText(value.toString());
                lblText.setText(txtComponent.getText());
            }
            return lblText;
        }
    }//End RulesSelectionRenderer
    
    private String getRuleTypeDescription(){
        String strRuleTypeDesc = "";
        if(getRuleType().trim().equalsIgnoreCase("R")){
            strRuleTypeDesc = "Routing";
        }else if(getRuleType().trim().equalsIgnoreCase("V")){
            strRuleTypeDesc = "Validation";
        }else if(getRuleType().trim().equalsIgnoreCase("N")){
            strRuleTypeDesc = "Notification";
        }else if(getRuleType().trim().equalsIgnoreCase("Q")){
            strRuleTypeDesc = "Question";
        }
        return strRuleTypeDesc;
    }//End of getRuleTypeDescription
    
    /**
     * Getter for property selectedNodeBean.
     * @return Value of property selectedNodeBean.
     */
    public edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean getSelectedNodeBean() {
        return selectedNodeBean;
    }
    
    /**
     * Setter for property selectedNodeBean.
     * @param selectedNodeBean New value of property selectedNodeBean.
     */
    public void setSelectedNodeBean(edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean selectedNodeBean) {
        this.selectedNodeBean = selectedNodeBean;
    }
    
    /**
     * Getter for property isOkClicked.
     * @return Value of property isOkClicked.
     */
    public boolean isIsOkClicked() {
        return isOkClicked;
    }
    
    /**
     * Setter for property isOkClicked.
     * @param isOkClicked New value of property isOkClicked.
     */
    public void setIsOkClicked(boolean isOkClicked) {
        this.isOkClicked = isOkClicked;
    }
    
    /**
     * Getter for property selectedType.
     * @return Value of property selectedType.
     */
    public java.lang.String getSelectedType() {
        return selectedType;
    }
    
    /**
     * Setter for property selectedType.
     * @param selectedType New value of property selectedType.
     */
    public void setSelectedType(java.lang.String selectedType) {
        this.selectedType = selectedType;
    }
    
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    /**
     * Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public String getModuleCode() {
        return moduleCode;
    }
    
    /**
     * Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     */
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /**
     * Getter for property subModuleCode.
     * @return Value of property subModuleCode.
     */
    public String getSubModuleCode() {
        return subModuleCode;
    }
    /**
     * Setter for property subModuleCode.
     * @param subModuleCode New value of property subModuleCode.
     */
    public void setSubModuleCode(String subModuleCode) {
        this.subModuleCode = subModuleCode;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /**
     * Method to set form data for question
     * @param cvRuleData 
     */
    public void setFormDataForQuestion(CoeusVector cvRuleData){
        setRuleType("Q");
        rulesSelectionForm.pnlRulesCriteria.setVisible(false);
        rulesSelectionTableModel = new RulesSelectionTableModel();
        rulesSelectionRenderer = new RulesSelectionRenderer();
        rulesSelectionForm.tblRules.setModel(rulesSelectionTableModel);
        setTableRenderer();
        if(cvRuleData != null && cvRuleData.size()>0){
            cvRulesSelectionData = cvRuleData;
            rulesSelectionTableModel.setData(cvRuleData);
        }
    }
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
}//End RulesSelectionController
