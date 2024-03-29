/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardDocumentForm.java
 *
 * Created on October 3, 2007, 11:04 AM
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.JFrame;

/**
 *
 * @author  divyasusendran
 */
public class AwardDocumentForm extends javax.swing.JPanel {
    
    /**
     * Creates new form AwardDocumentForm
     */
    public AwardDocumentForm() {
        initComponents();
    }
    
   
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlListDocument = new javax.swing.JPanel();
        scrPnListDocument = new javax.swing.JScrollPane();
        tblListDocument = new javax.swing.JTable();
        btnAdd = new edu.mit.coeus.utils.CoeusButton();
        btnShowAll = new edu.mit.coeus.utils.CoeusButton();
        btnVoid = new edu.mit.coeus.utils.CoeusButton();
        btnShowActive = new edu.mit.coeus.utils.CoeusButton();
        // JM 4-4-2013 added Delete button back
        btnDelete = new edu.mit.coeus.utils.CoeusButton();
        // JM END
        btnModify = new edu.mit.coeus.utils.CoeusButton();

        setMinimumSize(new java.awt.Dimension(120, 20));
        setPreferredSize(new java.awt.Dimension(550, 341));
        setLayout(new java.awt.GridBagLayout());

        pnlListDocument.setLayout(new java.awt.GridBagLayout());

        scrPnListDocument.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attachments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 11))); // NOI18N
        scrPnListDocument.setMaximumSize(new java.awt.Dimension(770, 600));
        scrPnListDocument.setMinimumSize(new java.awt.Dimension(770, 600));
        scrPnListDocument.setPreferredSize(new java.awt.Dimension(770, 600));

        tblListDocument.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnListDocument.setViewportView(tblListDocument);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        pnlListDocument.add(scrPnListDocument, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 3, 0);
        add(pnlListDocument, gridBagConstraints);

        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setAlignmentX(0.5F);
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMaximumSize(new java.awt.Dimension(110, 22));
        btnAdd.setMinimumSize(new java.awt.Dimension(110, 22));
        btnAdd.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 2, 0);
        add(btnAdd, gridBagConstraints);

        btnShowAll.setMnemonic('S');
        btnShowAll.setText("Show All");
        btnShowAll.setBorderPainted(false);
        btnShowAll.setFont(CoeusFontFactory.getLabelFont());
        btnShowAll.setMaximumSize(new java.awt.Dimension(110, 22));
        btnShowAll.setMinimumSize(new java.awt.Dimension(110, 22));
        btnShowAll.setPreferredSize(new java.awt.Dimension(110, 22));
        btnShowAll.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(100, 4, 0, 0);
        add(btnShowAll, gridBagConstraints);

        btnVoid.setMnemonic('O');
        btnVoid.setText("Void");
        btnVoid.setAlignmentX(0.5F);
        btnVoid.setDisplayedMnemonicIndex(0);
        btnVoid.setFont(CoeusFontFactory.getLabelFont());
        btnVoid.setMaximumSize(new java.awt.Dimension(110, 22));
        btnVoid.setMinimumSize(new java.awt.Dimension(110, 22));
        btnVoid.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(73, 4, 0, 0);
        add(btnVoid, gridBagConstraints);

        btnShowActive.setMnemonic('h');
        btnShowActive.setText("Show Active");
        btnShowActive.setFont(CoeusFontFactory.getLabelFont());
        btnShowActive.setMaximumSize(new java.awt.Dimension(110, 22));
        btnShowActive.setMinimumSize(new java.awt.Dimension(110, 22));
        btnShowActive.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(100, 4, 0, 0);
        add(btnShowActive, gridBagConstraints);

        // JM 4-4-2013 added Delete button back
        btnDelete.setText("Delete");
        btnDelete.setAlignmentX(0.5F);
        btnDelete.setAutoscrolls(true);
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMargin(new java.awt.Insets(2, 4, 2, 5));
        btnDelete.setMaximumSize(new java.awt.Dimension(110, 22));
        btnDelete.setMinimumSize(new java.awt.Dimension(110, 22));
        btnDelete.setPreferredSize(new java.awt.Dimension(110, 22));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 10.0;
        gridBagConstraints.insets = new java.awt.Insets(130, 4, 0, 0);
        add(btnDelete, gridBagConstraints);
        // JM END

        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMargin(new java.awt.Insets(2, 4, 2, 14));
        btnModify.setMaximumSize(new java.awt.Dimension(110, 22));
        btnModify.setMinimumSize(new java.awt.Dimension(110, 22));
        btnModify.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(43, 4, 0, 0);
        add(btnModify, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.utils.CoeusButton btnAdd;
    // JM 4-4-2013 added Delete button back
    public edu.mit.coeus.utils.CoeusButton btnDelete;
    // JM END
    public edu.mit.coeus.utils.CoeusButton btnModify;
    public edu.mit.coeus.utils.CoeusButton btnShowActive;
    public edu.mit.coeus.utils.CoeusButton btnShowAll;
    public edu.mit.coeus.utils.CoeusButton btnVoid;
    private javax.swing.JPanel pnlListDocument;
    private javax.swing.JScrollPane scrPnListDocument;
    public javax.swing.JTable tblListDocument;
    // End of variables declaration//GEN-END:variables
 
    public static void main(String s[]){
        JFrame frame = new JFrame("Award Upload");
        AwardDocumentForm awardUploadDocumentForm = new AwardDocumentForm();
        frame.getContentPane().add(awardUploadDocumentForm);
        frame.setSize(1100, 620);
        frame.show();
     }
    
}
