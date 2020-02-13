/*
 * ProposalBudgetHierarchyDetailsForm.java
 *
 * Created on August 25, 2005, 3:18 PM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.controller.BudgetTotalController;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.propdev.gui.PropHierarchyShowLegendForm;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Dimension;
import java.util.Vector;

/**
 *
 * @author  vinayks
 */
public class ProposalBudgetHierarchyDetailsForm extends javax.swing.JComponent {
    
    private  BudgetTotalController budgetTotalController =null;
    
    private DateUtils dtUtils = new DateUtils();
    private QueryEngine queryEngine = QueryEngine.getInstance();
    //Added by tarique to show budget Status start
    private String budgetStatus;
    //Added by tarique to show budget Status end
    
    /** Creates new form ProposalBudgetHierarchyDetailsForm */
    public ProposalBudgetHierarchyDetailsForm() {  
        initComponents();
        postInitComponents();
    }
    
    public void postInitComponents(){   
        if(budgetTotalController == null){
            budgetTotalController = new BudgetTotalController();
            pnlTotal.setMaximumSize(new Dimension(0,380));
            pnlTotal.setMinimumSize(new Dimension(0,380));
            pnlTotal.setPreferredSize(new Dimension(0,380));
            pnlTotal.add(budgetTotalController.getControlledUI());
        }
    }
    
    public void setFormData(java.util.HashMap hmBudgetData){
        // Set the summary data
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean) hmBudgetData.get(BudgetInfoBean.class);
        lblProposalNoValue.setText(budgetInfoBean.getProposalNumber());
        lblVersionNoValue.setText(""+budgetInfoBean.getVersionNumber());
        lblStartDateValue.setText(formatDate(budgetInfoBean.getStartDate()));
        //lblBudgetStatusValue.setText(budgetInfoBean.get
        lblEndDateValue.setText(formatDate(budgetInfoBean.getEndDate()));
        txtTotalCost.setText(""+budgetInfoBean.getTotalCost());
        txtTotalDirectCost.setText(""+budgetInfoBean.getTotalDirectCost());
        txtUnderRecovery.setText(""+budgetInfoBean.getUnderRecoveryAmount());
        txtTotalCostLimit.setText(""+budgetInfoBean.getTotalCostLimit());
        txtTotalIndirectCost.setText(""+budgetInfoBean.getTotalIndirectCost());
        txtCostSharing.setText(""+budgetInfoBean.getCostSharingAmount());
        lblOhRateTypeValue.setText(budgetInfoBean.getOhRateClassDescription());
        chkFinal.setSelected(budgetInfoBean.isFinalVersion());
        lblURRateTypeValue.setText(budgetInfoBean.getOhRateClassDescription());
        txtArComments.setText(budgetInfoBean.getComments());
        //Added by tarique to show budget Status start
        if(getBudgetStatus().equalsIgnoreCase("C")){
            lblBudgetStatusValue.setText("Complete");
        }else if(getBudgetStatus().equalsIgnoreCase("I")){
            lblBudgetStatusValue.setText("Incomplete");
        }else{
            lblBudgetStatusValue.setText("None");
        }
        //Added by tarique to show budget Status end   
        //set the budgettotal data
        String queryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
        Vector data = new Vector();
        data.add(0, budgetInfoBean.getProposalNumber());
        data.add(1, new Integer(budgetInfoBean.getVersionNumber()));
//        budgetTotalController.setDataObject(data);
//        budgetTotalController.setFormDataInHiearchy();
        addBudgetdetails(hmBudgetData, queryKey);
        budgetTotalController.setFormData(budgetInfoBean);
//        removeBudgetdetails(queryKey);
    }
    
    
    private String formatDate(java.util.Date date) {
       return dtUtils.formatDate(date.toString(),"dd-MMM-yyyy");
    }
    
    private void addBudgetdetails(java.util.HashMap hmBudgetDetails,String queryKey) {
        queryEngine.addCollection(queryKey,edu.mit.coeus.budget.bean.BudgetPeriodBean.class,
                                (CoeusVector)hmBudgetDetails.get(edu.mit.coeus.budget.bean.BudgetPeriodBean.class));
        queryEngine.addCollection(queryKey,edu.mit.coeus.budget.bean.BudgetDetailBean.class,
                                (CoeusVector)hmBudgetDetails.get(edu.mit.coeus.budget.bean.BudgetDetailBean.class));
        queryEngine.addCollection(queryKey,edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean.class,
                                (CoeusVector)hmBudgetDetails.get(edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean.class));
    }
    
    private void removeBudgetdetails(String queryKey) {
        //queryEngine.removeDataCollection(queryKey);
        queryEngine.removeData(queryKey,edu.mit.coeus.budget.bean.BudgetPeriodBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        queryEngine.removeData(queryKey,edu.mit.coeus.budget.bean.BudgetDetailBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        queryEngine.removeData(queryKey,edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
    }
    //Added by tarique to show budget Status start
    /**
     * Getter for property budgetStatus.
     * @return Value of property budgetStatus.
     */
    public java.lang.String getBudgetStatus() {
        return budgetStatus;
    }    
    
    /**
     * Setter for property budgetStatus.
     * @param budgetStatus New value of property budgetStatus.
     */
    public void setBudgetStatus(java.lang.String budgetStatus) {
        this.budgetStatus = budgetStatus;
    }    
    //Added by tarique to show budget Status start
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlBudgetSummary = new javax.swing.JPanel();
        pnlGeneralSummary = new javax.swing.JPanel();
        lblProposalNumber = new javax.swing.JLabel();
        lblProposalNoValue = new javax.swing.JLabel();
        lblVersionNumber = new javax.swing.JLabel();
        lblVersionNoValue = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblStartDateValue = new javax.swing.JLabel();
        lblBudgetStatus = new javax.swing.JLabel();
        lblBudgetStatusValue = new javax.swing.JLabel();
        chkFinal = new javax.swing.JCheckBox();
        lblEndDate = new javax.swing.JLabel();
        lblEndDateValue = new javax.swing.JLabel();
        pnlCost = new javax.swing.JPanel();
        lblTotalCost = new javax.swing.JLabel();
        lblTotalDirectCost = new javax.swing.JLabel();
        lblUnderRecovery = new javax.swing.JLabel();
        txtTotalCost = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalDirectCost = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtUnderRecovery = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblTotalCostLimit = new javax.swing.JLabel();
        txtTotalCostLimit = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblTotalIndirectCost = new javax.swing.JLabel();
        txtTotalIndirectCost = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblCostSharing = new javax.swing.JLabel();
        txtCostSharing = new edu.mit.coeus.utils.DollarCurrencyTextField();
        pnlRate = new javax.swing.JPanel();
        lblOhRateType = new javax.swing.JLabel();
        lblUnderRecoveryRateType = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        scrpnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        lblOhRateTypeValue = new javax.swing.JLabel();
        lblURRateTypeValue = new javax.swing.JLabel();
        pnlTotal = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pnlBudgetSummary.setLayout(new java.awt.GridBagLayout());

        pnlGeneralSummary.setLayout(new java.awt.GridBagLayout());

        pnlGeneralSummary.setMinimumSize(new java.awt.Dimension(455, 75));
        pnlGeneralSummary.setPreferredSize(new java.awt.Dimension(463, 72));
        lblProposalNumber.setFont(CoeusFontFactory.getLabelFont()
        );
        lblProposalNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblProposalNumber.setText("Proposal Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 3);
        pnlGeneralSummary.add(lblProposalNumber, gridBagConstraints);

        lblProposalNoValue.setFont(CoeusFontFactory.getNormalFont());
        lblProposalNoValue.setText("Proposal Value");
        lblProposalNoValue.setMaximumSize(new java.awt.Dimension(95, 15));
        lblProposalNoValue.setMinimumSize(new java.awt.Dimension(95, 15));
        lblProposalNoValue.setPreferredSize(new java.awt.Dimension(95, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 18);
        pnlGeneralSummary.add(lblProposalNoValue, gridBagConstraints);

        lblVersionNumber.setFont(CoeusFontFactory.getLabelFont());
        lblVersionNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblVersionNumber.setText("Version Number:");
        lblVersionNumber.setMaximumSize(new java.awt.Dimension(95, 15));
        lblVersionNumber.setMinimumSize(new java.awt.Dimension(95, 15));
        lblVersionNumber.setPreferredSize(new java.awt.Dimension(95, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        pnlGeneralSummary.add(lblVersionNumber, gridBagConstraints);

        lblVersionNoValue.setFont(CoeusFontFactory.getNormalFont());
        lblVersionNoValue.setText("Version no value");
        lblVersionNoValue.setMaximumSize(new java.awt.Dimension(85, 15));
        lblVersionNoValue.setMinimumSize(new java.awt.Dimension(85, 15));
        lblVersionNoValue.setPreferredSize(new java.awt.Dimension(85, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 18);
        pnlGeneralSummary.add(lblVersionNoValue, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblStartDate.setText("Start Date: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 0, 1);
        pnlGeneralSummary.add(lblStartDate, gridBagConstraints);

        lblStartDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblStartDateValue.setText("StartDateValue");
        lblStartDateValue.setMaximumSize(new java.awt.Dimension(95, 15));
        lblStartDateValue.setMinimumSize(new java.awt.Dimension(95, 15));
        lblStartDateValue.setPreferredSize(new java.awt.Dimension(95, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 18);
        pnlGeneralSummary.add(lblStartDateValue, gridBagConstraints);

        lblBudgetStatus.setFont(CoeusFontFactory.getLabelFont());
        lblBudgetStatus.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblBudgetStatus.setText("Budget Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        pnlGeneralSummary.add(lblBudgetStatus, gridBagConstraints);

        lblBudgetStatusValue.setFont(CoeusFontFactory.getNormalFont());
        lblBudgetStatusValue.setText("Budget Status Value");
        lblBudgetStatusValue.setMinimumSize(new java.awt.Dimension(140, 20));
        lblBudgetStatusValue.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlGeneralSummary.add(lblBudgetStatusValue, gridBagConstraints);

        chkFinal.setFont(CoeusFontFactory.getLabelFont());
        chkFinal.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkFinal.setLabel("Final:");
        chkFinal.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        pnlGeneralSummary.add(chkFinal, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblEndDate.setText("End Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        pnlGeneralSummary.add(lblEndDate, gridBagConstraints);

        lblEndDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblEndDateValue.setText("EndDateValue");
        lblEndDateValue.setMinimumSize(new java.awt.Dimension(140, 20));
        lblEndDateValue.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlGeneralSummary.add(lblEndDateValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlBudgetSummary.add(pnlGeneralSummary, gridBagConstraints);

        pnlCost.setLayout(new java.awt.GridBagLayout());

        lblTotalCost.setFont(CoeusFontFactory.getLabelFont());
        lblTotalCost.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTotalCost.setText("Total Cost:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlCost.add(lblTotalCost, gridBagConstraints);

        lblTotalDirectCost.setFont(CoeusFontFactory.getLabelFont()
        );
        lblTotalDirectCost.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTotalDirectCost.setText("Total Direct Cost:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlCost.add(lblTotalDirectCost, gridBagConstraints);

        lblUnderRecovery.setFont(CoeusFontFactory.getLabelFont());
        lblUnderRecovery.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblUnderRecovery.setText("UnderRecovery:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlCost.add(lblUnderRecovery, gridBagConstraints);

        txtTotalCost.setEditable(false);
        txtTotalCost.setMinimumSize(new java.awt.Dimension(125, 20));
        txtTotalCost.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlCost.add(txtTotalCost, gridBagConstraints);

        txtTotalDirectCost.setEditable(false);
        txtTotalDirectCost.setMinimumSize(new java.awt.Dimension(125, 20));
        txtTotalDirectCost.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlCost.add(txtTotalDirectCost, gridBagConstraints);

        txtUnderRecovery.setEditable(false);
        txtUnderRecovery.setMinimumSize(new java.awt.Dimension(125, 20));
        txtUnderRecovery.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlCost.add(txtUnderRecovery, gridBagConstraints);

        lblTotalCostLimit.setFont(CoeusFontFactory.getLabelFont());
        lblTotalCostLimit.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTotalCostLimit.setText("Total Cost Limit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 3);
        pnlCost.add(lblTotalCostLimit, gridBagConstraints);

        txtTotalCostLimit.setEditable(false);
        txtTotalCostLimit.setMinimumSize(new java.awt.Dimension(125, 20));
        txtTotalCostLimit.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlCost.add(txtTotalCostLimit, gridBagConstraints);

        lblTotalIndirectCost.setFont(CoeusFontFactory.getLabelFont());
        lblTotalIndirectCost.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTotalIndirectCost.setText("Total Indirect Cost:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 3);
        pnlCost.add(lblTotalIndirectCost, gridBagConstraints);

        txtTotalIndirectCost.setEditable(false);
        txtTotalIndirectCost.setMinimumSize(new java.awt.Dimension(125, 20));
        txtTotalIndirectCost.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlCost.add(txtTotalIndirectCost, gridBagConstraints);

        lblCostSharing.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharing.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCostSharing.setText("Cost Sharing:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 3);
        pnlCost.add(lblCostSharing, gridBagConstraints);

        txtCostSharing.setEditable(false);
        txtCostSharing.setMinimumSize(new java.awt.Dimension(125, 20));
        txtCostSharing.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlCost.add(txtCostSharing, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlBudgetSummary.add(pnlCost, gridBagConstraints);

        pnlRate.setLayout(new java.awt.GridBagLayout());

        pnlRate.setPreferredSize(new java.awt.Dimension(455, 99));
        lblOhRateType.setFont(CoeusFontFactory.getLabelFont()
        );
        lblOhRateType.setText("OH Rate Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 18, 0, 3);
        pnlRate.add(lblOhRateType, gridBagConstraints);

        lblUnderRecoveryRateType.setFont(CoeusFontFactory.getLabelFont());
        lblUnderRecoveryRateType.setText("UR Rate Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 18, 0, 3);
        pnlRate.add(lblUnderRecoveryRateType, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblComments.setText("Comments:");
        lblComments.setMinimumSize(new java.awt.Dimension(70, 15));
        lblComments.setPreferredSize(new java.awt.Dimension(70, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 18, 0, 3);
        pnlRate.add(lblComments, gridBagConstraints);

        scrpnComments.setMinimumSize(new java.awt.Dimension(200, 40));
        scrpnComments.setPreferredSize(new java.awt.Dimension(384, 40));
        txtArComments.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArComments.setEditable(false);
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setMinimumSize(new java.awt.Dimension(0, 14));
        txtArComments.setPreferredSize(new java.awt.Dimension(0, 14));
        scrpnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 3);
        pnlRate.add(scrpnComments, gridBagConstraints);

        lblOhRateTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblOhRateTypeValue.setText("OH Rate Type value");
        lblOhRateTypeValue.setMinimumSize(new java.awt.Dimension(140, 20));
        lblOhRateTypeValue.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlRate.add(lblOhRateTypeValue, gridBagConstraints);

        lblURRateTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblURRateTypeValue.setText("UR Rate Type Value");
        lblURRateTypeValue.setMinimumSize(new java.awt.Dimension(140, 20));
        lblURRateTypeValue.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlRate.add(lblURRateTypeValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlBudgetSummary.add(pnlRate, gridBagConstraints);

        add(pnlBudgetSummary, java.awt.BorderLayout.NORTH);

        pnlTotal.setLayout(new java.awt.BorderLayout());

        add(pnlTotal, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JCheckBox chkFinal;
    public javax.swing.JLabel lblBudgetStatus;
    public javax.swing.JLabel lblBudgetStatusValue;
    public javax.swing.JLabel lblComments;
    public javax.swing.JLabel lblCostSharing;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblEndDateValue;
    public javax.swing.JLabel lblOhRateType;
    public javax.swing.JLabel lblOhRateTypeValue;
    public javax.swing.JLabel lblProposalNoValue;
    public javax.swing.JLabel lblProposalNumber;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JLabel lblStartDateValue;
    public javax.swing.JLabel lblTotalCost;
    public javax.swing.JLabel lblTotalCostLimit;
    public javax.swing.JLabel lblTotalDirectCost;
    public javax.swing.JLabel lblTotalIndirectCost;
    public javax.swing.JLabel lblURRateTypeValue;
    public javax.swing.JLabel lblUnderRecovery;
    public javax.swing.JLabel lblUnderRecoveryRateType;
    public javax.swing.JLabel lblVersionNoValue;
    public javax.swing.JLabel lblVersionNumber;
    public javax.swing.JPanel pnlBudgetSummary;
    public javax.swing.JPanel pnlCost;
    public javax.swing.JPanel pnlGeneralSummary;
    public javax.swing.JPanel pnlRate;
    public javax.swing.JPanel pnlTotal;
    public javax.swing.JScrollPane scrpnComments;
    public javax.swing.JTextArea txtArComments;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtCostSharing;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalCost;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalCostLimit;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalDirectCost;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtTotalIndirectCost;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtUnderRecovery;
    // End of variables declaration//GEN-END:variables
    
}
