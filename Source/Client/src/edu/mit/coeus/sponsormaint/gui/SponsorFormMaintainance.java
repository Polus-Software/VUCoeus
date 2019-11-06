/*
 * SponsorFormMaintainance.java
 *
 * Created on August 18, 2004, 3:02 PM
 */

package edu.mit.coeus.sponsormaint.gui;


import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.sponsormaint.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
import java.io.*;
import java.text.SimpleDateFormat;


/**
 *
 * @author  chandrashekara
 */
public class SponsorFormMaintainance extends javax.swing.JComponent 
implements ListSelectionListener,ActionListener{
    
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
    //Code commented and modified for Case#3648 - Uploading non-pdf files
//    private static final String POST_SCRIPT = "ps";
    private static final String []FILE_TYPES = {"ps", "xml", "xsl"};
    

    private CoeusFileChooser fileChooser;
    //private boolean isTableDataChanged =false;
    
    
    private static final String TIME_STAMP = "MM/dd/yyyy";
    private static final String REQUIRED_DATE_FORMAT = TIME_STAMP +" hh:mm:ss ";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    
    
    
    private SponsorPackageTableModel sponsorPackageTableModel;
    private SponsorPageTableModel sponsorPageTableModel;
    private SponsorPackageTableCellEditor sponsorPackageTableCellEditor;
    private SponsorPageTableCellEditor sponsorPageTableCellEditor;
    private SponsorPackageRenderer sponsorPackageRenderer;
    private SponsorPageRenderer sponsorPageRenderer;
    private CoeusMessageResources  coeusMessageResources;
    private DateUtils dtUtils = new DateUtils();
    
    private final String connect = CoeusGuiConstants.CONNECTION_URL + "/spMntServlet";
    private static final char  GET_SPONSOR_DATA = 'K';
    private static final char UPDATE_SPONSOR_TEMPLATE = 'S'; 
    private static final char GET_SPONSOR_TEMPLATE = 'T'; 
    private static final char UPDATE_FORM_DATA = 'V';
    private static final char GET_CLOB_DATA = 'X'; 
    
    private String sponsorCode=EMPTY_STRING;
    private String sponsorName = EMPTY_STRING;
    private boolean modified;
//    private boolean packageModified;
    
    private SponsorFormsBean  sponsorFormsBean = null;
    private SponsorTemplateBean sponsorTemplateBean = null;
    private int pageRowId ;
    private int packageRowId;
    private String queryKey=EMPTY_STRING;
    private int sponsorPackageNumber=1;
    private int sponsorPageNumber=1;
    private String mesg = EMPTY_STRING;
    private boolean canPackageExists;
    int selPackageNumber;
  
    private static final String DELETE_CONFIRMATION = "Are you sure you want to delete the record?";
    private static final String CANT_DELETE_PACKAGE = "Can not delete as pages exists for the package";
    private static final String PACKAGE_SSHLD_CONTAIN_PAGE = "Package should have atleast one page";
    
    private static final String SELECT_PKG_TO_DELETE = "Select a package to be deleted. ";
    private static final String SELECT_PAGE_TO_DELETE = "Select a page to be deleted. ";
    private static final String DELETE_PAGE = "Are you sure you want to delete the selected page? ";
    private static final String PAGE_TEMPLATE_EXISTS = "Record already exits. Do you want to overwrite it? ";
    private static final String ENTER_NAME_FOR_PACKAGAE = "Enter the name for the package";
    //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
    private boolean isSponsorHierarchy;
    private String hierarchyName = CoeusGuiConstants.EMPTY_STRING;
    private String groupName = CoeusGuiConstants.EMPTY_STRING;
    private static final char GET_HIERARCHY_SPONSOR_DATA = 'k';
    private static final char GET_HIERAR_GROUP_MAX_PACKAGE_NUMBER = 'F';
    private static final char GET_PACKAGE_MAX_PAGE_NUMBER ='f';
    private int hierarchypackageNumber = 0;
    private static final String PACKAGE_NUMBER = "packageNumber";
    private static final String SPONSOR_HIERARCHY = "SPONSOR_HIERARCHY";
    private static final String SPONSOR_CODE = "SPONSOR_CODE";
    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String ADD_PACKAGE_BEFORE_ADDING_A_PAGE = "maintainSponsorForm_exceptionCode.1001";
    private static final String FORM_CANNOT_BE_LOAD_WITHOUT_ADDING_A_PAGE = "maintainSponsorForm_exceptionCode.1002";
    private static final String NO_PAGE_TO_DOWNLOAD = "maintainSponsorForm_exceptionCode.1003";
    //Case#2445 - End    
    
    /** Creates new form SponsorFormMaintainance */
    public SponsorFormMaintainance() {
        initComponents();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setTableEditors();
        postInitComponents();
    }
    
    //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
    /**
     *  Creates new form SponsorFormMaintainance for SponsorHierarchy
     */
    public SponsorFormMaintainance(String hierarchyName, String groupName) {
        isSponsorHierarchy = true;
        this.hierarchyName = hierarchyName;
        this.groupName = groupName;
        setSponsorCode("HIERAR");
        initComponents();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setTableEditors();
        postInitComponents();
    }
    //Case#2445 - End

    private void registerComponents(){
        
        
        sponsorPackageTableModel = new SponsorPackageTableModel();
        sponsorPageTableModel = new SponsorPageTableModel();
        
        sponsorPackageTableCellEditor = new SponsorPackageTableCellEditor();
        sponsorPageTableCellEditor = new SponsorPageTableCellEditor();
        sponsorPackageRenderer = new SponsorPackageRenderer();
        sponsorPageRenderer = new SponsorPageRenderer();
        
        tblPackage.setModel(sponsorPackageTableModel);
        tblPages.setModel(sponsorPageTableModel);
        
        
        tblPackage.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPages.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPackage.getSelectionModel().addListSelectionListener(this);
        
        btnCancel.addActionListener(this);
        btnDownLoad.addActionListener(this);
        btnLoad.addActionListener(this);
        btnOk.addActionListener(this);
        btnPackageAdd.addActionListener(this);
        btnPackageDelete.addActionListener(this);
        btnPageAdd.addActionListener(this);
        btnPageDelete.addActionListener(this);
        
        
        java.awt.Component[] components = {
            btnOk,btnCancel,btnPackageAdd,btnPackageDelete,btnPageAdd,btnPageDelete,btnLoad,btnDownLoad
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
     }
    
    private void setTableEditors(){
         tblPackage.setTableHeader(null);
//        JTableHeader tablePackageHeader = tblPackage.getTableHeader();
//        tablePackageHeader.setReorderingAllowed(false);
//        tablePackageHeader.setFont(CoeusFontFactory.getLabelFont());
        tblPackage.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPackage.setRowHeight(22);
        tblPackage.setSelectionBackground(java.awt.Color.white);
        tblPackage.setSelectionForeground(java.awt.Color.black);
        tblPackage.setShowHorizontalLines(true);
        tblPackage.setShowVerticalLines(true);
        tblPackage.setOpaque(false);
        tblPackage.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        tblPages.setTableHeader(null);
//        JTableHeader tblPageHeader = tblPages.getTableHeader();
//        tblPageHeader.setReorderingAllowed(false);
//        tblPageHeader.setFont(CoeusFontFactory.getLabelFont());
        tblPages.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPages.setRowHeight(22);
        tblPages.setSelectionBackground(java.awt.Color.white);
        tblPages.setSelectionForeground(java.awt.Color.black);
        tblPages.setShowHorizontalLines(true);
        tblPages.setShowVerticalLines(true);
        tblPages.setOpaque(false);
        tblPages.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        TableColumn packageColumn = tblPackage.getColumnModel().getColumn(0);
        // This is a hidden column to keep track of page number.
        packageColumn.setMinWidth(0);
        packageColumn.setMaxWidth(0);
        packageColumn.setPreferredWidth(0);
        packageColumn.setResizable(true);
        
        packageColumn = tblPackage.getColumnModel().getColumn(1);
        packageColumn.setPreferredWidth(449);
        packageColumn.setResizable(true);
        packageColumn.setCellRenderer(sponsorPackageRenderer);
        packageColumn.setCellEditor(sponsorPackageTableCellEditor);
        
        
        TableColumn pageColumn = tblPages.getColumnModel().getColumn(0);
        pageColumn.setPreferredWidth(37);
        pageColumn.setCellEditor(sponsorPageTableCellEditor);
        pageColumn.setCellRenderer(sponsorPageRenderer);
        pageColumn.setResizable(true);
        
        pageColumn= tblPages.getColumnModel().getColumn(1);
        pageColumn.setPreferredWidth(200);
        pageColumn.setCellEditor(sponsorPageTableCellEditor);
        pageColumn.setCellRenderer(sponsorPageRenderer);
        pageColumn.setResizable(true);
        
        pageColumn= tblPages.getColumnModel().getColumn(2);
        pageColumn.setPreferredWidth(120);
        pageColumn.setCellEditor(sponsorPageTableCellEditor);
        pageColumn.setCellRenderer(sponsorPageRenderer);
        pageColumn.setResizable(true);
        
        pageColumn= tblPages.getColumnModel().getColumn(3);
        pageColumn.setPreferredWidth(105);
        pageColumn.setCellEditor(sponsorPageTableCellEditor);
        pageColumn.setCellRenderer(sponsorPageRenderer);
        pageColumn.setResizable(true);
    }
    
    public void setFormData(){
        //Modified  for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        cvPackageData= new CoeusVector();
        cvPageData = new CoeusVector();
        cvDeletedPackageData = new CoeusVector();
        cvDeletedPageData = new CoeusVector();
        //Load hierarchy related forms and templates if isSponsorHierarchy is true else load sponsor form for individual sponsor 
        if(isSponsorHierarchy){
            lblSponsorCode.setText("Hierarchy Name: " +hierarchyName);
            lblSponsorName.setText("Group Name: " +groupName);
            queryKey = getSponsorCode();
            SponsorFormsBean sponsorFormsBean = new SponsorFormsBean();
            sponsorFormsBean.setSponsorCode(queryKey);
            sponsorFormsBean.setGroupName(groupName);
            Hashtable htSponsorData = null;
            try{
                htSponsorData = getHierarchySponsorData(sponsorFormsBean);
            }catch (CoeusUIException coeusUIException) {
                CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
                return;
            }
            if(htSponsorData != null && htSponsorData.size() > 0){
                cvPackageData = (CoeusVector) htSponsorData.get(KeyConstants.PACKAGE_DATA);
                cvPackageData.sort(PACKAGE_NUMBER,false);
                cvPageData = (CoeusVector)htSponsorData.get(KeyConstants.PAGE_DATA);
                sponsorPackageTableModel.setData(cvPackageData);
            }
            
        }else{//Case#2445 - End
            lblSponsorCode.setText("Sponsor Code: " +getSponsorCode());
            lblSponsorName.setText("Sponsor Name: " +getSponsorName());
            queryKey = getSponsorCode();
            Hashtable htSponsorData = getSponsorData(getSponsorCode());
            if(htSponsorData != null && htSponsorData.size() > 0){ 
                cvPackageData = (CoeusVector) htSponsorData.get(KeyConstants.PACKAGE_DATA);
                cvPackageData.sort(PACKAGE_NUMBER,false);
                cvPageData = (CoeusVector)htSponsorData.get(KeyConstants.PAGE_DATA);
                sponsorPackageTableModel.setData(cvPackageData);
            }

        }
    }
    
    private Hashtable getSponsorData(String sponsorCode){
        Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setFunctionType(GET_SPONSOR_DATA);
        requester.setDataObject(sponsorCode);
        
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
        if(source.equals(btnOk)){
             performOKAction();
        }else if(source.equals(btnPackageAdd)){
            performPackageAddAction();
        }else if(source.equals(btnPageAdd)){
            performPageAddAction();
        }else if(source.equals(btnPackageDelete)){
            performPackageDeleteAction();
        }else if(source.equals(btnCancel)){
            cancelSposnorAction();
        }else if(source.equals(btnPageDelete)){
            performDeletePageAction();
        }else if(source.equals(btnLoad)){
            performLoadAction();
        }else if(source.equals(btnDownLoad)){
            performDownLoadAction();
        }
    }
    
    private void performOKAction(){
        
        if(tblPackage.getRowCount()>0){
            sponsorPackageTableCellEditor.stopCellEditing();
        }
        
        if(tblPages.getRowCount()>0){
            sponsorPageTableCellEditor.stopCellEditing();
        }
        
        try{
            if(modified ){
                if(validatePackageData()){
                    saveFormData();
                    dlgSponsorMaintainance.setVisible(false);
                }
            }else{
                dlgSponsorMaintainance.setVisible(false);
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    private void cancelSposnorAction(){
        if(tblPackage.getRowCount()>0){
            sponsorPackageTableCellEditor.stopCellEditing();
        }
        
        if(tblPages.getRowCount()>0){
            sponsorPageTableCellEditor.stopCellEditing();
        }
        if(modified){   
               confirmClosing();
        }else{
            dlgSponsorMaintainance.dispose();
        }
    }
    
    // check whether the AcType of two beans are changed or not.
    private boolean isAcTypeChanged(){
        SponsorFormsBean sponsorFormsBean = null;
        SponsorTemplateBean  sponsorTemplateBean=null;
        
        if(cvPackageData!= null && cvPackageData.size()>0){
            for(int packageIndex=0; packageIndex<cvPackageData.size(); packageIndex++){
                sponsorFormsBean = (SponsorFormsBean)cvPackageData.get(packageIndex);
                if(sponsorFormsBean.getAcType()==null){
                    return false;
                }else{
                    return true;
                }
            }
        }
        
         if(cvPageData!= null && cvPageData.size()>0){
            for(int pageIndex=0; pageIndex<cvPageData.size(); pageIndex++){
                sponsorTemplateBean = (SponsorTemplateBean)cvPageData.get(pageIndex);
                if(sponsorTemplateBean.getAcType()==null){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return true;
    }// End isAcTypeChanged
    
    
     /** Confirm before closing the BudgetPersons dialog box */    
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                 if(validatePackageData()){
                        saveFormData();
                        dlgSponsorMaintainance.setVisible(false);
                }
                
            }else if(option == CoeusOptionPane.SELECTION_NO){
                dlgSponsorMaintainance.dispose();
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(CoeusException exception){
            exception.printStackTrace();
            exception.getMessage();
        }
    }
    
    private void saveFormData()throws CoeusException{
        
        CoeusVector packData = new CoeusVector();
        CoeusVector pageData = new CoeusVector();
        Hashtable returnData;
        Hashtable updateData = new Hashtable();
        
        if(cvDeletedPackageData!= null && cvDeletedPackageData.size()>0){
            packData.addAll(cvDeletedPackageData);
        }
        
         if(cvPackageData!= null && cvPackageData.size()>0){
            packData.addAll(cvPackageData);
        }
        
        if(cvDeletedPageData!= null && cvDeletedPageData.size()>0){
            pageData.addAll(cvDeletedPageData);
        }
        
        if(cvPageData!= null && cvPageData.size()>0){
            pageData.addAll(cvPageData);
        }
        // check for the integrated vector and then put into the hashtable
        if(packData!= null && packData.size()>0){
            updateData.put(KeyConstants.PACKAGE_DATA,packData);
        }
        
        if(pageData!= null && pageData.size()>0){
            updateData.put(KeyConstants.PAGE_DATA,pageData); 
        }
        getPackageExistingMaxId();
        returnData = updateFormData(updateData);
        
    }
    
    
    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
    /*
     * Method to get sponsor hiearchy group sponsor forms
     * @param sponsorFormsBean - holds the sponsor code and hierarchy group name(level1)
     * @return htData - holds SponsorFormsBean and SponsorTemplateBean
     * @throws CoeusUIException
     */
    private Hashtable getHierarchySponsorData(SponsorFormsBean sponsorFormsBean)throws CoeusUIException{
        Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setFunctionType(GET_HIERARCHY_SPONSOR_DATA);
        requester.setDataObject(sponsorFormsBean);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder != null){
            if(responder.isSuccessfulResponse()){
                htData = (Hashtable)responder.getDataObject();
            }
        }else{
             throw new CoeusUIException(coeusMessageResources.parseMessageKey("coeusApplet_exceptionCode.1147"));
        }
        return htData;
    }
    /*
     * Method to update package number to all SponsorFormsBean and SponsorTemplaeBean when package and pages are insert
     * @param packageData - holds SponsorFormsBean
     * @param pageData - holds SponsorTemplateBean
     */
    private void updatePackageNumber(CoeusVector packageData,CoeusVector pageData){
        if(packageData != null && packageData.size() > 0){
            Equals equals = new Equals("acType",TypeConstants.INSERT_RECORD);
            //filters newly insert record's'
            CoeusVector insertData = packageData.filter(equals);
            if(insertData != null && insertData.size() > 0){
                //gets max package number from database
                hierarchypackageNumber = getHierarchyMaxPackageNumber();
                for(int index = 0;index<insertData.size();index++){
                    SponsorFormsBean formsBean = (SponsorFormsBean)insertData.get(index);
                    if(formsBean != null){
                        int oldPackageNumber = formsBean.getPackageNumber();
                        int packageNumber = ++hierarchypackageNumber;
                        //update the bean with packagenumber
                        formsBean.setPackageNumber(packageNumber);
                        //filter the pageData vector with the old package number from sponsorFormsBean and update with new package number
                        Equals equal = new Equals(PACKAGE_NUMBER,new Integer(oldPackageNumber));
                        CoeusVector cvFilteredPageData = pageData.filter(equal);
                        if(cvFilteredPageData != null){
                            for(int pageIndex=0; pageIndex < cvFilteredPageData.size();pageIndex++){
                                SponsorTemplateBean templateBean = (SponsorTemplateBean)cvFilteredPageData.get(pageIndex);
                                if(templateBean != null){
                                    templateBean.setPackageNumber(packageNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /*
     * Method to update page number to all SponsorTemplatesBean's when pages are insert
     * @param packageData - holds SponsorFormsBean
     * @param pageData - holds SponsorTemplateBean
     */
    private void updatePackagePagesNumber(CoeusVector packageData,CoeusVector pageData){
        if(packageData != null && packageData.size() > 0){
            for(int packageIndex = 0 ;packageIndex<packageData.size();packageIndex++){
                SponsorFormsBean formsBean = (SponsorFormsBean)packageData.get(packageIndex);
                int packageNumber = formsBean.getPackageNumber();
                Equals eqPackage = new Equals(PACKAGE_NUMBER, new Integer(packageNumber));
                Equals eqPage = new Equals("acType",TypeConstants.INSERT_RECORD);
                And eqPageAndEqPackage = new And(eqPage, eqPackage);
                CoeusVector cvPage = pageData.filter(eqPageAndEqPackage);
                if(cvPage != null && cvPage.size() > 0){
                    //gets the max page number from DB based on package number 
                    int pageNumber = getPackageMaxPageNumber(packageNumber);
                    for(int pageIndex=0;pageIndex<cvPage.size();pageIndex++){
                        SponsorTemplateBean templateBean = (SponsorTemplateBean)cvPage.get(pageIndex);
                        templateBean.setPageNumber(++pageNumber);
                    }
                }
            }
            
        }
    }
    
    /*
     * Method to update page number to SponsorTemplatesBean when one page are insert
     * @param sponsorTemplateBean
     */
    private SponsorTemplateBean updatePackagePageNumber(SponsorTemplateBean sponsorTemplateBean){
        int pageNumber = getPackageMaxPageNumber(sponsorTemplateBean.getPackageNumber());
        sponsorTemplateBean.setPageNumber(++pageNumber);
        return sponsorTemplateBean;
    }
    
    private Hashtable updateFormData(Hashtable data) throws CoeusException{
        RequesterBean requester;
        ResponderBean responder;
        Hashtable returnSponsorData=null;
        requester = new RequesterBean();
        requester.setFunctionType(UPDATE_FORM_DATA);
        requester.setDataObject(data);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            returnSponsorData = (Hashtable)responder.getDataObject();
        }else{
//            System.out.println("There is a problem.");
            throw new CoeusException(responder.getMessage());
        }
        //modified = false;
        return returnSponsorData;
        
    }
    
    private boolean validatePackageData(){
        
        int packageRowCount = tblPackage.getRowCount();
        int pageRowCount = tblPages.getRowCount();
        
     //   if(packageRowCount>0){
            sponsorPackageTableCellEditor.stopCellEditing();
      //  }
        
     //   if(pageRowCount>0){
            sponsorPageTableCellEditor.stopCellEditing();
     //   }
        
        SponsorTemplateBean sponsorTemplateBean=null;
        SponsorFormsBean  sponsorFormsBean =null;
        
        if(cvPackageData!= null && cvPackageData.size() >0){
            for(int packageIndex=0; packageIndex<cvPackageData.size(); packageIndex++){
                sponsorFormsBean = (SponsorFormsBean)cvPackageData.get(packageIndex);
                int packageNumber = sponsorFormsBean.getPackageNumber();
                String packageDesc=  sponsorFormsBean.getPackageName();

                if(packageDesc==null || packageDesc.trim().equals(EMPTY_STRING)){
                    //Modified for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
//                     CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PACKAGE_SSHLD_CONTAIN_PAGE));
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_NAME_FOR_PACKAGAE));
                    //Case#2445 - End
                    tblPackage.setRowSelectionInterval(packageIndex, packageIndex);
                    tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
//                    tblPackage.editCellAt(packageIndex,PACKAGE_DESC);
//                    tblPackage.getEditorComponent().requestFocusInWindow();
                    return false;
                }//End if check for package
                
                CoeusVector cvPages = cvPageData.filter(new Equals(PACKAGE_NUMBER,new Integer(packageNumber)));
                
                if(cvPages!=null && cvPages.size()>0){
                    for(int pageIndex=0; pageIndex<cvPages.size(); pageIndex++){
                        sponsorTemplateBean = (SponsorTemplateBean)cvPages.get(pageIndex);
                        int pagePackageNumber = sponsorTemplateBean.getPackageNumber();
                        String pageDesc = sponsorTemplateBean.getPageDescription();
                        int  pageNumber = sponsorTemplateBean.getPageNumber();
                        int value  = checkSponsorPageExists(getSponsorCode(), pagePackageNumber, pageNumber);
                           
                        if(pageDesc== null || pageDesc.trim().equals(EMPTY_STRING)){
                            CoeusOptionPane.showInfoDialog("Enter the page description for package no: "+packageNumber+ ","+" page no "+pageNumber+". ");
                            tblPackage.setRowSelectionInterval(packageIndex, packageIndex);
                            tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                            
                            tblPages.setRowSelectionInterval(pageIndex, pageIndex);
                            tblPages.setColumnSelectionInterval(PAGE_DESC_COLUMN,PAGE_DESC_COLUMN);
                            tblPages.editCellAt(pageIndex,PAGE_DESC_COLUMN);
                            tblPages.getEditorComponent().requestFocusInWindow();
                            
                            return false;
                        }else if(value==0){
                            CoeusOptionPane.showInfoDialog("Load forms for package "+pagePackageNumber+","+" page "+ pageNumber+". ");
                            tblPackage.setRowSelectionInterval(packageIndex, packageIndex);
                            tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                            
                            
                            tblPages.setRowSelectionInterval(pageIndex, pageIndex);
                            tblPages.setColumnSelectionInterval(PAGE_DESC_COLUMN,PAGE_DESC_COLUMN);
                            tblPages.editCellAt(pageIndex,PAGE_DESC_COLUMN);
                            tblPages.getEditorComponent().requestFocusInWindow();
                            return false;
                        }
                        /*else if(pageDesc==null || pageDesc.trim().equals(EMPTY_STRING)){
                            CoeusOptionPane.showInfoDialog("Package should contain atleast one page");
                            tblPackage.setRowSelectionInterval(packageIndex, packageIndex);
                            tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                            tblPackage.editCellAt(packageIndex,PAGE_DESC_COLUMN);
                            tblPackage.getEditorComponent().requestFocusInWindow();
                            return false;
                        }*/

                    }
                }else{
                    CoeusOptionPane.showInfoDialog("Package should contain atleast one page");
                    tblPackage.setRowSelectionInterval(packageIndex, packageIndex);
                    tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                    tblPackage.editCellAt(packageIndex,PAGE_DESC_COLUMN);
                    tblPackage.getEditorComponent().requestFocusInWindow();
                    return false;
                }
            }
    }
        return true;
    }
    
    
     private void performPackageAddAction(){
          if(cvPackageData!= null && cvPackageData.size() > 0){
            sponsorPackageTableCellEditor.stopCellEditing();
        }
          
          if(tblPages.getRowCount()>0){
              sponsorPageTableCellEditor.stopCellEditing();
          }
        int rowCount =  tblPackage.getRowCount();
        if(rowCount  > 0){
            
            if(cvPackageData!= null && cvPackageData.size() > 0){
                for(int index =0 ; index < cvPackageData.size(); index++){
                    SponsorFormsBean bean = (SponsorFormsBean)cvPackageData.get(index);
                    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy  - Start
                    if(isSponsorHierarchy){
                        int packageNumber = bean.getPackageNumber();
                        Equals equals= new Equals(PACKAGE_NUMBER,new Integer(packageNumber));
                        CoeusVector cvData = cvPageData.filter(equals);
                        if(cvData == null || cvData.size() < 1 ){
                            //Warning message is thrown when user tries to add a new package without load a page for previous package
                            CoeusOptionPane.showInfoDialog("Please load page for the package "+bean.getPackageName());
                            tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                            tblPackage.setRowSelectionInterval(index,index);
                            tblPackage.scrollRectToVisible(tblPackage.getCellRect(index, PACKAGE_DESC, true));
                            return;
                        }
                    }
                    //Case#2445  - End
                    // If the Package description columns are empty then don't add the row.If any one of the
                    // field is entered then allow the user to add a new Row.
                    if(bean.getPackageName().trim().equals(EMPTY_STRING)){
                        tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                        tblPackage.setRowSelectionInterval(index,index);
                        tblPackage.scrollRectToVisible(tblPackage.getCellRect(index, PACKAGE_DESC, true));
//                        tblPackage.editCellAt(index,PACKAGE_DESC);
//                        tblPackage.getEditorComponent().requestFocusInWindow();
                        return;
                    }
                }
            }
        }
        SponsorFormsBean newBean= new SponsorFormsBean();
        //Modified for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy  - Start
        if(isSponsorHierarchy){
            newBean.setSponsorCode("HIERAR");
            newBean.setGroupName(groupName);
        }else{
            newBean.setSponsorCode(getSponsorCode());
        }
        //Case#2445 - End
        packageRowId = packageRowId + 1;
        newBean.setRowId(packageRowId);
        
        newBean.setPackageNumber(getMaxPackageNumber());
        
        newBean.setPackageName(EMPTY_STRING);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        cvPackageData.add(newBean);
        sponsorPackageTableModel.fireTableRowsInserted(sponsorPackageTableModel.getRowCount()+1,
        sponsorPackageTableModel.getRowCount()+1);
        
        int lastRow = tblPackage.getRowCount()-1;
        if(lastRow >= 0){
            tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
            tblPackage.setRowSelectionInterval(lastRow,lastRow);
            tblPackage.scrollRectToVisible(tblPackage.getCellRect(lastRow, PACKAGE_DESC, true));
//            tblPackage.editCellAt(lastRow,PACKAGE_DESC);
//            tblPackage.getEditorComponent().requestFocusInWindow();
        }
        
        if(tblPackage.getRowCount() == 0){
            btnPackageDelete.setVisible(false);
        }else{
            btnPackageDelete.setVisible(true);
        }
    }// End of PerformPackageAddAction
     
     
    // Perform Package delete action
    private void performPackageDeleteAction(){
        int selPackageRow = tblPackage.getSelectedRow();
        int selPageRow = tblPages.getSelectedRow();
        int packageRowCount = tblPackage.getRowCount();
        
        if(packageRowCount > 0){
            sponsorPackageTableCellEditor.stopCellEditing();
        }
        
         if(packageRowCount > 0 && selPackageRow != -1 && selPackageRow >= 0){
            mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_NO);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                int packageNumber = ((Integer)tblPackage.getValueAt(selPackageRow, 0)).intValue();
                CoeusVector cvData  = cvPageData.filter(new Equals(PACKAGE_NUMBER,new Integer(packageNumber)));
                   
                if(cvData!= null && cvData.size()>0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANT_DELETE_PACKAGE));
                    return ;
                }
                SponsorFormsBean deletedBean = (SponsorFormsBean)cvPackageData.get(selPackageRow);
                //Modified for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy  - Start
                //updating the DB for existing data only
//                cvDeletedPackageData.add(deletedBean);
//                cvPackageData.remove(deletedBean);
//                sponsorPackageTableModel.fireTableRowsDeleted(selPackageRow, selPackageRow);
//                deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                CoeusVector cvPages = cvDeletedPageData.filter(new Equals(PACKAGE_NUMBER,new Integer(packageNumber)));
                if(cvPages != null && cvPages.size() > 0){
                    cvDeletedPackageData.add(deletedBean);
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                }
                cvPackageData.remove(deletedBean);
                sponsorPackageTableModel.fireTableRowsDeleted(selPackageRow, selPackageRow);
                //Case#2445 - End

                modified = true;
                
                 int lastRow = selPackageRow -1;
                if(lastRow>0){
                    tblPackage.setRowSelectionInterval(lastRow,lastRow);
                    tblPackage.scrollRectToVisible(tblPackage.getCellRect(lastRow ,1, true));
                    
                }else if(lastRow==0){
                    tblPackage.setRowSelectionInterval(0,0);
                }else{
                    if(tblPackage.getRowCount()>0){
                        tblPackage.setRowSelectionInterval(0,0);
                    }
                }
            }else{
                tblPackage.setRowSelectionInterval(selPackageRow,selPackageRow);
                tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                tblPackage.scrollRectToVisible(tblPackage.getCellRect(selPackageRow ,1, true));
//                tblPackage.editCellAt(selPackageRow,PACKAGE_DESC);
//                tblPackage.getEditorComponent().requestFocusInWindow();
            }
               
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_PKG_TO_DELETE));
                return ;
            }
        }
    
    
    private void performPageAddAction(){
        byte[] data = null;
        selPackageNumber = tblPackage.getSelectedRow();
        int selRow =0;
            if(selPackageNumber==-1){
            //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy  - Start
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ADD_PACKAGE_BEFORE_ADDING_A_PAGE));//Case#2445 - End
            return ;
            }
        
        if(tblPackage.getRowCount()>0){
            selRow = selPackageNumber;
            sponsorPackageTableCellEditor.stopCellEditing();
        }
         
        
        if(tblPages.getRowCount()>0){
            sponsorPageTableCellEditor.stopCellEditing();
            }
        
       
        
        int rowCount =  tblPages.getRowCount();
        if(rowCount  > 0){
            if(cvFilterVector!= null && cvFilterVector.size() > 0){
                for(int index =0 ; index < cvFilterVector.size(); index++){
                    SponsorTemplateBean bean = (SponsorTemplateBean)cvFilterVector.get(index);
                    // If the page description columns are empty then don't add the row.If any one of the
                    // field is entered then allow the user to add a cvFilterVectornew Row.
                    if(bean.getPageDescription().trim().equals(EMPTY_STRING)){
                        tblPages.setColumnSelectionInterval(PAGE_NUMBER_COLUMN,PAGE_NUMBER_COLUMN);
                        tblPages.setRowSelectionInterval(index,index);
                        tblPages.scrollRectToVisible(tblPages.getCellRect(index, PAGE_DESC_COLUMN, true));
                        return;
                    }
                }
            }
        }
        // check if the package description is null (EMPTY). If empty throw the message.
        int packageRowCount = tblPackage.getRowCount();
        for(int packageRow=0; packageRow< packageRowCount; packageRow++){
            String packageDesc = (String)tblPackage.getValueAt(packageRow, PACKAGE_DESC);
            if(packageDesc== null || packageDesc.trim().equals(EMPTY_STRING)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_NAME_FOR_PACKAGAE));
                    tblPackage.setColumnSelectionInterval(PACKAGE_DESC,PACKAGE_DESC);
                    tblPackage.setRowSelectionInterval(packageRow,packageRow);
                    tblPackage.scrollRectToVisible(tblPackage.getCellRect(packageRow, PACKAGE_DESC, true));
//                    tblPackage.editCellAt(packageRow,PACKAGE_DESC);
//                    tblPackage.getEditorComponent().requestFocusInWindow();
                    return;
            }
        }
        
        // Select the couesFileChooser and display the window
            SponsorTemplateBean sponsorTemplateBean = new SponsorTemplateBean();
            SponsorTemplateBean bean=null;
            sponsorPageNumber = getMaxPageNumber()+1;
            
            SponsorFormsBean sponsorFormsBean = (SponsorFormsBean)cvPackageData.get(selPackageNumber);
            int packageNumber = sponsorFormsBean.getPackageNumber();
            
            sponsorTemplateBean.setSponsorCode(getSponsorCode());
                       
            fileChooser = new CoeusFileChooser(mdiForm);
            
            
            //Code commented and modified for Case#3648 - Uploading non-pdf files
            //fileChooser.setSelectedFileExtension(POST_SCRIPT);
            fileChooser.setSelectedFileExtension(FILE_TYPES);
            fileChooser.setAcceptAllFileFilterUsed(true);
            tblPackage.setRowSelectionInterval(selPackageNumber, selPackageNumber);
            fileChooser.showFileChooser();
            tblPackage.setRowSelectionInterval(selPackageNumber, selPackageNumber);
            
            if(fileChooser.isFileSelected()){
                String fileName = fileChooser.getSelectedFile();
                if(fileName != null && !fileName.trim().equals("")){
                    int index = fileName.lastIndexOf('.');
                    if(index != -1 && index != fileName.length()){
                        String extension = fileName.substring(index+1,fileName.length());
                        if(extension != null && 
                            (extension.equalsIgnoreCase("xml")  || extension.equalsIgnoreCase("xsl"))){
                        sponsorTemplateBean.setPageNumber(sponsorPageNumber);
                        sponsorTemplateBean.setPackageNumber(packageNumber);
                        data = fileChooser.getFile();
                        if(data.length==0){
                            return ;
                        }
                        sponsorTemplateBean.setFormTemplate(data);
                        File file = fileChooser.getFileName();
                        sponsorTemplateBean.setPageDescription(file.getName());
                        sponsorTemplateBean.setAcType(TypeConstants.INSERT_RECORD);
                                                
                        Equals equals= new Equals(PACKAGE_NUMBER,new Integer(packageNumber));
                        CoeusVector cvData = cvPackageData.filter(equals);
                                                
                        SponsorFormsBean addSponsorFormsBean = (SponsorFormsBean)cvData.get(0);
                        
                        if(addSponsorFormsBean.getAcType()!=null && 
                            addSponsorFormsBean.getAcType().equals(TypeConstants.INSERT_RECORD) 
                                && cvFilterVector.size()==0){
                            CoeusVector packageData = new CoeusVector();
                            CoeusVector pageData = new CoeusVector();
                            Hashtable sendHashData = new Hashtable();
                            
                            packageData.add(addSponsorFormsBean);
                            //check if this page has been deleted earlier
                            Equals eqPage = new Equals("pageNumber", new Integer(sponsorTemplateBean.getPageNumber()));
                            Equals eqPackage = new Equals(PACKAGE_NUMBER, new Integer(sponsorTemplateBean.getPackageNumber()));
                            And eqPageAndEqPackage = new And(eqPage, eqPackage);
                            
                            CoeusVector cvDelPage = cvDeletedPageData.filter(eqPageAndEqPackage);
                            if(cvDelPage != null && cvDelPage.size() > 0) {
                                //this bean has been deleted. so update the existing 
                                //bean and don't delete it.
                                SponsorTemplateBean delSponsorTemplateBean = (SponsorTemplateBean)cvDelPage.get(0);
                                
                                cvDelPage.remove(delSponsorTemplateBean);
                                
                                delSponsorTemplateBean.setAcType(TypeConstants.UPDATE_RECORD);
                                delSponsorTemplateBean.setFormTemplate(sponsorTemplateBean.getFormTemplate());
                                delSponsorTemplateBean.setPageDescription(sponsorTemplateBean.getPageDescription());
                                
                                pageData.add(delSponsorTemplateBean);
                            }else {
                                pageData.add(sponsorTemplateBean);
                            }
                            //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
                            if(isSponsorHierarchy){
                                updatePackageNumber(packageData,pageData);
                                updatePackagePagesNumber(packageData,pageData);
                                sendHashData.put(SPONSOR_HIERARCHY,new Boolean(true));
                                sendHashData.put(SPONSOR_CODE,getSponsorCode());
                                sendHashData.put(GROUP_NAME,groupName);
                            }else{
                                sendHashData.put(SPONSOR_HIERARCHY,new Boolean(false));
                            }
                            sendHashData.put(KeyConstants.PACKAGE_DATA,packageData);
                            sendHashData.put(KeyConstants.PAGE_DATA,pageData);
                            Hashtable returnData = null;
                            try{
                                returnData = updateFormData(sendHashData);
                            }catch (CoeusException exception){
                                exception.printStackTrace();
                                CoeusOptionPane.showErrorDialog(exception.getMessage());
                            }
                            
                            if(returnData==null) return ;
                            CoeusVector cvReturnPageData = (CoeusVector)returnData.get(KeyConstants.PAGE_DATA);
                            CoeusVector cvReturnPackageData= (CoeusVector)returnData.get(KeyConstants.PACKAGE_DATA);
                            
                            cvPackageData = cvReturnPackageData;
                            cvPageData = cvReturnPageData;
                           // cvPackageData = cvReturnPackageData;
                            
                            filterData(selPackageNumber);
                            
                        }else{
                            
                            Equals eqPage = new Equals("pageNumber", new Integer(sponsorTemplateBean.getPageNumber()));
                            Equals eqPackage = new Equals(PACKAGE_NUMBER, new Integer(sponsorTemplateBean.getPackageNumber()));
                            And eqPageAndEqPackage = new And(eqPage, eqPackage);
                            CoeusVector cvDelPage = cvDeletedPageData.filter(eqPageAndEqPackage);
                            if(cvDelPage != null && cvDelPage.size() > 0) {
                                //this bean has been deleted. so update the existing 
                                //bean and don't delete it.
                                SponsorTemplateBean delSponsorTemplateBean = (SponsorTemplateBean)cvDelPage.get(0);
                                
                                cvDelPage.remove(delSponsorTemplateBean);
                                
                                delSponsorTemplateBean.setAcType(TypeConstants.UPDATE_RECORD);
                                delSponsorTemplateBean.setFormTemplate(sponsorTemplateBean.getFormTemplate());
                                delSponsorTemplateBean.setPageDescription(sponsorTemplateBean.getPageDescription());
                                //pageData.add(delSponsorTemplateBean);
                                //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
                                sponsorTemplateBean = updatePackagePageNumber(sponsorTemplateBean);
                                //Case#2445 - End
                                bean = updateSponsorTemplate(delSponsorTemplateBean);
                                cvPageData.add(bean);
                                cvFilterVector.add(bean);
                            }else{
                                //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
                                sponsorTemplateBean = updatePackagePageNumber(sponsorTemplateBean);
                                //Case#2445 - End
                                bean = updateSponsorTemplate(sponsorTemplateBean);
                                
                                cvPageData.add(bean);
                                cvFilterVector.add(bean);
                                
                           }
                        }
                        sponsorPageTableModel.fireTableRowsInserted(sponsorPageTableModel.getRowCount()+1,
                        sponsorPageTableModel.getRowCount()+1);
                   }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                        "correspType_exceptionCode.1011"));
                        data = null; 
                        return ;
                    }
                    
                    }
                }
            }else{
                sponsorTemplateBean.setPageNumber(sponsorPageNumber);
                sponsorTemplateBean.setPackageNumber(packageNumber);
                sponsorTemplateBean.setFormTemplate(null);
                sponsorTemplateBean.setPageDescription(EMPTY_STRING);
                sponsorTemplateBean.setAcType(TypeConstants.INSERT_RECORD);
                modified =  true;
                
                int pageIndex = cvPageData.indexOf(sponsorTemplateBean);
                int filterIndex = cvFilterVector.indexOf(sponsorTemplateBean);
                if(pageIndex!= -1){
                    cvPageData.set(pageIndex, sponsorTemplateBean);
                }
                //Commented for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
//                else{
//                    cvPageData.add(sponsorTemplateBean);
//                }
                //Case#2445 - End
                if(filterIndex!= -1){
                    cvFilterVector.set(pageIndex, sponsorTemplateBean);
                }
                //Commented for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
//                else{
//                    cvFilterVector.add(sponsorTemplateBean);
//                }

//                sponsorPageTableModel.fireTableRowsInserted(
//                        sponsorPageTableModel.getRowCount()+1,sponsorPageTableModel.getRowCount()+1);
                //Case#2445 - End
            }
            modified = false;
            int lastRow = tblPages.getRowCount()-1;
            if(lastRow >= 0){
                tblPages.setColumnSelectionInterval(PAGE_DESC_COLUMN,PAGE_DESC_COLUMN);
                tblPages.setRowSelectionInterval(lastRow,lastRow);
                tblPages.scrollRectToVisible(tblPages.getCellRect(lastRow, PAGE_DESC_COLUMN, true));
                tblPages.editCellAt(lastRow,PAGE_DESC_COLUMN);
                tblPages.getEditorComponent().requestFocusInWindow();
            }
            
            tblPackage.setRowSelectionInterval(selPackageNumber, selPackageNumber);
            
    }
    
    private void performDeletePageAction(){
        int selPackageRow = tblPackage.getSelectedRow();
        int selPageRow = tblPages.getSelectedRow();
        int packageRowCount = tblPackage.getRowCount();
        int pageRowCount = tblPages.getRowCount();
        
        if(pageRowCount > 0){
            sponsorPageTableCellEditor.stopCellEditing();
        }
        
         if(packageRowCount > 0){
            sponsorPackageTableCellEditor.stopCellEditing();
        }
        
         if(pageRowCount > 0 && selPageRow != -1 && selPageRow >= 0){
            mesg = DELETE_PAGE;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_NO);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                SponsorTemplateBean deletedBean = (SponsorTemplateBean)cvFilterVector.get(selPageRow);
                cvDeletedPageData.add(deletedBean);
                
                cvFilterVector.remove(deletedBean);
                cvPageData.remove(deletedBean);
                
                sponsorPageTableModel.fireTableRowsDeleted(selPageRow, selPageRow);
                deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                
                modified = true;
                //isTableDataChanged = true;
                 int lastRow = selPageRow -1;
                if(lastRow>0){
                    tblPages.setRowSelectionInterval(lastRow,lastRow);
                    tblPages.scrollRectToVisible(tblPages.getCellRect(lastRow ,1, true));
                }else if(lastRow==0){
                    tblPages.setRowSelectionInterval(0,0);
                }else{
                    if(tblPages.getRowCount()>0){
                        tblPages.setRowSelectionInterval(0,0);
                    }
                }
            }else{
                tblPages.setRowSelectionInterval(selPageRow,selPageRow);
                tblPages.setColumnSelectionInterval(PAGE_DESC_COLUMN,PAGE_DESC_COLUMN);
                tblPages.scrollRectToVisible(tblPackage.getCellRect(selPageRow ,PAGE_DESC_COLUMN, true));
                tblPages.editCellAt(selPageRow,PAGE_DESC_COLUMN);
                tblPages.getEditorComponent().requestFocusInWindow();
            }
               
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_PAGE_TO_DELETE));
                return ;
            }
    }
    
    
    private void performLoadAction(){
        
        int selPageRow = tblPages.getSelectedRow();
        int selPackRow = tblPackage.getSelectedRow();
        int packageNumber;
        int pageNumber;
        int value;
        int pageRowCount = tblPages.getRowCount();
        
        if(pageRowCount > 0){
            sponsorPageTableCellEditor.stopCellEditing();
        }
        
        if(tblPackage.getRowCount()> 0){
            sponsorPackageTableCellEditor.stopCellEditing();
        }
        
         if(pageRowCount > 0 && selPageRow != -1 && selPageRow >= 0){
            SponsorTemplateBean bean = (SponsorTemplateBean)cvFilterVector.get(selPageRow);
            packageNumber = bean.getPackageNumber();
            pageNumber = bean.getPageNumber();

            value = checkSponsorPageExists(getSponsorCode(),packageNumber,pageNumber);

            if(value > 0){
                 mesg = PAGE_TEMPLATE_EXISTS;
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES){
                   fileChooser = new CoeusFileChooser(mdiForm);
                   //Code commented and modified for Case#3648 - Uploading non-pdf files
                    //fileChooser.setSelectedFileExtension(POST_SCRIPT);
                    fileChooser.setSelectedFileExtension(FILE_TYPES);
                    fileChooser.setAcceptAllFileFilterUsed(true);
                    fileChooser.showFileChooser();
                    if(fileChooser.isFileSelected()){
                        String fileName = fileChooser.getSelectedFile();
                        if(fileName != null && !fileName.trim().equals("")){
                            int index = fileName.lastIndexOf('.');
                            if(index != -1 && index != fileName.length()){
                                bean.setPageNumber(pageNumber);
                                bean.setPackageNumber(packageNumber);
//                                bean.setFormTemplate(new String(fileChooser.getFile()));
                                byte data[] = fileChooser.getFile();
                                if(data.length==0){
                                    return ;
                                }
                                bean.setFormTemplate(data);
                                File file = fileChooser.getFileName();
                                bean.setPageDescription(file.getName());
                                bean.setAcType(TypeConstants.UPDATE_RECORD);
                                SponsorTemplateBean changedBean = updateSponsorTemplate(bean);
//                                cvFilterVector.add(changedBean);
                                cvPageData.add(changedBean);

                                sponsorPageTableModel.fireTableRowsUpdated(sponsorPageTableModel.getRowCount(),
                                sponsorPageTableModel.getRowCount());
                            }
                        }
                    }else{
                        return ;
                    }
                }else{
                    return ;
                }
            }else{
                // If there is no template add the template
                fileChooser = new CoeusFileChooser(mdiForm);
                    //Code commented and modified for Case#3648 - Uploading non-pdf files
                    //fileChooser.setSelectedFileExtension(POST_SCRIPT);
                    fileChooser.setSelectedFileExtension(FILE_TYPES);
                    fileChooser.setAcceptAllFileFilterUsed(true);
                    fileChooser.showFileChooser();
                    if(fileChooser.isFileSelected()){
                        String fileName = fileChooser.getSelectedFile();
                        if(fileName != null && !fileName.trim().equals("")){
                            int index = fileName.lastIndexOf('.');
                            if(index != -1 && index != fileName.length()){
                                bean.setPageNumber(pageNumber);
                                bean.setPackageNumber(packageNumber);
                                byte data[] = fileChooser.getFile();
                                if(data.length==0){
                                    return ;
                                }
                                bean.setFormTemplate(data);
                                File f = fileChooser.getFileName();
                                bean.setPageDescription(f.getName());
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                SponsorTemplateBean changedBean = updateSponsorTemplate(bean);
                                
                                int pageIndex = cvPageData.indexOf(changedBean);
                                if(pageIndex!=-1){
                                    cvPageData.set(pageIndex, changedBean);
                                }else{
                                    cvPageData.add(changedBean);
                                }
                                
                                int pageFilterIndex = cvFilterVector.indexOf(changedBean);
                                if(pageFilterIndex!=-1){
                                    cvFilterVector.set(pageFilterIndex, changedBean);
                                }else{
                                    cvFilterVector.add(changedBean);
                                }
                                sponsorPageTableModel.fireTableRowsUpdated(sponsorPageTableModel.getRowCount(),
                                sponsorPageTableModel.getRowCount());
                            }
                        }
                    }
                }
         }
        //Addded for 2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
         else{
              CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(FORM_CANNOT_BE_LOAD_WITHOUT_ADDING_A_PAGE));
         }
        //Case#2445 - End
    }
    
     private void performDownLoadAction(){
        int selPageRow = tblPages.getSelectedRow();
        int packageNumber;
        int pageNumber;
        int value;
        int pageRowCount = tblPages.getRowCount();
        
        if(pageRowCount > 0){
            sponsorPageTableCellEditor.stopCellEditing();
        }
         if(pageRowCount > 0 && selPageRow != -1 && selPageRow >= 0){
            SponsorTemplateBean bean = (SponsorTemplateBean)cvFilterVector.get(selPageRow);
            SponsorTemplateBean newClobBean  = downLoadClobData(bean);
            if(newClobBean != null) {
                fileChooser = new CoeusFileChooser(mdiForm);
                String fileName = newClobBean.getPageDescription();
                // Select the file which has to be renamed 
                File selFile = new File(fileName);
                fileChooser.setSelectedFileName(selFile);
                int selection = fileChooser.showSaveDialog(mdiForm);
                if(selection == JFileChooser.APPROVE_OPTION) {
                    //clicked on Save
                    try{
                        File file = fileChooser.getFileName();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte dataBytes[] = newClobBean.getFormTemplate();
                        fileOutputStream.write(dataBytes);
                        fileOutputStream.close();
                    }catch (FileNotFoundException fileNotFoundException) {
                        CoeusOptionPane.showErrorDialog(fileNotFoundException.getMessage());
                    }catch (IOException iOException) {
                        CoeusOptionPane.showErrorDialog(iOException.getMessage());
                    }
                }
            }//End if check for null
         }
        //Addded for 2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
         else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PAGE_TO_DOWNLOAD));
         }
        //Case#2445 - End
     }
     
     private SponsorTemplateBean downLoadClobData(SponsorTemplateBean sponsorTemplateBean){
         RequesterBean requester;
         ResponderBean responder;
         SponsorTemplateBean bean;
         
         requester = new RequesterBean();
         requester.setFunctionType(GET_CLOB_DATA);
         requester.setDataObject(sponsorTemplateBean);
         
         AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
         comm.send();
         responder = comm.getResponse();
         if(responder.isSuccessfulResponse()){
             System.out.println("Got the Clob Data");
             bean = (SponsorTemplateBean)responder.getDataObject();
             
         }else{
             CoeusOptionPane.showInfoDialog("Error while getting clob data");
             return null;
         }
       //  modified= false;
         return bean;
     }
     
     
     
     /** If there is no description then it is new page else it is a modify mode, in which case
      *only update clob has to be taken care of.
      */
     private int checkSponsorPageExists(String sponsorCode, int packageNumber,int pageNumber){
         
         CoeusVector data = new CoeusVector();
         data.addElement(sponsorCode);
         data.addElement(new Integer(packageNumber));
         data.addElement(new Integer(pageNumber));
         
         int value=0;
         
         RequesterBean requester;
         ResponderBean responder;
         
         requester = new RequesterBean();
         requester.setFunctionType(GET_SPONSOR_TEMPLATE);
         requester.setDataObject(data);
         
         AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
         comm.send();
         responder = comm.getResponse();
         if(responder.isSuccessfulResponse()){
             value = ((Integer)responder.getDataObject()).intValue();
         }else{
             CoeusOptionPane.showInfoDialog("Error while Updating Sponsor Template");
             return 0;
         }
         return value;
         
         
     }
     
     private int getMaxPageNumber(){
         int pageNumber;
         if(cvFilterVector == null || cvFilterVector.size() == 0) {
             pageNumber = 0;//Starts with
             
         }
         else {
             SponsorTemplateBean bean;
             cvFilterVector.sort("pageNumber",false);
             bean = (SponsorTemplateBean)cvFilterVector.get(0);
             pageNumber = bean.getPageNumber();
         }
         return pageNumber;
     }
     
     
     
     
     private SponsorTemplateBean updateSponsorTemplate(SponsorTemplateBean sponsorTemplateBean){
         
         RequesterBean requester;
         ResponderBean responder;
         SponsorTemplateBean bean;
         
         requester = new RequesterBean();
         requester.setFunctionType(UPDATE_SPONSOR_TEMPLATE);
         requester.setDataObject(sponsorTemplateBean);
         
         AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
         comm.send();
         responder = comm.getResponse();
         if(responder.isSuccessfulResponse()){
             bean = (SponsorTemplateBean)responder.getDataObject();
         }else{
             CoeusOptionPane.showInfoDialog("Error while Updating Sponsor Template");
             return null;
         }
         return bean;
         
     }
     
     
     
     
     /** To get the max rowId of the existing beans
      * @return cvExistingRecords.size() the count of existing package beans
      */
     private int getPackageExistingMaxId() {
         CoeusVector cvExistingRecords = new CoeusVector();
         int maxRowId = 0;
         try{
             cvExistingRecords = queryEngine.getDetails(queryKey, SponsorFormsBean.class);
             cvExistingRecords.sort("rowId",false);
             if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                 SponsorFormsBean bean = (SponsorFormsBean)cvExistingRecords.get(0);
                 maxRowId = bean.getRowId();
             }else{
                 maxRowId = 0;
             }
         }catch (CoeusException coeusException){
             coeusException.getMessage();
         }
         return maxRowId;
     }
     
     /** To get the max rowId of the existing beans
      * @return cvExistingRecords.size() the count of existing package beans
      */
     private int getPageExistingMaxId() {
         CoeusVector cvExistingRecords = new CoeusVector();
         int maxRowId = 0;
         try{
             cvExistingRecords = queryEngine.getDetails(queryKey, SponsorTemplateBean.class);
             cvExistingRecords.sort("rowId",false);
             if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                 SponsorTemplateBean bean = (SponsorTemplateBean)cvExistingRecords.get(0);
                 maxRowId = bean.getRowId();
             }else{
                 maxRowId = 0;
             }
         }catch (CoeusException coeusException){
             coeusException.getMessage();
         }
         return maxRowId;
     }
     
     private void postInitComponents(){
         
         dlgSponsorMaintainance = new CoeusDlgWindow(mdiForm);
         dlgSponsorMaintainance.getContentPane().add(this);
         dlgSponsorMaintainance.setTitle("Sponsor Form Maintenance");
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
                 cancelSposnorAction();
                 return;
             }
         });
         dlgSponsorMaintainance.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
         dlgSponsorMaintainance.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 cancelSposnorAction();
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
            btnPackageAdd.requestFocusInWindow();
        // }
     }
     
     public class SponsorPackageTableModel extends AbstractTableModel{
         
         private String colName[] = {EMPTY_STRING,EMPTY_STRING};
         private Class colClass[] ={Integer.class, String.class};
         
         public boolean isCellEditable(int row, int col){
             return true;
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
     
     
     public class SponsorPageTableModel extends AbstractTableModel{
         
         private String colName[] = {"No","Description", "Last Update","Update User"};
         private Class colClass[] ={Integer.class,String.class,String.class,String.class};
         CoeusVector data= new CoeusVector();
         
         public boolean isCellEditable(int row, int col){
             if(col==0 ||col==2||col==3){
                 return false;
             }else{
                 return true;
             }
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
             this.data.sort("pageNumber", true);
             if(bean!= null){
                 switch(col){
                     case PAGE_NUMBER_COLUMN:
                         return new Integer(bean.getPageNumber());
                     case PAGE_DESC_COLUMN:
                         return bean.getPageDescription();
                     case PAGE_UPDATE_COLUMN:
                         return bean.getLastUpdateTime();
                     case PAGE_USER_COLUMN:
//                         return bean.getUpdateUser();
                        /*
                         * UserID to UserName Enhancement - Start
                         * Added new property getUpdateUserName to get the username
                         */
                         return bean.getUpdateUserName();
                         // UserId to UserName Enhancement - End
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
     public class SponsorPackageTableCellEditor extends AbstractCellEditor implements TableCellEditor{
         
         private CoeusTextField txtPackage;
         private int column;
         
         public SponsorPackageTableCellEditor(){
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
     public class SponsorPageTableCellEditor extends AbstractCellEditor implements TableCellEditor{
         
         private CoeusTextField txtPage;
         private int column;
         
         public SponsorPageTableCellEditor(){
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
                 case PAGE_UPDATE_COLUMN:
                     if(value!= null || !(value.toString().equals(EMPTY_STRING))) {
                         txtPage.setText(value.toString());
                         return txtPage;
                     }
                 case PAGE_USER_COLUMN:
                     if(value!= null || !(value.toString().equals(EMPTY_STRING))) {
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
                 case PAGE_UPDATE_COLUMN:
                     return txtPage.getText();
                 case PAGE_USER_COLUMN:
                     return txtPage.getText();
             }
             return txtPage;
         }
     }
     
     
     public class SponsorPackageRenderer extends DefaultTableCellRenderer{
         CoeusTextField txtComponent;
         
         public SponsorPackageRenderer(){
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
     
     public class SponsorPageRenderer extends DefaultTableCellRenderer{
         CoeusTextField txtComponent;
         
         public SponsorPageRenderer(){
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
                 case PAGE_UPDATE_COLUMN:
                     if (isSelected) {
                         txtComponent.setBackground(java.awt.Color.yellow);
                     }else {
                         txtComponent.setBackground(java.awt.Color.white);
                     }
                     
                     if(value!= null && !(value.toString().trim().equals(EMPTY_STRING))){
                         value = simpleDateFormat.format(value);
                         txtComponent.setText(value.toString());
                     }else{
                         txtComponent.setText(null);
                     }
                     return txtComponent;
                     
                 case PAGE_USER_COLUMN:
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
             sponsorPackageTableCellEditor.stopCellEditing();
         }
         if(tblPages.getRowCount()>0){
             sponsorPageTableCellEditor.stopCellEditing();
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
         sponsorPackageTableCellEditor.stopCellEditing();
         sponsorFormsBean= (SponsorFormsBean)cvPackageData.get(selRow);
         int packageNumber = sponsorFormsBean.getPackageNumber();
         Equals eqFilter = new Equals(PACKAGE_NUMBER,new Integer(packageNumber));
         
         
         cvPageData.sort("pageNumber", false);
         cvFilterVector = cvPageData.filter(eqFilter);
         cvFilterVector.sort("pageNumber",true);
         
         sponsorPackageTableCellEditor.stopCellEditing();
         sponsorPageTableModel.setData(cvFilterVector);
         sponsorPageTableModel.fireTableDataChanged();
     }
     
     private int getMaxPackageNumber() {
         int packageNumber = 1;
         if(cvPackageData!= null && cvPackageData.size() > 0 ) {
             cvPackageData.sort(PACKAGE_NUMBER,false);
             SponsorFormsBean bean=null;
             bean = (SponsorFormsBean)cvPackageData.get(0);
             packageNumber= bean.getPackageNumber();
             packageNumber = packageNumber+1;
             canPackageExists = true;
             return packageNumber;
         }
         else {
             return packageNumber;
         }
     }
     
     //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
     /*
      * Method to get max package number for 'HIERAR' sponsor code
      * @return maxPackageNumber
      */
     private int getHierarchyMaxPackageNumber(){
         RequesterBean requester = new RequesterBean();
         ResponderBean responder;
         int maxPackageNumber = 0;

         requester.setFunctionType(GET_HIERAR_GROUP_MAX_PACKAGE_NUMBER);
         requester.setDataObject(getSponsorCode());
         
         AppletServletCommunicator comm
                 = new AppletServletCommunicator(connect, requester);
         
         comm.send();
         responder = comm.getResponse();
         if(responder.isSuccessfulResponse()){
             maxPackageNumber = ((Integer)responder.getDataObject()).intValue();
         }
         return maxPackageNumber;
     }
     
     /*
      * Method to get max page number based on package number
      * @return packageMaxPageNumber
      */
     private int getPackageMaxPageNumber(int packageNumber){
         
         RequesterBean requester = new RequesterBean();
         ResponderBean responder;
         int packageMaxPageNumber = 0;
         SponsorFormsBean sponsorFormsBean = new SponsorFormsBean();
         sponsorFormsBean.setSponsorCode(getSponsorCode());
         sponsorFormsBean.setPackageNumber(packageNumber);
         requester.setFunctionType(GET_PACKAGE_MAX_PAGE_NUMBER);
         requester.setDataObject(sponsorFormsBean);
         
         AppletServletCommunicator comm
                 = new AppletServletCommunicator(connect, requester);
         
         comm.send();
         responder = comm.getResponse();
         if(responder.isSuccessfulResponse()){
             packageMaxPageNumber = ((Integer)responder.getDataObject()).intValue();
         }
         return packageMaxPageNumber;
     }
     //Case#2445 - End
     
     public void display(){
         if(tblPackage.getRowCount()>0){
             tblPackage.setRowSelectionInterval(0,0);
         }else{
             btnPackageAdd.requestFocusInWindow();
         }
         dlgSponsorMaintainance.setVisible(true);
     }
     
     
     
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
     
     
     
     /** This method is called from within the constructor to
      * initialize the form.
      * WARNING: Do NOT modify this code. The content of this method is
      * always regenerated by the Form Editor.
      */
    private void initComponents() {                          
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnPackage = new javax.swing.JScrollPane();
        tblPackage = new javax.swing.JTable();
        scrPnPages = new javax.swing.JScrollPane();
        tblPages = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnPackageAdd = new javax.swing.JButton();
        btnPackageDelete = new javax.swing.JButton();
        btnPageAdd = new javax.swing.JButton();
        btnPageDelete = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        btnDownLoad = new javax.swing.JButton();
        lblSponsorCode = new javax.swing.JLabel();
        lblPackage = new javax.swing.JLabel();
        lblPages = new javax.swing.JLabel();
        lblSponsorName = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        scrPnPackage.setBorder(new javax.swing.border.EtchedBorder());
        scrPnPackage.setMinimumSize(new java.awt.Dimension(470, 100));
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
        scrPnPages.setMinimumSize(new java.awt.Dimension(470, 200));
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

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMinimumSize(new java.awt.Dimension(90, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(90, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnCancel, gridBagConstraints);

        btnPackageAdd.setFont(CoeusFontFactory.getLabelFont());
        btnPackageAdd.setMnemonic('A');
        btnPackageAdd.setText("Add");
        btnPackageAdd.setMaximumSize(new java.awt.Dimension(90, 26));
        btnPackageAdd.setMinimumSize(new java.awt.Dimension(90, 26));
        btnPackageAdd.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnPackageAdd, gridBagConstraints);

        btnPackageDelete.setFont(CoeusFontFactory.getLabelFont());
        btnPackageDelete.setMnemonic('t');
        btnPackageDelete.setText("Delete");
        btnPackageDelete.setMaximumSize(new java.awt.Dimension(74, 26));
        btnPackageDelete.setMinimumSize(new java.awt.Dimension(90, 26));
        btnPackageDelete.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnPackageDelete, gridBagConstraints);

        btnPageAdd.setFont(CoeusFontFactory.getLabelFont());
        btnPageAdd.setMnemonic('d');
        btnPageAdd.setText("Add");
        btnPageAdd.setMinimumSize(new java.awt.Dimension(90, 26));
        btnPageAdd.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnPageAdd, gridBagConstraints);

        btnPageDelete.setFont(CoeusFontFactory.getLabelFont());
        btnPageDelete.setMnemonic('e');
        btnPageDelete.setText("Delete");
        btnPageDelete.setMaximumSize(new java.awt.Dimension(90, 26));
        btnPageDelete.setMinimumSize(new java.awt.Dimension(90, 26));
        btnPageDelete.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnPageDelete, gridBagConstraints);

        btnLoad.setFont(CoeusFontFactory.getLabelFont());
        btnLoad.setMnemonic('L');
        btnLoad.setText("Load");
        btnLoad.setMaximumSize(new java.awt.Dimension(90, 26));
        btnLoad.setMinimumSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnLoad, gridBagConstraints);

        btnDownLoad.setFont(CoeusFontFactory.getLabelFont());
        btnDownLoad.setMnemonic('w');
        btnDownLoad.setText("Download");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnDownLoad, gridBagConstraints);

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

        lblSponsorName.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorName.setText("Sponsor Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(lblSponsorName, gridBagConstraints);

    }                        

   
    // Variables declaration - do not modify                     
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDownLoad;
    public javax.swing.JButton btnLoad;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnPackageAdd;
    public javax.swing.JButton btnPackageDelete;
    public javax.swing.JButton btnPageAdd;
    public javax.swing.JButton btnPageDelete;
    public javax.swing.JLabel lblPackage;
    public javax.swing.JLabel lblPages;
    public javax.swing.JLabel lblSponsorCode;
    public javax.swing.JLabel lblSponsorName;
    public javax.swing.JScrollPane scrPnPackage;
    public javax.swing.JScrollPane scrPnPages;
    public javax.swing.JTable tblPackage;
    public javax.swing.JTable tblPages;
    // End of variables declaration                   
    
}
