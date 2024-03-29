/*
 * AddValidRatesForm.java
 *
 * Created on August 21, 2007, 4:42 PM
 */

package edu.mit.coeus.rates.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  talarianand
 */
public class AddValidRatesForm extends javax.swing.JComponent {
    
    /** Creates new form AddValidRatesForm */
    public AddValidRatesForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblCE = new javax.swing.JLabel();
        txtCE = new edu.mit.coeus.utils.CoeusTextField();
        lblDescription = new javax.swing.JLabel();
        txtDescription = new edu.mit.coeus.utils.CoeusTextField();
        lblFiscalYear = new javax.swing.JLabel();
        txtFiscalYear = new edu.mit.coeus.utils.CoeusTextField();
        lblRate = new javax.swing.JLabel();
        txtRate = new edu.mit.coeus.utils.CoeusTextField();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtStartDate = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(500, 200));
        setPreferredSize(new java.awt.Dimension(500, 200));
        lblCE.setFont(CoeusFontFactory.getLabelFont());
        lblCE.setText("Cost Element:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblCE, gridBagConstraints);

        txtCE.setMinimumSize(new java.awt.Dimension(100, 20));
        txtCE.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(txtCE, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDescription, gridBagConstraints);

        txtDescription.setMinimumSize(new java.awt.Dimension(250, 20));
        txtDescription.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        add(txtDescription, gridBagConstraints);

        lblFiscalYear.setFont(CoeusFontFactory.getLabelFont());
        lblFiscalYear.setText("Fiscal Year:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblFiscalYear, gridBagConstraints);

        txtFiscalYear.setMinimumSize(new java.awt.Dimension(100, 20));
        txtFiscalYear.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        add(txtFiscalYear, gridBagConstraints);

        lblRate.setFont(CoeusFontFactory.getLabelFont());
        lblRate.setText("Monthly Rate:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblRate, gridBagConstraints);

        txtRate.setMinimumSize(new java.awt.Dimension(100, 20));
        txtRate.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        add(txtRate, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnOk.setMaximumSize(new java.awt.Dimension(73, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(btnCancel, gridBagConstraints);

        jLabel1.setFont(CoeusFontFactory.getLabelFont());
        jLabel1.setText("Effective Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel1, gridBagConstraints);

        txtStartDate.setMinimumSize(new java.awt.Dimension(100, 22));
        txtStartDate.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        add(txtStartDate, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel lblCE;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblFiscalYear;
    public javax.swing.JLabel lblRate;
    public edu.mit.coeus.utils.CoeusTextField txtCE;
    public edu.mit.coeus.utils.CoeusTextField txtDescription;
    public edu.mit.coeus.utils.CoeusTextField txtFiscalYear;
    public edu.mit.coeus.utils.CoeusTextField txtRate;
    public edu.mit.coeus.utils.CoeusTextField txtStartDate;
    // End of variables declaration//GEN-END:variables
    
}
