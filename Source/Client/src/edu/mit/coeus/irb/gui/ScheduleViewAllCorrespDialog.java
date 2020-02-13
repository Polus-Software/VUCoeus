/*
 * ScheduleViewAllCorrespDialog.java
 *
 * Created on November 25, 2003, 6:03 PM
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.URLOpener;
import java.util.Vector;
import java.util.Enumeration;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;


import java.net.URL;

import java.applet.AppletContext;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.CorrespondenceDetailsBean;
import edu.mit.coeus.utils.CustomTagScanner;
import edu.mit.coeus.utils.ReportGui;
import edu.mit.coeus.utils.CoeusVector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Cursor;
import edu.mit.coeus.irb.bean.ProtocolActionsBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.util.*;
import java.io.ByteArrayInputStream;


public class ScheduleViewAllCorrespDialog  extends edu.mit.coeus.gui.CoeusDlgWindow 
                                           implements ActionListener 
{
    private CoeusMessageResources messageResource;    
    private String scheduleID,committeeID,committeeName;
    private Vector receivedParams;
    private Vector dbData,columnNames;
    private Vector dataObject;
    private Vector recipientData;
    private URL reportUrl;
    String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/correspondenceServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    
    private ImageIcon imgIcnPDF ;
    private final String PROTOCOL_ACTION_SERVLET = "/protocolActionServlet";

    private final static String ACTION_RIGHT = "PERFORM_IRB_ACTIONS_ON_PROTO";
    private final static char GET_CUSTOM_TAGS_REGENERATE = 'y';
    private final static char REGENERATE_CORRESPONDENCE_WITH_TAGS ='Y';
    
    
    /** Creates new form ScheduleViewAllCorrespDialog */
    public ScheduleViewAllCorrespDialog(Vector params, boolean modal)
    {
        super(CoeusGuiConstants.getMDIForm(), modal );
        this.receivedParams = params;
        setTitle("View Correspondence") ;
        
    }
    
    public ScheduleViewAllCorrespDialog(Vector params)
    {
        super( CoeusGuiConstants.getMDIForm(), true );
        this.receivedParams = params;
        setTitle("View Correspondence") ;
    }
    
     
        
    public void showForm() throws Exception {
        
        messageResource = CoeusMessageResources.getInstance();
        initComponents();
        btnView.addActionListener(this);
        btnCancel.addActionListener(this);
        btnSelectAll.addActionListener(this);
        btnGenerate.addActionListener(this);
      
        
        if( receivedParams != null )
        {
            if (receivedParams.get(1).toString().equals("D"))
            {
                btnGenerate.setEnabled(false) ;
            }    
            else
            {     
                if (receivedParams.size()> 2)
                {    
                    HashMap hashDocumentRights = (HashMap)receivedParams.get(2) ;
                    btnGenerate.setEnabled(((Boolean)hashDocumentRights.get(ACTION_RIGHT)).booleanValue()) ;
                }    
            }    
        }    
           
        
        
        
        //Added by Vyjayanthi 21/12/2003 - Start
        java.awt.Component[] components = {tblCorresp, btnView, btnSelectAll, btnGenerate, btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        if( tblCorresp.getRowCount() > 0 ){
            tblCorresp.setRowSelectionInterval(0,0);
        }
        //End
        
        setResizable( false );
        tblCorresp.addMouseListener( new MouseAdapter(){
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
        if( tblCorresp.getRowCount() > 0 ) {
            tblCorresp.requestFocusInWindow();
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
        dataObject = getAllReportDetails(); 
        System.out.println("rows retrieved" + dataObject.size()) ;
            ((DefaultTableModel)tblCorresp.getModel()).setDataVector( 
               constructTableData(), getColumnNames() );
        tblCorresp.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;    
        setTableColumnWidths() ;   
        setTableEditor() ;
        
    }
    
    protected Vector constructTableData()
    {
        Vector vecData = new Vector() ;
        for (int i = 0 ; i< dataObject.size() ; i++)
        {
           Vector vecDataRow = new Vector() ; 
           System.out.println("Adding row number " + i) ;
           CorrespondenceDetailsBean correspondenceBean = (CorrespondenceDetailsBean)dataObject.get(i) ;
           
           vecDataRow.add(correspondenceBean.getProtocolNumber()) ;
           //vecDataRow.add(new Integer(correspondenceBean.getSequenceNumber())) ;
           vecDataRow.add(correspondenceBean.getDescription()) ; 
           vecDataRow.add(CoeusDateFormat.format(correspondenceBean.getUpdateTimestamp().toString())) ;
           vecDataRow.add(new Boolean(correspondenceBean.isValid())) ;
          
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
            Enumeration enumColNames = tblCorresp.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    
    }
    
     protected void setTableColumnWidths()
     {
        TableColumn column = tblCorresp.getColumnModel().getColumn(0);
        column.setMinWidth(125);
        
        column = tblCorresp.getColumnModel().getColumn(1);
        column.setMinWidth(175);
        
        column = tblCorresp.getColumnModel().getColumn(2);
        column.setMinWidth(150);
        
        column = tblCorresp.getColumnModel().getColumn(3);
        column.setMinWidth(25);
         
                
        tblCorresp.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblCorresp.getTableHeader().setReorderingAllowed(false);
    }
     
     protected void setTableEditor()
     {
            TableColumn column = tblCorresp.getColumnModel().getColumn(3);
           // column.setCellEditor(new ScheduleViewAllCorrespDialog.BtnCellEditor()) ;
            column.setCellRenderer(new ScheduleViewAllCorrespDialog.BtnCellRenderer()) ;
      }
     
    public Vector getAllReportDetails() throws Exception{
        
        Vector correspData  = new Vector(3,2);
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('A');
        request.setId( receivedParams.get(0).toString()); // scheduleId
        request.setDataObject(request);

        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            correspData = (Vector)response.getDataObject();
            if( correspData == null || correspData.size() == 0 ) {
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
     
    private void selectAll()
    {
        tblCorresp.selectAll() ;
    }
    
     private void showReportDetails() throws Exception
     {
        int selRowCount = tblCorresp.getSelectedRowCount() ;    
        if (selRowCount <= 0) // no rows selected
        {
            CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(
                    "abstractReportViewFrm_exceptionCode.1002"));
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
        selRow =  tblCorresp.getSelectedRows() ;
        
        for (int rowCount = 0; rowCount < selRow.length ; rowCount++ )
        {
            vecDocParam.add(dataObject.get(selRow[rowCount])) ;
        }    

        RequesterBean request = new RequesterBean();
//        request.setFunctionType('V');
//        request.setDataObject(vecDocParam);
        
        //For Streaming
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "VIEW_ALL_CORRESP");
        map.put("DATA", vecDocParam);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ScheduleDocumentReader");
        documentBean.setParameterMap(map);
        request.setDataObject(documentBean);
        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        //For Streaming

        AppletServletCommunicator comm = new AppletServletCommunicator(STREAMING_SERVLET, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() )
        {
//            String reportContextName = (String)response.getDataObject() ; // get the URL

            // we will have to use jnlp library in order to showDocument when Applet 
            // is converted to application, as in application appletContext will be null.

//            reportContextName = reportContextName.replace('\\', '/') ; // this is fix for Mac
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
            map = (Map)response.getDataObject();
            String strReport = (String)map.get(DocumentConstants.DOCUMENT_URL);
            strReport = strReport.replace('\\', '/') ; // this is fix for Mac
            reportUrl = new URL(strReport);
            URLOpener.openUrl(reportUrl);
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
    
    
    private void regenerateLetter() throws Exception
        {
            boolean genComplete = false ;
            if (validateRegeneration())
            {    
                setCursor( new Cursor( Cursor.WAIT_CURSOR ) ); 
                int[] selRow = tblCorresp.getSelectedRows() ;
                String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_ACTION_SERVLET ;
                if (selRow.length>1) {
                    for (int genCount = 0; genCount < selRow.length ; genCount++) {
                        ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
                        
                        CorrespondenceDetailsBean correspondenceBean
                        = (CorrespondenceDetailsBean)dataObject.get(selRow[genCount]) ;
                        actionBean.setActionId(correspondenceBean.getActionId()) ;
                        System.out.println("Action Id : " + correspondenceBean.getActionId()) ;
                        actionBean.setProtocolNumber(correspondenceBean.getProtocolNumber()) ;
                        System.out.println("Protocol No : " + correspondenceBean.getProtocolNumber()) ;
                        actionBean.setSequenceNumber(correspondenceBean.getSequenceNumber()) ;
                        System.out.println("Sequence No : " + correspondenceBean.getSequenceNumber()) ;
                        actionBean.setSubmissionNumber(correspondenceBean.getSubmissionNumber()) ;
                        System.out.println("Submission No : " + correspondenceBean.getSubmissionNumber()) ;
                        
                        // connect to the database and get the formData for the given organization id
                        RequesterBean request = new RequesterBean();
                        
                        request.setFunctionType('G');
                        request.setId(String.valueOf(correspondenceBean.getProtocolCorrespondenceTypeCode())) ;
                        request.setDataObject(actionBean) ;
                        AppletServletCommunicator comm
                        = new AppletServletCommunicator(connectTo, request);
                        comm.send();
                        ResponderBean response = comm.getResponse();
                        if (response == null) {
                            response = new ResponderBean();
                            response.setResponseStatus(false);
                            response.setMessage(messageResource.parseMessageKey(
                            "server_exceptionCode.1000"));
                        }
                        if (response.isSuccessfulResponse()) {
                            genComplete = true ;
                        }
                        else {
                            setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                            genComplete = false ;
                            CoeusOptionPane.showInfoDialog("Error while regenerating the correspondence for Protocol " + correspondenceBean.getProtocolNumber()) ;
                            return ;
                        }
                    }// end for
                } else {
                    
                    ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
                    CorrespondenceDetailsBean correspondenceBean
                    = (CorrespondenceDetailsBean)dataObject.get(selRow[0]) ;
                    actionBean.setActionId(correspondenceBean.getActionId()) ;
                    actionBean.setProtocolNumber(correspondenceBean.getProtocolNumber()) ;
                    actionBean.setSequenceNumber(correspondenceBean.getSequenceNumber()) ;
                    actionBean.setSubmissionNumber(correspondenceBean.getSubmissionNumber()) ;
                    
                    // connect to the database and get the formData for the given organization id
                    RequesterBean request = new RequesterBean();
                    
                    request.setFunctionType(GET_CUSTOM_TAGS_REGENERATE);
                    
                    request.setId(String.valueOf(correspondenceBean.getProtocolCorrespondenceTypeCode())) ;
                    request.setDataObject(actionBean) ;
                    ResponderBean response = showCustomizeWindow(request,actionBean);
                    if (response == null) {
                        request.setFunctionType('G');
                        AppletServletCommunicator comm
                        = new AppletServletCommunicator(connectTo,request);
                        comm.send();
                        response = comm.getResponse();
                    }else if (response.getMessage() !=null && response.getMessage().equals("NO_TAGS")) {
                        request.setFunctionType('G');
                        AppletServletCommunicator comm
                        = new AppletServletCommunicator(connectTo,request);
                        comm.send();
                        response = comm.getResponse();
                    }
                    //ResponderBean response = comm.getResponse();
                    if (response == null) {
                        response = new ResponderBean();
                        response.setResponseStatus(false);
                        response.setMessage(messageResource.parseMessageKey(
                        "server_exceptionCode.1000"));
                    }
                    if (response.isSuccessfulResponse()) {
                        genComplete = true ;
                    }
                    else {
                        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                        genComplete = false ;
                        CoeusOptionPane.showInfoDialog("Error while regenerating the correspondence for Protocol " + correspondenceBean.getProtocolNumber()) ;
                        return ;
                    }
                }
                
                if (genComplete) {  // Display a msg saying regeneration successfull & Refresh the list
                    setValues() ;
                    setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                    CoeusOptionPane.showInfoDialog("Regenerate complete") ;
                }
            }// end if validation
    }
    
    //Bijosh
    private ResponderBean showCustomizeWindow(RequesterBean request,ProtocolActionsBean actionBean) {
        //,Vector data
        try {
            Vector dataObjects = new Vector();
            String connectTo =CoeusGuiConstants.CONNECTION_URL
            + PROTOCOL_ACTION_SERVLET ;
            Vector sendToServer = new Vector();
            byte[] stream = null;
            String startingCutomTag = null;
            String endingCutomTag = null;
            ByteArrayInputStream byteArrayInputStream=null;
            String scheduleID = null;
            String committeeId = null;
            String protoCorrespTypeDesc= null;
            // int selRow =  tblAdhocList.getSelectedRow() ;
            //this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            AppletServletCommunicator comm = new AppletServletCommunicator(
            connectTo, request );
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response!= null){
                if(response.isSuccessfulResponse()){
                    Vector dataFromServerVec =(Vector)response.getDataObjects();
                    if (dataFromServerVec !=null) {
                        stream = (byte[])dataFromServerVec.get(0);// xsl stream
                        startingCutomTag = (String)dataFromServerVec.get(1);// Starting tag
                        endingCutomTag = (String)dataFromServerVec.get(2);// Ending tag
                        protoCorrespTypeDesc=(String)dataFromServerVec.get(3);//Get the protoCorresTypeDesc
                    }
                    if (stream != null) {
                        byteArrayInputStream = new ByteArrayInputStream(stream);
                    }
                }else {
                    if (response == null) {
                        response.setMessage("NO_TAGS");
                        return response;
                    }
                    if (response.getMessage().equals("NO_TAGS")) {
                        return response;
                    }
                    CoeusOptionPane.showErrorDialog(response.getMessage());
                }
            }
            ResponderBean responderBean = null;
            if (byteArrayInputStream != null) {
                java.io.BufferedWriter modifiedXsl = null;
                String readtext="";
                java.io.DataInputStream dataInputStream = new java.io.DataInputStream(byteArrayInputStream);
                CustomTagScanner customTagScanner = new CustomTagScanner();
                CoeusVector cvCustomTags = customTagScanner.stringScan(stream,startingCutomTag,endingCutomTag);
                // check for the custom tags. If present then popup the window for the corresponsding
                //tags. If not then generate pdf without popping up the window
                if(cvCustomTags!= null && cvCustomTags.size()>0){
                    ReportGui  reportGui = new ReportGui(CoeusGuiConstants.getMDIForm(),protoCorrespTypeDesc);
                    reportGui.setTemplateData(cvCustomTags);
                    reportGui.postInitComponents();
                    int action;
                    try {
                        this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                        action = reportGui.displayReportGui();
                    } finally {
                        this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                    if(action ==reportGui.CLICKED_OK){
                        try {
                        this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                        Hashtable htData = reportGui.getXslData();
                        byte[] customData = customTagScanner.replaceContents(htData);
                        //adhocDetailsBean.setFormId(tblAdhocList.getValueAt(selRow, 0).toString()) ;
                        // adhocDetailsBean.setDescription(tblAdhocList.getValueAt(selRow, 1).toString()) ;
                        dataObjects.add(0,actionBean);
                        dataObjects.add(1,customData);
                        request.setFunctionType(REGENERATE_CORRESPONDENCE_WITH_TAGS);
                        request.setDataObjects(dataObjects);
                        responderBean= generatePdfForTags(request);
                        } finally {
                            this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        }
                    }else{
                        // Do the usual action
                        //scheduleXMLGenerator('M');
                    }
                }else{
                    // Do the usual action
                    // showDocument();
                }
            }
            return responderBean;
        } catch (Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
            return null;
        }
    }
    //Bijosh
    
    //Bijosh
    private ResponderBean generatePdfForTags(RequesterBean request) {
        String connectTo =CoeusGuiConstants.CONNECTION_URL
        + PROTOCOL_ACTION_SERVLET ;
        boolean success=false;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        return response;
    }
    //Bijosh
    
    private boolean validateRegeneration() {
        System.out.println("validating") ;
        
        if (tblCorresp.getSelectedRowCount()<= 0) // no rows selected
        {
            CoeusOptionPane.showErrorDialog("Select a correspondence for generation") ;
            return false ;
        }
        //        else if (tblCorresp.getSelectedRowCount()> 1) // more than one rows selected
        //        {
        //            CoeusOptionPane.showErrorDialog("Select only one correspondence for generation") ;
        //            return false ;
        //        }
        //        else
        //        {
        //            int selRow = tblCorresp.getSelectedRow() ;
        //             CorrespondenceDetailsBean correspondenceBean
        //                                        = (CorrespondenceDetailsBean)dataObject.get(selRow) ;
        //            if (correspondenceBean.isValid() == true) // if correspondence doesnt need regeneration
        //            {
        //                CoeusOptionPane.showErrorDialog("This correspondence does not need regeneration") ;
        //                return false ;
        //            }
        //        }
        
        return true ;
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        scrplCorresp = new javax.swing.JScrollPane();
        tblCorresp = new javax.swing.JTable();
        btnView = new javax.swing.JButton();
        btnSelectAll = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnGenerate = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlMain.setLayout(new java.awt.GridBagLayout());

        scrplCorresp.setPreferredSize(new java.awt.Dimension(425, 150));
        tblCorresp.setFont(CoeusFontFactory.getNormalFont());
        tblCorresp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Protocol Number", "Correspondence", "Date Created", "Valid"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrplCorresp.setViewportView(tblCorresp);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(scrplCorresp, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 5);
        pnlMain.add(btnView, gridBagConstraints);

        btnSelectAll.setFont(CoeusFontFactory.getLabelFont());
        btnSelectAll.setMnemonic('S');
        btnSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        pnlMain.add(btnSelectAll, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 5, 5);
        pnlMain.add(btnCancel, gridBagConstraints);

        btnGenerate.setFont(CoeusFontFactory.getLabelFont());
        btnGenerate.setMnemonic('G');
        btnGenerate.setText("Regenerate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        pnlMain.add(btnGenerate, gridBagConstraints);

        getContentPane().add(pnlMain, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    public void actionPerformed(java.awt.event.ActionEvent actionEvent)
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
                selectAll() ;
            }    
            else if (source.equals(btnGenerate))
            {
               System.out.println("Regenerating correspondence") ;
                regenerateLetter() ;            
            }
            
//            else if( source.equals( btnSend ) ) {
//                showMailForm( recipientData );
//            }
        }catch(Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnGenerate;
    public javax.swing.JButton btnSelectAll;
    public javax.swing.JButton btnView;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JScrollPane scrplCorresp;
    public javax.swing.JTable tblCorresp;
    // End of variables declaration//GEN-END:variables

    
    class BtnCellRenderer extends DefaultTableCellRenderer 
    {
        private JButton btnView;
                
        BtnCellRenderer() 
        {
            
        }
    
         public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
         {
             Boolean val = (Boolean)value ;
             if (val.equals(new Boolean(true)))
                imgIcnPDF = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
            else
                imgIcnPDF = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON));
            
            btnView = new JButton(imgIcnPDF);
            
            return btnView;
        }
        
    }        
    
//    class BtnCellEditor extends DefaultCellEditor
//    {
//        private JButton btnViewDocuments;
//                
//        BtnCellEditor()
//        {
//            super(new JComboBox());
//            
//        }
//            
//        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) 
//        {
//            Boolean val = (Boolean)value ;
//             if (val.equals(new Boolean(true)))
//                imgIcnPDF = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
//            else
//                imgIcnPDF = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON));
//            
//            btnViewDocuments = new JButton(imgIcnPDF);
//            
//            return btnViewDocuments;
//        }
//        
//    }
 
    
}
