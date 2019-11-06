/*
 * AwardContactsForm.java
 *
 * Created on April 26, 2004, 11:25 AM
 */

package edu.mit.coeus.award.gui;
import javax.swing.*;
import edu.mit.coeus.gui.*;


/**
 *
 * @author  ajaygm
 */
public class AwardContactsForm extends javax.swing.JComponent {
    
    /** Creates new form AwardContactsForm */
    public AwardContactsForm() {
        initComponents ();
    }
    
     public static void main(String s[]){
        JFrame frame = new JFrame("Proposal IP Review");
        AwardContactsForm awardContactsForm = new AwardContactsForm();
        frame.getContentPane().add(awardContactsForm);
        frame.setSize(1100, 620);
        frame.show();
     }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblContactList = new javax.swing.JLabel();
        scrPnContactList = new javax.swing.JScrollPane();
        tblContactList = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnSync = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnContactDetails = new javax.swing.JScrollPane();
        awardContactDetailsForm1 = new edu.mit.coeus.award.gui.AwardContactDetailsForm();
        lblContactDetails = new javax.swing.JLabel();
        btnAddSync = new javax.swing.JButton();
        btnModifySync = new javax.swing.JButton();
        btnDelSync = new javax.swing.JButton();
        pnlUpdateDetails = new javax.swing.JPanel();
        lblLastUpdate = new javax.swing.JLabel();
        txtLastUpdate = new javax.swing.JTextField();
        lblUpdateUser = new javax.swing.JLabel();
        txtUpdateUser = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        lblContactList.setFont(CoeusFontFactory.getLabelFont());
        lblContactList.setText("Contact List: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblContactList, gridBagConstraints);

        scrPnContactList.setMaximumSize(new java.awt.Dimension(770, 400));
        scrPnContactList.setMinimumSize(new java.awt.Dimension(200, 100));
        scrPnContactList.setPreferredSize(new java.awt.Dimension(600, 125));

        tblContactList.setFont(CoeusFontFactory.getNormalFont());
        tblContactList.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnContactList.setViewportView(tblContactList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 8, 0);
        add(scrPnContactList, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(120, 25));
        btnAdd.setMinimumSize(new java.awt.Dimension(70, 25));
        btnAdd.setPreferredSize(new java.awt.Dimension(120, 25));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnAdd, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        btnModify.setMaximumSize(new java.awt.Dimension(120, 25));
        btnModify.setMinimumSize(new java.awt.Dimension(70, 25));
        btnModify.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnModify, gridBagConstraints);

        btnSync.setFont(CoeusFontFactory.getLabelFont());
        btnSync.setMnemonic('S');
        btnSync.setText("Sync to Templ");
        btnSync.setToolTipText("Sync to Template");
        btnSync.setMaximumSize(new java.awt.Dimension(120, 25));
        btnSync.setMinimumSize(new java.awt.Dimension(70, 25));
        btnSync.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(btnSync, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(120, 25));
        btnDelete.setMinimumSize(new java.awt.Dimension(70, 25));
        btnDelete.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnDelete, gridBagConstraints);

        scrPnContactDetails.setMinimumSize(new java.awt.Dimension(200, 160));
        scrPnContactDetails.setPreferredSize(new java.awt.Dimension(600, 160));
        scrPnContactDetails.setViewportView(awardContactDetailsForm1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 6, 0);
        add(scrPnContactDetails, gridBagConstraints);

        lblContactDetails.setFont(CoeusFontFactory.getLabelFont());
        lblContactDetails.setText("Contact Details: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblContactDetails, gridBagConstraints);

        btnAddSync.setFont(CoeusFontFactory.getLabelFont());
        btnAddSync.setMnemonic('Y');
        btnAddSync.setText("Add & Sync");
        btnAddSync.setMaximumSize(new java.awt.Dimension(120, 25));
        btnAddSync.setMinimumSize(new java.awt.Dimension(70, 25));
        btnAddSync.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnAddSync, gridBagConstraints);

        btnModifySync.setFont(CoeusFontFactory.getLabelFont());
        btnModifySync.setMnemonic('N');
        btnModifySync.setText("Modify & Sync");
        btnModifySync.setMaximumSize(new java.awt.Dimension(120, 25));
        btnModifySync.setMinimumSize(new java.awt.Dimension(70, 25));
        btnModifySync.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnModifySync, gridBagConstraints);

        btnDelSync.setFont(CoeusFontFactory.getLabelFont());
        btnDelSync.setMnemonic('C');
        btnDelSync.setText("Delete & Sync");
        btnDelSync.setMaximumSize(new java.awt.Dimension(120, 25));
        btnDelSync.setMinimumSize(new java.awt.Dimension(70, 25));
        btnDelSync.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnDelSync, gridBagConstraints);
        pnlUpdateDetails.setLayout(new javax.swing.BoxLayout(pnlUpdateDetails, javax.swing.BoxLayout.X_AXIS));

        lblLastUpdate.setFont(CoeusFontFactory.getLabelFont());
        lblLastUpdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastUpdate.setText("Last Update: ");
        lblLastUpdate.setMaximumSize(new java.awt.Dimension(97, 14));
        lblLastUpdate.setMinimumSize(new java.awt.Dimension(97, 14));
        pnlUpdateDetails.add(lblLastUpdate);

        txtLastUpdate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtLastUpdate.setEnabled(false);
        txtLastUpdate.setMaximumSize(new java.awt.Dimension(140, 2147483647));
        txtLastUpdate.setMinimumSize(new java.awt.Dimension(140, 20));
        txtLastUpdate.setPreferredSize(new java.awt.Dimension(140, 20));
        pnlUpdateDetails.add(txtLastUpdate);

        lblUpdateUser.setFont(CoeusFontFactory.getLabelFont());
        lblUpdateUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdateUser.setText("Update User: ");
        lblUpdateUser.setMaximumSize(new java.awt.Dimension(310, 14));
        lblUpdateUser.setMinimumSize(new java.awt.Dimension(300, 14));
        lblUpdateUser.setPreferredSize(new java.awt.Dimension(300, 14));
        pnlUpdateDetails.add(lblUpdateUser);

        txtUpdateUser.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtUpdateUser.setEnabled(false);
        txtUpdateUser.setMaximumSize(new java.awt.Dimension(200, 2147483647));
        txtUpdateUser.setMinimumSize(new java.awt.Dimension(200, 20));
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(200, 20));
        pnlUpdateDetails.add(txtUpdateUser);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(pnlUpdateDetails, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.award.gui.AwardContactDetailsForm awardContactDetailsForm1;
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnAddSync;
    public javax.swing.JButton btnDelSync;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnModify;
    public javax.swing.JButton btnModifySync;
    public javax.swing.JButton btnSync;
    public javax.swing.JLabel lblContactDetails;
    public javax.swing.JLabel lblContactList;
    public javax.swing.JLabel lblLastUpdate;
    public javax.swing.JLabel lblUpdateUser;
    public javax.swing.JPanel pnlUpdateDetails;
    public javax.swing.JScrollPane scrPnContactDetails;
    public javax.swing.JScrollPane scrPnContactList;
    public javax.swing.JTable tblContactList;
    public javax.swing.JTextField txtLastUpdate;
    public javax.swing.JTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables
    
}
