/*
 * ColumnsForm.java
 *
 * Created on October 17, 2005, 11:29 AM
 */

package edu.mit.coeus.mapsrules.gui;

/**
 *
 * @author  vinayks
 */
public class ColumnsForm extends javax.swing.JPanel {
    
    /** Creates new form ColumnsForm */
    public ColumnsForm() {
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

        scrpnColumns = new javax.swing.JScrollPane();
        tblColumns = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        scrpnColumns.setMinimumSize(new java.awt.Dimension(730, 200));
        scrpnColumns.setPreferredSize(new java.awt.Dimension(730, 200));
        tblColumns.setModel(new javax.swing.table.DefaultTableModel(
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
        scrpnColumns.setViewportView(tblColumns);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrpnColumns, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane scrpnColumns;
    public javax.swing.JTable tblColumns;
    // End of variables declaration//GEN-END:variables
    
}
