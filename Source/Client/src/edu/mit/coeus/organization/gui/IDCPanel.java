/*
 * IDCPanel.java  08/30/02 15:05:12
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.organization.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import java.awt.event.KeyEvent;
import javax.swing.table.*;
import javax.swing.table.TableColumnModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


import java.util.Enumeration;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * <code> IDCPanel </code> is a class to  get the organization's IDC details.
 *
 * @author  Phaneendra
 * @date  30,Aug,2002
 * @since 1.0
 * @modified by Sagin
 * @date 24-10-02
 * Description : As part of Java V1.3 compatibility, Replaced all null Vectors
 *               with new Vector() instance.
 *
 */

public class IDCPanel extends javax.swing.JPanel implements ListSelectionListener {

    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnAdd;
    private javax.swing.JScrollPane scrlPnComments;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable idcTable;
    public javax.swing.JTextArea txtComments;
    public javax.swing.JLabel jLabel1;

    private Vector colNames;
    private Vector defaultData;
    private int rowHeight=20;
    private Vector typeData;
    private boolean saveRequired =false;
    private int deletedRow=-1;
    private Hashtable commentsInfo;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    /** Creates new form AuditPanel */
    public IDCPanel(){
        //Initialize Vectors
        colNames = new Vector();
        defaultData = new Vector();

    }

    /**
     *  This is used to render the Idc panel with the given information, 
     *  set the Column width for the hidden field and set 
     *  the selection interval for the JTable.
     */
    
    public JPanel renderPanel(){
        initComponents();
        
        java.awt.Component[] components={idcTable,txtComments,btnAdd,btnDelete};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);  
        
        //This is to create the table with the provided info
        setTableColumnWidths();

        ListSelectionModel listSelectionModel =idcTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(this);

        idcTable.getSelectionModel().setSelectionInterval(0,0);
        return this;
    }

    /**
     * Sets the column names that are required for table
     * @param colNames A Vector consisting of column names.
     */
    public void setColumnNames(Vector colNames){
        this.colNames = colNames;
    }

    /**
     * Gets the Column names of the table available in this panel in a Vector
     *
     * @return A Vector of column names
     */
    public Vector getColumnNames(){
        return this.colNames;
    }
    
    /** This method is used to set the vector which contains Vector of beans
     *  @param defaultData is the Vector of data beans.
     */

    public void setDefaultData(Vector defaultData){
        this.defaultData = defaultData;
    }

    /**
     * Method to get the Form Data
     * @return defaultData Vector of beans containing data.
     */
    public Vector getDefaultData(){
        return this.defaultData;
    }

    /**
     * Helper method which gives which gives vector of TypeDescriptions
     */
    public Vector getTypeData(){
        // modified by ravi for sending only descriptions instead of comboboxbeans
        // for showing in table.
        Vector descVector = new Vector();
        if(typeData!= null){
            for(int index = 0; index < typeData.size(); index ++){
                descVector.addElement(((ComboBoxBean)
                    typeData.elementAt(index)).toString());
            }
        }
        return descVector;
    }

    /**
     * Method to set the types used in ComboBoxBean 
     * @param typeData, a Vector Values of comboBox
     */
    public void setTypeData(Vector typeData){
        this.typeData = typeData;
    }

    /**
     * This returns idc Table with data
     */
    public JTable getIDCTable(){
        return this.idcTable;
    }


    /**
     *  This is used to set the comments info from the IDCForm
     *
     * @param commentsInfo hashTable with info
     */
    public void setCommentsInfo(Hashtable commentsInfo){
        this.commentsInfo = commentsInfo;
    }

    /**
     *  This is used to get the comments info from the IDCForm
     *
     * @return commentsInfo hashTable with info
     */
    public Hashtable getCommentsInfo(){
        return commentsInfo;
    }
    
    /**
    * Forwards the message from the CellEditor to the delegate. Tell the 
    * editor to stop editing and accept any partially edited value as the 
    * value of the editor.
    */

    public void stopCellEditing(){
        if(idcTable.isEditing() ){
            idcTable.getCellEditor().stopCellEditing();
        }
    }


    /**
     *  This is to initialize all the components in IDCPanel with the existing
     * information
     *
     */
    private void initComponents() {

        coeusMessageResources = CoeusMessageResources.getInstance();

        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        idcTable = new javax.swing.JTable();
        idcTable.setFont(CoeusFontFactory.getNormalFont());
        idcTable.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        scrlPnComments  = new javax.swing.JScrollPane();
        txtComments = new javax.swing.JTextArea();
        txtComments.setFont(CoeusFontFactory.getNormalFont());
        // Added by Chandra
        txtComments.setLineWrap(true);
        txtComments.setWrapStyleWord(true);        
        // end chandra
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());


        jPanel2.setLayout(new java.awt.GridBagLayout());


        btnAdd.setText("Add");
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic(KeyEvent.VK_A);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 5, 0);
        jPanel2.add(btnAdd, gridBagConstraints);

        btnDelete.setText("Delete");
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic(KeyEvent.VK_D);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel2.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 50, 40);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(jPanel2, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(393, 175));

        idcTable.setModel(new javax.swing.table.DefaultTableModel(
        getDefaultData(), getColumnNames()
        ));
        jScrollPane1.setViewportView(idcTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        txtComments.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                saveRequired = true;
                int selRow = idcTable.getSelectedRow();
                if (commentsInfo == null) {
                    commentsInfo = new Hashtable();
                }
                commentsInfo.put(new Integer(selRow+1),txtComments.getText());
            }
        });

        scrlPnComments = new JScrollPane();
        scrlPnComments.setPreferredSize(new Dimension(390, 107));
        scrlPnComments.setViewportView(txtComments);
        scrlPnComments.setBackground(java.awt.Color.white);
        scrlPnComments.setForeground(java.awt.Color.white);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(scrlPnComments, gridBagConstraints);

        jLabel1.setText("Comments:");
        jLabel1.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 7, 0);
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        add(jPanel1, gridBagConstraints);
        // set the row height
        idcTable.setRowHeight(rowHeight);
        idcTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       //idcTable.setCellSelectionEnabled(true);
        // set the single selection
        idcTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // avoid table header to reOrder the columns
        idcTable.getTableHeader().setReorderingAllowed(false);
        setTableColumnWidths();
        
        
        idcTable.addFocusListener(new FocusAdapter(){
            public void focusLost(){
                DefaultCellEditor editor =
                        (DefaultCellEditor)idcTable.getCellEditor();
                if (editor != null) {
                    editor.stopCellEditing();
                }
            }
        });
        btnAdd.addActionListener(  new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                if( DetailForm.selectedTabIndex != 6 ){
                    return;
                }
                saveRequired = true;
                /* If a button press is the trigger to leave a JTable cell and
                 * save the data in model
                 */
                if(idcTable.isEditing() ){
                    idcTable.getCellEditor().stopCellEditing();
                }
                // find out total available rows
                int totalRows = idcTable.getRowCount();
                txtComments.setText("");
                int cols = idcTable.getModel().getColumnCount();
                if( idcTable.getModel() instanceof DefaultTableModel ) {

                    ((DefaultTableModel)
                        idcTable.getModel()).addRow(new Object[cols]);

                    int newRowCount = idcTable.getRowCount();

                    // select the first row
                    idcTable.getSelectionModel().setSelectionInterval(
                        newRowCount-1,newRowCount-1);
                }

                 //Added by Amit 11/20/2003 for IRB_DEF_Gen_6
                idcTable.requestFocusInWindow();
                idcTable.editCellAt(totalRows,0);
                idcTable.getEditorComponent().requestFocusInWindow();
                //End Amit                
            }
        } );
        // delete button
        btnDelete.addActionListener(  new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent){
                if( DetailForm.selectedTabIndex != 6 ){
                    return;
                }
                saveRequired = true;
                /* If a button press is the trigger to leave a JTable cell
                 * and save the data in model
                 */
                if(idcTable.isEditing() ){
                    idcTable.getCellEditor().stopCellEditing();
                }

                int totalRows = idcTable.getRowCount();
                // If there are more than one row in table then delete it
                if( totalRows > 0){

                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                                            "orgIDCPnl_delConfirmCode.1096"),
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    // if Yes then selectedOption is 0
                    // if No then selectedOption is 1
                    if(0 == selectedOption  ){
                        // get the selected row
                        int selectedRow = idcTable.getSelectedRow();

                        if(  selectedRow != -1 ){

                            DefaultTableModel dm =
                                (DefaultTableModel)idcTable.getModel();

                            deletedRow = selectedRow;
                            refreshComments();
                            // remove the selected row
                            ((DefaultTableModel)
                                idcTable.getModel()).removeRow(selectedRow);
                            deletedRow = -1;
                            idcTable.clearSelection();


                            // find out again row count in table
                            int newRowCount = idcTable.getRowCount();
                            // select the next row if exists
                            if( newRowCount > selectedRow){
                                idcTable.getSelectionModel().setSelectionInterval(
                                    selectedRow,selectedRow);
                            }else{
                                idcTable.getSelectionModel().setSelectionInterval(
                                    newRowCount-1,newRowCount-1);
                            }

                        }

                    }
                }else{
                    // show the error message
                    CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                            "orgIDCPnl_exceptionCode.1097"));
                }


            }
        } );



    }// end of initComponents

    private void setTableColumnWidths(){
        Enumeration enums = idcTable.getColumnModel().getColumns();
        // set the renders
        while (enums.hasMoreElements()) {
            TableColumn column =  (TableColumn)enums.nextElement();
            int columnIndex = column.getModelIndex();
            // Rate
            if( columnIndex == 0 ) {
                column.setPreferredWidth(50);
                column.setMinWidth(50);
                String columnName = ((DefaultTableModel)
                    idcTable.getModel()).getColumnName(columnIndex);
                TableCellEditor currencyCellEditor =
                            new CurrencyEditor(columnName);
                column.setCellEditor(currencyCellEditor);
            }
            //type
            if( columnIndex == 1) {
                column.setPreferredWidth(85);
                column.setMinWidth(85);
                JComboBox coeusCombo = new JComboBox(getTypeData());
                coeusCombo.setFont(CoeusFontFactory.getNormalFont());
                coeusCombo.setEditable(false);
                column.setCellEditor(new DefaultCellEditor(coeusCombo ));
            }
            //start date
            if( columnIndex == 2 ) {
                column.setPreferredWidth(75); // width of column
                column.setMinWidth(75);
                // get column name and set the editor
                String columnName = ((DefaultTableModel)
                    idcTable.getModel()).getColumnName(columnIndex);
                TableCellEditor dateCellEditor = new DateEditor(columnName);
                column.setCellEditor(dateCellEditor);
            }
            //end date
            if( columnIndex == 3) {
                column.setPreferredWidth(75); // width of column
                column.setMinWidth(75);
                // get the column name and set cell editor
                String columnName = ((DefaultTableModel)
                    idcTable.getModel()).getColumnName(columnIndex);
                TableCellEditor dateCellEditor = new DateEditor(columnName);
                column.setCellEditor(dateCellEditor);

            }
            //Requested date
            if( columnIndex == 4) {
                column.setPreferredWidth(150);
                column.setMinWidth(150);
                // get column name and set the editor
                String columnName = ((DefaultTableModel)
                    idcTable.getModel()).getColumnName(columnIndex);
                TableCellEditor dateCellEditor = new DateEditor(columnName);
                column.setCellEditor(dateCellEditor);
            }
            
             if( columnIndex == 5) {
                column.setPreferredWidth(0);
                column.setMinWidth(0);     
                column.setMaxWidth(0);
            }
        }
    }// while ends


    /** This  method is used to refresh the comments information based on the
     * selection of Idc row in IDCTable.
     *
     */
    private void refreshComments(){
        /* If the record in the IDCTable is deleted corresponding comments
         * information should be removed from the Hashtable with that key value
         */
        if ( ( deletedRow != -1) && (commentsInfo != null) ){
            int dataSize = idcTable.getRowCount();
            if (deletedRow <=dataSize) {
                for (int row =0;row <dataSize-1 ;row++){
                    if (row < deletedRow){
                       commentsInfo.put(new Integer(row+1),
                        (commentsInfo.get(new Integer(row+1)) == null ? "":
                                (String)commentsInfo.get(new Integer(row+1))));
                    }else {
                       commentsInfo.put(new Integer(row+1),
                        (commentsInfo.get(new Integer(row+2)) == null ? "" :
                            (String)commentsInfo.get(new Integer(row+2))));
                    }
                }
                if (commentsInfo.contains(new Integer(dataSize))){
                    commentsInfo.remove(new Integer(dataSize));
                }

            }

        }
    }



    /**
     * set enabled or diabled for form controls as per the functionality
     *
     * @param functionType the functionality type
     *                 'D' - display
     *                 'M' - modify
     */
    public void formatFields(char functionType) {
        boolean enableStatus = true;
        if (functionType=='D') {
            enableStatus = false;
            idcTable.setModel(new DefaultTableModel(
                getDefaultData(), getColumnNames()) {
                public boolean isCellEditable(int row,int col){
                    return false;
                }
            });
            setTableColumnWidths();
        }else{
            enableStatus = true;
        }
        btnDelete.setEnabled(enableStatus);
        btnAdd.setEnabled(enableStatus);
        txtComments.setEditable(enableStatus);
        if (!enableStatus){
            idcTable.setBackground(this.getBackground());
            txtComments.setBackground(this.getBackground());

        }
    }


    /**
     * This is to check whether user changed information to promt the user for
     * saving if any changes done before closing the window
     *
     * @return saveRequired flag value
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){        
            if(idcTable.getRowCount() > 0 ) {
                idcTable.requestFocusInWindow();
                idcTable.setRowSelectionInterval(0, 0);
                idcTable.setColumnSelectionInterval(0,0);
            }            
            else{
                btnAdd.requestFocusInWindow();
            }        
    }    
    //end Amit      

    /**
     * This is used to set the save required
     *
     * @param boolean saveRequired
     */
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }

    /**
     *  This is used to set the comments information for any change in IDC Table
     *
     * @param listSelectionEvent
     */

    public void valueChanged(ListSelectionEvent e) {

        txtComments.setText("");
        if (commentsInfo == null) {
            commentsInfo = new Hashtable();
        }


        int selectedRow = idcTable.getSelectedRow();
        if ((selectedRow != -1) ) {
            if ( commentsInfo.get(new Integer(selectedRow+1)) != null ){
                txtComments.setText((String)commentsInfo.get(
                    new Integer(selectedRow+1)));
            }
        }
    } // end of valueChanged


    /*
     * Inner class to set the editor for currency columns/cells.
     */
    class CurrencyEditor extends AbstractCellEditor implements TableCellEditor {

        private String colName;

        private JComponent currencyComponent = null;

        CurrencyEditor(String colName) {
            this.colName = colName;
        }

        /**
         * An overridden method to set the editor component in a cell.
         * @param table - the JTable that is asking the editor to edit; can be null
         * @param value - the value of the cell to be edited; it is up to the
         * specific editor to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered as a
         * string or it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected - true if the cell is to be rendered with highlighting
         * @param row - the row of the cell being edited
         * @param column - the column of the cell being edited
         * @return the component for editing
         */
        public Component getTableCellEditorComponent(JTable table,Object value,
            boolean isSelected,
        int row,int column){

            String currencyValue ="";
            if (value != null) {
                currencyValue = value.toString();
            }
            currencyComponent = new CurrencyField(currencyValue);

            return currencyComponent;
        }

        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {

            try {

                String editingValue = (String)getCellEditorValue();

                if( (editingValue == null ) || (editingValue.trim().length()== 0 )){
                    return super.stopCellEditing();
                }

            }
            catch(ClassCastException exception) {
                return false;
            }
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)currencyComponent).getText();
        }

        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need to
         * occur when an cell is selected (or deselected).
         * @param e an ItemEvent.
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
    }





    /*
     * Inner class to set the editor for date columns/cells.
     */
    class DateEditor extends AbstractCellEditor implements TableCellEditor {

        private String colName;
        private static final String DATE_SEPARATERS = ":/.,|-";
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        private JComponent dateComponent = new JTextField();

        DateEditor(String colName) {
            this.colName = colName;
            ((JTextField)dateComponent).setFont(CoeusFontFactory.getNormalFont());
        }

        /**
         * An overridden method to set the editor component in a cell.
         * @param table - the JTable that is asking the editor to edit; can be null
         * @param value - the value of the cell to be edited; it is up to the
         * specific editor to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered as a
         * string or it could be rendered as a check box that is checked. null is
         * a valid value
         * @param isSelected - true if the cell is to be rendered with highlighting
         * @param row - the row of the cell being edited
         * @param column - the column of the cell being edited
         * @return the component for editing
         */
        public Component getTableCellEditorComponent(JTable table,Object value,
            boolean isSelected,
        int row,int column){

            // Configure the component with the specified value

            JTextField tfield =(JTextField)dateComponent;
            // take the current value and convert the date to actual date
            String currentValue = (String)value;
            if( ( currentValue != null  ) && (currentValue.trim().length()!= 0) ){
                String newValue = new DateUtils().restoreDate(currentValue,
                    DATE_SEPARATERS) ;
                tfield.setText(newValue);
                return dateComponent;
            }

            tfield.setText( ((String)value));
            // Return the configured component
            return dateComponent;
        }

        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {

            try {

                String editingValue = (String)getCellEditorValue();

                if( (editingValue == null ) || (editingValue.trim().length()== 0 )){
                    return super.stopCellEditing();
                }
                String formattedDate = new DateUtils().formatDate(editingValue,
                    DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                if(null == formattedDate ) {
                    CoeusOptionPane.showErrorDialog("Please enter a Valid "+colName);
                    return false;
                }else{
                    ((JTextField)dateComponent).setText( formattedDate);
                }

            }
            catch(ClassCastException exception) {
                return false;
            }
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)dateComponent).getText();
        }

        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need to
         * occur when an cell is selected (or deselected).
         * @param e an ItemEvent.
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
    }





    class DateValidateRenderer extends JTextField implements TableCellRenderer{

        /**
         * A default constructor, that is called by table while rendering the
         * related cell.
         * @param items A String array of elements that are shown in
         * combobox of acceptance column
         */
        public DateValidateRenderer() {
            super();
            super.setFont(CoeusFontFactory.getNormalFont());
        }

        /**
         * An overridden method render the cell in Acceptance column, which is being
         * called by table.
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(Color.blue);
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            if( hasFocus ){
                setBackground(Color.green);

            }

            return this;
        }
    }

    /**
     * An inner class to draw an editor when the cell of Fiscal Year column is
     * selected by user to edit the data.
     */
    class DateValidateEditor extends AbstractCellEditor implements TableCellEditor {

        // This is the component that will handle the editing of the cell value
        JComponent component = new JTextField();

        /**
         * The Default Constructor to instantiate this renderer
         */
        public DateValidateEditor(){
            ((JTextField)component).setFont(CoeusFontFactory.getNormalFont());
        }

        /**
         * An overridden method to render a cell with customized options when user
         * wish to edit the cell of Fiscal year column, This method is called when
         * a cell value is edited by the user.
         */
        public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int rowIndex, int vColIndex) {

            // Configure the component with the specified value
            JTextField tfield =(JTextField)component;
            tfield.setText( ((String)value));
            return component;
        }

        /**
         * This method is called when editing is completed.
         * @return Object  The new value to be stored in the cell.
         */
        public Object getCellEditorValue() {
            return ((JTextField)component).getText();
        }

        /**
         * This method is called just before the cell value
         * @return If the value is not valid, false should be returned.
         **/
        public boolean stopCellEditing() {
            String s = (String)getCellEditorValue();
            return super.stopCellEditing();
        }

        /**
         * This method is called when any item is selected from the
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }

    }//end of ValidateCellDataEditor class


    /**
     * An inner class to draw editor for cell in Acceptance columns
     */
    class AuditTypeEditor extends DefaultCellEditor {

        public AuditTypeEditor(Vector items, boolean editable) {
            super( new CoeusComboBox(items,editable));
        }

    } // end editor class


    /*
     * An inner class to render the combobox in the Acceptance column in Audit table,
     * which sets the default background,foreground colors of cell.
     */
    class  AuditTypeRenderer  implements TableCellRenderer {

        private Color selBackGroundColor;

        CoeusComboBox coeusCombo = null;


        /**
         * A default constructor, that is called by table while rendering the
         * related cell.
         * @param items A String array of elements that are shown in combobox of
         * acceptance column
         */
        public AuditTypeRenderer(String[] items, boolean editable,
            Color selBackground) {
            coeusCombo = new CoeusComboBox(items,editable);
            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
            this.selBackGroundColor = selBackground;
        }

        /**
         * A default constructor, that is called by table while rendering the
         * related cell.
         * @param items The vector of elements that are shown in combobox of
         * acceptance column
         */
        public AuditTypeRenderer(Vector items, boolean editable,
            Color selBackground) {
            coeusCombo = new CoeusComboBox(items,editable);
            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
            this.selBackGroundColor = selBackground;
        }

        /**
         * An overridden method render the cell in Acceptance column, which is being
         * called by table.
         * Returns the component used for drawing the cell.
         * This method is used to configure the renderer appropriately before drawing
         *
         * @param table  the JTable that is asking the renderer to draw; can be null
         * @param value  the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value. For example, if value
         * is the string "true", it could be rendered as a string or it could be
         * rendered as a check box that is checked. null is a valid value
         * @param isSelected  true if the cell is to be rendered with the selection
         * highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example, put a
         * special border on the cell, if the cell can be edited, render in the
         * color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the header,
         * the value of row is -1
         * @param column  the column index of the cell being drawn
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {

            if (isSelected) {
                coeusCombo.setForeground(table.getSelectionForeground());
                coeusCombo.setBackground(selBackGroundColor);
            } else {
                coeusCombo.setForeground(table.getForeground());
                coeusCombo.setBackground(table.getBackground());
            }
            // Select the current value
            coeusCombo.setSelectedItem(value);
            return coeusCombo;
        }


    }
}