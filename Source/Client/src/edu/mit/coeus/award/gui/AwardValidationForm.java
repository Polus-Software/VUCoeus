/*
 * AwardValidationForm.java
 *
 * Created on November 9, 2011, 3:24 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.LimitedPlainDocument;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author  satheeshkumarkn
 */
public class AwardValidationForm extends javax.swing.JPanel {
    
    private ImageIcon iIcnWarning = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.HISTORY_MODIFIED_ICON));
    // JM 10-7-2013 added error icon
    //private ImageIcon iIcnError = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON));
    private ImageIcon iIcnError = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.REJECT_ICON_PATH));
    private String headerValue;
    
    /** Creates new form AwardValidationForm */
    public AwardValidationForm(String headerValue) {
        this.headerValue = headerValue;
        initComponents();
        lblHeader.setText(headerValue);
    }
    
    /**
     * Method to add warning message component
     * @param warningMessage
     */
    // JM 10-7-2013 added parameter to indicate errors for alerts
    public void addWarningMessageComponent(String warningMessage,boolean isError){
        JPanel pnlValidationComponent = new JPanel();
        java.awt.GridBagConstraints gridBagConstraints;
        JLabel lblIcon = new JLabel();
        lblIcon.setIcon(iIcnWarning);
        if (isError) {
            lblIcon.setIcon(iIcnError);
        }
        
        javax.swing.JScrollPane scrPnWarningMessage = new javax.swing.JScrollPane();
        javax.swing.JTextArea txtWarningMsg = new javax.swing.JTextArea();
        
        pnlValidationComponent.setLayout(new java.awt.GridBagLayout());
        pnlValidationComponent.setBackground(new java.awt.Color(255, 255, 255));
        pnlValidationComponent.setMaximumSize(new java.awt.Dimension(525, 40));
        pnlValidationComponent.setMinimumSize(new java.awt.Dimension(525, 40));
        pnlValidationComponent.setPreferredSize(new java.awt.Dimension(525, 40));
        
        scrPnWarningMessage.setMaximumSize(new java.awt.Dimension(500, 40));
        scrPnWarningMessage.setMinimumSize(new java.awt.Dimension(500, 40));
        scrPnWarningMessage.setPreferredSize(new java.awt.Dimension(500, 40));
        scrPnWarningMessage.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        txtWarningMsg.setBackground(java.awt.Color.WHITE);
        txtWarningMsg.setDocument(new LimitedPlainDocument(4000));
        txtWarningMsg.setEditable(false);
        txtWarningMsg.setFont(CoeusFontFactory.getNormalFont());
        txtWarningMsg.setLineWrap(true);
        txtWarningMsg.setWrapStyleWord(true);
        txtWarningMsg.setBorder(null);
        txtWarningMsg.setText(warningMessage);
        txtWarningMsg.setCaretPosition(0);
        scrPnWarningMessage.setViewportView(txtWarningMsg);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 9, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlValidationComponent.add(scrPnWarningMessage, gridBagConstraints);
        
        lblIcon.setMaximumSize(new java.awt.Dimension(30, 16));
        lblIcon.setMinimumSize(new java.awt.Dimension(30, 16));
        lblIcon.setPreferredSize(new java.awt.Dimension(30, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        pnlValidationComponent.add(lblIcon, gridBagConstraints);
        pnlValidationComponent.setAlignmentX((float) 0);
        pnlValidationComponent.setAlignmentY((float) 0);
        pnlValidations.add(pnlValidationComponent);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnYes = new javax.swing.JButton();
        pnlHeader = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        scnValidations = new javax.swing.JScrollPane();
        pnlValidations = new javax.swing.JPanel();
        btnNo = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(750, 450));
        setMinimumSize(new java.awt.Dimension(750, 450));
        setPreferredSize(new java.awt.Dimension(750, 450));
        btnYes.setFont(CoeusFontFactory.getLabelFont());
        btnYes.setMnemonic('Y');
        // JM 11-29-2012 want button say OK; "No" button has been removed
        btnYes.setText("OK");
        // JM END
        btnYes.setMaximumSize(new java.awt.Dimension(75, 26));
        btnYes.setMinimumSize(new java.awt.Dimension(75, 26));
        btnYes.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 4);
        add(btnYes, gridBagConstraints);

        pnlHeader.setLayout(new javax.swing.BoxLayout(pnlHeader, javax.swing.BoxLayout.X_AXIS));

        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblHeader.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnlHeader.add(lblHeader);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlHeader, gridBagConstraints);

        scnValidations.setMinimumSize(new java.awt.Dimension(550, 450));
        scnValidations.setPreferredSize(new java.awt.Dimension(550, 450));
        pnlValidations.setLayout(new javax.swing.BoxLayout(pnlValidations, javax.swing.BoxLayout.Y_AXIS));

        pnlValidations.setBackground(new java.awt.Color(255, 255, 255));
        scnValidations.setViewportView(pnlValidations);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(scnValidations, gridBagConstraints);

        btnNo.setFont(CoeusFontFactory.getLabelFont());
        btnNo.setMnemonic('N');
        btnNo.setText("No");
        btnNo.setMaximumSize(new java.awt.Dimension(75, 26));
        btnNo.setMinimumSize(new java.awt.Dimension(75, 26));
        btnNo.setPreferredSize(new java.awt.Dimension(75, 26));
        btnNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 5);
        //add(btnNo, gridBagConstraints); // JM 11-29-2012 no choice for changing status needed

    }// </editor-fold>//GEN-END:initComponents
    
    private void btnNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_btnNoActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnNo;
    public javax.swing.JButton btnYes;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JPanel pnlHeader;
    public javax.swing.JPanel pnlValidations;
    private javax.swing.JScrollPane scnValidations;
    // End of variables declaration//GEN-END:variables
    
}
