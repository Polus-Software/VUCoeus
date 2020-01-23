/**
 * @(#)InstituteRatesForm.java 17/08/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/*
 * InstituteRatesForm.java
 *
 * Created on August 17, 2004, 10:23 AM
 */

package edu.mit.coeus.rates.gui;

import javax.swing.*;
import edu.mit.coeus.gui.*;



/**
 *
 * @author  surekhan
 */
public class InstituteRatesForm extends javax.swing.JComponent {
    
    /** Creates new form InstituteRatesForm */
    public InstituteRatesForm() {
        initComponents();
    }
    
    public static void main(String Args[]){
        JFrame frame = new JFrame("Institute Rates");
        InstituteRatesForm instituteRatesForm = new InstituteRatesForm();
        frame.getContentPane().add(instituteRatesForm);
        frame.setSize(550,400);
        frame.show();
    }
    
     public javax.swing.JTable getInstituteRateTableInstance(){
        return tblInstituteRate;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblRateClass = new javax.swing.JLabel();
        lblRateType = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInstituteRate = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        getInstituteRateTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
                            getInstituteRateTableInstance() ,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    }
                });
            }
        };
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSort = new javax.swing.JButton();
        cmbRateClass = new javax.swing.JComboBox();
        cmbRateType = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        lblRateClass.setFont(CoeusFontFactory.getLabelFont());
        lblRateClass.setText("Rate Class:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(lblRateClass, gridBagConstraints);

        lblRateType.setFont(CoeusFontFactory.getLabelFont());
        lblRateType.setText("Rate Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(lblRateType, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(520, 300));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(520, 300));
        tblInstituteRate.setModel(new javax.swing.table.DefaultTableModel(
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
        tblInstituteRate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInstituteRateMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(tblInstituteRate);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 5, 0);
        add(jScrollPane1, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont()
        );
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(53, 21));
        btnAdd.setMinimumSize(new java.awt.Dimension(73, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 0);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMinimumSize(new java.awt.Dimension(73, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnDelete, gridBagConstraints);

        btnSort.setFont(CoeusFontFactory.getLabelFont()
        );
        btnSort.setMnemonic('S');
        btnSort.setText("Sort");
        btnSort.setMinimumSize(new java.awt.Dimension(73, 26));
        btnSort.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 3, 0);
        add(btnSort, gridBagConstraints);

        cmbRateClass.setMinimumSize(new java.awt.Dimension(220, 22));
        cmbRateClass.setPreferredSize(new java.awt.Dimension(220, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(cmbRateClass, gridBagConstraints);

        cmbRateType.setMinimumSize(new java.awt.Dimension(150, 22));
        cmbRateType.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(cmbRateType, gridBagConstraints);

    }//GEN-END:initComponents

    private void tblInstituteRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstituteRateMouseClicked
        // TODO add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                tblInstituteRate.dispatchEvent(new java.awt.event.KeyEvent(
                tblInstituteRate ,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
    }//GEN-LAST:event_tblInstituteRateMouseClicked
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnSort;
    public javax.swing.JComboBox cmbRateClass;
    public javax.swing.JComboBox cmbRateType;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblRateClass;
    public javax.swing.JLabel lblRateType;
    public javax.swing.JTable tblInstituteRate;
    // End of variables declaration//GEN-END:variables
    
}
