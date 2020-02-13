/*
 * BudgetModularController.java
 *
 * Created on September 20, 2005, 12:11 PM
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.gui.BudgetModularForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
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
public class BudgetModularController extends Controller implements java.awt.event.ActionListener{
    
    private static final int PERIOD_COLUMN=0;
    private static final int DCLESS_FA_COLUMN=1;
    private static final int CONS_FA_COLUMN=2;
    private static final int TOTAL_COST_COLUMN=3;
    private static String EMPTY_STRING="";
    private BudgetModularForm budgetModularForm; 
    private static final int WIDTH=585;
    private static final int HEIGHT=283;
    private static final String WINDOW_TITLE="Modular Budget";
    private edu.mit.coeus.gui.CoeusDlgWindow dlgBudgetModular;
    private edu.mit.coeus.gui.CoeusAppletMDIForm mdiForm 
                            = edu.mit.coeus.utils.CoeusGuiConstants.getMDIForm();
    private CoeusMessageResources coeusMessageResources;
    private BudgetModularTableModel budgetModularTableModel;
    private BudgetModularTotalTableModel budgetModularTotalTableModel;;
    private BudgetModularEditor budgetModularEditor;
    private BudgetModularTableCellRenderer budgetModularTableCellRenderer;
    private ModularTotalTableCellRenderer modularTotalTableCellRenderer;
    private QueryEngine queryEngine;
    private String queryKey;
    private BudgetInfoBean budgetInfoBean;
    private CoeusVector cvData;
    private CoeusVector cvModularData;
    private boolean modified=false;
    private static final String SAVE_CHANGES="budget_saveChanges_exceptionCode.1210";
    
    /** Creates a new instance of BudgetModularController */
    public BudgetModularController(BudgetInfoBean budgetInfoBean) throws CoeusException{
        coeusMessageResources=CoeusMessageResources.getInstance();
        this.budgetInfoBean = budgetInfoBean;
        queryEngine = QueryEngine.getInstance();
        queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        registerComponents();
        setTableEditors();
        formatFields();
        setTableKeyTraversal();
        postInitComponents();
       
    }
    public void display() {
        dlgBudgetModular.setVisible(true);
    }
    
    public void formatFields() {
        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
            budgetModularForm.btnOk.setEnabled(false);
        }
    }
    
    public java.awt.Component getControlledUI() {
        return budgetModularForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        budgetModularForm=new BudgetModularForm();
        budgetModularTableModel=new BudgetModularTableModel();
        budgetModularTotalTableModel=new BudgetModularTotalTableModel();
        budgetModularEditor=new BudgetModularEditor();
        budgetModularTableCellRenderer=new BudgetModularTableCellRenderer();
        modularTotalTableCellRenderer=new ModularTotalTableCellRenderer();
        budgetModularForm.tblBudgetModular.setModel(budgetModularTableModel);
        budgetModularForm.tblBudgetModularTotal.setModel(budgetModularTotalTableModel);
        java.awt.Component[] components = {
            budgetModularForm.btnOk, budgetModularForm.btnCancel,
                budgetModularForm.scrPnBudgetModular
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetModularForm.setFocusTraversalPolicy(traversePolicy);
        budgetModularForm.setFocusCycleRoot(true);
        
        budgetModularForm.btnOk.addActionListener(this);
        budgetModularForm.btnCancel.addActionListener(this);
        
    }
     public void postInitComponents() throws CoeusException{
        dlgBudgetModular = new edu.mit.coeus.gui.CoeusDlgWindow(mdiForm);
        dlgBudgetModular.setResizable(false);
        dlgBudgetModular.setModal(true);
        dlgBudgetModular.setTitle(WINDOW_TITLE );
        dlgBudgetModular.getContentPane().add(budgetModularForm);
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
                }catch (CoeusException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
        
        dlgBudgetModular.addEscapeKeyListener(new javax.swing.AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                try{
                    performCancelAction();
                }catch (CoeusException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
    }
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = budgetModularForm.tblBudgetModular.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setMaximumSize(new java.awt.Dimension(100,24));
            tableHeader.setMinimumSize(new java.awt.Dimension(100,24));
            tableHeader.setPreferredSize(new java.awt.Dimension(100,24));
            tableHeader.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
            budgetModularForm.tblBudgetModular.setRowHeight(22);
            budgetModularForm.tblBudgetModular.setShowHorizontalLines(true);
            budgetModularForm.tblBudgetModular.setShowVerticalLines(true);
            budgetModularForm.tblBudgetModular.setOpaque(false);
            budgetModularForm.tblBudgetModular.setSelectionMode(
                                    DefaultListSelectionModel.SINGLE_SELECTION);
            budgetModularForm.tblBudgetModular.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            budgetModularForm.tblBudgetModularTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            budgetModularForm.tblBudgetModularTotal.setRowHeight(22);
            budgetModularForm.tblBudgetModularTotal.setShowVerticalLines(false);
            budgetModularForm.tblBudgetModularTotal.setShowHorizontalLines(false);
            budgetModularForm.tblBudgetModularTotal.setOpaque(true);
            TableColumn columnModular;
            TableColumn columnModularTotal;
            int size[] = {60,160,120,120};
            for(int index=0;index<size.length;index++){
                columnModular=budgetModularForm.tblBudgetModular.getColumnModel().getColumn(index);
                columnModular.setPreferredWidth(size[index]);
                columnModular.setCellEditor(budgetModularEditor);
                columnModular.setCellRenderer(budgetModularTableCellRenderer);
                columnModularTotal=budgetModularForm.tblBudgetModularTotal.getColumnModel().getColumn(index);
                columnModularTotal.setPreferredWidth(size[index]);
                columnModularTotal.setHeaderRenderer(new EmptyHeaderRenderer());
                columnModularTotal.setCellRenderer(modularTotalTableCellRenderer);
                
            }
         }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
     public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
     public void saveFormData(){
         CoeusVector cvTemp=new CoeusVector();
         if(modified){
             if(cvModularData!=null&&cvModularData.size()>0)
                 cvTemp.addAll(cvModularData);
         }
         else{
             cvTemp.addAll(cvModularData);
         }//End if
         CoeusVector cvDataToEngine=new CoeusVector();
         if(cvTemp!=null){
             
             for(int index=0;index<cvTemp.size();index++){
                 BudgetModularBean budgetModularBean=(BudgetModularBean)cvTemp.get(index);
                 double totalDirectCost=budgetModularBean.getDirectCostFA()
                 +budgetModularBean.getConsortiumFNA();
                 budgetModularBean.setTotalDirectCost(totalDirectCost);
                 String acType=budgetModularBean.getAcType();
                 if(acType==null){
                     budgetModularBean.setAcType(TypeConstants.UPDATE_RECORD);
                     if(totalDirectCost<=0){
                         budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
                     }//End if
                     cvDataToEngine.add(budgetModularBean);
                 }else if(acType.equals(TypeConstants.INSERT_RECORD)){
                     if(totalDirectCost>0){
                         cvDataToEngine.add(budgetModularBean);
                     }//End if
                 }else if(acType!=null&&!acType.equals(TypeConstants.INSERT_RECORD)){
                     if(acType.equals(TypeConstants.UPDATE_RECORD)){
                         if(totalDirectCost<=0){
                             budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
                         }//End if
                     }//End if
                     if(acType.equals(TypeConstants.DELETE_RECORD)){
                         if(totalDirectCost>0){
                             budgetModularBean.setAcType(TypeConstants.UPDATE_RECORD);
                         }//End if
                     }//End if
                     cvDataToEngine.add(budgetModularBean);
                 }//End if
             }//End for
         }
         
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
     }
    
    public void setFormData(Object data){
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        CoeusVector cvPeriodBeans = new CoeusVector();
        try{
            cvPeriodBeans = queryEngine.executeQuery(queryKey,budgetPeriodBean);
            if(cvPeriodBeans!=null && cvPeriodBeans.size()>0){
                cvPeriodBeans = cvPeriodBeans.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            }
            cvData = queryEngine.executeQuery(queryKey,BudgetModularBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            cvModularData=getModularDetails(cvPeriodBeans,cvData);
            if(cvModularData!=null&&cvModularData.size()>0){
                cvModularData.sort("budgetPeriod",true);
            }
            budgetModularTableModel.setData(cvModularData);
            budgetModularTableModel.fireTableDataChanged();
            
        }catch(CoeusException ce){
            ce.printStackTrace();
        }
    }
    private CoeusVector getModularDetails(CoeusVector cvPeriod,CoeusVector cvData) 
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
          }else{
            if(cvPeriod!=null&&cvPeriod.size()>0){
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
                    cvModularData.add(budgetModularBean);
                  }
                return cvModularData;
            }else{
                return null;
            }//End if
        }//End if
    }
    private void requestDefaultFocus(){
        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
            if(budgetModularForm.tblBudgetModular.getRowCount()>0){
                
                budgetModularForm.tblBudgetModular.setRowSelectionInterval(0,0);
                budgetModularForm.tblBudgetModular.scrollRectToVisible(
                    budgetModularForm.tblBudgetModular.getCellRect(0,0,true));
                budgetModularForm.btnCancel.requestFocusInWindow();
            }else{
                budgetModularForm.btnCancel.requestFocusInWindow();
            }
        }else{
            if(budgetModularForm.tblBudgetModular.getRowCount()>0){
                budgetModularForm.tblBudgetModular.setRowSelectionInterval(0,0);
                budgetModularForm.tblBudgetModular.setColumnSelectionInterval(0,1);
                budgetModularForm.tblBudgetModular.scrollRectToVisible(
                    budgetModularForm.tblBudgetModular.getCellRect(0,1,true));
                budgetModularForm.tblBudgetModular.editCellAt(0,DCLESS_FA_COLUMN);
                budgetModularForm.tblBudgetModular.getEditorComponent().requestFocusInWindow();
            }else{
                budgetModularForm.btnCancel.requestFocusInWindow();
            }
        }
     }
//    private void setRequestFocusInThread(final java.awt.Component component){
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                component.requestFocusInWindow();
//            }
//        });
//    }
    private void performCancelAction() throws CoeusException{
        budgetModularEditor.stopCellEditing();
        if(modified){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
            switch( option ) {
                case (CoeusOptionPane.SELECTION_YES):
                         saveFormData();
                         dlgBudgetModular.dispose();
                    break;
                case(CoeusOptionPane.SELECTION_NO):
                    dlgBudgetModular.dispose();
                    budgetModularForm=null;
                    break;
                default:
                    break;
            }
        }else{
            dlgBudgetModular.dispose();
            budgetModularForm=null;
        }
    }
    private void performOkAction() throws CoeusException{
        budgetModularEditor.stopCellEditing();
        saveFormData();
        dlgBudgetModular.dispose();
        
    }
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source= e. getSource();
        try{
            if(source.equals(budgetModularForm.btnOk)){
                performOkAction();
            }else if(source.equals(budgetModularForm.btnCancel)){
                performCancelAction();
            }
        }catch(CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
    }
    
    public void setTableKeyTraversal(){
         javax.swing.InputMap im = budgetModularForm.tblBudgetModular.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
         KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
         KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
         final Action oldTabAction = budgetModularForm.tblBudgetModular.getActionMap().get(im.get(tab));
         Action tabAction = new AbstractAction() {
             int row = 0;
             int column =0;
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                boolean selectionOut=false;
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                if((rowCount-1)==row && column==(columnCount-1)){
                    if(getFunctionType()!=TypeConstants.DISPLAY_MODE){
                        selectionOut=true;
                        budgetModularForm.btnOk.requestFocusInWindow();
                    }
                    else{
                        budgetModularForm.btnCancel.requestFocusInWindow();
                    }
                }
                
                if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column=0;
                    budgetModularForm.btnOk.requestFocusInWindow();
                    return ;
               }
                
               while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
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
             }
        };
        budgetModularForm.tblBudgetModular.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction = budgetModularForm.tblBudgetModular.getActionMap().get(im.get(shiftTab));
        Action tabShiftAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                      column -= 1;
                  if (column <= 0) {
                        column = CONS_FA_COLUMN;
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
        budgetModularForm.tblBudgetModular.getActionMap().put(im.get(shiftTab), tabShiftAction);
     }
    
    /** 
     *An inner class provides the model for the Budget Modular
     *
     */
    class BudgetModularTableModel extends javax.swing.table.AbstractTableModel{
        
        private Class colClass[] = {Integer.class, Double.class,Double.class,Double.class,Double.class};
        private String colNames[] = {"Period","DC Less Consortium F&A","Consortium F&A","Total Direct Cost"};
        private CoeusVector cvData;
        
        public boolean isCellEditable(int row, int col){
            if(getFunctionType()!=TypeConstants.DISPLAY_MODE){
                if(col==0||col==3){
                    return false;
                }else{
                    return true;
                }
            }else{
                return false;
            }
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
            this.cvData=cvData;
            
        }
        public int getRowCount() {
           int vecSize=(cvData==null)?0:cvData.size();
             return vecSize;
         }
        
        public Object getValueAt(int row, int col) {
           BudgetModularBean budgetModularBean = (BudgetModularBean)cvData.get(row);
           double totalDirectCost=budgetModularBean.getDirectCostFA()
                                        +budgetModularBean.getConsortiumFNA();
           if(budgetModularBean!=null&&cvData!=null&&cvData.size()>0){
               switch(col){
                   case PERIOD_COLUMN:
                       return new Integer(budgetModularBean.getBudgetPeriod());
                   case DCLESS_FA_COLUMN:
                       return new Double(budgetModularBean.getDirectCostFA());
                   case CONS_FA_COLUMN:
                       return new Double(budgetModularBean.getConsortiumFNA());
                   case TOTAL_COST_COLUMN:
                       return new Double(totalDirectCost);
               }
           }
            return EMPTY_STRING;
        }
        public void setValueAt(Object value,int rowIndex,int colIndex){
            if(cvData==null||cvData.size()==0){
                return;
            }
            BudgetModularBean budgetModularBean=(BudgetModularBean)cvData.get(rowIndex);
            switch(colIndex){
                case DCLESS_FA_COLUMN:
                    double dclessAmt=0.00;
                    dclessAmt=new Double(value.toString()).doubleValue();
                    double beanDclessAmt=budgetModularBean.getDirectCostFA();
                    if(beanDclessAmt!=dclessAmt){
                        modified=true;
                        budgetModularBean.setDirectCostFA(dclessAmt);
//                        if(budgetModularBean.getAcType()!=null&&
//                            budgetModularBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
//                               
//                        }else{
//                            budgetModularBean.setAcType(TypeConstants.UPDATE_RECORD);
//                        }
                    }
                    if(modified){
                        budgetModularTableModel.fireTableDataChanged();
                        budgetModularTotalTableModel.fireTableDataChanged();
                    }
                    break;
                case CONS_FA_COLUMN:
                    double consFaAmt=0.00;
                    consFaAmt=new Double(value.toString()).doubleValue();
                    double beanConsFaAmt=budgetModularBean.getConsortiumFNA();
                    if(beanConsFaAmt!=consFaAmt){
                        modified=true;
                        budgetModularBean.setConsortiumFNA(consFaAmt);
//                        if(budgetModularBean.getAcType()!=null&&
//                            budgetModularBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
//                               
//                        }else{
//                            budgetModularBean.setAcType(TypeConstants.UPDATE_RECORD);
//                        }
                    }
                    if(modified){
                        budgetModularTableModel.fireTableDataChanged();
                        budgetModularTotalTableModel.fireTableDataChanged();
                    }
                    break;
            }
        }
     }
    /*** 
     * Table editor for Budget Modular
     */
     class BudgetModularEditor extends DefaultCellEditor {
         private int column;
         private CoeusLabel lblBudgetModular;
         private DollarCurrencyTextField txtDCLessFA;
         private DollarCurrencyTextField txtConsFA;
         private DollarCurrencyTextField txtTotalDirectCost;
         public BudgetModularEditor() {
            super(new JComboBox());
            lblBudgetModular=new CoeusLabel();
            lblBudgetModular.setOpaque(false);
            txtDCLessFA = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            txtConsFA = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            txtTotalDirectCost = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
        }
       public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch (column) {
                case PERIOD_COLUMN:
                    if(value == null) value = EMPTY_STRING;
                    lblBudgetModular.setText(value.toString());
                    return lblBudgetModular;
                case DCLESS_FA_COLUMN:
                    if(value== null){
                        txtDCLessFA.setValue(0.00);
                    }else{
                        txtDCLessFA.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtDCLessFA;
                case CONS_FA_COLUMN:
                    if(value== null){
                        txtConsFA.setValue(0.00);
                    }else{
                        txtConsFA.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtConsFA;
                case TOTAL_COST_COLUMN:
                    txtTotalDirectCost.setText(value.toString());
                    return txtTotalDirectCost;
            }
            return lblBudgetModular;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case PERIOD_COLUMN:
                    return lblBudgetModular.getText();
                case DCLESS_FA_COLUMN:
                    return txtDCLessFA.getValue();
                case CONS_FA_COLUMN:
                    return txtConsFA.getValue();
                case TOTAL_COST_COLUMN:
                    return txtTotalDirectCost.getValue();
            }
            return lblBudgetModular;
        }
      }
     /** 
      * Table Renderer for Budget Modular table
      */
     class BudgetModularTableCellRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        private DollarCurrencyTextField txtDollar;
        public BudgetModularTableCellRenderer(){
            lblComponent=new CoeusLabel();
            lblComponent.setOpaque(true);
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            txtDollar =  new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                    boolean hasFocus, int row, int col){
           switch(col){
              case PERIOD_COLUMN:
                  lblComponent.setHorizontalAlignment(CENTER);  
                  if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value=(value==null?EMPTY_STRING:value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                case DCLESS_FA_COLUMN:
                   value=(value==null?"0.00":value);
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
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;
                case CONS_FA_COLUMN:
                    value=(value==null?"0.00":value);
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
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;                  
                    
                case TOTAL_COST_COLUMN:
                    value=(value==null?"0.00":value);
                    lblComponent.setHorizontalAlignment(RIGHT);
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
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
    class BudgetModularTotalTableModel extends javax.swing.table.AbstractTableModel{
        
        private Class colClass[] = {String.class,Double.class,Double.class,Double.class,Double.class};
        private String colNames[] = {"Total",EMPTY_STRING,EMPTY_STRING,EMPTY_STRING};
        private final String strTotal="Total";
        
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
            
        }
        public Object getValueAt(int row, int col) {
               switch(col){
                   case PERIOD_COLUMN:
                       return strTotal;
                   case DCLESS_FA_COLUMN:
                       return new Double(cvModularData.sum("directCostFA"));
                   case CONS_FA_COLUMN:
                       return new Double(cvModularData.sum("consortiumFNA"));
                   case TOTAL_COST_COLUMN:
                       double cost = cvModularData.sum("directCostFA")+cvModularData.sum("consortiumFNA");
                       return new Double(cost);
               }
            return EMPTY_STRING;
        }
      }
    /** 
      * Table Renderer for Budget Modular Total table
      */
     class ModularTotalTableCellRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        private DollarCurrencyTextField txtDollar;
        public ModularTotalTableCellRenderer(){
            lblComponent=new CoeusLabel();
            lblComponent.setOpaque(true);
            txtDollar =  new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                    boolean hasFocus, int row, int col){
           switch(col){
              case PERIOD_COLUMN:
                  lblComponent.setHorizontalAlignment(CENTER);
                  lblComponent.setFont(CoeusFontFactory.getLabelFont());
                  lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                  lblComponent.setForeground(java.awt.Color.black);
                  value=(value==null?EMPTY_STRING:value);
                  lblComponent.setText(value.toString().trim());
                  return lblComponent;
                case DCLESS_FA_COLUMN:
                    value=(value==null?"0.00":value);
                    lblComponent.setHorizontalAlignment(RIGHT);
                    lblComponent.setFont(CoeusFontFactory.getNormalFont());
                    lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    lblComponent.setForeground(java.awt.Color.black);
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;
                case CONS_FA_COLUMN:
                    value=(value==null?"0.00":value);
                    lblComponent.setHorizontalAlignment(RIGHT);
                    lblComponent.setFont(CoeusFontFactory.getNormalFont());
                    lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    lblComponent.setForeground(java.awt.Color.black);
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;                  
                    
                case TOTAL_COST_COLUMN:
                    value=(value==null?"0.00":value);
                    lblComponent.setHorizontalAlignment(RIGHT);
                    lblComponent.setFont(CoeusFontFactory.getLabelFont());
                    lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    lblComponent.setForeground(java.awt.Color.black);
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
                    lblComponent.setText(txtDollar.getText());
                    return lblComponent;        
            }
            return lblComponent;
        }
      }
    
}
