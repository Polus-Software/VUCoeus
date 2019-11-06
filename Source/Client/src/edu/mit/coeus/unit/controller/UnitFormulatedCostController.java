/*
 * UnitFormualtedCostMaintenanceController.java
 *
 * Created on November 22, 2011, 9:22 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.unit.controller;

import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.codetable.bean.RequestTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.unit.bean.UnitFormulatedCostBean;
import edu.mit.coeus.unit.gui.UnitFormulatedCostMaintenanceForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.TypeSelectionLookUp;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author satheeshkumarkn
 */
public class UnitFormulatedCostController implements ActionListener{
    
    private boolean isSaveRequired;
    private char functionType;
    private static final char GET_UNIT_FORMULATED_COST = 'Z';
    private static final char UPDATE_UNIT_FORMULATED_COST = 'z';
    private static final int ROW_HEIGHT = 22;
    private int WIDTH = 700;
    private int HEIGHT = 400;
    private static final int ICON_COLUMN = 0;
    private static final int FORMULATED_CODE_COLUMN = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int UNIT_COST_COLUMN = 3;
    private static final int ICON_COLUMN_WIDTH = 30;
    private static final int FORMULATED_CODE_COLUMN_WIDTH = 125;
    private static final int FORMULATED_DESCRIPTION_COLUMN_WIDTH = 225;
    private static final int UNIT_COST_COLUMN_WIDTH = 100;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
    private String selUnitNumber;
    private static final String UNIT_FORMULATE_COST_DEL_CONFIRMATION = "unitFormulatedFrm_exceptionCode.1000";
    private static final String SAVE_CHANGES_CONFIRMATION = "saveConfirmCode.1002";
    private String UNIT_SERVLET = "/unitServlet";
    private String  CODE_TABLE_SERVLET = "/CodeTableServlet";
    private static final String INSERT_UPDATE_FORM_COST_BEAN = "GET_INSERT_UPDATE_BEAN";
    private static final String DELETE_FORM_COST_BEAN = "GET_DELETE_BEAN";
    private String windowTitle;
    private static final char GET_CODE_TABLE_TYPE = 'Z';
    private CoeusMessageResources coeusMessageResources;
    private CoeusVector cvFormulatedCost, cvFormulatedType, cvDeleletedUnitCost;
    private CoeusDlgWindow dlgFormulatedCost;
    private UnitFormulatedCostModel formulatedCostModel;
    private UnitFormulatedCostMaintenanceForm formulatedCostForm;
    private final String SELECT_FORMULATED_TYPE = "formulatedTypeSelection_exceptionCode.1000";
    private final String FORMULATED_TYPE_EXISTS = "formulatedTypeAlreadyExists_exceptionCode.1000";
    
    /** Creates a new instance of UnitFormualtedCostMaintenanceController */
    public UnitFormulatedCostController(String selUnitNumber, String selUnitName, char functionType) {
        this.functionType = functionType;
        this.selUnitNumber = selUnitNumber;
        windowTitle = "Formulated Cost for "+selUnitNumber+":"+selUnitName;
        coeusMessageResources = CoeusMessageResources.getInstance();
        formulatedCostForm = new UnitFormulatedCostMaintenanceForm();
        registerComponents();
    }

    /*
     * Method to set properties for all the columns in the table
     */
    private void setTableColumnProperties(){
        formulatedCostForm.tblFormualtedCost.setOpaque(false);
        formulatedCostForm.tblFormualtedCost.setShowVerticalLines(false);
        formulatedCostForm.tblFormualtedCost.setShowHorizontalLines(false);
        formulatedCostForm.tblFormualtedCost.setRowHeight(ROW_HEIGHT);
        formulatedCostForm.tblFormualtedCost.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        formulatedCostForm.tblFormualtedCost.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // ICON Column properties setting
        TableColumn column = formulatedCostForm.tblFormualtedCost.getColumnModel().getColumn(ICON_COLUMN);
        column.setMinWidth(ICON_COLUMN_WIDTH);
        column.setMaxWidth(ICON_COLUMN_WIDTH);
        column.setPreferredWidth(ICON_COLUMN_WIDTH);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setResizable(false);
        
        UnitFormulatedTableCellRenderer unitFormulatedTableCellRenderer = new UnitFormulatedTableCellRenderer();
        // Formualted code column properties
        column = formulatedCostForm.tblFormualtedCost.getColumnModel().getColumn(FORMULATED_CODE_COLUMN);
        column.setMinWidth(FORMULATED_CODE_COLUMN_WIDTH);
        column.setMaxWidth(FORMULATED_CODE_COLUMN_WIDTH);
        column.setPreferredWidth(FORMULATED_CODE_COLUMN_WIDTH);
        column.setCellRenderer(unitFormulatedTableCellRenderer);
        column.setResizable(false);
        
        // Formualted code column properties
        column = formulatedCostForm.tblFormualtedCost.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setMinWidth(FORMULATED_DESCRIPTION_COLUMN_WIDTH);
        column.setMaxWidth(FORMULATED_DESCRIPTION_COLUMN_WIDTH);
        column.setPreferredWidth(FORMULATED_DESCRIPTION_COLUMN_WIDTH);
        column.setCellRenderer(unitFormulatedTableCellRenderer);
        column.setResizable(true);
        
        // Unit Cost column properties
        column = formulatedCostForm.tblFormualtedCost.getColumnModel().getColumn(UNIT_COST_COLUMN);
        column.setMinWidth(UNIT_COST_COLUMN_WIDTH);
        column.setMaxWidth(UNIT_COST_COLUMN_WIDTH);
        column.setPreferredWidth(UNIT_COST_COLUMN_WIDTH);
        column.setCellRenderer(unitFormulatedTableCellRenderer);
        column.setCellEditor(new UnitFormulatedCellEditor());
        column.setResizable(false);
        
        formulatedCostForm.tblFormualtedCost.getTableHeader().setReorderingAllowed(false);
        formulatedCostForm.tblFormualtedCost.getTableHeader().setResizingAllowed(true);
        formulatedCostForm.tblFormualtedCost.getTableHeader().setFont(CoeusFontFactory.getNormalFont());
        formulatedCostForm.tblFormualtedCost.getTableHeader().setFont(CoeusFontFactory.getLabelFont()); 
        
    }

    /*
     * Method to set the unit formualted form data 
     */
    public void setFormData(){
        cvFormulatedCost =  getFormulatedCostFromServer();
        formulatedCostModel = new UnitFormulatedCostModel();
        formulatedCostForm.tblFormualtedCost.setModel(formulatedCostModel);
        setTableColumnProperties();
        if(cvFormulatedCost != null && !cvFormulatedCost.isEmpty()){
            formulatedCostModel.setData(cvFormulatedCost);
            formulatedCostForm.tblFormualtedCost.setRowSelectionInterval(0,0);
        }else{
            formulatedCostForm.btnDelete.setEnabled(false);
            formulatedCostForm.btnChangeFormulatedType.setEnabled(false);
        }
    }
    
    /*
     * Method to register all the Components in the form 
     */
    public void registerComponents() {
        formulatedCostForm.btnOk.addActionListener(this);
        formulatedCostForm.btnCancel.addActionListener(this);
        formulatedCostForm.btnAdd.addActionListener(this);
        formulatedCostForm.btnDelete.addActionListener(this);
        formulatedCostForm.btnChangeFormulatedType.addActionListener(this);
        
        java.awt.Component[] components={formulatedCostForm.btnOk,formulatedCostForm.btnCancel,
        formulatedCostForm.btnAdd,formulatedCostForm.btnDelete,formulatedCostForm.btnChangeFormulatedType};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        formulatedCostForm.setFocusTraversalPolicy(traversePolicy);
        formulatedCostForm.setFocusCycleRoot(true);
    }

    /**
     * All the button Event trigger are handled in this method
     * @param aE
     */
    public void actionPerformed(ActionEvent aE) {
        try {
            if(aE.getSource().equals(formulatedCostForm.btnAdd)){
                addFormulatedTypes();
            }else if(aE.getSource().equals(formulatedCostForm.btnCancel)){
                checkBeforeClose();
            }else if(aE.getSource().equals(formulatedCostForm.btnDelete)){
                deleteFormulatedCostRow();
            }else if(aE.getSource().equals(formulatedCostForm.btnChangeFormulatedType)){
                changeFormualatedType();
            }else if(aE.getSource().equals(formulatedCostForm.btnOk)){
                if(isSaveRequired()){
                    if(validateFormData()){
                        updateFormulatedCost();
                        dlgFormulatedCost.dispose();
                    }
                }else{
                    dlgFormulatedCost.dispose();
                }
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Method to check whether to close unit formulated dialog.
     * If form is modified will through save confirmation and close the dialog
     */
    private void checkBeforeClose(){
        if(formulatedCostForm.tblFormualtedCost.isEditing()){
            if(formulatedCostForm.tblFormualtedCost.getCellEditor() != null){
                formulatedCostForm.tblFormualtedCost.getCellEditor().stopCellEditing();
            }
        }
        if(isSaveRequired() ){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_CHANGES_CONFIRMATION),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_NO);
            switch(selectedOption){
                case CoeusOptionPane.SELECTION_YES:
                    if(validateFormData()){
                        updateFormulatedCost();
                        dlgFormulatedCost.dispose();
                    }
                    break;
                case CoeusOptionPane.SELECTION_NO:
                    dlgFormulatedCost.dispose();
                    break;
                case CoeusOptionPane.SELECTION_CANCEL:
                    dlgFormulatedCost.show(true);
                    break;
            }
        }else{
            dlgFormulatedCost.dispose();
        }
    }
    
    /*
     * Method to delete the seleted unit formualted cost in the table
     */
    private void deleteFormulatedCostRow(){
        int selectedRow = formulatedCostForm.tblFormualtedCost.getSelectedRow();
        if(selectedRow > -1){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(UNIT_FORMULATE_COST_DEL_CONFIRMATION),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            switch(selectedOption){
                case CoeusOptionPane.SELECTION_YES:
                    CoeusVector cvFormulatedCostFromModel = formulatedCostModel.getData();
                    if(cvFormulatedCostFromModel != null && !cvFormulatedCostFromModel.isEmpty()){
                        UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)cvFormulatedCostFromModel.get(selectedRow);
                        if(!TypeConstants.INSERT_RECORD.equals(unitFormulatedCostBean.getAcType())){
                            unitFormulatedCostBean.setAcType(TypeConstants.DELETE_RECORD);
                            if(cvDeleletedUnitCost == null){
                                cvDeleletedUnitCost = new CoeusVector();
                            }
                            cvDeleletedUnitCost.add(unitFormulatedCostBean);
                        }
                        cvFormulatedCostFromModel.remove(selectedRow);
                        formulatedCostModel.setData(cvFormulatedCostFromModel);
                        if(cvFormulatedCostFromModel != null && !cvFormulatedCostFromModel.isEmpty()){
                            int newRowCount = formulatedCostForm.tblFormualtedCost.getRowCount();
                            if(newRowCount >0){
                                if(newRowCount > selectedRow){
                                    formulatedCostForm.tblFormualtedCost.setRowSelectionInterval(selectedRow,selectedRow);
                                }else{
                                    formulatedCostForm.tblFormualtedCost.setRowSelectionInterval(newRowCount - 1,newRowCount - 1);
                                }
                                int rowCount = formulatedCostForm.tblFormualtedCost.getRowCount() -1 ;
                                formulatedCostForm.tblFormualtedCost.requestFocusInWindow();
                                formulatedCostForm.tblFormualtedCost.editCellAt(rowCount,UNIT_COST_COLUMN);
                                formulatedCostForm.tblFormualtedCost.getEditorComponent().requestFocusInWindow();
                            }
                        }
                        setSaveRequired(true);
                    }
                    if(cvFormulatedCostFromModel == null || cvFormulatedCostFromModel.isEmpty()){
                        formulatedCostForm.btnDelete.setEnabled(false);
                        formulatedCostForm.btnChangeFormulatedType.setEnabled(false);
                    }
            }
            
        }
    }
    
    /**
     * Method to display the Unit formulated cost form in a dialog
     * @throws edu.mit.coeus.exception.CoeusException
     */
    public void display()throws CoeusException{
        dlgFormulatedCost =new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), windowTitle, true);
        dlgFormulatedCost.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                formulatedCostForm.btnOk.requestFocusInWindow();
                formulatedCostForm.btnOk.setFocusable(true);
                formulatedCostForm.btnOk.requestFocus();
            }
            
            public void windowClosing(WindowEvent we){
                checkBeforeClose();
            }
        });
        dlgFormulatedCost.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                checkBeforeClose();
            }
        });
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dlgFormulatedCost.setSize(WIDTH, HEIGHT);
        Dimension dlgSize = dlgFormulatedCost.getSize();
        dlgFormulatedCost.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgFormulatedCost.getContentPane().add( formulatedCostForm );
        dlgFormulatedCost.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgFormulatedCost.setResizable(false);
        dlgFormulatedCost.setVisible(true);
    }

    /**
     * 
     * @return 
     */
    public boolean isSaveRequired() {
        if(formulatedCostForm.tblFormualtedCost.getCellEditor() !=null){
            formulatedCostForm.tblFormualtedCost.getCellEditor().stopCellEditing();
        }
        return isSaveRequired;
    }
    
    /**
     * 
     * @param isSaveRequired 
     */
    public void setSaveRequired(boolean isSaveRequired) {
        this.isSaveRequired = isSaveRequired;
    }
    
    /**
     * 
     * @param cvDeleletedUnitCost 
     */
    public void setDeleletedUnitCost(CoeusVector cvDeleletedUnitCost) {
        this.cvDeleletedUnitCost = cvDeleletedUnitCost;
    }
    
    /**
     * Method to ge the deleted formualted unt cost
     * @return 
     */
    public CoeusVector getDeleletedUnitCost() {
        return cvDeleletedUnitCost;
    }
    
    /*
     * Method to get the formualted cost details for the selected unit
     */
    private CoeusVector getFormulatedCostFromServer() {
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ UNIT_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(selUnitNumber);
        CoeusVector cvFormulCost = null;
        request.setFunctionType(GET_UNIT_FORMULATED_COST);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if(response.isSuccessfulResponse()){
                cvFormulCost = (CoeusVector)response.getDataObject();
            }
        }
        return cvFormulCost;
    }
    
    /*
     * Method to get the formualted types
     */
    private CoeusVector getFormulatedTypesFromServer() {
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ CODE_TABLE_SERVLET;
        RequesterBean request = new RequesterBean();
        RequestTxnBean requestTxnBean = new RequestTxnBean();
        requestTxnBean.setAction("GET_CODE_TABLE_TYPE_DETAILS");
        requestTxnBean.setProcedureName("GET_FORMULATED_TYPES");
        request.setDataObject(requestTxnBean);
        CoeusVector cvFormulatedType = null;
        request.setFunctionType(GET_CODE_TABLE_TYPE);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null && response.isSuccessfulResponse()){
                cvFormulatedType = (CoeusVector)response.getDataObject();
        }
        return cvFormulatedType;
    }
    
    /*
     * Method to update the unit formualted cost
     */
    private boolean updateFormulatedCost() {
        boolean isUpdated = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ UNIT_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(selUnitNumber);
        HashMap hmFormCost = new HashMap();
        hmFormCost.put(INSERT_UPDATE_FORM_COST_BEAN,formulatedCostModel.getData());
        hmFormCost.put(DELETE_FORM_COST_BEAN,cvDeleletedUnitCost);
        request.setDataObject(hmFormCost);
        request.setFunctionType(UPDATE_UNIT_FORMULATED_COST);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null && response.isSuccessfulResponse()){
                isUpdated = true;
                setSaveRequired(false);
        }
        return isUpdated;
    }
    
    /*
     * Method to add selected formulated types
     */
    private void addFormulatedTypes() throws CoeusException{
        formulatedCostForm.btnChangeFormulatedType.setEnabled(true);
        int selectedRow = formulatedCostForm.tblFormualtedCost.getSelectedRow();
        if(cvFormulatedType == null || cvFormulatedType.isEmpty()){
            cvFormulatedType = getFormulatedTypesFromServer();
        }
        TypeSelectionLookUp typeSelectionLookUp = new TypeSelectionLookUp("Formulated Types",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        typeSelectionLookUp.setFormData(cvFormulatedType);
        typeSelectionLookUp.display();
        CoeusVector cvSelectedTypes = typeSelectionLookUp.getSelectedTypes();
        if(cvSelectedTypes != null && !cvSelectedTypes.isEmpty()){
            String alreadyExistsType = "Following formulated types are already exists! \n";
            boolean formulatedTypesExists = false;
            for(Object typeBean : cvSelectedTypes){
                boolean insertFormulatedType = false;
                CoeusTypeBean coeusTypeBean = (CoeusTypeBean)typeBean;
                CoeusVector cvFormCostData = formulatedCostModel.getData();
                if(cvFormCostData != null && !cvFormCostData.isEmpty()){
                    Equals eqFormType = new Equals("formulatedCode",coeusTypeBean.getTypeCode());
                    CoeusVector cvFilteredData = cvFormCostData.filter(eqFormType);
                    if(cvFilteredData != null && !cvFilteredData.isEmpty()){
                        formulatedTypesExists = true;
                        alreadyExistsType = alreadyExistsType+"\n"+coeusTypeBean.getTypeDescription();
                    }else{
                        insertFormulatedType = true;
                    }
                }else{
                    insertFormulatedType = true;
                }
                if(insertFormulatedType){
                    UnitFormulatedCostBean unitFormulatedCostBean = new UnitFormulatedCostBean();
                    unitFormulatedCostBean.setAcType(TypeConstants.INSERT_RECORD);
                    unitFormulatedCostBean.setUnitNumber(selUnitNumber);
                    unitFormulatedCostBean.setFormulatedCode(coeusTypeBean.getTypeCode());
                    unitFormulatedCostBean.setFormulatedCodeDescription(coeusTypeBean.getTypeDescription());
                    CoeusVector cvFormulatedCostFromModel = formulatedCostModel.getData();
                    if(cvFormulatedCostFromModel == null){
                        cvFormulatedCostFromModel = new CoeusVector();
                    }
                    cvFormulatedCostFromModel.add(unitFormulatedCostBean);
                    formulatedCostModel.setData(cvFormulatedCostFromModel);
                    formulatedCostForm.btnDelete.setEnabled(true);
                    formulatedCostForm.tblFormualtedCost.setEnabled(true);
                }
            }
            setSaveRequired(true);
            if(formulatedTypesExists){
                CoeusOptionPane.showWarningDialog(alreadyExistsType);
            }
        }
        int lastAddedRow = formulatedCostForm.tblFormualtedCost.getRowCount()-1;
        if(lastAddedRow > -1){
            formulatedCostForm.tblFormualtedCost.setRowSelectionInterval(lastAddedRow,lastAddedRow);
            formulatedCostForm.tblFormualtedCost.editCellAt(lastAddedRow,UNIT_COST_COLUMN);
            formulatedCostForm.tblFormualtedCost.getEditorComponent().requestFocusInWindow();
        }
    }
    
    
    /**
     * Method to change the selected formulated type
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void changeFormualatedType() throws CoeusException{
        int selectedRow = formulatedCostForm.tblFormualtedCost.getSelectedRow();
        if(selectedRow > -1){
            if(cvFormulatedType == null || cvFormulatedType.isEmpty()){
                cvFormulatedType = getFormulatedTypesFromServer();
            }
            TypeSelectionLookUp typeSelectionLookUp = new TypeSelectionLookUp("Formulated Types", ListSelectionModel.SINGLE_SELECTION);
            typeSelectionLookUp.setFormData(cvFormulatedType);
            typeSelectionLookUp.display();
            CoeusTypeBean coeusTypeBean = typeSelectionLookUp.getSelectedType();
            if(coeusTypeBean != null){
                CoeusVector cvFormCostData = formulatedCostModel.getData();
                if(cvFormCostData != null && !cvFormCostData.isEmpty()){
                    UnitFormulatedCostBean formCostBean = (UnitFormulatedCostBean)cvFormCostData.get(selectedRow);
                    Equals eqFormType = new Equals("formulatedCode",coeusTypeBean.getTypeCode());
                    CoeusVector cvFilteredData = cvFormCostData.filter(eqFormType);
                    if(cvFilteredData != null && !cvFilteredData.isEmpty()){
                        MessageFormat formatter = new MessageFormat("");
                        String message = formatter.format(coeusMessageResources.parseMessageKey(FORMULATED_TYPE_EXISTS),
                                "'",coeusTypeBean.getTypeDescription(),"'");
                        CoeusOptionPane.showWarningDialog(message);
                    }else{
                        formCostBean.setAcType(TypeConstants.UPDATE_RECORD);
                        formCostBean.setFormulatedCode(coeusTypeBean.getTypeCode());
                        formCostBean.setFormulatedCodeDescription(coeusTypeBean.getTypeDescription());
                        setSaveRequired(true);
                        formulatedCostModel.fireTableDataChanged();
                        formulatedCostForm.tblFormualtedCost.setRowSelectionInterval(selectedRow,selectedRow);
                    }
                }
            }
        }
    }

    /*
     * Method to validate the form datas
     */
    private boolean validateFormData(){
        if(formulatedCostForm.tblFormualtedCost.getCellEditor() !=null){
            formulatedCostForm.tblFormualtedCost.getCellEditor().stopCellEditing();
        }
        boolean isValidate = true;
        CoeusVector cvFormulatedCostFromModel = formulatedCostModel.getData();
        if(cvFormulatedCostFromModel != null && !cvFormulatedCostFromModel.isEmpty()){
            for(int indexRow = 0; indexRow < cvFormulatedCostFromModel.size(); indexRow++){
                UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)cvFormulatedCostFromModel.get(indexRow);
                if(unitFormulatedCostBean.getFormulatedCode() == 0){
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(SELECT_FORMULATED_TYPE));
                    formulatedCostForm.tblFormualtedCost.setRowSelectionInterval(indexRow,indexRow);
                    isValidate = false;
                    break;
                }
            }
        }
        return isValidate;
    }

    /*
     * Unit formualted cost model class
     */
    class UnitFormulatedCostModel extends DefaultTableModel {
        private String colNames[] = {"","Formulated Code","Description","Unit Cost"};
        private Class colTypes[]  = {Object.class, Integer.class, String.class, Double.class, };
        private CoeusVector cvData;
        
        /**
         * Method to check whether the selected column can be edited
         * @param row 
         * @param column 
         * @return 
         */
        public boolean isCellEditable(int row, int column) {
            boolean canEdit = false;
            if(column == UNIT_COST_COLUMN && functionType != TypeConstants.DISPLAY_MODE){
                canEdit = true;
            }
            return canEdit;
        }
        
        /**
         * Method to get the value for row and column
         * @param row 
         * @param column 
         * @return Object
         */
        public Object getValueAt(int row, int column) {
            if(cvData != null && !cvData.isEmpty()){
                UnitFormulatedCostBean formulatedCostBean = (UnitFormulatedCostBean)cvData.get(row);
                switch(column) {
                    case FORMULATED_CODE_COLUMN:
                        return formulatedCostBean.getFormulatedCode();
                    case DESCRIPTION_COLUMN:
                        return formulatedCostBean.getFormulatedCodeDescription();
                    case UNIT_COST_COLUMN:
                        return formulatedCostBean.getUnitCost();
                    default:
                        break;
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        /**
         * Method to get the column name
         * @return String
         */        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        /**
         * Method to set the data for the model
         * @param cvData 
         */
        public void setData(CoeusVector cvData) {
            dataVector = cvData;
            this.cvData = cvData;
            cvData.sort("formulatedCode");
            fireTableDataChanged();
        }
        
        /**
         * Method to get the model data
         * @return 
         */
        public CoeusVector getData(){
            return cvData;
        }
        
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            return dataVector.size();
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        /**
         * Method to set the model value to the table
         * @param value 
         * @param row 
         * @param column 
         */
        public void setValueAt(Object value, int row, int column) {
            if(TypeConstants.DISPLAY_MODE == functionType ||
                    value == null || cvData == null ||
                    (cvData != null && cvData.isEmpty()) || row > cvData.size()-1 ){
                return;
            }
            UnitFormulatedCostBean formulatedCostBean = (UnitFormulatedCostBean)cvData.get(row);
            switch(column){
                case FORMULATED_CODE_COLUMN:
                    formulatedCostBean.setFormulatedCode(Integer.parseInt(value.toString()));
                    break;
                case DESCRIPTION_COLUMN :
                    formulatedCostBean.setFormulatedCodeDescription(value.toString());
                    break;
                case UNIT_COST_COLUMN :
                    double unitCost = Double.parseDouble(value.toString());
                    if(formulatedCostBean.getUnitCost() != unitCost){
                        if(!TypeConstants.INSERT_RECORD.equals(formulatedCostBean.getAcType())){
                            formulatedCostBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        setSaveRequired(true);
                    }
                    formulatedCostBean.setUnitCost(unitCost);
                    break;
                default:
                    break;
            }
            
        }
        
    }
    
    /*
     * Cell editor for formualted cost
     */
    public class UnitFormulatedCellEditor extends AbstractCellEditor implements TableCellEditor{
        private DollarCurrencyTextField txtUnitCost;
        private int column;
        
        public UnitFormulatedCellEditor(){
            txtUnitCost= new DollarCurrencyTextField();
            txtUnitCost.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        /**
         * method to get the editing cell componenet
         * @param table 
         * @param value 
         * @param isSelected 
         * @param row 
         * @param col 
         * @return Component
         */
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int col){
            this.column = col;
            switch(col) {
                case UNIT_COST_COLUMN:
                    txtUnitCost.setValue(new Double(value.toString()).doubleValue());
            }
            return txtUnitCost;
        }
        
        /**
         * Method to the cell value which is editing 
         * @return String
         */
        public Object getCellEditorValue() {
            switch (column) {
                case UNIT_COST_COLUMN:
                    return txtUnitCost.getValue();
            }
            return txtUnitCost.getValue();
        }
    }
    
    /*
     * Cell renderer for formualted cost
     */
    public class UnitFormulatedTableCellRenderer extends DefaultTableCellRenderer{
        
        private DollarCurrencyTextField txtUnitCost;
        private JLabel lblLabel;
        public UnitFormulatedTableCellRenderer(){
            txtUnitCost= new DollarCurrencyTextField();
            lblLabel = new JLabel();
            lblLabel.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblLabel.setOpaque(true);
        }
        
        /**
         * method to get the cell renderer Component
         * @param table 
         * @param value 
         * @param isSelected 
         * @param hasFocus 
         * @param row 
         * @param col 
         * @return Component
         */
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
            
            if(functionType == TypeConstants.DISPLAY_MODE ){
                lblLabel.setBackground(disabledBackground);
                lblLabel.setForeground(java.awt.Color.BLACK);
            }else if(isSelected){
                lblLabel.setBackground(Color.YELLOW);
                lblLabel.setForeground(Color.black);
            } else {
                lblLabel.setBackground(Color.white);
                lblLabel.setForeground(Color.black);
            }
            
            switch(col) {
                case FORMULATED_CODE_COLUMN:
                    if(value != null && "0".equals(value.toString())){
                        lblLabel.setText(CoeusGuiConstants.EMPTY_STRING);
                    }else if(value != null){
                        lblLabel.setText(value.toString());
                    }
                    lblLabel.setHorizontalAlignment(LEFT);
                    return lblLabel;
                case DESCRIPTION_COLUMN:
                    lblLabel.setText(value.toString());
                    lblLabel.setHorizontalAlignment(LEFT);
                    return lblLabel;
                case UNIT_COST_COLUMN :
                   txtUnitCost.setText(value.toString());
                    lblLabel.setHorizontalAlignment(RIGHT);
                    lblLabel.setText(txtUnitCost.getText());
                    return lblLabel;
            }
            return lblLabel;
        }
    }
}
