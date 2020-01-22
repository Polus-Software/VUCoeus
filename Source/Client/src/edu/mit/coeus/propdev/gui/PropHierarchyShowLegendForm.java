/*
 * PropHierarchyShowLegendForm.java
 *
 * Created on January 17, 2006, 7:33 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import javax.swing.ImageIcon;

/**
 *
 * @author  tarique
 */
public class PropHierarchyShowLegendForm extends javax.swing.JComponent {
    
    /** Creates new form PropHierarchyShowLegendForm */
    public PropHierarchyShowLegendForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlShowLegend = new javax.swing.JPanel();
        lblHierarchyParent = new edu.mit.coeus.utils.CoeusLabel();
        lblHierarchyChild = new edu.mit.coeus.utils.CoeusLabel();
        lblVersionFinal = new edu.mit.coeus.utils.CoeusLabel();
        lblVersionFinalIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblBudgetComplete = new edu.mit.coeus.utils.CoeusLabel();
        lblBudget = new edu.mit.coeus.utils.CoeusLabel();
        lblBudgetCompleteIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblBudgetIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblBudgetIncomplete = new edu.mit.coeus.utils.CoeusLabel();
        lblSync = new edu.mit.coeus.utils.CoeusLabel();
        lblHierarchyParentIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblHierarchyChildIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblBudgetIncompleteIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblNotSync = new edu.mit.coeus.utils.CoeusLabel();
        lblNotSyncIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblSyncIcon = new edu.mit.coeus.utils.CoeusLabel();
        lblSubProject = new javax.swing.JLabel();
        lblSubProjectIcon = new javax.swing.JLabel();
        btnShowLegend = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlShowLegend.setLayout(new java.awt.GridBagLayout());

        pnlShowLegend.setMinimumSize(new java.awt.Dimension(425, 65));
        pnlShowLegend.setPreferredSize(new java.awt.Dimension(425, 65));
        lblHierarchyParent.setText("Parent Proposal : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblHierarchyParent, gridBagConstraints);

        lblHierarchyChild.setText("Child Proposal : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblHierarchyChild, gridBagConstraints);

        lblVersionFinal.setText("Budget Version Final : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblVersionFinal, gridBagConstraints);

        lblVersionFinalIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.BUDGET_FINAL_ICON )));
        lblVersionFinalIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblVersionFinalIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblVersionFinalIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblVersionFinalIcon, gridBagConstraints);

        lblBudgetComplete.setText("Budget Complete : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblBudgetComplete, gridBagConstraints);

        lblBudget.setText("Budget : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblBudget, gridBagConstraints);

        lblBudgetCompleteIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.COMPLETE_ICON )));
        lblBudgetCompleteIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblBudgetCompleteIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblBudgetCompleteIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblBudgetCompleteIcon, gridBagConstraints);

        lblBudgetIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.BUDGET_ICON )));
        lblBudgetIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblBudgetIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblBudgetIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblBudgetIcon, gridBagConstraints);

        lblBudgetIncomplete.setText("Budget Incomplete : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblBudgetIncomplete, gridBagConstraints);

        lblSync.setText("Synced : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblSync, gridBagConstraints);

        lblHierarchyParentIcon.setIcon(new ImageIcon(getClass().getClassLoader()
            .getResource(CoeusGuiConstants.PARENT_PROP_HIE_ICON)));
        lblHierarchyParentIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblHierarchyParentIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblHierarchyParentIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblHierarchyParentIcon, gridBagConstraints);

        lblHierarchyChildIcon.setIcon(new ImageIcon(getClass().getClassLoader()
            .getResource(CoeusGuiConstants.CHILD_PROP_HIE_ICON)));
        lblHierarchyChildIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblHierarchyChildIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblHierarchyChildIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblHierarchyChildIcon, gridBagConstraints);

        lblBudgetIncompleteIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.INCOMPLETE_ICON )));
        lblBudgetIncompleteIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblBudgetIncompleteIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblBudgetIncompleteIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblBudgetIncompleteIcon, gridBagConstraints);

        lblNotSync.setText("Not Synced : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblNotSync, gridBagConstraints);

        lblNotSyncIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.RED )));
        lblNotSyncIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblNotSyncIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblNotSyncIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblNotSyncIcon, gridBagConstraints);

        lblSyncIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.GREEN )));
        lblSyncIcon.setMaximumSize(new java.awt.Dimension(20, 16));
        lblSyncIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblSyncIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        pnlShowLegend.add(lblSyncIcon, gridBagConstraints);

        lblSubProject.setFont(CoeusFontFactory.getLabelFont());
        lblSubProject.setText("Sub Project :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlShowLegend.add(lblSubProject, gridBagConstraints);

        lblSubProjectIcon.setIcon(new ImageIcon(getClass().getClassLoader()
            .getResource(CoeusGuiConstants.AWARDS_ICON)));
        lblSubProjectIcon.setMinimumSize(new java.awt.Dimension(20, 16));
        lblSubProjectIcon.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        pnlShowLegend.add(lblSubProjectIcon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(pnlShowLegend, gridBagConstraints);

        btnShowLegend.setFont(CoeusFontFactory.getLabelFont());
        btnShowLegend.setMnemonic('w');
        btnShowLegend.setText("Show Legend");
        btnShowLegend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowLegendAction(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 3);
        add(btnShowLegend, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnShowLegendAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowLegendAction
        if(pnlShowLegend.isVisible()){
            pnlShowLegend.setVisible(false);
            btnShowLegend.setText("Show Legend");
            btnShowLegend.setMnemonic('w');
        }else{
            pnlShowLegend.setVisible(true);
            btnShowLegend.setText("Hide");
            btnShowLegend.setMnemonic('i');
        }
    }//GEN-LAST:event_btnShowLegendAction
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnShowLegend;
    public edu.mit.coeus.utils.CoeusLabel lblBudget;
    public edu.mit.coeus.utils.CoeusLabel lblBudgetComplete;
    public edu.mit.coeus.utils.CoeusLabel lblBudgetCompleteIcon;
    public edu.mit.coeus.utils.CoeusLabel lblBudgetIcon;
    public edu.mit.coeus.utils.CoeusLabel lblBudgetIncomplete;
    public edu.mit.coeus.utils.CoeusLabel lblBudgetIncompleteIcon;
    public edu.mit.coeus.utils.CoeusLabel lblHierarchyChild;
    public edu.mit.coeus.utils.CoeusLabel lblHierarchyChildIcon;
    public edu.mit.coeus.utils.CoeusLabel lblHierarchyParent;
    public edu.mit.coeus.utils.CoeusLabel lblHierarchyParentIcon;
    public edu.mit.coeus.utils.CoeusLabel lblNotSync;
    public edu.mit.coeus.utils.CoeusLabel lblNotSyncIcon;
    public javax.swing.JLabel lblSubProject;
    public javax.swing.JLabel lblSubProjectIcon;
    public edu.mit.coeus.utils.CoeusLabel lblSync;
    public edu.mit.coeus.utils.CoeusLabel lblSyncIcon;
    public edu.mit.coeus.utils.CoeusLabel lblVersionFinal;
    public edu.mit.coeus.utils.CoeusLabel lblVersionFinalIcon;
    public javax.swing.JPanel pnlShowLegend;
    // End of variables declaration//GEN-END:variables
    
}