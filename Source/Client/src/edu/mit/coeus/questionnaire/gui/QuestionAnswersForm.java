/*
 * QuestionAnswersForm.java
 *
 * Created on September 29, 2006, 4:42 PM
 */

package edu.mit.coeus.questionnaire.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

/**
 *
 * @author  tarique
 */
public class QuestionAnswersForm extends javax.swing.JComponent {
    
    /** Creates new form QuestionAnswersForm */
    public QuestionAnswersForm() {
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

        splitPaneQuestionnaireAnswers = new javax.swing.JSplitPane();
        pnlMenus = new javax.swing.JPanel();
        scrlMenus = new javax.swing.JScrollPane();
        tblMenus = new javax.swing.JTable();
        pnlQuestionsAnswers = new javax.swing.JPanel();
        btnGoBack = new javax.swing.JButton();
        scrPnQuestions = new javax.swing.JScrollPane();
        tblQuestions = new javax.swing.JTable();
        btnSaveAndProceed = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnStartOver = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnSaveAndComplete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(1155, 800));
        setPreferredSize(new java.awt.Dimension(1155, 800));
        splitPaneQuestionnaireAnswers.setBorder(null);
        pnlMenus.setLayout(new java.awt.GridBagLayout());

        pnlMenus.setMinimumSize(new java.awt.Dimension(100, 300));
        pnlMenus.setPreferredSize(new java.awt.Dimension(100, 300));
        scrlMenus.setMinimumSize(new java.awt.Dimension(300, 402));
        scrlMenus.setPreferredSize(new java.awt.Dimension(300, 402));
        tblMenus.setModel(new javax.swing.table.DefaultTableModel(
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
        scrlMenus.setViewportView(tblMenus);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlMenus.add(scrlMenus, gridBagConstraints);

        splitPaneQuestionnaireAnswers.setLeftComponent(pnlMenus);

        pnlQuestionsAnswers.setLayout(new java.awt.GridBagLayout());

        pnlQuestionsAnswers.setMinimumSize(new java.awt.Dimension(1050, 670));
        pnlQuestionsAnswers.setPreferredSize(new java.awt.Dimension(1050, 670));
        btnGoBack.setFont(CoeusFontFactory.getLabelFont());
        btnGoBack.setMnemonic('B');
        btnGoBack.setText("Go Back");
        btnGoBack.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnGoBack.setMaximumSize(new java.awt.Dimension(115, 23));
        btnGoBack.setMinimumSize(new java.awt.Dimension(115, 23));
        btnGoBack.setPreferredSize(new java.awt.Dimension(115, 23));
        btnGoBack.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(65, 0, 0, 0);
        pnlQuestionsAnswers.add(btnGoBack, gridBagConstraints);

        scrPnQuestions.setMinimumSize(new java.awt.Dimension(625, 535));
        scrPnQuestions.setOpaque(false);
        scrPnQuestions.setPreferredSize(new java.awt.Dimension(650, 535));
        scrPnQuestions.setRequestFocusEnabled(false);
        tblQuestions.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblQuestions.setFont(CoeusFontFactory.getLabelFont());
        tblQuestions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblQuestions.setOpaque(false);
        tblQuestions.setRowMargin(3);
        tblQuestions.setRowSelectionAllowed(false);
        tblQuestions.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblQuestions.setSelectionForeground(new java.awt.Color(204, 204, 204));
        tblQuestions.setShowHorizontalLines(false);
        tblQuestions.setShowVerticalLines(false);
        scrPnQuestions.setViewportView(tblQuestions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlQuestionsAnswers.add(scrPnQuestions, gridBagConstraints);

        btnSaveAndProceed.setFont(CoeusFontFactory.getLabelFont());
        btnSaveAndProceed.setMnemonic('P');
        btnSaveAndProceed.setText("Save & Proceed");
        btnSaveAndProceed.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnSaveAndProceed.setMaximumSize(new java.awt.Dimension(115, 23));
        btnSaveAndProceed.setMinimumSize(new java.awt.Dimension(115, 23));
        btnSaveAndProceed.setPreferredSize(new java.awt.Dimension(115, 23));
        btnSaveAndProceed.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        pnlQuestionsAnswers.add(btnSaveAndProceed, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('m');
        btnModify.setText("Modify");
        btnModify.setMaximumSize(new java.awt.Dimension(115, 23));
        btnModify.setMinimumSize(new java.awt.Dimension(115, 23));
        btnModify.setPreferredSize(new java.awt.Dimension(115, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(95, 2, 0, 2);
        pnlQuestionsAnswers.add(btnModify, gridBagConstraints);

        btnStartOver.setFont(CoeusFontFactory.getLabelFont());
        btnStartOver.setMnemonic('t');
        btnStartOver.setText("Start Over");
        btnStartOver.setMaximumSize(new java.awt.Dimension(115, 23));
        btnStartOver.setMinimumSize(new java.awt.Dimension(115, 23));
        btnStartOver.setPreferredSize(new java.awt.Dimension(115, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(125, 2, 0, 2);
        pnlQuestionsAnswers.add(btnStartOver, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(115, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(115, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(115, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(155, 2, 0, 2);
        pnlQuestionsAnswers.add(btnClose, gridBagConstraints);

        btnSaveAndComplete.setFont(CoeusFontFactory.getLabelFont());
        btnSaveAndComplete.setMnemonic('P');
        btnSaveAndComplete.setText("Save & Complete");
        btnSaveAndComplete.setEnabled(false);
        btnSaveAndComplete.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnSaveAndComplete.setMaximumSize(new java.awt.Dimension(115, 23));
        btnSaveAndComplete.setMinimumSize(new java.awt.Dimension(115, 23));
        btnSaveAndComplete.setPreferredSize(new java.awt.Dimension(115, 23));
        btnSaveAndComplete.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(35, 0, 0, 0);
        pnlQuestionsAnswers.add(btnSaveAndComplete, gridBagConstraints);

        splitPaneQuestionnaireAnswers.setRightComponent(pnlQuestionsAnswers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(splitPaneQuestionnaireAnswers, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnGoBack;
    public javax.swing.JButton btnModify;
    public javax.swing.JButton btnSaveAndComplete;
    public javax.swing.JButton btnSaveAndProceed;
    public javax.swing.JButton btnStartOver;
    public javax.swing.JPanel pnlMenus;
    public javax.swing.JPanel pnlQuestionsAnswers;
    public javax.swing.JScrollPane scrPnQuestions;
    public javax.swing.JScrollPane scrlMenus;
    public javax.swing.JSplitPane splitPaneQuestionnaireAnswers;
    public javax.swing.JTable tblMenus;
    public javax.swing.JTable tblQuestions;
    // End of variables declaration//GEN-END:variables
    
}
