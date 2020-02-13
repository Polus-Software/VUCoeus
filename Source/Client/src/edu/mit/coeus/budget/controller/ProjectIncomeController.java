/*
 * ProjectIncomeController.java
 *
 * Created on May 13, 2005, 10:28 PM
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import edu.mit.coeus.budget.gui.ProjectDescriptionForm;
import edu.mit.coeus.budget.gui.ProjectIncomeForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//import sun.security.krb5.internal.n;

/**
 *
 * @author  tarique
 */
public class ProjectIncomeController extends Controller implements ActionListener, MouseListener {
    private ProjectIncomeForm projectIncomeForm;
    private static final String EMPTY_STRING = "";
    private static final int PERIOD_COLUMN = 0;
    private static final int INCOME_COLUMN =1;
    private static final int DESCRIPTION_COLUMN =2;
    private static final int MORE_COLUMN=3;
    private CoeusVector cvData;
    private CoeusVector cvPeriods;
    private CoeusVector cvDeletedIncome;
    CoeusVector sortVector;
    private ProjectIncomeTableModel projectIncomeTableModel;
    private ProjectIncomeSummaryTableModel projectIncomeSummaryTableModel;
    private AddProjectIncomeController addBudgetIncomeController;
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgProjectIncome;
    private ProjectIncomeBean projectIncomeBean;
    private QueryEngine queryEngine;
    private String queryKey;
    private CoeusMessageResources coeusMessageResources;
    private BudgetInfoBean budgetInfoBean;
    private ProjectIncomeDetailsEditor projectIncomeDetailsEditor;
    private ProjectIncomeDetailsRenderer projectIncomeDetailsRenderer;
    private ProjectIncomeSummaryRenderer projectIncomeSummaryRenderer;
    private ProjectIncomeDescriptionCellEditor projectIncomeDescriptionCellEditor;
    private static final String WINDOW_TITLE = "Project Income";
    private static final int WIDTH = 670;
    private static final int HEIGHT =  490;
    private static final int PERIOD = 0;
    private static final int INCOME = 1;
    private static final int DESCRIPTION=2;
    private int incomeId;
    private boolean modified=false;
    private static final String DESCRIPTION_VALIDATE="budget_project_income_exceptionCode.1169";
    private static final String AMOUNT_VALIDATE = "budget_project_income_exceptionCode.1168";
    private static final String DELETE_CONFIRM="budget_project_income_exceptionCode.1170";
    private static final String SAVE_CHANGES="budget_project_income_exceptionCode.1171";
    
    private ProjectDescriptionForm projectDescriptionForm;
    private boolean addActionisPerformed=false;
    private boolean deletePressed = false;
    /** Creates a new instance of ProjectIncomeController */
    public ProjectIncomeController(CoeusAppletMDIForm mdiForm,BudgetInfoBean budgetInfoBean) {
        
        this.mdiForm = mdiForm;
        this.budgetInfoBean = budgetInfoBean;
        queryEngine = QueryEngine.getInstance();
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        
        registerComponents();
        
        postInitComponents();
        try{
            setFormData(budgetInfoBean);
        }
        
        catch(edu.mit.coeus.exception.CoeusException e){
        }
          setTableKeyTraversal();
    }
    public void formatFields() {
        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
            projectIncomeForm.btnAdd.setEnabled(false);
            projectIncomeForm.btnDelete.setEnabled(false);
            projectIncomeForm.btnOk.setEnabled(false);
            projectIncomeForm.btnCancel.requestFocus();
        }
    }
    
    public java.awt.Component getControlledUI() {
        return projectIncomeForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        
        cvData = new CoeusVector();
        sortVector=new CoeusVector();
        cvDeletedIncome=new CoeusVector();
        projectIncomeForm = new ProjectIncomeForm();
       
        projectIncomeForm.btnOk.addActionListener(this);
        projectIncomeForm.btnCancel.addActionListener(this);
        projectIncomeForm.btnAdd.addActionListener(this);
        projectIncomeForm.btnDelete.addActionListener(this);
        projectIncomeForm.tblIncomeDetails.addMouseListener(this);
       projectIncomeForm.requestFocus();
        /** Code for focus traversal - start */
        java.awt.Component[] components = { 
        projectIncomeForm.btnOk,projectIncomeForm.btnCancel,projectIncomeForm.btnAdd,
        projectIncomeForm.btnDelete,projectIncomeForm.scrPnIncomeDetails
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        projectIncomeForm.setFocusTraversalPolicy(traversePolicy);
        projectIncomeForm.setFocusCycleRoot(true);         
        /** Code for focus traversal - end */
        projectIncomeDetailsEditor=new ProjectIncomeDetailsEditor();
        projectIncomeSummaryRenderer=new ProjectIncomeSummaryRenderer();
        projectIncomeDetailsRenderer=new ProjectIncomeDetailsRenderer();
        projectIncomeDescriptionCellEditor=new ProjectIncomeDescriptionCellEditor();
        projectIncomeTableModel = new ProjectIncomeTableModel();
        projectIncomeSummaryTableModel=new ProjectIncomeSummaryTableModel();
        projectIncomeForm.tblIncomeDetails.setModel(projectIncomeTableModel);
        projectIncomeForm.tblSummary.setModel(projectIncomeSummaryTableModel);
        setTableEditors();
        
    }
    public void actionPerformed(ActionEvent e) {
        deletePressed = true;
        Object source = e.getSource();
        if(source.equals(projectIncomeForm.btnAdd)){
            projectIncomeDetailsEditor.stopCellEditing();
           addBudgetIncomeController=new AddProjectIncomeController(mdiForm,budgetInfoBean,cvPeriods);
          addBudgetIncomeController.display();
            
            if(addBudgetIncomeController.getFlagForRemove()){
                ProjectIncomeBean projectIncomeBean = addBudgetIncomeController.getProjectIncomeBean();
                if(cvData!=null&&cvData.size()>0){
                    cvData.sort("incomeNumber",false);
                    incomeId = ((ProjectIncomeBean)cvData.get(0)).getIncomeNumber();
                    cvData.sort("incomeNumber");
                 }
                else{
                    incomeId=0;
                }
                incomeId=incomeId+1;
                projectIncomeBean.setIncomeNumber(incomeId);
                cvData.add(projectIncomeBean);
                modified=true;
                sortVector.removeAllElements();
                setValuestobean();
                projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(cvData.size()-1,cvData.size()-1);
                projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(1, 1);
                projectIncomeForm.tblIncomeDetails.scrollRectToVisible(
                    projectIncomeForm.tblIncomeDetails.getCellRect(
                    cvData.size()-1 ,1, true));
                
                projectIncomeForm.tblIncomeDetails.editCellAt(cvData.size()-1,1);
                setRequestFocusInThread(projectIncomeDetailsEditor.txtAmount);
                projectIncomeForm.btnDelete.setEnabled(true);
            }
            deletePressed = false;
       }
        else if(source.equals(projectIncomeForm.btnDelete)){
            projectIncomeDetailsEditor.stopCellEditing();
            if(cvData.size()!=0){
            int yesNo=CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_CONFIRM), 2, 3);
            if(yesNo==CoeusOptionPane.SELECTION_YES){
                performDeleteOperation();
                setValuestobean();
                if(cvData!=null&&cvData.size()>0){
                    projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(cvData.size()-1,cvData.size()-1);
                    projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(1,1);
                    projectIncomeForm.tblIncomeDetails.editCellAt(cvData.size()-1,1);
                    setRequestFocusInThread(projectIncomeDetailsEditor.txtAmount);
                }
                else
                    {
                    incomeId=0;
                    projectIncomeForm.btnAdd.requestFocusInWindow();
                }
            }
            deletePressed = false;
            } 
        }
        else if(source.equals(projectIncomeForm.btnCancel)){
            projectIncomeDetailsEditor.stopCellEditing();
            performCancelAction();
            
        }
        else if(source.equals(projectIncomeForm.btnOk)){
            projectIncomeDetailsEditor.stopCellEditing();
            try{
                if(validate()){
                    saveFormData();
                    dlgProjectIncome.dispose();
                   }
            }catch(Exception ce){
                ce.printStackTrace();
            }
        }
    }
    public void saveFormData() {
        CoeusVector cvTemp=new CoeusVector();
        if(modified){
            if(cvDeletedIncome!=null&&cvDeletedIncome.size()>0){
                cvTemp.addAll(cvDeletedIncome);
            }
            if(cvData!=null&&cvData.size()>0)
                    cvTemp.addAll(cvData);
         }
       else{
            cvTemp.addAll(cvData);
       }
         if(cvTemp!=null){
            for(int index=0;index<cvTemp.size();index++){
                ProjectIncomeBean projectIncomeBean=(ProjectIncomeBean)cvTemp.get(index);
                if(projectIncomeBean.getAcType()!=null){
                  String acType=projectIncomeBean.getAcType();
                if(acType.equals(TypeConstants.INSERT_RECORD)){
                    queryEngine.insert(queryKey,projectIncomeBean);
                }
                else if(acType.equals(TypeConstants.UPDATE_RECORD)){
                    projectIncomeBean.setAcType(TypeConstants.DELETE_RECORD);
                    try{
                    queryEngine.delete(queryKey, projectIncomeBean);
                    }
                    catch(CoeusException ce){
                        ce.printStackTrace();
                    }
                    projectIncomeBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, projectIncomeBean);
                }
                else if(acType.equals(TypeConstants.DELETE_RECORD)){
                    projectIncomeBean.setAcType(TypeConstants.DELETE_RECORD);
                    try{
                        queryEngine.delete(queryKey,projectIncomeBean);
                    }
                    catch(CoeusException ced){
                        ced.printStackTrace();
                    }
                }
              } 
            }
        }
       
    }
    private void performCancelAction(){
        projectIncomeDetailsEditor.stopCellEditing();
        if(modified){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            2);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    setSaveRequired(true);
                    try{
                        if( validate() ){
                            saveFormData();
                            dlgProjectIncome.dispose();
                            projectIncomeForm=null;
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                   dlgProjectIncome.dispose();
                   projectIncomeForm=null;
                    break;
                default:
                    break;
            }
        }else{
            dlgProjectIncome.dispose();
            projectIncomeForm=null;
        }
    }
    public void performDeleteOperation(){
        
        int rowCount=projectIncomeForm.tblIncomeDetails.getRowCount();
        int selectedRow=projectIncomeForm.tblIncomeDetails.getSelectedRow();
        
        if (selectedRow != -1&&selectedRow<=rowCount) {
            ProjectIncomeBean  projectIncomeBean=(ProjectIncomeBean)cvData.get(selectedRow);
            projectIncomeBean.setAcType(TypeConstants.DELETE_RECORD);
            cvDeletedIncome.add(projectIncomeBean);
            if(cvData!=null&&cvData.size()>0){
                cvData.remove(selectedRow);
                projectIncomeTableModel.fireTableDataChanged();
                modified = true;
            }
            
            projectIncomeSummaryTableModel.fireTableDataChanged();
            if(cvData.size()==0){
                projectIncomeForm.btnDelete.setEnabled(false);
                projectIncomeForm.btnAdd.requestFocusInWindow();
            }
            else{
                 projectIncomeForm.btnDelete.setEnabled(true);
            }
            
        }
        
    }
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        // get the data and the assign to cvData object
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        CoeusVector cvPeriodBeans = new CoeusVector();
        
        
        cvPeriodBeans = queryEngine.executeQuery(queryKey,budgetPeriodBean);
        if(cvPeriodBeans!=null && cvPeriodBeans.size()>0){
            cvPeriodBeans = cvPeriodBeans.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            cvPeriods =  getPeriods(cvPeriodBeans);
        }
        
        cvData = queryEngine.executeQuery(queryKey,ProjectIncomeBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        if(cvData != null && cvData.size() > 0){
            cvData.sort("incomeNumber",false);
            incomeId = ((ProjectIncomeBean)cvData.get(0)).getIncomeNumber();
            cvData.sort("incomeNumber");
            setValuestobean();
            projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(1, 1);
            projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(0,0);
            projectIncomeForm.btnDelete.setEnabled(true);
        }
        else{
            incomeId=0;
             projectIncomeForm.btnDelete.setEnabled(false);
        }
        
    }
    /* 
     ** Method to set focus in any component
     **/
     private void setRequestFocusInThread(final Component component){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            component.requestFocusInWindow();

        }
        });
    }
     public void setTableKeyTraversal(){
         
         javax.swing.InputMap im = projectIncomeForm.tblIncomeDetails.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
         KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
         KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
          final Action oldTabAction = projectIncomeForm.tblIncomeDetails.getActionMap().get(im.get(tab));
         Action tabAction = new AbstractAction() {
             int row = 0;
             int column =0;
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                if((rowCount-1)==row && column==(columnCount-1)){
                    if(getFunctionType()!=TypeConstants.DISPLAY_MODE)
                        projectIncomeForm.btnOk.requestFocusInWindow();
                    else
                        projectIncomeForm.btnCancel.requestFocusInWindow();
                }
                
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column=0;
                    projectIncomeForm.btnOk.requestFocusInWindow();
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
                  table.changeSelection(row, column, false, false);
                   
            }
        };
        projectIncomeForm.tblIncomeDetails.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction = projectIncomeForm.tblIncomeDetails.getActionMap().get(im.get(shiftTab));
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
                        column = 3;
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
        projectIncomeForm.tblIncomeDetails.getActionMap().put(im.get(shiftTab), tabShiftAction);
     }
    
    
    public void postInitComponents(){
        dlgProjectIncome = new CoeusDlgWindow(mdiForm);
        dlgProjectIncome.setResizable(false);
        dlgProjectIncome.setModal(true);
        dlgProjectIncome.getContentPane().add(projectIncomeForm);
        dlgProjectIncome.setTitle(WINDOW_TITLE+" : Proposal Number "+budgetInfoBean.getProposalNumber()+ ", Version "+budgetInfoBean.getVersionNumber());
        dlgProjectIncome.setFont(CoeusFontFactory.getLabelFont());
        dlgProjectIncome.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgProjectIncome.getSize();
        dlgProjectIncome.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgProjectIncome.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgProjectIncome.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        dlgProjectIncome.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                    performCancelAction();
             }
        });
        dlgProjectIncome.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    performCancelAction();
            }
        });
    }
    /** To set the default focus for the component
    */
    public void requestDefaultFocus(){
        if(getFunctionType()!= TypeConstants.DISPLAY_MODE){
            if(projectIncomeForm.tblIncomeDetails.getRowCount ()>0){
                projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(1, 1);
               projectIncomeForm.tblIncomeDetails.setRowSelectionInterval (0,0);
               projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval (0,1);
               projectIncomeForm.tblIncomeDetails.editCellAt (0,1);
               projectIncomeForm.tblIncomeDetails.getEditorComponent ().requestFocusInWindow ();
            }else{
                projectIncomeForm.btnAdd.requestFocus();
            }
        }else{
            if(projectIncomeForm.tblIncomeDetails.getRowCount()>0){
                java.awt.Component[] components = { projectIncomeForm.tblIncomeDetails,
                projectIncomeForm.btnCancel
            };
        
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            projectIncomeForm.setFocusTraversalPolicy(traversePolicy);
            projectIncomeForm.setFocusCycleRoot(true);         
            projectIncomeForm.btnCancel.requestFocus();
            }else{
                java.awt.Component[] components = { projectIncomeForm.btnCancel };
        
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            projectIncomeForm.setFocusTraversalPolicy(traversePolicy);
            projectIncomeForm.setFocusCycleRoot(true); 
            projectIncomeForm.btnCancel.requestFocus();
            }
        }
    } 
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        projectIncomeDetailsEditor.stopCellEditing();
            for(int index=0 ; index < cvData.size() ; index++){
                 ProjectIncomeBean projectIncomeBean = (ProjectIncomeBean)cvData.elementAt(index);
                if(projectIncomeBean.getAmount() == 0.00||projectIncomeBean.getAmount()==0.0||projectIncomeBean.getAmount()==.00){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(AMOUNT_VALIDATE));
                    projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(1, 1);
                    projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(index,index);
                    projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(1,1);
                    projectIncomeForm.tblIncomeDetails.scrollRectToVisible(
                    projectIncomeForm.tblIncomeDetails.getCellRect(
                    index ,1, true));
                    projectIncomeForm.tblIncomeDetails.editCellAt(index,1);
                    setRequestFocusInThread(projectIncomeDetailsEditor.txtAmount);
                    return false;
                }
                else if(projectIncomeBean.getDescription().trim().equals("")||projectIncomeBean.getDescription()==null){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DESCRIPTION_VALIDATE));
                    projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(1, 1);
                    projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(index,index);
                    projectIncomeForm.tblIncomeDetails.setColumnSelectionInterval(2,2);
                    projectIncomeForm.tblIncomeDetails.scrollRectToVisible(
                    projectIncomeForm.tblIncomeDetails.getCellRect(
                    index ,2, true));
                    projectIncomeForm.tblIncomeDetails.editCellAt(index,2);
                    setRequestFocusInThread(projectIncomeDetailsEditor.txtDescription);
                    
                    return false;
                }
            }
        return true;
    }
    
    public void display(){
        dlgProjectIncome.setVisible(true);
    }
    
    
    /*Sets the tabe editors renderers and width for each column of the table*/
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = projectIncomeForm.tblIncomeDetails.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            projectIncomeForm.tblIncomeDetails.setRowHeight(22);
            projectIncomeForm.tblIncomeDetails.setSelectionBackground(java.awt.Color.yellow);
            projectIncomeForm.tblIncomeDetails.setSelectionForeground(java.awt.Color.white);
            projectIncomeForm.tblIncomeDetails.setShowHorizontalLines(true);
            projectIncomeForm.tblIncomeDetails.setShowVerticalLines(true);
            projectIncomeForm.tblIncomeDetails.setOpaque(false);
            
            projectIncomeForm.tblIncomeDetails.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
           TableColumn columnIncomeDetails;
           int prefWidth[] = {48,120,400,20};
            for(int index = 0; index < prefWidth.length; index++) {
                columnIncomeDetails = projectIncomeForm.tblIncomeDetails.getColumnModel().getColumn(index);
                columnIncomeDetails.setPreferredWidth(prefWidth[index]);
                if(index==0){
                    columnIncomeDetails.setCellRenderer(projectIncomeDetailsRenderer);
                    columnIncomeDetails.setCellEditor(projectIncomeDetailsEditor);
                }
                else if(index==3){
                    columnIncomeDetails.setMinWidth(20);
                    columnIncomeDetails.setMaxWidth(20);
                    columnIncomeDetails.setPreferredWidth(20);
                    columnIncomeDetails.setHeaderRenderer(new EmptyHeaderRenderer());
                    columnIncomeDetails.setCellEditor(projectIncomeDetailsEditor);
                    columnIncomeDetails.setCellRenderer(projectIncomeDetailsRenderer);
                    
                }
                else{ 
                   columnIncomeDetails.setCellRenderer(projectIncomeDetailsRenderer);
                    columnIncomeDetails.setCellEditor(projectIncomeDetailsEditor);
                }
            }
            
            JTableHeader summaryTableHeader = projectIncomeForm.tblSummary.getTableHeader();
            summaryTableHeader.setReorderingAllowed(false);
            summaryTableHeader.setFont(CoeusFontFactory.getLabelFont());
            projectIncomeForm.tblSummary.setRowHeight(22);
            projectIncomeForm.tblSummary.setShowHorizontalLines(true);
            projectIncomeForm.tblSummary.setShowVerticalLines(true);
            projectIncomeForm.tblSummary.setOpaque(false);
            
            projectIncomeForm.tblSummary.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            TableColumn columnSummary;
            int prefWidthSummary[] = {45,162};
            for(int index = 0; index < prefWidthSummary.length; index++) {
                columnSummary = projectIncomeForm.tblSummary.getColumnModel().getColumn(index);
                columnSummary.setPreferredWidth(prefWidth[index]);
                columnSummary.setCellRenderer(projectIncomeSummaryRenderer);
              }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    /*Table Cell editor  for Project Income*/
    class ProjectIncomeDetailsEditor extends AbstractCellEditor implements TableCellEditor,ActionListener{
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private JTextArea txtDescription;
        private int column;
        private JButton btnDescription;
        private ImageIcon imgIcnDesc;
        public ProjectIncomeDetailsEditor() {
            txtComponent = new CoeusTextField();
            txtAmount = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,false);
            txtDescription=new JTextArea();
            txtDescription.setFont(CoeusFontFactory.getNormalFont());
            btnDescription = new JButton();
            imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            btnDescription.setIcon(imgIcnDesc);
            btnDescription.addActionListener(this);
            
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case PERIOD:
                    return txtComponent.getText();
                case INCOME:
                    return txtAmount.getValue();
                case DESCRIPTION:
                    return txtDescription.getText().trim();
            }
            return txtComponent;
        }
        public void actionPerformed(ActionEvent actionEvent) {
            if( btnDescription.equals(actionEvent.getSource())){
                this.stopCellEditing();
                editDescription();
            }
        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case PERIOD:
                    txtComponent.setDocument(new LimitedPlainDocument(4));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    txtComponent.setEditable(false);
                    return txtComponent;
                    
                case INCOME:
                    if(value == null){
                        txtAmount.setValue(0.00);
                    }else{
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtAmount;
                case DESCRIPTION:
                    if(value == null){
                        txtDescription.setText(EMPTY_STRING);
                    }else{
                        txtDescription.setText(value.toString());
                    }
                    return txtDescription;
                case MORE_COLUMN:
                    return btnDescription;
            }
            return txtComponent;
        }
        
        class CustomFocusListener extends java.awt.event.FocusAdapter {
            public void focusLost(java.awt.event.FocusEvent e) {
                stopCellEditing();
            }
            
        }
        
    }
    /*Table cell renderer for Project Income*/
    class ProjectIncomeDetailsRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private JLabel lblText,lblAmount,lblDescription;
        private JButton btnMore;
        private int selRow;
        private ImageIcon imgIcnDesc;
        private JTextArea txtDescription;          
        
        public ProjectIncomeDetailsRenderer(){
            imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            txtComponent = new CoeusTextField();
            txtComponent.setHorizontalAlignment(RIGHT);
            txtAmount =  new DollarCurrencyTextField(12,RIGHT,false);
            txtDescription=new JTextArea();
            txtDescription.setFont(CoeusFontFactory.getNormalFont());
            lblText = new JLabel();
            lblAmount = new JLabel();
            lblDescription=new JLabel();
            // button more here
            btnMore  = new JButton();
            btnMore.setIcon(imgIcnDesc);
            
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtAmount.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtDescription.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblText.setOpaque(true);
            lblAmount.setOpaque(true);
            lblDescription.setOpaque(true);
            lblText.setHorizontalAlignment(RIGHT);
            lblAmount.setHorizontalAlignment(RIGHT);
            lblDescription.setHorizontalAlignment(LEFT);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col) {
                
                case PERIOD:
                    if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                        
                        if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }else{
                            lblText.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblText.setForeground(java.awt.Color.black);
                        }
                    }
                    else if(isSelected){
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
                case INCOME:
                    if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                        
                        if(isSelected){
                            lblAmount.setBackground(java.awt.Color.YELLOW);
                            lblAmount.setForeground(java.awt.Color.black);
                        }else{
                            lblAmount.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblAmount.setForeground(java.awt.Color.black);
                        }
                        
                    }
                    else if(isSelected){
                        lblAmount.setBackground(java.awt.Color.YELLOW);
                        lblAmount.setForeground(java.awt.Color.black);
                    }else{
                        lblAmount.setBackground(java.awt.Color.white);
                        lblAmount.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtAmount.setText(EMPTY_STRING);
                        lblAmount.setText(txtAmount.getText());
                    }else{
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                        lblAmount.setText(txtAmount.getText());
                    }
                    return lblAmount;
                case DESCRIPTION:
                    if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                        
                        if(isSelected){
                            lblDescription.setBackground(java.awt.Color.YELLOW);
                            lblDescription.setForeground(java.awt.Color.black);
                        }else{
                            lblDescription.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblDescription.setForeground(java.awt.Color.black);
                        }
                    }
                    else if(isSelected){
                        lblDescription.setBackground(java.awt.Color.YELLOW);
                        lblDescription.setForeground(java.awt.Color.black);
                    }else{
                        lblDescription.setBackground(java.awt.Color.white);
                        lblDescription.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtDescription.setText(EMPTY_STRING);
                        lblDescription.setText(txtDescription.getText());
                    }else{
                        txtDescription.setText(value.toString());
                        lblDescription.setText(txtDescription.getText());
                    }
                    return lblDescription;
                    
                case MORE_COLUMN:
                    return btnMore;
            }
            return txtComponent;
        }
    }
    /*Table cell renderer for Project Income Summary table*/
    class ProjectIncomeSummaryRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private JLabel lblText,lblAmount;
        
        public ProjectIncomeSummaryRenderer(){
            txtComponent = new CoeusTextField();
            txtAmount =  new DollarCurrencyTextField();
            lblText = new JLabel();
            lblAmount = new JLabel();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtAmount.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtAmount.setEditable(false);
            txtComponent.setEditable(false);
            lblText.setOpaque(true);
            lblAmount.setOpaque(true);
            lblAmount.setHorizontalAlignment(RIGHT);
            lblText.setHorizontalAlignment(RIGHT);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col) {
                
                case PERIOD:
                    if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                           lblText.setBackground(java.awt.Color.yellow);
                           lblText.setForeground(java.awt.Color.black);
                        }else{
                           lblText.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                           lblText.setForeground(java.awt.Color.black); 
                        }
                            
                    }
                    else if(isSelected){
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
                case INCOME:
                    if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                           lblAmount.setBackground(java.awt.Color.yellow);
                           lblAmount.setForeground(java.awt.Color.black);
                        }else{
                           lblAmount.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                           lblAmount.setForeground(java.awt.Color.black); 
                        }
                    }
                    else if(isSelected){
                        lblAmount.setBackground(java.awt.Color.YELLOW);
                        lblAmount.setForeground(java.awt.Color.black);
                    }else{
                        lblAmount.setBackground(java.awt.Color.white);
                        lblAmount.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtAmount.setText(EMPTY_STRING);
                        lblAmount.setText(txtAmount.getText());
                    }else{
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                        lblAmount.setText(txtAmount.getText());
                    }
                    return lblAmount;
            }
            return txtComponent;
        }
    }
    /** An inner class provides the model for the Project Income table
     *component
     */
    public class ProjectIncomeTableModel extends AbstractTableModel{
        
        private Class colClass[] = {Integer.class, Double.class, String.class,Boolean.class};
        private String colNames[] = {"Period","Income","Description","."};
        
        public boolean isCellEditable(int row, int col){
            if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                if(col==3){
                    return true;
                }else{
                return false;
                }
            }
            else
            {
                if(col==0||col==2){
                     return false;
                }else{
                    return true;
                }
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
        public int getRowCount() {
            if(cvData.size()==0){
                return 0;
            }else{
                return cvData.size();
            }
        }
        
        public void setData(CoeusVector cvData){
            cvData = cvData;
            
        }
        
        public Object getValueAt(int row, int col) {
            ProjectIncomeBean projectIncomeBean  = (ProjectIncomeBean)cvData.get(row);
            switch(col){
                case PERIOD_COLUMN:
                    return new Integer(projectIncomeBean.getBudgetPeriod());
                case INCOME_COLUMN:
                    return new Double(projectIncomeBean.getAmount());
                case DESCRIPTION_COLUMN:
                    return new String(projectIncomeBean.getDescription().trim());
            }
            return EMPTY_STRING;
        }
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if(cvData == null) return;
            ProjectIncomeBean projectIncomeBean = (ProjectIncomeBean)cvData.get(rowIndex);
            boolean changed=false;
            switch(columnIndex){
                case INCOME_COLUMN:
                   double amount=Double.parseDouble(aValue.toString().trim());
                   double amountBean=projectIncomeBean.getAmount(); 
                   if (amount!=amountBean) {
                        projectIncomeBean.setAmount(amount);
                        projectIncomeBean.setAcType(TypeConstants.UPDATE_RECORD);
                        changed = modified=true;
                    }
                                  
                    break;
                case PERIOD_COLUMN:
                    break;
                case DESCRIPTION_COLUMN:
                    String description=aValue.toString().trim();
                    if(!description.equals(projectIncomeBean.getDescription().trim())){
                        projectIncomeBean.setDescription(description);
                        projectIncomeBean.setAcType(TypeConstants.UPDATE_RECORD);
                        changed = modified=true;
                        }
            }
            if(changed){
                setValuestobean();
            }
         }
      }
    /** An inner class provides the model for the Project Income table summary
     *component
     */
    public class ProjectIncomeSummaryTableModel extends AbstractTableModel{
        
        private Class colClass[] = {Integer.class, Double.class};
        private String colNames[] = {"Period","Income"};
        
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
            if(sortVector.size()==0){
                return 0;
            }else{
                return sortVector.size();
            }
        }
        
        public void setData(CoeusVector cvData){
            sortVector = cvData;
        }
        
        public Object getValueAt(int row, int col) {
            ProjectIncomeBean projectIncomeBean  = (ProjectIncomeBean)sortVector.get(row);
            switch(col){
                case PERIOD_COLUMN:
                    return new Integer(projectIncomeBean.getBudgetPeriod());
                case INCOME_COLUMN:
                    return new Double(projectIncomeBean.getAmount());
            }
            return EMPTY_STRING;
        }
     }
    /* Table cell editor for Description button 
     *@ author tarique
     */
    class ProjectIncomeDescriptionCellEditor extends DefaultCellEditor implements ActionListener{
        
        private JButton btnDescription;
        
        private ImageIcon imgIcnDesc;
        ProjectIncomeDescriptionCellEditor() {
            super(new JComboBox());
            imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            btnDescription = new JButton();
            btnDescription.setIcon(imgIcnDesc);
            btnDescription.setOpaque(true);
            btnDescription.addActionListener(this);
          }
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
            return btnDescription;
        }
        public void actionPerformed(ActionEvent actionEvent) {
            if( btnDescription.equals(actionEvent.getSource())){
                this.stopCellEditing();
                editDescription();
            }
        }
    }
  
    /*******
     *Method to total summary income
     */
    private void setValuestobean(){
        sortVector.removeAllElements();
        CoeusVector filterVector=new CoeusVector();
        CoeusVector cvTemp=new CoeusVector();
            double totalAmount=0.0;
            int countPeriod=0;
            if(cvData.sort("budgetPeriod",true)) {
                projectIncomeTableModel.setData(cvData);
                projectIncomeTableModel.fireTableDataChanged();
                
                cvTemp.addAll(cvData);
                while(cvTemp.size()>0){
                    int period = ((ProjectIncomeBean)cvTemp.get(0)).getBudgetPeriod();
                    Equals equalsCheck=new Equals("budgetPeriod",new Integer(period));
                    filterVector=cvTemp.filter(equalsCheck);
                    NotEquals notEqualsCheck=new NotEquals("budgetPeriod",new Integer(period));
                    cvTemp=cvTemp.filter(notEqualsCheck);
                    for(int k=0;k<filterVector.size();k++){
                        
                        totalAmount=totalAmount+ ((ProjectIncomeBean)filterVector.get(k)).getAmount();
                        
                        countPeriod=((ProjectIncomeBean)filterVector.get(k)).getBudgetPeriod();
                        
                    }
                    ProjectIncomeBean sortBean=new ProjectIncomeBean();
                    sortBean.setAmount(totalAmount);
                    sortBean.setBudgetPeriod(countPeriod);
                    sortVector.add(sortBean);
                    filterVector=null;
                    countPeriod=0;
                    totalAmount=0.0;
                }
            }
            cvTemp=null;
            projectIncomeSummaryTableModel.setData(sortVector);
            projectIncomeSummaryTableModel.fireTableDataChanged();
      }
private CoeusVector getPeriods(CoeusVector cvPeriod){
        CoeusVector cvPeriodBeans = new CoeusVector();
        for(int index = 0; index < cvPeriod.size(); index++){
            BudgetPeriodBean budgetPeriodBean =
            (BudgetPeriodBean)cvPeriod.get(index);
            ComboBoxBean cmbPeriodBean = new ComboBoxBean("",""+budgetPeriodBean.getBudgetPeriod());
            cvPeriodBeans.add(cmbPeriodBean);
        }
        return cvPeriodBeans;
    }

public void mouseClicked(java.awt.event.MouseEvent e) {
    //projectIncomeForm.tblIncomeDetails.getCellEditor().stopCellEditing();
    if(projectIncomeForm.tblIncomeDetails.getSelectedColumn() == 3){
        editDescription();
    }
}

public void mouseEntered(java.awt.event.MouseEvent e) {
}

public void mouseExited(java.awt.event.MouseEvent e) {
}

public void mousePressed(java.awt.event.MouseEvent e) {
}

public void mouseReleased(java.awt.event.MouseEvent e) {
}

 public void editDescription(){
            int row=projectIncomeForm.tblIncomeDetails.getSelectedRow();
            if(row!=-1){
            ProjectIncomeBean projectIncomeBean=(ProjectIncomeBean)
                            cvData.get(projectIncomeForm.tblIncomeDetails.getSelectedRow());
                    projectDescriptionForm=new ProjectDescriptionForm(getFunctionType());
                    if(getFunctionType()!=TypeConstants.DISPLAY_MODE){
                        projectDescriptionForm.setData(projectIncomeBean.getDescription().trim());
                        projectDescriptionForm.display();
                        if(projectDescriptionForm.modifiedDescription){
                            if(!projectIncomeBean.getDescription().trim().equals(projectDescriptionForm.txtArDescription.getText().trim())){
                                projectIncomeBean.setDescription(projectDescriptionForm.txtArDescription.getText().trim());
                                projectIncomeBean.setAcType(TypeConstants.UPDATE_RECORD);
                                 modified=true;
                                 setValuestobean();
                                 projectIncomeForm.tblIncomeDetails.scrollRectToVisible(
                                    projectIncomeForm.tblIncomeDetails.getCellRect(
                                        cvData.size()-1 ,1, true));
                                    projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(row,row);
                                    projectIncomeForm.tblIncomeDetails.editCellAt(row,3);
                                    setRequestFocusInThread(projectIncomeDetailsEditor.btnDescription);
                               }
                         }else{
                            projectIncomeForm.tblIncomeDetails.scrollRectToVisible(
                                    projectIncomeForm.tblIncomeDetails.getCellRect(
                                        cvData.size()-1 ,1, true));
                                    projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(row,row);
                                    projectIncomeForm.tblIncomeDetails.editCellAt(row,3);
                                    setRequestFocusInThread(projectIncomeDetailsEditor.btnDescription); 
                         }
                    }else{
                        projectDescriptionForm.txtArDescription.setBackground(UIManager.getColor("Panel.Background"));
                        projectDescriptionForm.txtArDescription.setEditable(false);
                        projectDescriptionForm.btnOk.setEnabled(false);
                        projectDescriptionForm.setData(projectIncomeBean.getDescription().trim());
                        projectDescriptionForm.display();
                        projectIncomeForm.tblIncomeDetails.scrollRectToVisible(
                                    projectIncomeForm.tblIncomeDetails.getCellRect(
                                        cvData.size()-1 ,1, true));
                        projectIncomeForm.tblIncomeDetails.setRowSelectionInterval(row,row);
                        projectIncomeForm.tblIncomeDetails.editCellAt(row,3);
                        setRequestFocusInThread(projectIncomeDetailsEditor.btnDescription); 
                    }
           }else{
               CoeusOptionPane.showInfoDialog("Select a row first");
          }
        }
}
