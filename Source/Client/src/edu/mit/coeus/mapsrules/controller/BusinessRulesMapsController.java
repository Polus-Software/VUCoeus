/*
 * @(#)BusinessRulesMapsController.java 1.0 11/03/05 1:56PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 17-AUG-2007
 * by Leena
 */
package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.mapsrules.bean.MapDetailsBean;
import edu.mit.coeus.mapsrules.bean.MapHeaderBean;
import edu.mit.coeus.mapsrules.gui.BusinessRulesMapsForm;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.UserUtils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
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
public class BusinessRulesMapsController extends RuleController implements ActionListener,ListSelectionListener{
    
    /*
     * UserId to UserName Enhancement - Start
     * Modified the width to display the username
     */
//    private static final int WIDTH = 632 ;
    private static final int WIDTH = 730 ;
    //UserId to UserName Enhancement - End
    private static final int HEIGHT = 393;
    //Commented for 2785 - Routing enhancement - start
    //private static final String WINDOW_TITLE = "Map Selection";
    //Commented for 2785 - Routing enhancement - end
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_TYPE = 1;
    private static final int COLUMN_DESCRIPTION = 2;
    private static final String EMPTY_STRING = "";
    private static final char GET_MAP_DATA = 'B';
    private static final char GET_UNIT_MAP_DETAIL_DATA = 'E';
    private static final String APPROVEBY_LABEL ="Approve By";
    /** String constant OR LABEL */
    private static final String OR_LABEL ="Or       ";
    private final String MAP_SERVLET ="/MapMaintainanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ MAP_SERVLET;
    
    BusinessRulesMapsForm businessRulesMapsForm;
    CoeusDlgWindow dlgMaps;
    private edu.mit.coeus.gui.CoeusAppletMDIForm mdiForm;
    private CoeusVector cvmapsData,cvMapDetails;
    String unitNumber ="";
    int selectedRow = 0;
    MapTableModel mapTableModel;
    MapHeaderBean mapBean;
    private Dimension pnlSequentialStopRowMaxSize,pnlSequentialStopRowMinSize,pnlSequentialStopRowPrefSize,sptrSequentialStopTopMaxSize,
            sptrSequentialStopTopMinSize,sptrSequentialStopTopPrefSize,sptrSequentialStopMaxSize,sptrSequentialStopMinSize,sptrSequentialStopPrefSize,
            pnlApprobedByRowMaxSize,pnlApprobedByRowMinSize,pnlApprobedByRowPrefSize;
    
    private boolean okClicked;
    
    private HashMap objMap = new HashMap();
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    //Map that holds the role as key and value another hashMap with qualifierCode
    //as key qualifier description as value
    private Map roleQulaifiersMap;
    private static final char GET_QUALIFIER_LIST = 'Q';
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    
    /** Creates a new instance of BusinessRulesMapsController */
    public BusinessRulesMapsController(String unitNumber) {
        mdiForm = edu.mit.coeus.utils.CoeusGuiConstants.getMDIForm();
        this.unitNumber = unitNumber;
        postInitComponents();
        initComponents();
        registerComponents();
        try {
            setFormData(null);
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        setTableColumn();
        //formatFields();
    }
    
    private void setTableColumn() {
        JTableHeader tableHeader = businessRulesMapsForm.tblMap.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        businessRulesMapsForm.tblMap.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        businessRulesMapsForm.tblMap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        businessRulesMapsForm.tblMap.setCellSelectionEnabled(false);
        businessRulesMapsForm.tblMap.setRowSelectionAllowed(true);
        businessRulesMapsForm.tblMap.setRowHeight(20);
        //mapMaintenanceBaseWindow.tblMap.setRowSelectionInterval(0, 0);
        
        TableColumn column = businessRulesMapsForm.tblMap.getColumnModel().getColumn(COLUMN_ID);
        /*
         *UserId to UserName Enhancement - Start
         *Modified the width of the table
         */
        //column.setPreferredWidth(70);
        column.setPreferredWidth(80);
        column.setResizable(true);
        
        column = businessRulesMapsForm.tblMap.getColumnModel().getColumn(COLUMN_TYPE);
        //column.setPreferredWidth(80);
        // 3768: Indirect and direct Maps - Start
//        column.setPreferredWidth(100);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
//        column.setResizable(true);
        column.setResizable(false);
        // 3768: Indirect and direct Maps - End
        column = businessRulesMapsForm.tblMap.getColumnModel().getColumn(COLUMN_DESCRIPTION);
        //column.setPreferredWidth(360);
        // 3768: Indirect and direct Maps - Start
//        column.setPreferredWidth(425);
        column.setPreferredWidth(525);
        // 3768: Indirect and direct Maps - End
        //UserId to UserName Enhancement - End
        column.setResizable(true);
        
    }
    
    private void postInitComponents() {
        businessRulesMapsForm = new BusinessRulesMapsForm();
        dlgMaps = new CoeusDlgWindow(mdiForm);
        dlgMaps.setResizable(false);
        dlgMaps.setModal(true);
        dlgMaps.getContentPane().add(businessRulesMapsForm);
        dlgMaps.setFont(CoeusFontFactory.getLabelFont());
        dlgMaps.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgMaps.setSize(WIDTH, HEIGHT);
        //Added for case 2785 - Routing enhancement - start
        String windowTitle = "Select Map";
        if(unitNumber != null){
            windowTitle = windowTitle + " for unit " + unitNumber;
        }
        dlgMaps.setTitle(windowTitle);
        //Added for case 2785 - Routing enhancement - end
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgMaps.getSize();
        dlgMaps.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgMaps.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgMaps.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        dlgMaps.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    public void initComponents() {
        pnlSequentialStopRowMaxSize = businessRulesMapsForm.pnlSelectedMapListHeaderRow.getMaximumSize();
        pnlSequentialStopRowMinSize=businessRulesMapsForm.pnlSelectedMapListHeaderRow.getMinimumSize();
        pnlSequentialStopRowPrefSize=businessRulesMapsForm.pnlSelectedMapListHeaderRow.getPreferredSize();
        sptrSequentialStopTopMaxSize=businessRulesMapsForm.sptrSelectedMapListTop.getMaximumSize();
        sptrSequentialStopTopMinSize=businessRulesMapsForm.sptrSelectedMapListTop.getMinimumSize();
        sptrSequentialStopTopPrefSize=businessRulesMapsForm.sptrSelectedMapListTop.getPreferredSize();
        sptrSequentialStopMaxSize= businessRulesMapsForm.sptrSelectedMapList.getMaximumSize();
        sptrSequentialStopMinSize= businessRulesMapsForm.sptrSelectedMapList.getMinimumSize();
        sptrSequentialStopPrefSize= businessRulesMapsForm.sptrSelectedMapList.getPreferredSize();
        pnlApprobedByRowMaxSize= businessRulesMapsForm.pnlSelectedMapListPanelRow.getMaximumSize();
        pnlApprobedByRowMinSize=businessRulesMapsForm.pnlSelectedMapListPanelRow.getMinimumSize();
        pnlApprobedByRowPrefSize=businessRulesMapsForm.pnlSelectedMapListPanelRow.getPreferredSize();
    }
    
    private void performCancelAction() {
        this.okClicked = false;
        dlgMaps.dispose();
    }
    
    private void setWindowFocus() {
        businessRulesMapsForm.btnCancel.requestFocusInWindow();
    }
    
    public void display() {
        if(businessRulesMapsForm.tblMap.getRowCount() > 0) {
            businessRulesMapsForm.tblMap.setRowSelectionInterval(0,0);
        }
        dlgMaps.setVisible(true);
    }
    
    /*public MapHeaderBean displayMaps() {
        display();
        return mapBean;
    }*/
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return mapBean;
    }
    
    public void registerComponents() {
        java.awt.Component[] components = {businessRulesMapsForm.btnCancel,
        businessRulesMapsForm.btnOk,
        businessRulesMapsForm.scrPnMap,
        businessRulesMapsForm.scrPnMapDetails
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        businessRulesMapsForm.setFocusTraversalPolicy(traversePolicy);
        businessRulesMapsForm.setFocusCycleRoot(true);
        businessRulesMapsForm.btnOk.addActionListener(this);
        businessRulesMapsForm.btnCancel.addActionListener(this);
        
        mapTableModel = new MapTableModel();
        businessRulesMapsForm.tblMap.setModel(mapTableModel);
        ListSelectionModel selectionModel = businessRulesMapsForm.tblMap.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        businessRulesMapsForm.tblMap.setSelectionModel(selectionModel);
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        try {
            CoeusVector cvmData=getMapsData();
            CoeusVector cvFilteredMaps = cvmData.filter(new Equals("mapType", "I"));
            cvFilteredMaps.sort("mapId",true);
            mapTableModel.setData(cvFilteredMaps);
            if(businessRulesMapsForm.tblMap.getRowCount() != 0) {
                businessRulesMapsForm.tblMap.setRowSelectionInterval(0,0);
            }else if(businessRulesMapsForm.tblMap.getRowCount() == 0) {
                businessRulesMapsForm.pnlSelectedMapList.removeAll();
                businessRulesMapsForm.pnlSelectedMapList.revalidate();
            }
            //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
            getRoleQualifiers();
            //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
        
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
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
                cvMapData = (CoeusVector)cvMaps.get(2);
                //formatFields();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return cvMapData;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        dlgMaps.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(businessRulesMapsForm.btnCancel)) {
            performCancelAction();
        }else if(source.equals(businessRulesMapsForm.btnOk)) {
            performOkAction();
        }
        dlgMaps.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void performOkAction() {
        dlgMaps.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(businessRulesMapsForm.tblMap.getRowCount() > 0) {
            selectedRow = businessRulesMapsForm.tblMap.getSelectedRow();
            mapBean =(MapHeaderBean)cvmapsData.get(selectedRow);
            this.okClicked = true;
        }
        dlgMaps.dispose();
    }
    
    public void cleanUp() {
        dlgMaps=null;
        cvmapsData= null;
        cvMapDetails = null;
        businessRulesMapsForm.btnOk.removeActionListener(this);
        businessRulesMapsForm.btnCancel.removeActionListener(this);
        mapBean= null;
        mapTableModel=null;
        businessRulesMapsForm=null;
        mdiForm=null;
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        dlgMaps.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(businessRulesMapsForm.tblMap.getRowCount() > 0) {
            selectedRow = businessRulesMapsForm.tblMap.getSelectedRow();
            if(selectedRow > -1) {
                try {
                    CoeusVector cvUnitMaps = getMapDetails();
                    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
                    populateQualifierDescription(cvUnitMaps);
                    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
                
                }catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                }
                setSelectedSequentialDetails(cvMapDetails);
            }
        }
        dlgMaps.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private CoeusVector getMapDetails() throws CoeusClientException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        CoeusVector cvMapDetailsData = null;
        String id=(String)businessRulesMapsForm.tblMap.getValueAt(selectedRow, 0);
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
    
    public void setSelectedSequentialDetails(CoeusVector cvBeansDetails) {
        
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
            
            businessRulesMapsForm.pnlSelectedMapList.removeAll();
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
                    businessRulesMapsForm.pnlSelectedMapList.add(getSequentialStopRowPanel(Integer.parseInt(level)));//isSelectedMapTab
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
                businessRulesMapsForm.pnlSelectedMapList.repaint();
                dlgMaps.validate();
            }
            
        }catch(Exception exp) {
            exp.getMessage();
        }
        
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
            //            valuesForPanel[1] = mapDetailsBean.getUserId();
            //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
            if(mapDetailsBean.isRoleType()){
                if(mapDetailsBean.getQualifierCode().equals("") || mapDetailsBean.getQualifierCode().equals("-1")){
                    valuesForPanel[1] = mapDetailsBean.getRoleDescription();
                }else{
                    valuesForPanel[1] = mapDetailsBean.getRoleDescription() + 
                            "(" +mapDetailsBean.getQualifierDescription() + ")" ;
                }
            }else{
            /*
             * UserID to UserName Enhancement - Start
             * Added UserUtils class to change userid to username
             */
            valuesForPanel[1] = getUpdatedUser(mapDetailsBean.getUserId());
            }
            // UserId to UserName Enhancement - End
            //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
            valuesForPanel[2] = mapDetailsBean.getDetailDescription();
            
            addSequentialDetialsRowstoPanel(valuesForPanel,false);
            
        }
        
        if(mapDetailsNonPrimaryApprover != null && mapDetailsNonPrimaryApprover.size() > 0) {
            
            for(int index= 0; index < mapDetailsNonPrimaryApprover.size();index++) {
                mapDetailsBean =  (MapDetailsBean) mapDetailsNonPrimaryApprover.get(index);
                valuesForPanel[0] = OR_LABEL;
                //                valuesForPanel[1] = mapDetailsBean.getUserId();
                //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
                if(mapDetailsBean.isRoleType()){
                if(mapDetailsBean.getQualifierCode().equals("") || mapDetailsBean.getQualifierCode().equals("-1")){
                    valuesForPanel[1] = mapDetailsBean.getRoleDescription();
                }else{
                    valuesForPanel[1] = mapDetailsBean.getRoleDescription() + 
                            "(" +mapDetailsBean.getQualifierDescription() + ")" ;
                }
            }else{
            /*
             * UserID to UserName Enhancement - Start
             * Added UserUtils class to change userid to username
             */
                valuesForPanel[1] = getUpdatedUser(mapDetailsBean.getUserId());
            }
                // UserId to UserName Enhancement - End
                //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
                valuesForPanel[2] = mapDetailsBean.getDetailDescription();
                addSequentialDetialsRowstoPanel(valuesForPanel,true);
            }
        }
        
    }
    /*
     * UserId to UserName Enhancement - Start
     * getUpdatedUser(String) method added to avoid the repetetive database
     * interaction for getting the username instead of going to the database everytime
     * mainting the values locally in HashMap
     */
    public String getUpdatedUser(String strUserId) {
        String userName = "";
        if(objMap.containsKey(strUserId)) {
            userName =objMap.get(strUserId).toString();
        } else {
            userName = UserUtils.getDisplayName(strUserId);
            objMap.put(strUserId, userName);
        }
        return userName;
    }
    // UserId to UserName Enhancement- End
    
    public void addSequentialDetialsRowstoPanel(String [] valuesForPanel,boolean isRowFormatReq) {
        businessRulesMapsForm.pnlSelectedMapList.add(getApprovedByRowPanel(valuesForPanel,isRowFormatReq));//isSelectedMapTab
    }
    
    //To add the Approved By row to the panel
    private JPanel getApprovedByRowPanel(String elementValues[],boolean isRowFormatReq) {
        
        JPanel pnlApprobedByRow = new JPanel();
        javax.swing.JLabel lblApproveByRow = new javax.swing.JLabel();
        javax.swing.JLabel lblUserIdRow = new javax.swing.JLabel();
        javax.swing.JLabel lblDescriptionRow = new javax.swing.JLabel();
        javax.swing.JCheckBox chkPrimary = new javax.swing.JCheckBox();
        GridBagConstraints gridBagConstraints = null;
        
        
        //========For Select List Tab =============
        
        pnlApprobedByRow.setLayout(new java.awt.GridBagLayout());
        //if(hasModifyMapRight) {
        pnlApprobedByRow.setBackground(new java.awt.Color(255, 255, 255));
        //        }else {
        //            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        //            pnlApprobedByRow.setBackground(bgColor);
        //        }
        pnlApprobedByRow.setMaximumSize(pnlApprobedByRowMaxSize);
        pnlApprobedByRow.setMinimumSize(pnlApprobedByRowMinSize);
        pnlApprobedByRow.setPreferredSize(pnlApprobedByRowPrefSize);
        
        if(isRowFormatReq) {
            lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
            lblApproveByRow.setForeground(Color.blue);
            lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            //Font(String name, int style, int size)
            java.awt.Font italicFont = new java.awt.Font(lblUserIdRow.getFont().getFontName(),
                    java.awt.Font.ITALIC,
                    lblUserIdRow.getFont().getSize());
            
            lblUserIdRow.setFont(italicFont);
            lblDescriptionRow.setFont(italicFont);
            lblApproveByRow.setFont(italicFont);
            
            chkPrimary.setSelected(false);
            
        } else {
            lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
            lblApproveByRow.setForeground(Color.red);
            lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lblUserIdRow.setFont(CoeusFontFactory.getNormalFont());
            lblDescriptionRow.setFont(CoeusFontFactory.getNormalFont());
            chkPrimary.setSelected(true);
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
        //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
        //Changed the size of lblUserId from 85,20 to 150,20
        //lblUserIdRow.setMaximumSize(new java.awt.Dimension(85, 20));
        /*
         *UserId to UserName Enhancement - Start
         *Modified the width of the user id to display username
         */
//        lblUserIdRow.setMinimumSize(new java.awt.Dimension(85, 20));
        //lblUserIdRow.setMinimumSize(new java.awt.Dimension(130, 20));
        //UserId to UserName Enhancement - End
        //lblUserIdRow.setPreferredSize(new java.awt.Dimension(85, 20));
        
        lblUserIdRow.setMaximumSize(new java.awt.Dimension(180, 20));
        lblUserIdRow.setMinimumSize(new java.awt.Dimension(180, 20));
        lblUserIdRow.setPreferredSize(new java.awt.Dimension(180, 20));
        //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlApprobedByRow.add(lblUserIdRow, gridBagConstraints);
        
        /*
         *UserId to UserName Enhancement - Start
         *Modified the width of the user id to display username
         */
//        chkPrimary.setIconTextGap(24);
        chkPrimary.setIconTextGap(260);
        //UserId to UserName Enhancement - End
        chkPrimary.setText(" ");
        //chkPrimary.setSelected(true);
        chkPrimary.setBackground(Color.white);
        chkPrimary.setEnabled(false);
        chkPrimary.setForeground(Color.BLACK);
        chkPrimary.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets=new java.awt.Insets(0, 0, 0, 0);
        pnlApprobedByRow.add(chkPrimary, gridBagConstraints);
        
        lblDescriptionRow.setText(elementValues[2]);
        lblDescriptionRow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblDescriptionRow.setMaximumSize(new java.awt.Dimension(32767, 32767));
        /*
         *UserId to UserName Enhancement - Start
         *Modified the width of the user id to display username
         */
//        lblDescriptionRow.setMinimumSize(new java.awt.Dimension(300, 20));
//        lblDescriptionRow.setPreferredSize(new java.awt.Dimension(300, 20));
        lblDescriptionRow.setMinimumSize(new java.awt.Dimension(460, 20));
        lblDescriptionRow.setPreferredSize(new java.awt.Dimension(460, 20));
        //UserId to UserName Enhancement - End
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets=new java.awt.Insets(0, 10, 0, 0);
        pnlApprobedByRow.add(lblDescriptionRow, gridBagConstraints);
        
        return pnlApprobedByRow;
        
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
    
    //To add the sequential stop row to the panel
    private JPanel getSequentialStopRowPanel(int indexNumber) {
        
        JPanel pnlSequentialStopRow = new JPanel();
        javax.swing.JLabel lblSequentialStop = new javax.swing.JLabel();
        javax.swing.JSeparator sptrSequentialStop = new javax.swing.JSeparator();
        GridBagConstraints gridBagConstraints = null;
        
        javax.swing.JLabel lblDescription = new javax.swing.JLabel();
        javax.swing.JSeparator sptrSequentialStopTop = new javax.swing.JSeparator();
        javax.swing.JLabel lblPrimary = new javax.swing.JLabel();
        
        pnlSequentialStopRow.setLayout(new java.awt.GridBagLayout());
        //if(hasModifyMapRight) {
        pnlSequentialStopRow.setBackground(new java.awt.Color(255, 255, 255));
        // }else {
        // java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        //  pnlSequentialStopRow.setBackground(bgColor);
        // }
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
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        pnlSequentialStopRow.add(sptrSequentialStopTop, gridBagConstraints);
        
        lblSequentialStop.setFont(CoeusFontFactory.getNormalFont());
        lblSequentialStop.setForeground(Color.blue);
        lblSequentialStop.setText("Sequential Stop:   "+indexNumber);
        lblSequentialStop.setMaximumSize(new java.awt.Dimension(32767, 32767));
        //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
        //Changed the size of lblSequentialStop from 170,16 to 280,20
        //lblSequentialStop.setMinimumSize(new java.awt.Dimension(170,16));
        /*
         *UserId to UserName Enhancement - Start
         *Modified the width of the user id to display username
         */
//        lblSequentialStop.setPreferredSize(new java.awt.Dimension(170,16));
        //lblSequentialStop.setPreferredSize(new java.awt.Dimension(260,16));
        //UserId to UserName Enhancement - End
        lblSequentialStop.setMinimumSize(new java.awt.Dimension(300,16));
        lblSequentialStop.setPreferredSize(new java.awt.Dimension(300,16));
        //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
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
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlSequentialStopRow.add(sptrSequentialStop, gridBagConstraints);
        
        lblPrimary.setFont(CoeusFontFactory.getNormalFont());
        lblPrimary.setForeground(Color.blue);
        lblPrimary.setText("Primary");
        lblPrimary.setMaximumSize(new java.awt.Dimension(50, 16));
        lblPrimary.setMinimumSize(new java.awt.Dimension(50, 16));
        lblPrimary.setPreferredSize(new java.awt.Dimension(50, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        //gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSequentialStopRow.add(lblPrimary, gridBagConstraints);
        
        lblDescription.setFont(CoeusFontFactory.getNormalFont());
        lblDescription.setForeground(Color.blue);
        lblDescription.setText("Description");
        lblDescription.setMaximumSize(new java.awt.Dimension(50, 16));
        lblDescription.setMinimumSize(new java.awt.Dimension(50, 16));
        lblDescription.setPreferredSize(new java.awt.Dimension(50, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets=new java.awt.Insets(0, 4, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSequentialStopRow.add(lblDescription, gridBagConstraints);
        return pnlSequentialStopRow;
        
    }
    
    /**
     * Getter for property okClicked.
     * @return Value of property okClicked.
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Setter for property okClicked.
     * @param okClicked New value of property okClicked.
     */
    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
      //Added Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
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
     //Added Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    
    private class MapTableModel extends AbstractTableModel {
        
        String colNames[] = {"Id","Type","Description"};
        Class[] colTypes = new Class[] {String.class,String.class,String.class};
        
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
                case COLUMN_TYPE:
                    String mapType="";
                    if(mapHeaderBean.getMapType().equals("I")){
                        mapType = "Indirect";
                    }else if(mapHeaderBean.getMapType().equals("D")){
                        mapType = "Direct";
                    }
                    return mapType;
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
                case COLUMN_TYPE:
                    mapHeaderBean.setMapType(value.toString());
                    break;
                case COLUMN_DESCRIPTION:
                    mapHeaderBean.setMapDescription(value.toString());
                    break;
                    
            }
        }
        
    }
    
}
