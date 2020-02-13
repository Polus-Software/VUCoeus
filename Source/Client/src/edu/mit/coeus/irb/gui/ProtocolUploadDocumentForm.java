/*
 * @(#)ProtocolUploadDocumentForm.java  8/28/06
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 21-DEC-2010
 * by Bharati Umarani
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusDateFormat;
//import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.ComboBoxBean;
//import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
//import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeus.utils.UserUtils;
//import java.applet.AppletContext;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
//import javax.swing.AbstractAction;
//import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
//import javax.swing.JButton;
import javax.swing.JTable;
//import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Class for Upload Document for Protocol
 * @author tarique
 */
public class ProtocolUploadDocumentForm extends javax.swing.JComponent
        implements ActionListener, MouseListener, ListSelectionListener {
    //Commented for case 3552 - IRB Attachments - start
    // Modified for Coeus 4.3 enhancement - starts
    //private static final int WIDTH = 780;
//    private static final int WIDTH = 800;
//    private static final int HEIGHT = 560;
    // Modified for Coeus 4.3 enhancement - ends
    //Commented for case 3552 - IRB Attachments - end
    private static final String EMPTY_STRING = "";
    private static final int TYPE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    //Commented for Coeus 4.3 enhancement
    //Status column is removed
    //private static final int STATUS_COLUMN = 2;
    private static final int LAST_UPDATED_COLUMN = 2;
    private static final int UPDATED_BY_COLUMN = 3;
//    //Added for Coeus4.3 enhancement - starts
//    //To include the version number to be added to the document table model
//    private static final int VERSION_COLUMN =3;
//    private static final int PARENT_LAST_UPDATED_COLUMN = 4;
//    private static final int PARENT_UPDATED_BY_COLUMN = 5;
//    //Added for Coeus4.3 enhancement - ends
    private static final String PROTOCOL_SERVLET = "/protocolMntServlet";
    private static final String connectTo
            = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final char GET_DOCUMENT_TYPE = 'b';
    private static final char GET_UPLOAD_DOC_DATA = 'g';
    private static final char ADD_UPD_DEL_DOC_DATA = 'm';
//    private static final char VIEW_PROTO_DOC_DATA = 'o';
    private static final char GET_PARENT_PROTO_DATA = 'P';
    private static final char GET_PROTO_HISTORY_DATA = 'q';
    //Added for Coeus 4.3 enhancement - start
    //variables used to specidy the functionality while clicking the command buttons
    private static final char MODIFY_DOCUMENT = 'M';
    private static final char CHANGE_STATUS = 'S';
    private static final char ADD_DOCUMENT = 'A';
    private static final char AMEND_DOCUMENT = 'E';
    private static final char AMEND_DOCUMENT_CHECK = 'P';
    //Added for Coeus 4.3 enhancement - end
    private static final String NOT_PARENT_WINDOW = "Not Parent Protocol";
//    private static final String PROTOCOL_WINDOW = "Upload Documents for ";
    private Vector vecListData;
//    private Vector vecLatestData;
    private Vector vecParentLatestData;
    //Commented for case 3552 - IRB Attachments - start
//    private CoeusDlgWindow dlgUploadDoc;
    //Commented for case 3552 - IRB Attachments - end
    private ProtocolUploadDocRenderer protocolUploadDocRenderer;
    private ProtocolUploadDocTableModel protocolUploadDocTableModel;
    private ProtocolUploadParentTableModel protocolUploadParentTableModel;
    private char functionType;
    private CoeusMessageResources coeusMessageResources;
 //   private CoeusFileChooser fileChooser;
    private boolean fileSelected;
    private ProtocolInfoBean protocolInfoBean;
    private AppletServletCommunicator comm;
    private RequesterBean requester;
    private ResponderBean responder;
    private byte[] blobData;
    private String windowType;
    private CoeusVector cvDocType;
//    private boolean success = false;
    //Commented for case 3552 - IRB Attachments - start
//    private String baseWindowTitle;
    //Commented for case 3552 - IRB Attachments - end
    private boolean showAll = false;
    
    /*
     * Added for Coeus 4.3 enhancement - start
     * Used to know whether the Change Status button is to be enabled or not
     * according to the protocol status
     */
    private boolean enableChangeStatus = true;
    // Added for Coeus 4.3 enhancement - end
    
    //Added for case 3552 - IRB Attachments - start
    private int DELETED_STATUS_CODE = 3;
    private ProtocolDetailForm protocolDetailForm;
    private final String PROTOCOL_UPLOAD = "Protocol Upload";
    private char previousFunctionType;
    boolean canAddAttachment = false;
    //Added for case 3552 - IRB Attachments - end
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
    private static final char COPY_PROTOCOL = 'P';
    //COEUSQA:3503 - End
    
    //Modified method signature for case 3552 - IRB Attachments
    //Removed the argument base WindowTitle
    /**
     * Creates new form ProtocolUploadDocumentForm
     * @param protocolInfoBean contains Protocol Information
     * @param functionType Window Mode
     * @throws CoeusException if any exception occurs
     */
    public ProtocolUploadDocumentForm(ProtocolInfoBean protocolInfoBean,
            char functionType) throws CoeusException {
//     public ProtocolUploadDocumentForm(ProtocolInfoBean protocolInfoBean,
//            char functionType, String baseWindowTitle) throws CoeusException {
     
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
//        this.baseWindowTitle = baseWindowTitle;
        this.protocolInfoBean = protocolInfoBean;
        this.functionType = functionType;
        setFunctionType(functionType);
        registerComponents();
        //Commented for case 3552 - IRB Attachments - start
        //postInitComponents();
        //Commented for case 3552 - IRB Attachments - end
        formatFields();
    }
    /**
     * Method to register table Model and Listener
     * @throws CoeusException If Any exception occurs
     */
    private void registerComponents() throws CoeusException {
        //Commented for Coeus4.3 enhancement
        //Ok, Cancel, Browse button not required. Upload functionality done in
        //seperate screen
//        btnOk.addActionListener(this);
//        btnCancel.addActionListener(this);
//        btnBrowse.addActionListener(this);
        btnDelete.addActionListener(this);
        btnView.addActionListener(this);
        //Commented for Coeus4.3 enhancement
//        btnUpload.addActionListener(this);
        btnParentView.addActionListener(this);
        btnHistory.addActionListener(this);
        //Added for Coeus 4.3 enhancement - start
        //New Command buttons added - Ammend, showall, close, history
        btnchangeStatus.addActionListener(this);
        btnModify.addActionListener(this);
        btnAmend.addActionListener(this);
        btnAdd.addActionListener(this);
        //Commented for case 3552 - IRB Attachments - start
        //btnClose.addActionListener(this);
        //Commented for case 3552 - IRB Attachments - end
        btnShowAll.addActionListener(this);
        btnParentHistory.addActionListener(this);
        //Added for Coeus 4.3 enhancement - end
        
        //Commented for case 3552 - IRB Attachments - start
//        tblListDocument.addMouseListener(this);
//        tblParent.addMouseListener(this);
        //Commented for case 3552 - IRB Attachments - end
        protocolUploadDocTableModel = new ProtocolUploadDocTableModel();
        protocolUploadDocRenderer = new ProtocolUploadDocRenderer();
        tblListDocument.setModel(protocolUploadDocTableModel);
        tblListDocument.getSelectionModel().addListSelectionListener(this);
        tblParent.getSelectionModel().addListSelectionListener(this);
        //Added for the Case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-start
        tblListDocument.getTableHeader().addMouseListener(this);
        tblParent.getTableHeader().addMouseListener(this);
        //Added for the Case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-end
    }
    /**
     * set The protocol Document editor and headers
     * @throws CoeusException If Any exception occurs
     */
    private void setTableEditors() throws CoeusException {
        JTableHeader tableHeader = tblListDocument.getTableHeader();
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
        tableHeader.addMouseListener(new ColumnHeaderListener());
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
        tableHeader.setReorderingAllowed(false);
        tableHeader.setMaximumSize(new Dimension(100, 25));
        tableHeader.setMinimumSize(new Dimension(100, 25));
        tableHeader.setPreferredSize(new Dimension(100, 25));
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblListDocument.setRowHeight(22);
        tblListDocument.setShowHorizontalLines(true);
        tblListDocument.setShowVerticalLines(true);
        tblListDocument.setOpaque(false);
        tblListDocument.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        tblListDocument.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn columnDetails;
        //Modified for case 3552 - IRB Attachments - start
        //Modified for Coeus 4.3 enhancement -start
        //Removed the status column and adjusted the size
        int []size = {110,270,130,180};
        //Modified for Coeus 4.3 enhancement -end
        //Modified for case 3552 - IRB Attachments - end
        for(int index = 0; index < size.length; index++) {
            columnDetails = tblListDocument.getColumnModel().getColumn(index);
            columnDetails.setPreferredWidth(size[index]);
            if(index == 0) {
                columnDetails.setMinWidth(110);
            }else if(index == 1) {
                //Modified for case 3552 - IRB Attachments - start
                columnDetails.setMinWidth(270);
                //Modified for case 3552 - IRB Attachments - start
            }else if(index == 2) {
                columnDetails.setMinWidth(130);
            }else {
                columnDetails.setMinWidth(180);
            }
            columnDetails.setCellRenderer(protocolUploadDocRenderer);
        }
        if( getWindowType().equals(NOT_PARENT_WINDOW) ) {
            setChildTableEditors();
        }
        //set the focus traverse policy
    }
    /**
     * set the parent document editor , header and register components
     * @throws CoeusException If Any exception occurs
     */
    private void setChildTableEditors() throws CoeusException {
        protocolUploadParentTableModel = new ProtocolUploadParentTableModel();
        tblParent.setModel(protocolUploadParentTableModel);
        JTableHeader tableHeader = tblParent.getTableHeader();
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
        tableHeader.addMouseListener(new ParentColumnHeaderListener());
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
        tableHeader.setReorderingAllowed(false);
        tableHeader.setMaximumSize(new Dimension(100,25));
        tableHeader.setMinimumSize(new Dimension(100,25));
        tableHeader.setPreferredSize(new Dimension(100,25));
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblParent.setRowHeight(22);
        tblParent.setShowHorizontalLines(true);
        tblParent.setShowVerticalLines(true);
        tblParent.setOpaque(false);
        tblParent.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tblParent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn columnDetails;
        //Modified for case 3552 - IRB Attachments - start
        //Modified for Coeus 4.3 enhancement -start
        // Removed status column and adjusted the size
        //int []size = {120,200,80,80,120,90};
        //int []size = {110,220,130,180};
        int []size = {110,270,130,180};
        for(int index = 0; index < size.length; index ++) {
            columnDetails = tblParent.getColumnModel().getColumn(index);
            columnDetails.setPreferredWidth(size[index]);
            if(index == 0) {
                columnDetails.setMinWidth(110);
            }else if(index == 1) {
                columnDetails.setMinWidth(270);
            }else if(index == 2) {
                columnDetails.setMinWidth(130);
            }
            //Modified for Coeus 4.3 enhancement -end
            //Modified for case 3552 - IRB Attachments - end
            columnDetails.setCellRenderer(protocolUploadDocRenderer);
        }
    }
    
    //Commented for case 3552 - IRB attachments - start
    /**
     * Method to set the form data
     * @param data contains form data
     * @throws CoeusException If Any exception occurs
     */
//     public void setFormData(Object data) throws CoeusException {
//        setTableEditors();
//        cvDocType = (CoeusVector) data;
//        ScreenFocusTraversalPolicy traversePolicy;
//        Dimension screenSize;
//        Dimension dlgSize;
//        //Added for Coeus 4.3 enhancement -start
//        //To set the traversal order
//        java.awt.Component[] components = {  btnClose, btnAdd, btnModify,
//        btnDelete, btnView, btnHistory, btnParentView, btnAmend,
//        btnParentHistory, btnShowAll
//        };
//        traversePolicy = new ScreenFocusTraversalPolicy( components );
//        //Added for Coeus 4.3 enhancement -end
//        if( getWindowType().equals(NOT_PARENT_WINDOW) ) {
//            // Commented for Coeus 4.3 enhancement
//            //Since tab is changed to a single window, only one traversal policy required which
//            //is given at the top
////           java.awt.Component[] components = {  btnAdd, btnModify, btnDelete, btnView,btnchangeStatus, btnHistory, btnClose
////            };
////
////            traversePolicy = new ScreenFocusTraversalPolicy( components );
//            // Modified for Coeus 4.3 enhancement - starts
//            
////            tbdDocuments.addTab("List of Documents", pnlUploadDocuments);
////            tbdDocuments.addTab("Documents in Parent Protocol", pnlParent);
////            tbdDocuments.setVisible(true);
////            pnlParentList.setVisible(true);
////            btnParentView.setVisible(true);
//            dlgUploadDoc.setSize(WIDTH, HEIGHT);
//            // Modified for Coeus 4.3 enhancement - ends
//            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            dlgSize = dlgUploadDoc.getSize();
//            dlgUploadDoc.setLocation(screenSize.width / 2 - (dlgSize.width / 2),
//                    screenSize.height / 2 - (dlgSize.height / 2));
//        } else {
//            //Commented for Coeus4.3 enhancement
////            java.awt.Component[] components = { btnDelete, btnView, btnHistory };
////            traversePolicy = new ScreenFocusTraversalPolicy(components);
//            // Modified for Coeus 4.3 enhancement - starts
////            pnlParentList.setVisible(false);
////            btnParentView.setVisible(false);
//            dlgUploadDoc.setSize(WIDTH, 340);
//            pnlParent.setVisible(false);
//            pnlUploadDocuments.setVisible(true);
////            dlgUploadDoc.setSize(WIDTH, HEIGHT);
//            // Modified for Coeus 4.3 enhancement - ends
//            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            dlgSize = dlgUploadDoc.getSize();
//            dlgUploadDoc.setLocation(screenSize.width / 2 - (dlgSize.width / 2),
//                    screenSize.height / 2 - (dlgSize.height / 2));
//        }
//        this.setFocusTraversalPolicy(traversePolicy);
//        this.setFocusCycleRoot(true);
//        refereshData(false);
//    }
    //Commented for case 3552 - IRB attachments - end
    
     //Added for case 3552 - IRB attachments - start
    /**
     * Method to set the form data
     * @throws CoeusException If Any exception occurs
     */
    public void setFormData() throws CoeusException {
        if(functionType != previousFunctionType){
            cvDocType
                = (CoeusVector)getDocumentData('b',null,null);
            String protocolId = null;
            if(protocolInfoBean != null){
                protocolId = protocolInfoBean.getProtocolNumber();
            }
            setWindowType("Protocol Upload");
            if(protocolId != null){
                if(( protocolId.indexOf('A') != -1 ) ||
                        ( protocolId.indexOf('R') != -1 )) {
                    setWindowType("Not Parent Protocol");
                }
            }
            if(functionType == TypeConstants.AMEND_MODE){
                setWindowType("Not Parent Protocol");
            }
             if(functionType == TypeConstants.DISPLAY_MODE){
                    btnView.requestFocus();
                }else{
                    btnAdd.requestFocus();
                }
            setTableEditors();
            ScreenFocusTraversalPolicy traversePolicy;
            java.awt.Component[] components = { btnAdd, btnModify, btnDelete,
                btnView, btnHistory, btnShowAll, btnParentView, btnAmend,
                btnParentHistory
            };
            if( getWindowType().equals(PROTOCOL_UPLOAD) ) {
                pnlParent.setVisible(false);
                pnlUploadDocuments.setVisible(true);
                //Added for COEUSDEV-324 : Premium - Protocol Attachments tab - Provide more room to list attachments - Start
                //To increase the size of the attachment panel
                pnlUploadDocuments.setMinimumSize(new Dimension(850,450));
                pnlUploadDocuments.setPreferredSize(new Dimension(850,450));
                pnlListDocument.setMinimumSize(new Dimension(720,435));
                pnlListDocument.setPreferredSize(new Dimension(720,435));
                scrPnListDocument.setMinimumSize(new Dimension(710,410));
                scrPnListDocument.setPreferredSize(new Dimension(710,410));
                //COEUSDEV-324 : End
                components = new java.awt.Component[]{btnAdd, btnModify, btnDelete,
                    btnView, btnHistory, btnShowAll};
            }
            traversePolicy = new ScreenFocusTraversalPolicy( components );
            this.setFocusTraversalPolicy(traversePolicy);
            this.setFocusCycleRoot(true);
            refereshData(false);
            previousFunctionType = functionType;
        }
    }
    //Added for case 3552 - IRB attachments - end
    /**
     * Populates the original protocol document table
     */
    public void setOriginalProtocolData() throws CoeusException{
        //Added for case 3552 - IRB Attachments - start
        if(protocolInfoBean!=null && functionType != TypeConstants.AMEND_MODE){
            //Added for case 3552 - IRB Attachments - end
            //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
            //vecListData = (Vector) getDocumentData(GET_UPLOAD_DOC_DATA,null, null);
            if(functionType != COPY_PROTOCOL) {
                vecListData = (Vector) getDocumentData(GET_UPLOAD_DOC_DATA,null, null);
            } else {
                if(protocolInfoBean.isCopyAttachments() == false || protocolInfoBean.getAttachments() == null || protocolInfoBean.getAttachments().isEmpty()){
                    vecListData = new Vector();
                } else{
                    vecListData = protocolInfoBean.getAttachments();
                }
            }
            //COEUSQA:3503 - End
            if(!showAll){
                UploadDocumentBean docBean = null;
                Vector filteredVector = new Vector();
                for(int i = 0; i< vecListData.size(); i++){
                    docBean = (UploadDocumentBean)vecListData.get(i);
                    //Modified for case 3552 - IRB Attachments - start
                    //if(!docBean.getStatusDescription().equals("Deleted")){
                    if(docBean.getStatusCode() != DELETED_STATUS_CODE){
                        //Modified for case 3552 - IRB Attachments - end
                        filteredVector.add(docBean);
                    }
                }
                vecListData = filteredVector;
            }
            protocolUploadDocTableModel.setData(vecListData);
            protocolUploadDocTableModel.fireTableDataChanged();
            if (tblListDocument.getRowCount() > 0) {
                tblListDocument.setRowSelectionInterval(0,0);
                tblListDocument.setColumnSelectionInterval(0,0);
            }
        }
    }
    
    /**
     * Method to set the parent protocol data in table.
     * @throws CoeusException If any exception occurs
     */
    private void setParentProtocolData() throws CoeusException {
        String protocolNumber = protocolInfoBean.getProtocolNumber();
        if (protocolNumber != null) {
            if (protocolNumber.indexOf('A') != -1) {
                protocolNumber = protocolNumber.substring(0,
                        protocolNumber.indexOf('A'));
            } else if(protocolNumber.indexOf('R') != -1) {
                protocolNumber = protocolNumber.substring(0,
                        protocolNumber.indexOf('R'));
            }
            vecParentLatestData = (Vector) getDocumentData(
                    GET_PARENT_PROTO_DATA, protocolNumber,null);
            UploadDocumentBean docBean = null;
            Vector filteredParentVector = new Vector();
            for(int i = 0; i< vecParentLatestData.size(); i++){
                docBean = (UploadDocumentBean)vecParentLatestData.get(i);
                //Modified for case 3552 - IRB Attachments - start
                //if(!docBean.getStatusDescription().equals("Deleted")){
                if(docBean.getStatusCode()!= DELETED_STATUS_CODE){
                    //Modified for case 3552 - IRB Attachments - end
                    filteredParentVector.add(docBean);
                }
            }
            vecParentLatestData = filteredParentVector;
            protocolUploadParentTableModel.setData(vecParentLatestData);
            protocolUploadParentTableModel.fireTableDataChanged();
            //Commented for Coeus4.3 enhancement start1
            //TO remove the selection of the table row
//            if (tblParent.getRowCount() > 0) {
//                tblParent.setRowSelectionInterval(0, 0);
//                tblParent.setColumnSelectionInterval(0, 0);
//            }
            //Commented for Coeus4.3 enhancement end1
        }
    }
    /**
     * method to set the data for parent, amendment and renewal
     * @param isUpdated boolean to check data is refereh again or not
     * @throws CoeusException If any exception occurs
     */
    private void refereshData(boolean isUpdated) throws CoeusException {
        //if user modified code table for doc type
        if (isUpdated) {
            cvDocType = (CoeusVector) getDocumentData(GET_DOCUMENT_TYPE, null,null);
        }
        // Commented for Coeus 4.3 enhancement - start -1
        //Moved to a new function setOriginalProtocolData
        
//        cmbDocumentType.setModel(new DefaultComboBoxModel(cvDocType));
//        vecListData = (Vector) getDocumentData(GET_UPLOAD_DOC_DATA, null);
//        if(!showAll){
//                UploadDocumentBean docBean = null;
//                Vector filteredVector = new Vector();
//                for(int i = 0; i< vecParentLatestData.size(); i++){
//                    docBean = (UploadDocumentBean)vecParentLatestData.get(i);
//                    if(!docBean.getStatusDescription().equals("Deleted")){
//                        filteredVector.add(docBean);
//                    }
//                }
//                vecListData = filteredVector;
//            }
//        protocolUploadDocTableModel.setData(vecListData);
//        protocolUploadDocTableModel.fireTableDataChanged();
//        if (tblListDocument.getRowCount() > 0) {
//            tblListDocument.setRowSelectionInterval(0,0);
//            tblListDocument.setColumnSelectionInterval(0,0);
//        }
        // Commented for Coeus 4.3 enhancement - end -2
        setOriginalProtocolData();
        if (getWindowType().equals(NOT_PARENT_WINDOW)) {
            setParentProtocolData();
        }
        
    }
    // Commented for Coeus 4.3 enhancement - start
    
    /**
     * method to set default focus based on open window
     * @throws CoeusException If Any exception occurs
     */
    /*
    private void requestDefaultFocusToComp() throws CoeusException{
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            txtDescription.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
     */
    // Commented for Coeus 4.3 enhancement - end
    
    //Commented for case 3552 - IRB Attachments - start
    /**
     * Method to initialise dialog
     * @throws CoeusException If Any exception occurs
     */
//    private void postInitComponents() throws CoeusException{
//        dlgUploadDoc = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
//        dlgUploadDoc.setResizable(false);
//        dlgUploadDoc.setModal(true);
//        dlgUploadDoc.getContentPane().add(this);
//        dlgUploadDoc.setFont(CoeusFontFactory.getLabelFont());
//        //dlgUploadDoc.setTitle(PROTOCOL_WINDOW +baseWindowTitle);
//        dlgUploadDoc.setTitle("Attachments");
//        
//        dlgUploadDoc.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
//        // Commented for Coeus 4.3 enhancement -start
//        //Not required as the upload section is moved to a new dialog window
////        dlgUploadDoc.addComponentListener(
////                new ComponentAdapter(){
////            public void componentShown(ComponentEvent e){
////                try{
////                    requestDefaultFocusToComp();
////                }catch(CoeusException ce){
////                    ce.printStackTrace();
////                }
////            }
////        });
//        // Commented for Coeus 4.3 enhancement -end
//        dlgUploadDoc.addWindowListener(new WindowAdapter(){
//            public void windowClosing(WindowEvent we){
//                //Commented for Coeus4.3 enhancement
//                //Not required as the upload section is moved to a new dialog window
////                    performCloseAction();
//                dlgUploadDoc.dispose();
//            }
//        });
//        
//        dlgUploadDoc.addEscapeKeyListener(new AbstractAction("escPressed"){
//            
//            public void actionPerformed(ActionEvent ae){
//                //Commented for Coeus4.3 enhancement
//                //Not required as the upload section is moved to a new dialog window
////                    performCloseAction();
//                dlgUploadDoc.dispose();
//            }
//        });
////        setPreferredSize(new Dimension(789, 477));
////        setMinimumSize(new Dimension(789, 477));
////        setMaximumSize(new Dimension(789, 477));
//    }
    //Commented for case 3552 - IRB Attachments - end
    
    /**
     * method to disable components based on open window
     */
    public void formatFields() {
        //Modified for case 3552 - IRB Attachments - start
        if(functionType == TypeConstants.DISPLAY_MODE
                || functionType == TypeConstants.AMEND_MODE){
//            btnOk.setEnabled(false);
            //Modified for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_Start
            try {
                if(isRenewal(protocolInfoBean.getProtocolNumber())){
                    btnDelete.setEnabled(true);
                    btnAdd.setEnabled(true);
                    btnModify.setEnabled(true);
                    btnAmend.setEnabled(false);
                }else{
                    btnDelete.setEnabled(false);
                    btnAdd.setEnabled(false);
                    btnchangeStatus.setEnabled(false);
                    enableChangeStatus = false;
                    btnModify.setEnabled(false);
                    btnAmend.setEnabled(false);
                }
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
            //Modified for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_End
        }else{
            btnDelete.setEnabled(true);
            btnAdd.setEnabled(true);
            btnModify.setEnabled(true);
            btnAmend.setEnabled(true);
        }
        //Modified for case 3552 - IRB Attachments - end
            // Commented for Coeus 4.3 enhancement - start
            //Not required as the upload section is moved to a new dialog window
//            btnUpload.setEnabled(false);
//            btnBrowse.setEnabled(false);
//            txtDescription.setBackground(UIManager.getColor("Panel.Background"));
//            txtDescription.setEditable(false);
//            txtDescription.setEnabled(false);
//            txtFileName.setBackground(UIManager.getColor("Panel.Background"));
//            txtFileName.setEditable(false);
//            txtFileName.setEnabled(false);
//            cmbDocumentType.setEditable(false);
//            cmbDocumentType.setEnabled(false);
            // Commented for Coeus 4.3 enhancement - end
        
        // Added for Coeus 4.3 enhancement - start
        /*
         * Enable the change status button only when the protocol is of the status
         * submitted(101)
         * Active - Open to enrollment(200)
         */
//        if(protocolInfoBean!=null && getFunctionType() != TypeConstants.DISPLAY_MODE &&
//                enableChangeStatus &&
//                (protocolInfoBean.getProtocolStatusCode() == 101 ||
//                protocolInfoBean.getProtocolStatusCode() == 200)){
//            btnchangeStatus.setEnabled(true);
//            enableChangeStatus = true;
//        }else if(getFunctionType() != TypeConstants.DISPLAY_MODE){
//            btnchangeStatus.setEnabled(false);
//            enableChangeStatus = false;
//        }
        // Added for Coeus 4.3 enhancement - end
    }
    /**
     * method to clean up when window dispose.
     * @throws CoeusException If Any exception occurs
     */
    public void cleanup() throws CoeusException{
        // Commented for Coeus 4.3 enhancement - start
        //Not required as the upload section is moved to a new dialog window
//        btnBrowse.removeActionListener(this);
//        btnCancel.removeActionListener(this);
        // Commented for Coeus 4.3 enhancement - end
        btnDelete.removeActionListener(this);
        btnHistory.removeActionListener(this);
//        btnOk.removeActionListener(this); // Commented for Coeus 4.3 enhancement
        btnParentView.removeActionListener(this);
//        btnUpload.removeActionListener(this); // Commented for Coeus 4.3 enhancement
        btnView.removeActionListener(this);
        tblParent.removeMouseListener(this);
        tblListDocument.removeMouseListener(this);
        protocolUploadDocRenderer = null;
        protocolUploadDocTableModel = null;
        protocolUploadParentTableModel = null;
        //Commented for case 3552 - IRB Attachments - start
//        dlgUploadDoc = null;
        //Commented for case 3552 - IRB Attachments - end
    }
    // Commented for Coeus 4.3 enhancement - start
    /**
     * method to perform action on cancel button
     * @throws CoeusException If Any exception occurs
     */
    
//    private void performCloseAction() throws CoeusException{
//        if(isDataChanged()){
//            int option = CoeusOptionPane.showQuestionDialog(
//                    coeusMessageResources.parseMessageKey("budget_baseWindow_exceptionCode.1402"),
//                    CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
//            switch( option ) {
//                case (CoeusOptionPane.SELECTION_YES):
//                    dlgUploadDoc.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
//                    performUploadAction(true);
//                    if(success){
//                        dlgUploadDoc.dispose();
//                    }
//                    success = false;
//                    dlgUploadDoc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
//                    break;
//                case(CoeusOptionPane.SELECTION_NO):
//                    dlgUploadDoc.dispose();
//                    break;
//                default:
//                    break;
//
//            }
//        }else{
//            //data not modified so dispose the window
//            dlgUploadDoc.dispose();
//        }
//
//    }
    /**
     * method to check data is changed
     * @throws CoeusException If Any exception occurs
     * @return boolean to check data is modified or not
     */
//    private boolean isDataChanged() throws CoeusException{
//        boolean success = false;
//        if(txtDescription.getText() != null && txtFileName.getText() != null){
//            if(txtDescription.getText().trim().length() > 0){
//                success = true;
//            }
//            if(txtFileName.getText().trim().length() > 0){
//                success = true;
//            }
//        }
//        return success;
//    }
    /**
     * method to perform action on browse button
     * @throws CoeusException If Any exception occurs
     */
//    private void performBrowseAction() throws CoeusException{
//        fileChooser = new CoeusFileChooser(dlgUploadDoc);
//        fileChooser.setAcceptAllFileFilterUsed(true);
//        fileChooser.showFileChooser();
//        if(fileChooser.isFileSelected()){
//            String fileName = fileChooser.getSelectedFile();
//            if(fileName != null && !fileName.trim().equals(EMPTY_STRING)){
//                int index = fileName.lastIndexOf('.');
//                if(index != -1 && index != fileName.length()){
//                    setFileSelected(true);
//                    txtFileName.setText(fileChooser.getFileName().getName());
//                    setBlobData(fileChooser.getFile());
//                }else{
//                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
//                            "correspType_exceptionCode.1012"));
//                    setFileSelected(false);
//                    setBlobData(null);
//                }
//            }
//        }
//    }
    // Commented for Coeus 4.3 enhancement - end
    /**
     * method to perform delete button operation
     * @throws CoeusException If Any exception occurs
     */
    private void performDeleteAction() throws CoeusException{
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1000"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1001"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vecListData.get(selectedRow);
//        if(uploadDocumentBean.getStatusCode() != 1) {
//            CoeusOptionPane.showInfoDialog("This document is '"
//                    +uploadDocumentBean.getStatusDescription()+"'. Cannot be deleted.");
//            return;
//        }
        //Added for case 3552 - IRB Attachments - start
        //Save the protocol, before adding the attachment
        if(!canAddAttachment && protocolDetailForm != null 
                && (protocolDetailForm.getOriginalFunctionType() == 'A'
                || protocolDetailForm.getOriginalFunctionType() == 'R'
                || protocolDetailForm.getOriginalFunctionType() == 'N'
                || protocolDetailForm.isSaveRequired())){
                ActionEvent ae = new ActionEvent(protocolDetailForm.btnSaveProtocol, 1001, null);
                protocolDetailForm.actionPerformed(ae);
                if(protocolDetailForm.isSaveSuccessful()){
                    protocolDetailForm.setSaveSuccessful(false);
                    canAddAttachment = true;
                } 
            
        }else{
            canAddAttachment = true;
        }
        
        if(canAddAttachment){
            //Added for case 3552 - IRB Attachments - end
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);

            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                //Added for Coeus4.3 enhancement - end 1
                //If the status of the document is draft, delete the record from db
                //else insert a record with status deleted, and blob data null
                if(uploadDocumentBean.getStatusCode()==1){
                    uploadDocumentBean.setAcType(TypeConstants.DELETE_RECORD);
                }else{
                    uploadDocumentBean.setDocument(new byte[1]);//dummy file with 1 byte size
                    uploadDocumentBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                    uploadDocumentBean.setStatusCode(3);
                    uploadDocumentBean.setAcType("I");
                }
                //Added for Coeus4.3 enhancement - end 1
                boolean isSuccess = addUpdDocument(uploadDocumentBean, true, false);
                if(isSuccess){
                    refereshData(true);
                }
            }
        }
    }
    /**
     * method to perform view button operation
     * @throws CoeusException If Any exception occurs
     */
    private void performViewAction() throws CoeusException{
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vecListData.get(selectedRow);
        viewDocument(uploadDocumentBean);
    }
    /**
     * Method to perform view action in parent protocol table
     * @throws CoeusException If any exception occurs
     */
    private void performParentViewAction() throws CoeusException{
        int rowCount = tblParent.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
            return;
        }
        int selectedRow = tblParent.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vecParentLatestData.get(selectedRow);
        viewDocument(uploadDocumentBean);
    }
    /**
     * Method to perform history of all documents uploaded
     * @throws CoeusException If any exception occurs
     */
    private void performHistoryAction() throws CoeusException{
        //Added for Coeus4.3 enhancement -starts 1
        // Show the history of the selected document
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1014"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1015"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vecListData.get(selectedRow);
        //Added for Coeus4.3 enhancement - ends 1
        List vecHistoryData = getDocumentData(GET_PROTO_HISTORY_DATA, protocolInfoBean.getProtocolNumber(),
                Integer.toString(uploadDocumentBean.getDocumentId()));
        
        // Commented for Coeus 4.3 enhancement - start
        //Not required since we are disabling the history button if there is no history
//        if(vecHistoryData == null || vecHistoryData.size() == 0 ) {
//            CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(
//                    "protocolUpload_exceptionCode.1010")
//                    +baseWindowTitle+"'.");
//            return;
//        }
        // Commented for Coeus 4.3 enhancement - end
        try{
            //Modified for case 3552 - IRB Attachments - start
            setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            ProtocolUploadHistoryForm protocolUploadHistoryForm
                    = new ProtocolUploadHistoryForm(protocolInfoBean);
            //Modified for case 3552 - IRB Attachments - end
            protocolUploadHistoryForm.setFormData(vecHistoryData);
            protocolUploadHistoryForm.setParentWindow(this);
            protocolUploadHistoryForm.display();
            protocolUploadHistoryForm.cleanup();
            protocolUploadHistoryForm = null;
        }finally {
            //Modified for case 3552 - IRB Attachments - start
            setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            //Modified for case 3552 - IRB Attachments - end
        }
        
    }
    /**
     *Coeus 4.3 enhancement
     * Shows the history of all the selected document in the parent table
     */
    private void performParentHistoryAction() throws CoeusException{
        int rowCount = tblParent.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1014"));
            return;
        }
        int selectedRow = tblParent.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1015"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vecParentLatestData.get(selectedRow);
        String protocolNumber = protocolInfoBean.getProtocolNumber();
        if (protocolNumber.indexOf('A') != -1) {
            protocolNumber = protocolNumber.substring(0,
                    protocolNumber.indexOf('A'));
        } else if(protocolNumber.indexOf('R') != -1) {
            protocolNumber = protocolNumber.substring(0,
                    protocolNumber.indexOf('R'));
        }
        List vecHistoryData = getDocumentData(GET_PROTO_HISTORY_DATA, protocolNumber,
                Integer.toString(uploadDocumentBean.getDocumentId()));
        
        
        try{
            //Modified for case 3552 - IRB Attachments - start
            setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            ProtocolUploadHistoryForm protocolUploadHistoryForm
                    = new ProtocolUploadHistoryForm(protocolInfoBean);
            //Modified for case 3552 - IRB Attachments - end
            protocolUploadHistoryForm.setFormData(vecHistoryData);
            protocolUploadHistoryForm.setParentWindow(this);
            protocolUploadHistoryForm.display();
            protocolUploadHistoryForm.cleanup();
            protocolUploadHistoryForm = null;
        }finally {
            //Modified for case 3552 - IRB Attachments - start
            setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            //Modified for case 3552 - IRB Attachments - end
        }
        
    }
    /**
     * Mehtod to perform the modify of the selected document
     * @throws CoeusException if any exception occurs
     */
    public void performModifyAction() throws CoeusException{
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1012"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
            return;
        }
        //Added for case 3552 - IRB attachments - start
        //Save the protocol before modifying the attachment
        if(!canAddAttachment && protocolDetailForm != null 
                && (protocolDetailForm.getOriginalFunctionType() == 'A'
                || protocolDetailForm.getOriginalFunctionType() == 'R'
                || protocolDetailForm.getOriginalFunctionType() == 'N'
                || protocolDetailForm.isSaveRequired())){
                ActionEvent ae = new ActionEvent(protocolDetailForm.btnSaveProtocol, 1001, null);
                protocolDetailForm.actionPerformed(ae);
                if(protocolDetailForm.isSaveSuccessful()){
                    protocolDetailForm.setSaveSuccessful(false);
                    canAddAttachment = true;
                } 
        }else{
            canAddAttachment = true;
        }
        
        if(canAddAttachment &&  checkDocumentTypeExists()){
            //Added for case 3552 - IRB attachments - end
            UploadDocumentBean uploadDocumentBean
                    = (UploadDocumentBean)vecListData.get(selectedRow);
            ProtocolUploadModifyForm protUploadModifyForm =
                    new ProtocolUploadModifyForm(uploadDocumentBean, protocolInfoBean, MODIFY_DOCUMENT);
            protUploadModifyForm.setCvDocType(cvDocType);
            protUploadModifyForm.setTblDocuments(tblListDocument);
            protUploadModifyForm.showWindow();
            if(protUploadModifyForm.isSaveRequired()){
                refereshData(true);
            }
        }
    }
    public void performAddAction() throws CoeusException{
        //Modified for case 3552 - IRB Attachments - start
        //If the protocol is not in modify mode first save the protocol.
        //Check whether the all validations passed and protocol is saved
        //If save successful show the Add window for the attachments
        if(!canAddAttachment && protocolDetailForm != null 
                && (protocolDetailForm.getOriginalFunctionType() == 'A'
                || protocolDetailForm.getOriginalFunctionType() == 'R'
                || protocolDetailForm.getOriginalFunctionType() == 'N'
                || protocolDetailForm.isSaveRequired())){
                ActionEvent ae = new ActionEvent(protocolDetailForm.btnSaveProtocol, 1001, null);
                protocolDetailForm.actionPerformed(ae);
                if(protocolDetailForm.isSaveSuccessful()){
                    protocolDetailForm.setSaveSuccessful(false);
                    canAddAttachment = true;
                } 
            
        }else{
            canAddAttachment = true;
        }
        
        if(canAddAttachment && checkDocumentTypeExists()){
            UploadDocumentBean uploadDocumentBean =  new UploadDocumentBean();
            uploadDocumentBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
            uploadDocumentBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
            ProtocolUploadModifyForm protUploadModifyForm =
                    new ProtocolUploadModifyForm(uploadDocumentBean, protocolInfoBean, ADD_DOCUMENT);
            protUploadModifyForm.setCvDocType(cvDocType);
            protUploadModifyForm.showWindow();
            if(protUploadModifyForm.isSaveRequired()){
                refereshData(true);
            }
       }else{
            return;
       }
        //Modified for case 3552 - IRB Attachments - end
    }
    
    /**
     * Coeus 4.3 enhancement
     * Mehtod to perform the change status of the selected document
     * @throws CoeusException if any exception occurs
     */
    public void performChangeStatusAction() throws CoeusException{
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1011"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vecListData.get(selectedRow);
        ProtocolUploadModifyForm protUploadModifyForm =
                new ProtocolUploadModifyForm(uploadDocumentBean,protocolInfoBean, CHANGE_STATUS);
        protUploadModifyForm.setCvDocType(cvDocType);
        protUploadModifyForm.setTblDocuments(tblListDocument);
        protUploadModifyForm.showWindow();
        if(protUploadModifyForm.isSaveRequired()){
            refereshData(true);
        }
    }
    public void performShowAll() throws CoeusException{
        
        if(showAll){
            showAll = false;
        }else {
            showAll = true;
        }
        if(showAll){
            btnShowAll.setText("Show Active");
        }else{
            btnShowAll.setText("Show All");
        }
        setOriginalProtocolData();
    }
    //Commented for Coeus4.3 enhancement - start
    //Not required as the upload section is moved to a new dialog window
    /**
     * method to perform upload button operation
     * @param isClosedConfirm is close confirm or ok button
     * @throws CoeusException If Any exception occurs
     */
//    private void performUploadAction(boolean isClosedConfirm) throws CoeusException{
//        if(validateForm()){
//            if(isFileSelected()){
//                if(getBlobData().length == 0 ){
//                    CoeusOptionPane.showErrorDialog(
//                            coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1005"));
//                    return ;
//                }
//            }
//            ComboBoxBean cmbTypeCode =
//                    (ComboBoxBean)cmbDocumentType.getSelectedItem();
//            UploadDocumentBean uploadDocumentBean
//                    = new UploadDocumentBean();
//            uploadDocumentBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
//            uploadDocumentBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
//            uploadDocumentBean.setDescription(txtDescription.getText().trim());
//            uploadDocumentBean.setDocCode(Integer.parseInt(cmbTypeCode.getCode()));
//            uploadDocumentBean.setDocument(getBlobData());
//            uploadDocumentBean.setFileName(txtFileName.getText().trim());
//            uploadDocumentBean.setStatusCode(1);
//            uploadDocumentBean.setAcType(TypeConstants.INSERT_RECORD);
//            if(checkDraftDocAvailable(uploadDocumentBean.getDocCode())) {
//                uploadDocumentBean = getDraftDocument(uploadDocumentBean.getDocCode());
//                uploadDocumentBean.setDescription(txtDescription.getText().trim());
//                uploadDocumentBean.setFileName(txtFileName.getText().trim());
//                uploadDocumentBean.setDocument(getBlobData());
//                uploadDocumentBean.setAcType(TypeConstants.UPDATE_RECORD);
//            }
//            boolean isSuccess = addUpdDocument(uploadDocumentBean);
//            System.out.println("isSuccess ::"+isSuccess);
//            if(isSuccess){
//                txtDescription.setText(EMPTY_STRING);
//                txtFileName.setText(EMPTY_STRING);
//                setFileSelected(false);
//                setBlobData(null);
//                System.out.println(" before the  is ClsesConfirm");
//                if(!isClosedConfirm) {
//                    System.out.println(" inside the  is ClsesConfirm");
//                    refereshData(true);
//                }else {
//                    success = true;
//                }
//
//            }
//        }
//    }
    //Commented for Coeus4.3 enhancement - end
    /**
     * Coeus 4.3 enhancement
     * Method to perform the amend action
     * @throws CoeusException if any exception occurs
     */
    public void performAmendAction() throws CoeusException{
        int rowCount = tblParent.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1017"));
            return;
        }
        int selectedRow = tblParent.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1016"));
            return;
        }
        //Added for case 3552 - IRB attachments - start
        if(checkDocumentTypeExists()){
            //Added for case 3552 - IRB attachments - end
            UploadDocumentBean uploadDocumentBean
                    = (UploadDocumentBean)vecParentLatestData.get(selectedRow);
            //Check if the document is already amended in any other amendments or renewals
            if(isAmendable(uploadDocumentBean)){
                uploadDocumentBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                uploadDocumentBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                ProtocolUploadModifyForm protocolUploadModifyForm =
                        new ProtocolUploadModifyForm(uploadDocumentBean, protocolInfoBean, AMEND_DOCUMENT);
                protocolUploadModifyForm.setCvDocType(cvDocType);
                protocolUploadModifyForm.showWindow();

                if(protocolUploadModifyForm.isSaveRequired()){
                    refereshData(true);
                }else{
                    refereshData(false);
                }
            }else{
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1013"));
            }
        }
    }
    /**
     * Coeus 4.3 enhancement
     * Checks whether the document is already amended
     * @return boolean true if the document is not present else false
     */
    public boolean isAmendable(UploadDocumentBean uploadDocumentBean) throws CoeusException{
        boolean isAmendable = true;
        requester = new RequesterBean();
        requester.setFunctionType(AMEND_DOCUMENT_CHECK);
        Vector serverData = new Vector();
        serverData.add(protocolInfoBean.getProtocolNumber());
        //COEUSQA-2431 checking whether the document is already amended - start
        serverData.add(protocolInfoBean.getSequenceNumber());
        //COEUSQA-2431 - end
        serverData.add(new Integer(uploadDocumentBean.getDocumentId()));
        requester.setDataObjects(serverData);
        comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }else{
            isAmendable = ((Boolean)responder.getDataObject()).booleanValue();
        }
        return isAmendable;
    }
    /**
     * Method to get draft document data
     * @param docTypeDoc for which document type
     * @throws CoeusException If any exception occurs
     * @return bean containing draft data
     */
//    private UploadDocumentBean getDraftDocument(int docTypeDoc)
//    throws CoeusException {
//        UploadDocumentBean uploadDocumentBean = null;;
//        if(vecListData != null && vecListData.size() > 0 ) {
//            for(int index = 0 ; index < vecListData.size() ; index ++ ) {
//                uploadDocumentBean
//                        = (UploadDocumentBean)vecListData.get(index);
//                if(uploadDocumentBean.getDocCode() == docTypeDoc ) {
//                    if(uploadDocumentBean.getStatusCode() == 1) {
//                        break;
//                    }
//                }
//            }
//        }
//        return uploadDocumentBean;
//    }
    /**
     * method to check draft data is available or not.
     * @param docTypeCode for which document type
     * @throws CoeusException If any exception occurs
     * @return true if specified document type contain draft data or not.
     */
//    private boolean checkDraftDocAvailable(int docTypeCode)
//    throws CoeusException {
//        boolean isDraftDoc = false;
//        if(vecListData != null && vecListData.size() > 0 ) {
//            for(int index = 0 ; index < vecListData.size() ; index ++ ) {
//                UploadDocumentBean uploadDocumentBean
//                        = (UploadDocumentBean)vecListData.get(index);
//                if(uploadDocumentBean.getDocCode() == docTypeCode ) {
//                    if(uploadDocumentBean.getStatusCode() == 1){
//                        isDraftDoc = true;
//                        break;
//                    }
//                }
//            }
//        }
//        return isDraftDoc;
//    }
    /**
     * Method to add or update or delete data
     * @param uploadDocumentBean Upload Document bean data contains information
     * for added or deleted or updated bean
     * @throws CoeusException If Any exception occurs
     * @return true if data successfully added or deleted.
     */
    private boolean addUpdDocument(UploadDocumentBean uploadDocumentBean,
            boolean newVersion, boolean newDocumentId)
            throws CoeusException{
        requester = new RequesterBean();
        //requester.setDataObject(uploadDocumentBean);  //Commented for Coeus 4.3 enhancement
        requester.setFunctionType(ADD_UPD_DEL_DOC_DATA);
        //Added for Coeus 4.3 enhancement - start
        Vector vecServerObjects = new Vector();
        vecServerObjects.add(0,uploadDocumentBean);
        vecServerObjects.add(1, new Boolean(newDocumentId));//new document id
        vecServerObjects.add(2, new Boolean(newVersion));// new version number
        requester.setDataObjects(vecServerObjects);
        //Added for Coeus 4.3 enhancement -end
        comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        return responder.isSuccessfulResponse();
    }
    
    //Commented for case 3552 - IRB Attachments - start
    /**
     * method to display dialog
     * @throws CoeusException If Any exception occurs
     */
//    public void display() throws CoeusException{
//        dlgUploadDoc.setVisible(true);
//    }
    //Commented for case 3552 - IRB Attachments - end
    
    /**
     * Method to get the document url with file contents
     * @param uploadDocumentBean bean containing data for view document
     * @throws CoeusException If any exception occurs
     * @return url of view document
     */
    public String getDocumentURL(UploadDocumentBean uploadDocumentBean) throws CoeusException{
        String templateUrl =  null;
        requester = new RequesterBean();
        //requester.setDataObject(uploadDocumentBean);
        //requester.setFunctionType(VIEW_PROTO_DOC_DATA);
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("UPLOAD_DOC_BEAN", uploadDocumentBean);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        requester.setDataObject(documentBean);
        requester.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        comm = new AppletServletCommunicator(STREMING_SERVLET, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
//        templateUrl = (String)responder.getDataObject();
        map = (Map)responder.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
        return templateUrl;
    }
    /**
     * Method to get the document url with file contents
     * @param uploadDocumentBean bean containing data for view document
     * @throws CoeusException If any exception occurs
     */
    public void viewDocument(UploadDocumentBean uploadDocumentBean) throws CoeusException{
        String url = getDocumentURL(uploadDocumentBean);
        if(url == null || url.trim().length() == 0 ) {
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1009"));
            return;
        }
//        AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        url = url.replace('\\', '/') ; // this is fix for Mac
        try{
//            URL reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + url );
//
//            if (coeusContxt != null) {
//                coeusContxt.showDocument( reportUrl, "_blank" );
//            }
//            else {
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                bs.showDocument(reportUrl);
//            }
            URL urlObj = new URL(url);
            URLOpener.openUrl(urlObj);
        }catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
//        }catch(javax.jnlp.UnavailableServiceException usex) {
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("printFrm_exceptionCode.1001"));
//            usex.printStackTrace();
            
        }catch( Exception ue) {
            ue.printStackTrace() ;
        }
    }
    /**
     * Method to perform action on components
     * @param event event on components
     */
    public void actionPerformed(java.awt.event.ActionEvent event) {
        Object source= event. getSource();
        try{
            //Modified for case 3552 - IRB attachments - start
            setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            //Modified for case 3552 - IRB attachments - end
            //Commented for Coeus4.3 enhancement - start
            //The document upload is moved to seperate popup, hence btnOk abd
            //is not required in this form
//            if(source.equals(btnBrowse)){
//                performBrowseAction();
//            }else if(source.equals(btnCancel)) {
//                performCloseAction();
//
//                dlgUploadDoc.dispose();
//            }else
            //Commented for Coeus4.3 enhancement - end
            if(source.equals(btnDelete)) {
                performDeleteAction();
                //Commented for Coeus4.3 enhancement -start
                //The document upload is moved to seperate popup, hence btnOk abd
                //is not required in this form
//            }
//            else if(source.equals(btnUpload)) {
//                performUploadAction(false);
                //Commented for Coeus4.3 enhancement - end
            }else if(source.equals(btnView) ) {
                performViewAction();
            }else if(source.equals(btnParentView) ) {
                performParentViewAction();
            }else if(source.equals(btnHistory) ){
                performHistoryAction();
            }else if(source.equals(btnchangeStatus)){
                performChangeStatusAction();
            }else if(source.equals(btnModify)){
                performModifyAction();
                //Commented for Coeus4.3 enhancement - start
                //The document upload is moved to seperate popup, hence btnOk abd
                //is not required in this form
//            }else if(source.equals(btnOk)){
//                if(isDataChanged()){
//                    performUploadAction(true);
//                    if(success){
//                        dlgUploadDoc.dispose();
//                    }
//                    success = false;
//                }else{
//                    //data not modified so dispose the window
//                    dlgUploadDoc.dispose();
//                }
                //Commented for Coeus4.3 enhancement - end
                //Added for Coeus 4.3 enhancement - start
                //Actions to be performed for the amend, add, close, history buttons
            }else if(source.equals(btnAmend)){
                performAmendAction();
            }else if(source.equals(btnAdd)){
                performAddAction();
            }
            //Commented for case 3552 - IRB attachments - start
//            else if(source.equals(btnClose)){
//                dlgUploadDoc.dispose();
//            }
            //Commented for case 3552 - IRB attachments - end
            else if(source.equals(btnShowAll)){
                performShowAll();
            }else if(source.equals(btnParentHistory)){
                performParentHistoryAction();
            }
            //Added for Coeus 4.3 enhancement -end
        }catch(CoeusException ce){
            CoeusOptionPane.showErrorDialog(ce.getMessage());
            ce.printStackTrace();
        }finally{
            //Modified for case 3552 - IRB attachments - start
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            //Modified for case 3552 - IRB attachments - end
        }
    }
    //Commented for Coeus 4.3 enhancement -start
    //The document upload is moved to seperate popup, and the validation is done in that popup
    /**
     * Method to validate form data
     * @throws CoeusException If Any exception occurs
     * @return boolean to tell data is valid or not.
     */
    
    
//    private boolean validateForm() throws CoeusException{
//        if(txtDescription.getText() != null && txtFileName.getText() != null){
//            if(txtDescription.getText().trim().length() == 0){
//                CoeusOptionPane.showInfoDialog(
//                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1006"));
//                txtDescription.requestFocusInWindow();
//                return false;
//            }
//            if(txtFileName.getText().trim().length() == 0){
//                CoeusOptionPane.showInfoDialog(
//                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
//                return false;
//            }
//        }
//        return true;
//    }
    //Commented for Coeus 4.3 enhancement -end
    /**
     * Class for Upload Document Table Model
     */
    private class ProtocolUploadDocTableModel extends AbstractTableModel{
        //Modified for Coeus 4.3 enhancement - start
        //To remove the status column from the table
//        private String [] colNames = {"Type","Description","Status","Last Updated","Updated By"};
//        private Class colClass[]={String.class,String.class,String.class,String.class,String.class};
        private String [] colNames = {"Type","Description","Last Updated","Updated By"};
        private Class colClass[]={String.class,String.class,String.class,String.class};
        //Modified for Coeus 4.3 enhancement - end
        private Vector vecListData;
        /**
         * method to make cell editable
         * @param row disable this row
         * @param col disable this column
         * @return boolean for cell editable state
         */
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * method to count total column
         * @return number of column
         */
        public int getColumnCount() {
            return colNames.length;
        }
        /**
         * Method to get column name
         * @param col column number to get name
         * @return name of the column
         */
        public String getColumnName(int col){
            return colNames[col];
        }
        /**
         * method to get column class
         * @param col number of column
         * @return Class of column
         */
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * method to get total rows
         * @return total number of rows
         */
        public int getRowCount() {
            if(vecListData == null || vecListData.size() == 0){
                return 0;
            }
            return vecListData.size();
            
        }
        /**
         * method to set data in table
         * @param vecListData contains data for table
         */
        public void setData(Vector vecListData){
            this.vecListData = vecListData;
        }
        /**
         * method to get value
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object value related to this row and column
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(vecListData != null && vecListData.size() > 0){
                UploadDocumentBean uploadDocBean
                        = (UploadDocumentBean)vecListData.get(rowIndex);
                switch(columnIndex){
                    case TYPE_COLUMN:
                        return uploadDocBean.getDocType();
                    case DESCRIPTION_COLUMN:
                        return uploadDocBean.getDescription();
                        //Commented for Coeus4.3 enhancement -start
                        //Status column is removed
                        //case STATUS_COLUMN:
                        //return uploadDocBean.getStatusDescription();
                        //Commented for Coeus4.3 enhancement -end
                    case LAST_UPDATED_COLUMN:
                        //Modified for case 3552 - IRB Attachments - start
                        //return uploadDocBean.getUpdateTimestamp().toString();
                        return CoeusDateFormat.format(uploadDocBean.getUpdateTimestamp().toString());
                        //Modified for case 3552 - IRB Attachments - end
                    case UPDATED_BY_COLUMN:
//                        return uploadDocBean.getUpdateUser();
                        //Modified for case 3552 - IRB attachments - start
                    /*
                     * UserID to UserName Enhancement - Start
                     * Added UserUtils class to change userid to username
                     */
                        //return UserUtils.getDisplayName(uploadDocBean.getUpdateUser());
                        return uploadDocBean.getUpdateUserName();
                        // UserId to UserName Enhancement - End
                        //Modified for case 3552 - IRB attachments - end
                }
            }
            return EMPTY_STRING;
        }
    }
    /**
     * Class for Upload Document renderer
     */
    private class ProtocolUploadDocRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        
        /**
         * contruttor for Upload Document renderer
         */
        public ProtocolUploadDocRenderer(){
            lblComponent = new CoeusLabel();
            lblComponent.setOpaque(true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            
        }
        /**
         * Method to get table cell component
         * @param table table object
         * @param value component value
         * @param isSelected component state
         * @param hasFocus component focus
         * @param row on which row exist
         * @param col on which column exist
         * @return object component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col){
            switch (col){
                case TYPE_COLUMN:
                case DESCRIPTION_COLUMN:
                    //Commented for Coeus4.3 enhancement
                    //case STATUS_COLUMN:
                case UPDATED_BY_COLUMN:
                case LAST_UPDATED_COLUMN:
                    lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
                    if(col == LAST_UPDATED_COLUMN ){
                        lblComponent.setHorizontalAlignment(CoeusLabel.RIGHT);
                    }
                    lblComponent.setFont(CoeusFontFactory.getNormalFont());
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value = (value==null ? EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                    
            }
            return lblComponent;
        }
    }
    /**
     * Table Model for Parent Document
     */
    private class ProtocolUploadParentTableModel extends AbstractTableModel{
        //Modified for Coeus4.3 enhancement starts
        //included version
        private String []colNames = {"Type","Description", "Last Updated","Updated By"};
        private Class colClass[]={String.class,String.class,String.class,String.class};
        //Modified for Coeus4.3 enhancement ends
        private Vector vecParentListData;
        /**
         * method to make cell editable
         * @param row disable this row
         * @param col disable this column
         * @return boolean for cell editable state
         */
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * method to count total column
         * @return number of column
         */
        public int getColumnCount() {
            return colNames.length;
        }
        /**
         * Method to get column name
         * @param col column number to get name
         * @return name of the column
         */
        public String getColumnName(int col){
            return colNames[col];
        }
        /**
         * method to get column class
         * @param col number of column
         * @return Class of column
         */
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * method to get total rows
         * @return total number of rows
         */
        public int getRowCount() {
            if(vecParentListData == null || vecParentListData.size() == 0){
                return 0;
            }
            return vecParentListData.size();
            
        }
        /**
         * method to set data in table
         * @param vecParentListData contains data for table
         */
        public void setData(Vector vecParentListData){
            this.vecParentListData = vecParentListData;
        }
        /**
         * method to get value
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object value related to this row and column
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(vecParentListData != null && vecParentListData.size() > 0){
                UploadDocumentBean uploadDocBean
                        = (UploadDocumentBean)vecParentListData.get(rowIndex);
                switch(columnIndex){
                    case TYPE_COLUMN:
                        return uploadDocBean.getDocType();
                    case DESCRIPTION_COLUMN:
                        return uploadDocBean.getDescription();
                        //Commented for Coeus 4.3 enhancement -start
                        //Status column is removed
                        //case STATUS_COLUMN:
                        //return uploadDocBean.getStatusDescription();
                        //Commented for Coeus 4.3 enhancement -end
                    case LAST_UPDATED_COLUMN:
                        return CoeusDateFormat.format(uploadDocBean.getUpdateTimestamp().toString());
                    case UPDATED_BY_COLUMN:
//                        return uploadDocBean.getUpdateUser();
                        //Modified for case 3552 - IRB attachments - start
                    /*
                     * UserID to UserName Enhancement - Start
                     * Added UserUtils class to change userid to username
                     */
                        //return UserUtils.getDisplayName(uploadDocBean.getUpdateUser());
                        return uploadDocBean.getUpdateUserName();
                        // UserId to UserName Enhancement - End
                        //Modified for case 3552 - IRB attachments - end
                }
            }
            return EMPTY_STRING;
        }
    }
    
    /**
     * Get the Document Data based on data Type
     * @return List containg data
     * @param parentProtocolNumber parent protocol number
     * @param getDataType data type for getting data or added data
     * @param documentId of the selected document for which the history has to be shown
     * @throws CoeusException If Any exception occurs
     */
    
    public List getDocumentData(char getDataType, String parentProtocolNumber, String documentId)
    throws CoeusException {
        List cvParam = null;
        requester = new RequesterBean();
        if(getDataType == GET_UPLOAD_DOC_DATA){
            requester.setDataObject(protocolInfoBean);
        }else if(getDataType == GET_PARENT_PROTO_DATA) {
            ProtocolInfoBean protocolInfoBean =
                    new ProtocolInfoBean();
            protocolInfoBean.setProtocolNumber(parentProtocolNumber);
            requester.setDataObject(protocolInfoBean);
        }else if (getDataType == GET_PROTO_HISTORY_DATA) {
            //Commented for Coeus 4.3 enhancement
            //requester.setDataObject(protocolInfoBean.getProtocolNumber());
            // Coeus 4.3 enhancement- To show the history for a particular document the documentId is also
            //passed while fetching the documents*/
            Vector vecServerObject = new Vector();
            vecServerObject.add(parentProtocolNumber);
            vecServerObject.add(documentId);
            requester.setDataObject(vecServerObject);
        }
        if(getDataType == GET_PARENT_PROTO_DATA){
            requester.setFunctionType(GET_UPLOAD_DOC_DATA);
        }else{
            requester.setFunctionType(getDataType);
        }
        
        comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        if(getDataType == GET_DOCUMENT_TYPE){
            cvParam = (CoeusVector)responder.getDataObject();
        }else if(getDataType == GET_UPLOAD_DOC_DATA){
            cvParam = (Vector)responder.getDataObject();
        }else if(getDataType == GET_PARENT_PROTO_DATA ) {
            cvParam = (Vector)responder.getDataObject();
        }else if( getDataType == GET_PROTO_HISTORY_DATA) {
            cvParam = (Vector)responder.getDataObject();
        }
        return cvParam;
    }
    /**
     * Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /**
     * Getter for property fileSelected.
     * @return Value of property fileSelected.
     */
    public boolean isFileSelected() {
        return fileSelected;
    }
    
    /**
     * Setter for property fileSelected.
     * @param fileSelected New value of property fileSelected.
     */
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }
    
    /**
     * Getter for property blobData.
     * @return Value of property blobData.
     */
    public byte[] getBlobData() {
        return this.blobData;
    }
    
    /**
     * Setter for property blobData.
     * @param blobData New value of property blobData.
     */
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    /**
     * Getter for property windowType.
     * @return Value of property windowType.
     */
    public java.lang.String getWindowType() {
        return windowType;
    }
    
    /**
     * Setter for property windowType.
     * @param windowType New value of property windowType.
     */
    public void setWindowType(java.lang.String windowType) {
        this.windowType = windowType;
    }
     
    /**
     * Mouse click event
     * @param mouseEvent event for mouse clicked
     */
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
//        This is commented For the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments
//        if( mouseEvent.getClickCount() == 2 ){
//            int row;
//            Object source = mouseEvent.getSource();
//            UploadDocumentBean uploadDocumentBean = null;;
//            if(source.equals(tblListDocument)) {
//                row = tblListDocument.rowAtPoint(mouseEvent.getPoint());
//                if(row == -1 ) {
//                    row = tblListDocument.getSelectedRow();
//                }
//                uploadDocumentBean = (UploadDocumentBean)vecListData.get(row);
//            }else if(source.equals(tblParent) ) {
//                row = tblParent.rowAtPoint(mouseEvent.getPoint());
//                if(row == -1 ) {
//                    row = tblListDocument.getSelectedRow();
//                }
//                //get bean for parent document
//                uploadDocumentBean = (UploadDocumentBean)vecParentLatestData.get(row);
//            }
//            try{
//                viewDocument(uploadDocumentBean);
//            }catch( CoeusException ce ) {
//                ce.printStackTrace();
//            }
//        }
    
    }
    
    /**
     * Method to get mouse entered
     * @param e event for mouse entered
     */
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    /**
     * Method to perform action when mouse exit
     * @param e event for mouse exited
     */
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    /**
     * method to perform mouse event when mouse pressed
     * @param e event for mouse press
     */
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    /**
     * Method to get action when mouse released
     * @param e event for mouse released
     */
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    /**
     * Coeus 4.3 enhancement
     * Disables the change status button according to the status of the document
     * and the protocol selected in table
     */
    public void valueChanged(ListSelectionEvent evt){
        if(evt.getSource().equals(tblListDocument.getSelectionModel())){
            if(tblListDocument.getSelectedRow()!=-1){
                UploadDocumentBean uploadDocBean =
                        (UploadDocumentBean)vecListData.get(tblListDocument.getSelectedRow());
                if(uploadDocBean.getVersionNumber()==1){
                    btnHistory.setEnabled(false);
                }else{
                    btnHistory.setEnabled(true);
                }
                // Disable the buttons if the document status is deleted
                if(uploadDocBean.getStatusCode()==3){
                    btnModify.setEnabled(false);
                    btnView.setEnabled(false);
                    btnDelete.setEnabled(false);
                }else{
                    btnModify.setEnabled(true);
                    btnView.setEnabled(true);
                    btnDelete.setEnabled(true);
                    
                }
            }
        }else if(evt.getSource().equals(tblParent.getSelectionModel())){
            if(tblParent.getSelectedRow()!=-1){
                UploadDocumentBean uploadDocBean =
                        (UploadDocumentBean)vecParentLatestData.get(tblParent.getSelectedRow());
                if(uploadDocBean.getVersionNumber()==1){
                    btnParentHistory.setEnabled(false);
                }else{
                    btnParentHistory.setEnabled(true);
                }
                
                if(uploadDocBean.getStatusCode()==3){
                    btnAmend.setEnabled(false);
                    btnView.setEnabled(false);
                }else{
                    btnAmend.setEnabled(true);
                    btnView.setEnabled(true);
                }
            }
        }
        //Modified for case 3552 - IRB attachments - start
        if(functionType == TypeConstants.DISPLAY_MODE 
                || functionType == TypeConstants.AMEND_MODE){
            //Modified for case 3552 - IRB attachments - end
            //Modified for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_Start
            try {
                if(isRenewal(protocolInfoBean.getProtocolNumber())){
                    btnModify.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnAmend.setEnabled(false);
                }else{
                    btnModify.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnAmend.setEnabled(false);
                }
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
            //Modified for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_End
        }
    }
    //Added for case 3552 - IRB attachments - start
    
    //Added for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_Start
    public boolean isRenewal(String protocolNumber) throws CoeusException{
        boolean isRenewal = false;
        requester = new RequesterBean();
        requester.setFunctionType('K');               
        requester.setDataObject(protocolNumber);
        comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();        
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }else{
            isRenewal = ((Boolean)responder.getDataObject()).booleanValue();
        }
        return isRenewal;
    }
    
    //Added for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_Start
    /**
     * Getter method for property protocolInfoBean
     * @return protocolInfoBean
     */
    public ProtocolInfoBean getProtocolInfoBean() {
        return protocolInfoBean;
    }
    
    /**
     * Setter method for property ProtocolInfoBean
     * @param protocolInfoBean
     */
    public void setProtocolInfoBean(ProtocolInfoBean protocolInfoBean) {
        this.protocolInfoBean = protocolInfoBean;
    }

    /**
     * Setter method for property protocolDetailForm
     * @param protocolDetailForm
     */
    public void setProtocolDetailForm(ProtocolDetailForm protocolDetailForm) {
        this.protocolDetailForm = protocolDetailForm;
    }
    
    /**
     * Shows warning message if the not document types available
     * @return boolean
     */
    public boolean checkDocumentTypeExists(){
        if(cvDocType == null || cvDocType.size() == 0){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1008"));
                return false;
        }
        return true;
    }
    //Added for case 3552 - IRB attachments - end
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only questionnaireId, name and description
     */
    //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
    public class ColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={
            {"0","docType" },
            {"1","description" },
            {"2","updateTimestamp"},
            {"3","updateUserName"},
        };
        boolean sort =true;
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent mouseEvent) {
            try {
                JTable table = ((JTableHeader)mouseEvent.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(mouseEvent.getX());
                CoeusVector cvListData =  new CoeusVector();
                if(vecListData != null && vecListData.size() > 0){
                    for(int index=0; index<vecListData.size();index++){
                        cvListData.add(vecListData.get(index));
                    }
                }
                if(cvListData!=null && cvListData.size()>0 &&
                        nameBeanId [vColIndex][1].length() >1 ){
                    (cvListData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    
                    if(cvListData != null && cvListData.size() > 0){
                        for(int index=0; index<cvListData.size();index++){
                            vecListData.set(index,cvListData.get(index));
                        }
                    }
                    protocolUploadDocTableModel.fireTableRowsUpdated(0, protocolUploadDocTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
        
    }// End of ColumnHeaderListener.................\
      /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only questionnaireId, name and description
     */
    //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
    public class ParentColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={
            {"0","docType" },
            {"1","description" },
            {"2","updateTimestamp"},
            {"3","updateUserName"},
        };
        boolean sort =true;
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent mouseEvent) {
            try {
                JTable table = ((JTableHeader)mouseEvent.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(mouseEvent.getX());
                CoeusVector cvListData =  new CoeusVector();
                if(vecParentLatestData != null && vecParentLatestData.size() > 0){
                    for(int index=0; index<vecParentLatestData.size();index++){
                        cvListData.add(vecParentLatestData.get(index));
                    }
                }
                if(cvListData!=null && cvListData.size()>0 &&
                        nameBeanId [vColIndex][1].length() >1 ){
                    (cvListData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    
                    if(cvListData != null && cvListData.size() > 0){
                        for(int index=0; index<cvListData.size();index++){
                            vecParentLatestData.set(index,cvListData.get(index));
                        }
                    }
                    protocolUploadParentTableModel.fireTableRowsUpdated(0, protocolUploadParentTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
        
    }// End of ColumnHeaderListener.................
     //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnchangeStatus = new javax.swing.JButton();
        pnlParent = new javax.swing.JPanel();
        pnlParentList = new javax.swing.JPanel();
        scrPnParent = new javax.swing.JScrollPane();
        tblParent = new javax.swing.JTable();
        btnParentView = new javax.swing.JButton();
        btnAmend = new javax.swing.JButton();
        btnParentHistory = new javax.swing.JButton();
        pnlUploadDocuments = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        pnlListDocument = new javax.swing.JPanel();
        scrPnListDocument = new javax.swing.JScrollPane();
        tblListDocument = new javax.swing.JTable();
        btnModify = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnShowAll = new javax.swing.JButton();

        btnchangeStatus.setFont(CoeusFontFactory.getLabelFont());
        btnchangeStatus.setMnemonic('H');
        btnchangeStatus.setText("Change Status");
        btnchangeStatus.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnchangeStatus.setMaximumSize(new java.awt.Dimension(110, 22));
        btnchangeStatus.setMinimumSize(new java.awt.Dimension(110, 22));
        btnchangeStatus.setNextFocusableComponent(btnHistory);
        btnchangeStatus.setPreferredSize(new java.awt.Dimension(110, 22));

        setLayout(new java.awt.GridBagLayout());

        pnlParent.setLayout(new java.awt.GridBagLayout());

        pnlParent.setMinimumSize(new java.awt.Dimension(850, 213));
        pnlParent.setNextFocusableComponent(btnView);
        pnlParent.setPreferredSize(new java.awt.Dimension(850, 213));
        pnlParentList.setLayout(new java.awt.GridBagLayout());

        pnlParentList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attachments in Original Protocol", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlParentList.setMinimumSize(new java.awt.Dimension(720, 197));
        pnlParentList.setPreferredSize(new java.awt.Dimension(720, 197));
        scrPnParent.setMaximumSize(new java.awt.Dimension(710, 170));
        scrPnParent.setMinimumSize(new java.awt.Dimension(710, 170));
        scrPnParent.setPreferredSize(new java.awt.Dimension(710, 170));
        tblParent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        tblParent.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnParent.setViewportView(tblParent);

        pnlParentList.add(scrPnParent, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlParent.add(pnlParentList, gridBagConstraints);

        btnParentView.setFont(CoeusFontFactory.getLabelFont());
        btnParentView.setMnemonic('w');
        btnParentView.setText("View");
        btnParentView.setMaximumSize(new java.awt.Dimension(110, 22));
        btnParentView.setMinimumSize(new java.awt.Dimension(110, 22));
        btnParentView.setNextFocusableComponent(btnAmend);
        btnParentView.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 5, 3);
        pnlParent.add(btnParentView, gridBagConstraints);

        btnAmend.setFont(CoeusFontFactory.getLabelFont());
        btnAmend.setMnemonic('n');
        btnAmend.setText("Amend");
        btnAmend.setMaximumSize(new java.awt.Dimension(110, 22));
        btnAmend.setMinimumSize(new java.awt.Dimension(110, 22));
        btnAmend.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        pnlParent.add(btnAmend, gridBagConstraints);

        btnParentHistory.setFont(CoeusFontFactory.getLabelFont());
        btnParentHistory.setMnemonic('i');
        btnParentHistory.setText("History");
        btnParentHistory.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnParentHistory.setMaximumSize(new java.awt.Dimension(110, 22));
        btnParentHistory.setMinimumSize(new java.awt.Dimension(110, 22));
        btnParentHistory.setName("btnParentHistory");
        btnParentHistory.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlParent.add(btnParentHistory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(pnlParent, gridBagConstraints);

        pnlUploadDocuments.setLayout(new java.awt.GridBagLayout());

        pnlUploadDocuments.setMinimumSize(new java.awt.Dimension(850, 265));
        pnlUploadDocuments.setPreferredSize(new java.awt.Dimension(850, 265));
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('l');
        btnDelete.setText("Delete");
        btnDelete.setMargin(new java.awt.Insets(2, 10, 2, 14));
        btnDelete.setMaximumSize(new java.awt.Dimension(110, 22));
        btnDelete.setMinimumSize(new java.awt.Dimension(110, 22));
        btnDelete.setNextFocusableComponent(btnView);
        btnDelete.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(btnDelete, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        btnView.setMaximumSize(new java.awt.Dimension(110, 22));
        btnView.setMinimumSize(new java.awt.Dimension(110, 22));
        btnView.setNextFocusableComponent(btnchangeStatus);
        btnView.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(btnView, gridBagConstraints);

        btnHistory.setFont(CoeusFontFactory.getLabelFont());
        btnHistory.setMnemonic('H');
        btnHistory.setText("History");
        btnHistory.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnHistory.setMaximumSize(new java.awt.Dimension(110, 22));
        btnHistory.setMinimumSize(new java.awt.Dimension(110, 22));
        btnHistory.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(btnHistory, gridBagConstraints);

        pnlListDocument.setLayout(new java.awt.GridBagLayout());

        pnlListDocument.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "New/Changed Attachments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlListDocument.setMinimumSize(new java.awt.Dimension(720, 250));
        pnlListDocument.setPreferredSize(new java.awt.Dimension(720, 250));
        scrPnListDocument.setMaximumSize(new java.awt.Dimension(710, 220));
        scrPnListDocument.setMinimumSize(new java.awt.Dimension(710, 220));
        scrPnListDocument.setPreferredSize(new java.awt.Dimension(710, 220));
        tblListDocument.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnListDocument.setViewportView(tblListDocument);

        pnlListDocument.add(scrPnListDocument, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(pnlListDocument, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('o');
        btnModify.setText("Modify");
        btnModify.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnModify.setMaximumSize(new java.awt.Dimension(110, 22));
        btnModify.setMinimumSize(new java.awt.Dimension(110, 22));
        btnModify.setNextFocusableComponent(btnDelete);
        btnModify.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(btnModify, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnAdd.setMaximumSize(new java.awt.Dimension(110, 22));
        btnAdd.setMinimumSize(new java.awt.Dimension(110, 22));
        btnAdd.setName("btnAdd");
        btnAdd.setNextFocusableComponent(btnModify);
        btnAdd.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 3, 3);
        pnlUploadDocuments.add(btnAdd, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(CoeusFontFactory.getNormalFont());
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(3);
        jTextArea1.setText(coeusMessageResources.parseMessageKey(
            "protocolUpload_exceptionCode.1021"));
    jTextArea1.setMaximumSize(new java.awt.Dimension(650, 5));
    jTextArea1.setMinimumSize(new java.awt.Dimension(650, 5));
    jTextArea1.setOpaque(false);
    jScrollPane1.setViewportView(jTextArea1);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 3);
    pnlUploadDocuments.add(jScrollPane1, gridBagConstraints);

    btnShowAll.setFont(CoeusFontFactory.getLabelFont());
    btnShowAll.setMnemonic('s');
    btnShowAll.setText("Show All");
    btnShowAll.setMaximumSize(new java.awt.Dimension(110, 22));
    btnShowAll.setMinimumSize(new java.awt.Dimension(110, 22));
    btnShowAll.setName("btnShowAll");
    btnShowAll.setPreferredSize(new java.awt.Dimension(110, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
    pnlUploadDocuments.add(btnShowAll, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 3, 5, 3);
    add(pnlUploadDocuments, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAmend;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnParentHistory;
    private javax.swing.JButton btnParentView;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JButton btnView;
    private javax.swing.JButton btnchangeStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel pnlListDocument;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlParentList;
    private javax.swing.JPanel pnlUploadDocuments;
    private javax.swing.JScrollPane scrPnListDocument;
    private javax.swing.JScrollPane scrPnParent;
    private javax.swing.JTable tblListDocument;
    private javax.swing.JTable tblParent;
    // End of variables declaration//GEN-END:variables
    
}
