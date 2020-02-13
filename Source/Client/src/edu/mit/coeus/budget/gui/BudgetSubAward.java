/*
 * SubAwardBudget.java
 *
 * Created on February 16, 2006, 11:27 AM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  sharathk
 */
public class BudgetSubAward extends javax.swing.JPanel {
    
    /** Creates new form SubAwardBudget */
    public BudgetSubAward() {
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

        lblPropNum = new javax.swing.JLabel();
        lblProposalNumber = new javax.swing.JLabel();
        lblVersionNum = new javax.swing.JLabel();
        lblVersionNumber = new javax.swing.JLabel();
        scrPnSubAwardBudget = new javax.swing.JScrollPane();
        tblSubAwardBudget = new javax.swing.JTable();
        tblSubAwardBudget.setRowHeight(25);
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnlDetails = new javax.swing.JPanel();
        scrPnStatus = new javax.swing.JScrollPane();
        txtArStatus = new javax.swing.JTextArea();
        pnlSubAwardLastUpdated = new edu.mit.coeus.gui.LastUpdatedPanel();
        pnlPDFLastUpdated = new edu.mit.coeus.gui.LastUpdatedPanel();
        pnlXMLLastUpdated = new edu.mit.coeus.gui.LastUpdatedPanel();
        lblStatus = new javax.swing.JLabel();
        scrPnAttachments = new javax.swing.JScrollPane();
        lstAttachments = new javax.swing.JList();
        lblPDF = new javax.swing.JLabel();
        lblXML = new javax.swing.JLabel();
        lblSubAward = new javax.swing.JLabel();
        lblAttachments = new javax.swing.JLabel();
        lblPdfFileName = new javax.swing.JLabel();
        lblPdfFile = new javax.swing.JLabel();
        lblNamespaceValue = new javax.swing.JLabel();
        lblNamespace = new javax.swing.JLabel();
        chkDetails = new javax.swing.JCheckBox();
        lblScrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        lblComments = new javax.swing.JLabel();
        btnTranslate = new javax.swing.JButton();
        btnViewPDF = new javax.swing.JButton();
        btnViewXML = new javax.swing.JButton();
        btnUploadPDF = new javax.swing.JButton();
        lblParent = new javax.swing.JLabel();
        btnSubAwardDetails = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(500, 450));
        setPreferredSize(new java.awt.Dimension(750, 450));
        lblPropNum.setFont(CoeusFontFactory.getLabelFont());
        lblPropNum.setText("Proposal Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblPropNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblProposalNumber, gridBagConstraints);

        lblVersionNum.setFont(CoeusFontFactory.getLabelFont());
        lblVersionNum.setText("Version Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblVersionNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblVersionNumber, gridBagConstraints);

        scrPnSubAwardBudget.setPreferredSize(new java.awt.Dimension(475, 100));
        tblSubAwardBudget.setFont(CoeusFontFactory.getNormalFont());
        tblSubAwardBudget.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnSubAwardBudget.setViewportView(tblSubAwardBudget);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(scrPnSubAwardBudget, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnDelete, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnCancel, gridBagConstraints);

        pnlDetails.setLayout(new java.awt.GridBagLayout());

        pnlDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Details", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getNormalFont()));
        pnlDetails.setMinimumSize(new java.awt.Dimension(487, 200));
        pnlDetails.setPreferredSize(new java.awt.Dimension(467, 200));
        scrPnStatus.setMinimumSize(new java.awt.Dimension(24, 50));
        txtArStatus.setEditable(false);
        txtArStatus.setFont(CoeusFontFactory.getNormalFont());
        scrPnStatus.setViewportView(txtArStatus);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        pnlDetails.add(scrPnStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlDetails.add(pnlSubAwardLastUpdated, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlDetails.add(pnlPDFLastUpdated, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlDetails.add(pnlXMLLastUpdated, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlDetails.add(lblStatus, gridBagConstraints);

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

        lblXML.setFont(CoeusFontFactory.getLabelFont());
        lblXML.setText("XML");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDetails.add(lblXML, gridBagConstraints);

        lblSubAward.setFont(CoeusFontFactory.getLabelFont());
        lblSubAward.setText("Sub Award");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDetails.add(lblSubAward, gridBagConstraints);

        lblAttachments.setFont(CoeusFontFactory.getLabelFont());
        lblAttachments.setText("Attachments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlDetails.add(lblAttachments, gridBagConstraints);

        lblPdfFileName.setFont(CoeusFontFactory.getNormalFont());
        lblPdfFileName.setText("none");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlDetails.add(lblPdfFileName, gridBagConstraints);

        lblPdfFile.setFont(CoeusFontFactory.getLabelFont());
        lblPdfFile.setText("PDF File:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDetails.add(lblPdfFile, gridBagConstraints);

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

        chkDetails.setFont(CoeusFontFactory.getLabelFont());
        chkDetails.setMnemonic('S');
        chkDetails.setText("Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        add(chkDetails, gridBagConstraints);

        lblScrPnComments.setMinimumSize(new java.awt.Dimension(24, 50));
        lblScrPnComments.setPreferredSize(new java.awt.Dimension(250, 50));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        lblScrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        add(lblScrPnComments, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(lblComments, gridBagConstraints);

        btnTranslate.setFont(CoeusFontFactory.getLabelFont());
        btnTranslate.setMnemonic('T');
        btnTranslate.setText("Translate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnTranslate, gridBagConstraints);

        btnViewPDF.setFont(CoeusFontFactory.getLabelFont());
        btnViewPDF.setMnemonic('V');
        btnViewPDF.setText("View Form");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnViewPDF, gridBagConstraints);

        btnViewXML.setFont(CoeusFontFactory.getLabelFont());
        btnViewXML.setMnemonic('X');
        btnViewXML.setText("View XML");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnViewXML, gridBagConstraints);

        btnUploadPDF.setFont(CoeusFontFactory.getLabelFont());
        btnUploadPDF.setMnemonic('U');
        btnUploadPDF.setText("Upload");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnUploadPDF, gridBagConstraints);

        lblParent.setFont(CoeusFontFactory.getLabelFont());
        lblParent.setForeground(java.awt.Color.red);
        lblParent.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblParent.setMaximumSize(new java.awt.Dimension(100, 20));
        lblParent.setMinimumSize(new java.awt.Dimension(100, 20));
        lblParent.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblParent, gridBagConstraints);

        btnSubAwardDetails.setFont(CoeusFontFactory.getLabelFont());
        btnSubAwardDetails.setMnemonic('b');
        btnSubAwardDetails.setText("Sub Award Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnSubAwardDetails, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnSubAwardDetails;
    public javax.swing.JButton btnTranslate;
    public javax.swing.JButton btnUploadPDF;
    public javax.swing.JButton btnViewPDF;
    public javax.swing.JButton btnViewXML;
    public javax.swing.JCheckBox chkDetails;
    private javax.swing.JLabel lblAttachments;
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblNamespace;
    public javax.swing.JLabel lblNamespaceValue;
    private javax.swing.JLabel lblPDF;
    public javax.swing.JLabel lblParent;
    private javax.swing.JLabel lblPdfFile;
    public javax.swing.JLabel lblPdfFileName;
    private javax.swing.JLabel lblPropNum;
    public javax.swing.JLabel lblProposalNumber;
    private javax.swing.JScrollPane lblScrPnComments;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSubAward;
    private javax.swing.JLabel lblVersionNum;
    public javax.swing.JLabel lblVersionNumber;
    private javax.swing.JLabel lblXML;
    public javax.swing.JList lstAttachments;
    public javax.swing.JPanel pnlDetails;
    public edu.mit.coeus.gui.LastUpdatedPanel pnlPDFLastUpdated;
    public edu.mit.coeus.gui.LastUpdatedPanel pnlSubAwardLastUpdated;
    public edu.mit.coeus.gui.LastUpdatedPanel pnlXMLLastUpdated;
    public javax.swing.JScrollPane scrPnAttachments;
    private javax.swing.JScrollPane scrPnStatus;
    private javax.swing.JScrollPane scrPnSubAwardBudget;
    public javax.swing.JTable tblSubAwardBudget;
    public javax.swing.JTextArea txtArComments;
    public javax.swing.JTextArea txtArStatus;
    // End of variables declaration//GEN-END:variables
    
}
