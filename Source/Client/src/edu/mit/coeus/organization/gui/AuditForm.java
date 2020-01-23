/*
 * AuditForm.java  08/30/02 15:05:12
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.organization.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Vector;

import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.organization.bean.OrganizationAuditBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * <code> AuditForm </code> is a class to  get the organization's audit details.
 *
 * @author  yaman
 * @date  15,Aug,2002
 * @since 1.0
 */
public class AuditForm extends javax.swing.JPanel
                                            implements ListSelectionListener {

    // These are the combobox values
    private String[] acceptenceOptions;
    private Vector colNames;

    private int rowHeight = 20;
    private int column0Width = 70;
    private int column1Width = 120;
    private int column2Width = 400;
    private int prevSelectedRow = -1;

    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnAdd;
    private javax.swing.JScrollPane scrlPaneAuditInfo;
    private javax.swing.JTable auditInfo;

    private Vector dataVector;

    private OrganizationAuditBean[] formData;
    private char functionType;
    private boolean saveRequired = false;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    /**
     * Constructor which instantiates AuditForm and populates them with data
     * specified, in Organization Module. And sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param formData a vector which consists of all the details
     * of a Organization Audits.
     */
    
    public AuditForm(char functionType, OrganizationAuditBean[] formData) {
        this.functionType = functionType;
        this.formData = formData;
        //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - start
        //setAcceptenceOptions(new String[]{"Accepted", "Rejected", "Requested"});
        setAcceptenceOptions(new String[]{"Accepted", "Rejected", "Requested","Reviewed"});
        //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - end
        java.util.Vector colVector = new java.util.Vector();
        
        colVector.add("Fiscal Year");
        colVector.add("Audit Accepted");
        colVector.add("Comment");
        
        setColumnNames(colVector);
        setRowData(formData);
        renderPanel();
        formatFields();
        
    }

     /**
     *  This is used to render the Idc panel with the given information.
     */
    public void renderPanel() {
        initComponents();
        
        java.awt.Component[] component={auditInfo,btnAdd,btnDelete};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
    }

    /**
     * Gets the all available Acceptance options(Ex. Accepted,Required,Requested)..
     * @return An array of options, which are rendered in combobox in 2nd column
     */
    public String[] getAcceptenceOptions() {
        return acceptenceOptions;
    }

    /**
     * Sets the all available Acceptance options(Ex. Accepted,Required,Requested)..
     * @param acceptenceOptions An array of options, which are rendered
     * in combobox in 2nd column
     */
    public void setAcceptenceOptions(String[] acceptenceOptions) {
        this.acceptenceOptions = acceptenceOptions;
    }

    /**
     *  Gets all the column names that are shown in Table as a column headers
     *
     * @return A Vector of Column names.
     */
    public Vector getColumnNames() {
        return colNames;
    }

    /**
     * Sets all column names that will be shown as column headers in a table
     * @param colNames All column names required for table.
     */
    public void setColumnNames(Vector colNames) {
        this.colNames = colNames;
    }

    /**
     * Gets the default Data required for the table when it is rendered
     * in the beginning.
     *
     * @return The table default data in a vector
     */
    public Vector getData() {
        return this.dataVector;
    }

    /**
     * Method to set the data in the JTable.
     * This method sets the data which is available in formData Vector into JTable.
     * @param formInfo  form info
     */
    public void setRowData(OrganizationAuditBean[] formInfo) {
        dataVector = new Vector();
        if (formData != null) {
            for (int i = 0; i < formData.length; i++) {
                Vector auditRow = new Vector();
                auditRow.addElement((formData[i].getFiscalYear() == null ?
                    "" :formData[i].getFiscalYear()));
                String auditAcceptance = "";
                if (formData[i].getAuditAccepted() != null) {
                    if (formData[i].getAuditAccepted().trim().equals("A")) {
                        auditAcceptance = "Accepted";
                    } else if (formData[i].getAuditAccepted().trim().equals("R")) {
                        auditAcceptance = "Rejected";
                    } else if (formData[i].getAuditAccepted().trim().equals("Q")) {
                        auditAcceptance = "Requested";
                        //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - start
                    }else if(formData[i].getAuditAccepted().trim().equals("V")){
                        auditAcceptance = "Reviewed";
                    }
                    //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - end
                }
                auditRow.addElement(auditAcceptance);
                auditRow.addElement((formData[i].getAuditComment() == null ?
                    "" : formData[i].getAuditComment()));
                dataVector.addElement(auditRow);
            }
        }
    }

    /** This method is used to get all the information entered by the user in the
     * Audit table by a OrganizationAudit Bean to save the information in
     * the database.
     * @return  OrganizationAuditBean[]
     */

    public OrganizationAuditBean[] getFormData() {
        int auditListSize = auditInfo.getRowCount();

        Vector newAuditData = null;

        if (functionType == 'I') {
            newAuditData = new Vector();
            int nextAudit = 0;
            if (auditListSize > 0) {
                for (int rows = 0; rows < auditListSize; rows++) {
                    OrganizationAuditBean orgAuditData =
                        new OrganizationAuditBean();
                    for (int cols = 0; cols < auditInfo.getColumnCount(); cols++) {
                        if (auditInfo.getColumnName(cols
                            ).toString().trim().equals("Fiscal Year")) {
                            orgAuditData.setFiscalYear(
                                auditInfo.getValueAt(rows, cols) ==
                                    null ? "" : auditInfo.getValueAt(rows,
                                    cols).toString().trim());
                        } else if (auditInfo.getColumnName(cols
                            ).toString().trim().equals("Audit Accepted")) {

                            String acceptance = (auditInfo.getValueAt(rows, cols)
                                == null ? "" : auditInfo.getValueAt(rows,
                                    cols).toString().trim());
                            String answer = "";
                            if (acceptance.trim().equals("Accepted")) {
                                answer = "A";
                            } else if (acceptance.trim().equals("Rejected")) {
                                answer = "R";
                            } else if (acceptance.trim().equals("Requested")) {
                                answer = "Q";
                                //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - start
                            }else if (acceptance.trim().equals("Reviewed")) {
                                answer = "V";
                            }
                            //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - end
                            orgAuditData.setAuditAccepted(answer);
                        } else if (auditInfo.getColumnName(
                            cols).toString().trim().equals("Comment")) {
                            orgAuditData.setAuditComment(auditInfo.getValueAt(rows,
                                cols) == null ? "" : auditInfo.getValueAt(rows,
                                cols).toString().trim());
                        }
                    }
                    orgAuditData.setAcType("I");
                    newAuditData.add(orgAuditData);
                }
            }
            //  formData = newAuditData;
        } else if (functionType == 'U') {
            newAuditData = new Vector();
            int nextAudit = 0;
            /* This is if the operation is Modify and the data is changed in the
             * Organization Audit list table like removing the existing data
             * then we need to delete the information from the Audit Information
             * in the data base
             */
            if (formData != null) {
                for (int count = 0; count < formData.length; count++) {
                    OrganizationAuditBean orgAuditData = formData[count];
                    boolean found = false;
                    int selOrgAuditSize = auditInfo.getRowCount();
                    if (selOrgAuditSize > 0) {
                        for (int rowCount = 0; rowCount < selOrgAuditSize;
                                rowCount++) {
                            if (orgAuditData.getFiscalYear().trim().equals(
                                auditInfo.getValueAt(rowCount,
                                    0).toString().trim())) {
                                String acceptance =
                                    (auditInfo.getValueAt(rowCount, 1) == null ?
                                        "" : auditInfo.getValueAt(rowCount,
                                        1).toString().trim());
                                String answer = "";
                                if (acceptance.trim().equals("Accepted")) {
                                    answer = "A";
                                } else if (acceptance.trim().equals("Rejected")) {
                                    answer = "R";
                                } else if (acceptance.trim().equals("Requested")) {
                                    answer = "Q";
                                }
                                //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - start
                                else if (acceptance.trim().equals("Reviewed")) {
                                    answer = "V";
                                }
                                //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - end
                                
                                orgAuditData.setAuditAccepted(answer);
                                orgAuditData.setAuditComment(
                                    auditInfo.getValueAt(rowCount, 2) == null ?
                                        "" : auditInfo.getValueAt(rowCount,
                                        2).toString().trim());
                                found = true;
                            }
                        }

                        if (found) {
                            orgAuditData.setAcType("U");
                        } else {
                            orgAuditData.setAcType("D");
                        }
                        newAuditData.add(orgAuditData);
                    } else {
                        orgAuditData.setAcType("D");
                        newAuditData.add(orgAuditData);
                    }
                } //for formData is not null and no rows in audit table
            }//if formdata!=null

            /* This is if the operation is Modify and the data is added in the
             * Audit list table then we need to insert the new information in
             * the Audit Information in the data base
             */
            if (auditListSize > 0) {
                for (int rows = 0; rows < auditInfo.getRowCount(); rows++) {
                    boolean found = false;
                    if (formData != null && formData.length > 0) {
                        int selAuditSize = formData.length;
                        for (int rowCount = 0; rowCount < selAuditSize;
                                rowCount++) {
                            OrganizationAuditBean orgAuditData =
                                formData[rowCount];
                            if (auditInfo.getValueAt(rows,
                                0).toString().trim().equals(
                                    orgAuditData.getFiscalYear().trim())) {
                                found = true;
                            }
                        }
                    }
                    OrganizationAuditBean orgAuditData = new OrganizationAuditBean();
                    if (!found) {
                        for (int cols = 0; cols < auditInfo.getColumnCount(); cols++) {
                            if (auditInfo.getColumnName(cols
                                ).toString().trim().equals("Fiscal Year")) {
                                orgAuditData.setFiscalYear(
                                    auditInfo.getValueAt(rows, cols) == null ?
                                        "" : auditInfo.getValueAt(rows,
                                            cols).toString().trim());
                            } else if (auditInfo.getColumnName(
                                cols).toString().trim().equals("Audit Accepted")) {
                                String acceptance = (auditInfo.getValueAt(rows,
                                    cols) == null ? "" :
                                        auditInfo.getValueAt(rows,
                                        cols).toString().trim());
                                String answer = "";
                                if (acceptance.trim().equals("Accepted")) {
                                    answer = "A";
                                } else if (acceptance.trim().equals("Rejected")) {
                                    answer = "R";
                                } else if (acceptance.trim().equals("Requested")) {
                                    answer = "Q";
                                }
                                //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - start
                                else if (acceptance.trim().equals("Reviewed")) {
                                    answer = "V";
                                }
                                //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - end
                                orgAuditData.setAuditAccepted(answer);
                            } else if (auditInfo.getColumnName(
                                cols).toString().trim().equals("Comment")) {
                                orgAuditData.setAuditComment(
                                    auditInfo.getValueAt(rows, cols) == null ?
                                        "" : auditInfo.getValueAt(rows,
                                        cols).toString().trim());
                            }
                        } //ends for columns
                        orgAuditData.setAcType("I");
                        newAuditData.add(orgAuditData);
                    } //ends for not found check
                } // ends for auditInfo table rows loop
            }
        }//ends for function Type 'U
        int dataSize = 0;
        OrganizationAuditBean[] newData = null;
        if (newAuditData != null) {
            newData = new OrganizationAuditBean[newAuditData.size()];
            for (int auditCnt = 0; auditCnt < newAuditData.size(); auditCnt++) {
                OrganizationAuditBean newInfo =
                    (OrganizationAuditBean) newAuditData.elementAt(auditCnt);
                newData[auditCnt] = newInfo;
            }
        }
        return newData;

    }

    /**
     * A private method, and called from within the constructor to
     * initialize the components.
     */
    private void initComponents() {

        coeusMessageResources = CoeusMessageResources.getInstance();

        JPanel pnlMain = new JPanel();
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        auditInfo = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());
        setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));

        jPanel2.setLayout(new java.awt.GridBagLayout());
        jPanel2.setBorder(new javax.swing.border.EmptyBorder(0, 0, 20, 0));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(new javax.swing.border.BevelBorder(
            javax.swing.border.BevelBorder.LOWERED, Color.black, Color.gray));

        auditInfo.setModel(new javax.swing.table.DefaultTableModel(getData(),
        getColumnNames()));

        ListSelectionModel listSelectionModel = auditInfo.getSelectionModel();
        listSelectionModel.addListSelectionListener(this);


        // get all the columns and set the column required properties
        setTableColumnWidths();
//        java.util.Enumeration enum = auditInfo.getColumnModel().getColumns();
//        while (enum.hasMoreElements()) {
//            TableColumn column = (TableColumn) enum.nextElement();
//            
//            if (column.getModelIndex() == 0) {
//                column.setPreferredWidth(70);
//                column.setMinWidth(70);
//                column.setCellEditor(new ValidateCellDataEditor(true));
//            }
//
//            if (column.getModelIndex() == 1) {
//                column.setPreferredWidth(120);
//                column.setMinWidth(120);
//                column.setCellEditor(new AcceptenceComboBoxEditor(
//                getAcceptenceOptions()));
//                // If the cell should appear like a combobox in its
//                // non-editing state, also set the combobox renderer
//
//            }
//
//            if (column.getModelIndex() == 2) {
//                column.setPreferredWidth(240); // width of column
//                column.setMinWidth(240);
//                column.setCellEditor(new ValidateCellDataEditor(false));
//            }
//        }

        scrlPaneAuditInfo = new javax.swing.JScrollPane(auditInfo);

        scrlPaneAuditInfo.setPreferredSize(new java.awt.Dimension(460, 280));

        auditInfo.setMinimumSize(new java.awt.Dimension(60, 70));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 150;
        jPanel1.add(scrlPaneAuditInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 50, 0);
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 150;

        pnlMain.add(scrlPaneAuditInfo, gridBagConstraints);

        //***********************************************************
        btnAdd.setText("Add");
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel2.add(btnAdd, gridBagConstraints);

        btnDelete.setText("Delete");
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel2.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 200, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlMain.add(jPanel2, gridBagConstraints);
        //***********************************************************
        // set the row height
        auditInfo.setRowHeight(rowHeight);

        // set the single selection
        auditInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // avoid table header to resize/ rearrange
        auditInfo.getTableHeader().setReorderingAllowed(false);
        auditInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        auditInfo.setOpaque(true);
//        auditInfo.getTableHeader().setResizingAllowed(false);

        // Table header font
        auditInfo.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        auditInfo.setFont(CoeusFontFactory.getNormalFont());
        auditInfo.getSelectionModel().setSelectionInterval(0, 0);
        prevSelectedRow = 0;

        auditInfo.addFocusListener(new FocusAdapter(){
            public void focusLost(){
                DefaultCellEditor editor =
                (DefaultCellEditor)auditInfo.getCellEditor();
                if (editor != null) {
                    editor.stopCellEditing();
                }
            }
        });


        btnAdd.setMnemonic(KeyEvent.VK_A);

        // action of add button
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if( DetailForm.selectedTabIndex != 5 ){
                    return;
                }               
                
                saveRequired = true;
                /* If a button press is the trigger to leave a JTable cell
                 * and save the data in model
                 */
                if (auditInfo.isEditing()) {
                    auditInfo.getCellEditor().stopCellEditing();
                }

                // find out total available rows
                int totalRows = auditInfo.getRowCount();

                int cols = auditInfo.getModel().getColumnCount();

                if (auditInfo.getModel() instanceof DefaultTableModel) {

                    ((DefaultTableModel) auditInfo.getModel()).addRow(
                        new Object[cols]);

                    int newRowCount = auditInfo.getRowCount();

                    // select the first row
                    auditInfo.getSelectionModel().setSelectionInterval(
                        newRowCount - 1, newRowCount - 1);
                }

                 //Added by Amit 11/20/2003 for IRB_DEF_Gen_6
                auditInfo.requestFocusInWindow();
                auditInfo.editCellAt(totalRows,0);
                auditInfo.getEditorComponent().requestFocusInWindow();
                //End Amit  

            }
        });


        btnDelete.setMnemonic(KeyEvent.VK_D);
        // action of Delete button
        btnDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if( DetailForm.selectedTabIndex != 5 ){
                    return;
                }
                saveRequired = true;

                /* If a button press is the trigger to leave a JTable cell and
                 * save the data in model
                 */
                if (auditInfo.isEditing()) {
                    auditInfo.getCellEditor().stopCellEditing();
                }

                int totalRows = auditInfo.getRowCount();
                // If there are more than one row in table then delete it
                if (totalRows > 0) {
                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                                        "orgAudFrm_delConfirmCode.1090"),
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    // if Yes then selectedOption is 0
                    // if No then selectedOption is 1
                    if (0 == selectedOption) {
                        // get the selected row
                        int selectedRow = auditInfo.getSelectedRow();
                        if (selectedRow != -1) {

                            DefaultTableModel dm =
                                (DefaultTableModel) auditInfo.getModel();
                            // remove the selected row
                            ((DefaultTableModel) auditInfo.getModel()
                                ).removeRow(selectedRow);
                            auditInfo.clearSelection();

                            // find out again row count in table
                            int newRowCount = auditInfo.getRowCount();
                            // select the next row if exists
                            if (newRowCount > selectedRow) {
                                auditInfo.getSelectionModel().setSelectionInterval(
                                selectedRow, selectedRow);
                            } else {
                                auditInfo.getSelectionModel().setSelectionInterval(
                                newRowCount - 1, newRowCount - 1);
                            }

                        }

                    }
                } else {
                    // show the error message
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                        "orgAudFrm_exceptionCode.1091"));
                }


            }
        }
        );
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(pnlMain);
    } // end of initcomponents

    private void setTableColumnWidths(){
        java.util.Enumeration enums = auditInfo.getColumnModel().getColumns();
        while (enums.hasMoreElements()) {
            TableColumn column = (TableColumn) enums.nextElement();
            
            if (column.getModelIndex() == 0) {
                column.setPreferredWidth(70);
                column.setMinWidth(70);
                column.setCellEditor(new ValidateCellDataEditor(true));
            }

            if (column.getModelIndex() == 1) {
                column.setPreferredWidth(120);
                column.setMinWidth(120);
                column.setCellEditor(new AcceptenceComboBoxEditor(
                getAcceptenceOptions()));
                // If the cell should appear like a combobox in its
                // non-editing state, also set the combobox renderer

            }

            if (column.getModelIndex() == 2) {
                column.setPreferredWidth(265); // width of column
                column.setMinWidth(265);
                column.setCellEditor(new ValidateCellDataEditor(false));
            }
        }
    
    
    }
/*
 * An inner class to render the combobox in the Acceptance column in Audit table,
 * which sets the default background,foreground colors of cell.
 */
    class AcceptenceComboBoxRenderer extends JComboBox
        implements TableCellRenderer {
        /**
         * A default constructor, that is called by table while rendering the
         * related cell.
         * @param items A String array of elements that are shown in combobox
         * of acceptance column
         */
        public AcceptenceComboBoxRenderer(String[] items) {
            super(items);
            super.setFont(CoeusFontFactory.getNormalFont());
        }

        /**
         * An overridden method render the cell in Acceptance column,
         * which is being called by table.
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            // Select the current value
            setSelectedItem(value);

            return this;
        }
    }

    /**
     * An inner class to draw editor for cell in Acceptance columns
     */
    class AcceptenceComboBoxEditor extends DefaultCellEditor {
        public AcceptenceComboBoxEditor(String[] items) {
            super(new JComboBox(items));
        }
    } // end editor class

    /**
     * An inner class to draw an editor when the cell of Fiscal Year column is
     * selected by user to edit the data.
     */
    public class ValidateCellDataEditor extends AbstractCellEditor
                                                    implements TableCellEditor {

        // This is the component that will handle the editing of the cell value
        JComponent component = new JTextField();
        boolean validate;

        /**
         * The Default Constructor to instantiate this renderer
         * @param validate   cell validation status
         */
        public ValidateCellDataEditor(boolean validate) {
            this.validate = validate;
            ((JTextField)component).setFont(CoeusFontFactory.getNormalFont());
        }

        /**
         * An overridden method to render a cell with customized options when
         * user wish
         * to edit the cell of Fiscal year column, This method is called when a
         * cell value is edited by the user.
         */
        //
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int rowIndex,
                                                     int vColIndex) {

            // Configure the component with the specified value
            JTextField tfield = (JTextField) component;

            // if any vaidations to be done for this cell
            if (validate) {
                tfield.setDocument(
                    new JTextFieldFilter(JTextFieldFilter.NUMERIC, 4));

            }

            tfield.setText(((String) value));
            // Return the configured component
            return component;
        }

        /**
         * This method is called when editing is completed.
         * @return Object  The new value to be stored in the cell.
         */
        public Object getCellEditorValue() {
            return ((JTextField) component).getText();
        }

        /**
         * This method is called just before the cell value
         * @return If the value is not valid, false should be returned.
         **/
        public boolean stopCellEditing() {
            String s = (String) getCellEditorValue();
            return super.stopCellEditing();
        }

        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }

    }//end of ValidateCellDataEditor class


    public boolean validateData() {
        /* If a button press is the trigger to leave a JTable cell
         * and save the data in model
         */
        if (auditInfo.isEditing()) {
            auditInfo.getCellEditor().stopCellEditing();
         }


        if (auditInfo.getRowCount() > 0) {
            for (int rowCount = 0; rowCount < auditInfo.getRowCount(); rowCount++) {
                String value = (auditInfo.getValueAt(rowCount, 0) == null ?
                "" : auditInfo.getValueAt(rowCount, 0).toString().trim());
                boolean found = false;
                if (value.trim().equals("")) {
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                        "orgAudFrm_exceptionCode.1092"));
                    return false;
                } else {
                    for (int rows = 0; rows < auditInfo.getRowCount(); rows++) {
                        if (rows != rowCount) {
                            if (value.trim().equals(
                            (auditInfo.getValueAt(rows, 0) == null ? "" :
                                auditInfo.getValueAt(rows,
                                0).toString().trim()))) {
                                found = true;
                            }
                        }
                    }
                }
                if (found) {
                    CoeusOptionPane.showErrorDialog("Fiscal Year : '" + value
                        + "' must be unique ");
                    return false;
                }
            }
        }
        return true;
    }


    public void stopTableEditing(){
        if(auditInfo.isEditing() && auditInfo.getEditingColumn() != 1){
            int selectedRow = auditInfo.getSelectedRow();
            int selCol = auditInfo.getEditingColumn();
            String value = null;
            if(selectedRow != -1 && selCol != -1){
                value = ((javax.swing.JTextField)
                auditInfo.getEditorComponent()).getText();
                if( (value != null)){
                    auditInfo.setValueAt(value,selectedRow,selCol);
                }
                auditInfo.getCellEditor().cancelCellEditing();
            }
        }

    }

    public boolean isSaveRequired() {
        if ( (formData==null || formData.length == 0) && !saveRequired  ){
            return false;
        }
        try{
            for (int i = 0; i < formData.length; i++) {
                String auditAcceptance="";
                if (formData[i].getAuditAccepted().trim().equals("A")) {
                    auditAcceptance = "Accepted";
                } else if (formData[i].getAuditAccepted().trim().equals("R")) {
                    auditAcceptance = "Rejected";
                } else if (formData[i].getAuditAccepted().trim().equals("Q")) {
                    auditAcceptance = "Requested";
                }
                //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - start
                else if (formData[i].getAuditAccepted().trim().equals("V")) {
                    auditAcceptance = "Reviewed";
                }
                //Added for COEUSDEV-1064 : Add Value to Organization Audit Accepted Dropdown - end
                
                //added by guptha to set the saveRequired flag
                if (!formData[i].getFiscalYear().trim().equals(
                    auditInfo.getValueAt(i,0).toString().trim())) {
                    saveRequired=true;
                    break;
                }else if (!auditAcceptance.equals(auditInfo.getValueAt(i,1).toString().trim())) {
                    saveRequired=true;
                    break;
                }else if (!formData[i].getAuditComment().trim().equals(
                    auditInfo.getValueAt(i,2).toString().trim())) {
                    saveRequired=true;
                    break;
                }

            }
        }catch (Exception e){
            saveRequired=true;
        }
        return saveRequired;
    }

    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {            
            if(auditInfo.getRowCount() > 0 ) {
                auditInfo.requestFocusInWindow();
                auditInfo.setRowSelectionInterval(0, 0);
                auditInfo.setColumnSelectionInterval(0,0);
            }            
            else{
                btnAdd.requestFocusInWindow();
            }
        }
    }    
    //end Amit          
    
    public void valueChanged(ListSelectionEvent e) {

    } // end of valueChanged
    /**
     * set enabled or diabled for form controls as per the functionality
     *
     * @param functionType the functionality type
     *                 'D' - display
     *                 'M' - modify
     */
    public void formatFields() {
        boolean enableStatus = true;
        if (functionType == 'D') {
            enableStatus = false;
            auditInfo.setModel(new DefaultTableModel(getData(), getColumnNames()) {
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            });
        } else {
            enableStatus = true;
        }
        btnDelete.setEnabled(enableStatus);
        btnAdd.setEnabled(enableStatus);
        if (!enableStatus){
            auditInfo.setBackground(this.getBackground());
        }
        setTableColumnWidths();
    }
}
