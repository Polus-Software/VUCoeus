/*
 * BusinessRuleDetailController.java
 *
 * Created on October 17, 2005, 4:09 PM
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.gui.BusinessRuleDetailForm;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author  chandrashekara
 */
public class BusinessRuleDetailController extends RuleController 
implements ActionListener,ItemListener{
    private BusinessRuleDetailForm businessRuleDetailForm;
    private static final int TYPE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN =1;
    private CoeusVector cvBusinessRuleData;
    private char functionType;
    private BusinessRuleTableModel businessRuleTableModel;
    private BusinessRuleTableCellRenderer businessRuleTableCellRenderer;
    private RuleBaseBean ruleBaseBean;
    private QueryEngine queryEngine;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    private boolean addRuleRight;
    private boolean modifyRuleRight;
    private boolean deleteRuleRight;
    private Vector rightsData;
    private CoeusVector cvDeletedData;
    
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    private boolean modified;
    private static final char GET_BUSINESS_RULE_DATA = 'K';
    private static final String RULE_SERVLET = "/RuleMaintenanceServlet";
    //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
    private Map hmSubModules = null;    
    private static String EMPTY_SELECTED_COMBO_VALUE = "0";
    private CoeusVector cvFilteredBusinessRuleData;
    //COEUSQA:3500 - End
    /** Creates a new instance of BusinessRuleDetailController */
    public BusinessRuleDetailController(char functionType,RuleBaseBean ruleBaseBean) throws CoeusException{
        this.functionType = functionType;
        this.ruleBaseBean = ruleBaseBean;
        businessRuleDetailForm = new BusinessRuleDetailForm();
        queryEngine = QueryEngine.getInstance();
        queryKey = ruleBaseBean.getUnitNumber();
        registerComponents();
        setFormData(null);
        setTableEditor();
    }
    
    public void display() {
    }
    
    public void formatFields() {
        addRuleRight = ((Boolean)getRightsData().get(0)).booleanValue();
        modifyRuleRight = ((Boolean)getRightsData().get(1)).booleanValue();
        deleteRuleRight = ((Boolean)getRightsData().get(2)).booleanValue();
        businessRuleDetailForm.btnAdd.setEnabled(addRuleRight);
        businessRuleDetailForm.btnDelete.setEnabled(deleteRuleRight);
        if(!modifyRuleRight){
            businessRuleDetailForm.btnAdd.setEnabled(false);
            businessRuleDetailForm.btnModify.setText("View");
            businessRuleDetailForm.btnModify.setMnemonic('V');
            businessRuleDetailForm.btnModify.setFont(CoeusFontFactory.getLabelFont());
            businessRuleDetailForm.btnModify.setMinimumSize(businessRuleDetailForm.btnDelete.getMinimumSize());
        }
    }
    
     public void registerComponents() {
        businessRuleDetailForm.btnAdd.addActionListener(this);
        businessRuleDetailForm.btnModify.addActionListener(this);
        businessRuleDetailForm.btnDelete.addActionListener(this);
        
        businessRuleTableModel = new BusinessRuleTableModel();
        businessRuleTableCellRenderer = new BusinessRuleTableCellRenderer();
        businessRuleDetailForm.tblBusinessRule.setModel(businessRuleTableModel);
        
        //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
        businessRuleDetailForm.cmbModule.addItemListener(this);
        businessRuleDetailForm.cmbSubmodule.addItemListener(this);
        //COEUSQA:3500 - End
    }
     
    private void setTableEditor(){
        JTableHeader tableHeader = businessRuleDetailForm.tblBusinessRule.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            tableHeader.addMouseListener(new ColumnHeaderListener());
            businessRuleDetailForm.tblBusinessRule.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            businessRuleDetailForm.tblBusinessRule.setRowHeight(22);
            businessRuleDetailForm.tblBusinessRule.setSelectionBackground(java.awt.Color.white);
            businessRuleDetailForm.tblBusinessRule.setSelectionForeground(java.awt.Color.black);
            businessRuleDetailForm.tblBusinessRule.setShowHorizontalLines(true);
            businessRuleDetailForm.tblBusinessRule.setShowVerticalLines(true);
            businessRuleDetailForm.tblBusinessRule.setOpaque(false);
            tableHeader.setResizingAllowed(true);
            businessRuleDetailForm.tblBusinessRule.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            
            TableColumn column = businessRuleDetailForm.tblBusinessRule.getColumnModel().getColumn(TYPE_COLUMN);
            column.setPreferredWidth(80);
            column.setResizable(true);
            column.setCellRenderer(businessRuleTableCellRenderer);
            
            
            column =businessRuleDetailForm.tblBusinessRule.getColumnModel().getColumn(DESCRIPTION_COLUMN);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 
            //Increased the width
            column.setPreferredWidth(770);
            column.setResizable(true);
            column.setCellRenderer(businessRuleTableCellRenderer);
    }
    
   
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        cvDeletedData = new CoeusVector();
        cvBusinessRuleData = new CoeusVector();
        cvBusinessRuleData = queryEngine.executeQuery(queryKey,BusinessRuleBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
        populateModuleComboBox();
        //COEUSQA:3500 - End
        businessRuleTableModel.setData(cvBusinessRuleData);             
        if(businessRuleDetailForm.tblBusinessRule.getRowCount() > 0){
            businessRuleDetailForm.tblBusinessRule.setRowSelectionInterval(0,0);
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public java.awt.Component getControlledUI() {
        return businessRuleDetailForm;
    }
    
    public Object getFormData() {
        CoeusVector dataObjects = new CoeusVector();
        CoeusVector dataToServer = new CoeusVector();
        if(modified){
            if(cvDeletedData!= null && cvDeletedData.size() > 0){
                dataObjects.addAll(cvDeletedData);
            }
            if(cvBusinessRuleData!= null && cvBusinessRuleData.size()>0){
                dataObjects.addAll(cvBusinessRuleData);
            }
            
            if(dataObjects!= null && dataObjects.size() > 0){
                for(int index = 0; index < dataObjects.size(); index++ ){
                    BusinessRuleBean businessRuleBean = (BusinessRuleBean)dataObjects.get(index);
                    if(businessRuleBean.getAcType()!= null && !(businessRuleBean.getAcType().equals(null))){
                        dataToServer.addElement(businessRuleBean);
                    }
                }
            }
        }
        return dataToServer;
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        public void mouseClicked(MouseEvent mouseEvent) {
            Point clickedPoint = mouseEvent.getPoint();
            int xPosition = (int)clickedPoint.getX();
            int selectedRow = businessRuleDetailForm.tblBusinessRule.getSelectedRow();
            BusinessRuleBean businessRuleBean = null;
            if(selectedRow!= -1){
                 businessRuleBean=(BusinessRuleBean)cvBusinessRuleData.get(selectedRow);
            }
            int columnIndex = businessRuleDetailForm.tblBusinessRule.getColumnModel().getColumnIndexAtX(xPosition);
            switch (columnIndex) {
                case TYPE_COLUMN:
                    if(sortCodeAsc) {
                        cvBusinessRuleData.sort("ruleType", false);
                        sortCodeAsc = false;
                    }else {
                        //Code already sorted in Descending order. Sort now in Ascending order.
                        cvBusinessRuleData.sort("ruleType", true);
                        sortCodeAsc = true;
                    }
                    break;
                case DESCRIPTION_COLUMN:
                    if(sortCodeAsc) {
                        //Code already sorted in Ascending order. Sort now in Descending order.
                        cvBusinessRuleData.sort("description", false);
                        sortCodeAsc = false;
                    }else {
                        //Code already sorted in Descending order. Sort now in Ascending order.
                        cvBusinessRuleData.sort("description", true);
                        sortCodeAsc = true;
                    }
                    break;
            }
            
            businessRuleTableModel.fireTableDataChanged();
            if(selectedRow!= -1){
            businessRuleDetailForm.tblBusinessRule.setRowSelectionInterval(
                cvBusinessRuleData.indexOf(businessRuleBean),cvBusinessRuleData.indexOf(businessRuleBean));
            businessRuleDetailForm.tblBusinessRule.scrollRectToVisible(
            businessRuleDetailForm.tblBusinessRule.getCellRect(
            cvBusinessRuleData.indexOf(businessRuleBean), TYPE_COLUMN, true));
            }else{
                if(businessRuleDetailForm.tblBusinessRule.getRowCount() > 0){
                    businessRuleDetailForm.tblBusinessRule.setRowSelectionInterval(0,0);
                    businessRuleDetailForm.tblBusinessRule.scrollRectToVisible(
                        businessRuleDetailForm.tblBusinessRule.getCellRect(0,TYPE_COLUMN, true));
                }
            }
        }
    }// End of ColumnHeaderListener.................
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(businessRuleDetailForm.btnAdd)){
                performAddAction();
            }else if(source.equals(businessRuleDetailForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(businessRuleDetailForm.btnModify)){
                performModifyAction();
            }
        }catch (CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
    }
    
    private void performAddAction() throws CoeusException{
        showDefineRuleWindow(TypeConstants.ADD_MODE);
    }
    
    private void performDeleteAction() throws CoeusException{
        int selRow = businessRuleDetailForm.tblBusinessRule.getSelectedRow();
        BusinessRuleBean deletedRuleBean = null;
        MetaRuleDetailBean metaRuleDetailBean = null;
        MetaRuleDetailBean filterdMetaRuleBean = null;
        Equals eqRuleId = null;
        CoeusVector cvFilterMetaRule = null;
        String ruleId= EMPTY_STRING;
        String ruleDescription = EMPTY_STRING;
        
        int optionSelected = CoeusOptionPane.showQuestionDialog(
        "Are you sure you want to delete this record",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
        if(optionSelected == CoeusOptionPane.SELECTION_YES){
            if(selRow!= -1){
                //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
                //deletedRuleBean = (BusinessRuleBean)cvBusinessRuleData.get(selRow);
                String moduleId = getSelectedModuleCode();
                if(EMPTY_SELECTED_COMBO_VALUE.equals(moduleId)){
                    deletedRuleBean = (BusinessRuleBean)cvBusinessRuleData.get(selRow);
                } else{
                    deletedRuleBean = (BusinessRuleBean)cvFilteredBusinessRuleData.get(selRow);
                }
                //COEUSQA:3500 - End             
                ruleId = deletedRuleBean.getRuleId();
                ruleDescription = deletedRuleBean.getDescription();
                CoeusVector cvMetaruleData = queryEngine.executeQuery(
                queryKey,MetaRuleDetailBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvMetaruleData!= null && cvMetaruleData.size() > 0){
                    for(int index=0; index< cvMetaruleData.size(); index++){
                        metaRuleDetailBean = (MetaRuleDetailBean)cvMetaruleData.get(index);
                        eqRuleId = new Equals("ruleId",metaRuleDetailBean.getRuleId());
                        cvFilterMetaRule = cvMetaruleData.filter(eqRuleId);
                        filterdMetaRuleBean = (MetaRuleDetailBean)cvFilterMetaRule.get(0);
                        if(filterdMetaRuleBean.getRuleId().equals(ruleId)){
                            CoeusOptionPane.showInfoDialog("This rule can't be deleted as it is used to define a Meta Rule ");
                            return ;
                        }
                    }
                }
                // Go and delete the record.
                    modified = true;
                    if(deletedRuleBean.getAcType()!= null && deletedRuleBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        cvBusinessRuleData.remove(deletedRuleBean);
                        //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
                        if(!EMPTY_SELECTED_COMBO_VALUE.equals(moduleId)){
                            cvFilteredBusinessRuleData.remove(deletedRuleBean);
                        }
                        //COEUSQA:3500 - End
                    }else{
                        deletedRuleBean.setAcType(TypeConstants.DELETE_RECORD);
                        cvDeletedData.addElement(deletedRuleBean);
                        cvBusinessRuleData.remove(deletedRuleBean);
                        //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
                        if(!EMPTY_SELECTED_COMBO_VALUE.equals(moduleId)){
                            cvFilteredBusinessRuleData.remove(deletedRuleBean);
                        }
                        //COEUSQA:3500 - End
                    }
                    businessRuleTableModel.fireTableRowsDeleted(selRow,selRow);
                    if(selRow!=-1 && selRow!=0){
                        businessRuleDetailForm.tblBusinessRule.setRowSelectionInterval(selRow-1,selRow-1);
                    }else{
                        if(businessRuleDetailForm.tblBusinessRule.getRowCount() > 0){
                            businessRuleDetailForm.tblBusinessRule.setRowSelectionInterval(0,0);
                        }
                    }
                
            }else{
                CoeusOptionPane.showInfoDialog("Select a row to be deleted.");
            }
        }
    }
    
    private void performModifyAction() throws CoeusException{
        showDefineRuleWindow(TypeConstants.MODIFY_MODE);
    }
    
    private void showDefineRuleWindow(char mode) throws CoeusException{
        int selRow = businessRuleDetailForm.tblBusinessRule.getSelectedRow();
        DefineRulesController defineRulesController = null;
        BusinessRuleBean businessRuleBean = null;
        BusinessRuleBean modifyBean = null;
        //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
        String moduleId = getSelectedModuleCode();
        //COEUSQA:3500 - End
        if(selRow== -1 && mode == TypeConstants.MODIFY_MODE){
            CoeusOptionPane.showInfoDialog("Please select a row to modify");
            return ;
        }
        
        if(mode == TypeConstants.MODIFY_MODE){
            //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
            //modifyBean = (BusinessRuleBean)cvBusinessRuleData.get(selRow);
            if(EMPTY_SELECTED_COMBO_VALUE.equals(moduleId)){
                modifyBean = (BusinessRuleBean)cvBusinessRuleData.get(selRow);
            } else{
                modifyBean = (BusinessRuleBean)cvFilteredBusinessRuleData.get(selRow);
            }
            //COEUSQA:3500 - End
        }
        
        if(!modifyRuleRight){
            mode = TypeConstants.DISPLAY_MODE;
        }
        
        defineRulesController = new DefineRulesController(ruleBaseBean , mode);
        defineRulesController.setFormData(modifyBean);
        defineRulesController.display();
        if(defineRulesController.isOkClicked()){
            businessRuleBean = (BusinessRuleBean)defineRulesController.getFormData();
            if(mode == TypeConstants.MODIFY_MODE){
                if(!modifyBean.getDescription().equals(businessRuleBean.getDescription())){
                    queryEngine.removeDataCollection(cvBusinessRuleData);
                    CoeusVector cvData = getBusinessRuleData();
                    queryEngine.addCollection(queryKey,BusinessRuleBean.class,cvData);
                    //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
                    //setFormData(null);
                    cvBusinessRuleData = queryEngine.executeQuery(queryKey,BusinessRuleBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                    String submoduleId = getSelectedSubModuleCode();
                    refreshFormData(moduleId, submoduleId);
                    //COEUSQA:3500 - End
                }
            }else if(mode == TypeConstants.ADD_MODE){
                queryEngine.removeDataCollection(cvBusinessRuleData);
                CoeusVector cvData = getBusinessRuleData();
                queryEngine.addCollection(queryKey,BusinessRuleBean.class,cvData);
                setFormData(null);
            }
        }
    }
    /** Get the latest Business Rules data once the Business Rules are
     *added or Modified. Communicate with the server and get the corresponding 
     *Business Rule data
     *@returns CoeusVector containing Business Rules
     */
    private CoeusVector getBusinessRuleData() throws CoeusException{
        CoeusVector cvData = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_BUSINESS_RULE_DATA );
        requester.setDataObject(ruleBaseBean.getUnitNumber());
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+RULE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            cvData = (CoeusVector)response.getDataObjects();
        }
        return cvData;
    }
    
    /**
     * Getter for property rightsData.
     * @return Value of property rightsData.
     */
    public java.util.Vector getRightsData() {
        return rightsData;
    }
    
    /**
     * Setter for property rightsData.
     * @param rightsData New value of property rightsData.
     */
    public void setRightsData(java.util.Vector rightsData) {
        this.rightsData = rightsData;
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

    //COEUSQA:3500 - Add columns in the Business Rules tab for Applies to and sub module - Start
    /**
     * listens to item state changed event
     * @param itemEvent 
     */
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        Object selectedItem = null;
        try{
            if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
                if(source.equals(businessRuleDetailForm.cmbModule) ){
                    String moduleId = getSelectedModuleCode();
                    populateSubModuleComboBox(moduleId);
                    String submoduleId = getSelectedSubModuleCode();                    
                    refreshFormData(moduleId, submoduleId);
                }
                 if(source.equals(businessRuleDetailForm.cmbSubmodule) ){
                    String moduleId = ((ComboBoxBean)businessRuleDetailForm.cmbModule.getSelectedItem()).getCode();
                    String submoduleId = getSelectedSubModuleCode();                    
                    refreshFormData(moduleId, submoduleId);
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    /**
     * Method to refresh the tblBusinessRule data
     *
     * @param moduleId 
     * @param submoduleId 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    public void refreshFormData(String moduleId, String submoduleId) throws edu.mit.coeus.exception.CoeusException {        
        //If nothing is selected from 'Module', load all module data
        if(EMPTY_SELECTED_COMBO_VALUE.equals(moduleId)){
            cvFilteredBusinessRuleData = cvBusinessRuleData;
        } else{
            Equals eqModuleId = new Equals("moduleCode",moduleId);
            Equals eqSubmoduleId = new Equals("submoduleCode", submoduleId);
            And andModule = new And(eqModuleId, eqSubmoduleId); 
            //If nothing is selected from 'Submodule', load Modules's all submodule data
            if(EMPTY_SELECTED_COMBO_VALUE.equals(submoduleId)){
                cvFilteredBusinessRuleData = cvBusinessRuleData.filter(eqModuleId);
            } else{
                cvFilteredBusinessRuleData = cvBusinessRuleData.filter(andModule);
            }
        }
        businessRuleTableModel.setData(cvFilteredBusinessRuleData);
    } 
   
    /**
     * Populate the module combobox with the module names
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    public void populateModuleComboBox() throws CoeusException{
        CoeusVector cvModule = queryEngine.getDetails(queryKey, ModuleDataBean.class);
        CoeusVector cvModuleData = new CoeusVector();
        if(cvModule!=null){
            cvModuleData.add(EMPTY_STRING);
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
        businessRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
        businessRuleDetailForm.cmbModule.setModel(new DefaultComboBoxModel(cvModuleData));
        
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
    
    /**
     * Populate the submodule combobox with the submodule names
     * @param moduleId 
     */
    public void populateSubModuleComboBox(String moduleId){
        CoeusVector cvSubmodule = (CoeusVector)hmSubModules.get(moduleId);
        if(cvSubmodule!=null){
            businessRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel(cvSubmodule));
        }else{
            businessRuleDetailForm.cmbSubmodule.setModel(new DefaultComboBoxModel());
        }
    }
    
    /**
     * Method to get the selected module code
     * @return 
     */
    public String getSelectedModuleCode(){
        String moduleCode = "0";
        if(!EMPTY_STRING.equals(businessRuleDetailForm.cmbModule.getSelectedItem())){
            ComboBoxBean moduleComboBean = (ComboBoxBean)businessRuleDetailForm.cmbModule.getSelectedItem();
            if(moduleComboBean!=null &&
                    ((moduleComboBean!=null) && (!moduleComboBean.getCode().equals(EMPTY_STRING)))){
                moduleCode = moduleComboBean.getCode();
            }
        }
        return moduleCode;
    }
    
    /**
     * Method to get the selected submodule code
     * @return 
     */
    public String getSelectedSubModuleCode(){
        String subModuleCode = "0";
        ComboBoxBean subModuleComboBean = (ComboBoxBean)businessRuleDetailForm.cmbSubmodule.getSelectedItem();
        if(subModuleComboBean!=null &&
                ((subModuleComboBean!=null) && (!subModuleComboBean.getCode().equals(EMPTY_STRING)))){
            subModuleCode = subModuleComboBean.getCode();
        }
        return subModuleCode;
    }   
    //COEUSQA:3500 - End
    
    public class BusinessRuleTableModel extends AbstractTableModel{
        
        private String colName[] = {"Type", "Description"};
        private Class colClass[] = {String.class, String.class};
        CoeusVector cvBusinessRuleData;
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public void setData(CoeusVector cvBusinessRuleData){
            this.cvBusinessRuleData = cvBusinessRuleData;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvBusinessRuleData== null){
                return 0;
            }else{
                return cvBusinessRuleData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvBusinessRuleData.get(row);
            switch(col){
                case TYPE_COLUMN:
                    return (businessRuleBean.getRuleType());
                case DESCRIPTION_COLUMN:
                    return businessRuleBean.getDescription();
            }
            return EMPTY_STRING;
            
        }
    }
    
    public class BusinessRuleTableCellRenderer extends DefaultTableCellRenderer{
        private CoeusTextField txtComponent;
        private String typeValue;
        public BusinessRuleTableCellRenderer(){
            txtComponent = new CoeusTextField();
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
        }
        
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            
            switch(col){
                case TYPE_COLUMN:
                     if(functionType == TypeConstants.DISPLAY_MODE ){
                        txtComponent.setBackground(disabledBackground);
                        txtComponent.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }else{
                        txtComponent.setBackground(java.awt.Color.white);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                     
                    String type = "";
                    if(value!= null){
                        type = (String)value;
                        if(type.trim().equals(ROUTING)){
                            txtComponent.setText(VAL_ROUTING);
                            return txtComponent;
                        }else if(type.trim().equals(VALIDATION)){
                            txtComponent.setText(VAL_VALIDATION);
                            return txtComponent;
                        }else if(type.trim().equals(NOTIFICATION)){
                            txtComponent.setText(VAL_NOTIFICATION);
                            return txtComponent;
                        }
                        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 
                        else if(type.trim().equals(QUESTION)){
                            txtComponent.setText(VAL_QUESTION);
                            return txtComponent;
                        }
                      //Added for Coeus 4.3 Routing enhancement -PT ID:2785 
                    }else{
                        txtComponent.setText(EMPTY_STRING);
                    }
                case DESCRIPTION_COLUMN:
                     if(functionType == TypeConstants.DISPLAY_MODE ){
                        txtComponent.setBackground(disabledBackground);
                        txtComponent.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }else{
                        txtComponent.setBackground(java.awt.Color.white);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                     
                    txtComponent.setText(value.toString());
            }
            return txtComponent;
        }
        
    }
    
}
