/*
 * SelectAwardTemplateForm.java
 *
 * Created on December 10, 2004, 4:22 PM
 * @author  bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.admin.controller.AwardTemplateBaseWindowController;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
//case 1632 begin
import java.applet.AppletContext;
import java.net.URL;
import java.net.MalformedURLException;
//case 1632 end
import java.awt.Cursor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.HashSet;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class SelectAwardTemplateForm extends javax.swing.JPanel
implements ActionListener,MouseListener {
    private static final int CODE_COLUMN = 0;
    private static final int DESC_COLUMN = 1;
    private static final int STATUS_COLUMN = 2;
    private static final String EMPTY_STRING = "";
    //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
//    private static final int WIDTH = 582;
//    private static final int HEIGHT = 295;
    private static final int WIDTH = 850;
    private static final int HEIGHT = 400;
    //COEUSQA-1456 : End
    private CoeusVector cvTemplateData;
    private CoeusVector cvAllTemplateData;
    private TemplateTableModel templateTableModel;
    private CoeusDlgWindow dlgTemplate= null;
    private static final String DLG_TITLE = "Select Template";
    private static final char GET_TEMPLATES = 'G';
    private static final char DELETE_TEMPLATE = 'D';
    private CoeusAppletMDIForm mdiForm;
    private final String SERVLET ="/AwardTemplateMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ SERVLET;
    private CoeusMessageResources coeusMessageResources;
    private final String DELETE_CONFIRMATION_MESSAGE_KEY = "awardTemplateExceptionCode.1601";
    private final String DELETION_NOT_ALLOWED_MESSAGE_KEY = "awardTemplateExceptionCode.1602";
    private final String CLOSE_WINDOW_FOR_DELETE = "awardTemplateExceptionCode.1609";
    //case 1632 begin
    private static final char AWARD_TEMPLATE_REPORT = 'T';
    //private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    //case 1632 end
    
    private AwardTemplateBaseWindowController awardTemplateBaseWindowController;
    
    private boolean VIEW_RIGHTS;
    private boolean CREATE_RIGHTS;
    private boolean MODIFY_RIGHTS;
    private boolean DELETE_RIGHTS;
    boolean openedFromMenu;
//    boolean valuePresent;
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    private static final int LAST_UPDATED_COLUMN = 3;
    private static final int UPDATED_BY_COLUMN = 4;
    private static final int LAST_UPDATED_COLUMN_WIDTH = 135;
    private static final int UPDATED_BY_COLUMN_WIDTH = 135;
    //COEUSQA-1456 : End
    
    /** Creates new form SelectAwardTemplateForm */
    public SelectAwardTemplateForm(CoeusAppletMDIForm coeusAppletMDIForm,boolean openedFromMenu) {
        try {
            this.mdiForm = coeusAppletMDIForm;
            this.openedFromMenu = openedFromMenu;
            coeusMessageResources = CoeusMessageResources.getInstance();
            initComponents();
            registerComponents();
            setFormData();
            setColumnData();
            postInitComponents();
            formatFields();
        } catch (CoeusClientException cce) {
            cce.printStackTrace();
            CoeusOptionPane.showErrorDialog(cce.getMessage());
        }
    }
    
    /**
     * Sets the dialog properties
     */
    public void postInitComponents() {
        if (cvTemplateData.size() >0) {
            tblTemplates.setRowSelectionInterval(0,0);
            tblTemplates.setColumnSelectionInterval(0,0);
            tblTemplates.scrollRectToVisible(
            tblTemplates.getCellRect(0 ,0, true));
        }
        dlgTemplate = new CoeusDlgWindow(mdiForm);
        dlgTemplate.getContentPane().add(this);
        dlgTemplate.setTitle(DLG_TITLE);
        dlgTemplate.setFont(CoeusFontFactory.getLabelFont());
        dlgTemplate.setModal(true);
        dlgTemplate.setResizable(false);
        dlgTemplate.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgTemplate.getSize();
        dlgTemplate.setLocation(CoeusDlgWindow.CENTER);
        dlgTemplate.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgTemplate.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgTemplate.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgTemplate.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    public CoeusVector getAllTemplateData() {
        return this.cvAllTemplateData;
    }
    
    /**
     * Closes the window
     */
    private void performCancelAction() {
        dlgTemplate.dispose();
    }
    
    /**
     * Sets the focus to the default component
     */
    private void setWindowFocus(){
        tblTemplates.requestFocusInWindow();
    }
    
    /**
     * Adds the listeners for the components
     */
    public void registerComponents() {
        templateTableModel = new TemplateTableModel();
        tblTemplates.setModel(templateTableModel);
        tblTemplates.addMouseListener(this);
        btnClose.addActionListener(this);
        btnCopy.addActionListener(this);
        btnDelete.addActionListener(this);
        btnDisplay.addActionListener(this);
        btnModify.addActionListener(this);
        btnNew.addActionListener(this);
        btnShowAll.addActionListener(this);
        //case 1632 begin
        btnReport.addActionListener(this);
        //case 1632 end 
    }
    
    /**
     * Sets the data
     */
    public void setFormData() throws CoeusClientException {
        Hashtable htDataFromServer = getDataFromServer();
        if(htDataFromServer != null){
            cvAllTemplateData = (CoeusVector)htDataFromServer.get(AwardTemplateBean.class);
            filterData();
            cvTemplateData.sort("templateCode");
            templateTableModel.setData(cvTemplateData);
            VIEW_RIGHTS = ((Boolean) htDataFromServer.get(KeyConstants.VIEW_RIGHTS)).booleanValue();
            CREATE_RIGHTS = ((Boolean) htDataFromServer.get(KeyConstants.CREATE_RIGHTS)).booleanValue();
            MODIFY_RIGHTS = ((Boolean) htDataFromServer.get(KeyConstants.MODIFY_RIGHTS)).booleanValue();
            DELETE_RIGHTS = ((Boolean) htDataFromServer.get(KeyConstants.DELETE_RIGHTS)).booleanValue();
        }
    }
    
    private void formatFields(){
        
        btnDisplay.setEnabled(VIEW_RIGHTS);
        btnNew.setEnabled(CREATE_RIGHTS);
        btnModify.setEnabled(MODIFY_RIGHTS);
        btnDelete.setEnabled(DELETE_RIGHTS);
        btnCopy.setEnabled(CREATE_RIGHTS);
    }
    
    /**
     * Filters the data for status code != 2
     */
    private void filterData() {
        NotEquals notEqStatusCode = new NotEquals("statusCode",new Integer(2));
        cvTemplateData = cvAllTemplateData.filter(notEqStatusCode);
    }
    
    /**
     * Displays the dialog
     */
    public void display() {
        if (cvTemplateData.size()>0) {
            tblTemplates.setRowSelectionInterval(0,0);
            tblTemplates.scrollRectToVisible(
            tblTemplates.getCellRect(0 ,0, true));
        }
        dlgTemplate.setVisible(true);
    }
    /**
     * Sets the table column's properties
     */
    private void setColumnData(){
        JTableHeader tableHeader = tblTemplates.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblTemplates.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblTemplates.setRowHeight(22);
        tblTemplates.setShowHorizontalLines(true);
        tblTemplates.setShowVerticalLines(true);
        tblTemplates.setOpaque(true);
        tblTemplates.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        tblTemplates.setRowSelectionAllowed(true);
        TableColumn column = tblTemplates.getColumnModel().getColumn(CODE_COLUMN);
        column.setPreferredWidth(50);
        column.setResizable(true);
        column = tblTemplates.getColumnModel().getColumn(DESC_COLUMN);
        column.setPreferredWidth(320);
        column.setResizable(true);
        column = tblTemplates.getColumnModel().getColumn(STATUS_COLUMN);
        column.setPreferredWidth(90);
        column.setResizable(true);
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        column = tblTemplates.getColumnModel().getColumn(LAST_UPDATED_COLUMN);
        column.setPreferredWidth(LAST_UPDATED_COLUMN_WIDTH);
        column.setResizable(true);
        column = tblTemplates.getColumnModel().getColumn(UPDATED_BY_COLUMN);
        column.setPreferredWidth(UPDATED_BY_COLUMN_WIDTH);
        column.setResizable(true);
        //COEUSQA-1456 : End
    }
    
    /**
     * Class which handles the click of the header for the table.
     * It sorts the table data on the basis of the column heading clicked
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","templateCode" },
            {"1","description" },
            {"2","statusDescription"},
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start  
            {"3","updateTimestamp"},
            {"4","updateUserName"}
            //COEUSQA-1456 : End
        };
        boolean sort =true;
        
        public void mouseClicked(MouseEvent evt) {
            try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvTemplateData!=null && cvTemplateData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvTemplateData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    templateTableModel.fireTableRowsUpdated(0, templateTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener
    
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object actionSource = ae.getSource();
        try {
            dlgTemplate.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (actionSource.equals(btnClose) ) {
                performCancelAction();
            } else if (actionSource.equals(btnCopy) ) {
                showTemplate(TypeConstants.COPY_MODE);
            } else if (actionSource.equals(btnDelete) ) {
                int selectedIndex = tblTemplates.getSelectedRow();
                if (selectedIndex < 0 ) {
                    // Case# 3295: Award Templates Module wont open if all templates have been deleted 
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
                    return;
                }
                //                if(isValuePresent()){
                //                    CoeusOptionPane.showInfoDialog(
                //                    coeusMessageResources.parseMessageKey("Ajay Test"));
                //                    setValuePresent(false);
                //                    return ;
                //                }
                
                
                AwardTemplateBean bean = (AwardTemplateBean)cvTemplateData.get(selectedIndex);
                String templateDesc = bean.getDescription();
                
                HashSet templateSet = mdiForm.getHashSet();
                Integer templCode = new Integer(bean.getTemplateCode());
                
                if(templateSet != null && templateSet.contains(templCode)){
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(CLOSE_WINDOW_FOR_DELETE));
//                    setValuePresent(false);
                    return ;
                }
                
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(DELETE_CONFIRMATION_MESSAGE_KEY) + " "+ templateDesc,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    int templateCode = bean.getTemplateCode();
                    try {
                        deleteTemplate(templateCode,selectedIndex);
                    } catch (CoeusClientException cce) {
                        
                    }
                }
            } else if (actionSource.equals(btnDisplay) ) {
                showTemplate(TypeConstants.DISPLAY_MODE);
            } else if (actionSource.equals(btnModify) ) {
                showTemplate(TypeConstants.MODIFY_MODE);
            } else if (actionSource.equals(btnNew) ) {
                showTemplate(TypeConstants.NEW_MODE);
            } else if (actionSource.equals(btnShowAll) ) {
                //To change the text of the button as required
                if (btnShowAll.getText().equals("Show All")) {
                    cvTemplateData = cvAllTemplateData;
                    cvTemplateData.sort("templateCode");
                    templateTableModel.setData(cvTemplateData);
                    if (cvTemplateData.size() >0) {
                        tblTemplates.setRowSelectionInterval(0,0);
                        tblTemplates.setColumnSelectionInterval(0,0);
                        tblTemplates.scrollRectToVisible(
                        tblTemplates.getCellRect(0 ,0, true));
                    }
                    btnShowAll.setText("Filter");
                    btnShowAll.setMnemonic('F');
                } else {
                    filterData();
                    cvTemplateData.sort("templateCode");
                    templateTableModel.setData(cvTemplateData);
                    if (cvTemplateData.size() >0) {
                        tblTemplates.setRowSelectionInterval(0,0);
                        tblTemplates.setColumnSelectionInterval(0,0);
                        tblTemplates.scrollRectToVisible(
                        tblTemplates.getCellRect(0 ,0, true));
                    }
                    btnShowAll.setText("Show All");
                    btnShowAll.setMnemonic('S');
                }
            }//case 1632 begin
            else if (actionSource.equals(btnReport) ){
                try{
                    performReport();
                }catch(CoeusException coeusException){
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                    coeusException.printStackTrace();
                }
            }        
            //case 1632 end
        } finally {
            dlgTemplate.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * opens up the template window in the corresponding mode
     * @param mode - Mode in which the window has to be opened
     */
    private void showTemplate(char mode) {
        int selectedIndex = tblTemplates.getSelectedRow();
        // Case# 3295:Award Templates Module wont open if all templates have been deleted -Start
        AwardTemplateBean bean = null;
        String title="";
        AwardTemplateBean beanToSend = new AwardTemplateBean();
        if(selectedIndex == -1){
            if(mode == TypeConstants.MODIFY_MODE
                    || mode == TypeConstants.DISPLAY_MODE
                    || mode == TypeConstants.COPY_MODE){
                // Return if user tries to Copy, Display and Edit template and if the there are no Templates selected. 
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
                return;
            }
        } else {
            bean = (AwardTemplateBean) cvTemplateData.get(selectedIndex);
//            beanToSend = new AwardTemplateBean();
            beanToSend.setAcType(bean.getAcType());
            beanToSend.setTemplateCode(bean.getTemplateCode());
            //beanToSend.setBasisOfPaymentCode(bean.getBasisOfPaymentCode());
            title = bean.getDescription();
        }
         // Case# 3295:Award Templates Module wont open if all templates have been deleted - End                   
        if (openedFromMenu || (!isTemplateWindowOpen("",true))) {
            mdiForm.addToHashSet(new Integer(beanToSend.getTemplateCode()));
            awardTemplateBaseWindowController =
            new AwardTemplateBaseWindowController(title,mode,beanToSend,this);// changed from bean
        } else {
            Integer templateCode =
            new Integer(awardTemplateBaseWindowController.getTemplateCode());
            
            //           HashSet templateSet = mdiForm.getHashSet();
            //
            //            if(templateSet != null && templateSet.contains(templateCode)){
            //                setValuePresent(true);
            //            }else{
            mdiForm.deleteHashSetVal(templateCode);
            //            }
            
            int selIndex = tblTemplates.getSelectedRow();
            AwardTemplateBean templatebean = (AwardTemplateBean) cvTemplateData.get(selIndex);
            mdiForm.addToHashSet(new Integer(templatebean.getTemplateCode()));
            try {
                awardTemplateBaseWindowController.setTitle(title);
                awardTemplateBaseWindowController.setDisplayProperties(mode);
                awardTemplateBaseWindowController.setFunctionType(mode);
                awardTemplateBaseWindowController.setFormData(beanToSend);
                awardTemplateBaseWindowController.refresh();
            } catch (CoeusException ce) {
                ce.printStackTrace();
            }
        }
        awardTemplateBaseWindowController.display();
        dlgTemplate.setVisible(false);
    }
    
    /** This method is used to check whether the template window is already
     * opened or not.
     * @param refId refId - for institute proposal its proposal Number.
     * @param mode mode of Form open.
     * @param displayMessage if true displays error messages else doesn't.
     * @return true if proposal window is already open else returns false.
     */
    boolean isTemplateWindowOpen(String refId, boolean displayMessage) {
        boolean duplicate = false;
        //try{
        //duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.AWARD_TEMPLATE_BASE_WINDOW, "1", TypeConstants.DISPLAY_MODE);
        //}catch(Exception e){
        // Exception occured.  Record may be already opened in requested mode
        //   or if the requested mode is edit mode and application is already
        //   editing any other record.
        duplicate = true;
           /* if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }*/
        // try to get the requested frame which is already opened
        CoeusInternalFrame frame = mdiForm.getFrame(
        CoeusGuiConstants.AWARD_TEMPLATE_BASE_WINDOW);
        
        if (frame != null) {
               /* try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch (PropertyVetoException propertyVetoException) {
                
                }*/
            return true;
        }
        //return true;
        //}
        return false ;
    }
    
    
    
    /**
     * Gets the data from the server by making a server call
     */
    private Hashtable getDataFromServer() throws CoeusClientException {
        //        CoeusVector data = null;
        Hashtable htData = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_TEMPLATES);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if (responder!= null) {
            if(responder.isSuccessfulResponse()){
                htData = (Hashtable)responder.getDataObject();
            }
        }else{
            throw new CoeusClientException(responder.getMessage(),0);
        }
        return htData;
    }
    
    /**
     * Deletes the selected template from the database
     */
    private void deleteTemplate(int templateCode,int selctIndex) throws CoeusClientException {
        CoeusVector data = null;
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(new Integer(templateCode));
        requester.setFunctionType(DELETE_TEMPLATE);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean responder = comm.getResponse();
        if (responder!= null) {
            if(responder.isSuccessfulResponse()){
                Boolean boolDeleted = (Boolean)responder.getDataObject();
                if (boolDeleted.booleanValue()) {
                    cvTemplateData.remove(selctIndex);
                    templateTableModel.fireTableRowsDeleted(selctIndex, selctIndex);
                    Equals eqTemplateCode = new Equals("templateCode",new Integer(templateCode));
                    CoeusVector cv = cvAllTemplateData.filter(eqTemplateCode);
                    if (cv!=null && cv.size() > 0) {
                        AwardTemplateBean beanToDelete = (AwardTemplateBean)cv.get(0);
                        cvAllTemplateData.remove(beanToDelete);
                    }
                    if (cvTemplateData.size() >0) {
                        tblTemplates.setRowSelectionInterval(0,0);
                        tblTemplates.setColumnSelectionInterval(0,0);
                        tblTemplates.scrollRectToVisible(
                        tblTemplates.getCellRect(0 ,0, true));
                    }
                } else {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DELETION_NOT_ALLOWED_MESSAGE_KEY));
                }
            } else {
                throw new CoeusClientException(responder.getMessage(),0);
            }
        } else {
            throw new CoeusClientException(responder.getMessage(),0);
        }
    }
    
    public void mouseClicked(MouseEvent me) {
        if (me.getSource() == tblTemplates) {
            int selIndex = tblTemplates.getSelectedRow();
            if (selIndex < 0) {
                return;
            }
            if (me.getClickCount() == 2) {
                try {
                    dlgTemplate.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    showTemplate(TypeConstants.DISPLAY_MODE);
                } finally {
                    dlgTemplate.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }
    
    //case 1632 begin
    //add for creating awardTemplate report
    private String performReport()throws CoeusException{
        int selectedIndex = tblTemplates.getSelectedRow();
        // Case# 3295: Award Templates Module wont open if all templates have been deleted - Start
        if(selectedIndex == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            return EMPTY_STRING;
        }
        // Case# 3295: Award Templates Module wont open if all templates have been deleted - End
        AwardTemplateBean bean = (AwardTemplateBean) cvTemplateData.get(selectedIndex);
         Hashtable htPrintParams = new Hashtable(); 
         htPrintParams.put("TEMPLATE_CODE",""+bean.getTemplateCode());
         RequesterBean requester = new RequesterBean();
         requester.setFunctionType(AWARD_TEMPLATE_REPORT);
         requester.setDataObject(htPrintParams);
         
         //For Streaming
         requester.setId("Award/AwardTemplate");
         requester.setFunctionType('R');
         //For Streaming
         
         AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
         comm.send();
         ResponderBean responder = comm.getResponse();
         String fileName = "";
         if(responder.isSuccessfulResponse()){
//             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
           
             fileName = (String)responder.getDataObject();
//             System.out.println("Report Filename is=>"+fileName);
//             
//             fileName.replace('\\', '/') ; // this is fix for Mac
//             URL reportUrl = null;
//             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
//             }catch(MalformedURLException muEx){
//                 throw new CoeusException(muEx.getMessage());
//             }catch(Exception uaEx){
//                 throw new CoeusException(uaEx.getMessage());
//             }
             try{
                 URL url = new URL(fileName);
                 URLOpener.openUrl(url);
             }catch (MalformedURLException malformedURLException) {
                 throw new CoeusException(malformedURLException.getMessage());
             }
         }else{
             throw new CoeusException(responder.getMessage());
         }
         return fileName;
    }   
    //case 1632 end
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    /**
     * Table model class
     */
    public class TemplateTableModel extends AbstractTableModel {
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
//        private String[] colName = {"Code","Description","Status"};
//        private Class[] colClass = {String.class,String.class,String.class};
        private String colName[] = {"Code", "Description", "Status", "Last Updated", "Updated By"};
        private Class[] colClass = {String.class,String.class,String.class,String.class,String.class};
        //COEUSQA-1456 : End
        CoeusVector cvData= null;
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount() {
            return colName.length;
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public void setData(CoeusVector cvTemplateData){
            cvData = cvTemplateData;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvData==null){
                return 0;
            }else{
                return cvData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            AwardTemplateBean awardTemplateBean= (AwardTemplateBean)cvData.get(row);
            switch(col){
                case CODE_COLUMN:
                    int iCode = awardTemplateBean.getTemplateCode();
                    return ""+iCode;
                case DESC_COLUMN:
                    return awardTemplateBean.getDescription();
                case STATUS_COLUMN:
                    return awardTemplateBean.getStatusDescription();
                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                case LAST_UPDATED_COLUMN:
                     return CoeusDateFormat.format(awardTemplateBean.getUpdateTimestamp().toString());
                case UPDATED_BY_COLUMN:
                     return awardTemplateBean.getUpdateUserName();
                //COEUSQA-1456 : End
            }
            return EMPTY_STRING;
        }
    }// end of table model class
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTemplate = new javax.swing.JPanel();
        scrpnTemplates = new javax.swing.JScrollPane();
        tblTemplates = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnCopy = new javax.swing.JButton();
        btnDisplay = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnShowAll = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlTemplate.setLayout(new java.awt.GridBagLayout());

        pnlTemplate.setMinimumSize(new java.awt.Dimension(355, 255));
        scrpnTemplates.setMinimumSize(new java.awt.Dimension(350, 250));
        scrpnTemplates.setPreferredSize(new java.awt.Dimension(750, 350));
        tblTemplates.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrpnTemplates.setViewportView(tblTemplates);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        pnlTemplate.add(scrpnTemplates, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlTemplate, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMinimumSize(new java.awt.Dimension(83, 255));
        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnClose, gridBagConstraints);

        btnNew.setFont(CoeusFontFactory.getLabelFont());
        btnNew.setMnemonic('N');
        btnNew.setText("New");
        btnNew.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnNew, gridBagConstraints);

        btnCopy.setFont(CoeusFontFactory.getLabelFont());
        btnCopy.setMnemonic('y');
        btnCopy.setText("Copy");
        btnCopy.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnCopy, gridBagConstraints);

        btnDisplay.setFont(CoeusFontFactory.getLabelFont());
        btnDisplay.setMnemonic('D');
        btnDisplay.setText("Display");
        btnDisplay.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnDisplay, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        btnModify.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnModify, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('l');
        btnDelete.setText("Delete");
        btnDelete.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnDelete, gridBagConstraints);

        btnShowAll.setFont(CoeusFontFactory.getLabelFont());
        btnShowAll.setMnemonic('S');
        btnShowAll.setText("Show All");
        btnShowAll.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnShowAll, gridBagConstraints);

        btnReport.setFont(CoeusFontFactory.getLabelFont());
        btnReport.setMnemonic('P');
        btnReport.setLabel("Print");
        btnReport.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        pnlButtons.add(btnReport, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pnlButtons, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Getter for property openedFromMenu.
     * @return Value of property openedFromMenu.
     */
    public boolean isOpenedFromMenu() {
        return openedFromMenu;
    }
    
    /**
     * Setter for property openedFromMenu.
     * @param openedFromMenu New value of property openedFromMenu.
     */
    public void setOpenedFromMenu(boolean openedFromMenu) {
        this.openedFromMenu = openedFromMenu;
    }
    
    /**
     * Getter for property valuePresent.
     * @return Value of property valuePresent.
     */
//    public boolean isValuePresent() {
//        return valuePresent;
//    }
    
    /**
     * Setter for property valuePresent.
     * @param valuePresent New value of property valuePresent.
     */
//    public void setValuePresent(boolean valuePresent) {
//        this.valuePresent = valuePresent;
//    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnCopy;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnDisplay;
    public javax.swing.JButton btnModify;
    public javax.swing.JButton btnNew;
    public javax.swing.JButton btnReport;
    public javax.swing.JButton btnShowAll;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlTemplate;
    public javax.swing.JScrollPane scrpnTemplates;
    public javax.swing.JTable tblTemplates;
    // End of variables declaration//GEN-END:variables
    
}
