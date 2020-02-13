/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardAddDocumentController.java
 *
 * Created on October 5, 2007, 11:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award.controller;


import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardDocumentBean;
import edu.mit.coeus.award.gui.AwardAddDocumentForm;
import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author divyasusendran
 */
public class AwardAddDocumentController extends AwardController implements ActionListener {
    
    private CoeusDlgWindow dlgAwardUploadDoc;
    private CoeusAppletMDIForm mdiForm;
    private AwardDocumentBean awardDocumentBean;
    private char functionType = 'M';
    private static final char MODIFY_DOCUMENT = 'M';
    private static final char MODIFY_AWARD_DOC_DATA = 'b';
    private AwardAddDocumentForm awardAddDocumentForm;
    private CoeusMessageResources coeusMessageResources;
    private boolean dataChanged = false;
    private boolean fileSelected;
    private QueryEngine queryEngine;
    private byte[] blobData;
    private boolean okClicked;
    private boolean docTypeEmpty = false;
    private int DOC_CODE = 1;
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String AWARD_MAINTENANCE_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/AwardMaintenanceServlet";
    private static final String EMPTY_STRING = "";
    private static final char ADD_VOID_AWARD_DOC_DATA = 'p';   
    //Added with case 4007: Icon based on mime type
    private String mimeType;
    //4007 End
    private boolean newVersion;
    private boolean newDocumentId;
    private boolean saveRequired;
    
    /* commented for new process
    public AwardAddDocumentController(AwardBaseBean awardBaseBean,CoeusAppletMDIForm mdiForm) {
        super(awardBaseBean);
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
    }
     */

    /**
     * Creates a new instance of AwardAddDocumentController.
     * Added two more argument and commented default one.
     * @param CoeusAppletMDIForm, AwardBaseBean, AwardDocumentBean and functionType.
     */
      public AwardAddDocumentController(AwardBaseBean awardBaseBean,CoeusAppletMDIForm mdiForm,
              AwardDocumentBean awardDocumentBean,char functionType) {
        
        super(awardBaseBean);
        this.mdiForm = mdiForm;
        this.awardDocumentBean = awardDocumentBean;
        this.functionType = functionType;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
    }

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }

    public boolean isNewDocumentId() {
        return newDocumentId;
    }

    public void setNewDocumentId(boolean newDocumentId) {
        this.newDocumentId = newDocumentId;
    }

    public boolean isSaveRequired() {
        return saveRequired;
    }

    /**
     * Method for checking whether any of the field values changed, 
     * if values have changed then set dataChanged as true.
     * This method is used for firing save confirmation message
     * @param 
     * @return
     */
    private void dataValuesChanged(){
        ComboBoxBean cmbTypeCode =(ComboBoxBean)awardAddDocumentForm.cmbDocumentType.getSelectedItem();
        if(!cmbTypeCode.getCode().equals("") && !cmbTypeCode.getDescription().equals("")){
            if( !awardAddDocumentForm.txtDescription.getText().equals(EMPTY_STRING)
            || !awardAddDocumentForm.txtFileName.getText().equals(EMPTY_STRING) ||
                    Integer.parseInt(cmbTypeCode.getCode())!= DOC_CODE){
                dataChanged = true;
            }
        }else{
            if( !awardAddDocumentForm.txtDescription.getText().equals(EMPTY_STRING)
            || !awardAddDocumentForm.txtFileName.getText().equals(EMPTY_STRING)){
                dataChanged = true;
            }else{
                dataChanged = false;
            }            
        }        
    }

    /**
     * Method for setting the size,title,location etc of the dialogWindow.Also
     * values are set to the Document Type combo box
     * @param 
     * @return
     */
    private void postInitComponents(){           
        dlgAwardUploadDoc = new CoeusDlgWindow(mdiForm);
        dlgAwardUploadDoc.setResizable(false);
        dlgAwardUploadDoc.setModal(true);
        dlgAwardUploadDoc.getContentPane().add(awardAddDocumentForm);
        dlgAwardUploadDoc.setTitle("Add Document");
        if(this.functionType == 'M'){
          dlgAwardUploadDoc.setTitle("Modify Document");
        }
        dlgAwardUploadDoc.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardUploadDoc.setSize(470, 150);
        Dimension screenSize;
        Dimension dlgSize;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dlgSize = dlgAwardUploadDoc.getSize();
        dlgAwardUploadDoc.setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                screenSize.height / 2 - (dlgSize.height / 2));
        
        dlgAwardUploadDoc.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                try{
                    requestDefaultFocusToComp();
                }catch(CoeusException ce){
                    ce.printStackTrace();
                }
            }
        });
        
        dlgAwardUploadDoc.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardUploadDoc.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                try{
                    dataValuesChanged();
                    if(dataChanged){
                        performCloseAction();
                    }else{
                        dlgAwardUploadDoc.dispose();
                    }   
                }catch(Exception ce){
                    ce.printStackTrace();
                }
            }
        });
        
        dlgAwardUploadDoc.addEscapeKeyListener(new AbstractAction("escPressed"){
        public void actionPerformed(ActionEvent ae){
                try{
                    dataValuesChanged();                    
                    if(dataChanged){
                        performCloseAction();
                    }else{
                        dlgAwardUploadDoc.dispose();
                    }                    
                }catch(Exception ce){
                    ce.printStackTrace();
                }
            }
        });
        queryEngine = QueryEngine.getInstance();
        CoeusVector cvAwardDocTypes = null;
        CoeusVector newAwardDocTypes = new CoeusVector();
        try{
            cvAwardDocTypes = queryEngine.getDetails(queryKey, KeyConstants.AWARD_DOCUMENT_TYPES);
            if(this.functionType == TypeConstants.ADD_MODE ){
                newAwardDocTypes.add("");
            }
            newAwardDocTypes.addAll(cvAwardDocTypes);
            awardAddDocumentForm.cmbDocumentType.setModel(new DefaultComboBoxModel(newAwardDocTypes));
        }catch (Exception e){
            e.printStackTrace();
        }
         if(this.functionType == TypeConstants.MODIFY_MODE ){
                if (awardDocumentBean != null){
                    awardAddDocumentForm.txtDescription.setText(awardDocumentBean.getDescription());
                    awardAddDocumentForm.txtFileName.setText(awardDocumentBean.getFileName());
                    awardAddDocumentForm.cmbDocumentType.setSelectedIndex(getDocumentTypeCodeIndex(cvAwardDocTypes));
                }
         }
    }
   
    /**
     * Case for Award Attachment Modification Process.
     * This method return the current selected DocumentType index of the Award Upload Document.
     * @param CoeusVector
     * @return index
     */
    public int getDocumentTypeCodeIndex(CoeusVector cvDocType){
        int documentTypeIndex = 0;
        if(awardDocumentBean!=null && cvDocType!=null && awardDocumentBean.getDocumentTypeDescription()!=null){
            for(int index = 0; index<cvDocType.size(); index++){
                if(awardDocumentBean.getDocumentTypeDescription().equalsIgnoreCase(cvDocType.get(index).toString())){
                    documentTypeIndex = index;
                    break;
                }
            }
        }
        return documentTypeIndex;
    }
    /**
     * This is used when Escape or 'X' is used to close the dialog window,
     * if any data is changed then a save confirmation message is fired
     * @param
     */
    private void performCloseAction() throws Exception{
        int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                JOptionPane.YES_OPTION);
        switch( option ) {
            case (JOptionPane.YES_OPTION ):
                saveFormData();
                if(isOkClicked()){
                    dlgAwardUploadDoc.dispose();
                }
                break;
            case(JOptionPane.NO_OPTION ):
                dlgAwardUploadDoc.dispose();
                break;
            default:
                requestDefaultFocusToComp();
                break;
        }
    }
    
    /**
     * Method for registering and enabling/disabling the components, 
     * setting focus traversal rule
     * @param 
     * @return
     */
    public void registerComponents() {
        awardAddDocumentForm = new AwardAddDocumentForm();
        ScreenFocusTraversalPolicy focusTraversalPolicy ;
        java.awt.Component components[] = {awardAddDocumentForm.cmbDocumentType,
        awardAddDocumentForm.txtDescription,
        awardAddDocumentForm.btnUpload,
        awardAddDocumentForm.btnOk,
        awardAddDocumentForm.btnCancel,
        awardAddDocumentForm.btnView};
        focusTraversalPolicy = new ScreenFocusTraversalPolicy(components);
        awardAddDocumentForm.setFocusTraversalPolicy(focusTraversalPolicy);
        awardAddDocumentForm.setFocusCycleRoot(true);
        awardAddDocumentForm.btnUpload.addActionListener(this);
        awardAddDocumentForm.btnCancel.addActionListener(this);
        awardAddDocumentForm.btnOk.addActionListener(this);
        awardAddDocumentForm.btnView.addActionListener(this);
        awardAddDocumentForm.cmbDocumentType.addActionListener(this);
        awardAddDocumentForm.txtDescription.setDocument(new LimitedPlainDocument(200));
        setEnableComponents();
    }
    
    public void showWindow(){
        dlgAwardUploadDoc.setVisible(true);
    }
    
    private void requestDefaultFocusToComp() throws CoeusException{
        awardAddDocumentForm.cmbDocumentType.requestFocusInWindow();
    }    
    
    public boolean checkSaveRequired(){
        return true;
    }
    
    public Component getControlledUI() {
        return null;
    }
    
    public void setFormData(Object data) throws CoeusException {
        
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void formatFields() {
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }    
    /**
     * Method for saving the document details. 
     * @param 
     * @return
     */
    public void saveFormData() throws CoeusException {
        ComboBoxBean cmbTypeCode =(ComboBoxBean)awardAddDocumentForm.cmbDocumentType.getSelectedItem();
        if(!cmbTypeCode.getCode().equals("") && !cmbTypeCode.getDescription().equals("")){
            RequesterBean requesterBean = new RequesterBean();
            AwardDocumentBean awardDocumentBean = new AwardDocumentBean();
            awardDocumentBean.setAwardNumber(awardBaseBean.getMitAwardNumber());
            awardDocumentBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            if(awardAddDocumentForm.cmbDocumentType.getSelectedItem() != null
                    && !awardAddDocumentForm.cmbDocumentType.getSelectedItem().equals(EMPTY_STRING)){
                cmbTypeCode =(ComboBoxBean)awardAddDocumentForm.cmbDocumentType.getSelectedItem();
                awardDocumentBean.setDocumentTypeCode(Integer.parseInt(cmbTypeCode.getCode()));
                awardDocumentBean.setDocumentTypeDescription((awardAddDocumentForm.cmbDocumentType.getSelectedItem().toString()));
                docTypeEmpty = false;
            }else{
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("awardDocuments_NoDocType_exceptionCode.1112"));
                docTypeEmpty = true;
                awardAddDocumentForm.cmbDocumentType.requestFocusInWindow();                
                return;
            }
            if(awardAddDocumentForm.txtDescription.getText() != null &&
                    !awardAddDocumentForm.txtDescription.getText().equals(EMPTY_STRING)){
                if(awardAddDocumentForm.txtDescription.getText().length() > 200){
                    String description = awardAddDocumentForm.txtDescription.getText().substring(0,201);
                    awardDocumentBean.setDescription(description);
                    docTypeEmpty = false;
                }else{
                    awardDocumentBean.setDescription(awardAddDocumentForm.txtDescription.getText());
                }
            }
             else {
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("awardDocuments_DescDoc_exceptionCode.1115"));
                docTypeEmpty = true;
                awardAddDocumentForm.txtDescription.requestFocusInWindow();
                return;
            }
            if(awardAddDocumentForm.txtFileName.getText() != null
                    && !awardAddDocumentForm.txtFileName.getText().equals(EMPTY_STRING)){
                awardDocumentBean.setFileName(awardAddDocumentForm.txtFileName.getText());
                docTypeEmpty = false;
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("awardDocuments_AttachDoc_exceptionCode.1113"));
                docTypeEmpty = true;
                awardAddDocumentForm.btnUpload.requestFocusInWindow();                
                return;
            }
            if(fileSelected && getBlobData() != null){
                awardDocumentBean.setDocument(getBlobData());
                awardDocumentBean.setMimeType(getMimeType());//4007
            }
            awardDocumentBean.setAcType("I");
            awardDocumentBean.setDocStatusCode("A");
            awardDocumentBean.setDocStatusDescription("ACTIVE");
            requesterBean.setDataObject(awardDocumentBean);
            requesterBean.setFunctionType(ADD_VOID_AWARD_DOC_DATA);
            AppletServletCommunicator appletServletCommunicator = new
                    AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            if(!responder.isSuccessfulResponse()){
                setOkClicked(false);
                dlgAwardUploadDoc.dispose();
                CoeusOptionPane.showErrorDialog(responder.getMessage());
                throw new CoeusException(responder.getMessage(),0);
            }else{
                setOkClicked(true);
            }
        }else{
            docTypeEmpty = true;
            setOkClicked(false);
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("awardDocuments_NoDocType_exceptionCode.1112"));
                awardAddDocumentForm.cmbDocumentType.requestFocusInWindow();
                return;
        }        
    }

    /**
     * Case for Award Attachment Modification Process.
     * This method used to modify the awardDocumentBean/Upload Document.
     * @param awardDocumentBean
     * @throws CoeusException
     */
    public void modifyFormData(AwardDocumentBean awardDocumentBean) throws CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(awardDocumentBean);
        requesterBean.setFunctionType(MODIFY_AWARD_DOC_DATA);
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();

        if ( !responder.isSuccessfulResponse() ){
            setOkClicked(false);
            dlgAwardUploadDoc.dispose();
            CoeusOptionPane.showErrorDialog(responder.getMessage());
            throw new CoeusException(responder.getMessage(),0);
        }else{
            setOkClicked(true);
        }
    }
     
    public void display() {
    }
    
    /**
     * Method for enabling and disabling of components
     * @param 
     * @return
     */
    private void setEnableComponents(){
        awardAddDocumentForm.cmbDocumentType.setEnabled(true);
        awardAddDocumentForm.txtDescription.setEditable(true);
        awardAddDocumentForm.txtFileName.setEditable(false);
        awardAddDocumentForm.btnUpload.setEnabled(true);
        awardAddDocumentForm.btnOk.setEnabled(true);
        awardAddDocumentForm.btnCancel.setEnabled(true);
        awardAddDocumentForm.btnView.setEnabled(false);
    }
  
    /**
     * Depending on the button pressed,corresponding actions are performed
     * if 'Upload', then file is selected using file chooser
     * if 'View' then checking whether BLOB data exists and displaying on the browser
     * if 'OK', then validate the details before saving to the database
     * if 'Cancel' then check if any data changed and fire save confirmation else dispose the window
     * @param 
     * @return
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        /*if Upload , then set 'fileSelected' as true, get the filename from file chooser and set it to the txtFilename,
            set the BLOB data and enable the view button*/
        if(source.equals(awardAddDocumentForm.btnUpload)){
            CoeusFileChooser fileChooser = new CoeusFileChooser(dlgAwardUploadDoc);
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.showFileChooser();
            if(fileChooser.isFileSelected()){
                String fileName = fileChooser.getSelectedFile();
                if(fileName != null && !fileName.trim().equals(EMPTY_STRING)){
                    int index = fileName.lastIndexOf('.');
                    if(index != -1 && index != fileName.length()){
                        setFileSelected(true);
                        awardAddDocumentForm.txtFileName.setText(fileChooser.getFileName().getName());
                        setBlobData(fileChooser.getFile());
                        //Added with case 4007: Icon based on mime type
                        CoeusDocumentUtils docTypeUtils = CoeusDocumentUtils.getInstance();
                        CoeusAttachmentBean attachmentBean = new CoeusAttachmentBean(fileName,getBlobData());
                        setMimeType(docTypeUtils.getDocumentMimeType(attachmentBean));
                        //4007 End
                        awardAddDocumentForm.btnView.setEnabled(true);
                    }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                "correspType_exceptionCode.1012"));
                        setFileSelected(false);
                        setBlobData(null);
                        setMimeType(null);//4007
                        return;
                    }
                }
            }
        } else if(source.equals(awardAddDocumentForm.btnOk)){
            try{
                /*Check the function type  and awardDocumentBean is null or not*/
                if(awardDocumentBean != null){
                    if(functionType == MODIFY_DOCUMENT){
                        awardDocumentBean.setAcType(TypeConstants.UPDATE_RECORD);
                            newVersion = false;
                            newDocumentId = false;
                            ComboBoxBean cmbTypeCode =(ComboBoxBean)awardAddDocumentForm.cmbDocumentType.getSelectedItem();
                            if(!cmbTypeCode.getCode().equals("") && !cmbTypeCode.getDescription().equals("")){
                                if(awardAddDocumentForm.cmbDocumentType.getSelectedItem() != null
                                && !awardAddDocumentForm.cmbDocumentType.getSelectedItem().equals(EMPTY_STRING)){
                                    cmbTypeCode =(ComboBoxBean)awardAddDocumentForm.cmbDocumentType.getSelectedItem();
                                    awardDocumentBean.setDocumentTypeCode(Integer.parseInt(cmbTypeCode.getCode()));
                                    awardDocumentBean.setDocumentTypeDescription((awardAddDocumentForm.cmbDocumentType.getSelectedItem().toString()));
                                    docTypeEmpty = false;
                                }
                            }
                            
                            if(awardAddDocumentForm.txtDescription.getText() != null &&
                                !awardAddDocumentForm.txtDescription.getText().equals(EMPTY_STRING)){
                                if(awardAddDocumentForm.txtDescription.getText().length() > 200){
                                    String description = awardAddDocumentForm.txtDescription.getText().substring(0,201);
                                    awardDocumentBean.setDescription(description);
                                    docTypeEmpty = false;
                                }else{
                                    awardDocumentBean.setDescription(awardAddDocumentForm.txtDescription.getText());
                                }
                            }else {
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("awardDocuments_DescDoc_exceptionCode.1115"));
                                docTypeEmpty = true;
                                awardAddDocumentForm.txtDescription.requestFocusInWindow();
                                return;
                            }
                            if(awardAddDocumentForm.txtFileName.getText() != null
                                    && !awardAddDocumentForm.txtFileName.getText().equals(EMPTY_STRING)){
                                awardDocumentBean.setFileName(awardAddDocumentForm.txtFileName.getText());
                                docTypeEmpty = false;
                            }else{
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("awardDocuments_AttachDoc_exceptionCode.1113"));
                                docTypeEmpty = true;
                                awardAddDocumentForm.btnUpload.requestFocusInWindow();
                                return;
                            }
                            if(fileSelected && getBlobData() != null){
                                awardDocumentBean.setDocument(getBlobData());
                                awardDocumentBean.setMimeType(getMimeType());
                            }
                            modifyFormData(awardDocumentBean);
                    }
                } else {
                    saveFormData();
                }
//                if(okClicked){
                if(isOkClicked() || !docTypeEmpty){
                    dlgAwardUploadDoc.dispose();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        /* if View, check value for fileSelected,
         if true the display the blob data else throw an error msg*/
        else if(source.equals(awardAddDocumentForm.btnView)){
            if(fileSelected){
                try{
                    viewDocument();
                }catch (Exception exception){
                    exception.printStackTrace();
                    if(!( exception.getMessage().equals(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130")) )){
                        CoeusOptionPane.showInfoDialog(exception.getMessage());
                        return;
                    }
                }
            }
        } else if(source.equals(awardAddDocumentForm.btnCancel)){
            dataValuesChanged();
            if(dataChanged){
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        JOptionPane.YES_OPTION);
                switch(option){
                    case JOptionPane.YES_OPTION:
                        try{
                            if(this.functionType != 'M'){
                                 saveFormData();
                            } else{
                                dlgAwardUploadDoc.dispose();
                            }
                            if(isOkClicked()){
                                dlgAwardUploadDoc.dispose();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case JOptionPane.NO_OPTION :
                        dlgAwardUploadDoc.dispose();
                        break;
                    default:
                        
                        try {
                            requestDefaultFocusToComp();
                        } catch (CoeusException ex) {
                            ex.printStackTrace();
                        }
                        break;
                }
            }else{
                dlgAwardUploadDoc.dispose();
            }
        }
    }
    
    public boolean isFileSelected() {
        return fileSelected;
    }
    
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }
    
    public byte[] getBlobData() {
        return blobData;
    }
    
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    
    /**
     * Method for viewing the selected file on the browser
     * @param 
     * @return
     */
    private void viewDocument() throws Exception{        
        CoeusVector cvDataObject = new CoeusVector();
        HashMap hmDocumentDetails = new HashMap();
        hmDocumentDetails.put("awardNumber", awardBaseBean.getMitAwardNumber());
        hmDocumentDetails.put("sequenceNumber", EMPTY_STRING+awardBaseBean.getSequenceNumber());
        hmDocumentDetails.put("fileName", awardAddDocumentForm.txtFileName.getText());
        hmDocumentDetails.put("document", getBlobData());        
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
        AppletServletCommunicator appletServletCommunicator = new
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
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
    //Added with case 4007: Icon based on mime type : Start
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    //4007 : End
}
