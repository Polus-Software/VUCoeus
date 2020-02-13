/*
 * JustificationForm.java
 *
 * Created on September 6, 2003, 4:28 PM
 */

package edu.mit.coeus.budget.gui;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.JTableHeader;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;

/**
 *
 * @author  Vyjayanthi
 */
public class JustificationForm extends JComponent implements ActionListener, KeyListener {
    
    private CoeusDlgWindow coeusDlgWindow;
    private static final String TITLE = "Budget Justification";
    
    /** Creates new form JustificationForm */
    public JustificationForm() {
        initComponents();
        postInitComponenets();        
    }
    
    private void postInitComponenets() {
        //setting up dialog
        coeusDlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), TITLE, true);
        coeusDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.HIDE_ON_CLOSE);
        coeusDlgWindow.getContentPane().add( this );
        coeusDlgWindow.setResizable(false);
        coeusDlgWindow.pack();
        coeusDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        
        txtArJustification.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        
        //registering with event handlers
        coeusDlgWindow.addKeyListener(this);
        btnOk.addActionListener(this);
    }
    
    public void setEditable(boolean editable) {
        txtArJustification.setEditable(editable);
    }
    
    public void display() {
        coeusDlgWindow.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnJustification = new javax.swing.JScrollPane();
        txtArJustification = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        lblJustification = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        scrPnJustification.setMinimumSize(new java.awt.Dimension(450, 125));
        scrPnJustification.setPreferredSize(new java.awt.Dimension(450, 125));
        txtArJustification.setFont(CoeusFontFactory.getNormalFont());
        txtArJustification.setLineWrap(true);
        scrPnJustification.setViewportView(txtArJustification);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(scrPnJustification, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(81, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(66, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(66, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        add(btnOk, gridBagConstraints);

        lblJustification.setBackground(new java.awt.Color(212, 208, 200));
        lblJustification.setFont(CoeusFontFactory.getLabelFont());
        lblJustification.setForeground(java.awt.Color.red);
        lblJustification.setText("Justification:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        add(lblJustification, gridBagConstraints);

    }//GEN-END:initComponents
        
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if ( source.equals(btnOk) ){
            coeusDlgWindow.setVisible(false);
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblJustification;
    public javax.swing.JScrollPane scrPnJustification;
    public javax.swing.JTextArea txtArJustification;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String s[]) {
        JustificationForm form = new JustificationForm();
        form.display();
    }
    
    public void keyPressed(KeyEvent keyEvent) {
    }
    
    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
            coeusDlgWindow.setVisible(false);
        }
    }
    
    public void keyTyped(KeyEvent keyEvent) {
    }
    
    /** Getter for property justificationText.
     * @return Value of property justificationText.
     */
    public String getJustificationText() {
        return txtArJustification.getText();
    }
    
    /** Setter for property justificationText.
     * @param justificationText New value of property justificationText.
     */
    public void setJustificationText(String justificationText) {
        txtArJustification.setText(justificationText);
    }
    
}
