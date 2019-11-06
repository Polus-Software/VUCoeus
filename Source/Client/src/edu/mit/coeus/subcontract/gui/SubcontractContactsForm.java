/*
 * SubcontractContactsForm.java
 *
 * Created on September 3, 2004, 2:22 PM
 */

package edu.mit.coeus.subcontract.gui;

import javax.swing.*;
import edu.mit.coeus.gui.*;

/**
 *
 * @author  surekhan
 */
public class SubcontractContactsForm extends javax.swing.JPanel {
    
    /** Creates new form SubcontractContactsForm */
    public SubcontractContactsForm() {
        initComponents();
    }
    
     public static void main(String s[]){
        JFrame frame = new JFrame( "IP Review");
        SubcontractContactsForm subcontractContactsForm = new SubcontractContactsForm();
        frame.getContentPane().add(subcontractContactsForm);
        frame.setSize(1100, 620);
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

        scrPnContactDetails = new javax.swing.JScrollPane();
        awardContactDetailsForm1 = new edu.mit.coeus.award.gui.AwardContactDetailsForm();
        scrPnContactList = new javax.swing.JScrollPane();
        tblContactList = new javax.swing.JTable();
        lblContactList = new javax.swing.JLabel();
        lblContactDetails = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(887, 543));
        scrPnContactDetails.setMinimumSize(new java.awt.Dimension(300, 23));
        scrPnContactDetails.setPreferredSize(new java.awt.Dimension(790, 200));
        awardContactDetailsForm1.setPreferredSize(new java.awt.Dimension(531, 350));
        scrPnContactDetails.setViewportView(awardContactDetailsForm1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(scrPnContactDetails, gridBagConstraints);

        scrPnContactList.setPreferredSize(new java.awt.Dimension(790, 300));
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
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(scrPnContactList, gridBagConstraints);

        lblContactList.setFont(CoeusFontFactory.getLabelFont());
        lblContactList.setText("Contact List: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(lblContactList, gridBagConstraints);

        lblContactDetails.setFont(CoeusFontFactory.getLabelFont()
        );
        lblContactDetails.setText("Contact Details: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(lblContactDetails, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont()
        );
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add Non-Employee"); // JM 3-21-2014 relabeling
        btnAdd.setPreferredSize(new java.awt.Dimension(160, 25)); // JM 3-24-2014 size; was 90, 25
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont()
        );
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setPreferredSize(new java.awt.Dimension(160, 25)); // JM 3-24-2014 size; was 90, 25
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        add(btnDelete, gridBagConstraints);

        // JM 3-21-2014 repurposing for person search
        btnModify.setFont(CoeusFontFactory.getLabelFont()
        );
        btnModify.setMnemonic('M');
        btnModify.setText("Add Employee");
        btnModify.setPreferredSize(new java.awt.Dimension(160, 25)); // JM 3-24-2014 size; was 90, 25
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        add(btnModify, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.award.gui.AwardContactDetailsForm awardContactDetailsForm1;
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnModify;
    public javax.swing.JLabel lblContactDetails;
    public javax.swing.JLabel lblContactList;
    public javax.swing.JScrollPane scrPnContactDetails;
    public javax.swing.JScrollPane scrPnContactList;
    public javax.swing.JTable tblContactList;
    // End of variables declaration//GEN-END:variables
    
}
