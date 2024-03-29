/*
 * BudgetJustificationForm.java
 *
 * Created on September 6, 2003, 10:02 AM
 */

package edu.mit.coeus.budget.gui;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class BudgetJustificationForm extends CoeusDlgWindow {
    
    
    
    /** Creates new form BudgetJustificationForm */    
    public BudgetJustificationForm() {
        super(CoeusGuiConstants.getMDIForm(),"",true);
        setFormUI ();
        
    }
    
    /** Constructor BudgetJustificationForm
     * @param parent Component parent form
     * @param modal boolean if <CODE>true<CODE> Modal window
     */    
    public BudgetJustificationForm(Component parent, boolean modal) {
        super((Component) parent,"",true);
        setFormUI ();
    }
    

    /** For setting Form User Interface */    
    public void setFormUI () {        
        
        initComponents();
        //getContentPane().add( this );
        setResizable(false);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
  
    }
    
  
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblBudgetJustification = new javax.swing.JLabel();
        lblLastUpdate = new javax.swing.JLabel();
        lblUpdateUser = new javax.swing.JLabel();
        txtLastUpdate = new javax.swing.JTextField();
        txtUpdateUser = new javax.swing.JTextField();
        scrPnBudgetJustification = new javax.swing.JScrollPane();
        txtArBudgetJustification = new javax.swing.JTextArea();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnConsolidate = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setFont(CoeusFontFactory.getLabelFont());
        lblBudgetJustification.setFont(CoeusFontFactory.getLabelFont());
        lblBudgetJustification.setText("Budget Justification:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
        getContentPane().add(lblBudgetJustification, gridBagConstraints);

        lblLastUpdate.setFont(CoeusFontFactory.getLabelFont());
        lblLastUpdate.setText("Last Update:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(lblLastUpdate, gridBagConstraints);

        lblUpdateUser.setFont(CoeusFontFactory.getLabelFont());
        lblUpdateUser.setText("Update User:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblUpdateUser, gridBagConstraints);

        txtLastUpdate.setEditable(false);
        txtLastUpdate.setFont(CoeusFontFactory.getNormalFont());
        txtLastUpdate.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtLastUpdate.setMinimumSize(new java.awt.Dimension(120, 20));
        txtLastUpdate.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(txtLastUpdate, gridBagConstraints);

        txtUpdateUser.setEditable(false);
        txtUpdateUser.setFont(CoeusFontFactory.getNormalFont());
        txtUpdateUser.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtUpdateUser.setMinimumSize(new java.awt.Dimension(70, 20));
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(txtUpdateUser, gridBagConstraints);

        scrPnBudgetJustification.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnBudgetJustification.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrPnBudgetJustification.setMaximumSize(new java.awt.Dimension(430, 325));
        scrPnBudgetJustification.setMinimumSize(new java.awt.Dimension(430, 325));
        scrPnBudgetJustification.setPreferredSize(new java.awt.Dimension(430, 325));
        txtArBudgetJustification.setFont(CoeusFontFactory.getNormalFont());
        txtArBudgetJustification.setLineWrap(true);
        scrPnBudgetJustification.setViewportView(txtArBudgetJustification);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
        getContentPane().add(scrPnBudgetJustification, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMaximumSize(new java.awt.Dimension(100, 95));
        pnlButtons.setMinimumSize(new java.awt.Dimension(100, 95));
        pnlButtons.setPreferredSize(new java.awt.Dimension(101, 95));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(85, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(85, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(85, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(85, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnConsolidate.setFont(CoeusFontFactory.getLabelFont());
        btnConsolidate.setMnemonic('s');
        btnConsolidate.setText("Consolidate");
        btnConsolidate.setMaximumSize(new java.awt.Dimension(100, 25));
        btnConsolidate.setMinimumSize(new java.awt.Dimension(100, 25));
        btnConsolidate.setPreferredSize(new java.awt.Dimension(101, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlButtons.add(btnConsolidate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        getContentPane().add(pnlButtons, gridBagConstraints);

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnConsolidate;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblBudgetJustification;
    public javax.swing.JLabel lblLastUpdate;
    public javax.swing.JLabel lblUpdateUser;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JScrollPane scrPnBudgetJustification;
    public javax.swing.JTextArea txtArBudgetJustification;
    public javax.swing.JTextField txtLastUpdate;
    public javax.swing.JTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables
    
}
