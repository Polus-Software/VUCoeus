/*
 * @(#)ReviewerSelectionForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
 * Created on November 26, 2002, 2:45 PM
 * @version 1.0 
 */

/* PMD check performed, and commented unused imports and variables on 25-AUGUST-2010
 * by Satheesh Kumar K N
 */

package edu.mit.coeus.irb.gui;

//import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
//import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;


/** This class is used to select Reviewers for reviewing a given Protocol. Only
 * those persons who are active members of the Committee, to which this particular
 * Protocol is submitted can become a reviewer. User can select any of the
 * available reviewer types for each reviewer.
 *
 * @author ravikanth
 */
public class ReviewerSelectionForm extends javax.swing.JComponent {
    
    /** collection which stores all the available reviewerTypes */
    private ArrayList reviewerTypes;
    
    /** collection which stores all the  selected reviewers */
    private ArrayList reviewersList;
    
    /** collection which stores all active reviewers */
    private ArrayList availableReviewersList;
    
    /** boolean value which specifies any changes have been made */
    private boolean saveRequired;
    
    /** holds the  committee id of the reviewers */
    private String committeeId;
    
    // 3282: Reviewer view of protocols - Start
    // Commented to disable the Review Complete and Recommended ACtions Column - Start
//    private Vector recommendedActionTypes;
//    private JCheckBox chkReviewComplete, chkTmpReviewComplete;
    // Commented to disable the Review Complete and Recommended ACtions Column - End
    // 3282: Reviewer view of protocols -End
    // Added to disable the Review Complete and Recommended ACtions Column - Start
    private static final int REVIEWER_ID_COLUMN = 0;
    private static final int REVIEWER_NAME_COLUMN = 1;
    private static final int REVIEWER_REVIEW_TYPE_COLUMN = 2;
    private static final int REVIEWER_ASSIGNED_DATE_COLUMN = 3;
    private static final int REVIEWER_DUE_DATE_COLUMN = 4;
    private static final int REVIEWER_EMP_COLUMN = 5;
    // Added to disable the Review Complete and Recommended ACtions Column - End
    
    /**
     * Creates new form <CODE>ReviewerSelectionForm</CODE> and initializes all the
     * components.
     *
     * @deprecated instead use the constructor
     * ReviewerSelectionForm(ArrayList reviewersList)
     */
    public ReviewerSelectionForm() {
        initComponents();
        setTableModel();
        setTableFormat();
        tblAvailableReviewers.getTableHeader().setFont(
            CoeusFontFactory.getLabelFont());

        tblSelectedReviewers.getTableHeader().setFont(
            CoeusFontFactory.getLabelFont());       
    }

    /** Constructor used to create and initialize the components in
     * <CODE>ReviewerSelectionForm</CODE>. And also used to populate the reviewers list
     * which has been sent as a parameter.
     *
     * @param reviewersList ArrayList of beans which will be used to display
     * the list of selected reviewers in a table.
     */    
    
    public ReviewerSelectionForm(ArrayList reviewersList) {
        this.reviewersList = reviewersList;
        
        initComponents();
        setTableModel();
        setFormData();
        setTableFormat();
        tblAvailableReviewers.getTableHeader().setFont(
            CoeusFontFactory.getLabelFont());

        tblSelectedReviewers.getTableHeader().setFont(
            CoeusFontFactory.getLabelFont());
    }
    
    /**
     * This method is used to set the model to the schedule table with 
     * default values and column names.
     */
    
    private void setTableModel(){
    
        // 3282: Reviewer View of Protocol materials - Start
//        tblSelectedReviewers.setModel(new javax.swing.table.DefaultTableModel(
//        new Object [][] {},
//        new String [] {
//            "Id","Name", "Reviewer Type","Emp"
//        }
//        ) {
//            boolean[] canEdit = new boolean [] {false, false, true,false};
//            
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                return canEdit [columnIndex];
//            }
//        });
        // Modified to disable the Review Complete and Recommended ACtions Column - Start
//        tblSelectedReviewers.setModel(new javax.swing.table.DefaultTableModel(
//                new Object [][] {},
//                new String [] {
//            "Id","Review Complete","Name", "Reviewer Type","Assigned Date","Due Date","Recommend Action","Emp"
//        }
        tblSelectedReviewers.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {
            "Id","Name", "Reviewer Type","Assigned Date","Due Date","Emp"
        }
        ) {
//            boolean[] canEdit = new boolean [] {false, true, false, true, true, true, true, false};
            boolean[] canEdit = new boolean [] {false, false, true, true, true, false};
        // Modified to disable the Review Complete and Recommended ACtions Column - end

            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
             public void setValueAt(Object value, int row, int col) {
                           
                 DateUtils dateUtils = new DateUtils();
//                 Date date       = null;
                 String strDate  = null;
//                 SimpleDateFormat simpleDateFormat;
                 DefaultTableModel tableModel = (DefaultTableModel)tblSelectedReviewers.getModel();
                 Vector vecData =(Vector) tableModel.getDataVector().elementAt(row);
                 switch(col) {
                     
                     
                     case REVIEWER_ASSIGNED_DATE_COLUMN :
                     case REVIEWER_DUE_DATE_COLUMN :
                         if (value.toString().trim().length() > 0) {
                             strDate = dateUtils.formatDate(value.toString().trim(), CoeusGuiConstants.DATE_SEPARATORS, CoeusGuiConstants.UI_DATE_FORMAT);
                             strDate = dateUtils.restoreDate(strDate, CoeusGuiConstants.DATE_SEPARATORS);
                             if(strDate==null) {
                                 CoeusOptionPane.showErrorDialog("Please enter a valid Date");
                             }else{
                                 vecData.setElementAt(strDate, col);
                                 updateRowInTable(tblSelectedReviewers,vecData);
                             }
                         } else{
                             vecData.setElementAt("", col);
                             updateRowInTable(tblSelectedReviewers,vecData);
                         }
                         break;
                     default:                
                         vecData.setElementAt(value, col);
                         updateRowInTable(tblSelectedReviewers,vecData);
                 }
             }
             
        });
        // 3282: Reviewer View of Protocol materials - End
        
        tblAvailableReviewers.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] {
            "Id", "Name","Emp"
        }
        ) {
            boolean[] canEdit = new boolean [] {false, false, false};
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
    
    }

    /**
     * This method is used to set the form data as well as the lookup data by 
     * extracting details from ProtocolReviewerInfoBean.
     */
    
    private void setFormData(){
        if(availableReviewersList != null){
            ((DefaultTableModel)tblAvailableReviewers.getModel()).setDataVector(
                constructAvailRevTableData(),getAvailRevColumnNames());

            ((DefaultTableModel)
                tblAvailableReviewers.getModel()).fireTableDataChanged();
        }
            
        if( reviewersList != null ) {
            ((DefaultTableModel)tblSelectedReviewers.getModel()).setDataVector(
                constructSelRevTableData(),getSelRevColumnNames());

            ((DefaultTableModel)
                tblSelectedReviewers.getModel()).fireTableDataChanged();
        }
        int availRevCount = tblAvailableReviewers.getRowCount();
        int selRevCount = tblSelectedReviewers.getRowCount();
        if( availRevCount > 0 && selRevCount > 0 ){
            
            for(int selRevIndex = 0; selRevIndex < selRevCount; selRevIndex++){
                String selPersonId = (String)
                    tblSelectedReviewers.getValueAt(selRevIndex,0);
                availRevCount = tblAvailableReviewers.getRowCount();
                for(int availRevIndex = 0; availRevIndex < availRevCount ;
                     availRevIndex++){
                    String availPersonId = (String)
                        tblAvailableReviewers.getValueAt(availRevIndex,0);
                        
                    if(selPersonId.equals(availPersonId)){
                        ((DefaultTableModel)
                            tblAvailableReviewers.getModel()).removeRow(
                                availRevIndex);
                        availRevCount--;
                    }
                }
            }
                        
        }
    
    }
    
    /**
     * Supporting method used to get the column names of available reveviewers 
     * table 
     * @return  colNames Vector which consists of column header names.
     */    
    
    private Vector getAvailRevColumnNames(){
        
        Enumeration enumColumns 
                = tblAvailableReviewers.getColumnModel().getColumns();
        Vector colNames = new Vector();
        while( enumColumns.hasMoreElements()){
            colNames.addElement(((TableColumn)
                enumColumns.nextElement()).getHeaderValue().toString());
        }
        return colNames;
    }
    
    /**
     * Supporting method which constructs vector of vectors from the 
     * collection of CommitteeMembershipDetailsBean.
     * @return  availRevTableData vector which will be used in displaying 
     * available reviewers table data
     */    
    
    private Vector constructAvailRevTableData() {
        /** holds the values to be displayed in available reveiwers table */
        Vector availRevTableData;

        availRevTableData = new Vector();
        CommitteeMembershipDetailsBean memberBean;
        Vector tableRowData;
        int availRevSize = availableReviewersList.size();
        for( int rowIndex = 0 ; rowIndex < availRevSize ; rowIndex++ ) {
                    
            /* extract person name from bean and construct
             * vector of vectors.
             */
            memberBean = ( CommitteeMembershipDetailsBean )
                    availableReviewersList.get( rowIndex );
            tableRowData = new Vector();
            tableRowData.addElement(memberBean.getPersonId());
            tableRowData.addElement(memberBean.getPersonName());
            tableRowData.addElement(new Boolean(
                (memberBean.getNonEmployeeFlag() == 'y' || 
                memberBean.getNonEmployeeFlag() == 'Y') ? true : false) );
            
            availRevTableData.addElement(tableRowData);
        }
        return availRevTableData;
    }
    
    

    /**
     * Supporting method used to get the column names of selected reveviewers 
     * table 
     * @return  colNames vector which consists of column header names.
     */    
    
    private Vector getSelRevColumnNames(){
        
        Enumeration enumColumns 
            = tblSelectedReviewers.getColumnModel().getColumns();
        Vector colNames = new Vector();
        while( enumColumns.hasMoreElements()){
            colNames.addElement(((TableColumn)
                enumColumns.nextElement()).getHeaderValue().toString());
        }
        return colNames;
    }
    
    /**
     * Supporting method which constructs vector of vectors from
     * the collection of ProtocolReviewerInfoBean.
     * @return  selRevTableData vector which will be used in displaying 
     * selected reviewers table data
     */    
    
    private Vector constructSelRevTableData() {
        /** holds the values to be displayed in selected reveiwers table */
        Vector selRevTableData;
    
        selRevTableData = new Vector();
        DateUtils dateUtils = new DateUtils();
        ProtocolReviewerInfoBean reviewerBean;
        Vector tableRowData;
        int revSize = reviewersList.size();
        for( int rowIndex = 0 ; rowIndex < revSize; rowIndex++ ) {
            /* extract person name and reviewer type from bean and construct
             * double dimensional object array.
             */
            reviewerBean = ( ProtocolReviewerInfoBean )
                    reviewersList.get( rowIndex );
            tableRowData = new Vector();
            tableRowData.addElement(reviewerBean.getPersonId());
            tableRowData.addElement(new Boolean(reviewerBean.isReviewComplete()));
            tableRowData.addElement(reviewerBean.getPersonName());
            tableRowData.addElement(reviewerBean.getReviewerTypeDesc());
            // 3282: Reviewer View of Protocol materials - Start
            if(reviewerBean.getAssignedDate() != null){
                String strAssignedDate = reviewerBean.getAssignedDate().toString();
                tableRowData.addElement(dateUtils.formatDate(strAssignedDate,CoeusGuiConstants.DEFAULT_DATE_FORMAT));
            }else{
                tableRowData.addElement("");
            }
            if(reviewerBean.getDueDate() != null){
                String strDueDate = reviewerBean.getDueDate().toString();
                tableRowData.addElement(dateUtils.formatDate(strDueDate,CoeusGuiConstants.DEFAULT_DATE_FORMAT));
            }else{
                tableRowData.addElement("");
            }
            tableRowData.addElement(reviewerBean.getRecommendedActionCode());
            // 3282: Reviewer View of Protocol materials - End
            tableRowData.addElement(new Boolean(reviewerBean.isNonEmployee()) );
            selRevTableData.addElement(tableRowData);
        }
        return selRevTableData;
    }
    
    
    /** This method is used to set the available Committee Members who are
     * eligible to become Reviewers.
     *
     * @param availableReviewers ArrayList which consits of collection of
     * <CODE>ProtocolReviewerInfoBean</CODE>.
     */
    public void setAvailableReviewers(ArrayList availableReviewers){
        this.availableReviewersList = availableReviewers;
        setFormData();
        setTableFormat();
    }
    
    /** This method is used to set the available reviewer types for selected
     * reviewers.
     *
     * @param revTypes ArrayList which consists of collection of
     * <CODE>ComboBoxBean</CODE> with <CODE>reviewerTypeCode</CODE> and <CODE>reviewerTypeDescription</CODE> as values.
     */
    public void setReviewerTypes(ArrayList revTypes){
        this.reviewerTypes = revTypes;
        setTableFormat();
    }
    
    /** This method is used to set the committee id which specifies the Committee
     * to which all the Reviewers belongs to.
     *
     * @param commId String which represents committee Id.
     */
    public void setCommitteeId(String commId){
        this.committeeId = commId;
    }

    /**
     * This method is used to set the sizes of the tables depending on the size
     * of the parent panel.
     */
    public void setComponentSizes(){
        // 3282: Reviewer View of Protocol materials - Start
//        int width = (int)(getPreferredSize().getWidth()-50)/2;
//        int height = (int)getPreferredSize().getHeight()-20;
//        scrPnAvailableReviewers.setPreferredSize(
//            new java.awt.Dimension(width-30,height));
//        scrPnSelectedReviewers.setPreferredSize(
//            new java.awt.Dimension(width+30,height));
        int width = (int)(getPreferredSize().getWidth()-50)/2;
        int height = (int)getPreferredSize().getHeight()+ 20;
        scrPnAvailableReviewers.setPreferredSize(
                new java.awt.Dimension(width-110,height));
        scrPnSelectedReviewers.setPreferredSize(
                new java.awt.Dimension(width+110,height));
        // 3282: Reviewer View of Protocol materials - End
    }
    
    /**
     * Supporting method used to set the display formats of the table like
     * column lengths etc.
     */
    
    private void setTableFormat(){
        tblAvailableReviewers.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        tblAvailableReviewers.getTableHeader().setReorderingAllowed(false);
        tblAvailableReviewers.getTableHeader().setResizingAllowed(false);
        tblAvailableReviewers.setFont(CoeusFontFactory.getNormalFont());
        
        tblSelectedReviewers.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        tblSelectedReviewers.getTableHeader().setReorderingAllowed(false);
        tblSelectedReviewers.getTableHeader().setResizingAllowed(false);
        tblSelectedReviewers.setFont(CoeusFontFactory.getNormalFont());

        // 3282: Reviewer View of Protocol materials - Start
//        tblSelectedReviewers.getTableHeader().setPreferredSize(new Dimension(0, 30));
        SelectedReviewersTableHeaderRenderer selectedReviewersTableHeaderRenderer = new SelectedReviewersTableHeaderRenderer();
        
        // Modified to disable the Review Complete and Recommended ACtions Column - Start
//        TableColumn column = tblSelectedReviewers.getColumnModel().getColumn(1);       
//        column.setMaxWidth(65);
//        column.setMinWidth(65);
//        column.setPreferredWidth(65);
//        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
//        column.setHeaderValue("<html>Review<br>Complete</html>");
//        chkReviewComplete = new  JCheckBox();
//        column.setCellEditor(new DefaultCellEditor( chkReviewComplete){
//            
//            public Component getTableCellEditorComponent(JTable table, Object value,
//                    boolean isSelected,
//                    int row, int column) {
//                
//                chkReviewComplete.setSelected(((Boolean)(value)).booleanValue());
//                chkReviewComplete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//                return chkReviewComplete;
//                
//            }
//            
//        }
//        );
//        chkTmpReviewComplete = new JCheckBox();
//        column.setCellRenderer(new DefaultTableCellRenderer(){
//            public Component getTableCellRendererComponent(JTable table,Object value,
//                    boolean isSelected,boolean hasFocus, int row, int col) {
//                
//                chkTmpReviewComplete.setSelected(((Boolean)(value)).booleanValue());
//                chkTmpReviewComplete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//                return chkTmpReviewComplete;
//            }
//        }
//        );
//        column = tblSelectedReviewers.getColumnModel().getColumn(2);       
        TableColumn column = tblSelectedReviewers.getColumnModel().getColumn(REVIEWER_NAME_COLUMN);       
        // Modified to disable the Review Complete and Recommended ACtions Column - End
        column.setMaxWidth(120);
        column.setMinWidth(120);
        column.setPreferredWidth(120);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);       
        
//        TableColumn column = tblSelectedReviewers.getColumn("Reviewer Type");
        column = tblSelectedReviewers.getColumnModel().getColumn(REVIEWER_REVIEW_TYPE_COLUMN);
        column.setMaxWidth(80);
        column.setMinWidth(80);
        column.setPreferredWidth(80);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        column.setHeaderValue("<html>Reviewer<br> Type</html>");
        
        /* setting CoeusComboBox as editor to Reviewer Type column */
        if(reviewerTypes != null && reviewerTypes.size()>0){
            JComboBox  coeusCombo = new JComboBox(getReviewerTypes());
            coeusCombo.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    saveRequired = true;
                }
            });
            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
            column.setCellEditor(new DefaultCellEditor(coeusCombo ));
        }

        
        column = tblAvailableReviewers.getColumn("Id");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
//        column = tblSelectedReviewers.getColumn("Id");
//        column.setMaxWidth(0);
//        column.setMinWidth(0);
//        column.setPreferredWidth(0);
        
        column = tblAvailableReviewers.getColumn("Emp");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
//        column = tblSelectedReviewers.getColumn("Emp");
//        column.setMaxWidth(0);
//        column.setMinWidth(0);
//        column.setPreferredWidth(0);
        
        column = tblSelectedReviewers.getColumnModel().getColumn(REVIEWER_ID_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);

        column = tblSelectedReviewers.getColumnModel().getColumn(REVIEWER_ASSIGNED_DATE_COLUMN);
        column.setMaxWidth(70);
        column.setMinWidth(70);
        column.setPreferredWidth(70);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        column.setHeaderValue("<html>Assigned<br> Date</html>");
        
        column = tblSelectedReviewers.getColumnModel().getColumn(REVIEWER_DUE_DATE_COLUMN);
        column.setMaxWidth(70);
        column.setMinWidth(70);
        column.setPreferredWidth(70);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
         // Commented to disable the Review Complete and Recommended ACtions Column - Start
//        column = tblSelectedReviewers.getColumnModel().getColumn(6);
//        column.setMaxWidth(150);
//        column.setMinWidth(150);
//        column.setPreferredWidth(150);
//        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
//        
//        if(recommendedActionTypes != null && recommendedActionTypes.size()>0){
//            JComboBox  coeusCombo = new JComboBox(recommendedActionTypes);
//            coeusCombo.addItemListener(new ItemListener(){
//                public void itemStateChanged(ItemEvent ie){
//                    saveRequired = true;
//                }
//            });
//            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
//            column.setCellEditor(new DefaultCellEditor(coeusCombo ));
//        }
         // Commented to disable the Review Complete and Recommended ACtions Column - End
        
        column = tblSelectedReviewers.getColumnModel().getColumn(REVIEWER_EMP_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        
         // 3282: Reviewer View of Protocol materials - End
        
    }
    
    /**
     * This method returns the reviwer types
     * @return revTypes collection of ComboBoxBeans which consists of 
     * reviewer type descriptions
     */
    
    private Vector getReviewerTypes(){
    
        Vector revTypes = new Vector();
        int revSize = reviewerTypes.size();
        if(reviewerTypes != null){
            for(int index = 0; index < revSize; index++ ){
                revTypes.addElement(((ComboBoxBean)
                    reviewerTypes.get(index)).toString());
            }
        }

        return revTypes;
    
    }
    
    /**
     * This method is used to add a specified row in the specified table
     * in sorted order
     *
     * @param table JTable to which the specified row to be added
     * @param vtr Vector which consists of data that has to be added as a row in 
     * a table
     */
    private void insertRowInTable(JTable table, Vector vtr){
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = dVector.size();
        int rowIndex;
        Vector vecElement;
        
        // find the position for inserting by looping through the list
        for(rowIndex =0;rowIndex<rowCount;rowIndex++){
            vecElement = (Vector)dVector.elementAt(rowIndex);
            if( ( ((String)vecElement.elementAt(0)).compareTo(
                    (String)vtr.elementAt(0))) < 0){
                continue;
            }
            else {
                break;
            }
            
        }
        saveRequired=true;
        tableModel.insertRow(rowIndex,vtr);
        tableModel.fireTableDataChanged();
    }
    
    // 3282: Reviewer View of Protocol materials - Start
    /** 
     * Methos used to Update the data in the Data Vector od Table Model
     * @param JTable table
     * @param Vector vector
     * @return void
     */
    
    private void updateRowInTable(JTable table, Vector vector){
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = dVector.size();
        int rowIndex;
        Vector vecElement;
        
        for(rowIndex =0;rowIndex<rowCount;rowIndex++){
            vecElement = (Vector)dVector.elementAt(rowIndex);
            if( ( ((String)vecElement.elementAt(0)).compareTo(
                    (String)vector.elementAt(0))) == 0){
                tableModel.removeRow(rowIndex);
                tableModel.insertRow(rowIndex,vector);
            }
        }
        tableModel.fireTableDataChanged();
    }
    // 3282: Reviewer View of Protocol materials -End
    
    /** This method is used to get the Reviewers selected by the user
     *
     * @return Collection of vectors which is given by the table model.
     */
    public Vector getSelectedReviewers(){
        int selRowCount = tblSelectedReviewers.getRowCount();
        if(selRowCount > 0){
            return ((DefaultTableModel)
                tblSelectedReviewers.getModel()).getDataVector();
        }else{
            return null;
        }
    }
    
    // 3282: Reviewer view of protocols - Start
    private class SelectedReviewersTableHeaderRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setFont(table.getTableHeader().getFont());
            setText((value == null) ? "" : value.toString());
            setPreferredSize(new Dimension(50,35));
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
        
    }
    // 3282: Reviewer view of protocols - End
    
    /** This method is used to check whether user has modified any existing
     * details.
     *
     * @return true if user modified any details, else false.
     */
    
    public boolean isSaveRequired(){
        
        return saveRequired;
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnSelectedReviewers = new javax.swing.JScrollPane();
        tblSelectedReviewers = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnAvailableReviewers = new javax.swing.JScrollPane();
        tblAvailableReviewers = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(780, 400));
        setPreferredSize(new java.awt.Dimension(780, 400));
        scrPnSelectedReviewers.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Selected Reviewers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnSelectedReviewers.setMinimumSize(new java.awt.Dimension(370, 190));
        scrPnSelectedReviewers.setPreferredSize(new java.awt.Dimension(370, 190));
        tblSelectedReviewers.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSelectedReviewers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSelectedReviewersMouseClicked(evt);
            }
        });

        scrPnSelectedReviewers.setViewportView(tblSelectedReviewers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(scrPnSelectedReviewers, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("<<");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(50, 10, 0, 10);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText(">>");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 50, 5);
        add(btnDelete, gridBagConstraints);

        scrPnAvailableReviewers.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Available Reviewers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnAvailableReviewers.setMinimumSize(new java.awt.Dimension(150, 190));
        scrPnAvailableReviewers.setPreferredSize(new java.awt.Dimension(150, 190));
        tblAvailableReviewers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAvailableReviewersMouseClicked(evt);
            }
        });

        scrPnAvailableReviewers.setViewportView(tblAvailableReviewers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 10);
        add(scrPnAvailableReviewers, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void tblSelectedReviewersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelectedReviewersMouseClicked
        // Add your handling code here:
        if(evt.getClickCount() == 2){
            showMemberDetails(tblSelectedReviewers);
        }
    }//GEN-LAST:event_tblSelectedReviewersMouseClicked

    private void tblAvailableReviewersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAvailableReviewersMouseClicked
        // Add your handling code here:
        if(evt.getClickCount() == 2){
            showMemberDetails(tblAvailableReviewers);
        }
        
    }//GEN-LAST:event_tblAvailableReviewersMouseClicked
    /**
     * This method is used to show the membership details for the selected 
     * reviewer from the given table.
     * 
     */
    private void showMemberDetails(JTable table){
        int selRow = table.getSelectedRow();
        if(selRow != -1){
            String memberId = (String)table.getValueAt(selRow,REVIEWER_NAME_COLUMN);
            new MemberDetailsForm(committeeId,memberId,'D');
        }
    }
    // 3282: Reviewer view of protocols - Start
    // Commented to disable the Review Complete and Recommended ACtions Column - Start
//    public Vector getRecommendedActionTypes() {
//        return recommendedActionTypes;
//    }
//
//    public void setRecommendedActionTypes(Vector recommendedActionTypes) {
//        this.recommendedActionTypes = recommendedActionTypes;
//    }
    // Commented to disable the Review Complete and Recommended ACtions Column - End
    // 3282: Reviewer view of protocols - End
    /**
     * This method is used to remove the selected reviewer
     */
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        Vector vecRow = new Vector();
        int rowNum = tblSelectedReviewers.getSelectedRow();
        DefaultTableModel model 
                = (DefaultTableModel)tblSelectedReviewers.getModel();
        if ( (rowNum != -1) && (rowNum <model.getRowCount()) ){
            
            vecRow.add(model.getValueAt(rowNum,0));
            // Modified for COEUSQA-2961 : IRB protocol submission screen: unable to move selected reviewer back to available reviewers column - Start
            // Column 7 is not exist
//            vecRow.addElement(model.getValueAt(rowNum,2));            
//            vecRow.add(model.getValueAt(rowNum, 7));
            vecRow.addElement(model.getValueAt(rowNum,REVIEWER_NAME_COLUMN));
            // Modified for COEUSQA-2961 : IRB protocol submission screen: unable to move selected reviewer back to available reviewers column - Start
            
            // insert the deleted reviewer into available reviewers list.
            insertRowInTable(tblAvailableReviewers,vecRow);

            model.removeRow(rowNum);
            model.fireTableDataChanged();

            int newRowCount = model.getRowCount();
            // select the next row if exists
            if (newRowCount > rowNum) {
                (tblSelectedReviewers.getSelectionModel())
                    .setSelectionInterval(rowNum, 
                                                rowNum);
            } else {
                (tblSelectedReviewers.getSelectionModel())
                    .setSelectionInterval(newRowCount - 1, 
                        newRowCount - 1);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed


    /**
     * This method is used to add the selected available reviewer to  
     * selected reveiwers table 
     */
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
        
        Vector vecRow = new Vector();
        int rowNum = tblAvailableReviewers.getSelectedRow();
        DefaultTableModel model 
               = (DefaultTableModel)tblAvailableReviewers.getModel();
        if ( (rowNum != -1) && (rowNum <model.getRowCount()) ){
            vecRow.add(model.getValueAt(rowNum,0));
//            vecRow.add(new Boolean(false));
            vecRow.add(model.getValueAt(rowNum,1));
            String revType = ((ComboBoxBean)reviewerTypes.get(0)).toString();
            vecRow.add(revType);
            // 3282: Reviewer View of Protocol materials - Start
            java.sql.Date  currentDate = new java.sql.Date(new java.util.Date().getTime());
            vecRow.add(new DateUtils().formatDate(currentDate.toString(),CoeusGuiConstants.DEFAULT_DATE_FORMAT));
            vecRow.add("");
//            vecRow.add(new ComboBoxBean ("",""));
            // 3282: Reviewer View of Protocol materials - End
            vecRow.add(model.getValueAt(rowNum, 2));
            insertRowInTable(tblSelectedReviewers,vecRow);
            model.removeRow(rowNum);
        }
        
    }//GEN-LAST:event_btnAddActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JScrollPane scrPnAvailableReviewers;
    private javax.swing.JScrollPane scrPnSelectedReviewers;
    private javax.swing.JTable tblAvailableReviewers;
    private javax.swing.JTable tblSelectedReviewers;
    // End of variables declaration//GEN-END:variables
    
}
