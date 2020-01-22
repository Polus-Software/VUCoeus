/*
 * BudgetModularBaseForm.java
 *
 * Created on February 2, 2006, 11:26 AM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  tarique
 */
public class BudgetModularBaseForm extends javax.swing.JComponent {
    
    /** Creates new form BudgetModularBaseForm */
    public BudgetModularBaseForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tbdPnBudgetModular = new edu.mit.coeus.utils.CoeusTabbedPane();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSync = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        tbdPnBudgetModular.setMinimumSize(new java.awt.Dimension(660, 390));
        tbdPnBudgetModular.setPreferredSize(new java.awt.Dimension(660, 390));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 3, 3);
        gridBagConstraints.weighty = 1.0;
        add(tbdPnBudgetModular, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(70, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(70, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 3, 3, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMargin(new java.awt.Insets(2, 12, 2, 12));
        btnCancel.setMaximumSize(new java.awt.Dimension(70, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(70, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        add(btnCancel, gridBagConstraints);

        btnSync.setFont(CoeusFontFactory.getLabelFont());
        btnSync.setMnemonic('Y');
        btnSync.setText("Sync");
        btnSync.setMaximumSize(new java.awt.Dimension(70, 25));
        btnSync.setMinimumSize(new java.awt.Dimension(70, 25));
        btnSync.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        add(btnSync, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnSync;
    public edu.mit.coeus.utils.CoeusTabbedPane tbdPnBudgetModular;
    // End of variables declaration//GEN-END:variables
    
}