/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.search.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;

/**
 * InstituteProposalDetails.java
 * Created on May 11, 2004, 3:16 PM
 * @author  Vyjayanthi
 */

public class InstituteProposalDetails extends javax.swing.JPanel {
    
    /** Holds an instance of dateUtils */
    private DateUtils dateUtils;
    
    private static final String EMPTY_STRING = "";
    private static final double ZERO = 0.0;
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    
    /** Creates new form InstituteProposalDetails */
    public InstituteProposalDetails() {
        initComponents();
        dateUtils = new DateUtils();
    }
    
    /** Set the data to the form
     * @param instituteProposalBean holds the data
     */
    public void setFormData(InstituteProposalBean instituteProposalBean){
        if( instituteProposalBean == null ){
            resetData();
            return ;
        }
        txtArTitle.setText(instituteProposalBean.getTitle());
        txtArTitle.setCaretPosition(0);
        txtStartDateInitial.setText(
            instituteProposalBean.getRequestStartDateInitial() == null ? EMPTY_STRING :
            dateUtils.formatDate(instituteProposalBean.getRequestStartDateInitial().toString(), REQUIRED_DATEFORMAT));
        txtEndDateInitial.setText(
            instituteProposalBean.getRequestEndDateInitial() == null ? EMPTY_STRING :
            dateUtils.formatDate(instituteProposalBean.getRequestEndDateInitial().toString(), REQUIRED_DATEFORMAT));
        txtDirectCostInitial.setValue(
            instituteProposalBean.getTotalDirectCostInitial());
        txtIndirectCostInitial.setValue(
            instituteProposalBean.getTotalInDirectCostInitial());
        txtStartDateTotal.setText(
            instituteProposalBean.getRequestStartDateTotal() == null ? EMPTY_STRING :
            dateUtils.formatDate(instituteProposalBean.getRequestStartDateTotal().toString(), REQUIRED_DATEFORMAT));
        txtEndDateTotal.setText(
            instituteProposalBean.getRequestEndDateTotal() == null ? EMPTY_STRING :
            dateUtils.formatDate(instituteProposalBean.getRequestEndDateTotal().toString(), REQUIRED_DATEFORMAT));
        txtDirectCostTotal.setValue(
            instituteProposalBean.getTotalDirectCostTotal());
        txtIndirectCostTotal.setValue(
            instituteProposalBean.getTotalInDirectCostTotal());
    }
    
    /** To clear the displayed data
     */
    public void resetData() {
        txtArTitle.setText(EMPTY_STRING);
        txtStartDateInitial.setText(EMPTY_STRING);
        txtEndDateInitial.setText(EMPTY_STRING);
        txtDirectCostInitial.setValue(ZERO);
        txtIndirectCostInitial.setValue(ZERO);
        txtStartDateTotal.setText(EMPTY_STRING);
        txtEndDateTotal.setText(EMPTY_STRING);
        txtDirectCostTotal.setValue(ZERO);
        txtIndirectCostTotal.setValue(ZERO);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblStartDateInitial = new javax.swing.JLabel();
        txtStartDateInitial = new javax.swing.JTextField();
        lblEndDateInitial = new javax.swing.JLabel();
        txtEndDateInitial = new javax.swing.JTextField();
        lblDirectCostInitial = new javax.swing.JLabel();
        lblIndirectCostInitial = new javax.swing.JLabel();
        lblStartDateTotal = new javax.swing.JLabel();
        txtStartDateTotal = new javax.swing.JTextField();
        lblEndDateTotal = new javax.swing.JLabel();
        txtEndDateTotal = new javax.swing.JTextField();
        lblDirectCostTotal = new javax.swing.JLabel();
        lblIndirectCostTotal = new javax.swing.JLabel();
        txtDirectCostInitial = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtDirectCostTotal = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtIndirectCostInitial = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtIndirectCostTotal = new edu.mit.coeus.utils.DollarCurrencyTextField();

        setLayout(new java.awt.GridBagLayout());

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 2);
        add(lblTitle, gridBagConstraints);

        scrPnTitle.setMinimumSize(new java.awt.Dimension(450, 35));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(450, 35));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtArTitle.setOpaque(false);
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(scrPnTitle, gridBagConstraints);

        lblStartDateInitial.setFont(CoeusFontFactory.getLabelFont());
        lblStartDateInitial.setText("Start Date Initial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 2);
        add(lblStartDateInitial, gridBagConstraints);

        txtStartDateInitial.setEditable(false);
        txtStartDateInitial.setFont(CoeusFontFactory.getNormalFont());
        txtStartDateInitial.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtStartDateInitial.setMinimumSize(new java.awt.Dimension(180, 21));
        txtStartDateInitial.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(txtStartDateInitial, gridBagConstraints);

        lblEndDateInitial.setFont(CoeusFontFactory.getLabelFont());
        lblEndDateInitial.setText("End Date Initial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 2);
        add(lblEndDateInitial, gridBagConstraints);

        txtEndDateInitial.setEditable(false);
        txtEndDateInitial.setFont(CoeusFontFactory.getNormalFont());
        txtEndDateInitial.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(txtEndDateInitial, gridBagConstraints);

        lblDirectCostInitial.setFont(CoeusFontFactory.getLabelFont());
        lblDirectCostInitial.setText("Direct Cost Initial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 2);
        add(lblDirectCostInitial, gridBagConstraints);

        lblIndirectCostInitial.setFont(CoeusFontFactory.getLabelFont());
        lblIndirectCostInitial.setText("Indirect Cost Initial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 2);
        add(lblIndirectCostInitial, gridBagConstraints);

        lblStartDateTotal.setFont(CoeusFontFactory.getLabelFont());
        lblStartDateTotal.setText("Start Date Total:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(lblStartDateTotal, gridBagConstraints);

        txtStartDateTotal.setEditable(false);
        txtStartDateTotal.setFont(CoeusFontFactory.getNormalFont());
        txtStartDateTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtStartDateTotal.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(txtStartDateTotal, gridBagConstraints);

        lblEndDateTotal.setFont(CoeusFontFactory.getLabelFont());
        lblEndDateTotal.setText("End Date Total:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(lblEndDateTotal, gridBagConstraints);

        txtEndDateTotal.setEditable(false);
        txtEndDateTotal.setFont(CoeusFontFactory.getNormalFont());
        txtEndDateTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(txtEndDateTotal, gridBagConstraints);

        lblDirectCostTotal.setFont(CoeusFontFactory.getLabelFont());
        lblDirectCostTotal.setText("Direct Cost Total:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(lblDirectCostTotal, gridBagConstraints);

        lblIndirectCostTotal.setFont(CoeusFontFactory.getLabelFont());
        lblIndirectCostTotal.setText("Indirect Cost Total:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(lblIndirectCostTotal, gridBagConstraints);

        txtDirectCostInitial.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(txtDirectCostInitial, gridBagConstraints);

        txtDirectCostTotal.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(txtDirectCostTotal, gridBagConstraints);

        txtIndirectCostInitial.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 5);
        add(txtIndirectCostInitial, gridBagConstraints);

        txtIndirectCostTotal.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 5);
        add(txtIndirectCostTotal, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblDirectCostInitial;
    private javax.swing.JLabel lblDirectCostTotal;
    private javax.swing.JLabel lblEndDateInitial;
    private javax.swing.JLabel lblEndDateTotal;
    private javax.swing.JLabel lblIndirectCostInitial;
    private javax.swing.JLabel lblIndirectCostTotal;
    private javax.swing.JLabel lblStartDateInitial;
    private javax.swing.JLabel lblStartDateTotal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JTextArea txtArTitle;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtDirectCostInitial;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtDirectCostTotal;
    private javax.swing.JTextField txtEndDateInitial;
    private javax.swing.JTextField txtEndDateTotal;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtIndirectCostInitial;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtIndirectCostTotal;
    private javax.swing.JTextField txtStartDateInitial;
    private javax.swing.JTextField txtStartDateTotal;
    // End of variables declaration//GEN-END:variables
    
}
