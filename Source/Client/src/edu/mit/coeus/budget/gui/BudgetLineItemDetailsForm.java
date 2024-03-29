/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.*;

/** BudgetLineItemDetailsForm.java
 * @author  mukund
 * Created on 5 September 2003, 15:04
 */
public class BudgetLineItemDetailsForm extends javax.swing.JComponent{
    public CoeusDlgWindow dlgBudgetLineItem;
    private Frame parent;
    private boolean modal;

    /** Creates new form BudgetLineItemDetailsForm
     * @param parent takes the frame
     * @param modal true if form is modal, false otherwise
     */
    public BudgetLineItemDetailsForm(Frame parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initComponents();
        txtArJustification.setDocument(new LimitedPlainDocument(3878));
        postInitComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form. */
    public void postInitComponents(){
        dlgBudgetLineItem = new CoeusDlgWindow(parent,modal);
        dlgBudgetLineItem.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgBudgetLineItem.getContentPane().add(this);
        dlgBudgetLineItem.pack();
        dlgBudgetLineItem.setResizable(false);
        dlgBudgetLineItem.setTitle("Budget - Line Item Detail");
        dlgBudgetLineItem.setFont(CoeusFontFactory.getLabelFont());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgBudgetLineItem.getSize();
        dlgBudgetLineItem.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdBtnGroup = new javax.swing.ButtonGroup();
        pnlValues = new javax.swing.JPanel();
        lblCategory = new javax.swing.JLabel();
        rdBtnOnCampus = new javax.swing.JRadioButton();
        rdBtnOffCampus = new javax.swing.JRadioButton();
        lblCostElm = new javax.swing.JLabel();
        txtCostElm = new javax.swing.JTextField();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblCost = new javax.swing.JLabel();
        lblQuantity = new javax.swing.JLabel();
        lblInflation = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        chkInflation = new javax.swing.JCheckBox();
        scrpnDesc = new javax.swing.JScrollPane();
        txtArDescription = new javax.swing.JTextArea();
        lblCostSharing = new javax.swing.JLabel();
        lblRecovery = new javax.swing.JLabel();
        txtCost = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtCostSharing = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtUnderRecovery = new edu.mit.coeus.utils.DollarCurrencyTextField();
        cmbCategory = new edu.mit.coeus.utils.CoeusComboBox();
        lblCostElementDescription = new javax.swing.JLabel();
        txtStartDate = new edu.mit.coeus.utils.CoeusTextField();
        txtEndDate = new edu.mit.coeus.utils.CoeusTextField();
        chkSubmitCostSharing = new javax.swing.JCheckBox();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnJustify = new javax.swing.JButton();
        pnlJustify = new javax.swing.JPanel();
        lblJustification = new javax.swing.JLabel();
        scrPnJustification = new javax.swing.JScrollPane();
        txtArJustification = new javax.swing.JTextArea();
        pnlLineItemCalAmtsTable = new edu.mit.coeus.budget.utils.LineItemCalculatedAmountsTable();

        setMinimumSize(new java.awt.Dimension(670, 445));
        setPreferredSize(new java.awt.Dimension(670, 445));
        setLayout(new java.awt.GridBagLayout());

        pnlValues.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlValues.setMinimumSize(new java.awt.Dimension(630, 175));
        pnlValues.setPreferredSize(new java.awt.Dimension(630, 175));
        pnlValues.setLayout(new java.awt.GridBagLayout());

        lblCategory.setFont(CoeusFontFactory.getLabelFont());
        lblCategory.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCategory.setText("Category: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblCategory, gridBagConstraints);

        rdBtnGroup.add(rdBtnOnCampus);
        rdBtnOnCampus.setFont(CoeusFontFactory.getNormalFont());
        rdBtnOnCampus.setSelected(true);
        rdBtnOnCampus.setText("On Campus");
        rdBtnOnCampus.setMaximumSize(new java.awt.Dimension(80, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 2, 0);
        pnlValues.add(rdBtnOnCampus, gridBagConstraints);

        rdBtnGroup.add(rdBtnOffCampus);
        rdBtnOffCampus.setFont(CoeusFontFactory.getNormalFont());
        rdBtnOffCampus.setText("Off Campus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 6);
        pnlValues.add(rdBtnOffCampus, gridBagConstraints);

        lblCostElm.setFont(CoeusFontFactory.getLabelFont());
        lblCostElm.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCostElm.setText("Cost Elm: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblCostElm, gridBagConstraints);

        txtCostElm.setEditable(false);
        txtCostElm.setFont(CoeusFontFactory.getNormalFont());
        txtCostElm.setMinimumSize(new java.awt.Dimension(60, 22));
        txtCostElm.setPreferredSize(new java.awt.Dimension(60, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 4, 0);
        pnlValues.add(txtCostElm, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setText("Start Date: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblStartDate, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEndDate.setText("End Date: ");
        lblEndDate.setMinimumSize(new java.awt.Dimension(75, 16));
        lblEndDate.setPreferredSize(new java.awt.Dimension(85, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblEndDate, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDescription.setText("Description: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblDescription, gridBagConstraints);

        lblCost.setFont(CoeusFontFactory.getLabelFont());
        lblCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCost.setText("Cost: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblCost, gridBagConstraints);

        lblQuantity.setFont(CoeusFontFactory.getLabelFont());
        lblQuantity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblQuantity.setText("Quantity: ");
        lblQuantity.setMinimumSize(new java.awt.Dimension(75, 16));
        lblQuantity.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.weighty = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblQuantity, gridBagConstraints);

        lblInflation.setFont(CoeusFontFactory.getLabelFont());
        lblInflation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInflation.setText(" Apply Inflation: ");
        lblInflation.setMaximumSize(new java.awt.Dimension(85, 16));
        lblInflation.setMinimumSize(new java.awt.Dimension(85, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 20);
        pnlValues.add(lblInflation, gridBagConstraints);

        txtQuantity.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        txtQuantity.setFont(CoeusFontFactory.getNormalFont());
        txtQuantity.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQuantity.setMinimumSize(new java.awt.Dimension(90, 22));
        txtQuantity.setOpaque(false);
        txtQuantity.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtQuantity, gridBagConstraints);

        chkInflation.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkInflation.setMinimumSize(new java.awt.Dimension(15, 17));
        chkInflation.setPreferredSize(new java.awt.Dimension(15, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 6);
        pnlValues.add(chkInflation, gridBagConstraints);

        scrpnDesc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrpnDesc.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrpnDesc.setMinimumSize(new java.awt.Dimension(200, 45));
        scrpnDesc.setPreferredSize(new java.awt.Dimension(200, 45));

        txtArDescription.setDocument(new LimitedPlainDocument(80));
        txtArDescription.setFont(CoeusFontFactory.getNormalFont());
        txtArDescription.setLineWrap(true);
        txtArDescription.setWrapStyleWord(true);
        txtArDescription.setBorder(null);
        scrpnDesc.setViewportView(txtArDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 6);
        pnlValues.add(scrpnDesc, gridBagConstraints);

        lblCostSharing.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharing.setText("Cost Sharing: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlValues.add(lblCostSharing, gridBagConstraints);

        lblRecovery.setFont(CoeusFontFactory.getLabelFont());
        lblRecovery.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRecovery.setText("Underrecovery: ");
        lblRecovery.setMinimumSize(new java.awt.Dimension(75, 16));
        lblRecovery.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlValues.add(lblRecovery, gridBagConstraints);

        txtCost.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtCost, gridBagConstraints);

        txtCostSharing.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlValues.add(txtCostSharing, gridBagConstraints);

        txtUnderRecovery.setEditable(false);
        txtUnderRecovery.setFont(CoeusFontFactory.getNormalFont());
        txtUnderRecovery.setMinimumSize(new java.awt.Dimension(120, 22));
        txtUnderRecovery.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        pnlValues.add(txtUnderRecovery, gridBagConstraints);

        cmbCategory.setMaximumSize(new java.awt.Dimension(300, 20));
        cmbCategory.setMinimumSize(new java.awt.Dimension(275, 20));
        cmbCategory.setPreferredSize(new java.awt.Dimension(275, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(cmbCategory, gridBagConstraints);

        lblCostElementDescription.setFont(CoeusFontFactory.getNormalFont());
        lblCostElementDescription.setText("Cost Element Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 65, 2, 6);
        pnlValues.add(lblCostElementDescription, gridBagConstraints);

        txtStartDate.setMinimumSize(new java.awt.Dimension(120, 22));
        txtStartDate.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 4, 0);
        pnlValues.add(txtStartDate, gridBagConstraints);

        txtEndDate.setMinimumSize(new java.awt.Dimension(120, 22));
        txtEndDate.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 4, 6);
        pnlValues.add(txtEndDate, gridBagConstraints);

        chkSubmitCostSharing.setFont(CoeusFontFactory.getLabelFont());
        chkSubmitCostSharing.setText("Submit Cost Sharing:");
        chkSubmitCostSharing.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkSubmitCostSharing.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkSubmitCostSharing.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        pnlValues.add(chkSubmitCostSharing, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(pnlValues, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setEnabled(false);
        btnOk.setLabel("OK");
        btnOk.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 5, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 5, 0);
        add(btnCancel, gridBagConstraints);

        btnJustify.setFont(CoeusFontFactory.getLabelFont());
        btnJustify.setMnemonic('J');
        btnJustify.setText("Justify");
        btnJustify.setEnabled(false);
        btnJustify.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        add(btnJustify, gridBagConstraints);

        pnlJustify.setLayout(new java.awt.GridBagLayout());

        lblJustification.setFont(CoeusFontFactory.getLabelFont());
        lblJustification.setForeground(new java.awt.Color(255, 0, 0));
        lblJustification.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblJustification.setText("Justification");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlJustify.add(lblJustification, gridBagConstraints);

        scrPnJustification.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnJustification.setMinimumSize(new java.awt.Dimension(550, 140));
        scrPnJustification.setPreferredSize(new java.awt.Dimension(550, 140));

        txtArJustification.setFont(CoeusFontFactory.getNormalFont());
        txtArJustification.setLineWrap(true);
        txtArJustification.setWrapStyleWord(true);
        txtArJustification.setMinimumSize(new java.awt.Dimension(650, 18));
        txtArJustification.setPreferredSize(new java.awt.Dimension(650, 18));
        scrPnJustification.setViewportView(txtArJustification);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        pnlJustify.add(scrPnJustification, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(pnlJustify, gridBagConstraints);

        pnlLineItemCalAmtsTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlLineItemCalAmtsTable.setMinimumSize(new java.awt.Dimension(500, 100));
        pnlLineItemCalAmtsTable.setPreferredSize(new java.awt.Dimension(500, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(pnlLineItemCalAmtsTable, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnJustify;
    public javax.swing.JButton btnOk;
    public javax.swing.JCheckBox chkInflation;
    public javax.swing.JCheckBox chkSubmitCostSharing;
    public edu.mit.coeus.utils.CoeusComboBox cmbCategory;
    public javax.swing.JLabel lblCategory;
    public javax.swing.JLabel lblCost;
    public javax.swing.JLabel lblCostElementDescription;
    public javax.swing.JLabel lblCostElm;
    public javax.swing.JLabel lblCostSharing;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblInflation;
    public javax.swing.JLabel lblJustification;
    public javax.swing.JLabel lblQuantity;
    public javax.swing.JLabel lblRecovery;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JPanel pnlJustify;
    public edu.mit.coeus.budget.utils.LineItemCalculatedAmountsTable pnlLineItemCalAmtsTable;
    public javax.swing.JPanel pnlValues;
    public javax.swing.ButtonGroup rdBtnGroup;
    public javax.swing.JRadioButton rdBtnOffCampus;
    public javax.swing.JRadioButton rdBtnOnCampus;
    public javax.swing.JScrollPane scrPnJustification;
    public javax.swing.JScrollPane scrpnDesc;
    public javax.swing.JTextArea txtArDescription;
    public javax.swing.JTextArea txtArJustification;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtCost;
    public javax.swing.JTextField txtCostElm;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtCostSharing;
    public edu.mit.coeus.utils.CoeusTextField txtEndDate;
    public javax.swing.JTextField txtQuantity;
    public edu.mit.coeus.utils.CoeusTextField txtStartDate;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtUnderRecovery;
    // End of variables declaration//GEN-END:variables

}
