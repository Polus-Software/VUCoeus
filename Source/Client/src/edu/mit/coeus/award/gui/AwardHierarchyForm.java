/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import java.awt.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;

/**
 * AwardHierarchyForm.java
 * Created on March 10, 2004, 10:20 AM
 * @author  Vyjayanthi
 */
public class AwardHierarchyForm extends javax.swing.JComponent {
    
    public CoeusDlgWindow dlgAwardHierarchy;
    boolean modal;
    private Frame parent;
    
    private static final int WIDTH = 530;
    private static final int HEIGHT = 410;
    
    /** Creates new form AwardHierarchyForm
     * @param parent takes the frame
     * @param modal true if form is modal, false otherwise
     */
    public AwardHierarchyForm(Frame parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initComponents();
        postInitComponents();
        
        //Details will not be visible initially 
        pnlAwardDetails.setVisible(false);        
    }
    
    /** To initialize other properties of the form */
    private void postInitComponents(){
        dlgAwardHierarchy = new CoeusDlgWindow(parent,modal);
        dlgAwardHierarchy.getContentPane().add(this);
        dlgAwardHierarchy.setResizable(false);
        dlgAwardHierarchy.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardHierarchy.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardHierarchy.getSize();
        dlgAwardHierarchy.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }
    
//    public static void main(String s[]){
//        javax.swing.JFrame frame = new javax.swing.JFrame("Award Hierarchy");
//        frame.getContentPane().add(new AwardHierarchyForm());
//        frame.setSize(530, 410);
//        frame.show();
//    }
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnCorrect = new javax.swing.JButton();
        btnNewEntry = new javax.swing.JButton();
        btnDisplay = new javax.swing.JButton();
        btnCopy = new javax.swing.JButton();
        btnNewChild = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        chkDetails = new javax.swing.JCheckBox();
        pnlAwardHierarchyTree = new edu.mit.coeus.award.gui.AwardHierarchyTree();
        pnlAwardDetails = new edu.mit.coeus.award.gui.AwardDetailsPanel();

        setLayout(new java.awt.GridBagLayout());

        btnCorrect.setFont(CoeusFontFactory.getLabelFont());
        btnCorrect.setMnemonic('r');
        btnCorrect.setText("Correct");
        btnCorrect.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnCorrect, gridBagConstraints);

        btnNewEntry.setFont(CoeusFontFactory.getLabelFont());
        btnNewEntry.setMnemonic('E');
        btnNewEntry.setText("New Entry");
        btnNewEntry.setDisplayedMnemonicIndex(4);
        btnNewEntry.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnNewEntry, gridBagConstraints);

        btnDisplay.setFont(CoeusFontFactory.getLabelFont());
        btnDisplay.setMnemonic('D');
        btnDisplay.setText("Display");
        btnDisplay.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnDisplay, gridBagConstraints);

        btnCopy.setFont(CoeusFontFactory.getLabelFont());
        btnCopy.setMnemonic('p');
        btnCopy.setText("Copy");
        btnCopy.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnCopy, gridBagConstraints);

        btnNewChild.setFont(CoeusFontFactory.getLabelFont());
        btnNewChild.setMnemonic('h');
        btnNewChild.setText("New Child");
        btnNewChild.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnNewChild, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnClose, gridBagConstraints);

        chkDetails.setFont(CoeusFontFactory.getLabelFont());
        chkDetails.setText("Details ");
        chkDetails.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(chkDetails, gridBagConstraints);

        pnlAwardHierarchyTree.setBorder(new javax.swing.border.EtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(pnlAwardHierarchyTree, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        add(pnlAwardDetails, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnCopy;
    public javax.swing.JButton btnCorrect;
    public javax.swing.JButton btnDisplay;
    public javax.swing.JButton btnNewChild;
    public javax.swing.JButton btnNewEntry;
    public javax.swing.JCheckBox chkDetails;
    public edu.mit.coeus.award.gui.AwardDetailsPanel pnlAwardDetails;
    public edu.mit.coeus.award.gui.AwardHierarchyTree pnlAwardHierarchyTree;
    // End of variables declaration//GEN-END:variables
    
}
