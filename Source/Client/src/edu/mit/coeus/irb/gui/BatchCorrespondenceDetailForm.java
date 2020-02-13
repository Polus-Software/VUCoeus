/*
 * BatchCorrespondenceDetailForm.java
 *
 * Created on March 8, 2004, 3:37 PM
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
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import java.applet.*;
import java.net.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;

public class BatchCorrespondenceDetailForm extends edu.mit.coeus.gui.CoeusDlgWindow 
                                           implements ActionListener {
    
                                               
    private CoeusMessageResources messageResource;    
    private Vector dbData,columnNames;
    private Vector dataObject;
    private BatchCorrespondenceBean batchCorrespondenceBean ;
    private Vector recipientData;
    private URL reportUrl ;
       
    String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/correspondenceServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
   private DateUtils dtUtils = new DateUtils();
   private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
   
   
                                               
    public BatchCorrespondenceDetailForm(BatchCorrespondenceBean batchCorrespondenceBean) {
        super(CoeusGuiConstants.getMDIForm(), true );
        this.batchCorrespondenceBean = batchCorrespondenceBean ;
        setTitle("Batch Details") ;
        
    }
    
    public void showForm() throws Exception
    {
        messageResource = CoeusMessageResources.getInstance();
        initComponents();
        btnView.addActionListener(this);
        btnCancel.addActionListener(this);
        btnSelectAll.addActionListener(this);         
        btnSaveAs.addActionListener(this);
        
        java.awt.Component[] components = {tblProtocol, btnView, btnCancel, btnSelectAll};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        if( tblProtocol.getRowCount() > 0 ){
            tblProtocol.setRowSelectionInterval(0,0);
        }
        //End
        
        setResizable( false );
        tblProtocol.addMouseListener( new MouseAdapter(){
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
            (DefaultTableModel)tblProtocol.getModel(),false);
        tblProtocol.setModel(sorter);
        sorter.addMouseListenerToHeaderInTable(tblProtocol);
           
        
        txtCommitteeId.setText(batchCorrespondenceBean.getCommitteeId()) ;
        txtCommitteeName.setText(batchCorrespondenceBean.getCommitteeName()) ;
        
        setValues();
        pack();
        if( tblProtocol.getRowCount() > 0 ) {
            tblProtocol.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        show();
     
    
    }
    
    
    private void setValues() throws Exception
    {
        tblProtocol.removeAll() ;
        Vector vecData = getBatchReportDetails() ;
        if (vecData != null)
        {
            batchCorrespondenceBean = (BatchCorrespondenceBean) vecData.get(0) ;
            
            txtLastRunDate.setText(dtUtils.formatDate(
                      batchCorrespondenceBean.getBatchRunDate().toString(), "dd-MMM-yyyy")) ;
            txtLastStartDate.setText(dtUtils.formatDate(
                      batchCorrespondenceBean.getTimeWindowStart().toString(), "dd-MMM-yyyy")) ;
            txtLastEndDate.setText(dtUtils.formatDate(
                      batchCorrespondenceBean.getTimeWindowEnd().toString(), "dd-MMM-yyyy")) ;
            txtUserId.setText(batchCorrespondenceBean.getUpdateUser()) ;
            
            String tempStr = convertTimeToString(new java.sql.Time(batchCorrespondenceBean.getUpdateTimestamp().getTime())) ; 
            txtTime.setText(tempStr ) ;  
          
            
            dataObject = (Vector)vecData.get(1) ;
            if (dataObject != null && dataObject.size() > 0)
            {
                System.out.println("rows retrieved" + dataObject.size()) ;
                ((DefaultTableModel)tblProtocol.getModel()).setDataVector( 
                constructTableData(), getColumnNames() );
                tblProtocol.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;   
            }    
            
            setTableColumnWidths() ;   
                
         }    
        
    }
    
    protected Vector constructTableData()
    {
        Vector vecData = new Vector() ;
        
        for (int i = 0 ; i< dataObject.size() ; i++)
        {
           Vector vecDataRow = new Vector() ; 
           System.out.println("Adding row number " + i) ;
           BatchCorrespondenceDetailsBean batchCorrespondenceDetailsBean = (BatchCorrespondenceDetailsBean)dataObject.get(i) ;
           
           vecDataRow.add(batchCorrespondenceDetailsBean.getProtocolNumber()) ;
           vecDataRow.add(String.valueOf(batchCorrespondenceDetailsBean.getProtocolSequenceNumber())) ;
           vecDataRow.add(String.valueOf(batchCorrespondenceDetailsBean.getProtocolActionId())) ;
           
           vecDataRow.add(batchCorrespondenceDetailsBean.getProtocolTitle()) ; 
                      
           if (batchCorrespondenceDetailsBean.getProtocolApprovalDate() != null)
           {
            vecDataRow.add(dtUtils.formatDate(
                      batchCorrespondenceDetailsBean.getProtocolApprovalDate().toString(), "dd-MMM-yyyy")) ;
           }
           else
           {
                vecDataRow.add("") ;
           }  
           
           if (batchCorrespondenceDetailsBean.getProtocolExpirationDate() != null)
           {    
             vecDataRow.add(dtUtils.formatDate(
                      batchCorrespondenceDetailsBean.getProtocolExpirationDate().toString(), "dd-MMM-yyyy")) ;
           }
           else
           {
                vecDataRow.add("") ;
           }    
           vecDataRow.add(batchCorrespondenceDetailsBean.getDescription()) ;
           
           vecData.add(vecDataRow) ;
        }   
    
        return vecData ;
    }
    
    
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
            Enumeration enumColNames = tblProtocol.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    
    }
    
     protected void setTableColumnWidths()
     {
        TableColumn column = tblProtocol.getColumnModel().getColumn(0);
        column.setMinWidth(100);
        column = tblProtocol.getColumnModel().getColumn(1);
        column.setMinWidth(0);
        column.setMaxWidth(0) ;
        
        
        column = tblProtocol.getColumnModel().getColumn(2);
        column.setMinWidth(0);
        column.setMaxWidth(0) ;
        
        column = tblProtocol.getColumnModel().getColumn(3);
        column.setMinWidth(250);
        
        column = tblProtocol.getColumnModel().getColumn(4);
        column.setMinWidth(100);
        
        column = tblProtocol.getColumnModel().getColumn(5);
        column.setMinWidth(100);
        
        column = tblProtocol.getColumnModel().getColumn(6);
        column.setMinWidth(200);
        
        
        tblProtocol.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblProtocol.getTableHeader().setReorderingAllowed(false);
    }
     
          
    public Vector getBatchReportDetails() throws Exception
    {
        Vector batchData  = new Vector(3,2);
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('b');
        request.setId(this.batchCorrespondenceBean.getCorrespondenceBatchId()); 
        request.setDataObject(this.batchCorrespondenceBean);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            batchData = response.getDataObjects();
            if( batchData == null || batchData.size() == 0 ) {
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
        return batchData ;
      } 
     
    private void showReportDetails() throws Exception
     {
        int selRowCount = tblProtocol.getSelectedRowCount() ;    
        if (selRowCount <= 0) // no rows selected
        {
            CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(
                    "batchReportViewFrm_exceptionCode.1001"));
        }
        else if (selRowCount >= 1) // one row selected, just display one document
                                   // (selRowCount > 1) multiple rows selected, merge all the docs n display 
        {
            showDocument() ;
        }    
    }
     
    private void showDocument ()
    {        
       try
       {
        AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        int[] selRow ;
        Vector vecDocParam = new Vector() ;
        selRow =  tblProtocol.getSelectedRows() ;
        
        for (int rowCount = 0; rowCount < selRow.length ; rowCount++ )
        {
            CorrespondenceDetailsBean correspondenceDetailsBean = new CorrespondenceDetailsBean() ;
            correspondenceDetailsBean.setProtocolNumber(tblProtocol.getValueAt(selRow[rowCount], 0).toString()) ;
            correspondenceDetailsBean.setSequenceNumber(Integer.parseInt(tblProtocol.getValueAt(selRow[rowCount], 1).toString())) ;
            correspondenceDetailsBean.setActionId(Integer.parseInt(tblProtocol.getValueAt(selRow[rowCount], 2).toString())) ;
            vecDocParam.add(correspondenceDetailsBean) ;
        }    

        RequesterBean request = new RequesterBean();
//        request.setFunctionType('V');
//        request.setDataObject(vecDocParam);
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("DATA", vecDocParam);
        map.put("DOCUMENT_TYPE", "BATCH_CORRESPONDENCE");
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.BatchCorrespondenceReader");
        documentBean.setParameterMap(map);
        request.setDataObject(documentBean);
        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    STREAMING_SERVLET, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() )
        {
//            String reportContextName = (String)response.getDataObject() ; // get the URL
//
//            // we will have to use jnlp library in order to showDocument when Applet 
//            // is converted to application, as in application appletContext will be null.
//
//            reportContextName = reportContextName.replace('\\', '/') ; // this is fix for Mac
//            reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + reportContextName );
//
//            if (coeusContxt != null)
//            {    
//                coeusContxt.showDocument( reportUrl, "_blank" );
//            } 
//            else
//            {
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService"); 
//                bs.showDocument( reportUrl );
//            }    
            map = (Map)response.getDataObject();
            String strUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
            strUrl = strUrl.replace('\\', '/');
            URL urlObj = new URL(strUrl);
            URLOpener.openUrl(urlObj);
            dispose();

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

       }
       catch(Exception ex)
       {
       
       ex.printStackTrace() ;
       }
    }
    
    
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
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblCommitteeId = new javax.swing.JLabel();
        txtCommitteeId = new edu.mit.coeus.utils.CoeusTextField();
        lblCommitteeName = new javax.swing.JLabel();
        txtCommitteeName = new edu.mit.coeus.utils.CoeusTextField();
        lblLastRunDate = new javax.swing.JLabel();
        txtLastRunDate = new edu.mit.coeus.utils.CoeusTextField();
        lblLastStartDate = new javax.swing.JLabel();
        txtLastStartDate = new edu.mit.coeus.utils.CoeusTextField();
        lblLastEndDate = new javax.swing.JLabel();
        txtLastEndDate = new edu.mit.coeus.utils.CoeusTextField();
        scrpnlProtocol = new javax.swing.JScrollPane();
        tblProtocol = new javax.swing.JTable();
        lblUserId = new javax.swing.JLabel();
        txtUserId = new edu.mit.coeus.utils.CoeusTextField();
        lblTime = new javax.swing.JLabel();
        txtTime = new edu.mit.coeus.utils.CoeusTextField();
        pnlBtn = new javax.swing.JPanel();
        btnView = new javax.swing.JButton();
        btnSelectAll = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSaveAs = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeId.setText("Committee Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 5, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblCommitteeId, gridBagConstraints);

        txtCommitteeId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 106;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtCommitteeId, gridBagConstraints);

        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setText("Committee Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 5, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblCommitteeName, gridBagConstraints);

        txtCommitteeName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtCommitteeName, gridBagConstraints);

        lblLastRunDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastRunDate.setText("Run Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblLastRunDate, gridBagConstraints);

        txtLastRunDate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 106;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtLastRunDate, gridBagConstraints);

        lblLastStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastStartDate.setText("Start Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblLastStartDate, gridBagConstraints);

        txtLastStartDate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 106;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtLastStartDate, gridBagConstraints);

        lblLastEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastEndDate.setText("End Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblLastEndDate, gridBagConstraints);

        txtLastEndDate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtLastEndDate, gridBagConstraints);

        tblProtocol.setFont(CoeusFontFactory.getNormalFont());
        tblProtocol.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Protocol Number", "Sequence Number", "Action Id", "Title", "Approval Date", "Expiration Date", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrpnlProtocol.setViewportView(tblProtocol);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 25;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlMain.add(scrpnlProtocol, gridBagConstraints);

        lblUserId.setFont(CoeusFontFactory.getLabelFont());
        lblUserId.setText("User Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 3);
        pnlMain.add(lblUserId, gridBagConstraints);

        txtUserId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 106;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtUserId, gridBagConstraints);

        lblTime.setFont(CoeusFontFactory.getLabelFont());
        lblTime.setText("Time:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 3);
        pnlMain.add(lblTime, gridBagConstraints);

        txtTime.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlMain.add(txtTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlMain, gridBagConstraints);

        pnlBtn.setLayout(new java.awt.GridBagLayout());

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(66, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        pnlBtn.add(btnView, gridBagConstraints);

        btnSelectAll.setFont(CoeusFontFactory.getLabelFont());
        btnSelectAll.setMnemonic('A');
        btnSelectAll.setText("Select All");
        btnSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        pnlBtn.add(btnSelectAll, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlBtn.add(btnCancel, gridBagConstraints);

        btnSaveAs.setFont(CoeusFontFactory.getLabelFont());
        btnSaveAs.setMnemonic('A');
        btnSaveAs.setText("Save As");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        pnlBtn.add(btnSaveAs, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 26;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlBtn, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAllActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnSelectAllActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        
    }//GEN-LAST:event_exitForm
    
    public void saveAsActiveSheet() 
    {
         SaveAsDialog saveAsDialog = new SaveAsDialog(tblProtocol);
    }
    
    public void actionPerformed(ActionEvent actionEvent) 
    {
        Object source = actionEvent.getSource();
        try{
            if( source.equals ( btnCancel ) ){
                dispose();
            }
            else if( source.equals( btnView ) ) 
            {
                showReportDetails();
            }
            else if (source.equals(btnSelectAll))
            {
                tblProtocol.selectAll() ;
            }    
            else if (source.equals(btnSaveAs))
            {
                saveAsActiveSheet() ;
            }
        }catch(Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
        
    }    
    
       
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSaveAs;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnView;
    private javax.swing.JLabel lblCommitteeId;
    private javax.swing.JLabel lblCommitteeName;
    private javax.swing.JLabel lblLastEndDate;
    private javax.swing.JLabel lblLastRunDate;
    private javax.swing.JLabel lblLastStartDate;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblUserId;
    private javax.swing.JPanel pnlBtn;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrpnlProtocol;
    private javax.swing.JTable tblProtocol;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeId;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeName;
    private edu.mit.coeus.utils.CoeusTextField txtLastEndDate;
    private edu.mit.coeus.utils.CoeusTextField txtLastRunDate;
    private edu.mit.coeus.utils.CoeusTextField txtLastStartDate;
    private edu.mit.coeus.utils.CoeusTextField txtTime;
    private edu.mit.coeus.utils.CoeusTextField txtUserId;
    // End of variables declaration//GEN-END:variables
    
}
