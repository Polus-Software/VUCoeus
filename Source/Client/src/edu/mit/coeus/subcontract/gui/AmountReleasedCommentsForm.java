/*
 * AmountReleasedCommentsForm.java
 *
 * Created on July 9, 2007, 11:33 PM
 */

package edu.mit.coeus.subcontract.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  noorula
 */
public class AmountReleasedCommentsForm extends javax.swing.JPanel {
    
    /** Creates new form AmountReleasedCommentsForm */
    public AmountReleasedCommentsForm() {
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

        lblSubcontractNumber = new javax.swing.JLabel();
        lblInvoiceNumber = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        scrlComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtSubcontractNumber = new edu.mit.coeus.utils.CoeusTextField();
        txtInvoiceNumber = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        lblSubcontractNumber.setFont(CoeusFontFactory.getLabelFont());
        lblSubcontractNumber.setText("Subcontract Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(lblSubcontractNumber, gridBagConstraints);

        lblInvoiceNumber.setFont(CoeusFontFactory.getLabelFont());
        lblInvoiceNumber.setText("Invoice Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblInvoiceNumber, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblComments, gridBagConstraints);

        scrlComments.setMinimumSize(new java.awt.Dimension(275, 150));
        scrlComments.setPreferredSize(new java.awt.Dimension(275, 150));
        txtArComments.setColumns(20);
        txtArComments.setRows(5);
        scrlComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(scrlComments, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("Approve");
        btnOk.setMaximumSize(new java.awt.Dimension(90, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(90, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(90, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(90, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnCancel, gridBagConstraints);

        txtSubcontractNumber.setEnabled(false);
        txtSubcontractNumber.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(txtSubcontractNumber, gridBagConstraints);

        txtInvoiceNumber.setEnabled(false);
        txtInvoiceNumber.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(txtInvoiceNumber, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblComments;
    public javax.swing.JLabel lblInvoiceNumber;
    public javax.swing.JLabel lblSubcontractNumber;
    public javax.swing.JScrollPane scrlComments;
    public javax.swing.JTextArea txtArComments;
    public edu.mit.coeus.utils.CoeusTextField txtInvoiceNumber;
    public edu.mit.coeus.utils.CoeusTextField txtSubcontractNumber;
    // End of variables declaration//GEN-END:variables
    
}
