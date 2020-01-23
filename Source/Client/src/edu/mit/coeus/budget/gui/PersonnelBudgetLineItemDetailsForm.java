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

// JM 3-11-2013 needed for custom features
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
// JM END
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.*;

/** PersonnelBudgetLineItemDetailsForm.java
 * @author  Vyjayanthi
 * Created on October 22, 2003, 4:50 PM
 */
public class PersonnelBudgetLineItemDetailsForm extends javax.swing.JComponent {
    
    public CoeusDlgWindow dlgPersonnelBudgetLineItem;
    private Frame parent;
    private boolean modal;
    
    /** Creates new form PersonnelBudgetLineItemDetailsForm
     * @param parent takes the frame
     * @param modal true if form is modal, false otherwise
     */
    public PersonnelBudgetLineItemDetailsForm( Frame parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initComponents();
        postInitComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form. */
    public void postInitComponents(){
        dlgPersonnelBudgetLineItem= new CoeusDlgWindow(parent,modal);
        dlgPersonnelBudgetLineItem.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgPersonnelBudgetLineItem.getContentPane().add(this);
        dlgPersonnelBudgetLineItem.pack();
        dlgPersonnelBudgetLineItem.setResizable(false);
        dlgPersonnelBudgetLineItem.setTitle("Personnel Budget - Line Item Detail");
        dlgPersonnelBudgetLineItem.setFont(CoeusFontFactory.getLabelFont());
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgPersonnelBudgetLineItem.getSize();
        dlgPersonnelBudgetLineItem.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnlLineItemCalAmtsTable = new edu.mit.coeus.budget.utils.LineItemCalculatedAmountsTable();
        pnlValues = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblJobCode = new javax.swing.JLabel();
        txtJobCode = new javax.swing.JTextField();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblPeriod = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblCharged = new javax.swing.JLabel();
        lblCostSharingPercent = new javax.swing.JLabel();
        lblReqSalary = new javax.swing.JLabel();
        txtReqSalary = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblCostSharing = new javax.swing.JLabel();
        txtCostSharing = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblEffort = new javax.swing.JLabel();
        txtUnderRecovery = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblRecovery = new javax.swing.JLabel();
        txtPercentCharged = new edu.mit.coeus.utils.CurrencyField();
        txtCostSharingPercent = new edu.mit.coeus.utils.CurrencyField();
        cmbPeriod = new javax.swing.JComboBox();
        txtPercentEffort = new edu.mit.coeus.utils.CurrencyField();
        scrPnDescription = new javax.swing.JScrollPane();
        txtArDescription = new javax.swing.JTextArea();
        txtStartDate = new edu.mit.coeus.utils.CoeusTextField();
        txtEndDate = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setLabel("OK");
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        btnOk.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 5, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 5, 0);
        add(btnCancel, gridBagConstraints);

        pnlLineItemCalAmtsTable.setBorder(new javax.swing.border.EtchedBorder());
        pnlLineItemCalAmtsTable.setMinimumSize(new java.awt.Dimension(550, 100));
        pnlLineItemCalAmtsTable.setPreferredSize(new java.awt.Dimension(550, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(pnlLineItemCalAmtsTable, gridBagConstraints);

        pnlValues.setLayout(new java.awt.GridBagLayout());

        pnlValues.setBorder(new javax.swing.border.EtchedBorder());
        pnlValues.setMinimumSize(new java.awt.Dimension(550, 190));
        pnlValues.setPreferredSize(new java.awt.Dimension(550, 190));
        lblName.setFont(CoeusFontFactory.getLabelFont());
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblName.setText("Name: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 0);
        pnlValues.add(lblName, gridBagConstraints);

        txtName.setEditable(false);
        txtName.setFont(CoeusFontFactory.getNormalFont());
        txtName.setMinimumSize(new java.awt.Dimension(60, 22));
        txtName.setPreferredSize(new java.awt.Dimension(60, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 3);
        pnlValues.add(txtName, gridBagConstraints);

        lblJobCode.setFont(CoeusFontFactory.getLabelFont());
        lblJobCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblJobCode.setText("Job Code: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(lblJobCode, gridBagConstraints);

        txtJobCode.setEditable(false);
        txtJobCode.setFont(CoeusFontFactory.getNormalFont());
        txtJobCode.setMinimumSize(new java.awt.Dimension(91, 22));
        txtJobCode.setPreferredSize(new java.awt.Dimension(91, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtJobCode, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setText("Start Date: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(lblStartDate, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEndDate.setText("End Date: ");
        lblEndDate.setMinimumSize(new java.awt.Dimension(75, 16));
        lblEndDate.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblEndDate, gridBagConstraints);

        lblPeriod.setFont(CoeusFontFactory.getLabelFont());
        lblPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPeriod.setText("Period: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 2, 0);
        pnlValues.add(lblPeriod, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDescription.setText("Description: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(lblDescription, gridBagConstraints);

        lblCharged.setFont(CoeusFontFactory.getLabelFont());
        lblCharged.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCharged.setText("% Charged: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(lblCharged, gridBagConstraints);

        lblCostSharingPercent.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharingPercent.setText("Cost Sharing %: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlValues.add(lblCostSharingPercent, gridBagConstraints);

        lblReqSalary.setFont(CoeusFontFactory.getLabelFont());
        lblReqSalary.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReqSalary.setText("Req. Salary: ");
        lblReqSalary.setMinimumSize(new java.awt.Dimension(75, 16));
        lblReqSalary.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlValues.add(lblReqSalary, gridBagConstraints);

        txtReqSalary.setEditable(false);
        txtReqSalary.setFont(CoeusFontFactory.getNormalFont());
        txtReqSalary.setMinimumSize(new java.awt.Dimension(160, 22));
        txtReqSalary.setPreferredSize(new java.awt.Dimension(160, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtReqSalary, gridBagConstraints);

        lblCostSharing.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharing.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCostSharing.setText("Cost Sharing: ");
        lblCostSharing.setMinimumSize(new java.awt.Dimension(75, 16));
        lblCostSharing.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlValues.add(lblCostSharing, gridBagConstraints);

        txtCostSharing.setEditable(false);
        txtCostSharing.setFont(CoeusFontFactory.getNormalFont());
        txtCostSharing.setMinimumSize(new java.awt.Dimension(160, 22));
        txtCostSharing.setPreferredSize(new java.awt.Dimension(160, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 3);
        pnlValues.add(txtCostSharing, gridBagConstraints);

        lblEffort.setFont(CoeusFontFactory.getLabelFont());
        lblEffort.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffort.setText("% Effort: ");
        lblEffort.setMinimumSize(new java.awt.Dimension(75, 16));
        lblEffort.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.weighty = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlValues.add(lblEffort, gridBagConstraints);

        txtUnderRecovery.setEditable(false);
        txtUnderRecovery.setFont(CoeusFontFactory.getNormalFont());
        txtUnderRecovery.setMinimumSize(new java.awt.Dimension(160, 22));
        txtUnderRecovery.setPreferredSize(new java.awt.Dimension(160, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 3);
        pnlValues.add(txtUnderRecovery, gridBagConstraints);

        lblRecovery.setFont(CoeusFontFactory.getLabelFont());
        lblRecovery.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRecovery.setText("Underrecovery: ");
        lblRecovery.setMinimumSize(new java.awt.Dimension(75, 16));
        lblRecovery.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlValues.add(lblRecovery, gridBagConstraints);

        txtPercentCharged.setFont(CoeusFontFactory.getNormalFont());
        txtPercentCharged.setMinimumSize(new java.awt.Dimension(70, 22));
        txtPercentCharged.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtPercentCharged, gridBagConstraints);

        txtCostSharingPercent.setFont(CoeusFontFactory.getNormalFont());
        txtCostSharingPercent.setMinimumSize(new java.awt.Dimension(70, 22));
        txtCostSharingPercent.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtCostSharingPercent, gridBagConstraints);

        cmbPeriod.setBackground(new java.awt.Color(255, 255, 255));
        cmbPeriod.setFont(CoeusFontFactory.getNormalFont());
        // JM 11-13-2012 want to get list from database for active types only
        //cmbPeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Academic", "Calendar", "Cycle", "Summer" }));
        PersonnelBudgetDetailTable controller = new PersonnelBudgetDetailTable();
        String[][] activePeriodTypesArr = {{"",""},{"",""}};
		try {
			activePeriodTypesArr = controller.fetchActivePeriodTypes();
			for (int f=0; f<activePeriodTypesArr.length; f++) {
				cmbPeriod.addItem(activePeriodTypesArr[f][1]);
			}
		} catch (CoeusClientException e) {
			System.out.println("Client exception: Cannot get active period types");
			//e.printStackTrace();
		} catch (CoeusException e) {
			System.out.println("Cannot get active period types");
			//e.printStackTrace();
		}
        // JM END
        cmbPeriod.setMinimumSize(new java.awt.Dimension(91, 22));
        cmbPeriod.setPreferredSize(new java.awt.Dimension(91, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 3);
        pnlValues.add(cmbPeriod, gridBagConstraints);

        txtPercentEffort.setFont(CoeusFontFactory.getNormalFont());
        txtPercentEffort.setMinimumSize(new java.awt.Dimension(70, 22));
        txtPercentEffort.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtPercentEffort, gridBagConstraints);

        scrPnDescription.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnDescription.setMinimumSize(new java.awt.Dimension(200, 35));
        scrPnDescription.setPreferredSize(new java.awt.Dimension(200, 35));
        txtArDescription.setDocument(new LimitedPlainDocument(80));
        txtArDescription.setFont(CoeusFontFactory.getNormalFont());
        txtArDescription.setLineWrap(true);
        txtArDescription.setWrapStyleWord(true);
        txtArDescription.setBorder(null);
        scrPnDescription.setViewportView(txtArDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(scrPnDescription, gridBagConstraints);

        txtStartDate.setMinimumSize(new java.awt.Dimension(91, 22));
        txtStartDate.setPreferredSize(new java.awt.Dimension(91, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtStartDate, gridBagConstraints);

        txtEndDate.setMinimumSize(new java.awt.Dimension(91, 22));
        txtEndDate.setPreferredSize(new java.awt.Dimension(91, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        pnlValues.add(txtEndDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        add(pnlValues, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JComboBox cmbPeriod;
    public javax.swing.JLabel lblCharged;
    public javax.swing.JLabel lblCostSharing;
    public javax.swing.JLabel lblCostSharingPercent;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblEffort;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblJobCode;
    public javax.swing.JLabel lblName;
    public javax.swing.JLabel lblPeriod;
    public javax.swing.JLabel lblRecovery;
    public javax.swing.JLabel lblReqSalary;
    public javax.swing.JLabel lblStartDate;
    public edu.mit.coeus.budget.utils.LineItemCalculatedAmountsTable pnlLineItemCalAmtsTable;
    public javax.swing.JPanel pnlValues;
    public javax.swing.JScrollPane scrPnDescription;
    public javax.swing.JTextArea txtArDescription;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtCostSharing;
    public edu.mit.coeus.utils.CurrencyField txtCostSharingPercent;
    public edu.mit.coeus.utils.CoeusTextField txtEndDate;
    public javax.swing.JTextField txtJobCode;
    public javax.swing.JTextField txtName;
    public edu.mit.coeus.utils.CurrencyField txtPercentCharged;
    public edu.mit.coeus.utils.CurrencyField txtPercentEffort;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtReqSalary;
    public edu.mit.coeus.utils.CoeusTextField txtStartDate;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtUnderRecovery;
    // End of variables declaration//GEN-END:variables
    
}
