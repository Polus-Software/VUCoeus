/*
 * CustomElementsontroller.java
 *
 * Created on December 9, 2004, 5:00 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.CustomElementsForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.customelements.bean.CustomElementsUsageBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.ObjectCloner;

import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.table.*;
import javax.swing.*;
import java.util.Hashtable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Or;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Component;
/**
 *
 * @author  shijiv
 */
 public class CustomElementsController extends AdminController implements ActionListener, MouseListener,ListSelectionListener,KeyListener  {
    
    private static final int WIDTH = 960 ;
    private static final int HEIGHT = 490;
    private static final int COLUMN_NAME = 0;
    private static final int COLUMN_LABEL = 1;
    private static final int DATA_TYPE = 2;
    private static final int DATA_LENGTH = 3;
    private static final int DEFAULT_VALUE = 4;
    private static final int HAS_LOOKUP = 5;
    private static final int LOOKUP_WINDOW = 6;
    private static final int LOOKUP_ARGUMENTS = 7;
    private static final int MODULE = 0;
    private static final int REQUIRED = 1;
    private static final String WINDOW_TITLE = "Coeus Modules Custom Data Elements";
    private static final char GET_CUSTOM_ELEMENTS='U';
    private static final char DELETE_CUSTOM_ELEMENT='V';
    private static final char UPDATE_CUSTOM_ELEMENTS='a';
    private final String BUDGET_SERVLET ="/BudgetMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_SERVLET;
    private static final String EMPTY_STRING = "";
    
    private CoeusAppletMDIForm mdiForm;
    private CustomElementsForm customElementsForm;
    private CoeusDlgWindow dlgCustomElements;
    private CustomElementsTableModel customElementsTableModel;
    private CustomElemColumnUsageTableModel customElemColumnUsageTableModel;
    boolean modified=false;
    boolean hasRight= false;
    private CoeusMessageResources coeusMessageResources;
    private CustomElementsInfoBean customElementsInfoBean;
    private CustomElementsUsageBean customElementsUsageBean;
    private CoeusVector cvCustomElements;
    private CoeusVector cvColumnUsage,cvFilterColumnUsage;
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    private CoeusVector cvNewCustomElementsData,cvNewColumnUsageData;
    private Hashtable hColumnUsage;
    private boolean isModified = false; 
    private ColumnUsageRenderer columnUsageRenderer;
    
    
    /** Creates a new instance of CustomElementsontroller */
    public CustomElementsController(CoeusAppletMDIForm mdiForm) {
         this.mdiForm=mdiForm;
         coeusMessageResources = CoeusMessageResources.getInstance();
         cvNewCustomElementsData= new CoeusVector();
         cvNewColumnUsageData = new CoeusVector();
         hColumnUsage= new Hashtable();
          postInitComponents();
        registerComponents();
        try {
            setFormData(null);
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        setTableColumn();
        formatFields();
    }
    
    private void postInitComponents() {
        customElementsForm = new CustomElementsForm();
        columnUsageRenderer= new ColumnUsageRenderer();
        dlgCustomElements = new CoeusDlgWindow(mdiForm);
        dlgCustomElements.setResizable(false);
        dlgCustomElements.setModal(true);
        dlgCustomElements.getContentPane().add(customElementsForm);
        dlgCustomElements.setFont(CoeusFontFactory.getLabelFont());
        dlgCustomElements.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgCustomElements.setSize(WIDTH, HEIGHT);
        dlgCustomElements.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgCustomElements.getSize();
        dlgCustomElements.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgCustomElements.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgCustomElements.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        dlgCustomElements.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    public void setWindowFocus() {
       customElementsForm.scrPnCustomElements.requestFocusInWindow();
    }
    
    private void performCancelAction() {
        if(saveRequired()){
            confirmClosing();
        }else{
            dlgCustomElements.setVisible(false);
        }
    }
    
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                if(validate()){
                    saveCustomData();
                    dlgCustomElements.setVisible(false);
                }
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setSaveRequired(false);
                dlgCustomElements.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    public void display() {
        //dlgValidateRateType.show();
        customElementsForm.tblCustomElements.setRowSelectionAllowed(true);
        if(customElementsForm.tblCustomElements.getRowCount() > 0) {
            customElementsForm.tblCustomElements.setRowSelectionInterval(0,0);
        }
        //Bug Fix: 1595 Start 1
        else {
            customElemColumnUsageTableModel.setData(new CoeusVector());
        }
        //Bug Fix: 1595 End 1
        dlgCustomElements.setVisible(true);
    }
    
    private void setTableColumn() {
        JTableHeader tableHeader = customElementsForm.tblCustomElements.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        customElementsForm.tblCustomElements.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        customElementsForm.tblCustomElements.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        customElementsForm.tblCustomElements.setCellSelectionEnabled(false);
        
        TableColumn column = customElementsForm.tblCustomElements.getColumnModel().getColumn(COLUMN_NAME);
        column.setPreferredWidth(153);
        column.setResizable(true);
        
        column = customElementsForm.tblCustomElements.getColumnModel().getColumn(COLUMN_LABEL);
        column.setPreferredWidth(130);
        column.setResizable(true);
        
        column = customElementsForm.tblCustomElements.getColumnModel().getColumn(DATA_TYPE);
        column.setPreferredWidth(75);
        column.setResizable(true);
        
        column = customElementsForm.tblCustomElements.getColumnModel().getColumn(DATA_LENGTH);
        column.setPreferredWidth(86);
        column.setResizable(true);
        
        column = customElementsForm.tblCustomElements.getColumnModel().getColumn(DEFAULT_VALUE);
        column.setPreferredWidth(93);
        column.setResizable(true);
        
        column = customElementsForm.tblCustomElements.getColumnModel().getColumn(HAS_LOOKUP);
        column.setPreferredWidth(85);
        column.setResizable(true);
        
        column = customElementsForm.tblCustomElements.getColumnModel().getColumn(LOOKUP_WINDOW);
        column.setPreferredWidth(108);
        column.setResizable(true);
        
        column = customElementsForm.tblCustomElements.getColumnModel().getColumn(LOOKUP_ARGUMENTS);
        column.setPreferredWidth(115);
        column.setResizable(true);
        
        tableHeader = customElementsForm.tblColumnUsage.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        customElementsForm.tblColumnUsage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        customElementsForm.tblColumnUsage.setShowHorizontalLines(false);
        customElementsForm.tblColumnUsage.setShowVerticalLines(false);
        customElementsForm.tblColumnUsage.setRowSelectionAllowed(false);
        customElementsForm.tblColumnUsage.setCellSelectionEnabled(false);
        
        column = customElementsForm.tblColumnUsage.getColumnModel().getColumn(MODULE);
        column.setPreferredWidth(283);
        column.setResizable(false);
        
        
        column = customElementsForm.tblColumnUsage.getColumnModel().getColumn(REQUIRED);
        column. setPreferredWidth(565);
        column.setCellRenderer(columnUsageRenderer);
        column.setResizable(false);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        customElementsForm.tblCustomElements.setBackground(bgColor);
        customElementsForm.tblColumnUsage.setBackground(bgColor);
    }
    
    
    public void formatFields() {
        if(!hasRight){
            customElementsForm.btnAdd.setEnabled(false);
            customElementsForm.btnModify.setEnabled(false);
            customElementsForm.btnDelete.setEnabled(false);
        }
    }
    
    public java.awt.Component getControlledUI() {
        return customElementsForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        customElementsTableModel = new CustomElementsTableModel();
        customElemColumnUsageTableModel = new CustomElemColumnUsageTableModel();
        java.awt.Component[] components = {customElementsForm.scrPnCustomElements,
        customElementsForm.scrPnColumnUsage,
        customElementsForm.btnOk,
        customElementsForm.btnCancel,
        customElementsForm.btnAdd,
        customElementsForm.btnDelete,
        customElementsForm.btnModify,
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        customElementsForm.setFocusTraversalPolicy(traversePolicy);
        customElementsForm.setFocusCycleRoot(true);
        customElementsForm.btnAdd.addActionListener(this);
        customElementsForm.btnCancel.addActionListener(this);
        customElementsForm.btnOk.addActionListener(this);
        customElementsForm.btnModify.addActionListener(this);
        customElementsForm.btnDelete.addActionListener(this);
        customElementsForm.tblCustomElements.getTableHeader().addMouseListener(this);
        customElementsForm.tblCustomElements.setModel(customElementsTableModel);
        customElementsForm.tblColumnUsage.setModel(customElemColumnUsageTableModel);
        ListSelectionModel selectionModel = customElementsForm.tblCustomElements.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        customElementsForm.tblCustomElements.setSelectionModel(selectionModel);
        customElementsForm.tblCustomElements.addKeyListener(this);
        
    }
    private CoeusVector cvUnchanged;
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException{
        try {
        Hashtable customElementsData;
        customElementsData=getCustomElementsData();
        hasRight=((Boolean)customElementsData.get(KeyConstants.AUTHORIZATION_CHECK)).booleanValue();
        cvCustomElements=(CoeusVector)customElementsData.get(CustomElementsInfoBean.class);
        cvColumnUsage = (CoeusVector)customElementsData.get(CustomElementsUsageBean.class);
        cvUnchanged = (CoeusVector)customElementsData.get(CustomElementsUsageBean.class);
        
        //Bug Fix: 1595 Start 2
        customElementsTableModel.setData(cvCustomElements);
        //Bug Fix: 1595 End 2
        
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    public Hashtable getCustomElementsData() throws CoeusClientException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable customElemData = null;
        
        requesterBean.setFunctionType(GET_CUSTOM_ELEMENTS);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                customElemData = (Hashtable)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return customElemData;
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        dlgCustomElements.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(customElementsForm.btnCancel)) {
           performCancelAction();
        }else if(source.equals(customElementsForm.btnAdd)) {
           performAddAction(); 
        }else if(source.equals(customElementsForm.btnModify)) {
            performModifyAction();
        }else if(source.equals(customElementsForm.btnDelete)) {
            performDeleteAction();
        }else if(source.equals(customElementsForm.btnOk)) {
            performOkAction();
        }
        dlgCustomElements.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public void performAddAction() {
        Hashtable hData= new Hashtable();
        try {
            Hashtable htData = new Hashtable();
            htData.put(CoeusVector.class, cvCustomElements);
            AddModifyCustomElementsController addModifyCustomElementsController = new AddModifyCustomElementsController(htData,'N');
            hData=addModifyCustomElementsController.showCustomElementData();
        }catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        if(hData == null)
            return;
        CustomElementsInfoBean cusInfoBean =(CustomElementsInfoBean)hData.get(CustomElementsInfoBean.class);
        if(cusInfoBean!= null){
            if(cusInfoBean.getAcType() != null) {
                isModified=true;
                cvCustomElements.add(cusInfoBean);
                //cvNewCustomElementsData.add(cusInfoBean);
                //cvCustomElements.add(cusInfoBean);
                customElementsTableModel.fireTableRowsInserted(cvCustomElements.size()-1,cvCustomElements.size()-1);
                customElementsForm.tblCustomElements.setRowSelectionInterval(cvCustomElements.size()-1,cvCustomElements.size()-1);
                customElementsForm.tblCustomElements.scrollRectToVisible(
                    customElementsForm.tblCustomElements.getCellRect(cvCustomElements.size()-1 ,0, true));
            }
            CoeusVector cvUsage=(CoeusVector)hData.get(CustomElementsUsageBean.class);
            for(int index=0;index<cvUsage.size();index++) {
                CustomElementsUsageBean cusUsageBean=(CustomElementsUsageBean)cvUsage.get(index);
                if(cusUsageBean.getAcType() != null) {
                    isModified=true;
                    cvNewColumnUsageData.add(cusUsageBean);
                }
            }
            cvFilterColumnUsage=cvUsage;
            hColumnUsage.put(new Integer(cvCustomElements.size()-1),cvUsage);
            customElemColumnUsageTableModel.fireTableDataChanged();
            doClean(cvCustomElements.size()-1);
        }
    }
    
    public void performModifyAction() {
        Hashtable htModify= new Hashtable();
        Hashtable hData=new Hashtable();
        CoeusVector cvColUsage= new CoeusVector();
        int selectedRow = customElementsForm.tblCustomElements.getSelectedRow();
        //Bug Fix:1595 Start 5
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protoBaseWin_exceptionCode.1051"));
            return ;
        }
        //Bug Fix:1595 End 5
        CoeusVector cvFilter=new CoeusVector();
        try {
            cvFilter = (CoeusVector)ObjectCloner.deepCopy(cvFilterColumnUsage);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        CustomElementsInfoBean customElemInfoBean =(CustomElementsInfoBean)cvCustomElements.get(selectedRow);
        htModify.put(CustomElementsInfoBean.class, customElemInfoBean);
        htModify.put(CustomElementsUsageBean.class,cvFilter);
        htModify.put(CoeusVector.class,cvCustomElements);
        try {
            AddModifyCustomElementsController addModifyCustomElementsController = new AddModifyCustomElementsController(htModify,TypeConstants.MODIFY_MODE);
            hData=addModifyCustomElementsController.showCustomElementData();
        }catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        if(hData == null) {
            return;
        }
        CustomElementsInfoBean cusInfoBean =(CustomElementsInfoBean)hData.get(CustomElementsInfoBean.class);
        if(cusInfoBean!= null){
            if(cusInfoBean.getAcType() != null) {
                isModified= true;
                cvCustomElements.removeElementAt(selectedRow);
                cvCustomElements.add(selectedRow,cusInfoBean);
                //cvNewCustomElementsData.add(cusInfoBean);
                cvCustomElements.set(selectedRow,cusInfoBean);
                customElementsForm.tblCustomElements.setRowSelectionInterval(selectedRow ,selectedRow );
                customElementsForm.tblCustomElements.scrollRectToVisible(
                customElementsForm.tblCustomElements.getCellRect(selectedRow ,0, true));
                customElementsTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            }
            CoeusVector cvUsage=(CoeusVector)hData.get(CustomElementsUsageBean.class);
            // You can find this code at doClean() method called in valueChanged
            
            //        for(int index=0;index<cvUsage.size();index++) {
            //            CustomElementsUsageBean cusUsageBean=(CustomElementsUsageBean)cvUsage.get(index);
            //            if(cusUsageBean.getAcType() != null) {
            //                isModified=true;
            //                cvNewColumnUsageData.add(cusUsageBean);
            //            }
            //            if(cusUsageBean.getAcType()==TypeConstants.DELETE_RECORD){
            //                cvUsage.remove(index);
            //            }
            //        }
            
            hColumnUsage.put(new Integer(selectedRow),cvUsage);
            cvFilterColumnUsage=cvUsage;
            doClean(selectedRow);
            customElemColumnUsageTableModel.fireTableDataChanged();
        }
    }
    
    public void saveModifiedCustomElements(Hashtable hData) throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
       //  boolean deleteStatus=false;
        
        requesterBean.setFunctionType(UPDATE_CUSTOM_ELEMENTS);
        requesterBean.setDataObject(hData);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        //if(cvTableName!= null && cvTableName.size() > 0){
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                dlgCustomElements.setVisible(false);
                //customElementsTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
           }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    public void performDeleteAction() {
        try {
            int selectedRow = customElementsForm.tblCustomElements.getSelectedRow();
            if(selectedRow == -1){
                //Bug Fix: 1595 Start 6
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("orgIDCPnl_exceptionCode.1097"));
                //Bug Fix: 1595 End 6
                
                return;
            }
            CustomElementsInfoBean customElemInfoBean =(CustomElementsInfoBean)cvCustomElements.get(selectedRow);
            String column_name=customElementsForm.tblCustomElements.getValueAt(selectedRow, 0).toString();

           
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("customElementExceptionCode.1551")+""+column_name+" "+coeusMessageResources.parseMessageKey("customElementExceptionCode.1552")+" "+column_name+coeusMessageResources.parseMessageKey("customElementExceptionCode.1553"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);

            if(option == CoeusOptionPane.SELECTION_YES){
                deleteRow(customElemInfoBean,selectedRow);
            }else if(option == CoeusOptionPane.SELECTION_NO){
                return;
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
            CoeusOptionPane.showDialog(coeusClientException);
            
        }
        
    }
    
    public void deleteRow(CustomElementsInfoBean customElemInfoBean,int selectedRow) throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
         boolean deleteStatus=false;
        requesterBean.setFunctionType(DELETE_CUSTOM_ELEMENT);
//        customElemInfoBean.setAcType(TypeConstants.DELETE_RECORD);
        requesterBean.setDataObject(customElemInfoBean);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        //if(cvTableName!= null && cvTableName.size() > 0){
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                CoeusVector cvResponse=(CoeusVector)responderBean.getDataObjects();
                boolean status=((Boolean)cvResponse.get(cvResponse.size()-1)).booleanValue();
                if(!status) {
                    String table_name=cvResponse.get(0).toString();
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1554")+" "+table_name+coeusMessageResources.parseMessageKey("customElementExceptionCode.1555"));
                }else {
                    //Added for Bug Fix - 1432 Start - 7-feb-2005
                    customElemInfoBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvCustomElements.removeElementAt(selectedRow);
                    cvNewCustomElementsData.addElement(customElemInfoBean);
                    for(int index = 0; index<cvFilterColumnUsage.size();index++) {
                        customElementsUsageBean = (CustomElementsUsageBean)cvFilterColumnUsage.elementAt(index);
                        customElementsUsageBean.setAcType(TypeConstants.DELETE_RECORD);
                        cvNewColumnUsageData.addElement(customElementsUsageBean);
                    }           
                    //End - 7-feb-2005 
                    cvFilterColumnUsage.removeAllElements();
                    customElementsTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    customElemColumnUsageTableModel.fireTableDataChanged();
                }
           }else{
//                cvCustomElements.removeElementAt(selectedRow);
//                customElementsTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
//                //CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1556"));
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean saveRequired() {
        Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
        cvNewCustomElementsData.addAll(cvCustomElements.filter(insertOrUpdate));
        cvNewColumnUsageData = cvNewColumnUsageData.filter(insertOrUpdateOrDelete);
        if(cvNewCustomElementsData.size() > 0 || cvNewColumnUsageData.size()>0)
            isModified = true;
        return isModified;
    }
    
    public void performOkAction() {
        if(saveRequired()) {
            saveCustomData();
        }else {
            dlgCustomElements.setVisible(false);
        }
    }
    
    private void saveCustomData() {
        try{
            Hashtable hModified= new Hashtable();
            hModified.put(CustomElementsInfoBean.class,cvNewCustomElementsData);
            // hModified.put(CustomElementsInfoBean.class,cvNewCustomElementsData);
            hModified.put(CustomElementsUsageBean.class, cvNewColumnUsageData);
            saveModifiedCustomElements(hModified);
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        Point clickedPoint = mouseEvent.getPoint();
        int xPosition = (int)clickedPoint.getX();
        int selectedRow = customElementsForm.tblCustomElements.getSelectedRow();
        CustomElementsInfoBean custInfoBean=(CustomElementsInfoBean)cvCustomElements.get(selectedRow);
        
        int columnIndex = customElementsForm.tblCustomElements.getColumnModel().getColumnIndexAtX(xPosition);
        switch (columnIndex) {
            case COLUMN_NAME:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("columnName", false);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("columnName", true);
                        sortCodeAsc = true;
                }
                break;
            case COLUMN_LABEL:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("columnLabel", false,true);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("columnLabel", true,true);
                        sortCodeAsc = true;
                }
               break;
            case DATA_TYPE:
                 if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("dataType", false);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("dataType", true);
                        sortCodeAsc = true;
                }
               break;
            case DATA_LENGTH:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("dataLength", false);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("dataLength", true);
                        sortCodeAsc = true;
                }
               break;
            case DEFAULT_VALUE:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("defaultValue", false);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("defaultValue", true);
                        sortCodeAsc = true;
                }
               break;
            case HAS_LOOKUP:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("lookUpReuired", false);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("lookUpReuired", true);
                        sortCodeAsc = true;
                }
               break;
            case LOOKUP_WINDOW:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("lookUpWindow", false);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("lookUpWindow", true);
                        sortCodeAsc = true;
                }
               break;
            case LOOKUP_ARGUMENTS:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                     cvCustomElements.sort("lookUpArgument", false);
                     sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                        cvCustomElements.sort("lookUpArgument", true);
                        sortCodeAsc = true;
                }
               break;
        }//End Switch
        
         customElementsTableModel.fireTableDataChanged();
         customElementsForm.tblCustomElements.setRowSelectionInterval(cvCustomElements.indexOf(custInfoBean),cvCustomElements.indexOf(custInfoBean));
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
//         if( !e.getValueIsAdjusting() ){
            int selectedRow = customElementsForm.tblCustomElements.getSelectedRow();
            if (selectedRow != -1) {
                if(!hColumnUsage.containsKey(new Integer(selectedRow))){
                setDataToColumnUsageTable(selectedRow);
                }else {
                    cvFilterColumnUsage=(CoeusVector)hColumnUsage.get(new Integer(selectedRow));
                    customElemColumnUsageTableModel.setData(cvFilterColumnUsage);
                }
//                doClean(selectedRow);
            }
//        }
    }
    
    private void doClean(int selRow) {
        CustomElementsInfoBean bean = (CustomElementsInfoBean)cvCustomElements.get(selRow);
        String colName = bean.getColumnName();
        Equals eqColName= new Equals("columnName",colName);
        CoeusVector cvData = cvNewColumnUsageData.filter(eqColName);
        if(bean.getAcType()==TypeConstants.DELETE_RECORD) {
            cvNewColumnUsageData.removeAll(cvData);
        }else {
            cvNewColumnUsageData.removeAll(cvData);
            cvNewColumnUsageData.addAll(cvFilterColumnUsage);
        }
        for(int index=0;index<cvFilterColumnUsage.size();index++) {
            CustomElementsUsageBean cusUsageBean=(CustomElementsUsageBean)cvFilterColumnUsage.get(index);
            if(cusUsageBean.getAcType()==TypeConstants.DELETE_RECORD){
                cvFilterColumnUsage.remove(index);
            }
        }
    }
    
    public void setDataToColumnUsageTable(int selectedRow) {
        String colName=customElementsForm.tblCustomElements.getValueAt(selectedRow,0).toString();
        Equals eqColName= new Equals("columnName",colName);
        cvFilterColumnUsage = cvColumnUsage.filter(eqColName);
        customElemColumnUsageTableModel.setData(cvFilterColumnUsage);
    }
    
    private void getColumnUsageData(int selectedRow) {
        String colName=customElementsForm.tblCustomElements.getValueAt(selectedRow,0).toString();
        Equals eqColName= new Equals("columnName",colName);
        cvFilterColumnUsage = cvUnchanged.filter(eqColName);
        customElemColumnUsageTableModel.setData(cvFilterColumnUsage);
    }
    
    public void keyPressed(KeyEvent e) {
        int source=e.getKeyCode();
        int selectedRow=customElementsForm.tblCustomElements.getSelectedRow();
        if(source == KeyEvent.VK_DOWN) {
            customElementsForm.tblCustomElements.requestFocusInWindow();
            customElementsForm.tblCustomElements.setRowSelectionInterval(selectedRow ,selectedRow);
        }else if(source == KeyEvent.VK_UP) {
            customElementsForm.tblCustomElements.requestFocusInWindow();
            customElementsForm.tblCustomElements.setRowSelectionInterval(selectedRow ,selectedRow);
        }
    }

    public void keyReleased(KeyEvent e) {
    }    
    
    public void keyTyped(KeyEvent e) {
    }
    
    class CustomElementsTableModel extends AbstractTableModel {
        String colNames[] = {"Column Name","Column Label","Data Type","Data Length","Default Value","Has Lookup","Lookup Window","Lookup Argument"};
        Class[] colTypes = new Class[] {String.class,String.class,String.class,Integer.class,String.class,String.class,String.class,String.class};
        
        CustomElementsTableModel() {
            
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
        
        //Bug Fix: 1595 Changed the name of the argument Start 3 
        public void setData(CoeusVector cvCustElements){
           cvCustomElements=cvCustElements;
           fireTableDataChanged();
        }
        //Bug Fix: 1595 End 3
        
        public int getRowCount() {
            if(cvCustomElements == null){
                return 0;
            } else{
                return cvCustomElements.size();
            }
        }

        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            CustomElementsInfoBean customElementsInfoBean = (CustomElementsInfoBean)cvCustomElements.get(row);
            switch(column) {
                case COLUMN_NAME:
                    return customElementsInfoBean.getColumnName();
                case COLUMN_LABEL:
                    return customElementsInfoBean.getColumnLabel();
                case DATA_TYPE:
                    return customElementsInfoBean.getDataType();
                case DATA_LENGTH:
                    return (new Integer(customElementsInfoBean.getDataLength()));
                case DEFAULT_VALUE:
                    return customElementsInfoBean.getDefaultValue();
                case HAS_LOOKUP:
                    String hasLookup="";
                    if(customElementsInfoBean.isHasLookUp()) {
                        hasLookup="Yes";
                    }else if(!customElementsInfoBean.isHasLookUp()) {
                        hasLookup="No";
                    }
                    return hasLookup;
                case LOOKUP_WINDOW:
                    return customElementsInfoBean.getLookUpWindow();
                case LOOKUP_ARGUMENTS:
                    return customElementsInfoBean.getLookUpArgument();
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(cvCustomElements== null) 
                return;
            CustomElementsInfoBean customElementsInfoBean = (CustomElementsInfoBean)cvCustomElements.get(row);
            switch(column) {
                case COLUMN_NAME:
                    customElementsInfoBean.setColumnName(value.toString());
                case COLUMN_LABEL:
                    customElementsInfoBean.setColumnLabel(value.toString());
                case DATA_TYPE:
                    customElementsInfoBean.setDataType(value.toString());
                case DATA_LENGTH:
                    customElementsInfoBean.setDataLength(((Integer)value).intValue());
                case DEFAULT_VALUE:
                    customElementsInfoBean.setDefaultValue(value.toString());
                case HAS_LOOKUP:
                    String hasLookup=value.toString();
                    if(hasLookup.equals("Yes")) {
                        customElementsInfoBean.setHasLookUp(true);
                    }else if(hasLookup.equals("No")) {
                        customElementsInfoBean.setHasLookUp(false);
                    }
                case LOOKUP_WINDOW:
                    customElementsInfoBean.setLookUpWindow(value.toString());
                case LOOKUP_ARGUMENTS:
                    customElementsInfoBean.setLookUpArgument(value.toString());
            }
        }
        
    }
    
    class CustomElemColumnUsageTableModel extends AbstractTableModel {
        String colNames[] = {"Module","Required"};
        Class[] colTypes = new Class[] {String.class,Boolean.class};
        
        CustomElemColumnUsageTableModel() {
            
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
        
        //Bug Fix: 1595 Changed the name of the argument Start 4 
        public void setData(CoeusVector cvFilterColUsage){
           cvFilterColumnUsage=cvFilterColUsage;
           fireTableDataChanged();
        }
        //Bug Fix: 1595 Changed the name of the argument End 4
        
        public int getRowCount() {
            if(cvCustomElements == null){
                return 0;
            } else{
                return cvFilterColumnUsage.size();
            }
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            CustomElementsUsageBean customElementsUsageBean = (CustomElementsUsageBean)cvFilterColumnUsage.get(row);
            switch(column) {
                case MODULE:
                    return customElementsUsageBean.getDescription();
                case REQUIRED:
                    return new Boolean(customElementsUsageBean.isIsRequired());
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(cvColumnUsage==null)
                return;
            CustomElementsUsageBean customElementsUsageBean = (CustomElementsUsageBean)cvFilterColumnUsage.get(row);
            switch(column) {
                case MODULE:
                    customElementsUsageBean.setDescription(value.toString());
                case REQUIRED:
                    customElementsUsageBean.setIsRequired(((Boolean)value).booleanValue());
            }
        }
        
    }
    
    
      public class ColumnUsageRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
            JLabel lblModule;
            JCheckBox chkIsRequired;
        java.awt.Color backgroundColor = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
        public ColumnUsageRenderer() {
            lblModule = new JLabel();
            chkIsRequired = new JCheckBox();
        }
        
        /**
         * @param JTable table
         * @param Object value
         * @param boolean isSelected
         * @param boolean hasFocus
         * @param boolean hasFocus
         * @param int row
         * @param int col
         * @return Component
         */
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected,boolean hasFocus ,int row, int col) {
            switch(col) {
                case MODULE:
                    lblModule.setText((String)value);
                    return lblModule;
                case REQUIRED:
                   chkIsRequired.setSelected(((Boolean)value).booleanValue());
                   if(isSelected)
                       chkIsRequired.setBackground(table.getSelectionBackground());
                   if(!isSelected)
                       chkIsRequired.setBackground(backgroundColor);
                   return chkIsRequired;
            }
            return lblModule;
        }
    }
}
