/*
 * ClassReportFreqForm.java
 *
 * Created on November 17, 2004, 3:55 PM
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.JFrame;

/**
 *
 * @author  ajaygm
 */
public class ClassReportFreqForm extends javax.swing.JComponent {
    
    /** Creates new form ClassReportFreqForm */
    public ClassReportFreqForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        cmbClass = new edu.mit.coeus.utils.CoeusComboBox();
        lblClass = new javax.swing.JLabel();
        pnlType = new javax.swing.JPanel();
        scrPnType = new javax.swing.JScrollPane();
        tblType = new javax.swing.JTable();
        btnTypeDelete = new javax.swing.JButton();
        pnlFrequency = new javax.swing.JPanel();
        scrPnFrequency = new javax.swing.JScrollPane();
        tblFrequency = new javax.swing.JTable();
        btnFreqDelete = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(cmbClass, gridBagConstraints);

        lblClass.setFont(CoeusFontFactory.getLabelFont());
        lblClass.setText("Class: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblClass, gridBagConstraints);

        pnlType.setLayout(new java.awt.GridBagLayout());

        pnlType.setBorder(new javax.swing.border.TitledBorder(null, "Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        tblType.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnType.setViewportView(tblType);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        pnlType.add(scrPnType, gridBagConstraints);

        btnTypeDelete.setFont(CoeusFontFactory.getLabelFont());
        btnTypeDelete.setMnemonic('l');
        btnTypeDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlType.add(btnTypeDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pnlType, gridBagConstraints);

        pnlFrequency.setLayout(new java.awt.GridBagLayout());

        pnlFrequency.setBorder(new javax.swing.border.TitledBorder(null, "Frequency", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlFrequency.add(scrPnFrequency, gridBagConstraints);

        btnFreqDelete.setFont(CoeusFontFactory.getLabelFont());
        btnFreqDelete.setMnemonic('D');
        btnFreqDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlFrequency.add(btnFreqDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(pnlFrequency, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 3);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 3);
        add(btnCancel, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 3);
        add(btnAdd, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnFreqDelete;
    public javax.swing.JButton btnOK;
    public javax.swing.JButton btnTypeDelete;
    public edu.mit.coeus.utils.CoeusComboBox cmbClass;
    public javax.swing.JLabel lblClass;
    public javax.swing.JPanel pnlFrequency;
    public javax.swing.JPanel pnlType;
    public javax.swing.JScrollPane scrPnFrequency;
    public javax.swing.JScrollPane scrPnType;
    public javax.swing.JTable tblFrequency;
    public javax.swing.JTable tblType;
    // End of variables declaration//GEN-END:variables
    
    
    public static void main(String args[]){
        JFrame frame = new JFrame("Coeus");
        ClassReportFreqForm classReportFreqForm = new ClassReportFreqForm();
        frame.getContentPane().add(classReportFreqForm);
        frame.setSize(625,380);
        frame.show();
        
    }
}


