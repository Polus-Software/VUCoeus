/*
 * SubcontractReportsController.java
 *
 * Created on February 1, 2012, 3:28 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.organization.gui.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.bean.SubcontractReportBean;
import edu.mit.coeus.subcontract.gui.SubcontractReportsForm;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.TypeSelectionLookUp;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author satheeshkumarkn
 */
public class SubcontractReportsController extends SubcontractController implements ActionListener{
    private SubcontractReportsForm subcontractReportsForm;
    private char functionType;
    private CoeusMessageResources coeusMessageResources;
    private final int ICON_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 2;
    private final int LAST_UPDATE_TIMESTAMP_COLUMN = 3;
    private final int LAST_UPDATE_USER_COLUMN = 4;
    private ReportsModel reportsModel;
    private CoeusVector cvDeleletedReports;
    
    /* JM 2-27-2015 modifications to allow tab access if user has only this right */
    private boolean userHasModify = false;
    private boolean userHasCreate = false;
    /* JM END */
    
    /** Creates a new instance of SubcontractReportsController */
    public SubcontractReportsController(SubContractBean subContractBean, char functionType) {
        super(subContractBean);
        this.subContractBean = subContractBean;
        
		/* JM 2-27-2015 no access if not modifier */
        userHasModify = subContractBean.getHasModify();
        userHasCreate = subContractBean.getHasCreate();
		if (!userHasModify && !userHasCreate) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */   
        
        this.functionType = functionType;
        subcontractReportsForm = new SubcontractReportsForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        formatFields();
        registerComponents();
        setTableProperties();
        setFormData(null);
    }
    
    
      /* Setting the table header, column width, renderer and editor for the tables
       * returns void
       */
    private void setTableProperties() {
        reportsModel = new ReportsModel();
        subcontractReportsForm.tblReports.setModel(reportsModel);
        JTableHeader tableHeader = subcontractReportsForm.tblReports.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        subcontractReportsForm.tblReports.setOpaque(false);
        subcontractReportsForm.tblReports.setShowVerticalLines(false);
        subcontractReportsForm.tblReports.setShowHorizontalLines(false);
        subcontractReportsForm.tblReports.setRowHeight(22);
        subcontractReportsForm.tblReports.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        subcontractReportsForm.tblReports.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = subcontractReportsForm.tblReports.getColumnModel().getColumn(ICON_COLUMN);
        column.setPreferredWidth(25);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setResizable(false);
        
        column = subcontractReportsForm.tblReports.getColumnModel().getColumn(CODE_COLUMN);
        column.setPreferredWidth(50);
        column.setResizable(false);
        
        column = subcontractReportsForm.tblReports.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setPreferredWidth(500);
        column.setResizable(true);
        
        column = subcontractReportsForm.tblReports.getColumnModel().getColumn(LAST_UPDATE_TIMESTAMP_COLUMN);
        column.setPreferredWidth(150);
        column.setResizable(true);
        
        column = subcontractReportsForm.tblReports.getColumnModel().getColumn(LAST_UPDATE_USER_COLUMN);
        column.setPreferredWidth(150);
        column.setResizable(true);
    }
    
    public void cleanUp() {
    }
    
    /**
     * Method to get the form instance
     * @return subcontractReportsForm
     */
    public Component getControlledUI() {
        return subcontractReportsForm;
    }
    
    /**
     * Method to set the form data
     * @param data
     */
    public void setFormData(Object data) {
        try {
            CoeusVector cvSubConReports = queryEngine.getActiveData(queryKey,SubcontractReportBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            reportsModel.setData(cvSubConReports);
            if(cvSubConReports != null && !cvSubConReports.isEmpty()){
                subcontractReportsForm.tblReports.setRowSelectionInterval(0,0);
            }
            if(cvSubConReports == null || cvSubConReports.isEmpty()){
                subcontractReportsForm.btnDelete.setEnabled(false);
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /**
     * Method to get the form date
     * @return CoeusVector
     */
    public Object getFormData() {
        return reportsModel.getData();
    }
    
    public void formatFields() {
        if(functionType == TypeConstants.DISPLAY_MODE){
            subcontractReportsForm.btnAdd.setEnabled(false);
            subcontractReportsForm.btnDelete.setEnabled(false);
        }
    }
    
    /**
     * Method to validate
     * @throws edu.mit.coeus.exception.CoeusUIException
     * @return boolean
     */
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    /*
     * Method to register the components in the form
     */
    public void registerComponents() {
        if(functionType != TypeConstants.DISPLAY_MODE){
            subcontractReportsForm.btnAdd.addActionListener(this);
            subcontractReportsForm.btnDelete.addActionListener(this);
        }
    }
    
    /*
     * Method to save the form data to query engine
     */
    public void saveFormData(){
        CoeusVector cvModelData = reportsModel.getData();
        if(cvModelData != null && !cvModelData.isEmpty()){
            for(Object reportbean : cvModelData){
                SubcontractReportBean subcontractReportBean  = (SubcontractReportBean)reportbean;
                if(TypeConstants.INSERT_RECORD.equals(subcontractReportBean.getAcType())){
                    queryEngine.insert(queryKey,subcontractReportBean);
                }
            }
        }
        
    }
    
    /*
     * Method to refresh the form
     */
    public void refresh(){
        if (isRefreshRequired()) {
            setFormData(subContractBean);
            setRefreshRequired(false);
        }
    }
    
    public void display() {
    }
    
    
    class ReportsModel extends DefaultTableModel {
        private String colNames[] = {"","Code","Description","Last Updated", "Updated  By"};
        private Class colTypes[]  = {Object.class, Integer.class, String.class, String.class, String.class};
        private CoeusVector cvData;
        
        /**
         * Method to check whether the selected column can be edited
         * @param row
         * @param column
         * @return
         */
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        
        /**
         * Method to get the value for row and column
         * @param row
         * @param column
         * @return Object
         */
        public Object getValueAt(int row, int column) {
            if(cvData != null && !cvData.isEmpty()){
                SubcontractReportBean subcontractReportBean = (SubcontractReportBean)cvData.get(row);
                switch(column) {
                    case CODE_COLUMN:
                        return subcontractReportBean.getReportTypeCode();
                    case DESCRIPTION_COLUMN:
                        return subcontractReportBean.getReportTypeDescription();
                    case LAST_UPDATE_TIMESTAMP_COLUMN:
                        String updateTimeStamp = CoeusGuiConstants.EMPTY_STRING;
                        if(!TypeConstants.INSERT_RECORD.equals(subcontractReportBean.getAcType()) && subcontractReportBean.getUpdateTimestamp() != null){
                            updateTimeStamp =  CoeusDateFormat.format(subcontractReportBean.getUpdateTimestamp().toString());
                        }
                        return updateTimeStamp;
                    case LAST_UPDATE_USER_COLUMN:
                        return subcontractReportBean.getUpdateUserName();
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
            cvData.sort("updateTimestamp");
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
        
        
    }
    
    /**
     * Method to display the report types
     * @throws edu.mit.coeus.exception.CoeusException
     */
    private void displayReportTypes() throws CoeusException {
        CoeusVector cvReportTypes = queryEngine.getDetails(queryKey,KeyConstants.SUBCONTRACT_REPORT_TYPES);
        CoeusVector cvSubContractReport = reportsModel.getData();
        if(cvSubContractReport != null && !cvSubContractReport.isEmpty()){
            cvReportTypes = getReportTypesNotInSubcontract(cvReportTypes,cvSubContractReport);
        }
        if(cvReportTypes == null || cvReportTypes.isEmpty()){
            if(cvSubContractReport == null || cvSubContractReport.isEmpty()){
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("subcontractReport_exceptionCode.1000"));
            }else{
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("subcontractReport_exceptionCode.1001"));
            }
        }else{
            TypeSelectionLookUp typeSelectionLookUp = new TypeSelectionLookUp("Select Report Types",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            typeSelectionLookUp.setFormData(cvReportTypes);
            typeSelectionLookUp.display();
            CoeusVector cvSelectedTypes = typeSelectionLookUp.getSelectedTypes();
            CoeusVector cvModelData = reportsModel.getData();
            if(cvSelectedTypes != null && !cvSelectedTypes.isEmpty()){
                subcontractReportsForm.btnDelete.setEnabled(true);
                if(cvModelData == null){
                    cvModelData = new CoeusVector();
                }
                for(Object subcontractReport : cvSelectedTypes){
                    CoeusTypeBean reportType = (CoeusTypeBean)subcontractReport;
                    SubcontractReportBean subcontractReportBean = new SubcontractReportBean();
                    subcontractReportBean.setSubContractCode(subContractBean.getSubContractCode());
                    subcontractReportBean.setSequenceNumber(subContractBean.getSequenceNumber());
                    subcontractReportBean.setReportTypeCode(reportType.getTypeCode());
                    subcontractReportBean.setReportTypeDescription(reportType.getTypeDescription());
                    subcontractReportBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvModelData.add(subcontractReportBean);
                }
            }
            reportsModel.setData(cvModelData);
            if(cvModelData != null && !cvModelData.isEmpty()){
                subcontractReportsForm.tblReports.setRowSelectionInterval(0,0);
            }
        }
        
    }
    
    /**
     * Method to get reports which is not added in the subcontract
     * @param cvReportTypes
     * @param cvSubContractReport
     * @return cvFilteredTypes - CoeusVector
     */
    private CoeusVector getReportTypesNotInSubcontract(CoeusVector cvReportTypes, CoeusVector cvSubContractReport){
        CoeusVector cvFilteredTypes = null;
        if(cvReportTypes != null && !cvReportTypes.isEmpty()){
            cvFilteredTypes = new CoeusVector();
            for(Object reportTypes : cvReportTypes){
                CoeusTypeBean  reportTypeBean = (CoeusTypeBean)reportTypes;
                CoeusVector cvFilSubReport = cvSubContractReport.filter(new Equals("reportTypeCode",reportTypeBean.getTypeCode()));
                if(cvFilSubReport == null || cvFilSubReport.isEmpty()){
                    cvFilteredTypes.add(reportTypeBean);
                }
            }
        }
        return cvFilteredTypes;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if(subcontractReportsForm.btnAdd.equals(source)){
                
                displayReportTypes();
                
            }else if(subcontractReportsForm.btnDelete.equals(source)){
                deleteReport();
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Method to delete a selected report
     * @throws edu.mit.coeus.exception.CoeusException
     */
    private void deleteReport() throws CoeusException{
        int selectedRow = subcontractReportsForm.tblReports.getSelectedRow();
        if(selectedRow > -1){
            int selectedOption = CoeusOptionPane.showQuestionDialog("Do you want to delete this report?",
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            switch(selectedOption){
                case CoeusOptionPane.SELECTION_YES:
                    CoeusVector cvSubcontractReport = reportsModel.getData();
                    if(cvSubcontractReport != null && !cvSubcontractReport.isEmpty()){
                        SubcontractReportBean subcontractReportBean = (SubcontractReportBean)cvSubcontractReport.get(selectedRow);
                        if(!TypeConstants.INSERT_RECORD.equals(subcontractReportBean.getAcType())){
                            subcontractReportBean.setAcType(TypeConstants.DELETE_RECORD);
                            queryEngine.delete(queryKey,subcontractReportBean);
                        }
                        cvSubcontractReport.remove(selectedRow);
                        reportsModel.setData(cvSubcontractReport);
                        if(cvSubcontractReport != null && !cvSubcontractReport.isEmpty()){
                            int newRowCount = subcontractReportsForm.tblReports.getRowCount();
                            if(newRowCount >0){
                                if(newRowCount > selectedRow){
                                    subcontractReportsForm.tblReports.setRowSelectionInterval(selectedRow,selectedRow);
                                }else{
                                    subcontractReportsForm.tblReports.setRowSelectionInterval(newRowCount - 1,newRowCount - 1);
                                }
                                int rowCount = subcontractReportsForm.tblReports.getRowCount() -1 ;
                                subcontractReportsForm.tblReports.requestFocusInWindow();
                            }
                        }
                        setSaveRequired(true);
                    }
                    if(cvSubcontractReport == null || cvSubcontractReport.isEmpty()){
                        subcontractReportsForm.btnDelete.setEnabled(false);
                    }
            }
        }
    }
    
}
