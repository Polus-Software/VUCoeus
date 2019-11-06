/*
 * MultiCampusLoginForm.java
 *
 * Created on January 24, 2007, 12:22 PM
 */

package edu.mit.coeus.user.auth.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

/**
 *
 * @author  geot
 */
public class MultiCampusLoginForm extends javax.swing.JFrame {
    
    /** Creates new form MultiCampusLoginForm */
    public MultiCampusLoginForm() {
        initComponents();
        java.awt.Component[] components = {txtUserId,password,cmbCampus,btnOk, btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        lblUserId = new javax.swing.JLabel();
        lblPasswrd = new javax.swing.JLabel();
        lblCampus = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        cmbCampus = new edu.mit.coeus.utils.CoeusComboBox();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Coeus Login");
        setResizable(false);
        // JM 12-19-2014 commented out image
        //jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/coeus.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jLabel1, gridBagConstraints);

        lblUserId.setText("User Id : ");
        lblUserId.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(lblUserId, gridBagConstraints);

        lblPasswrd.setText("Password : ");
        lblPasswrd.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(lblPasswrd, gridBagConstraints);

        lblCampus.setText("Campus : ");
        lblCampus.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 6, 2);
        getContentPane().add(lblCampus, gridBagConstraints);

        txtUserId.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(txtUserId, gridBagConstraints);

        cmbCampus.setMaximumRowCount(6);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 6, 2);
        getContentPane().add(cmbCampus, gridBagConstraints);

        btnOk.setText("OK");
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        getContentPane().add(btnOk, gridBagConstraints);

        btnCancel.setText("Cancel");
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        getContentPane().add(btnCancel, gridBagConstraints);

        lblTitle.setText("Coeus multi campus login");
        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 7, 0);
        getContentPane().add(lblTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(password, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MultiCampusLoginForm().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public edu.mit.coeus.utils.CoeusComboBox cmbCampus;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCampus;
    private javax.swing.JLabel lblPasswrd;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserId;
    public javax.swing.JPasswordField password;
    public javax.swing.JTextField txtUserId;
    // End of variables declaration//GEN-END:variables
    
}
