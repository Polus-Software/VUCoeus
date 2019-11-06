/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * ValidRateTypeController.java
 *
 * Created on November 18, 2004, 11:53 AM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.ValidRateTypesForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.rates.bean.RateClassBean;
import edu.mit.coeus.rates.bean.RateTypeBean;
import edu.mit.coeus.budget.bean.CERateTypeBean;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.ObjectCloner;


//import java.awt.event.ActionEvent;
//import java.awt.event.ItemListener;
//import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.table.AbstractTableModel;
import java.util.Hashtable;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.Point;
import java.awt.Color;
import java.awt.Cursor;

/**
 *
 * @author  shijiv
 */
public class ValidRateTypeController extends AdminController implements ActionListener,ItemListener,MouseListener{
    
    private static final int RATE_TYPE_CODE_COL = 0;
    private static final int RATE_TYPE_DESC_COL = 1;
    private static final char GET_VALID_RATES = 'P' ;
    private static final String EMPTY_STRING = "";
    private static final int WIDTH = 780 ;
    private static final int HEIGHT = 475;
    private final String BUDGET_SERVLET ="/BudgetMaintenanceServlet";
    private static final String WINDOW_TITLE = "Valid Cost Element Rate Types";
    private static final String SERVLET = "/BudgetMaintenanceServlet";
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_SERVLET;
    private ValidRateTypesForm validRateTypesForm;
    private CoeusDlgWindow dlgValidateRateType;
    private CoeusAppletMDIForm mdiForm;
    private RateClassBean  rateClassBean,prevRateClassBean;
    private RateTypeBean  rateTypeBean;
    private CoeusVector cvRateClass, cvRateType, cvApplicable,cvAvailable,cvFilterApplicableRate;
    private CoeusVector cvFilterAvailableRate,cvFilterRateType,cvRateClassConstants;
    private ApplicableCostElemTableModel applicableCostElemTableModel;
    private AvailableCostElementsTableModel availableCostElementsTableModel;
    private String vacationRateClassCode,ebRateClassCode;
    boolean hasRight,modified;
    private CoeusMessageResources coeusMessageResources;
    private CoeusVector cvOriginalAppValidRate;
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    private CoeusVector modifiedRateClassTypes;
    private CoeusVector modifiedAppRateOnRateClass;
    private MultipleTableColumnSorter sorter,sorterAv;
    
    // private CoeusVector cvAdded
    
    /** Creates a new instance of ValidRateTypeController */
    public ValidRateTypeController(CoeusAppletMDIForm mdiForm) {
        this.mdiForm=mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        modifiedRateClassTypes=new CoeusVector();
        cvOriginalAppValidRate =  new CoeusVector();
        modifiedAppRateOnRateClass= new CoeusVector();
        //Added for bug id :1902 & 1930 step 1 - start
        cvFilterApplicableRate = new CoeusVector();
        //bug id :1902 & 1930 step 1 - end
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
        validRateTypesForm = new ValidRateTypesForm();
        dlgValidateRateType = new CoeusDlgWindow(mdiForm);
        dlgValidateRateType.setResizable(false);
        dlgValidateRateType.setModal(true);
        dlgValidateRateType.getContentPane().add(validRateTypesForm);
        dlgValidateRateType.setFont(CoeusFontFactory.getLabelFont());
        dlgValidateRateType.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgValidateRateType.setSize(WIDTH, HEIGHT);
        dlgValidateRateType.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgValidateRateType.getSize();
        dlgValidateRateType.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgValidateRateType.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgValidateRateType.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        dlgValidateRateType.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    public void setWindowFocus() {
        if(hasRight){
            validRateTypesForm.btnOk.requestFocusInWindow();
        }else{
            validRateTypesForm.btnCancel.requestFocusInWindow();
        }
    }
    
    public void display() {
        //dlgValidateRateType.show();
        dlgValidateRateType.setVisible(true);
    }
    
    public void formatFields() {
        if(!hasRight){
            validRateTypesForm.btnAdd.setEnabled(false);
            validRateTypesForm.btnRemove.setEnabled(false);
            validRateTypesForm.btnOk.setEnabled(false);
            validRateTypesForm.tblApplicationCostElements.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
            validRateTypesForm.tblCostElements.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    public java.awt.Component getControlledUI() {
        return validRateTypesForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        
        applicableCostElemTableModel = new ApplicableCostElemTableModel();
        availableCostElementsTableModel = new AvailableCostElementsTableModel();
        java.awt.Component[] components = {validRateTypesForm.cmbRateClass,
        validRateTypesForm.cmbRateType,
        validRateTypesForm.btnAdd,
        validRateTypesForm.btnRemove,
        validRateTypesForm.btnOk,
        validRateTypesForm.btnCancel};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        validRateTypesForm.setFocusTraversalPolicy(traversePolicy);
        validRateTypesForm.setFocusCycleRoot(true);
        validRateTypesForm.btnAdd.addActionListener(this);
        validRateTypesForm.btnCancel.addActionListener(this);
        validRateTypesForm.btnOk.addActionListener(this);
        validRateTypesForm.btnRemove.addActionListener(this);
        validRateTypesForm.cmbRateClass.addItemListener(this);
        validRateTypesForm.cmbRateType.addItemListener(this);
        validRateTypesForm.tblApplicationCostElements.getTableHeader().addMouseListener(this);
        validRateTypesForm.tblCostElements.getTableHeader().addMouseListener(this);
        validRateTypesForm.tblApplicationCostElements.setModel(applicableCostElemTableModel);
        validRateTypesForm.tblCostElements.setModel(availableCostElementsTableModel);
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException{
        try{
            Hashtable validRatesData;
            cvRateClass=new CoeusVector();
            cvRateType = new CoeusVector();
            cvApplicable = new CoeusVector();
            cvAvailable = new CoeusVector();
            //case 2303 start
            modifiedRateClassTypes=new CoeusVector();
            cvOriginalAppValidRate =  new CoeusVector();
            modifiedAppRateOnRateClass= new CoeusVector();
            //case 2303 end
            // get the data by making server call
            validRatesData = getValidRateTypes();
            
            cvApplicable = (CoeusVector)validRatesData.get(new Integer(0));
            cvAvailable = (CoeusVector)validRatesData.get(new Integer(1));
            cvRateType = (CoeusVector)validRatesData.get(new Integer(2));
            cvRateClass = (CoeusVector)validRatesData.get(new Integer(3));
            hasRight = ((Boolean)validRatesData.get(new Integer(4))).booleanValue();
            cvRateClassConstants=(CoeusVector)validRatesData.get(new Integer(5));
            if(cvRateClassConstants!= null && cvRateClassConstants.size() > 0){
                vacationRateClassCode=(String)cvRateClassConstants.elementAt(0);
                ebRateClassCode=(String)cvRateClassConstants.elementAt(1);
            }
            setComboData(cvRateClass);
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    private Hashtable getValidRateTypes()throws CoeusClientException{
        RequesterBean   requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable validRateTypeData = null;
        
        requesterBean.setFunctionType(GET_VALID_RATES);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                validRateTypeData = (Hashtable)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return validRateTypeData;
    }
    
    private void setTableColumn() {
        JTableHeader tableHeader = validRateTypesForm.tblApplicationCostElements.getTableHeader();
        tableHeader.setReorderingAllowed(false);
//        if( sorter == null ) {
//            sorter = new MultipleTableColumnSorter((TableModel)validRateTypesForm.tblApplicationCostElements.getModel());
//            sorter.setTableHeader(validRateTypesForm.tblApplicationCostElements.getTableHeader());
//            validRateTypesForm.tblApplicationCostElements.setModel(sorter);
//        }
//        if( sorterAv == null ) {
//            sorterAv = new MultipleTableColumnSorter((TableModel)validRateTypesForm.tblCostElements.getModel());
//            sorterAv.setTableHeader(validRateTypesForm.tblCostElements.getTableHeader());
//            validRateTypesForm.tblApplicationCostElements.setModel(sorterAv);
//        }
        
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        validRateTypesForm.tblApplicationCostElements.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        validRateTypesForm.tblApplicationCostElements.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn column = validRateTypesForm.tblApplicationCostElements.getColumnModel().getColumn(RATE_TYPE_CODE_COL);
        column.setPreferredWidth(65);
        column.setResizable(true);
        
        
        column = validRateTypesForm.tblApplicationCostElements.getColumnModel().getColumn(RATE_TYPE_DESC_COL);
        column. setPreferredWidth(220);
        column.setResizable(true);
        
        tableHeader = validRateTypesForm.tblCostElements.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        validRateTypesForm.tblCostElements.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        validRateTypesForm.tblCostElements.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        
        column = validRateTypesForm.tblCostElements.getColumnModel().getColumn(RATE_TYPE_CODE_COL);
        column.setPreferredWidth(65);
        column.setResizable(true);
        
        
        column = validRateTypesForm.tblCostElements.getColumnModel().getColumn(RATE_TYPE_DESC_COL);
        column. setPreferredWidth(220);
        column.setResizable(true);
    }
    
    
    private void setComboData(CoeusVector cvRateClass){
        //Setting the rate class type values to the rate class combo.
        if( ( cvRateClass != null ) && ( cvRateClass.size() > 0 )
        && ( validRateTypesForm.cmbRateClass.getItemCount() == 0 ) ) {
            int rateClassSize = cvRateClass.size();
            for(int index = 0 ; index < rateClassSize ; index++){
                validRateTypesForm.cmbRateClass.addItem(
                (RateClassBean)cvRateClass.elementAt(index));
            }
            rateClassBean = (RateClassBean)cvRateClass.elementAt(0);
            validRateTypesForm.cmbRateClass.setSelectedItem(rateClassBean);
        }
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        Point clickedPoint = mouseEvent.getPoint();
        int xPosition = (int)clickedPoint.getX();
        boolean sortAppCE=false;
        boolean sortCE=false;
        int columnIndex=0;
        if(mouseEvent.getSource().equals(validRateTypesForm.tblApplicationCostElements.getTableHeader())) {
            columnIndex = validRateTypesForm.tblApplicationCostElements.getColumnModel().getColumnIndexAtX(xPosition);
            sortAppCE=true;
        }else if(mouseEvent.getSource().equals(validRateTypesForm.tblCostElements.getTableHeader())) {
            columnIndex = validRateTypesForm.tblCostElements.getColumnModel().getColumnIndexAtX(xPosition);
            sortCE=true;
        }
        
        switch (columnIndex) {
            case RATE_TYPE_CODE_COL:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    if(sortAppCE) {
                        if(cvFilterApplicableRate!= null && cvFilterApplicableRate.size() > 0){
                            cvFilterApplicableRate.sort("costElement", false);
                        }
                    }else if(sortCE) {
                        if(cvFilterAvailableRate!= null && cvFilterAvailableRate.size() > 0){
                            cvFilterAvailableRate.sort("costElement", false);
                        }
                    }
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    if(sortAppCE) {
                        if(cvFilterApplicableRate!= null && cvFilterApplicableRate.size() > 0){
                            cvFilterApplicableRate.sort("costElement", true);
                        }
                    }else if(sortCE) {
                        if(cvFilterAvailableRate!= null && cvFilterAvailableRate.size() > 0){
                            cvFilterAvailableRate.sort("costElement", true);
                        }
                    }
                    sortCodeAsc = true;
                }
                break;
            case RATE_TYPE_DESC_COL:
                if(sortDescAsc){
                    if(sortAppCE) {
                        if(cvFilterApplicableRate!= null && cvFilterApplicableRate.size() > 0){
                            cvFilterApplicableRate.sort("rateTypeDescription", false,true);
                        }
                    }else if(sortCE) {
                        if(cvFilterAvailableRate!= null && cvFilterAvailableRate.size() > 0){
                            cvFilterAvailableRate.sort("rateTypeDescription", false,true);
                        }
                    }
                    sortDescAsc = false;
                }else {
                    if(sortAppCE) {
                        if(cvFilterApplicableRate!= null && cvFilterApplicableRate.size() > 0){
                            cvFilterApplicableRate.sort("rateTypeDescription", true,true);
                        }
                    }else if(sortCE) {
                        if(cvFilterAvailableRate!= null && cvFilterAvailableRate.size() > 0){
                            cvFilterAvailableRate.sort("rateTypeDescription", true,true);
                        }
                    }
                    sortDescAsc = true;
                }
                break;
        }//End Switch
        if(sortAppCE) {
            applicableCostElemTableModel.fireTableDataChanged();
        }else if(sortCE) {
            availableCostElementsTableModel.fireTableDataChanged();
        }
        
    }//End Mouse Click
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        CoeusVector updateDataVector = new CoeusVector();
    if(cvOriginalAppValidRate!= null && cvOriginalAppValidRate.size() > 0){
            for(int count=0;count<cvOriginalAppValidRate.size();count++) {
                CoeusVector cvAppVector=(CoeusVector)cvOriginalAppValidRate.get(count);
                String rateClassCode = cvAppVector.get(0).toString();
                String rateTypeCode =  cvAppVector.get(1).toString();
                CoeusVector cVector=(CoeusVector)cvAppVector.get(2);
                if(cVector== null){
                    cVector = new CoeusVector();
                }
                int originalSize=cVector.size();
                if(isModified(rateClassCode,rateTypeCode)) {
                    for(int k=0;k<modifiedRateClassTypes.size();k++) {
                        CoeusVector rCTCodes= (CoeusVector)modifiedRateClassTypes.get(k);
                        String rClCode=(String)rCTCodes.get(0);
                        if(rClCode.equals(rateClassCode)) {
                            String rTyCode =(String)rCTCodes.get(1);
                            if(rTyCode.equals(rateTypeCode)) {
                                cvFilterApplicableRate=(CoeusVector)rCTCodes.get(2);
//                                if(cvFilterApplicableRate == null) {
//                                    cvFilterApplicableRate = new CoeusVector();
//                                }
                                //  break;
                            }
                        }
                    }
                }else {
                    continue;
                }
                int actualSize = cvFilterApplicableRate.size();
                boolean flag;
                if((originalSize < actualSize) || (originalSize ==  actualSize)) {
                    for(int index=0;index<actualSize;index++) {
                        flag=false;
                        CERateTypeBean cERateTypeBean=(CERateTypeBean)cvFilterApplicableRate.get(index);
                        for(int ind=0;ind<originalSize;ind++) {
                            CERateTypeBean cEOrignalRateTypeBean =(CERateTypeBean)cVector.get(ind);
                            if(cEOrignalRateTypeBean.getCostElement().equals(cERateTypeBean.getCostElement())){
                                flag=true;
                                break;
                            }
                        }
                        if(!flag) {
                            cERateTypeBean.setAcType(TypeConstants.INSERT_RECORD);
                           // updateDataVector.add(cERateTypeBean);
                            //Added for bug fixed for Case #2303 start
                            cERateTypeBean.setRateClassCode(Integer.parseInt(rateClassCode));
                            cERateTypeBean.setRateTypeCode(Integer.parseInt(rateTypeCode));
                            try{
                                CERateTypeBean ceCopy = (CERateTypeBean)ObjectCloner.deepCopy(cERateTypeBean);
                                updateDataVector.add(ceCopy);
                            }catch(Exception ce) {
                                ce.printStackTrace();
                            }
                            //Added for bug fixed for Case #2303 end
                        }
                    }
                }
                if((originalSize > actualSize) || (originalSize ==  actualSize)) {
                    for(int index=0;index<originalSize;index++) {
                        flag=false;
                        CERateTypeBean cEOrignalRateTypeBean=(CERateTypeBean)cVector.get(index);
                        for(int ind=0;ind<actualSize;ind++) {
                            CERateTypeBean cERateTypeBean =(CERateTypeBean)cvFilterApplicableRate.get(ind);
                            if(cERateTypeBean.getCostElement().equals(cEOrignalRateTypeBean.getCostElement())){
                                flag=true;                                
                                break;
                            }
                        }
                        if(!flag) {                            
                            cEOrignalRateTypeBean.setAcType(TypeConstants.DELETE_RECORD);
                            updateDataVector.add(cEOrignalRateTypeBean);                           
                        }
                    }
                }
            }
        }
        
        try {
            saveValidRateTypes(updateDataVector);
        }catch(CoeusUIException coeusUIException) {
            CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
        }
    }
    
    public void saveValidRateTypes(CoeusVector data) throws CoeusUIException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('Q');
        requesterBean.setDataObject(data);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        
        //            if(responderBean == null) {
        //                //Could not contact server.
        //                CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        //                throw new CoeusUIException();
        //            }
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source=actionEvent.getSource();
        if(source.equals(validRateTypesForm.btnAdd)) {
            performAddAtion();
        }else if(source.equals(validRateTypesForm.btnCancel)) {
            performCancelAction();
        }else if(source.equals(validRateTypesForm.btnOk)) {
            performOkAction();
        }else if(source.equals(validRateTypesForm.btnRemove)) {
            performRemoveAction();
        }
    }
    
    
    
    
    private void performCancelAction() {
        if(modified){
            confirmClosing();
        }else{
            dlgValidateRateType.setVisible(false);
        }
    }
    
    private void performOkAction() {
        dlgValidateRateType.setCursor(new Cursor(Cursor.WAIT_CURSOR)); 
        try{
            if( validate() ){
                saveFormData();
            }
            dlgValidateRateType.dispose();
        }catch (Exception exception){
            exception.printStackTrace();
        }finally {
           dlgValidateRateType.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
        }
    }
    
    
    private void performRemoveAction() {
        int rowCount = validRateTypesForm.tblApplicationCostElements.getRowCount();
        //int selectedOption = -1;
        int selRow = validRateTypesForm.tblApplicationCostElements.getSelectedRow();
        //if(selRow== -1) return;
        int rowIndex = 0;
        if(rowCount == 0) return ;
        CoeusVector cvTemp;
        boolean replace=false;
        CoeusVector rClassTypeCodes;
        int i=0;
        String rateClassCode = ((ComboBoxBean)validRateTypesForm.cmbRateClass.getSelectedItem()).getCode();
        String rateTypeCode = ((ComboBoxBean)validRateTypesForm.cmbRateType.getSelectedItem()).getCode();
        int[] selectedRows = validRateTypesForm.tblApplicationCostElements.getSelectedRows();
        if(selectedRows.length==0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("validRateTypeExceptionCode.1702"));
            //return;
        }else {
            for(rowIndex = selectedRows.length-1; rowIndex >=0 ; rowIndex--) {
                CERateTypeBean cERateTypeBean = (CERateTypeBean)cvFilterApplicableRate.get(selectedRows[rowIndex]);
                try {
                    cERateTypeBean.setRateClassCode(Integer.parseInt(rateClassCode));
                    cERateTypeBean.setRateTypeCode(Integer.parseInt(rateClassCode));
                }catch(NumberFormatException numberFormatException) {
                    CoeusOptionPane.showErrorDialog(numberFormatException.getMessage());
                }
                cERateTypeBean.setCostElement((String)validRateTypesForm.tblApplicationCostElements.getValueAt(selectedRows[rowIndex], 0));
                cERateTypeBean.setRateTypeDescription((String)validRateTypesForm.tblApplicationCostElements.getValueAt(selectedRows[rowIndex], 1));
                if(rateClassCode.equals(vacationRateClassCode) || rateClassCode.equals(ebRateClassCode)) {
                    cERateTypeBean.setCategoryType('P');
                }
                if(cvFilterApplicableRate!=null && cvFilterApplicableRate.size() > 0){
                    cvFilterApplicableRate.remove(selectedRows[rowIndex]);
                    setModifiedOnRatelass(rateClassCode,rateTypeCode,cERateTypeBean, false);
                    cvFilterAvailableRate.add(0+i,cERateTypeBean);
                    modified=true;i++;
                    
                }
            }
            cvTemp= new CoeusVector();
            cvTemp=cvFilterApplicableRate;
            if(modifiedRateClassTypes.size() != 0) {
                for(int k=0;k<modifiedRateClassTypes.size();k++) {
                    rClassTypeCodes= (CoeusVector)modifiedRateClassTypes.get(k);
                    String rClassCode=(String)rClassTypeCodes.get(0);
                    if(rateClassCode.equals(rClassCode)) {
                        String rTypeCode =(String)rClassTypeCodes.get(1);
                        if(rateTypeCode.equals(rTypeCode)) {
                            rClassTypeCodes.set(2, cvTemp);
                            modifiedRateClassTypes.set(k, rClassTypeCodes);
                            replace=true;
                        }
                    }
                }
            }
            if(!replace) {
                rClassTypeCodes=new CoeusVector();
                rClassTypeCodes.add(0,rateClassCode);
                rClassTypeCodes.add(1,rateTypeCode);
                rClassTypeCodes.add(2,cvTemp);
                modifiedRateClassTypes.add(rClassTypeCodes);
            }
            
            applicableCostElemTableModel.setData(cvFilterApplicableRate);
            availableCostElementsTableModel.setData(cvFilterAvailableRate);
            
        }
    }
    
    private void performAddAtion() {
        int rowCount = validRateTypesForm.tblCostElements.getRowCount();
        //int selectedOption = -1;
        int selRow = validRateTypesForm.tblCostElements.getSelectedRow();
        //if(selRow== -1) return;
        int i=0;
        int rowIndex = 0;
        CoeusVector cvTemp;
        boolean replace=false;
        CoeusVector rClassTypeCodes;
        if(rowCount == 0) return ;
        String rateClassCode = ((ComboBoxBean)validRateTypesForm.cmbRateClass.getSelectedItem()).getCode();
        String rateTypeCode = ((ComboBoxBean)validRateTypesForm.cmbRateType.getSelectedItem()).getCode();
        int[] selectedRows = validRateTypesForm.tblCostElements.getSelectedRows();
        if(selectedRows.length==0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("validRateTypeExceptionCode.1701"));
            //return;
        }else {
            for(rowIndex = selectedRows.length-1; rowIndex >=0 ; rowIndex--) {
                CERateTypeBean cERateTypeBean = (CERateTypeBean)cvFilterAvailableRate.get(selectedRows[rowIndex]);
                try {
                    cERateTypeBean.setRateClassCode(Integer.parseInt(rateClassCode));
                    cERateTypeBean.setRateTypeCode(Integer.parseInt(rateTypeCode));
                }catch(NumberFormatException numberFormatException) {
                    CoeusOptionPane.showErrorDialog(numberFormatException.getMessage());
                }
                cERateTypeBean.setCostElement((String)validRateTypesForm.tblCostElements.getValueAt(selectedRows[rowIndex], 0));
                cERateTypeBean.setRateTypeDescription((String)validRateTypesForm.tblCostElements.getValueAt(selectedRows[rowIndex], 1));
                
                if(cvFilterAvailableRate!=null && cvFilterAvailableRate.size() > 0){
                    cvFilterAvailableRate.remove(selectedRows[rowIndex]);
                    //availableCostElementsTableModel.fireTableRowsDeleted(rowIndex, rowIndex);                    
                    cvFilterApplicableRate.add(cERateTypeBean);
                    setModifiedOnRatelass(rateClassCode,rateTypeCode,cERateTypeBean,true);
                    modified=true;i++;
                }
            }
            cvTemp= new CoeusVector();
            cvTemp=cvFilterApplicableRate;
            if(modifiedRateClassTypes.size() != 0) {
                for(int k=0;k<modifiedRateClassTypes.size();k++) {
                    rClassTypeCodes= (CoeusVector)modifiedRateClassTypes.get(k);
                    String rClassCode=(String)rClassTypeCodes.get(0);
                    if(rateClassCode.equals(rClassCode)) {
                        String rTypeCode =(String)rClassTypeCodes.get(1);
                        if(rateTypeCode.equals(rTypeCode)) {
                            rClassTypeCodes.set(2, cvTemp);
                            modifiedRateClassTypes.set(k, rClassTypeCodes);
                            replace=true;
                        }
                    }
                }
            }
            if(!replace) {
                rClassTypeCodes=new CoeusVector();
                rClassTypeCodes.add(0,rateClassCode);
                rClassTypeCodes.add(1,rateTypeCode);
                rClassTypeCodes.add(2,cvTemp);
                modifiedRateClassTypes.add(rClassTypeCodes);
            }
            applicableCostElemTableModel.setData(cvFilterApplicableRate);
            availableCostElementsTableModel.setData(cvFilterAvailableRate);
        }
    }
    
    private boolean isModified(String rClassCode,String rTypeCode) {
        boolean flag=false;
        if(modifiedRateClassTypes.size() != 0) {
            for(int k=0;k<modifiedRateClassTypes.size();k++) {
                CoeusVector rCTCodes= (CoeusVector)modifiedRateClassTypes.get(k);
                String rClCode=(String)rCTCodes.get(0);
                if(rClCode.equals(rClassCode)) {
                    String rTyCode =(String)rCTCodes.get(1);
                    if(rTyCode.equals(rTypeCode)) {
                        flag=true;
                    }
                }
            }
        }
        return flag;
    }
    
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                if(validate()){
                    saveFormData();
                    dlgValidateRateType.setVisible(false);
                }
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setSaveRequired(false);
                dlgValidateRateType.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            
        }
    }
    
 //Case 2303 start   
 private void confirmClosing(String confirm){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            if(option == CoeusOptionPane.SELECTION_YES){
                if(validate()){
                    saveFormData();
                    setFormData(null);
                    cvOriginalAppValidRate.clear();
                    modified = false ;                                        
                }
            }else if(option == CoeusOptionPane.SELECTION_NO){               
                setFormData(null);
                modified = false ; 
                cvOriginalAppValidRate.clear();
                setSaveRequired(false); 
                applicableCostElemTableModel.fireTableDataChanged();
                availableCostElementsTableModel.fireTableDataChanged();
            }
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
  //Case 2303 end  
    
    private CoeusVector getOriginalAppCostElems(String rCCode,String rTCode) {
        CoeusVector cvOrigVec= new CoeusVector();
        for(int k=0;k<cvOriginalAppValidRate.size();k++) {
            CoeusVector OriginalAppCE= (CoeusVector)cvOriginalAppValidRate.get(k);
            String rClCode=(String)OriginalAppCE.get(0);
            if(rClCode.equals(rCCode)) {
                String rTyCode =(String)OriginalAppCE.get(1);
                if(rTyCode.equals(rTCode)) {
                    cvOrigVec=(CoeusVector)OriginalAppCE.get(2);
                }
            }
        }
        return cvOrigVec;
    }
    
    private CoeusVector getModifiedAppCostElements(String rCCode,String rTCode) {
        CoeusVector cvModifiedVec=new CoeusVector();
        for(int k=0;k<modifiedRateClassTypes.size();k++) {
            CoeusVector rCTCodes= (CoeusVector)modifiedRateClassTypes.get(k);
            String rClCode=(String)rCTCodes.get(0);
            if(rClCode.equals(rCCode)) {
                String rTyCode =(String)rCTCodes.get(1);
                if(rTyCode.equals(rTCode)) {
                    cvModifiedVec=(CoeusVector)rCTCodes.get(2);
                }
            }
        }
        return cvModifiedVec;
    }
    
    //Modified for bug id :1902 & 1930 step 2 - start 
    private void setOriginalAppCostElems(String rCCode,String rTCode,CoeusVector cvOriginal) {
        boolean isPresent= false;
        if(cvOriginalAppValidRate != null && cvOriginalAppValidRate.size()>0) {
            for(int i=0;i<cvOriginalAppValidRate.size(); i++) {
                CoeusVector cVector=(CoeusVector)cvOriginalAppValidRate.get(i);
                String code = (String)cVector.get(0);
                if(code.equals(rCCode)) {
                    String type=(String)cVector.get(1);
                    if(type.equals(rTCode)) {
                        isPresent= true;
                        break;
                    }
                }
            }
        }
        if(!isPresent) {
            CoeusVector OriginalAppCE=new CoeusVector();
            OriginalAppCE.add(0,rCCode);
            OriginalAppCE.add(1,rTCode);
            OriginalAppCE.add(2, cvOriginal);
            cvOriginalAppValidRate.add(OriginalAppCE);
        }
    }
    //bug id :1902 & 1930 step 2 - end
    
    private void getFilteredAvailableVector(String rClassCode,String rTypeCode) {
        CoeusVector cOrVec =getOriginalAppCostElems(rClassCode,rTypeCode);
        CoeusVector cvMod=getModifiedAppCostElements(rClassCode,rTypeCode);
        if(cOrVec.size()<cvMod.size() || cOrVec.size()==cvMod.size()) {
            for(int i=0;i<cvMod.size();i++) {
                boolean flag=false;
                CERateTypeBean cERateTypeBean =(CERateTypeBean)cvMod.get(i);
                for(int j=0;j<cOrVec.size();j++){
                    CERateTypeBean cEOrigRateTypeBean=(CERateTypeBean)cOrVec.get(j);
                    if(cEOrigRateTypeBean.getCostElement().equals(cERateTypeBean.getCostElement())){
                        flag=true;
                        break;
                    }
                }
                if(!flag) {
                    cvFilterAvailableRate.removeElement(cERateTypeBean);
                }
            }
        }else if(cOrVec.size()>cvMod.size() || cOrVec.size()==cvMod.size()) {
            for(int i=0;i<cOrVec.size();i++) {
                boolean flag=false;
                CERateTypeBean cEOrigRateTypeBean =(CERateTypeBean)cOrVec.get(i);
                for(int j=0;j<cvMod.size();j++){
                    CERateTypeBean cERateTypeBean=(CERateTypeBean)cvMod.get(j);
                    if(cERateTypeBean.getCostElement().equals(cEOrigRateTypeBean.getCostElement())){
                        flag=true;
                        break;
                    }
                }
                if(!flag) {
                    cvFilterAvailableRate.add(cEOrigRateTypeBean);
                }
            }
        }
    }
    
    private void setModifiedOnRatelass(String rateClCode,String rateTyCode,CERateTypeBean cERateTypeBean,boolean isAdd) {
        for(int k=0;k<modifiedAppRateOnRateClass.size();k++) {
            CoeusVector rCTCodes= (CoeusVector)modifiedAppRateOnRateClass.get(k);
            String rClCode=(String)rCTCodes.get(0);
            if(rClCode.equals(rateClCode)) {
                String rTCode =(String)rCTCodes.get(1);
                if(rTCode.equals(rateTyCode)) {
                    CoeusVector vector=(CoeusVector)rCTCodes.get(2);
                    if(isAdd)
                        vector.add(cERateTypeBean);
                    else if(!isAdd) {
                        vector.removeElement(cERateTypeBean);
                        rCTCodes.set(2, vector);
                        modifiedAppRateOnRateClass.set(k,rCTCodes);
                    }
                }
            }
        }
    }
    
    private CoeusVector getModifiedOnRatelass(String rClassCode,String rTypeCode) {
        CoeusVector cvModifiedVec=new CoeusVector();
        for(int k=0;k<modifiedAppRateOnRateClass.size();k++) {
            CoeusVector rCTCodes= (CoeusVector)modifiedAppRateOnRateClass.get(k);
            String rClCode=(String)rCTCodes.get(0);
            if(rClCode.equals(rClassCode)) {
                String rTCode =(String)rCTCodes.get(1);
                if(rTCode.equals(rTypeCode)) {
                    cvModifiedVec=(CoeusVector)rCTCodes.get(2);
                }
            }
        }
        return cvModifiedVec;
    }
    
    
    //Modified for bug id :1902 & 1930 step 3 - start
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        String rateClassCode = ((ComboBoxBean)validRateTypesForm.cmbRateClass.getSelectedItem()).getCode();
        String rateTypeCode=EMPTY_STRING;
        Equals eqRateClassCode;
        Equals eqRateTypeCode;
        CoeusVector avaVector= new CoeusVector();
        if(itemEvent.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if(source.equals(validRateTypesForm.cmbRateClass)) {
                //case 2303 start
                if(modified){
                    confirmClosing(null);
                }               
                cvFilterAvailableRate=new CoeusVector();
                //case 2303 end
                cvFilterApplicableRate = new CoeusVector();
                eqRateClassCode = new Equals("rateClassCode", new Integer(rateClassCode));
                if(cvRateType!= null && cvRateType.size() > 0){
                    cvFilterRateType = cvRateType.filter(eqRateClassCode);
                }
                if( ( cvFilterRateType!= null ) && ( cvFilterRateType.size() > 0 )){
                    validRateTypesForm.cmbRateType.setModel(new DefaultComboBoxModel(cvFilterRateType));
                    rateTypeBean = (RateTypeBean)cvFilterRateType.elementAt(0);
                    validRateTypesForm.cmbRateType.setSelectedItem(rateTypeBean);
                }else {
                    CoeusOptionPane.showInfoDialog("There are no Rate Types defined for this Rate Class.");
                    if(prevRateClassBean!=null) {
                        validRateTypesForm.cmbRateClass.setSelectedItem(prevRateClassBean);
                    }else {
                        validRateTypesForm.cmbRateClass.setSelectedItem(rateClassBean);
                    }
                }
                prevRateClassBean =(RateClassBean)validRateTypesForm.cmbRateClass.getSelectedItem();
                rateClassCode = ((ComboBoxBean)validRateTypesForm.cmbRateClass.getSelectedItem()).getCode();
                rateTypeCode = ((ComboBoxBean)validRateTypesForm.cmbRateType.getSelectedItem()).getCode();
                
                eqRateClassCode = new Equals("rateClassCode", new Integer(rateClassCode));
                eqRateTypeCode = new Equals("rateTypeCode", new Integer(rateTypeCode));
                And rateClassAndRateType = new And(eqRateClassCode,eqRateTypeCode);
                if(cvApplicable!=null && !isModified(rateClassCode,rateTypeCode)) {// || rateClassCode.equals("7")){
                    cvFilterApplicableRate = cvApplicable.filter(eqRateClassCode);
                    if(!isModified(rateClassCode,rateTypeCode)) {
                        CoeusVector cvNew=new CoeusVector();
                        cvNew.add(0, rateClassCode);
                        cvNew.add(1,rateTypeCode);
                        cvNew.add(2,cvFilterApplicableRate);
                        modifiedAppRateOnRateClass.add(cvNew);
                    }
                    
                }else if(cvApplicable!= null && isModified(rateClassCode,rateTypeCode) && !rateClassCode.equals("7")) {
                    cvFilterApplicableRate=getModifiedOnRatelass(rateClassCode, rateTypeCode);
                    // cvFilterApplicableRate=getModifiedAppCostElements(rateClassCode, rateTypeCode);
                }
                if(rateClassCode.equals(vacationRateClassCode) || rateClassCode.equals(ebRateClassCode)) {
                    char categoryType = 'P';
                    Equals eqCategoryType=new Equals("categoryType", new Character(categoryType));
                    avaVector = cvAvailable.filter(eqCategoryType);
                } else {
                    avaVector = cvAvailable;
                }
                if(avaVector!= null && avaVector.size() > 0){
                    for (int availableIndex=0;availableIndex<avaVector.size();availableIndex++) {
                        boolean reject=false;
                        CERateTypeBean availableBean = (CERateTypeBean)avaVector.get(availableIndex);
                        if(cvFilterApplicableRate!= null && cvFilterApplicableRate.size() > 0){
                            for(int applicableIndex=0;applicableIndex<cvFilterApplicableRate.size();applicableIndex++) {
                                CERateTypeBean applicableBean = (CERateTypeBean)cvFilterApplicableRate.get(applicableIndex);
                                if(availableBean.getCostElement().equals(applicableBean.getCostElement())) {
                                    reject=true;
                                    break;
                                }
                            }
                        }
                        if(!reject) {
                            cvFilterAvailableRate.add(availableBean);
                        }
                        
                    }
                }
                //                 if(rateClassCode.equals("7") && isModified(rateClassCode,rateTypeCode)) {
                //                     getFilteredAvailableVector(rateClassCode,rateTypeCode);
                //                 }
                
                if(cvApplicable!=null && cvApplicable.size() > 0 && !isModified(rateClassCode,rateTypeCode)){
                    cvFilterApplicableRate = cvApplicable.filter(rateClassAndRateType);
                    // cvFilterApplicableRate = cvFilterApplicableRate.filter(CoeusVector.FILTER_ACTIVE_BEANS);
                }else if(cvApplicable!= null && cvApplicable.size() > 0 && isModified(rateClassCode,rateTypeCode)) {
                    cvFilterApplicableRate=getModifiedAppCostElements(rateClassCode, rateTypeCode);
                }else if(cvApplicable == null && !isModified(rateClassCode,rateTypeCode)) {
                    cvFilterApplicableRate = new CoeusVector();
                }else if(cvApplicable == null && isModified(rateClassCode,rateTypeCode)) {
                    cvFilterApplicableRate = getModifiedAppCostElements(rateClassCode, rateTypeCode);
                }
            }else if(source.equals(validRateTypesForm.cmbRateType)){
                //case 2303 start
                if(modified){
                    confirmClosing(null);
                }               
                cvFilterAvailableRate=new CoeusVector();
                cvFilterApplicableRate = new CoeusVector();
                //case 2303 end
                rateClassCode = ((ComboBoxBean)validRateTypesForm.cmbRateClass.getSelectedItem()).getCode();
                rateTypeCode = ((ComboBoxBean)validRateTypesForm.cmbRateType.getSelectedItem()).getCode();
                eqRateClassCode = new Equals("rateClassCode", new Integer(rateClassCode));
                eqRateTypeCode = new Equals("rateTypeCode",new Integer(rateTypeCode));
                And rateClassAndRateType = new And(eqRateClassCode,eqRateTypeCode);
                if(cvApplicable!=null && cvApplicable.size() > 0 && !isModified(rateClassCode,rateTypeCode) || rateClassCode.equals("7")){
                    cvFilterApplicableRate = cvApplicable.filter(eqRateClassCode);
                    if(!isModified(rateClassCode,rateTypeCode)) {
                        CoeusVector cvNew=new CoeusVector();
                        cvNew.add(0, rateClassCode);
                        cvNew.add(1,rateTypeCode);
                        cvNew.add(2,cvFilterApplicableRate);
                        modifiedAppRateOnRateClass.add(cvNew);
                    }
                    
                }else if(cvApplicable!= null && cvApplicable.size() > 0 && isModified(rateClassCode,rateTypeCode)) {// && !rateClassCode.equals("7")) {
                    //cvFilterApplicableRate=getModifiedAppCostElements(rateClassCode, rateTypeCode);
                    cvFilterApplicableRate=getModifiedOnRatelass(rateClassCode, rateTypeCode);
                }
                
                if(rateClassCode.equals(vacationRateClassCode) || rateClassCode.equals(ebRateClassCode)) {
                    char categoryType = 'P';
                    Equals eqCategoryType=new Equals("categoryType", new Character(categoryType));
                    if(cvAvailable!= null && cvAvailable.size() > 0){
                        avaVector = cvAvailable.filter(eqCategoryType);
                    }
                }else {
                    avaVector=cvAvailable;
                }
                if(avaVector!= null && avaVector.size() > 0){
                    for (int availableIndex=0;availableIndex<avaVector.size();availableIndex++) {
                        boolean reject=false;
                        CERateTypeBean availableBean = (CERateTypeBean)avaVector.get(availableIndex);
                        for(int applicableIndex=0;applicableIndex<cvFilterApplicableRate.size();applicableIndex++) {
                            CERateTypeBean applicableBean = (CERateTypeBean)cvFilterApplicableRate.get(applicableIndex);
                            if(availableBean.getCostElement().equals(applicableBean.getCostElement())) {
                                reject=true;
                                break;
                            }
                        }
                        if(!reject) {
                            cvFilterAvailableRate.add(availableBean);
                        }
                        
                    }
                }
                ////                if(rateClassCode.equals("7") && isModified(rateClassCode,rateTypeCode)) {
                ////                     getFilteredAvailableVector(rateClassCode,rateTypeCode);
                ////                }
                
                if(cvApplicable!=null && cvApplicable.size() > 0 && !isModified(rateClassCode,rateTypeCode)){
                    cvFilterApplicableRate = cvApplicable.filter(rateClassAndRateType);
                    // cvFilterApplicableRate = cvFilterApplicableRate.filter(CoeusVector.FILTER_ACTIVE_BEANS);
                }else if(cvApplicable!= null && isModified(rateClassCode,rateTypeCode)) {
                    cvFilterApplicableRate=getModifiedAppCostElements(rateClassCode, rateTypeCode);
                }else if(cvApplicable == null && !isModified(rateClassCode,rateTypeCode)) {
                    cvFilterApplicableRate = new CoeusVector();
                }else if(cvApplicable == null && isModified(rateClassCode,rateTypeCode)) {
                    cvFilterApplicableRate = getModifiedAppCostElements(rateClassCode, rateTypeCode);
                }
            }
            try {
                if(!isModified(rateClassCode,rateTypeCode)) {
                    if(cvFilterApplicableRate!= null && cvFilterApplicableRate.size() > 0){
                        CoeusVector OrigAppCE=(CoeusVector)ObjectCloner.deepCopy(cvFilterApplicableRate);
                        setOriginalAppCostElems(rateClassCode,rateTypeCode,OrigAppCE);
                    }else if(cvFilterApplicableRate.size() == 0) {
                        CoeusVector OrigAppCE = new CoeusVector();
                        setOriginalAppCostElems(rateClassCode,rateTypeCode,OrigAppCE);
                    }
                    // cvOriginalAppValidRate = (CoeusVector)ObjectCloner.deepCopy(cvFilterApplicableRate);
                }
            }catch(Exception exception) {
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
            if(cvFilterApplicableRate!= null && cvFilterApplicableRate.size() > 0){
                cvFilterApplicableRate.sort("rateClassCode",false);
            }
            applicableCostElemTableModel.setData(cvFilterApplicableRate);
            applicableCostElemTableModel.fireTableDataChanged();
            if(cvFilterAvailableRate!= null && cvFilterAvailableRate.size() > 0){
                cvFilterAvailableRate.sort("rateClassCode",false);
            }
            availableCostElementsTableModel.setData(cvFilterAvailableRate);
            availableCostElementsTableModel.fireTableDataChanged();
        }
        
    }
    //bug id :1902 & 1930 step 3 - end
    
    class ApplicableCostElemTableModel extends AbstractTableModel {
        String colNames[] = {"Code","Description"};
        Class[] colTypes = new Class[] {String.class,String.class};
        
        ApplicableCostElemTableModel() {
            
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
        
        public void setData(CoeusVector cvFilterApplicableRate){
            cvFilterApplicableRate = cvFilterApplicableRate;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvFilterApplicableRate==null){
                return 0;
            }else{
                return cvFilterApplicableRate.size();
            }
            
            
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            CERateTypeBean cERateTypeBean = (CERateTypeBean)cvFilterApplicableRate.get(row);
            switch(column) {
                case RATE_TYPE_CODE_COL:
                    return cERateTypeBean.getCostElement();
                case RATE_TYPE_DESC_COL:
                    return cERateTypeBean.getRateTypeDescription();
            }
            return EMPTY_STRING;
        }
        
        public void addRow(CERateTypeBean cERateTypeBean) {
            cvFilterApplicableRate.add(cERateTypeBean);
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(cvFilterApplicableRate == null)
                return;
            CERateTypeBean cERateTypeBean = (CERateTypeBean)cvFilterApplicableRate.get(row);
            switch(column) {
                case RATE_TYPE_CODE_COL:
                    try {
                        cERateTypeBean.setCostElement(value.toString());
                    }catch(NumberFormatException numberFormatException) {
                        numberFormatException.printStackTrace();
                    }
                case RATE_TYPE_DESC_COL:
                    cERateTypeBean.setRateTypeDescription(value.toString());
            }
        }
        
    }
    
    class AvailableCostElementsTableModel extends AbstractTableModel implements TableModel{
        String colNames[] = {"Code","Description"};
        Class[] colTypes = new Class[] {String.class,String.class};
        
        AvailableCostElementsTableModel() {
            
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
        
        public int getRowCount() {
            if(cvFilterAvailableRate== null){
                return 0;
            }else{
                return cvFilterAvailableRate.size();
            }
        }
        
        public void remove(CERateTypeBean cERateTypeBean) {
            cvFilterAvailableRate.remove(cERateTypeBean);
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public void setData(CoeusVector cvFilterAvailableRate){
            cvFilterAvailableRate = cvFilterAvailableRate;
            fireTableDataChanged();
        }
        
        public Object getValueAt(int row,int column) {
            CERateTypeBean cERateTypeBean =(CERateTypeBean)cvFilterAvailableRate.get(row);
            switch(column) {
                case RATE_TYPE_CODE_COL:
                    return cERateTypeBean.getCostElement();
                case RATE_TYPE_DESC_COL:
                    return cERateTypeBean.getRateTypeDescription();
            }
            
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value,int row,int column) {
            CERateTypeBean cERateTypeBean =(CERateTypeBean)cvFilterAvailableRate.get(row);
            switch(column) {
                case RATE_TYPE_CODE_COL:
                    cERateTypeBean.setCostElement(value.toString());
                case RATE_TYPE_DESC_COL:
                    cERateTypeBean.setRateTypeDescription(value.toString());
            }
        }
        
    }
    
}
