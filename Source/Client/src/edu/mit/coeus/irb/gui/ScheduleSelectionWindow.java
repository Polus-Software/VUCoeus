/*
 * ScheduleSelectionWindow.java
 *
 * Created on July 22, 2003, 4:09 PM
 */

package edu.mit.coeus.irb.gui;


import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.text.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import java.awt.Frame;


public class ScheduleSelectionWindow extends CoeusDlgWindow
{
    
       /** collection which stores all the  schedules whose status is SCHEDULED */
    private ArrayList scheduleList;

    /** reference object of DateUtils which will be used in date conversions */
    private DateUtils dtUtils = new DateUtils();

    
   private ScheduleDetailsBean scheduleDetailsBean ;
    
    /** Creates new form <CODE>ScheduleSelectionForm</CODE> and initializes the components.
     *
     * @deprecated instead use the constructor
     * ScheduleSelectionForm( ArrayList scheduleList )
     */
    public ScheduleSelectionWindow() {
        initComponents();
    }

    /** Constructor used to create and initialize the components in
     * <CODE>ScheduleSelectionForm</CODE>. And also used to populate the schedule list
     * which has been sent as a parameter.
     *
     * @param scheduleList ArrayList of <CODE>ScheduleDetailsFormBean</CODE> which will
     * be used to display the list of schedules in a table.
     */

    public ScheduleSelectionWindow(Frame parent, String title, boolean modal, ArrayList scheduleList ){
        super(parent, title, modal);
        this.scheduleList = scheduleList;
        initComponents();
        setSize(550,400) ; 
         
        setTableModel();
        ((DefaultTableModel)tblSchedules.getModel()).setDataVector(
            constructTableData(),getColumnNames());

        ((DefaultTableModel)tblSchedules.getModel()).fireTableDataChanged();

        setTableFormat();
        tblSchedules.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                scheduleDetailsBean = null;
                tblSchedules.clearSelection();
                dispose();
            }
        });
        addWindowListener( new java.awt.event.WindowAdapter(){
            public void windowActivated(java.awt.event.WindowEvent we){
                if( tblSchedules.getRowCount() > 0 ) {
                    tblSchedules.requestFocusInWindow();
                }else{
                    btnCancel.requestFocusInWindow();
                }
            
            }
            public void windowClosing(java.awt.event.WindowEvent we){
                scheduleDetailsBean = null;
                tblSchedules.clearSelection();
                dispose();
            }
        });
        
        //Added by Vyjayanthi 21/12/2003 - Start
        java.awt.Component[] components = {tblSchedules, btnOk, btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        if( tblSchedules.getRowCount() > 0){
            tblSchedules.setRowSelectionInterval(0,0);
        }
        btnOk.setMnemonic('O');
        btnCancel.setMnemonic('C');
        //End
    }

    public ScheduleDetailsBean getScheduleDetailsBean()
    {
      return  this.scheduleDetailsBean ;
    }
    
    
    /**
     * This method is used to set the model to the schedule table with
     * default values and column names.
     */

    private void setTableModel(){
        tblSchedules.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Schedule Id", "Scheduled Date", "Submission Deadline", "Place",
            "Time", "Max Protocols"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

    }

    /**
     * Supporting method used to get the column names to be used in the table
     * @return  colNames String[] which consists of column header names.
     */

    private String[] getColumnNames(){

        Enumeration enumColumns = tblSchedules.getColumnModel().getColumns();
        String[] colNames = new String[ tblSchedules.getColumnCount() ];
        int colIndex = 0;
        while( enumColumns.hasMoreElements()){
            colNames[ colIndex++ ] = ((TableColumn)
                    enumColumns.nextElement()).getHeaderValue().toString();
        }
        return colNames;
    }

    /**
     * Supporting method which constructs double dimensional object array from
     * the collection of ScheduleDetailsFormBean.
     * @return  tableData Object[][] which will be used in displaying table data
     */

    private Object[][] constructTableData() {
        /** holds the values to be displayed in table */
        Object[][] tableData;


        int rows=0;
        tableData = new Object[rows][tblSchedules.getColumnCount()];
        if( scheduleList != null ){
            rows = scheduleList.size();
            tableData = new Object[rows][tblSchedules.getColumnCount()];
            ScheduleDetailsBean scheduleBean;
            Object[] tableRowData;

            for( int rowIndex = 0 ; rowIndex < rows ; rowIndex++ ) {
                /* extract details from bean and construct double dimensional
                   object array.
                 */
                scheduleBean = ( ScheduleDetailsBean )
                        scheduleList.get( rowIndex );
                tableRowData = new Object[tblSchedules.getColumnCount()];
                tableRowData[0] = scheduleBean.getScheduleId();
                String schDate = dtUtils.formatDate(
                    scheduleBean.getScheduleDate().toString(),"dd-MMM-yyyy");
                tableRowData[1] = schDate == null ? "" : schDate;

                schDate = dtUtils.formatDate(
                    scheduleBean.getProtocolSubDeadLine().toString(),
                    "dd-MMM-yyyy");
                tableRowData[2] = schDate == null ? "" : schDate;

                String place = scheduleBean.getScheduledPlace();
                tableRowData[3] =  place == null ? "" : place;
                if(scheduleBean.getScheduledTime() != null){
                    /* convert the Time object to HH:mm format */
                    GregorianCalendar gCal = new GregorianCalendar();
                    gCal.setTime(scheduleBean.getScheduledTime());
                    String min = "";
                    if(gCal.get(Calendar.MINUTE)<=9){
                        min = "0"+gCal.get(Calendar.MINUTE);
                    }else{
                        min = ""+gCal.get(Calendar.MINUTE);
                    }
                    tableRowData[4] = gCal.get(Calendar.HOUR_OF_DAY)+":"+min;
                }else{
                    tableRowData[4] = "";
                }

                tableRowData[5] = ""+scheduleBean.getMaxProtocols();

                tableData [ rowIndex ] = tableRowData;
            }

        }


        return tableData;
    }

    /** This method is used to check whether user has selected any schedule or
     * not.
     * @return true if any schedule row has been selected, else false.
     */

    public boolean isScheduleSelected(){
        return ( tblSchedules.getSelectedRow() == -1 ) ? false : true ;
    }


    /** This method is used to get the selected Schedule from the list of
     * schedules displayed.
     * @return <CODE>ScheduleDetailsBean</CODE> which consists of
     * schedule id, scheduled date and maximum number of protocols reviewed in
     * that Schedule.
     */

    public ScheduleDetailsBean getSelectedSchedule(){
        ScheduleDetailsBean scheduleDetailsBean = null;
        int selRow = tblSchedules.getSelectedRow();
        SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
        if( selRow != -1 ) {
            scheduleDetailsBean = new ScheduleDetailsBean();
            scheduleDetailsBean.setScheduleId(( String )
                tblSchedules.getValueAt(selRow,0));
            String stDate = dtUtils.restoreDate(
                tblSchedules.getValueAt(selRow,1).toString(),"/:-,");
            scheduleDetailsBean.setScheduleDate(new java.sql.Date(
                       dtFormat.parse(stDate,new ParsePosition(0)).getTime()));
            // added by manoj to display place in submission details window. 16/09/2003
            scheduleDetailsBean.setScheduledPlace(tblSchedules.getValueAt(selRow,3).toString());
            int maxProtocols = 0;
            try{
                maxProtocols = Integer.parseInt(
                        (String)tblSchedules.getValueAt(selRow,5));

            } catch ( NumberFormatException nfe ) {
                maxProtocols = 0;
            }
            scheduleDetailsBean.setMaxProtocols(maxProtocols);
        }
        return scheduleDetailsBean;
    }

    /** This method is used to set the selected Schedule and select that Schedule
     * in the schedules table.
     *
     * @param scheduleId String representing schedule id.
     */

    public void setSelectedSchedule(String scheduleId){
        for(int count = 0 ; count < tblSchedules.getRowCount(); count++) {
            if(scheduleId.equals((String)tblSchedules.getValueAt(count,0))){
                tblSchedules.setRowSelectionInterval(count,count);
                tblSchedules.scrollRectToVisible(
                    tblSchedules.getCellRect(count ,0, true));
                break;
            }
        }
    }

    /**
     * Supporting method used to set the display formats of the table like
     * column lengths etc.
     */

    private void setTableFormat() {
        tblSchedules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSchedules.getTableHeader().setReorderingAllowed(false);
        tblSchedules.getTableHeader().setResizingAllowed(false);

        TableColumn column = tblSchedules.getColumn("Schedule Id");
        column.setMaxWidth(90);
        column.setMinWidth(90);
        column.setPreferredWidth(90);

        column = tblSchedules.getColumn("Scheduled Date");
        column.setMaxWidth(110);
        column.setMinWidth(110);
        column.setPreferredWidth(110);

        column = tblSchedules.getColumn("Submission Deadline");
        column.setMaxWidth(130);
        column.setMinWidth(130);
        column.setPreferredWidth(130);

        column = tblSchedules.getColumn("Time");
        column.setMaxWidth(50);
        column.setMinWidth(50);
        column.setPreferredWidth(50);

        column = tblSchedules.getColumn("Max Protocols");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents()//GEN-BEGIN:initComponents
    {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnSchedules = new javax.swing.JScrollPane();
        tblSchedules = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        tblSchedules.setFont(CoeusFontFactory.getNormalFont());
        scrPnSchedules.setViewportView(tblSchedules);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.ipadx = 420;
        gridBagConstraints.ipady = 300;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(scrPnSchedules, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 2);
        getContentPane().add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOkActionPerformed
    {//GEN-HEADEREND:event_btnOkActionPerformed
        // Add your handling code here:
        if (tblSchedules.getSelectedRowCount()>0)
        {
            this.scheduleDetailsBean = getSelectedSchedule() ;
            this.dispose() ;
        }    
        else
        {
            CoeusOptionPane.showErrorDialog("Schedule not selected") ;
        }    
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        // Add your handling code here:
        this.scheduleDetailsBean = null ;
        tblSchedules.clearSelection();
        this.dispose() ;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JTable tblSchedules;
    private javax.swing.JScrollPane scrPnSchedules;
    private javax.swing.JButton btnOk;
    // End of variables declaration//GEN-END:variables
    
}
