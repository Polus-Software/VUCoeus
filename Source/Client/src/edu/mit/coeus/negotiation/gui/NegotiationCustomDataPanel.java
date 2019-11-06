/**
 *
 * @author  nadhgj
 */

package edu.mit.coeus.negotiation.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.customelements.CustomElementsForm;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;


public class NegotiationCustomDataPanel extends javax.swing.JPanel {
    
    /** Creates new form JPanel */
    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    private CustomElementsForm customElementsForm;
    //Case :#3149 - End
  
    public NegotiationCustomDataPanel() {
             initComponents();
       
    }
    
    public void setCustomDataReference(edu.mit.coeus.utils.customelements.CustomElementsForm customElementsForm){
         
         scrPnPanel.setViewportView(customElementsForm);
         scrPnPanel.setViewportView(customElementsForm);
         //Case :#3149 – Tabbing between fields does not work on others tabs - Start
          this.customElementsForm = customElementsForm;
          customElementsForm.setFocusToNegotiation(true);
          customElementsForm.setTabFocus();
         //Case :#3149 - End
     }    
    
    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    /**
     *Method to get CustomElementsForm object
     */
     public CustomElementsForm getCustomElementsForm(){
         return customElementsForm;
     }
     //Case :#3149 - End
     
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnPanel = new javax.swing.JScrollPane();
        pnlOkCancelButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(766, 560));
        setPreferredSize(new java.awt.Dimension(745, 560));
        scrPnPanel.setMinimumSize(new java.awt.Dimension(800, 400));
        scrPnPanel.setPreferredSize(new java.awt.Dimension(750, 400));
        scrPnPanel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 9, 0, 0);
        add(scrPnPanel, gridBagConstraints);

        pnlOkCancelButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(106, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(106, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(85, 26));
        pnlOkCancelButtons.add(btnOk, new java.awt.GridBagConstraints());

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(106, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(106, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        pnlOkCancelButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);
        add(pnlOkCancelButtons, gridBagConstraints);

    }//GEN-END:initComponents

   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JPanel pnlOkCancelButtons;
    public javax.swing.JScrollPane scrPnPanel;
    // End of variables declaration//GEN-END:variables
    
}
