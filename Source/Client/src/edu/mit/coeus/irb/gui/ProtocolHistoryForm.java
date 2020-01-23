/*
 * @(#)ProtocolHistoryForm.java 1.0 07/11/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;

/**
 * This form is used to show the protocol history with changes occured between
 * two sequences of the protocol.
 *
 * @author  leenababu
 */
public class ProtocolHistoryForm extends CoeusInternalFrame {
    
    private CoeusAppletMDIForm mdiForm;
    private int maxSequenceNumber;
    /** Creates new form ProtocolHistoryForm */
    public ProtocolHistoryForm(String title, CoeusAppletMDIForm mdiForm) {
        super(title,  mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }

    public int getMaxSequenceNumber() {
        return maxSequenceNumber;
    }

    public void setMaxSequenceNumber(int maxSequenceNumber) {
        this.maxSequenceNumber = maxSequenceNumber;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        pnlDetails = new javax.swing.JPanel();
        lblProtocolNo = new edu.mit.coeus.utils.CoeusLabel();
        lblPI = new edu.mit.coeus.utils.CoeusLabel();
        lblSeqNo = new edu.mit.coeus.utils.CoeusLabel();
        lblTitle = new edu.mit.coeus.utils.CoeusLabel();
        lblSeqNo1 = new edu.mit.coeus.utils.CoeusLabel();
        lblProtlNoValue = new edu.mit.coeus.utils.CoeusLabel();
        lblSeqValue = new edu.mit.coeus.utils.CoeusLabel();
        lblActionValue = new edu.mit.coeus.utils.CoeusLabel();
        lblPIValue = new edu.mit.coeus.utils.CoeusLabel();
        scrTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        pnlItemList = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblItemList = new javax.swing.JTable();
        pnlItemValues = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblItemValues = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(795, 443));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(900, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 500));
        pnlDetails.setLayout(new java.awt.GridBagLayout());

        pnlDetails.setMaximumSize(new java.awt.Dimension(720, 100));
        pnlDetails.setMinimumSize(new java.awt.Dimension(720, 100));
        pnlDetails.setName("pnlDetails");
        pnlDetails.setPreferredSize(new java.awt.Dimension(720, 100));
        lblProtocolNo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblProtocolNo.setText("Protocol Number: ");
        lblProtocolNo.setMaximumSize(new java.awt.Dimension(105, 14));
        lblProtocolNo.setMinimumSize(new java.awt.Dimension(105, 14));
        lblProtocolNo.setName("lblProtocolNo");
        lblProtocolNo.setPreferredSize(new java.awt.Dimension(105, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlDetails.add(lblProtocolNo, gridBagConstraints);

        lblPI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPI.setText("PI: ");
        lblPI.setMaximumSize(new java.awt.Dimension(105, 14));
        lblPI.setMinimumSize(new java.awt.Dimension(105, 14));
        lblPI.setName("lblPI");
        lblPI.setPreferredSize(new java.awt.Dimension(105, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlDetails.add(lblPI, gridBagConstraints);

        lblSeqNo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblSeqNo.setText("Sequence Number: ");
        lblSeqNo.setMaximumSize(new java.awt.Dimension(115, 14));
        lblSeqNo.setMinimumSize(new java.awt.Dimension(115, 14));
        lblSeqNo.setName("lblSeqNo");
        lblSeqNo.setPreferredSize(new java.awt.Dimension(115, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlDetails.add(lblSeqNo, gridBagConstraints);

        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTitle.setText("Title: ");
        lblTitle.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblTitle.setMaximumSize(new java.awt.Dimension(105, 30));
        lblTitle.setMinimumSize(new java.awt.Dimension(105, 30));
        lblTitle.setName("lblTitle");
        lblTitle.setPreferredSize(new java.awt.Dimension(105, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 3, 3);
        pnlDetails.add(lblTitle, gridBagConstraints);

        lblSeqNo1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblSeqNo1.setText("Last Action: ");
        lblSeqNo1.setMaximumSize(new java.awt.Dimension(80, 14));
        lblSeqNo1.setMinimumSize(new java.awt.Dimension(80, 14));
        lblSeqNo1.setName("lblSeqNo");
        lblSeqNo1.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlDetails.add(lblSeqNo1, gridBagConstraints);

        lblProtlNoValue.setText("0311000001");
        lblProtlNoValue.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblProtlNoValue.setMaximumSize(new java.awt.Dimension(110, 14));
        lblProtlNoValue.setMinimumSize(new java.awt.Dimension(110, 14));
        lblProtlNoValue.setName("lblProtlNoValue");
        lblProtlNoValue.setPreferredSize(new java.awt.Dimension(110, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlDetails.add(lblProtlNoValue, gridBagConstraints);

        lblSeqValue.setText("8");
        lblSeqValue.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblSeqValue.setMaximumSize(new java.awt.Dimension(25, 14));
        lblSeqValue.setMinimumSize(new java.awt.Dimension(25, 14));
        lblSeqValue.setName("lblSeqValue");
        lblSeqValue.setPreferredSize(new java.awt.Dimension(25, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlDetails.add(lblSeqValue, gridBagConstraints);

        lblActionValue.setText("Submitted to IRB");
        lblActionValue.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblActionValue.setMaximumSize(new java.awt.Dimension(250, 14));
        lblActionValue.setMinimumSize(new java.awt.Dimension(250, 14));
        lblActionValue.setName("lblActionValue");
        lblActionValue.setPreferredSize(new java.awt.Dimension(250, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlDetails.add(lblActionValue, gridBagConstraints);

        lblPIValue.setText("John, Mathew");
        lblPIValue.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblPIValue.setMaximumSize(new java.awt.Dimension(105, 14));
        lblPIValue.setMinimumSize(new java.awt.Dimension(105, 14));
        lblPIValue.setName("lblPIValue");
        lblPIValue.setPreferredSize(new java.awt.Dimension(105, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlDetails.add(lblPIValue, gridBagConstraints);

        scrTitle.setBorder(null);
        scrTitle.setEnabled(false);
        scrTitle.setMaximumSize(new java.awt.Dimension(200, 40));
        scrTitle.setMinimumSize(new java.awt.Dimension(200, 40));
        scrTitle.setPreferredSize(new java.awt.Dimension(200, 40));
        txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        txtArTitle.setLineWrap(true);
        txtArTitle.setRows(2);
        txtArTitle.setMaximumSize(new java.awt.Dimension(110, 32));
        txtArTitle.setMinimumSize(new java.awt.Dimension(110, 32));
        scrTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        pnlDetails.add(scrTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel1.add(pnlDetails, gridBagConstraints);

        pnlItemList.setLayout(new java.awt.GridBagLayout());

        pnlItemList.setMaximumSize(new java.awt.Dimension(730, 280));
        pnlItemList.setMinimumSize(new java.awt.Dimension(730, 280));
        pnlItemList.setPreferredSize(new java.awt.Dimension(730, 280));
        jScrollPane3.setEnabled(false);
        jScrollPane3.setMaximumSize(new java.awt.Dimension(710, 260));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(710, 260));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(710, 260));
        tblItemList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tblItemList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlItemList.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 20, 0);
        jPanel1.add(pnlItemList, gridBagConstraints);

        pnlItemValues.setLayout(new java.awt.GridBagLayout());

        pnlItemValues.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Changes to", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlItemValues.setMaximumSize(new java.awt.Dimension(720, 200));
        pnlItemValues.setMinimumSize(new java.awt.Dimension(720, 200));
        pnlItemValues.setPreferredSize(new java.awt.Dimension(720, 200));
        jScrollPane4.setMaximumSize(new java.awt.Dimension(690, 150));
        jScrollPane4.setMinimumSize(new java.awt.Dimension(690, 150));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(700, 150));
        tblItemValues.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tblItemValues);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlItemValues.add(jScrollPane4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 10, 0);
        jPanel1.add(pnlItemValues, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JScrollPane jScrollPane4;
    public edu.mit.coeus.utils.CoeusLabel lblActionValue;
    public edu.mit.coeus.utils.CoeusLabel lblPI;
    public edu.mit.coeus.utils.CoeusLabel lblPIValue;
    public edu.mit.coeus.utils.CoeusLabel lblProtlNoValue;
    public edu.mit.coeus.utils.CoeusLabel lblProtocolNo;
    public edu.mit.coeus.utils.CoeusLabel lblSeqNo;
    public edu.mit.coeus.utils.CoeusLabel lblSeqNo1;
    public edu.mit.coeus.utils.CoeusLabel lblSeqValue;
    public edu.mit.coeus.utils.CoeusLabel lblTitle;
    public javax.swing.JPanel pnlDetails;
    public javax.swing.JPanel pnlItemList;
    public javax.swing.JPanel pnlItemValues;
    public javax.swing.JScrollPane scrTitle;
    public javax.swing.JTable tblItemList;
    public javax.swing.JTable tblItemValues;
    public javax.swing.JTextArea txtArTitle;
    // End of variables declaration//GEN-END:variables

}
