/*
 * AddInstituteRatesForm.java
 *
 * Created on August 17, 2004, 11:30 AM
 */

package edu.mit.coeus.rates.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class AddInstituteRatesForm extends javax.swing.JPanel {
	
	/** Creates new form AddInstituteRatesForm */
	public AddInstituteRatesForm() {
		initComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		btnGroup = new javax.swing.ButtonGroup();
		lblRateClass = new javax.swing.JLabel();
		txtRateClass = new edu.mit.coeus.utils.CoeusTextField();
		lblRateType = new javax.swing.JLabel();
		txtRateType = new edu.mit.coeus.utils.CoeusTextField();
		lblActivityType = new javax.swing.JLabel();
		scrlPaneActivityType = new javax.swing.JScrollPane();
		tblActivityType = new edu.mit.coeus.utils.table.CoeusTable();
		lblFiscalYear = new javax.swing.JLabel();
		txtFiscalYear = new edu.mit.coeus.utils.CoeusTextField();
		lblStartDate = new javax.swing.JLabel();
		txtStartDate = new edu.mit.coeus.utils.CoeusTextField();
		btnOK = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		lblCampusFlag = new javax.swing.JLabel();
		lblRate = new javax.swing.JLabel();
		txtRates = new edu.mit.coeus.utils.CurrencyField();
		rdBtnOn = new javax.swing.JRadioButton();
		rdBtnOff = new javax.swing.JRadioButton();
		rdBtnBoth = new javax.swing.JRadioButton();
		
		
		setLayout(new java.awt.GridBagLayout());
		
		lblRateClass.setFont(CoeusFontFactory.getLabelFont());
		lblRateClass.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblRateClass.setText("Rate Class:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(lblRateClass, gridBagConstraints);
		lblRateClass.getAccessibleContext().setAccessibleName("Rate Class");
		
		txtRateClass.setBackground(new java.awt.Color(255, 255, 255));
		txtRateClass.setFont(CoeusFontFactory.getNormalFont());
		txtRateClass.setMinimumSize(new java.awt.Dimension(250, 21));
		txtRateClass.setPreferredSize(new java.awt.Dimension(250, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		add(txtRateClass, gridBagConstraints);
		
		lblRateType.setFont(CoeusFontFactory.getLabelFont());
		lblRateType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblRateType.setText("Rate Type:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 2);
		add(lblRateType, gridBagConstraints);
		
		txtRateType.setFont(CoeusFontFactory.getNormalFont());
		txtRateType.setMinimumSize(new java.awt.Dimension(70, 20));
		txtRateType.setPreferredSize(new java.awt.Dimension(70, 20));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 2);
		gridBagConstraints.weightx = 1.0;
		add(txtRateType, gridBagConstraints);
		
		lblActivityType.setFont(CoeusFontFactory.getLabelFont());
		lblActivityType.setText("Activity Type:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
		add(lblActivityType, gridBagConstraints);
		
		scrlPaneActivityType.setBorder(new javax.swing.border.EtchedBorder());
		scrlPaneActivityType.setMinimumSize(new java.awt.Dimension(250, 150));
		tblActivityType.setModel(new javax.swing.table.DefaultTableModel(
		new Object [][] {
			{},
			{},
			{},
			{}
		},
		new String [] {
			
		}
		));
		scrlPaneActivityType.setViewportView(tblActivityType);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(scrlPaneActivityType, gridBagConstraints);
		
		lblFiscalYear.setFont(CoeusFontFactory.getLabelFont());
		lblFiscalYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblFiscalYear.setText("Fiscal year:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 1);
		add(lblFiscalYear, gridBagConstraints);
		
		txtFiscalYear.setFont(CoeusFontFactory.getNormalFont());
		txtFiscalYear.setMinimumSize(new java.awt.Dimension(100, 21));
		txtFiscalYear.setPreferredSize(new java.awt.Dimension(100, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 1);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(txtFiscalYear, gridBagConstraints);
		
		lblStartDate.setFont(CoeusFontFactory.getLabelFont());
		lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblStartDate.setText("Start Date:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
		add(lblStartDate, gridBagConstraints);
		
		txtStartDate.setFont(CoeusFontFactory.getNormalFont());
		txtStartDate.setMinimumSize(new java.awt.Dimension(6, 20));
		txtStartDate.setPreferredSize(new java.awt.Dimension(6, 20));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
		add(txtStartDate, gridBagConstraints);
		
		btnOK.setFont(CoeusFontFactory.getLabelFont());
		btnOK.setMnemonic('O');
		btnOK.setText("OK");
		btnOK.setMaximumSize(new java.awt.Dimension(67, 25));
		btnOK.setMinimumSize(new java.awt.Dimension(67, 25));
		btnOK.setPreferredSize(new java.awt.Dimension(67, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 2);
		add(btnOK, gridBagConstraints);
		
		btnCancel.setFont(CoeusFontFactory.getLabelFont());
		btnCancel.setMnemonic('C');
		btnCancel.setText("Cancel");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
		add(btnCancel, gridBagConstraints);
		
		lblCampusFlag.setFont(CoeusFontFactory.getLabelFont());
		lblCampusFlag.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblCampusFlag.setText("Campus Flag:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(lblCampusFlag, gridBagConstraints);
		
		lblRate.setFont(CoeusFontFactory.getLabelFont());
		lblRate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblRate.setText("Rate:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(lblRate, gridBagConstraints);
		
		txtRates.setMinimumSize(new java.awt.Dimension(100, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(txtRates, gridBagConstraints);
		
		rdBtnOn.setFont(CoeusFontFactory.getLabelFont());
		rdBtnOn.setText("On");
		btnGroup.add(rdBtnOn);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(rdBtnOn, gridBagConstraints);
		
		rdBtnOff.setFont(CoeusFontFactory.getLabelFont());
		rdBtnOff.setText("Off");
		btnGroup.add(rdBtnOff);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(rdBtnOff, gridBagConstraints);
		
		rdBtnBoth.setFont(CoeusFontFactory.getLabelFont());
		rdBtnBoth.setText("Both");
		btnGroup.add(rdBtnBoth);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(rdBtnBoth, gridBagConstraints);
		
	}//GEN-END:initComponents
	
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	public javax.swing.JButton btnCancel;
	public javax.swing.ButtonGroup btnGroup;
	public javax.swing.JButton btnOK;
	public javax.swing.JLabel lblActivityType;
	public javax.swing.JLabel lblCampusFlag;
	public javax.swing.JLabel lblFiscalYear;
	public javax.swing.JLabel lblRate;
	public javax.swing.JLabel lblRateClass;
	public javax.swing.JLabel lblRateType;
	public javax.swing.JLabel lblStartDate;
	public javax.swing.JRadioButton rdBtnBoth;
	public javax.swing.JRadioButton rdBtnOff;
	public javax.swing.JRadioButton rdBtnOn;
	public javax.swing.JScrollPane scrlPaneActivityType;
	public edu.mit.coeus.utils.table.CoeusTable tblActivityType;
	public edu.mit.coeus.utils.CoeusTextField txtFiscalYear;
	public edu.mit.coeus.utils.CoeusTextField txtRateClass;
	public edu.mit.coeus.utils.CoeusTextField txtRateType;
	public edu.mit.coeus.utils.CurrencyField txtRates;
	public edu.mit.coeus.utils.CoeusTextField txtStartDate;
	// End of variables declaration//GEN-END:variables
	
}