/*
 * AwardHeaderForm.java
 *
 * Created on May 24, 2004, 10:25 AM
 */

package edu.mit.coeus.award.gui;

import javax.swing.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.bean.AwardDetailsBean;
/**
 *
 * @author  ajaygm
 */
public class AwardHeaderForm extends javax.swing.JPanel {
    
    private static final String EMPTY_STRING = "";
        
    /** Creates new form AwardHeaderForm */
    public AwardHeaderForm () {
        initComponents ();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblSponsorAwardNumber = new javax.swing.JLabel();
        lblAwardNumber = new javax.swing.JLabel();
        lblSequenceNumber = new javax.swing.JLabel();
        lblSequenceNumberValue = new javax.swing.JLabel();
        lblAwardNumberValue = new javax.swing.JLabel();
        lblSponsorAwardNumberValue = new javax.swing.JLabel();
        sptrApprovedEquipment = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        lblSponsorAwardNumber.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorAwardNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorAwardNumber.setText("Sponsor Award Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(lblSponsorAwardNumber, gridBagConstraints);

        lblAwardNumber.setFont(CoeusFontFactory.getLabelFont());
        lblAwardNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAwardNumber.setText("Award Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(lblAwardNumber, gridBagConstraints);

        lblSequenceNumber.setFont(CoeusFontFactory.getLabelFont());
        lblSequenceNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSequenceNumber.setText("Sequence Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3;
        add(lblSequenceNumber, gridBagConstraints);

        lblSequenceNumberValue.setFont(CoeusFontFactory.getNormalFont());
        lblSequenceNumberValue.setMinimumSize(new java.awt.Dimension(100, 15));
        lblSequenceNumberValue.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        add(lblSequenceNumberValue, gridBagConstraints);

        lblAwardNumberValue.setFont(CoeusFontFactory.getNormalFont());
        lblAwardNumberValue.setMinimumSize(new java.awt.Dimension(80, 16));
        lblAwardNumberValue.setPreferredSize(new java.awt.Dimension(80, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        add(lblAwardNumberValue, gridBagConstraints);

        lblSponsorAwardNumberValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorAwardNumberValue.setMinimumSize(new java.awt.Dimension(20, 20));
        lblSponsorAwardNumberValue.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblSponsorAwardNumberValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(sptrApprovedEquipment, gridBagConstraints);

    }//GEN-END:initComponents
    
    public void setFormData(AwardDetailsBean awardDetailsBean) {
        String sponsorAwardNo = awardDetailsBean.getSponsorAwardNumber();
        int seqNo = awardDetailsBean.getSequenceNumber ();
        if(sponsorAwardNo == null){
            sponsorAwardNo = EMPTY_STRING;
        }
        lblSponsorAwardNumberValue.setText (sponsorAwardNo);
        lblAwardNumberValue.setText (awardDetailsBean.getMitAwardNumber().trim ());
        lblSequenceNumberValue.setText (EMPTY_STRING + seqNo);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblAwardNumber;
    public javax.swing.JLabel lblAwardNumberValue;
    public javax.swing.JLabel lblSequenceNumber;
    public javax.swing.JLabel lblSequenceNumberValue;
    public javax.swing.JLabel lblSponsorAwardNumber;
    public javax.swing.JLabel lblSponsorAwardNumberValue;
    public javax.swing.JSeparator sptrApprovedEquipment;
    // End of variables declaration//GEN-END:variables
    
}
