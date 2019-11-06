/*
 * InvestigatorUnitAdminTypeForm.java
 *
 * Created on September 25, 2006, 4:49 PM
 */

package edu.mit.coeus.utils.investigator.invUnitAdminType;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.TypeConstants;

/**
 *
 * @author  tarique
 */
public class InvestigatorUnitAdminTypeForm extends javax.swing.JComponent {
    
    /** Creates new form InvestigatorUnitAdminTypeForm */
    public InvestigatorUnitAdminTypeForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tbdPnUnitForm = new edu.mit.coeus.utils.CoeusTabbedPane();
        scrpnAdministrators = new javax.swing.JScrollPane();
        pnlAdministrators = new javax.swing.JPanel();
        scrPnAdminType = new javax.swing.JScrollPane();
        tblAdminType = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                if (editCellAt(row, column)){
                    setRowSelectionInterval(row,row);
                    setColumnSelectionInterval(column,column);
                    getEditorComponent().requestFocusInWindow();
                }
            }};
            btnAdd = new javax.swing.JButton();
            btnSync = new javax.swing.JButton();
            btnDelete = new javax.swing.JButton();
            pnlButtons = new javax.swing.JPanel();
            btnOk = new javax.swing.JButton();
            btnCancel = new javax.swing.JButton();

            setLayout(new java.awt.GridBagLayout());

            scrpnAdministrators.setMinimumSize(new java.awt.Dimension(500, 300));
            scrpnAdministrators.setPreferredSize(new java.awt.Dimension(500, 300));
            pnlAdministrators.setLayout(new java.awt.GridBagLayout());

            scrPnAdminType.setBorder(null);
            scrPnAdminType.setMinimumSize(new java.awt.Dimension(390, 265));
            scrPnAdminType.setPreferredSize(new java.awt.Dimension(390, 265));
            scrPnAdminType.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    scrPnAdminTypeFocusGained(evt);
                }
            });

            tblAdminType.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {

                }
            ));
            scrPnAdminType.setViewportView(tblAdminType);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridheight = 5;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
            pnlAdministrators.add(scrPnAdminType, gridBagConstraints);

            btnAdd.setFont(CoeusFontFactory.getLabelFont());
            btnAdd.setMnemonic('a');
            btnAdd.setText("Add");
            btnAdd.setMaximumSize(new java.awt.Dimension(73, 23));
            btnAdd.setMinimumSize(new java.awt.Dimension(73, 23));
            btnAdd.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlAdministrators.add(btnAdd, gridBagConstraints);

            btnSync.setFont(CoeusFontFactory.getLabelFont());
            btnSync.setMnemonic('y');
            btnSync.setText("Sync");
            btnSync.setMaximumSize(new java.awt.Dimension(73, 23));
            btnSync.setMinimumSize(new java.awt.Dimension(73, 23));
            btnSync.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlAdministrators.add(btnSync, gridBagConstraints);

            btnDelete.setFont(CoeusFontFactory.getLabelFont());
            btnDelete.setMnemonic('d');
            btnDelete.setText("Delete");
            btnDelete.setMaximumSize(new java.awt.Dimension(73, 23));
            btnDelete.setMinimumSize(new java.awt.Dimension(73, 23));
            btnDelete.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlAdministrators.add(btnDelete, gridBagConstraints);

            scrpnAdministrators.setViewportView(pnlAdministrators);

            tbdPnUnitForm.addTab(" Administrators", scrpnAdministrators);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            add(tbdPnUnitForm, gridBagConstraints);

            pnlButtons.setLayout(new java.awt.GridBagLayout());

            btnOk.setFont(CoeusFontFactory.getLabelFont());
            btnOk.setMnemonic('o');
            btnOk.setText("OK");
            btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
            btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
            btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
            pnlButtons.add(btnOk, gridBagConstraints);

            btnCancel.setFont(CoeusFontFactory.getLabelFont());
            btnCancel.setMnemonic('n');
            btnCancel.setText("Cancel");
            btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
            btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
            btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
            pnlButtons.add(btnCancel, gridBagConstraints);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            add(pnlButtons, gridBagConstraints);

        }//GEN-END:initComponents

    private void scrPnAdminTypeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_scrPnAdminTypeFocusGained
        if(tblAdminType.getRowCount() > 0){
            tblAdminType.setRowSelectionInterval(0,0);
            tblAdminType.setColumnSelectionInterval(1,1);
            tblAdminType.scrollRectToVisible(
                            tblAdminType.getCellRect(0 ,1, true));
            tblAdminType.editCellAt(0, 1);
            if(tblAdminType.getEditorComponent()!=null){
                tblAdminType.getEditorComponent().requestFocusInWindow();
            }else{
                btnCancel.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_scrPnAdminTypeFocusGained
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnSync;
    public javax.swing.JPanel pnlAdministrators;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JScrollPane scrPnAdminType;
    public javax.swing.JScrollPane scrpnAdministrators;
    public edu.mit.coeus.utils.CoeusTabbedPane tbdPnUnitForm;
    public javax.swing.JTable tblAdminType;
    // End of variables declaration//GEN-END:variables
    
}
