/*
 * QuestionExplanationForm.java
 *
 * Created on September 9, 2008, 04:45 PM
 */

package edu.mit.coeus.questionnaire.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.JFrame;

/**
 *
 * @author  sreenathv
 */
public class QuestionExplanationForm extends javax.swing.JComponent {
    
    /** Creates new form QuestionExplanationForm */
    public QuestionExplanationForm() {
        initComponents();
        txtArQuestion.setLineWrap(true);
        txtArQuestion.setWrapStyleWord(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        tbdPnExplanation = new edu.mit.coeus.utils.CoeusTabbedPane(edu.mit.coeus.utils.CoeusTabbedPane.CTRL_T);
        scrPnQuestion = new javax.swing.JScrollPane();
        txtArQuestion = new javax.swing.JTextArea();
        scrPnExplanation = new javax.swing.JScrollPane();
        editorExplanation = new javax.swing.JEditorPane();
        scrPnPolicy = new javax.swing.JScrollPane();
        editorPolicy = new javax.swing.JEditorPane();
        scrPnRegulation = new javax.swing.JScrollPane();
        ediotrRegulation = new javax.swing.JEditorPane();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(625, 245));
        setPreferredSize(new java.awt.Dimension(625, 245));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnCancel, gridBagConstraints);

        tbdPnExplanation.setFont(CoeusFontFactory.getNormalFont());
        tbdPnExplanation.setMinimumSize(new java.awt.Dimension(522, 230));
        tbdPnExplanation.setPreferredSize(new java.awt.Dimension(522, 230));
        scrPnQuestion.setMinimumSize(new java.awt.Dimension(535, 230));
        scrPnQuestion.setPreferredSize(new java.awt.Dimension(535, 230));
        txtArQuestion.setFont(CoeusFontFactory.getNormalFont());
        scrPnQuestion.setViewportView(txtArQuestion);

        tbdPnExplanation.addTab("Question", scrPnQuestion);

        scrPnExplanation.setMinimumSize(new java.awt.Dimension(535, 230));
        scrPnExplanation.setPreferredSize(new java.awt.Dimension(535, 230));
        scrPnExplanation.setViewportView(editorExplanation);

        tbdPnExplanation.addTab("Explanation", scrPnExplanation);

        scrPnPolicy.setMinimumSize(new java.awt.Dimension(535, 230));
        scrPnPolicy.setPreferredSize(new java.awt.Dimension(535, 230));
        scrPnPolicy.setViewportView(editorPolicy);

        tbdPnExplanation.addTab("Policy", scrPnPolicy);

        scrPnRegulation.setMinimumSize(new java.awt.Dimension(535, 230));
        scrPnRegulation.setPreferredSize(new java.awt.Dimension(535, 230));
        scrPnRegulation.setViewportView(ediotrRegulation);

        tbdPnExplanation.addTab("Regulation", scrPnRegulation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(tbdPnExplanation, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JEditorPane ediotrRegulation;
    public javax.swing.JEditorPane editorExplanation;
    public javax.swing.JEditorPane editorPolicy;
    public javax.swing.JScrollPane scrPnExplanation;
    public javax.swing.JScrollPane scrPnPolicy;
    public javax.swing.JScrollPane scrPnQuestion;
    public javax.swing.JScrollPane scrPnRegulation;
    public edu.mit.coeus.utils.CoeusTabbedPane tbdPnExplanation;
    public javax.swing.JTextArea txtArQuestion;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String args[]){
        JFrame frame = new JFrame();
        frame.getContentPane().add(new QuestionExplanationForm());
        frame.setSize(610,245);
        frame.setVisible(true);
    }
}