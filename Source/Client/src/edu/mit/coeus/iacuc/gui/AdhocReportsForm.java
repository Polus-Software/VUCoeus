/*
 * AdhocReportsForm.java
 *
 * Created on December 19, 2003, 2:49 PM
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.MalformedURLException;

import java.applet.AppletContext;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListSelectionModel;

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
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.iacuc.bean.CorrespondenceDetailsBean;
import edu.mit.coeus.utils.ReportGui; //Bijosh
import edu.mit.coeus.utils.CustomTagScanner; // Bijosh
import edu.mit.coeus.utils.CoeusVector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Cursor;
import edu.mit.coeus.irb.bean.AdhocDetailsBean;

public class AdhocReportsForm extends edu.mit.coeus.gui.CoeusDlgWindow 
                                           implements ActionListener
{
    private CoeusMessageResources messageResource;    
    private String scheduleID,committeeID,committeeName;
    private AdhocDetailsBean adhocDetailsBean ;
    private Vector dbData,columnNames;
    private Vector dataObject;
    private Vector recipientData;
    private URL reportUrl;
    String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/iacucCorrespondenceServlet";
    
    private ImageIcon imgIcnPDF ;
    private final String PROTOCOL_ACTION_SERVLET = "/protocolActionServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    
    
   public AdhocReportsForm(AdhocDetailsBean adhocDetailsBean)
    {
        super(CoeusGuiConstants.getMDIForm(), true );
        this.adhocDetailsBean = adhocDetailsBean ;
        setTitle("Correspondence") ;
    }
    
//    public AdhocReportsForm(Vector params, boolean modal)
//    {
//        super(CoeusGuiConstants.getMDIForm(), modal );
//        this.receivedParams = params;
//        setTitle("Correspondence") ;
//    }
    
        public void showForm() throws Exception {
        
        messageResource = CoeusMessageResources.getInstance();
        initComponents();
        btnView.addActionListener(this);
        btnCancel.addActionListener(this);
                
       
        java.awt.Component[] components = {tblAdhocList, btnView, btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        if( tblAdhocList.getRowCount() > 0 ){
            tblAdhocList.setRowSelectionInterval(0,0);
        }
        //End
        
        setResizable( false );
        tblAdhocList.addMouseListener( new MouseAdapter(){
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
        if( tblAdhocList.getRowCount() > 0 ) {
            tblAdhocList.requestFocusInWindow();
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
        dataObject = getAdhocReportDetails(); 
        System.out.println("rows retrieved" + dataObject.size()) ;
            ((DefaultTableModel)tblAdhocList.getModel()).setDataVector( 
               constructTableData(), getColumnNames() );
        tblAdhocList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;    
        setTableColumnWidths() ;   
    }
    
    protected Vector constructTableData()
    {
        Vector vecData = new Vector() ;
        for (int i = 0 ; i< dataObject.size() ; i++)
        {
           Vector vecDataRow = new Vector() ; 
           System.out.println("Adding row number " + i) ;
           AdhocDetailsBean adhocDetailsBean = (AdhocDetailsBean)dataObject.get(i) ;
           
           vecDataRow.add(adhocDetailsBean.getFormId()) ;
           vecDataRow.add(adhocDetailsBean.getDescription()) ; 
                     
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
            Enumeration enumColNames = tblAdhocList.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    
    }
    
     protected void setTableColumnWidths()
     {
        TableColumn column = tblAdhocList.getColumnModel().getColumn(0);
        column.setMinWidth(100);
        
        column = tblAdhocList.getColumnModel().getColumn(1);
        column.setMinWidth(315);
        
        tblAdhocList.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblAdhocList.getTableHeader().setReorderingAllowed(false);
    }
     
          
    public Vector getAdhocReportDetails() throws Exception
    {
        Vector correspData  = new Vector(3,2);
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('L');
        request.setId(String.valueOf(adhocDetailsBean.getModule())); // schedule (2 types) or protocol or committee
        if (adhocDetailsBean.getModule() == 'U')
        {    
            request.setDataObject(adhocDetailsBean.getScheduleId());
        }
        else
        {
            request.setDataObject(request) ;
        }
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
     
    private void showReportDetails() throws Exception
     {
        int selRowCount = tblAdhocList.getSelectedRowCount() ;    
        if (selRowCount <= 0) // no rows selected
        {
            CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(
                    "abstractReportViewFrm_exceptionCode.1002"));
        } 
        else if (selRowCount == 1) // one row selected, just display one document
                                   // (selRowCount > 1) multiple rows selected, merge all the docs n display 
        {
            //showDocument() ;
            showCustomizeWindow();
            
        } else if (selRowCount > 1) {
            showDocument();
        }
    }

    private void showCustomizeWindow() {
        try {
        Vector sendToServer = new Vector();
        byte[] stream = null;
        String startingCutomTag = null;
        String endingCutomTag = null;
        ByteArrayInputStream byteArrayInputStream=null;
        String scheduleID = null;
        String committeeId = null;
        String protoCorrespTypeDesc= null;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('P');
        int selRow =  tblAdhocList.getSelectedRow() ;
        adhocDetailsBean.setFormId(tblAdhocList.getValueAt(selRow, 0).toString()) ;
        adhocDetailsBean.setDescription(tblAdhocList.getValueAt(selRow, 1).toString()) ;
        request.setDataObject(adhocDetailsBean);
        //this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                Vector dataFromServerVec =(Vector)response.getDataObject();
                if (dataFromServerVec !=null) {
                    stream = (byte[])dataFromServerVec.get(0);// xsl stream
                    startingCutomTag = (String)dataFromServerVec.get(1);// Starting tag
                    endingCutomTag = (String)dataFromServerVec.get(2);// Ending tag
                    protoCorrespTypeDesc=(String)dataFromServerVec.get(3);//Get the protoCorresTypeDesc
                }
                
                if (stream != null) {
                    byteArrayInputStream = new ByteArrayInputStream(stream);
                }
                //Added for the case# COEUSDEV-219- send Functionality for agenda-start
                else{
                    CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(
                            "adHocPrintingForm_exceptionCode.1005"));
                }
                //Added for the case# COEUSDEV-219- send Functionality for agenda-start
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage());
            }
        }
  
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
                        Hashtable data = reportGui.getXslData();
                        byte[] customData = customTagScanner.replaceContents(data);
                        adhocDetailsBean.setFormId(tblAdhocList.getValueAt(selRow, 0).toString()) ;
                        adhocDetailsBean.setDescription(tblAdhocList.getValueAt(selRow, 1).toString()) ;
                        sendToServer.add(0, adhocDetailsBean);
                        sendToServer.add(1, customData);
                        generatePdf(sendToServer);
                    } finally {
                        this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                }else{
                    // Do the usual action
                    //scheduleXMLGenerator('M');
                }
            }else{
                // Do the usual action
                showDocument();
            }
        }
        }catch(javax.jnlp.UnavailableServiceException usex) {
            // Service is not supported
            CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey("printFrm_exceptionCode.1001"));
            usex.printStackTrace();
        } catch (Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    private void generatePdf(Vector data) throws Exception {
        boolean success=false;
        RequesterBean request = new RequesterBean();
        request.setDataObject(data);
        request.setFunctionType('T');
        AppletServletCommunicator comm = new AppletServletCommunicator(connURL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ) {
            String pdfURL = response.getDataObject().toString() ;
            AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            if (coeusContxt != null) {
                coeusContxt.showDocument( new URL(
                CoeusGuiConstants.CONNECTION_URL + pdfURL ), "_blank" );
            }
            else {
                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + pdfURL ));
                // try {
                if ((response.getId().equalsIgnoreCase("Yes")) || (response.getId().equalsIgnoreCase("Y")) ) {
                    String debugXmlURL = pdfURL.substring(0,pdfURL.indexOf(".pdf"))  + ".xml" ;
                    bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + debugXmlURL ));
                }
                //  }
                // catch(Exception xmlExp) {
                //   xmlExp.printStackTrace() ;
                // }
            }
            
        }
        else {
            CoeusOptionPane.showErrorDialog(response.getMessage()) ;
        }
    }
    
    private void showDocument() {
        AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        int selRow =  tblAdhocList.getSelectedRow() ;
        // send the selection
        adhocDetailsBean.setFormId(tblAdhocList.getValueAt(selRow, 0).toString()) ;
        adhocDetailsBean.setDescription(tblAdhocList.getValueAt(selRow, 1).toString()) ;
        
        RequesterBean request = new RequesterBean();
//        request.setFunctionType('R');
//        request.setDataObject(adhocDetailsBean);
        
        //For Streaming
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "GENERATE_CORRESPONDENCE");
        map.put("DATA", adhocDetailsBean);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        request.setDataObject(documentBean);
        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        //For Streaming
        
        this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
        STREAMING_SERVLET, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        try {
            
            if ( response.isSuccessfulResponse() ) {
                //String reportContextName = (String)response.getDataObject() ; // get the URL
                
                // we will have to use jnlp library in order to showDocument when Applet
                // is converted to application, as in application appletContext will be null.
                
//                reportContextName = reportContextName.replace('\\', '/') ; // this is fix for Mac
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + reportContextName );
                
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/') ; // this is fix for Mac
                
//                if (coeusContxt != null) {
//                    coeusContxt.showDocument( reportUrl, "_blank" );
//                }
//                else {
//                    javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                    bs.showDocument( reportUrl );
//                }
//                URL urlObj = new URL(reportUrl);
                URLOpener.openUrl(reportUrl);
                
                this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                dispose();
                
            }else{
                
                if(response.getDataObject() != null){
                    Object obj = response.getDataObject();
                    if( obj  instanceof CoeusException){
                        this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        throw (CoeusException)response.getDataObject();
                    }
                }else{
                    this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    throw new Exception(response.getMessage());
                }
            }
            
        }
        catch(Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(response.getMessage()) ;
        }
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        scrpnlMain = new javax.swing.JScrollPane();
        tblAdhocList = new javax.swing.JTable();
        btnView = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        scrpnlMain.setPreferredSize(new java.awt.Dimension(350, 150));
        tblAdhocList.setFont(CoeusFontFactory.getNormalFont());
        tblAdhocList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Form Id", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrpnlMain.setViewportView(tblAdhocList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        pnlMain.add(scrpnlMain, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlMain.add(btnView, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlMain.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        getContentPane().add(pnlMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        
    }//GEN-LAST:event_exitForm
    
        
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
        }catch(Exception e) {
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
        
        
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnView;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JScrollPane scrpnlMain;
    public javax.swing.JTable tblAdhocList;
    // End of variables declaration//GEN-END:variables
    
}
