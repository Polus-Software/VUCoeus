/*
 * AddModifyCustomDataElements.java
 *
 * Created on December 20, 2004, 11:38 AM
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  nadhgj
 */
public class AddModifyCustomDataElements extends javax.swing.JComponent {
    
    /** Creates new form AddModifyCustomDataElements */
    public AddModifyCustomDataElements() {
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
        lblColumnName = new javax.swing.JLabel();
        lblDefaultValue = new javax.swing.JLabel();
        lblColumnLabel = new javax.swing.JLabel();
        lblDataType = new javax.swing.JLabel();
        lblLookupWindow = new javax.swing.JLabel();
        lblDataLength = new javax.swing.JLabel();
        lblLookupArgument = new javax.swing.JLabel();
        chkLookUp = new javax.swing.JCheckBox();
        cmbDataType = new edu.mit.coeus.utils.CoeusComboBox();
        txtColumnName = new edu.mit.coeus.utils.CoeusTextField();
        txtColumnLabel = new edu.mit.coeus.utils.CoeusTextField();
        txtDefaultValue = new edu.mit.coeus.utils.CoeusTextField();
        txtDataLength = new edu.mit.coeus.utils.CoeusTextField();
        cmbLookupWindow = new edu.mit.coeus.utils.CoeusComboBox();
        cmbLookupArgument = new edu.mit.coeus.utils.CoeusComboBox();
        lblGroup = new javax.swing.JLabel();
        txtGroupData = new edu.mit.coeus.utils.CoeusTextField();
        scrPnColumnUsage = new javax.swing.JScrollPane();
        tblColumnUsage = new javax.swing.JTable();
        scrPnModules = new javax.swing.JScrollPane();
        tblModules = new javax.swing.JTable();
        pnlButton = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(730, 310));
        setPreferredSize(new java.awt.Dimension(730, 310));
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlMain.setMinimumSize(new java.awt.Dimension(625, 140));
        pnlMain.setPreferredSize(new java.awt.Dimension(650, 140));
        lblColumnName.setFont(CoeusFontFactory.getLabelFont());
        lblColumnName.setText("Column Name:");
        lblColumnName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlMain.add(lblColumnName, gridBagConstraints);

        lblDefaultValue.setFont(CoeusFontFactory.getLabelFont());
        lblDefaultValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDefaultValue.setText("Default Value:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlMain.add(lblDefaultValue, gridBagConstraints);

        lblColumnLabel.setFont(CoeusFontFactory.getLabelFont());
        lblColumnLabel.setText("Column Label:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblColumnLabel, gridBagConstraints);

        lblDataType.setFont(CoeusFontFactory.getLabelFont());
        lblDataType.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDataType.setText("Data Type:");
        lblDataType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblDataType, gridBagConstraints);

        lblLookupWindow.setFont(CoeusFontFactory.getLabelFont());
        lblLookupWindow.setText("Lookup Window:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblLookupWindow, gridBagConstraints);

        lblDataLength.setFont(CoeusFontFactory.getLabelFont());
        lblDataLength.setText("Data Length:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblDataLength, gridBagConstraints);

        lblLookupArgument.setFont(CoeusFontFactory.getLabelFont());
        lblLookupArgument.setText("Lookup Argument:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(lblLookupArgument, gridBagConstraints);

        chkLookUp.setFont(CoeusFontFactory.getLabelFont());
        chkLookUp.setText("Has Lookup  :");
        chkLookUp.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        pnlMain.add(chkLookUp, gridBagConstraints);

        cmbDataType.setMinimumSize(new java.awt.Dimension(90, 20));
        cmbDataType.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(cmbDataType, gridBagConstraints);

        txtColumnName.setMinimumSize(new java.awt.Dimension(199, 20));
        txtColumnName.setPreferredSize(new java.awt.Dimension(199, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMain.add(txtColumnName, gridBagConstraints);

        txtColumnLabel.setMinimumSize(new java.awt.Dimension(199, 20));
        txtColumnLabel.setPreferredSize(new java.awt.Dimension(199, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(txtColumnLabel, gridBagConstraints);

        txtDefaultValue.setMinimumSize(new java.awt.Dimension(199, 20));
        txtDefaultValue.setPreferredSize(new java.awt.Dimension(199, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMain.add(txtDefaultValue, gridBagConstraints);

        txtDataLength.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDataLength.setMinimumSize(new java.awt.Dimension(90, 20));
        txtDataLength.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(txtDataLength, gridBagConstraints);

        cmbLookupWindow.setMinimumSize(new java.awt.Dimension(199, 20));
        cmbLookupWindow.setPreferredSize(new java.awt.Dimension(199, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(cmbLookupWindow, gridBagConstraints);

        cmbLookupArgument.setMinimumSize(new java.awt.Dimension(199, 20));
        cmbLookupArgument.setPreferredSize(new java.awt.Dimension(199, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(cmbLookupArgument, gridBagConstraints);

        lblGroup.setFont(CoeusFontFactory.getLabelFont());
        lblGroup.setText("Group:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        pnlMain.add(lblGroup, gridBagConstraints);

        txtGroupData.setMinimumSize(new java.awt.Dimension(200, 20));
        txtGroupData.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(txtGroupData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 2, 2);
        add(pnlMain, gridBagConstraints);

        scrPnColumnUsage.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Column Usage", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnColumnUsage.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnColumnUsage.setMinimumSize(new java.awt.Dimension(256, 160));
        scrPnColumnUsage.setPreferredSize(new java.awt.Dimension(256, 160));
        tblColumnUsage.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnColumnUsage.setViewportView(tblColumnUsage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 3);
        add(scrPnColumnUsage, gridBagConstraints);

        scrPnModules.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Coeus Modules", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnModules.setMinimumSize(new java.awt.Dimension(265, 160));
        scrPnModules.setPreferredSize(new java.awt.Dimension(270, 160));
        tblModules.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnModules.setViewportView(tblModules);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        add(scrPnModules, gridBagConstraints);

        pnlButton.setLayout(new java.awt.GridBagLayout());

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("<< Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(97, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(97, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(87, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlButton.add(btnAdd, gridBagConstraints);

        btnRemove.setFont(CoeusFontFactory.getLabelFont());
        btnRemove.setMnemonic('R');
        btnRemove.setText("Remove >>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlButton.add(btnRemove, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(39, 0, 0, 0);
        add(pnlButton, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 10, 10);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(33, 2, 0, 10);
        add(btnCancel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnRemove;
    public javax.swing.JCheckBox chkLookUp;
    public edu.mit.coeus.utils.CoeusComboBox cmbDataType;
    public edu.mit.coeus.utils.CoeusComboBox cmbLookupArgument;
    public edu.mit.coeus.utils.CoeusComboBox cmbLookupWindow;
    public javax.swing.JLabel lblColumnLabel;
    public javax.swing.JLabel lblColumnName;
    public javax.swing.JLabel lblDataLength;
    public javax.swing.JLabel lblDataType;
    public javax.swing.JLabel lblDefaultValue;
    public javax.swing.JLabel lblGroup;
    public javax.swing.JLabel lblLookupArgument;
    public javax.swing.JLabel lblLookupWindow;
    public javax.swing.JPanel pnlButton;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JScrollPane scrPnColumnUsage;
    public javax.swing.JScrollPane scrPnModules;
    public javax.swing.JTable tblColumnUsage;
    public javax.swing.JTable tblModules;
    public edu.mit.coeus.utils.CoeusTextField txtColumnLabel;
    public edu.mit.coeus.utils.CoeusTextField txtColumnName;
    public edu.mit.coeus.utils.CoeusTextField txtDataLength;
    public edu.mit.coeus.utils.CoeusTextField txtDefaultValue;
    public edu.mit.coeus.utils.CoeusTextField txtGroupData;
    // End of variables declaration//GEN-END:variables
     
}
