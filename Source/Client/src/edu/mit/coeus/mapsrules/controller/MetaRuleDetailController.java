/*
 * @(#)MetaRuleMaintainanceController.java 1.0 10/17/05 10:46 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 04-Feb-2011
 * by Leena
 */
package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.MetaRuleBean;

import edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.gui.MetaRuleDetailForm;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
//import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
//import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
//import javax.swing.JLabel;
import javax.swing.JTree;

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
public class MetaRuleDetailController extends RuleController implements ActionListener,ItemListener {
    private MetaRuleDetailForm metaRuleDetailForm;
    private CoeusVector cvRuleData;
    private DefaultTreeModel ruleTreeModel;
//    private MetaRuleNode rootNode;
    private JTree ruleTree;
    
    private CoeusVector cvDescriptionData;
    private char functionType;
    private QueryEngine queryEngine;
    private RuleBaseBean ruleBaseBean;
    private CoeusVector cvMetaMasterData;
//    private DefaultMutableTreeNode root;
    private MetaRuleTreeRenderer metaRuleTreeRenderer;
    private java.awt.Color backGroundColor;
    // holds the selected node information.
    private MetaRuleNode selectedNode;
    //holds current tree selection path
    private TreePath selTreePath;
    private CoeusVector cvDeletedData;
    private boolean modified;
    private static final String RULE_SERVLET = "/RuleMaintenanceServlet";
    private static final char GET_META_RULE_ID= 'F';
    private String unitNumber;
    private boolean addRuleRight;
    private boolean modifyRuleRight;
    private boolean deleteRuleRight;
    private Vector rightsData;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
    private String selectedType;
    int nodeCount;
    private boolean loadFirstTime = true;
    private boolean createMetaRule = true;
    private boolean refreshRequired;
    private static final String CONFIRM_DELETE= "metaruleDetail_exceptionCode.1151";
    private static final String CHILD_NODE_EXISTS ="metaruleDetail_exceptionCode.1152";
    private static final String SELECT_NODE_DELETE ="metaruleDetail_exceptionCode.1153";
    private static final String MAXIMUM_NODES="metaruleDetail_exceptionCode.1154";
    private static final String SELECT_NODE_MODIFY="metaruleDetail_exceptionCode.1155";
    private CoeusMessageResources  coeusMessageResources;
    
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    private Map hmSubModules = null;
    private static final String SELECT_MODULE = "defineRules_exceptionCode.1058";
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
//    private String metaRuleId ;
    /** Creates a new instance of MetaRuleMaintainanceController */
    public MetaRuleDetailController(){
        
    }
    
    
    public MetaRuleDetailController(char functionType,RuleBaseBean ruleBaseBean) throws CoeusException{
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.functionType = functionType;
        this.ruleBaseBean = ruleBaseBean;
        queryKey = ruleBaseBean.getUnitNumber();
        queryEngine = QueryEngine.getInstance();
        registerComponents();
//        setFormData(null);
        postInit();
        
        
    }
    
    /** Register and instantiate the components
     */
    public void registerComponents() {
        metaRuleDetailForm = new MetaRuleDetailForm();
        metaRuleDetailForm.btnNewNode.addActionListener(this);
        metaRuleDetailForm.btnModifyNode.addActionListener(this);
        metaRuleDetailForm.btnDeleteNode.addActionListener(this);
        metaRuleDetailForm.cmbType.addItemListener(this);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        metaRuleDetailForm.cmbModule.addItemListener(this);
        metaRuleDetailForm.cmbSubmodule.addItemListener(this);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        metaRuleTreeRenderer = new MetaRuleTreeRenderer();
        metaRuleDetailForm.txtArDescription.setDocument(new LimitedPlainDocument(200));
        
    }
    
    public void formatFields(){
        addRuleRight = ((Boolean)getRightsData().get(0)).booleanValue();
        modifyRuleRight = ((Boolean)getRightsData().get(1)).booleanValue();
        deleteRuleRight = ((Boolean)getRightsData().get(2)).booleanValue();
        metaRuleDetailForm.btnNewNode.setEnabled(addRuleRight);
        metaRuleDetailForm.btnModifyNode.setEnabled(modifyRuleRight);
        metaRuleDetailForm.btnDeleteNode.setEnabled(deleteRuleRight);
        if(!addRuleRight || !modifyRuleRight || !deleteRuleRight){
            metaRuleDetailForm.txtArDescription.setBackground(disabledBackground);
            metaRuleDetailForm.txtArDescription.setEditable(false);
        }
        
    }
    /** set the tree decorators and set the properticies of the tree
     */
    private void postInit() {
        buildTree();
        if(cvRuleData!= null && cvRuleData.size() > 0){
            ruleTree.setCellRenderer(metaRuleTreeRenderer);
            metaRuleDetailForm.scrpnTree.setViewportView(ruleTree);
            backGroundColor = metaRuleDetailForm.scrpnTree.getBackground();
            ruleTree.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            ruleTree.setOpaque(false);
            ruleTree.setShowsRootHandles(true);
            ruleTree.putClientProperty("Jtree.lineStyle", "Angled");
            ruleTree.setSelectionRow(0);
            selTreePath = ruleTree.getPathForRow(0);
            selectedNode = (MetaRuleNode)selTreePath.getLastPathComponent();
            ruleTree.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent event) {
                    if(event.getKeyCode() == 17) {
                        ruleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
                    }
                }
            });
            expandAll(ruleTree,true);
        }
    }
    /** Build the metaRuleTree
     */
    private void buildTree() {
        if(cvRuleData != null && cvRuleData.size() > 0) {
            ruleTree = new JTree( new MetaRuleNode(cvRuleData.get(0)));
            ruleTree.setShowsRootHandles(true);
            ruleTreeModel = (DefaultTreeModel)ruleTree.getModel();
//            CoeusVector childData = null;
//            Equals equals = null;
            TreePath treePath = null;
            for(int index=1;index<cvRuleData.size();index++) {
                MetaRuleDetailBean ruleBean = (MetaRuleDetailBean)cvRuleData.get(index);
                treePath = findTreePath(ruleBean.getParentNodeId());
                setChildsForParent(ruleBean,treePath);
            }
        }
    }
    /** Set the childs to the parent. Insert all the nodes to the tree
     */
    private void setChildsForParent(MetaRuleDetailBean ruleBean,TreePath treePath) {
        MetaRuleNode childNode = null;
        if(treePath!= null){
            MetaRuleNode parentNode = (MetaRuleNode)treePath.getLastPathComponent();
            childNode = new MetaRuleNode(ruleBean);
            ruleTreeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
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
    public TreePath findTreePath(String name) {
        TreeNode root = (TreeNode)ruleTree.getModel().getRoot();
        TreePath result = findTreePath(ruleTree, new TreePath(root), name.trim());
        return result;
    }
    /** Set the child nodes to the tree using TreeNodes and buld the tree
     *for each parent
     */
    private TreePath findTreePath(JTree tree, TreePath parent, String parentId) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        MetaRuleDetailBean ruleBean = ((MetaRuleNode)node).getDataObject();
        String nodeValue = ruleBean.getNodeId();
        if (node != null && nodeValue.trim().equals(parentId)) {
            return parent;
        }else{
            
//            Object o = node;
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    TreePath result = findTreePath(tree, path, parentId);
                    if(result!=null){
                        return result;
                    }
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
    
    
    
    
    public void display() {
    }
    
    
    
    public java.awt.Component getControlledUI() {
        return metaRuleDetailForm;
    }
    
    public Object getFormData() {
        return null;
    }
    /** set the ACTypes for the description based on the itemStatechanged and
     * while Validating and saving
     * @param ruleType selected rule type
     * @param moduleCode code of selected module
     * @param submoduleCode code of selected submodule
     */
    //Modified method signature for Coeus 4.3 Routing enhancement -PT ID:2785
    //Changed the method signature to include moduleCode and submoduleCode as argument
    private String saveDescData(String ruleType, String moduleCode, String submoduleCode)
    throws CoeusException{
        String retValue = EMPTY_STRING;
        String descriptionValue = metaRuleDetailForm.txtArDescription.getText().trim();
        Equals eqRuleType = new Equals("metaRuleType",ruleType);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Filter the vector cvData based on moduleCode, subModuleCode and ruleType
        Equals eqModuleId = new Equals("moduleCode",moduleCode);
        Equals eqSubmoduleId = new Equals("submoduleCode", submoduleCode);
        And andModuleType = new And(eqRuleType, new And(eqModuleId, eqSubmoduleId));
        //CoeusVector cvData = cvDescriptionData.filter(eqRuleType);
        CoeusVector cvData = cvDescriptionData.filter(andModuleType);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        String description = null;
        if(cvData != null && cvData.size() > 0){
            MetaRuleBean  metaRuleBean = (MetaRuleBean)cvData.get(0);
            description = metaRuleBean.getDescription();
            if(description== null){
                description = EMPTY_STRING;
            }
            if((!description.equals(descriptionValue))){
                if(metaRuleBean.getAcType()==null){
                    if(descriptionValue.equals(EMPTY_STRING) &&
                            (cvRuleData == null || cvRuleData.size() == 0)){
                        metaRuleBean.setAcType(TypeConstants.DELETE_RECORD);
                    }else{
                        if(metaRuleBean.getAcType()!= null && !metaRuleBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                            metaRuleBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        
                    }
                }else if(metaRuleBean.getAcType()!= null && metaRuleBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    if(descriptionValue.equals(EMPTY_STRING) && (cvRuleData == null || cvRuleData.size() == 0) ){
                        metaRuleBean.setAcType(null);
                    }
                }
                metaRuleBean.setDescription(descriptionValue);
                modified = true;
                retValue = descriptionValue;
            }
        }else{
            //if(isCreateMetaRule()|| !descriptionValue.equals(EMPTY_STRING)){
            if(!descriptionValue.equals(EMPTY_STRING)){
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //module code, submoduleCode and rule type is passed as arguments
                createMetaRule(ruleType, moduleCode, submoduleCode);
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            }
        }
        return  retValue;
    }
    
    public void saveDescriptionData() throws CoeusException{
        MetaRuleBean metaRuleBean = null;
        String metaRuleId = EMPTY_STRING;
        Equals eqMetaRuleId = null;
        CoeusVector cvFilterData = null;
        if(cvDescriptionData!= null && cvDescriptionData.size() > 0){
            for(int index=0; index < cvDescriptionData.size(); index ++){
                metaRuleBean = (MetaRuleBean)cvDescriptionData.get(index);
                metaRuleId = metaRuleBean.getMetaRuleId();
                eqMetaRuleId = new Equals("metaRuleId",metaRuleId);
                cvFilterData = cvMetaMasterData.filter(eqMetaRuleId);
                if(metaRuleBean.getAcType()==null){
                    if(metaRuleBean.getDescription().equals(EMPTY_STRING) &&
                            (cvFilterData == null || cvFilterData.size() == 0) ){
                        metaRuleBean.setAcType(TypeConstants.DELETE_RECORD);
                    }else{
                        if(metaRuleBean.getAcType()!= null && !metaRuleBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            metaRuleBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        
                    }
                }
                //}else
                if(metaRuleBean.getAcType()!= null &&
                        metaRuleBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    if(metaRuleBean.getDescription().equals(EMPTY_STRING) &&
                            (cvFilterData == null || cvFilterData.size() == 0) ){
                        metaRuleBean.setAcType(null);
                    }
                }else if(metaRuleBean.getAcType()== null &&
                        !metaRuleBean.getDescription().equals(EMPTY_STRING)){
                    metaRuleBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
    }
    //Modified method signature for Coeus 4.3 Routing enhancement -PT ID:2785
    //Changed the method signature to include moduleCode and submoduleCode as argument
    private void createMetaRule(String ruleType, String moduleCode, String submoduleCode) throws CoeusException{
        MetaRuleBean metaRuleBean = new MetaRuleBean();
        metaRuleBean.setMetaRuleId(getNewMetaRuleId());
        metaRuleBean.setMetaRuleType(ruleType);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        metaRuleBean.setModuleCode(moduleCode);
        metaRuleBean.setSubmoduleCode(submoduleCode);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        metaRuleBean.setDescription(metaRuleDetailForm.txtArDescription.getText().trim());
        metaRuleBean.setUnitNumber(unitNumber);
        modified = true;
        metaRuleBean.setAcType(TypeConstants.INSERT_RECORD);
        cvDescriptionData.addElement(metaRuleBean);
    }
    
    
    /** check whther save is required or not
     *@return boolean
     */
    public boolean isSaveRequired(){
        Object selectedItem = metaRuleDetailForm.cmbType.getSelectedItem();
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//        String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode();
        String moduleId = getSelectedModuleCode();
        ComboBoxBean subModuleComboBean = (ComboBoxBean)metaRuleDetailForm.cmbSubmodule.getSelectedItem();
        String submoduleId = getSelectedSubModuleCode();
        try{
            saveDescData(getSelectedRuleType(selectedItem), moduleId, submoduleId);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }catch (CoeusException ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        if(modified){
            return true;
        }else{
            return false;
        }
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        
    }
    
    /** Get all the MetaRuleTreeData which is going for saving. Get only the
     * modified data in the vector
     *@ return CoeusVector containing modified tree data
     */
    public  CoeusVector saveMetaRuleFormData()throws edu.mit.coeus.exception.CoeusException{
        Object selectedItem = metaRuleDetailForm.cmbType.getSelectedItem();
        CoeusVector dataToServer = null;
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode();
        String moduleId = getSelectedModuleCode();
        //String subModuleId = ((ComboBoxBean)metaRuleDetailForm.cmbSubmodule.getSelectedItem()).getCode();
        String subModuleId = getSelectedSubModuleCode();
        saveDescData(getSelectedRuleType(selectedItem), moduleId, subModuleId);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        if(modified){
            CoeusVector dataObjects = new CoeusVector();
            if(cvDeletedData!= null && cvDeletedData.size() > 0){
                dataObjects.addAll(cvDeletedData);
            }
            dataObjects.addAll(cvMetaMasterData);
            dataToServer = new CoeusVector();
            if(dataObjects!= null && dataObjects.size() > 0){
                for(int index = 0; index < dataObjects.size(); index++ ){
                    MetaRuleDetailBean metaRuleDetailBean = (MetaRuleDetailBean)dataObjects.get(index);
                    if(metaRuleDetailBean.getAcType()!= null && !(metaRuleDetailBean.getAcType().equals(null))){
                        dataToServer.addElement(metaRuleDetailBean);
                    }
                }
            }
        }
        return dataToServer;
    }
    
    /** Make server call to get the new MetaRuleId.
     *@returns String containing new MetaRuleId
     */
    private String getNewMetaRuleId() throws CoeusException{
//        String metaRuleId = EMPTY_STRING;
        int ruleId = 0;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_META_RULE_ID );
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+RULE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            ruleId = ((Integer)response.getDataObject()).intValue();
        }
        return new Integer(ruleId).toString();
    }
    
    /** Query the data for the MetaRule and MetaRuleDetail from QueryEngine and
     *filter the data for the selected Rule Type and MetaRuleId
     */
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        
        cvDeletedData = new CoeusVector();
        cvRuleData = new CoeusVector();
        cvDescriptionData = new CoeusVector();
        cvMetaMasterData = new CoeusVector();
        cvMetaMasterData = queryEngine.executeQuery(queryKey,MetaRuleDetailBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        cvDescriptionData = queryEngine.executeQuery(queryKey,MetaRuleBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        //loadFirstTime = true;
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        if(loadFirstTime){
            populateModuleComboBox();
            //Added with case 2158:Budget Validations - Start
            String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode();
            populateSubModuleComboBox(moduleId);
            //2158 End
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        if(isRefreshRequired()){
            metaRuleDetailForm.cmbType.setSelectedItem(VAL_ROUTING);
        }
        setRefreshRequired(false);
        loadFirstTime = false;
        Object selectedItem = metaRuleDetailForm.cmbType.getSelectedItem();
        filterData(getSelectedRuleType(selectedItem));
        
    }
    
    
    /** Pass the selected RuleType as like "Routing" and get the
     *the DB data as "R". This method will give formatted RuleType
     *@retyrn String containing RuleType(V,R,N)
     */
    private String  getSelectedRuleType(Object selectedItem){
        String selectedValue = EMPTY_STRING;
        if(selectedItem.equals(VAL_ROUTING)){
            selectedValue = ROUTING;
        }else if(selectedItem.equals(VAL_VALIDATION)){
            selectedValue = VALIDATION;
        }else if(selectedItem.equals(VAL_NOTIFICATION)){
            selectedValue = NOTIFICATION;
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        else if(selectedItem.equals(VAL_QUESTION)){
            selectedValue = QUESTION;
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        return selectedValue;
    }
    /** Get the RuleType as Routing
     *@poaram RuleType as (R,V,N)
     *returns RuleType as(Routing, Validation, Notification
     */
    private String getComboValue(String beanRuleType){
        String selectedValue = EMPTY_STRING;
        if(beanRuleType.equals(ROUTING)){
            selectedValue = VAL_ROUTING;
        }else if(beanRuleType.equals(VALIDATION)){
            selectedValue = VAL_VALIDATION;
        }else if(beanRuleType.equals(NOTIFICATION)){
            selectedValue = VAL_NOTIFICATION;
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        else if(beanRuleType.equals(QUESTION)){
            selectedValue = VAL_QUESTION;
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        return selectedValue;
    }
    /** this is the prevalidation which is done for the selected RuleType.
     *After Prevalidation the validate() method will be invoked.
     *@ returns boolean saying validation is passed or failed
     */
    public boolean prevalidation(){
        String desc = EMPTY_STRING;
        try{
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode();Leena
            String moduleId = getSelectedModuleCode();
            if(moduleId == EMPTY_STRING){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_MODULE));
                return false;
            }
            String subModuleId = getSelectedSubModuleCode();
            desc = saveDescData(getSelectedRuleType(metaRuleDetailForm.cmbType.getSelectedItem()),
                    moduleId, subModuleId);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            if(cvRuleData!= null && cvRuleData.size() > 0){
            }else{
                if(!desc.equals(EMPTY_STRING)){
                    CoeusOptionPane.showInfoDialog("At least one "+metaRuleDetailForm.cmbType.getSelectedItem()+" node must be defined ");
                    return false;
                }
            }
        }catch (CoeusException ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        return true;
    }
    /** An overridden method. Validate the MetaRule Data
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        MetaRuleBean metaRuleBean = null;
//        MetaRuleDetailBean metaRuleDetailBean = null;
        Equals eqMetaRuleId = null;
        Equals eqMetaRuleType = null;
        CoeusVector cvMetaRuleDetail= null;
        String beanDesc = EMPTY_STRING;
//        String compValue = EMPTY_STRING;
        //try{
        // compValue = saveDescData(getSelectedRuleType(metaRuleDetailForm.cmbType.getSelectedItem()));
        if(cvDescriptionData!= null){
            for(int index=0; index < cvDescriptionData.size(); index++){
                metaRuleBean = (MetaRuleBean)cvDescriptionData.get(index);
                eqMetaRuleType = new Equals("metaRuleType",metaRuleBean.getMetaRuleType());
                eqMetaRuleId = new Equals("metaRuleId",metaRuleBean.getMetaRuleId());
                cvMetaRuleDetail = cvMetaMasterData.filter(eqMetaRuleId);
                beanDesc= metaRuleBean.getDescription();
                if(cvMetaRuleDetail!= null && cvMetaRuleDetail.size() > 0){
                    if(beanDesc.equals(EMPTY_STRING)){
                        String msg = "Please enter description for "+getComboValue(metaRuleBean.getMetaRuleType())+" meta rule";
                        CoeusOptionPane.showInfoDialog(msg);
                        metaRuleDetailForm.txtArDescription.requestFocusInWindow();
                        return false;
                    }
                }else{
                    if(beanDesc!= null && !beanDesc.equals(EMPTY_STRING)){
                        CoeusOptionPane.showInfoDialog("At least one "+getComboValue(metaRuleBean.getMetaRuleType())+" node must be defined ");
                        return false;
                    }
                }
            }
        }
//            else{
//            if(!compValue.equals(EMPTY_STRING)){
//                CoeusOptionPane.showInfoDialog("At least one "+metaRuleDetailForm.cmbType.getSelectedItem()+" node must be defined ");
//                return false;
//            }
//        }
        // Do the validation for the selected item.
        if(cvRuleData!= null && cvRuleData.size() > 0){
        }else{
            String desc = metaRuleDetailForm.txtArDescription.getText().trim();
            if(!desc.equals(EMPTY_STRING)){
                CoeusOptionPane.showInfoDialog("At least one "+metaRuleDetailForm.cmbType.getSelectedItem()+" node must be defined ");
                return false;
            }
        }
        
        
//        }catch (CoeusException exception){
//            exception.printStackTrace();
//            CoeusOptionPane.showErrorDialog(exception.getMessage());
//        }
        return true;
    }
    
    
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(metaRuleDetailForm.btnNewNode) ){
                performRuleSelection(TypeConstants.ADD_MODE);
            }else if(source.equals(metaRuleDetailForm.btnModifyNode)){
                performRuleSelection(TypeConstants.MODIFY_MODE);
            }else if(source.equals(metaRuleDetailForm.btnDeleteNode)){
                performDeleteAction();
            }
        }catch (CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
    }
    
    private void performDeleteAction() throws CoeusException{
        MetaRuleDetailBean deletedBean = null;
        if(ruleTree== null){
            ruleTree = new JTree( new MetaRuleNode(null));
        }
        TreePath selectionPath = ruleTree.getSelectionPath();
        if(selectionPath!= null){
            MetaRuleNode selNode = (MetaRuleNode)selectionPath.getLastPathComponent();
            int selRow=ruleTree.getRowForPath(selectionPath);
            
            int childCount = selNode.getChildCount();
            int optionSelected = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(CONFIRM_DELETE)
            ,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            if(optionSelected == CoeusOptionPane.SELECTION_YES){
                if(childCount > 0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CHILD_NODE_EXISTS));
                    return ;
                }
                
                deletedBean = ((MetaRuleNode)selNode).getDataObject();
                if(deletedBean.getAcType()!= null && deletedBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    cvRuleData.remove(deletedBean);
                    modified = false;
                    cvMetaMasterData.remove(deletedBean);
                    queryEngine.addCollection(queryKey,MetaRuleDetailBean.class,cvMetaMasterData);
                }else{
                    modified = true;
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedData.addElement(deletedBean);
                    cvRuleData.remove(deletedBean);
                    cvMetaMasterData.remove(deletedBean);
                    queryEngine.addCollection(queryKey,MetaRuleDetailBean.class,cvMetaMasterData);
                }
                updateParentData(deletedBean);
                if(selNode.isRoot()){
                    ruleTreeModel.setRoot(null);
                }else{
                    ruleTreeModel.removeNodeFromParent(selNode);
                }
                postInit();
                
                if(selRow < ruleTree.getRowCount() &&
                        ((MetaRuleNode)ruleTree.getPathForRow(selRow).getLastPathComponent()).getAllowsChildren() && !selNode.getAllowsChildren())
                    ruleTree.setSelectionPath(selectionPath.getParentPath());
                else
                    if(selRow >= ruleTree.getRowCount()){
                    selRow = selRow-1;
                    }
                ruleTree.setSelectionRow(selRow);
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_NODE_DELETE));
            return ;
        }
    }
    /** Update the parent node data when delete operation is performd
     *@param MetaRuleDetailBean deletedBean
     */
    private void updateParentData(MetaRuleDetailBean deletedBean){
        Equals equals= new Equals("nodeId",deletedBean.getParentNodeId());
        CoeusVector cvParentData = cvRuleData.filter(equals);
//        CoeusVector cvParentData = cvMetaMasterData.filter(equals);- Aithal
        if(cvParentData!= null && cvParentData.size() > 0){
            MetaRuleDetailBean detailBean = (MetaRuleDetailBean)cvParentData.get(0);
            if(detailBean.getNextNode()!= null &&detailBean.getNextNode().equals(deletedBean.getNodeId())){
                detailBean.setNextNode(EMPTY_STRING);
            }else if(detailBean.getNodeIfFalse()!= null &&detailBean.getNodeIfFalse().equals(deletedBean.getNodeId())){
                detailBean.setNodeIfFalse(EMPTY_STRING);
            }else if(detailBean.getNodeIfTrue()!= null &&detailBean.getNodeIfTrue().equals(deletedBean.getNodeId())){
                detailBean.setNodeIfTrue(EMPTY_STRING);
            }
            if(deletedBean.getAcType()!= null && !deletedBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                detailBean.setAcType("U");
            }
        }
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
//        String selectedValue = EMPTY_STRING;
        Object selectedItem = null;
        try{
            
            if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                if (source.equals(metaRuleDetailForm.cmbType) ||
                        source.equals(metaRuleDetailForm.cmbModule) ||
                        source.equals(metaRuleDetailForm.cmbSubmodule)) {
                    if(!loadFirstTime){
                        //Object item = (String) itemEvent.getItem();
                        Object item = (String)metaRuleDetailForm.cmbType.getSelectedItem();
                        //String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode(); Leena
                        String moduleId = getSelectedModuleCode();
                        String submoduleId = getSelectedSubModuleCode();
                        if(itemEvent.getSource().equals(metaRuleDetailForm.cmbType)){
                            item = (String) itemEvent.getItem();
                        }else if(itemEvent.getSource().equals(metaRuleDetailForm.cmbModule)){
                            moduleId = ((ComboBoxBean)itemEvent.getItem()).getCode();
                        }else if(itemEvent.getSource().equals(metaRuleDetailForm.cmbSubmodule)){
                            submoduleId = "0";
                            if(itemEvent.getItem()!=null && !((ComboBoxBean)itemEvent.getItem()).getCode().equals("")){
                                submoduleId = ((ComboBoxBean)itemEvent.getItem()).getCode();
                            }
                        }
                            saveDescData(getSelectedRuleType(item), moduleId, submoduleId);
                        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                    }
                }
            }
            if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //The filtering is to be done whenever the type and module combobox
                //value is changed
                //Commented and Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-start
                //Disabling cmbSubmodule if Type is Validation and Module is IRB
//                if(source.equals(metaRuleDetailForm.cmbModule) ||
//                           source.equals(metaRuleDetailForm.cmbType)){//2158
//                    String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode();
//                    populateSubModuleComboBox(moduleId);
//                }    
                // Modified for COEUSQA-1724_ Implement validation based on rules in protocols_Start
                String protocolModule = ""+ModuleConstants.PROTOCOL_MODULE_CODE;
                if(source.equals(metaRuleDetailForm.cmbModule) ||
                           source.equals(metaRuleDetailForm.cmbType)){//2158
                    String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode();
                    String strSelectedType =(String)metaRuleDetailForm.cmbType.getSelectedItem();                    
                    if("Validation".equals(strSelectedType) &&
                            //protocolModule.equals(moduleId)){
                            checkModule(moduleId)){
                        metaRuleDetailForm.cmbSubmodule.setEnabled(false);
                        metaRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
                    }else{
                         //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-Start
                         if("Validation".equals(strSelectedType) &&                             
                            !checkModule(moduleId)){
                             metaRuleDetailForm.cmbSubmodule.setEnabled(true);
                             populateSubModuleComboBox(moduleId);
                         }else{
                            metaRuleDetailForm.cmbSubmodule.setEnabled(false); 
                            metaRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
                         }
                         //Modified for COEUSQA-3056 Sub module options should not be available for certain business rule types-End
                    }
                }
                // Modified for COEUSQA-1724_ Implement validation based on rules in protocols_End
                //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols-end
                if(source.equals(metaRuleDetailForm.cmbType) ||
                        source.equals(metaRuleDetailForm.cmbModule) ||
                        source.equals(metaRuleDetailForm.cmbSubmodule)){
                    selectedItem = metaRuleDetailForm.cmbType.getSelectedItem();
                    filterData(getSelectedRuleType(selectedItem));
                    
                }
                
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    /** Filter the MetaRule and MetaRuleDetail Data. Set the Filter data to the
     *Tree and to the description components
     */
    private void filterData(String selectedValue)  throws CoeusException{
        String metaRuleId = null;
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Instead of only filtering by type, now the filtering is done based on
        //type,module and submodule selected
        Equals eqMetaRuleType,eqMetaRuleId, eqModuleId, eqSubModuleId = null;
        eqMetaRuleType = new Equals("metaRuleType",selectedValue);
        //String moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode(); Leena
        String moduleId = getSelectedModuleCode(); 
        String submoduleId = getSelectedSubModuleCode();
        eqModuleId = new Equals("moduleCode",moduleId);
        eqSubModuleId = new Equals("submoduleCode", submoduleId);
        And andModuleType = new And(eqMetaRuleType, new And(eqModuleId, eqSubModuleId));
        //CoeusVector cvFilterDescData = cvDescriptionData.filter(eqMetaRuleType);
        CoeusVector cvFilterDescData = cvDescriptionData.filter(andModuleType);
        
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        if(cvFilterDescData!= null && cvFilterDescData.size() > 0){
            MetaRuleBean metaRuleBean = (MetaRuleBean)cvFilterDescData.get(0);
            metaRuleId = metaRuleBean.getMetaRuleId();
            eqMetaRuleId = new Equals("metaRuleId",metaRuleId);
            cvRuleData = cvMetaMasterData.filter(eqMetaRuleId);
            if(cvRuleData!= null && cvRuleData.size() > 0){
                postInit();
                metaRuleDetailForm.txtArDescription.setText(metaRuleBean.getDescription());
            }else{
                cvRuleData = new CoeusVector();
                if(ruleTreeModel!= null){
                    ruleTreeModel.setRoot(null);
                    metaRuleDetailForm.txtArDescription.setText(metaRuleBean.getDescription());
                }
            }
        }else{
            cvRuleData = new CoeusVector();
            if(ruleTreeModel!= null){
                ruleTreeModel.setRoot(null);
            }
            // setFormData(null);
            metaRuleDetailForm.txtArDescription.setText(EMPTY_STRING);
        }
    }
    
    /** Validate when ADD button action is performed, Validate whether new node can
     *be addded or not
     *@param CoeusVector contains the filterData(MetaRuleDetail), MetaRuleDetaiBen
     */
    private boolean validateActionData(CoeusVector filterData,MetaRuleDetailBean selParentBean){
        Equals eqNodeId  = new Equals("nodeId",selParentBean.getNodeId());
        CoeusVector cvFilterData = cvRuleData.filter(eqNodeId );
        if(cvFilterData!= null && cvFilterData.size() > 0){
//            MetaRuleDetailBean metaRuleDetailBean = (MetaRuleDetailBean)cvFilterData.get(0);
            //String errorMsg = "Maximum node is defined";
            if(selParentBean.getNextNode()!= null && !selParentBean.getNextNode().equals(EMPTY_STRING)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MAXIMUM_NODES));
                return false;
            }else if(filterData.size() > 1){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MAXIMUM_NODES));
                return false;
            }
        }
        return true;
    }
    
    /** This method will be invoked when root node is adding. This method will
     *add a new node and calls for the New MetaRuleId if there are no comments
     *and no nodes
     *@param action Mode, RuleType
     *@param modulecode code of module selected.
     *@param submoduleCode code of the submodule selected
     *@throws CoeusException
     */
    //Modified method signature for Coeus 4.3 Routing enhancement -PT ID:2785
    //Changed the method signature to include moduleCode as argument
    private void createParentNode(char actionMode,String ruleType, String moduleCode, String submoduleCode)
    throws CoeusException{
        // Declarations - start
        MetaRuleDetailBean  metaRuleDetailBean= null;
        MetaRuleBean metaRuleBean = null;
        BusinessRuleBean businessRuleBean = null;
        modified = true;
//        String nodeId = null;
        int newNodeId = 1;
        Equals eqMetaRuleId = null;
        CoeusVector cvFilterMetaRule = null;
//        boolean isNewMetaRuleId = false;
        String metaRuleId = EMPTY_STRING;
        String ruleCriteria = EMPTY_STRING;
        // Declarations - End
        // Check for the selected ruletype and check whethe data is there or not.
        //If not then create new metaRuleId
        eqMetaRuleId = new Equals("metaRuleType",ruleType);
        
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Filter the vector using rule type and module id
        Equals eqModuleId = new Equals("moduleCode", moduleCode);
        Equals eqSubmoduleId = new Equals("submoduleCode", submoduleCode);
        And andTypeModuleId = new And(eqMetaRuleId, new And(eqModuleId, eqSubmoduleId));
        //cvFilterMetaRule = cvDescriptionData.filter(eqMetaRuleId);
        cvFilterMetaRule = cvDescriptionData.filter(andTypeModuleId);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        if(cvFilterMetaRule!= null && cvFilterMetaRule.size() > 0){
            metaRuleBean = (MetaRuleBean)cvFilterMetaRule.get(0);
            metaRuleId = metaRuleBean.getMetaRuleId();
        }else{
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            createMetaRule(ruleType, moduleCode, submoduleCode);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            eqMetaRuleId = new Equals("metaRuleType",ruleType);
            //Modifed for case 3162 - Routing Issues - start
            //Filter using type module and sub module
            cvFilterMetaRule = cvDescriptionData.filter(andTypeModuleId);
            //Modifed for case 3162 - Routing Issues - end
            if(cvFilterMetaRule!= null && cvFilterMetaRule.size() > 0){
                metaRuleBean = (MetaRuleBean)cvFilterMetaRule.get(0);
                metaRuleId = metaRuleBean.getMetaRuleId();
            }
        }
//        if(cvFilterMetaRule!= null && cvFilterMetaRule.size() > 0){
//            isNewMetaRuleId = false;
//            metaRuleBean = (MetaRuleBean)cvFilterMetaRule.get(0);
//        }else{
//            isNewMetaRuleId = true;
//        }// End for checking new metaRuleId.
//
//        metaRuleDetailBean = new MetaRuleDetailBean();
//        if(isNewMetaRuleId){
//            metaRuleDetailBean = new MetaRuleDetailBean();
//            metaRuleId = getNewMetaRuleId();
//            metaRuleBean = new MetaRuleBean();
//            metaRuleBean.setAcType(TypeConstants.INSERT_RECORD);
//            metaRuleBean.setDescription(metaRuleDetailForm.txtArDescription.getText().trim());
//            metaRuleBean.setMetaRuleId(metaRuleId);
//            metaRuleBean.setMetaRuleType(ruleType);
//            metaRuleBean.setUnitNumber(getUnitNumber());
//
//        }else{
//            metaRuleId = metaRuleBean.getMetaRuleId();
//        }
        
        // Update the MetaRuleDetailBean which is addded new
        metaRuleDetailBean = new MetaRuleDetailBean();
        metaRuleDetailBean.setMetaRuleId(metaRuleId);
        metaRuleDetailBean.setNodeId(new Integer(newNodeId).toString());
        metaRuleDetailBean.setParentNodeId("0");
        metaRuleDetailBean.setNodeIfFalse(EMPTY_STRING);
        metaRuleDetailBean.setNodeIfTrue(EMPTY_STRING);
        metaRuleDetailBean.setNextNode(EMPTY_STRING);
        
        cvRuleData = new CoeusVector();
        RulesSelectionController rulesSelectionController =
                new RulesSelectionController(ruleBaseBean, functionType,actionMode);
        rulesSelectionController.setSelectedNodeBean(null);
        rulesSelectionController.setRuleType(getSelectedRuleType(metaRuleDetailForm.cmbType.getSelectedItem()));
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        rulesSelectionController.setModuleCode(moduleCode);
        rulesSelectionController.setSubModuleCode(submoduleCode);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        rulesSelectionController.setFormData(null);
        rulesSelectionController.display();
        if(!rulesSelectionController.isIsOkClicked()){
            return ;
        }
        CoeusVector cvCritraiData =  (CoeusVector)rulesSelectionController.getFormData();
        if(cvCritraiData!= null && cvCritraiData.size() > 0){
            ruleCriteria = (String)cvCritraiData.get(0);
            businessRuleBean = (BusinessRuleBean)cvCritraiData.get(1);
        }
        metaRuleDetailBean.setRuleId(businessRuleBean.getRuleId());
        metaRuleDetailBean.setDescription(businessRuleBean.getDescription());
        metaRuleDetailBean.setAcType("I");
        cvRuleData.addElement(metaRuleDetailBean);
        cvMetaMasterData.addElement(metaRuleDetailBean);
        queryEngine.addCollection(queryKey,MetaRuleDetailBean.class,cvMetaMasterData);
        MetaRuleNode newNode = new MetaRuleNode(metaRuleDetailBean);
        postInit();
        
    }
    /** This method will be adding OR modifying the nodes
     *@param action Mode - Add/Modify
     */
    private void performRuleSelection(char actionMode) throws CoeusException{
        Object selectedData = metaRuleDetailForm.cmbType.getSelectedItem();
        String ruleType = EMPTY_STRING;
        // Get the Selected Rule Type
        if(selectedData.equals(VAL_ROUTING)){
            ruleType = ROUTING;
        }else if(selectedData.equals(VAL_NOTIFICATION)){
            ruleType = NOTIFICATION;
        }else if(selectedData.equals(VAL_VALIDATION)){
            ruleType = VALIDATION;
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        else if(selectedData.equals(VAL_QUESTION)){
            ruleType = QUESTION;
        }
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        if(actionMode== TypeConstants.MODIFY_MODE){
            if(ruleTree== null){
                ruleTree = new JTree( new MetaRuleNode(null));
            }
            TreePath selTreePath = ruleTree.getSelectionPath();
            if(selTreePath== null){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_NODE_MODIFY));
                return ;
            }
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        String moduleId = "0";
        if(metaRuleDetailForm.cmbModule.getSelectedItem()!=null){
            moduleId = ((ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem()).getCode();
        }
        String subModuleId = getSelectedSubModuleCode();
        if(cvRuleData==null || cvRuleData.size()==0){
            createParentNode(actionMode, ruleType, moduleId, subModuleId);
            return ;
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        if(actionMode== TypeConstants.ADD_MODE){
            
            TreePath selTreePath = ruleTree.getSelectionPath();
            MetaRuleNode selNode = (MetaRuleNode)selTreePath.getLastPathComponent();
            /**this is to set the property to the selection window. It contains only
             *the parent bean which is selected
             */
            MetaRuleDetailBean selParentBean = ((MetaRuleNode)selNode).getDataObject();
            // End
            MetaRuleDetailBean metaRuleDetailBean;
            int childCount = selNode.getChildCount();
            CoeusVector cvChildData =  new CoeusVector();
            MetaRuleNode nextNodes  = null;
            for(int index = 0; index < childCount; index++){
                nextNodes = (MetaRuleNode)selNode.getChildAt(index);
                metaRuleDetailBean = ((MetaRuleNode)nextNodes).getDataObject();
                cvChildData.addElement(metaRuleDetailBean);
            }
            if(actionMode!= TypeConstants.MODIFY_MODE){
                boolean isAllowed  = validateActionData(cvChildData,selParentBean);
                if(!isAllowed){
                    return ;
                }
            }
            RulesSelectionController rulesSelectionController =
                    new RulesSelectionController(ruleBaseBean, functionType,actionMode);
            rulesSelectionController.setSelectedNodeBean(selParentBean);
            rulesSelectionController.setRuleType(ruleType);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            rulesSelectionController.setModuleCode(moduleId);
            rulesSelectionController.setSubModuleCode(subModuleId);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            rulesSelectionController.setFormData(cvRuleData);
            rulesSelectionController.display();
            if(!rulesSelectionController.isIsOkClicked()){
                return ;
            }
            modified = true;
//            String nodeId = null;
            int newNodeId = 0;
            CoeusVector cvCritraiData =  (CoeusVector)rulesSelectionController.getFormData();
            BusinessRuleBean businessRuleBean = null;
            if(cvCritraiData!= null && cvCritraiData.size() > 0){
                if(actionMode== TypeConstants.ADD_MODE){
                    metaRuleDetailBean = new MetaRuleDetailBean();
                    String ruleCriteria = (String)cvCritraiData.get(0);
                    businessRuleBean = (BusinessRuleBean)cvCritraiData.get(1);
                    String nodeNum = EMPTY_STRING;
                    int nodeIdVal=0;
                    // Get the Max node Id
                    for(int index= 0; index < cvRuleData.size(); index++){
                        MetaRuleDetailBean countBean = (MetaRuleDetailBean)cvRuleData.get(index);
                        nodeNum = countBean.getNodeId();
                        nodeIdVal = Integer.parseInt(nodeNum);
                    }
                    newNodeId = nodeIdVal+1;
                    metaRuleDetailBean.setNodeId(new Integer(newNodeId).toString());
                    metaRuleDetailBean.setParentNodeId(selParentBean.getNodeId());
                    metaRuleDetailBean.setNodeIfFalse(EMPTY_STRING);
                    metaRuleDetailBean.setNodeIfTrue(EMPTY_STRING);
                    metaRuleDetailBean.setNextNode(EMPTY_STRING);
                    metaRuleDetailBean.setMetaRuleId(selParentBean.getMetaRuleId());
                    metaRuleDetailBean.setRuleId(businessRuleBean.getRuleId());
                    metaRuleDetailBean.setDescription(businessRuleBean.getDescription());
                    metaRuleDetailBean.setAcType("I");
                    
                    cvRuleData.addElement(metaRuleDetailBean);
                    cvMetaMasterData.addElement(metaRuleDetailBean);
                    queryEngine.addCollection(queryKey,MetaRuleDetailBean.class,cvMetaMasterData);
                    updateNodeId(newNodeId,selParentBean,ruleCriteria);
                    postInit();
                    cvCritraiData = null;
                }
                rulesSelectionController = null;
            }
            
        }else if(actionMode== TypeConstants.MODIFY_MODE){
            TreePath selTreePath = ruleTree.getSelectionPath();
            MetaRuleNode selNode = (MetaRuleNode)selTreePath.getLastPathComponent();
            Equals eqParent  = null;
            CoeusVector cvData  = null;
            /**this is to set the property to the selection window. It contains only
             *the parent bean which is selected
             */
            MetaRuleDetailBean selParentBean = ((MetaRuleNode)selNode).getDataObject();
            if(selParentBean.getNodeId().equals("1")){
                eqParent = new Equals("nodeId",selParentBean.getNodeId());
                cvData  = cvRuleData.filter(eqParent);
            }else{
                eqParent = new Equals("nodeId",selParentBean.getParentNodeId());
                cvData  = cvRuleData.filter(eqParent);
            }
            // End
//            MetaRuleDetailBean metaRuleDetailBean;
            RulesSelectionController rulesSelectionController =
                    new RulesSelectionController(ruleBaseBean, functionType,actionMode);
            rulesSelectionController.setSelectedNodeBean(selParentBean);
            rulesSelectionController.setRuleType(ruleType);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            rulesSelectionController.setModuleCode(moduleId);
            rulesSelectionController.setSubModuleCode(subModuleId);
            //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            rulesSelectionController.setSelectedType(selectedType);
            rulesSelectionController.setFormData(cvRuleData);
            rulesSelectionController.display();
            if(!rulesSelectionController.isIsOkClicked()){
                return ;
            }
            modified = true;
//            String nodeId = null;
//            int newNodeId = 0;
            CoeusVector cvCritraiData =  (CoeusVector)rulesSelectionController.getFormData();
            BusinessRuleBean businessRuleBean = null;
            MetaRuleDetailBean parentBean = null;
            if(cvCritraiData!= null && cvCritraiData.size() > 0){
                String ruleCriteria = (String)cvCritraiData.get(0);
                businessRuleBean = (BusinessRuleBean)cvCritraiData.get(1);
                if(cvData!=null && cvData.size() > 0){
                    parentBean = (MetaRuleDetailBean)cvData.get(0);
                    boolean value = checkForUpdate(ruleCriteria,parentBean);
                    if(value){
                        parentBean.setNodeIfFalse(EMPTY_STRING);
                        parentBean.setNodeIfTrue(EMPTY_STRING);
                        parentBean.setNextNode(EMPTY_STRING);
                    }
                    selParentBean.setRuleId(businessRuleBean.getRuleId());
                    selParentBean.setDescription(businessRuleBean.getDescription());
                    
                    if(selParentBean.getAcType()==null){
                        selParentBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    updateNodeId(Integer.parseInt(selParentBean.getNodeId()),parentBean,ruleCriteria);
                    postInit();
                }
            }
            rulesSelectionController = null;
            queryEngine.addCollection(queryKey,MetaRuleDetailBean.class,cvMetaMasterData);
        }
    }
    /** This method will check for updating the node.If only the Rule Id and
     *Description is Changed then no need to update the parent node. Only the
     *the selected bean can be modified
     */
    private boolean checkForUpdate(String ruleCriteria,MetaRuleDetailBean parentBean){
        boolean value = false;
        if(ruleCriteria.equals("T")){
            if(parentBean.getNodeIfTrue()== null || parentBean.getNodeIfTrue().equals(EMPTY_STRING)){
                value =  true;
            }
        }else if(ruleCriteria.equals("F")){
            
            if(parentBean.getNodeIfFalse()== null || parentBean.getNodeIfFalse().equals(EMPTY_STRING)){
                value = true;
            }
        }else if(ruleCriteria.equals("N") ){
            if(parentBean.getNextNode()== null || parentBean.getNextNode().equals(EMPTY_STRING)){
                value = true;
            }
        }
        return value;
    }
    
    
    /** This method will be invoked when ADD/Modify action is performed
     *It will update the parent node and its corresponding True, False and Next
     *Nodes
     *@param new NodeId, MetaRuleDetailbean selected bean, RuleCriteria
     */
    private void updateNodeId(int newNodeId,MetaRuleDetailBean selectedParentBean,String ruleCriteria){
        if(ruleCriteria.equals("N")){
            selectedParentBean.setNextNode(new Integer(newNodeId).toString());
        }else if(ruleCriteria.equals("F")){
            selectedParentBean.setNodeIfFalse(new Integer(newNodeId).toString());
        }else if(ruleCriteria.equals("T")){
            selectedParentBean.setNodeIfTrue(new Integer(newNodeId).toString());
        }
        if(selectedParentBean.getAcType() == null){
            selectedParentBean.setAcType("U");
        }
    }
    
    /**
     * Getter for property modified.
     * @return Value of property modified.
     */
    public boolean isModified() {
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
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Getter for property rightsData.
     * @return Value of property rightsData.
     */
    public Vector getRightsData() {
        return rightsData;
    }
    
    /**
     * Setter for property rightsData.
     * @param rightsData New value of property rightsData.
     */
    public void setRightsData(Vector rightsData) {
        this.rightsData = rightsData;
    }
    
    /**
     * Getter for property cvDescriptionData.
     * @return Value of property cvDescriptionData.
     */
    public edu.mit.coeus.utils.CoeusVector getCvDescriptionData() {
        CoeusVector cvDescData = new CoeusVector();
        if(cvDescriptionData!= null && cvDescriptionData.size() > 0){
            for(int index=0; index < cvDescriptionData.size(); index++){
                MetaRuleBean metaRuleBean = (MetaRuleBean)cvDescriptionData.get(index);
                if(metaRuleBean.getAcType()!= null ){
                    cvDescData.addElement(metaRuleBean);
                }
            }
        }
        
        return cvDescData;
        //return cvDescriptionData;
    }
    
    /**
     * Setter for property cvDescriptionData.
     * @param cvDescriptionData New value of property cvDescriptionData.
     */
    public void setCvDescriptionData(edu.mit.coeus.utils.CoeusVector cvDescriptionData) {
        this.cvDescriptionData = cvDescriptionData;
    }
    
    /**
     * Getter for property createMetaRule.
     * @return Value of property createMetaRule.
     */
    public boolean isCreateMetaRule() {
        return createMetaRule;
    }
    
    /**
     * Setter for property createMetaRule.
     * @param createMetaRule New value of property createMetaRule.
     */
    public void setCreateMetaRule(boolean createMetaRule) {
        this.createMetaRule = createMetaRule;
    }
    
    /**
     * Getter for property refreshRequired.
     * @return Value of property refreshRequired.
     */
    public boolean isRefreshRequired() {
        return refreshRequired;
    }
    
    /**
     * Setter for property refreshRequired.
     * @param refreshRequired New value of property refreshRequired.
     */
    public void setRefreshRequired(boolean refreshRequired) {
        this.refreshRequired = refreshRequired;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    /**
     * Populate the module and submodule combobox with the module names
     */
    public void populateModuleComboBox() throws CoeusException{
        //Populate the module combobox
        CoeusVector cvModule = queryEngine.getDetails(queryKey, ModuleDataBean.class);
        CoeusVector cvModuleData = new CoeusVector();
        if(cvModule!=null){
            ComboBoxBean comboBoxBean = null;
            ModuleDataBean moduleDataBean = null;
            for(int i=0; i<cvModule.size();i++){
                moduleDataBean = (ModuleDataBean)cvModule.get(i);
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(moduleDataBean.getCode());
                comboBoxBean.setDescription(moduleDataBean.getDescription());
                cvModuleData.add(comboBoxBean);
            }
        }
        metaRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
        metaRuleDetailForm.cmbModule.setModel(new DefaultComboBoxModel(cvModuleData));
        
        //Populate the submodule combobox
        hmSubModules = new HashMap();
        CoeusVector cvSubmodule = queryEngine.getDetails(queryKey, SubModuleDataBean.class);
        CoeusVector cvModulewiseSubModules = null;
        if(cvSubmodule!=null){
            ComboBoxBean comboBoxBean = null;
            SubModuleDataBean submoduleDataBean = null;
            for(int i=0; i<cvSubmodule.size();i++){
                submoduleDataBean = (SubModuleDataBean)cvSubmodule.get(i);
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(submoduleDataBean.getCode());
                comboBoxBean.setDescription(submoduleDataBean.getDescription());
                if(hmSubModules.get(Integer.toString(submoduleDataBean.getModuleCode()))!=null){
                    ((CoeusVector)hmSubModules.get(Integer.toString(submoduleDataBean.getModuleCode()))).add(comboBoxBean);
                }else{
                    cvModulewiseSubModules = new CoeusVector();
                    cvModulewiseSubModules.add(new ComboBoxBean(EMPTY_STRING, EMPTY_STRING));
                    cvModulewiseSubModules.add(comboBoxBean);
                    hmSubModules.put(Integer.toString(submoduleDataBean.getModuleCode()), cvModulewiseSubModules);
                }
            }
        }
    }
    
    public void populateSubModuleComboBox(String moduleId){
        //Modified with case 2158:Budget Validations - Start
        Object selectedRuleType = metaRuleDetailForm.cmbType.getSelectedItem();
        if(VALIDATION.equals(getSelectedRuleType(selectedRuleType))){
            CoeusVector cvSubmodule = (CoeusVector)hmSubModules.get(moduleId);
            if(cvSubmodule!=null){
                metaRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel(cvSubmodule));
            }else{
                metaRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
            }
        }else{
            metaRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
        }
        //2158 End
    }
    public String getSelectedSubModuleCode(){
        String subModuleCode = "0";
        ComboBoxBean subModuleComboBean = (ComboBoxBean)metaRuleDetailForm.cmbSubmodule.getSelectedItem();
        if(subModuleComboBean!=null &&
                ((subModuleComboBean!=null) && (!subModuleComboBean.getCode().equals("")))){
            subModuleCode = subModuleComboBean.getCode();
        }
        return subModuleCode;
    }
    
    public String getSelectedModuleCode(){
        String moduleCode = "";
        ComboBoxBean moduleComboBean = (ComboBoxBean)metaRuleDetailForm.cmbModule.getSelectedItem();
        if(moduleComboBean!=null &&
                ((moduleComboBean!=null) && (!moduleComboBean.getCode().equals("")))){
            moduleCode = moduleComboBean.getCode();
        }
        return moduleCode;
    }
    
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
    /** Set the Image icons for the tree and check for null.Get the Image icons for
     * Institute Proposal, development Proposal, award and subcontract
     */
    public class MetaRuleTreeRenderer extends DefaultTreeCellRenderer{
        private ImageIcon parentRule,trueRule,falseRule,nextRule;
        private String nodeInfo = EMPTY_STRING;
        private String nextNode, nodeIfTrue, nodeIfFalse,nodeId;
        public MetaRuleTreeRenderer(){
            super();
            java.net.URL parentRuleNode = getClass().getClassLoader().getResource( CoeusGuiConstants.RULE_PARENT_NODE );
            java.net.URL trueRuleNode = getClass().getClassLoader().getResource( CoeusGuiConstants.RULE_TRUE_NODE);
            java.net.URL falseRuleNode = getClass().getClassLoader().getResource( CoeusGuiConstants.NEW_ICON);
            java.net.URL nextRuleNode = getClass().getClassLoader().getResource( CoeusGuiConstants.RULE_NEXT_NODE);
            parentRule = new ImageIcon(parentRuleNode);
            trueRule = new ImageIcon(trueRuleNode);
            falseRule = new ImageIcon(falseRuleNode);
            nextRule = new ImageIcon(nextRuleNode);
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
//            Equals eqNodeId = null;
            Equals eqParent = null;
            MetaRuleDetailBean metaRuleDetailBean  = null;
//            MetaRuleNode selNode = (MetaRuleNode)value;
            Object obj = ((MetaRuleNode)value).getDataObject();
            if( obj instanceof MetaRuleDetailBean ){
                metaRuleDetailBean = ( MetaRuleDetailBean) obj;
                if(metaRuleDetailBean.getParentNodeId().equals("0")){
                    setIcon(parentRule);
                    nodeInfo= "";
                } else {
                    if(cvRuleData!= null && cvRuleData.size() > 0){
                        eqParent = new Equals("nodeId",metaRuleDetailBean.getParentNodeId());
                        CoeusVector cvData  = cvRuleData.filter(eqParent);
                        if(cvData!=null && cvData.size() > 0){
                            MetaRuleDetailBean selectedBean = (MetaRuleDetailBean)cvData.get(0);
                            nextNode = selectedBean.getNextNode();
                            nodeId = metaRuleDetailBean.getNodeId();
                            nodeIfTrue = selectedBean.getNodeIfTrue();
                            nodeIfFalse = selectedBean.getNodeIfFalse();
                            if(nextNode!= null && nextNode.equals(nodeId)) {
                                setIcon(nextRule);
                                nodeInfo = ("(Next)");
                                selectedType = "next";
                            } else if (nodeIfTrue!= null && nodeIfTrue.equals(nodeId)) {
                                setIcon(trueRule);
                                nodeInfo = ("(If True)");
                                selectedType = "true";
                            } else if (nodeIfFalse!= null && nodeIfFalse.equals(nodeId)) {
                                setIcon(falseRule);
                                nodeInfo = ("(If False)");
                                selectedType = "false";
                            }
                        }
                    }
                }
            }else{
                setText((String)((DefaultMutableTreeNode)value).getUserObject());
            }
            setText(nodeInfo+metaRuleDetailBean.getDescription());
            setComponentOrientation(tree.getComponentOrientation());
            return this;
        }
    }
    
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    /*
     * Check if module id provided is either IRB protocol or IACUC protocol
     * @param module id
     * @return true if module is IACUC protocol or IRB protocol
     */
    private boolean checkModule(String moduleId ){
        boolean isValid = false;
        if((moduleId.equals(""+ModuleConstants.PROTOCOL_MODULE_CODE) ||
                             moduleId.equals(""+ModuleConstants.IACUC_MODULE_CODE))){
            isValid = true;
        }
        return isValid;
    }
    // COEUSQA-1724_ Implement validation based on rules in protocols_End
}

