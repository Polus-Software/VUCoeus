/*
 * @(#)MapDetailController.java 1.0 10/14/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * MapDetailController.java
 *
 * Created on October 14, 2005, 5:53 PM
 */

/* PMD check performed, and commented unused imports and variables on 02-JUL-2007
 * by Leena
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.MapDetailsBean;
import edu.mit.coeus.mapsrules.bean.MapHeaderBean;
import edu.mit.coeus.mapsrules.gui.MapDetailForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  tarique
 */
public class MapDetailController extends MapsController implements ActionListener{
    /** Table model for map detail table */
    private MapsDetailTableModel mapsDetailTableModel;
    /** Table renderer for map detail table */
    private MapsDetailTableCellRenderer mapsDetailTableCellRenderer;
    /** Table Editor for map detail table */
    private MapsDetailTableCellEditor mapsDetailTableCellEditor;
    /** Class for accesing messages from resource file */
    private CoeusMessageResources coeusMessageResources;
    /** Message for Map option */
    private static final String MAP_OPTION="unitMapsDetail_exceptionCode.1000";
    /** Message for Map Description */
    private static final String MAP_DESC="unitMapsDetail_exceptionCode.1001";
//    /** Message for Map type */
//    private static final String MAP_TYPE="unitMapsDetail_exceptionCode.1002";
    /** Message for If no row is present */
    private static final String NO_ROW_TO_DELETE="unitMapsDetail_exceptionCode.1003";
    /** Message for Select row before deleting(if row is not selected) */
    private static final String SELECT_ROW_DELETE="unitMapsDetail_exceptionCode.1004";
    /** Message for Delete confirmation */
    private static final String DELETE_ROW_CONFIRM="unitMapsDetail_exceptionCode.1005";
    /** Message for save changes */
    private static final String SAVE_CHANGES="unitMapsDetail_exceptionCode.1009";
    /** Message for At least one stop in Map */
    private static final String ATLEAST_ONE_STOP="unitMapsDetail_exceptionCode.1010";
    /** Message for Atleast one primary approver for  every stop number */
    private static final String ATLEAST_ONE_PRIMARY="unitMapsDetail_exceptionCode.1011";
    /** Map servlet */
    private static final String MAPS_SERVLET = "/MapMaintainanceServlet";
    /** char used  for getting maps details data */
    private static final char GET_MAPS_DETAIL_DATA = 'A';
    /** char used for getting maps id data */
    private static final char GET_MAP_ID='C';
    /** char used for updating maps details data */
    private static final char UPDATE_UNIT_MAP='G';
    /** Global instance of EMPTY string */
    private static final String EMPTY_STRING="";
    /** Global instance of Indirect type */
    private static final String INDIRECT_TYPE="I";
//    /** Global instance of Direct type */
//    private static final String DIRECT_TYPE="D";
    /**Vector which contains header data */
    private CoeusVector cvHeaderData;
    /**Vector which contains details data */
    private CoeusVector cvDetailsData;
    /**Vector which contains both header and details data */
    private CoeusVector cvData;
    /**Vector which contains deleted data */
    private CoeusVector cvDeletedData;
    /**Header bean */
    private MapHeaderBean mapHeaderBean;
    /** Global instance of row height  */
    private static final int INCREASED_ROW_HEIGHT=47;
    /** Global instance of row height (decrease) */
    private static final int DECREASED_ROW_HEIGHT=22;
    /** Global instance of modified status in window */
    private boolean modified=false;
    /** Global instance of succesful saving */
    private boolean succesfulSave=false;
    /** Map detail form class */
    private MapDetailForm mapDetailForm;
    /** width of the window */
    //UserId to UserName Enhancement
//    private final int WIDTH=660;
    //Modified for Coeus 4.3 enhancement PT ID 2785- Routing enhancement - start
    //Increased the width from 760 to 785
    private final int WIDTH=785;
    //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    /** dialog Title */
    private static String WINDOW_TITLE="Map Details";
    /** Dialog */
    private CoeusDlgWindow dlgMapDetail;
    /** StopDetails Form */
    private StopDetailsController stopDetailsController;
    /** Hieght of the window */
    private final int HEIGHT=440;
    /** Creates the base window form */
    private edu.mit.coeus.gui.CoeusAppletMDIForm mdiForm
            = edu.mit.coeus.utils.CoeusGuiConstants.getMDIForm();
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    //Map that holds the role as key and value another hashMap with qualifierCode
    //as key qualifier description as value
    private Map roleQulaifiersMap;
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    /**
     * Creates a new instance of MapDetailController
     * @throws CoeusException
     */
    public MapDetailController(MapHeaderBean mapHeaderBean) throws CoeusException{
        this.mapHeaderBean = mapHeaderBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setTableEditors();
        postInitComponents();
        setTableKeyTraversal();
        
    }
    /**Method to display the controlled window
     */
    public void display() {
        dlgMapDetail.setVisible(true);
        
    }
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
    }
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public java.awt.Component getControlledUI() {
        return mapDetailForm;
    }
    /** returns the form data
     * @return the form data
     */
    public Object getFormData() {
        String desc = mapDetailForm.txtArDescription.getText().trim();
        
        //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
        // Removed the direct and indirect options from gui and set the default
        // type of the map as INDIRECT
        /*String mapType = "";
        if(mapDetailForm.rdBtnDirect.isSelected()){
            mapType = DIRECT_TYPE;
        }else if(mapDetailForm.rdBtnIndirect.isSelected()){
            mapType = INDIRECT_TYPE;
        }
        if(!mapHeaderBean.getMapType().equalsIgnoreCase(mapType)){
            modified = true;
            mapHeaderBean.setMapType(mapType);
            if(getFunctionType() != TypeConstants.NEW_MODE){
                mapHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
            }else{
                mapHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
            }
        }*/
        mapHeaderBean.setMapType(INDIRECT_TYPE);
        //Modified for Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - end
        
        if(!mapHeaderBean.getMapDescription().equals(desc)){
            modified  =  true;
            mapHeaderBean.setMapDescription(desc);
            if(getFunctionType() != TypeConstants.NEW_MODE){
                mapHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
            }else{
                mapHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
            }
        }
        return mapHeaderBean;
    }
    /**
     * Method to initialize the dialog
     * @throws CoeusException
     */
    public void postInitComponents() throws CoeusException{
        dlgMapDetail = new edu.mit.coeus.gui.CoeusDlgWindow(mdiForm);
        dlgMapDetail.setResizable(false);
        dlgMapDetail.setModal(true);
        dlgMapDetail.setTitle(WINDOW_TITLE );
        dlgMapDetail.getContentPane().add(mapDetailForm);
        dlgMapDetail.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        dlgMapDetail.setSize(WIDTH, HEIGHT);
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dlgSize = dlgMapDetail.getSize();
        dlgMapDetail.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgMapDetail.setDefaultCloseOperation(edu.mit.coeus.gui.
                CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgMapDetail.addComponentListener(
                new java.awt.event.ComponentAdapter(){
            public void componentShown(java.awt.event.ComponentEvent e){
                requestDefaultFocus();
            }
        });
        dlgMapDetail.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent we){
                try{
                    performCancelAction();
                }catch (CoeusException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
        
        dlgMapDetail.addEscapeKeyListener(new javax.swing.AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                try{
                    performCancelAction();
                }catch (CoeusException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
    }
    /** Set the Editor and renderer for Table */
    private void setTableEditors() {
        try{
            mapDetailForm.txtMap.setDocument(new LimitedPlainDocument(6));
            mapDetailForm.txtUnitNumber.setDocument(new LimitedPlainDocument(8));
            mapDetailForm.txtArDescription.setDocument(new LimitedPlainDocument(200));
            mapDetailForm.scrPnDescription.setBackground(Color.WHITE);
            mapDetailForm.tblMap.setRowHeight(DECREASED_ROW_HEIGHT);
            mapDetailForm.tblMap.setShowHorizontalLines(false);
            mapDetailForm.tblMap.setShowVerticalLines(false);
            TableColumn column = mapDetailForm.tblMap.getColumnModel().getColumn(0);
            column.setPreferredWidth(672);
            column.setMaxWidth(672);
            column.setCellEditor(mapsDetailTableCellEditor);
            column.setCellRenderer(mapsDetailTableCellRenderer);
            column.setHeaderRenderer(new EmptyHeaderRenderer());
            JTableHeader header = mapDetailForm.tblMap.getTableHeader();
            header.setReorderingAllowed(false);
            header.setResizingAllowed(false);
            
        }catch(Exception ce){
            CoeusOptionPane.showErrorDialog(ce.getMessage());
            ce.printStackTrace();
        }
        
    }
    /**
     *Method to focus on window open
     */
    private void requestDefaultFocus(){
        if(mapDetailForm.tblMap.getRowCount()  >  0){
            mapDetailForm.tblMap.setRowSelectionInterval(0,0);
            if(mapDetailForm.tblMap.getEditorComponent()!=null){
                mapDetailForm.tblMap.getEditorComponent().requestFocusInWindow();
            }else {
                mapDetailForm.txtArDescription.requestFocusInWindow();
            }
        }else{
            mapDetailForm.txtArDescription.requestFocusInWindow();
        }
    }
    /** registers GUI Components with event Listeners.
     */
    public void registerComponents() {
        cvData = new CoeusVector();
        cvDeletedData = new CoeusVector();
        cvDetailsData = new CoeusVector();
        cvHeaderData = new CoeusVector();
        mapDetailForm = new MapDetailForm();
        mapDetailForm.btnAdd.addActionListener(this);
        mapDetailForm.btnOk.addActionListener(this);
        mapDetailForm.btnCancel.addActionListener(this);
        mapDetailForm.btnDelete.addActionListener(this);
        //Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - start
//        mapDetailForm.btnAddRole.addActionListener(this);
        //Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - end
        
        //Commented for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
        // Removed the direct and indirect options from gui and set the default
        // type of the map as INDIRECT
        /*mapDetailForm.rdBtnDirect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                mapDetailForm.rdBtnDirect.setSelected(true);
            }
        });
        mapDetailForm.rdBtnIndirect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                mapDetailForm.rdBtnIndirect.setSelected(true);
            }
        });*/
        //Commented for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
        
        mapDetailForm.txtArDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                mapDetailForm.txtArDescription.selectAll();
            }
        }
        );
        mapsDetailTableModel = new MapsDetailTableModel();
        mapsDetailTableCellEditor = new MapsDetailTableCellEditor();
        mapsDetailTableCellRenderer = new MapsDetailTableCellRenderer();
        mapDetailForm.tblMap.setModel(mapsDetailTableModel);
        mapDetailForm.tblMap.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        //Modified for Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - start
        // Removed the direct and indirect options from gui and set the default
        // type of the map as INDIRECT
        java.awt.Component[] components = {
            mapDetailForm.tblMap, mapDetailForm.btnAdd,mapDetailForm.btnDelete
                    ,mapDetailForm.btnOk,mapDetailForm.btnCancel,mapDetailForm.txtArDescription};
        //Modified for Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - end
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        mapDetailForm.setFocusTraversalPolicy(traversePolicy);
        mapDetailForm.setFocusCycleRoot(true);
        
    }
    /**
     * saves the Form Data.
     * @throws CoeusException
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        mapsDetailTableCellEditor.stopCellEditing();
        try{
            MapHeaderBean mapHeaderBean = (MapHeaderBean)getFormData();
            if(modified){
                CoeusVector cvTemp = new CoeusVector();
                if(cvDeletedData != null && cvDeletedData.size()  >  0){
                    boolean checkNew =
                            (getFunctionType()  ==   TypeConstants.NEW_MODE) ? false : true;
                    if(checkNew){
                        for(int index = 0;index < cvDeletedData.size();index++){
                            MapDetailsBean mapDetailsBean
                                    =(MapDetailsBean)cvDeletedData.get(index);
                            mapDetailsBean.setMapId(mapHeaderBean.getMapId());
                            mapDetailsBean.setAcType(TypeConstants.DELETE_RECORD);
                        }
                        cvTemp.addAll(cvDeletedData);
                    }
                }
                if(getFunctionType()  ==   TypeConstants.NEW_MODE){
                    for(int index = 0;index < cvDetailsData.size();index++){
                        MapDetailsBean mapDetailsBean
                                =(MapDetailsBean)cvDetailsData.get(index);
                        mapDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                        mapDetailsBean.setMapId(mapHeaderBean.getMapId());
                        
                    }
                    cvTemp.addAll(cvDetailsData);
                }else{
                    for(int index = 0;index < cvDetailsData.size();index++){
                        MapDetailsBean mapDetailsBean
                                =(MapDetailsBean)cvDetailsData.get(index);
                        mapDetailsBean.setMapId(mapHeaderBean.getMapId());
                    }
                    cvTemp.addAll(cvDetailsData);
                }
                Hashtable dataToServer = new Hashtable();
                dataToServer.put(MapHeaderBean.class,mapHeaderBean);
                dataToServer.put(MapDetailsBean.class,cvTemp);
                dataToServer.put("MODE", new Character(getFunctionType()));
                RequesterBean request = new RequesterBean();
                ResponderBean response = null;
                mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                String connectTo = CoeusGuiConstants.CONNECTION_URL + MAPS_SERVLET;
                request.setDataObject(dataToServer);
                request.setFunctionType(UPDATE_UNIT_MAP);
                AppletServletCommunicator comm =
                        new AppletServletCommunicator(connectTo, request);
                comm.send();
                response = comm.getResponse();
                if(response != null){
                    if(response.isSuccessfulResponse()){
                        mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        modified = false;
                        boolean success = ((Boolean)response.getDataObject()).booleanValue();
                        if(success){
                            succesfulSave = true;
                        }
                    }else{
                        mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        throw new CoeusException(response.getMessage(),0);
                    }
                }else{
                    mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            }
            
        }catch(CoeusException ce){
            ce.printStackTrace();
        }
    }
    /**
     * This method is used to set the form data specified in
     * <CODE >  data </CODE >
     * @param data to set to the form
     * @throws CoeusException
     */
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        String desc;
        if(getFunctionType()  ==   TypeConstants.NEW_MODE){
            int mapId = ((Integer)getNextMapId()).intValue();
            desc = mapDetailForm.txtArDescription.getText().trim();
            if(desc  ==   null||desc.equals(EMPTY_STRING)){
                mapHeaderBean.setMapDescription(EMPTY_STRING);
            }
            mapDetailForm.txtMap.setText(""+mapId);;
            //Commented for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
            // Removed the direct and indirect options from gui and set the default
            // type of the map as INDIRECT
            //mapDetailForm.rdBtnIndirect.setSelected(true);
            //Commented for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
            mapDetailForm.defaultFlagType.setSelected(false);
            mapDetailForm.txtUnitNumber.setText(mapHeaderBean.getUnitNumber());
            mapHeaderBean.setMapId(mapId);
            mapHeaderBean.setDefaultMapFlag(mapDetailForm.defaultFlagType.isSelected());
            mapHeaderBean.setMapType("I");
            mapHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
        }else{
            cvData = getUnitMapHeaderandDetailsData(mapHeaderBean.getMapId());
            
        }
        if(cvData != null&&cvData.size()  >  0){
            cvHeaderData = (CoeusVector)cvData.get(0);
            cvDetailsData = (CoeusVector)cvData.get(1);
            if(cvHeaderData != null&&cvHeaderData.size() > 0){
                if(getFunctionType() != TypeConstants.NEW_MODE){
                    mapHeaderBean = (MapHeaderBean)cvHeaderData.get(0);
                    mapDetailForm.txtMap.setText(""+mapHeaderBean.getMapId());;
                    mapDetailForm.txtUnitNumber.setText(mapHeaderBean.getUnitNumber());
                    mapDetailForm.txtArDescription.setText(mapHeaderBean.getMapDescription().trim());
                    //Commented for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
                    // Removed the direct and indirect options from gui and set the default
                    // type of the map as INDIRECT
                    /*if(mapHeaderBean.getMapType().trim().equals(INDIRECT_TYPE)){
                        mapDetailForm.rdBtnIndirect.setSelected(true);
                    }else if(mapHeaderBean.getMapType().trim().equals(DIRECT_TYPE)){
                        mapDetailForm.rdBtnDirect.setSelected(true);
                    }*/
                    //Commented for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
                    mapDetailForm.defaultFlagType.setSelected(mapHeaderBean.isDefaultMapFlag());
                }
            }
            if(cvDetailsData != null && cvDetailsData.size()  >  0){
                cvDetailsData = setValuesToBean(cvDetailsData);
                //Added for Coeus4.3 enhancement PT ID 2785 - Routing enhancement - start
                //Populates the qualifier description into the beans
                populateQualifierDescription(cvDetailsData);
                //Added for Coeus4.3 enhancement  PT ID 2785- Routing enhancement - end
                mapsDetailTableModel.setData(cvDetailsData);
                mapsDetailTableModel.fireTableDataChanged();
                resetRowHeights(cvDetailsData);
                
            }
        }
        
    }
    /** Set the row heights for the report details table based on the grouping
     * @param cvData
     */
    private void resetRowHeights(CoeusVector cvData) {
        if( cvData  ==   null || cvData.size()  ==   0 ){
            return ;
        }
        String []category = new String[3];
        category[0]="";
        boolean newGroup;
        for(int index = 0; index < cvData.size(); index++) {
            MapDetailsBean mapDetailsBean = (MapDetailsBean)cvData.get(index);
            if( !category[0].trim().equals(""+mapDetailsBean.getLevelNumber())){
                newGroup=true;
            }else {
                if(category[0].trim().equals(EMPTY_STRING)){
                    newGroup = true;
                }else{
                    newGroup = false;
                }
            }
            
            if( newGroup ){
                mapDetailForm.tblMap.setRowHeight(index, INCREASED_ROW_HEIGHT);
                category[0] = ""+mapDetailsBean.getLevelNumber();
            }
        }
    }
    /**
     *Method to sorting the vector according to level number, stop number and primary
     *flag
     *@param cvDetailsData
     */
    private CoeusVector setValuesToBean(CoeusVector cvDetailsData){
        if(cvDetailsData  ==   null||cvDetailsData.size()  ==   0){
            return null;
        }
        CoeusVector resultantVector = new CoeusVector();
        CoeusVector cvTemp = new CoeusVector();
        cvTemp.addAll(cvDetailsData);
        String fieldNames[] = {"levelNumber","stopNumber"};
        cvTemp.sort(fieldNames,true);
        int levelNo = ((MapDetailsBean)cvTemp.lastElement()).getLevelNumber();
        
        for(int index=0;index<levelNo;index++){
            NotEquals notequals = new NotEquals("levelNumber",new Integer(index));
            cvTemp=cvTemp.filter(notequals);
            
            int levelNom = ((MapDetailsBean)cvTemp.get(0)).getLevelNumber();
            Equals equals = new Equals("levelNumber",new Integer(levelNom));
            CoeusVector filterData = cvTemp.filter(equals);
            while(filterData.size() > 0){
                int stopnumber = ((MapDetailsBean)filterData.get(0)).getStopNumber();
                Equals eq = new Equals("stopNumber",new Integer(stopnumber));
                CoeusVector cvSorted = filterData.filter(eq);
                CoeusVector cvmainSort = new CoeusVector();
                Equals flagequals = new Equals("primayApproverFlag",true);
                cvmainSort=cvSorted.filter(flagequals);
                if(cvmainSort != null&&cvmainSort.size()  >  0){
                    resultantVector.addAll(cvmainSort);
                }
                NotEquals noequals = new NotEquals("primayApproverFlag",true);
                cvmainSort=cvSorted.filter(noequals);
                if(cvmainSort != null&&cvmainSort.size()  >  0){
                    cvmainSort.sort("userId",true);
                    resultantVector.addAll(cvmainSort);
                }
                Equals leveleq = new Equals("levelNumber",new Integer(levelNom));
                NotEquals stopNoEq = new NotEquals("stopNumber",new Integer(stopnumber));
                And anCond = new And(leveleq,stopNoEq);
                filterData = filterData.filter(anCond);
            }
        }
        return resultantVector;
    }
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if(!headerValidate()){
            return false;
        }
        boolean atLeastOnePrimaryFlag = false;
        int rowCount = mapDetailForm.tblMap.getRowCount();
        if(rowCount <= 0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    ATLEAST_ONE_STOP));
            return false;
        }
        CoeusVector mapDetailsVector = null;
        MapDetailsBean mapDetailsBeans = (MapDetailsBean)cvDetailsData.get(0);
        if(mapDetailsBeans != null) {
            mapDetailsVector = cvDetailsData;
        }
        for(int index = 0;index < cvDetailsData.size();index ++) {
            int level = 0;
            int stopLevel = 0;
            int mapId = 0;
            MapDetailsBean mapDetailsBean = (MapDetailsBean)mapDetailsVector.get(index);
            level = mapDetailsBean.getLevelNumber();
            mapId = mapDetailsBean.getMapId();
            stopLevel = mapDetailsBean.getStopNumber();
            CoeusVector eachStopDataPrimaryApprovers = filterVectorByLevel(mapDetailsVector,level,stopLevel,true,mapId);
            if(eachStopDataPrimaryApprovers  ==   null||
                    eachStopDataPrimaryApprovers.size()  ==   0){
                atLeastOnePrimaryFlag=true;
                break;
            }
        }
        if(atLeastOnePrimaryFlag){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    ATLEAST_ONE_PRIMARY));
            return false;
        }
        return true;
    }
    /**
     *Method to clean up the register components
     */
    public void cleanUp(){
        mapHeaderBean = null;
        mapsDetailTableModel = null;
        mapsDetailTableCellRenderer = null;
        mapsDetailTableCellEditor=null;
        cvDeletedData = null;
        cvHeaderData = null;
        cvDetailsData = null;
        cvData = null;
        mapDetailForm = null;
        dlgMapDetail = null;
        if(stopDetailsController!=null){
            stopDetailsController.cleanUp();
        }
        
    }
    /** Method to perform Add button action */
    private void performAddAction() throws CoeusException{
        try{
            if(headerValidate()){
                MapDetailsBean mapDetailsBean;
                int rowCount=mapDetailForm.tblMap.getRowCount();
                if(rowCount  ==   0){
                    mapDetailsBean=new MapDetailsBean();
                    mapDetailsBean.setLevelNumber(0);
                    mapDetailsBean.setStopNumber(0);
                    mapDetailsBean.setMapDescription(
                            mapDetailForm.txtArDescription.getText().trim());
                    
                    mapDetailsBean.setUnitNumber(mapDetailForm.txtUnitNumber.getText());
                }else{
                    mapsDetailTableCellEditor.stopCellEditing();
                    int selRow = mapDetailForm.tblMap.getSelectedRow();
                    if(selRow < 0){
                        mapDetailsBean
                                =(MapDetailsBean)mapsDetailTableModel.getData().get(0);
                        mapDetailsBean.setMapDescription(
                                mapDetailForm.txtArDescription.getText().trim());
                    }else{
                        mapDetailsBean
                                =(MapDetailsBean)mapsDetailTableModel.getData().get(selRow);
                        mapDetailsBean.setMapDescription(
                                mapDetailForm.txtArDescription.getText().trim());
                    }
                }
                CoeusVector cvAdd = new CoeusVector();
                MapDetailsBean mapBean = new MapDetailsBean();
                try{
                    if(cvDetailsData !=null && cvDetailsData.size() > 0){
                        cvAdd = (CoeusVector) ObjectCloner.deepCopy(cvDetailsData);
                    }else{
                        cvAdd = null;
                    }
                    mapBean = (MapDetailsBean)ObjectCloner.deepCopy(mapDetailsBean);
                }catch(Exception exp){
                    exp.printStackTrace();
                }
                
                stopDetailsController
                        = new StopDetailsController(mapBean,cvAdd);
                CoeusVector cvAddData  = stopDetailsController.displayData();
                //Modified if condition Coeus 4.3 Routing enhancement -PT ID:2785
                //Only if saveRequired the followingcode is executed
                if( stopDetailsController.isSaveRequired() && cvAddData != null && cvAddData.size()  >  0){
                    MapDetailsBean bean = (MapDetailsBean)cvAddData.get(0);
                    bean.setAcType(TypeConstants.INSERT_RECORD);
                    
                    if(cvDetailsData == null){
                        cvDetailsData = new CoeusVector();
                    }
                    cvDetailsData.addElement(bean);
                    modified = true;
                    cvDetailsData = setValuesToBean(cvDetailsData);
                    mapsDetailTableModel.setData(cvDetailsData);
                    mapsDetailTableModel.fireTableDataChanged();
                    resetRowHeights(cvDetailsData);
                    
                }
            }
        }catch(edu.mit.coeus.exception.CoeusException cu){
            cu.printStackTrace();
        }
    }
    /** Method to perform Delete Button Action */
    private void performDeleteAction() throws CoeusException {
        int rowCount = mapDetailForm.tblMap.getRowCount();
        if(rowCount <= 0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROW_TO_DELETE));
            return;
        }
        if(headerValidate()){
            int selRow = mapDetailForm.tblMap.getSelectedRow();
            if(selRow  ==   -1){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW_DELETE));
                return ;
            }
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_ROW_CONFIRM),
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
            if(selection  ==   CoeusOptionPane.SELECTION_NO) {
                return ;
            }
            modified = true;
            mapsDetailTableCellEditor.stopCellEditing();
            MapDetailsBean mapDetailsBean=
                    (MapDetailsBean)cvDetailsData.get(selRow);
            boolean isPrimary = mapDetailsBean.isPrimayApproverFlag();
            int levelNo = mapDetailsBean.getLevelNumber();
            int stopnp = mapDetailsBean.getStopNumber();
            Equals eq = new Equals("levelNumber",new Integer(levelNo));
            Equals eqstp = new Equals("stopNumber",new Integer(stopnp));
            And and = new And(eq,eqstp);
            CoeusVector cvFilter = cvDetailsData.filter(and);
            if(cvFilter != null&&cvFilter.size()  >  0){
                if(cvFilter.size()  ==   1){
                    if(mapDetailsBean.getAcType()  ==   TypeConstants.INSERT_RECORD){
                        cvDetailsData.remove(selRow);
                        
                    }else{
                        cvDetailsData.remove(selRow);
                        mapDetailsBean.setAcType(TypeConstants.DELETE_RECORD);
                        cvDeletedData.add(mapDetailsBean);
                    }
                    modified = true;
                    mapsDetailTableModel.fireTableRowsDeleted(selRow,selRow);
                    
                    //Bug Fix: Resetting the row size after delete Start
                    resetRowHeights(cvDetailsData);
                    //Bug Fix: Setting the row size after delete End
                    
                    int levno = ((MapDetailsBean)cvFilter.get(0)).getLevelNumber();
                    Equals eqlev = new Equals("levelNumber",new Integer(levno));
                    CoeusVector data = cvDetailsData.filter(eqlev);
                    if(data != null && data.size()  >  0){
                        return;
                    }
                    for(int index = 0;index < cvDetailsData.size();index++){
                        MapDetailsBean bean = (MapDetailsBean)cvDetailsData.get(index);
                        if(bean.getLevelNumber() > levelNo){
                            bean.setLevelNumber(bean.getLevelNumber()-1);
                            if(bean.getAcType() != TypeConstants.INSERT_RECORD){
                                bean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                        }
                    }
                }else if(cvFilter.size() > 1){
                    if(cvDetailsData.size() !=0 && isPrimary ){
                        if(mapDetailsBean.getAcType()  ==   TypeConstants.INSERT_RECORD){
                            cvDetailsData.remove(selRow);
                            
                        }else{
                            cvDetailsData.remove(selRow);
                            mapDetailsBean.setAcType(TypeConstants.DELETE_RECORD);
                            cvDeletedData.add(mapDetailsBean);
                        }
                        modified = true;
                        mapsDetailTableModel.fireTableRowsDeleted(selRow,selRow);
                        MapDetailsBean mapBean=
                                (MapDetailsBean)cvDetailsData.get(selRow);
                        if((mapBean.getLevelNumber()  ==   levelNo) && (mapBean.getStopNumber()  ==   stopnp)) {
                            mapBean.setPrimayApproverFlag(true);
                            if(mapBean.getAcType() != TypeConstants.INSERT_RECORD){
                                mapBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                            
                        }
                    }else{
                        if(mapDetailsBean.getAcType()  ==   TypeConstants.INSERT_RECORD){
                            cvDetailsData.remove(selRow);
                        }else{
                            mapDetailsBean.setAcType(TypeConstants.DELETE_RECORD);
                            cvDeletedData.add(mapDetailsBean);
                            cvDetailsData.remove(selRow);
                        }
                        modified = true;
                        mapsDetailTableModel.fireTableRowsDeleted(selRow,selRow);
                    }
                }
            }
            cvDetailsData =  setValuesToBean(cvDetailsData);
            mapsDetailTableModel.setData(cvDetailsData);
            mapsDetailTableModel.fireTableDataChanged();
            resetRowHeights(cvDetailsData);
            if( cvDetailsData != null && cvDetailsData.size()  >  0 ){
                mapDetailForm.tblMap.setRowSelectionInterval(0, 0);
                mapDetailForm.tblMap.scrollRectToVisible(mapDetailForm.tblMap.getCellRect(
                        0 ,0, true));
            }
        }
    }
    /**
     * Method to perform Cancel button action
     *
     */
    private void performCancelAction() throws CoeusException{
        mapsDetailTableCellEditor.stopCellEditing();
        //Using for getting modified status
        MapHeaderBean mapHeaderBean = (MapHeaderBean)getFormData();
        if(modified){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_CHANGES),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    2);
            switch( option ) {
                case (CoeusOptionPane.SELECTION_YES):
                    mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    try{
                        
                        if(validate()){
                            saveFormData();
                            if(succesfulSave){
                                succesfulSave = false;
                            }
                            dlgMapDetail.dispose();
                        }
                        
                    }catch(Exception e){
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                        return;
                    }finally{
                        mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                    break;
                case(CoeusOptionPane.SELECTION_NO):
                    dlgMapDetail.dispose();
                    mapDetailForm = null;
                    break;
                default:
                    break;
                    
            }
        }else{
            dlgMapDetail.dispose();
            mapDetailForm = null;
        }
    }
    /** Method to validate Unit Header data */
    private boolean headerValidate(){
        if(mapDetailForm.txtArDescription.getText() ==  null||
                mapDetailForm.txtArDescription.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    MAP_DESC));
            mapDetailForm.txtArDescription.requestFocusInWindow();
            return false;
        }
        //Commented for Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - start
        // Removed the direct and indirect options from gui and set the default
        // type of the map as INDIRECT
       /* else if(mapDetailForm.rdBtnDirect ==  null
                ||mapDetailForm.rdBtnDirect.equals(EMPTY_STRING)
                ||mapDetailForm.rdBtnIndirect ==  null
                ||mapDetailForm.rdBtnIndirect.equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    MAP_TYPE));
            return false;
        }*/
        //Commented for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
        else if(mapDetailForm.defaultFlagType ==  null||
                mapDetailForm.defaultFlagType.equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    MAP_OPTION));
            return false;
            
        }
        return true;
    }
    /*
     *Method for getting Maps Details from server
     */
    /**
     * Method to get header and map details data
     * @param mapId
     * @throws CoeusException
     * @return CoeusVector containing header and details
     */
    public CoeusVector getUnitMapHeaderandDetailsData(int mapId) throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + MAPS_SERVLET;
        request.setFunctionType(GET_MAPS_DETAIL_DATA);
        request.setDataObject(new Integer(mapId));
        
        AppletServletCommunicator comm =
                new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return cvData;
    }
    /**
     * Method for getting next Map Id
     * @return integer object
     */
    public Integer getNextMapId() throws CoeusException{
        Integer mapId = null;
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + MAPS_SERVLET;
        
        request.setFunctionType(GET_MAP_ID);
        
        AppletServletCommunicator comm =
                new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                mapId = (Integer)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return mapId;
    }
    /**
     * Method to check primary flag is true in every level and stop number
     * @param cvMapBeans
     * @param startLevel
     * @param stopLevel
     * @param isPrimaryApprover
     * @param mapId
     * @return coeusvector
     */
    public CoeusVector filterVectorByLevel(CoeusVector cvMapBeans,int startLevel,int stopLevel,boolean isPrimaryApprover,int mapId) {
        
        MapDetailsBean mapDetailsBean =  null;
        int mapIDOfBean = 0;
        int startLevelOfBean = 0;
        int stopLevelOfBean = 0;
        boolean isPrimaryApproverOfBean = false;
        CoeusVector filteredVector = new CoeusVector();
        try {
            for(int indexData = 0; indexData < cvMapBeans.size();indexData ++) {
                mapDetailsBean =  (MapDetailsBean) cvMapBeans.get(indexData);
                mapIDOfBean = mapDetailsBean.getMapId();
                startLevelOfBean = mapDetailsBean.getLevelNumber();
                stopLevelOfBean = mapDetailsBean.getStopNumber();
                isPrimaryApproverOfBean = mapDetailsBean.isPrimayApproverFlag();
                
                if((startLevelOfBean  ==   startLevel) &&
                        (stopLevelOfBean  ==   stopLevel) &&
                        (mapIDOfBean  ==   mapId) &&
                        (isPrimaryApproverOfBean  ==   isPrimaryApprover )
                        ) {
                    //cvMapBeans.remove(unitMapDetailsBean);
                    filteredVector.add(mapDetailsBean);
                }
            }
            
        }catch(Exception exp) {
            exp.printStackTrace();
            filteredVector = null;
        }
        
        return filteredVector;
    }
    /**
     * Method to get events
     * @param e Event
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(mapDetailForm.btnAdd)){
                performAddAction();
                
            }else if(source.equals(mapDetailForm.btnDelete)){
                performDeleteAction();
                
            }else if(source.equals(mapDetailForm.btnOk)){
                performOKAction();
                
            }else if(source.equals(mapDetailForm.btnCancel)){
                performCancelAction();
            }
            //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
            //Shows the window to add role to the map
//            else if(source.equals(mapDetailForm.btnAddRole)){
//                performAddRoleAction();
//            }
            //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
        }catch(CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
    }
    private void performOKAction() throws CoeusException{
        mapsDetailTableCellEditor.stopCellEditing();
        mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        try{
            if(validate()){
                saveFormData();
                if(succesfulSave){
                    succesfulSave = false;
                }
                dlgMapDetail.dispose();
            }
            
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
            return;
        }finally{
            mapDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    /**
     * Sets the qualifier description into the mapDetailsBean in the vector cvMapDetails
     * @param cvMapDetails - vector with the MapDetailsBean objects
     */
    public void populateQualifierDescription(CoeusVector cvMapDetails){
        if(cvMapDetails!=null){
            MapDetailsBean mapDetailsBean = null;
            for(int i =0; i<cvMapDetails.size(); i++){
                mapDetailsBean = (MapDetailsBean)cvMapDetails.get(i);
                if(mapDetailsBean.getQualifierCode()!=null && !mapDetailsBean.getQualifierCode().equals("-1") ){
                    mapDetailsBean.setQualifierDescription(
                            getQualDescForCode(mapDetailsBean.getRoleCode(),
                            mapDetailsBean.getQualifierCode()));
                }
            }
        }
    }
    
    /**
     * Returns the qualifier description for a particular roleCode and qualifier code
     * @param roleCode - role type code
     * @param qualifierCode - qualifier code of the bean
     */
    public String getQualDescForCode(String roleCode, String qualifierCode){
        if(roleQulaifiersMap!=null){
            if(roleQulaifiersMap.containsKey(roleCode)){
                HashMap qualifierMap = (HashMap)roleQulaifiersMap.get(roleCode);
                if(qualifierMap.get(qualifierCode) == null){
                    return "";
                }else{
                    return qualifierMap.get(qualifierCode).toString();
                }
            }
        }
        return "";
    }
   
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    
    /** Table for Map Detail Window */
    public class MapsDetailTableModel extends DefaultTableModel{
        private CoeusVector cvMapsDetail;
        String[] colNames = {"Sequential Stop", "Level Number","Primary flag","Description"};
        Class[] colTypes = {String.class, String.class, String.class, String.class};
        
        /**
         * Constructor for table model
         */
        MapsDetailTableModel(){
        }
        /**
         *
         * @param cvMapsDetail
         */
        public void setData(CoeusVector cvMapsDetail){
            this.cvMapsDetail = cvMapsDetail;
        }
        /**
         *
         * @return CoeusVector
         */
        public CoeusVector getData(){
            return cvMapsDetail;
        }
        /**
         *
         * @return int
         */
        public int getRowCount() {
            if( cvMapsDetail  ==   null ){
                return 0;
            }else{
                return cvMapsDetail.size();
            }
        }
        /**
         *
         * @param rowIndex
         * @param columnIndex
         * @return Boolean
         */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
        /**
         *
         * @param aValue
         * @param rowIndex
         * @param columnIndex
         */
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if(cvMapsDetail  ==   null||cvMapsDetail.size() ==  0){
                return;
            }
            MapDetailsBean mapDetailsBean = (MapDetailsBean)cvMapsDetail.get(rowIndex);
        }
        
        /**
         *
         * @param rowIndex
         * @param columnIndex
         * @return Object
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            MapDetailsBean mapDetailsBean = (MapDetailsBean)
            cvMapsDetail.get(rowIndex);
            return mapDetailsBean;
        }
        
        /**
         *
         * @param columnIndex
         * @return String
         */
        public String getColumnName(int columnIndex) {
            return colNames[columnIndex];
        }
        
        /**
         *
         * @param columnIndex
         * @return Class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }
        
        /**
         *
         * @return int
         */
        public int getColumnCount() {
            return 1;
        }
    }
    /**
     * Method for focus traversing from table to components
     */
    public void setTableKeyTraversal(){
        javax.swing.InputMap im = mapDetailForm.tblMap.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        final Action oldTabAction = mapDetailForm.tblMap.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            int row = 0;
            int column =0;
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                boolean selectionOut=false;
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                if((rowCount-1) ==  row && column ==  (columnCount-1)){
                    selectionOut = true;
                    mapDetailForm.btnAdd.requestFocusInWindow();
                }
                if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column = 0;
                    mapDetailForm.btnAdd.requestFocusInWindow();
                    return ;
                }
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    if (column  ==   columnCount) {
                        column = 0;
                        row +=1;
                    }
                    if (row  ==   rowCount) {
                        row = 0;
                    }
                    if (row  ==   table.getSelectedRow()
                    && column  ==   table.getSelectedColumn()) {
                        break;
                    }
                }
                if(!selectionOut){
                    table.changeSelection(row, column, false, false);
                }
            }
        };
        mapDetailForm.tblMap.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction = mapDetailForm.tblMap.getActionMap().get(im.get(shiftTab));
        Action tabShiftAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    if (row  ==   table.getSelectedRow()
                    && column  ==   table.getSelectedColumn()) {
                        break;
                    }
                }
                table.changeSelection(row, column, false, false);
            }
        };
        mapDetailForm.tblMap.getActionMap().put(im.get(shiftTab), tabShiftAction);
    }
    /** class for Set the editor */
    class MapsDetailTableCellEditor extends DefaultCellEditor implements MouseListener,KeyListener{
        private JPanel pnlGroup;
        //To display the grouping values
        private JPanel pnlNewGroup;
        private JLabel lblUserName,lblDescription,lblValue;
        private boolean newGroup;
        private String oldBean[] = new String[3];
        private JCheckBox chkPrimaryFlag;
        private JPanel pnlSinglerow,pnlWithGroup;
        private MapDetailsBean mapDetailsBean;
        public MapsDetailTableCellEditor(){
            super(new JComboBox());
            pnlGroup = new JPanel(new GridLayout(2, 1));
            pnlSinglerow = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
            pnlWithGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
            pnlNewGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
            chkPrimaryFlag = new JCheckBox();
            chkPrimaryFlag.addMouseListener(this);
            mapDetailForm.tblMap.addKeyListener(this);
            
            pnlGroup.addKeyListener(this);
            pnlSinglerow.addKeyListener(this);
            pnlWithGroup.addKeyListener(this);
            pnlNewGroup.addKeyListener(this);
            lblValue = new JLabel();
            lblValue.setOpaque(true);
            lblValue.setHorizontalAlignment(JLabel.LEFT);
            lblValue.setMinimumSize(new Dimension(70,15));
            lblValue.setMaximumSize(new Dimension(70,15));
            lblValue.setPreferredSize(new Dimension(70,15));
            lblUserName = new JLabel();
            lblUserName.setOpaque(true);
            lblUserName.setHorizontalAlignment(JLabel.LEFT);
            //UserId to UserName Enhancement - Start
//            lblUserName.setMinimumSize(new Dimension(70,15));
//            lblUserName.setMaximumSize(new Dimension(70,15));
//            lblUserName.setPreferredSize(new Dimension(70,15));
            //Modified for Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - start
            //Increased the width of the userName lablel
//            lblUserName.setMinimumSize(new Dimension(120,15));
//            lblUserName.setMaximumSize(new Dimension(120,15));
//            lblUserName.setPreferredSize(new Dimension(120,15));
            lblUserName.setMinimumSize(new Dimension(195,15));
            lblUserName.setMaximumSize(new Dimension(195,15));
            lblUserName.setPreferredSize(new Dimension(195,15));
            //Modified for Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - end
            //UserId to UserName Enhancement - End
            chkPrimaryFlag.setMinimumSize(new Dimension(30,15));
            chkPrimaryFlag.setMaximumSize(new Dimension(30,15));
            chkPrimaryFlag.setPreferredSize(new Dimension(30,15));
            
            lblDescription = new JLabel();
            lblDescription.setOpaque(true);
            lblDescription.setHorizontalAlignment(JLabel.LEFT);
            pnlGroup.add(pnlNewGroup);
            pnlGroup.setBackground(Color.WHITE);
            pnlNewGroup.setBackground(Color.WHITE);
            pnlNewGroup.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            pnlGroup.add(pnlWithGroup);
            
        }
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if(row > 0){
                mapDetailsBean=(MapDetailsBean)mapsDetailTableModel.getData().get(row-1);
                oldBean[0] = ""+mapDetailsBean.getLevelNumber();
            }else{
                oldBean[0] = "";
            }
            mapDetailsBean=(MapDetailsBean)mapsDetailTableModel.getData().get(row);
            if(!oldBean[0].equals(""+mapDetailsBean.getLevelNumber())){
                newGroup = true;
            }else{
                if(oldBean[0].equals(EMPTY_STRING)){
                    newGroup = true;
                }else{
                    newGroup = false;
                }
            }
            if(mapDetailsBean.isPrimayApproverFlag()){
                mapDetailForm.tblMap.setRowSelectionInterval(row, row);
                chkPrimaryFlag.setSelected(true);
                lblValue.setFont(CoeusFontFactory.getNormalFont());
                lblValue.setHorizontalAlignment(JLabel.CENTER);
                lblValue.setText("Approve By");
                lblValue.setForeground(Color.RED);
                lblUserName.setFont(CoeusFontFactory.getNormalFont());
//                lblUserName.setText(mapDetailsBean.getUserId());
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username
                 */
                lblUserName.setText(mapDetailsBean.getUpdateUserName());
                // UserId to UserName Enhancement - End
                lblDescription.setFont(CoeusFontFactory.getNormalFont());
                lblDescription.setText(mapDetailsBean.getDetailDescription());
            }else{
                chkPrimaryFlag.setSelected(false);
                lblValue.setFont(new Font("MS Sans Serif", 2, 11));
                lblValue.setHorizontalAlignment(JLabel.RIGHT);
                lblValue.setText("or   ");
                lblValue.setForeground(Color.BLUE);
                lblUserName.setFont(new Font("MS Sans Serif", 2, 11));
//                lblUserName.setText(mapDetailsBean.getUserId());
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username
                 */
                lblUserName.setText(mapDetailsBean.getUpdateUserName());
                // UserId to UserName Enhancement - End
                lblDescription.setFont(new Font("MS Sans Serif", 2, 11));
                lblDescription.setText(mapDetailsBean.getDetailDescription());
            }
            //Added for Coeus 4.3 enhancement PT ID 2785 - Routing Enhancement - Start
            //Set the text according to the approver type role or role qualifier
            if(mapDetailsBean.isRoleType()){
                if(mapDetailsBean == null ||
                        mapDetailsBean.getQualifierCode().equals("") ||
                        mapDetailsBean.getQualifierCode().equals("-1")){
                    lblUserName.setText(mapDetailsBean.getRoleDescription());
                }else{
                    lblUserName.setText(mapDetailsBean.getRoleDescription() + 
                            "(" +mapDetailsBean.getQualifierDescription() + ")" );
                }
            }
            //Added for Coeus 4.3 enhancement PT ID 2785 - Routing Enhancement - End
            if(isSelected) {
                lblValue.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                lblValue.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                lblUserName.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                lblUserName.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                lblDescription.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                lblDescription.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                chkPrimaryFlag.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                chkPrimaryFlag.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                pnlWithGroup.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                pnlSinglerow.setBackground(mapDetailForm.tblMap.getSelectionBackground());
            }else {
                if(lblValue.getText()!=null){
                    String str=lblValue.getText();
                    if(str.equals("or   ")){
                        lblValue.setForeground(Color.BLUE);
                    }else{
                        lblValue.setForeground(Color.RED);
                    }
                }
                lblValue.setBackground(Color.WHITE);
                lblUserName.setForeground(Color.BLUE);
                lblUserName.setBackground(Color.WHITE);
                lblDescription.setForeground(Color.BLACK);
                lblDescription.setBackground(Color.WHITE);
                chkPrimaryFlag.setForeground(Color.BLACK);
                chkPrimaryFlag.setBackground(Color.WHITE);
                pnlWithGroup.setBackground(Color.WHITE);
                pnlSinglerow.setBackground(Color.WHITE);
            }
            
            if(newGroup){
                String[] beanValues = {"Sequential Stop:",""+mapDetailsBean.getLevelNumber(),"Primary","Description"};
                pnlNewGroup.removeAll();
                for( int index = 0; index < beanValues.length; index++ ){
                    JLabel lblGroup = new JLabel(beanValues[index]);
                    lblGroup.setFont(CoeusFontFactory.getNormalFont());
                    lblGroup.setForeground(Color.BLUE);
                    //UserId to UserName Enhancement - Start
/*                    if(index ==  0){
                        lblGroup.setMinimumSize(new Dimension(90,15));
                        lblGroup.setMaximumSize(new Dimension(90,15));
                        lblGroup.setPreferredSize(new Dimension(90,15));
                    }else if(index ==  2){
                        lblGroup.setMinimumSize(new Dimension(40,15));
                        lblGroup.setMaximumSize(new Dimension(40,15));
                        lblGroup.setPreferredSize(new Dimension(40,15));
                    }else{
                        lblGroup.setMinimumSize(new Dimension(55,15));
                        lblGroup.setMaximumSize(new Dimension(55,15));
                        lblGroup.setPreferredSize(new Dimension(55,15));
                    }
 */
                    if(index ==  0){
                        lblGroup.setMinimumSize(new Dimension(90,15));
                        lblGroup.setMaximumSize(new Dimension(90,15));
                        lblGroup.setPreferredSize(new Dimension(90,15));
                    }
                    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
                    //Set the size of the label to 120 when used to show the approver name
                    else if(index ==  1){
                        lblGroup.setMinimumSize(new Dimension(180,15));
                        lblGroup.setMaximumSize(new Dimension(180,15));
                        lblGroup.setPreferredSize(new Dimension(180,15));
                    }
                    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
                    else if(index ==  2){
                        lblGroup.setMinimumSize(new Dimension(45,15));
                        lblGroup.setMaximumSize(new Dimension(45,15));
                        lblGroup.setPreferredSize(new Dimension(45,15));
                    }else{
                        lblGroup.setMinimumSize(new Dimension(75,15));
                        lblGroup.setMaximumSize(new Dimension(75,15));
                        lblGroup.setPreferredSize(new Dimension(75,15));
                    }
                    //UserId to UserName Enhancement - End
                    pnlNewGroup.add(lblGroup);
                }
                pnlWithGroup.removeAll();
                pnlWithGroup.add(lblValue);
                pnlWithGroup.add(lblUserName);
                pnlWithGroup.add(chkPrimaryFlag);
                pnlWithGroup.add(lblDescription);
                
            }else if(!newGroup){
                pnlSinglerow.removeAll();
                pnlSinglerow.add(lblValue);
                pnlSinglerow.add(lblUserName);
                pnlSinglerow.add(chkPrimaryFlag);
                pnlSinglerow.add(lblDescription);
            }
            if(newGroup ){
                return pnlGroup;
            }else{
                return pnlSinglerow;
            }
        }
        public void mouseClicked(java.awt.event.MouseEvent e) {
        }
        
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }
        
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
        
        public void mousePressed(java.awt.event.MouseEvent e) {
        }
        
        public void mouseReleased(java.awt.event.MouseEvent e) {
            int selRow = mapDetailForm.tblMap.getSelectedRow();
            if(e.getSource() instanceof JCheckBox  ){
                CoeusVector cvFilterData = new CoeusVector();
                boolean rowFound = false;
                mapsDetailTableCellEditor.stopCellEditing();
                if(selRow  !=  -1){
                    MapDetailsBean mapDetailsBean = (MapDetailsBean)cvDetailsData.get(selRow);
                    int levelNumber = mapDetailsBean.getLevelNumber();
                    int stopNumber = mapDetailsBean.getStopNumber();
                    Equals levelNumEquals = new Equals("levelNumber", new Integer(levelNumber));
                    Equals stopNumEquals = new Equals("stopNumber", new Integer(stopNumber));
                    And levelNumAndStopNumEquals = new And(levelNumEquals, stopNumEquals);
                    cvFilterData = cvDetailsData.filter(levelNumAndStopNumEquals);
                    if(cvFilterData.size()   >   1){
                        for(int i=0;i<cvDetailsData.size();i++){
                            MapDetailsBean dataBean = (MapDetailsBean)cvDetailsData.get(i);
                            if(dataBean.getLevelNumber()  ==   levelNumber &&
                                    dataBean.getStopNumber()  ==   stopNumber&&
                                    dataBean.isPrimayApproverFlag() ){
                                if(dataBean  != mapDetailsBean) {
                                    mapDetailsBean.setPrimayApproverFlag(true);
                                    dataBean.setPrimayApproverFlag(false);
                                    if(mapDetailsBean.getAcType() != TypeConstants.INSERT_RECORD){
                                        mapDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                                        
                                    }
                                    if(dataBean.getAcType() != TypeConstants.INSERT_RECORD){
                                        dataBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    }
                                    modified = true;
                                    cvDetailsData = setValuesToBean(cvDetailsData);
                                    mapsDetailTableModel.setData(cvDetailsData);
                                    mapsDetailTableModel.fireTableDataChanged();
                                    resetRowHeights(cvDetailsData);
                                    mapDetailForm.tblMap.setRowSelectionInterval(i,i);
                                    selRow = i;
                                } else {
                                    mapDetailsBean.setPrimayApproverFlag(false);
                                    if(mapDetailsBean.getAcType() != TypeConstants.INSERT_RECORD){
                                        mapDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    }
                                    modified = true;
                                }
                                rowFound = true;
                            }
                        }
                    }
                    if(!rowFound){
                        CoeusVector cvData = new CoeusVector();
                        MapDetailsBean dtBean = new MapDetailsBean();
                        MapDetailsBean mapBean = (MapDetailsBean)cvDetailsData.get(selRow);
                        for(int i=0;i<cvDetailsData.size();i++){
                            dtBean = (MapDetailsBean)cvDetailsData.get(i);
                            if(dtBean.getLevelNumber()  ==   mapBean.getLevelNumber()
                            && dtBean.getStopNumber()  ==   mapBean.getStopNumber()){
                                cvData.addElement(dtBean);
                            }
                        }
                        if(cvData.size()  >  0){
                            mapDetailsBean.setPrimayApproverFlag(true);
                            if(mapDetailsBean.getAcType() != TypeConstants.INSERT_RECORD){
                                mapDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                            modified=true;
                            cvDetailsData=setValuesToBean(cvDetailsData);
                            mapsDetailTableModel.setData(cvDetailsData);
                            mapDetailForm.tblMap.setRowSelectionInterval(selRow,selRow);
                            selRow = selRow;
                            mapsDetailTableModel.fireTableDataChanged();
                            resetRowHeights(cvDetailsData);
                        }
                    }
                }
            }
            mapDetailForm.tblMap.setRowSelectionInterval(selRow,selRow);
        }
        public void keyPressed(KeyEvent e) {
            int selRow = mapDetailForm.tblMap.getSelectedRow();
            if(e.getKeyCode()  ==   KeyEvent.VK_SPACE ){
                CoeusVector cvFilterData = new CoeusVector();
                boolean rowFound = false;
                mapsDetailTableCellEditor.stopCellEditing();
                if(selRow  !=  -1){
                    MapDetailsBean mapDetailsBean = (MapDetailsBean)cvDetailsData.get(selRow);
                    int levelNumber = mapDetailsBean.getLevelNumber();
                    int stopNumber = mapDetailsBean.getStopNumber();
                    Equals levelNumEquals = new Equals("levelNumber", new Integer(levelNumber));
                    Equals stopNumEquals = new Equals("stopNumber", new Integer(stopNumber));
                    And levelNumAndStopNumEquals = new And(levelNumEquals, stopNumEquals);
                    cvFilterData = cvDetailsData.filter(levelNumAndStopNumEquals);
                    if(cvFilterData.size()   >   1){
                        for(int i=0;i<cvDetailsData.size();i++){
                            MapDetailsBean dataBean = (MapDetailsBean)cvDetailsData.get(i);
                            if(dataBean.getLevelNumber()  ==   levelNumber &&
                                    dataBean.getStopNumber()  ==   stopNumber&&
                                    dataBean.isPrimayApproverFlag() ){
                                if(dataBean  != mapDetailsBean) {
                                    mapDetailsBean.setPrimayApproverFlag(true);
                                    dataBean.setPrimayApproverFlag(false);
                                    if(mapDetailsBean.getAcType() != TypeConstants.INSERT_RECORD){
                                        mapDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                                        
                                    }
                                    if(dataBean.getAcType() != TypeConstants.INSERT_RECORD){
                                        dataBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    }
                                    modified = true;
                                    cvDetailsData = setValuesToBean(cvDetailsData);
                                    mapsDetailTableModel.setData(cvDetailsData);
                                    mapsDetailTableModel.fireTableDataChanged();
                                    resetRowHeights(cvDetailsData);
                                    mapDetailForm.tblMap.setRowSelectionInterval(i,i);
                                    selRow = i;
                                } else {
                                    
                                    mapDetailsBean.setPrimayApproverFlag(false);
                                    if(mapDetailsBean.getAcType() != TypeConstants.INSERT_RECORD){
                                        mapDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    }
                                    modified=true;
                                }
                                rowFound= true;
                            }
                        }
                    }
                    if(!rowFound){
                        CoeusVector cvData = new CoeusVector();
                        MapDetailsBean dtBean = new MapDetailsBean();
                        MapDetailsBean mapBean = (MapDetailsBean)cvDetailsData.get(selRow);
                        for(int i=0;i<cvDetailsData.size();i++){
                            dtBean = (MapDetailsBean)cvDetailsData.get(i);
                            if(dtBean.getLevelNumber()  ==   mapBean.getLevelNumber()
                            && dtBean.getStopNumber()  ==   mapBean.getStopNumber()){
                                cvData.addElement(dtBean);
                            }
                        }
                        if(cvData.size()  >  0){
                            mapDetailsBean.setPrimayApproverFlag(true);
                            if(mapDetailsBean.getAcType() != TypeConstants.INSERT_RECORD){
                                mapDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                            modified=true;
                            cvDetailsData=setValuesToBean(cvDetailsData);
                            mapsDetailTableModel.setData(cvDetailsData);
                            mapDetailForm.tblMap.setRowSelectionInterval(selRow,selRow);
                            selRow = selRow;
                            mapsDetailTableModel.fireTableDataChanged();
                            resetRowHeights(cvDetailsData);
                        }
                    }
                }
            }
            if(mapDetailForm.tblMap.getRowCount()  >  0){
                mapDetailForm.tblMap.setRowSelectionInterval(selRow,selRow);
            }
        }
        
        public void keyReleased(KeyEvent e) {
        }
        
        public void keyTyped(KeyEvent e) {
        }
        
    }
    /** renderer class for table Map detail */
    private class MapsDetailTableCellRenderer extends DefaultTableCellRenderer{
        private JPanel pnlGroup;
        private JPanel pnlNewGroup;
        private JLabel lblUserName,lblDescription,lblValue;
        private boolean newGroup;
        private String oldBean[] = new String[3];
        private JCheckBox chkPrimaryFlag;
        private JPanel pnlSinglerow,pnlWithGroup;
        private MapDetailsBean mapDetailsBean;
        public MapsDetailTableCellRenderer(){
            pnlGroup = new JPanel(new GridLayout(2, 1));
            pnlSinglerow = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
            pnlWithGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
            pnlNewGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
            chkPrimaryFlag = new JCheckBox();
            lblValue = new JLabel();
            lblValue.setOpaque(true);
            lblValue.setHorizontalAlignment(JLabel.LEFT);
            lblValue.setMinimumSize(new Dimension(70,15));
            lblValue.setMaximumSize(new Dimension(70,15));
            lblValue.setPreferredSize(new Dimension(70,15));
            lblUserName = new JLabel();
            lblUserName.setOpaque(true);
            lblUserName.setHorizontalAlignment(JLabel.LEFT);
            //UserId to UserName Enhancement - Start
//            lblUserName.setMinimumSize(new Dimension(70,15));
//            lblUserName.setMaximumSize(new Dimension(70,15));
//            lblUserName.setPreferredSize(new Dimension(70,15));
            //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
            //Increased the size of the username to 175
//            lblUserName.setMinimumSize(new Dimension(120,15));
//            lblUserName.setMaximumSize(new Dimension(120,15));
//            lblUserName.setPreferredSize(new Dimension(120,15));
            
            lblUserName.setMinimumSize(new Dimension(195,15));
            lblUserName.setMaximumSize(new Dimension(195,15));
            lblUserName.setPreferredSize(new Dimension(195,15));
            //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
            //UserId to UserName Enhancement - End
            
            chkPrimaryFlag.setMinimumSize(new Dimension(30,15));
            chkPrimaryFlag.setMaximumSize(new Dimension(30,15));
            chkPrimaryFlag.setPreferredSize(new Dimension(30,15));
            
            lblDescription = new JLabel();
            lblDescription.setOpaque(true);
            lblDescription.setHorizontalAlignment(JLabel.LEFT);
            pnlGroup.add(pnlNewGroup);
            pnlGroup.setBackground(Color.WHITE);
            pnlNewGroup.setBackground(Color.WHITE);
            pnlNewGroup.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            pnlGroup.add(pnlWithGroup);
            
        }
        public Component getTableCellRendererComponent(javax.swing.JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(row  >  0){
                mapDetailsBean=(MapDetailsBean)mapsDetailTableModel.getData().get(row-1);
                oldBean[0] = ""+mapDetailsBean.getLevelNumber();
                
            }else{
                oldBean[0] = "";
            }
            mapDetailsBean = (MapDetailsBean)mapsDetailTableModel.getData().get(row);
            if(!oldBean[0].equals(""+mapDetailsBean.getLevelNumber())){
                newGroup = true;
            }else{
                if(oldBean[0].equals(EMPTY_STRING)){
                    newGroup = true;
                }else{
                    newGroup = false;
                }
            }
            if(mapDetailsBean.isPrimayApproverFlag()){
                chkPrimaryFlag.setSelected(true);
                lblValue.setFont(CoeusFontFactory.getNormalFont());
                lblValue.setHorizontalAlignment(JLabel.CENTER);
                lblValue.setText("Approve By");
                // lblValue.setForeground(Color.RED);
                lblUserName.setFont(CoeusFontFactory.getNormalFont());
//                lblUserName.setText(mapDetailsBean.getUserId());
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username
                 */
                lblUserName.setText(mapDetailsBean.getUpdateUserName());
                //UserId to UserName Enhancement - End
                lblDescription.setFont(CoeusFontFactory.getNormalFont());
                lblDescription.setText(mapDetailsBean.getDetailDescription());
            }else{
                chkPrimaryFlag.setSelected(false);
                lblValue.setFont(new Font("MS Sans Serif", 2, 11));
                lblValue.setHorizontalAlignment(JLabel.RIGHT);
                lblValue.setText("or   ");
                //  lblValue.setForeground(Color.BLUE);
                lblUserName.setFont(new Font("MS Sans Serif", 2, 11));
//                lblUserName.setText(mapDetailsBean.getUserId());
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username
                 */
                lblUserName.setText(mapDetailsBean.getUpdateUserName());
                // UserId to UserName Enhancement - End
                lblDescription.setFont(new Font("MS Sans Serif", 2, 11));
                lblDescription.setText(mapDetailsBean.getDetailDescription());
            }
            
            //Added for Coeus 4.3 enhancement PT ID 2785 - Routing Enhancement - Start
            //Set the text according to the approver type role or role qualifier
            if(mapDetailsBean.isRoleType()){
                if(mapDetailsBean.getQualifierCode()==null ||
                        mapDetailsBean.getQualifierCode().equals("") ||
                        mapDetailsBean.getQualifierCode().equals("-1")){
                    lblUserName.setText(mapDetailsBean.getRoleDescription());
                }else{
                    lblUserName.setText(mapDetailsBean.getRoleDescription() + 
                            "(" +mapDetailsBean.getQualifierDescription() + ")" );
                }
            }
            //Added for Coeus 4.3 enhancement PT ID 2785 - Routing Enhancement - End
            
            if(isSelected) {
                lblValue.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                lblValue.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                lblUserName.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                lblUserName.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                lblDescription.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                lblDescription.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                chkPrimaryFlag.setForeground(mapDetailForm.tblMap.getSelectionForeground());
                chkPrimaryFlag.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                pnlWithGroup.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                pnlSinglerow.setBackground(mapDetailForm.tblMap.getSelectionBackground());
                
            }else {
                if(lblValue.getText()!=null){
                    String str=lblValue.getText();
                    if(str.equals("or   ")){
                        lblValue.setForeground(Color.BLUE);
                    }else{
                        lblValue.setForeground(Color.RED);
                    }
                }
                
                lblValue.setBackground(Color.WHITE);
                lblUserName.setForeground(Color.BLACK);
                lblUserName.setBackground(Color.WHITE);
                lblDescription.setForeground(Color.BLACK);
                lblDescription.setBackground(Color.WHITE);
                chkPrimaryFlag.setForeground(Color.BLACK);
                chkPrimaryFlag.setBackground(Color.WHITE);
                pnlWithGroup.setBackground(Color.WHITE);
                pnlSinglerow.setBackground(Color.WHITE);
            }
            
            
            if(newGroup){
                String[] beanValues = {"Sequential Stop:",""+mapDetailsBean.getLevelNumber(),"Primary","Description"};
                pnlNewGroup.removeAll();
                for( int index = 0; index < beanValues.length; index++ ){
                    JLabel lblGroup = new JLabel(beanValues[index]);
                    lblGroup.setFont(CoeusFontFactory.getNormalFont());
                    lblGroup.setForeground(Color.BLUE);
                    //UserId to UserName Enhancement - Start
/*                    if(index ==  0){
                        lblGroup.setMinimumSize(new Dimension(90,15));
                        lblGroup.setMaximumSize(new Dimension(90,15));
                        lblGroup.setPreferredSize(new Dimension(90,15));
                    }else if(index ==  2){
                        lblGroup.setMinimumSize(new Dimension(40,15));
                        lblGroup.setMaximumSize(new Dimension(40,15));
                        lblGroup.setPreferredSize(new Dimension(40,15));
 
                    }else{
                        lblGroup.setMinimumSize(new Dimension(55,15));
                        lblGroup.setMaximumSize(new Dimension(55,15));
                        lblGroup.setPreferredSize(new Dimension(55,15));
                    }
 */
                    if(index ==  0){
                        lblGroup.setMinimumSize(new Dimension(90,15));
                        lblGroup.setMaximumSize(new Dimension(90,15));
                        lblGroup.setPreferredSize(new Dimension(90,15));
                    }
                    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
                    //Set the size of the label to 120 when the approver name is shown
                    else if(index ==  1){
                        lblGroup.setMinimumSize(new Dimension(180,15));
                        lblGroup.setMaximumSize(new Dimension(180,15));
                        lblGroup.setPreferredSize(new Dimension(180,15));
                        lblGroup.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    //Added for Coeus 4.3 enhancement PT ID 2785- Routing enhancement - end
                    else if(index ==  2){
                        lblGroup.setMinimumSize(new Dimension(45,15));
                        lblGroup.setMaximumSize(new Dimension(45,15));
                        lblGroup.setPreferredSize(new Dimension(45,15));
                        
                    }else{
                        lblGroup.setMinimumSize(new Dimension(75,15));
                        lblGroup.setMaximumSize(new Dimension(75,15));
                        lblGroup.setPreferredSize(new Dimension(75,15));
                    }
                    //UserId to UserName Enhancement - End
                    pnlNewGroup.add(lblGroup);
                }
                pnlWithGroup.removeAll();
                pnlWithGroup.add(lblValue);
                pnlWithGroup.add(lblUserName);
                pnlWithGroup.add(chkPrimaryFlag);
                pnlWithGroup.add(lblDescription);
                
            }else if(!newGroup){
                pnlSinglerow.add(lblValue);
                pnlSinglerow.add(lblUserName);
                pnlSinglerow.add(chkPrimaryFlag);
                pnlSinglerow.add(lblDescription);
            }
            
            if(newGroup ){
                return pnlGroup;
            }else{
                return pnlSinglerow;
            }
        }
        
    }
    //Added for Coeus 4.3 enhancement  PT ID 2785 - Routing enhancement - start
    /**
     * Setter method for the property roleQualifiersMap
     * @param roleQualifierMap - Map containing the role as key and the hashmap with 
     *      qualifier details as value
     */
    public void setRoleQulaifiersMap(Map roleQulaifiersMap) {
        this.roleQulaifiersMap = roleQulaifiersMap;
    }
    //Added for Coeus 4.3 enhancement  PT ID 2785- Routing enhancement - end
}
