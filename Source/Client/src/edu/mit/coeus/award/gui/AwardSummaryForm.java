/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import java.awt.*;
import javax.swing.*;

import edu.mit.coeus.gui.*;

/** Displays the summary details for the selected award
 * AwardSummaryForm.java
 * Created on March 26, 2004, 9:26 AM
 * @author  Vyjayanthi
 */
public class AwardSummaryForm extends javax.swing.JComponent {
    
    public CoeusDlgWindow dlgAwardSummary;
    private boolean modal;
    private Frame parent;
    
    private static final int WIDTH = 645;
    private static final int HEIGHT = 400;
    private static final String WINDOW_TITLE = "Award Summary";
    
    /** Creates new form AwardSummaryForm */
    public AwardSummaryForm(Frame parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initComponents();
        postInitComponents();
    }
    
    /** To initialize other properties of the form */
    private void postInitComponents(){
        dlgAwardSummary = new CoeusDlgWindow(parent, modal);
        dlgAwardSummary.getContentPane().add(this);
        dlgAwardSummary.setResizable(false);
        dlgAwardSummary.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardSummary.setSize(WIDTH,HEIGHT);
        dlgAwardSummary.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardSummary.getSize();
        dlgAwardSummary.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlInvestigatorUnit = new edu.mit.coeus.propdev.gui.MedusaInvestigatorUnitForm();
        btnClose = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        pnlAwardDetails = new edu.mit.coeus.award.gui.MedusaAwardDetailForm();

        setLayout(new java.awt.GridBagLayout());

        pnlInvestigatorUnit.setMinimumSize(new java.awt.Dimension(100, 100));
        pnlInvestigatorUnit.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        gridBagConstraints.weightx = 1.0;
        add(pnlInvestigatorUnit, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(73, 26));
        btnClose.setMinimumSize(new java.awt.Dimension(73, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnClose, gridBagConstraints);

        btnPrev.setFont(CoeusFontFactory.getLabelFont());
        btnPrev.setMnemonic('P');
        btnPrev.setText("Prev");
        btnPrev.setMaximumSize(new java.awt.Dimension(73, 26));
        btnPrev.setMinimumSize(new java.awt.Dimension(73, 26));
        btnPrev.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnPrev, gridBagConstraints);

        btnNext.setFont(CoeusFontFactory.getLabelFont());
        btnNext.setMnemonic('N');
        btnNext.setText("Next");
        btnNext.setMaximumSize(new java.awt.Dimension(73, 26));
        btnNext.setMinimumSize(new java.awt.Dimension(73, 26));
        btnNext.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnNext, gridBagConstraints);

        pnlAwardDetails.setBorder(new javax.swing.border.EtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pnlAwardDetails, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnNext;
    public javax.swing.JButton btnPrev;
    public edu.mit.coeus.award.gui.MedusaAwardDetailForm pnlAwardDetails;
    public edu.mit.coeus.propdev.gui.MedusaInvestigatorUnitForm pnlInvestigatorUnit;
    // End of variables declaration//GEN-END:variables
    
}
