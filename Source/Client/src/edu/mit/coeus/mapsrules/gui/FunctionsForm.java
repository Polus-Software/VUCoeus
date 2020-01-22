/*
 * FunctionsForm.java
 *
 * Created on October 17, 2005, 11:51 AM
 */

package edu.mit.coeus.mapsrules.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.JFrame;

/**
 *
 * @author  vinayks
 */
public class FunctionsForm extends javax.swing.JPanel {
    
    /** Creates new form FunctionsForm */
    public FunctionsForm() {
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

        scrpnFunctionName = new javax.swing.JScrollPane();
        tblFunctionName = new javax.swing.JTable();
        pnlFunctionDescription = new javax.swing.JPanel();
        lblFunctionDescription = new javax.swing.JLabel();
        scrpnFunctionDescription = new javax.swing.JScrollPane();
        txtArFunctionDescription = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(728, 173));
        scrpnFunctionName.setMinimumSize(new java.awt.Dimension(340, 170));
        scrpnFunctionName.setPreferredSize(new java.awt.Dimension(340, 170));
        tblFunctionName.setModel(new javax.swing.table.DefaultTableModel(
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
        scrpnFunctionName.setViewportView(tblFunctionName);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(scrpnFunctionName, gridBagConstraints);

        pnlFunctionDescription.setLayout(new java.awt.GridBagLayout());

        pnlFunctionDescription.setMinimumSize(new java.awt.Dimension(390, 170));
        pnlFunctionDescription.setPreferredSize(new java.awt.Dimension(390, 170));
        lblFunctionDescription.setFont(CoeusFontFactory.getLabelFont());
        lblFunctionDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlFunctionDescription.add(lblFunctionDescription, gridBagConstraints);

        scrpnFunctionDescription.setMinimumSize(new java.awt.Dimension(390, 165));
        scrpnFunctionDescription.setPreferredSize(new java.awt.Dimension(390, 165));
        txtArFunctionDescription.setFont(CoeusFontFactory.getNormalFont());
        txtArFunctionDescription.setLineWrap(true);
        txtArFunctionDescription.setWrapStyleWord(true);
        txtArFunctionDescription.setMinimumSize(new java.awt.Dimension(90, 100));
        txtArFunctionDescription.setPreferredSize(new java.awt.Dimension(90, 80));
        scrpnFunctionDescription.setViewportView(txtArFunctionDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlFunctionDescription.add(scrpnFunctionDescription, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        add(pnlFunctionDescription, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblFunctionDescription;
    public javax.swing.JPanel pnlFunctionDescription;
    public javax.swing.JScrollPane scrpnFunctionDescription;
    public javax.swing.JScrollPane scrpnFunctionName;
    public javax.swing.JTable tblFunctionName;
    public javax.swing.JTextArea txtArFunctionDescription;
    // End of variables declaration//GEN-END:variables
    
}