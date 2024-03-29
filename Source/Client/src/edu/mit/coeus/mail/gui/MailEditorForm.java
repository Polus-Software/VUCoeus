/*
 * MailEditorForm.java
 *
 * Created on June 4, 2007, 12:50 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  talarianand
 */
public class MailEditorForm extends javax.swing.JPanel {
    
    /** Creates new form MailEditorForm */
    public MailEditorForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblTo = new javax.swing.JLabel();
        lblSubject = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        scrPnlTo = new javax.swing.JScrollPane();
        lstTo = new javax.swing.JList();
        txtSubject = new edu.mit.coeus.utils.CoeusTextField();
        scrPnlMessage = new javax.swing.JScrollPane();
        txtMessage = new edu.mit.coeus.utils.CoeusTextPane();
        btnToPerson = new javax.swing.JButton();
        btnToRole = new javax.swing.JButton();
        btnToDelete = new javax.swing.JButton();
        btnAddRolodex = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();
        btnCancel = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(857, 500));
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlMain.setMinimumSize(new java.awt.Dimension(770, 410));
        pnlMain.setPreferredSize(new java.awt.Dimension(770, 410));
        lblTo.setFont(CoeusFontFactory.getLabelFont());
        lblTo.setText("Send To:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlMain.add(lblTo, gridBagConstraints);

        lblSubject.setFont(CoeusFontFactory.getLabelFont());
        lblSubject.setText("Subject Line:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblSubject, gridBagConstraints);

        lblMessage.setFont(CoeusFontFactory.getLabelFont());
        lblMessage.setText("Message Body:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblMessage, gridBagConstraints);

        scrPnlTo.setMinimumSize(new java.awt.Dimension(265, 100));
        scrPnlTo.setPreferredSize(new java.awt.Dimension(560, 100));
        lstTo.setFont(CoeusFontFactory.getNormalFont());
        scrPnlTo.setViewportView(lstTo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlMain.add(scrPnlTo, gridBagConstraints);

        txtSubject.setMinimumSize(new java.awt.Dimension(560, 20));
        txtSubject.setPreferredSize(new java.awt.Dimension(560, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(txtSubject, gridBagConstraints);

        scrPnlMessage.setMinimumSize(new java.awt.Dimension(6, 270));
        scrPnlMessage.setPreferredSize(new java.awt.Dimension(6, 240));
        scrPnlMessage.setViewportView(txtMessage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(scrPnlMessage, gridBagConstraints);

        btnToPerson.setFont(CoeusFontFactory.getLabelFont());
        btnToPerson.setMnemonic('P');
        btnToPerson.setText("Add Person");
        btnToPerson.setMaximumSize(new java.awt.Dimension(110, 23));
        btnToPerson.setMinimumSize(new java.awt.Dimension(105, 23));
        btnToPerson.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
        pnlMain.add(btnToPerson, gridBagConstraints);

        btnToRole.setFont(CoeusFontFactory.getLabelFont());
        btnToRole.setMnemonic('R');
        btnToRole.setText("Add Role");
        btnToRole.setMaximumSize(new java.awt.Dimension(110, 23));
        btnToRole.setMinimumSize(new java.awt.Dimension(105, 23));
        btnToRole.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlMain.add(btnToRole, gridBagConstraints);

        btnToDelete.setFont(CoeusFontFactory.getLabelFont());
        btnToDelete.setMnemonic('D');
        btnToDelete.setText("Delete");
        btnToDelete.setMaximumSize(new java.awt.Dimension(110, 23));
        btnToDelete.setMinimumSize(new java.awt.Dimension(105, 23));
        btnToDelete.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlMain.add(btnToDelete, gridBagConstraints);

        btnAddRolodex.setFont(CoeusFontFactory.getLabelFont());
        btnAddRolodex.setMnemonic('X');
        btnAddRolodex.setText("Add Rolodex");
        btnAddRolodex.setMaximumSize(new java.awt.Dimension(110, 23));
        btnAddRolodex.setMinimumSize(new java.awt.Dimension(105, 23));
        btnAddRolodex.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlMain.add(btnAddRolodex, gridBagConstraints);

        add(pnlMain, new java.awt.GridBagConstraints());

        btnSend.setFont(CoeusFontFactory.getLabelFont());
        btnSend.setMnemonic('S');
        btnSend.setText("Send");
        btnSend.setMaximumSize(new java.awt.Dimension(105, 23));
        btnSend.setMinimumSize(new java.awt.Dimension(105, 23));
        btnSend.setPreferredSize(new java.awt.Dimension(105, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        add(btnSend, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(105, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(105, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(105, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 10, 0, 0);
        add(btnCancel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAddRolodex;
    public edu.mit.coeus.utils.CoeusButton btnCancel;
    public javax.swing.JButton btnSend;
    public javax.swing.JButton btnToDelete;
    public javax.swing.JButton btnToPerson;
    public javax.swing.JButton btnToRole;
    public javax.swing.JLabel lblMessage;
    public javax.swing.JLabel lblSubject;
    public javax.swing.JLabel lblTo;
    public javax.swing.JList lstTo;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JScrollPane scrPnlMessage;
    public javax.swing.JScrollPane scrPnlTo;
    public edu.mit.coeus.utils.CoeusTextPane txtMessage;
    public edu.mit.coeus.utils.CoeusTextField txtSubject;
    // End of variables declaration//GEN-END:variables
   
}
