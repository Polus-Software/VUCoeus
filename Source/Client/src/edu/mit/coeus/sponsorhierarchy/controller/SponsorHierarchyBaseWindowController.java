/*
 * SponsorHierarchyBaseWindowController.java
 *
 * Created on November 22, 2004, 9:52 AM
 */

package edu.mit.coeus.sponsorhierarchy.controller;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.sponsorhierarchy.gui.*;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.sponsormaint.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.tree.*;
import edu.mit.coeus.utils.treetable.AbstractTreeTableModel;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.budget.gui.StatusWindow;
import edu.mit.coeus.sponsormaint.gui.SponsorFormMaintainance;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Operator;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TreeModelListener;
import java.util.EventObject;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */
public class SponsorHierarchyBaseWindowController extends SponsorHierarchyController 
        implements MouseListener,ActionListener, VetoableChangeListener,TreeSelectionListener, TreeModelListener {
                                    
    
    private MaintainSponsorHierarchyBaseWindow maintainSponsorHierarchyBaseWindow;
    
    private CoeusMessageResources coeusMessageResources;
    
    //holds Frame title
    private String title;
    
    private String hierarchyTitle;
    
    //holds the parent Frame instance
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    //holds the QueryEngine instance
    private QueryEngine queryEngine;
    
    
    private boolean closed = false;
    
    private boolean newGroup = false;
    
    private boolean canClose = true;
    
    private static final char GET_HIERARCHY_DATA = 'O';
    
    private static final char SAVE_HIERARCHY_DATA = 'A';
    
    private static final String EMPTY_STRING = "";
    
    private static final String GET_SERVLET = "/spMntServlet";
    
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    private javax.swing.JTree sponsorHierarchyTree;
    
    private DefaultTreeModel model;
    
    private CoeusVector cvHierarchyData,cvGroupsData,cvSortData,cvSaveData;
    
    //holds current selected node
    private DefaultMutableTreeNode selectedNode;
    
    private GroupsController groupsController;
    
    private DefaultTreeCellEditor editor;
    
    private HierarchyRenderer hierarchyRenderer;
    
    private HierarchyTreeCellEditor hierarchyTreeCellEditor;
    
    //holds current selected node value
    private String selectedValue;
    
    //holds current tree selection path
    private TreePath selTreePath;
    
    //holds fieldNames of hierarchy bean
    private String[] fieldName = {"","levelOne","levelTwo","levelThree","levelFour","levelFive","levelSix","levelSeven","levelEight","levelNine","levelTen"};
    
    DropTarget treeDropTarget;
    
    private JDialog panel;
    
    //holds background color of tree cell renderer
    private java.awt.Color backGroundColor;
   
    private boolean saveRequired,saving;
    
    private SponsorHierarchyBean sponsorHierarchyBean;
    
    //holds change password instance.
    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    private StatusWindow statusWindow;
    
    private String previousNodeValue;
    
    private int rowId;
    
    private ArrayList resChild;
    
    private int addAt = 0;
    //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
    private String printHierParameterValue = "";
    private boolean isPrintingHierarchy = false;
    private boolean saveRequiredToLoadForm = false;
    private static final char HAS_FORM_LOAD_RIGHTS = 'l';
    private static final char DELETE_FORM_DATA = 'd';
    private static final String CANNOT_LOAD_FORMS_IN_THIS_LEVEL = "maintainSponsorHierarchy_form_exceptionCode.1001";
    private static final String CANNOT_RENAME_LEVEL1= "maintainSponsorHierarchy_form_exceptionCode.1002";
    private static final char FORM_EXISTS_IN_GROUP = 'e';
    //Case#2445 - End
    
    
    /** Creates a new instance of SponsorHierarchyBaseWindowController */
    public SponsorHierarchyBaseWindowController(String title, String hierarchyTitle, char functionType) {
        setFunctionType(functionType);
        this.title = title;
        this.hierarchyTitle = hierarchyTitle;
        //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        this.hierarchyName = hierarchyTitle;
        //Case#2445 - end
        queryEngine = QueryEngine.getInstance();
        fetchData();
        setFormData(null);
        
        initComponents();
        registerComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the components.
     */
    private void initComponents() {
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BorderLayout());
        ArrayList hierarchy = buildHierarchy(cvHierarchyData);
        sponsorHierarchyTree = new javax.swing.JTree(processHierarchy(hierarchy));
        hierarchyRenderer = new HierarchyRenderer();
        hierarchyTreeCellEditor = new HierarchyTreeCellEditor();
        editor = new DefaultTreeCellEditor(sponsorHierarchyTree,hierarchyRenderer,hierarchyTreeCellEditor);
        model = (DefaultTreeModel)sponsorHierarchyTree.getModel();
        coeusMessageResources = CoeusMessageResources.getInstance();
        maintainSponsorHierarchyBaseWindow = new MaintainSponsorHierarchyBaseWindow(title, mdiForm);
        //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        if(!isPrintingHierarchy){
            maintainSponsorHierarchyBaseWindow.mnuItmSponsorForm.setEnabled(false);
        }
        //Case#2445 - End
        Container sponsorHierarchyBaseContainer = maintainSponsorHierarchyBaseWindow.getContentPane();
        basePanel.add(sponsorHierarchyTree);
        JScrollPane jScrollPane = new JScrollPane(basePanel);
        backGroundColor = jScrollPane.getBackground();
        sponsorHierarchyBaseContainer.add(jScrollPane);
        sponsorHierarchyTree.setOpaque(false);
        sponsorHierarchyTree.setCellRenderer(hierarchyRenderer);
        sponsorHierarchyTree.setCellEditor(editor);
        sponsorHierarchyTree.setEditable(true);
        sponsorHierarchyTree.collapsePath(new TreePath(model.getRoot()));
        sponsorHierarchyTree.setShowsRootHandles(true);
        sponsorHierarchyTree.putClientProperty("Jtree.lineStyle", "Angled");
        groupsController = new GroupsController(cvGroupsData);
        treeDropTarget = new DropTarget(sponsorHierarchyTree,new TreeDropTarget());
        if(getFunctionType() == VIEW_HIERARCHY) {
            disableComponents();
        }else {
            initPanel();
        }
        sponsorHierarchyTree.setSelectionRow(0);
        selTreePath = sponsorHierarchyTree.getPathForRow(0);
        selectedNode = (DefaultMutableTreeNode)selTreePath.getLastPathComponent();
        //Used to display status when calcualting / Saving / Refreshing.
        statusWindow = new StatusWindow(mdiForm, true);
        sponsorHierarchyTree.getInputMap().put(javax.swing.KeyStroke.getKeyStroke("SPACE"), "none");
//        sponsorHierarchyTree.getInputMap().put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.BUTTON1_MASK,java.awt.event.KeyEvent.SHIFT_MASK), "none");
        sponsorHierarchyTree.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent event) {
                if(event.getKeyCode() == 17) {
                    sponsorHierarchyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
                }
            }
        });
    }
    
    /*
     * Initializes the panel dialogbox
     */
    private void initPanel() {
        panel = new JDialog(mdiForm);
        panel.setTitle("Groups");
        panel.setSize(325, 290);
        panel.getContentPane().add(groupsController.getControlledUI());
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dlgSize = panel.getSize();
        int x = screenSize.width/1 - ((dlgSize.width/1)+20);
        int y = screenSize.height/1 - ((dlgSize.height/1)+60);
        panel.setLocation(x, y);
        panel.setFocusable(false);
        panel.show();
        panel.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        panel.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent event) {
                panel.dispose();
                maintainSponsorHierarchyBaseWindow.mnuItmPanel.setSelected(false);
                maintainSponsorHierarchyBaseWindow.btnPanel.setSelected(false);
            }
        });
    }
    
    /** 
     * This method called when the window opens in display mode. 
     */
    private void disableComponents() {
        maintainSponsorHierarchyBaseWindow.btnChangeGroupName.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.btnCreateNewGroup.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.btnDelete.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.btnMoveDown.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.btnMoveUp.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.btnSave.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.btnPanel.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.btnPanel.setSelected(false);
        maintainSponsorHierarchyBaseWindow.mnuItmChangeGroupName.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.mnuItmCreateNewGroup.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.mnuItmDelete.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.mnuItmMoveDown.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.mnuItmMoveUp.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.mnuItmSave.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.mnuItmPanel.setEnabled(false);
        maintainSponsorHierarchyBaseWindow.mnuItmPanel.setSelected(false);
    }
    
    /** 
     * displays thie internal frame which is controlled by this controller. 
     */
    public void display() {
        try {
            if( mdiForm.getFrame(CoeusGuiConstants.SPONSORHIERARCHY_BASE_WINDOW) == null ){
                mdiForm.putFrame(CoeusGuiConstants.SPONSORHIERARCHY_BASE_WINDOW, maintainSponsorHierarchyBaseWindow);
                mdiForm.getDeskTopPane().add(maintainSponsorHierarchyBaseWindow);
                maintainSponsorHierarchyBaseWindow.setSelected(true);
                maintainSponsorHierarchyBaseWindow.setVisible(true);
            }
        }catch (java.beans.PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /**
     * registers the components with event listeners. 
     */
    public void registerComponents() {
        maintainSponsorHierarchyBaseWindow.addVetoableChangeListener(this);
        maintainSponsorHierarchyBaseWindow.btnChangeGroupName.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnClose.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnCreateNewGroup.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnDelete.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnDetails.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnMoveDown.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnMoveUp.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnSave.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnSearchSponser.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.btnPanel.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmChangeGroupName.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmChangePassword.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmClose.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmCreateNewGroup.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmDetails.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmDelete.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmExit.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmInbox.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmMoveDown.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmMoveUp.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        maintainSponsorHierarchyBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        maintainSponsorHierarchyBaseWindow.mnuItmPreferences.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmSave.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmPanel.addActionListener(this);
        maintainSponsorHierarchyBaseWindow.mnuItmSearchSponser.addActionListener(this);
        //Case 2110 Start
        maintainSponsorHierarchyBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        maintainSponsorHierarchyBaseWindow.mnuItmSponsorForm.addActionListener(this);
        //Case#2445 - End        
        sponsorHierarchyTree.addTreeSelectionListener(this);
        sponsorHierarchyTree.addMouseListener(this);
        model.addTreeModelListener(this);
    }
    
    /** 
     * saves the form data.
     */
    public void saveFormData() {
        if(!saveRequired) return;
        if(cvHierarchyData != null && cvHierarchyData.size()>0) {
        SponsorHierarchyBean sponsorHierarchyBean;
            for(int index=0; index<cvHierarchyData.size(); index++) {
                try {
                    sponsorHierarchyBean = (SponsorHierarchyBean)cvHierarchyData.get(index);
                    if(sponsorHierarchyBean.getAcType() != null) {
                        if(sponsorHierarchyBean.getAcType() == TypeConstants.UPDATE_RECORD) {
                            queryEngine.update(queryKey,sponsorHierarchyBean);
                        }else if(sponsorHierarchyBean.getAcType() == TypeConstants.INSERT_RECORD) {
                            queryEngine.insert(queryKey,sponsorHierarchyBean);
                        }else if(sponsorHierarchyBean.getAcType() == TypeConstants.DELETE_RECORD) {
                            queryEngine.delete(queryKey,sponsorHierarchyBean);
                        }
                    }
                }catch(CoeusException coeusException){
                    coeusException.printStackTrace();
                }
            }
        }
        saveRequired = false;
    }
    
    /** 
     * sets the form data.
     * @param data form data
     */
    public void setFormData(Object data) /*throws edu.mit.coeus.exception.CoeusException*/ {
        try {
            cvHierarchyData = (CoeusVector)queryEngine.getDetails(queryKey,SponsorHierarchyBean.class);
            cvSortData = (CoeusVector)queryEngine.getDetails(queryKey,SponsorHierarchyBean.class);
            cvGroupsData = (CoeusVector)queryEngine.getDetails(queryKey,SponsorMaintenanceFormBean.class);
            rowId = cvHierarchyData.size()+1;
            //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy 
            if(getFunctionType() != CoeusGuiConstants.DISPLAY_MODE){
                try{
                    isPrintingHierarchy = canUserLoadSponsorForms();
                }catch (CoeusUIException coeusUIException) {
                    CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
                    return;
                }
            }
            //Case#2445 - End
      
            }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /*
     * Method to check user can load sponsor forms
     * @return hasFormLoadRight
     * throws CoeusUIException
     */
    private boolean canUserLoadSponsorForms() throws CoeusUIException{
        boolean hasFormLoadRight = false;
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(HAS_FORM_LOAD_RIGHTS);
        requesterBean.setDataObject(hierarchyName);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect,requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse() && responderBean.getDataObject() != null) {
                hasFormLoadRight = ((Boolean)responderBean.getDataObject()).booleanValue();
            }
        }else {
            throw new CoeusUIException(coeusMessageResources.parseMessageKey("coeusApplet_exceptionCode.1147"));
        }
        return hasFormLoadRight;
    }
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /** closes the Base Window and removes the reference from MDIForm.
     * @throws PropertyVetoException PropertyVetoException
     */
     
    public void close() throws PropertyVetoException {
        if(saving) throw new PropertyVetoException(EMPTY_STRING,null);
        if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
            if(saveRequired) {
                int optionSelected = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1258"), 
                                CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_NO);
                if(optionSelected == CoeusOptionPane.SELECTION_YES) {
                    try {
                        //Case 2427 
                        if(findEmptyGroup()){ 
                            showSavingForm();
                        }
                            canClose = false;
                        throw new PropertyVetoException(EMPTY_STRING,null);
                    } catch (CoeusUIException coeusUIException) {
                        throw new PropertyVetoException(EMPTY_STRING,null);
                    }
                }else if(optionSelected == CoeusOptionPane.SELECTION_CANCEL) {
                    throw new PropertyVetoException(EMPTY_STRING,null);
                }
            } 
        }
         mdiForm.removeFrame(CoeusGuiConstants.SPONSORHIERARCHY_BASE_WINDOW);
         queryEngine.removeDataCollection(queryKey);
         cleanUp();
         closed = true; 
    }
    
    public void vetoableChange(java.beans.PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(javax.swing.JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /**
     * cleans up all the instance objects.
     */
    public void cleanUp() {
        if(getFunctionType() != VIEW_HIERARCHY)
            panel.dispose();
        cvGroupsData = null;
        cvHierarchyData = null;
        cvSortData = null;
        sponsorHierarchyBean = null;
        sponsorHierarchyTree = null;
        model = null;
        selectedNode = null;
        selTreePath = null;
        groupsController = null;
        hierarchyRenderer = null;
        hierarchyTreeCellEditor = null;
        treeDropTarget = null;
        panel = null;
        editor = null;
        changePassword = null;
        backGroundColor = null;
        maintainSponsorHierarchyBaseWindow = null;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        blockEvents(true);
        boolean state = true;
        int[] selRows = sponsorHierarchyTree.getSelectionRows();
        try{
        if(source.equals(maintainSponsorHierarchyBaseWindow.btnPanel)) {
            state = maintainSponsorHierarchyBaseWindow.btnPanel.isSelected();
            if(state){
                maintainSponsorHierarchyBaseWindow.btnPanel.setSelected(false);
                maintainSponsorHierarchyBaseWindow.mnuItmPanel.setState(false);
                panel.dispose();
            }else {
                maintainSponsorHierarchyBaseWindow.btnPanel.setSelected(true);
                maintainSponsorHierarchyBaseWindow.mnuItmPanel.setState(true);
                panel.show();
            }
            
        } else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmPanel)) {
                state = maintainSponsorHierarchyBaseWindow.mnuItmPanel.getState();
                maintainSponsorHierarchyBaseWindow.btnPanel.setSelected(state);
                if(state)
                    panel.show();
                else
                    panel.dispose();
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmDelete) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.btnDelete)) {
                        deleteHierarchyData(sponsorHierarchyTree.getSelectionPaths());
                        
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmCreateNewGroup) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.btnCreateNewGroup)) {
                      createNewGroup();  
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmChangeGroupName) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.btnChangeGroupName)) {
                      changeGroupName();  
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmMoveDown) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.btnMoveDown)) {
                      moveDown();
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmMoveUp) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.btnMoveUp)) {
                      moveUp();
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmDetails) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.btnDetails)) {
                      displaySponsor();
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.btnSearchSponser) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.mnuItmSearchSponser)) {
                      searchSponsor();
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.btnSave) ||
                    source.equals(maintainSponsorHierarchyBaseWindow.mnuItmSave)) {
                    try {
                        //Case 2427 
                        if(findEmptyGroup() && saveRequired) {
                            saving = true;
                            showSavingForm();
                        }
                    } catch (CoeusUIException coeusUIException) {
                         coeusUIException.printStackTrace();
                    }
                     
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmInbox)) {
                      showInboxDetails();
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmClose) || 
                        source.equals(maintainSponsorHierarchyBaseWindow.btnClose)) {
                      maintainSponsorHierarchyBaseWindow.doDefaultCloseAction();
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmChangePassword) ) {
                      showChangePassword();
        }else if(source.equals(maintainSponsorHierarchyBaseWindow.mnuItmPreferences)){
                      showPreference();
        //Added for Case#3682 - Enhancements related to Delegations - Start
        }else if(source.equals(maintainSponsorHierarchyBaseWindow.mnuItmDelegations)){
                      displayUserDelegation();                      
        //Added for Case#3682 - Enhancements related to Delegations - End
        }else if (source.equals(maintainSponsorHierarchyBaseWindow.mnuItmExit) ) {
                      exitApplication();
        }//Case 2110 Start
        else if(source.equals(maintainSponsorHierarchyBaseWindow.mnuItmCurrentLocks)){
            showLocksForm();
        }//Case 2110 End
        //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        else if(source.equals(maintainSponsorHierarchyBaseWindow.mnuItmSponsorForm)){
            if(selectedNode != null && selectedNode.getLevel() != 1 && isPrintingHierarchy){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_LOAD_FORMS_IN_THIS_LEVEL));
            }else if(saveRequired){
                int optionSelected = CoeusOptionPane.showQuestionDialog("Do you want to save the changes?",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(optionSelected == CoeusOptionPane.SELECTION_YES) {
                    boolean isEmpty = findEmptyGroup();
                    if(isEmpty){
                        showSavingForm();
                        displaySponsorFormWindow();
                    }
                }
            }else{
                displaySponsorFormWindow();
            }
        }//Case#2445 - End
        else {
              CoeusOptionPane.showInfoDialog("Functionality is not implemented.");
        }
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        blockEvents(false);
    }
    //Added for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
    /*
     * Method to display sponsor form window
     */
    private void displaySponsorFormWindow(){
        if(isPrintingHierarchy && sponsorHierarchyTree.getSelectionPath() != null){
            sponsorHierarchyTree.cancelEditing();
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)sponsorHierarchyTree.getSelectionPath().getLastPathComponent();
            //allows user to load sponsor form in level1 only
            String groupName = (String)selectedNode.getUserObject();
            SponsorFormMaintainance sponsorFormMaintainance = new SponsorFormMaintainance(hierarchyTitle,groupName);
            sponsorFormMaintainance.setFormData();
            sponsorFormMaintainance.display();
        }
    }
    //Case#2445 - End
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha

    
    
    
    /**
     * gets the SponsorHierarchy data from the server
     * @returns void
     */
    private void fetchData() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(hierarchyTitle);
        requesterBean.setFunctionType(GET_HIERARCHY_DATA);
        AppletServletCommunicator conn = new AppletServletCommunicator(connect,requesterBean);
        conn.send();
        ResponderBean responderBean = conn.getResponse();
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                queryKey = hierarchyTitle;
                Hashtable htData = (Hashtable)responderBean.getDataObject();
                extractToQueryEngine(htData);
            }
        }else {
            //Server Error
//            throw new CoeusUIException(responderBean.getMessage(),CoeusUIException.ERROR_MESSAGE);
        }
    }
    
    private void extractToQueryEngine(Hashtable htData) {
        queryEngine.addDataCollection(queryKey, htData);
    }
    
    // displays change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }
    
    /** displays inbox details. */
    private void showInboxDetails() {
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    //Case 2110 Start To get the locks for the logged in user
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }    
    //Case 2110  End
     
    /**
     * Method used to close the application after confirmation.
     */
    public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
                                    "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == CoeusOptionPane.SELECTION_YES) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
    }
    
    /*
     * builds the tree data from the beans.
     * @param cvHierarchyData Tree data
     */
   /*  
    private ArrayList buildHierarchy(CoeusVector cvHierarchyData) {
        ArrayList data = new ArrayList();
        data.add(hierarchyTitle);
        if(cvHierarchyData != null && cvHierarchyData.size() > 0) {
            int index=0;
            while(index < cvHierarchyData.size()) {
                SponsorHierarchyBean sponsorHierarchyBean = (SponsorHierarchyBean)cvHierarchyData.get(index);
                Equals level1 = new Equals("levelOneSortId",new Integer(sponsorHierarchyBean.getLevelOneSortId()));
                CoeusVector cvFilteredData = (CoeusVector)cvHierarchyData.filter(level1);
                index = index + cvFilteredData.size();
                ArrayList childNode,childNode1,childNode2,childNode3,childNode4,childNode5,childNode6,childNode7,childNode8,childNode9 = null;
                ArrayList node = new ArrayList();
                boolean firstTime = false;
                String levelData = null;
                childNode = null;
                String fieldName = "levelOne";
                if(sponsorHierarchyBean.getLevelOne() != null) {
                        childNode = new ArrayList();
                        levelData = sponsorHierarchyBean.getLevelOne();
                        childNode.add(levelData);
                }
//                int i = 0;
//                while(i<cvFilteredData.size()) {
                for(int i=0;  i < cvFilteredData.size(); i++) {
                    sponsorHierarchyBean = (SponsorHierarchyBean)cvFilteredData.get(i);
                    addAt = 0;
                    if(sponsorHierarchyBean.getLevelOne() != null) {
                        if(sponsorHierarchyBean.getLevelTwo() != null) {
                            levelData = sponsorHierarchyBean.getLevelTwo();
                            fieldName="levelTwo";
                            childNode1 = new ArrayList();
                            childNode1.add(levelData);
                            if(!firstTime)
                                childNode.add(childNode1); 
                            if(sponsorHierarchyBean.getLevelThree() != null) {
                                childNode2 = new ArrayList();
                                levelData = sponsorHierarchyBean.getLevelThree();
                                fieldName="levelThree";
                                childNode2.add(levelData);
//                                if(!firstTime)
                                    childNode1.add(childNode2);
                                if(sponsorHierarchyBean.getLevelFour() != null) {
                                    childNode3 = new ArrayList();
                                    levelData = sponsorHierarchyBean.getLevelFour();
                                    fieldName="levelFour";
                                    childNode3.add(levelData);
//                                    if(!firstTime)
                                        childNode2.add(childNode3);
                                    if(sponsorHierarchyBean.getLevelFive() != null) {
                                        childNode4 = new ArrayList();
                                        levelData = sponsorHierarchyBean.getLevelFive();
                                        fieldName="levelFive";
                                        childNode4.add(levelData);
//                                        if(!firstTime)
                                            childNode3.add(childNode4);
                                        if(sponsorHierarchyBean.getLevelSix() != null) {
                                            childNode5 = new ArrayList();
                                            levelData = sponsorHierarchyBean.getLevelSix();
                                            fieldName="levelSix";
                                            childNode5.add(levelData);
//                                            if(!firstTime)
                                                childNode4.add(childNode5);
                                            if(sponsorHierarchyBean.getLevelSeven() != null) {
                                                childNode6 = new ArrayList();
                                                levelData = sponsorHierarchyBean.getLevelSeven();
                                                fieldName="levelSeven";
                                                childNode6.add(levelData);
//                                               if(!firstTime)
                                                    childNode5.add(childNode6);
                                                if(sponsorHierarchyBean.getLevelEight() != null) {
                                                    childNode7 = new ArrayList();
                                                    levelData = sponsorHierarchyBean.getLevelEight();
                                                    fieldName="levelEight";
                                                    childNode7.add(levelData);
//                                                    if(!firstTime)
                                                        childNode6.add(childNode7);
                                                    if(sponsorHierarchyBean.getLevelNine() != null) {
                                                        childNode8 = new ArrayList();
                                                        levelData = sponsorHierarchyBean.getLevelNine();
                                                        fieldName="levelNine";
                                                        childNode8.add(levelData);
//                                                        if(!firstTime)
                                                            childNode7.add(childNode8);
                                                        if(sponsorHierarchyBean.getLevelTen() != null) {
                                                            childNode9 = new ArrayList();
                                                            levelData = sponsorHierarchyBean.getLevelTen();
                                                            childNode9.add(levelData);
                                                            fieldName="levelTen";
                                                            childNode9.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                                            i = i + childNode9.size()-2;
                                                            childNode8.add(childNode9);
                                                        } else {
                                                            childNode8.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                                            i = i + childNode8.size()-2;
                                                            if(firstTime) {
                                                                ArrayList ar=(ArrayList)childNode.get(childNode.size()-1);
                                                                newChildNode(ar,childNode1);
                                                                for (int k=0;k<addAt-1;k++) {
                                                                    if(ar.get(ar.size()-1) instanceof ArrayList)
                                                                    ar = (ArrayList)ar.get(ar.size()-1);
                                                                }
                                                                ar.add(resChild);
                                                            }
                                                        }
                                                    } else {
                                                        childNode7.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                                        i = i + childNode7.size()-2;
                                                        if(firstTime) {
                                                            ArrayList ar=(ArrayList)childNode.get(childNode.size()-1);
                                                            newChildNode(ar,childNode1);
                                                            for (int k=0;k<addAt-1;k++) {
                                                                if(ar.get(ar.size()-1) instanceof ArrayList)
                                                                ar = (ArrayList)ar.get(ar.size()-1);
                                                            }
                                                            ar.add(resChild);
                                                        }
                                                    }
                                                } else {
                                                    childNode6.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                                    i = i + childNode6.size()-2;
                                                    if(firstTime) {
                                                        ArrayList ar=(ArrayList)childNode.get(childNode.size()-1);
                                                        newChildNode(ar,childNode1);
                                                        for (int k=0;k<addAt-1;k++) {
                                                            if(ar.get(ar.size()-1) instanceof ArrayList)
                                                            ar = (ArrayList)ar.get(ar.size()-1);
                                                        }
                                                        ar.add(resChild);
                                                    }
                                                }
                                            } else {
                                                childNode5.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                                i = i + childNode5.size()-2;
                                                if(firstTime) {
                                                    ArrayList ar=(ArrayList)childNode.get(childNode.size()-1);
                                                    newChildNode(ar,childNode1);
                                                    for (int k=0;k<addAt-1;k++) {
                                                        if(ar.get(ar.size()-1) instanceof ArrayList)
                                                            ar = (ArrayList)ar.get(ar.size()-1);
                                                    }
                                                    ar.add(resChild);
                                                }
                                            }
                                        } else {
                                            childNode4.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                            i = i + childNode4.size()-2;
                                            if(firstTime) {
                                                ArrayList ar=(ArrayList)childNode.get(childNode.size()-1);
                                                newChildNode(ar,childNode1);
                                                for (int k=0;k<addAt-1;k++) {
                                                    if(ar.get(ar.size()-1) instanceof ArrayList)
                                                    ar = (ArrayList)ar.get(ar.size()-1);
                                                }
                                            ar.add(resChild);
                                            }
                                        }
                                    } else {
                                        childNode3.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                        i = i + childNode3.size()-2;
                                        if(firstTime) {
                                            ArrayList ar=(ArrayList)childNode.get(childNode.size()-1);
                                            newChildNode(ar,childNode1);
                                            if(ar.get(ar.size()-1) instanceof ArrayList)
                                                ar = (ArrayList)ar.get(ar.size()-1);
                                            else 
                                                ar = (ArrayList)childNode;
                                            ar.add(resChild);
                                        }
                                    }
                                } else {
                                    childNode2.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                    i = i + childNode2.size()-2;
                                    if(firstTime) {
                                        ArrayList ar=(ArrayList)childNode.get(childNode.size()-1);
                                        newChildNode(ar,childNode1);
                                        ar.add(resChild);
                                    }
                                }
                            } else {
                                childNode1.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                                i = i + childNode1.size()-2;
                                if(firstTime)
                                childNode.add(childNode1);
                            }
                        }else {
                            childNode.addAll(getChildNodes(cvFilteredData,levelData,fieldName));
                            i = i + childNode.size()-2;
                        }

                    }     
                    firstTime = true;
                }
                data.add(childNode);
            }
        }
        return data;
    }
    
    private void newChildNode(ArrayList root, ArrayList child) {
        resChild = new ArrayList();
        if(root.get(0).toString().equals(child.get(0).toString())) {
            addAt = addAt+1;
            if(root.size()>1 && child.size()>1) 
            newChildNode((ArrayList)root.get(root.size()-1),(ArrayList)child.get(1));
        }else 
            resChild.addAll(child); 
        //return resChild;
    }*/
    
    //Added for bug fix Start #1830
    /*
     * builds the tree data from the beans.
     * @param cvHierarchyData Tree data
     */    
    private ArrayList buildHierarchy(CoeusVector cvHData) {
        int i=0;
        ArrayList data = new ArrayList();
        data.add(hierarchyTitle);
        while (i<cvHData.size()) {
            SponsorHierarchyBean sponsorHierarchyBean = (SponsorHierarchyBean)cvHierarchyData.get(i);
            Equals level1 = new Equals("levelOneSortId",new Integer(sponsorHierarchyBean.getLevelOneSortId()));
            CoeusVector cvFilteredData = (CoeusVector)cvHierarchyData.filter(level1);
            data.add(getNodeData(cvFilteredData));
            i+=cvFilteredData.size();
        }
        return data;
    }
    private ArrayList getNodeData(CoeusVector cvFilteredData) {
        int i=0;
        Method method;
        Field field;
        Class dataClass;
        String methodName, strField;
        Object value;
        ArrayList equals = null;
        ArrayList parentNode = new ArrayList();
        java.util.List prevSortOrder = null;
        java.util.List currSortOrder = null;
        String[] hierarchyDetails = null;
        And and = null;
        try {
            while(i<cvFilteredData.size()) {
                int fieldIdx = 0;
                and = null;
                equals = new ArrayList();
                hierarchyDetails = new String[10];
                currSortOrder = new ArrayList();
                while(fieldIdx<fieldName.length) {
                    strField = fieldName[fieldIdx+1]+"SortId";
                    SponsorHierarchyBean sponsorHierarchyBean = (SponsorHierarchyBean)cvFilteredData.get(i);
                    dataClass = sponsorHierarchyBean.getClass();
                    methodName = "get" + (strField.charAt(0)+"").toUpperCase()+ strField.substring(1);
                    method = dataClass.getMethod(methodName, null);
                    value = method.invoke(sponsorHierarchyBean, null);
                    if(((Integer)value).intValue() != 0) {
                        currSortOrder.add(value);
                        methodName = "get" + (fieldName[fieldIdx+1].charAt(0)+"").toUpperCase()+ fieldName[fieldIdx+1].substring(1);
                        method = dataClass.getMethod(methodName, null);
                        hierarchyDetails[fieldIdx] = (String)method.invoke(sponsorHierarchyBean, null);
                        equals.add(new Equals(fieldName[fieldIdx+1]+"SortId",(Integer)value));
                        ++fieldIdx;
                    }else {
                        fieldIdx = fieldName.length;
                        if (equals.size() > 1) {
                            for(int idx=0;idx<equals.size();idx++) {
                                if(and==null) {
                                    and = new And((Operator)equals.get(idx),(Operator)equals.get(idx+=1));
                                }else {
                                    and = new And(and,(Operator)equals.get(idx));
                                }
                            }
                        }
                    }
                    
                }
                CoeusVector cvData = (CoeusVector)cvHierarchyData.filter(and==null ? (Operator)equals.get(0) : and);
                if(prevSortOrder == null) {
                    prevSortOrder = currSortOrder;
                    parentNode = getNodeData(cvData, hierarchyDetails,0);
                } else {
                    int index = getOrderedNodeIndex(prevSortOrder, currSortOrder);
                    addNode(parentNode, index).add(getNodeData(cvData, hierarchyDetails,index));
                    if(prevSortOrder.size()<currSortOrder.size() || prevSortOrder.size() == currSortOrder.size()) {
                        prevSortOrder = currSortOrder;
                    }
                }
                i+=cvData.size();
                
                
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return parentNode;
    }
   
    private ArrayList addNode(ArrayList list,int index) {
        ArrayList childData = list;
        for(int idx=0; idx<index-1; idx++) {
            if(childData.get(childData.size()-1) instanceof ArrayList) {
                childData = (ArrayList)childData.get(childData.size()-1);
            }else {
                break;
            }
        }
        return childData;
    }
    
   private int getOrderedNodeIndex(java.util.List prevSortorder, java.util.List currSortOrder) {
       boolean hasMoreData = true;
       int index = 0;
       int prevSortId = 0;
       int currSortId = 0;
       while(hasMoreData) {
           prevSortId = ((Integer)prevSortorder.get(index)).intValue();
           currSortId = ((Integer)currSortOrder.get(index)).intValue();
           if(prevSortId != currSortId) 
               hasMoreData = false;
           else
               index+=1;
       }
       return index;
   }
   
    private ArrayList getNodeData(CoeusVector cvFilteredData,String[] hierarchyDetails, int nodeIdx) {
        ArrayList node = new ArrayList();
        boolean lastnode = false;
        ArrayList list = null;
        for(int index=nodeIdx; index<hierarchyDetails.length; index++) {
            lastnode = false;
            if(hierarchyDetails[index] == null)
                break;
            if(node.size()>0) {
                ArrayList child = new ArrayList();
                child.add(hierarchyDetails[index]);
                list = node;
                while(!lastnode) {
                    if(list.get(list.size()-1) instanceof ArrayList) {
                        list = (ArrayList)list.get(list.size()-1);
                    }else{
                      lastnode = true;
                      list.add(child);
                    }
                }
                
            }else {
                node.add(hierarchyDetails[index]);
            }
        }
        list = list == null ? node : (ArrayList)list.get(list.size()-1);
        for(int index=0;index<cvFilteredData.size();index++) {
            SponsorHierarchyBean bean = (SponsorHierarchyBean)cvFilteredData.get(index);
            list.add((String)bean.getSponsorCode()+ ":" + bean.getSponsorName());
        }
        return node;
    }
    private SponsorHierarchyBean flushBean(SponsorHierarchyBean delBean) {
        delBean.setLevelOne(null);
        delBean.setLevelTwo(null);
        delBean.setLevelThree(null);
        delBean.setLevelFour(null);
        delBean.setLevelFive(null);
        delBean.setLevelSix(null);
        delBean.setLevelSeven(null);
        delBean.setLevelEight(null);
        delBean.setLevelNine(null);
        delBean.setLevelTen(null);
        delBean.setLevelOneSortId(0);
        delBean.setLevelTwoSortId(0);
        delBean.setLevelThreeSortId(0);
        delBean.setLevelFourSortId(0);
        delBean.setLevelFiveSortId(0);
        delBean.setLevelSixSortId(0);
        delBean.setLevelSevenSortId(0);
        delBean.setLevelEightSortId(0);
        delBean.setLevelNineSortId(0);
        delBean.setLevelTenSortId(0);
        return delBean;
    }
    
    private CoeusVector getDeleteData(CoeusVector cvDelData) {
        for(int index=0; index<cvDelData.size(); index++) {
            SponsorHierarchyBean bean = (SponsorHierarchyBean)cvDelData.get(index);
            if(TypeConstants.DELETE_RECORD.equals(bean.getAcType()))
                cvDelData.remove(index);
        }
        return cvDelData;
    }
    //Bug fix End #1830 
    
    /**
     * This methods loads the search window and displays 
     * with the search sponsor details.
     *
     */
    private void searchSponsor() {
        String sponsorCode=null;
        try {
            CoeusSearch coeusSearch =
                new CoeusSearch(mdiForm, "SPONSORSEARCH",
            CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
            coeusSearch.showSearchWindow();
            javax.swing.JTable tblResultsTable = coeusSearch.getSearchResTable();
            if(tblResultsTable == null) return;
            int row = tblResultsTable.getSelectedRow();
            if(row!=-1){
                    sponsorCode = (String)tblResultsTable.getValueAt(row, 0);
                    sponsorCode = sponsorCode+":"+(String)tblResultsTable.getValueAt(row, 1);
            }else{
                    CoeusOptionPane.showErrorDialog("Please select a Sponsor");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        int startRow = 0;
        TreePath root = new TreePath(sponsorHierarchyTree.getModel().getRoot());
        // Find the path (regardless of visibility) that matches the
        // specified sequence of names
        TreePath path = findByName(sponsorHierarchyTree, sponsorCode);
        if(path == null) {
            CoeusOptionPane.showInfoDialog("The sponsor:"+sponsorCode.substring(0,sponsorCode.indexOf(":"))+" is not found in this hierarchy.");
            sponsorHierarchyTree.collapsePath(root);
            sponsorHierarchyTree.setSelectionPath(root);
            return;
        }
        sponsorHierarchyTree.expandPath(root);
        sponsorHierarchyTree.expandPath(path);
        sponsorHierarchyTree.setSelectionPath(path);
        sponsorHierarchyTree.scrollRowToVisible(sponsorHierarchyTree.getRowForPath(path));
    }
    
    /** Finds the path in tree as specified by the array of names.
     * The names array is a sequence of names where names[0]
     * is the root and names[i] is a child of names[i-1].
     * Comparison is done using String.equals().
     * Returns null if not found.
     * @param tree JTree instance
     * @param name name to the searched/find
     * @return  TreePath found for the specific name
     */
    public TreePath findByName(JTree tree, String name) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        TreePath result = find2(tree, new TreePath(root), name.trim());
        return result;
    }

    private TreePath find2(JTree tree, TreePath parent, String nodeName) {
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
    
    /*
     * This method returns child nodes for a given  level and levelValue.
     */
    private CoeusVector getChildNodes(CoeusVector cvData, String levelData, String fieldName) {
        CoeusVector cvTemp = new CoeusVector();
        Equals levelFilter = new Equals(fieldName,levelData);
        CoeusVector cvFilteredData = cvData.filter(levelFilter);
        
        for (int index = 0; index < cvFilteredData.size(); index++) {
            SponsorHierarchyBean sponsorHierarchyBean = (SponsorHierarchyBean)cvFilteredData.get(index);
            cvTemp.add((String)sponsorHierarchyBean.getSponsorCode()+ ":" + sponsorHierarchyBean.getSponsorName());
        }
        return cvTemp;
    }
    
    /** Its a routine that will make node out of the first entry
     *  in the array, then make nodes out of subsequent entries
     *  and make them child nodes of the first one. The process is
     *  repeated recursively for entries that are arrays.
     */
    private HierarchyNode processHierarchy(ArrayList hierarchy) {
        HierarchyNode node = new HierarchyNode((Object)hierarchy.get(0));
        HierarchyNode child;
        for(int i=1; i<hierarchy.size(); i++) {
          Object nodeSpecifier = hierarchy.get(i);
          if (nodeSpecifier instanceof ArrayList) { // Ie node with children
            child = processHierarchy((ArrayList)nodeSpecifier);
            child.setAllowsChildren(true);
          }
          else {
            child = new HierarchyNode(nodeSpecifier); // Ie Leaf
            child.setAllowsChildren(false);
          }
          node.add(child);
        }
        return(node);
      }
    /*
     * this method deletes the selected nodes from the hierarchy.
     */
    private void deleteHierarchyData(TreePath[] selRows) {
        SponsorHierarchyBean delBean;
        CoeusVector delData = new CoeusVector();
        TreePath treePath=selRows[selRows.length-1];
        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
        int selRow=sponsorHierarchyTree.getRowForPath(treePath);
        String optionPaneMsg=EMPTY_STRING;
        if(selNode.isRoot()) return;
        if(!selNode.getAllowsChildren()) {
            if(selRows.length > 1) {
                optionPaneMsg = coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1252");
            }else {
                optionPaneMsg = "Do you want to remove the sponsor "+selNode.toString()+" from the hierarchy?";
            }
                int optionSelected = CoeusOptionPane.showQuestionDialog(optionPaneMsg,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(optionSelected == CoeusOptionPane.SELECTION_YES)  {
                    for (int index=selRows.length-1; index>=0; index--) {
                        treePath = selRows[index];
                        selRow = sponsorHierarchyTree.getRowForPath(treePath);
                        selNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
                        int endIndex = selNode.toString().indexOf(":");
                        Equals getRowData = new Equals("sponsorCode",selNode.toString().substring(0,endIndex == -1 ? selNode.toString().length():endIndex));
                        CoeusVector cvFilteredData = getDeleteData(cvHierarchyData.filter(getRowData));
                        delBean = (SponsorHierarchyBean)cvFilteredData.get(0);
                        delBean.setAcType(TypeConstants.DELETE_RECORD);
                        flushBean(delBean);//Added for bug fix Start #1830
                        model.removeNodeFromParent(selNode);
                        delData.addAll(cvFilteredData);
                        saveRequired = true;
                    }
                }
        } else {
            
//                int optionSelected = CoeusOptionPane.showQuestionDialog("Do you want to remove the Group "+selNode.toString()+" all its children from the hierarchy.",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            int optionSelected = -1;
            try{
                if(selNode != null && selNode.getLevel() == 1 && isPrintingHierarchy && isFormsExistInGroup(selNode.toString())){
                    optionSelected = CoeusOptionPane.showQuestionDialog("Do you want to remove the group "+selNode.toString()+" , all its children and associated sponsor forms from the hierarchy?",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                }else{
                    optionSelected = CoeusOptionPane.showQuestionDialog("Do you want to remove the Group "+selNode.toString()+" , all its children from the hierarchy.",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                }
            }catch(CoeusUIException coeusUIException){
                CoeusOptionPane.showDialog(coeusUIException);
                return ;
            }
            if(optionSelected == CoeusOptionPane.SELECTION_YES) {
                //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
                if(isPrintingHierarchy && selectedNode.getLevel() == 1){
                    TreeNode root = (TreeNode)sponsorHierarchyTree.getModel().getRoot();
                    TreePath result = findEmptyGroup(sponsorHierarchyTree, new TreePath(root));
                    if( result == null){
                        try{
                            deleteForms(selNode.toString());
                        }catch(CoeusUIException coeusUIException){
                            CoeusOptionPane.showDialog(coeusUIException);
                            return ;
                        }
                    }
                }
                Equals getRowData = new Equals(fieldName[selNode.getLevel()],selNode.toString());
                CoeusVector cvFilteredData = getDeleteData(cvHierarchyData.filter(getRowData));//Modified for bug fix Start #1830
                model.removeNodeFromParent(selNode);
                for (int i=0; i<cvFilteredData.size(); i++) {
                    delBean = (SponsorHierarchyBean)cvFilteredData.get(i);
                    delBean.setAcType(TypeConstants.DELETE_RECORD);
                    flushBean(delBean);//Added for bug fix Start #1830
                    saveRequired = true;
                }
                delData.addAll(cvFilteredData);
            }
        }
        if(selRow < sponsorHierarchyTree.getRowCount() && 
            ((DefaultMutableTreeNode)sponsorHierarchyTree.getPathForRow(selRow).getLastPathComponent()).getAllowsChildren() && !selNode.getAllowsChildren())
            sponsorHierarchyTree.setSelectionPath(treePath.getParentPath());
        else
            if(selRow >= sponsorHierarchyTree.getRowCount())
                selRow = 0;
            sponsorHierarchyTree.setSelectionRow(selRow);
        
        groupsController.refreshTableData(delData);
    }
    
    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
    /*
     * Calls the servlet to delete all the all forms and templates
     * @param groupName
     * throws CoeusUIException
     */
    private void deleteForms(String groupName)throws CoeusUIException{
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(DELETE_FORM_DATA);
        requesterBean.setDataObject(groupName);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect,requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(responderBean == null) {
            throw new CoeusUIException(coeusMessageResources.parseMessageKey("coeusApplet_exceptionCode.1147"));
        }
    }
    
    /*
     * Method to check sponsor forms exists for the group
     * @param groupName
     * @return isFormExistsInGroup, True - Sponsor forms exists for group
     * throws CoeusUIException
     */
    private boolean isFormsExistInGroup(String groupName)throws CoeusUIException{
        RequesterBean requesterBean  = new RequesterBean();
        boolean isFormExistsInGroup = false;
        requesterBean.setFunctionType(FORM_EXISTS_IN_GROUP);
        requesterBean.setDataObject(groupName);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect,requesterBean);
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder != null){
            if(responder.isSuccessfulResponse() && responder.getDataObject() != null){
                isFormExistsInGroup = ((Boolean)responder.getDataObject()).booleanValue();
            }
        }else {
            throw new CoeusUIException(coeusMessageResources.parseMessageKey("coeusApplet_exceptionCode.1147"));
        }
        return isFormExistsInGroup;
    }
    //Case#2445 - End
    
    /*
     *the code below is for differentiatin between sponsors nodes and Groups 
     */
    private class HierarchyRenderer extends DefaultTreeCellRenderer {
        ImageIcon folderNode,root,sponsor;
        
        public HierarchyRenderer() {
        folderNode  =  new ImageIcon(getClass().
                                   getClassLoader().getResource("images/folderNode.gif"));
        root  =  new ImageIcon(getClass().
                                   getClassLoader().getResource("images/hierarchyRoot.gif"));
        sponsor  =  new ImageIcon(getClass().
                                   getClassLoader().getResource("images/sponsor.gif"));
        }

        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

	    
            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)value;
            setBackgroundNonSelectionColor(backGroundColor);
            if(selNode.getAllowsChildren() && !selNode.isRoot()) {
                setIcon(folderNode);
                setOpenIcon(folderNode);
                setClosedIcon(folderNode);
                 setLeafIcon(folderNode);
            }else if(!selNode.getAllowsChildren()) {
                setIcon(sponsor);
                setLeafIcon(sponsor);
            }else
                setIcon(root);
            setComponentOrientation(tree.getComponentOrientation());
	    return this;
        }
        
    }
    
    /*
     * Creates a new group in the selected tree path.
     */
    private void createNewGroup() {
        if(selectedNode != null) { 
            DefaultMutableTreeNode nextNode = (DefaultMutableTreeNode)selectedNode.getNextNode();
            if(selectedNode.isLeaf() && !selectedNode.getAllowsChildren()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1254"));
                return;
            } else if(selTreePath.getPathCount() == 10) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1255"));
                return;
            }else if (nextNode != null && nextNode.isLeaf()) {
                CoeusOptionPane.showInfoDialog("The group '"+selectedNode.toString()+"' has sponsors assigned to it. \nCannot create subgroups for this group.");
                return;
            }else {
                nextNode = new DefaultMutableTreeNode("New Group - "+(selectedNode.getLevel()+1)+"."+selectedNode.getChildCount(),true);
                model.insertNodeInto(nextNode, selectedNode, selectedNode.getChildCount());
                TreePath newSelectionPath = selTreePath.pathByAddingChild(nextNode);
                sponsorHierarchyTree.clearSelection();
                sponsorHierarchyTree.addSelectionPath(newSelectionPath);
                sponsorHierarchyTree.startEditingAtPath(newSelectionPath);
                newGroup = true;
                saveRequired = true;
            }
        }
    }
    
    /*
     * this mothod checks for duplicated level names 
     * If found returns true
     * else returns false.
     */
    private boolean isDuplicated(TreeNode selTreeNode) {
            TreePath duplicatedPath = findByName(sponsorHierarchyTree, selTreeNode.toString());
            int level = selectedNode.getLevel();
            if(duplicatedPath != null && selectedNode.getLevel()==((DefaultMutableTreeNode)duplicatedPath.getLastPathComponent()).getLevel() && 
                       selTreeNode.toString().equals(duplicatedPath.getLastPathComponent().toString()) && sponsorHierarchyTree.getRowForPath(duplicatedPath) != sponsorHierarchyTree.getRowForPath(selTreePath) ) {
                CoeusOptionPane.showInfoDialog("A group with name '"+selTreeNode.toString()+"' already exists for level "+(level+1));
                return true;
            }
            return false;
    }
    
    /*
     * this method fires when ever selection changes.
     */
    public void valueChanged(javax.swing.event.TreeSelectionEvent treeSelectionEvent) {
        //Addded for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        if(sponsorHierarchyTree.isEditing() && isPrintingHierarchy){
            saveRequiredToLoadForm = true;
        }
        //Case#2445 - End
        selTreePath = treeSelectionEvent.getNewLeadSelectionPath();
        if(selTreePath == null)
            return;
        selectedNode = (DefaultMutableTreeNode)selTreePath.getLastPathComponent();
        
//        if(EMPTY_STRING.equals(treeSelectionEvent.getOldLeadSelectionPath().getLastPathComponent().toString())) {
//           CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1257"));
////           sponsorHierarchyTree.addSelectionPath(treeSelectionEvent.getOldLeadSelectionPath());
//           sponsorHierarchyTree.startEditingAtPath(treeSelectionEvent.getOldLeadSelectionPath());
//        }
        if(sponsorHierarchyTree.getSelectionPaths().length >1) {
            String s = treeSelectionEvent.getOldLeadSelectionPath().getLastPathComponent().toString();
            if(((DefaultMutableTreeNode)treeSelectionEvent.getOldLeadSelectionPath().getLastPathComponent()).getAllowsChildren()) {
                 sponsorHierarchyTree.getSelectionModel().removeSelectionPath(treeSelectionEvent.getOldLeadSelectionPath());
                 sponsorHierarchyTree.getSelectionModel().addSelectionPath(selTreePath);
             }
            TreePath[] path  = sponsorHierarchyTree.getSelectionPaths();
            int count = 0;
            for(int selPathIndex=0; selPathIndex<path.length;selPathIndex++) {
                if(((DefaultMutableTreeNode)path[selPathIndex].getLastPathComponent()).getAllowsChildren()){
                    count++;
                    if(count>1)
                        sponsorHierarchyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                }
            }
        }
        selectedValue = (String)selectedNode.getUserObject();
    }
    
    /*
     * this method makes a cell editable.
     */
    private void changeGroupName() {
        //Added for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy  - Start
        //To check sponsor form is exists for the group,exists - group name is made non-editable
        boolean isFormExist = false;
        try{
            TreePath existTreepath = findEmptyGroup(sponsorHierarchyTree, sponsorHierarchyTree.getSelectionPath());
            if( existTreepath == null){
                isFormExist = isFormsExistInGroup(selectedNode.toString());
            }
        }catch(CoeusUIException coeusUIException){
            CoeusOptionPane.showDialog(coeusUIException);
            return ;
        }
        if(isPrintingHierarchy && selectedNode.getLevel() == 1 && isFormExist){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_RENAME_LEVEL1));
            
        }else {//CAse#2445 - End
            if(!selectedNode.getAllowsChildren() || selectedNode.isRoot() ) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1251"));
                return;
            }
            sponsorHierarchyTree.startEditingAtPath(selTreePath);
        }
    }

    /*
     * this function moves a group down.
     */
    private void moveDown() {
        if(selectedNode != null && !selectedNode.isRoot()) {
            if(selectedNode != null && !selectedNode.isLeaf() && !selectedNode.isRoot()) {
                TreePath path = selTreePath;
                int i = selectedNode.getParent().getChildCount()-1;
                MutableTreeNode parent = (MutableTreeNode)selectedNode.getParent();
                int insertIndex = selectedNode.getParent().getIndex(selectedNode);
                if(insertIndex < i) {
                    changeSortOrder((selTreePath.getPathCount())-1,selectedValue,insertIndex+2);
                    changeSortOrder((selTreePath.getPathCount())-1,selectedNode.getParent().getChildAt(insertIndex+1).toString(),insertIndex+1);
                    model.removeNodeFromParent((MutableTreeNode)selectedNode);
                    model.insertNodeInto((MutableTreeNode)selectedNode, parent, (insertIndex+1));
                    sponsorHierarchyTree.setSelectionPath(path);
                    saveRequired = true;
                }
            } else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1253"));
            }
        }
    }

    /*
     * this function moves a group up.
     */
    private void moveUp() {
        if(selectedNode != null && !selectedNode.isRoot()) {
            if(!selectedNode.isLeaf()) {
                TreePath path = selTreePath;
                int i = selectedNode.getParent().getChildCount();
                MutableTreeNode parent = (MutableTreeNode)selectedNode.getParent();
                int insertIndex = selectedNode.getParent().getIndex(selectedNode);
                if(insertIndex < i && insertIndex > 0) {
                    changeSortOrder((selTreePath.getPathCount())-1,selectedValue,insertIndex);
                    changeSortOrder((selTreePath.getPathCount())-1,selectedNode.getParent().getChildAt(insertIndex-1).toString(),insertIndex+1);
                    model.removeNodeFromParent((MutableTreeNode)selectedNode);
                    model.insertNodeInto((MutableTreeNode)selectedNode, parent, (insertIndex-1));
                    sponsorHierarchyTree.setSelectionPath(path);
                    saveRequired = true;
                }
            }else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1253"));
            }
        }
    }

    /*
     * changes the sorting order for a group when it moves up or down.
     */
    private void changeSortOrder(int sortField, String value,int sortId) {
        Equals levelFilter = new Equals(fieldName[sortField], value);
        CoeusVector cvFilteredData = (CoeusVector)cvHierarchyData.filter(levelFilter);
        for (int index=0; index < cvFilteredData.size(); index++) {
            sponsorHierarchyBean = (SponsorHierarchyBean)cvFilteredData.get(index);
            //Modified Case #1830 Begin
            if(sponsorHierarchyBean.getAcType() == null)
                sponsorHierarchyBean.setAcType(TypeConstants.UPDATE_RECORD);
            //Modified Case #1830 End
            switch(sortField) {
                case 1:
                    sponsorHierarchyBean.setLevelOneSortId(sortId);
                    break;
                case 2:
                    sponsorHierarchyBean.setLevelTwoSortId(sortId);
                    break;
                case 3:
                    sponsorHierarchyBean.setLevelThreeSortId(sortId);
                    break;
                case 4:
                    sponsorHierarchyBean.setLevelFourSortId(sortId);
                    break;
                case 5:
                    sponsorHierarchyBean.setLevelFiveSortId(sortId);
                    break;
                case 6:
                    sponsorHierarchyBean.setLevelSixSortId(sortId);
                    break;
                case 7:
                    sponsorHierarchyBean.setLevelSevenSortId(sortId);
                    break;
                case 8:
                    sponsorHierarchyBean.setLevelEightSortId(sortId);
                    break;
                case 9:
                    sponsorHierarchyBean.setLevelNineSortId(sortId);
                    break;
                case 10:
                    sponsorHierarchyBean.setLevelTenSortId(sortId);
                    break;
            }
        }
    }

    /*
     * updates the a group name, when modified.
     */
    private void updateGroup(String newGroupName, int level) {
        Equals levelFilter = new Equals(fieldName[level], previousNodeValue);
        CoeusVector cvFilteredData = (CoeusVector)cvHierarchyData.filter(levelFilter);
        if(cvFilteredData != null && cvFilteredData.size()>0) {
            for (int index=0; index < cvFilteredData.size(); index++) {
                sponsorHierarchyBean = (SponsorHierarchyBean)cvFilteredData.get(index);
                sponsorHierarchyBean.setAcType(TypeConstants.UPDATE_RECORD);
                switch(level) {
                    case 1:
                        sponsorHierarchyBean.setLevelOne(newGroupName);
                        break;
                    case 2:
                        sponsorHierarchyBean.setLevelTwo(newGroupName);
                        break;
                    case 3:
                        sponsorHierarchyBean.setLevelThree(newGroupName);
                        break;
                    case 4:
                        sponsorHierarchyBean.setLevelFour(newGroupName);
                        break;
                    case 5:
                        sponsorHierarchyBean.setLevelFive(newGroupName);
                        break;
                    case 6:
                        sponsorHierarchyBean.setLevelSix(newGroupName);
                        break;
                    case 7:
                        sponsorHierarchyBean.setLevelSeven(newGroupName);
                        break;
                    case 8:
                        sponsorHierarchyBean.setLevelEight(newGroupName);
                        break;
                    case 9:
                        sponsorHierarchyBean.setLevelNine(newGroupName);
                        break;
                    case 10:
                        sponsorHierarchyBean.setLevelTen(newGroupName);
                        break;
                }
            }
        }
    }
    
    private void showSavingForm() throws CoeusUIException {
        statusWindow.setHeader("Saving New Settings...");
        statusWindow.setFooter("Please Wait...");
        statusWindow.display();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                
                try {
                    blockEvents(true);
                    saveFormData();
                    saveHierarchy();
                }catch (CoeusUIException coeusUIException) {
                    saving = false;
                    statusWindow.setVisible(false);
                    saveRequired = true;
                    CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
                    blockEvents(false);
                    return;
                }
                statusWindow.setVisible(false);
                if(!canClose) {
                    canClose = true;
                    maintainSponsorHierarchyBaseWindow.doDefaultCloseAction();
                }
                saving = false;
            }
        });
        thread.start();
    }
    
    private boolean saveRequired() {
        Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
        try {
            cvSaveData = queryEngine.executeQuery(queryKey, SponsorHierarchyBean.class, insertOrUpdateOrDelete);
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        if(cvSaveData.size() > 0)
            return true;
        return false;
    }
  
    /** sends Sponsor data to server to be saved.
      * @throws CoeusUIException if any exception occurs.
      */
    private void saveHierarchy() throws CoeusUIException {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE )
            return;
        if(!saveRequired()) return;
//            CoeusVector cvData = queryEngine.getDetails(queryKey,SponsorHierarchyBean.class);
            
            cvSaveData.sort("acType");    
            RequesterBean requesterBean  = new RequesterBean();
            requesterBean.setFunctionType(SAVE_HIERARCHY_DATA);
            requesterBean.setDataObject(cvSaveData);
            AppletServletCommunicator comm = new AppletServletCommunicator(connect,requesterBean);
            comm.send();
            ResponderBean responderBean = comm.getResponse();
            if(responderBean != null) {
                if(!responderBean.isSuccessfulResponse()) {
                    CoeusOptionPane.showErrorDialog(responderBean.getMessage());
                    throw new CoeusUIException(EMPTY_STRING);
                } else {
                    Hashtable htData = new Hashtable();
                    htData.put(SponsorHierarchyBean.class, responderBean.getDataObjects()==null ? new CoeusVector() : responderBean.getDataObjects());
                    htData.put(SponsorMaintenanceFormBean.class, cvGroupsData);
                    extractToQueryEngine(htData);
                    setFormData(null);
                    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
                    saveRequiredToLoadForm = false;
                    //Case#2445 - End
                }
            }else {
                throw new CoeusUIException("Could not find the server.");
            }
    }

    public void treeNodesChanged(javax.swing.event.TreeModelEvent treeModelEvent) {
       DefaultMutableTreeNode modifiedNode = (DefaultMutableTreeNode)sponsorHierarchyTree.getSelectionPath().getLastPathComponent();
       if(isDuplicated((TreeNode)modifiedNode)) {
           sponsorHierarchyTree.getCellEditor().stopCellEditing();
           sponsorHierarchyTree.startEditingAtPath(treeModelEvent.getTreePath().pathByAddingChild(modifiedNode));
           return;
       }
//       else if(EMPTY_STRING.equals(modifiedNode.toString())) {
//           CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1257"));
//           sponsorHierarchyTree.startEditingAtPath(treeModelEvent.getTreePath().pathByAddingChild(selectedNode));
//       }
       if(!newGroup) {
           updateGroup((String)modifiedNode.getUserObject(),modifiedNode.getLevel());
           saveRequired = true;
       }
       
       newGroup = false;
    }

    public void treeNodesInserted(javax.swing.event.TreeModelEvent e) {
    }

    public void treeNodesRemoved(javax.swing.event.TreeModelEvent e) {
    }

    public void treeStructureChanged(javax.swing.event.TreeModelEvent e) {
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() != 2) return;
        if(selectedNode.isLeaf()) {
            java.awt.Point o = mouseEvent.getPoint();
            java.awt.Rectangle t = sponsorHierarchyTree.getPathBounds(selTreePath);
            if(t.contains(o))
                displaySponsor();
            else
                return;
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    /*
     * displays the details of a selected sponsor.
     */
    private void displaySponsor() {
        if(selectedNode != null && !selectedNode.getAllowsChildren()) {
            SponsorMaintenanceForm sponsorForm = new SponsorMaintenanceForm(TypeConstants.DISPLAY_MODE, selectedValue.substring(0,selectedValue.indexOf(":")));
            sponsorForm.showForm(mdiForm, "Display Sponsor", true);
        }
    }

 public class HierarchyTreeCellEditor implements TreeCellEditor {
      HierarchyTreeEditor nodeEditor;
      javax.swing.CellEditor currentEditor;
      TreeNode value;
      public HierarchyTreeCellEditor() {

            nodeEditor = new HierarchyTreeEditor();

      }

        public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                    boolean isSelected,
                                                    boolean expanded,
                                                    boolean leaf, int row) {
              
              nodeEditor.requestFocusInWindow();
              previousNodeValue = value.toString();
              nodeEditor.selectAll();
              currentEditor = nodeEditor;
              nodeEditor.setText(value.toString());
              this.value = (DefaultMutableTreeNode)value;
              return (Component)currentEditor;
        }

        public Object getCellEditorValue() {
            if(EMPTY_STRING.equals(nodeEditor.getText().trim())) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1257"));
                nodeEditor.setText(selectedValue);
            }
            return nodeEditor.getText().trim();
        }
        public boolean isCellEditable(EventObject event) {
            if(selectedNode != null && getFunctionType() != VIEW_HIERARCHY  && !selectedNode.isRoot()) {
                //Added for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
                TreePath existTreepath = findEmptyGroup(sponsorHierarchyTree, sponsorHierarchyTree.getSelectionPath());
                if( existTreepath == null){
                    if(isPrintingHierarchy && selectedNode.getLevel() == 1){
                        boolean isFormExist = false;
                        try{
                            isFormExist = isFormsExistInGroup(selectedNode.toString());
                            if(!isFormExist){
                                return true;
                            }
                        }catch(CoeusUIException coeusUIException){
                            CoeusOptionPane.showDialog(coeusUIException);
                        }
                        return false;
                    }else{
                        return true;
                    }
                }//Case#2445 - end
                else if(selectedNode.getAllowsChildren()) {
                    return true;
                }
            }
            return false;
        }

        public boolean shouldSelectCell(EventObject event) {
          return currentEditor.shouldSelectCell(event);
        }

        public boolean stopCellEditing() {
            if(nodeEditor.getText() == null || nodeEditor.getText().equals(""))
                return false;
            return currentEditor.stopCellEditing();
        }

        public void cancelCellEditing() {
            //Modified for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
            boolean isDuplicated = isDuplicated((TreeNode)new DefaultMutableTreeNode(nodeEditor.getText()));
            if(isDuplicated && isPrintingHierarchy){
                DefaultMutableTreeNode modifiedNode = (DefaultMutableTreeNode)new DefaultMutableTreeNode(nodeEditor.getText());
                if(modifiedNode != null && selTreePath != null && selectedNode.getLevel() == 1)  {
                    TreePath currentTreePath = sponsorHierarchyTree.getPathForRow(0);
                    sponsorHierarchyTree.getCellEditor().stopCellEditing();
                    int row = sponsorHierarchyTree.getLeadSelectionRow();
                    sponsorHierarchyTree.setSelectionRow(row);
                    sponsorHierarchyTree.startEditingAtPath(currentTreePath.pathByAddingChild(modifiedNode));
                    return;
                }
            }
           //Case#2445 - End
            if(!nodeEditor.getText().equals(selectedNode.toString())) {
                selectedNode.setUserObject(nodeEditor.getText());
                updateGroup((String)selectedNode.getUserObject(),selectedNode.getLevel());
                saveRequired = true;
                return;
            }
          currentEditor.cancelCellEditing();
        }

        public void addCellEditorListener(CellEditorListener l) {
          nodeEditor.addCellEditorListener(l);
        }

        public void removeCellEditorListener(CellEditorListener l) {
          nodeEditor.removeCellEditorListener(l);
        }
 }
 
 public class TreeDropTarget implements DropTargetListener {

  JTree targetTree;

  /*
   * Drop Event Handlers
   */
  
      public void dragEnter(DropTargetDragEvent dtde) {
        
      }

      public void dragOver(DropTargetDragEvent dtde) {
         Point loc = dtde.getLocation();
         DropTargetContext dtc = dtde.getDropTargetContext();
         JTree tree = (JTree)dtc.getComponent();
         tree.cancelEditing();
        TreePath destinationPath = tree.getPathForLocation(loc.x, loc.y);
        if ( destinationPath != null ) {
            DefaultMutableTreeNode newParent =
            (DefaultMutableTreeNode)destinationPath.getLastPathComponent();
                tree.setSelectionPath( destinationPath );
        }
      }

      public void dragExit(DropTargetEvent dte) { }
      public void dropActionChanged(DropTargetDragEvent dtde) { }

      public void drop(DropTargetDropEvent dtde) {
        Point pt = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree)dtc.getComponent();
        TreePath parentpath = tree.getSelectionPath();
        if(parentpath == null) {
            dtde.getDropTargetContext().dropComplete(false);
        }
        DefaultMutableTreeNode parent = 
          (DefaultMutableTreeNode)parentpath.getLastPathComponent();
        if (!parent.getAllowsChildren()) {
            parent =(DefaultMutableTreeNode) parent.getParent();
        } else {
            int tn = parent.getChildCount();
            if(parent.isRoot()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("maintainSponsorHierarchy_exceptionCode.1256"));
                dtde.getDropTargetContext().dropComplete(false);
                dtde.rejectDrop();
                return;
            }
            if( parent.getChildCount() != 0 && parent.getFirstChild().getAllowsChildren()) {
                CoeusOptionPane.showInfoDialog("The group '"+parent+"' has subgroups. Cannot add sponsors to this group");
                dtde.getDropTargetContext().dropComplete(false);
                dtde.rejectDrop();
                return;
            }
        }

        try {
          blockEvents(true);
          Transferable tr = dtde.getTransferable();
          dtde.acceptDrop(dtde.getDropAction());
          SponsorHierarchyBean sponsorMaintenanceFormBean;
          SponsorHierarchyBean sponsorHierarchyBean;
          Vector dataToTree = (Vector)tr.getTransferData(TransferableUserRoleData.MULTIPLE_USERS_FLAVOR);
          DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
          DefaultMutableTreeNode newNode;
          TreeNode[] treeNode = parent.getPath();
          for(int index=0; index<dataToTree.size(); index++) {
              sponsorMaintenanceFormBean = (SponsorHierarchyBean)dataToTree.get(index);
                  setBeanData(treeNode,sponsorMaintenanceFormBean);
              newNode = new DefaultMutableTreeNode((String)sponsorMaintenanceFormBean.getSponsorCode()+":"+
                    sponsorMaintenanceFormBean.getSponsorName(),false);
              model.insertNodeInto(newNode, parent, 0);
              saveRequired = true;
          }
          tree.expandPath(parentpath);
          dtde.dropComplete(true);
          return;
        }catch ( UnsupportedFlavorException ex ){
          dtde.getDropTargetContext().dropComplete(false);
        }catch (Exception e) {
            e.printStackTrace();
            dtde.getDropTargetContext().dropComplete(false);
          dtde.rejectDrop();
        }finally{
            setRequestFocusInThread();
            blockEvents(false);
        }
      }
} 
 
 private void setBeanData(TreeNode[] treeNode, SponsorHierarchyBean sponsorHierarchyBean) {
     if(sponsorHierarchyBean.getAcType() == null)
        sponsorHierarchyBean.setRowId(rowId++);
     sponsorHierarchyBean.setAcType(TypeConstants.INSERT_RECORD);
     TreePath newTreePath = new TreePath(treeNode[0]);
     for(int index=0; index<treeNode.length; index++) {
         switch(index) {
             case 0:
                 sponsorHierarchyBean.setHierarchyName(treeNode[index].toString());
                 break;
             case 1:
                 sponsorHierarchyBean.setLevelOne(treeNode[index].toString());
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelOneSortId(getMaxLevelOneSortId(newTreePath.getLastPathComponent()));
                 break;
             case 2:
                 sponsorHierarchyBean.setLevelTwo(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 1
                 //sponsorHierarchyBean.setLevelTwoSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelTwoSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelTwo"));
                 //Bug Fix: 2058 End 1
                 
                 break;
             case 3:
                 sponsorHierarchyBean.setLevelThree(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 2
                 //sponsorHierarchyBean.setLevelThreeSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelThreeSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelThree"));
                 //Bug Fix: 2058 End 2
                 
                 break;
             case 4:
                 sponsorHierarchyBean.setLevelFour(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 3
                 //sponsorHierarchyBean.setLevelFourSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelFourSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelFour"));
                 //Bug Fix: 2058 End 3
                 
                 break;
             case 5:
                 sponsorHierarchyBean.setLevelFive(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 4
                 //sponsorHierarchyBean.setLevelFiveSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelFiveSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelFive"));
                 //Bug Fix: 2058 End 4
                 
                 break;
             case 6:
                 sponsorHierarchyBean.setLevelSix(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 5
                 //sponsorHierarchyBean.setLevelSixSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelSixSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelSix"));
                 //Bug Fix: 2058 End 5
                 
                 break;
             case 7:
                 sponsorHierarchyBean.setLevelSeven(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 6
                 //sponsorHierarchyBean.setLevelSevenSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelSevenSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelSeven"));
                 //Bug Fix: 2058 End 6
                 
                 break;
             case 8:
                 sponsorHierarchyBean.setLevelEight(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 7
                 //sponsorHierarchyBean.setLevelEightSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelEightSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelEight"));
                 //Bug Fix: 2058 End 7
                 
                 break;
             case 9:
                 sponsorHierarchyBean.setLevelNine(treeNode[index].toString());
                 
                 //Bug Fix: 2058 Start 8
                 //sponsorHierarchyBean.setLevelNineSortId(treeNode[index].getParent().getIndex(treeNode[index])+1);
                 newTreePath = newTreePath.pathByAddingChild(treeNode[index]);
                 sponsorHierarchyBean.setLevelNineSortId(getMaxSortId(newTreePath.getLastPathComponent() , "levelNine"));
                 //Bug Fix: 2058 End 8
                 
                 break;
         }
     }
     cvHierarchyData.add(sponsorHierarchyBean);
 }
 
 private int getMaxLevelOneSortId(Object levelOneData) {
    int sortId = 1;
    if(getFunctionType() == 'N') {
         sortId = ((DefaultMutableTreeNode)levelOneData).getParent().getIndex((TreeNode)levelOneData)+1;
         return sortId;
     }
    Equals filter = new Equals("levelOne",levelOneData.toString());
    CoeusVector cvFilteredData = cvHierarchyData.filter(filter);
    if(cvFilteredData != null && cvFilteredData.size()>0) {
        sortId = ((SponsorHierarchyBean)cvFilteredData.get(0)).getLevelOneSortId();
    }else {
        //Modified Case #1830 Begin
        if(cvHierarchyData != null && cvHierarchyData.size()>0) {
            cvHierarchyData.sort("levelOneSortId",false);
            sortId = ((SponsorHierarchyBean)cvHierarchyData.get(0)).getLevelOneSortId()+1;
        }
        //Modified Case #1830 End
    }
    return sortId;
 }
 
 //Bug Fix: 2058 Start 9
 private int getMaxSortId(Object nodeData , String level){
     int sortId = 1;
     SponsorHierarchyBean sponsorHierarchyBean = new SponsorHierarchyBean();
     Class dataClass = sponsorHierarchyBean.getClass();
     Method method = null;
     String levelSortId = level+"SortId";
     
     Equals filter = new Equals(level,nodeData.toString());
     
     try{
         String methodName =  "get" + (levelSortId.charAt(0)+"").toUpperCase()+ levelSortId.substring(1);
         method = dataClass.getMethod(methodName , null);
         
         CoeusVector cvFilteredData = cvHierarchyData.filter(filter);
         if(cvFilteredData != null && cvFilteredData.size()>0) {
             sponsorHierarchyBean = (SponsorHierarchyBean)cvFilteredData.get(0);
             sortId = Integer.parseInt(method.invoke(sponsorHierarchyBean , null).toString());
         }else {
             if(cvHierarchyData != null && cvHierarchyData.size()>0) {
                 cvHierarchyData.sort(levelSortId,false);
                 sponsorHierarchyBean = (SponsorHierarchyBean)cvHierarchyData.get(0);
                 sortId = Integer.parseInt(method.invoke(sponsorHierarchyBean , null).toString()) + 1;
             }
         }
     }catch (NoSuchMethodException nEx){
         nEx.printStackTrace();
     }catch (IllegalAccessException iEx){
         iEx.printStackTrace();
     }catch (IllegalArgumentException iArEx){
         iArEx.printStackTrace();
     }catch (InvocationTargetException itEx){
         itEx.printStackTrace();
     }
     return sortId;
 }
 //Bug Fix: 2058 End 9
 
 private void setRequestFocusInThread() {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                sponsorHierarchyTree.requestFocusInWindow();
            }
        });
    }

 /** Do the validation if the empty group resides in the tree view
  *Case 2427 
  */
     private boolean findEmptyGroup(){
         boolean isEmpty = true;
         TreeNode root = (TreeNode)sponsorHierarchyTree.getModel().getRoot();
         TreePath result = findEmptyGroup(sponsorHierarchyTree, new TreePath(root));
         if( result != null && result.getLastPathComponent() instanceof Integer ){
             CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("sponsorHierarchyList_exceptionCode.1204"));
             isEmpty = false;
         }
         return isEmpty;
     }


     /** An overloaded method to check the elements in the tree view
      *Case 2427
      */
     private TreePath findEmptyGroup(JTree tree, TreePath parent) {
         TreeNode node = (TreeNode)parent.getLastPathComponent();
         if (node != null && (node.getChildCount() <= 0 && node.getAllowsChildren())){
             return new TreePath( new Integer( 0 ) );
             
         }else{
             Object o = node;
             if (node.getChildCount() >= 0) {
                 for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                     TreeNode n = (TreeNode)e.nextElement();
                     TreePath path = parent.pathByAddingChild(n);
                     TreePath result = findEmptyGroup(tree, path);
                     if(result!=null)
                         return result;
                     // Found a match
                 }
             }
         }
         return null;
     }
 
}