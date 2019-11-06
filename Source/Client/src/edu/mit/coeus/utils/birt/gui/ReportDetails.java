/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ReportDetails.java
 *
 * Created on Apr 13, 2009, 1:10:59 PM
 */

package edu.mit.coeus.utils.birt.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.LimitedPlainDocument;

/**
 *
 * @author sharathk
 */
public class ReportDetails extends javax.swing.JPanel {

    /** Creates new form ReportDetails */
    public ReportDetails() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblId = new javax.swing.JLabel();
        lblLabel = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblRight = new javax.swing.JLabel();
        lblLastUpdate = new javax.swing.JLabel();
        lblBy = new javax.swing.JLabel();
        pnlReportTemplate = new javax.swing.JPanel();
        lblFileLastUpdate = new javax.swing.JLabel();
        txtTemplateTimestamp = new javax.swing.JLabel();
        lblFileby = new javax.swing.JLabel();
        lblFile = new javax.swing.JLabel();
        txtTemplateUpdateUser = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        btnFileChooser = new javax.swing.JButton();
        btnDownload = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        txtLabel = new javax.swing.JTextField();
        scrPnDescription = new javax.swing.JScrollPane();
        txtArDescription = new javax.swing.JTextArea();
        cmbType = new javax.swing.JComboBox();
        cmbRight = new javax.swing.JComboBox();
        txtTimestamp = new javax.swing.JLabel();
        txtUpdateUser = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblId.setFont(CoeusFontFactory.getLabelFont());
        lblId.setText("Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblId, gridBagConstraints);

        lblLabel.setFont(CoeusFontFactory.getLabelFont());
        lblLabel.setText("Label:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblLabel, gridBagConstraints);

        lblType.setFont(CoeusFontFactory.getLabelFont());
        lblType.setText("Used In:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblType, gridBagConstraints);

        lblRight.setFont(CoeusFontFactory.getLabelFont());
        lblRight.setText("Right:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblRight, gridBagConstraints);

        lblLastUpdate.setFont(CoeusFontFactory.getLabelFont());
        lblLastUpdate.setText("Last Update:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblLastUpdate, gridBagConstraints);

        lblBy.setText("by");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblBy, gridBagConstraints);

        pnlReportTemplate.setBorder(javax.swing.BorderFactory.createTitledBorder("Report Template"));
        pnlReportTemplate.setLayout(new java.awt.GridBagLayout());

        lblFileLastUpdate.setFont(CoeusFontFactory.getLabelFont());
        lblFileLastUpdate.setText("Last Update:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlReportTemplate.add(lblFileLastUpdate, gridBagConstraints);

        txtTemplateTimestamp.setText("<Timestamp>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlReportTemplate.add(txtTemplateTimestamp, gridBagConstraints);

        lblFileby.setText("by");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlReportTemplate.add(lblFileby, gridBagConstraints);

        lblFile.setFont(CoeusFontFactory.getLabelFont());
        lblFile.setText("File:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlReportTemplate.add(lblFile, gridBagConstraints);

        txtTemplateUpdateUser.setText("<user>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlReportTemplate.add(txtTemplateUpdateUser, gridBagConstraints);

        txtFile.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlReportTemplate.add(txtFile, gridBagConstraints);

        btnFileChooser.setFont(CoeusFontFactory.getLabelFont());
        btnFileChooser.setText("...");
        btnFileChooser.setToolTipText("Upload Report Template");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlReportTemplate.add(btnFileChooser, gridBagConstraints);

        btnDownload.setFont(CoeusFontFactory.getLabelFont());
        btnDownload.setText("Download Template");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        pnlReportTemplate.add(btnDownload, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(pnlReportTemplate, gridBagConstraints);

        txtId.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(txtId, gridBagConstraints);

        txtLabel.setDocument(new LimitedPlainDocument(50));
        txtLabel.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(txtLabel, gridBagConstraints);

        txtArDescription.setColumns(20);
        txtArDescription.setDocument(new LimitedPlainDocument(2000));
        txtArDescription.setFont(CoeusFontFactory.getNormalFont());
        txtArDescription.setRows(5);
        scrPnDescription.setViewportView(txtArDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(scrPnDescription, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(cmbType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(cmbRight, gridBagConstraints);

        txtTimestamp.setText("<timestamp>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(txtTimestamp, gridBagConstraints);

        txtUpdateUser.setText("<user>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(txtUpdateUser, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblDescription, gridBagConstraints);

        btnSave.setFont(CoeusFontFactory.getLabelFont());
        btnSave.setMnemonic('S');
        btnSave.setText("Save");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnSave, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnCancel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDownload;
    public javax.swing.JButton btnFileChooser;
    public javax.swing.JButton btnSave;
    public javax.swing.JComboBox cmbRight;
    public javax.swing.JComboBox cmbType;
    private javax.swing.JLabel lblBy;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFile;
    private javax.swing.JLabel lblFileLastUpdate;
    private javax.swing.JLabel lblFileby;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLabel;
    private javax.swing.JLabel lblLastUpdate;
    private javax.swing.JLabel lblRight;
    private javax.swing.JLabel lblType;
    private javax.swing.JPanel pnlReportTemplate;
    private javax.swing.JScrollPane scrPnDescription;
    public javax.swing.JTextArea txtArDescription;
    public javax.swing.JTextField txtFile;
    public javax.swing.JTextField txtId;
    public javax.swing.JTextField txtLabel;
    public javax.swing.JLabel txtTemplateTimestamp;
    public javax.swing.JLabel txtTemplateUpdateUser;
    public javax.swing.JLabel txtTimestamp;
    public javax.swing.JLabel txtUpdateUser;
    // End of variables declaration//GEN-END:variables

}
