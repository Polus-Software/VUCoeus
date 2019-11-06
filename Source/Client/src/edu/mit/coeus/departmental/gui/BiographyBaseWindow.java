/*
 * @(#)BiographyBaseWindow.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.Equals;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import java.util.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.net.URL;
import java.applet.AppletContext;

import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;

/** <CODE>BiographyBaseWindow</CODE> is a form object which display
 * all the biographical information for a selected person and can be used to
 * <CODE>add/modify/display</CODE> the biographical details details.
 * This class is instantiated in <CODE>PersonBaseWindow</CODE>.
 *
 * @author  Raghunath P.V.
 * @version:
 */

public class BiographyBaseWindow extends CoeusInternalFrame
                            implements TypeConstants, ActionListener,ListSelectionListener {

    private final String SEPERATOR="seperator";
    private final String EMPTY_STRING = "";
    //private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    
    //Modified for case 3685 - Person Bio Timestamp - start
    private CoeusMenuItem biographyAdd, biographyDelete,
            biographyViewPDF,biographyUploadPDF;

    private CoeusToolBarButton btnAddModule, btnDeleteModule,
            btnUploadPDF, btnSaveBiography,
            btnCloseBiography;
    //Modified for case 3685 - Person Bio Timestamp - start
    private CoeusMenu biographyEditMenu;

    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;

    private String personName;
    private String personId;
    private String loginName;
    private String referenceId;
    private BiographyTableModel biographyTableModel;

    private char functionType;

    private boolean saveRequired;
    private boolean canMaintainBiography;
    //private boolean userCanMaintainUnit;
    //private boolean userCanMaintainSelectedPerson;
    //private boolean isBiographyMaintained;

    //private Vector biographyDetails;

    private Vector vecBiographyData;
    private Vector vecDeletedBiographyData;
    //private Vector vecBioPdfData;
    private static final String SAVE_BIOGRAPHY_DETAILS = "saveBiographyDetails";
    //private static final String GET_BIOGRAPHY_DETAILS_FOR_PERSON = "getBiographyDetails";
    private CoeusFileChooser fileChooser;
    private static final String WORD_FORMAT = "doc";
    private static final String PDF_FORMAT = "pdf";
    private static final char GET_PERSON_BIO_PDF = 'B';
    private static final char GET_PERSON_BIO_SOURCE = 'C';
    private static final String CONNECTION_STRING = 
        CoeusGuiConstants.CONNECTION_URL +"/personMaintenanceServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private ColumnValueEditor columnValueEditor;
    //Case 2793:NOW Person Maintainer - Uploading documents
    private Vector vecDocumentType;
    private boolean instanceCreated;
    //2793 End
    /**
     *  Constructor which instantiates the BiographyBaseWindow
     *  @param title a String sets the title to the JInternal Frame
     *  @mdiForm a Reference of CoeusAppletMDIForm
     */
    public BiographyBaseWindow(String title, CoeusAppletMDIForm mdiForm) {

        super(title, mdiForm);
        this.mdiForm = mdiForm;
        this.vecDeletedBiographyData = new Vector();
        try {
            //mdiForm.putFrame(CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE, this);
            mdiForm.putFrame(CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE, referenceId, functionType, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method which sets the biographical data.
     * @param biographyData is a vector containing all the biographical information
     */
    public void setBiographyData(Vector biographyData){
        this.vecBiographyData = biographyData;
    }

    /**
     * Method which displays the Biographical Window
     */
    public void showWindow(){
        try{
            postInitComponents();
            this.referenceId = personId;
            if(personName.equalsIgnoreCase(loginName)){
                this.canMaintainBiography = true;
            }
            mdiForm.getDeskTopPane().add(this);
            columnValueEditor= new ColumnValueEditor(200);
            this.setSelected(true);
            this.setVisible(true);
            showPersonBiographyWindow();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Helper method which sets the required properties

    private void postInitComponents(){

        setFrameMenu(getPersonBiographyEditMenu());
        setFrameToolBar(getPersonBiographyToolBarMenu());
        setFrame(CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE);
        setFrameIcon(mdiForm.getCoeusIcon());
        //createPersonBiographyInfo();
        coeusMessageResources = CoeusMessageResources.getInstance();

    }

    //Helper method which shows biography window
    
    private void showPersonBiographyWindow(){

        try{
            initComponents();
            //biographyTableModel = new BiographyTableModel();
            tblPersonBiography.setModel(new BiographyTableModel());
            //tblPersonBiography.setModel(biographyTableModel);
            tblPersonBiography.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tblPersonBiography.getSelectionModel().addListSelectionListener(this);
            vecDocumentType = getDocumentTypeCodes();//2793
            setFormData();
            formatComponents();
             // Added by chandra. If there is no row found then disable the corresponding menu items 
            // and tool bar buttons
            if(tblPersonBiography.getRowCount() == 0){
                btnDeleteModule.setEnabled(false);
                btnUploadPDF.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - start
//                btnUploadWord.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - end
                biographyDelete.setEnabled(false);
                biographyViewPDF.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - start
//                biographyViewWord.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - end
                biographyUploadPDF.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - start
//                biographyUploadWord.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - end
            }else if( functionType != DISPLAY_MODE ){
                btnDeleteModule.setEnabled(true);
                btnUploadPDF.setEnabled(true);
                //Commented for case 3685 - Remove Word icons - start
//                btnUploadWord.setEnabled(true);
                //Commented for case 3685 - Remove Word icons - end
                biographyDelete.setEnabled(true);
                biographyViewPDF.setEnabled(true);
                //Commented for case 3685 - Remove Word icons - start
//                biographyViewWord.setEnabled(true);
                //Commented for case 3685 - Remove Word icons - end
                biographyUploadPDF.setEnabled(true);
                //Commented for case 3685 - Remove Word icons - start
//                biographyUploadWord.setEnabled(true);
                //Commented for case 3685 - Remove Word icons - end
            }
            // End chandra
            
//            setFormData();
           
           
            
            setTableEditors();
            setBackground((Color)javax.swing.UIManager.getDefaults().get("Panel.background"));
            this.addVetoableChangeListener(new VetoableChangeListener(){
                public void vetoableChange(PropertyChangeEvent pce)
                throws PropertyVetoException {
                    if (pce.getPropertyName().equals(
                            JInternalFrame.IS_CLOSED_PROPERTY) ) {
                        boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
                        if( changed ) {
                            try {
                                int rowCount = tblPersonBiography.getRowCount();
                                if( rowCount > 0 && tblPersonBiography.isEditing()){
                                    tblPersonBiography.getCellEditor().stopCellEditing();
                                }
                                performClose();
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
        // Added by chandra. Select the first row whne the form is opened.
         if(tblPersonBiography.getRowCount() > 0){
                tblPersonBiography.setRowSelectionInterval(0,0);
                tblPersonBiography.editCellAt(0,0);
            }
        // End chandra
        
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
        This method is used to Enable or Disable the Buttons
        depending on the function Type.
    */

    private void formatComponents(){

        boolean enabled =  functionType == DISPLAY_MODE ? false : true;
        btnAddModule.setEnabled(enabled);
        btnDeleteModule.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - start
//        btnUploadWord.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - end
        btnUploadPDF.setEnabled(enabled);
        btnSaveBiography.setEnabled(enabled);
        biographyAdd.setEnabled(enabled);
        biographyDelete.setEnabled(enabled);
//        biographyViewWord.setEnabled(enabled);
//        biographyViewPDF.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - start
//        biographyUploadWord.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - end
        biographyUploadPDF.setEnabled(enabled);
    }
    
//Method rewritten with case 2793:NOW Person Maintainer - Uploading documents
    //Helper method which validates the data
//    private boolean validateData() throws Exception{
//
//        boolean valid = true;
//        int rowCount = tblPersonBiography.getRowCount();
//
//        if(rowCount > 0 && tblPersonBiography.getCellEditor() != null){
//            tblPersonBiography.getCellEditor().stopCellEditing();
//        }
//        if(rowCount > 0){
//
//            for(int inInd=0; inInd < rowCount ;inInd++){
//
//                String desc = (String)((DefaultTableModel)
//                                            tblPersonBiography.getModel()).
//                                                    getValueAt(inInd,2);
//                if((desc == null) || (desc.trim().length() <= 0)){
//                    valid=false;
//                    //Added for case 3685 - Show Persn Timestamp - start
//                    //To set the focus to the description column 
//                    setRequestFocusInThread(inInd, 2);
//                    //Added for case 3685 - Show Persn Timestamp - end
//                    errorMessage("Enter the Description");
//                    tblPersonBiography.requestFocus();
//                    break;
//                }
//            }
//            if(!valid){
//                return false;
//            }
//        }
//        return true;
//    }
    
    private boolean validateData() throws Exception{
        
        boolean valid = true;
        
        if( tblPersonBiography.isEditing() ){
            if(tblPersonBiography.getCellEditor() != null && tblPersonBiography.getRowCount() > 0){
                tblPersonBiography.getCellEditor().stopCellEditing();
            }
        }
        
        if(tblPersonBiography.getRowCount()>0){
            
            int bioRowCount = tblPersonBiography.getRowCount();
            String selPersonId="";
            String selPersonName = "";
            DepartmentBioPersonFormBean departmentBioPersonFormBean = null;
            //TextValidator validator = new TextValidator(TextValidator.ALPHA+TextValidator.NUMERIC+TextValidator.PERIOD+TextValidator.UNDERSCORE+TextValidator.HYPHEN);
            for(int perRow = 0 ; perRow < bioRowCount ; perRow++ ){
                departmentBioPersonFormBean = (DepartmentBioPersonFormBean)vecBiographyData.elementAt(perRow);
                String description = departmentBioPersonFormBean.getDescription();
                int documentTypeCode = departmentBioPersonFormBean.getDocumentTypeCode();
                if( description == null || description.length() <= 0){
                    valid=false;
//                    CoeusOptionPane.showInfoDialog("Enter the Description");
                    tblPersonBiography.setRowSelectionInterval(perRow,perRow);
                    setRequestFocusInThread(perRow, 2);
                    errorMessage("Enter the Description");
                    break;
                //JIRA COEUSQA 1540 - START
                /*} else if (! validator.validate(description)) {
                    valid = false;
                    errorMessage("Description "+coeusMessageResources.parseMessageKey("text_validation_exceptionCode.1000"));
                *///JIRA COEUSQA 1540 - END
                }else if(documentTypeCode == 0){//check the document type
                    valid=false;
                    tblPersonBiography.setRowSelectionInterval(perRow,perRow);
                    setRequestFocusInThread(perRow, 4);
                    errorMessage(coeusMessageResources.parseMessageKey("prop_person_document_typeCode.2000"));
                }
            }
//            Commented  with COEUSDEV-139 : Allow multiple person attachments of same document type in Person Bio Module
//            if(valid && isDuplicateDocumentType()){//check duplicate entry
//                    valid=false;
//                    errorMessage(coeusMessageResources.parseMessageKey("prop_person_document_typeCode.2001"));
//                }
            if(!valid){
                return false;
            }
        }else{
            return true;
        }
        return true;
    }
    //2793 End
    //Added for case 3685 - Show Persn Timestamp - start
     private void setRequestFocusInThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tblPersonBiography.setRowSelectionInterval(selrow, selrow);
                tblPersonBiography.scrollRectToVisible(
                tblPersonBiography.getCellRect(selrow ,0, true));
                tblPersonBiography.editCellAt(selrow,selcol);
                tblPersonBiography.getEditorComponent().requestFocusInWindow();
                
            }
        });
    }
    //Added for case 3685 - Show Persn Timestamp - end
     
    /**
     * This method is used to get the Form data.
     * @returns Vector of DepartmentBioPersonFormBean's
     */

    public Vector getBiographyData(){

        if(vecDeletedBiographyData != null){

            int delSize = vecDeletedBiographyData.size();
            DepartmentBioPersonFormBean depBioPersonFormBean = null;
            for(int index = 0; index < delSize; index++){
                depBioPersonFormBean = (DepartmentBioPersonFormBean)vecDeletedBiographyData.get(index);
                if(depBioPersonFormBean != null && vecBiographyData !=null){
                    vecBiographyData.insertElementAt(depBioPersonFormBean,index);
                }
            }
        }
        //printData();
        return vecBiographyData;
    }

    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */

    private void errorMessage(String mesg) throws Exception{
        throw new Exception(mesg);
    }

    // helper method which sets the Edit menu for departmental module
    private CoeusMenu getPersonBiographyEditMenu(){
        if( biographyEditMenu == null ) {
            Vector fileChildren = new Vector();

            biographyAdd = new CoeusMenuItem("Add Module", null, true, true);
            biographyAdd.setMnemonic('A');
            biographyAdd.addActionListener(this);

            biographyDelete = new CoeusMenuItem("Delete Module", null, true, true);
            biographyDelete.setMnemonic('D');
            biographyDelete.addActionListener(this);


            Vector uploadFilesChildren = new Vector();
            //Commented for case 3685 - Remove Word icons - start
//            biographyUploadWord = new CoeusMenuItem("Upload Word File",null,true,true);
//            //wordFileSaveToDatabase.setMnemonic('O');
//            biographyUploadWord.addActionListener(this);
            //Commented for case 3685 - Remove Word icons - end
            //Adding AreaOfResearch Menu Item
            //COEUSDEV-173 : Menu selection name in Maintain Person needs tweaking : Start
            biographyUploadPDF = new CoeusMenuItem("Upload File", null, true, true);
            biographyUploadPDF.setMnemonic('U');
            //wordFileSaveToDisk.setMnemonic('E');
            biographyUploadPDF.addActionListener(this);
             //Commented for case 3685 - Remove Word icons - start
//            uploadFilesChildren.add( biographyUploadWord );
            //Commented for case 3685 - Remove Word icons - end
            uploadFilesChildren.add( biographyUploadPDF );
            //Commented for case 3685 - Remove Word icons - start
//            CoeusMenu uploadFiles = new CoeusMenu("Upload Files",null,uploadFilesChildren,true,true);
//            uploadFiles.setMnemonic('U');
            //Commented for case 3685 - Remove Word icons - end
            biographyViewPDF = new CoeusMenuItem("View File", null, true, true);
            biographyViewPDF.setMnemonic('V');
            biographyViewPDF.addActionListener(this);
            //COEUSDEV-173 End
            //Commented for case 3685 - Remove Word icons - start
//            biographyViewWord = new CoeusMenuItem("View Word File",null,true,true);
//            biographyViewWord.addActionListener(thbiographyViewWordis);
            //Commented for case 3685 - Remove Word icons - end
            
            Vector viewFilesChildren = new Vector();
            //Commented for case 3685 - Remove Word icons - start
//            viewFilesChildren.add( biographyViewWord );
            //Commented for case 3685 - Remove Word icons - end
            viewFilesChildren.add( biographyViewPDF );

            //Commented for case 3685 - Remove Word icons - start
//            CoeusMenu viewFiles = new CoeusMenu("View Files",null,viewFilesChildren,true,true);
//            viewFiles.setMnemonic('V');
            //Commented for case 3685 - Remove Word icons - end
            fileChildren.add(biographyAdd);
            fileChildren.add(biographyDelete);
            fileChildren.add(SEPERATOR);
//            fileChildren.add(biographyEditWordSource);
//            fileChildren.add(biographyViewPDF);
//            fileChildren.add(SEPERATOR);
            //Commented for case 3685 - Remove Word icons - start
//            fileChildren.add(uploadFiles);
//            fileChildren.add(viewFiles);
            //Commented for case 3685 - Remove Word icons - end
            fileChildren.add(biographyUploadPDF);
            fileChildren.add(biographyViewPDF);

            biographyEditMenu = new CoeusMenu("Edit", null, fileChildren, true, true);
            biographyEditMenu.setMnemonic('E');
        }
        return biographyEditMenu;
    }

    //Helper method which sets the ToolBar buttons
    
    private JToolBar getPersonBiographyToolBarMenu(){

        JToolBar personBiographyToolBar = new JToolBar();

        btnAddModule = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
            null, "Add Module");

        btnDeleteModule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
            null, "Delete Module");
            //Commented for case 3685 - Remove Word icons - start
//        btnUploadWord = new CoeusToolBarButton(new ImageIcon(
//        getClass().getClassLoaderbtnUploadWord().getResource(CoeusGuiConstants.WORD_ICON)),
//            null, "Upload Word File");
        //Commented for case 3685 - Remove Word icons - end
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//        btnUploadPDF = new CoeusToolBarButton(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON)),
//            null, "Upload PDF File");
        btnUploadPDF = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON)), null, "Upload Attachment");        
        
//        btnUploadPDF.setDisabledIcon(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));   
        btnUploadPDF.setDisabledIcon(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_ATTACHMENT_ICON)));        
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
        
        btnSaveBiography = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
            null, "Save");

        btnCloseBiography = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
            null, "Close");

        btnAddModule.addActionListener(this);
        btnDeleteModule.addActionListener(this);
        //Commented for case 3685 - Remove Word icons - start
//        btnUploadWord.addActionListener(this);
        //Commented for case 3685 - Remove Word icons - end
        btnUploadPDF.addActionListener(this);
        btnSaveBiography.addActionListener(this);
        btnCloseBiography.addActionListener(this);

        personBiographyToolBar.add(btnAddModule);
        personBiographyToolBar.add(btnDeleteModule);
        //Commented for case 3685 - Remove Word icons - start
//        personBiographyToolBar.add(btnUploadWord);
        //Commented for case 3685 - Remove Word icons - end
        personBiographyToolBar.add(btnUploadPDF);
        personBiographyToolBar.addSeparator();
        personBiographyToolBar.add(btnSaveBiography);
        personBiographyToolBar.add(btnCloseBiography);

        personBiographyToolBar.setFloatable(false);
        return personBiographyToolBar;
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }

    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }

    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }

    /** Getter for property loginName.
     * @return Value of property loginName.
     */
    public java.lang.String getLoginName() {
        return loginName;
    }

    /** Setter for property loginName.
     * @param loginName New value of property loginName.
     */
    public void setLoginName(java.lang.String loginName) {
        this.loginName = loginName;
    }

    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }

    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }

    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }

    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlPersonBiographyContainer = new javax.swing.JPanel();
        scrPnBiography = new javax.swing.JScrollPane();
        tblPersonBiography = new javax.swing.JTable();
        pnlMessage = new javax.swing.JPanel();
        lblUpdatedBy = new javax.swing.JLabel();
        txtUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblLastDocument = new javax.swing.JLabel();
        txtLastDocUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        pnlPersonBiographyContainer.setLayout(new java.awt.GridBagLayout());

        scrPnBiography.setBorder(null);
        scrPnBiography.setPreferredSize(new java.awt.Dimension(792, 472));
        tblPersonBiography.setFont(CoeusFontFactory.getNormalFont());
        tblPersonBiography.setOpaque(false);
        tblPersonBiography.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblPersonBiography.setShowHorizontalLines(false);
        tblPersonBiography.setShowVerticalLines(false);
        scrPnBiography.setViewportView(tblPersonBiography);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlPersonBiographyContainer.add(scrPnBiography, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(pnlPersonBiographyContainer, gridBagConstraints);

        pnlMessage.setLayout(new java.awt.GridBagLayout());

        pnlMessage.setMaximumSize(new java.awt.Dimension(895, 90));
        pnlMessage.setMinimumSize(new java.awt.Dimension(895, 90));
        pnlMessage.setPreferredSize(new java.awt.Dimension(895, 90));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        pnlMessage.add(lblUpdatedBy, gridBagConstraints);

        txtUpdateUser.setEditable(false);
        txtUpdateUser.setMinimumSize(new java.awt.Dimension(380, 20));
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 2, 6);
        pnlMessage.add(txtUpdateUser, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlMessage.add(lblLastDocument, gridBagConstraints);

        txtLastDocUpdateUser.setEditable(false);
        txtLastDocUpdateUser.setMinimumSize(new java.awt.Dimension(380, 20));
        txtLastDocUpdateUser.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 6);
        pnlMessage.add(txtLastDocUpdateUser, gridBagConstraints);

        lblFileName.setFont(CoeusFontFactory.getLabelFont());
        lblFileName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFileName.setText("File Name: ");
        lblFileName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblFileName.setMaximumSize(new java.awt.Dimension(150, 20));
        lblFileName.setMinimumSize(new java.awt.Dimension(150, 20));
        lblFileName.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlMessage.add(lblFileName, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setMinimumSize(new java.awt.Dimension(380, 20));
        txtFileName.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 6);
        pnlMessage.add(txtFileName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 3, 2);
        getContentPane().add(pnlMessage, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /** This abstract method must be implemented by all classes which inherits this class.
     * Used for saving the current activesheet when clicked Save from File menu.
     */
    public void saveActiveSheet() {
    }

    // Helpe method which validates the row entry whether the description is null or not
    private boolean validateRowEntry() throws Exception{
        //columnValueEditor.stopCellEditing();
        boolean valid = true;
        int rowCnt = tblPersonBiography.getRowCount();
        if(rowCnt >= 0){

            for(int inInd=0; inInd < rowCnt ;inInd++){

                String desc = (String)((DefaultTableModel)
                                            tblPersonBiography.getModel()).
                                                    getValueAt(inInd,2);
                if((desc == null) || (desc.trim().length() <= 0)){
                    valid=false;
                    errorMessage("Enter the Description");
                    tblPersonBiography.requestFocus();
                    break;
                }
            }
            if(!valid){
                return false;
            }
        }
        return valid;
    }

    public class BiographyTableModel extends DefaultTableModel implements TableModel{
        //Case 3855 Added new column for storing file extension
        //Case 2793: Document Type in person biography
        private String colNames[] = {EMPTY_STRING,EMPTY_STRING, "Description",EMPTY_STRING,"Document Type"};
        //2793 End
         //Case 3855 End
        public boolean isCellEditable(int row, int col){
            // If the user doen't have right then stop cell editable for all col &row
            
            if( col == 0 || col == 1 ){
                return ((Boolean)getValueAt(row,col)).booleanValue();
            }
            
            if( functionType == DISPLAY_MODE ) {
                return false;
            }
            
            return true;
        }
        
        public int getColumnCount(){
            return colNames.length;
        }
        
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
    }
    
    /**
     * This method is invoked whenever the user fires the action event by button
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object actSource = actionEvent.getSource();
        try{
            if (actSource.equals(biographyAdd) || actSource.equals(btnAddModule)) {
                if(tblPersonBiography.getRowCount() >0){
                    columnValueEditor.stopCellEditing();
                }
                boolean valid = validateRowEntry();
                if(!valid){
                  return;
                }
                Vector newRowData = new Vector();

                TableColumn column = tblPersonBiography.getColumnModel().getColumn(0);
                //Modified for case 3685 - Remove Word icons - start
//                column.setCellRenderer(new ButtonRenderer());
//                column.setCellEditor(new ButtonEditor(new JCheckBox()));
                column.setMaxWidth(0);
                column.setMinWidth(0);
                column.setPreferredWidth(0);
                 //Modified for case 3685 - Remove Word icons - end
                
                column = tblPersonBiography.getColumnModel().getColumn(1);
                column.setCellRenderer(new ButtonRenderer());
                column.setCellEditor(new ButtonEditor(new JCheckBox()));

                column = tblPersonBiography.getColumnModel().getColumn(2);
                column.setCellRenderer(new ColumnValueRenderer());
                //column.setCellEditor(new ColumnValueEditor(200));
                column.setCellEditor(columnValueEditor);
               // #  Case 3855 start
                column = tblPersonBiography.getColumnModel().getColumn(3);
                column.setMaxWidth(0);
                column.setMinWidth(0);
                column.setPreferredWidth(0);
               // #  Case 3855 End
               
                //Case 2793:NOW Person Maintainer - Uploading documents
                column = tblPersonBiography.getColumnModel().getColumn(4);
                column.setCellRenderer(new ColumnValueRenderer());
                column.setCellEditor(columnValueEditor);
                //2793: End
                
                newRowData.addElement(new Boolean(false));
                newRowData.addElement(new Boolean(false));
                newRowData.addElement("");
                
                // #Case 3855 -- start added an empty file extension elementy
                newRowData.addElement("");
               // #Case 3855 ---- End
               //Case 2793:NOW Person Maintainer - Uploading documents
                newRowData.addElement(new ComboBoxBean("",""));
               //2793 End
                DepartmentBioPersonFormBean bioBean = new DepartmentBioPersonFormBean();
                bioBean.setAcType(INSERT_RECORD);
                bioBean.setPersonId(personId);

                if(vecBiographyData != null){
                    vecBiographyData.addElement(bioBean);
                }else{
                    vecBiographyData = new Vector();
                    vecBiographyData.addElement(bioBean);
                }
                saveRequired=true;
                ((DefaultTableModel)tblPersonBiography.getModel()).addRow( newRowData );
                ((DefaultTableModel)tblPersonBiography.getModel()).fireTableDataChanged();

                int lastRow = tblPersonBiography.getRowCount() - 1;
                if(lastRow >= 0){
                    btnDeleteModule.setEnabled(true);
                    biographyDelete.setEnabled(true);
//                    btnUploadPDF.setEnabled(true);
//                    btnUploadWord.setEnabled(true);
//                    biographyDelete.setEnabled(false);
//                    biographyViewPDF.setEnabled(false);
//                    biographyViewWord.setEnabled(false);
//                    biographyUploadPDF.setEnabled(false);
//                    biographyUploadWord.setEnabled(false);
                    tblPersonBiography.setRowSelectionInterval( lastRow, lastRow );
                    tblPersonBiography.scrollRectToVisible(tblPersonBiography.getCellRect(
                            lastRow ,0, true));
                }
                
                
                
                int bioRowCount = tblPersonBiography.getRowCount();                
            //Added by Amit 11/20/2003                
                if(actSource.equals(btnAddModule))
                {
                    tblPersonBiography.requestFocusInWindow();
                    tblPersonBiography.editCellAt(bioRowCount-1 ,2);
                    tblPersonBiography.getEditorComponent().requestFocusInWindow();
                }                
                //End Amit
                
                if(tblPersonBiography.getRowCount()!= 0){
                    btnDeleteModule.setEnabled(true);
                    biographyDelete.setEnabled(true);
                    btnUploadPDF.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - start
//                    btnUploadWord.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - end
                    biographyUploadPDF.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - start
//                    biographyUploadWord.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - end
                }else{
                    btnDeleteModule.setEnabled(false);
                    biographyDelete.setEnabled(false);
                    btnUploadPDF.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - start
//                    btnUploadWord.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - end
                    biographyUploadPDF.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - start
//                    biographyUploadWord.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - end
                }

            }else if (actSource.equals(btnCloseBiography)) {
                performClose();
            }else if (actSource.equals(biographyDelete) || actSource.equals(btnDeleteModule)) {
                performDelete();
            }else if (actSource.equals(btnSaveBiography)){
                if(isSaveRequired()){
                    setBiographyInfo();
                }
                //Commented for case 3685 - Remove Word icons - start
//            }else if ( actSource.equals( btnUploadWord ) || actSource.equals( biographyUploadWord ) ){
//                uploadWordDocument();
                //Commented for case 3685 - Remove Word icons - end
            }else if( actSource.equals( btnUploadPDF ) || actSource.equals( biographyUploadPDF ) ){
                uploadPdfDocument();
            }else if( actSource.equals( biographyViewPDF ) ){
                viewPdfDocument();
                //Commented for case 3685 - Remove Word icons - start
//            }else if( actSource.equals( biographyViewWord ) ){
//                viewWordDocument();
                //Commented for case 3685 - Remove Word icons - end
            }else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                                            "funcNotImpl_exceptionCode.1100"));
            }
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }

    // Helpe method which closes the BiographyBaseWindow
    private void performClose() throws Exception{
        String msg = coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002");
        JInternalFrame frame = null;
        if ( isSaveRequired()) {
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case(JOptionPane.YES_OPTION):
                    try{
                        setBiographyInfo();
                        mdiForm.removeFrame(
                            CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE,
                            referenceId );
                        if(mdiForm.getFrame(
                            CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE,
                            referenceId) == null){
                            frame = mdiForm.getFrame(
                                CoeusGuiConstants.PERSON_BASE_FRAME_TITLE);
                            if(frame != null){
                                frame.setSelected(true);
                                frame.setVisible(true);
                            }
                        }
                        this.dispose();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        String exMsg = ex.getMessage();
                        CoeusOptionPane.showWarningDialog(exMsg);
                        throw new Exception(
                        coeusMessageResources.parseMessageKey(
                        "protoDetFrm_exceptionCode.1130"));
                    }
                    break;
                case(JOptionPane.NO_OPTION):

                    saveRequired= false;
                    vecBiographyData = null;                //Check This
                    mdiForm.removeFrame(
                        CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE,
                        referenceId );
                    if(mdiForm.getFrame(
                        CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE,
                        referenceId) == null){
                        frame = mdiForm.getFrame(
                            CoeusGuiConstants.PERSON_BASE_FRAME_TITLE);
                        if(frame != null){
                            frame.setSelected(true);
                            frame.setVisible(true);
                        }
                    }
                    this.dispose();
                    break;
                case(JOptionPane.CANCEL_OPTION):
                    this.dispose();
                    break;
            }
        }else{
             mdiForm.removeFrame(
                CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE,
                referenceId );
            if(mdiForm.getFrame(
                CoeusGuiConstants.PERSON_BIOGRAPHY_BASE_FRAME_TITLE,
                referenceId) == null){
                frame = mdiForm.getFrame(
                    CoeusGuiConstants.PERSON_BASE_FRAME_TITLE);
                if(frame != null){
                    frame.setSelected(true);
                    frame.setVisible(true);
                }
            }
            this.dispose();
        }
    }
    // Helper method which updates biographical information of a person to database
    private void setBiographyInfo() throws Exception{

        if(functionType == 'D'){
            //do nothing
        }else {
            if(!validateData()){
                
            }else{
                Vector vecBioData = getBiographyData();
                if( vecBioData != null && vecBioData.size() > 0 ) {
                    Vector vecDataToServer = new Vector();
                    vecDataToServer.addElement(vecBioData);
                    vecDataToServer.addElement(personId);
                    int selRow = tblPersonBiography.getSelectedRow();
                    String connectTo = CoeusGuiConstants.CONNECTION_URL +"/personMaintenanceServlet";
                    RequesterBean request = new RequesterBean();
                    request.setFunctionType(SAVE_RECORD);
                    request.setId(SAVE_BIOGRAPHY_DETAILS);
                    request.setDataObject(vecDataToServer);
                    AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
                    comm.send();
                    ResponderBean response = comm.getResponse();
                    if (response.isSuccessfulResponse()) {
                        saveRequired = false;
                        Vector dataFromServer = (Vector)response.getDataObjects();
                        vecBiographyData = dataFromServer;
                        setFormData();
                        setTableEditors();
                        this.vecDeletedBiographyData.removeAllElements();
                        if( selRow != -1 && tblPersonBiography.getRowCount() > selRow ) {
                            tblPersonBiography.setRowSelectionInterval(selRow,selRow);
                        }
                    } else {
                        CoeusOptionPane.showErrorDialog(response.getMessage());
                        if (response.isCloseRequired()) {
                            //mdiForm.removeFrame(
                            //CoeusGuiConstants.PROTOCOL_FRAME_TITLE, referenceId);
                            this.doDefaultCloseAction();
                            return;
                        }else {
                            throw new Exception(response.getMessage());
                        }
                    }
                }
                saveRequired = false;
            }
        }
    }

    //Helper method which deletes a row in the JTable
    private void performDelete(){

        int totalRows = tblPersonBiography.getRowCount();
            if (totalRows > 0) {
                int selectedRow = tblPersonBiography.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedOption = CoeusOptionPane.
                                        showQuestionDialog(
                                        "Are you sure you want to delete this biography?",
                                        CoeusOptionPane.OPTION_YES_NO,
                                        CoeusOptionPane.DEFAULT_YES);
                    if (0 == selectedOption) {
                        DepartmentBioPersonFormBean depBioBean = null;
                        saveRequired = true;

                        if(vecBiographyData != null){
                            depBioBean =
                                (DepartmentBioPersonFormBean) vecBiographyData.get( selectedRow );
                        }

                        if( (depBioBean.getAcType() != null )){
                            if( ! depBioBean.getAcType().equalsIgnoreCase(INSERT_RECORD) ) {
                                depBioBean.setAcType( DELETE_RECORD );
                                vecDeletedBiographyData.addElement( depBioBean );
                            }
                        }else{
                            depBioBean.setAcType( DELETE_RECORD );
                            vecDeletedBiographyData.addElement( depBioBean );
                        }

                        vecBiographyData.removeElementAt( selectedRow );

                        ((DefaultTableModel)
                        tblPersonBiography.getModel()).removeRow(selectedRow);

                        ((DefaultTableModel)
                        tblPersonBiography.getModel()).fireTableDataChanged();

                        int newRowCount = tblPersonBiography.getRowCount();
                        if(newRowCount == 0){
                            btnDeleteModule.setEnabled(false);
                            btnUploadPDF.setEnabled(false);
                            //Commented for case 3685 - Remove Word icons - start
//                            btnUploadWord.setEnabled(false);
                            //Commented for case 3685 - Remove Word icons - end
                            biographyDelete.setEnabled(false);
                            biographyViewPDF.setEnabled(false);
                            //Commented for case 3685 - Remove Word icons - start
//                            biographyViewWord.setEnabled(false);
                            //Commented for case 3685 - Remove Word icons - end
                            biographyUploadPDF.setEnabled(false);
                            //Commented for case 3685 - Remove Word icons - start
//                            biographyUploadWord.setEnabled(false);
                            //Commented for case 3685 - Remove Word icons - end
                            //Added for case 3685 - Show Document Timestamps - start
                            txtUpdateUser.setText("");
                            txtLastDocUpdateUser.setText("");
                            txtFileName.setText("");
                            //Added for case 3685 - Show Document Timestamps - start
                        }else if (newRowCount > selectedRow) {
                            (tblPersonBiography.getSelectionModel())
                                .setSelectionInterval(selectedRow,
                                    selectedRow);
                        } else {

                            tblPersonBiography.setRowSelectionInterval( newRowCount - 1,
                                           newRowCount -1 );
                            tblPersonBiography.scrollRectToVisible( tblPersonBiography.getCellRect(
                                            newRowCount - 1 ,
                                            0, true));
                        }
                    }
                }else{
                    CoeusOptionPane.
                                showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "protoFndSrcFrm_exceptionCode.1057"));
                }
            }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblFileName;
    public javax.swing.JLabel lblLastDocument;
    public javax.swing.JLabel lblUpdatedBy;
    public javax.swing.JPanel pnlMessage;
    public javax.swing.JPanel pnlPersonBiographyContainer;
    public javax.swing.JScrollPane scrPnBiography;
    public javax.swing.JTable tblPersonBiography;
    public edu.mit.coeus.utils.CoeusTextField txtFileName;
    public edu.mit.coeus.utils.CoeusTextField txtLastDocUpdateUser;
    public edu.mit.coeus.utils.CoeusTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables

    /** Method to set the data in the JTable.
     * This method sets the data which is available in DepartmentBioPersonFormBean
     * Vector to JTable.
     */
    private void setFormData(){

        Vector vcDataPopulate = new Vector();
        Vector vcData=null;

        if((vecBiographyData != null) &&
            (vecBiographyData.size() > 0)){

                int bioSize = vecBiographyData.size();
                DepartmentBioPersonFormBean depBioPersonFormBean = null;

                for(int inCtrdata = 0; inCtrdata < bioSize; inCtrdata++){

                    depBioPersonFormBean = (DepartmentBioPersonFormBean)
                            vecBiographyData.get(inCtrdata);

                    String desc = depBioPersonFormBean.getDescription();
                    vcData= new Vector();

                    vcData.addElement(new Boolean(depBioPersonFormBean.isHasBioSource() ) );
                    vcData.addElement(new Boolean(depBioPersonFormBean.isHasBioPDF() ) );
                    vcData.addElement(desc == null ? "" : desc );
                    //Modified with case 4007:Icon based on mime Type
                    // # Case 3855 start  Adding file extension for column 
//                     String fileName = null;
//                    if(depBioPersonFormBean.getDepartmentBioPDFPersonFormBean() !=null) {
//                        fileName = depBioPersonFormBean.getDepartmentBioPDFPersonFormBean().getFileName();
//                    }
//                     vcData.addElement(UserUtils.getFileExtension(fileName));
                      // # Case 3855 end
                    vcData.addElement(depBioPersonFormBean.getDepartmentBioPDFPersonFormBean());
                    //4007 end
                    //Case 2793:NOW Person Maintainer - Uploading documents
                    int typeCode=depBioPersonFormBean.getDocumentTypeCode();
                    ComboBoxBean comboBean = getDocumentComboData(typeCode);
                    vcData.add( comboBean );
                    //2793 End
                    vcDataPopulate.addElement(vcData);
                }
                    ((DefaultTableModel)tblPersonBiography.getModel()).
                        setDataVector(vcDataPopulate,getColumnNames());
                    ((DefaultTableModel)tblPersonBiography.getModel()).
                        fireTableDataChanged();

                    // If the Table contains more than one row.. move the cursor to the first row..
                    if( tblPersonBiography.getRowCount() > 0 ){
                        //tblPersonBiography.setRowSelectionInterval(0,2);
                    }else{
//                        btnDeleteModule.setEnabled(false);
//                        btnUploadPDF.setEnabled(false);
//                        btnUploadWord.setEnabled(false);
//                        biographyDelete.setEnabled(false);
//                        biographyViewPDF.setEnabled(false);
//                        biographyViewWord.setEnabled(false);
//                        biographyUploadPDF.setEnabled(false);
//                        biographyUploadWord.setEnabled(false);
                    }
            }
    }

    /**
     * This helper method is used to get all the column names of the JTable.
     */
    private Vector getColumnNames(){

        Enumeration enumColNames = tblPersonBiography.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }


    /* This method is used to set the cell editors to the columns,
       set the column width to each individual column, disable the column
       resizable feature to the JTable, setting single selection mode to the
       JTable */

    private void setTableEditors(){

        JTableHeader header = tblPersonBiography.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setFont(CoeusFontFactory.getLabelFont());

        tblPersonBiography.setRowHeight(24);
        tblPersonBiography.setOpaque(false);
        tblPersonBiography.setShowVerticalLines(false);
        tblPersonBiography.setShowHorizontalLines(false);
        tblPersonBiography.setSelectionMode(
                            DefaultListSelectionModel.SINGLE_SELECTION);


        TableColumn column = tblPersonBiography.getColumnModel().getColumn(0);
        //Modified for case 3685 - Remove Word icons - end                            
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
//        column.setCellRenderer(new ButtonRenderer());
//        column.setCellEditor(new ButtonEditor(new JCheckBox()));
        //Modifed for case 3685 - Remove Word icons - end
        column = tblPersonBiography.getColumnModel().getColumn(1);
        column.setMinWidth(22);
        column.setMaxWidth(22);
        column.setPreferredWidth(22);
        column.setResizable(false);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));

        column = tblPersonBiography.getColumnModel().getColumn(2);
        //column.setMinWidth(650);
        //column.setMaxWidth(650);
        //column.setPreferredWidth(650);
        column.setResizable(false);
        column.setCellRenderer(new ColumnValueRenderer());
        //column.setCellEditor(new ColumnValueEditor(200));
        column.setCellEditor(columnValueEditor);
        
        // #  Case 3855 start
        column = tblPersonBiography.getColumnModel().getColumn(3);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        // #  Case 3855 End
        
       //Case 2793:NOW Person Maintainer - Uploading documents
       column = tblPersonBiography.getColumnModel().getColumn(4);
        column.setMinWidth(120);
        column.setMaxWidth(120);
        column.setPreferredWidth(120);
        column.setResizable(false);
        column.setCellRenderer(new ColumnValueRenderer());;
        column.setCellEditor(columnValueEditor);
       //2793 End
    }

    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }

    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    public void saveAsActiveSheet() {
    }
    
    /** Allows to upload the word document for the selected row
     * @throws Exception in case of error occured
     */
    private void uploadWordDocument() throws Exception{
        int rowCount = tblPersonBiography.getRowCount();
        if(rowCount > 0 && tblPersonBiography.getCellEditor() != null){
            tblPersonBiography.getCellEditor().stopCellEditing();
        }
        int selectedRow = tblPersonBiography.getSelectedRow();
        if( selectedRow != -1 ){
            DepartmentBioPersonFormBean depBioBean =
                        (DepartmentBioPersonFormBean)vecBiographyData.elementAt(selectedRow);

            String acType = depBioBean.getAcType();
            DepartmentBioSourceFormBean depBioSourceBean = null;
            if( null == depBioBean.getDepartmentBioSourceFormBean() ) {
                depBioSourceBean = new DepartmentBioSourceFormBean();
            }else{
                depBioSourceBean = depBioBean.getDepartmentBioSourceFormBean();
            }
            
            depBioSourceBean.setPersonId(personId);
            depBioSourceBean.setBioNumber(depBioBean.getBioNumber());
            
            fileChooser = new CoeusFileChooser(mdiForm);
            fileChooser.setSelectedFileExtension(WORD_FORMAT);
            fileChooser.showFileChooser();
            if(fileChooser.isFileSelected()){
                String fileName = fileChooser.getSelectedFile();
                if(fileName != null && !fileName.trim().equals("")){
                    int index = fileName.lastIndexOf('.');
                    if(index != -1 && index != fileName.length()){
                        String extension = fileName.substring(index+1,fileName.length());
                        if(extension != null && (extension.equalsIgnoreCase(WORD_FORMAT) )){
                            if( depBioBean.isHasBioSource() ){
                                depBioSourceBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }else{
                                depBioSourceBean.setAcType(TypeConstants.INSERT_RECORD);
                            }
                            if( null == acType ) {
                                depBioBean.setAcType( TypeConstants.UPDATE_RECORD );
                            }
                            depBioSourceBean.setFileBytes(fileChooser.getFile());
                            depBioBean.setDepartmentBioSourceFormBean( depBioSourceBean );
                            vecBiographyData.setElementAt(depBioBean, selectedRow );
                            saveRequired = true;
                            setBiographyInfo();
//                            if( !saveToDataBase(proposalNarrativePDFSourceBean, WORD_FORMAT) ){
//                                CoeusOptionPane.showErrorDialog(
//                                    coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6607"));
//                            }else{
//                                proposalNarrativeFormBean.setHasSourceModuleNumber(true);
//                                tblModule.getModel().setValueAt(new Boolean(true), selectedRow, 0);
//                                tblModule.setRowSelectionInterval(selectedRow, selectedRow);
//                            }
//                            return;
                        }else{
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                            "proposal_narr_exceptionCode.6608"));
                            return;
                        }
                    }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                        "correspType_exceptionCode.1012"));
                    }
                }
            }
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }

    private void uploadPdfDocument() throws Exception{
        int rowCount = tblPersonBiography.getRowCount();
        if(rowCount > 0 && tblPersonBiography.getCellEditor() != null){
            tblPersonBiography.getCellEditor().stopCellEditing();
        }
        int selectedRow = tblPersonBiography.getSelectedRow();
        if( selectedRow != -1 ){
            if(validateData()){
                DepartmentBioPDFPersonFormBean deptBioPdfbean = 
                    new DepartmentBioPDFPersonFormBean();
                DepartmentBioPersonFormBean deptBioBean = 
                    (DepartmentBioPersonFormBean)vecBiographyData.get(selectedRow);

                String acType = deptBioBean.getAcType();

                deptBioPdfbean.setPersonId(deptBioBean.getPersonId());
                deptBioPdfbean.setBioNumber(deptBioBean.getBioNumber());

                fileChooser = new CoeusFileChooser(mdiForm);
                //Modified for case 3685 - Remove Word icons - start
                //fileChooser.setSelectedFileExtension(PDF_FORMAT);
                fileChooser.setAcceptAllFileFilterUsed(true);       
                //Modified for case 3685 - Remove Word icons - end
                fileChooser.showFileChooser();
                if(fileChooser.isFileSelected()){
                    String fileName = fileChooser.getSelectedFile();
                    if(fileName != null && !fileName.trim().equals("")){
                        int index = fileName.lastIndexOf('.');
                        if(index != -1 && index != fileName.length()){
                            String extension = fileName.substring(index+1,fileName.length());
                            //Modified for case 3685 - Remove Word icons - start
                            if(extension != null){// && (extension.equalsIgnoreCase(PDF_FORMAT) )){
                            //Modified for case 3685 - Remove Word icons - end
                                if( deptBioBean.isHasBioPDF() ){
                                    deptBioPdfbean.setAcType(TypeConstants.UPDATE_RECORD);
                                }else{
                                    deptBioPdfbean.setAcType(TypeConstants.INSERT_RECORD);
                                }
                                if( null == acType ) {
                                    deptBioBean.setAcType( TypeConstants.UPDATE_RECORD );
                                }
                                deptBioPdfbean.setFileBytes(fileChooser.getFile());
                                deptBioPdfbean.setFileName(fileChooser.getFileName().getName());
                                //added with 4007: Icon based on mime type : Start
                                CoeusDocumentUtils docUtils  = CoeusDocumentUtils.getInstance();
                                deptBioPdfbean.setMimeType(docUtils.getDocumentMimeType(deptBioPdfbean));
                                //4007 End
                                deptBioBean.setDepartmentBioPDFPersonFormBean( deptBioPdfbean );
                                vecBiographyData.setElementAt(deptBioBean, selectedRow );
                                saveRequired = true;
                                setBiographyInfo();
                            }else{
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                "proposal_narr_exceptionCode.6609"));
                                return;
                            }
                        }else{
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                            "correspType_exceptionCode.1012"));
                        }
                    }
                }
        }
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }
    
    /** Allows to view the word document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewWordDocument() throws Exception{
        
        int rowCount = tblPersonBiography.getRowCount();
        if(rowCount > 0 && tblPersonBiography.getCellEditor() != null){
            tblPersonBiography.getCellEditor().stopCellEditing();
        }
        
        int selectedRow = tblPersonBiography.getSelectedRow();
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        if( selectedRow != -1 ){
            DepartmentBioSourceFormBean deptSourceBean = new DepartmentBioSourceFormBean();
            DepartmentBioPersonFormBean deptBioBean = 
                (DepartmentBioPersonFormBean)vecBiographyData.get(selectedRow);

            deptSourceBean.setPersonId(deptBioBean.getPersonId());
            deptSourceBean.setBioNumber(deptBioBean.getBioNumber());
            
            RequesterBean request = new RequesterBean();
            //request.setFunctionType(GET_PERSON_BIO_SOURCE);
            //request.setDataObject(deptSourceBean);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", deptSourceBean);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.departmental.DepartmentalPrintReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("REPORT_TYPE", ""+GET_PERSON_BIO_SOURCE);
            documentBean.setParameterMap(map);
            request.setDataObject(documentBean);
            request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator comm =
                new AppletServletCommunicator(STREAMING_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
//                Vector vecData = response.getDataObjects();
//                if( vecData != null && vecData.size() > 0 ){
//                    String url = (String)vecData.get(0);
//                    if(url== null ) return ;// Added by chandra. 05/04/2004
//                    url = url.replace('\\', '/');
//                    URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//                    if(coeusContext != null){
//                        coeusContext.showDocument( templateURL, "_blank" );
//                    }else{
//                        javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                        bs.showDocument(templateURL);
//                    } 
//                }
                map = (Map)response.getDataObject();
                String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                url = url.replace('\\', '/');
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }else{
                CoeusOptionPane.showInfoDialog(response.getMessage());
            }
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }
    
    /** Allows to view the PDF document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewPdfDocument() throws Exception{
         int rowCount = tblPersonBiography.getRowCount();
        if(rowCount > 0 && tblPersonBiography.getCellEditor() != null){
            tblPersonBiography.getCellEditor().stopCellEditing();
        }
        
        int selectedRow = tblPersonBiography.getSelectedRow();
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        if( selectedRow != -1 ){
            DepartmentBioPDFPersonFormBean deptBioPdfBean = 
                new DepartmentBioPDFPersonFormBean();
            DepartmentBioPersonFormBean deptBioBean = 
                (DepartmentBioPersonFormBean)vecBiographyData.get(selectedRow);

            deptBioPdfBean.setPersonId(deptBioBean.getPersonId());
            deptBioPdfBean.setBioNumber(deptBioBean.getBioNumber());
            
            RequesterBean request = new RequesterBean();
            //request.setFunctionType(GET_PERSON_BIO_PDF);
            //request.setDataObject(deptBioPdfBean);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", deptBioPdfBean);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.departmental.DepartmentalPrintReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("REPORT_TYPE", ""+GET_PERSON_BIO_PDF);
            documentBean.setParameterMap(map);
            request.setDataObject(documentBean);
            request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(STREAMING_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
//                Vector vecData = response.getDataObjects();
//                if( vecData != null && vecData.size() > 0 ){
//                    String url = (String)vecData.get(0);
//                    if(url== null) return ;// Added by chandra. 05/04/2004.
//                    url = url.replace('\\', '/');
//                    URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//                    if(coeusContext != null){
//                        coeusContext.showDocument( templateURL, "_blank" );
//                    }else{
//                        javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                        bs.showDocument(templateURL);
//                    }
//
//                }
                map = (Map)response.getDataObject();
                String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                url = url.replace('\\', '/');
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }else{
                CoeusOptionPane.showInfoDialog(response.getMessage());
            }
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting()) {
            int selRow = tblPersonBiography.getSelectedRow();
            if(selRow!= -1){
                boolean isPdf = ((Boolean)tblPersonBiography.getValueAt(selRow,1)).booleanValue();
                //Commented for case 3685 - Remove Word icons - start
                //boolean isWord = ((Boolean)tblPersonBiography.getValueAt(selRow,0)).booleanValue();
                //Commented for case 3685 - Remove Word icons - end
                biographyViewPDF.setEnabled(isPdf);
                //Commented for case 3685 - Remove Word icons - start
//                biographyViewWord.setEnabled(isWord);
                //Commented for case 3685 - Remove Word icons - end
                
                if(functionType==DISPLAY_MODE){
                    biographyViewPDF.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - start
//                    biographyViewWord.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - end
                }
                DepartmentBioPersonFormBean deptPersonFormBean = 
                        (DepartmentBioPersonFormBean)vecBiographyData.get(selRow);
                if(deptPersonFormBean != null){
                     if(deptPersonFormBean.getUpdateTimestamp()!=null){
                            txtUpdateUser.setText(UserUtils.getDisplayName(deptPersonFormBean.getUpdateUser())+ " at "+
                                CoeusDateFormat.format(deptPersonFormBean.getUpdateTimestamp().toString()));
                     }else{
                            txtUpdateUser.setText("");
                     }
                }
                //Added for case 3685 - Remove Word icons - start
                DepartmentBioPDFPersonFormBean deptBioPDFPersonFormBean = null;
                if(deptPersonFormBean != null){
                    deptBioPDFPersonFormBean = deptPersonFormBean.getDepartmentBioPDFPersonFormBean();
                }
                if(deptBioPDFPersonFormBean != null            
                    && deptBioPDFPersonFormBean.getUpdateTimestamp()!=null){
                    txtLastDocUpdateUser.setText(deptBioPDFPersonFormBean.getUpdateUser() + " at "+
                        CoeusDateFormat.format(deptBioPDFPersonFormBean.getUpdateTimestamp().toString()));
                    txtFileName.setText(deptBioPDFPersonFormBean.getFileName());
                } else {
                    txtLastDocUpdateUser.setText("");
                    txtFileName.setText("");
                }
                //Added for case 3685 - Remove Word icons - start
            }
        }
    }    
    

    /**
    * This is a Default Cell Editor for the description column in the Table.
    */
    public class ColumnValueEditor extends AbstractCellEditor//DefaultCellEditor
                                                implements TableCellEditor,ItemListener {
        private JTextField txtDesc;
        private CoeusComboBox cmbDocType;//2793
        private int selectedRow ;
        private int selectedCol ;
        private String stDescription;
        boolean temporary;
        // Constructor which sets the size of TextField
        ColumnValueEditor(int len ){
            //Case 2793:NOW Person Maintainer - Uploading documents
            instanceCreated = true;
//            super(new JTextField());
            cmbDocType = new CoeusComboBox();
            cmbDocType.addItemListener(this);
            //2793 End
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            txtDesc.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){

                    if (!fe.isTemporary() ){
                       if(!temporary){
                            stopCellEditing();
                       }
                    }
                }
            });
        }
        
        /** This overridden to get the custom cell component in the
         * JTable.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param column column index
         * @return a Component which is a editor component to the JTable cell.
         */
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){

            selectedRow = row;
            selectedCol = column;
            stDescription = (String)tblPersonBiography.getValueAt(row,2);
            if(stDescription == null){
                stDescription = "";
            }
            //Case 2793:NOW Person Maintainer - Uploading documents
            if(selectedCol == 2){
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            this.selectedRow = row;
            return txtDesc;
            }else if(selectedCol == 4){
                if(instanceCreated) {
                    populateDocumentTypeCode();
//                    typeComboPopulated = true;
                    instanceCreated = false;
                }
                cmbDocType.setSelectedItem(value);
                return cmbDocType;
            }
            return null;
            //2793 End
        }
        // This overridden method is to enable editor in a single mouse click
        public int getClickCountToStart(){
            return 1;
        }
        
        /** Returns the value contained in the editor.
         * @return a value contained in the editor
         */
        public Object getCellEditorValue() {
            //Case 2793:NOW Person Maintainer - Uploading documents
            if(selectedCol == 2){
                return ((JTextField)txtDesc).getText();
            }else if(selectedCol == 4){
                Object data = ((CoeusComboBox)cmbDocType).getSelectedItem();
                String selectedValue = "";
                if(data!= null){
                    selectedValue = data.toString();
                }
                return selectedValue;
            }
            return null;
            //2793 End
        }

        public boolean isCellEditable(java.util.EventObject anEvent ){
            if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                return false;
            }
            return true;
        }
        // This method validates the editing value in the description column.
        
        private void validateEditorComponent(){
            temporary = true;
            String editingValue = (String) getCellEditorValue();
            if (editingValue != null && editingValue.trim().length() > 0) {
                if(!(selectedCol == 2 && editingValue.equalsIgnoreCase(stDescription))){
                    setModel(editingValue);
                }
            }
        }

        // This method sets the Edited value in the description to the bean and set the Actype.
        
        private void setModel(String editingValue){
            if(vecBiographyData!= null && vecBiographyData.size() >0 && selectedRow!= -1 ){
                saveRequired=true;

                DepartmentBioPersonFormBean depBioBean =
                            (DepartmentBioPersonFormBean)vecBiographyData.elementAt(selectedRow);
                String aType = depBioBean.getAcType();
                //Case 2793:NOW Person Maintainer - Uploading documents
                if(selectedCol == 2 ){
                    depBioBean.setDescription(editingValue);
                }else if(selectedCol == 4){
                    int code = Integer.parseInt(((ComboBoxBean)cmbDocType.getSelectedItem()).getCode());
                    String desc = ((ComboBoxBean)cmbDocType.getSelectedItem()).getDescription();
                    depBioBean.setDocumentTypeCode(code);
                    depBioBean.setDocumentTypeDescription(desc);
                }
                //2793 End
                if(aType != null){
                  if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                         depBioBean.setAcType(UPDATE_RECORD);
                  }
                }else{
                    depBioBean.setAcType(UPDATE_RECORD);
                }
            }
        }
        
        /**
        * Forwards the message from the CellEditor to the delegate. Tell the 
        * editor to stop editing and accept any partially edited value as the 
        * value of the editor.
        * @return true if editing was stopped; false otherwise
        */
        public boolean stopCellEditing() {
            validateEditorComponent();
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
        //Case 2793:NOW Person Maintainer - Uploading documents
        
        /** Populate the document type codes available for the persons
         */
        public  void populateDocumentTypeCode(){
            int size = vecDocumentType.size();
            ComboBoxBean comboBoxBean;
            cmbDocType.addItem(new ComboBoxBean("",""));
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)vecDocumentType.get(index);
                cmbDocType.addItem(comboBoxBean);
            }
        }
        
        /** Listener for the document type combobox values
         */
        public void itemStateChanged(ItemEvent itemEvent) {
            Object source = itemEvent.getSource();
            if(source.equals(cmbDocType)){
                if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
                    stopCellEditing();
                }
            }
        }
        //2793 End
    }
    
    /**
     * This inner class is used for setting the button editor to the 
     * first and second column for viewing the Word and PDf functionality.
     * At present these features are not implemented.
     */
    
    public class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private int selRow ;
        private int selCol;
        ImageIcon wordIcon=null, pdfIcon=null;

        public ButtonEditor(JCheckBox cf ) {

            super(cf);
            wordIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON));
            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
            button = new JButton();
            button.setOpaque(true);
            //button.setEnabled(false);
            button.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent actionEvent){
                  try{
                      cancelCellEditing();
                      if( selCol == 0 ) {
                          //Commented for case 3685 - Remove Word icons - start
//                          if(tblPersonBiography.getRowCount() > 0){
//                            viewWordDocument();
//                          }
                          //Commented for case 3685 - Remove Word icons - end
                      }else if( selCol == 1){
                          if(tblPersonBiography.getRowCount() > 0){
                            viewPdfDocument();
                          }
                      }
                  }catch(Exception exception){
                      exception.printStackTrace();
                      CoeusOptionPane.showInfoDialog(exception.getMessage());
                  }                      
                  /*CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "funcNotImpl_exceptionCode.1100"));*/
                  //CoeusOptionPane.showErrorDialog("Functionalty not implemented");
              }
            });
            }
        /** This overridden to get the custom cell component in the
         * JTable.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param column column index
         * @return a Component which is a editor component to the JTable cell.
         */
        public Component getTableCellEditorComponent(JTable table, Object value,
                         boolean isSelected, int row, int column) {

            this.selRow = row;
            this.selCol = column;
            boolean showIcon=false;    
            if( value != null ) {
            showIcon = ( (Boolean) value ).booleanValue();
            }
            if( showIcon ) {
                if( column == 0) {
                    //Commented for case 3685 - Remove Word icons - start
                   // button.setIcon( wordIcon );
                    //Commented for case 3685 - Remove Word icons - end
                }else{
                    //Modified with case 4007: Icon based on mime Type: Start
                    // # Case 3855 start  Added icon for button corrosponding to the file extension
//                       button.setIcon(UserUtils.getAttachmentIcon((String) table.getModel().getValueAt(row,3)));
                    // button.setIcon( pdfIcon );
                    // # Case 3855 end
                    CoeusAttachmentBean attachment = (CoeusAttachmentBean) ((DefaultTableModel)table.getModel()).getValueAt(row,3);
                    CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                    button.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                    //4007:End
                }
            }else{
                button.setIcon( null );
            }
          return button;
        }

        // This overridden method is to enable editor in a single mouse click
        public int getClickCountToStart(){
            return 1;
        }
        /**
        * Forwards the message from the CellEditor to the delegate. Tell the 
        * editor to stop editing and accept any partially edited value as the 
        * value of the editor.
        * @return true if editing was stopped; false otherwise
        */
        public boolean stopCellEditing() {
          cancelCellEditing();  
          return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
          cancelCellEditing();
          super.fireEditingStopped();
        }
    }
    
    // This inner class is used for rendering the description column in the Table
    
    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {

        private int selRow;
        private int selCol;
        
        // constructor which sets the Font and opache to true
        
        public ColumnValueRenderer() {
          setOpaque(true);
      setFont(CoeusFontFactory.getNormalFont());
        }
        /** An overridden method to render the component(icon) in cell.
         * foreground/background for this cell and Font too.
         *
         * @param table the JTable that is asking the renderer to draw;
         * can be null
         * @param value the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value. For example,
         * if value is the string "true", it could be rendered as a string or
         * it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited, render
         * in the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the
         * header, the value of row is -1
         * @param column the column index of the cell being drawn
         * @return Component which is to be rendered.
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
         * boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {

              selRow = row;
              selCol = column;

              setText( (value == null) ? "" : value.toString() );
              if( functionType == CoeusGuiConstants.DISPLAY_MODE) {
                setBackground((Color)javax.swing.UIManager.getDefaults().get("Panel.background"));
              }
              return this;
        }
     }

    // This inner class is used for rendering the Button to the Column 1 and 2 in the Table
    
    public class ButtonRenderer extends JButton implements TableCellRenderer {

        ImageIcon wordIcon=null, pdfIcon=null;
        
        public ButtonRenderer() {
            setOpaque(true);
            wordIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON));
            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end

        }
        /** An overridden method to render the component(icon) in cell.
         * foreground/background for this cell and Font too.
         *
         * @param table the JTable that is asking the renderer to draw;
         * can be null
         * @param value the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value. For example,
         * if value is the string "true", it could be rendered as a string or
         * it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited, render
         * in the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the
         * header, the value of row is -1
         * @param column the column index of the cell being drawn
         * @return Component which is to be rendered.
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
         * boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          
                boolean showIcon=false;    
                if( value != null ) {
                    showIcon = ( (Boolean) value ).booleanValue();
                }
                if( showIcon ) {
                    if( column == 0) {
                        //Commented for case 3685 - Remove Word icons - start
                       // setIcon( wordIcon );
                        //Commented for case 3685 - Remove Word icons - end
                    }else{
                        //Modified with case 4007: Icon based on mime Type: Start
                        // # Case 3855 start  Added icon for button corrosponding to the file extension
//                        setIcon(UserUtils.getAttachmentIcon((String) table.getModel().getValueAt(row,3)));
                        // button.setIcon( pdfIcon );
                        // # Case 3855 end
                        
                        CoeusAttachmentBean attachment = (CoeusAttachmentBean) ((DefaultTableModel) table.getModel()).getValueAt(row,3);
                        CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                        setIcon(docTypeUtils.getAttachmentIcon(attachment));
                        //4007:End
                        //  setIcon( pdfIcon );
                    }
                }else{
                    setIcon( null );
                }
                return this;
        }
     }
    
    //Case 2793:NOW Person Maintainer - Uploading documents
    //To fetch the available document Types
    private Vector getDocumentTypeCodes() {
        Vector dataObjects = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('z');
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                dataObjects = response.getDataObjects();
            }
        }
        return dataObjects;
    }
    
    /** Get the document type details in comboboxBean
     *@param document type code
     *@returns Comboboxbean for the given document type code
     */
    private ComboBoxBean getDocumentComboData(int typeCode){
        ComboBoxBean comboBean = null;
        ComboBoxBean dataBean = null;
        if(vecDocumentType!= null && vecDocumentType.size() > 0){
            for(int index = 0; index < vecDocumentType.size(); index++){
                comboBean = (ComboBoxBean)vecDocumentType.get(index);
                int code = Integer.parseInt(comboBean.getCode());
                if(code == typeCode ){
                    dataBean = comboBean;
                    break;
                }
            }
        }
        if(dataBean==null){
            dataBean = new ComboBoxBean("","");
        }
        return dataBean;
    }
    
    /** Check for the duplicate document type code
     *@param containing the bio info for a person
     *@returns is boolean if it is duplicate
     */
    private boolean isDuplicateDocumentType(){
        boolean duplicate = false;
        CoeusVector cvData = new CoeusVector();
        ComboBoxBean codeBaseBean = null;
        if(vecBiographyData!= null && vecBiographyData.size() > 0){
            for(int index = 0; index < vecBiographyData.size(); index++){
                DepartmentBioPersonFormBean bean = (DepartmentBioPersonFormBean)vecBiographyData.get(index);
                codeBaseBean = new ComboBoxBean();
                codeBaseBean.setCode(String.valueOf(bean.getDocumentTypeCode()));
                codeBaseBean.setDescription(bean.getDocumentTypeDescription());
                cvData.addElement(codeBaseBean);
            }
            for(int ind = 0; ind < cvData.size(); ind++){
                ComboBoxBean comBean = (ComboBoxBean)cvData.get(ind);
                Equals eqData = new Equals("code",comBean.getCode());
                CoeusVector cvValueData = cvData.filter(eqData);
                if(cvValueData!= null && cvValueData.size() > 1){
                    duplicate = true;
                    break;
                }
            }
        }
        return duplicate;
    }
    //2793 End
}
