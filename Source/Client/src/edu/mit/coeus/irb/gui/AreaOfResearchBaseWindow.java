/*
 * @(#)AreaOfResearchBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 06-AUGUST-2010
 * by Johncy M John
 */
package edu.mit.coeus.irb.gui;

import edu.mit.coeus.utils.ModuleConstants;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.beans.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;

import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.tree.*;
import edu.mit.coeus.irb.bean.AreaOfResearchTreeNodeBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * The class used to represent main/base window (CoeusInternalFrame) for
 * Area of Research tree structure. This component contain the toolbar and menubar
 * to provide plumbing functionalities of Area of Research like 
 * Add/Modify/Move/Drag & Drop to the Area of Research Tree Nodes.
 *
 * @author  Subramanya
 * @version 1.0 September 29, 2002, 11:50 PM
 */

public class AreaOfResearchBaseWindow  extends CoeusInternalFrame 
                                       implements ActionListener,
                                                  TreeSelectionListener{
                                                      
    //Added by Chandra - To check whether the right is there or not..call server
    private static final String MAINTAIN_AREA_OF_RESEARCH = "MAINTAIN_AREAS_OF_RESEARCH";
    private static final String FN_USER_HAS_RIGHT_IN_ANY_UNIT = "FN_USER_HAS_RIGHT_IN_ANY_UNIT";
    private static final String FUNCTION_SERVLET = "/coeusFunctionsServlet";
    String connectTo = CoeusGuiConstants.CONNECTION_URL+ FUNCTION_SERVLET;   
    // End Chandra
    private final static String connURL = CoeusGuiConstants.CONNECTION_URL + "/AreaOfResearchServlet";                                                  
    
    //Customized Menu Items for Add/Modify/Move Functionality. 
    private CoeusMenuItem addNode;//Adding new Area Of Research
    private CoeusMenuItem modifyNode;//Modify the Existing Area Of Research
    private CoeusMenuItem moveNode;//Move Node between Parents
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC
    private CoeusMenuItem deleteNode;//Delete the Existing Area Of Research
    
    //Customized Menu Items for Search Functionality. 
    private CoeusMenuItem searchNode;//Search for Sepcific Area Of Research    
        
    // Customized Tool bar for Area Of Research Module    
    private CoeusToolBarButton tlbrAddNode;
    private CoeusToolBarButton tlbrModifyNode;
    private CoeusToolBarButton tlbrMoveNode;
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC
    private CoeusToolBarButton tlbrDeleteNode;
    private CoeusToolBarButton tlbrSaveTree;
    private CoeusToolBarButton tlbrSearchNode;
    private CoeusToolBarButton tlbrClose;       
        
    //Research Area Code Root Node ID. Ex:0000001
    private final static String ROOT_NODE_ID = "000001";
    
    //Main JTree with DnD properties. obtained from 
    //Form/Component of AreaOfResearchHerDetailForm Instance.     
    private DnDJTree trAreaOfResearchLive = null;
    
    //Defualt Selected Tree Node Data Bean.
    private AreaOfResearchTreeNodeBean selAORBean = null;
    
    //Current Tree Selected Node Path
    private TreePath selAORTreePath = null;
        
    //Current Tree Selected Node Path
    //private TreePath lastSelAORTreePath = null;
    
    //Current Tree Selected Node Path
    private HierarchyNode selAORMutableTreeNode = null;
        
    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;
    
    //Area Of Research Tree Cosntructing component/Form
    private AreaOfResearchHerDetailForm researchAreaForm = null;
    
    //holds save flag from previous operations
    private boolean isSavedSoFar = true;
    
    /* Area Of Research New Nodes Added during this Application Session
     * key -> nodeID/ResearchCode ; Value - AreaOfResearchTreeNodeBean
     */
    private Hashtable newAORData = null;
    
    /* Holds the Modified Nodes of Research Area Tree.
     * key -> nodeID/ResearchCode ; Value - AreaOfResearchTreeNodeBean
     */
    private Hashtable updatedAORData = null;
    
    private Hashtable deletedAORData = null;
    
    //holds the Source Tree Node of Move
    private HierarchyNode srcTreeNode = null;
    
    //holds the Parent Tree Node of Source.
    private HierarchyNode srcParentTreeNode = null;
    
    //holds the destination Tree Node of Move.
    private HierarchyNode dstTreeNode = null;
    
    //Move State
    private boolean isMovePressed = false;
        
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //holds the boolean value for close
    private boolean isCalledFromClose = false;

    private static final char CHECK_DEPENDENCY = 'C';
    
    /**
     * Constructs the AreaOfResearchBaseWindow with base Window as MDI form.
     * @param mdiForm   represent the main Application From where AOR Displayed
     */
    public AreaOfResearchBaseWindow( CoeusAppletMDIForm mdiForm ){ 
        
            super(CoeusGuiConstants.TITLE_AREA_OF_RESEARCH,  mdiForm );
            this.mdiForm = mdiForm;
            initComponents();            
            this.setFrame(CoeusGuiConstants.TITLE_AREA_OF_RESEARCH);
            mdiForm.putFrame( CoeusGuiConstants.TITLE_AREA_OF_RESEARCH, this );
            mdiForm.getDeskTopPane().add( this );
            this.setVisible(true);
    }
    
    
    /**
     * Constructs the AreaOfResearchBaseWindow with base Window as MDI Form.
     * @param frameName represent the Name of the Window/Frame
     * @param mdiForm   represent the main Application From where AOR Displayed
     */
    public AreaOfResearchBaseWindow( String frameName, 
                                                   CoeusAppletMDIForm mdiForm ){
        
            super( frameName,  mdiForm );
            this.mdiForm = mdiForm;
            initComponents();                        
            this.setFrame(CoeusGuiConstants.TITLE_AREA_OF_RESEARCH);
            mdiForm.putFrame(CoeusGuiConstants.TITLE_AREA_OF_RESEARCH, this );
            mdiForm.getDeskTopPane().add( this );            
    }
    
    
    /**
     * Method Used to initialize components like tool bar, menu bar & tree.
     */
    private void initComponents(){        
        
        try{
            researchAreaForm = new AreaOfResearchHerDetailForm( ROOT_NODE_ID );
            getContentPane().add( researchAreaForm ); 
            setFrameMenu( getAOREditMenu() );
            setToolsMenu( getAORToolMenu() );
            setFrameToolBar(getAORToolBar());            
            trAreaOfResearchLive = researchAreaForm.
                                                getAreaOfResearchDisplayTree();
            trAreaOfResearchLive.addTreeSelectionListener( this );            
            newAORData = new Hashtable();
            updatedAORData = new Hashtable();
            deletedAORData = new Hashtable();         
            ImageIcon imgIconCoeus  =  new ImageIcon(getClass().getClassLoader().
                                        getResource("images/coeus16.gif"));
            super.setFrameIcon( imgIconCoeus );
            
            this.addVetoableChangeListener(new VetoableChangeListener(){                     
                public void vetoableChange(PropertyChangeEvent pce)
                throws PropertyVetoException {
                    
                    //Added by Chandra
                    if (pce.getPropertyName().equals(
                        JInternalFrame.IS_CLOSED_PROPERTY)) {
                        maintainSaveMenu(true);               
                    }
                    //Added by Chandra
                    
                    if (pce.getPropertyName().equals(
                                        JInternalFrame.IS_CLOSED_PROPERTY) &&
                                        !isCalledFromClose ){
                        boolean changed = (
                            (Boolean) pce.getNewValue()).booleanValue();
                      if( changed ){
                            
                        Hashtable checkMoved = 
                                        trAreaOfResearchLive.getModifiedNodes();            
                        if( !isSavedSoFar ||  ( checkMoved != null &&  
                                                          checkMoved.size()>0)){                

                            int toSave = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(
                                        "saveConfirmCode.1002"), 
                                CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                                CoeusOptionPane.DEFAULT_YES);

                            switch( toSave ){
                                case JOptionPane.YES_OPTION  :
                                    saveAllChanges();                                 
                                break;
                                case JOptionPane.NO_OPTION :                                
                                    //doDefaultCloseAction();
                                //dispose();
                                break;
                                case JOptionPane.CANCEL_OPTION :
                                case JOptionPane.CLOSED_OPTION :
                                    throw new PropertyVetoException(
                                        coeusMessageResources.parseMessageKey(
                                            "protoDetFrm_exceptionCode.1130"),
                                        null);                                
                            }
                        } 
                      }
                    }
                }});
                
            coeusMessageResources = CoeusMessageResources.getInstance();    
            trAreaOfResearchLive.setSelectionInterval(0,0);  //Updated for default Root Node Selection. 
            
            //Check whether the User has rights to Maintain hierarchy- Added by chandra
        boolean hasAORRight = isUserHasRight();
        tlbrAddNode.setEnabled(hasAORRight);
        tlbrModifyNode.setEnabled(hasAORRight);
        tlbrMoveNode.setEnabled(hasAORRight);
        tlbrDeleteNode.setEnabled(hasAORRight);
        tlbrSaveTree.setEnabled(hasAORRight);
        // Disable the menu items
        addNode.setEnabled(hasAORRight);
        modifyNode.setEnabled(hasAORRight);
        moveNode.setEnabled(hasAORRight);
        deleteNode.setEnabled(hasAORRight);
        maintainSaveMenu(hasAORRight);
        
        //Drag and Drop facility based on user right.
        if (!hasAORRight) {
            trAreaOfResearchLive.getDropTarget().setActive(false);
        }
        
        // End Chandra
            
        }catch( Exception treePanelErr ){
            treePanelErr.printStackTrace();
        }
        
    }
    
    
    /**
     * Constructs the Area Of Research Tool Menu which embeds Search Option.
     * @return CoeusMenu The Coeus Tool Menu having Search MenuItem for search
     * of Area Of Research.
     */
    public CoeusMenu getAORToolMenu(){
    
        CoeusMenu coeusMenuTool = null;        
        Vector vtrToolMenuItems = new Vector();
        searchNode = new CoeusMenuItem( "Search", null, true, true );
        searchNode.setMnemonic( 'S' );
        searchNode.addActionListener( this );                
        vtrToolMenuItems.addElement( searchNode );                
        coeusMenuTool = new CoeusMenu( "Tools", null,vtrToolMenuItems,true,true);
        coeusMenuTool.setMnemonic( 'T' );        
        return coeusMenuTool;        
    }
    
    
    /**
     * constructs Area of Research Edit Menu with Add/Modify/Display
     *
     * @return CoeusMenu edit menu items of Area Of Research
     */
    public CoeusMenu getAOREditMenu(){        
        CoeusMenu coeusMenu=null;        
        java.util.Vector fileChildren = new java.util.Vector();        
        addNode = new CoeusMenuItem( "Add", null, true, true );
        addNode.addActionListener( this );
        addNode.setMnemonic( 'A' );        
        modifyNode  = new CoeusMenuItem( "Modify", null, true, true );
        modifyNode.setMnemonic( 'M' );
        modifyNode.addActionListener( this );
        //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
        deleteNode = new CoeusMenuItem( "Delete", null, true, true );
        deleteNode.addActionListener( this );
        deleteNode.setMnemonic( 'D' );
        moveNode = new CoeusMenuItem( "Move", null, true, true );
        moveNode.setMnemonic( 'V' );
        moveNode.addActionListener( this );         
        fileChildren.addElement( addNode );
        fileChildren.addElement( modifyNode );
        fileChildren.addElement( deleteNode );
        fileChildren.addElement( moveNode );        
        coeusMenu = new CoeusMenu( "Edit", null, fileChildren, true, true);
        coeusMenu.setMnemonic( 'E' );        
        return coeusMenu;        
    }
    
    
    /** Area Of Research ToolBar is a which provides the Icons for Performing
     * Save, Add, Modify, Close buttons.
     *
     * @return JToolBar sponsor toolbar
     */
    public JToolBar getAORToolBar(){
        
        JToolBar toolbar = new JToolBar();   
        
        /**
         * ClassLoader Implementation for WebStart
         */
//        tlbrAddNode = new CoeusToolBarButton( new ImageIcon(
//                            getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
//                            null, "Add New Area Of Research" );        
//        tlbrModifyNode = new CoeusToolBarButton( new ImageIcon(
//                           getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
//                           null, "Modify Area Of Research");        
//        tlbrMoveNode = new CoeusToolBarButton( new ImageIcon(
//                            getClass().getClassLoader().getResource(CoeusGuiConstants.MOVE_ICON)),
//                            null, "Move Area Of Research");        
//        tlbrSearchNode = new CoeusToolBarButton( new ImageIcon(
//                         getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
//                            null, "Search for Area Of Research");        
//        tlbrSaveTree = new CoeusToolBarButton( new ImageIcon(
//                            getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
//                            null, "Save Area Of Research Tree");        
//        tlbrClose = new CoeusToolBarButton( new ImageIcon(
//                           getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
//                           null, "Close Area Of Research Tree Window");
        
        tlbrAddNode = new CoeusToolBarButton( new ImageIcon(
                            getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
                            null, "Add New IRB Area Of Research" );        
        tlbrModifyNode = new CoeusToolBarButton( new ImageIcon(
                           getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
                           null, "Modify IRB Area Of Research"); 
        //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
        tlbrDeleteNode = new CoeusToolBarButton( new ImageIcon(
                            getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
                            null, "Delete IRB Area Of Research"); 
        tlbrMoveNode = new CoeusToolBarButton( new ImageIcon(
                            getClass().getClassLoader().getResource(CoeusGuiConstants.MOVE_ICON)),
                            null, "Move IRB Area Of Research");         
        tlbrSearchNode = new CoeusToolBarButton( new ImageIcon(
                         getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
                            null, "Search for IRB Area Of Research");        
        tlbrSaveTree = new CoeusToolBarButton( new ImageIcon(
                            getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
                            null, "Save IRB Area Of Research Tree");        
        tlbrClose = new CoeusToolBarButton( new ImageIcon(
                           getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
                           null, "Close IRB Area Of Research Tree Window");
        
        tlbrSaveTree.addActionListener( this );
        tlbrSearchNode.addActionListener( this );
        tlbrAddNode.addActionListener( this );
        tlbrModifyNode.addActionListener( this );
        tlbrMoveNode.addActionListener( this );
        tlbrDeleteNode.addActionListener( this );
        tlbrClose.addActionListener( this );
        
        toolbar.add( tlbrAddNode );
        toolbar.add( tlbrModifyNode );
         toolbar.add( tlbrDeleteNode );
        toolbar.add( tlbrMoveNode );        
        toolbar.add( tlbrSearchNode ); 
        toolbar.add( tlbrSaveTree );
        toolbar.addSeparator();
        toolbar.add( tlbrClose );
        
        toolbar.setFloatable( false );
        
        return toolbar;        
    }
    
        
    /**
     * Takes Care of Actions like Add/Modify/Close/Save on Area Of Research 
     * Tree.
     * @param    actionType  it can be Add / Modify / Close both from Menu as 
     *                       Toolbar Icon ( button ).
     */
    public void actionPerformed( ActionEvent actionType ){
          
        Object actSource = actionType.getSource();
        trAreaOfResearchLive.setCursor( 
                                          new Cursor ( Cursor.DEFAULT_CURSOR));
        
        if( actSource.equals( addNode ) || actSource.equals( tlbrAddNode )){            
                boolean isAdded = addNewAreaOfResearch( "" );                
                if( isAdded ) {                    
                    isSavedSoFar = false;                 
                }                
                
                trAreaOfResearchLive.setSelectionPath( selAORTreePath );
                trAreaOfResearchLive.revalidate();
                
                
        }else if(  actSource.equals( modifyNode ) || 
                   actSource.equals( tlbrModifyNode ) ) {  
                       
                Hashtable aorMovedData = trAreaOfResearchLive.getModifiedNodes();
                if( aorMovedData!= null &&
                    selAORBean!= null &&
                    aorMovedData.containsKey( selAORBean.getNodeID()) ){
                    selAORBean = ( AreaOfResearchTreeNodeBean )
                                aorMovedData.get( selAORBean.getNodeID() );
                }
                boolean isModified = modifyAreOfResearchEntry();                             
                if( isModified ){                    
                    isSavedSoFar = false;                    
                }                                
                trAreaOfResearchLive.setSelectionPath( selAORTreePath );
                trAreaOfResearchLive.revalidate();                
                
        }else if( actSource.equals( moveNode ) || actSource.equals( 
                                                            tlbrMoveNode )){

            //Press(Operate)/Release(Cancel) Action of Move Button.                                                                     
            if( selAORBean != null ){                    
                
                trAreaOfResearchLive.setCursor( 
                                            new Cursor ( Cursor.HAND_CURSOR ) );                
                isMovePressed = true;                    
                
            }else {                
                CoeusOptionPane.showWarningDialog(
                    coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1003"));                
                trAreaOfResearchLive.setCursor( 
                                            new Cursor( Cursor.DEFAULT_CURSOR));                
                isMovePressed = false;                                             
            }
        }
        //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
        else if( actSource.equals( deleteNode ) || actSource.equals( tlbrDeleteNode )){ 
             boolean isDeleted = deleteAreaOfResearchEntry();    
             if( isDeleted ){                    
                    isSavedSoFar = false;                    
             } 
        }
        else if( actSource.equals( tlbrSaveTree )){            
            
                saveAllChanges();                                
                
        }else if( actSource.equals( tlbrSearchNode ) || ( actSource.equals(
                                                                searchNode ))){
         
            try{      
            
            CoeusSearch aorSearch = new CoeusSearch( mdiForm, "AORSEARCH", 
                                                    CoeusSearch.TWO_TABS ) ;
            aorSearch.showSearchWindow();
            //HashMap result = aorSearch.getSelectedRow();            
            String aorCode = aorSearch.getSelectedValue()+" : ";
            if( aorCode == null ){                
                 return;                 
            }
            
            trAreaOfResearchLive.expandPath( 
                       new TreePath(trAreaOfResearchLive.getModel().getRoot()));
           /*String aorRelativeName = result.get("RESEARCH_AREA_CODE").toString()+
                                    " : " + result.get("DESCRIPTION").toString();
            */
            TreePath aorSelectedPath = findByName( trAreaOfResearchLive, 
                                                    aorCode );
            trAreaOfResearchLive.expandPath( aorSelectedPath );
            trAreaOfResearchLive.setSelectionPath( aorSelectedPath );
            trAreaOfResearchLive.scrollRowToVisible(
                    trAreaOfResearchLive.getRowForPath(aorSelectedPath));            
            
            }catch( Exception err ){
                err.printStackTrace();
            }            
        }else if( actSource.equals( tlbrClose )){            
            closeAreaOfResearch();            
        }
                
    }

        
    /**
     * This method is fired when the Tree Node Selected.
     * @param   treeEvent   represent the node selected information/data.
     */
    public void valueChanged( TreeSelectionEvent treeEvent ){
        
        selAORTreePath = treeEvent.getNewLeadSelectionPath();          
        if( selAORTreePath != null  ){        
            processTreeNodeSelection();            
        }        
    }

    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
    /**
     * This method is used to remove area of research entry from GUI
     * @return  boolean (true if able to delete, otherwise flase)
     */
    private boolean deleteAreaOfResearchEntry() {
        
         boolean isNodeDeleted = false;
         String messageKey = null;
         if( selAORTreePath != null && selAORBean != null && !( selAORBean.getNodeID().equalsIgnoreCase(ROOT_NODE_ID ))){
                
                DefaultTreeModel treeModel = (DefaultTreeModel)trAreaOfResearchLive.getModel();
                DefaultMutableTreeNode treeNode = ( HierarchyNode )selAORTreePath.getLastPathComponent();
                if(treeNode.getChildCount() > 0) {
                    messageKey = "areaRsrchBaseWin_exceptionCode.1013";
                } else if (treeNode.getChildCount() == 0) {
                    messageKey = "protoAORFrm_delConfirmCode.1050";
                }
                int option = CoeusOptionPane.showQuestionDialog( 
                            coeusMessageResources.parseMessageKey(messageKey),
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(option == CoeusOptionPane.SELECTION_YES){                    
                    int canDelete = checkCanDeleteResearchArea(selAORBean); 
                    if(canDelete != -1){
                        if(canDelete == 1) {
                            treeModel.removeNodeFromParent(treeNode);
                            //create hash table for deleted nodes
                            deletedAORData.put( selAORBean.getNodeID(), selAORBean );
                            isNodeDeleted = true;
                        } else {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("areaRsrchBaseWin_exceptionCode." + canDelete));
                        }
                    }
                } 
                 
             } else if( selAORTreePath != null && selAORBean != null && selAORBean.getNodeID().equalsIgnoreCase( ROOT_NODE_ID )){
                CoeusOptionPane.showWarningDialog(
                                    coeusMessageResources.parseMessageKey(
                                            "areaRsrchBaseWin_exceptionCode.1008"));              
             }else {                
                CoeusOptionPane.showWarningDialog(
                                    coeusMessageResources.parseMessageKey(
                                            "areaRsrchBaseWin_exceptionCode.1003"));              
             } 
         return isNodeDeleted;
        
    }

    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
     /**
     * This method checks whether any dependency exits for selected Area Of Research on Protocol/Committee.
     * @return  int (contains error code if dependency exists, else returns 1)
     */
     private int checkCanDeleteResearchArea(AreaOfResearchTreeNodeBean areaOfResearchBean) {

        int canDelete = -1;
        try {
            RequesterBean requesterBean = new RequesterBean();
            ResponderBean responderBean = new ResponderBean();
            requesterBean.setFunctionType(CHECK_DEPENDENCY);
            requesterBean.setDataObject(areaOfResearchBean);
            Vector dataObjects = new Vector();
            dataObjects.addElement(ModuleConstants.PROTOCOL_MODULE_CODE);
            requesterBean.setDataObjects(dataObjects);
            AppletServletCommunicator comm = new AppletServletCommunicator(connURL, requesterBean);
            comm.send();
            if(responderBean!= null){
            responderBean = comm.getResponse();
                if (responderBean.isSuccessfulResponse()) {
                    canDelete = Integer.parseInt(responderBean.getId());            
                }
            }
           }catch(Exception ex) {
                ex.printStackTrace();
           }
       return canDelete ;
     }
    
        
    //Method to Support Tree Selected Node Post Operations .( for move and
    // to get the data bean for add/modify )
    private void processTreeNodeSelection () {
        
        //boolean isMoveSuccessful = false;               
        selAORMutableTreeNode = ( HierarchyNode )selAORTreePath.
                                                        getLastPathComponent();                 
        Object selNode =  selAORMutableTreeNode.getUserObject() ;        
        selAORBean = ( AreaOfResearchTreeNodeBean )selNode ; 
        
        if( !isMovePressed ){                 
            srcTreeNode = selAORMutableTreeNode ;
            srcParentTreeNode = (HierarchyNode)
                                            selAORMutableTreeNode.getParent();                        
        }else if( isMovePressed ){
            
            dstTreeNode = selAORMutableTreeNode;
            trAreaOfResearchLive.setCursor( new Cursor(Cursor.DEFAULT_CURSOR ));            
            isSavedSoFar = false;
            isMovePressed = false;                            
            
            if( srcTreeNode != null && srcParentTreeNode != null &&
                                                        dstTreeNode != null ){         
                //Modified Nodes Data Bean Updates
                AreaOfResearchTreeNodeBean dstNodeDataBean = getModifiedNode(
                    ( AreaOfResearchTreeNodeBean ) dstTreeNode.getUserObject());                
                AreaOfResearchTreeNodeBean srcNodeDataBean = getModifiedNode(
                    ( AreaOfResearchTreeNodeBean ) srcTreeNode.getUserObject());                
                
                Object srcParentObj = srcParentTreeNode.getUserObject();
                AreaOfResearchTreeNodeBean srcParentDataBean = null;
                 
                if( srcParentObj instanceof edu.mit.coeus.irb.bean.AreaOfResearchTreeNodeBean ){
                 srcParentDataBean = getModifiedNode (
                                            ( AreaOfResearchTreeNodeBean ) 
                                            srcParentTreeNode.getUserObject());                    
                }   
                
                if( srcParentDataBean != null && 
                    srcParentDataBean.getNodeID().equals( dstNodeDataBean.
                                                       getNodeID() )){                                                        
                    CoeusOptionPane.showWarningDialog(
                        coeusMessageResources.parseMessageKey(
                                        "Tree_exceptionCode.1004"));                    
                    trAreaOfResearchLive.clearSelection();
                    trAreaOfResearchLive.setCursor( 
                                           new Cursor ( Cursor.DEFAULT_CURSOR));                    
                    return;                    
                }else if( srcParentDataBean == null ||
                          srcNodeDataBean.getNodeID().equals( dstNodeDataBean.
                                                       getParentNodeID() )){                    
                    CoeusOptionPane.showWarningDialog( 
                        coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1005"));
                    
                    trAreaOfResearchLive.clearSelection();
                    trAreaOfResearchLive.setCursor( 
                                           new Cursor ( Cursor.DEFAULT_CURSOR));                    
                    return;
                    
                }else if( srcNodeDataBean.getNodeID().equals( 
                                                  dstNodeDataBean.getNodeID())){
                    
                    CoeusOptionPane.showWarningDialog(
                        coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1006"));                    
                    trAreaOfResearchLive.clearSelection();
                    trAreaOfResearchLive.setCursor( 
                                           new Cursor ( Cursor.DEFAULT_CURSOR));                    
                    return;
                }
                
                DefaultTreeModel model = (DefaultTreeModel) trAreaOfResearchLive.
                                                                    getModel();
                model.removeNodeFromParent( srcTreeNode );
                model.insertNodeInto( srcTreeNode, dstTreeNode, dstTreeNode.
                                                               getChildCount());
                model.reload();
                trAreaOfResearchLive.revalidate();
                trAreaOfResearchLive.expandPath( new TreePath(
                                                       dstTreeNode.getPath()) );                
                trAreaOfResearchLive.setSelectionPath( new TreePath(
                                                       srcTreeNode.getPath()) );                
                
                dstNodeDataBean.setChildrenFlag("Y");                
                srcNodeDataBean.setParentNodeID( dstNodeDataBean.getNodeID() ); 
                if( srcParentTreeNode.isLeaf() ){
                    srcParentDataBean.setChildrenFlag("N");
                }
                
                /*register moved node (source, source-parent and destionation )
                 * node as modified nodes. ( make entry in modified hastable)
                 *it check whether the modified nodes already exists in
                 * either newly added hash or modified hash. Accordingly 
                 *addes to the Updated Hash.
                 */
                if( !isDataBeanInAddHash( srcNodeDataBean ) ){                    
                    updatedAORData.put( srcNodeDataBean.getNodeID(), 
                                                              srcNodeDataBean );
                }                
                if( !isDataBeanInAddHash( srcParentDataBean ) ){
                    
                    updatedAORData.put( srcParentDataBean.getNodeID(), 
                                                            srcParentDataBean );
                }                
                if( !isDataBeanInAddHash( dstNodeDataBean ) ){
                    
                    updatedAORData.put( dstNodeDataBean.getNodeID(), 
                                                              dstNodeDataBean );                    
                }                                                     
                isMovePressed = false;                
            }else{
         
                CoeusOptionPane.showWarningDialog(
                        coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1003"));                
                trAreaOfResearchLive.clearSelection();
                trAreaOfResearchLive.setCursor( 
                                           new Cursor ( Cursor.DEFAULT_CURSOR));                
            }
        }   
    }
    
    /*check the method is in newly added node hash 
     *if exists replaces it with the the new data bean.
     */
    private boolean isDataBeanInAddHash( AreaOfResearchTreeNodeBean newAORNode ){
     
        boolean isInAddedHash = false;
        if( newAORData.containsKey( newAORNode.getNodeID() )){            
           isInAddedHash  = true;
           newAORData.put( newAORNode.getNodeID(), newAORNode );           
        }        
        return isInAddedHash;        
    }
    
    
    //private method to get the modified node list
    private AreaOfResearchTreeNodeBean 
                   getModifiedNode( AreaOfResearchTreeNodeBean newNode ){

        if( updatedAORData.containsKey( newNode.getNodeID() ) ){
            return ((AreaOfResearchTreeNodeBean)updatedAORData.
                                            get( newNode.getNodeID() ));            
        }else if( newAORData.containsKey( newNode.getNodeID() ) ){
            
            return ((AreaOfResearchTreeNodeBean)newAORData.
                                            get( newNode.getNodeID() ));            
        }
        return newNode;
    }
        
        
    //method to support for closing operation for the Area Of Research.
    private void closeAreaOfResearch(){
        
            Hashtable checkMoved = trAreaOfResearchLive.getModifiedNodes();            
            if( !isSavedSoFar ||( checkMoved != null && checkMoved.size() > 0)){
                
                int toSave = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(
                                        "saveConfirmCode.1002"), 
                                CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                                CoeusOptionPane.DEFAULT_YES);
                if( toSave == JOptionPane.YES_OPTION ){

                    saveAllChanges();
                    mdiForm.removeFrame(
                                    CoeusGuiConstants.TITLE_AREA_OF_RESEARCH);
                    this.doDefaultCloseAction();
                    //dispose();                        
                }else if ( toSave == JOptionPane.NO_OPTION ){
                    mdiForm.removeFrame(
                                    CoeusGuiConstants.TITLE_AREA_OF_RESEARCH);
                    isCalledFromClose = true;
                    this.doDefaultCloseAction();                      
                    //dispose();                    
                }                
            }else{
                mdiForm.removeFrame(CoeusGuiConstants.TITLE_AREA_OF_RESEARCH);
                this.doDefaultCloseAction();
            }    
    }
    
    
    /* Method Add's the New Tree by invoking the JDailog, Accepting Inputs
     * Construct the Hierarchy Node and adds to extising Live Tree Model.
     * Here description is provided only during the recursive call/validation
     *call.
     */
    private boolean addNewAreaOfResearch( String desc ){
        
            boolean isAdded = false;            
            if( selAORBean != null ) {
                   
                AreaOfResearchDescriptionForm addForm = 
                              new AreaOfResearchDescriptionForm( mdiForm, 
                                        new AreaOfResearchTreeNodeBean(
                                        null, selAORBean.getNodeID(), false,
                                        desc ), true );                                
                addForm.showDialog( "Add Area Of Research" );
                //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
                if(!addForm.isCancelClik()) {
                // Adding a New Node to the Tree.
                AreaOfResearchTreeNodeBean newAORBean = addForm.
                                                           getNewResearchArea();                
                boolean researchCodeExists = false;
                if(  newAORBean != null && newAORBean.getNodeID() != null ) {
                    
                    researchCodeExists = isAreaOfResearchCodeExists( 
                                                    newAORBean.getNodeID() );
                }
                
                /* Warning Message Window to Alert the User About Using the 
                 * Same Research Area Code ( should be unique across the app. ).
                 */
                if( researchCodeExists ) {
                    
                        CoeusOptionPane.showErrorDialog(                         
                           "Please Enter Different Research Area Code. " +
                           "Research Code \"" + newAORBean.getNodeID() +
                           " \" Already Exists.");                        
                        //recursive to call to set the description.
                        addNewAreaOfResearch( newAORBean.getNodeDescription());                        
                }else if ( ( newAORBean != null ) &&
                           ( newAORBean.getNodeID() != null ) && newAORBean.getResearchAreaCode().trim().length() > 0) {
                    
                    DefaultTreeModel aorTreeModel = ( DefaultTreeModel )
                                                trAreaOfResearchLive.getModel();                    
                    HierarchyNode childNode = new HierarchyNode( newAORBean );
                    aorTreeModel.insertNodeInto( childNode, 
                                    selAORMutableTreeNode, 
                                    selAORMutableTreeNode.getChildCount());                 
                    //puts to hashtale to have register for saving this info.
                    newAORData.put( newAORBean.getNodeID(), newAORBean );
                    
                    /*check the parent exists in add hashtable and 
                     *change its hasChildren flag to true/yes.
                     */
                    resetParentNodeChildFlag( newAORBean.getParentNodeID() );                    
                    selAORTreePath.pathByAddingChild( selAORMutableTreeNode );
                    trAreaOfResearchLive.expandPath( selAORTreePath );
                    isAdded = true;                    
                    String newNodeIDDesc = newAORBean.getNodeID() + " : " + 
                                           newAORBean.getNodeDescription();
                    TreePath newAddedNodePath = findByName(
                             trAreaOfResearchLive, newNodeIDDesc );
                    //Updated to Show the Newly Added Area Of Research NOde
                    //Selected.
                    trAreaOfResearchLive.setSelectionPath( newAddedNodePath );                    
                    setTreeSelectionDetails( newAORBean, newAddedNodePath, 
                                             childNode );                    
                } else {
                        CoeusOptionPane.showWarningDialog(
                                    coeusMessageResources.parseMessageKey(
                                            "areaRsrchBaseWin_exceptionCode.1002"));   
                        //recursive to call to set the description.
                        addNewAreaOfResearch( newAORBean.getNodeDescription());    
                 }                  
              }
            }else{
                
              CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1003"));                
            }            
            return isAdded;            
    }
    
    
    //supporting method to change all the changes made for a specific session.
    private void saveAllChanges(){

            boolean isAddSuccessful = false;
            boolean isModifySuccessful = false;  
            boolean isDeleteSuccessful = false;
            
            try{    
                    if( newAORData.size() > 0 ){                        
                        isAddSuccessful = researchAreaForm.
                                            addModifyAORTreeNodeData( 
                                            newAORData, 'I' );                        
                    }
                    
                    
                    /*append the moved node from the DnDJTree (modified Hash) to
                     *UpdatedAORData Object.
                     */
                    appendMovedTreeNodeEntries();   
                    
                    if( updatedAORData.size() > 0 ){
                    
                        isModifySuccessful = researchAreaForm.
                                             addModifyAORTreeNodeData( 
                                             updatedAORData, 'U' );                    
                    }    
                    if(deletedAORData.size() > 0) {
                        isDeleteSuccessful = researchAreaForm.
                                             addModifyAORTreeNodeData( 
                                             deletedAORData, 'D' ); 
                    }
                    
                    
                    clearAddModifyObject();
                    isSavedSoFar = true;
            }catch( Exception addErr ){             
                  addErr.printStackTrace();                  
            } 
            /** begin: fixed bug with id #147  */
            /*   set the message to be displayed in status bar. RefID:#147 */
            /*setStatusMessage(coeusMessageResources.parseMessageKey(
                "general_saveCode.2275"));*/
            /** end: fixed bug with id #147  */
            refreshAORTree();            
    }
    
    
    //supporting method refresh the tree usually invoked after the save routine.
    private void refreshAORTree(){
        
        try{
                        
            researchAreaForm = new AreaOfResearchHerDetailForm( ROOT_NODE_ID );
            DnDJTree refreshedTree = researchAreaForm.
                                                getAreaOfResearchDisplayTree();            
            trAreaOfResearchLive.setModel( refreshedTree.getModel()  );
            trAreaOfResearchLive.expandRow(0);  
            trAreaOfResearchLive.setSelectionInterval(0,0);  //Updated for default Root Node Selection. 
            
            }catch ( Exception err ){                
                err.printStackTrace();
            }            
    }
    
    
    /*supporting method to set the Parent Flag for those Node which are added
     *during particular user operation. ( those nodes which db entry does'nt 
     * exists )
     */
    private void resetParentNodeChildFlag( String parentCodeID ){
        Hashtable aorMovedData = trAreaOfResearchLive.getModifiedNodes();
        AreaOfResearchTreeNodeBean prNode = null;
        
        if( newAORData.containsKey( parentCodeID ) ){
            prNode = ( AreaOfResearchTreeNodeBean )
                                                 newAORData.get( parentCodeID );
            prNode.setChildrenFlag("y");
            return;
        }else if( aorMovedData!= null && aorMovedData.size() > 0  &&
                  aorMovedData.containsKey( parentCodeID ) ){
            prNode = ( AreaOfResearchTreeNodeBean )
                                              aorMovedData.get( parentCodeID );      
            prNode.setChildrenFlag("y");
            return;
        }
        
        //explicitly changes the parent Node(selected)child status falg to true.
        selAORBean.setChildrenFlag("y");        
        //put the resepctive modified node into the modifed hashtable
        //for save purpose
        updatedAORData.put( selAORBean.getNodeID(), selAORBean );
    }
    
    
    /**
     * This Method is used to Modify the Description of Selected Area Of 
     * Research Node. The Parent Node ID and Modifying Research Area Code
     * is non-editable.
     */
    private boolean modifyAreOfResearchEntry(){
        
        boolean isNodeModified = false;                                        
        if( selAORBean != null && !( selAORBean.getNodeID().equalsIgnoreCase(
                                                               ROOT_NODE_ID ))){                                                                   
             
            AreaOfResearchDescriptionForm modifyForm = 
                                        new AreaOfResearchDescriptionForm( 
                                        mdiForm, selAORBean, false );                
            
            modifyForm.showDialog( "Modify Area Of Research");
            
            // Modify a Specific Area of Research Node in the Tree.
            AreaOfResearchTreeNodeBean modAORBean = modifyForm.
                                                           getNewResearchArea();
            if( modAORBean != null ){                
                DefaultTreeModel aorTreeModel = ( DefaultTreeModel )
                                                trAreaOfResearchLive.getModel();                
                //Modify Tree Node information - Reflected in display Tree
                selAORBean.setRADescription( modAORBean.getNodeDescription() );
                selAORMutableTreeNode.setUserObject( selAORBean );
                aorTreeModel.reload( selAORMutableTreeNode );
                isNodeModified = true;
                
                /*check the modified node do exists (Live) Added nodes,
                 *take that node and modify and put back to the Add nodes hash
                 */
                if( newAORData.containsKey( modAORBean.getNodeID() ) ){
                    AreaOfResearchTreeNodeBean existAORBean = 
                            ( AreaOfResearchTreeNodeBean ) 
                                       newAORData.get( modAORBean.getNodeID() );                    
                    existAORBean.setRADescription( 
                                            modAORBean.getNodeDescription() );                   
                    
                }else {                    
                /*put the resepctive modified node into the modifed hashtable
                 *for save purpose
                 */
                updatedAORData.put( modAORBean.getNodeID(), modAORBean );                
                }                
            }            
            
            trAreaOfResearchLive.expandPath( selAORTreePath );            
            //Updated to Show the Modified Area Of Research NOde
            //Selected.
            trAreaOfResearchLive.setSelectionPath( selAORTreePath );                    
            
         }else if( selAORBean != null && 
                        selAORBean.getNodeID().equalsIgnoreCase( ROOT_NODE_ID )){
             CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1007"));              
         }else{                
                CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1003"));              
         }                                              
        return isNodeModified;        
    }
    
    /*supporting method Add Moved entries in the Hashtable of moved nodes to
     *Modified Hashtable data. ( Each entry contain nodeID/AOR Data Bean
     */
    private void appendMovedTreeNodeEntries(){
        
        Hashtable aorMoveddata = trAreaOfResearchLive.getModifiedNodes();
        java.util.Iterator aorIterator = aorMoveddata.keySet().iterator();
        AreaOfResearchTreeNodeBean updTreeDataBean = null;
        Object key = null;        
        while( aorIterator.hasNext() ){             
            key = aorIterator.next();
            updTreeDataBean = ( AreaOfResearchTreeNodeBean )aorMoveddata.get( 
                                                                        key  );            
            updatedAORData.put( updTreeDataBean.getNodeID(), updTreeDataBean );            
        }        
        trAreaOfResearchLive.clearAllModifiedNodes();        
    }
    
    
    
    /** Check for the new Adding Area Of Research Code Already Exists with
     *  Database or newly added Node Hashtable.
     * @param   newAORID        user Sepcified Area Of Research Code.
     *
     * @return  boolean         True if the ID already exists : False otherwise.
     */
    public boolean isAreaOfResearchCodeExists( String newAORID ){
        
        //CHECK is duplicate from db data
        boolean dbFlag = checkForDuplicate( 
                        researchAreaForm.getAreaOfResearchDBData(), newAORID );         
        //CHECK is duplicate from programtically added new data
        boolean prgFlag = checkForDuplicate( newAORID );        
        return ( ( dbFlag || prgFlag ) ? true : false );       
    }    
    
    
    //check duplicate against DB data
    private boolean checkForDuplicate( Vector resDBData, String nodeID ){        
        boolean isDuplicate = false;
        AreaOfResearchTreeNodeBean tempDataBean = null ;        
        for( int indx = 0; indx < resDBData.size(); indx ++ ){                
            tempDataBean = ( AreaOfResearchTreeNodeBean ) resDBData.get( indx );            
            if( tempDataBean != null && tempDataBean.getNodeID().
                                                   equalsIgnoreCase( nodeID ) ){                
                isDuplicate = true;
                break;                
            }                
        }        
        return isDuplicate;        
    }
    
    
    //check duplicate against programatically added new data
    private boolean checkForDuplicate( String nodeID ){
        
        boolean isDuplicate = false;        
        if( newAORData != null ){            
                Iterator nodes = newAORData.keySet().iterator();
                String key = null;                
                while ( nodes.hasNext() ){                    
                    key = ( String )nodes.next();
                    if( key.equalsIgnoreCase( nodeID ) ){                        
                        isDuplicate = true;        
                        break;
                    }                    
                }               
        }        
        return isDuplicate;        
    }
    
       
   //supporting class to set the selected Node Object references.
    private void setTreeSelectionDetails( AreaOfResearchTreeNodeBean nodeBean,
                                          TreePath selPath,
                                          HierarchyNode selMutableTreeNode ){     
        selAORBean = nodeBean ;
        selAORTreePath = selPath ;
        selAORMutableTreeNode = selMutableTreeNode ;
    }
    
        
    //clear all keys/value entry btween Transaction/Operations and Save Call
    private void clearAddModifyObject(){        
        newAORData.clear();
        updatedAORData.clear();   
        deletedAORData.clear();
        trAreaOfResearchLive.clearAllModifiedNodes();
        trAreaOfResearchLive.clearAllNewNodes();
        setTreeSelectionDetails( null, null, null );
    }
    
    
    /**
     * To get the TreePath from the selected Tree Node.
     * @param tree DnDJTree Instance of complete AOR data
     * @param name Name of the String to be Searched in the tree Nodes.
     *
     * @return TreePath complete path from root to the match found node.
     */
    public TreePath findByName( DnDJTree tree, String name ) {
        
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        TreePath result = find2(tree, new TreePath(root), name);
        return result;
    }


    //supporting method for findByName method - recurcive implementation.
    private TreePath find2(DnDJTree tree, TreePath parent, String nodeName) {
        
        TreeNode node = (TreeNode)parent.getLastPathComponent();        
        if (node != null && node.toString().trim().startsWith(nodeName)) {            
            return parent;            
        }else{

            Object o = node;
            if (node.getChildCount() >= 0) {                 
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {                    
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);                    
                    TreePath result = find2(tree, path, nodeName);
                    if(result!=null)
                        return result;
                    // Found a match
                }
            }
        }        
        // No match at this branch
        return null;
    }
    
    /**
     * This method called from Save Menu Item under File Menu.
     * Saves all the changes and refreshes the tree view.
     */
    public void saveActiveSheet() {
        saveAllChanges();
    }
    
    public void saveAsActiveSheet() {
    }
    
    /* Added by chandra - To check wheter the user  have MAINTAIN_AREAS_OF_RESEARCH
     *right or not. If yes show the toolbar buttons else disable the buttons. - 
     *Modified on 01/16/2003.Call server whether the user has right or not.
     */
    private boolean isUserHasRight() {
        boolean hasOSPRights = false;
        RequesterBean request = new RequesterBean();
        request.setId(MAINTAIN_AREA_OF_RESEARCH);
        request.setDataObject(FN_USER_HAS_RIGHT_IN_ANY_UNIT);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()) {
            if(response.getDataObject() != null){
                Boolean obj = (Boolean) response.getDataObject();
                hasOSPRights = obj.booleanValue();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                hasOSPRights = false ;
            }
        }
        
        return  hasOSPRights ;
        
    }// End of the Right checking method.
    
    /*Added by Chandra Start - This method specifies, the enabling and disabling the 
     *save menu item. If the user has right enable the save menu item else disable it
     */
    private void maintainSaveMenu(boolean hasRight) {
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
//        JMenu fileMenu = mdiForm.getFileMenu().getMenu();
//        fileMenu.getItem(2).setEnabled(hasRight);        
        mdiForm.getFileMenu().setSaveEnabled(hasRight);
    }//Added by Chandra End
    
    
}