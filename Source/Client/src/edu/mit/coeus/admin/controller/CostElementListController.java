/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables 
 * on 24-JAN-2011 by Maharaja Palanichamy
/*
 * CostElementListController.java
 *
 * Created on Dec 1, 2004, 6:05 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.CostElementListForm;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.utils.saveas.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.budget.bean.BudgetCategoryBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.saveas.SaveAsDialog;

import java.beans.*;
//import javax.swing.JComponent;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
//import java.util.HashMap;
import java.util.Hashtable;
//import java.util.Observer;
//import java.util.Vector;
import javax.swing.table.*;

//import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.user.gui.UserPreferencesForm;
//import edu.mit.coeus.utils.query.Equals;



/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author jinu
 */
public class CostElementListController extends AdminController implements ActionListener, VetoableChangeListener{
    /** holds Award List instance to be controlled. */
    private CostElementListForm costElementListForm;
    private CoeusAppletMDIForm mdiForm;
    private CostElementTableModel costElementTableModel;
    
    private final int CODE_COLUMN = 0;
    private final int DESCRIPTION_COLUMN = 1;
    private final int BUDGET_CATEGORY_COLUMN = 2;
    private final int FLAG = 3;
    //COEUSQA-1414 - Allow schools to indicate if cost element is still active
    //Constant to hold active value
    private final int ACTIVE_COLUMN = 4;
    
    private final String FLAG_ON = "On";
    private final String FLAG_OFF = "Off";
    private final String EMPTY = "";
    private final String SELECT_A_ROW = "search_exceptionCode.1119";
    private final String CANCEL_CONFIRMATION = "adminAward_exceptionCode.1353";
    private final String FUNC_NOT_IMPL = "Functionality not implemented";
    private final String UPDATE_CAMPUS_FLAG_ON_CONFIRMATION = "costElement_exceptionCode.1455";
    private final String UPDATE_CAMPUS_FLAG_OFF_CONFIRMATION = "costElement_exceptionCode.1456";
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources;
    
    private final String GET_SERVLET = "/BudgetMaintenanceServlet";
    
    private ChangePassword changePassword;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations -Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    private CoeusVector cvCostElement;
    private CoeusVector cvCategory;
    public CostElementsBean costElementsBean;
    
    private boolean hasRight = true;
    private boolean dataModified = false;
    private boolean closed = false;
    //bug fix for cost element not getting saved when modify operation is performed on a newly added cost element - step 1
    private boolean isSaved=true;
    // step 1 : end
    
    /** Creates a new instance of InstituteProposalBaseWindowController */
    public CostElementListController(CoeusAppletMDIForm mdiForm) {
        try{
            this.mdiForm = mdiForm;
            coeusMessageResources = CoeusMessageResources.getInstance();
            initComponents();
            registerComponents();
            setFormData(null);
            setColumnData();
            formatFields();
            costElementListForm.display();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * To initializes the components before opening the screen
     * @return void
     */
    private void initComponents(){
        try{
            costElementListForm = new CostElementListForm("Maintain Cost Element", mdiForm);
            costElementTableModel = new CostElementTableModel();
            costElementListForm.initComponents();
            costElementListForm.tblCostElements.setModel(costElementTableModel);
        }catch (Exception exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            exception.printStackTrace();
        }
    }
    
    /**
     * registering the components
     * @return void
     */
    public void registerComponents() {
        costElementListForm.addVetoableChangeListener(this);
        costElementListForm.mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        // Setting listener for the Tool bar buttons
        costElementListForm.btnAddCostElement.addActionListener(this);
        costElementListForm.btnModifyCostElement.addActionListener(this);
        costElementListForm.btnCategory.addActionListener(this);
        costElementListForm.btnOnCampus.addActionListener(this);
        costElementListForm.btnOffCampus.addActionListener(this);
        costElementListForm.btnSave.addActionListener(this);
        costElementListForm.btnSaveas.addActionListener(this);
        costElementListForm.btnClose.addActionListener(this);
        
        //Setting the listener for the menu items
        costElementListForm.mnuItmInbox.addActionListener(this);
        costElementListForm.mnuItmClose.addActionListener(this);
        costElementListForm.mnuItmSave.addActionListener(this);
        costElementListForm.mnuItmSaveas.addActionListener(this);
        costElementListForm.mnuItmSortCostElement.addActionListener(this);
        costElementListForm.mnuItmChangePassword.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        costElementListForm.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        costElementListForm.mnuItmPreferences.addActionListener(this);
        costElementListForm.mnuItmExit.addActionListener(this);
        //Case 2110 Start
        costElementListForm.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        
        costElementListForm.mnuItmAddCostElement.addActionListener(this);
        costElementListForm.mnuItmModifyCostElement.addActionListener(this);
        costElementListForm.mnuItmCategory.addActionListener(this);
        costElementListForm.mnuItmOnCampus.addActionListener(this);
        costElementListForm.mnuItmOffCampus.addActionListener(this);
        
        costElementListForm.tblCostElements.addMouseListener( new CostElementModifyAdapter());

    }
    
    /**
     * To set the form data
     * @param data Object
     *
     * @return void
     **/
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        
        Hashtable htData  = getCostElement();
        if(htData != null){
            cvCostElement = new CoeusVector();
            cvCategory = new CoeusVector();
            try{
                hasRight = ((Boolean)htData.get(KeyConstants.AUTHORIZATION_CHECK)).booleanValue();
                cvCostElement = (CoeusVector)htData.get(CostElementsBean.class);
                cvCategory = (CoeusVector)htData.get(BudgetCategoryBean.class);
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
    /**
     * Setting up the column data
     * @return void
     **/
    private void setColumnData(){
        JTableHeader tableHeader = costElementListForm.tblCostElements.getTableHeader();
        
        costElementListForm.tblCostElements.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.addMouseListener(new ColumnHeaderListener());
        // setting up the table columns
        TableColumn column = costElementListForm.tblCostElements.getColumnModel().getColumn(CODE_COLUMN);
//        column.setMinWidth(60);
        column.setPreferredWidth(60);
        costElementListForm.tblCostElements.setRowHeight(22);
        
        column = costElementListForm.tblCostElements.getColumnModel().getColumn(DESCRIPTION_COLUMN);
//        column.setMinWidth(475);
        column.setPreferredWidth(440);
        
        column = costElementListForm.tblCostElements.getColumnModel().getColumn(BUDGET_CATEGORY_COLUMN);
//        column.setMinWidth(400);
        column.setPreferredWidth(372);
        
        column = costElementListForm.tblCostElements.getColumnModel().getColumn(FLAG);
//        column.setMinWidth(50);
        column.setPreferredWidth(50);
        
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
        column = costElementListForm.tblCostElements.getColumnModel().getColumn(ACTIVE_COLUMN);
        column.setPreferredWidth(60);
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        
        costElementListForm.tblCostElements.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
    }
    
    /**
     * Format the Fields or Controls.
     * @return void
     **/
    public void formatFields() {
        if(!hasRight){
            costElementListForm.btnAddCostElement.setEnabled(false);
            costElementListForm.btnModifyCostElement.setEnabled(false);
            costElementListForm.btnCategory.setEnabled(false);
            costElementListForm.btnOnCampus.setEnabled(false);
            costElementListForm.btnOffCampus.setEnabled(false);
            costElementListForm.btnSave.setEnabled(false);
            costElementListForm.btnSaveas.setEnabled(false);
            
            //Setting the listener for the menu items
            //            costElementListForm.mnuItmInbox.setEnabled(false);
            //            costElementListForm.mnuItmClose.setEnabled(false);
            costElementListForm.mnuItmSave.setEnabled(false);
            costElementListForm.mnuItmSaveas.setEnabled(false);
            //            costElementListForm.mnuItmSortCostElement.setEnabled(false);
            //            costElementListForm.mnuItmChangePassword.setEnabled(false);
            //            costElementListForm.mnuItmPreferences.setEnabled(false);
            
            costElementListForm.mnuItmAddCostElement.setEnabled(false);
            costElementListForm.mnuItmModifyCostElement.setEnabled(false);
            costElementListForm.mnuItmCategory.setEnabled(false);
            costElementListForm.mnuItmOnCampus.setEnabled(false);
            costElementListForm.mnuItmOffCampus.setEnabled(false);
            
        }
    }
    /** Displays Cost Element List. */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.COST_ELEMENT_FRAME_TITLE, costElementListForm);
            mdiForm.getDeskTopPane().add(costElementListForm);
            costElementListForm.setSelected(true);
            costElementListForm.setVisible(true);
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    /**
     * Gets the CostElement Data
     * @return Hashtable
     */
    private Hashtable getCostElement()throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('Y');//"GET_COST_ELEMENT_AND_BUDGET_CATEGORY"
        Hashtable data=null;
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + GET_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean!= null){
            if(!responderBean.isSuccessfulResponse()){
                throw new CoeusException(responderBean.getMessage(), 1);
            }else{
                data = (Hashtable)responderBean.getDataObject();
            }
        }
        return data;
    }
    
    /**
     * To get the ControlledUI
     * @return java.awt.Component
     **/
    public java.awt.Component getControlledUI() {
        return costElementListForm;
    }
    
    /**
     * To get the Form's data
     * @return Object
     **/
    public Object getFormData() {
        return cvCostElement;
    }
    
    /**
     * To save the Form's data
     * @return void
     **/
    public void saveFormData() throws CoeusException {
        try{
            if(cvCostElement != null){
                
                RequesterBean requesterBean = new RequesterBean();
                requesterBean.setFunctionType('Z');//UPDATE_COST_ELEMENT = 'Z'
                requesterBean.setDataObject(cvCostElement);
                AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
                appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + GET_SERVLET);
                appletServletCommunicator.setRequest(requesterBean);
                appletServletCommunicator.send();
                ResponderBean responderBean = appletServletCommunicator.getResponse();
                if(responderBean!= null){
                    if(!responderBean.isSuccessfulResponse()){
                        throw new CoeusException(responderBean.getMessage(), 1);
                    }else{
                        dataModified = false;
                        //bug fix for cost element not getting saved when modify operation is performed on a newly added cost element -step 6
                        isSaved=true;
                        // step 6 : end
                        Hashtable data = (Hashtable)responderBean.getDataObject();
                        
                        cvCostElement = new CoeusVector();
                        cvCategory = new CoeusVector();
                        if(data != null){
                            cvCostElement = (CoeusVector)data.get(CostElementsBean.class);
                            cvCategory = (CoeusVector)data.get(BudgetCategoryBean.class);
                        }
                        costElementTableModel.fireTableDataChanged();
                        if(costElementsBean != null){
                            int index = searchIndex(costElementsBean);
                            if(index != -1){
                                costElementListForm.tblCostElements.setRowSelectionInterval(index, index);
                                costElementListForm.tblCostElements.scrollRectToVisible(
                                costElementListForm.tblCostElements.getCellRect(index ,0, true));
                            }
                        }
//                        
//                        if(cvCostElement.size() > 0)
//                            costElementListForm.tblCostElements.setRowSelectionInterval(0, 0);

                    }
                }
            }
        }catch(CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * To validate data
     * @return boolean
     **/
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    /**
     * To search the index of an object(CostElementsBean) in the CoeusVector(cvCostElement)
     * @param data Object
     * @return int
     **/
    private int searchIndex(Object object){
        if(object != null && object.getClass().getName().equals("edu.mit.coeus.budget.bean.CostElementsBean")){
            for(int i=0;i<cvCostElement.size();i++){
                if(((CostElementsBean)cvCostElement.elementAt(i)).getCostElement().equals(((CostElementsBean)object).getCostElement())){
                    return i;
                }
            }
        }
        return -1;
    }

    public void actionPerformed(ActionEvent actionEvent){
        try{
            costElementListForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

            Object source = actionEvent.getSource();
            if(source.equals(costElementListForm.btnAddCostElement) ||
            source.equals(costElementListForm.mnuItmAddCostElement)){
                blockEvents(true);
                performAddOperation();
                blockEvents(false);
            }else if(source.equals(costElementListForm.btnModifyCostElement) ||
            source.equals(costElementListForm.mnuItmModifyCostElement)){
                blockEvents(true);
                performModifyOperation();
                blockEvents(false);
            }else if(source.equals(costElementListForm.btnCategory)
            || source.equals(costElementListForm.mnuItmCategory)){
                blockEvents(true);
                performModifyCategoryOperation();
                blockEvents(false);
            }else if(source.equals(costElementListForm.btnOnCampus) ||
            source.equals(costElementListForm.mnuItmOnCampus)){
                blockEvents(true);
                performModifyCampusOperation(FLAG_ON);
                blockEvents(false);
            }else if(source.equals(costElementListForm.btnOffCampus) ||
            source.equals(costElementListForm.mnuItmOffCampus)){
                blockEvents(true);
                performModifyCampusOperation(FLAG_OFF);
                blockEvents(false);
            }else if(source.equals(costElementListForm.btnSave) ||
            source.equals(costElementListForm.mnuItmSave)){
                blockEvents(true);
                saveFormData();
                blockEvents(false);
            }else if(source.equals(costElementListForm.btnSaveas)||
            source.equals(costElementListForm.mnuItmSaveas)){
                blockEvents(true);
                performSaveAsOperation();
                blockEvents(false);
            }else if(source.equals(costElementListForm.btnClose) ||
            source.equals(costElementListForm.mnuItmClose)){
                performCloseOperation();
            }else if(source.equals(costElementListForm.mnuItmInbox)){
                blockEvents(true);
                showInboxDetails();
                blockEvents(false);
            }else if(source.equals(costElementListForm.mnuItmExit)){
                blockEvents(true);
                exitApplication();
                blockEvents(false);
                //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(costElementListForm.mnuItmDelegations)){
                blockEvents(true);
                displayUserDelegation();
                blockEvents(false); 
                //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(costElementListForm.mnuItmPreferences)){
                blockEvents(true);
                showPreference();
                blockEvents(false);
            }else if(source.equals(costElementListForm.mnuItmChangePassword)){
                blockEvents(true);
                showChangePassword();
                blockEvents(false);
            }//Case 2110 Start
            else if(source.equals(costElementListForm.mnuItmCurrentLocks)){
                blockEvents(true);
                showLocksForm();
                blockEvents(false);
            } //Case 2110 End           
            else {
                CoeusOptionPane.showInfoDialog(FUNC_NOT_IMPL);
            }
        }catch (CoeusException coeusException){
               coeusException.printStackTrace(); 
               CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
                exception.printStackTrace();
                CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            costElementListForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * To perform the add operation
     * @return void
     **/
    private void performAddOperation(){
        //bug fix for cost element not getting saved when modify operation is performed on a newly added cost element - step 2
        isSaved=false;
        // step 2: end
        try{
            if(cvCategory != null){
                CostElementDetailController costElementDetailController = new CostElementDetailController(mdiForm, TypeConstants.ADD_MODE);
                costElementDetailController.setFormData(cvCategory,cvCostElement, null);
                //Case #2161 start 1
                costElementDetailController.setRights(hasRight);
                //Case #2161 end 1
                CostElementsBean costElementsBeanNew = costElementDetailController.display();
                if(costElementsBeanNew != null){
                    cvCostElement.add(costElementsBeanNew);
                    //bug fix for cost element not getting saved when modify operation is performed on a newly added cost element : step 7
                    costElementsBean = costElementsBeanNew;
                    //step 7 : end
                    dataModified = true;
                    costElementTableModel.fireTableDataChanged();
                    int index = searchIndex(costElementsBeanNew);
                    if(index != -1){
                        costElementListForm.tblCostElements.setRowSelectionInterval(index, index);
                        costElementListForm.tblCostElements.scrollRectToVisible(
                        costElementListForm.tblCostElements.getCellRect(index ,0, true));
                    }
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * To perform the modify operation
     * @return void
     **/
    private void performModifyOperation(){
        try{
            int rowIndex = costElementListForm.tblCostElements.getSelectedRow();
            if (rowIndex < 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protoBaseWin_exceptionCode.1051"));//please select a row to modify
                return;
            }
            if (rowIndex >= 0) {
                costElementsBean = (CostElementsBean)cvCostElement.get(rowIndex);
                if(costElementsBean != null && cvCategory != null){
                    CostElementDetailController costElementDetailController = new CostElementDetailController(mdiForm, TypeConstants.MODIFY_MODE);
                    costElementDetailController.setFormData(cvCategory,cvCostElement,costElementsBean);
                    //Case #2161 start 2
                     costElementDetailController.setRights(hasRight);
                     //Case #2161 end 3
                    costElementsBean = costElementDetailController.display();
                    if(costElementsBean != null && costElementDetailController.dataModified){
                        dataModified = true;
                        //bug fix for cost element not getting saved when modify operation is performed on a newly added cost element - step 3
                        if(isSaved) {
                            costElementsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }else if(!isSaved) {
                            costElementsBean.setAcType(TypeConstants.INSERT_RECORD);
                        }
                        //step 3 : end
                        costElementTableModel.fireTableDataChanged();
                        int index = searchIndex(costElementsBean);
                        if(index != -1){
                            costElementListForm.tblCostElements.setRowSelectionInterval(index, index);
                            costElementListForm.tblCostElements.scrollRectToVisible(
                            costElementListForm.tblCostElements.getCellRect(index ,0, true));
                        }
                    }
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * To perform Modify Category
     * @return void
     **/
    private void performModifyCategoryOperation(){
        try{
            int rowIndex = costElementListForm.tblCostElements.getSelectedRow();
            if (rowIndex < 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW));
                return;
            }
            if (rowIndex >= 0) {
                costElementsBean = (CostElementsBean)cvCostElement.get(rowIndex);
                if(costElementsBean != null && cvCategory != null){
                    AddBudgetCategoryController addBudgetCategoryController = new AddBudgetCategoryController(mdiForm);
                    addBudgetCategoryController.setFormData(cvCategory);
                    BudgetCategoryBean bean = addBudgetCategoryController.display();
                    if(bean != null){
                        //bug fix for cost element not getting saved when modify operation is performed on a newly added cost element - step 4
                        if(isSaved){
                        costElementsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }else if(!isSaved) {
                            costElementsBean.setAcType(TypeConstants.INSERT_RECORD);
                        }
                        //step 4 : end
                        costElementsBean.setBudgetCategoryCode(Integer.parseInt(bean.getCode()));
                        costElementsBean.setBudgetCategoryDescription(bean.getDescription());
                        costElementTableModel.fireTableDataChanged();
                        dataModified = true;
                        int index = searchIndex(costElementsBean);
                        if(index != -1){
                            costElementListForm.tblCostElements.setRowSelectionInterval(index, index);
                            costElementListForm.tblCostElements.scrollRectToVisible(
                            costElementListForm.tblCostElements.getCellRect(index ,0, true));
                        }
                    }
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * To perform the Modify Campus Operation
     * @return void
     **/
    private void performModifyCampusOperation(String campus){
        int rowIndex = costElementListForm.tblCostElements.getSelectedRow();
        if (rowIndex < 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW));
            return;
        }
        if (rowIndex >= 0) {
            costElementsBean = (CostElementsBean)cvCostElement.get(rowIndex);
            if(costElementsBean != null){
                
                String mesg = EMPTY;
                if(campus.trim().equals(FLAG_ON)){
                    if(costElementsBean.isOnOffCampusFlag())
                        return;
                    mesg = coeusMessageResources.parseMessageKey(UPDATE_CAMPUS_FLAG_ON_CONFIRMATION);
                }else{
                    if(!costElementsBean.isOnOffCampusFlag())
                        return;
                    mesg = coeusMessageResources.parseMessageKey(UPDATE_CAMPUS_FLAG_OFF_CONFIRMATION);
                }
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    //bug fix for cost element not getting saved when modify operation is performed on a newly added cost element - step 5
                    if(isSaved) {
                        costElementsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }else if(!isSaved) {
                        costElementsBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                    //step 5 : end
                    costElementsBean.setCampusFlag(campus);
                    if(campus.trim().equals(FLAG_ON))
                        costElementsBean.setOnOffCampusFlag(true);
                    else
                        costElementsBean.setOnOffCampusFlag(false);
                    costElementTableModel.fireTableDataChanged();
                    dataModified = true;
                } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    return;
                } else{
                    return;
                }
                int index = searchIndex(costElementsBean);
                if(index != -1){
                    costElementListForm.tblCostElements.setRowSelectionInterval(index, index);
                    costElementListForm.tblCostElements.scrollRectToVisible(
                    costElementListForm.tblCostElements.getCellRect(index ,0, true));
                }
            }
        }
    }
    
    /**
     * To perform SaveAs operation
     * @return void
     **/
    private void performSaveAsOperation(){
        SaveAsDialog saveAsDialog = new SaveAsDialog(costElementListForm.tblCostElements);
    }
    
    /**
     * To perform close operation
     * @return void
     **/
    private void performCloseOperation() throws PropertyVetoException {
        if(dataModified){
            String mesg = coeusMessageResources.parseMessageKey(CANCEL_CONFIRMATION);
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                try{
                    saveFormData();
                }catch (CoeusException coeusException) {
                    throw new PropertyVetoException(EMPTY, null);
                }
            } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                int i =0;
            } else if(selectedOption == CoeusOptionPane.SELECTION_CANCEL){
                int i =0;
                throw new PropertyVetoException(EMPTY, null);
            } else{
            }
        }
        closed = true;
        mdiForm.removeFrame(CoeusGuiConstants.COST_ELEMENT_FRAME_TITLE);
        costElementListForm.doDefaultCloseAction();
    }
    
    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    //Case 2110 Start To get the Current Locks of the user
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }//Case 2110 End
    
    /**
     * Method used to close the application after confirmation.
     */
    public void exitApplication() throws PropertyVetoException {
        String message = coeusMessageResources.parseMessageKey(
        "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            performCloseOperation();
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** Display the Preferences
     */
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
    
    /** Display the inbox details
     */
    private void showInboxDetails(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
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
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            performCloseOperation();
        }
    }
    
    /** This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","costElement" },
            {"1","description" },
            {"2","budgetCategoryDescription" },
            {"3","campusFlag" },
            {"4","active" }
        };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvCostElement != null && cvCostElement.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvCostElement).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    costElementTableModel.fireTableRowsUpdated(
                    0, costElementTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    /**
     * This is an inner class represents the table model for the Award Basis
     * screen table
     **/
    public class CostElementTableModel extends AbstractTableModel {
        
        // represents the column names of the table
        private String colName[] = {"Code","Description","Budget Category","Flag","Active"};
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class,String.class, String.class, String.class};
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        
        /**
         * To check whether the table cell is editable or not
         * @param row int
         * @param col int
         * @return boolean
         **/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            if (cvCostElement == null){
                return 0;
            } else {
                return cvCostElement.size();
            }
        }
        
        /**
         * To set the data for the model.
         * @param cvAwardBasis CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvCostElementParam) {
            cvCostElement = cvCostElementParam;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
            CostElementsBean costElementsBean =
            (CostElementsBean)cvCostElement.get(rowIndex);
            if (costElementsBean != null) {
                switch(columnIndex) {
                    case CODE_COLUMN:
                        return costElementsBean.getCostElement();
                    case DESCRIPTION_COLUMN:
                        return costElementsBean.getDescription();
                    case BUDGET_CATEGORY_COLUMN:
                        return costElementsBean.getBudgetCategoryDescription();
                    case FLAG:
                        return costElementsBean.getCampusFlag();
                    //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
                    case ACTIVE_COLUMN:
                        return costElementsBean.getActive();
                    //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
                }
            }
            return EMPTY;
        }
        
        /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
    }
    // supporting class to display ModifyCostElements on
    // double clicking of any cost element row.
    class CostElementModifyAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
//            SponsorContactBean bean = (SponsorContactBean)cvContactsData.get(selRow);

            if ( me.getClickCount() == 2) {
                try{
                    int rowIndex = costElementListForm.tblCostElements.getSelectedRow();
                    if (rowIndex < 0) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protoBaseWin_exceptionCode.1051"));//please select a row to modify
                        return;
                    }
                    if (rowIndex >= 0) {
                        costElementsBean = (CostElementsBean)cvCostElement.get(rowIndex);
                        if(costElementsBean != null && cvCategory != null){
                            CostElementDetailController costElementDetailController = new CostElementDetailController(mdiForm, TypeConstants.MODIFY_MODE);
                            costElementDetailController.setFormData(cvCategory,cvCostElement,costElementsBean);
                            //Case #2161 start 3
                            costElementDetailController.setRights(hasRight);
                            //Case #2161 end 3
                            costElementsBean = costElementDetailController.display();
                            if(costElementsBean != null && costElementDetailController.dataModified){
                                dataModified = true;
                                costElementTableModel.fireTableDataChanged();
                                int index = searchIndex(costElementsBean);
                                if(index != -1){
                                    costElementListForm.tblCostElements.setRowSelectionInterval(index, index);
                                    costElementListForm.tblCostElements.scrollRectToVisible(
                                    costElementListForm.tblCostElements.getCellRect(index ,0, true));
                                }
                            }
                        }
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }
    
}

