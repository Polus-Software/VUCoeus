/*
 * SponsorHierarchyList.java
 *
 * Created on November 16, 2004, 4:11 PM
 */

package edu.mit.coeus.sponsorhierarchy.gui;


import edu.mit.coeus.gui.*;
import javax.swing.*;
import java.awt.Frame;
/**
 *
 * @author  surekhan
 */
public class SponsorHierarchyListForm extends javax.swing.JPanel {
    
    /** Creates new form SponsorHierarchyList */
    public SponsorHierarchyListForm() {
        initComponents();
    }
    
    public static void main(String args[]){
        JFrame frame = new JFrame("Sponsor Hierarchy list");
        SponsorHierarchyListForm sponsorHierarchyListForm = new SponsorHierarchyListForm();
        frame.getContentPane().add(sponsorHierarchyListForm);
        frame.setSize(435,275);
        frame.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnList = new javax.swing.JScrollPane();
        lstSponsorList = new javax.swing.JList();
        btnNew = new javax.swing.JButton();
        btnMaintain = new javax.swing.JButton();
        btnCopy = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnList.setMinimumSize(new java.awt.Dimension(335, 265));
        scrPnList.setPreferredSize(new java.awt.Dimension(335, 265));
        lstSponsorList.setBackground(new java.awt.Color(236, 233, 216));
        lstSponsorList.setFont(CoeusFontFactory.getNormalFont());
        lstSponsorList.setOpaque(false);
        scrPnList.setViewportView(lstSponsorList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(scrPnList, gridBagConstraints);

        btnNew.setFont(CoeusFontFactory.getLabelFont());
        btnNew.setMnemonic('N');
        btnNew.setText("New");
        btnNew.setMaximumSize(new java.awt.Dimension(82, 26));
        btnNew.setMinimumSize(new java.awt.Dimension(82, 26));
        btnNew.setPreferredSize(new java.awt.Dimension(82, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        add(btnNew, gridBagConstraints);

        btnMaintain.setFont(CoeusFontFactory.getLabelFont());
        btnMaintain.setMnemonic('M');
        btnMaintain.setText("Maintain");
        btnMaintain.setMaximumSize(new java.awt.Dimension(82, 26));
        btnMaintain.setMinimumSize(new java.awt.Dimension(82, 26));
        btnMaintain.setPreferredSize(new java.awt.Dimension(82, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnMaintain, gridBagConstraints);

        btnCopy.setFont(CoeusFontFactory.getLabelFont());
        btnCopy.setMnemonic('p');
        btnCopy.setText("Copy");
        btnCopy.setMaximumSize(new java.awt.Dimension(82, 26));
        btnCopy.setMinimumSize(new java.awt.Dimension(82, 26));
        btnCopy.setPreferredSize(new java.awt.Dimension(82, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnCopy, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(82, 26));
        btnDelete.setMinimumSize(new java.awt.Dimension(82, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(82, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnDelete, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(82, 26));
        btnClose.setMinimumSize(new java.awt.Dimension(82, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(82, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 3, 0, 0);
        add(btnClose, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnCopy;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnMaintain;
    public javax.swing.JButton btnNew;
    public javax.swing.JList lstSponsorList;
    public javax.swing.JScrollPane scrPnList;
    // End of variables declaration//GEN-END:variables
    
}