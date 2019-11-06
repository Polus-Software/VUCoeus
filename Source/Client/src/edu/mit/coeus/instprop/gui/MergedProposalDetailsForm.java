/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * MergedProposalDetailsForm.java
 *
 * Created on April 20, 2011, 2:52 PM
 */

package edu.mit.coeus.instprop.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  divyasusendran
 */
public class MergedProposalDetailsForm extends javax.swing.JPanel {
    
    /** Creates new form MergedProposalDetailsForm */
    public MergedProposalDetailsForm() {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        txtArMergedProposals = new javax.swing.JTextArea();
        pnlButton = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(450, 250));
        setPreferredSize(new java.awt.Dimension(450, 250));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(350, 250));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(350, 250));
        txtArMergedProposals.setRows(20);
        txtArMergedProposals.setMinimumSize(new java.awt.Dimension(350, 250));
        txtArMergedProposals.setOpaque(false);
        jScrollPane1.setViewportView(txtArMergedProposals);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(jScrollPane1, gridBagConstraints);

        pnlButton.setLayout(new java.awt.GridBagLayout());

        pnlButton.setMinimumSize(new java.awt.Dimension(80, 26));
        pnlButton.setPreferredSize(new java.awt.Dimension(80, 26));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlButton.add(btnOk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(pnlButton, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnOk;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JPanel pnlButton;
    public javax.swing.JTextArea txtArMergedProposals;
    // End of variables declaration//GEN-END:variables
    
}
