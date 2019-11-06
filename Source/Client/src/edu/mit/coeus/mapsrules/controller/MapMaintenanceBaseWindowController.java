/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * MapMaintenanceBaseWindowController.java
 *
 * Created on October 17, 2005, 2:05 PM
 */
/* PMD check performed, and commented unused imports and variables on 05-JUL-2007
 * by Leena
 */
package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.mapsrules.bean.MapDetailsBean;
import edu.mit.coeus.mapsrules.gui.MapMaintenanceBaseWindow;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.mapsrules.bean.MapHeaderBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  shijiv
 */
public class MapMaintenanceBaseWindowController extends Controller implements ActionListener,ListSelectionListener,VetoableChangeListener,MouseListener{
    private static final int COLUMN_ID = 0;
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    //Removed the column type from the table
    //private static final int COLUMN_TYPE = 1;
    private static final int COLUMN_DESCRIPTION = 1;
    //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    private static final String EMPTY_STRING = "";
    private static final char GET_MAP_DATA = 'B';
    private static final char GET_UNIT_MAP_DETAIL_DATA = 'E';
    private static final String APPROVEBY_LABEL ="Approve By";
    /** String constant OR LABEL */
    private static final String OR_LABEL ="Or       ";
    private static final char DELETE_UNIT_MAP='F';
    
    private MapMaintenanceBaseWindow mapMaintenanceBaseWindow;
    private CoeusAppletMDIForm mdiForm;
    private MapTableModel mapTableModel;
    private final String MAP_SERVLET ="/MapMaintainanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ MAP_SERVLET;
    private CoeusVector cvmapsData,cvMapDetails;
    boolean hasAddMapRight,hasModifyMapRight;
    private CoeusMessageResources coeusMessageResources;
    private String unitNumber="";
    private String unit = "";
    private String topLevelUnit ="";
    private boolean isOpenedFromUserHierarchy,closed;
    private boolean sortCodeAsc = true;
    private boolean isIdCol=true,isTypeCol,isDescriptionCol;
    int selectedRow = 0;
    private Dimension pnlSequentialStopRowMaxSize,pnlSequentialStopRowMinSize,pnlSequentialStopRowPrefSize,sptrSequentialStopTopMaxSize,
            sptrSequentialStopTopMinSize,sptrSequentialStopTopPrefSize,sptrSequentialStopMaxSize,sptrSequentialStopMinSize,sptrSequentialStopPrefSize,
            pnlApprobedByRowMaxSize,pnlApprobedByRowMinSize,pnlApprobedByRowPrefSize;
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    //Map that holds the role as key and value another hashMap with qualifierCode
    //as key qualifier description as value
    private Map roleQulaifiersMap;
    private static final char GET_QUALIFIER_LIST = 'Q';
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    
    /** Creates a new instance of MapMaintenanceBaseWindowController */
    public MapMaintenanceBaseWindowController(String topLevelUnit,boolean isOpenedFromUserHierarchy) {
        mdiForm = CoeusGuiConstants.getMDIForm();
        this.topLevelUnit= topLevelUnit;
        this.isOpenedFromUserHierarchy=isOpenedFromUserHierarchy;
        if(!isOpenedFromUserHierarchy) {
            this.unitNumber=topLevelUnit;
            this.unit=unitNumber;
        }else if(isOpenedFromUserHierarchy){
            String str = topLevelUnit.substring(0,topLevelUnit.indexOf(":"));
            this.unitNumber = str.trim();
            this.unit=topLevelUnit;
        }
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvMapDetails= new CoeusVector();
        initComponents();
        registerComponents();
        setTableColumn();
        try {
            setFormData(null);
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    public void initComponents() {
        mapMaintenanceBaseWindow= new MapMaintenanceBaseWindow(CoeusGuiConstants.MAPS_BASE_FRAME_TITLE+" "+unit,mdiForm);
        pnlSequentialStopRowMaxSize = mapMaintenanceBaseWindow.pnlSelectedMapListHeaderRow.getMaximumSize();
        pnlSequentialStopRowMinSize=mapMaintenanceBaseWindow.pnlSelectedMapListHeaderRow.getMinimumSize();
        pnlSequentialStopRowPrefSize=mapMaintenanceBaseWindow.pnlSelectedMapListHeaderRow.getPreferredSize();
        sptrSequentialStopTopMaxSize=mapMaintenanceBaseWindow.sptrSelectedMapListTop.getMaximumSize();
        sptrSequentialStopTopMinSize=mapMaintenanceBaseWindow.sptrSelectedMapListTop.getMinimumSize();
        sptrSequentialStopTopPrefSize=mapMaintenanceBaseWindow.sptrSelectedMapListTop.getPreferredSize();
        sptrSequentialStopMaxSize= mapMaintenanceBaseWindow.sptrSelectedMapList.getMaximumSize();
        sptrSequentialStopMinSize= mapMaintenanceBaseWindow.sptrSelectedMapList.getMinimumSize();
        sptrSequentialStopPrefSize= mapMaintenanceBaseWindow.sptrSelectedMapList.getPreferredSize();
        pnlApprobedByRowMaxSize= mapMaintenanceBaseWindow.pnlSelectedMapListPanelRow.getMaximumSize();
        pnlApprobedByRowMinSize=mapMaintenanceBaseWindow.pnlSelectedMapListPanelRow.getMinimumSize();
        pnlApprobedByRowPrefSize=mapMaintenanceBaseWindow.pnlSelectedMapListPanelRow.getPreferredSize();
    }
    
    public void display() {
        try {
            mdiForm.putFrame(CoeusGuiConstants.MAPS_BASE_FRAME_TITLE + " "+unit, mapMaintenanceBaseWindow);
            mdiForm.getDeskTopPane().add(mapMaintenanceBaseWindow);
            mapMaintenanceBaseWindow.setSelected(true);
            mapMaintenanceBaseWindow.setVisible(true);
            updateNewInstance();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
        if(!hasAddMapRight) {
            mapMaintenanceBaseWindow.btnAdd.setEnabled(false);
            mapMaintenanceBaseWindow.mnuItmAdd.setEnabled(false);
        }
        if(!hasModifyMapRight) {
            mapMaintenanceBaseWindow.btnModify.setEnabled(false);
            mapMaintenanceBaseWindow.mnuItmModify.setEnabled(false);
            mapMaintenanceBaseWindow.btnDelete.setEnabled(false);
            mapMaintenanceBaseWindow.mnuItmDelete.setEnabled(false);
            
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            mapMaintenanceBaseWindow.tblMap.setBackground(bgColor);
            mapMaintenanceBaseWindow.scrPnMap.getViewport().setBackground(bgColor);
            mapMaintenanceBaseWindow.pnlSelectedMapList.setBackground(bgColor);
            mapMaintenanceBaseWindow.pnlSelectedMapListHeaderRow.setBackground(bgColor);
            mapMaintenanceBaseWindow.pnlSelectedMapListPanelRow.setBackground(bgColor);
            
        }
    }
    
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        mapMaintenanceBaseWindow.btnAdd.addActionListener(this);
        mapMaintenanceBaseWindow.btnModify.addActionListener(this);
        mapMaintenanceBaseWindow.btnDelete.addActionListener(this);
        mapMaintenanceBaseWindow.btnClose.addActionListener(this);
        mapMaintenanceBaseWindow.mnuItmAdd.addActionListener(this);
        mapMaintenanceBaseWindow.mnuItmModify.addActionListener(this);
        mapMaintenanceBaseWindow.mnuItmDelete.addActionListener(this);
        mapMaintenanceBaseWindow.addVetoableChangeListener(this);
        mapMaintenanceBaseWindow.tblMap.getTableHeader().addMouseListener(this);
        
        mapTableModel = new MapTableModel();
        mapMaintenanceBaseWindow.tblMap.setModel(mapTableModel);
        ListSelectionModel selectionModel = mapMaintenanceBaseWindow.tblMap.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        mapMaintenanceBaseWindow.tblMap.setSelectionModel(selectionModel);
    }
    
    private void setTableColumn() {
        JTableHeader tableHeader = mapMaintenanceBaseWindow.tblMap.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        mapMaintenanceBaseWindow.tblMap.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        mapMaintenanceBaseWindow.tblMap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mapMaintenanceBaseWindow.tblMap.setCellSelectionEnabled(false);
        mapMaintenanceBaseWindow.tblMap.setRowSelectionAllowed(true);
        mapMaintenanceBaseWindow.tblMap.setRowHeight(22);
        //mapMaintenanceBaseWindow.tblMap.setRowSelectionInterval(0, 0);
        
        TableColumn column = mapMaintenanceBaseWindow.tblMap.getColumnModel().getColumn(COLUMN_ID);
        column.setPreferredWidth(85);
        column.setResizable(true);
        
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Removed the column type from the table
//        column = mapMaintenanceBaseWindow.tblMap.getColumnModel().getColumn(COLUMN_TYPE);
//        column.setPreferredWidth(100);
//        column.setResizable(true);
        
        column = mapMaintenanceBaseWindow.tblMap.getColumnModel().getColumn(COLUMN_DESCRIPTION);
        column.setPreferredWidth(885);
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        column.setResizable(true);
        
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        try {
            CoeusVector cvmData=getMapsData();
            cvmData.sort("mapId",true);
            if(sortCodeAsc) {
                //Code already sorted in Ascending order. Sort now in Descending order.
                if(isIdCol) {
                    cvmData.sort("mapId", true);
                }else if(isTypeCol) {
                    cvmData.sort("mapType", true);
                }else if(isDescriptionCol) {
                    cvmData.sort("mapDescription", true);
                }
            }else {
                //Code already sorted in Descending order. Sort now in Ascending order.
                if(isIdCol) {
                    cvmData.sort("mapId", false);
                }else if(isTypeCol) {
                    cvmData.sort("mapType", false);
                }else if(isDescriptionCol) {
                    cvmData.sort("mapDescription", false);
                }
            }
            
            mapTableModel.setData(cvmData);
            getRoleQualifiers();
            if(mapMaintenanceBaseWindow.tblMap.getRowCount() != 0) {
                mapMaintenanceBaseWindow.tblMap.setRowSelectionInterval(0,0);
            }else if(mapMaintenanceBaseWindow.tblMap.getRowCount() == 0) {
                mapMaintenanceBaseWindow.pnlSelectedMapList.removeAll();
                mapMaintenanceBaseWindow.pnlSelectedMapList.revalidate();
            }
            
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    //Added Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    /**
     * Sorts the vector according to level number, stop number and primary
     * flag
     * @param cvDetailsData
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
    /**
     * Populates the roleQualifierMap with role code as key and value a hashmap
     * with qualifier code as key and qualifier description as value
     */
    public void getRoleQualifiers() {
        roleQulaifiersMap = new HashMap();
        CoeusVector cvRoleQualifiers = null;
        try {
            cvRoleQualifiers = getQualifiers();
        } catch (CoeusClientException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        if(cvRoleQualifiers!=null){
            PersonRoleInfoBean personRoleInfoBean = null;
            Map qualifierMap = null;
            String qualifierCode = "";
            for(int i=0;i<cvRoleQualifiers.size();i++){
                personRoleInfoBean = (PersonRoleInfoBean)cvRoleQualifiers.get(i);
                if(roleQulaifiersMap.containsKey(personRoleInfoBean.getRoleCode())){
                    qualifierMap =  (HashMap)roleQulaifiersMap.get(personRoleInfoBean.getRoleCode());
                    qualifierMap.put(personRoleInfoBean.getQualifierCode(), personRoleInfoBean.getRoleQualifier());
                }else{
                    qualifierMap  = new HashMap();
                    qualifierMap.put(personRoleInfoBean.getQualifierCode(), personRoleInfoBean.getRoleQualifier());
                    roleQulaifiersMap.put(personRoleInfoBean.getRoleCode(), qualifierMap);
                }
            }
        }
        
    }
    /**
     * Get the role qualifier from the server
     */
    public CoeusVector getQualifiers() throws CoeusClientException, CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        String PERSON_ROLE_SERVLET ="/personRoleServlet";
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PERSON_ROLE_SERVLET;
        Hashtable htQualifiers = null;
        CoeusVector cvQualifiers = null;
        requesterBean.setFunctionType(GET_QUALIFIER_LIST);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htQualifiers = (Hashtable)responderBean.getDataObject();
                cvQualifiers = (CoeusVector) htQualifiers.get(PersonRoleInfoBean.class);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return cvQualifiers;
    }
    /**
     * Populate the MapDetailsBean in the the vector cvMapDetails with the qualifierDescription
     * from the values in the roleQualifierMap
     * @param cvMapDetails - Map containing the MapDetailsBean
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
     * Returns the qualifier description given the rolecode and the qualifiercode
     * @param roleCode - role type code
     * @param qualifierCode - qualifier code
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
    
    public void setBaseWindowFlag(boolean val) {
        this.isOpenedFromUserHierarchy =  val;
    }
    
    //To get the Map data
    private CoeusVector getMapsData() throws CoeusClientException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        CoeusVector cvMapData = null;
        CoeusVector inDataObjects= new CoeusVector();
        inDataObjects.add(unitNumber);
        requesterBean.setDataObject(inDataObjects);
        requesterBean.setFunctionType(GET_MAP_DATA);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                CoeusVector cvMaps=(CoeusVector)responderBean.getDataObject();
                hasAddMapRight = ((Boolean)cvMaps.get(0)).booleanValue();
                hasModifyMapRight = ((Boolean)cvMaps.get(1)).booleanValue();
                cvMapData = (CoeusVector)cvMaps.get(2);
                formatFields();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return cvMapData;
    }
    
//    //To get the campus for the Department
//    private String getUnitNumber(String topLevelUnit) throws CoeusClientException{
//        String campusForDept = null;
//        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
//        RequesterBean request = new RequesterBean();
//        request.setId(topLevelUnit);
//        request.setDataObject("FN_GET_CAMPUS_FOR_DEPT");
//        AppletServletCommunicator comm
//                = new AppletServletCommunicator(connectTo, request);
//        comm.send();
//        ResponderBean response = comm.getResponse();
//
//        if (response!=null){
//            if (response.isSuccessfulResponse()){
//                campusForDept = (String)response.getDataObject();
//            }
//        }else{
//            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
//        }
//        return campusForDept;
//    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(mapMaintenanceBaseWindow.btnClose)) {
            try {
                close();
            }catch(PropertyVetoException PropertyVetoException) {
            }
        }else if(source.equals(mapMaintenanceBaseWindow.btnAdd)||
                source.equals(mapMaintenanceBaseWindow.mnuItmAdd)){
            mapMaintenanceBaseWindow.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            showMapDetails(TypeConstants.NEW_MODE);
            refresh();
            int rowCount=mapMaintenanceBaseWindow.tblMap.getRowCount();
            mapMaintenanceBaseWindow.tblMap.setRowSelectionInterval(rowCount-1, rowCount-1);
            mapMaintenanceBaseWindow.tblMap.scrollRectToVisible(
                    mapMaintenanceBaseWindow.tblMap.getCellRect(0, COLUMN_ID, true));
        }else if(source.equals(mapMaintenanceBaseWindow.btnModify)||
                source.equals(mapMaintenanceBaseWindow.mnuItmModify)){
            int selRow=mapMaintenanceBaseWindow.tblMap.getSelectedRow();
            mapMaintenanceBaseWindow.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            showMapDetails(TypeConstants.MODIFY_MODE);
            refresh();
            if(selRow >= 0){
                mapMaintenanceBaseWindow.tblMap.setRowSelectionInterval(selRow, selRow);
            }
            mapMaintenanceBaseWindow.tblMap.scrollRectToVisible(
                    mapMaintenanceBaseWindow.tblMap.getCellRect(0, COLUMN_ID, true));
        }else if(source.equals(mapMaintenanceBaseWindow.btnDelete)||
                source.equals(mapMaintenanceBaseWindow.mnuItmDelete)){
            try{
                int selRow=mapMaintenanceBaseWindow.tblMap.getSelectedRow();
                if(selRow < 0) {
                    return;
                }
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("unitMapsDetail_exceptionCode.1006"),
                        CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_CANCEL);
                
                if(option == CoeusOptionPane.SELECTION_YES){
                    deleteMap();
                    refresh();
                }else if(option == CoeusOptionPane.SELECTION_NO){
                    return;
                }
            }catch(CoeusClientException coeusClientException) {
                CoeusOptionPane.showErrorDialog(coeusClientException.getMessage());
            }
        }
    }
    
    //To delete the selected map
    public void deleteMap() throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        String id=(String)mapMaintenanceBaseWindow.tblMap.getValueAt(selectedRow, 0);
        Integer mapId = new Integer(id);
        requesterBean.setDataObject(mapId);
        requesterBean.setFunctionType(DELETE_UNIT_MAP);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                int value=((Integer)responderBean.getDataObject()).intValue();
                if(value == 1 ) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("unitMapsDetail_exceptionCode.1007"));
                }
                
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        
    }
    
    private void close() throws PropertyVetoException{
        closed = true;
        mdiForm.removeFrame(CoeusGuiConstants.MAPS_BASE_FRAME_TITLE+" "+unit);
        mapMaintenanceBaseWindow.doDefaultCloseAction();
        clearOldInstance();
        clean();
    }
    
    private void clean(){
        mapMaintenanceBaseWindow.tblMap.getTableHeader().removeMouseListener(this);
        mapMaintenanceBaseWindow.tblMap.getSelectionModel().removeListSelectionListener(this);
        mapMaintenanceBaseWindow.btnModify.removeActionListener(this);
        mapMaintenanceBaseWindow.btnAdd.removeActionListener(this);
        mapMaintenanceBaseWindow.btnDelete.removeActionListener(this);
        mapTableModel = null;
        mapMaintenanceBaseWindow = null;
        cvMapDetails=null;
        cvmapsData=null;
        mdiForm=null;
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        if(mapMaintenanceBaseWindow.tblMap.getRowCount() > 0) {
            selectedRow = mapMaintenanceBaseWindow.tblMap.getSelectedRow();
            if(selectedRow > -1) {
                try {
                    CoeusVector cvUnitMaps = getMapDetails();
                    populateQualifierDescription(cvUnitMaps);
                }catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                }
                setSelectedSequentialDetails(cvMapDetails);
            }
        }
    }
    
    //To display the sequential details of the selected map
    public void setSelectedSequentialDetails(CoeusVector cvBeansDetails) {
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //To sort the vector according to the level number, stop number
        cvBeansDetails = setValuesToBean(cvBeansDetails);
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        try {
            CoeusVector mapDetails = null;
            Hashtable seqHashTab = new Hashtable();
            Hashtable seqSubHashTab = new Hashtable();
            boolean isUIUpdated = false;
            int mapId = 0;
            
            MapDetailsBean mapDetailsBean = (MapDetailsBean)cvBeansDetails.get(0);
            if(mapDetailsBean != null) {
                mapDetails = cvBeansDetails;
            }
            
            mapMaintenanceBaseWindow.pnlSelectedMapList.removeAll();
            if(mapDetails == null || mapDetails.size() < 1) {
                return;
            }
            
            for(int index =0;index < mapDetails.size();index ++) {
                String level = null;
                int stopLevel = 0;
                
                mapDetailsBean = (MapDetailsBean)mapDetails.get(index);
                level = mapDetailsBean.getLevelNumber()+"";
                mapId = mapDetailsBean.getMapId();
                stopLevel = mapDetailsBean.getStopNumber();
                
                CoeusVector eachStopDataPrimaryApprovers = filterVectorByLevel(mapDetails,Integer.parseInt(level),stopLevel,true,mapId);
                CoeusVector eachStopDataNonPrimaryApprovers = filterVectorByLevel(mapDetails,Integer.parseInt(level),stopLevel,false,mapId);
                
                
                //=========Adding the SequentialStopHeader  ==================
                boolean isSequentialHeaderYoAdd = false;
                if(!seqHashTab.containsKey(level+mapId)) {
                    seqHashTab.put(level+mapId, level+mapId);
                    isSequentialHeaderYoAdd = true;
                }
                
                if((eachStopDataPrimaryApprovers != null && eachStopDataPrimaryApprovers.size() > 0) && isSequentialHeaderYoAdd) {
                    mapMaintenanceBaseWindow.pnlSelectedMapList.add(getSequentialStopRowPanel(Integer.parseInt(level)));//isSelectedMapTab
                }
                
                //=========Adding Data for each SequentialStopHeader ==========
                
                if((eachStopDataPrimaryApprovers != null && eachStopDataPrimaryApprovers.size() > 0) ) {
                    
                    if(!seqSubHashTab.containsKey(level+mapId+stopLevel)) {
                        setSequentialRows(eachStopDataPrimaryApprovers,eachStopDataNonPrimaryApprovers);
                        isUIUpdated = true;
                        seqSubHashTab.put(level+mapId+stopLevel, level+mapId+stopLevel);
                    }
                }
                
            }
            
            if(isUIUpdated) {
                mapMaintenanceBaseWindow.pnlSelectedMapList.repaint();
            }
            
        }catch(Exception exp) {
            exp.getMessage();
        }
        
    }
    
    //To display the Map details form
    private void showMapDetails(char functionType){
        try{
            MapHeaderBean bean;
            if(functionType==TypeConstants.NEW_MODE){
                bean=new MapHeaderBean();
                bean.setUnitNumber(unitNumber);
            }else{
                int rowCount=mapMaintenanceBaseWindow.tblMap.getRowCount();
                if(rowCount<=0){
                    return;
                }
                int selRow=mapMaintenanceBaseWindow.tblMap.getSelectedRow();
                if(selRow<0){
                    return;
                }
                bean=(MapHeaderBean)cvmapsData.get(selRow);
                bean.setUnitNumber(unitNumber);
            }
            MapDetailController mapDetailController=new MapDetailController(bean);
            mapDetailController.setFunctionType(functionType);
            mapDetailController.setRoleQulaifiersMap(roleQulaifiersMap);
            mapDetailController.setFormData(null);
            mapDetailController.display();
            mapDetailController.cleanUp();
            mapDetailController = null;
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }finally{
            mapMaintenanceBaseWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void updateNewInstance() {
        //update to handle new window menu item to the existing Window Menu.
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.addNewMenuItem( CoeusGuiConstants.MAPS_BASE_FRAME_TITLE+" "+unit, mapMaintenanceBaseWindow );
            windowMenu.updateCheckBoxMenuItemState( CoeusGuiConstants.MAPS_BASE_FRAME_TITLE+" "+unit, true );
        }
        mdiForm.refreshWindowMenu(windowMenu);
    }
    
    //To remove the frame from the window menu item
    private void clearOldInstance() {
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.removeMenuItem( CoeusGuiConstants.MAPS_BASE_FRAME_TITLE+" "+unit );
        }
        
    }
    
    
    //To add the sequential stop row to the panel
    private JPanel getSequentialStopRowPanel(int indexNumber) {
        
        JPanel pnlSequentialStopRow = new JPanel();
        javax.swing.JLabel lblSequentialStop = new javax.swing.JLabel();
        javax.swing.JSeparator sptrSequentialStop = new javax.swing.JSeparator();
        GridBagConstraints gridBagConstraints = null;
        
        javax.swing.JLabel lblDescription = new javax.swing.JLabel();
        javax.swing.JSeparator sptrSequentialStopTop = new javax.swing.JSeparator();
        
        pnlSequentialStopRow.setLayout(new java.awt.GridBagLayout());
        if(hasModifyMapRight) {
            pnlSequentialStopRow.setBackground(new java.awt.Color(255, 255, 255));
        }else {
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            pnlSequentialStopRow.setBackground(bgColor);
        }
        pnlSequentialStopRow.setMaximumSize(pnlSequentialStopRowMaxSize);
        pnlSequentialStopRow.setMinimumSize(pnlSequentialStopRowMinSize);
        pnlSequentialStopRow.setPreferredSize(pnlSequentialStopRowPrefSize);
        
        sptrSequentialStopTop.setMaximumSize(sptrSequentialStopTopMaxSize);
        sptrSequentialStopTop.setMinimumSize(sptrSequentialStopTopMinSize);
        sptrSequentialStopTop.setPreferredSize(sptrSequentialStopTopPrefSize);
        sptrSequentialStopTop.setForeground(Color.black);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        pnlSequentialStopRow.add(sptrSequentialStopTop, gridBagConstraints);
        
        lblSequentialStop.setFont(CoeusFontFactory.getNormalFont());
        lblSequentialStop.setForeground(Color.blue);
        lblSequentialStop.setText("Sequential Stop:   "+indexNumber);
        lblSequentialStop.setMaximumSize(new java.awt.Dimension(32767, 32767));
        /*
         *UserId to UserName Enhancement - Start
         *Modified width of the user id field to display username
         */
//        lblSequentialStop.setMinimumSize(new java.awt.Dimension(190, 16));
        lblSequentialStop.setMinimumSize(new java.awt.Dimension(450, 16));
        //UserId to UserName Enhancement - End
        lblSequentialStop.setPreferredSize(new java.awt.Dimension(190, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlSequentialStopRow.add(lblSequentialStop, gridBagConstraints);
        
        sptrSequentialStop.setMaximumSize(sptrSequentialStopMaxSize);
        sptrSequentialStop.setMinimumSize(sptrSequentialStopMinSize);
        sptrSequentialStop.setPreferredSize(sptrSequentialStopPrefSize);
        sptrSequentialStop.setForeground(Color.black);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlSequentialStopRow.add(sptrSequentialStop, gridBagConstraints);
        
        lblDescription.setFont(CoeusFontFactory.getNormalFont());
        lblDescription.setForeground(Color.blue);
        lblDescription.setText("Description");
        lblDescription.setMaximumSize(new java.awt.Dimension(50, 16));
        lblDescription.setMinimumSize(new java.awt.Dimension(50, 16));
        lblDescription.setPreferredSize(new java.awt.Dimension(50, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSequentialStopRow.add(lblDescription, gridBagConstraints);
        return pnlSequentialStopRow;
        
    }
    
    //To filter for primary approvers and non primary approvers
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
                
                if(
                        (startLevelOfBean == startLevel) &&
                        (stopLevelOfBean == stopLevel) &&
                        (mapIDOfBean == mapId) &&
                        (isPrimaryApproverOfBean == isPrimaryApprover )
                        ) {
                    filteredVector.add(mapDetailsBean);
                }
            }
            
        }catch(Exception exp) {
            exp.printStackTrace();
            filteredVector = null;
        }
        
        return filteredVector;
    }
    
    //To get the Map details data
    private CoeusVector getMapDetails() throws CoeusClientException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        CoeusVector cvMapDetailsData = null;
        String id=(String)mapMaintenanceBaseWindow.tblMap.getValueAt(selectedRow, 0);
        Integer mapId = new Integer(id);
        requesterBean.setDataObject(mapId);
        requesterBean.setFunctionType(GET_UNIT_MAP_DETAIL_DATA);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                cvMapDetailsData = (CoeusVector)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        cvMapDetails = cvMapDetailsData;
        return cvMapDetailsData;
    }
    
    //To add sequential rows to the panel
    public void setSequentialRows(CoeusVector mapDetailsPrimaryApprover,CoeusVector mapDetailsNonPrimaryApprover) {
        
        String valuesForPanel [] = new String[3];
        MapDetailsBean mapDetailsBean = null;
        int level = 0;
        int stopLevel = 0;
        int mapID = 0;
        //APPROVEBY_LABEL OR_LABEL
        for(int indexData = 0; indexData < mapDetailsPrimaryApprover.size();indexData ++) {
            mapDetailsBean =  (MapDetailsBean) mapDetailsPrimaryApprover.get(indexData);
            level = mapDetailsBean.getLevelNumber();
            stopLevel = mapDetailsBean.getStopNumber();
            mapID = mapDetailsBean.getMapId();
            
            valuesForPanel[0] = APPROVEBY_LABEL;
            //Modified for Coeus4.3 enhancement PT ID 2785 - Routing Enhancement - start
            //Set the name into valuesPanel[1] which need to be displayed, according to
            //the type whether the approver is a user, role or role qualifier
            
            if(mapDetailsBean.isRoleType()){
                if(mapDetailsBean.getQualifierCode().equals("") || mapDetailsBean.getQualifierCode().equals("-1")){
                    valuesForPanel[1] = mapDetailsBean.getRoleDescription();
                }else{
                    valuesForPanel[1] = mapDetailsBean.getRoleDescription() + 
                            "(" +mapDetailsBean.getQualifierDescription() + ")" ;
                }
            }else{
//            valuesForPanel[1] = mapDetailsBean.getUserId();
            /*
             * UserID to UserName Enhancement - Start
             * Added new property getUpdateUserName to get username
             */
                valuesForPanel[1] = mapDetailsBean.getUpdateUserName();
                // UserId to UserName Enhancement - End
            }
            //Modified for Coeus4.3 enhancement PT ID 2785 - Routing Enhancement - end
            valuesForPanel[2] = mapDetailsBean.getDetailDescription();
            addSequentialDetialsRowstoPanel(valuesForPanel,false);
            
        }
        
        if(mapDetailsNonPrimaryApprover != null && mapDetailsNonPrimaryApprover.size() > 0) {
            
            for(int index= 0; index < mapDetailsNonPrimaryApprover.size();index++) {
                mapDetailsBean =  (MapDetailsBean) mapDetailsNonPrimaryApprover.get(index);
                valuesForPanel[0] = OR_LABEL;
                //Modified for Coeus4.3 enhancement PT ID 2785 - Routing Enhancement - start
                //Set the name into valuesPanel[1] which need to be displayed, according to
                //the type whether the approver is a user, role or role qualifier
                if(mapDetailsBean.isRoleType()){
                    if(mapDetailsBean.getQualifierCode().equals("") || mapDetailsBean.getQualifierCode().equals("-1")){
                        valuesForPanel[1] = mapDetailsBean.getRoleDescription();
                    }else{
                        valuesForPanel[1] = mapDetailsBean.getRoleDescription() + 
                            "(" +mapDetailsBean.getQualifierDescription() + ")" ;
                    }
                }else{
//                valuesForPanel[1] = mapDetailsBean.getUserId();
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username
                 */
                    valuesForPanel[1] = mapDetailsBean.getUpdateUserName();
                    // UserId to UserName Enhancement - End
                }
                //Modified for Coeus4.3 enhancement PT ID 2785 - Routing Enhancement - end
                valuesForPanel[2] = mapDetailsBean.getDetailDescription();
                addSequentialDetialsRowstoPanel(valuesForPanel,true);
            }
        }
        
    }
    
    //To add sequential details rows to the panel
    public void addSequentialDetialsRowstoPanel(String [] valuesForPanel,boolean isRowFormatReq) {
        mapMaintenanceBaseWindow.pnlSelectedMapList.add(getApprovedByRowPanel(valuesForPanel,isRowFormatReq));//isSelectedMapTab
    }
    
    //To add the Approved By row to the panel
    private JPanel getApprovedByRowPanel(String elementValues[],boolean isRowFormatReq) {
        
        JPanel pnlApprobedByRow = new JPanel();
        javax.swing.JLabel lblApproveByRow = new javax.swing.JLabel();
        javax.swing.JLabel lblUserIdRow = new javax.swing.JLabel();
        javax.swing.JLabel lblDescriptionRow = new javax.swing.JLabel();
        GridBagConstraints gridBagConstraints = null;
        
        
        //========For Select List Tab =============
        
        pnlApprobedByRow.setLayout(new java.awt.GridBagLayout());
        if(hasModifyMapRight) {
            pnlApprobedByRow.setBackground(new java.awt.Color(255, 255, 255));
        }else {
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            pnlApprobedByRow.setBackground(bgColor);
        }
        pnlApprobedByRow.setMaximumSize(pnlApprobedByRowMaxSize);
        pnlApprobedByRow.setMinimumSize(pnlApprobedByRowMinSize);
        pnlApprobedByRow.setPreferredSize(pnlApprobedByRowPrefSize);
        
        if(isRowFormatReq) {
            lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
            lblApproveByRow.setForeground(Color.blue);
            lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            
            //Font(String name, int style, int size)
            java.awt.Font italicFont = new java.awt.Font(lblUserIdRow.getFont().getFontName(),
                    java.awt.Font.ITALIC,
                    lblUserIdRow.getFont().getSize());
            
            lblUserIdRow.setFont(italicFont);
            lblDescriptionRow.setFont(italicFont);
            lblApproveByRow.setFont(italicFont);
            
        } else {
            lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
            lblApproveByRow.setForeground(Color.red);
            lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblUserIdRow.setFont(CoeusFontFactory.getNormalFont());
            lblDescriptionRow.setFont(CoeusFontFactory.getNormalFont());
        }
        
        lblApproveByRow.setText(elementValues[0]);  //"Approve By");
        lblApproveByRow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblApproveByRow.setMaximumSize(new java.awt.Dimension(85, 20));
        lblApproveByRow.setMinimumSize(new java.awt.Dimension(85, 20));
        lblApproveByRow.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets=new java.awt.Insets(0, 12, 0, 0);
        pnlApprobedByRow.add(lblApproveByRow, gridBagConstraints);
        lblUserIdRow.setText(elementValues[1]); //"user");
        lblUserIdRow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        /*
         *UserId to UserName Enhancement - Start
         *Modified width of the user id field to display username
         */
        /*
         lblUserIdRow.setMaximumSize(new java.awt.Dimension(100, 20));
         lblUserIdRow.setMinimumSize(new java.awt.Dimension(100, 20));
         lblUserIdRow.setPreferredSize(new java.awt.Dimension(100, 20));
         */
        lblUserIdRow.setMaximumSize(new java.awt.Dimension(362, 20));
        lblUserIdRow.setMinimumSize(new java.awt.Dimension(362, 20));
        lblUserIdRow.setPreferredSize(new java.awt.Dimension(362, 20));
        //UserId to UserName Enhancement - End
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlApprobedByRow.add(lblUserIdRow, gridBagConstraints);
        
        lblDescriptionRow.setText(elementValues[2]);
        lblDescriptionRow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblDescriptionRow.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lblDescriptionRow.setMinimumSize(new java.awt.Dimension(520, 20));
        lblDescriptionRow.setPreferredSize(new java.awt.Dimension(520, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets=new java.awt.Insets(0, 10, 0, 0);
        pnlApprobedByRow.add(lblDescriptionRow, gridBagConstraints);
        
        return pnlApprobedByRow;
        
    }
    
    private class MapTableModel extends AbstractTableModel {
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Removed the column type from the table
        /*String colNames[] = {"Id","Type","Description"};
        Class[] colTypes = new Class[] {String.class,String.class,String.class};*/
        String colNames[] = {"Id","Description"};
        Class[] colTypes = new Class[] {String.class,String.class};
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        MapTableModel() {
            
        }
        
        public boolean isCellEditable() {
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }
        
        public void setData(CoeusVector cvData){
            cvmapsData=cvData;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvmapsData != null)
                return cvmapsData.size();
            else
                return 0;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            MapHeaderBean mapHeaderBean =  (MapHeaderBean)cvmapsData.get(row);
            switch(column) {
                case COLUMN_ID:
                    return (new Integer(mapHeaderBean.getMapId())).toString();
                    //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                    //Removed the column type from the table
                /*case COLUMN_TYPE:
                    String mapType="";
                    if(mapHeaderBean.getMapType().equals("I")){
                        mapType = "Indirect";
                    }else if(mapHeaderBean.getMapType().equals("D")){
                        mapType = "Direct";
                    }
                    return mapType;*/
                    //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                case COLUMN_DESCRIPTION:
                    return mapHeaderBean.getMapDescription();
                    
            }
            return EMPTY_STRING;
            
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(cvmapsData== null){
                return;
            }
            MapHeaderBean mapHeaderBean = (MapHeaderBean)cvmapsData.get(row);
            switch(column) {
                case COLUMN_ID:
                    mapHeaderBean.setMapId((new Integer(value.toString())).intValue());
                    break;
                    //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                    //Removed the column type from the table
                /*case COLUMN_TYPE:
                    mapHeaderBean.setMapType(value.toString());
                    break;*/
                    //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                case COLUMN_DESCRIPTION:
                    mapHeaderBean.setMapDescription(value.toString());
                    break;
                    
            }
        }
        
    }
    
    public void refresh() {
        try {
            setFormData(null);
            
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    public void vetoableChange(java.beans.PropertyChangeEvent pce) throws PropertyVetoException {
        if(closed)
            return;
        boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
        if(pce.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Point clickedPoint = mouseEvent.getPoint();
        int xPosition = (int)clickedPoint.getX();
        int selectedRow = mapMaintenanceBaseWindow.tblMap.getSelectedRow();
        if(selectedRow < 0 )
            return;
        MapHeaderBean mapHeaderBean=(MapHeaderBean)cvmapsData.get(selectedRow);
        
        int columnIndex = mapMaintenanceBaseWindow.tblMap.getColumnModel().getColumnIndexAtX(xPosition);
        switch (columnIndex) {
            case COLUMN_ID:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvmapsData.sort("mapId", false);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvmapsData.sort("mapId", true);
                    sortCodeAsc = true;
                }
                isIdCol=true;
                isTypeCol=false;
                isDescriptionCol =false;
                break;
                //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //Removed the column type from the table
            /*case COLUMN_TYPE:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvmapsData.sort("mapType", false);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvmapsData.sort("mapType", true);
                    sortCodeAsc = true;
                }
                isIdCol=false;
                isTypeCol=true;
                isDescriptionCol =false;
                break;*/
                //Commented for Coeus 4.3 Routing enhancement -PT ID:2785 - end
            case COLUMN_DESCRIPTION:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvmapsData.sort("mapDescription", false);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvmapsData.sort("mapDescription", true);
                    sortCodeAsc = true;
                }
                isIdCol=false;
                isTypeCol=false;
                isDescriptionCol =true;
                break;
                
        }//End Switch
        
        mapTableModel.fireTableDataChanged();
        mapMaintenanceBaseWindow.tblMap.setRowSelectionInterval(cvmapsData.indexOf(mapHeaderBean),cvmapsData.indexOf(mapHeaderBean));
        mapMaintenanceBaseWindow.tblMap.scrollRectToVisible(
                mapMaintenanceBaseWindow.tblMap.getCellRect(0, COLUMN_ID, true));
        
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
}
