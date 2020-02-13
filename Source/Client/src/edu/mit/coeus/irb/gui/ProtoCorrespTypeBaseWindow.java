/*
 * @(#)ProtoCorrespTypeBaseWindow.java 1.0 February 27, 2003, 12:24 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.gui.CoeusDlgWindow;

import java.awt.Component;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

import edu.mit.coeus.irb.bean.CorrespondenceTypeFormBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.applet.AppletContext;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 * The List window used to list all the correspondence types for the protocol.
 * This is screen which will accessible from the <code>IRB</code> menuitem of
 * <code>Maintain</code> menu. It provides the option to create, modify, delete
 * option for <code>Correspondence</code>.
 * @author  Geo Thomas
 * @version 1.0 February 27, 2003, 12:24 PM
 */
public class ProtoCorrespTypeBaseWindow extends edu.mit.coeus.gui.CoeusInternalFrame
implements ActionListener{
    
    /** Connects to the required servelet that is Correspondence Servlet
     */
    final String connectTo = CoeusGuiConstants.CONNECTION_URL + "/correspondenceServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    /** menu item to add new correspondence type
     */
    private CoeusMenuItem addProtoCorrespType;
    
    /** menuitem to update and upload template
     */
    private CoeusMenuItem editProtoCorrespType;
    
    /** menu item to delete correspondence type
     */
    private CoeusMenuItem deleteProtoCorrespType;
    
    private CoeusFileChooser fileChooser = null; /* File Chooser to get Template File */
    
    /** byte array to hold the file data
     */
    private byte[] templateData = null; /* TO hold to File Data*/
    
    
    
    /** menu item to open selected template
     */
    private CoeusMenuItem   openTemplate;
    
    
    // Toolbar for CorrespondenceTypes
    /** Toolbar button to add new correspondence type
     */
    private CoeusToolBarButton btnAddProtoCorrespType;
    /** tool bar button to delete existing correspondence type
     */
    private CoeusToolBarButton btnDeleteProtoCorrespType;
    /** Tool bar button to update existing correspondence type
     */
    private CoeusToolBarButton btnEditProtoCorrespType;
    /** Tool bar button to read selected template
     */
    private CoeusToolBarButton btnTemplate;
    
    //modified by Manoj
    
    private CoeusToolBarButton btnSearchProtoCorrespType;
    /** Tool bar button to close the correspondence detail window
     */
    private CoeusToolBarButton btnCloseProtoCorrespType;
    /**
     */
    //private CoeusToolBarButton btnSaveProtoCorrespType;
    
    //Main MDI Form.
    /** CoeusAppletMDIform to hold the base window
     */
    private CoeusAppletMDIForm mdiForm = null;
    
    // added by manoj to hold correspondence data
    /** This contains all correspondence type form bean instances existing
     */
    private Vector correspData;
    /** this is to display all correspondecen types and committees
     */
    private JTree treeCorresp;
    /** this contains the line style for tree
     */
    private String lineStyle = "Angled";
    
    //holds CoeusMessageResources instance used for reading message Properties.
    /** Coeuse message resources instance to parse the error message
     */
    private CoeusMessageResources coeusMessageResources;
    /** Mode to indicate Insert operation in the servelet
     */
    private final char INSERT_MODE = 'U';
    /** Mode to indicate delete operation at servelet
     */
    private final char DELETE_MODE = 'D';
    
    /** Creates a new instance of ProtoCorrespTypeBaseWindow */
    public ProtoCorrespTypeBaseWindow(CoeusAppletMDIForm mdiForm) throws Exception{
        super(CoeusGuiConstants.PROTO_CORRESP_TYPE_BASE_FRAME_TITLE, mdiForm,OTHER_MODE);
        this.mdiForm = mdiForm;
        initComponents();
        mdiForm.putFrame(CoeusGuiConstants.PROTO_CORRESP_TYPE_BASE_FRAME_TITLE,this);
        mdiForm.getDeskTopPane().add(this);
    }
    /** Constructor to create <CODE>ProtoCorrespTypeBaseWindow</CODE> with the given frame name and
     * parent component. This will be called from <CODE>CoeusMaintainMenu</CODE> when
     * the CorrespondenceTypes menu item is selected in Maintain menu of the application.
     *
     * @param frameName String used to identify this InternalFrame.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ProtoCorrespTypeBaseWindow(String frameName, CoeusAppletMDIForm mdiForm) throws Exception{
        super(frameName, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
        mdiForm.putFrame(CoeusGuiConstants.PROTO_CORRESP_TYPE_BASE_FRAME_TITLE,this);
        mdiForm.getDeskTopPane().add(this);
    }
    
    /**
     * Initialize the components for the base window
     */
    private void initComponents() throws Exception{
        coeusMessageResources = CoeusMessageResources.getInstance();
        setFrameMenu(buildEditMenu());
        setToolsMenu(null);
        final JToolBar correspToolBar = buildToolBar();
        this.setFrameToolBar(correspToolBar);
        this.setFrame(CoeusGuiConstants.PROTO_CORRESP_TYPE_BASE_FRAME_TITLE);
        this.setFrameIcon(mdiForm.getCoeusIcon());
        JScrollPane scrlPnCorresp = new JScrollPane();
        scrlPnCorresp.setMinimumSize(new Dimension(22, 15));
        scrlPnCorresp.setPreferredSize(new Dimension(600, 400));
        buildTree();
        scrlPnCorresp.setViewportView(treeCorresp);
        //scrlPnCorresp.getViewport().setBackground(Color.white);
        //scrlPnCorresp.setForeground(java.awt.Color.white);
        getContentPane().add(scrlPnCorresp);
    }
    
    
    // Added by Manoj to the new Requirements 27/08/2003
    /** this method used to build the tree with all correspondence information
     * @throws Exception
     */
    private void buildTree()throws Exception{
        DefaultMutableTreeNode root;
        treeCorresp = new JTree();
        treeCorresp.setShowsRootHandles( false );
        treeCorresp.putClientProperty("JTree.lineStyle", lineStyle);
        treeCorresp.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        root = new DefaultMutableTreeNode("Correspondence Templates",true);
        treeCorresp.setModel( new DefaultTreeModel(root) );
        TreeRenderer renderer = new TreeRenderer();
        treeCorresp.setCellRenderer(renderer);
        treeCorresp.setBackground(Color.lightGray);
        correspData = getCorrespList();
        if(correspData != null && correspData.size()>0){
            int beansLength = correspData.size();
            for(int beanIndex=0;beanIndex<beansLength;beanIndex++){
                CorrespondenceTypeFormBean correspTypeBean =
                (CorrespondenceTypeFormBean)correspData.elementAt(beanIndex);
                int presentCode = correspTypeBean.getProtoCorrespTypeCode();
                String desc = correspTypeBean.getProtoCorrespTypeDesc();
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(presentCode+" : "+desc,true);
                root.add(child);
                ((DefaultTreeModel)treeCorresp.getModel()).nodeStructureChanged(root);
                String comm = correspTypeBean.getCommitteeId();
                if(comm != null){
                    child.add(new DefaultMutableTreeNode(comm,false));
                }
                for(int newIndex = beanIndex+1;newIndex<beansLength;newIndex++){
                    CorrespondenceTypeFormBean tempBean =
                    (CorrespondenceTypeFormBean)correspData.elementAt(newIndex);
                    int newCode = tempBean.getProtoCorrespTypeCode();
                    if(newCode == presentCode){
                        comm = tempBean.getCommitteeId();
                        if(comm != null){
                            int childIndex = getIndex(comm,child);
                            child.insert(new DefaultMutableTreeNode(comm,false),childIndex);
                        }
                        beanIndex++;
                    }else{
                        break;
                    }
                }
            }
        }
        /*treeCorresp.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(MouseEvent mouseEvent){
                if(mouseEvent.getClickCount()==2){
                    TreePath  treePath = treeCorresp.getSelectionPath();
                    if(treePath !=null){
                        int count = treePath.getPathCount();
                        try{
                            if(count == 2){
                                addCorrespondenceTypes();
                            }else if(count == 3){
                                editTemplate();
                            }
                        }catch(CoeusClientException ex){
                            CoeusOptionPane.showErrorDialog(ex.getMessage());
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });*/
    }// ends build tree
    int getIndex(String newNode,MutableTreeNode parent){
        int count = parent.getChildCount();
        for(int index = 0;index<count;index++){
            MutableTreeNode child = (MutableTreeNode)parent.getChildAt(index);
            if(newNode.compareToIgnoreCase(child.toString())<0){
                return index;
            }
        }
        return count;
    }
    //tree.setCellRenderer(new MyRenderer());
    // inner class to renderer the tree
    
    class TreeRenderer extends DefaultTreeCellRenderer {
        ImageIcon commIcon;
        ImageIcon correspIcon;
        int count = 0;
        public TreeRenderer() {
            commIcon = new ImageIcon(getClass().getClassLoader().getResource(
            CoeusGuiConstants.CORRESPONDENCE_NODE_ICON));
            correspIcon = new ImageIcon(getClass().getClassLoader().getResource(
            CoeusGuiConstants.CORRESPONDENCE_TEMPLATES_ICON));
            this.setBackgroundNonSelectionColor(Color.lightGray);
        }
        public Component getTreeCellRendererComponent(JTree tree,Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){
            TreePath path = tree.getPathForRow(row);
            super.getTreeCellRendererComponent(
            tree, value, sel,
            expanded, leaf, row,
            hasFocus);
            setText(value.toString());
            if(path != null){
                if(path.getPathCount() >2){
                    setIcon(commIcon);
                }else{
                    setIcon(correspIcon);
                }
            }
            return this;
        }
    }
    
    /** Overridden method of <code>ActionListener</code>. Actions to be performed
     * for all the menu items and toolbars associated with this form are specified
     * in this method.
     * @param actionType <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent actionType) {
        try{
            Object actSource = actionType.getSource();
            if(actSource.equals(addProtoCorrespType) ||
            actSource.equals(btnAddProtoCorrespType)){
                addCorrespondenceTypes();
            }if (actSource.equals(deleteProtoCorrespType) ||
            actSource.equals(btnDeleteProtoCorrespType )){
                deleteCorrespondenceTypes();
            }else if (actSource.equals(editProtoCorrespType) ||
            actSource.equals(btnEditProtoCorrespType)){    /* Open Template*/
                editTemplate();
            }else if(actSource.equals(btnTemplate) || actSource.equals(openTemplate)){
                showTemplate();
            }else if (actSource.equals(btnCloseProtoCorrespType)) {
                this.doDefaultCloseAction();
            }
        }catch(Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        
    }
    /** this method used to update exiting Correspondence details.
     *
     * @throws It throws Exception if any error occurs
     */
    
    private void editTemplate()throws Exception{
        TreePath path = treeCorresp.getSelectionPath();
        if(path != null){
            int count = path.getPathCount();
            if(count == 3){
                CorrespondenceTypeFormBean correspBean = null;
                String commId = path.getLastPathComponent().toString();
                String correspCode = path.getPathComponent(1).toString();
                int codeIndex = correspCode.indexOf(':');
                correspCode = correspCode.substring(0,codeIndex).trim();
                if(correspData !=null && correspData.size() >0){
                    int index=0;
                    for(;index <correspData.size();index++){
                        correspBean = (CorrespondenceTypeFormBean)correspData.elementAt(index);
                        if(correspCode.equals(""+correspBean.getProtoCorrespTypeCode())){
                            if(commId.equals(correspBean.getCommitteeId())){
                                break;
                            }
                        }
                    }
                    if(index != correspData.size()){
                        correspBean.setAcType('U');
                        correspBean.setAw_Committee_Id(correspBean.getCommitteeId());
                        correspBean.setCommitteeId(correspBean.getCommitteeId());
                        correspBean.setCommitteeName(correspBean.getCommitteeName());
                        CoeusDlgWindow dlgWindow = new CoeusDlgWindow(this.mdiForm);
                        fileChooser = new CoeusFileChooser(dlgWindow); /* Creating an instance of fileChooser */
                        fileChooser.showFileChooser();             /*   Opening a file Chooser */
                        if(fileChooser.isFileSelected()){          /*   Checking whether a file is selected or not*/
                            String fileName = fileChooser.getSelectedFile();
                            if(fileName != null && !fileName.trim().equals("")){
                                int fileIndex = fileName.lastIndexOf('.');
                                if(fileIndex != -1 && fileIndex != fileName.length()){
                                    String extension = fileName.substring(fileIndex+1,fileName.length());
                                    if(extension != null && (extension.equalsIgnoreCase("xml")  || extension.equalsIgnoreCase("xsl")) ){
                                        correspBean.setFileBytes(fileChooser.getFile());
                                        if(saveToDataBase(correspBean)){
                                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                            "correspType_exceptionCode.1014"));
                                        }else{
                                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                            "correspType_exceptionCode.1005"));
                                        }
                                    }else{
                                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                        "correspType_exceptionCode.1011"));
                                    }
                                }else{
                                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                    "correspType_exceptionCode.1012"));
                                }
                            }//ends filename if
                        }//ends file selected if
                    }//ends correspsize index if
                }// ends corresp size if
            }else{
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                "correspType_exceptionCode.1003"));
            }
        }else{
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            "correspType_exceptionCode.1006"));
        }
    }// ends edit template
    /** this shows a template in the browser
     */
    private void showTemplate()throws Exception{
        TreePath treePath = treeCorresp.getSelectionPath();
        if(treePath != null){
            if(treePath.getPathCount() == 3){
                AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
                CorrespondenceTypeFormBean correspBean = null;
                String correspCode = treePath.getLastPathComponent().toString();
                MutableTreeNode treeNode = (MutableTreeNode)treePath.getLastPathComponent();
                String code = (String)treePath.getPathComponent(1).toString();
                int codeIndex = code.indexOf(':');
                code = code.substring(0,codeIndex).trim();
                if(correspData !=null && correspData.size() >0){
                    int index=0;
                    for(;index <correspData.size();index++){
                        correspBean = (CorrespondenceTypeFormBean)correspData.elementAt(index);
                        if(code.equals(""+correspBean.getProtoCorrespTypeCode())){
                            if(treeNode.toString().equals(correspBean.getCommitteeId())){
                                break;
                            }
                        }
                    }
                    if(index != correspData.size()){
                        
                        RequesterBean request = new RequesterBean();
//                        request.setFunctionType('E');
//                        request.setDataObject(correspBean);
                        DocumentBean documentBean = new DocumentBean();
                        Map map = new HashMap();
                        map.put("DATA", correspBean);
                        map.put("USERID", mdiForm.getUserId());
                        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.CorrespondenceTemplateReader");
                        documentBean.setParameterMap(map);
                        request.setDataObject(documentBean);
                        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
                        AppletServletCommunicator comm
                        = new AppletServletCommunicator(STREAMING_SERVLET, request);
                        comm.send();
                        ResponderBean response = comm.getResponse();
                        if(response.isSuccessfulResponse()){
//                            String url = (String)response.getDataObject();
//                            url = url.replace('\\', '/');
//                            URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//                            if(coeusContext != null)
//                            {
//                                coeusContext.showDocument( templateURL, "_blank" );
//                            }
//                            else //prps added this else clause on 29 oct 2003
//                            { // for webstart
//                                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                                bs.showDocument(templateURL);
//                            }    
                            map = (Map)response.getDataObject();
                            String strUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                            strUrl = strUrl.replace('\\', '/');
                            URL urlObj = new URL(strUrl);
                            URLOpener.openUrl(urlObj);
                        }
                    }
                }
            }else{
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                "correspType_exceptionCode.1007"));
            }
        }else{
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            "correspType_exceptionCode.1006"));
        }
    }
    
    
    /** this method saves the data to database.
     *
     * @param this takes CorrespondenceTypeFormBean instance as parameter to save to database
     * @return returns true if saves to the database
     * @throws Exception when any exception o
     *
     */
    private  boolean saveToDataBase(CorrespondenceTypeFormBean correspBean)throws Exception {
        RequesterBean request = new RequesterBean();
        request.setFunctionType('J');
        Vector data = new Vector();
        data.addElement(correspBean);
        request.setDataObject(data);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            correspData = (Vector)response.getDataObject();
            return true;
        }
        else{
            return false;
        }
    }
    /*
     * This method is used to add a CorrespondenceTypes.
     */
    /** this method adds new correspondence types to database. This will call a dialog window to get the details of new correspondence type
     * @throws Exception any exception occurs
     *
     */
    private void addCorrespondenceTypes() throws Exception{
        TreePath path = treeCorresp.getSelectionPath();
        if(path != null){
            int count = path.getPathCount();
            if(count == 2){
                String correspCode = path.getLastPathComponent().toString();
                int index = correspCode.indexOf(':');
                correspCode = correspCode.substring(0,index).trim();
                
                ProtocolCorrespDialog protocolDialog =
                new ProtocolCorrespDialog(mdiForm,"Committee template upload",true,correspData,correspCode);
                if(protocolDialog.isSaveRequired()){
                    CorrespondenceTypeFormBean newCorrespValue = protocolDialog.getCorrespBeantData();
                    newCorrespValue.setAcType('I');
                    if(saveToDataBase(newCorrespValue)){
                        MutableTreeNode parent = (MutableTreeNode)path.getLastPathComponent();
                        MutableTreeNode newNode = new DefaultMutableTreeNode(protocolDialog.getCommitteeID(),false);
                        int newIndex = getIndex(newNode.toString(),parent);
                        parent.insert(newNode,newIndex);
                        ((DefaultTreeModel)treeCorresp.getModel()).nodeStructureChanged(parent);
                        treeCorresp.expandPath(path);
                        TreePath newPath = path.pathByAddingChild(newNode);
                        treeCorresp.setSelectionPath(newPath);
                        treeCorresp.scrollRowToVisible(
                        treeCorresp.getRowForPath(newPath));
                    }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                        "correspType_exceptionCode.1005"));
                    }
                }
            }else{
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                "correspType_exceptionCode.1006"));
            }
        }else{
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            "correspType_exceptionCode.1006"));
        }
    }
    /** this method deletes existing correspondence type
     * @throws Exception when any exception occurs
     */
    private void deleteCorrespondenceTypes() throws Exception{
        TreePath path = treeCorresp.getSelectionPath();
        if(path != null){
            int count = path.getPathCount();
            if(count == 3){
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(
                "correspType_exceptionCode.1008")),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES){
                    CorrespondenceTypeFormBean correspBean = null;
                    String correspCode = path.getLastPathComponent().toString();
                    MutableTreeNode treeNode = (MutableTreeNode)path.getLastPathComponent();
                    String code = (String)path.getPathComponent(1).toString();
                    int codeIndex = code.indexOf(':');
                    code = code.substring(0,codeIndex).trim();
                    MutableTreeNode parent = (MutableTreeNode)path.getPathComponent(1);
                    if(correspData !=null && correspData.size() >0){
                        int index=0;
                        for(;index <correspData.size();index++){
                            correspBean = (CorrespondenceTypeFormBean)correspData.elementAt(index);
                            if(code.equals(""+correspBean.getProtoCorrespTypeCode())){
                                if(treeNode.toString().equals(correspBean.getCommitteeId())){
                                    break;
                                }
                            }
                        }
                        if(index != correspData.size()){
                            correspBean.setAcType('D');
                            correspBean.setAw_Committee_Id(correspBean.getCommitteeId());
                            if(saveToDataBase(correspBean)){
                                ((DefaultTreeModel)treeCorresp.getModel()).removeNodeFromParent(treeNode);
                                ((DefaultTreeModel)treeCorresp.getModel()).nodeStructureChanged(parent);
                            }else{
                                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                "correspType_exceptionCode.1005"));
                            }
                        }
                    }
                }
            }else{
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                "correspType_exceptionCode.1009"));
            }
        }else{
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            "correspType_exceptionCode.1009"));
        }
    }
    
    /**
     * constructs CorrespondenceTypes edit menu
     *
     * @return JMenu CorrespondenceTypes edit menu
     */
    private CoeusMenu buildEditMenu() {
        CoeusMenu mnuProtoCorrespType = null;
        Vector fileChildren = new Vector();
        
        /* This is to add a new CorrespondenceTypes */
        addProtoCorrespType = new CoeusMenuItem("Add",null,true,true);
        addProtoCorrespType.setMnemonic('A');
        addProtoCorrespType.addActionListener(this);
        
        deleteProtoCorrespType = new CoeusMenuItem("Delete",null,true,true);
        deleteProtoCorrespType.setMnemonic('D');
        deleteProtoCorrespType.addActionListener(this);
        
        /* This is to upload the selected pdf file*/
        editProtoCorrespType = new CoeusMenuItem("Upload template",null,true,true);
        editProtoCorrespType.setMnemonic('E');
        editProtoCorrespType.addActionListener(this);
        
        openTemplate = new CoeusMenuItem("View Template",null,true,true);
        openTemplate.setMnemonic('T');
        openTemplate.addActionListener(this);
        
        
        fileChildren.add(addProtoCorrespType);
        fileChildren.add(editProtoCorrespType);
        fileChildren.add(openTemplate);
        fileChildren.add(deleteProtoCorrespType);
        
        /* This is the mail Edit menu which will be added to the main MDIMenu*/
        mnuProtoCorrespType = new CoeusMenu("Edit",null,fileChildren,true,true);
        mnuProtoCorrespType.setMnemonic('E');
        return mnuProtoCorrespType;
    }
    
    
    /**
     * CorrespondenceTypes ToolBar is a which provides the Icons for Performing
     * Save, Add, Mofify, Close buttons.
     *
     * @returns JToolBar CorrespondenceTypes Toolbar
     */
    private JToolBar buildToolBar() {
        JToolBar toolbar = new JToolBar();
        /* This is the tool bar button to add a new CorrespondenceTypes*/
        btnAddProtoCorrespType = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),null,
        "Add Correspondence Type");
        
        /* This is the tool bar button to add a new CorrespondenceTypes*/
        btnDeleteProtoCorrespType = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),null,
        "Delete Correspondence Type");
        
        /* This is the tool bar button used to upload pdf file*/
        btnEditProtoCorrespType = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CORRESPONDENCE_UPLOAD_ICON)),null,
        "Upload new Template ");
        btnTemplate = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.XML_ICON)),null,
        "View Template");
        
        /* This is used to close the existing opened CorrespondenceTypes details window */
        btnCloseProtoCorrespType = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),null,
        "Close Correspondence Window");
        
        btnAddProtoCorrespType.addActionListener(this);
        btnDeleteProtoCorrespType.addActionListener(this);
        btnEditProtoCorrespType.addActionListener(this);
        btnTemplate.addActionListener(this);
        btnCloseProtoCorrespType.addActionListener(this);
        
        toolbar.add(btnAddProtoCorrespType);
        //modified by Vyjayanthi
        //changed the order of display of the edit and delete buttons
        toolbar.add(btnEditProtoCorrespType);
        toolbar.add(btnDeleteProtoCorrespType);
        toolbar.add(btnTemplate);
        //toolbar.addSeparator();
        toolbar.add(btnCloseProtoCorrespType);
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    /** This method is used to get the details of all the CorrespondenceTypess from the
     * database.
     *
     * @return Collection of <CODE>CorrespondenceTypesMaintenanceFormBean</CODE> whose values
     * will be used to populate the CorrespondenceTypes list table.
     * @throws Exception when any exception occurs
     */
    public Vector getCorrespList() throws Exception{
        /**
         * This sends the functionType as 'G' to the servlet indiacting to
         * get the details of all existing correspondence types with the required
         * information
         */
        Vector correspTypes = null;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('G');
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            correspTypes = (Vector)response.getDataObject();
        } else {
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    throw (CoeusException)obj;
                }
            }else{
                throw new Exception(response.getMessage());
            }
        }
        return correspTypes;
    }
    
    /**
     * This abstract method must be implemented by all classes which inherits this class.
     * Used for saving the current activesheet when clicked Save from File menu.
     */
    public void saveActiveSheet() {
        try{
            // this.saveCorrespData();
        }catch(Exception ex){
            
        }
    }
    
    public void saveAsActiveSheet() {
        
    }
    
}// End of Main Class

