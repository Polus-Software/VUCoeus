/*
 * QuestionnaireForm.java
 *
 * Created on October August 31, 2007, 12:54 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.mapsrules.gui;

/**
 *
 * @author  leenababu
 */
public class QuestionnaireForm extends javax.swing.JPanel {
    
    /** Creates new form QuestionnaireForm */
    public QuestionnaireForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        tblQuestionnaire = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(570, 175));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(725, 200));
        tblQuestionnaire.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Questionnaire"
            }
        ));
        tblQuestionnaire.setName("tblQuestionnaire");
        tblQuestionnaire.setOpaque(false);
        jScrollPane1.setViewportView(tblQuestionnaire);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        add(jScrollPane1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tblQuestionnaire;
    // End of variables declaration//GEN-END:variables
    
}
