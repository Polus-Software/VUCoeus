/*
 * BudgetModularBaseController.java
 *
 * Created on February 2, 2006, 4:17 PM
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetModularIDCBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.gui.BudgetModularBaseForm;
import edu.mit.coeus.budget.gui.BudgetModularForm;
import edu.mit.coeus.budget.gui.BudgetModularForms;
import edu.mit.coeus.budget.gui.CumulativeBudgetInformationForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  tarique
 */
public class BudgetModularBaseController extends Controller implements ActionListener, ChangeListener{
    
    private BudgetModularBaseForm budgetModularBaseForm;
    private CoeusDlgWindow dlgBudgetModular;
    private static final String WINDOW_TITLE = "Modular Budget for Proposal ";
    private static final int WIDTH = 745;
    private static final int HEIGHT = 442;
    private Vector vecEntireModBudController;
    private static final String SAVE_CHANGES = "budget_saveChanges_exceptionCode.1210";
    private CoeusMessageResources coeusMessageResources;
    private CumulativeBudgetInformationForm cumulativeBudgetInformationForm;
    private QueryEngine queryEngine;
    private BudgetInfoBean budgetInfoBean;
    private Hashtable syncedData;
    private boolean hierarchyMode;
    /** Creates a new instance of BudgetModularBaseController */
    public BudgetModularBaseController(CumulativeBudgetInformationForm
                        cumulativeBudgetInformationForm, BudgetInfoBean budgetInfoBean, Hashtable syncedData) throws CoeusException{
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.cumulativeBudgetInformationForm = cumulativeBudgetInformationForm;
        this.budgetInfoBean = budgetInfoBean;
        this.syncedData = syncedData;
        queryEngine = QueryEngine.getInstance();
        registerComponents();
        postInitComponents();
    }
       /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public Component getControlledUI(){
        return budgetModularBaseForm;
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     */
    public void setFormData(Object data){
        vecEntireModBudController = (Vector)data;
    }
    
    
    /** returns the form data
     * @return the form data
     */
    public Object getFormData(){
        return null;
    }
    
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields(){
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            budgetModularBaseForm.btnOk.setEnabled(false);
            budgetModularBaseForm.btnSync.setEnabled(false);
        }
    }
    
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws CoeusUIException{
        return true;
    }
    
    /** registers GUI Components with event Listeners.
     */
    public void registerComponents(){
        budgetModularBaseForm = new BudgetModularBaseForm();
        java.awt.Component[] components = {budgetModularBaseForm.btnOk, 
            budgetModularBaseForm.btnCancel, budgetModularBaseForm.btnSync};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetModularBaseForm.setFocusTraversalPolicy(traversePolicy);
        budgetModularBaseForm.setFocusCycleRoot(true);
        budgetModularBaseForm.btnOk.addActionListener(this);
        budgetModularBaseForm.btnCancel.addActionListener(this);
        budgetModularBaseForm.btnSync.addActionListener(this);
        budgetModularBaseForm.tbdPnBudgetModular.addChangeListener(this);
        
    }
     public void postInitComponents() throws CoeusException{
        dlgBudgetModular = new edu.mit.coeus.gui.CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgBudgetModular.setResizable(false);
        dlgBudgetModular.setModal(true);
        dlgBudgetModular.setTitle(WINDOW_TITLE + budgetInfoBean.getProposalNumber() +", Version "+budgetInfoBean.getVersionNumber() );
        dlgBudgetModular.getContentPane().add(budgetModularBaseForm);
        dlgBudgetModular.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        dlgBudgetModular.setSize(WIDTH, HEIGHT);
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dlgSize = dlgBudgetModular.getSize();
        dlgBudgetModular.setLocation(screenSize.width/2 - (dlgSize.width/2),
                                        screenSize.height/2 - (dlgSize.height/2));
        dlgBudgetModular.setDefaultCloseOperation(edu.mit.coeus.gui.
                                                CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgBudgetModular.addComponentListener(
        new java.awt.event.ComponentAdapter(){
            public void componentShown(java.awt.event.ComponentEvent e){
                requestDefaultFocus();
            }
        });
        dlgBudgetModular.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent we){
                try{
                    performCancelAction();
                }catch (CoeusUIException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
        
        dlgBudgetModular.addEscapeKeyListener(new javax.swing.AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                try{
                    performCancelAction();
                }catch (CoeusUIException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
    }
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            budgetModularBaseForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            if(source ==budgetModularBaseForm.btnOk){
                performOkAction();
            }else if(source == budgetModularBaseForm.btnCancel){
                
                performCancelAction();
                
            }else if(source == budgetModularBaseForm.btnSync){
                int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("budget_summary_modular_budget_exceptionCode.1122"),
                                            CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                switch( option ) {
                    case (CoeusOptionPane.SELECTION_YES):
                        performSyncAction();
                        break;
                    default:
                        break;
                        
                }
             }
        }catch(CoeusUIException exception){
            
        }catch(CoeusException ce){
            
        }finally{
            budgetModularBaseForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
   
    private void performSyncAction() throws CoeusUIException, CoeusException{
        Controller controller;
        CoeusVector cvDCData = null;
        CoeusVector cvIDCData = null;
        double entireTotalDCFA =0;
        double entireTotalConsFA = 0;
        double entireTotalDC = 0;
        double entireTotalIDC = 0;
        double entireTotalDCNIDC = 0;
        
        syncedData = syncModularBudget(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
        
        for(int index = 0; index < vecEntireModBudController.size(); index++) {
            controller = (Controller)vecEntireModBudController.get(index);
            if(((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.isEditing()){
                ((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.getCellEditor().stopCellEditing();
            }
            CoeusVector dcData  = (CoeusVector)syncedData.get(BudgetModularBean.class);
            CoeusVector iDCData = (CoeusVector)syncedData.get(BudgetModularIDCBean.class);

            controller.setSaveRequired(true);
            
            // Added for COEUSQA-3303 : Error on Modular Budget - start
            // Only sync datas have been to brought froward with acType 'I', so the BudgetModularIDCBean with 'I' actype in query engine will be deleted
            // and acType other than 'I' beans acType will be updated with 'D'
            String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
            Equals eqPeriod = new Equals("budgetPeriod" , new Integer(index+1));
            CoeusVector cvModularIDCData = queryEngine.executeQuery(queryKey,BudgetModularIDCBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            cvModularIDCData = cvModularIDCData.filter(eqPeriod);
            if(cvModularIDCData != null && !cvModularIDCData.isEmpty()){
                for(Object modularIdc : cvModularIDCData){
                    BudgetModularIDCBean modularIDCBean = (BudgetModularIDCBean)modularIdc;
                    if(TypeConstants.INSERT_RECORD.equals(modularIDCBean.getAcType())){
                        queryEngine.delete(queryKey,modularIDCBean);
                    }else{
                        modularIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                        queryEngine.update(queryKey,modularIDCBean);
                    }
                }
            }
            // To maintain unique awRateNumber in all the BudgetModularIDCBean beans in the queryEngine,
            // All the BudgetModularIDCBean beans in the query engine will be fetched and max awRateNumber will be fetched from the list
            // and sync BudgetModularIDCBean beans awRateNumber will be updated with the incremental awRateNumber
            cvModularIDCData = queryEngine.executeQuery(queryKey,BudgetModularIDCBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            cvModularIDCData = cvModularIDCData.filter(eqPeriod);
            if(cvModularIDCData != null && !cvModularIDCData.isEmpty()){
                cvModularIDCData.sort("awRateNumber",true);
                BudgetModularIDCBean modularIDCBean = (BudgetModularIDCBean)cvModularIDCData.get((cvModularIDCData.size()-1));
                int awRateNumber = modularIDCBean.getAwRateNumber()+1;
                CoeusVector cvSyncPeriodModularIDC = iDCData.filter(eqPeriod);
                if(cvSyncPeriodModularIDC != null && !cvSyncPeriodModularIDC.isEmpty()){
                    for(Object modularIdc : cvSyncPeriodModularIDC){
                        BudgetModularIDCBean syncedModularIDCBean = (BudgetModularIDCBean)modularIdc;
                        syncedModularIDCBean.setAwRateNumber(awRateNumber);
                        syncedModularIDCBean.setAcType(TypeConstants.INSERT_RECORD);
                        awRateNumber++;
                    }
                }
            }else if(iDCData != null && !iDCData.isEmpty()){
                CoeusVector cvSyncPeriodModularIDC = iDCData.filter(eqPeriod);
                if(cvSyncPeriodModularIDC != null && !cvSyncPeriodModularIDC.isEmpty()){
                    int awRateNumber = 1;
                    for(Object modularIdc : cvSyncPeriodModularIDC){
                        BudgetModularIDCBean syncedModularIDCBean = (BudgetModularIDCBean)modularIdc;
                        syncedModularIDCBean.setAwRateNumber(awRateNumber);
                        syncedModularIDCBean.setAcType(TypeConstants.INSERT_RECORD);
                        awRateNumber++;
                    }
                }
                
            }
            // Added for COEUSQA-3303 : Error on Modular Budget - start
            
            controller.setModularDataObject(syncedData);
            cvDCData = (CoeusVector)((HashMap)controller.getFormData()).get(BudgetModularBean.class);
            cvIDCData = (CoeusVector)((HashMap)controller.getFormData()).get(BudgetModularIDCBean.class);
            
            entireTotalDCFA = entireTotalDCFA + ((BudgetModularBean)cvDCData.get(0)).getDirectCostFA();
            entireTotalConsFA = entireTotalConsFA + ((BudgetModularBean)cvDCData.get(0)).getConsortiumFNA();
            entireTotalDC = entireTotalDC + ((BudgetModularBean)cvDCData.get(0)).getTotalDirectCost();
            entireTotalIDC = entireTotalIDC + cvIDCData.sum("fundRequested");
            entireTotalDCNIDC = entireTotalDC + entireTotalIDC;
            
            //Case 2260 Start
            ((BudgetModularFormsController)controller).enableDisableComp();
            //Case 2260 End
            
        }
               
        cumulativeBudgetInformationForm.txtTotalDCLessConsFA.setValue(entireTotalDCFA);
        cumulativeBudgetInformationForm.txtTotalConsFA.setValue(entireTotalConsFA);
        cumulativeBudgetInformationForm.txtTotalDirectCosts.setValue(entireTotalDC);
        cumulativeBudgetInformationForm.txtTotalIDC.setValue(entireTotalIDC);
        cumulativeBudgetInformationForm.txtTotalDCNIDC.setValue(entireTotalDCNIDC);
        controller = null;
    }
    private void setModularDCData(CoeusVector cvDCData, CoeusVector cvPeriodBean){
        if(cvPeriodBean!=null&&cvPeriodBean.size()>0){
            for(int index=0;index<cvPeriodBean.size();index++){
                BudgetPeriodBean budgetPeriodBean
                            =(BudgetPeriodBean)cvPeriodBean.get(index);
                BudgetModularBean budgetModularBean
                                    =new BudgetModularBean();
                budgetModularBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
                budgetModularBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
                budgetModularBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                budgetModularBean.setDirectCostFA(0);
                budgetModularBean.setConsortiumFNA(0);
                budgetModularBean.setTotalDirectCost(0);
                budgetModularBean.setAcType(TypeConstants.INSERT_RECORD);
                cvDCData.add(budgetModularBean);
                
            }
            
        }
    }
    private void setModularIDCData(CoeusVector cvIDCData, CoeusVector cvPeriodBean){
        if(cvPeriodBean!=null&&cvPeriodBean.size()>0){
            int rateNumber[] = {1,2,3,4};
            for(int index=0;index<cvPeriodBean.size();index++){
                BudgetPeriodBean budgetPeriodBean
                            =(BudgetPeriodBean)cvPeriodBean.get(index);
                for(int idcIndex = 1; idcIndex <= rateNumber.length; idcIndex++){
                    BudgetModularIDCBean budgetModularIDCBean
                                            = new BudgetModularIDCBean();
                    budgetModularIDCBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
                    budgetModularIDCBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
                    budgetModularIDCBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                    budgetModularIDCBean.setRateNumber(idcIndex);
                    budgetModularIDCBean.setDescription("");
                    budgetModularIDCBean.setIdcRate(0);
                    budgetModularIDCBean.setIdcBase(0);
                    budgetModularIDCBean.setFundRequested(0);
                    budgetModularIDCBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvIDCData.add(budgetModularIDCBean);
                }
            }
            
        }
    }
    private void performOkAction() throws CoeusUIException, CoeusException{
        Controller controller;
        for(int index = 0; index < vecEntireModBudController.size(); index++) {
            controller = (Controller)vecEntireModBudController.get(index);
            if(((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.isEditing()){
                ((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.getCellEditor().stopCellEditing();
            }
            controller.saveFormData();
        }
        //Code commented for bug fix case#3183 - Proposal hierarchy - starts
//        if(isHierarchyMode()){
//             boolean dataChange = false;
//            for(int index = 0; index < vecEntireModBudController.size(); index++) {
//                controller = (Controller)vecEntireModBudController.get(index);
//                if(((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.isEditing()){
//                    ((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.getCellEditor().stopCellEditing();
//                }
//                if(controller.isSaveRequired()){
//                    dataChange = true;
//                    //break;
//                }
//            }
//            if(dataChange){
//                String key = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
//                Hashtable htData = queryEngine.getDataCollection(key);
//                queryEngine.addDataCollection(key, updateBudgetData(htData));
//            }
//        }
        //Code commented for bug fix case#3183 - Proposal hierarchy - ends
        dlgBudgetModular.dispose();
    }
    
    private Hashtable updateBudgetData(Hashtable htData) throws CoeusException{
        final String BUDGET_SERVLET ="/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(htData);
        requester.setFunctionType('u');
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+BUDGET_SERVLET, requester);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage());
        }else{
            return (Hashtable)responderBean.getDataObject();
        }
    }
    
     private void performCancelAction() throws CoeusUIException{
         boolean dataChange = false;
         Controller controller;
         for(int index = 0; index < vecEntireModBudController.size(); index++) {
             controller = (Controller)vecEntireModBudController.get(index);
             if(((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.isEditing()){
                 ((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.getCellEditor().stopCellEditing();
             }
             if(controller.isSaveRequired()){
                 dataChange = true;
                 //break;
             }
         }
         //case : 2261
         //if(dataChange){
         if(dataChange && getFunctionType()!=TypeConstants.DISPLAY_MODE){
         //case : 2261
             int option = CoeusOptionPane.showQuestionDialog(
             coeusMessageResources.parseMessageKey(SAVE_CHANGES),
             CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
             switch( option ) {
                 case (CoeusOptionPane.SELECTION_YES):
                     try{
                         performOkAction();
                     }catch(CoeusUIException ce){
                         CoeusOptionPane.showErrorDialog(ce.getMessage());
                     }catch (CoeusException ex){
                         CoeusOptionPane.showErrorDialog(ex.getMessage());
                     }
                     break;
                 case(CoeusOptionPane.SELECTION_NO):
                     dlgBudgetModular.dispose();
                     
                     break;
                 default:
                     break;
             }
         }else{
             dlgBudgetModular.dispose();
             
         }
     }
     private void requestDefaultFocus(){
         if(getFunctionType() != TypeConstants.DISPLAY_MODE){
             Controller controller = (Controller)vecEntireModBudController.get(0);
             controller.formatFields();
         }else{
             budgetModularBaseForm.btnCancel.requestFocusInWindow();
         }
         
     }
     public void cleanUp(){
         
         budgetModularBaseForm.btnOk.removeActionListener(this);
         budgetModularBaseForm.btnCancel.removeActionListener(this);
         budgetModularBaseForm.btnSync.removeActionListener(this);
         budgetModularBaseForm.tbdPnBudgetModular = null;
         budgetModularBaseForm = null;
     }
    /** saves the Form Data.
     */
    public void saveFormData(){
        
    }
    
    /** displays the Form which is being controlled.
     */
    public void display(){
        dlgBudgetModular.show();
    }
  
    public void stateChanged(javax.swing.event.ChangeEvent e) {
            Component selComp = budgetModularBaseForm.tbdPnBudgetModular.getSelectedComponent();
            double entireTotalDCFA =0;
            double entireTotalConsFA = 0;
            double entireTotalDC = 0;
            double entireTotalIDC = 0;
            double entireTotalDCNIDC = 0;
            Controller controller = null;
            if(selComp instanceof edu.mit.coeus.budget.gui.CumulativeBudgetInformationForm){
                
                if(vecEntireModBudController!= null){
                for(int index = 0; index < vecEntireModBudController.size(); index++) {
                    controller = (Controller)vecEntireModBudController.get(index);
                    if(((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.isEditing()){
                        ((BudgetModularForms)controller.getControlledUI()).tblIndirectCosts.getCellEditor().stopCellEditing();
                    }
                    CoeusVector cvModDcData = (CoeusVector)((HashMap)controller.getFormData()).get(BudgetModularBean.class);
                    CoeusVector cvModIDcData = (CoeusVector)((HashMap)controller.getFormData()).get(BudgetModularIDCBean.class);
                    entireTotalDCFA = entireTotalDCFA + ((BudgetModularBean)cvModDcData.get(0)).getDirectCostFA();
                    entireTotalConsFA = entireTotalConsFA + ((BudgetModularBean)cvModDcData.get(0)).getConsortiumFNA();
                    entireTotalDC = entireTotalDC + ((BudgetModularBean)cvModDcData.get(0)).getTotalDirectCost();
                    entireTotalIDC = entireTotalIDC + cvModIDcData.sum("fundRequested");
                    entireTotalDCNIDC = entireTotalDC + entireTotalIDC;
                    cvModDcData = null;
                    cvModIDcData = null;
                    controller = null;
                }
                }
                cumulativeBudgetInformationForm.txtTotalDCLessConsFA.setValue(entireTotalDCFA);
                cumulativeBudgetInformationForm.txtTotalConsFA.setValue(entireTotalConsFA);
                cumulativeBudgetInformationForm.txtTotalDirectCosts.setValue(entireTotalDC);
                cumulativeBudgetInformationForm.txtTotalIDC.setValue(entireTotalIDC);
                cumulativeBudgetInformationForm.txtTotalDCNIDC.setValue(entireTotalDCNIDC);
                
            }else{
                if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                    budgetModularBaseForm.btnCancel.requestFocusInWindow();
                }else{
                    int selIndex = budgetModularBaseForm.tbdPnBudgetModular.getSelectedIndex();
                    if(vecEntireModBudController !=null){
                        controller = (Controller)vecEntireModBudController.get(selIndex);
                        controller.formatFields();
                        
                    }
                }
            }
      }
    
    /**
     * Getter for property hierarchyMode.
     * @return Value of property hierarchyMode.
     */
    public boolean isHierarchyMode() {
        return hierarchyMode;
    }
    
    /**
     * Setter for property hierarchyMode.
     * @param hierarchyMode New value of property hierarchyMode.
     */
    public void setHierarchyMode(boolean hierarchyMode) {
        this.hierarchyMode = hierarchyMode;
    }
    
 }
