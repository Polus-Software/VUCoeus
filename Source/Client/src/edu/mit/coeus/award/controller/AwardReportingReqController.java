/*
 * AwardReportingReqController.java
 *
 * Created on July 29, 2004, 10:10 AM
 * @author   bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 25-SEPTEMBER-2007
 * by Nandkumar S N
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.gui.AwardReportingReqBaseWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.award.gui.CustomizeMaintRepReqForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.award.bean.AwardReportReqBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.award.gui.AwardBaseWindow;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.awt.event.ActionListener;
import java.awt.Component;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.DefaultListSelectionModel;
import java.awt.event.MouseListener;
import java.util.*;

import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CurrentLockForm;
    /* BEGIN Case 2074
     * converting to date 
     from string for sorting
     */
import edu.mit.coeus.utils.DateUtils;
    /* END Case 2074
     * converting to date 
     from string for sorting
     */
    
//import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JTable;



public class AwardReportingReqController extends ReportingRequirementsController
implements ActionListener,MouseListener,VetoableChangeListener {
    private static final char ROLODEX_DISPLAY_MODE='V';
    //private static final char GET_DATA = 'F';
    public static final char HAS_REPORTING_REQUIREMENT = 'E';
    public static final char IS_AWARD_LOCKED = 'I';
    public static final char CORRECT_AWARD = 'M';
    private static final char GET_REPORT_RIGHTS = 'L' ;
    public static final String REP_REQ_SERVLET = "/AwardReportReqMaintenanceServlet";
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    public static final String REP_REQ_NOT_AVAILABLE = "repRequirements_exceptionCode.1054";
    //start add for printReportingReq
    private int selectedRow;
    private static final char PRINT_REPORT_REQ = 'Q';
    //private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    //edn add for printReportingReq
    //private static final String WINDOW_TITLE = "Reporting Requirements";
    
    //Commented/Added for case#2268 - Report Tracking Functionality - start
//    private int visibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22};
    private int visibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19};
//    private boolean boolGroupVisibleColumns [] ={false,true,false,false,true,true,true,true,true,false,
//    false,true,false,false,false,false,false,false,false,false,false,false,false};
    private boolean boolGroupVisibleColumns [] ={false,true,false,false,true,true,true,true,true,false,
    false,true,false,false,false,false,false,false,false,false};    
   
    /* BEGIN Case 2074
     * converting to date 
     from string for sorting
     */
    private DateUtils dtUtils = new DateUtils();
    
    /* End Case 2074
     * converting to date 
     from string for sorting
     */
       
//    private boolean boolDetailsVisibleColumns [] ={true,false,true,true,false,false,false,false,false,true,
//    true,false,true,true,true,true,true,true,true,false,false,false,false};
    private boolean boolDetailsVisibleColumns [] ={true,false,true,true,false,false,false,false,false,true,
    true,false,true,true,true,true,false,false,false,false};        
    
//    private int groupVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22};
    private int groupVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19};
//    private int detailsVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22};
    private int detailsVisibleColumns[] = {0, 1, 2, 3, 4,5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19};
    
    private int view1GroupColumns[] = {1, 4,5,6,7,8,11};
//    private int view1DetailColumns[] = {0,2,3,9,10,12,13,14,15,16,17,18};
    private int view1DetailColumns[] = {0,2,3,9,10,12,13,14,15};
    
//    private int view2GroupColumns[] = {0, 1,20,21,22};
    private int view2GroupColumns[] = {0, 1,17,18,19};
    private int view2DetailColumns[] = {5,6,8,10,11,13,14,15}; 
    
//    private int view3GroupColumns[] = {20};
    private int view3GroupColumns[] = {17};
//    private int view3DetailColumns[] = {9,1,0,10,21,5,22};    
    private int view3DetailColumns[] = {0,1,5,9,10,18,19};
    
//    private int colSize[] = {80, 160, 80, 160, 130,110,160, 160, 80, 90,80,100,170,150,55,80,80,160,140,100,160,160,240};
    private int colSize[] = {80, 160, 80, 160, 130,110,160, 160, 80, 90,80,100,80,80,160,140,100,160,160,240};
    
    private Component cmpForm=null;    
    private String colNames[] =
    //modified for case# 3008 - Principal Investigator misspelt as Principle Investigator - Start
    {"Award No.",/*"Principle Investigator"*/"Principal Investigator","Unit No.","Unit Name","Report Class","Report Type",
     "Frequency","Frequency Base","Base Date","Status","Due Date","Copy OSP",
     /*"Contact","Address","Copies",*/"Overdue #","Activity Date","Comments","Person Name","Sponsor Code","Sponsor Name","Sponsor Award Number","Title"
    };
    //modified for case# 3008 - Principal Investigator misspelt as Principle Investigator - End
    
    private ReportTableRenderer groupTableRenderer;
    private ReportTableRenderer detailsTableRenderer;
    
    private CoeusMessageResources coeusMessageResources;
    
    private int groupColumns[];
    private int detailColumns[];
    /*
     "mitAwardNumber","principleInvestigator","unitNumber","unitName","reportClassDescription",
     "reportDescription","frequencyDescription","frequencyBaseDescription",
     "frequencyBaseDate","reportStatusDescription","stringDueDate","ospDistributionDescription",
     "contactTypeDescription","address","numberOfCopies","overdueCounter",
     "stringActivityDate","comments","personId","sponsorCode","sponsorName","sponsorAwardNumber","title"
     */
    private String columnFields [] = {
        "mitAwardNumber","principleInvestigator","unitNumber","unitName","reportClassDescription",
        "reportDescription","frequencyDescription","frequencyBaseDescription",
        "frequencyBaseDate","reportStatusDescription","stringDueDate","ospDistributionDescription",
        /*"contact","address","numberOfCopies",*/"overdueCounter",
        "stringActivityDate","comments","personId","sponsorCode","sponsorName","sponsorAwardNumber","title"
    };
    
    private static final int AWARD_NUMBER_COLUMN = 0;
    private static final int PI_COLUMN = 1;
    private static final int UNIT_NUMBER_COLUMN = 2;
    private static final int UNIT_NAME_COLUMN = 3;
    private static final int REPORT_CLASS_COLUMN = 4;
    private static final int REPORT_TYPE_COLUMN = 5;
    private static final int FREQUENCY_COLUMN = 6;
    private static final int FREQUENCY_BASE_COLUMN = 7;
    private static final int BASE_DATE_COLUMN = 8;
    private static final int STATUS_COLUMN = 9;
    private static final int DUE_DATE_COLUMN = 10;
    private static final int COPY_OSP_COLUMN = 11;
//    private static final int CONTACT_COLUMN = 12;
//    private static final int ADDRESS_COLUMN = 13;
//    private static final int COPIES_COLUMN = 14;
    private static final int OVERDUE_COLUMN = 12;
    private static final int ACTIVITY_DATE_COLUMN = 13;
    private static final int COMMENTS_COLUMN = 14;
    private static final int PERSON_COLUMN = 15;
    private static final int SPONSOR_CODE_COLUMN = 16;
    private static final int SPONSOR_NAME_COLUMN = 17;
    private static final int SPONSOR_AWARD_NUMBER_COLUMN = 18;
    private static final int TITLE_COLUMN = 19;
    //Commented/Added for case#2268 - Report Tracking Functionality - end
    
    private AwardReportingReqBaseWindow awardReportingReqBaseWindow;
    private CoeusAppletMDIForm mdiForm;
    private CustomizeMaintRepReqForm customizeMaintRepReqForm;
    private CoeusSearch coeusSearch;
    //private ReportingRequirementsController  reportingRequirementsController;
    private CoeusVector cvData;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    private boolean hasCreateRight = false;
    private boolean hasModifyRight = false;
    private boolean hasViewRight = false;
    private boolean hasMaintainRepRight = false;
    
    private boolean closed = false;
    
    private ChangePassword changePassword;
    
    //Code added for Case#3388 - Implementing authorization check at department level
    private String awardNumber;
    
//    Added for case 4308 - Due date not displayed in award reporting - Start
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SEARCH_RESULT_FORMAT = "yyyy/MM/dd";
//    Added for case 4308 - Due date not displayed in award reporting - End
    
    /** Creates a new instance of AwardReportingReqController */
    public AwardReportingReqController() {
        super();
        //Case #2074 - start
        super.setParent(true);
        //Case #2074 - End
        groupTableColumns = new TableColumn[colNames.length];
        detailsTableColumns = new TableColumn[colNames.length];
        setArrays();
        initComponents();
        registerComponents();
    }
    
    public void initComponents() {
        super.initComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
        groupTableRenderer = new ReportTableRenderer('G');
        detailsTableRenderer = new ReportTableRenderer('D');
        mdiForm = CoeusGuiConstants.getMDIForm();
        awardReportingReqBaseWindow = new AwardReportingReqBaseWindow("Reporting Requirements",mdiForm);
    }
    
    /**
     * Registers the listeners
     */
    public void registerComponents() {
        awardReportingReqBaseWindow.btnClose.addActionListener(this);
        awardReportingReqBaseWindow.btnCustomizeView.addActionListener(this);
        awardReportingReqBaseWindow.btnPrintAll.addActionListener(this);
        awardReportingReqBaseWindow.btnPrintSelected.addActionListener(this);
        awardReportingReqBaseWindow.btnSaveAs.addActionListener(this);
        awardReportingReqBaseWindow.btnSearch.addActionListener(this);
        awardReportingReqBaseWindow.btnView1.addActionListener(this);
        awardReportingReqBaseWindow.btnView2.addActionListener(this);
        awardReportingReqBaseWindow.btnView3.addActionListener(this);
        
        awardReportingReqBaseWindow.mnuItmChangePassword.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmClose.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmCustomize.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmDisplayAward.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmExit.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmInbox.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        awardReportingReqBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        awardReportingReqBaseWindow.mnuItmPreferences.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmPrintSetup.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmReportingReqAward.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmSave.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmView1.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmView2.addActionListener(this);
        awardReportingReqBaseWindow.mnuItmView3.addActionListener(this);
        /*Case 2110 Start*/
        awardReportingReqBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        /*Case 2110 End*/
        reportingRequirementsForm.tblDetails.addMouseListener(this);
        super.registerComponents();
        awardReportingReqBaseWindow.addVetoableChangeListener(this);
        super.setColumnData();
        groupVisibleColumns = super.getGroupVisibleColumns();
        detailsVisibleColumns = super.getDetailsVisibleColumns();
    }
    
    
    
    public void setFormData(Object data) {
        //super.setFormData(data);
    }
    
    public void columnRenderers() {
        TableColumn tableColumn = null;
        //For group table
        JTableHeader tableHeader = reportingRequirementsForm.tblGroup.getTableHeader();
        tableHeader.setReorderingAllowed(true);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        //tableHeader.addMouseListener(new GroupColumnHeaderListener());
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
        tableHeader = reportingRequirementsForm.tblDetails.getTableHeader();
        tableHeader.setReorderingAllowed(true);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        //tableHeader.addMouseListener(new DetailColumnHeaderListener());
        reportingRequirementsForm.tblDetails.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_OFF);
        reportingRequirementsForm.tblDetails.setRowHeight(18);
        reportingRequirementsForm.tblDetails.setShowHorizontalLines(true);
        reportingRequirementsForm.tblDetails.setShowVerticalLines(true);
        reportingRequirementsForm.tblDetails.setOpaque(false);
        reportingRequirementsForm.scrpnDetails.getViewport().setBackground(java.awt.Color.white);
        reportingRequirementsForm.tblDetails.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        tableColumn = null;
        for(int col = 0; col < detailsVisibleColumns.length; col++) {
            tableColumn = reportingRequirementsForm.tblDetails.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[visibleColumns[col]]);
            tableColumn.setCellRenderer(detailsTableRenderer);
            detailsTableColumns[col] = tableColumn;
        }
    }
    
    /**
     *  Displays the window
     */
    public void display() {
        try {
            mdiForm.putFrame(CoeusGuiConstants.AWARD_REPORTING_REQ, awardReportingReqBaseWindow);
            // javax.swing.JPanel basePanel = new javax.swing.JPanel();
            //basePanel.setLayout(new java.awt.BorderLayout());
            //basePanel.add(tbdPnReports);
            mdiForm.getDeskTopPane().add(awardReportingReqBaseWindow);
            awardReportingReqBaseWindow.setSelected(true);
            awardReportingReqBaseWindow.setVisible(true);
            cmpForm = super.getControlledUI();
            awardReportingReqBaseWindow.getContentPane().add(cmpForm);
            showSearchWindow();
            
        } catch(PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    /*
     * This method overrides the super class method.
     */ 
    public void setCurrentDetailData(int currentRow) {
        if (cvTableData==null || cvTableData.size()<1) {
            return;
        }
        if (currentRow ==-1) {
            cvCurrentDetailsData = cvTableData;
            detailsTableModel.setData(cvCurrentDetailsData);
            detailsTableModel.fireTableDataChanged();
            if (cvCurrentDetailsData.size()> 0) {
                super.setDefaultRowSelection();
            }
            return;
        }
        AwardReportReqBean currentBean = (AwardReportReqBean)cvGrpTableData.get(currentRow);
        Equals equalsAwardNo,equalsPrincipleInvestigator,equalsUnitNo,
        equalsUnitName,equalsReportClass,equalsReportType,equalsFrequency,
        equalsFrequencyBase,equalsBaseDate,equalsStatus,equalsDueDate,equalsCopyOSP,
        equalsContact,equalsAddress,equalsCopies,equalsOverdueCounter,equalsActivityDate,equalsComments,equalsPerson,
        equalsSponsorCode,equalsSponsorName,equalsSponsorAwNum,equalsTitle;
        Equals equalsArray[]= new Equals[colNames.length];
        And and;
        for (int index = 0;index < this.groupVisibleColumns.length;index++) {
            int currentColIndex = groupVisibleColumns[index];
            String columnFieldName = columnFields[groupVisibleColumns[index]];
            switch (currentColIndex) {
                case AWARD_NUMBER_COLUMN:
                    equalsAwardNo = new Equals(columnFieldName,currentBean.getMitAwardNumber());
                    equalsArray [index] = equalsAwardNo;
                    break;
                case PI_COLUMN:
                    equalsPrincipleInvestigator = new Equals(columnFieldName,currentBean.getPrincipleInvestigator());
                    equalsArray [index] = equalsPrincipleInvestigator;
                    break;
                case UNIT_NUMBER_COLUMN:
                    equalsUnitNo = new Equals(columnFieldName,currentBean.getUnitNumber());
                    equalsArray [index] = equalsUnitNo;
                    break;
                case UNIT_NAME_COLUMN:
                    equalsUnitName = new Equals(columnFieldName,currentBean.getUnitName());
                    equalsArray [index] = equalsUnitName;
                    break;
                case REPORT_CLASS_COLUMN:
                    equalsReportClass  = new Equals(columnFieldName,currentBean.getReportClassDescription());
                    equalsArray [index] = equalsReportClass;
                    break;
                case REPORT_TYPE_COLUMN:
                    equalsReportType  = new Equals(columnFieldName,currentBean.getReportDescription());
                    equalsArray [index] = equalsReportType;
                    break;
                case FREQUENCY_COLUMN:
                    equalsFrequency = new Equals(columnFieldName,currentBean.getFrequencyDescription());
                    equalsArray [index] = equalsFrequency;
                    break;
                case FREQUENCY_BASE_COLUMN:
                    equalsFrequencyBase = new Equals(columnFieldName,currentBean.getFrequencyBaseDescription());
                    equalsArray [index] = equalsFrequencyBase;
                    break;
                case BASE_DATE_COLUMN:
                    equalsBaseDate = new Equals(columnFieldName,currentBean.getFrequencyBaseDate());
                    equalsArray [index] = equalsBaseDate;
                    break;
                case STATUS_COLUMN:
                    equalsStatus = new Equals(columnFieldName,currentBean.getReportStatusDescription());
                    equalsArray [index] = equalsStatus;
                    break;
                case DUE_DATE_COLUMN:
                       /* BEGIN Case 2074
                        * converting to date 
                        *from string for sorting
                        */
                    Object value  = currentBean.getStringDueDate();
                    if(value!= null){
                        value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                    }else{
                        value = EMPTY;
                    }
                    equalsDueDate = new Equals(columnFieldName,value.toString());
                    equalsArray [index] = equalsDueDate;
                    
                       /* END Case 2074
                        * converting to date 
                        *from string for sorting
                        */

                    break;
                case COPY_OSP_COLUMN:
                    equalsCopyOSP = new Equals(columnFieldName,currentBean.getOspDistributionDescription());
                    equalsArray [index] = equalsCopyOSP;
                    break;
                    //Commented for case#2268 - Report Tracking Functionality - start
//                case CONTACT_COLUMN:
//                    equalsContact = new Equals(columnFieldName,currentBean.getContact());
//                    equalsArray [index] = equalsContact;
//                    break;
//                case ADDRESS_COLUMN:
//                    equalsAddress = new Equals(columnFieldName,currentBean.getAddress());
//                    equalsArray [index] = equalsAddress;
//                    break;
//                case COPIES_COLUMN:
//                    equalsCopies = new Equals(columnFieldName,new Integer(currentBean.getNumberOfCopies()));
//                    equalsArray [index] = equalsCopies;
//                    break;
                    //Commented for case#2268 - Report Tracking Functionality - end
                case OVERDUE_COLUMN:
                    equalsOverdueCounter = new Equals(columnFieldName,new Integer(currentBean.getOverdueCounter()));
                    equalsArray [index] = equalsOverdueCounter;
                    break;
                case ACTIVITY_DATE_COLUMN:
                    //equalsActivityDate = new Equals(columnFieldName,currentBean.getStringActivityDate());
                    //equalsArray [index] = equalsActivityDate;
                      /* BEGIN Case 2074
                        * converting to date 
                        *from string for sorting
                        */
                    value  = currentBean.getStringActivityDate();
                    if(value!= null){
                        value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                    }else{
                        value = EMPTY;
                    }
                        equalsActivityDate = new Equals(columnFieldName,value.toString());
                        equalsArray [index] = equalsActivityDate;
                    
                       /* END Case 2074
                        * converting to date 
                        *from string for sorting
                        */
                    break;
                case COMMENTS_COLUMN:
                    equalsComments = new Equals(columnFieldName,currentBean.getComments());
                    equalsArray [index] = equalsComments;
                    break;
                case PERSON_COLUMN:
                    equalsPerson = new Equals(columnFieldName,currentBean.getPersonId());
                    equalsArray [index] = equalsPerson;
                    break;
                case SPONSOR_CODE_COLUMN:
                    equalsSponsorCode = new Equals(columnFieldName,currentBean.getSponsorCode());
                    equalsArray [index] = equalsSponsorCode;
                    break;
                case SPONSOR_NAME_COLUMN:
                    equalsSponsorName = new Equals(columnFieldName,currentBean.getSponsorName());
                    equalsArray [index] = equalsSponsorName;
                    break;
                case SPONSOR_AWARD_NUMBER_COLUMN:
                    equalsSponsorAwNum = new Equals(columnFieldName,currentBean.getSponsorAwardNumber());
                    equalsArray [index] = equalsSponsorAwNum;
                    break;
                case TITLE_COLUMN:
                    equalsTitle = new Equals(columnFieldName,currentBean.getTitle());
                    equalsArray [index] = equalsTitle;
                    break;
            } //switch ends
            
        } //For loop ends 
        if (groupVisibleColumns.length == 0) { // BugFix 1211
            cvCurrentDetailsData = cvTableData;
        } else if (groupVisibleColumns.length == 1) {
            cvCurrentDetailsData = cvTableData.filter(equalsArray[0]);
        } else if (groupVisibleColumns.length > 1) {
            and =  new And(equalsArray[0],equalsArray[1]);
            for (int indx = 2; indx<groupVisibleColumns.length;indx++) {
                and = new And(and,equalsArray[indx]);
            }
            
            //Ajay: Removed unwanted loop Start
            /*for (int indx =0;indx<cvTableData.size();indx++) {
                AwardReportReqBean bn = (AwardReportReqBean)cvTableData.get(indx);
            }*/
            //Ajay: Removed unwanted loop End
                cvCurrentDetailsData = cvTableData.filter(and);
        }
        detailsTableModel.setData(cvCurrentDetailsData);
        detailsTableModel.fireTableDataChanged();
        if (cvCurrentDetailsData.size()> 0) {
            super.setDefaultRowSelection();
        }
        
    }
    /*
     * This method overrides its super class method
     */
    protected CoeusVector getGroupedVector() {
        CoeusVector cvGroupData = new CoeusVector();
        if (cvTableData==null ) {
            return new CoeusVector();
        } else if (cvTableData.size()<1) {
            return new CoeusVector();
        }
        cvGroupData.add(cvTableData.get(0));
        for (int idx=1;idx<cvTableData.size();idx++) {
            AwardReportReqBean currentBean = (AwardReportReqBean)cvTableData.get(idx);
            Equals equalsAwardNo,equalsPrincipleInvestigator,equalsUnitNo,
            equalsUnitName,equalsReportClass,equalsReportType,equalsFrequency,
            equalsFrequencyBase,equalsBaseDate,equalsStatus,equalsDueDate,equalsCopyOSP,
            equalsContact,equalsAddress,equalsCopies,equalsOverdueCounter,equalsActivityDate,equalsComments,equalsPerson,
            equalsSponsorCode,equalsSponsorName,equalsSponsorAwNum,equalsTitle;
            Equals equalsArray[]= new Equals[colNames.length];
            And and;
            for (int index = 0;index < groupVisibleColumns.length;index++) {
                int currentColIndex = groupVisibleColumns[index];
                String columnFieldName = columnFields[currentColIndex];
                switch (currentColIndex) {
                    case AWARD_NUMBER_COLUMN:
                        equalsAwardNo = new Equals(columnFieldName,currentBean.getMitAwardNumber());
                        equalsArray [index] = equalsAwardNo;
                        break;
                    case PI_COLUMN:
                        equalsPrincipleInvestigator = new Equals(columnFieldName,currentBean.getPrincipleInvestigator());
                        equalsArray [index] = equalsPrincipleInvestigator;
                        break;
                    case UNIT_NUMBER_COLUMN:
                        equalsUnitNo = new Equals(columnFieldName,currentBean.getUnitNumber());
                        equalsArray [index] = equalsUnitNo;
                        break;
                    case UNIT_NAME_COLUMN:
                        equalsUnitName = new Equals(columnFieldName,currentBean.getUnitName());
                        equalsArray [index] = equalsUnitName;
                        break;
                    case REPORT_CLASS_COLUMN:
                        equalsReportClass  = new Equals(columnFieldName,currentBean.getReportClassDescription());
                        equalsArray [index] = equalsReportClass;
                        break;
                    case REPORT_TYPE_COLUMN:
                        equalsReportType  = new Equals(columnFieldName,currentBean.getReportDescription());
                        equalsArray [index] = equalsReportType;
                        break;
                    case FREQUENCY_COLUMN:
                        equalsFrequency = new Equals(columnFieldName,currentBean.getFrequencyDescription());
                        equalsArray [index] = equalsFrequency;
                        break;
                    case FREQUENCY_BASE_COLUMN:
                        equalsFrequencyBase = new Equals(columnFieldName,currentBean.getFrequencyBaseDescription());
                        equalsArray [index] = equalsFrequencyBase;
                        break;
                    case BASE_DATE_COLUMN:
                        equalsBaseDate = new Equals(columnFieldName,currentBean.getFrequencyBaseDate());
                        equalsArray [index] = equalsBaseDate;
                        break;
                    case STATUS_COLUMN:
                        equalsStatus = new Equals(columnFieldName,currentBean.getReportStatusDescription());
                        equalsArray [index] = equalsStatus;
                        break;
                    case DUE_DATE_COLUMN:
                        /* BEGIN Case 2074
                        * converting to date 
                        *from string for sorting
                        */
                        Object value  = currentBean.getStringDueDate();
			if(value != null){
                            value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                        }else{
                            value = EMPTY;
                        }
                            equalsDueDate = new Equals(columnFieldName,(String)value);
                            equalsArray [index] = equalsDueDate;
                        /* END Case 2074
                        * converting to date 
                        *from string for sorting
                        */
                    
                        break;
                    case COPY_OSP_COLUMN:
                        equalsCopyOSP = new Equals(columnFieldName,currentBean.getOspDistributionDescription());
                        equalsArray [index] = equalsCopyOSP;
                        break;
                        //Commented for case#2268 - Report Tracking Functionality - start
//                    case CONTACT_COLUMN:
//                        equalsContact = new Equals(columnFieldName,currentBean.getContact());
//                        equalsArray [index] = equalsContact;
//                        break;
//                    case ADDRESS_COLUMN:
//                        equalsAddress = new Equals(columnFieldName,currentBean.getAddress());
//                        equalsArray [index] = equalsAddress;
//                        break;
//                    case COPIES_COLUMN:
//                        equalsCopies = new Equals(columnFieldName,new Integer(currentBean.getNumberOfCopies()));
//                        equalsArray [index] = equalsCopies;
//                        break;
                        //Commented for case#2268 - Report Tracking Functionality - end
                    case OVERDUE_COLUMN:
                        equalsOverdueCounter = new Equals(columnFieldName,new Integer(currentBean.getOverdueCounter()));
                        equalsArray [index] = equalsOverdueCounter;
                        break;
                    case ACTIVITY_DATE_COLUMN:
                        /* BEGIN Case 2074
                        * converting to date 
                        *from string for sorting
                        */
                         value  = currentBean.getStringActivityDate();
                         if(value!= null){
                            value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                         }else{
                             value = EMPTY;
                         }
                            equalsActivityDate = new Equals(columnFieldName,(String)value);
                            equalsArray [index] = equalsActivityDate;
                       /* END Case 2074
                        * converting to date 
                        *from string for sorting
                        */
                        //equalsActivityDate = new Equals(columnFieldName,currentBean.getStringActivityDate());
                        //equalsArray [index] = equalsActivityDate;
                        break;
                    case COMMENTS_COLUMN:
                        equalsComments = new Equals(columnFieldName,currentBean.getComments());
                        equalsArray [index] = equalsComments;
                        break;
                    case PERSON_COLUMN:
                        equalsPerson = new Equals(columnFieldName,currentBean.getPersonId());
                        equalsArray [index] = equalsPerson;
                        break;
                    case SPONSOR_CODE_COLUMN:
                        equalsSponsorCode = new Equals(columnFieldName,currentBean.getSponsorCode());
                        equalsArray [index] = equalsSponsorCode;
                        break;
                    case SPONSOR_NAME_COLUMN:
                        equalsSponsorName = new Equals(columnFieldName,currentBean.getSponsorName());
                        equalsArray [index] = equalsSponsorName;
                        break;
                    case SPONSOR_AWARD_NUMBER_COLUMN:
                        equalsSponsorAwNum = new Equals(columnFieldName,currentBean.getSponsorAwardNumber());
                        equalsArray [index] = equalsSponsorAwNum;
                        break;
                    case TITLE_COLUMN:
                        equalsTitle = new Equals(columnFieldName,currentBean.getTitle());
                        equalsArray [index] = equalsTitle;
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
                if(and!= null ){
                    cv = cvGroupData.filter(and);
                }
            }
            if(cv!=null && cv.size()==0) {
                cvGroupData.add(currentBean);
            }
        } //Outer For loop ends
        return cvGroupData;
    }
    
    /**
     * This method returns the cell values for the table.
     * This method is being called from the table model's getvalueAt()
     * This method is an overridden method from the super class
     */
    protected Object getTableCellValue(AwardReportReqBean awardReportReqBean,int columnIndex) {
        switch(columnIndex) {
            case AWARD_NUMBER_COLUMN:
                return awardReportReqBean.getMitAwardNumber();
            case PI_COLUMN:
                return awardReportReqBean.getPrincipleInvestigator();
            case UNIT_NUMBER_COLUMN:
                return awardReportReqBean.getUnitNumber();
            case UNIT_NAME_COLUMN:
                return awardReportReqBean.getUnitName();
            case REPORT_CLASS_COLUMN:
                return awardReportReqBean.getReportClassDescription();
            case REPORT_TYPE_COLUMN:
                return awardReportReqBean.getReportDescription();
            case FREQUENCY_COLUMN:
                return awardReportReqBean.getFrequencyDescription();
            case FREQUENCY_BASE_COLUMN:
                return awardReportReqBean.getFrequencyBaseDescription();
            case BASE_DATE_COLUMN:
//                Case 4308:Due date not displayed in award reporting - Start
//                return awardReportReqBean.getFrequencyBaseDate();
                Object freqBaseDate  = awardReportReqBean.getFrequencyBase();
                if(freqBaseDate!= null){
                    freqBaseDate = dtUtils.formatDate(freqBaseDate.toString(),REQUIRED_DATEFORMAT);
                }else{
                    freqBaseDate = EMPTY;
                }
                return freqBaseDate;
//                Case 4308:Due date not displayed in award reporting - End
            case STATUS_COLUMN:
                return awardReportReqBean.getReportStatusDescription();
            case DUE_DATE_COLUMN:
                /* BEGIN: Case 2074 */
                    Object value  = awardReportReqBean.getStringDueDate();
                    if(value!= null){
                        value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                    }else{
                        value = EMPTY;
                    }
                return value;
                //return awardReportReqBean.getStringDueDate();
                /* END: Case 2074 */
            case COPY_OSP_COLUMN:
                return awardReportReqBean.getOspDistributionDescription();
                //Commented for case#2268 - Report Tracking Functionality - start
//            case CONTACT_COLUMN:
//                return awardReportReqBean.getContact();
//            case ADDRESS_COLUMN:
//                return awardReportReqBean.getAddress();
//            case COPIES_COLUMN:
//                return new Integer(awardReportReqBean.getNumberOfCopies());
                //Commented for case#2268 - Report Tracking Functionality - end
            case OVERDUE_COLUMN:
                return new Integer(awardReportReqBean.getOverdueCounter());
            case ACTIVITY_DATE_COLUMN:
                /* BEGIN: Case 2074 */
                     value  = awardReportReqBean.getStringActivityDate();
                    if(value!= null){
                        value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                    }else{
                        value = EMPTY;
                    }
                return value;
                //return awardReportReqBean.getStringActivityDate();
                /* END: Case 2074 */
            case COMMENTS_COLUMN:
                return awardReportReqBean.getComments();
            case PERSON_COLUMN:
                return awardReportReqBean.getFullName();
            case SPONSOR_CODE_COLUMN:
                return awardReportReqBean.getSponsorCode();
            case SPONSOR_NAME_COLUMN:
                return awardReportReqBean.getSponsorName();
            case SPONSOR_AWARD_NUMBER_COLUMN:
                return awardReportReqBean.getSponsorAwardNumber();
            case TITLE_COLUMN:
                return awardReportReqBean.getTitle();
            default :
                return null;
        }
    }
    
    /**
     * sets the visible column arrays and fields
     */
    private void setArrays() {
        super.setBoolDetailsVisibleColumns(boolDetailsVisibleColumns);
        super.setBoolGroupVisibleColumns(boolGroupVisibleColumns);
        super.setDetailsVisibleColumns(detailsVisibleColumns);
        super.setGroupVisibleColumns(groupVisibleColumns);
        super.setVisibleColumns(visibleColumns);
        super.setColSize(colSize);
        super.setColNames(colNames);
        super.setColumnFields(columnFields);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            Object source = actionEvent.getSource();
            blockEvents(true); // changed for hour glass
            if (source.equals(awardReportingReqBaseWindow.btnClose) || source.equals(awardReportingReqBaseWindow.mnuItmClose) ) {
                closeWindow();
            } else if(source.equals(awardReportingReqBaseWindow.btnCustomizeView) || source.equals(awardReportingReqBaseWindow.mnuItmCustomize)) {
                showCustomizedView();
                awardReportingReqBaseWindow.mnuItmView1.setSelected(false);
                awardReportingReqBaseWindow.mnuItmView2.setSelected(false);
                awardReportingReqBaseWindow.mnuItmView3.setSelected(false);
                //Commented for case#2268 - Report Tracking Functionality - start
//                awardReportingReqBaseWindow.btnPrintAll.setEnabled(false);
//                awardReportingReqBaseWindow.btnPrintSelected.setEnabled(false);
                //Commented for case#2268 - Report Tracking Functionality - end
                //Bug Fix 1933: Start 1
                if(cvGrpTableData.size() > 0){
                    reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
                }
                //Bug Fix 1933: End 1
            } else if (source.equals(awardReportingReqBaseWindow.btnView1) || source.equals(awardReportingReqBaseWindow.mnuItmView1)) {
                awardReportingReqBaseWindow.mnuItmView1.setSelected(true);
                awardReportingReqBaseWindow.mnuItmView2.setSelected(false);
                awardReportingReqBaseWindow.mnuItmView3.setSelected(false);
                awardReportingReqBaseWindow.btnPrintAll.setEnabled(true);
                awardReportingReqBaseWindow.btnPrintSelected.setEnabled(true);
                customizeView(view1GroupColumns, view1DetailColumns);
                //detailsVisibleColumns= super.getDetailsVisibleColumns();
                //groupVisibleColumns= super.getGroupVisibleColumns();
                sortGroupTable();
                sortDetailsTable();
                
                //Bug Fix 1933: Start 2
                if(cvGrpTableData.size() > 0){
                    reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
                }
                //Bug Fix 1933: End 2
            } else if (source.equals(awardReportingReqBaseWindow.btnView2) || source.equals(awardReportingReqBaseWindow.mnuItmView2)) {
                awardReportingReqBaseWindow.mnuItmView1.setSelected(false);
                awardReportingReqBaseWindow.mnuItmView2.setSelected(true);
                awardReportingReqBaseWindow.mnuItmView3.setSelected(false);
                awardReportingReqBaseWindow.btnPrintAll.setEnabled(false);
                awardReportingReqBaseWindow.btnPrintSelected.setEnabled(false);
                customizeView(view2GroupColumns, view2DetailColumns);
                //detailsVisibleColumns= super.getDetailsVisibleColumns();
                //groupVisibleColumns= super.getGroupVisibleColumns();
                sortGroupTable();
                sortDetailsTable();
                
                //Bug Fix 1933: Start 3
                if(cvGrpTableData.size() > 0){
                    reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
                }
                //Bug Fix 1933: End 3
            } else if (source.equals(awardReportingReqBaseWindow.btnView3) || source.equals(awardReportingReqBaseWindow.mnuItmView3)) {
                awardReportingReqBaseWindow.mnuItmView1.setSelected(false);
                awardReportingReqBaseWindow.mnuItmView2.setSelected(false);
                awardReportingReqBaseWindow.mnuItmView3.setSelected(true);
                awardReportingReqBaseWindow.btnPrintAll.setEnabled(false);
                awardReportingReqBaseWindow.btnPrintSelected.setEnabled(false);
                customizeView(view3GroupColumns, view3DetailColumns);
                //detailsVisibleColumns= super.getDetailsVisibleColumns();
                //groupVisibleColumns= super.getGroupVisibleColumns();                
                super.removeGroupListener();
                movingColumns(view3GroupColumns,groupVisibleColumns,reportingRequirementsForm.tblGroup);
                groupVisibleColumns = view3GroupColumns;
                super.addGroupListener();
                super.removeDetailsListener();
                movingColumns(view3DetailColumns,detailsVisibleColumns,reportingRequirementsForm.tblDetails);
                detailsVisibleColumns = view3DetailColumns;
                super.addDetailsListener();                
                sortGroupTable();
                sortDetailsTable();
                
                //Bug Fix 1933: Start 4
                if(cvGrpTableData.size() > 0){
                    reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
                }
                //Bug Fix 1933: End 4
            } else if (source.equals(awardReportingReqBaseWindow.btnSearch)) {
                showSearchWindow();
            }  else if(source.equals(awardReportingReqBaseWindow.mnuItmDisplayAward)) {
                displayAward();
            } else if (source.equals(awardReportingReqBaseWindow.mnuItmReportingReqAward)) {
                String awardNum=getSelectedAwardNumber();
                if (awardNum!=null) {
                    showReportingReqmts(awardNum);
                }
            }   else if(source.equals(awardReportingReqBaseWindow.mnuItmChangePassword)) {
                showChangePassword();
            //Added for Case#3682 - Enhancements related to Delegations - Start 
             } else if(source.equals(awardReportingReqBaseWindow.mnuItmDelegations)) {
                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            } else if(source.equals(awardReportingReqBaseWindow.mnuItmPreferences)) {
                showPreference();
            //start added for print
            }else if (source.equals(awardReportingReqBaseWindow.btnPrintAll)){
                selectedRow = -1; 
                PrintPeportingReq();
            }else if (source.equals(awardReportingReqBaseWindow.btnPrintSelected)){
                selectedRow = reportingRequirementsForm.tblGroup.getSelectedRow(); 
                PrintPeportingReq();
            //end added for print
            }else if (source.equals(awardReportingReqBaseWindow.mnuItmSave) || source.equals(awardReportingReqBaseWindow.btnSaveAs)) {
                if (reportingRequirementsForm.tblDetails.getRowCount()>0) {
                    cvTableData.sort("mitAwardNumber",true);
                    detailsTableModel.setData(cvTableData);
                    detailsTableModel.fireTableDataChanged();
                    reportingRequirementsForm.tblDetails.setRowSelectionInterval(0, reportingRequirementsForm.tblDetails.getRowCount()-1);                    
                    // bug fix 2101 - start
                    detailsTableModel.setData(cvCurrentDetailsData);
                    detailsTableModel.fireTableDataChanged();
                    // bug fix 2101 - End
                }
                //Code modified for PT ID#2382 - Save As functionality
                //Modified for Case#2908-Exports from Search Results Do Not Preserve Data Format - Start
                //SaveAsDialog saveAsDialog = new SaveAsDialog(reportingRequirementsForm.tblDetails);
                JTable tblResults = coeusSearch.getSearchResTable();
                if(tblResults == null){
                    tblResults = coeusSearch.getEmptyResTable();
                }
                SaveAsDialog saveAsDialog = new SaveAsDialog(tblResults);
                //Case#2908 - End
            //start of bug fix id 1651
            } else if(source.equals(awardReportingReqBaseWindow.mnuItmExit)) {
                exitApplication();
            }//end of bug fix id 1651
            //Case 2110 Start
            else if(source.equals(awardReportingReqBaseWindow.mnuItmCurrentLocks)){
               showLocksForm(); 
            }//Case 2110 End
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            blockEvents(false); // changed for hour glass
        }
        
    }
    
    //Added by shiji for bug fix id 1651
     public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
                                    "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
     }//End bug fix id 1651
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Displays Delegations window
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
    
    
     // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    //Case 2110 Start To get the curentLocks of the user
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    } //Case 2110 End    
    
     
    //start add for print
    private void PrintPeportingReq()throws CoeusClientException {
        Hashtable htPrintParams = new Hashtable();        
        htPrintParams.put("SEL_ROW",""+selectedRow);
        htPrintParams.put("GRP",cvGrpTableData);
        htPrintParams.put("DATA",cvTableData);
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PRINT_REPORT_REQ);
        requester.setDataObject(htPrintParams);
        
        //For Streaming
        String printType = (String)htPrintParams.get("PRINT_TYPE");
        requester.setId("Award/ReportingRequirements");
        requester.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
        comm.send();
        ResponderBean responder = comm.getResponse();
        String fileName = "";
        if(responder.isSuccessfulResponse()){
             //AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
             
           
             fileName = (String)responder.getDataObject();
             //System.out.println("Report Filename is=>"+fileName);
             
             fileName.replace('\\', '/') ; // this is fix for Mac
             URL reportUrl = null;
             try{
                reportUrl = new URL( fileName );
             
             
             /*if (coeusContxt != null) {
                 coeusContxt.showDocument( reportUrl, "_blank" );
             }else {
                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                 bs.showDocument( reportUrl );
             }*/
                URLOpener.openUrl(reportUrl);
             }catch(MalformedURLException muEx){
                 throw new CoeusClientException(muEx.getMessage());
             }catch(Exception uaEx){
                 throw new CoeusClientException(uaEx.getMessage());
             }
             
         }else{
             throw new CoeusClientException(responder.getMessage());
         }              
  
    }
    //end add for print
    
    /*
     * To get the selected Award number
     */
    public String getSelectedAwardNumber() {
        int selRow = reportingRequirementsForm.tblDetails.getSelectedRow();
        if (selRow==-1) {
            return null;
        }
        AwardReportReqBean bean = (AwardReportReqBean)cvCurrentDetailsData.get(selRow);
        String awardNum = bean.getMitAwardNumber();
        return awardNum;
    }
    
    /**
     * To display the award
     */
    private void displayAward() {
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award
        int selRow = reportingRequirementsForm.tblDetails.getSelectedRow();
        String awardNum=null;
        if (selRow==-1) {
            return;
        }
        AwardReportReqBean bean = (AwardReportReqBean)cvCurrentDetailsData.get(selRow);
        awardNum = bean.getMitAwardNumber();
        this.awardNumber = awardNum;
        try {
            getRights();
        } catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
        if (!(hasCreateRight || hasModifyRight || hasViewRight )) {
            // You do not have rights to view Awards
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
            return;
        }
        //Code commented for Case#3388 - Implementing authorization check at department level - starts
//        int selRow = reportingRequirementsForm.tblDetails.getSelectedRow();
//        String awardNum=null;
//        if (selRow==-1) {
//            return;
//        }
//        AwardReportReqBean bean = (AwardReportReqBean)cvCurrentDetailsData.get(selRow);
//        awardNum = bean.getMitAwardNumber();
        //Code commented for Case#3388 - Implementing authorization check at department level - ends
        if(isAwardWindowOpen(awardNum, TypeConstants.DISPLAY_MODE,hasMaintainRepRight)) {
            return;
        }
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNum);
        AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Display Award : ", TypeConstants.DISPLAY_MODE , awardBean);
        awardBaseWindowController.setMaintainReporting(true);
        awardBaseWindowController.display();

    }
    /*
     * To sort the table on the columns selected in the group table
     */
    private void sortGroupTable () {
        String fieldNames[] = new String[groupVisibleColumns.length];
        boolean sort = false;
        for (int index=0;index<groupVisibleColumns.length; index++ ) {
            fieldNames[index] = columnFields[groupVisibleColumns[index]];
            sort = true;
        }
        if (sort) {
            if (fieldNames!=null && fieldNames.length > 0) {
                cvGrpTableData.sort(fieldNames,true);//Added for bug fix 1151 - 15-Feb-2005
                super.groupTableRowsUpdated();
            }
        }
    }
    
    /*
     * To sort the table on the columns selected in the group table
     */
    private void sortDetailsTable () {
        boolean sort = false;
        String fieldNames[] = new String[detailsVisibleColumns.length];
        for (int index=0;index<detailsVisibleColumns.length; index++ ) {
            sort = true;
            fieldNames[index] = columnFields[detailsVisibleColumns[index]];
        }
        if (sort) {
            cvCurrentDetailsData.sort(fieldNames,true);
            super.detailsTableRowsUpdated();
        }
        
    }
    /**
     * Checks whether the user has any of these rights :
     * VIEW_AWARD or MODIFY_AWARD or CREATE_AWARD and MAINTAIN_REPORTING
     * For this it makes a server call
     */
    private void getRights() 
        throws CoeusClientException {
        RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        requester.setFunctionType(GET_REPORT_RIGHTS);
        //Code added for Case#3388 - Implementing authorization check at department level
        //Check the user is having rights to view this award        
        requester.setDataObject(this.awardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + REP_REQ_SERVLET, requester);
        comm.send();
        responder = comm.getResponse();
        if (!responder.isSuccessfulResponse()) {
            throw new CoeusClientException(responder.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        HashMap hmRights = (HashMap)responder.getDataObject();

        Boolean boolCreateRight = (Boolean)hmRights.get(KeyConstants.CREATE_RIGHTS);
        if (boolCreateRight!=null) {
            hasCreateRight = boolCreateRight.booleanValue();
        }
        Boolean boolModifyRight = (Boolean)hmRights.get(KeyConstants.MODIFY_RIGHTS);
        if (boolModifyRight!=null) {
            hasModifyRight = boolModifyRight.booleanValue();
        }
        
        Boolean boolViewRight = (Boolean)hmRights.get(KeyConstants.VIEW_RIGHTS);
        if (boolViewRight!=null) {
            hasViewRight = boolViewRight.booleanValue();
        }
        
        Boolean boolMaintainRepRight = (Boolean)hmRights.get(KeyConstants.MAINTAIN_RIGHTS);
        if (boolMaintainRepRight!=null) {
            hasMaintainRepRight = boolMaintainRepRight.booleanValue();
        }
    }
    
    /**
     * To show the reporting requirements for te selected award.
     */
    public void showReportingReqmts(String awardNumber) {
        try {
            ReportingReqBaseWindowController reportingReqBaseWindowController =null;
            
            char mode = TypeConstants.DISPLAY_MODE;
            //Code added for Case#3388 - Implementing authorization check at department level - starts
            //Check the user is having rights to view this award
            this.awardNumber = awardNumber;
            try {
                getRights();
            } catch (CoeusClientException coeusClientException) {
                CoeusOptionPane.showDialog(coeusClientException);
            }
            if (!(hasCreateRight || hasModifyRight || hasViewRight )) {
                // You do not have rights to view Awards
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
                return;
            }
            //Code added for Case#3388 - Implementing authorization check at department level - ends
            //Check if reporting requirements is available
            RequesterBean requester = new RequesterBean();
            ResponderBean responder;
            
            requester.setDataObject(awardNumber);
            requester.setFunctionType(HAS_REPORTING_REQUIREMENT);
            AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + REP_REQ_SERVLET, requester);
            
            comm.send();
            responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
                boolean hasRepReq = ((Boolean)responder.getDataObject()).booleanValue();
                if(!hasRepReq) {
                    //Doesn't have Reporting Requirements
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(REP_REQ_NOT_AVAILABLE) + awardNumber);
                    return ;
                }else {
                    //check if any reporting Requirement Window is opened and >= 3
                    //Disp msg Cannot open more than 3 Award Reporting Requirement windows.
                    //Please close atleast one Award Reporting Requirement window.
                    boolean repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.DISPLAY_MODE, true, true);
                    if(repReqWinOpen) {
                        return ;
                    }
                    //else Check if this rep req is opened then bring it to front
                    
                    //check if any reporting Requirements is already opened in Modify Mode.
                    //Disp msg Only one Reporting Requirements window can be open in modify mode.
                    //Reporting Requirements window for award "+Award + " is already open in modify mode.
                    //Do you want to open Reporting Requirements for the selected award in display mode."
                    repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.MODIFY_MODE, false, true);
                    if(repReqWinOpen) {
                        int selected = CoeusOptionPane.showQuestionDialog("Only one Reporting Requirements window can be open in modify mode."+
                        "Reporting Requirements window for award " + awardNumber + " is already open in modify mode."+
                        "Do you want to open Reporting Requirements for the selected award in display mode.",
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                        if(selected == CoeusOptionPane.SELECTION_NO) {
                            return ;
                        }else {
                            mode = TypeConstants.DISPLAY_MODE;
                        }
                    }else {
                        // No reporting Requirements is opened in Modify Mode
                        getRights();
                        if (hasModifyRight) {
                            mode = TypeConstants.MODIFY_MODE;
                        } else {
                            if (!hasMaintainRepRight) {
                                mode = TypeConstants.DISPLAY_MODE;
                            } else {
                                mode = TypeConstants.MODIFY_MODE;
                            }
                        }
                    }
                    
                    //if Modify Mode then check if Award sheet is opened in Modes M, N, E,C,P.
                    //Award sheet for +s_mit_award_number + is open in modify mode.
                    //Cannot open reporting requirements window in modify mode when
                    //an award is being modified.\n Do you want to open reporting
                    //requirements window for the selected award in display mode
                    if(mode == TypeConstants.MODIFY_MODE) {
                        if(isAwardWindowOpen(awardNumber, CORRECT_AWARD, false)) {
                            //An Award is opened in Modify Mode
                            AwardBaseWindow awardBaseWindow = (AwardBaseWindow)mdiForm.getEditingFrame(CoeusGuiConstants.AWARD_BASE_WINDOW);
                            awardNumber = awardBaseWindow.getAwardBaseBean().getMitAwardNumber();
                            int selection = CoeusOptionPane.showQuestionDialog("Award sheet for " + awardNumber + " is open in modify mode."
                            +"Cannot open reporting requirements window in modify mode when"
                            +"an award is being modified.\n Do you want to open reporting "
                            +"requirements window for the selected award in display mode.",
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                            if(selection == CoeusOptionPane.SELECTION_NO) {
                                return ;
                            }else {
                                mode = TypeConstants.DISPLAY_MODE;
                            }//End if else selection
                        }
                    }
                    
                    //if award is locked then display Yes No Dialog
                    //"Do you want to open reporting requirements for the selected award in display mode.
                    if(mode == TypeConstants.MODIFY_MODE) {
                        requester.setFunctionType(IS_AWARD_LOCKED);
                        requester.setDataObject(awardNumber);
                        comm.send();
                        responder = comm.getResponse();
                        if(responder.isSuccessfulResponse()){
                            boolean awardLocked = ((Boolean)responder.getDataObject()).booleanValue();
                            if(awardLocked) {
                                CoeusOptionPane.showInfoDialog(responder.getMessage());
                                int selection = CoeusOptionPane.showQuestionDialog("Do you want to open reporting requirements for the selected award in display mode.",
                                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                                if(selection == CoeusOptionPane.SELECTION_NO) {
                                    return ;
                                }else {
                                    mode = TypeConstants.DISPLAY_MODE;
                                }
                            }
                        }else {
                            //Server Error
                            CoeusOptionPane.showInfoDialog(responder.getMessage());
                            return ;
                        }
                    }//End if Modify mode
                    
                }
            }else{
                CoeusOptionPane.showInfoDialog(responder.getMessage());
                return ;
            }
            
            AwardBean awardBean = new AwardBean();
            awardBean.setMitAwardNumber(awardNumber);
            reportingReqBaseWindowController = new ReportingReqBaseWindowController(awardBean, mode);
            reportingReqBaseWindowController.display();
        } catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    /** This method is used to check whether the given Award number is already
     * opened in the given mode or not.
     * @param refId refId - for award its Award Number.
     * @param mode mode of Form open.
     * @param displayMessage if true displays error messages else doesn't.
     * @return true if Award window is already open else returns false.
     */
    boolean isAwardWindowOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.AWARD_BASE_WINDOW, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            // try to get the requested frame which is already opened
            CoeusInternalFrame frame = mdiForm.getFrame(
            CoeusGuiConstants.AWARD_BASE_WINDOW,refId);
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            }
            return true;
        }
        return false;
    }
    
    /** This method is used to check whether the given Reporting Requirement is already
     * opened in the given mode or not.
     * @param refId refId - for Reporting Requirement its Award Number.
     * @param mode mode of Form open.
     * @param displayMessage if true displays error messages else doesn't.
     * @return true if Reporting Requirement window is already open else returns false.
     */
    boolean isRepReqWindowOpen(String refId, char mode, boolean displayMessage, boolean selectFrame) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            
            if(selectFrame) {
                // try to get the requested frame which is already opened
                CoeusInternalFrame frame = mdiForm.getFrame(
                CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, refId);
                if (frame != null){
                    try{
                        frame.setSelected(true);
                        frame.setVisible(true);
                    }catch (PropertyVetoException propertyVetoException) {
                        
                    }
                }
            }
            
            return true;
        }
        return false;
    }
    
    
    /**
     *  To display the search window
     */
    private void showSearchWindow() {
        try {
            coeusSearch = new CoeusSearch(mdiForm, "REPORTINGQUERYSEARCH" , 0);
            coeusSearch.showSearchWindow();
            javax.swing.JTable tblResults = coeusSearch.getEmptyResTable();
            java.util.Vector vecSearchyResults = coeusSearch.getResultRecords();
            /*if (vecSearchyResults == null) {
                return;
            }*/
            
            cvData = new CoeusVector();
            java.util.HashMap hmRowItem = null;
            AwardReportReqBean awardReportReqBean=null;
            if (vecSearchyResults != null) {
                int vecSize = vecSearchyResults.size();
                for (int index=0;index<vecSize;index++) {
                    hmRowItem = (java.util.HashMap)vecSearchyResults.elementAt(index);
                    if (hmRowItem!=null) {
                        awardReportReqBean = new AwardReportReqBean();
                        awardReportReqBean.setMitAwardNumber(hmRowItem.get("MIT_AWARD_NUMBER").toString());
                        awardReportReqBean.setPrincipleInvestigator(hmRowItem.get("PERSON_NAME").toString());
                        awardReportReqBean.setUnitNumber(hmRowItem.get("UNIT_NUMBER").toString());
                        awardReportReqBean.setUnitName(hmRowItem.get("UNIT_NAME").toString());
                        awardReportReqBean.setReportClassDescription(hmRowItem.get("REPORT_CLASS_DESCRIPTION").toString().trim());
                        awardReportReqBean.setReportDescription(hmRowItem.get("REPORT_DESCRIPTION").toString().trim());
                        awardReportReqBean.setFrequencyBaseDescription(hmRowItem.get("FREQUENCY_DESCRIPTION").toString());
                        if (hmRowItem.get("FREQUENCY_BASE")!=null) {
/*                             Case 4308:Due date not displayed in award reporting - Start
                            Search result is in yyyy/mm/dd format; Formatting due date to yyyy/mm/dd */
                            awardReportReqBean.setFrequencyBaseDate(hmRowItem.get("FREQUENCY_BASE").toString());
                            String strDate = hmRowItem.get("FREQUENCY_BASE").toString();
                            if(strDate!= null || (!strDate.equals(EMPTY))){
                                strDate = dtUtils.restoreDate(strDate, ":/.,|-");
                                java.text.SimpleDateFormat dtFormat = new java.text.SimpleDateFormat(SEARCH_RESULT_FORMAT);
                                java.util.Date date = dtFormat.parse(strDate.trim());
                                java.sql.Date dt = new java.sql.Date(date.getTime());
                                awardReportReqBean.setFrequencyBase(dt);
                            }else{
                                awardReportReqBean.setFrequencyBase(null);
                            }
//                             Case 4308:Due date not displayed in award reporting - End
                        }
                        awardReportReqBean.setFrequencyBaseDescription(hmRowItem.get("FREQ_BASE_DESCRIPTION").toString());
                        awardReportReqBean.setReportStatusDescription(hmRowItem.get("REPORT_STATUS_DESCRIPTION").toString());
                        if (hmRowItem.get("DUE_DATE")!=null) {
                           // BEGIN Case 2074 
                            String strDate = hmRowItem.get("DUE_DATE").toString();
                            if(strDate!= null || (!strDate.equals(EMPTY))){
                                strDate = dtUtils.restoreDate(strDate, ":/.,|-");
//                              Case 4308:Due date not displayed in award reporting - Start
//                                java.text.SimpleDateFormat dtFormat
//                                    = new java.text.SimpleDateFormat("MM/dd/yyyy");
                                java.text.SimpleDateFormat dtFormat = new java.text.SimpleDateFormat(SEARCH_RESULT_FORMAT);
//                                Case 4308:Due date not displayed in award reporting - End
                                java.util.Date date = dtFormat.parse(strDate.trim());
                                java.sql.Date dt = new java.sql.Date(date.getTime());
                                awardReportReqBean.setStringDueDate(dt);
                            }else{
                                awardReportReqBean.setStringDueDate(null);
                            }
                            // END Case 2074 
                        }
                        
                        if (hmRowItem.get("DESCRIPTION")!=null) {
                            awardReportReqBean.setOspDistributionDescription(hmRowItem.get("DESCRIPTION").toString());
                        }
                        if (hmRowItem.get("CONTACT_TYPE_DESCRIPTION")!=null) {
                            awardReportReqBean.setContact(hmRowItem.get("CONTACT_TYPE_DESCRIPTION").toString());
                        }

                        StringBuffer strBuffAddress= new StringBuffer();
                        Object lastName = hmRowItem.get("L_NAME");
                        Object orgn = hmRowItem.get("ORGN");
                        Object prefix = hmRowItem.get("PFIX");
                        Object suffix = hmRowItem.get("SUFIX");
                        Object firstName = hmRowItem.get("FNAME");
                        Object middleName = hmRowItem.get("MNAME");
                        if(lastName!=null && lastName.toString().trim().length()>0) {
                            strBuffAddress.append(lastName.toString());
                        } else if (orgn!=null && orgn.toString().trim().length()>0) {
                            strBuffAddress.append(orgn.toString());
                        }
                        if (suffix!=null && suffix.toString().trim().length()>0) {
                            strBuffAddress.append(" ");
                            strBuffAddress.append(suffix.toString());
                        }
                        if (prefix!=null && prefix.toString().trim().length()>0) {
                            strBuffAddress.append(" ");
                            strBuffAddress.append(prefix.toString());
                        }
                        if (firstName!=null && firstName.toString().trim().length()>0) {
                            strBuffAddress.append(" ");
                            strBuffAddress.append(firstName.toString());
                        }
                        if (middleName!=null && middleName.toString().trim().length()>0) {
                            strBuffAddress.append(" ");
                            strBuffAddress.append(middleName.toString());
                        }
                        
                        String strAddress = strBuffAddress.toString();
                        if (strAddress!=null && strAddress.trim().length()>0) {
                            awardReportReqBean.setAddress(strAddress);
                        }
                        
                        if(hmRowItem.get("FREQUENCY_DESCRIPTION")!=null) {
                            awardReportReqBean.setFrequencyDescription(hmRowItem.get("FREQUENCY_DESCRIPTION").toString());
                        }
                        if (hmRowItem.get("NUMBER_OF_COPIES")!=null) {
                            awardReportReqBean.setNumberOfCopies(Integer.parseInt(hmRowItem.get("NUMBER_OF_COPIES").toString()));
                        }
                        if (hmRowItem.get("OVERDUE_COUNTER")!=null) {
                            awardReportReqBean.setOverdueCounter(Integer.parseInt(hmRowItem.get("OVERDUE_COUNTER").toString()) );
                        }
                        if (hmRowItem.get("ACTIVITY_DATE")!=null) {
                            // BEGIN Case 2074 
                            //awardReportReqBean.setStringActivityDate(hmRowItem.get("ACTIVITY_DATE").toString());
                             String strDate = hmRowItem.get("ACTIVITY_DATE").toString();
                            if(strDate!= null || (!strDate.equals(EMPTY))){
                                strDate = dtUtils.restoreDate(strDate, ":/.,|-");
//                              Case 4308:Due date not displayed in award reporting - Start
//                                java.text.SimpleDateFormat dtFormat
//                                    = new java.text.SimpleDateFormat("MM/dd/yyyy");
                                java.text.SimpleDateFormat dtFormat = new java.text.SimpleDateFormat(SEARCH_RESULT_FORMAT);
//                              Case 4308:Due date not displayed in award reporting - End
                                java.util.Date date = dtFormat.parse(strDate.trim());
                                java.sql.Date dt = new java.sql.Date(date.getTime());
                                awardReportReqBean.setStringActivityDate(dt);
                            }else{
                                awardReportReqBean.setStringActivityDate(null);
                            }
                             // END Case 2074 
                        }
                        if (hmRowItem.get("COMMENTS")!=null) {
                            awardReportReqBean.setComments(hmRowItem.get("COMMENTS").toString());
                        }
                        if (hmRowItem.get("FULL_NAME")!=null) {
                            awardReportReqBean.setFullName(hmRowItem.get("FULL_NAME").toString());
                        }
                        if (hmRowItem.get("SPONSOR_CODE")!=null) {
                            awardReportReqBean.setSponsorCode(hmRowItem.get("SPONSOR_CODE").toString());
                        }
                        if (hmRowItem.get("SPONSOR_NAME")!=null) {
                            awardReportReqBean.setSponsorName(hmRowItem.get("SPONSOR_NAME").toString());
                        }
                        if (hmRowItem.get("SPONSOR_AWARD_NUMBER")!=null) {
                            awardReportReqBean.setSponsorAwardNumber(hmRowItem.get("SPONSOR_AWARD_NUMBER").toString());
                        }
                        if (hmRowItem.get("TITLE")!=null) {
                            awardReportReqBean.setTitle(hmRowItem.get("TITLE").toString());
                        }
                        
                        if (hmRowItem.get("ROLODEX_ID")!=null) {
                            awardReportReqBean.setRolodexId(Integer.parseInt(hmRowItem.get("ROLODEX_ID").toString()));
                        }
                        cvData.add(awardReportReqBean);
                    }
                } // For loop ends
                //modified by Nadh to avoid Unnecessary sortiing stats 16-June-2005
                if(cvData != null && cvData.size()>0){
                    super.setFormData(cvData);
                }
                sortGroupTable();
                sortDetailsTable();
                //Bug Fix 1933: Start 5
                if(cvGrpTableData.size() > 0){
                    reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
                }
                //Bug Fix 1933: End 6
            }//ends 16-June-2005
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * Closes the window
     */
    private void closeWindow() {
        mdiForm.removeFrame(CoeusGuiConstants.AWARD_REPORTING_REQ);
        closed=true;
        awardReportingReqBaseWindow.doDefaultCloseAction();
    }
    
    /**
     * Shows the customized view window
     */
    private void showCustomizedView() {
        if (customizeMaintRepReqForm == null) {
            customizeMaintRepReqForm = new CustomizeMaintRepReqForm();
        }
        int returnVal = customizeMaintRepReqForm.display();
        if (returnVal==customizeMaintRepReqForm.OK_CLICKED) {
            //Added for case#2268 - Report Tracking Functionality - start
            awardReportingReqBaseWindow.btnPrintAll.setEnabled(false);
            awardReportingReqBaseWindow.btnPrintSelected.setEnabled(false);
            //Added for case#2268 - Report Tracking Functionality - end
            groupColumns = customizeMaintRepReqForm.getGroupColumns();
            detailColumns = customizeMaintRepReqForm.getDetailColumns();
            super.customizeView(groupColumns, detailColumns);
            //groupVisibleColumns = super.getGroupVisibleColumns();
            //detailsVisibleColumns = super.getDetailsVisibleColumns();
            super.removeGroupListener();
            //For Bugfix 1151
            for (int indx = 0;indx<groupVisibleColumns.length;indx++) {
                String name = reportingRequirementsForm.tblGroup.getColumnName(indx);
                for (int counter = 0;counter<colNames.length;counter++) {
                    if(name.equals(colNames[counter])) {
                        groupVisibleColumns[indx] =counter;
                        break;
                    }
                }
            }
            if (groupColumns.length>1) {
                movingColumns(groupColumns,groupVisibleColumns,reportingRequirementsForm.tblGroup);
            }
            groupVisibleColumns = groupColumns;
            super.addGroupListener();
            
            super.removeDetailsListener();
            if (detailColumns.length>1) {
                movingColumns(detailColumns,detailsVisibleColumns,reportingRequirementsForm.tblDetails);
            }
            detailsVisibleColumns = detailColumns;
            super.addDetailsListener();
            sortGroupTable();
            sortDetailsTable();
        }
    }
    
    /*
     * This method will put the columns in the order which actually selected.
     */
    private void movingColumns(int orginalArray[],int transferedArray[],javax.swing.JTable table) {
        int tIndex=0;
        while (tIndex<transferedArray.length) {
            int elementAtIndex = transferedArray[tIndex];
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
                table.moveColumn(tIndex, orginalIndex);
                for (int tempIndex=tIndex;tempIndex<orginalIndex;tempIndex++) {
                    transferedArray[tempIndex] = transferedArray[tempIndex+1];
                }
                transferedArray[orginalIndex] = elementAtIndex;
            }else { //column not moved
                tIndex++;
            }
        }
    }

    public void mouseClicked(java.awt.event.MouseEvent me) {
        //Commented for case#2268 - Report Tracking Functionality - start
//        int row = reportingRequirementsForm.tblDetails.getSelectedRow();
//        int col = reportingRequirementsForm.tblDetails.getSelectedColumn();
//        int colIndex = detailsVisibleColumns[col];        
//        if (colIndex !=ADDRESS_COLUMN) {
//            return;
//        }        
//        if (me.getClickCount() == 2) {
//            try {
//                blockEvents(true);
//                displayRolodex(row);
//            } finally{
//                blockEvents(false);
//            }
//        }
        //Commented for case#2268 - Report Tracking Functionality - end
    }
    private void displayRolodex(int row) {
        AwardReportReqBean bean = (AwardReportReqBean) cvCurrentDetailsData.get(row);
        if(bean.getAddress().equals("")) {
            return;
        }
        RolodexMaintenanceDetailForm rolodexMaintenanceDetailForm=new RolodexMaintenanceDetailForm(ROLODEX_DISPLAY_MODE,""+bean.getRolodexId());
        rolodexMaintenanceDetailForm.showForm(mdiForm,"Display Rolodex",true);
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    public void vetoableChange(java.beans.PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(javax.swing.JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            closeWindow();
        }
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
     * Table Renderer
     */
    
    class ReportTableRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private int visibleColumns[];
        private char tableType;
        private int colIndex;
        private int rowIndex;
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
            rowIndex = row;
            if (tableType=='D') {
                columnIndex = detailsVisibleColumns[column];
            } else if (tableType=='G') {
                columnIndex = groupVisibleColumns[column];
            }
            colIndex = columnIndex;
            setHorizontalAlignment(javax.swing.JLabel.LEFT);
            //switch(columnIndex) {
            //setText(value.toString());
            //return this;
            return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
            
            //} //switch ends
        }
        
        // getTableCellRendererComponent() ends
    } //Table renderer ends here
    
}
