/*
 * InstituteProposalAttachmentForm.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on January 23, 2010, 6:15 PM
 */

package edu.mit.coeus.instprop.gui;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.instprop.bean.InstituteProposalAttachmentBean;
import edu.mit.coeus.instprop.bean.InstituteProposalAttachmentTypeBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.Component;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;


import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author  satheeshkumarkn
 */
public class InstituteProposalAttachmentForm extends CoeusInternalFrame
        implements ActionListener, ListSelectionListener, Observer {
    
    private CoeusMenuItem mnuAdd;
    private CoeusMenuItem mnuModify;
    private CoeusMenuItem mnuDelete;
    private CoeusMenuItem mnuViewAttachment;
    private CoeusMenuItem mnuUploadAttachment;
    private CoeusToolBarButton btnAdd;
    private CoeusToolBarButton btnModify;
    private CoeusToolBarButton btnDelete;
    private CoeusToolBarButton btnUploadAttachment;
    private CoeusToolBarButton btnClose;
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private final int ATTACHMENT_COLUMN = 0;
    private final int ATTACHMENT_COLUMN_WIDHTH = 25;
    private final int NUMBER_COLUMN = 1;
    private final int NUMBER_COLUMN_WIDTH = 40;
    private final int ATTACHMENT_TYPE_COLUMN = 2;
    private final int ATTACHMENT_TYPE_COLUMN_WIDTH = 250;
    private static final int ATTACHMENT_TYPE_COLUMN_MAX_WIDTH = 1250;
    private final int TITLE_COLUMN = 3;
    private final int TITLE_COLUMN_WIDTH = 620;
    private final int ATTACHMENT_TABLE_ROW_HEIGHT = 22;
    private final int ATTACHMENT_TABLE_HEADER_HEIGHT = 24;
    private final int ATTACHMENT_TABLE_HEADER_WIDTH = 4000;
    private CoeusDlgWindow dlgDetails;
    private final String ADD_TITLE = "Add Institute Proposal Attachment";
    private final String MODIFY_TITLE = "Edit Institute Proposal Attachment";
    private char functionType;
    private String proposalNumber;
    private int sequenceNumber;
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private static final char GET_ATTACHMENT_DATAS ='t';
    private CoeusVector cvAttachments;
    private Vector vecAttachmentTypes;
    private InstituteProposalAttachmentModel proposalAttachmentModel;
    private static final char UPDATE_ATTACHMENT = 'u';
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private InstituteProposalAttachmentDetailsForm attachmentDetails;
    private static final char RELEASE_ATTACHMENT_LOCK = 'r';
    private static final String NO_DOCUMENT_TO_VIEW = "instPropAttachment_exceptionCode.1604";
    private static final String NO_ATTACHMENT_TYPE_EXISTS = "instPropAttachment_exceptionCode.1605";
    private static final String DELETE_ATTACHMENT = "instPropAttachment_exceptionCode.1606";
 
    
    /** Creates new form InstituteProposalAttachmentForm */
    public InstituteProposalAttachmentForm(CoeusAppletMDIForm mdiForm, String proposalNumber,
            int sequenceNumber, char functionType) {
        super("", mdiForm);
        this.mdiForm = mdiForm;
        this.proposalNumber = proposalNumber;
        this.sequenceNumber = sequenceNumber;
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.functionType = functionType;
        setToolsMenu(null);
        setFrameMenu(getEditMenu());
        setFrameToolBar(getToolBar());
        setFrameIcon(mdiForm.getCoeusIcon());
        initComponents();
        setAttachmentTableColumns();
        registerComponents();
        if(TypeConstants.DISPLAY_MODE == functionType) {
            enableDisableManitain(false);
            mnuViewAttachment.setEnabled(false);
        }
        try {
            fetchAttachments();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setFormData(cvAttachments);
        if(cvAttachments != null && cvAttachments.size() > 0){
            tblAttachment.setRowSelectionInterval(0,0);
        }
    }
    
  /*
   * Method to set the form datas
   * @param CoeusVector
   *
   */
    private void setFormData(CoeusVector cvAttachments){
        proposalAttachmentModel.setData(cvAttachments);
        
        if(cvAttachments != null && cvAttachments.size()>0 ){
            if(functionType != TypeConstants.DISPLAY_MODE){
                enableDisableManitain(true);
            }
            mnuViewAttachment.setEnabled(true);
            InstituteProposalAttachmentBean attachmentBean = (InstituteProposalAttachmentBean)cvAttachments.get(0);
            setFormValues(attachmentBean);
        }else if(functionType != TypeConstants.DISPLAY_MODE){
            enableDisableManitain(false);
            btnAdd.setEnabled(true);
            mnuAdd.setEnabled(true);
            mnuViewAttachment.setEnabled(false);
        }
        
    }
    
    /*
     * Method to set values to the form components
     * @param InstituteProposalAttachmentBean
     */
    private void setFormValues(InstituteProposalAttachmentBean attachmentBean){
        txtContactName.setText(attachmentBean.getContactName());
        txtPhoneNumber.setText(attachmentBean.getPhoneNumber());
        txtArComments.setText(attachmentBean.getComments());
        txtUpdateUser.setText(attachmentBean.getUpdateUserName()+" at "+
                CoeusDateFormat.format(attachmentBean.getUpdateTimestamp().toString()));
        if(attachmentBean.getDocUpdateTimestamp() != null){
            txtLastDocUpdateUser.setText(attachmentBean.getDocUpdateUserName()+" at "+
                    CoeusDateFormat.format(attachmentBean.getDocUpdateTimestamp().toString()));
        }else{
            txtLastDocUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
        }
        txtFileName.setText(attachmentBean.getFileName());
        txtEmail.setText(attachmentBean.getEmailAddress());
    }
    
    /*
     * Method to register the form components
     */
    private void registerComponents(){
        mnuAdd.addActionListener(this);
        btnAdd.addActionListener(this);
        mnuModify.addActionListener(this);
        btnModify.addActionListener(this);
        mnuDelete.addActionListener(this);
        btnDelete.addActionListener(this);
        mnuViewAttachment.addActionListener(this);
        mnuUploadAttachment.addActionListener(this);
        btnUploadAttachment.addActionListener(this);
        btnClose.addActionListener(this);
        tblAttachment.setRowSelectionAllowed(true);
        tblAttachment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAttachment.getSelectionModel().addListSelectionListener(this);
    }
    
    /*
     * Method to 'Edit' menu
     * @return CoeusMenu
     */
    private CoeusMenu getEditMenu() {
        CoeusMenu menuNarrative = null;
        Vector fileChildren = new Vector();
        mnuAdd = new CoeusMenuItem("Add Attachment", null, true, true);
        mnuAdd.setMnemonic('A');
        
        mnuModify = new CoeusMenuItem("Modify Attachment", null, true, true);
        mnuModify.setMnemonic('M');
        
        mnuDelete = new CoeusMenuItem("Delete Attachment", null, true, true);
        mnuDelete.setMnemonic('D');
        
        mnuViewAttachment = new CoeusMenuItem("View Document", null, true, true);
        mnuViewAttachment.setMnemonic('V');
        
        mnuUploadAttachment = new CoeusMenuItem("Upload Document", null, true, true);
        mnuUploadAttachment.setMnemonic('U');
        
        fileChildren.add(mnuAdd);
        fileChildren.add(mnuModify);
        fileChildren.add(mnuDelete);
        fileChildren.add("seperator");
        fileChildren.add(mnuViewAttachment);
        fileChildren.add(mnuUploadAttachment);
        menuNarrative = new CoeusMenu("Edit", null, fileChildren, true, true);
        menuNarrative.setMnemonic('E');
        
        return menuNarrative;
    }
    
    /*
     * Method to get the form 'Tool Bar'
     * @return JToolBar
     */
    private JToolBar getToolBar() {
        JToolBar toolbar = new JToolBar();
        btnAdd = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(
                CoeusGuiConstants.ADD_ICON)), null, "Add Attachment");
        btnAdd.setDisabledIcon(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DADD_ICON)));
        
        btnModify = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(
                CoeusGuiConstants.EDIT_ICON)), null, "Modify Attachment");
        btnModify.setDisabledIcon(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DEDIT_ICON)));
        
        btnDelete = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(
                CoeusGuiConstants.DELETE_ICON)), null, "Delete Attachment");
        btnDelete.setDisabledIcon(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DDELETE_ICON)));
        
        btnUploadAttachment = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(
                CoeusGuiConstants.ENABLED_ATTACHMENT_ICON)), null, "Upload Document");
        btnUploadAttachment.setDisabledIcon(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_ATTACHMENT_ICON)));
        btnClose = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(
                CoeusGuiConstants.CLOSE_ICON)), null, "Close");
        
        toolbar.add(btnAdd);
        toolbar.add(btnModify);
        toolbar.add(btnDelete);
        toolbar.addSeparator();
        toolbar.add(btnUploadAttachment);
        toolbar.addSeparator();
        toolbar.add(btnClose);
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(btnAdd) || source.equals(mnuAdd)) {
                if(vecAttachmentTypes != null && vecAttachmentTypes.size()>0){
                    Vector attmntType = new Vector();
                    InstituteProposalAttachmentTypeBean instAttmnt = new InstituteProposalAttachmentTypeBean();
                    attmntType.add(instAttmnt);
                    attmntType.addAll(vecAttachmentTypes);
                    vecAttachmentTypes.removeAllElements();
                    vecAttachmentTypes.addAll(attmntType) ;
                    addNewAttachment();
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ATTACHMENT_TYPE_EXISTS)); 
                }
            }else if(source.equals(btnModify) || source.equals(mnuModify)) {
                modifyAttachment();
            }else if(source.equals(btnUploadAttachment) || source.equals(mnuUploadAttachment)){
                uploadDocument();
            }else if(source.equals(mnuViewAttachment)){
                viewDocument();
            }else if(source.equals(btnDelete) || source.equals(mnuDelete)){
                performDeleteAction();
            }else if(source.equals(btnClose)){
                this.doDefaultCloseAction();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    /*
     * Method to enable/disable form toolbar buttons and menu items
     * @param boolean
     */
    public void enableDisableManitain(boolean enable) {
        btnAdd.setEnabled(enable);
        mnuAdd.setEnabled(enable);
        btnDelete.setEnabled(enable);
        mnuDelete.setEnabled(enable);
        btnModify.setEnabled(enable);
        mnuModify.setEnabled(enable);
        btnUploadAttachment.setEnabled(enable);
        mnuUploadAttachment.setEnabled(enable);
    }
    
    /*
     * Method to Show the Form
     */
    public void showAttachmentForm() {
        setTitle(CoeusGuiConstants.INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE+" "+proposalNumber);
        mdiForm.putFrame(CoeusGuiConstants.INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE, proposalNumber, functionType, this);
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        this.addVetoableChangeListener(new VetoableChangeListener(){
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
                if (pce.getPropertyName().equals(
                        JInternalFrame.IS_CLOSED_PROPERTY) ) {
                    boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
                    if( changed ) {
                        try {
                            closeProposalAttachment();
                        } catch ( Exception e) {
                            e.printStackTrace();
                            if(!( e.getMessage().equals(
                                    coeusMessageResources.parseMessageKey(
                                    "protoDetFrm_exceptionCode.1130")) )){
                                CoeusOptionPane.showErrorDialog(e.getMessage());
                            }
                            throw new PropertyVetoException(
                                    coeusMessageResources.parseMessageKey(
                                    "protoDetFrm_exceptionCode.1130"),null);
                        }
                    }
                }
            }
        });
        
    }
    
    /*
     * Method to set the Attachment table column details
     *
     */
    public void setAttachmentTableColumns() {
        tblAttachment.setRowHeight(ATTACHMENT_TABLE_ROW_HEIGHT);
        tblAttachment.getTableHeader().setReorderingAllowed(false);
        tblAttachment.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        Dimension tableHeaderSize = tblAttachment.getTableHeader().getPreferredSize();
        tableHeaderSize.width = ATTACHMENT_TABLE_HEADER_WIDTH;
        tableHeaderSize.height = ATTACHMENT_TABLE_HEADER_HEIGHT;
        tblAttachment.getTableHeader().setPreferredSize(tableHeaderSize);
        proposalAttachmentModel =  new InstituteProposalAttachmentModel();
        tblAttachment.setModel(proposalAttachmentModel);
        
        TableColumn attachmentButtonColumn = tblAttachment.getColumnModel().getColumn(ATTACHMENT_COLUMN);
        attachmentButtonColumn.setCellRenderer(new AttachmentTableRenderer());
        attachmentButtonColumn.setCellEditor(new AttachmentTableEditor());
        attachmentButtonColumn.setResizable(true);
        attachmentButtonColumn.setMinWidth(ATTACHMENT_COLUMN_WIDHTH);
        attachmentButtonColumn.setMaxWidth(ATTACHMENT_COLUMN_WIDHTH);
        attachmentButtonColumn.setPreferredWidth(ATTACHMENT_COLUMN_WIDHTH);
        
        TableColumn attachmentColumn = tblAttachment.getColumnModel().getColumn(NUMBER_COLUMN);
        attachmentColumn.setResizable(true);
        attachmentColumn.setMinWidth(NUMBER_COLUMN_WIDTH);
        attachmentColumn.setMaxWidth(NUMBER_COLUMN_WIDTH);
        attachmentColumn.setPreferredWidth(NUMBER_COLUMN_WIDTH);
        
        TableColumn attachmentTypeColumn = tblAttachment.getColumnModel().getColumn(ATTACHMENT_TYPE_COLUMN);
        attachmentTypeColumn.setResizable(true);
        attachmentTypeColumn.setMinWidth(ATTACHMENT_TYPE_COLUMN_WIDTH);
        //User can drag the type column till ATTACHMENT_TYPE_COLUMN_MAX_WIDTH size
        attachmentTypeColumn.setMaxWidth(ATTACHMENT_TYPE_COLUMN_MAX_WIDTH);
        attachmentTypeColumn.setPreferredWidth(ATTACHMENT_TYPE_COLUMN_WIDTH);
        
        TableColumn titleColumn = tblAttachment.getColumnModel().getColumn(TITLE_COLUMN);
        titleColumn.setResizable(true);
        titleColumn.setMinWidth(TITLE_COLUMN_WIDTH);
        titleColumn.setPreferredWidth(TITLE_COLUMN_WIDTH);

        //coeusqa-3858 start
//        tblAttachment.setSelectionMode(
//			DefaultListSelectionModel.SINGLE_SELECTION);
//	tblAttachment.getTableHeader().addMouseListener(new ColumnHeaderListener());
        //coeusqa-3858 end
        
    }


    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = tblAttachment.getSelectedRow();
        if(selectedRow < 0){
            return;
        }
        if(cvAttachments != null && cvAttachments.size()>0){
            InstituteProposalAttachmentBean attachmentBean = (InstituteProposalAttachmentBean)cvAttachments.get(selectedRow);
            setFormValues(attachmentBean);
        }
        //coeusqa-3858 start
//        sortOnDisplay();
//        proposalAttachmentModel.setData(cvAttachments);
//        proposalAttachmentModel.fireTableDataChanged();
        //coeusqa-3858 end
    }
    
    class AttachmentTableRenderer extends DefaultTableCellRenderer {
        private JButton btnAttachment;
        public AttachmentTableRenderer() {
            btnAttachment = new JButton("");
        }
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            InstituteProposalAttachmentBean proposalAttachmentBean = ((InstituteProposalAttachmentBean)cvAttachments.get(row));
            CoeusAttachmentBean attachmentBean = (CoeusAttachmentBean)proposalAttachmentBean;
            if(attachmentBean != null && (attachmentBean.getFileName() != null ||
                    CoeusGuiConstants.EMPTY_STRING.equals(attachmentBean.getFileName()))) {
                CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
                btnAttachment.setIcon(docUtils.getAttachmentIcon(attachmentBean));
            }else{
                btnAttachment.setIcon(null);
            }
            return btnAttachment;
        }
    }
    
    
    class AttachmentTableEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton btnAttachment;
        
        public AttachmentTableEditor() {
            btnAttachment = new JButton("");
            btnAttachment.addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            try{
                if( source.equals(btnAttachment) ){
                    viewDocument();
                }
                this.fireEditingStopped();
            }catch (Exception exception){
                exception.printStackTrace();
                if(!( exception.getMessage().equals(
                        coeusMessageResources.parseMessageKey(
                        "protoDetFrm_exceptionCode.1130")) )){
                    CoeusOptionPane.showInfoDialog(exception.getMessage());
                }
            }
        }
        
        public Object getCellEditorValue() {
            return "";
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            CoeusAttachmentBean attachmentBean = (CoeusAttachmentBean)cvAttachments.get(row);
            if(attachmentBean != null && (attachmentBean.getFileName() != null ||
                    CoeusGuiConstants.EMPTY_STRING.equals(attachmentBean.getFileName()))) {
                CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
                btnAttachment.setIcon(docUtils.getAttachmentIcon(attachmentBean));
            }else{
                btnAttachment.setIcon(null);
            }
            return btnAttachment;
        }
    }
    
    /*
     * Method to add new attachment
     */
    private void addNewAttachment() throws Exception{
        int attachmentNumber = 1;
        int newAttachmentRow = 0;
        InstituteProposalAttachmentBean proposalAttachmentBean = new InstituteProposalAttachmentBean();
        proposalAttachmentBean.setProposalNumber(proposalNumber);
        proposalAttachmentBean.setSequenceNumber(sequenceNumber);
        if(cvAttachments != null && cvAttachments.size() > 0){
            InstituteProposalAttachmentBean attachmentBean =
                    (InstituteProposalAttachmentBean)cvAttachments.get(cvAttachments.size()-1);
            attachmentNumber = attachmentBean.getAttachmentNumber()+1;
        }
        proposalAttachmentBean.setAttachmentNumber(attachmentNumber);
        proposalAttachmentBean.setAcType(TypeConstants.INSERT_RECORD);
        showAttachmentDetails(ADD_TITLE,proposalAttachmentBean,TypeConstants.ADD_MODE);
        newAttachmentRow = cvAttachments.size()-1;
        if(newAttachmentRow > -1){
            tblAttachment.setRowSelectionInterval(newAttachmentRow,newAttachmentRow);
        }
    }
    
    /*
     * Method to modify the selected attachment
     */
    private void modifyAttachment()throws Exception{
        int selectedRow = tblAttachment.getSelectedRow();
        if(selectedRow < 0){
            return;
        }
        InstituteProposalAttachmentBean proposalAttachmentBean = (InstituteProposalAttachmentBean)cvAttachments.get(selectedRow);;
        InstituteProposalAttachmentBean modifyAttachmentBean = new InstituteProposalAttachmentBean();
        modifyAttachmentBean.setProposalNumber(proposalAttachmentBean.getProposalNumber());
        //COEUSQA:3589 - Institute Proposal Attachments can't be fetched from Modify IP Attachment Window
        modifyAttachmentBean.setAwSequenceNumber(proposalAttachmentBean.getSequenceNumber());
        //COEUSQA:3589 - End
        // COEUSQA - 1525 - Start
        // the actual sequence number is passed not the deleted sequence
        modifyAttachmentBean.setSequenceNumber(sequenceNumber);
        // COEUSQA - 1525 - End
        modifyAttachmentBean.setAttachmentNumber(proposalAttachmentBean.getAttachmentNumber());
        modifyAttachmentBean.setAttachmentTitle(proposalAttachmentBean.getAttachmentTitle());
        modifyAttachmentBean.setAttachmentTypeCode(proposalAttachmentBean.getAttachmentTypeCode());
        modifyAttachmentBean.setAttachmentTypeDescription(proposalAttachmentBean.getAttachmentTypeDescription());
        modifyAttachmentBean.setComments(proposalAttachmentBean.getComments());
        modifyAttachmentBean.setContactName(proposalAttachmentBean.getContactName());
        modifyAttachmentBean.setContactName(proposalAttachmentBean.getContactName());
        modifyAttachmentBean.setEmailAddress(proposalAttachmentBean.getEmailAddress());
        modifyAttachmentBean.setFileName(proposalAttachmentBean.getFileName());
        modifyAttachmentBean.setPhoneNumber(proposalAttachmentBean.getPhoneNumber());
        modifyAttachmentBean.setAcType(TypeConstants.UPDATE_RECORD);
        showAttachmentDetails(MODIFY_TITLE,modifyAttachmentBean,TypeConstants.MODIFY_MODE);
        tblAttachment.setRowSelectionInterval(selectedRow,selectedRow);
    }
    
    /*
     * Method to delete the selected attachment
     */
    private void performDeleteAction()throws Exception{
        int selectedRow = tblAttachment.getSelectedRow();
        if(selectedRow < 0){
            return;
        }
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(DELETE_ATTACHMENT),
                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
        if(selectedOption == CoeusOptionPane.SELECTION_YES){
            InstituteProposalAttachmentBean proposalAttachmentBean = (InstituteProposalAttachmentBean)cvAttachments.get(selectedRow);;
            proposalAttachmentBean.setAcType(TypeConstants.DELETE_RECORD);
            //COEUSQA-1525 - Attachments for Institute Proposal - Start
            proposalAttachmentBean.setAwSequenceNumber(proposalAttachmentBean.getSequenceNumber());
            //COEUSQA-1525 - End
            saveAttachment(proposalAttachmentBean);
            int rowToSelect = selectedRow-1;
            if(rowToSelect < 0 && cvAttachments.size()>0){
                tblAttachment.setRowSelectionInterval(0,0);
            }else if(rowToSelect >= 0 && cvAttachments.size() > 0){
                tblAttachment.setRowSelectionInterval(rowToSelect,rowToSelect);
            }
        }
    }
    /*
     * Method to show the attachment details in a dialog window
     * @param String - attachmentTitle
     * @param InstituteProposalAttachmentBean
     * @param int - attachmentNumber
     * @param char - functionType
     *
     */
    private void showAttachmentDetails(String attachmentTitle,
            InstituteProposalAttachmentBean attachmentBean, char functionType) throws Exception{
        dlgDetails = new CoeusDlgWindow(mdiForm, attachmentTitle, true);
        attachmentDetails =
                new InstituteProposalAttachmentDetailsForm(attachmentBean,dlgDetails);
        attachmentDetails.setFunctionType(functionType);
        attachmentDetails.setAttachmentTypes(this.getAttachmentTypes());
        attachmentDetails.setAttachments(cvAttachments);
        attachmentDetails.setFormData();
        attachmentDetails.registerObserver(this);
        dlgDetails.getContentPane().add(attachmentDetails);
        dlgDetails.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgDetails.getSize();
        dlgDetails.setLocation(screenSize.width / 2 - dlgSize.width / 2, screenSize.height / 2 - dlgSize.height / 2);
        dlgDetails.setResizable(false);
        dlgDetails.setDefaultCloseOperation(0);
        dlgDetails.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                validateData();
            }
        });
        dlgDetails.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                validateData();
            }
        });
        
        dlgDetails.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                attachmentDetails.setInitialFocus();
            }
        });
        dlgDetails.show();
    }
    
    /*
     * Method to validate the form data
     */
    private void validateData(){
        if ( attachmentDetails.isDataChanged()  ) {
            String msg = coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002");
            
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case ( JOptionPane.NO_OPTION ) :
                    attachmentDetails.setSaveRequired( false );
                    dlgDetails.dispose();
                    break;
                case ( JOptionPane.YES_OPTION ) :
                    try{
                        if( attachmentDetails.validateAttachmentDetails() ){
                            boolean canDispose = attachmentDetails.saveAttachmentDetails();
                            if(canDispose){
                                dlgDetails.dispose();
                            }
                        }
                    }catch(Exception e) {
                        CoeusOptionPane.showInfoDialog( e.getMessage() );
                    }
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    dlgDetails.setVisible(true);
                    break;
            }
            
        }else{
            dlgDetails.dispose();
        }
    }
    
    /*
     * Method to fetch attachment types and attachment details
     */
    private void fetchAttachments() throws Exception{
        RequesterBean request = new RequesterBean();
        Vector vecProposalDetail = new Vector();
        vecProposalDetail.add(0,this.proposalNumber);
        vecProposalDetail.add(1,new Integer(this.sequenceNumber));
        request.setDataObjects(vecProposalDetail);
        request.setFunctionType(GET_ATTACHMENT_DATAS);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            Vector vecAttachmentDetails = response.getDataObjects();
            this.setAttachmentTypes((Vector)vecAttachmentDetails.get(0));
            this.setAttachments((CoeusVector)vecAttachmentDetails.get(1));
        }else{
            throw new Exception(response.getMessage());
        }
    }
    
    private class InstituteProposalAttachmentModel extends DefaultTableModel{
        private String [] colNames = {"","No.","Attachment Type","Title"};
        private Class colClass[]={String.class,String.class,String.class,String.class};
        private Vector vecListData;
        /**
         * Method to make cell editable
         * @param row disable this row
         * @param col disable this column
         * @return boolean for cell editable state
         */
        public boolean isCellEditable(int row, int col){
            if(col ==  0){
                return true;
            }else{
                return false;
            }
        }

        /**
         * Method to count total column
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
         * Method to get column class
         * @param col number of column
         * @return Class of column
         */
        public Class getColumnClass(int col){
            return colClass[col];
        }
        public void setValueAt(Object value, int row, int column){
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(vecListData != null && vecListData.size() > 0){
                InstituteProposalAttachmentBean attachmentBean
                        = (InstituteProposalAttachmentBean)vecListData.get(rowIndex);
                switch(columnIndex){
                    case 0:
                        return true;
                    case 1:
                        return attachmentBean.getAttachmentNumber()+CoeusGuiConstants.EMPTY_STRING;
                    case 2:
                        return attachmentBean.getAttachmentTypeDescription();
                    case 3:
                        return attachmentBean.getAttachmentTitle();
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        /**
         * Method to get total rows
         * @return total number of rows
         */
        public int getRowCount() {
            if(vecListData == null || vecListData.size() == 0){
                return 0;
            }
            return vecListData.size();
            
        }
        /**
         * Method to set data in table
         * @param vecListData contains data for table
         */
        public void setData(Vector vecListData){
            this.vecListData = vecListData;
        }
    }
    
    public void update(Observable o, Object arg) {
        if( arg instanceof Vector ) {
            Vector vecAttachments = (Vector)arg;
            vecAttachmentTypes = (Vector)vecAttachments.get(0);
            cvAttachments = (CoeusVector)vecAttachments.get(1);
            setAttachmentTableColumns();
            setFormData(cvAttachments);
        }
    }
    
    /*
     * Method to set attachments details
     * @param cvAttachments
     */
    public void setAttachments(CoeusVector cvAttachments) {
        this.cvAttachments = cvAttachments;
    }
    
    /*
     * Method to get attachments types
     * @param Vector
     */
    public Vector getAttachmentTypes() {
        return vecAttachmentTypes;
    }
    
    /*
     * Method to set attachment types
     * @param Vector - vecAttachmentTypes
     */
    public void setAttachmentTypes(Vector vecAttachmentTypes) {
        this.vecAttachmentTypes = vecAttachmentTypes;
    }
    
    /*
     * Method to upload a document for the attachment
     */
    private void uploadDocument(){
        String extension;
        int selectedRow = tblAttachment.getSelectedRow();
        if(selectedRow < 0 || (cvAttachments != null && cvAttachments.size() < 1)){
            return;
        }
        InstituteProposalAttachmentBean attachmentBean = (InstituteProposalAttachmentBean)cvAttachments.get(selectedRow);
        CoeusFileChooser fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.showFileChooser();
        if(fileChooser.isFileSelected()){
            String fileName = fileChooser.getSelectedFile();
            File file = fileChooser.getFileName();
            //Modified for COEUSQA-3690	Coeus4.5: Institute Proposal Attachments File Name Display Issue - Start
            //Displaying the entire path name instead of just the file name and type extension.
//            String path = file.getAbsolutePath();
            String path = file.getName();
            //COEUSQA-3690 - End
            attachmentBean.setFileName(path);
            txtFileName.setText(path);
            attachmentBean.setFileBytes(fileChooser.getFile());
            CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
            String mimeType = docUtils.getDocumentMimeType(attachmentBean);
            attachmentBean.setMimeType(mimeType);
            //COEUSQA-1525 - Attachments for Institute Proposal - Start
            attachmentBean.setAwSequenceNumber(attachmentBean.getSequenceNumber());
            //COEUSQA-1525 - End
            if(fileName != null && !fileName.trim().equals("")){
                int index = fileName.lastIndexOf('.');
                if(index != -1 && index != fileName.length()){
                    extension = fileName.substring(index+1, fileName.length());
                    if(extension != null){
                        attachmentBean.setFileBytes(fileChooser.getFile());
                    }
                }
            }
            try {
                attachmentBean.setAcType(TypeConstants.UPDATE_RECORD);
                attachmentBean.setSequenceNumber(sequenceNumber);
                saveAttachment(attachmentBean);
                tblAttachment.setRowSelectionInterval(selectedRow,selectedRow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
	 * To display the fields on sorting order depends on the code
	 */
	private void sortOnDisplay() {
		if (cvAttachments != null && cvAttachments.size() > 0) {
			cvAttachments.sort("attachmentNumber", true);
		}
    }

 public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
        {"0","fileName" },
        {"1","attachmentNumber" },
            {"2","attachmentTypeDescription"},
            {"3","attachmentTitle"}
             
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
                if(cvAttachments != null && cvAttachments.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvAttachments).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    proposalAttachmentModel.fireTableRowsUpdated(
                                            0, tblAttachment.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    /*
     * Method to save the attachment details
     * @param InstituteProposalAttachmentBean
     */
    private void saveAttachment(InstituteProposalAttachmentBean attachmentBean) throws Exception{
        RequesterBean request = new RequesterBean();
        request.setFunctionType(UPDATE_ATTACHMENT);
        request.setDataObject(attachmentBean);
        // COEUSQA - 1525 - Start
        Vector vecSeq = new Vector();
        vecSeq.add(new Integer(sequenceNumber));
        request.setDataObjects(vecSeq);
        // COEUSQA - 1525 - End
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            Vector vecAttachments = response.getDataObjects();
            vecAttachmentTypes = (Vector)vecAttachments.get(0);
            cvAttachments = (CoeusVector)vecAttachments.get(1);
            setAttachmentTableColumns();
            setFormData(cvAttachments);
        }else{
            throw new Exception(response.getMessage());
        }
    }
    
    /*
     * Method to view the selected attachment document
     *
     */
    private void viewDocument() throws Exception{
        int selectedRow = tblAttachment.getSelectedRow();
        if(selectedRow < 0 || (cvAttachments != null && cvAttachments.size() < 1)){
            return;
        }
        InstituteProposalAttachmentBean attachmentBean = (InstituteProposalAttachmentBean)cvAttachments.get(selectedRow);
        Map map = new HashMap();
        if(attachmentBean.getFileName() != null && !attachmentBean.getFileName().equals(CoeusGuiConstants.EMPTY_STRING)){
            Vector dataObjects = new Vector();
            map.put("VIEW_DOCUMENT", new Boolean(false));
            dataObjects.add(0, attachmentBean);
            dataObjects.add(1, CoeusGuiConstants.getMDIForm().getUnitNumber());
            RequesterBean requesterBean = new RequesterBean();
            DocumentBean documentBean = new DocumentBean();
            map.put("DATA", dataObjects);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.instprop.InstituteProposalDocumentReader");
            map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
            map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
            documentBean.setParameterMap(map);
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(STREMING_SERVLET, requesterBean);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/');
                URL urlObj = new URL(reportUrl);
                URLOpener.openUrl(urlObj);
            }else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_DOCUMENT_TO_VIEW));
            return ;
        }
    }
    
    /*
     * Method to close the attachment screen and release the lock
     *
     */
    public void closeProposalAttachment() throws Exception{
        RequesterBean request = new RequesterBean();
        request.setDataObject(this.proposalNumber);
        request.setFunctionType(RELEASE_ATTACHMENT_LOCK);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()) {
            throw new Exception(response.getMessage());
        }
        mdiForm.removeFrame(CoeusGuiConstants.INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE,proposalNumber);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlAttachment = new javax.swing.JPanel();
        scrPnAttachment = new javax.swing.JScrollPane();
        tblAttachment = new javax.swing.JTable();
        pnlContact = new javax.swing.JPanel();
        pnlContactDetails = new javax.swing.JPanel();
        lblContactName = new javax.swing.JLabel();
        lblPhoneNumber = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        txtContactName = new edu.mit.coeus.utils.CoeusTextField();
        txtPhoneNumber = new edu.mit.coeus.utils.CoeusTextField();
        txtEmail = new edu.mit.coeus.utils.CoeusTextField();
        pnlUserTimeStamp = new javax.swing.JPanel();
        lblUpdatedBy = new javax.swing.JLabel();
        txtLastDocUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblLastDocument = new javax.swing.JLabel();
        txtUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();

        setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pnlMain.setMinimumSize(new java.awt.Dimension(975, 700));
        pnlMain.setPreferredSize(new java.awt.Dimension(975, 700));
        pnlMain.setRequestFocusEnabled(false);
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlAttachment.setMinimumSize(new java.awt.Dimension(965, 380));
        pnlAttachment.setPreferredSize(new java.awt.Dimension(965, 380));
        pnlAttachment.setLayout(new java.awt.BorderLayout(10, 0));

        scrPnAttachment.setMinimumSize(new java.awt.Dimension(560, 200));
        scrPnAttachment.setPreferredSize(new java.awt.Dimension(960, 200));

        tblAttachment.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblAttachment.setFont(CoeusFontFactory.getNormalFont());
        tblAttachment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblAttachment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnAttachment.setViewportView(tblAttachment);

        pnlAttachment.add(scrPnAttachment, java.awt.BorderLayout.WEST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(pnlAttachment, gridBagConstraints);

        pnlContact.setMinimumSize(new java.awt.Dimension(890, 180));
        pnlContact.setPreferredSize(new java.awt.Dimension(890, 180));
        pnlContact.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pnlContactDetails.setMinimumSize(new java.awt.Dimension(965, 250));
        pnlContactDetails.setPreferredSize(new java.awt.Dimension(965, 180));
        pnlContactDetails.setLayout(new java.awt.GridBagLayout());

        lblContactName.setFont(CoeusFontFactory.getLabelFont());
        lblContactName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblContactName.setText("Contact Name :");
        lblContactName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblContactName.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lblContactName.setMaximumSize(new java.awt.Dimension(95, 15));
        lblContactName.setMinimumSize(new java.awt.Dimension(105, 15));
        lblContactName.setPreferredSize(new java.awt.Dimension(105, 15));
        lblContactName.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        pnlContactDetails.add(lblContactName, gridBagConstraints);

        lblPhoneNumber.setFont(CoeusFontFactory.getLabelFont());
        lblPhoneNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPhoneNumber.setText("Phone Number :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        pnlContactDetails.add(lblPhoneNumber, gridBagConstraints);

        lblEmail.setFont(CoeusFontFactory.getLabelFont());
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblEmail.setText("Email Address :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 0);
        pnlContactDetails.add(lblEmail, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblComments.setText("Comments :");
        lblComments.setMaximumSize(new java.awt.Dimension(95, 15));
        lblComments.setMinimumSize(new java.awt.Dimension(105, 15));
        lblComments.setPreferredSize(new java.awt.Dimension(105, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContactDetails.add(lblComments, gridBagConstraints);

        scrPnComments.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnComments.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrPnComments.setMinimumSize(new java.awt.Dimension(200, 50));
        scrPnComments.setPreferredSize(new java.awt.Dimension(200, 50));

        txtArComments.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArComments.setColumns(25);
        txtArComments.setEditable(false);
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContactDetails.add(scrPnComments, gridBagConstraints);

        txtContactName.setEditable(false);
        txtContactName.setMinimumSize(new java.awt.Dimension(250, 20));
        txtContactName.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContactDetails.add(txtContactName, gridBagConstraints);

        txtPhoneNumber.setEditable(false);
        txtPhoneNumber.setMinimumSize(new java.awt.Dimension(150, 20));
        txtPhoneNumber.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContactDetails.add(txtPhoneNumber, gridBagConstraints);

        txtEmail.setEditable(false);
        txtEmail.setMinimumSize(new java.awt.Dimension(260, 20));
        txtEmail.setPreferredSize(new java.awt.Dimension(260, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContactDetails.add(txtEmail, gridBagConstraints);

        pnlUserTimeStamp.setEnabled(false);
        pnlUserTimeStamp.setFocusable(false);
        pnlUserTimeStamp.setMinimumSize(new java.awt.Dimension(890, 150));
        pnlUserTimeStamp.setPreferredSize(new java.awt.Dimension(890, 150));
        pnlUserTimeStamp.setLayout(new java.awt.GridBagLayout());

        lblUpdatedBy.setFont(CoeusFontFactory.getLabelFont());
        lblUpdatedBy.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdatedBy.setText("Last Updated by :");
        lblUpdatedBy.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblUpdatedBy.setMaximumSize(new java.awt.Dimension(150, 20));
        lblUpdatedBy.setMinimumSize(new java.awt.Dimension(150, 20));
        lblUpdatedBy.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlUserTimeStamp.add(lblUpdatedBy, gridBagConstraints);

        txtLastDocUpdateUser.setEditable(false);
        txtLastDocUpdateUser.setMinimumSize(new java.awt.Dimension(438, 20));
        txtLastDocUpdateUser.setPreferredSize(new java.awt.Dimension(438, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        pnlUserTimeStamp.add(txtLastDocUpdateUser, gridBagConstraints);

        lblLastDocument.setFont(CoeusFontFactory.getLabelFont());
        lblLastDocument.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastDocument.setText("Last Document Uploaded by :");
        lblLastDocument.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblLastDocument.setMaximumSize(new java.awt.Dimension(170, 20));
        lblLastDocument.setMinimumSize(new java.awt.Dimension(160, 20));
        lblLastDocument.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        pnlUserTimeStamp.add(lblLastDocument, gridBagConstraints);

        txtUpdateUser.setEditable(false);
        txtUpdateUser.setMinimumSize(new java.awt.Dimension(438, 20));
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(438, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 15);
        pnlUserTimeStamp.add(txtUpdateUser, gridBagConstraints);

        lblFileName.setFont(CoeusFontFactory.getLabelFont());
        lblFileName.setText("File Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 1);
        pnlUserTimeStamp.add(lblFileName, gridBagConstraints);

        txtFileName.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtFileName.setEnabled(false);
        txtFileName.setMinimumSize(new java.awt.Dimension(438, 20));
        txtFileName.setPreferredSize(new java.awt.Dimension(438, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        pnlUserTimeStamp.add(txtFileName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlContactDetails.add(pnlUserTimeStamp, gridBagConstraints);

        pnlContact.add(pnlContactDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        pnlMain.add(pnlContact, gridBagConstraints);

        getContentPane().add(pnlMain);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblContactName;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblLastDocument;
    private javax.swing.JLabel lblPhoneNumber;
    private javax.swing.JLabel lblUpdatedBy;
    private javax.swing.JPanel pnlAttachment;
    private javax.swing.JPanel pnlContact;
    private javax.swing.JPanel pnlContactDetails;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlUserTimeStamp;
    private javax.swing.JScrollPane scrPnAttachment;
    private javax.swing.JScrollPane scrPnComments;
    private javax.swing.JTable tblAttachment;
    private javax.swing.JTextArea txtArComments;
    private edu.mit.coeus.utils.CoeusTextField txtContactName;
    private edu.mit.coeus.utils.CoeusTextField txtEmail;
    public edu.mit.coeus.utils.CoeusTextField txtFileName;
    private edu.mit.coeus.utils.CoeusTextField txtLastDocUpdateUser;
    private edu.mit.coeus.utils.CoeusTextField txtPhoneNumber;
    private edu.mit.coeus.utils.CoeusTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables
    
    
    
}
