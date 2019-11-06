/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;

/*
 * AwardSyncDetailsController.java
 *
 * Created on April 21, 2009, 12:14 PM
 * @author keerthyjayaraj
 */

public class AwardSyncDetailsForm extends javax.swing.JComponent {
    
    public CoeusDlgWindow dlgAwardSyncDetails;
    private CoeusMessageResources coeusMessageResources;
    
    private static final int WIDTH = 400;
    private static final int HEIGHT = 170;
    
    /** Creates new form AwardHierarchyForm
     * @param parent takes the frame
     * @param modal true if form is modal, false otherwise
     */
    public AwardSyncDetailsForm() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        postInitComponents();
        
    }
    
    /** To initialize other properties of the form */
    private void postInitComponents(){
        dlgAwardSyncDetails = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),true);
        dlgAwardSyncDetails.getContentPane().add(this);
        dlgAwardSyncDetails.setResizable(false);
        dlgAwardSyncDetails.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardSyncDetails.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardSyncDetails.getSize();
        dlgAwardSyncDetails.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnGrpSyncMode = new javax.swing.ButtonGroup();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnlAwardTypes = new javax.swing.JPanel();
        rbtnActive = new javax.swing.JRadioButton();
        rbtnAll = new javax.swing.JRadioButton();
        chkFabE = new javax.swing.JCheckBox();
        chkCS = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 3, 5);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
        add(btnCancel, gridBagConstraints);

        pnlAwardTypes.setLayout(new java.awt.GridLayout(4, 1, 2, 5));

        pnlAwardTypes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), coeusMessageResources.parseLabelKey("awardSyncDetails.3051"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlAwardTypes.setMaximumSize(new java.awt.Dimension(300, 120));
        pnlAwardTypes.setMinimumSize(new java.awt.Dimension(300, 120));
        pnlAwardTypes.setPreferredSize(new java.awt.Dimension(300, 120));
        btnGrpSyncMode.add(rbtnActive);
        rbtnActive.setFont(CoeusFontFactory.getNormalFont());
        rbtnActive.setSelected(true);
        rbtnActive.setText(coeusMessageResources.parseLabelKey("awardSyncDetails.3053"));
        rbtnActive.setIconTextGap(8);
        pnlAwardTypes.add(rbtnActive);

        btnGrpSyncMode.add(rbtnAll);
        rbtnAll.setFont(CoeusFontFactory.getNormalFont());
        rbtnAll.setText(coeusMessageResources.parseLabelKey("awardSyncDetails.3052"));
        rbtnAll.setIconTextGap(8);
        pnlAwardTypes.add(rbtnAll);

        chkFabE.setFont(CoeusFontFactory.getNormalFont());
        chkFabE.setText(coeusMessageResources.parseLabelKey("awardSyncDetails.3056"));
        chkFabE.setIconTextGap(8);
        pnlAwardTypes.add(chkFabE);

        chkCS.setFont(CoeusFontFactory.getNormalFont());
        chkCS.setText(coeusMessageResources.parseLabelKey("awardSyncDetails.3057"));
        chkCS.setIconTextGap(8);
        pnlAwardTypes.add(chkCS);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlAwardTypes, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.ButtonGroup btnGrpSyncMode;
    public javax.swing.JButton btnOK;
    public javax.swing.JCheckBox chkCS;
    public javax.swing.JCheckBox chkFabE;
    public javax.swing.JPanel pnlAwardTypes;
    public javax.swing.JRadioButton rbtnActive;
    public javax.swing.JRadioButton rbtnAll;
    // End of variables declaration//GEN-END:variables
    
}
