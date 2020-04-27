/*
 * PrintProposal.java
 *
 * Created on December 20, 2004, 7:47 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.sponsormaint.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import java.applet.AppletContext;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
/**
 *
 * @author  chandrashekara
 */
    
 public class PrintProposal extends javax.swing.JComponent 
    implements ListSelectionListener, ActionListener,ItemListener{
   
     private static final int PACKAGE_NO = 0;
    private static final int PACKAGE_DESC = 1;
    private static final String EMPTY_STRING = "";
    
    private static final int PAGE_NUMBER_COLUMN = 0;
    private static final int PAGE_DESC_COLUMN = 1;
    private static final int PAGE_UPDATE_COLUMN = 2;
    private static final int PAGE_USER_COLUMN =3;
    
    private static final int WIDTH = 540;
    private static final int HEIGHT = 450;
    
    private CoeusDlgWindow dlgSponsorMaintainance;
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusVector cvPackageData;
    private CoeusVector cvPageData;
    private CoeusVector cvFilterVector;
    private CoeusVector cvDeletedPackageData;
    private CoeusVector cvDeletedPageData;

    
    private QueryEngine queryEngine;
    private static final String POST_SCRIPT = "ps";
    

    private CoeusFileChooser fileChooser;
    //private boolean isTableDataChanged =false;
    
    
    private static final String TIME_STAMP = "MM/dd/yyyy";
    private static final String REQUIRED_DATE_FORMAT = TIME_STAMP +" hh:mm:ss ";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    
    
    
    private PackageTableModel packageTableModel;
    private PageTableModel pageTableModel;
    private PackageRenderer packageRenderer;
    private PageRenderer pageRenderer;
    private CoeusMessageResources  coeusMessageResources;
    private DateUtils dtUtils = new DateUtils();
    
    private final String connect = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
    private static final char  GET_PRINT_DATA = 'g';
    private static final char UPDATE_SPONSOR_TEMPLATE = 'S'; 
    private static final char GET_SPONSOR_TEMPLATE = 'T'; 
    private static final char UPDATE_FORM_DATA = 'V';
    private static final char PRINT_PROPOSAL = 'P'; 
    
    private String sponsorCode=EMPTY_STRING;
    private String sponsorName = EMPTY_STRING;
    private boolean modified;
    
    private SponsorFormsBean  sponsorFormsBean = null;
    private SponsorTemplateBean sponsorTemplateBean = null;
    private int pageRowId ;
    private int packageRowId;
    private String queryKey=EMPTY_STRING;
    private int packageNumber=1;
    private int pageNumber=1;
    private String mesg = EMPTY_STRING;
    private boolean canPackageExists;
    int selPackageNumber;
    
    /** Creates new form PrintProposal */
    public PrintProposal(ProposalDevelopmentFormBean proposalBean,CoeusAppletMDIForm mdiForm) {
         this.proposalBean = proposalBean;
         this.mdiForm = mdiForm;
        initComponents();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setTableEditors();
        postInitComponents();
    }
    
    private void registerComponents(){
        
        
        packageTableModel = new PackageTableModel();
        pageTableModel = new PageTableModel();
        
        packageRenderer = new PackageRenderer();
        pageRenderer = new PageRenderer();
        
        tblPackage.setModel(packageTableModel);
         //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        tblPackage.setTableHeader(null);
        tblPages.setTableHeader(null);
        //Case#2445 - End
        tblPages.setModel(pageTableModel);
        
        
        tblPackage.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //tblPages.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPackage.getSelectionModel().addListSelectionListener(this);
        tblPages.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblPages.getSelectionModel().addListSelectionListener(this);
        
        btnClose.addActionListener(this);
        btnPrint.addActionListener(this);
        chkPrintAll.addItemListener(this);
        
        java.awt.Component[] components = {
            btnPrint,btnClose
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
        
    }
    
    private void setTableEditors(){
        //Commented for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
//        JTableHeader tablePackageHeader = tblPackage.getTableHeader();
//        tablePackageHeader.setReorderingAllowed(false);
//        tablePackageHeader.setFont(CoeusFontFactory.getLabelFont());
        //Case#2445 - End
        tblPackage.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPackage.setRowHeight(22);
        tblPackage.setShowHorizontalLines(true);
        tblPackage.setShowVerticalLines(true);
        tblPackage.setOpaque(false);
        tblPackage.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
       //Commented for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start 
//        JTableHeader tblPageHeader = tblPages.getTableHeader();
//        tblPageHeader.setReorderingAllowed(false);
//        tblPageHeader.setFont(CoeusFontFactory.getLabelFont());
        //Case#2445 - End
        tblPages.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPages.setRowHeight(22);
        
        tblPages.setShowHorizontalLines(false);
        tblPages.setShowVerticalLines(false);
        tblPages.setOpaque(false);
        tblPages.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        
        TableColumn packageColumn = tblPackage.getColumnModel().getColumn(0);
        // This is a hidden column to keep track of page number.
        packageColumn.setMinWidth(0);
        packageColumn.setMaxWidth(0);
        packageColumn.setPreferredWidth(0);
        packageColumn.setResizable(true);
        
        packageColumn = tblPackage.getColumnModel().getColumn(1);
        packageColumn.setPreferredWidth(455);
        packageColumn.setResizable(true);
        packageColumn.setCellRenderer(packageRenderer);
        
        
        TableColumn pageColumn = tblPages.getColumnModel().getColumn(0);

        pageColumn.setMinWidth(0);
        pageColumn.setMaxWidth(0);
        pageColumn.setPreferredWidth(0);
        pageColumn.setResizable(true);
        
        pageColumn= tblPages.getColumnModel().getColumn(1);
        pageColumn.setPreferredWidth(457);
        pageColumn.setCellRenderer(pageRenderer);
        pageColumn.setResizable(true);
    }
    
    public void setFormData(){
        String sponsorCode = EMPTY_STRING;
        String sponsorName = EMPTY_STRING;
        if(proposalBean.getSponsorCode()!=null && 
            (!proposalBean.getSponsorCode().trim().equals(EMPTY_STRING))){
            sponsorCode = proposalBean.getSponsorCode().trim();
        }else{
            sponsorCode = EMPTY_STRING;
        }
        
        if(proposalBean.getSponsorName()!= null && 
            (!proposalBean.getSponsorName().trim().equals(EMPTY_STRING))){
            sponsorName = proposalBean.getSponsorName().trim();
        }else{
            sponsorName = EMPTY_STRING;
        }
        //Commented for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        //lblSponsor.setText("Sponsor Code:  "+sponsorCode+ " : "+sponsorName);
        //Case#2445 - End
        
        cvPackageData= new CoeusVector();
        cvPageData = new CoeusVector();
        cvDeletedPackageData = new CoeusVector();
        cvDeletedPageData = new CoeusVector();
        
        Hashtable htSponsorData = getPckagePageData(proposalBean);
        if(htSponsorData != null && htSponsorData.size() > 0){
            cvPackageData = (CoeusVector) htSponsorData.get(KeyConstants.PACKAGE_DATA);
            cvPackageData.sort("packageNumber",false);
            cvPageData = (CoeusVector)htSponsorData.get(KeyConstants.PAGE_DATA);
            packageTableModel.setData(cvPackageData);
            display();
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("proposal_print_exceptionCode.1001"));
            return;
        }
    }
    
    private Hashtable getPckagePageData(ProposalDevelopmentFormBean proposalBean){
        Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setFunctionType(GET_PRINT_DATA);
        requester.setDataObject(proposalBean);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            htData = (Hashtable)responder.getDataObject();
        }
        return htData;
    }
    
    
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgSponsorMaintainance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(btnPrint)){
                performPrintAction();
            }else if(source.equals(btnClose)){
                closeWindow();
            }
        }catch(Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        finally{
            dlgSponsorMaintainance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == ItemEvent.SELECTED){
            if(itemEvent.getSource().equals(chkPrintAll)){
                selectAllAction();
            }
        }else if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
            selectAllAction();
        }
        
    }
    
    private void selectAllAction(){
        if(chkPrintAll.isSelected()){
            if(tblPages.getRowCount()>0){
                tblPages.setRowSelectionInterval(0, tblPages.getRowCount()-1);
            }else{
                tblPages.setRowSelectionInterval(0, 0);
            }
        }else{
            if(tblPages.getRowCount()>0){
                tblPages.setRowSelectionInterval(0,0);
            }
        }
    }
    
    //    /**
    //     *  Code to go for printing
    //     */
    //    private String performPrintAction() throws Exception{
    //         String printServletURL = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
    //         RequesterBean requester;
    //         ResponderBean responder;
    //
    //         Hashtable htPrintParams = new Hashtable();
    //         htPrintParams.put("PROPOSAL_NUMBER", this.proposalBean.getProposalNumber());
    //         int sltdRow = tblPackage.getSelectedRow();
    //
    //         htPrintParams.put("SPONSOR_CODE", ((SponsorFormsBean)
    //                      ((PackageTableModel)tblPackage.getModel()).getValueAt(
    //                                    sltdRow)).getSponsorCode());
    //         Integer packageNumber = (Integer)tblPackage.getValueAt(sltdRow, 0);
    //         htPrintParams.put("PACKAGE_NUMBER", packageNumber.toString());
    //         Integer pageNumber = (Integer)tblPages.getValueAt(tblPages.getSelectedRow(), 0);
    //         htPrintParams.put("PAGE_NUMBER", pageNumber.toString());
    //         requester = new RequesterBean();
    //         requester.setFunctionType(PRINT_PROPOSAL);
    //         requester.setDataObject(htPrintParams);
    //
    //         AppletServletCommunicator comm
    //         = new AppletServletCommunicator(printServletURL, requester);
    //
    //         comm.send();
    //         responder = comm.getResponse();
    //         String fileName = "";
    //         if(responder.isSuccessfulResponse()){
    //             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
    //
    //             //             System.out.println("Got the Clob Data");
    //             fileName = (String)responder.getDataObject();
    //             System.out.println("Report Filename is=>"+fileName);
    //
    //             fileName = fileName.replace('\\', '/') ; // this is fix for Mac
    //             URL reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
    //
    //             if (coeusContxt != null) {
    //                 coeusContxt.showDocument( reportUrl, "_blank" );
    //             }else {
    //                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
    //                 bs.showDocument( reportUrl );
    //             }
    //             closeWindow();
    //
    //         }else{
    //             throw new CoeusException(responder.getMessage());
    //         }
    //         return fileName;
    //    }
    
    
    
    /**
     *  Code to go for printing
     */
    private String performPrintAction() throws Exception{
        //String printServletURL = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
        String printServletURL = CoeusGuiConstants.CONNECTION_URL+"/StreamingServlet";
        RequesterBean requester;
        ResponderBean responder;
        int selPackageRow = tblPackage.getSelectedRow();
        int selRows[] = tblPages.getSelectedRows();
        CoeusVector data = new CoeusVector();
        for(int index = 0; index<selRows.length ; index++){
            Hashtable htPrintParams = new Hashtable();
            htPrintParams.put("PROPOSAL_NUMBER", this.proposalBean.getProposalNumber());
            htPrintParams.put("SPONSOR_CODE", ((SponsorFormsBean)
                          ((PackageTableModel)tblPackage.getModel()).getValueAt(
                                        selPackageRow)).getSponsorCode());
//            this.proposalBean.getSponsorCode());
            Integer packageNumber = (Integer)tblPackage.getValueAt(selPackageRow, 0);
            htPrintParams.put("PACKAGE_NUMBER", packageNumber.toString());
            Integer pageNumber = (Integer)tblPages.getValueAt(selRows[index], 0);
            htPrintParams.put("PAGE_NUMBER", pageNumber.toString());
            htPrintParams.put("PAGE_DATA", ((PageTableModel)tblPages.getModel()).getValueAt(
                                        selRows[index]));
            data.addElement(htPrintParams);
        }
        requester = new RequesterBean();
        //requester.setFunctionType(PRINT_PROPOSAL);
        //requester.setDataObject(htPrintParams);
        //requester.setDataObjects(data);
        
        //For Streaming
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("PRINT_PROPOSAL", data);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPrintReader");
        documentBean.setParameterMap(map);
        requester.setDataObject(documentBean);
        requester.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(printServletURL, requester);
        
        comm.send();
        responder = comm.getResponse();
        //String fileName = "";
        /*if(responder.isSuccessfulResponse()){
            AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            
            //             System.out.println("Got the Clob Data");
            fileName = (String)responder.getDataObject();
            System.out.println("Report Filename is=>"+fileName);
            
            fileName = fileName.replace('\\', '/') ; // this is fix for Mac
            URL reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
            
            if (coeusContxt != null) {
                coeusContxt.showDocument( reportUrl, "_blank" );
            }else {
                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                bs.showDocument( reportUrl );
            }
            closeWindow();
            
        }else{
            throw new CoeusException(responder.getMessage());
        }*/
        
        String url = "";
        Object dataObject = null;
        if(responder.hasResponse()) {
            dataObject = responder.getDataObject();
            if(dataObject != null && dataObject instanceof Map) {
               map = (Map)dataObject;
               url = (String)map.get(DocumentConstants.DOCUMENT_URL);
               URL urlObj = new URL(url);
               URLOpener.openUrl(urlObj);
            }else {
                throw new CoeusException("Server returned no Data");
            }
        }else{
            throw new CoeusException(responder.getMessage());
        }
        
        return url;
    }
    
    
    private void closeWindow(){
        dlgSponsorMaintainance.dispose();
    }
    
    private void postInitComponents(){
        
        dlgSponsorMaintainance = new CoeusDlgWindow(mdiForm);
        dlgSponsorMaintainance.getContentPane().add(this);
        dlgSponsorMaintainance.setTitle("Print Proposal : "+this.proposalBean.getProposalNumber());
        dlgSponsorMaintainance.setFont(CoeusFontFactory.getLabelFont());
        dlgSponsorMaintainance.setModal(true);
        dlgSponsorMaintainance.setResizable(false);
        dlgSponsorMaintainance.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSponsorMaintainance.getSize();
        dlgSponsorMaintainance.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgSponsorMaintainance.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                closeWindow();
                return;
            }
        });
        dlgSponsorMaintainance.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSponsorMaintainance.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                closeWindow();
                return;
            }
        });
        
        dlgSponsorMaintainance.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    
    private void setWindowFocus(){
        
        //         if(tblPackage.getRowCount()>0){
        //             tblPackage.setRowSelectionInterval(0,0);
        //         }else{
        btnPrint.requestFocusInWindow();
        // }
    }
    
    public class PackageTableModel extends AbstractTableModel{
        
        private String colName[] = {EMPTY_STRING,EMPTY_STRING};
        private Class colClass[] ={Integer.class, String.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public void setData(CoeusVector cvPackageData){
            cvPackageData = cvPackageData;
        }
        
        public int getRowCount() {
            if(cvPackageData==null){
                return 0;
            }else{
                return cvPackageData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            SponsorFormsBean sponsorFormsBean =
            (SponsorFormsBean)cvPackageData.get(row);
            switch(col){
                case PACKAGE_NO:
                    return new Integer(sponsorFormsBean.getPackageNumber());
                case PACKAGE_DESC:
                    return sponsorFormsBean.getPackageName();
            }
            return EMPTY_STRING;
        }
        public Object getValueAt(int row) {
            SponsorFormsBean sponsorFormsBean =
            (SponsorFormsBean)cvPackageData.get(row);
            return sponsorFormsBean;
        }
    }
    
    
    public class PageTableModel extends AbstractTableModel{
        
        private String colName[] = {EMPTY_STRING,EMPTY_STRING};
        private Class colClass[] ={Integer.class,String.class};
        CoeusVector data= new CoeusVector();
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public void setData(CoeusVector pageData){
            this.data = pageData;
        }
        
        public int getRowCount() {
            if(cvFilterVector== null){
                return 0;
            }else{
                return cvFilterVector.size();
            }
        }
        public Object getValueAt(int row, int col) {
            SponsorTemplateBean bean = (SponsorTemplateBean)this.data.get(row);
            if(bean!= null){
                switch(col){
                    case PAGE_NUMBER_COLUMN:
                        return new Integer(bean.getPageNumber());
                    case PAGE_DESC_COLUMN:
                        return bean.getPageDescription();
                }
            }
            return EMPTY_STRING;
        }
        public Object getValueAt(int row) {
            SponsorTemplateBean pageBean =
	            (SponsorTemplateBean)data.get(row);
            return pageBean;
        }
    }
    
    public class PackageRenderer extends DefaultTableCellRenderer{
        public PackageRenderer(){
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int col){
            
            if(col==PACKAGE_DESC){
                
                if(value!= null || !(value.toString().trim().equals(EMPTY_STRING))){
                    setText(value.toString());
                }else{
                    setText(EMPTY_STRING);
                }
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
        
    }
    
    public class PageRenderer extends DefaultTableCellRenderer{
        public PageRenderer(){
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int col){
            switch(col){
                case PAGE_NUMBER_COLUMN:
                    if(value!= null && !(value.toString().trim().equals(EMPTY_STRING))){
                        setText(value.toString());
                    }else{
                        setText(EMPTY_STRING);
                    }
                    break;
                case PAGE_DESC_COLUMN:
                    if(value!= null && !(value.toString().trim().equals(EMPTY_STRING))){
                        setText(value.toString());
                    }else{
                        setText(EMPTY_STRING);
                    }
                    break;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
    }
    
    
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if(cvPackageData != null && cvPackageData.size() > 0){
            //if( !listSelectionEvent.getValueIsAdjusting()){
            ListSelectionModel source = (ListSelectionModel)listSelectionEvent.getSource();
            int selRow = tblPackage.getSelectedRow();
            
            if (source.equals(tblPackage.getSelectionModel())) {
                if (selRow != -1) {
                    cvFilterVector = new CoeusVector();
                    filterData(selRow);
                    if(tblPages.getRowCount()>0){
                        tblPages.setRowSelectionInterval(0,0);
                    }
                }
            }else if(source.equals(tblPages.getSelectionModel())){
                // Check if all the rows selected then select the check box
                // else deselect the check box
                if( tblPages.getSelectedRows().length-1 == tblPages.getRowCount()-1){
                    chkPrintAll.setSelected(true);
                }else{
                    chkPrintAll.setSelected(false);
                }
            }
        }
    }
    
    
    private void filterData(int selRow){
        SponsorFormsBean  sponsorFormsBean= (SponsorFormsBean)cvPackageData.get(selRow);
        sponsorFormsBean= (SponsorFormsBean)cvPackageData.get(selRow);
        int packageNumber = sponsorFormsBean.getPackageNumber();
        Equals eqFilter = new Equals("packageNumber",new Integer(packageNumber));
        Equals eqSpCodeFilter = new Equals("sponsorCode",sponsorFormsBean.getSponsorCode());
        And pckNumAndSpCode = new And(eqFilter,eqSpCodeFilter);
        
        cvFilterVector = cvPageData.filter(pckNumAndSpCode);
        pageTableModel.setData(cvFilterVector);
        pageTableModel.fireTableDataChanged();
    }
    
    private int getMaxPackageNumber() {
        int packageNumber = 1;
        if(cvPackageData!= null && cvPackageData.size() > 0 ) {
            cvPackageData.sort("packageNumber",false);
            SponsorFormsBean bean=null;
            bean = (SponsorFormsBean)cvPackageData.get(0);
            packageNumber= bean.getPackageNumber();
            packageNumber = packageNumber+1;
            canPackageExists = true;
            return packageNumber;
        }else {
            return packageNumber;
        }
    }
    
    public void display(){
        if(tblPackage.getRowCount()>0){
            tblPackage.setRowSelectionInterval(0,0);
        }else{
            btnPrint.requestFocusInWindow();
        }
        dlgSponsorMaintainance.setVisible(true);
    }
    private ProposalDevelopmentFormBean proposalBean;
    
    /** Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     *
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /** Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     *
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     *
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     *
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public ProposalDevelopmentFormBean getProposalBean() {
        return proposalBean;
    }
    
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalBean(ProposalDevelopmentFormBean proposalBean) {
        this.proposalBean = proposalBean;
        this.sponsorCode = proposalBean.getSponsorCode();
        this.sponsorName = proposalBean.getSponsorName();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblPackegeToPrint = new javax.swing.JLabel();
        scrPnPackage = new javax.swing.JScrollPane();
        tblPackage = new javax.swing.JTable();
        lblPages = new javax.swing.JLabel();
        chkPrintAll = new javax.swing.JCheckBox();
        scrPnPages = new javax.swing.JScrollPane();
        tblPages = new javax.swing.JTable();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblPackegeToPrint.setFont(CoeusFontFactory.getLabelFont());
        lblPackegeToPrint.setText("Select a package to print");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 3, 0, 0);
        add(lblPackegeToPrint, gridBagConstraints);

        scrPnPackage.setMinimumSize(new java.awt.Dimension(450, 200));
        tblPackage.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnPackage.setViewportView(tblPackage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(scrPnPackage, gridBagConstraints);

        lblPages.setFont(CoeusFontFactory.getLabelFont());
        lblPages.setText("Select page[s] to print");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblPages, gridBagConstraints);

        chkPrintAll.setFont(CoeusFontFactory.getLabelFont());
        chkPrintAll.setMnemonic('a');
        chkPrintAll.setText("Print all pages");
        chkPrintAll.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(chkPrintAll, gridBagConstraints);

        scrPnPages.setMinimumSize(new java.awt.Dimension(460, 200));
        tblPages.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnPages.setViewportView(tblPages);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(scrPnPages, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setMnemonic('P');
        btnPrint.setText("Print");
        btnPrint.setMinimumSize(new java.awt.Dimension(66, 26));
        btnPrint.setPreferredSize(new java.awt.Dimension(66, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        add(btnPrint, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMinimumSize(new java.awt.Dimension(66, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(65, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrint;
    private javax.swing.JCheckBox chkPrintAll;
    private javax.swing.JLabel lblPackegeToPrint;
    private javax.swing.JLabel lblPages;
    private javax.swing.JScrollPane scrPnPackage;
    private javax.swing.JScrollPane scrPnPages;
    private javax.swing.JTable tblPackage;
    private javax.swing.JTable tblPages;
    // End of variables declaration//GEN-END:variables
    
}
