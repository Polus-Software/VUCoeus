/*
 * SelectBudgetPersonsController.java
 *
 * Created on October 29, 2003, 3:42 PM
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTable;
import javax.swing.AbstractAction;
import javax.swing.ListSelectionModel;

import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.budget.gui.SelectBudgetPersonsForm;
import edu.mit.coeus.budget.gui.PersonnelBudgetDetailsForm;
import edu.mit.coeus.budget.gui.PersonnelBudgetDetailTable;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.AppletServletCommunicator;

import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.NotEquals;
import java.util.List;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */

public class SelectBudgetPersonsController extends Controller implements TypeConstants, ActionListener ,ListSelectionListener  {
    
    private SelectBudgetPersonsForm selectBudgetPersonsForm;
    private BudgetDetailBean budgetDetailBean;
    private CoeusVector vecBudgetPersonnelDetailsBean;
    private BudgetPersonsBean budgetPersonsBean;
    private CoeusVector vecBudgetPersonsBean;
    private String queryKey;
    private QueryEngine queryEngine;
    
    private PersonnelBudgetDetailController personnelBudgetDetailController ;
    /** Array containing lits of selected rows in the table */
    private int selectedRow [];
    
    /** String constant containing Messages */
    private static final String SELECT_A_PERSON = "budget_personnelBudget_exceptionCode.1206";
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** Parent instane */
    private Component parent;
    /** Model Parameter */
    boolean modal;
    //Added for bug fixed for case #2354 start 1
    private static final char GET_VALID_JOB_CODES_FOR_CE = 't'; 
    private static final char GET_JOB_CODE_VALIDATION_ENABLED = 'v';   
    //Added for bug fixed for case #2354 end 1
    /** Creates a new instance of SelectBudgetPersonsController
     * @param controller PersonnelBudgetDetailController instance
     */
    public SelectBudgetPersonsController(PersonnelBudgetDetailController controller ) {
        super();
        this.parent = CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        budgetDetailBean = null;
        personnelBudgetDetailController = controller;
        initialiseController();
    }
    /** Creates a new instance of SelectBudgetPersonsController
     * @param parent Component parent frame instance
     * @param modal boolean if <true> Model window
     * @param budgetDetailBean BudgetDetailBean instance
     */
    public SelectBudgetPersonsController(Component parent, boolean modal,BudgetDetailBean budgetDetailBean) {
        super(budgetDetailBean);
        this.budgetDetailBean = budgetDetailBean;
        this.parent = parent;
        this.modal =  true;
        initialiseController();
        
    }
    
    /**
     * Initialisation of Controller
     */
    
    public void initialiseController() {
        selectBudgetPersonsForm =  new SelectBudgetPersonsForm((Component) parent,modal);
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        vecBudgetPersonsBean = new CoeusVector();
        registerComponents();
        formatFields();
    }
    
    /**
     * Displays the Form which is being controlled.
     */
    public void display() {
        ((SelectBudgetPersonsForm) getControlledUI()).dlgSelectBudgetPersonsForm.setVisible(true);
    }
    
    /** Format the Fields */
    public void formatFields() {
        CoeusVector vecTableData = new  CoeusVector();
        ((SelectBudgetPersonsForm)getControlledUI()).setTableData(vecTableData);
        ((SelectBudgetPersonsForm) getControlledUI()).btnCancel.requestFocus();
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller here
     *         it is selectBudgetPersonsForm instance
     */
    
    public java.awt.Component getControlledUI() {
        return selectBudgetPersonsForm;
    }
    
    /** defined to returns the form data
     * @return returns the form data
     */
    
    public Object getFormData() {
        return null;
    }
    
    /**
     * registers GUI Components with event Listeners.
     */
    
    public void registerComponents() {
        
        ((SelectBudgetPersonsForm) getControlledUI()).btnOK.addActionListener(this);
        ((SelectBudgetPersonsForm) getControlledUI()).btnCancel.addActionListener(this);
        
        ((JTable) getTableInstance()).setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ((JTable) getTableInstance()).addMouseListener(new MouseAdapter() {
            
            public void mouseClicked(MouseEvent mouseEvent) {
                // Object source = mouseEvent.getSource();
                if(mouseEvent.getClickCount() == 2) {
                    addPersonsToBudgetDetail();
                }
            }
            public void mousePressed(MouseEvent e) {
                // Object source = e.getSource();
                
            }
            // implements java.awt.event.MouseListener
            public void mouseReleased(MouseEvent e) {
                
            }
            // implements java.awt.event.MouseListener
            public void mouseEntered(MouseEvent e) {
            }
            // implements java.awt.event.MouseListener
            public void mouseExited(MouseEvent e) {
            }
            
            
        });
        
        
        ((SelectBudgetPersonsForm) getControlledUI()).dlgSelectBudgetPersonsForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    close();
                    
                }
            }
        });
        
        
        ((CoeusDlgWindow) ((SelectBudgetPersonsForm) getControlledUI()).dlgSelectBudgetPersonsForm).addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                close();
            }
        });
        
        
        ((SelectBudgetPersonsForm) getControlledUI()).dlgSelectBudgetPersonsForm.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                close();
                
            }
        });
        
        
        // Travel all the components while pressing tab button
        java.awt.Component[] components = {
            ((SelectBudgetPersonsForm) getControlledUI()).btnOK,
            ((SelectBudgetPersonsForm) getControlledUI()).btnCancel,
            ((JTable) getTableInstance())
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        selectBudgetPersonsForm.pnlSelectBudgetPerson.setFocusTraversalPolicy(traversePolicy);
        selectBudgetPersonsForm.pnlSelectBudgetPerson.setFocusCycleRoot(true);
        
    }
    
    /** To Save the Form dat on click of Ok */
    public void saveFormData() {
        
        
    }
    
    /** To Close the window */
    public void close() {
        ((SelectBudgetPersonsForm) getControlledUI()).dlgSelectBudgetPersonsForm.setVisible(false);
        
    }
    
    /** to Set the Form Data
     * @param data Object
     */
    public void setFormData(Object data) {
        
        if(data == null)  {
            return;
        }
        
        
        budgetDetailBean = new BudgetDetailBean();
        budgetDetailBean = (BudgetDetailBean ) data;
        
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean =  new BudgetPersonnelDetailsBean();
        budgetPersonnelDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
        budgetPersonnelDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
        budgetPersonnelDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
        budgetPersonnelDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
        
        queryKey = budgetDetailBean.getProposalNumber()+budgetDetailBean.getVersionNumber();
        
        try {
            
            Equals equalsActype = new Equals("acType",null);
            NotEquals notequalsActype = new NotEquals("acType",TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            vecBudgetPersonnelDetailsBean = QueryEngine.getInstance().executeQuery(queryKey,budgetPersonnelDetailsBean.getClass(),actypeBoth);
            
            budgetPersonsBean = new BudgetPersonsBean();
            budgetPersonsBean.setProposalNumber(budgetPersonnelDetailsBean.getProposalNumber());
            budgetPersonsBean.setVersionNumber(budgetPersonnelDetailsBean.getVersionNumber());
            vecBudgetPersonsBean = QueryEngine.getInstance().executeQuery(queryKey,budgetPersonsBean.getClass(),actypeBoth);
            //Added for bug fixed for case #2354 start 2
            vecBudgetPersonsBean = (CoeusVector)getValidPersonForCE(vecBudgetPersonsBean,budgetDetailBean);
            //Added for bug fixed for case #2354 end 2
            setTableDate(vecBudgetPersonsBean);
            for (int index=0; index < vecBudgetPersonsBean.size();index++) {
                budgetPersonsBean  = (BudgetPersonsBean) vecBudgetPersonsBean.get(index);
            }
            
        } catch ( Exception e) {
            e.getMessage();
            
        }
        display();
    }
    
    /** Set the Data to the Table in the SelectBudgetPersonsForm
     * @param vecBudgetPersonsBean CoeusVector containing BudgetPersonBeans
     */
    
    private void setTableDate(CoeusVector vecBudgetPersonsBean) {
        ((SelectBudgetPersonsForm)getControlledUI()).setTableData(vecBudgetPersonsBean);
        getTableInstance().getSelectionModel().addListSelectionListener(this);
    }
    
    /** Get the Table Instance of table in the SelectBudgetPersonsForm
     * @return JTable handler
     */
    
    private javax.swing.JTable getTableInstance() {
        return ((SelectBudgetPersonsForm) getControlledUI()).tblPersonsList;
    }
    
    /*
     * Get the Table Model of table in the SelectBudgetPersonsForm
     */
    
    /** To get The Handle on the Table Model
     * @return TableModel handler
     */
    private javax.swing.table.TableModel getTableModel() {
        return (((SelectBudgetPersonsForm) getControlledUI()).selectBudgetPersonsTableModel);
    }
    
    /*
     * Get the Controller from which this Controller is initiated
     * PersonnelBudgetDetailController is passed on to this controller to call method in those Controllers
     */
    
    /** set the ParentController from which its being invoked
     * @param personnelBudgetDetailController instance of PersonnelBudgetDetailController
     */
    public void setParentController(PersonnelBudgetDetailController personnelBudgetDetailController) {
        this.personnelBudgetDetailController = personnelBudgetDetailController;
    }
    
    
    /** Defining the validate method of Controller interface  for validation check on the UI form
     * @throws CoeusUIException throws CoeusUIException
     * @return if <true> validation is passed
     */
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
        return false;
    }
    
    /** The actionPerformed method for responding to button actions
     * @param actionEvent Object actionEvent
     */
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source != null && source.equals(((SelectBudgetPersonsForm) getControlledUI()).btnOK)){
            
            if(selectedRow != null && selectedRow.length > 0) {
                addPersonsToBudgetDetail();
            } else {
                //if(personnelBudgetDetailController.getTableInstance().getRowCount() > 0)  {
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(SELECT_A_PERSON));
                //} else
                //  close();
            }
            
        }
        
        //CANCEL ACTION
        if (source != null && source.equals(((SelectBudgetPersonsForm) getControlledUI()).btnCancel)){
            close();
        }
    }
    
    /**
     * Add the Selected BudgetPersons to PersonnelBudgetDetail
     */
    
    private void addPersonsToBudgetDetail() {
        for(int row =0;row < selectedRow.length ;row ++) {
            BudgetPersonnelDetailsBean oldBudgetPersonnelDetailsBean = null;
            BudgetPersonnelDetailsBean budgetPersonnelDetailsBean  = new BudgetPersonnelDetailsBean();
            BudgetPersonsBean budgetPersonsBean =(BudgetPersonsBean) vecBudgetPersonsBean.get(selectedRow[row]);
            
            CoeusVector vecBudgetPersonnelDetailBeans =  personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean();
            
            if(vecBudgetPersonnelDetailBeans != null && vecBudgetPersonnelDetailBeans.size() > 0) {
                oldBudgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailBeans.get(0);
                budgetPersonnelDetailsBean.setProposalNumber(oldBudgetPersonnelDetailsBean.getProposalNumber());
                budgetPersonnelDetailsBean.setVersionNumber(oldBudgetPersonnelDetailsBean.getVersionNumber());
                budgetPersonnelDetailsBean.setBudgetPeriod(oldBudgetPersonnelDetailsBean.getBudgetPeriod());
                budgetPersonnelDetailsBean.setLineItemNumber(oldBudgetPersonnelDetailsBean.getLineItemNumber());
                
                budgetPersonnelDetailsBean.setStartDate(budgetDetailBean.getLineItemStartDate());
                budgetPersonnelDetailsBean.setEndDate(budgetDetailBean.getLineItemEndDate());
                
            } else {
                budgetPersonnelDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                budgetPersonnelDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                budgetPersonnelDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                budgetPersonnelDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                budgetPersonnelDetailsBean.setStartDate(
                budgetDetailBean.getLineItemStartDate());
                budgetPersonnelDetailsBean.setEndDate(
                budgetDetailBean.getLineItemEndDate());
            }
            int maxExistingPersonId = 1;
            
            
            
            
            if(vecBudgetPersonnelDetailBeans !=  null &&
            vecBudgetPersonnelDetailBeans.size() > 0) {
                vecBudgetPersonnelDetailBeans.sort("personNumber",false);
                maxExistingPersonId = ((BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailBeans.get(0)).getPersonNumber();
                maxExistingPersonId+=1;
            }
            vecBudgetPersonnelDetailBeans.sort("personNumber",true);
            
            budgetPersonnelDetailsBean.setFullName( budgetPersonsBean.getFullName());
            budgetPersonnelDetailsBean.setJobCode( budgetPersonsBean.getJobCode());
            budgetPersonnelDetailsBean.setPersonId(budgetPersonsBean.getPersonId());
            // Added by chandra to fix the #1102 - start
            budgetPersonnelDetailsBean.setSalaryRequested(budgetPersonsBean.getCalculationBase());
            // Added by chandra to fix the #1102 - end
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
            //budgetPersonnelDetailsBean.setPeriodType("Cycle");
            try {
                //to set the active first period type in the elements
                String [][] periodArray = fetchActivePeriodTypes();
                budgetPersonnelDetailsBean.setPeriodType(periodArray[0][0]);
            } catch (CoeusClientException ex) {
                ex.printStackTrace();
            }
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
            budgetPersonnelDetailsBean.setPersonNumber(maxExistingPersonId);
            budgetPersonnelDetailsBean.setPercentCharged(0);
            budgetPersonnelDetailsBean.setPercentEffort(0);
            budgetPersonnelDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
            budgetPersonnelDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
            
            budgetPersonnelDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
            
            if(personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean() != null) {
                
                personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean().add(budgetPersonnelDetailsBean);
                
                ((PersonnelBudgetDetailTable) personnelBudgetDetailController.getTableInstance()).setTableData(personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean());
                personnelBudgetDetailController.getParentTableModel().fireTableRowsInserted(personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean().size(),personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean().size());
                personnelBudgetDetailController.getTableInstance().setRowSelectionInterval(personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean().size()-1, personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean().size()-1);
                
                String quantity = personnelBudgetDetailController.getNumOfQuantity(personnelBudgetDetailController.getVecBudgetPersonnelDetailsBean());
                ((PersonnelBudgetDetailsForm) personnelBudgetDetailController.getControlledUI()).txtQuantity.setText(quantity);
                
            }
            
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(budgetPersonnelDetailsBean);
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
        }
        
        close();
    }
    
    /**  Method implementation of ListSelectionListener
     * @param listSelectEvent Object listSelectEvent
     */
    
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectEvent) {
        selectedRow = getTableInstance().getSelectedRows();
        
    }
   //Added for bug fixed for case #2354 start 3
    /** Get the Valid Job Code or parameter value based on param string.
     *@param param value to pass data
     *@param functionType Char to execute procedures
     *@return List containg data
     *@throws Exception if any errors occur.
     */
    
    private List getValidJobCodeForCE(String param,char functionType) throws CoeusException{
        final String VALID_JOB_CODES ="/BudgetMaintenanceServlet";
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ VALID_JOB_CODES;
        List cvParam = null;
        RequesterBean requester = new RequesterBean();
        if(functionType == GET_JOB_CODE_VALIDATION_ENABLED){
            requester.setFunctionType(GET_JOB_CODE_VALIDATION_ENABLED);
            param = TypeConstants.JOB_CODE_VALIDATION_ENABLED;
        }else if(functionType == GET_VALID_JOB_CODES_FOR_CE){
            requester.setFunctionType(GET_VALID_JOB_CODES_FOR_CE);
        }
        requester.setDataObject(param);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            cvParam = (CoeusVector)responder.getDataObject();
            
        }
        return cvParam;
    }
    private List getValidPersonForCE(CoeusVector cvBudgetPersons,BudgetDetailBean budgetDetailBean)
        throws CoeusException{
        List cvValidCE;
        List cvValidPersonForCE = new CoeusVector();
        int checkValidEnabled = 0;
        if(cvBudgetPersons != null && cvBudgetPersons.size() > 0){
            cvValidCE = getValidJobCodeForCE("parameter",GET_JOB_CODE_VALIDATION_ENABLED);
            if(cvValidCE != null && cvValidCE.size() > 0){
                checkValidEnabled = Integer.parseInt(((String)cvValidCE.get(0)));
                if(checkValidEnabled == 0){
                    return cvBudgetPersons;
                }
                cvValidCE = getValidJobCodeForCE(budgetDetailBean.getCostElement(), GET_VALID_JOB_CODES_FOR_CE);
                if(cvValidCE != null){
                    String validJobCode = null;
                    for(int index = 0; index < cvValidCE.size(); index ++){
                        validJobCode = (String)cvValidCE.get(index);
                        if(cvBudgetPersons != null){
                            for(int rIndex = 0; rIndex < cvBudgetPersons.size(); rIndex ++){
                                BudgetPersonsBean budgetPersonsBean
                                    = (BudgetPersonsBean)cvBudgetPersons.get(rIndex);
                                if(budgetPersonsBean.getJobCode().equals(validJobCode)){
                                    cvValidPersonForCE.add(budgetPersonsBean);
                                    // Case# 3013:Jobcode enabled causes only 1 person to be returned if persons have same jobcode
//                                    break;
                                }
                            }
                        }
                    }//end for
                }//end if
            }//end if
            if(checkValidEnabled == 1){
                return cvValidPersonForCE;
            }
            return cvBudgetPersons;
        }//end if
        return cvBudgetPersons;
    }
    //Added for bug fixed for case #2354 end 3
}
