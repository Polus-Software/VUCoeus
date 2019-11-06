/*
 * @(#)BatchCorrespondenceHistoryForm.java 9th march 2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
 /* PMD check performed, and commented unused imports and variables on 09-NOV-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.iacuc.gui;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Date;

import java.awt.Dimension;
import java.awt.Toolkit; 
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.saveas.*;
import java.applet.*;
import java.awt.event.*;
import java.net.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;


public class BatchCorrespondenceHistoryForm extends edu.mit.coeus.gui.CoeusDlgWindow 
                                           implements ActionListener {
        
    private CoeusMessageResources messageResource;    
    private Vector dbData,columnNames;
    private Vector dataObject;
    private Vector vecParam ;
    private URL reportUrl ;
    private int batchCorespondenceTypeCode = 1 ; 
    //Modified for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-start
    String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/iacucCorrespondenceServlet";
    private String DATE_FORMAT = "dd-MMM-yyyy";
    private String DATE_SEPERATOR = "/-:,";
    //Modified for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-end
    
   private DateUtils dtUtils = new DateUtils();
   private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
   
    public BatchCorrespondenceHistoryForm(Vector vecParam) 
    {
        super(CoeusGuiConstants.getMDIForm(), true );
        setTitle("Batch History") ;
        this.vecParam = vecParam ;
    }
    
        /*
        * This method is used to set the form data and their components.   
        * @return Void
        */
        public void showForm() throws Exception
        {
        messageResource = CoeusMessageResources.getInstance();
        initComponents();
       
        txtCommitteeId.setText((String)vecParam.get(0)) ;
        txtCommitteeName.setText((String)vecParam.get(1)) ;
        
        txtLastStartDate.setDocument(new LimitedPlainDocument(11));
        txtLastStartDate.addFocusListener(new CustomFocusAdapter());
        
        txtLastEndDate.setDocument(new LimitedPlainDocument(11));
        txtLastEndDate.addFocusListener(new CustomFocusAdapter());
                
        btnOk.addActionListener(this);
        btnDetails.addActionListener(this);
        btnSaveAs.addActionListener(this) ;
        btnShowlist.addActionListener(this) ;
        
        cmbCorrespondenceType.addItemListener(new ItemListener()
        {
        public void itemStateChanged(java.awt.event.ItemEvent evt)
        {
            if (evt.getStateChange() == ItemEvent.SELECTED)
            {
                 ComboBoxBean cmb = (ComboBoxBean)cmbCorrespondenceType.getSelectedItem() ;
                 batchCorespondenceTypeCode = Integer.parseInt(cmb.getCode()) ;
                 try
                 {
                    txtLastStartDate.setText("") ;
                    txtLastEndDate.setText("") ;
                    setValues(batchCorespondenceTypeCode) ;             
                 }
                 catch(Exception ex)
                 {
                    ex.printStackTrace();
                    CoeusOptionPane.showInfoDialog( ex.getMessage() );
                 }
            } 
            
            if(evt.getStateChange() == evt.DESELECTED)
            {
                return ;
            }
            
         }        
        }) ;
        
        java.awt.Component[] components = {cmbCorrespondenceType, txtLastStartDate, txtLastEndDate, tblBatchHistory, btnDetails, btnShowlist, btnSaveAs, btnOk};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        if( tblBatchHistory.getRowCount() > 0 ){
            tblBatchHistory.setRowSelectionInterval(0,0);
        }      
        
        setResizable( false );
        tblBatchHistory.addMouseListener( new MouseAdapter(){
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
                
        TableSorter sorter = new TableSorter(
            (DefaultTableModel)tblBatchHistory.getModel(),false);
        tblBatchHistory.setModel(sorter);
        sorter.addMouseListenerToHeaderInTable(tblBatchHistory);
        sorter.sortByColumn(0, false) ;        
        
        initialiseData() ;
        pack();
        if( tblBatchHistory.getRowCount() > 0 ) {
            tblBatchHistory.requestFocusInWindow();
        }else{
            btnDetails.requestFocusInWindow();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        show();   
    }
       
   /*
    * This method will initialize the combo box data for renewal reminder selection and 
    * reminder to IACUC nitification  
    * @return Void
    */
     private void initialiseData() throws Exception
     {
        ComboBoxBean comboBean ;
        //Modified for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-start
        comboBean = new ComboBoxBean("1", "IACUC Protocol Renewal Reminders") ;
        cmbCorrespondenceType.addItem(comboBean) ;
        comboBean = new ComboBoxBean("2", "Reminder to IACUC Notifications") ;
        //Modified for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-end
        cmbCorrespondenceType.addItem(comboBean) ;        
     }
     
   /*
    * This method will set the values of batch correspondence history form
    * @param int corrCode  
    * @return Void
    */
     private void setValues(int corrCode) throws Exception
     {
        dataObject = getBatchHistoryDetails(corrCode); 

        if (dataObject != null && dataObject.size() > 0)
        {                                        
            ((DefaultTableModel)tblBatchHistory.getModel()).setDataVector( 
                 constructTableData(), getColumnNames() );
            tblBatchHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;    
        }    
        else
        {
             ((DefaultTableModel)tblBatchHistory.getModel()).setDataVector( 
                new Vector(), getColumnNames() );
        }    
        setTableColumnWidths() ;   
    }
    
   /*
    * This method will set construct the table data after changing the comboBox selection   
    * @return Vector vecData
    */
    protected Vector constructTableData()
    {
        Vector vecData = new Vector() ;
        for (int i = 0 ; i< dataObject.size() ; i++)
        {
           Vector vecDataRow = new Vector() ;         
           BatchCorrespondenceBean batchCorrespondenceBean = (BatchCorrespondenceBean)dataObject.get(i) ;
           
           vecDataRow.add(batchCorrespondenceBean.getCorrespondenceBatchId()) ;
           vecDataRow.add(dtUtils.formatDate(
                      batchCorrespondenceBean.getBatchRunDate().toString(), DATE_FORMAT)) ; 
           vecDataRow.add(dtUtils.formatDate(
                      batchCorrespondenceBean.getTimeWindowStart().toString(), DATE_FORMAT)) ;
           vecDataRow.add(dtUtils.formatDate(
                    batchCorrespondenceBean.getTimeWindowEnd().toString(), DATE_FORMAT)) ;
           
           String tempStr = convertTimeToString(new java.sql.Time(batchCorrespondenceBean.getUpdateTimestamp().getTime())) ;
           
           vecDataRow.add(tempStr) ;
//           vecDataRow.add(batchCorrespondenceBean.getUpdateUser()) ;
            /*
             * UserID to UserName Enhancement - Start
             * Added new property getUpdateUserName() to get the username
             */
           vecDataRow.add(batchCorrespondenceBean.getUpdateUserName()) ;
           // UserId to UserName Enhancement - End
           
           vecData.add(vecDataRow) ;
        }   
    
        return vecData ;
    }
    
   /*
    * This method will set the values of batch correspondence history form
    * @param Time time  
    * @return String strTime
    */
    public String convertTimeToString(java.sql.Time time)
    {
        GregorianCalendar gCal = new GregorianCalendar();
        String strTime = "";
        if(time!=null){
            /* if time is present then convert it to HH:mm format */
            gCal.setTime(time);
            String hours = "" ;
            String minutes = "";
            if(gCal.get(Calendar.HOUR_OF_DAY)<=9){
                /* If minutes is a single digit append 0 before that */
                hours = "0"+gCal.get(Calendar.HOUR_OF_DAY);
            }else{
                hours = ""+gCal.get(Calendar.HOUR_OF_DAY);
            }
            
            if(gCal.get(Calendar.MINUTE)<=9){
                /* If minutes is a single digit append 0 before that */
                minutes = "0"+gCal.get(Calendar.MINUTE);
            }else{
                minutes = ""+gCal.get(Calendar.MINUTE);
            }
            strTime = hours +":"+minutes;
        }
        return strTime;

    }  
    
    /*
    * This method is used to close the dialog box
    * @param WindowEvent evt corrCode  
    * @return Void
    */
    private void closeDialog(java.awt.event.WindowEvent evt) 
    {
        setVisible(false);
        dispose();
    }
      /**
     * This method is used to return the names of the columns used in the table
     * @return Collection of column names in the table used to show the versions
     * of the report.
     */
    protected Vector getColumnNames() {
        if( columnNames == null ) {
            columnNames = new Vector(); 
            Enumeration enumColNames = tblBatchHistory.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    
    }
    
    /*
     * This method is used to set the table column height and width   
     */
     protected void setTableColumnWidths()
     {
        TableColumn column = tblBatchHistory.getColumnModel().getColumn(0);
        column.setMinWidth(75);
        
        column = tblBatchHistory.getColumnModel().getColumn(1);
        column.setMinWidth(75);
        
        column = tblBatchHistory.getColumnModel().getColumn(2);
        column.setMinWidth(75);
        
        column = tblBatchHistory.getColumnModel().getColumn(3);
        column.setMinWidth(75);
        
        column = tblBatchHistory.getColumnModel().getColumn(4);
        column.setMinWidth(50);
        
        column = tblBatchHistory.getColumnModel().getColumn(5);
        column.setMinWidth(100);
        
        tblBatchHistory.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblBatchHistory.getTableHeader().setReorderingAllowed(false);
    }
     
    /*
    * This method is used to get the batch history details from server
    * @param int correspondenceBatchTypeCode  
    * @return Vector correspData
    */      
    public Vector getBatchHistoryDetails(int correspondenceBatchTypeCode) throws Exception
    {
        Vector correspData  = new Vector(3,2);
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('h');
        BatchCorrespondenceBean batchCorrespondenceBean = new BatchCorrespondenceBean() ;
        batchCorrespondenceBean.setCommitteeId(txtCommitteeId.getText()) ;
        
        if (txtLastStartDate.getText() != null && txtLastStartDate.getText().trim().length() > 0)
        {    
            batchCorrespondenceBean.setTimeWindowStart(new java.sql.Date(dtFormat.parse(
                            dtUtils.restoreDate(txtLastStartDate.getText(),DATE_SEPERATOR)).getTime())) ;
        }                    
        else
        {    
            batchCorrespondenceBean.setTimeWindowStart(null) ;
        }
        
        if (txtLastEndDate.getText() != null && txtLastEndDate.getText().trim().length() > 0)
        {    
            batchCorrespondenceBean.setTimeWindowEnd(new java.sql.Date(dtFormat.parse(
                            dtUtils.restoreDate(txtLastEndDate.getText(),DATE_SEPERATOR)).getTime())) ;
        }                    
        else
        {    
            batchCorrespondenceBean.setTimeWindowEnd(null) ;
        }
                
        batchCorrespondenceBean.setCorrespondenceBatchTypeCode(correspondenceBatchTypeCode) ;
        request.setId(txtCommitteeId.getText()); 
        request.setDataObject(batchCorrespondenceBean);

        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            correspData = (Vector)response.getDataObject();
            if( correspData == null) {
                throw new Exception(messageResource.parseMessageKey(
                    "abstractReportViewFrm_exceptionCode.1003"));
            }
            
        }else{
            
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if( obj  instanceof CoeusException){
                    throw (CoeusException)response.getDataObject();
                }
            }else{
                throw new Exception(response.getMessage());
            }
        }
        return correspData ;
      } 
     
   /*
    * This method is used to show the report detail  
    */
    private void showReportDetails() throws Exception
     {
            
        if (tblBatchHistory.getRowCount()<= 0) // no rows selected
        {
            CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(
                    "batchReportViewFrm_exceptionCode.1002"));
        }
        else 
        {
           int selRow = tblBatchHistory.getSelectedRow() ; 
           if (selRow >= 0)
           {    
                showDocument(selRow) ;
           }
           else
           {
             CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(
                    "batchReportViewFrm_exceptionCode.1002"));
           }    
        }    
    }     
    
    /*
    * This method is used to show the correspondence generated report in pdf format
    * @param int selRow       
    */
    private void showDocument(int selRow) throws Exception
    {
        BatchCorrespondenceBean viewBatchCorrespondenceBean 
                    = new BatchCorrespondenceBean() ;
               viewBatchCorrespondenceBean.setCorrespondenceBatchId(tblBatchHistory.getValueAt(selRow,0).toString()) ;
               viewBatchCorrespondenceBean.setCommitteeId(txtCommitteeId.getText()) ;
               viewBatchCorrespondenceBean.setCommitteeName(txtCommitteeName.getText()) ;
              BatchCorrespondenceDetailForm batchCorrespondenceDetailForm 
              = new BatchCorrespondenceDetailForm(viewBatchCorrespondenceBean) ;
              batchCorrespondenceDetailForm.showForm() ;
    }
    
    /*
    * This method is used to validate the form data
    * @return Boolean value       
    */
    public boolean validateData() throws Exception
    {
        
    if((txtLastStartDate.getText()== null || txtLastEndDate.getText() == null))
    {
            /* Application Date doesn't have any value */
            return false;
            
    }
    else if (txtLastStartDate.getText().trim().length() <= 0)
    {
        return false ;
    }    
    else if (txtLastEndDate.getText().trim().length() <= 0)
    {
        return false ;
    }
    else{
            Date applnDate = null;
            Date apprDate = null;
            Date expDate = null;

            String oldDate;
            String convertedDate ;
            if((txtLastStartDate.getText() != null)
                && (txtLastStartDate.getText().trim().length() > 0)){

                convertedDate = dtUtils.formatDate(txtLastStartDate.getText(),
                        DATE_SEPERATOR , DATE_FORMAT);
                
                if (convertedDate==null){
                    oldDate = dtUtils.restoreDate(txtLastStartDate.getText(),DATE_SEPERATOR);
                    if(oldDate == null || oldDate.equals(txtLastStartDate.getText())){
                        return false;
                    }
                }
                apprDate = dtFormat.parse(
                    dtUtils.restoreDate(txtLastStartDate.getText(),"/:-,"));

                if(apprDate == null){
                    return false;
                }
            }
            if((txtLastEndDate.getText() != null)
                && (txtLastEndDate.getText().trim().length() > 0)){

                convertedDate = dtUtils.formatDate(txtLastEndDate.getText(),
                        DATE_SEPERATOR , DATE_FORMAT);
                if (convertedDate==null){
                    oldDate = dtUtils.restoreDate(txtLastEndDate.getText(),DATE_SEPERATOR);
                    if(oldDate == null || oldDate.equals(txtLastEndDate.getText())){
                        return false;
                    }
                }

                expDate = dtFormat.parse(
                    dtUtils.restoreDate(txtLastEndDate.getText(),"/:-,"));
                if(expDate == null){
                    return false;
                }
            }

            if(expDate != null && apprDate != null){
                if( (expDate.compareTo(apprDate) <0 ))
                {
                    txtLastEndDate.requestFocus();
                    return false;
                }
            }
           
        }
        return true;
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        cmbCorrespondenceType = new edu.mit.coeus.utils.CoeusComboBox();
        scrlBatch = new javax.swing.JScrollPane();
        tblBatchHistory = new javax.swing.JTable();
        lblCommitteeId = new javax.swing.JLabel();
        txtCommitteeId = new edu.mit.coeus.utils.CoeusTextField();
        lblCommitteeName = new javax.swing.JLabel();
        txtCommitteeName = new edu.mit.coeus.utils.CoeusTextField();
        lblCorrespondenceType = new javax.swing.JLabel();
        lblLastStartDate = new javax.swing.JLabel();
        txtLastStartDate = new edu.mit.coeus.utils.CoeusTextField();
        txtLastEndDate = new edu.mit.coeus.utils.CoeusTextField();
        pnlBtnShow = new javax.swing.JPanel();
        btnDetails = new javax.swing.JButton();
        btnShowlist = new javax.swing.JButton();
        btnSaveAs = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        lblLastStartDate1 = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        cmbCorrespondenceType.setMinimumSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlMain.add(cmbCorrespondenceType, gridBagConstraints);

        scrlBatch.setPreferredSize(new java.awt.Dimension(325, 175));
        tblBatchHistory.setFont(CoeusFontFactory.getNormalFont());
        tblBatchHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Batch Id", "Run Date", "Start Date", "End Date", "Time", "User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrlBatch.setViewportView(tblBatchHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 69;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlMain.add(scrlBatch, gridBagConstraints);

        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeId.setText("Batch Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 3);
        pnlMain.add(lblCommitteeId, gridBagConstraints);

        txtCommitteeId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlMain.add(txtCommitteeId, gridBagConstraints);

        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setText("Committee Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 3);
        pnlMain.add(lblCommitteeName, gridBagConstraints);

        txtCommitteeName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 0);
        pnlMain.add(txtCommitteeName, gridBagConstraints);

        lblCorrespondenceType.setFont(CoeusFontFactory.getLabelFont());
        lblCorrespondenceType.setText("Committee Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 3);
        pnlMain.add(lblCorrespondenceType, gridBagConstraints);

        lblLastStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastStartDate.setText("Batch Run between:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 3);
        pnlMain.add(lblLastStartDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlMain.add(txtLastStartDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 119, 5, 0);
        pnlMain.add(txtLastEndDate, gridBagConstraints);

        pnlBtnShow.setLayout(new java.awt.GridBagLayout());

        btnDetails.setFont(CoeusFontFactory.getLabelFont());
        btnDetails.setMnemonic('V');
        btnDetails.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlBtnShow.add(btnDetails, gridBagConstraints);

        btnShowlist.setFont(CoeusFontFactory.getLabelFont());
        btnShowlist.setMnemonic('R');
        btnShowlist.setText("Refresh");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        pnlBtnShow.add(btnShowlist, gridBagConstraints);

        btnSaveAs.setFont(CoeusFontFactory.getLabelFont());
        btnSaveAs.setMnemonic('A');
        btnSaveAs.setText("Save As");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        pnlBtnShow.add(btnSaveAs, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlMain.add(pnlBtnShow, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('C');
        btnOk.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 3, 0);
        pnlMain.add(btnOk, gridBagConstraints);

        lblLastStartDate1.setFont(CoeusFontFactory.getLabelFont());
        lblLastStartDate1.setText("and");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 91, 5, 3);
        pnlMain.add(lblLastStartDate1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 2, 0);
        getContentPane().add(pnlMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        
    }//GEN-LAST:event_exitForm
    
   /*
    * This method is used to save the batch history in .xls format
    */
    public void saveAsActiveSheet() {
         SaveAsDialog saveAsDialog = new SaveAsDialog(tblBatchHistory);
    }
    
    /*Action performed*/
    public void actionPerformed(ActionEvent actionEvent) 
    {
        Object source = actionEvent.getSource();
        try{
            if( source.equals ( btnOk ) ){
                dispose();
            }
            else if( source.equals( btnDetails ) ) 
            {
                showReportDetails() ;
            }
            else if( source.equals( btnSaveAs ) ) 
            {
                saveAsActiveSheet() ;
            }
            else if (source.equals( btnShowlist ))
            {
                setValues(this.batchCorespondenceTypeCode) ;
            }  
        }catch(Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }    
    
    /*
    * This method is used to show end time of schedule data  
    */
    public void showEndDate() throws java.text.ParseException
    {
            Date apprDate = null;
            String stDate = dtUtils.restoreDate(txtLastStartDate.getText(),"/:-,");
            apprDate = new java.sql.Date((dtFormat.parse(stDate)).getTime());
            java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
            cal.setTimeInMillis(dtFormat.parse(stDate).getTime());
            // set the time gap appropriately
            cal.set(Calendar.DATE,cal.get(Calendar.DATE)+ 1);
            String expDate = (cal.get(Calendar.MONTH)+1)+"/"
                    +cal.get(Calendar.DATE)+"/"+(cal.get(Calendar.YEAR));
            txtLastEndDate.setText(dtUtils.formatDate( expDate, "/", DATE_FORMAT));
      }   
    
    /**
    * Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field.
    */
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;
        String strDate = "";
        String oldData = "";
        boolean temporary = false;

        public void focusGained (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))) {
                    oldData = dateField.getText();
                    String focusDate = dtUtils.restoreDate(
                            dateField.getText(),DATE_SEPERATOR);
                    dateField.setText(focusDate);
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
                            dtUtils.formatDate(dateField.getText(), DATE_SEPERATOR ,
                                    DATE_FORMAT);
                    if (convertedDate==null){
                            CoeusOptionPane.showErrorDialog("Please enter a valid date in mm/dd/yyyy format.");
                        dateField.setText(oldData);
                        temporary = true;
                    }else {
                        dateField.setText(convertedDate);
                        temporary = false;
                        if( fe.getSource() == txtLastStartDate ) {
                             try{  
                                 showEndDate();
                              }catch(java.text.ParseException PException){ 
                                CoeusOptionPane.showErrorDialog("Please enter a valid date in mm/dd/yyyy format.");
                              }
                        }
                    }
                }
            }
        }
    }    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetails;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnSaveAs;
    private javax.swing.JButton btnShowlist;
    private edu.mit.coeus.utils.CoeusComboBox cmbCorrespondenceType;
    private javax.swing.JLabel lblCommitteeId;
    private javax.swing.JLabel lblCommitteeName;
    private javax.swing.JLabel lblCorrespondenceType;
    private javax.swing.JLabel lblLastStartDate;
    private javax.swing.JLabel lblLastStartDate1;
    private javax.swing.JPanel pnlBtnShow;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrlBatch;
    private javax.swing.JTable tblBatchHistory;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeId;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeName;
    private edu.mit.coeus.utils.CoeusTextField txtLastEndDate;
    private edu.mit.coeus.utils.CoeusTextField txtLastStartDate;
    // End of variables declaration//GEN-END:variables
    
}
