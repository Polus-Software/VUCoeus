/*
 * UserPreferencesForm.java
 *
 * Created on June 27, 2003, 1:23 AM
 */

package edu.mit.coeus.user.gui;

import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.*;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.UserUtils;

/**
 *
 * @author  senthil
 */
public class UserPreferencesForm extends javax.swing.JComponent {
    
    private char functionType;
    private Vector vecUserPreferences = new Vector();
    private boolean saveRequired;
    private UserPreferencesController userPreferencesController;
    
    private UserPreferencesTableModel userPreferencesTableModel;
    private CoeusDlgWindow userPreferencesDialog;
    
    private edu.mit.coeus.gui.CoeusAppletMDIForm mdiForm = edu.mit.coeus.utils.CoeusGuiConstants.getMDIForm();
    private JFrame parent;
    private boolean modal;
    private boolean dirty = false;
    
    private static final String TITLE = "User Preferences";
    private static final String MESSAGE = "Message";
    private static final String PREFERENCES_FOR = "User Preferences for ";
    private static final String SAVE_CHANGES = "Do you want to save the changes?";
    private static final String CONFIRM = "Confirm";
    private static final String UPDATE = "U";
    private static final String INSERT = "I";
    private static final String EMPTY = "";

    private static final int WIDTH = 460;
    private static final int HEIGHT = 350;

    /** Creates new form UserPreferencesForm */
    public UserPreferencesForm(JFrame parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initComponents();
        scrPnPreferences.setBorder(new javax.swing.border.EtchedBorder(EtchedBorder.LOWERED));
        postInitComponents();
    }
    
    /** Creates new form UserPreferencesForm
     * @param vecUserPreferences Collection of UserPreferencesBean */
    private UserPreferencesForm(Vector vecUserPreferences) {
        this.vecUserPreferences = vecUserPreferences;
        this.functionType = 'U';
        initComponents();
        scrPnPreferences.setBorder(new javax.swing.border.EtchedBorder(EtchedBorder.LOWERED));
        postInitComponents();
    }
    
    /** Creates new form UserPreferencesForm
     * @param userId user Id */
    public UserPreferencesForm(String userId, JFrame parent, boolean modal) {
        this(parent, modal);
        
        postInitComponents();
        
        loadUserPreferences(userId);
        
    }
    
    /** Loads User Preferences for the User Id.
     * @param userId user Id. */
    public void loadUserPreferences(String userId) {
        String PUBLIC_MESSAGE = "Public Message";

        userPreferencesDialog.setChanged(false);
        
        if(userPreferencesController == null) {
            userPreferencesController = new UserPreferencesController();
        }
        vecUserPreferences = userPreferencesController.displayUserPreferencesInfo(userId);
        
//Modified by Jinu on 25/01/2005 to implement the Public Message
//**************************************************************
        Vector selected = new Vector();
        Vector notSelected = new Vector();
        
        Vector tempSelected = (Vector)vecUserPreferences.get(0);
        Vector tempNotSelected = (Vector)vecUserPreferences.get(1);
        if(tempSelected != null){
            for(int count=0;count<tempSelected.size();count++){
                UserPreferencesBean bean = (UserPreferencesBean)tempSelected.get(count);
                if(bean != null && bean.getVariableName()!=null && !bean.getVariableName().equals(PUBLIC_MESSAGE)){
                    selected.add(bean);
                }
            }
        }
        if(tempNotSelected != null){
            for(int count=0;count<tempNotSelected.size();count++){
                UserPreferencesBean bean = (UserPreferencesBean)tempNotSelected.get(count);
                if(bean != null && bean.getVariableName()!=null && !bean.getVariableName().equals(PUBLIC_MESSAGE)){
                    notSelected.add(bean);
                }
            }
        }
//**************************************************************
//End Jinu.        
        Vector temp = new Vector();
        
        if(selected != null) {
            for(int count = 0; count < selected.size(); count++) {
                UserPreferencesBean bean = (UserPreferencesBean)selected.get(count);
                if(bean.getVarValue() == null) bean.setVarValue(EMPTY);
                bean.setAcType(UPDATE);
            }
            temp.addAll(selected);
        }
        
        if(notSelected != null) {
            for(int count = 0; count < notSelected.size(); count++) {
                UserPreferencesBean bean = (UserPreferencesBean)notSelected.get(count);
                bean.setAcType(INSERT);
                bean.setUpdateUser(mdiForm.getUserName());
                bean.setUserId(userId);
                bean.setVarValue(EMPTY);
            }
            temp.addAll(notSelected);
        }
        
        vecUserPreferences = temp;
        
        tblPreferences.editCellAt(0,0);
        userPreferencesTableModel.fireTableDataChanged();
    }
    
    /** This method is called from within the constructor to
     * initialize the form when all data is ready to be displayed.
     */
    private void postInitComponents(){
        
        tblPreferences.setBackground((Color)UIManager.getDefaults().get("Pane.background"));
        tblPreferences.setRowHeight(20);
        tblPreferences.getTableHeader().setReorderingAllowed( false );
        tblPreferences.setTableHeader(null);
        tblPreferences.setFont(CoeusFontFactory.getNormalFont());
        //Setting Model
        userPreferencesTableModel = new UserPreferencesTableModel();
        tblPreferences.setModel(userPreferencesTableModel);
        
        //Setting Editors
        tblPreferences.getColumnModel().getColumn(1).setCellEditor(new UserPreferencesTableCellEditor());
        tblPreferences.getColumnModel().getColumn(1).setCellRenderer(new UserPreferencesTableCellRenderer());
        
        tblPreferences.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        userPreferencesDialog = new CoeusDlgWindow(parent,modal);
        userPreferencesDialog.setTitle(TITLE);
        userPreferencesDialog.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent windowEvent){
                if( tblPreferences.getRowCount() > 0 ){
                       tblPreferences.requestFocusInWindow();
                       tblPreferences.setRowSelectionInterval(0,0);
                       tblPreferences.editCellAt(0,1);
                       tblPreferences.getEditorComponent().requestFocusInWindow();
                }else{
                    btnCancel.requestFocusInWindow();
                    btnCancel.requestFocusInWindow();
                }
            }
            public void windowClosing(WindowEvent windowEvent) {
                if(userPreferencesDialog.isChanged()) {
                    int value = JOptionPane.showConfirmDialog(mdiForm,SAVE_CHANGES,CONFIRM,JOptionPane.YES_NO_CANCEL_OPTION);
                    if(value == JOptionPane.YES_OPTION) {
                        updateUserPreferences();
                        userPreferencesDialog.setVisible(false);
                    }
                    else if(value == JOptionPane.NO_OPTION)
                    {
                        userPreferencesDialog.setVisible(false);
                    }
                    else if(value == JOptionPane.CANCEL_OPTION)
                    {
                        //DO NOTHING
                        return ;
                    }
                }
                else
                {
                    userPreferencesDialog.setVisible(false);
                }
            }
        });
        userPreferencesDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        userPreferencesDialog.getContentPane().add(this);
        userPreferencesDialog.setSize(WIDTH,HEIGHT);
        userPreferencesDialog.setResizable(false);
    }
    
    /** Displays the UserPreferencesForm as a dialog.
     */
    public void display() {
        dirty = false;
        userPreferencesDialog.setLocation(CoeusDlgWindow.CENTER);
        
        userPreferencesDialog.setVisible(true);
    }
    
    /** sets the user name for whom the preferences are displayed.
     * @param userName User Name
     */
    public void setUserName(String userName) {
//        lblUserPreferences.setText(PREFERENCES_FOR+userName);
        /*
         * UserID to UserName Enhancement - Start
         * Added UserUtils class to change userid to username
         */
        lblUserPreferences.setText(PREFERENCES_FOR+UserUtils.getDisplayName(userName));
        // UserId to UserName - End
    }
    
    private void updateUserPreferences() {
        try{
            /*UserPreferencesBean bean;
            for(int count = 0; ;)
            {
                if(count >= vecUserPreferences.size()) break;
             
                bean = (UserPreferencesBean)vecUserPreferences.get(count);
                if(bean.getVarValue().trim().equals(EMPTY))
                {
                    vecUserPreferences.remove(count);
                    continue;
                }
                count++;
            }*/
            userPreferencesController.update(vecUserPreferences);
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),MESSAGE,JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jTextArea1 = new javax.swing.JTextArea();
        lblUserPreferences = new javax.swing.JLabel();
        scrPnPreferences = new javax.swing.JScrollPane();
        tblPreferences = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(650, 400));
        setMinimumSize(new java.awt.Dimension(650, 200));
        setPreferredSize(new java.awt.Dimension(650, 350));
        lblUserPreferences.setFont(CoeusFontFactory.getLabelFont());
        lblUserPreferences.setText("User Preferences for ");
        lblUserPreferences.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(lblUserPreferences, gridBagConstraints);

        scrPnPreferences.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnPreferences.setMaximumSize(new java.awt.Dimension(400, 325));
        scrPnPreferences.setMinimumSize(new java.awt.Dimension(350, 300));
        scrPnPreferences.setPreferredSize(new java.awt.Dimension(400, 325));
        tblPreferences.setBackground(new java.awt.Color(204, 204, 204));
        tblPreferences.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
            }
        ));
        scrPnPreferences.setViewportView(tblPreferences);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(scrPnPreferences, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(90, 30));
        btnOk.setMinimumSize(new java.awt.Dimension(90, 30));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 30));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(90, 30));
        btnCancel.setMinimumSize(new java.awt.Dimension(90, 30));
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 30));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    /** This method is invoked when OK button is clicked.
     * @param evt Action Event.
     */
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        if(userPreferencesDialog.isChanged())
        {
            updateUserPreferences();
        }
        userPreferencesDialog.setVisible(false);
    }//GEN-LAST:event_btnOkActionPerformed
    
    /** This method is invoked when Cancel Button is Clicked.
     * @param evt ActionEvent
     */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        if(dirty) {
            int selection = CoeusOptionPane.showQuestionDialog(SAVE_CHANGES, CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                updateUserPreferences();
            }
            else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                return ;
            }
        }
        userPreferencesDialog.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblUserPreferences;
    private javax.swing.JScrollPane scrPnPreferences;
    private javax.swing.JTable tblPreferences;
    // End of variables declaration//GEN-END:variables
    
    /** For testing purpose only
     * Sharath  8-July-2003
     * @param s Command Line Arguments
     */
    public static void main(String s[]) {
        UserPreferencesBean bean1 = new UserPreferencesBean();
        bean1.setVariableName("ONE");
        bean1.setVarValue("1");
        bean1.setVarDescription("ONE Description");
        
        UserPreferencesBean bean2 = new UserPreferencesBean();
        bean2.setVariableName("TWO");
        bean2.setVarValue("2");
        bean2.setVarDescription("Two Description");
        
        Vector dataVec = new Vector();
        dataVec.add(bean1);
        dataVec.add(bean2);
        
        UserPreferencesForm prefs = new UserPreferencesForm(dataVec);
        prefs.setUserName("Person 1");
        
        /*javax.swing.JFrame jf = new javax.swing.JFrame();
        jf.getContentPane().add(prefs.pnlPref);
        jf.setSize(400,300);
        jf.setVisible(true);
         */
        prefs.display();
    }
    
    /**Inner Custom Table Cell Editor for User Preferences Form.
     */
    class UserPreferencesTableCellEditor
    extends AbstractCellEditor
    implements TableCellEditor, java.awt.event.ItemListener {
        //holds User Preferences Collection.
        //private Vector vecUserPreferences;
        private JComboBox cmbValue;
        
        private static final String YES = "Yes";
        private static final String NO = "No";
        private static final String NOTHING = "";
        
        /** Constructs new Table Cell Editor.
         * @param vecUserPreferences Collection of User Preferences.
         */
        UserPreferencesTableCellEditor(Vector vecUserPreferences) {
//          super(new JTextField());
            cmbValue = new JComboBox();
            cmbValue.addItem(NOTHING);
            cmbValue.addItem(YES);
            cmbValue.addItem(NO);
            cmbValue.addItemListener(this);
            //this.vecUserPreferences = vecUserPreferences;
        }
        
        /** Constructs new Table Cell Editor.
         */
        UserPreferencesTableCellEditor() {
//            super(new JTextField());
            cmbValue = new JComboBox();
            cmbValue.addItem(NOTHING);
            cmbValue.addItem(YES);
            cmbValue.addItem(NO);
            cmbValue.addItemListener(this);
        }
        
        public int getClickCountToStart() {
            return 1;
        }
        
        /** returns the Editor Component.
         * @param value the value of the cell to be edited; it is up to the specific editor to interpret and draw the value. For example, if value is the string "true", it could be rendered as a string or it could be rendered as a check box that is checked. null is a valid value
         * @param isSelected true if the cell is to be rendered with highlighting
         * @param row the row of the cell being edited
         * @param column the column of the cell being edited
         * @param jTable the JTable that is asking the editor to edit; can be null
         * @return Component
         */
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
            String toolTip = ((UserPreferencesBean)vecUserPreferences.get(row)).getVarDescription();
            String item = ((UserPreferencesBean)vecUserPreferences.get(row)).getVarValue();
            cmbValue.setSelectedItem(item);
            cmbValue.setToolTipText(toolTip);
            return cmbValue;
        }
        
        /** listens to Combo Box Item Selection Changes.
         * @param itemEvent Item Event
         */
        public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
            dirty = true;
            int row = tblPreferences.getSelectedRow();
            UserPreferencesBean dataBean;
            dataBean = ((UserPreferencesBean)vecUserPreferences.get(row));
            dataBean.setVarValue(cmbValue.getSelectedItem().toString());
            userPreferencesDialog.setChanged(true);
            userPreferencesTableModel.fireTableRowsUpdated(row,row);
        }
        
        public Object getCellEditorValue() {
            return cmbValue.getSelectedItem();
        }
        
//        public Object get
        
    }//End Class
    
    /**Inner Class Custom Table Model for User Preferences Form.
     */
    class UserPreferencesTableModel extends AbstractTableModel {

        /** Returns the number of columns in the model.
         * @return column count.
         */
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        /** Returns the number of rows in the model.
         * @return row count.
         */
        public int getRowCount() {
            //System.out.println("Row Count"+vecUserPreferences.size());
            return vecUserPreferences.size();
        }
        
        /** Returns the value for the cell at columnIndex and rowIndex.
         * @param row row index.
         * @param column column index.
         * @return value.
         */
        public Object getValueAt(int row, int column) {
            switch(column){
                case 0:
                    return ((UserPreferencesBean)vecUserPreferences.get(row)).getVariableName();
                case 1:
                    return ((UserPreferencesBean)vecUserPreferences.get(row)).getVarValue();
            }
            return NOTHING;
        }
        
        /** Returns true if the cell at rowIndex and columnIndex is editable.
         * @param row row index.
         * @param column column index.
         * @return editable.
         */
        public boolean isCellEditable(int row, int column) {
            if(column == 1)return true;
            else return false;
        }
        
        //Class Variables
        private static final int COLUMN_COUNT = 2;
        private static final String NOTHING = "";
    }//End Class
    
    class UserPreferencesTableCellRenderer extends DefaultTableCellRenderer{
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column).setBackground(Color.white);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

    }

}//End Class
