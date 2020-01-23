/*
 * SubcontractFormsController.java
 *
 * Created on December 13, 2004, 5:20 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.subcontract.bean.SubContractBaseBean;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.subcontract.gui.SubcontractForms;
import edu.mit.coeus.subcontract.controller.SubcontractController;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import java.applet.AppletContext;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class SubcontractFormsController extends SubcontractController implements ActionListener{
    
    private SubcontractForms subcontractForms;
    private SubContractFormsTableModel subContractFormsTableModel;
    private CoeusMessageResources coeusMessageResources;
    private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private final char PRINT_RTF = 'R';
    private final char PRINT_PDF = 'P';
    private static final char GET_RTF_FORM_LIST = 'J';
    private static final char GET_RTF_FORM_VARIABLE = 'V';
    private static final int FORM_ID_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private CoeusVector cvTableData;
    //setting up the width and height of the screen
    private static final int WIDTH = 600;
    private static final int HEIGHT = 380;
    //Represents the mdiForm
    private CoeusAppletMDIForm mdiForm;
    private String subContractCode;
    private SubContractBaseBean subContractBaseBean;
    private QueryEngine queryEngine;
    private String queryKey;
    //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
    private String amtReleasedLineItemNumber;
    //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
    //Represents the dialog box
    private CoeusDlgWindow dlgSubContractForms;
    /** Creates a new instance of SubcontractFormsController */
    public SubcontractFormsController(SubContractBaseBean subContractBaseBean)  throws Exception{
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryKey = subContractBaseBean.getSubContractCode() + subContractBaseBean.getSequenceNumber();
        registerComponents();
        formatFields();
        setFormsData(null);
        setColumnData();
        postInitComponents();
        queryEngine = QueryEngine.getInstance();
        this.subContractBaseBean = subContractBaseBean;
        this.subContractCode = subContractBaseBean.getSubContractCode();
    }
    
    public void display() {
        dlgSubContractForms.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return subcontractForms;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        subcontractForms = new SubcontractForms();
        subContractFormsTableModel = new SubContractFormsTableModel();
        subcontractForms.tblSubcontractForms.setModel(subContractFormsTableModel);
        subcontractForms.btnOk.addActionListener(this);
        subcontractForms.btnCancel.addActionListener(this);
        
        Component[] component = {subcontractForms.btnOk,subcontractForms.btnCancel,subcontractForms.rdBtnRTF,subcontractForms.rdBtnPDF};
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        subcontractForms.setFocusTraversalPolicy(policy);
        subcontractForms.setFocusCycleRoot(true);
        
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) {
        
    }
    
    public void setFormsData(Object data)  throws Exception{
        
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_RTF_FORM_LIST);
        AppletServletCommunicator appletServletCommunicator = new
        AppletServletCommunicator(connect, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                cvTableData = (CoeusVector)responderBean.getDataObject();
                if (cvTableData != null && cvTableData.size() > 0) {
                    subContractFormsTableModel.setData(cvTableData);
                }
            }
        }
    }
    
    private void setColumnData() {
        JTableHeader tableHeader = subcontractForms.tblSubcontractForms.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        subcontractForms.tblSubcontractForms.setRowHeight(22);
        subcontractForms.tblSubcontractForms.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        subcontractForms.tblSubcontractForms.setOpaque(false);
        subcontractForms.tblSubcontractForms.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = subcontractForms.tblSubcontractForms.getColumnModel().getColumn(FORM_ID_COLUMN);
        
        column.setPreferredWidth(200);
        column.setResizable(true);
        
        column = subcontractForms.tblSubcontractForms.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setPreferredWidth(290);
        column.setResizable(true);
        
    }
    
    /**
     * Specifies the Modal window
     */
    private void postInitComponents() {
        mdiForm = CoeusGuiConstants.getMDIForm();
        dlgSubContractForms = new CoeusDlgWindow(mdiForm);
        dlgSubContractForms.getContentPane().add(subcontractForms);
        dlgSubContractForms.setTitle("Subcontract Forms");
        dlgSubContractForms.setFont(CoeusFontFactory.getLabelFont());
        dlgSubContractForms.setModal(true);
        dlgSubContractForms.setResizable(false);
        dlgSubContractForms.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSubContractForms.getSize();
        dlgSubContractForms.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgSubContractForms.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent ev){
                subcontractForms.btnCancel.requestFocusInWindow();
            }
        });
        dlgSubContractForms.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performCancelAction();
                return;
            }
        });
        dlgSubContractForms.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSubContractForms.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                performCancelAction();
                return;
            }
        });
        
        
    }
    
    /** This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","formId" },
            {"1","description" }
        };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvTableData != null && cvTableData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvTableData).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    subContractFormsTableModel.fireTableRowsUpdated(
                    0, subContractFormsTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    
    /**
     * This is an inner class represents the table model for the Award For Budget
     * screen table
     **/
    public class SubContractFormsTableModel extends AbstractTableModel {
        
        // represents the column names of the table
        private String colName[] = {"Form Id", "Description"};
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class};
        
        private CoeusVector cvTableData;
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            if (cvTableData == null){
                return 0;
            } else {
                return cvTableData.size();
            }
        }
        
        /**
         * To set the data for the model.
         * @param cvBudgetTableData CoeusVector
         */
        public void setData(CoeusVector cvTableData) {
            this.cvTableData = cvTableData;
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
            RTFFormBean rtfFormBean = (RTFFormBean)cvTableData.elementAt(rowIndex);
            
            if (rtfFormBean != null) {
                switch(columnIndex) {
                    
                    case FORM_ID_COLUMN:
                        return rtfFormBean.getFormId();
                    case DESCRIPTION_COLUMN:
                        return rtfFormBean.getDescription();
                }
            }
            return EMPTY_STRING;
        }
        
        /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         */
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
        
        /**
         * return true if cell is editable, else returns false
         * @return boolean
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
    
    public void cleanUp() {
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            dlgSubContractForms.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
            if (source.equals(subcontractForms.btnOk)) {
                performOKOperation();
            }
            if (source.equals(subcontractForms.btnCancel)) {
                performCancelAction();
            }
        }catch (Exception exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        finally{
            dlgSubContractForms.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    //    private void performOKOperation() throws Exception{
    //        int selectedRow = subcontractForms.tblSubcontractForms.getSelectedRow();
    //        if (selectedRow == -1) {
    //            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
    //            "subcontractPrint_exceptionCode.1401"));
    //        } else {
    //            RTFFormBean bean = (RTFFormBean)cvTableData.get(selectedRow);
    //            String formId = bean.getFormId();
    //            CoeusVector cvDataObject = new CoeusVector();
    //            cvDataObject.addElement(formId);
    //            cvDataObject.addElement(subContractCode);
    //            RequesterBean requesterBean = new RequesterBean();
    //            requesterBean.setFunctionType(GET_RTF_FORM_VARIABLE);
    //            requesterBean.setDataObject(cvDataObject);
    //            AppletServletCommunicator appletServletCommunicator = new
    //            AppletServletCommunicator(connect, requesterBean);
    //            appletServletCommunicator.send();
    //            ResponderBean responderBean = appletServletCommunicator.getResponse();
    //            if(responderBean != null) {
    //                if(responderBean.isSuccessfulResponse()) {
    //                    String fileNamePath = (String)responderBean.getDataObject();
    //                    String title = "Preview Form "+formId+" for Subcontract "+subContractCode;
    //                    SubcontractPrintBaseWindowController subcontractPrintBaseWindowController =
    //                    new SubcontractPrintBaseWindowController(title, subContractBaseBean,fileNamePath);
    //                    subcontractPrintBaseWindowController.display();
    //                    dlgSubContractForms.setVisible(false);
    //
    //                }else{
    //                    throw new Exception("Select proper template ");
    //                }
    //            }
    //        }
    //
    //    }
    
    
    private void performOKOperation() throws Exception{
        int selectedRow = subcontractForms.tblSubcontractForms.getSelectedRow();
        if (selectedRow == -1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "subcontractPrint_exceptionCode.1401"));
        } else {
            Hashtable subcontractData = new Hashtable();
            CoeusVector cvData = queryEngine.getDetails(queryKey,SubContractBean.class);
            subcontractData.put(SubContractBean.class,cvData);
            cvData = queryEngine.getDetails(queryKey, SubcontractContactDetailsBean.class);
            subcontractData.put(SubcontractContactDetailsBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey,SubcontractCloseoutBean.class);
            subcontractData.put(SubcontractCloseoutBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey, SubContractFundingSourceBean.class);
            subcontractData.put(SubContractFundingSourceBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey,SubContractAmountInfoBean.class);
            subcontractData.put(SubContractAmountInfoBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey,SubContractAmountReleased.class);
            subcontractData.put(SubContractAmountReleased.class, cvData);
            cvData = queryEngine.getDetails(queryKey,SubContractCustomDataBean.class);
            subcontractData.put(SubContractCustomDataBean.class, cvData);
            //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
            if(getAmtReleasedLineItemNumber() !=null){
                subcontractData.put("bpgLineItemNumber",getAmtReleasedLineItemNumber());
            }
            //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
            AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            RTFFormBean bean = (RTFFormBean)cvTableData.get(selectedRow);
            String formId = bean.getFormId();
            CoeusVector cvDataObject = new CoeusVector();
            cvDataObject.addElement(formId);
            cvDataObject.addElement(subContractCode);
            cvDataObject.addElement(subcontractData);
            char printMode='R';
            if(subcontractForms.rdBtnRTF.isSelected()){
                printMode = PRINT_RTF;
            }else if(subcontractForms.rdBtnPDF.isSelected()){
                printMode = PRINT_PDF;
            }
            cvDataObject.addElement(new Character(printMode));
            RequesterBean requesterBean = new RequesterBean();
            
//            requesterBean.setFunctionType(GET_RTF_FORM_VARIABLE);
//            requesterBean.setDataObject(cvDataObject);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", cvDataObject);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.subcontract.SubcontractDocumentReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            documentBean.setParameterMap(map);
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator appletServletCommunicator = new
            AppletServletCommunicator(STREMING_SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            if(printMode == PRINT_PDF) {
                if(responderBean != null) {
                    try{
//                        String url = (String)responderBean.getDataObject();
                        map = (Map)responderBean.getDataObject();
                        String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                        url = url.replace('\\', '/');
//                        coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
//                        
//                        URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//                        if(coeusContext != null){
//                            coeusContext.showDocument( templateURL, "_blank" );
//                        }else{
//                            javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                            bs.showDocument(templateURL);
//                        }
                        URL urlObj = new URL(url);
                        URLOpener.openUrl(urlObj);
                        //return true;
                    }catch (Exception ex) {
                        throw new Exception(responderBean.getMessage());
                        // return false;
                    }
                } else {
                    throw new Exception(responderBean.getMessage());
                }
            } else {
                if(responderBean != null) {
                    if(responderBean.isSuccessfulResponse()) {
//                        String url = (String)responderBean.getDataObject();
                        map = (Map)responderBean.getDataObject();
                        String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                        url = url.replace('\\', '/');
//                        URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//                        if(coeusContext != null){
//                            coeusContext.showDocument( templateURL, "_blank" );
//                        }else{
//                            javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                            bs.showDocument(templateURL);
//                        }
                        URL urlObj = new URL(url);
                        URLOpener.openUrl(urlObj);
                    }else{
                        //Bug Fix:1422
                        //throw new Exception("Select proper template ");
                        throw new Exception(responderBean.getMessage());
                        //Bug Fix:1422
                    }
                }
            }
            dlgSubContractForms.dispose();
        }
    }
    
    
    private void performCancelAction() {
        dlgSubContractForms.setVisible(false);
    }
    //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
    /**
     * Getter for property amtReleasedLineItemNumber.
     * @return Value of property amtReleasedLineItemNumber.
     */
    public java.lang.String getAmtReleasedLineItemNumber() {
        return amtReleasedLineItemNumber;
    }
    
    /**
     * Setter for property amtReleasedLineItemNumber.
     * @param amtReleasedLineItemNumber New value of property amtReleasedLineItemNumber.
     */
    public void setAmtReleasedLineItemNumber(java.lang.String amtReleasedLineItemNumber) {
        this.amtReleasedLineItemNumber = amtReleasedLineItemNumber;
    }
    //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
}
