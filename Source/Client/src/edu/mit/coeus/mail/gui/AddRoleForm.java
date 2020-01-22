/*
 * AddRoleForm.java
 *
 * Created on June 4, 2007, 2:29 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.gui;

import edu.mit.coeus.gui.CoeusFontFactory;


/**
 *
 * @author  talarianand
 */
public class AddRoleForm extends javax.swing.JPanel {

    /** Creates new form AddRoleForm */
    public AddRoleForm() {
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

        pnlMain = new javax.swing.JPanel();
        lblRole = new javax.swing.JLabel();
        lblQualifier = new javax.swing.JLabel();
        scrPnlUserList = new javax.swing.JScrollPane();
        tblUserList = new edu.mit.coeus.utils.table.CoeusTable();
        cmbRole = new edu.mit.coeus.utils.CoeusComboBox();
        cmbQualifier = new edu.mit.coeus.utils.CoeusComboBox();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMinimumSize(new java.awt.Dimension(500, 300));
        pnlMain.setPreferredSize(new java.awt.Dimension(400, 300));
        lblRole.setFont(CoeusFontFactory.getLabelFont());
        lblRole.setText("Role:");
        pnlMain.add(lblRole, new java.awt.GridBagConstraints());

        lblQualifier.setFont(CoeusFontFactory.getLabelFont());
        lblQualifier.setText("Qualifier:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        pnlMain.add(lblQualifier, gridBagConstraints);

        scrPnlUserList.setMinimumSize(new java.awt.Dimension(500, 500));
        scrPnlUserList.setPreferredSize(new java.awt.Dimension(270, 270));
        tblUserList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrPnlUserList.setViewportView(tblUserList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(scrPnlUserList, gridBagConstraints);

        cmbRole.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        pnlMain.add(cmbRole, gridBagConstraints);

        cmbQualifier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbQualifier.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        pnlMain.add(cmbQualifier, gridBagConstraints);

        add(pnlMain, new java.awt.GridBagConstraints());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 0, 0);
        add(btnCancel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public edu.mit.coeus.utils.CoeusComboBox cmbQualifier;
    public edu.mit.coeus.utils.CoeusComboBox cmbRole;
    public javax.swing.JLabel lblQualifier;
    public javax.swing.JLabel lblRole;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JScrollPane scrPnlUserList;
    public edu.mit.coeus.utils.table.CoeusTable tblUserList;
    // End of variables declaration//GEN-END:variables
    
}