/*
 * ProposalDetailsForm.java
 *
 * Created on April 12, 2004, 3:46 PM
 */

package edu.mit.coeus.instprop.gui;

/**
 *
 * @author  bijosht
 */
import edu.mit.coeus.gui.CoeusFontFactory;
public class ProposalDetailsForm extends javax.swing.JComponent {
    
    /** Creates new form ProposalDetailsForm */
    public ProposalDetailsForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblProposalNo = new javax.swing.JLabel();
        lblProposalNoValue = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatusValue = new javax.swing.JLabel();
        lblAccount = new javax.swing.JLabel();
        lblAccountValue = new javax.swing.JLabel();
        lblInitialContractAdmin = new javax.swing.JLabel();
        lblInitialContractAdminValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        lblSponsorName = new javax.swing.JLabel();
        lblSponsorType = new javax.swing.JLabel();
        lblSponsorTypeValue = new javax.swing.JLabel();
        lblProposalType = new javax.swing.JLabel();
        lblProposalTypeValue = new javax.swing.JLabel();
        lblLeadUnit = new javax.swing.JLabel();
        lblLeadUnitValue = new javax.swing.JLabel();
        lblLeadUnitName = new javax.swing.JLabel();
        lblPI = new javax.swing.JLabel();
        lblPIValue = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblStartDateValue = new javax.swing.JLabel();
        lblInitialAmount = new javax.swing.JLabel();
        lblPropCreateDate = new javax.swing.JLabel();
        lblPropCreateDateValue = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblEndDateValue = new javax.swing.JLabel();
        lblTotalAmount = new javax.swing.JLabel();
        lblLastModifiedDate = new javax.swing.JLabel();
        lblLastModifiedDateValue = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtInitialAmount = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalAmount = new edu.mit.coeus.utils.DollarCurrencyTextField();
        scrpnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalNo.setText("Proposal No :");
        lblProposalNo.setMaximumSize(new java.awt.Dimension(60, 16));
        lblProposalNo.setMinimumSize(new java.awt.Dimension(60, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblProposalNo, gridBagConstraints);

        lblProposalNoValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblProposalNoValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblProposalNoValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 27);
        add(lblProposalNoValue, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblStatus, gridBagConstraints);

        lblStatusValue.setFont(CoeusFontFactory.getNormalFont());
        lblStatusValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblStatusValue.setMinimumSize(new java.awt.Dimension(270, 20));
        lblStatusValue.setPreferredSize(new java.awt.Dimension(270, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblStatusValue, gridBagConstraints);

        lblAccount.setFont(CoeusFontFactory.getLabelFont());
        lblAccount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//JM        lblAccount.setText("Account :");
        lblAccount.setText("Center Number :"); //JM 5-25-2011 updated to Center per 4.4.2
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblAccount, gridBagConstraints);

        lblAccountValue.setFont(CoeusFontFactory.getNormalFont());
        lblAccountValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblAccountValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblAccountValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 27);
        add(lblAccountValue, gridBagConstraints);

        lblInitialContractAdmin.setFont(CoeusFontFactory.getLabelFont());
        lblInitialContractAdmin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInitialContractAdmin.setText("Initial Contract Admin :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblInitialContractAdmin, gridBagConstraints);

        lblInitialContractAdminValue.setFont(CoeusFontFactory.getNormalFont());
        lblInitialContractAdminValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblInitialContractAdminValue.setMaximumSize(new java.awt.Dimension(180, 20));
        lblInitialContractAdminValue.setMinimumSize(new java.awt.Dimension(4, 20));
        lblInitialContractAdminValue.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblInitialContractAdminValue, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblSponsor, gridBagConstraints);

        lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblSponsorValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblSponsorValue, gridBagConstraints);

        lblSponsorName.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorName.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblSponsorName.setMinimumSize(new java.awt.Dimension(180, 20));
        lblSponsorName.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblSponsorName, gridBagConstraints);

        lblSponsorType.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorType.setText("Sponsor Type :");
        lblSponsorType.setMaximumSize(new java.awt.Dimension(60, 16));
        lblSponsorType.setMinimumSize(new java.awt.Dimension(90, 20));
        lblSponsorType.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblSponsorType, gridBagConstraints);

        lblSponsorTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorTypeValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblSponsorTypeValue.setMinimumSize(new java.awt.Dimension(170, 20));
        lblSponsorTypeValue.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblSponsorTypeValue, gridBagConstraints);

        lblProposalType.setFont(CoeusFontFactory.getLabelFont());
        lblProposalType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalType.setText("Proposal Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblProposalType, gridBagConstraints);

        lblProposalTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblProposalTypeValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblProposalTypeValue.setMinimumSize(new java.awt.Dimension(180, 20));
        lblProposalTypeValue.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblProposalTypeValue, gridBagConstraints);

        lblLeadUnit.setFont(CoeusFontFactory.getLabelFont());
        lblLeadUnit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLeadUnit.setText("Lead Unit :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblLeadUnit, gridBagConstraints);

        lblLeadUnitValue.setFont(CoeusFontFactory.getNormalFont());
        lblLeadUnitValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblLeadUnitValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblLeadUnitValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblLeadUnitValue, gridBagConstraints);

        lblLeadUnitName.setFont(CoeusFontFactory.getNormalFont());
        lblLeadUnitName.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblLeadUnitName.setMinimumSize(new java.awt.Dimension(180, 20));
        lblLeadUnitName.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblLeadUnitName, gridBagConstraints);

        lblPI.setFont(CoeusFontFactory.getLabelFont());
        lblPI.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPI.setText("PI :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblPI, gridBagConstraints);

        lblPIValue.setFont(CoeusFontFactory.getNormalFont());
        lblPIValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(lblPIValue, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setText("Title :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblTitle, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setText("Start Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblStartDate, gridBagConstraints);

        lblStartDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblStartDateValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblStartDateValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblStartDateValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(lblStartDateValue, gridBagConstraints);

        lblInitialAmount.setFont(CoeusFontFactory.getLabelFont());
        lblInitialAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInitialAmount.setText("Initial Amount :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblInitialAmount, gridBagConstraints);

        lblPropCreateDate.setFont(CoeusFontFactory.getLabelFont());
        lblPropCreateDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPropCreateDate.setText("Prop Create Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        add(lblPropCreateDate, gridBagConstraints);

        lblPropCreateDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblPropCreateDateValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblPropCreateDateValue.setMinimumSize(new java.awt.Dimension(90, 20));
        lblPropCreateDateValue.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(lblPropCreateDateValue, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEndDate.setText("End Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblEndDate, gridBagConstraints);

        lblEndDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblEndDateValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblEndDateValue.setMinimumSize(new java.awt.Dimension(80, 20));
        lblEndDateValue.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblEndDateValue, gridBagConstraints);

        lblTotalAmount.setFont(CoeusFontFactory.getLabelFont());
        lblTotalAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalAmount.setText("Total Amount :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblTotalAmount, gridBagConstraints);

        lblLastModifiedDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastModifiedDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastModifiedDate.setText("Last Modified Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        add(lblLastModifiedDate, gridBagConstraints);

        lblLastModifiedDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblLastModifiedDateValue.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(lblLastModifiedDateValue, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMinimumSize(new java.awt.Dimension(70, 26));
        btnOK.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 4);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(70, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 4);
        add(btnCancel, gridBagConstraints);

        txtInitialAmount.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtInitialAmount.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(txtInitialAmount, gridBagConstraints);

        txtTotalAmount.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalAmount.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(txtTotalAmount, gridBagConstraints);

        scrpnTitle.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrpnTitle.setMinimumSize(new java.awt.Dimension(500, 25));
        scrpnTitle.setPreferredSize(new java.awt.Dimension(500, 20));
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setOpaque(false);
        scrpnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scrpnTitle, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JLabel lblAccount;
    public javax.swing.JLabel lblAccountValue;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblEndDateValue;
    public javax.swing.JLabel lblInitialAmount;
    public javax.swing.JLabel lblInitialContractAdmin;
    public javax.swing.JLabel lblInitialContractAdminValue;
    public javax.swing.JLabel lblLastModifiedDate;
    public javax.swing.JLabel lblLastModifiedDateValue;
    public javax.swing.JLabel lblLeadUnit;
    public javax.swing.JLabel lblLeadUnitName;
    public javax.swing.JLabel lblLeadUnitValue;
    public javax.swing.JLabel lblPI;
    public javax.swing.JLabel lblPIValue;
    public javax.swing.JLabel lblPropCreateDate;
    public javax.swing.JLabel lblPropCreateDateValue;
    public javax.swing.JLabel lblProposalNo;
    public javax.swing.JLabel lblProposalNoValue;
    public javax.swing.JLabel lblProposalType;
    public javax.swing.JLabel lblProposalTypeValue;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorName;
    public javax.swing.JLabel lblSponsorType;
    public javax.swing.JLabel lblSponsorTypeValue;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JLabel lblStartDateValue;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JLabel lblStatusValue;
    public javax.swing.JLabel lblTitle;
    public javax.swing.JLabel lblTotalAmount;
    public javax.swing.JScrollPane scrpnTitle;
    public javax.swing.JTextArea txtArTitle;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtInitialAmount;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalAmount;
    // End of variables declaration//GEN-END:variables
    
}
