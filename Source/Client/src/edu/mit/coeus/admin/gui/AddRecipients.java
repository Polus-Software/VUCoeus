/*
 * AddRecipients.java
 *
 * Created on October 18, 2007, 10:31 AM
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 *
 * @author  talarianand
 */
public class AddRecipients extends javax.swing.JComponent {
    
    CoeusMessageResources messageResources;
    private static final String  MSGKEY_SYSTEM_GENERATED = "mailFrm_exceptionCode.2006";
    /** Creates new form AddRecipients */
    public AddRecipients() {
        messageResources = CoeusMessageResources.getInstance();
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

        rdBtnGroup = new javax.swing.ButtonGroup();
        rdBtnStatusGroup = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        lblActionName = new javax.swing.JLabel();
        lblModule = new javax.swing.JLabel();
        lblSubject = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        lblPromptUser = new javax.swing.JLabel();
        txtActionName = new edu.mit.coeus.utils.CoeusTextField();
        txtModule = new edu.mit.coeus.utils.CoeusTextField();
        txtSubject = new edu.mit.coeus.utils.CoeusTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRoles = new javax.swing.JTable();
        rdBtnPromptUserYes = new javax.swing.JRadioButton();
        rdBtnPromptUserNo = new javax.swing.JRadioButton();
        pnlMessage = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        lblStatus = new javax.swing.JLabel();
        rdStatusActive = new javax.swing.JRadioButton();
        rdStatusInActive = new javax.swing.JRadioButton();
        pnlRecipients = new javax.swing.JPanel();
        lblRecipients = new javax.swing.JLabel();
        lblSysGenerated = new javax.swing.JLabel();
        btnOk = new edu.mit.coeus.utils.CoeusButton();
        btnCancel = new edu.mit.coeus.utils.CoeusButton();
        btnAdd = new edu.mit.coeus.utils.CoeusButton();
        btnDelete = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(720, 550));
        setPreferredSize(new java.awt.Dimension(620, 550));
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlMain.setMinimumSize(new java.awt.Dimension(470, 450));
        pnlMain.setPreferredSize(new java.awt.Dimension(520, 450));
        lblActionName.setFont(CoeusFontFactory.getLabelFont());
        lblActionName.setText("Notify when:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblActionName, gridBagConstraints);

        lblModule.setFont(CoeusFontFactory.getLabelFont());
        lblModule.setText("Module:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(lblModule, gridBagConstraints);

        lblSubject.setFont(CoeusFontFactory.getLabelFont());
        lblSubject.setText("Subject Line:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblSubject, gridBagConstraints);

        lblMessage.setFont(CoeusFontFactory.getLabelFont());
        lblMessage.setText("Message Body:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblMessage, gridBagConstraints);

        lblPromptUser.setFont(CoeusFontFactory.getLabelFont());
        lblPromptUser.setText("Prompt User before sending mail:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblPromptUser, gridBagConstraints);

        txtActionName.setBorder(null);
        txtActionName.setMinimumSize(new java.awt.Dimension(150, 20));
        txtActionName.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlMain.add(txtActionName, gridBagConstraints);

        txtModule.setBorder(null);
        txtModule.setMinimumSize(new java.awt.Dimension(150, 20));
        txtModule.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(txtModule, gridBagConstraints);

        txtSubject.setMinimumSize(new java.awt.Dimension(200, 20));
        txtSubject.setPreferredSize(new java.awt.Dimension(400, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(txtSubject, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(370, 150));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(370, 150));
        tblRoles.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblRoles);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(jScrollPane1, gridBagConstraints);

        rdBtnPromptUserYes.setText("Yes");
        rdBtnPromptUserYes.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdBtnPromptUserYes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 105, 0, 0);
        pnlMain.add(rdBtnPromptUserYes, gridBagConstraints);

        rdBtnPromptUserNo.setText("No");
        rdBtnPromptUserNo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdBtnPromptUserNo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 150, 0, 0);
        pnlMain.add(rdBtnPromptUserNo, gridBagConstraints);

        pnlMessage.setMinimumSize(new java.awt.Dimension(192, 135));
        pnlMessage.setPreferredSize(new java.awt.Dimension(192, 135));
        txtMessage.setColumns(80);
        txtMessage.setFont(CoeusFontFactory.getNormalFont());
        txtMessage.setLineWrap(true);
        txtMessage.setRows(5);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setMinimumSize(new java.awt.Dimension(190, 149));
        pnlMessage.setViewportView(txtMessage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 0);
        pnlMain.add(pnlMessage, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 210, 0, 0);
        pnlMain.add(lblStatus, gridBagConstraints);

        rdStatusActive.setText("Active");
        rdStatusActive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdStatusActive.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 255, 0, 0);
        pnlMain.add(rdStatusActive, gridBagConstraints);

        rdStatusInActive.setText("Inactive");
        rdStatusInActive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdStatusInActive.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 306, 0, 0);
        pnlMain.add(rdStatusInActive, gridBagConstraints);

        pnlRecipients.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        lblRecipients.setFont(CoeusFontFactory.getLabelFont());
        lblRecipients.setText("Default Recipients: ");
        pnlRecipients.add(lblRecipients);

        lblSysGenerated.setText(messageResources.parseMessageKey(MSGKEY_SYSTEM_GENERATED));
        pnlRecipients.add(lblSysGenerated);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlMain.add(pnlRecipients, gridBagConstraints);

        add(pnlMain, new java.awt.GridBagConstraints());

        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        add(btnCancel, gridBagConstraints);

        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 150, 0);
        add(btnAdd, gridBagConstraints);

        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 120, 0);
        add(btnDelete, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.utils.CoeusButton btnAdd;
    public edu.mit.coeus.utils.CoeusButton btnCancel;
    public edu.mit.coeus.utils.CoeusButton btnDelete;
    public edu.mit.coeus.utils.CoeusButton btnOk;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblActionName;
    public javax.swing.JLabel lblMessage;
    public javax.swing.JLabel lblModule;
    public javax.swing.JLabel lblPromptUser;
    public javax.swing.JLabel lblRecipients;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JLabel lblSubject;
    public javax.swing.JLabel lblSysGenerated;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JScrollPane pnlMessage;
    public javax.swing.JPanel pnlRecipients;
    public javax.swing.ButtonGroup rdBtnGroup;
    public javax.swing.JRadioButton rdBtnPromptUserNo;
    public javax.swing.JRadioButton rdBtnPromptUserYes;
    public javax.swing.ButtonGroup rdBtnStatusGroup;
    public javax.swing.JRadioButton rdStatusActive;
    public javax.swing.JRadioButton rdStatusInActive;
    public javax.swing.JTable tblRoles;
    public edu.mit.coeus.utils.CoeusTextField txtActionName;
    public javax.swing.JTextArea txtMessage;
    public edu.mit.coeus.utils.CoeusTextField txtModule;
    public edu.mit.coeus.utils.CoeusTextField txtSubject;
    // End of variables declaration//GEN-END:variables
    
}
