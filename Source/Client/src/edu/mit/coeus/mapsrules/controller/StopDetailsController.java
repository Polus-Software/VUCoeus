/*
 * StopDetailsController.java
 *
 * Created on October 17, 2005, 11:51 AM
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 04-SEP-2007
 * by Leena
 */
package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.gui.StopDetailsForm;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.mapsrules.bean.MapDetailsBean;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.ListSelectionModel;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author  surekhan
 */
public class StopDetailsController extends MapsController
        implements ActionListener,DropTargetListener,DragSourceListener, DragGestureListener{
    
    //holds the stop details form components
    private StopDetailsForm stopDetailsForm;
    
    //dialog for the stopdetailsform
    private CoeusDlgWindow dlgStopDetails;
    
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    //width of the stopdetails form
    private static final int WIDTH = 735;
    
    //height for the stopdetials form
    private static final int HEIGHT = 555;
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
    //holds the title for the stopdetails form
    private static final String WINDOW_TITLE  =  "Stop Details";
    
    //coeus message resources for popup messages
    private CoeusMessageResources coeusMessageResources;
    
    //coeus search instance to open the search window
    private CoeusSearch coeusSearch;
    
    //the table model for stopdetails form
    private StopDetailsUsersTableModel stopDetailsUsersTableModel;
    
    //the icon column in the table
    private static final int ICON_COL = 0;
    
    //user id column in the table
    private static final int USER_ID = 1;
    
    //user name column in the table
    private static final int USER_NAME = 2;
    
    //unit number column in the table
    private static final int UNIT_NUMBER = 3;
    
    //unit name column in the table
    private static final int UNIT_NAME = 4;
    
    //the target drop of the drag and drop
    DropTarget targetDrop;
    
    //holds the function type for getting the users for  the unit
    private static final char GET_USERS_FOR_UNIT = 'D';
    
    //holds the servlet to be called
    private static final String MAPS_SERVLET = "/MapMaintainanceServlet";
    
    //vector to hold the user data
    private CoeusVector cvUserData;
    
    //Please select a user from the list
    private static final String SELECT_USER = "stopDetails_exceptionCode.1006";
    
    //The user has already beem selected at this level
    private static final String ALREADY_SELECTED_USER = "stopDetails_exceptionCode.1007";
    
    //Sequential Stop canot be Zero. Pls. define a sequential stop
    private static final String ZERO_SEQSTOP = "stopDetails_exceptionCode.1008";
    
    //holds the mapdetails bean
    private MapDetailsBean mapDetailsBean;
    
    //holds the level number
    private int levelNumber;
    
    //holds the stop number
    private int stopNumber;
    
    //holds the map data in the base window for the mapid
    private CoeusVector cvDetailsData;
    
    //holds the bean to get added to the base window
    private MapDetailsBean mapBean;
    
    //Do you want to save the changes?
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    //mdi form for stopdetails
    private CoeusAppletMDIForm mdiForm
            = edu.mit.coeus.utils.CoeusGuiConstants.getMDIForm();
    
    //holds the String literal "levelNumber"
    private final String LEVEL_NUMBER = "levelNumber";
    
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    //holds the roles data
    private CoeusVector cvRoles;
    
    //holds the qualifierData
    private CoeusVector cvQualifiers;
    
    //holds the DragSource
    private DragSource dragSource;
    
    //holds the cursor used while dragging
    Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    
    //holds the model for tblRoles
    private MapRolesTableModel mapRolesTableModel;
    
    //holds the DataFlavor for the type PersonRoleInfoBean.class
    private final DataFlavor ROLE_DATA_FLAVOR = new DataFlavor(PersonRoleInfoBean.class, "PersonRoleInfoBean");
    
    //to identify the user has selected a user or role
    private boolean roleType = false;
    
    private boolean saveRequired = false;
    
    private static final String ALREADY_SELECTED_ROLE = "stopDetails_exceptionCode.1009";
    
    private static final String SELECT_QUALIFIER = "stopDetails_exceptionCode.1010";
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    /** Creates a new instance of StopDetailsController */
    public StopDetailsController(MapDetailsBean mapDetailsBean,CoeusVector cvDetailsVector) throws CoeusException{
        this.mdiForm = mdiForm;
        this.mapDetailsBean = mapDetailsBean;
        this.cvDetailsData = cvDetailsVector;
        mapBean = (MapDetailsBean)mapDetailsBean;
        levelNumber = mapDetailsBean.getLevelNumber();
        stopNumber = mapDetailsBean.getStopNumber();
        stopDetailsUsersTableModel = new StopDetailsUsersTableModel();
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        mapRolesTableModel = new MapRolesTableModel();
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        postInitComponents();
        registerComponents();
        setFormData(null);
        setTableEditors();
    }
    
    //to instantiate the components in the stop details form window
    private void postInitComponents(){
        stopDetailsForm = new StopDetailsForm();
        dlgStopDetails = new CoeusDlgWindow(mdiForm);
        dlgStopDetails.setResizable(false);
        dlgStopDetails.setModal(true);
        dlgStopDetails.getContentPane().add(stopDetailsForm);
        dlgStopDetails.setFont(CoeusFontFactory.getLabelFont());
        dlgStopDetails.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgStopDetails.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgStopDetails.getSize();
        dlgStopDetails.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgStopDetails.setTitle(WINDOW_TITLE);
        
        //to show the default focus in teh window
        dlgStopDetails.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                stopDetailsForm.txtArDescription.requestFocusInWindow();
            }
        });
        
        //the action to be performed on the click of the escape key
        dlgStopDetails.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelOperation();
                return;
            }
        });
        
        //the action to be performed on the click of the window close button
        dlgStopDetails.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelOperation();
            }
        });
    }
    
    //to display the form
    public void display() {
    }
    
    //to the display the stop detail form and return the bean to get added to the base window
    public CoeusVector displayData(){
        CoeusVector  cvDetailMapData = new CoeusVector();
        stopDetailsForm.txtUserId.setEditable(false);
        if(stopDetailsForm.txtUserId.isEnabled()){
            stopDetailsForm.txtUserId.setBackground(Color.white);
            stopDetailsForm.txtUserId.setOpaque(true);
        }
        /*
         * Bugfix - Start
         * Added for Bugfix for Displaying the user name in Map details
         */
        stopDetailsForm.txtUserName.setEditable(false);
        if(stopDetailsForm.txtUserName.isEnabled()){
            stopDetailsForm.txtUserName.setBackground(Color.white);
            stopDetailsForm.txtUserName.setOpaque(true);
        }
        //Bugfix - End
        
        dlgStopDetails.show();
        
        if(mapBean == null){
            return null;
        }
        if(mapBean != null){
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            if(isRoleType()){
                mapDetailsBean.setRoleType(true);
                mapBean.setRoleCode(stopDetailsForm.txtUserId.getText());
                mapBean.setRoleDescription(stopDetailsForm.txtUserName.getText());
                mapDetailsBean.setUserId("");
                if(stopDetailsForm.cmbQualifiers.getSelectedIndex() ==-1 ||
                        ((stopDetailsForm.cmbQualifiers.getSelectedIndex() !=-1) &&
                        (((ComboBoxBean)stopDetailsForm.cmbQualifiers.getSelectedItem()).getCode().equals("")))){
                    mapBean.setQualifierCode("-1");
                    mapBean.setQualifierDescription("");
                }else{
                    ComboBoxBean selectedComboBean = (ComboBoxBean)stopDetailsForm.cmbQualifiers.getSelectedItem();
                    mapBean.setQualifierCode(selectedComboBean.getCode());
                    mapBean.setQualifierDescription(selectedComboBean.getDescription());
                }
                
            }else{
                mapDetailsBean.setRoleType(false);
                mapBean.setUserId(stopDetailsForm.txtUserId.getText());
                mapBean.setRoleCode(null);
                mapBean.setQualifierCode(null);
            }
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            //Bug fix N007 & N008 UserName Display - Start
            mapBean.setUpdateUserName(stopDetailsForm.txtUserName.getText());
            //Bug fix N007 & N008 UserName Display - End
            mapBean.setDetailDescription(stopDetailsForm.txtArDescription.getText());
            mapBean.setMapDescription(stopDetailsForm.txtArMap.getText());
            cvDetailMapData.addElement(mapBean);
        }
        return cvDetailMapData;
    }
    
    /**
     * perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return SpecialRateForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return stopDetailsForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /**
     * Registers listener and other components
     */
    public void registerComponents() {
        
        stopDetailsForm.btnCancel.addActionListener(this);
        stopDetailsForm.btnFindUser.addActionListener(this);
        stopDetailsForm.btnOk.addActionListener(this);
        stopDetailsForm.rdBtnSeqStop.addActionListener(this);
        stopDetailsForm.rdBtnApprover.addActionListener(this);
        stopDetailsForm.rdBtnAltApprover.addActionListener(this);
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.Component[] components = {stopDetailsForm.txtArDescription ,
        stopDetailsForm.btnOk ,
        stopDetailsForm.btnFindUser,
        stopDetailsForm.btnCancel,
        stopDetailsForm.rdBtnSeqStop,
        stopDetailsForm.rdBtnApprover,
        stopDetailsForm.rdBtnAltApprover};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        stopDetailsForm.setFocusTraversalPolicy(traversePolicy);
        stopDetailsForm.setFocusCycleRoot(true);
        stopDetailsForm.txtArDescription.setLineWrap(true);
        stopDetailsForm.txtArDescription.setFont(CoeusFontFactory.getNormalFont());
        stopDetailsForm.txtArDescription.setDocument(new LimitedPlainDocument(60));
        
        stopDetailsForm.tblUser.setModel(stopDetailsUsersTableModel);
        stopDetailsForm.tblUser.setAutoResizeMode(
                stopDetailsForm.tblUser.AUTO_RESIZE_OFF);
        stopDetailsForm.tblUser.setRowSelectionAllowed(true);
        
        /*
         * Bug fix - N011 Username Display - Start
         * Made Userid invisible and added one more text field for displaying
         * Username
         */
        stopDetailsForm.txtUserId.setVisible(false);
        //targetDrop = new DropTarget(stopDetailsForm.txtUserId,this);
        targetDrop = new DropTarget(stopDetailsForm.txtUserName,this);
        //Bug fix - N011 Username Display - End
        stopDetailsForm.txtArDescription.setCaretPosition(0);
        stopDetailsForm.txtArMap.setCaretPosition(0);
        
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(stopDetailsForm.tblRoles,DnDConstants.ACTION_MOVE, this);
        
        stopDetailsForm.tblRoles.setModel(mapRolesTableModel);
        stopDetailsForm.tblRoles.setRowSelectionAllowed(true);
        stopDetailsForm.tblRoles.getTableHeader().setReorderingAllowed(false);
        //stopDetailsForm.tblRoles.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    }
    
    /* setting the renderers and sizes for columns of the table*/
    private void setTableEditors() {
        stopDetailsForm.tblUser.setRowHeight(22);
        
        JTableHeader tableHeader = stopDetailsForm.tblUser.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        stopDetailsForm.tblUser.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        stopDetailsForm.tblUser.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        
        
        TableColumn column = stopDetailsForm.tblUser.getColumnModel().getColumn(ICON_COL);
        column.setPreferredWidth(20);
        column.setResizable(true);
        column.setCellRenderer(new StatusCellRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        tableHeader.setReorderingAllowed(false);
        
        
        column = stopDetailsForm.tblUser.getColumnModel().getColumn(USER_ID);
        column.setPreferredWidth(70);
        column.setResizable(true);
        tableHeader.setReorderingAllowed(false);
        
        column = stopDetailsForm.tblUser.getColumnModel().getColumn(USER_NAME);
        column.setPreferredWidth(140);
        column.setResizable(true);
        tableHeader.setReorderingAllowed(false);
        
        column = stopDetailsForm.tblUser.getColumnModel().getColumn(UNIT_NAME);
        //column.setPreferredWidth(250);
        column.setPreferredWidth(284);
        column.setResizable(true);
        tableHeader.setReorderingAllowed(false);
        
        column = stopDetailsForm.tblUser.getColumnModel().getColumn(UNIT_NUMBER);
        column.setPreferredWidth(75);
        column.setResizable(true);
        tableHeader.setReorderingAllowed(false);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        stopDetailsForm.tblRoles.setRowHeight(22);
        stopDetailsForm.tblRoles.setBackground((Color)UIManager.getDefaults().get("Panel.background"));
        stopDetailsForm.tblRoles.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        stopDetailsForm.tblRoles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        column  = stopDetailsForm.tblRoles.getColumnModel().getColumn(0);
        //column.setPreferredWidth(200);
        column.setResizable(true);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    }
    
    //to save the form data
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    //to set the data to the form and the table
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        int levelNo = 0;
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //cvUserData = getUserData(mapDetailsBean.getUnitNumber());//CoeusGuiConstants.getMDIForm().getUnitNumber());
        getUserData(mapDetailsBean.getUnitNumber());
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        int levelNum = mapDetailsBean.getLevelNumber();
        if(levelNum == 0){
            stopDetailsForm.rdBtnSeqStop.setSelected(true);
            stopDetailsForm.rdBtnApprover.setEnabled(false);
            stopDetailsForm.rdBtnAltApprover.setEnabled(false);
        }else{
            stopDetailsForm.rdBtnSeqStop.setSelected(true);
            stopDetailsForm.rdBtnApprover.setEnabled(true);
            stopDetailsForm.rdBtnAltApprover.setEnabled(true);
        }
        if(cvDetailsData!=null&&cvDetailsData.size()>0){
            cvDetailsData.sort(LEVEL_NUMBER,false);
            levelNo = ((MapDetailsBean)cvDetailsData.get(0)).getLevelNumber();
            cvDetailsData.sort(LEVEL_NUMBER,true);
        }
        stopDetailsForm.txtSeqStop.setText(""+(levelNo + 1));
        mapBean.setLevelNumber(levelNo + 1);
        if(mapBean.getStopNumber() == 0){
            mapBean.setStopNumber(1);
        }
        if(stopDetailsForm.rdBtnSeqStop.isSelected()){
            mapBean.setPrimayApproverFlag(true);
        }
        stopDetailsForm.txtArMap.setText(mapDetailsBean.getMapDescription());
    }
    
    
    //to get the user map data by passing the home unitnumber
    private CoeusVector getUserData(String unitNo)
    throws CoeusException{
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //CoeusVector cvUserData = null;
        CoeusVector cvMapData = null;
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        
        request.setDataObject(unitNo);
        request.setFunctionType(GET_USERS_FOR_UNIT);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + MAPS_SERVLET;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //cvUserData = (CoeusVector)response.getDataObject();
                cvMapData = (CoeusVector)response.getDataObject();
                cvUserData = (CoeusVector)cvMapData.get(0);
                cvRoles = (CoeusVector)cvMapData.get(1);
                cvQualifiers = (CoeusVector)cvMapData.get(2);
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return cvUserData;
    }
    
    //to validate the stop details form
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if(stopDetailsForm.txtUserId.getText().equals("") || stopDetailsForm.txtUserId.getText().equals(null)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_USER));
            stopDetailsForm.txtUserId.requestFocusInWindow();
            return false;
        }
        
        //if the user selected is already there for the level
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        if(isRoleType()){
            if(stopDetailsForm.cmbQualifiers.getModel().getSize()>1){
                if(((ComboBoxBean)stopDetailsForm.cmbQualifiers.getSelectedItem()).getCode().equals("")){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_QUALIFIER));
                    stopDetailsForm.cmbQualifiers.requestFocusInWindow();
                    return false;
                }
            }
            Equals roleIdEquals,levelNumEquals, qualifierCodeEquals;
            And levelNumAndRoleIdAnd, levelRoleAndQualifierAnd;
            CoeusVector cvFilterData = new CoeusVector();
            
            if(cvRoles != null && cvRoles.size()>0){
                String selectedRoleId = stopDetailsForm.txtUserId.getText();
                String selectedQualifierCode = "-1";
                if(stopDetailsForm.cmbQualifiers.getSelectedIndex() != -1){
                    selectedQualifierCode = ((ComboBoxBean)stopDetailsForm.cmbQualifiers.getSelectedItem()).getCode();
                }
                if(cvDetailsData != null && cvDetailsData.size() > 0){
                    roleIdEquals = new Equals("roleCode", selectedRoleId);
                    levelNumEquals = new Equals("levelNumber", new Integer(mapDetailsBean.getLevelNumber()));
                    qualifierCodeEquals = new Equals("qualifierCode", selectedQualifierCode);
                    levelNumAndRoleIdAnd = new And(roleIdEquals, levelNumEquals);
                    levelRoleAndQualifierAnd = new And(levelNumAndRoleIdAnd, qualifierCodeEquals);
                    cvFilterData = cvDetailsData.filter(levelRoleAndQualifierAnd);
                    if(cvFilterData.size() > 0){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ALREADY_SELECTED_ROLE));
                        return false;
                    }
                }
            }
        }else{
            Equals userIdEquals,levelNumEquals;
            And levelNumAnduserIdEquals;
            CoeusVector cvFilterData = new CoeusVector();
            if(cvDetailsData != null && cvDetailsData.size() > 0){
                userIdEquals = new Equals("userId", stopDetailsForm.txtUserId.getText());
                levelNumEquals = new Equals(LEVEL_NUMBER, new Integer(mapBean.getLevelNumber()));
                levelNumAnduserIdEquals = new And(userIdEquals, levelNumEquals);
                cvFilterData = cvDetailsData.filter(levelNumAnduserIdEquals);
                if(cvFilterData.size() > 0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ALREADY_SELECTED_USER));
                    return false;
                }
            }
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        if(mapBean != null){
            if(mapBean.getLevelNumber() == 0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ZERO_SEQSTOP));
                return false;
            }
        }
        return true;
    }
    
    //the actions tobe performed on the action event
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(source.equals(stopDetailsForm.btnCancel)){
                performCancelOperation();
            }else if(source.equals(stopDetailsForm.btnFindUser)){
                displayUserSearch();
            }else if(source.equals(stopDetailsForm.btnOk)){
                performOkOperation();
            }else if(source.equals(stopDetailsForm.rdBtnSeqStop)){
                stopDetailsForm.txtSeqStop.setText(""+(levelNumber+1));
                mapBean.setLevelNumber(new Integer(stopDetailsForm.txtSeqStop.getText()).intValue());
                mapBean.setStopNumber(1);
                mapBean.setPrimayApproverFlag(true);
            }else if(source.equals(stopDetailsForm.rdBtnApprover)){
                int stopNumber = 0;
                CoeusVector cvData = computeStopAndLevelNumbers();
                cvData.sort("stopNumber",false);
                mapBean.setPrimayApproverFlag(true);
                if(cvData != null && cvData.size() > 0 && cvData.size() > 0){
                    MapDetailsBean dataBean = (MapDetailsBean)cvData.get(0);
                    stopNumber = dataBean.getStopNumber();
                    stopDetailsForm.txtSeqStop.setText(""+(levelNumber));
                    mapBean.setStopNumber(stopNumber + 1);
                    mapBean.setLevelNumber(levelNumber);
                }else if(cvData.size() == 0){
                    stopDetailsForm.txtSeqStop.setText(""+(levelNumber));
                    mapBean.setStopNumber(stopNumber + 1);
                    mapBean.setLevelNumber(levelNumber);
                }
            }else if(source.equals(stopDetailsForm.rdBtnAltApprover)){
                stopDetailsForm.txtSeqStop.setText(""+(levelNumber));
                mapBean.setLevelNumber(levelNumber);
                mapBean.setStopNumber(stopNumber);
                mapBean.setPrimayApproverFlag(false);
            }
        }catch(CoeusUIException coeusUIException){
            coeusUIException.printStackTrace();
        }finally {
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    //the action performed on the click of the cancel button
    private void performCancelOperation(){
        //Added the if condition for Coeus 4.3 Routing enhancement -PT ID:2785
        //Show the save confirmation only if the user has entered some values
        if(checkSaveRequired()){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_CHANGES),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    try{
                        if( validate() ){
                            dlgStopDetails.dispose();
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                    mapBean = null;
                    dlgStopDetails.dispose();
                    break;
                default:
                    break;
            }
        }else{
            dlgStopDetails.dispose();
        }
    }
    
    //to compute the stop and level numbers on the click of the radio buttons in the form
    private CoeusVector computeStopAndLevelNumbers(){
        Equals levelNumEquals,mapIdEquals;
        And levelNumAndMapIdEquals;
        CoeusVector cvFilterData = new CoeusVector();
        levelNumEquals = new Equals(LEVEL_NUMBER, new Integer(levelNumber));
        mapIdEquals = new Equals("mapId", new Integer(mapDetailsBean.getMapId()));
        levelNumAndMapIdEquals = new And(levelNumEquals, mapIdEquals);
        cvFilterData = cvDetailsData.filter(levelNumAndMapIdEquals);
        return cvFilterData;
        
    }
    
    //the action to be performed on the click of the Ok button
    private void performOkOperation()throws CoeusUIException{
        if(validate()){
            saveRequired = true;
            dlgStopDetails.dispose();
        }
    }
    
    /*the action to be performed on the click of the search button,
     *opens the user search window*/
    private void displayUserSearch() {
        try{
            coeusSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "userSearch",CoeusSearch.TWO_TABS);
            coeusSearch.showSearchWindow();
            HashMap selectedRow = coeusSearch.getSelectedRow();
            if(selectedRow != null) {
                if(selectedRow.get("USER_ID").toString().trim() == null) {
                    stopDetailsForm.txtUserId.setText("");
                }else{
                    //stopDetailsForm.txtUserId.setText(selectedRow.get("USER_ID").toString().trim().toUpperCase());
                    stopDetailsForm.txtUserId.setText(selectedRow.get("USER_ID").toString().trim());
                    //Bug fix #N008 Displaying UserName - Start
                    //stopDetailsForm.txtUserName.setText(selectedRow.get("USER_NAME").toString().trim().toUpperCase());
                    stopDetailsForm.txtUserName.setText(selectedRow.get("USER_NAME").toString().trim());
                    //Bug fix #N008 Displaying UserName - End
                }
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                stopDetailsForm.cmbQualifiers.setEnabled(false);
                stopDetailsForm.lblQualifier.setEnabled(false);
                stopDetailsForm.cmbQualifiers.setModel(new DefaultComboBoxModel());
                setRoleType(false);
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            }
            
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }
    
    public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
        
    }
    
    public void dragExit(java.awt.dnd.DropTargetEvent dte) {
        
    }
    
    public void dragOver(java.awt.dnd.DropTargetDragEvent dtde) {
        
    }
    
    //the unitnumber to be dropped in  the userid text box
    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        try{
            DropTargetContext dtc = dtde.getDropTargetContext();
            targetDrop =new DropTarget(dtc.getComponent(),this);
            Transferable transferable = dtde.getTransferable();
            dtde.acceptDrop(dtde.getDropAction());
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
            //Checks whether the drag was originated from the tblUsers or tblRoles
            if(dtde.isDataFlavorSupported(TransferableUserRoleData.MULTIPLE_USERS_FLAVOR)){
                Object data = transferable.getTransferData(TransferableUserRoleData.MULTIPLE_USERS_FLAVOR );
                Vector vecData = (Vector)data;
                UserInfoBean bean = (UserInfoBean)vecData.get(0);
                //Bug fix Case#N007 UserName Display - Start
                //stopDetailsForm.txtUserName.setText(bean.getUserName().trim().toUpperCase());
                stopDetailsForm.txtUserName.setText(bean.getUserName().trim());
                //Bug fix Case#N007 UserName Display - End
                //stopDetailsForm.txtUserId.setText(bean.getUserId().trim().toUpperCase());
                stopDetailsForm.txtUserId.setText(bean.getUserId().trim());
                stopDetailsForm.cmbQualifiers.setEnabled(false);
                stopDetailsForm.lblQualifier.setEnabled(false);
                stopDetailsForm.cmbQualifiers.setModel(new DefaultComboBoxModel());
                setRoleType(false);
            }else if(dtde.isDataFlavorSupported(ROLE_DATA_FLAVOR)){
                PersonRoleInfoBean personRoleInfoBean = (PersonRoleInfoBean)transferable.getTransferData(ROLE_DATA_FLAVOR);
                stopDetailsForm.txtUserName.setText(personRoleInfoBean.getRoleName());
                stopDetailsForm.txtUserId.setText(personRoleInfoBean.getRoleCode());
                stopDetailsForm.cmbQualifiers.setEnabled(true);
                stopDetailsForm.lblQualifier.setEnabled(true);
                setRoleType(true);
                populateQualifiers(personRoleInfoBean.getRoleCode());
            }
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        }catch(Exception exception){
            exception.printStackTrace();
            dtde.getDropTargetContext().dropComplete(false);
        }
    }
    
    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde) {
        
    }
    
    //the table model for the users table
    class StopDetailsUsersTableModel extends AbstractTableModel{
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //String colNames[] = {"", "User ID", "User Name", "Unit No.", "Unit Name"};
        String colNames[] = {"", "User ID", "User Name", "Unit No.", "Unit Name"};
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        Class[] colTypes = new Class [] {Object.class , String.class, String.class, String.class, String.class};
        
        /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /* returns the number of rows*/
        public int getRowCount() {
            if(cvUserData == null ){
                return 0;
            }else{
                return cvUserData.size();
            }
        }
        
        /* gets the value at a particular cell*/
        public Object getValueAt(int row, int column) {
            UserInfoBean userInfoBean = (UserInfoBean)cvUserData.get(row);
            switch(column){
                case ICON_COL:
                    if(userInfoBean.getStatus() == 'A'){
                        return "A";
                    }else if(userInfoBean.getStatus() == 'I'){
                        return "I";
                    }
                case USER_ID:
                    return  userInfoBean.getUserId();
                case USER_NAME:
                    return userInfoBean.getUserName();
                case UNIT_NUMBER:
                    return userInfoBean.getUnitNumber();
                case UNIT_NAME:
                    return userInfoBean.getUnitName();
                    
            }
            return "";
        }
        
        /* Sets the value in the cell*/
        public void setValueAt(Object value, int row, int col){
            
        }
        
    }
    
    /**
     * Custom cell renderer which is used in this table. Renders different icons to
     * the first column depending on the status of the user.
     */
    class StatusCellRenderer extends DefaultTableCellRenderer {
        ImageIcon activeUserIcon, inactiveUserIcon;
        String activeUserImageName, inactiveUserImageName;
        public StatusCellRenderer() {
            /* try to read from the URL, if unable to read , take the default values.
             * If UserRolesTable is used as a component in Forte Form editor, it can't read
             * from URL, so then it will take the icons from the strings given and
             * bring up the component.
             */
            
            activeUserImageName = "/images/usera.gif";
            inactiveUserImageName = "/images/useri.gif";
            java.net.URL actUser = getClass().getClassLoader().getResource( CoeusGuiConstants.ACTIVE_USER_ICON );
            if( actUser != null ) {
                activeUserIcon = new ImageIcon( actUser );
            }else{
                activeUserIcon = new ImageIcon( activeUserImageName );
            }
            java.net.URL inactUser = getClass().getClassLoader().getResource( CoeusGuiConstants.INACTIVE_USER_ICON );
            if( inactUser != null ) {
                inactiveUserIcon = new ImageIcon( inactUser );
            }else{
                inactiveUserIcon = new ImageIcon( inactiveUserImageName );
            }
        }
        
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            
            setOpaque(false);
            if( value != null ) {
                if( value.toString().equals("A") ) {
                    setIcon( activeUserIcon );
                }else if( value.toString().equals("I") ) {
                    setIcon( inactiveUserIcon );
                }
            }
            return this;
            
        }
        
    }
    
    //to clean up the instances
    public void cleanUp(){
        mdiForm = null;
        dlgStopDetails = null;
        stopDetailsForm = null;
        stopDetailsUsersTableModel = null;
        cvDetailsData = null;
        cvUserData = null;
        mapDetailsBean = null;
        mapBean = null;
    }
    
    public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde) {
    }
    
    public void dragEnter(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dragExit(java.awt.dnd.DragSourceEvent dse) {
    }
    
    public void dragOver(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dropActionChanged(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    public void dragGestureRecognized(DragGestureEvent dge) {
        int selectedRow = stopDetailsForm.tblRoles.getSelectedRow();
        if(selectedRow!=-1){
            PersonRoleInfoBean personRoleInfoBean = (PersonRoleInfoBean)cvRoles.get(selectedRow);
            TransferableRoleData data = new TransferableRoleData(personRoleInfoBean);
            dragSource.startDrag( dge, dragCursor, data, this);
        }
    }
    public void populateQualifiers(String roleCode){
        
        Equals equalsOperator = new Equals("roleCode", roleCode);
        CoeusVector cvRoleQualifiers = cvQualifiers.filter(equalsOperator);
        CoeusVector cvComboQualifiers = new CoeusVector();
        ComboBoxBean comboBoxBean = null;
        PersonRoleInfoBean personRoleInfoBean = null;
        if(cvRoleQualifiers!=null && cvRoleQualifiers.size()>0){
            comboBoxBean = new ComboBoxBean("","");
            cvComboQualifiers.add(comboBoxBean);
            comboBoxBean = new ComboBoxBean("","");
            cvComboQualifiers.add(comboBoxBean);
            for(int i=0; i<cvRoleQualifiers.size(); i++){
                personRoleInfoBean = (PersonRoleInfoBean)cvRoleQualifiers.get(i);
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(personRoleInfoBean.getQualifierCode());
                comboBoxBean.setDescription(personRoleInfoBean.getRoleQualifier());
                cvComboQualifiers.add(comboBoxBean);
            }
            stopDetailsForm.cmbQualifiers.setModel(new DefaultComboBoxModel(cvComboQualifiers));
        }else{
            stopDetailsForm.cmbQualifiers.setModel(new DefaultComboBoxModel());
        }
        
    }
    
    //This class is used as the model for the roles table
    class MapRolesTableModel extends AbstractTableModel{
        private String columnNames[] = {"Role Description"};
        private Class colClass[] = {String.class, String.class};
        
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        public Object getValueAt(int row, int column){
            if(cvRoles.get(row)!=null){
                return ((PersonRoleInfoBean)cvRoles.get(row)).getRoleName();
            }else{
                return "";
            }
        }
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public String getColumnName(int column){
            return columnNames[column];
        }
        
        public int getRowCount() {
            if(cvRoles == null){
                return 0;
            }else {
                return cvRoles.size();
            }
        }
    }
    class TransferableRoleData implements Transferable{
        DataFlavor[] flavors = {ROLE_DATA_FLAVOR};
        public PersonRoleInfoBean personRoleInfoBean;
        public TransferableRoleData(PersonRoleInfoBean personRoleInfoBean){
            this.personRoleInfoBean = personRoleInfoBean;
        }
        public Object getTransferData(java.awt.datatransfer.DataFlavor dataFlavor)
        throws UnsupportedFlavorException, IOException {
            return personRoleInfoBean;
        }
        
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
        
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            boolean dataFlavorSupported = false;
            if (flavor.equals( ROLE_DATA_FLAVOR ) ) {
                dataFlavorSupported = true;
            }
            return dataFlavorSupported;
        }
        
    }
    
    public boolean isRoleType() {
        return roleType;
    }
    
    public void setRoleType(boolean roleType) {
        this.roleType = roleType;
    }
    /**
     * Check whether the user has entered some data
     *
     * @return boolean true if the user has entered some data, else false
     */
    public boolean checkSaveRequired(){
        saveRequired = false;
        if(!stopDetailsForm.rdBtnSeqStop.isSelected()){
            saveRequired = true;
        }else if(stopDetailsForm.txtUserName.getText().trim().length()>0){
            saveRequired = true;
        }else if(stopDetailsForm.txtArDescription.getText().trim().length()>0){
            saveRequired = true;
        }else if(stopDetailsForm.cmbQualifiers.isEnabled()){
            saveRequired = true;
        }
        return saveRequired;
    }
    public boolean isSaveRequired() {
        return saveRequired;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    
    
}
