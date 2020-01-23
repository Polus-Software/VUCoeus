/*
 * AwardGoToForm.java
 *
 * Created on June 18, 2004, 8:15 PM
 * @author  bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;

import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * This class creates a component for handling go operations
 */
public class AwardGoToForm extends javax.swing.JComponent implements ActionListener,CaretListener {
    private CoeusDlgWindow dlgAwardGoTo;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private static final int WIDTH = 300;
    private static final int HEIGHT = 125;
    private static final int TEXT_LIMIT=10;
    private String value="";
    private int selOption=0; //by default cancel action
    
    public static final int MIT_NUMBER = 1;
    public static final int ACCOUNT_NUMBER = 2;
    public static final int CANCEL = 0;
    private static final String EMPTY_STRING ="";
    /** Creates new form AwardGoToForm */
    public AwardGoToForm() {
        initComponents();
        txtNumber.setDocument(new LimitedPlainDocument(TEXT_LIMIT));
        setFormData();
        registerComponets();
        postInitComponents();
    }
    /*
     * Sets the states of the buttons and check boxes
     */
    public void setFormData(){
        rdBtnAccount.setSelected(true);
        btnOK.setEnabled(false);
        if(rdBtnMIT.isSelected()){
            selOption = MIT_NUMBER;
        }else if(rdBtnAccount.isSelected()){
            selOption = ACCOUNT_NUMBER;
        }
    }
    /*
     * Registers listeners 
     */
    private void registerComponets(){
        btnCancel.addActionListener(this);
        btnOK.addActionListener(this);
        txtNumber.addCaretListener(this);
        
        //Buf Fix : 1126 Start
        txtNumber.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_ENTER &&
                kEvent.getSource() instanceof JTextField){
                    btnOK.doClick();
                    kEvent.consume();
                }
            }
        });
        
        rdBtnAccount.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_ENTER &&
                kEvent.getSource() instanceof JRadioButton){
                    btnOK.doClick();
                    kEvent.consume();
                }
            }
        });
        
        rdBtnMIT.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_ENTER &&
                kEvent.getSource() instanceof JRadioButton){
                    btnOK.doClick();
                    kEvent.consume();
                }
            }
        });
        //Buf Fix : 1126 End
    }
    /**
     * Creates a dialog for the forma and sets its properties
     */
    private void postInitComponents(){
        dlgAwardGoTo = new CoeusDlgWindow(mdiForm);
        dlgAwardGoTo = new CoeusDlgWindow(mdiForm);
        dlgAwardGoTo.getContentPane().add(this);
        dlgAwardGoTo.setTitle("Goto Award");
        dlgAwardGoTo.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardGoTo.setModal(true);
        dlgAwardGoTo.setResizable(false);
        dlgAwardGoTo.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardGoTo.getSize();
        dlgAwardGoTo.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAwardGoTo.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgAwardGoTo.dispose();
            }
        });
        dlgAwardGoTo.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardGoTo.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) {
                dlgAwardGoTo.dispose();
            }
            public void windowOpened(WindowEvent we) {
                txtNumber.requestFocusInWindow();
            }
        });
    }
    /**
     * Displays the form.
     * Returns value 0 if no item was selected. Otherwise
     * if MIT awa number is selected, returns 1
     * and if account number is selected returns 2
     */
    public int display(){
        dlgAwardGoTo.setVisible(true);
        return selOption;
    }
    /**
     * Handler for button actions
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(btnOK)){
            if(!(txtNumber.getText().trim().equals(EMPTY_STRING))){
                value = txtNumber.getText().trim();
            }else{
                value = EMPTY_STRING;
            }
            if(rdBtnMIT.isSelected()){
                selOption = MIT_NUMBER;
            }else if(rdBtnAccount.isSelected()){
                selOption = ACCOUNT_NUMBER;
            }
            dlgAwardGoTo.dispose();
        }else if(source.equals(btnCancel)){
            selOption = CANCEL;
            value = EMPTY_STRING;
            dlgAwardGoTo.dispose();
        }
    }
    /**
     * To make the ok button enabled and disabled
     * depending on whther there is anything entered or not
     */
    public void caretUpdate(CaretEvent e) {
        if (txtNumber.getText().equals(EMPTY_STRING)) {
            btnOK.setEnabled(false);
        } else {
            btnOK.setEnabled(true);
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnGrp = new javax.swing.ButtonGroup();
        rdBtnAccount = new javax.swing.JRadioButton();
        rdBtnMIT = new javax.swing.JRadioButton();
        txtNumber = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        rdBtnAccount.setFont(CoeusFontFactory.getLabelFont());
//JM        rdBtnAccount.setText("Account Number");
        rdBtnAccount.setText("Center Number"); //JM 5-25-2011 updated text per 4.4.2
        btnGrp.add(rdBtnAccount);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        add(rdBtnAccount, gridBagConstraints);

        rdBtnMIT.setFont(CoeusFontFactory.getLabelFont());
        rdBtnMIT.setText("MIT Award Number");
        btnGrp.add(rdBtnMIT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 0);
        add(rdBtnMIT, gridBagConstraints);

        txtNumber.setFont(CoeusFontFactory.getNormalFont());
        txtNumber.setMinimumSize(new java.awt.Dimension(200, 24));
        txtNumber.setPreferredSize(new java.awt.Dimension(200, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 0);
        add(txtNumber, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(75, 25));
        btnOK.setMinimumSize(new java.awt.Dimension(67, 25));
        btnOK.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 5);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 5);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents

    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public java.lang.String getValue() {
        return value;
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.ButtonGroup btnGrp;
    public javax.swing.JButton btnOK;
    public javax.swing.JRadioButton rdBtnAccount;
    public javax.swing.JRadioButton rdBtnMIT;
    public javax.swing.JTextField txtNumber;
    // End of variables declaration//GEN-END:variables
    
}
