/*
 * ReportingRequirementsController.java
 * Created on July 14, 2004, 11:26 AM
 * @author   bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardReportReqBean;
import edu.mit.coeus.award.controller.RepRequirementController;
import edu.mit.coeus.award.gui.ReportingRequirementsForm;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.search.gui.CoeusSearch;


import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.table.AbstractTableModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.event.TableColumnModelListener;


public class ReportingRequirementsController extends RepRequirementController
implements ListSelectionListener,TableColumnModelListener{
    
    //Modified for case#2268 - Report Tracking Functionality - start
    private static final int REPORT_TYPE_COLUMN = 0;
    private static final int FREQUENCY_COLUMN = 1;
    private static final int FREQUENCY_BASE_COLUMN = 2;
    private static final int BASE_DATE_COLUMN = 3;
    private static final int STATUS_COLUMN = 4;
    private static final int DISTRIBUTION_COLUMN = 5;
//    private static final int CONTACT_COLUMN = 6;
//    private static final int ADDRESS_COLUMN = 7;
    private static final int DUE_DATE_COLUMN = 6;
//    private static final int COPIES_COLUMN = 9;
    private static final int OVERDUE_COUNTER_COLUMN = 7;
    private static final int ACTIVITY_DATE_COLUMN = 8;
    private static final int COMMENTS_COLUMN = 9;
    private static final int PERSON_COLUMN = 10;
    private static final int LAST_UPDATE_COLUMN = 11;
    //Modified for case#2268 - Report Tracking Functionality - end
    
    private static final String EMPTY_STRING = "";
    private DateUtils dateUtils = new DateUtils();
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String EDITING_DATEFORMAT = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    private static final String  INVALID_PERSON ="repRequirements_exceptionCode.1053";
    // Added for COEUSQA-2794_Specific error message required for Person entry in Award Reporting Requirements maintenance_start
    private static final String INVALID_PERSON_MSG = "repRequirements_exceptionCode.1060";
    // Added for COEUSQA-2794_Specific error message required for Person entry in Award Reporting Requirements maintenance_end
    private boolean parent;
    
    /** Simple Date Format. */
    private SimpleDateFormat simpleDateFormat;
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    //Modified for case#2268 - Report Tracking Functionality - start
    //will be used in sub class also
    private String colNames[] =
    {"Rep Type","Frequency","Frequency Base","Base Date","Status","Distribution",
     /*"Contact","Address",*/ "Due Date", /*"Copies",*/
     "Overdue #","Activity Date","Comments","Person","Last Update"
    };
    
    private String columnFields [] =
    {"reportDescription","frequencyDescription","frequencyBaseDescription",
     "frequencyBase","reportStatusDescription","ospDistributionDescription",
     /*"contactTypeDescription","address",*/"dueDate",/*"numberOfCopies",*/"overdueCounter",
     "activityDate","comments","personId","updateTimestamp"
    };
    
    private Class colTypes[] =
    {String.class, String.class, String.class,
     Date.class, String.class, String.class,
     /*String.class,String.class,*/ Date.class,/*Integer.class,*/ Integer.class,
     Date.class, String.class, String.class, String.class
    };
    
    private boolean colEditable[] =
    {false, false,false,false, true,false,/*false, false,*/ true, /*false,*/ true,
     true,true,true, false
    };
    
    protected TableColumn groupTableColumns[] = new TableColumn[colNames.length];
    protected TableColumn detailsTableColumns[] = new TableColumn[colNames.length];
    
//    private int visibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14};
    private int visibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11};
    
//    private boolean boolGroupVisibleColumns [] ={true,true,true,true,false,true,false,false,false,false,
//    false,false,false,false,false};
    private boolean boolGroupVisibleColumns [] ={true,true,true,true,false,true,false,
    false,false,false,false,false};    
        
//    private boolean boolDetailsVisibleColumns [] ={false,false,false,false,true,false,true,true,true,true,
//    true,true,true,true,true};
    private boolean boolDetailsVisibleColumns [] ={false,false,false,false,true,false,true,
    true,true,true,true,true};        
    
//    private int groupVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14};//{4, 6, 7, 8, 9,10,11,12,13,14};
//    private int detailsVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14};//{4, 6, 7, 8, 9,10,11,12,13,14};
    private int groupVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11};
    private int detailsVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11};
    
    /*
     * UserId to UserName Enhancement - Start
     * Modified the width of the user id to display username
     */     
    //private int colSize[] = {125, 170, 170, 125,100, 150, 190, 200, 75, 60, 80, 90,200,100,180};
//    private int colSize[] = {125, 170, 170, 125,100, 150, 190, 200, 75, 60, 80, 90,200,100,200};
    private int colSize[] = {125, 170, 170, 125,100, 150, 75, 80, 90,200,100,200};    
    //UserId to UserName Enhancement - End
    //Modified for case#2268 - Report Tracking Functionality - end
    
    protected ReportingRequirementsForm reportingRequirementsForm;
    protected CoeusVector cvTableData;
    protected CoeusVector cvGrpTableData;
    private ReportingRequirementsTableModel groupTableModel;
    protected ReportingRequirementsTableModel detailsTableModel;
    protected ReportTableRenderer groupTableRenderer;
    protected ReportTableRenderer detailsTableRenderer;
    private ReportTableCellEditor detailsTableCellEditor;
    private ListSelectionModel selectionModel;
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    protected CoeusVector cvCurrentDetailsData;
    private CoeusVector cvStatus;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private String personName = "";
    private String personId = "";
    
    private static final String SERVLET = "/AwardReportReqMaintenanceServlet";
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final char GET_PERSON_DETAILS = 'G';
    
    
    private static final char GROUP_TABLE = 'G';
    private static final char DETAILS_TABLE = 'D';
    private boolean tableModified = false;
    private boolean columnsMoved = false;
    private boolean detailsColumnsMoved = false;
    
    private boolean focus = false;
    private String personNumber;
    private String perName;
    
      
   
    
  
    
    /** Creates a new instance of ReportingRequirementsController */
    public ReportingRequirementsController(AwardBaseBean awardBaseBean,char functionType) {
        super(awardBaseBean);
        setFunctionType(functionType);
        initComponents();
        registerComponents();
        columnRenderers();
        setColumnData();
    }
    
    public ReportingRequirementsController() {
        super();
        setFunctionType(TypeConstants.DISPLAY_MODE);
    }
    /**
     * This method is called from AwardReportingRequiements to initialize components
     */
    public void initialize() {
        initComponents();
        registerComponents();
        columnRenderers();
        setColumnData();
    }
    protected void setDefaultRowSelection() {
        reportingRequirementsForm.tblDetails.setRowSelectionInterval(0,0);
        
    }
    /**
     * Initializes the components
     */
    public void initComponents() {
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        reportingRequirementsForm = new ReportingRequirementsForm();
        groupTableModel = new ReportingRequirementsTableModel(GROUP_TABLE);
        groupTableModel.setAllColumnsNonEditable();
        
        detailsTableModel = new ReportingRequirementsTableModel(DETAILS_TABLE);
        groupTableRenderer = new ReportTableRenderer(GROUP_TABLE);
        detailsTableRenderer = new ReportTableRenderer(DETAILS_TABLE);
        detailsTableCellEditor = new ReportTableCellEditor();
        
        if (getFunctionType() == TypeConstants.DISPLAY_MODE) {
            detailsTableModel.setAllColumnsNonEditable();
        } else {
            detailsTableModel.setEditableColumns(colEditable);
        }
        /*groupTableModel.setVisibleColumns(groupVisibleColumns);
        groupTableRenderer.setVisibleColumns(groupVisibleColumns);
        detailsTableModel.setVisibleColumns(detailsVisibleColumns);
        detailsTableRenderer.setVisibleColumns(detailsVisibleColumns);*/
        cvCurrentDetailsData = new CoeusVector();
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    }
    
    
    protected void columnRenderers() {
        reportingRequirementsForm.tblDetails.setModel(detailsTableModel);
        reportingRequirementsForm.tblGroup.setModel(groupTableModel);
        TableColumn tableColumn = null;
        //For group table
        JTableHeader tableHeader = reportingRequirementsForm.tblGroup.getTableHeader();
        tableHeader.setReorderingAllowed(true);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.addMouseListener(new GroupColumnHeaderListener());
        reportingRequirementsForm.tblGroup.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_OFF);
        reportingRequirementsForm.tblGroup.setRowHeight(18);
        reportingRequirementsForm.tblGroup.setShowHorizontalLines(true);
        reportingRequirementsForm.tblGroup.setShowVerticalLines(true);
        reportingRequirementsForm.tblGroup.setOpaque(false);
        reportingRequirementsForm.scrpnGroup.getViewport().setBackground(java.awt.Color.white);
        reportingRequirementsForm.tblGroup.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        for(int col = 0; col < groupVisibleColumns.length; col++) {
            tableColumn = reportingRequirementsForm.tblGroup.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[visibleColumns[col]]);
            groupTableColumns[col] = tableColumn;
            tableColumn.setCellRenderer(groupTableRenderer);
        }
        
        //For details table
        JTableHeader detailTableHeader = reportingRequirementsForm.tblDetails.getTableHeader();
        detailTableHeader.setReorderingAllowed(true);
        detailTableHeader.setFont(CoeusFontFactory.getLabelFont());
        detailTableHeader.addMouseListener(new DetailColumnHeaderListener());
        reportingRequirementsForm.tblDetails.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_OFF);
        reportingRequirementsForm.tblDetails.setRowHeight(18);
        reportingRequirementsForm.tblDetails.setShowHorizontalLines(true);
        reportingRequirementsForm.tblDetails.setShowVerticalLines(true);
        reportingRequirementsForm.tblDetails.setOpaque(false);
        reportingRequirementsForm.scrpnDetails.getViewport().setBackground(java.awt.Color.white);
        tableColumn = null;
        for(int col = 0; col < detailsVisibleColumns.length; col++) {
            tableColumn = reportingRequirementsForm.tblDetails.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[visibleColumns[col]]);
            tableColumn.setCellRenderer(detailsTableRenderer);
            detailsTableColumns[col] = tableColumn;
            tableColumn.setCellEditor(detailsTableCellEditor);
        }
    }
    /**
     * Sets the column properties
     */
    protected void setColumnData() {
        reportingRequirementsForm.tblDetails.setModel(detailsTableModel);
        reportingRequirementsForm.tblGroup.setModel(groupTableModel);
        TableColumn tableColumn = null;
        columnRenderers();
        reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().removeColumnModelListener(this);
        int count = 0;
        int cnt=0;
        for(int index = 0; count < groupVisibleColumns.length; count++) {
            if(boolGroupVisibleColumns[count] == false) {
                tableColumn = reportingRequirementsForm.tblGroup.getColumnModel().getColumn(index);
                reportingRequirementsForm.tblGroup.removeColumn(tableColumn);
            }else {
                index++;
                cnt++;
            }
        }
        int group[] = new int[cnt];
        int arrIndex=0;
        for (int indx=0;indx<boolGroupVisibleColumns.length;indx++) {
            if (boolGroupVisibleColumns[indx]) {
                group[arrIndex]=indx;
                arrIndex++;
            }
        }
        // groupVisibleColumns = null;
        //groupVisibleColumns = new int[group.length];
        this.groupVisibleColumns = group;
        reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().addColumnModelListener(this);
        reportingRequirementsForm.tblGroup.getTableHeader().addMouseListener(new GroupColumnHeaderListener());
        reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().removeColumnModelListener(this);
        count = 0;
        cnt=0;
        for(int index = 0; count < detailsVisibleColumns.length; count++) {
            if(boolDetailsVisibleColumns[count] == false) {
                tableColumn = reportingRequirementsForm.tblDetails.getColumnModel().getColumn(index);
                reportingRequirementsForm.tblDetails.removeColumn(tableColumn);
            }else {
                index++;
                cnt++;
            }
        }
        int detail[]= new int[cnt];
        int arrayIndex=0;
        for (int indx=0;indx<boolDetailsVisibleColumns.length;indx++) {
            if (boolDetailsVisibleColumns[indx]) {
                detail[arrayIndex]=indx;
                arrayIndex++;
            }
        }
        this.detailsVisibleColumns = detail;
        reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().addColumnModelListener(this);
        reportingRequirementsForm.tblDetails.getTableHeader().addMouseListener(new DetailColumnHeaderListener());
    }
    
    
    
    /**
     * This method handles the editing of reporting requirements
     * It identifies the selected rows in the table and calls the setFormData()
     * of EditReportingRequirements.
     */
    public void editReportingRequirements() {
        int selectedRowIndices[] = reportingRequirementsForm.tblDetails.getSelectedRows();
        CoeusVector cvEditData= new CoeusVector();
        for (int index=0;index<selectedRowIndices.length;index++) {
            AwardReportReqBean bean = (AwardReportReqBean) cvCurrentDetailsData.get(selectedRowIndices[index]);
            if (bean!=null) {
                cvEditData.add(bean);
            }
        }
        EditReportingRequirementController editReportingRequirementController;
        if (cvEditData != null && cvEditData.size()>0) {
            editReportingRequirementController = new EditReportingRequirementController(awardBaseBean,getFunctionType());
            editReportingRequirementController.setFormData(cvEditData);
            int retEdit = editReportingRequirementController.displayEditDialog();
            if (retEdit == editReportingRequirementController.OK_SELECTED) {
                if(editReportingRequirementController.isModified()) {
                    tableModified = true;
                }
                CoeusVector cvEditedData = (CoeusVector)editReportingRequirementController.getFormData();
                cvEditData = cvEditedData;
            } //OK selected ends
        }
    }
    
    public void postInitComponents() {
    }
    /**
     * Registers the components
     */
    public void registerComponents() {
        selectionModel = reportingRequirementsForm.tblGroup.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().addColumnModelListener(this);
    }
    
    /**
     * Sets the data
     */
    public void setFormData(Object data) {
        try {
            cvTableData = (CoeusVector)data;
            if (cvTableData.size()<1) {
                return;
            }
            cvGrpTableData = getGroupedVector();
            groupTableModel.setData(cvGrpTableData);
            if (queryKey!=null) {
                cvStatus = (CoeusVector) queryEngine.getDetails(queryKey,KeyConstants.AWARD_REPORT_STATUS);
            }
            
            int selectedIdx = reportingRequirementsForm.tblGroup.getSelectedRow();
            
            groupTableModel.fireTableDataChanged();
            
            //Bug Fix 1933: Start 1
            /** The code has uncommnetd for the bug fix #2101. The reason for refreshing is a
             *different issue, it is not concerned with the row selection
             */
            if(selectedIdx==-1) {
                reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
            }
            else {
                reportingRequirementsForm.tblGroup.setRowSelectionInterval(selectedIdx,selectedIdx);
            }
            //Bug Fix 1933: End 1
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stopEditingTable() {
        detailsTableCellEditor.stopCellEditing();
    }
    
    public void saveFormData() {
        detailsTableCellEditor.stopCellEditing();
        try{
            for(int index=0;index<cvTableData.size();index++) {
                AwardReportReqBean bean = (AwardReportReqBean)cvTableData.get(index);
                if(bean.getAcType()==TypeConstants.UPDATE_RECORD) {
                    queryEngine.update(queryKey,bean);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    public void display() {
        
    }
    public void formatFields() {
        
    }
    public java.awt.Component getControlledUI() {
        return reportingRequirementsForm;
    }
    public Object getFormData() {
        return null;
    }
    public void selectAll() {
        if (detailsTableModel.getRowCount() >0) {
            reportingRequirementsForm.tblDetails.setRowSelectionInterval(0,detailsTableModel.getRowCount()-1);
        }
    }
    
    protected CoeusVector getGroupedVector() {
        CoeusVector cvGroupData = new CoeusVector();
        cvGroupData.add(cvTableData.get(0));
        for (int idx=1;idx<cvTableData.size();idx++) {
            AwardReportReqBean bean = (AwardReportReqBean)cvTableData.get(idx);
            Equals equalsReportDescription,equalsFrequencyDescription,equalsFrequencyBaseDescription,
            equalsFrequencyBase,equalsReportStatusDescription,equalsOspDistributionDescription,equalsContact,
            equalsAddress,equalsDueDate,equalsCopiesColumn,equalsOverdueCounter,equalsActivityDate,
            equalsComments,equalsPerson,equalsLastUpdate;
            Equals equalsArray[]= new Equals[15];
            And and;
            for (int index = 0;index < groupVisibleColumns.length;index++) {
                int currentColIndex = groupVisibleColumns[index];
                String columnFieldName = columnFields[currentColIndex];
                switch (currentColIndex) {
                    case REPORT_TYPE_COLUMN:
                        equalsReportDescription = new Equals(columnFieldName,bean.getReportDescription());
                        equalsArray [index] = equalsReportDescription;
                        break;
                    case FREQUENCY_COLUMN:
                        equalsFrequencyDescription = new Equals(columnFieldName,bean.getFrequencyDescription());
                        equalsArray [index] = equalsFrequencyDescription;
                        break;
                    case FREQUENCY_BASE_COLUMN:
                        equalsFrequencyBaseDescription = new Equals(columnFieldName,bean.getFrequencyBaseDescription());
                        equalsArray [index] = equalsFrequencyBaseDescription;
                        break;
                    case BASE_DATE_COLUMN:
                        equalsFrequencyBase = new Equals(columnFieldName,bean.getFrequencyBase());
                        equalsArray [index] = equalsFrequencyBase;
                        break;
                    case STATUS_COLUMN:
                        equalsReportStatusDescription  = new Equals(columnFieldName,bean.getReportStatusDescription());
                        equalsArray [index] = equalsReportStatusDescription;
                        break;
                    case DISTRIBUTION_COLUMN:
                        equalsOspDistributionDescription  = new Equals(columnFieldName,bean.getOspDistributionDescription());
                        equalsArray [index] = equalsOspDistributionDescription;
                        break;
                        //Commented for case#2268 - Report Tracking Functionality - start
//                    case CONTACT_COLUMN:
//                        equalsContact = new Equals(columnFieldName,bean.getContactTypeDescription());
//                        equalsArray [index] = equalsContact;
//                        break;
//                    case ADDRESS_COLUMN:
//                        equalsAddress = new Equals(columnFieldName,bean.getAddress());
//                        equalsArray [index] = equalsAddress;
//                        break;
                    case DUE_DATE_COLUMN:
                        equalsDueDate = new Equals(columnFieldName,bean.getDueDate());
                        equalsArray [index] = equalsDueDate;
                    
                        break;
//                    case COPIES_COLUMN:
//                        equalsCopiesColumn = new Equals(columnFieldName,new Integer(bean.getNumberOfCopies()));
//                        equalsArray [index] = equalsCopiesColumn;
//                        break;
                        //Commented for case#2268 - Report Tracking Functionality - end
                    case OVERDUE_COUNTER_COLUMN:
                        equalsOverdueCounter = new Equals(columnFieldName,new Integer(bean.getOverdueCounter()));
                        equalsArray [index] = equalsOverdueCounter;
                        break;
                    case ACTIVITY_DATE_COLUMN:
                        equalsActivityDate = new Equals(columnFieldName,bean.getActivityDate());
                        equalsArray [index] = equalsActivityDate;
                        break;
                    case COMMENTS_COLUMN:
                        equalsComments = new Equals(columnFieldName,bean.getComments());
                        equalsArray [index] = equalsComments;
                        break;
                    case PERSON_COLUMN:
                        equalsPerson = new Equals(columnFieldName,bean.getPersonId());
                        equalsArray [index] = equalsPerson;
                        break;
                    case LAST_UPDATE_COLUMN:
                        equalsLastUpdate = new Equals(columnFieldName,bean.getUpdateTimestamp());
                        equalsArray [index] = equalsLastUpdate;
                        break;
                } //switch ends
                
            } //Inner For loop ends
            CoeusVector cv=null;
            if (groupVisibleColumns.length == 1) {
                cv = cvGroupData.filter(equalsArray[0]);
            } else if (groupVisibleColumns.length > 1) {
                and =  new And(equalsArray[0],equalsArray[1]);
                for (int indx = 2; indx<groupVisibleColumns.length;indx++) {
                    and = new And(and,equalsArray[indx]);
                }
                cv = cvGroupData.filter(and);
            }
            if(cv!=null && cv.size()==0) {
                cvGroupData.add(bean);
            }
        } //Outer For loop ends
        return cvGroupData;
    }
    
    
    public void removeGroupListener() {
        reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().removeColumnModelListener(this);
    }
    
    public void removeDetailsListener() {
        reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().removeColumnModelListener(this);
    }
    
    public void addGroupListener() {
        reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().addColumnModelListener(this);
    }
    
    public void addDetailsListener() {
        reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().addColumnModelListener(this);
    }
    
    
    public void customizeView(int groupColumns[],int detailColumns[]) {
        
        int newVisibleColumns[] = new int[groupColumns.length];
        int visibleCount = 0;
        int columns =reportingRequirementsForm.tblGroup.getColumnCount();
        
        // For group columns
        for(int index = 0; index < colNames.length; index++) {
            //setting visible columns
            boolean isPresent = false;
            //to check the item is present or not in the customized columns
            for (int i = 0;i<groupColumns.length;i++) {
                if (visibleColumns[index] ==groupColumns[i]) {
                    isPresent = true;
                    break;
                }
            }
            
            if(isPresent) {
                newVisibleColumns[visibleCount] = index;
                visibleCount++;
            }
            
            
            //if columns moved then no need to add/delete its done later in same method
            //when all columns get deleted and added orderly.
            if(! columnsMoved) {
                if(! (isPresent == boolGroupVisibleColumns[index])) {
                    //set column visibility.
                    if(isPresent) {
                        //Show Column
                        reportingRequirementsForm.tblGroup.addColumn(groupTableColumns[index]);
                        boolGroupVisibleColumns[index] = true;
                        //Since Column gets appended.Move Column to its original location.
                        reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().removeColumnModelListener(this);
                        reportingRequirementsForm.tblGroup.moveColumn(
                        columns , visibleCount-1);
                        reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().addColumnModelListener(this);
                        columns++;
                    }else{
                        //Hide Column
                        reportingRequirementsForm.tblGroup.removeColumn(groupTableColumns[index]);
                        boolGroupVisibleColumns[index] = false;
                        columns--;
                    }
                }//End IF
            }//End If Columns Moved
        }//End For
        
        
        //Check If Columns have moved.if moved move it back to its original position.
        if(columnsMoved) {
            reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().removeColumnModelListener(this);
            //Column has moved. move to original location
            
            int len = reportingRequirementsForm.tblGroup.getColumnCount();
            for(int i = 0; i < len; i++) {
                if(groupVisibleColumns.length < i) break;
                reportingRequirementsForm.tblGroup.removeColumn(groupTableColumns[groupVisibleColumns[i]]);
            }
            
            //set All columns visibility -> false
            for(int index = 0; index < boolGroupVisibleColumns.length; index++) {
                boolGroupVisibleColumns[index] = false;
            }
            
            for(int i = 0; i < newVisibleColumns.length; i++) {
                reportingRequirementsForm.tblGroup.addColumn(groupTableColumns[newVisibleColumns[i]]);
                boolGroupVisibleColumns[newVisibleColumns[i]] = true;
            }
            columnsMoved = false;
            reportingRequirementsForm.tblGroup.getTableHeader().getColumnModel().addColumnModelListener(this);
        }//End If
        
        // Group column ends
        
        
        
        //For details column
        
        int newDetailsVisibleColumns[] = new int[detailColumns.length];
        int detailsVisibleCount = 0;
        int detailsColumns = reportingRequirementsForm.tblDetails.getColumnCount();
        
        
        for(int index = 0; index < colNames.length; index++) {
            //setting visible columns
            boolean isPresent = false;
            //to check the item is present or not in the customized columns
            for (int i = 0;i<detailColumns.length;i++) {
                if (visibleColumns[index] ==detailColumns[i]) {
                    isPresent = true;
                    break;
                }
            }
            
            if(isPresent) {
                newDetailsVisibleColumns[detailsVisibleCount] = index;
                detailsVisibleCount++;
            }
            
            
            //if columns moved then no need to add/delete its done later in same method
            //when all columns get deleted and added orderly.
            if(! detailsColumnsMoved) {
                if(! (isPresent == boolDetailsVisibleColumns[index])) {
                    //set column visibility.
                    if(isPresent) {
                        //Show Column
                        reportingRequirementsForm.tblDetails.addColumn(detailsTableColumns[index]);
                        boolDetailsVisibleColumns[index] = true;
                        //Since Column gets appended.Move Column to its original location.
                        reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().removeColumnModelListener(this);
                        reportingRequirementsForm.tblDetails.moveColumn(
                        detailsColumns , detailsVisibleCount-1);
                        reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().addColumnModelListener(this);
                        detailsColumns++;
                    }else{
                        //Hide Column
                        reportingRequirementsForm.tblDetails.removeColumn(detailsTableColumns[index]);
                        boolDetailsVisibleColumns[index] = false;
                        detailsColumns--;
                    }
                }//End IF
            }//End If Columns Moved
        }//End For
        
        
        //Check If Columns have moved.if moved move it back to its original position.
        if(detailsColumnsMoved) {
            reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().removeColumnModelListener(this);
            //Column has moved. move to original location
            
            int len = reportingRequirementsForm.tblDetails.getColumnCount();
            for(int i = 0; i < len; i++) {
                if(detailsVisibleColumns.length < i) break;
                reportingRequirementsForm.tblDetails.removeColumn(detailsTableColumns[detailsVisibleColumns[i]]);
            }
            
            //set All columns visibility -> false
            for(int index = 0; index < boolDetailsVisibleColumns.length; index++) {
                boolDetailsVisibleColumns[index] = false;
            }
            
            for(int i = 0; i < newDetailsVisibleColumns.length; i++) {
                reportingRequirementsForm.tblDetails.addColumn(detailsTableColumns[newDetailsVisibleColumns[i]]);
                boolDetailsVisibleColumns[newDetailsVisibleColumns[i]] = true;
            }
             
            detailsColumnsMoved = false;
            
            reportingRequirementsForm.tblDetails.getTableHeader().getColumnModel().addColumnModelListener(this);
        }//End If
        // bug fix 1211 :: begin
        setGroupVisibleColumns(newVisibleColumns);
        setDetailsVisibleColumns(newDetailsVisibleColumns);
        // bug fix 1211 :: end
        
        cvGrpTableData=getGroupedVector();
        groupTableModel.setData(cvGrpTableData);
        groupTableModel.fireTableDataChanged();
        
        //Bug Fix 1933: Start 2
        /*reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
        setCurrentDetailData(0);
        detailsTableModel.fireTableDataChanged();
        //setColumnData();
        
        if (detailsTableModel.getRowCount()>0) {
            reportingRequirementsForm.tblDetails.setRowSelectionInterval(0,0);
        }*/
        //Bug Fix 1933: End 2
        
    }
    public void sortGroup() {
        String fieldNames[] = new String[groupVisibleColumns.length];
        boolean sort = false;
        for (int index=0;index<groupVisibleColumns.length; index++ ) {
            fieldNames[index] = columnFields[groupVisibleColumns[index]];
            sort = true;
        }
        if (sort) {
            if (fieldNames!=null && fieldNames.length > 0) {
                cvGrpTableData.sort(fieldNames,true);//Added for bug fix 1151 - 15-Feb-2005
                groupTableRowsUpdated();
            }
        }
    }
    public void movingColumns(int orginalArray[]) {
        int newGrpVisibleColumns1[] = new int[groupVisibleColumns.length];
        for (int indx = 0;indx<groupVisibleColumns.length;indx++) {
            String name = reportingRequirementsForm.tblGroup.getColumnName(indx);
            for (int counter = 0;counter<colNames.length;counter++) {
                if(name.equals(colNames[counter])) {
                    groupVisibleColumns[indx] = counter;
                    break;
                }
            }
        }
        int newGrpVisibleColumns[] = new int[groupVisibleColumns.length];
        for (int idx = 0;idx<groupVisibleColumns.length;idx++) {
            newGrpVisibleColumns[idx] = groupVisibleColumns[idx];
            
        }
        int tIndex=0;
        while (tIndex<groupVisibleColumns.length) {
            int elementAtIndex = newGrpVisibleColumns[tIndex];
            int orginalIndex=-1;
            // get the orginal index
            for (int oIndex=0; oIndex<orginalArray.length;oIndex++) {
                if(elementAtIndex==orginalArray[oIndex]) {
                    orginalIndex=oIndex;
                    break; // Got index
                }
            }
            if (tIndex!=orginalIndex) { //Move column and the array values
                //move column
                reportingRequirementsForm.tblGroup.moveColumn(tIndex, orginalIndex);
                for (int tempIndex=tIndex;tempIndex<orginalIndex;tempIndex++) {
                    newGrpVisibleColumns[tempIndex] = newGrpVisibleColumns[tempIndex+1];
                }
                newGrpVisibleColumns[orginalIndex] = elementAtIndex;
            }else { //column not moved
                tIndex++;
            }
        }
        for (int idx = 0;idx<newGrpVisibleColumns.length;idx++) {
            groupVisibleColumns[idx] = newGrpVisibleColumns[idx];
        }
    }
    
    
    public int getSelectedDetailItem() {
        int index = reportingRequirementsForm.tblDetails.getSelectedRow();
        return index;
    }
    
    public void setSelectedDetailItem(int index) {
        if(index!= -1){
            reportingRequirementsForm.tblDetails.setRowSelectionInterval(index,index);
        }
    }
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        if(!listSelectionEvent.getValueIsAdjusting() ){
            int selectedRow = reportingRequirementsForm.tblGroup.getSelectedRow();
            // It calls the method of corresponding class either super class's or its sub class's method
            setCurrentDetailData(selectedRow);
        } // If codition for chcking not adjusting ends
    }
    
    protected void setCurrentDetailData(int currentRow) {
        // Bug fix for : 1158,1159,1160,1161
        if (detailsTableModel.getRowCount()>0) {
            detailsTableCellEditor.stopCellEditing();
        }
        // Bug fix for : 1158,1159,1160,1161 ends
        if (currentRow ==-1) {
            cvCurrentDetailsData = cvTableData;
            detailsTableModel.setData(cvCurrentDetailsData);
            detailsTableModel.fireTableDataChanged();
            if (cvCurrentDetailsData != null && cvCurrentDetailsData.size()> 0) {
                reportingRequirementsForm.tblDetails.setRowSelectionInterval(0,0);
            }
            return;
        }
        AwardReportReqBean currentBean = (AwardReportReqBean)cvGrpTableData.get(currentRow);
        Equals equalsReportDescription,equalsFrequencyDescription,equalsFrequencyBaseDescription,
        equalsFrequencyBase,equalsReportStatusDescription,equalsOspDistributionDescription,equalsContact,
        equalsAddress,equalsDueDate,equalsCopiesColumn,equalsOverdueCounter,equalsActivityDate,
        equalsComments,equalsPerson,equalsLastUpdate;
        Equals equalsArray[]= new Equals[colNames.length];
        And and;
        for (int index = 0;index < groupVisibleColumns.length;index++) {
            int currentColIndex = groupVisibleColumns[index];
            String columnFieldName = columnFields[groupVisibleColumns[index]];
            switch (currentColIndex) {
                case REPORT_TYPE_COLUMN:
                    equalsReportDescription = new Equals(columnFieldName,currentBean.getReportDescription());
                    equalsArray [index] = equalsReportDescription;
                    break;
                case FREQUENCY_COLUMN:
                    equalsFrequencyDescription = new Equals(columnFieldName,currentBean.getFrequencyDescription());
                    equalsArray [index] = equalsFrequencyDescription;
                    break;
                case FREQUENCY_BASE_COLUMN:
                    equalsFrequencyBaseDescription = new Equals(columnFieldName,currentBean.getFrequencyBaseDescription());
                    equalsArray [index] = equalsFrequencyBaseDescription;
                    break;
                case BASE_DATE_COLUMN:
                    equalsFrequencyBase = new Equals(columnFieldName,currentBean.getFrequencyBase());
                    equalsArray [index] = equalsFrequencyBase;
                    break;
                case STATUS_COLUMN:
                    equalsReportStatusDescription  = new Equals(columnFieldName,currentBean.getReportStatusDescription());
                    equalsArray [index] = equalsReportStatusDescription;
                    break;
                case DISTRIBUTION_COLUMN:
                    equalsOspDistributionDescription  = new Equals(columnFieldName,currentBean.getOspDistributionDescription());
                    equalsArray [index] = equalsOspDistributionDescription;
                    break;
                    //Commented for case#2268 - Report Tracking Functionality - start
//                case CONTACT_COLUMN:
//                    equalsContact = new Equals(columnFieldName,currentBean.getContactTypeDescription());
//                    equalsArray [index] = equalsContact;
//                    break;
//                case ADDRESS_COLUMN:
//                    equalsAddress = new Equals(columnFieldName,currentBean.getAddress());
//                    equalsArray [index] = equalsAddress;
//                    break;
                case DUE_DATE_COLUMN:
                    equalsDueDate = new Equals(columnFieldName,currentBean.getDueDate());
                    equalsArray [index] = equalsDueDate;
                    break;
//                case COPIES_COLUMN:
//                    equalsCopiesColumn = new Equals(columnFieldName,new Integer(currentBean.getNumberOfCopies()));
//                    equalsArray [index] = equalsCopiesColumn;
//                    break;
                    //Commented for case#2268 - Report Tracking Functionality - end
                case OVERDUE_COUNTER_COLUMN:
                    equalsOverdueCounter = new Equals(columnFieldName,new Integer(currentBean.getOverdueCounter()));
                    equalsArray [index] = equalsOverdueCounter;
                    break;
                case ACTIVITY_DATE_COLUMN:
                    equalsActivityDate = new Equals(columnFieldName,currentBean.getActivityDate());
                    equalsArray [index] = equalsActivityDate;
                    break;
                case COMMENTS_COLUMN:
                    equalsComments = new Equals(columnFieldName,currentBean.getComments());
                    equalsArray [index] = equalsComments;
                    break;
                case PERSON_COLUMN:
                    equalsPerson = new Equals(columnFieldName,currentBean.getPersonId());
                    equalsArray [index] = equalsPerson;
                    break;
                case LAST_UPDATE_COLUMN:
                    equalsLastUpdate = new Equals(columnFieldName,currentBean.getUpdateTimestamp());
                    equalsArray [index] = equalsLastUpdate;
                    break;
            } //switch ends
            
        } //For loop ends
        if (groupVisibleColumns.length == 0) {
            cvCurrentDetailsData = cvTableData;
        } else if (groupVisibleColumns.length == 1) {
            cvCurrentDetailsData = cvTableData.filter(equalsArray[0]);
        } else if (groupVisibleColumns.length > 1) {
            and =  new And(equalsArray[0],equalsArray[1]);
            for (int indx = 2; indx<groupVisibleColumns.length;indx++) {
                and = new And(and,equalsArray[indx]);
            }
            cvCurrentDetailsData = cvTableData.filter(and);
        }
        detailsTableModel.setData(cvCurrentDetailsData);
        detailsTableModel.fireTableDataChanged();
        if (cvCurrentDetailsData.size()> 0) {
            reportingRequirementsForm.tblDetails.setRowSelectionInterval(0,0);
        }
        
    }
    
    /**
     * Getter for property tableModified.
     * @return Value of property tableModified.
     */
    public boolean isTableModified() {
        return tableModified;
    }
    
    /**
     * Setter for property tableModified.
     * @param tableModified New value of property tableModified.
     */
    public void setTableModified(boolean tableModified) {
        this.tableModified = tableModified;
    }
    
    public void columnAdded(javax.swing.event.TableColumnModelEvent e) {
    }
    
    public void columnMarginChanged(javax.swing.event.ChangeEvent e) {
    }
    
    public void columnMoved(javax.swing.event.TableColumnModelEvent tableColumnModelEvent) {
        Object source = tableColumnModelEvent.getSource();
        if (source.equals(reportingRequirementsForm.tblGroup.
        getTableHeader().getColumnModel())) {
            int fromIndex = tableColumnModelEvent.getFromIndex();
            int toIndex = tableColumnModelEvent.getToIndex();
            if(fromIndex != toIndex) {
                int from = groupVisibleColumns[fromIndex];
                groupVisibleColumns[fromIndex] = groupVisibleColumns[toIndex];
                groupVisibleColumns[toIndex] = from;
                columnsMoved = true;
            }
        } else if (source.equals(reportingRequirementsForm.tblDetails.
        getTableHeader().getColumnModel())) {
            int fromIndex = tableColumnModelEvent.getFromIndex();
            int toIndex = tableColumnModelEvent.getToIndex();
            if(fromIndex != toIndex) {
                int from = detailsVisibleColumns[fromIndex];
                detailsVisibleColumns[fromIndex] = detailsVisibleColumns[toIndex];
                detailsVisibleColumns[toIndex] = from;
                detailsColumnsMoved = true;
            }
        }
    }
    
    public void columnRemoved(javax.swing.event.TableColumnModelEvent e) {
    }
    
    public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {
    }
    
    /**
     * Getter for property colNames.
     * @return Value of property colNames.
     */
    public java.lang.String[] getColNames() {
        return this.colNames;
    }
    
    /**
     * Setter for property colNames.
     * @param colNames New value of property colNames.
     */
    public void setColNames(java.lang.String[] colNames) {
        this.colNames = colNames;
    }
    
    /**
     * Getter for property groupVisibleColumns.
     * @return Value of property groupVisibleColumns.
     */
    public int[] getGroupVisibleColumns() {
        return this.groupVisibleColumns;
    }
    
    /**
     * Setter for property groupVisibleColumns.
     * @param groupVisibleColumns New value of property groupVisibleColumns.
     */
    public void setGroupVisibleColumns(int[] groupVisibleColumns) {
        this.groupVisibleColumns = groupVisibleColumns;
    }
    
    /**
     * Getter for property detailsVisibleColumns.
     * @return Value of property detailsVisibleColumns.
     */
    public int[] getDetailsVisibleColumns() {
        return this.detailsVisibleColumns;
    }
    
    /**
     * Setter for property detailsVisibleColumns.
     * @param detailsVisibleColumns New value of property detailsVisibleColumns.
     */
    public void setDetailsVisibleColumns(int[] detailsVisibleColumns) {
        this.detailsVisibleColumns = detailsVisibleColumns;
    }
    
    /**
     * Getter for property boolDetailsVisibleColumns.
     * @return Value of property boolDetailsVisibleColumns.
     */
    public boolean[] getBoolDetailsVisibleColumns() {
        return this.boolDetailsVisibleColumns;
    }
    
    /**
     * Setter for property boolDetailsVisibleColumns.
     * @param boolDetailsVisibleColumns New value of property boolDetailsVisibleColumns.
     */
    public void setBoolDetailsVisibleColumns(boolean[] boolDetailsVisibleColumns) {
        this.boolDetailsVisibleColumns = boolDetailsVisibleColumns;
    }
    
    /**
     * Getter for property boolGroupVisibleColumns.
     * @return Value of property boolGroupVisibleColumns.
     */
    public boolean[] getBoolGroupVisibleColumns() {
        return this.boolGroupVisibleColumns;
    }
    
    /**
     * Setter for property boolGroupVisibleColumns.
     * @param boolGroupVisibleColumns New value of property boolGroupVisibleColumns.
     */
    public void setBoolGroupVisibleColumns(boolean[] boolGroupVisibleColumns) {
        this.boolGroupVisibleColumns = boolGroupVisibleColumns;
    }
    
    /**
     * Getter for property visibleColumns.
     * @return Value of property visibleColumns.
     */
    public int[] getVisibleColumns() {
        return this.visibleColumns;
    }
    
    /**
     * Setter for property visibleColumns.
     * @param visibleColumns New value of property visibleColumns.
     */
    public void setVisibleColumns(int[] visibleColumns) {
        this.visibleColumns = visibleColumns;
    }
    
    /**
     * Getter for property colSize.
     * @return Value of property colSize.
     */
    public int[] getColSize() {
        return this.colSize;
    }
    
    /**
     * Setter for property colSize.
     * @param colSize New value of property colSize.
     */
    public void setColSize(int[] colSize) {
        this.colSize = colSize;
    }
    
    /**
     * Getter for property columnFields.
     * @return Value of property columnFields.
     */
    public java.lang.String[] getColumnFields() {
        return this.columnFields;
    }
    
    /**
     * Setter for property columnFields.
     * @param columnFields New value of property columnFields.
     */
    public void setColumnFields(java.lang.String[] columnFields) {
        this.columnFields = columnFields;
    }
    /**
     * This method returns the cell values for the table.
     * This method is being called from the table model's getvalueAt()
     * This method is overridden in the sub class
     */
    protected Object getTableCellValue(AwardReportReqBean awardReportReqBean,int columnIndex) {
        switch(columnIndex) {
            case REPORT_TYPE_COLUMN:
                return awardReportReqBean.getReportDescription();
            case FREQUENCY_COLUMN:
                return awardReportReqBean.getFrequencyDescription();
            case FREQUENCY_BASE_COLUMN :
                return awardReportReqBean.getFrequencyBaseDescription();
            case BASE_DATE_COLUMN:
                return awardReportReqBean.getFrequencyBase();
            case STATUS_COLUMN:
                int typeCode=awardReportReqBean.getReportStatusCode();
                CoeusVector filteredVector = cvStatus.filter(new Equals("code",""+typeCode));
                if(filteredVector!=null && filteredVector.size() > 0){
                    ComboBoxBean comboBoxBean = null;
                    comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                    return comboBoxBean;
                }else{
                    return new ComboBoxBean("","");
                }
                //return awardReportReqBean.getReportStatusDescription();
            case DISTRIBUTION_COLUMN:
                return awardReportReqBean.getOspDistributionDescription();
                //Commented for case#2268 - Report Tracking Functionality - start
//            case CONTACT_COLUMN:
//                return awardReportReqBean.getContactTypeDescription();
//            case ADDRESS_COLUMN:
//                return awardReportReqBean.getAddress();
            case DUE_DATE_COLUMN:
                return awardReportReqBean.getDueDate();
//            case COPIES_COLUMN:
//                return new Integer(awardReportReqBean.getNumberOfCopies());
                //Commented for case#2268 - Report Tracking Functionality - end
            case OVERDUE_COUNTER_COLUMN:
                return new Integer(awardReportReqBean.getOverdueCounter());
            case ACTIVITY_DATE_COLUMN:
                return awardReportReqBean.getActivityDate();
            case COMMENTS_COLUMN:
                return awardReportReqBean.getComments();
            case PERSON_COLUMN:
                if(awardReportReqBean.getFullName()!=null) {
                    return awardReportReqBean.getFullName();
                } else {
                    return EMPTY_STRING;
                }
            case LAST_UPDATE_COLUMN:
                String timestampFormat = REQUIRED_DATEFORMAT + " hh:mm a";
                simpleDateFormat.applyPattern(timestampFormat);
                if (awardReportReqBean.getUpdateTimestamp()==null) {
                    return null;
                }
//                return simpleDateFormat.format(awardReportReqBean.getUpdateTimestamp())+" "+awardReportReqBean.getUpdateUser();
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username
                 */
                return simpleDateFormat.format(awardReportReqBean.getUpdateTimestamp())+" "+awardReportReqBean.getUpdateUserName();
                // UserId to UserName Enhancement - End
            default :
                return null;
        }
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will  only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class GroupColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][]=new String[groupVisibleColumns.length][2];//groupVisibleColumns.length
        public GroupColumnHeaderListener() {
            super();
            init();
        }
        public void init() {
            for (int index=0;index<groupVisibleColumns.length;index++) {
                nameBeanId [index] [0] = ""+index;
                nameBeanId [index] [1] = columnFields[groupVisibleColumns[index]];
            }
        }
        
        boolean sort = true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                if(colNames.length>15) {
                    return;
                }
                int selIndex = reportingRequirementsForm.tblGroup.getSelectedRow();
                AwardReportReqBean selBean = null;
                if (selIndex !=-1) {
                    selBean = (AwardReportReqBean)cvGrpTableData.get(selIndex);
                }
                stopEditingTable();
                javax.swing.JTable table = ((JTableHeader)evt.getSource()).getTable();
                javax.swing.table.TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                if(cvGrpTableData!=null && cvGrpTableData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvGrpTableData).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    groupTableModel.fireTableRowsUpdated(
                    0, groupTableModel.getRowCount());
                }
                if (selIndex !=-1)  {
                    int indexToBSelected = cvGrpTableData.indexOf(selBean);
                    if (indexToBSelected!=-1) {
                        reportingRequirementsForm.tblGroup.setRowSelectionInterval(indexToBSelected, indexToBSelected);
                    }
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    public void groupTableRowsUpdated() {
        groupTableModel.fireTableRowsUpdated(
        0, groupTableModel.getRowCount());
    }
    
    public void detailsTableRowsUpdated() {
        detailsTableModel.fireTableRowsUpdated(
        0, detailsTableModel.getRowCount());
    }
    
    /**
     * Getter for property parent.
     * @return Value of property parent.
     */
    public boolean isParent() {
        return parent;
    }    
   
    /**
     * Setter for property parent.
     * @param parent New value of property parent.
     */
    public void setParent(boolean parent) {
        this.parent = parent;
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class DetailColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][]=new String[detailsVisibleColumns.length][2];//groupVisibleColumns.length
        public DetailColumnHeaderListener() {
            super();
            init();
        }
        public void init() {
            detailsTableCellEditor.stopCellEditing();
            for (int index=0;index<detailsVisibleColumns.length;index++) {
                nameBeanId [index] [0] = ""+index;
                nameBeanId [index] [1] = columnFields[detailsVisibleColumns[index]];
            }
        }
        
        boolean sort = true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                javax.swing.JTable table = ((JTableHeader)evt.getSource()).getTable();
                javax.swing.table.TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                String colName = table.getColumnName(vColIndex);
                //System.out.println("Colummn Name : "+colName);
                //BEGIN Case 2074
                String filedName = new String();
                if(isParent()){
                    filedName = getFormattedParentColumnNames(colName);
                }else{
                    filedName = getFormattedChildColumnNames(colName);
                }
                if(cvCurrentDetailsData!=null && cvCurrentDetailsData.size()>0 &&
                filedName.length() >1 ){
                    //END Case 2074
                    ((CoeusVector)cvCurrentDetailsData).sort(filedName,sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    detailsTableModel.fireTableRowsUpdated(
                    0, detailsTableModel.getRowCount());
                }
                
            } catch(Exception exception) {
                exception.printStackTrace();
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        }
        
        
        
        /** To identify the property name based on the column that is clicked for
         *the sorting Case #2074
         */
        private String getFormattedParentColumnNames(String colName){
            String fieldName = EMPTY_STRING;
            
            if(colName.equalsIgnoreCase("Due Date")){
                fieldName  = "stringDueDate";
            }else if(colName.equalsIgnoreCase("Unit No.")){
                fieldName  = "unitNumber";
            }else if(colName.equalsIgnoreCase("Unit Name")){
                fieldName =  "unitName";
                //Commented for case#2268 - Report Tracking Functionality - start
//            }else if(colName.equalsIgnoreCase("Contact")){
//                 fieldName = "contact";
//            }else if(colName.equalsIgnoreCase("Address")){
//                 fieldName = "address";   
//            }else if(colName.equalsIgnoreCase("Copies")){
//                 fieldName = "numberOfCopies";
                //Commented for case#2268 - Report Tracking Functionality - end
            }else if(colName.equalsIgnoreCase("Overdue #")){
                 fieldName = "overdueCounter";
            }else if(colName.equalsIgnoreCase("Activity Date")){
                 fieldName = "stringActivityDate";
            }else if(colName.equalsIgnoreCase("Comments")){
                 fieldName = "comments";
            }else if(colName.equalsIgnoreCase("Person Name")){
                 fieldName = "fullName";
            }else if(colName.equalsIgnoreCase("Status")){
                 fieldName = "reportStatusDescription";
            }else if(colName.equalsIgnoreCase("Report Type")){
                 fieldName = "reportClassDescription";
            }else if(colName.equalsIgnoreCase("Base Date")){
                 fieldName = "frequencyBase";
            }else if(colName.equalsIgnoreCase("Copy OSP")){
                 fieldName = "ospDistributionDescription";
            }
            //modified for case# 3008 - Principal Investigator misspelt as Principle Investigator - Start
            /*else if(colName.equalsIgnoreCase("Principle Investigator")){
                 fieldName = "principleInvestigator";*/
            else if(colName.equalsIgnoreCase("Principal Investigator")){
                 fieldName = "principalInvestigator";
            //modified for case# 3008 - Principal Investigator misspelt as Principle Investigator - Start
            }else if(colName.equalsIgnoreCase("title")){
                 fieldName = "title";
            }else if(colName.equalsIgnoreCase("Frequency")){
                 fieldName = "frequencyDescription";
            }else if(colName.equalsIgnoreCase("Sponsor Award Number")){
                 fieldName = "sponsorAwardNumber";
            }else if(colName.equalsIgnoreCase("Frequency Base")){
                fieldName = "frequencyBaseDescription";
            }else if(colName.equalsIgnoreCase("Award No.")){
                fieldName = "mitAwardNumber";
            }else if(colName.equalsIgnoreCase("Last Update")){
                fieldName = "updateTimestamp";
            }
            return fieldName;
        }
        
        /** If the window opened from the parent then, get the corresponding
         *column name according with the property name
         *Case #2074
         */
        private String getFormattedChildColumnNames(String colName){
             String fieldName = EMPTY_STRING;
            
            if(colName.equalsIgnoreCase("Due Date")){
                fieldName  = "dueDate";
            }else if(colName.equalsIgnoreCase("Unit No.")){
                fieldName  = "unitNumber";
            }else if(colName.equalsIgnoreCase("Unit Name")){
                fieldName =  "unitName";
                //Commented for case#2268 - Report Tracking Functionality - start
//            }else if(colName.equalsIgnoreCase("Contact")){
//                 fieldName = "contact";
//            }else if(colName.equalsIgnoreCase("Address")){
//                 fieldName = "address";   
//            }else if(colName.equalsIgnoreCase("Copies")){
//                 fieldName = "numberOfCopies";
                //Commented for case#2268 - Report Tracking Functionality - end
            }else if(colName.equalsIgnoreCase("Overdue #")){
                 fieldName = "overdueCounter";
            }else if(colName.equalsIgnoreCase("Activity Date")){
                 fieldName = "activityDate";
            }else if(colName.equalsIgnoreCase("Comments")){
                 fieldName = "comments";
            }else if(colName.equalsIgnoreCase("Person Name")){
                 fieldName = "fullName";
            }else if(colName.equalsIgnoreCase("Status")){
                 fieldName = "reportStatusDescription";
            }else if(colName.equalsIgnoreCase("Report Type")){
                 fieldName = "reportClassDescription";
            }else if(colName.equalsIgnoreCase("Base Date")){
                 fieldName = "frequencyBase";
            }else if(colName.equalsIgnoreCase("Copy OSP")){
                 fieldName = "ospDistributionDescription";
            }
            //modified for case# 3008 - Principal Investigator misspelt as Principle Investigator - Start
//            else if(colName.equalsIgnoreCase("Principle Investigator")){
//                 fieldName = "principleInvestigator";
             else if(colName.equalsIgnoreCase("Principal Investigator")){
                 fieldName = "principalInvestigator";
            //modified for case# 3008 - Principal Investigator misspelt as Principle Investigator - End
            }else if(colName.equalsIgnoreCase("title")){
                 fieldName = "title";
            }else if(colName.equalsIgnoreCase("Frequency")){
                 fieldName = "frequencyDescription";
            }else if(colName.equalsIgnoreCase("Sponsor Award Number")){
                 fieldName = "sponsorAwardNumber";
            }else if(colName.equalsIgnoreCase("Frequency Base")){
                fieldName = "frequencyBaseDescription";
            }else if(colName.equalsIgnoreCase("Award No.")){
                fieldName = "mitAwardNumber";
            }else if(colName.equalsIgnoreCase("Last Update")){
                fieldName = "updateTimestamp";
            }else if(colName.equalsIgnoreCase("Distribution")){
                 fieldName = "ospDistributionDescription";
            }
            return fieldName;
        }
         
    }// End of ColumnHeaderListener.................
    
    
    
    /**
     * Table model class for the tables
     */
    public class ReportingRequirementsTableModel extends AbstractTableModel {
        private int visColumns[];
        private boolean editableColumns[];
        private CoeusVector cvData;
        private char tableType;
        
        ReportingRequirementsTableModel(char tableType) {
            this.tableType = tableType;
            editableColumns = new boolean[colNames.length];
            visColumns = new int[50];
        }
        
        public int getColumnCount() {
            if (tableType==GROUP_TABLE) {
                return groupVisibleColumns.length;
            } else {
                return detailsVisibleColumns.length;
            }
        }
        /**
         * Returns the number of rows in the table
         */
        public int getRowCount() {
            if (cvData==null) {
                return 0;
            }
            return cvData.size();
        }
        
        /** Returns the name of the column at column.
         * This is used to initialize the table's column header name.
         * @param column the index of the column
         * @return the name of the column
         *
         */
        public String getColumnName(int col) {
            return colNames[col];
        }
        public Class getColumnClass(int column) {
            return colTypes[column];
        }
        /**
         * To set the visible columns
         *
         * public void setVisibleColumns(int columns[]) {
         * visColumns = columns;
         * }*/
        /**
         * To set the editable columns
         */
        public void setEditableColumns(boolean columnValues[]) {
            editableColumns = columnValues;
        }
        /**
         * To set all the columns as non editable
         */
        public void setAllColumnsNonEditable() {
            for (int index=0;index<colNames.length;index++) {
                editableColumns[index] = false;
            }
        }
        /**
         * If cell is editable, returns true, else returns false
         */
        public boolean isCellEditable(int row, int col) {
            return editableColumns[col];
        }
        
        /** Returns the value for the cell at column and row.
         * @param rowIndex the rowIndex whose value is to be queried
         * @param columnIndex the columnIndex whose value is to be queried
         * @return the value Object at the specified cell
         */
        public Object getValueAt(int row, int col) {
            AwardReportReqBean awardReportReqBean =
            (AwardReportReqBean)cvData.get(row);
            int columnIndex = visibleColumns[col];
            return getTableCellValue(awardReportReqBean,columnIndex);
        }
        
        
        private boolean isDiffrentOnNulls(Object comp1,Object comp2) {
            if (comp1 == null && comp2 == null) {
                return false;
            }
            if (comp1 == null && comp2 != null) {
                return true;
            }
            if (comp1 != null && comp2 == null) {
                return true;
            }
            if  (comp1 != null && comp2 != null) {
                return false;
            }
            return false;
        }
        
        public boolean getPersonDetails(String perName) {
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(GET_PERSON_DETAILS);
            requesterBean.setDataObject(perName);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean == null) {
                //Could not contact server.
                CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
                return false;
            }
            if(responderBean.isSuccessfulResponse()) {
                PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)responderBean.getDataObject();
                if(personInfoFormBean.getPersonID().equals("TOO_MANY")) {
                    CoeusOptionPane.showInfoDialog(perName+ " is not a unique name.\n Use find to choose a person.");
                    return false;
                }
                personId = personInfoFormBean.getPersonID();
                personName = personInfoFormBean.getFullName();
                return true;
            } else {
                
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_PERSON));
                return false;
            }
        }
        public void setValueAt(Object value, int row, int column) {
            AwardReportReqBean reportReqBean=
            (AwardReportReqBean)cvData.elementAt(row);
            boolean modified = false;
            String message=null;
            Date date = null;
            String strDate=null;
            switch(column) {
                case REPORT_TYPE_COLUMN:
                    reportReqBean.setReportDescription(value.toString());
                    break;
                case FREQUENCY_COLUMN:
                    reportReqBean.setFrequencyDescription(value.toString());
                    break;
                case FREQUENCY_BASE_COLUMN :
                    reportReqBean.setFrequencyBaseDescription(value.toString());
                    break;
                case BASE_DATE_COLUMN:
                    try{
                        if (value.toString().trim().length() > 0) {
                            strDate = dateUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                        } else {
                            reportReqBean.setFrequencyBase(null);
                            return;
                        }
                        strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = "Invalid date. Please enter a valid base date";
                        CoeusOptionPane.showWarningDialog(message);
                        return ;
                    }
                    catch (CoeusException coeusException) {
                        message = "Invalid date. Please enter a valid base date";
                        CoeusOptionPane.showWarningDialog(message);
                        return ;
                    }
                    reportReqBean.setFrequencyBase(
                    new java.sql.Date(date.getTime()));
                    break;
                case STATUS_COLUMN:
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvStatus.filter(new Equals("description", value.toString())).get(0);
                    String desc = comboBoxBean.getDescription();
                    int typeCode = Integer.parseInt(comboBoxBean.getCode());
                    /*if(isDiffrentOnNulls(reportReqBean.getReportStatusCode()(),desc)) {
                        reportReqBean.setReportStatusCode(typeCode);
                        //ReportStatusDescription(desc);
                        modified = true;
                    }*/
                    //if (reportReqBean.getReportStatusDescription()!=null && !reportReqBean.getReportStatusDescription().equals(desc)) {
                    if(reportReqBean.getReportStatusCode()!=typeCode) {
                        reportReqBean.setReportStatusCode(typeCode);
                        reportReqBean.setReportStatusDescription(desc);
                        //Description(desc);
                        modified = true;
                    }
                    break;
                case DISTRIBUTION_COLUMN:
                    reportReqBean.setOspDistributionDescription(value.toString());
                    break;
                    //Commented for case#2268 - Report Tracking Functionality - start
//                case CONTACT_COLUMN:
//                    reportReqBean.setContactTypeDescription(value.toString());
//                    break;
//                case ADDRESS_COLUMN:
//                    reportReqBean.setAddress(value.toString());
//                    break;
                    //Commented for case#2268 - Report Tracking Functionality - end
                case DUE_DATE_COLUMN:
                    try{
                        if (value.toString().trim().length() > 0) {
                            strDate = dateUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                        } else {
                            reportReqBean.setDueDate(null);
                            return;
                        }
                        strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = "Invalid date. Please enter a valid due date";
                        CoeusOptionPane.showWarningDialog(message);
                        return ;
                    }
                    catch (CoeusException coeusException) {
                        message = "Invalid date. Please enter a valid due date";
                        CoeusOptionPane.showWarningDialog(message);
                        return ;
                    }
                    java.sql.Date dateValue = new java.sql.Date(date.getTime());
                    if (reportReqBean.getDueDate()==null) {
                        reportReqBean.setDueDate(dateValue);
                        modified = true;
                    } else if (!reportReqBean.getDueDate().equals(dateValue)) {
                        reportReqBean.setDueDate(dateValue);
                        modified = true;
                    }
                    break;
//                case COPIES_COLUMN:
//                    reportReqBean.setNumberOfCopies(Integer.parseInt(value.toString()));
//                    modified=true;
//                    break;
                case OVERDUE_COUNTER_COLUMN:
                    if(value.toString().equals(EMPTY_STRING)) {
                        reportReqBean.setOverdueCounter(0);
                        modified=true;
                    } else if (reportReqBean.getOverdueCounter() != Integer.parseInt(value.toString())) {
                        reportReqBean.setOverdueCounter(Integer.parseInt(value.toString()));
                        modified=true;
                    }
                    break;
                case ACTIVITY_DATE_COLUMN:
                    try{
                        if (value.toString().trim().length() > 0) {
                            strDate = dateUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                        } else {
                            reportReqBean.setActivityDate(null);
                            return;
                        }
                        strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = "Invalid date. Please enter a valid activity date";
                        CoeusOptionPane.showWarningDialog(message);
                        return ;
                    }
                    catch (CoeusException coeusException) {
                        message = "Invalid date. Please enter a valid activity date";
                        CoeusOptionPane.showWarningDialog(message);
                        return ;
                    }
                    java.sql.Date activityDateValue = new java.sql.Date(date.getTime());
                    if (reportReqBean.getActivityDate()==null) {
                        reportReqBean.setActivityDate(activityDateValue);
                        modified = true;
                    } else if (!reportReqBean.getActivityDate().equals(activityDateValue)) {
                        reportReqBean.setActivityDate(activityDateValue);
                        modified = true;
                    }
                    break;
                case COMMENTS_COLUMN:
                    if(isDiffrentOnNulls(reportReqBean.getComments(),value)) {
                        reportReqBean.setComments(value.toString());
                        modified = true;
                    }
                    if (reportReqBean.getComments()!=null && !reportReqBean.getReportStatusDescription().equals(value.toString())) {
                        reportReqBean.setComments(value.toString());
                        modified = true;
                    }
                    break;
                case PERSON_COLUMN:
                    boolean isCheckSuccessful = true;
                    int indx = getSelectedDetailItem();
                    if (!value.toString().trim().equals(EMPTY_STRING)) {
                         /* The focus flag is set when the person is searched from the search window and to allow the person with
                         the same person name and different person ids to be entered.Bug Fix:1273*/
                        isCheckSuccessful=authenticatePerson(value.toString() , focus );
                        /* Added for COEUSQA-2794_Specific error message required for Person entry in 
                           Award Reporting Requirements maintenance_start*/
                        if(!isCheckSuccessful){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_PERSON_MSG));
                        }                        
                        /* Added for COEUSQA-2794_Specific error message required for Person entry in 
                           Award Reporting Requirements maintenance_end*/
                        setSelectedDetailItem(indx);
                    } else {
                        if (reportReqBean.getFullName()==null) {
                            reportReqBean.setPersonId(null);
                            reportReqBean.setFullName(null);
                            break;
                        }
                        if (!reportReqBean.getFullName().equals(EMPTY_STRING)) {
                            modified = true;
                        }
                        reportReqBean.setPersonId(null);
                        reportReqBean.setFullName(null);
                        focus = false;
                        break;
                        
                    }
                    
                    if(isDiffrentOnNulls(reportReqBean.getPersonId(),personId)) {
                        if(isCheckSuccessful) {
                            if(focus){
                                reportReqBean.setPersonId(personNumber);
                                reportReqBean.setFullName(perName);
                            }else{
                                reportReqBean.setPersonId(getAuthenticatedPersonId());
                                reportReqBean.setFullName(getAuthenticatedPersonName());
                            }
                            //reportReqBean.setFullName(value.toString());
                            modified = true;
                            focus = false;
                            break;
                        }
                    }
                    if (reportReqBean.getPersonId()!=null && !reportReqBean.getPersonId().equals(value.toString())) {
                        if(isCheckSuccessful) {
                            if(focus){
                                reportReqBean.setPersonId(personNumber);
                                reportReqBean.setFullName(perName);
                            }else{
                                reportReqBean.setPersonId(getAuthenticatedPersonId());
                                reportReqBean.setFullName(getAuthenticatedPersonName());
                            }
                            //reportReqBean.setFullName(value.toString());
                            modified = true;
                            focus = false;
                        }
                    }
                    
                    break;
            }
            if (modified) {
                reportReqBean.setAcType(TypeConstants.UPDATE_RECORD);
                setTableModified(true);
            }
        }
        
        /** sets the data to be displayed by the table.
         * @param cvFundingProposal Data to be displayed.
         */
        public void setData(CoeusVector cvGroupTableData) {
            cvData = cvGroupTableData;
        }
        
    } // Table model class ends
    
    /**
     * Table Renderer
     */
    
    class ReportTableRenderer extends DefaultTableCellRenderer {
        private int visibleColumns[];
        private char tableType;
        ReportTableRenderer(char tableType) {
            this.tableType = tableType;
            visibleColumns = new int[50];
        }
        /**
         * To set the visible columns
         *
         * public void setVisibleColumns(int columns[]) {
         * visibleColumns = columns;
         * }*/
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int columnIndex=0;
            if (tableType==DETAILS_TABLE) {
                columnIndex = detailsVisibleColumns[column];
            } else if (tableType==GROUP_TABLE) {
                columnIndex = groupVisibleColumns[column];
            }
            setHorizontalAlignment(JLabel.LEFT);
            switch(columnIndex) {
                case REPORT_TYPE_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                case FREQUENCY_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                case FREQUENCY_BASE_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                case BASE_DATE_COLUMN:
                    if(value == null || value.toString().equals(EMPTY_STRING)){
                        setText(EMPTY_STRING);
                        return this;
                    }else {
                        String val=null;
                        val= dateUtils.formatDate(value.toString(),REQUIRED_DATEFORMAT);
                        setText(val);
                        return this;
                    }
                case DISTRIBUTION_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                case STATUS_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                    //Commented for case#2268 - Report Tracking Functionality - start
//                case CONTACT_COLUMN:
//                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
//                case ADDRESS_COLUMN:
//                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                    //Commented for case#2268 - Report Tracking Functionality - end
                case DUE_DATE_COLUMN:
                    if(value == null || value.toString().equals(EMPTY_STRING)){
                        setText(EMPTY_STRING);
                        return this;
                    }else{
                        String val = dateUtils.formatDate(value.toString(),REQUIRED_DATEFORMAT);
                        setText(val);
                        return this;
                    }
//                case COPIES_COLUMN:
//                    setHorizontalAlignment(JLabel.RIGHT);
//                    setText(value.toString());
//                    return this;
                case OVERDUE_COUNTER_COLUMN:
                    setHorizontalAlignment(JLabel.RIGHT);
                    setText(value.toString());
                    return this;
                case ACTIVITY_DATE_COLUMN:
                    if(value == null || value.toString().equals(EMPTY_STRING)){
                        setText(EMPTY_STRING);
                        return this;
                    }else{
                        String val = dateUtils.formatDate(value.toString(),REQUIRED_DATEFORMAT);
                        setText(val);
                        return this;
                    }
                case COMMENTS_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                case PERSON_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                case LAST_UPDATE_COLUMN:
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
                default :
                    return null;
            } //switch ends
        } // getTableCellRendererComponent() ends
    } //Table renderer ends here
    
    
    /**
     * Table cell editor
     */
    public class ReportTableCellEditor extends AbstractCellEditor
    implements TableCellEditor,MouseListener {
        private CoeusComboBox cmbStatus;
        private CoeusTextField txtComponent;
        private int column;
        private int visibleColumns[];
        private int columnIndex;
        
        ReportTableCellEditor() {
            visibleColumns = new int[50];
            cmbStatus =new CoeusComboBox();
            txtComponent = new CoeusTextField();
            txtComponent.addMouseListener(this);
        }
        
        private void populateTypeCombo() {
            if (cvStatus==null) {
                return;
            }
            int size = cvStatus.size();
            ComboBoxBean comboBoxBean = new ComboBoxBean();
            if(cmbStatus.getItemCount()>0) {
                return;
            }
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)cvStatus.get(index);
                cmbStatus.addItem(comboBoxBean);
            }
        }
        /**
         * Returns the cell editor componenet
         */
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table,
        Object value, boolean isSelected, int row, int column) {
            txtComponent.setHorizontalAlignment(JLabel.LEFT);
            columnIndex = detailsVisibleColumns[column];
            txtComponent.setDocument(new edu.mit.coeus.utils.LimitedPlainDocument(1000));
            switch(columnIndex) {
                case STATUS_COLUMN:
                    populateTypeCombo();
                    cmbStatus.setSelectedItem(value);
                    return cmbStatus;
                case DUE_DATE_COLUMN:
                        if(value == null || value.toString().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        return txtComponent;
                    }else{
                        String val = dateUtils.formatDate(value.toString(),EDITING_DATEFORMAT);
                        txtComponent.setText(val);
                        return txtComponent;
                    }
                case ACTIVITY_DATE_COLUMN:
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        txtComponent.setText(EMPTY_STRING);
                        return txtComponent;
                    }else{
                    String val = dateUtils.formatDate(value.toString(),EDITING_DATEFORMAT);
                    txtComponent.setText(val);
                    return txtComponent;
                    }
                case OVERDUE_COUNTER_COLUMN:
                    txtComponent.setHorizontalAlignment(JLabel.RIGHT);
                    txtComponent.setDocument(new edu.mit.coeus.utils.JTextFieldFilter(edu.mit.coeus.utils.JTextFieldFilter.NUMERIC,3));
                    if (value == null) {
                        txtComponent.setText(EMPTY_STRING);
                    } else {
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                case COMMENTS_COLUMN:
                    if (value == null) {
                        txtComponent.setText(EMPTY_STRING);
                    } else {
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                case PERSON_COLUMN:
                    if (value == null) {
                        txtComponent.setText(EMPTY_STRING);
                    } else {
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                default:
                    txtComponent.setText(value.toString());
                    return txtComponent;
            }
            
        }
        
        public Object getCellEditorValue() {
            switch(columnIndex) {
                case STATUS_COLUMN:
                    return cmbStatus.getSelectedItem();
                default:
                    return txtComponent.getText();
            }
        }
        
        /**
         * Performs search operation and sets the selected person name to the textbox
         */
        private void searchPerson() {
            try {
                CoeusSearch coeusSearch = new CoeusSearch(
                mdiForm, "PERSONSEARCH", 1);
                coeusSearch.showSearchWindow();
                HashMap personSelected = coeusSearch.getSelectedRow();
                if (personSelected != null && !personSelected.isEmpty() ) {
                    String fullName = Utils.convertNull(personSelected.get(
                    "FULL_NAME"));
                    personName=fullName;
                    perName = fullName;
                    personId=Utils.convertNull(personSelected.get(
                    "PERSON_ID"));
                    personNumber = personId;
                    if (fullName.length() > 0) {
                        txtComponent.setText(Utils.convertNull(fullName));
                        focus = true;
                        stopEditingTable();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void mouseClicked(java.awt.event.MouseEvent me) {
            if (columnIndex != PERSON_COLUMN) {
                return;
            }
            if (me.getClickCount() == 2) {
                try {
                    blockEvents(true);
                    searchPerson();
                } finally{
                    blockEvents(false);
                }
            }
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
}
