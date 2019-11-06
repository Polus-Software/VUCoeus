/*
 * AwardBudgetOptionForm.java
 *
 * Created on July 14, 2005, 4:29 PM
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author  chandrashekara
 */
public class AwardBudgetOptionForm extends javax.swing.JComponent implements ActionListener{
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgAwardBudgetOption;
    private static final int WIDTH = 240;
    private static final int HEIGHT = 100;
    private static final String TITLE  = "Select Type of Award Budget";
    private static final String BRIEF_AWARD_BUDGET_SELECTED = "BRIEF_AWARD_BUDGET_SELECTED";
    private static final String DETAIL_AWARD_BUDGET_SELECTED = "DETAIL_AWARD_BUDGET_SELECTED";
    private String selectedItem;
    
    /** Creates new form AwardBudgetOptionForm */
    public AwardBudgetOptionForm() {
        initComponents();
        registerComponents();
        postInitComponents();
    }
    
    private void registerComponents(){
        btnCancel.addActionListener(this);
        btnOk.addActionListener(this);
    }
    
    /** Display the form data
     */
    public void display(){
        dlgAwardBudgetOption.setVisible(true);
    }
    
    /** Listener for the type of action performed on the buttons
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnOk)){
            performOkAction();
        }else if(source.equals(btnCancel)){
            performCancelAction();
        }
    }
    
    private void performOkAction(){
        if(validateData()){
            if(!rdBtnMIT_BUDGET.isSelected()){
                setSelectedItem(BRIEF_AWARD_BUDGET_SELECTED);
            }else if(!rdBtnPU_BUDGET.isSelected()){
                setSelectedItem(DETAIL_AWARD_BUDGET_SELECTED);
            }
            dlgAwardBudgetOption.setVisible(false);
        }
    }
    
    private void performCancelAction(){
        dlgAwardBudgetOption.setVisible(false);
    }
    /** Validate if no options are selected
     */
    private boolean validateData(){
        boolean isValid = true;
        boolean briefSelected=  rdBtnMIT_BUDGET.isSelected();
        boolean detailedSelected = rdBtnPU_BUDGET.isSelected();
        if(!briefSelected &&!detailedSelected){
            CoeusOptionPane.showInfoDialog("Please select any one option to open the Award Budget");
            isValid = false;
            return isValid;
        }
        return isValid;
    }
    
     private void postInitComponents() {
        dlgAwardBudgetOption = new CoeusDlgWindow(mdiForm);
        dlgAwardBudgetOption.getContentPane().add(this);
        dlgAwardBudgetOption.setTitle(TITLE);
        dlgAwardBudgetOption.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardBudgetOption.setModal(true);
        dlgAwardBudgetOption.setResizable(false);
        dlgAwardBudgetOption.setSize(WIDTH,HEIGHT);
        dlgAwardBudgetOption.setLocation(CoeusDlgWindow.CENTER);
        
        dlgAwardBudgetOption.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgAwardBudgetOption.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardBudgetOption.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
         dlgAwardBudgetOption.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 setWindowFocus();
             }
         });
    }
     
     private void setWindowFocus(){
         rdBtnPU_BUDGET.setSelected(true);
       //  rdBtnPU_BUDGET.requestFocusInWindow();
     }
     
     /**
     * Getter for property selectedItem.
     * @return Value of property selectedItem.
     */
    public java.lang.String getSelectedItem() {
        return selectedItem;
    }    

    /**
     * Setter for property selectedItem.
     * @param selectedItem New value of property selectedItem.
     */
    public void setSelectedItem(java.lang.String selectedItem) {
        this.selectedItem = selectedItem;
    }    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdBtnBudgetType = new javax.swing.ButtonGroup();
        rdBtnPU_BUDGET = new javax.swing.JRadioButton();
        rdBtnMIT_BUDGET = new javax.swing.JRadioButton();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        rdBtnPU_BUDGET.setFont(CoeusFontFactory.getLabelFont());
        rdBtnPU_BUDGET.setText("Brief Award Budget");
        rdBtnBudgetType.add(rdBtnPU_BUDGET);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(rdBtnPU_BUDGET, gridBagConstraints);

        rdBtnMIT_BUDGET.setFont(CoeusFontFactory.getLabelFont());
        rdBtnMIT_BUDGET.setText("Detailed Award Budget");
        rdBtnBudgetType.add(rdBtnMIT_BUDGET);
        rdBtnMIT_BUDGET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdBtnMIT_BUDGETActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 3, 0);
        add(rdBtnMIT_BUDGET, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(65, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(65, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(65, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 3, 0);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents

    private void rdBtnMIT_BUDGETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdBtnMIT_BUDGETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdBtnMIT_BUDGETActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.ButtonGroup rdBtnBudgetType;
    public javax.swing.JRadioButton rdBtnMIT_BUDGET;
    public javax.swing.JRadioButton rdBtnPU_BUDGET;
    // End of variables declaration//GEN-END:variables
    
}
