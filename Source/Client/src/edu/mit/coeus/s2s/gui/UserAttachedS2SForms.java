/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UserAttachedS2SForms.java
 *
 * Created on Aug 27, 2013, 12:33:47 PM
 */

package edu.mit.coeus.s2s.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author vishal
 */
public class UserAttachedS2SForms extends javax.swing.JPanel {

    /** Creates new form UserAttachedS2SForms */
    public UserAttachedS2SForms() {
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

        lblProplNum = new javax.swing.JLabel();
        lblProposalNumber = new javax.swing.JLabel();
        scrPnUserS2SForms = new javax.swing.JScrollPane();
        tblUserS2SForms = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUploadPDF = new javax.swing.JButton();
        btnTranslate = new javax.swing.JButton();
        btnViewPDF = new javax.swing.JButton();
        btnViewXML = new javax.swing.JButton();
        pnlDetails = new javax.swing.JPanel();
        pnlPDFLastUpdated = new edu.mit.coeus.gui.LastUpdatedPanel();
        scrPnAttachments = new javax.swing.JScrollPane();
        lstAttachments = new javax.swing.JList();
        lblPDF = new javax.swing.JLabel();
        lblAttachments = new javax.swing.JLabel();
        lblNamespaceValue = new javax.swing.JLabel();
        lblNamespace = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(500, 400));
        setPreferredSize(new java.awt.Dimension(750, 450));
        setLayout(new java.awt.GridBagLayout());

        lblProplNum.setFont(CoeusFontFactory.getLabelFont());
        lblProplNum.setText("Proposal Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblProplNum, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        add(lblProposalNumber, gridBagConstraints);

        scrPnUserS2SForms.setPreferredSize(new java.awt.Dimension(400, 100));

        tblUserS2SForms.setFont(CoeusFontFactory.getNormalFont());
        tblUserS2SForms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tblUserS2SForms.setRowHeight(18);
        scrPnUserS2SForms.setViewportView(tblUserS2SForms);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(scrPnUserS2SForms, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnCancel, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnDelete, gridBagConstraints);

        btnUploadPDF.setFont(CoeusFontFactory.getLabelFont());
        btnUploadPDF.setText("Upload");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnUploadPDF, gridBagConstraints);

        btnTranslate.setFont(CoeusFontFactory.getLabelFont());
        btnTranslate.setText("Translate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnTranslate, gridBagConstraints);

        btnViewPDF.setFont(CoeusFontFactory.getLabelFont());
        btnViewPDF.setText("View Form");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnViewPDF, gridBagConstraints);

        btnViewXML.setFont(CoeusFontFactory.getLabelFont());
        btnViewXML.setText("View XML");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnViewXML, gridBagConstraints);

        pnlDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Details", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getNormalFont()));
        pnlDetails.setMinimumSize(new java.awt.Dimension(487, 200));
        pnlDetails.setPreferredSize(new java.awt.Dimension(467, 120));
        pnlDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlDetails.add(pnlPDFLastUpdated, gridBagConstraints);

        lstAttachments.setFont(CoeusFontFactory.getNormalFont());
        lstAttachments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstAttachments.setVisibleRowCount(3);
        scrPnAttachments.setViewportView(lstAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        pnlDetails.add(scrPnAttachments, gridBagConstraints);

        lblPDF.setFont(CoeusFontFactory.getLabelFont());
        lblPDF.setText("PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDetails.add(lblPDF, gridBagConstraints);

        lblAttachments.setFont(CoeusFontFactory.getLabelFont());
        lblAttachments.setText("Attachments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlDetails.add(lblAttachments, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlDetails.add(lblNamespaceValue, gridBagConstraints);

        lblNamespace.setFont(CoeusFontFactory.getLabelFont());
        lblNamespace.setText("Namespace:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        pnlDetails.add(lblNamespace, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        add(pnlDetails, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnTranslate;
    public javax.swing.JButton btnUploadPDF;
    public javax.swing.JButton btnViewPDF;
    public javax.swing.JButton btnViewXML;
    private javax.swing.JLabel lblAttachments;
    private javax.swing.JLabel lblNamespace;
    public javax.swing.JLabel lblNamespaceValue;
    private javax.swing.JLabel lblPDF;
    private javax.swing.JLabel lblProplNum;
    public javax.swing.JLabel lblProposalNumber;
    public javax.swing.JList lstAttachments;
    public javax.swing.JPanel pnlDetails;
    public edu.mit.coeus.gui.LastUpdatedPanel pnlPDFLastUpdated;
    public javax.swing.JScrollPane scrPnAttachments;
    private javax.swing.JScrollPane scrPnUserS2SForms;
    public javax.swing.JTable tblUserS2SForms;
    // End of variables declaration//GEN-END:variables

}
