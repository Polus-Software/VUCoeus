/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardDocumentController.java
 *
 * Created on October 3, 2007, 3:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardDocumentBean;
import edu.mit.coeus.award.gui.AwardDocumentForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// JM 10-8-2012 needed for scrolling
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
// JM END
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author divyasusendran
 */
public class AwardDocumentController extends AwardController implements ActionListener, ListSelectionListener{
    
    private JScrollPane jscrPn;
    private AwardDocumentForm awardDocumentForm ;
    private AwardAddDocumentController awardAddDocumentController;
    private static final int DOCUMENTS = 0;
    // JM added sequence number to display; renumbered columns
    private static final int SEQ_COLUMN = 1;
    private static final int TYPE_COLUMN = 2;
    private static final int DESCRIPTION_COLUMN = 3;
    private static final int LAST_UPDATED_COLUMN = 4;
    private static final int UPDATED_BY_COLUMN = 5;
    // JM END
    private static final String AWARD_MAINTENANCE_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/AwardMaintenanceServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String EMPTY_STRING ="";
    private static final char GET_AWARD_UPLOAD_DOC_DATA = 'o';
    private static final char ADD_VOID_AWARD_DOC_DATA = 'p';
    private static final char VIEW_AWARD_DOC_DATA = 'q';
    private static final char UPD_DEL_AWARD_UPLOAD_DOC_DATA = 'd';
    boolean canAddAttachment = false;
    private RequesterBean requester;
    private char performType = 'M';
    // 4385: User with View_award_documents role is not able to view documents in a dept
    private static final char GET_DOCUMENT_RIGHTS = 'n';
    private CoeusAppletMDIForm mdiForm;
    private AwardDocumentTableModel awardDocumentTableModel;
    private AwardDocumentRenderer awardDocumentRenderer;
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private CoeusVector vecAwardDocList = new CoeusVector();
    private CoeusVector vecActiveDoc = new CoeusVector();
    private boolean filterActiveDocs = true;
    private byte[] blobData;
    private boolean userHasModifyAward;
    private boolean userHasViewAward;
    private boolean canModifyAttachment;
    private boolean canViewAttachment;
    private AppletServletCommunicator appletServletCommunicator;
    private CoeusVector vecListData;
    boolean isFired = false;
    /**
     * Creates a new instance of AwardDocumentController
     */
    public AwardDocumentController(AwardBaseBean awardBaseBean,
            CoeusAppletMDIForm mdiForm, char functionType) {
        super(awardBaseBean);
        setFunctionType(functionType);
        queryEngine = QueryEngine.getInstance();
        this.mdiForm = mdiForm;
        awardDocumentForm = new AwardDocumentForm();
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(awardDocumentForm);
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        getAwardDocRights();
        postInitComponents();
        registerComponents();
        setTableEditors();
        setFormData((CoeusVector)getDocData());
    }
    
    /**
     * This method is used to get the rights required in Award Document
     * @param
     * @return hashtable which contains boolean values for
     * userHasModifyAward,userHasViewAward,canModifyAttachment,canViewAttachment
     */
    private void getAwardDocRights(){
        // 4385: User with View_award_documents role is not able to view documents in a dept - Start
//        Hashtable htAwdDocRights = new Hashtable();
//        AwardListController controller = new AwardListController();
//        htAwdDocRights = controller.getAwardDocumentRights();
//        userHasModifyAward = new Boolean(htAwdDocRights.get("AwardModify").toString()).booleanValue();
//        userHasViewAward = new Boolean(htAwdDocRights.get("AwardView").toString()).booleanValue();
//        canModifyAttachment = new Boolean(htAwdDocRights.get("AwardMaintainDoc").toString()).booleanValue();
//        canViewAttachment = new Boolean(htAwdDocRights.get("AwardViewDoc").toString()).booleanValue();
        
        RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        Vector dataObjects = new Vector();
        dataObjects.add(awardBaseBean.getMitAwardNumber());
        dataObjects.add(String.valueOf(getFunctionType()));
        requester.setDataObjects(dataObjects);
        requester.setFunctionType(GET_DOCUMENT_RIGHTS);
        AppletServletCommunicator comm = new AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            Hashtable htDocumentRights = (Hashtable)responder.getDataObject();
            if(htDocumentRights != null){
                userHasModifyAward = new Boolean(htDocumentRights.get("userHasModifyAward").toString()).booleanValue();
                userHasViewAward = new Boolean(htDocumentRights.get("userHasViewAward").toString()).booleanValue();
                canModifyAttachment = new Boolean(htDocumentRights.get("canModifyAttachment").toString()).booleanValue();
                canViewAttachment = new Boolean(htDocumentRights.get("canViewAttachment").toString()).booleanValue();
            }
        }
        // 4385: User with View_award_documents role is not able to view documents in a dept - End
    }
    
    public Component getControlledUI() {
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrPn = new JScrollPane(awardDocumentForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrPn;
    }
    
    /**
     * This method is used to set the data
     * @param a CoeusVector Object which contains the both Active and Void documents depending whether
     * the documents have been filtered or not
     */
    //this method to be used for setting data only , no filtering of docs done here
    public void setFormData(Object data){
        //Added for the case#COEUSDEV-301 -Awd List>Scrolling thru List in Display mode-start
        //getting the awardDocumnet bean and setting the data in form
        if(!(data instanceof CoeusVector)){
            data = getDocData();
        }
        //Added for the case#COEUSDEV-301 - Awd List>Scrolling thru List in Display mode-end
        try{
            CoeusVector vecActDoc = (CoeusVector)data;           
            if(vecAwardDocList.size() == 0){
                awardDocumentForm.btnShowAll.setVisible(true);
                awardDocumentForm.btnShowAll.setEnabled(false);
                awardDocumentForm.btnVoid.setEnabled(false);
                // JM 3-11-2013 VU still wants this feature
                awardDocumentForm.btnDelete.setEnabled(false);
                awardDocumentForm.btnModify.setEnabled(false);
            }else{
                if(!awardDocumentForm.btnShowActive.isEnabled()){
                    awardDocumentForm.btnShowAll.setVisible(true);
                    awardDocumentForm.btnShowAll.setEnabled(true);
                    // JM 3-11-2013 VU still wants this feature
                    awardDocumentForm.btnDelete.setEnabled(true);
                    awardDocumentForm.btnModify.setEnabled(true);
                }else{
                    awardDocumentForm.btnShowActive.setVisible(true);
                    awardDocumentForm.btnShowActive.setEnabled(true);
                    // JM 3-11-2013 VU still wants this feature
                    awardDocumentForm.btnDelete.setEnabled(true);
                }
                // 4385: User with View_award_documents role is not able to view documents in a dept -Start
//                awardDocumentForm.btnVoid.setEnabled(userHasModifyAward && canModifyAttachment);
                awardDocumentForm.btnModify.setEnabled(canModifyAwardDocument()); /*Added for COEUSQA-4157*/
                awardDocumentForm.btnVoid.setEnabled(canModifyAwardDocument());
                // 4385: User with View_award_documents role is not able to view documents in a dept - End
            }
            awardDocumentTableModel.setData(vecActDoc);
            isFired = true;
            awardDocumentTableModel.fireTableDataChanged();
            if( awardDocumentForm.tblListDocument.getRowCount() > 0){
                awardDocumentForm.tblListDocument.setRowSelectionInterval(0,0);
                awardDocumentForm.tblListDocument.setColumnSelectionInterval(0,0);
                AwardDocumentBean awardDocumentBean =(AwardDocumentBean)vecActiveDoc.get(0);
                if(awardDocumentBean.getDocStatusCode().equalsIgnoreCase("V")){
                    awardDocumentForm.btnVoid.setEnabled(false);
                }else{
                    if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                        // 4385: User with View_award_documents role is not able to view documents in a dept -Start
//                        awardDocumentForm.btnVoid.setEnabled(userHasModifyAward && canModifyAttachment);
                        awardDocumentForm.btnVoid.setEnabled(canModifyAwardDocument());
                        // 4385: User with View_award_documents role is not able to view documents in a dept - End
            }else{
                awardDocumentForm.btnVoid.setEnabled(false);
            }            
            
                }
            } else {
                awardDocumentForm.btnVoid.setEnabled(false);
            }
            isFired = false;
            awardDocumentForm.btnAdd.requestFocusInWindow();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Object getFormData() {
        return null;
    }
    
    /**
     * This method is used to get the document list data from queryEngine
     * @return Vector object which contains documents
     */
    private Object getDocData(){
        try{
            vecAwardDocList = queryEngine.getDetails(queryKey,AwardDocumentBean.class);
            vecActiveDoc = filterDocuments(vecAwardDocList);            
        }catch(Exception e){
            e.printStackTrace();
        }
        return vecActiveDoc;
    }
    
    public void formatFields() {
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    private void postInitComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        screenFocusTraversal();
    }
    
    /**
     * This method is used to set the traversal of components through
     * components present in the form
     */
    private void screenFocusTraversal(){
        ScreenFocusTraversalPolicy traversePolicy;
        java.awt.Component[] components = { awardDocumentForm.btnAdd,
		// JM 3-11-2013 VU still wants this feature
		awardDocumentForm.btnDelete,
        awardDocumentForm.btnModify,
        awardDocumentForm.btnVoid,
        awardDocumentForm.btnShowActive,
        awardDocumentForm.btnShowAll};
        traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardDocumentForm.setFocusTraversalPolicy(traversePolicy);
        awardDocumentForm.setFocusCycleRoot(true);
    }
    
    /**
     * This method is used to register components
     */
    public void registerComponents() {
        setEnableDisableComponents();
        awardDocumentForm.btnVoid.addActionListener(this);
        awardDocumentForm.btnShowAll.addActionListener(this);
        awardDocumentForm.btnAdd.addActionListener(this);
        awardDocumentForm.btnShowActive.addActionListener(this);
        // JM 3-11-2013 VU still wants this feature
        awardDocumentForm.btnDelete.addActionListener(this);
        awardDocumentForm.btnModify.addActionListener(this);
        awardDocumentTableModel = new AwardDocumentTableModel();
        awardDocumentRenderer = new AwardDocumentRenderer();
        awardDocumentForm.tblListDocument.setModel(awardDocumentTableModel);
        awardDocumentForm.tblListDocument.getSelectionModel().addListSelectionListener(this);
    }
    
    /**
     * This method is used to enable/disable/visible depending on the
     * function type and different rights of the logged in user
     */
    private void setEnableDisableComponents(){
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            // 4385: User with View_award_documents role is not able to view documents in a dept - Start
//            awardDocumentForm.btnAdd.setEnabled(userHasModifyAward && canModifyAttachment);
            awardDocumentForm.btnAdd.setEnabled(canModifyAwardDocument());
            // 4385: User with View_award_documents role is not able to view documents in a dept - End
            awardDocumentForm.btnShowActive.setVisible(false);
            awardDocumentForm.btnShowActive.setEnabled(false);
//            awardDocumentForm.btnDelete.setEnabled(false);
            // 4385: User with View_award_documents role is not able to view documents in a dept - Start
//            awardDocumentForm.btnVoid.setEnabled(userHasModifyAward && canModifyAttachment);
            awardDocumentForm.btnVoid.setEnabled(canModifyAwardDocument());
            // 4385: User with View_award_documents role is not able to view documents in a dept - End
        }else{
            awardDocumentForm.btnAdd.setEnabled(false);
            awardDocumentForm.btnShowActive.setVisible(false);
            awardDocumentForm.btnShowActive.setEnabled(false);
            awardDocumentForm.btnVoid.setEnabled(false);
            // JM 3-11-2013 VU still wants this feature
            awardDocumentForm.btnDelete.setEnabled(false);
            awardDocumentForm.btnModify.setEnabled(false);
        }
        
    }
    public void saveFormData() throws CoeusException {
    }
    
    public void display() {
    }
    /**
     * set The Award Document editor and headers
     */
    private void setTableEditors() {        
        JTableHeader tableHeader = awardDocumentForm.tblListDocument.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setMaximumSize(new Dimension(100, 25));
        tableHeader.setMinimumSize(new Dimension(100, 25));
        tableHeader.setPreferredSize(new Dimension(100, 25));
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        awardDocumentForm.tblListDocument.setRowHeight(22);
        awardDocumentForm.tblListDocument.setShowHorizontalLines(true);
        awardDocumentForm.tblListDocument.setShowVerticalLines(true);
        awardDocumentForm.tblListDocument.setOpaque(false);
        awardDocumentForm.tblListDocument.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
         awardDocumentForm.tblListDocument.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        awardDocumentForm.tblListDocument.getTableHeader().addMouseListener(new ColumnHeaderListener());
        awardDocumentForm.tblListDocument.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);        
        TableColumn columnDetails;
        // JM added sequence column to display table
        int []size = {20,40,130,300,130,140};
        // JM END
        for(int index = 0; index < size.length; index++) {
            columnDetails = awardDocumentForm.tblListDocument.getColumnModel().getColumn(index);
            columnDetails.setPreferredWidth(size[index]);
            if(index == DOCUMENTS) {
                columnDetails.setHeaderRenderer(new EmptyHeaderRenderer());
		columnDetails.setPreferredWidth(20);
		columnDetails.setResizable(false);                
                columnDetails.setCellEditor(new AwardDocumentTableEditor());
            }
            columnDetails.setCellRenderer(awardDocumentRenderer);
        }
    }
    
    
     class AwardDocumentTableEditor extends DefaultCellEditor implements ActionListener{
        
        private JButton btnDocument;
        int row;
        private ImageIcon  pdfIcon;
        AwardDocumentTableEditor() {
            super(new JComboBox());
            //Commented/Added for case#2156 - start
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
            //Commented/Added for case#2156 - end
            // #case 3855 Modified to set attachment specific icon
            btnDocument = new JButton();
            btnDocument.addActionListener(this);             
          }
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            AwardDocumentBean awardDocumentBean = (AwardDocumentBean)vecActiveDoc.elementAt(row);
            switch(column){
                case DOCUMENTS:
                    //Case 4007: Icon based on mime Type
                    // # case 3855 start Added attachment specific icon
//                    String fileExtension = UserUtils.getFileExtension(value.toString());
//                    btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                    // # case 3855 end
                    CoeusDocumentUtils coeusDocumentUtils  = CoeusDocumentUtils.getInstance();
                    btnDocument.setIcon(coeusDocumentUtils.getAttachmentIcon(awardDocumentBean));
                    //4007 end
                    if(awardDocumentBean.getDocStatusCode().equals("V")){
                        btnDocument.setEnabled(false);
                        return btnDocument;
                    }else {
                        // 4385: User with View_award_documents role is not able to view documents in a dept - Start
//                        btnDocument.setEnabled((userHasModifyAward ||userHasViewAward) &&(canViewAttachment  || canModifyAttachment));
                        btnDocument.setEnabled(canViewAwardDocument());
                        // 4385: User with View_award_documents role is not able to view documents in a dept - End
                        return btnDocument;
                    }
            }
            return btnDocument;
        }
        public void actionPerformed(ActionEvent actionEvent) {
            if( btnDocument.equals(actionEvent.getSource())){
                this.stopCellEditing();
                performViewDocument();
            }             
        }
    }
    
    
    private class AwardDocumentTableModel extends AbstractTableModel{
    	// JM added sequence column to display table
        private String [] colNames = {".","Seq","Type","Description","Last Updated","Updated By"};
        private Class colClass[]={Boolean.class,Integer.class,String.class,String.class,String.class,String.class};
        // JM END
        private CoeusVector vecListData1;
        /**
         * method to make cell editable
         * @param row disable this row
         * @param col disable this column
         * @return boolean for cell editable state
         */
        public boolean isCellEditable(int row, int col){
            if(col == DOCUMENTS){
                return true;
            }
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
            if(vecListData1 == null || vecListData1.size() == 0){
                return 0;
            }
            return vecListData1.size();
            
        }
        /**
         * method to set data in table
         * @param vecListData contains data for table
         */
        public void setData(CoeusVector vecListData){
            this.vecListData1 = vecListData;
        }
        /**
         * method to get value
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object value related to this row and column
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(vecListData1 != null && vecListData1.size() > 0){
                AwardDocumentBean awardDocumentBean
                        = (AwardDocumentBean)vecListData1.get(rowIndex);
                switch(columnIndex){
                    case DOCUMENTS:
                        return awardDocumentBean.getFileName();
// JM added sequence column to display table
                    case SEQ_COLUMN:
                        return awardDocumentBean.getSequenceNumber();
// JM END
                    case TYPE_COLUMN:
                        return awardDocumentBean.getDocumentTypeDescription();
                    case DESCRIPTION_COLUMN:
                        return awardDocumentBean.getDescription();
                    case LAST_UPDATED_COLUMN:
                        return awardDocumentBean.getUpdateTimestamp().toString();
                    case UPDATED_BY_COLUMN:
                        return awardDocumentBean.getUpdateUserName();
                }
            }
            return EMPTY_STRING;
        }
    }
    
    private class AwardDocumentRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        private JButton btnDocument;        
        private ImageIcon  pdfIcon;
        
        /**
         * constructor for Award Document renderer
         */
        public AwardDocumentRenderer(){
            lblComponent = new CoeusLabel();
            lblComponent.setOpaque(true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            //Commented/Added for case#2156 - start
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
            //Commented/Added for case#2156 - end
            //#Csae 3855 - modified to set attachment specific icon.
            btnDocument = new JButton();           
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
            
            AwardDocumentBean awardDocumentBean
                    = (AwardDocumentBean)vecActiveDoc.get(row);
            switch (col){
                case DOCUMENTS:
                    //Case 4007: Icon based on mime Type
                    // # case 3855 start Added attachment specific icon
//                    String fileExtension = UserUtils.getFileExtension(value.toString());
//                    btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                    // # case 3855 end
                    CoeusDocumentUtils coeusDocumentUtils  = CoeusDocumentUtils.getInstance();
                    btnDocument.setIcon(coeusDocumentUtils.getAttachmentIcon(awardDocumentBean));
                    //4007 end
                    if(awardDocumentBean.getDocStatusCode().equals("V")){
                        btnDocument.setEnabled(false);
                        return btnDocument;
                    }else {
                        // 4385: User with View_award_documents role is not able to view documents in a dept - Start
//                        btnDocument.setEnabled((userHasModifyAward ||userHasViewAward) &&(canViewAttachment  || canModifyAttachment));
                        btnDocument.setEnabled(canViewAwardDocument());
                        // 4385: User with View_award_documents role is not able to view documents in a dept - End
                         return btnDocument;
                    }
// JM added sequence column
                case SEQ_COLUMN:
// JM END
                case TYPE_COLUMN:
                case DESCRIPTION_COLUMN:
                case UPDATED_BY_COLUMN:
                case LAST_UPDATED_COLUMN:
                    lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
                    if(col == LAST_UPDATED_COLUMN ){
                        lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
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
    
    
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        setRefreshRequired(false);
        setFormData(vecActiveDoc);
    }
    
    /**
     * This method is used to route to the corresponding method
     * depending on the action performed
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            if( source.equals(awardDocumentForm.btnAdd)) {
                if(getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD 
                        || getFunctionType() == NEW_CHILD_COPIED|| getFunctionType() == NEW_ENTRY){
                    CoeusOptionPane.showInfoDialog("Please save award");
                }else{
                    performAddDocument();
                }                
            }else if (source.equals(awardDocumentForm.btnVoid)){
                if(getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD 
                        || getFunctionType() == NEW_CHILD_COPIED|| getFunctionType() == NEW_ENTRY){
                    CoeusOptionPane.showInfoDialog("Please save award");
                }else{
                    performVoidDocument();
                }
            // JM 3-11-2013 VU still wants this feature
            } else if ( source.equals( awardDocumentForm.btnDelete )) {
                executeDeleteAction();
                vecAwardDocList = getUpdatedAwardData();

                if(awardDocumentForm.btnShowActive.isEnabled()){
                    vecActiveDoc = vecAwardDocList;
                } else{
                    vecActiveDoc = filterDocuments(vecAwardDocList);
                }
                setFormData(vecActiveDoc);
//              setFormData(vecAwardList);
//              refresh();
            // JM END
            } else if( source.equals(awardDocumentForm.btnModify)) {                   
                    performModifyDocument();
                    setFormData(vecAwardDocList);
                    awardDocumentTableModel.fireTableRowsUpdated(0, vecActiveDoc.size());
                    awardDocumentTableModel.setData(vecActiveDoc);


            }else if (source.equals(awardDocumentForm.btnShowAll)){
                performShowAllActiveDocument(true);            
            }else if(source.equals(awardDocumentForm.btnShowActive)){
                performShowAllActiveDocument(false);
            }
        }catch(Exception ce){
            CoeusOptionPane.showErrorDialog(ce.getMessage());
            ce.printStackTrace();
        }
    }

    /**
     * Case for Award Attachment Modification Process.
     * This method used to delete the Award Upload Document.
     * @param No arguments
     * @throws Exception, CoeusException if the instance of a dbEngine is null.
   */
    private void executeDeleteAction() throws CoeusException, Exception{
        int rowCount =awardDocumentForm.tblListDocument.getRowCount();
        boolean newVersion = false;
        boolean newDocument = false;
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("Award_exceptionCode.1000"));
            return;
        }
        int selectedRow = awardDocumentForm.tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("Award_exceptionCode.1001"));
            return;
        }
       
        AwardDocumentBean awardDocumentBean = (AwardDocumentBean) awardDocumentTableModel.vecListData1.get(selectedRow);
       // case we can add active award document filter here..
        canAddAttachment = true;
        if(canAddAttachment){

            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);

            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                newVersion = true;
                newDocument = false;
                //If the status of the document is draft, delete the record from db
                //else insert a record with status deleted, and blob data null
                if(awardDocumentBean.getDocumentTypeCode()==1){
                    awardDocumentBean.setAcType(TypeConstants.DELETE_RECORD);
                }
                boolean isSuccess = addUpdDocument(awardDocumentBean, newVersion, newDocument);
                if(isSuccess){
                    //  getUpdatedAwardData();
                    //refresh();
                }
            }
        }
    }

  /**
     * Case for Award Attachment Modification Process.
     * This method used to Modify the Award Upload Document.     
     * @throws CoeusException if the instance of a dbEngine is null.
   */
  private void performModifyDocument() throws CoeusException {

      int rowCount =awardDocumentForm.tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("Award_exceptionCode.1000"));
            return;
        }
        int selectedRow = awardDocumentForm.tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("Award_exceptionCode.1001"));
            return;
        }
        AwardDocumentBean selectedAwardDocumentBean = (AwardDocumentBean) awardDocumentTableModel.vecListData1.get(selectedRow);
        selectedAwardDocumentBean.setAcType(TypeConstants.UPDATE_RECORD);
        performType = TypeConstants.MODIFY_MODE;
        awardAddDocumentController = new AwardAddDocumentController(awardBaseBean,mdiForm,selectedAwardDocumentBean,performType);
        // Default case
        // awardAddDocumentController = new AwardAddDocumentController(awardBaseBean,mdiForm);
        awardAddDocumentController.showWindow();
        if(!awardAddDocumentController.isOkClicked()){
            return;
        }
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_AWARD_UPLOAD_DOC_DATA);
        requesterBean.setDataObject(selectedAwardDocumentBean);
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }else{

            vecAwardDocList = (CoeusVector)responder.getDataObject();
            if(!awardDocumentForm.btnShowActive.isEnabled()){
                vecActiveDoc = filterDocuments(vecAwardDocList);
            } else{
                vecActiveDoc = vecAwardDocList;
            }
            setFormData(vecActiveDoc);
        }
    }

  /**
     * Case for Award Attachment Modification Process.
     * This method used to delete the Award Upload Document.
     * @param Vector of AwardDocumentBean, boolean newVersion and newDocumentId
     * @return boolean value
     * @throws DBException, CoeusException if the instance of a dbEngine is null.
   */
  private boolean addUpdDocument(AwardDocumentBean awardDocumentBean, boolean newVersion,
          boolean newDocumentId) throws CoeusException{

        requester = new RequesterBean();
        requester.setFunctionType(UPD_DEL_AWARD_UPLOAD_DOC_DATA);

        requester.setDataObject(awardDocumentBean);

        Vector vecServerObjects = new Vector();
        vecServerObjects.add(0,awardDocumentBean);
        vecServerObjects.add(1, new Boolean(newDocumentId));//new document id
        vecServerObjects.add(2, new Boolean(newVersion));// new version number
        appletServletCommunicator = new
                AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requester);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();

        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        return responder.isSuccessfulResponse();
  }
   
    /**
     * In this method a server call is made to get the list of updated documents
     * @return Vector Object which contains updated award document list
     */
    private CoeusVector getUpdatedAwardData(){
        RequesterBean requesterBean = new RequesterBean();
        AwardDocumentBean awardDocumentBean =  new AwardDocumentBean();
        awardDocumentBean.setAwardNumber(awardBaseBean.getMitAwardNumber());
        awardDocumentBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        requesterBean.setDataObject(awardDocumentBean);
        requesterBean.setFunctionType(GET_AWARD_UPLOAD_DOC_DATA);
        appletServletCommunicator = new
                AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();        
        if(!responder.isSuccessfulResponse()){
            try {
                throw new CoeusException(responder.getMessage(),0);
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
        }
        vecAwardDocList = (CoeusVector)responder.getDataObject();
        vecAwardDocList = vecAwardDocList != null && vecAwardDocList.size() >0 ? vecAwardDocList : new CoeusVector();
        return vecAwardDocList;
    }
    /**
     * Method to Add documents
     * Add window is called ,document details are entered and saved     
     */
    private void performAddDocument() throws CoeusException {
        AwardDocumentBean awardDocumentBean =  new AwardDocumentBean();
        awardDocumentBean.setAwardNumber(awardBaseBean.getMitAwardNumber());
        awardDocumentBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        performType = TypeConstants.ADD_MODE;
        awardAddDocumentController = new AwardAddDocumentController(awardBaseBean,mdiForm,null,performType);
        awardAddDocumentController.showWindow();
        if(!awardAddDocumentController.isOkClicked()){
            return;
        }
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_AWARD_UPLOAD_DOC_DATA);
        requesterBean.setDataObject(awardDocumentBean);
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }else{
            
            vecAwardDocList = (CoeusVector)responder.getDataObject();
            if(!awardDocumentForm.btnShowActive.isEnabled()){
                vecActiveDoc = filterDocuments(vecAwardDocList);                
            } else{
                vecActiveDoc = vecAwardDocList;
            }
            setFormData(vecActiveDoc);
        }
    }
    
    
    /**
     * Method used for both show all and show active actions,
     * used for displaying 'Active' as well 'Void' documents 
     */
    private void performShowAllActiveDocument(boolean showDocuments){
        // if showDocuments is true perform Show All, else show Active
        if(showDocuments){
            if(vecAwardDocList.size()>0){
                awardDocumentForm.btnShowActive.setVisible(true);
                awardDocumentForm.btnShowActive.setEnabled(true);
                awardDocumentForm.btnShowAll.setVisible(false);
                awardDocumentForm.btnShowAll.setEnabled(false);
                vecActiveDoc = vecAwardDocList;                
            }
        }else{
            awardDocumentForm.btnShowActive.setVisible(false);
            awardDocumentForm.btnShowActive.setEnabled(false);
            awardDocumentForm.btnShowAll.setVisible(true);
            awardDocumentForm.btnShowAll.setEnabled(true);
            vecActiveDoc = filterDocuments(vecAwardDocList);            
        }
        setFormData(vecActiveDoc);
    }
    /**
     * Method for viewing an Active document
     * If a Void document is selected, then PDF icon is disabled
     * BLOB data for the selected row is obtained by making a server call
     */
    private void performViewDocument(){
        try{
            boolean blobDataAvailable = false;
            int rowCount = awardDocumentForm.tblListDocument.getRowCount();
            int selectedRow = awardDocumentForm.tblListDocument.getSelectedRow();
            AwardDocumentBean awardDocumentBean =(AwardDocumentBean)vecActiveDoc.get(selectedRow);
            if(!awardDocumentBean.getDocStatusCode().equalsIgnoreCase("V")){
                // getting blob data for viewing
                RequesterBean requesterBean = new RequesterBean();
                requesterBean.setDataObject(awardDocumentBean);
                requesterBean.setFunctionType(VIEW_AWARD_DOC_DATA);
                appletServletCommunicator = new
                        AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requesterBean);
                appletServletCommunicator.send();
                ResponderBean responder = appletServletCommunicator.getResponse();                
                if(!responder.isSuccessfulResponse()){
                    blobDataAvailable = false;
                    throw new CoeusException(responder.getMessage(),0);
                }else{
                    blobDataAvailable = true;
                    awardDocumentBean = (AwardDocumentBean)responder.getDataObject();
                }
                if(blobDataAvailable){
                    viewDocumentData(awardDocumentBean);
                }else{
                    CoeusOptionPane.showErrorDialog("awardDocuments_NoDocsToView_exceptionCode.1111");
                    return;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /**
     * Method for viewing the BLOB
     * @param awardDocumentBean which has the awardNumber,sequenceNumber
     * fileName and document for viewing on the browser
     * @return String the url to display the selected file
     */
    
    private void viewDocumentData(AwardDocumentBean awardDocumentBean){
        try{
            CoeusVector cvDataObject = new CoeusVector();
            HashMap hmDocumentDetails = new HashMap();
            hmDocumentDetails.put("awardNumber", awardDocumentBean.getAwardNumber());
            hmDocumentDetails.put("sequenceNumber", EMPTY_STRING+awardDocumentBean.getSequenceNumber());
            hmDocumentDetails.put("fileName", awardDocumentBean.getFileName());
            hmDocumentDetails.put("document", awardDocumentBean.getDocument());
            cvDataObject.add(hmDocumentDetails);
            RequesterBean requesterBean = new RequesterBean();
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", cvDataObject);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.award.AwardDocumentReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("MODULE_NAME", "VIEW_DOCUMENT");
            documentBean.setParameterMap(map);
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);            
            appletServletCommunicator = new
                    AppletServletCommunicator(STREAMING_SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();            
            if(!responder.isSuccessfulResponse()){
                throw new CoeusException(responder.getMessage(),0);
            }
            map = (Map)responder.getDataObject();
            String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
            if(url == null || url.trim().length() == 0 ) {
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1009"));
                return;
            }
            url = url.replace('\\', '/') ;
            try{
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }catch( Exception ue) {
                ue.printStackTrace() ;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Method to Void a document
     */
    private void performVoidDocument()throws Exception {
        boolean voidDoc = false;
        int rowCount = awardDocumentForm.tblListDocument.getRowCount();
        int selectedRow = awardDocumentForm.tblListDocument.getSelectedRow();
        CoeusVector cvData = (CoeusVector) ObjectCloner.deepCopy(vecActiveDoc);
        AwardDocumentBean awardDocumentBean =(AwardDocumentBean)cvData.get(selectedRow);
        if(!awardDocumentBean.getDocStatusCode().equalsIgnoreCase("V")){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("awardDocuments_StatusChange_exceptionCode.1110"),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                voidDoc = true;
                awardDocumentBean.setAcType("U");
                awardDocumentBean.setDocStatusCode("V");
                awardDocumentBean.setDocStatusDescription("VOID");
            }
        }
        if(voidDoc){
            boolean success = updateAwardDocument(awardDocumentBean);
            if(success){
                filterActiveDocs = true;
                vecAwardDocList = getUpdatedAwardData();
                if(awardDocumentForm.btnShowActive.isEnabled()){
                    vecActiveDoc = vecAwardDocList;
                } else{
                    vecActiveDoc = filterDocuments(vecAwardDocList);
                }
                setFormData(vecActiveDoc);
            }
        }
    }
    
    /**
     * Method to update the status of the document from Active to Void
     * @param awardDocumentBean which contains awardNumber,sequenceNumber
     * and document id
     * @return boolean value indicating succesful updation or a failure
     */
    private boolean updateAwardDocument(AwardDocumentBean awardDocumentBean){
        boolean success = false;
        try{
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(ADD_VOID_AWARD_DOC_DATA);
            requesterBean.setDataObject(awardDocumentBean);
            AppletServletCommunicator appletServletCommunicator = new
                    AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            if(!responder.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(responder.getMessage());
                throw new CoeusException(responder.getMessage(),0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        success = true;
        return success;
    }
        
    /**
     * Method used for filtering the documents in the vector vecAwdDocList
     * @param Vector vecAwdDocList conatining the list of all documents     
     * @return Vector containing only Active documents 
     */
    private CoeusVector filterDocuments(CoeusVector vecAwdDocList){
        CoeusVector vecAwdDoc = new CoeusVector();
        if(vecAwdDocList != null && vecAwdDocList.size()>0){
            for(int index =0;index<vecAwdDocList.size() ;index++){
                AwardDocumentBean awardDocumentBean = (AwardDocumentBean)vecAwdDocList.get(index);
                if(awardDocumentBean.getDocStatusCode() != null && awardDocumentBean.getDocStatusCode().equalsIgnoreCase("A")){
                    vecAwdDoc.add(awardDocumentBean);
                }
            }
        }
        return vecAwdDoc;
    }
    
    public void valueChanged(ListSelectionEvent e) {
//         sortOnDisplay();
//        awardDocumentTableModel.setData(vecAwardDocList);
//       awardDocumentTableModel.fireTableDataChanged(); // commment for sorting error
         
        int rowCount = awardDocumentForm.tblListDocument.getRowCount();
        if(isFired){
            return;
        }
         int selectedRow = awardDocumentForm.tblListDocument.getSelectedRow();
        AwardDocumentBean awardDocumentBean =(AwardDocumentBean)vecActiveDoc.get(selectedRow);
        if(awardDocumentBean.getDocStatusCode().equalsIgnoreCase("V")){
            
            awardDocumentForm.btnVoid.setEnabled(false);
        }else{
            if(getFunctionType() != TypeConstants.DISPLAY_MODE){                
                // 4385: User with View_award_documents role is not able to view documents in a dept - Start
//                awardDocumentForm.btnVoid.setEnabled(userHasModifyAward && canModifyAttachment);
                awardDocumentForm.btnVoid.setEnabled(canModifyAwardDocument());
                // 4385: User with View_award_documents role is not able to view documents in a dept - End
             }else{
                awardDocumentForm.btnVoid.setEnabled(false);
            }            
            
        }

//        sortOnDisplay();
//        awardDocumentTableModel.setData(vecAwardDocList);
//        // Case 3774: Sorting Error found..
//       // awardDocumentTableModel.fireTableDataChanged(); // commment for sorting error
    }
    
    
    public byte[] getBlobData() {
        return blobData;
    }
    
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    
    public void cleanUp(){
        awardDocumentForm = null;
        awardAddDocumentController = null;
        mdiForm = null;
    }

          /**
	 * To display the fields on sorting order depends on the code
	 */
	private void sortOnDisplay() {
		if (vecAwardDocList != null && vecAwardDocList.size() > 0) {
			vecAwardDocList.sort("updateTimeStamp", false);
		}
    }

         public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","documentId"},
            {"1","documentTypeDescription"},
            {"2","description"},
            {"3","updateTimeStamp"},
            {"4","updateUser"}

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
                if(vecActiveDoc != null && vecActiveDoc.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                     ((CoeusVector)vecActiveDoc).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    awardDocumentTableModel.fireTableRowsUpdated(
                                            0, awardDocumentForm.tblListDocument.getRowCount());
                }
                awardDocumentTableModel.setData(vecActiveDoc);
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................

    
    // 4385: User with View_award_documents role is not able to view documents in a dept - Start
    private boolean canViewAwardDocument(){
        return (canViewAttachment  || canModifyAttachment);
    }
    
    private boolean canModifyAwardDocument(){
        return (userHasModifyAward && canModifyAttachment);
    }
    // 4385: User with View_award_documents role is not able to view documents in a dept - End
  }
