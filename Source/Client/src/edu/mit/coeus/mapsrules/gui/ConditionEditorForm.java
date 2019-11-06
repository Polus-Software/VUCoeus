/*
 * ConditionEditor.java
 *
 * Created on October 25, 2005, 12:15 PM
 */

package edu.mit.coeus.mapsrules.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.LimitedPlainDocument;
import javax.swing.JFrame;

/**
 *
 * @author  surekhan
 */
public class ConditionEditorForm extends javax.swing.JPanel {
    /** Creates new form ConditionEditor */
    public ConditionEditorForm() {
        initComponents();
    }
    
   
    
    public static void main(String args[]) {
        ConditionEditorForm conditionEditorForm = new ConditionEditorForm();
        JFrame frame= new JFrame();
        frame.setSize(658, 470);
        frame.getContentPane().add(conditionEditorForm);
        frame.show();
    }
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlDescription = new javax.swing.JPanel();
        lblDescription = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        lblCondtion = new javax.swing.JLabel();
        lblConditionValue = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        scrPnUserMessage = new javax.swing.JScrollPane();
        txtArUserMessage = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnMap = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnRules = new javax.swing.JScrollPane();
        tblRules = new javax.swing.JTable();
        tbdPnConditions = new edu.mit.coeus.utils.CoeusTabbedPane();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        pnlDescription.setLayout(new java.awt.GridBagLayout());

        pnlDescription.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlDescription.setMinimumSize(new java.awt.Dimension(545, 120));
        pnlDescription.setPreferredSize(new java.awt.Dimension(545, 120));
        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDescription.setText("Description: ");
        lblDescription.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblDescription.setMaximumSize(new java.awt.Dimension(90, 20));
        lblDescription.setMinimumSize(new java.awt.Dimension(90, 20));
        lblDescription.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        pnlDescription.add(lblDescription, gridBagConstraints);

        txtDescription.setDocument(new LimitedPlainDocument(200));
        txtDescription.setFont(CoeusFontFactory.getNormalFont());
        txtDescription.setMinimumSize(new java.awt.Dimension(490, 20));
        txtDescription.setPreferredSize(new java.awt.Dimension(490, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 10);
        pnlDescription.add(txtDescription, gridBagConstraints);

        lblCondtion.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlDescription.add(lblCondtion, gridBagConstraints);

        lblConditionValue.setMinimumSize(new java.awt.Dimension(70, 15));
        lblConditionValue.setPreferredSize(new java.awt.Dimension(70, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 10);
        pnlDescription.add(lblConditionValue, gridBagConstraints);

        jLabel2.setFont(CoeusFontFactory.getLabelFont());
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("User Message: ");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel2.setMaximumSize(new java.awt.Dimension(90, 20));
        jLabel2.setMinimumSize(new java.awt.Dimension(90, 20));
        jLabel2.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        pnlDescription.add(jLabel2, gridBagConstraints);

        scrPnUserMessage.setMinimumSize(new java.awt.Dimension(490, 50));
        scrPnUserMessage.setName("scrPnUserMessage");
        scrPnUserMessage.setPreferredSize(new java.awt.Dimension(490, 50));
        txtArUserMessage.setColumns(20);
        txtArUserMessage.setDocument(new LimitedPlainDocument(4000));
        txtArUserMessage.setFont(CoeusFontFactory.getNormalFont()
        );
        txtArUserMessage.setLineWrap(true);
        txtArUserMessage.setName("txtArUserMessage");
        scrPnUserMessage.setViewportView(txtArUserMessage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlDescription.add(scrPnUserMessage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(pnlDescription, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(79, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(79, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(79, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 3);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(79, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(79, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(79, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 3);
        add(btnCancel, gridBagConstraints);

        btnMap.setFont(CoeusFontFactory.getLabelFont());
        btnMap.setMnemonic('M');
        btnMap.setText("Map");
        btnMap.setMaximumSize(new java.awt.Dimension(79, 23));
        btnMap.setMinimumSize(new java.awt.Dimension(79, 23));
        btnMap.setPreferredSize(new java.awt.Dimension(79, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 3);
        add(btnMap, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont()
        );
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(79, 23));
        btnAdd.setMinimumSize(new java.awt.Dimension(79, 23));
        btnAdd.setPreferredSize(new java.awt.Dimension(79, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 6, 0, 3);
        add(btnAdd, gridBagConstraints);

        btnInsert.setFont(CoeusFontFactory.getLabelFont());
        btnInsert.setMnemonic('I');
        btnInsert.setText("Insert");
        btnInsert.setMaximumSize(new java.awt.Dimension(79, 23));
        btnInsert.setMinimumSize(new java.awt.Dimension(79, 23));
        btnInsert.setPreferredSize(new java.awt.Dimension(79, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 3);
        add(btnInsert, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(79, 23));
        btnDelete.setMinimumSize(new java.awt.Dimension(79, 23));
        btnDelete.setPreferredSize(new java.awt.Dimension(79, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 3);
        add(btnDelete, gridBagConstraints);

        scrPnRules.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnRules.setMinimumSize(new java.awt.Dimension(555, 140));
        scrPnRules.setPreferredSize(new java.awt.Dimension(555, 140));
        scrPnRules.setRequestFocusEnabled(false);
        tblRules.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnRules.setViewportView(tblRules);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(scrPnRules, gridBagConstraints);

        tbdPnConditions.setMinimumSize(new java.awt.Dimension(660, 220));
        tbdPnConditions.setPreferredSize(new java.awt.Dimension(660, 220));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(tbdPnConditions, gridBagConstraints);

        jLabel1.setFont(CoeusFontFactory.getLabelFont());
        jLabel1.setText("Conditions");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        add(jLabel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnInsert;
    public javax.swing.JButton btnMap;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel lblConditionValue;
    public javax.swing.JLabel lblCondtion;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JPanel pnlDescription;
    public javax.swing.JScrollPane scrPnRules;
    public javax.swing.JScrollPane scrPnUserMessage;
    public edu.mit.coeus.utils.CoeusTabbedPane tbdPnConditions;
    public javax.swing.JTable tblRules;
    public javax.swing.JTextArea txtArUserMessage;
    public javax.swing.JTextField txtDescription;
    // End of variables declaration//GEN-END:variables
    
}
