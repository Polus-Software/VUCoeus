/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
//import edu.mit.coeus.propdev.gui.ProposalDetailAdminForm;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.budget.controller.BudgetBaseWindowController;

/** CopyBudgetForm.java
 * @author  Vyjayanthi
 * Created on September 30, 2003, 2:51 PM
 */
public class CopyBudgetForm extends javax.swing.JComponent implements ActionListener{
    CoeusDlgWindow dlgCopyBudgetForm;
    private boolean modal;
    private Frame parent;
    private int versionNumber;
    private int newVersionNumber;
    private String proposalId;
    private BudgetInfoBean budgetInfoBean;
    private ProposalDevelopmentFormBean propDevFormBean;
//    private ProposalDetailAdminForm proposalDetailAdminForm;
    private boolean okClicked = false;
    
    private static final int WIDTH = 320;
    private static final int HEIGHT = 130;
    
    /** Creates new form CopyBudgetForm 
     * @param parent takes the frame
     * @param modal true if form is modal, false otherwise
     * @param version takes the version number
     * @param budgetInfoBean takes the budget info bean
     * @param propDevFormBean takes the proposal development form bean
     */
    public CopyBudgetForm(Frame parent, boolean modal, int version, 
    BudgetInfoBean budgetInfoBean, ProposalDevelopmentFormBean propDevFormBean) {
        this.parent = parent;
        this.modal = modal;
        this.versionNumber = version;
        this.budgetInfoBean = budgetInfoBean;
        this.proposalId = budgetInfoBean.getProposalNumber();
        this.propDevFormBean = propDevFormBean;
        initComponents();
        postInitComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form. */
    public void postInitComponents(){
        dlgCopyBudgetForm = new CoeusDlgWindow(parent,modal);
        dlgCopyBudgetForm.getContentPane().add(this);
        dlgCopyBudgetForm.pack();
        dlgCopyBudgetForm.setResizable(false);
        dlgCopyBudgetForm.setTitle("Copy Budget");
        txtArTitle.setText(txtArTitle.getText() + getVersionNumber());
        dlgCopyBudgetForm.setSize(WIDTH,HEIGHT);

        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgCopyBudgetForm.getSize();
        dlgCopyBudgetForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgCopyBudgetForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    dlgCopyBudgetForm.dispose();
                }
            }
        });
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { btnOk, btnCancel};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */

    }
    
    /** Setter for property newVersionNumber.
     * @param newVersionNumber New value of property newVersionNumber.
     */
    public void setNewVersionNumber(int newVersionNumber){
        this.newVersionNumber = newVersionNumber;
    }
    
    /** Setter for property proposalDetailAdminForm.
     * @param proposalDetailAdminForm New value of property proposalDetailAdminForm.
     */
//    public void setProposalDetailAdminForm(ProposalDetailAdminForm proposalDetailAdminForm){
//        this.proposalDetailAdminForm = proposalDetailAdminForm;
//    }
    
    /** Displays the Copy Budget Form 
     * @return okClicked true if ok button is clicked, false otherwise
     */    
    public boolean display(){
        btnOk.requestFocusInWindow();
        dlgCopyBudgetForm.show();
        return okClicked;
    }

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source.equals(btnOk)){
            dlgCopyBudgetForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            dlgCopyBudgetForm.getGlassPane().setVisible(true);
            BudgetBaseWindowController budgetBaseWindowController = new BudgetBaseWindowController(
                "Modify Budget for Proposal " + proposalId + ", Version " + versionNumber,
                budgetInfoBean,TypeConstants.MODIFY_MODE, propDevFormBean);
//            budgetBaseWindowController.setProposalDetailAdminForm(proposalDetailAdminForm);
            budgetBaseWindowController.setFunctionType(TypeConstants.MODIFY_MODE);
            if( rdBtnCopyOnePeriod.isSelected() ) {
                budgetBaseWindowController.copyBudget(BudgetBaseWindowController.COPY_ONE_PERIOD_ONLY, newVersionNumber);
            }else if( rdBtnCopyAllPeriods.isSelected() ) {
                budgetBaseWindowController.copyBudget(BudgetBaseWindowController.COPY_ALL_PERIODS, newVersionNumber);
            }
            budgetBaseWindowController.display();
            dlgCopyBudgetForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            dlgCopyBudgetForm.getGlassPane().setVisible(false);
            dlgCopyBudgetForm.dispose();
            okClicked = true;
        }
        else if (source.equals(btnCancel)){
            dlgCopyBudgetForm.dispose();
        }
    }
    
    /** Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /** Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdBtnGroup = new javax.swing.ButtonGroup();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtArTitle = new javax.swing.JTextArea();
        pnlRdBtns = new javax.swing.JPanel();
        rdBtnCopyOnePeriod = new javax.swing.JRadioButton();
        rdBtnCopyAllPeriods = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setToolTipText("");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 5);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setToolTipText("");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 5);
        add(btnCancel, gridBagConstraints);

        txtArTitle.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getLabelFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setText("A new Version of the budget will be created based on version ");
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setMinimumSize(new java.awt.Dimension(200, 35));
        txtArTitle.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(txtArTitle, gridBagConstraints);

        pnlRdBtns.setLayout(new java.awt.GridBagLayout());

        pnlRdBtns.setBorder(new javax.swing.border.EtchedBorder());
        rdBtnCopyOnePeriod.setFont(CoeusFontFactory.getLabelFont());
        rdBtnCopyOnePeriod.setSelected(true);
        rdBtnCopyOnePeriod.setText("Copy period one only");
        rdBtnGroup.add(rdBtnCopyOnePeriod);
        rdBtnCopyOnePeriod.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlRdBtns.add(rdBtnCopyOnePeriod, gridBagConstraints);

        rdBtnCopyAllPeriods.setFont(CoeusFontFactory.getLabelFont());
        rdBtnCopyAllPeriods.setText("Copy all periods");
        rdBtnGroup.add(rdBtnCopyAllPeriods);
        rdBtnCopyAllPeriods.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlRdBtns.add(rdBtnCopyAllPeriods, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(pnlRdBtns, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JPanel pnlRdBtns;
    public javax.swing.JRadioButton rdBtnCopyAllPeriods;
    public javax.swing.JRadioButton rdBtnCopyOnePeriod;
    public javax.swing.ButtonGroup rdBtnGroup;
    public javax.swing.JTextArea txtArTitle;
    // End of variables declaration//GEN-END:variables
    
}
