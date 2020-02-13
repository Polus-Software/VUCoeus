/*
 * AddReportFreqForm.java
 *
 * Created on November 23, 2004, 4:07 PM
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.JFrame;

/**
 *
 * @author  ajaygm
 */
public class AddReportFreqForm extends javax.swing.JPanel {
    
    /** Creates new form AddReportFreqForm */
    public AddReportFreqForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblClass = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblFrequency = new javax.swing.JLabel();
        lblClassValue = new javax.swing.JLabel();
        scrPnFrequency = new javax.swing.JScrollPane();
        tblFrequency = new javax.swing.JTable();
        cmbType = new edu.mit.coeus.utils.CoeusComboBox();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblClass.setFont(CoeusFontFactory.getLabelFont());
        lblClass.setText("Class: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblClass, gridBagConstraints);

        lblType.setFont(CoeusFontFactory.getLabelFont());
        lblType.setText("Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblType, gridBagConstraints);

        lblFrequency.setFont(CoeusFontFactory.getLabelFont());
        lblFrequency.setText("Frequency: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblFrequency, gridBagConstraints);

        lblClassValue.setText("xxx");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblClassValue, gridBagConstraints);

        tblFrequency.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnFrequency.setViewportView(tblFrequency);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 5);
        gridBagConstraints.weighty = 1.0;
        add(scrPnFrequency, gridBagConstraints);

        cmbType.setMinimumSize(new java.awt.Dimension(250, 19));
        cmbType.setPreferredSize(new java.awt.Dimension(250, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(cmbType, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 4);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 4);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public edu.mit.coeus.utils.CoeusComboBox cmbType;
    public javax.swing.JLabel lblClass;
    public javax.swing.JLabel lblClassValue;
    public javax.swing.JLabel lblFrequency;
    public javax.swing.JLabel lblType;
    public javax.swing.JScrollPane scrPnFrequency;
    public javax.swing.JTable tblFrequency;
    // End of variables declaration//GEN-END:variables
    
    
     public static void main(String args[]){
        JFrame frame = new JFrame("Add Report Frequency");
        AddReportFreqForm addReportFreqForm = new AddReportFreqForm();
        frame.getContentPane().add(addReportFreqForm);
        frame.setSize(425,300);
        frame.show();
        
    }
}
