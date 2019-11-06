/*
 * CostElementDetailController.java
 *
 * Created on November 19, 2004, 3:16 PM
 */
/* PMD check performed, and commented unused imports and variables 
 * on 24-JAN-2011 by Maharaja Palanichamy
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.CostElementDetailForm;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.bean.BudgetCategoryBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.TypeConstants;

import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;


/**
 *
 * @author  jinum
 */
public class CostElementDetailController implements ActionListener, ItemListener{
    
    /** Holds an instance of <CODE>CostElementDetailForm</CODE> */
    private CostElementDetailForm costElementDetailForm;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgCostElementDetail;
    private CoeusMessageResources coeusMessageResources;
    private CostElementsBean costElementsBean;
    
    //setting up the width and height of the screen
    private static final int WIDTH = 560;
    private static final int HEIGHT = 145;
    public static final int ADD = 0;
    public static final int MODIFY = 1;
    private char functionType;
    private static final String EMPTY = "";
    private static final String ADD_TITLE = "Add Cost Element";
    private static final String MODIFY_TITLE = "Modify Cost Element";
    private static final String CANCEL_CONFIRMATION = "adminAward_exceptionCode.1353";
    private static final String BUDGET_CATEGORY_IS_NULL = "costElement_exceptionCode.1457";
    private static final String FLAG_ON = "On";
    private static final String FLAG_OFF = "Off";
    
    public boolean dataModified = false;
    
    //For the table data and for the selected data
    private CoeusVector cvBudgetCategory;
    private CoeusVector cvCostElement;
    //Case #2161 start 1
    private boolean rights;
    //Case #2161 end 1
    /** Creates a new instance of CostElementDetailController */
    public CostElementDetailController(CoeusAppletMDIForm mdiForm, char mode) {
        this.mdiForm = mdiForm;
        setFunctionType(mode);
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
        formatFields();
    }
    
    /**
     * registering the components
     * @return void
     */
    public void registerComponents() {
        
        //Add listeners to all the buttons
        costElementDetailForm = new CostElementDetailForm();
        
        costElementDetailForm.btnOK.addActionListener(this);
        costElementDetailForm.btnCancel.addActionListener(this);
        costElementDetailForm.cmbBudgetCategory.addItemListener(this);
        costElementDetailForm.cmbOnOffCampusFlag.addItemListener(this);
        costElementDetailForm.chkActiveFlag.addActionListener(this);
    }
    
    /**
     * To set the components before opening the screen
     * @return void
     */
    public void postInitComponents(){
        if(getFunctionType() == TypeConstants.ADD_MODE){
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
            Component[] components = { costElementDetailForm.txtCostElement, costElementDetailForm.txtArDescription,costElementDetailForm.cmbBudgetCategory,costElementDetailForm.cmbOnOffCampusFlag,costElementDetailForm.chkActiveFlag,costElementDetailForm.btnOK,costElementDetailForm.btnCancel};
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            costElementDetailForm.setFocusTraversalPolicy(traversePolicy);
            costElementDetailForm.setFocusCycleRoot(true);
        }else{
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
            Component[] components = { costElementDetailForm.txtArDescription,costElementDetailForm.cmbBudgetCategory,costElementDetailForm.cmbOnOffCampusFlag,costElementDetailForm.chkActiveFlag,costElementDetailForm.btnOK,costElementDetailForm.btnCancel};
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            costElementDetailForm.setFocusTraversalPolicy(traversePolicy);
            costElementDetailForm.setFocusCycleRoot(true);
        }
        
        
        dlgCostElementDetail = new CoeusDlgWindow(mdiForm);
        dlgCostElementDetail.setResizable(false);
        dlgCostElementDetail.setModal(true);
        dlgCostElementDetail.getContentPane().add(costElementDetailForm);
        dlgCostElementDetail.setFont(CoeusFontFactory.getLabelFont());
        dlgCostElementDetail.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgCostElementDetail.getSize();
        dlgCostElementDetail.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgCostElementDetail.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performCancelOperation();
                return;
            }
        });
        dlgCostElementDetail.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgCostElementDetail.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                performCancelOperation();
                return;
            }
        });

        dlgCostElementDetail.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
    }
    
    /**
     * Format the Fields or Controls.
     * @return void
     **/
    public void formatFields() {
//      setting up the title and it is getting up from the basewindow
        //Bug fix id 1725 done by shiji - step1: start  
        costElementDetailForm.txtCostElement.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,8));
        //Bug fix id 1725 - step1: end
        costElementDetailForm.txtArDescription.setDocument(new LimitedPlainDocument(200));
        if(getFunctionType() == TypeConstants.ADD_MODE){
            dlgCostElementDetail.setTitle(ADD_TITLE);
        }else{
            dlgCostElementDetail.setTitle(MODIFY_TITLE);
            costElementDetailForm.txtCostElement.setEditable(false);
        }
    }

    /** sets the form view mode.
     * @param functionType Possible Values:
     * Display Mode = 'D'
     * Modify Mode = 'M'
     * New Mode = 'N'
     */
    public final void setFunctionType(char functionType) {
        this.functionType = functionType;
    }

    /** returns the function Type
     * @return function Type
     */
    public final char getFunctionType() {
        return functionType;
    }
    
    /**
     * Display the Dialog
     * @return CostElementsBean
     **/
    public CostElementsBean display() {
        //Case #2161 start 2
        if(!isRights()){
            costElementDetailForm.btnOK.setEnabled(isRights());
            costElementDetailForm.cmbBudgetCategory.setEnabled(isRights());
            costElementDetailForm.cmbOnOffCampusFlag.setEnabled(isRights());
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
            costElementDetailForm.chkActiveFlag.setEnabled(isRights());
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
            costElementDetailForm.txtArDescription.setEditable(isRights());
            costElementDetailForm.txtArDescription.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            
        }
        //Case #2161 end 2
        dlgCostElementDetail.setVisible(true);
        return this.costElementsBean;
    }
    
    /**
     * setting up the default focus
     * @return void
     */
    public void requestDefaultFocus(){
        // For bug fix #1597 
        if(getFunctionType() == TypeConstants.ADD_MODE){
            costElementDetailForm.txtCostElement.requestFocus();
        } else {
            costElementDetailForm.btnCancel.requestFocus();
        }
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source != null){
            if( source.equals(costElementDetailForm.btnCancel)) {
                performCancelOperation();
            }else if( source.equals(costElementDetailForm.btnOK) ){
                performOKOperation();
            }
        }
    }
    
    /**
     * To get the ControlledUI
     * @return java.awt.Component
     **/
    public java.awt.Component getControlledUI() {
        return costElementDetailForm;
    }
    
    /**
     * To save the Form's data
     * @throws edu.mit.coeus.exception.CoeusException
     * @return void
     **/
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * To get the Form's data
     * @return Object
     **/
    public Object getFormData() {
        return costElementDetailForm;
    }
    
    /**
     * To set the form data
     * @param cvBudgetCategory CoeusVector
     * @param cvCostElement CoeusVector
     * @param costElementsBean CostElementsBean
     * @return void
     **/
    public void setFormData(CoeusVector cvBudgetCategory, CoeusVector cvCostElement, CostElementsBean costElementsBean) throws edu.mit.coeus.exception.CoeusException {
        try{
            if(cvBudgetCategory != null && cvCostElement != null){
                this.cvBudgetCategory = cvBudgetCategory;
                this.cvCostElement = cvCostElement;
                CoeusVector onOffVector = new CoeusVector();
                ComboBoxBean bean = new ComboBoxBean("0",FLAG_ON);
                onOffVector.add(bean);
                bean = new ComboBoxBean("1",FLAG_OFF);
                onOffVector.add(bean);

                costElementDetailForm.cmbOnOffCampusFlag.setModel(new DefaultComboBoxModel(onOffVector));

                ComboBoxBean emptyBean = new ComboBoxBean("-1", "");
                cvBudgetCategory.add(0, emptyBean);
                costElementDetailForm.cmbBudgetCategory.setModel(new DefaultComboBoxModel(cvBudgetCategory));
                costElementDetailForm.cmbBudgetCategory.setShowCode(true);
                if(getFunctionType() == TypeConstants.ADD_MODE){
                    this.costElementsBean = new CostElementsBean();
                    costElementDetailForm.cmbBudgetCategory.setSelectedIndex(0);
                    costElementDetailForm.cmbOnOffCampusFlag.setSelectedIndex(0);
                    //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
                    costElementDetailForm.chkActiveFlag.setSelected(true);
                    //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
                }else{
                    this.costElementsBean = costElementsBean;
                    costElementDetailForm.txtCostElement.setText(this.costElementsBean.getCostElement());
                    costElementDetailForm.txtArDescription.setText(this.costElementsBean.getDescription());
                    if(this.costElementsBean.getBudgetCategoryCode() != -1){
                        BudgetCategoryBean budgetCategoryBean =  (BudgetCategoryBean)search(this.costElementsBean);
                        costElementDetailForm.cmbBudgetCategory.setSelectedItem(budgetCategoryBean);
                    }else{
                        costElementDetailForm.cmbBudgetCategory.setSelectedIndex(0);
                    }
                    if(this.costElementsBean.getCampusFlag().trim().equals(FLAG_ON)){
                        costElementDetailForm.cmbOnOffCampusFlag.setSelectedIndex(0);
                    }else{
                        costElementDetailForm.cmbOnOffCampusFlag.setSelectedIndex(1);
                    }
                    //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
                    // To get the status of the cost element and set the value in the form
                    if(this.costElementsBean.getActive() != "" && "Y".equals(this.costElementsBean.getActive())){
                        costElementDetailForm.chkActiveFlag.setSelected(true);
                    }else{
                        costElementDetailForm.chkActiveFlag.setSelected(false);
                    }
                    //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
                }
                dataModified = false;
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * To search an object(CostElementsBean) in the CoeusVector(cvBudgetCategory)
     * @param data Object
     * @return Object
     **/
    private Object search(Object object){
        if(object != null && object.getClass().getName().equals("edu.mit.coeus.budget.bean.CostElementsBean")){
            for(int i=0;i<cvBudgetCategory.size();i++){
                String a = ((ComboBoxBean)cvBudgetCategory.elementAt(i)).getCode();
                String b = ""+((CostElementsBean)object).getBudgetCategoryCode();
                if(a.equals(b)){
                    return cvBudgetCategory.elementAt(i);
                }
            }
        }
        return null;
    }
    
    /**
     * To check whether the current CostElementBean with the same CostElement
     * property exists in the vector.
     * @return boolean
     **/
    private boolean isCostElementExists(){
        if(costElementsBean != null && cvCostElement != null){
            for(int i=0;i<cvCostElement.size();i++){
                String beanProperty = ((CostElementsBean)cvCostElement.elementAt(i)).getCostElement();
                String formProperty = costElementDetailForm.txtCostElement.getText().trim();
                if(beanProperty.equals(formProperty)){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * performing the validation
     * @return boolean
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }

    /**
     * performing the update operation
     * @return void
     */
    private void performOKOperation(){
        if(costElementDetailForm.txtCostElement.getText() == null || costElementDetailForm.txtCostElement.getText().trim().equals(EMPTY)){
            String mesg = coeusMessageResources.parseMessageKey("costElement_exceptionCode.1451");
            CoeusOptionPane.showErrorDialog(mesg);
            setRequestFocusInThread(costElementDetailForm.txtCostElement);
            return;
        }else if(getFunctionType() == TypeConstants.ADD_MODE && isCostElementExists()){
            String mesg = coeusMessageResources.parseMessageKey("costElement_exceptionCode.1453");
            CoeusOptionPane.showErrorDialog(mesg);
            setRequestFocusInThread(costElementDetailForm.txtCostElement);
            return;
        }
        //Bug fix id 1725 done by shiji - step2 : start  
        /*if(getFunctionType() == TypeConstants.ADD_MODE && costElementDetailForm.txtCostElement.getText().trim().length() < 6){
            String mesg = coeusMessageResources.parseMessageKey("costElement_exceptionCode.1452");
            CoeusOptionPane.showErrorDialog(mesg);
            setRequestFocusInThread(costElementDetailForm.txtCostElement);
            return;
        }*/
        //Bug fix id 1725 - step2 : end
        if(costElementDetailForm.txtArDescription.getText() == null || costElementDetailForm.txtArDescription.getText().trim().equals(EMPTY)){
            String mesg = coeusMessageResources.parseMessageKey("costElement_exceptionCode.1454");
            CoeusOptionPane.showErrorDialog(mesg);
            setRequestFocusInThread(costElementDetailForm.txtArDescription);
            return;
        }
        ComboBoxBean obj = (ComboBoxBean)costElementDetailForm.cmbBudgetCategory.getSelectedItem();
        if(obj == null || obj.getCode() == null || obj.getCode().trim().equals(EMPTY) || obj.getCode().trim().equals("-1")){
            costElementDetailForm.cmbBudgetCategory.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(BUDGET_CATEGORY_IS_NULL));
            return;
        }
        if(getFunctionType() == TypeConstants.ADD_MODE){
            this.costElementsBean.setCostElement(costElementDetailForm.txtCostElement.getText().trim());
            this.costElementsBean.setAcType(TypeConstants.INSERT_RECORD);
            
        }else{
            this.costElementsBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        String description = costElementsBean.getDescription();
        String modifiedDescription = costElementDetailForm.txtArDescription.getText().trim();
        if( (description != null && modifiedDescription != null 
            && modifiedDescription.trim().length() != description.trim().length()) 
            || (description == null && modifiedDescription != null 
            && !modifiedDescription.trim().equals(EMPTY)) ){
                dataModified = true;
        }
        this.costElementsBean.setDescription(modifiedDescription);
        this.costElementsBean.setBudgetCategoryCode(Integer.parseInt(((ComboBoxBean)costElementDetailForm.cmbBudgetCategory.getSelectedItem()).getCode()));   
        this.costElementsBean.setBudgetCategoryDescription(((ComboBoxBean)costElementDetailForm.cmbBudgetCategory.getSelectedItem()).getDescription());   
        int index = costElementDetailForm.cmbOnOffCampusFlag.getSelectedIndex();
        if(index == 0){
            this.costElementsBean.setCampusFlag(FLAG_ON);
            this.costElementsBean.setOnOffCampusFlag(true);
        }else{
            this.costElementsBean.setCampusFlag(FLAG_OFF);
            this.costElementsBean.setOnOffCampusFlag(false);
        }
        
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
        String activestatus = costElementsBean.getActive();
        boolean select = costElementDetailForm.chkActiveFlag.isSelected();
        String modifiedactivestatus;
        if(select){
            modifiedactivestatus = "Y";
        }else{
            modifiedactivestatus = "N";
        }
         
        if((activestatus != null && modifiedactivestatus != null) && (!activestatus.trim().equals(modifiedactivestatus.trim()))) {
                dataModified = true;
        }
        
        if(select){
            this.costElementsBean.setActive("Y");
        }else{
           this.costElementsBean.setActive("N");
        }
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        close();
    }

    /**
     * performing the close operation
     * @return void
     */
    private void close(){
        cvBudgetCategory.removeElementAt(0);
        dlgCostElementDetail.dispose();
    }

    /**
     * performing the cancel operation
     * @return void
     */
    private void performCancelOperation(){
        if(getFunctionType() == TypeConstants.ADD_MODE){
            if(dataModified || (costElementDetailForm.txtArDescription.getText() != null 
            && !costElementDetailForm.txtArDescription.getText().trim().equals(EMPTY)) 
            || (costElementDetailForm.txtCostElement.getText() != null 
            && !costElementDetailForm.txtCostElement.getText().trim().equals(EMPTY))){
                    
                String mesg = coeusMessageResources.parseMessageKey(CANCEL_CONFIRMATION);
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    performOKOperation();
                } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    this.costElementsBean = null;
                    close();
                } 
            }else{
                this.costElementsBean = null;
                close();
            }
        }else{
            String description = costElementsBean.getDescription();
            String modifiedDescription = costElementDetailForm.txtArDescription.getText();
            if(dataModified || (description != null && modifiedDescription != null 
            && modifiedDescription.trim().length() != description.trim().length()) 
            || (description == null && modifiedDescription != null 
            && !modifiedDescription.trim().equals(EMPTY)) ){
                String mesg = coeusMessageResources.parseMessageKey(CANCEL_CONFIRMATION);
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    performOKOperation();
                } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    dataModified = false;
                    close();
                }
            }else{
                close();
            }
        }
    }
    public void itemStateChanged(ItemEvent itemEvent) {
        dataModified = true;
    }    
    
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    //Case #2161 start 3
    /**
     * Getter for property rights.
     * @return Value of property rights.
     */
    public boolean isRights() {
        return rights;
    }
    
    /**
     * Setter for property rights.
     * @param rights New value of property rights.
     */
    public void setRights(boolean rights) {
        this.rights = rights;
    }
    //Case #2161 end 3
}
