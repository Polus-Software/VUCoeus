/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RoutingDocumentForm.java
 *
 * Created on Dec 13, 2011, 5:47:16 PM
 */
package edu.mit.coeus.award.gui;


import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardDocumentRouteBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.routing.gui.RoutingForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author midhunmk
 */
public class RoutingDocumentForm extends javax.swing.JPanel implements ActionListener {
    private static final String SERVLET = "/AwardMaintenanceServlet";
    private static final String DOCUMENT_SERVER="/AwardSubmissionServlet";
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final char GET_AWARD_DOC_DETAILS='s';
    private CoeusVector cvData;
    private CoeusDlgWindow dlgRouteHistory;
    private static final String DLG_TITLE="Document Route History";
    private static final int DOCUMENT_NUMBER = 0;
    private static final int DOCUMENT_TYPE =1;
    private static final int SEQUENCE_NUMBER = 2;
    private static final int ROUTING_STATUS =3;
    private static final int ROUTE_START_DATE = 4;
    private static final int ROUTE_END_DATE =5;
    private static final String EMPTY_STRING="";
    private RouteDocumentTableModel routeDocumentTableModel;
    private AwardBean awardBean;
    private AwardDocumentRouteBean curSelectedBean;
    private static final char GET_AWARD_DOCUMENT='E';
    private static final char GET_AWD_DOC_DETAILS='H';
   

    /** Creates new form RoutingDocumentForm */
    public RoutingDocumentForm(AwardBean awardBeanTmp) {
        
        awardBean= awardBeanTmp;
        initComponents();
        getFormData();
        registerComponents();
        //action listeners
        btnClose.addActionListener(this);
        btnShowRouting.addActionListener(this);
        btnViewDocument.addActionListener(this);
    }
public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object source =ae.getSource();
        if(source.equals(btnClose)) {
        //close the form
            dlgRouteHistory.dispose();
        }
        else if(source.equals(btnShowRouting)){
        //showing the present routing situation
            int selectedRow=tblRouteDocuments.getSelectedRow();
            if(selectedRow>=0){
            curSelectedBean=(AwardDocumentRouteBean)cvData.get(selectedRow);
            getAwardDocumentRouteBean();
            RoutingForm routingForm = new RoutingForm(
                                    curSelectedBean,
                                    ModuleConstants.AWARD_MODULE_CODE,curSelectedBean.getMitAwardNumber(),
                                    curSelectedBean.getRoutingDocumentNumber(), awardBean.getLeadUnitNumber(), false);
            if(!routingForm.isMapsNotFound()){
            routingForm.display();}
            //if some thing happen, the list need to be updated.
            getFormData();
            registerComponents();
        }
            else
            {CoeusOptionPane.showErrorDialog("Please select a document.");}
        }
        else if(source.equals(btnViewDocument)){
            //load the document
            int selectedRow=tblRouteDocuments.getSelectedRow();
            if(selectedRow>=0){
                curSelectedBean=(AwardDocumentRouteBean)cvData.get(selectedRow);
                ShowDocument();
            }else{CoeusOptionPane.showErrorDialog("Please select a document.");}
            
        }
}
public void registerComponents() {
        //loading the model
        routeDocumentTableModel=new RouteDocumentTableModel();
        tblRouteDocuments.setModel(routeDocumentTableModel);
            JTableHeader tableHeader = tblRouteDocuments.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
           // tableHeader.setMaximumSize(new Dimension(120,22));
           // tableHeader.setMinimumSize(new Dimension(120,22));
           // tableHeader.setPreferredSize(new Dimension(120,22));
            tblRouteDocuments.setRowHeight(22);
            //tblRouteDocuments.setSelectionBackground(java.awt.Color.MAGENTA);
            tblRouteDocuments.setSelectionForeground(java.awt.Color.white);
            tblRouteDocuments.setShowHorizontalLines(true);
            tblRouteDocuments.setShowVerticalLines(true);
            tblRouteDocuments.setOpaque(false);
            tblRouteDocuments.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
            
            //seting the column size
            TableColumn tblColumn= tblRouteDocuments.getColumnModel().getColumn(DOCUMENT_NUMBER);
            tblColumn.setMaxWidth(60);
            tblColumn.setMinWidth(60);
            tblColumn.setPreferredWidth(60);
            tblColumn.setResizable(false);
            
            tblColumn= tblRouteDocuments.getColumnModel().getColumn(DOCUMENT_TYPE);
            tblColumn.setMaxWidth(120);
            tblColumn.setMinWidth(120);
            tblColumn.setPreferredWidth(120);
            tblColumn.setResizable(false);
            
            tblColumn= tblRouteDocuments.getColumnModel().getColumn(SEQUENCE_NUMBER);
            tblColumn.setMaxWidth(60);
            tblColumn.setMinWidth(60);
            tblColumn.setPreferredWidth(60);
            tblColumn.setResizable(false);
            
            tblColumn= tblRouteDocuments.getColumnModel().getColumn(ROUTING_STATUS);
            tblColumn.setMaxWidth(120);
            tblColumn.setMinWidth(120);
            tblColumn.setPreferredWidth(120);
            tblColumn.setResizable(false);
            
            tblColumn= tblRouteDocuments.getColumnModel().getColumn(ROUTE_START_DATE);
            tblColumn.setMaxWidth(120);
            tblColumn.setMinWidth(120);
            tblColumn.setPreferredWidth(120);
            tblColumn.setResizable(false);
            
            tblColumn= tblRouteDocuments.getColumnModel().getColumn(ROUTE_END_DATE);
            tblColumn.setMaxWidth(120);
            tblColumn.setMinWidth(120);
            tblColumn.setPreferredWidth(120);
            tblColumn.setResizable(false);
           
            
        routeDocumentTableModel.setData();
       
        if((cvData==null)||(cvData.size()==0)){
        btnShowRouting.setEnabled(false);
        btnViewDocument.setEnabled(false);
        }
            
        
}
private boolean  getFormData(){
    RequesterBean requesterBean = new RequesterBean();
    requesterBean.setFunctionType(GET_AWARD_DOC_DETAILS);
    requesterBean.setDataObject(awardBean.getMitAwardNumber());
    AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
    appletServletCommunicator.setRequest(requesterBean);
     appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER); 
           return  false;}
       if((responderBean!=null)&&(!responderBean.isSuccessfulResponse()))   {
        CoeusOptionPane.showErrorDialog("Server Error...");
        return false;
        }
       cvData=(CoeusVector)responderBean.getDataObject();
       return true;
}
private void ShowDocument(){
RequesterBean requesterBean = new RequesterBean();
    requesterBean.setFunctionType(GET_AWARD_DOCUMENT);
    Vector dataToServer=new Vector();
    dataToServer.add(curSelectedBean.getMitAwardNumber());
    dataToServer.add(String.valueOf(curSelectedBean.getRoutingDocumentNumber()));
    requesterBean.setDataObject(dataToServer);
    AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + DOCUMENT_SERVER, requesterBean);
    appletServletCommunicator.setRequest(requesterBean);
     appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER); 
        }
        else if(!responderBean.isSuccessfulResponse())   {
        CoeusOptionPane.showErrorDialog("Server Error...");
        }
        else{
            //document is loaded in mentioned url.
            String templateURL=(String)responderBean.getDataObject();
            templateURL = templateURL.replace('\\', '/');
        
        try{
            URL urlObj = new URL(templateURL);
            URLOpener.openUrl(urlObj);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+ex.getMessage());}
        }
       
}
        
public void display() {
        
        dlgRouteHistory = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgRouteHistory.setTitle(DLG_TITLE);
        dlgRouteHistory.getContentPane().add(this);
        dlgRouteHistory.setSize(800, 330);
        dlgRouteHistory.setResizable(false);
        dlgRouteHistory.setLocation(CoeusDlgWindow.CENTER);
        dlgRouteHistory.setVisible(true);
    }

//COEUSQA 2111 STARTS
    private void getAwardDocumentRouteBean(){
        int approvalSeq=curSelectedBean.getRoutingApprovalSeq();
        if(approvalSeq>1){            
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_AWD_DOC_DETAILS);
        requester.setDataObject(curSelectedBean);
        try{
            String connectTo =CoeusGuiConstants.CONNECTION_URL+DOCUMENT_SERVER;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            if((res!=null)&&res.isSuccessfulResponse()){
               curSelectedBean=(AwardDocumentRouteBean)res.getDataObject();
            }

        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        }        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnRoutingDocument = new javax.swing.JScrollPane();
        tblRouteDocuments = new javax.swing.JTable();
        jpanBtn = new javax.swing.JPanel();
        btnShowRouting = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnViewDocument = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(850, 330));
        setMinimumSize(new java.awt.Dimension(850, 330));
        setPreferredSize(new java.awt.Dimension(850, 330));
        setLayout(new java.awt.GridBagLayout());

        scrPnRoutingDocument.setMaximumSize(new java.awt.Dimension(650, 300));
        scrPnRoutingDocument.setMinimumSize(new java.awt.Dimension(650, 300));
        scrPnRoutingDocument.setPreferredSize(new java.awt.Dimension(650, 300));

        tblRouteDocuments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnRoutingDocument.setViewportView(tblRouteDocuments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 22, 0);
        add(scrPnRoutingDocument, gridBagConstraints);

        jpanBtn.setLayout(new java.awt.GridBagLayout());

        btnShowRouting.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnShowRouting.setText("Show Routing");
        btnShowRouting.setToolTipText("To show the routing details of selected row");
        btnShowRouting.setMaximumSize(new java.awt.Dimension(121, 23));
        btnShowRouting.setMinimumSize(new java.awt.Dimension(121, 23));
        btnShowRouting.setPreferredSize(new java.awt.Dimension(121, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 10);
        jpanBtn.add(btnShowRouting, gridBagConstraints);

        btnClose.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnClose.setText("Close");
        btnClose.setToolTipText("close the window");
        btnClose.setMaximumSize(new java.awt.Dimension(121, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(121, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(121, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 27, 10);
        jpanBtn.add(btnClose, gridBagConstraints);

        btnViewDocument.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnViewDocument.setText("View Document");
        btnViewDocument.setToolTipText("To show the routing details of selected row");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 10);
        jpanBtn.add(btnViewDocument, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 22, 10);
        add(jpanBtn, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnShowRouting;
    private javax.swing.JButton btnViewDocument;
    private javax.swing.JPanel jpanBtn;
    private javax.swing.JScrollPane scrPnRoutingDocument;
    private javax.swing.JTable tblRouteDocuments;
    // End of variables declaration//GEN-END:variables

        public class RouteDocumentTableModel extends AbstractTableModel{
        
        private String colName[] = {"Doc #", "Document Type", "Seq #", "Routing Status","Routing Start Date","Routing End Date"};
        private Class colClass[] = {String.class, String.class,String.class,String.class,String.class,String.class};
        
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
        
        public void setData(){
            
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvData== null){
                return 0;
            }else{
                return cvData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            AwardDocumentRouteBean awardDocumentRouteBean = (AwardDocumentRouteBean)cvData.get(row);
            switch(col){
                case SEQUENCE_NUMBER:
                    return (awardDocumentRouteBean.getSequenceNumber());
                case DOCUMENT_NUMBER:
                    return (awardDocumentRouteBean.getRoutingDocumentNumber());
                case ROUTING_STATUS:
                    return awardDocumentRouteBean.getRoutingStatusDesc();
                case DOCUMENT_TYPE:
                    return awardDocumentRouteBean.getDocumentTypeDesc();
                case ROUTE_START_DATE:
                    return awardDocumentRouteBean.getRouteStartDate();
                case ROUTE_END_DATE:
                    return awardDocumentRouteBean.getRouteEndDate();
                
            }
            return EMPTY_STRING;
            
        }
    }
}
