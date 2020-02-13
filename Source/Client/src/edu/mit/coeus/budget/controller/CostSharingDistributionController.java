/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author ranjeeva
 */
 /*
  * PMD check performed, and commented unused imports and variables on 10-AUGUST-2011
  * by Maharaja Palanichamy
  */


package edu.mit.coeus.budget.controller;

import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.budget.gui.SubAwardCostSharingForm;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.ProposalCostSharingBean;
import edu.mit.coeus.budget.gui.CostSharingDistributionForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.gui.event.BeanEvent;
//import edu.mit.coeus.utils.dbengine.DBException;
import java.util.*;
import java.text.*;

/**
 * CostSharingDistributionController.java
 * Created on December 4, 2003, 3:44 PM
 * @author ranjeeva
 */
public class CostSharingDistributionController extends Controller implements ActionListener {
    
    /** Parent Component instance */
    private Component parent;
    /** Parameter For Model Form Window */
    boolean modal;
    
    /** CoeusMessageResources instance for Messages */
    private CoeusMessageResources coeusMessageResources;
    /** Query Engine instance */
    private QueryEngine queryEngine;
    /** instance of BudgetInfoBean */
    private BudgetInfoBean budgetInfoBean;
    
    /** instance of queryKey value equals ProposalNumber plus VersionNumber
     */
    private String queryKey;
    /** Constant String for ACTYPE value */
    private static final String ACTYPE = "acType";
    
    /** String constant for No cost Sharing details */
    private static final String NO_COST_SHARING ="costSharingDistribution__exceptionCode.1512" ;   //"There is no Cost Sharing for this version of the budget";
    
    /** String constant for delete confirmation */
    private static final String CONFRIM_DELETE = "costSharingDistribution__exceptionCode.1513";  //"Are you sure you want to delete this row? ";
    
    /** String constant for save confirmation */
    private static final String CONFRIM_SAVE = "budget_saveChanges_exceptionCode.1210";  //"Do you want to save Changes ?";
    
    /** String constant for row selection while deletion */
    private static final String MESSAGE_SELECT_A_ROW = "orgIDCPnl_exceptionCode.1097"; //"Please Select a row to delete";
    
    /** String constant for checking total amount entry */
    private static String CHECK_AMOUNT_MESSAGE ="costSharingDistribution__exceptionCode.1506"; //"Please check amount, the total amount from the distribution list should be equal to ";
    
    /** String constant for invalid fiscal year entry */
    private static String INVALID_FISCAL_YEAR ="costSharingDistribution__exceptionCode.1507"; //"Please enter a valid fiscal year for cost sharing row ";
    
    /** String constant for fiscal year entry */
    private static String ENTER_FISCAL_YEAR = "costSharingDistribution__exceptionCode.1508"; //"Please enter a fiscal year for cost sharing row ";
    
    /** String constant for Source Account entry */
    private static String CHECK_SOURCEACCOUNT ="costSharingDistribution__exceptionCode.1509"; //"Please enter a source account for cost sharing row ";
    
    /** String constant constants if fiscal year retrival is failed */
    private static String FISCALYEARFAIL ="Failed to retrieve FiscalYear \n Please Contact Administrator ";
    
    /** String constant for duplicate row entry */
    private static String DUPLICATE_ENTRY = "costSharingDistribution__exceptionCode.1510"; //"A row duplicates another. \n Enter different Fiscal Year or Source Account";
    
    /** String constant for valid amount check */
    private static String INVALID_AMOUNT = "costSharingDistribution__exceptionCode.1511"; //"Please enter an amount in row ";
    
    /** String constant for empty string "" */
    private static final String EMPTYSTRING = "";
    
    /** CostSharingDistributionForm form instance */
    public CostSharingDistributionForm costSharingDistributionForm;
    
    /** Vector of BudgetPeriodBean instance */
    private CoeusVector vecBudgetPeriodBean;
    
    /** Vector of ProposalCostSharingBean instance */
    private CoeusVector vecProposalCostSharingBean;
    
    /** Vector of deleted ProposalCostSharingBean instance */
    private CoeusVector vecDeletedProposalCostSharingBean;
    
    /** String constant for determining end month for fiscal year */
    private static final String FISCAL_YEAR_END_MONTH = "1998-06-30"; //id_current_fiscal_year_end_date = 1998-06-30
    
    /** SimpleDateFormat  instance */
    private SimpleDateFormat simpleDateFormat;
    
    /** acTypeNullandNotDelete instance operator for ACTYPE = null
     * and ACTYPE not equals delete
     */
    private Or acTypeNullandNotDelete =null;
    
    /** Boolean flag to check for CostSharing distribution */
    boolean isNoCostDistribution = false;
    
    private boolean fromHierarchy;
    private static final char GET_COST_SHARING_DISTRIBUTION = 'h'; 
    private static final char GET_BUDGET_PERIOD = 'i'; 
    private static final char GET_BUDGET_INFO = 'j';
    private static final char COST_SHARING_DATA = 'C';
    
    public  static final String SERVLET = "/BudgetMaintenanceServlet";
    private Vector dataObject;
    
    //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
    public SubAwardCostSharingForm subAwardCostSharingForm;
    private static final char GET_SUB_AWARD_COST_SHARING = 'E';
    public  static final String SUB_AWARD_SERVLET = "/BudgetSubAwardServlet";
    private HashMap hmCostSharingData;
    private CoeusVector vecSubAwardOrganizationList;
    //COEUSQA-2735 Cost sharing distribution for Sub awards - End
    
    /** Creates a new instance of CostSharingDistributionController */
    public CostSharingDistributionController() {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
        
    }
    
    
    /** Creates a new instance of CostSharingDistributionController
     * @param parent Component instance
     * @param modal boolean if <CODE>true</CODE> a modal window
     * @param budgetInfoBean instance of BudgetInfoBean
     */
    public CostSharingDistributionController(Component parent,boolean modal,BudgetInfoBean budgetInfoBean) {
        super();
        this.parent= parent;
        this.modal =  modal;
        this.budgetInfoBean = budgetInfoBean;
        initialiseController();
    }
    
    /** Method to initialise the Controller */
    private void initialiseController() {
        if(costSharingDistributionForm == null) {
            costSharingDistributionForm =  new CostSharingDistributionForm();
            //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
            subAwardCostSharingForm = new SubAwardCostSharingForm();
            //COEUSQA-2735 Cost sharing distribution for Sub awards - End
        }
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
    }
    
    /** displays the Form which is being controlled. */
    public void display() {
        try {
            if(!isFromHierarchy()){
                setFormData(budgetInfoBean);
            }else{
                setDataForHierarcy();
            }
            formatFields();
            if(isNoCostDistribution == false)
                ((CostSharingDistributionForm) getControlledUI()).dlgCostSharingDistributionForm.setVisible(true);
            else
                close();
        }catch(Exception expClose) {
            expClose.getMessage();
        }
    }
    
    /** close form by displosing the form */
    public void close() {
        
        ((CostSharingDistributionForm) getControlledUI()).dlgCostSharingDistributionForm.dispose();
    }
    
    /** perform field formatting.
     * enabling, disabling components depending on the function type.
     */
    public void formatFields() {
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            costSharingDistributionForm.btnAdd.setEnabled(false);
            costSharingDistributionForm.btnDelete.setEnabled(false);
            costSharingDistributionForm.btnCancel.requestFocus();
            costSharingDistributionForm.btnOk.setEnabled(false);
            costSharingDistributionForm.tblCostSharingDistribution.setEnabled(false);
            costSharingDistributionForm.tblCostSharingDistribution.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
            
        } else {
            costSharingDistributionForm.btnAdd.setEnabled(true);
            costSharingDistributionForm.btnDelete.setEnabled(true);
            costSharingDistributionForm.btnOk.setEnabled(true);
            costSharingDistributionForm.btnOk.requestFocus();
        }
        costSharingDistributionForm.tblCostSharingDistribution.getTableHeader().setReorderingAllowed(false);
        
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public java.awt.Component getControlledUI() {
        
        return costSharingDistributionForm;
    }
    
    /** returns the form data
     * @return the form data
     */
    public Object getFormData() {
        return costSharingDistributionForm;
    }
    
    /** registers GUI Components with event Listeners. */
    public void registerComponents() {
        
        costSharingDistributionForm.scrPnPeriod.getVerticalScrollBar().setUnitIncrement(25);
        costSharingDistributionForm.scrPnPeriod.getVerticalScrollBar().setBlockIncrement(25);
        
        costSharingDistributionForm.tblCostSharingDistribution.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                
                try {
                    
                    javax.swing.SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            costSharingDistributionForm.tblCostSharingDistribution.dispatchEvent(new KeyEvent(
                            costSharingDistributionForm.tblCostSharingDistribution,KeyEvent.KEY_PRESSED,0,0,KeyEvent.VK_F2,
                            KeyEvent.CHAR_UNDEFINED) );
                        }
                    });
                    
                } catch(Exception excep) {
                    excep.getMessage();
                }
                
            }
        });
        
        costSharingDistributionForm.btnOk.addActionListener(this);
        costSharingDistributionForm.btnCancel.addActionListener(this);
        costSharingDistributionForm.btnAdd.addActionListener(this);
        costSharingDistributionForm.btnDelete.addActionListener(this);
        //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
        costSharingDistributionForm.btnCostSharing.addActionListener(this);
        subAwardCostSharingForm.btnClose.addActionListener(this);
        //COEUSQA-2735 Cost sharing distribution for Sub awards - End
        
        costSharingDistributionForm.dlgCostSharingDistributionForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        // Travel all the components while pressing tab button
        java.awt.Component[] components = {
            costSharingDistributionForm.tblCostSharingDistribution,
            costSharingDistributionForm.btnDelete,
            costSharingDistributionForm.btnAdd,
            costSharingDistributionForm.btnCancel,
            costSharingDistributionForm.btnOk,
            //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
            costSharingDistributionForm.btnCostSharing
            //COEUSQA-2735 Cost sharing distribution for Sub awards - End
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        costSharingDistributionForm.setFocusTraversalPolicy(traversePolicy);
        costSharingDistributionForm.setFocusCycleRoot(true);
        
        
        
        costSharingDistributionForm.dlgCostSharingDistributionForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    checkforSave();
                }
            }
        });
        
        costSharingDistributionForm.dlgCostSharingDistributionForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                checkforSave();
            }
        });
        
        costSharingDistributionForm.dlgCostSharingDistributionForm.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we ) {
                checkforSave();
            }
        });
        
        
    }
    
    /** saves the Form Data. */
    public void saveFormData() {
        try {
            
            if(vecProposalCostSharingBean != null ) {
                
                //===Deleting the beans thats is removed from table
                for(int indexCount=0;indexCount < vecDeletedProposalCostSharingBean.size(); indexCount++) {
                    ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecDeletedProposalCostSharingBean.get(indexCount);
                    queryEngine.delete(queryKey,proposalCostSharingBean);
                }
                
                //===saving the beans to Basewindow based on acType
                
                for(int indexCount=0;indexCount < vecProposalCostSharingBean.size(); indexCount++) {
                    
                    ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecProposalCostSharingBean.get(indexCount);
                    
                    if(proposalCostSharingBean.getAcType() != null && proposalCostSharingBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        
                        if(proposalCostSharingBean.getRowId () < 1)
                        proposalCostSharingBean.setRowId(getMaxIdValue()+1);
                        
                        queryEngine.insert(queryKey,proposalCostSharingBean);
                        
                    }
                    
                    if(proposalCostSharingBean.getAcType()!= null && proposalCostSharingBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                         proposalCostSharingBean.setAcType(TypeConstants.DELETE_RECORD);
                         queryEngine.delete(queryKey,proposalCostSharingBean);
                         proposalCostSharingBean.setRowId(getMaxIdValue()+1);
                         queryEngine.insert(queryKey,proposalCostSharingBean);
                         
                         //queryEngine.update(queryKey,proposalCostSharingBean);
                    }
                }
                
                //Bug - fix code Added by Vyjayanthi - Start
                //To update total cost sharing amount of budgetInfoBean in query engine
                double value = ((double)Math.round(vecProposalCostSharingBean.sum("amount") *
                                Math.pow(10.0, 2) )) / 100;
                budgetInfoBean.setTotalCostSharingDistribution(value);

                queryEngine.update(queryKey, budgetInfoBean);

                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(budgetInfoBean);
                beanEvent.setSource(this);
                fireBeanUpdated(beanEvent);
                //Bug - fix code Added by Vyjayanthi - End
                
            }
            
        } catch(Exception saveExp) {
            saveExp.getMessage();
        }
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        
        budgetInfoBean = (BudgetInfoBean) data;
        isNoCostDistribution = false;
        if(budgetInfoBean != null && budgetInfoBean.getProposalNumber().length()> 0) {
            
            try {
                String proposalNumber = EMPTYSTRING;
                int versionNumber = 0;
                costSharingDistributionForm.tblCostSharingDistribution.setControllerInstance(this);
                    proposalNumber = budgetInfoBean.getProposalNumber();
                    versionNumber = budgetInfoBean.getVersionNumber();
                    queryKey = proposalNumber+versionNumber;
                    costSharingDistributionForm.txtVersion.setText(versionNumber+EMPTYSTRING);
                
                Equals equalsActype = new Equals(ACTYPE,null);
                NotEquals notEqualsDelete = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
                acTypeNullandNotDelete = new Or(equalsActype,notEqualsDelete);
                
                ProposalCostSharingBean proposalCostSharingBean = new ProposalCostSharingBean();
                proposalCostSharingBean.setProposalNumber(proposalNumber);
                proposalCostSharingBean.setVersionNumber(versionNumber);
                
                BudgetPeriodBean budgetPeriodBean= new BudgetPeriodBean();
                budgetPeriodBean.setProposalNumber(proposalNumber);
                budgetPeriodBean.setVersionNumber(versionNumber);
                
                
                if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                    costSharingDistributionForm.dlgCostSharingDistributionForm.setTitle("Display Proposal Cost Sharing for "+proposalNumber+", Version "+versionNumber);
                } else {
                    costSharingDistributionForm.dlgCostSharingDistributionForm.setTitle("Modify Proposal Cost Sharing for "+proposalNumber+", Version "+versionNumber);
                    // calling the calculate initially before computing Proposal Cost Sharing
                    //calculate(queryKey,-1);
                }
                    vecBudgetPeriodBean = queryEngine.executeQuery(queryKey,budgetPeriodBean.getClass(),acTypeNullandNotDelete);
                
                if(vecBudgetPeriodBean != null && vecBudgetPeriodBean.size() > 0) {
                    costSharingDistributionForm.addPeriodRows(vecBudgetPeriodBean);
                }
                //Case#2402- use a parameter to set the length of the account number throughout app - Start
                initAccountNumberMaxLength();
                //Case#2402 - End
                CoeusVector vecBudgetInfoBean = queryEngine.executeQuery(queryKey,budgetInfoBean.getClass(),acTypeNullandNotDelete);
                vecProposalCostSharingBean = getBeanFromQueryEngine();
                if(vecBudgetInfoBean != null && vecBudgetInfoBean.size() > 0) {
                    BudgetInfoBean budgetInfoBean  = (BudgetInfoBean ) vecBudgetInfoBean.get(0);
                    
                    if(budgetInfoBean.getCostSharingAmount() > 0 || (vecProposalCostSharingBean != null && vecProposalCostSharingBean.size() > 0)) {
                        double costSharingAmount = ((double)Math.round(budgetInfoBean.getCostSharingAmount()*Math.pow(10.0,2)))/100;
                        String totalCostSharingAmount = costSharingAmount+EMPTYSTRING;
                        costSharingDistributionForm.txtTotalCostSharing.setText(totalCostSharingAmount);
                    }
                    else {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_COST_SHARING));
                        close();
                        isNoCostDistribution = true;
                    }
                }
                
                
                vecDeletedProposalCostSharingBean =  new CoeusVector();
                
                if(vecProposalCostSharingBean != null && vecProposalCostSharingBean.size() > 0) {
                    vecProposalCostSharingBean.sort("fiscalYear");
                    costSharingDistributionForm.tblCostSharingDistribution.setTableData(vecProposalCostSharingBean);
                }
                else {
                    vecProposalCostSharingBean =  getProposalCostSharingBean(vecBudgetPeriodBean);
                    vecProposalCostSharingBean.sort("fiscalYear");
                    costSharingDistributionForm.tblCostSharingDistribution.setTableData(vecProposalCostSharingBean);
                    setSaveRequired(true);
                }
                
                // you must save the row and column when you enter a cell
                // in this case I keep the values in prevRow and prevCol
                //                 final int indexRow = 0;
                //                 final int indexColumn = 0;
                //                 javax.swing.SwingUtilities.invokeLater(new Runnable() {
                //                            public void run(){
                //                               //costSharingDistributionForm.tblCostSharingDistribution.requestFocusInWindow();
                //                                costSharingDistributionForm.tblCostSharingDistribution.changeSelection(indexRow , indexColumn, false, false);
                //                                costSharingDistributionForm.tblCostSharingDistribution.setEditingColumn(indexColumn);
                //                                costSharingDistributionForm.tblCostSharingDistribution.editCellAt(indexRow ,indexColumn);
                //                                costSharingDistributionForm.tblCostSharingDistribution.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                //                                Component editingComponent = costSharingDistributionForm.tblCostSharingDistribution.getEditorComponent();
                //                                editingComponent.requestFocus();
                //
                //                            }  });
                
            }  catch(Exception e){
                e.printStackTrace();
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }
        
        
        
    }
    
    private void setDataForHierarcy(){
        try{
                String proposalNumber = EMPTYSTRING;
                int versionNumber = 0;
                costSharingDistributionForm.tblCostSharingDistribution.setControllerInstance(this);
                proposalNumber = (String)getDataObject().elementAt(0);
                versionNumber = ((Integer)getDataObject().elementAt(1)).intValue();
                costSharingDistributionForm.txtVersion.setText(versionNumber+EMPTYSTRING);
                ProposalCostSharingBean proposalCostSharingBean = new ProposalCostSharingBean();
                proposalCostSharingBean.setProposalNumber(proposalNumber);
                proposalCostSharingBean.setVersionNumber(versionNumber);
                
                BudgetPeriodBean budgetPeriodBean= new BudgetPeriodBean();
                budgetPeriodBean.setProposalNumber(proposalNumber);
                budgetPeriodBean.setVersionNumber(versionNumber);
                costSharingDistributionForm.dlgCostSharingDistributionForm.setTitle("Display Proposal Cost Sharing for "+proposalNumber+", Version "+versionNumber);
                vecBudgetPeriodBean = getPeriodDataFromServer(proposalNumber,versionNumber);
                
                if(vecBudgetPeriodBean != null && vecBudgetPeriodBean.size() > 0) {
                    costSharingDistributionForm.addPeriodRows(vecBudgetPeriodBean);
                }
                //Case#2402- use a parameter to set the length of the account number throughout app - Start
                initAccountNumberMaxLength();
                //Case#2402 - End
                CoeusVector vecBudgetInfoBean = getBudgetInfoDataFromServer(proposalNumber,versionNumber);
                vecProposalCostSharingBean = getCostSharingDataFromServer(proposalNumber,versionNumber);
                if(vecBudgetInfoBean != null && vecBudgetInfoBean.size() > 0) {
                    BudgetInfoBean budgetInfoBean  = (BudgetInfoBean ) vecBudgetInfoBean.get(0);
                    if(budgetInfoBean.getCostSharingAmount() > 0 || (vecProposalCostSharingBean != null && vecProposalCostSharingBean.size() > 0)) {
                        double costSharingAmount = ((double)Math.round(budgetInfoBean.getCostSharingAmount()*Math.pow(10.0,2)))/100;
                        String totalCostSharingAmount = costSharingAmount+EMPTYSTRING;
                        costSharingDistributionForm.txtTotalCostSharing.setText(totalCostSharingAmount);
                    }
                    else {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_COST_SHARING));
                        close();
                        isNoCostDistribution = true;
                    }
                }
                if(vecProposalCostSharingBean != null && vecProposalCostSharingBean.size() > 0) {
                    vecProposalCostSharingBean.sort("fiscalYear");
                    costSharingDistributionForm.tblCostSharingDistribution.setTableData(vecProposalCostSharingBean);
                }
                else {
                    vecProposalCostSharingBean =  getProposalCostSharingBean(vecBudgetPeriodBean);
                    vecProposalCostSharingBean.sort("fiscalYear");
                    costSharingDistributionForm.tblCostSharingDistribution.setTableData(vecProposalCostSharingBean);
                }
        }catch (Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    private CoeusVector getCostSharingDataFromServer(String proposalNumber, int versionNumber) throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(proposalNumber);
       cvData.addElement(new Integer(versionNumber));
       cvData.addElement(new Character(COST_SHARING_DATA));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_COST_SHARING_DISTRIBUTION);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
    
    private CoeusVector getPeriodDataFromServer(String proposalNumber, int versionNumber) throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(proposalNumber);
       cvData.addElement(new Integer(versionNumber));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_BUDGET_PERIOD);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
    
    private CoeusVector getBudgetInfoDataFromServer(String proposalNumber, int versionNumber) throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(proposalNumber);
       cvData.addElement(new Integer(versionNumber));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_BUDGET_INFO);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
    
    /** gets the vector of ProposalCostSharingBean from Query Engine
     * <CODE>QueryEngine</CODE>
     * @return CoesVector of ProposalCostSharingBean
     * @see QueryEngine
     */
    public CoeusVector getBeanFromQueryEngine() {
        try {
            ProposalCostSharingBean proposalCostSharingBean = new ProposalCostSharingBean();
            proposalCostSharingBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalCostSharingBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            
            return queryEngine.executeQuery(queryKey,proposalCostSharingBean.getClass(),acTypeNullandNotDelete);
            
        }catch(Exception e) {
            e.getMessage();
        }
        return null;
    }
    
    /** Gets all the ProposalCostSharingBean from vector of BudgetPeriodBean
     * @param vecBudgetPeriodBean vector of BudgetPeriodBean
     * @return vector of ProposalCostSharingBean
     */
    public CoeusVector getProposalCostSharingBean(CoeusVector vecBudgetPeriodBean) {
        
        CoeusVector vecBeans = new CoeusVector();
        for(int index=0;index < vecBudgetPeriodBean.size(); index++) {
            BudgetPeriodBean budgetPeriodBean  = (BudgetPeriodBean ) vecBudgetPeriodBean.get(index);
            
            ProposalCostSharingBean proposalCostSharingBean = new ProposalCostSharingBean();
            
            proposalCostSharingBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
            proposalCostSharingBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
            proposalCostSharingBean.setAcType(TypeConstants.INSERT_RECORD);
            //proposalCostSharingBean.setRowId(getMaxIdValue()+1);
            proposalCostSharingBean.setCostSharingPercentage(0);
            proposalCostSharingBean.setFiscalYear(getFiscalYear(budgetPeriodBean.getStartDate()+EMPTYSTRING));
            proposalCostSharingBean.setAmount(budgetPeriodBean.getCostSharingAmount());
            proposalCostSharingBean.setSourceAccount(EMPTYSTRING);
            
            vecBeans.add(proposalCostSharingBean);
        }
        
        return vecBeans;
    }
    
    /** Gets the fiscal year from the sql date of the BudgetPeriodBean
     * @param datesql String sql date of the BudgetPeriodBean from which fiscalyear is determined
     * @return String fiscalYear
     */
    public String getFiscalYear(String datesql) {
        try {
            String fiscalYear = null;
            DateUtils dateUtils = new DateUtils();
            
            simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strDatesimple = dateUtils.formatDate(datesql, "MM/dd/yyyy");
            Date date =  simpleDateFormat.parse(strDatesimple);
            
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            
            int month = gregorianCalendar.get(Calendar.MONTH)+1;
            int year = gregorianCalendar.get(Calendar.YEAR);
            
            String formatDate = dateUtils.formatDate(FISCAL_YEAR_END_MONTH, "MM/dd/yyyy");
            Date utilFormatDate = simpleDateFormat.parse(formatDate);
            gregorianCalendar.setTime(utilFormatDate);
            
            int monthofFormatDate = gregorianCalendar.get(Calendar.MONTH)+1;
            
            
            if(month > monthofFormatDate ) {
                year = year+1;
                fiscalYear = year+EMPTYSTRING ;
            } else
                fiscalYear = year+EMPTYSTRING ;
            
            return fiscalYear;
        }catch(Exception e) {
            e.getMessage();
            CoeusOptionPane.showInfoDialog(FISCALYEARFAIL);
        }
        return null;
    }
    
    /** validate the form data/Form and returns true if validation is through
     * else returns false.
     * @throws CoeusUIException CoeusUIException if some exception occurs or some validation fails.
     * @return boolean if<CODE>true</CODE> validation is through
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        boolean validated = true;
        if(validateEntryCostSharingTable()) {
            if(!totalAmountValidation()  && costSharingDistributionForm.tblCostSharingDistribution.getRowCount() > 0) {
                String totalCostSharing = costSharingDistributionForm.txtTotalCostSharing.getText();
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CHECK_AMOUNT_MESSAGE)+totalCostSharing);
                validated = false;
            }
        } else
            validated = false;
        
        return validated;
    }
    
    /** Method to validate the Amount and the Total CostSharing before save operation
     * @return boolean  if <CODE>true</CODE> total of all Amount equals Total CostSharing Amount
     */
    
    public boolean totalAmountValidation() {
        boolean validateAmountEntry;
        
        String totalCostSharing = costSharingDistributionForm.txtTotalCostSharing.getValue();
        double totalSumfromDistribution = 0;
        //totalSumfromDistribution = vecProposalCostSharingBean.sum("amount");
        
        for(int indexCount=0;indexCount < vecProposalCostSharingBean.size(); indexCount++) {
            ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecProposalCostSharingBean.get(indexCount);
            totalSumfromDistribution += proposalCostSharingBean.getAmount();
        }
        
        totalSumfromDistribution= ((double)Math.round(totalSumfromDistribution*Math.pow(10.0,2)))/100;
        double totalCostSharingValue = ((double)Math.round(Double.parseDouble(totalCostSharing)*Math.pow(10.0,2)))/100;
        
        if(totalSumfromDistribution ==  totalCostSharingValue) {
            validateAmountEntry = true;
        }
        else
            validateAmountEntry = false;
        
        return validateAmountEntry;
    }
    
    /** checks if fiscalYear starts is between 1900 and 2099 as per PB validation
     * @param fiscalYear String fiscalYear to validate
     * @return boolean if<CODE>true</CODE> a valid fiscal year entry
     */
    public boolean isFiscalYearValid(String fiscalYear) {
        
         boolean isValidFiscalYear = false;
       // modified for COEUSQA-1426: Ability to enter data besides YYYY  start
        // now fiscal year will accpet values from 1-9999
        //previously fiscal year was accepting only YYYY format
            if(fiscalYear.length() > 0 && fiscalYear.length() <=4) {
               isValidFiscalYear = true;
            }
            else {
	       isValidFiscalYear = false;	
            }
         // to check whether fiscal year is '0'
           if(Integer.parseInt(fiscalYear)==0)
                isValidFiscalYear = false;
        // modified for COEUSQA-1426: Ability to enter data besides YYYY  start
        return isValidFiscalYear;
    }
    
    /** checks DuplicateEntry in source Account entry
     * @return boolean if<CODE>true</CODE> a duplicate entry
     */
    public boolean isDuplicateEntry() {
        
        boolean isDuplicateEntry  = false;
        
        Hashtable hastTable = new Hashtable();
        
        for(int indexCount=0;indexCount < vecProposalCostSharingBean.size(); indexCount++) {
            ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecProposalCostSharingBean.get(indexCount);
            String key = null;
            if(proposalCostSharingBean.getSourceAccount() != null && proposalCostSharingBean.getFiscalYear() != null
            && (proposalCostSharingBean.getSourceAccount().length() > 0 && proposalCostSharingBean.getFiscalYear().length() > 3) ) {
                key = (proposalCostSharingBean.getSourceAccount().trim()+proposalCostSharingBean.getFiscalYear().trim());
                if(hastTable.containsValue(key)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_ENTRY));
                    setTableEditing(indexCount,1);
                    isDuplicateEntry = true;
                    break;
                    
                } else
                    hastTable.put(key,key);
            }
        }
        
        return isDuplicateEntry;
    }
    
    /** Method to validate the Entry in CostSharingTable before save operation
     * @return boolean if<CODE>true</CODE> all validate Entry in CostSharingTable
     */
    
    public boolean validateEntryCostSharingTable() {
        if(isDuplicateEntry()) {
            return false;
        }
        
        if(!fiscalYearValidation())  {
            return false;
        }
        
        //Bug-fix comment by Vyjayanthi to allow zero amount
        /*
        if(!isAmountValid()) {
            return false;
        }
         */
        
        if(!sourceAccountValidation()) {
            return false;
        }
        
        return true;
    }
    
    /** Is valid Amount entry in CostSharingTable i.e greater than zero
     * @return boolean if<CODE>true</CODE> a valid Amount entry
     */
    public boolean isAmountValid() {
        
        boolean isAmountValid = true;
        for(int indexCount=0;indexCount < vecProposalCostSharingBean.size(); indexCount++) {
            ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecProposalCostSharingBean.get(indexCount);
            
            if(proposalCostSharingBean.getAmount() <= 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_AMOUNT)+(indexCount+1));
                setTableEditing(indexCount,2);
                isAmountValid = false;
                break;
            }
        }
        return isAmountValid;
    }
    
    /** Checks Fiscal Year column is empty or the entered fiscal year is valid
     * if empty/invalid entry it prompts the user to enter at the table cell
     * @return boolean if<CODE>true</CODE> Fiscal Year column is empty
     */
    public boolean fiscalYearValidation() {
        
        boolean validFiscalYear = true;
        for(int indexCount=0;indexCount < vecProposalCostSharingBean.size(); indexCount++) {
            ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecProposalCostSharingBean.get(indexCount);
            
            if(!(proposalCostSharingBean.getFiscalYear() != null &&
            proposalCostSharingBean.getFiscalYear().length() > 0)) {
                
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_FISCAL_YEAR)+(indexCount+1));
                setTableEditing(indexCount,1);
                validFiscalYear = false;
                break;
                
            }
            
            // checks the entered fiscal year is a valid entry
            if(!isFiscalYearValid(proposalCostSharingBean.getFiscalYear())) {
                
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_FISCAL_YEAR)+(indexCount+1));
                setTableEditing(indexCount,1);
                validFiscalYear = false;
                break;
            }
            
        }
        
        return validFiscalYear;
    }
    
    /** Validates whether Source Account column is empty if empty it prompts the user to
     * enter at the table cell
     * @return boolean if<CODE>true</CODE> Source Account column is empty
     */
    public boolean sourceAccountValidation() {
        
        boolean validSourceAccount = true;
        for(int indexCount=0;indexCount < vecProposalCostSharingBean.size(); indexCount++) {
            ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecProposalCostSharingBean.get(indexCount);
            if(!(proposalCostSharingBean.getSourceAccount() != null &&
            proposalCostSharingBean.getSourceAccount().length() > 0)) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CHECK_SOURCEACCOUNT)+(indexCount+1));
                setTableEditing(indexCount,3);
                validSourceAccount = false;
                break;
            }
        }
        
        return validSourceAccount;
    }
    
    /** Sets the Editing of row and clumn of a table by setting cursor on that cell
     * @param row int row of the table
     * @param column int column of the table
     */
    private void setTableEditing(int row,int column) {
        
        costSharingDistributionForm.tblCostSharingDistribution.getTableCellEditor().cancelCellEditing();
        costSharingDistributionForm.tblCostSharingDistribution.requestFocusInWindow();
        costSharingDistributionForm.tblCostSharingDistribution.setRowSelectionInterval(row, row);
        
        // saves the row and column when you enter a cell
        // in this case the values in prevRow and prevCol is set and
        // SwingUtilities.invokeLater() method is called to do the rest
        final int indexRow = row;
        final int indexColumn = column;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                costSharingDistributionForm.tblCostSharingDistribution.requestFocusInWindow();
                costSharingDistributionForm.tblCostSharingDistribution.changeSelection(indexRow , indexColumn, false, false);
                costSharingDistributionForm.tblCostSharingDistribution.setEditingColumn(indexColumn);
                costSharingDistributionForm.tblCostSharingDistribution.editCellAt(indexRow ,indexColumn);
                costSharingDistributionForm.tblCostSharingDistribution.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                
                Component editingComponent = costSharingDistributionForm.tblCostSharingDistribution.getEditorComponent();
                editingComponent.requestFocus();
                
                // this is where I restart the cell edit in the bad cell
                // table.editCellAt(pRow, pCol);
                // Component tec = lastJTable.getEditorComponent();
                // if (tec!=null){tec.requestFocus();}
                // try {((JTextComponent)tec).selectAll();}
                // catch (Exception notTextEx) {}
                // ignore select all failure
            }  });
            
            
    }
    
    /** actionPerfomed method for the Buttons
     * @param actionEvent ActionEvent object
     */
    public void actionPerformed(ActionEvent actionEvent) {
        
        Object source = actionEvent.getSource();
        callTableStopCellEditing();
        if(costSharingDistributionForm.tblCostSharingDistribution.isTableDateModified()) {
            setSaveRequired(true);
        }
        
        // *************** OK ACTION ***************
        if (source.equals(costSharingDistributionForm.btnOk)){
            try {
                if(isSaveRequired() ) {
                    if(validate()) {
                        saveFormData();
                        close();
                    }
                } else  {
                    if(validate()) {
                        close();
                    }
                }
                
            } catch(Exception e) {
                e.getMessage();
            }
            costSharingDistributionForm.tblCostSharingDistribution.setIsTableDateModified(false);
        }
        
        
        // *************** CANCEL ACTION  ***************
        if (source.equals(costSharingDistributionForm.btnCancel)){
            checkforSave();
            costSharingDistributionForm.tblCostSharingDistribution.setIsTableDateModified(false);
        }
        
        // *************** DELETE ACTION  ***************
        if (source.equals(costSharingDistributionForm.btnDelete)){
            
            if(costSharingDistributionForm.tblCostSharingDistribution.getRowCount() > 0) {
                
                int option = JOptionPane.NO_OPTION;
                option  = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(CONFRIM_DELETE),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                switch(option) {
                    case ( JOptionPane.YES_OPTION ):
                        try{
                            int selectedRow = costSharingDistributionForm.tblCostSharingDistribution.getSelectedRow();
                            if(selectedRow != -1) {
                                vecDeletedProposalCostSharingBean.add(vecProposalCostSharingBean.get(selectedRow));
                                vecProposalCostSharingBean.remove(selectedRow);
                                costSharingDistributionForm.tblCostSharingDistribution.getTableModel().fireTableRowsDeleted(selectedRow,selectedRow );
                                int rowIndex = 0;
                                int columnIndex = 0;
                                
                                if(costSharingDistributionForm.tblCostSharingDistribution.getRowCount() > 1) {
                                    if(selectedRow > 0 ) {
                                        rowIndex = selectedRow-1;
                                        columnIndex = selectedRow-1;
                                    }
                                    if(selectedRow == 0) {
                                        rowIndex = selectedRow;
                                        columnIndex = selectedRow;
                                    }
                                }
                                
                                if(costSharingDistributionForm.tblCostSharingDistribution.getRowCount() == 1) {
                                    rowIndex = 0;
                                    columnIndex = 0;
                                }
                                
                                if(costSharingDistributionForm.tblCostSharingDistribution.getRowCount() > 0)   {
                                    costSharingDistributionForm.tblCostSharingDistribution.setRowSelectionInterval(rowIndex,columnIndex);
                                    
                                    costSharingDistributionForm.tblCostSharingDistribution.setColumnSelectionInterval(0,0);
                                    if(selectedRow!=0){
                                        costSharingDistributionForm.tblCostSharingDistribution.editCellAt(selectedRow-1,0);
                                        costSharingDistributionForm.tblCostSharingDistribution.getEditorComponent().requestFocusInWindow();
                                    }
                
                                    costSharingDistributionForm.tblCostSharingDistribution.scrollRectToVisible(costSharingDistributionForm.tblCostSharingDistribution.getCellRect(rowIndex, columnIndex,true));
                                }
                                
                                setSaveRequired(true);
                            }
                            
                        }catch(Exception e){
                            CoeusOptionPane.showErrorDialog(e.getMessage());
                        }
                        break;
                        
                }
            } else
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MESSAGE_SELECT_A_ROW));
            
            
        }
        // *************** ADD ACTION  ***************
        if (source.equals(costSharingDistributionForm.btnAdd )){
            
            if(addProposalCostSharingBean()) {
                setSaveRequired(true);
                costSharingDistributionForm.tblCostSharingDistribution.setRowSelectionInterval(vecProposalCostSharingBean.size()-1,vecProposalCostSharingBean.size()-1);
                
                costSharingDistributionForm.tblCostSharingDistribution.setColumnSelectionInterval(0,0);
                costSharingDistributionForm.tblCostSharingDistribution.editCellAt(vecProposalCostSharingBean.size()-1,0);
                costSharingDistributionForm.tblCostSharingDistribution.getEditorComponent().requestFocusInWindow();
                
                int selectRow = costSharingDistributionForm.tblCostSharingDistribution.getSelectedRow();
                int selectColumn = costSharingDistributionForm.tblCostSharingDistribution.getSelectedColumn();
                costSharingDistributionForm.tblCostSharingDistribution.scrollRectToVisible(costSharingDistributionForm.tblCostSharingDistribution.getCellRect(selectRow, selectColumn,true));
            }
        }
        
        //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
        // *************** COST SHARING ACTION  ***************
        if (source.equals(costSharingDistributionForm.btnCostSharing)){
            List lstSubAwardCostSharing = new ArrayList(10);
            try {
                lstSubAwardCostSharing = fetchCostSharingData(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber());
                vecBudgetPeriodBean = getPeriodDataFromServer(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(lstSubAwardCostSharing!=null && lstSubAwardCostSharing.size()>0){
                fetchDataForForm(lstSubAwardCostSharing);
                subAwardCostSharingForm.setTitle(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber());
                subAwardCostSharingForm.setFormData(vecSubAwardOrganizationList, hmCostSharingData);
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_COST_SHARING));
            }
        }
        
        // *************** CLOSE ACTION  ***************
        if (source.equals(subAwardCostSharingForm.btnClose)){
            subAwardCostSharingForm.close();
        }
        //COEUSQA-2735 Cost sharing distribution for Sub awards - End
        
    }
    
    /** checks whether a save is required on cancel or close */
    public void checkforSave() {
        callTableStopCellEditing();
        if(costSharingDistributionForm.tblCostSharingDistribution.isTableDateModified()) {
            setSaveRequired(true);
        }
        
        if(isSaveRequired() && (!(getFunctionType() == TypeConstants.DISPLAY_MODE)) ) {
            
            int option = JOptionPane.NO_OPTION;
            option  = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(CONFRIM_SAVE),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_CANCEL);
            switch(option) {
                case ( JOptionPane.YES_OPTION ):
                    try{
                        
                        if(validate()) {
                            saveFormData();
                            close();
                        }
                        
                    }catch(Exception e){
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                    }
                    break;
                    
                case ( JOptionPane.NO_OPTION ):
                    setSaveRequired(false);
                    close();
                    break;
                case ( JOptionPane.CANCEL_OPTION):
                    break;
                    
            }
            
        } else
            close();
    }
    
    /** Method that performs stop cell edting on the table */
    public void callTableStopCellEditing() {
        costSharingDistributionForm.tblCostSharingDistribution.getTableCellEditor().stopCellEditing();
    }
    
    /** Returns the maxRowId Variable of variable id rowID of the bean
     * from the Vector of ProposalCostSharingBean
     * @return int maximum value of the Bean Variable Value
     */
    public int getMaxIdValue() {
        
        int maxRowId = 0;
        try {
            ProposalCostSharingBean allProposalCostSharingBean = new ProposalCostSharingBean();
            allProposalCostSharingBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            allProposalCostSharingBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            
            CoeusVector vecAllProposalCostSharingBean =  queryEngine.executeQuery(queryKey,allProposalCostSharingBean);
            
            vecAllProposalCostSharingBean.sort("rowId",false);
            ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean) vecAllProposalCostSharingBean.get(0);
            maxRowId = proposalCostSharingBean.getRowId();
            
        }catch(Exception e) {
            e.getMessage();
        }
        return maxRowId ;
    }
    /** Adds a new ProposalCostSharingBean to the table on ADD button action
     * @return boolean if <CODE>true</CODE> added a new ProposalCostSharingBean
     */
    public boolean addProposalCostSharingBean() {
        
        ProposalCostSharingBean   proposalCostSharingBean   = new ProposalCostSharingBean();
        proposalCostSharingBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        proposalCostSharingBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        proposalCostSharingBean.setAcType(TypeConstants.INSERT_RECORD);
        //proposalCostSharingBean.setRowId((getMaxIdValue()+1));
        
        proposalCostSharingBean.setCostSharingPercentage(0);
        proposalCostSharingBean.setFiscalYear(EMPTYSTRING);
        proposalCostSharingBean.setAmount(0);
        proposalCostSharingBean.setSourceAccount(EMPTYSTRING);
        
        //boolean isaRowalreadyAdded = false;
        boolean isAllFieldEmpty = false;
        if(vecProposalCostSharingBean != null && vecProposalCostSharingBean.size() > 0) {
            ProposalCostSharingBean lastProposalCostSharingBean = (ProposalCostSharingBean) vecProposalCostSharingBean.get((vecProposalCostSharingBean.size()-1));
            
            isAllFieldEmpty = !(lastProposalCostSharingBean.getFiscalYear() != null && lastProposalCostSharingBean.getFiscalYear().length() > 0);
            if(isAllFieldEmpty){
                isAllFieldEmpty = !(lastProposalCostSharingBean.getCostSharingPercentage() > 0);
            }
            if(isAllFieldEmpty){
                isAllFieldEmpty = !(lastProposalCostSharingBean.getSourceAccount() != null && lastProposalCostSharingBean.getSourceAccount().length() > 0);
            }
            if(isAllFieldEmpty){
                isAllFieldEmpty = !(lastProposalCostSharingBean.getAmount() > 0);
            }
            
            //            if(!((lastProposalCostSharingBean.getFiscalYear() != null && lastProposalCostSharingBean.getFiscalYear().length() > 0))) {
            //                isaRowalreadyAdded = true;
            //            }
            
            
            
        }
        
        if(isAllFieldEmpty)
            return false;
        
        if(!isAllFieldEmpty) {
            vecProposalCostSharingBean.add(vecProposalCostSharingBean.size(),proposalCostSharingBean);
            costSharingDistributionForm.tblCostSharingDistribution.getTableModel().fireTableRowsInserted(vecProposalCostSharingBean.size(), vecProposalCostSharingBean.size());
            return true;
        } else
            return false;
        
    }
    
    /**
     * Getter for property fromHierarchy.
     * @return Value of property fromHierarchy.
     */
    public boolean isFromHierarchy() {
        return fromHierarchy;
    }    
   
    /**
     * Setter for property fromHierarchy.
     * @param fromHierarchy New value of property fromHierarchy.
     */
    public void setFromHierarchy(boolean fromHierarchy) {
        this.fromHierarchy = fromHierarchy;
    }    
    
    /**
     * Getter for property dataObject.
     * @return Value of property dataObject.
     */
    public java.util.Vector getDataObject() {
        return dataObject;
    }
    
    /**
     * Setter for property dataObject.
     * @param dataObject New value of property dataObject.
     */
    public void setDataObject(java.util.Vector dataObject) {
        this.dataObject = dataObject;
    }
    
    //Case#2402- use a parameter to set the length of the account number throughout app
    /**
     * Method to intialize account number field size
     */
    private void initAccountNumberMaxLength()throws CoeusException{
        CoeusVector cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
        CoeusVector cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
        if(cvFiltered != null && cvFiltered.size() > 0){
            CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
            int accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
            costSharingDistributionForm.tblCostSharingDistribution.initAccountNumberMaxLength(accountNumberMaxLength);
        }
    }
    //Case#2402 - End
    
    //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
    /** fetches the cost sharing data for sub award
     * @param String proposalNumber
     * @param int versionNumber
     */
    private List fetchCostSharingData(String proposalNumber, int versionNumber) throws Exception {
        RequesterBean request = new RequesterBean();
        CoeusVector cvData = new CoeusVector();
        cvData.addElement(proposalNumber);
        cvData.addElement(new Integer(versionNumber));
        request.setDataObjects(cvData);
        request.setFunctionType(GET_SUB_AWARD_COST_SHARING);
        AppletServletCommunicator comm =
                new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SUB_AWARD_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return (List)response.getDataObject();
        }else {
            throw new Exception(response.getMessage());
        }
    }
    
    /** fetches the cost sharing data for sub award
     * @param List lstSubAwardCostSharing
     */
    public void fetchDataForForm(List lstSubAwardCostSharing){
        hmCostSharingData = new HashMap(10);
        vecSubAwardOrganizationList = new CoeusVector();
        Set subAwardUniqueData = new TreeSet();
        Set uniqueBudgetPeriod = new HashSet();
        CoeusVector cvUniqueBudgetPeriods = new CoeusVector();
        
        //to create unique list of organization for the sub award
        for(Object subAwardData:lstSubAwardCostSharing){
            BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)subAwardData;
            int subAwardNumber = budgetSubAwardDetailBean.getSubAwardNumber();
            if(subAwardUniqueData.add(new Integer(subAwardNumber))){
                vecSubAwardOrganizationList.add(budgetSubAwardDetailBean);
            }
        }
        //to fetch the sub award cost sharing data and group them if they belong to same sub award
        for(Object subAward:subAwardUniqueData){
            int subAwardNumber = (((Integer)subAward).intValue());
            CoeusVector cvSubAwardCostSharingData = new CoeusVector();
            for(Object subAwardData:lstSubAwardCostSharing){
                BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)subAwardData;
                int beanSubAwardNumber = budgetSubAwardDetailBean.getSubAwardNumber();
                if(subAwardNumber==beanSubAwardNumber){                    
                    cvSubAwardCostSharingData.add(budgetSubAwardDetailBean);
                }
            }
            CoeusVector cvSubAwardSharingData = validateForBudgetPeriods(cvSubAwardCostSharingData,vecBudgetPeriodBean);
            hmCostSharingData.put(subAwardNumber,cvSubAwardSharingData);
        }
    }
    
    /** validates the cost sharing data for sub award and adds the periods for sub award
     * @param CoeusVector cvSubAwardCostShrgData
     * @param CoeusVector vecBudgetPeriodBean
     * @return CoeusVector cvSubAwardCostShrgData
     */
    public CoeusVector validateForBudgetPeriods(CoeusVector cvSubAwardCostShrgData, CoeusVector vecBudgetPeriodBean){
        if(cvSubAwardCostShrgData.size()==vecBudgetPeriodBean.size()){
            //do nothing
            return cvSubAwardCostShrgData;
        }else{
            int totSize = cvSubAwardCostShrgData.size();
            for(int counter=0;counter<vecBudgetPeriodBean.size();counter++){
                if(counter+1>totSize){
                    BudgetSubAwardDetailBean subBudgetSubAwardBean = new BudgetSubAwardDetailBean();
                    BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(counter);
                    subBudgetSubAwardBean.setCostSharingAmount(new Double(0.00));
                    subBudgetSubAwardBean.setPeriodStartDate(budgetPeriodBean.getStartDate());
                    subBudgetSubAwardBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                    subBudgetSubAwardBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
                    subBudgetSubAwardBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
                    cvSubAwardCostShrgData.add(subBudgetSubAwardBean);
                }
            }
        }
        return cvSubAwardCostShrgData;
    }   
    //COEUSQA-2735 Cost sharing distribution for Sub awards - End
}
