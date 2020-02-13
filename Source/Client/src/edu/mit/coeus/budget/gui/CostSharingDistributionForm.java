/*
 * CostSharingDistributionnewForm.java
 *
 * Created on December 4, 2003, 11:19 AM
 */

package edu.mit.coeus.budget.gui;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class CostSharingDistributionForm extends javax.swing.JPanel {
    
    /** CoeusDlgWindow instance */
    public CoeusDlgWindow dlgCostSharingDistributionForm;
    /** variable for modal */
    boolean modal = true;
    /** ParentForm instance of this Dialog */
    Component parentForm;
    /** String for title */
    private String title = "";
    
    
    /** Creates new form CostSharingDistributionnewForm */
    public CostSharingDistributionForm() {
        this.parentForm = CoeusGuiConstants.getMDIForm();
        this.modal = true;
        initComponents();
        postInitComponent();
    }
    
    /** Constructor BudgetJustificationForm
     * @param parent Component parent form
     * @param modal boolean if <CODE>true<CODE> Modal window
     */
    public CostSharingDistributionForm(Component parent, boolean modal) {
        this.parentForm = parent;
        this.modal = modal;
        initComponents();
        postInitComponent();
    }
    
    /** post intialisation of CoeusDialog */
    public void postInitComponent(){
        
        dlgCostSharingDistributionForm = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),title,modal);
        dlgCostSharingDistributionForm.getContentPane().add(this);
        dlgCostSharingDistributionForm.pack();
        dlgCostSharingDistributionForm.setResizable(false);
        dlgCostSharingDistributionForm.setFont(CoeusFontFactory.getLabelFont());
        dlgCostSharingDistributionForm.setVisible(false);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        dlgCostSharingDistributionForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }
    
    
    /** adds the panel of rows for the Beans containing the
     * Period and Total Cost Sharing values
     * @param vecBudgetPeriodBeans CoeusVector of BudgetPeriodBeans
     * @return JPanel instance containing Panel Rows
     */
    public JPanel addPeriodRows(CoeusVector vecBudgetPeriodBeans) {
        
        for(int loopIndex = 0 ;loopIndex < vecBudgetPeriodBeans.size();loopIndex++) {
            
            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean) vecBudgetPeriodBeans.get(loopIndex);
            
            JPanel pnlEachPeriodPanel = new JPanel();
            JLabel lblPeriod = new JLabel();
            JLabel lblblank = new JLabel();
            JTextField txtPeriod = new JTextField();
            JLabel lblTotalCostSharing = new JLabel();
            DollarCurrencyTextField txtTotalCostSharing = new DollarCurrencyTextField();
            
            pnlEachPeriodPanel.setLayout(new java.awt.GridBagLayout());
            pnlEachPeriodPanel.setMaximumSize(new java.awt.Dimension(420, 25));
            pnlEachPeriodPanel.setMinimumSize(new java.awt.Dimension(220,25 ));
            pnlEachPeriodPanel.setPreferredSize(new java.awt.Dimension(220,25));
            
            lblPeriod.setFont(CoeusFontFactory.getLabelFont());
            lblPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            lblPeriod.setText("Period:  ");
            lblPeriod.setMaximumSize(new java.awt.Dimension(63, 20));
            lblPeriod.setMinimumSize(new java.awt.Dimension(63, 20));
            lblPeriod.setPreferredSize(new java.awt.Dimension(63, 20));
            GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            pnlEachPeriodPanel.add(lblPeriod, gridBagConstraints);
            
            txtPeriod.setEditable(false);
            txtPeriod.setFont(CoeusFontFactory.getNormalFont());
            txtPeriod.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
            txtPeriod.setMinimumSize(new java.awt.Dimension(50, 17)); //20
            txtPeriod.setPreferredSize(new java.awt.Dimension(50, 17));
            txtPeriod.setHorizontalAlignment(JTextField.RIGHT);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            pnlEachPeriodPanel.add(txtPeriod, gridBagConstraints);
            
            lblTotalCostSharing.setFont(CoeusFontFactory.getLabelFont());
            lblTotalCostSharing.setText("Total Cost Sharing:");
            lblTotalCostSharing.setMaximumSize(new java.awt.Dimension(114, 20));
            lblTotalCostSharing.setMinimumSize(new java.awt.Dimension(114, 20));
            lblTotalCostSharing.setPreferredSize(new java.awt.Dimension(114, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            pnlEachPeriodPanel.add(lblTotalCostSharing, gridBagConstraints);
            
            txtTotalCostSharing.setEditable(false);
            txtTotalCostSharing.setFont(CoeusFontFactory.getNormalFont());
            txtTotalCostSharing.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
            txtTotalCostSharing.setMinimumSize(new java.awt.Dimension(130, 17)); //20
            txtTotalCostSharing.setPreferredSize(new java.awt.Dimension(130, 17));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 0.1;
            gridBagConstraints.weighty = 1.0;
            pnlEachPeriodPanel.add(txtTotalCostSharing, gridBagConstraints);
            
            lblblank.setMaximumSize(new java.awt.Dimension(18, 20));
            lblblank.setMinimumSize(new java.awt.Dimension(18, 20));
            lblblank.setPreferredSize(new java.awt.Dimension(18, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            pnlEachPeriodPanel.add(lblblank, gridBagConstraints);
            
            txtPeriod.setText(budgetPeriodBean.getBudgetPeriod()+"");
            txtTotalCostSharing.setText(budgetPeriodBean.getCostSharingAmount()+"");
            
            pnlPeriodPanel.add(pnlEachPeriodPanel);
            
        }
        return pnlPeriodPanel;
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCostSharing = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        lblVersion = new javax.swing.JLabel();
        txtVersion = new javax.swing.JTextField();
        lblTotalCostSharing = new javax.swing.JLabel();
        txtTotalCostSharing = new edu.mit.coeus.utils.DollarCurrencyTextField();
        scrPnPeriod = new javax.swing.JScrollPane();
        pnlPeriodPanel = new javax.swing.JPanel();
        lblCostSharingDistributionList = new javax.swing.JLabel();
        scrPnCostSharingDistribution = new javax.swing.JScrollPane();
        tblCostSharingDistribution = new edu.mit.coeus.budget.gui.CostSharingDistributionTable();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(510, 350));
        setPreferredSize(new java.awt.Dimension(510, 330));
        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMaximumSize(new java.awt.Dimension(100, 175));
        pnlButtons.setMinimumSize(new java.awt.Dimension(100, 175));
        pnlButtons.setPreferredSize(new java.awt.Dimension(110, 175));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(90, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(90, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 6);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(90, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(90, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 6);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(90, 25));
        btnAdd.setMinimumSize(new java.awt.Dimension(90, 25));
        btnAdd.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 6);
        pnlButtons.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(90, 25));
        btnDelete.setMinimumSize(new java.awt.Dimension(90, 25));
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 6);
        pnlButtons.add(btnDelete, gridBagConstraints);

        btnCostSharing.setFont(CoeusFontFactory.getLabelFont());
        btnCostSharing.setMnemonic('O');
        btnCostSharing.setText("Subaward");
        btnCostSharing.setMaximumSize(new java.awt.Dimension(90, 25));
        btnCostSharing.setMinimumSize(new java.awt.Dimension(90, 25));
        btnCostSharing.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 6);
        pnlButtons.add(btnCostSharing, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(pnlButtons, gridBagConstraints);

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMaximumSize(new java.awt.Dimension(400, 330));
        pnlMain.setMinimumSize(new java.awt.Dimension(400, 330));
        pnlMain.setPreferredSize(new java.awt.Dimension(400, 330));
        lblVersion.setFont(CoeusFontFactory.getLabelFont());
        lblVersion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVersion.setText("Version: ");
        lblVersion.setMaximumSize(new java.awt.Dimension(64, 20));
        lblVersion.setMinimumSize(new java.awt.Dimension(64, 20));
        lblVersion.setPreferredSize(new java.awt.Dimension(64, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlMain.add(lblVersion, gridBagConstraints);

        txtVersion.setEditable(false);
        txtVersion.setFont(CoeusFontFactory.getNormalFont());
        txtVersion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtVersion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtVersion.setMaximumSize(new java.awt.Dimension(50, 20));
        txtVersion.setMinimumSize(new java.awt.Dimension(50, 20));
        txtVersion.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlMain.add(txtVersion, gridBagConstraints);

        lblTotalCostSharing.setFont(CoeusFontFactory.getLabelFont());
        lblTotalCostSharing.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalCostSharing.setText("Total Cost Sharing:  ");
        lblTotalCostSharing.setMaximumSize(new java.awt.Dimension(132, 20));
        lblTotalCostSharing.setMinimumSize(new java.awt.Dimension(132, 20));
        lblTotalCostSharing.setPreferredSize(new java.awt.Dimension(132, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlMain.add(lblTotalCostSharing, gridBagConstraints);

        txtTotalCostSharing.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalCostSharing.setEditable(false);
        txtTotalCostSharing.setMaximumSize(new java.awt.Dimension(130, 20));
        txtTotalCostSharing.setMinimumSize(new java.awt.Dimension(130, 20));
        txtTotalCostSharing.setPreferredSize(new java.awt.Dimension(130, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlMain.add(txtTotalCostSharing, gridBagConstraints);

        scrPnPeriod.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrPnPeriod.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnPeriod.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        scrPnPeriod.setMinimumSize(new java.awt.Dimension(220, 100));
        scrPnPeriod.setPreferredSize(new java.awt.Dimension(220, 100));
        pnlPeriodPanel.setLayout(new javax.swing.BoxLayout(pnlPeriodPanel, javax.swing.BoxLayout.Y_AXIS));

        scrPnPeriod.setViewportView(pnlPeriodPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(scrPnPeriod, gridBagConstraints);

        lblCostSharingDistributionList.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharingDistributionList.setText("Cost Sharing Distribution List:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblCostSharingDistributionList, gridBagConstraints);

        scrPnCostSharingDistribution.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnCostSharingDistribution.setMinimumSize(new java.awt.Dimension(250, 125));
        scrPnCostSharingDistribution.setPreferredSize(new java.awt.Dimension(250, 125));
        scrPnCostSharingDistribution.setViewportView(tblCostSharingDistribution);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 0, 0);
        pnlMain.add(scrPnCostSharingDistribution, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(pnlMain, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnCostSharing;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblCostSharingDistributionList;
    public javax.swing.JLabel lblTotalCostSharing;
    public javax.swing.JLabel lblVersion;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JPanel pnlPeriodPanel;
    public javax.swing.JScrollPane scrPnCostSharingDistribution;
    public javax.swing.JScrollPane scrPnPeriod;
    public edu.mit.coeus.budget.gui.CostSharingDistributionTable tblCostSharingDistribution;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalCostSharing;
    public javax.swing.JTextField txtVersion;
    // End of variables declaration//GEN-END:variables
    
}
