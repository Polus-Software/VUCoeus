
/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * ProposalDataOverride.java
 *
 * Created on January 7, 2004, 11:08 AM
 */

/* PMD check performed, and commented unused imports and variables on 31-MAY-2007
 * by Leena
 */
/* PMD check performed, and commented unused imports and
 * variables on 24-SEPTEMBER-2007 by Divya
 */
package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.propdev.gui.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.customelements.bean.*;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;
//import java.text.SimpleDateFormat;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author  Amit Jadhav, 01/07/2004
 *@ modified by chandra
 */
public class ProposalDataOverride extends javax.swing.JComponent implements
        LookUpWindowConstants, TypeConstants, ListSelectionListener,MouseListener{
    
//    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private CoeusVector cvDataObjects ;
    private CoeusVector cvDataValues;
    private CoeusVector cvChangedData = null;
    
    private boolean hasRight = false;
    private int dataExists = 0;
    
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    private ProposalColumnsToAlterBean proposalColumnsToAlterBean;
    private ProposalDataOveridesBean proposalDataOveridesBean;
    
    private ColumnOverrideTableModel  columnOverrideTableModel;
    private DataOverrideTableModel dataOverrideTableModel;
    private ColumnOverrideRenderer columnOverrideRenderer;
    //  private EmptyHeaderRenderer emptyHeaderRenderer;
    private DataCellEditor dataCellEditor;
    private DataRenderer dataRenderer;
    
    private static final String  EMPTY_STRING = "";
    private static final String  DATA_STRING = "DATA";
    private String dataSelected="";
    
//    private static final int DATE_VALUE = 10;
    //  private static final int NUMBER_VALUE = 3;
    //   private static final int CHARACTER_VALUE = 1;
    
    private int editableRow = -1;
    
    private static final String DEADLINE_DATE = "Dead Line Date";
    private static final String DEADLINE_TYPE = "Dead Line Type";
    
    private DateUtils dtUtils = new DateUtils();
    private static final String DATE = "DATE";
    private static final String  NUMBER = "NUMBER";
    private static final String CHARACTER = "CHAR";
    private static final String VARCHAR = "VARCHAR2";
//    private static final String DEAD_LINE_DATE = "DEADLINE_DATE";
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    //private static final String REQUIRED_DATE_FORMAT = "mm/dd/yyyy";
//     private static final String REQUIRED_DATE_FORMAT = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    
    private boolean modified=false;
    private boolean hasLookUp;
//    private boolean isNewData;
    private boolean isLookUp;
    private String lookUpWindow;
    private String lookUpArgument;
    
    //holds whether save is required
    private boolean saveRequired;
    private CoeusMessageResources coeusMessageResources;
    private CoeusDlgWindow dlgWindow;
    
    private static final int WIDTH=753;
    private static final int HEIGHT = 365;
    
    private static final int NO_COLUNM = 0;
    private static final int OLD_VALUE = 1;
    private static final int NEW_VALUE = 2;
    private static final int COMMENTS = 3;
    
    private static final int FILE_COLUMN = 0;
    private static final int VALUE_COLUMN=1;
    
    private CoeusAppletMDIForm mdiForm;
    private edu.mit.coeus.utils.Utils Utils;
    
    javax.swing.ImageIcon EMPTY_PAGE_ICON,FILL_PAGE_ICON;
    
    private int selRow ;
    private String cValue;
    private String proposalId;
    
//    private String newDesc="newDesc";
    // private String desc="desc";
//    private String comment = "test comment ";
    private JLabel lblIcon;
    
    /** Creates new form ProposalDataOverride
     * @param proposalId
     */
    public ProposalDataOverride(String proposalId) {
        this.proposalId=proposalId;
        cvDataValues = new CoeusVector();
        cvChangedData = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        dataOverrideTableModel = new DataOverrideTableModel();
        columnOverrideTableModel = new ColumnOverrideTableModel();
        columnOverrideRenderer = new ColumnOverrideRenderer();
        dataCellEditor = new DataCellEditor();
        dataRenderer = new DataRenderer();
        registerComponents();
        tblColumn.setModel(columnOverrideTableModel);
        tblData.setModel(dataOverrideTableModel );
        setFormData();
//        setColumnData();
        setColumnData();
        setDefaultFocusForComponent();
        showDataOverride();
        //display();
    }
    
    /** sets the form data */
    public void setFormData(){
        try{
            cvDataObjects = new CoeusVector();
            Vector dataObjects = new Vector();
            //boolean hasRight ;
            //int dataOverideExist = 0;
            RequesterBean reqBean = new RequesterBean();
            reqBean.setFunctionType('K');
            reqBean.setDataObject(proposalId);
            String connectTo = CoeusGuiConstants.CONNECTION_URL+CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,reqBean);
            comm.send();
            
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                CoeusOptionPane.showInfoDialog( response.getMessage() );
            } else{
                //cvDataObjects = (Vector)response.getDataObjects();
                dataObjects = (Vector)response.getDataObjects();
                //Rights
                hasRight = ((Boolean)dataObjects.elementAt(0)).booleanValue();
                //Whether DataOveride exist or not
                dataExists = ((Integer)dataObjects.elementAt(1)).intValue();
                //Data
                cvDataObjects = (CoeusVector)dataObjects.elementAt(2);
            }
            if(cvDataObjects!= null && cvDataObjects.size() > 0){
                if(proposalColumnsToAlterBean!= null){
                    proposalColumnsToAlterBean = (ProposalColumnsToAlterBean)cvDataObjects.get(0);
                    //cvDataObjects.sort("columnLabel",true);
                    if(cvDataObjects!= null && cvDataObjects.size() > 0){
                        for(int index = 0; index < cvDataObjects.size(); index ++){
                            proposalColumnsToAlterBean = (ProposalColumnsToAlterBean)cvDataObjects.get(index);
                            cvDataValues = proposalColumnsToAlterBean.getProposalDataOverides();
                        }
                    }
                }
            }else{
                cvDataObjects = new CoeusVector();
            }
            columnOverrideTableModel.setData(cvDataObjects);
            if(cvDataValues!=null && cvDataValues.size() > 0){
                dataOverrideTableModel.setData(cvDataValues);
                dataOverrideTableModel.fireTableDataChanged();
            }
        } catch(Exception exc){
            exc.printStackTrace();
        }
    }
    private boolean viewOnlyMode;
    /** Displays the form data */
    public void display(boolean viewOnlyMode) throws Exception{
        this.viewOnlyMode = viewOnlyMode;
        dataCellEditor.stopCellEditing();
        //Added for case 3587: Multi campus enhancements
        if(!viewOnlyMode && !hasRight){
            throw new Exception(coeusMessageResources.parseMessageKey(
                    "dataOverride_exceptionCode.1101"));
        //3587: End
        }else  if(viewOnlyMode||!hasRight){
            if(dataExists > 0){
                //Button Disabled
                //Open
                btnChange.setEnabled(false);
                btnOk.setEnabled(false);
                dlgWindow.setVisible(true);
                dlgWindow.setEnabled(false);
            }else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                        "dataOverride_exceptionCode.1100"));
//                //Message
//                //should not open
//                CoeusOptionPane.showInfoDialog(
//                coeusMessageResources.parseMessageKey("dataOverride_exceptionCode.1100"));
//                dlgWindow.setVisible(false);
                
            }
        }else{
            //Open
            //Enabled
            btnChange.setEnabled(true);
            btnOk.setEnabled(true);
            dlgWindow.setVisible(true);
            
        }
        
        //        modified=false;
        //        dlgWindow.setVisible(true);
    }
    
    /** Registers all the listeners */
    private void registerComponents(){
        tblColumn.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblColumn.getSelectionModel().addListSelectionListener(this);
        tblData.addMouseListener(this);
        
        Component[] comp = {btnOk, btnCancel, btnChange };
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
    }
    
    /** value changes when the table row is selected.Get the corresponding data for the
     * selected column
     * @param listSelectionEvent
     */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        dataCellEditor.stopCellEditing();
        ProposalColumnsToAlterBean proposalColumnsToAlterBean;
        ListSelectionModel source = (ListSelectionModel)listSelectionEvent.getSource();
        int selectedRow;
        if(source.equals(tblColumn.getSelectionModel())) {
            selectedRow= tblColumn.getSelectedRow();
            proposalColumnsToAlterBean= (ProposalColumnsToAlterBean)cvDataObjects.get(selectedRow);
            dataSelected = proposalColumnsToAlterBean.getColumnLabel();
            lblOverridedData.setText("Overriden Data for column "+dataSelected);
            lblOverridedData.setFont(CoeusFontFactory.getLabelFont());
            dataOverrideTableModel.setData(proposalColumnsToAlterBean.getProposalDataOverides());
            dataOverrideTableModel.fireTableDataChanged();
            
            //Commented for case 3702 -Data Overide- Title field  - start
            //Modified for Coeus 4.3 enhancement: Data Override Nullable validation - start
            //To not allow the user to enter invalid data type, changed the document set to JTextFieldFilter
            //which checks both the type and length of the data user entered
//            if(proposalColumnsToAlterBean.getDataType().equals(DATE)){
//                //txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
//                dataCellEditor.txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
//            }else if(proposalColumnsToAlterBean.getDataType().equals(NUMBER)){
//                //txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
//                dataCellEditor.txtNewValue.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,proposalColumnsToAlterBean.getDataPrecision()));
//            }else if(proposalColumnsToAlterBean.getDataType().equals(CHARACTER)){
//                // txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
//                dataCellEditor.txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
//            }else if(proposalColumnsToAlterBean.getDataType().equals(VARCHAR)){
//                //txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
//                dataCellEditor.txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
//            }
            //Modified for Coeus 4.3 enhancement: Data Override Nullable validation - end
            //Commented for case 3702 -Data Overide- Title field  - end
            
            //Added  for case 3702 -Data Overide- Title field  - start
            //Allow all the characters to be entered for the fields of type 
            //date,char and varchar
            if(proposalColumnsToAlterBean.getDataType().equals(DATE)){
                dataCellEditor.txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
            }else if(proposalColumnsToAlterBean.getDataType().equals(NUMBER)){
                dataCellEditor.txtNewValue.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,proposalColumnsToAlterBean.getDataPrecision()));
            }else if(proposalColumnsToAlterBean.getDataType().equals(CHARACTER)){
                dataCellEditor.txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
            }else if(proposalColumnsToAlterBean.getDataType().equals(VARCHAR)){
                dataCellEditor.txtNewValue.setDocument(new LimitedPlainDocument(proposalColumnsToAlterBean.getDataLength()));
            }
            //Added  for case 3702 -Data Overide- Title field  - end
            
            if(tblData.getRowCount() == 0 || cvDataValues == null){
                cvDataValues = new CoeusVector();
            }
        }
    }
    
    /** sets the column width and the table properticies */
    public void setColumnData(){
        JTableHeader header = tblData.getTableHeader();
        header.addMouseListener(new ColumnHeaderListener());
        header.setReorderingAllowed(false);
        header.setFont(CoeusFontFactory.getLabelFont());
        
        TableColumn column = tblColumn.getColumnModel().getColumn(0);
        column.setMaxWidth(15);
        column.setMinWidth(15);
        column.setPreferredWidth(15);
        column.setCellRenderer(columnOverrideRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = tblColumn.getColumnModel().getColumn(1);
        column.setCellRenderer(columnOverrideRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        /** This is to specify the column width, edfitor and specifying
         *other sizes for the Data Table
         */
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableColumn dataColumn = tblData.getColumnModel().getColumn(NO_COLUNM);
        tblData.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        dataColumn.setCellEditor(dataCellEditor);
        dataColumn.setMinWidth(30);
        dataColumn.setPreferredWidth(30);
        dataColumn.setCellRenderer(dataRenderer);
        
        dataColumn = tblData.getColumnModel().getColumn(OLD_VALUE);
        dataColumn.setCellEditor(dataCellEditor);
        dataColumn.setCellRenderer(dataRenderer);
        dataColumn.setMinWidth(110);
        dataColumn.setPreferredWidth(130);
        
        dataColumn = tblData.getColumnModel().getColumn(NEW_VALUE);
        dataColumn.setCellEditor(dataCellEditor);
        dataColumn.setCellRenderer(dataRenderer);
        dataColumn.setMinWidth(110);
        dataColumn.setPreferredWidth(130);
        
        dataColumn  = tblData.getColumnModel().getColumn(COMMENTS);
        dataColumn.setCellEditor(dataCellEditor);
        dataColumn.setCellRenderer(dataRenderer);
        // dataColumn.setMaxWidth(160);
        dataColumn.setMinWidth(130);
        dataColumn.setPreferredWidth(145);
        
        tblData.setRowHeight(22);
    }
    
    /** specifies the modal data, where the data is showing */
    private void showDataOverride() {
        dataCellEditor.stopCellEditing();
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),"Proposal Data Override",true);
        dlgWindow.getContentPane().add(this);
        dlgWindow.setResizable(false);
        dlgWindow.setSize(WIDTH, HEIGHT);
        dlgWindow.setModal(true);
        dlgWindow.setLocation(CoeusDlgWindow.CENTER);
        dlgWindow.setFont(CoeusFontFactory.getLabelFont());
        
        dlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                
                dataCellEditor.stopCellEditing();
                try{
                    if(modified){
                        performWindowClosing();
                    }else{
                        dlgWindow.dispose();
                    }
                }catch(Exception e){
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
                return;
            }
        });
        dlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        dlgWindow.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we) {
                requestDefaultFocus();
            }
            public void windowClosing(WindowEvent we){
                dataCellEditor.stopCellEditing();
                try{
                    if(modified){
                        performWindowClosing();
                    }else{
                        dlgWindow.dispose();
                    }
                }catch(Exception e){
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
                return;
            }
        });
    }
    
    /** sets the default focus when the form is opened */
    public void setDefaultFocusForComponent(){
        if(tblColumn.getRowCount()  > 0){
            tblColumn.requestFocusInWindow();
            int rowNum = tblColumn.getSelectedRowCount();
            if(rowNum  != -1){
                tblColumn.setRowSelectionInterval(rowNum, rowNum);
            }
            tblColumn.setColumnSelectionInterval(1,1);
        }
    }
    
    private class ColumnOverrideTableModel extends AbstractTableModel implements TableModel{
        
        private String colNames[] = {EMPTY_STRING,EMPTY_STRING};
        
        private Class colClass[] = {ImageIcon.class, String.class};
        
        public   int getColumnCount() {
            return colNames.length;
        }
        
        public   int getRowCount() {
            if(cvDataObjects==null){
                return 0;
            }
            return cvDataObjects.size();
        }
        public void setData(CoeusVector data){
            cvDataObjects = data;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public  Object getValueAt(int rowIndex, int columnIndex) {
            ProposalColumnsToAlterBean proposalColumnsToAlterBean = (ProposalColumnsToAlterBean)cvDataObjects.get(rowIndex);
            switch(columnIndex){
                case FILE_COLUMN:
                    if(proposalColumnsToAlterBean.getProposalDataOverides()==null || proposalColumnsToAlterBean.getProposalDataOverides().size() == 0 ){
                        return EMPTY_STRING;
                    }else {
                        return DATA_STRING;
                    }
                    
                case VALUE_COLUMN:
                    //return new String(proposalColumnsToAlterBean. getColumnLabel());
                    return proposalColumnsToAlterBean. getColumnLabel();
            }
            return EMPTY_STRING;
        }
        
        
    }
    
    private class DataOverrideTableModel extends AbstractTableModel implements TableModel{
        
        private String colNames[] = {"No.","Old value","New value","Comments"};
        
        private Class colClass[] = {Integer.class, String.class, String.class,String.class};
        
        public boolean isCellEditable(int row, int col){
            
            if(hasLookUp){
                if(editableRow == -1){
                    return false;
                    
                }else if(row==editableRow){
                    switch(col){
                        case NO_COLUNM:
                        case OLD_VALUE:
                        case NEW_VALUE:
                            return false;
                        default:
                            return true;
                    }
                }else{
                    return false;
                }
            }else{
                if(editableRow == -1){
                    return false;
                }else if(row==editableRow){
                    switch(col){
                        case NO_COLUNM:
                        case OLD_VALUE:
                            return false;
                        default:
                            return true;
                            
                    }
                }else{
                    return false;
                }
            }
            
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            if(cvDataValues==null){
                return 0;
            }
            return cvDataValues.size();
            
        }
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public void setData(CoeusVector data){
            cvDataValues = data;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public  Object getValueAt(int rowIndex, int columnIndex) {
            ProposalDataOveridesBean  proposalDataOveridesBean = (ProposalDataOveridesBean)cvDataValues.get(rowIndex);
//            int selRow = tblColumn.getSelectedRow();
//            String colLabel = (String)tblColumn.getValueAt(selRow,1);
            boolean isLook = proposalDataOveridesBean.isHasLookUp();
            if(proposalDataOveridesBean != null){
                
                switch(columnIndex){
                    case NO_COLUNM:
                        return new Integer(proposalDataOveridesBean.getChangedNumber());
                        
                    case OLD_VALUE:
                        if(proposalDataOveridesBean.getOldDisplayValue()!=null){
                            //return new String(proposalDataOveridesBean.getOldDisplayValue());
                            return proposalDataOveridesBean.getOldDisplayValue();
                        }else{
                            return EMPTY_STRING;
                        }
                    case NEW_VALUE:
                        if(isLook){
                            if(proposalDataOveridesBean.getDisplayValue()!=null){
                                //return new String(proposalDataOveridesBean.getDisplayValue());
                                return proposalDataOveridesBean.getDisplayValue();
                            }else{
                                return EMPTY_STRING;
                            }
                        }else{
//                            if(colLabel.equals(DEADLINE_DATE)){
//                                    try{
//                                       String value = proposalDataOveridesBean.getDisplayValue();
//                                        if(value!=null && !value.equals(EMPTY_STRING)){
//                                            value = dtUtils.formatDate(value, DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
//                                            return value;
//                                        }
//                                    }catch(Exception exception){
//                                        exception.printStackTrace();
//                                    }
//                            }
                            //else if(proposalDataOveridesBean.getDisplayValue()!=null){
                            if(proposalDataOveridesBean.getDisplayValue()!=null){
                                //return new String(proposalDataOveridesBean.getDisplayValue());
                                return proposalDataOveridesBean.getDisplayValue();
                            }else{
                                return EMPTY_STRING;
                            }
                        }
                    case COMMENTS:
                        if(proposalDataOveridesBean.getComments()!=null){
                            //return new String(proposalDataOveridesBean.getComments());
                            return proposalDataOveridesBean.getComments();
                        }else{
                            return EMPTY_STRING;
                        }
                }
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int col){
            ProposalDataOveridesBean proposalDataOveridesBean =
                    (ProposalDataOveridesBean)cvDataValues.get(row);
            switch(col){
                case NO_COLUNM:
                    if(proposalDataOveridesBean.getChangedNumber() ==  0){
                        proposalDataOveridesBean.setChangedNumber(1);
                    }else{
                        proposalDataOveridesBean.setChangedNumber(proposalDataOveridesBean.getChangedNumber()+1);
                    }
                    break;
                case OLD_VALUE:
                    proposalDataOveridesBean.setOldDisplayValue(value.toString().trim());
                    break;
                case NEW_VALUE:
                    if(!isLookUp){
                        proposalDataOveridesBean.setChangedValue(value.toString().trim());
                        proposalDataOveridesBean.setDisplayValue(value.toString().trim());
                        if(proposalDataOveridesBean.getChangedValue()!= null &&
                                proposalDataOveridesBean.getChangedValue().equals(value.toString())){
                            break;
                        }
                    }else{
                        proposalDataOveridesBean.setDisplayValue(value.toString().trim());
                    }
                    if(proposalDataOveridesBean.getDisplayValue()!= null &&
                            proposalDataOveridesBean.getDisplayValue().equals(value.toString())){
                        break;
                    }
                    modified=true;
                    break;
                case COMMENTS:
                    proposalDataOveridesBean.setComments(value.toString().trim());
                    modified=true;
                    
            }
        }
    }
    
    /** Renderer for the column table where the image icons are placed. Rendering
     *the image icons based on the data.
     */
    private class ColumnOverrideRenderer extends DefaultTableCellRenderer{
        java.net.URL emptyPageUrl = getClass().getClassLoader().getResource( CoeusGuiConstants.ADD_ICON);
        java.net.URL fillPageUrl = getClass().getClassLoader().getResource( CoeusGuiConstants.DATA_ICON);
        
        ColumnOverrideRenderer(){
            super();
            lblIcon = new JLabel();
            
        }
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            switch(column){
                case FILE_COLUMN:
                    if(emptyPageUrl != null){
                        EMPTY_PAGE_ICON = new ImageIcon(emptyPageUrl);
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    
                    if(fillPageUrl != null){
                        FILL_PAGE_ICON = new ImageIcon(fillPageUrl);
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }
                    
                    if(value.equals(EMPTY_STRING)){
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }else {
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }
                    
                    return lblIcon;
                case VALUE_COLUMN:
                    //                    setSize(181, 16);
                    setText(value.toString());
            }
            return super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
        }
    }// End of ColumnOverrideRenderer class..............
    
    
    /** Editor for the Data table where the values can be entered in the componets */
    
    private class DataCellEditor extends AbstractCellEditor implements TableCellEditor {
        ProposalColumnsToAlterBean proposalColumnsToAlterBean;
        String colLabel = null;
        private JTextField txtComponent;
        private JTextField txtComments;
        private JTextField txtNewValue;
        private int column;
        
        DataCellEditor(){
            txtComponent= new JTextField();
            txtComments = new JTextField();
            txtNewValue = new JTextField();
        }
        
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case NO_COLUNM:
                case OLD_VALUE:
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case NEW_VALUE:
                    txtNewValue.setText(value.toString());
                    return txtNewValue;
                case COMMENTS:
                    txtComments.setText(value.toString());
                    return txtComments;
            }
            return txtComponent;
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case NO_COLUNM:
                case OLD_VALUE:
                    return txtComponent.getText();
                case NEW_VALUE:
                    return txtNewValue.getText();
                case COMMENTS:
                    return txtComments.getText();
            }
            return txtComments.getText();
        }
        
        public int getClickCountToStart(){
            return 1;
        }
        /** To Stop cell Editing
         * @return boolean if <true> editinging of cell stopped
         */
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnData = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        scrPnColumn = new javax.swing.JScrollPane();
        tblColumn = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        lblDataOverride = new javax.swing.JLabel();
        lblProposalNumber = new javax.swing.JLabel();
        ldlColumnOverridden = new javax.swing.JLabel();
        lblOverridedData = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(650, 400));
        setPreferredSize(new java.awt.Dimension(700, 400));
        scrPnData.setBackground(new java.awt.Color(255, 255, 255));
        scrPnData.setBorder(new javax.swing.border.EtchedBorder());
        scrPnData.setMinimumSize(new java.awt.Dimension(440, 300));
        scrPnData.setPreferredSize(new java.awt.Dimension(440, 300));
        tblData.setFont(CoeusFontFactory.getNormalFont());
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblData.setRowHeight(20);
        scrPnData.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 5);
        add(scrPnData, gridBagConstraints);

        scrPnColumn.setBorder(new javax.swing.border.EtchedBorder());
        scrPnColumn.setMinimumSize(new java.awt.Dimension(210, 300));
        scrPnColumn.setPreferredSize(new java.awt.Dimension(210, 300));
        tblColumn.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblColumn.setFont(CoeusFontFactory.getNormalFont());
        tblColumn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblColumn.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblColumn.setRowHeight(20);
        tblColumn.setRowMargin(2);
        tblColumn.setShowHorizontalLines(false);
        tblColumn.setShowVerticalLines(false);
        scrPnColumn.setViewportView(tblColumn);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(scrPnColumn, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(btnCancel, gridBagConstraints);

        btnChange.setFont(CoeusFontFactory.getLabelFont());
        btnChange.setMnemonic('a');
        btnChange.setText("Change");
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 0, 0);
        add(btnChange, gridBagConstraints);

        lblDataOverride.setFont(CoeusFontFactory.getLabelFont());
        lblDataOverride.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDataOverride.setText("Data Overrides for Proposal ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblDataOverride, gridBagConstraints);

        lblProposalNumber.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNumber.setText(proposalId);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblProposalNumber, gridBagConstraints);

        ldlColumnOverridden.setFont(CoeusFontFactory.getLabelFont());
        ldlColumnOverridden.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ldlColumnOverridden.setText("Column that can be Overridden");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(ldlColumnOverridden, gridBagConstraints);

        lblOverridedData.setFont(CoeusFontFactory.getLabelFont());
        lblOverridedData.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOverridedData.setText("Overrided Data for column " + dataSelected);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblOverridedData, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void tblColumnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblColumnMouseClicked
        // Add your handling code here:
    }//GEN-LAST:event_tblColumnMouseClicked
    
    /** listener for the cancel button */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        dataCellEditor.stopCellEditing();
        try{
            if(modified){
                performWindowClosing();
            }else{
                dlgWindow.dispose();
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    /** action performs when the ok button is clicked */
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        dataCellEditor.stopCellEditing();
        if(validateData()){
            updateInboxDetails();
        }
    }//GEN-LAST:event_btnOkActionPerformed
    
    /** Update the form data. While clicking ok save the neccessary information to the
     * repective beans and the save it to the database
     */
    private void updateInboxDetails(){
        
        Vector vecInbox = new Vector();
        Vector vecNotePad = new Vector();
        Vector vecProposal = new Vector();
        //   Vector vecData = new Vector();
        String message = null;
        String changedValue = null;
        String displayValue = null;
        String colName = null;
        //UserRolesInfoBean userRolesBean = new UserRolesInfoBean();
        
        for(int index = 0 ; index < cvDataObjects.size(); index++ ){
            proposalColumnsToAlterBean = (ProposalColumnsToAlterBean)cvDataObjects.get(index);
            cvDataValues = proposalColumnsToAlterBean.getProposalDataOverides();
            if(cvDataValues != null && cvDataValues.size() > 0){
                for(int dataIndex = 0; dataIndex < cvDataValues.size();dataIndex++){
                    proposalColumnsToAlterBean  = (ProposalColumnsToAlterBean)cvDataObjects.get(index);
                    proposalDataOveridesBean = (ProposalDataOveridesBean)cvDataValues.get(dataIndex);
                    
                    if(proposalDataOveridesBean.getAcType()==null){
                        continue;
                    }
                    if(proposalDataOveridesBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        changedValue = proposalDataOveridesBean.getChangedValue();
                        displayValue = proposalDataOveridesBean.getDisplayValue();
                        colName = proposalColumnsToAlterBean.getColumnLabel();
                        
                        
                        
                        
                        proposalDataOveridesBean.setDataType(proposalColumnsToAlterBean.getDataType());
                        
                        InboxBean inboxBean = new InboxBean();
                        MessageBean messageBean = new MessageBean();
                        message = colName+" for proposal \'"+proposalId+ "\' has been changed to "+displayValue+".";
                        String toUser = CoeusGuiConstants.getMDIForm().getUserName();
                        messageBean.setAcType("I");
                        messageBean.setMessage(message);
                        
                        inboxBean.setMessageBean(messageBean);
                        inboxBean.setAcType("I");
                        inboxBean.setOpenedFlag('Y');
                        inboxBean.setFromUser(toUser);
                        inboxBean.setProposalNumber(proposalId);
                        /* Case :1828 Start */
                        inboxBean.setModuleCode(3);
                        /* Case :1828 End */
                        inboxBean.setToUser(toUser);
                        inboxBean.setSubjectType('N');
                        vecInbox.add(inboxBean);
                        
                        NotepadBean notepadBean = new NotepadBean();
                        notepadBean.setAcType("I");
                        notepadBean.setComments(message);
                        notepadBean.setProposalAwardNumber(proposalId);
                        //Case 3943 - START
                        notepadBean.setUpdateUser(toUser);
                        //Case 3943 - END
                        vecNotePad.add(notepadBean);
                    }
                }
            }
        }
        
        if(modified){
            try{
                RequesterBean reqBean = new RequesterBean();
                reqBean.setFunctionType('M');
                
                vecProposal.add(0, cvChangedData);
                vecProposal.add(1, vecInbox);
                vecProposal.add(2, vecNotePad);
                vecProposal.add(3, proposalId);
                
                reqBean.setDataObjects(vecProposal);
                
                String connectTo = CoeusGuiConstants.CONNECTION_URL+CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,reqBean);
                comm.send();
                ResponderBean response = comm.getResponse();
                if(!response.isSuccessfulResponse()){
                    CoeusOptionPane.showInfoDialog( response.getMessage() );
                }
                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)response.getDataObject();
                //saveRequired = true;
                dlgWindow.dispose();
            } catch(Exception exc){
                exc.printStackTrace();
            }
            //   }
            //}
        }else{
            dlgWindow.dispose();
        }
    }
    
    /** Actions performed while clicking on Change button is clicked
     * @param evt
     */
    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        // Add your handling code here:
        dataCellEditor.stopCellEditing();
        showLookUpWindow();
        
    }//GEN-LAST:event_btnChangeActionPerformed
    
    /** show the look up window and corresponding details. The details will be shown if
     * and only if the look up is present
     */
    private void showLookUpWindow(){
        dataCellEditor.stopCellEditing();
        
        String windowTitle;
        // String displayValue = null;
        int selectedRow = tblColumn.getSelectedRow();
        mdiForm = CoeusGuiConstants.getMDIForm();
        if(selectedRow != -1){
            proposalColumnsToAlterBean = (ProposalColumnsToAlterBean)cvDataObjects.get(selectedRow);
            
            //dataSelected = proposalColumnsToAlterBean.getColumnLabel();
            dataSelected = proposalColumnsToAlterBean.getColumnName();
            lookUpWindow = proposalColumnsToAlterBean.getLookUpWindow();
            hasLookUp = proposalColumnsToAlterBean.isHasLookUp();
            lookUpArgument = proposalColumnsToAlterBean.getLookUpArgument();
        }
        if(lookUpWindow != null){
            if(lookUpWindow.equalsIgnoreCase(PERSON_LOOKUP_WINDOW)){
                showPersonSearchWindow(PERSON_SEARCH, cValue, selRow);
            }else if(lookUpWindow.equalsIgnoreCase(UNIT_LOOKUP_WINDOW)){
                showUnitSearchWindow(UNIT_SEARCH, cValue, selRow);
            }else if(lookUpWindow.equalsIgnoreCase(ROLODEX_LOOKUP_WINDOW)){
                showRolodexSearch(ROLODEX_SEARCH, cValue, selRow);
            }else if(lookUpWindow.equalsIgnoreCase(CODE_LOOKUP_WINDOW)){
                if(lookUpArgument != null){
                    windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                    //Added for Case # 2162 - Adding Award Type - Start
                    if(lookUpArgument.equalsIgnoreCase("Proposal Award Type")){
                        ComboBoxBean comboboxBean = new ComboBoxBean();
                        comboboxBean.setCode("");
                        comboboxBean.setDescription("");
                        vecLookupdata.add(0,comboboxBean);
                    }
                    // Added for case # 2162 - Adding Award Type - End
                    OtherLookupBean otherLookupBean =
                            new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                    showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow, hasLookUp);
                }
            }else if(lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW)){
                if(lookUpArgument != null){
                    windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                    OtherLookupBean otherLookupBean =
                            new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                    showValueLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow, hasLookUp);
                }
            }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
                if(lookUpArgument != null){
                    windowTitle = "Lookup Values for - "+lookUpArgument.toUpperCase();
                    Vector vecLookupdata = getLookupValuesFromDB(lookUpArgument, lookUpWindow);
                    OtherLookupBean otherLookupBean =
                            new OtherLookupBean(windowTitle, vecLookupdata, CODE_COLUMN_NAMES);
                    //showLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow, hasLookUp);
                    showCostElementLookupSearchWindow(otherLookupBean, lookUpWindow, vecLookupdata, cValue, selRow, hasLookUp);
                }
            // Added for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
            }else if(ORGANIZATION_SEARCH.equalsIgnoreCase(lookUpWindow)){
                showSearchWindow(ORGANIZATION_SEARCH, cValue, selectedRow);
            }else if(SPONSOR_SEARCH.equalsIgnoreCase(lookUpWindow)){
                showSearchWindow(SPONSOR_SEARCH, cValue, selectedRow);
            }else if(AWARD_LOOKUP_WINDOW.equalsIgnoreCase(lookUpWindow)){
                showAwardSearchWindow(AWARD_SEARCH, cValue, selectedRow);
            }else if(PROPOSAL_DEV_LOOKUP_WINDOW.equalsIgnoreCase(lookUpWindow)){
                showProposalSearchWindow(PROPOSAL_DEV_SEARCH, cValue, selectedRow);
            }
            // Added for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
        } else{
            showNonLookUpWindow();
        }
    }
    
    /** Specifies the non lookup detials */
    private void showNonLookUpWindow(){
        ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
        int rowCount = tblData.getRowCount();
        //   int selectedRow = tblData.getSelectedRow();
        int changedNo = rowCount+1;
        String oldValue = EMPTY_STRING;
        boolean addNewRow = false;
        Equals eqColName = new Equals("columnName", dataSelected);
        CoeusVector cvFilteredColName;
        
        if(cvChangedData == null || cvChangedData.size() == 0) {
            addNewRow = true;
        } else {
            cvFilteredColName = cvChangedData.filter(eqColName);
            if (cvFilteredColName == null || cvFilteredColName.size() == 0) {
                addNewRow = true;
            }
        }
        
        if(addNewRow) {
            if(rowCount > 0){
                oldValue = tblData.getValueAt(rowCount-1,2).toString();
                proposalDataOveridesBean.setOldDisplayValue(oldValue);
            }else{
                //proposalDataOveridesBean.setOldDisplayValue(EMPTY_STRING);
                proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
                
            }
            proposalDataOveridesBean.setDisplayValue(EMPTY_STRING);
            proposalDataOveridesBean.setComments(EMPTY_STRING);
            proposalDataOveridesBean.setChangedNumber(changedNo);
            proposalDataOveridesBean.setColumnName(dataSelected);
            proposalDataOveridesBean.setChangedValue(EMPTY_STRING);
            proposalDataOveridesBean.setProposalNumber(proposalId);
            proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
            modified = true;
            
            cvDataValues.add(proposalDataOveridesBean);
            cvChangedData.add(proposalDataOveridesBean);
            proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
            
            if(rowCount==0){
                dataOverrideTableModel.fireTableRowsInserted(rowCount,rowCount);
            }else{
                dataOverrideTableModel.fireTableRowsInserted(rowCount,rowCount+1);
            }
            if(tblData.getRowCount()  > 0){
                editableRow = tblData.getRowCount() - 1;
            }
        }
    }
    
    /** Validate the form data before saving
     * @return
     */
    private boolean validateData(){
        String newValue = null;
        String colLabel = null;
        String deadLineTypeValue = null;
        String deadLineDateValue = null;
        String formatedDate = null;
        
        for(int index = 0 ; index < cvDataObjects.size(); index++ ){
            proposalColumnsToAlterBean = (ProposalColumnsToAlterBean)cvDataObjects.get(index);
            cvDataValues = proposalColumnsToAlterBean.getProposalDataOverides();
            if(cvDataValues != null && cvDataValues.size() > 0){
                for(int dataIndex = 0; dataIndex < cvDataValues.size(); dataIndex++){
                    proposalDataOveridesBean = (ProposalDataOveridesBean)cvDataValues.get(dataIndex);
                    if(proposalDataOveridesBean.getAcType()==null){
                        continue;
                    }
                    if(proposalDataOveridesBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        newValue = tblData.getValueAt(dataIndex,NEW_VALUE).toString().trim();
                        colLabel = proposalColumnsToAlterBean.getColumnLabel();
                        
                        //Modified for Coeus 4.3 enhancement : Data Override Nullable validation - start
                        //Check for empty string and null value only if the field is not nullable
                        if((newValue==null || newValue.equals(EMPTY_STRING)) && !proposalColumnsToAlterBean.isNullable()){
                            //Modified for Coeus 4.3 enhancement: Data Override Nullable validation - end
                            
                            CoeusOptionPane.showInfoDialog("Invalid data has been entered for "+colLabel + " ");
                            tblColumn.setRowSelectionInterval(index, index);
                            tblData.setRowSelectionInterval(dataIndex,dataIndex);
                            dataCellEditor.txtNewValue.setEditable(true);
                            tblData.editCellAt(dataIndex,NEW_VALUE);
                            return false;
                        }

                        if(colLabel.equals(DEADLINE_TYPE)){
                            
                            deadLineTypeValue = tblData.getValueAt(dataIndex, NEW_VALUE).toString();
                            if(!(deadLineTypeValue.trim().equalsIgnoreCase("P") ||
                                    deadLineTypeValue.trim().equalsIgnoreCase("R"))){
                                dataCellEditor.txtNewValue.setEditable(true);
                                dataCellEditor.txtComments.setEditable(true);
                                CoeusOptionPane.showInfoDialog("Invalid character \'" +deadLineTypeValue +"\' has been entered for " + colLabel +" should be 'P' or 'R'. " );
                                tblColumn.setRowSelectionInterval(index, index);
                                tblData.setRowSelectionInterval(dataIndex,dataIndex);
                                tblData.editCellAt(dataIndex,NEW_VALUE);
                                return false;
                            }
                            
                        }
                        
                        else if(colLabel.equals(DEADLINE_DATE)){
                            deadLineDateValue =  tblData.getValueAt(dataIndex, NEW_VALUE).toString().trim();
                            formatedDate = dtUtils.formatDate(deadLineDateValue,DATE_SEPARATERS,DATE_FORMAT);
                            formatedDate = dtUtils.restoreDate(formatedDate, DATE_SEPARATERS);
                            if(formatedDate==null){
                                CoeusOptionPane.showInfoDialog("Invalid Date has been entered for  " +colLabel );
                                tblColumn.setRowSelectionInterval(index, index);
                                tblData.setRowSelectionInterval(dataIndex,dataIndex);
                                tblData.editCellAt(dataIndex,NEW_VALUE);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    
    /** Get the lookup window
     * @param lookUpArgument
     * @param lookUpWindow
     * @return
     */
    private Vector getLookupValuesFromDB(String lookUpArgument, String lookUpWindow){
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        Vector serverLookUpDataObject = null;
        if( lookUpWindow.equalsIgnoreCase(VALUE_LOOKUP_WINDOW) ){
            request.setDataObject("DW_GET_ARG_VALUE_DESC_TBL_NEW");
        }else if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)){
            request.setDataObject("DW_GET_COST_ELEMENTS");
        }else{
            request.setDataObject("DW_GET_ARG_CODE_TBL_NEW");
        }
        request.setId(lookUpArgument);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                serverLookUpDataObject = (Vector)response.getDataObject();
            }
        }
        return serverLookUpDataObject;
    }
    
    // This method shows the lookup search window
    private void showLookupSearchWindow(OtherLookupBean otherLookupBean,String lookUpWindow,
            Vector vecLookupdata, String columnValue, int selectedRow, boolean lookUp){
        dataCellEditor .stopCellEditing();
        ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
        
        String oldValue = EMPTY_STRING;
        int rowCount = tblData.getRowCount();
        String description = null;
        ComboBoxBean cBean = null;
        if(otherLookupBean != null){
            if(lookUpWindow.equalsIgnoreCase(COST_ELEMENT_LOOKUP_WINDOW)) {
                CostElementsLookupWindow costElementsLookupWindow = new CostElementsLookupWindow(otherLookupBean);
            } else {
                CoeusTableWindow coeusTableWindow = new CoeusTableWindow(otherLookupBean);
            }
        }
        int selRow = otherLookupBean.getSelectedInd();
        
        //If data bean already present, then remove it and add again
        boolean addNewRow = false;
        Equals eqColName = new Equals("columnName", dataSelected);
        CoeusVector cvFilteredColName;
        NotEquals neqColName = new NotEquals("columnName", dataSelected);
        ProposalDataOveridesBean dataOveridesBean = null;
        
        if(cvChangedData == null || cvChangedData.size() == 0) {
            addNewRow = true;
        } else {
            cvFilteredColName = cvChangedData.filter(eqColName);
            if (cvFilteredColName == null || cvFilteredColName.size() == 0) {
                addNewRow = true;
            } else {
                dataOveridesBean = (ProposalDataOveridesBean) cvFilteredColName.get(0);
            }
            cvChangedData = cvChangedData.filter(neqColName);
        }
        
        if(vecLookupdata != null  && selRow >= 0){
            cBean = (ComboBoxBean)vecLookupdata.elementAt(selRow);
            if(cBean != null){
                String code = (String)cBean.getCode();
                description = (String)cBean.getDescription();
                
                if(rowCount > 0 ){
                    if (addNewRow) {
                        oldValue = tblData.getValueAt(rowCount-1, NEW_VALUE).toString();
                        proposalDataOveridesBean.setOldDisplayValue(oldValue);
                        proposalDataOveridesBean.setChangedNumber(rowCount + 1);
                        proposalDataOveridesBean.setChangedValue(code);
                        proposalDataOveridesBean.setDisplayValue(description);
                    } else {
                        proposalDataOveridesBean.setOldDisplayValue(
                                dataOveridesBean.getOldDisplayValue());
                        proposalDataOveridesBean.setChangedNumber(
                                dataOveridesBean.getChangedNumber());
                    }
                    proposalDataOveridesBean.setChangedValue(code);
                    proposalDataOveridesBean.setDisplayValue(description);
                    modified=true;
                } else{
                    proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
                    proposalDataOveridesBean.setChangedValue(code);
                    proposalDataOveridesBean.setDisplayValue(description);
                    proposalDataOveridesBean.setChangedNumber(rowCount+1);
                    modified=true;
                }
                
                try{
                    proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
                    proposalDataOveridesBean.setProposalNumber(proposalId);
                    proposalDataOveridesBean.setColumnName(dataSelected);
                    
                    
                    modified=true;
                    // Added for Case # 2162 - adding Award Type - Start
                    if(proposalDataOveridesBean.getColumnName().equalsIgnoreCase("AWARD_TYPE_CODE")){
                        if(proposalDataOveridesBean.getChangedValue() == null 
                        || proposalDataOveridesBean.getChangedValue().equals("")){
                            proposalDataOveridesBean.setChangedValue("null");
                        }
                    }
                    //Added for Case #2162 - adding Award Type - End
                    //Remove the old bean if any, before adding again
                    if (dataOveridesBean != null) {
                        cvDataValues.remove(dataOveridesBean);
                    }
                    cvDataValues.add(proposalDataOveridesBean);
                    cvChangedData.add(proposalDataOveridesBean);
                    
                    
                    proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
                    
                    if (addNewRow) {
                        if(tblData.getRowCount() == 0){
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount());
                        } else{
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount()+1);
                        }
                    } else {
                        dataOverrideTableModel.fireTableCellUpdated(tblData.getSelectedRow(),NEW_VALUE);
                    }
                    if(tblData.getRowCount()  > 0){
                        editableRow = tblData.getRowCount() - 1;
                    }
                }catch(Exception exc){
                    exc.printStackTrace();
                }
                tblData.getSelectionModel().setLeadSelectionIndex(selectedRow);
            }
        }
    }
    
    /** If the selected column is cost elemnt then the lookup will be cost
     *element . This lookup window will be opened when the lookup window is of type
     *cost element
     */
    private void showCostElementLookupSearchWindow(OtherLookupBean otherLookupBean,String lookUpWindow,
            Vector vecLookupdata, String columnValue, int selectedRow, boolean lookUp){
        
        ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
        
        String oldValue = EMPTY_STRING;
        int rowCount = tblData.getRowCount();
        String description = null;
        ComboBoxBean cBean = null;
        if(otherLookupBean != null){
            CostElementsLookupWindow costElementsLookupWindow = new CostElementsLookupWindow(otherLookupBean);
        }
        int selRow = otherLookupBean.getSelectedInd();
        
        //If data bean already present, then remove it and add again
        boolean addNewRow = false;
        Equals eqColName = new Equals("columnName", dataSelected);
        CoeusVector cvFilteredColName;
        NotEquals neqColName = new NotEquals("columnName", dataSelected);
        ProposalDataOveridesBean dataOveridesBean = null;
        
        if(cvChangedData == null || cvChangedData.size() == 0) {
            addNewRow = true;
        } else {
            cvFilteredColName = cvChangedData.filter(eqColName);
            if (cvFilteredColName == null || cvFilteredColName.size() == 0) {
                addNewRow = true;
            } else {
                dataOveridesBean = (ProposalDataOveridesBean) cvFilteredColName.get(0);
            }
            cvChangedData = cvChangedData.filter(neqColName);
        }
        
        if(vecLookupdata != null  && selRow >= 0){
            cBean = (ComboBoxBean)vecLookupdata.elementAt(selRow);
            if(cBean != null){
                String code = (String)cBean.getCode();
                description = (String)cBean.getDescription();
                
                if(rowCount > 0 ){
                    if (addNewRow) {
                        oldValue = tblData.getValueAt(rowCount-1, NEW_VALUE).toString();
                        proposalDataOveridesBean.setOldDisplayValue(oldValue);
                        proposalDataOveridesBean.setChangedNumber(rowCount + 1);
                        proposalDataOveridesBean.setChangedValue(code);
                        proposalDataOveridesBean.setDisplayValue(description);
                    } else {
                        proposalDataOveridesBean.setOldDisplayValue(
                                dataOveridesBean.getOldDisplayValue());
                        proposalDataOveridesBean.setChangedNumber(
                                dataOveridesBean.getChangedNumber());
                    }
                    proposalDataOveridesBean.setChangedValue(code);
                    proposalDataOveridesBean.setDisplayValue(description);
                    modified=true;
                } else{
                    proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
                    proposalDataOveridesBean.setChangedValue(code);
                    proposalDataOveridesBean.setDisplayValue(description);
                    proposalDataOveridesBean.setChangedNumber(rowCount+1);
                    modified=true;
                }
                
                try{
                    proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
                    proposalDataOveridesBean.setProposalNumber(proposalId);
                    proposalDataOveridesBean.setColumnName(dataSelected);
                    
                    
                    modified=true;
                    
                    
                    //Remove the old bean if any, before adding again
                    if (dataOveridesBean != null) {
                        cvDataValues.remove(dataOveridesBean);
                    }
                    cvDataValues.add(proposalDataOveridesBean);
                    cvChangedData.add(proposalDataOveridesBean);
                    
                    
                    proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
                    
                    if (addNewRow) {
                        if(tblData.getRowCount() == 0){
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount());
                        } else{
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount()+1);
                        }
                    } else {
                        dataOverrideTableModel.fireTableCellUpdated(tblData.getSelectedRow(),NEW_VALUE);
                    }
                    if(tblData.getRowCount()  > 0){
                        editableRow = tblData.getRowCount() - 1;
                    }
                }catch(Exception exc){
                    exc.printStackTrace();
                }
                tblData.getSelectionModel().setLeadSelectionIndex(selectedRow);
            }
        }
    }
    /** Specifies the Value Lookup window
     */
    private void showValueLookupSearchWindow(OtherLookupBean otherLookupBean,String lookUpWindow,
            Vector vecLookupdata, String columnValue, int selectedRow, boolean lookUp)  {
        ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
        
        String oldValue = EMPTY_STRING;
        int rowCount = tblData.getRowCount();
        String description = null;
        ComboBoxBean cBean = null;
        if(otherLookupBean != null){
            CoeusTableWindow coeusTableWindow = new CoeusTableWindow(otherLookupBean);
        }
        int selRow = otherLookupBean.getSelectedInd();
        
        //If data bean already present, then remove it and add again
        boolean addNewRow = false;
        Equals eqColName = new Equals("columnName", dataSelected);
        CoeusVector cvFilteredColName;
        NotEquals neqColName = new NotEquals("columnName", dataSelected);
        ProposalDataOveridesBean dataOveridesBean = null;
        
        if(cvChangedData == null || cvChangedData.size() == 0) {
            addNewRow = true;
        } else {
            cvFilteredColName = cvChangedData.filter(eqColName);
            if (cvFilteredColName == null || cvFilteredColName.size() == 0) {
                addNewRow = true;
            } else {
                dataOveridesBean = (ProposalDataOveridesBean) cvFilteredColName.get(0);
            }
            cvChangedData = cvChangedData.filter(neqColName);
        }
        
        if(vecLookupdata != null  && selRow >= 0){
            cBean = (ComboBoxBean)vecLookupdata.elementAt(selRow);
            if(cBean != null){
                String code = (String)cBean.getCode();
                description = (String)cBean.getDescription();
                
                if(rowCount > 0 ){
                    if (addNewRow) {
                        oldValue = tblData.getValueAt(rowCount-1, NEW_VALUE).toString();
                        proposalDataOveridesBean.setOldDisplayValue(oldValue);
                        proposalDataOveridesBean.setChangedNumber(rowCount + 1);
                        proposalDataOveridesBean.setChangedValue(code);
                        proposalDataOveridesBean.setDisplayValue(description);
                    } else {
                        proposalDataOveridesBean.setOldDisplayValue(
                                dataOveridesBean.getOldDisplayValue());
                        proposalDataOveridesBean.setChangedNumber(
                                dataOveridesBean.getChangedNumber());
                    }
                    proposalDataOveridesBean.setChangedValue(code);
                    proposalDataOveridesBean.setDisplayValue(description);
                    modified=true;
                } else{
                    proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
                    proposalDataOveridesBean.setChangedValue(code);
                    proposalDataOveridesBean.setDisplayValue(description);
                    proposalDataOveridesBean.setChangedNumber(rowCount+1);
                    modified=true;
                }
                
                try{
                    proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
                    proposalDataOveridesBean.setProposalNumber(proposalId);
                    proposalDataOveridesBean.setColumnName(dataSelected);
                    
                    
                    modified=true;
                    
                    
                    //Remove the old bean if any, before adding again
                    if (dataOveridesBean != null) {
                        cvDataValues.remove(dataOveridesBean);
                    }
                    cvDataValues.add(proposalDataOveridesBean);
                    cvChangedData.add(proposalDataOveridesBean);
                    
                    
                    proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
                    
                    if (addNewRow) {
                        if(tblData.getRowCount() == 0){
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount());
                        } else{
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount()+1);
                        }
                    } else {
                        dataOverrideTableModel.fireTableCellUpdated(tblData.getSelectedRow(),NEW_VALUE);
                    }
                    if(tblData.getRowCount()  > 0){
                        editableRow = tblData.getRowCount() - 1;
                    }
                }catch(Exception exc){
                    exc.printStackTrace();
                }
                tblData.getSelectionModel().setLeadSelectionIndex(selectedRow);
            }
        }
    }
    
    /** If the Lookup window is person search select a single person
     */
    private void showPersonSearchWindow(String searchType, String colValue, int selectedRow){
        try{
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, searchType, 1);
            coeusSearch.showSearchWindow();
            HashMap personInfo = coeusSearch.getSelectedRow();
            String pID = null;
            String name = null;
            if(personInfo!=null){
                pID = Utils.
                        convertNull(personInfo.get( "PERSON_ID" ));
                name = Utils.
                        convertNull(personInfo.get( "FULL_NAME" ));
                
                
                ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
                String oldValue = EMPTY_STRING;
                int rowCount = tblData.getRowCount();
                
                
                //If data bean already present, then remove it and add again
                boolean addNewRow = false;
                Equals eqColName = new Equals("columnName", dataSelected);
                CoeusVector cvFilteredColName;
                NotEquals neqColName = new NotEquals("columnName", dataSelected);
                ProposalDataOveridesBean dataOveridesBean = null;
                
                if(cvChangedData == null || cvChangedData.size() == 0) {
                    addNewRow = true;
                } else {
                    cvFilteredColName = cvChangedData.filter(eqColName);
                    if (cvFilteredColName == null || cvFilteredColName.size() == 0) {
                        addNewRow = true;
                    } else {
                        dataOveridesBean = (ProposalDataOveridesBean) cvFilteredColName.get(0);
                    }
                    cvChangedData = cvChangedData.filter(neqColName);
                }
                if(rowCount > 0 ){
                    if (addNewRow) {
                        oldValue = tblData.getValueAt(rowCount-1, NEW_VALUE).toString();
                        proposalDataOveridesBean.setOldDisplayValue(oldValue);
                        proposalDataOveridesBean.setChangedNumber(rowCount + 1);
                        proposalDataOveridesBean.setChangedValue(pID);
                        proposalDataOveridesBean.setDisplayValue(name);
                    } else {
                        proposalDataOveridesBean.setOldDisplayValue(
                                dataOveridesBean.getOldDisplayValue());
                        proposalDataOveridesBean.setChangedNumber(
                                dataOveridesBean.getChangedNumber());
                    }
                    proposalDataOveridesBean.setChangedValue(pID);
                    proposalDataOveridesBean.setDisplayValue(name);
                    
                    modified=true;
                } else{
                    proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
                    proposalDataOveridesBean.setChangedValue(pID);
                    proposalDataOveridesBean.setDisplayValue(name);
                    proposalDataOveridesBean.setChangedNumber(rowCount+1);
                    modified=true;
                }
                
                try{
                    proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
                    proposalDataOveridesBean.setProposalNumber(proposalId);
                    proposalDataOveridesBean.setColumnName(dataSelected);
                    
                    
                    modified=true;
                    
                    
                    //Remove the old bean if any, before adding again
                    if (dataOveridesBean != null) {
                        cvDataValues.remove(dataOveridesBean);
                    }
                    cvDataValues.add(proposalDataOveridesBean);
                    cvChangedData.add(proposalDataOveridesBean);
                    
                    
                    proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
                    
                    if (addNewRow) {
                        if(tblData.getRowCount() == 0){
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount());
                        } else{
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount()+1);
                        }
                    } else {
                        dataOverrideTableModel.fireTableCellUpdated(tblData.getSelectedRow(),NEW_VALUE);
                    }
                    if(tblData.getRowCount()  > 0){
                        editableRow = tblData.getRowCount() - 1;
                    }
                }catch(Exception exc){
                    exc.printStackTrace();
                }
                tblData.getSelectionModel().setLeadSelectionIndex(selectedRow);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** Specifies the Unit search. If the lookup type is Unit then the lookup
     *window will be unit search
     */
    private void showUnitSearchWindow(String searchType, String colValue, int selectedRow){
        try{
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, searchType, 1);
            coeusSearch.showSearchWindow();
            HashMap unitInfo = coeusSearch.getSelectedRow();
            String ID = null;
            String name = null;
            if(unitInfo!=null){
                ID = Utils.
                        convertNull(unitInfo.get( "UNIT_NUMBER" ));
                name = Utils.
                        convertNull(unitInfo.get( "UNIT_NAME" ));
                
                ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
                String oldValue = EMPTY_STRING;
                int rowCount = tblData.getRowCount();
                
                
                //If data bean already present, then remove it and add again
                boolean addNewRow = false;
                Equals eqColName = new Equals("columnName", dataSelected);
                CoeusVector cvFilteredColName;
                NotEquals neqColName = new NotEquals("columnName", dataSelected);
                ProposalDataOveridesBean dataOveridesBean = null;
                
                if(cvChangedData == null || cvChangedData.size() == 0) {
                    addNewRow = true;
                } else {
                    cvFilteredColName = cvChangedData.filter(eqColName);
                    if (cvFilteredColName == null || cvFilteredColName.size() == 0) {
                        addNewRow = true;
                    } else {
                        dataOveridesBean = (ProposalDataOveridesBean) cvFilteredColName.get(0);
                    }
                    cvChangedData = cvChangedData.filter(neqColName);
                }
                if(rowCount > 0 ){
                    if (addNewRow) {
                        oldValue = tblData.getValueAt(rowCount-1, NEW_VALUE).toString();
                        proposalDataOveridesBean.setOldDisplayValue(oldValue);
                        proposalDataOveridesBean.setChangedNumber(rowCount + 1);
                        proposalDataOveridesBean.setChangedValue(ID);
                        proposalDataOveridesBean.setDisplayValue(name);
                    } else {
                        proposalDataOveridesBean.setOldDisplayValue(
                                dataOveridesBean.getOldDisplayValue());
                        proposalDataOveridesBean.setChangedNumber(
                                dataOveridesBean.getChangedNumber());
                    }
                    proposalDataOveridesBean.setChangedValue(ID);
                    proposalDataOveridesBean.setDisplayValue(name);
                    modified=true;
                } else{
                    proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
                    proposalDataOveridesBean.setChangedValue(ID);
                    proposalDataOveridesBean.setDisplayValue(name);
                    proposalDataOveridesBean.setChangedNumber(rowCount+1);
                    modified=true;
                }
                
                try{
                    proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
                    proposalDataOveridesBean.setProposalNumber(proposalId);
                    proposalDataOveridesBean.setColumnName(dataSelected);
                    
                    
                    modified=true;
                    
                    
                    //Remove the old bean if any, before adding again
                    if (dataOveridesBean != null) {
                        cvDataValues.remove(dataOveridesBean);
                    }
                    cvDataValues.add(proposalDataOveridesBean);
                    cvChangedData.add(proposalDataOveridesBean);
                    
                    
                    proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
                    
                    if (addNewRow) {
                        if(tblData.getRowCount() == 0){
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount());
                        } else{
                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount()+1);
                        }
                    } else {
                        dataOverrideTableModel.fireTableCellUpdated(tblData.getSelectedRow(),NEW_VALUE);
                    }
                    if(tblData.getRowCount()  > 0){
                        editableRow = tblData.getRowCount() - 1;
                    }
                }catch(Exception exc){
                    exc.printStackTrace();
                }
                tblData.getSelectionModel().setLeadSelectionIndex(selectedRow);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        
    }
    
    /** Specifies the Rolodex lookup window and the Rolodex search window will be
     *populated if the selected type is rolodex
     */
    private void showRolodexSearch(String searchType, String colValue, int selectedRow){
        try{
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, searchType, 1);
            coeusSearch.showSearchWindow();
            HashMap rolodexInfo = coeusSearch.getSelectedRow();
            String ID = null;
            String name = null;
            String firstName = null;
            String lastName = null;
            String middleName = null;
            String namePreffix = null;
            String nameSuffix = null;
            
            if(rolodexInfo!=null){
                ID = Utils.convertNull(rolodexInfo.get( "ROLODEX_ID" ));
                firstName = Utils.convertNull(rolodexInfo.get( "FIRST_NAME" ));
                lastName = Utils.convertNull(rolodexInfo.get( "LAST_NAME" ));
                middleName = Utils.convertNull(rolodexInfo.get( "MIDDLE_NAME" ));
                namePreffix = Utils.convertNull(rolodexInfo.get( "PREFIX" ));
                nameSuffix = Utils.convertNull(rolodexInfo.get( "SUFFIX" ));
                
             /* construct full name of the rolodex if his last name is present
                  otherwise use his organization name to display in person name
                  column of investigator table */
                if ( lastName.length() > 0) {
                    name = ( lastName + " "+nameSuffix +", "+ namePreffix
                            +" "+firstName +" "+ middleName ).trim();
                } else {
                    name = Utils.convertNull(rolodexInfo.get("ORGANIZATION"));//result.get("ORGANIZATION"));
                }
                
                // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
                // Record updates are moved to updateDataOverrideBean method
//                ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
//                String oldValue = EMPTY_STRING;
//                int rowCount = tblData.getRowCount();
//                
//                
//                //If data bean already present, then remove it and add again
//                boolean addNewRow = false;
//                Equals eqColName = new Equals("columnName", dataSelected);
//                CoeusVector cvFilteredColName;
//                NotEquals neqColName = new NotEquals("columnName", dataSelected);
//                ProposalDataOveridesBean dataOveridesBean = null;
//                
//                if(cvChangedData == null || cvChangedData.size() == 0) {
//                    addNewRow = true;
//                } else {
//                    cvFilteredColName = cvChangedData.filter(eqColName);
//                    if (cvFilteredColName == null || cvFilteredColName.size() == 0) {
//                        addNewRow = true;
//                    } else {
//                        dataOveridesBean = (ProposalDataOveridesBean) cvFilteredColName.get(0);
//                    }
//                    cvChangedData = cvChangedData.filter(neqColName);
//                }
//                if(rowCount > 0 ){
//                    if (addNewRow) {
//                        oldValue = tblData.getValueAt(rowCount-1, NEW_VALUE).toString();
//                        proposalDataOveridesBean.setOldDisplayValue(oldValue);
//                        proposalDataOveridesBean.setChangedNumber(rowCount + 1);
//                        proposalDataOveridesBean.setChangedValue(ID);
//                        proposalDataOveridesBean.setDisplayValue(name);
//                    } else {
//                        proposalDataOveridesBean.setOldDisplayValue(
//                                dataOveridesBean.getOldDisplayValue());
//                        proposalDataOveridesBean.setChangedNumber(
//                                dataOveridesBean.getChangedNumber());
//                    }
//                    proposalDataOveridesBean.setChangedValue(ID);
//                    proposalDataOveridesBean.setDisplayValue(name);
//                    modified=true;
//                } else{
//                    proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
//                    proposalDataOveridesBean.setChangedValue(ID);
//                    proposalDataOveridesBean.setDisplayValue(name);
//                    proposalDataOveridesBean.setChangedNumber(rowCount+1);
//                    modified=true;
//                }
//                
//                try{
//                    proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
//                    proposalDataOveridesBean.setProposalNumber(proposalId);
//                    proposalDataOveridesBean.setColumnName(dataSelected);
//                    
//                    
//                    modified=true;
//                    
//                    
//                    //Remove the old bean if any, before adding again
//                    if (dataOveridesBean != null) {
//                        cvDataValues.remove(dataOveridesBean);
//                    }
//                    cvDataValues.add(proposalDataOveridesBean);
//                    cvChangedData.add(proposalDataOveridesBean);
//                    
//                    
//                    proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
//                    
//                    if (addNewRow) {
//                        if(tblData.getRowCount() == 0){
//                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount());
//                        } else{
//                            dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount()+1);
//                        }
//                    } else {
//                        dataOverrideTableModel.fireTableCellUpdated(tblData.getSelectedRow(),NEW_VALUE);
//                    }
//                    if(tblData.getRowCount()  > 0){
//                        editableRow = tblData.getRowCount() - 1;
//                    }
//                }catch(Exception exc){
//                    exc.printStackTrace();
//                }
//                tblData.getSelectionModel().setLeadSelectionIndex(selectedRow);
                updateDataOverrideBean(ID,name,selectedRow);                
                // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
    /*
     * Method to show the Coeus search window
     * @param String - searchType
     * @param String - colValue
     * @param int - selectedRow
     */
    private void showSearchWindow(String searchType, String colValue, int selectedRow){
        try{
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, searchType, 1);
            coeusSearch.showSearchWindow();
            HashMap personInfo = coeusSearch.getSelectedRow();
            if(personInfo!=null){
                String personID = Utils.convertNull(personInfo.get("ID"));
                String personName = Utils.convertNull(personInfo.get("NAME"));
               updateDataOverrideBean(personID,personName,selectedRow);                
            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }
    
    
    /*
     * Method to show the Proposal search window
     * @param String - searchType
     * @param String - colValue
     * @param int - selectedRow
     */
    private void showProposalSearchWindow(String searchType, String colValue, int selectedRow){
        try{
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, searchType, 1);
            coeusSearch.showSearchWindow();
            HashMap proposalInfo = coeusSearch.getSelectedRow();
            if(proposalInfo!=null){
                String proposalNumber = Utils.convertNull(proposalInfo.get("PROPOSAL_NUMBER"));
                updateDataOverrideBean(proposalNumber,proposalNumber,selectedRow);
            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }
    
    
    /*
     * Method to show the Award search window
     * @param String - searchType
     * @param String - colValue
     * @param int - selectedRow
     */
    private void showAwardSearchWindow(String searchType, String colValue, int selectedRow){
        try{
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, searchType, 1);
            coeusSearch.showSearchWindow();
            HashMap awardInfo = coeusSearch.getSelectedRow();
            if(awardInfo!=null){
                String awardNumber = Utils.convertNull(awardInfo.get("MIT_AWARD_NUMBER"));
                updateDataOverrideBean(awardNumber,awardNumber,selectedRow);
            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }
    
    /*
     * Method to update the changes to the ProposalDAtaOveridesBean
     * @param String - fieldId
     * @param String - fieldName
     * @param int - selectedRow
     */
    private void updateDataOverrideBean(String valueToDB, String displayValue, int selectedRow) {
        String oldValue = EMPTY_STRING;
        int rowCount = tblData.getRowCount();
        //If data bean already present, then remove it and add again
        boolean addNewRow = false;
        Equals eqColName = new Equals("columnName", dataSelected);
        CoeusVector cvFilteredColName;
        NotEquals neqColName = new NotEquals("columnName", dataSelected);
        ProposalDataOveridesBean dataOveridesBean = null;
        ProposalDataOveridesBean proposalDataOveridesBean = new ProposalDataOveridesBean();
        if(cvChangedData == null || cvChangedData.isEmpty()) {
            addNewRow = true;
        } else {
            cvFilteredColName = cvChangedData.filter(eqColName);
            if (cvFilteredColName == null || cvFilteredColName.isEmpty()) {
                addNewRow = true;
            } else {
                dataOveridesBean = (ProposalDataOveridesBean) cvFilteredColName.get(0);
            }
            cvChangedData = cvChangedData.filter(neqColName);
        }
        if(rowCount > 0 ){
            if (addNewRow) {
                oldValue = tblData.getValueAt(rowCount-1, NEW_VALUE).toString();
                proposalDataOveridesBean.setOldDisplayValue(oldValue);
                proposalDataOveridesBean.setChangedNumber(rowCount + 1);
                proposalDataOveridesBean.setChangedValue(valueToDB);
                proposalDataOveridesBean.setDisplayValue(displayValue);
            } else {
                proposalDataOveridesBean.setOldDisplayValue(
                        dataOveridesBean.getOldDisplayValue());
                proposalDataOveridesBean.setChangedNumber(
                        dataOveridesBean.getChangedNumber());
            }
            proposalDataOveridesBean.setChangedValue(valueToDB);
            proposalDataOveridesBean.setDisplayValue(displayValue);
            modified=true;
        } else{
            proposalDataOveridesBean.setOldDisplayValue(proposalColumnsToAlterBean.getDefaultValue());
            proposalDataOveridesBean.setChangedValue(valueToDB);
            proposalDataOveridesBean.setDisplayValue(displayValue);
            proposalDataOveridesBean.setChangedNumber(rowCount+1);
            modified=true;
        }
        
        proposalDataOveridesBean.setAcType(TypeConstants.INSERT_RECORD);
        proposalDataOveridesBean.setProposalNumber(proposalId);
        proposalDataOveridesBean.setColumnName(dataSelected);
        modified=true;
        //Remove the old bean if any, before adding again
        if (dataOveridesBean != null) {
            cvDataValues.remove(dataOveridesBean);
        }
        cvDataValues.add(proposalDataOveridesBean);
        cvChangedData.add(proposalDataOveridesBean);
        
        proposalColumnsToAlterBean.setProposalDataOverides(cvDataValues);
        
        if (addNewRow) {
            if(tblData.getRowCount() == 0){
                dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount());
            } else{
                dataOverrideTableModel.fireTableRowsInserted(tblData.getRowCount(),tblData.getRowCount()+1);
            }
        } else {
            dataOverrideTableModel.fireTableCellUpdated(tblData.getSelectedRow(),NEW_VALUE);
        }
        if(tblData.getRowCount()  > 0){
            editableRow = tblData.getRowCount() - 1;
        }
        
        tblData.getSelectionModel().setLeadSelectionIndex(selectedRow);
    }
    // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
    
    /**
     * This method is used to perform the Window closing operation
     */
    
    private void performWindowClosing()throws Exception{
        dataCellEditor.stopCellEditing();
        int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
        if(option == CoeusOptionPane.SELECTION_YES){
            if(validateData()){
                saveRequired = true;
                updateInboxDetails();
            }
        }else if(option == CoeusOptionPane.SELECTION_NO){
            saveRequired = false;
            dlgWindow.dispose();
        }else if(option==CoeusOptionPane.SELECTION_CANCEL){
            return;
        }
    }
    
    
    /** This method is used to get the saveRequired Flag
     *
     * @return boolean true if changes are made in the form, else false
     */
    public boolean isSaveRequired(){
        try{
            saveRequired = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return saveRequired;
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
        //Case 3810  - include !hasRight also in the IF Condition
        if(viewOnlyMode || !hasRight){
            return;
        }
        dataCellEditor.stopCellEditing();
        boolean hasLookUpWindow = false;
        for(int index = 0;index < cvDataValues.size(); index++){
            proposalDataOveridesBean = (ProposalDataOveridesBean)cvDataValues.get(index);
            hasLookUpWindow = proposalDataOveridesBean .isHasLookUp();
        }
        //if(hasLookUpWindow){
        if(e.getClickCount()==2){
            showLookUpWindow();
        }else{
            return;
        }
        //}
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    /** Getter for property proposalDevelopmentFormBean.
     * @return Value of property proposalDevelopmentFormBean.
     *
     */
    public edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }
    
    /** Setter for property proposalDevelopmentFormBean.
     * @param proposalDevelopmentFormBean New value of property proposalDevelopmentFormBean.
     *
     */
    public void setProposalDevelopmentFormBean(edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnChange;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblDataOverride;
    public javax.swing.JLabel lblOverridedData;
    public javax.swing.JLabel lblProposalNumber;
    public javax.swing.JLabel ldlColumnOverridden;
    public javax.swing.JScrollPane scrPnColumn;
    public javax.swing.JScrollPane scrPnData;
    public javax.swing.JTable tblColumn;
    public javax.swing.JTable tblData;
    // End of variables declaration//GEN-END:variables
    
    
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","changedNumber" },
            {"1","oldDisplayValue"},
            {"2","changedValue" },
            {"3","comments" },
        };
        boolean sort =true;
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int size = cvDataValues.size();
                for(int index =0;index < cvDataValues.size() ;index++) {
                    ProposalDataOveridesBean  proposalDataOveridesBean = (ProposalDataOveridesBean) cvDataValues.get(index);
                    if(proposalDataOveridesBean.getComments() == null){
                        proposalDataOveridesBean.setComments(EMPTY_STRING);
                    }else if(proposalDataOveridesBean.getOldDisplayValue()==null){
                        proposalDataOveridesBean.setOldDisplayValue(EMPTY_STRING);
                    }
                }
                
                if(cvDataValues!=null && cvDataValues.size()>0 &&
                        nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvDataValues).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    dataOverrideTableModel.fireTableRowsUpdated(0, dataOverrideTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
    }// End of ColumnHeaderListener.................
    
    
    /** Specifies the renderer for data table where the border and backgrounds are
     *specified for the columns
     */
    private class DataRenderer extends DefaultTableCellRenderer{
        JTextField txtComments;
        JTextField txtNewValue;
        JTextField txtChangeNo;
        JTextField txtOldValue;
        String formatedDate = null;
        
        DataRenderer(){
            txtComments = new JTextField();
            txtNewValue = new JTextField();
            txtChangeNo = new JTextField();
            txtOldValue = new JTextField();
            
            txtComments.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            
            txtNewValue.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            
            txtChangeNo.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            
            txtOldValue.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            
            
        }
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            proposalDataOveridesBean =(ProposalDataOveridesBean)cvDataValues.get(row);
            switch(column){
                
                case NO_COLUNM:
                    if(proposalDataOveridesBean.getAcType()==null){
                        txtChangeNo.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtChangeNo.setForeground(Color.black);
                    }else{
                        txtChangeNo.setBackground(Color.white);
                        txtChangeNo.setForeground(Color.black);
                    }
                    txtChangeNo.setText(value.toString());
                    txtChangeNo.setHorizontalAlignment(JTextField.LEFT);
                    return txtChangeNo;
                case OLD_VALUE:
                    if(proposalDataOveridesBean.getAcType()==null){
                        txtOldValue.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtOldValue.setForeground(Color.black);
                    }else{
                        txtOldValue.setBackground(Color.white);
                        txtOldValue.setForeground(Color.black);
                    }
                    txtOldValue.setText(value.toString());
                    return txtOldValue;
                case COMMENTS:
                    if(proposalDataOveridesBean.getAcType()==null){
                        txtComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtComments.setForeground(Color.black);
                    }else{
                        txtComments.setBackground(Color.white);
                        txtComments.setForeground(Color.black);
                    }
                    txtComments.setText(value.toString());
                    return txtComments;
                case NEW_VALUE:
                    
                    if(proposalDataOveridesBean.getAcType()==null){
                        txtNewValue.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtNewValue.setForeground(Color.black);
                    }else{
                        txtNewValue.setBackground(Color.white);
                        txtNewValue.setForeground(Color.black);
                    }
                    txtNewValue.setText(value.toString());
                    return txtNewValue;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
                Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
 
}
