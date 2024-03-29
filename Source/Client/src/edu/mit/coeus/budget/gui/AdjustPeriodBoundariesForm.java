/*
 * AdjustPeriodBoundaries.java
 *
 * Created on November 25, 2003, 5:15 PM
 */
/** Copyright (c) Massachusetts Institute of Technology
  * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
  * All rights reserved.
  */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  chandrashekara
 */
public class AdjustPeriodBoundariesForm extends javax.swing.JComponent {
    
    /** Creates new form AdjustPeriodBoundaries */
    public AdjustPeriodBoundariesForm() {
        initComponents();
        txtBudgetEndDate.setEditable(false);
    }
  public javax.swing.JTable getTableInstance() {
        return tblBoundaries;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblBudgetStartDate = new javax.swing.JLabel();
        lblBudgetEndDate = new javax.swing.JLabel();
        txtBudgetStartDate = new javax.swing.JTextField();
        txtBudgetEndDate = new javax.swing.JTextField();
        pnlTable = new javax.swing.JPanel();
        scrPnBoundaries = new javax.swing.JScrollPane();
        tblBoundaries = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        getTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
                            getTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    }
                });
            }
        };
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnDefault = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblBudgetStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblBudgetStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBudgetStartDate.setText("Budget Start Date:");
        lblBudgetStartDate.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(lblBudgetStartDate, gridBagConstraints);

        lblBudgetEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblBudgetEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBudgetEndDate.setText("Budget End Date:");
        lblBudgetEndDate.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(lblBudgetEndDate, gridBagConstraints);

        txtBudgetStartDate.setEditable(false);
        txtBudgetStartDate.setFont(CoeusFontFactory.getNormalFont());
        txtBudgetStartDate.setToolTipText("");
        txtBudgetStartDate.setMinimumSize(new java.awt.Dimension(90, 20));
        txtBudgetStartDate.setPreferredSize(new java.awt.Dimension(90, 20));
        txtBudgetStartDate.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(txtBudgetStartDate, gridBagConstraints);

        txtBudgetEndDate.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtBudgetEndDate.setFont(CoeusFontFactory.getNormalFont());
        txtBudgetEndDate.setMinimumSize(new java.awt.Dimension(90, 20));
        txtBudgetEndDate.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(txtBudgetEndDate, gridBagConstraints);

        pnlTable.setLayout(new java.awt.GridBagLayout());

        pnlTable.setMinimumSize(new java.awt.Dimension(280, 240));
        pnlTable.setPreferredSize(new java.awt.Dimension(285, 240));
        scrPnBoundaries.setBorder(new javax.swing.border.EtchedBorder());
        scrPnBoundaries.setMinimumSize(new java.awt.Dimension(280, 240));
        scrPnBoundaries.setPreferredSize(new java.awt.Dimension(285, 240));
        tblBoundaries.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblBoundaries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBoundariesMouseClicked(evt);
            }
        });

        scrPnBoundaries.setViewportView(tblBoundaries);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlTable.add(scrPnBoundaries, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlTable, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnCancel, gridBagConstraints);

        btnDefault.setFont(CoeusFontFactory.getLabelFont());
        btnDefault.setMnemonic('e');
        btnDefault.setText("Default");
        btnDefault.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnDefault, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnAdd, gridBagConstraints);

        btnInsert.setFont(CoeusFontFactory.getLabelFont());
        btnInsert.setMnemonic('I');
        btnInsert.setText("Insert");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnInsert, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnDelete, gridBagConstraints);

    }//GEN-END:initComponents

    private void tblBoundariesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBoundariesMouseClicked
        // Add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        getTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
                            getTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    }
                });
    }//GEN-LAST:event_tblBoundariesMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDefault;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnInsert;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblBudgetEndDate;
    public javax.swing.JLabel lblBudgetStartDate;
    public javax.swing.JPanel pnlTable;
    public javax.swing.JScrollPane scrPnBoundaries;
    public javax.swing.JTable tblBoundaries;
    public javax.swing.JTextField txtBudgetEndDate;
    public javax.swing.JTextField txtBudgetStartDate;
    // End of variables declaration//GEN-END:variables
    
}
