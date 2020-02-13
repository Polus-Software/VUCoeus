/*
 * BudgetModularFormsController.java
 *
 * Created on February 2, 2006, 11:37 AM
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetModularIDCBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.gui.BudgetModularForms;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Operator;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


/**
 *
 * @author  tarique
 */
public class BudgetModularFormsController extends Controller 
implements ActionListener{
    private static final String EMPTY_STRING = "";
    private static final int IDC_RATE_NUMBER = 0;
    private static final int IDC_TYPE_COLUMN = 1;
    private static final int IDC_RATE_COLUMN = 2;
    private static final int IDC_BASE_COLUMN = 3;
    private static final int FUNDS_COLUMN = 4;
    private static final int TOTAL_LABEL_COLUMN = 0;
    private static final int TOTAL_COLUMN = 1;
    
    private BudgetModularIDCTableModel budgetModularIDCTableModel;
    private BudgetModularIDCEditor budgetModularIDCEditor;
    private BudgetModularIDCCellRenderer budgetModularIDCCellRenderer;
    private BudgetModularTotalIDCTableModel budgetModularTotalIDCTableModel;
    private BudgetModularTotalDCNIDCTableModel budgetModularTotalDCNIDCTableModel;
    private ModularTotalTableCellRenderer modularTotalTableCellRenderer;
    private BudgetModularForms budgetModularForms;
    private int periodNumber;
    private CoeusDlgWindow dlgBudgetModular;
    //public boolean modified = false;
    private BudgetInfoBean budgetInfoBean;
    private QueryEngine queryEngine;
    private String queryKey;
    private HashMap hmModularData;
    private CoeusVector cvModularDCData;
    private CoeusVector cvModularIDCData;
    private CoeusVector cvData;
    private boolean synced;
    private Hashtable syncedData;
    private Hashtable modularDataObject;
    
    //Case 2260 Start 1
    private CoeusVector cvDelIDCData;
    private static final String DELETE_CONFIRMATION = "instPropIPReview_exceptionCode.1353";
    private CoeusMessageResources coeusMessageResources;
    private boolean modified = false;
    //Case 2260 End 1
    
    /** Creates a new instance of BudgetModularFormsController */
    public BudgetModularFormsController(int periodNumber, BudgetInfoBean budgetInfoBean, Hashtable syncedData ) {
        this.periodNumber = periodNumber;
        this.budgetInfoBean = budgetInfoBean;
        this.syncedData = syncedData;
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryEngine = QueryEngine.getInstance();
        queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        registerComponents();
        setTableEditors();
        setTableKeyTraversal();
        
    }
       /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public Component getControlledUI(){
        return budgetModularForms;
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     */
    public void setFormData(Object data) {
        hmModularData = new HashMap();
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        CoeusVector cvPeriodBeans = new CoeusVector();
        try{
       
            cvPeriodBeans = queryEngine.executeQuery(queryKey,budgetPeriodBean);
             if(cvPeriodBeans!=null && cvPeriodBeans.size()>0){
                cvPeriodBeans = cvPeriodBeans.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            }
           
            //Case 2260 Start 2
            setSaveRequired(false);
            //cvData = queryEngine.executeQuery(queryKey,BudgetModularBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            //cvModularDCData = getDCModularDetails(cvPeriodBeans,cvData);
            
            /*cvData = queryEngine.executeQuery(queryKey,BudgetModularIDCBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            cvModularIDCData = getIDCModularDetails(cvPeriodBeans,cvData);
            if(cvModularIDCData!=null&&cvModularIDCData.size()>0){
                cvModularIDCData.sort("budgetPeriod",true);
            }*/
            
            
            //autoSyncAction(cvModularDCData,cvModularIDCData);
            
            
            /*if(cvModularDCData == null || cvModularDCData.size() == 0){
                cvModularDCData = new CoeusVector();
                setModularDCData(cvModularDCData, cvPeriodBeans);
            }
            if(cvModularIDCData == null || cvModularIDCData.size() == 0){
                cvModularIDCData = new CoeusVector();    
                setModularIDCData(cvModularIDCData, cvPeriodBeans);
            }
            
            
            cvModularDCData = cvModularDCData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            //Added for modular budget enhancement delete period start 1
            //this code if some data found from sync but add some period or if we r adjusting from adjust period boundary
            if(cvModularDCData == null || cvModularDCData.size() == 0){
                cvModularDCData = new CoeusVector();
                setModularDCData(cvModularDCData, cvPeriodBeans);
                cvModularDCData = cvModularDCData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            }
            //this code if some data found from sync but add some period or if we r adjusting from adjust period boundary
            cvModularIDCData = cvModularIDCData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            if(cvModularIDCData == null || cvModularIDCData.size() == 0){
                cvModularIDCData = new CoeusVector();    
                setModularIDCData(cvModularIDCData, cvPeriodBeans);
                cvModularIDCData = cvModularIDCData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            }*/
            //Added for modular budget enhancement delete period end 1
            
            Equals eqPeriod = new Equals("budgetPeriod" , new Integer(periodNumber));
            
            
            cvModularDCData = queryEngine.executeQuery(queryKey,BudgetModularBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            cvModularDCData = cvModularDCData.filter(eqPeriod);
            
            cvModularIDCData = queryEngine.executeQuery(queryKey,BudgetModularIDCBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            
            cvModularIDCData = cvModularIDCData.filter(eqPeriod);
            
            
            if(cvModularIDCData == null){
                cvModularIDCData = new CoeusVector();
            }
            //Case 2260 End 2
            
            budgetModularIDCTableModel.setData(cvModularIDCData);
            budgetModularIDCTableModel.fireTableDataChanged();
         
            budgetModularTotalIDCTableModel.fireTableDataChanged();
       
            budgetModularTotalDCNIDCTableModel.fireTableDataChanged();
            
            //Case 2260 Start 3
            if(cvModularDCData == null || cvModularDCData.size() == 0){
                BudgetModularBean budModBean = new BudgetModularBean();

                budModBean.setDirectCostFA(0);
                budModBean.setConsortiumFNA(0);
                budModBean.setTotalDirectCost(0);
                budModBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                budModBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                budModBean.setBudgetPeriod(periodNumber);
                cvModularDCData.add(budModBean);
            } 
            
            enableDisableComp();
            //Case 2260 End 3
            
            BudgetModularBean budgetModularBean
                =(BudgetModularBean)cvModularDCData.get(0);
            budgetModularForms.txtDirectCostFA.setValue(budgetModularBean.getDirectCostFA());
            budgetModularForms.txtConsortiumFA.setValue(budgetModularBean.getConsortiumFNA());
            double totalDC = budgetModularBean.getDirectCostFA()+budgetModularBean.getConsortiumFNA();
            budgetModularForms.txtTotalDirectCost.setValue(totalDC);
            hmModularData.put(BudgetModularBean.class, cvModularDCData);
            hmModularData.put(BudgetModularIDCBean.class, cvModularIDCData );
            
            
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        
    }
    
    //Case 2260 Start 4
    /*private void setModularDCData(CoeusVector cvDCData, CoeusVector cvPeriodBean){
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
    private void autoSyncAction(CoeusVector cvDCData, CoeusVector cvIDCData) throws CoeusException{
         if(cvDCData== null || cvDCData.size() == 0 ){
              if( cvIDCData== null || cvIDCData.size() == 0 ){
                     cvModularDCData = (CoeusVector) syncedData.get(BudgetModularBean.class);
                    if(cvModularDCData == null){
                        cvModularDCData  = new CoeusVector();
                    }
                    cvModularIDCData = (CoeusVector) syncedData.get(BudgetModularIDCBean.class);
                    if(cvModularIDCData == null){
                        cvModularIDCData  = new CoeusVector();
                    }
             }
        }
    }
    private CoeusVector getDCModularDetails(CoeusVector cvPeriod,CoeusVector cvData)
                                                        throws CoeusException{
        CoeusVector cvModularData=new CoeusVector();
        if(cvData!=null&&cvData.size()>0){
            for(int index=0;index<cvData.size();index++){
                BudgetModularBean budgetModularBean
                            =(BudgetModularBean)cvData.get(index);
                NotEquals ne=new NotEquals("budgetPeriod",
                new Integer(budgetModularBean.getBudgetPeriod()));
                cvPeriod=cvPeriod.filter(ne);
            }
            if(cvPeriod!=null&&cvPeriod.size()>0){
                CoeusVector cvFilterData=new CoeusVector();
                for(int index=0;index<cvPeriod.size();index++){
                    BudgetPeriodBean budgetPeriodBean
                    =(BudgetPeriodBean)cvPeriod.get(index);
                    BudgetModularBean budgetModularBean
                                        =new BudgetModularBean();
                    budgetModularBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
                    budgetModularBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
                    budgetModularBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                    budgetModularBean.setDirectCostFA(0);
                    budgetModularBean.setConsortiumFNA(0);
                    budgetModularBean.setTotalDirectCost(0);
                    budgetModularBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvFilterData.add(budgetModularBean);
                }
                for(int index=0;index<cvFilterData.size();index++){
                    BudgetModularBean budgetModularBean
                    =(BudgetModularBean)cvFilterData.get(index);
                    cvModularData.add(budgetModularBean);
                }
                for(int index=0;index<cvData.size();index++){
                    BudgetModularBean budgetModularBean
                    =(BudgetModularBean)cvData.get(index);
                    cvModularData.add(budgetModularBean);
                }
                return cvModularData;
            }else{
                for(int index=0;index<cvData.size();index++){
                    BudgetModularBean budgetModularBean
                    =(BudgetModularBean)cvData.get(index);
                    cvModularData.add(budgetModularBean);
                }
                
                return cvModularData;
            }//End if
        }
        return null;
    }
    private CoeusVector getIDCModularDetails(CoeusVector cvPeriod,CoeusVector cvIDCData)
                                                        throws CoeusException{
        CoeusVector cvModularIDCData=new CoeusVector();
        int rateNumber[] = {1,2,3,4};
        if(cvIDCData!=null&&cvIDCData.size()>0){
            for(int index=0;index<cvIDCData.size();index++){
                BudgetModularIDCBean budgetModularIDCBeanBean
                            =(BudgetModularIDCBean)cvIDCData.get(index);
                NotEquals ne = new NotEquals("budgetPeriod",
                new Integer(budgetModularIDCBeanBean.getBudgetPeriod()));
                cvPeriod = cvPeriod.filter(ne);
            }
            if(cvPeriod != null && cvPeriod.size() > 0){
                CoeusVector cvFilterData = new CoeusVector();
                for(int index = 0;index < cvPeriod.size();index++){
                    BudgetPeriodBean budgetPeriodBean
                                        =(BudgetPeriodBean)cvPeriod.get(index);
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
                        cvFilterData.add(budgetModularIDCBean);
                    }
                }
                for(int index=0;index<cvFilterData.size();index++){
                    BudgetModularIDCBean budgetModularIDCBeanBean
                            =(BudgetModularIDCBean)cvFilterData.get(index);
                    cvModularIDCData.add(budgetModularIDCBeanBean);
                }
                for(int index = 0;index<cvIDCData.size();index++){
                    BudgetModularIDCBean budgetModularIDCBeanBean
                                =(BudgetModularIDCBean)cvIDCData.get(index);
                    cvModularIDCData.add(budgetModularIDCBeanBean);
                }
                return cvModularIDCData;
            }else{
                for(int index=0;index<cvIDCData.size();index++){
                    BudgetModularIDCBean budgetModularIDCBeanBean
                                = (BudgetModularIDCBean)cvIDCData.get(index);
                    cvModularIDCData.add(budgetModularIDCBeanBean);
                }
                
                return cvModularIDCData;
            }//End if
        }
        return null;
    }*/
    //Case 2260 End 4
    
    /** returns the form data
     * @return the form data
     */
    public Object getFormData(){
        return hmModularData;
    }
    
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields(){
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            budgetModularForms.txtDirectCostFA.setEnabled(false);
            budgetModularForms.txtConsortiumFA.setEnabled(false);
        }else{
            budgetModularForms.tblIndirectCosts.editCellAt(0,1);
            if(budgetModularForms.tblIndirectCosts.getEditorComponent()!=null){
                budgetModularForms.tblIndirectCosts.getEditorComponent().requestFocusInWindow();
                budgetModularForms.tblIndirectCosts.setRowSelectionInterval(0,0);
            }
            
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
        budgetModularForms = new BudgetModularForms();
        budgetModularIDCTableModel = new BudgetModularIDCTableModel();
        budgetModularIDCEditor = new BudgetModularIDCEditor();
        budgetModularIDCCellRenderer = new BudgetModularIDCCellRenderer();
        budgetModularTotalIDCTableModel = new BudgetModularTotalIDCTableModel();
        budgetModularTotalDCNIDCTableModel = new BudgetModularTotalDCNIDCTableModel();
        modularTotalTableCellRenderer = new ModularTotalTableCellRenderer();
        budgetModularForms.tblIndirectCosts.setModel(budgetModularIDCTableModel);
        budgetModularForms.tblTotalIndirectCosts.setModel(budgetModularTotalIDCTableModel);
        budgetModularForms.tblTotalDcIDC.setModel(budgetModularTotalDCNIDCTableModel);
        
        //Case 2260 Start 5
        budgetModularForms.btnAdd.addActionListener(this);
        budgetModularForms.btnDelete.addActionListener(this);
        //Case 2260 End 5
        
        java.awt.Component[] components = {
            budgetModularForms.btnAdd , budgetModularForms.btnDelete,
            budgetModularForms.txtDirectCostFA, budgetModularForms.txtConsortiumFA,
            budgetModularForms.scrPnIndirectCosts };
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            budgetModularForms.setFocusTraversalPolicy(traversePolicy);
            budgetModularForms.setFocusCycleRoot(true);
            budgetModularForms.txtDirectCostFA.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent fe){
                    BudgetModularBean budgetModularBean
                    =(BudgetModularBean)cvModularDCData.get(0);
                    if(budgetModularIDCEditor.getComponent() != null){
                        budgetModularIDCEditor.stopCellEditing();
                    }
                    double changeValue = (new Double(budgetModularForms.txtDirectCostFA.getValue()).doubleValue());
                    if(budgetModularBean.getDirectCostFA() != changeValue){
                        budgetModularBean.setDirectCostFA(changeValue);
                        budgetModularBean.setTotalDirectCost(changeValue
                        + (new Double(budgetModularForms.txtConsortiumFA.getValue()).doubleValue()));
                        
                        budgetModularForms.txtTotalDirectCost.setValue(changeValue
                        + (new Double(budgetModularForms.txtConsortiumFA.getValue()).doubleValue()));
                        setSaveRequired(true);
                        budgetModularTotalIDCTableModel.fireTableDataChanged();
                        budgetModularTotalDCNIDCTableModel.fireTableDataChanged();
                        
                        //Case 2260 Start 6
                        modified = true;
                        //Case 2260 End 6
                    }
                }
                //Added for modular budget enhancement delete period start 2
                public void focusGained(FocusEvent fe){
                    if(budgetModularIDCEditor.getComponent() != null){
                        budgetModularIDCEditor.stopCellEditing();
                    }
                }
                //Added for modular budget enhancement delete period end 2
            });
            budgetModularForms.txtConsortiumFA.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent fe){
                    BudgetModularBean budgetModularBean
                    =(BudgetModularBean)cvModularDCData.get(0);
                    if(budgetModularIDCEditor.getComponent() != null){
                        budgetModularIDCEditor.stopCellEditing();
                    }
                    double changeValue = (new Double(budgetModularForms.txtConsortiumFA.getValue()).doubleValue());
                    if(budgetModularBean.getConsortiumFNA() != changeValue){
                        budgetModularBean.setConsortiumFNA(changeValue);
                        budgetModularBean.setTotalDirectCost(changeValue
                            + (new Double(budgetModularForms.txtDirectCostFA.getValue()).doubleValue()));
                        budgetModularForms.txtTotalDirectCost.setValue(changeValue
                        + (new Double(budgetModularForms.txtDirectCostFA.getValue()).doubleValue()));
                        setSaveRequired(true);
                        budgetModularTotalIDCTableModel.fireTableDataChanged();
                        budgetModularTotalDCNIDCTableModel.fireTableDataChanged();
                        
                        //Case 2260 Start 7
                        modified = true;
                        //Case 2260 End 7
                    }
                }
                //Added for modular budget enhancement delete period start 3
                public void focusGained(FocusEvent fe){
                    if(budgetModularIDCEditor.getComponent() != null){
                        budgetModularIDCEditor.stopCellEditing();
                    }
                }
                //Added for modular budget enhancement delete period end 3
            });
            
    }
     private void setTableEditors(){
        try{
            JTableHeader tableHeader = budgetModularForms.tblIndirectCosts.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setMaximumSize(new java.awt.Dimension(100,24));
            tableHeader.setMinimumSize(new java.awt.Dimension(100,24));
            tableHeader.setPreferredSize(new java.awt.Dimension(100,24));
            tableHeader.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
            budgetModularForms.tblIndirectCosts.setRowHeight(22);
            budgetModularForms.tblIndirectCosts.setShowHorizontalLines(true);
            budgetModularForms.tblIndirectCosts.setShowVerticalLines(true);
            budgetModularForms.tblIndirectCosts.setOpaque(false);
            budgetModularForms.tblIndirectCosts.setSelectionMode(
                                    DefaultListSelectionModel.SINGLE_SELECTION);
            budgetModularForms.tblIndirectCosts.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            budgetModularForms.tblTotalIndirectCosts.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            budgetModularForms.tblTotalIndirectCosts.setRowHeight(22);
            budgetModularForms.tblTotalIndirectCosts.setShowVerticalLines(false);
            budgetModularForms.tblTotalIndirectCosts.setShowHorizontalLines(false);
            budgetModularForms.tblTotalIndirectCosts.setOpaque(true);
            budgetModularForms.tblTotalDcIDC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            budgetModularForms.tblTotalDcIDC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            budgetModularForms.tblTotalDcIDC.setRowHeight(22);
            budgetModularForms.tblTotalDcIDC.setShowVerticalLines(false);
            budgetModularForms.tblTotalDcIDC.setShowHorizontalLines(false);
            budgetModularForms.tblTotalDcIDC.setOpaque(true);
            TableColumn columnModular;
            TableColumn columnModularTotal;
            TableColumn columnTotalDCIDC;
            int size[] = {80,125,115,115,115};
            for(int index=0;index<size.length;index++){
                columnModular=budgetModularForms.tblIndirectCosts.getColumnModel().getColumn(index);
                columnModular.setPreferredWidth(size[index]);
                columnModular.setCellEditor(budgetModularIDCEditor);
                columnModular.setCellRenderer(budgetModularIDCCellRenderer);
              }
            columnModularTotal = budgetModularForms.tblTotalIndirectCosts.getColumnModel().getColumn(0);
            columnModularTotal.setPreferredWidth(120);
            columnModularTotal.setHeaderRenderer(new EmptyHeaderRenderer());
            columnModularTotal.setCellRenderer(modularTotalTableCellRenderer);
            columnModularTotal = budgetModularForms.tblTotalIndirectCosts.getColumnModel().getColumn(1);
            columnModularTotal.setPreferredWidth(120);
            columnModularTotal.setHeaderRenderer(new EmptyHeaderRenderer());
            columnModularTotal.setCellRenderer(modularTotalTableCellRenderer);
            
            columnTotalDCIDC=budgetModularForms.tblTotalDcIDC.getColumnModel().getColumn(0);
            columnTotalDCIDC.setPreferredWidth(120);
            columnTotalDCIDC.setHeaderRenderer(new EmptyHeaderRenderer());
            columnTotalDCIDC.setCellRenderer(modularTotalTableCellRenderer);
            columnTotalDCIDC = budgetModularForms.tblTotalDcIDC.getColumnModel().getColumn(1);
            columnTotalDCIDC.setPreferredWidth(120);
            columnTotalDCIDC.setHeaderRenderer(new EmptyHeaderRenderer());
            columnTotalDCIDC.setCellRenderer(modularTotalTableCellRenderer);
         }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
     public void cleanUp(){
         cvData = null;
         cvModularDCData = null;
         cvModularIDCData = null;
     }
    /** saves the Form Data.
     */
     public void saveFormData(){
         CoeusVector cvTemp=new CoeusVector();
         if(isSaveRequired()){
             if(cvModularDCData!=null && cvModularDCData.size() > 0){
                 //Case 2260 Start 8
                 //cvTemp.addAll(cvModularDCData);
                 if(modified){
                   cvTemp.addAll(cvModularDCData);  
                 }else{
                     if(cvModularIDCData != null && cvModularIDCData.size() > 0){
                         BudgetModularBean budModularBean = (BudgetModularBean) cvModularDCData.get(0);
                         budModularBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                         budModularBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                         budModularBean.setBudgetPeriod(periodNumber);
                         budModularBean.setAcType(TypeConstants.INSERT_RECORD);
                         cvTemp.add(budModularBean);
                     }
                 }
                 //Case 2260 End 8
             }
         }
//         else{
//             cvTemp.addAll(cvModularDCData);
//         }//End if
         CoeusVector cvDataToEngine = new CoeusVector();
         if(cvTemp!=null && cvTemp.size() > 0){
             for(int index=0;index<cvTemp.size();index++){
                 BudgetModularBean budgetModularBean = (BudgetModularBean)cvTemp.get(index);
                 double totalDirectCost=budgetModularBean.getDirectCostFA()
                                            + budgetModularBean.getConsortiumFNA();
                 budgetModularBean.setTotalDirectCost(totalDirectCost);
                 String acType=budgetModularBean.getAcType();
                 if(acType == null){
                     budgetModularBean.setAcType(TypeConstants.UPDATE_RECORD);
                     cvDataToEngine.add(budgetModularBean);
                 }else if(acType.equals(TypeConstants.INSERT_RECORD)){
                     cvDataToEngine.add(budgetModularBean);
                     
                 }else if(acType != null && !acType.equals(TypeConstants.INSERT_RECORD)){
                     cvDataToEngine.add(budgetModularBean);
                 }//End if
                 
                 //Case 2260 Start 9
                 validateData(cvDataToEngine , totalDirectCost, false);
                 //Case 2260 End 9
         
             }//End for
         }
         //Case 2260 Start 10
         else{
             String value = budgetModularForms.txtTotalDirectCost.getValue();
             cvDataToEngine = validateData(cvModularDCData , Double.parseDouble(value) , true);
         }
         //Case 2260 End 10
        
         if(cvDataToEngine!=null){
             for(int index=0;index<cvDataToEngine.size();index++){//change here vector
                 BudgetModularBean budgetModularBean=(BudgetModularBean)cvDataToEngine.get(index);
                 
                 if(budgetModularBean.getAcType()!=null){
                     String acType=budgetModularBean.getAcType();
                     
                     if(acType.equals(TypeConstants.INSERT_RECORD)){
                         queryEngine.insert(queryKey,budgetModularBean);
                     }
                     else if(acType.equals(TypeConstants.UPDATE_RECORD)){
                         budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
                         try{
                             queryEngine.delete(queryKey, budgetModularBean);
                         }
                         catch(CoeusException ce){
                             ce.printStackTrace();
                         }
                         budgetModularBean.setAcType(TypeConstants.INSERT_RECORD);
                         queryEngine.insert(queryKey, budgetModularBean);
                     }else if(acType.equals(TypeConstants.DELETE_RECORD)){
                         budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
                         try{
                             queryEngine.delete(queryKey,budgetModularBean);
                         }
                         catch(CoeusException ced){
                             ced.printStackTrace();
                         }
                     }
                 }//End if
             }//end for
         }//End if
         hmModularData.put(BudgetModularBean.class, cvDataToEngine);
         cvTemp.removeAllElements();
         cvTemp = null;
         cvDataToEngine.removeAllElements();
         if(isSaveRequired()){
             //Case 2260 Start 11
             if(cvDelIDCData != null && cvDelIDCData.size() > 0){
                 cvDataToEngine.addAll(cvDelIDCData);
             }
             //Case 2260 End 11
             
             if(cvModularIDCData != null && cvModularIDCData.size() > 0){
                 cvDataToEngine.addAll(cvModularIDCData);
             }else{
                 CoeusVector cvTempData = null;
                 try{
                     cvTempData = queryEngine.executeQuery(queryKey, BudgetModularIDCBean.class , new Equals("budgetPeriod" , new Integer(periodNumber)));
                 }catch (CoeusException cEx){
                     cEx.printStackTrace();
                 }
                 
                 if(cvTempData != null && cvTempData.size() >0){
                     for(int index = 0 ; index < cvTempData.size() ; index++){
                         BudgetModularIDCBean budModIDCBean = 
                                    (BudgetModularIDCBean)cvTempData.get(index);
                         budModIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                         cvDataToEngine.add(budModIDCBean);
                     }
                 }//End of if
                 
             }
         }
//         else{
//             cvDataToEngine.addAll(cvModularIDCData);
//         }//End if
         if(cvDataToEngine!=null){
             for(int index=0;index<cvDataToEngine.size();index++){//change here vector
                 BudgetModularIDCBean budgetModularIDCBean=(BudgetModularIDCBean)cvDataToEngine.get(index);
                 if(budgetModularIDCBean.getAcType() == null){
                         budgetModularIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
                 }
                 if(budgetModularIDCBean.getAcType()!=null){
                     String acType=budgetModularIDCBean.getAcType();
                     
                     if(acType.equals(TypeConstants.INSERT_RECORD)){
                         queryEngine.insert(queryKey,budgetModularIDCBean);
                     }
                     else if(acType.equals(TypeConstants.UPDATE_RECORD)){
                         budgetModularIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                         try{
                             queryEngine.delete(queryKey, budgetModularIDCBean);
                         }
                         catch(CoeusException ce){
                             ce.printStackTrace();
                         }
                         budgetModularIDCBean.setAcType(TypeConstants.INSERT_RECORD);
                         queryEngine.insert(queryKey, budgetModularIDCBean);
                     }else if(acType.equals(TypeConstants.DELETE_RECORD)){
                         budgetModularIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                         try{
                             queryEngine.delete(queryKey,budgetModularIDCBean);
                         }
                         catch(CoeusException ced){
                             ced.printStackTrace();
                         }
                     }
                 }//End if
             }//end for
         }//End if
         hmModularData.put(BudgetModularIDCBean.class, cvDataToEngine);
         cvDataToEngine = null;

     }
    
    /** displays the Form which is being controlled.
     */
    public void display(){
        
    }
//    /**
//     * Method for focus traversing from table to components
//     */    
//    public void setTableKeyTraversal(){
//         javax.swing.InputMap im = budgetModularForms.tblIndirectCosts.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//         KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
//         KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
//         final Action oldTabAction = budgetModularForms.tblIndirectCosts.getActionMap().get(im.get(tab));
//         Action tabAction = new AbstractAction() {
//             int row = 0;
//             int column =0;
//            public void actionPerformed(ActionEvent e) {
//                oldTabAction.actionPerformed( e );
//                JTable table = (JTable)e.getSource();
//                boolean selectionOut=false;
//                int rowCount = table.getRowCount();
//                int columnCount = table.getColumnCount();
//                row = table.getSelectedRow();
//                column = table.getSelectedColumn();
//                if((rowCount-1) ==  row && column ==  (columnCount-1)){
//                        selectionOut = true;
//                        budgetModularIDCEditor.stopCellEditing();
//                        budgetModularForms.tblIndirectCosts.setRowSelectionInterval(0,0);
//                        budgetModularForms.txtDirectCostFA.requestFocusInWindow();
//                }
//                if(rowCount<1){
//                    columnCount = 0;
//                    row = 0;
//                    column = 0;
//                    budgetModularForms.txtDirectCostFA.requestFocusInWindow();
//                    return ;
//               }
//               while (! table.isCellEditable(row, column) ) {
//                    column += 1;
//                    if (column  ==   columnCount) {
//                        column = 0;
//                        row +=1;
//                    }
//                    if (row  ==   rowCount) {
//                        row = 0;
//                   }
//                   if (row  ==   table.getSelectedRow()
//                    && column  ==   table.getSelectedColumn()) {
//                        break;
//                    }
//                }
//                if(!selectionOut){
//                    table.changeSelection(row, column, false, false);
//                }
//             }
//        };
//        budgetModularForms.tblIndirectCosts.getActionMap().put(im.get(tab), tabAction);
//        final Action oldShiftTabAction = budgetModularForms.tblIndirectCosts.getActionMap().get(im.get(shiftTab));
//        Action tabShiftAction = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                oldShiftTabAction.actionPerformed( e );
//                JTable table = (JTable)e.getSource();
//                int rowCount = table.getRowCount();
//                int row = table.getSelectedRow();
//                int column = table.getSelectedColumn();
//                
//                while (! table.isCellEditable(row, column) ) {
//                    if (row < 0) {
//                        row = rowCount-1;
//                    }
//                if (row  ==   table.getSelectedRow()
//                    && column  ==   table.getSelectedColumn()) {
//                        break;
//                    }
//                }
//              table.changeSelection(row, column, false, false);
//            }
//        };
//        budgetModularForms.tblIndirectCosts.getActionMap().put(im.get(shiftTab), tabShiftAction);
//     }
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = budgetModularForms.tblIndirectCosts.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = budgetModularForms.tblIndirectCosts.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                boolean selectionOut=false;
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    
                    if(row==0 && column==1){
                        selectionOut = true;
                        budgetModularForms.txtDirectCostFA.requestFocusInWindow();
                    }
                    
                    if (row == rowCount) {
                        row = 0;
                    }
                     if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                   
                }
                 if(!selectionOut){
                        table.changeSelection(row, column, false, false);
                 }
               // table.changeSelection(row, column, false, false);
            }
        };
        budgetModularForms.tblIndirectCosts.getActionMap().put(im.get(tab), tabAction);
       
        final Action oldTabAction1 = budgetModularForms.tblIndirectCosts.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    
                    column -= 1;
                 
                    if (column <= 0) {
                        column = 4;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    if (row == table.getSelectedRow()
                            && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        budgetModularForms.tblIndirectCosts.getActionMap().put(im.get(shiftTab), tabAction1);
     }
    
    /**
     * Getter for property modularDataObject.
     * @return Value of property modularDataObject.
     */
    public java.util.Hashtable getModularDataObject() {
        return modularDataObject;
    }    
    
    /**
     * Setter for property modularDataObject.
     * @param modularDataObject New value of property modularDataObject.
     */
    public void setModularDataObject(java.util.Hashtable modularDataObject) {
        this.modularDataObject = modularDataObject;
        
        cvModularDCData = (CoeusVector)modularDataObject.get(BudgetModularBean.class);
        cvModularIDCData = (CoeusVector)modularDataObject.get(BudgetModularIDCBean.class);
        
        hmModularData = new HashMap();
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        CoeusVector cvPeriodBeans = new CoeusVector();
       
       try{
            cvPeriodBeans = queryEngine.executeQuery(queryKey,budgetPeriodBean);
             if(cvPeriodBeans!=null && cvPeriodBeans.size()>0){
                cvPeriodBeans = cvPeriodBeans.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            }
            //Case 2260 Start 12
            //this is for again setting the data for new periods
            /*cvModularDCData = getDCModularDetails(cvPeriodBeans,cvModularDCData);
            cvModularIDCData = getIDCModularDetails(cvPeriodBeans,cvModularIDCData);
            if(cvModularIDCData!=null&&cvModularIDCData.size()>0){
                cvModularIDCData.sort("budgetPeriod",true);
            }
            autoSyncAction(cvModularDCData,cvModularIDCData);
            if(cvModularDCData == null || cvModularDCData.size() == 0){
                cvModularDCData = new CoeusVector();
                setModularDCData(cvModularDCData, cvPeriodBeans);
            }
            if(cvModularIDCData == null || cvModularIDCData.size() == 0){
                cvModularIDCData = new CoeusVector();    
                setModularIDCData(cvModularIDCData, cvPeriodBeans);
            }*/
            //Case 2260 End 12
            cvModularDCData = cvModularDCData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            cvModularIDCData = cvModularIDCData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            
            //Case 2260 Start 13
            if(cvModularIDCData == null){
                cvModularIDCData = new CoeusVector();
            }
            //Case 2260 End 13
            
            budgetModularIDCTableModel.setData(cvModularIDCData);
            budgetModularIDCTableModel.fireTableDataChanged();
         
            budgetModularTotalIDCTableModel.fireTableDataChanged();
        
            budgetModularTotalDCNIDCTableModel.fireTableDataChanged();
            
             //Case 2260 Start 14
            if(cvModularDCData == null || cvModularDCData.size() == 0){
                BudgetModularBean budModBean = new BudgetModularBean();

                budModBean.setDirectCostFA(0);
                budModBean.setConsortiumFNA(0);
                budModBean.setTotalDirectCost(0);
                budModBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                budModBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                budModBean.setBudgetPeriod(periodNumber);
                cvModularDCData.add(budModBean);
            }
            CoeusVector cvData = queryEngine.executeQuery(queryKey,BudgetModularIDCBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            cvData = cvData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            if(cvData != null && cvData.size() > 0){
                for(int index =0 ; index < cvData.size() ; index++){
                    BudgetModularIDCBean budgetModularIDCBean = 
                                        (BudgetModularIDCBean)cvData.get(index);
                    if(budgetModularIDCBean.getAcType() == null || 
                       budgetModularIDCBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                           budgetModularIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                           if(cvDelIDCData == null){
                               cvDelIDCData = new CoeusVector();
                           }
                           cvDelIDCData.add(budgetModularIDCBean);
                    }
                }
            }
            
            if(cvModularIDCData != null && cvModularIDCData.size() >0){
                budgetModularForms.tblIndirectCosts.setRowSelectionInterval(0 , 0);
            }
            //Case 2260 End 14
            
            BudgetModularBean budgetModularBean
                =(BudgetModularBean)cvModularDCData.get(0);
            budgetModularForms.txtDirectCostFA.setValue(budgetModularBean.getDirectCostFA());
            budgetModularForms.txtConsortiumFA.setValue(budgetModularBean.getConsortiumFNA());
            double totalDC = budgetModularBean.getDirectCostFA()+budgetModularBean.getConsortiumFNA();
            budgetModularForms.txtTotalDirectCost.setValue(totalDC);
            hmModularData.put(BudgetModularBean.class, cvModularDCData);
            hmModularData.put(BudgetModularIDCBean.class, cvModularIDCData );
            
       }catch (CoeusException ex){
           ex.printStackTrace();
           CoeusOptionPane.showErrorDialog(ex.getMessage());
       }
    }
    
    /** 
     *An inner class provides the model for the Budget Modular
     *
     */
    class BudgetModularIDCTableModel extends javax.swing.table.AbstractTableModel{
        
        private Class colClass[] = {Integer.class,String.class, Double.class,Double.class,Double.class};
        private String colNames[] = {"Rate Number","Indirect Cost Type","IDC Rate (%)"
                            ,"IDC Base","Funds Requested"};
        private CoeusVector cvData;
        
        public boolean isCellEditable(int row, int col){
            boolean returnValue 
                = (getFunctionType()!=TypeConstants.DISPLAY_MODE) ? true : false;
            if(returnValue){
                if(col == IDC_RATE_NUMBER){
                    return false;
                }else{
                    return returnValue;
                }
            }
            return returnValue;
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int column){
            return colNames[column];
        }
        
        public void setData(CoeusVector cvData){
            cvData = cvData.filter(new Equals("budgetPeriod", new Integer(periodNumber)));
            this.cvData = cvData;
            
        }
        public int getRowCount() {
            int rowCount = (cvData == null || cvData.size() == 0) ? 0 : cvData.size();
            return rowCount;
         }
        
        public Object getValueAt(int row, int col) {
           BudgetModularIDCBean budgetModularIDCBean = (BudgetModularIDCBean)cvData.get(row);
           if(budgetModularIDCBean!=null&&cvData!=null&&cvData.size()>0){
             switch(col){
                   case IDC_RATE_NUMBER:
                        return new Integer(budgetModularIDCBean.getRateNumber());
                   case IDC_TYPE_COLUMN:
                       return budgetModularIDCBean.getDescription();
                   case IDC_RATE_COLUMN:
                       return new Double(budgetModularIDCBean.getIdcRate());
                   case IDC_BASE_COLUMN:
                       return new Double(budgetModularIDCBean.getIdcBase());
                   case FUNDS_COLUMN:
                       return new Double(budgetModularIDCBean.getFundRequested());
               }
           }
            return EMPTY_STRING;
        }
        public void setValueAt(Object value,int rowIndex,int colIndex){
            if(cvData==null||cvData.size()==0){
                return;
            }
            boolean modified = false;
            BudgetModularIDCBean budgetModularIDCBean = (BudgetModularIDCBean)cvData.get(rowIndex);
            switch(colIndex){
                case IDC_TYPE_COLUMN:
                    String beanType=budgetModularIDCBean.getDescription();
                    String type = (String)value;
                    if(!beanType.equals(type)){
                        setSaveRequired(true);
                        modified = true;
                        budgetModularIDCBean.setDescription(type.trim());
                        if(budgetModularIDCBean.getAcType() == null){
                            budgetModularIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                    }
                    break;
                case IDC_RATE_COLUMN:
                    double idcRate=0.00;
                            idcRate=new Double(value.toString()).doubleValue();
                    double beanIdcRate = budgetModularIDCBean.getIdcRate();
                    if(beanIdcRate != idcRate){
                        setSaveRequired(true);
                        modified = true;
                        budgetModularIDCBean.setIdcRate(idcRate);
                        if(budgetModularIDCBean.getAcType() == null){
                            budgetModularIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                    }
                    break;
                case IDC_BASE_COLUMN:
                    double idcBase=0.00;
                            idcBase=new Double(value.toString()).doubleValue();
                    double beanIdcBase = budgetModularIDCBean.getIdcBase();
                    if(beanIdcBase != idcBase){
                        setSaveRequired(true);
                        modified = true;
                        budgetModularIDCBean.setIdcBase(idcBase);
                        if(budgetModularIDCBean.getAcType() == null){
                            budgetModularIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                    }
                    break;
                case FUNDS_COLUMN:
                    double fundRequested = 0.00;
                            fundRequested = new Double(value.toString()).doubleValue();
                    double beanFundRequested = budgetModularIDCBean.getFundRequested();
                    if(beanFundRequested != fundRequested){
                        setSaveRequired(true);
                        modified = true;
                        budgetModularIDCBean.setFundRequested(fundRequested);
                        if(budgetModularIDCBean.getAcType() == null){
                            budgetModularIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                    }
                    if(modified){
                        budgetModularTotalIDCTableModel.fireTableDataChanged();
                        budgetModularTotalDCNIDCTableModel.fireTableDataChanged();
                    }
                    break;
            }
        }
     }
    /*** 
     * Table editor for Budget Modular
     */
     class BudgetModularIDCEditor extends DefaultCellEditor {
         private int column;
         private JLabel lblRateNumber;
         private CoeusTextField txtIDCType;
         private CurrencyField txtIDCRate;
         private DollarCurrencyTextField txtIDCBase;
         private DollarCurrencyTextField txtFundsRequested;
         public BudgetModularIDCEditor() {
            super(new JComboBox());
            lblRateNumber =  new JLabel();
            lblRateNumber.setOpaque(true);
            txtIDCType=new CoeusTextField();
            txtIDCRate = new CurrencyField();
            txtIDCBase = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,false);
            txtFundsRequested = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,false);
        }
       public Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch (column) {
                case IDC_RATE_NUMBER:
                    value = (value == null) ? EMPTY_STRING : value;
                    lblRateNumber.setText(value.toString());
                    return lblRateNumber;
                case IDC_TYPE_COLUMN:
                    txtIDCType.setDocument(new LimitedPlainDocument(60));
                    value = (value == null) ? EMPTY_STRING : value;
                    txtIDCType.setText(value.toString().trim());
                    return txtIDCType;
                case IDC_RATE_COLUMN:
                    value = (value == null) ? "0.00" : value;
                    txtIDCRate.setText(value.toString());
                    return txtIDCRate;
                case IDC_BASE_COLUMN:
                    if(value== null){
                        txtIDCBase.setValue(0.00);
                    }else{
                        // txtIDCBase.setValue(0.00);
                        txtIDCBase.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtIDCBase;
                case FUNDS_COLUMN:
                    if(value== null){
                        txtFundsRequested.setValue(0.00);
                    }else{
                       // txtFundsRequested.setValue(0.00);
                        txtFundsRequested.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtFundsRequested;
            }
            return lblRateNumber;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case IDC_RATE_NUMBER:
                    return lblRateNumber;
                case IDC_TYPE_COLUMN:
                    return txtIDCType.getText();
                case IDC_RATE_COLUMN:
                    return txtIDCRate.getText();
                case IDC_BASE_COLUMN:
                    return txtIDCBase.getValue();
                case FUNDS_COLUMN:
                    return txtFundsRequested.getValue();
            }
            return lblRateNumber;
        }
        //Added for modular budget enhancement for editing cell start 4
        public int getClickCountToStart(){
            return 1;
        }
        //Added for modular budget enhancement for editing cell end 4
      }
     /** 
      * Table Renderer for Budget Modular table
      */
     class BudgetModularIDCCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblComponent;
        private DollarCurrencyTextField txtDollar;
        private CoeusTextField txtComponent;
        public BudgetModularIDCCellRenderer(){
            lblComponent=new JLabel();
            txtComponent = new CoeusTextField();
            lblComponent.setOpaque(true);
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            txtDollar =  new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
        }
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected,
                                                    boolean hasFocus, int row, int col){
           switch(col){
              case IDC_RATE_NUMBER:
                  lblComponent.setHorizontalAlignment(CENTER);  
                  if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value = (value==null ?EMPTY_STRING :value );
                    lblComponent.setText(value.toString().trim());
                    return lblComponent; 
              case IDC_TYPE_COLUMN:
                  lblComponent.setHorizontalAlignment(LEFT);  
                  if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.WHITE);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }
                    value = (value==null ?EMPTY_STRING :value );
                    txtComponent.setText(value.toString().trim());
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                case IDC_RATE_COLUMN:
                    lblComponent.setHorizontalAlignment(RIGHT);
                    if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.WHITE);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                     }
                    if(value != null){
                        // txtComponent.setText("0.00");
                        txtComponent.setText(value.toString());
                    }else{
                        txtComponent.setText("0.00");
                    }
                    lblComponent.setText(txtComponent.getText());
                    return lblComponent;
                case IDC_BASE_COLUMN:
                    lblComponent.setHorizontalAlignment(RIGHT);
                    if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.WHITE);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                     }
                    if(value == null){
                        txtDollar.setValue(0.00);
                    }else{
                       // txtDollar.setValue(0.00);
                        txtDollar.setValue(new Double(value.toString()).doubleValue());
                    }
                    
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;                  
                    
                case FUNDS_COLUMN:
                    lblComponent.setHorizontalAlignment(RIGHT);
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.WHITE);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }
                    if(value == null){
                        txtDollar.setValue(0.00);
                    }else{
                      //  txtDollar.setValue(0.00);
                        txtDollar.setValue(new Double(value.toString()).doubleValue());
                    }
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;        
            }
            return lblComponent;
        }
      }
     /** 
     *An inner class provides the model for the Budget Modular Total
     *
     */
    class BudgetModularTotalIDCTableModel extends javax.swing.table.AbstractTableModel{
        
        private Class colClass[] = {String.class,Double.class};
        private String colNames[] = {"Total Indirect Costs: ",EMPTY_STRING};
        private final String strTotal="Total Indirect Costs: ";
        CoeusVector cvData;
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int column){
            return colNames[column];
        }
        public int getRowCount() {
            return 1;
        }
        
        public void setData(CoeusVector cvData){
           // this.cvData = cvData;
        }
        public Object getValueAt(int row, int col) {
               switch(col){
                   case TOTAL_LABEL_COLUMN:
                       return strTotal;
                   case TOTAL_COLUMN:
                       //return new Double(0.00);
                       return new Double(cvModularIDCData.sum("fundRequested"));
               }
            return EMPTY_STRING;
        }
      }
    class BudgetModularTotalDCNIDCTableModel extends javax.swing.table.AbstractTableModel{
        
        private Class colClass[] = {String.class,Double.class};
        private String colNames[] = {"Funds Requested: ",EMPTY_STRING};
        private final String strTotal="Funds Requested: ";
        CoeusVector cvData;
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int column){
            return colNames[column];
        }
        public int getRowCount() {
            return 1;
        }
        
        public void setData(CoeusVector cvData){
          //  this.cvData = cvData;
        }
        public Object getValueAt(int row, int col) {
               switch(col){
                   case TOTAL_LABEL_COLUMN:
                       return strTotal;
                   case TOTAL_COLUMN:
                       double totalDCNIDC 
                        = (new Double(budgetModularForms.txtTotalDirectCost.getValue()).doubleValue()
                            + cvModularIDCData.sum("fundRequested"));
                       //return new Double(0.00);
                       return new Double(totalDCNIDC);
               }
            return EMPTY_STRING;
        }
      }
    /** 
      * Table Renderer for Budget Modular Total table
      */
     class ModularTotalTableCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblComponent;
        private DollarCurrencyTextField txtDollar;
        public ModularTotalTableCellRenderer(){
            lblComponent=new JLabel();
            lblComponent.setOpaque(true);
            txtDollar =  new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                    boolean hasFocus, int row, int col){
           switch(col){
              case TOTAL_LABEL_COLUMN:
                  lblComponent.setHorizontalAlignment(RIGHT);
                  lblComponent.setFont(CoeusFontFactory.getLabelFont());
                  lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                  lblComponent.setForeground(java.awt.Color.black);
                  value=(value==null?EMPTY_STRING:value);
                  lblComponent.setText(value.toString().trim());
                  return lblComponent;
                case TOTAL_COLUMN:
                    value = (value == null?"0.00":value);
                    lblComponent.setHorizontalAlignment(RIGHT);
                    lblComponent.setFont(CoeusFontFactory.getNormalFont());
                    lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    lblComponent.setForeground(java.awt.Color.black);
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;
            }
            return lblComponent;
        }
    }
     
     //Case 2260 Start 15
     public void actionPerformed(ActionEvent e) {
         Object source = e.getSource();
         budgetModularIDCEditor.stopCellEditing();
         if(source.equals(budgetModularForms.btnAdd)){
             performAddAction();
         }else if(source.equals(budgetModularForms.btnDelete)){
             performDeleteAction();
         }
     }
     
     private void performAddAction(){
        BudgetModularIDCBean budgetModularIDCBean = new BudgetModularIDCBean();
        budgetModularIDCBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetModularIDCBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        budgetModularIDCBean.setBudgetPeriod(periodNumber);
        if(cvModularIDCData == null || cvModularIDCData.size() == 0){
            budgetModularIDCBean.setRateNumber(1);
            // Since the BudgetModularIDCBean bean equals method will check for the awRateNumber equality
            // To make the awRateNumber unique across the beans, awRateNumber is provided even for the newly added Bean
            budgetModularIDCBean.setAwRateNumber(1);
        }else{
            budgetModularIDCBean.setRateNumber(cvModularIDCData.size()+1);
            //budgetModularIDCBean.setAwRateNumber(cvModularIDCData.size()+1);
            // Added for COEUSQA-3303 : Error on Modular Budget - Start
            // Since the BudgetModularIDCBean bean equals method will check for the awRateNumber equality
            // To make the awRateNumber unique across the beans in query engine, awRateNumber is provided even for the newly added Bean
            try{
                // Gets all the BudgetModularIDCBean beans in query engine
                Operator operToGetAllBeans = new Or(new Equals("acType", null),new NotEquals("acType",null));
                Operator operPeriodData =  new And(operToGetAllBeans,new Equals("budgetPeriod",new Integer(periodNumber)));
                CoeusVector cvTempModularIDCData = queryEngine.executeQuery(queryKey,BudgetModularIDCBean.class,operPeriodData);
                // cvModularIDCData will have the udpated beans
                // Removes all the beans in cvTempModularIDCData which is already in cvModularIDCData collection, to get the updated beans
                // ands adds all the cvModularIDCData beans to cvTempModularIDCData
                if(cvModularIDCData != null && !cvModularIDCData.isEmpty()){
                    cvTempModularIDCData.removeAll(cvModularIDCData);
                    cvTempModularIDCData.addAll(cvModularIDCData);
                }
                // cvDelIDCData will have the udpated beans
                // Removes all the beans in cvTempModularIDCData which is already in cvDelIDCData collection, to get the updated beans
                // ands adds all the cvDelIDCData beans to cvTempModularIDCData
                if(cvDelIDCData != null && !cvDelIDCData.isEmpty()){
                    cvTempModularIDCData.removeAll(cvDelIDCData);
                    cvTempModularIDCData.addAll(cvDelIDCData);
                }
                // Sorts the cvTempModularIDCData based on the awRateNumber in ascending order to get the max awRateNumber
                // And new bean awRateNumber property will be udpated with increment for max awRateNumber 
                if(cvTempModularIDCData!= null && !cvTempModularIDCData.isEmpty()){
                    if(cvTempModularIDCData.size() > 1){
                        cvTempModularIDCData.sort("awRateNumber",true);
                    }
                    budgetModularIDCBean.setAwRateNumber(((BudgetModularIDCBean)cvTempModularIDCData.get(cvTempModularIDCData.size()-1)).getAwRateNumber()+1);
                }else{
                    budgetModularIDCBean.setAwRateNumber(1);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            // Added for COEUSQA-3303 : Error on Modular Budget - start
        }
        
        budgetModularIDCBean.setDescription("");
        budgetModularIDCBean.setIdcRate(0);
        budgetModularIDCBean.setIdcBase(0);
        budgetModularIDCBean.setFundRequested(0);
        budgetModularIDCBean.setAcType(TypeConstants.INSERT_RECORD);
        cvModularIDCData.add(budgetModularIDCBean);
        
        budgetModularIDCTableModel.setData(cvModularIDCData);
        budgetModularIDCTableModel.fireTableDataChanged();
        
        budgetModularForms.tblIndirectCosts.setRowSelectionInterval(
                        cvModularIDCData.size()-1 , cvModularIDCData.size()-1);
        setSaveRequired(true);
        enableDisableComp();
     }
     
     private void performDeleteAction(){
         int selRow = budgetModularForms.tblIndirectCosts.getSelectedRow();
         
         if(selRow != -1){
             String mesg = DELETE_CONFIRMATION;
             int selectedOption = CoeusOptionPane.showQuestionDialog(
             coeusMessageResources.parseMessageKey(mesg),
             CoeusOptionPane.OPTION_YES_NO,
             CoeusOptionPane.DEFAULT_YES);
             if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                 BudgetModularIDCBean budgetModularIDCBean =
                 (BudgetModularIDCBean)cvModularIDCData.get(selRow);
                 
                 if(budgetModularIDCBean.getAcType() == null ||
                 budgetModularIDCBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                     budgetModularIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                     if( cvDelIDCData == null){
                         cvDelIDCData = new CoeusVector();
                     }
                     cvDelIDCData.add(budgetModularIDCBean);
                 }else if(TypeConstants.INSERT_RECORD.equals(budgetModularIDCBean.getAcType())){
                     // During deletion of the new bean, it will permanently deleted from the query engine
                     try{
                     queryEngine.delete(queryKey,budgetModularIDCBean);
                     } catch(CoeusException ced){
                         ced.printStackTrace();
                     }
                 }
                 cvModularIDCData.remove(selRow);
                 //Reset the rateNumber
                 for(int index =0 ; index< cvModularIDCData.size(); index++){
                     BudgetModularIDCBean budgetModIDCBean =
                     (BudgetModularIDCBean)cvModularIDCData.get(index);
                     
                     budgetModIDCBean.setRateNumber(index+1);
                     if(budgetModIDCBean.getAcType() == null){
                         budgetModIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
                     }
                 }//End of for
                 budgetModularIDCTableModel.setData(cvModularIDCData);
                 budgetModularIDCTableModel.fireTableDataChanged();
                 
                 if(selRow >0){
                    budgetModularForms.tblIndirectCosts.setRowSelectionInterval(
                    selRow-1,selRow-1);
                    budgetModularForms.tblIndirectCosts.setColumnSelectionInterval(1,1);
                    budgetModularForms.tblIndirectCosts.scrollRectToVisible(
                    budgetModularForms.tblIndirectCosts.getCellRect(
                    selRow -1 ,1, true));
                    budgetModularForms.tblIndirectCosts.editCellAt(selRow,1);
                }else{
                    if(budgetModularForms.tblIndirectCosts.getRowCount()>0){
                        budgetModularForms.tblIndirectCosts.setRowSelectionInterval(0,0);
                    }
                }
                 setSaveRequired(true);
                 enableDisableComp();
             }
         }
     }//End of performDeleteAction
     
     private CoeusVector validateData(CoeusVector cvData , double totalDirectCost, boolean dcModified){
         NotEquals neDesc = new NotEquals("description" , "");
         NotEquals neIDCRate = new NotEquals("idcRate" , new Double(0));
         NotEquals neIDCBase = new NotEquals("idcBase" , new Double(0));
         NotEquals neFundReq = new NotEquals("fundRequested" , new Double(0));
         
         /*And neDescAndneIDCRate = new And(neDesc , neIDCRate);
         And neIDCBaseAndneFundReq = new And(neIDCBase , neFundReq);
         And filterAnd = new And(neDescAndneIDCRate , neIDCBaseAndneFundReq);*/
         Or neDescOrneIDCRate = new Or(neDesc , neIDCRate);
         Or neIDCBaseOrneFundReq = new Or(neIDCBase , neFundReq);
         Or filterOr = new Or(neDescOrneIDCRate , neIDCBaseOrneFundReq);
         
         CoeusVector cvTempData = cvModularIDCData.filter(filterOr);
         
         if(cvTempData.size() == 0 && cvData.size() > 0 && totalDirectCost == 0){
             BudgetModularBean budgetModularBean = (BudgetModularBean)cvData.get(0);
             if(budgetModularBean.getAcType() == null ||
                budgetModularBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                    budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
             }else if(budgetModularBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                 CoeusVector cvTemp = null;
                 try{
                     cvTemp = queryEngine.executeQuery(queryKey, BudgetModularBean.class , new Equals("budgetPeriod" , new Integer(periodNumber)));
                 }catch (CoeusException cEx){
                     cEx.printStackTrace();
                 }
                 if(cvTemp != null && cvTemp.size() >0){
                     budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
                 }else{
                     budgetModularBean.setAcType(null);
                 }
             }
         }else if(cvTempData.size() == 0 && totalDirectCost == 0 && dcModified){
             BudgetModularBean budgetModularBean = (BudgetModularBean)cvData.get(0);
             if(budgetModularBean.getAcType() == null ||
                budgetModularBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                    budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
             }else if(budgetModularBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                 budgetModularBean.setAcType(null);
             }
         }
         
         validateIDCData();
         int rateNo = 1;
         for(int index =0 ; index< cvModularIDCData.size(); index++){
             
             BudgetModularIDCBean budgetModIDCBean =
             (BudgetModularIDCBean)cvModularIDCData.get(index);
             
             if(budgetModIDCBean.getAcType() == null || 
                !budgetModIDCBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                    budgetModIDCBean.setRateNumber(rateNo);
                    rateNo = rateNo + 1;
                    /*if(!isSaveRequired()){
                        setSaveRequired(true);
                    }*/
             }
             
             if(budgetModIDCBean.getAcType() == null){
                 /*if(!isSaveRequired()){
                     setSaveRequired(true);
                 }*/
                 budgetModIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
             }//End of if
         }//End of for
         
         return cvData;
     }
     
     private void validateIDCData(){
         if(cvModularIDCData != null && cvModularIDCData.size() > 0){
              Equals eqDesc = new Equals("description" , "");
              Equals eqIDCRate = new Equals("idcRate" , new Double(0));
              Equals eqIDCBase = new Equals("idcBase" , new Double(0));
              Equals eqFundReq = new Equals("fundRequested" , new Double(0));
              //Equals eqAcType = new Equals("acType" , TypeConstants.INSERT_RECORD);

              And eqDescAndeqIDCRate = new And(eqDesc , eqIDCRate);
              And eqIDCBaseAndeqFundReq = new And(eqIDCBase , eqFundReq);
              //And tempAnd = new And(eqDescAndeqIDCRate , eqIDCBaseAndeqFundReq);
              And filterAnd= new And(eqDescAndeqIDCRate , eqIDCBaseAndeqFundReq);
              
              CoeusVector cvTemp = cvModularIDCData.filter(filterAnd);
              //cvModularIDCData = cvModularIDCData.filter(filterAnd);
              if(cvTemp != null && cvTemp.size() > 0){
                   for(int index = 0 ; index<cvTemp.size(); index++){
                       BudgetModularIDCBean budgetModularIDCBean = 
                                        (BudgetModularIDCBean)cvTemp.get(index);
                       if(budgetModularIDCBean.getAcType() == null ||
                          budgetModularIDCBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                              budgetModularIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                       }
                   }//End of for
              }//End of If
              
              CoeusVector cvFilter = new CoeusVector();
              //cvFilter.addAll(cvModularIDCData);
              for(int i =0 ; i<cvModularIDCData.size(); i++){
                   BudgetModularIDCBean budModIDCBean = 
                                        (BudgetModularIDCBean)cvModularIDCData.get(i);
                   if(budModIDCBean.getDescription().equals("") &&
                      budModIDCBean.getIdcRate() == 0 &&
                      budModIDCBean.getIdcBase() == 0 && 
                      budModIDCBean.getFundRequested() == 0 && 
                      budModIDCBean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
                          continue;
                   }else{
                       cvFilter.add(budModIDCBean);
                   }
                       
              }
              
              if(cvFilter != null){
                  cvModularIDCData = cvFilter;
              }
              
              /*NotEquals neDesc = new NotEquals("description" , "");
              NotEquals neIDCRate = new NotEquals("idcRate" , new Double(0));
              NotEquals neIDCBase = new NotEquals("idcBase" , new Double(0));
              NotEquals eqFundReq = new NotEquals("fundRequested" , new Double(0));
              Equals eqAcType = new Equals("acType" , TypeConstants.INSERT_RECORD);

              And eqDescAndeqIDCRate = new And(eqDesc , eqIDCRate);
              And eqIDCBaseAndeqFundReq = new And(eqIDCBase , eqFundReq);
              //And tempAnd = new And(eqDescAndeqIDCRate , eqIDCBaseAndeqFundReq);
              And filterAnd= new And(eqDescAndeqIDCRate , eqIDCBaseAndeqFundReq);*/
              
         }
     }//validateIDCData
     
     
     public void enableDisableComp(){
         if(getFunctionType() == TypeConstants.DISPLAY_MODE){
             budgetModularForms.btnAdd.setEnabled(false);
             budgetModularForms.btnDelete.setEnabled(false);
         }else{
             if(cvModularIDCData.size() > 0){
                 budgetModularForms.btnAdd.setEnabled(true);
                 budgetModularForms.btnDelete.setEnabled(true);
             }else{
                 budgetModularForms.btnDelete.setEnabled(false);
             }
        }
     }
    
     //Case 2260 End 15
     
//    public static void main(String a[]){
//        BudgetModularFormsController budgetModularFormsController
//            = new BudgetModularFormsController();
//    }
  }

