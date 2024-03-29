/*
 * ValidRateTypesForm.java
 *
 * Created on November 17, 2004, 11:33 AM
 */

package edu.mit.coeus.admin.gui;


/**
 *
 * @author  shijiv
 */

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.JFrame;

public class ValidRateTypesForm extends javax.swing.JPanel {
    
    /** Creates new form ValidRateTypesForm */
    public ValidRateTypesForm() {
        initComponents();
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
        cmbRateClass = new edu.mit.coeus.utils.CoeusComboBox();
        cmbRateType = new edu.mit.coeus.utils.CoeusComboBox();
        lblAppliableCostElements = new javax.swing.JLabel();
        lblCostElements = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        scrpnApplicableCostElements = new javax.swing.JScrollPane();
        tblApplicationCostElements = new edu.mit.coeus.utils.table.CoeusTable();
        scrpnCostElements = new javax.swing.JScrollPane();
        tblCostElements = new edu.mit.coeus.utils.table.CoeusTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(780, 475));
        lblRateClass.setFont(CoeusFontFactory.getLabelFont());
        lblRateClass.setText("Rate Class:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 5);
        add(lblRateClass, gridBagConstraints);

        lblRateType.setFont(CoeusFontFactory.getLabelFont());
        lblRateType.setText("  Rate Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(lblRateType, gridBagConstraints);

        cmbRateClass.setFont(CoeusFontFactory.getNormalFont());
        cmbRateClass.setMinimumSize(new java.awt.Dimension(215, 21));
        cmbRateClass.setPreferredSize(new java.awt.Dimension(215, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(cmbRateClass, gridBagConstraints);

        cmbRateType.setFont(CoeusFontFactory.getNormalFont());
        cmbRateType.setMinimumSize(new java.awt.Dimension(215, 21));
        cmbRateType.setPreferredSize(new java.awt.Dimension(215, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        add(cmbRateType, gridBagConstraints);

        lblAppliableCostElements.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblAppliableCostElements.setText("Applicable Cost Elements for the selected rate class and type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(lblAppliableCostElements, gridBagConstraints);

        lblCostElements.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblCostElements.setText("Cost Elements");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        add(lblCostElements, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("<-Add");
        btnAdd.setMinimumSize(new java.awt.Dimension(95, 25));
        btnAdd.setPreferredSize(new java.awt.Dimension(95, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(btnAdd, gridBagConstraints);

        btnRemove.setFont(CoeusFontFactory.getLabelFont());
        btnRemove.setMnemonic('R');
        btnRemove.setText("Remove->");
        btnRemove.setMaximumSize(new java.awt.Dimension(95, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(95, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(95, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        add(btnRemove, gridBagConstraints);

        scrpnApplicableCostElements.setFocusCycleRoot(true);
        scrpnApplicableCostElements.setFocusable(false);
        scrpnApplicableCostElements.setMinimumSize(new java.awt.Dimension(290, 340));
        scrpnApplicableCostElements.setPreferredSize(new java.awt.Dimension(550, 600));
        tblApplicationCostElements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tblApplicationCostElements.setRowHeight(22);
        scrpnApplicableCostElements.setViewportView(tblApplicationCostElements);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 5);
        add(scrpnApplicableCostElements, gridBagConstraints);

        scrpnCostElements.setMinimumSize(new java.awt.Dimension(290, 340));
        scrpnCostElements.setPreferredSize(new java.awt.Dimension(550, 600));
        tblCostElements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tblCostElements.setRowHeight(22);
        scrpnCostElements.setViewportView(tblCostElements);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(scrpnCostElements, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMinimumSize(new java.awt.Dimension(73, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnRemove;
    public edu.mit.coeus.utils.CoeusComboBox cmbRateClass;
    public edu.mit.coeus.utils.CoeusComboBox cmbRateType;
    public javax.swing.JLabel lblAppliableCostElements;
    public javax.swing.JLabel lblCostElements;
    public javax.swing.JLabel lblRateClass;
    public javax.swing.JLabel lblRateType;
    public javax.swing.JScrollPane scrpnApplicableCostElements;
    public javax.swing.JScrollPane scrpnCostElements;
    public edu.mit.coeus.utils.table.CoeusTable tblApplicationCostElements;
    public edu.mit.coeus.utils.table.CoeusTable tblCostElements;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String args[]) {
        ValidRateTypesForm validRateTypesForm =new ValidRateTypesForm();
        JFrame frame= new JFrame();
        frame.getContentPane().add(validRateTypesForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(780, 475);
        frame.show();
    }
}
