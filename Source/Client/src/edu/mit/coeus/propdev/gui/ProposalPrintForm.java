/*
 * ProposalPrintForm.java
 *
 * Created on August 18, 2004, 3:02 PM
 */

package edu.mit.coeus.propdev.gui;


import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.sponsormaint.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import java.applet.AppletContext;

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
 * @author  geot
 */
public class ProposalPrintForm extends javax.swing.JComponent implements ListSelectionListener, ActionListener{
    
    private static final int PACKAGE_NO = 0;
    private static final int PACKAGE_DESC = 1;
    private static final String EMPTY_STRING = "";
    
    private static final int PAGE_NUMBER_COLUMN = 0;
    private static final int PAGE_DESC_COLUMN = 1;
    private static final int PAGE_UPDATE_COLUMN = 2;
    private static final int PAGE_USER_COLUMN =3;
    
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    
    private CoeusDlgWindow dlgSponsorMaintainance;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
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
    private PackageTableCellEditor packageTableCellEditor;
    private PageTableCellEditor pageTableCellEditor;
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
  
    
    /** Creates new form ProposalPrintForm */
    public ProposalPrintForm(ProposalDevelopmentFormBean proposalBean) {
        this.proposalBean = proposalBean;
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
        
        packageTableCellEditor = new PackageTableCellEditor();
        pageTableCellEditor = new PageTableCellEditor();
        packageRenderer = new PackageRenderer();
        pageRenderer = new PageRenderer();
        
        tblPackage.setModel(packageTableModel);
        tblPages.setModel(pageTableModel);
        
        
        tblPackage.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPages.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPackage.getSelectionModel().addListSelectionListener(this);
        
        btnClose.addActionListener(this);
        btnPrint.addActionListener(this);

        
        java.awt.Component[] components = {
            btnPrint,btnClose
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
        
    }
    
    private void setTableEditors(){
        
        JTableHeader tablePackageHeader = tblPackage.getTableHeader();
        tablePackageHeader.setReorderingAllowed(false);
        tablePackageHeader.setFont(CoeusFontFactory.getLabelFont());
        tblPackage.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPackage.setRowHeight(22);
        tblPackage.setSelectionBackground(java.awt.Color.white);
        tblPackage.setSelectionForeground(java.awt.Color.black);
        tblPackage.setShowHorizontalLines(true);
        tblPackage.setShowVerticalLines(true);
        tblPackage.setOpaque(false);
        tblPackage.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        JTableHeader tblPageHeader = tblPages.getTableHeader();
        tblPageHeader.setReorderingAllowed(false);
        tblPageHeader.setFont(CoeusFontFactory.getLabelFont());
        tblPages.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPages.setRowHeight(22);
        tblPages.setSelectionBackground(java.awt.Color.white);
        tblPages.setSelectionForeground(java.awt.Color.black);
        tblPages.setShowHorizontalLines(false);
        tblPages.setShowVerticalLines(false);
        tblPages.setOpaque(false);
        tblPages.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        
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
        packageColumn.setCellEditor(packageTableCellEditor);
        
        
        TableColumn pageColumn = tblPages.getColumnModel().getColumn(0);
        pageColumn.setPreferredWidth(37);
        pageColumn.setCellEditor(pageTableCellEditor);
        pageColumn.setCellRenderer(pageRenderer);
        pageColumn.setResizable(true);
        
        pageColumn= tblPages.getColumnModel().getColumn(1);
        pageColumn.setPreferredWidth(200);
        pageColumn.setCellEditor(pageTableCellEditor);
        pageColumn.setCellRenderer(pageRenderer);
        pageColumn.setResizable(true);
    }
    
    public void setFormData(){
        lblSponsorCode.setText("Sponsor Code:  " +
                                this.proposalBean.getSponsorCode()+
                                " "+this.proposalBean.getSponsorName());
        
        cvPackageData= new CoeusVector();
        cvPageData = new CoeusVector();
        cvDeletedPackageData = new CoeusVector();
        cvDeletedPageData = new CoeusVector();
        
        Hashtable htSponsorData = getPckagePageData(proposalBean);
        cvPackageData = (CoeusVector) htSponsorData.get(KeyConstants.PACKAGE_DATA);
        cvPackageData.sort("packageNumber",false);
        cvPageData = (CoeusVector)htSponsorData.get(KeyConstants.PAGE_DATA);
        packageTableModel.setData(cvPackageData);
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
        if(source.equals(btnPrint)){
             performPrintAction();
        }else if(source.equals(btnClose)){
             closeWindow();
        }
        }catch(Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    /**
     *  Code to go for printing
     */
    private String performPrintAction() throws Exception{
        if(tblPackage.getRowCount()>0){
            packageTableCellEditor.stopCellEditing();
        }
        
        if(tblPages.getRowCount()>0){
            pageTableCellEditor.stopCellEditing();
        }
         String printServletURL = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
         RequesterBean requester;
         ResponderBean responder;

         Hashtable htPrintParams = new Hashtable();
         htPrintParams.put("PROPOSAL_NUMBER", this.proposalBean.getProposalNumber());
         int sltdRow = tblPackage.getSelectedRow();
         
         htPrintParams.put("SPONSOR_CODE", ((SponsorFormsBean)
                      ((PackageTableModel)tblPackage.getModel()).getValueAt(
                                    sltdRow)).getSponsorCode());
         Integer packageNumber = (Integer)tblPackage.getValueAt(sltdRow, 0);
         htPrintParams.put("PACKAGE_NUMBER", packageNumber.toString());
         Integer pageNumber = (Integer)tblPages.getValueAt(tblPages.getSelectedRow(), 0);
         htPrintParams.put("PAGE_NUMBER", pageNumber.toString());
         requester = new RequesterBean();
         requester.setFunctionType(PRINT_PROPOSAL);
         requester.setDataObject(htPrintParams);
         
         AppletServletCommunicator comm
         = new AppletServletCommunicator(printServletURL, requester);
         
         comm.send();
         responder = comm.getResponse();
         String fileName = "";
         if(responder.isSuccessfulResponse()){
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
         }
         return fileName;
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
        
        public void setValueAt(Object value, int row, int col){
            SponsorFormsBean sponsorFormsBean = (SponsorFormsBean)cvPackageData.get(row);
            switch(col){
                case PACKAGE_DESC:
                    if(sponsorFormsBean.getPackageName().equals(value.toString())){
                        return ;
                    }
                    sponsorFormsBean.setPackageName(value.toString().trim());
                    modified = true;
                    break;
            }
            
            if(sponsorFormsBean.getAcType()==null){
                sponsorFormsBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            
            this.fireTableDataChanged();
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
            //cvFilterVector = pageData;
            this.data = pageData;
            //fireTableDataChanged();
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
        
        public void setValueAt(Object value, int row, int col){
            SponsorTemplateBean sponsorTemplateBean = (SponsorTemplateBean)this.data.get(row);
            switch(col){
                case 1:
                    if(sponsorTemplateBean.getPageDescription().equals(value.toString())){
                        return ;
                    }
                    sponsorTemplateBean.setPageDescription(value.toString().trim());
                    modified = true;
                    break;
            }
            if(sponsorTemplateBean.getAcType()==null){
                sponsorTemplateBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            this.fireTableDataChanged();
        }
    }
    
    /** Editor class for the sponsor package. This editor provides to edit the data in
     *the Package coolumn
     */
    public class PackageTableCellEditor extends AbstractCellEditor implements TableCellEditor{
        
        private CoeusTextField txtPackage;
        private int column;
        
        public PackageTableCellEditor(){
            txtPackage = new CoeusTextField();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case PACKAGE_NO:
                    txtPackage.setText(value.toString());
                    return txtPackage;
                case PACKAGE_DESC:
                    if (isSelected) {
                        txtPackage.setBackground(java.awt.Color.yellow);
                    }else{
                        txtPackage.setBackground(java.awt.Color.white);
                    }
                    txtPackage.setDocument(new LimitedPlainDocument(200));
                    txtPackage.setText(value.toString());
                    return txtPackage;
            }
            return txtPackage;
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case PACKAGE_NO:
                    return txtPackage.getText();
                case PACKAGE_DESC:
                    return txtPackage.getText();
            }
            return txtPackage;
        }
        
    }
    
    
    
    /** Editor class for the sponsor pages. This editor provides to edit the data in
     *the Package coolumn
     */
    public class PageTableCellEditor extends AbstractCellEditor implements TableCellEditor{
        
        private CoeusTextField txtPage;
        private int column;
        
        public PageTableCellEditor(){
            txtPage = new CoeusTextField();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case PAGE_NUMBER_COLUMN:
                    if(value!= null || (!value.toString().equals(EMPTY_STRING))) {
                        txtPage.setText(value.toString());
                        return txtPage;
                    }
                case PAGE_DESC_COLUMN:
                    if(value!= null || !(value.toString().equals(EMPTY_STRING))) {
                        txtPage.setDocument(new LimitedPlainDocument(200));
                        txtPage.setText(value.toString());
                        return txtPage;
                    }
            }
            return txtPage;
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case PAGE_NUMBER_COLUMN:
                    return txtPage.getText();
                case PAGE_DESC_COLUMN:
                    return txtPage.getText();
            }
            return txtPage;
        }
    }
    
    
    public class PackageRenderer extends DefaultTableCellRenderer{
        CoeusTextField txtComponent;
        
        public PackageRenderer(){
            txtComponent = new CoeusTextField();
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int col){
            
            if(col==PACKAGE_DESC){
                if (isSelected) {
                    txtComponent.setBackground(java.awt.Color.yellow);
                }else {
                    txtComponent.setBackground(java.awt.Color.white);
                }
                
                if(value!= null || !(value.toString().trim().equals(EMPTY_STRING))){
                    txtComponent.setText(value.toString());
                }else{
                    txtComponent.setText(null);
                }
            }
            return txtComponent;
        }
        
    }
    
    public class PageRenderer extends DefaultTableCellRenderer{
        CoeusTextField txtComponent;
        
        public PageRenderer(){
            txtComponent = new CoeusTextField();
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int col){
            switch(col){
                case PAGE_NUMBER_COLUMN:
                    if (isSelected) {
                        txtComponent.setBackground(java.awt.Color.yellow);
                    }else {
                        txtComponent.setBackground(java.awt.Color.white);
                    }
                    
                    if(value!= null && !(value.toString().trim().equals(EMPTY_STRING))){
                        txtComponent.setText(value.toString());
                    }else{
                        txtComponent.setText(null);
                    }
                    return txtComponent;
                case PAGE_DESC_COLUMN:
                    if (isSelected) {
                        txtComponent.setBackground(java.awt.Color.yellow);
                    }else {
                        txtComponent.setBackground(java.awt.Color.white);
                    }
                    
                    if(value!= null && !(value.toString().trim().equals(EMPTY_STRING))){
                        txtComponent.setText(value.toString());
                    }else{
                        txtComponent.setText(null);
                    }
                    return txtComponent;
            }
            return txtComponent;
        }
    }
    
    
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if(tblPackage.getRowCount()>0){
            packageTableCellEditor.stopCellEditing();
        }
        if(tblPages.getRowCount()>0){
            pageTableCellEditor.stopCellEditing();
        }
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
            }
            // }
        }
    }
    
    
    private void filterData(int selRow){
        SponsorFormsBean  sponsorFormsBean= (SponsorFormsBean)cvPackageData.get(selRow);
        packageTableCellEditor.stopCellEditing();
        sponsorFormsBean= (SponsorFormsBean)cvPackageData.get(selRow);
        int packageNumber = sponsorFormsBean.getPackageNumber();
        Equals eqFilter = new Equals("packageNumber",new Integer(packageNumber));
        Equals eqSpCodeFilter = new Equals("sponsorCode",sponsorFormsBean.getSponsorCode());
        And pckNumAndSpCode = new And(eqFilter,eqSpCodeFilter);
        
        cvFilterVector = cvPageData.filter(pckNumAndSpCode);
        
        packageTableCellEditor.stopCellEditing();
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
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnPackage = new javax.swing.JScrollPane();
        tblPackage = new javax.swing.JTable();
        scrPnPages = new javax.swing.JScrollPane();
        tblPages = new javax.swing.JTable();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblSponsorCode = new javax.swing.JLabel();
        lblPackage = new javax.swing.JLabel();
        lblPages = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        scrPnPackage.setBorder(new javax.swing.border.EtchedBorder());
        scrPnPackage.setMinimumSize(new java.awt.Dimension(470, 150));
        tblPackage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnPackage.setViewportView(tblPackage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(scrPnPackage, gridBagConstraints);

        scrPnPages.setBorder(new javax.swing.border.EtchedBorder());
        scrPnPages.setMinimumSize(new java.awt.Dimension(470, 150));
        tblPages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnPages.setViewportView(tblPages);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(scrPnPages, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setMnemonic('O');
        btnPrint.setText("Print");
        btnPrint.setMinimumSize(new java.awt.Dimension(90, 26));
        btnPrint.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnPrint, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setText("Close");
        btnClose.setMinimumSize(new java.awt.Dimension(90, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnClose, gridBagConstraints);

        lblSponsorCode.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorCode.setText("Sponsor Code:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(lblSponsorCode, gridBagConstraints);

        lblPackage.setFont(CoeusFontFactory.getLabelFont());
        lblPackage.setForeground(new java.awt.Color(51, 51, 255));
        lblPackage.setText("Package(s)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPackage, gridBagConstraints);

        lblPages.setFont(CoeusFontFactory.getLabelFont());
        lblPages.setForeground(new java.awt.Color(0, 0, 255));
        lblPages.setText("Page(s)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(lblPages, gridBagConstraints);

    }//GEN-END:initComponents

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrint;
    private javax.swing.JLabel lblPackage;
    private javax.swing.JLabel lblPages;
    private javax.swing.JLabel lblSponsorCode;
    private javax.swing.JScrollPane scrPnPackage;
    private javax.swing.JScrollPane scrPnPages;
    private javax.swing.JTable tblPackage;
    private javax.swing.JTable tblPages;
    // End of variables declaration//GEN-END:variables
    
}
