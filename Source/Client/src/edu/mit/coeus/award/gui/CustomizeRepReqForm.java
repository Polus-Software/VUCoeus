/*
 * CustomizeRepReqForm.java
 *
 * Created on July 19, 2004, 10:26 AM
 */
/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * AwardAmountTree.java
 *
 */
package edu.mit.coeus.award.gui;

/**
 *
 * @author  sharathk
 */

import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JTable;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.table.*;
import edu.mit.coeus.utils.EmptyHeaderRenderer;

public class CustomizeRepReqForm extends javax.swing.JPanel implements ActionListener{
    
    private Class[] colTypes = {String.class};
    private String[] colNames = {""};
    
    public static final String REPORT_TYPE = "Report Type";
    public static final String FREQUENCY = "Frequency";
    public static final String FREQUENCY_BASE = "Frequency Base";
    public static final String BASE_DATE = "Base Date";
    public static final String STATUS = "Status";
    public static final String DISTRIBUTION = "Distribution";
    public static final String CONTACT = "Contact";
    public static final String ADDRESS = "Address";
    public static final String DUE_DATE = "Due Date";
    public static final String COPIES = "Copies";
    public static final String OVERDUE_COUNTER = "Overdue Counter";
    public static final String ACTIVITY_DATE = "Activity Date";
    public static final String COMMENTS = "Comments";
    public static final String PERSON = "Person";
    public static final String LAST_UPDATE = "Last Update";
    
    public static final int OK_CLICKED = 0;
    public static final int CANCEL_CLICKED = 1;
    public static final int DEFAULT_CLICKED = 2;
    
    private int clicked = CANCEL_CLICKED;
    
    protected String[] strAll;
    
    protected String[] strGroups;
    
    protected String[] strDetail;
    
    private String title = "Customize Reporting Requirements View";
    
    private int[] groupColumns;
    
    private int[] detailColumns;
    
    private CoeusDlgWindow dlgCustomize;
    
    /** Creates new form CustomizeRepReqForm */
    public CustomizeRepReqForm() {
        initComponents();
        postInitComponents();
        registerComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnAllColumns = new javax.swing.JScrollPane();
        tblAllColumns = new edu.mit.coeus.utils.table.CoeusDnDTable();
        scrPnGroupColumns = new javax.swing.JScrollPane();
        tblGroupColumns = new edu.mit.coeus.utils.table.CoeusDnDTable();
        scrPnDetailColumns = new javax.swing.JScrollPane();
        tblDetailColumns = new edu.mit.coeus.utils.table.CoeusDnDTable();
        lblAllColumns = new javax.swing.JLabel();
        lblGroupColumns = new javax.swing.JLabel();
        lblDetailColumns = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnDefault = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(500, 300));
        scrPnAllColumns.setMinimumSize(new java.awt.Dimension(175, 100));
        scrPnAllColumns.setPreferredSize(new java.awt.Dimension(175, 275));
        tblAllColumns.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnAllColumns.setViewportView(tblAllColumns);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(scrPnAllColumns, gridBagConstraints);

        scrPnGroupColumns.setMinimumSize(new java.awt.Dimension(170, 100));
        scrPnGroupColumns.setPreferredSize(new java.awt.Dimension(175, 150));
        tblGroupColumns.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnGroupColumns.setViewportView(tblGroupColumns);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(scrPnGroupColumns, gridBagConstraints);

        scrPnDetailColumns.setMinimumSize(new java.awt.Dimension(170, 100));
        scrPnDetailColumns.setPreferredSize(new java.awt.Dimension(175, 150));
        tblDetailColumns.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnDetailColumns.setViewportView(tblDetailColumns);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(scrPnDetailColumns, gridBagConstraints);

        lblAllColumns.setFont(CoeusFontFactory.getLabelFont());
        lblAllColumns.setText("All Columns");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblAllColumns, gridBagConstraints);

        lblGroupColumns.setFont(CoeusFontFactory.getLabelFont());
        lblGroupColumns.setText("Columns in Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblGroupColumns, gridBagConstraints);

        lblDetailColumns.setFont(CoeusFontFactory.getLabelFont());
        lblDetailColumns.setText("Columns in Detail");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(lblDetailColumns, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(btnCancel, gridBagConstraints);

        btnDefault.setFont(CoeusFontFactory.getLabelFont());
        btnDefault.setMnemonic('D');
        btnDefault.setText("Default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(btnDefault, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDefault;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblAllColumns;
    public javax.swing.JLabel lblDetailColumns;
    public javax.swing.JLabel lblGroupColumns;
    public javax.swing.JScrollPane scrPnAllColumns;
    public javax.swing.JScrollPane scrPnDetailColumns;
    public javax.swing.JScrollPane scrPnGroupColumns;
    public edu.mit.coeus.utils.table.CoeusDnDTable tblAllColumns;
    public edu.mit.coeus.utils.table.CoeusDnDTable tblDetailColumns;
    public edu.mit.coeus.utils.table.CoeusDnDTable tblGroupColumns;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String s[]) {
        CustomizeRepReqForm cutomizeRepReq = new CustomizeRepReqForm();
        int sel = cutomizeRepReq.display();
        if(sel == OK_CLICKED) {
            //System.out.println("OK");
        }else if(sel == CANCEL_CLICKED){
            //System.out.println("Cancel");
        }
        
    }
    
    protected void initData() {
        //Commented for case#2268 - Report Tracking Functionality - start
        strAll = new String[]{REPORT_TYPE, FREQUENCY, FREQUENCY_BASE, BASE_DATE, STATUS,
        DISTRIBUTION, /*CONTACT, ADDRESS,*/ DUE_DATE, /*COPIES,*/ OVERDUE_COUNTER, ACTIVITY_DATE,
        COMMENTS, PERSON, LAST_UPDATE};
        
        strGroups = new String[]{REPORT_TYPE, FREQUENCY, FREQUENCY_BASE, BASE_DATE, DISTRIBUTION};
        
        strDetail = new String[]{STATUS, /*CONTACT, ADDRESS,*/ DUE_DATE, /*COPIES,*/ LAST_UPDATE, PERSON, COMMENTS, ACTIVITY_DATE, OVERDUE_COUNTER};
        //Commented for case#2268 - Report Tracking Functionality - end
    }
    
    protected void postInitComponents() {
        initData();
        
        setUpTable(tblAllColumns, strAll);
        tblAllColumns.setDeleteDataOnDrop(false);
        tblAllColumns.setAddDataOnDrop(false);
        
        setUpTable(tblGroupColumns, strGroups);
        
        setUpTable(tblDetailColumns, strDetail);
        
    }
    
    private void registerComponents() {
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnDefault.addActionListener(this);
    }
    
    private void setUpTable(JTable jTable, String data[]) {
        Vector vecData = prepareDataVector(data);
        
        CustomizeRepReqTableModel ctmTableModel = new CustomizeRepReqTableModel(vecData, colNames, colTypes);
        jTable.setModel(ctmTableModel);
        
        jTable.setShowHorizontalLines(false);
        jTable.setShowVerticalLines(false);
        
        jTable.getTableHeader().setDefaultRenderer(new EmptyHeaderRenderer());
        
        jTable.setForeground(Color.black);
        jTable.setBackground(Color.white);
        
    }
    
    /**
     *prepares Vector of Vectors from the String array to be displayed in the table
     */
    private Vector prepareDataVector(String data[]) {
        Vector vecTemp;
        Vector vecData = new Vector();
        
        for(int index = 0; index < data.length; index++) {
            vecTemp = new Vector();
            vecTemp.add(data[index]);
            vecData.add(vecTemp);
        }
        
        return vecData;
    }
    
    /**
     * Displays the dialog
     */
    public int display() {
        clicked = CANCEL_CLICKED;
        if(dlgCustomize == null){
            dlgCustomize = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
            dlgCustomize.setTitle(title);
            dlgCustomize.getContentPane().add(this);
            dlgCustomize.setSize(410, 325);
            dlgCustomize.setResizable(false);
        }
        dlgCustomize.setLocation(CoeusDlgWindow.CENTER);
        dlgCustomize.setVisible(true);
        
        return clicked;
    }
    
    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnOk)) {
            //Prepare int array which the caller will use.
            groupColumns = prepareIndices(tblGroupColumns);
            detailColumns = prepareIndices(tblDetailColumns);
            
            clicked = OK_CLICKED;
            
            dlgCustomize.dispose();
            
        }else if(source.equals(btnCancel)) {
            clicked = CANCEL_CLICKED;
            dlgCustomize.dispose();
        }else if(source.equals(btnDefault)){
            Vector vecDetail = prepareDataVector(strDetail);
            Vector vecGroup = prepareDataVector(strGroups);
            
            CustomizeRepReqTableModel tableModel;
            tableModel = (CustomizeRepReqTableModel)tblDetailColumns.getModel();
            tableModel.setData(vecDetail);
            tableModel.fireTableDataChanged();
            
            tableModel = (CustomizeRepReqTableModel)tblGroupColumns.getModel();
            tableModel.setData(vecGroup);
            tableModel.fireTableDataChanged();
            
            clicked = DEFAULT_CLICKED;
        }
    }//End Action Performed
    
    private int[] prepareIndices(JTable jTable) {
        int[] retVal = new int[jTable.getRowCount()];
        String value;
        for(int tblIndex = 0; tblIndex < retVal.length; tblIndex++) {
            value = jTable.getValueAt(tblIndex, 0).toString();
            for(int index = 0; index < strAll.length; index++) {
                if(value.equals(strAll[index])) {
                    retVal[tblIndex] = index;
                    break;
                }
            }//End for All
        }//End for group
        return retVal;
    }
    
    public int[] getGroupColumns() {
        return groupColumns;
    }
    
    public int[] getDetailColumns() {
        return detailColumns;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Setter for property strAll.
     * @param strAll New value of property strAll.
     */
    public void setColumns(String[] strAll) {
        this.strAll = strAll;
    }
    
    /**
     * Setter for property strGroups.
     * @param strGroups New value of property strGroups.
     */
    public void setGroups(String[] strGroups) {
        this.strGroups = strGroups;
    }
    
    /**
     * Setter for property strDetail.
     * @param strDetail New value of property strDetail.
     */
    public void setDetail(String[] strDetail) {
        this.strDetail = strDetail;
    }
    
}

class CustomizeRepReqTableModel extends CoeusTableModel {
    CustomizeRepReqTableModel(Vector data, String colNames[], Class colTypes[]){
        super(data, colNames, colTypes);
    }
    
    public String getColumnName(int column) {
        return null;
    }
    
    public void addRow(Vector rowData) {
        Object dataToAdd = rowData.get(0);
        int size = dataVector.size();
        Vector data;
        for(int index = 0; index < size; index++){
            data = (Vector)dataVector.get(index);
            if(data.contains(dataToAdd)) {
                return ;
            }
        }
        
        super.addRow(rowData);
    }
    
    public void setData(Vector data) {
        dataVector = data;
    }
    
}
