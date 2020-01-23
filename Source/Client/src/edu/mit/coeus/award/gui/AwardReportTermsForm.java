/*
 * AwardReportTerms.java
 *
 * Created on April 29, 2004, 6:13 PM
 */

package edu.mit.coeus.award.gui;

import javax.swing.*;

import edu.mit.coeus.gui.*;

/**
 *
 * @author  ajaygm
 */
public class AwardReportTermsForm extends javax.swing.JComponent {
    
    private CoeusMessageResources coeusmessageResources;
    /** Creates new form AwardReportTerms */
    public AwardReportTermsForm() {
        coeusmessageResources = CoeusMessageResources.getInstance();
        initComponents ();
    }
    
    
    public static void main(String s[])
     {
        JFrame frame = new JFrame("ProposalDetails");
        AwardReportTermsForm awardReportTerms = new AwardReportTermsForm();
        frame.getContentPane().add(awardReportTerms);
        frame.setSize(610, 375);
        frame.show();
     }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblClass = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblClassValue = new javax.swing.JLabel();
        lblRecipients = new javax.swing.JLabel();
        cmbType = new edu.mit.coeus.utils.CoeusComboBox();
        pnlFrequency = new javax.swing.JPanel();
        lblFrequency = new javax.swing.JLabel();
        lblOSPDistibution = new javax.swing.JLabel();
        lblDueDate = new javax.swing.JLabel();
        lblFrequencyBase = new javax.swing.JLabel();
        txtDueDate = new edu.mit.coeus.utils.CoeusTextField();
        cmbFrequency = new edu.mit.coeus.utils.CoeusComboBox();
        cmbFrequencyBase = new edu.mit.coeus.utils.CoeusComboBox();
        cmbOSPDistribution = new edu.mit.coeus.utils.CoeusComboBox();
        scrPnRecipients = new javax.swing.JScrollPane();
        tblRecipients = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        tblRecipients.dispatchEvent(new java.awt.event.KeyEvent(
                            tblRecipients,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                }
            });
        }
    };
    btnOK = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();
    btnAdd = new javax.swing.JButton();
    btnDelete = new javax.swing.JButton();
    awardHeaderForm = new edu.mit.coeus.award.gui.AwardHeaderForm();
    pnlSync = new javax.swing.JPanel();
    chkSync = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    lblClass.setFont(CoeusFontFactory.getLabelFont());
    lblClass.setText("Class: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
    add(lblClass, gridBagConstraints);

    lblType.setFont(CoeusFontFactory.getLabelFont());
    lblType.setText("Type: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    add(lblType, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
    add(lblClassValue, gridBagConstraints);

    lblRecipients.setFont(CoeusFontFactory.getLabelFont());
    lblRecipients.setText("Recipients:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    add(lblRecipients, gridBagConstraints);

    cmbType.setMinimumSize(new java.awt.Dimension(320, 19));
    cmbType.setPreferredSize(new java.awt.Dimension(320, 19));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    add(cmbType, gridBagConstraints);

    pnlFrequency.setLayout(new java.awt.GridBagLayout());

    pnlFrequency.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    lblFrequency.setFont(CoeusFontFactory.getLabelFont());
    lblFrequency.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblFrequency.setText("Frequency: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 0);
    pnlFrequency.add(lblFrequency, gridBagConstraints);

    lblOSPDistibution.setFont(CoeusFontFactory.getLabelFont());
    lblOSPDistibution.setText("OSP Distribution: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
    pnlFrequency.add(lblOSPDistibution, gridBagConstraints);

    lblDueDate.setFont(CoeusFontFactory.getLabelFont());
    lblDueDate.setText("Due Date: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
    pnlFrequency.add(lblDueDate, gridBagConstraints);

    lblFrequencyBase.setFont(CoeusFontFactory.getLabelFont());
    lblFrequencyBase.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblFrequencyBase.setText("Frequency Base: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 0);
    pnlFrequency.add(lblFrequencyBase, gridBagConstraints);

    txtDueDate.setMinimumSize(new java.awt.Dimension(150, 21));
    txtDueDate.setPreferredSize(new java.awt.Dimension(150, 21));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 4);
    pnlFrequency.add(txtDueDate, gridBagConstraints);

    cmbFrequency.setMinimumSize(new java.awt.Dimension(150, 19));
    cmbFrequency.setPreferredSize(new java.awt.Dimension(150, 19));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    pnlFrequency.add(cmbFrequency, gridBagConstraints);

    cmbFrequencyBase.setMinimumSize(new java.awt.Dimension(150, 19));
    cmbFrequencyBase.setPreferredSize(new java.awt.Dimension(150, 19));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
    pnlFrequency.add(cmbFrequencyBase, gridBagConstraints);

    cmbOSPDistribution.setMinimumSize(new java.awt.Dimension(150, 19));
    cmbOSPDistribution.setPreferredSize(new java.awt.Dimension(150, 19));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
    pnlFrequency.add(cmbOSPDistribution, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 10;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
    add(pnlFrequency, gridBagConstraints);

    scrPnRecipients.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    tblRecipients.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {

        }
    ));
    tblRecipients.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tblRecipientsMouseClicked(evt);
        }
    });

    scrPnRecipients.setViewportView(tblRecipients);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 10;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    add(scrPnRecipients, gridBagConstraints);

    btnOK.setFont(CoeusFontFactory.getLabelFont());
    btnOK.setMnemonic('O');
    btnOK.setText("OK");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 0);
    add(btnOK, gridBagConstraints);

    btnCancel.setFont(CoeusFontFactory.getLabelFont());
    btnCancel.setMnemonic('C');
    btnCancel.setText("Cancel");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
    add(btnCancel, gridBagConstraints);

    btnAdd.setFont(CoeusFontFactory.getLabelFont());
    btnAdd.setMnemonic('A');
    btnAdd.setText("Add");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
    add(btnAdd, gridBagConstraints);

    btnDelete.setFont(CoeusFontFactory.getLabelFont());
    btnDelete.setMnemonic('D');
    btnDelete.setText("Delete");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 0);
    add(btnDelete, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = 11;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    add(awardHeaderForm, gridBagConstraints);

    pnlSync.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

    pnlSync.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sync To Children", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
    chkSync.setFont(CoeusFontFactory.getLabelFont());
    chkSync.setText(coeusmessageResources.parseLabelKey("awardSyncDetails.3054"));
    chkSync.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    chkSync.setMargin(new java.awt.Insets(0, 0, 0, 0));
    pnlSync.add(chkSync);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(pnlSync, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void tblRecipientsMouseClicked (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRecipientsMouseClicked
        // TODO add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    tblRecipients.dispatchEvent(new java.awt.event.KeyEvent(
                    tblRecipients,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                    java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    
                }
            });
    }//GEN-LAST:event_tblRecipientsMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.award.gui.AwardHeaderForm awardHeaderForm;
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnOK;
    public javax.swing.JCheckBox chkSync;
    public edu.mit.coeus.utils.CoeusComboBox cmbFrequency;
    public edu.mit.coeus.utils.CoeusComboBox cmbFrequencyBase;
    public edu.mit.coeus.utils.CoeusComboBox cmbOSPDistribution;
    public edu.mit.coeus.utils.CoeusComboBox cmbType;
    public javax.swing.JLabel lblClass;
    public javax.swing.JLabel lblClassValue;
    public javax.swing.JLabel lblDueDate;
    public javax.swing.JLabel lblFrequency;
    public javax.swing.JLabel lblFrequencyBase;
    public javax.swing.JLabel lblOSPDistibution;
    public javax.swing.JLabel lblRecipients;
    public javax.swing.JLabel lblType;
    public javax.swing.JPanel pnlFrequency;
    public javax.swing.JPanel pnlSync;
    public javax.swing.JScrollPane scrPnRecipients;
    public javax.swing.JTable tblRecipients;
    public edu.mit.coeus.utils.CoeusTextField txtDueDate;
    // End of variables declaration//GEN-END:variables
    
}
