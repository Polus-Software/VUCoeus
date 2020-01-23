
/*
 * @(#)AlternativeSearchForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

 /* PMD Check performed and removed unused imports and variables
 * by Md.ehtesham Ansari on 16/12/2010
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.iacuc.bean.ProtocolAlterDatabaseSearchBean;
import edu.mit.coeus.iacuc.bean.ProtocolAlternativeSearchBean;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.util.*;
import java.beans.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.brokers.*;



public class AlternativeSearchForm extends JComponent implements ActionListener, ListSelectionListener, FocusListener {
    
    private DateUtils dtUtils = new DateUtils(); 
    private CoeusAppletMDIForm mdiForm = null;
    private int parentSequenceId;
    private JPanel pnlAltSearchColumns;
    private JScrollPane scrPnCorrespondent;    
    private JScrollPane scrPnAlternativeSearch;
    private JTable tblAltSearchData;
    private JButton btnDelete;    
    private JButton btnAdd;
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
    private JButton btnModify; 
    private JButton btnView; 
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
    private CoeusVector cvComboBoxBeanData = null;    
    ComboBoxBean comboEntry;
    private char functionType ;
    private Vector deletedAlternativeSearch = null;
    private boolean isFormDataUpdated = false;
    private boolean saveRequired = false;
    private boolean firstEntry = false;
    private int lastSelectedRow = 0;   
    private int selectedRow = 0;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    JTextArea txtAltKeySearch;   
    JTextArea txtAltYearSearch;
    JTextArea txtAltComments ;
    String protocolNumber;
    int sequenceNumber;
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
    private Vector vecAltenativeData;
    private ProtocolAlternativeSearchBean oldSearchBeanData;
    private AltSearchTableModel altSearchTableModel;
    //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
//    private static final String SELECT_ALTSEARCH_DATE = "iacucProtoAlterSearchDateFrm_exceptionCode.1000";
//    private static final String SELECT_ALTSEARCH_TYPE = "iacucProtoAlterSearchTypeFrm_exceptionCode.1001";    
//    private static final String SELECT_ALTSEARCH_YEAR =  "iacucProtoAlterSearchYearFrm_exceptionCode.1002";
//    private static final String SELECT_ALTSEARCH_KEYWORD = "iacucProtoAlterSearchKeyFrm_exceptionCode.1003"; 
//    private int ALT_SEARCH_TAB = 15;    
    //Commented for COEUSQA-2714 In the Alternative Search in IACUC-End
    private java.text.SimpleDateFormat dtFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");

//    private static final String SELECT_ALT_SEARCH_TYPE = "iacucProtoAlterSearchTypeFrm_exceptionCode.1001";
    private static final int SEARCHDATE_COLUMN = 1;
    private static final int ALTER_DB_COLUMN = 2;            
//    private static final int ICON_COLUMN = 0;    
    CoeusTextField eq;
    //Added for COEUSQA-2714 User should only have to enter search criteria once-start
    AlternativeSearchDataBaseForm alternativeSearchDataBaseForm;
    Vector vecAltBDSearchForDIsplay = new Vector();
    Vector vecOldAltBDSearchForDIsplay = new Vector();
    //Added for COEUSQA-2714 User should only have to enter search criteria once-end
    /**
     * Creates new form AlternativeSearchForm. Default Constructor.
     */
    public AlternativeSearchForm() {
    }

    /**
     * Creates new form ProtocolCorrespondenceForm with the parent mdiForm
     * @param mdiParentForm parent form window.
     */
    public AlternativeSearchForm( CoeusAppletMDIForm mdiParentForm ) {
        mdiForm = mdiParentForm;
    }

    /**
     * Creates new AlternativeSearchForm form object with Alternative Search data
     */
    public AlternativeSearchForm(Vector vecAltSearch,String protocolNumber, int sequenceNumber, char fnType ) {
        setProtocolNumber(protocolNumber);
        setSequenceNumber(sequenceNumber);
        this.functionType = fnType;
        this.vecAltenativeData = vecAltSearch;
        deletedAlternativeSearch = new Vector();
        fetchData();
    }
      
    public String getProtocolNumber() {
        return protocolNumber;
    }
    
     public int getSequenceNumber() {
        return sequenceNumber;
    }
     
     public void setProtocolNumber(String protocolNumber) {
         this.protocolNumber = protocolNumber;
     }
     
     public void setSequenceNumber(int sequenceNumber) {
         this.sequenceNumber = sequenceNumber;
     }

    /**
     * This method is used to loading the data to GUI
     */
    public void loadFormData()
    {
         fetchData();         
    }
    
    public JComponent AlternativeSearchForm(CoeusAppletMDIForm mdiForm){
        
        this.mdiForm = mdiForm;
        initComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
        
    }

    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void setTableColumnProperties(){
        boolean enabled = (functionType == CoeusGuiConstants.DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE) ? false : true;
         if(functionType == CoeusGuiConstants.DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE){
            
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblAltSearchData.setBackground(bgListColor);
            tblAltSearchData.setSelectionBackground(bgListColor );
            tblAltSearchData.setSelectionForeground(java.awt.Color.BLACK);
            
            txtAltKeySearch.setBackground(this.getBackground());
            txtAltYearSearch.setBackground(this.getBackground());
            txtAltComments.setBackground(this.getBackground());
            btnAdd.setEnabled(false);
            //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
            if(tblAltSearchData.getRowCount()>0){
                btnView.setEnabled(true);
            }
            txtAltYearSearch.setCaretPosition(0);
            txtAltKeySearch.setCaretPosition(0);                
            txtAltComments.setCaretPosition(0);
            //Added for COEUSQA-2714 In the Alternative Search in IACUC-End
            
        } else{
            tblAltSearchData.setBackground(java.awt.Color.white);
            tblAltSearchData.setSelectionBackground(java.awt.Color.white);
            tblAltSearchData.setSelectionForeground(java.awt.Color.black); 
            txtAltKeySearch.setBackground(java.awt.Color.white);
            txtAltYearSearch.setBackground(java.awt.Color.white);
            txtAltComments.setBackground(java.awt.Color.white);
            btnAdd.setEnabled(true);
           
        }
        tblAltSearchData.setOpaque(false);
        tblAltSearchData.setShowVerticalLines(false);
        tblAltSearchData.setShowHorizontalLines(false);
        tblAltSearchData.setRowHeight(22);
        tblAltSearchData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblAltSearchData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = tblAltSearchData.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setPreferredWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setResizable(false);
        
        column = tblAltSearchData.getColumnModel().getColumn(1);
        column.setMinWidth(90);
        column.setMaxWidth(90);
        column.setPreferredWidth(90);
//        setSearchDateEditor(column);
        
        column = tblAltSearchData.getColumnModel().getColumn(2);
        column.setMinWidth(330);
        column.setMaxWidth(330);
        column.setMinWidth(330);
        //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
//         if(TypeConstants.DISPLAY_MODE != functionType){
//            column.setCellEditor(new AlternativeSearchDBEditor());
//        }
        //Commented for COEUSQA-2714 In the Alternative Search in IACUC-End
        
        tblAltSearchData.getTableHeader().setReorderingAllowed( false );
        tblAltSearchData.getTableHeader().setResizingAllowed(false);
        tblAltSearchData.setFont(CoeusFontFactory.getNormalFont());
        tblAltSearchData.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
//        txtAltYearSearch.setEditable(enabled);
//        txtAltKeySearch.setEditable(enabled);
//        txtAltComments.setEditable(enabled);
//        txtAltYearSearch.setEnabled(enabled);
//        txtAltKeySearch.setEnabled(enabled);
//        txtAltComments.setEnabled(enabled);
//        btnAdd.setEnabled(enabled);
//        btnDelete.setEnabled(enabled);
//        
//        enabled = tblAltSearchData.getRowCount() > 0 ? true : false ;
//        if(functionType != TypeConstants.DISPLAY_MODE){
//             txtAltYearSearch.setEnabled(enabled);
//             txtAltComments.setEnabled(enabled);
//             txtAltKeySearch.setEnabled(enabled);
//         }
        //Commented for COEUSQA-2714 In the Alternative Search in IACUC-End
    }
    
    //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
//    private void setSearchDateEditor(TableColumn column){
//                 
//        column.setMinWidth(120);
//        column.setMaxWidth(120);
//        column.setMinWidth(120);
//        eq = new CoeusTextField();
//        eq.addFocusListener(new CustomFocusAdapter()); 
//        eq.setDocument(new LimitedPlainDocument(11));        
//        if(TypeConstants.DISPLAY_MODE != functionType){                
//            column.setCellEditor(new DefaultCellEditor(eq));                         
//        }        
////        setSaveRequired(true);
//    }
         
//    private void setAltSearchDbColumn(TableColumn column){
//         
//        column.setMinWidth(330);
//        column.setMaxWidth(330);
//        column.setMinWidth(330);
//        JComboBox coeusCombo = new JComboBox();
//        coeusCombo.setFont(CoeusFontFactory.getNormalFont());
//        coeusCombo.setModel( new CoeusComboBox(
//        getAltDBSearchComboValues() ,false).getModel());
//        column.setCellEditor(new DefaultCellEditor(coeusCombo ){
//            public Object getCellEditorValue(){
//                return ((JComboBox)getComponent()).getSelectedItem().
//                toString();
//            }
//        });       
//         setSaveRequired(true);
//    }
    //Commented for COEUSQA-2714 In the Alternative Search in IACUC-End    
    
    
    /*
     * Editor for Alternative search combo box
     */
//    class AlternativeSearchDBEditor extends AbstractCellEditor implements TableCellEditor{
//        private JComboBox cmbAltSearchDB;
//        private boolean populated = false;
//        private int column;
//        AlternativeSearchDBEditor() {
//            cmbAltSearchDB = new JComboBox();
//            cmbAltSearchDB.addItemListener(new ItemListener() {
//                public void itemStateChanged(ItemEvent e) {
//                    setSaveRequired(true);
//                }
//            });
//        }
//        private void populateCombo() {
//            cmbAltSearchDB.setModel(new DefaultComboBoxModel(cvComboBoxBeanData));
//        }
//        
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
//            this.column = column;
//            switch(column) {
//                case ALTER_DB_COLUMN:
//                    if(! populated) {
//                        populateCombo();
//                        populated = true;
//                    }
//                    if(comboBoxBean != null && (comboBoxBean.getDescription() == null || 
//                            comboBoxBean.getDescription().equals(CoeusGuiConstants.EMPTY_STRING))) {
//                        ComboBoxBean selBean = (ComboBoxBean)cvComboBoxBeanData.get(0);
//                        cmbAltSearchDB.setSelectedItem(selBean);
//                        return cmbAltSearchDB;
//                    }
//                    cmbAltSearchDB.setSelectedItem(value);
//                    return cmbAltSearchDB;
//                default:
//                    break;
//            }
//            return null;
//        }
//        
//        public Object getCellEditorValue() {
//            this.column = column;
//            switch(column) {
//                case ALTER_DB_COLUMN:
//                    return cmbAltSearchDB.getSelectedItem();
//                default:
//                    break;
//            }
//            return cmbAltSearchDB;
//        }
//        public int getClickCountToStart(){
//            return 1;
//        }
//    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        //COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
        btnModify = new JButton();
        btnModify = new JButton();
        btnModify.addActionListener( this );
        btnModify.setEnabled( false );
        btnModify.setMnemonic( 'M' );
        btnModify.setText("Modify");
        btnModify.setFont( CoeusFontFactory.getLabelFont() );
        btnView = new JButton();
        btnView = new JButton();
        btnView.addActionListener( this );
        btnView.setEnabled( false );
        btnView.setMnemonic( 'V' );
        btnView.setText("View");
        btnView.setFont( CoeusFontFactory.getLabelFont() );
        //COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
        btnDelete = new JButton();
        btnDelete.addActionListener( this );
        btnDelete.setEnabled( false );
        btnDelete.setMnemonic( 'D' );
        btnDelete.setFont( CoeusFontFactory.getLabelFont() );
        btnAdd = new JButton();
        btnAdd.addActionListener( this );
        btnAdd.setMnemonic( 'A' );
        pnlAltSearchColumns = new JPanel();
        tblAltSearchData = new JTable();
        scrPnCorrespondent = new JScrollPane();
        scrPnCorrespondent.setBorder(new TitledBorder(new EtchedBorder(), "Alternative Searches", TitledBorder.LEFT,
        TitledBorder.TOP,CoeusFontFactory.getLabelFont()));         
        scrPnCorrespondent.setMinimumSize(new java.awt.Dimension(500, 200));
        scrPnCorrespondent.setPreferredSize(new java.awt.Dimension(500, 200));

        txtAltYearSearch = new JTextArea();
        txtAltKeySearch = new JTextArea();
        txtAltComments = new JTextArea();
        txtAltYearSearch.addFocusListener( this ); 
        txtAltKeySearch.addFocusListener(this);
        txtAltComments.addFocusListener(this);
        setLayout(new java.awt.GridBagLayout());
        setPreferredSize( new java.awt.Dimension( 600, 450 ));
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("Add");
        pnlAltSearchColumns.setBorder(new TitledBorder(new EtchedBorder(), "", TitledBorder.LEFT,
        TitledBorder.TOP,CoeusFontFactory.getLabelFont()));
        pnlAltSearchColumns.setLayout(new java.awt.GridBagLayout());     
        
        ListSelectionModel perSelectionModel = tblAltSearchData.getSelectionModel();
        perSelectionModel.addListSelectionListener( this );
        perSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        perSelectionModel.setSelectionInterval( 1,
        tblAltSearchData.getColumnCount()-1);
        tblAltSearchData.setSelectionModel( perSelectionModel );
        tblAltSearchData.clearSelection();
        tblAltSearchData.setFont(CoeusFontFactory.getNormalFont());
        tblAltSearchData.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
      
        scrPnCorrespondent.setViewportView(tblAltSearchData);
        
        JLabel lblYearsSearched = new JLabel();
        Dimension labelDimension = new Dimension(150,20);
        lblYearsSearched.setMaximumSize(labelDimension);
        lblYearsSearched.setMinimumSize(labelDimension);
        lblYearsSearched.setPreferredSize(labelDimension);
        lblYearsSearched.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblYearsSearched.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblYearsSearched.setText("Years Searched: ");
        lblYearsSearched.setFont( CoeusFontFactory.getLabelFont() );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlAltSearchColumns.add(lblYearsSearched, gridBagConstraints);
        
        if( vecAltenativeData == null ){
            vecAltenativeData = new Vector();
            btnDelete.setEnabled( false );
            //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
            btnModify.setEnabled(false);
            btnView.setEnabled(false);
            //Addec for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
        }

        txtAltYearSearch.setName("txtSearchYear");
        txtAltYearSearch.setDocument( new LimitedPlainDocument( 2000 ) );                        
        txtAltYearSearch.setPreferredSize(new Dimension(275,600));  
        txtAltYearSearch.setWrapStyleWord(true);
        txtAltYearSearch.setLineWrap(true);
        txtAltYearSearch.setFont(CoeusFontFactory.getNormalFont());       
        txtAltYearSearch.setEditable(false);
        txtAltYearSearch.setEnabled(true);        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        
        scrPnAlternativeSearch= new javax.swing.JScrollPane();
        scrPnAlternativeSearch.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnAlternativeSearch.setPreferredSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setMinimumSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setMaximumSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setViewportView(txtAltYearSearch);
        pnlAltSearchColumns.add(scrPnAlternativeSearch, gridBagConstraints);
        
        JLabel lblKeywordsSearched = new JLabel();
        Dimension labelDimension1 = new Dimension(150,20);
        lblKeywordsSearched.setMaximumSize(labelDimension1);
        lblKeywordsSearched.setMinimumSize(labelDimension1);
        lblKeywordsSearched.setPreferredSize(labelDimension1);
        lblKeywordsSearched.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblKeywordsSearched.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKeywordsSearched.setText("Keywords Searched:");
        lblKeywordsSearched.setFont( CoeusFontFactory.getLabelFont() );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlAltSearchColumns.add(lblKeywordsSearched, gridBagConstraints);        
              
        txtAltKeySearch.setName("txtKeySearch");
        txtAltKeySearch.setDocument( new LimitedPlainDocument( 2000 ) );
        txtAltKeySearch.setWrapStyleWord(true);
        txtAltKeySearch.setFont(CoeusFontFactory.getNormalFont());
        txtAltKeySearch.setLineWrap(true);                        
        txtAltKeySearch.setPreferredSize(new Dimension(275,600));        
        txtAltKeySearch.setEditable(false);
        txtAltKeySearch.setEnabled(true);        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        
        scrPnAlternativeSearch= new javax.swing.JScrollPane();
        scrPnAlternativeSearch.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnAlternativeSearch.setPreferredSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setMinimumSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setMaximumSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setViewportView(txtAltKeySearch);
        
        pnlAltSearchColumns.add(scrPnAlternativeSearch, gridBagConstraints);
        
        JLabel lblComments = new JLabel();
        Dimension labelDimension2 = new Dimension(150,20);
        lblComments.setMaximumSize(labelDimension2);
        lblComments.setMinimumSize(labelDimension2);
        lblComments.setPreferredSize(labelDimension2);
        lblComments.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblComments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblComments.setText("Comments:");
        lblComments.setFont( CoeusFontFactory.getLabelFont() );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlAltSearchColumns.add(lblComments, gridBagConstraints);                
        
        txtAltComments.setName("txtArComments");
        txtAltComments.setDocument( new LimitedPlainDocument( 2000 ) );
        txtAltComments.setWrapStyleWord(true);
        txtAltComments.setFont(CoeusFontFactory.getNormalFont());
        txtAltComments.setLineWrap(true);          
        txtAltComments.setPreferredSize(new Dimension(275,600));         
        txtAltComments.setEditable(false);
        txtAltComments.setEnabled(true);       
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        
        scrPnAlternativeSearch= new javax.swing.JScrollPane();
        scrPnAlternativeSearch.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnAlternativeSearch.setPreferredSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setMinimumSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setMaximumSize(new java.awt.Dimension(325, 60));
        scrPnAlternativeSearch.setViewportView(txtAltComments);
        
        pnlAltSearchColumns.add(scrPnAlternativeSearch, gridBagConstraints);       
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(scrPnCorrespondent, gridBagConstraints);
        pnlAltSearchColumns.setMinimumSize(new java.awt.Dimension(500, 225)); 
        pnlAltSearchColumns.setPreferredSize(new java.awt.Dimension(500, 225));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy =5;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(pnlAltSearchColumns, gridBagConstraints);
        
        JPanel pnlButton = new JPanel();
        pnlButton.setLayout(new java.awt.GridBagLayout());
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnAdd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnDelete, gridBagConstraints); 
        
        //COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnModify, gridBagConstraints); 
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnView, gridBagConstraints); 
        //COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(pnlButton, gridBagConstraints);
        java.awt.Component[] components = {tblAltSearchData,btnAdd,btnDelete,btnModify,btnView,txtAltYearSearch,txtAltKeySearch,txtAltComments};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }



    /**
     * This method is used to set whether the data is to be saved or not.
     * @param save boolean true if any modifications have been done and are not
     * saved, else  false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }

    /**
     * This Method is used to perform Add/Delete Actions
     * @param btnActionEvent Action denote the ActionEvent for the buttons.
     */
    public void actionPerformed( ActionEvent btnActionEvent ){

        Object actionSource = btnActionEvent.getSource();      

        if( actionSource.equals( btnAdd ) ) {              
                   
            if(functionType != TypeConstants.DISPLAY_MODE){
                if(tblAltSearchData.isEditing()){
                    if(tblAltSearchData.getCellEditor() != null){
                        tblAltSearchData.getCellEditor().stopCellEditing();
                    }
                }
                //Modified for COEUSQA-2714 In the Alternative Search in IACUC-Start
                ProtocolAlternativeSearchBean newAltDBBean = new ProtocolAlternativeSearchBean();
                newAltDBBean.setProtocolNumber(protocolNumber);
                newAltDBBean.setSequenceNumber(sequenceNumber);
                newAltDBBean.setAcType(TypeConstants.INSERT_RECORD);
                newAltDBBean.setAlternativeSearchId(getMaxAltSearchId()+1);               
                int lastAddedRow = 0;
                vecAltBDSearchForDIsplay = new Vector();
                alternativeSearchDataBaseForm = new AlternativeSearchDataBaseForm(mdiForm,
                                    newAltDBBean,cvComboBoxBeanData,functionType, vecAltBDSearchForDIsplay);                                                     
                newAltDBBean = alternativeSearchDataBaseForm.getProtocolAltSearchBean();
                if(newAltDBBean.getYearsSearched() != null && newAltDBBean.getYearsSearched().trim().length()>0){
                vecAltenativeData.add(newAltDBBean);
                altSearchTableModel.setData(vecAltenativeData);
                lastAddedRow = tblAltSearchData.getRowCount()-1;
//                setSaveRequired(alternativeSearchDataBaseForm.saveRequired);               
                }                                
                tblAltSearchData.requestFocusInWindow();                                
                vecAltBDSearchForDIsplay = newAltDBBean.getChkDatabaseSearchedCode();
                StringBuilder dataBaseSearched = new StringBuilder();
                if(vecAltBDSearchForDIsplay != null && vecAltBDSearchForDIsplay.size()>0){
                    ProtocolAlterDatabaseSearchBean protocolAlterDatabaseSearchBean = new ProtocolAlterDatabaseSearchBean();
                    int index = 0;
                    for(Object obj:vecAltBDSearchForDIsplay){
                        
                        protocolAlterDatabaseSearchBean = (ProtocolAlterDatabaseSearchBean)obj;
                        if(cvComboBoxBeanData !=null && cvComboBoxBeanData.size()>0){
                            for(Object cvObj:cvComboBoxBeanData){
                                ComboBoxBean comboBoxBean = (ComboBoxBean)cvObj;
                                if(comboBoxBean.getCode().equals(""+protocolAlterDatabaseSearchBean.getDatabaseSearchedCode())){
                                    if(index !=0 && index < vecAltBDSearchForDIsplay.size()){
                                    dataBaseSearched.append(", ");
                                    dataBaseSearched.append(comboBoxBean.getDescription());                                    
                                    }else{
                                     dataBaseSearched.append(comboBoxBean.getDescription());   
                                    }
                                }
                            }
                        }
                        index++;                                               
                    }
                     txtAltYearSearch.setText(newAltDBBean.getYearsSearched());
                     txtAltYearSearch.setEditable(false);
                     txtAltKeySearch.setText(newAltDBBean.getKeyWordsSearched());
                     txtAltKeySearch.setEditable(false);
                     txtAltComments.setText(newAltDBBean.getComments());
                     txtAltComments.setEditable(false);
                     txtAltYearSearch.setEnabled( true );
                     txtAltKeySearch.setEnabled(true);
                     txtAltComments.setEnabled( true );
                }
                newAltDBBean.setDatabaseSeartched(dataBaseSearched.toString());
                altSearchTableModel.setValueAt(dataBaseSearched,lastAddedRow,2);
                tblAltSearchData.setRowSelectionInterval(lastAddedRow,lastAddedRow); 
               btnModify.setEnabled(true);
               btnView.setEnabled(true);
               btnDelete.setEnabled(true);               
               //Modified for COEUSQA-2714 In the Alternative Search in IACUC-End
            }
        }
         else if( actionSource.equals( btnDelete ) ) {      
            if(functionType != TypeConstants.DISPLAY_MODE){
                if(tblAltSearchData.isEditing()){
                    if(tblAltSearchData.getCellEditor() != null){
                        tblAltSearchData.getCellEditor().stopCellEditing();
                    }
                }
                int selectedRow = tblAltSearchData.getSelectedRow();
                if(selectedRow > -1){
                    int selectedOption = CoeusOptionPane.
                            showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            "altSearch_delete_confirmationCode.1001"),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    switch(selectedOption){
                        case CoeusOptionPane.SELECTION_YES:
                            
                            ProtocolAlternativeSearchBean alterBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(selectedRow);
                            if(!TypeConstants.INSERT_RECORD.equals(alterBean.getAcType())){
                                alterBean.setAcType(TypeConstants.DELETE_RECORD);
                                //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
                                Vector vecDeletedAltSeerchDB = alterBean.getChkDatabaseSearchedCode();
                                if(vecDeletedAltSeerchDB != null && vecDeletedAltSeerchDB.size()>0){
                                    ProtocolAlterDatabaseSearchBean databaseSearchBean = new ProtocolAlterDatabaseSearchBean();
                                    for(Object objAltDb:vecDeletedAltSeerchDB){                                        
                                        databaseSearchBean = (ProtocolAlterDatabaseSearchBean)objAltDb;
                                        databaseSearchBean.setAcType(TypeConstants.DELETE_RECORD);
                                    }
                                }
                                //Addec for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
                                deletedAlternativeSearch.add(alterBean);
                                setSaveRequired(true);
                            }
                          
                            vecAltenativeData.remove(selectedRow);
                            
                            altSearchTableModel.setData(vecAltenativeData);
                            if(selectedRow == 0 && tblAltSearchData.getRowCount() > 0){
                                    tblAltSearchData.setRowSelectionInterval(0,0);
                            }
                            if(selectedRow-1 > - 1){
                                tblAltSearchData.setRowSelectionInterval(selectedRow-1,selectedRow-1);
                            }                            
                               if(vecAltenativeData != null && vecAltenativeData.size() < 1){
                                btnDelete.setEnabled(false);
                                //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
                                btnModify.setEnabled(false);
                                btnView.setEnabled(false);
                                //Addec for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
                                txtAltYearSearch.setText("");
                                txtAltKeySearch.setText("");
                                txtAltComments.setText("");
                                
                                txtAltYearSearch.setEnabled(false);
                                txtAltKeySearch.setEnabled(false);
                                txtAltComments.setEnabled( false );         
                            }
//                            setSaveRequired(true);
                            break;
                        case CoeusOptionPane.SELECTION_NO:
                            break;
                        default:
                            break;
                    }
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("altSearch_select_exceptionCode.1000"));
                }
            }
        }  else if(actionSource.equals(btnModify)){
            if(functionType != TypeConstants.DISPLAY_MODE){ 
                int selectedRow = tblAltSearchData.getSelectedRow();
                ProtocolAlternativeSearchBean modifyAltSearchBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(selectedRow);                
                modifyAltSearchBean.setProtocolNumber(protocolNumber);
                //Modified for COEUSQA-2714 In the Alternative Search in IACUC-Start
                if((!TypeConstants.INSERT_RECORD.equals(modifyAltSearchBean.getAcType()))) {
                modifyAltSearchBean.setAcType(TypeConstants.UPDATE_RECORD);                 
                }
                vecAltBDSearchForDIsplay = new Vector();
                vecOldAltBDSearchForDIsplay = modifyAltSearchBean.getChkDatabaseOldSearchedCode();
                vecAltBDSearchForDIsplay = modifyAltSearchBean.getChkDatabaseSearchedCode();                 
                alternativeSearchDataBaseForm = new AlternativeSearchDataBaseForm(mdiForm,
                                    modifyAltSearchBean,cvComboBoxBeanData,functionType, vecAltBDSearchForDIsplay);                                                      
                modifyAltSearchBean = alternativeSearchDataBaseForm.getProtocolAltSearchBean();                                                  
                altSearchTableModel.setData(vecAltenativeData);                                                           
                vecAltBDSearchForDIsplay = modifyAltSearchBean.getChkDatabaseSearchedCode(); 
                                                                
                     txtAltYearSearch.setText(modifyAltSearchBean.getYearsSearched());
                     txtAltYearSearch.setEditable(false);
                     txtAltKeySearch.setText(modifyAltSearchBean.getKeyWordsSearched());
                     txtAltKeySearch.setEditable(false);
                     txtAltComments.setText(modifyAltSearchBean.getComments());
                     txtAltComments.setEditable(false);
                     txtAltYearSearch.setEnabled( true );
                     txtAltKeySearch.setEnabled(true);
                     txtAltComments.setEnabled( true );
                String dataBaseSearched = modifyAltSearchBean.getDatabaseSeartched();
                String searchdescription = dataBaseSearched.substring(0,dataBaseSearched.length()-2); 
                tblAltSearchData.requestFocusInWindow(); 
                tblAltSearchData.setRowSelectionInterval(selectedRow,selectedRow);
                modifyAltSearchBean.setDatabaseSeartched(searchdescription);
                altSearchTableModel.setValueAt(searchdescription,selectedRow,2);                 
                btnModify.setEnabled(true);
                btnView.setEnabled(true);
                btnDelete.setEnabled(true);
//                setSaveRequired(true);                
                //Modified for COEUSQA-2714 In the Alternative Search in IACUC-End
            }
        }else if(actionSource.equals(btnView)){                             
           viewProtocolAlternativeSearch();                                                                            
        }           
    }

    //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start             
    /*
     *This method is used to View the Protocol Alternative Search.
     *
     */
     private void viewProtocolAlternativeSearch() {
        selectedRow = tblAltSearchData.getSelectedRow();
        ProtocolAlternativeSearchBean viewAltSerachBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(selectedRow);
        vecAltBDSearchForDIsplay = viewAltSerachBean.getChkDatabaseSearchedCode();
        alternativeSearchDataBaseForm = new AlternativeSearchDataBaseForm(mdiForm,
                                    viewAltSerachBean,cvComboBoxBeanData,TypeConstants.DISPLAY_MODE, vecAltBDSearchForDIsplay);        
        viewAltSerachBean = alternativeSearchDataBaseForm.getProtocolAltSearchBean();
     }
    //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
     
    /*
     * Model class for the Alternative Search Table
     *
     */
    class AltSearchTableModel extends DefaultTableModel {
        private String colNames[] = {"","Search Date","Database Searched"};
        private Class colTypes[]  = {Object.class, Object.class, Object.class};
        
        //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
         public boolean isCellEditable(int row, int column) {
            boolean canEdit = false;             
            return canEdit;
        }
        //Addec for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
        
        public Object getValueAt(int row, int column) {
            if(vecAltenativeData != null & vecAltenativeData.size() > 0){
                ProtocolAlternativeSearchBean altSearchBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(row);
                switch(column) {
                    case ALTER_DB_COLUMN:
                        //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
//                        if(TypeConstants.DISPLAY_MODE != functionType){
//                            ComboBoxBean comboBoxBean = new ComboBoxBean("","");
//                            int dbsearchedCode = altSearchBean.getDatabaseSearchedCode();
//                            CoeusVector filteredVector = cvComboBoxBeanData.filter(new Equals("code", ""+dbsearchedCode));
//                            if(filteredVector!=null && filteredVector.size() > 0){
//                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
//                                
//                            }
//                            return comboBoxBean;
//                        }else{
                            return altSearchBean.getDatabaseSeartched();
//                        }
                          //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start  
                    case SEARCHDATE_COLUMN:
                        if(altSearchBean.getSearchDate() != null) {
                            try {
                                return dtUtils.formatDate(altSearchBean.getSearchDate().toString(),"dd-MMM-yyyy");
                            } 
                            catch(Exception ex) {
                                 return CoeusGuiConstants.EMPTY_STRING;
                            }
                        } else {
                            return CoeusGuiConstants.EMPTY_STRING;
                        }
                    default:
                        break;
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }

        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public void setData(Vector cvData) {
            dataVector = cvData;
            fireTableDataChanged();
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        public void setValueAt(Object value, int row, int column) {
//            if(TypeConstants.DISPLAY_MODE == functionType ||
//                    value == null ||
//                    vecAltenativeData == null ||
//                    (vecAltenativeData != null && vecAltenativeData.size() < 1)){
//                return;
//            }
            if(value == null || vecAltenativeData == null ||
                    (vecAltenativeData != null && vecAltenativeData.size() < 1)){
                return;
            }
            ProtocolAlternativeSearchBean altSearchBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(row);
            switch(column){
                case ALTER_DB_COLUMN :
                      String altDatabaseDesc = value.toString();
                      //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
//                    ComboBoxBean altSearchComboBoxBean = (ComboBoxBean)value;
//                    if(altSearchComboBoxBean != null && !"".equals(altSearchComboBoxBean.getCode())){
//                        altSearchBean.setDatabaseSearchedCode(Integer.parseInt(altSearchComboBoxBean.getCode()));
//                        altSearchBean.setDatabaseSeartched(altSearchComboBoxBean.getDescription());
//                    }
                      //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
                    break;
                case SEARCHDATE_COLUMN :
                    try {
                        String tempDate = dtUtils.restoreDate(value.toString(), "/-:,");
                         String convertedDate =dtUtils.formatDate(tempDate, "/-:," ,"dd-MMM-yyyy");
                         if(convertedDate !=null){
                         altSearchBean.setSearchDate(new java.sql.Date(dtFormat.parse(dtUtils.restoreDate(value.toString(), "/-:,")).getTime())); 
                         }                         
                    }catch(Exception ex) {
                        
                    }
                    break; 
                default:
                    break;

            }
        }
         
    }
 

    //supporting method to show the warning message
    private void showWarningMessage(){
        if( functionType != CoeusGuiConstants.DISPLAY_MODE ) {
            CoeusOptionPane.showWarningDialog(
            coeusMessageResources.parseMessageKey(
            "protoCorroFrm_exceptionCode.1053"));
        }
    }

     public void valueChanged( ListSelectionEvent listSelectionEvent ) {
        
        selectedRow = tblAltSearchData.getSelectedRow();
        String yearSearched = null;
        String keywordSearch = null;
        String comment = null;
        int rowCount =  tblAltSearchData.getRowCount();
        if( selectedRow >= 0  && selectedRow <= rowCount &&
        firstEntry && vecAltenativeData != null){
            ProtocolAlternativeSearchBean curBean = (ProtocolAlternativeSearchBean)
            vecAltenativeData.get( selectedRow ); 
            if( txtAltYearSearch.hasFocus() ){
                curBean = (ProtocolAlternativeSearchBean)
                vecAltenativeData.get(  lastSelectedRow );
                curBean.setYearsSearched( txtAltYearSearch.getText());
                curBean.setKeyWordsSearched( txtAltKeySearch.getText() );
                curBean.setComments( txtAltComments.getText() );
                return;
            } else if( txtAltKeySearch.hasFocus() ){
                curBean = (ProtocolAlternativeSearchBean)
                vecAltenativeData.get(  lastSelectedRow );
                curBean.setYearsSearched( txtAltYearSearch.getText());
                curBean.setKeyWordsSearched( txtAltKeySearch.getText() );
                curBean.setComments( txtAltComments.getText() );
                return;
            } 
            else if( txtAltComments.hasFocus() ){
                curBean = (ProtocolAlternativeSearchBean)
                vecAltenativeData.get(  lastSelectedRow );
                curBean.setYearsSearched( txtAltYearSearch.getText());
                curBean.setKeyWordsSearched( txtAltKeySearch.getText() );
                curBean.setComments( txtAltComments.getText() );
                return;
            }else{
                lastSelectedRow = selectedRow;
            }             
            if( curBean != null ){                
                yearSearched = curBean.getYearsSearched();
                keywordSearch = curBean.getKeyWordsSearched();
                comment = curBean.getComments();
                if( curBean.getAcType() == null ){                                        
                    if( functionType != CoeusGuiConstants.DISPLAY_MODE )                         
                        if( ( tblAltSearchData.getSelectedColumn() == 1 )){
//                            saveRequired = true;
                            isFormDataUpdated = true;
                        }
                }
                if( comment == null || comment.equalsIgnoreCase( "null" )){
                    comment = "";
                }
                if( yearSearched == null || yearSearched.equalsIgnoreCase( "null" )){
                    yearSearched = "";
                }
                if( keywordSearch == null || keywordSearch.equalsIgnoreCase( "null" )){
                    keywordSearch = "";
                }
                txtAltKeySearch.setText( keywordSearch );
                txtAltYearSearch.setText(yearSearched);
                txtAltComments.setText(comment);
                txtAltYearSearch.setCaretPosition(0);
                txtAltKeySearch.setCaretPosition(0);                
                txtAltComments.setCaretPosition(0);
            }
        }
        firstEntry = true ;
    }


//    //supporting method to construct Comob Box Entry of Type Field
//    private Vector getAltDBSearchComboValues() {
//
//        comboEntry =new ComboBoxBean("","");
//        Vector corType = new Vector();         
//        if( comboBoxBeanData == null ){
//            return corType;
//        }
//        corType.addElement(comboEntry);
//        for( int indx = 0; indx < comboBoxBeanData.size(); indx++ ){
//            comboEntry = ( ComboBoxBean ) comboBoxBeanData.get( indx );
//            corType.addElement( comboEntry.getDescription() );
//        }
//        return corType;
//    }


    //supporting method to construct Table data from db
//    private Vector getAlternativeSearchData(){
//
//        Vector tableData = new Vector();
//        ProtocolAlternativeSearchBean alterSearchBean = null;
//        Vector tableRowData;
//        if( vecAltenativeData != null ) {
//
//            for( int indx = 0; indx < vecAltenativeData.size(); indx++ ){
//                alterSearchBean = ( ProtocolAlternativeSearchBean ) vecAltenativeData.
//                get( indx );
//                tableRowData = new Vector();
//                tableRowData.addElement( "" );
//                //tableRowData.addElement( dtUtils.restoreDate(dtUtils.formatDate(alterSearchBean.getSearchDate().toString(),"dd-MMM-yyyy"),"/-:,"));
//                tableRowData.addElement(dtUtils.formatDate(alterSearchBean.getSearchDate().toString(),"dd-MMM-yyyy"));
//                tableRowData.addElement( alterSearchBean.getDatabaseSeartched() );
//                tableData.addElement( tableRowData );
//            }
//        }
//        
//        txtAltYearSearch.setEditable(false);
//        txtAltKeySearch.setEditable(false);
//        txtAltComments.setEditable(false);     
//        
//        btnAdd.setEnabled(true);
//        btnDelete.setEnabled(false);
//        return tableData;
//    }

    public Vector getFormData(){ 
         Vector alterDBSearch = new Vector();
         alterDBSearch.addAll(vecAltenativeData);            
         alterDBSearch.addAll(deletedAlternativeSearch);            
         return alterDBSearch;
     }

    /** This is method is raised/fired from FocusListener for Table/TextArea
     * component of this correspondance form when it gets the component
     * Focus.
     * @param focusEvent FocusEvent
     */
    public void focusGained( FocusEvent focusEvent ) {
        Object source = focusEvent.getSource();
        int selectedRow = tblAltSearchData.getSelectedRow();
        if( source.equals( txtAltYearSearch )){
            if( selectedRow == -1 && tblAltSearchData.getRowCount() > 0 ){
                showWarningMessage();
                tblAltSearchData.requestFocus();
            }
        }       
    }

    /**
     * This is method is raised/fired from FocusListener for Table/TextArea
     * component of this correspondance form when it losts the component
     * Focus.
     * @param focusEvent FocusEvent
     */
    public void focusLost( FocusEvent focusEvent )  {
        if ( !focusEvent.isTemporary()) {
            Object source = focusEvent.getSource();
            int selectedRow = lastSelectedRow;
            tblAltSearchData.getSelectedRow();            
            if( source.equals( txtAltYearSearch )||lastSelectedRow <= tblAltSearchData.getRowCount()  ){           
                ProtocolAlternativeSearchBean curBean =
                (ProtocolAlternativeSearchBean )
                vecAltenativeData.get(selectedRow); 
                curBean.setYearsSearched(txtAltYearSearch.getText());
                
        }
            if( source.equals( txtAltKeySearch )||lastSelectedRow <= tblAltSearchData.getRowCount()  ){           
                ProtocolAlternativeSearchBean curBean =
                (ProtocolAlternativeSearchBean )
                vecAltenativeData.get(selectedRow); 
                curBean.setKeyWordsSearched(txtAltKeySearch.getText());                
        }
            if( source.equals( txtAltComments )||lastSelectedRow <= tblAltSearchData.getRowCount()  ){                          
                ProtocolAlternativeSearchBean curBean =
                (ProtocolAlternativeSearchBean )
                vecAltenativeData.get(selectedRow); 
                curBean.setComments(txtAltComments.getText());
                
        }
    }
    }

    //Commented for COEUSQA-2714 In the Alternative Search in IACUC-Start
    public boolean validateData() throws Exception, CoeusUIException{
        boolean validate = true;
       // altSearchTableModel.setData(vecAltenativeData);
 
//         if(functionType == CoeusGuiConstants.DISPLAY_MODE || functionType == CoeusGuiConstants.AMEND_MODE){
//            return true;
//        }              
//        if(tblAltSearchData.isEditing()){
//            if(tblAltSearchData.getCellEditor() != null){
//                tblAltSearchData.getCellEditor().stopCellEditing();
//            }
//        }
//        boolean validate = true;
//        int rowCount = tblAltSearchData.getRowCount();
//        if(rowCount >= 0){
//            lastSelectedRow = tblAltSearchData.getSelectedRow();
//            for(int inInd=0; inInd < rowCount ;inInd++){
//                Object objSearchdate = (String)((DefaultTableModel) tblAltSearchData.getModel()).getValueAt(inInd,1);
//                if(objSearchdate == null || objSearchdate.toString().trim().length() <= 0 ) {
//                    validate = false;
//                    errorMessage(coeusMessageResources.parseMessageKey(SELECT_ALTSEARCH_DATE),inInd);
//                    break;
//                }
//                //Object objSearchtype = (String)((DefaultTableModel) tblAltSearchData.getModel()).getValueAt(inInd,2);
//                
//                ComboBoxBean combBeanSearchType = (ComboBoxBean)((DefaultTableModel) tblAltSearchData.getModel()).getValueAt(inInd,2);
//                 if(combBeanSearchType == null || combBeanSearchType.getCode() == null || combBeanSearchType.getCode().trim().length() == 0) {                     
//                        validate = false;
//                        errorMessage(coeusMessageResources.parseMessageKey(SELECT_ALTSEARCH_TYPE),inInd);
//                        break;                     
//                }
//                ProtocolAlternativeSearchBean curBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(inInd);
//                    String tempYearSearch=curBean.getYearsSearched();
//                    String tempKeyWord=curBean.getKeyWordsSearched();                    
//                    if((tempYearSearch == null) || (tempYearSearch.length()<=0))
//                    {
//                    validate = false;
//                    //Added to focus the field throwing error.
//                    setDefaultRowSelection(inInd) ;                  
//                    txtAltYearSearch.requestFocusInWindow();
//                    errorMessage(coeusMessageResources.parseMessageKey(SELECT_ALTSEARCH_YEAR));
//                     break;       
//                    }
//                 if((tempKeyWord == null) || (tempKeyWord.length()<=0))
//                    {
//                    validate = false;
//                    //Added to focus the field throwing error.
//                    setDefaultRowSelection(inInd) ;
//                    txtAltKeySearch.requestFocusInWindow();
//                    errorMessage(coeusMessageResources.parseMessageKey(SELECT_ALTSEARCH_KEYWORD));
//                     break;       
//                    }
//            }            
//        }
//        if(validate){
//            setAltSearchDBValue();
//        }
        return validate;
    }
    //Commented for COEUSQA-2714 In the Alternative Search in IACUC-End
    
    /* To Throw a coeusexception
     */
    private void errorMessage(String mesg) throws CoeusUIException{
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_ALTERNATIVE_SEARCH_EXCEP_TAB_INDEX);
        throw coeusUIException;
    }
    
    /* To Throw a coeusexception after selecting a table row
     */
    private void errorMessage(String mesg,int index) throws CoeusUIException{
        tblAltSearchData.setRowSelectionInterval(index,index);
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_ALTERNATIVE_SEARCH_EXCEP_TAB_INDEX);
        throw coeusUIException;
    }

    /** This method is used to set the functionType for the form.
     * @param functionType sets the functionType
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
        //formatFields();
    }



    /** throws the exception with the given message
     * @param mesg String represent the log message.
     * @throws Exception exception instance
     */
    public void log(String mesg) throws CoeusUIException {              
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(3);
        throw coeusUIException;      
    }


 

    //supporting method to select the default row when the application invoked
    private void setDefaultRowSelection( int rowIndex ){
        
        if( tblAltSearchData.getRowCount() > rowIndex){
            if(functionType != CoeusGuiConstants.DISPLAY_MODE) {
                btnDelete.setEnabled( true );
                //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
                btnModify.setEnabled(true);
                btnView.setEnabled(true);
                //Addec for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
                txtAltYearSearch.setEnabled( true );
                tblAltSearchData.setRowSelectionInterval( rowIndex, rowIndex );
//                saveRequired = false;
                txtAltYearSearch.setCaretPosition(0);
            }
            ProtocolAlternativeSearchBean curBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get( rowIndex );
            txtAltYearSearch.setText(curBean.getYearsSearched());
            txtAltKeySearch.setText(curBean.getKeyWordsSearched());
            txtAltComments.setText(curBean.getComments());
            lastSelectedRow = rowIndex;
        }
    }

    /**
     * This method is used to set this form data.
     * 
     * @param altSearchData collection of correspondent entry beans.
     */
    public void setFormData( Vector altSearchData ){   
        
        altSearchTableModel = new AltSearchTableModel();
        tblAltSearchData.setModel(altSearchTableModel);
        deletedAlternativeSearch = new Vector();   
              
        if ((altSearchData != null) && ((altSearchData.size())>0)){
            
            altSearchTableModel.setData(altSearchData);     
        }
//        if ((vecAltenativeData != null) && ((vecAltenativeData.size())>0)){
//            altSearchTableModel.setData(vecAltenativeData);
//        }
        int selectedRow=0;
        if(tblAltSearchData.getRowCount()>0)
        {
            selectedRow=tblAltSearchData.getSelectedRow();
        }
        vecAltenativeData = null;
        vecAltenativeData = altSearchData;
        if( vecAltenativeData == null ){
            vecAltenativeData = new Vector();
        }                
        if(tblAltSearchData.getRowCount()>0)
        {
          setDefaultRowSelection( 1 );
        }
        if(tblAltSearchData.getRowCount()>0 && tblAltSearchData.getRowCount() > selectedRow){
            if(selectedRow == -1){
                selectedRow = 0;
            }
            setDefaultRowSelection( selectedRow );
        }
        //Added for COEUSQA-3035 Indicator logic issue fixes-Start
        else{
            txtAltYearSearch.setText("");
            txtAltKeySearch.setText("");
            txtAltComments.setText("");
        }          
        //Added for COEUSQA-3035 Indicator logic issue fixes-end
    }
    
     public boolean isSaveRequired() {
         if(tblAltSearchData.isEditing() && tblAltSearchData.getCellEditor() != null){
                 tblAltSearchData.getCellEditor().stopCellEditing();
         }
         
         if(deletedAlternativeSearch != null && deletedAlternativeSearch.size()>0){
             return true;
         }
         
         if(vecAltenativeData == null || vecAltenativeData.isEmpty()){
             return false;
         }
         
         
         if(vecAltenativeData != null && !vecAltenativeData.isEmpty()){
             for(Object alterSearchBeanObj : vecAltenativeData){
                 ProtocolAlternativeSearchBean alterSearchDetails = (ProtocolAlternativeSearchBean)alterSearchBeanObj;
                 if(alterSearchDetails.getAcType() != null && !CoeusGuiConstants.EMPTY_STRING.equals(alterSearchDetails.getAcType())){
                     return true;
                 }
             }
         }
         if(functionType == TypeConstants.AMEND_MODE &&
                 vecAltenativeData != null && vecAltenativeData.size()>0){
          return true;   
         }
        return saveRequired;
    }
     
     //Added for Internal Issue Fix-1783:2260-Alt. Search-No msg when no mandatory details
     /* Method to check if the text fields are modified or not.
      * @param bean - the ProtocolAlternativeSearchBean to compare the text field data.
      * @return true if modified, false if not modified.
      */
     private boolean checkTextFieldsModified(ProtocolAlternativeSearchBean bean){
         boolean modified = false;
         String comments = txtAltComments.getText();
         String year = txtAltYearSearch.getText();
         String key = txtAltKeySearch.getText();
         if(functionType != TypeConstants.DISPLAY_MODE){
         /* The following length check is required because if the user modifies and save without
            tabbing out, the comments and year are mandatory and need to be validated. */
             if(comments.length() <=0 || !comments.equals(bean.getComments())){
                 modified = true;
             }else if(year.length() <=0 || !year.equals(bean.getYearsSearched())){
                 modified = true;
             }else if(!key.equals(bean.getKeyWordsSearched()) ){
                 modified = true;
             }
         }
         return modified;
     }

     public void setAltSearchTextValues() {
         
        ProtocolAlternativeSearchBean altSearchBean = null;
         if(vecAltenativeData!= null && vecAltenativeData.size()>0){
               altSearchBean = (ProtocolAlternativeSearchBean) vecAltenativeData.elementAt(tblAltSearchData.getSelectedRow());
               //Modified for Internal Issue Fix-1783:2260-Alt. Search-No msg when no mandatory details
                if(altSearchBean != null && checkTextFieldsModified(altSearchBean)){
                    altSearchBean.setComments(txtAltComments.getText());
                    altSearchBean.setYearsSearched(txtAltYearSearch.getText());
                    altSearchBean.setKeyWordsSearched(txtAltKeySearch.getText());
//                    setSaveRequired(true);
                }  
        } 
     }
     
     private void setAltSearchDBValue() throws NumberFormatException {
         ProtocolAlternativeSearchBean  curBean = null;
         for(int row=0;row<vecAltenativeData.size();row++) {
             curBean=(ProtocolAlternativeSearchBean)vecAltenativeData.get(row);
             if((curBean.getAcType()=="I")){
                 
                 if(vecAltenativeData!=null || vecAltenativeData.size()>0 ) {
                     if((tblAltSearchData.getValueAt(row,1) != null)){
                         try {
                             String oldDate=tblAltSearchData.getValueAt(row,1).toString();
                             
                             if(oldDate != null && oldDate.trim().length() > 0 ) {
                                 curBean.setSearchDate(new java.sql.Date(dtFormat.parse(
                                         dtUtils.restoreDate(oldDate,
                                         "/-:,")).getTime()));
                             }
                         } catch (ParseException ex) {
                             ex.printStackTrace();
                         }
                     }
                     ComboBoxBean comboBoxBean = (ComboBoxBean)tblAltSearchData.getValueAt(row,2);
                     curBean.setDatabaseSeartched(comboBoxBean.getDescription());
                     curBean.setDatabaseSearchedCode(Integer.parseInt(comboBoxBean.getCode()));
                     curBean.setComments(curBean.getComments());
                     curBean.setYearsSearched(curBean.getYearsSearched());
                     curBean.setKeyWordsSearched(curBean.getKeyWordsSearched());
                 }
             }
             
             if(curBean.getAcType()==null) {
                 curBean.setAcType("U");
                 if((tblAltSearchData.getValueAt(row,1) != null)){
                     try {
                         String oldDate=tblAltSearchData.getValueAt(row,1).toString();
                         curBean.setSearchDate(new java.sql.Date(dtFormat.parse(dtUtils.restoreDate(oldDate, "/-:,")).getTime()));
                     } catch (ParseException ex) {
                         ex.printStackTrace();
                     }
                     
                     ComboBoxBean comboBoxBean = (ComboBoxBean)tblAltSearchData.getValueAt(row,2);
                     curBean.setDatabaseSeartched(comboBoxBean.getDescription());
                     curBean.setDatabaseSearchedCode(Integer.parseInt(comboBoxBean.getCode()));
                     curBean.setComments(curBean.getComments());
                     curBean.setYearsSearched(curBean.getYearsSearched());
                     curBean.setKeyWordsSearched(curBean.getKeyWordsSearched());
                 }
             }
         }
     }

    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {

            if(tblAltSearchData .getRowCount() > 0 ) {
                tblAltSearchData.requestFocusInWindow();               
                int prevSelectedRow=tblAltSearchData.getSelectedRow();
                if(prevSelectedRow!=-1){
                    tblAltSearchData.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    tblAltSearchData.setRowSelectionInterval(0, 0);
                }

                tblAltSearchData.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }
        }
    }
   
    public void setParentSequenceId(int parentSequenceId) {
        this.parentSequenceId = parentSequenceId;
    }
    
    public JComponent showAlternativeSearchForm(CoeusAppletMDIForm
            mdiForm){
        this.mdiForm = mdiForm;
        initComponents();
        setListenersForAltSearchTable();
         if(cvComboBoxBeanData == null){
            cvComboBoxBeanData = new CoeusVector();
        }        
        setAlternativeSearch(this.vecAltenativeData);
        coeusMessageResources = CoeusMessageResources.getInstance();      
        return this;
       
    }
    
    public void setAlternativeSearch(Vector vecAltenativeData){
        saveRequired = false;
        if (vecAltenativeData == null) {
            vecAltenativeData = new Vector();
        }
        this.vecAltenativeData = vecAltenativeData;
        setFormData(vecAltenativeData);
        setTableColumnProperties();
        if(vecAltenativeData != null && vecAltenativeData.size() > 0){
            tblAltSearchData.setRowSelectionInterval(0, 0);
        }else{
            btnDelete.setEnabled(false);
            //Added for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - Start
            btnModify.setEnabled(false);
            btnView.setEnabled(false);
            //Addec for COEUSQA-2714 Alternative Search in IACUC, the user should enter search criteria once - End
        }
        setPropertyChangeSettings();
    }
     
    private Vector getAlternativeSearch(){
        RequesterBean request = new RequesterBean();
        Vector vecData = new Vector();
        Vector vecProtocolDetail = new Vector();
        vecProtocolDetail.add(0, protocolNumber);
        vecProtocolDetail.add(1, sequenceNumber);
        request.setFunctionType('0');
        request.setDataObjects(vecProtocolDetail);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                vecData = response.getDataObjects();
                if(functionType == TypeConstants.AMEND_MODE && 
                        vecData != null &&
                        vecData.size()>0){
                    for(int index=0;index<vecData.size();index++){
                        ProtocolAlternativeSearchBean altSearchBean =(ProtocolAlternativeSearchBean)vecData.get(index);
                        altSearchBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                    
                }
            }
        }
        
        return vecData;
    }

    private void fetchData() {
        vecAltenativeData = getAlternativeSearch();
        cvComboBoxBeanData = getAlterDBSearchCode();
    }
    
    //Added to get the IACUC procedures catgory -Start
   public CoeusVector getAlterDBSearchCode() {
        CoeusVector vecAltenativeDB=new CoeusVector();          
        RequesterBean request = new RequesterBean();
        Vector vecProtocolDetail = new Vector();
        vecProtocolDetail.add(0, protocolNumber);
        vecProtocolDetail.add(1, sequenceNumber);
        try{
            request.setFunctionType('%');
            request.setDataObjects(vecProtocolDetail);             
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,request);
            comm.send();
            ResponderBean responder = comm.getResponse();
            if(responder.hasResponse()){                
               vecAltenativeDB = (CoeusVector)responder.getDataObjects();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return vecAltenativeDB;
    }
    //Added to get the IACUC procedures catgory -End
   private int getMaxAltSearchId(){
        int maxAltSearchId = 0;
        ProtocolAlternativeSearchBean altDBBean= null;        
        if(vecAltenativeData != null && vecAltenativeData.size() > 0){
            for(int i=0;i<vecAltenativeData.size();i++){
                 altDBBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(i);
                if(altDBBean.getAlternativeSearchId()>maxAltSearchId){
                    maxAltSearchId = altDBBean.getAlternativeSearchId();
                }
            }
        }   
        if(deletedAlternativeSearch != null && deletedAlternativeSearch.size() > 0){
            for(int j=0;j<deletedAlternativeSearch.size();j++){
                 altDBBean = (ProtocolAlternativeSearchBean)deletedAlternativeSearch.get(j);
                if(altDBBean.getAlternativeSearchId()>maxAltSearchId){
                    maxAltSearchId = altDBBean.getAlternativeSearchId();
                }
            }
        }      
        return maxAltSearchId;
    }
    /*
     * Mehthod to set Species bean property
     */
    private void setPropertyChangeSettings(){
//        if(functionType != TypeConstants.AMEND_MODE && 
//                vecAltenativeData != null && vecAltenativeData.size() > 0){
//            for(int index=0;index<vecAltenativeData.size();index++){
//                oldSearchBeanData = (ProtocolAlternativeSearchBean)vecAltenativeData.get(index);
//                oldSearchBeanData.addPropertyChangeListener(new PropertyChangeListener() {
//                    public void propertyChange(PropertyChangeEvent pce) {
//                        //Modified If conditions to remove unnecessary save confirmations.
//                        if( !( pce.getOldValue() == null && (pce.getNewValue() == null || "".equals(pce.getNewValue().toString())))){
//                            ProtocolAlternativeSearchBean speciesBean = (ProtocolAlternativeSearchBean)vecAltenativeData.get(tblAltSearchData.getSelectedRow());
//                            speciesBean.setAcType(TypeConstants.UPDATE_RECORD);
//                            setSaveRequired(true);
//                        }
//                    }
//                });
//            }
//        }
    } 
   
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField=null;
        String strDate = "";
        String oldData = "" ;
        String focusDate = "";
        boolean temporary = false;        

        public void focusGained (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                     
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))) {
                    //System.out.println("in focus gained");        
//                    saveRequired = true;
                    oldData = dateField.getText();
                    focusDate = dtUtils.restoreDate(
                            oldData,"/-:,");
                    dateField.setText(focusDate);
//                    dateField.setText("");
                }
            }
        }

        public void focusLost (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();                
                temporary = fe.isTemporary();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))
                        && (!temporary) ) {
                    strDate = dateField.getText();
                    String convertedDate =
                            dtUtils.formatDate(strDate, "/-:," ,
                                    "dd-MMM-yyyy");                    
                    if (convertedDate==null){                         
//                        saveRequired = true;   
                        dateField.setText("");                                                   
                        CoeusOptionPane.showErrorDialog(
                               coeusMessageResources.parseMessageKey(
                                  "memMntFrm_exceptionCode.1048"));
                        temporary = true;
                        selectedRow = tblAltSearchData.getSelectedRow();
                        tblAltSearchData.editCellAt(selectedRow,1);
                        dateField.requestFocus();
                    }else {
                          
                        dateField.setText(convertedDate);
                        tblAltSearchData.setValueAt(convertedDate,selectedRow,1);
                        temporary = false;
                        if(!oldData.equals(convertedDate)){
                            tblAltSearchData.editCellAt(selectedRow,2);                            
//                            saveRequired = true;
                        }else{      
                            tblAltSearchData.editCellAt(selectedRow,1);
//                            saveRequired = false;
                        }
                    }
                }
            }
        }
        
    }
    
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
    /*
    *This method is used to set the listeners to the components.
    *
    */
    private void setListenersForAltSearchTable(){
        coeusMessageResources = CoeusMessageResources.getInstance();        
        tblAltSearchData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                         viewProtocolAlternativeSearch();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-End
}

