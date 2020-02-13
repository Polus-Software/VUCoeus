/*
 * AbstractReportViewForm.java
 *
 * Created on July 22, 2003, 4:02 PM
 */

package edu.mit.coeus.irb.gui;

import java.util.Vector;
import java.util.Enumeration;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.net.URL;

import java.applet.AppletContext;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListSelectionModel;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

/**
 * This abstract class is used to show all the versions ofreports available for a 
 * give schedule for Agenda/Minutes. And also displays the pdf report for the selected 
 * version of agenda/minute number. Two abstract methods getAllReportDetails() and
 * getSpecificReportContextName() should be implemented by the classes which extends
 * this class for getting all the versions of the reports for a module for a given
 * schedule id and to get the context name of the pdf report file corresponding to
 * the selected version.
 * @author  ravikanth
 */
public abstract class AbstractReportViewForm extends edu.mit.coeus.gui.CoeusDlgWindow 
    implements ActionListener {
    
    private CoeusMessageResources messageResource;    
    private String scheduleID,committeeID,committeeName;
    private Vector receivedParams;
    private Vector dbData,columnNames;
    private Vector dataObject;
    private Vector recipientData;
    private URL reportUrl;
    /**
     * Constructs the AbstractReportViewForm with the given parameters
     * @param params Vector consisting of ScheduleId, CommitteeId and Committee Name.
     * @param modal boolean which specifies whether the dialog is modal or not.
     */
    public AbstractReportViewForm( Vector params, boolean modal ) {
        super(CoeusGuiConstants.getMDIForm(), modal );
        this.receivedParams = params;
    }
    /**
     * Constructs the AbstractReportViewForm with the given parameters
     * @param params Vector consisting of ScheduleId, CommitteeId and Committee Name.
     */
    
    public AbstractReportViewForm( Vector params ) {
        super( CoeusGuiConstants.getMDIForm(), true );
        this.receivedParams = params;
    }
    /**
     * This method is used to initialize all the components and show the form
     * with the details.
     * @throws Exception if fetching of the details fails or there are no report
     * details available.
     */
    public void showForm() throws Exception {
        
        if( receivedParams != null && receivedParams.size() >= 3 ) {
            scheduleID = (String) receivedParams.elementAt(0);
            committeeID = (String) receivedParams.elementAt(1);
            committeeName = (String) receivedParams.elementAt(2);
        }
        messageResource = CoeusMessageResources.getInstance();
        initComponents();
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnSend.addActionListener(this);
        
        //Added by Vyjayanthi 21/12/2003 - Start
        java.awt.Component[] components = {tblVersions, btnOk, btnCancel, btnSend };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        if( tblVersions.getRowCount() > 0 ){
            tblVersions.setRowSelectionInterval(0,0);
        }
        //End
        
        setResizable( false );
        tblVersions.addMouseListener( new MouseAdapter(){
            public void mouseClicked( MouseEvent me ) {
                if( me.getClickCount() == 2 ) {
                    try{
                        showReportDetails();
                    }catch( Exception e) {
                        e.printStackTrace();
                        CoeusOptionPane.showInfoDialog( e.getMessage() );
                    }
                }
            }
        });
        setValues();
        pack();
        if( tblVersions.getRowCount() > 0 ) {
            tblVersions.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        show();
    }
    // supporting method to set the form data.
    private void setValues() throws Exception{
        lblCommIdValue.setText(committeeID);
        lblCommNameValue.setText( committeeName );
        dataObject = getAllReportDetails();
        if( dataObject != null){
            dbData = (Vector)dataObject.elementAt(0);
            recipientData = (Vector)dataObject.elementAt(1);
        }
        if( dbData != null ) {
            ((DefaultTableModel)tblVersions.getModel()).setDataVector( 
                constructTableData(), getColumnNames() );
        }
        setTableColumnWidths();
        tblVersions.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);      
    }
    
    /**
     * This method is used to return the names of the columns used in the table
     * @return Collection of column names in the table used to show the versions
     * of the report.
     */
    protected Vector getColumnNames() {
        if( columnNames == null ) {
            columnNames = new Vector(); 
            Enumeration enumColNames = tblVersions.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    
    }
    /**
     * Method used to construct the table data for showing the version details of
     * the reports for a given schedule id.
     * @returns Vector of vectors used in setDataVector method for the table model
     */
    protected Vector constructTableData(){
        Vector tableData = new Vector();
        Vector tableRowData = null;
        int dbRowCount = dbData.size();
        for( int rowIndx = 0; rowIndx < dbRowCount ; rowIndx++ ) {
            Vector dbRow = (Vector) dbData.elementAt(rowIndx);
            if( dbRow != null && dbRow.size() >= 6 ) {
                tableRowData = new Vector();
                /* dbRow contains all details from respective database table for report
                 like committee id, scheduleid, agenda/minute number, creation date etc*/
                
                // to hold agenda/ minute number
                tableRowData.addElement( (String)dbRow.elementAt( 3 ) );
                
                // to hold created date
                tableRowData.addElement( CoeusDateFormat.format( 
                        dbRow.elementAt( 5 ).toString() ) );
                
                tableData.addElement( tableRowData );
            }
        }
        return tableData;
    }
    
    /**
     * Method used to set the preferred widths of all the columns in the table used.
     */
    protected void setTableColumnWidths(){
        TableColumn column = tblVersions.getColumnModel().getColumn(0);
        
        column.setMinWidth(100);
        //column.setPreferredWidth(100);
        
        column = tblVersions.getColumnModel().getColumn(1);
        column.setMinWidth(180);
        //column.setPreferredWidth(150);
        tblVersions.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblVersions.getTableHeader().setReorderingAllowed(false);
    }
    /**
     * Action listener implementation for all the buttons used in the form
     */
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        try{
            if( source.equals ( btnCancel ) ){
                dispose();
            }else if( source.equals( btnOk ) ) {
                showReportDetails();
            }else if( source.equals( btnSend ) ) {
                showMailForm( recipientData );
            }
        }catch(Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }
    
    protected abstract void showMailForm(Vector data);
    
    /**
     * Supporting method used to show the pdf report for the selected version 
     * number of the module. 
     */
    private void showReportDetails() throws Exception{
        int selRow = tblVersions.getSelectedRow();
        if( selRow != -1 ) {
            AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            String reportContextName = getSpecificReportContextName();

            // we will have to use jnlp library in order to showDocument when Applet 
            // is converted to application, as in application appletContext will be null.

            reportContextName = reportContextName.replace('\\', '/') ; // this is fix for Mac
//            reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + reportContextName );

//            if (coeusContxt != null)
//            {    
//                coeusContxt.showDocument( reportUrl, "_blank" );
//            } 
//            else
//            {
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService"); 
//                bs.showDocument( reportUrl );
//            }    
            URL urlObj = new URL(reportContextName);
            URLOpener.openUrl(urlObj);
            
            dispose();
        }else{
            CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(
                    "abstractReportViewFrm_exceptionCode.1001"));
        }
        
    }
    /**
     * Method which returns the reference to the table used in this form, which 
     * subclasses can use.
     *
     * @return JTable reference used in this form.
     */
    protected JTable getTableRef() {
        return tblVersions;
    }
    /**
     * Abstract method which returns the context name of the pdf report file to view.
     * @return String representing the context name of the pdf file.
     */
    public abstract String getSpecificReportContextName() throws Exception;
    
    /**
     * Abstract method used to get all the version details of pdf reports generated
     * for a given schedule id.
     * @returns Vector of Vectors which contains all the column details in 
     * corresponding database table where report details are stored.
     */
    public abstract Vector getAllReportDetails() throws Exception;
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblCommId = new javax.swing.JLabel();
        lblCommIdValue = new javax.swing.JLabel();
        lblCommName = new javax.swing.JLabel();
        lblCommNameValue = new javax.swing.JLabel();
        scrPnVersions = new javax.swing.JScrollPane();
        tblVersions = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        lblCommId.setFont(CoeusFontFactory.getLabelFont());
        lblCommId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommId.setText("Committee ID : ");
        lblCommId.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(lblCommId, gridBagConstraints);

        lblCommIdValue.setFont(CoeusFontFactory.getNormalFont());
        lblCommIdValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(lblCommIdValue, gridBagConstraints);

        lblCommName.setFont(CoeusFontFactory.getLabelFont());
        lblCommName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommName.setText("Committee Name : ");
        lblCommName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(lblCommName, gridBagConstraints);

        lblCommNameValue.setFont(CoeusFontFactory.getNormalFont());
        lblCommNameValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(lblCommNameValue, gridBagConstraints);

        scrPnVersions.setMinimumSize(new java.awt.Dimension(300, 150));
        scrPnVersions.setPreferredSize(new java.awt.Dimension(300, 150));
        tblVersions.setFont(CoeusFontFactory.getNormalFont());
        tblVersions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Version No.", "Date Created"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVersions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnVersions.setViewportView(tblVersions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        pnlMain.add(scrPnVersions, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 25, 0, 5);
        pnlMain.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 25, 0, 5);
        pnlMain.add(btnCancel, gridBagConstraints);

        btnSend.setFont(CoeusFontFactory.getLabelFont());
        btnSend.setMnemonic('S');
        btnSend.setText("Send");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 25, 0, 5);
        pnlMain.add(btnSend, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    /** Getter for property scheduleID.
     * @return Value of property scheduleID.
     */
    public java.lang.String getScheduleID() {
        return scheduleID;
    }    
    
    /** Setter for property scheduleID.
     * @param scheduleID New value of property scheduleID.
     */
    public void setScheduleID(java.lang.String scheduleID) {
        this.scheduleID = scheduleID;
    }    
    
    /** Getter for property tblVersions.
     * @return Value of property tblVersions.
     */
    public javax.swing.JTable getTblVersions() {
        return tblVersions;
    }
    
    /** Setter for property tblVersions.
     * @param tblVersions New value of property tblVersions.
     */
    public void setTblVersions(javax.swing.JTable tblVersions) {
        this.tblVersions = tblVersions;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel lblCommId;
    private javax.swing.JLabel lblCommIdValue;
    private javax.swing.JLabel lblCommName;
    private javax.swing.JLabel lblCommNameValue;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrPnVersions;
    private javax.swing.JTable tblVersions;
    // End of variables declaration//GEN-END:variables
    
}
