/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import javax.swing.*;
import edu.mit.coeus.gui.*;

/**Displays the Comments History Details for the selected Comment Type
 * CommentsHistoryForm.java
 * Created on April 12, 2004, 5:59 PM
 * @author  ajaygm
 */
public class CommentsHistoryForm extends javax.swing.JComponent {
    
    /** Creates new form CommentsHistoryForm */
    public CommentsHistoryForm () {
        initComponents ();
        txtArComments.setEditable(false);
    }
    
    
//    public static void main(String s[])
//     {
//        JFrame frame = new JFrame("Comments History");
//        CommentsHistoryForm commentsHistoryForm = new CommentsHistoryForm();
//        frame.getContentPane().add(commentsHistoryForm);
//        frame.setSize(585, 275);
//        frame.show();
//     }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblSponsorAwardNumber = new javax.swing.JLabel();
        lblAwardNumber = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        scrPnFiscalReportComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        btnClose = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblSponsorAwardNumberValue = new javax.swing.JLabel();
        lblAwardNumberValue = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(300, 254));
        setMinimumSize(new java.awt.Dimension(300, 254));
        setPreferredSize(new java.awt.Dimension(300, 254));
        lblSponsorAwardNumber.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorAwardNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorAwardNumber.setText("Sponsor Award Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblSponsorAwardNumber, gridBagConstraints);

        lblAwardNumber.setFont(CoeusFontFactory.getLabelFont());
        lblAwardNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAwardNumber.setText("Award Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(lblAwardNumber, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        add(lblComments, gridBagConstraints);

        scrPnFiscalReportComments.setMinimumSize(new java.awt.Dimension(350, 200));
        scrPnFiscalReportComments.setPreferredSize(new java.awt.Dimension(350, 200));
        txtArComments.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnFiscalReportComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 20, 0);
        add(scrPnFiscalReportComments, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnClose, gridBagConstraints);

        btnPrevious.setFont(CoeusFontFactory.getLabelFont());
        btnPrevious.setMnemonic('P');
        btnPrevious.setText("Previous");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnPrevious, gridBagConstraints);

        btnNext.setFont(CoeusFontFactory.getLabelFont());
        btnNext.setMnemonic('N');
        btnNext.setText("Next");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnNext, gridBagConstraints);

        lblSponsorAwardNumberValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblSponsorAwardNumberValue, gridBagConstraints);

        lblAwardNumberValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblAwardNumberValue, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnNext;
    public javax.swing.JButton btnPrevious;
    public javax.swing.JLabel lblAwardNumber;
    public javax.swing.JLabel lblAwardNumberValue;
    public javax.swing.JLabel lblComments;
    public javax.swing.JLabel lblSponsorAwardNumber;
    public javax.swing.JLabel lblSponsorAwardNumberValue;
    public javax.swing.JScrollPane scrPnFiscalReportComments;
    public javax.swing.JTextArea txtArComments;
    // End of variables declaration//GEN-END:variables
    
}
