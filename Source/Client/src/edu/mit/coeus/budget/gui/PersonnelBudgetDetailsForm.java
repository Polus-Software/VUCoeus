/*
 * PersonnelBudgetDetailsForm.java
 *
 * Created on October 6, 2003, 8:18 PM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.utils.FormattedDocument;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.budget.utils.LineItemCalculatedAmountsTable;
import edu.mit.coeus.budget.gui.PersonnelBudgetDetailTable;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.JTextFieldFilter;



/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class PersonnelBudgetDetailsForm extends javax.swing.JComponent {
    
    
    public CoeusDlgWindow dlgPersonnelBudgetDetailsForm;
    boolean modal;
    private Component parent;
    public char functionType;
    private static final int WIDTH = 775;
    private static final int HEIGHT = 440;
    private String title = "Personnel Budget Details";
    
    /** Creates new form PersonnelBudgetDetailsForm */
    public PersonnelBudgetDetailsForm(Component parent, boolean modal) {
        this.parent = parent;
        this.modal = true;
        initialiseComponents();
        
        //Bug Fix: 1571 Start 1 
        setTableKeyTraversal();
        //Bug Fix: 1571 End 1
        setUpForm();
        
    }
    
    private void setUpForm() {
        
        dlgPersonnelBudgetDetailsForm = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),title,modal);
        dlgPersonnelBudgetDetailsForm.getContentPane().add(this);
        dlgPersonnelBudgetDetailsForm.pack();
        dlgPersonnelBudgetDetailsForm.setResizable(false);
        dlgPersonnelBudgetDetailsForm.setFont(CoeusFontFactory.getLabelFont());
        //        dlgPersonnelBudgetDetailsForm.setSize(WIDTH,HEIGHT);
        dlgPersonnelBudgetDetailsForm.setVisible(false);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgPersonnelBudgetDetailsForm.getSize();
        dlgPersonnelBudgetDetailsForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        
    }
    
    public PersonnelBudgetDetailTable getPersonnelBudgetDetailTable() {
        return (PersonnelBudgetDetailTable) tblPersonnelBudgetTable;
    }
    
    
    private void initialiseComponents() {
        
        java.awt.GridBagConstraints gridBagConstraints;
        
        txtQuantity = new javax.swing.JTextField();
        DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        decimalFormat.setMinimumIntegerDigits(1);
        decimalFormat.setMaximumIntegerDigits(4);
        
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        
        JTextField textField = txtQuantity;
        FormattedDocument formattedDocument = new FormattedDocument(decimalFormat, textField);
        formattedDocument.setNegativeAllowed(true);
        textField.setDocument(formattedDocument);
        textField.setHorizontalAlignment(JLabel.RIGHT);
        
        pnlTopHeaderPanel = new javax.swing.JPanel();
        lblCostElement = new javax.swing.JLabel();
        txtCostElmCode = new javax.swing.JTextField();
        txtCostElmDescription = new javax.swing.JTextField();
        lblStartDate = new javax.swing.JLabel();
        txtStartDate = new javax.swing.JTextField();
        lblEndDate = new javax.swing.JLabel();
        txtEndDate = new javax.swing.JTextField();
        lblDescription = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        lblCost = new javax.swing.JLabel();
        txtCost = new DollarCurrencyTextField();
        lblUnderRecovery = new javax.swing.JLabel();
        txtUnderRecovery = new DollarCurrencyTextField();
        lblCostShare = new javax.swing.JLabel();
        txtCostShare = new DollarCurrencyTextField();
        lblQuantity = new javax.swing.JLabel();
//        txtQuantity = new javax.swing.JTextField();
//        txtQuantity .setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 4));
        txtQuantity.setHorizontalAlignment(JTextField.RIGHT);
        pnlCenterTabularPanel = new javax.swing.JPanel();
        lblblankSpace = new javax.swing.JLabel();
        scrPnPersonnelBudgetTable = new javax.swing.JScrollPane();
        
        pnlCalAmount = new javax.swing.JPanel();
        pnlLineItemCalAmount = new LineItemCalculatedAmountsTable();
        pnlPersonnelBudgetTable =  new JPanel();
        
        pnlActiobButtonPanel = new javax.swing.JPanel();
        btnPersons = new javax.swing.JButton();
        btnCalculate = new javax.swing.JButton();
        btnDetails = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        
        setLayout(new java.awt.GridBagLayout());
        
        
        //pnlTopHeaderPanel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlTopHeaderPanel.setBorder(new javax.swing.border.EtchedBorder());
        pnlTopHeaderPanel.setLayout(new java.awt.GridBagLayout());
        
        
        //pnlTopHeaderPanel.setMaximumSize(new java.awt.Dimension(660, 80));
        pnlTopHeaderPanel.setMinimumSize(new java.awt.Dimension(660, 110));
        pnlTopHeaderPanel.setPreferredSize(new java.awt.Dimension(660, 110));
        lblCostElement.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblCostElement.setText("Cost Elm.: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTopHeaderPanel.add(lblCostElement, gridBagConstraints);
        
        txtCostElmCode.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtCostElmCode.setMaximumSize(new java.awt.Dimension(80, 20));
        txtCostElmCode.setMinimumSize(new java.awt.Dimension(80, 20));
        txtCostElmCode.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTopHeaderPanel.add(txtCostElmCode, gridBagConstraints);
        
        txtCostElmDescription.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtCostElmDescription.setMaximumSize(new java.awt.Dimension(100, 20));
        txtCostElmDescription.setMinimumSize(new java.awt.Dimension(100, 20));
        txtCostElmDescription.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTopHeaderPanel.add(txtCostElmDescription, gridBagConstraints);
        
        lblStartDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setText("Start Date : ");
        lblStartDate.setMaximumSize(new java.awt.Dimension(70, 16));
        lblStartDate.setMinimumSize(new java.awt.Dimension(70, 16));
        lblStartDate.setPreferredSize(new java.awt.Dimension(70, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTopHeaderPanel.add(lblStartDate, gridBagConstraints);
        
        txtStartDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtStartDate.setMaximumSize(new java.awt.Dimension(85, 20));
        txtStartDate.setMinimumSize(new java.awt.Dimension(85, 20));
        txtStartDate.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTopHeaderPanel.add(txtStartDate, gridBagConstraints);
        
        lblEndDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEndDate.setText("End Date : ");
        lblEndDate.setMaximumSize(new java.awt.Dimension(70, 16));
        lblEndDate.setMinimumSize(new java.awt.Dimension(70, 16));
        lblEndDate.setPreferredSize(new java.awt.Dimension(70, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTopHeaderPanel.add(lblEndDate, gridBagConstraints);
        
        txtEndDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtEndDate.setMaximumSize(new java.awt.Dimension(85, 20));
        txtEndDate.setMinimumSize(new java.awt.Dimension(85, 20));
        txtEndDate.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTopHeaderPanel.add(txtEndDate, gridBagConstraints);
        
        lblDescription.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDescription.setText("Desc.: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        pnlTopHeaderPanel.add(lblDescription, gridBagConstraints);
        
        txtDescription.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtDescription.setMinimumSize(new java.awt.Dimension(8, 20));
        txtDescription.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        pnlTopHeaderPanel.add(txtDescription, gridBagConstraints);
        
        lblCost.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCost.setText("Cost: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTopHeaderPanel.add(lblCost, gridBagConstraints);
        
        txtCost.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtCost.setMaximumSize(new java.awt.Dimension(85, 20));
        txtCost.setMinimumSize(new java.awt.Dimension(85, 20));
        txtCost.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTopHeaderPanel.add(txtCost, gridBagConstraints);
        
        lblUnderRecovery.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblUnderRecovery.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUnderRecovery.setText("Underrecovery:");
        lblUnderRecovery.setMaximumSize(new java.awt.Dimension(100, 16));
        lblUnderRecovery.setMinimumSize(new java.awt.Dimension(100, 16));
        lblUnderRecovery.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTopHeaderPanel.add(lblUnderRecovery, gridBagConstraints);
        
        txtUnderRecovery.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtUnderRecovery.setMaximumSize(new java.awt.Dimension(85, 20));
        txtUnderRecovery.setMinimumSize(new java.awt.Dimension(85, 20));
        txtUnderRecovery.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTopHeaderPanel.add(txtUnderRecovery, gridBagConstraints);
        
        lblCostShare.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblCostShare.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCostShare.setText(" Cost Share: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTopHeaderPanel.add(lblCostShare, gridBagConstraints);
        
        txtCostShare.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtCostShare.setMaximumSize(new java.awt.Dimension(85, 20));
        txtCostShare.setMinimumSize(new java.awt.Dimension(85, 20));
        txtCostShare.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTopHeaderPanel.add(txtCostShare, gridBagConstraints);
        
        lblQuantity.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblQuantity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblQuantity.setText("Quantity: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTopHeaderPanel.add(lblQuantity, gridBagConstraints);
        
        txtQuantity.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        txtQuantity.setMaximumSize(new java.awt.Dimension(85, 20));
        txtQuantity.setMinimumSize(new java.awt.Dimension(85, 20));
        txtQuantity.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        
        //gridBagConstraints.weighty = 1.0;
        
        pnlTopHeaderPanel.add(txtQuantity, gridBagConstraints);
        
        add(pnlTopHeaderPanel, new java.awt.GridBagConstraints());
        
        pnlCenterTabularPanel.setLayout(new java.awt.GridBagLayout());
        
        //pnlCenterTabularPanel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlCenterTabularPanel.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        
        //pnlCenterTabularPanel.setMaximumSize(new java.awt.Dimension(660, 275));
        pnlCenterTabularPanel.setMinimumSize(new java.awt.Dimension(660, 297));
        pnlCenterTabularPanel.setPreferredSize(new java.awt.Dimension(660, 297));
        
//        lblblankSpace.setMaximumSize(new java.awt.Dimension(660, 3));
//        lblblankSpace.setMinimumSize(new java.awt.Dimension(660, 3));
//        lblblankSpace.setPreferredSize(new java.awt.Dimension(660, 3));
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
//        pnlCenterTabularPanel.add(lblblankSpace, gridBagConstraints);
//
        //Bug Fix: 1571 Start 4 
        tblPersonnelBudgetTable = new PersonnelBudgetDetailTable(){
           public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                    javax.swing.SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            tblPersonnelBudgetTable.dispatchEvent(new java.awt.event.KeyEvent(
                                tblPersonnelBudgetTable ,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                        }
                    });
                }  
        };
        //Bug Fix: 1571 End 4
        
        scrPnPersonnelBudgetTable.setViewportView(tblPersonnelBudgetTable);
        
        //Bug Fix: 1571 Start 2
        tblPersonnelBudgetTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPersonnelBudgetTableMouseClicked(evt);
            }
        });
        //Bug Fix: 1571 End 2
        
        scrPnPersonnelBudgetTable.setMinimumSize(new java.awt.Dimension(650, 258));
        scrPnPersonnelBudgetTable.setPreferredSize(new java.awt.Dimension(650, 258));
        
        scrPnPersonnelBudgetTable.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //gridBagConstraints.weighty = 1.0;
        pnlCenterTabularPanel.add(scrPnPersonnelBudgetTable, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        
        add(pnlCenterTabularPanel, gridBagConstraints);
        
        
        pnlCalAmount.setLayout(new BoxLayout(pnlCalAmount,BoxLayout.Y_AXIS));
        pnlCalAmount.setMaximumSize(new java.awt.Dimension(660, 100));
        pnlCalAmount.setMinimumSize(new java.awt.Dimension(660, 100));
        pnlCalAmount.setPreferredSize(new java.awt.Dimension(660, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.weightx = 1.0;
        
        add(pnlCalAmount, gridBagConstraints);
        
        pnlLineItemCalAmount.setMaximumSize(new java.awt.Dimension(660, 85));
        pnlLineItemCalAmount.setMinimumSize(new java.awt.Dimension(660, 85));
        pnlLineItemCalAmount.setPreferredSize(new java.awt.Dimension(660, 85));
        
        //pnlLineItemCalAmount.setBorder(new javax.swing.border.EtchedBorder());
        pnlCalAmount.add(pnlLineItemCalAmount);
        
        pnlActiobButtonPanel.setLayout(new java.awt.GridBagLayout());
        
        
        pnlActiobButtonPanel.setMaximumSize(new java.awt.Dimension(90, 440));
        pnlActiobButtonPanel.setMinimumSize(new java.awt.Dimension(90, 440));
        pnlActiobButtonPanel.setPreferredSize(new java.awt.Dimension(90, 440));
        btnPersons.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnPersons.setMnemonic('P');
        btnPersons.setText("Persons");
        
        //btnPersons.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPersons.setMaximumSize(new java.awt.Dimension(90, 25));
        btnPersons.setMinimumSize(new java.awt.Dimension(90, 25));
        btnPersons.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.weighty = 1.0;
        pnlActiobButtonPanel.add(btnPersons, gridBagConstraints);
        
        btnCalculate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnCalculate.setMnemonic('u');
        btnCalculate.setText("Calculate");
        
        //btnCalculate.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCalculate.setMaximumSize(new java.awt.Dimension(90, 25));
        btnCalculate.setMinimumSize(new java.awt.Dimension(90, 25));
        btnCalculate.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.weighty = 0; //0.2;
        pnlActiobButtonPanel.add(btnCalculate, gridBagConstraints);
        
        btnDetails.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnDetails.setMnemonic('t');
        btnDetails.setText("Details");
        
        //btnDetails.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDetails.setMaximumSize(new java.awt.Dimension(90, 25));
        btnDetails.setMinimumSize(new java.awt.Dimension(90, 25));
        btnDetails.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlActiobButtonPanel.add(btnDetails, gridBagConstraints);
        
        btnDelete.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        
        //btnDelete.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDelete.setMaximumSize(new java.awt.Dimension(90, 25));
        btnDelete.setMinimumSize(new java.awt.Dimension(90, 25));
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlActiobButtonPanel.add(btnDelete, gridBagConstraints);
        
        btnAdd.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        
        //  btnAdd.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAdd.setMaximumSize(new java.awt.Dimension(90, 25));
        btnAdd.setMinimumSize(new java.awt.Dimension(90, 25));
        btnAdd.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0; //0.1
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        pnlActiobButtonPanel.add(btnAdd, gridBagConstraints);
        
        btnCancel.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        
        //btnCancel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancel.setMaximumSize(new java.awt.Dimension(90, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(90, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlActiobButtonPanel.add(btnCancel, gridBagConstraints);
        
        btnOk.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        
        //btnOk.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnOk.setMaximumSize(new java.awt.Dimension(90, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(90, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlActiobButtonPanel.add(btnOk, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        
        add(pnlActiobButtonPanel, gridBagConstraints);
    }
    
    //Bug Fix: 1571 Start 3
    private void tblPersonnelBudgetTableMouseClicked(java.awt.event.MouseEvent evt) {
        // Add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {

                tblPersonnelBudgetTable.dispatchEvent(new java.awt.event.KeyEvent(
                tblPersonnelBudgetTable,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );

            }
        });
    }
    //Bug Fix: 1571 End 3
    
    //    // Variables declaration - do not modify
    
    public javax.swing.JLabel lblblankSpace;
    public javax.swing.JPanel pnlCenterTabularPanel;
    public javax.swing.JPanel pnlCalAmount;
    public LineItemCalculatedAmountsTable pnlLineItemCalAmount ;
    public javax.swing.JScrollPane scrPnPersonnelBudgetTable;
    public javax.swing.JTable tblPersonnelBudgetTable;
    public javax.swing.JTextField txtCostElmDescription;
    public javax.swing.JPanel pnlPersonnelBudgetTable;
    
    
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCalculate;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnDetails;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnPersons;
    public javax.swing.JLabel lblCost;
    
    public javax.swing.JLabel lblCostElement;
    public javax.swing.JLabel lblCostShare;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblQuantity;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JLabel lblUnderRecovery;
    
    public javax.swing.JPanel pnlActiobButtonPanel;
    public javax.swing.JPanel pnlTopHeaderPanel;
    public DollarCurrencyTextField txtCost;
    public javax.swing.JTextField txtCostElmCode;
    
    public DollarCurrencyTextField txtCostShare;
    
    public javax.swing.JTextField txtDescription;
    public javax.swing.JTextField txtEndDate;
    public javax.swing.JTextField txtQuantity;
    public javax.swing.JTextField txtStartDate;
    public DollarCurrencyTextField txtUnderRecovery;
    
    
    
    
    // End of variables declaration
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    /*
     
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTopHeaderPanel = new javax.swing.JPanel();
        lblCostElement = new javax.swing.JLabel();
        txtCostElmCode = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        lblStartDate = new javax.swing.JLabel();
        txtStartDate = new javax.swing.JTextField();
        lblEndDate = new javax.swing.JLabel();
        txtEndDate = new javax.swing.JTextField();
        lblDescription = new javax.swing.JLabel();
        txtCostElmDesc = new javax.swing.JTextField();
        lblCost = new javax.swing.JLabel();
        txtCost = new javax.swing.JTextField();
        lblUnderRecovery = new javax.swing.JLabel();
        txtUnderRecovery = new javax.swing.JTextField();
        lblCostShare = new javax.swing.JLabel();
        txtCostShare = new javax.swing.JTextField();
        lblQuantity = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        lblBlankSpace = new javax.swing.JLabel();
        pnlPersonBudgetTable = new javax.swing.JPanel();
        scrPnSelectedPersonList = new javax.swing.JScrollPane();
        tblSelectedPersonList = new javax.swing.JTable();
        pnlRateApplicablePanel = new javax.swing.JPanel();
        pnlActiobButtonPanel = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnDetails = new javax.swing.JButton();
        btnCalculate = new javax.swing.JButton();
        btnPersons = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Personal Budget Details");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlTopHeaderPanel.setLayout(new java.awt.GridBagLayout());

        pnlTopHeaderPanel.setToolTipText("");
        pnlTopHeaderPanel.setMaximumSize(new java.awt.Dimension(670, 80));
        pnlTopHeaderPanel.setMinimumSize(new java.awt.Dimension(670, 80));
        pnlTopHeaderPanel.setPreferredSize(new java.awt.Dimension(670, 80));
        lblCostElement.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblCostElement.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCostElement.setText("  Cost Elm. :");
        lblCostElement.setToolTipText("");
        lblCostElement.setMaximumSize(new java.awt.Dimension(65, 20));
        lblCostElement.setMinimumSize(new java.awt.Dimension(65, 20));
        lblCostElement.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblCostElement, gridBagConstraints);

        txtCostElmCode.setToolTipText("");
        txtCostElmCode.setMaximumSize(new java.awt.Dimension(50, 20));
        txtCostElmCode.setMinimumSize(new java.awt.Dimension(50, 20));
        txtCostElmCode.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtCostElmCode, gridBagConstraints);

        txtDescription.setToolTipText("");
        txtDescription.setMaximumSize(new java.awt.Dimension(200, 20));
        txtDescription.setMinimumSize(new java.awt.Dimension(200, 20));
        txtDescription.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtDescription, gridBagConstraints);

        lblStartDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setIcon(new javax.swing.ImageIcon(""));
        lblStartDate.setText("Start Date : ");
        lblStartDate.setToolTipText("");
        lblStartDate.setMaximumSize(new java.awt.Dimension(60, 20));
        lblStartDate.setMinimumSize(new java.awt.Dimension(60, 20));
        lblStartDate.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblStartDate, gridBagConstraints);

        txtStartDate.setToolTipText("");
        txtStartDate.setMaximumSize(new java.awt.Dimension(80, 20));
        txtStartDate.setMinimumSize(new java.awt.Dimension(80, 20));
        txtStartDate.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtStartDate, gridBagConstraints);

        lblEndDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblEndDate.setIcon(new javax.swing.ImageIcon(""));
        lblEndDate.setText("End Date :");
        lblEndDate.setToolTipText("");
        lblEndDate.setMaximumSize(new java.awt.Dimension(60, 20));
        lblEndDate.setMinimumSize(new java.awt.Dimension(60, 20));
        lblEndDate.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblEndDate, gridBagConstraints);

        txtEndDate.setToolTipText("");
        txtEndDate.setMaximumSize(new java.awt.Dimension(80, 20));
        txtEndDate.setMinimumSize(new java.awt.Dimension(80, 20));
        txtEndDate.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtEndDate, gridBagConstraints);

        lblDescription.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDescription.setIcon(new javax.swing.ImageIcon(""));
        lblDescription.setText("    Desc. :");
        lblDescription.setToolTipText("");
        lblDescription.setMaximumSize(new java.awt.Dimension(70, 20));
        lblDescription.setMinimumSize(new java.awt.Dimension(70, 20));
        lblDescription.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblDescription, gridBagConstraints);

        txtCostElmDesc.setToolTipText("");
        txtCostElmDesc.setMaximumSize(new java.awt.Dimension(100, 20));
        txtCostElmDesc.setMinimumSize(new java.awt.Dimension(100, 20));
        txtCostElmDesc.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlTopHeaderPanel.add(txtCostElmDesc, gridBagConstraints);

        lblCost.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblCost.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCost.setIcon(new javax.swing.ImageIcon(""));
        lblCost.setText("       Cost : ");
        lblCost.setToolTipText("");
        lblCost.setMaximumSize(new java.awt.Dimension(70, 20));
        lblCost.setMinimumSize(new java.awt.Dimension(70, 20));
        lblCost.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblCost, gridBagConstraints);

        txtCost.setMaximumSize(new java.awt.Dimension(100, 20));
        txtCost.setMinimumSize(new java.awt.Dimension(100, 20));
        txtCost.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtCost, gridBagConstraints);

        lblUnderRecovery.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblUnderRecovery.setIcon(new javax.swing.ImageIcon(""));
        lblUnderRecovery.setText("UnderRecovery :");
        lblUnderRecovery.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblUnderRecovery, gridBagConstraints);

        txtUnderRecovery.setToolTipText("");
        txtUnderRecovery.setMaximumSize(new java.awt.Dimension(100, 20));
        txtUnderRecovery.setMinimumSize(new java.awt.Dimension(100, 20));
        txtUnderRecovery.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtUnderRecovery, gridBagConstraints);

        lblCostShare.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblCostShare.setIcon(new javax.swing.ImageIcon(""));
        lblCostShare.setText("Cost Share : ");
        lblCostShare.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblCostShare, gridBagConstraints);

        txtCostShare.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtCostShare, gridBagConstraints);

        lblQuantity.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        lblQuantity.setIcon(new javax.swing.ImageIcon(""));
        lblQuantity.setText("Quantity : ");
        lblQuantity.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblQuantity, gridBagConstraints);

        txtQuantity.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(txtQuantity, gridBagConstraints);

        lblBlankSpace.setIcon(new javax.swing.ImageIcon(""));
        lblBlankSpace.setToolTipText("");
        lblBlankSpace.setMaximumSize(new java.awt.Dimension(20, 20));
        lblBlankSpace.setMinimumSize(new java.awt.Dimension(20, 20));
        lblBlankSpace.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlTopHeaderPanel.add(lblBlankSpace, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(pnlTopHeaderPanel, gridBagConstraints);

        pnlPersonBudgetTable.setLayout(new java.awt.GridBagLayout());

        pnlPersonBudgetTable.setMaximumSize(new java.awt.Dimension(650, 270));
        pnlPersonBudgetTable.setMinimumSize(new java.awt.Dimension(650, 270));
        pnlPersonBudgetTable.setPreferredSize(new java.awt.Dimension(650, 270));
        scrPnSelectedPersonList.setBorder(new javax.swing.border.EtchedBorder());
        scrPnSelectedPersonList.setMaximumSize(new java.awt.Dimension(655, 270));
        scrPnSelectedPersonList.setMinimumSize(new java.awt.Dimension(655, 270));
        scrPnSelectedPersonList.setPreferredSize(new java.awt.Dimension(655, 270));
        tblSelectedPersonList.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnSelectedPersonList.setViewportView(tblSelectedPersonList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlPersonBudgetTable.add(scrPnSelectedPersonList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(pnlPersonBudgetTable, gridBagConstraints);

        pnlRateApplicablePanel.setLayout(new javax.swing.BoxLayout(pnlRateApplicablePanel, javax.swing.BoxLayout.X_AXIS));

        pnlRateApplicablePanel.setBorder(new javax.swing.border.EtchedBorder());
        pnlRateApplicablePanel.setMaximumSize(new java.awt.Dimension(650, 90));
        pnlRateApplicablePanel.setMinimumSize(new java.awt.Dimension(650, 90));
        pnlRateApplicablePanel.setPreferredSize(new java.awt.Dimension(650, 90));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(pnlRateApplicablePanel, gridBagConstraints);

        pnlActiobButtonPanel.setLayout(new java.awt.GridBagLayout());

        pnlActiobButtonPanel.setMaximumSize(new java.awt.Dimension(90, 440));
        pnlActiobButtonPanel.setMinimumSize(new java.awt.Dimension(90, 440));
        pnlActiobButtonPanel.setPreferredSize(new java.awt.Dimension(100, 440));
        btnOk.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnOk.setIcon(new javax.swing.ImageIcon(""));
        btnOk.setText("OK");
        btnOk.setToolTipText("");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 25));
        pnlActiobButtonPanel.add(btnOk, new java.awt.GridBagConstraints());

        btnCancel.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnCancel.setIcon(new javax.swing.ImageIcon(""));
        btnCancel.setText("Cancel");
        btnCancel.setToolTipText("");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlActiobButtonPanel.add(btnCancel, gridBagConstraints);

        btnAdd.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnAdd.setIcon(new javax.swing.ImageIcon(""));
        btnAdd.setText("Add");
        btnAdd.setToolTipText("");
        btnAdd.setMaximumSize(new java.awt.Dimension(75, 25));
        btnAdd.setMinimumSize(new java.awt.Dimension(75, 25));
        btnAdd.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.1;
        pnlActiobButtonPanel.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnDelete.setIcon(new javax.swing.ImageIcon(""));
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("");
        btnDelete.setMaximumSize(new java.awt.Dimension(75, 25));
        btnDelete.setMinimumSize(new java.awt.Dimension(75, 25));
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlActiobButtonPanel.add(btnDelete, gridBagConstraints);

        btnDetails.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnDetails.setIcon(new javax.swing.ImageIcon(""));
        btnDetails.setText("Details");
        btnDetails.setToolTipText("");
        btnDetails.setMaximumSize(new java.awt.Dimension(75, 25));
        btnDetails.setMinimumSize(new java.awt.Dimension(75, 25));
        btnDetails.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlActiobButtonPanel.add(btnDetails, gridBagConstraints);

        btnCalculate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnCalculate.setIcon(new javax.swing.ImageIcon(""));
        btnCalculate.setText("Calculate");
        btnCalculate.setToolTipText("");
        btnCalculate.setMaximumSize(new java.awt.Dimension(75, 25));
        btnCalculate.setMinimumSize(new java.awt.Dimension(75, 25));
        btnCalculate.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weighty = 0.2;
        pnlActiobButtonPanel.add(btnCalculate, gridBagConstraints);

        btnPersons.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnPersons.setIcon(new javax.swing.ImageIcon(""));
        btnPersons.setText("Persons");
        btnPersons.setToolTipText("");
        btnPersons.setMaximumSize(new java.awt.Dimension(75, 25));
        btnPersons.setMinimumSize(new java.awt.Dimension(75, 25));
        btnPersons.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        pnlActiobButtonPanel.add(btnPersons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(pnlActiobButtonPanel, gridBagConstraints);

    }//GEN-END:initComponents
     
private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
// Add your handling code here:
}//GEN-LAST:event_closeDialog
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCalculate;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnDetails;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnPersons;
    public javax.swing.JLabel lblBlankSpace;
    public javax.swing.JLabel lblCost;
    public javax.swing.JLabel lblCostElement;
    public javax.swing.JLabel lblCostShare;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblQuantity;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JLabel lblUnderRecovery;
    public javax.swing.JPanel pnlActiobButtonPanel;
    public javax.swing.JPanel pnlPersonBudgetTable;
    public javax.swing.JPanel pnlRateApplicablePanel;
    public javax.swing.JPanel pnlTopHeaderPanel;
    public javax.swing.JScrollPane scrPnSelectedPersonList;
    public javax.swing.JTable tblSelectedPersonList;
    public javax.swing.JTextField txtCost;
    public javax.swing.JTextField txtCostElmCode;
    public javax.swing.JTextField txtCostElmDesc;
    public javax.swing.JTextField txtCostShare;
    public javax.swing.JTextField txtDescription;
    public javax.swing.JTextField txtEndDate;
    public javax.swing.JTextField txtQuantity;
    public javax.swing.JTextField txtStartDate;
    public javax.swing.JTextField txtUnderRecovery;
    // End of variables declaration//GEN-END:variables
     */
    
    //Bug Fix: 1571 Start 5 
    public void setTableKeyTraversal(){
        javax.swing.InputMap im = tblPersonnelBudgetTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = tblPersonnelBudgetTable.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    
                    if (row == rowCount) {
                        row = 0;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        tblPersonnelBudgetTable.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = tblPersonnelBudgetTable.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    
                    column -= 1;
                    
                    if (column <= 0) {
                        column = 6;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        tblPersonnelBudgetTable.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
    //Bug Fix: 1571 End 5
    
}
