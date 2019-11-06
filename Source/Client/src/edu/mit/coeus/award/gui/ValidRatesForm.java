/*
 * ValidEBRates.java
 *
 * Created on March 23, 2004, 12:15 PM
 */

package edu.mit.coeus.award.gui; 
 import javax.swing.*;

 import edu.mit.coeus.gui.*;
/**
 *
 * @author  ajaygm
 */
 public class ValidRatesForm extends javax.swing.JComponent {
    
    /** Creates new form ValidEBRates */
     public ValidRatesForm() {
        initComponents();
    }
    
     public static void main(String s[])
     {
        JFrame frame = new JFrame("Valid EB Rates");
        ValidRatesForm validRatesForm = new ValidRatesForm();
        frame.getContentPane().add(validRatesForm);
        frame.setSize(430, 170);
        frame.show();
     }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnValidRates = new javax.swing.JScrollPane();
        tblValidRates = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setFont(CoeusFontFactory.getNormalFont());
        scrPnValidRates.setFont(CoeusFontFactory.getNormalFont());
        scrPnValidRates.setMinimumSize(new java.awt.Dimension(350, 200));
        scrPnValidRates.setPreferredSize(new java.awt.Dimension(350, 200));
        tblValidRates.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblValidRates.setFont(CoeusFontFactory.getNormalFont());
        tblValidRates.setModel(new javax.swing.table.DefaultTableModel(
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
        tblValidRates.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblValidRates.setEnabled(false);
        scrPnValidRates.setViewportView(tblValidRates);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        add(scrPnValidRates, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(btnClose, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JScrollPane scrPnValidRates;
    public javax.swing.JTable tblValidRates;
    // End of variables declaration//GEN-END:variables
    
}
