/*
 * NewSponsorHierarchyForm.java
 *
 * Created on November 18, 2004, 11:21 AM
 */

package edu.mit.coeus.sponsorhierarchy.gui;

import javax.swing.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
/**
 *
 * @author  surekhan
 */
public class NewSponsorHierarchyForm extends javax.swing.JPanel {
    
    /** Creates new form NewSponsorHierarchyForm */
    public NewSponsorHierarchyForm() {
        initComponents();
    }
    
    public static void main(String args[]){
        NewSponsorHierarchyForm newSponsorHierarchyForm  = new NewSponsorHierarchyForm();
        JFrame frame = new JFrame("New Sponsor Hierarchy");
        frame.getContentPane().add(newSponsorHierarchyForm);
        frame.setSize(380,95);
        frame.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblText = new javax.swing.JLabel();
        txtSponsor = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblText.setFont(CoeusFontFactory.getLabelFont());
        lblText.setText("<html>Please enter a unique name for the new <br>sponsor hierarchy</html>\n");
        lblText.setMaximumSize(new java.awt.Dimension(230, 30));
        lblText.setMinimumSize(new java.awt.Dimension(230, 30));
        lblText.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblText, gridBagConstraints);

        txtSponsor.setFont(CoeusFontFactory.getNormalFont());
        txtSponsor.setMinimumSize(new java.awt.Dimension(295, 23));
        txtSponsor.setPreferredSize(new java.awt.Dimension(295, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 3, 0);
        add(txtSponsor, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblText;
    public javax.swing.JTextField txtSponsor;
    // End of variables declaration//GEN-END:variables
    
}
