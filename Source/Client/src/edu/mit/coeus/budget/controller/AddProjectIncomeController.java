/*
 * BudgetIncomeAddController.java
 *
 * Created on June 7, 2005, 6:57 PM
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import edu.mit.coeus.budget.gui.AddProjectIncomeForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author  tarique
 */
public class AddProjectIncomeController implements ActionListener{
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgAddProjectIncome;
    private BudgetInfoBean budgetInfoBean;
    private CoeusVector cvPeriods;
    private String queryKey;
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private static final String WINDOW_TITLE = "Add Project Income";
    private static final int WIDTH = 425;
    private static final int HEIGHT =  155;
    boolean checkFlagForRemovingData=false;
    private static final String AMOUNT_VALIDATE = "budget_project_income_exceptionCode.1168";
    private static final String DESCRIPTION_VALIDATE="budget_project_income_exceptionCode.1169";
    private static final String SAVE_CHANGES="budget_project_income_exceptionCode.1171";
    private AddProjectIncomeForm addProjectIncomeForm;
       ProjectIncomeBean projectIncomeBean;
    private int flagFocus=0;
    /** Creates a new instance of BudgetIncomeAddController */
    public AddProjectIncomeController(CoeusAppletMDIForm mdiForm, BudgetInfoBean budgetInfoBean ,
                                      CoeusVector cvPeriods) {
        this.mdiForm=mdiForm;
        this.budgetInfoBean = budgetInfoBean;
        this.cvPeriods = cvPeriods;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        registerComponents();
        postInitComponents();
        setFormData(cvPeriods);
    }
    public void registerComponents() {
        addProjectIncomeForm = new AddProjectIncomeForm();
        addProjectIncomeForm.dollarCurrencyTextField.setmaxMantissaDigits(10);
        addProjectIncomeForm.btnOK.addActionListener(this);
        addProjectIncomeForm.btnCancel.addActionListener(this);
        addProjectIncomeForm.txtArDescription.setDocument(new LimitedPlainDocument(2000));
        
    }
      public void postInitComponents(){
        dlgAddProjectIncome = new CoeusDlgWindow(mdiForm);
        dlgAddProjectIncome.setResizable(false);
        dlgAddProjectIncome.setModal(true);
        dlgAddProjectIncome.getContentPane().add(addProjectIncomeForm);
        dlgAddProjectIncome.setTitle(WINDOW_TITLE);
        dlgAddProjectIncome.setFont(CoeusFontFactory.getLabelFont());
        dlgAddProjectIncome.setSize(WIDTH, HEIGHT);
        dlgAddProjectIncome.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    addProjectIncomeForm.cmbPeriod.requestFocus();
                }
        });
        dlgAddProjectIncome.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAddProjectIncome.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                    performCancelAction();
                    if(flagFocus==1){
                        addProjectIncomeForm.dollarCurrencyTextField.requestFocusInWindow();
                    }else if(flagFocus==2){
                        addProjectIncomeForm.txtArDescription.requestFocusInWindow();
                    }
             }
        });
        dlgAddProjectIncome.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    performCancelAction();
                    if(flagFocus==1){
                        addProjectIncomeForm.dollarCurrencyTextField.requestFocusInWindow();
                    }else if(flagFocus==2){
                        addProjectIncomeForm.txtArDescription.requestFocusInWindow();
                    }
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddProjectIncome.getSize();
        dlgAddProjectIncome.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
    }
      
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source.equals(addProjectIncomeForm.btnOK)){
            try{
                if(validate()){
                 performUpdate();
                dlgAddProjectIncome.dispose();
                checkFlagForRemovingData=true;
                addProjectIncomeForm=null;
                
                }
            }
            catch(Exception exception){
            exception.printStackTrace();
            return;
            }
        }
        else if(source.equals(addProjectIncomeForm.btnCancel)){
                      performCancelAction();
                      if(flagFocus==1){
                        addProjectIncomeForm.dollarCurrencyTextField.requestFocusInWindow();
                    }else if(flagFocus==2){
                        addProjectIncomeForm.txtArDescription.requestFocusInWindow();
                    }
        }
               
    }
    private void performCancelAction(){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            2);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    try{
                        if( validate() ){
                            performUpdate();
                            dlgAddProjectIncome.dispose();
                            checkFlagForRemovingData=true;
                            addProjectIncomeForm=null;
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                   dlgAddProjectIncome.dispose();
                   break;
                default:
                    break;
            }
    }
    public boolean getFlagForRemove(){
        return checkFlagForRemovingData;
    }
    public boolean validate() throws CoeusUIException {
        double projectAmount=Double.parseDouble(addProjectIncomeForm.dollarCurrencyTextField.getValue());
        String projectDescription=addProjectIncomeForm.txtArDescription.getText();
        if(projectAmount==0.00||projectAmount==0.0||projectAmount==.00){
            flagFocus=1;
            addProjectIncomeForm.dollarCurrencyTextField.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(AMOUNT_VALIDATE));
            return false;
        }
        else if(projectDescription.equals("")||projectDescription==null){
            flagFocus=2;
            addProjectIncomeForm.txtArDescription.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DESCRIPTION_VALIDATE));
            return false;
        }
        return true;
    }
    private void performUpdate(){
        int periodSel = Integer.parseInt(addProjectIncomeForm.cmbPeriod.getSelectedItem().toString());
        double projectAmount=Double.parseDouble(addProjectIncomeForm.dollarCurrencyTextField.getValue());
        String projectIncomeDescription=addProjectIncomeForm.txtArDescription.getText();
        if(projectIncomeDescription==null){
            projectIncomeDescription="";
         }
        projectIncomeBean  = new ProjectIncomeBean();
        projectIncomeBean.setBudgetPeriod(periodSel);
        projectIncomeBean.setAmount(projectAmount);
        projectIncomeBean.setDescription(projectIncomeDescription);
        projectIncomeBean.setAcType(TypeConstants.INSERT_RECORD);
        projectIncomeBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        projectIncomeBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        setProjectIncomeBean(projectIncomeBean);
    }
    public ProjectIncomeBean getProjectIncomeBean(){
        return projectIncomeBean;
    }
    public void setProjectIncomeBean(ProjectIncomeBean projectIncomeBean){
        this.projectIncomeBean=projectIncomeBean;
    }
    public void setFormData(Object data) {
        if(data != null){
            cvPeriods = (CoeusVector)data;
            for(int index = 0 ; index<cvPeriods.size(); index++){
                addProjectIncomeForm.cmbPeriod.addItem(cvPeriods.get(index));
            }
        }
        
    }
    
    public void display(){
        dlgAddProjectIncome.setVisible(true);
        
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
   
    
}
