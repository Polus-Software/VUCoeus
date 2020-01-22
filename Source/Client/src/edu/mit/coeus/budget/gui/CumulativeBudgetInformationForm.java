/*
 * CumulativeBudgetInformationForm.java
 *
 * Created on February 2, 2006, 11:05 AM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  tarique
 */
public class CumulativeBudgetInformationForm extends javax.swing.JPanel {
    
    /** Creates new form CumulativeBudgetInformationForm */
    public CumulativeBudgetInformationForm() {
        initComponents();
    }
   /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlEntirePeriodTotal = new javax.swing.JPanel();
        lblTotalDCLessConsFA = new javax.swing.JLabel();
        lblTotalConsFA = new javax.swing.JLabel();
        lblTotalDirectCosts = new javax.swing.JLabel();
        lblTotalIDC = new javax.swing.JLabel();
        lblTotalDCNIDC = new javax.swing.JLabel();
        txtTotalDCLessConsFA = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalConsFA = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalDirectCosts = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalIDC = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalDCNIDC = new edu.mit.coeus.utils.DollarCurrencyTextField();

        setLayout(new java.awt.GridBagLayout());

        pnlEntirePeriodTotal.setLayout(new java.awt.GridBagLayout());

        pnlEntirePeriodTotal.setBorder(new javax.swing.border.TitledBorder(null, "Total Costs, Entire Project Period", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        lblTotalDCLessConsFA.setFont(CoeusFontFactory.getLabelFont());
        lblTotalDCLessConsFA.setText(" Total Direct Cost less Consortium F&A for Entire Project Period: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlEntirePeriodTotal.add(lblTotalDCLessConsFA, gridBagConstraints);

        lblTotalConsFA.setFont(CoeusFontFactory.getLabelFont());
        lblTotalConsFA.setText("Total Consortium F&A for Entire Project Period: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlEntirePeriodTotal.add(lblTotalConsFA, gridBagConstraints);

        lblTotalDirectCosts.setFont(CoeusFontFactory.getLabelFont());
        lblTotalDirectCosts.setText("Total Direct Costs for Entire Project Period: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlEntirePeriodTotal.add(lblTotalDirectCosts, gridBagConstraints);

        lblTotalIDC.setFont(CoeusFontFactory.getLabelFont());
        lblTotalIDC.setText("Total Indirect Costs for Entire Project Period: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlEntirePeriodTotal.add(lblTotalIDC, gridBagConstraints);

        lblTotalDCNIDC.setFont(CoeusFontFactory.getLabelFont());
        lblTotalDCNIDC.setText("Total Direct Costs and Indirect Costs for Entire Project Period: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 3, 0);
        pnlEntirePeriodTotal.add(lblTotalDCNIDC, gridBagConstraints);

        txtTotalDCLessConsFA.setEditable(false);
        txtTotalDCLessConsFA.setMaximumSize(new java.awt.Dimension(125, 21));
        txtTotalDCLessConsFA.setMinimumSize(new java.awt.Dimension(125, 21));
        txtTotalDCLessConsFA.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        pnlEntirePeriodTotal.add(txtTotalDCLessConsFA, gridBagConstraints);

        txtTotalConsFA.setEditable(false);
        txtTotalConsFA.setMaximumSize(new java.awt.Dimension(125, 21));
        txtTotalConsFA.setMinimumSize(new java.awt.Dimension(125, 21));
        txtTotalConsFA.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        pnlEntirePeriodTotal.add(txtTotalConsFA, gridBagConstraints);

        txtTotalDirectCosts.setEditable(false);
        txtTotalDirectCosts.setMaximumSize(new java.awt.Dimension(125, 21));
        txtTotalDirectCosts.setMinimumSize(new java.awt.Dimension(125, 21));
        txtTotalDirectCosts.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        pnlEntirePeriodTotal.add(txtTotalDirectCosts, gridBagConstraints);

        txtTotalIDC.setEditable(false);
        txtTotalIDC.setMaximumSize(new java.awt.Dimension(125, 21));
        txtTotalIDC.setMinimumSize(new java.awt.Dimension(125, 21));
        txtTotalIDC.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        pnlEntirePeriodTotal.add(txtTotalIDC, gridBagConstraints);

        txtTotalDCNIDC.setEditable(false);
        txtTotalDCNIDC.setMaximumSize(new java.awt.Dimension(125, 21));
        txtTotalDCNIDC.setMinimumSize(new java.awt.Dimension(125, 21));
        txtTotalDCNIDC.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlEntirePeriodTotal.add(txtTotalDCNIDC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 3, 0);
        add(pnlEntirePeriodTotal, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblTotalConsFA;
    public javax.swing.JLabel lblTotalDCLessConsFA;
    public javax.swing.JLabel lblTotalDCNIDC;
    public javax.swing.JLabel lblTotalDirectCosts;
    public javax.swing.JLabel lblTotalIDC;
    public javax.swing.JPanel pnlEntirePeriodTotal;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalConsFA;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalDCLessConsFA;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalDCNIDC;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalDirectCosts;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalIDC;
    // End of variables declaration//GEN-END:variables
    
}