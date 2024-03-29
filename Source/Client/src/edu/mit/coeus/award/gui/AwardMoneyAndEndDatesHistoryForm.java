/*
 * AwardMoneyAndEndDatesHistoryForm.java
 *
 * Created on June 7, 2004, 11:37 AM
 */

package edu.mit.coeus.award.gui;

import javax.swing.*;

import edu.mit.coeus.gui.*;

/**
 *
 * @author  surekhan
 */
public class AwardMoneyAndEndDatesHistoryForm extends javax.swing.JPanel {
    
    /** Creates new form AwardMoneyAndEndDatesHistoryForm */
    public AwardMoneyAndEndDatesHistoryForm() {
        initComponents();
    }
    
      public static void main(String s[]) {
     JFrame frame = new JFrame("Money and End Dates History for Award");
     AwardMoneyAndEndDatesHistoryForm awardMoneyAndEndDatesHistoryForm = new AwardMoneyAndEndDatesHistoryForm();
     frame.getContentPane().add(awardMoneyAndEndDatesHistoryForm);
     frame.setSize(775,425);
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

        scrPnTable = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        btnShowDetailTransaction = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        scrPnHeader = new javax.swing.JScrollPane();
        pnlHeader = new javax.swing.JPanel();
        lblHeader1 = new javax.swing.JLabel();
        lblHeader2 = new javax.swing.JLabel();
        lblHeader7 = new javax.swing.JLabel();
        lblHeader3 = new javax.swing.JLabel();
        lblHeader4 = new javax.swing.JLabel();
        lblHeader5 = new javax.swing.JLabel();
        lblHeader6 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        scrPnTable.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrPnTable.setMinimumSize(new java.awt.Dimension(952, 440));
        scrPnTable.setPreferredSize(new java.awt.Dimension(952, 440));
        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnTable.setViewportView(tblHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scrPnTable, gridBagConstraints);

        btnShowDetailTransaction.setFont(CoeusFontFactory.getLabelFont());
        btnShowDetailTransaction.setMnemonic('D');
        btnShowDetailTransaction.setText("Show Detail Transaction");
        btnShowDetailTransaction.setMaximumSize(new java.awt.Dimension(250, 25));
        btnShowDetailTransaction.setMinimumSize(new java.awt.Dimension(190, 25));
        btnShowDetailTransaction.setPreferredSize(new java.awt.Dimension(190, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 2);
        add(btnShowDetailTransaction, gridBagConstraints);

        btnPrevious.setFont(CoeusFontFactory.getLabelFont());
        btnPrevious.setMnemonic('P');
        btnPrevious.setText("Previous");
        btnPrevious.setMaximumSize(new java.awt.Dimension(85, 25));
        btnPrevious.setMinimumSize(new java.awt.Dimension(100, 25));
        btnPrevious.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 2);
        add(btnPrevious, gridBagConstraints);

        btnNext.setFont(CoeusFontFactory.getLabelFont());
        btnNext.setMnemonic('N');
        btnNext.setText("Next");
        btnNext.setMaximumSize(new java.awt.Dimension(85, 25));
        btnNext.setMinimumSize(new java.awt.Dimension(85, 25));
        btnNext.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 2);
        add(btnNext, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setMnemonic('r');
        btnPrint.setText("Print");
        btnPrint.setMaximumSize(new java.awt.Dimension(85, 25));
        btnPrint.setMinimumSize(new java.awt.Dimension(85, 25));
        btnPrint.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 2);
        add(btnPrint, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(85, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(85, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 2);
        add(btnClose, gridBagConstraints);

        scrPnHeader.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnHeader.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrPnHeader.setAutoscrolls(true);
        scrPnHeader.setMinimumSize(new java.awt.Dimension(600, 35));
        scrPnHeader.setPreferredSize(new java.awt.Dimension(600, 35));
        pnlHeader.setLayout(new java.awt.GridBagLayout());

        pnlHeader.setMinimumSize(new java.awt.Dimension(950, 35));
        pnlHeader.setPreferredSize(new java.awt.Dimension(1900, 35));
        pnlHeader.setRequestFocusEnabled(false);
        lblHeader1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader1.setText("header1");
        lblHeader1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblHeader1.setMinimumSize(new java.awt.Dimension(200, 25));
        lblHeader1.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 170, 0, 0);
        pnlHeader.add(lblHeader1, gridBagConstraints);

        lblHeader2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader2.setText("header2");
        lblHeader2.setMinimumSize(new java.awt.Dimension(200, 25));
        lblHeader2.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 150, 0, 0);
        pnlHeader.add(lblHeader2, gridBagConstraints);

        lblHeader7.setText("jLabel1");
        lblHeader7.setMaximumSize(new java.awt.Dimension(45, 14));
        lblHeader7.setMinimumSize(new java.awt.Dimension(45, 14));
        lblHeader7.setOpaque(true);
        lblHeader7.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 240);
        pnlHeader.add(lblHeader7, gridBagConstraints);

        lblHeader3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader3.setText("header3");
        lblHeader3.setMinimumSize(new java.awt.Dimension(200, 25));
        lblHeader3.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 190, 0, 0);
        pnlHeader.add(lblHeader3, gridBagConstraints);

        lblHeader4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader4.setText("header4");
        lblHeader4.setMinimumSize(new java.awt.Dimension(200, 25));
        lblHeader4.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 110, 0, 80);
        pnlHeader.add(lblHeader4, gridBagConstraints);

        lblHeader5.setText("    Header5");
        lblHeader5.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        pnlHeader.add(lblHeader5, gridBagConstraints);

        lblHeader6.setText("Header6");
        lblHeader6.setMinimumSize(new java.awt.Dimension(53, 14));
        lblHeader6.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        pnlHeader.add(lblHeader6, gridBagConstraints);

        scrPnHeader.setViewportView(pnlHeader);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scrPnHeader, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnNext;
    public javax.swing.JButton btnPrevious;
    public javax.swing.JButton btnPrint;
    public javax.swing.JButton btnShowDetailTransaction;
    public javax.swing.JLabel lblHeader1;
    public javax.swing.JLabel lblHeader2;
    public javax.swing.JLabel lblHeader3;
    public javax.swing.JLabel lblHeader4;
    public javax.swing.JLabel lblHeader5;
    public javax.swing.JLabel lblHeader6;
    public javax.swing.JLabel lblHeader7;
    public javax.swing.JPanel pnlHeader;
    public javax.swing.JScrollPane scrPnHeader;
    public javax.swing.JScrollPane scrPnTable;
    public javax.swing.JTable tblHistory;
    // End of variables declaration//GEN-END:variables
    
}
