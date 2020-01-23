/*
 * @(#)MemberStatusForm.java    
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.iacuc.bean.CommitteeMemberStatusChangeBean;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.awt.Font;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.DateUtils;

/**
 * <code> MemberStatusForm </code> is a class for displaying the status change
 * history for a member. All the changes made to the status will be recorded 
 * with new sequence number and stored in the database. User can see the all the
 * status history details in this tab page. This tab page is for display purpose only.
 *
 * @author Lenin Fernandes
 * @version: 1.0 September 19, 2002 
 *
 * @version: 1.1 October 19, 2002
 * @author ravikanth
 */
public class MemberStatusForm extends javax.swing.JComponent {
    
    // Variables declaration - do not modify
    private javax.swing.JScrollPane scrPnHistory;
    private javax.swing.JTable tblHistory;
    /* holds the status information */
    private CommitteeMemberStatusChangeBean memberStatus;
    JPanel pnlMain;
    /* holds the entire history of status changes made in the form of 
       CommitteeMemberStatusChangeBeans */
    private Vector availableStatusHistory;
    /* holds the column names used to display the status history table */
    private Vector columnNames;
    /* used to format and restore date values */
    private DateUtils dtUtils = new DateUtils();
    private JDialog parent;

    // End of variables declaration
    
    /**
     * Default constructor which creates new <CODE>MemberStatusForm</CODE> and initializes
     * the components. We can use this constructor if no history exists for a
     * member status.
     */
    public MemberStatusForm() {
        initComponents();
    }
    
    /** Constructor which is used to create <CODE>MemberStatusForm</CODE> with specified
     * status history details. The data in the history parameter is in the form
     * of <CODE>MemberStatusBean</CODE>s. This will be converted into vector of objects and
     * displayed in a table.
     * @param history Vector which consists of memeber status history details.
     */
    public MemberStatusForm(Vector history) {
        availableStatusHistory = history;
    }

    /** This method is used to initialize <CODE>MemberStatusForm</CODE> with the specified
     * member status history.
     *
     * @param parent reference to parent <CODE>JDialog</CODE>
     * @return JComponent reference of <CODE>MemberStatusForm</CODE> after initialization.
     */
    public JComponent showMemberStatusForm(JDialog parent){
        
        this.parent = parent;
        initComponents();
        setFormData();
        
        //Added by Amit 11/18/2003        
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
        tblHistory.setBackground(bgListColor);                    
        //End Amit 
        
        return pnlMain;        
    }
    
    /** This method is used to set the column names used for history display
     * table.
     * @param colNames Vector consists of column names to be used for history display
     * table.
     */
    public void setColumnNames(Vector colNames){
        
        this.columnNames = colNames;
        
    }
    
    /** This method is used to get the column names used by history display
     * table.
     * @return Vector consists of column names to be used by history display
     * table.
     */
    public Vector getColumnNames(){
        
        return columnNames;
        
    }
    
    /** This method is used to set the data to the status history details table.
     */
    public void setFormData(){
        
        if(availableStatusHistory!=null && availableStatusHistory.size()>0){
            Vector tableData = new Vector();
            Vector tableRowData = null;
            String fromDate = null;
            String endDate = null;
            for(int row=0;row<availableStatusHistory.size();row++){
                tableRowData = new Vector();
                memberStatus = (CommitteeMemberStatusChangeBean)
                        availableStatusHistory.elementAt(row);
                tableRowData.addElement(memberStatus.getStatusDescription());
                fromDate = dtUtils.formatDate(
                        memberStatus.getStartDate().toString(),"dd-MMM-yyyy");
                endDate = dtUtils.formatDate(
                        memberStatus.getEndDate().toString(),"dd-MMM-yyyy");
                tableRowData.addElement(fromDate==null?"":fromDate);
                tableRowData.addElement(endDate==null?"":endDate);
                tableData.addElement(tableRowData);
            }
            ((DefaultTableModel)tblHistory.getModel()).setDataVector(tableData,
                    getColumnNames());
            ((DefaultTableModel)tblHistory.getModel()).fireTableDataChanged();
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        
        Vector colNames = new Vector();
        colNames.addElement("Status");
        colNames.addElement("From Date");
        colNames.addElement("To Date");
        setColumnNames(colNames);
        pnlMain = new JPanel();
        java.awt.GridBagConstraints gridBagConstraints;
        
        scrPnHistory = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        pnlMain.setLayout(new java.awt.GridBagLayout());
        scrPnHistory.setPreferredSize(new java.awt.Dimension(300, 200));
        tblHistory.setModel(new javax.swing.table.DefaultTableModel(new Vector(),
        getColumnNames()) {
            public boolean isCellEditable(int row, int col){
                return false;
            }
        });
        tblHistory.setFont(CoeusFontFactory.getNormalFont());
        tblHistory.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblHistory.setEnabled(false);
        tblHistory.getTableHeader().setResizingAllowed(false);
        scrPnHistory.setViewportView(tblHistory);
        scrPnHistory.setBorder(
                new TitledBorder(new EtchedBorder(), "Status History",
                TitledBorder.LEFT, TitledBorder.TOP, 
                CoeusFontFactory.getLabelFont()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 0);
        pnlMain.add(scrPnHistory, gridBagConstraints);
        setLayout(new BorderLayout());
        this.add(pnlMain);
        
    }
    
}
